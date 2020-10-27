package com.ekalife.utils.scheduler.unused;

import com.ekalife.utils.parent.ParentScheduler;


/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis
 * 
 * @author Yusuf Sutarko
 * @since 20 Apr 09
 */
public class ListRKScheduler extends ParentScheduler{

	//main method
	public void main() throws Exception{
		/*
		Date yesterday = elionsManager.selectSysdate(-1);
		Date today = elionsManager.selectSysdate(0);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		//1. Report List RK (Menu dapat diakses di Entry > UW > Payment > List RK)
		if(jdbcName.equals("eka8i")) {
			try {
				String outputDir = props.getProperty("pdf.dir.report") + "\\list_rk";
				String outputFilename = "list_rk_"+dateFormat.format(today)+".pdf";
				
				//List<Map> daftarRK = uwManager.selectMstDrek2;
				
				
		SELECT   drek.norek_ajs, rbp.lsbp_nama bank_pusat_ajs, rb.lbn_nama bank_ajs, drek.tgl_trx, cab.nama_cab, drek.no_trx, drek.lsbp_id, drek.jenis,
		         drek.lku_id, drek.nilai_trx, drek.no_spaj,
		         drek.tgl_input, drek.flag_proses, drek.flag_update, drek.tgl_proses,
		         drek.user_input, drek.user_proses, drek.ket, drek.kode_cab,
		         bank.lsbp_nama, kurs.lku_symbol, user_input.lus_full_name nama_input,
		         user_proses.lus_full_name nama_proses, flag_recheck, ket_update, pol.mspo_no_blanko
		    FROM eka.mst_drek drek,
		         eka.lst_bank_pusat bank,
		         eka.lst_kurs kurs,
		         eka.lst_user user_input,
		         eka.lst_user user_proses,
		         eka.cab_bsm cab,
				 eka.lst_rek_ekalife r,
				 eka.lst_bank rb,
				 eka.lst_bank_pusat rbp,
				 eka.mst_policy pol
  		   WHERE drek.tgl_trx = eka.add_workdays(sysdate, -4)
		     AND nvl(drek.flag_recheck, 0) <> 1
			 AND drek.no_spaj is null
		     AND drek.lsbp_id = 156
		     AND drek.lsbp_id = bank.lsbp_id(+)
		     AND drek.lku_id = kurs.lku_id(+)
		     AND drek.user_input = user_input.lus_id(+)
		     AND drek.user_proses = user_proses.lus_id(+)
		     AND drek.kode_cab = cab.kode(+)
			 --AND cab.kode = RPAD (#kodok#, 3, ' ')
			AND drek.norek_ajs = replace(replace(replace(r.lre_acc_no,'.'),'-'),' ')
			AND r.lbn_id = rb.lbn_id
			AND rb.lsbp_id = rbp.lsbp_id
			AND drek.no_spaj = pol.reg_spaj(+)
		ORDER BY cab.nama_cab, drek.norek_ajs, rbp.lsbp_nama, rb.lbn_nama, drek.tgl_trx
				 
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				
				JasperUtils.exportReportToPdf(
						props.getProperty("report.bac.list_rk")+".jasper", 
						outputDir, 
						outputFilename, 
						params, 
						daftarRK, 
						PdfWriter.AllowPrinting, null, null);
				
				List<File> attachments = new ArrayList<File>();
				File sourceFile = new File(outputDir+"\\"+outputFilename);
				attachments.add(sourceFile);
				
				email.send(true, 
						props.getProperty("admin.ajsjava"), 
						new String[]{"novie@sinarmasmsiglife.co.id"}, 
						new String[]{props.getProperty("admin.yusuf")},
						null, 
						"Pending List RK",
						"Berikut adalah List RK yang masih berstatus Pending"
						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions [AJS].", 
						attachments);

			} catch (Exception e) {
				logger.error("ERROR :", e);
			}
		}
		*/
	}

}