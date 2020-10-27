/**
 * 
 */
package com.ekalife.elions.web.bac;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.CommandPowersaveCair;
import com.ekalife.elions.model.PowersaveCair;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Controller untuk Daftar Pencairan di tempat untuk Bank Sinarmas dan Sinarmas Sekuritas
 * Khusus produk2 powersave dan stable link
 * 
 * @author Yusuf
 * @since Dec 22, 2008 (3:34:07 PM)
 */
public class DaftarPowersaveCairFormController extends ParentFormController {
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		List<DropDown> daftarPosisi = new ArrayList<DropDown>();
		daftarPosisi.add(new DropDown("1", "1 - SUDAH DIINPUT"));
		if(currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 16){ 
			daftarPosisi.add(new DropDown("2", "2 - APPROVAL KP BSM"));
		}
		daftarPosisi.add(new DropDown("3", "3 - DALAM PROSES AJS"));
		daftarPosisi.add(new DropDown("5", "5 - SELESAI PROSES AJS (FILLING)"));
		
		m.put("daftarPosisi", daftarPosisi);
		return m;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		
		CommandPowersaveCair cpc = new CommandPowersaveCair();
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		int posisi = ServletRequestUtils.getIntParameter(request, "posisi", 1); 
		
		cpc.setDaftarPremi(elionsManager.selectDaftarCair(posisi, currentUser.getJn_bank(), currentUser.getCab_bank().trim()));

		request.setAttribute("posisi", posisi);
		
		if(currentUser.getJn_bank().intValue()==2 || currentUser.getJn_bank().intValue()==16){
			if(currentUser.getCab_bank().trim().equals("SSS")) {
				request.setAttribute("boleh", "true");
			}
		}else if(currentUser.getJn_bank().intValue()==3){
			if(currentUser.getFlag_approve().intValue()!=0){
				request.setAttribute("boleh", "true");
			}
		}
			
		
		return cpc;
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		CommandPowersaveCair cpc 	= (CommandPowersaveCair) command;
		User currentUser 			= (User) request.getSession().getAttribute("currentUser");
		Date sysdate 				= elionsManager.selectSysdate(0);
		int total 					= 0;
		Date duatiga 				= new SimpleDateFormat("dd/MM/yyyy").parse("23/06/2017"); 
		
		
		for(int i=0; i<cpc.daftarPremi.size(); i++) {
			PowersaveCair p = (PowersaveCair) cpc.daftarPremi.get(i);
			
			if(p.centang) {
				total++;
				
				if(p.flag_posisi == 1 && p.mpc_cair.compareTo(sysdate) <= 0){
					errors.reject("", "Anda hanya dapat melakukan transfer untuk Polis yang tanggal pencairannya lebih besar dari tanggal hari ini.");
				}
				
				//untuk user biasa, hanya boleh transfer yg di posisi 1
				if(p.flag_posisi >= 2 && !currentUser.getCab_bank().trim().equals("SSS")) {
					errors.reject("", "Anda hanya dapat melakukan transfer untuk Polis yang berada pada posisi INPUT (CABANG)");
				}
				
				//untuk user ardy, boleh transfer yg di posisi 1 dan 2
				if(p.flag_posisi >= 3 && currentUser.getCab_bank().trim().equals("SSS")) {
					errors.reject("", "Anda hanya dapat melakukan transfer untuk Polis yang berada pada posisi INPUT (CABANG) atau APPROVAL KANTOR PUSAT");
				}
				
				//untuk user ardy, bisa merubah tgl cair
				if(!errors.hasErrors()) {
					if(p.mpc_cair_baru != null) {
						ValidationUtils.rejectIfEmptyOrWhitespace(errors, "daftarPremi["+i+"].mpc_desc", "", "Harap isi keterangan bila merubah TANGGAL CAIR");
						if(!errors.hasErrors()) {
							if(p.mpc_cair_baru.compareTo(p.mpc_cair) <= 0) {
								errors.rejectValue("daftarPremi["+i+"].mpc_desc", "", "TANGGAL CAIR BARU tidak boleh dibawah atau sama dengan TANGGAL CAIR SEBELUMNYA");
							}else if(p.mpc_desc.length() > 100) {
								errors.rejectValue("daftarPremi["+i+"].mpc_desc", "", "Harap isi keterangan maksimal 100 karakter");
							}
						}
					}
				}
				
				//dikarenakan tgl 23 June 2017 BSM Tutup dan AJSM Buka sehingga munculkan validasi untuk proses pencairan pada tanggal ini.
				if (duatiga.compareTo(p.mpc_cair) == 0){
					errors.rejectValue("daftarPremi["+i+"].reg_spaj", "", " Untuk pencairan SPAJ "+ p.reg_spaj +" / "+ p.mspo_policy_no_format +" dengan tanggal cair 23/06/2017 tidak bisa di transfer, mohon di informasikan ke bagian Life Benefit (Helena@sinarmasmsiglife.co.id) ");
					}
				
			}
		}
		if(total == 0) errors.reject("", "Harap pilih minimal satu untuk melanjutkan");
		
		int posisi = ServletRequestUtils.getIntParameter(request, "posisi", 1); 
		request.setAttribute("posisi", posisi);
		if(currentUser.getCab_bank().trim().equals("SSS")) {
			request.setAttribute("boleh", "true");
		}
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	
		CommandPowersaveCair cpc = (CommandPowersaveCair) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String pesan = "";
		
		if(request.getParameter("transfer") != null) {
			uwManager.transferMstPowersaveCair(cpc, currentUser);
			pesan = "Data berhasil ditransfer. Anda dapat melihat posisi dokumen dengan melakukan pencarian nomor Register SPAJ / Polis pada menu INPUT PENCAIRAN.";
		}
		
		return new ModelAndView(new RedirectView("daftarcair.htm")).addObject("pesan", pesan);
	}
	
}