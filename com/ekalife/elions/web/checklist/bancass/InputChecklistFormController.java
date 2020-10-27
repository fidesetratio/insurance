package com.ekalife.elions.web.checklist.bancass;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Checklist;
import com.ekalife.elions.model.CommandChecklist;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Form Controller untuk input checklist new business
 * @author Yusuf
 * @since Sep 25, 2008 (8:57:48 AM)
 */
public class InputChecklistFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, 	doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, 	integerEditor); 
		binder.registerCustomEditor(Date.class, null, 		dateEditor);
	}

	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map refData = new HashMap();
		return refData;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Date sysdate = elionsManager.selectSysdate();
		
		CommandChecklist cmd = new CommandChecklist();
		cmd.setLspd_id(ServletRequestUtils.getIntParameter(request, "lspd_id", -1));
		cmd.setReg_spaj(ServletRequestUtils.getStringParameter(request, "reg_spaj", ""));
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		//bila underwriting, tetap bisa diedit
		if(ServletRequestUtils.getIntParameter(request, "editable", 1) == 0 && !currentUser.getLde_id().equals("11")){
			cmd.setEditable(false);
		}else{
			cmd.setEditable(true);
		}
		
//		if(!cmd.getReg_spaj().equals("") && cmd.getLspd_id() == -1) {
//			cmd.setLspd_id(elionsManager.selectPosisiDokumenBySpaj(cmd.getReg_spaj()));
//		}
		
		if(!cmd.getReg_spaj().equals("")) {
			cmd.setListChecklist(elionsManager.selectCheckListBySpaj(cmd.reg_spaj));

			//buat tampilan, di indent kedalam
			for(Checklist c : cmd.getListChecklist()) {
				if(c.level > 1) {
					String indent = "";
					for(int i=2; i<c.level; i++) {
						indent = "&nbsp;&nbsp;&nbsp;" + indent;
					}
					if(c.level == 2) c.lc_nama = indent + c.lc_nama;
					else if(c.level == 3) c.lc_nama = indent + " > " + c.lc_nama;
					else if(c.level >= 4) c.lc_nama = indent + " - " + c.lc_nama;
				}
			}
		}

		return cmd;
	}
	
	//validasi
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		CommandChecklist cmd = (CommandChecklist) command;		
		//errors.reject("", "Guru Fisika");
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandChecklist cmd = (CommandChecklist) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		elionsManager.saveChecklistBancass(cmd, currentUser);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bancass/checklist.htm?sukses=1&reg_spaj=" + cmd.reg_spaj + "&lspd_id=" + cmd.lspd_id));
	}
	
}