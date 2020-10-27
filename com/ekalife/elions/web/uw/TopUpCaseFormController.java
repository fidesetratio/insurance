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
import com.ekalife.elions.model.kyc.NewBusinessCase;
import com.ekalife.elions.model.kyc.TopUpCase;
import com.ekalife.utils.parent.ParentFormController;

public class TopUpCaseFormController extends ParentFormController {
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
	
		
		List KYCtopUp = new ArrayList(); 
		List lsKycTopUp = new ArrayList();
//		if(commandKyc.getProses()==0){
			//polis yang sudah di aksep / belum di aksep
			KYCtopUp= uwManager.selectKYCtopup_main(tglAwal,tglAkhir);
			lsKycTopUp=uwManager.selectLsKycTopUp(KYCtopUp);
			commandKyc.setLsKycTopUp(lsKycTopUp);
			if(lsKycTopUp.size()==0)
				err.reject("","Tidak Ada Daftar High Risk Customer");
			else
				err.reject("","Sukses.");

//			//if(flag==0)
//			//	err.reject("","Silahkan Pilih Baris yang ingin di simpan");
//			if(commandKyc.getLsKycTopUp().size()==0)
//				err.reject("","Tidak Ada Daftar High Risk Customer");
//			
//		}
		commandKyc.setSub(0);

	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		CommandKyc commandKyc=(CommandKyc)cmd;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Integer total=elionsManager.prosesUpdateKycResult(commandKyc.getLsKycTopUp(),currentUser);
		return new ModelAndView("uw/kyc/top_up_case",err.getModel()).addObject("suc","1").
				addObject("tot", total).addObject("tglAwal",commandKyc.getTglAwal()).addObject("tglAkhir",commandKyc.getTglAkhir());
	}
}
