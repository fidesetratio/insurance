package com.ekalife.elions.web.cross_selling;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Agentrec;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.cross_selling.AgentCs;
import com.ekalife.elions.model.cross_selling.CrossSelling;
import com.ekalife.elions.model.cross_selling.PolicyCs;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Form Controller untuk input spaj baru / edit spaj / viewer
 * @author Yusuf
 * @since Jul 21, 2008 (8:43:03 AM)
 */
public class InputSpajFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, 	doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, 	integerEditor); 
		binder.registerCustomEditor(Date.class, null, 		dateEditor);
	}

	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map refData = new HashMap();
		refData.put("listPayMode", elionsManager.selectDropDown("eka.lst_pay_mode", "lscb_id", "lscb_pay_mode", "", "lscb_id", null));
		refData.put("listBisnis", elionsManager.selectDropDown(
				"eka.lst_bisnis a, eka.lst_det_bisnis b",
				"(lpad(a.lsbs_id, 3, 0) || lpad(b.lsdbs_number, 3, 0))",
				"(a.lsbs_name || ' - ' || b.lsdbs_name)",
				"", "a.lsbs_id, b.lsdbs_number", 
				"a.lsbs_id in (161) and a.lsbs_id = b.lsbs_id and a.lsbs_active = 1 and b.lsdbs_aktif = 1"
		));
		refData.put("listKurs", elionsManager.selectDropDown("eka.lst_kurs", "lku_id", "lku_symbol", "", "lku_id", null));
		refData.put("listPosisi", elionsManager.selectDropDown(
				"eka.lst_document_position", "lspd_id", "lspd_position", "", "decode(lspd_id, 8, 98, lspd_id)",
				"lspd_id in (79, 80, 81, 82, 8, 99)"
		));
		
		return refData;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String reg_spaj = ServletRequestUtils.getStringParameter(request, "reg_spaj", "");
		Date sysdate = elionsManager.selectSysdate();
		CrossSelling crossSelling = new CrossSelling();

		//A. NEW
		if(reg_spaj.trim().equals("")) {
			crossSelling.mode 						= "INSERT";
			crossSelling.policyCs 					= new PolicyCs();
			crossSelling.policyCs.auto				= 1; //auto calculate premium
			crossSelling.policyCs.mscs_beg_date 	= sysdate;
			crossSelling.policyCs.mscs_spaj_date 	= sysdate;
			crossSelling.policyCs.daftarAgent 		= new ArrayList<AgentCs>();
			crossSelling.policyCs.daftarAgent.add(new AgentCs());

		//B. EDIT
		}else {
			crossSelling.mode 						= "UPDATE";
			crossSelling.policyCs 					= elionsManager.selectMstPolicyCsBySpaj(reg_spaj);
			crossSelling.policyCs.auto				= 1; //auto calculate premium
			crossSelling.policyCs.daftarAgent = elionsManager.selectMstAgentCsBySpaj(reg_spaj);
			if(crossSelling.policyCs.daftarAgent.isEmpty()) {
				crossSelling.policyCs.daftarAgent = new ArrayList<AgentCs>();
				crossSelling.policyCs.daftarAgent.add(new AgentCs());				
			}
			crossSelling.policyCs.historiPosisi = elionsManager.selectMstPositionCsBySpaj(reg_spaj);

			List<DropDown> listPosisi = elionsManager.selectDropDown(
					"eka.lst_document_position", "lspd_id", "lspd_position", "", "decode(lspd_id, 8, 98, lspd_id)",
					"lspd_id in (79, 80, 81, 82, 8, 99)");
			boolean ucup = false;
			for(DropDown d : listPosisi) {
				if(ucup) {
					request.setAttribute("nextPosition", d.getValue());
					break;
				}
				if(Integer.parseInt(d.getKey()) == crossSelling.policyCs.lspd_id.intValue()) {
					ucup = true;
				}
			}
		}
		
		return crossSelling;
	}
	
	//validasi
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		CrossSelling crossSelling = (CrossSelling) command;
		boolean isError = false;
		String errorMessage = "";

		/** 1. VALIDASI UNTUK PROSES INSERT / UPDATE SPAJ */
		if(request.getParameter("save_spaj") != null) {
			//0. validasi posisi
			
			if(crossSelling.policyCs.lspd_id != null) {
				if(crossSelling.policyCs.lspd_id.intValue() != 79) {
					errors.reject("", "Polis ini sudah tidak bisa diedit. Terima kasih.");
				}
			}
			
			//1. validasi not empty
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.lsbs_lsdbs", "", 				"Produk");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_ins_period", "", 			"Lama Pertanggungan");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_pay_period", "", 			"Lama Pembayaran");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.lscb_id", "", 					"Cara Bayar");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.lku_id", "", 					"Kurs");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_holder", "", 				"Nama Pemegang Polis");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.daftarAgent[0].msag_id", "", 	"Kode Agen");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_beg_date", "", 			"Tanggal Mulai Pertanggungan");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_spaj_date", "", 			"Tanggal SPAJ");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_birth_date", "", 			"Tanggal Lahir");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_policy_type", "", 			"Status");
			
			if(errors.hasErrors()) return;
			
			//n_prod
			crossSelling.policyCs.lsbs_id = Integer.valueOf(crossSelling.policyCs.lsbs_lsdbs.substring(0,3));
			crossSelling.policyCs.lsdbs_number = Integer.valueOf(crossSelling.policyCs.lsbs_lsdbs.substring(3));
			Class aClass = Class.forName("produk_asuransi.n_prod_"+FormatString.rpad("0", crossSelling.policyCs.lsbs_id.toString(), 2));
			n_prod produk = (n_prod) aClass.newInstance();
			produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
			produk.of_set_bisnis_no(crossSelling.policyCs.lsdbs_number);

			//2. validasi n_prod

			//flag_uppremi -> 0 up, 1 premi, 2 up dan premi
			if(produk.flag_uppremi == 0) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_tsi", "", 		"Uang Pertanggungan");
			}else if(produk.flag_uppremi == 1) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_premium", "", 	"Jumlah Premi");
			}else if(produk.flag_uppremi == 2) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_tsi", "", 		"Uang Pertanggungan");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_premium", "", 	"Jumlah Premi");
			}
			
			if(errors.hasErrors()) return;

			if(crossSelling.policyCs.auto == null) crossSelling.policyCs.auto = 0; 
			if(crossSelling.policyCs.auto.intValue() == 0) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_premium", "", 	"Jumlah Premi");
			}
			
			//3. validasi business process
			
			//lspd_id -> yang bisa diupdate hanya posisi 79 (input) dan 80 (input nopol asm)
			if(crossSelling.mode.equals("UPDATE") && crossSelling.policyCs.lspd_id.intValue() != 79 && crossSelling.policyCs.lspd_id.intValue() != 80) {
				errors.rejectValue("policyCs.lspd_id", "", "SPAJ ini sudah tidak bisa diedit. Terima kasih.");
			}
			
			if(errors.hasErrors()) return;
			
			//4. perhitungan-perhitungan
			//tidak dihitung lagi : ins_period, pay_period, lscb_id, lku_id 
			
			//hitung umur
			Map hasil = ajaxManager.hitung_umur(defaultDateFormat.format(crossSelling.policyCs.mscs_birth_date));
			crossSelling.policyCs.mscs_age = (Integer) hasil.get("umur");
			
			//hitung end date
			crossSelling.policyCs.mscs_end_date = defaultDateFormat.parse(
					ajaxManager.populateEndDateCrossSelling(
							defaultDateFormat.format(crossSelling.policyCs.mscs_beg_date), crossSelling.policyCs.mscs_ins_period));
			
			//hitung UP dan PREMI, bila check hitung otomatis dicentang saja tapinya
			if(produk.flag_uppremi == 0 && crossSelling.policyCs.auto.intValue() == 1) {
				//hitung PREMI
				produk.of_set_kurs(crossSelling.policyCs.lku_id);
				produk.of_set_usia_pp(crossSelling.policyCs.mscs_age);
				produk.of_set_usia_tt(crossSelling.policyCs.mscs_age);
				produk.of_set_age();
				produk.of_hit_premi();
				crossSelling.policyCs.mscs_premium = produk.idec_premi;
			}else if(produk.flag_uppremi == 1) {
				//hitung UP
			}else if(produk.flag_uppremi == 2) {
				//hitung UP dan PREMI
			}
			
			//hitung struktur agent
			Agentrec[] daftarAgen = elionsManager.proc_process_agent_2007(crossSelling.policyCs.daftarAgent.get(0).msag_id);
			if(daftarAgen == null) {
				errors.reject("", "Struktur agen bermasalah. harap hubungi Agency Support.");
			}else {
				crossSelling.policyCs.daftarAgent = new ArrayList<AgentCs>();
				for(int i=1; i<=4; i++) {
					Agentrec a = daftarAgen[i];
					if(a != null) {
						AgentCs agentCs = new AgentCs();
						agentCs.msag_id = a.getAgent_id();
						agentCs.lsle_id = a.getLevel_id();
						agentCs.lev_comm = a.getComm_id();
						agentCs.flag_sbm = a.getSbm();
						//Yusuf (8 Sep 09) Req Yosep : Cup buat insert-an ke MST_AGENT_CS (pohon produksi agent Cross seling) tolong ditambahin apabila dia BM (MSAG_FLAG_BM = 1) di kolom FLAG_SBM dijadiin 1 yah
						if(agentCs.lsle_id.intValue() == 2 && a.getBm().intValue() == 1){
							agentCs.flag_sbm = 1;
						}
						crossSelling.policyCs.daftarAgent.add(agentCs);
					}
				}
			}
			
			//5. validasi business process lanjutan (setelah dilakukan perhitungan2)
			
			//umur
			//if(!(produk.ii_age_from <= crossSelling.policyCs.mscs_age && crossSelling.policyCs.mscs_age <= produk.ii_age_to)) {
			if(!(17 <= crossSelling.policyCs.mscs_age && crossSelling.policyCs.mscs_age <= produk.ii_age_to)) {
				errors.rejectValue("policyCs.mscs_age", "", "Usia harus diantara " + 17 + " dan " + produk.ii_age_to);
			}
			
			errorMessage = produk.of_alert_min_up(crossSelling.policyCs.mscs_tsi);
			if(!errorMessage.equals("")) {
				errors.rejectValue("policyCs.mscs_tsi", "", errorMessage);
			}

		/** 2. VALIDASI UNTUK PROSES TRANSFER SPAJ */			
		}else if(request.getParameter("transfer") != null) {
			if(crossSelling.policyCs.lspd_id != null) {
				//validasi posisi
				if(crossSelling.policyCs.lspd_id.intValue() < 79 || crossSelling.policyCs.lspd_id.intValue() > 82) {
					errors.reject("", "Polis ini tidak bisa ditransfer melalui program ini. Terima kasih.");
				//validasi transfer dari input spaj ke input nopol & terima ttp
				}else if(crossSelling.policyCs.lspd_id.intValue() == 79) {
					if(crossSelling.policyCs.daftarAgent.get(0).getMsag_id().equals("000000")) { //apabila agen baru, tidak bisa ditransfer
						errors.reject("", "Polis tidak bisa ditransfer sampai kode Agen yang aktif dimasukkan. Terima kasih.");
					}
				//validasi transfer dari input nopol & terima ttp ke input tgl payment/bsb
				}else if(crossSelling.policyCs.lspd_id.intValue() == 80) {
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_policy_no", "", "Nomor Polis");
					
					if(crossSelling.policyCs.mscs_policy_no != null){
						int validasiDouble = uwManager.selectValidasiDoubleNoPolASM(crossSelling.policyCs.reg_spaj, crossSelling.policyCs.mscs_policy_no);
						if(validasiDouble > 0) errors.rejectValue("policyCs.mscs_policy_no", "", "Nomor Polis ASM Sudah pernah digunakan! Harap dicek ulang!");
					}
					
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_ttp_date", "", "Tanggal TTP");
				//validasi transfer dari input tgl payment/bsb ke konfirmasi asm
				}else if(crossSelling.policyCs.lspd_id.intValue() == 81) {
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_pay_date", "", "Tanggal Bayar/BSB");
				}
			}
			
		/** 3. VALIDASI UNTUK PROSES SAVE NOMOR POLIS */			
		}else if(request.getParameter("save_nopol") != null) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_policy_no", "", "Nomor Polis");
			
		/** 2. VALIDASI UNTUK PROSES SAVE TTP */			
		}else if(request.getParameter("save_ttp") != null) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "policyCs.mscs_ttp_date", "", "Tanggal TTP");
		}
				
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CrossSelling crossSelling = (CrossSelling) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map m = new HashMap();

		//1. INSERT / UPDATE SPAJ
		if(request.getParameter("save_spaj") != null) {
			String lanjutanOrNot = request.getParameter("tamp_lanjutan_or_not");
			Integer flag_lanjutan = 0;
			if(lanjutanOrNot != null && "yes".equals(lanjutanOrNot)){
				flag_lanjutan = 1;
			}
			crossSelling.getPolicyCs().setMscs_policy_type(flag_lanjutan);
			String reg_spaj = elionsManager.saveCrossSelling(crossSelling, Integer.valueOf(currentUser.getLus_id()), flag_lanjutan);
			if(reg_spaj==null) {
				m.put("pesan", "Data tidak berhasil disimpan. Harap Hubungi IT.");
			}else {
				m.put("pesan", "Data berhasil disimpan. Nomor SPAJ adalah " + FormatString.nomorSPAJ(reg_spaj));
				m.put("reg_spaj", reg_spaj);			
			}
		
		//2. TRANSFER SPAJ KE POSISI BERIKUTNYA
		}else if(request.getParameter("transfer") != null) {
			m.put("pesan", elionsManager.transferCrossSelling(crossSelling, Integer.valueOf(currentUser.getLus_id())));
			m.put("reg_spaj", crossSelling.policyCs.reg_spaj);

		//3. SAVE NOMOR POLIS & TGL TTP
		}else if(request.getParameter("save_nopol") != null) {
			m.put("pesan", elionsManager.saveNomorPolisCrossSelling(crossSelling, Integer.valueOf(currentUser.getLus_id())));
			m.put("reg_spaj", crossSelling.policyCs.reg_spaj);
			
		//4. SAVE TGL BAYAR
		}else if(request.getParameter("save_ttp") != null) {
			m.put("pesan", elionsManager.saveTtpCrossSelling(crossSelling, Integer.valueOf(currentUser.getLus_id())));
			m.put("reg_spaj", crossSelling.policyCs.reg_spaj);
		}
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/cross_selling/input.htm")).addAllObjects(m);
	}
	
}