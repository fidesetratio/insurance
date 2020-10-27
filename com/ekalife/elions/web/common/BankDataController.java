package com.ekalife.elions.web.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;
import com.google.gson.Gson;

import id.co.sinarmaslife.std.model.vo.DropDown;

public class BankDataController extends ParentController {
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<String> err = new ArrayList<String>();
		DateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
	//	String jenisupload = ServletRequestUtils.getStringParameter(request, "jenisupload", "");
		String judul=ServletRequestUtils.getStringParameter(request, "judul", "MARKETING TOOL SYSTEM");
		String flag=ServletRequestUtils.getStringParameter(request, "flag", "");
		Date sysdate = elionsManager.selectSysdateSimple();
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
		String tgl = format.format(sysdate);
		List select_data = uwManager.selectBankData(1);
		String lde_id =currentUser.getLde_id();
		String json = ServletRequestUtils.getStringParameter(request, "json","0");
		
		List<DropDown> listkategori = new ArrayList<DropDown>();
		listkategori.add(new DropDown("1", "Jenis Data "));
		listkategori.add(new DropDown("2", "Kriteria "));
		listkategori.add(new DropDown("3", "Sub Kriteria "));

		
		
		if(json.equals("1")){
			String lca = ServletRequestUtils.getStringParameter(request, "jenis", " ");
    		
			List result = bacManager.selectBankDataSub(Integer.parseInt(lca));
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
		}
		
		if(json.equals("2")){
			String lca = ServletRequestUtils.getStringParameter(request, "jenis", " ");
    		List result=null;
			if (lca.equals("23")||lca.equals("24")||lca.equals("25")||lca.equals("26")||lca.equals("27")||lca.equals("28")){
				result = bacManager.selectBankDataSub(99);
			}else{
				result = bacManager.selectBankDataSub(Integer.parseInt(lca));
			}
			
			
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
		}
		
		if(json.equals("0")){
			if(request.getParameter("download") !=null){
				String uploadid = ServletRequestUtils.getStringParameter(request, "uploadid", "");
				String code_id = ServletRequestUtils.getStringParameter(request, "code_id", "");
				String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
				//bagian ini agar data listnya tetap tampil
				String jenis = ServletRequestUtils.getStringParameter(request, "jenis", "");
				String level = null;
				logger.info(request.getParameter("tglawal")+" "+request.getParameter("tglakhir"));
				/*Date tgl1 = formatDate.parse(request.getParameter("tglawal"));
				Date tgl2 = formatDate.parse(request.getParameter("tglAkhir"));*/
				
				String tgl1 = ServletRequestUtils.getStringParameter(request, "bdate");
				String tgl2 = ServletRequestUtils.getStringParameter(request, "edate");
				List list = uwManager.selectListMstHistoryUploadByDate(judul, uploadid,tgl1,tgl2);
				//List list = elionsManager.selectListMstHistoryUpload(jenis, level, tglawal, tglakhir, judul);*/
				cmd.put("list", list);
				String path = uwManager.selectPathMstHistoryUpload2(uploadid,code_id);
				String filename = uwManager.selectFilenameMstHistoryUpload2(uploadid,code_id);
				String ext = path.substring(path.indexOf("."),path.length());
				File l_file = new File(path);
				FileInputStream in = null;
				ServletOutputStream ouputStream = null;
				try{
					
					response.setContentType("application/force-download");//application/force-download
					response.setHeader("Content-Disposition", "Attachment;filename="+filename+ext);
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					
					in = new FileInputStream(l_file);
				    ouputStream = response.getOutputStream();
				    
				    IOUtils.copy(in, ouputStream);
				}catch (Exception e) {
					  cmd.put("pesan", "File Tidak Ada / Di Hapus ");
					logger.error("ERROR :", e);
				}finally {
					try {
						if(in!=null) {
							in.close();
						}
						if(ouputStream!=null) {
							ouputStream.close();
						}
					}catch (Exception e) {
						logger.error("ERROR :", e);
					}
				}
				
				return null;
			}
			
			if(request.getParameter("viewbutton") !=null){
				String jenis = ServletRequestUtils.getStringParameter(request, "jenis", "");
				String jenisdataKode=bacManager.selectNamaBankData(ServletRequestUtils.getStringParameter(request, "select_data_view", null));
				String sub_kriteriaKode=ServletRequestUtils.getStringParameter(request, "select_sub_kriteria_view", null);
				String jenisdata=bacManager.selectNamaBankData(ServletRequestUtils.getStringParameter(request, "select_data_view", null));
				String kriteria=bacManager.selectNamaBankData(ServletRequestUtils.getStringParameter(request, "select_kriteria_view", null));
				String sub_kriteria=bacManager.selectNamaBankData(sub_kriteriaKode);
				String path =jenisdata+"/"+kriteria+"/"+sub_kriteria;
				//Date tgl1 = formatDate.parse(request.getParameter("tgl1"));
				//Date tgl2 = formatDate.parse(request.getParameter("tgl2"));
				
				String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
	    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
				
				String level = null;
				Date tglawal = null, tglakhir = null;
				List list = uwManager.selectListMstHistoryUploadByDate(judul, path,bdate,edate);
				cmd.put("list", list);
				cmd.put("jenis", jenis);
				cmd.put("tglDown1", bdate);
				cmd.put("tglDown2", edate);
				cmd.put("bdate", bdate);
				cmd.put("edate", edate);
	
			}
			
			if(request.getParameter("update") !=null){
				String sumber = ServletRequestUtils.getStringParameter(request, "sumber", "");
				String kd_id = ServletRequestUtils.getStringParameter(request, "kd_id", "");
				
				String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
	    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
				
	
			}
			
			
			if(request.getParameter("uploadbutton")!=null){
				String judul_upload=ServletRequestUtils.getStringParameter(request, "judul_upload", null);
				/*String jenisdata=ServletRequestUtils.getStringParameter(request, "select_data", null);
				String kriteria=ServletRequestUtils.getStringParameter(request, "select_kriteria", null);
				String sub_kriteria=ServletRequestUtils.getStringParameter(request, "select_sub_kriteria", null);*/
				String kode_id=ServletRequestUtils.getStringParameter(request, "kode_id", null);//sumber
				String sumber=ServletRequestUtils.getStringParameter(request, "sumber", null);
				
				String jenisdata=bacManager.selectNamaBankData(ServletRequestUtils.getStringParameter(request, "select_data", null));
				String kriteria=bacManager.selectNamaBankData(ServletRequestUtils.getStringParameter(request, "select_kriteria", null));
				String sub_kriteria=bacManager.selectNamaBankData(ServletRequestUtils.getStringParameter(request, "select_sub_kriteria", null));
				
				SimpleDateFormat dt = new SimpleDateFormat("yyyyMM");
				
				Date now = elionsManager.selectSysdate();
				//bagian ini untuk generate upload_id
				DateFormat df = new SimpleDateFormat("yyyyMM");
				Date tanggalData = formatDate.parse(request.getParameter("tgl_kumpul"));
				sysdate = elionsManager.selectSysdateSimple();
				tgl = formatDate.format(sysdate);
				String upload_id = jenisdata+"\\"+kriteria+"\\"+sub_kriteria+"\\";
				String upload_id2 = jenisdata+"/"+kriteria+"/"+sub_kriteria;
				//upload_id = upload_id+"000";
				
				String tDest=props.getProperty("upload.marketing")+"\\"+jenisdata+"\\"+kriteria+"\\"+sub_kriteria;
			/*	File destDir = new File(tDest);
				if(!destDir.exists()) destDir.mkdir();*/
				
				File userDir = new File(props.getProperty("upload.marketing")+"\\"+jenisdata+"\\"+kriteria+"\\"+sub_kriteria);
		        if(!userDir.exists()) {
		            userDir.mkdirs();
		        }
				
				Upload upload = new Upload();
				ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
				binder.bind(request);
			
			if(!jenisdata.equals("") && !kriteria.equals("") && !sub_kriteria.equals("") && !judul_upload.equals("")){
				if(upload.getFile1().isEmpty()==false){
					String filename = upload_id;
					String namefile = upload.getFile1().getName();
					String name=filename.toUpperCase();
					String pdf = judul_upload;
					
					logger.info(upload.getFile1().getContentType());
					
					String ext = upload.getFile1().getOriginalFilename();
					ext = ext.substring(ext.indexOf("."),ext.length());
					
					String dest=tDest+"\\"+judul_upload+ext;
					File outputFile = new File(dest);
					FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
					    
					    uwManager.insertMstHistoryUpload( kode_id, upload_id2, upload_id2, pdf, name, tanggalData, sub_kriteria, "AKTIF", null,Integer.parseInt(currentUser.getLus_id()), sumber, dest,tanggalData);
				   
				    cmd.put("pesan", "Data Berhasil diupload");
				    
				}
			}
			else{
				err.add("Semua Field harus diisi.");
				cmd.put("pesan", err);
			}
			
			cmd.put("judul_upload", judul_upload);
			cmd.put("judul", judul);
			//cmd.put("revisi", revisi+1);
			
		}
			cmd.put("select_data", select_data);
			cmd.put("listkategori", listkategori);
			cmd.put("lde_id", lde_id);//currentUser
			String today = defaultDateFormat.format(elionsManager.selectSysdate());
			cmd.put("today", today);
		
			return new ModelAndView("common/bank_data", cmd);
		}
	
	return null;
	}
}
