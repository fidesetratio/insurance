package com.ekalife.elions.web.bas;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
 * Modul controller untuk form approval spaj, digunakan agency support / bas
 * untuk approve permintaan spaj dari cabang
 * 
 * (package com.ekalife.elions.web.bas)
 * @author Yusuf Sutarko
 * @since Feb 23, 2007 (9:23:23 AM)
 */
public class ApprovalSpajFormController extends ParentFormController {

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
			String index = ServletRequestUtils.getStringParameter(request, "index", "");
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			cmd.setDaftarFormSpaj(elionsManager.selectFormSpaj(cmd.getMsf_id(), currentUser.getLca_id(), currentUser.getLus_id()));
			List<DropDown> x = new ArrayList<DropDown>();
			x.add(new DropDown("5000","10000"));
			x.add(new DropDown("10000","20000"));
			cmd.setTypeTravelIns(x);
			for(FormSpaj f : cmd.getDaftarFormSpaj()) {
				f.setWarna((String) daftarWarna.get(f.getStatus_form()));
			}
			
			Spaj spaj = new Spaj();
			spaj.setMss_jenis(0);
			spaj.setMsab_id(0);
			spaj.setLca_id(cmd.getDaftarFormSpaj().get(0).getLca_id());
			spaj.setLus_id(Integer.valueOf(currentUser.getLus_id()));
			cmd.setDaftarStokSpaj(elionsManager.selectStokSpaj(spaj));
			
			cmd.setDaftarFormHistory(elionsManager.selectFormHistory(cmd.getMsf_id()));

			FormHist formHist = new FormHist();
			formHist.setMsf_id(cmd.getMsf_id());
			formHist.setMsf_urut(1);
			cmd.setFormHistUser(elionsManager.selectFormHistory(formHist));
			cmd.setIndex(index);

			request.setAttribute("infoMessage", "Silahkan masukkan keterangan apabila anda menyetujui / menolak permintaan SPAJ ini");
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
		
		sukses = elionsManager.processFormSpaj(cmd, currentUser);
		cmd.setSubmitMode("new");
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/cek_spaj.htm")).addObject("sukses", sukses);
	}
	
}