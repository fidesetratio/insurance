/**
 * @author  : Iga Ukiarwan
 * @created : Nov 10, 2019 8:44:59 AM
 */
package com.ekalife.elions.web.uw;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.pep.CommandPep;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.kyc.NewBusinessCase;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentFormController;

public class PoliticallyExposedPersonController extends ParentFormController {
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CommandPep commandPep=new CommandPep();
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		String tglAwal=request.getParameter("tglAwal");
		String tglAkhir=request.getParameter("tglAkhir");
		//automatis retrieve
		commandPep.setSub(ServletRequestUtils.getIntParameter(request, "sub"));
		Date sysdate=elionsManager.selectSysdate();
		if(tglAwal==null)
			tglAwal=sdf.format(sysdate);
		if(tglAkhir==null)
			tglAkhir=sdf.format(sysdate);
		commandPep.setTglAwal(tglAwal);
		commandPep.setTglAkhir(tglAkhir);
		commandPep.setProses(0);
		return commandPep;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		CommandPep commandPep=(CommandPep)cmd;
		String tglAwal=request.getParameter("tglAwal");
		String tglAkhir=request.getParameter("tglAkhir");
		commandPep.setTglAwal(tglAwal);
		commandPep.setTglAkhir(tglAkhir);
	
		
		List reportPep =new ArrayList(); 
		List lsReportPep=new ArrayList();
		if(commandPep.getProses()==0){
			//polis yang sudah di aksep / belum di aksep
			reportPep= Common.serializableList(bacManager.selectReportPep(tglAwal,tglAkhir));
//			lsReportPep=Common.serializableList(bacManager.selectLsPep(reportPep));
//			commandPep.setLsKycNewBus(lsReportPep);
			if(reportPep.size()==0)
				err.reject("","Tidak Ada Daftar High Risk Customer");
			else
				err.reject("","Silahkan Update KYC Result.");
		}else if(commandPep.getProses()==1){//simpan
			
			if(commandPep.getLsReportPep().size()==0)
				err.reject("","Tidak Ada Daftar High Risk Customer");
			
		}
		commandPep.setSub(0);

	}
	
//	@Override
//	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
//		CommandPep commandPep=(CommandPep)cmd;
//		User currentUser=(User)request.getSession().getAttribute("currentUser");
//		Integer total=elionsManager.prosesUpdateKycResult(commandPep.getLsReportPep(),currentUser);
//		return new ModelAndView("uw/kyc/new_business_case_new",err.getModel()).addObject("suc","1").
//				addObject("tot", total).addObject("tglAwal",commandPep.getTglAwal()).addObject("tglAkhir",commandPep.getTglAkhir());
//	}
}
