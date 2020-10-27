package com.ekalife.elions.web.akseptasi_ssh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.worksheet.UwRenewal;
import com.ekalife.utils.parent.ParentFormController;

/**WorksheetUwRenewalController.java
 * @author  : Randy
 * @created : May 11, 2016
 */

public class WorksheetUwRenewalController extends ParentFormController {
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		UwRenewal cmdr = new UwRenewal();
		cmdr.setReg_spaj(ServletRequestUtils.getStringParameter(request, "reg_spaj"));
		String spaj = ServletRequestUtils.getStringParameter(request, "reg_spaj");
		cmdr.setLsRenewal(uwManager.selectUwRenewal(cmdr.getReg_spaj()));//link
		cmdr.setLsDataUsulan1(uwManager.selectRenewalDU(spaj,"1"));
		cmdr.setLsDataUsulan2(uwManager.selectRenewalDU(spaj,"2"));
		return cmdr;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		UwRenewal cmdr =(UwRenewal) cmd;
		String prod1 = (ServletRequestUtils.getStringParameter(request, "prod1",""));
		String area4 = (ServletRequestUtils.getStringParameter(request, "area4","")); 
		String area1 = (ServletRequestUtils.getStringParameter(request, "area1","")); 
		String area2 = (ServletRequestUtils.getStringParameter(request, "area2","")); 
		String area3 = (ServletRequestUtils.getStringParameter(request, "area3","")); 
		String area5 = (ServletRequestUtils.getStringParameter(request, "area5","")); 
		
		if(area1.contains("\\") || area1.contains("'")) err.reject("","Jangan menggunakan tanda ' dan \\.");
		if(area2.contains("\\") || area2.contains("'")) err.reject("","Jangan menggunakan tanda ' dan \\.");
		if(area3.contains("\\") || area3.contains("'")) err.reject("","Jangan menggunakan tanda ' dan \\.");
		if(area5.contains("\\") || area5.contains("'")) err.reject("","Jangan menggunakan tanda ' dan \\.");
		if(prod1.equals("")) err.reject("","Produk Belum Dipilih");
		if(area4.equals("")) err.reject("","Jumlah Premi Belum Diisi.");
	    
		cmdr.setProd1(prod1);
		cmdr.setArea4(area4); 
		cmdr.setArea1(area1);
		cmdr.setArea2(area2);
		cmdr.setArea3(area3);
		cmdr.setArea5(area5);
		
		String nospaj = (ServletRequestUtils.getStringParameter(request, "reg_spaj",""));	
		List<Map> data = new ArrayList<Map>();			
		data = uwManager.selectUwRenewal(nospaj);
		if (data.size() > 0){
		}else{
			err.reject("","Tidak ada data untuk polis ini.");	
		}
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		UwRenewal cmdRenewal =(UwRenewal) cmd;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String a1 = (ServletRequestUtils.getStringParameter(request, "area1",""));
		return new ModelAndView("akseptasi_ssh/worksheet_uw_renewal", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request, cmd, err));
	}
}
