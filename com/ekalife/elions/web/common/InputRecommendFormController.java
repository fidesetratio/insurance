package com.ekalife.elions.web.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import com.ekalife.elions.model.Aspirasi;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Matrix;
import com.ekalife.elions.model.Nasabah;
import com.ekalife.elions.model.Rekomendasi;
import com.ekalife.utils.parent.ParentFormController;

public class InputRecommendFormController extends ParentFormController {
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
		List lstRekomendasi;
		String a = props.getProperty("jiffy.bancass");
		int b = a.indexOf("120");
		lstRekomendasi = elionsManager.selectListRekomendasi();
		Map map=new HashMap();
		map.put("lstRekomendasi", lstRekomendasi);
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		command.setShowTab(1); //tab yang ditampilkan adalah tab 1
		String nomor=ServletRequestUtils.getStringParameter(request,"nomor","");
		String flag=ServletRequestUtils.getStringParameter(request, "flag","");
		Nasabah nasabah = new Nasabah();
		Matrix matrix = new Matrix();
		Aspirasi aspirasi = new Aspirasi();
		Rekomendasi rekomendasi = new Rekomendasi();
		String kdNasabah=null,noReferral=null;
		if(! nomor.equals("")){
			int pos=nomor.indexOf('~');
			noReferral=nomor.substring(0,pos);
			kdNasabah=nomor.substring(pos+1,nomor.length());
		}
		aspirasi.setListAspirasi(elionsManager.selectMstAspirasi(kdNasabah));
		matrix.setListMatrix(elionsManager.selectMstMatrix(kdNasabah));
		rekomendasi.setListRekomendasi(elionsManager.selectMstRekomendasi(kdNasabah));
		nasabah=elionsManager.selectMstNasabah(kdNasabah);
		command.setNasabah(nasabah);
		command.setMatrix(matrix);
		command.setAspirasi(aspirasi);
		command.setRekomendasi(rekomendasi);
		
		if(flag.equals("2")){//bagian ini untuk view dari aktivitas saja
			command.setFlagAdd(2);
		}
		
		return command;
	}
	
		
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		if(request.getParameter("save")!=null){
			elionsManager.prosesInputRecommend(command);
		}else {
			command.setFlagAdd(1);
			elionsManager.prosesTransRecommend(command);
		}
		return new ModelAndView("common/input_recommend",err.getModel()).addObject("submitSuccess","true").addAllObjects(this.referenceData(request,command,err));
	}
	
	
}