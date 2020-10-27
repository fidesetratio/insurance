package com.ekalife.elions.web.blanko;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.FormHist;
import com.ekalife.elions.model.FormSpaj;
import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Modul controller untuk menandakan bahwa KERTAS POLIS sudah dikirim ke cabang, 
 * digunakan oleh general affairs
 * 
 * (package com.ekalife.elions.web.blanko)
 * @author Hemilda Sari Dewi
 * @since Feb 23, 2007 (9:23:23 AM)
 */
public class KirimBlankoFormController extends ParentFormController {

	private Map daftarWarna;

	public void setDaftarWarna(Map daftarWarna) {
		this.daftarWarna = daftarWarna;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CommandControlSpaj cmd = new CommandControlSpaj();
		cmd.setDaftarWarna(daftarWarna);
		cmd.setDaftarCabang(elionsManager.selectlstCabang());
		Map all = new HashMap();
		all.put("KEY", "ALL_BRANCH");
		all.put("VALUE", "ALL");
		cmd.getDaftarCabang().add(0,all);
		cmd.setFormHist(new FormHist());
		return cmd;
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if("show".equals(submitMode)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		if("show".equals(cmd.getSubmitMode())) {
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			cmd.setDaftarFormSpaj(elionsManager.selectFormSpaj(cmd.getMsf_id(), currentUser.getLca_id(), currentUser.getLus_id()));

			for(FormSpaj f : cmd.getDaftarFormSpaj()) {
				f.setWarna((String) daftarWarna.get(f.getStatus_form()));
				f.setMsf_amount(f.getMsf_amount_req());
			}
			
			String lca_id="";
			String kode = cmd.getMsf_id();
			Map data = (HashMap) this.elionsManager.select_mst_form(kode );
			if (data != null)
			{
				lca_id = (String)data.get("LCA_ID");
			}
			
			Spaj spaj = new Spaj();
			spaj.setMss_jenis(2);
			spaj.setMsab_id(0);
			spaj.setLca_id(lca_id);
			cmd.setDaftarStokSpaj(elionsManager.selectStokBlanko(spaj));
			
			cmd.setDaftarFormHistory(elionsManager.selectFormHistory(cmd.getMsf_id()));
			
			request.setAttribute("infoMessage", "Silahkan masukkan keterangan pengiriman diatas.");
		}
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected void onBind(HttpServletRequest httpservletrequest, Object obj) throws Exception {
		super.onBind(httpservletrequest, obj);
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String sukses = null;
		
		sukses = elionsManager.processFormBlanko(cmd, currentUser);
		cmd.setSubmitMode("new");
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/blanko/kirim_blanko.htm")).addObject("sukses", sukses);
	}
	
}