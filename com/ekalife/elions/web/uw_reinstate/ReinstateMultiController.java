package com.ekalife.elions.web.uw_reinstate;

import java.io.File;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mail.MailException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Reinstate;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentMultiController;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.spring.util.Email;

public class ReinstateMultiController extends ParentMultiController {

	DecimalFormat f3 = new DecimalFormat ("000");
	SimpleDateFormat sdfTime=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	/*
	protected Connection connection = null;
	protected Connection getConnection() {
		if(this.connection==null) {
			try {
				this.connection = this.elionsManager.getUwDao().getDataSource().getConnection();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		}
		return this.connection;
	}	
	*/
	
	public void setEmail(Email email) {this.email = email;}
	public void send(boolean isHtml, String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments) throws MailException, MessagingException{
		this.email.send(isHtml, from, to, cc, bcc, subject, message, attachments);
	}	
	
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		List list;
		list=elionsManager.selectDaftarReinstate(18,106,"","");
		map.put("daftarSpaj",list);
		
		return new ModelAndView("uw_reinstate/reinstate", "cmd", map);
	}	
		
	public ModelAndView cari(HttpServletRequest request, HttpServletResponse response) throws Exception {
			Map map = new HashMap();
			if(request.getParameter("search")!=null){
				List list = this.elionsManager.selectDaftarReinstate(18,106,
						ServletRequestUtils.getStringParameter(request, "kata", ""), 
						ServletRequestUtils.getStringParameter(request, "kategori", ""));
				map.put("listSpaj", list);
			}
			return new ModelAndView("uw_reinstate/cari_reinstate", "cmd", map);
	}	

	public ModelAndView back_to_reins(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		String nomor=request.getParameter("nomor");
		String spaj=nomor.substring(0,nomor.indexOf("-"));
		String reinsNo=nomor.substring(nomor.indexOf("~")+1,nomor.length());
		//String noPolis=nomor.substring(nomor.indexOf("-")+1,nomor.indexOf("~"));
		map.put("spaj",spaj);
		List lsReins=elionsManager.selectMstReinstateLspdId(spaj);
		Integer liLspd=(Integer)lsReins.get(0);
		List list = this.elionsManager.selectDaftarReinstate(18,106,reinsNo,"2");//kategori 1=nopolis 2=noreinstate
		if(list.isEmpty()==false){
			Reinstate reins=(Reinstate)list.get(0);
			if(liLspd.intValue()==106)
				map.put("info",new Integer(1));
			else
				elionsManager.prosesBackToReinstate(spaj,reins.getReinsNo());
		}else
			map.put("info",new Integer(2));
		
		return new ModelAndView("uw_reinstate/backtoreins","cmd",map);
	}	

	public ModelAndView transfer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		
		String nomor=request.getParameter("nomor");
		String spaj=nomor.substring(0,nomor.indexOf("-"));
		String reinsNo=nomor.substring(nomor.indexOf("~")+1,nomor.length());
		//String noPolis=nomor.substring(nomor.indexOf("-")+1,nomor.indexOf("~"));
		
		Map mReinsate=new HashMap();
		Integer liAccept,liReas,liPrint;
		Date ldtTgl;
		boolean pro=true;
		int info=0;
		mReinsate=(HashMap)elionsManager.selectUwReinstate(spaj,reinsNo);
		if(mReinsate!=null){
			liAccept=(Integer)mReinsate.get("MSUR_ACCEPT");
			liReas=(Integer)mReinsate.get("LSPD_ID");
			liPrint=(Integer)mReinsate.get("MSUR_PRINT");
			ldtTgl=(Date)mReinsate.get("MSUR_TGL_BATAS_PAID");
			Date tgl_spaj_doc = (Date) mReinsate.get("TGL_BERKAS_LENGKAP_UW");
			//
			if( liPrint.intValue() != 1 ){
				info=1;
				pro=false;
				
	//			MessageBox('WARning','Surat Konfirmasi Belum Dicetak.!!!'+'~n~r'+&
	//										'TIDAK BISA DITRANSFER..')
	//			RETURN 
//			}else if( liAccept.intValue() == 0){
//				info=2;
//				pro=false;
	//			MessageBox('WARning','Reinstate Polis ini di tolak.!!!'+'~n~r'+&
	//										'TIDAK BISA DITRANSFER..')
	//			RETURN 
			}else if( liReas.intValue() != 106){
				info=3;
				pro=false;
	//			MessageBox('ERROR TransferPolis','Proses REAS belum dijalankan !!!')
	//			Return;
			}else if(ldtTgl==null) {
				info=4;
				pro=false;
	//			MessageBox('ERROR TransferPolis','Tanggal Batas Pembayaran Belum di INPUT !!!')
	//			Return;
//			}else if(tgl_spaj_doc == null) {
//				info=6;
//				pro=false;
	//			MessageBox('ERROR TransferPolis','Tanggal SPAJ Doc Belum Diinput !!!')
	//			Return;
			}
			if(pro){
				User currentUser = (User) request.getSession().getAttribute("currentUser");
					elionsManager.prosesTransferReinstate(spaj,reinsNo, currentUser.getLus_id());
			}	
		}else
			info=5;
		
		map.put("spaj",spaj);
		map.put("reins",reinsNo);
		map.put("info",new Integer(info));
		return new ModelAndView("uw_reinstate/transreins","cmd",map);
	}	
	
	/**@deprecated
	 */
	public ModelAndView surat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		String nomor=request.getParameter("nomor");
		String spaj=nomor.substring(0,nomor.indexOf("-"));
		String reinsNo=nomor.substring(nomor.indexOf("~")+1,nomor.length());
		//String noPolis=nomor.substring(nomor.indexOf("-")+1,nomor.indexOf("~"));
		int info=0;
		List list = this.elionsManager.selectDaftarReinstate(18,106,reinsNo,"2");//kategori 1=nopolis 2=noreinstate
		if(list.isEmpty()==false){
			Reinstate reins=(Reinstate)list.get(0);
			Integer li_print=elionsManager.selectMstUwReinstateMsurPrint(spaj,reins.getReinsNo());
			User currentUser = (User) request.getSession().getAttribute("currentUser");        
			
			if(li_print.intValue()==0){
				elionsManager.prosesPrintSuratKonfirmasiPemulihanPolis(spaj,reins.getReinsNo(),currentUser.getLus_id(),new Integer(1),currentUser);
			}else{
				info=1;
				//err.reject("","Surat Ini Sudah Pernah Dicetak.!! Tidak Bisa Dicetak Ulang");
			}
		}else
			info=2;
		
		map.put("spaj",spaj);
		map.put("reins",reinsNo);
		map.put("info",new Integer(info));
		return new ModelAndView("uw_reinstate/surat","cmd",map);
	}
	
	public ModelAndView cetakulang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		String spaj=request.getParameter("spaj");
		String reins=request.getParameter("reins");
		String desc=request.getParameter("desc");
		String flag=request.getParameter("flag");
		String inPass=request.getParameter("inpass");
		User currentUser = (User) request.getSession().getAttribute("currentUser");        
		int info=0;
		int userId=Integer.parseInt(currentUser.getLus_id());
		map.put("spaj",spaj);
		map.put("pass",inPass);
		
		//awal panggil
		if(flag==null || flag.equals("") || flag.equals("0")){
			//validasi user yang boleh edit surat konfirmasi pemulihan polis
			if(userId==14||userId==519||userId==716||userId==1504||userId==717){
				info=0;
			}else{
				info=1;
			}

			map.put("info",new Integer(info));
			return new ModelAndView("uw_reinstate/cetakulang","cmd",map);

		}else{
			currentUser.setPass(inPass);
			currentUser=elionsManager.selectLoginAuthentication(currentUser);
			if(currentUser==null){
				info=2;
				map.put("info",new Integer(info));
				return new ModelAndView("uw_reinstate/cetakulang","cmd",map);
			}else if(desc==null || desc.equals("")){
				info=3;
				map.put("info",new Integer(info));
				return new ModelAndView("uw_reinstate/cetakulang","cmd",map);
			}else{
				info=4;
				Date today=elionsManager.selectSysdate(new Integer(2));
				String msg="Re-edit "+sdfTime.format(today)+ " by "+currentUser.getLus_id()+" "+desc;
				elionsManager.prosesCetakUlangSuratKonfirmasi(spaj,reins,new Integer(0),null,msg);
				map.put("info",new Integer(info));
				return new ModelAndView("uw_reinstate/cetakulang","cmd",map);
			}
		
		}
	}

	/**
	 * Fungsi untuk isi tanggal spaj doc uw reinstate dan tanggal kirim ke LB
	 * 
	 * @author Yusuf
	 * @since Aug 26, 2008 (9:08:31 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView update_tanggal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Date now 			= new Date();
		String sysdate 		= defaultDateFormat.format(now);
		DateFormat dfh 		= new SimpleDateFormat("HH");
		DateFormat dfm 		= new SimpleDateFormat("mm");
		
		User currentUser 	= (User) request.getSession().getAttribute("currentUser");
		String tanggal 		= ServletRequestUtils.getStringParameter(request, 			"tanggal", sysdate);
		String hh 			= ServletRequestUtils.getStringParameter(request, 			"hh", dfh.format(now));
		String mm 			= ServletRequestUtils.getStringParameter(request, 			"mm", dfm.format(now));
		int flag 			= ServletRequestUtils.getRequiredIntParameter(request, 		"flag");
		
		Map params 			= new HashMap();
		
		String nomor=request.getParameter("nomor");
		String spaj=nomor.substring(0,nomor.indexOf("-"));
		String reinsNo=nomor.substring(nomor.indexOf("~")+1,nomor.length());
		String noPolis=nomor.substring(nomor.indexOf("-")+1,nomor.indexOf("~"));

		//update tanggal spaj doc
		if(flag == 1) {
			if(request.getParameter("save") != null) {
				String pesan = elionsManager.saveMstTransHistory(spaj, reinsNo, "TGL_BERKAS_LENGKAP_UW", tanggal, hh, mm, "USER_BERKAS_LENGKAP_UW", currentUser.getLus_id());
				params.put("pesan", pesan);
			}
		}

		//send params to JSP
		params.put("listHH", 		new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23});
		params.put("listMM", 		new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59});
		params.put("tanggal", 		tanggal);
		params.put("hh", 			hh);
		params.put("mm", 			mm);
		params.put("flag", 			flag);
		
		return new ModelAndView("uw_reinstate/update_tanggal", params);
	}
	
	public ModelAndView penawaran(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map params = new HashMap();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj");
		params.put("spaj", spaj);
		
		if(request.getParameter("send") != null) {
			//no surat
			Integer counter = 1;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyM");
			List<HashMap> noCounter = uwManager.selectNoSuratPenawaran();
			String yearMon = sdf.format(elionsManager.selectSysdate());
			Map MPp = (HashMap) elionsManager.selectPemegang(spaj);
			if(Integer.parseInt(yearMon.substring(4)) == Integer.parseInt(noCounter.get(1).get("MSCO_VALUE").toString())) {
				counter = Integer.parseInt(noCounter.get(0).get("MSCO_VALUE").toString())+1;
			}
			List<Integer> addCounter = new ArrayList<Integer>();
			addCounter.add(0, counter);
			addCounter.add(1, Integer.parseInt(yearMon.substring(4)));
			addCounter.add(2, Integer.parseInt(yearMon.substring(0,4)));
			
			uwManager.updateNoSuratPenawaran(addCounter);
			
			params.put("no_surat", FormatString.rpad("0", counter.toString(),3)+"/EM_RE/UND/"+FormatNumber.angkaRomawi(yearMon.substring(4))+"/"+yearMon.substring(2,4));
			params.put("em", ServletRequestUtils.getStringParameter(request, "em"));
			params.put("premi", ServletRequestUtils.getStringParameter(request, "premi"));
			
			
			String mssg = "";
			String email = ServletRequestUtils.getStringParameter(request, "email","");
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String outputDir = props.getProperty("pdf.dir.report") + "\\penawaran_reins_email\\" + spaj.substring(0,2) + "\\" + spaj + "\\";
			String outputFilename = spaj+"_Penawaran_Reins.pdf";
			
			Connection conn = null;
			try {
				conn = this.elionsManager.getUwDao().getDataSource().getConnection();
				JasperUtils.exportReportToPdf(props.getProperty("report.uw.surat_penawaran") + ".jasper", outputDir, outputFilename, params, conn, PdfWriter.AllowPrinting, null, null);
			}catch(Exception e){
				this.elionsManager.getUwDao().closeConn(conn);
	            throw e;
			}finally{
				this.elionsManager.getUwDao().closeConn(conn);
			}
						
			String [] to = new String[] {"fouresta@sinarmasmsiglife.co.id","eko.arianto@sinarmasmsiglife.co.id","ali@sinarmasmsiglife.co.id"};
			String [] cc = new String[] {}; 
			if(!email.equals("")) {
				cc = to;
				to = new String[] {email};
			}
			List<File> attachments = new ArrayList<File>();
			attachments.add(new File(outputDir + "\\" + outputFilename));
			send(true, 
					currentUser.getEmail(), 
					to,
					
					//new String[] {"yusup_a@sinarmasmsiglife.co.id"},
					//new String[]{"ingrid@sinarmasmsiglife.co.id","rachel@sinarmasmsiglife.co.id",currentUser.getEmail()},
					cc,
					null,
					"Extra premi reinstate polis no "+MPp.get("MSPO_POLICY_NO") + " a/n " + MPp.get("MCL_FIRST"), 
					mssg, attachments);
			
			params.put("email", email);
			params.put("success", "Email telah dikirim");			
		}
		
		
		return new ModelAndView("uw_reinstate/penawaran",params);
	}
}