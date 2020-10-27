package com.ekalife.elions.web.bas;

import java.io.File;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.utils.parent.ParentFormController;

public class RefundPolisSuccFormController extends ParentFormController{

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
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Date now = bacManager.selectSysdate();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		PolicyInfoVO command = (PolicyInfoVO) cmd;
		PolicyInfoVO data = new PolicyInfoVO();
		Upload upl = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upl);
		binder.bind(request);
		
		String spaj = command.getSpajNo();
		
		if(request.getParameter("btnCari") != null){
			data = (PolicyInfoVO) elionsManager.selectPolicyInfoBySpaj(spaj);

			if(data != null){
				command.setSpajNo(spaj);
				command.setPolicyNo(data.getPolicyNo());
				command.setNamaPp(data.getNamaPp());
				command.setKliAtasNama(data.getKliAtasNama());
				command.setKliNoRek(data.getKliNoRek());
				command.setKliNamaBank(data.getKliNamaBank());
				command.setKliCabangBank(data.getKliCabangBank());
				command.setKliKotaBank(data.getKliKotaBank());
				command.setLkuId(data.getLkuId());
				command.setPremi_ke(null);
				command.setPremirefundtmp(null);
				command.setTindakan(null);
				command.setKeterangan(null);
			}else{
				err.rejectValue("spajNo", "", "No SPAJ tidak terdaftar.");
			}
			
		} else if(request.getParameter("btnSubmit") != null){
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "kliAtasNama", "", "Kolom Nama Pemegang Rekening tidak boleh kosong!");
	    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "kliNoRek", "", "Kolom No Rekening Nasabah tidak boleh kosong!");
	    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "kliNamaBank", "", "Kolom Nama Bank tidak boleh kosong!");
	    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "kliCabangBank", "", "Kolom Cabang Bank tidak boleh kosong!");
	    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "kliKotaBank", "", "Kolom Kota Bank tidak boleh kosong!");
	    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "lkuId", "", "Kolom Kurs tidak boleh kosong!");
	    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "premi_ke", "", "Kolom Premi Ke tidak boleh kosong!");
	    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "premirefundtmp", "", "Kolom Jumlah tidak boleh kosong!");
	    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "tindakan", "", "Kolom Tindakan tidak boleh kosong!");
	    	ValidationUtils.rejectIfEmptyOrWhitespace(err, "keterangan", "", "Kolom Keterangan tidak boleh kosong!");
	    	
	    /*	String upl_file = "";
	    	if(upl.getFile1().isEmpty()){
	    		upl_file = upl_file + "BSB";
			}
	    	if(upl.getFile2().isEmpty()){
	    		if(!upl_file.equals("")) upl_file = upl_file + " - ";
	    		upl_file = upl_file + "Surat Pernyataan Refund";
			}
	    	if(upl.getFile3().isEmpty()){
	    		if(!upl_file.equals("")) upl_file = upl_file + " - ";
	    		upl_file = upl_file + "Cover Buku Tabungan";
			}
	    	if(!upl_file.equals("")) err.rejectValue("spajNo", "", "File " + upl_file + " tidak terdeteksi.");
		*/
	    	
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
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		PolicyInfoVO command = (PolicyInfoVO) cmd;
		Upload upl = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upl);
		binder.bind(request);
		
		if(request.getParameter("btnSubmit") != null){
			command.pesan = bacManager.prosesRefundPolisSucc(command, upl, currentUser);
			err.rejectValue("pesan", "", command.getPesan());
		}
		
		return new ModelAndView("bas/permintaan_refund_succ", err.getModel()).addAllObjects(this.referenceData(request,command,err));
	}

}
