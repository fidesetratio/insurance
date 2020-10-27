package com.ekalife.elions.web.bac;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Broker;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.Employee;
import com.ekalife.elions.model.Hadiah;
import com.ekalife.elions.model.History;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.PesertaPlus_mix;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.RencanaPenarikan;
import com.ekalife.elions.model.Simas;
import com.ekalife.elions.model.SumberKyc;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.Editbacvalidatorspajnew;
import com.ekalife.elions.web.bac.support.Editduvalidatorspajnew;
import com.ekalife.elions.web.bac.support.Editduvalidatorspajnew2;
import com.ekalife.elions.web.bac.support.form_data_usulan;
import com.ekalife.elions.web.bac.support.hit_biaya_powersave;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.F_CopyObjectToFile;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_replace;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentWizardController;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;
import produk_asuransi.n_prod;

/**
 * @author HEMILDA
 * Controller untuk input bac
 */
public class EditBacControllerSpajNew extends ParentWizardController {
	
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) throws Exception {
		Cmdeditbac cmd = (Cmdeditbac) command;
	}
	
	BindingResult errors;

    public BindingResult getErrors() {
         return errors;
    }

    public void setErrors(BindingResult errors) {
         this.errors = errors;
    }
	
//	public void validate(Object command) {
//        Validator[] validators = getValidators();
//        if (validators != null) {
//             for (int index = 0; index < validators.length; index++) {
//                  Validator validator = validators[index];
//                  if (validator instanceof Editbacvalidator) {
//                       if (((Editbacvalidator) validator).supports(command
//                                 .getClass())) {
//                            ValidationUtils.invokeValidator(validators[index],
//                                      command, errors);
//                       }
//                  } else if (validator.supports(command.getClass())) {
//                       ValidationUtils.invokeValidator(validators[index], command,
//                                 errors);
//                  }
//             }
//        }
//   }
	
	protected void validatePage(Object cmd, Errors err, int page) {
		try {
			logger.debug("EditBacController : validate page " + page);
			//logger.info("VALIDATING PAGE: " + page);
//			Editbacvalidator validator = (Editbacvalidator) this.getValidator();
//			Editduvalidator validatorDU = (Editduvalidator) this.getValidator();
			Validator[] validators = getValidators();
			//logger.info(page);
			Cmdeditbac halaman= (Cmdeditbac)cmd;
			Integer idx = halaman.getPemegang().getIndeks_halaman();
//			Integer sp = bacManager.selectLusSpecial(halaman.getCurrentUser().getLus_id());
			if (idx == null){
				idx = 1;
				halaman.getPemegang().setIndeks_halaman(idx);
			}
			Integer index = idx;
			String reg_spaj = halaman.getPemegang().getReg_spaj();
			if (reg_spaj ==null) {
				// buat kalau back tidak perlu validasi kalau next baru validasi
				F_CopyObjectToFile.serializable(cmd,halaman.getPathFileTemp()+"\\bac.txt");
					Validator validator = validators[0];
					Validator validatordu = validators[1];
					Validator validatorNew = validators[2];
					if(page==0) {
		                  ((Editbacvalidatorspajnew) validator).validatepp(cmd, err);
						//validator.validatepp(cmd, err);
					}else if(page==1 && index > page){ 
						((Editbacvalidatorspajnew) validator).validatettg(cmd, err);
	//					validator.validatettg(cmd, err);
					}else if(page==2 && index > page){
						((Editduvalidatorspajnew2) validatorNew).validatepPremi(cmd, err);
					}else if(page==3 && index > page){ 
///////////////////////////////////////// Tambahan karena pengecekan Validasi diakibatkan ERBE
						Cmdeditbac detiledit = (Cmdeditbac)cmd;
						//plan
						String bisnis=detiledit.getDatausulan().getPlan();
						if (bisnis==null)
						{
							bisnis="0~X0";
						}
						form_data_usulan a =new form_data_usulan();
						String hasil_bisnis = a.bisnis(bisnis);
						if(hasil_bisnis.trim().length()!=0)
						{
							err.rejectValue("datausulan.plan","","HALAMAN DATA USULAN :" +hasil_bisnis);
						}
						int letak=0;
						letak=bisnis.indexOf("X");
						Integer kode_produk=null;
						Integer number_produk=null;
						kode_produk=new Integer(Integer.parseInt(bisnis.substring(0,letak-1)));
						number_produk=new Integer(Integer.parseInt(bisnis.substring(letak+1,bisnis.length())));
						detiledit.getDatausulan().setLsbs_id(kode_produk);
						detiledit.getDatausulan().setLsdbs_number(number_produk);
						//////////////////////////////////////////////////////////////////
						
						//untuk produk ERBE di bikin validasi berbeda karena code yang sudah  terlalu panjang sehingga error pada saat build k sistem
						if((detiledit.getDatausulan().getLsbs_id()==217 && detiledit.getDatausulan().getLsdbs_number()==2) ||
							(detiledit.getDatausulan().getLsbs_id()==212 && (detiledit.getDatausulan().getLsdbs_number()==9 || detiledit.getDatausulan().getLsdbs_number()==10 || detiledit.getDatausulan().getLsdbs_number()==14))){// add 212 - 14 nana
							((Editduvalidatorspajnew2) validatorNew).validateDuNew(cmd, err);
						}else{
							((Editduvalidatorspajnew) validatordu).validatedChild(cmd, err);
						}
//						((Editduvalidator) validatordu).validateddu2(cmd, err);
//						validator.validateddu(cmd, err);
					}else if(page==4 && index > page){ 
						((Editbacvalidatorspajnew) validator).validateinvestasi(cmd, err);
//						validator.validateinvestasi(cmd, err);
					}else if(page==5 && index > page){
						((Editbacvalidatorspajnew) validator).validateagen(cmd, err);
						((Editbacvalidatorspajnew) validator).validateinvestasi(cmd, err);
//						validator.validateagen(cmd, err);
//						validator.validateinvestasi(cmd, err);
					}else if(page==6 && index > page){
						Cmdeditbac detiledit = (Cmdeditbac)cmd;
						//APABILA INPUTAN BANK, MAKA ADA VALIDASI OTORISASI SUPERVISOR DAN KACAB
						if (!detiledit.getPemegang().getCab_bank().equalsIgnoreCase("")){
							((Editbacvalidatorspajnew) validator).validateOtorisasi(detiledit, err, halaman.getCurrentUser().getJn_bank());
//							validator.validateOtorisasi(detiledit, err, halaman.getCurrentUser().getJn_bank());
						}
						//APABILA POLIS POWERSAVE DAN DIATAS 69 TAHUN, HARUS ADA INFORMASI SPECIAL CASE
						//SK 079/AJS-SK/VII/2009 memang lebih baru dari IM-095, akan tetapi IM-095 masih berlaku.Usia sampai dengan 70 tahun
						if(products.powerSave(detiledit.getDatausulan().getLsbs_id().toString())){
							if(detiledit.getTertanggung().getMste_age().intValue() >= 71 && Common.isEmpty(detiledit.getPemegang().getInfo_special_case())) {
								ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.info_special_case", "", "Harap isi keterangan special case");
							}
						}
					}
			}else{
					Validator validator = validators[0];
					Validator validatordu = validators[1];
					Validator validatorNew = validators[2];
					if(page==0){ 
						 ((Editbacvalidatorspajnew) validator).validatepp(cmd, err);
//						validator.validatepp(cmd, err);
					}else if(page==1){ 
						((Editbacvalidatorspajnew) validator).validatettg(cmd, err);
//						validator.validatettg(cmd, err);
					}else if(page==2){
						((Editduvalidatorspajnew2) validatorNew).validatepPremi(cmd, err);
					}else if(page==3){ 						
						Cmdeditbac detiledit = (Cmdeditbac)cmd;
						//untuk produk ERBE di bikin validasi berbeda karena code yang sudah  terlalu panjang sehingga error pada saat build k sistem
						if(detiledit.getDatausulan().getLsbs_id()==217 && detiledit.getDatausulan().getLsdbs_number()==2){
							((Editduvalidatorspajnew2) validatorNew).validateDuNew(cmd, err);
						}else{
							((Editduvalidatorspajnew) validatordu).validatedChild(cmd, err);
						}
//						((Editduvalidator) validatordu).validateddu2(cmd, err);
//						validatorDU.validateddu(cmd, err);
					}else if(page==4){ 
						((Editbacvalidatorspajnew) validator).validateinvestasi(cmd, err);
//						validator.validateinvestasi(cmd, err);
					}else if(page==5){
						Cmdeditbac detiledit = (Cmdeditbac)cmd;
						((Editbacvalidatorspajnew) validator).validateagen(cmd, err);
						((Editbacvalidatorspajnew) validator).validateinvestasi(cmd, err);
//						validator.validateagen(cmd, err);	
//						validator.validateinvestasi(cmd, err);
						
						Boolean agent_ssl = detiledit.getAgen().getMsag_id().equals("021052") ;
						Boolean agent_simas_prima = detiledit.getAgen().getMsag_id().equals("016409") ;
						Boolean kode_produk_simas_prima = detiledit.getDatausulan().getKodeproduk().equals("142");
						Boolean no_produk = detiledit.getDatausulan().getLsdbs_number().equals(2) ||
											detiledit.getDatausulan().getLsdbs_number().equals(1) ||
											detiledit.getDatausulan().getLsdbs_number().equals(11);
	//					*175-2 POWERSAVE  SYARIAH BSIM
						Boolean agent_simas_prima_syariah = detiledit.getAgen().getMsag_id().equals("901388");
						Boolean kode_produk_simas_prima_syariah = detiledit.getDatausulan().getKodeproduk().equals("175");
						Boolean no_produk_simas_prima_syariah = detiledit.getDatausulan().getLsdbs_number().equals(2);
	// 					*SIMAS POWER LINK
						Boolean agent_power_link = detiledit.getAgen().getMsag_id().equals("024369") ;
						Boolean kode_produk_power_link = detiledit.getDatausulan().getKodeproduk().equals("120");
						Boolean no_produk_power_link = detiledit.getDatausulan().getLsdbs_number().equals(10) ||
												       detiledit.getDatausulan().getLsdbs_number().equals(11) ||
												       detiledit.getDatausulan().getLsdbs_number().equals(12) ||
												       detiledit.getDatausulan().getLsdbs_number().equals(22) ||
												       detiledit.getDatausulan().getLsdbs_number().equals(23) ||
													   detiledit.getDatausulan().getLsdbs_number().equals(24);
	//					*MULTI INVEST SYARIAH
						Boolean agent_multi_invest_syariah = detiledit.getAgen().getMsag_id().equals("901389") ;
						Boolean kode_produk_multi_invest_syariah = detiledit.getDatausulan().getKodeproduk().equals("182");
						Boolean no_produk_multi_invest_syariah = detiledit.getDatausulan().getLsdbs_number().equals(7) ||
												       detiledit.getDatausulan().getLsdbs_number().equals(8) ||
													   detiledit.getDatausulan().getLsdbs_number().equals(9);
						
						
	//					*SIMAS MAXISAVE
						Boolean agent_simas_maxi = detiledit.getAgen().getMsag_id().equals("901328") ;
						Boolean kode_produk_simas_maxi = detiledit.getDatausulan().getKodeproduk().equals("111");
						Boolean no_produk_simas_maxi = detiledit.getDatausulan().getLsdbs_number().equals(2);
	
						/* SMile Proteksi 212 - 9 kode agent (agency) yang boleh input hanya dari cabang lca_id = 52 dan 09  */
						if (detiledit.getDatausulan().getLsbs_id().equals(212) && detiledit.getDatausulan().getLsdbs_number().equals(9)) {
							if (detiledit.getAgen().getLca_id().equals("52") || detiledit.getAgen().getLca_id().equals("09")) {
							} else {
								err.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN CABANG 52 atau 09");
							}
						} 
						
						if(!detiledit.getCurrentUser().getCab_bank().equals("") && detiledit.getCurrentUser().getJn_bank().intValue() == 2 ||(detiledit.getCurrentUser().getJn_bank().intValue() == 16) ) {
							if (detiledit.getDatausulan().getKodeproduk().equals("073")||detiledit.getDatausulan().getKodeproduk().equals("164")||detiledit.getDatausulan().getKodeproduk().equals("142")||detiledit.getDatausulan().getKodeproduk().equals("186")||detiledit.getDatausulan().getKodeproduk().equals("120") || detiledit.getDatausulan().getKodeproduk().equals("175") || detiledit.getDatausulan().getKodeproduk().equals("182")|| detiledit.getDatausulan().getKodeproduk().equals("111")){
								if(((detiledit.getDatausulan().getKodeproduk().equals("164")
										&& no_produk)
										|| (detiledit.getDatausulan().getKodeproduk().equals("186")
												&& detiledit.getDatausulan().getLsdbs_number().equals(3))) 
										 ){
									if(!agent_ssl){
										err.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 021052");
									}
								}
								
								if(kode_produk_simas_prima && no_produk){
									if(!agent_simas_prima){
										err.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 016409");
									}
								
								}
	//							*175-2 SIMAS PRIMA SYARIAH
								if(kode_produk_simas_prima_syariah && no_produk_simas_prima_syariah){
									if(!agent_simas_prima_syariah){
										err.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 901388");
									}
								}
	//							*MULTI INVEST SYARIAH
								if(kode_produk_multi_invest_syariah && no_produk_multi_invest_syariah){
									if(!agent_multi_invest_syariah){
										err.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 901389");
									}
								}
	// 							*SIMAS POWER LINK
								if(kode_produk_power_link && no_produk_power_link){
									if(!agent_power_link){
										err.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 024369");
									}
									
								}
								
	//							*SIMAS MAXI SAVE
								if( kode_produk_simas_maxi && no_produk_simas_maxi){
									if(!agent_simas_maxi){
										err.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 901328");
										detiledit.getAgen().setMsag_id("901328");
									}
									
								}
							}
						}
					}else if (page == 6){
						Cmdeditbac detiledit = (Cmdeditbac)cmd;
						//APABILA INPUTAN BANK, MAKA ADA VALIDASI OTORISASI SUPERVISOR DAN KACAB
						if (!detiledit.getPemegang().getCab_bank().equalsIgnoreCase("")){
							((Editbacvalidatorspajnew) validator).validateOtorisasi(detiledit, err, halaman.getCurrentUser().getJn_bank());
//							validator.validateOtorisasi(detiledit, err, halaman.getCurrentUser().getJn_bank());
						}
	
						//APABILA POLIS POWERSAVE DAN DIATAS 69 TAHUN, HARUS ADA INFORMASI SPECIAL CASE
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

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err, int page)
			throws Exception {
		
		logger.debug("EditBacController : referenceData page " + page);
		HashMap<String, Object> refData = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date tanggal = elionsManager.selectSysdateSimple();
		
		String sysdate = defaultDateFormat.format(new Date());
		
		DateFormat dfh = new SimpleDateFormat("HH");
		DateFormat dfm = new SimpleDateFormat("mm");
		
		String hh = ServletRequestUtils.getStringParameter(request, "hh", dfh.format(tanggal));
		String mm = ServletRequestUtils.getStringParameter(request, "mm", dfm.format(tanggal));
		String tanggalTerimaAdmin;
		
		Cmdeditbac a = (Cmdeditbac) cmd;
//		*Sumber Pendanaan pembelian asuransi tabahan
		if (a.getPemegang().getJmlkyc()==null){
			a.getPemegang().setJmlkyc(0);
		}
//		*sumber penghasilan tambahan
		if (a.getPemegang().getJmlkyc2()==null){
			a.getPemegang().setJmlkyc2(0);
		}
		
		/** Halaman 1 : Pemegang Polis */
		if (page == 0){
			refData = new HashMap<String, Object>();
			Cmdeditbac editBac = (Cmdeditbac) cmd;				
			refData.put("tanggalTerimaAdmin",  sysdate);
			refData.put("listHH", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23});
			refData.put("listMM", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59});
			refData.put("hh", hh);
			refData.put("mm", mm);
			refData.put("select_medis_desc", new ArrayList(bacManager.selectParamMedisDesc()) ); // Tambah nana medis value mst_config 148420
			
//			elionsManager.prosesEditTanggalSpajAdmin(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
//			refData.put("success", "Berhasil Update Tanggal Terima Admin");
			
			String jenis_pemegang_polis = editBac.getDatausulan().getJenis_pemegang_polis().toString();
			if(props.getProperty("user.permintaanspaj.simascard").indexOf(editBac.getCurrentUser().getLus_id())>0 && editBac.getPemegang().getReg_spaj()==null){
				Integer countValidPrint=bacManager.selectValidForInput(editBac.getCurrentUser().getLus_id());
				if(countValidPrint>0)err.rejectValue("pemegang.mcl_first","", "Ada Polis Yang sudah di Valid Belum Di Print .Silahkan Print Terlebih Dahulu");
			}
			if(jenis_pemegang_polis != null){
				try{
					//Map<String, Collection> map = new HashMap<String, Collection>();
					//map.put("gelar", elionsManager.selectGelar(1));
					//map.put("jenisUsaha", elionsManager.selectJenisUsaha());
					request.setAttribute("gelar", elionsManager.selectGelar(1));
					ArrayList<DropDown> bidangUsahaList = new ArrayList((List<DropDown>)elionsManager.selectBidangUsaha(2));
					bidangUsahaList.addAll(new ArrayList((List<DropDown>)elionsManager.selectBidangUsaha(1)));
					request.setAttribute("bidangUsaha", bidangUsahaList);
					editBac.getDatausulan().setJenis_pemegang_polis(Integer.parseInt(jenis_pemegang_polis));
				}catch(Exception e){
					editBac.getDatausulan().setJenis_pemegang_polis(0);
				}
			}else{
				editBac.getDatausulan().setJenis_pemegang_polis(0);
			}
			
			if (editBac.getPemegang().getKota_rumah()==null ){
				editBac.getPemegang().setKota_rumah("");
			}
			
			if (editBac.getPemegang().getKota_kantor()==null ){
				editBac.getPemegang().setKota_kantor("");
			}
			
			if (editBac.getAddressbilling().getKota_tgh()==null ){
				editBac.getAddressbilling().setKota_tgh("");				
			}
			if (editBac.getTertanggung().getMste_no_vacc()==null ){
				editBac.getTertanggung().setMste_no_vacc("");				
			}
			
			//rahmayanti
			//untuk menset edit di edit bac
			
			if (editBac.getDatausulan().getMste_flag_investasi()==null )
				editBac.getDatausulan().setMste_flag_investasi(0);				
			
			if(editBac.getPemegang().getEdit_pemegang()==null)
				editBac.getPemegang().setEdit_pemegang(0);
		
			if(editBac.getTertanggung().getEdit_tertanggung()==null)
				editBac.getTertanggung().setEdit_tertanggung(0);
			
			if(editBac.getDatausulan().getEdit_dataUsulan()==null)
				editBac.getDatausulan().setEdit_dataUsulan(0);
			
			if(editBac.getInvestasiutama().getEdit_investasi()==null)
				editBac.getInvestasiutama().setEdit_investasi(0);
			
			if(editBac.getAgen().getEdit_agen()==null)
				editBac.getAgen().setEdit_agen(0);	
			
			//randy
			if(editBac.getPemegang().getBulan_lainnya_note()==null){
				editBac.getPemegang().setBulan_lainnya_note("");
			}
			
			if(editBac.getPemegang().getBulan_lainnya_note().equals("")){
				editBac.getPemegang().setBulan_lainnya(null);
				editBac.getPemegang().setBulan_lainnya_note(null);
			}else{
				editBac.getPemegang().setBulan_lainnya("LAINNYA");
			}

			
			if(editBac.getPemegang().getFlag_upload()==null)
				editBac.getPemegang().setFlag_upload(0);
			//-------------------------------------------------
			if(editBac.getPemegang().getFlag_upload()==null){
				   editBac.getPemegang().setFlag_upload(0);
				}
			if(editBac.getPemegang().getCf_job_code()==null)
				editBac.getPemegang().setCf_job_code("");
			if(editBac.getPemegang().getCf_campaign_code()==null)
				editBac.getPemegang().setCf_campaign_code("");
			if(editBac.getPemegang().getCf_customer_id()==null)
				editBac.getPemegang().setCf_customer_id("");
			
			String lsbs_id_kopi=null;
			
			if(editBac.getDatausulan().isPsave){
				 lsbs_id_kopi= uwManager.selectLsbsId(editBac.getDatausulan().getKopiSPAJ());
				if(lsbs_id_kopi==null){
					lsbs_id_kopi = uwManager.selectLsbsId(editBac.getPowersave().getMsl_spaj_lama());
				}
				if(!products.powerSave(lsbs_id_kopi) ){
					//err.reject("", "Anda tidak dapat melakukan pengkonversian. Silakan pilih CANCEL apabila mengkonversi stable link lama menjadi stable link baru.Terima kasih.");
				}
			}
			
			if(editBac.getDatausulan().getKurs_premi()==null){
				editBac.getDatausulan().setKurs_premi("");
			}
				
				
			if(editBac.getDatausulan().isPsave){
				if(uwManager.selectCountMstSurrender(editBac.getPowersave().getMsl_spaj_lama())==0){
					if(products.powerSave(lsbs_id_kopi) ){
						err.reject("", "Polis ini belum dilakukan proses Surrender, sehingga tidak dapat dilakukan pengkonversian, Harap hubungi Departemen Life Benefit.");
					}
				}
			}
			
			//tambahan
			refData.put("select_penghasilan_info_nasabah",elionsManager.selectDropDown("eka.lst_info_nasabah", "info_ket", "info_ket", "","info_ket", "info_jenis='5' and flag_aktif=1"));
			refData.put("select_bidang_info_nasabah",elionsManager.selectDropDown("eka.lst_info_nasabah", "info_ket", "info_ket", "","info_ket DESC", "info_jenis='3' and flag_aktif=1"));
			refData.put("select_lst_pekerjaan", elionsManager.selectDropDown("eka.lst_pekerjaan", "lsp_id", "lsp_name", "", "lsp_id", "flag_active=1"));
			refData.put("select_relationship", elionsManager.selectDropDown("eka.lst_relation a,eka.lst_group_relation b", "a.lsre_id", "a.lsre_relation", "", "a.lsre_id", "a.lsre_id = b.lsre_id and b.gr_lsre_id=1"));
			//dari tabel
			refData.put("select_identitas", elionsManager.selectDropDown("eka.lst_identity", "lside_id", "lside_name", "", "lside_id", null));
			//refData.put("select_identitas",DroplistManager.getInstance().get("IDENTITY.xml","ID",request));
			refData.put("select_negara", elionsManager.selectDropDown("eka.lst_negara", "lsne_id", "lsne_note", "", "lsne_urut", null));
			//refData.put("select_negara",DroplistManager.getInstance().get("WARGANEGARA.xml","ID",request));
			refData.put("select_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "", "lsag_name", null));
			//refData.put("select_agama",DroplistManager.getInstance().get("AGAMA.xml","ID",request));
			refData.put("select_pendidikan", elionsManager.selectDropDown("eka.lst_education", "lsed_id", "lsed_name", "", "lsed_name", null));
			//refData.put("select_pendidikan",DroplistManager.getInstance().get("PENDIDIKAN.xml","ID",request));
			refData.put("select_jabatan",elionsManager.selectDropDown("eka.lst_jabatan", "ljb_id", "ljb_note", "", "ljb_id", null));
			//chandra a - 20180307
			refData.put("select_propinsi2", elionsManager.selectDropDown("eka.v_propinsi", "distinct lspr_id", "propinsi", "", "propinsi", null));
			//chandra a - 20180307
			
			
			// dari xml, semua pindah ke tabel kalo bisa
			refData.put("select_gelar",DroplistManager.getInstance().get("GELAR.xml","ID",request));
			refData.put("select_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","",request));
			refData.put("select_pekerjaan",DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request));
			refData.put("select_medis",DroplistManager.getInstance().get("medis.xml","ID",request));
			refData.put("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
			refData.put("select_tujuan",DroplistManager.getInstance().get("TUJUAN_ASR.xml","",request));
			refData.put("select_penghasilan",DroplistManager.getInstance().get("PENGHASILAN.xml","",request));
			refData.put("select_aset",DroplistManager.getInstance().get("ASET.xml","",request));
			refData.put("select_dana",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
			refData.put("select_dana_badan_usaha",DroplistManager.getInstance().get("SUMBER_PENDANAAN_BADAN_USAHA.xml","",request));
			refData.put("select_relasi_badan_usaha",DroplistManager.getInstance().get("RELATION_BADAN_USAHA.xml","",request));
			refData.put("select_sumberkyc",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
			refData.put("select_hasil",DroplistManager.getInstance().get("SUMBER_PENGHASILAN.xml","",request));
			refData.put("select_relasi_premi",DroplistManager.getInstance().get("RELATION_PREMI.xml","ID",request));
			refData.put("select_kriteria_kesalahan", DroplistManager.getInstance().get("KRITERIA_KESALAHAN.xml", "", request));

			ArrayList<DropDown> bidangUsahaList = new ArrayList((List<DropDown>)elionsManager.selectBidangUsaha(2));
			bidangUsahaList.addAll(new ArrayList((List<DropDown>)elionsManager.selectBidangUsaha(1)));
			refData.put("bidangUsaha", bidangUsahaList);
			
			//apabila inputan bank
			if(!currentUser.getCab_bank().equals("") && !currentUser.getName().contains("SIMASPRIMA")) {
				refData.put("select_relasi",DroplistManager.getInstance().get("RELATION_BANCASS.xml","",request));
				refData.put("select_flag_jp",DroplistManager.getInstance().get("FLAG_JENIS_PESERTA.xml","",request));
				refData.put("select_prodKes",DroplistManager.getInstance().get("PROD_KESEHATAN.xml","",request));
			}else {
				refData.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
				refData.put("select_flag_jp",DroplistManager.getInstance().get("FLAG_JENIS_PESERTA.xml","",request));
				refData.put("select_prodKes",DroplistManager.getInstance().get("PROD_KESEHATAN.xml","",request));
			}
			
			Integer kode_produk = editBac.getDatausulan().getLsbs_id();
 			Integer number_produk = editBac.getDatausulan().getLsdbs_number();
			String nama_produk="";
			if (kode_produk!=null && number_produk !=null){
				
				nama_produk="produk_asuransi.n_prod_"+FormatString.rpad("0", kode_produk.toString(), 2);
				
				try{
					Class aClass = Class.forName( nama_produk );
					n_prod produk = (n_prod) aClass.newInstance();
					produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
					
					produk.cek_flag_agen(kode_produk, number_produk, 0);
					editBac.getDatausulan().setFlag_platinumlink(produk.flag_platinumlink);

					if (produk.isProductBancass){
						editBac.getDatausulan().setFlag_bao(1);
					}else{
						editBac.getDatausulan().setFlag_bao(0);
					}
					
					if(number_produk==0){
						number_produk=1;
					}
					produk.of_set_bisnis_no(number_produk);
					produk.ii_bisnis_no=(number_produk);
					produk.ii_bisnis_id=(kode_produk);
					
					produk.of_set_kurs(editBac.getDatausulan().getKurs_premi());
					int kode_flag = produk.kode_flag;
					editBac.getDatausulan().setKode_flag(kode_flag);
					if (produk.isBungaSimponi){
						editBac.getDatausulan().setIsBungaSimponi(1);	
					}else{
						editBac.getDatausulan().setIsBungaSimponi(0);	
					}
					if (produk.isBonusTahapan){
						editBac.getDatausulan().setIsBonusTahapan(1);
					}else{
						editBac.getDatausulan().setIsBonusTahapan(0);
					}
					
					editBac.getDatausulan().setFlag_account(produk.flag_account);
					editBac.getDatausulan().setFlag_as(produk.flag_as);
					editBac.getDatausulan().setFlag_worksite(produk.flag_worksite);
					editBac.getDatausulan().setFlag_endowment(produk.flag_endowment);
					editBac.getDatausulan().setFlag_horison(produk.flag_horison);
					
					if (editBac.getDatausulan().getFlag_worksite().intValue() == 1){
						String kode_perusahaan = editBac.getPemegang().getMspo_customer();
						String nama_perusahaan="";
						if(kode_perusahaan!=null){
							Map data1= (HashMap) this.elionsManager.select_namacompany(kode_perusahaan);
							if (data1!=null){		
								nama_perusahaan = (String) data1.get("COMPANY_NAMA");
							}
						}
						if (nama_perusahaan.toUpperCase().contains("EKA LIFE") || nama_perusahaan.toUpperCase().contains("EKALIFE")){
							editBac.getDatausulan().setFlag_worksite_ekalife(1);
//							editBac.getDatausulan().setMste_flag_el(1);
						}
					}
					//tutup try produk rider included
				} catch (ClassNotFoundException e){
					logger.error("ERROR :", e);
				} catch (InstantiationException e) {
					logger.error("ERROR :", e);
				} catch (IllegalAccessException e) {
					logger.error("ERROR :", e);
				}
			}
			editBac.getDatausulan().setLde_id(currentUser.getLde_id());

			//bank rekening autodebet
			String bank_pp=editBac.getAccount_recur().getLbn_id();	
			String nama_bank_autodebet="";
			if(bank_pp!=null){
				Map data1= (HashMap) this.elionsManager.select_bank2(bank_pp);
				if (data1!=null){		
					nama_bank_autodebet = (String) data1.get("BANK_NAMA");
					//cek nama bank rek client kalau sudah pernah diisi
					editBac.getAccount_recur().setLbn_nama(nama_bank_autodebet);
				}
			}
			
			//bank rekening client
			String bank_pp1=editBac.getRekening_client().getLsbp_id();
			String nama_bank_rekclient="";
			if(bank_pp1!=null){
				Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);
				if (data1!=null){		
					nama_bank_rekclient = (String)data1.get("BANK_NAMA");
					editBac.getRekening_client().setLsbp_nama(nama_bank_rekclient);
				}
			}
			
			//cek nama perusahaan kalau sudah dipilih
			String kode_perusahaan = editBac.getPemegang().getMspo_customer();
			String nama_perusahaan="";
			if(kode_perusahaan!=null){
				Map data1= (HashMap) this.elionsManager.select_namacompany(kode_perusahaan);
				if (data1!=null){		
					nama_perusahaan = (String)data1.get("COMPANY_NAMA");
					editBac.getPemegang().setMspo_customer_nama(nama_perusahaan);
				}
			}
			
			//MANTA - Rekening Bank Broker
			String bank_brk = editBac.getBroker().getLbn_id();
			String nama_bank_broker="";
			if(bank_brk!=null){
				Map data1= (HashMap) this.elionsManager.select_bank2(bank_brk);
				if (data1!=null){		
					nama_bank_broker = (String) data1.get("BANK_NAMA");
					//cek nama bank rek client kalau sudah pernah diisi
					editBac.getBroker().setLbn_nama(nama_bank_broker);
				}
			}
			
		/** Halaman 2 : Tertanggung Polis */
		}else if (page == 1){
			refData = new HashMap<String, Object>();
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			if (editBac.getTertanggung().getKota_rumah()==null ){
				editBac.getTertanggung().setKota_rumah("");
			}
			
			if (editBac.getTertanggung().getKota_kantor()==null  ){
				editBac.getTertanggung().setKota_kantor("");
			}
			
			if (editBac.getAddressbilling().getKota_tgh()==null){
				editBac.getAddressbilling().setKota_tgh("");				
			}		
				
			if(editBac.getTertanggung().getBulan_lainnya_note()=="")editBac.getTertanggung().setBulan_lainnya_note(null);
			if(editBac.getTertanggung().getBulan_lainnya_note()==null){
				editBac.getTertanggung().setBulan_lainnya("");
			} else {
				editBac.getTertanggung().setBulan_lainnya("LAINNYA");
				
			}			
			
			refData.put("select_gelar",DroplistManager.getInstance().get("GELAR.xml","ID",request));
			
			refData.put("select_lst_pekerjaan", elionsManager.selectDropDown("eka.lst_pekerjaan", "lsp_id", "lsp_name", "", "lsp_id", "flag_active=1"));
			
			
			// REFDATA UNTUK HALAMAN LAMA
			refData.put("select_identitas",DroplistManager.getInstance().get("IDENTITY.xml","ID",request));
			refData.put("select_negara",DroplistManager.getInstance().get("WARGANEGARA.xml","ID",request));
			refData.put("select_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","",request));
			refData.put("select_pekerjaan",DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request));
			refData.put("select_medis",DroplistManager.getInstance().get("medis.xml","ID",request));
			refData.put("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
			refData.put("select_agama",DroplistManager.getInstance().get("AGAMA.xml","ID",request));
			refData.put("select_pendidikan",DroplistManager.getInstance().get("PENDIDIKAN.xml","ID",request));
			refData.put("select_tujuan",DroplistManager.getInstance().get("TUJUAN_ASR.xml","",request));
			refData.put("select_penghasilan",DroplistManager.getInstance().get("PENGHASILAN.xml","",request));
			refData.put("select_dana",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
			refData.put("select_dana_badan_usaha",DroplistManager.getInstance().get("SUMBER_PENDANAAN_BADAN_USAHA.xml","",request));
			refData.put("select_sumberkyc",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
			refData.put("select_hasil",DroplistManager.getInstance().get("SUMBER_PENGHASILAN.xml","",request));
			refData.put("select_relasi_premi",DroplistManager.getInstance().get("RELATION_PREMI.xml","ID",request));
			refData.put("select_kriteria_kesalahan", DroplistManager.getInstance().get("KRITERIA_KESALAHAN.xml", "", request));
			//chandra a - 20180307
			refData.put("select_propinsi2", elionsManager.selectDropDown("eka.v_propinsi", "distinct lspr_id", "propinsi", "", "propinsi", null));
			//chandra a - 20180307
			
			// REFDATA UNTUK HALAMAN BARU
//			refData.put("select_identitas", elionsManager.selectDropDown("eka.lst_identity", "lside_id", "lside_name", "lside_id", null));
//			refData.put("select_negara", elionsManager.selectDropDown("eka.lst_negara", "lsne_id", "lsne_note", "lsne_id", null));
//			refData.put("select_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "lsag_id", null));
//			refData.put("select_pendidikan", elionsManager.selectDropDown("eka.lst_education", "lsed_id", "lsed_name", "lsed_id", null));
			
			//apabila inputan bank
			if(!currentUser.getCab_bank().equals("") && !currentUser.getName().contains("SIMASPRIMA")) {
				refData.put("select_relasi",DroplistManager.getInstance().get("RELATION_BANCASS.xml","",request));
				refData.put("select_flag_jp",DroplistManager.getInstance().get("FLAG_JENIS_PESERTA.xml","",request));
				refData.put("select_prodKes",DroplistManager.getInstance().get("PROD_KESEHATAN.xml","",request));
			}else {
				refData.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
				refData.put("select_flag_jp",DroplistManager.getInstance().get("FLAG_JENIS_PESERTA.xml","",request));
				refData.put("select_prodKes",DroplistManager.getInstance().get("PROD_KESEHATAN.xml","",request));
			}
		/** Halaman 2 : Data Calon Pembayar Premi*/
		}else if (page == 2){
			refData = new HashMap<String, Object>();
			Cmdeditbac editBac = (Cmdeditbac) cmd;		
			String jenis_pemegang_polis = editBac.getDatausulan().getJenis_pemegang_polis().toString();
			
//			if (editBac.getPembayarPremi().getKota_perusahaan()==null ){
//				editBac.getPembayarPremi().setKota_perusahaan("");
//			}
			refData.put("select_pp", elionsManager.selectDropDown("eka.lst_relation a,eka.lst_group_relation b", "a.lsre_id", "a.lsre_relation", "", "a.lsre_id", "a.lsre_id = b.lsre_id and b.gr_lsre_id=5"));
			refData.put("select_cpp", elionsManager.selectDropDown("eka.lst_relation a,eka.lst_group_relation b", "a.lsre_id", "a.lsre_relation", "", "a.lsre_id", "a.lsre_id = b.lsre_id and b.gr_lsre_id=4"));
			refData.put("select_tujuan_investasi", elionsManager.selectDropDown("eka.lst_info_nasabah", "info_ket", "info_ket", "", "info_ket", "info_jenis='8' and flag_aktif=1"));
			refData.put("select_lst_pekerjaan", elionsManager.selectDropDown("eka.lst_pekerjaan", "lsp_id", "lsp_name", "", "lsp_id", "flag_active=1"));
			refData.put("select_bidang",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","",request));
			refData.put("select_tujuan_dana",DroplistManager.getInstance().get("TUJUAN_ASR.xml","",request));
			refData.put("select_sumber_dana",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
			refData.put("select_jabatan",elionsManager.selectDropDown("eka.lst_jabatan", "ljb_id", "ljb_note", "", "ljb_id", null));
			
			refData.put("select_propinsi", elionsManager.selectDropDown("eka.lst_propinsi_new", "propinsi_id", "propinsi", "", "propinsi_id", null));
			refData.put("select_negara", elionsManager.selectDropDown("eka.lst_negara", "lsne_id", "lsne_note", "", "lsne_urut", null));
			refData.put("select_pekerjaan", elionsManager.selectDropDown("eka.lst_pekerjaan", "lsp_id", "lsp_name", "", "lsp_id", "flag_active=1"));
			refData.put("select_rutin_bulan", elionsManager.selectDropDown("eka.lst_info_nasabah", "info_ket", "info_ket", "", "info_jenis", "info_jenis='6' and flag_aktif=1"));
			refData.put("select_non_tahun", elionsManager.selectDropDown("eka.lst_info_nasabah", "info_ket", "info_ket", "", "info_jenis", "info_jenis='7' and flag_aktif=1"));
			
			if(jenis_pemegang_polis != null){
				try{
					request.setAttribute("gelar", elionsManager.selectGelar(1));
				}catch(Exception e){
					logger.error("ERROR :", e);
				}
			}

		/** Halaman 3 : Data Usulan Asuransi */
		}else if (page ==3){
			refData = new HashMap<String, Object>();
			
			// REFDATA UNTUK HALAMAN LAMA
			if(currentUser.getJn_bank().intValue() == 0 || currentUser.getJn_bank().intValue() == 1){ //BII
				refData.put("listprodukutama", this.elionsManager.select_produkutama_platinumbii());
				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_platinumbii());
			}else if(currentUser.getJn_bank().intValue() == 2 && currentUser.getLus_bas()==0){ //BANK SINARMAS & BST BIASA
				refData.put("listprodukutama", this.elionsManager.select_produkutama_banksinarmas());
				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_banksinarmas());
			}else if(currentUser.getJn_bank().intValue() == 2 && currentUser.getLus_bas()==2){ //BANK SINARMAS & BAC SIMPOL
				refData.put("listprodukutama", this.bacManager.select_produkutama_banksinarmas_simpol());
				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_banksinarmas());
			}else if(currentUser.getJn_bank().intValue() == 3){ //sinarmas sekuritas
				refData.put("listprodukutama", this.elionsManager.select_produkutama_sekuritas());
				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_sekuritas());
			}else if(currentUser.getJn_bank().intValue() == 4){ //admin mall
				refData.put("listprodukutama", this.uwManager.select_produkutama_admin_mall());
				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk_sekuritas());
			}else if(currentUser.getLus_id().equals("2661")){
				refData.put("listprodukutama", this.uwManager.select_produkutama_online());
				refData.put("listtipeproduk",this.uwManager.select_tipeprodukonline());
			}else if(currentUser.getJn_bank().intValue() == 16){//Bank Sinarmas Syariah
				refData.put("listprodukutama", this.uwManager.select_produkutama_bsim());
				refData.put("listtipeproduk",this.uwManager.select_tipeprodukbsim());
			}
			else {
				refData.put("listprodukutama", this.elionsManager.select_produkutama());
				refData.put("listtipeproduk",this.elionsManager.select_tipeproduk());
			}

			// REFDATA UNTUK HALAMAN BARU
//			if(currentUser.getCab_bank() == null) {
//				refData.put("listprodukutama", this.elionsManager.select_produkutama());
//				refData.put("listtipeproduk", elionsManager.selectDropDown("eka.lst_type_produk", "lstp_id", "lstp_produk", "lstp_id", null));
//			}else if(currentUser.getJn_bank().intValue() == 0 || currentUser.getJn_bank().intValue() == 1){ //BII
//				refData.put("listprodukutama", this.elionsManager.select_produkutama_platinumbii());
//				refData.put("listtipeproduk", elionsManager.selectDropDown("eka.lst_type_produk", "lstp_id", "lstp_produk", "lstp_id", "lstp_id = 7"));
//			}else if(currentUser.getJn_bank().intValue() == 2){ //BANK SINARMAS
//				refData.put("listprodukutama", this.elionsManager.select_produkutama_banksinarmas());
//				refData.put("listtipeproduk", elionsManager.selectDropDown("eka.lst_type_produk", "lstp_id", "lstp_produk", "lstp_id", "lstp_id = 5"));
//			}
			
			refData.put("select_warganegara",DroplistManager.getInstance().get("WARGANEGARA.xml","ID",request));
			refData.put("select_autodebet",DroplistManager.getInstance().get("AUTODEBET.xml","ID",request));
			refData.put("select_persenUp",DroplistManager.getInstance().get("PERSENUP.xml","ID",request));
			refData.put("select_insrider",DroplistManager.getInstance().get("INSRIDER.xml","ID",request));
			refData.put("select_rider",DroplistManager.getInstance().get("PLANRIDER.xml","BISNIS_ID",request));
			refData.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
			refData.put("select_flag_jp",DroplistManager.getInstance().get("FLAG_JENIS_PESERTA.xml","",request));
			refData.put("select_prodKes",DroplistManager.getInstance().get("PROD_KESEHATAN.xml","",request));
			refData.put("select_kriteria_kesalahan", DroplistManager.getInstance().get("KRITERIA_KESALAHAN.xml", "", request));
			refData.put("select_face_to_face", new ArrayList(bacManager.selectFaceToFaceCategory()) );
			refData.put("select_medis_desc", new ArrayList(bacManager.selectParamMedisDesc()) ); // Tambah nana medis value mst_config 148420
			
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			Integer lsbs_id = editBac.getDatausulan().getLsbs_id();
			Integer lsdbs_number = editBac.getDatausulan().getLsdbs_number();
			if(lsbs_id != null && lsbs_id !=0) {
				if(lsdbs_number != null && lsdbs_number !=0){
					refData.put("select_rider", new ArrayList(elionsManager.list_rider(lsbs_id.toString(), lsdbs_number.toString())) );
					//helpdesk [137730] tambahin pilihan campaign
					refData.put("select_campaignid", new ArrayList(bacManager.selectMstCampaignProduct(lsbs_id.toString(), lsdbs_number.toString())) );
				}
			}
			if(editBac.getDatausulan().getCampaign_id() == null || editBac.getDatausulan().getCampaign_id().equalsIgnoreCase("0~X0") || editBac.getDatausulan().getCampaign_id().isEmpty()){ //helpdesk [137730] tambahin pilihan campaign
				editBac.getDatausulan().setCampradio("0");
			} else {
				editBac.getDatausulan().setCampradio("1");
			}
			
			refData.put("select_dplk",DroplistManager.getInstance().get("DPLK.xml","ID",request));
			refData.put("select_kombinasi",DroplistManager.getInstance().get("KOMBINASI_PREMI.XML","ID",request));
			
		/** Halaman 4 : Detail Investasi */
		}else if (page==4){
			refData = new HashMap<String, Object>();
			refData.put("select_jns_top_up",DroplistManager.getInstance().get("TOPUP.xml","ID",request));	
			refData.put("select_jenis_nasabah",DroplistManager.getInstance().get("jenis_nasabah.xml","ID",request));
			refData.put("select_jenis_tabungan",DroplistManager.getInstance().get("jenis_tabungan.xml","ID",request));
			refData.put("select_autodebet",DroplistManager.getInstance().get("AUTODEBET.xml","ID",request));
			refData.put("select_kriteria_kesalahan", DroplistManager.getInstance().get("KRITERIA_KESALAHAN.xml", "", request));
			refData.put("select_kriteria_kesalahan", DroplistManager.getInstance().get("KRITERIA_KESALAHAN.xml", "", request));
			refData.put("select_warganegara",DroplistManager.getInstance().get("WARGANEGARA.xml","ID",request));
		
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			Integer kode_produk = editBac.getDatausulan().getLsbs_id();
			Integer kode_subproduk = editBac.getDatausulan().getLsdbs_number();
			
			if(kode_produk.equals(142) & kode_subproduk.equals(13)) //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				refData.put("select_jangka_invest",DroplistManager.getInstance().get("JNSTOPUP_6.xml","ID",request));
			else
				refData.put("select_jangka_invest",DroplistManager.getInstance().get("JNSTOPUP.xml","ID",request));
			
			//bila keluarga stable save
			if(products.stableSavePremiBulanan(kode_produk.toString()) || products.stableSave(kode_produk, editBac.getDatausulan().getLsdbs_number()) || products.progressiveLink(kode_produk.toString())){
				if(products.stableSavePremiBulanan(kode_produk.toString()) || products.progressiveLink(kode_produk.toString())){
					refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_STABSAVE_PREMI.xml","ID",request));
				}else{
					if(products.stableSave(kode_produk, editBac.getDatausulan().getLsdbs_number())){
						if(editBac.getDatausulan().getLsdbs_number()==5){
							if(editBac.getDatausulan().getFlag_bulanan()==1){
								refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_SUCC_BULANAN.xml","ID",request));
							}else if(editBac.getDatausulan().getFlag_bulanan()==0){
								refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_SUCC_NON_BULANAN.xml","ID",request));
							}
						}else{
							//refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_SUCC_NON_BULANAN.xml","ID",request));
							refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_STABSAVE.xml","ID",request));
						}
					}else{
						refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_STABSAVE.xml","ID",request));
					}
				}
			//bila polisinputan bank sinarmas
			}else if(!currentUser.getCab_bank().equals("") && currentUser.getJn_bank().intValue() == 2) {
				refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_BANCASS.xml","ID",request));
				//if(currentUser.getName().contains("SIMASPRIMA")) refData.put("select_jenisbunga",DroplistManager.getInstance().get("jenisbunga.xml","",request));
			//bila powersave bulanan, hanya buka rollover premi
			}else if(kode_produk.intValue() == 158 || kode_produk.intValue() == 176 ){
				refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_BULANAN.xml","ID",request));
			}else if(kode_produk.equals(142) & kode_subproduk.equals(13)){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER_PREMI_ONLY.xml","ID",request));
			}else {
				refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER.xml","ID",request));
			}

			refData.put("select_jenisbunga",DroplistManager.getInstance().get("jenisbunga_bancass.xml","",request));
			//hanya cabang BAS / UW yang bisa input special rate, selain itu hanya normal saja
			if(currentUser.getLde_id().equals("11") || currentUser.getLde_id().equals("29") || currentUser.getJn_bank().intValue() == 3){
				refData.put("select_jenisbunga",DroplistManager.getInstance().get("jenisbunga.xml","",request));
			}
			
			if(!currentUser.getCab_bank().equals("") && !currentUser.getName().contains("SIMASPRIMA")) {
				refData.put("select_hubungan",DroplistManager.getInstance().get("RELATION_BANCASS_BENEF.xml","",request));
			}else {
				refData.put("select_hubungan",DroplistManager.getInstance().get("RELATION.xml","",request));
			}
			
			refData.put("select_karyawan",DroplistManager.getInstance().get("karyawan.xml","",request));
			refData.put("select_kurs",DroplistManager.getInstance().get("KURS.xml","ID",request));

		/** Halaman 5 : Detail Agen*/
		}else if (page ==5){
			if(currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 3){ //BANK SINARMAS
				Cmdeditbac editBac = (Cmdeditbac) cmd;
				editBac.getAddressbilling().setRegion("010100");
			}else if(currentUser.getJn_bank().intValue() == 16){
				Cmdeditbac editBac = (Cmdeditbac) cmd;
				if(editBac.getDatausulan().getKodeproduk().equals("175")){
					editBac.getAddressbilling().setRegion("090100");
					editBac.getAgen().setMsag_id("901388");
					editBac.getPemegang().setMspo_ao("901388");
				}else{
					editBac.getAddressbilling().setRegion("090100");
					editBac.getAgen().setMsag_id("901389");
					editBac.getPemegang().setMspo_ao("901389");
				}
			}
			refData = new HashMap<String, Object>();
			refData.put("select_regional", new ArrayList(elionsManager.selectDropDown("eka.lst_region", "(lca_id||lwk_id||lsrg_id)", "lsrg_nama", "", "lsrg_nama", null)));
			refData.put("select_kriteria_kesalahan", DroplistManager.getInstance().get("KRITERIA_KESALAHAN.xml", "", request));
			//refData.put("select_regional", DroplistManager.getInstance().get("region.xml","ID",request));
			
			//for stable save hadiah
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			editBac.getPemegang().setFlag_hadiah(0);
			refData.put("lh_harga", editBac.getDatausulan().getMspr_premium());
			refData.put("select_hadiah", new ArrayList<Object>(uwManager.selectHadiahPS(editBac.getDatausulan().getMspr_premium())));
			if(editBac.getDatausulan().getLsbs_id()==184 && editBac.getDatausulan().getLsdbs_number()==6){
				editBac.getPemegang().setFlag_hadiah(1);
			}
			
		/** Halaman 6 : Konfirmasi */
		}else if (page==6)	{
			Cmdeditbac editBac = (Cmdeditbac) cmd;
			String kode=editBac.getDatausulan().getPlan();
			refData = new HashMap<String, Object>();
			ArrayList<String> tmp = new ArrayList(this.elionsManager.select_namaproduk(kode));
			refData.put("listprodukutama",tmp);
			
			HttpSession session = request.getSession();
			String res = session.getServletContext().getResource("/xml/").toString();
			
			refData.put("select_lst_pekerjaan", elionsManager.selectDropDown("eka.lst_pekerjaan", "lsp_id", "lsp_name", "", "lsp_id", "flag_active=1"));
			refData.put("jenis_id_pp", Common.searchXml(res+"IDENTITY.xml", "ID", "TDPENGENAL", editBac.getPemegang().getLside_id()));
			refData.put("negara_pp", Common.searchXml(res+"WARGANEGARA.xml", "ID", "NEGARA", editBac.getPemegang().getLsne_id()));
			refData.put("status_pp", Common.searchXml(res+"MARITAL.xml", "ID", "MARITAL", editBac.getPemegang().getMspe_sts_mrt()));
			refData.put("agama_pp", Common.searchXml(res+"AGAMA.xml", "ID", "AGAMA", editBac.getPemegang().getLsag_id()));
			refData.put("pendidikan_pp", Common.searchXml(res+"PENDIDIKAN.xml", "ID", "PENDIDIKAN", editBac.getPemegang().getLsed_id()));
			
			refData.put("jenis_id_pic", Common.searchXml(res+"IDENTITY.xml", "ID", "TDPENGENAL", editBac.getContactPerson().getLside_id()));
			refData.put("negara_pic", Common.searchXml(res+"WARGANEGARA.xml", "ID", "NEGARA", editBac.getContactPerson().getLsne_id()));
			refData.put("status_pic", Common.searchXml(res+"MARITAL.xml", "ID", "MARITAL", editBac.getContactPerson().getSts_mrt()));
			refData.put("agama_pic", Common.searchXml(res+"AGAMA.xml", "ID", "AGAMA", editBac.getContactPerson().getLsag_id()));
			refData.put("pendidikan_pic", Common.searchXml(res+"PENDIDIKAN.xml", "ID", "PENDIDIKAN", editBac.getContactPerson().getLsed_id()));

			if(!currentUser.getCab_bank().equals("") && !currentUser.getName().contains("SIMASPRIMA")) {
				refData.put("relation_ttg", Common.searchXml(res+"RELATION.xml", "ID", "RELATION_BANCASS", editBac.getPemegang().getLsre_id()));
			}else {
				refData.put("relation_ttg", Common.searchXml(res+"RELATION.xml", "ID", "RELATION", editBac.getPemegang().getLsre_id()));
			}

			refData.put("jenis_id_ttg1", Common.searchXml(res+"IDENTITY.xml", "ID", "TDPENGENAL", editBac.getTertanggung().getLside_id()));
			refData.put("negara_ttg1", Common.searchXml(res+"WARGANEGARA.xml", "ID", "NEGARA", editBac.getTertanggung().getLsne_id()));
			refData.put("status_ttg1", Common.searchXml(res+"MARITAL.xml", "ID", "MARITAL", editBac.getTertanggung().getMspe_sts_mrt()));
			refData.put("agama_ttg1", Common.searchXml(res+"AGAMA.xml", "ID", "AGAMA", editBac.getTertanggung().getLsag_id()));
			refData.put("pendidikan_ttg1", Common.searchXml(res+"PENDIDIKAN.xml", "ID", "PENDIDIKAN", editBac.getTertanggung().getLsed_id()));

			refData.put("kurs_premi", Common.searchXml(res+"KURS.xml", "ID", "SYMBOL", editBac.getDatausulan().getKurs_p()));
			refData.put("kurs_up", Common.searchXml(res+"KURS.xml", "ID", "SYMBOL", editBac.getDatausulan().getKurs_p()));
			refData.put("pmode", Common.searchXml(res+"CARABAYAR.xml", "ID", "PAYMODE", editBac.getDatausulan().getLscb_id()));
			refData.put("auto_debet", Common.searchXml(res+"AUTODEBET.xml", "ID", "AUTODEBET", editBac.getDatausulan().getMste_flag_cc()));

			refData.put("regional", Common.searchXml(res+"region.xml", "ID", "REGION", editBac.getAddressbilling().getRegion()));
			refData.put("roll", Common.searchXml(res+"ROLLOVER.xml", "ID", "ROLLOVER", editBac.getPowersave().getMps_roll_over()));
			refData.put("tab",Common.searchXml(res+"jenis_tabungan.xml", "ID", "AUTODEBET", editBac.getRekening_client().getMrc_jenis()));
			refData.put("nsbh", Common.searchXml(res+"jenis_nasabah.xml", "ID", "JENIS", editBac.getRekening_client().getMrc_jn_nasabah()));
			refData.put("jnstopup", Common.searchXml(res+"TOPUP.xml", "ID", "JENIS", editBac.getInvestasiutama().getDaftartopup().getPil_berkala()));
			refData.put("jk", Common.searchXml(res+"JNSTOPUP.xml", "ID", "JANGKAWAKTU", editBac.getPowersave().getMps_jangka_inv()));
			refData.put("jns", Common.searchXml(res+"jenisbunga.xml", "ID", "JENIS", editBac.getPowersave().getMps_jenis_plan()));
			refData.put("kry", Common.searchXml(res+"karyawan.xml", "ID", "KRY", editBac.getPowersave().getMps_employee()));
			refData.put("kurs_rek", Common.searchXml(res+"KURS.xml", "ID", "SYMBOL", editBac.getRekening_client().getMrc_kurs()));
			
			refData.put("select_insrider",DroplistManager.getInstance().get("INSRIDER.xml","ID",request));
			refData.put("select_persenUp",DroplistManager.getInstance().get("PERSENUP.xml","ID",request));
			refData.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
			refData.put("select_flag_jp",DroplistManager.getInstance().get("FLAG_JENIS_PESERTA.xml","",request));
			refData.put("select_prodKes",DroplistManager.getInstance().get("PROD_KESEHATAN.xml","",request));
			
			refData.put("select_rider",DroplistManager.getInstance().get("PLANRIDER.xml","BISNIS_ID",request));
			if(editBac.getDatausulan().getLsbs_id() != null) {
				refData.put("select_rider", new ArrayList(elionsManager.list_rider(editBac.getDatausulan().getLsbs_id().toString(), editBac.getDatausulan().getLsdbs_number().toString())));
			}

			if(!currentUser.getCab_bank().equals("") && !currentUser.getName().contains("SIMASPRIMA")) {
				refData.put("select_hubungan",DroplistManager.getInstance().get("RELATION_BANCASS.xml","ID",request));
			}else {
				refData.put("select_hubungan",DroplistManager.getInstance().get("RELATION.xml","ID",request));
			}
			
			editBac.getPemegang().setKerjaNote(editBac.getPemegang().getMkl_kerja());
			editBac.getTertanggung().setKerjaNote(editBac.getTertanggung().getMkl_kerja());
			
			if(!currentUser.getCab_bank().equals("")) {
				if(currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 16) {
					String spv=request.getParameter("spv");
					
					String valid1=elionsManager.selectUserName(currentUser.getValid_bank_1());
					String valid2=elionsManager.selectUserName(currentUser.getValid_bank_2());
					String pass1 = editBac.getPemegang().getPass1();
					String pass2 = editBac.getPemegang().getPass2();
					String password1 = editBac.getPemegang().getPassword1();
					String password2 = editBac.getPemegang().getPassword2();
					refData.put("valid1", elionsManager.selectUserName(currentUser.getValid_bank_1()));
					refData.put("valid2", elionsManager.selectUserName(currentUser.getValid_bank_2()));
					if (spv!=null){
						spv= spv.toUpperCase();
						editBac.getPemegang().setSpv(spv);
						refData.put("spv", "-");
						if (!(spv.equalsIgnoreCase(valid1) || spv.equalsIgnoreCase(valid2)) && !err.hasErrors()){
							err.rejectValue("pemegang.pass1","","Password SPV/PINCAB yang anda masukkan salah.");
						}
					}
				}else if(currentUser.getJn_bank().intValue() == 3) {
					refData.put("valid1", "-");
					refData.put("daftarOtor", new ArrayList(elionsManager.selectDropDown(
							"EKA.LST_USER", "LUS_ID", "LUS_LOGIN_NAME", "", "LUS_LOGIN_NAME", 
							"cab_bank = '"+currentUser.getCab_bank()+"' AND jn_bank = 3 AND flag_approve = 1 AND lus_id <> " + currentUser.getLus_id())));
				}
			}				
		}
		
		return refData;
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
		String lde_id = currentUser.getLde_id();
		String spv=request.getParameter("spv");
		String spaj = request.getParameter("showSPAJ");
        session.removeAttribute("dataInputSpaj"); //remove session lama
		Cmdeditbac detiledit = (Cmdeditbac) session.getAttribute("dataInputSpaj");
		if(detiledit != null){
			detiledit.setFlag_special_case("0");
		}
		//if(detiledit != null){
			//if(detiledit.getDatausulan().getJenis_pemegang_polis() == null){
				//jenis_pemegang_polis = request.getParameter("jenis_pemegang_polis");
			//}else{
				//jenis_pemegang_polis = detiledit.getDatausulan().getJenis_pemegang_polis().toString();
			//}
		//}
		
		
		
		String jenis_pemegang_polis = null;
		
		if(currentUser.getTempJenisPemegangPolis()==null){
			if(request.getParameter("jenis_pemegang_polis") == null){
				jenis_pemegang_polis = "0";
				currentUser.setTempJenisPemegangPolis(jenis_pemegang_polis);
			}else{
				jenis_pemegang_polis = request.getParameter("jenis_pemegang_polis");
				currentUser.setTempJenisPemegangPolis(jenis_pemegang_polis);
			}
		}else{
			if(request.getParameter("jenis_pemegang_polis") == null){
				jenis_pemegang_polis = currentUser.getTempJenisPemegangPolis();
			}else{
				jenis_pemegang_polis = request.getParameter("jenis_pemegang_polis");
				currentUser.setTempJenisPemegangPolis(jenis_pemegang_polis);
			}
		}
		
		/*if(currentUser.getTempJenisPemegangPolis()==null || currentUser.getTempJenisPemegangPolis().equals("")){
			if(request.getParameter("jenis_pemegang_polis") == null){
				jenis_pemegang_polis = "0";
			}else{
				jenis_pemegang_polis = request.getParameter("jenis_pemegang_polis");
			}
			currentUser.setTempJenisPemegangPolis(jenis_pemegang_polis);
		}
		
		jenis_pemegang_polis = currentUser.getTempJenisPemegangPolis();*/
		
		String pPolis = ""+this.bacManager.selectJenisPemegangPolis(spaj); 
		
		if (pPolis.equals("1")){
			pPolis = "0";
		}else if(pPolis.equals("2")){
			pPolis ="1";
		}else {
			pPolis ="4";
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
			String jn_pp_polis = ServletRequestUtils.getStringParameter(request, "jn_pp_polis", "");
			
			if(!jn_pp_polis.equals("")){
				jenis_pemegang_polis = jn_pp_polis;
				currentUser.setTempJenisPemegangPolis(jenis_pemegang_polis);
			}
			
			/** EDIT SPAJ */
			if(spaj!=null) {
				int v = this.elionsManager.selectValidasiEditUnitLink(spaj);
				if(v > 0) {
					detiledit.setPesan("PERHATIAN!!! Proses FUND sudah dilakukan untuk polis ini. Anda tidak bisa mengedit detail Unit-Link.");
				}
				
				pPolis = ""+this.bacManager.selectJenisPemegangPolis(spaj); 
				
				if (pPolis.equals("1")){
					pPolis = "0";
				}else if(pPolis.equals("2")){
					pPolis ="1";
				}else {
					pPolis ="2";
				}
				
				HashMap<String, Object> jenisPihakKetiga = (HashMap<String, Object>) this.bacManager.selectJenisPihakKetiga(spaj);
				PembayarPremi pm = this.bacManager.selectPembayarPremi(spaj);
				
				String pihakKetiga = (String) jenisPihakKetiga.get("PIHAK_KETIGA");
				String jenisPihak = (String) jenisPihakKetiga.get("JENIS_PIHAK_KETIGA");

				
				if(pm == null){
					pm = new PembayarPremi();
					detiledit.setPembayarPremi(pm);
				}else{
					detiledit.setPembayarPremi(pm);
				}
				
				detiledit.setPemegang((Pemegang)this.elionsManager.selectpp(spaj));
				
			    String mklId = "0";
				mklId = bacManager.selectIdLstPekerjaan(detiledit.getPemegang().getMkl_kerja());
				detiledit.getPemegang().setMkl_kerja(mklId);
				
				detiledit.getPemegang().setPemegang_polis(pPolis);
				//detiledit.getPembayarPremi().setAda_pihak_ketiga(pihakKetiga);
				detiledit.getPembayarPremi().setJenis_pihak_ketiga(jenisPihak);
				detiledit.getPemegang().setSpv(spv);
				detiledit.getPemegang().setDaftarKyc(new ArrayList(uwManager.selectDaftar_kyc(spaj)));
				detiledit.getPemegang().setDaftarKyc2(new ArrayList(uwManager.selectDaftar_kyc2(spaj)));
				
				HashMap sumber_bisnis = Common.serializableMap(elionsManager.select_sumberBisnis(detiledit.getPemegang().getSumber_id()));
				if(sumber_bisnis != null){
					String nama_sumber = (String) sumber_bisnis.get("NAMA_SUMBER");
					detiledit.getPemegang().setNama_sumber(nama_sumber);
				}
				
				detiledit.setNo_pb(uwManager.selectNoPB(spaj));
				detiledit.setAddressbilling((AddressBilling)this.elionsManager.selectAddressBilling(spaj));
				if(detiledit.getAddressbilling()!=null){
					if(detiledit.getAddressbilling().getMsap_address().equals(detiledit.getPemegang().getAlamat_rumah())){
						detiledit.getAddressbilling().setTagih("2");
					}else if(detiledit.getAddressbilling().getMsap_address().equals(detiledit.getPemegang().getAlamat_kantor())){
						detiledit.getAddressbilling().setTagih("3");
					}else if(detiledit.getAddressbilling().getMsap_address().equals(detiledit.getPemegang().getAlamat_tpt_tinggal())){
						detiledit.getAddressbilling().setTagih("4");
					}else{
						detiledit.getAddressbilling().setTagih("1");
					}
				}
				detiledit.setDatausulan((Datausulan)this.elionsManager.selectDataUsulanutama(spaj));
				detiledit.getDatausulan().setLde_id(lde_id);
				detiledit.setTertanggung((Tertanggung)this.elionsManager.selectttg(spaj));
				
				/** flag penanda untuk enable full autosales checkbox, jika edit BAS QA lspd_id = 218 **/
				if (detiledit.getDatausulan().getLspd_id() == 1 || detiledit.getDatausulan().getLspd_id() == 218 ){
					detiledit.getDatausulan().setEnable_full_autosales("4");
				}
				
				//TAMBAHAN UNTUK PERUSAHAAN
				// set isi jenis pemegang polis (INDIVIDU/PERUSAHAAN)
				// TODO:ada tambahan get jenis_pemegang_polis yang harus dipasang
				if(detiledit.getPemegang().getMcl_jenis() != null && spaj!=null){
					jenis_pemegang_polis = detiledit.getPemegang().getMcl_jenis().toString();
					detiledit.setPersonal((Personal)this.elionsManager.selectProfilePerusahaan(detiledit.getPemegang().getMcl_id()));
					detiledit.getPersonal().setAddress(new AddressNew());
					detiledit.setContactPerson((ContactPerson)this.elionsManager.selectpic(detiledit.getPemegang().getMcl_id()));
					if(detiledit.getContactPerson() == null)detiledit.setContactPerson(new ContactPerson());
				}
				if(jenis_pemegang_polis != null){
					try{
						//Map<String, Collection> map = new HashMap<String, Collection>();
						//map.put("gelar", elionsManager.selectGelar(1));
						//map.put("jenisUsaha", elionsManager.selectJenisUsaha());
						request.setAttribute("gelar", new ArrayList(elionsManager.selectGelar(1)));
						ArrayList<DropDown> bidangUsahaList = (new ArrayList((List<DropDown>)elionsManager.selectBidangUsaha(2)));
						bidangUsahaList.addAll(new ArrayList((List<DropDown>)elionsManager.selectBidangUsaha(1)));
						request.setAttribute("bidangUsaha", bidangUsahaList);
						detiledit.getDatausulan().setJenis_pemegang_polis(Integer.parseInt(jenis_pemegang_polis));
					}catch(Exception e){
						detiledit.getDatausulan().setJenis_pemegang_polis(0);
					}
				}else{
					detiledit.getDatausulan().setJenis_pemegang_polis(0);
				}
				//detiledit.setPersonal(new Personal());
				//detiledit.getPersonal().setAddress(new AddressNew());
				//==================================

				Powersave data_pwr= new Powersave();
				String spaj_conversi = uwManager.selectCekSpajSebelumSurrender(spaj);
				if(spaj_conversi!=null || !Common.isEmpty(topupslinkke)){
					data_pwr.setMsl_spaj_lama(spaj_conversi);
				}
				if (products.stableLink(detiledit.getDatausulan().getLsbs_id().toString())) {
					data_pwr = (Powersave)this.elionsManager.select_slink(spaj);
					if(spaj_conversi!=null){
						data_pwr.setMsl_spaj_lama(spaj_conversi);
					}
					Powersave dt = (Powersave)this.elionsManager.select_slink_topup(spaj);
					if(data_pwr.getFlag_rider()==1){//ada rider
						HashMap mappingRiderSave = Common.serializableMap(uwManager.select_rider_save(spaj));
						BigDecimal mpr_cb_rider = (BigDecimal) mappingRiderSave.get("MRS_RIDER_CB");
						BigDecimal lscb_id_rider = (BigDecimal) mappingRiderSave.get("LSCB_ID_RIDER");
						data_pwr.setMpr_cara_bayar_rider(mpr_cb_rider.intValue());
						detiledit.getDatausulan().setLscb_id_rider(lscb_id_rider.intValue());
					}
					if (dt == null) {
						InvestasiUtama inv = new InvestasiUtama();
						
						// DAFTAR INVESTASI UNTUK HALAMAN LAMA						
						inv.setDaftarinvestasi(new ArrayList(this.elionsManager.selectDetailInvestasi(null)));
						inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
						
						detiledit.setInvestasiutama(inv);
						DetilTopUp  detiltopup = new DetilTopUp();
						inv.setDaftartopup(detiltopup);
						data_pwr.setBegdate_topup(null);
						data_pwr.setEnddate_topup(null);
						if(detiledit.getDatausulan().getLsbs_id()==186){
							detiledit.getDatausulan().setCara_premi(0);
						}else{
							detiledit.getDatausulan().setCara_premi(1);
						}
						
					}else{
						InvestasiUtama inv = new InvestasiUtama();

						// DAFTAR INVESTASI UNTUK HALAMAN LAMA						
						inv.setDaftarinvestasi(new ArrayList(this.elionsManager.selectDetailInvestasi(null)));
						inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
						
						detiledit.setInvestasiutama(inv);
						DetilTopUp  detiltopup = new DetilTopUp();
						inv.setDaftartopup(detiltopup);
						if(detiledit.getDatausulan().getLsbs_id()==186){
							detiledit.getDatausulan().setCara_premi(0);
						}else{
							detiledit.getDatausulan().setCara_premi(1);
						}
						detiledit.getDatausulan().setTotal_premi_kombinasi(data_pwr.getMps_prm_deposit() + dt.getMps_prm_deposit());
						inv.getDaftartopup().setPil_tunggal(2);
						//inv.getDaftartopup().setPremi_tunggal(dt.getMps_prm_deposit());
						data_pwr.setBegdate_topup(dt.getMsl_bdate());
						data_pwr.setEnddate_topup(dt.getMsl_edate());
						if(spaj_conversi!=null){
							Powersave roTerakhir = uwManager.selectRolloverPowersaveTerakhir(spaj_conversi);
							data_pwr.setBegdate_topup(roTerakhir.mpr_mature_date);
							detiledit.getDatausulan().setPsave(true);
						}
					}
	
				}else{
					data_pwr = (Powersave) this.elionsManager.select_powersaver(spaj);
					if(detiledit.getDatausulan().getLsbs_id()==188){
						data_pwr = (Powersave) this.uwManager.select_powersaver_baru(spaj);
					}
					if(data_pwr != null){
						if(data_pwr.getFlag_rider() != null){
							if(data_pwr.getFlag_rider()==1){//ada rider
								HashMap mappingRiderSave = Common.serializableMap(uwManager.select_rider_save(spaj));
								BigDecimal mpr_cb_rider = (BigDecimal) mappingRiderSave.get("MRS_RIDER_CB");
								BigDecimal lscb_id_rider = (BigDecimal) mappingRiderSave.get("LSCB_ID_RIDER");
								data_pwr.setMpr_cara_bayar_rider(mpr_cb_rider.intValue());
								detiledit.getDatausulan().setLscb_id_rider(lscb_id_rider.intValue());
							}
						}
						if(spaj_conversi != null){
							Powersave roTerakhir = uwManager.selectRolloverPowersaveTerakhir(spaj_conversi);
							data_pwr.setBegdate_topup(roTerakhir.mpr_mature_date);
							detiledit.getDatausulan().setPlatinumSave(true);
							data_pwr.setMsl_spaj_lama(spaj_conversi);
							
							Integer mssur_se = uwManager.selectCountMstSurrender(spaj_conversi);
							detiledit.getDatausulan().setMssur_se(mssur_se);
						}
					}
					
					
					InvestasiUtama investasi  = (InvestasiUtama)this.elionsManager.selectinvestasiutama(spaj);
					if (investasi == null) {
						InvestasiUtama inv = new InvestasiUtama();

						// DAFTAR INVESTASI UNTUK HALAMAN LAMA						
						inv.setDaftarinvestasi(new ArrayList(this.elionsManager.selectDetailInvestasi(null)));
						inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
						
						detiledit.setInvestasiutama(inv);
						DetilTopUp  detiltopup = new DetilTopUp();
						inv.setDaftartopup(detiltopup);
					}else{
						detiledit.setInvestasiutama(investasi);
						DetilTopUp detiltopup = (DetilTopUp)this.elionsManager.select_detil_topup(spaj);
						if (detiltopup == null) {
							detiltopup = new DetilTopUp();
							detiledit.getInvestasiutama().setDaftartopup(detiltopup);
						}else{
							detiledit.getInvestasiutama().setDaftartopup(detiltopup);
						}
						
						if(detiledit.getDatausulan().getLsbs_id()==121 || detiledit.getDatausulan().getLsbs_id()==160){
							detiledit.getDatausulan().setCara_premi(1);
							detiledit.getDatausulan().setTotal_premi_kombinasi(investasi.getMu_jlh_premi() + investasi.getMu_jlh_tu());
						}
					}
					
					
				}
				
				
				
				if (data_pwr==null) {
					detiledit.setPowersave(new Powersave());
				}else{
					detiledit.setPowersave(data_pwr);
				}

				//START OF fee based income = rate powersave + mst_default (Yusuf 3/4/2008)
				HashMap info = Common.serializableMap(elionsManager.selectInformasiPowersaveUntukFeeBased(spaj));
				if(info != null) {
					String nama_produk="produk_asuransi.n_prod_"+FormatString.rpad("0", ((BigDecimal) info.get("LSBS_ID")).toString(), 2);
					Class aClass = Class.forName( nama_produk );
					n_prod produk = (n_prod) aClass.newInstance();
					produk.ii_bisnis_no = ((BigDecimal) info.get("LSDBS_NUMBER")).intValue();
					produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
					
					if(detiledit.getPowersave().getFlag_bulanan() != null){
						produk.of_get_bisnis_no(detiledit.getPowersave().getFlag_bulanan());
					}else{
						produk.of_get_bisnis_no(0);
					}
					
					hit_biaya_powersave h = new hit_biaya_powersave();
					
					int flag_breakable = 0;
					if(detiledit.getPowersave().getMpr_breakable() != null) flag_breakable = detiledit.getPowersave().getMpr_breakable();  
					
					HashMap data =  Common.serializableMap(elionsManager.selectbungaprosave(
							(String) info.get("LKU_ID"),
							String.valueOf(h.hit_jangka_invest(((BigDecimal) info.get("MPR_JANGKA_INVEST")).toString())),
							((BigDecimal) info.get("MPR_DEPOSIT")).doubleValue(),
							(Date) info.get("MSTE_BEG_DATE"),
							produk.flag_powersave, flag_breakable));
					
					if(data != null) {
						double rate = Double.parseDouble(data.get("LPR_RATE").toString());
						double feebased = elionsManager.selectMst_default_numeric(31);
						detiledit.getPowersave().setFee_based_income((rate + feebased) - detiledit.getPowersave().getMps_rate());
					}
				}else {
					detiledit.getPowersave().setFee_based_income(0.);
				}
				//END OF fee based income = rate powersave + mst_default (Yusuf 3/4/2008)

				detiledit.setRekening_client(this.elionsManager.select_rek_client(spaj));
				detiledit.setAccount_recur(this.elionsManager.select_account_recur(spaj));
				detiledit.setAgen(this.elionsManager.select_detilagen(spaj));
				
				//MANTA - Broker
				detiledit.setBroker(this.uwManager.select_detilbroker(spaj));
				
				/**
				 * TAMBAHAN DARI BERTHO UNTUK SPLIT NO REKENING
				 */			
				detiledit.getRekening_client().setMrc_no_ac_split(FormatString.splitWordToCharacter(detiledit.getRekening_client().getMrc_no_ac(),21));
				detiledit.getAccount_recur().setMar_acc_no_split(FormatString.splitWordToCharacter(detiledit.getAccount_recur().getMar_acc_no(),21));
				////--------------END OF SPLIT REKENING--------------------////////////
			
				String kode_agen = detiledit.getAgen().getMsag_id();
				String nama_agent="";
				if (kode_agen.equalsIgnoreCase("000000")){
					nama_agent = (String) this.elionsManager.select_agent_temp(spaj);
				}
				detiledit.getAgen().setMcl_first(nama_agent);
				detiledit.setEmployee(this.elionsManager.select_detilemployee(spaj));
				
				detiledit.setHistory(new History());
				detiledit.getDatausulan().setDaftahcp(new ArrayList(this.elionsManager.select_hcp(spaj)));
				detiledit.getDatausulan().setDaftapeserta(new ArrayList(this.elionsManager.select_semua_mst_peserta(spaj)));
				detiledit.getDatausulan().setDaftarplus(new ArrayList(this.uwManager.select_all_mst_peserta(spaj)));
				
				//referensi(tambang emas)--cek apakah ada id referral untuk no polis/spaj ini
				Integer ref = uwManager.selectCekRef(detiledit.getPemegang().getMcl_first(), defaultDateFormat.format(detiledit.getPemegang().getMspe_date_birth()), detiledit.getTertanggung().getMcl_first(), defaultDateFormat.format(detiledit.getTertanggung().getMspe_date_birth()));
				if(ref>0){
					//List l_ref = uwManager.selectRefferal(detiledit.getPemegang().getMcl_first(), defaultDateFormat.format(detiledit.getPemegang().getMspe_date_birth()));
					//List l_ref = uwManager.selectKodeGenerate(detiledit.getPemegang().getReg_spaj());
					ArrayList l_ref = new ArrayList(uwManager.selectKodeGenerate(detiledit.getPemegang().getMcl_first(), defaultDateFormat.format(detiledit.getPemegang().getMspe_date_birth())));
					if(l_ref != null){
						if(!l_ref.isEmpty()){
							HashMap reff = Common.serializableMap((HashMap)l_ref.get(0));
							
							detiledit.getAgen().setId_ref((String) reff.get("ID_REF"));
							detiledit.getAgen().setName_ref((String) reff.get("NAMA_REF"));
							detiledit.getAgen().setJenis_ref(((BigDecimal) reff.get("JENIS_REF")).intValue());
						}
					}
					detiledit.getAgen().setPesan_ref("Polis ini memiliki Id Referral");
				}else{
					detiledit.getAgen().setPesan_ref(null);
				}
				//end
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
				detiledit.setPersonal(new Personal());
				detiledit.setContactPerson(new ContactPerson());
				detiledit.getPersonal().setAddress(new AddressNew());
				//==================================

				// DAFTAR INVESTASI UNTUK HALAMAN LAMA						
				inv.setDaftarinvestasi(new ArrayList(this.elionsManager.selectDetailInvestasi(null)));
				inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
				
				detiledit.setInvestasiutama(inv);
				inv.setDaftartopup(new DetilTopUp());
				detiledit.setRekening_client(new Rekening_client());
				detiledit.setAccount_recur(new Account_recur());
				detiledit.setPowersave(new Powersave());

				//fee based income = rate powersave + mst_default (Yusuf 3/4/2008)
				detiledit.getPowersave().setFee_based_income(elionsManager.selectMst_default_numeric(31));
				
				detiledit.setAgen(new Agen());
				detiledit.setHistory(new History());
				detiledit.setEmployee(new Employee());
				detiledit.setBroker(new Broker());
				
				//Yusuf (31 Aug 2009) - Bagian dari proses Konversi Psave ke Slink
				//Apakah Spaj ini merupakan konversi produk Powersave / Simas Prima ke Stable Link / Simas Stabil Link?
				//boolean isPsaveToSlink = Boolean.parseBoolean(ServletRequestUtils.getStringParameter(request, "isPsaveToSlink", "false"));
				
				//Yusuf - apabila kopi data pemegang/tertanggung, langsung set data pemegang dan tertanggungnya, serta address billing nya
				if(!kopiSPAJ.trim().equals("")) {					
					//lufi upload spaj					
					
					
					if(flag_upload.equals("1")){
						String no_temp=kopiSPAJ;						
						Pemegang pemegangTemp=new Pemegang();
						AddressBilling addrBillTemp=new AddressBilling();
						Tertanggung ttgTemp=new Tertanggung();
						Account_recur acrTemp=new Account_recur();						
						pemegangTemp=(Pemegang) this.bacManager.selectppTemp(no_temp);						
						addrBillTemp=(AddressBilling) this.bacManager.selectAddressBillingTemp(no_temp);
						
					    ttgTemp=(Tertanggung)this.bacManager.selectttgTemp(no_temp);
					    acrTemp=this.bacManager.selectAccRecurTemp(no_temp);
					    Datausulan planUtama=bacManager.selectMstProductTemp(no_temp);
					    ArrayList<Datarider> planRider=new ArrayList(bacManager.selectMstProductTempRider(no_temp));
					    ArrayList< PesertaPlus_mix>peserta_plus=new ArrayList(this.bacManager.selectPesertaTemp(no_temp));					    
						detiledit.setPemegang(pemegangTemp);
						detiledit.getPemegang().setFlag_upload(Integer.parseInt(flag_upload));
						detiledit.setNo_temp(no_temp);
						if(ttgTemp!=null){
							detiledit.setTertanggung(ttgTemp);
							//detiledit.getTertanggung().setMspe_place_birth("-");
							detiledit.getTertanggung().setMspe_no_identity_expired(elionsManager.selectSysdate());
						}
						Agen agen = new Agen();
					    agen = uwManager.select_detilagen2(detiledit.getPemegang().getMsag_id());
						detiledit.setAddressbilling(addrBillTemp);
						if(pemegangTemp!=null){
							detiledit.getPemegang().setTujuana("-");
							detiledit.getPemegang().setDanaa("-");
							detiledit.getPemegang().setKerjaa("-");
							detiledit.getPemegang().setIndustria("-");
							detiledit.getPemegang().setShasil("-");
							detiledit.getPemegang().setMspe_no_identity_expired(elionsManager.selectSysdate());
							
						}
						if(addrBillTemp!=null){
							if(detiledit.getAddressbilling().getMsap_address().equals(detiledit.getPemegang().getAlamat_rumah())){
								detiledit.getAddressbilling().setTagih("2");
							}else if(detiledit.getAddressbilling().getMsap_address().equals(detiledit.getPemegang().getAlamat_kantor())){
								detiledit.getAddressbilling().setTagih("3");
							}else{
								detiledit.getAddressbilling().setTagih("1");
							}
						}
						if(planUtama!=null){
							detiledit.setDatausulan(planUtama);
							detiledit.getDatausulan().setJenis_pemegang_polis(0);
						}
						if(!planRider.isEmpty()){
							int plan_rider=planRider.size();
							detiledit.getDatausulan().setJmlrider(plan_rider);
							detiledit.getDatausulan().setDaftaRider(planRider);
						}
						detiledit.setAccount_recur(acrTemp);						
						detiledit.getAccount_recur().setMar_acc_no_split(FormatString.splitWordToCharacter(detiledit.getAccount_recur().getMar_acc_no(),21));
						if(peserta_plus!=null){
							int jmlPsrta=peserta_plus.size();
							detiledit.getDatausulan().setJml_peserta(jmlPsrta);
							detiledit.getDatausulan().setDaftarplus(peserta_plus);
							detiledit.getDatausulan().setLstp_id(13);
						}
						if(agen!=null){
							detiledit.setAgen(agen);
						}
						
					}else if (flag_upload.equals("2")){//MANTA - Copy dari data proposal
						String no_temp = kopiSPAJ;
						Pemegang pemegangTemp = new Pemegang();
						AddressBilling addrBillTemp = new AddressBilling();
						Tertanggung ttgTemp = new Tertanggung();
						Account_recur acrTemp = new Account_recur();
						Map DataProposal = bacManager.selectDataMstProposal(no_temp);
						
						ArrayList<Datarider> datarider = new ArrayList<Datarider>();
						
					    HashMap DataProp = bacManager.selectMstDataProposal(no_temp);
					    HashMap PropProduk = bacManager.selectMstProposalProduct(no_temp);
					    ArrayList<HashMap> PropProdukRider = bacManager.selectMstProposalProductRider(no_temp);
					    ArrayList<HashMap> PropProdukPeserta = bacManager.selectMstProposalProductPeserta(no_temp);
					    ArrayList<HashMap> PropProdukTopUp = bacManager.selectMstProposalProductTopUp(no_temp);
					    
					    detiledit.getPemegang().setFlag_upload(Integer.parseInt(flag_upload));
					    detiledit.getPemegang().setId_proposal((String)DataProposal.get("NO_PROPOSAL"));
					    detiledit.getPemegang().setLead_id((String)DataProposal.get("LEAD_ID"));
					    
					    //Data PP
					    if(!Common.isEmpty(DataProp.get("SEX_PP"))){
					    	if (DataProp.get("SEX_PP").equals("1")){
						    	detiledit.getPemegang().setMspe_sex(1);
						    }else{
						    	detiledit.getPemegang().setMspe_sex(0);
						    }
					    }
					    BigDecimal umur_si_pp = Common.isEmpty(DataProp.get("UMUR_PP"))?null:(BigDecimal) DataProp.get("UMUR_PP");
					    detiledit.getPemegang().setMspe_date_birth(Common.isEmpty(DataProp.get("TGL_LAHIR_PP"))?null:(Date) DataProp.get("TGL_LAHIR_PP"));
					    detiledit.getPemegang().setMcl_first(Common.isEmpty(DataProp.get("NAMA_PP"))?null:(String) DataProp.get("NAMA_PP"));
					    detiledit.getPemegang().setMste_age(umur_si_pp.intValue());
					    
					    //Data TT
					    if(!Common.isEmpty(DataProp.get("SEX_TT"))){
					    	if (DataProp.get("SEX_TT").equals("1")){
					    		detiledit.getTertanggung().setMspe_sex(1);
						    }else{
						    	detiledit.getTertanggung().setMspe_sex(0);
						    }
					    }
					    BigDecimal umur_si_tt = Common.isEmpty(DataProp.get("UMUR_TT"))?null:(BigDecimal) DataProp.get("UMUR_TT");
					    detiledit.getTertanggung().setMspe_date_birth(Common.isEmpty(DataProp.get("TGL_LAHIR_TT"))?null:(Date) DataProp.get("TGL_LAHIR_TT"));
					    detiledit.getTertanggung().setMcl_first(Common.isEmpty(DataProp.get("NAMA_TT"))?null:(String) DataProp.get("NAMA_TT"));
					    detiledit.getTertanggung().setMste_age(umur_si_tt.intValue());
					    
					    //Data Usulan 
					    BigDecimal lsbs_id_props = Common.isEmpty(PropProduk.get("LSBS_ID"))?null:(BigDecimal) PropProduk.get("LSBS_ID");
					    BigDecimal lsdbs_id_props = Common.isEmpty(PropProduk.get("LSDBS_NUMBER"))?null:(BigDecimal) PropProduk.get("LSDBS_NUMBER");
					    BigDecimal lscb_id_props = Common.isEmpty(PropProduk.get("CARA_BAYAR"))?null:(BigDecimal) PropProduk.get("CARA_BAYAR");
					    BigDecimal up_props = Common.isEmpty(PropProduk.get("UP"))?null:(BigDecimal) PropProduk.get("UP");
					    BigDecimal premi_props = Common.isEmpty(PropProduk.get("PREMI"))?null:(BigDecimal) PropProduk.get("PREMI");
					    BigDecimal thn_cuti_props = Common.isEmpty(PropProduk.get("THN_CUTI_PREMI"))?null:(BigDecimal) PropProduk.get("THN_CUTI_PREMI");
					    BigDecimal thn_lamabyr_props = Common.isEmpty(PropProduk.get("THN_LAMA_BAYAR"))?null:(BigDecimal) PropProduk.get("THN_LAMA_BAYAR");
					    BigDecimal masa_kontrak_props = Common.isEmpty(PropProduk.get("THN_MASA_KONTRAK"))?null:(BigDecimal) PropProduk.get("THN_MASA_KONTRAK");
					    
					    if(lsbs_id_props!=null) detiledit.getDatausulan().setKodeproduk(lsbs_id_props.toString());
					    if(lsbs_id_props!=null) detiledit.getDatausulan().setLsbs_id(lsbs_id_props.intValue());
					    if(lsdbs_id_props!=null) detiledit.getDatausulan().setLsdbs_number(lsdbs_id_props.intValue());
					    if(lsbs_id_props!=null && lsdbs_id_props!=null) detiledit.getDatausulan().setPlan(lsbs_id_props.toString()+"~X"+lsdbs_id_props.toString());
					    detiledit.getDatausulan().setLku_id((String) PropProduk.get("LKU_ID"));
					    if(lscb_id_props!=null) detiledit.getDatausulan().setLscb_id(lscb_id_props.intValue());
					    if(up_props!=null) detiledit.getDatausulan().setMspr_tsi(up_props.doubleValue());
					    if(premi_props!=null) detiledit.getDatausulan().setMspr_premium(premi_props.doubleValue());
					    if(thn_cuti_props!=null) detiledit.getDatausulan().setMspo_installment(thn_cuti_props.intValue());
					    if(thn_lamabyr_props!=null) detiledit.getDatausulan().setMspo_pay_period(thn_lamabyr_props.intValue());
					    if(masa_kontrak_props!=null) detiledit.getDatausulan().setMspr_ins_period(masa_kontrak_props.intValue());
					    
//						if(!PropProdukRider.isEmpty()){
//							Integer jml_rider = PropProdukRider.size();
//							for(int i=0; i<jml_rider; i++){
//								datarider.add(i, new Datarider());
//							    BigDecimal lsbs_id_rider = Common.isEmpty(PropProdukRider.get(i).get("LSBS_ID"))?null:(BigDecimal) PropProdukRider.get(i).get("LSBS_ID");
//							    BigDecimal lsdbs_no_rider = Common.isEmpty(PropProdukRider.get(i).get("LSDBS_NUMBER"))?null:(BigDecimal) PropProdukRider.get(i).get("LSDBS_NUMBER");
//							    BigDecimal persen_up_rider = Common.isEmpty(PropProdukRider.get(i).get("PERSEN_UP"))?null:(BigDecimal) PropProdukRider.get(i).get("PERSEN_UP");
//							    
//							    if(lsbs_id_rider!=null){
//							    	datarider.get(i).setLsbs_id(lsbs_id_rider.intValue());
//							    	datarider.get(i).setPlan_rider1(lsbs_id_rider.toString());
//							    }
//							    if(lsdbs_no_rider!=null) datarider.get(i).setLsdbs_number(lsdbs_no_rider.intValue());
//							    if(lsbs_id_rider!=null && lsdbs_no_rider!=null) datarider.get(i).setPlan_rider(lsbs_id_rider.toString() + "~X" + lsdbs_no_rider.toString());
//							    if(persen_up_rider!=null) datarider.get(i).setPersenUp(persen_up_rider.intValue());
//							    datarider.get(i).setLku_id((String) PropProdukRider.get(i).get("LKU_ID"));
//							}
//							detiledit.getDatausulan().setJmlrider(jml_rider);
//							detiledit.getDatausulan().setDaftaRider(datarider);
//						}
					    
					    //Detil Agen
						Agen agen = new Agen();
					    agen = uwManager.select_detilagen2(DataProp.get("MSAG_ID").toString());
						if(agen!=null) detiledit.setAgen(agen);
						
						//Address Billing
						detiledit.setAddressbilling(addrBillTemp);
						if(pemegangTemp!=null){
							detiledit.getPemegang().setTujuana("-");
							detiledit.getPemegang().setDanaa("-");
							detiledit.getPemegang().setKerjaa("-");
							detiledit.getPemegang().setIndustria("-");
							detiledit.getPemegang().setShasil("-");
							detiledit.getPemegang().setMspe_no_identity_expired(elionsManager.selectSysdate());
						}
					
					}else if (flag_upload.equals("3")){//Copy dari data Gadget. Flag_upload dijadiin satu aja, value nya 3- RYAN
						String no_temp = kopiSPAJ;						
						Pemegang pemegangTemp = new Pemegang();
						Tertanggung ttgTemp = new Tertanggung();
						PembayarPremi pemPremi = new PembayarPremi();
						Datausulan planUtama = new Datausulan();
						ArrayList<Datarider> planRider = new ArrayList<Datarider>();
						AddressBilling addrBillTemp = new AddressBilling();
						Rekening_client rekTemp = new Rekening_client();
						Account_recur acrTemp = new Account_recur();
						ArrayList<PesertaPlus_mix> peserta_plus = new ArrayList<PesertaPlus_mix>();
						ArrayList<Benefeciary> benef = new ArrayList<Benefeciary>();
						Agen agen = new Agen();
						
						pemegangTemp = this.bacManager.selectppTemp(no_temp);
					    ttgTemp = this.bacManager.selectttgTemp(no_temp);
					    pemPremi = this.bacManager.selectPemPremiTemp(no_temp);
					    planUtama = bacManager.selectMstProductTemp(no_temp);
					    planRider = Common.serializableList(bacManager.selectMstProductTempRider(no_temp));
						addrBillTemp = this.bacManager.selectAddressBillingTemp(no_temp);
						rekTemp = this.bacManager.select_rek_client_temp(no_temp);
					    acrTemp = this.bacManager.selectAccRecurTemp(no_temp);
					    peserta_plus = Common.serializableList(this.bacManager.selectPesertaTemp(no_temp));
					    benef = Common.serializableList(this.bacManager.select_benef_temp(no_temp));
					    agen = uwManager.select_detilagen2(pemegangTemp.getMsag_id());
					    
					    /** flag penanda untuk disable or enable full autosales **/
					    if (flag_upload.equals("3")){
					    	planUtama.setEnable_full_autosales("4");
					    }
					    
					    // 20190201 Iga Ukiarwan - va dollar data usulan
					    //if (planUtama.getLku_id() != null){
					    	//if(planUtama.getLku_id().equals("02")){
						    	//planUtama.setKurs_p("02") ;
						    	//rekTemp.setMrc_kurs("02");
						    //}
					    //}
					    //planUtama.setMspo_installment(planUtama.getThn_cuti_premi());
					    if (planUtama.getLku_id() != null){
					    	if(planUtama.getLku_id().equals("02")){
					    		planUtama.setKurs_p("02") ;
					    		rekTemp.setMrc_kurs("02");
					    	}
					    }
					  
					    detiledit.setNo_temp(no_temp);
					    
					    if(pemegangTemp==null){
							detiledit.setPemegang(new Pemegang());
							detiledit.getPemegang().setFlag_upload(Integer.parseInt(flag_upload));
						}else{
							//Set data pekerjaan ayah & ibu jika PP pelajar
							if(pemegangTemp.getPekerjaan_ayah()!=null){
								String mklIdAyah = "0";
								mklIdAyah = bacManager.selectIdLstPekerjaan(pemegangTemp.getPekerjaan_ayah().toUpperCase());
								pemegangTemp.setPekerjaan_ayah(mklIdAyah);
							}
							//Set data pekerjaan ayah & ibu jika PP pelajar
							if(pemegangTemp.getPekerjaan_ibu()!=null){
								String mklIdIbu = "0";
								mklIdIbu = bacManager.selectIdLstPekerjaan(pemegangTemp.getPekerjaan_ibu().toUpperCase());
								pemegangTemp.setPekerjaan_ibu(mklIdIbu);
							}
							//Set data pekerjaan suami
							if(pemegangTemp.getPekerjaan_suami()!=null){
								String mklIdSuami = "0";
								mklIdSuami = bacManager.selectIdLstPekerjaan(pemegangTemp.getPekerjaan_suami().toUpperCase());
								pemegangTemp.setPekerjaan_suami(mklIdSuami);
							}
							
							detiledit.setPemegang(pemegangTemp);
							detiledit.getPemegang().setFlag_upload(Integer.parseInt(flag_upload));
							detiledit.getPemegang().setTujuana("-");
							detiledit.getPemegang().setDanaa("-");
							detiledit.getPemegang().setKerjaa("-");
							detiledit.getPemegang().setIndustria("-");
							detiledit.getPemegang().setShasil("-");
							detiledit.getPemegang().setPemegang_polis(this.bacManager.selectCountKeluarga(no_temp)+"");
							if(StringUtils.isEmpty(detiledit.getPemegang().getMcl_npwp())) detiledit.getPemegang().setMcl_npwp("0000");
						}
						
						if(ttgTemp!=null){
							detiledit.setTertanggung(ttgTemp);
						}
						
						if(pemPremi!=null){
							detiledit.setPembayarPremi(pemPremi);
						}

						if(agen !=null){
							detiledit.setAgen(agen);
							
							 /** untuk simpol tarikan dari autosales agen leader di kosongkan **/
							 boolean simpol = planUtama.getLsbs_id() == 120 && (planUtama.getLsdbs_number() >= 22 && planUtama.getLsdbs_number() <= 24);
							 if (simpol && planUtama.getLstp_id() == 14) {
								 detiledit.getPemegang().setMspo_leader(null);
							 }else {
								 detiledit.getPemegang().setMspo_leader(detiledit.getAgen().getMst_leader());
							 }
							 
							 /** retrieve autosales 212-14 ao di uncheck 37, 52, 60 **/
							 if (agen.getLca_id() != null){
								 if (agen.getLca_id().equals("37") ||  
									 agen.getLca_id().equals("52") ||
									 agen.getLca_id().equals("60"))  {
									 
									 if (planUtama.getLsbs_id() == 212 && planUtama.getLsdbs_number() == 14) {
										 detiledit.getPemegang().setMspo_ref_bii(null);
									 }
								 }
							 }
							 
						}
						
						if(planUtama!=null){
							Integer lstp_id = 14;
							if(planUtama.getLstp_id() != null || planUtama.getLstp_id() != 0){
								lstp_id = planUtama.getLstp_id();
							}
							
							detiledit.setDatausulan(planUtama);
							detiledit.getDatausulan().setJenis_pemegang_polis(0);
							
							//Mark Valentino 20181116 Revisi Kode Penutup Berdasarkan Region, LSBS_ID, LSDBS_ID
							HashMap spajTemp = bacManager.selectMstSpajTempAutoSales(no_temp);
							
							String lca_id = (String)spajTemp.get("LCA_ID");
							String lwk_id = (String)spajTemp.get("LWK_ID");
							String lsrg_id = (String)spajTemp.get("LSRG_ID");
							Integer lsbs_id = detiledit.getDatausulan().getLsbs_id();
							Integer lsdbs_number = detiledit.getDatausulan().getLsdbs_number();
							String campaign_id = (spajTemp.get("MCP_CAMPAIGN_ID") != null ? (String)spajTemp.get("MCP_CAMPAIGN_ID") : ""); //helpdesk [143180] autosales tambah campaign_id
							String camp_radio = (campaign_id.length() < 1 ? "0" : "1"); //helpdesk [143180] autosales tambah campaign_id
							
							//String kdPenutup = bacManager.selectKodePenutup(lca_id, lwk_id, lsrg_id, lsbs_id, lsdbs_number);							
							String kdPenutup = bacManager.selectKodePenutup(lsbs_id, lsdbs_number);
							
							if (kdPenutup == null){
								if( "094100".equals(detiledit.getAgen().getKode_regional()) ){
//									lstp_id = 23;
									detiledit.getAgen().setMsag_id("028220");
									detiledit.getPemegang().setMspo_leader("");
									if(!"".equals(detiledit.getPemegang().getMspo_ao())) detiledit.getPemegang().setMspo_ref_bii(1);
								}	
								
								/** produk simpol **/
								boolean simpol = planUtama.getLsbs_id() == 120 && (planUtama.getLsdbs_number() >= 22 && planUtama.getLsdbs_number() <= 24);
								
								/** hardcode agen simpol **/
								 if (simpol && planUtama.getLstp_id() == 14) {
									 detiledit.getAgen().setMsag_id("901327");
									 detiledit.getPemegang().setMspo_ao(pemegangTemp.getMspo_ao());
								 }
								 
							}else{
								detiledit.getAgen().setMsag_id(kdPenutup);
								detiledit.getPemegang().setMspo_leader("");
								if(!"".equals(detiledit.getPemegang().getMspo_ao())) detiledit.getPemegang().setMspo_ref_bii(1);								
							}
							
							detiledit.getDatausulan().setLstp_id(lstp_id);
							detiledit.getDatausulan().setTipeproduk(lstp_id);
							if(detiledit.getDatausulan().getCara_premi()==0){
								detiledit.getDatausulan().setCara_premi(1);
								
								/** simpol tarik dari autosales custom tarik premi pokok + topup berkala **/
								 if (planUtama.getLsbs_id() == 120 && (planUtama.getLsdbs_number() >= 22 && planUtama.getLsdbs_number() <= 24) ) {
									 detiledit.getDatausulan().setTotal_premi_kombinasi(bacManager.selectSumPremiPokokAndBerkalaTemp(no_temp));
									 Double premiTunggalTemp = bacManager.selectPremiTunggalTemp(no_temp);
									 if (premiTunggalTemp != null) {
										 detiledit.getDatausulan().setPremiTunggal(premiTunggalTemp);
									 } else {
										 detiledit.getDatausulan().setPremiTunggal(0.0);
									 }
								 } else {
									 detiledit.getDatausulan().setTotal_premi_kombinasi(bacManager.selectSumPremiTemp(no_temp));
								 }
								 
								detiledit.getDatausulan().setKombinasi(planUtama.getKombinasi().replaceAll(" ", ""));
							}else{
								detiledit.getDatausulan().setCara_premi(0);
							}
							
							if(camp_radio.equalsIgnoreCase("1")){ //helpdesk [143180] autosales tambah campaign_id
								List campaign_data = bacManager.selectMstCampaignProductAutoSales(campaign_id, lsbs_id.toString(), lsdbs_number.toString());
								if(campaign_data.size() > 0){
									detiledit.getDatausulan().setCampradio(camp_radio);
									detiledit.getDatausulan().setCampaign_id(campaign_id);
								}
							}
						}
						
						if(!planRider.isEmpty()){
							Integer plan_rider = planRider.size();
							detiledit.getDatausulan().setJmlrider(plan_rider);
							detiledit.getDatausulan().setDaftaRider(planRider);
						}
						
						if(addrBillTemp!=null){
							detiledit.setAddressbilling(addrBillTemp);
							if(addrBillTemp.getMsap_address().equals(detiledit.getPemegang().getAlamat_rumah())){
								detiledit.getAddressbilling().setTagih("2");
							}else if(detiledit.getAddressbilling().getMsap_address().equals(detiledit.getPemegang().getAlamat_kantor())){
								detiledit.getAddressbilling().setTagih("3");
							}else{
								detiledit.getAddressbilling().setTagih("1");
							}
						}
						
						if(rekTemp != null) {
							detiledit.setRekening_client(rekTemp);
							detiledit.getRekening_client().setMrc_no_ac_split(FormatString.splitWordToCharacter(detiledit.getRekening_client().getMrc_no_ac(),21));
						}
						
						if(acrTemp!=null){
							detiledit.setAccount_recur(acrTemp);						
							detiledit.getAccount_recur().setMar_acc_no_split(FormatString.splitWordToCharacter(detiledit.getAccount_recur().getMar_acc_no(),21));
						}
						
						if(peserta_plus!=null){
							Integer jmlPsrta = peserta_plus.size();
							detiledit.getDatausulan().setJml_peserta(jmlPsrta);
							detiledit.getDatausulan().setDaftarplus(peserta_plus);
							detiledit.getDatausulan().setLstp_id(13);
						}
						
						if(benef!=null){
							detiledit.getDatausulan().setDaftabenef(benef);
						}
						
						if(planUtama.getLsbs_id()==217 && planUtama.getLsdbs_number()== 2){
							detiledit.getInvestasiutama().setDaftarinvestasi(new ArrayList(this.bacManager.selectDaftarInvestasiTemp2(no_temp)));
						}else{
							detiledit.getInvestasiutama().setDaftarinvestasi(new ArrayList(this.bacManager.selectDaftarInvestasiTemp(no_temp)));
						}
						/*if(planUtama.getLsbs_id()==202){
							//detiledit.getInvestasiutama().setDaftarinvestasi(new ArrayList(this.elionsManager.selectDetailInvestasi(null)));
							detiledit.getInvestasiutama().setDaftarinvestasi(new ArrayList(this.bacManager.selectDaftarInvestasiTemp(no_temp)));
						}else{
							detiledit.getInvestasiutama().setDaftarinvestasi(new ArrayList(this.bacManager.selectDaftarInvestasiTemp(no_temp)));
						}*/
						//detiledit.getInvestasiutama().setDaftarinvestasi(new ArrayList(this.elionsManager.selectDetailInvestasi(null)));
						
						//detiledit.setInvestasiutama(invtemp);
						
						/*invtemp.setDaftarinvestasi(new ArrayList(this.elionsManager.selectDetailInvestasi(null)));
						invtemp.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));*/
						//detiledit.setInvestasiutama(invtemp);
						
						
					}else{				
						//harus punya hak akses untuk mengkopi data
						ArrayList<String> akses = currentUser.getAksesCabang();
						kopiSPAJ = elionsManager.selectGetSpaj(kopiSPAJ);
						String region = elionsManager.selectCabangFromSpaj_lar(kopiSPAJ);
						if (akses.contains(region) || currentUser.getLca_id().equals("01")) {
							//data pemegang
							Pemegang p = (Pemegang) this.elionsManager.selectpp(kopiSPAJ);
							if(p != null) {
								//p.setReg_spaj(null);
								p.setMspo_jenis_terbit(0);
								detiledit.setPemegang(p);
								detiledit.getDatausulan().setJenis_pemegang_polis(p.getMcl_jenis());
								detiledit.getDatausulan().setMspo_flag_sph(1);
								
								if(detiledit.getDatausulan().getJenis_pemegang_polis() == 1){
										jenis_pemegang_polis = detiledit.getPemegang().getMcl_jenis().toString();
										detiledit.setPersonal((Personal)this.elionsManager.selectProfilePerusahaan(detiledit.getPemegang().getMcl_id()));
										detiledit.getPersonal().setAddress(new AddressNew());
										detiledit.setContactPerson((ContactPerson)this.elionsManager.selectpic(detiledit.getPemegang().getMcl_id()));
										if(detiledit.getContactPerson() == null)detiledit.setContactPerson(new ContactPerson());
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
								}
								
								p.setReg_spaj(null);
							}
							// *Andhika - Data sumber pendanaan pembelian asuransi
							detiledit.getPemegang().setDaftarKyc(new ArrayList(uwManager.selectDaftar_kyc(kopiSPAJ)));
	
							// *Andhika - Data sumber penghasilan
							detiledit.getPemegang().setDaftarKyc2(new ArrayList(uwManager.selectDaftar_kyc2(kopiSPAJ)));
	
							//data addr billing
							AddressBilling a = (AddressBilling) this.elionsManager.selectAddressBilling(kopiSPAJ);
							if(a != null) {
								detiledit.setAddressbilling(a);
								a.setReg_spaj(null);
							}
							//data tertanggung
							Tertanggung t = (Tertanggung) this.elionsManager.selectttg(kopiSPAJ);
							if(t != null) {
								t.setReg_spaj(null);
								t.setMste_no_vacc("");
								detiledit.setTertanggung(t);
							}
							
							//data pembayarPremi
							PembayarPremi ppm = (PembayarPremi) this.bacManager.selectPembayarPremi(kopiSPAJ);
							if(ppm != null) {
								ppm.setReg_spaj(null);
								detiledit.setPembayarPremi(ppm);
							}
							
							String pPolis2 = ""+this.bacManager.selectJenisPemegangPolis(kopiSPAJ); 
							
							if (pPolis2.equals("1")){
								pPolis2 = "0";
							}else if(pPolis.equals("2")){
								pPolis2 ="1";
							}else {
								pPolis2 ="2";
							}
							
							detiledit.getPemegang().setPemegang_polis(pPolis2);
							
							String mklId = "0";
							mklId = bacManager.selectIdLstPekerjaan(detiledit.getPemegang().getMkl_kerja());
							detiledit.getPemegang().setMkl_kerja(mklId);
							
							//data rekening client
							Rekening_client rc = this.elionsManager.select_rek_client(kopiSPAJ);
							if(rc != null) {
								detiledit.setRekening_client(rc);
								detiledit.getRekening_client().setMrc_no_ac_split(FormatString.splitWordToCharacter(detiledit.getRekening_client().getMrc_no_ac(),21));
							}
							//data ahli waris
							detiledit.getDatausulan().setDaftabenef(new ArrayList(elionsManager.select_benef(kopiSPAJ)));
							detiledit.getTertanggung().setDaftarDth(new ArrayList(uwManager.select_dth(kopiSPAJ)));
							detiledit.setAgen(elionsManager.select_detilagen(kopiSPAJ));
							detiledit.setBroker(uwManager.select_detilbroker(kopiSPAJ));
							
							//data 
						}
						//data rekening client
						Rekening_client rc = this.elionsManager.select_rek_client(kopiSPAJ);
						if(rc != null) {
							detiledit.setRekening_client(rc);
							detiledit.getRekening_client().setMrc_no_ac_split(FormatString.splitWordToCharacter(detiledit.getRekening_client().getMrc_no_ac(),21));
						}
					}	
				}				
				//Yusuf - Apabila inputan bank sinarmas, set beberapa nilai default
				if(!currentUser.getCab_bank().equals("") && currentUser.getJn_bank().intValue() == 2) {
					//detiledit.getDatausulan().setLsbs_id(142); //lsbs : powersave 
					//detiledit.getDatausulan().setLsdbs_number(2); //lsdbs : powersave bank sinarmas
					//detiledit.getDatausulan().setTipeproduk(5); //tipe produk : powersave
					//detiledit.getAgen().setMsag_id("016409"); //kode penutup : bank sinarmas
					//req NOVIE : UNTUK BANK SINARMAS< DEFAULT KODE AGENTNYA KE 021052
//					detiledit.getDatausulan().setLsbs_id(164); //lsbs : simas stabil link 
//					detiledit.getDatausulan().setLsdbs_number(2); //lsdbs : stabil link bank sinarmas
//					detiledit.getDatausulan().setTipeproduk(5); //tipe produk : powersave
//					detiledit.getAgen().setMsag_id("021052"); //kode penutup : bank sinarmas
//					detiledit.getPemegang().setMspo_ao("021052");
					// *TAMBAHAN SIMAS POWER LINK
//					detiledit.getDatausulan().setLsbs_id(120); //lsbs : simas power link 
//					detiledit.getDatausulan().setLsdbs_number(10);//lsdbs : power link bank sinarmas
					detiledit.getDatausulan().setTipeproduk(5); //tipe produk : Power Save
					detiledit.getAgen().setMsag_id("901327"); //kode penutup : bank sinarmas
					detiledit.getPemegang().setMspo_ao("901327");
					ArrayList<HashMap> jabodetabek = new ArrayList(uwManager.selectListJabodetabekBSM());
					for(HashMap mapJabodetabek : jabodetabek){
						String lcb_no_jabodetabek = (String) mapJabodetabek.get("LCB_NO");
						if(currentUser.getCab_bank().contains(lcb_no_jabodetabek)){
							detiledit.getPemegang().setMspo_ao("021300");
						}
					}
					//detiledit.getPemegang().setMspo_ao("016409");
					if(currentUser.getMsag_id_ao() != null) {
						if(!currentUser.getMsag_id_ao().equals("")) {
							detiledit.getPemegang().setMspo_ao(currentUser.getMsag_id_ao());
						}
					}
				//lufi - Apabila inputan BSIM, set beberapa nilai default				
//				}else if (!currentUser.getCab_bank().equals("") && currentUser.getJn_bank().intValue() == 16){
//					if(detiledit.getDatausulan().getKodeproduk().equals("175"))	{				
//					   detiledit.getPemegang().setMspo_ao("901388");
//					}else{
//						detiledit.getPemegang().setMspo_ao("901389");
//					}
				//Yusuf - Apabila inputan BII, set beberapa nilai default
				}else if(!currentUser.getCab_bank().equals("") && (currentUser.getJn_bank().intValue() == 0 || currentUser.getJn_bank().intValue() == 1)) {
					detiledit.getDatausulan().setTipeproduk(7); //tipe produk : BII
				//Yusuf - Apabila inputan sinarmas sekuritas, set beberapa nilai default
				}else if(!currentUser.getCab_bank().equals("") && currentUser.getJn_bank().intValue() == 3) {
					detiledit.getDatausulan().setLsbs_id(142); //lsbs : powersave 
					detiledit.getDatausulan().setLsdbs_number(9); //lsdbs : danamas prima
					detiledit.getDatausulan().setTipeproduk(5); //tipe produk : powersave
					detiledit.getAgen().setMsag_id("016374"); //kode penutup : SIMAS SEKURITAS
					
					detiledit.getPemegang().setMspo_ao("016374");
					if(currentUser.getMsag_id_ao() != null) {
						if(!currentUser.getMsag_id_ao().equals("")) {
							detiledit.getPemegang().setMspo_ao(currentUser.getMsag_id_ao());
						}
					}
				}else if(currentUser.getLus_id().equals("2661")){
					detiledit.getPemegang().setMspo_ao(currentUser.getMsag_id_ao());
					detiledit.getAgen().setMsag_id(currentUser.getMsag_id_ao());
				}
				
				//Deddy(25 Sep 2009) - proses konversi dicek apakah kopiSpaj merupakan produk powersave
				String lsbs_id_kopi = uwManager.selectLsbsId(kopiSPAJ);
				String lsdbs_number_kopi = uwManager.selectLsdbsNumber(kopiSPAJ);
				boolean isPsaveToSlink = false;
				boolean isPlatinumSave = false;
				boolean isSlinkToPsave = false;
				Integer convertPsaveToSlink = 0;
				if(products.powerSave(lsbs_id_kopi)){
						isPlatinumSave = true;
						isPsaveToSlink = true;
						convertPsaveToSlink = 1;
				}
				
				if(!Common.isEmpty(topupslinkke)){
					isSlinkToPsave = true;
					detiledit.getDatausulan().setSlink(isSlinkToPsave);
				}
				
				//Yusuf (31 Aug 2009) - Bagian dari proses Konversi Psave ke Slink
				if(isPsaveToSlink || isPlatinumSave){
					Integer mssur_se = uwManager.selectCountMstSurrender(kopiSPAJ); //1: endors ke stable link; 2:endors ke stablesave; 3: endors ke powersave
					if(mssur_se!=null){
						detiledit.setDatausulan((Datausulan)this.elionsManager.selectDataUsulanutama(kopiSPAJ));
						detiledit.getDatausulan().setPlatinumSave(isPlatinumSave);
						detiledit.getDatausulan().setPsave(isPsaveToSlink);
						detiledit.getDatausulan().setKopiSPAJ(kopiSPAJ);
						detiledit.getDatausulan().setConvert(convertPsaveToSlink);
						detiledit.getDatausulan().setMssur_se(mssur_se);
						//Yusuf (31 Aug 2009) - Bagian dari proses Konversi Psave ke Slink
						//Apakah Spaj ini merupakan konversi produk Powersave / Simas Prima ke Stable Link / Simas Stabil Link?
						//detiledit.getDatausulan().setConvertPsaveToSlink(convertPsaveToSlink);
						
						if(mssur_se==1){
							if(currentUser.getJn_bank().intValue() == 2){
								detiledit.getDatausulan().setLsbs_id(164); //lsbs : slink 
								detiledit.getDatausulan().setLsdbs_number(11); //lsdbs : simas stabil link
								detiledit.getDatausulan().setTipeproduk(5); //tipe produk : powersave
								detiledit.getAgen().setMsag_id("021052"); //kode penutup : bank sinarmas
							}else{
								detiledit.getDatausulan().setLsbs_id(164); //lsbs : slink 
								detiledit.getDatausulan().setLsdbs_number(1); //lsdbs : slink
								detiledit.getDatausulan().setKodeproduk("164");
								detiledit.getDatausulan().setLsdbs_name("STABLE LINK(I)");
								detiledit.getDatausulan().setPlan("164~X1");
								
							}
							detiledit.getDatausulan().setCara_premi(1); //ISI TOTAL PREMI DAN PILIH KOMBINASI
							
						}else if(mssur_se==2){
							detiledit.getDatausulan().setLsbs_id(184); //lsbs : slink 
							detiledit.getDatausulan().setLsdbs_number(1); //lsdbs : slink
							detiledit.getDatausulan().setKodeproduk("184");
							detiledit.getDatausulan().setLsdbs_name("STABLE SAVE");
							detiledit.getDatausulan().setPlan("184~X1");
							detiledit.getDatausulan().setCara_premi(0); //ISI PREMI
						}else if(mssur_se==3){
							detiledit.getDatausulan().setLsbs_id(155); //lsbs : slink 
							detiledit.getDatausulan().setLsdbs_number(1); //lsdbs : slink
							detiledit.getDatausulan().setKodeproduk("155");
							detiledit.getDatausulan().setLsdbs_name("PLATINUM SAVE");
							detiledit.getDatausulan().setPlan("155~X1");
							detiledit.getDatausulan().setCara_premi(0); //ISI PREMI
						}
						
//						total premi = premi + bunga rollover terakhir
						Powersave roTerakhir = uwManager.selectRolloverPowersaveTerakhir(kopiSPAJ);
						if(roTerakhir!=null){
							if(roTerakhir.mpr_bayar_prm.intValue() == 1){
								detiledit.getDatausulan().setTotal_premi_kombinasi(roTerakhir.mpr_deposit);
							}else{
								detiledit.getDatausulan().setTotal_premi_kombinasi(roTerakhir.mpr_deposit + roTerakhir.mpr_interest);
							}
						}

						//data powersave
						Powersave data_pwr = (Powersave) this.elionsManager.select_powersaver(kopiSPAJ);
						if(data_pwr != null){
							if(data_pwr.getFlag_rider() != null){
								if(data_pwr.getFlag_rider()==1){//ada rider
									Map mappingRiderSave = uwManager.select_rider_save(spaj);
									BigDecimal mpr_cb_rider = (BigDecimal) mappingRiderSave.get("MRS_RIDER_CB");
									BigDecimal lscb_id_rider = (BigDecimal) mappingRiderSave.get("LSCB_ID_RIDER");
									data_pwr.setMpr_cara_bayar_rider(mpr_cb_rider.intValue());
									detiledit.getDatausulan().setLscb_id_rider(lscb_id_rider.intValue());
								}
							}
							detiledit.setPowersave(data_pwr);
							detiledit.getPowersave().setMps_rate(0.);
							detiledit.getPowersave().setMpr_note("");
							detiledit.getPowersave().setBegdate_topup(roTerakhir.mpr_mature_date);
							detiledit.getPowersave().setBegdateTerbaru(roTerakhir.mpr_mature_date);
							detiledit.getPowersave().setMsl_spaj_lama(kopiSPAJ);
						}
					}
				}else if(isSlinkToPsave){//Slink ke Psave tidak dilihat dari mst_surrender, krn hanya penawaran pemindahan dana dari slink ke psave dari masing2 pembayaran(utama/top up)
					detiledit.getPowersave().setMsl_spaj_lama(kopiSPAJ);
					detiledit.getDatausulan().setLsbs_id(188);
				}
				
				//DATA UNTUK TESTING
				
				//pemegang, tertanggung
				if(currentUser.getFlag_user_test() == null){
					currentUser.setFlag_user_test(1);
				}
				if(currentUser.getFlag_user_test()==1){
					detiledit.getPemegang().setMspo_nasabah_dcif("12345");
//					detiledit.getPemegang().setMcl_first("JON TOR");
					detiledit.getPemegang().setMspe_mother("MAMAK JONI");
					detiledit.getPemegang().setMspe_no_identity("09.1234.126578.9874");
//					detiledit.getPemegang().setMspe_date_birth(defaultDateFormat.parse("09/11/1981"));
					detiledit.getPemegang().setMspe_place_birth("JAKARTA");
					detiledit.getPemegang().setAlamat_rumah("GANG NAM BURUNG BEO");
					detiledit.getPemegang().setKota_rumah("BATAVIA");
					detiledit.getPemegang().setArea_code_rumah("021");
					detiledit.getPemegang().setTelpon_rumah("580");
					detiledit.getAddressbilling().setTagih("1");
					detiledit.getAddressbilling().setMsap_address(detiledit.getPemegang().getAlamat_rumah());
					detiledit.getAddressbilling().setKota(detiledit.getPemegang().getKota_rumah());
					detiledit.getAddressbilling().setKota_tgh(detiledit.getPemegang().getKota_rumah());
					detiledit.getPemegang().setTujuana("MU");
					detiledit.getPemegang().setDanaa("RB");
					detiledit.getPemegang().setDanaa2("M");
					detiledit.getPemegang().setIndustria("P");
					detiledit.getPemegang().setKerjaa("G");
					detiledit.getPemegang().setLsre_id(1);
				}
				//END OF DATA UNTUK TESTING
				
			}
			
			detiledit.getPemegang().setLus_id(Integer.parseInt(currentUser.getLus_id()));
			HashMap data_valid = Common.serializableMap((HashMap) this.elionsManager.select_validbank(Integer.parseInt(currentUser.getLus_id())));
			if (data_valid != null){
				detiledit.getPemegang().setCab_bank((String)data_valid.get("CAB_BANK"));
				detiledit.getPemegang().setJn_bank((Integer) data_valid.get("JN_BANK"));
				detiledit.getPemegang().setPassword1((String)data_valid.get("PASS1"));
				detiledit.getPemegang().setPassword2((String)data_valid.get("PASS2"));
			}else{
				detiledit.getPemegang().setCab_bank("");
				detiledit.getPemegang().setJn_bank(-1);
				detiledit.getPemegang().setPassword1("");
				detiledit.getPemegang().setPassword2("");
			}

			Integer jumlah_rate;
			Integer jumlah_rate_bulanan;
			Integer jumlah_rate_stabil;

			//APABILA BANK SINARMAS
			if (detiledit.getPemegang().getJn_bank().intValue() == 2){
				jumlah_rate = this.elionsManager.selectcount_rate(2); //simas prima
				//jumlah_rate_bulanan = this.elionsManager.selectcount_rate(5); //simas prima manfaat bulanan
				jumlah_rate_stabil = this.elionsManager.selectcount_rate(13); //simas stabil link
				
				//APABILA KOSONG, EMAIL!
				//Yusuf (18 Aug 2011) -> pindah ke bagian validator rate
//				if(jumlah_rate.intValue() == 0){// || jumlah_rate_bulanan == 0) {
//					DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
//					Date tgl = elionsManager.selectSysdate();
//					this.email.send(false, props.getProperty("admin.ajsjava"),
//							new String[] {"sandy@sinarmasmsiglife.co.id"}, 
//							null, null,
//							"[System Warning] Rate Powersave/Stable Link Per Tanggal " + df.format(tgl), 
//							"Kepada Yth.\n"+
//							"Bagian Investment di tempat.\n\n"+
//							"Mohon bantuannya untuk melakukan proses upload rate per tanggal " + df.format(tgl) +" untuk produk Simas Prima / Simas Stabil Link, karena ada Inputan SPAJ di Cabang Bank Sinarmas."+
//							"\n\nTerima kasih.", null);
//				}
				
				detiledit.getPemegang().setRate_1(jumlah_rate);
				//detiledit.getPemegang().setRate_2(jumlah_rate_bulanan);
			//APABILA LAINNYA
			}else {
				jumlah_rate = 0;
				jumlah_rate_bulanan = 0;
				detiledit.getPemegang().setRate_1(jumlah_rate);
				detiledit.getPemegang().setRate_2(jumlah_rate_bulanan);
			}
			
		}
		detiledit.setCurrentUser(currentUser);
		
		String pathFileTemp = props.getProperty("temp.fileinput.user")+"\\"+detiledit.getCurrentUser().getLus_id();
		if(currentUser.getLus_id().equals("2661")){
			pathFileTemp=props.getProperty("temp.fileinput.user")+"\\"+detiledit.getCurrentUser().getLus_id()+"\\"+detiledit.getCurrentUser().getMsag_id_ao();
		}
		detiledit.setPathFileTemp(pathFileTemp);
		
		File pathFile = new File(detiledit.getPathFileTemp());
		File file = new File(detiledit.getPathFileTemp()+"\\bac.txt");
		if(!pathFile.exists()) {
			pathFile.mkdirs();
        }
		String refreshObject = ServletRequestUtils.getStringParameter(request, "refreshObject", "");
		if(refreshObject.equals("")){
			refreshObject = "0";
		}
		
		String flagAwal = ServletRequestUtils.getStringParameter(request, "flagAwal", "0");
		
		
		if(file.exists() && !refreshObject.equals("2") && flagAwal.equals("1")){
			Cmdeditbac tempdetiledit = detiledit;
			Object cmd = null;
			cmd = F_CopyObjectToFile.unserializeable(cmd, detiledit.getPathFileTemp()+"\\bac.txt");
			if(refreshObject.equals("0")){
				refreshObject="2";
			}
			flagAwal="1";		
			detiledit= (Cmdeditbac)cmd;
			
			if(detiledit!=null){				
				detiledit.setFileConfirm("Perhatian Baru, Sebelumnya pernah dilakukan penginputan untuk nasabah " +detiledit.getPemegang().getMcl_first()+ ". Apakah Anda ingin meneruskan penginputannya? tekan OK apabila mau meneruskan, CANCEL apabila menginput data baru");
				detiledit.setFlag_special_case("0");
				detiledit.setRefreshObject(Integer.parseInt(refreshObject));
				if(detiledit.getRefreshObject()==1){//1 refresh object
					file.delete();
					detiledit.setFileConfirm("");
					detiledit = tempdetiledit;
//					detiledit = null;
				}
			}else{
				ArrayList<File> attachFile = new ArrayList<File>();
				attachFile.add(file);				
				if(file.exists()){
					email.send(true, "ajsjava@sinarmasmsiglife.co.id", new String[] {"deddy@sinarmasmsiglife.co.id","ryan@sinarmasmsiglife.co.id"}, null, null, "ADA ERROR SERIALIZABLE", 
							"Telah Terjadi Error pada saat dilakukan Serializable untuk user ID"+ currentUser.getLus_id() +"dengan Nama Lengkap User " +currentUser.getLus_full_name(), attachFile);
					file.delete();
					detiledit = new Cmdeditbac();
					detiledit.setPemegang(new Pemegang());
					detiledit.setPembayarPremi(new PembayarPremi());
					detiledit.setAddressbilling(new AddressBilling());
					detiledit.setDatausulan(new Datausulan());
					detiledit.setTertanggung(new Tertanggung());
					detiledit.setFlag_special_case("0");
					InvestasiUtama inv = new InvestasiUtama();
					detiledit.setPersonal(new Personal());
					detiledit.setContactPerson(new ContactPerson());
					detiledit.getPersonal().setAddress(new AddressNew());
					inv.setDaftarinvestasi(new ArrayList(this.elionsManager.selectDetailInvestasi(null)));
					inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
					detiledit.setInvestasiutama(inv);
					inv.setDaftartopup(new DetilTopUp());
					detiledit.setRekening_client(new Rekening_client());
					detiledit.setAccount_recur(new Account_recur());
					detiledit.setPowersave(new Powersave());
					detiledit.getPowersave().setFee_based_income(elionsManager.selectMst_default_numeric(31));
					detiledit.setAgen(new Agen());
					detiledit.setHistory(new History());
					detiledit.setEmployee(new Employee());
					detiledit.setCurrentUser(currentUser);
					detiledit.setBroker(new Broker());
					if(jenis_pemegang_polis != null){
						try{
							//Map<String, Collection> map = new HashMap<String, Collection>();
							//map.put("gelar", elionsManager.selectGelar(1));
							//map.put("jenisUsaha", elionsManager.selectJenisUsaha());
							request.setAttribute("gelar", Common.serializableList(elionsManager.selectGelar(1)));
							ArrayList<DropDown> bidangUsahaList = (Common.serializableList((List<DropDown>)elionsManager.selectBidangUsaha(2)));
							bidangUsahaList.addAll(Common.serializableList((List<DropDown>)elionsManager.selectBidangUsaha(1)));
							request.setAttribute("bidangUsaha", bidangUsahaList);
							detiledit.getDatausulan().setJenis_pemegang_polis(Integer.parseInt(jenis_pemegang_polis));
						}catch(Exception e){
							detiledit.getDatausulan().setJenis_pemegang_polis(0);
						}
					}else{
						detiledit.getDatausulan().setJenis_pemegang_polis(0);
					}
					if(!currentUser.getCab_bank().equals("") && currentUser.getJn_bank().intValue() == 2) {
						if(currentUser.getMsag_id_ao() != null) {
							if(!currentUser.getMsag_id_ao().equals("")) {
								detiledit.getPemegang().setMspo_ao(currentUser.getMsag_id_ao());
							}
						}

					//Yusuf - Apabila inputan BII, set beberapa nilai default
					}else if(!currentUser.getCab_bank().equals("") && (currentUser.getJn_bank().intValue() == 0 || currentUser.getJn_bank().intValue() == 1)) {
						detiledit.getDatausulan().setTipeproduk(7); //tipe produk : BII
					//Yusuf - Apabila inputan sinarmas sekuritas, set beberapa nilai default
					}else if(!currentUser.getCab_bank().equals("") && currentUser.getJn_bank().intValue() == 3) {
						detiledit.getDatausulan().setLsbs_id(142); //lsbs : powersave 
						detiledit.getDatausulan().setLsdbs_number(9); //lsdbs : danamas prima
						detiledit.getDatausulan().setTipeproduk(5); //tipe produk : powersave
						detiledit.getAgen().setMsag_id("016374"); //kode penutup : SIMAS SEKURITAS
						
						detiledit.getPemegang().setMspo_ao("016374");
						if(currentUser.getMsag_id_ao() != null) {
							if(!currentUser.getMsag_id_ao().equals("")) {
								detiledit.getPemegang().setMspo_ao(currentUser.getMsag_id_ao());
							}
						}
					}else if(currentUser.getLus_id().equals("2661")){
						detiledit.getPemegang().setMspo_ao(currentUser.getMsag_id_ao());
						detiledit.getAgen().setMsag_id(currentUser.getMsag_id_ao());
					}
				}
			}
		}
		
		//referensi(tambang emas) agar jenis_ref tidak null
		detiledit.getAgen().setJenis_ref(2);
		
		//hadiah
		ArrayList<Hadiah> hd = Common.serializableList(uwManager.selectHadiah(detiledit.getPemegang().getReg_spaj()));
		detiledit.getPemegang().setDaftar_hadiah(hd);
		
		Integer jml_hadiah = detiledit.getPemegang().getDaftar_hadiah().size();
		if(jml_hadiah==null)jml_hadiah=0;
		detiledit.getPemegang().setUnit(jml_hadiah);
		
		detiledit.getPemegang().setFlag_standard(0);
		//detiledit.getPemegang().setMspo_flag_spaj(2);
		
		return detiledit;
	}

	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		
		logger.debug("EditBacController : onBind");
		
		NumberFormat nf = NumberFormat.getNumberInstance(); 
		f_replace konteks = new f_replace();
		int halaman = ServletRequestUtils.getIntParameter(request, "hal", 1);
		
		request.getSession().setAttribute("dataInputSpaj", (Cmdeditbac) cmd);
		
		// TAMBAHAN UNTUK PERUSAHAAN
		if (halaman==0){
			Cmdeditbac a = (Cmdeditbac) cmd;
			if (a.getDatausulan().getJmlrider() == null) {
				a.getDatausulan().setJmlrider(0);
			}
			
			int jenis_pemegang_polis = a.getDatausulan().getJenis_pemegang_polis();
			if(jenis_pemegang_polis == 0){
		//	(Andhika) *Sumber Pendanaan pembelian asuransi tambahan
			ArrayList listkyc = a.getPemegang().getDaftarKyc();
			int juml_kyc = request.getParameter("jmlDaftarKyc") == null ? 0 : Integer.parseInt(request.getParameter("jmlDaftarKyc"));

			String kyc_desc1;
			String kyc_desc_x;
			String kyc_id;
			String kyc_pp;
			
			ArrayList<SumberKyc> b = new ArrayList<SumberKyc>();
			
			a.getPemegang().setJmlkyc(juml_kyc);
		
				if (juml_kyc>0){
						for (int k = 1; k <= juml_kyc; k++){
							kyc_desc1 = request.getParameter("listkyc.kyc_desc1"+k);
							kyc_desc_x = request.getParameter("listkyc.kyc_desc_x"+k);
							kyc_id = request.getParameter("listkyc.kyc_id"+k);
							kyc_pp = request.getParameter("listkyc.kyc_pp"+k);
							if (kyc_desc1.equalsIgnoreCase("LAINNYA")){
								kyc_desc1="";
							}
							if(kyc_pp==null){
								kyc_pp="0";
							}
							if (kyc_id==null){
								kyc_id="1"; //sumber pendanaan
							}
						SumberKyc skc = new SumberKyc();
						skc.setKyc_desc1(kyc_desc1);
						skc.setKyc_desc_x(kyc_desc_x);
						skc.setKyc_id(Integer.parseInt(kyc_id));
						skc.setKyc_pp(Integer.parseInt(kyc_pp));
							
						b.add(skc);
					}
				}
				a.getPemegang().setDaftarKyc(b);
		
				
		//	(Andhika) *Sumber penghasilan tambahan
			ArrayList listkyc2 = a.getPemegang().getDaftarKyc2();
			int juml_kyc2 = request.getParameter("jmlDaftarKyc2") == null ? 0 : Integer.parseInt(request.getParameter("jmlDaftarKyc2"));		
			String kyc_desc2;
			String kyc_desc2_x;
			String kyc_id2;
			String kyc_pp2;
				
			ArrayList<SumberKyc> b2 = new ArrayList<SumberKyc>();
				
				a.getPemegang().setJmlkyc2(juml_kyc2);
					
					if (juml_kyc2>0){
						for (int k = 1 ;k <= juml_kyc2; k++){
							kyc_desc2 = request.getParameter("listkyc2.kyc_desc2"+k);
							kyc_desc2_x = request.getParameter("listkyc2.kyc_desc2_x"+k);
							kyc_id2 = request.getParameter("listkyc2.kyc_id2"+k);
							kyc_pp2 = request.getParameter("listkyc2.kyc_pp2"+k);
								if (kyc_desc2=="LAINNYA"){
									kyc_desc2="";
								}
								if (kyc_desc2_x==null){
									kyc_desc2_x="";
								}
								if(kyc_pp2==null){
									kyc_pp2="0";
								}
								if (kyc_id2==null){
									kyc_id2="2"; //sumber penghasilan
								}
							SumberKyc skc2 = new SumberKyc();
							skc2.setKyc_desc2(kyc_desc2);
							skc2.setKyc_desc2_x(kyc_desc2_x);
							skc2.setKyc_id(Integer.parseInt(kyc_id2));
							skc2.setKyc_pp(Integer.parseInt(kyc_pp2));
							
							b2.add(skc2);
						}
					}
			a.getPemegang().setDaftarKyc2(b2);
			}
			// set isi jenis pemegang polis (INDIVIDU/PERUSAHAAN)
			if(jenis_pemegang_polis == 1){
				try{
					//Map<String, Collection> map = new HashMap<String, Collection>();
					//map.put("gelar", elionsManager.selectGelar(1));
					//map.put("jenisUsaha", elionsManager.selectJenisUsaha());
					request.setAttribute("gelar", Common.serializableList(elionsManager.selectGelar(1)));
					ArrayList<DropDown> bidangUsahaList = (Common.serializableList((List<DropDown>)elionsManager.selectBidangUsaha(2)));
					bidangUsahaList.addAll(Common.serializableList((List<DropDown>)elionsManager.selectBidangUsaha(1)));
					request.setAttribute("bidangUsaha", bidangUsahaList);
					a.getDatausulan().setJenis_pemegang_polis(1);
				}catch(Exception e){
					a.getDatausulan().setJenis_pemegang_polis(0);
				}
			}else{
				a.getDatausulan().setJenis_pemegang_polis(0);
			}
			
		}
		//==================================
		
		/** HALAMAN PEMEGANG */
		if (halaman==1){
			Cmdeditbac a = (Cmdeditbac) cmd;
		
			if (a.getDatausulan().getJmlrider() == null) {
				a.getDatausulan().setJmlrider(0);
			}
			if (a.getDatausulan().getJml_peserta()==null){
				a.getDatausulan().setJml_peserta(0);
			}
			// TAMBAHAN UNTUK PERUSAHAAN
				// set isi jenis pemegang polis (INDIVIDU/PERUSAHAAN)
				int jenis_pemegang_polis = a.getDatausulan().getJenis_pemegang_polis();
				if(jenis_pemegang_polis == 1){
					try{
						//Map<String, Collection> map = new HashMap<String, Collection>();
						//map.put("gelar", elionsManager.selectGelar(1));
						//map.put("jenisUsaha", elionsManager.selectJenisUsaha());
						request.setAttribute("gelar", Common.serializableList(elionsManager.selectGelar(1)));
						ArrayList<DropDown> bidangUsahaList = (Common.serializableList((List<DropDown>)elionsManager.selectBidangUsaha(2)));
						bidangUsahaList.addAll(Common.serializableList((List<DropDown>)elionsManager.selectBidangUsaha(1)));
						request.setAttribute("bidangUsaha", bidangUsahaList);
						a.getDatausulan().setJenis_pemegang_polis(1);
					}catch(Exception e){
						a.getDatausulan().setJenis_pemegang_polis(0);
					}
				}else{
					a.getDatausulan().setJenis_pemegang_polis(0);
				}
			//==================================
		
		/*HALAMAN PEMBAYAR PREMI*/
		}else if(halaman==2){
			Cmdeditbac a = (Cmdeditbac) cmd;
			a.getPemegang().setLsre_id_premi(Integer.parseInt(a.getPembayarPremi().getLsre_id_premi()));
			
		/** HALAMAN TERTANGGUNG */
		}else if (halaman==3){
			// *for data rider
			int jmlh_rider = Integer.parseInt(request.getParameter("mn"));
			
			String kode_rider;
			Integer kd_rd;
			String number_rider;
			Integer nm_rd = 0;
			String unit ;
			Integer unt = 0;
			String klas;
			Integer kls = 0;
			String tsi;
			Double ts = 0.;
			String premi;
			Double prm = 0.;
			String pUp;
			Integer fp = 0;
			Integer in = 0;
			String ins;
			String beg_date;
			String end_date;
			String end_pay;
			String rate;
			Double rt = 0.;
			String insured;
			Integer isr = 0;
			String bisnis;
			String basic;
			String namaRiderNya;
			String spouse;
			String child1;
			String child2;
			String child3;
			String child4;
			Integer juml_rid_kes = 0;
			Integer countbasic = null;
			Integer cekBasic = null;
			Integer jenis = null;
			int letak = 0;
			Integer spouseChild = 0;
			Integer flag_jenis_peserta=null;
			Integer flag_Prov_ttg1 = 0, flag_Prov_ttg2 = 0 , flag_Prov_ttg3 = 0 , flag_Prov_ttg4 = 0, flag_Prov_ttg5 = 0;
			Integer cek_PT_ttg1=0 ,  cek_PT_ttg2=0 ,  cek_PT_ttg3=0,  cek_PT_ttg4=0,  cek_PT_ttg5=0;
			Integer cek_PT_ttg1RG=0 ,cek_PT_ttg1RB=0,cek_PT_ttg1Pk=0,   
					cek_PT_ttg2RIJ=0 ,cek_PT_ttg2RG=0 ,cek_PT_ttg2RB=0,cek_PT_ttg2Pk=0, 
					cek_PT_ttg3RIJ=0 ,cek_PT_ttg3RG=0 ,cek_PT_ttg3RB=0,cek_PT_ttg3Pk=0,
					cek_PT_ttg4RIJ=0 ,cek_PT_ttg4RG=0 ,cek_PT_ttg4RB=0,cek_PT_ttg4Pk=0,
					cek_PT_ttg5RIJ=0 ,cek_PT_ttg5RG=0 ,cek_PT_ttg5RB=0,cek_PT_ttg5Pk=0;
			Integer juml_rid_prov = 0;
			
					
			ArrayList<Simas> c = new ArrayList<Simas>();
			ArrayList<Datarider> b = new ArrayList<Datarider>();
			ArrayList daftarPesertaMedPlus=new ArrayList();
			ArrayList daftarRiderPlusTtg1=new ArrayList();
			ArrayList daftarMedPlusTtg1=new ArrayList();
			ArrayList daftarRiderPlusTtg2=new ArrayList();
			ArrayList daftarMedPlusTtg2=new ArrayList();
			ArrayList daftarRiderPlusTtg3=new ArrayList();
			ArrayList daftarMedPlusTtg3=new ArrayList();
			ArrayList daftarRiderPlusTtg4=new ArrayList();
			ArrayList daftarMedPlusTtg4=new ArrayList();
			ArrayList daftarRiderPlusTtg5=new ArrayList();
			ArrayList daftarMedPlusTtg5=new ArrayList();	
			ArrayList daftarRiderPlusTtg6=new ArrayList();			
			ArrayList daftarMedPlusTtg6=new ArrayList();
			ArrayList daftarRiderPlusTtg=new ArrayList();
			Integer jmlMedPlusTtg1=0,jmlMedPlusTtg2=0,jmlMedPlusTtg3=0,jmlMedPlusTtg4=0,
					jmlMedPlusTtg5=0,jmlMedPlusTtg6=0,jmlallMedPlus=0;
			Integer jmlRiderMedPlusTtg1=0,jmlRiderMedPlusTtg2=0,jmlRiderMedPlusTtg3=0,
					jmlRiderMedPlusTtg4=0,jmlRiderMedPlusTtg5=0,jmlRiderMedPlusTtg6=0,jmlallRiderMedPlus=0;
			
			ArrayList daftarPesertaProvestara=new ArrayList();
			ArrayList daftarRiderProvestaraTtg1=new ArrayList();
			ArrayList daftarProvestaraTtg1=new ArrayList();
			ArrayList daftarRiderProvestaraTtg2=new ArrayList();
			ArrayList daftarProvestaraTtg2=new ArrayList();
			ArrayList daftarRiderProvestaraTtg3=new ArrayList();
			ArrayList daftarProvestaraTtg3=new ArrayList();
			ArrayList daftarRiderProvestaraTtg4=new ArrayList();
			ArrayList daftarProvestaraTtg4=new ArrayList();
			ArrayList daftarRiderProvestaraTtg5=new ArrayList();
			ArrayList daftarProvestaraTtg5=new ArrayList();	
			ArrayList daftarRiderProvestaraTtg6=new ArrayList();			
			ArrayList daftarProvestaraTtg6=new ArrayList();
			ArrayList daftarRiderProvestaraTtg=new ArrayList();
			Integer jmlProvestaraTtg1=0,jmlProvestaraTtg2=0,jmlProvestaraTtg3=0,jmlProvestaraTtg4=0,
					jmlProvestaraTtg5=0,jmlProvestaraTtg6=0,jmlallProvestara=0;
			Integer jmlRiderProvestaraTtg1=0,jmlRiderProvestaraTtg2=0,jmlRiderProvestaraTtg3=0,
					jmlRiderProvestaraTtg4=0,jmlRiderProvestaraTtg5=0,jmlRiderProvestaraTtg6=0,jmlallRiderProvestara=0;
			
			ArrayList<PesertaPlus_mix> pp_mix = new ArrayList<PesertaPlus_mix>();
			ArrayList<PesertaPlus> pp = new ArrayList<PesertaPlus>();
			Cmdeditbac a = (Cmdeditbac) cmd;
			a.getDatausulan().setJmlrider(jmlh_rider);
						
			ArrayList testt = a.getDatausulan().getDaftaRider(); // *Banyaknya Rider yang diambil
			
			if (jmlh_rider>0){				
				for (int k = 1 ;k <= jmlh_rider; k++)	{
					bisnis = request.getParameter("ride.plan_riderx"+k);
					if (bisnis == null){
						bisnis="0~X0";
					}	
					letak = 0;
					letak = bisnis.indexOf("X");
					//randy - error: index String index out of range: -2
					if(letak < 0){
						errors.reject("","HALAMAN DATA USULAN : Mohon dipastikan penginputan produk Ridernya.");
						return;
					}
					kode_rider = bisnis.substring(0,letak-1);
					kd_rd = Integer.parseInt(kode_rider);
					number_rider = bisnis.substring(letak+1,bisnis.length());
					nm_rd = Integer.parseInt(number_rider);
					unit = request.getParameter("ride.mspr_unit"+k);
					
					//Adrian: Iwan Surya(BAS) case rider ESCI Polis
					//request.setAttribute("ride.mspr_persen", a.getDatausulan().getDaftaRider().get(k)
					Datarider drr = new Datarider();
					if(kd_rd != null){
					if(kd_rd==837 || kd_rd==813){
					//	drr = (Datarider)testt.get(k-1);
					//	Integer x_lsbsId = drr.getPersenUp();
					}}
					if (unit == null){
						unit ="";
					}
					if (unit.trim().length()==0){
						unit="0";
					}else{
						boolean cekk1= f_validasi.f_validasi_numerik(unit);	
						if (cekk1==false){
							unit = "0";
						}
					}
					unt = nf.parse(unit).intValue();
					klas = request.getParameter("ride.mspr_class"+k);
					if (klas == null){
						klas ="";
					}
					if (klas.trim().length()==0){
						klas="0";
					}else{
						boolean cekk1 = f_validasi.f_validasi_numerik(klas);
						if (cekk1==false){
							klas="0";
						}
					}
					kls = nf.parse(klas).intValue();
					tsi = request.getParameter("ride.mspr_tsi"+k);
					if (tsi == null){
						tsi ="";
					}
					if (tsi.trim().length()==0){
						tsi="0";
					}
					
					/** tambah kondisi untuk replace . menjadi , karena format number tidak bisa jika menggunakan . 
					 * jadi harus ke format , untuk ribuanya **/
					if (tsi.trim().length()>0){
						String tsiTemp = tsi.replace('.',',');
						tsi = tsiTemp;
					}
					ts = nf.parse(tsi).doubleValue();
					premi = request.getParameter("ride.mspr_premium"+k);
					if (premi == null){
						premi = "";
					}
					if (premi.trim().length()==0){
						premi= "0";
					}
					prm = nf.parse(premi).doubleValue();
					
					pUp = request.getParameter("ride.persenUpx"+k);
					if(kd_rd != null){
					if(kd_rd==837 || kd_rd==813){
						if(pUp != null ){
							if(!pUp.equals("")){
						Integer up = Integer.valueOf(pUp);
						//Adrian: Iwan Surya case Rider ESCI Polis : 37217201800304
						drr.setPersenUp(up);
						drr.setPersenUpx(up);
						drr.setMspr_persen(up);
						//a.getDatausulan().getDaftaRider().get(k-1)
						//Cmdeditbac a = (Cmdeditbac) cmd;	
						}}
					}}
					if (pUp == null){
						pUp = "";
					}
					if (pUp.trim().length()==0){
						pUp= "0";
					}
					fp = nf.parse(pUp).intValue();
					
					ins = request.getParameter("ride.mspr_ins_period"+k);
					if (ins == null){
						ins = "";
					}
					if (ins.trim().length()==0){
						ins="0";
					}
					in = nf.parse(ins).intValue();
					beg_date = request.getParameter("mspr_beg_date"+k);
					Date begin_date;	
					if (beg_date == null){
						beg_date ="";
					}
					if (beg_date.trim().length()==0) {
						begin_date=null;
					}else{
						begin_date=defaultDateFormat.parse(beg_date);
					}
					end_date = request.getParameter("mspr_end_date"+k);
					Date ending_date;
					if (end_date == null){
						end_date = "";
					}
					if (end_date.trim().length()==0){
						ending_date=null;
					}else{
						ending_date=defaultDateFormat.parse(end_date);
					}
					end_pay = request.getParameter("mspr_end_pay"+k);
					Date ending_pay;
					if (end_pay == null){
						end_pay = "";
					}
					if (end_pay.trim().length()==0){
						ending_pay=null;
					}else{
						ending_pay=defaultDateFormat.parse(end_pay);
					}

					rate = request.getParameter("ride.mspr_rate"+k);
					if (rate == null){
						rate = "";
					}
					if (rate.trim().length()==0){
						rate="0";
					}
					rt = nf.parse(rate).doubleValue();
					
					insured = request.getParameter("ride.mspr_tt1"+k);
					if (insured == null)	{
						insured = "";
					}
					if (insured.trim().length()==0){
						insured="0";
					}
					isr = nf.parse(insured).intValue();
					
					String flag_include= "0";
					flag_include = request.getParameter("ride.flag_include"+k);
					if (flag_include == null){
						flag_include ="";
					}
					if (flag_include == null){
						flag_include = "0";
					}
					if (cekBasic == null){
						cekBasic = 0;
					}
					if (countbasic == null){
						countbasic = 0;
					}
					if(flag_jenis_peserta == null){
						flag_jenis_peserta = 0;
					}
					// *FIX ME
					namaRiderNya = uwManager.selectNamaRiderNya(kd_rd, nm_rd);
					
					basic = uwManager.selectBasicName(kd_rd, nm_rd);
					if(basic != null){
						jenis = 0;
						countbasic = countbasic+1;
					}
					spouse = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG II)");
					if(spouse != null){
						jenis = 1;
					}
					child1 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG III)");
					if(child1 != null){
						jenis = 2;
					}
					child2 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG IV)");
					if(child2 != null){
						jenis = 3;
					}
					child3 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG V)");
					if(child3 != null){
						jenis = 4;
					}
					child4 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG VI)");
					if(child4 != null){
						jenis = 5;
					}				
					// cek produk kesehatan
					if("819,820,823,825,826,831,832,833,836,838,840,841,842,843,848".indexOf(kd_rd.toString())>-1){
						juml_rid_kes = juml_rid_kes+1;
						if(spouse == null && child1 == null && child2 == null && child3 == null && child4 == null){
							jenis = 0;}
					}else{
						jenis = 10;
					}
					
					// Cek Provestara - Ridhaal					
					if("840,841,842,843".indexOf(kd_rd.toString())>-1){
						if(nm_rd <= 2 ){
							juml_rid_prov = juml_rid_prov+1;
							flag_Prov_ttg1 = flag_Prov_ttg1 + 1;
						}else if((nm_rd >= 3 && nm_rd <= 4)){
							juml_rid_prov = juml_rid_prov+1;
							flag_Prov_ttg2 = flag_Prov_ttg2 + 1;
						}else if((nm_rd >= 5 && nm_rd <= 6)){
							juml_rid_prov = juml_rid_prov+1;
							flag_Prov_ttg3 = flag_Prov_ttg3 + 1;
						}else if((nm_rd >= 7 && nm_rd <= 8)){
							juml_rid_prov = juml_rid_prov+1;
							flag_Prov_ttg4 = flag_Prov_ttg4 + 1;
						}else if((nm_rd >= 9 && nm_rd <= 10)){
							juml_rid_prov = juml_rid_prov+1;
							flag_Prov_ttg5 = flag_Prov_ttg5 + 1;
						}						
										
					}
					
					if((countbasic > 0) && (spouse == null && child1 == null && child2 == null && child3 == null && child4 == null)){
						cekBasic = 1;
					}
					if(jenis != 0 && jenis !=10){
						spouseChild = spouseChild+1;
					}
					Datarider rd = new Datarider();
					rd.setLsbs_id(kd_rd);
					rd.setLsdbs_number(nm_rd);
					rd.setPlan_rider(bisnis);
					rd.setMspr_unit(unt);
					rd.setMspr_class(kls);
					rd.setMspr_ins_period(in);
					rd.setMspr_tt(isr);
					rd.setMspr_rate(rt);
					rd.setMspr_beg_date(begin_date);
					rd.setMspr_end_date(ending_date);
					rd.setMspr_end_pay(ending_pay);
					rd.setMspr_tsi(ts);
					rd.setMspr_premium(prm);
					rd.setPersenUp(fp);
					rd.setLsdbs_name(basic);
					//rd.setFlag_include(Integer.parseInt(flag_include));
					rd.setSpouseName(spouse);
					rd.setChildName(child1);
					rd.setJenis(jenis);
					rd.setCountbasic(countbasic);
					rd.setNamaRiderNya(namaRiderNya);
					if(rd.getLsbs_id()==838){
						if(products.medicalPlusSilver(rd.getLsdbs_number().toString())){
							rd.setFlag_jenis_Paketmedplus(2);//1:bronze 2:silver 3:gold 4:platinum
						}else if(products.medicalPlusGold(rd.getLsdbs_number().toString())){
							rd.setFlag_jenis_Paketmedplus(3);//1:bronze 2:silver 3:gold 4:platinum
						}else if(products.medicalPlusPlatinum(rd.getLsdbs_number().toString())){
							rd.setFlag_jenis_Paketmedplus(3);//1:bronze 2:silver 3:gold 4:platinum
						}
						
						if(rd.getJenis()==0){
							daftarMedPlusTtg1.add(rd);
							jmlMedPlusTtg1=daftarMedPlusTtg1.size();
						}else if(rd.getJenis()==1){
							daftarMedPlusTtg2.add(rd);
							jmlMedPlusTtg2=daftarMedPlusTtg2.size();
						}else if(rd.getJenis()==2){
							daftarMedPlusTtg3.add(rd);
							jmlMedPlusTtg3=daftarMedPlusTtg3.size();
						}else if(rd.getJenis()==3){
							daftarMedPlusTtg4.add(rd);
							jmlMedPlusTtg4=daftarMedPlusTtg4.size();
						}else if(rd.getJenis()==4){
							daftarMedPlusTtg5.add(rd);
							jmlMedPlusTtg5=daftarMedPlusTtg5.size();
						}else if(rd.getJenis()==5){
							daftarMedPlusTtg6.add(rd);
							jmlMedPlusTtg6=daftarMedPlusTtg6.size();
						}
						jmlallMedPlus=jmlMedPlusTtg6+jmlMedPlusTtg5+jmlMedPlusTtg4+jmlMedPlusTtg3+jmlMedPlusTtg2+jmlMedPlusTtg1;
						
					}else if(rd.getLsbs_id()==839){
						boolean test=products.medicalPlusGoldAddOn(rd.getLsdbs_number().toString());
						if(products.medicalPlusSilverAddon(rd.getLsdbs_number().toString())){
							rd.setFlag_jenis_Paketmedplus(2);//1:bronze 2:silver 3:gold 4:platinum
						}else if(products.medicalPlusGoldAddOn(rd.getLsdbs_number().toString())){
							rd.setFlag_jenis_Paketmedplus(3);//1:bronze 2:silver 3:gold 4:platinum
						}else if(products.medicalPlusPlatinumAddOn(rd.getLsdbs_number().toString())){
							rd.setFlag_jenis_Paketmedplus(3);//1:bronze 2:silver 3:gold 4:platinum
						}
						
						basic = uwManager.selectBasicName(kd_rd, nm_rd);
						if(basic != null){
							jenis = 0;
							
						}
						spouse = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG II)");
						if(spouse != null){
							jenis = 1;
						}
						child1 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG III)");
						if(child1 != null){
							jenis = 2;
						}
						child2 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG IV)");
						if(child2 != null){
							jenis = 3;
						}
						child3 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG V)");
						if(child3 != null){
							jenis = 4;
						}
						child4 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG VI)");
						if(child4 != null){
							jenis = 5;
						}
						rd.setJenis(jenis);
						if(rd.getJenis()==0){
							daftarRiderPlusTtg1.add(rd);
							jmlRiderMedPlusTtg1=daftarRiderPlusTtg1.size();
						}else if(rd.getJenis()==1){
							daftarRiderPlusTtg2.add(rd);
							jmlRiderMedPlusTtg2=daftarRiderPlusTtg2.size();
						}else if(rd.getJenis()==2){
							daftarRiderPlusTtg3.add(rd);
							jmlRiderMedPlusTtg3=daftarRiderPlusTtg3.size();
						}else if(rd.getJenis()==3){
							daftarRiderPlusTtg4.add(rd);
							jmlRiderMedPlusTtg4=daftarRiderPlusTtg4.size();
						}else if(rd.getJenis()==4){
							daftarRiderPlusTtg5.add(rd);
							jmlRiderMedPlusTtg5=daftarRiderPlusTtg5.size();
						}else if(rd.getJenis()==5){
							daftarRiderPlusTtg6.add(rd);
							jmlRiderMedPlusTtg6=daftarRiderPlusTtg6.size();
						}
						daftarRiderPlusTtg.add(rd);
						
					}//provestara
					else if(rd.getLsbs_id()==840){
										
						if(rd.getJenis()==0){
							daftarProvestaraTtg1.add(rd);
							jmlProvestaraTtg1=daftarProvestaraTtg1.size();
						}else if(rd.getJenis()==1){
							daftarProvestaraTtg2.add(rd);
							jmlProvestaraTtg2=daftarProvestaraTtg2.size();
						}else if(rd.getJenis()==2){
							daftarProvestaraTtg3.add(rd);
							jmlProvestaraTtg3=daftarProvestaraTtg3.size();
						}else if(rd.getJenis()==3){
							daftarProvestaraTtg4.add(rd);
							jmlProvestaraTtg4=daftarProvestaraTtg4.size();
						}else if(rd.getJenis()==4){
							daftarProvestaraTtg5.add(rd);
							jmlProvestaraTtg5=daftarProvestaraTtg5.size();
						}else if(rd.getJenis()==5){
							daftarProvestaraTtg6.add(rd);
							jmlProvestaraTtg6=daftarProvestaraTtg6.size();
						}
					jmlallProvestara=jmlProvestaraTtg6+jmlProvestaraTtg5+jmlProvestaraTtg4+jmlProvestaraTtg3+jmlProvestaraTtg2+jmlProvestaraTtg1;
					
				}else if( rd.getLsbs_id()==841 || rd.getLsbs_id()==842 || rd.getLsbs_id()==843 ){
										
					basic = uwManager.selectBasicName(kd_rd, nm_rd);
					if(basic != null){
						jenis = 0;
						
					}
					spouse = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG II)");
					if(spouse != null){
						jenis = 1;
					}
					child1 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG III)");
					if(child1 != null){
						jenis = 2;
					}
					child2 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG IV)");
					if(child2 != null){
						jenis = 3;
					}
					child3 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG V)");
					if(child3 != null){
						jenis = 4;
					}
					child4 = uwManager.selectJenisRider(kd_rd, nm_rd, "TERTANGGUNG VI)");
					if(child4 != null){
						jenis = 5;
					}
					rd.setJenis(jenis);
					if(rd.getJenis()==0){
						daftarRiderProvestaraTtg1.add(rd);
						jmlRiderProvestaraTtg1=daftarRiderProvestaraTtg1.size();
					}else if(rd.getJenis()==1){
						daftarRiderProvestaraTtg2.add(rd);
						jmlRiderProvestaraTtg2=daftarRiderProvestaraTtg2.size();
					}else if(rd.getJenis()==2){
						daftarRiderProvestaraTtg3.add(rd);
						jmlRiderProvestaraTtg3=daftarRiderProvestaraTtg3.size();
					}else if(rd.getJenis()==3){
						daftarRiderProvestaraTtg4.add(rd);
						jmlRiderProvestaraTtg4=daftarRiderProvestaraTtg4.size();
					}else if(rd.getJenis()==4){
						daftarRiderProvestaraTtg5.add(rd);
						jmlRiderProvestaraTtg5=daftarRiderProvestaraTtg5.size();
					}else if(rd.getJenis()==5){
						daftarRiderProvestaraTtg6.add(rd);
						jmlRiderProvestaraTtg6=daftarRiderProvestaraTtg6.size();
					}
					daftarRiderProvestaraTtg.add(rd);
					
				}
					
					b.add(rd);
					
				}
				jmlallRiderMedPlus=jmlRiderMedPlusTtg6+jmlRiderMedPlusTtg5+jmlRiderMedPlusTtg4+jmlRiderMedPlusTtg3+jmlRiderMedPlusTtg2+jmlRiderMedPlusTtg1;
				// *Andhika - Untuk produk stand alone
				if("183,189,193,195,201,204,214".indexOf(a.getDatausulan().getLsbs_id().toString())>-1){
					PesertaPlus_mix set_pp_mix = new PesertaPlus_mix();
					set_pp_mix.setMspr_premium(a.getDatausulan().getMspr_premium());
					set_pp_mix.setLsre_id(1);
					set_pp_mix.setTgl_lahir(a.getTertanggung().getMspe_date_birth());
					set_pp_mix.setSex(a.getTertanggung().getMspe_sex2());
					set_pp_mix.setKelamin(a.getTertanggung().getMspe_sex());
					set_pp_mix.setUmur(a.getTertanggung().getUsiattg());
					set_pp_mix.setFlag_jenis_peserta(0);
					set_pp_mix.setLsbs_id(a.getDatausulan().getLsbs_id());
					set_pp_mix.setLsdbs_number(a.getDatausulan().getLsdbs_number());
//					set_pp_mix.setPlan_rider(bisnis);
					set_pp_mix.setFlag_jenis_peserta(jenis);
					//rd.setFlag_include(Integer.parseInt(flag_include));
					pp_mix.add(set_pp_mix);
				}
				
			}else if(jmlh_rider == 0 && "183,189,193,195,201,204,214".indexOf(a.getDatausulan().getLsbs_id().toString())>-1){
				Integer lsbsXid = a.getDatausulan().getLsbs_id();
				Integer lsdbsXnumber = nm_rd;
				PesertaPlus_mix set_pp_mix = new PesertaPlus_mix();
				set_pp_mix.setLsbs_id(lsbsXid);
				set_pp_mix.setLsdbs_number(lsdbsXnumber);
				set_pp_mix.setFlag_jenis_peserta(0);
				pp_mix.add(set_pp_mix);
			}
			
			a.getDatausulan().setJmlriderplus(countbasic);
			a.getDatausulan().setCekBasic(cekBasic);
			a.getDatausulan().setDaftaRider(b);
			a.getDatausulan().setDaftarplusplus(c);
			a.getDatausulan().setDaftarplus_mix(pp_mix);
			a.getDatausulan().setDaftarplus(pp);
			a.getDatausulan().setDaftaRiderAddOnTtg1(daftarRiderPlusTtg1);
			a.getDatausulan().setDaftaRiderAddOnTtg2(daftarRiderPlusTtg2);
			a.getDatausulan().setDaftaRiderAddOnTtg3(daftarRiderPlusTtg3);
			a.getDatausulan().setDaftaRiderAddOnTtg4(daftarRiderPlusTtg4);
			a.getDatausulan().setDaftaRiderAddOnTtg5(daftarRiderPlusTtg5);
			a.getDatausulan().setDaftaRiderAddOnTtg6(daftarRiderPlusTtg6);
			a.getDatausulan().setDaftaRiderMedicalPlusTtg1(daftarMedPlusTtg1);
			a.getDatausulan().setDaftaRiderMedicalPlusTtg2(daftarMedPlusTtg2);
			a.getDatausulan().setDaftaRiderMedicalPlusTtg3(daftarMedPlusTtg3);
			a.getDatausulan().setDaftaRiderMedicalPlusTtg4(daftarMedPlusTtg4);
			a.getDatausulan().setDaftaRiderMedicalPlusTtg5(daftarMedPlusTtg5);
			a.getDatausulan().setDaftaRiderMedicalPlusTtg6(daftarMedPlusTtg6);
			a.getDatausulan().setDaftaRiderAddOn(jmlallRiderMedPlus);
			a.getDatausulan().setDaftaRiderMedicalPlus(jmlallMedPlus);
			a.getDatausulan().setDaftaRiderAddOnTtg(daftarRiderPlusTtg);
						
			// *Andhika - Daftar peserta tambahan
			int jumlah_peserta = Integer.parseInt(request.getParameter("pesertax"));
			Integer no_urut = null;
			String lsre_id;
			String flagJenis;
			Integer jmlFlagJenis;
			Integer tinggi;
			Integer berat;
			String lsne ="";
			String kerja="";
			String nama = null;
			String tgllhr = null;
			String blnhr = null;
			String thnhr = null;
			Integer kelamin = null;
			Integer umur = null;
			String tanggal_lahir = null;
			Integer x_FC = 0;
			Integer jml_flagJP = 0;
			Integer LsbsId = null;
			Integer LsdbsNo = null;
			Double msprPremi= 0.;
			String planR = null;
			Integer cekRiderBasic = 0;
			String lsbs_id_plus=null;
			Integer lsbs_id_plus_int;
			String s_flagCekRJ , s_flagCekRG, s_flagCekRB, s_flagCekPK ;
			ArrayList<PesertaPlus_mix> xxxMix = new ArrayList<PesertaPlus_mix>();
			a.getDatausulan().setJml_peserta(jumlah_peserta);
			
			//Untuk provestara variabel ini di gunakan untuk cek data peserta
			String NamaT1 = null, NamaT2 = null, NamaT3 = null,NamaT4 = null,NamaT5 = null;
			String tgllhrT1 = null, tgllhrT2 = null, tgllhrT3 = null,tgllhrT4 = null,tgllhrT5 = null;
			String blnhrT1 = null, blnhrT2 = null, blnhrT3 = null,blnhrT4 = null,blnhrT5 = null;
			String thnhrT1 = null, thnhrT2 = null, thnhrT3 = null,thnhrT4 = null,thnhrT5 = null;
			Integer kelaminT1 = null, kelaminT2 = null, kelaminT3 = null, kelaminT4 = null, kelaminT5 = null;
			String lsre_idT1 = null , lsre_idT2 = null, lsre_idT3 = null, lsre_idT4 = null , lsre_idT5 = null;
			
			if(jumlah_peserta > 0){
				for(int x = 1; x <= jumlah_peserta; x++){
					no_urut = ServletRequestUtils.getIntParameter(request, "ptx.no_urut"+x);
					nama = request.getParameter("ptx.nama"+x);
					tgllhr = request.getParameter("tgllhr"+x);
					blnhr = request.getParameter("blnhr"+x);
					thnhr = request.getParameter("thnhr"+x);
					tinggi = ServletRequestUtils.getIntParameter(request, "ptx.tinggi"+x,0);
					berat = ServletRequestUtils.getIntParameter(request, "ptx.berat"+x,0);
					lsne = request.getParameter("ptx.lsne_id"+x);
					tanggal_lahir = FormatString.rpad("0",tgllhr,2)+"/"+FormatString.rpad("0",blnhr,2)+"/"+thnhr;
					kelamin = ServletRequestUtils.getIntParameter(request, "ptx.kelamin"+x);
					umur = ServletRequestUtils.getIntParameter(request, "ptx.umur"+x);
					lsre_id =request.getParameter("ptx.lsre_id"+x);
					kerja = request.getParameter("ptx.kerja"+x);
					flagJenis =request.getParameter("ptx.flag_jenis_peserta"+x);
					jmlFlagJenis = Integer.parseInt(flagJenis);
					lsbs_id_plus = request.getParameter("ptx.lsbs_id_plus"+x);
					lsbs_id_plus_int = Integer.parseInt(lsbs_id_plus);
					s_flagCekRJ=ServletRequestUtils.getStringParameter(request, "cekRJ"+x,"0");
					s_flagCekRG=ServletRequestUtils.getStringParameter(request, "cekRG"+x,"0");
					s_flagCekRB=ServletRequestUtils.getStringParameter(request, "cekRB"+x,"0");
					s_flagCekPK=ServletRequestUtils.getStringParameter(request, "cekPK"+x,"0");
					if(jmlFlagJenis == null)jmlFlagJenis = 0;
					if(lsbs_id_plus_int == null)lsbs_id_plus_int = 0;
					if(nama == null)nama = "";
					if(planR == null)planR = "";
					tgllhr = request.getParameter("tgllhr"+x);
					if (tgllhr == null)tgllhr ="";
					blnhr = request.getParameter("blnhr"+x);
					if (blnhr == null)blnhr = "";
					thnhr = request.getParameter("thnhr"+x);
					if (thnhr == null)thnhr = "";
					if ((tgllhr.trim().length()==0)||(blnhr.trim().length()==0)||(thnhr.trim().length()==0)){
						tanggal_lahir=null;
					}else{
						boolean cekk1= f_validasi.f_validasi_numerik(tgllhr);	
						boolean cekk2= f_validasi.f_validasi_numerik(blnhr);
						boolean cekk3= f_validasi.f_validasi_numerik(thnhr);		
						if ((cekk1==false) ||(cekk2==false) || (cekk3==false)){
							tanggal_lahir=null;
						}
					}
					Date tanggallahir = null;
					
					if (tanggal_lahir != null){
						tanggallahir = defaultDateFormat.parse(tanggal_lahir);
					}
					if(tanggallahir==null){
						errors.reject("","Mohon mengisi kolom tanggal lahir yang kosong");
					}
					if(flagJenis == ""){
						flagJenis = "0";
					}
					if(flagJenis == null || flagJenis == "0"){
						errors.reject("","Mohon memilih jenis Rider terlebih dahulu");
					}
					if(lsbs_id_plus_int == 0){
						errors.reject("","Mohon memilih Jenis Produk Rider terlebih dahulu");
					}
					if(nama == null || nama == ""){
						errors.reject("","Mohon mengisi nama peserta asuransi tambahan terlebih dahulu");
					}
					
					if(tinggi == 0){
						errors.reject("","Mohon memasukkan tinggi badan asuransi tambahan terlebih dahulu");
					}
					
					if(berat==0){
						errors.reject("","Mohon memasukkan berat badan asuransi tambahan terlebih dahulu");
					}
					
					if (umur==null){
						umur = new Integer(0);
					}
//					if(x>0 && flagJenis="0" && tanggallahir!=null){
					if(tanggallahir!=null){
						f_hit_umur umr= new f_hit_umur();
						Integer tahun1,tahun2, bulan1, bulan2, tanggal1, tanggal2;
						tanggal1= a.getDatausulan().getMste_beg_date().getDate();
						bulan1 = (a.getDatausulan().getMste_beg_date().getMonth())+1;
						tahun1 = (a.getDatausulan().getMste_beg_date().getYear())+1900;
						tanggal2=tanggallahir.getDate();
						bulan2=(tanggallahir.getMonth())+1;
						tahun2=(tanggallahir.getYear())+1900;
						umur = umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1); //umurx
						if(umur == 0){umur = 1;}
						if(tanggal2==null){tanggal2=0;}
						if(bulan2==null){bulan2=0;}
						if(tahun2==null){tahun2=0;}
					}
					if (kelamin == null){
						kelamin= new Integer(1);
					}
					
					Integer fjp = (Integer.parseInt(flagJenis));
					
//					if(lsbs_id_plus_int!=838 &&
//						(s_flagCekPK.equalsIgnoreCase("on") || s_flagCekRG.equalsIgnoreCase("on") ||
//						 s_flagCekRB.equalsIgnoreCase("on") ||s_flagCekRJ.equalsIgnoreCase("on"))){
//							errors.reject("", "Untuk Manfaat Tambahan Hanya untuk Smile Medical (+)");
//					}			
					
					PesertaPlus set_pp = new PesertaPlus();
					set_pp.setNo_urut(no_urut);
					set_pp.setLsre_id(Integer.parseInt(lsre_id));
					set_pp.setNama(nama);
					set_pp.setKelamin(kelamin);
					set_pp.setUmur(umur);
					set_pp.setTgl_lahir(tanggallahir);
					set_pp.setFlag_jenis_peserta(Integer.parseInt(flagJenis));
					set_pp.setLsbs_id_plus(Integer.parseInt(lsbs_id_plus));
					set_pp.setTinggi(tinggi);
					set_pp.setBerat(berat);
					set_pp.setLsne(lsne);
					set_pp.setKerja(kerja);
					
						ArrayList test = a.getDatausulan().getDaftaRider(); // *Banyaknya Rider yang diambil
						Integer jumlah_r = new Integer(test.size());
					
							for (int k =0 ; k <jumlah_r.intValue();k++)
							{
								Datarider drr = (Datarider)test.get(k);
								Integer x_jenis = drr.getJenis();
								Double x_prm  = drr.getMspr_premium();
								Integer x_lsbsId = drr.getLsbs_id();
								Integer x_lsdbsNumber = drr.getLsdbs_number();
								if(x_jenis == null){
									x_jenis = new Integer(0);
								}
								
																								
								if(x_jenis.compareTo(jmlFlagJenis) == 0 && ("840,841,842,843".indexOf(lsbs_id_plus.toString()) < 0)){
									if(lsbs_id_plus_int.compareTo(x_lsbsId) == 0){
										LsbsId = drr.getLsbs_id();
										LsdbsNo = drr.getLsdbs_number();
										msprPremi = drr.getMspr_premium();
										x_FC = x_FC+1;
									}
								}//provestara
								else if (("840,841,842,843".indexOf(lsbs_id_plus.toString()) >-1)){							
																		
									if (lsbs_id_plus_int.compareTo(x_lsbsId) == 0 ){
										
										if(jmlFlagJenis == 0 && (x_lsdbsNumber == 1 || x_lsdbsNumber == 2)){
												x_FC = x_FC+1;
												cek_PT_ttg1 = cek_PT_ttg1 + 1;
												LsbsId = drr.getLsbs_id();
												LsdbsNo = drr.getLsdbs_number();
												msprPremi = drr.getMspr_premium();
												
												if(cek_PT_ttg1 > 1 ){
													if(!NamaT1.equalsIgnoreCase(nama) || !tgllhrT1.equalsIgnoreCase(tgllhr) || !blnhrT1.equalsIgnoreCase(blnhr) || !thnhrT1.equalsIgnoreCase(thnhr) || !kelaminT1.equals(kelamin) || !lsre_idT1.equalsIgnoreCase(lsre_id) ){
														errors.reject("","HALAMAN DATA USULAN : Data Peserta ASURANSI KESEHATAN untuk Tertangung I Harus Sama antara data (RI+RJ) dengan data RG/RB/PK");
													}
												}else{
													NamaT1 = nama;
													tgllhrT1 = tgllhr ;
													blnhrT1 = blnhr ;
													thnhrT1  = thnhr ;
													kelaminT1 = kelamin ;
													lsre_idT1 = lsre_id;
												}
											
										}else if(jmlFlagJenis == 1  && (x_lsdbsNumber == 3 || x_lsdbsNumber == 4) ){
												x_FC = x_FC+1;
												cek_PT_ttg2 = cek_PT_ttg2 + 1;
												LsbsId = drr.getLsbs_id();
												LsdbsNo = drr.getLsdbs_number();
												msprPremi = drr.getMspr_premium();
												
												if(cek_PT_ttg2 > 1 ){
													if(!NamaT2.equalsIgnoreCase(nama) || !blnhrT2.equalsIgnoreCase(blnhr) || !thnhrT2.equalsIgnoreCase(thnhr) || !lsre_idT2.equalsIgnoreCase(lsre_id) || !kelaminT2.equals(kelamin)){
															errors.reject("","HALAMAN DATA USULAN : Data Peserta ASURANSI KESEHATAN untuk Tertangung II Harus Sama antara data (RI+RJ) dengan data RG/RB/PK");
													}
												}else{
													NamaT2 = nama;
													tgllhrT2 = tgllhr ;
													blnhrT2 = blnhr ;
													thnhrT2  = thnhr ;
													kelaminT2 = kelamin ;
													lsre_idT2 = lsre_id;
												}
											
										}else if(jmlFlagJenis == 2 && (x_lsdbsNumber == 5 || x_lsdbsNumber == 6)   ){
												x_FC = x_FC+1;
												cek_PT_ttg3 = cek_PT_ttg3 + 1;
												LsbsId = drr.getLsbs_id();
												LsdbsNo = drr.getLsdbs_number();
												msprPremi = drr.getMspr_premium();
												
												if(cek_PT_ttg3 > 1 ){
													if(!NamaT3.equalsIgnoreCase(nama) || !tgllhrT3.equalsIgnoreCase(tgllhr) || !blnhrT3.equalsIgnoreCase(blnhr) || !thnhrT3.equalsIgnoreCase(thnhr)|| !kelaminT3.equals(kelamin) || !lsre_idT3.equalsIgnoreCase(lsre_id) ){
															errors.reject("","HALAMAN DATA USULAN : Data Peserta ASURANSI KESEHATAN untuk Tertangung III Harus Sama antara data (RI+RJ) dengan data RG/RB/PK");
													}
												}else{
													NamaT3 = nama;
													tgllhrT3 = tgllhr ;
													blnhrT3 = blnhr ;
													thnhrT3  = thnhr ;
													kelaminT3 = kelamin ;
													lsre_idT3 = lsre_id;
												}
										}else if(jmlFlagJenis == 3 && (x_lsdbsNumber == 7 || x_lsdbsNumber == 8) ){
												x_FC = x_FC+1;
												cek_PT_ttg4 = cek_PT_ttg4 + 1;
												LsbsId = drr.getLsbs_id();
												LsdbsNo = drr.getLsdbs_number();
												msprPremi = drr.getMspr_premium();
												
												if(cek_PT_ttg4 > 1 ){
													if(!NamaT4.equalsIgnoreCase(nama) || !tgllhrT4.equalsIgnoreCase(tgllhr) || !blnhrT4.equalsIgnoreCase(blnhr) || !thnhrT4.equalsIgnoreCase(thnhr) || !kelaminT4.equals(kelamin) ||  !lsre_idT4.equalsIgnoreCase(lsre_id) ){
															errors.reject("","HALAMAN DATA USULAN : Data Peserta ASURANSI KESEHATAN untuk Tertangung IV Harus Sama antara data (RI+RJ) dengan data RG/RB/PK");
													}
												}else{
													NamaT4 = nama;
													tgllhrT4 = tgllhr ;
													blnhrT4 = blnhr ;
													thnhrT4  = thnhr ;
													kelaminT4 = kelamin ;
													lsre_idT4 = lsre_id;
												}
										}else if(jmlFlagJenis == 4 && (x_lsdbsNumber == 9 || x_lsdbsNumber == 10) ){
												x_FC = x_FC+1;
												cek_PT_ttg5 = cek_PT_ttg5 + 1;
												LsbsId = drr.getLsbs_id();
												LsdbsNo = drr.getLsdbs_number();
												msprPremi = drr.getMspr_premium();
												
												if(cek_PT_ttg5 > 1 ){
													if(!NamaT5.equalsIgnoreCase(nama) || !tgllhrT5.equalsIgnoreCase(tgllhr) || !blnhrT5.equalsIgnoreCase(blnhr) || !thnhrT5.equalsIgnoreCase(thnhr) || !kelaminT5.equals(kelamin) || !lsre_idT5.equalsIgnoreCase(lsre_id) ){
															errors.reject("","HALAMAN DATA USULAN : Data Peserta ASURANSI KESEHATAN untuk Tertangung V Harus Sama antara data (RI+RJ) dengan data RG/RB/PK");
													}
												}else{
													NamaT5 = nama;
													tgllhrT5 = tgllhr ;
													blnhrT5 = blnhr ;
													thnhrT5  = thnhr ;
													kelaminT5 = kelamin ;
													lsre_idT5 = lsre_id;
												}
										}
																		
										
										if (x_lsbsId ==840){
											
											if(jmlFlagJenis == 1  && (x_lsdbsNumber == 3 || x_lsdbsNumber == 4) ){
												cek_PT_ttg2RIJ = cek_PT_ttg2RIJ + 1;
											}else if(jmlFlagJenis == 2 && (x_lsdbsNumber == 5 || x_lsdbsNumber == 6) ){
												cek_PT_ttg3RIJ = cek_PT_ttg3RIJ + 1;
											}else if(jmlFlagJenis == 3 && (x_lsdbsNumber == 7 || x_lsdbsNumber == 8) ){
												cek_PT_ttg4RIJ = cek_PT_ttg4RIJ + 1;
											}else if(jmlFlagJenis == 4 && (x_lsdbsNumber == 9 || x_lsdbsNumber == 10)  ){
												cek_PT_ttg5RIJ = cek_PT_ttg5RIJ + 1;
											}
										}
										
										if (x_lsbsId == 841){
											if(jmlFlagJenis == 0 && (x_lsdbsNumber == 1 || x_lsdbsNumber == 2)  ){
												cek_PT_ttg1RG = cek_PT_ttg1RG + 1;
											}else if(jmlFlagJenis == 1 && (x_lsdbsNumber == 3 || x_lsdbsNumber == 4)  ){
												cek_PT_ttg2RG = cek_PT_ttg2RG + 1;
											}else if(jmlFlagJenis == 2 && (x_lsdbsNumber == 5 || x_lsdbsNumber == 6)  ){
												cek_PT_ttg3RG = cek_PT_ttg3RG + 1;
											}else if(jmlFlagJenis == 3 && (x_lsdbsNumber == 7 || x_lsdbsNumber == 8)  ){
												cek_PT_ttg4RG = cek_PT_ttg4RG + 1;
											}else if(jmlFlagJenis == 4 && (x_lsdbsNumber == 9 || x_lsdbsNumber == 10)  ){
												cek_PT_ttg5RG = cek_PT_ttg5RG + 1;
											}
										}
										
										if (x_lsbsId == 842){
											if(jmlFlagJenis == 0  && (x_lsdbsNumber == 1 || x_lsdbsNumber == 2) ){
												cek_PT_ttg1RB = cek_PT_ttg1RB + 1;
											}else if(jmlFlagJenis == 1  && (x_lsdbsNumber == 3 || x_lsdbsNumber == 4) ){
												cek_PT_ttg2RB = cek_PT_ttg2RB + 1;
											}else if(jmlFlagJenis == 2 && (x_lsdbsNumber == 5 || x_lsdbsNumber == 6)){
												cek_PT_ttg3RB = cek_PT_ttg3RB + 1;
											}else if(jmlFlagJenis == 3 && (x_lsdbsNumber == 7 || x_lsdbsNumber == 8) ){
												cek_PT_ttg4RB = cek_PT_ttg4RB + 1;
											}else if(jmlFlagJenis == 4 && (x_lsdbsNumber == 9 || x_lsdbsNumber == 10) ){
												cek_PT_ttg5RB = cek_PT_ttg5RB + 1;
											}
										}
										
										if (x_lsbsId == 843){
											if(jmlFlagJenis == 0 && (x_lsdbsNumber == 1 || x_lsdbsNumber == 2) ){
												cek_PT_ttg1Pk = cek_PT_ttg1Pk + 1;
											}else if(jmlFlagJenis == 1 &&  (x_lsdbsNumber == 3 || x_lsdbsNumber == 4)){
												cek_PT_ttg2Pk = cek_PT_ttg2Pk + 1;
											}else if(jmlFlagJenis == 2 &&  (x_lsdbsNumber == 5 || x_lsdbsNumber == 6) ){
												cek_PT_ttg3Pk = cek_PT_ttg3Pk + 1;
											}else if(jmlFlagJenis == 3 &&  (x_lsdbsNumber == 7 || x_lsdbsNumber == 8) ){
												cek_PT_ttg4Pk = cek_PT_ttg4Pk + 1;
											}else if(jmlFlagJenis == 4 &&  (x_lsdbsNumber == 9 || x_lsdbsNumber == 10) ){
												cek_PT_ttg5Pk = cek_PT_ttg5Pk + 1;
											}
										}
										
									}
									
									
							
//									LsbsId = drr.getLsbs_id();
//									LsdbsNo = drr.getLsdbs_number();
//									msprPremi = drr.getMspr_premium();
								}else{
									x_FC = x_FC;
								}
																															
							}
							a.getDatausulan().setFlag_compare(x_FC);
							
					set_pp.setMspr_premium(msprPremi);			
					set_pp.setLsbs_id(LsbsId);
					set_pp.setLsdbs_number(LsdbsNo);
				    pp.add(set_pp);
				
				// *SetMix
				PesertaPlus_mix set_pp_mix = new PesertaPlus_mix();
				set_pp_mix.setNo_urut(no_urut);
				set_pp_mix.setLsre_id(Integer.parseInt(lsre_id));
				set_pp_mix.setNama(nama);
				set_pp_mix.setKelamin(kelamin);
				set_pp_mix.setUmur(umur);
				set_pp_mix.setTgl_lahir(tanggallahir);
				set_pp_mix.setFlag_jenis_peserta(Integer.parseInt(flagJenis));
				set_pp_mix.setLsbs_id_plus(Integer.parseInt(lsbs_id_plus));
				set_pp_mix.setTinggi(tinggi);
				set_pp_mix.setBerat(berat);
				set_pp_mix.setLsne(lsne);
				set_pp_mix.setKerja(kerja);
				Integer xax = a.getDatausulan().getLsbs_id();
				
						for (int k =0 ; k <jumlah_r.intValue();k++)
						{
							Datarider drr = (Datarider)test.get(k);
							Integer x_jenis = drr.getJenis();
							Double x_prm  = drr.getMspr_premium();
							Integer x_lsbsId = drr.getLsbs_id();
							Integer x_lsdbsNumber = drr.getLsdbs_number();
							if (!(xax==208 && x_lsbsId==836)){
								set_pp_mix.setLsbs_id(x_lsbsId);
								set_pp_mix.setLsdbs_number(x_lsdbsNumber);
							}
							if(x_jenis == null){
								x_jenis = new Integer(0);
							}
							if(x_jenis.compareTo(jmlFlagJenis) == 0 ){
								if(x_lsbsId.compareTo(lsbs_id_plus_int) == 0 && ("840,841,842,843".indexOf(lsbs_id_plus.toString())< 0)){
									LsbsId = drr.getLsbs_id();
									LsdbsNo = drr.getLsdbs_number();
									msprPremi = drr.getMspr_premium();
								}
								//kondisi untuk provestara
								else if(("840,841,842,843".indexOf(lsbs_id_plus.toString())>-1)){
																		
									if (lsbs_id_plus_int.compareTo(x_lsbsId) == 0 ){
										
												if(jmlFlagJenis == 0 && (x_lsdbsNumber == 1 || x_lsdbsNumber == 2)){
														
														LsbsId = drr.getLsbs_id();
														LsdbsNo = drr.getLsdbs_number();
														msprPremi = drr.getMspr_premium();
														
												}else if(jmlFlagJenis == 1  && (x_lsdbsNumber == 3 || x_lsdbsNumber == 4) ){
														
														LsbsId = drr.getLsbs_id();
														LsdbsNo = drr.getLsdbs_number();
														msprPremi = drr.getMspr_premium();
														
												}else if(jmlFlagJenis == 2 && (x_lsdbsNumber == 5 || x_lsdbsNumber == 6)   ){
														
														LsbsId = drr.getLsbs_id();
														LsdbsNo = drr.getLsdbs_number();
														msprPremi = drr.getMspr_premium();
														
												}else if(jmlFlagJenis == 3 && (x_lsdbsNumber == 7 || x_lsdbsNumber == 8) ){
														
														LsbsId = drr.getLsbs_id();
														LsdbsNo = drr.getLsdbs_number();
														msprPremi = drr.getMspr_premium();
														
												}else if(jmlFlagJenis == 4 && (x_lsdbsNumber == 9 || x_lsdbsNumber == 10) ){
														
														LsbsId = drr.getLsbs_id();
														LsdbsNo = drr.getLsdbs_number();
														msprPremi = drr.getMspr_premium();
														
												}
										}
								}
							}
						}
						
					set_pp_mix.setMspr_premium(msprPremi);	
					Integer xay = set_pp_mix.getLsbs_id_plus();
//					if (!(xax==208 && xay==836)){
//						set_pp_mix.setLsbs_id(LsbsId);
//						set_pp_mix.setLsdbs_number(LsdbsNo);
//					}
					set_pp_mix.setLsbs_id(LsbsId);
					set_pp_mix.setLsdbs_number(LsdbsNo);
					pp_mix.add(set_pp_mix);
					
					if("214".indexOf(a.getDatausulan().getLsbs_id().toString())>-1){
						//jika umur tertanggung tambahan provestara di atas 50 tahun wajib memilih Medis pada halaman Pemegang Polis dengan status "Dengan Pemeriksaan Kesehatan"
						if (umur >= 50 ){
							if (a.getDatausulan().getMste_medical() == 0){
								errors.reject("","HALAMAN DATA USULAN : Terdaftarnya Peserta Asuransi diatas usia 50 - 65 tahun, wajib memilih status medis - DENGAN PEMERIKSAAN KESEHATAN - pada halaman Pemegang Polis");
							}
						}
					}
					
			}
			a.getDatausulan().setDaftarplus(pp);
			a.getDatausulan().setDaftarplus_mix(pp_mix);
			
			}else if(jumlah_peserta == 0){

				//validasi provestara tanpa ada peserta tambahan (jumlah_peserta = 0)
				if("214".indexOf(a.getDatausulan().getLsbs_id().toString())>-1){
					//jika umur tertanggung utama provestara di atas 50 tahun wajib memilih Medis pada halaman Pemegang Polis dengan status "Dengan Pemeriksaan Kesehatan"
					
					if(a.getTertanggung().getMste_age()!=null){	
					if (a.getTertanggung().getMste_age() >= 50 ){
						if (a.getDatausulan().getMste_medical() == 0){
							errors.reject("","HALAMAN DATA USULAN : Terdaftarnya Tertanggung Utama diatas usia 50 - 65 tahun, wajib memilih status medis - DENGAN PEMERIKSAAN KESEHATAN - pada halaman Pemegang Polis");
						}
					}
				}
				}
			}
			
			
			// *Compare
			if(jumlah_peserta > 0 || jmlh_rider > 0){
//				if(jumlah_peserta != jmlh_rider){
//					errors.reject("", "Jumlah Data Peserta dan Data Rider Kesehatan tidak sama");
//				}
				ArrayList test = a.getDatausulan().getDaftaRider();
				Integer jumlah_data_rider = new Integer(test.size());
			
					for (int k =0 ; k <jumlah_data_rider.intValue();k++)
					{
						Datarider drr = (Datarider)test.get(k);
						Integer x_jenis2 = drr.getJenis();
						Integer x = 0;
						
						if(x_jenis2 == null){
							x_jenis2 = new Integer(9);
						}
						
						if(x_jenis2.compareTo(x) == 0){
							cekRiderBasic = cekRiderBasic+1;
						}else{
							cekRiderBasic = cekRiderBasic;
						}
					}
					// *cekBasic
					if(cekRiderBasic < 1){
//						if("183,189,193,195,201,204".indexOf(a.getDatausulan().getLsbs_id().toString())<-1){
							String kodex = a.getDatausulan().getLsbs_id().toString();
//							if(kodex != "183" || kodex != "189" ||kodex != "193" ||kodex != "195" ||kodex != "201" ||kodex != "204"){
//							errors.reject("", "Harus memilih Product Rider Utama (TERTANGGUNG I) terlebih dahulu");
//						}
					}
					
					if(cekRiderBasic > 0 && jumlah_peserta == 0){
						errors.reject("", "Product Rider UTAMA / (TERTANGGUNG I) harus mengisi Data Peserta Tambahan terlebih dahulu");
					}
					
					if("183,189,193,195,201,204".indexOf(a.getDatausulan().getLsbs_id().toString())>-1 && cekRiderBasic >= 1 ){
						errors.reject("", "Untuk Product Stand Alone tidak perlu memilih Rider Utama / (TERTANGGUNG I)");
					}
					
				
			ArrayList<PesertaPlus> plus2 = a.getDatausulan().getDaftarplus();
			Integer cocok = new Integer(plus2.size()); // *data peserta
			jml_flagJP = a.getDatausulan().getFlag_compare();
				if(jml_flagJP==null){
					jml_flagJP = 0;
				}
				if("183,189,193,195,201,204,214".indexOf(a.getDatausulan().getLsbs_id().toString())>-1){
					spouseChild=spouseChild-1;
				}
				
				//Provestara Validation1 (Ridhaal) - cek rider sudah ada data tambahan atau belum
				if(juml_rid_prov > 0 && jumlah_peserta == 0){
					errors.reject("", "Mohon mengisi data peserta asuransi tambahan terlebih dahulu");
				}
				
				if(cocok > 0 && (!lsbs_id_plus.equals("836"))){ // Smile baby ada peserta, tapi ga kena validasi ini - Ryan
					
					if(("840,841,842,843".indexOf(lsbs_id_plus.toString())>-1)){ // Provestara Validation2 (Ridhaal) - cek kesesuain rider dengan data tertanggung
						
						if (cek_PT_ttg2RIJ > 1 || cek_PT_ttg3RIJ > 1 || cek_PT_ttg4RIJ > 1 || cek_PT_ttg5RIJ > 1 || 
							cek_PT_ttg1RG > 1 || cek_PT_ttg2RG > 1  || cek_PT_ttg3RG > 1 || cek_PT_ttg4RG > 1 || cek_PT_ttg5RG > 1 || 
							cek_PT_ttg1RB > 1 || cek_PT_ttg2RB > 1  || cek_PT_ttg3RB > 1 || cek_PT_ttg4RB > 1 || cek_PT_ttg5RB > 1 || 
							cek_PT_ttg1Pk > 1 || cek_PT_ttg2Pk > 1  || cek_PT_ttg3Pk > 1 || cek_PT_ttg4Pk > 1 || cek_PT_ttg5Pk > 1){
							errors.reject("", "Terdapat Duplikasi JENIS ASURANSI TAMBAHAN (RIDER) yang sama. Silahkan Diperiksa kembali");
						}
							
						if ((jmlRiderProvestaraTtg2 > 0 && jmlProvestaraTtg2 == 0) || (jmlRiderProvestaraTtg3 > 0 && jmlProvestaraTtg3 == 0)
							|| (jmlRiderProvestaraTtg4 > 0 && jmlProvestaraTtg4 == 0) || (jmlRiderProvestaraTtg5 > 0 && jmlProvestaraTtg5 == 0)){
							errors.reject("", "Untuk Mengambil Rider Jaminan Tambahan (RG,RB,PK) harus mengambil rider SMiLE Provestara Rawat Inap + Rawat Jalan terlebih dahulu");
						}					
													
						if(cocok.compareTo(juml_rid_prov) != 0){
								errors.reject("", "Jumlah Data Peserta dan Data Rider Provestara tidak sama");
						}
						
							if(flag_Prov_ttg1 != cek_PT_ttg1  ||flag_Prov_ttg2 != cek_PT_ttg2 || flag_Prov_ttg3 != cek_PT_ttg3 ||
								flag_Prov_ttg4 != cek_PT_ttg4 ||flag_Prov_ttg5 != cek_PT_ttg5 ){
								errors.reject("", "Data Peserta dan Data Rider Provestara tidak sama, silahkan cek kembali jenis ridernya");
								a.getDatausulan().setCekCompare(9);
							}else {
								a.getDatausulan().setCekCompare(0);
							}
						//type produk provestara yang di ambil oleh tertanggung tambahan harus sama dengan tertanggung utama (RG, PK)	
						if((cek_PT_ttg1RG==1) 
								&& 
								(((cek_PT_ttg2RG == 0 && cek_PT_ttg2RIJ == 1) || (cek_PT_ttg2RG == 1 && cek_PT_ttg2RIJ == 0))
								|| ((cek_PT_ttg3RG == 0 && cek_PT_ttg3RIJ == 1) || (cek_PT_ttg3RG == 1 && cek_PT_ttg3RIJ == 0))  
								|| ((cek_PT_ttg4RG == 0 && cek_PT_ttg4RIJ == 1) || (cek_PT_ttg4RG == 1 && cek_PT_ttg4RIJ == 0))
								|| ((cek_PT_ttg5RG == 0 && cek_PT_ttg5RIJ == 1) || (cek_PT_ttg5RG == 1 && cek_PT_ttg5RIJ == 0))   ))	{
							errors.reject("", "Type Rider Provestara Peserta Utama dengan Rider Tertanggung Tambahan  harus sama, silahkan cek kembali tipe rider RG untuk peserta utama dan peserta tambahan");
						}else if((cek_PT_ttg1RG==0) && ( cek_PT_ttg2RG == 1 || cek_PT_ttg3RG == 1 || cek_PT_ttg4RG == 1  || cek_PT_ttg5RG == 1) ){
							errors.reject("", "Type Rider Provestara Peserta Utama dengan Rider Tertanggung Tambahan harus sama, silahkan cek kembali tipe rider RG untuk peserta utama dan peserta tambahan");
						}																	
												
						if((cek_PT_ttg1Pk==1) 
								&& 
								(((cek_PT_ttg2Pk == 0 && cek_PT_ttg2RIJ == 1) || (cek_PT_ttg2Pk == 1 && cek_PT_ttg2RIJ == 0))
								|| ((cek_PT_ttg3Pk == 0 && cek_PT_ttg3RIJ == 1) || (cek_PT_ttg3Pk == 1 && cek_PT_ttg3RIJ == 0))  
								|| ((cek_PT_ttg4Pk == 0 && cek_PT_ttg4RIJ == 1) || (cek_PT_ttg4Pk == 1 && cek_PT_ttg4RIJ == 0))
								|| ((cek_PT_ttg5Pk == 0 && cek_PT_ttg5RIJ == 1) || (cek_PT_ttg5Pk == 1 && cek_PT_ttg5RIJ == 0))   ))	{
							errors.reject("", "Type Rider Provestara Peserta Utama dengan Rider Tertanggung Tambahan harus sama, silahkan cek kembali tipe rider PK untuk peserta utama dan peserta tambahan");
						}else if((cek_PT_ttg1Pk==0) && ( cek_PT_ttg2Pk == 1 || cek_PT_ttg3Pk == 1 || cek_PT_ttg4Pk == 1  || cek_PT_ttg5Pk == 1) ){
								errors.reject("", "Type Rider Provestara Peserta Utama dengan Rider Tertanggung Tambahan harus sama, silahkan cek kembali tipe rider PK untuk peserta utama dan peserta tambahan");
						}
							
																		
					}else{
						if(cocok.compareTo(juml_rid_kes) != 0){
							errors.reject("", "Jumlah Data Peserta dan Data Rider tidak sama");
						}
						
						if(cocok.compareTo(jml_flagJP) != 0){
								errors.reject("", "Data Peserta dan Data Rider tidak sama, silahkan cek kembali jenis ridernya");
								a.getDatausulan().setCekCompare(9);
						}else if(cocok.compareTo(jml_flagJP) == 0){
							a.getDatausulan().setCekCompare(0);
						}
					}
				}else{
					a.getDatausulan().setCekCompare(0);
				}
			}else{ // if(jumlah_peserta < 1 && jmlh_rider > 0){
				ArrayList test2 = a.getDatausulan().getDaftaRider();
				Integer jumlah_r = new Integer(test2.size());
			
					for (int k =0 ; k <jumlah_r.intValue();k++)
					{
						Datarider drr = (Datarider)test2.get(k);
						Integer x_jenis = drr.getJenis();
						if(x_jenis != 0){
							errors.reject("", "Mohon mengisi data peserta asuransi tambahan terlebih dahulu");
							a.getDatausulan().setCekCompare(9);
						}else{
							a.getDatausulan().setCekCompare(0);
						}
						
					}
			}

		if (a.getDatausulan().getJml_benef()==null){
			a.getDatausulan().setJml_benef(0);
		}
		
		if (a.getTertanggung().getJml_dth()==null){
			a.getTertanggung().setJml_dth(0);
		}
		
		if (a.getDatausulan().getDaftaRiderAddOn()==null){
			a.getDatausulan().setDaftaRiderAddOn(0);
		}
		
		if (a.getDatausulan().getDaftaRiderMedicalPlus()==null){
			a.getDatausulan().setDaftaRiderMedicalPlus(0);
		}
		if(jmlallMedPlus<=0 && jmlallRiderMedPlus>0){
			errors.reject("", "Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) harus mengambil rider SMiLE Medical+ Rawat Inap terlebih dahulu");
		}
				
		
		//lufi		
		if(jmlallMedPlus>0 && jmlallRiderMedPlus>0){
			a.getDatausulan().setDaftaRiderMedicalPlus(jmlallMedPlus);
			a.getDatausulan().setDaftaRiderAddOn(jmlallRiderMedPlus);
				if(jmlallRiderMedPlus>0){
					Integer i_flagRJ=0,i_flagRG=0,i_flagRB=0,i_flagPK=0;
					
					if((jmlMedPlusTtg1<=0 && jmlRiderMedPlusTtg1>0) || (jmlMedPlusTtg2<=0 && jmlRiderMedPlusTtg2>0) ||
						(jmlMedPlusTtg3<=0 && jmlRiderMedPlusTtg3>0)	||(jmlMedPlusTtg4<=0 && jmlRiderMedPlusTtg4>0) ||
						(jmlMedPlusTtg5<=0 && jmlRiderMedPlusTtg5>0)	||(jmlMedPlusTtg6<=0 && jmlRiderMedPlusTtg6>0)){
						
							errors.reject("", "MANFAAT TAMBAHAN  SMiLE Medical+ YANG DIPILIH TIDAK SAMA DENGAN JENIS PESERTA YANG DIAMBIL");
					}
					
					if(jmlMedPlusTtg1>0 && jmlRiderMedPlusTtg1>0){
						for (int i =0 ; i<a.getDatausulan().getJml_peserta();i++){
							PesertaPlus pesertaAdd=(PesertaPlus) (a.getDatausulan().getDaftarplus()).get(i);	
							for (int j =0 ; j<jmlMedPlusTtg1;j++){
								Datarider dtrdMedPlus=(Datarider) (a.getDatausulan().getDaftaRiderMedicalPlusTtg1()).get(j);
								if(pesertaAdd.getFlag_jenis_peserta()==dtrdMedPlus.getJenis()){	
										for (int k =0 ; k <jmlRiderMedPlusTtg1;k++){
											Datarider dtrdAddOn=(Datarider) (a.getDatausulan().getDaftaRiderAddOnTtg1()).get(k);
												if(dtrdMedPlus.getJenis()==dtrdAddOn.getJenis()){											
													if(dtrdAddOn.getLsdbs_number()>=1 && dtrdAddOn.getLsdbs_number()<=20)i_flagRJ=1;											
													if(dtrdAddOn.getLsdbs_number()>=21 && dtrdAddOn.getLsdbs_number()<=40)i_flagRG=1;
													if(dtrdAddOn.getLsdbs_number()>=41 && dtrdAddOn.getLsdbs_number()<=60)i_flagRB=1;
													if(dtrdAddOn.getLsdbs_number()>=61 && dtrdAddOn.getLsdbs_number()<=80)i_flagPK=1;
													if(dtrdMedPlus.getJenis()!=dtrdAddOn.getJenis())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Tertanggungnya Harus sama dengan Jaminan Utamanya");
													if(dtrdMedPlus.getFlag_jenis_Paketmedplus()!=dtrdAddOn.getFlag_jenis_Paketmedplus())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Paketnya Harus sama dengan Jaminan Utamanya");
													if(i_flagRJ<=0 && (i_flagRG>0 ||i_flagRB>0 ||i_flagPK>0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RG,RB,PK) Harus mengambil Jaminan Tambahan Rawat Jalan");										
													//if(i_flagPK>0 && (i_flagRB<=0 && pesertaAdd.getKelamin()==0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (PK) Harus mengambil Jaminan Tambahan Rawat Bersalin");
											   }
										 }
									}
								}
							}
					}
					
					if(jmlMedPlusTtg2>0 && jmlRiderMedPlusTtg2>0){
						i_flagRJ=0;i_flagRG=0;i_flagRB=0;i_flagPK=0;
						for (int i =0 ; i<a.getDatausulan().getJml_peserta();i++){
							PesertaPlus pesertaAdd=(PesertaPlus) (a.getDatausulan().getDaftarplus()).get(i);	
							for (int j =0 ; j<jmlMedPlusTtg2;j++){
								Datarider dtrdMedPlus=(Datarider) (a.getDatausulan().getDaftaRiderMedicalPlusTtg2()).get(j);
								if(pesertaAdd.getFlag_jenis_peserta()==dtrdMedPlus.getJenis()){	
										for (int k =0 ; k <jmlRiderMedPlusTtg2;k++){
											Datarider dtrdAddOn=(Datarider) (a.getDatausulan().getDaftaRiderAddOnTtg2()).get(k);
												if(dtrdMedPlus.getJenis()==dtrdAddOn.getJenis()){											
													if(dtrdAddOn.getLsdbs_number()>=1 && dtrdAddOn.getLsdbs_number()<=20)i_flagRJ=1;											
													if(dtrdAddOn.getLsdbs_number()>=21 && dtrdAddOn.getLsdbs_number()<=40)i_flagRG=1;
													if(dtrdAddOn.getLsdbs_number()>=41 && dtrdAddOn.getLsdbs_number()<=60)i_flagRB=1;
													if(dtrdAddOn.getLsdbs_number()>=61 && dtrdAddOn.getLsdbs_number()<=80)i_flagPK=1;
													if(dtrdMedPlus.getJenis()!=dtrdAddOn.getJenis())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Tertanggungnya Harus sama dengan Jaminan Utamanya");
													if(dtrdMedPlus.getFlag_jenis_Paketmedplus()!=dtrdAddOn.getFlag_jenis_Paketmedplus())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Paketnya Harus sama dengan Jaminan Utamanya");
													if(i_flagRJ<=0 && (i_flagRG>0 ||i_flagRB>0 ||i_flagPK>0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RG,RB,PK) Harus mengambil Jaminan Tambahan Rawat Jalan");										
													//if(i_flagPK>0 && (i_flagRB<=0 && pesertaAdd.getKelamin()==0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (PK) Harus mengambil Jaminan Tambahan Rawat Bersalin");
											   }
										 }
									}
								}
							}
					}
					
					if(jmlMedPlusTtg3>0 && jmlRiderMedPlusTtg3>0){
						i_flagRJ=0;i_flagRG=0;i_flagRB=0;i_flagPK=0;
						for (int i =0 ; i<a.getDatausulan().getJml_peserta();i++){
							PesertaPlus pesertaAdd=(PesertaPlus) (a.getDatausulan().getDaftarplus()).get(i);	
							for (int j =0 ; j<jmlMedPlusTtg3;j++){
								Datarider dtrdMedPlus=(Datarider) (a.getDatausulan().getDaftaRiderMedicalPlusTtg3()).get(j);
								if(pesertaAdd.getFlag_jenis_peserta()==dtrdMedPlus.getJenis()){	
										for (int k =0 ; k <jmlRiderMedPlusTtg3;k++){
											Datarider dtrdAddOn=(Datarider) (a.getDatausulan().getDaftaRiderAddOnTtg3()).get(k);
												if(dtrdMedPlus.getJenis()==dtrdAddOn.getJenis()){											
													if(dtrdAddOn.getLsdbs_number()>=1 && dtrdAddOn.getLsdbs_number()<=20)i_flagRJ=1;											
													if(dtrdAddOn.getLsdbs_number()>=21 && dtrdAddOn.getLsdbs_number()<=40)i_flagRG=1;
													if(dtrdAddOn.getLsdbs_number()>=41 && dtrdAddOn.getLsdbs_number()<=60)i_flagRB=1;
													if(dtrdAddOn.getLsdbs_number()>=61 && dtrdAddOn.getLsdbs_number()<=80)i_flagPK=1;
													if(dtrdMedPlus.getJenis()!=dtrdAddOn.getJenis())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Tertanggungnya Harus sama dengan Jaminan Utamanya");
													if(dtrdMedPlus.getFlag_jenis_Paketmedplus()!=dtrdAddOn.getFlag_jenis_Paketmedplus())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Paketnya Harus sama dengan Jaminan Utamanya");
													if(i_flagRJ<=0 && (i_flagRG>0 ||i_flagRB>0 ||i_flagPK>0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RG,RB,PK) Harus mengambil Jaminan Tambahan Rawat Jalan");										
													//if(i_flagPK>0 && (i_flagRB<=0 && pesertaAdd.getKelamin()==0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (PK) Harus mengambil Jaminan Tambahan Rawat Bersalin");
											   }
										 }
									}
								}
							}
					}
					
					if(jmlMedPlusTtg4>0 && jmlRiderMedPlusTtg4>0){
						i_flagRJ=0;i_flagRG=0;i_flagRB=0;i_flagPK=0;
						for (int i =0 ; i<a.getDatausulan().getJml_peserta();i++){
							PesertaPlus pesertaAdd=(PesertaPlus) (a.getDatausulan().getDaftarplus()).get(i);	
							for (int j =0 ; j<jmlMedPlusTtg4;j++){
								Datarider dtrdMedPlus=(Datarider) (a.getDatausulan().getDaftaRiderMedicalPlusTtg4()).get(j);
								if(pesertaAdd.getFlag_jenis_peserta()==dtrdMedPlus.getJenis()){	
										for (int k =0 ; k <jmlRiderMedPlusTtg4;k++){
											Datarider dtrdAddOn=(Datarider) (a.getDatausulan().getDaftaRiderAddOnTtg4()).get(k);
												if(dtrdMedPlus.getJenis()==dtrdAddOn.getJenis()){											
													if(dtrdAddOn.getLsdbs_number()>=1 && dtrdAddOn.getLsdbs_number()<=20)i_flagRJ=1;											
													if(dtrdAddOn.getLsdbs_number()>=21 && dtrdAddOn.getLsdbs_number()<=40)i_flagRG=1;
													if(dtrdAddOn.getLsdbs_number()>=41 && dtrdAddOn.getLsdbs_number()<=60)i_flagRB=1;
													if(dtrdAddOn.getLsdbs_number()>=61 && dtrdAddOn.getLsdbs_number()<=80)i_flagPK=1;
													if(dtrdMedPlus.getJenis()!=dtrdAddOn.getJenis())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Tertanggungnya Harus sama dengan Jaminan Utamanya");
													if(dtrdMedPlus.getFlag_jenis_Paketmedplus()!=dtrdAddOn.getFlag_jenis_Paketmedplus())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Paketnya Harus sama dengan Jaminan Utamanya");
													if(i_flagRJ<=0 && (i_flagRG>0 ||i_flagRB>0 ||i_flagPK>0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RG,RB,PK) Harus mengambil Jaminan Tambahan Rawat Jalan");										
													//if(i_flagPK>0 && (i_flagRB<=0 && pesertaAdd.getKelamin()==0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (PK) Harus mengambil Jaminan Tambahan Rawat Bersalin");
											   }
										 }
									}
								}
							}
					}
					
					if(jmlMedPlusTtg5>0 && jmlRiderMedPlusTtg5>0){
						i_flagRJ=0;i_flagRG=0;i_flagRB=0;i_flagPK=0;
						for (int i =0 ; i<a.getDatausulan().getJml_peserta();i++){
							PesertaPlus pesertaAdd=(PesertaPlus) (a.getDatausulan().getDaftarplus()).get(i);	
							for (int j =0 ; j<jmlMedPlusTtg5;j++){
								Datarider dtrdMedPlus=(Datarider) (a.getDatausulan().getDaftaRiderMedicalPlusTtg5()).get(j);
								if(pesertaAdd.getFlag_jenis_peserta()==dtrdMedPlus.getJenis()){	
										for (int k =0 ; k <jmlRiderMedPlusTtg5;k++){
											Datarider dtrdAddOn=(Datarider) (a.getDatausulan().getDaftaRiderAddOnTtg5()).get(k);
												if(dtrdMedPlus.getJenis()==dtrdAddOn.getJenis()){											
													if(dtrdAddOn.getLsdbs_number()>=1 && dtrdAddOn.getLsdbs_number()<=20)i_flagRJ=1;											
													if(dtrdAddOn.getLsdbs_number()>=21 && dtrdAddOn.getLsdbs_number()<=40)i_flagRG=1;
													if(dtrdAddOn.getLsdbs_number()>=41 && dtrdAddOn.getLsdbs_number()<=60)i_flagRB=1;
													if(dtrdAddOn.getLsdbs_number()>=61 && dtrdAddOn.getLsdbs_number()<=80)i_flagPK=1;
													if(dtrdMedPlus.getJenis()!=dtrdAddOn.getJenis())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Tertanggungnya Harus sama dengan Jaminan Utamanya");
													if(dtrdMedPlus.getFlag_jenis_Paketmedplus()!=dtrdAddOn.getFlag_jenis_Paketmedplus())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Paketnya Harus sama dengan Jaminan Utamanya");
													if(i_flagRJ<=0 && (i_flagRG>0 ||i_flagRB>0 ||i_flagPK>0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RG,RB,PK) Harus mengambil Jaminan Tambahan Rawat Jalan");										
													//if(i_flagPK>0 && (i_flagRB<=0 && pesertaAdd.getKelamin()==0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (PK) Harus mengambil Jaminan Tambahan Rawat Bersalin");
											   }
										 }
									}
							}
						}
					}
					if(jmlMedPlusTtg6>0 && jmlRiderMedPlusTtg6>0){
						i_flagRJ=0;i_flagRG=0;i_flagRB=0;i_flagPK=0;
						for (int i =0 ; i<a.getDatausulan().getJml_peserta();i++){
							PesertaPlus pesertaAdd=(PesertaPlus) (a.getDatausulan().getDaftarplus()).get(i);	
							for (int j =0 ; j<jmlMedPlusTtg6;j++){
								Datarider dtrdMedPlus=(Datarider) (a.getDatausulan().getDaftaRiderMedicalPlusTtg6()).get(j);
								if(pesertaAdd.getFlag_jenis_peserta()==dtrdMedPlus.getJenis()){	
										for (int k =0 ; k <jmlRiderMedPlusTtg1;k++){
											Datarider dtrdAddOn=(Datarider) (a.getDatausulan().getDaftaRiderAddOnTtg6()).get(k);
												if(dtrdMedPlus.getJenis()==dtrdAddOn.getJenis()){											
													if(dtrdAddOn.getLsdbs_number()>=1 && dtrdAddOn.getLsdbs_number()<=20)i_flagRJ=1;											
													if(dtrdAddOn.getLsdbs_number()>=21 && dtrdAddOn.getLsdbs_number()<=40)i_flagRG=1;
													if(dtrdAddOn.getLsdbs_number()>=41 && dtrdAddOn.getLsdbs_number()<=60)i_flagRB=1;
													if(dtrdAddOn.getLsdbs_number()>=61 && dtrdAddOn.getLsdbs_number()<=80)i_flagPK=1;
													if(dtrdMedPlus.getJenis()!=dtrdAddOn.getJenis())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Tertanggungnya Harus sama dengan Jaminan Utamanya");
													if(dtrdMedPlus.getFlag_jenis_Paketmedplus()!=dtrdAddOn.getFlag_jenis_Paketmedplus())errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RJ,RG,RB,PK) Tipe Paketnya Harus sama dengan Jaminan Utamanya");
													if(i_flagRJ<=0 && (i_flagRG>0 ||i_flagRB>0 ||i_flagPK>0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (RG,RB,PK) Harus mengambil Jaminan Tambahan Rawat Jalan");										
													//if(i_flagPK>0 && (i_flagRB<=0 && pesertaAdd.getKelamin()==0 ))errors.reject("", " Peserta NO: "+pesertaAdd.getNo_urut()+" Untuk Mengambil Rider Jaminan Tambahan (PK) Harus mengambil Jaminan Tambahan Rawat Bersalin");
											   }
										 }
									}
							}
						}
					}
					
			}	
		}

		/** HALAMAN DATA USULAN */
		}else if (halaman==4){
			
			Cmdeditbac a = (Cmdeditbac) cmd;
			int jmlh_penerima = Integer.parseInt(request.getParameter("jmlpenerima"));
			int jmlh_dth = ServletRequestUtils.getIntParameter(request, "jmldth", 0);
			
			String lsne_id;
			String msaw_first;
			String msaw_ket;
			String tgllhr;
			String blnhr;
			String thnhr;
			String lsre_id;
			String msaw_persen;
			String msaw_sex;
			String jumlah;
			String tgltarik;
			String blntarik;
			String thntarik;
			String keterangan;
			ArrayList<Benefeciary> b = new ArrayList<Benefeciary>();
			a.getDatausulan().setJml_benef(jmlh_penerima);
			ArrayList<RencanaPenarikan> c = new ArrayList<RencanaPenarikan>();
			a.getTertanggung().setJml_dth(jmlh_dth);
			
			//randy
			int jmlbaris = ServletRequestUtils.getIntParameter(request, "jmlbaris",0);
			
			if(jmlbaris>0){
				//TAMBAHAN dari BERTHO.. untuk mengatasi ahli waris yang hilang bila next kopy spaj 05/05/2009
				if(a.getDatausulan().getDaftabenef()!=null && !a.getPemegang().getStatus().equalsIgnoreCase("edit") ){
					if(jmlh_penerima==0 && !a.getDatausulan().getDaftabenef().isEmpty()){
						jmlh_penerima=a.getDatausulan().getDaftabenef().size();
//						jmlh_penerima=0;
					}
				}
			}else{
				jmlh_penerima=0;
			}
			
			if(a.getPemegang().getStatus().equalsIgnoreCase("edit") ){
				if(jmlh_penerima==0 && !a.getDatausulan().getDaftabenef().isEmpty()){
					jmlh_penerima=a.getDatausulan().getDaftabenef().size();
					/*jmlh_penerima=0;*/
				}
			}
			if (jmlh_penerima>0){
				for (int k = 1 ;k <= jmlh_penerima; k++){
					msaw_first = request.getParameter("benef.msaw_first"+k);
					tgllhr = request.getParameter("tgllhr"+k);
					blnhr = request.getParameter("blnhr"+k);
					thnhr = request.getParameter("thnhr"+k);
					lsne_id = request.getParameter("benef.lsne_id"+k);
					lsre_id =request.getParameter("benef.lsre_id"+k);
					msaw_sex =request.getParameter("benef.msaw_sex"+k);
					msaw_ket = request.getParameter("benef.msaw_ket"+k);
					String tanggal_lahir = null;
					if (msaw_first==null){
						msaw_first="";
					}
					if (msaw_ket==null){
						msaw_ket="";
					}
					
					tanggal_lahir = FormatString.rpad("0",tgllhr,2)+"/"+FormatString.rpad("0",blnhr,2)+"/"+thnhr;
					if ((tgllhr.trim().length()==0)||(blnhr.trim().length()==0)||(thnhr.trim().length()==0)){
						tanggal_lahir=null;
					}else{
						boolean cekk1= f_validasi.f_validasi_numerik(tgllhr);	
						boolean cekk2= f_validasi.f_validasi_numerik(blnhr);
						boolean cekk3= f_validasi.f_validasi_numerik(thnhr);		
						if ((cekk1==false) ||(cekk2==false) || (cekk3==false)){
							tanggal_lahir=null;
						}
					}
						
					Date tanggallahir = null;
					if (tanggal_lahir != null){
						tanggallahir = defaultDateFormat.parse(tanggal_lahir);
					}
					
					if (lsre_id==null){
						lsre_id="1";
					}
					msaw_persen=request.getParameter("benef.msaw_persen"+k);
					if (msaw_persen.trim().length()==0){
						msaw_persen="0";
					}else{
						msaw_persen=konteks.f_replace_persen(msaw_persen);

						boolean cekk1 = f_validasi.f_validasi_numerik1(msaw_persen);
						if (cekk1 == false){
							msaw_persen="0";
						}
					}
	
					Benefeciary bf = new Benefeciary();
					bf.setMsaw_first(msaw_first);
					bf.setMsaw_birth(tanggallahir);
					bf.setLsre_id(Integer.parseInt(lsre_id));
					bf.setMsaw_persen(Double.parseDouble(msaw_persen));
					bf.setMsaw_sex(Integer.parseInt(msaw_sex));
					bf.setLsne_id(Integer.parseInt(lsne_id));
					bf.setMsaw_ket(msaw_ket);
					b.add(bf);
				}
			}
			
			a.getDatausulan().setDaftabenef(b);
			
			if(a.getTertanggung().getDaftarDth()!=null && !a.getPemegang().getStatus().equalsIgnoreCase("edit") ){
				if(jmlh_dth==0 && !a.getTertanggung().getDaftarDth().isEmpty()){
					jmlh_dth=a.getTertanggung().getDaftarDth().size();
				}
			}
			
			if (jmlh_dth>0){
				for (int k = 1 ;k <= jmlh_dth; k++){
					jumlah = request.getParameter("dth.jumlah"+k);
					tgltarik = request.getParameter("tgltarik"+k);
					blntarik = request.getParameter("blntarik"+k);
					thntarik = request.getParameter("thntarik"+k);
					keterangan = request.getParameter("dth.keterangan"+k);
					String tanggal_penarikan = null;
					Double jumlah2 = 0.0;
					if (jumlah==null || jumlah=="" || jumlah.equals("0")){
						errors.reject("", "Harap menginput jumlah DTH terlebih dahulu");
					}else{
						jumlah2 = nf.parse(jumlah).doubleValue();
					}
					if(keterangan==null || keterangan==""){
						errors.reject("", "Harap menginput keterangan DTH terlebih dahulu");
					}
					
					tanggal_penarikan = FormatString.rpad("0",tgltarik,2)+"/"+FormatString.rpad("0",blntarik,2)+"/"+thntarik;
					if ((tgltarik.trim().length()==0)||(blntarik.trim().length()==0)||(thntarik.trim().length()==0)){
						tanggal_penarikan=null;
					}else{
						boolean cekk1= f_validasi.f_validasi_numerik(tgltarik);	
						boolean cekk2= f_validasi.f_validasi_numerik(blntarik);
						boolean cekk3= f_validasi.f_validasi_numerik(thntarik);		
						if ((cekk1==false) ||(cekk2==false) || (cekk3==false)){
							tanggal_penarikan=null;
						}
					}
						
					Date tanggalpenarikan = null;
					if (tanggal_penarikan != null){
						tanggalpenarikan = defaultDateFormat.parse(tanggal_penarikan);
					}else{
						errors.reject("", "Harap menginput tanggal penarikan DTH terlebih dahulu");
					}
					
					RencanaPenarikan rp = new RencanaPenarikan();
					rp.setTgl_penarikan(tanggalpenarikan);
					rp.setJumlah(jumlah2);
					rp.setKeterangan(keterangan);
					c.add(rp);
				}
			}
			
			a.getTertanggung().setDaftarDth(c);
			
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			
			if(currentUser.getJn_bank().intValue() == 3){
				if(a.getDatausulan().getDaftabenef().size()<=0){
					errors.reject("", "Harap menginput data Ahli Waris terlebih dahulu");
				}
			}
			
			String nama_produk="produk_asuransi.n_prod_"+FormatString.rpad("0", a.getDatausulan().getLsbs_id().toString(), 2);
			
			/**
			 * GABUNGKAN NO REKENING YANG DISPLIT
			 * bertho
			 */
			String mrc_no="";
			for (int i = 0; i < a.getRekening_client().getMrc_no_ac_split().length; i++) {
				if(a.getRekening_client().getMrc_no_ac_split()[i]!=null){
					mrc_no=mrc_no+a.getRekening_client().getMrc_no_ac_split()[i];
				}
			}
			a.getRekening_client().setMrc_no_ac(mrc_no);
			
			String mar_acc_no="";
			for (int i = 0; i < a.getAccount_recur().getMar_acc_no_split().length; i++) {
				if(a.getAccount_recur().getMar_acc_no_split()[i]!=null){
					mar_acc_no=mar_acc_no+a.getAccount_recur().getMar_acc_no_split()[i];
				}
			}
			a.getAccount_recur().setMar_acc_no(mar_acc_no);
			
			// DETAIL INVESTASI HALAMAN BARU
//			try{
//				Class aClass = Class.forName( nama_produk );
//				n_prod produk = (n_prod) aClass.newInstance();
//				a.getInvestasiutama().setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(produk.kode_flag));
//				a.getInvestasiutama().setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(produk.kode_flag));				
//			} catch (ClassNotFoundException e){
//				logger.error("ERROR :", e);
//			} catch (InstantiationException e) {
//				logger.error("ERROR :", e);
//			} catch (IllegalAccessException e) {
//				logger.error("ERROR :", e);
//			}
		
			Cmdeditbac detiledit = (Cmdeditbac) cmd;
			//Adrian: Iwan Surya case Rider ESCI Polis : 37217201800304
			int jmlh_rider = detiledit.getDatausulan().getDaftaRider().size();
			ArrayList testt = a.getDatausulan().getDaftaRider(); // 
			if (jmlh_rider>0){
				for (int k = 1 ;k <= jmlh_rider; k++)	{
					Datarider drr = (Datarider)testt.get(k-1);
					Integer kd_rd = drr.getLsbs_id();
					if(kd_rd != null){
					if(kd_rd==837 || kd_rd==813){
						drr.setMspr_persen(drr.getPersenUp());
						drr.setPersenUpx(drr.getPersenUp());
						//drr.setPersenUpx(up);
						//.setMspr_persen(up);
					}
					}
				}				
				
			}
			
			
			
//khusus produk HCP sekuritas
			if(detiledit.getDatausulan().getLsbs_id().intValue() == 171 && detiledit.getDatausulan().getLsdbs_number().intValue() == 2){
				detiledit.getDatausulan().setTipeproduk(12); //sinarmas sekuritas
				detiledit.getAgen().setMsag_id("016374"); //kode penutup : SIMAS SEKURITAS
				detiledit.getPemegang().setMspo_ao("016374");
				if(currentUser.getMsag_id_ao() != null) {
					if(!currentUser.getMsag_id_ao().equals("")) {
						detiledit.getPemegang().setMspo_ao(currentUser.getMsag_id_ao());
					}
				}
			}							
		if(currentUser.getJn_bank().intValue() == 2){
				if(detiledit.getDatausulan().getKodeproduk().equals("164")
						&& (detiledit.getDatausulan().getLsdbs_number()==2 || detiledit.getDatausulan().getLsdbs_number()==11 )){
					detiledit.getAgen().setMsag_id("021052"); //kode penutup : SIMAS STABIL LINK					
				}
				if(detiledit.getDatausulan().getKodeproduk().equals("142")
						&& detiledit.getDatausulan().getLsdbs_number()==2){
					detiledit.getAgen().setMsag_id("016409"); //kode penutup : SIMAS PRIMA					
				}
//				*SIMAS PRIMA SYARIAH
				if(detiledit.getDatausulan().getKodeproduk().equals("175")
						&& detiledit.getDatausulan().getLsdbs_number()==2){
					detiledit.getAgen().setMsag_id("901352"); //kode penutup : SIMAS PRIMA SYARIAH
				}
//				*SIMAS MAXI SAVE SYARIAH
				if((detiledit.getDatausulan().getKodeproduk().equals("182") && detiledit.getDatausulan().getLsdbs_number() == 13) ||
						   (detiledit.getDatausulan().getKodeproduk().equals("182") && detiledit.getDatausulan().getLsdbs_number() == 14) ||
						   (detiledit.getDatausulan().getKodeproduk().equals("182") && detiledit.getDatausulan().getLsdbs_number() == 15) ){
							detiledit.getAgen().setMsag_id("901353"); // *KODE PENUTUP : SIMAS MULTI INVEST SYARIAH
						}
// 				*SIMAS POWER LINK
				if((detiledit.getDatausulan().getKodeproduk().equals("120") && detiledit.getDatausulan().getLsdbs_number() == 10) ||
				   (detiledit.getDatausulan().getKodeproduk().equals("120") && detiledit.getDatausulan().getLsdbs_number() == 11) ||
				   (detiledit.getDatausulan().getKodeproduk().equals("120") && detiledit.getDatausulan().getLsdbs_number() == 12) ||
				   (detiledit.getDatausulan().getKodeproduk().equals("120") && detiledit.getDatausulan().getLsdbs_number() == 22) ||
				   (detiledit.getDatausulan().getKodeproduk().equals("120") && detiledit.getDatausulan().getLsdbs_number() == 23) ||
				   (detiledit.getDatausulan().getKodeproduk().equals("120") && detiledit.getDatausulan().getLsdbs_number() == 24) ){
					detiledit.getAgen().setMsag_id("024369"); // *KODE PENUTUP : SIMAS POWER LINK 
					
				/*	if(currentUser.getJn_bank()==2 && currentUser.getLus_bas()==2  ){
						detiledit.getPemegang().setMspo_ao(currentUser.getLus_msag_id());
					}*/
				}
			}
		}else if (halaman==5){
			Cmdeditbac detiledit = (Cmdeditbac) cmd;
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			if(currentUser.getJn_bank().intValue() == 2&!(currentUser.getCab_bank()==null?"":currentUser.getCab_bank()).trim().equals("SSS")){
				if (detiledit.getDatausulan().getKodeproduk().equals("164")||detiledit.getDatausulan().getKodeproduk().equals("142")||detiledit.getDatausulan().getKodeproduk().equals("120")||detiledit.getDatausulan().getKodeproduk().equals("175")||detiledit.getDatausulan().getKodeproduk().equals("182")){
					if(!detiledit.getAgen().getMsag_id().equals("021052") 
							&& detiledit.getDatausulan().getKodeproduk().equals("164")
							&& detiledit.getDatausulan().getLsdbs_number()==11){
						errors.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 021052");
					}
					if(!detiledit.getAgen().getMsag_id().equals("016409") 
							&& detiledit.getDatausulan().getKodeproduk().equals("142")
							&& detiledit.getDatausulan().getLsdbs_number()==2){
						errors.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 016409");
					}
//					*SIMAS PRIMA SYARIAH
					if(!detiledit.getAgen().getMsag_id().equals("901352") 
							&& detiledit.getDatausulan().getKodeproduk().equals("175")
							&& detiledit.getDatausulan().getLsdbs_number()==2){
						errors.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 901352");
					}
// 					*SIMAS POWER LINK
					if(!detiledit.getAgen().getMsag_id().equals("024369") 
							&& detiledit.getDatausulan().getKodeproduk().equals("120")
							&& (detiledit.getDatausulan().getLsdbs_number() == 10 || detiledit.getDatausulan().getLsdbs_number() == 11 || detiledit.getDatausulan().getLsdbs_number() == 12)){
						errors.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 024369");
					}
//					*MULTI INVEST SYARIAH
					if(!detiledit.getAgen().getMsag_id().equals("901353") 
							&& detiledit.getDatausulan().getKodeproduk().equals("182")
							&& (detiledit.getDatausulan().getLsdbs_number() == 13 || detiledit.getDatausulan().getLsdbs_number() == 14 || detiledit.getDatausulan().getLsdbs_number() == 15)){
						errors.reject("", "KODE PENUTUP harus diisi dengan KODE AGEN 901353");
					}
				}
			}	
			
			//hadiah
			int jmlh_hadiah = ServletRequestUtils.getIntParameter(request, "jmlhadiah", 0);
			int hadiah_standard = detiledit.getPemegang().getFlag_standard();
			
			detiledit.getPemegang().setUnit(jmlh_hadiah);
			ArrayList<Hadiah> hdh = new ArrayList<Hadiah>();
			if(hadiah_standard==1){
				Hadiah hadiah = new Hadiah();
				
				Hadiah lh_id = uwManager.selectLh_id(detiledit.getDatausulan().getMspr_premium());
				
				hadiah.mh_no = 1;
				hadiah.lhc_id = 8;
				hadiah.lh_id = lh_id.lh_id;
				hadiah.lspd_id = 84;
				hadiah.mh_alamat = detiledit.getPemegang().getAlamat_rumah();
				hadiah.mh_kota = detiledit.getPemegang().getKota_rumah();
				hadiah.mh_kodepos = detiledit.getPemegang().getKd_pos_rumah();
				hadiah.mh_telepon = detiledit.getPemegang().getArea_code_rumah()+" "+detiledit.getPemegang().getTelpon_rumah();
				hadiah.mh_quantity = 1;
				hadiah.create_id = Integer.parseInt(currentUser.getLus_id());
				hadiah.program_hadiah = 1;
				hadiah.mh_alamat_kirim = detiledit.getPemegang().getAlamat_rumah();
				hadiah.mh_kota_kirim = detiledit.getPemegang().getKota_rumah();
				hadiah.mh_kodepos_kirim = detiledit.getPemegang().getKd_pos_rumah();
				hadiah.flag_standard = 1;
				hadiah.lh_harga = lh_id.lh_harga;
					
				hdh.add(hadiah);
			}else{
				
				if (jmlh_hadiah>0){	
					for (int i = 1; i <= jmlh_hadiah; i++){
						String value = request.getParameter("hadiah.mh_no"+i);
						String qty = request.getParameter("hadiah.mh_quantity"+i);
						
						String lh_id = value.substring(0,value.indexOf("~"));
						String lh_harga = value.substring(value.indexOf("~")+1, value.length());
						
						Hadiah hadiah = new Hadiah();
						//hadiah.reg_spaj = "";
						hadiah.mh_no = i;
						hadiah.lhc_id = 8;
						hadiah.lh_id = Integer.parseInt(lh_id);
						hadiah.lspd_id = 84;
						hadiah.mh_alamat = detiledit.getPemegang().getAlamat_rumah();
						hadiah.mh_kota = detiledit.getPemegang().getKota_rumah();
						hadiah.mh_kodepos = detiledit.getPemegang().getKd_pos_rumah();
						hadiah.mh_telepon = detiledit.getPemegang().getArea_code_rumah()+" "+detiledit.getPemegang().getTelpon_rumah();
						hadiah.mh_quantity = Integer.parseInt(qty);
						hadiah.create_id = Integer.parseInt(currentUser.getLus_id());
						hadiah.program_hadiah = 1;
						hadiah.mh_alamat_kirim = detiledit.getPemegang().getAlamat_rumah();
						hadiah.mh_kota_kirim = detiledit.getPemegang().getKota_rumah();
						hadiah.mh_kodepos_kirim = detiledit.getPemegang().getKd_pos_rumah();
						hadiah.flag_standard = 0;
						hadiah.lh_harga = Double.parseDouble(lh_harga);
							
						hdh.add(hadiah);
					}
				}
			}
			detiledit.getPemegang().setDaftar_hadiah(hdh);
		}
		
	}
	
	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object cmd, BindException err)
			throws Exception {
		logger.debug("EditBacController : processCancel");
		return new ModelAndView("bac/editpemegangnew","cmd",this.formBackingObject(request));
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object cmd, BindException err)
			throws Exception {
		
		logger.debug("EditBacController : processFinish");
		
		Cmdeditbac detiledit = (Cmdeditbac)cmd;
		//Rahmayanti
		//Untuk memasukkan history edit di lst_ulangan
			String reg_spaj = detiledit.getPemegang().getReg_spaj();
			
			Integer edit_pemegang = detiledit.getPemegang().getEdit_pemegang();
			Integer edit_tertanggung = detiledit.getTertanggung().getEdit_tertanggung();
			Integer edit_dataUsulan = detiledit.getDatausulan().getEdit_dataUsulan();
			Integer edit_investasi = detiledit.getInvestasiutama().getEdit_investasi();
			Integer edit_agen = detiledit.getAgen().getEdit_agen();
			
			String kesalahan_pemegang = detiledit.getPemegang().getKriteria_kesalahan();
			String kesalahan_tertanggung = detiledit.getTertanggung().getKriteria_kesalahan();
			String kesalahan_usulan = detiledit.getDatausulan().getKriteria_kesalahan();
			String kesalahan_investasi = detiledit.getInvestasiutama().getKriteria_kesalahan();
			String kesalahan_agen = detiledit.getAgen().getKriteria_kesalahan();
		//-----------------------------------END------------------------------------------------	
			String spaj="";
			String status=detiledit.getPemegang().getStatus();
			String keterangan = detiledit.getDatausulan().getKeterangan_fund();
			//detiledit.getDatausulan().setIndeks_validasi(1);
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			
			String status_submit = detiledit.getDatausulan().getStatus_submit();
			if (status_submit == null)
			{
				status_submit="";
			}
			if (status.equalsIgnoreCase("edit"))
			{
				if (status_submit.equalsIgnoreCase("ulang"))
				{
					status="input";
					detiledit.getPemegang().setStatus(status);
				}
			}
			
			if (status.equalsIgnoreCase("input"))
			{
				String nospajtemp = bacManager.selectUsedMstSpajTemp(detiledit.getNo_temp());
				if(detiledit.getPemegang().getFlag_upload()!=null && !StringUtils.isEmpty(nospajtemp)){
					err.rejectValue("pemegang.reg_spaj","","No SPAJ Temp tersebut sudah pernah diinput.");
				}else{
					detiledit = this.bacManager.savingspajnew(cmd, err, "input", currentUser);
					spaj = detiledit.getPemegang().getReg_spaj();
					if(!spaj.equals("")) {
						File file = new File(detiledit.getPathFileTemp()+"\\bac.txt");
						if(file.exists()){
							file.delete();
							detiledit.setFileConfirm("");
						}
					}
				}
//				uwDao.updateMstInsuredTglAdmin(spaj, insuredNo, tgl, show);
//				elionsManager.prosesEditTanggalSpajAdmin(spaj,1,tanggalTerimaAdmin,keterangan);
////				params.put("success", "Berhasil Update Tanggal Terima Admin");
			}else{
				/**
				 * @author HEMILDA
				 * edit
				 * untuk yang sudah aksep tidak bisa diedit lagi
				 */
				Integer status_polis = this.elionsManager.selectPositionSpaj(detiledit.getPemegang().getReg_spaj());
				//if(status_polis==null) status_polis = 5;
				if (status_polis.intValue() != 5)
				{
					detiledit=this.bacManager.savingspajnew(cmd,err,"edit",currentUser);
					spaj = detiledit.getPemegang().getReg_spaj();
					if(spaj.equals("")) {
						request.getSession().setAttribute("dataInputSpaj", detiledit);
					}else{
						File file = new File(detiledit.getPathFileTemp()+"\\bac.txt");
						if(file.exists()){
							file.delete();
							detiledit.setFileConfirm("");
						}
						request.getSession().removeAttribute("dataInputSpaj");
					}
				}else{
					err.rejectValue("pemegang.reg_spaj","","Spaj ini sudah diaksep, tidak bisa diedit.");
				}
			}
		
			keterangan = detiledit.getDatausulan().getKeterangan_fund();
			Integer status_polis = detiledit.getHistory().getStatus_polis();
			Integer lspd_id = detiledit.getDatausulan().getLspd_id();
			Integer kodebisnis = detiledit.getDatausulan().getLsbs_id();
			Integer jml_peserta = detiledit.getDatausulan().getJml_peserta();
			
			//Rahmayanti
			//Untuk memasukkan history edit di lst_ulangan
			Integer lus_id = Integer.parseInt(currentUser.getLus_id());

			if(edit_pemegang==null)
				edit_pemegang = 0;
			if(edit_tertanggung==null)
				edit_tertanggung = 0;
			if(edit_dataUsulan==null)
				edit_dataUsulan = 0;
			if(edit_investasi==null)
				edit_investasi = 0;
			if(edit_agen==null)
				edit_agen = 0;
			
			if(edit_pemegang.equals(1)){
				String jenis ="DATA PEMEGANG POLIS";
				bacManager.insertLstUlangan3(reg_spaj, 1, kesalahan_pemegang, lus_id, jenis);										
			}
			
			if(edit_tertanggung.equals(1)){
				String jenis ="DATA TERTANGGUNG";
				bacManager.insertLstUlangan3(reg_spaj, 1, kesalahan_tertanggung, lus_id, jenis);										
			}
			
			if(edit_dataUsulan.equals(1)){
				String jenis ="DATA USULAN ASURANSI";
				bacManager.insertLstUlangan3(reg_spaj, 1, kesalahan_usulan, lus_id, jenis);										
			}
			
			if(edit_investasi.equals(1)){
				String jenis ="DATA DETIL INVESTASI"; 
				bacManager.insertLstUlangan3(reg_spaj, 1, kesalahan_investasi, lus_id, jenis);										
			}
			
			if(edit_agen.equals(1)){
				String jenis ="DATA DETIL AGEN";
				bacManager.insertLstUlangan3(reg_spaj, 1, kesalahan_agen, lus_id, jenis);										
			}
			//--------------------------------END----------------------------------------			
			HashMap<String, Comparable> m =new HashMap<String, Comparable>();
			m.put("nomorspaj",spaj);
			m.put("status",status);
			m.put("keterangan",keterangan);
			m.put("status_polis",status_polis);
			m.put("lspd_id",lspd_id);
			m.put("kodebisnis", kodebisnis);
			m.put("jml_peserta", jml_peserta);
			m.put("status_submit", status_submit);
			
			if(logger.isDebugEnabled())logger.debug("Input SPAJ: " + spaj + " dengan waktu " + ((System.currentTimeMillis()-this.accessTime)/1000) + " detik.");

			return new ModelAndView("bac/editsubmit",m);

	}

}