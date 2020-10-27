/**
 * @author  : Andhika
 * @created : Jul 02, 2012 3:44:59 PM
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

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.kyc.CommandKyc;
import com.ekalife.elions.model.kyc.PencairanCase;
import com.ekalife.utils.parent.ParentFormController;

public class PencairanCaseController extends ParentFormController {
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CommandKyc commandKyc=new CommandKyc();
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		String tglAwal=request.getParameter("tglAwal");
		String tglAkhir=request.getParameter("tglAkhir");
		// *retrieve
		commandKyc.setSub(ServletRequestUtils.getIntParameter(request, "sub"));
		Date sysdate=elionsManager.selectSysdate();
		if(tglAwal==null)
			tglAwal=sdf.format(sysdate);
		if(tglAkhir==null)
			tglAkhir=sdf.format(sysdate);
		commandKyc.setTglAwal(tglAwal);
		commandKyc.setTglAkhir(tglAkhir);
		commandKyc.setProses(0);
		return commandKyc;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		CommandKyc commandKyc=(CommandKyc)cmd;
		String tglAwal=request.getParameter("tglAwal");
		String tglAkhir=request.getParameter("tglAkhir");
		commandKyc.setTglAwal(tglAwal);
		commandKyc.setTglAkhir(tglAkhir);
	
		
		List KYCpencairan = new ArrayList(); 
		List lsKycPencairan = new ArrayList();
		
			KYCpencairan= uwManager.selectKYCpencairan_main(tglAwal,tglAkhir);
			lsKycPencairan=uwManager.selectLsKycPencairan(KYCpencairan);
			commandKyc.setLsKycPencairan(lsKycPencairan);
			if(lsKycPencairan.size()==0)
				err.reject("","Tidak Ada Daftar High Risk Customer");
			else
				err.reject("","Sukses.");

		commandKyc.setSub(0);

	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		CommandKyc commandKyc=(CommandKyc)cmd;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Integer total=elionsManager.prosesUpdateKycResult(commandKyc.getLsKycPencairan(),currentUser);
		return new ModelAndView("uw/kyc/pencairan_case",err.getModel()).addObject("suc","1").
				addObject("tot", total).addObject("tglAwal",commandKyc.getTglAwal()).addObject("tglAkhir",commandKyc.getTglAkhir());
	}
}
