package com.ekalife.elions.web.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Aktivitas;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Nasabah;
import com.ekalife.utils.parent.ParentFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReferralNasabahFormController extends ParentFormController {
	NumberFormat f2=new DecimalFormat("00");
	NumberFormat f5=new DecimalFormat("00000");
	protected final Log logger = LogFactory.getLog( getClass() );

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
		//binder.registerCustomEditor(BigDecimal.class, null, decimalEditor);
		binder.registerCustomEditor(BigDecimal.class, null, new CustomNumberEditor( BigDecimal.class, new DecimalFormat("###,##0.00") , true ));
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		Nasabah nasabah = new Nasabah();
		Date sysdate = elionsManager.selectSysdateSimple();
		String nomor=ServletRequestUtils.getStringParameter(request,"nomor","");
		String kdNasabah=null,noReferral=null;
		if(! nomor.equals("")){
			int pos=nomor.indexOf('~');
			noReferral=nomor.substring(0,pos);
			kdNasabah=nomor.substring(pos+1,nomor.length());
		}
		String mns_kd_nasabah = kdNasabah;
		String nm_jenis = uwManager.selectJenisReferral(mns_kd_nasabah);
		nasabah.setNm_jenis(nm_jenis);
		nasabah.setMns_kd_nasabah(mns_kd_nasabah);
		nasabah.setMns_no_ref(noReferral);
		command.setNasabah(nasabah);
		String reg_spaj = uwManager.selectCountRegSpaj(mns_kd_nasabah);
		logger.info(reg_spaj);
		if(reg_spaj!= null ){
			command.setReg_spaj(reg_spaj);
			command.setCount(1);
			String nama_produk = uwManager.selectBusinessName(reg_spaj);
			command.setNama_produk(nama_produk);
		}else { 
			command.setReg_spaj("");
		}
		command.setSysdate(sysdate);
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		if(request.getParameter("cekspaj")!=null){
			String reg_spaj = command.getReg_spaj();
			String nama_produk = uwManager.selectBusinessName(reg_spaj);
			if(nama_produk!=null){
				command.setNama_produk(nama_produk);
				err.reject("", "Munculkan Nama Produk");
			}else err.reject("", "No SPAJ tidak ada");
		}
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		Nasabah nasabah = command.getNasabah();
		String reg_spaj = command.getReg_spaj();
		if(request.getParameter("save")!=null){
			uwManager.updateRegSpajIntoNasabah(nasabah, reg_spaj);
		}
		return new ModelAndView("common/referral_nasabah",err.getModel()).addObject("submitSuccess","true");
	}
	
}