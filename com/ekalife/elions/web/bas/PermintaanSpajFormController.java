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
 * Controller untuk form permintaan spaj dari cabang ke pusat
 * digunakan di modul sistem kontrol spaj
 * 
 * @author Yusuf Sutarko
 * @since Feb 23, 2007 (9:23:47 AM)
 */
public class PermintaanSpajFormController extends ParentFormController {

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
		FormSpaj formSpaj = new FormSpaj();
		formSpaj.setMss_jenis(0);
		formSpaj.setLca_id(currentUser.getLca_id());
		formSpaj.setMsab_id(0);
		formSpaj.setLus_id(Integer.valueOf(currentUser.getLus_id()));
		cmd.setDaftarNomorFormSpaj(elionsManager.selectDaftarFormSpaj(formSpaj));
		for(FormSpaj f : cmd.getDaftarNomorFormSpaj()) {
			f.setWarna((String) daftarWarna.get(f.getStatus_form()));
		}
		Spaj spaj = new Spaj();
		spaj.setMss_jenis(0);
		spaj.setMsab_id(0);
		spaj.setLca_id(currentUser.getLca_id());
		spaj.setLus_id(Integer.valueOf(currentUser.getLus_id()));
		cmd.setDaftarStokSpaj(elionsManager.selectStokSpaj(spaj));
		cmd.setAdmTravIns(uwManager.cekAdmTravIns(currentUser.getLus_id()));
		
		
		if(request.getParameter("nowarning")==null){
			cmd.setDaftarFormOverdue(elionsManager.selectWarning30Hari(Integer.valueOf(currentUser.getLus_id())));
		}
		
		return cmd;
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if("show".equals(submitMode) || "new".equals(submitMode)) {
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
			request.setAttribute("infoMessage", "Silahkan tekan tombol TELAH DITERIMA apabila anda sudah menerima SPAJ hasil permintaan ini.");
			cmd.setDaftarFormSpaj(elionsManager.selectFormSpaj(cmd.getMsf_id(), currentUser.getLca_id(), currentUser.getLus_id()));
			cmd.setDaftarFormHistory(elionsManager.selectFormHistory(cmd.getMsf_id()));
			for(FormSpaj f : cmd.getDaftarFormSpaj()) {
				f.setWarna((String) daftarWarna.get(f.getStatus_form()));
			}
			FormHist formHist = new FormHist();
			formHist.setMsf_id(cmd.getMsf_id());
			formHist.setMsf_urut(1);
			cmd.setFormHistUser(elionsManager.selectFormHistory(formHist));
			cmd.setButton(0);
			cmd.setDaftarCabBmi(uwManager.selectCabBmi());
			cmd.setTypeTravelIns(x);
		} else if("new".equals(cmd.getSubmitMode())) {
			request.setAttribute("infoMessage", "Silahkan isi jumlah SPAJ yang diminta");
			cmd.setMsf_id(null);
			cmd.setButton(1);
			cmd.setDaftarFormSpaj(elionsManager.selectFormSpaj(null, currentUser.getLca_id(), currentUser.getLus_id()));
			cmd.setDaftarCabBmi(uwManager.selectCabBmi());
			cmd.setTypeTravelIns(x);
		}
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	// Ryan - Pembatasan Permintaan SPAJ , Max 50 per Jenisnya -> req Martino (BAS)
	// tambahan req desy(BAS)-> validasi untuk EV dan MV, tidak boleh >100
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		HashMap mapEmail = bacManager.selectMstConfig(6, "user_simascard", "user_simascard");
		HttpSession session = request.getSession();
		if(cmd.getDaftarFormSpaj()!=null)
			for(FormSpaj f : cmd.getDaftarFormSpaj()) {
				if (f.getMsf_amount_req()!=null){
					List brosur = bacManager.selectStokSPAJ("0",f.getLsjs_id());
					Integer amount=0;
					for(int z=0; z<brosur.size();z++){
						Map dataSize = (Map) brosur.get(z);
						 amount=((BigDecimal) dataSize.get("LSJS_STOCK")).intValue();
					}
					if(f.getMsf_amount_req()!=0){
						if (amount < 50 && amount !=0){
							err.reject("", "SPAJ "+f.getLsjs_desc()+" tidak dapat di order. Silahkan hubungi DEWI ext 8392  atau TITIS ext 8527 email : Dewi_Andriyati@sinarmasmsiglife.co.id ; tities@sinarmasmsiglife.co.id");
						}
					}

					if (f.getLsjs_id()==26 ||f.getLsjs_id()==27){
						if (f.getMsf_amount_req()>100){//msf_amount_req, daftarFormSpaj, FormSpaj
							//err.reject("","Maksimal Permintaan untuk kode EV dan MV sebanyak 100 spaj. ");
						}
					}else{
						if (f.getMsf_amount_req()>50){//msf_amount_req, daftarFormSpaj, FormSpaj							
							if(f.getLsjs_id()==94 || f.getLsjs_id()==106 || f.getLsjs_id()==107 || f.getLsjs_id()==108 || f.getLsjs_id()==110 || f.getLsjs_id()==111 || f.getLsjs_id()==112 ){ //validasi jenis spaj umum (new) > 50 karena kumulatif dari unit link (new), non unit link (new), simpol (new), simas prima (new), produk save (new), vip family plan (new), smile link 1 (new)
								//								none
							}
							else{
								err.reject("","Maksimal Permintaan sebanyak 50 spaj untuk setiap jenis. ");
							}
						}

						if((f.getLsjs_id()==21 || f.getLsjs_id()==104 ||f.getLsjs_id()==105 ) && f.getMsf_amount_req()>0){
							if(mapEmail.get("NAME").toString().indexOf(currentUser.getLus_id())<0)err.reject("","MAAF, PERMINTAAN TIDAK DAPAT DIPROSES KARENA ANDA TIDAK MEMILIKI AKSES CETAK POLIS. ");
						}
					}

				}
			}
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String sukses = null;
		
		sukses = elionsManager.processFormSpaj(cmd, currentUser);
		cmd.setSubmitMode("show");
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/form_spaj.htm?nowarning=true")).addObject("sukses", sukses);
	}
	
}