package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;

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
import com.ekalife.elions.model.HslReas;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class HasilReasFormController extends ParentFormController  {

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected Map referenceData(HttpServletRequest arg0, Object arg1, Errors arg2) throws Exception {
		Map map=new HashMap();
		List<DropDown> lsKeputusanReas = new ArrayList<DropDown>();
		lsKeputusanReas.add(new DropDown("1","standard"));
		lsKeputusanReas.add(new DropDown("2","substandard"));
		lsKeputusanReas.add(new DropDown("5","Borderline standard"));
		lsKeputusanReas.add(new DropDown("3","decline"));
		lsKeputusanReas.add(new DropDown("4","postpone"));
		
		map.put("lsKeputusanReas", lsKeputusanReas);
		map.put("lsHslReas",uwManager.selectLsInsurer());
		
		return map;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		List lsHslReas = new ArrayList();
		
		command.setSpaj(request.getParameter("spaj"));
		command.setInsuredNo(new Integer(request.getParameter("no")));
		
		lsHslReas = elionsManager.selectLsHslReas(command.getSpaj(),command.getInsuredNo());
		command.setLsHslReas(lsHslReas);
		command.setFlagAdd(0);
		
		// untuk viewer Yusup.A (05/06/2009)
		String mode = ServletRequestUtils.getStringParameter(request, "mode","");
		if(!mode.equals("")) command.setMode("viewer");
		else command.setMode("");
		
		return command;		
	}

	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		HslReas hslReas = new HslReas();
		Command command=(Command)cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		hslReas.setReg_spaj(command.getSpaj());
		hslReas.setMsdhr_lus_id(Integer.parseInt(currentUser.getLus_id()));
		hslReas.setLus_full_name(currentUser.getLus_full_name());
		//hslReas.setMsdhr_input_date(new Date());
		hslReas.setMste_insured_no(command.getInsuredNo());
		hslReas.setCek(0);
		
		List<HslReas> lsHslReas = command.getLsHslReas();
		if(command.getFlagAdd()==1){//add
			lsHslReas.add(hslReas);
			command.setLsHslReas(lsHslReas);
			command.setFlagAdd(1);
			err.reject("","hasil reas add");			
		}else if(command.getFlagAdd()==2){//save
			for(int i=0;i<command.getLsHslReas().size();i++) {
				HslReas hslReasTemp =(HslReas)command.getLsHslReas().get(i);
				if(hslReasTemp.getLsrei_id() == null)
					err.reject("","Silahkan Pilih perusahaan reas di barisan ke-"+(i+1));
			}			
		}else if(command.getFlagAdd()==3){//delete
			int jum=0;
			for(int i=0;i<command.getLsHslReas().size();i++){
				HslReas hslReasTemp =(HslReas)command.getLsHslReas().get(i);
				if(hslReasTemp.getCek()!= null){
					jum++;
				}
			}
			if(jum==0)
				err.reject("","Untuk Menghapus data hasil reas silahkan centang keputusan reas yang ingin di hapus");
		}
		else if(command.getFlagAdd()==4){//count expired date
			for(int a=0;a<lsHslReas.size();a++) {
				if(!lsHslReas.get(a).getMsdhr_input_date().equals("") && lsHslReas.get(a).getMsdhr_expired_day() != null) {
					lsHslReas.get(a).setMsdhr_expired_date(uwManager.selectExpiredDate(lsHslReas.get(a).getMsdhr_input_date(),lsHslReas.get(a).getMsdhr_expired_day()));
				}
			}
			err.reject("","hasil perhitungan expired date");
		}
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		uwManager.prosesHslReas(command);
		List lsHslReas = elionsManager.selectLsHslReas(command.getSpaj(),command.getInsuredNo());
		command.setLsHslReas(lsHslReas);
		if(lsHslReas.isEmpty())
			command.setRow(new Integer(0));
		else
			command.setRow(new Integer(lsHslReas.size()));		
		command.setFlagAdd(0);
		elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "Save Reas Worksheet" , command.getSpaj(), 0);
		return new ModelAndView("uw/hasil_reas", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,cmd,err));		
	}	
}
