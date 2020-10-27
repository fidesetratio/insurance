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
import com.ekalife.elions.model.SpajDet;
import com.ekalife.utils.parent.ParentController;

/**
 * Controller untuk Upload Dokumen Scan Cabang
 * 
 * @author Yusuf
 *
 */
public class UploadDokumenAsmController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<String, Object> cmd = new HashMap<String, Object>();
		String no_blanko 		= ServletRequestUtils.getStringParameter(request, "no_blanko", "").replace(".", "").trim();
		List<SpajDet> dataBlanko= null;
		String errorMessage		= "";
		List<DropDown> jenis 	= elionsManager.selectDropDown("eka.lst_scan", "nmfile", "ket", "dept", "dept, ket, nmfile", "dept = 'UW'");

		// tambah validasi bahwa user cabang hanya bisa akses spaj tertentu
		
		//user menekan tombol show spaj/polis
		if(request.getParameter("show") != null){
			if(no_blanko.length() == 6){
				dataBlanko = uwManager.selectMstSpajDetBasedBlanko( no_blanko );
			}else{ 
				errorMessage = "Silahkan masukkan Nomor Blanko yang Benar.";
			}
			
			if(errorMessage.equals("") && dataBlanko == null || errorMessage.equals("") && dataBlanko != null && dataBlanko.size() == 0 ){
				errorMessage = "Data tidak ditemukan. Mohon Cek Nomor Blanko yang Anda Masukkan.";
			}
			
		//user menekan tombol upload file
		}else if(request.getParameter("upload") != null){
			String pesan = uwManager.prosesUploadDokumenAsm(no_blanko, request);
			cmd.put("pesan", pesan);
		}
				
		//put all variables into model object
		cmd.put("no_blanko", 	no_blanko);
		cmd.put("errorMessage", errorMessage);
		cmd.put("dataBlanko",	dataBlanko);
		cmd.put("daftarJenis", jenis);
		
		return new ModelAndView("common/upload_dokumen_asm", cmd);
	}
}