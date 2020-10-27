package com.ekalife.elions.web.bac;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.MstInbox;
import com.ekalife.elions.model.MstInboxChecklist;
import com.ekalife.elions.model.MstInboxDet;
import com.ekalife.elions.model.MstInboxHist;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Scan;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentMultiController;
import com.google.gson.Gson;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;

/**
 * Controller untuk modul snows
 * @author alfian_h
 * @since 03/06/2015
 */
@SuppressWarnings("unchecked")
public class SnowsMultiController extends ParentMultiController {

//	protected BacManager bacManager;
	
	/**
	 * @author alfian_h
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @Link http://localhost:8080/E-Lions/snows/snows.htm?window=inputSnows
	 */
	
	Common common;
	
	@SuppressWarnings("null")
	public ModelAndView inputSnows(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<DropDown> jnsInput = new ArrayList<DropDown>();
		jnsInput = bacManager.selectLstJnJob();
		map.put("jnsInput", jnsInput);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//		Date now = bacManager.selectSysdate();
		Date now = new Date();
		
		if(request.getParameter("btnSave")!=null){
			String noPol = ServletRequestUtils.getStringParameter(request, "noPolis",null);
			String noSPAJ = ServletRequestUtils.getStringParameter(request, "noSPAJ",null);
			Integer jnsInbox = ServletRequestUtils.getIntParameter(request, "jnsInbox",0);
			String tgl_berkas_masuk = ServletRequestUtils.getStringParameter(request, "tgl_berkas_masuk",null);
			String tgl_berkas_lengkap = ServletRequestUtils.getStringParameter(request, "tgl_berkas_lengkap",null);
			String tgl_jt_tempo = ServletRequestUtils.getStringParameter(request, "tgl_jt_tempo",null);
			String tgl_konfirmasi = ServletRequestUtils.getStringParameter(request, "tgl_konfirmasi",null);
			String tgl_admin_terima = ServletRequestUtils.getStringParameter(request, "tgl_admin_terima",null);
			String desc = ServletRequestUtils.getStringParameter(request, "desc",null);
			String reff = ServletRequestUtils.getStringParameter(request, "reff",null);
			String lca_id = ServletRequestUtils.getStringParameter(request, "lca_id");
			Integer lstb_id = ServletRequestUtils.getIntParameter(request, "lstb_id");
//			Policy policy = bacManager.selectMstPolicyAll(noSPAJ);
//			Integer lstb_id_db = policy.getLstb_id();
//			if(lstb_id_db!=0 || lstb_id_db !=null){
//				if(lstb_id==lstb_id_db) lstb_id = lstb_id;
//				else{
//					map.put("pesan", "Tipe Bisnis tidak sama");
//					return new ModelAndView("snows/inputSnows", map);
//				}
//			}
			String check[] = request.getParameterValues("chkBox");
			String mi_id = bacManager.selectGenMIID();
			noSPAJ = noSPAJ.replace(".", "");
			Date tbm,tbl, tjt, tk, tat;
			if(tgl_berkas_masuk==null) tbm = null; else tbm = df.parse(tgl_berkas_masuk);
			if(tgl_berkas_lengkap==null) tbl = null; else tbl = df.parse(tgl_berkas_lengkap);
			if(tgl_jt_tempo==null) tjt = null; else tjt = df.parse(tgl_jt_tempo);
			if(tgl_konfirmasi==null) tk = null; else tk = df.parse(tgl_konfirmasi);
			if(tgl_admin_terima==null)tat = null; else tat = df.parse(tgl_admin_terima);
			
			if(jnsInbox==23){ //khusus jnsInbox 23 reff diset null 20191010 iga
				MstInbox mstInbox = new MstInbox(mi_id, jnsInbox, 207, null, null, null, null, noSPAJ, null, desc, 1, 
						tbm, tbl, null, null, Integer.valueOf(currentUser.getLus_id()), 
						now, null, null, lstb_id, tjt, tk, tat,0, null, null, null,null, 0,1);
				MstInboxHist mstInboxHist = new MstInboxHist(mi_id, 207, null, null, null, desc, Integer.valueOf(currentUser.getLus_id()), now, null,0,0);
			
				bacManager.insertMstInbox(mstInbox, mstInboxHist);
				if(request.getParameterValues("chkBox")!=null){
					for(int i=0;i<check.length;i++){
						String lc_id = check[i];
						String descChk = ServletRequestUtils.getStringParameter(request, "desc_"+lc_id, null);
						MstInboxChecklist inboxChecklist = new MstInboxChecklist(mi_id, jnsInbox, Integer.valueOf(lc_id), 1, descChk, Integer.valueOf(currentUser.getLus_id()), now);
						bacManager.insertMstInboxChecklist(inboxChecklist);
					}
				}
			}
			else{
			MstInbox mstInbox = new MstInbox(mi_id, jnsInbox, 207, null, null, null, null, noSPAJ, reff, desc, 1, 
					tbm, tbl, null, null, Integer.valueOf(currentUser.getLus_id()), 
					now, null, null, lstb_id, tjt, tk, tat,0, null, null, null,null, 0,1);
			MstInboxHist mstInboxHist = new MstInboxHist(mi_id, 207, null, null, null, desc, Integer.valueOf(currentUser.getLus_id()), now, null,0,0);
			
			bacManager.insertMstInbox(mstInbox, mstInboxHist);
			if(request.getParameterValues("chkBox")!=null){
				for(int i=0;i<check.length;i++){
					String lc_id = check[i];
					String descChk = ServletRequestUtils.getStringParameter(request, "desc_"+lc_id, null);
					MstInboxChecklist inboxChecklist = new MstInboxChecklist(mi_id, jnsInbox, Integer.valueOf(lc_id), 1, descChk, Integer.valueOf(currentUser.getLus_id()), now);
					bacManager.insertMstInboxChecklist(inboxChecklist);
				}
			}
			}
			@SuppressWarnings("rawtypes")
			List uncheck = bacManager.selectLstChecklist(String.valueOf(jnsInbox), mi_id);
			for(int i=0;i<uncheck.size();i++){
				Map n = (HashMap) uncheck.get(i);
				BigDecimal lc_id = (BigDecimal) n.get("LC_ID");

				MstInboxChecklist inboxChecklist = new MstInboxChecklist(mi_id, jnsInbox, Integer.valueOf(lc_id.toString()), 0, null, Integer.valueOf(currentUser.getLus_id()), now);
				bacManager.insertMstInboxChecklist(inboxChecklist);
			}
			map.put("pesan", "Data berhasil di simpan");
		}else if(request.getParameter("btnDel")!=null){
			String mi_id = ServletRequestUtils.getStringParameter(request, "hidNoInbox");
			String desc = ServletRequestUtils.getStringParameter(request, "desc");
			if(desc.equals("") || desc.isEmpty() || desc==null)desc = "Delete by user";
			MstInbox mstInbox = new MstInbox(mi_id, null, null, null, null, null, null, null, 
					null, desc, null, null, null, null, null, null, null, null, null, 
					null, null, null, null, null, 1, now, Integer.valueOf(currentUser.getLus_id()),null, 0,1);
			MstInboxHist mstInboxHist = new MstInboxHist(mi_id, null, null, null, null, desc, Integer.valueOf(currentUser.getLus_id()), now, null,0,0);
			
			bacManager.updateMstInbox(mstInbox);
			bacManager.insertMstInboxHist(mstInboxHist);
			map.put("pesan", "Data berhasil di delete");
		}else if(request.getParameter("btnEdit")!=null){
			String noPol = ServletRequestUtils.getStringParameter(request, "noPolis",null);
			String noSPAJ = ServletRequestUtils.getStringParameter(request, "noSPAJ",null);
			Integer jnsInbox = ServletRequestUtils.getIntParameter(request, "jnsInbox",0);
			String tgl_berkas_masuk = ServletRequestUtils.getStringParameter(request, "tgl_berkas_masuk",null);
			String tgl_berkas_lengkap = ServletRequestUtils.getStringParameter(request, "tgl_berkas_lengkap",null);
			String tgl_jt_tempo = ServletRequestUtils.getStringParameter(request, "tgl_jt_tempo",null);
			String tgl_konfirmasi = ServletRequestUtils.getStringParameter(request, "tgl_konfirmasi",null);
			String tgl_admin_terima = ServletRequestUtils.getStringParameter(request, "tgl_admin_terima",null);
			String desc = ServletRequestUtils.getStringParameter(request, "desc",null);
			String reff = ServletRequestUtils.getStringParameter(request, "reff",null);
			String lca_id = ServletRequestUtils.getStringParameter(request, "lca_id");
			Integer lstb_id = ServletRequestUtils.getIntParameter(request, "lstb_id");
			String check[] = request.getParameterValues("chkBox");
			String mi_id = ServletRequestUtils.getStringParameter(request, "noInbox",null);
			noSPAJ = noSPAJ.replace(".", "");
			Date tbm,tbl, tjt, tk, tat;
			if(tgl_berkas_masuk==null) tbm = null; else tbm = df.parse(tgl_berkas_masuk);
			if(tgl_berkas_lengkap==null) tbl = null; else tbl = df.parse(tgl_berkas_lengkap);
			if(tgl_jt_tempo==null) tjt = null; else tjt = df.parse(tgl_jt_tempo);
			if(tgl_konfirmasi==null) tk = null; else tk = df.parse(tgl_konfirmasi);
			if(tgl_admin_terima==null)tat = null; else tat = df.parse(tgl_admin_terima);

			MstInbox mstInbox = new MstInbox(mi_id, jnsInbox, 207, null, null, null, null, noSPAJ, reff, desc, 1, 
					tbm, tbl, null, null, Integer.valueOf(currentUser.getLus_id()), 
					now, null, null, lstb_id, tjt, tk, tat,0, null, null, null,null, 0,1);
			MstInboxHist mstInboxHist = new MstInboxHist(mi_id, 207, null, null, null, desc, Integer.valueOf(currentUser.getLus_id()), now, null,0,0);
			bacManager.updateMstInbox(mstInbox);
			bacManager.insertMstInboxHist(mstInboxHist);
			MstInboxChecklist inboxChecklist = new MstInboxChecklist(mi_id, jnsInbox, null, 0, null, Integer.valueOf(currentUser.getLus_id()), now);
			bacManager.updateMstInboxChecklist(inboxChecklist);
			if(request.getParameterValues("chkBox")!=null){
				for(int i=0;i<check.length;i++){
					String lc_id = check[i];
					String descChk = ServletRequestUtils.getStringParameter(request, "desc_"+lc_id, null);
					inboxChecklist = new MstInboxChecklist(mi_id, jnsInbox, Integer.valueOf(lc_id), 1, descChk, Integer.valueOf(currentUser.getLus_id()), now);
					bacManager.updateMstInboxChecklist(inboxChecklist);
				}
			}
			/*List uncheck = bacManager.selectLstChecklist(String.valueOf(jnsInbox), mi_id);
			for(int i=0;i<uncheck.size();i++){
				Map n = (HashMap) uncheck.get(i);
				BigDecimal lc_id = (BigDecimal) n.get("LC_ID");

				inboxChecklist = new MstInboxChecklist(mi_id, jnsInbox, Integer.valueOf(lc_id.toString()), 0, null, Integer.valueOf(currentUser.getLus_id()), now);
				bacManager.updateMstInboxChecklist(inboxChecklist);
			}*/
			map.put("pesan", "Data berhasil di update");
		}
		
		map.put("dataInbox", bacManager.selectListLeft(Integer.valueOf(currentUser.getLus_id()), currentUser.getLde_id()));
		return new ModelAndView("snows/inputSnows", map);
	}
	
	@SuppressWarnings("rawtypes")
	public ModelAndView transfer(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		HashMap map = new HashMap();
		map.put("masuk", 1);
		String desc = ServletRequestUtils.getStringParameter(request, "desc",null);
		String transKe = ServletRequestUtils.getStringParameter(request, "transKe","203");
		String mi_id = ServletRequestUtils.getStringParameter(request, "mi_id",null);
//		Date now = bacManager.selectSysdate();
		Date now = new Date();
		Boolean flag_hardcopy = false;
		
		if(request.getParameter("trans")!=null){
			// query update mst_inbox dan insert mst_inbox_hint by mi_id
			map.put("masuk", 2);
			String jmlh = bacManager.selectCountChecklist(mi_id, "1");
			String ljj_folder = null, lca_id = null, spaj = null, lstb_id = null, dftrFile = "", lspd_id = null;
			Integer lc_id;
			List ni = bacManager.selectNmFileChecklist(mi_id, null);
			if(!ni.isEmpty()){
				for(int i=0;i<ni.size();i++){
					Map a_ni = (HashMap) ni.get(i);
					ljj_folder = (String) a_ni.get("LJJ_FOLDER");
					lca_id = (String) a_ni.get("LCA_ID");
					spaj = (String) a_ni.get("REG_SPAJ");
					BigDecimal bd_lstb_id = (BigDecimal) a_ni.get("LSTB_ID");
					lstb_id = bd_lstb_id.toString();
					BigDecimal bd_lc_id = (BigDecimal) a_ni.get("LC_ID");
					BigDecimal bd_lspd_id = (BigDecimal) a_ni.get("LSPD_ID");
					lspd_id = bd_lspd_id.toString();
					String lc_nama = (String) a_ni.get("LC_NAMA");
					dftrFile = dftrFile +", "+ lc_nama;
					lc_id = bd_lc_id.intValue();
					if(lc_id==25){
							flag_hardcopy = true;					
					}
				}
				dftrFile = dftrFile.substring(2, dftrFile.length()); 
			}else{
				ni = bacManager.selectMstInbox(mi_id,null, Integer.valueOf(currentUser.getLus_id()), currentUser.getLde_id());
				for(int i=0;i<ni.size();i++){
					Map a_ni = (HashMap) ni.get(i);
					ljj_folder = (String) a_ni.get("LJJ_FOLDER");
					lca_id = (String) a_ni.get("LCA_ID");
					spaj = (String) a_ni.get("REG_SPAJ");
					BigDecimal bd_lstb_id = (BigDecimal) a_ni.get("LSTB_ID");
					lstb_id = bd_lstb_id.toString();
					BigDecimal bd_lspd_id = (BigDecimal) a_ni.get("LSPD_ID");
					lspd_id = bd_lspd_id.toString();
				}
			}
			ljj_folder = ljj_folder+"_"+mi_id;
			String dest = null;
			if(lstb_id=="2"){
				dest = props.getProperty("pdf.dir.uploadmri");
			}else{
				dest = props.getProperty("pdf.dir.uploadind");
			}
			File path = new File(dest+"\\"+lca_id.trim()+"\\"+spaj.trim()+"\\"+ljj_folder);
			File[] fList = path.listFiles();
			if(fList!=null && !ni.isEmpty()){
				if(fList.length>=Integer.valueOf(jmlh)){
					if(mi_id!=null){
						if(flag_hardcopy==true){
							MstInbox mstInbox = new MstInbox(mi_id, null, Integer.valueOf(transKe), null, 
									207, null, null, null, null, desc, null, null, null, Integer.valueOf(currentUser.getLus_id()), 
									now, null, null, null, null, null, null, null, null, null, null, null, null, 1, 0,null);	
							bacManager.updateMstInbox(mstInbox);
						}
						else{
							MstInbox mstInbox = new MstInbox(mi_id, null, Integer.valueOf(transKe), null, 
									207, null, null, null, null, desc, null, null, null, Integer.valueOf(currentUser.getLus_id()), 
									now, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0,null);	
							bacManager.updateMstInbox(mstInbox);
						}
						MstInboxHist inboxHist = new MstInboxHist(mi_id, 207, Integer.valueOf(transKe), null, null, "Transfer Inbox Cabang", Integer.valueOf(currentUser.getLus_id()), now, null,0,0);
						bacManager.insertMstInboxHist(inboxHist);
						map.put("pesan", "Berhasil di transfer");
						String cur_pos = bacManager.selectLstDocumentPositionByLSPDID(lspd_id);
						String next_pos = bacManager.selectLstDocumentPositionByLSPDID(transKe);
						String dial_ke = (String) bacManager.selectMaxMscsfDialKe(spaj.replace(".", ""));
						if (dial_ke == "0" || dial_ke == null) dial_ke = "1";
						else {
							int x = Integer.valueOf(dial_ke) + 1;
							dial_ke = String.valueOf(x);
						}
						String mscsf_ket = "Terima berkas asli berupa : "+dftrFile+". Data transfer ke "+next_pos+". No inbox SNOWS "+mi_id;
						bacManager.insertMstCsfDial(mscsf_ket, spaj, dial_ke, "B", currentUser.getLus_id(), null, "0", null, null, null, null);
						map.put("dataInbox", bacManager.selectListLeft(Integer.valueOf(currentUser.getLus_id()), currentUser.getLde_id()));
					}
				}else{
					map.put("pesan", "Jumlah file tidak sama dengan data yang di upload");
				}
			}else if((fList==null || ni.isEmpty())){
				map.put("pesan", "File upload tidak ditemukan");
			}
		}
		map.put("mi_id", mi_id);
		return new ModelAndView("snows/transfer",map);
	}
	
	public ModelAndView listWithdrawSlink(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		HashMap map = new HashMap();
		String noSPAJ = ServletRequestUtils.getStringParameter(request, "noSPAJ");
		noSPAJ = noSPAJ.replace(".", "");
		
		map.put("dataList", bacManager.selectWithdrawStableLink(noSPAJ));
		return new ModelAndView("snows/listWithdrawSlink",map);
	}
	
	public ModelAndView listReturSlink(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		HashMap map = new HashMap();
		String noSPAJ = ServletRequestUtils.getStringParameter(request, "noSPAJ");
		noSPAJ = noSPAJ.replace(".", "");
		
		map.put("dataList", bacManager.selectReturSLink(noSPAJ));
		return new ModelAndView("snows/listReturSlink",map);
	}
	
	public ModelAndView listEndorsSlink(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		HashMap map = new HashMap();
		String noSPAJ = ServletRequestUtils.getStringParameter(request, "noSPAJ");
		noSPAJ = noSPAJ.replace(".", "");
		
		map.put("dataList", bacManager.selectEndorsmentRollover(noSPAJ));
		return new ModelAndView("snows/listEndorsSlink",map);
	}
	
/*	public ModelAndView listWithdrawUlink(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap map = new HashMap();
		
		return new ModelAndView("snows/listWithdrawUlink",map);
	}*/
	
/*	public ModelAndView listSwitchingUlink(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap map = new HashMap();
		
		return new ModelAndView("snows/listSwitchingUlink",map);
	}*/
	
	public ModelAndView upload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		HashMap map = new HashMap();
//		Date now = bacManager.selectSysdate();
		Date now = new Date();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj",null);
		String mi_id = ServletRequestUtils.getStringParameter(request, "mi_id",null);
//		String lca_id = ServletRequestUtils.getStringParameter(request, "lca_id");
		String ljj_id = ServletRequestUtils.getStringParameter(request, "ljj_id");
		String lstb_id = ServletRequestUtils.getStringParameter(request, "lstb_id");
		spaj = spaj.replace(".", "");
		Policy policy = bacManager.selectMstPolicyAll(spaj);
		String lca_id = policy.getLca_id();			
		
		List a = bacManager.selectMstInboxChecklist(mi_id, null);
		/*List array = new ArrayList();
		for(int i=0; i<a.size(); i++){
			Map mp = (HashMap) a.get(i);
			Map cm = new HashMap();
			BigDecimal lc_id = (BigDecimal) mp.get("LC_ID");
			BigDecimal mi_flag = (BigDecimal) mp.get("MI_FLAG");
			String desc = (String) mp.get("MI_DESC");
			String lc_nama = (String) mp.get("LC_NAMA");
			Boolean chk = null;
			if(mi_flag.intValue() == 1) chk = true;
			else chk = false;
			
			cm.put("ljj_id", ljj_id);
			cm.put("lc_id", lc_id);
			cm.put("mi_flag", mi_flag);
			cm.put("desc", desc);
			cm.put("chk", chk);
			cm.put("lc_nama", lc_nama);
			
			array.add(cm);
		}
		map.put("chkList", array);*/
		map.put("mi_id", mi_id);
		map.put("spaj", spaj);
//		map.put("lca_id", lca_id);
		map.put("ljj_id", ljj_id);
		map.put("lstb_id", lstb_id);
		List dtck = bacManager.selectMstInboxChecklist(mi_id, "1");
		map.put("dataCheckList", dtck);
		
		if(!Common.isEmpty(request.getParameter("upload"))){
			Upload upload = new Upload();
			upload.setDaftarFile(new ArrayList<MultipartFile>(a.size()));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
//			MultipartFile mf = upload.getFile1();
//			String fileName = mf.getOriginalFilename();
//			String format = fileName.substring(fileName.length()-3, fileName.length()).toUpperCase();
			String dftrFile = "";	
			for(int i=0; i<upload.getDaftarFile().size();i++){
				MultipartFile file = (MultipartFile) upload.getDaftarFile().get(i);
				String fileName = file.getOriginalFilename();
				String format = null;
				if(fileName==null || fileName.isEmpty() || fileName.equals("")){
//					map.put("pesan", "Pilihan file tidak boleh kosong");
//					return new ModelAndView("snows/upload",map);
				}else{
					format = fileName.substring(fileName.length()-3, fileName.length()).toUpperCase();
					String dest = null;
					
					if(format.equals("PDF")){
						if(lstb_id=="2"){
							dest = props.getProperty("pdf.dir.uploadmri");
						}else{
							dest = props.getProperty("pdf.dir.uploadind");
//							dest = props.getProperty("pdf.dir.export");
						}
					}else{
						map.put("pesan", "Format File Harus PDF");
					}
					
					String lc_id = ServletRequestUtils.getStringParameter(request, "lc_id"+i);
					List ni = bacManager.selectNmFileChecklist(mi_id, lc_id);
					
					String ljj_folder = null, ljj_file = null;
					for(int in=0;in<ni.size();in++){
						Map a_ni = (HashMap) ni.get(in);
						ljj_folder = (String) a_ni.get("LJJ_FOLDER");
						ljj_file = (String) a_ni.get("NMFILE");
						lca_id = (String) a_ni.get("LCA_ID");
					}
					ljj_folder = ljj_folder+"_"+mi_id;
					
					File path = new File(dest+"\\"+lca_id.trim()+"\\"+spaj.trim()+"\\"+ljj_folder);
					if(!path.exists()) path.mkdirs();
					File[] fList = path.listFiles();
					if(fList.length>0){
						int pre = 0;
						for(File files : fList){
							String fn = files.getName();
							String part[] = fn.split("_");
							String bag0 = part[0];
							String bag1 = "";
							if (part.length>1 ){
								bag1 = part[1];
								String nm = bag1.substring(0, bag1.length()-4).toUpperCase();
								
								if(bag0.matches(ljj_file)){
									int n_nm = Integer.valueOf(nm);
									if(n_nm>pre){
										pre = n_nm;
									}
								}
							}
						}
						if(pre>0){
							pre += 1;
							fileName = ljj_file+"_"+FormatString.rpad("0", String.valueOf(pre), 3)+".PDF";
						}else{
							fileName = ljj_file+"_001.PDF";
						}
					}else{
						fileName = ljj_file+"_001.PDF";
					}
					path = new File(path+"\\"+fileName);
					FileUtils.writeByteArrayToFile(path, file.getBytes());
					dftrFile = dftrFile+", "+fileName;
				}
			}
			dftrFile = dftrFile.substring(2, dftrFile.length());
			MstInboxHist inboxHist = new MstInboxHist(mi_id, 207, null, null, null, "Upload berkas ( "+ dftrFile +" )", Integer.valueOf(currentUser.getLus_id()), now, null,0,0);
			bacManager.insertMstInboxHist(inboxHist);
			map.put("pesan", "File berhasil di upload");
		}
		return new ModelAndView("snows/upload",map);
	}
	
	public ModelAndView viewer(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj",null).trim();
		spaj = spaj.replace(".", "");
		String mi_id = ServletRequestUtils.getStringParameter(request, "mi_id",null).trim();
//		String lc_id = ServletRequestUtils.getStringParameter(request, "lc_id");
		Policy policy = bacManager.selectMstPolicyAll(spaj);
		String lca_id = policy.getLca_id();
		String lstb_id = ServletRequestUtils.getStringParameter(request, "lstb_id");
		String dest = null;
		if(lstb_id=="2"){
			dest = props.getProperty("pdf.dir.uploadmri");
		}else{
			dest = props.getProperty("pdf.dir.uploadind");
		}
		
		List list = new ArrayList();
		for(int i=1;i<=2;i++){
			list.add(i);
		}
		map.put("perulangan", list);
		List ni = bacManager.selectNmFileChecklist(mi_id, null);
		String ljj_folder = null, ljj_file = null;
		for(int i=0;i<ni.size();i++){
			Map a_ni = (HashMap) ni.get(i);
			ljj_folder = (String) a_ni.get("LJJ_FOLDER");
			ljj_file = (String) a_ni.get("NMFILE");
			lca_id = (String) a_ni.get("LCA_ID");
		}
		
		ljj_folder = ljj_folder+"_"+mi_id;
		
		String directory = dest+"\\"+lca_id+"\\"+spaj+"\\"+ljj_folder;
		List<DropDown> daftarFile = com.ekalife.utils.FileUtils.listFilesInDirectory(directory);
		map.put("spaj", spaj);
		map.put("mi_id", mi_id);
		map.put("lca_id", lca_id);
		map.put("lstb_id", lstb_id);
		map.put("daftarFile", daftarFile);
		
		return new ModelAndView("snows/view_doc_list",map);
	}
	
	public ModelAndView hist(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		String mi_id = ServletRequestUtils.getStringParameter(request, "mi_id",null);
		List a = bacManager.selectMstInboxHist(mi_id);
		map.put("hist", bacManager.selectMstInboxHist(mi_id));
		return new ModelAndView("snows/history",map);
	}
	
	
	public ModelAndView load_viewer(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj",null);
		String mi_id = ServletRequestUtils.getStringParameter(request, "mi_id",null);
		String lca_id = ServletRequestUtils.getStringParameter(request, "lca_id",null);
		String lstb_id = ServletRequestUtils.getStringParameter(request, "lstb_id",null);
		String dest = null;
		if(lstb_id=="2"){
			dest = props.getProperty("pdf.dir.uploadmri");
		}else{
			dest = props.getProperty("pdf.dir.uploadind");
		}
		List ni = bacManager.selectNmFileChecklist(mi_id, null);
		String ljj_folder = null, ljj_file = null;
		for(int i=0;i<ni.size();i++){
			Map a_ni = (HashMap) ni.get(i);
			ljj_folder = (String) a_ni.get("LJJ_FOLDER");
			ljj_file = (String) a_ni.get("NMFILE");
			lca_id = (String) a_ni.get("LCA_ID");
		}
		
		ljj_folder = ljj_folder+"_"+mi_id;
		String directory = dest+"\\"+lca_id+"\\"+spaj+"\\"+ljj_folder;
		
		String fileName = null;
		if((fileName = request.getParameter("file")) != null) {
			Document document = new Document();
			try{
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
			}catch(DocumentException de){
				logger.error(de);
	    		ServletOutputStream out = response.getOutputStream();
	    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
	    		out.flush();
			}
			if(document!=null){
				document.close();
			}
		}
		return null;
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @Link http://localhost:8080/E-Lions/snows/snows.htm?window=ajax&pages=?&jn=?&val=?
	 */
	public ModelAndView ajax(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		HashMap m = new HashMap();
		List array = new ArrayList();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//		Date now = bacManager.selectSysdate();
		Date now = new Date();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		
		String pg = ServletRequestUtils.getStringParameter(request, "pages", null);
		String jn = ServletRequestUtils.getStringParameter(request, "jn", null);
		String val = ServletRequestUtils.getStringParameter(request, "val", "0");
		Object result = null;
		
		if("inputSnows".equals(pg)){
			String lstb_id = ServletRequestUtils.getStringParameter(request, "lstb_id", null);
			Map data = new HashMap();
			data.put("peringatan", "");
			
			if("noPolis".equals(jn) || "noSPAJ".equals(jn)){
//				if(!lstb_id.equals("11")){
				if(lstb_id!="11"){
					val = val.replace(".", "");
					if("noPolis".equals(jn)){
//						result = bacManager.selectMstPolicy(val, null);
						array = bacManager.selectMstPolicy(val, null);
						data.put("dataInputSnows", array);
						String a = bacManager.selectCheckPending(null, val, lstb_id);
						if(Integer.valueOf(a)>0) data.put("pesan", "Terdapat "+a+" POLIS pending. Mohon agar dapat lebih diperhatikan.");
					}else if("noSPAJ".equals(jn)){
//						result = bacManager.selectMstPolicy(null, val);
						array = bacManager.selectMstPolicy(null, val);
						data.put("dataInputSnows", array);
						String a = bacManager.selectCheckPending(val, null, lstb_id);
						if(Integer.valueOf(a)>0) data.put("pesan", "Terdapat "+a+" SPAJ pending. Mohon agar dapat lebih diperhatikan.");
					}
				}else{
					if("noPolis".equals(jn)){
//						result = bacManager.selectMstTmms(val, null);
						array = bacManager.selectMstTmms(val, null);
						data.put("dataInputSnows", array);
						String a = bacManager.selectCheckPending(null, val, lstb_id);
						if(Integer.valueOf(a)>0) data.put("pesan", "Terdapat "+a+" POLIS pending");
					}else if("noSPAJ".equals(jn)){
						val = val.replace(".", "");
//						result = bacManager.selectMstTmms(null, val);
						array = bacManager.selectMstTmms(null, val);
						data.put("dataInputSnows", array);
						String a = bacManager.selectCheckPending(val, null, lstb_id);
						if(Integer.valueOf(a)>0) data.put("pesan", "Terdapat "+a+" SPAJ pending");
					}
				}
				result = data;
			}
			else if("jnsInbox".equals(jn)){
				array = new ArrayList();
				int ljj_id = ServletRequestUtils.getIntParameter(request, "ljj_id");
				String noSPAJ = ServletRequestUtils.getStringParameter(request, "noSPAJ");
				noSPAJ = noSPAJ.replace(".", "");
//				result = bacManager.selectLstChecklist(String.valueOf(ljj_id), null);
				m.put("checkList", bacManager.selectLstChecklist(String.valueOf(ljj_id), null));
				Date jt_tempo = null; 
				String no_reff = null;
				switch (ljj_id) {
				case 5: case 6: case 7: case 8: case 9: case 10: // tahapan
					if (lstb_id != "11"){
						String jmlh = bacManager.selectJmlhTahapan(noSPAJ, String.valueOf(ljj_id));
						
						if(Integer.valueOf(jmlh) >= 1){
							//randy -- untuk tahapan, dapat memilih tahapan mana yang mau diproses, pilihnya dari tgl jt tempo
							String tgl_jt = ServletRequestUtils.getStringParameter(request, "tgl_jt_tempo","");
							
							if(tgl_jt.equals("")){
								List<DropDown> pilihan = new ArrayList<DropDown>();
								pilihan = bacManager.selectJtTempoTahapanMulti(noSPAJ, String.valueOf(ljj_id));
								data.put("pilihan", pilihan);
							}else{
								List a = bacManager.selectJtTempoTahapan(noSPAJ, String.valueOf(ljj_id),tgl_jt);
								for(int i=0;i<a.size();i++){
									Map ma = (HashMap) a.get(i);
									jt_tempo = (Date) ma.get("jt_tempo");
									no_reff = (String) ma.get("no_reff");
									data.put("jt_tempo", df.format(jt_tempo));
									data.put("no_reff", no_reff);
								}
								
								if((jt_tempo==null) || (jt_tempo == df.parse("01/01/1900"))){
									//khusus multi invest, maka isi tgl jt tempo nya beg date insured + 5 tahun
									List b = bacManager.selectMutliInvest(noSPAJ);
									int count = (Integer) b.get(0);
									if(count > 0){
										Date jt = bacManager.selectBegDateInsured(noSPAJ, String.valueOf(ljj_id));
									}
								}
							}
						}else{
							data.put("peringatan", "Tidak ada data TAHAPAN yang dapat diproses!\nNO. REFF tidak akan muncul!");
						}
					}else if(lstb_id == "11"){
						String jmlh = bacManager.selectTmmsTHP(noSPAJ, String.valueOf(ljj_id));
						if(Integer.valueOf(jmlh)==1){
							List a = bacManager.selectJtTempoTmmsTHP(noSPAJ, String.valueOf(ljj_id));
							for(int i=0;i<a.size();i++){
								Map ma = (HashMap) a.get(i);
								jt_tempo = (Date) ma.get("jt_tempo");
								no_reff = (String) ma.get("no_reff");
								data.put("jt_tempo", df.format(jt_tempo));
								data.put("no_reff", no_reff);
							}
							if((jt_tempo==null) || (jt_tempo == df.parse("01/01/1900"))){
								//khusus multi invest, maka isi tgl jt tempo nya beg date insured + 5 tahun
								List b = bacManager.selectTmmsDet(noSPAJ);
								int count = (Integer) b.get(0);
								if(count > 0){
									Date jt = bacManager.selectBegDateInsured(noSPAJ, String.valueOf(ljj_id));
								}
							}
						}
					}
					break;
				case 13: case 14: case 15: case 16: case 17: case 18: // simpanan -- dapat memilih simpanan mana yang mau diproses, pilihnya dari tgl jt tempo (end date)
					String jmlh = bacManager.selectJmlhSimpanan(noSPAJ, String.valueOf(ljj_id));
					if(Integer.valueOf(jmlh)>=1){
						String tgl_jt = ServletRequestUtils.getStringParameter(request, "tgl_jt_tempo","");
						
						if(tgl_jt.equals("")){
							List<DropDown> pilihan = new ArrayList<DropDown>();
							pilihan = bacManager.selectJtTempoSimpananMulti(noSPAJ, String.valueOf(ljj_id));
							data.put("pilihan", pilihan);
						}else{
							List a = bacManager.selectJtTempoSimpanan(noSPAJ, String.valueOf(ljj_id), tgl_jt);
							for(int i=0;i<a.size();i++){
								Map ma = (HashMap) a.get(i);
								jt_tempo = (Date) ma.get("jt_tempo");
								no_reff = (String) ma.get("no_reff");
								data.put("jt_tempo", df.format(jt_tempo));
								data.put("no_reff", no_reff);
							}
						}
					}else{
						data.put("peringatan", "Tidak ada data SIMPANAN yang dapat diproses!\nNO. REFF tidak akan muncul!");
					}
					break;
				case 11: // maturity
					if (lstb_id != "11"){
						String jmlh1 = bacManager.selectJmlhMaturity(noSPAJ, String.valueOf(ljj_id));
						if(Integer.valueOf(jmlh1)==1){
							List a = bacManager.selectJtTempoMaturity(noSPAJ, String.valueOf(ljj_id));
							for(int i=0;i<a.size();i++){
								Map ma = (HashMap) a.get(i);
								jt_tempo = (Date) ma.get("jt_tempo");
								no_reff = (String) ma.get("no_reff");
								data.put("jt_tempo", df.format(jt_tempo));
								data.put("no_reff", no_reff);
							}
						}
					}else if (lstb_id == "11"){
						String jmlh1 = bacManager.selectTmmsMaturity(noSPAJ, String.valueOf(ljj_id));
						if(Integer.valueOf(jmlh1)==1){
							List a = bacManager.selectJtTempoTmmsMaturity(noSPAJ, String.valueOf(ljj_id));
							for(int i=0;i<a.size();i++){
								Map ma = (HashMap) a.get(i);
								jt_tempo = (Date) ma.get("jt_tempo");
								no_reff = (String) ma.get("no_reff");
								data.put("jt_tempo", df.format(jt_tempo));
								data.put("no_reff", no_reff);
							}
						}
					}
					break;
				case 26: // pencairan seluruh stable link
					String jmlh1 = bacManager.selectJmlPencairanAllStableLink(noSPAJ);
					if(Integer.valueOf(jmlh1)> 0){
						// notifikasi apakah ingin diambil semua TOP UP
						
					}
					break;
//				case 27: // pencairan sebagian stable link
//					
//					break;
//				case 33: // tolakan retur stable link
//					break;
				case 23: // WITHDRAW UNIT LINKED -- USER BISA PILIH BERDASARKAN MU_KE di mst_ulink, diambil no_registernya utk ke no_reff
					List data_ul = bacManager.selectWithdrawULSnows(noSPAJ);
					String ulke = ServletRequestUtils.getStringParameter(request, "mu_ke","");
					String mu_ke=""; 
					if(data_ul.size()>0){
						if(ulke.equals("")){
							List<DropDown> pilihan = new ArrayList<DropDown>();
							pilihan = bacManager.selectWithdrawULSnows(noSPAJ);
							data.put("pilihanWU", pilihan);
						}else{
							List a = bacManager.selectWithdrawULSnowsMuKe(noSPAJ, ulke);
							for(int i=0;i<a.size();i++){
								Map ma = (HashMap) a.get(i);
	//							mu_ke = (String) ma.get("MU_KE");
//								Integer ke = ((BigDecimal) ma.get("MU_KE")).intValue();
//								mu_ke = ke.toString();
								no_reff = (String) ma.get("NO_REGISTER");
								Date tgl_input =  (Date) ma.get("MU_TGL_INPUT");
//								data.put("mu_ke", mu_ke);
								data.put("jt_tempo", df.format(tgl_input));
								data.put("no_reff", no_reff);
							}
						}
					}else{
						data.put("peringatan", "Tidak ada data WITHDRAW UNIT LINKED yang dapat diproses!\nNO. REFF tidak akan muncul!");
					}
					break;
//				case 24: // Switching UNIT LINKED
//					break;
//				case 28: // ENDORSMENT ROLLOVER
//					break;
				case 30: // nilai tunai MRI
					String jmlhMRI = bacManager.selectJmlNilaiTunaiMRI(noSPAJ, String.valueOf(ljj_id));
					if (Integer.valueOf(jmlhMRI) == 1){
						List a = bacManager.selectJtTempoMstSurrender(noSPAJ, String.valueOf(ljj_id));
						for(int i=0;i<a.size();i++){
							Map ma = (HashMap) a.get(i);
							jt_tempo = (Date) ma.get("jt_tempo");
							no_reff = (String) ma.get("no_reff");
							data.put("jt_tempo", df.format(jt_tempo));
							data.put("no_reff", no_reff);
						}
					}
					break;
				}
				array.add(data);
				m.put("jenisCase", array);
				result = m;
			}
			else if("showData".equals(jn)){
				array = new ArrayList();
				String mi_id = ServletRequestUtils.getStringParameter(request, "mi_id",null);
//				m.put("dataMstInbox", bacManager.selectMstInbox(mi_id,null));
				List x = bacManager.selectMstInbox(mi_id, null, Integer.valueOf(currentUser.getLus_id()), currentUser.getLde_id());
				for(int i=0; i<x.size(); i++){
					Map mp = (HashMap) x.get(i);
					Map cm = new HashMap();
					String TGL_BERKAS_LENGKAP = null, TGL_BERKAS_MASUK = null, TGL_JT_TEMPO = null, TGL_KONFIRMASI = null, TGL_ADMIN_TERIMA = null, CREATE_DATE = null;
					
					BigDecimal lstb_id2 = (BigDecimal) mp.get("LSTB_ID");
					String polis = (String) mp.get("polis");
					String SPAJ_FORMATTED = (String) mp.get("SPAJ_FORMATTED");
					String Tertanggung = (String) mp.get("Tertanggung");
					String Pemegang = (String) mp.get("Pemegang");
					String LSDBS_NAME = (String) mp.get("LSDBS_NAME");
					BigDecimal LSBS_ID = (BigDecimal) mp.get("LSBS_ID");
					String LCA_ID = (String) mp.get("LCA_ID");
					String MI_ID = (String) mp.get("MI_ID");
					BigDecimal LJJ_ID = (BigDecimal) mp.get("LJJ_ID");
					Date TGL_BERKAS_MASUK_dt = (Date) mp.get("TGL_BERKAS_MASUK");
					if(TGL_BERKAS_MASUK_dt != null) TGL_BERKAS_MASUK = df.format(TGL_BERKAS_MASUK_dt);
					Date TGL_BERKAS_LENGKAP_dt = (Date) mp.get("TGL_BERKAS_LENGKAP");
					if(TGL_BERKAS_LENGKAP_dt != null) TGL_BERKAS_LENGKAP = df.format(TGL_BERKAS_LENGKAP_dt);
					Date TGL_JT_TEMPO_dt = (Date) mp.get("TGL_JT_TEMPO");
					if(TGL_JT_TEMPO_dt != null) TGL_JT_TEMPO = df.format(TGL_JT_TEMPO_dt);
					Date TGL_KONFIRMASI_dt = (Date) mp.get("TGL_KONFIRMASI");
					if(TGL_KONFIRMASI_dt != null) TGL_KONFIRMASI = df.format(TGL_KONFIRMASI_dt);
					Date TGL_ADMIN_TERIMA_dt = (Date) mp.get("TGL_ADMIN_TERIMA");
					if(TGL_ADMIN_TERIMA_dt != null) TGL_ADMIN_TERIMA = df.format(TGL_ADMIN_TERIMA_dt);
					Date CREATE_DATE_dt = (Date) mp.get("CREATE_DATE");
					if(CREATE_DATE_dt != null) CREATE_DATE = df.format(CREATE_DATE_dt);
					String MI_DESC = (String) mp.get("MI_DESC");
					if(MI_DESC=="" || MI_DESC==null){
						MI_DESC = "";
					}
					String NO_REFF = (String) mp.get("NO_REFF");
					String nama = (String) mp.get("nama");
					String LJJ_FILE = (String) mp.get("LJJ_FILE");
					String LJJ_FOLDER = (String) mp.get("LJJ_FOLDER");

					cm.put("lstb_id", lstb_id2);
					cm.put("polis", polis);
					cm.put("SPAJ_FORMATTED", SPAJ_FORMATTED);
					cm.put("Tertanggung", Tertanggung);
					cm.put("Pemegang", Pemegang);
					cm.put("LSDBS_NAME", LSDBS_NAME);
					cm.put("LSBS_ID", LSBS_ID);
					cm.put("LCA_ID", LCA_ID);
					cm.put("MI_ID", MI_ID);
					cm.put("LJJ_ID", LJJ_ID);
					cm.put("TGL_BERKAS_MASUK", TGL_BERKAS_MASUK);
					cm.put("TGL_BERKAS_LENGKAP", TGL_BERKAS_LENGKAP);
					cm.put("TGL_JT_TEMPO", TGL_JT_TEMPO);
					cm.put("TGL_KONFIRMASI", TGL_KONFIRMASI);
					cm.put("TGL_ADMIN_TERIMA", TGL_ADMIN_TERIMA);
					cm.put("CREATE_DATE", CREATE_DATE);
					cm.put("MI_DESC", MI_DESC);
					cm.put("NO_REFF", NO_REFF);
					cm.put("LJJ_FILE", LJJ_FILE);
					cm.put("LJJ_FOLDER", LJJ_FOLDER);
					cm.put("nama", nama);
					cm.put("NOW", "["+df.format(now)+"]");
					
					array.add(cm);
				}
				m.put("dataMstInbox", array);
				List a = bacManager.selectMstInboxChecklist(mi_id, null);
				array = new ArrayList();
				for(int i=0; i<a.size(); i++){
					Map mp = (HashMap) a.get(i);
					Map cm = new HashMap();
					BigDecimal ljj_id = (BigDecimal) mp.get("LJJ_ID");
					BigDecimal lc_id = (BigDecimal) mp.get("LC_ID");
					BigDecimal mi_flag = (BigDecimal) mp.get("MI_FLAG");
					String desc = (String) mp.get("MI_DESC");
					if(desc==null) desc = "";
					String lc_nama = (String) mp.get("LC_NAMA");
					Boolean chk = null;
					if(mi_flag.intValue() == 1) chk = true;
					else chk = false;
					
					cm.put("ljj_id", ljj_id);
					cm.put("lc_id", lc_id);
					cm.put("mi_flag", mi_flag);
					cm.put("desc", desc);
					cm.put("chk", chk);
					cm.put("lc_nama", lc_nama);
					
					array.add(cm);
				}
				m.put("dataCheckList", array);
				result = m;				
			}
			else if(jn.equals("cariData")){
				array = new ArrayList();
				String selC = ServletRequestUtils.getStringParameter(request, "selC");
				String txt = ServletRequestUtils.getStringParameter(request, "txt",null).trim();
				txt = txt.replace(".", "");
				if(selC.equals("MI_ID")){
					List mInbox = bacManager.selectMstInbox(txt, null, Integer.valueOf(currentUser.getLus_id()), currentUser.getLde_id());
					if(!mInbox.isEmpty()){
						for(int i=0; i<mInbox.size(); i++){
							Map mp = (HashMap) mInbox.get(i);
							Map cm = new HashMap();
							String TGL_BERKAS_LENGKAP = null, TGL_BERKAS_MASUK = null, TGL_JT_TEMPO = null, TGL_KONFIRMASI = null, TGL_ADMIN_TERIMA = null, CREATE_DATE = null;
							
							BigDecimal lstb_id2 = (BigDecimal) mp.get("lstb_id");
							String polis = (String) mp.get("polis");
							String SPAJ_FORMATTED = (String) mp.get("SPAJ_FORMATTED");
							String Tertanggung = (String) mp.get("Tertanggung");
							String Pemegang = (String) mp.get("Pemegang");
							String LSDBS_NAME = (String) mp.get("LSDBS_NAME");
							BigDecimal LSBS_ID = (BigDecimal) mp.get("LSBS_ID");
							String LCA_ID = (String) mp.get("LCA_ID");
							String MI_ID = (String) mp.get("MI_ID");
							BigDecimal LJJ_ID = (BigDecimal) mp.get("LJJ_ID");
							Date TGL_BERKAS_MASUK_dt = (Date) mp.get("TGL_BERKAS_MASUK");
							if(TGL_BERKAS_MASUK_dt != null) TGL_BERKAS_MASUK = df.format(TGL_BERKAS_MASUK_dt);
							Date TGL_BERKAS_LENGKAP_dt = (Date) mp.get("TGL_BERKAS_LENGKAP");
							if(TGL_BERKAS_LENGKAP_dt != null) TGL_BERKAS_LENGKAP = df.format(TGL_BERKAS_LENGKAP_dt);
							Date TGL_JT_TEMPO_dt = (Date) mp.get("TGL_JT_TEMPO");
							if(TGL_JT_TEMPO_dt != null) TGL_JT_TEMPO = df.format(TGL_JT_TEMPO_dt);
							Date TGL_KONFIRMASI_dt = (Date) mp.get("TGL_KONFIRMASI");
							if(TGL_KONFIRMASI_dt != null) TGL_KONFIRMASI = df.format(TGL_KONFIRMASI_dt);
							Date TGL_ADMIN_TERIMA_dt = (Date) mp.get("TGL_ADMIN_TERIMA");
							if(TGL_ADMIN_TERIMA_dt != null) TGL_ADMIN_TERIMA = df.format(TGL_ADMIN_TERIMA_dt);
							Date CREATE_DATE_dt = (Date) mp.get("CREATE_DATE");
							if(CREATE_DATE_dt != null) CREATE_DATE = df.format(CREATE_DATE_dt);
							String MI_DESC = (String) mp.get("MI_DESC");
							String NO_REFF = (String) mp.get("NO_REFF");
							String nama = (String) mp.get("nama");
							String LJJ_FILE = (String) mp.get("LJJ_FILE");
							String LJJ_FOLDER = (String) mp.get("LJJ_FOLDER");

							cm.put("lstb_id", lstb_id2);
							cm.put("polis", polis);
							cm.put("SPAJ_FORMATTED", SPAJ_FORMATTED);
							cm.put("Tertanggung", Tertanggung);
							cm.put("Pemegang", Pemegang);
							cm.put("LSDBS_NAME", LSDBS_NAME);
							cm.put("LSBS_ID", LSBS_ID);
							cm.put("LCA_ID", LCA_ID);
							cm.put("MI_ID", MI_ID);
							cm.put("LJJ_ID", LJJ_ID);
							cm.put("TGL_BERKAS_MASUK", TGL_BERKAS_MASUK);
							cm.put("TGL_BERKAS_LENGKAP", TGL_BERKAS_LENGKAP);
							cm.put("TGL_JT_TEMPO", TGL_JT_TEMPO);
							cm.put("TGL_KONFIRMASI", TGL_KONFIRMASI);
							cm.put("TGL_ADMIN_TERIMA", TGL_ADMIN_TERIMA);
							cm.put("CREATE_DATE", CREATE_DATE);
							cm.put("MI_DESC", MI_DESC);
							cm.put("NO_REFF", NO_REFF);
							cm.put("LJJ_FILE", LJJ_FILE);
							cm.put("LJJ_FOLDER", LJJ_FOLDER);
							cm.put("nama", nama);
							
							array.add(cm);
						}
						
						m.put("dataMstInbox", array);
						array = new ArrayList();
						List a = bacManager.selectMstInboxChecklist(txt, null);
						for(int i=0; i<a.size(); i++){
							Map mp = (HashMap) a.get(i);
							Map cm = new HashMap();
							BigDecimal ljj_id = (BigDecimal) mp.get("LJJ_ID");
							BigDecimal lc_id = (BigDecimal) mp.get("LC_ID");
							BigDecimal mi_flag = (BigDecimal) mp.get("MI_FLAG");
							String desc = (String) mp.get("MI_DESC");
							String lc_nama = (String) mp.get("LC_NAMA");
							Boolean chk = null;
							if(mi_flag.intValue() == 1) chk = true;
							else chk = false;
							
							cm.put("ljj_id", ljj_id);
							cm.put("lc_id", lc_id);
							cm.put("mi_flag", mi_flag);
							cm.put("desc", desc);
							cm.put("chk", chk);
							cm.put("lc_nama", lc_nama);
							
							array.add(cm);
						}
					}else{
						m.put("pesan", "Data tidak ada");
					}
				}else{
					List mInbox = bacManager.selectMstInbox(null, txt, Integer.valueOf(currentUser.getLus_id()), currentUser.getLde_id());
					if(!mInbox.isEmpty()){
						String mi_id = null;
						for(int i=0; i<mInbox.size();i++){
							Map mp = (HashMap) mInbox.get(i);
							mi_id = (String) mp.get("MI_ID");
						}
						m.put("dataMstInbox", mInbox);
						List a = bacManager.selectMstInboxChecklist(mi_id, null);
						for(int i=0; i<a.size(); i++){
							Map mp = (HashMap) a.get(i);
							Map cm = new HashMap();
							BigDecimal ljj_id = (BigDecimal) mp.get("LJJ_ID");
							BigDecimal lc_id = (BigDecimal) mp.get("LC_ID");
							BigDecimal mi_flag = (BigDecimal) mp.get("MI_FLAG");
							String desc = (String) mp.get("MI_DESC");
							String lc_nama = (String) mp.get("LC_NAMA");
							Boolean chk = null;
							if(mi_flag.intValue() == 1) chk = true;
							else chk = false;
							
							cm.put("ljj_id", ljj_id);
							cm.put("lc_id", lc_id);
							cm.put("mi_flag", mi_flag);
							cm.put("desc", desc);
							cm.put("chk", chk);
							cm.put("lc_nama", lc_nama);
							
							array.add(cm);
						}
					}else{
						m.put("pesan", "Data tidak ada");
					}
				}
				m.put("dataCheckList", array);
				result = m;
			}
		}//Ridhaal (1 Jul 2016) / Ajax AjuanBerkas
		else if("inputBerkasAjuBiaya".equals(pg)){
				
			
			String lcaName = ServletRequestUtils.getStringParameter(request, "cabang", null);
			Integer lusID = Integer.valueOf(currentUser.getLus_id());
			Map data = new HashMap();
			
			if("cekIDBerkas".equals(jn)){
				array = new ArrayList();
				String mi_id = ServletRequestUtils.getStringParameter(request, "mi_id",null);
				String lcaId = bacManager.selectLcaIdAB(mi_id,null,1);
//				m.put("dataMstInbox", bacManager.selectMstInbox(mi_id,null));
				List x = bacManager.selectListAjuanBiaya(lcaId,lcaName, 1, mi_id);
				for(int i=0; i<x.size(); i++){
					Map mp = (HashMap) x.get(i);
					Map cm = new HashMap();
					String TGL_BERKAS_LENGKAP = null, TGL_BERKAS_MASUK = null, TGL_JT_TEMPO = null, TGL_KONFIRMASI = null, TGL_ADMIN_TERIMA = null, CREATE_DATE = null;
										
					String NO_BERKAS = (String) mp.get("NO_BERKAS");
					String TGL_AJUAN = (String) mp.get("TGL_AJUAN");
					String NAMA_BANK = (String) mp.get("NAMA_BANK");
					String MRC_NO_AC = (String) mp.get("MRC_NO_AC");
					String MRC_ATAS_NAMA = (String) mp.get("MRC_ATAS_NAMA");
					String ALOKASI_KODE =  mp.get("ALOKASI_KODE").toString();
					String MRC_KOTA =  mp.get("MRC_KOTA").toString();
					String TGL_KIRIM_BERKAS = (String) mp.get("TGL_KIRIM_BERKAS");
					
					if (TGL_KIRIM_BERKAS == null){
						TGL_KIRIM_BERKAS = "";
					}
					
					cm.put("NO_BERKAS", NO_BERKAS);
					cm.put("TGL_AJUAN", TGL_AJUAN);
					cm.put("NAMA_BANK", NAMA_BANK);
					cm.put("MRC_NO_AC", MRC_NO_AC);
					cm.put("MRC_ATAS_NAMA", MRC_ATAS_NAMA);
					cm.put("ALOKASI_KODE", ALOKASI_KODE);
					cm.put("MRC_KOTA", MRC_KOTA);
					cm.put("TGL_KIRIM_BERKAS", TGL_KIRIM_BERKAS);
					
																									
					array.add(cm);
					
									
				}
				m.put("dataMstInbox", array);
				
				
				List a = bacManager.selectDataNoBerkasDet(mi_id);
				array = new ArrayList();
				Integer JumlahTtlBiaya = 0;	
				Integer no_urut = 1;
				
				for(int i=0; i<a.size(); i++){
					Map mp = (HashMap) a.get(i);
					Map cm = new HashMap();
					
					
					
					String MID_DESC = (String) mp.get("MID_DESC");
					BigDecimal KUANTITAS = (BigDecimal) mp.get("KUANTITAS");
					String AB_TYPE = (String) mp.get("AB_TYPE");
					BigDecimal MID_NOMINAL = (BigDecimal) mp.get("MID_NOMINAL");
					String TGL_GUNA = (String) mp.get("TGL_GUNA");
					String MID_KET = (String) mp.get("MID_KET");
					
					if (MID_KET == null){
						MID_KET = "";
					}
					
					cm.put("no_urut", no_urut);
					cm.put("MID_DESC", MID_DESC);
					cm.put("KUANTITAS", KUANTITAS);
					cm.put("AB_TYPE", AB_TYPE);
					cm.put("MID_NOMINAL", MID_NOMINAL);
					cm.put("TGL_GUNA", TGL_GUNA);
					cm.put("MID_KET", MID_KET);
					
					array.add(cm);
					
					JumlahTtlBiaya = JumlahTtlBiaya + MID_NOMINAL.intValue();
					no_urut++;
				}
				
				String[] listAlokasi = new String[20];
				
				String [] alokasiCA = {"LISTRIK","LISTRIK TOKEN","TELP","PDAM","SOLAR GENSET","SERVICE CHARGE GEDUNG", "PERMOHONAN SUMBANGAN DARI INSTANSI/LINGKUNGAN","PBB GEDUNG", "PAJAK REKLAME","BIAYA PENGURUSAN SURAT DOMISILI"};
				String [] alokasiSDP = {"SERVICE AC","RENOVASI KANTOR CABANG","PERBAIKAN KANTOR CABANG"};
				String [] alokasiReimburse = {"ENTERTAINT CAO","ENTERTAINT MARKETING","PULSA MARKETING","KARANGAN BUNGA KARYAWAN","KARANGAN BUNGA NASABAH","KAS KECIL","LISTRIK","LISTRIK TOKEN","TELP","PDAM","SOLAR GENSET","SERVICE CHARGE GEDUNG", "PERMOHONAN SUMBANGAN DARI INSTANSI/LINGKUNGAN","PBB GEDUNG", "PAJAK REKLAME","BIAYA PENGURUSAN SURAT DOMISILI"};
				String [] alokasiATK = {"PERMINTAAN TINTA/TONER","PERMINTAAN BARANG CETAKAN","PERMINTAAN ATK"};
				
				
				
				m.put("alokasiCA", alokasiCA);
				m.put("alokasiSDP", alokasiSDP);
				m.put("alokasiReimburse", alokasiReimburse);
				m.put("alokasiATK", alokasiATK);
				
				m.put("countnumber",a.size());
				m.put("pesan", null);
				m.put("JumlahTtlBiaya", JumlahTtlBiaya);
				m.put("dataDetailItem", array);
				
				result = m;	
							
			}else if("newIdBerkas".equals(jn)){
				String mi_id = bacManager.selectSeqInboxId();
				
//				String hariIni = bacManager.selectSysdateddmmyyy();
				String hariIni = df.format(now).toString();
				
				m.put("mi_id", mi_id);
				m.put("hariIni", hariIni);
				
				result = m;	
			}else if("searchIDBerkas".equals(jn)){
				array = new ArrayList();
				String mi_id = ServletRequestUtils.getStringParameter(request, "mi_id",null);
				String lcaId = currentUser.getLca_id();
				
				if(!mi_id.isEmpty()){
				
						if(lcaId.equals("01")){
							m.put("dataHistory", bacManager.selectHistoryBerkas(1,mi_id, null));
						}else{
							m.put("dataHistory", bacManager.selectHistoryBerkas(3,mi_id,currentUser.getCabang()));
						}				
				}
						
				result = m;	
			}else if("addRowtabel".equals(jn)){
				array = new ArrayList();
				Integer alokasi = ServletRequestUtils.getIntParameter(request, "alokasi");
				String[] listAlokasi = new String[20];
				
				String [] alokasiCA = {"LISTRIK","LISTRIK TOKEN","TELP","PDAM","SOLAR GENSET","SERVICE CHARGE GEDUNG", "PERMOHONAN SUMBANGAN DARI INSTANSI/LINGKUNGAN","PBB GEDUNG", "PAJAK REKLAME","BIAYA PENGURUSAN SURAT DOMISILI"};
				String [] alokasiSDP = {"SERVICE AC","RENOVASI KANTOR CABANG","PERBAIKAN KANTOR CABANG"};
				String [] alokasiReimburse = {"ENTERTAINT CAO","ENTERTAINT MARKETING","PULSA MARKETING","KARANGAN BUNGA KARYAWAN","KARANGAN BUNGA NASABAH","KAS KECIL","LISTRIK","LISTRIK TOKEN","TELP","PDAM","SOLAR GENSET","SERVICE CHARGE GEDUNG", "PERMOHONAN SUMBANGAN DARI INSTANSI/LINGKUNGAN","PBB GEDUNG", "PAJAK REKLAME","BIAYA PENGURUSAN SURAT DOMISILI"};
				String [] alokasiATK = {"PERMINTAAN TINTA/TONER","PERMINTAAN BARANG CETAKAN","PERMINTAAN ATK"};
				
			
				m.put("alokasiCA", alokasiCA);
				m.put("alokasiSDP", alokasiSDP);
				m.put("alokasiReimburse", alokasiReimburse);
				m.put("alokasiATK", alokasiATK);
			
				result = m;	
			}else if("databycabang".equals(jn)){
				array = new ArrayList();
				String cabangpil = ServletRequestUtils.getStringParameter(request, "cabangpil");
							
				m.put("dataInbox",  bacManager.selectListAjuanBiaya(null,cabangpil,3,null));
			
				result = m;	
			}else if("inputTglBerkas".equals(jn)){
				
				String NoBerkas = ServletRequestUtils.getStringParameter(request, "mi_id","");
				String tbk = ServletRequestUtils.getStringParameter(request, "tbk","");
				String pesan ="";
				String desc ="";
				if( !tbk.isEmpty()){
						try{
							desc = "Input Tanggal Pengiriman Berkas : "+tbk;
							Date tglberkas = df.parse(tbk);
							MstInbox mstInbox = new MstInbox(NoBerkas, null, null, null, null, null, null, null, 
									null, null, null, tglberkas, null, null, null, null, null, null, null, 
									null, null, null, null, null, null, null, null,null, null,null);
							
							MstInboxHist mstInboxHist = new MstInboxHist(NoBerkas, 207, 207, null, null, desc, Integer.valueOf(currentUser.getLus_id()), now, null,0,0);
							
							//bacManager.updateTglPengirimanBerkas(NoBerkas,tbk);
							bacManager.updateMstInbox(mstInbox);
							bacManager.insertMstInboxHist(mstInboxHist);
							
							pesan ="Penyetingan  tanggal pengiriman berkas (hard Copy) telah berhasil, silahkan melakukan proses transfer ke BAS";
							
						}catch (Exception e) {
							pesan = "Penyetingan  tanggal pengiriman berkas gagal, Format tanggal tidak sesuai- (dd/mm/yyyy)";
							tbk="";
							logger.error("ERROR :", e);
						}
					}else{
					pesan ="Mohon menginput tanggal terlebih dahulu dengan format (dd/mm/yyyy)";
					tbk="";
				}
				
				m.put("tglbk", tbk);
				m.put("pesan", pesan);
			
				result = m;	
			}else if("cekPermintaan".equals(jn)){
				array = new ArrayList();
				int role = 0;
				int role2 = 0;
				List cekRol = null;
				String tgl_pemakaian = "";	
				String jenis ="";
				String cabang ="";
				String NO_BERKAS ="";
				String nama_item ="";
				Integer qty =0;
				Integer biaya = 0;
				
				String cek = ServletRequestUtils.getStringParameter(request, "cek");
				
				if("cekNew".equals(cek)){
					jenis = ServletRequestUtils.getStringParameter(request, "jenis");					
					tgl_pemakaian = ServletRequestUtils.getStringParameter(request, "tgl_pemakaian","");
					cabang = ServletRequestUtils.getStringParameter(request, "cabang");
					
					if("LISTRIK,TELP,PDAM,SOLAR GENSET,SERVICE CHARGE GEDUNG,ENTERTAINT CAO,ENTERTAINT MARKETING,PULSA MARKETING,KAS KECIL,PERMINTAAN BARANG CETAKAN,PERMINTAAN ATK".indexOf(jenis)>=0){
						role = 1;
					}else if("PERMINTAAN TINTA/TONER".indexOf(jenis)>=0){
						role = 1;
						role2 = 2;
					}else if("SERVICE AC".indexOf(jenis)>=0){
						role = 3;
					}else if("PBB GEDUNG,PAJAK REKLAME,BIAYA PENGURUSAN SURAT DOMISILI".indexOf(jenis)>=0){
						role = 4;
					}else if("LISTRIK TOKEN,PERMOHONAN SUMBANGAN DARI INSTANSI/LINGKUNGAN,RENOVASI KANTOR CABANG,PERBAIKAN KANTOR CABANG,KARANGAN BUNGA KARYAWAN,KARANGAN BUNGA NASABAH".indexOf(jenis)>=0){
						role = 5;
					}
					
					cekRol= bacManager.selectCekRolAB(jenis, tgl_pemakaian, cabang, role,null,null);	
				}else if("cekUpdate".equals(cek)){
					NO_BERKAS = ServletRequestUtils.getStringParameter(request, "NO_BERKAS","");
					nama_item = ServletRequestUtils.getStringParameter(request, "nama_item");
					qty = ServletRequestUtils.getIntParameter(request, "qty",0);
					jenis = ServletRequestUtils.getStringParameter(request, "jenis");
					tgl_pemakaian = ServletRequestUtils.getStringParameter(request, "tgl_pemakaian","");
					biaya = ServletRequestUtils.getIntParameter(request, "biaya",0);
					cabang = bacManager.selectKotaInboxDet(NO_BERKAS);
					
					if("LISTRIK,TELP,PDAM,SOLAR GENSET,SERVICE CHARGE GEDUNG,ENTERTAINT CAO,ENTERTAINT MARKETING,PULSA MARKETING,KAS KECIL,PERMINTAAN BARANG CETAKAN,PERMINTAAN ATK".indexOf(jenis)>=0){
						role = 1;
					}else if("PERMINTAAN TINTA/TONER".indexOf(jenis)>=0){
						role = 1;
						role2 = 2;
					}else if("SERVICE AC".indexOf(jenis)>=0){
						role = 3;
					}else if("PBB GEDUNG,PAJAK REKLAME,BIAYA PENGURUSAN SURAT DOMISILI".indexOf(jenis)>=0){
						role = 4;
					}else if("LISTRIK TOKEN,PERMOHONAN SUMBANGAN DARI INSTANSI/LINGKUNGAN,RENOVASI KANTOR CABANG,PERBAIKAN KANTOR CABANG,KARANGAN BUNGA KARYAWAN,KARANGAN BUNGA NASABAH".indexOf(jenis)>=0){
						role = 5;
					}
					
					cekRol= bacManager.selectCekRolAB(jenis, tgl_pemakaian, cabang, role, 1,NO_BERKAS );	
				}
				
					
					//lakukan validasi untuk masa keperluan (1 x /bulan)
					if(role > 0){							
										
						String psn = "";
						String noB = "";
						String ada = "";
						String noB1 = "";
						String noB2 = "";
						
						if(!cekRol.isEmpty()){
																				
							for(int i=0; i<cekRol.size();i++){
								Map mp = (HashMap) cekRol.get(i);
								noB = (String) mp.get("MI_ID");	
								
								if(i==0){
									noB1 = (String) mp.get("MI_ID");	
								}else{
									noB2 = (String) mp.get("MI_ID");	
								}
								
								if(role == 1 && role2 == 0){
								psn = "Data tidak bisa di simpan dikarenakan jenis "+ jenis + " sudah terdaftar dengan No Regristasi "+ noB +" untuk BULAN dengan CABANG yang sama" ;
								}else if (role == 4){
									psn = "Data tidak bisa di simpan dikarenakan jenis "+ jenis + " sudah terdaftar dengan No Regristasi "+ noB +" untuk TAHUN dengan CABANG yang sama" ;
								}else if (role == 3){
									psn = "Data tidak bisa di simpan dikarenakan jenis "+ jenis + " sudah terdaftar dengan No Regristasi "+ noB +" dalam 6 BULAN ini dengan CABANG yang sama " ;
								}else if (role2 == 2){
									
									if (cekRol.size() >= 2){
										psn = "Data tidak bisa di simpan dikarenakan jenis "+ jenis + " sudah terdaftar dengan No Regristasi "+ noB1+" dan " + noB2 + " untuk BULAN dengan CABANG yang sama (req hanya bisa 2 x dalam sebuan)" ;
									}
								}else if (role == 5){
									//do nothing
								}
							}							
						}
						m.put("psn", psn);
						result = m;	
					}else{
						m.put("psn", "");
						result = m;	
					}
				}else if("cekUpdatePermintaan".equals(jn)){
					array = new ArrayList();
					int role = 0;
					int role2 = 0;
					List cekRol = null;
					String NO_BERKAS = ServletRequestUtils.getStringParameter(request, "NO_BERKAS","");
					String nama_item = ServletRequestUtils.getStringParameter(request, "nama_item");
					Integer qty = ServletRequestUtils.getIntParameter(request, "qty",0);
					String jenis = ServletRequestUtils.getStringParameter(request, "jenis");
					String tgl_pemakaian = ServletRequestUtils.getStringParameter(request, "tgl_pemakaian","");
					Integer biaya = ServletRequestUtils.getIntParameter(request, "biaya",0);
					String cabang = bacManager.selectKotaInboxDet(NO_BERKAS);
					
					if("LISTRIK,TELP,PDAM,SOLAR GENSET,SERVICE CHARGE GEDUNG,ENTERTAINT CAO,ENTERTAINT MARKETING,PULSA MARKETING,KAS KECIL,PERMINTAAN BARANG CETAKAN,PERMINTAAN ATK".indexOf(jenis)>=0){
						role = 1;
					}else if("PERMINTAAN TINTA/TONER".indexOf(jenis)>=0){
						role = 1;
						role2 = 2;
					}else if("SERVICE AC".indexOf(jenis)>=0){
						role = 3;
					}else if("PBB GEDUNG,PAJAK REKLAME,BIAYA PENGURUSAN SURAT DOMISILI".indexOf(jenis)>=0){
						role = 4;
					}else {
						role = 5;
					}
					
					if(role > 0){
						
						cekRol= bacManager.selectCekRolAB(jenis, tgl_pemakaian, cabang, role, 1,NO_BERKAS );	
						
						String psn = "";
						String noB = "";
						String ada = "";
						String noB1 = "";
						String noB2 = "";
						
						if(!cekRol.isEmpty()){
																				
							for(int i=0; i<cekRol.size();i++){
								Map mp = (HashMap) cekRol.get(i);
								noB = (String) mp.get("MI_ID");	
								
								if(i==0){
									noB1 = (String) mp.get("MI_ID");	
								}else{
									noB2 = (String) mp.get("MI_ID");	
								}
								
								if(role == 1 && role2 == 0){
								psn = "Data tidak bisa di simpan dikarenakan jenis "+ jenis + " sudah terdaftar dengan No Regristasi "+ noB +" untuk BULAN dengan CABANG yang sama" ;
								}else if (role == 4){
									psn = "Data tidak bisa di simpan dikarenakan jenis "+ jenis + " sudah terdaftar dengan No Regristasi "+ noB +" untuk TAHUN dengan CABANG yang sama" ;
								}else if (role == 3){
									psn = "Data tidak bisa di simpan dikarenakan jenis "+ jenis + " sudah terdaftar dengan No Regristasi "+ noB +" dalam 6 BULAN ini dengan CABANG yang sama " ;
								}else if (role2 == 2){
									if (cekRol.size() >= 2){
										psn = "Data tidak bisa di simpan dikarenakan jenis "+ jenis + " sudah terdaftar dengan No Regristasi "+ noB1+" dan " + noB2 + " untuk BULAN dengan CABANG yang sama (req hanya bisa 2 x dalam sebuan)" ;
									}
								}else if (role == 5){
									//do nothing
								}
							}							
						}
						m.put("psn", psn);
						result = m;		
						
					}else{
						m.put("psn", "");
						result = m;	
					}
					
				}else if("EditIDBerkas".equals(jn)){
					array = new ArrayList();
					String mi_id = ServletRequestUtils.getStringParameter(request, "mi_id",null);
					String lcaId = bacManager.selectLcaIdAB(mi_id,null,1);
//					m.put("dataMstInbox", bacManager.selectMstInbox(mi_id,null));
					List x = bacManager.selectListAjuanBiaya(lcaId,lcaName, 1, mi_id);
					for(int i=0; i<x.size(); i++){
						Map mp = (HashMap) x.get(i);
						Map cm = new HashMap();
						String TGL_BERKAS_LENGKAP = null, TGL_BERKAS_MASUK = null, TGL_JT_TEMPO = null, TGL_KONFIRMASI = null, TGL_ADMIN_TERIMA = null, CREATE_DATE = null;
											
						String NO_BERKAS = (String) mp.get("NO_BERKAS");
						String TGL_AJUAN = (String) mp.get("TGL_AJUAN");
						String NAMA_BANK = (String) mp.get("NAMA_BANK");
						String MRC_NO_AC = (String) mp.get("MRC_NO_AC");
						String MRC_ATAS_NAMA = (String) mp.get("MRC_ATAS_NAMA");
						String ALOKASI_KODE =  mp.get("ALOKASI_KODE").toString();
						String MRC_KOTA =  mp.get("MRC_KOTA").toString();
						String TGL_KIRIM_BERKAS = (String) mp.get("TGL_KIRIM_BERKAS");
						
						if (TGL_KIRIM_BERKAS == null){
							TGL_KIRIM_BERKAS = "";
						}
						
						cm.put("NO_BERKAS", NO_BERKAS);
						cm.put("TGL_AJUAN", TGL_AJUAN);
						cm.put("NAMA_BANK", NAMA_BANK);
						cm.put("MRC_NO_AC", MRC_NO_AC);
						cm.put("MRC_ATAS_NAMA", MRC_ATAS_NAMA);
						cm.put("ALOKASI_KODE", ALOKASI_KODE);
						cm.put("MRC_KOTA", MRC_KOTA);
						cm.put("TGL_KIRIM_BERKAS", TGL_KIRIM_BERKAS);
						
																										
						array.add(cm);
						
										
					}
					m.put("dataMstInbox", array);
					
					
					List a = bacManager.selectDataNoBerkasDet(mi_id);
					array = new ArrayList();
					Integer JumlahTtlBiaya = 0;	
					Integer no_urut = 1;
					
					for(int i=0; i<a.size(); i++){
						Map mp = (HashMap) a.get(i);
						Map cm = new HashMap();
						
						
						
						String MID_DESC = (String) mp.get("MID_DESC");
						BigDecimal KUANTITAS = (BigDecimal) mp.get("KUANTITAS");
						String AB_TYPE = (String) mp.get("AB_TYPE");
						BigDecimal MID_NOMINAL = (BigDecimal) mp.get("MID_NOMINAL");
						String TGL_GUNA = (String) mp.get("TGL_GUNA");
						String MID_KET = (String) mp.get("MID_KET");
						
						if (MID_KET == null){
							MID_KET = "";
						}
						
						cm.put("no_urut", no_urut);
						cm.put("MID_DESC", MID_DESC);
						cm.put("KUANTITAS", KUANTITAS);
						cm.put("AB_TYPE", AB_TYPE);
						cm.put("MID_NOMINAL", MID_NOMINAL);
						cm.put("TGL_GUNA", TGL_GUNA);
						cm.put("MID_KET", MID_KET);
						
						array.add(cm);
						
						JumlahTtlBiaya = JumlahTtlBiaya + MID_NOMINAL.intValue();
						no_urut++;
					}
					
					String[] listAlokasi = new String[20];
					
					String [] alokasiCA = {"LISTRIK","LISTRIK TOKEN","TELP","PDAM","SOLAR GENSET","SERVICE CHARGE GEDUNG", "PERMOHONAN SUMBANGAN DARI INSTANSI/LINGKUNGAN","PBB GEDUNG", "PAJAK REKLAME","BIAYA PENGURUSAN SURAT DOMISILI"};
					String [] alokasiSDP = {"SERVICE AC","RENOVASI KANTOR CABANG","PERBAIKAN KANTOR CABANG"};
					String [] alokasiReimburse = {"ENTERTAINT CAO","ENTERTAINT MARKETING","PULSA MARKETING","KARANGAN BUNGA KARYAWAN","KARANGAN BUNGA NASABAH","KAS KECIL","LISTRIK","LISTRIK TOKEN","TELP","PDAM","SOLAR GENSET","SERVICE CHARGE GEDUNG", "PERMOHONAN SUMBANGAN DARI INSTANSI/LINGKUNGAN","PBB GEDUNG", "PAJAK REKLAME","BIAYA PENGURUSAN SURAT DOMISILI"};
					String [] alokasiATK = {"PERMINTAAN TINTA/TONER","PERMINTAAN BARANG CETAKAN","PERMINTAAN ATK"};
					
					
					
					m.put("alokasiCA", alokasiCA);
					m.put("alokasiSDP", alokasiSDP);
					m.put("alokasiReimburse", alokasiReimburse);
					m.put("alokasiATK", alokasiATK);
					
					m.put("countnumber",a.size());
					m.put("pesan", null);
					m.put("JumlahTtlBiaya", JumlahTtlBiaya);
					m.put("dataDetailItem", array);
					
					result = m;	
								
				}
			
		}
		
		if(result != null){
			out.print(gson.toJson(result));
			out.close();
		}
		
		return null;
	}
	
	/**
	 * Created by Ridhaal 24 Jun 2016
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @Link http://localhost:8080/E-Lions/snows/snows.htm?window=inputBerkasAjuBiaya
	 */
	public ModelAndView inputBerkasAjuBiaya(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap map = new HashMap();
		List array = new ArrayList();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<DropDown> jnsInput = new ArrayList<DropDown>();
		jnsInput = bacManager.selectLstJnJob();
		map.put("jnsInput", jnsInput);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//		Date now = bacManager.selectSysdate();
		Date now = new Date();
//		String hariIni = bacManager.selectSysdateddmmyyy();
		String hariIni = df.format(now).toString();
		Integer countnumber = 0;
		Integer pilihalokasi = 0;
		Integer SubNoBerkas = 0;
		Integer statBtn = 0;
		Integer statSave = 0; // 0 = data ke save , 1 = data gagal di save , 2 = checking ok
		String name_item = "";
		String jenis = "";
		Integer qty = 0;
		String tgl_pemakaian = "";
		String tg2 = "";
		String bl2 = "";
		String th2 = "";
		String tgl_p = "";
		String bln_p = "";
		String thn_p = "";
		Integer biaya = 0;
		String ket = "";
		String nom ="";
		String cabang = "";
		String pesan ="";
		
		if(request.getParameter("btnSave")!=null){
							
			countnumber = ServletRequestUtils.getIntParameter(request, "countnumber");
			String NoBerkas = ServletRequestUtils.getStringParameter(request, "NoBerkas");
			String tgl_berkas_masuk = ServletRequestUtils.getStringParameter(request, "tgl_berkas_masuk",null);
			cabang = ServletRequestUtils.getStringParameter(request, "cabanglist1",cabang);
			Integer alokasi = ServletRequestUtils.getIntParameter(request, "alokasiBiaya");
			String namaBank = ServletRequestUtils.getStringParameter(request, "namaBank","");
			String noRek = ServletRequestUtils.getStringParameter(request, "noRek","");
			String atasNamaRek = ServletRequestUtils.getStringParameter(request, "atasNamaRek","");
			String desc = "Input Request Pengajuan Biaya Cabang " + cabang +" : ";
			pilihalokasi = alokasi;
			List cekRol = null;
			Integer JumlahTtlBiaya = 0;	
			String DescJenis ="";
			
			Integer role = 0; //0 = Sesuai Kebutuhan ; 1 = Bulanan; 2 = (2x) sebulan ; 3 = (1x) 6 bulanan; 4 = Tahunan
			Integer role2 = 0; // untuk perhitungan role = 2 (2x) sebulan
			
			if(countnumber > 0){
				
				//list data detail keperluan
				for(int x= 1; x <= countnumber;x++){
					jenis = ServletRequestUtils.getStringParameter(request, "jenis2"+x,"");
					//tgl_pemakaian = ServletRequestUtils.getStringParameter(request, "tg2"+x,"") +"/"+ ServletRequestUtils.getStringParameter(request, "bl2"+x,"") +"/"+ServletRequestUtils.getStringParameter(request, "th2"+x,"");
					tgl_pemakaian = ServletRequestUtils.getStringParameter(request, "tgl_pemakaian2"+x,"");
					name_item = ServletRequestUtils.getStringParameter(request, "name_item2"+x,"");
					
					DescJenis = name_item+" (" +jenis + ") , "+ DescJenis;
					
					if("LISTRIK,TELP,PDAM,SOLAR GENSET,SERVICE CHARGE GEDUNG,ENTERTAINT CAO,ENTERTAINT MARKETING,PULSA MARKETING,KAS KECIL,PERMINTAAN BARANG CETAKAN,PERMINTAAN ATK".indexOf(jenis)>=0){
						role = 1;
					}else if("PERMINTAAN TINTA/TONER".indexOf(jenis)>=0){
						role = 1;
						role2 = 2;
					}else if("SERVICE AC".indexOf(jenis)>=0){
						role = 3;
					}else if("PBB GEDUNG,PAJAK REKLAME,BIAYA PENGURUSAN SURAT DOMISILI".indexOf(jenis)>=0){
						role = 4;
					}else if("LISTRIK TOKEN,PERMOHONAN SUMBANGAN DARI INSTANSI/LINGKUNGAN,RENOVASI KANTOR CABANG,PERBAIKAN KANTOR CABANG,KARANGAN BUNGA KARYAWAN,KARANGAN BUNGA NASABAH".indexOf(jenis)>=0){
						role = 5;
					}
					
					//lakukan validasi untuk masa keperluan
					if(role > 0){	
						cekRol= bacManager.selectCekRolAB(jenis, tgl_pemakaian, cabang, role, null,null);					
						
						if(!cekRol.isEmpty()){
								String mi_id;
								String ab_ty;
								String psn = "";
								String ada = "";
								String mi_id1 = "";
								String mi_id2 = "";
								
									for(int i=0; i<cekRol.size();i++){
										Map mp = (HashMap) cekRol.get(i);
										mi_id = (String) mp.get("MI_ID");
										ab_ty = (String) mp.get("AB_TYPE");
										
										if(i==0){
											mi_id1 =  (String) mp.get("MI_ID");	
										}else{
											mi_id2 =  (String) mp.get("MI_ID");	
										}
										
										if(role == 1 && role2 == 0){
												pesan = "Data tidak tersimpan dikarenakan jenis "+ ab_ty + " sudah terdaftar dengan No Regristasi "+ mi_id +" untuk BULAN dengan CABANG yang sama" ;
												statSave = 1;
												pilihalokasi = 60; // biar alokasi jadi ke CA (default) sehingga pada saat pilih atk, kolom transfer bisa hidden
												countnumber = 0 ;// sehingga pada saat input awal karena kegagalan save , no pada list detail = 0
											}else if (role == 4){
												pesan = "Data tidak tersimpan dikarenakan jenis "+ ab_ty + " sudah terdaftar dengan No Regristasi "+ mi_id +" untuk TAHUN dengan CABANG yang sama" ;
												statSave = 1;
												pilihalokasi = 60; 
												countnumber = 0 ;
											}else if (role == 3){
												pesan = "Data tidak tersimpan dikarenakan jenis "+ ab_ty + " sudah terdaftar dengan No Regristasi "+ mi_id +" dalam 6 BULAN ini dengan CABANG yang sama " ;
												statSave = 1;
												pilihalokasi = 60; 
												countnumber = 0 ;
											}else if (role2 == 2){
												if (cekRol.size() >= 2){
													pesan = "Data tidak tersimpan dikarenakan jenis "+ ab_ty + " sudah terdaftar dengan No Regristasi "+ mi_id1+" dan " + mi_id2 + " untuk BULAN dengan CABANG yang sama (req hanya bisa 2 x dalam sebuan)" ;
													statSave = 1;
													pilihalokasi = 60; 
													countnumber = 0 ;
												}
											}else if (role == 5){
												//do nothing
											}
										map.put("pesan", pesan);
									}
									map.put("NoBerkas", NoBerkas);
									map.put("tgl_berkas_masuk", hariIni);										
									map.put("namaBank", ServletRequestUtils.getStringParameter(request, "namaBank",""));
									map.put("noRek", ServletRequestUtils.getStringParameter(request, "noRek",""));
									map.put("atasNamaRek", ServletRequestUtils.getStringParameter(request, "atasNamaRek",""));
									
							}
					}else{
						map.put("pesan", "Data tidak bisa tersimpan karena list ini "+ jenis+", belum terdaftar");
						statSave = 1;
					}
					
				
				}
										
				if (statSave != 1){
					Connection conn = null;
					try{
							conn = this.elionsManager.getUwDao().getDataSource().getConnection();
							Date tbm; // untuk tgl berkas masuk
							if(tgl_berkas_masuk==null) tbm = null; else tbm = df.parse(tgl_berkas_masuk);
							
							MstInbox mstInbox = new MstInbox(NoBerkas, alokasi, 207, null, null,
																null, null, null, null, 
																null, 0, tbm, null, 
																null,null, Integer.valueOf(currentUser.getLus_id()),now,
																null, null, 1, null, 
																null, null, 0, 0, 
																null, null, null, 0,null);
							
							MstInboxHist mstInboxHist = new MstInboxHist(NoBerkas, null, 207, null, 
																		null, desc + DescJenis, Integer.valueOf(currentUser.getLus_id()), now,
																		null,0,0);
							
							bacManager.insertMstInbox(mstInbox, mstInboxHist);
							
							if(countnumber > 0){
								for(int r= 1; r <= countnumber;r++){
									SubNoBerkas = ServletRequestUtils.getIntParameter(request, "noUrut2"+r,0);
									name_item = ServletRequestUtils.getStringParameter(request, "name_item2"+r,"");
									qty = ServletRequestUtils.getIntParameter(request, "qty2"+r,1);
									jenis = ServletRequestUtils.getStringParameter(request, "jenis2"+r,"");
									//tgl_pemakaian = ServletRequestUtils.getStringParameter(request, "tg2"+r,"") +"/"+ ServletRequestUtils.getStringParameter(request, "bl2"+r,"") +"/"+ServletRequestUtils.getStringParameter(request, "th2"+r,"");
									tgl_pemakaian = ServletRequestUtils.getStringParameter(request, "tgl_pemakaian2"+r,"");
									biaya = ServletRequestUtils.getIntParameter(request, "biaya2"+r,0);
									ket = ServletRequestUtils.getStringParameter(request, "ket2"+r,"");
																	
										
									Date tp;
									if(tgl_pemakaian==null) tp = null; else tp = df.parse(tgl_pemakaian);
								
									MstInboxDet mstInboxDet = new MstInboxDet( NoBerkas,  SubNoBerkas,  name_item.toUpperCase(),  null,
																			  1,  biaya,  null,  namaBank.toUpperCase(),
																			  atasNamaRek.toUpperCase(),  noRek,  null,  null,
																			  ket,  cabang,  null,  qty,
																			  jenis,  tp);
									
									bacManager.insertMstInboxDet(mstInboxDet);
								}
							}
							
							//daftarkan ke list data yang harus di upload
							if(alokasi != 68){
								for(int lc_id= 150; lc_id <= 152;lc_id++){
								MstInboxChecklist inboxChecklist = new MstInboxChecklist(NoBerkas, alokasi, lc_id , 0, "", Integer.valueOf(currentUser.getLus_id()), now);
								bacManager.insertMstInboxChecklist(inboxChecklist);
								}
							}else{
								for(int lc_id= 150; lc_id <= 151;lc_id++){
									MstInboxChecklist inboxChecklist = new MstInboxChecklist(NoBerkas, alokasi, lc_id , 0, "", Integer.valueOf(currentUser.getLus_id()), now);
									bacManager.insertMstInboxChecklist(inboxChecklist);
									}
							}
								
							String cabangId = bacManager.selectLcaIdAB(null,cabang,2);
							Map<String, Object> params = new HashMap<String, Object>();
						//	String outputDir = props.getProperty("pdf.dir.report") + "\\pengajuan_biaya\\" + cabangId + "\\" + NoBerkas + "\\";
							String outputDir = props.getProperty("pdf.dir") + "\\pengajuan_biaya\\" + cabangId + "\\" + NoBerkas + "\\";
							String outputFilename = "";
							
							params.put("noBerkas",NoBerkas);
							
							outputFilename = NoBerkas+"form_pengajuan_biaya.pdf";		
							JasperUtils.exportReportToPdf(props.getProperty("report.bas.form_Pengajuan_Biaya") + ".jasper", outputDir, outputFilename, params, conn, PdfWriter.AllowPrinting, null, null);
							
							pesan = "Data berhasil di simpan dengan No. Regristrasi : "+NoBerkas ;			
							map.put("pesan", pesan);
							
					}catch (Exception e){						
						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
						map.put("pesan", "Terjadi Kegagalan sistem - silahkan menghubungi IT");
						logger.error("ERROR :", e);
					}finally{
						this.elionsManager.getUwDao().closeConn(conn);
					}		
				}
			
			}
			else{
				map.put("NoBerkas", NoBerkas);
				map.put("tgl_berkas_masuk", hariIni);				
				cabang = ServletRequestUtils.getStringParameter(request, "cabanglist1");
				map.put("namaBank", ServletRequestUtils.getStringParameter(request, "namaBank",""));
				map.put("noRek", ServletRequestUtils.getStringParameter(request, "noRek",""));
				map.put("atasNamaRek", ServletRequestUtils.getStringParameter(request, "atasNamaRek",""));
				statSave = 1;
				map.put("pesan", pesan);
				
			}
		
		}else if(request.getParameter("btnSave2")!=null){
			countnumber = ServletRequestUtils.getIntParameter(request, "countnumber");
			String NoBerkas = ServletRequestUtils.getStringParameter(request, "NoBerkas");
			cabang = bacManager.selectKotaInboxDet(NoBerkas);
			Integer alokasi = ServletRequestUtils.getIntParameter(request, "pilihalokasi");
			String namaBank = ServletRequestUtils.getStringParameter(request, "namaBank","");
			String noRek = ServletRequestUtils.getStringParameter(request, "noRek","");
			String atasNamaRek = ServletRequestUtils.getStringParameter(request, "atasNamaRek","");
			String desc = "Update Request Pengajuan Biaya Cabang " + cabang + " : ";
			pilihalokasi = alokasi;
			List cekRol = null;
			Integer JumlahTtlBiaya = 0;	
			String DescJenis= "";
			
			Integer role = 0; //0 = Sesuai Kebutuhan ; 1 = Bulanan; 2 = (2x) sebulan ; 3 = (1x) 6 bulanan; 4 = Tahunan
			Integer role2 = 0; // untuk perhitungan role = 2 (2x) sebulan
			
			if(countnumber > 0){
				
				//list data detail keperluan
				for(int x= 1; x <= countnumber;x++){
					jenis = ServletRequestUtils.getStringParameter(request, "jenis2"+x,"");
					//tgl_pemakaian = ServletRequestUtils.getStringParameter(request, "tg2"+x,"") +"/"+ ServletRequestUtils.getStringParameter(request, "bl2"+x,"") +"/"+ServletRequestUtils.getStringParameter(request, "th2"+x,"");
					tgl_pemakaian = ServletRequestUtils.getStringParameter(request, "tgl_pemakaian2"+x,"");
					name_item = ServletRequestUtils.getStringParameter(request, "name_item2"+x,"");					
					DescJenis = name_item+" (" +jenis + ") , "+ DescJenis;
					
					if("LISTRIK,TELP,PDAM,SOLAR GENSET,SERVICE CHARGE GEDUNG,ENTERTAINT CAO,ENTERTAINT MARKETING,PULSA MARKETING,KAS KECIL,PERMINTAAN BARANG CETAKAN,PERMINTAAN ATK".indexOf(jenis)>=0){
						role = 1;
					}else if("PERMINTAAN TINTA/TONER".indexOf(jenis)>=0){
						role = 1;
						role2 = 2;
					}else if("SERVICE AC".indexOf(jenis)>=0){
						role = 3;
					}else if("PBB GEDUNG,PAJAK REKLAME,BIAYA PENGURUSAN SURAT DOMISILI".indexOf(jenis)>=0){
						role = 4;
					}else if("LISTRIK TOKEN,PERMOHONAN SUMBANGAN DARI INSTANSI/LINGKUNGAN,RENOVASI KANTOR CABANG,PERBAIKAN KANTOR CABANG,KARANGAN BUNGA KARYAWAN,KARANGAN BUNGA NASABAH".indexOf(jenis)>=0){
						role = 5;
					}
					
					//lakukan validasi untuk masa keperluan
					if(role > 0){	
						cekRol= bacManager.selectCekRolAB(jenis, tgl_pemakaian, cabang, role, 1,NoBerkas );	
											
						
						if(!cekRol.isEmpty()){
								String mi_id;
								String ab_ty;
								String psn = "";
								String ada = "";
								String mi_id1 = "";
								String mi_id2 = "";
								
									for(int i=0; i<cekRol.size();i++){
										Map mp = (HashMap) cekRol.get(i);
										mi_id = (String) mp.get("MI_ID");
										ab_ty = (String) mp.get("AB_TYPE");
										
										if(i==0){
											mi_id1 =  (String) mp.get("MI_ID");	
										}else{
											mi_id2 =  (String) mp.get("MI_ID");	
										}
										
										if(role == 1 && role2 == 0){
												pesan = "Data tidak tersimpan dikarenakan jenis "+ ab_ty + " sudah terdaftar dengan No Regristasi "+ mi_id +" untuk BULAN dengan CABANG yang sama" ;
												statSave = 1;
												pilihalokasi = 60; // biar alokasi jadi ke CA (default) sehingga pada saat pilih atk, kolom transfer bisa hidden
												countnumber = 0 ;// sehingga pada saat input awal karena kegagalan save , no pada list detail = 0
											}else if (role == 4){
												pesan = "Data tidak tersimpan dikarenakan jenis "+ ab_ty + " sudah terdaftar dengan No Regristasi "+ mi_id +" untuk TAHUN dengan CABANG yang sama" ;
												statSave = 1;
												pilihalokasi = 60; 
												countnumber = 0 ;
											}else if (role == 3){
												pesan = "Data tidak tersimpan dikarenakan jenis "+ ab_ty + " sudah terdaftar dengan No Regristasi "+ mi_id +" dalam 6 BULAN ini dengan CABANG yang sama " ;
												statSave = 1;
												pilihalokasi = 60; 
												countnumber = 0 ;
											}else if (role2 == 2){
												if (cekRol.size() >= 2){
													pesan = "Data tidak tersimpan dikarenakan jenis "+ ab_ty + " sudah terdaftar dengan No Regristasi "+ mi_id1+" dan " + mi_id2 + " untuk BULAN dengan CABANG yang sama (req hanya bisa 2 x dalam sebuan)" ;
													statSave = 1;
													pilihalokasi = 60; 
													countnumber = 0 ;
												}
											}else if (role == 5){
												//do nothing
											}
										map.put("pesan", pesan);
									}
									map.put("NoBerkas", NoBerkas);
									map.put("tgl_berkas_masuk", hariIni);										
									map.put("namaBank", ServletRequestUtils.getStringParameter(request, "namaBank",""));
									map.put("noRek", ServletRequestUtils.getStringParameter(request, "noRek",""));
									map.put("atasNamaRek", ServletRequestUtils.getStringParameter(request, "atasNamaRek",""));
									
							}
					}else{
						map.put("pesan", "Data tidak bisa tersimpan karena list ini "+ jenis+", belum terdaftar");
						statSave = 1;
					}
					
				
				}
										
			if (statSave != 1){
				Connection conn = null;
				try{
							conn = this.elionsManager.getUwDao().getDataSource().getConnection();
							MstInboxHist mstInboxHist = new MstInboxHist(NoBerkas, 207, 207, null, 
																		null, desc+DescJenis, Integer.valueOf(currentUser.getLus_id()), now,
																		null,0,0);
							
							bacManager.insertMstInboxHist( mstInboxHist);
							bacManager.deleteMstInboxDet( NoBerkas);
							
							if(countnumber > 0){
								for(int r= 1; r <= countnumber;r++){
									SubNoBerkas = ServletRequestUtils.getIntParameter(request, "noUrut2"+r,0);
									name_item = ServletRequestUtils.getStringParameter(request, "name_item2"+r,"");
									qty = ServletRequestUtils.getIntParameter(request, "qty2"+r,1);
									jenis = ServletRequestUtils.getStringParameter(request, "jenis2"+r,"");
									//tgl_pemakaian = ServletRequestUtils.getStringParameter(request, "tg2"+r,"") +"/"+ ServletRequestUtils.getStringParameter(request, "bl2"+r,"") +"/"+ServletRequestUtils.getStringParameter(request, "th2"+r,"");
									tgl_pemakaian = ServletRequestUtils.getStringParameter(request, "tgl_pemakaian2"+r,"");
									biaya = ServletRequestUtils.getIntParameter(request, "biaya2"+r,0);
									ket = ServletRequestUtils.getStringParameter(request, "ket2"+r,"");
									
										
									Date tp;
									if(tgl_pemakaian==null) tp = null; else tp = df.parse(tgl_pemakaian);
								
									MstInboxDet mstInboxDet = new MstInboxDet( NoBerkas,  SubNoBerkas,  name_item.toUpperCase(),  null,
																			  1,  biaya,  null,  namaBank.toUpperCase(),
																			  atasNamaRek.toUpperCase(),  noRek,  null,  null,
																			  ket,  cabang,  null,  qty,
																			  jenis,  tp);
									
									bacManager.insertMstInboxDet(mstInboxDet);
								}
							}
							
							//set inbox checklist mi_flag = 0 untuk LC_ID = 150
							MstInboxChecklist inboxChecklist = new MstInboxChecklist(NoBerkas, alokasi, 150, 0, "REVISI", Integer.valueOf(currentUser.getLus_id()), now);
							bacManager.updateMstInboxChecklist(inboxChecklist);
							
							//set tanggal berkas kirim = 0 (dalam data base di record pada field TGL_BERKAS_MASUK)
							bacManager.updateTglPengirimanBerkas(NoBerkas, null);
						
								
							String cabangId = bacManager.selectLcaIdAB(null,cabang,2);
							Map<String, Object> params = new HashMap<String, Object>();
						//	String outputDir = props.getProperty("pdf.dir.report") + "\\pengajuan_biaya\\" + cabangId + "\\" + NoBerkas + "\\";
							String outputDir = props.getProperty("pdf.dir") + "\\pengajuan_biaya\\" + cabangId + "\\" + NoBerkas + "\\";
							String outputFilename = "";
							
							params.put("noBerkas",NoBerkas);
							
							outputFilename = NoBerkas+"form_pengajuan_biaya_REV.pdf";		
							JasperUtils.exportReportToPdf(props.getProperty("report.bas.form_Pengajuan_Biaya") + ".jasper", outputDir, outputFilename, params, conn, PdfWriter.AllowPrinting, null, null);
							
							pesan = "Data dengan No. Regristrasi : "+NoBerkas+" telah berhasil di perbaharui, silahkan melakukan pencetakan form dan mengupload hasil scan kembali" ;			
							map.put("pesan", pesan);
					}catch (Exception e){						
						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
						map.put("pesan", "Terjadi Kegagalan sistem - silahkan menghubungi IT");
						logger.error("ERROR :", e);
					}finally{
						this.elionsManager.getUwDao().closeConn(conn);
					}
				}
			
			}else{
				map.put("NoBerkas", NoBerkas);
				map.put("tgl_berkas_masuk", hariIni);				
				cabang = ServletRequestUtils.getStringParameter(request, "cabanglist1");
				map.put("namaBank", ServletRequestUtils.getStringParameter(request, "namaBank",""));
				map.put("noRek", ServletRequestUtils.getStringParameter(request, "noRek",""));
				map.put("atasNamaRek", ServletRequestUtils.getStringParameter(request, "atasNamaRek",""));
				statSave = 1;
				map.put("pesan", pesan);
				
			}
			
		}else if(request.getParameter("btnPrint")!=null){
			
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try{
				String NoBerkas = ServletRequestUtils.getStringParameter(request, "NoBerkas");
				String lca_ids = bacManager.selectLcaIdAB(NoBerkas,null,1);				
				
			  //String outputDir = props.getProperty("pdf.dir.report") + "\\pengajuan_biaya\\" + lca_ids + "\\" + NoBerkas + "\\";
				String outputDir = props.getProperty("pdf.dir") + "\\pengajuan_biaya\\" + lca_ids + "\\" + NoBerkas + "\\";
				
				List cekRev = bacManager.selectListInboxCheck(NoBerkas,null, 2);
				
				
				if(!cekRev.isEmpty()){
					in = new FileInputStream(outputDir+NoBerkas+"Form_Pengajuan_Biaya_REV.pdf");
				}else{
					in = new FileInputStream(outputDir+NoBerkas+"Form_Pengajuan_Biaya.pdf");
				}
				
			    response.setContentType("application/pdf");
			    if(!cekRev.isEmpty()){
			    	response.setHeader("Content-Disposition", "Attachment;filename="+NoBerkas+"Form_Pengajuan_Biaya_REV.pdf");
				}else{
					response.setHeader("Content-Disposition", "Attachment;filename="+NoBerkas+"Form_Pengajuan_Biaya.pdf");
				}
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
			    ouputStream = response.getOutputStream();
			    IOUtils.copy(in, ouputStream);
			} catch (Exception e) {
				map.put("pesan", "Ada kesalahan dalam generate Form - Form not found.");
				logger.error("ERROR :", e);
			}finally{
	            try {
	            	if(in != null) {
	            		in.close();
	            	}
	            	if(ouputStream != null) {
	            		ouputStream.flush();
	            		ouputStream.close();
	            	}  
	            } catch (Exception e) {
	                   // TODO Auto-generated catch block
	                   logger.error("ERROR", e);
	            }

			}
			return null;
		}
		else if(request.getParameter("btnDel")!=null){
			try{
			String NoBerkas = ServletRequestUtils.getStringParameter(request, "NoBerkas");		
//			hariIni = bacManager.selectSysdateddmmyyy();
			
				MstInbox mstInbox = new MstInbox(NoBerkas, null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null, null, 1, now, Integer.valueOf(currentUser.getLus_id()),null, null,null);
			
			
				MstInboxHist mstInboxHist = new MstInboxHist(NoBerkas, 207, 207, null, null, "Pengajuan Berkas Biaya di Batalkan", Integer.valueOf(currentUser.getLus_id()), now, null,0,0);
			
				bacManager.updateMstInbox(mstInbox);
				bacManager.insertMstInboxHist(mstInboxHist);

				map.put("pesan", "Pengajuan Biaya berhasil di batalkan");
		
			
			} catch (Exception e) {
				map.put("pesan", "Terjadi kesalahan - Pengajuan Biaya belum berhasil di batalkan");
				logger.error("ERROR :", e);
				
			}
		}else if(request.getParameter("btnTrans2")!=null){
			String NoBerkas = ServletRequestUtils.getStringParameter(request, "NoBerkas");
			Integer alokasi = ServletRequestUtils.getIntParameter(request, "pilihalokasi");	
			String desc ="";
			List cekUpload = bacManager.selectListInboxCheck(NoBerkas,0, 1);	
			List cekTglKirim = bacManager.selectListInboxCheck(NoBerkas,null, 0);
			
			
			if(cekUpload.isEmpty() && !cekTglKirim.isEmpty()  ){
				
				desc="Tranfer Berkas ke BAS";
				
				MstInbox mstInbox = new MstInbox(NoBerkas, null, 208, null, 207, null, null, null, 
						null, null, null, null, null, Integer.valueOf(currentUser.getLus_id()), now, null, null, null, null, 
						null, null, null, null, null, null, null, null,null, null,null);
				
				MstInbox lockid = new MstInbox(NoBerkas,3041);
				
				MstInboxHist mstInboxHist = new MstInboxHist(NoBerkas, 207, 208, null, null, desc, Integer.valueOf(currentUser.getLus_id()), now, null,0,0);
				
				try{
						bacManager.updateMstInbox(mstInbox);
						bacManager.updateMstInbox(lockid);
						bacManager.insertMstInboxHist(mstInboxHist);
						pesan= "Data Berhasil di transfer ke BAS.";
				}
				catch (Exception e) {
						pesan= "Data Tidak Berhasil di tranfer, Silahkan hubungin IT.";
						logger.error("ERROR :", e);
				}
				
				
			}else if(!cekUpload.isEmpty()){
				for(int i=0; i<cekUpload.size();i++){
					Map mp = (HashMap) cekUpload.get(i);
					desc = desc + " , "+(String) mp.get("LC_NAMA");	
				}					
				pesan = ("Data Tidak berhasil di transfer,Mohon melengkapi proses upload document "+desc+" terlebih dahulu !");
			}else if(cekTglKirim.isEmpty()){
				pesan = ("Data Tidak berhasil di transfer,Mohon melengkapi tanggal pengiriman berkas terlebih dahulu !");
			}
			
			
			map.put("pesan", pesan);
			
		}
		
		String lcaId = currentUser.getLca_id();
		cabang = currentUser.getCabang();
		
		if(lcaId.equals("01")){
			map.put("dataInbox", bacManager.selectListAjuanBiaya(lcaId,cabang,null,null));
		}else{
			map.put("dataInbox", bacManager.selectListAjuanBiaya(lcaId,cabang,3,null));
		}
		
		if(lcaId.equals("01")){
			map.put("dataHistory", bacManager.selectHistoryBerkas(null,null, null));
		}else{
			map.put("dataHistory", bacManager.selectHistoryBerkas(2,null,cabang));
		}
		
		if(lcaId.equals("01")){
			map.put("cabangAll",bacManager.selectAllLstCabangAB(null,null));
		}else{
			map.put("cabangAll",bacManager.selectAllLstCabangAB(currentUser.getLca_id(), 1));
		}
		
		
		
		map.put("namaCabang",cabang);
		map.put("countnumber",countnumber);
		map.put("statBtn",statBtn); // 0 = default , 1 = new inbox , 2 = datainbox
		map.put("statSave",statSave); // 0 = default (biar tombol save dan cancel hidden) 1 = gagal save(tombol save and cancel tetepa muncul)
		map.put("pilihalokasi",pilihalokasi);
	//	map.put("dataDetailItem", bacManager.selectDataNoBerkasDet("2016567783"));
		return new ModelAndView("snows/ajuBiaya_InputBerkas", map);
	}
	
	/*
	protected Connection connection = null;
	
	protected Connection getConnection() {
		if(this.connection==null) {
			try {
				this.connection = this.elionsManager.getUwDao().getDataSource().getConnection();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		}
		return this.connection;
	}
	*/
	
	public ModelAndView upload_ajuanBiaya(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		
		String NoBerkas = ServletRequestUtils.getStringParameter(request, "NoBerkas");
		User user = (User) request.getSession().getAttribute("currentUser");
		String lca_id = bacManager.selectLcaIdAB(NoBerkas,null,1);
	  //String dir = props.getProperty("pdf.dir.export") + "//pengajuan_biaya//" + lca_id; 
		String dir = props.getProperty("pdf.dir") + "//pengajuan_biaya//" + lca_id; 
		
		Integer lsbsId =null;
		Integer lsdbsNumber =null;
		
		List<Scan> daftarScan =null;
		
		Map<String, Object> cmd = new HashMap<String, Object>();
		Upload upload = new Upload();
		
			daftarScan = bacManager.selectLstScanAB(NoBerkas);
				

		upload.setDaftarFile(new ArrayList<MultipartFile>(daftarScan.size()));

		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
				
	
		cmd.put("direktori", dir + "//" + NoBerkas);
		
		List<DropDown> daftarFile = new ArrayList<DropDown>();
		for(Scan s : daftarScan) {
			daftarFile.add(new DropDown(s.nmfile, s.ket, String.valueOf(s.wajib)));
		}
		cmd.put("daftarFile", daftarFile);
		
		if(request.getParameter("upload")!=null) {
			
			File destDir = new File(dir + "//" + NoBerkas);
			if(!destDir.exists()) {
				destDir.mkdirs();
			}
			StringBuffer filenames = new StringBuffer();
//			Date nowDate = elionsManager.selectSysdate1("dd", false, 0);
			Date nowDate = new Date();
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
							return new ModelAndView("snows/ajuBiaya_Upload", cmd);
						}
					}
            }
			Integer hasil = 0;
            hasil = bacManager.selectProsesUploadScanAB (NoBerkas, lca_id, upload, destDir, daftarScan,filenames, currentUser);//true berarti berhasil, false gagal.
			
            
			if(hasil==1){//*data berhasil diupload				
					cmd.put("pesan", "Data berhasil di-upload.");			
				
			}
			if(hasil==0){//*data gagal diupload
				cmd.put("pesan", "Data untuk No Registrasi "+NoBerkas+" tidak berhasil di-upload.Silakan hubungi IT");
			}
			
			
		} 

		List<DropDown> daftarAda = listFilesInDirectory(dir +"/"+ NoBerkas);
		cmd.put("daftarAda", 	daftarAda);

		return new ModelAndView("snows/ajuBiaya_Upload", cmd);
	}
	
	//listing file di dalam suatu directory
		public static List<DropDown> listFilesInDirectory(String dir) {
			File destDir = new File(dir);
			List<DropDown> daftar = new ArrayList<DropDown>();
			if(destDir.exists()) {
				String[] children = destDir.list();
				daftar = new ArrayList<DropDown>();
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				for(int i=0; i<children.length; i++) {
					File file = new File(destDir+"/"+children[i]);
					if(!children[i].contains("REINS_"))
						daftar.add(new DropDown(children[i], df.format(new Date(file.lastModified())), dir));
				}
			}
			return daftar;
		}
	
		public ModelAndView viewer_ajuanBiaya(HttpServletRequest request, HttpServletResponse response) throws Exception{
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			Map map = new HashMap();
			
			String NoBerkas = ServletRequestUtils.getStringParameter(request, "NoBerkas");
			
			String lca_id = bacManager.selectLcaIdAB(NoBerkas,null,1);
						
		  //String dest = props.getProperty("pdf.dir.export");
			String dest = props.getProperty("pdf.dir");
			
			
			List list = new ArrayList();
			for(int i=1;i<=2;i++){
				list.add(i);
			}
			map.put("perulangan", list);

			
			String directory = dest+"\\pengajuan_biaya\\"+lca_id+"\\"+NoBerkas;
			List<DropDown> daftarFile = com.ekalife.utils.FileUtils.listFilesInDirectory(directory);
			
			map.put("NoBerkas", NoBerkas);
			map.put("lca_id", lca_id);
			map.put("daftarFile", daftarFile);
			
			return new ModelAndView("snows/ajuBiaya_view_doc_list",map);
		}
		
		public ModelAndView load_viewer_ajuanBiaya(HttpServletRequest request, HttpServletResponse response) throws Exception{
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			Map map = new HashMap();
		
			String NoBerkas = ServletRequestUtils.getStringParameter(request, "NoBerkas",null);
			String lca_id = ServletRequestUtils.getStringParameter(request, "lca_id",null);
			
			//String dest = props.getProperty("pdf.dir.export");
			String dest = props.getProperty("pdf.dir");
			
			List ni = bacManager.selectNmFileChecklist(NoBerkas, null);
			String ljj_folder = null, ljj_file = null;
			for(int i=0;i<ni.size();i++){
				Map a_ni = (HashMap) ni.get(i);
				ljj_folder = (String) a_ni.get("LJJ_FOLDER");
				ljj_file = (String) a_ni.get("NMFILE");
				lca_id = (String) a_ni.get("LCA_ID");
			}
			
			
			String directory = dest+"\\pengajuan_biaya\\"+lca_id+"\\"+NoBerkas;
			
			String fileName = null;
			if((fileName = request.getParameter("file")) != null) {
				Document document = new Document();
				try{
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
				}catch(DocumentException de){
					logger.error(de);
		    		ServletOutputStream out = response.getOutputStream();
		    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
		    		out.flush();
				}
				if(document!=null){
					document.close();
				}
			}
			return null;
		}
		
		
}
