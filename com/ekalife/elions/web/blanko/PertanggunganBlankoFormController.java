package com.ekalife.elions.web.blanko;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Controller untuk form permintaan KERTAS POLIS dari cabang ke pusat
 * digunakan di modul sistem kontrol KERTAS POLIS
 * 
 * @author Hemilda
 * @since Feb 23, 2007 (9:23:47 AM)
 */
public class PertanggunganBlankoFormController extends ParentFormController {

	private Map daftarWarna;

	public void setDaftarWarna(Map daftarWarna) {
		this.daftarWarna = daftarWarna;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CommandControlSpaj cmd = new CommandControlSpaj();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Spaj spaj = new Spaj();
		spaj.setMss_jenis(2);
		spaj.setMsab_id(0);
		spaj.setLca_id(currentUser.getLca_id());
		cmd.setDaftarStokSpaj(elionsManager.selectStokBlanko(spaj));
		cmd.setCurrentUser(currentUser);
		return cmd;
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if("show".equals(submitMode) ) {
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String sukses = null;
		
		sukses = elionsManager.processFormBlanko(cmd, currentUser);
		cmd.setSubmitMode("show");
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/blanko/pertanggungan_blanko.htm")).addObject("sukses", sukses);
	}
	
}