package com.ekalife.elions.web.uw;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Ulangan;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class UlanganFormController extends ParentFormController {
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		String spaj = request.getParameter("spaj");
		command.setSpaj(spaj);
		List lsUlangan=elionsManager.selectLstUlangan(spaj);
		command.setLsUlangan(lsUlangan);
		
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		if(currentUser.getLde_id().equals("11")) request.setAttribute("bolehEdit", true);
		
		return command;
	}
	
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		if(command.getFlag_ut().intValue()==1){//add
			Ulangan ulangan=new Ulangan();
			ulangan.setReg_spaj(command.getSpaj());
			ulangan.setLus_id(Integer.valueOf(currentUser.getLus_id()));
			ulangan.setTanggal(new Date());
			Map map=elionsManager.selectPemegang(command.getSpaj());
			Integer lsspId=(Integer)map.get("LSSP_ID");
			String lssp_status=elionsManager.selectLstPolicyStatus(lsspId);
			ulangan.setStatus_polis(lsspId);
			ulangan.setLssp_status(lssp_status);
			ulangan.setFlagAdd(1);
			ulangan.setLus_full_name(currentUser.getLus_full_name());
			List lsUlangan=command.getLsUlangan();
			lsUlangan.add(ulangan);
			command.setLsUlangan(lsUlangan);
			err.reject("","telah dilakukan penambahan");
			command.setFlagAdd(1);
			request.setAttribute("bolehEdit", true);
		}else if(command.getFlag_ut().intValue()==2){//save
			List lsUlangan=command.getLsUlangan();
			Ulangan ulangan =(Ulangan)lsUlangan.get(lsUlangan.size()-1);
			if(ulangan.getJenis()==null || ulangan.getJenis().trim().equals(""))
				err.reject("","Silahkan Isi Jenis Ulangan ");
			else if(ulangan.getKeterangan()==null || ulangan.getKeterangan().trim().equals(""))
				err.reject("","Silahkan Isi Keterangan");
			request.setAttribute("bolehEdit", true);
		}
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		List lsUlangan=command.getLsUlangan();
		Ulangan ulangan=(Ulangan)lsUlangan.get(lsUlangan.size()-1);

		elionsManager.insertLstUlangan(ulangan);
		return new ModelAndView("uw/viewer/ulangan", err.getModel()).addObject("submitSuccess", "true");
		
	}
	
}
