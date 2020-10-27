package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;
import id.co.sinarmaslife.std.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class AkseptasiPasController extends ParentFormController{
	private long accessTime;
	protected final static Log logger = LogFactory.getLog( AkseptasiPasController.class );
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
	}
	
	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
    {
		//Pas pas=(Pas)cmd;
		
		List lsJnsId=elionsManager.selectAllLstIdentity();
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = (User) request.getSession().getAttribute("currentUser");
		String msp_id = request.getParameter("msp_id");
		String action = request.getParameter("action");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		Integer flagNew = 1;
		map.put("user_id", lus_id);
		List<Pas> pas = uwManager.selectAllPasList(msp_id, "2", null, null, null, "pas", null, "pas_aksep",null,null,null);
		Pas p = pas.get(0);
		
		if("aksep".equals(action)){
			//p.setLus_id(lus_id);
			p.setLus_id_uw_pas(lus_id);
			p.setLus_login_name(user.getLus_full_name());
			p.setMsp_pas_accept_date(elionsManager.selectSysdate());
			//p.setMsp_fire_accept_date(elionsManager.selectSysdate());
			p.setMsp_flag_aksep(1);
			p.setLspd_id(2);
			//pas dipisah dengan fire - msp_fire_export_flag tidak diupdate
			//p.setMsp_fire_export_flag(0);
			//p.setMsp_fire_export_flag(5);
			p.setLssp_id(10);
			p.setLssa_id(5);
			p.setMsp_kode_sts_sms("00");
			p = uwManager.updatePas(p);//request, pas, errors,"input",user,errors);
			if(p.getStatus() == 1){
				request.setAttribute("successMessage","proses aksep gagal");
			}else{
				request.setAttribute("successMessage","aksep sukses");
			}
		}else if("leader".equals(action)){
			String new_msag_id = elionsManager.selectMsagLeader(p.getMsag_id());
			int aktif = 1;
			Map agentMap = elionsManager.selectagenpenutup(new_msag_id);
			if(agentMap == null){
				aktif = 0;
			}
			if(aktif == 0){// agen leader tidak aktif
				request.setAttribute("successMessage","proses perubahan agen ke "+new_msag_id+"  gagal");
			}else{
				p = uwManager.updateAgenPas(p, action, new_msag_id, lus_id);
				if(p.getStatus() == 1){
					request.setAttribute("successMessage","proses perubahan agen ke "+new_msag_id+" gagal");
				}else{
					request.setAttribute("successMessage","perubahan agen ke "+new_msag_id+" sukses");
				}
			}
		}else if("transfer".equals(action)){
			//cek agen apakah aktif atau tidak
			int aktif = 1;
			Map agentMap = elionsManager.selectagenpenutup(p.getMsag_id());
			if(agentMap == null){
				aktif = 0;
			}
			if(aktif == 0){// agen tidak aktif
				request.setAttribute("nama",p.getMsp_fire_id());
				request.setAttribute("msp_id",p.getMsp_id());
				request.setAttribute("agen_baru",elionsManager.selectMsagLeader(p.getMsag_id()));
				request.setAttribute("successMessage","Agen tidak aktif");
			}else{// agen aktif
				Cmdeditbac edit = new Cmdeditbac();
				//p.setLus_id(lus_id);
				p.setLus_id_uw_pas(lus_id);
				p.setLus_login_name(user.getLus_full_name());
				//langsung set ke posisi payment
				p.setLspd_id(4);
				edit=this.uwManager.prosesPas(request, "update", p, errors,"input",user,errors);
				try {
					
					String directory = props.getProperty("pdf.dir")+"\\Polis\\fire\\";
					
					List<DropDown> daftarFile = FileUtil.listFilesInDirectory(directory);
					List<DropDown> daftarFile2 = new ArrayList<DropDown>();
					
					List<DropDown> boleh = new ArrayList<DropDown>();
					boleh.add(new DropDown(p.getMsp_fire_id(), "PAS"));
					
					if(p.getMsp_fire_id()!=null && edit.getPemegang().getMspo_policy_no()!=null){
					
						for(DropDown d : daftarFile) {
							for(DropDown s : boleh) {
								if(d.getKey().toLowerCase().contains(s.getKey()) && !d.getKey().toLowerCase().contains("decrypted")) {
									d.setDesc(s.getValue());
									daftarFile2.add(d);
								}
							}
						}
						
						for(DropDown d2 : daftarFile2){
							copyfile("\\ebserver\\pdfind\\Polis\\fire\\"+d2.getKey(), "\\ebserver\\pdfind\\Polis\\"+edit.getPemegang().getLca_id()+"\\"+d2.getKey().replace(p.getMsp_fire_id(), "SPAJ"));
							deleteFile("\\ebserver\\pdfind\\Polis\\fire\\", d2.getKey(), null);
						}
					
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e);
				}
				if(edit.getPemegang().getMspo_policy_no() == null){
					request.setAttribute("successMessage","proses transfer gagal");
				}else{
					request.setAttribute("successMessage","transfer sukses dengan SPAJ : "+edit.getPemegang().getReg_spaj()+" dan No. Polis : "+edit.getPemegang().getMspo_policy_no());
				}
				
				//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
			}
		}else if("resend".equals(action)){
			Cmdeditbac edit = new Cmdeditbac();
			p.setLus_id(lus_id);
			p.setLus_id_uw_pas(lus_id);
			p.setLus_login_name(user.getLus_full_name());
			p.setMsp_fire_export_flag(2);
			p = uwManager.updatePas(p);
			if(p.getStatus() == 1){
				request.setAttribute("successMessage","proses resend gagal");
			}else{
				request.setAttribute("successMessage","resend sukses");
			}
			//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
		}else if("reject".equals(action)){
			Cmdeditbac edit = new Cmdeditbac();
			p.setLus_id(lus_id);
			p.setLus_id_uw_pas(lus_id);
			p.setLus_login_name(user.getLus_full_name());
			p.setMsp_fire_export_flag(6);
			p = uwManager.updatePas(p);
			if(p.getStatus() == 1){
				request.setAttribute("successMessage","proses reject gagal");
			}else{
				request.setAttribute("successMessage","reject sukses");
			}
			//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
		}
			
		pas = uwManager.selectAllPasList(null, "2", null, null, null, "pas", null, "pas_aksep",null,null,null);
		
		return new ModelAndView("uw/akseptasi_pas").addObject("cmd",pas);

    }
	
	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
		logger.debug("EditBacController : initBinder");
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		logger.debug("EditBacController : formBackingObject");
        this.accessTime = System.currentTimeMillis();
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		//Map<String, Object> map = new HashMap<String, Object>();
		
		String tipe = request.getParameter("tipe");
		String kata = request.getParameter("kata");
		String pilter = request.getParameter("pilter");
		
		List<Pas> pas = uwManager.selectAllPasList(null, "2", tipe, kata, pilter, "pas", null, "pas_aksep",null,null,null);
		
		
		String directory = props.getProperty("pdf.dir.arthamas.dokumenAgen");
		for(int i = 0 ; i < pas.size() ; i++){
			if(pas.get(i).getMsag_id_pp() != null){
				String kode_agen = pas.get(i).getMsag_id_pp();
				String directoryAgen = directory + "\\" + kode_agen;
				File file = new File(directoryAgen);
				if(file.isDirectory()){
					String[] files = file.list();
					if(files.length > 0){
						pas.get(i).setDirAgenBp(1);
					}else{
						pas.get(i).setDirAgenBp(0);
					}
				}else{
					pas.get(i).setDirAgenBp(0);
				}
			}
		}
		
		//map.put("pasList", pas);
		//request.setAttribute("cmd", pas);
		request.setAttribute("result","");
		return pas;
	}
	
	/**
	 * copy file
	 * @param srFile
	 * @param dtFile
	 * Filename : FileUtil.java
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : Jun 1, 2010 3:07:59 PM
	 */
	public static void copyfile(String srFile, String dtFile){
		InputStream in = null;
		OutputStream out = null;
	    try{
	    	File f1 = new File(srFile);
	    	File f2 = new File(dtFile);
	    	in = new FileInputStream(f1);
	      
//	    	For Append the file.
//	    	OutputStream out = new FileOutputStream(f2,true);

//	    	For Overwrite the file.
	    	out = new FileOutputStream(f2);

	    	byte[] buf = new byte[1024];
	    	int len;
	    	while ((len = in.read(buf)) > 0){
	    		out.write(buf, 0, len);
	    	}
	    	logger.info("File copied.");
	    }
	    catch(FileNotFoundException ex){
	    	logger.error(ex.getMessage() + " in the specified directory.");
	    	logger.error("ERROR : ", ex);
	    }
	    catch(IOException e){
	    	logger.error("ERROR :", e);      
	    }finally {
	    	try {
	    		if(in != null) {
	    			in.close();
	    		}
	    		if(out != null) {
	    			out.close();
	    		}
	    	}catch (Exception e) {
				logger.error("ERROR :", e);
			}
	    }
	}
	
	public static boolean deleteFile(String destDir, String fileName, HttpServletResponse response) throws FileNotFoundException,
		IOException {
		File file = new File(destDir + "/" + fileName);
		if (file.exists()) return file.delete();
			return false;
	}
}
