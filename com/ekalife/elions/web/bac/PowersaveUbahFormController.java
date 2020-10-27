/**
 * 
 */
package com.ekalife.elions.web.bac;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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


import com.ekalife.elions.model.CommandPowersaveUbah;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.ekalife;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Controller untuk Ubah MGI, Rate, roll over Next Periode Powersave  di tempat untuk  Sinarmas Sekuritas
 * Khusus produk2 powersave 
 * 
 * @author bertho
 * @since Apr 14, 2009 (3:34:07 PM)
 */
public class PowersaveUbahFormController extends ParentFormController {
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		
		CommandPowersaveUbah cpu = new CommandPowersaveUbah();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		cpu.valid1 = elionsManager.selectUserName(currentUser.getValid_bank_1());
		cpu.valid2 = elionsManager.selectUserName(currentUser.getValid_bank_2());
		
		cpu.reg_spaj=request.getParameter("spaj");
		if(cpu.reg_spaj!=null){
			CommandPowersaveUbah cpuTemp = uwManager.selectDataNasabah(cpu.reg_spaj);
			cpu.reg_spaj=cpuTemp.reg_spaj;
			cpu.mspo_policy_no=cpuTemp.mspo_policy_no;
			cpu.mcl_first=cpuTemp.mcl_first;
			
			if(cpu!=null){	
				cpu.powersaveProses=uwManager.selectProsesPowersave(cpu.getReg_spaj());
				if(cpu.powersaveProses != null) {
					cpu.lsbs_id=uwManager.selectBusinessId(cpu.reg_spaj);
					cpu.powersaveProses.reg_spaj=cpu.reg_spaj;
					cpu.powersaveProses.mps_rate=uwManager.selectRatePowerSave(cpu.getPowersaveProses());
					
//					cek pinjaman konvensional
//					TODO:cek pinjaman ini buat apa ya??
					cpu.flagPinjaman=uwManager.selectCekPinjaman(cpu.getReg_spaj(), 0);
						
					
					cpu.selisih=FormatDate.dateDifference(FormatDate.add(cpu.powersaveProses.mps_batas_date, Calendar.DATE, 1), elionsManager.selectSysdate(), true);//						add by rudy - req annalisa helpdesk #03743
					
					cpu.powersaveUbah.setLus_id(Integer.parseInt(currentUser.getLus_id()));
					
					cpu.listPowersaveUbah=uwManager.selectUbahView(cpu.reg_spaj);
					
					
				}
			}
		}
		
		
		
		return cpu;
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		return super.referenceData(request, command, errors);
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		//VALIDASI SETELAH SUBMIT
		CommandPowersaveUbah cpu = (CommandPowersaveUbah) command;
		cpu.reg_spaj = cpu.reg_spaj.trim().replace(".", "");
		int size = cpu.reg_spaj.length();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		int jn_bank = currentUser.getJn_bank().intValue();
		String process=request.getParameter("proses1")==""?"0":"1";
		String ganti=request.getParameter("ganti")==""?"0":"1";
		
//		A. User menekan tombol show
		if(request.getParameter("show") != null) {
//			1. Validasi User
			if(!errors.hasErrors() &&  jn_bank != 3) {
				errors.reject("", "Menu ini hanya dapat digunakan oleh Sinarmas Sekuritas.");
			}
			
//			2. Validasi panjang karakter untuk SPAJ / Polis
			if(!errors.hasErrors() && size != 11 && size != 14) {
				errors.reject("","Harap masukkan nomor NOMOR POLIS atau NOMOR REGISTER SPAJ yang benar");
			}
			if(size == 14) {
				cpu.reg_spaj = elionsManager.selectSpajFromPolis(cpu.reg_spaj); //cari dulu no spaj nya				
			}
			
//			3. Hanya Polis SMS aja yang boleh
			int jn_bank_check = elionsManager.selectIsInputanBank(cpu.reg_spaj);
			if (!errors.hasErrors() && jn_bank_check != 3){ // SMS
				errors.reject("","Anda tidak mempunyai akses terhadap Polis ini.");
			}
			
			//4. Validasi apakah data ada di sistem, sekalian tarik nama nasabah dan  data proses powersave 
			if(!errors.hasErrors()) {
				
				
				CommandPowersaveUbah cpuTemp = uwManager.selectDataNasabah(cpu.reg_spaj);
				cpu.reg_spaj=cpuTemp.reg_spaj;
				cpu.mspo_policy_no=cpuTemp.mspo_policy_no;
				cpu.mcl_first=cpuTemp.mcl_first;
				
				if(cpu==null){
					errors.reject("", "Data tidak ada. Harap pastikan nomor yang dimasukkan benar.");
				}else{
				
					cpu.powersaveProses=uwManager.selectProsesPowersave(cpu.getReg_spaj());
					if(cpu.powersaveProses != null) {
						cpu.lsbs_id=uwManager.selectBusinessId(cpu.reg_spaj);
						cpu.powersaveProses.reg_spaj=cpu.reg_spaj;
						cpu.powersaveProses.mps_rate=uwManager.selectRatePowerSave(cpu.getPowersaveProses());
						
	//					cek pinjaman konvensional
//						TODO:cek pinjaman ini buat apa ya??
						cpu.flagPinjaman=uwManager.selectCekPinjaman(cpu.getReg_spaj(), 0);
							
						
						cpu.selisih=FormatDate.dateDifference(FormatDate.add(cpu.powersaveProses.mps_batas_date, Calendar.DATE, 1), elionsManager.selectSysdate(), true);//						add by rudy - req annalisa helpdesk #03743
						
						cpu.powersaveUbah.setLus_id(Integer.parseInt(currentUser.getLus_id()));
						
						cpu.listPowersaveUbah=uwManager.selectUbahView(cpu.reg_spaj);
						
						 errors.reject("","Berhasil menampilkan data, silahkan melanjutkan");
					}
					else errors.reject("", "Bukan Polis Power Save Inforce.");
				}
			}
			
//			5. Validasi produk, hanya boleh powersave 
			if(!errors.hasErrors() && !(products.powerSave(cpu.lsbs_id.toString()) )){
				errors.reject("","Polis yang Anda masukkan bukan produk SAVE");
			}
			

			
		}else if(process != "0") {
			cpu.flagEditMGI=cpu.flagEditMGI==null?0:cpu.flagEditMGI;
			cpu.flagEditRATE=cpu.flagEditRATE==null?0:cpu.flagEditRATE;
			cpu.flagEditRO=cpu.flagEditRO==null?0:cpu.flagEditRO;
			cpu.flagStopRO=cpu.flagStopRO==null?0:cpu.flagStopRO;
			
			if(cpu.flagEditRATE==1)	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mps_rate", "","Isi terlebih dahulu rate perubahan");
			if(cpu.flagEditMGI==1){
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mps_jangka_inv", "","Isi terlebih dahulu MGI perubahan");
				if(cpu.mps_jangka_inv==0){
					errors.reject("mps_jangka_inv","Isi terlebih dahulu MGI perubahan");
				}
			}
			if(cpu.flagEditRO==1) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mps_roll_over", "","Isi terlebih dahulu Roll Over perubahan");
				
				if(cpu.mps_roll_over==0){
					errors.reject("","Isi terlebih dahulu Roll Over perubahan");
				}
			}
			
			if(cpu.flagStopRO==1&(cpu.flagEditMGI==1|cpu.flagEditRATE==1|cpu.flagEditRO==1)){
				errors.reject("","Gagal Proses Data.  Proses -Stop Roll Over- Tdk Dapat digabung dengan proses lainnya.");
			}
			
			
			
			
		}
		else if(ganti!= "0") {
			if(!errors.hasErrors()){
				cpu.powersaveProses.mps_rate=uwManager.selectRatePowerSave(cpu.getPowersaveProses());
				cpu.powersaveProses.mps_rate=cpu.powersaveProses.mps_rate==null?0.0:cpu.powersaveProses.mps_rate;
				errors.reject("","Beg Date Next MGI Berubah");
			}
		}
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	
		CommandPowersaveUbah cpu = (CommandPowersaveUbah) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String pesan = "";
		
		String process=request.getParameter("proses1")==""?"0":"1";
		String ganti=request.getParameter("ganti")==""?"0":"1";
		
		if(request.getParameter("proses1") != "0") {
			cpu=uwManager.processCPU(cpu);
			
			pesan=cpu.messageError;
		}
	
		
		return new ModelAndView(new RedirectView("ubah.htm?spaj="+cpu.reg_spaj)).addObject("pesan", pesan);
	}
	
}