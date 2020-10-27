package com.ekalife.utils.scheduler;

import java.net.InetAddress;
import java.util.Date;

import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis setiap midnight (00.30) hari senin - sabtu
 * 
 * @author Yusuf
 * @since Mar 29, 2010 (9:39:19 AM)
 */
public class MainScheduler extends ParentScheduler{
 
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i")	&& 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSCLUS1"))
				) 
		{
		//if(jdbcName.equals("eka8i")) {
			//Scheduler Summary RK
			String nama_scheduler = "";
			try{
				nama_scheduler="SUMMARY RK";
				//uwManager.schedulerSummaryRK(null); -- mohon di cek kembali query nya . bikin scheduller ga jalan
				//Scheduler BMI
				nama_scheduler="BMI";
				uwManager.schedulerBmi();
				//Scheduler Komisi 30 Hari
				nama_scheduler="KOMISI 30 HARI";
				uwManager.schedulerKomisi30Hari();
				//Scheduler Rekap Pembatalan UW
				nama_scheduler="REKAP BATAL UW";
				uwManager.schedulerRekapPembatalanUw();
				//Scheduler Polis Expired
				nama_scheduler="SUMMARY EXPIRED";
				uwManager.schedulerSummaryExpired();
				//Scheduler Summary untuk status2 aksep yg masih terpending, seperti further dkk
				nama_scheduler="SUMMARY AKSEPTASI";
				uwManager.schedulerSummaryAkseptasi();
				//Scheduler terlembat pencetakan polis di cabang.
				nama_scheduler="SUMMARY TERLAMBAT CETAK POLIS";
				uwManager.schedulerSummaryTerlambatCetakPolis();
				//Scheduler Mirip schedulerSummaryAkseptasi, tp ini khusus buat BSM & report nya di kelompokin per cabang
				nama_scheduler="SUMMARY AKSEPTASI BSM";
				uwManager.summaryAkseptasiBSM();
				//Scheduler Prata (Jatis-BII) - Yusuf (26/4/2013) Disabled, kayaknya sudah tidak perlu dijalankan
				//uwManager.schedulerPrata();
				// Scheduler Permintaan Spaj
				nama_scheduler="SUMMARY PERMINTAAN SPAJ";
				uwManager.schedulerPermintaanSpaj();
				// Scheduler PAS
				nama_scheduler="PAS";
				uwManager.schedulerPas(1,1,1,1,1,1,1,1);
				// Scheduler Email status PA BSM
				nama_scheduler="=EMAIL STATUS PA BSM";
				uwManager.schedulerEmailStatusPabsm();
				// Scheduler autoupdate kolom-kolom mst_pas_sms
				nama_scheduler="AUTO PAS COLUMN";
				uwManager.schedulerAutoUpdatePasColumn();
				// Scheduler further requirement BP
				nama_scheduler="FURTHER REQ BP";
				uwManager.schedulerFurtherRequirementBp();
				// Scheduler further requirement DBD(BP/Agency)
				nama_scheduler="FURTHER REQ DBD";
				uwManager.schedulerFurtherRequirementDbd();
				// Scheduler further requirement PAS MallAssurance
				nama_scheduler="FURTHER REQ MALL";
			    uwManager.schedulerFurtherRequirementMallAssurance();
				//Scheduler Laporan TTP Harian/Bulanan/Tahunan (Yusuf / 30 Dec 2010)
			    nama_scheduler="DAILY TTP";
				uwManager.schedulerDailyTTP();//
				//Scheduler Auto-cancellation khusus polis mallassurance yang pending di posisi input max 2 minggu dari tgl input (lca_id = 58, lspd_id = 1) (Yusuf / 5 Aug 2011)
				nama_scheduler="AUTO CANCEL POLIS MALL";
				uwManager.schedulerAutoCancelPolisMallAssurance();
				//Scheduler pending kartu admedika setelah tiga hari kerja dikirim
				nama_scheduler="PENDING KARTU ADMEDIKA";
				uwManager.schedulerpendingKartuAdmedika();
				//scheduler status akseptasi spt
				nama_scheduler="SUMMARY AKSEPTASI SPT";
				uwManager.schedulerAksepSpt();
				//Scheduler Admission untuk Bridge Agency
//				uwManager.schedulerAdmissionBridge();
				//Scheduler Summary further requirement PAS
				//digabung dengan sheduler yang non PAS
				//uwManager.schedulerSummaryFurtherRequirementPas();
				//Scheduler Rekap Referensi(Tambang Emas)
				//uwManager.schedulerRekapReferensi();
				//Scheduler Appointment(Program Hadiah)
				nama_scheduler="APPOINTMENT";
				uwManager.schedulerAppointment();
				nama_scheduler="SUMMARY ARCO";
				bacManager.schedulerSummaryArco();
				nama_scheduler="SCHEDULER PROSES NOT PROCEED WITH";
				bacManager.schedulerNotProceedWith();
				nama_scheduler="SCHEDULER PROSES NOT PROCEED DMTM WITH RECURRING";
				bacManager.schedulerNotProceedWithRecurring();
			}catch (Exception e) {
				// TODO: handle exception
				String pesan = "Scheduler "+nama_scheduler+" gagal";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
						new String[]{"deddy@sinarmasmsiglife.co.id"}, null, null, 
						"ERROR SCHEDULER "+nama_scheduler,  pesan + Common.getRootCause(e).getMessage(), null);
			}
			
//			//Scheduler Daily Currency
//			try{//khusus scheduler kurs dibuat try catch krn bikin error scheduler laen
//				uwManager.schedulerDailyCurr();
//			}catch(Exception e){
//				String pesan = "Dear Finance,\nMohon kurs harian diproses secara manual karena proses gagal dijalankan.\n\n--------------------------\n";
//				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
//						new String[]{"lilyana@sinarmasmsiglife.co.id"}, new String[]{"yusuf@sinarmasmsiglife.co.id","rudy_h@sinarmasmsiglife.co.id", "deddy@sinarmasmsiglife.co.id"}, null, 
//						"ERROR SCHEDULER KURS HARIAN",  pesan + Common.getRootCause(e).getMessage(), null);
//				
//			}
			//Scheduler IHSG - Yusuf - 8 Mei 2013
			try{
				uwManager.schedulerDailyIHSG();
			}catch(Exception e){
				String pesan = "Scheduler IHSG gagal";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
						new String[]{"yusuf@sinarmasmsiglife.co.id"}, null, null, 
						"ERROR SCHEDULER IHSG",  pesan + Common.getRootCause(e).getMessage(), null);
				
			}
			// *Andhika - Auto Transfer polis
			try{
				uwManager.schedulerTransPolToUw();
			}catch(Exception e){
				String pesan = "Scheduler Transfer Otomatis Dari Print Polis ke U/W tidak berjalan.\nTolong CEK ULANG Schedulernya!";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
						new String[]{"ryan@sinarmasmsiglife.co.id"}, new String[]{"deddy@sinarmasmsiglife.co.id"}, null, 
						"ERROR SCHEDULER TRANSFER OTOMATIS",  pesan + Common.getRootCause(e).getMessage(), null);
				
			}
			
			/**
			 * req : Novie
			 * @author 	Andhika
			 */
			try{
				uwManager.schedulerFollowUpValidPolis();
			}catch(Exception e){
				String pesan = "Scheduler Follow Up Polis tidak jalan";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"ryan@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER FOLLOW UP POLIS",  pesan + Common.getRootCause(e).getMessage(), null);
			
			}
			
			/**
			 * req : Titis
			 * @author 	Randy (01/03/2016)
			 */
			try{
				bacManager.schedulerAutomailSummaryFurther();
			}catch(Exception e){
				String pesan = "AUTOMAIL REPORT SPAJ NPW NON-DMTM tidak jalan";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"randy@sinarmasmsiglife.co.id"}, new String[]{"ryan@sinarmasmsiglife.co.id"}, null, 
					"ERROR SCHEDULER NPW NON-DMTM",  pesan + Common.getRootCause(e).getMessage(), null);
			
			}
			
			/**
			 * Scheduler warning upload and transfer spaj (H+1)
			 * @author 	Canpri (17/12/2013)
			 */
			try{
				bacManager.schedulerWarningUploadTransferSpaj();
			}catch(Exception e){
				String pesan = "Scheduler warning upload and transfer spaj tidak jalan";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR Scheduler warning upload and transfer spaj (H+1)",  pesan + Common.getRootCause(e).getMessage(), null);
			
			}
			
			/**
			 * Scheduler NOTIF SURAT DOMISILI (02/04/2014)
			 * @author 	Canpri 
			 */
//			try{
//				bacManager.schedulerSuratDomisili();
//			}catch(Exception e){
//				String pesan = "Scheduler NOTIF SURAT DOMISILI tidak jalan";
//				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
//					new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, 
//					"ERROR SCHEDULER NOTIF SURAT DOMISILI",  pesan + Common.getRootCause(e).getMessage(), null);
//			
//			}
			
			/**
			 * Scheduler untuk Retur KPL yang diinput untuk notifikasi ke cabang (27/03/2014)
			 * @author 	Canpri 
			 */
			try{
				bacManager.schedulerReturKpl();
			}catch(Exception e){
				String pesan = "Scheduler Retur KPL yang diinput untuk notifikasi ke cabang tidak jalan";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER RETUR KPL",  pesan + Common.getRootCause(e).getMessage(), null);
			
			}
			
			/**
			 * Scheduler status AAJI calon karyawan
			 * @author alfian_h
			 */
			/*try{
				bacManager.schedulerStatusAAJICalonKaryawan();
			}catch(Exception e){
				String pesan = "Scheduler Permintaan Refund Premi Auto tidak berjalan";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"alfian_h@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER STATUS AAJI CALON KARYAWAN",  pesan + Common.getRootCause(e).getMessage(), null);
			}*/
			
			/**
			 * Scheduler untuk cek sisa Virtual Account (19/06/2014)
			 * @author 	Canpri 
			 */
			try{
				bacManager.schedulerVA();
			}catch(Exception e){
				String pesan = "Scheduler sisa stok Virtual Account tidak jalan";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"ryan@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER VIRTUAL ACCOUNT",  pesan + Common.getRootCause(e).getMessage(), null);
			
			}
			
			/**
			 * Scheduler untuk cek sisa Stock Spaj (19/09/2015)
			 * @author 	Ryan 
			 */
			try{
				bacManager.schedulerStockSPAJ();
			}catch(Exception e){
				String pesan = "Scheduler sisa stok SPAJ tidak jalan";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"ryan@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER STOCK SPAJ",  pesan + Common.getRootCause(e).getMessage(), null);
			
			}
			
			/**
			 * Scheduler untuk info Further ke admin cabang (24/11/2014)
			 * @author 	Canpri 
			 */
			try{
				bacManager.schedulerFRkeCabang();
			}catch(Exception e){
				String pesan = "Scheduler info Further ke admin cabang tidak jalan";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER FURTHER KE CABANG",  pesan + Common.getRootCause(e).getMessage(), null);
			
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
			 * Auto Reminder Smile Baby
			 * @author 	Randy (29-08-2016)
			 */	
			try{
				bacManager.schedulerReminderSmileBaby();
			}catch(Exception e){
				String pesan = "REMINDER SMILE BABY tidak jalan ";
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"randy@sinarmasmsiglife.co.id"}, new String[]{"ryan@sinarmasmsiglife.co.id"}, null, 
					"ERROR REMINDER SMILE BABY",  pesan + Common.getRootCause(e).getMessage(), null);
			
			}
			
//			/**
//			 * req : Titis - Welcome Call 
//			 * proses 1 : inforce + 1 --> schedule hari ini, sehingga update pada spaj inforce - 1
//			 * ljj_id = 63 , lspd_id = 6 , flag_validasi = 1(need validasi;  )
//			 * Proses 2 : pengiriman email yang sudah di validasi oleh CS di SNOWS Applikasi --> flag_validasi = 2(success;  )
//			 * @author 	Ridhaal (22/09/2016)
//			 */
//			try{
//				bacManager.schedulerWelcomeCall();
//			}catch(Exception e){
//				String pesan = "SCHEDULER PROSES WELCOME CALL TIDAK BERHASIL";
//				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
//					new String[]{"ridhaal@sinarmasmsiglife.co.id"}, null, null, 
//					"ERROR SCHEDULER WELCOME CALL",  pesan + Common.getRootCause(e).getMessage(), null);
//			
//			}
			
		}
	}
}