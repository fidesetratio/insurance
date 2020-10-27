package com.ekalife.elions.web.uw;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Icd;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class ICDFormController extends ParentFormController {

	protected Map referenceData(HttpServletRequest arg0, Object arg1, Errors arg2) throws Exception {
		Map map=new HashMap();
		map.put("lsIcdCode",ajaxManager.listIcd(""));
		
		return map;
	}	
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		List lsIcd = new ArrayList();
		
		command.setSpaj(request.getParameter("spaj"));
		command.setInsuredNo(new Integer(0));
		
		lsIcd = elionsManager.selectIcd(command.getSpaj(),command.getInsuredNo());
		command.setLsIcd(lsIcd);
		command.setFlagAdd(0);
		
		// untuk viewer Yusup.A (05/06/2009)
		String mode = ServletRequestUtils.getStringParameter(request, "mode","");
		if(!mode.equals("")) command.setMode("viewer");
		else command.setMode("");
		
		return command;		
	}

	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Icd icd = new Icd();
		Command command=(Command)cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		icd.setReg_spaj(command.getSpaj());
		icd.setMsdi_lus_id(Integer.parseInt(currentUser.getLus_id()));
		icd.setLus_full_name(currentUser.getLus_full_name());
		icd.setMsdi_input_date(new Date());
		icd.setMste_insured_no(0);
		icd.setCek(0);
		
		List lsIcd = command.getLsIcd();
		if(command.getFlagAdd()==1){//add
			lsIcd.add(icd);
			command.setLsIcd(lsIcd);
			command.setFlagAdd(1);
			err.reject("icd add");			
		}else if(command.getFlagAdd()==2){//save
			for(int i=0;i<command.getLsIcd().size();i++) {
				Icd icdTemp =(Icd)command.getLsIcd().get(i);
				if(icdTemp.getLic_id() == null)
					err.reject("","Silahkan Pilih Jenis medis di barisan ke-"+(i+1));
			}			
		}else if(command.getFlagAdd()==3){//delete
			int jum=0;
			for(int i=0;i<command.getLsIcd().size();i++){
				Icd icdTemp =(Icd)command.getLsIcd().get(i);
				if(icdTemp.getCek()!= null){
					jum++;
				}
			}
			if(jum==0)
				err.reject("","Untuk Menghapus data ICD code silahkan centang icd yang ingin di hapus");
		}else if(command.getFlagAdd()==4) {
			int index = ServletRequestUtils.getIntParameter(request, "index", -1);
			Icd icdTemp =(Icd)command.getLsIcd().get(index);
			
			icd.setLic_id(icdTemp.getLic_id());
			icd.setLic_desc(uwManager.selectIcdDesc(icdTemp.getLic_id()));
			command.getLsIcd().set(index, icd);
			err.reject(" ");
		}
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		uwManager.prosesIcd(command);
		List lsIcd = elionsManager.selectIcd(command.getSpaj(),command.getInsuredNo());
		command.setLsIcd(lsIcd);
		if(lsIcd.isEmpty())
			command.setRow(new Integer(0));
		else
			command.setRow(new Integer(lsIcd.size()));		
		command.setFlagAdd(0);
		return new ModelAndView("uw/icd_code", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,cmd,err));		
	}	
}
