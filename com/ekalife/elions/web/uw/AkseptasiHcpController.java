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

public class AkseptasiHcpController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
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
		List<Pas> pas = uwManager.selectAllPasList(msp_id, "2", null, null, null, "hcp", null, "hcp",null,null,null);
		Pas p = pas.get(0);
		
		if("aksep".equals(action)){
			p.setLus_id_uw_pas(lus_id);
			p.setLus_login_name(user.getLus_full_name());
			p.setMsp_pas_accept_date(elionsManager.selectSysdate());
			p.setMsp_flag_aksep(1);
			p.setLspd_id(2);
			p.setLssp_id(10);
			p.setLssa_id(5);
			p.setMsp_kode_sts_sms("00");
			p = uwManager.updatePas(p);//request, pas, errors,"input",user,errors);
			if(p.getStatus() == 1){
				request.setAttribute("successMessage","proses aksep gagal");
			}else{
				request.setAttribute("successMessage","aksep sukses");
			}
		}else if("transfer".equals(action)){
			Cmdeditbac edit = new Cmdeditbac();
			//p.setLus_id(lus_id);
			p.setLus_id_uw_pas(lus_id);
			p.setLus_login_name(user.getLus_full_name());
			p.setLspd_id(4);
			edit=this.uwManager.prosesHcp(request, "update", p, errors,"input",user,errors);
			if(edit.getPemegang().getMspo_policy_no() == null){
				request.setAttribute("successMessage","proses transfer gagal");
			}else{
				request.setAttribute("successMessage","transfer sukses dengan SPAJ : "+edit.getPemegang().getReg_spaj()+" dan No. Polis : "+edit.getPemegang().getMspo_policy_no());
			}
			
			//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
		}
			
		pas = uwManager.selectAllPasList(null, "2", null, null, null, "hcp", null, "hcp",null,null,null);
		
		return new ModelAndView(
        "uw/akseptasi_hcp").addObject("cmd",pas);

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
		
		List<Pas> pas = uwManager.selectAllPasList(null, "2", tipe, kata, pilter, "hcp", null, "hcp",null,null,null);
		
//		String directory = props.getProperty("pdf.dir.arthamas.dokumenAgen");
//		for(int i = 0 ; i < pas.size() ; i++){
//			if(pas.get(i).getMsag_id_pp() != null){
//				String kode_agen = pas.get(i).getMsag_id_pp();
//				String directoryAgen = directory + "\\" + kode_agen;
//				File file = new File(directoryAgen);
//				if(file.isDirectory()){
//					String[] files = file.list();
//					if(files.length > 0){
//						pas.get(i).setDirAgenBp(1);
//					}else{
//						pas.get(i).setDirAgenBp(0);
//					}
//				}else{
//					pas.get(i).setDirAgenBp(0);
//				}
//			}
//		}
		
		//map.put("pasList", pas);
		//request.setAttribute("cmd", pas);
		request.setAttribute("result","");
		return pas;
	}
}
