package com.ekalife.elions.web.uw;

import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentFormController;

import id.co.sinarmaslife.std.spring.util.Email;

public class InputAsuransiKebakaranController extends ParentFormController{
	private long accessTime;
	protected final Log logger = LogFactory.getLog(getClass());
	private Email email;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		
		Pas pas=(Pas)cmd;
		
		if(pas.getMsp_fire_insured_addr_envir() != 5){
			pas.setMsp_fire_ins_addr_envir_else("");
		}else{
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_ins_addr_envir_else", "","Lainnya harus diisi");
		}
		
		if(!"L".equals(pas.getMsp_fire_okupasi())){
			pas.setMsp_fire_okupasi_else("");
		}else{
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_okupasi_else", "","Lainnya harus diisi");
		}
		
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msag_id", "","Kode Agen harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_no_rekening", "","No Rekening harus diisi");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_code_name", "","Kode Nama 1 harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_name", "","Nama harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_identity", "","No Identitas harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_occupation", "","Jenis Pekerjaan harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_type_business", "","Bidang Usaha harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_source_fund", "","Sumber Dana harus diisi");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_address_1", "","Alamat harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_phone_number", "","No Telp harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_email", "","Email harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_okupasi", "","Kode Okupasi harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_postal_code", "","Kode Pos harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_mobile", "","No HP harus diisi");
		
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr_code", "","Kode Alamat Tertanggung harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr", "","Alamat Tertanggung harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_postal_code", "","Kode Pos Tertanggung harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr_no", "","No Rumah Tertanggung harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_insured_addr_envir", "","Kode Object Sekitar tertanggung harus diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "msp_fire_date_of_birth2", "","Tanggal Lahir harus diisi");
		
		//request.setAttribute("addError", "addError");
				//err.reject("");
	}
	
//	protected ModelAndView onAddnewrow( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
//    {
//		CmdInputBlacklist detiledit = (CmdInputBlacklist) cmd;
//		
//		return new ModelAndView(
//        "uw/input_blacklist_customer").addObject("cmd",detiledit);
//    }

	protected ModelAndView onSubmit( HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors ) throws Exception
    {

		Pas pas=(Pas)cmd;
		//User currentUser = (User) session.getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		Integer flagNew = 1;
		map.put("user_id", lus_id);
		
		if(request.getParameter("kata")!=null){
			Date beg_date = elionsManager.selectSysdate();
			Date end_date = elionsManager.selectSysdate();
			end_date.setYear(end_date.getYear()+1);
			end_date.setDate(end_date.getDate()-1);
			pas.setMsp_pas_beg_date(beg_date);
			pas.setMsp_pas_end_date(end_date);
			pas.setLus_id(lus_id);
			pas.setLspd_id(2);
			pas.setLssp_id(10);
			pas.setMsp_kode_sts_sms("00");
			pas.setLus_login_name(user.getLus_full_name());
			uwManager.updatePas(pas);
			schedulerPas();
		}
		
		CheckSum checkSum = new CheckSum();
		Cmdeditbac edit= new Cmdeditbac();
		edit.setPemegang(new Pemegang());
		edit.setTertanggung(new Tertanggung());
		edit.setDatausulan(new Datausulan());
		edit.setAgen(new Agen());
		edit.setAddressbilling(new AddressBilling());
		edit.setRekening_client(new Rekening_client());
		
//		LSPD_ID, 
//		LSSP_ID, 
//		MSP_PAS_ACCEPT_DATE, 
		
//		MSP_FIRE_ACCEPT_DATE, 
//		MSP_FIRE_BEG_DATE, 
//		MSP_FIRE_END_DATE, 
//		MSP_FIRE_POLICY_NO, 
//		MSP_FIRE_CODE_NAME, 
//		MSP_FIRE_NAME, 
//		MSP_FIRE_IDENTITY, 
//		MSP_FIRE_OCCUPATION, 
//		MSP_FIRE_TYPE_BUSINESS, 
//		MSP_FIRE_SOURCE_FUND, 
//		MSP_FIRE_ADDRESS_1, 
//		MSP_FIRE_POSTAL_CODE, 
//		MSP_FIRE_PHONE_NUMBER, 
//		MSP_FIRE_MOBILE, 
//		MSP_FIRE_EMAIL, 
//		MSP_FIRE_OKUPASI, 
//		MSP_FIRE_INSURED_ADDR_CODE, 
//		MSP_FIRE_INSURED_ADDR, 
//		MSP_FIRE_INSURED_POSTAL_CODE, 
//		MSP_FIRE_INSURED_ADDR_NO, 
//		MSP_FIRE_INSURED_ADDR_ENVIR, 
//		MSP_FIRE_DATE_OF_BIRTH, 
//		MSP_NO_REKENING, 
//		MSAG_ID
		
		// agen
		Map agentMap = elionsManager.selectagenpenutup(pas.getMsag_id());
		String regionid = (String) agentMap.get("REGIONID");
		edit.getAgen().setMsag_id((String) agentMap.get("ID"));
		edit.getAgen().setMcl_first((String) agentMap.get("NAMA"));
		edit.getAgen().setKode_regional(regionid);
		edit.getAgen().setLca_id(regionid.substring(0, 2));
		edit.getAgen().setLwk_id(regionid.substring(2, 4));
		edit.getAgen().setLsrg_id(regionid.substring(4, 6));
		
		// pemegang
		edit.getPemegang().setNo_hp(pas.getMsp_mobile());
		edit.getPemegang().setNo_hp2(pas.getMsp_mobile2());
		edit.getPemegang().setMcl_first(pas.getMsp_full_name());
		edit.getPemegang().setMspe_no_identity(pas.getMsp_identity_no());
		edit.getPemegang().setMspe_date_birth(pas.getMsp_date_of_birth());
		edit.getPemegang().setAlamat_rumah(pas.getMsp_address_1());
		edit.getPemegang().setKota_rumah(pas.getMsp_city());
		edit.getPemegang().setKd_pos_rumah(pas.getMsp_postal_code());
		edit.getPemegang().setReg_spaj(pas.getReg_spaj());
		edit.getPemegang().setMspo_policy_no(pas.getMspo_policy_no());
		edit.getPemegang().setLus_id(pas.getLus_id());
		edit.getPemegang().setLsre_id(1);
		edit.getPemegang().setMspo_ao("");
		edit.getPemegang().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
		edit.getPemegang().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
		edit.getPemegang().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
		
		// address billing
		edit.getAddressbilling().setNo_hp(pas.getMsp_mobile());
		edit.getAddressbilling().setNo_hp2(pas.getMsp_mobile2());
		edit.getAddressbilling().setMsap_contact(pas.getMsp_full_name());
		edit.getAddressbilling().setMsap_address(pas.getMsp_address_1());
		edit.getAddressbilling().setKota(pas.getMsp_city());
		edit.getAddressbilling().setMsap_zip_code(pas.getMsp_postal_code());
		edit.getAddressbilling().setReg_spaj(pas.getReg_spaj());
		edit.getAddressbilling().setRegion(regionid);
		edit.getAddressbilling().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
		edit.getAddressbilling().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
		edit.getAddressbilling().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
		
		// rekening client
		edit.getRekening_client().setNotes("PAS");
		edit.getRekening_client().setLsbp_id(pas.getLsbp_id());
		edit.getRekening_client().setMrc_nama(pas.getMsp_rek_nama().toUpperCase());
		edit.getRekening_client().setMrc_cabang(pas.getMsp_rek_cabang().toUpperCase());
		edit.getRekening_client().setMrc_kota(pas.getMsp_rek_kota().toUpperCase());
		edit.getRekening_client().setNo_account(pas.getMsp_no_rekening());
		edit.getRekening_client().setMrc_no_ac(pas.getMsp_no_rekening());
		edit.getRekening_client().setMrc_no_ac_lama(pas.getMsp_no_rekening());
		edit.getRekening_client().setMrc_jenis(2);// rek client
		
		// data usulan
		String lsdbs_number = uwManager.selectCekPin(pas.getPin(), 0);
		if(lsdbs_number == null)lsdbs_number = "x";
		int mspo_pay_period = 1;
		if(pas.getLscb_id() == 6){
			mspo_pay_period = 12;
		}else if(pas.getLscb_id() == 3){
			mspo_pay_period = 1;
		}else if(pas.getLscb_id() == 2){
			mspo_pay_period = 2;
		}else if(pas.getLscb_id() == 1){
			mspo_pay_period = 3;
		}
		Double mspr_tsi = null;
		if("01".equals(lsdbs_number)){
			mspr_tsi = new Double(100000000);
		}else if("02".equals(lsdbs_number)){
			mspr_tsi = new Double(50000000);
		}else if("03".equals(lsdbs_number)){
			mspr_tsi = new Double(100000000);
		}else if("04".equals(lsdbs_number)){
			mspr_tsi = new Double(200000000);
		}else{
			mspr_tsi = new Double(100000000);
		}
		Double mspr_premium = null;
		if("01".equals(lsdbs_number) && pas.getLscb_id() == 3){
			mspr_premium = new Double(150000);
		}else if("01".equals(lsdbs_number) && pas.getLscb_id() == 6){
			mspr_premium = new Double(15000);
		}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 3){
			mspr_premium = new Double(300000);
		}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 6){
			mspr_premium = new Double(30000);
		}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 3){
			mspr_premium = new Double(500000);
		}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 6){
			mspr_premium = new Double(50000);
		}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 3){
			mspr_premium = new Double(900000);
		}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 6){
			mspr_premium = new Double(90000);
		}else{
			mspr_premium = new Double(0);
		}
		edit.getDatausulan().setIsBungaSimponi(0);
		edit.getDatausulan().setIsBonusTahapan(0);
		edit.getDatausulan().setMste_medical(0);
		edit.getDatausulan().setLscb_id(pas.getLscb_id());
		edit.getDatausulan().setMspr_tsi(mspr_tsi);
		edit.getDatausulan().setMspr_premium(new Double(0));
		edit.getDatausulan().setMspr_discount(new Double(0));
		edit.getDatausulan().setMste_flag_cc(0);
		edit.getDatausulan().setFlag_worksite(0);
		edit.getDatausulan().setFlag_account(2);// rek client
		edit.getDatausulan().setKode_flag(0);//default
		edit.getDatausulan().setMspo_beg_date(pas.getMsp_pas_beg_date());
		edit.getDatausulan().setMspo_end_date(pas.getMsp_pas_end_date());
		edit.getDatausulan().setMste_beg_date(pas.getMsp_pas_beg_date());
		edit.getDatausulan().setMste_end_date(pas.getMsp_pas_end_date());
		edit.getDatausulan().setMspo_ins_period(1);
		edit.getDatausulan().setMspr_ins_period(1);
		edit.getDatausulan().setMspo_pay_period(mspo_pay_period);
		edit.getDatausulan().setFlag_jenis_plan(5);//PAS
		edit.getDatausulan().setLsbs_id(187);//PAS-ekatest
		edit.getDatausulan().setLsdbs_number(Integer.parseInt(lsdbs_number));//PAS-ekatest
		edit.getDatausulan().setKurs_p("01");
		edit.getDatausulan().setLku_id("01");
		
		
		// tertanggung
		edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
		edit.getTertanggung().setNo_hp2(pas.getMsp_mobile2());
		edit.getTertanggung().setMcl_first(pas.getMsp_full_name());
		edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no());
		edit.getTertanggung().setMspe_date_birth(pas.getMsp_date_of_birth());
		edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
		edit.getTertanggung().setKota_rumah(pas.getMsp_city());
		edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
		edit.getTertanggung().setMste_age(pas.getMsp_age());
		edit.getTertanggung().setUsiattg(pas.getMsp_age());
		edit.getTertanggung().setDanaa("");
		edit.getTertanggung().setDanaa2("");
		edit.getTertanggung().setMkl_pendanaan("");
		edit.getTertanggung().setMkl_smbr_penghasilan("");
		edit.getTertanggung().setMkl_kerja("");
		edit.getTertanggung().setMkl_industri("");
		edit.getTertanggung().setKerjaa("");
		edit.getTertanggung().setKerjab("");
		edit.getTertanggung().setIndustria("");
		edit.getTertanggung().setTujuana("");
		edit.getTertanggung().setMkl_tujuan("");
		edit.getTertanggung().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
		edit.getTertanggung().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
		edit.getTertanggung().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
		edit.getTertanggung().setReg_spaj(pas.getReg_spaj());
		edit.getTertanggung().setMspo_policy_no(pas.getMspo_policy_no());
		edit.getTertanggung().setLus_id(pas.getLus_id());
		
		int hasil = 0;
		//edit=this.uwManager.savingspajpas(edit,errors,"input",user);
		//edit=this.uwManager.prosesPas(request, edit,errors,"input",user,errors);
		
//		if(!"".equals(edit.getPemegang().getReg_spaj())){
//			
//			// reas
//			
//			Reas reas=new Reas();
//			reas.setLstbId(new Integer(1));
//			String las_reas[]=new String[3];
//			las_reas[0]="Non-Reas";
//			las_reas[1]="Treaty";
//			las_reas[2]="Facultative";
//			reas.setCurrentUser((User) request.getSession().getAttribute("currentUser"));   
//			reas.setSpaj(edit.getPemegang().getReg_spaj());
//			
//			Map mPosisi=elionsManager.selectF_check_posisi(reas.getSpaj());
//			Integer lspdIdTemp=(Integer)mPosisi.get("LSPD_ID");
//			reas.setLspdId(lspdIdTemp);
//			String lspdPosTemp=(String)mPosisi.get("LSPD_POSITION");
//			//produk asm
//			Map mMap=uwManager.selectDataUsulan(reas.getSpaj());
//			Integer lsbsId=(Integer)mMap.get("LSBS_ID");
//
//			//tertanggung
//			Map mTertanggung=elionsManager.selectTertanggung(reas.getSpaj());
//			reas.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
//			reas.setMsteInsured((String)mTertanggung.get("MCL_ID"));
//			reas.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
//			//
//			Map mStatus=elionsManager.selectWfGetStatus(reas.getSpaj(),reas.getInsuredNo());
//			reas.setLiAksep((Integer)mStatus.get("LSSA_ID"));
//			reas.setLiReas((Integer)mStatus.get("MSTE_REAS"));
//			if (reas.getLiAksep()==null) 
//				reas.setLiAksep( new Integer(1));
//			
//			
//			//dw1 //pemegang
//			Policy policy=elionsManager.selectDw1Underwriting(reas.getSpaj(),reas.getLspdId(),reas.getLstbId());
//			if(policy!=null){
//				reas.setMspoPolicyHolder(policy.getMspo_policy_holder());
//				reas.setNoPolis(policy.getMspo_policy_no());
//				reas.setInsPeriod(policy.getMspo_ins_period());
//				reas.setPayPeriod(policy.getMspo_pay_period());
//				reas.setLkuId(policy.getLku_id());
//				reas.setUmurPp(policy.getMspo_age());
//				reas.setLcaId(policy.getLca_id());
//				reas.setLcaId(policy.getLca_id());
//				reas.setMste_kyc_date(policy.getMste_kyc_date());
//			}
//			
//			//reas
//			
//			elionsManager.prosesReasIndividuNew(reas, BindUtils.bind(request, reas, "cmd"));
//			
//			//billing
//			List lsDp = new ArrayList();
//			uwManager.wf_ins_bill(edit.getPemegang().getReg_spaj(), new Integer(1), new Integer(1), edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number(), lspdIdTemp, new Integer(1), lsDp, lus_id.toString(), policy, errors);
//			
//			
//			//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
//		}
		
		request.setAttribute("result","sukses");
		
		request.setAttribute("kode_nama_list",DroplistManager.getInstance().get("KODE_NAMA.xml","ID",request));
		request.setAttribute("kode_alamat_list",DroplistManager.getInstance().get("KODE_ALAMAT.xml","ID",request));
		request.setAttribute("kode_okupasi_list",DroplistManager.getInstance().get("KODE_OKUPASI.xml","ID",request));
		request.setAttribute("kode_obyek_sekitar_list",DroplistManager.getInstance().get("KODE_OBYEK_SEKITAR.xml","ID",request));
		request.setAttribute("carabayar_pas",DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request));
		
		return new ModelAndView(
        "uw/input_asuransi_kebakaran").addObject("cmd",pas);

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
		Pas pas=new Pas();
		String msp_id=ServletRequestUtils.getStringParameter(request, "msp_id",null);
		Map<String, Object> refData = new HashMap<String, Object>();
		
		request.setAttribute("kode_nama_list",DroplistManager.getInstance().get("KODE_NAMA.xml","ID",request));
		request.setAttribute("kode_alamat_list",DroplistManager.getInstance().get("KODE_ALAMAT.xml","ID",request));
		request.setAttribute("kode_okupasi_list",DroplistManager.getInstance().get("KODE_OKUPASI.xml","ID",request));
		request.setAttribute("kode_obyek_sekitar_list",DroplistManager.getInstance().get("KODE_OBYEK_SEKITAR.xml","ID",request));
		request.setAttribute("carabayar_pas",DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request));
		
		List<Pas> pasList = uwManager.selectAllPasList(msp_id, "2", null, null, null, "pas", null, "individu",null,null,null);
		pas = pasList.get(0);
		
		String bank_pp1=pas.getLsbp_id();
		String nama_bank_rekclient="";
		if(bank_pp1!=null){
			Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);
			if (data1!=null){		
				nama_bank_rekclient = (String)data1.get("BANK_NAMA");
				pas.setLsbp_nama(nama_bank_rekclient);
			}
		}
		
		return pas;
	}
	
	/**
	 * Report Pas kemarin, dikirim
	 */
	public void schedulerPas() throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("hh:mm");
		NumberFormat nf = NumberFormat.getInstance();

//		Date kemarin = tanggal;
//		if(kemarin == null) kemarin = uwDao.selectKemarin(); 

		try {
			
				List<File> attachments = new ArrayList<File>();
				
				//1. Report 1 : Summary RK (ENTRY > BAC > SUMMARY > BIASA > ALL)
				//String outputDir = "C:\\";
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				String outputDir = props.getProperty("pdf.dir.report") + "\\pas\\";
				String outputFilename = "pas" + sdf.format(elionsManager.selectSysdate()) + ".xls";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
//				params.put("tglAwal", kemarin);
//				params.put("tglAkhir", kemarin);
//				params.put("user_print", "SYSTEM");
//				params.put("lus_id", "All");
				
				List<Map> reportPas = uwManager.selectReportPas();
				//JasperUtils.exportReportToPdf(props.getProperty("report.summary.biasa")+".jasper", outputDir, outputFilename, params, reportSummary, PdfWriter.AllowPrinting, null, null);
				JasperUtils.exportReportToXls(props.getProperty("report.fire") + ".jasper", 
						outputDir, outputFilename, params, reportPas, null);
				
				File sourceFile = new File(outputDir+"\\"+outputFilename);
				attachments.add(sourceFile);
				
				//3. Email the reports
//				List<String> daftarEmailUnderwriter = new ArrayList<String>();
//				for(Map m : reportSummary){
//					String email = (String) m.get("LUS_EMAIL");
//					if(!daftarEmailUnderwriter.contains(email)) daftarEmailUnderwriter.add(email);
//				}
//				String[] underwriters = new String[daftarEmailUnderwriter.size() + 2];
//				for(int i=0; i<daftarEmailUnderwriter.size(); i++){
//					underwriters[i] = daftarEmailUnderwriter.get(i);
//				}
//				underwriters[underwriters.length-2] = "ingrid@sinarmasmsiglife.co.id";
//				underwriters[underwriters.length-1] = "rachel@sinarmasmsiglife.co.id";
//				
//				email.send(true, 
//						props.getProperty("admin.ajsjava"),
//						//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//						new String[]{"gesti@sinarmasmsiglife.co.id", "arnold@sinarmasmsiglife.co.id", "julina.hasan@sinarmasmsiglife.co.id"}, underwriters,
//						new String[]{"yusuf@sinarmasmsiglife.co.id"}, 
//						"Summary RK dari UW " + df.format(kemarin),
//						"Berikut adalah Laporan Summary RK dari UW."
//						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//						attachments);
				
				email.send(true, 
						"system-test",
						//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
						new String[]{"andy@sinarmasmsiglife.co.id"}, new String[]{"andy@sinarmasmsiglife.co.id"},
						new String[]{"andy@sinarmasmsiglife.co.id"}, 
						"Report " + outputFilename,
						"Berikut adalah Laporan PAS."
						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachments);
//				
//				for(Map m : reportSummary) {
//					int add = 0;
//					boolean sukses = false;
//					
//					while(!sukses){
//						try {
//							sukses = false;
//							uwDao.insertMstPositionSpaj("0", "Kirim Summary RK ke Accounting(" + df2.format((Date) m.get("TGL_INPUT")) + "|" + m.get("NO_PRE") + "|" + m.get("NO_VOUCHER") + "|" + nf.format(((BigDecimal)m.get("JUMLAH")).doubleValue()) + ")", (String) m.get("KEY_JURNAL"), add);
//							add = 0; sukses = true;
//						} catch (Exception e) {
//							add++; sukses = false;
//						}
//					}
//				}
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
		}

//		try {
//			insertMstSchedulerHist(
//					InetAddress.getLocalHost().getHostName(),
//					"SCHEDULER SUMMARY RK", bdate, new Date(), desc);
//		} catch (UnknownHostException e) {
//			logger.error("ERROR :", e);
//		}
	}

}