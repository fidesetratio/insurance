package com.ekalife.elions.web.bac;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.Editbacvalidator;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.parent.ParentFormController;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;

/**
 * Controller untuk input spaj
 * digunakan di modul input bac
 * 
 * @author Canpri
 * @since Aug 13, 2014 (9:23:47 AM)
 * @
 */
public class EditBacControllerBaru extends ParentFormController {
	
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	BindingResult errors;

    public BindingResult getErrors() {
         return errors;
    }

    public void setErrors(BindingResult errors) {
         this.errors = errors;
    }

	@Override
	protected HashMap<String, Object> referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		//logger.debug("EditBacController : referenceData page " + page);
		logger.info("EditBacController : referenceData");
		
		HashMap<String, Object> refData = new HashMap<String, Object>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		
		//reference di buat perpage
			//Page pemegang
			refData.put("select_medis",DroplistManager.getInstance().get("medis.xml","ID",request));
			refData.put("select_hasil",DroplistManager.getInstance().get("SUMBER_PENGHASILAN.xml","",request));
			
//			refData.put("select_aset",DroplistManager.getInstance().get("ASET.xml","",request));
//			refData.put("select_relasi_badan_usaha",DroplistManager.getInstance().get("RELATION_BADAN_USAHA.xml","",request));
//			refData.put("select_sumberkyc",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
			
			refData.put("select_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "", "lsag_id desc", null));
			refData.put("select_pendidikan", elionsManager.selectDropDown("eka.lst_education", "lsed_id", "lsed_name", "", "lsed_name", null));
			refData.put("select_pekerjaan", elionsManager.selectDropDown("eka.lst_pekerjaan","lsp_id","lsp_name","","lsp_name",null));
			refData.put("select_identitas", elionsManager.selectDropDown("eka.lst_identity", "lside_id", "lside_name", "", "lside_id", null));
			refData.put("select_negara", elionsManager.selectDropDown("eka.lst_negara", "lsne_id", "lsne_note", "", "lsne_urut", null));
			refData.put("select_penghasilan", elionsManager.selectDropDown("eka.lst_klasifikasi_new", "mkl_seq", "mkl_desc", "", "mkl_seq", "mkl_id=2"));
			refData.put("select_tujuan", elionsManager.selectDropDown("eka.lst_klasifikasi_new", "mkl_seq", "mkl_desc", "", "mkl_seq desc", "mkl_id=1"));
			refData.put("select_marital", elionsManager.selectDropDown("eka.lst_status", "lsst_id", "lsst_name", "", "lsst_id", null));
			refData.put("select_gelar", elionsManager.selectDropDown("eka.lst_title", "lti_id", "lti_note", "", "lti_id", "lti_jn_title=0"));
			refData.put("select_relasi", elionsManager.selectDropDown("eka.lst_relation", "lsre_id", "lsre_relation", "", "lsre_id", "lsre_id in (5,4,2,7)"));
			refData.put("select_relasi_premi", elionsManager.selectDropDown("eka.lst_relation", "lsre_id", "lsre_relation", "", "lsre_id", null));
			refData.put("select_gelar_bu", elionsManager.selectDropDown("eka.lst_title", "lti_id", "lti_note", "", "lti_note asc", "lti_jn_title=1"));
			refData.put("select_industri", elionsManager.selectDropDown("eka.lst_klasifikasi_new", "mkl_seq", "mkl_desc", "", "mkl_seq desc", "mkl_id=5"));
			refData.put("select_dana_badan_usaha", elionsManager.selectDropDown("eka.lst_klasifikasi_new", "mkl_seq", "mkl_desc", "", "mkl_seq desc", "mkl_id=3"));

			refData.put("select_grp_job", elionsManager.selectDropDown("eka.lst_grp_job", "lgj_id", "lgj_note", "", "lgj_note", null));
			refData.put("select_relation", elionsManager.selectDropDown("eka.lst_relation", "lsre_id", "lsre_relation", "", "lsre_relation", null));
			refData.put("select_jabatan", elionsManager.selectDropDown("eka.lst_jabatan", "ljb_id", "ljb_note", "", "ljb_id", null));
//			refData.put("select_calon_pembayar_premi", elionsManager.selectDropDown("eka.lst_calon_pembayar_premi", "lcpp_id", "lcpp_name", "", "lcpp_name", "lcpp_flag=1"));
//			refData.put("select_pendapatan_rutin", elionsManager.selectDropDown("eka.lst_pendapatan_rutin", "lpr_id", "lpr_name", "", "lpr_name", "lpr_flag=1"));
//			refData.put("select_pendapatan_non_rutin", elionsManager.selectDropDown("eka.lst_pendapatan_non_rutin", "lpnr_id", "lpnr_name", "", "lpnr_name", "lpnr_flag=1"));
//			refData.put("select_jumlah_pendapatan", elionsManager.selectDropDown("eka.lst_jumlah_pendapatan", "ljmp_id", "ljmp_name", "", "ljmp_name", "ljmp_flag=1"));
//			refData.put("select_tujuan_asuransi", elionsManager.selectDropDown("eka.lst_tujuan_asuransi", "lta_id", "lta_name", "", "lta_name", "lta_flag=1"));
		return refData;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		//logger.debug("EditBacController : formBackingObject");
		logger.info("EditBacController : formBackingObject");
		
		this.accessTime = System.currentTimeMillis();
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String lde_id = currentUser.getLde_id();
		String spv=request.getParameter("spv");
		String spaj = request.getParameter("showSPAJ");

		Cmdeditbac detiledit = (Cmdeditbac) session.getAttribute("dataInputSpaj");
		
		//if(detiledit != null){
			//if(detiledit.getDatausulan().getJenis_pemegang_polis() == null){
				//jenis_pemegang_polis = request.getParameter("jenis_pemegang_polis");
			//}else{
				//jenis_pemegang_polis = detiledit.getDatausulan().getJenis_pemegang_polis().toString();
			//}
		//}
		String jenis_pemegang_polis = null;
		if(request.getParameter("jenis_pemegang_polis") == null){
			jenis_pemegang_polis = "0";
		}else{
			jenis_pemegang_polis = request.getParameter("jenis_pemegang_polis");
		}
		if(request.getParameter("data_baru") != null) {
			session.removeAttribute("dataInputSpaj");
			detiledit = null;
		}
		
		if(detiledit != null) { // SUMBER DATA : DARI GAGAL SUBMIT SEBELUMNYA
			request.setAttribute("adaData", "adaData");
			
//			TAMBAHAN UNTUK PERUSAHAAN
			// set isi jenis pemegang polis (INDIVIDU/PERUSAHAAN)
			// TODO:ada tambahan get jenis_pemegang_polis yang harus dipasang
			jenis_pemegang_polis = detiledit.getDatausulan().getJenis_pemegang_polis().toString();
//			if(detiledit.getPemegang().getMcl_jenis() != null){
//				jenis_pemegang_polis = detiledit.getPemegang().getMcl_jenis().toString();
//				detiledit.setPersonal((Personal)this.elionsManager.selectProfilePerusahaan(detiledit.getPemegang().getMcl_id()));
//				detiledit.getPersonal().setAddress(new AddressNew());
//				detiledit.setContactPerson((ContactPerson)this.elionsManager.selectpic(detiledit.getPemegang().getMcl_id()));
//				if(detiledit.getContactPerson() == null)detiledit.setContactPerson(new ContactPerson());
//			}
			if(jenis_pemegang_polis != null){
				try{
					//Map<String, Collection> map = new HashMap<String, Collection>();
					//map.put("gelar", elionsManager.selectGelar(1));
					//map.put("jenisUsaha", elionsManager.selectJenisUsaha());
					request.setAttribute("gelar", new ArrayList(elionsManager.selectGelar(1)));
					ArrayList<DropDown> bidangUsahaList = new ArrayList(((List<DropDown>)elionsManager.selectBidangUsaha(2)));
					bidangUsahaList.addAll(new ArrayList((List<DropDown>)elionsManager.selectBidangUsaha(1)));
					request.setAttribute("bidangUsaha", bidangUsahaList);
					detiledit.getDatausulan().setJenis_pemegang_polis(Integer.parseInt(jenis_pemegang_polis));
				}catch(Exception e){
					detiledit.getDatausulan().setJenis_pemegang_polis(0);
				}
			}else{
				detiledit.getDatausulan().setJenis_pemegang_polis(0);
			}
		}else {
			detiledit = new Cmdeditbac();
			
			String kopiSPAJ = ServletRequestUtils.getStringParameter(request, "kopiSPAJ", "");
			String topupslinkke = ServletRequestUtils.getStringParameter(request, "topupslinkke", "");
			String flag_upload = ServletRequestUtils.getStringParameter(request, "flag_upload", "");
			
			/** EDIT SPAJ */
			if(spaj!=null) {
				
			/** INSERT SPAJ BARU */
			}else{
				
				detiledit.setPemegang(new Pemegang());
				detiledit.setAddressbilling(new AddressBilling());
				detiledit.setDatausulan(new Datausulan());
				detiledit.setTertanggung(new Tertanggung());
				detiledit.setPembayarPremi(new PembayarPremi());
				InvestasiUtama inv = new InvestasiUtama();
				
				// TAMBAHAN UNTUK PERUSAHAAN
				// set isi jenis pemegang polis (INDIVIDU/PERUSAHAAN)
				if(jenis_pemegang_polis != null){
					try{
						request.setAttribute("gelar", new ArrayList(elionsManager.selectGelar(3)));
						ArrayList<DropDown> bidangUsahaList = new ArrayList(((List<DropDown>)elionsManager.selectBidangUsaha(2)));
						bidangUsahaList.addAll(new ArrayList((List<DropDown>)elionsManager.selectBidangUsaha(1)));
						request.setAttribute("bidangUsaha", bidangUsahaList);
						detiledit.getDatausulan().setJenis_pemegang_polis(Integer.parseInt(jenis_pemegang_polis));
					}catch(Exception e){
						detiledit.getDatausulan().setJenis_pemegang_polis(0);
					}
				}else{
					detiledit.getDatausulan().setJenis_pemegang_polis(0);
				}
				detiledit.setPersonal(new Personal());
				detiledit.setContactPerson(new ContactPerson());
//				detiledit.getPersonal().setAddress(new AddressNew());
				
			}
		}
		return detiledit;
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		//logger.debug("EditBacController : isFormChangeRequest");
		logger.info("EditBacController : isFormChangeRequest");
		
		String submitType = ServletRequestUtils.getStringParameter(request, "submitType", "");
		if("YA".equals(submitType) || "TIDAK".equals(submitType)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object cmd) throws Exception {
		//logger.debug("EditBacController : onFormChange");
		logger.info("EditBacController : onFormChange");
		Cmdeditbac detiledit = (Cmdeditbac) cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		String submitType = ServletRequestUtils.getStringParameter(request, "submitType", "");
		if("YA".equals(submitType)){
			detiledit.getDatausulan().setJenis_pemegang_polis(1);
		} else {
			detiledit.getDatausulan().setJenis_pemegang_polis(0);
		}
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}

	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		//logger.debug("EditBacController : onBindAndValidate");
		logger.info("EditBacController : onBindAndValidate");
		String test = ServletRequestUtils.getStringParameter(request, "test");
		Editbacvalidator validator = new Editbacvalidator();
		validator.validate(cmd, err);
		
		/*ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.alamat_rumah", "", "ALamat harus diisi");*/
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		//logger.debug("EditBacController : onSubmit");
		logger.info("EditBacController : onSubmit");
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		HashMap<String, Comparable> m =new HashMap<String, Comparable>();
		
		return new ModelAndView("bac/editsubmit",m);
	}
	
}