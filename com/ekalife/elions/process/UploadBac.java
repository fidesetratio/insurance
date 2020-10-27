package com.ekalife.elions.process;
 
import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import produk_asuransi.n_prod;
import produk_asuransi.n_prod_45;

import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.ClientHistory;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.CommandUploadBac;
import com.ekalife.elions.model.CommandUploadUw;
import com.ekalife.elions.model.DepositPremium;
import com.ekalife.elions.model.Icd;
import com.ekalife.elions.model.Insured;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.UploadSpaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author Yusuf Fungsi untuk upload SPAJ (input bac), dari file excel,
 *         processing menggunakan JExcelApi
 */
public class UploadBac extends ParentDao {
	protected final static Log logger = LogFactory.getLog( UploadBac.class );
	private SimpleDateFormat gmtFormat;
	private SimpleDateFormat localFormat;
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat monthFormat;
	private SimpleDateFormat yearFormat;

	public UploadBac() {
		this.gmtFormat = new SimpleDateFormat("dd/MM/yyyy (HH:mm:ss)");
		this.gmtFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.localFormat = new SimpleDateFormat("dd/MM/yyyy (HH:mm:ss)");
		this.yearFormat = new SimpleDateFormat("yyyy");
		this.monthFormat = new SimpleDateFormat("MM");
		this.dateFormat = new SimpleDateFormat("dd");
	}

	private int date(Date date) {
		return Integer.parseInt(dateFormat.format(date));
	}
	
	private int month(Date date) {
		return Integer.parseInt(monthFormat.format(date));
	}
	
	private int year(Date date) {
		return Integer.parseInt(yearFormat.format(date));
	}
	
	private static void print(Object o) {
		logger.info(o);
	}

	private String getString(Cell cell) {
		if(cell.getType() == CellType.EMPTY) return "";
		return cell.getContents();
	}

	private Double getDouble(Cell cell) {
		if(cell.getType() == CellType.EMPTY) return null;
		double value = ((NumberCell) cell).getValue();
		return value;
	}

	private Date getDate(Cell cell) throws ParseException {
		if(cell.getType() == CellType.EMPTY) return null;
		Date date = ((DateCell) cell).getDate();
		return localFormat.parse(gmtFormat.format(date));
	}

	public void bindSpajForUpload(CommandUploadBac cmd, HttpServletRequest request) throws Exception{
		if(logger.isDebugEnabled()) logger.debug("BINDING SPAJ");

		HttpSession session = request.getSession();
		User currentUser=(User) session.getAttribute("currentUser");
		
		if(logger.isDebugEnabled()) logger.debug("START UPLOADING SPAJ");

		List<UploadSpaj> daftarSpaj = new ArrayList<UploadSpaj>();
		
		Workbook workbook = Workbook.getWorkbook(cmd.getFile1().getInputStream());

		Sheet sheet = workbook.getSheet(0);
		Cell cell = null;
		int baris = 4; // start
		Date sysDate = commonDao.selectSysdate();
		n_prod produk = new n_prod_45();
		
		do {
			cell  = sheet.getCell(0, baris);
			if(cell.getType() == CellType.EMPTY) break;
			UploadSpaj upload = new UploadSpaj();

			print("- Processing Row " + (baris-4));
			
			// Pemegang Polis = Tertanggung
				//String mcl_id = sequence.sequenceMst_client_new("WW");
				Pemegang pemegang = new Pemegang(); // mcl_id,jenis,blacklist,lus_id,input_date
				cell = sheet.getCell(1, baris);
				pemegang.setMcl_first(getString(cell));
				cell = sheet.getCell(2, baris);
				Date tgl_lahir = getDate(cell);
				pemegang.setMspe_date_birth(tgl_lahir);
				cell = sheet.getCell(3, baris);
				pemegang.setMspe_place_birth(getString(cell));
				cell = sheet.getCell(4, baris);
				if(getString(cell).equals("WANITA")) pemegang.setMspe_sex(0);
				else if(getString(cell).equals("PRIA")) pemegang.setMspe_sex(1);
				// data opsional
				pemegang.setLside_id(0); // tdk ada identitas
				pemegang.setMspe_no_identity("-");
				pemegang.setLsed_id(0); // pendidikan smu
				pemegang.setMkl_kerja("BURUH");
				pemegang.setMpn_job_desc("PEMETIKAN JAMUR MERANG DAN PACKING. TIDAK MENGGUNAKAN MESIN SEPERTI DI PABRIK.");
				pemegang.setLgj_id("07"); // pegawai swasta
				pemegang.setLjb_id("001"); // staff
				pemegang.setMspe_mother("-"); // nyokap
				pemegang.setLsag_id(0); // tdk ada informasi agama
				pemegang.setMspe_sts_mrt("0"); // tdk ada info agama
				pemegang.setLsne_id(1); // indonesia
				//
				upload.setPemegang(pemegang);
				
			// AddressNew
				AddressNew address = new AddressNew(null);
				cell = sheet.getCell(5, baris);
				address.setAlamat_rumah(getString(cell));
				cell = sheet.getCell(6, baris);
				address.setKd_pos_rumah(getString(cell));
				cell = sheet.getCell(7, baris);
				address.setKota_rumah(getString(cell));
				cell = sheet.getCell(8, baris);
				address.setArea_code_rumah(getString(cell));
				cell = sheet.getCell(9, baris);
				address.setTelpon_rumah(getString(cell));
				cell = sheet.getCell(10, baris);
				address.setArea_code_rumah2(getString(cell));
				cell = sheet.getCell(11, baris);
				address.setTelpon_rumah2(getString(cell));
				//
				upload.setAddress(address);
				
			// Polis
				cell = sheet.getCell(28, baris);
				String msag_id = getString(cell);
				Map regionalAgen = bacDao.selectRegionalAgen(msag_id);
				//String reg_spaj = sequence.sequenceMst_policy(regionalAgen);
				Policy policy = new Policy(null, 1, 10, 1, Integer.parseInt(currentUser.getLus_id())); // spaj, lstb, lssp, lspd
				if(regionalAgen != null) {
				 	policy.setLca_id((String) regionalAgen.get("STRBRANCH"));
				 	policy.setLwk_id((String) regionalAgen.get("STRWILAYAH"));
				 	policy.setLsrg_id((String) regionalAgen.get("STRREGION"));
				}
				policy.setLsre_id(1); // relation = diri sendiri
				policy.setMsag_id(msag_id);
				cell = sheet.getCell(29, baris);
				Date mste_beg_date = getDate(cell);
				Integer mspo_age = null;
				if(mste_beg_date != null && tgl_lahir != null) {
					f_hit_umur umur = new f_hit_umur();
					mspo_age = umur.umur(
							year(tgl_lahir), month(tgl_lahir), date(tgl_lahir), 
							year(mste_beg_date), month(mste_beg_date), date(mste_beg_date));
					policy.setMspo_age(mspo_age);
				}
				String lku_id="01";
				policy.setLku_id(lku_id); //kurs Rupiah
				produk.of_set_pmode(3);
				policy.setLscb_id(3); //tahunan
				cell = sheet.getCell(30, baris);
				Date tgl_spaj = getDate(cell);
				policy.setMspo_spaj_date(tgl_spaj);
				policy.setMspo_ins_period(1); //lihat n_prod 45 & 130
				policy.setMspo_pay_period(1); //lihat n_prod 45 & 130
				policy.setMspo_proses_bill(0);
				policy.setMspo_installment(null); //gak ada cuti premi
				policy.setMspo_flat(0);
				policy.setMspo_ref_bii(0); //gak lewat bii
				policy.setMspo_jn_coas(0);
				policy.setMspo_pribadi(0);
				policy.setMspo_no_blanko(null); //gak ada no. blanko
				policy.setMspo_follow_up(0);
				policy.setMspo_komisi_bii(0);
				policy.setMspo_input_date(sysDate);
				policy.setMspo_provider(0);
				policy.setLstp_id(1); //tipe produk : biasa (agency regional)
				//
				upload.setPolis(policy);
				
			// Insured	
				Insured insured = new Insured(null, 1, 1, null, Integer.parseInt(currentUser.getLus_id()), sysDate);
				insured.setMste_age(mspo_age);
				insured.setMste_medical(0); //kagak ada medis
				insured.setMste_standard(0); //gak input, ini u/ powersave
				Date end_date = null;
				if(mste_beg_date!=null) {
					insured.setMste_beg_date(mste_beg_date);
					produk.of_set_begdate(year(mste_beg_date), month(mste_beg_date), date(mste_beg_date));
					end_date = defaultDateFormat.parse(defaultDateFormat.format(produk.idt_end_date.getTime()));
					insured.setMste_end_date(end_date);
				}
				insured.setMste_backup(0);
				insured.setLssa_id(1);
				insured.setMste_reas(0);
				insured.setMste_active(1);
				insured.setMste_flag_cc(0);
				insured.setMste_flag_ubah(0);
				insured.setMste_pct_dplk((double) 0);
				//
				upload.setInsured(insured);
				
			// Agen
//				Cmdeditbac edit = new Cmdeditbac();
//				pemegang.setMspo_ao("");
//				pemegang.setMspo_pribadi(0);
//				edit.setPemegang(pemegang);
//				edit.setAgen(new Agen());
//				Map data = (HashMap) bacDao.selectagenpenutup(msag_id);
//				String kode_regional=null;
//				if (data!=null)
//					kode_regional=(String)data.get("REGIONID");
//				//
//				savingBac.proc_save_agen(edit, reg_spaj, msag_id, agentBranch, policy.getLca_id(), policy.getLwk_id(), policy.getLsrg_id(), kode_regional);
				
			// AddressBilling
				AddressBilling addressBilling = new AddressBilling();
				addressBilling.setFlag_cc(0);
				addressBilling.setKota(address.getKota_rumah());
				addressBilling.setMsap_address(address.getAlamat_rumah());
				addressBilling.setMsap_zip_code(address.getKd_pos_rumah());
				addressBilling.setMsap_area_code1(address.getArea_code_rumah());
				addressBilling.setMsap_area_code2(address.getArea_code_rumah2());
				addressBilling.setMsap_phone1(address.getTelpon_rumah());
				addressBilling.setMsap_phone2(address.getTelpon_rumah2());
				addressBilling.setLca_id(policy.getLca_id());
				addressBilling.setLwk_id(policy.getLwk_id());
				addressBilling.setLsrg_id(policy.getLsrg_id());
				//
				upload.setAddressBilling(addressBilling);
				
//			// PositionSpaj 	
//				bacDao.insertMst_position_spaj(reg_spaj, currentUser.getLus_id(), 1);
//						
//			// Mst_sts_client
//				bacDao.insertMst_sts_client(mcl_id);

			// ProductInsured
				Product product = new Product(null, 1, 45, 1);
				product.setLku_id("01");
				product.setMspr_beg_date(mste_beg_date);
				product.setMspr_end_date(end_date);
				Double duapuluhlimajuta = new Double("25000000");
				produk.of_set_up(duapuluhlimajuta);
				product.setMspr_tsi(produk.idec_up);
				product.setMspr_tsi_pa_a(produk.idec_up);
				product.setMspr_tsi_pa_b(produk.idec_up);
				product.setMspr_tsi_pa_c((double) 0);
				product.setMspr_tsi_pa_d((double) 0);
				product.setMspr_tsi_pa_m(produk.idec_up);
				produk.of_hit_premi();
				product.setMspr_class(produk.ii_class);
				product.setMspr_unit(0);
				product.setMspr_rate(produk.ldec_rate);
				product.setMspr_persen(0);
				product.setMspr_premium((double) produk.idec_premi);
				product.setMspr_discount((double) 0);
				product.setMspr_active(1);
				product.setMspr_extra((double) 0);
				product.setMspr_ins_period(1);
				product.setMspr_tt(0);
				//
				upload.setProduct(product);
				
			// Beneficiary (0-4)
				List<Benefeciary> daftarBenef = new ArrayList<Benefeciary>();
				int counterBenef = 0;
				String res = session.getServletContext().getResource("/xml/").toString();
				
				cell = sheet.getCell(12, baris);
				if(!kosong(getString(cell))) {
					Benefeciary waris1 = new Benefeciary(null, 1, ++counterBenef);
					waris1.setMsaw_first(getString(cell));
					cell = sheet.getCell(13, baris);
					waris1.setMsaw_birth(getDate(cell));
					cell = sheet.getCell(14, baris);
					String lsre_id = getString(cell);
					if(!lsre_id.equals(""))
						waris1.setLsre_id(Integer.parseInt(Common.searchXml(res+"RELATION.xml", "RELATION", "ID", lsre_id)));
					waris1.setLsre_relation(getString(cell));
					cell = sheet.getCell(15, baris);
					waris1.setMsaw_persen(getDouble(cell));
					//
					daftarBenef.add(waris1);
				}
				
				cell = sheet.getCell(16, baris);
				if(!kosong(getString(cell))) {
					Benefeciary waris2 = new Benefeciary(null, 1, ++counterBenef);
					waris2.setMsaw_first(getString(cell));
					cell = sheet.getCell(17, baris);
					waris2.setMsaw_birth(getDate(cell));
					cell = sheet.getCell(18, baris);
					String lsre_id = getString(cell);
					if(!lsre_id.equals(""))
						waris2.setLsre_id(Integer.parseInt(Common.searchXml(res+"RELATION.xml", "RELATION", "ID", lsre_id)));
					waris2.setLsre_relation(getString(cell));
					cell = sheet.getCell(19, baris);
					waris2.setMsaw_persen(getDouble(cell));
					//
					daftarBenef.add(waris2);
				}
				
				cell = sheet.getCell(20, baris);
				if(!kosong(getString(cell))) {
					Benefeciary waris3 = new Benefeciary(null, 1, ++counterBenef);
					waris3.setMsaw_first(getString(cell));
					cell = sheet.getCell(21, baris);
					waris3.setMsaw_birth(getDate(cell));
					cell = sheet.getCell(22, baris);
					String lsre_id = getString(cell);
					if(!lsre_id.equals(""))
						waris3.setLsre_id(Integer.parseInt(Common.searchXml(res+"RELATION.xml", "RELATION", "ID", lsre_id)));
					waris3.setLsre_relation(getString(cell));
					cell = sheet.getCell(23, baris);
					waris3.setMsaw_persen(getDouble(cell));
					//
					daftarBenef.add(waris3);
				}
				
				cell = sheet.getCell(24, baris);
				if(!kosong(getString(cell))) {
					Benefeciary waris4 = new Benefeciary(null, 1, ++counterBenef);
					waris4.setMsaw_first(getString(cell));
					cell = sheet.getCell(25, baris);
					waris4.setMsaw_birth(getDate(cell));
					cell = sheet.getCell(26, baris);
					String lsre_id = getString(cell);
					if(!lsre_id.equals(""))
						waris4.setLsre_id(Integer.parseInt(Common.searchXml(res+"RELATION.xml", "RELATION", "ID", lsre_id)));
					waris4.setLsre_relation(getString(cell));
					cell = sheet.getCell(27, baris);
					waris4.setMsaw_persen(getDouble(cell));
					//
					daftarBenef.add(waris4);
				}
				upload.setDaftarBenefeciary(daftarBenef);

			// Titipan Premi
				DepositPremium ttp = new DepositPremium();
				ttp.setMsdp_jtp(1);
				ttp.setMsdp_flag("B");
				ttp.setLsjb_id(5); //cara bayar = transfer 
				ttp.setClient_bank(0);
				cell = sheet.getCell(33, baris); //tgl bayar
				if(getDate(cell)!=null) ttp.setMsdp_pay_date(getDate(cell));
				cell = sheet.getCell(32, baris); //tgl rk
				if(getDate(cell)!=null) ttp.setMsdp_date_book(getDate(cell));
				ttp.setLku_id(lku_id);
				ttp.setMsdp_payment((double) produk.idec_premi);
				ttp.setMsdp_selisih_kurs((double) 1);
				ttp.setMsdp_input_date(sysDate);
				ttp.setLus_id(Integer.parseInt(currentUser.getLus_id()));
				ttp.setMsdp_active(0);
				cell = sheet.getCell(31, baris); //bank ttp
				List rek = uwDao.selectBankEkaLife(getString(cell));
				if(rek.size()>0) {
					BigDecimal lsrek_id = (BigDecimal) ((Map) rek.get(0)).get("LSREK_ID");
					ttp.setLsrek_id(lsrek_id.intValue());
				}
				ttp.setMsdp_jurnal(0);
				ttp.setNo_kttp("000000");
				upload.setTtp(ttp);
				
			// Rider, Karyawan, Unitlink, Powersave, Rekening
			// TIDAK DI-INSERT	
				
			daftarSpaj.add(upload);
			
			baris++;
		}while(true);
		
		if(workbook!=null){
			workbook.close();
		}
		
		cmd.setDaftarSpaj(daftarSpaj);
		request.setAttribute("jumlahSpaj", daftarSpaj.size());
		
	}
	
	public List<DropDown> uploadEmailAgen(CommandUploadBac cmd, HttpServletRequest request) throws ParseException, IOException, BiffException, ServletException{
		List<DropDown> result = new ArrayList<DropDown>();

		Workbook workbook = Workbook.getWorkbook(cmd.getFile1().getInputStream());
		Sheet sheet = workbook.getSheet(0);
		Cell cell = null;
		int baris = 1; //mulai dari baris ke dua
		do {
			try {
				cell  = sheet.getCell(0, baris);
			}catch(ArrayIndexOutOfBoundsException e) {
				break;
			}
			if(cell.getType() == CellType.EMPTY) {
				break;
			} else {
				DropDown d = new DropDown(sheet.getCell(0, baris).getContents(), sheet.getCell(2, baris).getContents()); 
				d.setDesc(String.valueOf(commonDao.updateEmailAgen(d)));
				result.add(d);
			}
			baris++;
		}while(true);		
		if(workbook!=null){
			workbook.close();
		}
		
		return result;
	}
	
	public List<Icd> uploadIcdCode(CommandUploadUw cmd, HttpServletRequest request) throws ParseException, IOException, BiffException, ServletException {
		List<Icd> result = new ArrayList<Icd>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		Workbook workbook = Workbook.getWorkbook(cmd.getFile1().getInputStream());
		Sheet sheet = workbook.getSheet(0);
		Cell cell = null;
		int baris = 1; //mulai dari baris ke dua
		do {
			try {
				cell  = sheet.getCell(0, baris);
			}catch(ArrayIndexOutOfBoundsException e) {
				break;
			}
			if(cell.getType() == CellType.EMPTY) {
				break;
			} else {
				Icd tempIcd = new Icd();
				tempIcd.setLic_id(sheet.getCell(0, baris).getContents());
				tempIcd.setLic_desc(sheet.getCell(1, baris).getContents());
				tempIcd.setLic_type(Integer.parseInt(sheet.getCell(2, baris).getContents()));
				tempIcd.setMsdi_lus_id(Integer.parseInt(currentUser.getLus_id()));
				tempIcd.setMsdi_input_date(new Date());
				result.add(tempIcd);
				commonDao.insertIcdCode(tempIcd);
			}
			baris++;
		}while(true);		
		if(workbook!=null){
			workbook.close();
		}
		
		return result;		
	}
	
	public void uploadSpaj(CommandUploadBac cmd, HttpServletRequest request) throws ParseException, IOException, BiffException, ServletException{

		HttpSession session = request.getSession();
		User currentUser=(User) session.getAttribute("currentUser");
		
		if(logger.isDebugEnabled()) logger.debug("START UPLOADING SPAJ");

	
		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();

		for(int i=0; i<cmd.getDaftarSpaj().size(); i++) {
			// Pemegang Polis = Tertanggung
				UploadSpaj upload = cmd.getDaftarSpaj().get(i);
				String mcl_id = sequence.sequenceMst_client_new("WW");
				upload.getPemegang().setMcl_id(mcl_id);
				bacDao.insertMst_client_new(upload.getPemegang());
			// AddressNew
				upload.getAddress().setMcl_id(mcl_id);
				commonDao.insertMstCompanyAddress(upload.getAddress());
			// Polis
				Map regionalAgen = bacDao.selectRegionalAgen(upload.getPolis().getMsag_id());
				String reg_spaj = sequence.sequenceMst_policy(regionalAgen);
				upload.getPolis().setReg_spaj(reg_spaj);
			 	String agentBranch = (String) regionalAgen.get("STRAGENTBRANCH");
				upload.getPolis().setMspo_policy_holder(mcl_id);
				bacDao.insertMst_policy(upload.getPolis());
			// Insured
				upload.getInsured().setReg_spaj(reg_spaj);
				upload.getInsured().setMste_insured(mcl_id);
				bacDao.insertMst_insured(upload.getInsured());
			// Agen
				Cmdeditbac edit = new Cmdeditbac();
				upload.getPemegang().setMspo_ao("");
				upload.getPemegang().setMspo_pribadi(0);
				edit.setPemegang(upload.getPemegang());
				edit.setAgen(new Agen());
				Map data = (HashMap) bacDao.selectagenpenutup(upload.getPolis().getMsag_id());
				String kode_regional=null;
				if (data!=null)
					kode_regional=(String)data.get("REGIONID");
				savingBac.proc_save_agen(edit, reg_spaj, upload.getPolis().getMsag_id(), agentBranch, 
						upload.getPolis().getLca_id(), upload.getPolis().getLwk_id(), upload.getPolis().getLsrg_id(), kode_regional);
			// AddressBilling
				upload.getAddressBilling().setReg_spaj(reg_spaj);
				bacDao.insertMst_address_billing(upload.getAddressBilling());
			// PositionSpaj 	
				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "INPUT SPAJ", reg_spaj, 0);
			// Mst_sts_client
				bacDao.insertMst_sts_client(mcl_id);
			// ProductInsured
				upload.getProduct().setReg_spaj(reg_spaj);
				bacDao.insertMst_product_insured(upload.getProduct());
			// Beneficiary (0-4)
				for(int j=0; j<upload.getDaftarBenefeciary().size(); j++) {
					Benefeciary b = upload.getDaftarBenefeciary().get(j);
					b.setReg_spaj(reg_spaj);
					bacDao.insertMst_benefeciary(b);
				}
			// Titipan Premi
				upload.getTtp().setReg_spaj(reg_spaj);
				upload.getTtp().setMsdp_number(1);
				bacDao.insertMst_deposit_premium(upload.getTtp());
			// Rider, Karyawan, Unitlink, Powersave, Rekening
			// TIDAK DI-INSERT				

		}

	}
	/**Fungsi : Untuk Upload data client history data pengkinian
	 * 
	 * @param in
	 * @param currentUser
	 * @throws Exception
	 * @author Ferry Harlim
	 */
	public Integer prosesUploadClientHistory(InputStream in,User currentUser) throws Exception{
		try{
			int begRow=7;
			int count=0;
			HSSFWorkbook workBook=new HSSFWorkbook(in);
			HSSFSheet sheet = workBook.getSheetAt(0);
			logger.info(sheet.getLastRowNum());
			int j=0;
			for(int i=begRow;i<=sheet.getLastRowNum();i++){
				ClientHistory clientHistory=new ClientHistory();
				j++;
				HSSFRow rows=sheet.getRow(i);
				if(rows==null)
					break;
				Integer noUrut=commonDao.selectMaxUrutMstClientHistory(rows.getCell((short)4).getStringCellValue());
				if(noUrut==null)
					noUrut=1;
				else
					noUrut++;
				clientHistory.setMsch_no_urut(noUrut);
				clientHistory.setMsch_nama_pp(rows.getCell((short)2).getStringCellValue());
				clientHistory.setMsch_nama_tt(rows.getCell((short)3).getStringCellValue());
				clientHistory.setMspo_policy_no(rows.getCell((short)4).getStringCellValue());
				clientHistory.setMsch_nama_produk(rows.getCell((short)5).getStringCellValue());
				clientHistory.setMsch_cabang(rows.getCell((short)6).getStringCellValue());
				clientHistory.setMsch_alamat(rows.getCell((short)7).getStringCellValue());
				clientHistory.setMsch_kota(rows.getCell((short)8).getStringCellValue());
				String tglKirim=rows.getCell((short)9).getStringCellValue();
				String tglTerima=rows.getCell((short)10).getStringCellValue();
				if(tglKirim!=null && tglKirim.length()>=8)
						clientHistory.setMsch_tgl_kirim(FormatDate.toDate(tglKirim));
				if(tglTerima!=null && tglTerima.length()>=8)
					clientHistory.setMsch_tgl_terima(FormatDate.toDate(tglTerima));
				clientHistory.setMsch_penerima((rows.getCell((short)11).getStringCellValue()));
				clientHistory.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				String lsshid=rows.getCell((short)12).getStringCellValue();
				if(lsshid!=null)
					clientHistory.setLssh_id(Integer.valueOf(lsshid));
				else
					clientHistory.setLssh_id(null);
				clientHistory.setMsch_status_input(3);//input lewat upload data vendor
				commonDao.insertMstClientHistory(clientHistory);
				count++;
			}	
			return count;
		}finally{
			if(in!=null){
				in.close();
			}
		}
		
	}
	
	private boolean kosong(String kata) {
		if(kata == null) return true;
		else if(kata.trim().equals("")) return true;
		else return false;
	}
	
	/**Fungsi : Untuk mengupdate kolom upload data vendor yang missing.
	 * @param in
	 * @param lusId
	 * @return
	 * @throws IOException 
	 */
	public Integer prosesUploadClientHistory(InputStream in, String lusId) throws IOException {
		try{
			int begRow=7;
			int count=0;
			HSSFWorkbook workBook=new HSSFWorkbook(in);
			HSSFSheet sheet = workBook.getSheetAt(0);
			logger.info(sheet.getLastRowNum());
			int j=0;
			for(int i=begRow;i<=sheet.getLastRowNum();i++){
				ClientHistory clientHistory=new ClientHistory();
				j++;
				HSSFRow rows=sheet.getRow(i);
				if(rows==null)
					break;
				clientHistory.setMspo_policy_no(rows.getCell((short)4).getStringCellValue());
				String tglKirim=rows.getCell((short)9).getStringCellValue();
				String tglTerima=rows.getCell((short)10).getStringCellValue();
				if(tglKirim!=null && tglKirim.length()>=8)
						clientHistory.setMsch_tgl_kirim(FormatDate.toDate(tglKirim));
				if(tglTerima!=null && tglTerima.length()>=8)
					clientHistory.setMsch_tgl_terima(FormatDate.toDate(tglTerima));
				clientHistory.setMsch_status_input(3);//input lewat upload data vendor
				clientHistory.setLus_id(Integer.valueOf(lusId));
				commonDao.updateMstClientHistoryTgl(clientHistory);
				count++;
			}	
			return count;
		}finally{
			if(in!=null){
				in.close();
			}
		}
		
	}
	
}