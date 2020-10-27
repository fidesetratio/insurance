/**
 * @author  : Ferry Harlim
 * @created : Sep 24, 2007 3:44:59 PM
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
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentFormController;

public class NewBusinessCaseFormController extends ParentFormController {
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CommandKyc commandKyc=new CommandKyc();
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		String tglAwal=request.getParameter("tglAwal");
		String tglAkhir=request.getParameter("tglAkhir");
		//automatis retrieve
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
	
		
		List KYCnewBis =new ArrayList(); 
		List lsKycNewBus=new ArrayList();
		if(commandKyc.getProses()==0){
			//polis yang sudah di aksep / belum di aksep
			KYCnewBis= Common.serializableList(elionsManager.selectKYCnewBis_utama(tglAwal,tglAkhir));
			lsKycNewBus=Common.serializableList(elionsManager.selectLsKycNewBus(KYCnewBis));
			commandKyc.setLsKycNewBus(lsKycNewBus);
			if(lsKycNewBus.size()==0)
				err.reject("","Tidak Ada Daftar High Risk Customer");
			else
				err.reject("","Silahkan Update KYC Result.");
		}else if(commandKyc.getProses()==1){//simpan
			int flag=0;
			int flagYUw=1;
			int flagTUw=1;
			int flagYUkm=1;
			int flagTUkm=1;
			int flagYDirec=1;
			int flagTDirec=1;
			
//			for(int i=0;i<commandKyc.getLsKycNewBus().size();i++){
//				NewBusinessCase newBus=(NewBusinessCase)commandKyc.getLsKycNewBus().get(i);
//				if(newBus.getFlagKyc()==null)
//					newBus.setFlagKyc(0);
//				
//				if(newBus.getFlagKyc()==1){
//					flag++;
//				}
//			}
			
			for(int i=0;i<commandKyc.getLsKycNewBus().size();i++){
				NewBusinessCase newBus=(NewBusinessCase)commandKyc.getLsKycNewBus().get(i);	
				if(newBus.getFlagYUw()==null){

						}else if(newBus.getFlagYUw().equals(1)){
							flagYUw=1;
							newBus.setFlagYUw(flagYUw); 
						}
				if(newBus.getFlagTUw()==null){

						}else if(newBus.getFlagTUw().equals(1)){
							flagTUw=0;
							newBus.setFlagYUw(flagTUw);
					}
				if(newBus.getFlagYUkm()==null){

					}else if(newBus.getFlagYUkm().equals(1)){
						flagYUkm=1;
						newBus.setFlagYUkm(flagYUkm); 
					}
				if(newBus.getFlagTUkm()==null){

					}else if(newBus.getFlagTUkm().equals(1)){
						flagTUkm=0;
							newBus.setFlagYUkm(flagTUkm);
					}
				if(newBus.getFlagYDirec()==null){

					}else if(newBus.getFlagYDirec().equals(1)){
						flagYDirec=1;
						newBus.setFlagYDirec(flagYDirec); 
					}
				if(newBus.getFlagTDirec()==null){

				}else if(newBus.getFlagTDirec().equals(1)){
					flagTDirec=0;
					newBus.setFlagYDirec(flagTDirec); 
					}
				}
			
				
			
			//if(flag==0)
			//	err.reject("","Silahkan Pilih Baris yang ingin di simpan");
			if(commandKyc.getLsKycNewBus().size()==0)
				err.reject("","Tidak Ada Daftar High Risk Customer");
			
		}
		commandKyc.setSub(0);

	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		CommandKyc commandKyc=(CommandKyc)cmd;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Integer total=elionsManager.prosesUpdateKycResult(commandKyc.getLsKycNewBus(),currentUser);
		return new ModelAndView("uw/kyc/new_business_case_new",err.getModel()).addObject("suc","1").
				addObject("tot", total).addObject("tglAwal",commandKyc.getTglAwal()).addObject("tglAkhir",commandKyc.getTglAkhir());
	}
}
