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
import com.ekalife.elions.model.FormSpaj;
import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Controller untuk form permintaan spaj dari agen ke branch admin
 * digunakan di modul sistem kontrol spaj
 * (Seluruh Agen Bisa Meminta SPAJ di seluruh Cabang)
 * 
 * @author Ferry Harlim
 * @since Oct 22, 2007 (10:26 AM)
 */
public class PermintaanAgenNewFormController extends ParentFormController {

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
		//cmd.setDaftarRegion(elionsManager.selectUserAdminRegion(Integer.valueOf(currentUser.getLus_id())));
//		Region region = new Region();
//		region.setLca_lwk_lsrg("ALL_AGEN");
//		region.setLsrg_nama("ALL");
//		cmd.getDaftarRegion().add(0, region);
		Spaj spaj = new Spaj();
		spaj.setMss_jenis(0);
		spaj.setMsab_id(0);
		spaj.setLca_id(currentUser.getLca_id());
		spaj.setLus_id(Integer.valueOf(currentUser.getLus_id()));
		cmd.setDaftarStokSpaj(elionsManager.selectStokSpaj(spaj));
		spaj.setMss_jenis(1); // 0 : per cabang 1 : per agen
		spaj.setMsab_id(0);
		spaj.setLca_id("");
		cmd.setDaftarStokSpajAgen(elionsManager.selectStokSpajAgen(spaj));
		cmd.setAgen(new Agen());

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
			request.setAttribute("infoMessage", "Silahkan isi jumlah SPAJ yang diberikan ke agen");
			cmd.setDaftarFormSpaj(elionsManager.selectFormSpaj(null, currentUser.getLca_id(), currentUser.getLus_id()));
			Spaj spaj = new Spaj();
			spaj.setMss_jenis(1);
			spaj.setMsab_id(cmd.getAgen().getMsab_id());
			spaj.setLca_id(cmd.getAgen().getLca_id());
			spaj.setLus_id(Integer.valueOf(currentUser.getLus_id()));
			cmd.setDaftarStokSpajAgen(elionsManager.selectStokSpajAgen(spaj));
			cmd.setMsf_id_bef(elionsManager.selectMsfIdBef(cmd.getAgen().getMsab_id()));
			cmd.setMsf_id(cmd.getMsf_id_bef());
		}
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object obj, BindException err) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) obj;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		// tambahin untuk form req dari agen maks 300 karakter atau (40 buah spaj).
		List lsDaftar=cmd.getDaftarFormSpaj();
		if(lsDaftar!=null)
		for(int i=0;i<lsDaftar.size();i++){
			FormSpaj formSPaj=(FormSpaj)lsDaftar.get(i);
			if(formSPaj.getNo_blanko_req().length()>300){
				err.reject("","Maksimal Permintaan sebanyak 40 spaj untuk jenis "+formSPaj.getLsjs_desc());
			}
			 
		}
		cmd.setLca_id(currentUser.getLca_id());
		cmd.setCurrentUser(currentUser);
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String sukses = null;
		
		sukses = elionsManager.processFormSpaj(cmd, currentUser);
		cmd.setSubmitMode("show");
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/form_agen_new.htm?nowarning=true")).addObject("sukses", sukses);
	}
	
}