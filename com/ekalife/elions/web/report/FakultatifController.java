package com.ekalife.elions.web.report;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mail.MailException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Fakultatif;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Reinsurer;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentJasperReportingController;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;

public class FakultatifController extends ParentJasperReportingController {
//protected Connection connection = null;
private Email email;

	public void setEmail(Email email) {this.email = email;}
	public void send(boolean isHtml, String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments) throws MailException, MessagingException{
		this.email.send(isHtml, from, to, cc, bcc, subject, message, attachments);
	}	
	
//LAYAR UTAMA
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Fakultatif fakultatif=new Fakultatif();
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj","");
		
		/*List<DropDown> cp_reas=new ArrayList<DropDown>();
		cp_reas.add(new DropDown("1","TRINITY RE"));
		cp_reas.add(new DropDown("2","REINDO"));
		cp_reas.add(new DropDown("3","TUGURE"));
		cp_reas.add(new DropDown("4","MAREIN"));
		cp_reas.add(new DropDown("5","NASRE"));
		cp_reas.add(new DropDown("6","REINDO SYARIAH"));*/
		List<DropDown> cp_reas = uwManager.selectLsInsurer();
		cp_reas.add(new DropDown("0","LAINNYA......"));
		
		map.put("cp_reas", cp_reas);
		map.put("fak", fakultatif);
		map.put("spaj", spaj);
		return new ModelAndView("uw/fakultatif", "cmd", map);
	}

	public ModelAndView cetak_surat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String reasurder="";
		String email="";
		String fax="";
		String telepon="";
		String cp="";
		String send = ServletRequestUtils.getStringParameter(request, "send","");
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj","");		
		String reasurderSelect=ServletRequestUtils.getStringParameter(request, "reasurderSelect","");
		String lampiran="";
		String retensi = ServletRequestUtils.getStringParameter(request, "retensi","");
		Integer row = ServletRequestUtils.getIntParameter(request, "row",0);
		
		if(spaj.equals("")) {
			ServletOutputStream out = response.getOutputStream();
			out.println("<script>alert('Harap masukkan nomor Polis / SPAJ.');window.close();</script>");
			out.flush();
			return null;
		}
		
		if(reasurderSelect.equals("")){
			ServletOutputStream out = response.getOutputStream();
			out.println("<script>alert('Harap pilih reasurder.');window.close();</script>");
			out.flush();
			return null;
		}
		
		if(request.getParameter("L3")!=null){
			if(request.getParameter("L3").replace(" ", "").equals("")){
				ServletOutputStream out = response.getOutputStream();
				out.println("<script>alert('Harap isi lampiran lainnya..');window.close();</script>");
				out.flush();
				return null;
			}
		}
		
		int count=0;
		for (int i = 1; i <= 3; i++) {
			if(count==0){
				if(request.getParameter("L"+i)!=null){
					if(i==3){
						String[]breakL3=request.getParameter("L3").split("-");
						if(breakL3.length==0) { lampiran += "- "+request.getParameter("L"+i).trim();
						}else{
							for (int j = 0; j < breakL3.length; j++) {
								if(j==0) lampiran += "- "+breakL3[j].trim(); 
								else lampiran += "\n- "+breakL3[j].trim(); 
							}
						}
					}else lampiran += "- "+request.getParameter("L"+i);
					
					count++;
				}
			}else{
				if(request.getParameter("L"+i)!=null){
					if(i==3){
						String[]breakL3=request.getParameter("L3").split("-");
						for (int j = 0; j < breakL3.length; j++) { lampiran=lampiran+"\n- "+breakL3[j].trim(); }
					}else lampiran=lampiran+"\n- "+request.getParameter("L"+i); 
					
					count++;
				}
			}
		}
		
		if(reasurderSelect.equals("0")){
			reasurder=ServletRequestUtils.getStringParameter(request, "reasurder","");
			email=ServletRequestUtils.getStringParameter(request, "email","");
			fax=ServletRequestUtils.getStringParameter(request, "fax","");
			telepon=ServletRequestUtils.getStringParameter(request, "telepon","");
			cp=ServletRequestUtils.getStringParameter(request, "cp","");
			
			if(reasurder.equals("")){
				ServletOutputStream out = response.getOutputStream();
				out.println("<script>alert('Maaf nama reasurder belum diisi.');window.close();</script>");
				out.flush();
				return null;
			}
			
			if(cp.equals("")){
				ServletOutputStream out = response.getOutputStream();
				out.println("<script>alert('Maaf nama contact person reasurder belum diisi.');window.close();</script>");
				out.flush();
				return null;
			}
			
			if(email.equals("")){
				ServletOutputStream out = response.getOutputStream();
				out.println("<script>alert('Maaf email reasurder belum diisi. Bila memang tidak ada isi dengan tanda dash \"-\"');window.close();</script>");
				out.flush();
				return null;
			}
			
			if(fax.equals("")){
				ServletOutputStream out = response.getOutputStream();
				out.println("<script>alert('Maaf no fax reasurder belum diisi. Bila memang tidak ada isi dengan tanda dash \"-\"');window.close();</script>");
				out.flush();
				return null;
			}
			
			if(telepon.equals("")){
				ServletOutputStream out = response.getOutputStream();
				out.println("<script>alert('Maaf no telepon reasurder belum diisi. Bila memang tidak ada isi dengan tanda dash \"-\"');window.close();</script>");
				out.flush();
				return null;
			}
		}else{
			/*String reasProp=props.getProperty("cp.reas."+reasurderSelect);
			String []reasBreak=reasProp.split("\\|");
			reasurder=reasBreak[1];
			cp=reasBreak[2];
			email=reasBreak[3];
			fax=reasBreak[4];
			telepon=reasBreak[5];*/
			
			Reinsurer reinsurer = uwManager.getDataReinsurer(Integer.parseInt(reasurderSelect));
			reasurder = reinsurer.getLsre_nama();
			cp = reinsurer.getLsre_contact();
			email = reinsurer.getLsre_email();
			fax = reinsurer.getLsre_fax();
			telepon = reinsurer.getLsre_telpon();
		}
		
		Map params = new HashMap();
		params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.fakultatif"));		
		params.put("lampiran",lampiran);
		params.put("reasurder", reasurder);
		params.put("cp", cp);
		params.put("email", email);
		params.put("fax", fax);
		params.put("telepon", telepon);
		
		
		// no surat
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Integer noSurat = uwManager.selectNoSuratAtDb();
		String yearMon = sdf.format(elionsManager.selectSysdate());
		String temp = noSurat.toString().substring(6);
		if(yearMon.equals(noSurat.toString().substring(0,6))) {
			temp = temp.replace("0", "");
			Integer counter = Integer.parseInt(temp.replace("0", ""))+1;
			temp =  new String();
			for(int a=0;a<(3-counter.toString().length());a++) temp ="0"+temp;
			temp = temp + counter;
		}
		else temp = "001";
		params.put("no_surat", temp+"/UND-IND/"+FormatNumber.angkaRomawi(noSurat.toString().substring(4,6))+"/"+noSurat.toString().substring(0,4));
		uwManager.addCounterNoSurat(yearMon+temp);
		
		Map mTt = (HashMap) elionsManager.selectTertanggung(spaj);
		Map MPp = (HashMap) elionsManager.selectPemegang(spaj);
		Integer lstb_id = uwManager.getLstbId(spaj);
		
		if (mTt==null){
			ServletOutputStream out = response.getOutputStream();
			out.println("<script>alert('No. Polis / Reg SPAJ tidak ditemukan');window.close();</script>");
			out.flush();
			return null;
		}
		List lsProduk=elionsManager.selectMstProductInsured(spaj);	
		DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
		Product produk=(Product)lsProduk.get(0);
		Map mDataUsulan = (HashMap) uwManager.selectDataUsulan3(spaj);
		
		if (mDataUsulan.isEmpty()){
			ServletOutputStream out = response.getOutputStream();
			out.println("<script>alert('Tolong hubungi ITwebandmobile@sinarmasmsiglife.co.id, Data Usulan Tidak Lengkap');window.close();</script>");
			out.flush();
			return null;
		}
			
		if (mDataUsulan.get("LSBS_ID") == null){
			ServletOutputStream out = response.getOutputStream();
			out.println("<script>alert('Tolong hubungi ITwebandmobile@sinarmasmsiglife.co.id, Bisnis Id tidak ada ');window.close();</script>");
			out.flush();
			return null;
		}
			
		String tglLahir = "";	
		/*if(lstb_id == 1) {
			params.put("nama_ttg", MPp.get("MCL_FIRST"));
			params.put("bod_ttg", MPp.get("MSPE_DATE_BIRTH"));
			params.put("nama_product", produk.getLsdbs_name());
			params.put("UP", produk.getLku_symbol()+" "+ df.format((Double)mDataUsulan.get("MSPR_TSI")));
			params.put("lama_pertanggungan", produk.getMspr_ins_period());
			params.put("beg_date", produk.getMspr_beg_date());
			String medis=((Integer) mDataUsulan.get("MSTE_MEDICAL")==1?"Medis":"Non Medis");
			params.put("kon_pertanggungan", medis);
			params.put("pekerjaan", MPp.get("MKL_KERJA"));
			params.put("spaj", spaj);
			params.put("usia_tt",MPp.get("MSTE_AGE"));
			params.put("tgl_surat", elionsManager.selectSysdate());			
			
			tglLahir = new SimpleDateFormat("yyyyMMdd").format((Date)MPp.get("MSPE_DATE_BIRTH"));
		}
		else if(lstb_id == 2) {*/
			params.put("nama_ttg", mTt.get("MCL_FIRST"));
			params.put("bod_ttg", mTt.get("MSPE_DATE_BIRTH"));
			params.put("nama_product", produk.getLsdbs_name());
			params.put("UP", produk.getLku_symbol()+" "+ df.format((Double)mDataUsulan.get("MSPR_TSI")));
			params.put("lama_pertanggungan", produk.getMspr_ins_period());
			params.put("beg_date", produk.getMspr_beg_date());
			String medis=((Integer) mDataUsulan.get("MSTE_MEDICAL")==1?"Medis":"Non Medis");
			params.put("kon_pertanggungan", medis);
			params.put("pekerjaan", mTt.get("MKL_KERJA"));
			params.put("spaj", spaj);
			params.put("usia_tt",mTt.get("MSTE_AGE"));
			params.put("tgl_surat", elionsManager.selectSysdate());			
			
			tglLahir = new SimpleDateFormat("yyyyMMdd").format((Date)mTt.get("MSPE_DATE_BIRTH"));			
		//}
		
		

		//retensi & rider name
		List<Map> filters = new ArrayList();
		List<SortedMap> akumulasi = new ArrayList();
		BigDecimal tot = new BigDecimal(0);
		String riderName = "-";
		Map x = new HashMap();
		x.put("REG_SPAJ", spaj);
		filters.add(x);
		akumulasi = this.uwManager.selectAkumulasiPolisBySpajListNew(filters,0,false,"00/00/0000",null);
		Integer ctt = 1;
		JasperScriptlet js = new JasperScriptlet();
		for(SortedMap map : akumulasi) {
			if(map.get("A").toString().equals("&nbsp;")) continue;
			if(map.get("E1") != null && map.get("E1").toString().equals("Total Retensi Life")) {
				tot = new BigDecimal(750000000).subtract(new BigDecimal(map.get("W").toString()));
			}
			if(Integer.parseInt(map.get("O").toString()) > 300 && lstb_id == 1) {
				if(ctt == 1) riderName += map.get("V").toString().trim()+ " UP. " + map.get("D").toString() + " " + js.format2Digit(new BigDecimal(map.get("E").toString()));
				else riderName += ","+map.get("V").toString().trim()+ " UP. " + map.get("D").toString() + " " + js.format2Digit(new BigDecimal(map.get("E").toString()));
				ctt++;
			}
		}
		params.put("rider", riderName);
		if(!retensi.equals(""))params.put("retensi", retensi);
		else params.put("retensi", tot.toString());
		
		filters = new ArrayList<Map>();
		akumulasi = new ArrayList<SortedMap>();
		
		int akumCount = 0;
		Boolean inputAkum = false;
		for(int a = 1;a <= row;a++) {
			SortedMap xy = new TreeMap();
			xy.put("C", ServletRequestUtils.getStringParameter(request, "noPol"+a,""));
			xy.put("V", ServletRequestUtils.getStringParameter(request, "prod"+a,""));
			xy.put("D", ServletRequestUtils.getStringParameter(request, "kurs"+a,""));
			xy.put("E", new BigDecimal(ServletRequestUtils.getStringParameter(request, "up"+a,"")));
			xy.put("W", new BigDecimal(ServletRequestUtils.getStringParameter(request, "ret"+a,"")));
			if(!xy.get("C").toString().equals("") && !xy.get("E").toString().equals("") && !xy.get("W").toString().equals("")) {
				akumulasi.add(akumCount++,xy);
				inputAkum = true;
			}
		}
		
		if(!inputAkum) {
			filters = this.uwManager.selectFilterSpajNew("3", (String)MPp.get("MCL_FIRST"), tglLahir, "LIKE%",false);
			List<SortedMap> data = this.uwManager.selectAkumulasiPolisBySpajListNew(filters,0,false,"00/00/0000",null);
			for(SortedMap map : data) {
				//if(map.get("A").toString().equals("&nbsp;") || (!map.get("A").toString().equals("") && map.get("A1") != null)) continue;
				if(!map.get("A").toString().equals("&nbsp;") && Integer.parseInt(map.get("L").toString()) != 10) {
					map.put("C", map.get("C").toString());
					map.put("V", map.get("V").toString().trim());
					map.put("E", new BigDecimal(map.get("E").toString()));
					map.put("W", new BigDecimal(map.get("W").toString()));
					akumulasi.add(map);				
				}
			}			
		}

		if(!send.equals("")) {
			String mssg = "";
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String outputDir = props.getProperty("pdf.dir.export") + "\\surat_fakultatif_email\\" + spaj.substring(0,2) + "\\" + spaj + "\\";
			String outputFilename = spaj+"_Surat_Fakultatif.pdf";
			JasperUtils.exportReportToPdf(props.getProperty("report.fakultatif") + ".jasper", outputDir, outputFilename, params, akumulasi, PdfWriter.AllowPrinting, null, null);
			
			if(request.getParameter("AL")!=null) mssg = "*) data medis sisanya akan segera di emailkan.";
			
			List<File> attachments = new ArrayList<File>();
			attachments.add(new File(outputDir + "\\" + outputFilename));
			send(true, 
					currentUser.getEmail(), 
					new String[] {email},
					new String[]{"ingrid@sinarmasmsiglife.co.id","asriwulan@sinarmasmsiglife.co.id",currentUser.getEmail()},
					new String[]{},
					"penawaran fakultatif a/n "+MPp.get("MCL_FIRST"), 
					mssg, attachments);
			
			return null;
		}
		
		return generateReport(akumulasi, request, response, params, "0", spaj, spaj+"_Surat_Fakultatif");
	}

}
