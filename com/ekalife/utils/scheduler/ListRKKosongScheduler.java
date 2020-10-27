package com.ekalife.utils.scheduler;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentScheduler;
import com.lowagie.text.pdf.PdfWriter;
/**
 * Scheduler ini dibuat untuk menginformasikan kepada pihak BSM mengenai
 * daftar2 list rk yg belum terpakai. Awal nya u/w melakukan proses secara
 * manual, sekarang dibuat otomatis dengan scheduler 
 * 
 * @author Yusup_A
 * @since Jul 23, 2009 (3:28:13 PM)
 */
public class ListRKKosongScheduler extends ParentScheduler{
	protected final Log logger = LogFactory.getLog( getClass() );

	public void main() throws UnknownHostException {
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
			Date bdate 	= new Date();
			/* BSM */
			List<Map> lsPincab = uwManager.selectPincab();
			String dir = props.getProperty("pdf.dir.report") + "\\list_rk_kosong\\";
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			StringBuffer pesan = new StringBuffer(), cheking = new StringBuffer();
			List<File> attachments = new ArrayList<File>();
			
			for(int a=0;a<lsPincab.size();a++) {
				attachments = new ArrayList<File>();
				Connection conn = null;
				try {
					conn = this.elionsManager.getUwDao().getDataSource().getConnection();
					// buat attachment	
					Map params = new HashMap();
					//logger.info("-" + lsPincab.get(a).get("KODE_CAB").toString().trim() + "-");
					params.put("kode_cab", lsPincab.get(a).get("KODE_CAB").toString().trim());
					//logger.info("kode : " + params.get("kode_cab"));
					/*JasperUtils.exportReportToPdf(
							props.getProperty("report.bac.list_rk_kosong")+".jasper", 
							dir, 
							"cabang_"+lsPincab.get(a).get("KODE_CAB").toString().trim()+".pdf", 
							params, 
							getConnection(), 
							PdfWriter.AllowPrinting, null, null);
					
					File sourceFile = new File(dir+"\\cabang_"+lsPincab.get(a).get("KODE_CAB").toString().trim()+".pdf");
					attachments.add(sourceFile);*/
					
					JasperUtils.exportReportToPdf(
							props.getProperty("report.bac.list_rk_kosong_akumulatif")+".jasper", 
							dir, 
							"cabang_"+lsPincab.get(a).get("KODE_CAB").toString().trim()+"_akumulatif.pdf", 
							params, 
							conn, 
							PdfWriter.AllowPrinting, null, null);
					
					File sourceFile = new File(dir+"\\cabang_"+lsPincab.get(a).get("KODE_CAB").toString().trim()+"_akumulatif.pdf");
					attachments.add(sourceFile);				
				
					// kirim ke cabang
					List<Map> daftarEmail = uwManager.selectEmailPincab(lsPincab.get(a).get("KODE_CAB").toString().trim());
					
					pesan = new StringBuffer();
					pesan.append("<span style='font-family:Arial,Helvetica,sans-serif;font-size: 13px;'>");
					pesan.append("<b>Yth. Bank Sinarmas "+lsPincab.get(a).get("NAMA_CAB").toString().trim()+"</b>");
					pesan.append("<br/><br/>");
					//pesan.append("Berikut daftar transaksi BSM tanggal "+df.format(lsPincab.get(a).get("DATE1"))+", yang sampai dengan tanggal "+df.format(lsPincab.get(a).get("DATE2")));
					pesan.append("Berikut daftar transaksi BSM, yang sampai dengan tanggal "+df.format(lsPincab.get(a).get("DATE2")));
					pesan.append(", Polis belum dicetak di BSM "+lsPincab.get(a).get("NAMA_CAB").toString().trim()+".");
					pesan.append("<br/>");
					pesan.append("<b>Kami tunggu konfirmasi  mengenai  premi tersebut.</b>");
					pesan.append("<br/><br/><br/><br/>");
					pesan.append("Salam,");
					pesan.append("<br/>");
					pesan.append("PT Asuransi Jiwa Sinarmas MSIG Tbk.");
					pesan.append("<br/><br/>");
					pesan.append("Timmy Walandaru");
					pesan.append("<br/>");
					pesan.append("Underwriting Departement");
					pesan.append("<br/>");
					pesan.append("[ (021)6257807 / ext. 8711 ]");
					pesan.append("</span>");
					
					String ao = "";
					if(!daftarEmail.get(0).get("MSAG_ID_AO").equals("n/a")) {
						String email = uwManager.selectEmailPincab_Ao(daftarEmail.get(0).get("MSAG_ID_AO").toString());
						if(!email.equals("n/a")) ao = email;
					}
					String[] to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString()};
					if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("BSD")) to = new String[]{"ming.aswaty@banksinarmas.com","cs011@banksinarmas.com","RO011@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("FTM")) to = new String[]{"niken.wulan@banksinarmas.com","cs059@banksinarmas.com","ro059@banksinarmas.com","niken.wulan@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("SRG")) to = new String[]{"deddy.t.wiharja@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("SKB")) to = new String[]{"richard@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("TNA")) to = new String[]{"antonius.gustaman@banksinarmas.com","Jackson@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("LPG")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"Bancass_Lampung@sinarmasmsiglife.co.id"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("PLU")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"maya.natalia@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("KUD")) to = new String[]{"koengfoe15@yahoo.com","kanthi.nalarantini@banksinarmas.com",daftarEmail.get(0).get("EM_PINCAB").toString()};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("PLM")) to = new String[]{"sudrajat@banksinarmas.com","cs041@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("MDN")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"cs015@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("PWT")) to = new String[]{"andreas.s.andrianto@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("HAS")) to = new String[]{"linawati.sudarmo@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("SKJ")) to = new String[]{"laurencia.y.gunawan@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("JTT")) to = new String[]{"yanti@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("RXM")) to = new String[]{"cs009@banksinarmas.com","cindy.chandra@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("SER")) to = new String[]{"hans.felix@banksinarmas.com","cs033@banksinarmas.com "};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("DIP")) to = new String[]{"subiantoro@banksinarmas.com","widha.s.wijaya@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("MND")) to = new String[]{"stella.sumual@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("BTM")) to = new String[]{"hotma.situmeang@banksinarmas"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("KRW")) to = new String[]{"cs036@banksinarmas.com","enung.n.zarkasih@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("SPG")) to = new String[]{"cs070@banksinarmas.co","evelin.mailoa@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("RPT")) to = new String[]{"jony@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("JYP")) to = new String[]{"hendra.purwanto@banksinarmas.com","Raymond.pancadarma@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("BKS")) to = new String[]{"hendry.lee@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("SEN")) to = new String[]{"handoko@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("ZAF")) to = new String[]{"irawati.rusli@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("MGD")) to = new String[]{"devi.c.wongso@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("MLG")) to = new String[]{"ronny.santoso@banksinarmas.com"};
					else if(lsPincab.get(a).get("KODE_CAB").toString().trim().equals("PTK")) to = new String[]{"yenni@banksinarmas.com"};
					
					if(ao.equals("n/a")) {
						cheking.append("cabang : " + lsPincab.get(a).get("KODE_CAB").toString().trim());
						cheking.append("<br/>");
						cheking.append("msag id ao : " +  daftarEmail.get(0).get("MSAG_ID_AO").toString());
						cheking.append("<br/>");
						cheking.append("email ao : " + ao);
						cheking.append("<br/>");
					}
					/*logger.info("cabang : " + lsPincab.get(a).get("KODE_CAB").toString().trim());
					logger.info("msag id ao : " +  daftarEmail.get(0).get("MSAG_ID_AO").toString());
					logger.info("email ao : " + ao);*/
					
					email.send(true, 
							"fikki@sinarmasmsiglife.co.id",
							//"yusup_a@sinarmasmsiglife.co.id",
							//to,
							new String[]{},
							//new String[]{"ardy@banksinarmas.com","nia.julidaria@banksinarmas.com","joni@banksinarmas.com","novie@sinarmasmsiglife.co.id","timmy@sinarmasmsiglife.co.id",ao},
							//new String[]{"nia.julidaria@banksinarmas.com","joni@banksinarmas.com",ao},
							//new String[]{"novie@sinarmasmsiglife.co.id","timmy@sinarmasmsiglife.co.id"},
							new String[]{},
							//new String[]{"novie@sinarmasmsiglife.co.id"},
							new String[]{"yusup_a@sinarmasmsiglife.co.id"}, 
							"List Setoran Premi yang belum diketahui Pemegang Polisnya BSM "+lsPincab.get(a).get("NAMA_CAB").toString().trim(),
							pesan.toString(), 
							attachments);
					
					break;
					
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}finally{
					this.elionsManager.getUwDao().closeConn(conn);
				}			
			}
			
			/* Sinarmas Sekuritas */
			Integer row = uwManager.getRowSs();
			//if(row > 0) {
				attachments = new ArrayList<File>();
				Connection conn = null;
				try {
					conn = this.elionsManager.getUwDao().getDataSource().getConnection();
					JasperUtils.exportReportToPdf(
							props.getProperty("report.bac.list_rk_kosong_akumulatif_ss")+".jasper", 
							dir, 
							"sinarmas_sekuritas_akumulatif.pdf", 
							null, 
							conn, 
							PdfWriter.AllowPrinting, null, null);
					
					File sourceFile = new File(dir+"\\sinarmas_sekuritas_akumulatif.pdf");
					attachments.add(sourceFile);
					
					pesan = new StringBuffer();
					pesan.append("<span style='font-family:Arial,Helvetica,sans-serif;font-size: 13px;'>");
					pesan.append("<b>Yth. Sinarmas Sekuritas </b>");
					pesan.append("<br/><br/>");
					pesan.append("Berikut daftar transaksi Sinarmas Sekuritas, yang sampai dengan tanggal "+df.format(lsPincab.get(0).get("DATE2")));
					pesan.append(", Polis belum dicetak di Sinarmas Sekuritas.");
					pesan.append("<br/>");
					pesan.append("<b>Kami tunggu konfirmasi  mengenai  premi tersebut.</b>");
					pesan.append("<br/><br/><br/><br/>");
					pesan.append("Salam,");
					pesan.append("<br/>");
					pesan.append("PT Asuransi Jiwa Sinarmas MSIG Tbk.");
					pesan.append("<br/><br/>");
					pesan.append("Fikki Indra Prasta");
					pesan.append("<br/>");
					pesan.append("Underwriting Departement");
					pesan.append("<br/>");
					pesan.append("[ (021)6257807 / ext. 8711 ]");
					pesan.append("</span>");					
					
					email.send(true, 
							"fikki@sinarmasmsiglife.co.id",
							//"yusup_a@sinarmasmsiglife.co.id",
							//to,
							new String[]{},
							//new String[]{"ardy@banksinarmas.com","nia.julidaria@banksinarmas.com","joni@banksinarmas.com","novie@sinarmasmsiglife.co.id","timmy@sinarmasmsiglife.co.id",ao},
							//new String[]{"natalia@sinarmasmsiglife.co.id"},
							//new String[]{"novie@sinarmasmsiglife.co.id","timmy@sinarmasmsiglife.co.id"},
							new String[]{},
							//new String[]{"novie@sinarmasmsiglife.co.id"},
							new String[]{"yusup_a@sinarmasmsiglife.co.id"}, 
							"List Setoran Premi yang belum diketahui Pemegang Polisnya Sinarmas Sekuritas",
							pesan.toString(), 
							attachments);					
					
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}finally {
					this.elionsManager.getUwDao().closeConn(conn);
				}
			//}
			
			/* ALL */
			attachments = new ArrayList<File>();
			try {
				conn = this.elionsManager.getUwDao().getDataSource().getConnection();
				JasperUtils.exportReportToPdf(
						props.getProperty("report.bac.list_rk_kosong_akumulatif_all")+".jasper", 
						dir, 
						"akumulatif_all.pdf", 
						null, 
						conn, 
						PdfWriter.AllowPrinting, null, null);
				
				File sourceFile = new File(dir+"\\akumulatif_all.pdf");
				attachments.add(sourceFile);
				
				pesan = new StringBuffer();
				pesan.append("<span style='font-family:Arial,Helvetica,sans-serif;font-size: 13px;'>");
				pesan.append("Berikut daftar transaksi BSM - Sinarmas Sekuritas, sampai dengan tanggal "+df.format(lsPincab.get(0).get("DATE2"))+".");
				pesan.append("<br/><br/><br/><br/><br/><br/>");
				pesan.append("* Email dikirim secara otomatis dari E-Lions");
				pesan.append("</span>");			
				
				email.send(true, 
						"ajsjava@sinarmasmsiglife.co.id",
						//"yusup_a@sinarmasmsiglife.co.id",
						//to,
						new String[]{},
						//new String[]{"UnderwritingBancass2@sinarmasmsiglife.co.id"},
						//new String[]{"ariani@sinarmasmsiglife.co.id"},
						//new String[]{"novie@sinarmasmsiglife.co.id","timmy@sinarmasmsiglife.co.id"},
						new String[]{},
						//new String[]{"novie@sinarmasmsiglife.co.id"},
						new String[]{"yusup_a@sinarmasmsiglife.co.id"}, 
						"List Setoran Premi yang belum diketahui Pemegang Polisnya",
						pesan.toString(), 
						attachments);					
				
			}catch (Exception e) {
				logger.error("ERROR :", e);
			}finally {
				this.elionsManager.getUwDao().closeConn(conn);
			}
			
			/* Check Email AO */
			if(!cheking.toString().equals("")) {
				try {
					email.send(true, 
							"ajsjava@sinarmasmsiglife.co.id",
							new String[]{"yusup_a@sinarmasmsiglife.co.id"},
							new String[]{},
							new String[]{},
							//new String[]{"yusup_a@sinarmasmsiglife.co.id"}, 
							"Daftar ao kosong list rk kosong",
							cheking.toString(), 
							null);
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}					
			}
				
			
			/*try {
				uwManager.insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						"SCHEDULER LIST RK KOSONG", bdate, new Date(), "OK");
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}*/			
		}
	}
}
