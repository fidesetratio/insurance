package com.ekalife.elions.web.checklist;

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
		int centang = ServletRequestUtils.getIntParameter(request, "centang", 0);
		
		
		
		cmd.setLspd_id(ServletRequestUtils.getIntParameter(request, "lspd_id", -1));
		cmd.setReg_spaj(ServletRequestUtils.getStringParameter(request, "reg_spaj", ""));
		
		Integer lssa_id= elionsManager.selectStsAksepBySpaj(cmd.reg_spaj);
		cmd.setLssa_id(lssa_id);
		
		String lca_id=elionsManager.selectLcaIdBySpaj(cmd.reg_spaj);
		cmd.setLca_id(lca_id);
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		//bila underwriting, tetap bisa diedit
		if(ServletRequestUtils.getIntParameter(request, "editable", 1) == 0 && !currentUser.getLde_id().equals("11")){
			cmd.setEditable(false);
		}else{
			cmd.setEditable(true);
		}
		 
		if (cmd.lca_id.equals("09")&&(currentUser.getJn_bank()!=2 && currentUser.getJn_bank()!=3 && currentUser.getJn_bank()!=16)){
			cmd.setFlagbancass(1);
		}else{
			cmd.setFlagbancass(0);
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
		List lsError=new ArrayList();
		if (cmd.lca_id.equals("09")&&(currentUser.getJn_bank()!=2&&currentUser.getJn_bank()!=3&&currentUser.getJn_bank()!=16)){
			cmd.setFlagbancass(1);
		}else{
			cmd.setFlagbancass(0);
		}
		if(cmd.centang==0&&cmd.lca_id.equals("09")&&(currentUser.getJn_bank()!=2&&currentUser.getJn_bank()!=3&&currentUser.getJn_bank()!=16)){
			lsError.add("Harap Check Box Print Polis di centang terlebih dahulu.");
			cmd.setLsError(lsError);
			return new ModelAndView(new RedirectView(request.getContextPath()+"/checklist.htm?sukses=0&reg_spaj=" + cmd.reg_spaj + "&lspd_id=" + cmd.lspd_id+ "&centang=" + 1));
		}else{
			elionsManager.saveChecklist(cmd, currentUser);
			if(cmd.cekflag==0&&cmd.lca_id.equals("09")&&(currentUser.getJn_bank()!=2&&currentUser.getJn_bank()!=3&&currentUser.getJn_bank()!=16)){
				return new ModelAndView(new RedirectView(request.getContextPath()+"/checklist.htm?sukses=0&reg_spaj=" + cmd.reg_spaj + "&lspd_id=" + cmd.lspd_id+ "&centang=" + 1));
			}else
			return new ModelAndView(new RedirectView(request.getContextPath()+"/checklist.htm?sukses=1&reg_spaj=" + cmd.reg_spaj + "&lspd_id=" + cmd.lspd_id+ "&centang=" + 1));
		}
//		return new ModelAndView(new RedirectView(request.getContextPath()+"/checklist.htm?sukses=1&reg_spaj=" + cmd.reg_spaj + "&lspd_id=" + cmd.lspd_id+ "&centang=" + 1));
	}
	
}