package com.ekalife.elions.web.rekruitment;

import java.io.File;
import java.io.IOException;
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
import javax.servlet.http.HttpSession;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Kuesioner;
import com.ekalife.elions.model.Scan;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.parent.ParentController;

import id.co.sinarmaslife.std.model.vo.DropDown;

/**
 * Class untuk upload dokumen new business
 * 
 * @author Yusuf
 * @since Feb 3, 2009 (10:29:01 AM)
 */
public class UploadRekruitmentNewController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map<String, Object> cmd = new HashMap<String, Object>();
		
		String kode_rekrut = request.getParameter("kode_rekrut");
		if (kode_rekrut == null)
		{
			kode_rekrut ="";
		}
		Kuesioner data=new Kuesioner();
		data = this.elionsManager.selectkuesioner(kode_rekrut);
		data.setDaftarTanggungan(this.uwManager.select_tertanggung_rekrut(kode_rekrut));
		
		
		
		//klo dari e-agency cek hanya boleh yang punya dia aja di akses
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		if(currentUser.getMsag_id_ao()!=null){
			//klo sudah ditransfer hanya bisa view aja
			if(data.getMku_tgl_transfer_admin()!=null){
				cmd.put("editAuthorize", 1);
			}
			
			//klo bukan inputan agen di pentalin			
			if(!data.getMsag_id().equals(currentUser.getMsag_id_ao())){
				ServletOutputStream out;
				try {
					out = response.getOutputStream();
					out.println("<script>alert('Mohon maaf, Anda tidak memiliki hak akses ke halaman ini.');history.go(-1);</script>");
		    		out.flush();
		    		return null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
				}
				return null;
			}
		}
		
		cmd.put("select_berkas",DroplistManager.getInstance().get("BERKAS_AGEN.xml","ID",request));
		String agent = data.getMku_no_reg();	
		HttpSession session = request.getSession();
		String res = session.getServletContext().getResource("/xml/").toString();
		String jenis = Common.searchXml(res+"JENIS_CABANG.xml", "ID", "JENIS", data.getMku_jenis_cabang());
//		String dir = props.getProperty("pdf.dir.export") + "/" + lca_id;
		String dir = props.getProperty("pdf.dir.export.rekruitment");
		
		dir = dir +  "\\" +jenis+ "\\"+agent;
		
		
		
		Upload upload = new Upload();

		List<Scan> daftarScan = uwManager.selectLstScan("DS",null);
		
//		String[] daftarFileName 	= props.getProperty("upload.scan.file").split(",");
//		String[] daftarFileTitle 	= props.getProperty("upload.scan.nama").split(",");
//		String[] daftarFileRequired = props.getProperty("upload.scan.required").split(",");

		upload.setDaftarFile(new ArrayList<MultipartFile>(daftarScan.size()));

		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		//User currentUser = (User) request.getSession().getAttribute("currentUser");
		String berkas=ServletRequestUtils.getStringParameter(request, "berkas","0");		
//		cmd.put("reg_spaj", 	reg_spaj);
//		cmd.put("direktori", dir + "/" + reg_spaj);
		cmd.put("direktori", dir);
		
		List<DropDown> daftarFile = new ArrayList<DropDown>();
		for(Scan s : daftarScan) {
			daftarFile.add(new DropDown(s.nmfile, s.ket, String.valueOf(s.wajib)));
		}
		cmd.put("daftarFile", daftarFile);
		
		if(request.getParameter("upload")!=null) {
			
//			File destDir = new File(dir + "/" + reg_spaj);
			Date dateNow = elionsManager.selectSysdateSimple();
			DateFormat ff = new SimpleDateFormat("yyyyMMdd");
//			DateFormat yf = new SimpleDateFormat("yyyy");
//			DateFormat mf = new SimpleDateFormat("MM");
//			DateFormat df = new SimpleDateFormat("dd");
			DateFormat hm = new SimpleDateFormat("hhmmss");
			String dateFormatFf = ff.format(dateNow);
//			String dateFormatYf = yf.format(dateNow);
//			String dateFormatMf = mf.format(dateNow);
//			String dateFormatDf = df.format(dateNow);
			String dateFormatHm = hm.format(dateNow);
			File destDir = new File(dir);
			if(!destDir.exists()) destDir.mkdir();
			
//			for(int i=0; i<upload.getDaftarFile().size(); i++) {
				try{
					MultipartFile file = upload.getFile1();
					if(berkas.equals("0")){
						cmd.put("pesan", "Silahkan pilih terlebih dahulu tipe berkas");
					}else if(file.isEmpty()==false){
						String contentType=file.getContentType();
						String []namafile = file.getOriginalFilename().split(".");
						if(file.getSize()>=1024*5000){
							cmd.put("pesan", "Mohon maaf, ukuran file maksimal 5 MB");
						}else if("image/pjpeg,image/jpeg,image/jpg".contains(contentType.toLowerCase())){	
							String berkasName= Common.searchXml(res+"BERKAS_AGEN.xml", "ID", "BERKAS", berkas).replace(" ", "_");
		//					String fileName = reg_spaj + daftarScan.get(i).getNmfile() + " " + "WWW.pdf";
							String fileName = berkasName+ "_" +agent  + ".jpg";
							String dest = destDir.getPath() +"/"+ fileName; //file.getOriginalFilename();
							File outputFile = new File(dest);
						    FileCopyUtils.copy(file.getBytes(), outputFile);
							cmd.put("pesan", "Data berhasil di-upload.");
							Kuesioner updateKuesioner=new Kuesioner();
							updateKuesioner.setMku_no_reg(data.getMku_no_reg());
							updateKuesioner.setMku_region(data.getMku_region());
							
							if(berkas.equals("1")){
								updateKuesioner.setMku_flag_ktp(1);
							}else if(berkas.equals("2")){
								updateKuesioner.setMku_flag_foto(1);
							}else if(berkas.equals("3")){
								updateKuesioner.setMku_flag_buku_rek(1);
							} else if(berkas.equals("4")){
								updateKuesioner.setMku_flag_bsb_ujian(1);
							}  
							
							uwManager.updateKuesioner(updateKuesioner);
							data = this.elionsManager.selectkuesioner(kode_rekrut);
							data.setDaftarTanggungan(this.uwManager.select_tertanggung_rekrut(kode_rekrut));
						}else{
							cmd.put("pesan", "Mohon maaf, upload dokumen hanya mendukung file dengan format *.jpg *.jpeg *.JPG");
						}
					}else{
						cmd.put("pesan", "Silahkan pilih file yang ingin di upload terlebih dahulu");
					}
				}catch (Exception e) {
					// TODO: handle exception
					logger.error("ERROR :", e);
					cmd.put("pesan", ""+e.getMessage());
				}
//			}

		
			
		} else {
			//cmd.put("pesan", "Silahkan isi minimal satu file untuk di-upload");
		}

//		List<DropDown> daftarAda = FileUtils.listFilesInDirectory(dir +"/"+ reg_spaj);
		List<DropDown> daftarAda = FileUtils.listFilesInDirectory(dir);
		cmd.put("daftarAda", 	daftarAda);
		cmd.put("berkas", 	berkas);
		

		
		cmd.put("data",data);
		return new ModelAndView("rekruitment/upload_rekruitment_new", cmd);
	}
	  
}