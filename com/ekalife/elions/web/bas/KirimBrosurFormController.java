package com.ekalife.elions.web.bas;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.math.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
 * Modul controller untuk menandakan bahwa brosur sudah dikirim ke cabang, 
 * digunakan oleh general affairs
 * http://localhost/E-Lions/bas/kirim_brosur.htm
 * 
 * (package com.ekalife.elions.web.bas)
 * @author Canpri
 * @since Feb 06, 2013 (10:21:23 AM)
 */
public class KirimBrosurFormController extends ParentFormController {

	private Map daftarWarna;
	private Map daftarWarnaAgen;
	private List<Map> daftarJenisSpaj;
	private List<Map> daftarJenisBrosur;
	
	public void setDaftarJenisBrosur(List<Map> daftarJenisBrosur) {
		this.daftarJenisBrosur = daftarJenisBrosur;
	}
	
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
		
		daftarJenisBrosur = uwManager.selectJenisBrosur();
		
		cmd.setLegendBrosur(daftarWarna, daftarWarnaAgen, daftarJenisBrosur);
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
			Integer jn = bacManager.selectJenisFormBrosur(cmd.getMsf_id());
			cmd.setJn_brosur(jn);
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			cmd.setDaftarFormBrosur(uwManager.selectFormBrosur(cmd.getMsf_id(), currentUser.getLca_id(), currentUser.getLus_id(), jn));
			List<DropDown> x = new ArrayList<DropDown>();
			x.add(new DropDown("5000","10000"));
			x.add(new DropDown("10000","20000"));
			cmd.setTypeTravelIns(x);
			if(cmd.getDaftarFormBrosur().size() != 0)	{
				for(FormSpaj f : cmd.getDaftarFormBrosur()) {
					f.setWarna((String) daftarWarna.get(f.getStatus_form()));
					f.setMsf_amount(f.getMsf_amount_req());
				}
				
				Spaj brosur = new Spaj();
				brosur.setMss_jenis(2);
				brosur.setMsab_id(0);
				brosur.setLca_id(cmd.getDaftarFormBrosur().get(0).getLca_id());
				brosur.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				brosur.setJn_brosur(jn);
				cmd.setDaftarStokBrosur(uwManager.selectStokBrosur(brosur));
				
				cmd.setDaftarFormHistory(elionsManager.selectFormHistory(cmd.getMsf_id()));
				FormHist formHist=new FormHist();
				formHist.setMsf_id(cmd.getMsf_id());
				//formHist.setPosisi(cmd.getPosisi());
				formHist.setPosisi(cmd.getDaftarFormHistory().get(0).getPosisi());
				formHist.setMsf_urut(1);
				cmd.setFormHist(elionsManager.selectFormHistory(formHist));
				
				request.setAttribute("infoMessage", "Silahkan masukkan keterangan pengiriman diatas.");				
			}

		}
		else if("search".equals(cmd.getSubmitMode())) {
			Integer jn = bacManager.selectJenisFormBrosur(cmd.getMsf_id());
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			cmd.setDaftarFormBrosur(uwManager.selectFormBrosur(cmd.getMsf_id(), currentUser.getLca_id(), currentUser.getLus_id(), jn));			
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
	
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException err) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		HttpSession session = request.getSession();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		if(cmd.getSubmitMode().equals("send")){
			if(cmd.getDaftarFormBrosur()!=null){
				Integer jn = bacManager.selectJenisFormBrosur(cmd.getMsf_id());
				List stok = bacManager.selectStokBrosurBusDev(cmd.getBusdev(), jn.toString());
				for(FormSpaj f : cmd.getDaftarFormBrosur()) {
					for(int i=0;i<stok.size();i++){
						Map m = (Map) stok.get(i);
						
						String stok_name = (String) m.get("STOCK_NAME");
						Integer sisa_stok = ((BigDecimal) m.get("STOCK")).intValue();
						String kode = (String) m.get("CODE_BROSUR");
						
						if(f.getLsjs_prefix().toUpperCase().equals(kode.toUpperCase().trim())){
							if(sisa_stok<f.getMsf_amount()){
								err.reject("","Stok "+stok_name+" tidak mencukupi.");
							}
						}
					}
				}
	
				//validate per PIC
				/*if(cmd.getDaftarFormBrosur().get(0).getBusdev().equals("1")){
					//PIC Ani chryswantini (Agency)
					if("2664, 2832".indexOf(currentUser.getLus_id()) == -1 ){
						err.reject("","Anda tidak ada hak akses kirim.");
					}
				}else if(cmd.getDaftarFormBrosur().get(0).getBusdev().equals("2")){
					//PIC Nixon Sompie (Corporate)
					if("2664, 3530".indexOf(currentUser.getLus_id()) == -1 ){
						err.reject("","Anda tidak ada hak akses kirim.");
					}
				}else if(cmd.getDaftarFormBrosur().get(0).getBusdev().equals("3")){
					//PIC Grisye S (Bancass 1 (Team Ibu Yanti S) Bancass Support)
					if("2664, 672".indexOf(currentUser.getLus_id()) == -1 ){
						err.reject("","Anda tidak ada hak akses kirim.");
					}
				}else if(cmd.getDaftarFormBrosur().get(0).getBusdev().equals("4")){
					//PIC Jelita S (Bancass 2 & Bancass 1 BusDev)
					if("2664, 39".indexOf(currentUser.getLus_id()) == -1 ){
						err.reject("","Anda tidak ada hak akses kirim.");
					}
				}else{
					err.reject("","Anda tidak ada hak akses kirim.");
				}*/
			}
		}
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String sukses = null;
		Integer stok_kurang = 0;
		
		sukses = uwManager.processFormBrosur(cmd, currentUser);
		
		cmd.setSubmitMode("new");
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/kirim_brosur.htm")).addObject("sukses", sukses);
	}
	
}