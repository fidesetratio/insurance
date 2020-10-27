package com.ekalife.elions.web.uw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.xml.JRGenericPrintElementParameterFactory.Parameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.Commission;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DrekDet;
import com.ekalife.elions.model.Payment;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.TopUp;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.vo.JenisMedicalVO;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.Print;
import com.ekalife.utils.parent.ParentFormController;
import com.ekalife.utils.view.PDFViewer;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

public class TransferToPrintPolisFormController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );
	protected PrintPolisPrintingController ppc;
	
	public PrintPolisPrintingController getPpc() {
		return ppc;
	}

	public void setPpc(PrintPolisPrintingController ppc) {
		this.ppc = ppc;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");

		String lca_id = elionsManager.selectCabangFromSpaj(spaj);
		Map form=new HashMap();

		if(request.getParameter("submitSuccess")==null) {
			//cek, ada kekurangan/kelebihan bayar gak, kalo ada, tanya
			List billInfo = this.uwManager.selectBillingInfoForTransfer(spaj, 1, 1);
			String lsbs_id = uwManager.selectLsbsId(spaj);
			String lsdbs_number = uwManager.selectLsdbsNumber(spaj);
			Map map = (HashMap) billInfo.get(0);
//			if(map.get("MSBI_PAID").toString().equals("0")){ //sudah lunas, dan ..
				/*String lsbs_id = uwManager.selectLsbsId(spaj);
				String lsdbs_number = uwManager.selectLsdbsNumber(spaj);*/
				if(lsbs_id.equals("187") && lsdbs_number.equals("6")){
					if(Double.parseDouble(map.get("MSBI_REMAIN").toString())>49000){//masih kurang bayar 74.000 - 25.000 = 49.000
						Double kurang_bayar_bpd = Double.parseDouble(map.get("MSBI_REMAIN").toString()) - new Double(49000);
						form.put("kuranglebih", "Ada kekurangan pembayaran sebesar "+map.get("LKU_SYMBOL")+twoDecimalNumberFormat.format(kurang_bayar_bpd)+" .");
					}
				}else{
					if(Double.parseDouble(map.get("MSBI_REMAIN").toString())>0){//masih kurang bayar
						form.put("kuranglebih", "Ada kekurangan pembayaran sebesar "+map.get("LKU_SYMBOL")+twoDecimalNumberFormat.format(map.get("MSBI_REMAIN"))+" .");
					}else if(Double.parseDouble(map.get("MSBI_REMAIN").toString())<0){//kelebihan bayar
						form.put("kuranglebih", "Ada kelebihan pembayaran sebesar "+map.get("LKU_SYMBOL")+twoDecimalNumberFormat.format(map.get("MSBI_REMAIN"))+" .");
					}
				}
//			}
//			else{
//				if(Double.parseDouble(map.get("MSBI_REMAIN").toString())<0){//kelebihan bayar
//					form.put("kuranglebih", "Ada kelebihan pembayaran sebesar "+map.get("LKU_SYMBOL")+twoDecimalNumberFormat.format(map.get("MSBI_REMAIN"))+" .");
//				}
//			}
			
			Integer countBilling = uwManager.selectCountMstBillingNB(spaj);
			if(countBilling>1){
				for(int i=2;i<=countBilling;i++){
					List billInfoTopUp = this.uwManager.selectBillingInfoForTransfer(spaj, 1, i);
					Map mapTopup = (HashMap) billInfoTopUp.get(0);
//					if(mapTopup.get("MSBI_PAID").toString().equals("0")){ //sudah lunas, dan ..
						if(Double.parseDouble(mapTopup.get("MSBI_REMAIN").toString())>0){//masih kurang bayar
							form.put("kuranglebihTopUp", "Ada kekurangan pembayaran TopUp sebesar "+mapTopup.get("LKU_SYMBOL")+twoDecimalNumberFormat.format(mapTopup.get("MSBI_REMAIN"))+" .");
						}else if(Double.parseDouble(mapTopup.get("MSBI_REMAIN").toString())<0){//kelebihan bayar
							form.put("kuranglebihTopup", "Ada kelebihan pembayaran TopUp sebesar "+mapTopup.get("LKU_SYMBOL")+twoDecimalNumberFormat.format(mapTopup.get("MSBI_REMAIN"))+" .");
						}
//					}
				}
			}

			//cek konfirmasi RK disini 
			Date defaultDate = elionsManager.selectMst_default(1);
			Date inputDate = elionsManager.selectSysdate(0);
			List topUpList = elionsManager.selectTopUp(spaj, 1, 1, "desc");
			if(topUpList.size()>0) {
//				throw new Exception("Terjadi error dalam sistem, harap hubungi EDP");
//			}
				Date rkDate = ((TopUp) topUpList.get(0)).getMspa_date_book(); 
				Date prodDate = inputDate;
				Date rkLastDate = elionsManager.selectMst_default(15);
				List lRkCek= elionsManager.cekTglRk(spaj);
				Date mspa_date_book =((TopUp)lRkCek.get(0)).getMspa_date_book();
				Date mste_beg_date = ((TopUp)lRkCek.get(0)).getMste_beg_date();
//				Date mste_beg_date;
//				setMste_beg_date =mspa_date_book();
				
				
				//khusus bancass, gak usah tanya ini itu
				if(rkDate.compareTo(defaultDate)>0 && !lca_id.equals("09")){
					prodDate = defaultDate;
					if(rkDate.after(rkLastDate)){
						prodDate = rkDate;
					}else{
						form.put(
								"konfirmasiRK", "Tanggal Produksi: " + defaultDateFormat.format(prodDate) + 
								"<br>- Tgl RK Terakhir: "+defaultDateFormat.format(rkLastDate)+
								"<br>- Tgl RK Polis ini: "+defaultDateFormat.format(rkDate)+".");
					}
				}
				
				Integer jenis = 1;
				if((lsbs_id.equals("190") && lsdbs_number.equals("9")) || (lsbs_id.equals("200") && lsdbs_number.equals("7")) ) jenis = 3;
				Date newProdDate = uwManager.selectTanggalProduksiUntukProsesProduksi(rkDate, lca_id, jenis);
				Date maxRkDate = ((TopUp) topUpList.get(0)).getMspa_date_book(); 
				Date minRkDate = ((TopUp) topUpList.get(topUpList.size()-1)).getMspa_date_book();
				if(!lca_id.equals("09")){
					if(maxRkDate.after(rkLastDate)){
						if(minRkDate.compareTo(rkLastDate)<=0 ){
							form.put(
									"konfirmasiRK", "Tanggal Produksi: " + defaultDateFormat.format(newProdDate) + 
									"<br>- Tgl RK Terakhir: "+defaultDateFormat.format(rkLastDate)+
									"<br>- Tgl RK Terakhir Polis ini: "+defaultDateFormat.format(maxRkDate)+".");
						}
					}
				}
				
				if (mspa_date_book.compareTo(mste_beg_date)!=0){
					request.setAttribute("confirmMessage","Tanggal Rk dan Beg_date tidak sama.");
					//logger.info("Beda ya");
				}
			}
			
		
		}
		
		String rdblock = uwManager.selectRedeemBlock(spaj);
		if (rdblock != null){		
			form.put("block", 1);
		}
		
		form.put("spaj", spaj);
		
		return form;
	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		//initBinder ini jalannya waktu baru masuk halaman, mirip seperti OPEN pada WINDOW di POWERBUILDER
		
		if(request.getParameter("submitSuccess")==null) {
			String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
			String from = ServletRequestUtils.getRequiredStringParameter(request, "from");
			BindingResult err = binder.getBindingResult();
			
			//cek posisi SPAJ dan asalnya
			if(from.equals("underwriting")) {
				if(this.elionsManager.validationPositionSPAJ(spaj) != 2) err.reject("payment.invalidPosition");
			}else if(from.equals("payment")) {
				if(this.elionsManager.validationPositionSPAJ(spaj) != 4) err.reject("payment.invalidPosition");
			}
			
			//cek, billing topup harus sudah lunas
//			Integer topupBerkala;
//			if((topupBerkala=this.elionsManager.validationTopupBerkala(spaj))!=null){ //apakah ada topup berkala?
//				if(topupBerkala==1){
//					if(this.elionsManager.validationAlreadyPaid(spaj, 1 ,2)==0){//belom lunas topupnya
//						err.reject("payment.notEnough");
//					}
//				}
//			}
//			
//			Integer countBilling = uwManager.selectCountNewBusiness(spaj);
//			if(countBilling>1){
//				for(int i=2;i<=countBilling;i++){
//					if(this.elionsManager.validationAlreadyPaid(spaj, 1 ,i)==0){//belom lunas topupnya
//						err.reject("payment.notEnough");
//					}
//				}
//			}
			
			//bila produk link muamalat
			//Yusuf (15 jul 2011) disabled request Ariani via email
			//tidak perlu dicegat, agar tetap masuk produksi, sehingga data BMI akan tetap terkirim ke muamalat (tanpa menunggu data NAB di H+1)
			
//			String lsbs_id = uwManager.selectBusinessId(spaj);
//			if(products.muamalat(spaj) && products.unitLink(lsbs_id)){
//				List<Date> asdf = uwManager.selectSudahProsesNab(spaj);
//				boolean oke = true;
//				for(Date d : asdf){
//					if(d == null) {
//						oke = false;
//						break;
//					}
//				}
//				if(!oke) err.reject("", "Harap selesaikan dulu proses-proses unit link seperti fund, nab, dan print surat.");
//			}
		
			//yg bisa ditransfer, hanya yg status aksepnya AKSEP atau AKSEPTASI KHUSUS
			Map info = uwManager.selectValidasiSebelumEditDate(spaj);
			int lssa = ((BigDecimal) info.get("LSSA_ID")).intValue();
			String aksep = (String) info.get("STATUS_ACCEPT");
			if(lssa != 5 && lssa != 10){
				err.reject("", "Status polis masih " + aksep + ". Tidak dapat ditransfer kecuali status AKSEPTASI NORMAL atau KHUSUS!");
			}
			
			String businessId = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 2);
			String businessNumber = uwManager.selectLsdbsNumber(spaj);
			if(uwManager.selectCountKoefisienUpp(businessId, businessNumber)<=0){
				err.reject("", "Rate UPP Kode Plan ("+businessId+"-"+businessNumber+") belum dimasukkan, Silakan menghubungi IT mengenai perihal ini.");
			}
			
			//Deddy (11 Oct 2012) - REQ IWEN : SEMUA POLIS HARUS MENGINPUT IBANK, APABILA TIDAK ADA KONFIRM KE FINANCE UNTUK CREATE IBANK.
			//Deddy (12 Oct 2012) - Permintaan User dan disetujui Pak IWEN, untuk non cash, tidak perlu input IBANK.
			//Andhika (13 Nov 2012) - Permintaan User jika autodebet tidah perlu input I-BANK
			List<DrekDet> mstDrekDetBasedSpaj = uwManager.selectMstDrekDet( null, spaj, null, null );
			List<Payment> payment = this.elionsManager.selectPaymentCount(spaj, 1, 1);
			Account_recur account_recur = elionsManager.select_account_recur(spaj);
			if(payment.get(0).getLsrek_id()!=0){
				if(mstDrekDetBasedSpaj.size()==0){
					if(account_recur.getFlag_autodebet_nb() != null){
						if(account_recur.getFlag_autodebet_nb()!=1){
							err.reject("", "Silakan menginput I-Bank terlebih dahulu sebelum transfer. Apabila Ibank tidak ada, silakan dikonfirmasikan ke pihak Finance.");
						}
					}
				}
			}
			
			//Deddy (25 Jan 2013) - Apabila beg_date/end_date di product_insured dgn billing berbeda, langsung dicegat dan dilakukan pengecekan apakah ada kesalahan secara system atau edit beg_date yang masih kurang.Perhatikan juga mspo_bill_date.
			Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
			List listBilling = elionsManager.selectBillingInformation(spaj, 1, 1);
			Map dataBilling = (HashMap) listBilling.get(0);
			if(dataUsulan.getMste_beg_date().compareTo((Date) dataBilling.get("MSBI_BEG_DATE"))!=0){
				err.reject("", "BegDate Produk tgl "+  defaultDateFormat.format(dataUsulan.getMste_beg_date())+" dengan BegDate Billing tgl "+defaultDateFormat.format((Date) dataBilling.get("MSBI_BEG_DATE"))+" berbeda.Silakan dikonfirmasikan dengan IT mengenai perihal ini.");
			}
//			if(dataUsulan.getMste_end_date().compareTo((Date) dataBilling.get("MSBI_END_DATE"))!=0){
//				err.reject("", "EndDate Produk tgl "+  defaultDateFormat.format(dataUsulan.getMste_end_date())+" dengan EndDate Billing tgl "+defaultDateFormat.format((Date) dataBilling.get("MSBI_END_DATE"))+" berbeda.Silakan dikonfirmasikan dengan IT mengenai perihal ini.");
//			}
			
			String lca_id = elionsManager.selectCabangFromSpaj(spaj);
			//18-03-2015-->Update DMTM tidak perlu cek level komisi ke 4 nya
			if("01,09,40,58,60".indexOf(lca_id)<=-1){
		 		List lds_kom = this.uwManager.selectViewKomisiAsli(spaj);
		 		if(!lds_kom.isEmpty()){
		 			Commission kms = (Commission) lds_kom.get(0);
		 			String[] to = new String[] {"Karunia@sinarmasmsiglife.co.id;jelita@sinarmasmsiglife.co.id","hisar@sinarmasmsiglife.co.id"};
					String[] cc = new String[] {"tri.handini@sinarmasmsiglife.co.id"};
					
					if( lca_id.indexOf("08,42,62,68") >=0){//request Wesni, apabila worksite/mnc
						to = new String[] {"wesni@sinarmasmsiglife.co.id"};
						cc= null;
					}
					
					if(kms.getLev_kom()!=4 && !(kms.getBisnis_id()==190 && kms.getBisnis_no()==9) && !(kms.getBisnis_id()==200 && kms.getBisnis_no()==7)){
						err.reject("", "Ada kesalahan Struktur Agent, dimana Komisi Utama Untuk Polis ini tidak diberikan ke Agent Penutupnya. Silakan dikonfirmasikan ke Pihak Distribution Support mengenai perihal ini");
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
								null, 0, 0, new Date(), null, 
								false,props.getProperty("admin.ajsjava"),to,cc ,new String[] {"ridhaal@sinarmasmsiglife.co.id"},
								"[System Warning] Pengecekan level Agent untuk SPAJ " + spaj, 
								"Kepada Yth.\n"+
								"Bagian Distribution Support di tempat.\n\n"+
								"Mohon dilakukan pengecekan pada SPAJ ini, karena untuk Komisi Utamanya tidak diberikan ke agent penutup langsung. Namun diberikan kepada kode Agent Leadernya yakni :"+kms.getAgent_id()+
								"\n\nTerima kasih.",  null, spaj);
					}
		 		}
				
			}
		}
		
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException err) throws Exception {
		
		if(request.getParameter("submitSuccess")==null) {
			if(request.getParameter("selectKurang") != null) {
				String verify = ServletRequestUtils.getStringParameter(request, "password", "").toUpperCase(); 
				if(!elionsManager.validationVerify(2).get("PASSWORD").toString().trim().equals(verify)){ //cek otorisasi password
					err.reject("payment.notAuthorized");
				}				
			}
			if(request.getParameter("selectKurangTopUp") != null) {
				String verify = ServletRequestUtils.getStringParameter(request, "password", "").toUpperCase(); 
				if(!elionsManager.validationVerify(2).get("PASSWORD").toString().trim().equals(verify)){ //cek otorisasi password
					err.reject("payment.notAuthorized");
				}				
			}
		}
		
		//tambahan validasi untuk produk simas prima jikalau akseptasi khusus tidak bisa transfer
		//15/02/2008 -- Ferry
//		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		//jn bank =2 bahwa polis tersebut produk simas prima
		//lssa_id =10 akseptasi khusus
//		Integer count=elionsManager.selectCountProductSimasPrimaAkseptasiKhusus(spaj, 1,10,2);
//		
//		if(count>0){
//			err.reject("","Produk Simas Prima Masih Terakseptasi Khusus. Tidak Bisa di transfer");
//		}		
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		String from = ServletRequestUtils.getRequiredStringParameter(request, "from");
		String lca_id = elionsManager.selectCabangFromSpaj(spaj);
		String pesanDirectPrint = "";
		String li_retur="-1";
		String li_returTopUp="-1";
		int bulanRK=-1;
		
		if(request.getParameter("transfer")!=null) {
			 spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			li_retur = ServletRequestUtils.getStringParameter(request, "selectKurang", "3");
			li_returTopUp = ServletRequestUtils.getStringParameter(request, "selectKurangTopUp", "3");
			bulanRK = ServletRequestUtils.getIntParameter(request, "selectBulan", 0);
		}

		if(!li_retur.equals("-1") && request.getParameter("submitSuccess")==null) {
			if(this.elionsManager.transferToPrintPolis(spaj, err, bulanRK, currentUser, li_retur, li_returTopUp, from)==false){
				err.reject("payment.transferFailed");
			}
		}

		if(err.hasErrors()) {
			return new ModelAndView("uw/transfer_to_print", err.getModel());
		}else {
			//Yusuf - 14/12/2007 - Apabila inputan bank -> dipakai untuk disable validasi reff BII dan titipan premi
			int isInputanBank = elionsManager.selectIsInputanBank(spaj);
			Integer count = elionsManager.selectCountProductSimasPrimaAkseptasiKhusus(spaj, 1,10, isInputanBank);			
			String flag = "0";
			String businessId = uwManager.selectBusinessId(spaj);
			String businessNo = uwManager.selectLsdbsNumber(spaj);
			String pesanKemenangan;
			Map tambahan = new HashMap();
			//PROJECT: POWERSAVE & STABLE LINK 			
			if(isInputanBank==2 || isInputanBank==3 || (businessId.equals("175") && businessNo.equals("2")) || (businessId.equals("73") && businessNo.equals("14"))) {
			//if(products.powerSave(businessId) || products.stableLink(businessId)){
				if(businessId.equals("73") && businessNo.equals("14")){//MANTA (13/01/2014) - Request Andy
					pesanKemenangan = "SPAJ nomor " + spaj + " berhasil ditransfer ke Filling.";
				}else if(isInputanBank==2){
					pesanKemenangan = "SPAJ nomor " + spaj + " berhasil ditransfer ke Print Polis.";
				}else if(products.productBsmFlowStandardIndividu(Integer.parseInt(businessId), Integer.parseInt(businessNo))){
					pesanKemenangan = "SPAJ nomor " + spaj + " berhasil ditransfer ke Print Polis.";
				}else{
					if(count>0){ //AKSEPTASI KHUSUS
						pesanKemenangan = "SPAJ nomor " + spaj + " berhasil ditransfer ke Print Polis. Status masih Akseptasi Khusus.";
					}else { //AKSEPTASI BIASA
						pesanKemenangan = "SPAJ nomor " + spaj + " berhasil ditransfer ke Input Tanda Terima.";
					}
					tambahan.put("transferTTP", true);
					tambahan.put("spaj", spaj);
					flag ="1";
				}
			}else if(businessId.equals("157") ) {
				pesanKemenangan = "SPAJ nomor " + spaj + " berhasil ditransfer ke Input Tanda Terima.";
			}else if( businessId.equals("203") && "4".indexOf(businessNo)>-1 ) {
				pesanKemenangan = "SPAJ nomor " + spaj + " berhasil ditransfer ke Print Polis.";
			}else if("187,203,209".indexOf(businessId) > -1) {
				Map m = uwManager.selectInformasiEmailSoftcopy(spaj);
				String errorRekening = elionsManager.cekRekAgen2(spaj);
				String email = (String) m.get("MSPE_EMAIL");
				String keterangan = null;
				if(email != null) {
					if((businessId.equals("187") && "5,6".indexOf(businessNo)>-1)){
						keterangan = "Filling.Softcopy Telah dikirimkan ke : " + email;
					}else{
						if(!errorRekening.equals("")) {
							keterangan = "PROSES CHECKING TTP (REKENING AGEN MASIH KOSONG).Softcopy Telah dikirimkan.";
						}else{
							keterangan = "Komisi (Finance).Softcopy Telah dikirimkan.";
						}
					}
					pesanKemenangan = "SPAJ nomor " + spaj + " berhasil ditransfer ke " + keterangan;
				}else{
					if((businessId.equals("187") && "5,6".indexOf(businessNo)>-1)){
						keterangan = "Filling.";
					}else{
						if(!errorRekening.equals("")) {
							keterangan = "PROSES CHECKING TTP (REKENING AGEN MASIH KOSONG).Softcopy Telah dikirimkan.";
						}else{
							keterangan = "Komisi (Finance).";
						}	
					}
					pesanKemenangan = "SPAJ nomor " + spaj + " berhasil ditransfer ke " + keterangan;
				}
			}else {
				pesanKemenangan = "SPAJ nomor " + spaj + " berhasil ditransfer ke Print Polis.";
			}
			
			Integer mspo_provider= uwManager.selectGetMspoProvider(spaj);
			String LusId = currentUser.getLus_id();
			String lde_id = currentUser.getLde_id();
			
			try{
				//start - untuk melakukan proses BTN - Link SMS
				//Ridhaal - Bank BTN - Pembuatan ShortUrl dengan melakukan pengeneratan file yang akan di simpan di link untuk di download oleh pemegang polis (SURL-2/2)
				//Metode juga di jalankan di proses BacManager.java - ProsesTransferToPrintPolis (SURL-1/2)
				//Metode ini di jalankan di proses TransferToPrintPolisFormController.java - onSubmit (SURL-2/2)
				List tmp = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				String lsdbs_id = (String) ((Map) tmp.get(0)).get("DETBISNIS");
				if((lsbs.equals("195") &&  "013,014,015,016,017,018,019,020,021,022,023,024".indexOf(lsdbs_id) > 0 ) 
					|| (lsbs.equals("183") &&  "076,077,078,079,080,081,082,083,084,085,086,087,088,089,090".indexOf(lsdbs_id) > 0 )
					|| (lsbs.equals("187") &&  "012,013,014".indexOf(lsdbs_id) > 0 )
					|| (lsbs.equals("169") &&  "034,035".indexOf(lsdbs_id) > 0 )
					|| (lsbs.equals("212") &&  "001".indexOf(lsdbs_id) > 0 )) 
				{
						PdfReader reader = null;
						String cabang = elionsManager.selectCabangFromSpaj(spaj);
						
						//untuk produk pas tergenerate pada saat pengiriman softcopy
						if (!lsbs.equals("187")){//non produk PAS	
							//generate report PolisAll
							bacManager.generateReport(request,mspo_provider,3 , elionsManager,uwManager, 1 , spaj );
						}										
						
						//untuk set password IText PDF										 
						if (lsbs.equals("187")){//Produk PAS
							 reader = new PdfReader(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+"polis_pas.pdf");												
						}else{//Non Produk PAS
							 reader = new PdfReader(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+"PolisAll.pdf");
						}								 
							 
						 String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+"PolisSMS.pdf";
					     PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
					     String PwsdPolisSMSPP = bacManager.selectTglLhrPP(spaj);
					     stamp.setEncryption(PwsdPolisSMSPP.getBytes(), "417M1N".getBytes(), PdfWriter.AllowPrinting | PdfWriter.AllowCopy, PdfWriter.STRENGTH40BITS);
					     stamp.close(); 							     
					     FileUtils.deleteFile(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\", "PolisAll.pdf");
					     
					     //Create link URL
					     String id =  elionsManager.selectEncryptDecrypt(spaj, "en");
					     //cek jika hasil enkripsi SPAJ ada tanda #, jika ada di encrypsi ulang karena longURL Id tidak boleh ada tanda #
					     while(id.contains("#")){
					    	 id =  elionsManager.selectEncryptDecrypt(spaj, "en");
					     }							     
					   
					     String LongURL   = "https://elions.sinarmasmsiglife.co.id/common/util.htm?window=publics&id="+id;
					 //  String LongURL   = "http://localhost:8080/E-Lions/common/util.htm?window=publics&id="+id;
					     
					     //pemendekan URL untuk di SMS (URLSHORTENER) menggunakan Google	
					     //cek jika shortlink sudah ada atau belom di data base EKA.MST_URL_SECURE_FILE
					     List Flag_linkBtn = bacManager.cekUrlSecureFile(spaj, "LINK SMS BTN" );
					     if (Flag_linkBtn.isEmpty()){
						     String ShortURL = bacManager.prosesUrlShortener(LongURL); 
						     logger.info(bacManager.prosesUrlShortener(LongURL));	
						     Date sysdate = elionsManager.selectSysdate();
						     bacManager.insertUrlSecureFile(sysdate ,spaj, "LINK SMS BTN", 1, id , LongURL, null, ShortURL);
						     elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "Proses Pengeneretan Short URL : " + ShortURL , spaj, 1);
								
					     }
				}//end - untuk melakukan proses BTN - Link SMS
		
			}catch(Exception e){
				logger.error("ERROR :", e);
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, new Date(), null, false, props.getProperty("admin.ajsjava"), new String[]{"ryan@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id;ridhaal@sinarmasmsiglife.co.id","antasari@sinarmasmsiglife.co.id"}, null, null,  
						"Error proses Create URLLink Shortener for SMS on Transfer Print Polis", 
						e+"", null, spaj);
			}
			
			//20190611 Mark - DISABLE DIRECT PRINT
//			try{
//				
//				if( businessId.equals("163") || ( businessId.equals("143") && ("1,2,3,7".indexOf(businessNo)>-1) && lca_id.equals("01") ) || (businessId.equals("144") && businessNo.equals("1") && lca_id.equals("01") ) ||
//					businessId.equals("183") || ((businessId.equals("142") && !businessNo.equals("11")) && lca_id.equals("01")) || (businessId.equals("177") && businessNo.equals("4")) 
//					|| (businessId.equals("197") && businessNo.equals("2")))
//				{
//					if("11,39".indexOf(lde_id)>-1){
//						String email = currentUser.getEmail();
//						Integer flagprint = 1;
//						if(businessId.equals("177") && businessNo.equals("4")) flagprint = 4; //Khusus PT INTI
//						
//						elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", LusId, 6, 1, "PRINT POLIS (E-LIONS) DIRECT PRINT", true, currentUser);
//						
//						if( (businessId.equals("197") && businessNo.equals("2")) ) {
//							PDFViewer.genItextTemplate1(elionsManager, bacManager, props, "PolisAll.pdf", false, 0, spaj, businessId, businessNo);
//						}else if( (businessId.equals("212") && businessNo.equals("8")) ) {
//							PDFViewer.genItextTemplate1(elionsManager, bacManager, props, "PolisAll.pdf", false, 1, spaj, businessId, businessNo);
//						}else {
//							ppc.generateReport(request, mspo_provider, flagprint);
//						}
//						
//						HashMap<String, Object> printer = (HashMap<String, Object>) this.bacManager.selectPropertiesPrinter();
//						String ipAddress = (String) printer.get("IP_ADDRESS");
//						String printerName = (String) printer.get("PRINTER_NAME");
//
//						
//						String cabang = elionsManager.selectCabangFromSpaj(spaj);
//						String allowPrint = this.bacManager.getAllowPrint(printerName);
//						String pdfFile = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+"PolisAll.pdf";
//						
//						
//						try{
//							ThreadPrint T1 = new ThreadPrint(printerName,"directPrint",pdfFile,allowPrint,spaj,currentUser.getLus_id(),Print.getCountPrint(pdfFile));
//							T1.start();
//							
//							//randy
//							if(cabang.equals("40")){
//								String f2 = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+"espajDMTM2_"+spaj+".pdf";
//								File espajdmtm = new File (f2);
//								if (espajdmtm.exists()){
//									ThreadPrint T2 = new ThreadPrint(printerName,"directPrint",f2,allowPrint,spaj,currentUser.getLus_id(),Print.getCountPrint(pdfFile));
//									T2.start();
//								}
//							}
//						}catch(Exception e){
//							EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
//									null, 0, 0, new Date(), null, false,
//									props.getProperty("admin.ajsjava"),
//									new String[]{"ryan@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id;antasari@sinarmasmsiglife.co.id;randy@sinarmasmsiglife.co.id;ridhaal@sinarmasmsiglife.co.id;trifena_y@sinarmasmsiglife.co.id"},
//									null,
//									null,  
//									"Error Thread print", 
//									e+"", null, spaj);
//						}
//						
//						pesanDirectPrint = bacManager.prosesPrint(spaj,cabang,ipAddress,printerName);
//					}
//				}
//			}catch(Exception e){
//				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
//						null, 0, 0, new Date(), null, false,
//						props.getProperty("admin.ajsjava"),
//						new String[]{"ryan@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id;antasari@sinarmasmsiglife.co.id;randy@sinarmasmsiglife.co.id;ridhaal@sinarmasmsiglife.co.id;trifena_y@sinarmasmsiglife.co.id"},
//						null,
//						null,  
//						"Error Direct Print Transfer", 
//						e+"", null, spaj);
//			}
			
			//Mark Valentino 20190313 Revisi PA Konven - Inforce -> Email Sertifikat ke Customer
			try{
				if(businessId.equals("73") && ("14".indexOf(businessNo)>-1)){
					request.setAttribute("produk", "PA Konven");
					PrintPolisAllPelengkap ppap = new PrintPolisAllPelengkap();
					String resultEmail = ppap.emailPaKonven(request, response, elionsManager, uwManager, props, spaj);
					pesanKemenangan = pesanKemenangan + "<br><br>" + resultEmail;
				}
			}catch(Exception e){
				logger.error("ERROR :", e);				
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, new Date(), null, false,
						props.getProperty("admin.ajsjava"),
						new String[]{"mark.valentino@sinarmasmsiglife.co.id"},
						null,
						null,  
						"Error Auto Email PA Konven", 
						e+"", null, spaj);
			}	
			
			request.getSession().setAttribute("inbox_flag_transfer_document", 0);
			return new ModelAndView(new RedirectView("transfer_to_print.htm")).addObject("submitSuccess", pesanKemenangan).addAllObjects(tambahan).addObject("pesanDirectPrint", pesanDirectPrint);
		}
	}
	
	class ThreadPrint extends Thread implements Serializable{
		   
		private static final long serialVersionUID = -2208935165403596233L;
		private Thread t;
		   private String printerNameThread;
		   private String pdfFileThread;
		   private String allowPrintThread;
		   private String ThreadPrint;
		   private String spajPrint;
		   private Integer CountPrint;
		   private String lusIdPrint;
		   
		   ThreadPrint(String printerName,String name,String pdfFile,String allowPrint,String spaj,String lusId,Integer Count){
			   ThreadPrint = name;
			   printerNameThread = printerName;
		       pdfFileThread =pdfFile;
			   allowPrintThread = allowPrint;
			   spajPrint = spaj;
			   CountPrint = Count;
			   lusIdPrint = lusId;
		   }
		   
		   public void run() {
		      if(Print.directPrint(pdfFileThread, printerNameThread, allowPrintThread)){
		    	  bacManager.insertPrintHistory(spajPrint, "POLIS ALL", CountPrint, lusIdPrint);
			  }

		   }
		   
		   public void start ()
		   {
		      if (t == null)
		      {
		         t = new Thread (this, ThreadPrint);
		         t.start ();
		      }
		   }
		}
}
