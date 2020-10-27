package com.ekalife.elions.web.uw;


import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;
import id.co.sinarmaslife.std.util.FileUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;


import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.BlackList;
import com.ekalife.elions.model.BlackListFamily;
import com.ekalife.elions.model.Client;
import com.ekalife.elions.model.CmdInputBlacklist;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.DetBlackList;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.ReffBii;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class PasDetailController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	private Properties props;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	public void setProps(Properties props) {
		this.props = props;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		
		String msp_id = request.getParameter("msp_id");
		String action = request.getParameter("action");
		
		User user = (User) request.getSession().getAttribute("currentUser");
		List<Pas> pasList = new ArrayList<Pas>();
		
		//validasi untuk mall - kelengkapan data
		if("58".equals(user.getLca_id())){
			pasList = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pas", null, "mall",user.getLus_admin(),null,null);
			Pas pas = pasList.get(0);
			
			if("transfer".equals(action)){
				
				int err = 0;
				
				if(pas.getMsp_pas_tmp_lhr_pp() == null){
					err = 1;			
				}
				if(pas.getMsp_pas_tmp_lhr_tt() == null){
					err = 1;
				}
				if(pas.getMsp_identity_no() == null){
					err = 1;
				}
				if(pas.getMsp_identity_no_tt() == null){
					err = 1;
				}
				
				if(pas.getMsp_flag_cc() != null && pas.getMsp_flag_cc() == 2){//tabungan
					if(pas.getLsbp_id() == null){
						err = 1;
					}
					if(pas.getMsp_no_rekening() == null){
						err = 1;			
					}
					if(pas.getMsp_rek_cabang() == null){
						err = 1;
					}
					if(pas.getMsp_rek_kota() == null){
						err = 1;
					}
					if(pas.getMsp_rek_nama() == null){
						err = 1;
					}
					if(pas.getLsbp_id_autodebet() == null){
						err = 1;
					}
					if(pas.getMsp_no_rekening_autodebet() == null){
						err = 1;
					}
					if(pas.getMsp_tgl_debet() == null){
						err = 1;
					}
					if(pas.getMsp_rek_nama_autodebet() == null){
						err = 1;
					}
				}else if(pas.getMsp_flag_cc() != null && pas.getMsp_flag_cc() == 1){//kartu kredit
					if(pas.getLsbp_id() == null){
						err = 1;
					}
					if(pas.getLsbp_id_autodebet() == null){
						err = 1;			
					}
					if(pas.getMsp_no_rekening_autodebet() == null){
						err = 1;
					}
					if(pas.getMsp_tgl_debet() == null){
						err = 1;
					}
					if(pas.getMsp_tgl_valid() == null){
						err = 1;
					}
					if(pas.getMsp_rek_nama_autodebet() == null){
						err = 1;
					}
				}
				
				if(err == 1){
					errors.addError(null);
					request.setAttribute("successMessage","Mohon lengkapi data terlebih dahulu, untuk dapat mentransfer");
				}else{
					//request.setAttribute("successMessage","transfer sukses");
				}
			}
		}	
	}
	
	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
    {
		//Pas detiledit = (Pas) cmd;
		
		String msp_id = request.getParameter("msp_id");
		String action = request.getParameter("action");
		
		User user = (User) request.getSession().getAttribute("currentUser");
		List<Pas> pas = new ArrayList<Pas>();
		if("58".equals(user.getLca_id())){
			pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pas", null, "mall",user.getLus_admin(),null,null);
		}else{
			pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pas", null, "individu",null,null,null);
		}
		
		Integer lus_id = Integer.parseInt(user.getLus_id());
		Pas p = pas.get(0);
		
		if("transfer".equals(action)){
			if(p.getReg_spaj() == null)p.setReg_spaj("");
			//Date end_date = elionsManager.selectSysdate();
			//end_date.setYear(end_date.getYear()+1);
			//end_date.setDate(end_date.getDate()-1);
			//p.setLus_id(lus_id);
			p.setLus_login_name(user.getLus_full_name());
			p.setMsp_tgl_transfer(elionsManager.selectSysdate());
			p = uwManager.updatePas1(p, lus_id);//request, pas, errors,"input",user,errors);
			
			/* Automated underwriting starts here */
			/* SEGEL DIBUKA KALAU UW MINTA UNTUK AUTOMATED -> AUTHOR DARU -> COMMENT BY ANDY
			// Pertama2, aksep dulu
			p.setLus_id_uw_pas(lus_id);
			p.setLus_login_name(user.getLus_full_name());
			p.setMsp_pas_accept_date(elionsManager.selectSysdate());
			//p.setMsp_fire_accept_date(elionsManager.selectSysdate());
			p.setMsp_flag_aksep(1);
			p.setLspd_id(2);
			//pas dipisah dengan fire - msp_fire_export_flag tidak diupdate
			//p.setMsp_fire_export_flag(0);
			//p.setMsp_fire_export_flag(5);
			p.setLssp_id(10);//POLICY IS BEING PROCESSED 
			p.setMsp_kode_sts_sms("00");
			p = uwManager.updatePas(p);//request, pas, errors,"input",user,errors);
			
			// Abis itu baru transfer ke payment
			//cek agen apakah aktif atau tidak
			int aktif = 1;
			Map agentMap = elionsManager.selectagenpenutup(p.getMsag_id());
			if(agentMap == null){
				aktif = 0;
			}
			if(aktif == 0){// agen tidak aktif
				request.setAttribute("nama",p.getMsp_fire_id());
				request.setAttribute("msp_id",p.getMsp_id());
				//request.setAttribute("agen_baru",elionsManager.selectMsagLeader(p.getMsag_id()));
				request.setAttribute("successMessage","Agen tidak aktif, harap hubungi bagian underwriting.");
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
							AkseptasiPasController.copyfile("\\ebserver\\pdfind\\Polis\\fire\\"+d2.getKey(), "\\ebserver\\pdfind\\Polis\\"+edit.getPemegang().getLca_id()+"\\"+d2.getKey().replace(p.getMsp_fire_id(), "SPAJ"));
							AkseptasiPasController.deleteFile("\\ebserver\\pdfind\\Polis\\fire\\", d2.getKey(), null);
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
			}
			*/
			/* Automated underwriting ends here */
			
			/**///TUTUP DARI SINI : SAAT UW MINTA UNTUK AUTOMATED
			 
			if(p.getStatus() == 1){
				request.setAttribute("successMessage","proses transfer gagal");
			}else{
				request.setAttribute("successMessage","transfer sukses");
				if(p.getJenis_pas().equals("MALLINSURANCE")){
					try{
						sendEmailPASMallAssurance(request, cmd, p);
						request.setAttribute("successMessage", "E-Mail PAS-Mallassurance sukses terkirim");
					}catch(Exception e){
						request.setAttribute("successMessage", "E-Mail PAS-Mallassurance gagal terkirim");
					}
				}
				
			}
			/**///TUTUP SAMPAI SINI : SAAT UW MINTA UNTUK AUTOMATED
			
			if("58".equals(user.getLca_id())){
				if(p.getStatus() == 1){
					pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pas", null, "mall",user.getLus_admin(),null,null);
				}else{
					pas = uwManager.selectAllPasList(null, "1", null, null, null, "pas", null, "mall",null,null,null);
				}
			}else{
				if(p.getStatus() == 1){
					pas = uwManager.selectAllPasList(msp_id, "1", null, null, null, "pas", null, "individu",user.getLus_admin(),null,null);
				}else{
					pas = uwManager.selectAllPasList(null, "1", null, null, null, "pas", null, "individu",null,null,null);
				}
			}
			
		}
		
		return new ModelAndView(
        "uw/pas_detail").addObject("cmd",pas);

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
		//currentUser.setLca_id("58");
		String tipe = request.getParameter("tipe");
		String kata = request.getParameter("kata");
		String pilter = request.getParameter("pilter");
		
		List<Pas> pas = new ArrayList<Pas>();
		
		if("58".equals(currentUser.getLca_id())){
			pas = uwManager.selectAllPasList(null, "1", tipe, kata, pilter, "pas", null, "mall",currentUser.getLus_admin(),null,null);
		}else{
			pas = uwManager.selectAllPasList(null, "1", tipe, kata, pilter, "pas", null, "individu",null,null,null);
		}
		
		if("58".equals(currentUser.getLca_id())){
			String np = request.getParameter("np");
			
			if(np != null){
				request.setAttribute("popUpInsert", "open");
			}
		}
		
		//map.put("pasList", pas);
		//request.setAttribute("cmd", pas);
		request.setAttribute("lus_admin", currentUser.getLus_admin());
		request.setAttribute("lca_id",currentUser.getLca_id());
		return pas;
	}
	
	private String sendEmailPASMallAssurance( HttpServletRequest request, Object command, Pas pas)
	{
		String result = "";
		//User currentUser = ( User ) request.getSession().getAttribute( "currentUser" );			
		HttpSession session = request.getSession();	
		//SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		
		String namaTT = pas.getMsp_full_name();
		String noKartu = pas.getNo_kartu();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date tglTrf = pas.getMsp_tgl_transfer();
		String tglTransfer = dateFormat.format(tglTrf);
	
		List temp = new ArrayList();
					
		try
        {
            email.send(
                    // TODO: use this params
            		true,            		
            		"info@sinarmasmsiglife.co.id",				
					new String[]{"UnderwritingDMTM@sinarmasmsiglife.co.id"}, 
					null,
					new String[]{"andy@sinarmasmsiglife.co.id","adrian_n@sinarmasmsiglife.co.id"},
            		"Data Transfer PAS MallAssurance",
            		"INFO UNTUK UNDERWRITING:" + "\n" +
            		"PAS MallAssurance dengan nomor kartu "+ noKartu + "\n" +
            		"Atas Nama    : " + namaTT + "\n" +
            		"pd tgl " + tglTransfer  +" telah ditransfer ke posisi Underwriting, silakan diproses selanjutnya." + "\n"+
            		"INFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.",                 
                    null//
            );
        }
        catch( MessagingException e )
        {
        	logger.error("ERROR :", e);
            if(logger.isDebugEnabled())logger.debug( "Error pada pengiriman email notofikasi E-Lions PAS Mallasurrance" );
            result = result + "\\nGagal kirim email";
        }
		return result;
	}
	
}
