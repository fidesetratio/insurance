package com.ekalife.elions.web.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.mail.MailException;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.AksesHist;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.FileItem;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.saveseries.Result;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.Telpon;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentMultiController;
import com.google.gson.Gson;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.DateUtil;

/**
 * @author Yusuf
 *
 */
public class MainMultiController extends ParentMultiController {

	/**
	 * Report Save Series, Requested by Iwen
	 * @param request
	 * @param response
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ServletRequestBindingException
	 * Filename : MainMultiController.java
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : Mar 5, 2010 4:56:39 PM
	 */
	public ModelAndView laporan_save_series(HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, IOException, ServletRequestBindingException{
		Map map = new HashMap();
		map.put("sysDate", DateUtil.toString(elionsManager.selectSysdate()));
		String bdate=ServletRequestUtils.getStringParameter(request, "begDate");
//		String edate=ServletRequestUtils.getStringParameter(request, "endDate");
		
		
		
		if(request.getParameter("show") != null){
			Date begdate=null;
//			Date enddate=null;
			try {
				begdate = new SimpleDateFormat("dd/MM/yyyy").parse(bdate);
//				enddate = new SimpleDateFormat("dd/MM/yyyy").parse(edate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			}
			List<Result> lsHasil = uwManager.selectLaporanSaveSeries(bdate, null);
			map.put("lsHasil", lsHasil);
			map.put("begDate", begdate);
//			map.put("endDate", enddate);
			return new ModelAndView("xlsCreatorReportSaveSeries", "map", map);
		}else if(request.getParameter("attach") != null){
			Date begdate=null;
//			Date enddate=null;
			try {
				begdate = new SimpleDateFormat("dd/MM/yyyy").parse(bdate);
//				enddate = new SimpleDateFormat("dd/MM/yyyy").parse(edate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			}
			List<Result> lsHasil = uwManager.selectLaporanSaveSeries(bdate, null);
			map.put("lsHasil", lsHasil);
			map.put("begDate", begdate);
//			map.put("endDate", enddate);
			map.put("attach", 1);
			return new ModelAndView("xlsCreatorReportSaveSeries", "map", map);
		}
		
		return new ModelAndView("common/laporan_save_series", map);
	}
	
	public ModelAndView laporan_bii_bsm(HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, IOException, ServletRequestBindingException{
		Map map = new HashMap();
		map.put("sysDate", DateUtil.toString(elionsManager.selectSysdate()));
//		map.put("sysDate", "01/01/2010");
		
		map.put("sysDate1", "31/01/2010");
		String bdate=ServletRequestUtils.getStringParameter(request, "begDate");
//		String edate=ServletRequestUtils.getStringParameter(request, "endDate");
		String attach=ServletRequestUtils.getStringParameter(request, "attach");
		
		String[]bancassGroup=new String[]{"BII","BANK SINARMAS"};
		
		if(request.getParameter("show") != null){
			Date begdate=null;
			Date enddate=null;
			try {
				begdate = new SimpleDateFormat("dd/MM/yyyy").parse(bdate);
//				enddate = new SimpleDateFormat("dd/MM/yyyy").parse(edate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			}
			List<List<Result>>lsHasil=new ArrayList<List<Result>>();
			for(String bank:bancassGroup){
				lsHasil.add((List<Result>)uwManager.selectLaporanBIIBSM(bdate, null,bank));
			}
			map.put("lsHasil", lsHasil);
			map.put("begDate", begdate);
			map.put("endDate", enddate);
			return new ModelAndView("xlsCreatorReportBIIBSM", "map", map);
		}else if(request.getParameter("attach") != null){
			Date begdate=null;
			Date enddate=null;
			try {
				begdate = new SimpleDateFormat("dd/MM/yyyy").parse(bdate);
//				enddate = new SimpleDateFormat("dd/MM/yyyy").parse(edate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			}
			List<List<Result>>lsHasil=new ArrayList<List<Result>>();
			for(String bank:bancassGroup){
				lsHasil.add((List<Result>)uwManager.selectLaporanBIIBSM(bdate, null,bank));
			}
			map.put("lsHasil", lsHasil);
			map.put("begDate", begdate);
			map.put("endDate", enddate);
			map.put("attach", 1);
			return new ModelAndView("xlsCreatorReportBIIBSM", "map", map);
		}
		
		return new ModelAndView("common/laporan_bii_bsm", map);
	}

	public ModelAndView support(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		
		Map map = new HashMap();
		
		if(request.getParameter("send") != null) {
			String pesan = "";
			try{
				String[] to = new String[] {props.getProperty("admin."+ServletRequestUtils.getRequiredStringParameter(request, "to"))};
				email.send(false, props.getProperty("admin.ajsjava"), to, null, null, 
						ServletRequestUtils.getRequiredStringParameter(request, "subject"), 
						"Pesan dikirim oleh : " + currentUser.getName() + " ["+currentUser.getDept()+"]\n\n" +					
						ServletRequestUtils.getRequiredStringParameter(request, "message"),
						null);
				pesan = "Pesan berhasil dikirim";
			}
			catch(MailException ex) {
				pesan = "Pesan Gagal dikirim. Silahkan ulangi kembali";
				logger.error("ERROR :", ex);
			} catch (MessagingException e) {
				pesan = "Pesan Gagal dikirim. Silahkan ulangi kembali";
				logger.error("ERROR :", e);
			}
			
			if(!pesan.equals("")) map.put("pepesan", pesan);
		}
		
		return new ModelAndView("common/support", map);
	}

	public ModelAndView motd(HttpServletRequest request, HttpServletResponse response)throws ServletRequestBindingException{
		if(request.getParameter("simpan") != null){
			String tanggal = ServletRequestUtils.getStringParameter(request, "tanggal");
			String pesan = ServletRequestUtils.getStringParameter(request, "pesan");
			User currentUser = (User)request.getSession().getAttribute("currentUser");
			
			try {
				uwManager.saveMessageOfTheDay(tanggal, pesan, currentUser.getLus_id());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e);
			}
		}
		
		Map m = new HashMap();
		m.put("listMotd", uwManager.selectLast10MessageOfTheDay());
		m.put("sysDate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
		
		return new ModelAndView("common/motd", m);
	}

	public ModelAndView uat(HttpServletRequest request, HttpServletResponse response)throws ServletRequestBindingException{
		return new ModelAndView("common/uat");
	}

	public ModelAndView download_per_user(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, IOException{
		Map m = new HashMap();
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		int json = ServletRequestUtils.getIntParameter(request, "json", 0);
		
		//user menekan tombol download file
		if(request.getParameter("download") != null){
			String fileName = request.getParameter("fileName");
			String fileDir = request.getParameter("fileDir");
			FileUtils.downloadFile("attachment;", fileDir, fileName, response);
			return null;
			
		//load halaman
		}else if(json == 0){
			String folder = currentUser.getCabang().toUpperCase().trim() + "_" + currentUser.getName().toUpperCase().trim();
			//1. create folder dulu, sapa tau blom ada
			String destDir = props.getProperty("upload.dir.per_user2") + "/" + folder;
			File dir = new File(destDir);
			if(!dir.exists()){
				dir.mkdirs();
			}
			dir = null;
			
			//2. baru list isinya
			destDir = props.getProperty("upload.dir.per_user") + "/" + folder;
			
			//3. taruh ke command object
			m.put("listFolder", FileUtils.listDirectories(destDir));
			return new ModelAndView("common/download_per_user", m);
			
		//tarik data json untuk ajax
		}else if(json == 1){
    		String folder = ServletRequestUtils.getStringParameter(request, "folder");
    		List<DropDown> result = FileUtils.listDirectories(folder);
    		
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
			
   		//tarik data json untuk ajax
		}else if(json == 2){
    		String folder = ServletRequestUtils.getStringParameter(request, "folder");
    		List<DropDown> result = FileUtils.listFilesInDirectory(folder);
    		
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
			
		}
		
		return null;
	}
	
	public ModelAndView treemenu(HttpServletRequest request, HttpServletResponse response)throws ServletRequestBindingException, FileNotFoundException, IOException{
		HttpSession session = request.getSession();
		
		User currentUser = (User)session.getAttribute("currentUser");
		if(currentUser!=null) {
			
			//bagian update email
			String new_email = ServletRequestUtils.getStringParameter(request, "new_email", "");
			if(!new_email.trim().equals("")){
				uwManager.updateEmail(currentUser.getLus_id(), new_email);
				currentUser.setEmail(new_email);
			}
			//end of bagian update email
				
			if(request.getParameter("fileName") != null) {
				String fileName = request.getParameter("fileName");
				String fileDir = request.getParameter("fileDir");
				FileUtils.downloadFile("attachment;", fileDir, fileName, response);
				return null;
			}else {
				List addr = this.elionsManager.selectUserAdmin(Integer.parseInt(currentUser.getLus_id()));
				List fileListAgency = new ArrayList();
				List fileListKalender = new ArrayList();
				List fileListCabangAgency = new ArrayList();
				List fileListCabangAKM = new ArrayList();
				List fileListCabangBancass = new ArrayList();
				
				List fileListTreeMenu = new ArrayList();

				Map map = new HashMap();
				Map leveling = new HashMap();

				//File List upload dari mba Tri
				for(int i=0; i<addr.size(); i++) {
					//upload agency
					String destDir = props.getProperty("upload.dir")+"/"+FormatString.rpad("0", addr.get(i).toString(), 3);
					fileListAgency.addAll(FileUtils.listFilesInDirectory(destDir));
				}
				//map.put("fileListAgency", fileListAgency);
				
				if(fileListAgency.size() != 0) {
					leveling = new HashMap();
					leveling.put("level", 1);
					leveling.put("data", "File Agency");
					fileListTreeMenu.add(leveling);
					leveling = new HashMap();
					leveling.put("level", 2);
					leveling.put("data", fileListAgency);
					fileListTreeMenu.add(leveling);						
				}

				//kalau user cabang
				if(!(currentUser.getLca_id().equals("01") || currentUser.getLca_id().equals("09") ||  currentUser.getLca_id().equals("58")) || currentUser.getLde_id().equals("29")){

					//File List untuk distribusi kalender oleh BAS (\\ebserver\download\per_user) , hanya kalau user admin cabang (LST_USER.LUS_BAS = 1)
					//Yusuf - 13/10/2010 - Req By Arief
					//Yusuf - 14/12/2011 - dipindah ke menu download_per_user, agar tidak terlalu lama loadingnya
//					if(currentUser.getLus_bas().intValue() == 1){
//						String folder = currentUser.getCabang().toUpperCase().trim() + "_" + currentUser.getName().toUpperCase().trim();
//						//1. create folder dulu, sapa tau blom ada
//						String destDir = props.getProperty("upload.dir.per_user2") + "/" + folder;
//						File dir = new File(destDir);
//						if(!dir.exists()){
//							dir.mkdirs();
//						}
//						dir = null;
//						//2. baru list isinya
//						destDir = props.getProperty("upload.dir.per_user") + "/" + folder;
//						//fileListKalender.addAll(FileUtils.listFilesInDirectory(destDir));
//						fileListKalender.addAll(FileUtils.listFilesInDirectoryRecursive(new File(destDir)));
//						fileListKalender = bubbleSort(fileListKalender);
//						//map.put("fileListPerUser", fileListKalender);
//						
//						if(fileListKalender.size() != 0) {
//							leveling = new HashMap();
//							leveling.put("level", 1);
//							leveling.put("data", "File Per User");
//							fileListTreeMenu.add(leveling);
//							//leveling = new HashMap();
//							//leveling.put("level", 2);
//							//leveling.put("data", fileListKalender);
//							//fileListTreeMenu.add(leveling);	
//							fileListTreeMenu.addAll(reconstructList(fileListKalender));
//						}
//					}
					
					//File List untuk distribusi file2 untuk cabang (\\ebserver\download), dipecah 2, tergantung user cabang agency / AKM
					if(currentUser.getLde_id().equals("29") || currentUser.getFlag_akm().intValue() == 0){
						String folderDownload = props.getProperty("download.folder") + "\\download_agency";
						fileListCabangAgency.addAll(FileUtils.listFilesInDirectoryRecursive(new File(folderDownload)));
						fileListCabangAgency = bubbleSort(fileListCabangAgency);
						//map.put("fileListCabangAgency", fileListCabangAgency);
						
						leveling = new HashMap();
						leveling.put("level", 1);
						leveling.put("data", "File Cabang AGENCY");
						fileListTreeMenu.add(leveling);
						fileListTreeMenu.addAll(reconstructList(fileListCabangAgency));
					}
					if(currentUser.getLde_id().equals("29") || currentUser.getFlag_akm().intValue() == 1){
						String folderDownload = props.getProperty("download.folder") + "\\download_akm";
						fileListCabangAKM.addAll(FileUtils.listFilesInDirectoryRecursive(new File(folderDownload)));
						fileListCabangAKM = bubbleSort(fileListCabangAKM);
						//map.put("fileListCabangAKM", fileListCabangAKM);
						
						leveling = new HashMap();
						leveling.put("level", 1);
						leveling.put("data", "File Cabang AKM");
						fileListTreeMenu.add(leveling);
						fileListTreeMenu.addAll(reconstructList(fileListCabangAKM));
					}
				}
				
				//Yusuf (19/10/2010) - Request Hendra BAS - Khusus user2 bancass yang paling banyak maunya 
				int lus_id = Integer.parseInt(currentUser.getLus_id());
				if(lus_id == 672 || lus_id == 1056 || lus_id == 39 || lus_id == 1393){
					String folderDownload = props.getProperty("download.folder") + "/" + "Bancassurance";
					fileListCabangBancass.addAll(FileUtils.listFilesInDirectoryRecursive(new File(folderDownload)));
					fileListCabangBancass = bubbleSort(fileListCabangBancass);
					//map.put("fileListCabangBancass", fileListCabangBancass);
					
					leveling = new HashMap();
					leveling.put("level", 1);
					leveling.put("data", "File Bancassurance");
					fileListTreeMenu.add(leveling);
					fileListTreeMenu.addAll(reconstructList(fileListCabangBancass));					
				}
				
				//Yusuf - Aug 2010 - Download List untuk file2 tambahan lainnya
				List<DropDown> fileList = new ArrayList<DropDown>();
				fileList.add(new DropDown("Download Print Pdf Installer", 	"include/installer/pdfdrv250.exe", 			"Untuk print file apapun menjadi PDF"));
				fileList.add(new DropDown("Download Acrobat Reader", 		"include/installer/AdbeRdr90_en_US.exe",	"Untuk membaca file PDF"));
				fileList.add(new DropDown("Download Absensi", 				"include/download/SetupAbsen.zip", 			"Untuk program absensi cabang"));
				fileList.add(new DropDown("Download Remote Admin", 			"include/installer/RADMIN20.EXE", 			"Untuk Remote Admin"));
				fileList.add(new DropDown("Download 7Zip Compression", 		"include/installer/7z465.exe", 				"Untuk membuka file ZIP"));
				fileList.add(new DropDown("Download Flash Player (Non IE)", "include/installer/install_flash_player(NonIE).exe", 				"Plugin Untuk menjalankan flash di browser Non IE (Internet Explorer)"));
				fileList.add(new DropDown("Download Flash Player (IE)", 	"include/installer/install_flash_player(IE).exe", 				"Plugin Untuk menjalankan flash di browser IE (Internet Explorer)"));
				fileList.add(new DropDown("Download Amyuni 25 Win XP", 		"include/installer/amyuni25winxp.zip", 				"Untuk menconvert file dokumen ke pdf"));
				fileList.add(new DropDown("Download Team Viewer", 			"include/installer/TeamViewer_Setup.exe", 				"Untuk remote"));
				fileList.add(new DropDown("Download install Software LOMA ", 			"include/installer/IStarClient.exe", 				"Untuk install Software LOMA "));
				fileList.add(new DropDown("Download Manual Guide install Software LOMA", 			"include/installer/ManualIStarLOMA.doc", 				"Manual Guide \" install Software LOMA \"."));
				fileList.add(new DropDown("Download Team ViewerQS", 			"include/installer/TeamViewerQS_id.zip", 				"Untuk remote"));
				//map.put("fileList", fileList);
				
				leveling = new HashMap();
				leveling.put("level", 1);
				leveling.put("data", "File Lainnya");
				fileListTreeMenu.add(leveling);
				leveling = new HashMap();
				leveling.put("level", 2);
				leveling.put("data", fileList);
				fileListTreeMenu.add(leveling);	
				
				
				//Yusuf - Aug 2010 - Daftar Telpon
				map.put("extList", Telpon.daftarExtension());
				
				map.put("currentUser", currentUser);
				map.put("allMenu", this.elionsManager.selectTreeMenu(ServletRequestUtils.getIntParameter(request, "jenis", 13), Integer.parseInt(currentUser.getLus_id())));
				
				//1. informasi tgl closing
				if("01".equals(currentUser.getLca_id())) {
					map.put("closing", elionsManager.selectClosingDate());
				}
				
				//2. informasi summary new business
				//TODO : mau dipake gak? query nya agak lambat
				/*
				if(currentUser.getLde_id().equals("01") || currentUser.getLde_id().equals("11")){
					map.put("summaryNB", uwManager.selectSummaryAllNewBusiness());
				}
				*/
				
				//3. informasi rate bank sinarmas + list pending
				if(currentUser.getJn_bank().intValue() == 2){
					List<Map> rateBSM = uwManager.selectRateBankSinarmas();
					List<Map> currentRateBSM = new ArrayList<Map>();
					List<Map> futureRateBSM = new ArrayList<Map>();
					for(Map m : rateBSM){
						int tipe = ((BigDecimal) m.get("TIPE")).intValue();
						if(tipe == 1) currentRateBSM.add(m);
						else futureRateBSM.add(m);
					}
					map.put("currentRateBSM", currentRateBSM);
					map.put("futureRateBSM", futureRateBSM);
					
					map.put("listPending", uwManager.selectPendingBSM(currentUser.getCab_bank()));
				}
				
				//4. informasi update proposal terbaru, yg bisa liat admin cabang
				//info ini ada di 2 tempat:
				//- com.ekalife.elions.web.common.MainMultiController.treemenu
				//- com.ekalife.elions.web.common.UpdateMultiController.proposal
				if(! "01, 09".contains(currentUser.getLca_id()) ){
					String dir = props.getProperty("upload.proposal");
					List<DropDown> daftarFile = FileUtils.listFilesInDirectory(dir);
					
					for(DropDown d : daftarFile) {
						if(d.getKey().toLowerCase().startsWith("upas_")) {
							d.setDesc("Proposal (Agency)");
						}else if(d.getKey().toLowerCase().startsWith("update_absen")) {
							d.setDesc("Absensi");
						}else if(d.getKey().toLowerCase().startsWith("update_")) {
							d.setDesc("Proposal (Regional)");
						}else if(d.getKey().toLowerCase().startsWith("upfa_")) {
							d.setDesc("Proposal Bancassurance");
						}else {
							d.setDesc("");
						}
					}
					map.put("daftarUpdateProposal", daftarFile);

					//5. Yusuf (25 Jan 2010) - Req Arief BAS - ada message of the day yg ditampilkan untuk user
					map.put("messageOfTheDay", uwManager.selectMessageOfTheDay());
				}
				
				//Canpri 13 Sep 2013 Rate Agency permintaan Martino
				List<DropDown> fileListRate = new ArrayList<DropDown>();
				List f_rate = FileUtils.listFilesInDirectory(props.getProperty("download.folder")+"\\rate_agency");
				for(int i=0;i<f_rate.size();i++){
					DropDown rt = (DropDown) f_rate.get(i);
//					String rate_dir = "\\\\\\\\ebserver\\\\download\\\\rate_agency\\\\";
					String rate_dir = props.getProperty("download.folder") + "\\rate_agency\\";
					fileListRate.add(new DropDown(rt.getKey(), rt.getValue(), rate_dir));
				}
				
				leveling = new HashMap();
				leveling.put("level", 1);
				leveling.put("data", "File Rate Agency");
				fileListTreeMenu.add(leveling);
				leveling = new HashMap();
				leveling.put("level", 2);
				leveling.put("data", fileListRate);
				fileListTreeMenu.add(leveling);	
				//
				
				map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));

				map.put("reimender", elionsManager.selectReimenderInvoicePerusahaan()); // hanya u/ novie dan tities
				
				map.put("treeMenu", constructTreeMenu(fileListTreeMenu,request.getContextPath()));
				
				return new ModelAndView("common/treemenu", "cmd", map);
			}
		}else {
			return new ModelAndView(new RedirectView(request.getContextPath()+"/include/page/blocked.jsp?jenis=redirect"));
		}
	}
	
	/**
	 * Buat tampilan list html :
	 * 
	 * <ul id="imageTree">
	 * 	<li>level 1.1</li>
	 *  <li>level 1.2
	 *  	<ul>
	 *  		<li>level 1.2.1</li>
	 *  		<li>level 1.2.2
	 *  			<ul>
	 *  				<li>level 1.2.2.1</li>
	 *  			</ul>
	 *  		</li>
	 *  	</ul>
	 *  </li>
	 * </ul>
	 * @param data : arraylist berisi data yg dipassing
	 * @param uri : request.getContextPath()
	 * @return
	 *
	 * @author yusup_a
	 * @since Jan 4, 2011 (2:41:22 PM)
	 */
	public String constructTreeMenu(List data,String uri) {
		Integer tingkat = 1;
		String menu = "<ul id='imageTree'>"; 
		String parent = "";
		
		for (int a = 0; a < data.size(); a++) {
			Map mapData = (Map) data.get(a);
			if(a != data.size()-1) {
				Map mapDataAfter = (Map) data.get(a+1);
				if(mapData.get("data").getClass().toString().equals("class java.lang.String")) {
					if(Integer.parseInt(mapDataAfter.get("level").toString()) == tingkat) {
						menu += "<li style='padding-top:3px'><b>"+mapData.get("data").toString()+"</b></li>";	
					}
					else if(Integer.parseInt(mapDataAfter.get("level").toString()) == tingkat+1) {
						menu += "<li style='padding-top:3px'><b>"+mapData.get("data").toString()+"</b><ul>";	
						tingkat = Integer.parseInt(mapDataAfter.get("level").toString());	    
					}
					else if(Integer.parseInt(mapDataAfter.get("level").toString()) < tingkat) {
						menu += "<li style='padding-top:3px'><b>"+mapData.get("data").toString()+"</b></li>";
						for(int  b=0;b<tingkat-Integer.parseInt(mapDataAfter.get("level").toString());b++) {
							menu += "</ul></li>";
						}
						tingkat = Integer.parseInt(mapDataAfter.get("level").toString());	    
					}					
					parent = mapData.get("data").toString();
				}
				else {
					List listData =  (List) mapData.get("data");
					for(int c=0;c<listData.size();c++) {
						if(c != listData.size()-1) {
							if(listData.get(c).getClass().toString().equals("class id.co.sinarmaslife.std.model.vo.DropDown")) {
								DropDown dd = (DropDown) listData.get(c);
								if(!parent.equals("File Lainnya"))
									menu += "<li><a href='#' onclick='document.formpost.fileName.value=\""+dd.getKey()+"\";document.formpost.fileDir.value=\""+dd.getDesc()+"\";document.formpost.submit();return false;'>"+dd.getKey()+"</a> ["+dd.getValue()+"]</li>";
								else 
									menu += "<li><a href='"+uri+"/"+dd.getValue()+"'>"+dd.getKey()+"</a> ["+dd.getDesc()+"]</li>";
							}
							else if(listData.get(c).getClass().toString().equals("class com.ekalife.elions.model.FileItem")) {
								FileItem fi = (FileItem) listData.get(c);
								menu += "<li><a href='#' onclick='document.formpost.fileName.value=\""+fi.getName()+"\";document.formpost.fileDir.value=\""+fi.getDir()+"\";document.formpost.submit();return false;'>"+fi.getName()+"</a> ["+fi.getDateModified()+"]</li>";
							}							
						}
						else {
							if(listData.get(c).getClass().toString().equals("class id.co.sinarmaslife.std.model.vo.DropDown")) {
								DropDown dd = (DropDown) listData.get(c);
								if(!parent.equals("File Lainnya"))
									menu += "<li><a href='#' onclick='document.formpost.fileName.value=\""+dd.getKey()+"\";document.formpost.fileDir.value=\""+dd.getDesc()+"\";document.formpost.submit();return false;'>"+dd.getKey()+"</a> ["+dd.getValue()+"]</li>";
								else 
									menu += "<li><a href='"+uri+"/"+dd.getValue()+"'>"+dd.getKey()+"</a> ["+dd.getDesc()+"]</li>";
							}
							else if(listData.get(c).getClass().toString().equals("class com.ekalife.elions.model.FileItem")) {
								FileItem fi = (FileItem) listData.get(c);
								menu += "<li><a href='#' onclick='document.formpost.fileName.value=\""+fi.getName()+"\";document.formpost.fileDir.value=\""+fi.getDir()+"\";document.formpost.submit();return false;'>"+fi.getName()+"</a> ["+fi.getDateModified()+"]";
							}
							
							if(Integer.parseInt(mapDataAfter.get("level").toString()) == tingkat) {
								menu += "</li>";	
							}
							else if(Integer.parseInt(mapDataAfter.get("level").toString()) == tingkat+1) {
								menu += "<ul>";	
								tingkat = Integer.parseInt(mapDataAfter.get("level").toString());	    
							}
							else if(Integer.parseInt(mapDataAfter.get("level").toString()) < tingkat) {
								menu += "</li>";
								for(int  b=0;b<tingkat-Integer.parseInt(mapDataAfter.get("level").toString());b++) {
									menu += "</ul></li>";
								}
								tingkat = Integer.parseInt(mapDataAfter.get("level").toString());	    
							}							
						}
					}
				}
			}
			else {
				if(mapData.get("data").getClass().toString().equals("class java.lang.String")) {
					for(int b=0;b<tingkat-Integer.parseInt(mapData.get("level").toString());b++) {
						menu += "</ul></li>";
					}
					menu += "<li style='padding-top:3px'><b>"+mapData.get("data").toString()+"</b></li>";
				}
				else {
					List listData =  (List) mapData.get("data");
					for(int c=0;c<listData.size();c++) {
						if(listData.get(c).getClass().toString().equals("class id.co.sinarmaslife.std.model.vo.DropDown")) {
							DropDown dd = (DropDown) listData.get(c);
							if(!parent.equals("File Lainnya"))
								menu += "<li><a href='#' onclick='document.formpost.fileName.value=\""+dd.getKey()+"\";document.formpost.fileDir.value=\""+dd.getDesc()+"\";document.formpost.submit();return false;'>"+dd.getKey()+"</a> ["+dd.getValue()+"]</li>";
							else 
								menu += "<li><a href='"+uri+"/"+dd.getValue()+"'>"+dd.getKey()+"</a> ["+dd.getDesc()+"]</li>";		
						}
						else if(listData.get(c).getClass().toString().equals("class com.ekalife.elions.model.FileItem")) {
							FileItem fi = (FileItem) listData.get(c);
							menu += "<li><a href='#' onclick='document.formpost.fileName.value=\""+fi.getName()+"\";document.formpost.fileDir.value=\""+fi.getDir()+"\";document.formpost.submit();return false;'>"+fi.getName()+"</a> ["+fi.getDateModified()+"]</li>";
						}
					}
				}				
				
				if(tingkat > 1) {
					for(int b=0;b<tingkat-1;b++) {
						menu += "</ul></li>";
					}
				}
			}
		}
		menu += "</ul>";
		
		return menu;
	}
	
	public List bubbleSort(List data) {
		for(int a=0;a<data.size();a++) {
			for(int b=0;b<data.size()-(a+1);b++) {
				FileItem dataA = (FileItem) data.get(b);
				FileItem dataB = (FileItem) data.get(b+1);
				
				if(dataA.getDesc().compareTo(dataB.getDesc()) > 0) {
					FileItem dataTemp = dataA;
					data.set(b, dataB);
					data.set(b+1, dataTemp);
				}
			}
		}
		
		return data;
	}
	
	public List reconstructList(List data) {
		List list = new ArrayList(), listData = new ArrayList();
		Map leveling = new HashMap();
		String dirBef = "";
		Integer level = 1;
		
		for(int a=0;a<data.size();a++) {
			FileItem fi = (FileItem) data.get(a);
			
			if(!fi.getDesc().equals(dirBef)) {
				if(!dirBef.equals("")) {
					leveling = new HashMap();
					leveling.put("level", level+1);
					leveling.put("data", listData);
					list.add(leveling);
					
					listData = new ArrayList();
				}
				
				// posisi file berada
				if(!fi.getDesc().contains("\\")) {
					leveling = new HashMap();
					if(fi.getDesc().contains(".")) leveling.put("level", level); 
					else leveling.put("level", ++level);
					leveling.put("data", fi.getDesc());
					list.add(leveling);
				}
				else {
					String[] dirTemp = new String[] {};
					FileItem fi2 = null;
					if(a != 0) fi2 = (FileItem) data.get(a-1);
					String[] dir = fi.getDesc().split("\\\\");
					if(fi2 != null) {
						if(!fi2.getDesc().contains("\\")) {
							for(int b=0;b<dir.length;b++) {
								if(!fi2.getDesc().equals(dir[b])) {
									leveling = new HashMap();
									leveling.put("level", ++level);
									leveling.put("data", dir[b]);
									list.add(leveling);
								}
							}
						}
						else {
							dirTemp = fi2.getDesc().split("\\\\");
							for(int b=0;b<dir.length;b++) {
								if(dirTemp.length-1 >= b && dirTemp[b].equals(dir[b])) {
									continue;
								}
								else {
									leveling = new HashMap();
									leveling.put("level", ++level);
									leveling.put("data", dir[b]);
									list.add(leveling);
								}
							}
						}
					}
				}		
				dirBef = fi.getDesc();
			}
			listData.add(fi);
		}
		leveling = new HashMap();
		leveling.put("level", level+1);
		leveling.put("data", listData);
		list.add(leveling);
		
		return list;
	}
	
	/**
	 * Frame Utama E-Lions, disini di-generate dropdown menu untuk semua akses user
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response)throws ServletRequestBindingException{
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		Map map = new HashMap();
		
		String url = request.getRequestURL().toString().toLowerCase();
		if(url.contains("www.sinarmaslife")) map.put("intranet", "http://www.sinarmasmsiglife.com");
		else if(url.contains("www.sinarmasmsiglife")) map.put("intranet", "http://www.sinarmasmsiglife.com");
		else map.put("intranet", "http://intranet");
		
		if(currentUser!=null) {
			String snow_reg_spaj = (String) session.getAttribute("snows_reg_spaj");
			String path_menu = "/common/menu.htm?frame=treemenu";
			if(snow_reg_spaj!=null){
				Integer lspd_id = elionsManager.selectPosisiDocumentBySpaj(snow_reg_spaj);
				if(lspd_id==2){
//					path_menu = "/common/menu.htm?frame=treemenu";
					path_menu ="/uw/uw.htm?window=main&snow_spaj="+snow_reg_spaj+"&posisi_uw=2";
				}else if(lspd_id==4){
					path_menu ="/uw/payment.htm?snow_spaj="+snow_reg_spaj;
				}else if(lspd_id==6){
					path_menu ="/uw/printpolis.htm?window=main&snow_spaj="+snow_reg_spaj;
				}else if(lspd_id==27){
					path_menu ="/uw/uw.htm?window=main&snow_spaj="+snow_reg_spaj+"&posisi_uw=27";
				}else if(lspd_id==7){
					path_menu ="/uw/ttp.htm?window=main&snow_spaj="+snow_reg_spaj;
				}
			}
			map.put("path_menu", path_menu);
			map.put("currentUser", currentUser);
			map.put("menuScript", Common.generateJavascriptMenu(currentUser.getLus_id(), request.getContextPath(), this.elionsManager,path_menu));
			map.put("listKurs", elionsManager.selectKurs());
			return new ModelAndView("common/menu", "cmd", map);
		}else {
			return new ModelAndView(new RedirectView(request.getContextPath()+"/include/page/blocked.jsp?jenis=redirect"));
		}
	}

	/**
	 * TODO : Frame Baru E-Lions, yang lebih enteng + gratis
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView main_new(HttpServletRequest request, HttpServletResponse response)throws ServletRequestBindingException{
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		
		if(currentUser!=null) {
			Map map = new HashMap();
			map.put("currentUser", currentUser);
			map.put("allMenu", this.elionsManager.selectTreeMenu(ServletRequestUtils.getIntParameter(request, "jenis", 1), Integer.parseInt(currentUser.getLus_id())));
			return new ModelAndView("common/menu_new", "cmd", map);
		}else {
			return new ModelAndView(new RedirectView(request.getContextPath()+"/include/page/blocked.jsp?jenis=redirect"));
		}
	}

	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response)throws ServletRequestBindingException{
		User user = (User) request.getSession().getAttribute("currentUser");
		
		if(user!=null) {
			String userName = user.getName();
			Map users = (HashMap) request.getSession().getServletContext().getAttribute("users");
			if(users!=null)
				users.remove(userName);
			request.getSession().getServletContext().setAttribute("users", users);
			request.getSession().removeAttribute("currentUser");
		}
		
		request.getSession().invalidate();
		
		Date sysdate = new Date();
		if(user!=null) {
			AksesHist a = new AksesHist();
			a.setLus_id(Integer.valueOf(user.getLus_id()));
			a.setMsah_date(sysdate);
			a.setMsah_jenis(1);
			a.setMsah_spaj(null);
			a.setMsah_uri("/common/menu.htm?frame=logout");
			elionsManager.insertAksesHist(a);
		}

		String path = request.getContextPath();
		if(path.equals("")) path = "/";
		
		return new ModelAndView(new RedirectView(path));
	}
	
	public ModelAndView cabang(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		String lus = ServletRequestUtils.getStringParameter(request, "lus", "");
		map.put("lus", lus);
		map.put("username", ServletRequestUtils.getStringParameter(request, "username", ""));
		if(request.getParameter("add")!=null) {
			this.elionsManager.saveAllAksesCabang(lus);
		}else if(request.getParameter("save")!=null) {
			this.elionsManager.saveAksesCabang(lus, request.getParameter("nilai").toString().split(","));
		}else if(request.getParameter("remove")!=null) {
			this.elionsManager.deleteAllAksesCabang(lus);
		}
		return new ModelAndView("common/aksescabang", map);
	}

	public ModelAndView changepassword(HttpServletRequest request, HttpServletResponse response){
		Map map = new HashMap();
		
		if(request.getParameter("save")!=null) {
			List errors = new ArrayList();
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			//cek user nul
			String oldp = request.getParameter("oldpassword");
			String newp = request.getParameter("newpassword");
			String confp = request.getParameter("confirmpassword");
			
			if(currentUser==null) {
				errors.add("Harap Login ulang");
			}else if(oldp.trim().equals("") || newp.trim().equals("") || confp.trim().equals("") ) {
				errors.add("Harap isi dengan lengkap");
			}else {
				if(!this.elionsManager.selectPasswordAuthentication(currentUser.getLus_id()).toUpperCase().equals(oldp.toUpperCase())) {
					errors.add("Password lama anda salah");
				}
				if(!newp.equals(confp)) {
					errors.add("Harap samakan New Password dengan Confirm Password");
				}
			}
			
			if(errors.size()==0) {	
				String validatePass="";
				validatePass=Common.isPasswordValid(newp, oldp, null);
				if(!validatePass.isEmpty()){
					errors.add(validatePass);
					map.put("err", errors);
				}else{					
					this.elionsManager.updateLoginPassword(currentUser.getLus_id(), newp);
					currentUser.setPass(newp);
					currentUser.setLus_change(elionsManager.selectSysdate());
					map.put("succ", "Password berhasil dirubah");
					map.put("berhasil","1");
				}
				
					
				
				
			}else{
				map.put("err", errors);
				map.put("oldp", oldp);
				map.put("newp", newp);
				map.put("confp", confp);
			}
			
		}else {
			Integer warning = ServletRequestUtils.getIntParameter(request, "warning", -1);
			Integer flag=ServletRequestUtils.getIntParameter(request, "flag", 0);
			String pesan=ServletRequestUtils.getStringParameter(request, "warn", null);
			if(warning > -1) {
				List errors = new ArrayList();
				errors.add("Harap Ganti Password Default Anda untuk mencegah penyalahgunaan user ID. Terima kasih.");				
				map.put("err", errors);
			}else if(flag>0){
				List errors = new ArrayList();
				errors.add(pesan);
				map.put("err", errors);
			}
			
		}
		
		return new ModelAndView("common/changepassword", map);
	}

	public ModelAndView xml(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String tipe = ServletRequestUtils.getStringParameter(request, "tipe", "");
		
		// (Yusuf) -> Belum bisa digunakan di browser FireFox
		response.setContentType("text/xml");
		
		PrintWriter out = response.getWriter();
		
		if(tipe.equals("otorisasimenu")) { //nebeng fungsi menu otorisasi disini
			this.elionsManager.selectXmlAllUserMenu(ServletRequestUtils.getIntParameter(request, "lus_id"), ServletRequestUtils.getIntParameter(request, "jenis"), ServletRequestUtils.getStringParameter(request, "aplikasi", "")).write(out);
		}else if(tipe.equals("cabangRegistered")) { //nebeng 
			this.elionsManager.selectXmlAllCabangRegistered(ServletRequestUtils.getIntParameter(request, "lus_id")).write(out);
		}else if(tipe.equals("cabangNotRegistered")) { //nebeng 
			this.elionsManager.selectXmlAllCabangNotRegistered(ServletRequestUtils.getIntParameter(request, "lus_id")).write(out);
		}
		return null;
	}
	
	public ModelAndView kurs(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Command cmd = new Command();
		
		ServletRequestDataBinder binder = new ServletRequestDataBinder(cmd);
		binder.bind(request);
		BindingResult err = binder.getBindingResult();

		List daftarKurs = elionsManager.selectCurrency(cmd);
		cmd.setDaftarKurs(daftarKurs);

		if(request.getParameter("show") == null) {
			cmd.setTglAwal(defaultDateFormat.format(elionsManager.selectSysdate(-7)));
			cmd.setTglAkhir(defaultDateFormat.format(elionsManager.selectSysdate()));
		}
		
		return new ModelAndView("common/kurs", "cmd", cmd)
			.addObject("select_kurs",DroplistManager.getInstance().get("KURS.xml","ID",request));
	}
	
	public ModelAndView cariprofile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap<String, Object>();
		
		SortedMap<String, String> daftarTipePersonal = new TreeMap<String, String>();
		daftarTipePersonal.put("0", "Profile ID");
		daftarTipePersonal.put("1", "Nama");
		daftarTipePersonal.put("2", "No. Identitas");
		daftarTipePersonal.put("3", "Ibu Kandung");
		
		SortedMap<String, String> daftarTipeCompany = new TreeMap<String, String>();
		daftarTipeCompany.put("0", "Profile ID");
		daftarTipeCompany.put("1", "Nama");
		daftarTipeCompany.put("4", "Jenis Usaha");
		daftarTipeCompany.put("5", "Jangka Waktu");
		daftarTipeCompany.put("6", "NPWP");
		
		cmd.put("daftarTipePersonal", daftarTipePersonal);
		cmd.put("daftarTipeCompany", daftarTipeCompany);
		
		if(request.getParameter("mcl_id") != null) {
			String jenis = request.getParameter("mcl_jenis");
			String kata = request.getParameter("mcl_id");
			if(jenis.equals("")) {
				List daftar = elionsManager.selectProfile("0", "0", kata);
				if(daftar.isEmpty()) daftar = elionsManager.selectProfile("0", "1", kata);
				cmd.put("daftar", daftar);
			}else {
				cmd.put("daftar", elionsManager.selectProfile("0", jenis, kata));
			}
		}else if(request.getParameter("searchPersonal") != null) {
			String tipe = request.getParameter("tipePersonal");
			String kata = request.getParameter("kataPersonal");
			cmd.put("daftar", elionsManager.selectProfile(tipe, "0", kata));
		}else if(request.getParameter("searchCompany") != null) {
			String tipe = request.getParameter("tipeCompany");
			String kata = request.getParameter("kataCompany");
			cmd.put("daftar", elionsManager.selectProfile(tipe, "1", kata));
		}
		
		return new ModelAndView ("common/cariprofile", "cmd", cmd);
	}
	public ModelAndView loadIkon(HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, IOException{
		Map map=new HashMap();
		File file=new File("G:\\Workspaces\\MyEclipse 10\\E-Lions\\WebContent\\include\\image");
//		File file=new File("C:\\EkaWeb\\webapps2\\elions.sinarmasmsiglife.co.id\\ROOT\\include\\image");
		String [] fileNames=file.list();
		//logger.info("ssss ="+file.length());
		List<Map<String, ?>> lsIkon=new ArrayList<Map<String, ?>>();
		File newFile;
		for(String nama: fileNames){
			newFile=new File(file.getPath()+"\\"+nama);
			//logger.info(nama);
			if(newFile.length()<1000 ){
				if((!newFile.getName().equals("winxp.gif")) && (!newFile.getName().equals("bgTab_Y.gif"))){
					Map param=new HashMap();
					param.put("VALUE", request.getContextPath()+"/include/image/"+nama);
					param.put("VALUE2", "include/image/"+nama);
					lsIkon.add(param);
				}
			}	
		}
		List lsIkonNew=new ArrayList();
		int kolom=5,counter=0;
		int brs=(int) Math.ceil(lsIkon.size()/kolom);
		for(int j=0;j<brs;j++){
			List<Map<?, ?>> lsBaris=new ArrayList<Map<?,?>>();
			Map<String, Object> mapBrs=new HashMap<String,Object>();
			for(int i=0;i<kolom;i++){
				Map param=(HashMap)lsIkon.get(counter);
				lsBaris.add(param);
				counter++;
				
			}
			mapBrs.put("BRS", j);
			mapBrs.put("LSBARIS", lsBaris);
			lsIkonNew.add(mapBrs);
		}
		
		map.put("lsIkonNew", lsIkonNew);
		return new ModelAndView("common/loadIkon", map);
	}
	
	/**Deprecated
	 * Digantikan dengan class yang beraada di UtilMutliController => reas_utilities
	 * Menu E-Lions adalah Maintenance=>Others=>Reas Utilities
	 */
	public ModelAndView laporan_reas(HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, IOException{
		Map map=new HashMap();
		List lsHasil=new ArrayList(),lsTampil;
		
		Integer angka=ServletRequestUtils.getIntParameter(request,"angka",1);
//		if(angka==1){
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		
		if(currentUser.getLus_id().equals("534"))
			lsHasil=elionsManager.prosesLaporanReasRiderNew();
//			lsTampil=lsHasil;
//			if(lsTampil.size()<1000)
//				lsTampil=lsTampil.subList(0, lsTampil.size());
//			else
//				lsTampil=lsTampil.subList(0, 999);
//		}else{
//			lsHasil=(List) request.getAttribute("lsHasil");
//			lsTampil=lsHasil;
//			if(lsTampil.size()<(angka*1000)-1)
//				lsTampil=lsTampil.subList((angka-1)*1000, lsTampil.size());
//			else
//				lsTampil=lsTampil.subList((angka-1)*1000, (angka*1000)-1);
//		}
//		double page;
//		if(lsHasil.size()<1000)
//			page=1;
//		else
//			page=Math.ceil(new Double(lsHasil.size())/1000);
		
//		page=Math.round(page);
//		request.setAttribute("lsHasil", lsHasil);
//		List lsPage=new ArrayList();
//		for(int i=0;i<page;i++){
//			Map p=new HashMap();
////			if(i==0)
//				p.put("no", i+1);
////			else
////				p.put("no", i*1000);
////			
//			p.put("link", "common/menu.htm?frame=laporan_reas&angka="+(i+1));
//			lsPage.add(p);
//		}
		map.put("lsHasil", lsHasil);
		
//		map.put("lsPage", lsPage);
//		return new ModelAndView("common/laporan_reas", map);
		return new ModelAndView("xlsCreator", "lsHasil", lsHasil);
	}

	public ModelAndView view_limit_uw(HttpServletRequest request, HttpServletResponse response) throws Exception{
		return new ModelAndView("common/view_limit_uw");
	}
	/**Fungsi:	Untuk Mengirim Email report
	 * @param 	HttpServletRequest request, HttpServletResponse response
	 * @return 	ModelAndView
	 * @throws 	Exception
	 * @authors	Ferry Harlim
	 */
	public ModelAndView sendEmailReport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String subject="List Report "+request.getParameter("title");
		String to=request.getParameter("to");
		f_validasi valid=new f_validasi();
		if(valid.f_validasi_email(to)){
			String text=subject;
			List<File> attachments = new ArrayList<File>();
			File sourceFile = new File(props.getProperty("report.email.dir")+"\\"+props.getProperty("report.email.nama"));
			attachments.add(sourceFile);
			String cc[]=props.getProperty("report.email.cc").split(";");
			email.send(false, currentUser.getEmail(), new String[] {to} ,cc, null, subject, text, attachments);
			map.put("info","Berhasil Kirim Email");
		}else{
			map.put("info","Email Salah, Silahkan Masukan Email yang Benar");
		}
		return new ModelAndView("common/sendEmailReport", map);
	}
	
	public ModelAndView cekCaraBayar(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map=new HashMap();
		List lsHasil=new ArrayList(),lsTampil;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		if(currentUser.getLus_id().equals("534")){
			lsHasil=elionsManager.prosesCariCaraBayar();
			map.put("lsHasil", lsHasil);
		}
		return new ModelAndView("xlsCreator2", "lsHasil", lsHasil);
	}

	public ModelAndView prosesInsertPremiUlinkManual(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		elionsManager.prosesInsertPremiUlinkManual(currentUser);
		return new ModelAndView("common/temp");
	}

//	/**Fungsi Untuk memproses reas manual(insert eka.m_sar_Temp, eka.m_reas_temp, eka.mst_reins , eka.mst_reins_product)
//	 * 
//	 * @param request
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	public ModelAndView prosesReasUwManual(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		elionsManager.prosesReasUwManual();
//		return new ModelAndView("common/temp");
//	}
	
/*	public ModelAndView laporan_reasRiderLink(HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, IOException{
		Map map=new HashMap();
		List lsHasil=new ArrayList(),lsTampil;
		
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		
		if(currentUser.getLus_id().equals("534"))
			lsHasil=elionsManager.prosesLaporanReasRiderLinkNew();

		map.put("lsHasil", lsHasil);
		
		return new ModelAndView("xlsCreatorRiderLink", "lsHasil", lsHasil);
	}*/
	
	
	/**Fungsi Untuk memproses insert_nik(insert eka.m_sar_Temp, eka.m_reas_temp, eka.mst_reins , eka.mst_reins_product)
	 * author lufi
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
/	 */
	public ModelAndView isinik(HttpServletRequest request, HttpServletResponse response){
		
		Map map = new HashMap();		
		if(request.getParameter("save")!=null) {
			List errors = new ArrayList();
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			//cek user nul
			String nik = request.getParameter("nik");			
			String confn= request.getParameter("confirmnik");
			
			if(currentUser==null) {
				errors.add("Harap Login ulang");			
			}			
			int exist=uwManager.selectExistingNik(nik);
			if(exist >0){
				errors.add("NIK tersebut sudah pernah digunakan silahkan konfirmasi ke HRD");
				map.put("err", errors);
			}
			if(errors.size()==0) {	
				String lus_id=currentUser.getLus_id();
				uwManager.updateNik(nik,lus_id);				
				map.put("succ", " ");
				currentUser.setLus_nik(nik);
				
				
			}else{
				
				map.put("err", errors);				
				map.put("nik", nik);
				map.put("confn", confn);
				
			}
		}else {
			Integer warning = ServletRequestUtils.getIntParameter(request, "warning", -1);
				
			if(warning > -1) {
				List errors = new ArrayList();
				errors.add("Harap Input NIK Karyawan Anda untuk mencegah penyalahgunaan user ID. Terima kasih.");
				map.put("err", errors);
			}
			
		}
		return new ModelAndView("common/isinik",map);
	}
	
	/**Fungsi untuk  mengupdate file rate Agency
	 * @author Canpri
	 * @since Sep 13, 2013 (2:41:22 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws Exception
/	 */
	public ModelAndView update_rate_agency(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, IOException{
		Map map = new HashMap();
		StringBuffer result = new StringBuffer();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		
		if(request.getParameter("fileName") != null) {
			String fileName = request.getParameter("fileName");
			String fileDir = props.getProperty("download.folder")+"\\rate_agency";
			FileUtils.downloadFile("attachment;", fileDir, fileName, response);
			return null;
		}
		
		if(request.getParameter("update") != null) {
			Upload upload = new Upload();
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			
			for(int i=0; i<10; i++){
				String old_file = ServletRequestUtils.getStringParameter(request, "old_file" + i, null);
				MultipartFile mf = upload.getDaftarFile().get(i);
				String filename = mf.getOriginalFilename();
				Integer q=0;
				
				File directory = new File(props.getProperty("download.folder")+"\\rate_agency");
				
				if(mf.isEmpty()){
					
				}else{
					if(old_file==null){
						result.append("File " + (i+1) + " (" + filename + ") GAGAL diupload, harap pilih file lama yang akan diupdate.\\n");
					}else{
						if(!directory.exists()) directory.mkdirs();
						
						String ori = upload.getDaftarFile().get(i).getOriginalFilename();
						
						//rename file lama
						File f = new File(directory+"\\"+old_file);
						String dest=directory+"\\"+ori;
						File outputFile_new = new File(dest);
						boolean success = f.renameTo(outputFile_new);
						//boolean success = f.delete();
						
						if(success==true){
							//copy file baru
						    FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile_new);
						    
						    result.append("File " + (i+1) + " (" + filename + ") BERHASIL diupload.\\n");
						}else{
							result.append("File " + (i+1) + " (" + filename + ") GAGAL diupload.\\n");
						}
					}
				}
			}
			
			map.put("pesan", result.toString());
		}
		
		List file = FileUtils.listFilesInDirectory(props.getProperty("download.folder")+"\\rate_agency");
		map.put("file", file);
		
		return new ModelAndView("common/updateRateAgency",map);
	}
	
	//Untuk download form bedattd Req Iriana
	public ModelAndView beda_ttd(HttpServletRequest request, HttpServletResponse response)throws ServletRequestBindingException{
		
		return new ModelAndView("common/beda_ttd");
	}

}
	