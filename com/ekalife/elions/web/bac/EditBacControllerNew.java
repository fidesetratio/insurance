package com.ekalife.elions.web.bac;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Billing;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.CommandInputPayment;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.Employee;
import com.ekalife.elions.model.History;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.TopUp;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.Editbacvalidator;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentController;
import com.ekalife.utils.parent.ParentWizardController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Redesign menu Input SPAJ (Entry - UW - BAC)
 * 
 * @author Yusuf
 * @since Jul 22, 2011 (10:45:01 AM)
 *
 */
public class EditBacControllerNew extends ParentWizardController {
	protected final Log logger = LogFactory.getLog( getClass() );

	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
		logger.debug("EditBacController : initBinder");
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected void validatePage(Object cmd, Errors err, int page) {
		try {
			logger.debug("EditBacController : validate page " + page);
			//logger.info("VALIDATING PAGE: " + page);
			Editbacvalidator validator = (Editbacvalidator) this.getValidator();
			//logger.info(page);
			Cmdeditbac halaman= (Cmdeditbac)cmd;
			Integer idx =halaman.getPemegang().getIndeks_halaman();
			if (idx == null){
				idx = 1;
				halaman.getPemegang().setIndeks_halaman(idx);
			}
			int index = idx.intValue();
			String reg_spaj = halaman.getPemegang().getReg_spaj();
			if (reg_spaj ==null) {
				// buat kalau back tidak perlu validasi kalau next baru validasi
//				if(page==0) 
////					validator.validateddu(cmd, err);
//				else 
					if(page==1 && index > page) 
					validator.validateinvestasi(cmd, err);
				else if(page==2 && index > page) 
					validator.validatepp(cmd, err);
				else if(page==3 && index > page) 
					validator.validatettg(cmd, err);
				else if(page==4 && index > page){
					validator.validateagen(cmd, err);
					//validator.validateddu(cmd, err);
					validator.validateinvestasi(cmd, err);
				}else if(page==5 && index > page){
					Cmdeditbac detiledit = (Cmdeditbac)cmd;
					//APABILA INPUTAN BANK, MAKA ADA VALIDASI OTORISASI SUPERVISOR DAN KACAB
					if (!detiledit.getPemegang().getCab_bank().equalsIgnoreCase("")){
						validator.validateOtorisasi(detiledit, err, halaman.getCurrentUser().getJn_bank());
					}
					//APABILA POLIS POWERSAVE DAN DIATAS 69 TAHUN, HARUS ADA INFORMASI SPECIAL CASE
					//SK 079/AJS-SK/VII/2009 memang lebih baru dari IM-095, akan tetapi IM-095 masih berlaku.Usia sampai dengan 70 tahun
					if(products.powerSave(detiledit.getDatausulan().getLsbs_id().toString())){
						if(detiledit.getTertanggung().getMste_age().intValue() >= 71 && Common.isEmpty(detiledit.getPemegang().getInfo_special_case())) {
							ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.info_special_case", "", "Harap isi keterangan special case");
						}
					}
				}
			}
		} catch (ServletException e) {
			logger.error("ERROR :", e);	
		} catch (IOException e) {
			logger.error("ERROR :", e);
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
		
		logger.info("page = " + page);
		
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors errors, int page) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Cmdeditbac bac = (Cmdeditbac) cmd;
		Map<String, Object> refData = new HashMap<String, Object>();
		
		/** Halaman 1 : Data Usulan Asuransi */
		if (page ==0){
//			if(currentUser.getJn_bank().intValue() == 0 || currentUser.getJn_bank().intValue() == 1){ //BII
//				refData.put("listprodukutama", this.elionsManager.select_produkutama_platinumbii());
//				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_platinumbii());
//			}else if(currentUser.getJn_bank().intValue() == 2){ //BANK SINARMAS
//				refData.put("listprodukutama", this.elionsManager.select_produkutama_banksinarmas());
//				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_banksinarmas());
//			}else if(currentUser.getJn_bank().intValue() == 3){ //sinarmas sekuritas
//				refData.put("listprodukutama", this.elionsManager.select_produkutama_sekuritas());
//				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_sekuritas());
//			}else if(currentUser.getJn_bank().intValue() == 4){ //admin mall
//				refData.put("listprodukutama", this.uwManager.select_produkutama_admin_mall());
//				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_sekuritas());
//			}else {
				refData.put("listprodukutama", this.elionsManager.select_produkutama());
				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk());
//			}
		}
		
		return refData;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		logger.debug("EditBacController : formBackingObject");
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		Cmdeditbac bac = new Cmdeditbac();
		bac.setPemegang(new Pemegang());
		bac.setAddressbilling(new AddressBilling());
		bac.setDatausulan(new Datausulan());
		bac.setTertanggung(new Tertanggung());
		InvestasiUtama inv = new InvestasiUtama();
		inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
		inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
		
		bac.setInvestasiutama(inv);
		inv.setDaftartopup(new DetilTopUp());
		bac.setRekening_client(new Rekening_client());
		bac.setAccount_recur(new Account_recur());
		bac.setPowersave(new Powersave());
		bac.getPowersave().setFee_based_income(elionsManager.selectMst_default_numeric(31));
		bac.setAgen(new Agen());
		bac.setHistory(new History());
		bac.setEmployee(new Employee());
		
		return bac;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException errors, int page) throws Exception {
		Cmdeditbac bac = (Cmdeditbac) cmd;
		User currentUser 		= (User) request.getSession().getAttribute("currentUser");
		int halaman = ServletRequestUtils.getIntParameter(request, "hal", 1);
	}
	
	@Override
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Map<String, Comparable> m =new HashMap<String, Comparable>();
		
		
        return new ModelAndView("bac/bac_baru/input", m);
	}

}

