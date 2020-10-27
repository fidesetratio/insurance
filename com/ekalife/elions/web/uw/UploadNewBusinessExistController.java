package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.FileUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Hadiah;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.MstInbox;
import com.ekalife.elions.model.MstInboxChecklist;
import com.ekalife.elions.model.MstInboxHist;
import com.ekalife.elions.model.Scan;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.common.CommonConst;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.parent.ParentController;

/**
 * Class untuk Mencopy dokumen new business ke Spaj Yang baru (Req Pak Rudi & BAS)
 *  
 * @author Ryan
 * @since June 27, 2013 (10:29:01 AM)
 */
public class UploadNewBusinessExistController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String reg_spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		User user = (User) request.getSession().getAttribute("currentUser");
		String copy[] = request.getParameterValues("copy");
		String lca_id = elionsManager.selectCabangFromSpaj(reg_spaj);
		String lca_id2 = elionsManager.selectCabangFromSpaj(spaj);
		String dir = props.getProperty("pdf.dir.export") + "//" + lca_id;
		String dir2 = props.getProperty("pdf.dir.export") + "//" + lca_id2;
		List<Scan> daftarScan =null;
		Pemegang pmg =elionsManager.selectpp(reg_spaj);
		String spajProdukLama= uwManager.selectMstInboxNoReff(pmg.getMspo_no_blanko());
		Map<String, Object> cmd = new HashMap<String, Object>();
		Upload upload = new Upload();
		
		if (spajProdukLama!=null){
			spaj = spajProdukLama;
		}
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
				
		cmd.put("reg_spaj", 	reg_spaj);
		cmd.put("spaj", 	spaj);
		cmd.put("direktori", dir + "//" + reg_spaj);
		
		//action - view , buat liat list spaj lama
		if(request.getParameter("view")!=null) {
			
			List<DropDown> daftarAdaLama = FileUtils.listFilesInDirectory(dir2 +"/"+ spaj);
			cmd.put("daftarAdaLama", 	daftarAdaLama);
		} 
		
		//action - Copy, buat copy doc list spaj lama ke Spaj Baru
		if(request.getParameter("action")!=null) {
			/*s*/
			Integer hasil = uwManager.selectProsesUploadScanExist (reg_spaj, spaj,copy, currentUser, dir, dir2, spajProdukLama);
			
			if(hasil==0){//*data gagal dicopy
				cmd.put("pesan", "Data untuk no SPAJ "+reg_spaj+" tidak berhasil di Copy dari no SPAJ "+spaj+".Silakan hubungi IT");
			}
			if(hasil==1){//*berhasil dicopy
				cmd.put("pesan", "Data untuk no SPAJ "+reg_spaj+" berhasil di Copy dari no SPAJ "+spaj);
			}
		} 
		
		List<DropDown> daftarAda = FileUtils.listFilesInDirectory(dir +"/"+ reg_spaj);
		cmd.put("daftarAda", 	daftarAda);
		cmd.put("spaj", spaj);
		
		if(!spaj.equals("")){
			List<DropDown> daftarAdaLama = FileUtils.listFilesInDirectory(dir2 +"/"+ spaj);
			cmd.put("daftarAdaLama", 	daftarAdaLama);
		}

		return new ModelAndView("uw/upload_nb_exist", cmd);
	}
	
}