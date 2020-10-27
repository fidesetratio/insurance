package com.ekalife.elions.web.bas;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.FormHist;
import com.ekalife.elions.model.FormSpaj;
import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Controller untuk form permintaan brosur
 * http://localhost/E-Lions/bas/form_brosur.htm
 * 
 * @author Canpri
 * @since Feb 4, 2013 (9:23:47 AM)
 */
public class PermintaanBrosurFormController extends ParentFormController {

	private Map daftarWarna;
	private Map daftarWarnaAgen;
	private List<Map> daftarJenisBrosur;
	private List<Map> daftarJenisSpaj;
	
	public void setDaftarJenisSpaj(List<Map> daftarJenisSpaj) {
		this.daftarJenisSpaj = daftarJenisSpaj;
	}

	public void setDaftarJenisBrosur(List<Map> daftarJenisBrosur) {
		this.daftarJenisBrosur = daftarJenisBrosur;
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
		
		daftarJenisBrosur = uwManager.selectJenisBrosur();
		
		cmd.setLegendBrosur(daftarWarna, daftarWarnaAgen, daftarJenisBrosur);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		FormSpaj formBrosur = new FormSpaj();
		formBrosur.setMss_jenis(2);
		formBrosur.setLca_id(currentUser.getLca_id());
		formBrosur.setMsab_id(0);
		formBrosur.setLus_id(Integer.valueOf(currentUser.getLus_id()));
		cmd.setDaftarNomorFormBrosur(uwManager.selectDaftarFormBrosur(formBrosur));
		for(FormSpaj f : cmd.getDaftarNomorFormBrosur()) {
			f.setWarna((String) daftarWarna.get(f.getStatus_form()));
		}
		Spaj brosur = new Spaj();
		brosur.setMss_jenis(2);
		brosur.setMsab_id(0);
		brosur.setLca_id(currentUser.getLca_id());
		brosur.setLus_id(Integer.valueOf(currentUser.getLus_id()));
		if(cmd.getJn_brosur()==null)brosur.setJn_brosur(1);
		else brosur.setJn_brosur(cmd.getJn_brosur());
		cmd.setDaftarStokBrosur(uwManager.selectStokBrosur(brosur));
		cmd.setAdmTravIns(uwManager.cekAdmTravIns(currentUser.getLus_id()));
		
		
		if(request.getParameter("nowarning")==null){
			cmd.setDaftarFormOverdue(elionsManager.selectWarning30Hari(Integer.valueOf(currentUser.getLus_id())));
		}
		
		return cmd;
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if("show".equals(submitMode) || "new".equals(submitMode) || "ganti".equals(submitMode)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<DropDown> x = new ArrayList<DropDown>();
		x.add(new DropDown("5000","10000"));
		x.add(new DropDown("10000","20000"));
		
		if("show".equals(cmd.getSubmitMode())) {
			Integer jn = bacManager.selectJenisFormBrosur(cmd.getMsf_id());
			request.setAttribute("infoMessage", "Silahkan tekan tombol TELAH DITERIMA apabila anda sudah menerima Marketing Tools hasil permintaan ini.");
			cmd.setDaftarFormBrosur(uwManager.selectFormBrosur(cmd.getMsf_id(), currentUser.getLca_id(), currentUser.getLus_id(), jn));
			cmd.setDaftarFormHistory(elionsManager.selectFormHistory(cmd.getMsf_id()));
			for(FormSpaj f : cmd.getDaftarFormBrosur()) {
				f.setWarna((String) daftarWarna.get(f.getStatus_form()));
			}
			FormHist formHist = new FormHist();
			formHist.setMsf_id(cmd.getMsf_id());
			formHist.setMsf_urut(1);
			cmd.setFormHistUser(elionsManager.selectFormHistory(formHist));
			cmd.setButton(0);
			cmd.setDaftarCabBmi(uwManager.selectCabBmi());
			cmd.setTypeTravelIns(x);
			cmd.setJn_brosur(cmd.getDaftarFormBrosur().get(0).getJn_brosur());
			
			Spaj brosur = new Spaj();
			brosur.setMss_jenis(2);
			brosur.setMsab_id(0);
			brosur.setLca_id(currentUser.getLca_id());
			brosur.setLus_id(Integer.valueOf(currentUser.getLus_id()));
			brosur.setJn_brosur(jn);
			cmd.setDaftarStokBrosur(uwManager.selectStokBrosur(brosur));
		} else if("new".equals(cmd.getSubmitMode())) {
			request.setAttribute("infoMessage", "Silahkan isi jumlah Marketing Tools yang diminta");
			cmd.setMsf_id(null);
			cmd.setButton(1);
			cmd.setDaftarFormBrosur(uwManager.selectFormBrosur(null, currentUser.getLca_id(), currentUser.getLus_id(), cmd.getJn_brosur()));
			cmd.setDaftarCabBmi(uwManager.selectCabBmi());
			cmd.setTypeTravelIns(x);
			
			Spaj brosur = new Spaj();
			brosur.setMss_jenis(2);
			brosur.setMsab_id(0);
			brosur.setLca_id(currentUser.getLca_id());
			brosur.setLus_id(Integer.valueOf(currentUser.getLus_id()));
			if(cmd.getJn_brosur()==null)brosur.setJn_brosur(1);
			else brosur.setJn_brosur(cmd.getJn_brosur());
			cmd.setDaftarStokBrosur(uwManager.selectStokBrosur(brosur));
		} else if("ganti".equals(cmd.getSubmitMode())) {
			request.setAttribute("infoMessage", "Silahkan isi jumlah Marketing Tools yang diminta");
			cmd.setMsf_id(null);
			cmd.setButton(1);
			cmd.setDaftarFormBrosur(uwManager.selectFormBrosur(null, currentUser.getLca_id(), currentUser.getLus_id(), cmd.getJn_brosur()));
			cmd.setDaftarCabBmi(uwManager.selectCabBmi());
			cmd.setTypeTravelIns(x);
			
			Spaj brosur = new Spaj();
			brosur.setMss_jenis(2);
			brosur.setMsab_id(0);
			brosur.setLca_id(currentUser.getLca_id());
			brosur.setLus_id(Integer.valueOf(currentUser.getLus_id()));
			if(cmd.getJn_brosur()==null)brosur.setJn_brosur(1);
			else brosur.setJn_brosur(cmd.getJn_brosur());
			cmd.setDaftarStokBrosur(uwManager.selectStokBrosur(brosur));
		}
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException err) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		HttpSession session = request.getSession();
		
		/*if(cmd.getDaftarFormBrosur()!=null){
		FormSpaj bd = cmd.getDaftarFormBrosur().get(0);
			if(bd.getBusdev().equals(""))err.reject("","Harap pilih busdev. ");
		}*/
		
		if(cmd.getDaftarFormBrosur()!=null){
			for(FormSpaj f : cmd.getDaftarFormBrosur()) {
				if (f.getMsf_amount_req()>100){//msf_amount_req, daftarFormSpaj, FormSpaj
					err.reject("","Maksimal Permintaan sebanyak 100 brosur perjenis");
				}
			}
		}
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String sukses = null;
		
		sukses = uwManager.processFormBrosur(cmd, currentUser);
		cmd.setSubmitMode("show");
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/form_brosur.htm?nowarning=true")).addObject("sukses", sukses);
	}
	
}