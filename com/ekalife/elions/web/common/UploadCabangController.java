package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.PowersaveCair;
import com.ekalife.utils.parent.ParentController;

/**
 * Controller untuk Upload Dokumen Scan Cabang
 * 
 * @author Yusuf
 *
 */
public class UploadCabangController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<String, Object> cmd = new HashMap<String, Object>();
		String reg_spaj 		= ServletRequestUtils.getStringParameter(request, "reg_spaj", "").replace(".", "").trim();
		PowersaveCair dataPolis	= null;
		String errorMessage		= "";
		List<DropDown> jenis 	= elionsManager.selectDropDown("eka.lst_scan", "nmfile", "ket", "dept", "dept, ket, nmfile", "dept = 'UW'");

		// tambah validasi bahwa user cabang hanya bisa akses spaj tertentu
		
		//user menekan tombol show spaj/polis
		if(request.getParameter("show") != null){
			if(reg_spaj.length() == 11){
				dataPolis = elionsManager.selectRolloverData(reg_spaj);
			}else if(reg_spaj.length() == 14){
				reg_spaj = elionsManager.selectSpajFromPolis(reg_spaj);
				dataPolis = elionsManager.selectRolloverData(reg_spaj);
			}else{ 
				errorMessage = "Silahkan masukkan Nomor Register SPAJ / Polis yang Benar.";
			}
			
			if(errorMessage.equals("") && dataPolis == null){
				errorMessage = "Data tidak ditemukan. Mohon Cek Nomor Register SPAJ / Polis yang Anda Masukkan.";
			}
			
		//user menekan tombol upload file
		}else if(request.getParameter("upload") != null){
			String pesan = uwManager.prosesUploadCabang(reg_spaj, request);
			cmd.put("pesan", pesan);
		}
				
		//put all variables into model object
		cmd.put("reg_spaj", 	reg_spaj);
		cmd.put("errorMessage", errorMessage);
		cmd.put("dataPolis",	dataPolis);
		cmd.put("daftarJenis", jenis);
		
		return new ModelAndView("common/uploadcabang", cmd);
	}
}