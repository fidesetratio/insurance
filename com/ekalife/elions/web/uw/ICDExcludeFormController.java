package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentFormController;

public class ICDExcludeFormController extends ParentFormController {

	protected Map referenceData(HttpServletRequest arg0, Object arg1, Errors arg2) throws Exception {
		Map map=new HashMap();
		
		String spaj=ServletRequestUtils.getStringParameter(arg0, "spaj", "");
		ArrayList<DropDown> daftarPeserta = new ArrayList<DropDown>();
		daftarPeserta=Common.serializableList(bacManager.selectDropDownDaftarPeserta(spaj))	;
		daftarPeserta.add(0, new DropDown("10", "-----------PILIH PESERTA----------"));//10 buat key aja 
		map.put("lsIcdCode",Common.serializableList(ajaxManager.listIcd("")));
		map.put("daftarPeserta", daftarPeserta);
		map.put("dataICD", bacManager.selectDataIcdExclude(spaj));
		return map;
	}	
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		ArrayList  lsIcd = new ArrayList();		
		command.setSpaj(request.getParameter("spaj"));
		command.setInsuredNo(new Integer(0));		
	    //lsIcd = Common.serializableList(elionsManager.selectIcd(command.getSpaj(),command.getInsuredNo()));
		command.setLsIcd(lsIcd);		
		command.setFlagAdd(0);	
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
		//icd.setMste_insured_no(1);
		icd.setCek(0);
		
		ArrayList lsIcd = Common.serializableList(command.getLsIcd());
		if(command.getFlagAdd()==1){//add
			lsIcd.add(icd);
			command.setLsIcd(lsIcd);
			command.setFlagAdd(1);
			err.reject("","Silahkan Tambahkan ICD");			
		}else if(command.getFlagAdd()==2){//save			
			String desc=ServletRequestUtils.getStringParameter(request, "description", "");
			String dp=ServletRequestUtils.getStringParameter(request, "dp", "");
			String payorId=ServletRequestUtils.getStringParameter(request, "payorID", "");
			String cardNo=ServletRequestUtils.getStringParameter(request, "card", "");
			if(dp.equals("10")){
				err.reject("","pilih peserta terlebih dahulu");
			}
			if(desc.equals("")){
				err.reject("","isi keterangan terlebih dahulu");
			}
			if(payorId.equals("")){
				err.reject("","isi keterangan Payor Number ID terlebih dahulu");
			}
			if(cardNo.equals("")){
				err.reject("","isi keterangan Card Number terlebih dahulu");
			}
			
			if(command.getLsIcd().size()>0){
				for(int i=0;i<command.getLsIcd().size();i++) {
					Icd icdTemp =(Icd)command.getLsIcd().get(i);
					if(icdTemp.getLic_id() == null)
						err.reject("","Silahkan Pilih Jenis medis di barisan ke-"+(i+1));
				}
			}else{
				err.reject("","Silahkan Input Penyakit Pengecualian Terlebih dahulu");
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
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String succ="";
		ArrayList<Icd> lsIcd = new ArrayList<Icd>();
		String desc=ServletRequestUtils.getStringParameter(request, "description", "");
		String dp=ServletRequestUtils.getStringParameter(request, "dp", "");
		String payorId=ServletRequestUtils.getStringParameter(request, "payorID", "");
		String cardNo=ServletRequestUtils.getStringParameter(request, "card", "");
		if(command.getFlagAdd()==2){
			bacManager.prosesICDExclude(desc,dp,command,currentUser,payorId,cardNo);
			succ="1";
		}		
		command.setLsIcd(lsIcd);
		if(lsIcd.isEmpty())
			command.setRow(new Integer(0));
		else
			command.setRow(new Integer(lsIcd.size()));		
		command.setFlagAdd(0);
		return new ModelAndView("uw/exclude_admedika", err.getModel()).addObject("submitSuccess", succ).addAllObjects(this.referenceData(request,cmd,err));		
	}	
}
