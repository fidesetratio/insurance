package com.ekalife.elions.web.uw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Medical;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentFormController;

public class MedicalFormController extends ParentFormController{

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
		map.put("lsJenis",uwManager.selectAllLstMedicalCheckUp());
		
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		List lsMedical=new ArrayList();
		command.setSpaj(request.getParameter("spaj"));
		command.setInsuredNo(new Integer(1));
		
		lsMedical=elionsManager.selectMedical(command.getSpaj(),command.getInsuredNo());
		command.setLsMedical(lsMedical);
		if(lsMedical.isEmpty())
			command.setRow(new Integer(0));
		else 
			command.setRow(new Integer(lsMedical.size()));
		command.setFlagAdd(0);
		return command;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Medical medical=new Medical();
		Command command=(Command)cmd;
		String flag=request.getParameter("flag");
		String flagAdd=request.getParameter("flagAdd");
		String brs=request.getParameter("brs");
		medical.setReg_spaj(command.getSpaj());
		medical.setMste_insured_no(command.getInsuredNo());
		List lsMedical=command.getLsMedical();
		if(flag!=null)
			if(flag.equals("1")){//add
				lsMedical.add(medical);
				command.setLsMedical(lsMedical);
				command.setFlagAdd(1);
				err.reject("medical.add");
			}else if(flagAdd.equals("3")){//delete
				if(brs!=null && (!brs.equals(""))){
					if(f_validasi.f_validasi_numerik(brs)==false)
						err.reject("","Masukan Angka.");
				}else{
					err.reject("","Silahkan Masukan Baris yang akan dihapus");
				}
			}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Integer liNo=null;
		String flag=request.getParameter("flag");
		String flagAdd=request.getParameter("flagAdd");
		String brs=request.getParameter("brs");
		Medical medicalIns=new Medical();
		Command command=(Command)cmd;
		List lsMedical=command.getLsMedical();
		
		medicalIns.setReg_spaj(command.getSpaj());
		medicalIns.setMste_insured_no(command.getInsuredNo());
		//
			
		if(flag.equals("2")){//simpan
			if(flagAdd!=null)
				if(flagAdd.equals("1")){//add
					String lsmc_id=request.getParameter("jenis[2]");
					String msdm_status=request.getParameter("status");
					String desc=request.getParameter("desc");
					//
					liNo=(Integer) elionsManager.selectMaxMstDetMedical(command.getSpaj(),command.getInsuredNo());
					if(liNo==null || liNo.intValue()==0)
						liNo=new Integer(1);
					liNo =new Integer(liNo.intValue()+1);
					
					medicalIns.setReg_spaj(command.getSpaj());
					medicalIns.setMste_insured_no(command.getInsuredNo());
					medicalIns.setMpa_number(liNo);
					medicalIns.setLsmc_id(Integer.valueOf(lsmc_id));
					medicalIns.setMsdm_status(Integer.valueOf(msdm_status));
					medicalIns.setMsdm_desc(desc);
//					elionsManager.insertMstDetMedical(medicalIns);
					

				}else if(flagAdd.equals("2")){//edit
					String lsmc_id[]=request.getParameterValues("jenis");
					String msdm_status[]=request.getParameterValues("status");
					String desc[]=request.getParameterValues("desc");
					//
					//
					for(int i=0;i<lsmc_id.length;i++){
						Medical medicalUp=new Medical();
						Medical medicalTemp=(Medical)lsMedical.get(i);
						if(logger.isDebugEnabled())logger.debug(medicalTemp.getLsmc_id());
						medicalUp.setReg_spaj(command.getSpaj());
						medicalUp.setMste_insured_no(command.getInsuredNo());
						medicalUp.setMpa_number(medicalTemp.getMpa_number());
						medicalUp.setLsmc_id(Integer.valueOf(lsmc_id[i]));
						medicalUp.setLsmc_id_old(medicalTemp.getLsmc_id());
						medicalUp.setMsdm_status(Integer.valueOf(msdm_status[i]));
						medicalUp.setMsdm_desc(desc[i]);
						elionsManager.updateMstDetMedical(medicalUp);
					}

				}else if(flagAdd.equals("3")){//delete
					Medical medicalDel=new Medical();
					if(brs!=null && (!brs.equals("")))
						if(Integer.parseInt(brs)!=0){
							medicalDel=(Medical)lsMedical.get(Integer.parseInt(brs)-1);
							uwManager.deleteMstDetMedical(medicalDel);
						}
						
				}
		}
		lsMedical=elionsManager.selectMedical(command.getSpaj(),command.getInsuredNo());
		command.setLsMedical(lsMedical);
		if(lsMedical.isEmpty())
			command.setRow(new Integer(0));
		else
			command.setRow(new Integer(lsMedical.size()));
		command.setFlagAdd(0);
		return new ModelAndView("uw/medical", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,cmd,err));
	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}	

}