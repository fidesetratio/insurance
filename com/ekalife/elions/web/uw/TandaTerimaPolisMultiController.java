package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Ttp;
import com.ekalife.elions.model.User;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentMultiController;

/**
 * @author Yusuf
 * @since Jan 11, 2006
 */
public class TandaTerimaPolisMultiController extends ParentMultiController{

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		BindException err= new BindException(bindAndValidate(request, map, true));
		String snow_spaj = ServletRequestUtils.getStringParameter(request, "snow_spaj", "");
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        if(currentUser.getLde_id().equals("01") || currentUser.getLde_id().equals("11")){
            double angka = (double) currentUser.getScreenWidth() / (double) currentUser.getScreenHeight();
            if(angka > 1.4) {
            	map.put("wideScreen", true);
            }
        }
		if(request.getParameter("30hari") != null) {
			map.put("pesan", this.elionsManager.transferTandaTerimaPolisToKomisiOrFilling30Hari(currentUser,err));
		}
		List lsError=new ArrayList();
		for(int i=0;i<err.getErrorCount();i++){
			ObjectError value=(ObjectError)err.getGlobalErrors().get(i);
			Map param=new HashMap();
			param.put("value", value.getDefaultMessage());
			lsError.add(param);
		}
		map.put("err",lsError);
		List daftarSPAJ = this.uwManager.selectDaftarSPAJ("7", 1,null,null);
		map.put("daftarSPAJ", daftarSPAJ);
		map.put("snow_spaj", snow_spaj);
		return new ModelAndView("uw/ttp_main", map);
	}
/*
	public ModelAndView transfer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
		String spaj = request.getParameter("spaj");
		Date sysdate = this.elionsManager.selectSysdateSimple();

		if(this.elionsManager.validationPositionSPAJ(spaj) != 7) {
			map.put("errorPosisi", "true");
		}else {
			String ls_ket = " Print Commission ?";
			if ( this.elionsManager.selectCekKomisi(spaj) ==0 ) {
				ls_ket = " Filling ?";
			}
			map.put("keterangan", ls_ket);
	
			if(request.getParameter("transfer")!= null) {
					User currentUser = (User) request.getSession().getAttribute("currentUser");
					this.elionsManager.transferTandaTerimaPolisToKomisiOrFilling(spaj, currentUser.getLus_id(), defaultDateFormat.parse(request.getParameter("tanggal")));
					map.put("sukses", "true");
			}else {
				String pepesan = this.elionsManager.cekAgenTakBerpolis(spaj);
				if(pepesan != null) map.put("pepesan", pepesan);
			}
		}
		
		map.put("sysdate", sysdate);
		return new ModelAndView("uw/ttp_transfer", map);
	}
	*/
	/**Fungsi:	Controller untuk form utama proses checking ttp
	 * @param 	HttpServletRequest request, HttpServletResponse response
	 * @throws 	Exception
	 * @author 	Ferry Harlim
	 */
	public ModelAndView checking_ttp_main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		BindException err= new BindException(bindAndValidate(request, map, true));
		
        User currentUser = (User) request.getSession().getAttribute("currentUser");
		if(request.getParameter("30hari") != null) 
			map.put("pesan", this.elionsManager.transferTandaTerimaPolisToKomisiOrFilling30Hari(currentUser,err));
		
		List daftarSPAJ = this.uwManager.selectDaftarSPAJ1("9", 1,null,null);
		map.put("daftarSPAJ", daftarSPAJ);
        
		return new ModelAndView("uw/checking_ttp_main", map);
	}
	
	/**Fungsi:	Controller untuk melakukan proses transfer balik ke ttp
	 * @param 	HttpServletRequest request, HttpServletResponse response
	 * @throws 	Exception
	 * @author 	Ferry Harlim
	 */
	public ModelAndView checking_ttp_transfer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		Date tanggal = elionsManager.selectSysdate();
		Integer flag=ServletRequestUtils.getIntParameter(request, "flag");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String spaj=ServletRequestUtils.getStringParameter(request,"spaj",null);
		String ket=ServletRequestUtils.getStringParameter(request, "ket","");
		Integer li_pos=7;//Input Tanda Terima
		Integer suc=null;
//		cheking posisi polis
		Map mPosisi=elionsManager.selectF_check_posisi(spaj);
		String emailCabang = uwManager.selectEmailCabangFromSpaj(spaj);
		Integer lspdId=(Integer)mPosisi.get("LSPD_ID");
		String lspdPos=(String)mPosisi.get("LSPD_POSITION");
		Map infoAgen = elionsManager.selectEmailAgen(spaj);
		String emailAgen =(String) infoAgen.get("MSPE_EMAIL");
		
		if(emailAgen!=null) {
			if(!emailAgen.trim().equals("")) {
					emailAgen=emailCabang;
			}
		}

		if (lspdId==9){
			String mscoId=this.elionsManager.selectMstCommissionMscoId(spaj, 1, 1, 4);
			if(this.elionsManager.selectMstDeduct(mscoId, 1)==null){
				if(flag!=null){
					suc=1;
					//elionsManager.transferBalikToTtp(spaj, li_pos, currentUser, ket);
					
					String errorRekening = elionsManager.cekRekAgen2(spaj);
					if(errorRekening.equals("")) {
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
								null, 0, 0, new Date(), null, 
								false, props.getProperty("admin.ajsjava"),emailAgen /*props.getProperty("agensys.emails")*/.split(";"), null, null, 
								"[E-Lions] Rekening Agen "+errorRekening+" Kosong", 
								"Harap cek rekening agen: "+errorRekening+" karena rekeningnya masih kosong, sehingga komisinya tidak dapat diproses. Terima kasih."+
								"\n\nNo SPAJ: "+spaj+""+
								"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.",							
								null, spaj);
						map.put("msg", 
								"Rekening Agen ini masih kosong. Sistem telah mengirimkan email ke Agen Yang bersangkutan. Silahkan dikonfirmasi lebih lanjut dengan Agency Support");
						suc=3;
					}else {
						//Yusuf - 17 nov 08 - bila dari agency sudah ok, tidak dikirim ke uw (tanda terima) lagi, tapi langsung ke mba murni (komisi)
						elionsManager.prosesTransferKomisiOrFilling(spaj, 8, currentUser.getLus_id(), elionsManager.selectSysdate(), "TRANSFER KE PRINT COMMISSION (FINANCE).");
					}
				}
			}else{
				map.put("msg", "Ada Slip Potongan komisi silahkan transfer di Deduct");
				suc=3;
			}
		}else{
			map.put("msg", lspdPos);
			suc=2;
		}
		map.put("ket", ket);
		map.put("suc", suc);
		map.put("spaj",spaj);
		return new ModelAndView("uw/checking_ttp_transfer", map);
	}
	
	/**
	 * @deprecated dah gak di pake di ganti ke transfertandaterimapoliformcontroller
	 */
	public ModelAndView transferNew(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		String ket=null;
		String spaj = request.getParameter("spaj");
		Date sysdate = this.elionsManager.selectSysdateSimple();
		Integer proses=0;
		String info=null;
		if(this.elionsManager.validationPositionSPAJ(spaj) != 7) {
			map.put("errorPosisi", true);
		}
		Integer countComm=this.uwManager.selectCekKomisi(spaj);
		Integer levComm=4; //agen
		String fax=request.getParameter("fax");
		//proses fax
		//ket=("Terima TTP dalam bentuk fax ?");
		//proses=7;
		Map tertanggung= this.elionsManager.selectTertanggung(spaj);
		String lcaId=(String)tertanggung.get("LCA_ID");
		Integer flagKomisi=(Integer)tertanggung.get("MSTE_FLAG_KOMISI");
		if(flagKomisi==0){//awal ttp blom ada proses
			info="TTP baru datang";
		}else if(flagKomisi==1){//
			info="TTP balikan dari Agency";
		}else if(flagKomisi==2){
			
		}
		
		if ( countComm ==0 ) {//gak ada komisi
				//li_pos = 99;
				//ket = "TRANSFER KE Filling ?";
				//prosesTransferKomisiOrFilling(spaj, li_pos, currentUser.getLus_id(), tanggal, ket);
				ket="Tidak Punya Komisi, Transfer Ke Filling?";
			proses=1;
		}else{//ada komisi
			
			String msag = this.uwManager.selectAgenFromSpaj(spaj);
			List tmp = this.uwManager.selectAgenCekKomisi(msag);
			Date ldt_birth = new Date();
			String ls_nama = "";
			Double ldec_kom = new Double(0);
			String nmCabang= uwManager.selectNmCabangFromSpaj(spaj);
			int li_polis = 0 ;
			if( tmp.size() > 0 ) {
				ldt_birth = (Date) ((Map) tmp.get(tmp.size()-1)).get("MSPE_DATE_BIRTH");
				ls_nama = (String) ((Map) tmp.get(tmp.size()-1)).get("MCL_FIRST");
				ldec_kom = (Double) ((Map) tmp.get(tmp.size()-1)).get("KOMISI");
				li_polis = this.elionsManager.selectJumlahPolisAgen(ls_nama, FormatDate.toString(ldt_birth));
			}
			//
			String cabang="37,09,42,46,52";
			if(cabang.indexOf(lcaId)>-1){
				ket=("Kode cabang ("+cabang+")"+ "Yakin Transfer ke Finance");
				proses=5;
			}else{
//				ldec_kom=new Double(600000);
				if(flagKomisi.intValue()==1 ){//proses transfer balikan dari Agency
					ket="Ada Komisi, Anda Ingin Langsung Transfer ke Finance?";
					proses=2;
				}else if(flagKomisi.intValue()==2){//
					ket="Ada Komisi, TTP Asli telah Ada dan Anda Ingin Langsung Transfer ke Finance?";
					proses=3;
				}else if(ldec_kom.doubleValue()<500000 || li_polis>0){
					//agen sudah punya polis atau komisi <500rb, transfer ke Finance
					ket="Agen sudah punya polis atau komisi < 500 rb,\\nYakin Transfer ke Finance?";
					proses=4;
					String emailAdd= this.uwManager.selectEmailCabangTtp(lcaId);
					emailAdd= (emailAdd);
					String[] to = emailAdd.split(";");
					
					if (emailAdd== null||emailAdd.equals("")){
//						email.send(false, props.getProperty("admin.ajsjava"), props.getProperty("agensys.emails").split(";"), null, null, 
//								"[E-Lions] Agen "+ls_nama+ "Kode Agen" +msag+ " Belum memiliki Polis Pribadi",
//								"Harap agen: \n\n" + "Nama Agen: "+ls_nama+ "\n\n" + "Kode Agen :" +msag+  "\n\n" +"Cabang : " +nmCabang+ "\n\n" + "No. SPAJ:" +spaj+
//								"\n\n" +"Jika sudah punya polis pribadi konfirmasi ke Agency Support No polisnya. Terima kasih."+
//								"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null);
					}else{
//						email.send(false, props.getProperty("admin.ajsjava"), emailAdd.split(";"), null, null, 
//								"[E-Lions] Agen "+ls_nama+ "Kode Agen" +msag+ " Belum memiliki Polis Pribadi",
//								"Harap agen: \n\n" + "Nama Agen: "+ls_nama+ "\n\n" + "Kode Agen :" +msag+  "\n\n" +"Cabang : " +nmCabang+ "\n\n" + "No. SPAJ:" +spaj+
//								"\n\n" +"Jika sudah punya polis pribadi konfirmasi ke Agency Support No polisnya. Terima kasih."+
//								"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null);
					}
				}else{//transfer ke Proses Checking TTP{
					ket="Agen tidak punya polis atau komisi > 500 rb \\nAnda Ingin Transfer Polis ke Agent Commission(Agency Support)?";
					proses=6;
				}
			}	
		
		}
		
		//klo tidak error dan tombol transfer di tekan
		if((String)map.get("errorPosisi")!=null){
			if(request.getParameter("transfer")!= null) {	
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				
				//ket=this.elionsManager.transferTandaTerimaPolisToKomisiOrFillingNew(spaj, currentUser, defaultDateFormat.parse(request.getParameter("tanggal")),proses);
				map.put("sukses", "Berhasil "+ket);
			}
		}
		map.put("info", info);
		map.put("keterangan", ket);
		map.put("sysdate", sysdate);
		return new ModelAndView("uw/ttp_transfer_new", map);
	}
	
	/**Fungsi:	Untuk Mentransfer polis langsung ke Agency Support
	 * @param 	HttpServletRequest request, HttpServletResponse response
	 * @return 	ModelAndView
	 * @throws 	Exception
	 * @authors	Ferry Harlim
	 */
	public ModelAndView transferToAs(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Map map = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj=request.getParameter("spaj");
		Date tanggal = elionsManager.selectSysdate();
		String transfer=request.getParameter("transfer");
		Date sysdate = this.elionsManager.selectSysdateSimple();
		String emailCabang = uwManager.selectEmailCabangFromSpaj(spaj);
		map.put("sysdate", sysdate);
//		cheking posisi polis
		Map mPosisi=elionsManager.selectF_check_posisi(spaj);
		Integer lspdId=(Integer)mPosisi.get("LSPD_ID");
		String lspdPos=(String)mPosisi.get("LSPD_POSITION");
		Map infoAgen = elionsManager.selectEmailAgen(spaj);
		String emailAgen = (String) infoAgen.get("MSPE_EMAIL");
		if(lspdId!=7){
			map.put("pos", "Posisi Polis ada di "+lspdPos);
		}
		if(emailAgen!=null) {
			if(!emailAgen.trim().equals("")) {
					emailAgen=emailCabang;
			}
		}
		if(transfer!=null){
			Ttp ttp=new Ttp();
			Map tertanggung= this.elionsManager.selectTertanggung(spaj);
			List <Map> inbox = uwManager.selectMstInbox(spaj, "1");
			Map dataInbox = inbox.get(0);
			ttp.setFlagKomisi((Integer)tertanggung.get("MSTE_FLAG_KOMISI"));
			ttp.setMsagId((String)tertanggung.get("MSAG_ID"));
			if(ttp.getFlagKomisi()==0 && ((BigDecimal) dataInbox.get("LSPD_ID")).intValue()==201){
				String errorRekening = elionsManager.cekRekAgen2(spaj);
				if(errorRekening.equals("")) {
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, new Date(), null, 
							false, props.getProperty("admin.ajsjava"), emailAgen/*props.getProperty("agensys.emails")*/.split(";"), null, null, 
//			     	email.send(false, props.getProperty("admin.ajsjava"), props.getProperty("admin.dian").split(";"), null, null, 
							"[E-Lions] Rekening Agen "+errorRekening+" Kosong", 
							"Harap cek rekening agen: "+errorRekening+" karena rekeningnya masih kosong, sehingga komisinya tidak dapat diproses. Terima kasih."+
							"\n\nNo SPAJ: "+spaj+""+
							"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.",							
							null, spaj);
					map.put("error", 
							"Rekening Agen ini masih kosong. Sistem telah mengirimkan email ke Agen Yang bersangkutan. Silahkan dikonfirmasi lebih lanjut dengan Agency Support");
				}else {				
					ttp.setSpaj(spaj);
					ttp.setProses(8);
					elionsManager.transferTandaTerimaPolisToKomisiOrFillingNew(ttp, currentUser);
					map.put("pepesan", "Berhasil Transfer");
				}
			}else{
				if(ttp.getFlagKomisi()==1)
					map.put("error","Status TTP adalah Balikan dari Agency, Tidak BIsa transfer.");
				else if(ttp.getFlagKomisi()==2)
					map.put("error","TTP dalam bentuk fax,Tidak bisa transfer dengan tombol  'Transfer To AS'");
				
				if(((BigDecimal) dataInbox.get("LSPD_ID")).intValue()==202){
					map.put("error","Transfer Polis tidak bisa dilakukan, karena belum melakukan Transfer dokumen ke Imaging.");
				}
			}
		}
		return new ModelAndView("uw/transferToAs", map);
	}
}