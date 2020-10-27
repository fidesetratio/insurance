package com.ekalife.elions.web.rekruitment;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.mail.MailException;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.CalonKaryawan;
import com.ekalife.elions.model.Kuesioner;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentMultiController;
import com.google.gson.Gson;

import id.co.sinarmaslife.std.model.vo.DropDown;

/**
 * 
 * @author alfian_h
 * Class MultiController
 *
 */

public class RekruitmentMultiController extends ParentMultiController {

	ElionsManager eManager = new ElionsManager();
	
	public ModelAndView inputAAJIkandidat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		CalonKaryawan ck = new CalonKaryawan();
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
		List<File> attachments = new ArrayList<File>();
		
		String lca_id = "65";
		
		if(request.getParameter("btnSubmit")!=null){
			Upload upload = new Upload();
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			MultipartFile mf = upload.getFile1();
			String fileName = mf.getOriginalFilename();
			String format = fileName.substring(fileName.length()-3, fileName.length()).toUpperCase();
			
			if(format.equals("PDF")){
				String ktp = ServletRequestUtils.getStringParameter(request, "ktp");
				String nama = ServletRequestUtils.getStringParameter(request, "nama");
				Integer jk = ServletRequestUtils.getIntParameter(request, "jk");
				String alamat = ServletRequestUtils.getStringParameter(request, "alamat");
				String tempat = ServletRequestUtils.getStringParameter(request, "tempat");
				String tl = ServletRequestUtils.getStringParameter(request, "tl");
				Date tgl = df.parse(tl);
				
				ck.setKtp(ktp);
				ck.setNama(nama);
				ck.setJk(jk);
				ck.setAlamat(alamat);
				ck.setTempat_lahir(tempat);
				ck.setTanggal_lahir(tl);
				ck.setDokumen(fileName);
				ck.setLca_id(lca_id);
				
				Map m = new HashMap();
				m.put("ktp", ktp);
				m.put("nama", nama);
				m.put("jk", jk);
				m.put("alamat", alamat);
				m.put("tempat_lahir", tempat);
				m.put("tanggal_lahir", tgl);
				m.put("dokumen", fileName);
				m.put("lca_id", lca_id);
				
				bacManager.insert_aaji_calon_karyawan(m);
				
				String dest = props.getProperty("pdf.dir.rekruitment.aaji");
				File path = new File(dest+"\\"+lca_id+"\\"+fileName);
				FileUtils.writeByteArrayToFile(path, mf.getBytes());
				
				String jenisKelamin=null;
				if(jk==1){
					jenisKelamin="Laki-Laki";
				}else{
					jenisKelamin="Perempuan";
				}
				
				attachments.add(path);
				String to[] = props.getProperty("rekruitment.pic.aaji").split(";");
				String cc[] = props.getProperty("rekruitment.aaji.cc.fcd").split(";");
				
				email.send(true, 
						"info@sinarmasmsiglife.co.id", // from 
						new String[]{"csbranchfcd@sinarmasmsiglife.co.id"}, // to 
						null, // cc 
						null, // bcc
						"Cek Kandidat Karyawan \"Marketing Baru\"", 
						"Harap Cek AAJI <br/>"+
						"<table border='0'>" +
						"<tr><td>No. KTP</td><td>:</td><td>"+ktp+"</td></tr>" +
						"<tr><td>Nama</td><td>:</td><td>"+nama+"</td></tr>" +
						"<tr><td>Jenis Kelamin</td><td>:</td><td>"+jenisKelamin+"</td></tr>" +
						"<tr><td>Alamat</td><td>:</td><td>"+alamat+"</td></tr>" +
						"</table>"+
						"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachments);
				
				map.put("pesan", "Data tersimpan");
			}
			else{
				map.put("pesan", "Format file harus PDF");
			}
		}
		return new ModelAndView("rekruitment/input_aaji_kandidat_karyawan", map);
	}
	
	@SuppressWarnings("unused")
	public ModelAndView updateAAJIkandidat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		CalonKaryawan ck = new CalonKaryawan();
		String ktp = ServletRequestUtils.getStringParameter(request, "ktp");
		List b = new ArrayList();
//		String nama = null;
		if(request.getParameter("cari")!=null){
			if(ktp!=null){
//				List data = bacManager.selectProsesAAJICalonKaryawan();
//				bacManager.schedulerStatusAAJICalonKaryawan();
				@SuppressWarnings("rawtypes")
				List a = bacManager.selectAAJICalonKaryawan(null, null, ktp);
				/*for(int i=0;i<a.size();i++){
					Map m = (HashMap) a.get(i);
					
					String ktp1 = (String) m.get("KTP");
					String nama1 = (String) m.get("NAMA");
				}*/
				map.put("ktp", ktp);
				map.put("calon", a);
			}else{
				map.put("pesan", "No. KTP tidak boleh kosong");
			}
		}
		
		if(request.getParameter("btnUpdate")!=null){
			Integer bl = ServletRequestUtils.getIntParameter(request, "blacklist");
			Integer pt = ServletRequestUtils.getIntParameter(request, "pt");
			Integer join = ServletRequestUtils.getIntParameter(request, "join");
			String ket = ServletRequestUtils.getStringParameter(request, "ket","-");
			String nama = ServletRequestUtils.getStringParameter(request, "nama","");
			
			bacManager.updateAAJICalonKaryawan(bl, pt, join, ket, ktp);
			
			String blacklist, ptLama, statusJoin;
			if(bl==1){
				blacklist="Tidak Bermasalah";
			}else{
				blacklist="Bermasalah";
			}
			
			if(pt==1){
				ptLama="Active";
			}else if(pt==2){
				ptLama="Inactive";
			}else if(pt==3){
				ptLama="Retaker";
			}else{
				ptLama="Tenggarai";
			}
			
			if(join==1){
				statusJoin="Bisa";
			}else{
				statusJoin="Tidak Bisa";
			}
			
			String to[] = props.getProperty("rekruitment.csbranch.fcd").split(";");
			String cc[] = props.getProperty("rekruitment.aaji.cc.fcd").split(";");
			
			email.send(true, 
					"info@sinarmasmsiglife.co.id", // from 
					new String[]{"alfian_h@sinarmasmsiglife.co.id","csbranchfcd@sinarmasmsiglife.co.id"}, // to 
					null, // cc 
					null, // bcc
					"Cek Kandidat Karyawan \"Marketing Baru\"", 
					"Data berikut ini sudah dilakukan pengecekan.<br/>"+
					"<table border='0'>" +
					"<tr><td>No. KTP</td><td>:</td><td>"+ktp+"</td></tr>" +
					"<tr><td>Nama</td><td>:</td><td>"+nama+"</td></tr>" +
					"<tr><td>Blacklist</td><td>:</td><td>"+blacklist+"</td></tr>" +
					"<tr><td>Perusahaan Lama</td><td>:</td><td>"+ptLama+"</td></tr>" +
					"<tr><td>Status Join</td><td>:</td><td>"+statusJoin+"</td></tr>" +
					"<tr><td>Keterangan</td><td>:</td><td>"+ket+"</td></tr>" +
					"</table>"+
					"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
					null);
			
			map.put("pesan", "Data berhasil di-update");
		}
		return new ModelAndView("rekruitment/update_aaji_kandidat_karyawan", map);
	}
	
	public ModelAndView previewTsr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> map=new HashMap<String, Object> ();
		String nip=ServletRequestUtils.getStringParameter(request, "nip", null);
		String dept=ServletRequestUtils.getStringParameter(request, "dept", null);		
		String nama=ServletRequestUtils.getStringParameter(request, "nama", null);
		String ktp=ServletRequestUtils.getStringParameter(request, "ktp", null);
		String alamat=ServletRequestUtils.getStringParameter(request, "alamat", null);
		String bdate=ServletRequestUtils.getStringParameter(request, "bdate", null);
		String edate=ServletRequestUtils.getStringParameter(request, "edate", null);
		String jangka=ServletRequestUtils.getStringParameter(request, "jangka", null);
		String jabatan=ServletRequestUtils.getStringParameter(request, "jabatan", null);
		String atasan=ServletRequestUtils.getStringParameter(request, "atasan", null);
		String jbt=ServletRequestUtils.getStringParameter(request, "jbt", null);
		String eva=ServletRequestUtils.getStringParameter(request, "eva", null);
		String sk=ServletRequestUtils.getStringParameter(request, "sk", null);		
		String shasil=ServletRequestUtils.getStringParameter(request, "noSurat", null);
		String tl=ServletRequestUtils.getStringParameter(request, "tl", null);
		String tgll=ServletRequestUtils.getStringParameter(request, "tgll", null);
		String jkl=ServletRequestUtils.getStringParameter(request, "jkl", null);
		String jbtBancass=ServletRequestUtils.getStringParameter(request, "jabatan2", null);
		String hasil = null;
		String pesan = null;
		map.put("nip", nip);
		map.put("dept", dept);
		map.put("nama", nama);
		map.put("ktp", ktp);
		map.put("alamat", alamat);
		map.put("bdate", bdate);
		map.put("edate", edate);
		map.put("jangka", jangka);
		if(dept.equals("7B")){
			map.put("jabatan", jbtBancass);
		}else{
			map.put("jabatan", jabatan);		
		}
		
		map.put("atasan", atasan);
		map.put("jbt", jbt);
		map.put("eva", eva);
		map.put("sk", sk);
		map.put("shasil", shasil);
		map.put("tl", tl);
		map.put("tgll", tgll);
		map.put("jkl", jkl);
		map.put("jbtBancass", jbtBancass);
		if(request.getParameter("send")==null ){
			hasil=bacManager.createSurattsr(map);
			if(hasil.equals("GAGAL")){
				pesan="gagal membuat surat kesepakatan";
			}
		}	
		
		if(request.getParameter("send")!=null && shasil!=null){
			map.put("shasil", shasil);
			pesan=bacManager.sendEmailTSR(map,shasil,nip,dept);
		}
		
		map.put("pesan", pesan);
		return new ModelAndView("rekruitment/previewTSR", map);
	}
	
	public ModelAndView viewDataTSR(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap <String, Object>  map=new HashMap<String, Object> ();
		String nip=ServletRequestUtils.getStringParameter(request, "nip", null);
		map.put("nip", nip);
		ArrayList data=new ArrayList();
		data=bacManager.selectDataTsr(null, null, null, "2",nip.trim());
		map.put("dpolis", data.get(0));
		return new ModelAndView("rekruitment/viewDataTSR", map);
	}
	
	public ModelAndView uploadDataTSR(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");		
		HashMap<String, Object>  map = new HashMap<String, Object> ();
		SimpleDateFormat dt = new SimpleDateFormat("MM/yyyy");
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String tabs="1";
		
		String directory=null;
		String directory1=null;
		String submit = request.getParameter("upload");
		
		///untuk tab 1(bank sinarmas)
		List<DropDown> dist = new ArrayList<DropDown>();	
		dist.add(new DropDown("0","PILIH DISTRIBUSI"));
		dist.add(new DropDown("1","DMTM"));
		dist.add(new DropDown("2","MALLASSURANCE"));	
		String distribusi =  ServletRequestUtils.getStringParameter(request, "dist",null);
		
		///untuk tab 2(bank sinarmas)
		List<DropDown> dist1 = new ArrayList<DropDown>();	
		dist1.add(new DropDown("0","PILIH DISTRIBUSI"));
		dist1.add(new DropDown("1","DMTM"));
		dist1.add(new DropDown("2","MALLASSURANCE"));
		map.put("tgl", dt.format(elionsManager.selectSysdate()));
		String distribusi1 =  ServletRequestUtils.getStringParameter(request, "dist1",null);
		
		
		if(submit != null){	//upload
			tabs="1";
			Upload upload = new Upload();
			//String file_fp = request.getParameter("file_fp");
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			MultipartFile mf = upload.getFile1();
			String fileName = df.format(elionsManager.selectSysdate())+"_"+mf.getOriginalFilename();	//				
			if(distribusi.equalsIgnoreCase("1")){
				directory = props.getProperty("pdf.surat.tsr_dmtm_save");
			}else if(distribusi.equalsIgnoreCase("2")){
				directory = props.getProperty("pdf.surat.tsr_mall_save");
			}
			String dest=directory+"/"+fileName;
			File outputFile = new File(dest);	
			FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
		}
		
		if(request.getParameter("search") != null) {
			tabs="2";
			String bulan = ServletRequestUtils.getStringParameter(request, "bulan","");
			ArrayList dok = new ArrayList();
			String bulan2 = bulan.replace("/", "");
			bulan2=bulan2.substring(2, 6)+bulan2.substring(0, 2);
			if(distribusi1.equalsIgnoreCase("1")){
				directory = props.getProperty("pdf.surat.tsr_dmtm_save");
			}else if(distribusi1.equalsIgnoreCase("2")){
				directory = props.getProperty("pdf.surat.tsr_mall_save");
			}
			
			List<DropDown> dokumen =com.ekalife.utils.FileUtils.listFilesInDirectory(directory);										
			if(!dokumen.isEmpty()){
				for(int i=0;i<dokumen.size();i++){
						DropDown dc = dokumen.get(i);
						HashMap<String, Object>  m = new HashMap<String, Object> ();	
						String bln = dc.getKey().substring(0,6);
						if(bulan2.equals(bln)){
							m.put("dok", dc.getKey());
							m.put("tgldok", dc.getValue());
							dok.add(m);	
						}
						if(dok.isEmpty())map.put("pesan", "Dokumen tidak ada");				
					}
				}else{
					map.put("pesan", "Dokumen tidak ada");	
				}
				
			map.put("jp", distribusi1);
			map.put("dokumen", dok);
		    map.put("tgl", bulan);	
		    map.put("showTab", tabs);
		}
		
		if((request.getParameter("file")) != null) {			
			String file =  ServletRequestUtils.getStringParameter(request, "file",null);
			String dist2= ServletRequestUtils.getStringParameter(request, "product",null);
			String tabs1= ServletRequestUtils.getStringParameter(request, "tab",null);
		
			if(dist2.equalsIgnoreCase("1")){
				directory1 = props.getProperty("pdf.surat.tsr_dmtm_save");
			}else{
				directory1 = props.getProperty("pdf.surat.tsr_mall_save");
			}
			
			com.ekalife.utils.FileUtils.downloadFile("inline;", directory1, file, response);
		}
		map.put("dist", dist);
		map.put("dist1", dist1);
		map.put("showTab", tabs);
		return new ModelAndView("rekruitment/uploadTSR", map);
		

	}
	
	public ModelAndView upload_berkas(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		
		String no_reg = ServletRequestUtils.getStringParameter(request, "no_reg",null);
		Kuesioner data = this.elionsManager.selectkuesioner(no_reg);
		map.put("no_reg", no_reg);
		List lscan = this.bacManager.selectLstScan("AD", null);
		if(data.getPosisi().equals("1")){
			lscan.remove(lscan.get(8));
			lscan.remove(lscan.get(7));
		}else if(data.getPosisi().equals("3")){
			lscan.remove(lscan.get(0));
			lscan.remove(lscan.get(0));
			lscan.remove(lscan.get(0));
			lscan.remove(lscan.get(0));
			lscan.remove(lscan.get(0));
			lscan.remove(lscan.get(0));
			lscan.remove(lscan.get(0));
		}
		map.put("listUpload", lscan);
		
		if(!Common.isEmpty(request.getParameter("upload"))){
			Upload upload = new Upload();
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			MultipartFile mf = upload.getFile1();
			String fileName = mf.getOriginalFilename();
			String format = fileName.substring(fileName.length()-3, fileName.length()).toUpperCase();
			
			String id = ServletRequestUtils.getStringParameter(request, "listUpload");
			List scan = this.bacManager.selectLstScan("AD", id);
			String nmfile = null;
			for(int i=0; i<scan.size();i++){
				Map a_ni = (HashMap) scan.get(i);
				nmfile = (String) a_ni.get("NMFILE");
			}
			if(format.equals("PDF")){
				//destiny upload file
				String tDest = props.getProperty("pdf.dir.arthamas.dokumenRegisterAgen");
				File destDir = new File(tDest+"\\"+no_reg);
				if(!destDir.exists()) destDir.mkdirs();
				File[] fList = destDir.listFiles();
				if(fList.length>0){
					int pre = 0;
					for(File file : fList){
						String fn = file.getName();
						String part[] = fn.split("_");
						String bag0 = part[0];
//						String bag1 = part[1];
						String bag1 = "";
						if (part.length>1 ){
							bag1 = part[1];
							String nm = bag1.substring(0, bag1.length()-4).toUpperCase();
							if(bag0.matches(nmfile)){
								int n_nm = Integer.valueOf(nm);
								if(n_nm>pre){
									pre = n_nm;
								}
							}
						}

					}
					if(pre>0){
						pre += 1;
						fileName = nmfile+"_"+FormatString.rpad("0", String.valueOf(pre), 3)+".PDF";
					}else{
						fileName = nmfile+"_001.PDF";
					}
				}else{
					fileName = nmfile+"_001.PDF";
				}
				destDir = new File(destDir+"\\"+fileName);
				FileUtils.writeByteArrayToFile(destDir, mf.getBytes());
				String desc = "Upload File - "+fileName;
				this.bacManager.insert_mstkuesioner_hist(no_reg, desc, "1", currentUser.getLus_id());
				map.put("pesan", "File berhasil di upload");
			}else{
				map.put("pesan", "Format file bukan PDF");
			}
		}
		return new ModelAndView("rekruitment/upload_berkas", map);
	}
	
	public ModelAndView rekrut_tools(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String pesan = ServletRequestUtils.getStringParameter(request, "pesan");
		Map map = new HashMap();
		map.put("pesan",pesan);
		map.put("data", this.bacManager.selectkuesionerBy(currentUser.getLus_id(),"1",null));
		return new ModelAndView("rekruitment/rekrut_tools", map);
	}
	
	public ModelAndView edit_calonaaji(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Kuesioner data = new Kuesioner();
		String pesan = ServletRequestUtils.getStringParameter(request, "pesan");
		String mku_no_reg = ServletRequestUtils.getStringParameter(request, "mku_no_reg", null);
		Map map = new HashMap();
		if(mku_no_reg==null){
			map.put("hasil", null);
		}else{
			map.put("hasil", elionsManager.selectkuesioner(mku_no_reg));
		}
//		map.put("hasil", elionsManager.selectkuesioner(mku_no_reg));
		map.put("lst_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "", "lsag_id", null));
		map.put("lst_propinsi", elionsManager.selectDropDown("eka.lst_propinsi", "lspr_id", "lspr_note", "", "lspr_id", null));
		map.put("select_bank", uwManager.selectlsBank());
		if(currentUser.getMsag_id_ao()!=null){
			map.put("jenis_rekrut",DroplistManager.getInstance().get("JENIS_REKRUT_AGEN.xml","ID",request));
		}else{
			map.put("jenis_rekrut",DroplistManager.getInstance().get("JENIS_REKRUT.xml","ID",request));
		}
		
		if(request.getParameter("edit")!=null){
			ServletRequestDataBinder binder = new ServletRequestDataBinder(data);
			binder.bind(request);
			this.uwManager.updateKuesioner(data);
			this.bacManager.insert_mstkuesioner_hist(data.getMku_no_reg(), "Update data calon agen", data.getPosisi(), currentUser.getLus_id());
			map.put("pesan", "Data berhasil di update");
		}else if(request.getParameter("cari")!=null){
			String mku_noreg = ServletRequestUtils.getStringParameter(request, "mku_noreg", null);
			map.put("hasil", elionsManager.selectkuesioner(mku_noreg));
		}
		
		return new ModelAndView("rekruitment/rekrut_edit", map);
	}
	
	public ModelAndView ajax(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date now = bacManager.selectSysdate();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		List array = new ArrayList();
		Map m = new HashMap();
		Object result = null;
		
		String pg = ServletRequestUtils.getStringParameter(request, "pages", null);
		String jn = ServletRequestUtils.getStringParameter(request, "jn", null);
		
		if("rekrut_tools".equals(pg)){
			if("showhist".equals(jn)){
				array = new ArrayList();
				String no_reg = ServletRequestUtils.getStringParameter(request, "no_reg",null);
				List x = this.bacManager.selectMstKuesioner_hist(no_reg);
				if(x.size()>0){
					for(int i=0;i<x.size();i++){
						Map mp = (HashMap) x.get(i);
						Map cm = new HashMap();
						
						Date tgl_input = (Date) mp.get("MKH_DATE");
						String desc = (String) mp.get("MKH_DESC");
						String pos = (String) mp.get("POSISI");
						String lus_id = (String) mp.get("NAMA");
						String dept = (String) mp.get("DEPT");
						
						cm.put("tgl", df.format(tgl_input));
						cm.put("ket", desc);
						cm.put("pos", pos);
						cm.put("user", lus_id);
						cm.put("dept", dept);
						
						array.add(cm);
					}
					m.put("history",array);
				}else{
					m.put("pesan", "History tidak ditemukan");
				}
				result = m;
			}
			else if("carinoreg".equals(jn)){
				String no_reg = ServletRequestUtils.getStringParameter(request, "no_reg",null);
				no_reg.replace("-", "");
				List x = this.bacManager.selectkuesionerBy(null, "1", no_reg);
				for(int i=0;i<x.size();i++){
					Map mp = (HashMap) x.get(i);
					Map cm = new HashMap();
					
					String n_reg = (String) mp.get("MKU_NO_REG");
					String nama = (String) mp.get("MKU_FIRST");
					
					cm.put("no_reg", n_reg);
					cm.put("nama", nama);
					
					array.add(cm);
				}
				m.put("noreg", array);
				result = m;
			}
			else if("carilistnoreg".equals(jn)){
				List x = this.bacManager.selectkuesionerBy(currentUser.getLus_id(), "1", null);
				m.put("data", x);
				result = m;
			}
			else if("transfer".equals(jn)){
				String no_reg = ServletRequestUtils.getStringParameter(request, "no_reg",null);
				Kuesioner data = this.elionsManager.selectkuesioner(no_reg);
				String[] to = null;
				String[] cc = null;
				String message = null, subject = null;
				int error = 0;
				to = new String[]{"yolanda@sinarmasmsiglife.co.id","monica@sinarmasmsiglife.co.id","dianverdana@sinarmasmsiglife.co.id"};
				cc = new String[]{"tedi@sinarmasmsiglife.co.id"};
				List dataX = this.bacManager.selectkuesionerBy(data.getLus_id().toString(), "0", no_reg);
				String pesan = "null aja";
				String regional = null;
				for(int i=0;i<dataX.size();i++){
					Map x = (HashMap) dataX.get(i);
					
					regional = (String) x.get("REGIONAL");
				}
				if(data.getPosisi().equals("1")){
					String[] nmfile = {"AGFORM_001.pdf","KTP_001.pdf","FOTO_001.pdf","AGDEC_001.pdf","ACC_001.pdf","AGAGREE_001.pdf"};
					for(int i=0;i<nmfile.length;i++){
						String s_path = props.getProperty("pdf.dir.arthamas.dokumenRegisterAgen").trim() + "\\"+no_reg.trim()+"\\"+nmfile[i];
						File file = new File(s_path);
						if(!file.exists()) error = 1;
						else error = 0;
					}
					if(error==1){
						m.put("pesan", "Mohon lengkapi upload 6 dokumen wajib.\n- Formulir Keagenan\n- KTP\n- Pas Photo\n- Deklarasi Kode Etik (AGENT DECLARATION)\n- Rekening Sinarmas (ACCOUNT STATEMENT)\n- Kontrak Keagenan (AGENT AGREEMENT)");
					}else if(error==0){
						data.setPosisi("2");
						this.uwManager.updateKuesioner(data);
						this.bacManager.insert_mstkuesioner_hist(no_reg, "Transfer calon agent ke Distribution Support", "1", currentUser.getLus_id());
						
						subject = "Rekrut Calon Agen";
						message = "Telah direkrut agen baru dengan nomor register : "+no_reg+
								"<br/>Nama : "+data.getMku_first()+"<br/>Region : "+regional+"<br/><br/>Terimakasih.";
						m.put("pesan", "Calon agen sudah di transfer");
						pesan = "Calon agen sudah di transfer";
					}
				}else if (data.getPosisi().equals("3")){
					data.setPosisi("4");
					error = 0;
					this.uwManager.updateKuesioner(data);
					subject = "Other Upload";
					message = "Telah dilakukan upload dokumen agen baru dengan nomor register : "+no_reg+
							"<br/>Nama : "+data.getMku_first()+"<br/>Region : "+regional+
							"<br/>Keterangan : Bukti setor Bank Ujian Lisensi<br/><br/>Terimakasih.";
					m.put("pesan", "Email BSB sudah dikirim");
					this.bacManager.insert_mstkuesioner_hist(no_reg, "Transfer ke generate agent code", "3", currentUser.getLus_id());
				}
				if(error==0){
					try {
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 
								Integer.valueOf(currentUser.getLus_id()), 
								bacManager.selectSysdate(), null, true, 
								currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava"), to, cc, null, 
								subject, message, null, null);
					} catch (MailException e) {
						logger.error("ERROR :", e);
					}
				}
				
				result = m;
			}
			
		}else if("rekrut_edit".equals(pg)){
			if("mst_leader".equals(jn)){
				int kodepil = ServletRequestUtils.getIntParameter(request, "kodepil", 0);
				String kode = ServletRequestUtils.getStringParameter(request, "kode");
				m.put("data_leader", ajaxManager.agen_rekruter_detil(kode, kodepil));
			}
			
			result = m;
		}
		
		
		if(result != null){
			out.print(gson.toJson(result));
			out.close();
		}
		return null;
	}
	
	
}
