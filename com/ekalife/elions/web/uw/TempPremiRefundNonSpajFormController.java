package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.utils.parent.ParentFormController;

public class TempPremiRefundNonSpajFormController extends ParentFormController{

	//Fungsi ini untuk mengubah bentuk angka baik type data double, integer, dan date. Misal: hasil keluarnya 12.234AF menjadi 12.234.567
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
		binder.registerCustomEditor(BigDecimal.class, null, new CustomNumberEditor( BigDecimal.class, new DecimalFormat("###,##0.00") , true ));
	}
	
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		PolicyInfoVO command = (PolicyInfoVO) cmd;
		Map map = new HashMap();
		map.put("listBankPusat", elionsManager.selectDropDown("eka.lst_bank_pusat", "lsbp_nama", "lsbp_nama", "", "2", null));
		map.put("listKurs", elionsManager.selectDropDown("eka.lst_kurs", "lku_id", "lku_symbol", "", "2", "lku_id in ('01','02')"));
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		PolicyInfoVO command = new PolicyInfoVO();
		command = (PolicyInfoVO) bacManager.getDataRefundCentrix(command);
		command.setTgl_rk(bacManager.selectSysdate());
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Date now = bacManager.selectSysdate();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		PolicyInfoVO command = (PolicyInfoVO) cmd;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "namaPp", "", "Harap lengkapi Nama Pemegang Polis!");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "kliAtasNama", "", "Harap lengkapi Nama Pemegang Rekening!");
    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "kliNoRek", "", "Harap lengkapi No Rekening Nasabah!");
    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "kliNamaBank", "", "Harap lengkapi Nama Bank!");
    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "kliCabangBank", "", "Harap lengkapi Cabang Bank!");
    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "kliKotaBank", "", "Harap lengkapi Kota Bank!");
    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "lkuId", "", "Harap pilih kurs terlebih dahulu!");
    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "no_trx", "", "Harap pilih Ibank terlebih dahulu!");
    	
    	if(command.getPremitmp() == null || command.getPremitmp().intValue() == 0){
    		err.rejectValue("premitmp","","Jumlah setoran premi tidak boleh 0 atau kosong!");
    	}
    	
    	command.setNamaPp(command.getNamaPp().toUpperCase());
    	command.setKliAtasNama(command.getKliAtasNama().toUpperCase());
    	command.setKliNamaBank(command.getKliNamaBank().toUpperCase());
    	command.setKliCabangBank(command.getKliCabangBank().toUpperCase());
    	command.setKliKotaBank(command.getKliKotaBank().toUpperCase());
    	
		//Validasi untuk cek Kurs
		Double kursBeli = new Double(1);
		command.setNilaiKurs(kursBeli);
		if(command.getLkuId().equals("02")){
			kursBeli = bacManager.selectGetKursJb(df.parse(df.format(now)),"B");
			if(kursBeli == null){
				err.rejectValue("pesan","","Kurs tgl " + df.format(now) + " (dd/MM/yyyy) tidak ada");
			}else{
				command.setNilaiKurs(kursBeli);
			}
		}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		PolicyInfoVO command = (PolicyInfoVO) cmd;
		
		if(request.getParameter("btnSave") != null){
			command.pesan = bacManager.prosesTempPremiRefund(command, 0, currentUser.getLus_id());
		}
		
		return new ModelAndView("uw/tmp_premi_refund_nonspaj", err.getModel()).addAllObjects(this.referenceData(request,command,err));
	}

}
