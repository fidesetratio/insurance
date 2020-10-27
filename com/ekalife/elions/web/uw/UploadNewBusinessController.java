package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;

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
import com.google.common.collect.Lists;

/**
 * Class untuk upload dokumen new business
 * 
 * @author Yusuf
 * @since Feb 3, 2009 (10:29:01 AM)
 */
public class UploadNewBusinessController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String reg_spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
		User user = (User) request.getSession().getAttribute("currentUser");
		String lca_id = elionsManager.selectCabangFromSpaj(reg_spaj);
		String dir = props.getProperty("pdf.dir.export") + "//" + lca_id;
		Integer lsbsId =null;
		Integer lsdbsNumber =null;
		Map mDataUsulan=uwManager.selectDataUsulan(reg_spaj);
		if(mDataUsulan!=null){
			lsbsId=(Integer)mDataUsulan.get("LSBS_ID");
			lsdbsNumber=(Integer)mDataUsulan.get("LSDBS_NUMBER");
		}
		// utk test
		// fadly
//		String dir = "C:\\EkaWeb\\lca_id";
		List<Scan> daftarScan =null;
		
		Map<String, Object> cmd = new HashMap<String, Object>();
		Upload upload = new Upload();
		
		if (user.getJn_bank()==2){
			daftarScan = uwManager.selectLstScan("BB",null);
		}else if (user.getCab_bank().equals("") && ((lsbsId==142 && lsdbsNumber==2) || (lsbsId==183 && (lsdbsNumber>=46 && lsdbsNumber<=60)) ))
		{
			daftarScan = uwManager.selectLstScan("BB",null);
		}else{
			daftarScan = uwManager.selectLstScan("UW",null);
		}
		
//		String[] daftarFileName 	= props.getProperty("upload.scan.file").split(",");
//		String[] daftarFileTitle 	= props.getProperty("upload.scan.nama").split(",");
//		String[] daftarFileRequired = props.getProperty("upload.scan.required").split(",");

		upload.setDaftarFile(new ArrayList<MultipartFile>(daftarScan.size()));

		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
				
		cmd.put("reg_spaj", 	reg_spaj);
		cmd.put("direktori", dir + "//" + reg_spaj);
		
		List<DropDown> daftarFile = new ArrayList<DropDown>();
		for(Scan s : daftarScan) {
			daftarFile.add(new DropDown(s.nmfile, s.ket, String.valueOf(s.wajib)));
		}
		cmd.put("daftarFile", daftarFile);
		
		if(request.getParameter("upload")!=null) {
			
			File destDir = new File(dir + "//" + reg_spaj);
			if(!destDir.exists()) {
				destDir.mkdirs();
			}
			StringBuffer filenames = new StringBuffer();
			Date nowDate = elionsManager.selectSysdate1("dd", false, 0);
			DateFormat yyyy = new SimpleDateFormat("yyyy");
			String year = yyyy.format(nowDate);
			
            for(int i=0; i<upload.getDaftarFile().size(); i++) {
	            MultipartFile file = (MultipartFile) upload.getDaftarFile().get(i);
				String extention = ".pdf";				
				String namaFileUpload=file.getOriginalFilename();				
					if(namaFileUpload.length()!=0){	
						String fileExtention = namaFileUpload.substring(namaFileUpload.length() - extention.length(), namaFileUpload.length());
						if (!fileExtention.toLowerCase().equals(extention)){
							cmd.put("pesan", " File Data yang di upload bukan File PDF . Pastikan tipe file yang anda upload adalah file PDF." );							
							return new ModelAndView("uw/upload_nb", cmd);
						}
					}
            }
			Integer hasil = uwManager.selectProsesUploadScan (reg_spaj, upload, destDir, daftarScan,filenames, currentUser);//true berarti berhasil, false gagal.
			Integer lspd_id = elionsManager.selectPosisiDocumentBySpaj(reg_spaj);
			if(hasil==1){//*data berhasil diupload
				if(lspd_id==1){
					cmd.put("pesan", "Data berhasil di-upload.Silakan ditransfer ke UW");
				}else{
					cmd.put("pesan", "Data berhasil di-upload.");
				}
				
			}
			if(hasil==0){//*data gagal diupload
				cmd.put("pesan", "Data untuk no SPAJ "+reg_spaj+" tidak berhasil di-upload.Silakan hubungi IT");
			}
			if(hasil==2){//*SPT belum divalidasi
				cmd.put("pesan", " SPT berhasil di-upload. SPT untuk no SPAJ  "+reg_spaj+" Belum di Proses Validasi Oleh UnderWriting atau Sudah pernah Dikirim ke Direksi.");
			}
			if(hasil==3){//*SPT sudah divalidasi
				cmd.put("pesan", "Data Berhasil di upload dan Email Permohonan Akseptasi SPT di kirim ke Direksi" );
			}
			
		} else {
			//cmd.put("pesan", "Silahkan isi minimal satu file untuk di-upload");
		}

		List<DropDown> daftarAda = FileUtils.listFilesInDirectory(dir +"/"+ reg_spaj);
		cmd.put("daftarAda", 	daftarAda);

		return new ModelAndView("uw/upload_nb", cmd);
	}
	
}