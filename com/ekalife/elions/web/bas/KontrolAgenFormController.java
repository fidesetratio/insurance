package com.ekalife.elions.web.bas;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.FormHist;
import com.ekalife.elions.model.Region;
import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Controller untuk pengontrolan agen di cabang
 * misalnya untuk penggunaan spaj, pengembalian spaj
 * digunakan di modul sistem kontrol spaj
 * 
 * @author Yusuf Sutarko
 * @since Feb 26, 2007 (3:47:47 PM)
 */
public class KontrolAgenFormController extends ParentFormController {

	private Map daftarWarna;
	private Map daftarWarnaAgen;
	private List<Map> daftarJenisSpaj;
	
	public void setDaftarJenisSpaj(List<Map> daftarJenisSpaj) {
		this.daftarJenisSpaj = daftarJenisSpaj;
	}

	public void setDaftarWarnaAgen(Map daftarWarnaAgen) {
		this.daftarWarnaAgen = daftarWarnaAgen;
	}

	public void setDaftarWarna(Map daftarWarna) {
		this.daftarWarna = daftarWarna;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CommandControlSpaj cmd = new CommandControlSpaj();
		cmd.setLegend(daftarWarna, daftarWarnaAgen, daftarJenisSpaj);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		cmd.setDaftarRegion(elionsManager.selectUserAdminRegion(Integer.valueOf(currentUser.getLus_id())));
		Region region = new Region();
		region.setLca_lwk_lsrg("ALL_AGEN");
		region.setLsrg_nama("ALL");
		cmd.getDaftarRegion().add(0, region);
		Spaj spaj = new Spaj();
		spaj.setMss_jenis(0);
		spaj.setMsab_id(0);
		spaj.setLca_id(currentUser.getLca_id());
		spaj.setLus_id(Integer.valueOf(currentUser.getLus_id()));
		cmd.setDaftarStokSpaj(elionsManager.selectStokSpaj(spaj));
		spaj.setMss_jenis(1);
		spaj.setMsab_id(0);
		spaj.setLca_id("");
		cmd.setDaftarStokSpajAgen(elionsManager.selectStokSpajAgen(spaj));
		cmd.setAgen(new Agen());
		cmd.setFormHist(new FormHist());
		
		if(request.getParameter("nowarning")==null) cmd.setDaftarFormOverdue(elionsManager.selectWarning30Hari(Integer.valueOf(currentUser.getLus_id())));
		
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
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if("show".equals(cmd.getSubmitMode())) {
			String index = ServletRequestUtils.getStringParameter(request, "index", "");
			request.setAttribute("infoMessage", "Silahkan masukkan informasi pertanggungjawaban SPAJ");
			cmd.setDaftarFormSpaj(elionsManager.selectFormSpaj(cmd.getMsf_id(), currentUser.getLca_id(), currentUser.getLus_id()));
			cmd.setDaftarFormHistory(elionsManager.selectFormHistory(cmd.getMsf_id()));
			
			Spaj spaj = new Spaj();
			spaj.setMss_jenis(1);
			spaj.setMsab_id(cmd.getAgen().getMsab_id());
			spaj.setLca_id(cmd.getAgen().getLca_id());
			spaj.setLus_id(Integer.valueOf(currentUser.getLus_id()));
			cmd.setDaftarStokSpajAgen(elionsManager.selectStokSpajAgen(spaj));
			cmd.setIndex(index);
		}
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object obj) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) obj;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		cmd.setLca_id(currentUser.getLca_id());
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		String sukses = null;
		cmd.setSubmitMode("show");
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/kontrol_agen.htm")).addObject("sukses", sukses);
	}
	
}