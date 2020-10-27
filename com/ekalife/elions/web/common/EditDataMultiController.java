package com.ekalife.elions.web.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.User;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentMultiController;

/**
 * Semua yg berhubungan dgn edit data, harus masuk disini
 * 
 * @author Yusuf
 * @since 1 Apr 2009
 */
public class EditDataMultiController extends ParentMultiController {

	/* Validasi Umum, semua controller harus ada validasi ini! */
	private List<String> validasiUmum(String reg_spaj){
		List<String> hasil 		= new ArrayList<String>();
		Date sysdate 			= elionsManager.selectSysdate(0);
		
		Map info 				= uwManager.selectValidasiSebelumEditDate(reg_spaj);
		int lspd_id 			= ((BigDecimal) info.get("LSPD_ID")).intValue();
		String lspd_position 	= (String) info.get("LSPD_POSITION");
		Date mste_beg_date 		= (Date) info.get("MSTE_BEG_DATE");
		
		if(lspd_id == 95){
			hasil.add("Polis ini sudah dibatalkan!");
		}else if(lspd_id > 6){
			hasil.add("Posisi polis saat ini di " + lspd_position + ". Sudah tidak dapat diedit!");
		}else if(FormatDate.dateDifference(mste_beg_date, sysdate, true) > 30){
			hasil.add("Sudah lewat 30 hari dari BEG DATE Polis. Harap hubungi IT!");
		}
		
		return hasil;
	}
	
	/* Window Utama */
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		return new ModelAndView("common/edit/main");
	}
	
	/* Window Powersave */
	public ModelAndView powersave(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		Map m = new HashMap();
		String reg_spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
		Powersave powersave = elionsManager.select_powersaver(reg_spaj);

		List<String> errorAwal = validasiUmum(reg_spaj);
		
		//Validasi awal, saat baru masuk halaman
		if(!errorAwal.isEmpty()){
			//Sengaja dikosongkan
		}else if(elionsManager.selectCountProsaveBayar(reg_spaj) > 0){
			errorAwal.add("Sudah ada proses pembayaran, harap konfirmasi ke LB!");
		}else if(uwManager.selectCountRolloverPowersave(reg_spaj) > 0){
			errorAwal.add("Sudah ada rollover, harap konfirmasi ke LB!");
		}else if(powersave == null){
			errorAwal.add("Harap cek nomor Polis / Spaj. Polis ini bukan polis Powersave!");
		}else{
			//Bila tidak ada error awal
			int ro = powersave.getMps_roll_over().intValue();
			m.put("rollover", (ro==1 ? "ROLLOVER ALL" : ro==2 ? "ROLLOVER PREMI" : ro==3 ? "AUTOBREAK" : ""));

			//Bila user menekan tombol proses data
			if(request.getParameter("save") != null){
				Map params = new HashMap();
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				params.put("jenis", 	ServletRequestUtils.getStringParameter(request, "jenis", ""));
				params.put("mgi", 		ServletRequestUtils.getStringParameter(request, "mgi", ""));
				params.put("rollover", 	ServletRequestUtils.getStringParameter(request, "rollover", ""));
				params.put("rate", 		ServletRequestUtils.getStringParameter(request, "rate", ""));
				params.put("begdate", 	ServletRequestUtils.getStringParameter(request, "begdate", ""));
				params.put("premi", 	ServletRequestUtils.getStringParameter(request, "premi", ""));
				params.put("desc", 		ServletRequestUtils.getStringParameter(request, "desc", ""));
				//
				m.put("pesan", uwManager.editDataPowersave(params, currentUser));
			}
		}
		
		m.put("reg_spaj", reg_spaj);
		m.put("powersave", powersave);
		m.put("errorAwal", errorAwal);
		
		m.put("select_rollover", DroplistManager.getInstance().get("ROLLOVER.xml","ID",request));
		m.put("select_mgi", new int[]{1, 3, 6, 12, 24, 36});
		
		return new ModelAndView("common/edit/powersave", m);
	}
	
}