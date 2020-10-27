package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.PowersaveCair;
import com.ekalife.elions.model.SpajDet;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.parent.ParentController;

/**
 * Controller untuk Upload Dokumen Scan Cabang
 * 
 * @author Yusuf
 *
 */
public class MoveDokumenAsmController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<String, Object> cmd = new HashMap<String, Object>();
		String no_blanko 		= ServletRequestUtils.getStringParameter(request, "no_blanko", "").replace(".", "").trim();
		String no_spaj 		= ServletRequestUtils.getStringParameter(request, "no_spaj", "").replace(".", "").trim();
		String jmlFile =  ServletRequestUtils.getStringParameter(request, "jmlFile", "").trim();
		List<SpajDet> dataBlanko= null;
		String errorMessage		= "";
		List<File> daftarFile = null;
		List<Map> checkSpaj = null;
		List fileList = null;
		String suksesBlanko = null;
		
		// tambah validasi bahwa user cabang hanya bisa akses spaj tertentu
		
		//user menekan tombol show spaj/polis
		File directory = new File(
				props.getProperty("pdf.dir.uploadASM") + "\\" +
				no_blanko);
		if(request.getParameter("inputSpaj") != null || request.getParameter("show") != null){
			if(no_blanko.length() == 6){
				dataBlanko = uwManager.selectMstSpajDetBasedBlanko( no_blanko );
			}else{ 
				errorMessage = "Silahkan masukkan Nomor Blanko yang Benar.";
			}
			if(errorMessage.equals("") && dataBlanko == null || errorMessage.equals("") && dataBlanko != null && dataBlanko.size() == 0 ){
				errorMessage = "Data tidak ditemukan. Mohon Cek Nomor Blanko yang Anda Masukkan.";
			}else if( errorMessage.equals("") && dataBlanko != null && dataBlanko.size() >0   ){
				suksesBlanko = "sukses";
			}
			if(request.getParameter("show") != null)
			{
				errorMessage = "";
				if(no_spaj.length() == 11){
					checkSpaj = uwManager.selectMstPolicyBasedRegSpaj(no_spaj);
				}else{ 
					errorMessage = "Silahkan masukkan Nomor SPAJ yang Benar.";
				}
				if(errorMessage.equals("") && checkSpaj == null || errorMessage.equals("") && checkSpaj != null && checkSpaj.size() == 0 ){
					errorMessage = "Nomor SPAJ tidak ditemukan. Mohon Cek Nomor SPAJ yang Anda Masukkan.";
				}else if (errorMessage.equals("") && checkSpaj != null && checkSpaj.size() > 0){
					daftarFile = FileUtils.listFilesInDirectory2(directory.toString());
				}
				if( daftarFile != null && daftarFile.size() > 0 ){
					cmd.put("jmlFile", 	daftarFile.size());
				}else if( daftarFile != null && daftarFile.size() == 0 ){
					errorMessage = "File Kosong";
				}
			}
		//user menekan tombol upload file
		}else if(request.getParameter("submit") != null){
			String tempCheckBoxString = null;
			if( jmlFile != null && LazyConverter.toInt(jmlFile) > 0 )
			{
				dataBlanko = uwManager.selectMstSpajDetBasedBlanko( no_blanko );
				suksesBlanko = "sukses";
				daftarFile = FileUtils.listFilesInDirectory2(directory.toString());
				for( int i = 1 ; i <= daftarFile.size() ; i ++ )
				{
					String checkBox = request.getParameter("checkBoxFile"+i);
					if( checkBox == null ){ checkBox = "0" ;}
					if( tempCheckBoxString == null ){
						tempCheckBoxString = checkBox;
					}else{
						tempCheckBoxString = tempCheckBoxString + "," + checkBox;
					}
				}
				String pesan = uwManager.prosesMoveDokumenAsm(no_blanko, request, daftarFile, tempCheckBoxString, no_spaj);
				daftarFile = FileUtils.listFilesInDirectory2(directory.toString());
				cmd.put("pesan", pesan);
				if( daftarFile != null ){
					cmd.put("jmlFile", 	daftarFile.size());
				}
			}
		}
				
		//put all variables into model object
		cmd.put("no_blanko", 	no_blanko);
		cmd.put("errorMessage", errorMessage);
		cmd.put("daftarFile", daftarFile);
		cmd.put("dataBlanko", dataBlanko);
		cmd.put("no_spaj", no_spaj);
		cmd.put("daftarFile", daftarFile);
		
		return new ModelAndView("common/move_dokumen_asm", cmd);
	}
}