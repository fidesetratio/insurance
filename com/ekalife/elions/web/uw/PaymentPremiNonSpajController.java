package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.DrekDet;
import com.ekalife.utils.parent.ParentFormController;

public class PaymentPremiNonSpajController extends ParentFormController {

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
	
	}

	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();
		BigDecimal jumlah;
		String no_trx = request.getParameter("no_trx");
		String norek_ajs = request.getParameter("norek_ajs");
		
		List<DropDown> temp = new ArrayList<DropDown>(); 
		temp.add(new DropDown("1@1","Premi"));
		
		List<DrekDet> mstDrekDetBasedNoTrx = uwManager.selectMstDrekDet(no_trx, null, null, norek_ajs); 
		String detailDisplay;
		if(mstDrekDetBasedNoTrx != null && mstDrekDetBasedNoTrx.size() > 0){
			detailDisplay = "true";
			Double total = 0.0;
			for(int i=0; i<mstDrekDetBasedNoTrx.size(); i++){
				mstDrekDetBasedNoTrx.get(i).setDescr("Premi utk SPAJ "+ mstDrekDetBasedNoTrx.get(i).getNo_spaj());
				if(mstDrekDetBasedNoTrx.get(i).getJumlah() == null){
					mstDrekDetBasedNoTrx.get(i).setJumlahForDisplay("0");
				}else{
					mstDrekDetBasedNoTrx.get(i).setJumlahForDisplay(mstDrekDetBasedNoTrx.get(i).getJumlah().toString());
				}
				
				total = total + mstDrekDetBasedNoTrx.get(i).getJumlah();
			}
			String totalDisplay = "0";
			if(total != null) totalDisplay = total.toString();
			map.put("total", totalDisplay);
			
		}else{
			detailDisplay = "none";
		}
		
		jumlah = new BigDecimal(uwManager.selectSumPremiMstDrekAndDet(no_trx, null));

		map.put("mstDrekDetBasedNoTrx", mstDrekDetBasedNoTrx);
		map.put("jumlah", jumlah.toString());
		map.put("jumlahReal", new BigDecimal(request.getParameter("jumlah")) );
		map.put("simbol", request.getParameter("simbol"));
		map.put("detailDisplay", detailDisplay);
		map.put("no_trx", no_trx.replace("\\","\\\\"));
		map.put("norek_ajs", norek_ajs);
		
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return new HashMap();
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView("uw/drekNonSpaj", errors.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request));
	}

}