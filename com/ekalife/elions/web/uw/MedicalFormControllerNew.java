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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Medical;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class MedicalFormControllerNew extends ParentFormController{

//	private Command command;
//	List lsMedical;
//	Integer insured=new Integer(1);
//	String spaj;

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
	}	

	protected Map referenceData(HttpServletRequest arg0, Object arg1, Errors arg2) throws Exception {
		Map map=new HashMap();
		map.put("lsJenis",uwManager.selectAllLstMedicalCheckUpNew());
		
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		List lsMedical=new ArrayList();
		
		command.setSpaj(request.getParameter("spaj"));
		command.setInsuredNo(new Integer(1));
		
		lsMedical=elionsManager.selectMedical(command.getSpaj(),command.getInsuredNo());
		command.setLsMedical(lsMedical);
		
		command.setVersion(uwManager.selectVersionViewMedis(command.getSpaj()));
		if(command.getVersion() == null) command.setVersion(1);
		command.setFlagAdd(0);
		
		// untuk viewer Yusup.A (05/06/2009)
		String mode = ServletRequestUtils.getStringParameter(request, "mode","");
		if(!mode.equals("")) command.setMode("viewer");
		else command.setMode("");
		
		return command;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Medical medical=new Medical();
		Command command=(Command)cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		medical.setReg_spaj(command.getSpaj());
		medical.setMste_insured_no(command.getInsuredNo());
		medical.setMsdm_status(1);
		medical.setCek(0);
		
		medical.setMsdm_input_date(new Date());
		medical.setMsdm_lus_id(Integer.parseInt(currentUser.getLus_id()));
		medical.setLus_full_name(currentUser.getLus_full_name());
		medical.setLsmc_id(0);
		
		List lsMedical=command.getLsMedical();
		if(command.getFlagAdd()==1){//add
			if (lsMedical.isEmpty()) command.setVersion(1);
			lsMedical.add(medical);
			command.setLsMedical(lsMedical);
			command.setFlagAdd(1);
			
			err.reject("medical.add");
		}else if(command.getFlagAdd()==2){//save
			for(int i=0;i<command.getLsMedical().size();i++) {
				Medical medis=(Medical)command.getLsMedical().get(i);
				if(medis.getLsmc_id()==null)
					err.reject("","Silahkan Pilih Jenis medis di barisan ke-"+(i+1));
			}
		}else if(command.getFlagAdd()==3){//delete
			int jum=0;
			for(int i=0;i<command.getLsMedical().size();i++){
				Medical medis=(Medical)command.getLsMedical().get(i);
				if(medis.getCek()!=null){
					jum++;
				}
			}
			if(jum==0)
				err.reject("","Untuk Menghapus data Medis silahkan centang medis yang ingin di hapus");
		}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		uwManager.prosesMedical(command);
		List lsMedical=elionsManager.selectMedical(command.getSpaj(),command.getInsuredNo());
		command.setLsMedical(lsMedical);
		if(lsMedical.isEmpty())
			command.setRow(new Integer(0));
		else
			command.setRow(new Integer(lsMedical.size()));
		command.setFlagAdd(0);
		return new ModelAndView("uw/medical_new", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,cmd,err));
	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}	

}