package com.ekalife.elions.web.uw.kyc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.kyc.CommandKyc;
import com.ekalife.elions.model.kyc.Hrc;
import com.ekalife.utils.parent.ParentFormController;

public class InputHrcFormController extends ParentFormController{
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CommandKyc command=new CommandKyc();
		command.setLsHrc(elionsManager.selectHighRiskCustm());
		command.setProses(0);
		command.setSize(command.getLsHrc().size());
		return command;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		CommandKyc command=(CommandKyc)cmd;
		List lsHrc=command.getLsHrc();
		List lsRemove=new ArrayList();
		if(command.getProses()==1){//add
			Hrc hrc=new Hrc();
			hrc.setBrs(command.getLsHrc().size()+1);
			lsHrc.add(hrc);
			err.reject("","ADD");
		}else if(command.getProses()==2){//save
			for(int i=0;i<lsHrc.size();i++){
				Hrc hrc=(Hrc)lsHrc.get(i);
				if(hrc.getLshc_desc()==null || hrc.getLshc_desc().equals(""))
					err.reject("","Silahkan Isi Daftar High Risk Customer pada no."+(i+1));
			}
		}
		command.setLsHrc(lsHrc);
		command.setSize(command.getLsHrc().size());
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		CommandKyc command=(CommandKyc)cmd;
		elionsManager.prosesInputHrc(command);
		return new ModelAndView("uw/kyc/input_hrc", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,cmd,err));
	}
}
