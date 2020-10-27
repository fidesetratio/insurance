package com.ekalife.elions.web.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.AddReffBii;
import com.ekalife.elions.model.Command;
import com.ekalife.utils.parent.ParentFormController;

public class AddRefferalBiiController extends ParentFormController {
	NumberFormat f2=new DecimalFormat("00");
	NumberFormat f5=new DecimalFormat("00000");
	
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map map=new HashMap();
		Command command=(Command)cmd;
		List lstCabangBii;
		lstCabangBii = uwManager.selectLstCabangBii(command.getAddReffBii().getJenis());
		map.put("lstCabangBii", lstCabangBii);
		return map;
		
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		AddReffBii addReffBii = new AddReffBii();
		addReffBii.setFlag_aktif(1);
		addReffBii.setJenis(2);
		addReffBii.setLisensi(1);
		
		command.setAddReffBii(addReffBii);
			
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		AddReffBii addReffBii = command.getAddReffBii();
		Integer size = addReffBii.getKode().length();
		Integer index = addReffBii.getKode().indexOf("~");
		String kode = addReffBii.getKode().substring(0, index);
		String cab_rek = addReffBii.getKode().substring(index+1, size);
		
		addReffBii.setKode(kode);
		addReffBii.setNama_cabang(cab_rek);
		
		command.setAddReffBii(addReffBii);
		
		if(addReffBii.getNamaReff().equals("") || addReffBii.getNamaReff() == null) {
			err.reject("","Nama Refferal harus diisi");
		}
		
		if(addReffBii.getKodeAgent().equals("") || addReffBii.getKodeAgent() == null) {
			err.reject("", "Kode Agent harus diisi");
		}
		
		if(addReffBii.getKodeAgent().length()<5){
			err.reject("", "Kode Agent terdiri dari 5 digit");
		}
		
		//cek apakah kode_agent sudah pernah ada, tolak apabila ada.
		int check = uwManager.selectCountKodeAgent(addReffBii.getKodeAgent());
		if(check !=0){
			err.reject("", "Kode Agent sudah pernah ada");
		}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		
		if(request.getParameter("btnsave")!=null){
			Integer autoNoRef = uwManager.selectGenerateLrbId();
			command.getAddReffBii().setLrb_id(autoNoRef);
			uwManager.insertLstReffBii(command.getAddReffBii());
		}
	
		return new ModelAndView("common/add_refferal_bii",err.getModel()).addObject("submitSuccess","true").addAllObjects(this.referenceData(request,command,err));
	}
	
}