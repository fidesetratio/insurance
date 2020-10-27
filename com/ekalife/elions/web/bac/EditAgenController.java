package com.ekalife.elions.web.bac;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;

import com.ekalife.elions.web.bac.support.Editagenvalidator;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author HEMILDA
 * Controller untuk update Agen
 */
public class EditAgenController extends ParentFormController{
	
	protected void validatePage(Object cmd, Errors err, int page) {
		Editagenvalidator validator = (Editagenvalidator) this.getValidator();
	}
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", ServletRequestUtils.getStringParameter(request, "editSPAJ", ""));
		Map refData = null;
		refData = new HashMap();
		
		HttpSession session = request.getSession();
		String res = session.getServletContext().getResource("/xml/").toString();
		//refData.put("select_regional",DroplistManager.getInstance().get("region.xml","ID",request));
		refData.put("select_regional", elionsManager.selectDropDown("eka.lst_region", "(lca_id||lwk_id||lsrg_id)", "lsrg_nama", "", "lsrg_nama", null));
		return refData;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		Cmdeditbac detiledit = new Cmdeditbac();
		String spaj = request.getParameter("spaj");
		if(spaj!=null)
		{
			/**
			 * @author HEMILDA
			 * edit data bukan input baru
			 */
			detiledit.setPemegang((Pemegang)this.elionsManager.selectpp(spaj));
			detiledit.setAddressbilling((AddressBilling)this.elionsManager.selectAddressBilling(spaj));
			detiledit.setDatausulan((Datausulan)this.elionsManager.selectDataUsulanutama(spaj));
			detiledit.setTertanggung((Tertanggung)this.elionsManager.selectttg(spaj));
			detiledit.setAgen(this.elionsManager.select_detilagen(spaj));
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			detiledit.setCurrentUser(currentUser);

			String kode_agen = detiledit.getAgen().getMsag_id();
			String nama_agent="";
			/**
			 * @author HEMILDA
			 * agen baru 000000
			 */
			if (kode_agen.equalsIgnoreCase("000000"))
			{
				nama_agent = (String)this.elionsManager.select_agent_temp(spaj);
			}
			detiledit.getAgen().setMcl_first(nama_agent);
		}else{
			/**
			 * @author HEMILDA
			 * input baru
			 */
			detiledit.setPemegang(new Pemegang());
			detiledit.setAddressbilling(new AddressBilling());
			detiledit.setDatausulan(new Datausulan());
			detiledit.setTertanggung(new Tertanggung());
			detiledit.setAgen(new Agen());
		}
		//detiledit.getDatausulan().setIndeks_validasi(new Integer(0));
	return detiledit;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		Cmdeditbac editBac = (Cmdeditbac) cmd;
		String spaj = request.getParameter("spaj");
		editBac.getAgen().setReg_spaj(spaj);
	}
		
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Cmdeditbac editBac = (Cmdeditbac) cmd;
		String spaj= editBac.getAgen().getReg_spaj();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
	
		/**
		 * @author HEMILDA
		 * kalau sudah diaksep, agen sudah tidak bisa diedit
		 */
		Integer status_polis = this.elionsManager.selectPositionSpaj(spaj);
		if (status_polis.intValue() != 5)
		{
			editBac=this.elionsManager.savingagen(cmd,err,"edit",currentUser);
		}else{
			err.rejectValue("pemegang.reg_spaj","","Spaj in sudah diaksep, tidak bisa diedit.");
		}
		
		/**
		 * @author HEMILDA
		 * pengecekan berhasil save data atau tidak. kalau editbac ada nilai spajnya artinya berhasil di save,sebaliknya berarti gagal
		 */
		if (!editBac.equals(""))
		{
			//Agen dataagen = (Agen)this.elionsManager.select_detilagen(spaj);
			editBac.getDatausulan().setIndeks_validasi(new Integer(1));
		}else{
			editBac.getDatausulan().setIndeks_validasi(new Integer(0));
		}
		return new ModelAndView("bac/editagenpenutup","cmd", editBac).addAllObjects(this.referenceData(request,cmd,err));
		//return new ModelAndView("bac/editagenpenutup",m);
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}
	
}
