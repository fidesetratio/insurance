package com.ekalife.elions.web.viewer;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;
import com.ibatis.common.resources.Resources;

public class CLetterBSMController extends ParentMultiController{
	
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date tglKirim = elionsManager.selectSysdateSimple();
		
		User currentUser = (User)request.getSession().getAttribute("currentUser");
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);
    	
    	Map<String, Object> m = new HashMap<String, Object>();
    	
    	//Untuk Menampilkan Report
    	if(request.getParameter("showReport") != null){
    		
    		String jnreport = ServletRequestUtils.getStringParameter(request, "jn_report");
    		String stpolis = ServletRequestUtils.getStringParameter(request, "st_polis");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");

    		Date tanggalAwal = defaultDateFormat.parse(bdate);
    		String userlogin = currentUser.getName();
    		Date tanggalAkhir = defaultDateFormat.parse(edate);
    		
    		//Bila Menampilkan Semua Report
    		if(jnreport.equals("0")){
    		List dbpolis = bacManager.selectDataCoverBsmAll(bdate, edate);
    			if(dbpolis.size() > 0){ //bila ada data
    	    		try{
    	    			ServletOutputStream sos = response.getOutputStream();
	    	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.cover_letter_bsm_all") + ".jasper");
	    	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	
	    	    		Map<String, Object> params = new HashMap<String, Object>();
	    	    		params.put("tanggalAwal", tanggalAwal);
	    	    		params.put("tanggalAkhir", tanggalAkhir);
	    	    		params.put("userlogin", userlogin);
	    	    		
	    	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(dbpolis));
	    		    	JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
	    	    		sos.close();
					} catch (Exception e) {
						logger.error("ERROR :", e);
					}
        		 }else{ //bila tidak ada data
        			try {
        				ServletOutputStream sos = response.getOutputStream();
	        			sos.println("<script>alert('Tidak ada data');window.close();</script>");
	        			sos.close();
					} catch (IOException e) {
						logger.error("ERROR :", e);
					}
        		}
    		}
    		
    		//Bila Menampilkan Report Berdasarkan Branch Admin
    		if(jnreport.equals("1")){
        		String lcb_no = ServletRequestUtils.getStringParameter(request, "branch");
    			if(lcb_no.equals("emp")){
    				lcb_no = null;
    			}
    			List dbpolis = bacManager.selectDataCoverBsmCab(bdate, edate, lcb_no);
    			if(dbpolis.size() > 0){ //bila ada data
    				try{
    					ServletOutputStream sos = response.getOutputStream();
	    				File sourceFile = Resources.getResourceAsFile(props.getProperty("report.cover_letter_bsm") + ".jasper");
	    				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	
	    				Map<String, Object> params = new HashMap<String, Object>();
	    				params.put("tanggalAwal", tanggalAwal);
	    				params.put("tanggalAkhir", tanggalAkhir);
	    				params.put("lcb_no", lcb_no);
	    				params.put("userlogin", userlogin);
	
	    				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(dbpolis));
	    				JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
	    				sos.close();
					} catch (Exception e) {
						logger.error("ERROR :", e);
					}
    			}else{ //bila tidak ada data
        			try {
        				ServletOutputStream sos = response.getOutputStream();
	        			sos.println("<script>alert('Tidak ada data');window.close();</script>");
	        			sos.close();
					} catch (IOException e) {
						logger.error("ERROR :", e);
					}
    			}
    			m.put("branch", lcb_no);
    		}
    		m.put("username", currentUser.getName());
    		m.put("bdate", bdate);
    		m.put("edate", edate);
			m.put("stpolis", stpolis);
			m.put("jnreport", jnreport);
			return null;
    	}


    	//Tampilkan Tabel
    	if(request.getParameter("showCL") != null){
    		
    		String jnreport = ServletRequestUtils.getStringParameter(request, "jn_report");
    		String stpolis = ServletRequestUtils.getStringParameter(request, "st_polis");
    		String lcb_no = ServletRequestUtils.getStringParameter(request, "branch");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
			String lusid = currentUser.getLus_id();
			String qa = "/QA";
			if(!currentUser.getLde_id().equals("29")){
				lusid = null;
			}
			
    		if(stpolis == null){
    			m.put("warn", "Pilih Salah Satu Status Polis!");
    		}else{
    			if(jnreport.equals("1") && lcb_no.equals("emp")){
    				m.put("warn", "Pilih Branch Admin!");
        		} else {
        			if(jnreport.equals("0")){
        				lcb_no = null;
        			}
        			List dbpolis = bacManager.selectPolisCoverLetterBsm(bdate, edate, stpolis, lcb_no);
        			if(dbpolis.isEmpty()){
        				m.put("warn", "Data Tidak Ada!");
        			}
        			m.put("dbpolis", dbpolis);
    				json = 1;
        		}		
    		}
    		m.put("bdate", bdate);
    		m.put("edate", edate);
			m.put("branch", lcb_no);
			m.put("stpolis", stpolis);
			m.put("jnreport", jnreport);
			m.put("qa", qa);
    	}
    	
    	
    	//Kirim Polis
    	if(request.getParameter("sendCL") != null){
    		
    		String jnreport = ServletRequestUtils.getStringParameter(request, "jn_report");
    		String stpolis = "0";
    		String lcb_no = ServletRequestUtils.getStringParameter(request, "branch");
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String lusid = currentUser.getLus_id();
    		
    		String check[] = request.getParameterValues("chbox");
    		String sms[] = request.getParameterValues("chsim");
    		String adm[] = request.getParameterValues("chadme");
    		String numberpo[] = request.getParameterValues("po");
    		String temp_spaj = null;

    		Boolean error = false;
    		
			if(check != null){
	    		String[] emaillastuser = new String[check.length];
	    		//String[] emailcabang = new String[check.length];
	    		String[] cabang = new String[check.length];
	    		
				for(int i=0;i<check.length;i++){
					//insert tanggal kirim
					String data = check[i];
					String nopol = data.substring(0, data.indexOf("~"));
					String regspaj = elionsManager.selectSpajFromPolis(nopol.replace(".",""));
					String tgl = uwManager.selectTglCoverLetter(nopol, stpolis);
					String ket = "";
					String kolom_tgl = "";
					if(stpolis.equals("0")){
						String sms3 = ""; String adm3 = "";
						if(sms != null){
							for(int j=0;j<sms.length;j++){
								String sms2 = sms[j];
								if(sms2.equals(nopol)){
									sms3 = "y";
								}
							}
						}
						if(adm != null){
							for(int j=0;j<adm.length;j++){
								String adm2 = adm[j];
								if(adm2.equals(nopol)){
									adm3 = "y";
								}
							}
						}
						if(sms3 == "y" && adm3 == "y"){ 
							ket = "POLIS TELAH DIKIRIM KE GA (ADA SIMASCARD DAN ADA KARTU ADMEDIKA)";
						} else if(sms3 == "y" && adm3 == ""){
							ket = "POLIS TELAH DIKIRIM KE GA (ADA SIMASCARD DAN TIDAK ADA KARTU ADMEDIKA)";
						} else if(sms3 == "" && adm3 == "y"){
							ket = "POLIS TELAH DIKIRIM KE GA (TIDAK ADA SIMASCARD DAN ADA KARTU ADMEDIKA)";
						} else if(sms3 == "" && adm3 == ""){
							ket = "POLIS TELAH DIKIRIM KE GA (TIDAK ADA SIMASCARD DAN TIDAK ADA KARTU ADMEDIKA)";
						}
						kolom_tgl = "tgl_kirim_polis";
					}else if(stpolis.equals("1")){
						ket = "POLIS TELAH DITERIMA OLEH GA";
						kolom_tgl = "tgl_terima_ga_from_uw";
					}else if(stpolis.equals("2")){
						if(numberpo[i] == null || numberpo[i] == ""){
							error = true;
							m.put("warn", "Kolom No. PO Harus Diisi!");
						}else{
							ket = "POLIS TELAH DIKIRIM KE CABANG BSM (NO. PO " + numberpo[i] + ")";
						}
						kolom_tgl = "tgl_kirim_ga_to_admin";
					}else if(stpolis.equals("3")){
						ket = "POLIS TELAH DITERIMA CABANG BSM";
						kolom_tgl = "tgl_terima_admin_from_ga";
					}else if(stpolis.equals("4")){
						ket = "POLIS TELAH DIKIRIM KE AGENT";
						kolom_tgl = "tgl_kirim_admin_to_agent";
					}
					
					String lastuser = uwManager.selectLusCoverLetterPositionSpaj(regspaj);
					if(lastuser!=null) emaillastuser[i] = uwManager.selectEmailUser(lastuser);
					
					//emailcabang[i] = uwManager.selectEmailCabangFromSpaj(regspaj);
					cabang[i] = data.substring(data.indexOf("~")+1, data.length());
					
					if(error == false){
						if(tgl.equals("kosong")){
							uwManager.updateCoverLetter(nopol, stpolis);
							bacManager.saveMstTransHistory(regspaj, kolom_tgl, null, null, null);
							elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), ket, regspaj, 0);	
							// Request Firmansyah QC - Rahmayanti
							elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "QC2_"+currentUser.getLus_full_name(), regspaj, 0);
							if(i==0){
								temp_spaj = "\'"+regspaj+"\'";
							}
							else{
								temp_spaj =temp_spaj+","+"\'"+regspaj+"\'";
							}
							m.put("warn", "Polis Berhasil Diproses");	
							m.put("temp_spaj", temp_spaj);
							
						}else{
							m.put("warn", "Polis Sudah Pernah Di Simpan/Kirim!");
						}
					}
				}
				
				
				if(error == false){
					
					//Distinct Array
					String[] distArray = new String[check.length];
					Integer arr = 0;
					distArray[arr] = cabang[0];
					Boolean duplicate = false;
					Boolean duplicate2 = false;
					for(int j=0;j<check.length;j++){
						String temp = cabang[j];
						//Looping untuk membandingkan array yg diambil dengan array pada source
						for(int k=0;k<check.length;k++){
							duplicate = false; duplicate2 = false;
							if(j != k && temp == cabang[k]){
								duplicate = true;
							}
							if(!duplicate){
								//Looping untuk membandingkan array yg diambil dengan array yg telah disimpan
								for(int l=0;l<arr+1;l++){
									if(distArray[l].equals(cabang[k])){
										duplicate2 = true;
									}
								}
								if(!duplicate2){
									arr = arr + 1;
									distArray[arr] = cabang[k];
								}
							}
						}
					}
					

					List<Map> letter = new ArrayList<Map>();
					List<Map> region = new ArrayList<Map>();
					
					//Proses Pengiriman Email
/*					if(stpolis.equals("0")){
						for(int j=0;j<arr+1;j++){
							String larcab = distArray[j];
							region = uwManager.selectAddrRegion(larcab);
							Map rg = region.get(0);
							try{			
								StringBuffer pesan = new StringBuffer();
								//Format isi email
								pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
								pesan.append("<body>Pada tanggal <b>" + df.format(tglKirim) + "</b> telah dikirimkan polis-polis tersebut kepada Dept. GA, ");
								pesan.append("untuk dapat dikirimkan ke Cabang <b>" + rg.get("CABANG") + "</b> dengan alamat <b>" + rg.get("ALAMAT") + "</b>.<br><br>");
								pesan.append("<table cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1000px'>");
								pesan.append("<tr bgcolor='#A8B8EE'><th>No</th>");
								pesan.append("<th>No. Polis</th>");
								pesan.append("<th>Pemegang Polis</th>");
								pesan.append("<th>Tertanggung</th>");
								pesan.append("<th>Admin Input</th>");
								pesan.append("<th>Produk</th></th>");
								pesan.append("<th>Tanggal Cetak</th>");
								pesan.append("<th>SimasCard</th>");
								pesan.append("<th>Kartu Admedika</th></tr>");
								
								int no = 0;
								for(int i=0; i<check.length;i++){
									String data = check[i];
									String nopol = data.substring(0, data.indexOf("~"));
									String regspaj = elionsManager.selectSpajFromPolis(nopol.replace(".",""));
									letter = uwManager.selectCabCoverLetter(regspaj);
									Map x = letter.get(0);
									
									if(x.get("IDCAB").toString().equals(larcab)){
										no = no + 1;
										if(no % 2 == 0){
											pesan.append("<tr bgcolor='#E0EEFE'><td align='center' nowrap>"+ no + "</td>");
										}
										else{
											pesan.append("<tr bgcolor='#BAD8FB'><td align='center' nowrap>"+ no + "</td>");
										}
										pesan.append("<td nowrap>"+ x.get("NOPOL") + "</td>");
										pesan.append("<td>"+ x.get("PP") + "</td>");
										pesan.append("<td>"+ x.get("TT") + "</td>");
										pesan.append("<td nowrap>"+ x.get("NAMAREFF") + "</td>");
										pesan.append("<td nowrap>"+ x.get("PROD") + "</td>");
										pesan.append("<td nowrap>"+ x.get("DATPRINT") + "</td>");
										pesan.append("<td nowrap>"+ x.get("SIMCARD") + "</td>");
										pesan.append("<td nowrap>"+ x.get("ADMED") + "</td></tr>");	

									}	
								}
								pesan.append("</table> <br><br>");
								pesan.append("Pengiriman polis akan memerlukan waktu sekitar 2-3 hari kerja.<br>");
								pesan.append("Apabila setelah melewati waktu tersebut polis tidak diterima, ");
								pesan.append("harap kirimkan email ke Dept. Underwriting - UP <b>" + currentUser.getLus_full_name() + "</b> (<u>" + currentUser.getEmail() + "</u>) dan Dept. GA - UP <b>Erik</b>/<b>Ito</b> (<u>GA-ekspedisi@sinarmasmsiglife.co.id</u>).<br><br><br>");
								pesan.append("Tgl Proses\t: " + df.format(tglKirim) + "<br>");
								pesan.append("User\t\t: " + currentUser.getLus_full_name() + "<br>");
								pesan.append("Email\t\t: " + currentUser.getEmail() + "<br>");
								pesan.append("Dept. Underwriting");
								
								String from = props.getProperty("admin.ajsjava");
								String emailto = "GA-ekspedisi@sinarmasmsiglife.co.id";
								String emailcc = rg.get("EMAIL").toString();
								String emailbcc = "antasari@sinarmasmsiglife.co.id;" + currentUser.getEmail();
								
								if(!emailto.trim().equals("")){	
									String[] to = emailto.split(";");
									String[] cc = emailcc.split(";");
									String[] bcc = emailbcc.split(";");

									try {
										email.send(true, from, to, cc, bcc,
											"Pengiriman Polis Individu",
											pesan.toString(), null);
									}
									catch (MailException e1) {
										// TODO Auto-generated catch block
										logger.error("ERROR :", e1);
									}
									catch (MessagingException e1) {
										// TODO Auto-generated catch block
										logger.error("ERROR :", e1);
									}
								}	
							}
							catch (Exception e) {
								logger.error("ERROR :", e);
							}
							
						}
					}else if(stpolis.equals("2")){
						for(int j=0;j<arr+1;j++){
							
							String larcab = distArray[j];
							region = uwManager.selectAddrRegion(larcab);
							Map rg = region.get(0);
							try{			
								StringBuffer pesan = new StringBuffer();
								//Format isi email
								pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
								pesan.append("<body>Pada tanggal <b>" + df.format(tglKirim) + "</b> telah dikirimkan polis-polis tersebut kepada ");
								pesan.append("Cabang <b>" + rg.get("CABANG") + "</b>dengan alamat <b>" + rg.get("ALAMAT") + "</b>.<br><br>");
								pesan.append("<table cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1000px'>");
								pesan.append("<tr bgcolor='#A8B8EE'><th>No</th>");
								pesan.append("<th>No. Polis</th>");
								pesan.append("<th>Pemegang Polis</th>");
								pesan.append("<th>Tertanggung</th>");
								pesan.append("<th>Admin Input</th>");
								pesan.append("<th>Produk</th></th>");
								pesan.append("<th>Tanggal Cetak</th>");
								pesan.append("<th>SimasCard</th>");
								pesan.append("<th>Kartu Admedika</th>");
								pesan.append("<th>No. Ekspedisi</th></tr>");
								
								String emailcc = "";
								int no = 0;
								for(int i=0; i<check.length;i++){
									String data = check[i];
									String nopol = data.substring(0, data.indexOf("~"));
									String regspaj = elionsManager.selectSpajFromPolis(nopol.replace(".",""));
									letter = uwManager.selectCabCoverLetter(regspaj);
									Map x = letter.get(0);

									if(x.get("IDCAB").toString().equals(larcab)){
										emailcc = emailcc + emaillastuser[i] + ";";
										no = no + 1;
										if(no % 2 == 0){
											pesan.append("<tr bgcolor='#E0EEFE'><td align='center' nowrap>"+ no + "</td>");
										}
										else{
											pesan.append("<tr bgcolor='#BAD8FB'><td align='center' nowrap>"+ no + "</td>");
										}
										pesan.append("<td nowrap>"+ x.get("NOPOL") + "</td>");
										pesan.append("<td>"+ x.get("PP") + "</td>");
										pesan.append("<td>"+ x.get("TT") + "</td>");
										pesan.append("<td nowrap>"+ x.get("NAMAREFF") + "</td>");
										pesan.append("<td nowrap>"+ x.get("PROD") + "</td>");
										pesan.append("<td nowrap>"+ x.get("DATPRINT") + "</td>");
										pesan.append("<td nowrap>"+ x.get("SIMCARD") + "</td>");
										pesan.append("<td nowrap>"+ x.get("ADMED") + "</td>");
										pesan.append("<td nowrap>"+ x.get("NOPO") + "</td></tr>");	

									}	
								}
								pesan.append("</table> <br><br>");
								pesan.append("Pengiriman polis akan memerlukan waktu sekitar 2-3 hari kerja.<br>");
								pesan.append("Apabila setelah melewati waktu tersebut polis tidak diterima, ");
								pesan.append("harap kirimkan email ke Dept. GA - UP <b>Erik</b>/<b>Ito</b> (<u>GA-ekspedisi@sinarmasmsiglife.co.id</u>).<br><br><br>");
								pesan.append("Tgl Proses\t: " + df.format(tglKirim) + "<br>");
								pesan.append("User\t\t: " + currentUser.getLus_full_name() + "<br>");
								pesan.append("Email\t\t: <u>GA-ekspedisi@sinarmasmsiglife.co.id</u><br>");
								pesan.append("Dept. GA");
								
								String from = props.getProperty("admin.ajsjava");
								String emailto = rg.get("EMAIL").toString();
								String emailbcc = "antasari@sinarmasmsiglife.co.id;GA-ekspedisi@sinarmasmsiglife.co.id";
								
								if(!emailto.trim().equals("")){	
									String[] to = emailto.split(";");
									String[] cc = emailcc.split(";");
									String[] bcc = emailbcc.split(";");

									try {
										email.send(true, from, to, cc, bcc,
											"Pengiriman Polis Individu",
											pesan.toString(), null);
									}
									catch (MailException e1) {
										// TODO Auto-generated catch block
										logger.error("ERROR :", e1);
									}
									catch (MessagingException e1) {
										// TODO Auto-generated catch block
										logger.error("ERROR :", e1);
									}
								}	
							}
							catch (Exception e) {
								logger.error("ERROR :", e);
							}
							
						}
					}					
*/				}
			}else{
	    		m.put("warn", "Checklist Polis Yang Akan Dikirim!");
			}
    		m.put("bdate", bdate);
    		m.put("edate", edate);
			m.put("branch", lcb_no);
			m.put("jnreport", jnreport);
			json = 1;
    	}
    	
    	
    	//Halaman depan
    	if(json == 0){
    		m.put("stpolis", "x");
//        	m.put("bdate", "22/06/2011");
//        	m.put("edate", "22/06/2011");
        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(-7)));
	    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	}
    	m.put("bradmin", bacManager.selectCabBII());
		if(currentUser.getLde_id().equals("01")){
			m.put("option", "all");
		}else if(currentUser.getLde_id().equals("11")){
			m.put("option", "uw");
		}else if(currentUser.getLde_id().equals("13")){
			m.put("option", "ga");
		}else if(currentUser.getLde_id().equals("29")){
			m.put("option", "adm");
		}
    	
    	return new ModelAndView("uw/viewer/coverletterbsm", m);
	}
	
}