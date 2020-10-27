package com.ekalife.elions.web.bac;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Billing;
import com.ekalife.elions.model.CommandInputPayment;
import com.ekalife.elions.model.TopUp;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentWizardController;

/**
 * Wizard Controller untuk input payment successive
 * cari spaj -> pilih billing mana yg mau diinput -> input payment lalu save
 * 
 * @author yusuf
 * @since Aug 10, 2009 (9:55:53 AM)
 */
public class InputPaymentSuccWizardController extends ParentWizardController {
	
	public InputPaymentSuccWizardController() {
		setCacheSeconds(1);		
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, 	null, dateEditor);
		binder.registerCustomEditor(Integer.class, 	null, integerEditor);
		binder.registerCustomEditor(Double.class, 	null, doubleEditor);
	}

	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
		Map<String, Object> ref = new HashMap<String, Object>();
		
		ref.put("daftarRekeningAJS", 	elionsManager.selectLstBank2());
		ref.put("daftarPaymentType", 	elionsManager.selectAllLstPaymentType());		
	
		ref.put("listBankPusat", 		this.uwManager.selectBankPusat());
		
		return ref;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		setAllowDirtyBack(true);
		CommandInputPayment cip = new CommandInputPayment();
		cip.payment 			= new TopUp();
		return cip;
	}

	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		CommandInputPayment cip = (CommandInputPayment) command;
		User currentUser 		= (User) request.getSession().getAttribute("currentUser");
	
		request.setAttribute("catatan", "Silahkan pilih salah satu Pembayaran diatas untuk merubah data, atau tekan tombol INPUT PEMBAYARAN BARU untuk menambah pembayaran");

		int target = getTargetPage(request, page); 
		
		//validasi dilakukan hanya kalo ke halaman next, bukan ke halaman previous
		if(target > page) {
			//Step 1 : masukkan nomor SPAJ / Polis
			if(page==0) {
				//rapihkan dulu kolom2nya
				cip.reg_spaj = cip.reg_spaj.trim().toUpperCase().replace(".", "");
				
				//bila yg dimasukkan nomor spaj
				if(cip.getReg_spaj().length() == 11){
					cip.mspo_policy_no 	= elionsManager.selectPolicyNumberFromSpaj(cip.reg_spaj);
				//bila yg dimasukkan nomor polis
				}else if(cip.getReg_spaj().length() == 14){
					cip.mspo_policy_no 	= cip.reg_spaj;
					cip.reg_spaj 		= elionsManager.selectSpajFromPolis(cip.reg_spaj);
				}else{
					errors.rejectValue("reg_spaj", "", "Nomor SPAJ / Polis yang anda masukkan tidak valid. Harap ulangi kembali.");
				}

				//validasi berikutnya adalah hak akses cabang (EKA.LST_USER_ADMIN)
				if(!errors.hasErrors()){
					List akses 		= currentUser.getAksesCabang();
					String region 	= elionsManager.selectCabangFromSpaj_lar(cip.reg_spaj);
					if (!akses.contains(region)) {
						// errors.rejectValue("reg_spaj", "", "Anda tidak mempunyai akses terhadap polis ini. Silahkan konfirmasi dengan BAS.");
					}
				}
				
				if(!errors.hasErrors()){
					//tarik data billing outstanding, yg boleh diinput payment hanya yg posisi 12
					cip.daftarBilling = uwManager.selectBillingInformationSucc(cip.reg_spaj, 12); //posisi 12 (INPUT PAYMENT SUCCESSIVE)
					if(cip.daftarBilling.isEmpty()){
						errors.rejectValue("reg_spaj", "", "Saat ini Polis ini tidak mempunyai Billing Outstanding. Silahkan konfirmasi dengan Finance.");
					}else{
						//tarik data umum polis nya
						cip.dataPolis 	= elionsManager.selectRolloverData(cip.reg_spaj);
						cip.bill_lku_id = cip.daftarBilling.get(0).getLku_id();
					}
				}
			
			//Step 2 : pilih billing yg ingin dibayar, yg boleh diinput hanya yg belum lunas
			}else if(page==1) {
				int jml = 0;
				for(Billing b : cip.daftarBilling){
					if(b.getPilih() != null){
						if(b.getPilih().intValue() == 1){
							jml++;
							if(b.getMsbi_paid().intValue() == 1){
								errors.reject("", "Billing yang dipilih sudah LUNAS. Input pembayaran tidak bisa dilanjutkan.");
							}
							cip.msbi_premi_ke = b.getMsbi_tahun_ke();
							cip.msbi_tahun_ke = b.getMsbi_premi_ke();
						}
					}
				}
				if(jml != 1) {
					errors.reject("", "Silahkan pilih cukup SATU Billing untuk melanjutkan input pembayaran.");
				}
				
				//tarik data payment history nya
				if(!errors.hasErrors()){
					cip.daftarPayment = elionsManager.selectPaymentCount(cip.reg_spaj, cip.msbi_tahun_ke, cip.msbi_premi_ke);
				}

				//reset values
				cip.mode = "";
				cip.mspa_payment_id = "";
				
			//Step 3 : input payment
			}else if(page==2) {
				if(!cip.mode.equals("")){
					request.setAttribute("catatan", "Silahkan lanjutkan penginputan kemudian menekan tombol SAVE.");
					if(cip.mode.equals("insert")){
						cip.payment = new TopUp();
					}else if(cip.mode.equals("update")){
						cip.payment = elionsManager.selectTopUp(
								cip.reg_spaj, 
								cip.mspa_payment_id, 
								cip.msbi_tahun_ke, 
								cip.msbi_premi_ke);
					}
				}
				errors.reject("", "");
				
			//Step 4 :
			}else if(page==3) { 
				
			}else errors.reject("", "-");
		}
		
		//validasi final, apabila ditekan tombol SAVE
		if(isFinishRequest(request)){
			request.setAttribute("catatan", "Silahkan lanjutkan penginputan kemudian menekan tombol SAVE.");
			
			//1. Validasi2 tidak boleh kosong
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payment.lsrek_id", "", 		"Harap isi Rekening AJS");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payment.mspa_date_book", "", "Harap isi Tgl RK");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payment.pay_date", "", 		"Harap isi Tgl Bayar");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payment.lsjb_id", "", 		"Harap isi Cara Bayar");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payment.lku_id", "", 		"Harap isi Mata Uang");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payment.mspa_payment", "", 	"Harap isi Jumlah Bayar");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payment.client_bank", "", 	"Harap isi Client Bank");
			//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "payment.mspa_due_date", "", 	"Harap isi Tgl Jatuh Tempo");
			
			//2. Validasi2 lanjutan
			if(!errors.hasErrors()){
				Date sysdate = elionsManager.selectSysdate(0);
				
				//jumlah bayar harus > 0
				if(cip.payment.mspa_payment.doubleValue() <= 0) {
					errors.rejectValue("payment.mspa_payment", "", "Silahkan masukkan Jumlah Pembayaran");
				//tgl RK maksimal hari ini
				}else if(cip.payment.mspa_date_book.after(sysdate)){
					errors.rejectValue("payment.mspa_date_book", "payment.invalidRK");
				//cek kurs dollar untuk tgl RK
				}else if(!cip.payment.lku_id.equals(cip.payment.bill_lku_id) && elionsManager.validationDailyCurrency("02", cip.payment.mspa_date_book)==0) {
					errors.reject("payment.noDailyCurrency", new Object[] { defaultDateFormat.format(cip.payment.mspa_date_book) }, "Kurs tanggal {0} tidak ada, harap hubungi CS");
				//cek bila non bank, harus yg tipe pembayaran nonbank juga
				}else if(cip.payment.lsrek_id.equals("0")){
					List<Map> listPaymentType = elionsManager.selectAllLstPaymentType();
					for(Map m : listPaymentType){
						if(cip.payment.lsjb_id.intValue() == ((Integer) m.get("LSJB_ID")).intValue()){
							int tipe = ((Integer) m.get("LSJB_TYPE_BANK")).intValue();
							if(tipe != 0){
								errors.rejectValue("payment.lsjb_id", "", "Untuk jenis pembayaran NON BANK, anda tidak dapat memilih cara pembayaran ATM, BUNGA TUNGGAKAN, CHEQUE, CREDIT CARD, GIRO, PINDAH BUKU, TRANSFER, atau TUNAI.");
							}
						}
					}
				}
			}
			
		}
	}
	
	@Override
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandInputPayment cip = (CommandInputPayment) command;
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        cip.payment.setLca_polis(elionsManager.selectCabangFromSpaj(cip.reg_spaj));
        cip.payment.setBill_lku_id(cip.bill_lku_id);
        cip.payment.setReg_spaj(cip.reg_spaj);
        cip.payment.setTahun_ke(cip.msbi_tahun_ke);
        cip.payment.setPremi_ke(cip.msbi_premi_ke);
        Map results = new HashMap();
        
        String result = elionsManager.savePayment(cip.payment, 9, currentUser, null);
        //String result = "Data pembayaran berhasil disimpan.";
        
        results.put("result", result);
        
        return new ModelAndView("bac/inputpaymentsucc4", results);
	}
		
}