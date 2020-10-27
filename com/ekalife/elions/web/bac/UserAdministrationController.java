package com.ekalife.elions.web.bac;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author Ridhaal
 * Date : 3/21/2016
*/

public class UserAdministrationController extends ParentFormController {

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		User cmd = new User();
		cmd.setAksesCabang(bacManager.selectCabangBiiUA(null,null));
		//set status untuk set tombol user aktif(1)/inaktif(0)
		cmd.setStat_Act(1);
	
		return cmd;
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if("show".equals(submitMode) || "new".equals(submitMode) || "search".equals(submitMode) || "activeU".equals(submitMode) || "reset".equals(submitMode)|| "inactiveU".equals(submitMode) || "edit".equals(submitMode) || "cancel".equals(submitMode) || "btncekspv".equals(submitMode) || "save".equals(submitMode)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
		User cmd = (User) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<DropDown> x = new ArrayList<DropDown>();
		x.add(new DropDown("5000","10000"));
		x.add(new DropDown("10000","20000"));
		
		if(("show".equals(cmd.getSubmitMode())) || ("cancel".equals(cmd.getSubmitMode())) || ("reset".equals(cmd.getSubmitMode()))) {
			
			cmd.setLus_id(ServletRequestUtils.getStringParameter(request, "lus_id", ""));
			
			if ("reset".equals(cmd.getSubmitMode())){
				cmd.setStat_sukses( bacManager.processUserAdministration(cmd, currentUser));
			}
			
			
			cmd.setCab_bank(ServletRequestUtils.getStringParameter(request, "cabangList", ""));
			cmd.setStat_Act(ServletRequestUtils.getIntParameter(request, "statusActiv"));
			cmd.setDaftarUser(bacManager.selectDaftarUserUA(cmd.getCab_bank(), cmd.getStat_Act(), null));
			cmd.setDetailUser(bacManager.selectDetUserUA(3,cmd.getLus_id(), null,null));
			cmd.setLcb_no(cmd.getCab_bank());
			cmd.setLcb_no_pil(cmd.getCab_bank());
			cmd.setDaftarSpv1(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 2, null));
			cmd.setSpv1_name(bacManager.cekSpv(cmd.getLus_id(),1));
			cmd.setDaftarSpv2(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 2, null));
			cmd.setSpv2_name(bacManager.cekSpv(cmd.getLus_id(),2));
			cmd.setButton(0);
			

		} else if("new".equals(cmd.getSubmitMode())) {

			cmd.setCab_bank(ServletRequestUtils.getStringParameter(request, "cabangList", ""));
			cmd.setDetailUser(bacManager.selectDetUserUA(0,null,cmd.getCab_bank(),null));
			cmd.setLus_id(bacManager.selectNewLusIDUA()); //set new lus_id
			cmd.setButton(1);
			cmd.setLcb_no(cmd.getCab_bank());
			cmd.setLcb_no_pil(cmd.getCab_bank());
			cmd.setDaftarUser(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 1, null));
			cmd.setDaftarSpv1(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 2, null));
			cmd.setDaftarSpv2(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 2, null));
			cmd.setLevel("inputerLvl");
			cmd.setStat_Act(1);
			
		}
		else if("search".equals(cmd.getSubmitMode())) {
			
			cmd.setCab_bank(ServletRequestUtils.getStringParameter(request, "cabangList", ""));
			cmd.setDaftarUser(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 1, null));
			cmd.setLcb_no(cmd.getCab_bank());
			cmd.setLcb_no_pil(ServletRequestUtils.getStringParameter(request, cmd.getCab_bank(), ""));
			cmd.setButton(2);
			cmd.setStat_Act(1);
						
		}else if("activeU".equals(cmd.getSubmitMode())) {
			
			cmd.setCab_bank(ServletRequestUtils.getStringParameter(request, "cabangList", ""));
			cmd.setDaftarUser(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 1, null));
			cmd.setLcb_no(cmd.getCab_bank());
			cmd.setButton(2);
			cmd.setStat_Act(1);
				
		}else if("inactiveU".equals(cmd.getSubmitMode())) {
			
			cmd.setCab_bank(ServletRequestUtils.getStringParameter(request, "cabangList", ""));
			cmd.setDaftarUser(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 0, null));
			cmd.setLcb_no(cmd.getCab_bank());
			cmd.setButton(2);
			cmd.setStat_Act(0);
	
				
		}else if("edit".equals(cmd.getSubmitMode())) {
			cmd.setLus_id(ServletRequestUtils.getStringParameter(request, "lus_id", ""));
			cmd.setCab_bank(ServletRequestUtils.getStringParameter(request, "cabangList", ""));
			cmd.setStat_Act(ServletRequestUtils.getIntParameter(request, "statusActiv"));
			cmd.setDaftarUser(bacManager.selectDaftarUserUA(cmd.getCab_bank(), cmd.getStat_Act(), null));
			cmd.setDetailUser(bacManager.selectDetUserUA(3,cmd.getLus_id(), null,null));
			cmd.setLcb_no(cmd.getCab_bank());
			cmd.setLcb_no_pil(cmd.getCab_bank());
			cmd.setDaftarSpv1(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 2, null));
			cmd.setSpv1_name(bacManager.cekSpv(cmd.getLus_id(),1));
			cmd.setDaftarSpv2(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 2, null));
			cmd.setSpv2_name(bacManager.cekSpv(cmd.getLus_id(),2));
			cmd.setButton(3);
		
		}else if("btncekspv".equals(cmd.getSubmitMode())) {
			
			cmd.setLus_id(ServletRequestUtils.getStringParameter(request, "hiddenLus_id", ""));
			cmd.setCab_bank(ServletRequestUtils.getStringParameter(request, "cabangList", ""));
			cmd.setStat_Act(ServletRequestUtils.getIntParameter(request, "statusActiv"));
			cmd.setDaftarUser(bacManager.selectDaftarUserUA(cmd.getCab_bank(), cmd.getStat_Act(), null));
			cmd.setDetailUser(bacManager.selectDetUserUA(3,cmd.getLus_id(), null,null));
			cmd.setLcb_no(cmd.getCab_bank());
			cmd.setLcb_no_pil(ServletRequestUtils.getStringParameter(request, "cabangPil", ""));
			cmd.setDaftarSpv1(bacManager.selectDaftarUserUA(cmd.getLcb_no_pil(), 2, null));
			cmd.setSpv1_name(bacManager.cekSpv(cmd.getLus_id(),1));
			cmd.setDaftarSpv2(bacManager.selectDaftarUserUA(cmd.getLcb_no_pil(), 2, null));
			cmd.setSpv2_name(bacManager.cekSpv(cmd.getLus_id(),2));
		
			cmd.setButton(3);
						
		}else if("save".equals(cmd.getSubmitMode())) {
			
			cmd.setLus_id(ServletRequestUtils.getStringParameter(request, "lusId", ""));
			cmd.setCab_bank(ServletRequestUtils.getStringParameter(request, "hiddenCab_id", ""));
			cmd.setLus_full_name(ServletRequestUtils.getStringParameter(request, "fullName", ""));
			cmd.setLus_login_name(ServletRequestUtils.getStringParameter(request, "loginName", ""));
			cmd.setLevel(ServletRequestUtils.getStringParameter(request, "levelList", ""));
			
			if(cmd.getLevel().equals("inputerLvl")){
			cmd.setSpv1_name(ServletRequestUtils.getStringParameter(request, "daftarSPV1", ""));
			cmd.setSpv2_name(ServletRequestUtils.getStringParameter(request, "daftarSPV2", ""));
			// default 5465 (INP) uat 4709
			cmd.setLus_id_copy("5465");
			}else{
			cmd.setSpv1_name(null);
			cmd.setSpv2_name(null);
			// default 5483 (SPV) uat 1231
			cmd.setLus_id_copy("5483");
			}
			
			cmd.setLus_email(ServletRequestUtils.getStringParameter(request, "email", ""));
			cmd.setLus_active(ServletRequestUtils.getStringParameter(request, "status", ""));
			cmd.setButton(ServletRequestUtils.getIntParameter(request, "tombol"));
			
			//save new
			
			if (cmd.getButton() == 1){
			
			cmd.setStat_sukses(null);
			
			String ceklogname = bacManager.cekLoginName(cmd.getLus_login_name());
			String cekfullname = bacManager.cekFullName(cmd.getLus_full_name());
			
			if (ceklogname == null && cekfullname == null )
			{
				cmd.setStat_sukses( bacManager.processUserAdministration(cmd, currentUser));	
				
			}else if (ceklogname != null && cekfullname == null )
			{		
				cmd.setStat_sukses("Login Name sudah digunakan. Pilih Login Name lain !");
				
			}else if (ceklogname == null && cekfullname != null )
			{		
				cmd.setStat_sukses("Full name sudah pernah terdaftar disistem (Full Name tidak boleh sama). Jika Full Name ini ingin di gunakan silahkan tambahkan/ sisipkan cabang pada Full Name!");
				
			}else {
				cmd.setStat_sukses("Login Name dan Full Name sudah digunakan. Pilih Login Name dan Full Name lain !");
			}
			
				cmd.setCab_bank(ServletRequestUtils.getStringParameter(request, "cabangList", ""));
				cmd.setDetailUser(bacManager.selectDetUserUA(0,null,cmd.getCab_bank(),null));
				cmd.setLus_id(bacManager.selectNewLusIDUA()); //set new lus_id
				cmd.setButton(1);
				cmd.setLcb_no(cmd.getCab_bank());
				cmd.setLcb_no_pil(cmd.getCab_bank());
				cmd.setDaftarUser(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 1, null));
				cmd.setDaftarSpv1(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 2, null));
				cmd.setDaftarSpv2(bacManager.selectDaftarUserUA(cmd.getCab_bank(), 2, null));
				cmd.setLevel("inputerLvl");
				cmd.setStat_Act(1);
		
			}
			//update data
			else{
				
				cmd.setLcb_no_pil(ServletRequestUtils.getStringParameter(request, "cabangPil", ""));			
								
				cmd.setStat_sukses( bacManager.processUserAdministration(cmd, currentUser));
				cmd.setLus_id(ServletRequestUtils.getStringParameter(request, "lus_id", ""));				
				cmd.setCab_bank(ServletRequestUtils.getStringParameter(request, "cabangPil", ""));	
				cmd.setStat_Act(ServletRequestUtils.getIntParameter(request, "statusActiv"));
				cmd.setDaftarUser(bacManager.selectDaftarUserUA(cmd.getLcb_no_pil(), cmd.getStat_Act(), null));
				cmd.setDetailUser(bacManager.selectDetUserUA(3,cmd.getLus_id(), null,null));
				cmd.setLcb_no(cmd.getCab_bank());
				cmd.setLcb_no_pil(cmd.getCab_bank());
				cmd.setDaftarSpv1(bacManager.selectDaftarUserUA(cmd.getLcb_no_pil(), 2, null));
				cmd.setSpv1_name(bacManager.cekSpv(cmd.getLus_id(),1));
				cmd.setDaftarSpv2(bacManager.selectDaftarUserUA(cmd.getLcb_no_pil(), 2, null));
				cmd.setSpv2_name(bacManager.cekSpv(cmd.getLus_id(),2));
				cmd.setButton(0);
			}
			cmd.setSubmitMode("show");
						
		}
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException err) throws Exception {
		
		
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
//		User cmd = (User) command;
//		User currentUser = (User) request.getSession().getAttribute("currentUser");

//		return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/user_administration.htm?nowarning=true")).addObject("sukses", sukses);
	
	return null;
	}
	
}
