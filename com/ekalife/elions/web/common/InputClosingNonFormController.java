package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Nasabah;
import com.ekalife.utils.parent.ParentFormController;

public class InputClosingNonFormController extends ParentFormController {
	NumberFormat f2=new DecimalFormat("00");
	NumberFormat f5=new DecimalFormat("00000");
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
		//binder.registerCustomEditor(BigDecimal.class, null, decimalEditor);
		binder.registerCustomEditor(BigDecimal.class, null, new CustomNumberEditor( BigDecimal.class, new DecimalFormat("###,##0.00") , true ));
	}

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Command command=(Command)cmd;
		List lstProduct;
		Map map = new HashMap();
		lstProduct = elionsManager.selectListRekomendasi();
		map.put("lstProduct", lstProduct);
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		String nomor=ServletRequestUtils.getStringParameter(request,"nomor","");
		String flag=ServletRequestUtils.getStringParameter(request, "flag","");
		String tipe=ServletRequestUtils.getStringParameter(request, "tipe","");
		Nasabah nasabah = new Nasabah();
		String kdNasabah=null,noReferral=null;
		if(! nomor.equals("")){
			int pos=nomor.indexOf('~');
			noReferral=nomor.substring(0,pos);
			kdNasabah=nomor.substring(pos+1,nomor.length());
		}
		String mns_kd_nasabah = kdNasabah;
		nasabah=elionsManager.selectMstNasabah(kdNasabah);
		command.setNasabah(nasabah);
		
		if(flag.equals("2")){//bagian ini untuk view dari aktivitas saja
			command.setFlagAdd(2);
		}
		
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		Nasabah nasabah = command.getNasabah();
		
		if(nasabah.getMns_tgl_tt()==null){
			err.reject("", "Tanggal harap diisi");
		}
		
		if(nasabah.getMns_up()==null){
			err.reject("", "Besar UP harap diisi");
		}
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		if(request.getParameter("save")!= null){
			elionsManager.updateMstNasabah(command.getNasabah());
		}else{ 
			command.setFlagAdd(1);
			elionsManager.prosesTransClosingNon(command);
		}
		
		return new ModelAndView("common/input_closingnon",err.getModel()).addObject("submitSuccess","true").addAllObjects(this.referenceData(request,command,err));
	}
	
}