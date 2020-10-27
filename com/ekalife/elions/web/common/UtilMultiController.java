package com.ekalife.elions.web.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.BindException;
import org.springframework.web.bind.BindUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Reas;
import com.ekalife.elions.model.User;
import com.ekalife.elions.process.NilaiTunai;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.parent.ParentMultiController;
import com.ekalife.utils.view.PDFViewer;
import com.ibatis.common.resources.Resources;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;
/**
 * @author Yusuf
 * @since Jan 11, 2006
 */
public class UtilMultiController extends ParentMultiController{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	private DataSource dataSource;
	private JdbcTemplate jt;
	private NilaiTunai nilaiTunai;
	
	public void setNilaiTunai(NilaiTunai nilaiTunai) {
		this.nilaiTunai = nilaiTunai;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * List untuk View daftar file NAB, untuk download file2 yg di upload ke \\ebserver\Nab
	 * Ryan
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=nab
		
		String dir 	= ServletRequestUtils.getStringParameter(request, "dir", "");
		String flag = ServletRequestUtils.getStringParameter(request, "flag", "");
	/*	
		String path = uwManager.selectPathMstHistoryUpload2(uploadid,code_id);
		String filename = uwManager.selectFilenameMstHistoryUpload2(uploadid,code_id);*/
		String file = dir.substring(dir.lastIndexOf("/")+1,dir.length());
		//String ext = dir.substring(dir.indexOf("."),dir.length());
		File l_file = new File(dir);
		if(!dir.equals("")){
			FileInputStream in = null;
			 ServletOutputStream ouputStream = null;
			try{
				
				response.setContentType("application/force-download");//application/force-download
				response.setHeader("Content-Disposition", "Attachment;filename="+file);
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
				logger.error("ERROR :", e);
			}finally {
				try {
					if(in != null) {
						in.close();
					}
					if(ouputStream != null) {
						ouputStream.close();
					}
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		}
	   else{
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('Error , Kemungkinan Data Kosong Or Kesalahan System. Silakan Hub IT');window.close();</script>");
			sos.close();
		}
		
	}
	
	public ModelAndView nab(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=nab
		
		String dir 	= ServletRequestUtils.getStringParameter(request, "dir", "");
		String file = ServletRequestUtils.getStringParameter(request, "file", "");
		String flag = ServletRequestUtils.getStringParameter(request, "flag", "");
		String aplikasi = "Download NAB";
		if(!dir.equals("") && !file.equals("")){
			FileUtils.downloadFile("inline;", dir, file, response);
			return null;
		}else{
			String directory = "";
			if(!flag.equals("")){
				directory = props.getProperty("pdf.dir.factsheet_nab");
				//directory = props.getProperty("pdf.dir.nabbsm");
			}else{
				directory = props.getProperty("pdf.dir.factsheet_nab");
				/*directory = props.getProperty("pdf.dir.nab");*/
			}
			//directory = props.getProperty("pdf.dir.factsheet_nab");
			directory = directory.replace("\\","\\\\");
			List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
			Map cmd = new HashMap();
			cmd.put("daftarFile", daftarFile);
			cmd.put("directory", directory);
			cmd.put("aplikasi", aplikasi);
			return new ModelAndView("common/treeviewnab", cmd);
		}
	}
	
	public ModelAndView file_gadget(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=nab
		
		String spaj = ServletRequestUtils.getStringParameter(request, "file_cari_spaj", null);
		String dir 	= ServletRequestUtils.getStringParameter(request, "dir", "");
		String file = ServletRequestUtils.getStringParameter(request, "file", "");
		String flag = ServletRequestUtils.getStringParameter(request, "flag", "");
		String aplikasi = "Download NAB";
		if(!dir.equals("") && !file.equals("")){
			FileUtils.downloadFile("inline;", dir, file, response);
			return null;
		}else{
			
			HashMap nomer_spaj = bacManager.select_det_va_spaj(spaj);
			
			if(nomer_spaj != null){
				spaj = (String) nomer_spaj.get("SPAJ_TEMP");
			}
			
			String directory = "";
			directory = props.getProperty("pdf.dir.file_gadget")+"\\"+spaj;
			directory = directory.replace("\\","\\\\");
			List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
			Map cmd = new HashMap();
			cmd.put("daftarFile", daftarFile);
			cmd.put("directory", directory);
			cmd.put("aplikasi", aplikasi);
			return new ModelAndView("common/tree_file_gadget", cmd);
		}
	}
	
	public ModelAndView fundfactsheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=nab
		
		String dir 	= ServletRequestUtils.getStringParameter(request, "dir", "");
		String file = ServletRequestUtils.getStringParameter(request, "file", "");
		String aplikasi = "Download fundfactsheet";
		if(!dir.equals("") && !file.equals("")){
			FileUtils.downloadFile("inline;", dir, file, response);
			return null;
		}else{
			String directory = "";
			directory = props.getProperty("pdf.dir.factsheet_nab");
			directory = directory.replace("\\","\\\\");
			List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
			Map cmd = new HashMap();
			cmd.put("daftarFile", daftarFile);
			cmd.put("aplikasi", aplikasi);
			cmd.put("directory", directory);
			return new ModelAndView("common/treeviewnab", cmd);
		}
	}
	
	/**
	 * List untuk View daftar file promo item dr mkt supp, untuk download file2 yg di upload ke \\ebserver\promoitem
	 */
	public ModelAndView promoitem(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=promoitem
		
		String dir 	= ServletRequestUtils.getStringParameter(request, "dir", "");
		String file = ServletRequestUtils.getStringParameter(request, "file", "");
		String aplikasi = "Download Promo Item";
		if(!dir.equals("") && !file.equals("")){
			FileUtils.downloadFile("inline;", dir, file, response);
			return null;
		}else{
			String directory = props.getProperty("pdf.dir.promoitem");
			directory = directory.replace("\\","\\\\");
			List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
			Map cmd = new HashMap();
			cmd.put("daftarFile", daftarFile);
			cmd.put("aplikasi", aplikasi);
			cmd.put("directory", directory);
			return new ModelAndView("common/nab", cmd);
		}
	}
	
//	public ModelAndView updatePrintDateYangKosong(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		jt = new JdbcTemplate(dataSource);
//		
//		List list = jt.queryForList(
//			"select a.reg_spaj, b.printdate from "+ 
//			"eka.mst_Policy a, "+ 
//			"(select reg_spaj, max(msps_date) printdate from eka.mst_position_spaj where msps_desc = 'PRINT POLIS' group by reg_spaj) b "+
//			"where a.reg_spaj = b.reg_spaj "+
//			"and a.mspo_date_print is null ");
//	
//		for(int i=0; i<list.size(); i++) {
//			Map map = (Map) list.get(i);
//			String reg_spaj = (String) map.get("REG_SPAJ");
//			Date print_date = (Date) map.get("PRINTDATE");
//			//logger.info("UPDATING " + reg_spaj + " TGL_PRINT = " + defaultDateFormat.format(print_date));
//			
//			jt.update("update eka.mst_policy set mspo_date_print = ? where reg_spaj = ?", 
//					new Object[] {print_date, reg_spaj});
//			
//		}
//		return null;
//	}
//	
//	public ModelAndView nilaitunai(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
//
//		Map m = new HashMap();
//		m.put("spaj", spaj);
//		
//		if(request.getParameter("show")!=null) {
//			
//			m.put("mst_policy", jt.queryForList("select * from eka.mst_policy where reg_spaj=?", new String[]{spaj}));
//			m.put("mst_insured", jt.queryForList("select * from eka.mst_insured where reg_spaj=?", new String[]{spaj}));
//			
//			m.put("nilaitunai", nilaiTunai.proses(spaj, "", "simpan"));
//			m.put("nilaitunai2", nilaiTunai.proses(spaj, "", "cetak"));
//		}
//		
//		return new ModelAndView("common/test_tables", m);
//	}
	
//	public ModelAndView komisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
//
//		Map m = new HashMap();
//		m.put("spaj", spaj);
//		
//		if(request.getParameter("hitung")!=null) {
//			User currentUser = (User) request.getSession().getAttribute("currentUser");
//			if(currentUser == null) {
//				currentUser = new User();
//				currentUser.setLus_id("");
//			}
//			//elionsManager.hitungKomisi(spaj, currentUser);
//			//elionsManager.testSequenceVoucherPremiIndividu(spaj, currentUser);
//			//elionsManager.testTransferToCekPrintPolis(spaj, currentUser);
//		}
//		
//		return new ModelAndView("common/test_komisi", m);
//	}
	
	public ModelAndView ssu_endors(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String no_endors = ServletRequestUtils.getRequiredStringParameter(request, "no");
		List<Map> daftarRiderEndors = uwManager.selectDaftarRiderEndors(no_endors);
		
		if(daftarRiderEndors.isEmpty()) return null;
		
		String reg_spaj 	= (String) daftarRiderEndors.get(0).get("REG_SPAJ");
		List detBisnis 		= elionsManager.selectDetailBisnis(reg_spaj);
		String produkUtama 	= (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String detProdukUtama = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		String dir 			= props.getProperty("pdf.dir.endors");
		String dir_ssu		= props.getProperty("pdf.dir.syaratpolis");
		String cabang 		= elionsManager.selectCabangFromSpaj(reg_spaj);
		
		//daftar file
		List<File> pdfFiles = new ArrayList<File>();
		for(int i=0; i<daftarRiderEndors.size(); i++) {
			Map m = (HashMap) daftarRiderEndors.get(i);
			m.put("BISNIS", ((BigDecimal) m.get("BISNIS")).toString());
			m.put("DETBISNIS", ((BigDecimal) m.get("DETBISNIS")).toString());
			File file = PDFViewer.riderFile(elionsManager,uwManager, dir_ssu, produkUtama, detProdukUtama, m,reg_spaj,props);
			if( Integer.parseInt((String)m.get("BISNIS")) <300){
				file = PDFViewer.productFile(elionsManager, uwManager, dir_ssu,reg_spaj, m, props);
			}
			
			logger.info(file.toString());
			if(file!=null) if(file.exists()) if(!pdfFiles.contains(file)) pdfFiles.add(file);
		}
		
		//Bila SSU/SSK tidak ketemu
		if(pdfFiles.isEmpty()){
    		ServletOutputStream out = response.getOutputStream();
    		out.println("<script>alert('SSU/SSK Endors untuk produk tersebut tidak ditemukan. Silahkan dikonfirmasi dengan IT.');</script>");
    		out.flush();
    		return null;
		}
		
		//create dir if not exist
        File userDir = new File(dir+"\\"+cabang+"\\"+reg_spaj);
        if(!userDir.exists()) userDir.mkdirs();
        String dir_lengkap = dir+"\\"+cabang+"\\"+reg_spaj+"\\SSU_"+no_endors+"_"+reg_spaj+".pdf";
		FileOutputStream fileOutput = new FileOutputStream(dir_lengkap);
		
		//combine pdf dan simpan di file
		if(!pdfFiles.isEmpty()){
			PdfCopyFields copy = new PdfCopyFields(fileOutput);
			for(File f : pdfFiles) {
				copy.addDocument(new PdfReader(f.toString()));
			}
			copy.close();
		}
		fileOutput.close();
       	fileOutput = null;
       	
       	//setelah selesai combine pdf nya, tampilkan
		
       	//response header
		response.setHeader("Content-Disposition", "inline;filename=file.pdf");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		response.setContentType("application/pdf");
       	
		//baca source pdf nya
		PdfReader reader = null;
		try {
			reader = new PdfReader(dir_lengkap);
		} catch (IOException ioe) {
		}

		//"stamp" source pdf nya ke output stream tanpa menambah apa2
        ServletOutputStream sos = response.getOutputStream();
		PdfStamper stamper = new PdfStamper(reader, sos);
		stamper.close();
		sos = null;
		
       	return null;
	}
	
	public ModelAndView multi_doc_list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String spaj = null, spaj2 = null, spaj3 = null, spaj4 = null;
		String cabang = null, cabang2 = null, cabang3 = null, cabang4 = null;
		String directory = null, directory2 = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map cmd = new HashMap();
		
		spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		cabang = elionsManager.selectCabangFromSpaj(spaj);
		if(cabang == null) cabang = "";
		
		List list = new ArrayList();
		for(int i=1;i<=2;i++){
			list.add(i);
		}
		cmd.put("perulangan", list);

		String delete, pass, dok;
		directory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
		
		if(!cabang.trim().equals("")) {
			String fileName;

			List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
			if(!currentUser.getLde_id().equals("01") && !currentUser.getLde_id().equals("11") && !currentUser.getLde_id().equals("39") && !currentUser.getLde_id().equals("29")) {
				daftarFile = FileUtils.listFilesInDirectoryForAdmin(directory, spaj);
			}
			if("133,500,516,3177,690,3041,3732,3725".indexOf(currentUser.getLus_id())>-1) {
				 daftarFile = FileUtils.listFilesInDirectory(directory);
			}
			cmd.put("spaj", spaj);
			cmd.put("daftarFile", daftarFile);
			
			//Yusuf - (27/01/10) Req Chizni, bila ada polis lamanya, dokumenyna bisa dilihat juga disini
			Map m = uwManager.selectPolisLamaSurrenderEndorsement(spaj);
			if(m != null){
				String polis = (String) m.get("MSPO_POLICY_NO");
				spaj2 = elionsManager.selectSpajFromPolis(polis);
				cabang2 = elionsManager.selectCabangFromSpaj(spaj2);
				if(cabang2 == null) cabang2 = "";
				directory2 = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj2;

				List<DropDown> daftarFile2 = FileUtils.listFilesInDirectory(directory2);
				cmd.put("spaj2", spaj2);
				cmd.put("daftarFile2", daftarFile2);
			}
			
			//Deddy - (30/1/2012) Req EKO BAGUS DARMAWAN, munculin dokumen2 PAS BP juga.
			Map n = elionsManager.selectPolisPas(spaj);
			if(n !=null){
				String paket = (String) n.get("PAKET");
				String jenis_pas = (String) n.get("JENIS_PAS");
				String msag_id = (String) n.get("MSAG_ID");
				if(paket.equals("BUSINESS PARTNER") && jenis_pas.equals("AP/BP")){
				String directory3=props.getProperty("pdf.dir.arthamas.dokumenAgen")+"\\"+msag_id;
				
				List<DropDown> daftarFile3 = FileUtils.listFilesInDirectory(directory3);
				cmd.put("spaj3", spaj);
				cmd.put("daftarFile3", daftarFile3);
				cmd.put("msag_id", msag_id);
				}
			}
			
			//Canpri - (18/10/2012) Req HAYATIN NAFIS, munculkan dokumen batal
			List doc_batal = uwManager.selectDocBatal(spaj);
			if(!doc_batal.isEmpty()){
				Map mm = (HashMap) doc_batal.get(0);
				
				String reg_spaj = (String)mm.get("REG_SPAJ");
				Integer tindakan_cd = ((BigDecimal)mm.get("TINDAKAN_CD")).intValue();
				String cancel_tgl = (String)mm.get("CANCEL_WHEN_TGL");
				String cancel_jam = (String)mm.get("CANCEL_WHEN_JAM");
				String tgl_cancel = (String)mm.get("CANCEL_WHEN_ALL");
				String tcd = "";
				
				if(tindakan_cd==2)tcd = "surat_refund_"+reg_spaj+"_"+cancel_tgl;
				if(tindakan_cd==3)tcd = "surat_ganti_tertanggung_"+reg_spaj+"_"+cancel_tgl;
				if(tindakan_cd==4)tcd = "surat_ganti_plan_"+reg_spaj+"_"+cancel_tgl;
				
				String directory4 = props.getProperty("upload.dir.refund");
				
				List<DropDown> daftarFile4 = FileUtils.listFilesInDirectoryStartsWith(directory4, tcd);
				cmd.put("spaj4", spaj);
				cmd.put("daftarFile4", daftarFile4);
			}
			
			//Deddy - (23/4/2013) Req Yune(disetujui Ariani juga untuk dibuka ke U/W). munculkan dokumen perpanjangan polis.
			//Info dari rudy, baca langsung di file path nya apakah ada apa ga. Tidak bisa dari history posisi di table query.
			String directory5 = props.getProperty("pdf.dir.perpanjangan")+"\\"+cabang+"\\"+spaj;
			List<DropDown> daftarFile5 = FileUtils.listFilesInDirectory(directory5);
			if(!daftarFile5.isEmpty()){
				cmd.put("spaj5", spaj);
				cmd.put("daftarFile5", daftarFile5);
			}
						
			return new ModelAndView("common/multi_doc_list", cmd);
		}else {
			cmd.put("error", "Anda tidak berhak mengakses halaman ini. Terima kasih.");
			return new ModelAndView("common/dokumen", cmd);
		}
	}
	
	/**
	 * List untuk View daftar document pdf, ini dipake di halaman viewer, uw, printpolis
	 */
	public ModelAndView doc_list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=doc_list&spaj=01200700020
		
		String spaj = null, spaj2 = null, spaj3 = null, spaj4 = null;
		String cabang = null, cabang2 = null, cabang3 = null, cabang4 = null;
		String directory = null, directory2 = null, directory9 = null, directoryR1 = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map cmd = new HashMap();
		String from = null;
		
		spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		cabang = elionsManager.selectCabangFromSpaj(spaj);
		if(cabang == null) cabang = "";

		String delete, pass, dok;
		directory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
		
		if((delete = request.getParameter("delete")) != null) {
			if((pass = request.getParameter("pass")) != null) {
				if(pass.equals("14041985")) {
//					dok = ServletRequestUtils.getStringParameter(request, "dok");//(request, "dok", "");
					dok=ServletRequestUtils.getStringParameter(request, "dok", "");
					String realfile=dok.substring( 12, dok.length());
					FileUtils.deleteFile(directory, realfile);
				}else {
					cmd.put("error", "Anda tidak berhak mengakses halaman ini. Terima kasih.");
				}
			}else {
				cmd.put("error", "Anda tidak berhak mengakses halaman ini. Terima kasih.");
			}
		}
		
		if(!cabang.trim().equals("")) {
			String fileName;

			List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
			
			//tambahan Request Yudi Bas untuk lus_id dibawah ini bisa akses document full			
			if(!currentUser.getLde_id().equals("01") && !currentUser.getLde_id().equals("11") && "4928,4929,4930,4931,3911,737,4259".indexOf(currentUser.getLus_id())<0 && !currentUser.getLde_id().equals("39")
					&& !currentUser.getLde_id().equals("29")) {
				daftarFile = FileUtils.listFilesInDirectoryForAdmin(directory, spaj);
			}
			cmd.put("spaj", spaj);
			cmd.put("daftarFile", daftarFile);
			
			//Yusuf - (27/01/10) Req Chizni, bila ada polis lamanya, dokumenyna bisa dilihat juga disini
			Map m = uwManager.selectPolisLamaSurrenderEndorsement(spaj);
			if(m != null){
				String polis = (String) m.get("MSPO_POLICY_NO");
				spaj2 = elionsManager.selectSpajFromPolis(polis);
				cabang2 = elionsManager.selectCabangFromSpaj(spaj2);
				if(cabang2 == null) cabang2 = "";
				directory2 = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj2;

				List<DropDown> daftarFile2 = FileUtils.listFilesInDirectory(directory2);
				cmd.put("spaj2", spaj2);
				cmd.put("daftarFile2", daftarFile2);
			}
			
			//Deddy - (30/1/2012) Req EKO BAGUS DARMAWAN, munculin dokumen2 PAS BP juga.
			Map n = elionsManager.selectPolisPas(spaj);
			if(n !=null){
				String paket = n.get("PAKET")==null?null:(String) n.get("PAKET");
				String jenis_pas = (String) n.get("JENIS_PAS");
				String msag_id = (String) n.get("MSAG_ID");
				if(!StringUtil.isEmpty(paket)){
					if(paket.equals("BUSINESS PARTNER") && jenis_pas.equals("AP/BP")){
						String directory3=props.getProperty("pdf.dir.arthamas.dokumenAgen")+"\\"+msag_id;
						
						List<DropDown> daftarFile3 = FileUtils.listFilesInDirectory(directory3);
						cmd.put("spaj3", spaj);
						cmd.put("daftarFile3", daftarFile3);
						cmd.put("msag_id", msag_id);
						}
				}
				
			}
			
			//Canpri - (18/10/2012) Req HAYATIN NAFIS, munculkan dokumen batal
			List doc_batal = uwManager.selectDocBatal(spaj);
			if(!doc_batal.isEmpty()){
				Map mm = (HashMap) doc_batal.get(0);
				
				String reg_spaj = (String)mm.get("REG_SPAJ");
				Integer tindakan_cd = ((BigDecimal)mm.get("TINDAKAN_CD")).intValue();
				String cancel_tgl = (String)mm.get("CANCEL_WHEN_TGL");
				String cancel_jam = (String)mm.get("CANCEL_WHEN_JAM");
				String tgl_cancel = (String)mm.get("CANCEL_WHEN_ALL");
				String tcd = "";
				
				if(tindakan_cd==2)tcd = "surat_refund_"+reg_spaj+"_"+cancel_tgl;
				if(tindakan_cd==3)tcd = "surat_ganti_tertanggung_"+reg_spaj+"_"+cancel_tgl;
				if(tindakan_cd==4)tcd = "surat_ganti_plan_"+reg_spaj+"_"+cancel_tgl;
				
				String directory4 = props.getProperty("upload.dir.refund");
				
				List<DropDown> daftarFile4 = FileUtils.listFilesInDirectoryStartsWith(directory4, tcd);
				cmd.put("spaj4", spaj);
				cmd.put("daftarFile4", daftarFile4);
			}
			
			//Deddy - (23/4/2013) Req Yune(disetujui Ariani juga untuk dibuka ke U/W). munculkan dokumen perpanjangan polis.
			//Info dari rudy, baca langsung di file path nya apakah ada apa ga. Tidak bisa dari history posisi di table query.
			String directory5 = props.getProperty("pdf.dir.perpanjangan")+"\\"+cabang+"\\"+spaj;
			List<DropDown> daftarFile5 = FileUtils.listFilesInDirectory(directory5);
			if(!daftarFile5.isEmpty()){
				cmd.put("spaj5", spaj);
				cmd.put("daftarFile5", daftarFile5);
			}
			
			//MANTA (2/12/2015) - Surat Reinstatement Policy
			String no_inbox = uwManager.selectIDinbox(spaj);
			String directory6 = directory + "\\REINS_" + no_inbox;
			List<DropDown> daftarFile6 = FileUtils.listFilesInDirectory(directory6);
			if(!daftarFile6.isEmpty()){
				cmd.put("spaj6", spaj);
				cmd.put("daftarFile6", daftarFile6);
			}
			
			directory9 = props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj;
			List<DropDown> daftarFile9 = FileUtils.listFilesInDirectory(directory9);
			if(!daftarFile9.isEmpty()){
				cmd.put("spaj9", spaj);
				cmd.put("daftarFile9", daftarFile9);
			}
			
			//Ridhaal (4/8/2016) (Req Shopiah Laura Margaret , untuk memunculkan file worksheet setelah print worksheet di UW speedy)
			//props.getProperty("pdf.dir.report") + "\\uw_worksheet\\" + ws.getReg_spaj().substring(0,2) + "\\" + ws.getReg_spaj() + "\\";
			directoryR1 = props.getProperty("pdf.dir.report")+ "\\uw_worksheet\\" +"\\"+cabang+"\\"+spaj;
			List<DropDown> daftarFileR1 = FileUtils.listFilesInDirectory(directoryR1);
			if(!daftarFileR1.isEmpty()){
				cmd.put("spajR1", spaj);
				cmd.put("daftarFileR1", daftarFileR1);
			}
			
						
			return new ModelAndView("common/doc_list", cmd);
		}else {
			cmd.put("error", "Anda tidak berhak mengakses halaman ini. Terima kasih.");
			return new ModelAndView("common/dokumen", cmd);
		}
				
	}
	
	/**
	 * List untuk View daftar document pdf, ini dipake di halaman akseptasi pas BP
	 */
	public ModelAndView doc_list_bp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=doc_list_bp&mid=000006
		
		String mid = null;
		String directory = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map cmd = new HashMap();
		
		mid = ServletRequestUtils.getStringParameter(request, "mid", "");

		directory = props.getProperty("pdf.dir.arthamas.dokumenAgen")+"\\"+mid;
		

			List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
			cmd.put("mid", mid);
			cmd.put("daftarFile", daftarFile);
			
						
			return new ModelAndView("common/doc_list_bp", cmd);
				
	}
	
	/**
	 * Request Himmia untuk view dokumen no pre di \\ebserver\accounting\NoPre\200909\090850725  
	 * 
	 * @author yusuf
	 * @since Sep 11, 2009 (4:20:06 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView doc_list_nopre(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=doc_list_nopre&spaj=01200700020
		String nopre = ServletRequestUtils.getStringParameter(request, "nopre", "");
		String directory = props.getProperty("pdf.dir.nopre")+"\\" + uwManager.selectTglJurnalFromPre(nopre) + "\\"+nopre;
		String fileName;
		Map cmd = new HashMap();
		cmd.put("nopre", nopre);

		List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
		cmd.put("daftarFile", daftarFile);
		return new ModelAndView("common/doc_list_nopre", cmd);
	}
	
	public ModelAndView doc2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//ini bisa untuk semua file di folder mana saja, namun parameter yg dipassing harus lengkap
		
		//http://ajsjava/E-Lions/common/util.htm?window=doc2&file=xxx
		String file = ServletRequestUtils.getRequiredStringParameter(request, "file");
		if(!file.equals("")) {
			Document document = new Document();
			try {
				//response header
				response.setHeader("Content-Disposition", "inline;filename=file.pdf");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentType("application/pdf");

				//baca source pdf nya
				PdfReader reader = null;
				reader = new PdfReader(file, "ekalife".getBytes());

				//"stamp" source pdf nya ke output stream tanpa menambah apa2
                ServletOutputStream sos = response.getOutputStream();
				PdfStamper stamper = new PdfStamper(reader, sos);
				stamper.close();				
				
			}catch(Exception de) {
				logger.error(de);
	    		ServletOutputStream out = response.getOutputStream();
	    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
	    		out.flush();
			}
			document.close();
		}else {
    		ServletOutputStream out = response.getOutputStream();
    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
    		out.flush();
		}
		return null;	
	}
	
	public ModelAndView doc_bp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=doc_bp&file=asdf&mid=000006
		
		String mid = ServletRequestUtils.getStringParameter(request, "mid", "");
		String directory = props.getProperty("pdf.dir.arthamas.dokumenAgen")+"\\"+mid;
		String fileName;
		Map cmd = new HashMap();
		cmd.put("mid", mid);
		
		if((fileName = request.getParameter("file")) != null) {
			Document document = new Document();
			try {
				//response header
				response.setHeader("Content-Disposition", "inline;filename=file.pdf");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentType("application/pdf");

				//baca source pdf nya
				PdfReader reader = null;
				try {reader = new PdfReader(directory + "\\" + fileName);} catch (IOException ioe3) {}

				//"stamp" source pdf nya ke output stream tanpa menambah apa2
                ServletOutputStream sos = response.getOutputStream();
				PdfStamper stamper = new PdfStamper(reader, sos);
				if(stamper!=null){
					stamper.close();		
				}
				if(sos!=null){
					sos.flush();
					sos.close();
				}
				if(reader!=null){
					reader.close();
				}
			}catch(DocumentException de) {
				logger.error(de);
	    		ServletOutputStream out = response.getOutputStream();
	    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
	    		out.flush();
			}
			if(document!=null){
				document.close();
			}
			return null;
		}else {
			List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
			cmd.put("daftarFile", daftarFile);
			return new ModelAndView("common/doc_bp", cmd);
		}
		
	}
	
	public ModelAndView doc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//http://ajsjava/E-Lions/common/util.htm?window=doc&file=asdf&spaj=01200700020
		String file = ServletRequestUtils.getRequiredStringParameter(request, "file");
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		//spaj = elionsManager.selectEncryptDecrypt(spaj, "d");
		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		if(cabang == null) cabang = "";

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		//Yusuf (11/1/12) - Admin cabang boleh liat, tapi hanya file scan saja, file generate tidak boleh
		if(currentUser != null) {
			if(currentUser.getLus_bas().intValue() == 1) {
				if(file.startsWith(spaj)) {
					//boleh	
				}else {
					//gak boleh
		    		ServletOutputStream out = response.getOutputStream();
		    		out.println("<script>alert('Anda tidak mempunyai hak akses terhadap dokumen ini. Silahkan akses HANYA dokumen hasil scan saja.');</script>");
		    		if(out!=null){
		    			out.flush();
		    			out.close();
		    		}
		    		return null;
				}
			}
		}
		
		if(!cabang.equals("")) {
			String directory = "";
			//Integer a = file.indexOf("surat");
			if(file.indexOf("surat") !=-1){
				if(file.contains("surat_simcard")){
					directory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
				}else{
					directory = props.getProperty("upload.dir.refund");
				}
			}else if(file.contains("SSH_SSP")){
				directory = props.getProperty("pdf.dir.perpanjangan")+"\\"+cabang+"\\"+spaj;
			}else if(file.contains("ENDORS_") || file.contains("SSU_")){
				directory = props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj;
			}else if(file.contains("WORKSHEET")){
				String no_inbox = uwManager.selectIDinbox(spaj);
				directory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\REINS_"+no_inbox;
			}else if(file.contains("uw_worksheet_") || file.contains("uw_worksheet_p1_") || file.contains("uw_worksheet_p2_") || file.contains("uw_worksheet_p3_") ){
				directory = props.getProperty("pdf.dir.report")+ "\\uw_worksheet\\" +"\\"+cabang+"\\"+spaj;
			}else if(file.contains("polis_all") || file.contains("pelengkap")){
				directory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;				
			}else{
				directory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
			}
			Document document = new Document();
			try {
				
				//Rahmayanti (15/8/2014) - Tambahahan viewer image
				String ext = file.substring(file.indexOf(".")+1,file.length());
				
				if(!(file.contains(".pdf") || file.contains(".PDF") || ext.equals("jpg") || ext.equals("jpeg")|| ext.equals("png") || ext.equals("JPG")||ext.equals("JPEG")||ext.equals("PNG"))){//bila file email, lgsg close saja.
					return null;
				}
				
				if(ext.equals("pdf")||ext.equals("PDF")){
					//response header
					response.setHeader("Content-Disposition", "inline;filename=file.pdf");
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					response.setContentType("application/pdf");
									
					Boolean itextReaderOld = true;
					//baca source pdf nya
					PdfReader reader = null;
					com.itextpdf.text.pdf.PdfReader readerNew = null;
					Document.plainRandomAccess = true;
					try{
						reader = new PdfReader(directory + "\\" + file, props.getProperty("pdf.ownerPassword").getBytes());
					} catch(IOException ioe1){
						try {
							reader = new PdfReader(directory + "\\" + file, "ekalife".getBytes());
						} catch (IOException ioe2) {
							try {
								reader = new PdfReader(directory + "\\" + file, elionsManager.selectTanggalLahirPemegangPolis(spaj).getBytes());
							} catch (IOException ioe3) {
								try {
									reader = new PdfReader(directory + "\\" + file);
								} catch (IOException ioe4) {
									try{
										File a = new File (directory + "\\" + file);
										reader = new PdfReader(a.toString());
									}catch (IOException ioe5) {
										try{
											readerNew = new com.itextpdf.text.pdf.PdfReader(directory + "\\" + file);
											itextReaderOld = false;
										}catch(IOException ioe6){
											
										}
									}
								}
							}
						}
					}
					
					
	
					//"stamp" source pdf nya ke output stream tanpa menambah apa2
	                ServletOutputStream sos = response.getOutputStream();
	                if(itextReaderOld){
	                	PdfStamper stamper = new PdfStamper(reader,sos);
	                	//disable printing apabila ada flag
	    				if(ServletRequestUtils.getIntParameter(request, "readonly", 0) == 1){
	    					stamper.setEncryption(false, null, null, PdfWriter.ALLOW_MODIFY_ANNOTATIONS);
	    				}
	    				if(stamper!=null){
	    					stamper.close();
	    				}
	                }else{
	                	com.itextpdf.text.pdf.PdfStamper stamper = new com.itextpdf.text.pdf.PdfStamper(readerNew, sos);
	                	//disable printing apabila ada flag
	    				if(ServletRequestUtils.getIntParameter(request, "readonly", 0) == 1){
	    					stamper.setEncryption(false, null, null, PdfWriter.ALLOW_MODIFY_ANNOTATIONS);
	    				}
	    				if(stamper!=null){
	    					stamper.close();
	    				}
	                }
					
					
					
	
					//if(file.toLowerCase().contains("sertifikat") || file.toLowerCase().contains("polis"))
	//				if(currentUser != null) {
	//					if(!currentUser.getLde_id().equals("01") && !currentUser.getLde_id().equals("11")) { //hanya IT & UW yang boleh print
	//						stamper.setEncryption(null, "nananananaBatman!!!".getBytes(), PdfWriter.ALLOW_MODIFY_ANNOTATIONS, PdfWriter.STANDARD_ENCRYPTION_128);
	//					}
	//				}
	                
					if(sos!=null){
						sos.flush();
						sos.close();
					}
					if(reader!=null){
	                	reader.close();
	                }
					if(readerNew!=null){
						readerNew.close();
					}
				}
				else if(ext.equals("jpg")||ext.equals("jpeg")||ext.equals("png")||ext.equals("JPG")||ext.equals("JPEG")||ext.equals("PNG"))
					FileUtils.downloadFile("inline;", directory, file, response);	

			}catch(Exception de) {
				logger.error(de);
	    		ServletOutputStream out = response.getOutputStream();
	    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
	    		if(out!=null){
	    			out.flush();
	    			out.close();
	    		}
	    		
			}finally{
				//if(document.isOpen()) document.close();
			}
		}else {
    		ServletOutputStream out = response.getOutputStream();
    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
    		if(out!=null){
    			out.flush();
    			out.close();
    		}
		}
		return null;	
	}
	
	public void doc_ssu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String file = ServletRequestUtils.getRequiredStringParameter(request, "file");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if(1+1==2) {
//			String directory =  "C:\\EkaWeb\\ss_smile_test";
			String directory =  "\\\\Ebserver\\pdfind\\ss_smile";
			File file_cek = new File(directory+"\\"+file);
			if (!file_cek.exists()){
				ServletOutputStream out = response.getOutputStream();
	    		out.println("<script>alert('FILE TIDAK ADA');</script>");
	    		if(out!=null){
	    			out.flush();
	    			out.close();
	    		}
			}
				
			Document document = new Document();
			try {
				String ext = file.substring(file.indexOf(".")+1,file.length());
				
				if(ext.equals("pdf")||ext.equals("PDF")){
					//response header
					response.setHeader("Content-Disposition", "inline;filename=file.pdf");
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					response.setContentType("application/pdf");
									
					Boolean itextReaderOld = true;
					//baca source pdf nya
					PdfReader reader = null;
					com.itextpdf.text.pdf.PdfReader readerNew = null;
					try{
						reader = new PdfReader(directory + "\\" + file, props.getProperty("pdf.ownerPassword").getBytes());
					} catch(IOException ioe1){
						try {
							reader = new PdfReader(directory + "\\" + file, "ekalife".getBytes());
						} catch (IOException ioe2) {
							try {
								reader = new PdfReader(directory + "\\" + file);
							} catch (IOException ioe3) {
								try {
									reader = new PdfReader(directory + "\\" + file);
								} catch (IOException ioe4) {
									try{
										File a = new File (directory + "\\" + file);
										reader = new PdfReader(a.toString());
									}catch (IOException ioe5) {
										try{
											readerNew = new com.itextpdf.text.pdf.PdfReader(directory + "\\" + file);
											itextReaderOld = false;
										}catch(IOException ioe6){
											
										}
									}
								}
							}
						}
					}
					//"stamp" source pdf nya ke output stream tanpa menambah apa2
	                ServletOutputStream sos = response.getOutputStream();
	                if(itextReaderOld){
	                	PdfStamper stamper = new PdfStamper(reader,sos);
	                	//disable printing apabila ada flag
	    				if(ServletRequestUtils.getIntParameter(request, "readonly", 0) == 1){
	    					stamper.setEncryption(false, null, null, PdfWriter.ALLOW_MODIFY_ANNOTATIONS);
	    				}
	    				if(stamper!=null){
	    					stamper.close();
	    				}
	                }else{
	                	com.itextpdf.text.pdf.PdfStamper stamper = new com.itextpdf.text.pdf.PdfStamper(readerNew, sos);
	                	//disable printing apabila ada flag
	    				if(ServletRequestUtils.getIntParameter(request, "readonly", 0) == 1){
	    					stamper.setEncryption(false, null, null, PdfWriter.ALLOW_MODIFY_ANNOTATIONS);
	    				}
	    				if(stamper!=null){
	    					stamper.close();
	    				}
	                }
	                
					if(sos!=null){
						sos.flush();
						sos.close();
					}
					if(reader!=null){
	                	reader.close();
	                }
					if(readerNew!=null){
						readerNew.close();
					}
				}
				else if(ext.equals("jpg")||ext.equals("jpeg")||ext.equals("png")||ext.equals("JPG")||ext.equals("JPEG")||ext.equals("PNG"))
					FileUtils.downloadFile("inline;", directory, file, response);	

			}catch(Exception de) {
				logger.error(de);
	    		ServletOutputStream out = response.getOutputStream();
	    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
	    		if(out!=null){
	    			out.flush();
	    			out.close();
	    		}
	    		
			}finally{
				//if(document.isOpen()) document.close();
			}
		}else {
    		ServletOutputStream out = response.getOutputStream();
    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
    		if(out!=null){
    			out.flush();
    			out.close();
    		}
		}
	}
	/*
	public ModelAndView doc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=doc&file=asdf&spaj=01200700020
		String file = ServletRequestUtils.getRequiredStringParameter(request, "file");
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		//spaj = elionsManager.selectEncryptDecrypt(spaj, "d");
		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		if(cabang == null) cabang = "";
		if(!cabang.equals("")) {
			String directory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
			Document document = new Document();
			try {
				PdfReader reader = null;
				try {
					reader = new PdfReader(directory + "\\" + file, "ekalife".getBytes());
				} catch (IOException ioe) {
					try {
						reader = new PdfReader(directory + "\\" + file, elionsManager.selectTanggalLahirPemegangPolis(spaj).getBytes());
					} catch (IOException ioe2) {
						try {
							reader = new PdfReader(directory + "\\" + file);
						} catch (IOException ioe3) {
						}
					}
				}

				response.setContentType("application/pdf");
                ServletOutputStream sos = response.getOutputStream();
                PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
				Rectangle psize = reader.getPageSizeWithRotation(1);
				
				document.setPageSize(psize);
				
                document.open();
		        PdfContentByte cb = writer.getDirectContent();
								
		        for (int j=1; j <= reader.getNumberOfPages(); j++){
		        	document.newPage();
		        	cb.addTemplate(writer.getImportedPage(reader, j), 0,0);
		        }
		        
				// setting some response headers
				response.setHeader("Content-Disposition", "inline;filename=file.pdf");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");

			}catch(DocumentException de) {
				logger.error(de);
	    		ServletOutputStream out = response.getOutputStream();
	    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
	    		out.flush();
			}
			document.close();
		}else {
    		ServletOutputStream out = response.getOutputStream();
    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
    		out.flush();
		}
		return null;	
	}
	*/
	public ModelAndView dokumen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=dokumen&spaj=01200700020
		
		String spaj = null;
		String cabang = null;
		String encrypted = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map cmd = new HashMap();
		String from = null;
		
		//di view dari e-lions viewer, currentUser harus ada, dan dept life benefit, uw, atau cs
		if(currentUser!= null) {
			if("01,11,12,27,04,39".indexOf(currentUser.getLde_id())>-1) {
				spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
				//encrypted = elionsManager.selectEncryptDecrypt(spaj, "e");
			}
			from = "java";
		//di view dari PB himmia, harus ada enkripsinya
		}else {
			//encrypted = ServletRequestUtils.getStringParameter(request, "spaj", "");
			//spaj = elionsManager.selectEncryptDecrypt(encrypted, "d");
			spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			from = "pb";
		}

		cmd.put("from", from);
		
		cabang = elionsManager.selectCabangFromSpaj(spaj);
		if(cabang == null) cabang = "";
		
		if(!cabang.trim().equals("")) {
			String directory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
			String fileName;

			String delete, pass;
			if((delete = request.getParameter("delete")) != null) {
				if((pass = request.getParameter("pass")) != null) {
					if(pass.equals("14041985")) {
						FileUtils.deleteFile(directory, delete);
					}else {
						cmd.put("error", "Anda tidak berhak mengakses halaman ini. Terima kasih.");
					}
				}else {
					cmd.put("error", "Anda tidak berhak mengakses halaman ini. Terima kasih.");
				}
			}
			
			if((fileName = request.getParameter("file")) != null) {
				if(from.equals("java")) {
					FileUtils.downloadFile("inline;", directory, fileName, response);
				} else {
					FileUtils.downloadFile("inline;", directory, fileName, response);
				}
				return null;
			}else {
				List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
				cmd.put("spaj", spaj);
				//cmd.put("encrypted", FormatString.forHTML(encrypted));
				cmd.put("daftarFile", daftarFile);
				return new ModelAndView("common/dokumen", cmd);
			}
		}else {
			cmd.put("error", "Anda tidak berhak mengakses halaman ini. Terima kasih.");
			return new ModelAndView("common/dokumen", cmd);
		}
		
	}

	/**
	 * Request Himmia, untuk view no pre di \\ebserver\accounting\NoPre\yyyymm\nopre
	 * 
	 * @author yusuf
	 * @since Sep 11, 2009 (3:25:53 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView dokumen_nopre(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=dokumen_nopre&nopre=090850725
		
		String nopre = ServletRequestUtils.getStringParameter(request, "nopre", "");
		String directory = props.getProperty("pdf.dir.nopre")+"\\" + uwManager.selectTglJurnalFromPre(nopre) + "\\"+nopre;
		String fileName;
		Map cmd = new HashMap();
		cmd.put("nopre", nopre);
		
		if((fileName = request.getParameter("file")) != null) {
			Document document = new Document();
			try {
				//response header
				response.setHeader("Content-Disposition", "inline;filename=file.pdf");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				response.setContentType("application/pdf");

				//baca source pdf nya
				PdfReader reader = null;
				try {reader = new PdfReader(directory + "\\" + fileName);} catch (IOException ioe3) {}

				//"stamp" source pdf nya ke output stream tanpa menambah apa2
                ServletOutputStream sos = response.getOutputStream();
				PdfStamper stamper = new PdfStamper(reader, sos);
				stamper.close();				
			}catch(DocumentException de) {
				logger.error(de);
	    		ServletOutputStream out = response.getOutputStream();
	    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
	    		out.flush();
			}
			document.close();
			return null;
		}else {
			List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
			cmd.put("daftarFile", daftarFile);
			return new ModelAndView("common/dokumen_nopre", cmd);
		}
		
	}
	
	/**
	 * Controller untuk download apapun yg ada di server
	 * 
	 * @author Yusuf
	 * @since Jan 21, 2009 (10:59:15 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public void download(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String dir 	= ServletRequestUtils.getRequiredStringParameter(request, "dir");
		String file = ServletRequestUtils.getRequiredStringParameter(request, "file");
		int logo = ServletRequestUtils.getIntParameter(request, "logo", 0);
		
		logger.info("dir "+dir);
		/*
		try {
			FileUtils.downloadFile("inline;", dir, file, response);
		} catch (FileNotFoundException e) {
			logger.error("ERROR :", e);
		} catch (IOException e) {
			logger.error("ERROR :", e);
		}
		return null;
		*/

		Document document = new Document();
		try {
			
			//baca source pdf nya
			Boolean itextReaderOld = true;
			PdfReader reader = null;
			com.itextpdf.text.pdf.PdfReader readerNew = null;
			
			try{
				reader = new PdfReader(dir + "\\" + file, "ekalife".getBytes());
			} catch(IOException ioe1){
				try{
					reader = new PdfReader(dir + "\\" + file);
				}catch(IOException ioe2){
					itextReaderOld=false;
					try {
						readerNew = new com.itextpdf.text.pdf.PdfReader(dir + "\\" + file, "ekalife".getBytes());
					} catch (IOException ioe) {
						try {
							readerNew = new com.itextpdf.text.pdf.PdfReader(dir + "\\" + file);
						} catch (IOException e) {
//							logger.error("ERROR :", e);		
							response.setContentType("text/html");					
							ServletOutputStream out = response.getOutputStream();
				    		out.println("<script>alert('File tidak ada. Harap cek kembali data Anda masukkan.');</script>");
				    		out.flush();
				    		document.close();
						}
					}
				}
			}
			
			
//			response header
			response.setHeader("Content-Disposition", "inline;filename=file.pdf");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setContentType("application/pdf");

			//"stamp" source pdf nya ke output stream tanpa menambah apa2
            ServletOutputStream sos = response.getOutputStream();
            if(itextReaderOld){
            	PdfStamper stamper = new PdfStamper(reader, sos);
            	//disable printing apabila ada flag
    			if(ServletRequestUtils.getIntParameter(request, "readonly", 0) == 1){
    				stamper.setEncryption(false, null, null, PdfWriter.ALLOW_MODIFY_ANNOTATIONS);
    			}
    			//Yusuf (21 Mar 2011) - Bisa insert logo di PDF
    			if(logo == 1){
    				PdfContentByte content = stamper.getOverContent(reader.getNumberOfPages());
    				Image image = Image.getInstance(Resources.getResourceURL(props.getProperty("images.ttd.ekalife")));
    				image.setAbsolutePosition(20, reader.getPageSize(1).getHeight() - 50);
    				image.scaleToFit(150, 36);
    				content.addImage(image);
    			}
    			stamper.close();	
            }else{
            	com.itextpdf.text.pdf.PdfStamper stamperNew = new com.itextpdf.text.pdf.PdfStamper(readerNew, sos);
            	//disable printing apabila ada flag
    			if(ServletRequestUtils.getIntParameter(request, "readonly", 0) == 1){
    				stamperNew.setEncryption(false, null, null, PdfWriter.ALLOW_MODIFY_ANNOTATIONS);
    			}
    			//Yusuf (21 Mar 2011) - Bisa insert logo di PDF
    			if(logo == 1){
    				com.itextpdf.text.pdf.PdfContentByte content = stamperNew.getOverContent(readerNew.getNumberOfPages());
    				com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(Resources.getResourceURL(props.getProperty("images.ttd.ekalife")));
    				image.setAbsolutePosition(20, reader.getPageSize(1).getHeight() - 50);
    				image.scaleToFit(150, 36);
    				content.addImage(image);
    			}
    			stamperNew.close();
            }
			
            if(sos!=null){
				sos.flush();
				sos.close();
			}
            if(reader!=null)reader.close();
            if(readerNew!=null)readerNew.close();	
            
			
		}catch(Exception de) {
			logger.error(de);
    		ServletOutputStream out = response.getOutputStream();
    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
    		out.flush();
		}
		document.close();
	}
	
	/**
	 * viewer surat rollover contoh : \\aplikasi\lions\pdf\18\18200600038
	 * dan disimpan oleh program PB nya rudy
	 * 
	 * @author Yusuf
	 * @since May 15, 2008 (5:09:22 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView dokumen_ro(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//http://ajsjava/E-Lions/common/util.htm?window=dokumen_ro&spaj=01200700020
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		int endors = ServletRequestUtils.getIntParameter(request, "endors", 0);
		String cabang = null;
		Map cmd = new HashMap();

		//init params
		String fileName;
		spaj = spaj.replace(".", "").trim();
		String spaj2 = elionsManager.selectSpajFromPolis(spaj);
		if(spaj2 != null) { //maka yg dimasukkin nomor polis
			spaj = spaj2;
		}
		String lsbs_id = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
		String folder = endors == 1 ? "Endorsment" : (lsbs_id==null ? "" : products.stableLink(lsbs_id) ? "StableLink" : "PowerSave");
		
		//tidak ada data
		if(lsbs_id == null) {
			cmd.put("pesan", "Silahkan masukkan nomor Polis / SPAJ yang benar.");
			return new ModelAndView("common/dokumen_ro", cmd);
		
		//download file
		}else if((fileName = request.getParameter("file")) != null) {
			cabang = elionsManager.selectCabangFromSpaj(spaj);
			String directory = props.getProperty("pdf.dir.rollover")+"\\"+folder+"\\"+cabang+"\\"+spaj;
			File cek = new File(directory+"\\"+fileName);
			if(!cek.exists()){
				String tahunbulan = elionsManager.selectBegDatePowerSave(spaj);
				directory = "\\\\storage\\"+folder+"\\"+cabang+"\\" + tahunbulan +"\\" +spaj;
			}
			FileUtils.downloadFile("inline;", directory, fileName, response);
			return null;
		
		//listing file di directory
		}else {
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			
			//menu ini, dipakai 2 macam user : user bank sinarmas, dan user cabang ajs
			if(currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 16){
				if(elionsManager.selectIsUserYangInputBank(spaj, Integer.valueOf(currentUser.getLus_id())) <= 0 && !currentUser.getCab_bank().trim().equals("SSS") && !currentUser.getName().toLowerCase().endsWith("simasprima")) {
					cmd.put("pesan","Anda tidak mempunyai akses terhadap Polis ini. Polis ini hanya dapat diakses oleh cabang " + 
						elionsManager.selectCabangBiiPolis(spaj));
				}
			}else if(currentUser.getJn_bank().intValue() == 3){
				if(elionsManager.selectIsUserYangInputSekuritas(spaj, Integer.valueOf(currentUser.getLus_id())) <= 0 && !currentUser.getCab_bank().trim().equals("M35")) {
					if(currentUser.getJn_bank().intValue() == 3){
						cmd.put("pesan","Anda tidak mempunyai akses terhadap Polis ini.");
					}
				}
			}else{
				if(!spaj.equals("")) {
					if("05,11,27,29".indexOf(currentUser.getLde_id())<=-1){
						if(uwManager.selectAksesUserTerhadapSpaj(spaj, currentUser.getLus_id()) == 0)
							cmd.put("pesan","Anda tidak mempunyai akses terhadap Polis ini");
					}
				}
			}
			
			//bila gak ada error
			if(cmd.get("pesan") == null){
				cabang = elionsManager.selectCabangFromSpaj(spaj);
				String directory = props.getProperty("pdf.dir.rollover")+"\\"+folder+"\\"+cabang+"\\"+spaj;
				String tahunbulan = elionsManager.selectBegDatePowerSave(spaj);
				String directory3 = "\\\\storage\\"+folder+"\\"+cabang+"\\" + tahunbulan +"\\" +spaj;
				List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
				List<DropDown> daftarFile2 = FileUtils.listFilesInDirectory(props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj);
				daftarFile.addAll(FileUtils.listFilesInDirectory(directory3));
				
				cmd.put("daftarFile", uwManager.selectDaftarFileRolloverAtauTopup(spaj, folder, daftarFile));
				cmd.put("daftarFile2", daftarFile2); 
			}

			cmd.put("spaj", spaj);
			cmd.put("folder", folder);
			
			return new ModelAndView("common/dokumen_ro", cmd);
			
		}		
	}
	
	public ModelAndView kurs(HttpServletRequest request, HttpServletResponse response) throws Exception {
		uwManager.schedulerDailyCurr();
		return null;
	}
	
	public ModelAndView summaryrk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Date tgl = uwManager.selectSysdateTruncated(-1);
		uwManager.schedulerSummaryRK(tgl);
		return null;
	}
	
	public ModelAndView outstandingbsm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		uwManager.schedulerOutstandingBSM();
		return null;
	}
		
	//E-Lions/common/util.htm?window=test
	public ModelAndView test(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");

		Map m = new HashMap();
		m.put("spaj", spaj);
		
		if(request.getParameter("hitung")!=null) {
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			bacManager.schedulerWelcomeCallCorona();
//			if(currentUser == null) {
//				currentUser = new User();
//				currentUser.setLus_id("574");
//			}s
			//currentUser = new User();
			//currentUser.setLus_id("2998");
			
			String result = bacManager.prosesofacscreening(spaj, 1, 0);
			System.out.println("result:"+result);
			
			List<String> daftarEspeaje = new ArrayList<String>();
			daftarEspeaje.add(spaj);
			//uwManager.schedulerpendingKartuAdmedika();
			//uwManager.schedulerWarningBac();
			//uwManager.schedulerAppointment();
			//uwManager.schedulerTransPolToUw(); // Andhika`s - Jalanin Auto Transfer Manual
//			uwManager.schedulerFollowUpValidPolis(); // Andhika`s - req: Novie
//			bacManager.schedulerAutomailSummaryFurther(); // Andhika`s - req: Inge
//			uwManager.schedulerDataHPlus1(); // Andhika`s - req: ningrum
			//uwManager.schedulerSummaryAkseptasiBancassSyariah();
			//uwManager.schedulerHistoryQuot();
			//uwManager.schedulerTargetListReport();
//			uwManager.schedulerRekapPembatalanUw();
			//uwManager.schedulerSummaryAkseptasi();
			/*bacManager.schedulerReportKycNewBussiness();*/
			//bacManager.schedulerNotProceedWith();
//			bacManager.schedulerNotProceedWithRecurring();
//			bacManager.schedulerWelcomeCall();	
//			String [] a = {"37201000963","37201000929","37201000949","37201000950","37201000955",
//						   "37201000959","37201000960","37201000964","37201000965","37201000930",
//						   "37201000935","37201000947","37201000945","37201000948","37201000951",
//						   "37201000952","37201000953","37201000954","37201000956","37201000957",
//						   "37201000958","37201000961","37201000962","37201000936","37201000937",
//						   "37201000938","37201000942","37201000943","37201000946"};
			
//			String [] a = {"37201107504","42201102377","42201102405","42201102321","37201107368",
//					   "05201101343","05201101344","05201101347","42201102338","42201102255",
//					   "11201100139","18201100213","18201100209","18201100216","05201101348",
//					   "05201101350","42201102334","05201101346","13201100483","11201100140",
//					   "18201100215","42201102330","18201100204","18201100206"};
			//a.
//			bacManager.sendAgenNAdmin2(spaj);
			/** 1. Hitung Komisi */
			//elionsManager.hitungKomisi(spaj, currentUser);
//			daftarEspeaje = bacManager.selectDaftarSpajYangMauDiProses();
//			Integer i = 0;
//			for(String s : daftarEspeaje){
//				bacManager.hitungReward(s, currentUser);
//				i++;
//			}
//			logger.info("Total SPAJ Reward yang diproses: "+i);
//			bacManager.hitungReward(spaj, currentUser);
//			elionsManager.hitungKomisiTopupOnly(spaj, currentUser);
//			daftarEspeaje = bacManager.selectDaftarSpajYangMauDiProses();
//			elionsManager.prosesKomisiBySelect(daftarEspeaje, currentUser);
	/*		String kd =elionsManager.cekSetifikatAgen("42201305643");
			
			if((!kd.equals(""))||(!kd.equals(""))||(!kd.equals(""))){
			logger.info(kd);
			}	*/
			
			/*String emailAgen = "test@sinarmasmsiglife.co.id";
			String []to;
			String []cc;
			 Map agen2 = elionsManager.selectEmailAgen(spaj);
			 String msagId = (String) agen2.get("MSAG_ID");
			 Map emailHybrid = bacManager.selectEmailHybrid(msagId);
			 String emailSiHybrid = (String) emailHybrid.get("EMAIL");
			 emailAgen = emailAgen +";"+ emailSiHybrid;
			 String[] emailToAgen=emailAgen.split(";");
			 to=emailToAgen;
			 String leaderFCD = props.getProperty("FCD.further_requirement_cc");
			 String[] emailCc = leaderFCD.split(";");
			 cc = emailCc;*/
			//
			//elionsManager.hitungUPP(spaj, 1);
						
//			String[] a = {
////					"37201401982", "37201400239", "37201402255", "37201402402","37201402094","52201400321","17201400073","37201402256","37201400418","37201401990",
////					"37201401622","37201401804","37201401051","37201402259","37201400849","37201402220","37201402327","52201400300","37201400959","52201400273",
////					"52201400211","37201402131","37201401987","37201401986","52201400329","37201402258","52201400303","22201400047","37201402262","52201400269",
////					"37201401103","37201402466","52201400323","37201402245","37201402532","37201402330","52201400261","37201402540","52201400271","37201402112",
//					"17201400055","52201400103","22201400048","37201402244","37201402326","17201400059","37201402092","37201401758","37201402229","17201400063",
//					"52201400328","52201400319","52201400299","52201400326"};
			
//			,"52201400329","37201402258","52201400303","22201400047","37201402262","52201400269",
//			"","","","","","","","","","",
//			"","","","","","","","","","",
//			"","","",""
			
//			
//			for(int x = 0; x< a.length; x++) {
//				elionsManager.hitungKomisi(a[x], currentUser);
//			}
			
			/** 2. Testing untuk Jurnal Produksi Individu / Unitlink dan stable */
//			elionsManager.testJurnalProduksiIndividu(spaj, currentUser);
			//elionsManager.testJurnalProduksiUnitLink(spaj, currentUser);
//			bacManager.ProsesJurnalIndividuKetinggalanNoPre(daftarEspeaje, currentUser);
//			bacManager.schedulerRefundPremiAuto();
			
			/** 3. Testing untuk sequence voucher premi individu*/
			//elionsManager.testSequenceVoucherPremiIndividu(spaj, currentUser);
			
			/** 4. Testing untuk transfer ke print polis*/
		//	elionsManager.testTransferToPrintPolis(spaj, currentUser);
			
			/** 5. cancel banyak polis guthrie*/
			//elionsManager.cancelBanyakPolisGuthrie(currentUser);
			
			/** 6. bulk transfer polis di ttp ke filling*/
			//elionsManager.transferBanyakPrintPolisKeFilling(currentUser);
			
			/** 7. proses banyak nilai tunai */
			
//			int hitung = 76700; //0
//			int penambah = 100; //
//			
//			while(true) {
//				logger.info("PROCESSING " + hitung + " - " + (hitung+penambah));
//				daftarEspeaje = elionsManager.selectDaftarSpajYangMauDiProsesNilaiTunai(hitung, hitung+penambah);
//				if(daftarEspeaje.isEmpty()) break;
//				elionsManager.prosesNilaiLama(daftarEspeaje, "", 0);
//				hitung += penambah;
//			}
			
			/** 8. proses nilai tunai dalam jumlah banyak (dari query) */
//			daftarEspeaje = elionsManager.selectDaftarSpajYangMauDiProsesNilaiTunai(0, 0);
//			daftarEspeaje.add("35200600003");
//			elionsManager.prosesNilaiLama(daftarEspeaje, "2258", 0);
			
			/** 9. proses email extra premi */
			//Command command = new Command();
			//command.setMsprTsi((double) 50000000);
			//command.setMsprPremium((double) 122850);
			//command.setLscbPayMode("Setiap Bulan");
			//command.setPeriode("Setiap Bulan");
			//command.setFlag_ut(1);
			//elionsManager.kirimEmail(
					//"05200800424", 4, 
					//props.getProperty("admin.yusuf"), 
					//props.getProperty("admin.yusuf"), 
					//"FURTHER REQUIREMENT", 
					//"17-06-2008 09:39:37", currentUser, 
					//"Ekstra Mortalita 50% untuk Payor", 
					//null, null, null, null, null, 1, "05", 
					//"H. FAISAL YAMAN", "ILMA SAFIRA", command, elionsManager.getUwDao().getDataSource().getConnection(), null);
			
			/** 10. Test Sequence */
			//logger.info(elionsManager.sequenceNoRegStableLink());
			
			/** 11. Proses Ulink Bill dan Ulink Det Bill yang ketinggalan */
			/*
			List<Map> daftar = elionsManager.selectSerbaGuna(
				"SELECT reg_spaj FROM eka.mst_ulink WHERE mu_ke = 1 " +
				"AND reg_spaj IN ( " +
				"SELECT reg_spaj FROM eka.mst_ulink a WHERE a.mu_tgl_trans >= '1 aug 2008' AND a.mu_ke = 1 " +
				"MINUS " + 
				"SELECT reg_spaj FROM eka.mst_ulink_bill) "
			);
			for(Map monyet : daftar) {
				elionsManager.prosesUlinkDetBill((String) monyet.get("REG_SPAJ"));
			}*/
			
			//String[] a = {"09200812069", "09200821985", "09200822271", "12200700120", "12200700121", "22200700768", "37200700949", "37200700951"};
			//for(String x : a) elionsManager.prosesUlinkDetBill(x);
			
			/** 12. Testing Sequence No Reg Stable Link */
//			for(int i=0; i<120; i++) {
//				logger.info(elionsManager.sequenceNoRegStableLink());
//			}
			
			/** 13. Testing Muamalat */
//			elionsManager.testMuamalat(currentUser);

			/** 14. cancel banyak polis */
			//uwManager.cancelBanyakPolis(currentUser);

			/** 15. summary terlambat cetak polis */
//			UwScheduler uws = new UwScheduler();
//			uws.setElionsManager(elionsManager);
//			uws.setUwManager(uwManager);
//			uws.setProps(props);
//			uws.setEmail(email);
//			uws.summaryTerlambatCetakPolis();
			
			/*
			 *topup
			 *
			daftarEspeaje = new ArrayList<String>();
			daftarEspeaje.add("10200800040");
			daftarEspeaje.add("22200800785");
			daftarEspeaje.add("24200800243");

			for(String reg_spaj : daftarEspeaje){
				uwManager.prosesRewardTertinggal(reg_spaj, currentUser);
			}*/
			
			/** 16. Testing Summary Akseptasi */
//			UwScheduler uw = new UwScheduler();
//			uw.setElionsManager(elionsManager);
//			uw.setUwManager(uwManager);
//			uw.setProps(props);
//			uw.setNumberFormat(NumberFormat.getNumberInstance());
//			uw.setEmail(email);
//			uw.setDateFormat(defaultDateFormat);
//			uw.setJdbcName("eka8i");
//			uw.summaryAkseptasi();
//			uwManager.schedulerSummaryAkseptasi();
			
			/** 17. Proses Generate SSU yg ketinggalan */
			/*
			daftarEspeaje = uwManager.selectStabilLinkAfterSept2009();
			for(String s : daftarEspeaje){
				File dir = new File(
						props.getProperty("pdf.dir.export") + "\\" +
						elionsManager.selectCabangFromSpaj(s) + "\\" +
						s);
				if(!dir.exists()) dir.mkdirs();
				
				Map data = uwManager.selectDataUsulan(s);
				Date begdate = (Date) data.get("MSTE_BEG_DATE");
				Date sep2009 = (new SimpleDateFormat("dd/MM/yyyy")).parse("10/09/2009");
				
				File from = null;
				if(begdate.before(sep2009)){
					from = new File(props.getProperty("pdf.dir.syaratpolis") + "\\164-002-KHUSUS_BSM_BEFORE1SEP.pdf");
				}else{
					from = new File(props.getProperty("pdf.dir.syaratpolis") + "\\164-002-KHUSUS_BSM_AFTER1SEP.pdf");
				}
				
				File to = new File(
						props.getProperty("pdf.dir.export") + "\\" +
						elionsManager.selectCabangFromSpaj(s) + "\\" +
						s + "\\" +
						props.getProperty("pdf.ssu") + ".pdf");
				FileUtils.copy(from, to);
				
				logger.info(from.toString());
				logger.info(to.toString());
			}
			*/
			
			/** 18. */
			//untuk stable link, harus cek apakah dalam 1 id_simultan, pernah punya stable link sebelumnya
			//bila memang punya, maka update tabel mst_policy (kolom old polis)
//			daftarEspeaje = uwManager.selectAllStableLink();
			//for(String s : daftarEspeaje){
			//	uwManager.prosesCekSimultanStableLink(s);
			//}
			
			/** 19. */
			//uwManager.prosesGenerateTextFileJatis();
			
			/** 20. INSERT BONUS KOMISI YG TERTINGGAL */
			//uwManager.prosesBonusStableLinkYangKetinggalan();
			
			/** 21. Generate All Stable Link yg SSU/SSK swine flu nya bermasalah */
			//uwManager.prosesSSKStableLinkKetinggalan();
			
			/** 22. Test Perhitungan Pajak Komisi yang Baru */
			//uwManager.testPerhitunganPajakNov2009();
			
			/** 23. Generate Komisi RollOver yang tertinggal krn agentnya dipromosi */
			//uwManager.prosesKomisiROAgentPromosiKetinggalanJanuari2010(spaj, currentUser);
			
			/** 24. Kirim Ulang Email Jatis yang tidak terkirim */
			//uwManager.kirimUlangEmailJatis();
			
			/** 25. Test Email ajsjava, ada masalah gak */
//			email.send(false, 
//					props.getProperty("admin.ajsjava"),
//					new String[] {props.getProperty("admin.yusuf"), "yusufsutarko@gmail.com"},
//					null, null,
//					"Subject", 
//					"Message", 
//					null);

			/** 26. Scheduler*/
//			Date tanggal = defaultDateFormat.parse("22/10/2014");
			//bacManager.schedulerPendingSmilePrioritas();
			//bacManager.tesProsesVa();
			//uwManager.schedulerSummaryRK(tanggal);
//			uwManager.schedulerBmi();
//			uwManager.schedulerKomisi30Hari();
			
			/** 27. Fungsi Simple untuk testing2 */
//			this.uwManager.test();
			
			/** 28. Create Endors yang tertinggal*/
//			uwManager.prosesEndorsemen(spaj, Integer.parseInt(uwManager.selectBusinessId(spaj)),0);
			
			/** 29 Generate Manual Nomor Kartu Simas Card*/
//			uwManager.test();
			
			/** 30 Generate data produksi/komisi cross selling agar bisa diproses finance */
//			List<PolicyCs> daftarSpaj = elionsManager.selectDaftarCrossSelling(null, "37201202158", null, null, null);
//			String pesan = elionsManager.saveKonfirmasiCrossSelling(daftarSpaj, Integer.valueOf(currentUser.getLus_id()));
			
			/** 31 Process Polis Batal yang tidak keinsert di production dan det_production*/
//			daftarEspeaje =bacManager.selectPolisBatalNonProduction();
//			for(String s : daftarEspeaje){
//				bacManager.prosesProductionBatal(tanggal, s);
//			}
			
//			uwManager.sendManualSoftcopyPas(currentUser);
			//uwManager.schedulerPenerimaanDanKlaimDanamasPrima();
//			uwManager.schedulerAutoCancelPolisMallAssurance();
			
			//Scheduler Daily Currency
//			uwManager.schedulerDailyCurr();
//			System.out.println("#######Scheduler Daily Currency#######");
			
//			uwManager.testEmail();
//			List<File> attachments = new ArrayList<File>();
//			attachments.add(new File("G:\\WebKerjasama.xls"));
//			
//			List<DropDown> listImageEmbeded=new ArrayList<DropDown>();
//			listImageEmbeded.add(new DropDown("promo","\\\\ebserver\\pdfind\\simascard\\logos\\100.jpg"));
//					
//			email.send(
//					false,
//					"berto@sinarmasmsiglife.co.id", 
//					new String[] { "berto@sinarmasmsiglife.co.id","brais_surya@yahoo.com"}, 
//					null, 
//					null,
//					"Testing with non html simple mail", 
//					"Testing", 
//					null);
//			
//			email.send(
//					true,
//					"berto@sinarmasmsiglife.co.id", 
//					new String[] { "berto@sinarmasmsiglife.co.id","brais_surya@yahoo.com"}, 
//					null, 
//					null,
//					"Testing with html simple mail", 
//					"Testing", 
//					null);
//			
//			email.sendImageEmbeded(
//					true,
//					"berto@sinarmasmsiglife.co.id", 
//					new String[] {"berto@sinarmasmsiglife.co.id","brais_surya@yahoo.com"}, 
//					null, 
//					null,
//					"Testing with html embeded mail", 
//					"plain text message <strong>apakah ini bold?</strong> <br/>" +
//					"<img src=\"cid:promo\" width=\"100px\"/>", 
//					attachments,listImageEmbeded);	
//			uwManager.schedulerResetCounter();
//			uwManager.schedulerOutstandingBSM();
//			uwManager.schedulerAksepSpt();
//			uwManager.testSequence();
			//bacManager.insertVisaCampTertinggal(spaj, currentUser);
		/*	String errorKdRek = elionsManager.cekKdBankRekAgen2(spaj);
			logger.info(errorKdRek);*/
			/** 32 - PERUBAHAN POSISI SNOWS */
			// Select SPAJ tergantung case error 
			/*List Polis = bacManager.selectSnowsError(687);
			Integer count = Polis.size();
			Integer lspd_id_uw = 202, lspd_id_collection = 212;

			for (int i=0; i<Polis.size();i++){
				HashMap inbox =(HashMap) Polis.get(i);
				String reg_spaj = (String) inbox.get("REG_SPAJ");
				
				//Untuk create_id
				String lus_id = bacManager.selectLusId(reg_spaj);	
				
				Policy policy = bacManager.selectMstPolicyAll(reg_spaj);
				Integer posisi = policy.getLspd_id();
				if(posisi==27){					
					bacManager.prosesSnows(reg_spaj, lus_id, lspd_id_uw, lspd_id_collection);
				}
				
			}*/
			/** END *//*
			DateFormat df = new SimpleDateFormat("ddMMyyyy~HH:mm:ss");
			Date today = Calendar.getInstance().getTime();        
			Date date = new Date();
			String reportDate = df.format(date);
			Boolean test=true;
			//test=FileUtils.writeFile("2258",reportDate);
			if(test){
				logger.info("true");
				FileUtils.readFile("2258", reportDate);
			}
		}*/
			/*String lca_id = elionsManager.selectCabangFromSpaj(spaj);
			String source = props.getProperty("pdf.dir.file_gadget") + "\\" + "201511000569"+ "\\spaj";
			String destination = props.getProperty("pdf.dir.export") + "\\" + lca_id+ "\\" + spaj;
			File src = new File(source);
			if(src.exists())
			{
				FileUtils.CopyDirectory(source, destination);
			}*/
			

		//	bacManager.prosesUWTransfer(spaj, currentUser);
			
			/** 33. PROSES URL SHORTENER*/	
			
//			String a = defaultDateFormatStripes.format(uwManager.selectTanggalLahirPemegang(spaj));
			
//			Integer mspo_provider= uwManager.selectGetMspoProvider(spaj);
//			bacManager.generateReport(request,mspo_provider,1 , elionsManager,uwManager,0,null);
//			logger.info(bacManager.prosesUrlShortener(spaj));
//			String a = bacManager.prosesUrlShortener(spaj);
//			m.put("message",a);
			
			//PDFViewer.genItextTemplate1(elionsManager, bacManager, props, "PolisAll.pdf", false, spaj, "212", "8");
		
			
			/** 34. Proses Komisi ERBE beserta Generate Jurnal Komisi Erbe 217 - 2*/
//			currentUser = new User();
//			currentUser.setLus_id("2306"); //set lus_id berdasarkan user yg trasnfer dari QA BAS - Prosess Checking
//			bacManager.prosesKomisiErbeAndJurnalKomisiManual(spaj, null, currentUser);
			/** 35. proses insert endorsment manual **/
			//uwManager.prosesEndorsemen(spaj, 200, 1);
		}
		return new ModelAndView("common/test_komisi", m);
	}
	
	
	public ModelAndView generateKartuPas(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map m=new HashMap();
		this.uwManager.test("");
		
		return null;
	}

	public ModelAndView updateStatusMstInsured(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map m=new HashMap();
		Integer mste_reas=0;
		Integer mste_backup=3;
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		Integer count=0;
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		if(currentUser.getLus_id().equals(props.getProperty("access.reas.utilities"))){
			count=elionsManager.prosesUpdateStatusMstInsured(mste_reas,mste_backup);
		}
		if(count>0)
			m.put("msg","Berhasil Update data sebanyak="+count);
		else
			m.put("msg","Tidak Berhasil Update");
		
		return new ModelAndView("common/updateStatusMstInsured", m);
	}
	
	/**Fungsi Untuk membenarkan data client History yang salah
	 * 		seperti namapp,namatt, alamat, kota
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView updateMstClientHistory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Map map=new HashMap();
		if(currentUser.getLus_id().equals(props.getProperty("access.reas.utilities"))){
			elionsManager.prosesUpdateMstClientHistory();
			map.put("msg","Berhasil");
		}
		return new ModelAndView("common/updateMstClientHistory", map);
	}
	
	/**
	 * Controller untuk export excel data nasabah (saat ini baru untuk sinarmas sekuritas saja)
	 * 
	 * @author Yusuf
	 * @since Jan 16, 2009 (9:08:49 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView data_nasabah(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Map model = new HashMap();
		
		String tanggalAwal = ServletRequestUtils.getRequiredStringParameter(request, "tanggalAwal");
		String tanggalAkhir = ServletRequestUtils.getRequiredStringParameter(request, "tanggalAkhir");
		String jenisReport = ServletRequestUtils.getRequiredStringParameter(request, "jenisReport");
		
		model.put("dataNasabah", uwManager.selectDataNasabahSinarmasSekuritas(currentUser.getCab_bank(), jenisReport, defaultDateFormat.parse(tanggalAwal), defaultDateFormat.parse(tanggalAkhir)));
		model.put("jenisReport", jenisReport);
		
		return new ModelAndView("xlsViewerDataNasabah", model);
	}	
	
	public ModelAndView reas_utilities(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Map map=new HashMap();
		String proses=ServletRequestUtils.getStringParameter(request, "proses","");
		String sysdate=FormatDate.toString(elionsManager.selectSysdate());
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tglAwal",sysdate);
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tglAkhir",sysdate);
		String tglAwal2=ServletRequestUtils.getStringParameter(request, "tglAwal2",sysdate);
		String tglAkhir2=ServletRequestUtils.getStringParameter(request, "tglAkhir2",sysdate);
		
		if(proses.equals("insert")){//insert ke tabel eka.m_reas_temp_new (khusus lsbs_id 810-819
			if(currentUser.getLus_id().equals(props.getProperty("access.reas.utilities"))){
				String begProdDate=tglAwal;
				String endProdDate=tglAkhir;
				//String begprodDate="01/01/2005";
				//String endProdDate="30/09/2007";
				//elionsManager.prosesInsertMReasTempNew(begProdDate,endProdDate);
				map.put("msg","Berhasil");
			}
		}else if(proses.equals("create")){//buat report rider link (setelah buat report update flag_ins
										  // di tabel eka.m_reas_temp_new jadi 0
			List lsHasil=new ArrayList(),lsTampil;
			if(currentUser.getLus_id().equals(props.getProperty("access.reas.utilities"))) {
				//lsHasil=elionsManager.prosesLaporanReasRiderLinkNew(null,null,1);
			}
			map.put("lsHasil", lsHasil);
			return new ModelAndView("xlsCreatorRiderLink", "lsHasil", lsHasil);
		}else if(proses.equals("create2")){//buat report rider link
										  //berdasarkan tanggal prod date
			List lsHasil=new ArrayList(),lsTampil;
			if(currentUser.getLus_id().equals(props.getProperty("access.reas.utilities"))) {
				//lsHasil=elionsManager.prosesLaporanReasRiderLinkNew(tglAwal2,tglAkhir2,2);
			}
			map.put("lsHasil", lsHasil);
			return new ModelAndView("xlsCreatorRiderLink", "lsHasil", lsHasil);
		}
		
		
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir",tglAkhir);
		map.put("tglAwal2",tglAwal2);
		map.put("tglAkhir2",tglAkhir2);
		return new ModelAndView("common/reas_utilities", map);
	}
		
	/**
	 * Proses Insert Ke table eka.mst_reins dan eka.mst_reins_product khusus link yang tidak terproses
	 *  : YUSUF-PERHATIAN !!! FUNGSI INI JANGAN DIPAKAI, ERROR SAAT INSERT SARNYA = 1000, HARUSNYA SARNYA RATE/1000 X TSI RIDER
	 *  : KALO MAU PAKE, TANYA GW DULU
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insert_reas(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Map map=new HashMap();
//		Map param=elionsManager.prosesInsertReasRiderLinkToReins();
//		List lsReport=(List)param.get("lsReport");
//		List lsErr=(List) param.get("lsErr");
//		logger.info("Data Tidak Sama..");
//		for(int i=0;i<lsErr.size();i++)
//			logger.info(lsErr.get(i));
//		
//		logger.info("Hasil");
//		
//		for(int i=0;i<lsReport.size();i++)
//			logger.info(lsReport.get(i));
//		
//		return new ModelAndView("xlsCreatorSpaj", "lsReport", lsReport);
		return null;
	}

	/**
	 * Proses insert reas (m_sar_temp, m_reas_temp, m_reas_temp_new)
	 * @author Yusuf
	 * @since May 29, 2008 (9:53:33 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertReas(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//http://yusufxp/E-Lions/common/util.htm?window=insertReas
		
		Reas reas=new Reas();
		reas.setLstbId(new Integer(1));
		String las_reas[]=new String[3];
		las_reas[0]="Non-Reas";
		las_reas[1]="Treaty";
		las_reas[2]="Facultative";
		reas.setCurrentUser((User) request.getSession().getAttribute("currentUser"));        

		String[] spajs = {
				"37201103003","32201100013","32201100012","37201102299","37201102317","37201102127","60201100002","37201101404",
				"05201100225","37201101134","37201101126","37201101035","37201100530","02201100429","37201100782","37201100312",
				"37201100844","37201100581","37201100390","37201100425","37201100232","37201100040","37201002815","37201002803",
				"37201002748","37201002720","37201002689","37201002636","37201002728","37201002345","37201002547","37201002513",
				"37201002611","37201002387","37201002563","37201002585","37201002439","37201002436","37201002412","37201002418",
				"05201000796","37201002229","37201002078","37201002197","37201002019","37201001887","37201001875","37201001847",
				"37201001748","37201001829","37201001789","37201001762","37201001766","37201001568","37201001603","37201001525",
				"37201001538","37201001506","37201001535","37201001458","37201001499","37201001460","37201001471","37201001302",
				"37201001258","37201001348","37201001144","37201001314","37201001297","37201001322","37201001359","37201001226",
				"32201000001","37201001127","37201001195","37201001194","37201001075","37201001213","37201001212","02201000201",
				"05201000443","37201001149","37201001138","37201001113","13201000090","37201001093","37201000851","37201000601",
				"37201000871","37201000672","37201000592","37201000849","37201000732","37201000721","37201000669","37201000577",
				"37201000586","37201000551","37201000516","37201000400","09201004096","37201000474","37201000485","37201000493",
				"37201000450","37201000402","37201000458","37201000447","37201000364","37201000335","37201000192","37201000193",
				"46201000002","22201000007","37201000268","37201000039","37201000074","37200902822","37200902824","37200902679",
				"15200900001","32200900050","37200902459","32200900049","37200902394","37200902315","37200902383","37200902281",
				"37200902206","37200902127","37200902090","37200901986","37200902015","37200901957","37200901855","37200901851",
				"37200901845"				
		};
		
		for(String spaj : spajs){
		
			reas.setSpaj(spaj);
			
			Map mPosisi=elionsManager.selectF_check_posisi(reas.getSpaj());
			Integer lspdIdTemp=(Integer)mPosisi.get("LSPD_ID");
			reas.setLspdId(lspdIdTemp);
			String lspdPosTemp=(String)mPosisi.get("LSPD_POSITION");
			//produk asm
			Map map=uwManager.selectDataUsulan(reas.getSpaj());
			Integer lsbsId=(Integer)map.get("LSBS_ID");

			//tertanggung
			Map mTertanggung=elionsManager.selectTertanggung(reas.getSpaj());
			reas.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
			reas.setMsteInsured((String)mTertanggung.get("MCL_ID"));
			reas.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
			
			Map mStatus=elionsManager.selectWfGetStatus(reas.getSpaj(),reas.getInsuredNo());
			reas.setLiAksep((Integer)mStatus.get("LSSA_ID"));
			reas.setLiReas((Integer)mStatus.get("MSTE_REAS"));
			if (reas.getLiAksep()==null) 
				reas.setLiAksep( new Integer(1));
			
			
			//dw1 //pemegang
			Policy policy=elionsManager.selectDw1Underwriting(reas.getSpaj(),reas.getLspdId(),reas.getLstbId());
			if(policy!=null){
				reas.setMspoPolicyHolder(policy.getMspo_policy_holder());
				reas.setNoPolis(policy.getMspo_policy_no());
				reas.setInsPeriod(policy.getMspo_ins_period());
				reas.setPayPeriod(policy.getMspo_pay_period());
				reas.setLkuId(policy.getLku_id());
				reas.setUmurPp(policy.getMspo_age());
				reas.setLcaId(policy.getLca_id());
				reas.setLcaId(policy.getLca_id());
				reas.setMste_kyc_date(policy.getMste_kyc_date());
			}
			
			elionsManager.prosesReasIndividuNew(reas, BindUtils.bind(request, reas, "cmd"));
		}
				
		return null;
	}
	
	/**
	 * Proses Insert Ke table eka.mst_reins dan eka.mst_reins_product type reas treaty dan facultative 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView insertLossMstReinsProduct(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map=new HashMap();
		//ServletRequestDataBinder binder = new ServletRequestDataBinder(map);
		//binder.bind(request);
		//BindingResult err = binder.getBindingResult();
		BindException err = null;
		//List lsSpaj=elionsManager.selectSpajLostInsertMstReinsProduct();
		List lsSpaj = new ArrayList();

		lsSpaj.add("09200807863");
//		lsSpaj.add("09200809532");
//		lsSpaj.add("35200800049");
//		lsSpaj.add("11200800087");
//		lsSpaj.add("11200800090");
//		lsSpaj.add("09200809565");
//		lsSpaj.add("09200807954");
//		lsSpaj.add("11200800111");
//		lsSpaj.add("09200808403");
//		lsSpaj.add("09200813546");
//		lsSpaj.add("18200800081");

		User currentUser = (User)request.getSession().getAttribute("currentUser");
		//if(currentUser.getLus_id().equals(props.getProperty("access.reas.utilities"))){
			elionsManager.prosesinsertLossMstReinsProduct(lsSpaj, currentUser, err);
			map.put("msg","Berhasil");
		//}
		return new ModelAndView("common/updateMstClientHistory", map);
	}
	
	/**
	 * Download kuitansi SSP
	 * @author Canpri
	 * @since 14 Dec 2012
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	//http://localhost/E-Lions/common/util.htm?window=kuitansi_ssp
	public ModelAndView kuitansi_ssp(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		
		Map cmd = new HashMap();
		SimpleDateFormat dt = new SimpleDateFormat("MM/yyyy");
		
		cmd.put("tgl", dt.format(elionsManager.selectSysdate()));
		
		//find pdf
		if(request.getParameter("search") != null) {
			String cabang =  ServletRequestUtils.getStringParameter(request, "cabang",null);
			String bulan = ServletRequestUtils.getStringParameter(request, "bulan","");
			
			if(cabang!=null){
				String bulan2 = bulan.replace("/", "");
				
				String directory = props.getProperty("pdf.dir.kuitansi_ssp")+"\\"+cabang+"\\";
				//List<DropDown> dokumen = FileUtils.listFilesInDirectoryStartsWith(directory, "KUITANSI_SSP");
				List<DropDown> dokumen = FileUtils.listFilesInDirectory(directory);
				
				//String dok = null;
				List dok = new ArrayList();
				
				if(!dokumen.isEmpty()){
					for(int i=0;i<dokumen.size();i++){
						DropDown dc = dokumen.get(i);
						Map m = new HashMap();
						
						String bln = dc.getKey();
						if(bln.indexOf("REKAP")<0)
							bln = bln.substring(bln.length()-19, bln.length()-13);
						if(bulan2.equals(bln)){
							 //dok = dc.getKey();
							m.put("dok", dc.getKey());
							
							dok.add(m);
						}
					}
				}
				
				if(dok.isEmpty())cmd.put("pesan", "Dokumen tidak ada");
				cmd.put("dokumen", dok);
				cmd.put("tgl", bulan);
				cmd.put("cab", cabang);
			}
		}

		//view pdf
		if((request.getParameter("file")) != null) {
			String cabang =  ServletRequestUtils.getStringParameter(request, "cab",null);
			String file =  ServletRequestUtils.getStringParameter(request, "file",null);
			String directory =props.getProperty("pdf.dir.kuitansi_ssp")+"\\"+cabang+"\\";
			FileUtils.downloadFile("inline;", directory, file, response);
			return null;
		
		}
			
		cmd.put("cabang", uwManager.selectCabangBSM());
	
		return new ModelAndView("common/kuitansi_ssp", cmd);
		
	}
	public ModelAndView bsim_ftp(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		
		Map cmd = new HashMap();
		SimpleDateFormat dt = new SimpleDateFormat("MM/yyyy");		
		String tabs="1";
		int prd=0;
		String directory=null;
		///untuk tab 1(bank sinarmas)
		List<DropDown> jenisProduk = new ArrayList<DropDown>();	
		jenisProduk.add(new DropDown("0","PILIH PRODUK"));
		jenisProduk.add(new DropDown("1","DANA SEJAHTERA"));
		jenisProduk.add(new DropDown("2","FREE PA"));		
		cmd.put("tgl", dt.format(elionsManager.selectSysdate()));
		String product =  ServletRequestUtils.getStringParameter(request, "jenisProduk",null);
		
		//untuk tab 2(ajs)		
		List<DropDown> jenisProduk1 = new ArrayList<DropDown>();	
		jenisProduk1.add(new DropDown("0","PILIH PRODUK"));
		jenisProduk1.add(new DropDown("1","DANA SEJAHTERA"));
		jenisProduk1.add(new DropDown("2","FREE PA"));				
		cmd.put("tgl1", dt.format(elionsManager.selectSysdate()));
		String product1 =  ServletRequestUtils.getStringParameter(request, "jenisProduk1",null);
		
		//untuk tab 3(File Sales)		
		List<DropDown> jenisProduk3 = new ArrayList<DropDown>();	
		jenisProduk3.add(new DropDown("1","File On Folder SALES"));
		String product3 =  ServletRequestUtils.getStringParameter(request, "jenisProduk3",null);

		
		//find pdf
		if(request.getParameter("search") != null) {			
			String bulan = ServletRequestUtils.getStringParameter(request, "bulan","");
			List dok = new ArrayList();	
			String bulan2 = bulan.replace("/", "");			
			bulan2=bulan2.substring(2, 6)+bulan2.substring(0, 2);	
			if(!product.equals("0")){
				if(product.equals("1"))	{
					//directory = props.getProperty("pdf.dir.server.dana_sejahtera")+"\\"+bulan2;
					directory = "/output/dana_sejahtera";
					
				}else{
					
					directory ="/output/free_pa";
   				    
   				 }	
				//List<DropDown> dokumen = FileUtils.listFilesInDirectory(directory);	
				List<DropDown> dokumen = FileUtils.listFilesInDirectorywithFtp(directory,"AJS","ajs123","10.39.7.8",21);
				
				if(!dokumen.isEmpty()){
					for(int i=0;i<dokumen.size();i++){
							DropDown dc = dokumen.get(i);
							Map m = new HashMap();	
							String blnUpload = dc.getValue().toString().substring(0, 6);
							String bln = dc.getKey().substring(0,6);
							if(bulan2.equals(blnUpload)){
								m.put("no", i+1);
								m.put("dok", dc.getKey());
								m.put("tgldok", dc.getValue());
								dok.add(m);	
							}
							//				
						}
					if(dok.isEmpty())cmd.put("pesan", "Dokumen tidak ada");
					}else{
							cmd.put("pesan", "Dokumen tidak ada");	
					}
				}else{
					cmd.put("pesan", "Silahkan Pilih Produk terlebih Dahulu");
				}
				cmd.put("jp", product);
				cmd.put("dokumen", dok);
			    cmd.put("tgl", bulan);		   
		  }
		
		if(request.getParameter("search1") != null) {
			tabs="2";
			String bulan = ServletRequestUtils.getStringParameter(request, "bulan1","");
			List dok = new ArrayList();	
			String bulan2 = bulan.replace("/", "");
			bulan2=bulan2.substring(2, 6)+bulan2.substring(0, 2);
		
			if(!product1.equals("0")){
				if(product1.equals("1"))	{		
					directory = props.getProperty("pdf.dir.server.dana_sejahtera")+"\\"+bulan2;
					prd=1;
				}else{
   				    directory =props.getProperty("pdf.dir.server.free_pa")+"\\"+bulan2;
   				    prd=2;
   				 }				
				List<DropDown> dokumen = FileUtils.listFilesInDirectory(directory);								
				if(!dokumen.isEmpty()){
					for(int i=0;i<dokumen.size();i++){
							DropDown dc = dokumen.get(i);
							Map m = new HashMap();
							String blnUpload = dc.getValue().toString().replace("/","").substring(4,8)+dc.getValue().toString().replace("/","").substring(2,4) ;
							//String bln = dc.getKey().substring(0,6);
							if(bulan2.equals(blnUpload)){
								m.put("no1", i+1);
								m.put("dok1", dc.getKey());
								m.put("tgldok1", dc.getValue());
								dok.add(m);	
							}										
						}
					if(dok.isEmpty())cmd.put("pesan", "Dokumen tidak ada");	
					}else{
						cmd.put("pesan", "Dokumen tidak ada");	
					}
				}else{
					cmd.put("pesan", "Silahkan Pilih Produk terlebih Dahulu");
				}			
				cmd.put("jp1", product1);
				cmd.put("dokumen1", dok);
			    cmd.put("tgl1", bulan);	
			    cmd.put("showTab", tabs);
		  }
		
		if(request.getParameter("search3") != null) {
			tabs="3";
			String bulan = ServletRequestUtils.getStringParameter(request, "bulan1","");
			List dok = new ArrayList();	
		
				directory = props.getProperty("pdf.dir.server.sales");
				List<DropDown> dokumen = FileUtils.listFilesInDirectory(directory);								
				if(!dokumen.isEmpty()){
					for(int i=1;i<dokumen.size();i++){
							DropDown dc = dokumen.get(i);
							Map m = new HashMap();
							String blnUpload = dc.getValue().toString().replace("/","").substring(4,8)+dc.getValue().toString().replace("/","").substring(2,4) ;
								m.put("no2", i);
								m.put("dok2", dc.getKey());
								m.put("tgldok2", dc.getValue());
								dok.add(m);	
						}
					if(dok.isEmpty())cmd.put("pesan", "Dokumen tidak ada");	
					}else{
						cmd.put("pesan", "Dokumen tidak ada");	
					}
				cmd.put("jp2", product1);
				cmd.put("dokumen3", dok);
			    cmd.put("tgl2", "03/2016");	
			    cmd.put("showTab", tabs);
		  }

		//view file	
		if((request.getParameter("file")) != null) {			
			String file =  ServletRequestUtils.getStringParameter(request, "file",null);
			String product2= ServletRequestUtils.getStringParameter(request, "product",null);
			String tabs1= ServletRequestUtils.getStringParameter(request, "tab",null);
			String bulan = ServletRequestUtils.getStringParameter(request, "bulan1","");
			String bulan2 = bulan.replace("/", "");
			bulan2=bulan2.substring(2, 6)+bulan2.substring(0, 2);		
			boolean succes=false;
			String directory2 =null;
			if(tabs1.equals("1")){				
				if(product2.equals("1")){
					directory = "/output/dana_sejahtera";
					directory2 =props.getProperty("pdf.dir.server.dana_sejahtera")+"\\"+bulan2;
				}else{
					directory = "/output/free_pa";
					directory2 =props.getProperty("pdf.dir.server.free_pa")+"\\"+bulan2;
				}
				succes=FileUtils.downloadFilewithFtp(directory,directory2, file, "AJS", "ajs123", "10.32.1.83", 21, response);
			}else if(tabs1.equals("2")){
				if(product2.equals("1")){
					directory2 =props.getProperty("pdf.dir.server.dana_sejahtera")+"\\"+bulan2;
				}else{
					directory2 =props.getProperty("pdf.dir.server.free_pa")+"\\"+bulan2;
				}
				FileUtils.downloadFile("inline;", directory2, file, response);
				
			}else{
				File to = new File(
						props.getProperty("pdf.dir.server.sales") + "\\" + "archived" +"\\"+ file );
				File from = new File(
						props.getProperty("pdf.dir.server.sales") + "\\" + file );
				String directoryHapus = props.getProperty("pdf.dir.server.sales");
				
					directory2 =props.getProperty("pdf.dir.server.sales");
					FileUtils.downloadFile("inline;", directory2, file, response);
					FileUtils.copy(from, to);
					FileUtils.deleteFile(directoryHapus, file);
			}
			if(succes){
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Data Berhasil Di Download');window.close();</script>");
    			sos.close();	
			}else{
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
			}
			return null;
			//return new ModelAndView(new RedirectView(request.getContextPath()+"/common/util.htm?window=bsim_ftp")).addObject("pesan", pesan2);
		}
	
		cmd.put("showTab", tabs);
		cmd.put("jenisProduk", jenisProduk);
		cmd.put("jenisProduk1", jenisProduk1);
		cmd.put("jenisProduk3", jenisProduk3);
		
		return new ModelAndView("common/bsim_ftp", cmd);
		
	}
	
	//file Downloader For Public
	//id ini hasil dari encript no SPAJ (contoh '12345'), untuk id jangan sampai ke encrypt dengan tanda # karena akan terjadi kegagalan pada saat pembentukan URL Link
	//E-Lions/common/util.htm?window=publics&id='12345'
	public ModelAndView publics(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = ServletRequestUtils.getStringParameter(request, "id", "");
		
		String spaj = elionsManager.selectEncryptDecrypt(id, "de");
				
		Map m = new HashMap();
		m.put("spaj", spaj);
		FileInputStream in = null;
		ServletOutputStream ouputStream = null;
		try{

			String lca_id = elionsManager.selectCabangFromSpaj(spaj);			
			
			String outputDir = props.getProperty("pdf.dir.export") +"\\"+ lca_id + "\\" + spaj + "\\";;
			
			response.setContentType("application/pdf");
		    response.setHeader("Content-Disposition", "Attachment;filename=Polis_Document.pdf");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			
			in = new FileInputStream(outputDir+"PolisSMS.pdf");			
		    ouputStream = response.getOutputStream();
		    
		    IOUtils.copy(in, ouputStream);
		    
		    return null;
		} catch (Exception e) {
			m.put("pesan", "Ada kesalahan dalam download Document Polis - Generated Form not found.");
			logger.error("ERROR :", e);
			
		}finally {
			try {
				if(in != null) {
					in.close();
				}
				if(ouputStream != null) {
					ouputStream.close();
				}
			}catch (Exception e) {
				logger.error("ERROR :", e);
			}
		}
		return new ModelAndView("common/filedownloader_public", m);
	}

}