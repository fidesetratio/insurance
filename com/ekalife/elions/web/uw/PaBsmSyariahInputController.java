package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.spring.util.Email;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.parent.ParentFormController;

/**
 * 
 * @author Patar.Tambunan
 * BSIM Syariah will follow process like PA BSM
 */

public class PaBsmSyariahInputController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	private static String IDENTIFIER = "PABSMSY";//PABSMSY
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		initData(request);
		Pas pas = (Pas)cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Date nowDate = elionsManager.selectSysdate();
		if(pas.getLsre_id() == 1){
			pas.setMsp_full_name(pas.getMsp_pas_nama_pp());
			pas.setMsp_pas_tmp_lhr_tt(pas.getMsp_pas_tmp_lhr_pp());
			pas.setMsp_date_of_birth(pas.getMsp_pas_dob_pp());
			pas.setMsp_identity_no_tt(pas.getMsp_identity_no());
		};
		
				//PAS getMsp_no_rekening_autodebet
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_nama_pp", "","PAS : Nama Pemegang harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_tmp_lhr_pp", "","PAS : Tempat Lahir Pemegang harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_dob_pp", "","PAS : Tanggal Lahir Pemegang harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_full_name", "","PAS : Nama Tertanggung harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_identity_no", "","PAS : No. Identitas harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_tmp_lhr_tt", "","PAS : Tempat Lahir Tertanggung harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_date_of_birth", "","PAS : Tgl. Lahir Tertanggung harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_postal_code", "","PAS : Kode Pos harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_address_1", "","PAS : Alamat harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_phone_number", "","PAS : No. Telepon harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_mobile", "","PAS : No. HP harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_identity_no", "","PAS : No. Identitas harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_city", "","PAS : Kota harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "lscb_id", "","PAS : Pembayaran harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_flag_cc", "","PAS : Cara Bayar harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "no_va", "","PAS : Mohon Di cek No Va yang tersedia");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_pas_email", "","PAS : Mohon di provide Email");
				String msp_no_rekening_autodebet = ServletRequestUtils.getStringParameter(request, "msp_no_rekening_autodebet", null);
				if(msp_no_rekening_autodebet  == null){
					err.reject("","PAS : Mohon Untuk menyediakan rekening");
				}else{
					if(msp_no_rekening_autodebet.trim().equals("")){
						err.reject("","PAS : Mohon Untuk menyediakan rekening");
					}
				}
				
				Integer umurPp = 0;
				Integer umurTt = 0;
				try{
					f_hit_umur umr = new f_hit_umur();
					
					SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
					SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
					SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
					Date sysdate = elionsManager.selectSysdate();
					int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
					int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
					int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
					
					if(pas.getMsp_pas_dob_pp() != null){
						
						int tahun1 = Integer.parseInt(sdfYear.format(pas.getMsp_pas_dob_pp()));
						int bulan1 = Integer.parseInt(sdfMonth.format(pas.getMsp_pas_dob_pp()));
						int tanggal1 = Integer.parseInt(sdfDay.format(pas.getMsp_pas_dob_pp()));
						
						umurPp=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
					}
					if(pas.getMsp_date_of_birth() != null){
						int tahun1 = Integer.parseInt(sdfYear.format(pas.getMsp_date_of_birth()));
						int bulan1 = Integer.parseInt(sdfMonth.format(pas.getMsp_date_of_birth()));
						int tanggal1 = Integer.parseInt(sdfDay.format(pas.getMsp_date_of_birth()));
						umurTt=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
					}
				}catch(Exception e){
					
				}
				
				
		
				if(!(umurPp>=17 && umurPp<=85)){ //batas umur 17 s/d 85 tahun
					err.reject("","PAS : Umur pemegang minimal 17 tahun & maksimal 85 tahun");
				}
				if(!(umurTt>=1 && umurTt<=60)){ //batas 1 s/d 60 tahun
					err.reject("","PAS : Umur tertanggung minimal 1 tahun & maksimal 60 tahun");
				}
				
				
				
				if(!Common.isEmpty(pas.getMsp_pas_email())){
					try {
						InternetAddress.parse(pas.getMsp_pas_email().trim());
					} catch (AddressException e) {
						err.reject("","PAS : email tidak valid");
					} finally {
						if(!pas.getMsp_pas_email().trim().toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
							err.reject("","PAS : email tidak valid");
						}
					}
				}
				
				
				
				
				try{
					pas.setMsp_postal_code(pas.getMsp_postal_code().trim());
					int x = Integer.parseInt( pas.getMsp_postal_code());
				}catch(Exception e){
					err.reject("","PAS : Kode Pos harus diisi angka");
				}
				
				if(!Common.isEmpty(pas.getMsp_pas_phone_number())){
					if(!Common.validPhone(pas.getMsp_pas_phone_number().trim())){
						err.reject("","PAS : No. Telepon harus diisi angka");
					}
				}
				if(!Common.isEmpty(pas.getMsp_pas_phone_number())){
					try{
						String x = pas.getMsp_pas_phone_number().trim().replace("0", "").replace("-", "");
						if(Common.isEmpty(x)){
							err.reject("","PAS : No. Telepon tidak boleh diisi '0' atau '-'");
						}
					}catch(Exception e){
						err.reject("","PAS : No. Telepon tidak boleh diisi '0' atau '-'");
					}
				}
				
				if(!Common.isEmpty(pas.getMsp_mobile())) {
					if(!Common.validPhone(pas.getMsp_mobile().trim())) {
						err.reject("", "PAS : No. HP harus diisi angka");
					}
					
					if(pas.getMsp_mobile().trim().length() > 20) {
						err.reject("", "PAS : No. HP terlalu panjang, max 20 digit");
					}
				}
				
				
				
				
					//String lscb_id = ServletRequestUtils.getStringParameter(request, "lscb_id", null);
				//msp_no_rekening_autodebet
				 if(pas.getMsp_no_rekening_autodebet().length()!=10){
					 err.reject("", "PAS : Panjang Rekening adalah 10");
				 }
				
				
					String lrb_id = ServletRequestUtils.getStringParameter(request, "lrb_id", null);
					String reff_id = ServletRequestUtils.getStringParameter(request, "reff_id", null);
					
					
					if(lrb_id == null){
						err.reject("","AGEN : Mohon input agen penginputan");
					}else{
						String agent = bacManager.select_agen_reff_bii(lrb_id);
					
						
						form_agen d_agen= new form_agen(); 
						Agen agen = new Agen();
						String agen_hsl = "";
						agen = uwManager.select_detilagen2(agent);
						if(agen.getMsag_id() != null){
						//	agen_hsl = d_agen.sertifikasi_agen(pas.getMsag_id(), 0,agen.getMsag_sertifikat(),agen.getMsag_berlaku(), nowDate);
							pas.setMsag_id(agen.getMsag_id());
							pas.setLrb_id(Integer.parseInt(lrb_id));
							pas.setReff_id(Integer.parseInt(reff_id));
							
							
						}else{
							err.reject("","Agen : Kode Agen tidak berlaku");
						};
		
					}
					
					
					
				   pas.setLsbp_id("224");// default bank sinarmas syariah untuk semua produk
				   pas.setPanjang_no_rek(uwManager.select_panj_rek1(pas.getLsbp_id()));
				   
				   
				   if(pas.getMsp_flag_cc() != null){
					   	if(pas.getMsp_flag_cc() == 2){// cuma autodebet tabungan
					   		ValidationUtils.rejectIfEmptyOrWhitespace(err, "lsbp_id", "","REKENING : Bank harus diisi");
							ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_no_rekening_autodebet", "","REKENING : No. Rekening harus diisi");
						}else{
							err.reject("","REKENING (AUTODEBET) :Selain Tabungan metode pembayaran lain tidak kita support");
						}
				  	
				   
				   }
				   
				   
					String bank_pp1=pas.getLsbp_id();
					String nama_bank_rekclient="";
					if(bank_pp1!=null){
						Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);
						if (data1!=null){		
							nama_bank_rekclient = (String)data1.get("BANK_NAMA");
							pas.setLsbp_nama(nama_bank_rekclient);
						}
					}
					
					
					String bank_pp2=pas.getLsbp_id_autodebet();
					String nama_bank_rekclient_autodebet="";
					if(bank_pp2!=null){
						Map data1= (HashMap) this.elionsManager.select_bank2(bank_pp2);
						if (data1!=null){		
							nama_bank_rekclient_autodebet = (String)data1.get("BANK_NAMA");
							pas.setLsbp_nama_autodebet(nama_bank_rekclient_autodebet);
						}
						
						if("205".equals(pas.getProduct_code())){
							HashMap<String, Object> bank = uwManager.selectKotaBank(bank_pp2);
							if(bank != null) {
								pas.setMsp_rek_cabang((String) bank.get("CABANG"));
								pas.setMsp_rek_kota((String) bank.get("KOTA"));
							}
						}
					}
					
					if("205".equals(pas.getProduct_code())){
						if(pas.getMsp_rek_nama_autodebet() != null && !"".equals(pas.getMsp_rek_nama_autodebet())) {
							pas.setMsp_rek_nama(pas.getMsp_rek_nama_autodebet());
						}
						if(pas.getMsp_no_rekening_autodebet() != null && !"".equals(pas.getMsp_no_rekening_autodebet())) {
							pas.setMsp_no_rekening(pas.getMsp_no_rekening_autodebet());
						}
					}
					
		
	}
	
	

	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
    {

		Pas pas=(Pas)cmd;
		//User currentUser = (User) session.getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		map.put("user_id", lus_id);
		
		Cmdeditbac edit = new Cmdeditbac();
		
		if(pas.getReg_spaj() == null)pas.setReg_spaj("");
		if(pas.getPribadi() == null)pas.setPribadi(0);
		
		if("insert".equals(request.getParameter("kata"))){
				pas.setPremi(getPremi(pas) + "");
				pas.setMsp_premi(pas.getPremi());
				pas.setLus_id(lus_id);
				pas.setLus_login_name(user.getLus_full_name());
				pas.setJenis_pas(IDENTIFIER);
				pas.setNo_kartu(null);
			
				if("205".equals(pas.getProduct_code())) {
					pas.setNo_sertifikat("");
					// Jika reff_id nya kosong, copy dari lrb_id
					if(pas.getReff_id() == null)
						pas.setReff_id(pas.getLrb_id());
				}
			pas = uwManager.insertPaTemp(pas, user);
			String no_va = pas.getNo_va();
			String no_temp = pas.getMsp_fire_id();
			bacManager.update_no_va(no_va, no_temp);
			
			if(pas.getStatus() != null){
				if(pas.getStatus() == 1){
					request.setAttribute("successMessage","proses insert gagal");
				}else if(pas.getStatus() == 2){
					request.setAttribute("successMessage","proses transfer gagal");
				}else{
				
					
					Integer msp_id = uwManager.selectGetPasIdFromFireId(pas.getMsp_fire_id());
					request.setAttribute("msp_id",msp_id);
					request.setAttribute("successMessage","insert sukses");
				}
			};
			
		}
		
		initData(request);
		return new ModelAndView(
        "uw/pa_bsm_syariah_input").addObject("cmd",pas);

    }
	
	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
		logger.debug("EditBacController : initBinder");
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	private void initData(HttpServletRequest request){
		
		    HttpSession session = request.getSession();
			User currentUser = (User) session.getAttribute("currentUser");
		
			String msp_id=ServletRequestUtils.getStringParameter(request, "msp_id",null);
			String input_type=ServletRequestUtils.getStringParameter(request, "input_type",null);
			Map<String, Object> refData = new HashMap<String, Object>();
			request.setAttribute("kode_nama_list",DroplistManager.getInstance().get("KODE_NAMA.xml","ID",request));
			request.setAttribute("kode_alamat_list",DroplistManager.getInstance().get("KODE_ALAMAT.xml","ID",request));
			request.setAttribute("kode_okupasi_list",DroplistManager.getInstance().get("KODE_OKUPASI.xml","ID",request));
			request.setAttribute("kode_obyek_sekitar_list",DroplistManager.getInstance().get("KODE_OBYEK_SEKITAR.xml","ID",request));
			request.setAttribute("carabayar_pas",DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request));
			request.setAttribute("autodebet_pas",DroplistManager.getInstance().get("AUTODEBET_PAS.xml","ID",request));
			input_type = "default";
			request.setAttribute("autodebet_pas", new ArrayList());
			Map<String,String> relasi = new HashMap<String,String>();
			relasi.put("ID", "1");
			relasi.put("RELATION", "DIRI SENDIRI");
			Map<String,String> produk = new HashMap<String,String>();
			List<Map> produkList = new ArrayList<Map>();
			produk.put("ID", "9");
			produk.put("PRODUK", "PAS SYARIAH PERDANA");
			produkList.add(produk);
			produk = new HashMap<String,String>();
			produk.put("ID", "10");
			produk.put("PRODUK", "PAS SYARIAH SINGLE");
			produkList.add(produk);
			produk = new HashMap<String,String>();
			produk.put("ID", "11");
			produk.put("PRODUK", "PAS SYARIAH CERIA");
			produkList.add(produk);
			produk = new HashMap<String,String>();
			produk.put("ID", "12");
			produk.put("PRODUK", "PAS SYARIAH IDEAL");
			produkList.add(produk);
			List<Map> relasiList = new ArrayList<Map>();
			relasiList.add(relasi);
			request.setAttribute("relasi_pas",relasiList);
			request.setAttribute("produk_pa_bsm",produkList);			
			request.setAttribute("sumber_pendanaan",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request));
			request.setAttribute("bidang_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request));
			request.setAttribute("pekerjaan",DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request));
			
			
			
			Map<String,String> produk_up = new HashMap<String,String>();
			List<Map> produkUpList = new ArrayList<Map>();
			produk_up = new HashMap<String,String>();
			produk_up.put("VALUE", "100000000");
			produk_up.put("LABEL", "Rp 100.000.000,-");
			produkUpList.add(produk_up);
			
			produk_up = new HashMap<String,String>();
			produk_up.put("VALUE", "200000000");
			produk_up.put("LABEL", "Rp 200.000.000,-");
			produkUpList.add(produk_up);
			
			produk_up = new HashMap<String,String>();
			produk_up.put("VALUE", "50000000");
			produk_up.put("LABEL", "Rp 50.000.000,-");
			produkUpList.add(produk_up);
			request.setAttribute("produk_pa_up_bsm",produkUpList);
			
			
			Map<String,String> caraBayarTemp = new HashMap<String,String>();
			List<Map> caraBayar = new ArrayList<Map>();
			caraBayarTemp = new HashMap<String,String>();
			caraBayarTemp.put("VALUE", "6");
			caraBayarTemp.put("LABEL", "BULANAN");
			caraBayar.add(caraBayarTemp);
			
			
			caraBayarTemp = new HashMap<String,String>();
			caraBayarTemp.put("VALUE", "2");
			caraBayarTemp.put("LABEL", "SEMESTERAN");
			caraBayar.add(caraBayarTemp);
			
			caraBayarTemp = new HashMap<String,String>();
			caraBayarTemp.put("VALUE", "3");
			caraBayarTemp.put("LABEL", "TAHUNAN");
			caraBayar.add(caraBayarTemp);
			
			caraBayarTemp = new HashMap<String,String>();
			caraBayarTemp.put("VALUE", "1");
			caraBayarTemp.put("LABEL", "TRIWULAN");
			caraBayar.add(caraBayarTemp);
			
			request.setAttribute("carabayar", caraBayar);
			
			
			HashMap<String, Object> autodebet = new HashMap<String, Object>();
			autodebet.put("ID", "2");
			autodebet.put("AUTODEBET", "TABUNGAN");
			ArrayList<HashMap<String, Object>> autodebetList = new ArrayList<HashMap<String, Object>>();
			autodebetList.add(autodebet);
			request.setAttribute("autodebet_pas", autodebetList);
		
			
			
			
			
			
		
			
		
		
	}
	
	protected Object formBackingObject(HttpServletRequest request){
		Pas pas=new Pas();
		initData(request); // init form
		
		
		
		pas.setMsp_flag_cc(2); // cara bayar tabungan
		
		String no_va = bacManager.select_bsim_no_va_syariah();
		if(no_va != null){
		if(!no_va.equals(""))
			pas.setNo_va(no_va); // no_va 
		}else{
			pas.setNo_va("");
		}
		pas.setPribadi(0); // set apakah untuk pribadi atautidak
		pas.setLscb_id(3); // cara bayar tahunan
		pas.setLsbp_id("224");
		pas.setProduct_code("205");
		
		

		String bank_pp1=pas.getLsbp_id();
		String nama_bank_rekclient="";
		if(bank_pp1!=null){
			Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);
			if (data1!=null){		
				nama_bank_rekclient = (String)data1.get("BANK_NAMA");
				pas.setLsbp_nama(nama_bank_rekclient);
			}
		};
		
		String bank_pp2=pas.getLsbp_id_autodebet();
		String nama_bank_rekclient_autodebet="";
		if(bank_pp2!=null){
			Map data1= (HashMap) this.elionsManager.select_bank2(bank_pp2);
			if (data1!=null){		
				nama_bank_rekclient_autodebet = (String)data1.get("BANK_NAMA");
				pas.setLsbp_nama_autodebet(nama_bank_rekclient_autodebet);
			}
		};
		
		
		
		
		if(pas.getLsbp_id() != null)
		pas.setPanjang_no_rek(uwManager.select_panj_rek1(pas.getLsbp_id()));
		
		Date end_date = elionsManager.selectSysdate();
		end_date.setYear(end_date.getYear()+1);
		end_date.setDate(end_date.getDate()-1);
		
		pas.setMsp_pas_beg_date(elionsManager.selectSysdate());
		pas.setMsp_pas_end_date(end_date);		
		pas.setInput_type("default");
		
		
		return pas;
	}
	
	
	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		boolean cek_kartu = ServletRequestUtils.getBooleanParameter(request, "cek_kartu", false);
		if(cek_kartu)
			return true;
		
		return false;
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Pas pas = (Pas) command;
		String input_type = ServletRequestUtils.getStringParameter(request, "input_type", null);
		pas.setInput_type(input_type);
	//	validasiKartu(request, pas, errors);
		if(!errors.hasErrors()) {
			if("typeafree".equals(input_type))
				request.setAttribute(
					"msgKartu", "Produk : PAS Plan A Free" + 
					"\\nUP : 50000000\\nPremi : 0\\nCara Bayar : Free"
				);
			else {
				String produk = ((ArrayList<HashMap>) request.getAttribute("produk_pa_bsm")).get(0).get("PRODUK").toString();
				String up = ((ArrayList<HashMap>) request.getAttribute("produk_pa_up_bsm")).get(0).get("VALUE").toString();
				String premi = request.getAttribute("premi").toString();
				request.setAttribute(
					"msgKartu", 
					"Kartu dapat digunakan.\\nNo. Kartu : " + pas.getNo_kartu() + 
					"\\nProduk : " + produk + 
					"\\nUP : " + up + 
					"\\nPremi : " + premi + 
					"\\nCara Bayar : AUTODEBET TABUNGAN"
				);
			}
		}
	}
	

	
	public int getPremi(Pas pas){
		Double premi;
		Integer cb =pas.getLscb_id();
		int product = pas.getProduk();
		String product_code = pas.getProduct_code();
		
		Double up = 0.0;
		
		
		if(product_code.equals("205")){
			if(product == 9){
			  up = new Double("100000000");
   			  pas.setMsp_up("100000000");
   			  
			}else if(product == 10){
			  up = new Double("50000000");
			  pas.setMsp_up("50000000");
   			  
			}else if(product == 11){
			 up = new Double("100000000");
			  pas.setMsp_up("100000000");
   			  
			}else if(product == 12){
			 up = new Double("200000000");
			 pas.setMsp_up("200000000");
			}
		}
		
		
		double rate = 0.00;
		if(cb == 3 ){
			rate = 1.0;
		}else if(cb == 2){
			rate = 0.525;
		}else if(cb == 1){
			rate = 0.27;
		}else if(cb == 6){
			rate = 0.1;
		}
		premi = (up * rate) / new Double(1000);
		premi = Math.ceil(premi);
		return premi.intValue();
	}
	

}