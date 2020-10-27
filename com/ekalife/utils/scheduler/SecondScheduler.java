package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import java.util.Date;

import com.ekalife.elions.process.Sequence;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis setiap bulan (00.30)
 * 
 * @author Andy
 * @since Dec 10, 2010 (2:39:19 PM)
 */
public class SecondScheduler extends ParentScheduler{
 
	protected Sequence sequence;
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {
			try{//khusus scheduler kurs dibuat try catch krn bikin error scheduler laen
				uwManager.schedulerDailyCurr();
			}catch(Exception e){
				String msh_name ="SCHEDULER KURS BI";
				if(uwManager.selectAlreadyExistScheduler(msh_name)<=0){
					Date nowDate = elionsManager.selectSysdateSimple();
					String me_id = sequence.sequenceMeIdEmail();
					
					String pesan = "Dear Finance,\nMohon kurs harian diinput secara manual karena proses otomatis gagal dijalankan.\n\n--------------------------\n";
					EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, true, "ajsjava@sinarmasmsiglife.co.id", new String[]{"lilyana@sinarmasmsiglife.co.id","rika@sinarmasmsiglife.co.id","aprina@sinarmasmsiglife.co.id"},
							new String[]{"himmia@sinarmasmsiglife.co.id","rudy_h@sinarmasmsiglife.co.id", "vonny_t@sinarmasmsiglife.co.id","deddy_p@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id","andy@sinarmasmsiglife.co.id"}, null, 
							"ERROR SCHEDULER KURS HARIAN",  pesan + Common.getRootCause(e).getMessage(), null, null);
				}
//				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
//						new String[]{"lilyana@sinarmasmsiglife.co.id","aprina@sinarmasmsiglife.co.id"}, new String[]{"himmia@sinarmasmsiglife.co.id","rudy_h@sinarmasmsiglife.co.id", "vonny_t@sinarmasmsiglife.co.id","deddy_p@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id","andy@sinarmasmsiglife.co.id"}, null, 
//						"ERROR SCHEDULER KURS HARIAN",  pesan + Common.getRootCause(e).getMessage(), null);
				
			}
			
			try{
				uwManager.schedulerTransPolToUw();
			}catch(Exception e){
				String pesan = "Scheduler Transfer Otomatis Dari Print Polis ke U/W tidak berjalan.\nTolong CEK ULANG Schedulernya!";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
						new String[]{"ryan@sinarmasmsiglife.co.id"}, new String[]{"deddy@sinarmasmsiglife.co.id"}, null, 
						"ERROR SCHEDULER TRANSFER OTOMATIS",  pesan + Common.getRootCause(e).getMessage(), null);
				
			}
			
			/**
			 * Scheduler untuk autotransfer Polis BSM dari print polis ke filling
			 * @author MANTA
			 */
			try{
				bacManager.schedulerTransferToFillingBSM();
			}catch(Exception e){
				String pesan = "Scheduler Transfer Otomatis Dari Print Polis ke Filling Tidak Berhasil!";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
						new String[]{"antasari@sinarmasmsiglife.co.id"}, null, null, 
						"ERROR SCHEDULER TRANSFER OTOMATIS",  pesan + Common.getRootCause(e).getMessage(), null);
				
			}
			/**
			 * Scheduler untuk mengirim report daily monitoring kyc
			 * @author LUFI
			 */
			try{
				bacManager.schedulerReportKycNewBussiness();
			}catch(Exception e){				
				EmailPool.send("E-Lions", 1, 0, 0, 0, null, 0, 0, new Date(), null, true, 
						props.getProperty("admin.ajsjava"), 
						new String[] { "ryan@sinarmasmsiglife.co.id" }, 
						null, 
						null, 
						"Error Pada Scheduler Report KYC New Bussiness", 
						null, 
						null, 
						null);
				
			}
			/**
			 * Scheduler untuk mengirim report jatuh tempo visa camp
			 * @author LUFI
			 */
			try{
				bacManager.schedulerJatuhTempoVisaCampBas();
			}catch(Exception e){				
				EmailPool.send("E-Lions", 1, 0, 0, 0, null, 0, 0, new Date(), null, true, 
						props.getProperty("admin.ajsjava"), 
						new String[] { "ryan@sinarmasmsiglife.co.id" }, 
						null, 
						null, 
						"Error Pada Scheduler Report Jatuh Tempo Visa Camp ", 
						null, 
						null, 
						null);
				
			}
			
			//scheduler dibawah ini dipindahkan ke class independen -> WelcomeCallScheduler.java
			/**
			 * req : Titis - Welcome Call 
			 * proses 1 : inforce + 1 --> schedule hari ini, sehingga update pada spaj inforce - 1
			 * ljj_id = 63 , lspd_id = 6 , flag_validasi = 1(need validasi;  )
			 * Proses 2 : pengiriman email yang sudah di validasi oleh CS di SNOWS Applikasi --> flag_validasi = 2(success;  )
			 * @author 	Ridhaal (22/09/2016)
			 */
			/*try{
				bacManager.schedulerWelcomeCall();
			}catch(Exception e){
				String pesan = "SCHEDULER PROSES WELCOME CALL TIDAK BERHASIL";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"ridhaal@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER WELCOME CALL",  pesan + Common.getRootCause(e).getMessage(), null);
			
			}*/
		}
	}
}