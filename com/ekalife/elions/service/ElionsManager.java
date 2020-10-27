package com.ekalife.elions.service;
 
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import com.ekalife.elions.dao.AccountingDao;
import com.ekalife.elions.dao.AkseptasiDao;
import com.ekalife.elions.dao.BacDao;
import com.ekalife.elions.dao.BasDao;
import com.ekalife.elions.dao.BlankoDao;
import com.ekalife.elions.dao.ChecklistDao;
import com.ekalife.elions.dao.CommonDao;
import com.ekalife.elions.dao.CrossSellingDao;
import com.ekalife.elions.dao.EditDataDao;
import com.ekalife.elions.dao.EndorsDao;
import com.ekalife.elions.dao.FinanceDao;
import com.ekalife.elions.dao.MuamalatDao;
import com.ekalife.elions.dao.RefundDao;
import com.ekalife.elions.dao.ReinstateDao;
import com.ekalife.elions.dao.RekruitmentDao;
import com.ekalife.elions.dao.SchedulerDao;
import com.ekalife.elions.dao.SimasCardDao;
import com.ekalife.elions.dao.UwDao;
import com.ekalife.elions.dao.WorksiteDao;
import com.ekalife.elions.model.*;
import com.ekalife.elions.model.btpp.Btpp;
import com.ekalife.elions.model.btpp.CommandBtpp;
import com.ekalife.elions.model.cross_selling.AgentCs;
import com.ekalife.elions.model.cross_selling.CrossSelling;
import com.ekalife.elions.model.cross_selling.PolicyCs;
import com.ekalife.elions.model.cross_selling.PositionCs;
import com.ekalife.elions.model.kyc.CommandKyc;
import com.ekalife.elions.model.kyc.Hrc;
import com.ekalife.elions.model.kyc.NewBusinessCase;
import com.ekalife.elions.model.reinstate.CommandReins;
import com.ekalife.elions.model.sms.Smsserver_out;
import com.ekalife.elions.model.tts.CaraBayar;
import com.ekalife.elions.model.tts.CommandTts;
import com.ekalife.elions.model.tts.PolicyTts;
import com.ekalife.elions.model.tts.Tahapan;
import com.ekalife.elions.model.tts.Tts;
import com.ekalife.elions.model.vo.JenisMedicalVO;
import com.ekalife.elions.model.vo.MedicalCheckupVO;
import com.ekalife.elions.process.CancelPolis;
import com.ekalife.elions.process.Endorsement;
import com.ekalife.elions.process.HistoryKirimPolis;
import com.ekalife.elions.process.HitungBac;
import com.ekalife.elions.process.Manfaat;
import com.ekalife.elions.process.Nab;
import com.ekalife.elions.process.NilaiTunai;
import com.ekalife.elions.process.Produksi;
import com.ekalife.elions.process.SavingBac;
import com.ekalife.elions.process.SavingBeaMeterai;
import com.ekalife.elions.process.SavingRekruitment;
import com.ekalife.elions.process.Sequence;
import com.ekalife.elions.process.SuratUnitLink;
import com.ekalife.elions.process.TransferPolis;
import com.ekalife.elions.process.UploadBac;
import com.ekalife.elions.process.uw.ReasIndividu;
import com.ekalife.elions.process.uw.ReasUtilities;
import com.ekalife.elions.process.uw.Reinstate;
import com.ekalife.elions.process.uw.Simultan;
import com.ekalife.elions.process.uw.TransferUw;
import com.ekalife.elions.web.refund.vo.BiayaUlinkDbVO;
import com.ekalife.elions.web.refund.vo.CheckSpajParamsVO;
import com.ekalife.elions.web.refund.vo.InfoBatalVO;
import com.ekalife.elions.web.refund.vo.LampiranListVO;
import com.ekalife.elions.web.refund.vo.MstBatalParamsVO;
import com.ekalife.elions.web.refund.vo.PenarikanUlinkDbVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RefundDbVO;
import com.ekalife.elions.web.refund.vo.RefundDetDbVO;
import com.ekalife.elions.web.refund.vo.RefundViewVO;
import com.ekalife.elions.web.refund.vo.RekapInfoVO;
import com.ekalife.elions.web.refund.vo.SetoranPokokDanTopUpVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.Products;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.f_validasi;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;

/**
 * @author Yusuf
 * @since 7 April 2006
 * 
 * Class UTAMA dari Aplikasi E-Lions, dimana SEMUA proses terletak di dalam class ini
 * 
 * - Secara Logika, 1 ElionsManager mempunyai banyak bawahan yaitu DAO (data access objects), 
 *   dimana masing2 DAO mempunyai tugasnya masing2
 * 
 * - DAO terbagi menjadi dua yaitu DAO biasa (untuk query2 sederhana), dan proses2 
 *   (untuk proses yang lebih kompleks, seperti jurnal / komisi / hitung nilai tunai dan masih banyak lainnya)
 * 
 * - Biasakan untuk mengurut fungsi2 dalam urutan alphabet agar mempermudah maintenance
 * 
 * - Apabila ingin test suatu proses, dan tidak ingin commit, tapi ingin melihat transaksi di log, 
 *   cukup tambahkan 1 baris ini di awal fungsi :
 *   TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
 *    
 */
public class ElionsManager {
	
	protected final Log logger = LogFactory.getLog( getClass() );
	private Email email;
	private Products products;
	private DecimalFormat f3= new DecimalFormat ("000");
	private DateFormat defaultDateFormat;
	private UwManager uwManager;
	public BacManager bacManager;
	public void setUwManager(UwManager uwManager) {this.uwManager = uwManager;}
	public void setBacManager(BacManager bacManager) {this.bacManager = bacManager;}
	
	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public void setProducts(Products products) {
		this.products = products;
	}
	public void setEmail(Email email) {
		this.email = email;
	}
	
	/* ======================= Proses2 (Pada dasarnya, sama seperti DAO juga) ======================= */
	private AjaxManager ajaxManager;
	private CancelPolis cancelPolis;
	private Endorsement endorsement;
	private Manfaat manfaat;
	private Nab nab;
	private NilaiTunai nilaiTunai;
	private SuratUnitLink suratUnitLink;
	private HistoryKirimPolis historyKirimPolis;
	private TransferPolis transferPolis;
	private HitungBac hitungBac;
	private SavingBac savingBac;
	private SavingBeaMeterai savingBeaMeterai;
	private SavingRekruitment savingRekruitment;
	private UploadBac uploadBac;
	private Properties props;
	private ReasIndividu reasIndividu;
	private ReasUtilities reasUtilities;
	private Sequence sequence;
	private Produksi produksi;
	private Simultan simultan;
	private TransferUw transferUw;
	private Reinstate reinstate;

	/* ======================= Data Access Objects (DAO) ======================= */
	private AkseptasiDao akseptasiDao;
	private BacDao bacDao;
	private BasDao basDao;
	private CommonDao commonDao;
	private FinanceDao financeDao;
	private ReinstateDao reinstateDao;
	private UwDao uwDao;
	private WorksiteDao worksiteDao;
	private BlankoDao blankoDao;
	private RekruitmentDao rekruitmentDao;
	private CrossSellingDao crossSellingDao;
	private AccountingDao accountingDao;
	private SimasCardDao simasCardDao;
	private ChecklistDao checklistDao;
	private MuamalatDao muamalatDao;
	private EditDataDao editDataDao;
	private RefundDao refundDao;
	private EndorsDao endorsDao;
	private SchedulerDao schedulerDao;
	public void setSchedulerDao(SchedulerDao schedulerDao) {this.schedulerDao = schedulerDao;}

	/* ======================= Getter-Setter ======================= */
	public Properties getProps() {return this.props;}
	public void setProps(Properties props) {this.props = props;}
	public void setCancelPolis(CancelPolis cancelPolis) {this.cancelPolis = cancelPolis;}
	public void setEndorsement(Endorsement endorsement) {this.endorsement = endorsement;}
	public void setManfaat(Manfaat manfaat) {this.manfaat = manfaat;}
	public void setNab(Nab nab) {this.nab = nab;}
	public NilaiTunai getNilaiTunai() {return nilaiTunai;}
	public void setNilaiTunai(NilaiTunai nilaiTunai) {this.nilaiTunai = nilaiTunai;}
	public void setTransferPolis(TransferPolis transferPolis) {this.transferPolis = transferPolis;}
	public void setSuratUnitLink(SuratUnitLink suratUnitLink) {this.suratUnitLink = suratUnitLink;}	
	public void setHistoryKirimPolis(HistoryKirimPolis historyKirimPolis) {this.historyKirimPolis = historyKirimPolis;}	
	public void setHitungBac(HitungBac hitungBac) {	this.hitungBac = hitungBac;	}
	public void setSavingBac(SavingBac savingBac) {this.savingBac = savingBac; }
	public void setSavingBeaMeterai(SavingBeaMeterai savingBeaMeterai) { this.savingBeaMeterai = savingBeaMeterai;}
	public void setUploadBac(UploadBac uploadBac) {this.uploadBac = uploadBac;}
	public void setProduksi(Produksi produksi) {this.produksi = produksi;}
	
	public void setAccountingDao(AccountingDao accountingDao) {this.accountingDao = accountingDao;}
	public void setAkseptasiDao(AkseptasiDao akseptasiDao) {this.akseptasiDao = akseptasiDao;}
	public void setBacDao(BacDao bacDao) {this.bacDao = bacDao;}
	public void setCommonDao(CommonDao commonDao) {this.commonDao = commonDao;}
	public void setEditDataDao(EditDataDao editDataDao) {this.editDataDao = editDataDao;}
	public void setFinanceDao(FinanceDao financeDao) {	this.financeDao = financeDao;}
	public void setReinstateDao(ReinstateDao reinstateDao) {this.reinstateDao = reinstateDao;}
	public UwDao getUwDao() {return uwDao;}
	public void setUwDao(UwDao uwDao) {this.uwDao = uwDao;}
	public void setWorksiteDao(WorksiteDao worksiteDao) {this.worksiteDao = worksiteDao;}
	public void setBlankoDao(BlankoDao blankoDao) { this.blankoDao = blankoDao; }
	public void setRekruitmentDao(RekruitmentDao rekruitmentDao) { this.rekruitmentDao = rekruitmentDao;}
	public void setBasDao(BasDao basDao) {this.basDao = basDao;}
	public void setSequence(Sequence sequence) {this.sequence = sequence;}
	public void setSavingRekruitment(SavingRekruitment savingRekruitment) { this.savingRekruitment = savingRekruitment; }	
	public void setAjaxManager(AjaxManager ajaxManager) {this.ajaxManager = ajaxManager;}
	public void setReinstate(Reinstate reinstate) {this.reinstate = reinstate;}
	public void setReasUtilities(ReasUtilities reasUtilities) {this.reasUtilities = reasUtilities;}
	public void setCrossSellingDao(CrossSellingDao crossSellingDao) {this.crossSellingDao = crossSellingDao;}
	public void setSimasCardDao(SimasCardDao simasCardDao) {this.simasCardDao = simasCardDao;}
	public void setChecklistDao(ChecklistDao checklistDao) {this.checklistDao = checklistDao;}
	public void setMuamalatDao(MuamalatDao muamalatDao) {this.muamalatDao = muamalatDao;}
	public void setRefundDao(RefundDao refundDao) {this.refundDao = refundDao;}
	public void setEndorsDao(EndorsDao endorsDao) {this.endorsDao = endorsDao;}

	public void setF3(DecimalFormat f3) {this.f3 = f3;}

	public Integer f_prod_topup(String spaj, Integer prod_ke){
		return (Integer)this.uwDao.f_prod_topup(spaj, prod_ke);
	}

	public Integer selectMasaGaransiInvestasi(String reg_spaj, Integer tahun_ke, Integer premi_ke) {
		return this.uwDao.selectMasaGaransiInvestasi(reg_spaj, tahun_ke, premi_ke);
	}

	//kode status aksep untuk ditampilkan di mst_position_spaj
	public String getKodeStatusAksep(int lssa_id) {		
		if(lssa_id == 1) {
			return "ES: ";
		}else if(lssa_id == 2) {
			return "DC: ";
		}else if(lssa_id == 3) {
			return "FR: ";
		}else if(lssa_id == 4) {
			return "EP: ";
		}else if(lssa_id == 5) {
			return "AC: ";
		}else if(lssa_id == 8) {
			return "FA: ";
		}else if(lssa_id == 9) {
			return "PP: ";
		}else if(lssa_id == 10) {
			return "AK: ";
		}
//		else if(lssa_id == 12){
//			return "AS: ";
//		}
		else{
			return "";
		}
	}
	
	/* ======================= Khusus TESTING !!!======================= */
	public void transferBanyakPrintPolisKeFilling(User currentUser) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String[] spaj = {
				"09200606887"};
		for(int i=0; i<spaj.length; i++) {
			logger.info("SPAJ NOMOR " + spaj[i] + " = " + transferPolis.transferBanyakPrintPolisKeFilling(spaj[i], currentUser));
		}
	}
	public String sequenceSertifikatSimasPrima() {
		return this.sequence.sequenceSertifikatSimasPrima();
	}
	
	public String sequenceSertifikatSimasSekuritas() {
		return this.sequence.sequenceSertifikatSimasSekuritas();
	}
	
	public String sequenceNoRegStableLink() {
		return this.sequence.sequenceNoRegStableLink();
	}
	
	public String sequenceDuplikatPolis(String reg_spaj) {
		return this.sequence.sequenceDuplikatPolis(reg_spaj);
	}
	
	public String sequenceNoVoucher(String mtb_gl_no,String rek_id, String nomor){
		return this.sequence.sequenceNoVoucher(mtb_gl_no,rek_id, nomor);
	}
	
	public void updateUserOnlineStatus(Date luss_online, String lus_id) {
		commonDao.updateUserOnlineStatus(luss_online, lus_id);
	}
	
	public void updatePosisiMst_insured(String spaj, int posisi)  {
		uwDao.updatePosisiMst_insured(spaj,posisi);
	}
	
	public void updatePosisiMst_policy1(String spaj, int posisi, Date mspo_date_ttp)  {
		uwDao.updatePosisiMst_policy1(spaj,posisi,mspo_date_ttp);
	}
	public void delete(String query) {
		commonDao.delete(query);
	}
	
	public void updateMstAktivitasUsingModel(Aktivitas aktivitas) throws DataAccessException{
		commonDao.updateMstAktivitasUsingModel(aktivitas);
	}
	
	public void cancelBanyakPolisGuthrie(User currentUser) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		/*
		List<String> daftarSpaj = commonDao.selectDaftarBatalGuthrie();
		int hitung=0;
		for(String spaj : daftarSpaj) {
			hitung++;
			this.cancelPolis.cancelPolisFromTandaTerimaPolis(spaj, "PENGGANTIAN CARA BAYAR", currentUser);
		}
		 */

		//bukan cancel guthrie, melainkan cancel polis2 worksite yg sampah 
		
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String[] daftarSpaj = {
				"42200700058","42200700177"};
		
		int hitung=0;
		for(String spaj : daftarSpaj) {
			hitung++;
			this.cancelPolis.cancelPolisFromTandaTerimaPolis(spaj, "TIDAK JADI DIPROSES (DATA SAMPAH)", currentUser);
		}
		
	}
	
	public String selectTanggalLahirPemegangPolis(String spaj) {
		return commonDao.selectTanggalLahirPemegangPolis(spaj);
	}
	
	public void insertAksesHist(AksesHist a) {
		try {
//			commonDao.insertAksesHist(a); //tidak perlu lagi krn sudah ada di trigger.
			commonDao.updateUserOnlineStatus(a.getMsah_date(), a.getLus_id().toString());
		} catch (DataAccessException e) {}
	}
	
	public List<String> selectDaftarSpajYangMauDiProsesNilaiTunai(int mulai, int selesai) {
		return commonDao.selectDaftarSpajYangMauDiProsesNilaiTunai(mulai, selesai);
	}
	
	public int selectApakahSudahProsesNilaiTunai(String spaj) {
		return commonDao.selectApakahSudahProsesNilaiTunai(spaj);
	}
	
	public void testJurnalProduksiIndividu(String spaj, User currentUser) {
		try {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			transferPolis.testJurnalProduksiIndividu(spaj, currentUser);
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
	}
	
	public void testJurnalProduksiUnitLink(String spaj, User currentUser) {
		try {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			transferPolis.testJurnalProduksiUnitLink(spaj, currentUser);
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
	}
	
	public void testSequenceVoucherPremiIndividu(String spaj, User currentUser) {
		try {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			transferPolis.testSequenceVoucherPremiIndividu(spaj, currentUser);
		} catch (Exception e) {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			logger.error("ERROR :", e);
		}
	}
	
	public void testMuamalat(User currentUser) throws DataAccessException, IOException {
//		String[] spaj = {"02200800035", "01200700262"};
//		
//		for(String s : spaj) {
//			muamalatDao.saveDataBmi(s, currentUser);			
//		}
		schedulerDao.schedulerBmi("SCHEDULER BMI");
	}
	
	public void testTransferToPrintPolis(String reg_spaj, User currentUser) {
		try {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();

			commonDao.delete("DELETE FROM EKA.TMP_YOSEP WHERE REG_SPAJ = '" + reg_spaj + "'");
			uwDao.deleteDataProduksi(reg_spaj);
			
			//INI HANYA BOLEH DIGUNAKAN, BILA DI ROLLBACK
			commonDao.delete("DELETE FROM EKA.MST_COMM_BONUS WHERE REG_SPAJ = '" + reg_spaj + "'");
			commonDao.delete("DELETE FROM EKA.MST_UPLOAD_NON WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + reg_spaj + "')");
			commonDao.delete("DELETE FROM EKA.MST_COMMISSION WHERE REG_SPAJ = '" + reg_spaj + "'");
			commonDao.delete("DELETE FROM EKA.MST_ULINK_DET_BILL WHERE REG_SPAJ = '" + reg_spaj + "'");
			commonDao.delete("DELETE FROM EKA.MST_ULINK_BILL WHERE REG_SPAJ = '" + reg_spaj + "'");
			commonDao.delete("DELETE FROM EKA.MST_COMM_BONUS WHERE REG_SPAJ = '" + reg_spaj + "'");
			commonDao.delete("DELETE FROM EKA.MST_UPLOAD_REFUND WHERE REG_SPAJ = '" + reg_spaj + "'");
			commonDao.delete("DELETE FROM EKA.MST_COMM_REFF_BII WHERE REG_SPAJ = '" + reg_spaj + "'");
			commonDao.delete("DELETE FROM EKA.MST_REWARD WHERE REG_SPAJ = '" + reg_spaj + "'");
			//
			
			List billInfo = this.uwDao.selectBillingInfoForTransfer(reg_spaj, 1, 1);
			BindException errors = new BindException(new HashMap(), "cmd");
			boolean hasil = this.transferPolis.transferToPrintPolis(reg_spaj, errors, 1, billInfo, currentUser, "1", "1", "payment", this, uwManager);
			//boolean hasil = this.transferPolis.transferToPrintPolisTest(reg_spaj, errors, 1, billInfo, currentUser, "1", "1", "payment", this, uwManager);

		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
	}
	
	public void prosesUlinkDetBill(String reg_spaj) throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		produksi.prosesUlinkDetBill(reg_spaj, uwDao.selectProductionDate(reg_spaj, 1, 1));
		Ulangan u = new Ulangan();
		u.setReg_spaj(reg_spaj);
		u.setJenis("INSERT ULINK-BILL");
		u.setTanggal(new Date());
		u.setLus_id(0);
		u.setKeterangan("INSERT ULINK-BILL (UNTUK PROSES POTONGAN)");
		editDataDao.insertLstUlangan(u);
	}
	
	public Upp prosesPerhitunganUpp(Upp upp) {
		return produksi.prosesPerhitunganUpp(upp);
	}
		
	public void hitungKomisi(String spaj, User currentUser) {
		try {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			
			//INI HANYA BOLEH DIGUNAKAN, BILA DI ROLLBACK
			commonDao.delete("DELETE FROM EKA.MST_UPLOAD WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
			commonDao.delete("DELETE FROM EKA.MST_UPLOAD_NON WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
			commonDao.delete("DELETE FROM EKA.MST_UPLOAD_NON_NEW WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
			commonDao.delete("DELETE FROM EKA.MST_COMM_BONUS WHERE REG_SPAJ = '" + spaj + "'");
			commonDao.delete("DELETE FROM EKA.MST_COMMISSION WHERE REG_SPAJ = '" + spaj + "'");
			commonDao.delete("DELETE FROM EKA.MST_REWARD WHERE REG_SPAJ = '" + spaj + "'");
			//
			
			String lca_id = uwDao.selectCabangFromSpaj(spaj);
			if("58, 63, 65".indexOf(lca_id)==-1){
				transferPolis.testHitungKomisi(spaj, currentUser);
			}
			//produksi.prosesUlinkDetBill(spaj, defaultDateFormat.parse("30/04/2007"));
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
	}
	
	public void hitungKomisiTopupOnly(String spaj, User currentUser) {
		try {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			
			//INI HANYA BOLEH DIGUNAKAN, BILA DI ROLLBACK
//			commonDao.delete("DELETE FROM EKA.MST_UPLOAD WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
//			commonDao.delete("DELETE FROM EKA.MST_UPLOAD_NON WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
//			commonDao.delete("DELETE FROM EKA.MST_UPLOAD_NON_NEW WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
//			commonDao.delete("DELETE FROM EKA.MST_COMM_BONUS WHERE REG_SPAJ = '" + spaj + "'");
//			commonDao.delete("DELETE FROM EKA.MST_COMMISSION WHERE REG_SPAJ = '" + spaj + "' AND MSBI_PREMI_KE = 2");
//			commonDao.delete("DELETE FROM EKA.MST_REWARD WHERE REG_SPAJ = '" + spaj + "' AND MSBI_PREMI_KE = 2");
			//
			
			transferPolis.testHitungKomisiTopUpOnly(spaj, currentUser);
			//produksi.prosesUlinkDetBill(spaj, defaultDateFormat.parse("30/04/2007"));
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
	}
	
	public void prosesKomisiBySelect(List<String> daftarEspeaje, User user){
		try {
			int i = 0;
			for(String spaj : daftarEspeaje){
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				
				//INI HANYA BOLEH DIGUNAKAN, BILA DI ROLLBACK
//				commonDao.delete("DELETE FROM EKA.MST_UPLOAD WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
//				commonDao.delete("DELETE FROM EKA.MST_UPLOAD_NON WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
//				commonDao.delete("DELETE FROM EKA.MST_UPLOAD_NON_NEW WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
//				commonDao.delete("DELETE FROM EKA.MST_COMM_BONUS WHERE REG_SPAJ = '" + spaj + "'");
//				commonDao.delete("DELETE FROM EKA.MST_COMMISSION WHERE REG_SPAJ = '" + spaj + "' AND MSBI_PREMI_KE = 2");
//				commonDao.delete("DELETE FROM EKA.MST_REWARD WHERE REG_SPAJ = '" + spaj + "' AND MSBI_PREMI_KE = 2");
				//
//				transferPolis.testHitungKomisi(spaj, user);
				transferPolis.testHitungKomisiTopUpOnly(spaj, user);
				i++;
			}
		}catch (Exception e) {
			logger.error("ERROR :", e);
		}
		
	}
	
	public void hitungUPP(String spaj, Integer prod_ke){
		Date inputDate = this.commonDao.selectSysdateTruncated(0);
		Date prodDate = inputDate;
		
		Upp upp = this.uwDao.selectAgenBuatHitungUppEvaluasi(spaj, 1);
		upp.setLstb_id(1);
		upp.setUpp_bonus(0.);

		List topupList = this.uwDao.selectTopUp(spaj, 1, 1, "desc");
		Date rkDate = ((TopUp) topupList.get(0)).getMspa_date_book(); 
		String lca_id = uwDao.selectCabangFromSpaj(spaj);
		String lsbs = uwDao.selectLsbsId(spaj);
		String lsdbs = uwDao.selectLsdbsNumber(spaj);
		Integer jenis = 1;
		if((lsbs.equals("190") && lsdbs.equals("9")) || (lsbs.equals("200") && lsdbs.equals("7"))) jenis = 3;
		
		prodDate = commonDao.selectTanggalProduksiUntukProsesProduksi(rkDate, lca_id, jenis);
		upp.setMspro_prod_date(prodDate);
		prosesPerhitunganUpp(upp);
	}
	
	/* ======================= AjaxManager ======================= */
	public List list_rider(String kode_bisnis, String lsdbs_number) {
		return ajaxManager.list_rider(kode_bisnis, lsdbs_number);
	}
	
	public String cekRekAgen2(String spaj){
		return uwDao.cekRekAgen2(spaj);
		/*return uwDao.cekKdBankRekAgen2(spaj);*/
		//return uwDao.cekSetifikatAgen(spaj);
	}
	public String cekKdBankRekAgen2(String spaj){
		return uwDao.cekKdBankRekAgen2(spaj);
	}
	
	public String cekSetifikatAgen(String spaj){
		return uwDao.cekSetifikatAgen(spaj);
	}
	
	public List cekSertifikat(String msag_id){
		return uwDao.cekSertifikat(msag_id);
	}
	/* ======================= CancelPolis ======================= */
	public void cancelPolisFromPayment(String spaj, String alasan, User currentUser) {
		this.cancelPolis.cancelPolisFromPayment(spaj, alasan, currentUser);
	}

	public void cancelPolisFromTandaTerimaPolis(String spaj, String alasan, User currentUser) {
		this.cancelPolis.cancelPolisFromTandaTerimaPolis(spaj, alasan, currentUser);
	}

	public void cancelPolisFromUw(String spaj, String ls_alasan, String lus_id) {
		this.cancelPolis.cancelPolisFromUw(spaj,ls_alasan,lus_id);
	}
	
	/* ======================= Endorsement ======================= */
	public Map prosesCetakEndorsementBaru(String noEndorse, String tipe){
		return this.endorsement.prosesCetakEndorsementBaru(noEndorse, tipe);
	}
	
	/* ======================= Manfaat ======================= */
	public Map prosesCetakManfaat(String spaj, String lus_id, HttpServletRequest request){
		try {
			return this.manfaat.prosesCetakManfaat(spaj, lus_id, request);
		} catch (Exception e) {
			logger.error("ERROR :", e);
			return null;
		}
	}	

	/* ======================= Nab ======================= */
	public List prosesHitungUnit(int pos, String startDate, String endDate, String nabDate, User user) {
		return this.nab.prosesHitungUnit(pos, startDate, endDate, nabDate, user);
	}
		
	/* ======================= NilaiTunai ======================= */
	public List viewNilai(String spaj, String lus_id, int flag_proses, int flag_fix) throws Exception {
		return this.nilaiTunai.proses(spaj, lus_id, "tampil", flag_proses, flag_fix);
	}
	
	public boolean prosesNilaiLama(List<String> daftarEspeaje, String lus_id, int flag_fix) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return this.nilaiTunai.prosesNilaiLama(daftarEspeaje, lus_id, flag_fix);
	}

	/* ======================= SuratUnitLink ======================= */
	public Map cetakSuratEndorsemen(List view, String spaj, boolean flagUpdate, int judulId, int pilihView,int tu_ke) throws Exception{
		return this.suratUnitLink.cetakSuratEndorsemen(view, spaj, flagUpdate, judulId, pilihView, tu_ke);
	}
	
	public Map cetakSuratUnitLink(List view, String spaj, boolean flagUpdate, int judulId, int pilihView,int tu_ke) throws Exception{
		return this.suratUnitLink.cetakSuratUnitLink(view, spaj, flagUpdate, judulId, pilihView, tu_ke);
	}
	
	/*======================== HistoryKirimPolis =================== */
	public Map cetakHistoryKirimPolis(List view, String startDate, String endDate, int kondisi,boolean flagUpdate, int judulId, int pilihView) throws Exception{
		return this.historyKirimPolis.cetakHistoryKirimPolis(view, startDate, endDate, kondisi,flagUpdate, judulId, pilihView);
	}
	
	/* ======================= TransferPolis ======================= */
	public Map transferKomisiToFilling(String spaj, int tahun, int premi, String lus_id, String palembang) {
		return this.transferPolis.transferKomisiToFilling(spaj, tahun, premi, lus_id, palembang);
	}
	public String transferPrintPolisToFillingAtauTandaTerimaPolis(String spaj, User currentUser, int isInputanBank){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String result = this.transferPolis.transferPrintPolisToFillingAtauTandaTerimaPolis(spaj, currentUser, isInputanBank);
		
		//Yusuf (10/02/2010) - Req Dr. Ingrid, untuk produk2 ini, langsung tembusin TTP
		if(products.isLangsungTransferKeKomisi(spaj)){
			int lssa_id = selectStatusAksep(spaj);
			Map pemegang = this.uwDao.selectPemegang(spaj);
			String to[]=null;
			String cc="";
			String lcaId=(String)pemegang.get("LCA_ID");
			String nama_dist = (String) pemegang.get("NAMA_DIST");
			if(nama_dist.equals("AGENCY") || nama_dist.equals("REGIONAL")){
				to = props.getProperty("agensys.emails").split(";");
			}else if(nama_dist.equals("MNC") || nama_dist.equals("WORKSITE")){
				to = props.getProperty("worksite.emails").split(";");
			}else if(nama_dist.equals("BANCASS")){
				to = props.getProperty("bancass.emails").split(";");
			}else if(nama_dist.equals("DMTM")){
				to = props.getProperty("dmtm.emails").split(";");
			}else if(nama_dist.equals("MALLASSURANCE")){
				to=props.getProperty("agensys.emailsMall").split(";");
			}
			if(lssa_id == 5){
				//sebelum transfer ke finance, harus cek rekening agen gak boleh kosong!
				String errorRekening = cekRekAgen2(spaj);
				if(errorRekening.equals("")) {
			        try {
			        	cc="santi@sinarmasmsiglife.co.id;";
			        	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
								null, 0, 0, new Date(), null, 
								false, props.getProperty("admin.ajsjava"), to, new String[]{currentUser.getEmail()}, new String[]{"ryan@sinarmasmsiglife.co.id"}, 
								"[E-Lions] Rekening Agen "+errorRekening+" Kosong", 
								"Harap cek rekening agen: \n\n"  +errorRekening+" karena rekeningnya masih kosong, sehingga komisinya tidak dapat diproses. Terima kasih."+
								"\n\nNo SPAJ  "  + spaj +
								"\n\nINFO UNTUK UNDERWRITING: Polis ini ditransfer ke posisi TANDA TERIMA, silahkan transfer ke finance HANYA SETELAH REKENING AGEN DI-UPDATE." +
								"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null, spaj);
//						 prosesTransferKomisiOrFilling(spaj, 9, currentUser.getLus_id(), commonDao.selectSysdate(), "TRANSFER KE CHECKING TANDA TERIMA(REKENING AGEN MASIH KOSONG).");
//							result = "CHECKING TTP";
							
						//Deddy 20 Nov 2014 : polis tidak ada yang masuk ke checking TTP lagi.Langsung ditransfer ke Filling apabila rekening atau sertifikat expired.Nanti dijaga pada saat proses komisi.
						prosesTransferKomisiOrFilling(spaj, 99, currentUser.getLus_id(), commonDao.selectSysdate(), "TRANSFER KE FILLING.");
						result = "FILLING";
			        } catch (MailException e) {
						logger.error("ERROR :", e);
					}
				}else{
					String lca_id = selectLcaIdBySpaj(spaj);
					if("01,55,62".indexOf(lca_id)>-1){
						//transfer lagi dari TANDA TERIMA ke AGENT COMMISSION
						prosesTransferKomisiOrFilling(spaj, 99, currentUser.getLus_id(), commonDao.selectSysdate(), "TRANSFER LANGSUNG KE FILLING.");
						result = "FILLING";
					}else{
						//transfer lagi dari TANDA TERIMA ke AGENT COMMISSION
						prosesTransferKomisiOrFilling(spaj, 8, currentUser.getLus_id(), commonDao.selectSysdate(), "TRANSFER LANGSUNG KE KOMISI (FINANCE).");
						result = "KOMISI FINANCE";
					}
					
				}
			}
		}
		String businessId = this.uwDao.selectBusinessId(spaj);
		Integer businessNumber = this.uwDao.selectBusinessNumber(spaj);
		
		String lcaid = uwDao.selectCabangFromSpaj(spaj);
		Product detailProduk = akseptasiDao.selectMstProductInsuredDetail(spaj, Integer.parseInt(businessId), businessNumber);
		if(lcaid.equals("58") && currentUser.getLca_id().equals("58") && detailProduk.getMspr_tsi()<= new Double(200000000)){
			
			try {
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, new Date(), null, 
						false, props.getProperty("admin.ajsjava"), props.getProperty("uw.dmtm").split(";"), null, new String[]{"deddy@sinarmasmsiglife.co.id"}, 
						"[E-Lions] Transfer Dari Mall Assurance", 
						"\nINFO UNTUK UNDERWRITING: SPAJ MallAssurance dengan nomor "+FormatString.nomorSPAJ(spaj)+" telah ditransfer ke posisi Underwriting, Silakan diproses selanjutnya." +
						"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null, spaj);
			} catch (MailException e) {
				logger.error("ERROR :", e);
			} 
		}
		//ryan, ngirim sms ke nasabah , untuk kode 203
		if(businessId.equals("203")){
			Map pemegang = this.uwDao.selectPemegang(spaj);
			   String no =(String)pemegang.get("NO_HP");
			   Integer kelamin =(Integer)pemegang.get("JENIS");
			   String nama =(String)pemegang.get("MCL_FIRST");
				try {
					Smsserver_out sms_out = new Smsserver_out();
					sms_out.setRecipient(no);
					sms_out.setJenis(22);
					sms_out.setLjs_id(22);
					sms_out.setText("Yth."+( kelamin ==1?"BPK. ":"IBU. ")+ nama +",SOFTCOPY POLIS SILAKAN AKSES KE HTTPS://EPOLICY.SINARMASMSIGLIFE.CO.ID/ DAN SDH DIKIRIM KE ALAMAT EMAIL "+( kelamin ==1?"BPK. ":"IBU. "));
				    basDao.insertSmsServerOutWithGateway(sms_out, true);
					
				} catch (Exception e){
					logger.error("ERROR :", e);
			  }
		}		
		return result;
	}
/*	public void transferTandaTerimaPolisToKomisiOrFilling(String spaj, String lus_id, Date date) {
		this.transferPolis.transferTandaTerimaPolisToKomisiOrFilling(spaj, lus_id, date);
	}*/
	public void prosesTransferKomisiOrFilling(String spaj,Integer li_pos,String lus_id,Date tanggal,String ket){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		this.transferPolis.prosesTransferKomisiOrFilling(spaj, li_pos, lus_id, tanggal, ket);
	}
	public String transferTandaTerimaPolisToKomisiOrFillingNew(Ttp ttp, User currentUser) {
//      TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return this.transferPolis.transferTandaTerimaPolisToKomisiOrFillingNew(ttp, currentUser);
	}
	public String transferTandaTerimaPolisToKomisiOrFilling30Hari(User currentUser,BindException err) throws Exception {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return this.transferPolis.transferTandaTerimaPolisToKomisiOrFilling30Hari(currentUser,err);
	}
	
	public boolean transferToPrintPolis(String reg_spaj, BindException errors, int bulanRK, User currentUser, String li_retur, String li_returTopUp, String from) throws Exception{
//        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		List billInfo = this.uwDao.selectBillingInfoForTransfer(reg_spaj, 1, 1);
		if(errors == null) errors = new BindException(new HashMap(), "cmd");
		boolean hasil = this.transferPolis.transferToPrintPolis(reg_spaj, errors, bulanRK, billInfo, currentUser, li_retur, li_returTopUp, from, this, uwManager);
		
		if(!hasil) {
			try {
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, new Date(), null, 
						false, props.getProperty("admin.ajsjava"), new String[] {props.getProperty("admin.yusuf")}, null, null, 
						"GAGAL Transfer SPAJ " + reg_spaj + " oleh "+ currentUser.getName() + " ["+currentUser.getDept()+"]", reg_spaj+"\n"+errors, null, reg_spaj);
			} catch (MailException e) {
				logger.error("ERROR :", e);
			}
			
		}
		
		return hasil;
	}
	public Integer prosesCheckingTtp(Ttp ttp,BindException err) throws Exception{
		return this.transferPolis.prosesCheckingTtp(ttp,err);
	}
	public List transferKustodianToNabOrFilling(int lspd, String[] spaj) {
		return this.transferPolis.transferKustodianToNabOrFilling(lspd, spaj);
	}
	/**Fungsi:	Untuk melakukan proses transfer balik ke TTP, pada menu Entry=>UW=>Checking proses TTP
	 * @param 	String spaj,Integer li_pos,User currentUser,String ket
	 * @return	Integer
	 * @author 	Ferry Harlim
	 */
	public void transferBalikToTtp(String spaj,Integer li_pos,User currentUser,String ket){
		this.transferPolis.transferBalikToTtp(spaj, li_pos, currentUser, ket);
	}

	/*=======================ChecklistDao===================================*/
	public int selectPosisiDokumenBySpaj(String reg_spaj){
		return this.checklistDao.selectPosisiDokumenBySpaj(reg_spaj);
	}
	
	public int selectStsAksepBySpaj(String reg_spaj){
		return this.checklistDao.selectStsAksepBySpaj(reg_spaj);
	}
	
	public String selectLcaIdBySpaj(String reg_spaj){
		return this.checklistDao.selectLcaIdBySpaj(reg_spaj);
	}
	
	public boolean selectValidasiCheckListBySpaj(String reg_spaj) {
		return this.checklistDao.selectValidasiCheckListBySpaj(reg_spaj);
	}
	
	public List<Checklist> selectCheckListBySpaj(String reg_spaj) {
		return this.checklistDao.selectCheckListBySpaj(reg_spaj);
	}
	
	public void saveChecklist(CommandChecklist cmd, User currentUser) {
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		this.checklistDao.saveChecklist(cmd, currentUser);
	}
	
	public void saveChecklistBancass(CommandChecklist cmd, User currentUser) {
		this.checklistDao.saveChecklistBancass(cmd, currentUser);
	}
	
	/*=======================SimasCardDao===================================*/
	public void updateSuratSimasCard(Integer flag_print, Integer msc_jenis, String no_kartu) {
		this.simasCardDao.updateSuratSimasCard(flag_print, msc_jenis, no_kartu);
	}
	
	public void updateKartuSimasCard(Integer flag_print, Date tgl_print, Integer msc_jenis, String no_kartu) {
		this.simasCardDao.updateKartuSimasCard(flag_print, tgl_print, msc_jenis, no_kartu);
	}
	
	public List<Simcard> selectCetakSimasCard(int msc_jenis, String lca_id, int flag_print, String cabangBII, String cabangBankSinarmas, int jumlah_print) {
		return this.simasCardDao.selectCetakSimasCard(msc_jenis, lca_id, flag_print, cabangBII, cabangBankSinarmas, jumlah_print);
	}
	
	public List<DropDown> selectJenisSimasCard(){
		return this.simasCardDao.selectJenisSimasCard();
	}
	
	public List selectCabangSimasCard() throws DataAccessException{
		return this.simasCardDao.selectCabangSimasCard();
	}
	
	public List<DropDown> selectCabangBankSimasCard(int bank) {
		return this.simasCardDao.selectCabangBankSimasCard(bank);
	}
	
	public List<DropDown> selectCariSimasCard(int jenis, String kata) {
		return this.simasCardDao.selectCariSimasCard(jenis, kata);
	}
	
	public Simcard selectSimcard(String msc_jenis, String no_kartu) {
		return this.simasCardDao.selectSimcard(msc_jenis, no_kartu);
	}
	
	public void saveSimcard(Simcard s, User u) {
		this.simasCardDao.saveSimcard(s, u);
	}
	
	/*=======================RekruitmentDao=================================*/
	public void insert_mst_kuesioner(Kuesioner kuesioner)
	{
		this.rekruitmentDao.insertmst_kuesioner(kuesioner);
	}
	
	public Integer cek_kuesioner(String nama,String tgllhr)
	{
		return (Integer) this.rekruitmentDao.cek_kuesioner(nama, tgllhr);
	}
	
	public Kuesioner selectkuesioner(String no_rekrut)
	{
		return (Kuesioner) this.rekruitmentDao.selectkuesioner(no_rekrut);
	}
	
	/*=======================WorksiteDao===================================*/
	public List select_nama_perusahaan(){
		return this.worksiteDao.select_nama_perusahaan();
	}		
	
	public List select_nama_perusahaan2(){
		return this.worksiteDao.select_nama_perusahaan2();
	}		
	
	public List select_invoice(String id)
	{		
		return this.worksiteDao.select_invoice(id);
	}
	
	public List select_invoice_lanjutan(String id)
	{		
		return this.worksiteDao.select_invoice_lanjutan(id);
	}
	
	public Long select_counter_invoice(Map param)
	{
		return this.worksiteDao.select_counter_invoice(param);
	}
	
	public Map select_max_invoice(String id)  {
		return this.worksiteDao.select_max_invoice(id);
	}
	
	public Map select_max_invoice_lanjutan(String id)  {
		return this.worksiteDao.select_max_invoice_lanjutan(id);
	}
	
	public void update_mst_counter(Map param) throws DataAccessException {
		this.worksiteDao.update_mst_counter(param);
	}	
	
	public void update_counter_mst_insured(Map param) throws DataAccessException {
		this.worksiteDao.update_counter_mst_insured(param);
	}	
	
	public Map select_billing(String spaj, Integer ke)  {
		return this.worksiteDao.select_billing(spaj,ke);
	}
	
	public Map select_billing_endow_spaj(String spaj, Integer ke)  {
		return this.worksiteDao.select_billing_endow_spaj(spaj,ke);
	}
	
	public void update_nobilling(Map param) throws DataAccessException {
		this.worksiteDao.update_nobilling(param);
	}	
	
	public List select_invoice_endow(String id, String tgl1, String tgl2)
	{		
		return this.worksiteDao.select_invoice_endow(id,tgl1,tgl2);
	}
	
	public List select_invoice_endow_perspaj(String id)
	{		
		return this.worksiteDao.select_invoice_endow_perspaj(id);
	}
	
	public Map select_max_invoice_endow(String id, String tgl1, String tgl2)  {
		return this.worksiteDao.select_max_invoice_endow(id,tgl1,tgl2);
	}
	
	public Map select_max_invoice_endow_perspaj(String id)  {
		return this.worksiteDao.select_max_invoice_endow_perspaj(id);
	}
	
	public List select_kwitansi_endow(String id, String tgl1, String tgl2)  {
		return this.worksiteDao.select_kwitansi_endow(id,tgl1,tgl2);
	}	
	
	public List select_billing_endow(Integer ke, String kode, String bisnis, String tgl1, String tgl2)  {
		return this.worksiteDao.select_billing_endow(ke,kode,bisnis,tgl1,tgl2);
	}		
	
	public Map select_max_billing_endow(Integer ke, String kode, String bisnis, String tgl1, String tgl2)  {
		return this.worksiteDao.select_max_billing_endow(ke,kode,bisnis,tgl1,tgl2);
	}
	
	public Map selectInformasiPowersaveUntukFeeBased(String spaj) {
		return this.bacDao.selectInformasiPowersaveUntukFeeBased(spaj);
	}
	
	/*=======================HitungBAC===================================*/
	public Map hitbac(Object cmd,Errors err, String kurs, Double up,Integer flag_account , String lca_id , Integer autodebet , Integer flag_bao1,Integer kode_flag,Integer flag_rider,Integer pmode, Double premi, Double premi_berkala,Double premi_tunggal, Integer flag_as, Integer pil_berkala,Integer pil_tunggal,Double li_pct_biaya, Double ld_premi_invest, Boolean  flag_minus ,Double jumlah_minus, Integer flag_powersave,Integer flag_bulanan, String param) throws ServletException,IOException,Exception
	{
		if (param.equalsIgnoreCase("bungapowersave"))
		{
			return this.hitungBac.proseshitungpowersave( cmd,err,kurs,up,flag_account ,lca_id ,autodebet ,flag_bao1,  premi ,flag_powersave,flag_bulanan);
			
		}else if (param.equalsIgnoreCase("biayaexcell"))
			{
				return this.hitungBac.proseshitungrider( cmd,err,kode_flag ,flag_rider, pmode , kurs , premi , up , premi_berkala, premi_tunggal , flag_as, pil_berkala, pil_tunggal);

			}else if (param.equalsIgnoreCase("fundexcell"))
				{
					return this.hitungBac.proseshitungfund(cmd,err,li_pct_biaya,kode_flag ,ld_premi_invest , flag_minus , premi_berkala, premi_tunggal ,jumlah_minus);
				}else if (param.equalsIgnoreCase("fundexcell1"))
					{
						return this.hitungBac.proseshitungfund1(cmd,err,li_pct_biaya,kode_flag ,ld_premi_invest , flag_minus ,premi_berkala, premi_tunggal ,jumlah_minus);
					}else if (param.equalsIgnoreCase("hcp"))
						{
							return this.hitungBac.proseshitunghcp( cmd,err,kode_flag ,flag_rider, pmode , kurs , premi , up , premi_berkala, premi_tunggal , flag_as, pil_berkala, pil_tunggal);

						}
		return null;
	}
		
	public Map validbac(Object cmd,Errors err, String param ,String status) throws ServletException,IOException,Exception
	{
		if (param.equalsIgnoreCase("hcp"))
		{
			return this.hitungBac.validhcp(cmd, err,status);
		}else if (param.equalsIgnoreCase("simas"))
			{
				return this.hitungBac.validsimas(cmd, err,status);
			}
		return null;
	}
	/* ======================= AkseptasiDao ======================= */
	public Double f_get_sar(int ai_type, int ai_bisnis_id,int ai_bisnis_no,String as_kurs,int ai_cbayar,int ai_lbayar,//ok
			int ai_ltanggung,int ai_year,int ai_age) throws Exception{
		return this.akseptasiDao.f_get_sar(ai_type, ai_bisnis_id,ai_bisnis_no,as_kurs,ai_cbayar, ai_lbayar,//ok
				ai_ltanggung, ai_year, ai_age);
	}
	
	public String f_get_nopolis(String lcaId,Integer lsbsId)throws Exception{
		return this.akseptasiDao.f_get_nopolis(lcaId, lsbsId);
	}
	
	public List selectMstPolicy(Integer lspdId,Integer lstbId,Integer lsspId1,Integer lsspId2,String kata,String kategori){
		return this.akseptasiDao.selectMstPolicy(lspdId,lstbId,lsspId1,lsspId2,kata,kategori);
	}
	/**@Fungsi:	Untuk Menampilkan Produk-produk apa saja yang dipilih pada spaj tersebut
	 * @param	String spaj,Integer insured,Integer active
	 * @return 	List
	 * */
	public List selectMstProductInsured(String spaj,Integer insured,Integer active){
		return this.akseptasiDao.selectMstProductInsured(spaj,insured,active);
	}
	
	public List selectMstProductInsured(String spaj){
		return this.akseptasiDao.selectMstProductInsured(spaj);
	}
	
	public String selectMstProductInsured3(String spaj){
		return this.akseptasiDao.selectMstProductInsured3(spaj);
	}
	
	public List selectLstDetBisnisAll(){
		return this.akseptasiDao.selectLstDetBisnisAll();
	}
	
	public List selectLstDetBisnisRider(String filBisnisId){
		return this.akseptasiDao.selectLstDetBisnisRider(filBisnisId);
	}

	public Double selectLstPremiEm(Integer lsbsId,String kursId,Integer caraBayar,Integer lamaBayar,
			Integer lamaTanggung,Integer umur,Double extra){
		return this.akseptasiDao.selectLstPremiEm(lsbsId,kursId,caraBayar,lamaBayar,lamaTanggung,umur,extra);
	}

	public Tertanggung2 selectTertanggung(String spaj,Integer lsle_id){
		return this.akseptasiDao.selectTertanggung(spaj ,lsle_id);
	}
	
	public Pemegang2 selectPemegang(String spaj,Integer lsle_id){
		return this.akseptasiDao.selectPemegang(spaj,lsle_id);
	}
	
	public DataUsulan2 selectDataUsulan(String spaj,Integer lstb_id){
		return this.akseptasiDao.selectDataUsulan(spaj,lstb_id);
	}

	public Integer selectMstInsuredMsteStandard(String spaj,Integer insuredNo){
		return this.akseptasiDao.selectMstInsuredMsteStandard(spaj,insuredNo);
	}
	
	public void insertMstProductInsured(Product productInsert){
		this.akseptasiDao.insertMstProductInsert(productInsert);
	}
	
	public void updateMstProductInsured(List lsUpdate){
		for(int i=1;i<lsUpdate.size();i++){
			Product productUp=new Product();
			productUp=(Product) lsUpdate.get(i);
			this.akseptasiDao.updateMstProductInsert(productUp);
		}
	}
	
	public List selectMstPositionSpajAkseptasi(String spaj){
		return this.akseptasiDao.selectMstPositionSpaj(spaj);
	}
	
	public Map selectMstCntPolis(String lcaId,Integer lsbsId){
		return this.akseptasiDao.selectMstCntPolis(lcaId,lsbsId);
	}

	public void insertMstCntPolis(String lcaId,Integer lsbsId,Long ldNo){
		this.akseptasiDao.insertMstCntPolis(lcaId,lsbsId,ldNo);
	}
	
	public void updateMstCntPolis(String lcaId,Integer lsbsId,Long ldNo){
		this.akseptasiDao.updateMstCntPolis(lcaId,lsbsId,ldNo);
	}
	
	public void prosesAksep1(String mspoPolicyNo,String regSpaj,Integer lspdId, Integer lstbId,Position position){
		this.akseptasiDao.updateMstPolicy1(mspoPolicyNo,regSpaj,lspdId,lstbId);
		insertMstPositionSpaj(position.getLus_id(), position.getMsps_desc(), position.getReg_spaj(), 0);
	}
	
	public void prosesAksep2(Position position){
		insertMstPositionSpaj(position.getLus_id(), position.getMsps_desc(), position.getReg_spaj(), 0);
	}

	public void prosesAksep3(String regSpaj,Integer lspdIdNew,Integer lspdIdOld,Integer lstbId,Position position){
		this.akseptasiDao.updateMstPolicy2(regSpaj,lspdIdNew,lspdIdOld,lstbId);
		insertMstPositionSpaj(position.getLus_id(), position.getMsps_desc(), position.getReg_spaj(), 0);
	}
	
	public String selectMstCancelRegSpaj(String spaj){
		return this.akseptasiDao.selectMstCancelRegSpaj(spaj);
	}
	
	public Map selectMstBillingMax(String spaj){
		return this.akseptasiDao.selectMstBillingMax(spaj);
	}
	
	public Map mst_billing(String spaj , String data)
	{
		return this.akseptasiDao.mst_billing(spaj,  data);
	}
	
	public String cabang_production(String spaj, Integer prod_ke)
	{
		return (String)this.akseptasiDao.cabang_production(spaj, prod_ke);
	}
	
	public Map mst_billing_ke(String spaj , Integer ke)
	{
		return (HashMap) this.akseptasiDao.mst_billing_ke(spaj, ke);
	}	
	
	public void updateBegDatePolis(String reg_spaj){
		this.uwDao.updateBegDatePolis(reg_spaj);
	}
	
	public List selectDaftarAkseptasiNyangkut3(int lssp_id,int lssa_id, boolean isEmailRequired,String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12) {
		return this.uwDao.selectDaftarAkseptasiNyangkut3(lssp_id,lssa_id, isEmailRequired, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12);
	}
	
	public List financeTopUp(boolean isEmailRequired,String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12) {
		return this.uwDao.financeTopUp(false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12);
	}
	
	public String selectNpwpAgen(String msag_id) {
		return this.uwDao.selectNpwpAgen(msag_id);
	}
	
	public int selectStatusAksep(String reg_spaj) {
		return this.uwDao.selectStatusAksep(reg_spaj);
	}
	
	public int selectPrintCabangAtauPusat(String reg_spaj) {
		return this.uwDao.selectPrintCabangAtauPusat(reg_spaj);
	}
	
	public String selectEmailVPFromSpaj(String reg_spaj) {
		return this.uwDao.selectEmailVPFromSpaj(reg_spaj);
	}
	
	public Map selectPolisPowersaveDMTM(String reg_spaj) {
		return this.uwDao.selectPolisPowersaveDMTM(reg_spaj);
	}
	
	public List selectRiderPolisPas(String reg_spaj) {
		return this.uwDao.selectRiderPolisPas(reg_spaj);
	}
	
	public Map selectPolisPas(String reg_spaj){
		return this.uwDao.selectPolisPAS(reg_spaj);
	}
	
	public List<Pbp> selectPbp(String reg_spaj){
		return this.uwDao.selectPbp(reg_spaj);
	}
	
	public Powersave selectInformasiPbp(String reg_spaj) {
		return this.uwDao.selectInformasiPbp(reg_spaj);
	}
	
	public List selectDaftarEmailCabang() {
		return this.uwDao.selectDaftarEmailCabang();
	}
	
	public List<Drek> selectMstDrek(int lsrek_id, int lsbp_id, String kode, Date startDate, Date endDate, Double startNominal, Double endNominal){
		List<Drek> returnValue = this.uwDao.selectMstDrek(lsrek_id, lsbp_id, kode, startDate, endDate, startNominal, endNominal);
		if( returnValue != null && returnValue.size() > 0 )
		{
			for( int i = 0 ; i < returnValue.size() ; i ++ )
			{
				if( returnValue.get(i).getFlag_tunggal_gabungan() != null )
				{
					if( returnValue.get(i).getFlag_tunggal_gabungan() == 0 )
					{
						returnValue.get(i).setFlag_tunggal("checked");
						returnValue.get(i).setRadioBoxGabunganDisabled("disabled");
					}
					else if( returnValue.get(i).getFlag_tunggal_gabungan() == 1 )
					{
						returnValue.get(i).setFlag_gabungan("checked");
						returnValue.get(i).setRadioBoxGabunganDisabled("");
					}
				}
				else if( returnValue.get(i).getNo_spaj() != null && !"".equals( returnValue.get(i).getNo_spaj() )
						&& returnValue.get(i).getFlag_tunggal_gabungan() == null )
				{
					returnValue.get(i).setRadioBoxGabunganDisabled("disabled");
				}
			}
		}
		
		return returnValue;
	}
	
	public List<ProSaveBayar> selectMstProSave(int mpb_flag_bs, String lcb_no, Date startDate, Date endDate){
		return this.uwDao.selectMstProSave(mpb_flag_bs, lcb_no, startDate, endDate);
	}
	
	public int selectJumlahTransferPbp(String reg_spaj) {
		return this.uwDao.selectJumlahTransferPbp(reg_spaj);
	}
	
	public int selectValidasiTransferPbp(String reg_spaj) {
		return this.uwDao.selectValidasiTransferPbp(reg_spaj);
	}
	
	public int selectValidasiPbp(String reg_spaj) {
		return this.uwDao.selectValidasiPbp(reg_spaj);
	}
	
	public String transferPbp(Command cmd, User user) {
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return this.uwDao.transferPbp(cmd, user);
	}
	
	public String savePbp(Command cmd, User user) {
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return this.uwDao.savePbp(cmd, user);
	}
	
	public List<DropDown> selectDropDown(String table_name, String key_column, String value_column, String desc_column, String order_column, String where_clause){
		return this.uwDao.selectDropDown(table_name, key_column, value_column, desc_column, order_column, where_clause);
	}
	
	public List<DropDown> selectDropDownUserUw(String table_name, String key_column, String value_column, String order_column, String where_clause){
		return this.uwDao.selectDropDownUserUw(table_name, key_column, value_column, order_column, where_clause);
	}
	
	public List<DropDown> selectDropDownHashMap(String table_name, String key_column, String value_column, String order_column, String where_clause){
		return this.uwDao.selectDropDownHashMap(table_name, key_column, value_column, order_column, where_clause);
	}
	
	public Integer selectCerdasSalesOrOperation(String spaj) {
		return this.uwDao.selectCerdasSalesOrOperation(spaj);
	}
	
	public Date selectTanggalBayar(String spaj) {
		return this.uwDao.selectTanggalBayar(spaj);
	}
	
	public Map selectMstCounter(Integer mscoNumber, String lcaId){
		return this.uwDao.selectMstCounter(mscoNumber,lcaId);
	}

	public Date kurang_tanggal(String tanggal, Integer hari)throws DataAccessException {
		return this.uwDao.kurang_tanggal(tanggal, hari);
	}
	
	public Date validationTglKirim(String spaj)throws DataAccessException {
		return this.uwDao.validationTglKirim(spaj);
	}
	
	public Integer selectCountsimassehat(String tgl1 , String tgl2)throws DataAccessException {
		return this.uwDao.selectCountsimassehat(tgl1, tgl2);
	}	
	
/*	public void updateMstCounter(Integer number,String lcaId,Integer value){
		this.akseptasiDao.updateMstCounter(number,lcaId,value);
	}*/

	public Date selectFAddMonths(String tgl,Integer bulan){
		return this.akseptasiDao.selectFAddMonths(tgl,bulan);
	}

	public Map selectLstDailyCurrency(Date tgl,String lkuId){
		return akseptasiDao.selectLstDailyCurrency(tgl,lkuId);
	}
	
	public Date selectSysdate(Integer flag){
		//return akseptasiDao.selectSysdate(flag);
		if (flag == 1){
			return commonDao.selectSysdateTrunc();
		}else{
			return commonDao.selectSysdate();
		}
	}
	
	public Date selectSysdate1(String add, boolean trunc, int nilai) {
		return commonDao.selectSysdate(add, trunc, nilai);
	}
	
	public int prosesTransfer(String spaj,List lsProdukInsured,Product produkInsured,Integer tahunKe,Integer premiKe,
			   String lcaId,Integer lsspId,String lwkId,String lsrgId,String lkuId,Integer lusId,String mspoPolicyNo,
			   Integer lspdId,Integer lstbId)throws Exception{
		return this.akseptasiDao.prosesTransfer(spaj, lsProdukInsured, produkInsured, tahunKe, premiKe, lcaId, lsspId, 
				lwkId, lsrgId, lkuId, lusId, mspoPolicyNo, lspdId, lstbId);
	}
	
	/* ======================= BacDao ======================= */
	
	public Integer selectPosisiUlink(String reg_spaj){
		return bacDao.selectPosisiUlink(reg_spaj);
	}
	
	public String selectCabangBiiPolis(String reg_spaj){
		return bacDao.selectCabangBiiPolis(reg_spaj);
	}
	
	public String saveTopupStableLink(InputTopup trans, List<InputTopup> daftarTopup, User currentUser) throws ParseException {
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return bacDao.saveTopupStableLink(trans, daftarTopup, currentUser);
	}
	
	public String otorisasiTopUpStabilLink(Integer mslTuKe, String ljiId, String lusId, String regSpaj) throws ParseException{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return bacDao.otorisasiTopUpStabilLink(mslTuKe, ljiId, lusId, regSpaj);
	}
	
	public InputTopup selectInputTopupBaruStableLink(String reg_spaj) throws DataAccessException{
		return bacDao.selectInputTopupBaruStableLink(reg_spaj);
	}
	
	public InputTopup selectEntryTransStableLink(String reg_spaj){
		return bacDao.selectEntryTransStableLink(reg_spaj);
	}
	
	public List<InputTopup> selectEntryTopupStableLink(String reg_spaj, int msl_posisi){
		List<InputTopup> result = bacDao.selectEntryTopupStableLink(reg_spaj, msl_posisi);
		String lsbs_id = uwManager.selectBusinessId(reg_spaj);
		if(Integer.parseInt(lsbs_id)==188){
			result = bacDao.selectEntryTopupPowerSave(reg_spaj, msl_posisi);
		}
		List mslIdOtorized = selectMslTuKeMstPositionSpajList(reg_spaj);
		if( mslIdOtorized != null && mslIdOtorized.size() > 0 )
		{
			for( int i = 0 ; i < mslIdOtorized.size() ; i ++ )
			{
				Map temp = (Map) mslIdOtorized.get(i);
				Object mslTuKe = temp.get("MSL_TU_KE");
				if( mslTuKe != null && !"".equals( mslTuKe ) )
				{
					for( int j = 0 ; j < result.size() ; j ++ )
					{
						if( LazyConverter.toInt( mslTuKe.toString() ) == result.get(j).getMsl_tu_ke() )
						{
							result.get(j).setStatus_otorisasi("sudah diotorisasi");
						}
					}
				}
			}
			for( int i = 0 ; i < result.size() ; i ++ )
			{
				if( !"sudah diotorisasi".equals( result.get(i).getStatus_otorisasi() ) )
				{
					result.get(i).setStatus_otorisasi("belum diotorisasi");
				}
			}
		}
		else
		{
			for( int i = 0 ; i < result.size() ; i ++ )
			{
				if( !"sudah diotorisasi".equals( result.get(i).getStatus_otorisasi() ) )
				{
					result.get(i).setStatus_otorisasi("belum diotorisasi");
				}
			}
		}
		return result;
	}
	
	public List<InputTopup> selectOtorisasiTopupStableLink(String reg_spaj, int msl_posisi, String[] tempTuKe, String lusId, Integer mslNo){
		return bacDao.selectOtorisasiTopupStableLink(reg_spaj, msl_posisi, tempTuKe, lusId, mslNo);
	}	
	
	public String selectBlanko(String reg_spaj) {
		return bacDao.selectBlanko(reg_spaj);
	}
	
	public Map selectKonfirmasiTransferBac(String reg_spaj) {
		return bacDao.selectKonfirmasiTransferBac(reg_spaj);
	}
	
	public List<PowersaveCair> selectReportCair(Map m) {
		return bacDao.selectReportCair(m);
	}
	
	public List<PowersaveCair> selectDaftarCair(int posisi, int jenis, String cab_bank) {
		return bacDao.selectDaftarCair(posisi, jenis, cab_bank);
	}

	public List selectDaftarSpajOtorisasi( String []cabBank, String kata, String tipe, String pilter ) {
		return bacDao.selectDaftarSpajOtorisasi( cabBank, kata, tipe, pilter );
	}
	
	public List selectDaftarSpajOtorisasiSpajDanaSekuritas( String cabBank, String kata, String tipe, String pilter ) {
		return bacDao.selectDaftarSpajOtorisasiSpajDanaSekuritas( cabBank, kata, tipe, pilter );
	}

	public boolean selectValidasiHapusPowersaveCair(String reg_spaj, int mpc_urut, Date mpc_bdate) {
		return bacDao.selectValidasiHapusPowersaveCair(reg_spaj, mpc_urut, mpc_bdate);
	}
	
	public boolean selectValidasiPowersaveCair(String reg_spaj, int mpc_urut, Date mpc_bdate) {
		return bacDao.selectValidasiPowersaveCair(reg_spaj, mpc_urut, mpc_bdate);
	}
	
	public Integer selectNonCashPowersaveCair(String reg_spaj, int mpc_urut, Date mpc_bdate) {
		return bacDao.selectNonCashPowersaveCair(reg_spaj, mpc_urut, mpc_bdate);
	}
	
	public String selectPosisiPowersaveCair(String reg_spaj, int mpc_urut, Date mpc_bdate) {
		return bacDao.selectPosisiPowersaveCair(reg_spaj, mpc_urut, mpc_bdate);
	}
	
	public PowersaveCair selectRolloverData(String reg_spaj){
		return bacDao.selectRolloverData(reg_spaj);
	}
	
	public List<PowersaveCair> selectRolloverPowersaveSusulan(String reg_spaj){
		return bacDao.selectRolloverPowersaveSusulan(reg_spaj);
	}
	
	public void updateProdDateSlink(String reg_spaj, Date prodDate) {
		bacDao.updateProdDateSlink(reg_spaj,prodDate);
	}
	
	public Integer selectValidasiPinjaman(String reg_spaj){
		String lsbs_id = uwDao.selectBusinessId(reg_spaj);
		if(lsbs_id.equals("188")){
			return bacDao.selectValidasiPinjaman(reg_spaj, 4); //powersave baru (yg bisa ditopup)
		}else if(products.stableLink(lsbs_id)){
			return bacDao.selectValidasiPinjaman(reg_spaj, 2); //slink
		}else if(products.powerSave(lsbs_id)){
			return bacDao.selectValidasiPinjaman(reg_spaj, 1); //psave
		}
		return null;
	}
	
	public List selectPunyaStableLink(String lku_id, Date pp_dob, String pp_name, Date tt_dob, String tt_name) {
		pp_name = "%" + pp_name.replaceAll(" ", "%").toUpperCase().trim() + "%";
		tt_name = "%" + tt_name.replaceAll(" ", "%").toUpperCase().trim() + "%";
		List<Map> tmp = bacDao.selectPunyaStableLink(lku_id, pp_dob, pp_name, tt_dob, tt_name);
		List<Map> result = new ArrayList<Map>(); 
			
		for(Map m : tmp) {
			BigDecimal lssp_id = (BigDecimal) m.get("LSSP_ID");
			if(lssp_id.intValue() == 1 || lssp_id.intValue() == 10) { //kalo masih inforce / being processed, berarti dianggap sudah punya
				String reg_spaj = (String) m.get("REG_SPAJ");
				if(bacDao.selectValidasiPinjaman(reg_spaj, 2) == 0) { //kalo ada di mst_pinjaman, berarti udah cair
					result.add(m);					
				}
			}
		}
		
		return result;
	}
	
	public List selectPunyaStableLinkBAC(String lku_id, Date pp_dob, String pp_name, Date tt_dob, String tt_name, String msag_id, Integer lsbs_id, Integer lsdbs_number) {
		//Special case apabila nama pemegang polis sebelumnya memiliki kata yang sama dengan polis sekarang.Misal:polis sebelumnya : Achmad Dineja, polis Sekarang  Dineja.
		pp_name = pp_name.replaceAll(" ", "%").toUpperCase().trim() + "%";
		//pp_name = "%" + pp_name.replaceAll(" ", "%").toUpperCase().trim() + "%";
		tt_name = "%" + tt_name.replaceAll(" ", "%").toUpperCase().trim() + "%";
		List<Map> tmp = bacDao.selectPunyaStableLinkBAC(lku_id, pp_dob, pp_name, tt_dob, tt_name, msag_id, lsbs_id, lsdbs_number);
		List<Map> result = new ArrayList<Map>(); 
			
		for(Map m : tmp) {
			BigDecimal lssp_id = (BigDecimal) m.get("LSSP_ID");
			if(lssp_id.intValue() == 1 || lssp_id.intValue() == 10) { //kalo masih inforce / being processed, berarti dianggap sudah punya
				String reg_spaj = (String) m.get("REG_SPAJ");
				if(bacDao.selectValidasiPinjaman(reg_spaj, 2) == 0) { //kalo ada di mst_pinjaman, berarti udah cair
					result.add(m);					
				}
			}
		}
		
		return result;
	}
	
	/**Fungsi : untuk membuat pdf password protected
	 * 
	 * @param reg_spaj
	 * @return
	 *
	 * @author Yusup_A
	 * @since Sep 8, 2008 (1:07:04 PM)
	 */
	public String createPass(String reg_spaj) {
		return bacDao.createPass(reg_spaj);
	}
	/*Fungsi: untuk mengetahui product yang diambil suatu polis
	 * param: spaj
	 * @author 	Dian natalia
	 */
	public String selectNmProduct(String reg_spaj) {
		return bacDao.selectNmProduct(reg_spaj);
	}
	/**Fungsi:	Untuk menyimpan history sebelum melakukan print kontrak
	 * @param 	content
	 * @return	void
	 * @author 	Yusup Andri
	 */
	
	public List<Map> selectGetKontrakList(String status, String fileName) { 
		return bacDao.selectGetKontrakList(status, fileName);
	}
	
	public List selectLstAgenAgency(String indexCabangAgency) {
//	public List selectLstAgenAgency(String[] a) {
		return bacDao.selectLstAgenAgency(indexCabangAgency);
	}
	
	public List selectLstAgen(String lca_id) {
		return bacDao.selectLstAgen(lca_id);
	}
	
	public List selectLstAgenBP(String lca_id) {
		return bacDao.selectLstAgenBP(lca_id);
	}
	
	public List selectLstHybridArthaMas(String lshb_id){
		return bacDao.selectLstHybridArthaMas(lshb_id);
	}
	
	public List selectLstHybridAJS(String lshb_id){
		return bacDao.selectLstHybridAJS(lshb_id);
	}
	
	public List<DropDown> selectLstJenisPrintPolisUlang() {
		List<DropDown> lstJenis = new ArrayList();
		lstJenis.add(new DropDown("ALL","ALL"));
		lstJenis.add(new DropDown("PRINT ULANG POLIS","PRINT ULANG POLIS"));
		lstJenis.add(new DropDown("PRINT DUPLIKAT POLIS","PRINT DUPLIKAT POLIS"));
		lstJenis.add(new DropDown("ENDORS NON MATERIAL","ENDORS NON MATERIAL"));
		return lstJenis;
		
	}
	
	public List<DropDown> selectLstLevelAgency() {
		List<DropDown> lstLevel = new ArrayList();
		lstLevel.add(new DropDown("0", ""));
		lstLevel.add(new DropDown("1", "Business Partner *** (Inkubator)"));
		lstLevel.add(new DropDown("2", "Business Partner *** (Kantor Mandiri)"));
		lstLevel.add(new DropDown("3", "Business Partner **"));
		lstLevel.add(new DropDown("4", "Business Partner *"));
		lstLevel.add(new DropDown("5", "Financial Consultant"));
		return lstLevel;
	}
	
	public List<DropDown> selectLstLevelBP() {
		List<DropDown> lstLevel = new ArrayList();
		lstLevel.add(new DropDown("0", ""));
		lstLevel.add(new DropDown("1", "BUSINESS PARTNER RELATIONSHIP MANAGER (BPRM)"));
		lstLevel.add(new DropDown("2", "CUSTOMER RELATIONSHIP OFFICER (CRO)"));
		lstLevel.add(new DropDown("3", "RELATIONSHIP OFFICER (RO)"));
		return lstLevel;
	}
	
	public List<DropDown> selectLstLevelBridge() {
		List<DropDown> lstLevel = new ArrayList();
		lstLevel.add(new DropDown("0", ""));
		lstLevel.add(new DropDown("1", "AGENCY DIRECTOR (Inkubator)"));
		lstLevel.add(new DropDown("2", "AGENCY DIRECTOR (Kantor Mandiri)"));
		lstLevel.add(new DropDown("3", "SALES MANAGER"));
		lstLevel.add(new DropDown("4", "SALES EXECUTIVE"));
		return lstLevel;
	}
	
	public List<DropDown> selectLstLevelHybrid() {
		List<DropDown> lstLevel = new ArrayList();
		lstLevel.add(new DropDown("0", ""));
		lstLevel.add(new DropDown("1", "REGIONAL DIRECTOR"));
		lstLevel.add(new DropDown("2", "REGIONAL MANAGER"));
		lstLevel.add(new DropDown("3", "DISTRICT MANAGER"));
		lstLevel.add(new DropDown("4", "BRANCH MANAGER"));
		lstLevel.add(new DropDown("5", "SALES MANAGER"));
		lstLevel.add(new DropDown("6", "FINANCIAL CONSULTANT"));
		return lstLevel;
	}
	
	public List<DropDown> selectLstLevelRegional() {
		List<DropDown> lstLevel = new ArrayList();
		lstLevel.add(new DropDown("0", ""));
		lstLevel.add(new DropDown("1", "REGIONAL MANAGER"));
		lstLevel.add(new DropDown("2", "SENIOR BRANCH MANAGER"));
		lstLevel.add(new DropDown("3", "BRANCH MANAGER"));
		lstLevel.add(new DropDown("4", "UNIT MANAGER"));
		lstLevel.add(new DropDown("5", "MARKETING EXECUTIVE"));
		return lstLevel;
	}
	
	public String selectNamaLeader(String msag_id){
		return bacDao.selectNamaLeader(msag_id);
	}
	
	public List selectLstAgenRegional(String indexCabang) {
		return bacDao.selectLstAgenRegional(indexCabang);
	}
	
//	public List selectLstAgenRegional(String lca_id1, String lca_id2, String lca_id3, String lca_id4) {
//		return bacDao.selectLstAgenRegional(lca_id1,lca_id2,lca_id3,lca_id4);
//	}
	
	public List selectLstAgencyArthamas(String lwk_flag){
		return bacDao.selectLstAgencyArthamas(lwk_flag);
	}
	

	public String selectGetPDF(String status, String fileName) { 
		return bacDao.selectGetPDF(status, fileName);
	}
	
	public String saveHistory(String msag_id) {
		return bacDao.saveHistory(msag_id);
	}
	
	public List selectFlagBmSbm(String msag_id) {
		return bacDao.selectFlagBmSbm(msag_id);
	}
	
	public List selectKetAgent(String msagID) {
		return bacDao.selectKetAgent(msagID);
	}
	
	public String selectVP(String msag_id) {
		return bacDao.selectVP(msag_id);
	}
	public void insertHistory(Map content) {
		bacDao.insertHistory(content);
	}
	
	public int selectCountProsaveBayar(String spaj) {
		return bacDao.selectCountProsaveBayar(spaj);
	}
	
	public void updateHistory(Map content) {
		bacDao.updateHistory(content);
	}
	
	public String selectSpajFromPolis(String nopol) {
		return bacDao.selectSpajFromPolis(nopol);
	}
	
	public String selectGetSpaj(String nopol) {
		return bacDao.selectGetSpaj(nopol);
	}
	
	public Integer selectValidasiEditUnitLink(String spaj) {
		Integer hasil = bacDao.selectValidasiEditUnitLink(spaj);
		if(hasil == null) hasil = 0;
		return hasil;
	}
	
	public List selectAgentHist(String msag_id) {
		return bacDao.selectAgentHist(msag_id);
	}
	
	public List selectCariAgen(String cari, String tgllahir, String lus_id) {
		return bacDao.selectCariAgen(cari, tgllahir,lus_id);
	}
	
	public Map selectInformasiRegionUntukReferral(String spaj) {
		return bacDao.selectInformasiRegionUntukReferral(spaj);
	}
	
	public int selectUsiaTertanggung(String spaj) {
		return bacDao.selectUsiaTertanggung(spaj);
	}
	
	public List selectDaftarPolisOtorisasiUwSimasPrima(Integer dari, Integer sampai) {
		return bacDao.selectDaftarPolisOtorisasiUwSimasPrima(dari, sampai);
	}
	
	public List selectDaftarPolisOtorisasiBankSinarmas(int lus_id, Integer dari, Integer sampai) {
		return bacDao.selectDaftarPolisOtorisasiBankSinarmas(lus_id, dari, sampai);
	}
	
	public List selectDaftarPolisOtorisasiSekuritas(int lus_id, Integer dari, Integer sampai) {
		return bacDao.selectDaftarPolisOtorisasiSekuritas(lus_id, dari, sampai);
	}

	
	public int selectIsSupervisorOrPincabBankSinarmas(int lus_id) {
		return bacDao.selectIsSupervisorOrPincabBankSinarmas(lus_id);
	}

	public int selectcount_rate(Integer id)
	{
		return bacDao.selectcount_rate(id);
	}

	public Double selectRateRider(String lku, int umurTertanggung, int umurPemegang, int lsbs, int jenis) {
		return bacDao.selectRateRider(lku, umurTertanggung, umurPemegang, lsbs, jenis);
	}
	
	public int selectIsUserYangInputBank(String spaj, Integer lus_id) {
		return bacDao.selectIsUserYangInputBank(spaj, lus_id);
	}
	
	public int selectIsUserYangInputSekuritas(String spaj, Integer lus_id) {
		return bacDao.selectIsUserYangInputSekuritas(spaj, lus_id);
	}
	
	public List selectMslTuKeMstPositionSpajList( String regSpaj ){
		return bacDao.selectMslTuKeMstPositionSpajList( regSpaj );
	}
	
	public int selectIsUserInputBank(Integer lus_id) {
		return bacDao.selectIsUserInputBank(lus_id);
	}

	public Map select_validbank(Integer lus_id)
	{
		return bacDao.select_validbank(lus_id);
	}
	

	public int selectIsInputanBank(String spaj) {
		return bacDao.selectIsInputanBank(spaj);
	}
	
	public Map select_tminus(String lji_id, Date tgl, int minus)
	{
		return bacDao.select_tminus(lji_id, tgl, minus);
	}
	
	public List<HashMap> selectSpajFromABM(Date startDate, Date endDate, String area, String produk) {
		return bacDao.selectSpajFromABM(startDate, endDate, area, produk);
	}
	
	public List<HashMap> selectSpajFromABM2(Date startDate, Date endDate, String area, String produk) {
		return bacDao.selectSpajFromABM2(startDate, endDate, area, produk);
	}
	
	public List<HashMap> selectSpajFromLCB(Date startDate, Date endDate, String lcb, String produk) {
		return bacDao.selectSpajFromLCB(startDate, endDate, lcb, produk);
	}
	
	public List<HashMap> selectABM(){
		return bacDao.selectABM();
	}
	
	public List<HashMap> selectABM2(){
		return bacDao.selectABM2();
	}
	
	public int selectIsUserYangInputDanaSekuritas(String spaj, String cabBank) {
		return bacDao.selectIsUserYangInputDanaSekuritas(spaj, cabBank);
	}
	
	public List<KuesionerPelayananDetails> selectMstKuesionerPelayananByInsertDate(Date begDate, Date endDate) {
		return bacDao.selectMstKuesionerPelayananByInsertDate(begDate, endDate);
	}
	
	public List<HashMap> selectCabangMayapada(){
		return bacDao.selectCabangMayapada();
	}
	
	public List<HashMap> selectCabangUOB(){
		return bacDao.selectCabangUOB();
	}
	
	public List selectHistoryBlanko(int lus_id) {
		return bacDao.selectHistoryBlanko(lus_id);
	}
	
	public String sequenceSpajElektronik(User currentUser, String nama_spaj) {
		return bacDao.sequenceSpajElektronik(currentUser, nama_spaj);
	}
	
	public String sequenceSpajElektronikASM() {
			String sequence = uwDao.selectGetCounter(114, "XX");
			uwDao.updateCounter(sequence, 114, "XX");
			
			return "ASM " + FormatString.rpad("0", sequence, 6);
	}
	public Date selectTanggalJatuhTempoPowersave(String spaj) {
		return bacDao.selectTanggalJatuhTempoPowersave(spaj);
	}
	
	public String blanko_sama(String no_blanko)
	{
		return (String) bacDao.blanko_sama(no_blanko);
	}
	
	public int updateFlagEkspedisiSpaj(String[] daftarSpaj) {
		return bacDao.updateFlagEkspedisiSpaj(daftarSpaj);
	}
	
	/**Fungsi:	Untuk cek No.KTP + Nama/kode plan produk sama + UP sama
	 * @return	Integer 
	 * @author	Hemilda
	 */
	public Integer cek_data_baru_sama( String ktp , String lsbs_id , String lsdbs_number , Double up)
	{
		return (Integer)this.bacDao.cek_data_baru_sama( ktp, lsbs_id, lsdbs_number, up);
	}
	
	/**Fungsi:	Untuk cek No.KTP + Nama/kode plan produk sama + UP sama
	 * @return	List 
	 * @author	Hemilda
	 */
	public List cek_spaj_sama( String ktp , String lsbs_id , String lsdbs_number , Double up)
	{
		return this.bacDao.cek_spaj_sama(ktp, lsbs_id, lsdbs_number, up);
	}

	/**Fungsi:	Untuk cek No.KTP + Nama/kode plan produk sama + UP sama
	 * @return	List 
	 * @author	Hemilda
	 */
	public List<Map<String, Object>> cek_spaj_sama1(String reg_spaj, String ktp , String lsbs_id , String lsdbs_number , Double up)
	{
		return  this.bacDao.cek_spaj_sama1(reg_spaj,ktp, lsbs_id, lsdbs_number, up);
	}	
	
/**Fungsi:	Untuk cek No.KTP + Nama/kode plan produk sama + UP sama
	 * @return	Integer 
	 * @author	Hemilda
	 */
	public Integer cek_data_sama(String reg_spaj , String ktp , String lsbs_id , String lsdbs_number , Double up)
	{
		return (Integer)this.bacDao.cek_data_sama(reg_spaj, ktp, lsbs_id, lsdbs_number, up);
	}
	
	
	public Integer cek_polis_dobel(String nama , String tgl , String lsbs_id , String lsdbs_number , Double premi , String site)
	{
		return (Integer)this.bacDao.cek_polis_dobel(nama, tgl, lsbs_id, lsdbs_number, premi, site);
	}
	
	public Integer cek_polis_dobel_tsi(String nama , String tgl , String lsbs_id , String lsdbs_number , Double up)
	{
		return (Integer)this.bacDao.cek_polis_dobel_tsi(nama, tgl, lsbs_id, lsdbs_number, up);
	}
	
	/**Fungsi:	Untuk list dari produk hcp family 
	 * @return	List 
	 * @author	Hemilda
	 */
	public List<Hcp> select_hcp(String reg_spaj)
	{
		return this.bacDao.select_hcp(reg_spaj);
	}
	
	/**Fungsi:	Untuk detil dari produk hcp family 
	 * @return	Hcp 
	 * @author	Hemilda
	 */
	public Hcp select_hcp_perkode (String spaj, Integer kode , Integer number1 , Integer number2 )
	{
		return (Hcp) this.bacDao.select_hcp_perkode(spaj, kode, number1, number2);
	}
	
	/**Fungsi:	Untuk mengetahui apakah sudah simultan atau belum
	 * @return	Integer
	 * @author	Hemilda
	 */
	public Integer count_simultan(String mcl_id)
	{
		return (Integer) this.bacDao.count_simultan(mcl_id);
	}

	/**Fungsi:	Untuk mengetahui no blanko sudah dipakai untuk produk yang sama atau tidak
	 * @return	Integer
	 * @author	Hemilda
	 */
	public Integer count_no_blanko(String kode,String number, String no_blanko)
	{
		return (Integer) this.bacDao.count_no_blanko(kode, number, no_blanko);
	}	
	
	/**Fungsi:	Untuk mengetahui no blanko sudah dipakai untuk produk yang sama atau tidak
	 * @return	Integer
	 * @author	Hemilda
	 */
	public Integer count_no_blanko_perspaj(String kode,String number, String no_blanko,String spaj)
	{
		return (Integer) this.bacDao.count_no_blanko_perspaj(kode, number, no_blanko, spaj);
	}	
	
	public List<Map> selectValidasiStableLink(String spaj){
		return this.bacDao.selectValidasiStableLink(spaj);
	}

	/**Fungsi:	Untuk mengetahui no blanko sudah dipakai ato blom
	 * @return	Integer
	 * @author	Ferry Harlim
	 */
	public Integer cekNoBlanko(String no_blanko)
	{
		return (Integer) this.bacDao.cekNoBlanko(no_blanko);
	}	

	/**Fungsi:	Untuk mengetahui apakah data client berubah atau tidak
	 * @return	Integer
	 * @author	Hemilda
	 */
	public Integer count_client_simultan(String mcl_id,String mcl_first,String mspe_date_birth)
	{
		return (Integer) this.bacDao.count_client_simultan(mcl_id, mcl_first, mspe_date_birth);
	}

	/**Fungsi:	Untuk Menampilkan data detil dari rider > = 900
	 * @return	Datarider
	 * @author	Hemilda
	 */
	public Datarider selectrider_perkode(String spaj, String kode)
	{
		return (Datarider) this.bacDao.selectrider_perkode(spaj, kode);
	}
	
	/**Fungsi:	Untuk Menampilkan data detil dari top up
	 * @return	DetilTopUp
	 * @author	Hemilda
	 */
	public DetilTopUp select_detil_topup(String reg_spaj)
	{
		return (DetilTopUp) this.bacDao.select_detil_topup(reg_spaj);
	}
	
	/**Fungsi:	Untuk Menampilkan data detil dari peserta dengan reg spaj dan no urut tertentu
	 * @return	model Simas
	 * @author	Hemilda
	 */
	public Simas selectmst_peserta (Simas simas)
	{
		return (Simas) this.bacDao.selectmst_peserta(simas);
	}	
	
	public void savelssaid(String spaj,  Integer status) {
		this.bacDao.savelssaid(spaj, status);
	}
	
	/**Fungsi:	Untuk Menampilkan max no urut yang sudah ada dari mst peserta
	 * @return	integer
	 * @author	Hemilda
	 */
	public Integer selectmax_peserta(String reg_spaj)
	{
		return (Integer)this.bacDao.selectmax_peserta(reg_spaj);
	}	

	/**Fungsi:	Untuk Menampilkan isi list yang sudah ada dari mst peserta
	 * @return	List
	 * @author	Hemilda
	 */
	public List select_semua_mst_peserta(String reg_spaj)
	{
		return this.bacDao.select_semua_mst_peserta(reg_spaj);
	}
	
	public Kesehatan selectkesehatan(String reg_spaj)
	{
		return this.bacDao.selectkesehatan(reg_spaj);
	}
	
	/**Fungsi:	Untuk edit eka.mst_peserta
	 * @author	Hemilda
	 */
	public void update_mst_peserta(Simas simas)
	{
		this.bacDao.update_mst_peserta(simas);
	}

	/**Fungsi:	Untuk delete eka.mst_peserta
	 * @author	Hemilda
	 */
	public void delete_mst_peserta(String reg_spaj)
	{
		this.bacDao.delete_mst_peserta(reg_spaj);
	}
	
	public void delete_mst_peserta_all(String reg_spaj)
	{
		this.bacDao.delete_mst_peserta_all(reg_spaj);
	}		
	
	public Integer count_mst_cancel(String spaj)
	{
		return (Integer) this.bacDao.count_mst_cancel(spaj);
	}
	
	public Map selecteditagenpenutup(String spaj)  {
		return (HashMap) this.bacDao.selecteditagenpenutup(spaj);
	}	
	
	public int selectIsKaryawanEkalife(String spaj) {
		return this.bacDao.selectIsKaryawanEkalife(spaj);
	}
	
	public int selectcountblanko(String blanko) {
		return this.bacDao.selectcountblanko(blanko);
	}	
	
	public int selectcountblanko_spaj(String blanko,String spaj) {
		return this.bacDao.selectcountblanko_spaj(blanko,spaj);
	}	
	
	public Date selectPrintDatePolis(String spaj) {
		return this.bacDao.selectPrintDatePolis(spaj);
	}
	/**Fungsi	Untuk menampilkan data region sesuai dengan spaj
	 * @param 	String query (No Spaj)
	 * @return 	Map
	 * @author 	Ferry Harlim
	 */
	public Map selectRegion(String query) {
		return this.bacDao.selectregion(query);
	}
	
	public Integer selectFlagCC(String spaj) {
		return this.bacDao.selectFlagCC(spaj);
	}
	
	public void savePending(String spaj, String keterangan, Integer status, String lus_id, Integer sub_id) {
		this.bacDao.savePending(spaj, keterangan,status, lus_id, sub_id);
	}
	
	public List select_transulink(String query) throws DataAccessException {
		return this.select_transulink(query);
	}
	
	public List select_detilprodukutama(Integer kode)
	{
		return this.bacDao.select_detilprodukutama(kode);
	}
	
	public List select_detilprodukutama_viewer(Integer kode)
	{
		return this.bacDao.select_detilprodukutama_viewer(kode);
	}
	
	public List<String> select_namaproduk(String kode){
		return this.bacDao.select_namaproduk(kode);
	}
	
	public List selectpremike(String spaj){
		return this.bacDao.selectpremike(spaj);
	}
	
	public List selectUserTtpPremi(String userOut){
		return this.bacDao.selectUserTtpPremi(userOut);
	}
	
	public DetailPembayaran premike(String spaj,Integer ke){
		return this.bacDao.premike(spaj,ke);
	}	
	
	public List tahapanttppremi(String nopolis){
		return this.bacDao.tahapanttppremi(nopolis);
	}	
	
	public Integer select_flag_cc(String spaj)
	{
		return this.bacDao.select_flag_cc(spaj);
	}
	
	public void insertmst_deposit(DetailPembayaran dp,String tahunKe,String premiKe, Integer i_flagCC) {
		this.bacDao.insertmst_deposit(dp);
		//tarik data dari tabel MST_DREK, trus diupdate juga datanya
		//TODO
		if(dp.getNo_trx() != null) {
			if(!dp.getNo_trx().trim().equals("")) {
				Integer flagTunggalGabungan = null;
				if( "Tunggal".equals(dp.getTipe()) ){
					flagTunggalGabungan = 0;
				}else if( "Gabungan".equals(dp.getTipe()) ){
					flagTunggalGabungan = 1;
				}
				
				String noUrut = uwDao.selectMaxNoUrutMstDrekDet( dp.getNo_trx() ) ;
				if(StringUtil.isEmpty(noUrut)){
					noUrut = "0";
				}
				
				uwDao.updateMstDrekListRk(dp.getReg_spaj(), dp.getLus_id(), dp.getNo_trx(), flagTunggalGabungan, dp.getJenis_kredit());
				Date nowDate = selectSysdate1("dd", false, 0);
				List mstDrekBasedNoTrx = uwDao.selectMstDrekBasedNoTrx(dp.getNo_trx());
				Map viewMstDrekDetail=(Map) mstDrekBasedNoTrx.get(0);
				Date tgl_trx = (Date) viewMstDrekDetail.get("TGL_TRX");
				uwDao.insertMstDrekDet(dp.getNo_trx(), tahunKe, premiKe, dp.getJml_bayar(), LazyConverter.toInt(noUrut) + 1, dp.getReg_spaj(), dp.getKe(), null, dp.getLus_id(), nowDate,dp.getNo_pre(), dp.getAcc_no(), dp.getJenis_kredit(), tgl_trx);
				dp.setNo_urut(LazyConverter.toInt(noUrut) + 1);
			}
		}
		//MANTA 18/06/2014
		if(i_flagCC==1) bacManager.updateMstDrekCc(dp.getNo_rek(), dp.getJml_bayar().intValue());
	}
	
	public void insertMstDrekDet(String noTrx, String tahunKe, String premiKe, Double jumlah, Integer noUrut, String regSpaj, Integer transKe, String paymentId, String createId, Date createDate, String no_pre, String norek_ajs, String jenis, Date tgl_trx) throws DataAccessException {
		this.uwDao.insertMstDrekDet(noTrx,tahunKe,premiKe,jumlah, noUrut,regSpaj, transKe, paymentId, createId, createDate, no_pre, norek_ajs, jenis, tgl_trx);
	}

	public void updatemst_deposit(DetailPembayaran dp, String tahunKe, String premiKe) {
		this.bacDao.updatemst_deposit(dp);
		//tarik data dari tabel MST_DREK, trus diupdate juga datanya
		if(dp.getNo_trx() != null) {
			if(!dp.getNo_trx().trim().equals("")) {
				Integer flagTunggalGabungan = null;
				if( "Tunggal".equals( dp.getTipe() ) )
					{ flagTunggalGabungan = 0; }
				else if( "Gabungan".equals( dp.getTipe() ) )
					{ flagTunggalGabungan = 1; }
				String noUrut = uwDao.selectMaxNoUrutMstDrekDet( dp.getNo_trx() ) ;
				Date nowDate = selectSysdate1("dd", false, 0);
				uwDao.updateMstDrek(dp.getReg_spaj(), dp.getLus_id(), dp.getNo_trx(), flagTunggalGabungan, dp.getJenis_kredit());
				uwDao.updateMstDrekDet(tahunKe, premiKe, dp.getJml_bayar(), dp.getNo_trx(), 
						dp.getReg_spaj(), dp.getKe(), null, dp.getLus_id(), nowDate, dp.getJenis_kredit(), dp.getNo_pre() );
			}
		}
	}
	
	public int countmstcontrolpayment(String no_kttp) {
		return this.bacDao.countmstcontrolpayment(no_kttp);
	}
	
	public List ceknokttp(String no_kttp) {
		return this.bacDao.ceknokttp(no_kttp);	
	}	
	
	public List cekflagcc(String spaj) {
		return this.bacDao.cekflagcc(spaj);
	}
	
	public List selectBidangUsaha(int flag) {
		return bacDao.selectBidangUsaha(flag);
	}
	
	public String select_kurs(String spaj)
	{
		return (String) this.bacDao.select_kurs(spaj);
	}
	
	public List selectnilaikurs(String kurs,String tgl_kurs) {
		return this.bacDao.selectnilaikurs(kurs,tgl_kurs);
	}	
			
	public Pemegang selectpp(String spaj) {
		return (Pemegang) this.bacDao.selectpp(spaj);
	}
	
	public ContactPerson selectpic(String mcl_id) {
		return (ContactPerson) this.bacDao.selectpic(mcl_id);
	}
	
	public Tertanggung selectttg(String spaj) {
		return (Tertanggung) this.bacDao.selectttg(spaj);
	}
		
	public AddressBilling selectAddressBilling(String spaj) {
		return (AddressBilling ) this.bacDao.selectAddressBilling(spaj);
	}
	
	public Datausulan selectDataUsulanutama(String spaj) {
		return (Datausulan) this.bacDao.selectDataUsulanutama(spaj);
	}		
	
	public List selectDataUsulan_rider(String spaj)
	{
		return this.bacDao.selectDataUsulan_rider(spaj);
	}
	
	public InvestasiUtama selectinvestasiutama(String spaj) {
		return (InvestasiUtama) this.bacDao.selectinvestasiutama(spaj);
	}	

	/*
	 * null - buka semua
	 * 0 - (tutup)
	 * 1 - (tutup) POWER SAVE
	 * 2 - (01, 02) MEDIVEST
	 * 3 - (03) EXCELLINK PLATINUM
	 * 4 - (01,02,03) EKALINK, EX18, EDUVEST, EX80+ KARY, PLATLINK, CERDAS SEJAHTERA, CERDAS PROTEKSI, CERDAS SISWA 
	 * 5 - (04,05) ($) EKALINK, EX18, EDUVEST, EX80+ KARY, PLATLINK, CERDAS SEJAHTERA, CERDAS PROTEKSI, CERDAS SISWA
	 * 6 - (06,07) FAST EXCELLINK SYARIAH 
	 * 7 - (06,07,08) EKALINK FAM SYARIAH, EX80+ SYARIAH, EX80 SYARIAH
	 * 8 - (09,10) ($) EKALINK FAM SYARIAH, EX80+ SYARIAH, EX80 SYARIAH
	 * 9 - (11,12,13) ARTHALINK, (16,17,18) EKALINK 88
	 * 10 - (14,15) ($) ARTHALINK, (19,20) ($) EKALINK 88
	 * 11 - (22) RP STABLE LINK, (23) $ STABLE LINK
	 * 12 - (21) INVESTIMAX
	 * 13 - (07,08) AMANAH LINK
	*/		
	public Integer selectinvestasiutamakosong(Integer kode_flag) {
		//apabila bukan produk link, return 0
		if(kode_flag != null) if(kode_flag.intValue() == 0 || kode_flag.intValue() == 1) return 0;
		return (Integer) this.bacDao.selectinvestasiutamakosong(kode_flag);
	}		
	public List selectDetailInvestasi(Integer kode_flag){
		//apabila bukan produk link, return null
		if(kode_flag != null) if(kode_flag.intValue() == 0 || kode_flag.intValue() == 1) return null;
		return this.bacDao.selectdetilinvestasikosong(kode_flag);
	}
	
	public List selectdetilinvestasi(String spaj){
		return this.bacDao.selectdetilinvestasi(spaj);
	}
	
	public List selectdetilinvbiaya(String spaj)
	{
		return this.bacDao.selectdetilinvbiaya(spaj);
	}	
	
	public Rekening_client select_rek_client(String spaj){
		Rekening_client r = this.bacDao.select_rek_client(spaj);
		if(r == null) return new Rekening_client(); else return r;
	}	
	
	public Account_recur select_account_recur(String spaj)	{
		Account_recur a = this.bacDao.select_account_recur(spaj);
		if(a == null) return new Account_recur(); else return a;
	}	

	public Powersave select_powersaver(String spaj){
		Powersave result = (Powersave) this.bacDao.select_powersaver(spaj); 
		if(result == null) result = (Powersave) this.bacDao.select_stablesaver(spaj);
		return result;
	}	
	
	public Powersave select_slink (String spaj)
	{
		return (Powersave) this.bacDao.select_slink(spaj);
	}
	
	public Powersave select_slink_topup (String spaj)
	{
		return (Powersave) this.bacDao.select_slink_topup(spaj);
	}
	public List select_benef(String spaj)
	{
		return  this.bacDao.select_benef(spaj);
	}
	
	public List select_produkutama() {
		return this.bacDao.select_produkutama();
	}	
	
	public List select_produkutama_banksinarmas() {
		return this.bacDao.select_produkutama_banksinarmas();
	}
	
	public List select_produkutama_sekuritas() {
		return this.bacDao.select_produkutama_sekuritas();
	}
	
	public List select_produkutama_platinumbii() {
		return this.bacDao.select_produkutama_platinumbii();
	}	
	
	public List select_listregion()
	{
		return this.bacDao.select_listregion();
	}
	
	public Agen select_detilagen(String spaj){
		Agen a = this.bacDao.select_detilagen(spaj);
		if(a == null) return new Agen(); else return a;
	}		

	public Employee select_detilemployee(String spaj){
		Employee e = this.bacDao.select_detilemployee(spaj);
		if(e == null) return new Employee(); else return e;
	}	
	
	public List select_detilproduk(){
		return this.bacDao.select_detilproduk();
	}	
	
	public Date select_bungasimponi(String kurs, Date tgl_begin_date_polis)  {
		return (Date) this.bacDao.select_bungasimponi(kurs, tgl_begin_date_polis);
	}	
	
	public Double select_bunga_simponi(String kurs,Date tgl_max)  {
		return (Double) this.bacDao.select_bunga_simponi(kurs,tgl_max);
	}		
	
	public Map selectjenisbiaya(String kode_rider,String number_rider)  {
		return (HashMap) this.bacDao.selectjenisbiaya(kode_rider,number_rider);
	}	
	
	public Map selectbungaprosave(String kurs,String jenis,Double up, Date tgl_beg_date , Integer flag, int flag_breakable)  {
		return (HashMap) this.bacDao.selectbungaprosave(kurs,jenis,up,tgl_beg_date,flag, flag_breakable);
	}	
	
	public Map select_nik_karyawan(String nik)  {
		return (HashMap) this.bacDao.select_nik_karyawan(nik);
	}
	
	public Map selectagenpenutup(String kodeagen)  {
		return (HashMap) this.bacDao.selectagenpenutup(kodeagen);
	}
	public String selectMsagLeader(String msag_id) {
		return (String) this.bacDao.selectMsagLeader(msag_id);
	}
	
	public List selectTingkatanAgent(String msag_id) {
		return this.bacDao.selectTingkatanAgent(msag_id);
	}
	
	public Map selectagenpenutup_endors(String kodeagen)  {
		return (HashMap) this.bacDao.selectagenpenutup_endors(kodeagen);
	}
	
	public Map selectregional(String kode_regional)  {
		return (HashMap) this.bacDao.selectregional(kode_regional);
	}
	
	public Map selectagenao(String kode_ao)  {
		return (HashMap) this.bacDao.selectagenao(kode_ao);
	}	
	
	public List selectagenao_list()
	{
		return this.bacDao.selectagenao_list();
	}
	
	public List select_tipeproduk()
	{
		return this.bacDao.select_tipeproduk();
	}
	
	public List select_tipeproduk_banksinarmas()
	{
		return this.bacDao.select_tipeproduk_banksinarmas();
	}
	
	public List select_tipeproduk_sekuritas()
	{
		return this.bacDao.select_tipeproduk_sekuritas();
	}
	
	public List select_tipeproduk_platinumbii()
	{
		return this.bacDao.select_tipeproduk_platinumbii();
	}
	
	public String selectUserName(Integer lus_id) throws DataAccessException{
		if(lus_id == null) return null;
		return this.bacDao.selectUserName(lus_id);
	}

	public String selectPwdSpv(Integer lus_id) throws DataAccessException{
		if(lus_id == null) return null;
		return this.bacDao.selectPwdSpv(lus_id);
	}

	public Integer select_kabupaten(String nama_wilayah)
	{
		return this.bacDao.select_kabupaten(nama_wilayah);
	}
	
	public Map select_bank1(String query) {
		return (HashMap) this.bacDao.select_bankrekclient(query);
	}
	
	public Map select_sumberBisnis(String query) {
		return (HashMap) this.bacDao.select_namaBisnis(query);
	}
	
	public Map select_bank2(String query) {
		return (HashMap) this.bacDao.select_bankautodebet(query);
	}	
	
	public Map select_namacompany(String query) {
		return (HashMap) this.bacDao.select_namacompany(query);
	}	

	public Date select_max_deposit_date(){
		return (Date) this.bacDao.select_max_deposit_date();
	}
	
	public Map select_referrer(String query) {
		return (HashMap) this.bacDao.select_referrer(query);
	}
	
	public Map select_referrer_shinta(String query ,Integer kode_flag) {
		return (HashMap) this.bacDao.select_referrer_shinta(query, kode_flag);
	}
	
	public Map select_bii(String query)  {
		return (HashMap) this.bacDao.select_bii(query);
	}	
	
	public Map select_cic1(String query)  {
		return (HashMap) this.bacDao.select_cic1(query);
	}	
	
	public Map select_reff_cic1(String query)  {
		return (HashMap) this.bacDao.select_reff_cic1(query);
	}
	
	public void deletemstreff_bii(String spaj) throws DataAccessException {
		this.bacDao.deletemstreff_bii(spaj);
	}	
	
	public void insertmst_reff_bii(String spaj, String level, String lcb_no , String lrb_id, String reff_id,String lcb_from_lrb) throws DataAccessException {
		this.bacDao.insertmst_reff_bii(spaj,level,lcb_no,lrb_id, reff_id, lcb_from_lrb);
	}
	
	public void insertmst_reff_cic(String spaj, String level, String lcc_no , String lrc_id) throws DataAccessException {
		this.bacDao.insertmst_reff_cic(spaj,level,lcc_no,lrc_id);
	}
	
	public ReffBii selectmst_reff_cic(String spaj)  {
		return (ReffBii) this.bacDao.selectmst_reff_cic(spaj);
	}	
	
	public ReffBii selectmst_reff_bii(String spaj)  {
		return (ReffBii) this.bacDao.selectmst_reff_bii(spaj);
	}	

	public List list_cabang_bii()  {
		return this.bacDao.list_cabang_bii();
	}
	
	public void deletemstreff_cic(String spaj) throws DataAccessException {
		this.bacDao.deletemstreff_cic(spaj);
	}	
	
	public Map select_cabang_cic(String query)  {
		return (HashMap) this.bacDao.select_cabang_cic(query);
	}
	
	public Map select_cabang_bii1(String query)  {
		return (HashMap) this.bacDao.select_cabang_bii1(query);
	}	
	
	public void delete_mst_deposit_premium(String spaj, String ke, DetailPembayaran dp, String tahunKe, String premiKe){ 
		this.bacDao.delete_mst_deposit_premium(spaj, ke);
		Date nowDate = selectSysdate1("dd", false, 0);
//		this.uwDao.updateDeactiveMstDrekDet(tahunKe, premiKe, dp.getNo_trx(), spaj, dp.getKe(), null, dp.getLus_id(), nowDate);
		this.refundDao.deleteMstDrekDet(spaj, dp.getNo_trx(), ke);
		List<DrekDet> mstDrekDetBasedSpaj = uwDao.selectMstDrekDet(dp.getNo_trx(), null, null, null);
		if(mstDrekDetBasedSpaj.size()==0){
			this.uwDao.updateDrekKosongkanSpaj(spaj, dp.getLus_id(), dp.getNo_trx());
		}
	}
	
	public Map select_wilayah1(String query) {
		return (HashMap) this.bacDao.select_wilayah1(query);
	}	

	public List list_reff(Integer number1, Integer number2,String kunci){
		return this.bacDao.list_reff(number1,number2,kunci);
	}		
	
	public List list_reff1(Integer number1, Integer number2){
		return this.bacDao.list_reff1(number1,number2);
	}	
	
	public Integer count_reff(String kunci){
		return (Integer) this.bacDao.count_reff(kunci);
	}	
	
	public Long max_reff_bii(){
		return (Long) this.bacDao.max_reff_bii();
	}		

	public void insert_lst_reff_bii(String nama_reff,String lcb_no,String no_rek,String atas_nama,String flag_aktif,String npk,String lrb_id,String cab_rek){
		this.bacDao.insert_lst_reff_bii(nama_reff,lcb_no,no_rek,atas_nama,flag_aktif,npk,lrb_id,cab_rek);
	}
	
	public void update_lst_reff_bii(String nama_reff,String lcb_no,String no_rek,String atas_nama,String flag_aktif,String npk,String lrb_id,String cab_rek)
	{
		this.bacDao.update_lst_reff_bii(nama_reff,lcb_no,no_rek,atas_nama,flag_aktif,npk,lrb_id,cab_rek);
	}	
	
	public Map select_groupjob(String param)  {
		return (HashMap)this.bacDao.select_groupjob(param);
	}	
	
	public Map select_jabatan(String param)  {
		return (HashMap) this.bacDao.select_jabatan(param);
	}		
	
	public Map select_company_endow(String query)
	{
		return (HashMap)this.bacDao.select_company_endow(query);
	}
		
	public List select_company(String query) {
		return this.bacDao.select_company(query);
	}	

	public List select_namacompany_list_endow(List query) {
		return this.bacDao.select_namacompany_list_endow(query);
	}	
	
	public Integer selectPositionSpaj(String spaj)
	{
		return (Integer) this.bacDao.selectPositionSpaj(spaj);
	}
	
	public Integer selectstsgutri ( String spaj)
	{
		return (Integer) this.bacDao.selectstsgutri(spaj);
	}
	
	public Map selectPositiondok(String spaj)
	{
		return (HashMap) this.bacDao.selectPositiondok(spaj);
	}
	
	public Map select_rek_ekal(Integer lsrek_id) throws DataAccessException {
		return (HashMap) this.bacDao.select_rek_ekal(lsrek_id);
	}	

	public String select_agent_temp(String spaj) {
		return (String) this.bacDao.select_agent_temp(spaj);
	}	
	
	public void insert_mst_non_medical(Kesehatan kesehatan)
	{
		this.bacDao.insert_mst_non_medical(kesehatan);
	}
	
	public int update_mst_non_medical(Kesehatan kesehatan)
	{
		return (Integer) this.bacDao.update_mst_non_medical(kesehatan);
	}
	
	public long select_counter(Integer number , String lca_id)
	{
		return (Long) this.bacDao.select_counter(number, lca_id);
	}	
	
	/*=======================Savingrekruitment===================================*/
	public Kuesioner savingrekruitment(Object cmd,Errors err,String keterangan, User currentUser) throws ServletException,IOException
	{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		if (keterangan.equals("input"))
		{
			return this.savingRekruitment.insertkuesionerbaru(cmd, currentUser);
		}else{
			return this.savingRekruitment.editkuesionerbaru(cmd, currentUser);
		}
	}
	
	/*=======================SavingBAC===================================*/
	
	public Cmdeditbac savingspaj(Object cmd,Errors err,String keterangan, User currentUser) throws ServletException,IOException,Exception
	{
//	 TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		if (keterangan.equals("input"))
		{
			return this.savingBac.insertspajbaru(cmd,currentUser);
		}else{
			return this.savingBac.editspaj(cmd,currentUser);
		}
	}
	
	public Agentrec[] proc_process_agent_2007(String v_strAgentId) {
		try {
			return this.savingBac.proc_process_agent_2007(v_strAgentId,1);
		} catch (Exception e) {
			logger.error("ERROR :", e);
			return null;
		}
	}
	
	public Kesehatan savingkesehatan(Object cmd,Errors err,String keterangan, User currentUser) throws ServletException,IOException
	{
		return this.savingBac.insertkesehatan(cmd, currentUser);
	}
	
	public Cmdeditbac savingagen(Object cmd,Errors err,String keterangan, User currentUser) throws ServletException,IOException
	{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return this.savingBac.editagenspaj(cmd, currentUser);
	}	
	
	public Cmdeditbac savingnik(Object cmd,Errors err,String keterangan, User currentUser) throws ServletException,IOException
	{
		return this.savingBac.editnik(cmd, currentUser);
	}	
		
//	public Cmdeditbac savingpeserta(Object cmd,Errors err,String keterangan, User currentUser) throws ServletException,IOException
//	{
//		return this.savingBac.save_peserta(cmd, keterangan, currentUser);
//	}
//	
//	public Cmdeditbac savinghcp(Object cmd,Errors err,String keterangan, User currentUser) throws ServletException,IOException
//	{
//		return this.savingBac.save_hcp(cmd, keterangan, currentUser);
//	}
	
	/*=======================UploadBAC===================================*/
	
	public void bindSpajForUpload(CommandUploadBac cmd, HttpServletRequest request) throws Exception{
		uploadBac.bindSpajForUpload(cmd, request);
	}
			
	public void uploadSpaj(CommandUploadBac cmd, HttpServletRequest request) throws Exception{
		uploadBac.uploadSpaj(cmd, request);
	}
			
	public List<DropDown> uploadEmailAgen(CommandUploadBac cmd, HttpServletRequest request) throws Exception{
		return uploadBac.uploadEmailAgen(cmd, request);
	}
	
	/* ======================= CommonDao ======================= */
	
	public Map selectInfoDetailUser(int lus_id){
		return commonDao.selectInfoDetailUser(lus_id);
	}
	
	public int selectJumlahSpajAksesHist(String lus_id) {
		return commonDao.selectJumlahSpajAksesHist(lus_id);
	}
	public Map selectAksesHist(String lus_id) {
		return commonDao.selectAksesHist(lus_id);
	}
	public List<Map> selectClosingDate() {
		return commonDao.selectClosingDate();
	}
	public Map<String, Object> selectUnderTablePeriode() {
		return commonDao.selectUnderTablePeriode();
	}
	
	public String selectEncryptDecrypt(String kata, String jenis) {
		return commonDao.selectEncryptDecrypt(kata, jenis);
	}
	
	public List selectAddressRegion() {
		return commonDao.selectAddressRegion();
	}
	
	public List selectMstHistoryUpload(){
		return commonDao.selectMstHistoryUpload();
	}
	
	public List selectListMstHistoryUpload(String jenis, String level, Date tglawal, Date tglakhir, String upload_jenis){
		return commonDao.selectListMstHistoryUpload(jenis, level, tglawal, tglakhir, upload_jenis);
	}
	
	public int selectCountMstHistoryUpload(String temp_filename){
		return commonDao.selectCountMstHistoryUpload(temp_filename);
	}
	
	public String selectPathMstHistoryUpload(String upload_id){
		return commonDao.selectPathMstHistoryUpload(upload_id);
	}
	
	public String selectFilenameMstHistoryUpload(String upload_id){
		return commonDao.selectFilenameMstHistoryUpload(upload_id);
	}
	
	/**Fungsi:	Untuk menampilkan semua data pada tabel EKA.LST_ADDR_REGION
	 * @param 	String larId (dimana larId ini dapat > 1, dengan menggunakan pembatas ';')
	 * 			Contoh:"123,12,123"
	 * @return	List
	 * @throws 	DataAccessException
	 * @author	Ferry Harlim
	 */
	public List selectAddressRegionLarId(String larId) {
		return commonDao.selectAddressRegionLarId(larId);
	}
	
	public List selectUserAdmin(int lus_id) {
		return commonDao.selectUserAdmin(lus_id);
	}
	
	public TandaTangan selectMstTandatangan(String mcl_id, String nama) {
		return this.commonDao.selectMstTandatangan(mcl_id, nama);
	}
	
	public List selectMstTandatanganSPAJ(String spaj) {
		return this.commonDao.selectMstTandatanganSPAJ(spaj);
	}
	
	public TandaTangan selectTandatangan(String mstt_id) {
		return this.commonDao.selectTandatangan(mstt_id);
	}
	
	public TandaTangan selectTandatanganMCLID(String mcl_id) {
		return this.commonDao.selectTandatanganMCLID(mcl_id);
	}
	
	public List selectDaftarMstTandatangan(int lus_id){
		return this.commonDao.selectDaftarMstTandatangan(lus_id);
	}
	
	public List selectListMCL_ID(String spaj){
		return this.commonDao.selectListMCL_ID(spaj);
	}
	
	public void saveTandaTangan(String nama, String spaj, FileItem item, User currentUser, String imageFormat) {
		
		List cariMCL=this.commonDao.selectListMCL_ID(spaj);
		String mcl_id="";
		int lus_id= Integer.parseInt(currentUser.getLus_id());
		nama=nama.trim();
		spaj=spaj.replace(".", "").trim();
		if(!cariMCL.isEmpty()){
			for(int i=0; i<cariMCL.size();i++){
				Map mcl_idMap= (Map) cariMCL.get(i);
				String mcl_idPP= (String) mcl_idMap.get("MSPO_POLICY_HOLDER");
				String mcl_idTT= (String) mcl_idMap.get("MSTE_INSURED");
				String namapp= (String) mcl_idMap.get("MSCH_NAMA_PP");
				String namatt= (String) mcl_idMap.get("MSCH_NAMA_TT");
				if(nama.equals(namapp) && nama.equals(namatt)){
					mcl_id=mcl_idPP;
				}else if(nama.equals(namapp)){
					mcl_id=mcl_idPP;
				}else if(nama.equals(namatt)){
					mcl_id=mcl_idTT;
				}else{
					spaj="";
				}
			}
		}else{
			spaj="";
		}
		
		//cari apakah 
		TandaTangan ttd = this.commonDao.selectMstTandatangan(mcl_id,nama);
		if(ttd != null) {
			//update
			ttd.setMstt_image(item.get());
			ttd.setModify_id(Integer.valueOf(currentUser.getLus_id()));
			ttd.setModify_dt(this.selectSysdate());
			this.commonDao.updateMstTandatangan(ttd);
		}else {
			//insert
			ttd = new TandaTangan();
			ttd.setReg_spaj(spaj);
			ttd.setMstt_image(item.get());

			ttd.setMstt_nama(nama);
			ttd.setMcl_id(mcl_id);
			ttd.setCreate_id(lus_id);
			ttd.setMstt_ext(imageFormat);
			ttd.setCreate_id(Integer.valueOf(currentUser.getLus_id()));
			this.commonDao.insertMstTandatangan(ttd);					
		}
		
	}
	
	public List selectAllUsers(String lca_id, String lde_id) {
		return commonDao.selectAllUsers(lca_id, lde_id);
	}
	
	public List selectAllBranchesAndDepartments(String lca_id, String lde_id) {
		return commonDao.selectAllBranchesAndDepartments(lca_id, lde_id);
	}
	
	public List selectTreeMenu(int jenis, int lus_id) {
		return commonDao.selectTreeMenu(jenis, lus_id);
	}
	
	public List selectAllMenuMaintenance(Integer jenis){
		return commonDao.selectAllMenuMaintenance(jenis);
	}
	
	public List selectContactPerson(String mcl_id) {
		return commonDao.selectContactPerson(mcl_id); 
	}
	
	public List selectJenisUsaha() {
		return commonDao.selectJenisUsaha();
	}
	
	public List selectStatusPersonal(String mcl_id) {
		return commonDao.selectStatusPersonal(mcl_id);
	}
	
	public Personal selectProfilePerusahaan(String mcl_id) {
		Personal result = commonDao.selectProfilePerusahaan(mcl_id); 
		return result==null?(new Personal()):result;
	}
	
	public List selectReimenderInvoicePerusahaan() {
		return commonDao.selectReimenderInvoicePerusahaan(); 
	}
	
	public List selectProfile(String tipe, String jenis, String kata) {
		return commonDao.selectProfile(tipe, jenis, kata);
	}
	
	public Integer validationCurrency(Currency kurs) {
		return commonDao.validationCurrency(kurs);
	}
	
	public void saveCurrency(List daftarKurs) {
		for(int i=0; i<daftarKurs.size(); i++) {
			Currency kurs = (Currency) daftarKurs.get(i);
			kurs.setLkh_kurs_beli(new Double(FormatNumber.round(0.99 * kurs.getLkh_currency(),0)).intValue());
			kurs.setLkh_kurs_jual(new Double(FormatNumber.round(1.01 * kurs.getLkh_currency(),0)).intValue());
			if(kurs.getFlag_insert().equals("1")) {
				commonDao.insertCurrency(kurs);
			}else if(kurs.getFlag_insert().equals("0")) {
				commonDao.updateCurrency(kurs);
			}
		}
	}
	
	public List selectCurrency(Command cmd) {
		return commonDao.selectCurrency(cmd);
	}
	
	/*public void insertCurrency(Currency kurs) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		commonDao.insertCurrency(kurs);
	}*/
	
	public List selectAllMenu(int jenis) {
		return commonDao.selectAllMenu(jenis);
	}
	
	public void deleteAllAksesCabang(String lus) {
		this.commonDao.deleteAllAksesCabang(lus);
	}
	
	public int updateMstTandatangan(TandaTangan ttd) {
		return this.commonDao.updateMstTandatangan(ttd);
	}
	
	public void deleteMenuAkses(Map params) {
		this.commonDao.deleteMenuAkses(params);
	}

	public void resetCommonIbatisCache() {
		this.commonDao.resetIbatisCache();
	}

	public void saveAksesCabang(String lus, String nilai[]) {
		this.commonDao.insertAksesCabang(lus, nilai);
	}

	public void saveAllAksesCabang(String lus) {
		this.commonDao.insertAllAksesCabang(lus);
	}

	public List selectAksesCabang(int lus_id) {
		return this.commonDao.selectAksesCabang(lus_id);
	}
	
	public List selectAksesCabang_lar(int lus_id) {
		return this.commonDao.selectAksesCabang_lar(lus_id);
	}

	public List selectAllUserMenu() {
		return this.commonDao.selectAllUserMenu(new Integer(0));
	}

	public List selectAllUserMenuWithAccessRights(int lus_id) {
		return this.commonDao.selectAllUserMenuWithAccessRights(new Integer(
				lus_id));
	}

	public List selectEmployeesInDepartment(String lde) {
		return this.commonDao.selectEmployeesInDepartment(lde);
	}

	public User selectLoginAuthentication(User user) {
		return this.commonDao.selectLoginAuthentication(user);
	}
	
	public User selectLoginAuthenticationByLusId(User user){
		return this.commonDao.selectLoginAuthenticationByLusId(user);
	}

	public User selectUser(String lus_id) {
		return this.commonDao.selectUser(lus_id);
	}
	
	public List selectMenu(String inOrNotIn) {
		return this.commonDao.selectMenu(inOrNotIn);
	}

	public String selectPasswordAuthentication(String id) {
		return this.commonDao.selectPasswordAuthentication(id);
	}

	public Document selectXmlAllCabangNotRegistered(Integer lus_id) {
		return this.commonDao.selectXmlAllCabangNotRegistered(lus_id);
	}

	public Document selectXmlAllCabangRegistered(Integer lus_id) {
		return this.commonDao.selectXmlAllCabangRegistered(lus_id);
	}

	public Document selectXmlAllUserMenu(Integer lus_id,Integer jenis, String aplikasi) {
		return this.commonDao.selectXmlAllUserMenu(lus_id, jenis, aplikasi);
	}

	public void updateLoginPassword(String id, String pass) {
		this.commonDao.updateLoginPassword(id, pass);
	}

	public void updateMenuAkses(String nilai[], String lus, String jenisAplikasi ) {
		this.commonDao.updateMenuAkses(nilai, lus, jenisAplikasi);
	}
	
	//untuk list sk
	public List<Surat> selectDaftarSk(String no_surat, String judul, String jenisSurat, String thUpdate, String lstBln, int page, int pageSize, String filter, String filterEx, String nik){
    	return commonDao.selectDaftarSk(no_surat, judul, jenisSurat, thUpdate, lstBln, page, pageSize, filter, filterEx, nik);
    }
	//untuk list sk
	public void insertDaftarSk(String no_surat, String ynCheckRegional, String ynCheckAkm){
    	commonDao.insertDaftarSk(no_surat, ynCheckRegional, ynCheckAkm);
    }
	//untuk list sk
	public int updateDaftarSk(String no_surat, String ynCheckRegional, String ynCheckAkm){
    	return commonDao.updateDaftarSk(no_surat, ynCheckRegional, ynCheckAkm);
    }
	//untuk list sk agency
	public int updateDaftarSkAgency(String no_surat, String ynCheckAgency){
	    return commonDao.updateDaftarSkAgency(no_surat, ynCheckAgency);
	}
	//untuk list sk
	public void deleteDaftarSk(String no_surat){
    	commonDao.deleteDaftarSk(no_surat);
    }
	//untuk list sk
	public String selectLastPageDaftarSk(String no_surat, String judul, String jenisSurat, String thUpdate, String lstBln, int page, int pageSize, String filter, String nik){
    	return commonDao.selectLastPageDaftarSk(no_surat, judul, jenisSurat, thUpdate, lstBln, page, pageSize, filter, nik);
    }
	
	public List<Nasabah> selectMstNasabah(){
		return commonDao.selectMstNasabah();
	}
	/**@Fungsi:	Untuk Menampilkan data nasabah pada tabel
	 * 			EKA.MST_NASABAH sesuai dengan posisi yang di kirim
	 * @param 	Integer lspdId
	 * @return 	List
	 * @author 	Ferry Harlim
	 */
	public List selectAllMstNasabah(Integer lspdId){
		return commonDao.selectAllMstNasabah(lspdId);
	}
	/**@Fungsi:	Untuk Menampilkan data nasabah pada tabel
	 * 			EKA.MST_NASABAH sesuai dengan kode nasabah
	 * @param 	String kdNasabah
	 * @return 	Nasabah
	 * @author 	Ferry Harlim
	 */
	public Nasabah selectMstNasabah(String kdNasabah){
		return commonDao.selectMstNasabah(kdNasabah);
	}
	
	public SurplusCalc selectMstSurplusCalc(String mns_kd_nasabah){
		return commonDao.selectMstSurplusCalc(mns_kd_nasabah);
	}
	
	public ProtectCalc selectMstProtectCalc(String mns_kd_nasabah){
		return commonDao.selectMstProtectCalc(mns_kd_nasabah);
	}
	
	public IncomeCalc selectMstIncomeCalc(String mns_kd_nasabah){
		return commonDao.selectMstIncomeCalc(mns_kd_nasabah);
	}
	
	/**@Fungsi:	Untuk Menampilkan data nasabah pada tabel
	 * 			EKA.MST_NASABAH sesuai dengan kode nasabah
	 * @param 	String nomor (KdNasabah atau No Referral)
	 * @return 	List
	 * @author 	Ferry Harlim
	 */
	public List selectMstNasabahByCode(String nomor,Integer tipe, Integer lspd_id){
		return commonDao.selectMstNasabahByCode(nomor, tipe, lspd_id);
	}
	
	public List selectMstPolicyByCode(String nomor, Integer tipe){
		return uwDao.selectMstPolicyByCode(nomor, tipe);
	}
	
	public List selectMstNasabahByCodeAll(String nomor,Integer tipe){
		return commonDao.selectMstNasabahByCodeAll(nomor, tipe);
	}
	/**@Fungsi:	Untuk Menampilkan data cabang bii pada tabel
	 * 			EKA.LST_CAB_BII sesuai dengan kode region
	 * @param 	String kdRegion
	 * @return 	List
	 * @author 	Ferry Harlim
	 */
	public List selectLstCabBii(String kdRegion){
		return commonDao.selectLstCabBii(kdRegion);
	}
	
	/**@Fungsi:	Untuk Menampilkan semua data cabang bii pada tabel
	 * 			EKA.LST_CAB_BII 
	 * @param 	
	 * @return 	List
	 * @author 	Ferry Harlim
	 */
	public List selectLstCabBiiAll(){
		return commonDao.selectLstCabBiiAll();
	}
	
	/**@Fungsi:	Untuk Menampilkan cabang BII dengan  format
	 * 			nama koordinir/nama area/nama cabang	
	 * @param 	String kdRegion
	 * @return 	String
	 * @author 	Ferry Harlim
	 */
	public String selectCabangBii(String kdRegion){
		return commonDao.selectCabangBii(kdRegion);
	}
	/**@Fungsi:	Untuk Menampilkan data pada tabel
	 * 			EKA.MST_BFA
	 * @param	String msagId,String kode
	 * @return 	List
	 * @author 	Ferry Harlim
	 */
	public List selectMstBfa(String msagId,String kode){
		return commonDao.selectMstBfa(msagId,kode);
	}
	/**@Fungsi:	Untuk Menampilkan data nama BFA dengan format
	 * 			nama koordinir/nama area/nama cabang
	 * @param 	String msagId,String kode
	 * @return 	String
	 * @author 	Ferry Harlim
	 */
	public String selectNamaBfa(String msagId,String kode){
		String lsNama="",lsNama1="",lsNama2="",lsNamaAll,lsLeader="",lsLeader1="";
		List list;
		if(msagId==null)
			list=commonDao.selectMstBfaKode(kode);
		else
			list=commonDao.selectMstBfa(msagId, kode);
		if(list.isEmpty()==false){
		Bfa bfa=(Bfa)list.get(0);
		if(bfa!=null){
			lsNama=bfa.getNama_bfa();
			lsLeader=bfa.getLeader();
		}
		List list1=commonDao.selectMstBfa(lsLeader,kode);
		if(list1.isEmpty()==false){
			Bfa bfa1=(Bfa)list1.get(0);
			if(bfa1!=null){
				lsNama1=bfa1.getNama_bfa();
				lsLeader1=bfa1.getLeader();
			}else{
				lsLeader1="";
				lsNama1="";
				bfa1=new Bfa();
			}
		}	
		
		if( (lsLeader1!=null ) ){
			List list2=commonDao.selectMstBfa(lsLeader1,kode);
			if(list2.isEmpty()==false){
				Bfa bfa2=(Bfa)list2.get(0);
				if(bfa2!=null)
					lsNama2=bfa2.getNama_bfa();
				else
					bfa2=new Bfa();
			}
			lsNamaAll=lsNama2 + " / " + lsNama1 + " / " + lsNama;
		}else
			lsNamaAll=lsNama1 + " / " + lsNama1 + " / " + lsNama; 
		
		return lsNamaAll;
		}else
			return "/ /";
	}
	/**@Fungsi:	Untuk Menampilkan data pada tabel
	 * 			EKA.MST_BFA sesuai dengan level_id
	 * @param	Integer level
	 * @return	List
	 * @author	Ferry Harlim 
	 */
	public List selectMstBfaLevel(Integer level){
		return commonDao.selectMstBfaLevel(level);
	}
	/**Fungsi:	Untuk menampilkan data pada tabel
	 * 			EKA.LST_JN_LEAD
	 * @param	String levelId 
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectLstJnLead(String levelId){
		return commonDao.selectLstJnLead(levelId);
	}
	/**Fungsi:	Untuk menampilkan data pada tabel
	 * 			EKA.LST_Cab_Bii
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectAllLstCabBii(){
		return commonDao.selectAllLstCabBii();
	}
	/**Fungsi:	Untuk Menampilkan data pada tabel 
	 * 			EKA.MST_BFA sesuai dengan kode.
	 * @param	String kode
	 * @return	com.ekalife.elions.model.Bfa
	 * @author 	Ferry Harlim
	 */
	public List selectMstBfaKode(String kode){
		return commonDao.selectMstBfaKode(kode);
	}
	/**Fungsi:	Untuk Menampilkan data pada tabel
	 * 			EKA.MST.BFA sesuai dengan kondisi level_id,status_aktif
	 * 			kd_region,kd_koord
	 * @param	Bfa bfa
	 * @return 	String
	 * @author 	Ferry Harlim
	 */
	public String selectMstBfaMsagId(Bfa bfa){
		return commonDao.selectMstBfaMsagId(bfa);
	}
	/**Fungsi:	Untuk Menampilkan data pada tabel 
	 * 			EKA.LST_REFERRER_BII dengan flag_aktif=1
	 * @param	Integer levelId
	 * @return	DropDown
	 * @author 	Ferry Harlim
	 */
	public List selectLstReferrerBii(Integer levelId){
		return commonDao.selectLstReferrerBii(levelId);
	}
	/**Fungsi:	Untuk Menampilkan Data Nama BFA beserta cabangnya
	 * @param	String msagId
	 * @return	java.util.HashMap
	 * @author 	Ferry Harlim
	 */
	public Map selectBfaNCabang(String msagId){
		return commonDao.selectBfaNCabang(msagId);
	}
	/**Fungsi:	Untuk Menampilkan Data referrer BII beserta cabangnya
	 * @param	String refBii
	 * @return 	java.util.HashMap
	 * @author 	Ferry Harlim
	 */
	public Map selectLstReffBiiNCabang(String refBii){
		return commonDao.selectLstReffBiiNCabang(refBii);
	}
	/**Fungsi:	Untuk Menampilkan nilai max MNS_NO_REF pada tabel EKA.MST_NASABAH
	 * 			no referral platinum (kdRegion,kdKoord,kdArea)=(02,07,01) 
	 * @param	String kdRegion
	 * 			String kdKoord
	 * 			String kdArea
	 * 			String jnNasabah
	 * @return 	String
	 * @author	Ferry Harlim
	 */
	public String selectMaxMstNasabahMnsNoReff(String kdRegion,String kdKoord,String kdArea,String jnNasabah){
		return (String)commonDao.selectMaxMstNasabahMnsNoReff(kdRegion, kdKoord, kdArea,jnNasabah);
	}
	/**Fungsi:	Untuk Menampilkan jumlah record pada tabel EKA.MST_NASABAH
	 * 			berdasarkan mns_no_ref
	 * @param	String mnsNoRef
	 * @return	Integer
	 * @author 	Ferry Harlim
	 */
	public Integer selectCountMstNasabahMnsNoRef(String mnsNoRef){
		return commonDao.selectCountMstNasabahMnsNoRef(mnsNoRef);
	}
	/**Fungsi:	Untuk Menampilkan level id dari tabel EKA.LST_JN_LEAD
	 * 			sesuai dengan ljlJenis
	 * @param	Integer ljlJenis
	 * @return	Integer
	 * @author 	Ferry Harlim
	 */
	public Integer selectLstJnLeadLevelId(Integer ljlJenis){
		return commonDao.selectLstJnLeadLevelId(ljlJenis);
	}
	

	public void updateMstNasabah(Nasabah nasabah){
		commonDao.updateMstnasabah(nasabah);
	}
	
	public String prosesInputBlacklist(CmdInputBlacklist cmd, User user)throws Exception{
		String lsClientNew=uwDao.wf_get_client_id(/*cabang*/"01");
		Integer blacklistIdnew=uwDao.wf_get_blacklist_id();
		Integer blacklistFamilyIdnew=uwDao.wf_get_blacklistFamily_id();
		//insert client_new
		cmd.getClient().setMcl_id_new(lsClientNew);
		cmd.getClient().setMcl_first(cmd.getClient().getMcl_first().toUpperCase());
		cmd.getClient().setMspe_no_identity(cmd.getClient().getMspe_no_identity().toUpperCase());
		cmd.getClient().setLus_id(Integer.parseInt(user.getLus_id()));
		cmd.getClient().setMcl_blacklist(1);
		uwDao.insertMstClientNew(cmd.getClient());
		//insert address
		cmd.getAddressNew().setMcl_id_new(lsClientNew);
		uwDao.insertMstAddressNew(cmd.getAddressNew());
		//insert blacklist
		cmd.getBlacklist().setLbl_alamat(cmd.getAddressNew().getAlamat_rumah());
		//cmd.getBlacklist().setLbl_asuransi(lbl_asuransi);
		cmd.getBlacklist().setLbl_nama(cmd.getClient().getMcl_first());
		cmd.getBlacklist().setLbl_no_identity(cmd.getClient().getMspe_no_identity());
		cmd.getBlacklist().setLbl_tempat(cmd.getClient().getMspe_place_birth());
		cmd.getBlacklist().setLbl_tgl_lahir(cmd.getClient().getMspe_date_birth());
		cmd.getBlacklist().setLus_login_name(user.getLus_full_name());
		cmd.getBlacklist().setLbl_id(blacklistIdnew);
		cmd.getBlacklist().setMcl_id(lsClientNew);
		cmd.getBlacklist().setLbl_tgl_input(commonDao.selectSysdate());
		cmd.getBlacklist().setLus_id(Integer.parseInt(user.getLus_id()));
		uwDao.insertLstBlacklist(cmd.getBlacklist());
		//insert det blacklist family
		for(int i = 0 ; i < cmd.getDetBlackListAll().size() ; i++){
			int ldblNumber = 1;
			if(cmd.getDetBlackListAll().get(i).getLdbl_tgl_kejadian() != null || cmd.getDetBlackListAll().get(i).getLdbl_tgl_kejadian_to() != null || !"".equals(cmd.getDetBlackListAll().get(i).getLdbl_diagnosa())){
				cmd.getDetBlackListAll().get(i).setLbl_id(blacklistIdnew);
				cmd.getDetBlackListAll().get(i).setLdbl_number(ldblNumber++);
				cmd.getDetBlackListAll().get(i).setLus_id(Integer.parseInt(user.getLus_id()));
				uwDao.insertLstDetBlacklist(cmd.getDetBlackListAll().get(i));
			}
		}
		//insert blacklist family
		cmd.getBlacklistfamily().setLbl_id(blacklistIdnew);
		cmd.getBlacklistfamily().setLblf_id(blacklistFamilyIdnew);
		cmd.getBlacklistfamily().setLblf_tgl_input(commonDao.selectSysdate());
		cmd.getBlacklistfamily().setLus_id(Integer.parseInt(user.getLus_id()));
		
		if(cmd.getBlacklistfamily().getLblf_nama_si() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_si() != null){
			uwDao.insertLstBlacklistFamily(new BlackListFamily(blacklistFamilyIdnew,blacklistIdnew,5,cmd.getBlacklistfamily().getLblf_nama_si(),cmd.getBlacklistfamily().getLblf_tgllhr_si(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
			blacklistFamilyIdnew++;
		}
		if(cmd.getBlacklistfamily().getLblf_nama_anak1() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_anak1() != null){
			uwDao.insertLstBlacklistFamily(new BlackListFamily(blacklistFamilyIdnew,blacklistIdnew,4,cmd.getBlacklistfamily().getLblf_nama_anak1(),cmd.getBlacklistfamily().getLblf_tgllhr_anak1(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
			blacklistFamilyIdnew++;
		}
		if(cmd.getBlacklistfamily().getLblf_nama_anak2() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_anak2() != null){
			uwDao.insertLstBlacklistFamily(new BlackListFamily(blacklistFamilyIdnew,blacklistIdnew,4,cmd.getBlacklistfamily().getLblf_nama_anak2(),cmd.getBlacklistfamily().getLblf_tgllhr_anak2(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
			blacklistFamilyIdnew++;
		}
		if(cmd.getBlacklistfamily().getLblf_nama_anak3() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_anak3() != null){
			uwDao.insertLstBlacklistFamily(new BlackListFamily(blacklistFamilyIdnew,blacklistIdnew,4,cmd.getBlacklistfamily().getLblf_nama_anak3(),cmd.getBlacklistfamily().getLblf_tgllhr_anak3(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
			blacklistFamilyIdnew++;
		}
		
		//===========
		
		return lsClientNew;
	}
	
	public void prosesUpdateBlacklist(CmdInputBlacklist cmd, User user)throws Exception{
		//String lsClientNew=uwDao.wf_get_client_id(/*cabang*/"01");
		Integer blacklistIdnew=uwDao.wf_get_blacklist_id();
		Integer blacklistFamilyIdnew=uwDao.wf_get_blacklistFamily_id();
		int bl = 0;
//		update client_new
		//cmd.getClient().setMcl_id_new(lsClientNew);
		cmd.getClient().setLus_id(Integer.parseInt(user.getLus_id()));
		cmd.getClient().setMcl_blacklist(1);
		if(!"".equals(cmd.getBlacklist().getReg_spaj())){
			uwDao.updateClientToBlacklist(cmd.getClient());
		}else{
			uwDao.updateMstClientNew(cmd.getClient());
		}
		//update address
		uwDao.updateMstAddressNew(cmd.getAddressNew());
		//update blacklist
		//cmd.getBlacklist().setLbl_id(blacklistIdnew);
		//cmd.getBlacklist().setMcl_id(lsClientNew);
		cmd.getBlacklist().setLbl_alamat(cmd.getAddressNew().getAlamat_rumah());
		//cmd.getBlacklist().setLbl_asuransi(lbl_asuransi);
		cmd.getBlacklist().setLbl_nama(cmd.getClient().getMcl_first());
		cmd.getBlacklist().setLbl_no_identity(cmd.getClient().getMspe_no_identity());
		cmd.getBlacklist().setLbl_tempat(cmd.getClient().getMspe_place_birth());
		cmd.getBlacklist().setLbl_tgl_lahir(cmd.getClient().getMspe_date_birth());
		cmd.getBlacklist().setLus_login_name(user.getLus_full_name());
		cmd.getBlacklist().setLbl_tgl_input(commonDao.selectSysdate());
		cmd.getBlacklist().setLus_id(Integer.parseInt(user.getLus_id()));
		cmd.getBlacklist().setMcl_id(cmd.getClient().getMcl_id());
		if(uwDao.selectAllBlacklist(cmd.getClient().getMcl_first(), cmd.getClient().getMspe_date_birth(), cmd.getClient().getMspe_no_identity()) == null){
			cmd.getBlacklist().setLbl_id(blacklistIdnew);
			uwDao.insertLstBlacklist(cmd.getBlacklist());
			bl = 1;
		}else{
			uwDao.updateLstBlacklist(cmd.getBlacklist());
			bl = 2;
		}
		
		//delete then insert det blacklist
		if(bl == 1 || bl == 2){
			if(bl == 2){
				uwDao.deleteLstDetBlacklist(cmd.getBlacklist().getLbl_id().toString());
			}
			int ldblNumber = 1;
			for(int i = 0 ; i < cmd.getDetBlackListAll().size() ; i++){
				if(cmd.getDetBlackListAll().get(i).getLdbl_tgl_kejadian() != null || cmd.getDetBlackListAll().get(i).getLdbl_tgl_kejadian_to() != null || !"".equals(cmd.getDetBlackListAll().get(i).getLdbl_diagnosa())){
					if(bl == 1){
						cmd.getDetBlackListAll().get(i).setLbl_id(blacklistIdnew);
					}else if(bl == 2){
						cmd.getDetBlackListAll().get(i).setLbl_id(cmd.getBlacklist().getLbl_id());
					}
					cmd.getDetBlackListAll().get(i).setLdbl_number(ldblNumber++);
					cmd.getDetBlackListAll().get(i).setLus_id(Integer.parseInt(user.getLus_id()));
					uwDao.insertLstDetBlacklist(cmd.getDetBlackListAll().get(i));
				}
			}
		}
		
		//update blacklist family
//		cmd.getBlacklistfamily().setLbl_id(blacklistIdnew);
//		cmd.getBlacklistfamily().setLblf_id(blacklistFamilyIdnew);
		//cmd.getBlacklistfamily().setLblf_tgl_input(commonDao.selectSysdate());
		cmd.getBlacklistfamily().setLbl_id(cmd.getBlacklist().getLbl_id());
		cmd.getBlacklistfamily().setLus_id(Integer.parseInt(user.getLus_id()));
		List<BlackListFamily> blf = uwDao.selectAllBlacklistFamily(cmd.getBlacklist().getLbl_id());
		
		//insert
		int p = 0;
		int c = 0;
		for(int i = 0 ; i < blf.size() ; i++){
			if(blf.get(i).getLsre_id() == 5){
				p = 1;
			}else if(blf.get(i).getLsre_id() == 4){
				c++;
			}
		}
		if(cmd.getBlacklistfamily().getLblf_nama_si() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_si() != null && p == 0){
			uwDao.insertLstBlacklistFamily(new BlackListFamily(blacklistFamilyIdnew,cmd.getBlacklist().getLbl_id(),5,cmd.getBlacklistfamily().getLblf_nama_si(),cmd.getBlacklistfamily().getLblf_tgllhr_si(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
			blacklistFamilyIdnew++;
		}
		if(cmd.getBlacklistfamily().getLblf_nama_anak1() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_anak1() != null && c == 0){
			uwDao.insertLstBlacklistFamily(new BlackListFamily(blacklistFamilyIdnew,cmd.getBlacklist().getLbl_id(),4,cmd.getBlacklistfamily().getLblf_nama_anak1(),cmd.getBlacklistfamily().getLblf_tgllhr_anak1(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
			blacklistFamilyIdnew++;
		}
		if(c > 0){c--;}
		if(cmd.getBlacklistfamily().getLblf_nama_anak2() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_anak2() != null && c == 0){
			uwDao.insertLstBlacklistFamily(new BlackListFamily(blacklistFamilyIdnew,cmd.getBlacklist().getLbl_id(),4,cmd.getBlacklistfamily().getLblf_nama_anak2(),cmd.getBlacklistfamily().getLblf_tgllhr_anak2(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
			blacklistFamilyIdnew++;
		}
		if(c > 0){c--;}
		if(cmd.getBlacklistfamily().getLblf_nama_anak3() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_anak3() != null && c == 0){
			uwDao.insertLstBlacklistFamily(new BlackListFamily(blacklistFamilyIdnew,cmd.getBlacklist().getLbl_id(),4,cmd.getBlacklistfamily().getLblf_nama_anak3(),cmd.getBlacklistfamily().getLblf_tgllhr_anak3(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
			blacklistFamilyIdnew++;
		}
		//update
		if(cmd.getBlacklistfamily().getLblf_nama_si() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_si() != null){
			//blacklistFamilyIdnew++;
			uwDao.updateLstBlacklistFamily(new BlackListFamily(cmd.getBlacklistfamily().getLblf_id(),cmd.getBlacklist().getLbl_id(),5,cmd.getBlacklistfamily().getLblf_nama_si(),cmd.getBlacklistfamily().getLblf_tgllhr_si(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
		}
		if(cmd.getBlacklistfamily().getLblf_nama_anak1() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_anak1() != null){
//			blacklistFamilyIdnew++;
			uwDao.updateLstBlacklistFamily(new BlackListFamily(cmd.getBlacklistfamily().getLblf_id(),cmd.getBlacklist().getLbl_id(),4,cmd.getBlacklistfamily().getLblf_nama_anak1(),cmd.getBlacklistfamily().getLblf_tgllhr_anak1(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
		}
		if(cmd.getBlacklistfamily().getLblf_nama_anak2() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_anak2() != null){
//			blacklistFamilyIdnew++;
			uwDao.updateLstBlacklistFamily(new BlackListFamily(cmd.getBlacklistfamily().getLblf_id(),cmd.getBlacklist().getLbl_id(),4,cmd.getBlacklistfamily().getLblf_nama_anak2(),cmd.getBlacklistfamily().getLblf_tgllhr_anak2(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
		}
		if(cmd.getBlacklistfamily().getLblf_nama_anak3() != "" && cmd.getBlacklistfamily().getLblf_tgllhr_anak3() != null){
//			blacklistFamilyIdnew++;
			uwDao.updateLstBlacklistFamily(new BlackListFamily(cmd.getBlacklistfamily().getLblf_id(),cmd.getBlacklist().getLbl_id(),4,cmd.getBlacklistfamily().getLblf_nama_anak3(),cmd.getBlacklistfamily().getLblf_tgllhr_anak3(),Integer.parseInt(user.getLus_id()),commonDao.selectSysdate()));
		}
		
		//===========
	}

	public String selectProductName(String reg_spaj){
		return uwDao.selectProductName(reg_spaj);
	}
	
	public void prosesInputRecommend(Command command){
		Matrix matrix = new Matrix();
		Rekomendasi rekomendasi = new Rekomendasi();
		Aspirasi aspirasi = new Aspirasi();
		String mns_kd_nasabah = command.getMatrix().getListMatrix().get(0).getMns_kd_nasabah();
		//bagian ini untuk melakukan update ke tabel mst_matrix_adv
		List<Matrix> listMatrix = command.getMatrix().getListMatrix();
		for(int i=0;i<listMatrix.size();i++){
			matrix.setMns_kd_nasabah(mns_kd_nasabah);
			matrix.setLsbs_id(listMatrix.get(i).getLsbs_id());
			//matrix.setLsbs_name(listMatrix.get(i).getLsbs_name());
			matrix.setMsma_fund(listMatrix.get(i).getMsma_fund());
			matrix.setMsma_studi_c(listMatrix.get(i).getMsma_studi_c());
			matrix.setMsma_studi_b(listMatrix.get(i).getMsma_studi_b());
			matrix.setMsma_studi_a(listMatrix.get(i).getMsma_studi_a());
			matrix.setMsma_pensiun_c(listMatrix.get(i).getMsma_pensiun_c());
			matrix.setMsma_pensiun_b(listMatrix.get(i).getMsma_pensiun_b());
			matrix.setMsma_pensiun_a(listMatrix.get(i).getMsma_pensiun_a());
			matrix.setMsma_proteksi_c(listMatrix.get(i).getMsma_proteksi_c());
			matrix.setMsma_proteksi_b(listMatrix.get(i).getMsma_proteksi_b());
			matrix.setMsma_proteksi_a(listMatrix.get(i).getMsma_proteksi_a());
			matrix.setMsma_saving_c(listMatrix.get(i).getMsma_saving_c());
			matrix.setMsma_saving_b(listMatrix.get(i).getMsma_saving_b());
			matrix.setMsma_saving_a(listMatrix.get(i).getMsma_saving_a());
			commonDao.updateMstMatrix(matrix);
		}
		
		//bagian ini untuk melakukan update ke tabel mst_rekomendasi
		List<Rekomendasi> listRekomendasi = command.getRekomendasi().getListRekomendasi();
		for(int i=0;i<listRekomendasi.size();i++){
			rekomendasi.setMns_kd_nasabah(mns_kd_nasabah);
			rekomendasi.setMsrek_no(i+1);
			rekomendasi.setLsbs_id(listRekomendasi.get(i).getLsbs_id());
			rekomendasi.setMsrek_alasan(listRekomendasi.get(i).getMsrek_alasan());
			commonDao.updateMstRekomendasi(rekomendasi);
		}
		
		//bagian ini untuk melakukan update ke tabel mst_aspirasi
		List<Aspirasi> listAspirasi = command.getAspirasi().getListAspirasi();
		for(int i=0;i<listAspirasi.size();i++){
			aspirasi.setMns_kd_nasabah(mns_kd_nasabah);
			aspirasi.setMsas_no(listAspirasi.get(i).getMsas_no());
			aspirasi.setMsas_aspirasi(listAspirasi.get(i).getMsas_aspirasi());
			commonDao.updateMstAspirasi(aspirasi);
		}
		
	}
	
	public void prosesInputActivity(Aktivitas aktivitas){
		List<Aktivitas> listAktivitas = aktivitas.getListAktivitas();
		for(int i=0;i< listAktivitas.size();i++){
			aktivitas.setMns_kd_nasabah(listAktivitas.get(i).getMns_kd_nasabah());
			aktivitas.setPert_ke(listAktivitas.get(i).getPert_ke());
			aktivitas.setKd_aktivitas(listAktivitas.get(i).getKd_aktivitas());
			aktivitas.setTgl_pert(listAktivitas.get(i).getTgl_pert());
			aktivitas.setKeterangan(listAktivitas.get(i).getKeterangan());
			aktivitas.setStatus(listAktivitas.get(i).getStatus());
			aktivitas.setApproval(listAktivitas.get(i).getApproval());
			if(aktivitas.getApproval()==null){
				aktivitas.setApproval(0);
			}
			aktivitas.setTgl_transfer(listAktivitas.get(i).getTgl_transfer());
			commonDao.updateMstAktivitasUsingModel(aktivitas);
		}
	}
	
	public void prosesInputAnalyst(Command command){
		Nasabah nasabah = command.getNasabah();
		Jiffy jiffy = command.getJiffy();
		Kebutuhan kebutuhan = new Kebutuhan();
		ProdBank prodBank = new ProdBank();
		Pendapatan pendapatan = new Pendapatan();
		Aktivitas aktivitas = new Aktivitas();
		String mns_kd_nasabah = jiffy.getMns_kd_nasabah();
		Date sysdate = commonDao.selectSysdate();
		
		commonDao.updateMstnasabah(nasabah);
		commonDao.updateMstjiffy(jiffy);
		
		//untuk Update nilai dari tabel mst_kebutuhan
		List<Kebutuhan> listKebutuhan = command.getKebutuhan().getListKebutuhan();
		for(int i=0;i<listKebutuhan.size();i++){
			kebutuhan.setLjk_id(listKebutuhan.get(i).getLjk_id());
			kebutuhan.setMkb_alasan(listKebutuhan.get(i).getMkb_alasan());
			kebutuhan.setMkb_jml_butuh(listKebutuhan.get(i).getMkb_jml_butuh());
			kebutuhan.setMkb_jml_dpoleh(listKebutuhan.get(i).getMkb_jml_dpoleh());
			kebutuhan.setMkb_no(listKebutuhan.get(i).getMkb_no());
			kebutuhan.setMkb_pdpt_butuh(listKebutuhan.get(i).getMkb_pdpt_butuh());
			kebutuhan.setMkb_pdpt_dpoleh(listKebutuhan.get(i).getMkb_pdpt_dpoleh());
			kebutuhan.setMkb_penting(listKebutuhan.get(i).getMkb_penting());
			kebutuhan.setMkb_rank(listKebutuhan.get(i).getMkb_rank());
			kebutuhan.setMkb_wkt_butuh_bl(listKebutuhan.get(i).getMkb_wkt_butuh_bl());
			kebutuhan.setMkb_wkt_butuh_th(listKebutuhan.get(i).getMkb_wkt_butuh_th());
			kebutuhan.setMkb_wkt_dpoleh_bl(listKebutuhan.get(i).getMkb_wkt_dpoleh_bl());
			kebutuhan.setMkb_wkt_dpoleh_th(listKebutuhan.get(i).getMkb_wkt_dpoleh_th());
			kebutuhan.setMns_kd_nasabah(mns_kd_nasabah);
			commonDao.updateMstKebutuhan(kebutuhan);
		}
		
//		untuk Update nilai dari tabel mst_pendapatan
		List<Pendapatan> listPendapatan = command.getPendapatan().getListPendapatan();
		for(int i=0;i<listPendapatan.size();i++){
			pendapatan.setLsp_id(listPendapatan.get(i).getLsp_id());
			pendapatan.setLsp_in_out(listPendapatan.get(i).getLsp_in_out());
			pendapatan.setMpp_value(listPendapatan.get(i).getMpp_value());
			pendapatan.setMpp_value_alasan(listPendapatan.get(i).getMpp_value_alasan());
			pendapatan.setMns_kd_nasabah(mns_kd_nasabah);
			commonDao.updateMstPendapatan(pendapatan);
		}
		
//		untuk Update&insert nilai dari tabel mst_prod_bank
		List<ProdBank> prodBankAfter = command.getProdBank().getListProdBank();
		List<ProdBank> prodBankBefore = commonDao.selectMstProdBankPlusLpbKet(mns_kd_nasabah);
		int sizeAfterProdBank = prodBankAfter.size();
		int sizeBeforeProdBank = prodBankBefore.size();
		
		for(int i=sizeBeforeProdBank;i<sizeAfterProdBank;i++){
			prodBank.setLpb_id(prodBankAfter.get(i).getLpb_id());
			if(prodBank.getLpb_id()==null){
				prodBank.setLpb_id(2);
			}
			prodBank.setMpb_no(i+1);
			prodBank.setMpb_status(prodBankAfter.get(i).getMpb_status());
			prodBank.setMpb_nm_bank(prodBankAfter.get(i).getMpb_nm_bank());
			prodBank.setMpb_tabungan(prodBankAfter.get(i).getMpb_tabungan());
			prodBank.setMpb_nm_tabungan(prodBankAfter.get(i).getMpb_nm_tabungan());
			prodBank.setMpb_jml_tabungan(prodBankAfter.get(i).getMpb_jml_tabungan());
			prodBank.setMpb_ket_nm_bank(prodBankAfter.get(i).getMpb_ket_nm_bank());
			prodBank.setMpb_note(prodBankAfter.get(i).getMpb_note());
			prodBank.setMns_kd_nasabah(mns_kd_nasabah);
			commonDao.insertMstProdBank(prodBank);
		}
		
		for(int i=0;i<sizeBeforeProdBank;i++){
			prodBank.setLpb_id(prodBankAfter.get(i).getLpb_id());
			prodBank.setMpb_no(i+1);
			prodBank.setMpb_status(prodBankAfter.get(i).getMpb_status());
			prodBank.setMpb_nm_bank(prodBankAfter.get(i).getMpb_nm_bank());
			prodBank.setMpb_tabungan(prodBankAfter.get(i).getMpb_tabungan());
			prodBank.setMpb_nm_tabungan(prodBankAfter.get(i).getMpb_nm_tabungan());
			prodBank.setMpb_jml_tabungan(prodBankAfter.get(i).getMpb_jml_tabungan());
			prodBank.setMpb_ket_nm_bank(prodBankAfter.get(i).getMpb_ket_nm_bank());
			prodBank.setMpb_note(prodBankAfter.get(i).getMpb_note());
			prodBank.setMns_kd_nasabah(mns_kd_nasabah);
			commonDao.updateMstProdBank(prodBank);
		}
		
//		untuk Update&insert nilai dari tabel mst_aktivitas
		List<Aktivitas> aktivitasAfter = command.getAktivitas().getListAktivitas();
		List<Aktivitas> aktivitasBefore = commonDao.selectMstAktivitasNext(mns_kd_nasabah);
		int sizeAfterAktivitas = aktivitasAfter.size();
		int sizeBeforeAktivitas = aktivitasBefore.size();
		
		for(int i=sizeBeforeAktivitas;i<sizeAfterAktivitas;i++){
			aktivitas.setTgl_pert(aktivitasAfter.get(i).getTgl_pert());
			aktivitas.setKeterangan(aktivitasAfter.get(i).getKeterangan());
			aktivitas.setStatus(0);
			aktivitas.setPert_ke(aktivitasAfter.get(i).getPert_ke());
			if(aktivitas.getPert_ke()==null){
				aktivitas.setPert_ke(1);
			}
			aktivitas.setKd_aktivitas(aktivitasAfter.get(i).getKd_aktivitas());
			aktivitas.setApproval(aktivitasAfter.get(i).getApproval());
			aktivitas.setMns_kd_nasabah(mns_kd_nasabah);
			commonDao.insertMstAktivitasUsingModel(aktivitas);
		}
		
		for(int i=0;i<sizeBeforeAktivitas;i++){
			aktivitas.setTgl_pert(aktivitasAfter.get(i).getTgl_pert());
			aktivitas.setKeterangan(aktivitasAfter.get(i).getKeterangan());
			aktivitas.setStatus(aktivitasAfter.get(i).getStatus());
			aktivitas.setPert_ke(aktivitasAfter.get(i).getPert_ke());
			aktivitas.setKd_aktivitas(aktivitasAfter.get(i).getKd_aktivitas());
			aktivitas.setApproval(aktivitasAfter.get(i).getApproval());
			aktivitas.setMns_kd_nasabah(mns_kd_nasabah);
			commonDao.updateMstAktivitasUsingModel(aktivitas);
		}
		
	}
	
	public void prosesInputFollowUp(Aktivitas aktivitas){
		String mns_kd_nasabah = aktivitas.getMns_kd_nasabah();
		List<Aktivitas> lstAktivitas = commonDao.selectMstAktivitas(mns_kd_nasabah);
		aktivitas.setPert_ke(lstAktivitas.size()+1);
		int i = 0;
		commonDao.insertMstAktivitasUsingModel(aktivitas);
	}
	
	public void prosesInputJiffy(Command command){
		//bagian ini untuk update tabel mst_jiffy
		Jiffy jiffy = command.getJiffy();
		RelasiNasabah relasiNasabah = new RelasiNasabah();
		Children children = new Children();
		commonDao.updateMstjiffy(jiffy);
		String mns_kd_nasabah = jiffy.getMns_kd_nasabah();
		
		//bagian ini untuk proses add row dibagian referensi(terdiri dari update data yg telah ada dan insert data dari row yang di-add)
		List<RelasiNasabah> relasiAfterAdd = command.getRelasiNasabah().getListRelasiNasabah();
		List<RelasiNasabah> relasiBeforeAdd = commonDao.selectMstRelasiNasabah(command.getJiffy().getMns_kd_nasabah());
		int sizeAfterRelasi = relasiAfterAdd.size();
		int sizeBeforeRelasi = relasiBeforeAdd.size();
		int jumlahAddRelasi = sizeAfterRelasi-sizeBeforeRelasi;
		//bagian ini untuk melakukan insert row yang ditambah
		for(int i=sizeBeforeRelasi;i<sizeAfterRelasi;i++){
			relasiNasabah.setMns_kd_nasabah(mns_kd_nasabah);
			relasiNasabah.setMrn_no_relasi(i+1);
			relasiNasabah.setMrn_nama(relasiAfterAdd.get(i).getMrn_nama());
			relasiNasabah.setMrn_alamat(relasiAfterAdd.get(i).getMrn_alamat());
			relasiNasabah.setMrn_contact_no(relasiAfterAdd.get(i).getMrn_contact_no());
			relasiNasabah.setMrn_ket_relasi(relasiAfterAdd.get(i).getMrn_ket_relasi());
			relasiNasabah.setMrn_alasan(relasiAfterAdd.get(i).getMrn_alasan());
			relasiNasabah.setMrn_ref_to_bii(relasiAfterAdd.get(i).getMrn_ref_to_bii());
			relasiNasabah.setReferrer_id(relasiAfterAdd.get(i).getReferrer_id());
			commonDao.insertMstRelasiNasabah(relasiNasabah);
		}
		//bagian ini untuk melakukan update row yang telah ada
		for(int i=0;i<sizeBeforeRelasi;i++){
			relasiNasabah.setMns_kd_nasabah(mns_kd_nasabah);
			relasiNasabah.setMrn_no_relasi(i+1);
			relasiNasabah.setMrn_nama(relasiAfterAdd.get(i).getMrn_nama());
			relasiNasabah.setMrn_alamat(relasiAfterAdd.get(i).getMrn_alamat());
			relasiNasabah.setMrn_contact_no(relasiAfterAdd.get(i).getMrn_contact_no());
			relasiNasabah.setMrn_ket_relasi(relasiAfterAdd.get(i).getMrn_ket_relasi());
			relasiNasabah.setMrn_alasan(relasiAfterAdd.get(i).getMrn_alasan());
			relasiNasabah.setMrn_ref_to_bii(relasiAfterAdd.get(i).getMrn_ref_to_bii());
			relasiNasabah.setReferrer_id(relasiAfterAdd.get(i).getReferrer_id());
			commonDao.updateMstRelasiNasabah(relasiNasabah);
		}
		
//		bagian ini untuk proses add row dibagian rencana pendidikan(terdiri dari update data yg telah ada dan insert data dari row yang di-add)
		List<Children> childAfterAdd = command.getChildren().getListChildren();
		List<Children> childBeforeAdd = commonDao.selectMstChildren(command.getJiffy().getMns_kd_nasabah());
		int sizeAfterChild = childAfterAdd.size();
		int sizeBeforeChild = childBeforeAdd.size();
		int jumlahAddChild = sizeAfterChild-sizeBeforeChild;
//		bagian ini untuk melakukan insert row yang ditambah
		for(int i=sizeBeforeChild;i<sizeAfterChild;i++){
			children.setMns_kd_nasabah(mns_kd_nasabah);
			children.setMch_id(i+1);
			children.setMch_jn_relasi(childAfterAdd.get(i).getMch_jn_relasi());
			children.setMch_nama(childAfterAdd.get(i).getMch_nama());
			children.setMch_birth_date(childAfterAdd.get(i).getMch_birth_date());
			children.setMch_usia(childAfterAdd.get(i).getMch_usia());
			children.setMch_pekerjaan(childAfterAdd.get(i).getMch_pekerjaan());
			children.setMch_renc_studi(childAfterAdd.get(i).getMch_renc_studi());
			children.setMch_tempat_studi(childAfterAdd.get(i).getMch_tempat_studi());
			children.setMch_waktu_realisasi(childAfterAdd.get(i).getMch_waktu_realisasi());
			children.setMch_biaya(childAfterAdd.get(i).getMch_biaya());
			children.setMch_renc_skrg(childAfterAdd.get(i).getMch_renc_skrg());
			children.setMch_pengaturan(childAfterAdd.get(i).getMch_pengaturan());
			commonDao.insertMstChildren(children);
		}
		//bagian ini untuk melakukan update row yang telah ada
		for(int i=0;i<sizeBeforeChild;i++){
			children.setMns_kd_nasabah(mns_kd_nasabah);
			children.setMch_id(i+1);
			children.setMch_jn_relasi(childAfterAdd.get(i).getMch_jn_relasi());
			children.setMch_nama(childAfterAdd.get(i).getMch_nama());
			children.setMch_birth_date(childAfterAdd.get(i).getMch_birth_date());
			children.setMch_usia(childAfterAdd.get(i).getMch_usia());
			children.setMch_pekerjaan(childAfterAdd.get(i).getMch_pekerjaan());
			children.setMch_renc_studi(childAfterAdd.get(i).getMch_renc_studi());
			children.setMch_tempat_studi(childAfterAdd.get(i).getMch_tempat_studi());
			children.setMch_waktu_realisasi(childAfterAdd.get(i).getMch_waktu_realisasi());
			children.setMch_biaya(childAfterAdd.get(i).getMch_biaya());
			children.setMch_renc_skrg(childAfterAdd.get(i).getMch_renc_skrg());
			children.setMch_pengaturan(childAfterAdd.get(i).getMch_pengaturan());
			commonDao.updateMstChildren(children);
		}
		
	}
	
	
	public void prosesInputPre(DBank dBank,int flag, List<DBank> listDBankDefault){
		DBank dBankdefault = new DBank();
		TBank tBank = new TBank();
		Date sysdate = selectSysdateSimple();
		List<DBank> daftarBank = dBank.getListDBank(); 
		dBankdefault.setListDBank(listDBankDefault);
		int sizeBeforeAdd = daftarBank.size();
		int sizeAfterAdd = listDBankDefault.size();
		//String no_pre = sequence.sequenceNoPre();
		String no_pre   = this.uwDao.selectGetPacGl("nopre");
		if(flag==1){//flag 1 untuk proses insert data row baru dan update dari no_pre yang sudah ada
			int jumlahAddRow =sizeBeforeAdd-sizeAfterAdd;
			for(int i=sizeAfterAdd;i<sizeBeforeAdd;i++){
				dBankdefault.setKeterangan(daftarBank.get(i).getKeterangan());
				dBankdefault.setGiro(daftarBank.get(i).getGiro());
				dBankdefault.setKas(daftarBank.get(i).getKas());
				dBankdefault.setJumlah(daftarBank.get(i).getJumlah());
				dBankdefault.setKode_cash_flow(daftarBank.get(i).getKode_cash_flow());
				dBankdefault.setNo_jurnal(i+1);
				dBankdefault.setTgl_input(sysdate);
				if(daftarBank.get(0).getNo_pre()==null || daftarBank.get(0).getNo_pre()==""){
					dBankdefault.setNo_pre(no_pre);
					if(i==0){
					dBank.setFlag(1);
					tBank.setNo_pre(no_pre);
					tBank.setKas(daftarBank.get(0).getKas());
					tBank.setJumlah(daftarBank.get(0).getJumlah());
					tBank.setLus_id(dBank.getLus_id());
					tBank.setPosition(1);
					tBank.setLus_id_add(dBank.getLus_id());
					tBank.setMtb_manual(1);
					tBank.setTgl_input(sysdate);
					accountingDao.insertMstTBank(tBank);
					}
				}else{
					dBankdefault.setNo_pre(daftarBank.get(0).getNo_pre());
				}
				accountingDao.insertMstDBank(dBankdefault);
			}
			for(int i=0;i<sizeBeforeAdd;i++){
				dBank.setKeterangan(daftarBank.get(i).getKeterangan());
				dBank.setGiro(daftarBank.get(i).getGiro());
				dBank.setKas(daftarBank.get(i).getKas());
				dBank.setJumlah(daftarBank.get(i).getJumlah());
				dBank.setKode_cash_flow(daftarBank.get(i).getKode_cash_flow());
				dBank.setNo_jurnal(i+1);
				dBank.setTgl_input(sysdate);
				if(daftarBank.get(0).getNo_pre()==null || daftarBank.get(0).getNo_pre()==""){
					dBank.setNo_pre(no_pre);
				}else{
					dBank.setNo_pre(daftarBank.get(0).getNo_pre());
				}
				accountingDao.updateMstDBank(dBank);
			}
		}
		else if(flag == 2){//flag 2 untuk proses insert no_pre yang baru
			int jumlahAddRow =sizeBeforeAdd-sizeAfterAdd;
			tBank.setNo_pre(no_pre);
			tBank.setKas(daftarBank.get(0).getKas());
			tBank.setJumlah(daftarBank.get(0).getJumlah());
			tBank.setLus_id(dBank.getLus_id());
			tBank.setPosition(1);
			tBank.setLus_id_add(dBank.getLus_id());
			tBank.setMtb_manual(1);
			tBank.setTgl_input(sysdate);
			accountingDao.insertMstTBank(tBank);
			for(int i=sizeAfterAdd;i<sizeBeforeAdd;i++){
				dBankdefault.setKeterangan(daftarBank.get(i).getKeterangan());
				dBankdefault.setGiro(daftarBank.get(i).getGiro());
				dBankdefault.setKas(daftarBank.get(i).getKas());
				dBankdefault.setJumlah(daftarBank.get(i).getJumlah());
				if (i==0){
					dBankdefault.setKode_cash_flow(null);
				}else
					dBankdefault.setKode_cash_flow(daftarBank.get(i).getKode_cash_flow());
				dBankdefault.setNo_jurnal(i+1);
				dBankdefault.setNo_pre(no_pre);
				dBankdefault.setTgl_input(sysdate);
				accountingDao.insertMstDBank(dBankdefault);
				dBank.setNo_pre(no_pre);
			}
		}
		else {//flag ini hanya untuk proses update no_pre yang sudah ada
			for(int i=0;i<sizeBeforeAdd;i++){
				dBank.setKeterangan(daftarBank.get(i).getKeterangan());
				dBank.setGiro(daftarBank.get(i).getGiro());
				dBank.setKas(daftarBank.get(i).getKas());
				dBank.setJumlah(daftarBank.get(i).getJumlah());
				dBank.setKode_cash_flow(daftarBank.get(i).getKode_cash_flow());
				dBank.setNo_jurnal(i+1);
				dBank.setNo_pre(daftarBank.get(0).getNo_pre());
				dBank.setTgl_input(sysdate);
				accountingDao.updateMstDBank(dBank);
			}
		}
	}
	
	/**Fungsi:	Untuk Melakukan Proses Input dan update referral
	 * @param	Nasabah nasabah
	 * @author	Ferry Harlim
	 */
	public void prosesInputReferral(Nasabah nasabah){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		//cek apakah pernah input apa belum
		int liLevel=4;
		nasabah.setLspd_id(1);
		DecimalFormat f5=new DecimalFormat("00000");
		 Nasabah tempNasabah=new Nasabah();
			if(nasabah.getPlatinum()==10){//platinum buat no referral dulu
				String noRef=selectMaxMstNasabahMnsNoReff("02", "07", "01","5,9,10");
				if(noRef==null)
					noRef="00001p";
				else{
					String oldRef=noRef.substring(0,5);
					Integer count=Integer.parseInt(oldRef)+1;
					noRef=f5.format(count)+"p";
				}
				nasabah.setMns_no_ref(noRef);
			}

		 tempNasabah=commonDao.selectMstNasabah(nasabah.getMns_kd_nasabah());
		 if(tempNasabah==null){
			 String lsMax= nasabah.getKode()+ nasabah.getMns_no_ref();
			 nasabah.setMns_kd_nasabah(lsMax);
		}	 
		String lsAgen=nasabah.getMsag_id();
		
		//bagian ini untuk menentukan nilai checkbox(isi Jiffy), kalau tidak di check,maka nilainy 0
		if(nasabah.getMns_ok_saran()==null){
			nasabah.setMns_ok_saran(0);
		}
		if(commonDao.updateMstnasabah(nasabah)==0)
			commonDao.insertMstNasabah(nasabah);
		//(Deddy) tambahan dari seno : link antara mst_nasabah dengan mst_det_nasabah
		if(commonDao.updateMstDetnasabah(nasabah)==0)
			commonDao.insertMstDetNasabah(nasabah);
		
		if(commonDao.selectCountMstAktivitas(nasabah.getMns_kd_nasabah(), 1)==0){
			 commonDao.insertMstAktivitas(nasabah.getMns_kd_nasabah(), 1, 1, "Input Referral", 1, null);
		}
		
		
		commonDao.deleteMstAgentFa(nasabah.getMns_kd_nasabah());
		if(nasabah.getPlatinum()<5)//klo bukan specta dan smart
			commonDao.insertMstAgentFa(nasabah.getMns_kd_nasabah(), lsAgen, liLevel, null, null);
		/*if(! nasabah.getKode().substring(0,6).equals("020701")){
			liLevel=liLevel-1;
			//error referral disini query lebih dari 1
			Bfa bfa=new Bfa();
			bfa.setLevel_id(liLevel);
			bfa.setStatus_aktif(1);
			bfa.setKd_region(nasabah.getKd_region());
			bfa.setKd_koord(nasabah.getKd_koord());
			bfa.setKd_area(nasabah.getKd_area());
			bfa.setKd_cabang(nasabah.getKd_cabang());
			lsAgen=commonDao.selectMstBfaMsagId(bfa);
			if(lsAgen!=null){
				commonDao.insertMstAgentFa(nasabah.getMns_kd_nasabah(), lsAgen, liLevel, null, null);
			}
			liLevel=liLevel-1;
			//
			bfa.setLevel_id(liLevel);
			lsAgen=commonDao.selectMstBfaMsagId(bfa);
			if(lsAgen!=null){
				commonDao.insertMstAgentFa(nasabah.getMns_kd_nasabah(), lsAgen, liLevel, null, null);
			}
			liLevel=liLevel-1;
			//
			bfa.setLevel_id(liLevel);
			lsAgen=commonDao.selectMstBfaMsagId(bfa);
			if(lsAgen!=null){
				commonDao.insertMstAgentFa(nasabah.getMns_kd_nasabah(), lsAgen, liLevel, null, null);
			}
		}	*/
		//
		
	}
	
	public void prosesTransClosing(Command command){
		Nasabah nasabah = command.getNasabah();
		String mns_kd_nasabah = command.getNasabah().getMns_kd_nasabah();
		commonDao.updateMstAktivitas(mns_kd_nasabah, 5);
		commonDao.updateMstNasabahLspdId(mns_kd_nasabah, 99);
	}
	
	public void prosesTransClosingNon(Command command){
		Nasabah nasabah = command.getNasabah();
		String mns_kd_nasabah = command.getNasabah().getMns_kd_nasabah();
		commonDao.updateMstAktivitas(mns_kd_nasabah, 6);
		commonDao.updateMstNasabahLspdId(mns_kd_nasabah, 99);
	}
	
	public void prosesTransAnalyst(Command command){
		int i ;
		Integer li_jiffy = 0,li_ct = 0, li_bisnis;
		String mns_kd_nasabah = command.getJiffy().getMns_kd_nasabah();
		
		commonDao.updateMstAktivitas(mns_kd_nasabah, 3);
		li_ct = commonDao.selectCountMstAktivitas(mns_kd_nasabah, 4);
		if(li_ct<=0){
			li_jiffy = commonDao.selectCountMstAktivitasPertKe(mns_kd_nasabah);
			if(li_jiffy == null){
				li_jiffy = 0;
			}else li_jiffy++;
			commonDao.insertMstAktivitas(mns_kd_nasabah, li_jiffy, 4, null, 1, null);
		}
		List<Map> lsbs_id = commonDao.selectLstLsbsIDInMatrix();
		li_ct = 0;
		li_ct = commonDao.selectCountMstMatrixAdv(mns_kd_nasabah);
		if(li_ct<=0 || li_ct == null){
			for(i=0;i<lsbs_id.size();i++){
				Map hasilMap=lsbs_id.get(i);
				BigDecimal a = (BigDecimal)hasilMap.get("LSBS_ID") ;
				li_bisnis = a.intValue();
				String bisnis = li_bisnis.toString();
				String tmp = props.getProperty("jiffy.unit_link");
				if(tmp.contains(bisnis)){
					for(int j=1;j<=3;j++){
						int c = 0;
						commonDao.insertMstMatrixAdv(mns_kd_nasabah, li_bisnis, j);
					}
				}else {
					int b = 0;
					commonDao.insertMstMatrixAdv(mns_kd_nasabah, li_bisnis, 0);
				}
			}
		}
		
		li_ct=0;
		li_ct = commonDao.selectCountMstAspirasi(mns_kd_nasabah);
		if(li_ct<=0 || li_ct == null){
			for(i=1;i<=3;i++){
				commonDao.insertMstAspirasi(mns_kd_nasabah, i);
			}
		}
		
		li_ct=0;
		li_ct = commonDao.selectCountMstRekomendasi(mns_kd_nasabah);
		if(li_ct<=0 || li_ct == null){
			for(i=1;i<=3;i++){
				commonDao.insertMstRekomendasi(mns_kd_nasabah, i);
			}
		}
		
		commonDao.updateMstNasabahLspdId(mns_kd_nasabah, 4);
	}
	
	public void prosesTransRecommend(Command command){
		int li_count = 0, li_ct = 0; 
		Integer li_bisnis = 0;
		String mns_kd_nasabah = command.getMatrix().getListMatrix().get(0).getMns_kd_nasabah();
		commonDao.updateMstAktivitas(mns_kd_nasabah, 4);
		li_ct = commonDao.selectCountMstAktivitas(mns_kd_nasabah, 5);
		if(li_ct<=0){
			li_count = commonDao.selectCountMstAktivitasPertKe(mns_kd_nasabah);
			if(li_count == 0){
				li_count = 0;
			}else li_count++;
			commonDao.insertMstAktivitas(mns_kd_nasabah, li_count, 5, null, 1, null);
		}
		
		li_bisnis = commonDao.selectLsbsIdInMstRekomendasi(mns_kd_nasabah);
		if(li_bisnis>0 && li_bisnis != null){
			commonDao.updateMstNasabahLsbsId(mns_kd_nasabah, li_bisnis);
		}
		commonDao.updateMstNasabahLspdId(mns_kd_nasabah, 5);
	}
	
	
	public void prosesTransFact(Command command){
		int li_jiffy = 0, li_ct = 0;
		commonDao.updateMstAktivitas(command.getJiffy().getMns_kd_nasabah(), 2);
		li_ct = commonDao.selectCountMstAktivitas(command.getJiffy().getMns_kd_nasabah(), 3);
		if(li_ct<=0){
			li_jiffy = commonDao.selectCountMstAktivitasPertKe(command.getJiffy().getMns_kd_nasabah());
			if(li_jiffy == 0){
				li_jiffy=0;
			}else li_jiffy++;
			commonDao.insertMstAktivitas(command.getJiffy().getMns_kd_nasabah(), li_jiffy, 3, null, 1, null);
		}
		
		li_ct= 0;
		li_ct = commonDao.selectCountMstSurplus(command.getJiffy().getMns_kd_nasabah());
		if(li_ct<=0){
			commonDao.insertNasabahInMstSurplus(command.getJiffy().getMns_kd_nasabah());
		}
		
		li_ct= 0;
		li_ct = commonDao.selectCountMstProtect(command.getJiffy().getMns_kd_nasabah());
		if(li_ct<=0){
			commonDao.insertNasabahInMstProtect(command.getJiffy().getMns_kd_nasabah());
		}
		
		li_ct= 0;
		li_ct = commonDao.selectCountMstIncome(command.getJiffy().getMns_kd_nasabah());
		if(li_ct<=0){
			commonDao.insertNasabahInMstIncome(command.getJiffy().getMns_kd_nasabah());
		}
		
		List<Map> lsJnKebutuhan = commonDao.selectLstJnKebutuhan();
		li_ct = commonDao.selectCountMstKebutuhan(command.getJiffy().getMns_kd_nasabah());
		if(li_ct<=0){
			for(int i=0;i<lsJnKebutuhan.size();i++){
				int mkb_no = i+1;
				Map hasilMap=lsJnKebutuhan.get(i);
				BigDecimal a = (BigDecimal)hasilMap.get("LJK_ID") ;
				int ljk_id = a.intValue();
				commonDao.insertNasabahInMstKebutuhan(command.getJiffy().getMns_kd_nasabah(), ljk_id , mkb_no);
			}
		}
		
		List<Map> lsPendapatan = commonDao.selectLstPendapatan();
		li_ct = commonDao.selectCountMstPendapatan(command.getJiffy().getMns_kd_nasabah());
		if(li_ct<=0){
			for(int i=0;i<lsPendapatan.size();i++){
				Map hasilMap = lsPendapatan.get(i);
				BigDecimal a = (BigDecimal) hasilMap.get("LSP_ID");
				BigDecimal b = (BigDecimal) hasilMap.get("LSP_IN_OUT");
				int lsp_id = a.intValue();
				int lsp_in_out = b.intValue();
				commonDao.insertNasabahInMstPendapatan(command.getJiffy().getMns_kd_nasabah(), lsp_id, lsp_in_out);
			}
		}
		commonDao.updateMstNasabahLspdId(command.getJiffy().getMns_kd_nasabah(), 3);
	}
	
	public void prosesTrans(Nasabah nasabah){
		int li_ct = 0;
		int li_count = 0;
		//untuk menetapkan nilai yang null menjadi 0
		if(nasabah.getMns_ok_saran()==null){
			nasabah.setMns_ok_saran(0);
		}
		//menentukan posisi apakah dipindah ke fact_find(2) atau closing non completion(6)
		if(nasabah.getMns_ok_saran()==1){
			nasabah.setLspd_id(2);
		}else{
			nasabah.setLspd_id(6);
		}
		commonDao.updateMstAktivitas(nasabah.getMns_kd_nasabah(),1);
		li_ct = commonDao.selectCountMstAktivitas(nasabah.getMns_kd_nasabah(), nasabah.getLspd_id());
		if(li_ct <=0){
			li_count = commonDao.selectCountMstAktivitasPertKe(nasabah.getMns_kd_nasabah());
			if(li_count == 0){
				li_count=0;
			}else li_count++;
			commonDao.insertMstAktivitas(nasabah.getMns_kd_nasabah(), li_count, nasabah.getLspd_id(), null, 1, null);
		}
		
		if(nasabah.getMns_ok_saran()==1){
			li_ct = 0;
			li_ct = commonDao.selectCountMstJiffy(nasabah.getMns_kd_nasabah());
			if(li_ct<=0){
				commonDao.insertMstJiffyKdNasabah(nasabah.getMns_kd_nasabah());
			}
		}
		
		commonDao.updateMstNasabahLspdId(nasabah.getMns_kd_nasabah(), nasabah.getLspd_id());
		
	}
	
	
	/**Fungsi:	Untuk Menampilkan data Jenis Alokasi Dana pada tabel EKA.MST_DET_ULINK
	 * @param	String spaj
	 * @return	List
	 * @author	Ferry Harlim
	 */
	public List selectDistinctMstDetUlinkLjiId(String spaj){
		return commonDao.selectDistinctMstDetUlinkLjiId(spaj);
	}
	/**Fungsi:	Untuk Menampilkan data Jenis Alokasi Dana pada tabel EKA.MST_TRANS_ULINK
	 * @param	String spaj
	 * @return	List
	 * @author	Ferry Harlim
	 */
	public List selectDistinctMstTransUlinkLjiId(String spaj){
		return commonDao.selectDistinctMstTransUlinkLjiId(spaj);
	}
	/**Fungsi:	Untuk Mengupdate kolom lar_email pada tabel EKA.LST_ADDR_REGION 
	 * @param	String[] larId,String[] larEmail;
	 * @author  Ferry Harlim
	 * @return 
	 */
	public void updateLstAddrRegionLarEmail(String[] larId, String[] larEmail){
		for(int i=0;i<larId.length;i++){
			commonDao.updateLstAddrRegionLarEmail(larId[i], larEmail[i]);
		}	
	}
	/**Fungsi:	Untuk Menampilkan jenis nasabah link
	 * @return	List
	 * @author	Ferry Harlim
	 */
	public List selectLstJnNasabah()throws DataAccessException{
		return commonDao.selectLstJnNasabah();
	}
	
	public Integer selectFlagLeadJnNasabah(Integer jn_nasabah){
		return commonDao.selectFlagLeadJnNasabah(jn_nasabah);
	}
	
	public Integer selectCountReferralNumber()throws DataAccessException{
		return commonDao.selectCountReferralNumber();
	}

	/* ======================= FinanceDao ======================= */
	
	public String selectKursReksadana(String ire_reksa_no) {
		return this.financeDao.selectKursReksadana(ire_reksa_no);
	}
	
	public Map selectDataAverageCostReksadana(String ire_reksa_no, Date irt_trans_date){
		return this.financeDao.selectDataAverageCostReksadana(ire_reksa_no, irt_trans_date);
	}
	
	public Map selectDataAverageCostBuyingReksadana(String ire_reksa_no, Date irt_trans_date){
		return this.financeDao.selectDataAverageCostBuyingReksadana(ire_reksa_no, irt_trans_date);
	}
	
	public List<String> saveUploadSaham(String tanggal, String tanggalAkhir, List<Map> daftar, User currentUser){
		return this.financeDao.saveUploadSaham(tanggal, tanggalAkhir, daftar, currentUser);
	}
	
	public List<String> saveUploadPooledFunds(String tanggal, String tanggalAkhir, List<Map> daftar, User currentUser,HttpServletRequest request) throws ServletRequestBindingException{
		return this.financeDao.saveUploadPooledFunds(tanggal, tanggalAkhir, daftar, currentUser,request);
	}
	
	public List<String> saveUploadBonds(String tanggal, List<Map> daftar, User currentUser){
		return this.financeDao.saveUploadBonds(tanggal, daftar, currentUser);
	}
	
	public List<String> saveUploadTransaksiReksadana(List<Reksadana> daftar, User currentUser){
		return this.financeDao.saveUploadTransaksiReksadana(daftar, currentUser);
	}
	
	public List<String> hapusSaham(String tanggal, String tanggalAkhir, User currentUser){
		return this.financeDao.hapusSaham(tanggal, tanggalAkhir, currentUser);
	}
	
	public List<String> hapusPooledFunds(String tanggal, String tanggalAkhir, User currentUser){
		return this.financeDao.hapusPooledFunds(tanggal, tanggalAkhir, currentUser);
	}
	
	public List<String> hapusBonds(String tanggal, User currentUser){
		return this.financeDao.hapusBonds(tanggal, currentUser);
	}

	public List selectListBayarKomisi(String startDate, String endDate) {
		return this.financeDao.selectListBayarKomisi(startDate, endDate);
	}
	
	public List warning_ttp() {
		return this.financeDao.warning_ttp();
	}
	
	public List warning_ttp_komisigaDiproses() {
		return this.financeDao.warning_ttp_komisigaDiproses();
	}
	public Integer count_ttp_komisigaDiproses(String spaj)throws DataAccessException{
		return this.financeDao.count_ttp_komisigaDiproses(spaj);
	}
	
	public Integer count_sdhlewatKom(String spaj)throws DataAccessException{
		return this.financeDao.count_sdhlewatKom(spaj);
	}
	public List<Reksadana> selectReksadana(String nama) {
		return this.financeDao.selectReksadana(nama);
	}
	
	public List selectDetailReksadanaByDate(Reksadana r) {
		return this.financeDao.selectDetailReksadanaByDate(r);
	}
	
	public int insertDetailReksadana(User currentUser, Date tgl, List<Reksadana> listReksadana) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		int counter = 0;
//		StringBuffer sudah = new StringBuffer();
		
		if(listReksadana == null) return counter;
		for(Reksadana r : listReksadana) {
//			if(sudah.indexOf(r.getIre_reksa_name()) == -1) {
//				sudah.append(r.getIre_reksa_name());
			int exist = this.financeDao.selectCekExistDetailReksadana(r.getIre_reksa_no(), tgl);
			int prevfile = this.financeDao.selectCekExistDetailReksadana(r.getIre_reksa_no(), FormatDate.add(tgl, Calendar.DATE, -1));
			if(exist == 0 && prevfile == 0){
				r.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				r.setIrd_trans_date(tgl);
				r.setIrd_reksa_trans(null);
				r.setIrd_process(selectSysdate());
				this.financeDao.insertDetailReksadana(r);
				counter ++;
			}else if(exist == 0 && prevfile == 1){
				r.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				r.setIrd_trans_date(tgl);
				r.setIrd_reksa_trans(null);
				r.setIrd_process(selectSysdate());
				this.financeDao.insertDetailReksadana(r);
				counter ++;
			}else if(prevfile != 0){
				counter = new Integer(-1);
			}
		}
		return counter;
	}
	
	public void saveDeduct(HttpServletRequest request, User currentUser){
		try {
			this.financeDao.saveDeduct(request, currentUser);
		} catch (ParseException e) {
			logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
	}

	public List selectDaftarKomisiAgen(String spaj) {
		return this.financeDao.selectDaftarKomisiAgen(spaj);
	}

	public List selectDeductKomisiAgen(String msco) {
		return this.financeDao.selectDeductKomisiAgen(msco);
	}

	public List selectJenisDeduct() {
		return this.financeDao.selectJenisDeduct();
	}

	public List selectKomisiAgen(String spaj, int tahun, int premi) {
		return this.financeDao.selectKomisiAgen(spaj, tahun, premi);
	}
	

	/* ======================= BlankoDao ==================== */
	
	public List<FormSpaj> selectFormBlanko(String msf_id, String lca_id) {
		return blankoDao.selectFormBlanko(msf_id, lca_id);
	}
	
	public Map select_mst_form(String msf_id)
	{
		return blankoDao.select_mst_form(msf_id);
	}
		

	/* ======================= ReinstateDao ======================= */
	public String saveMstTransHistory(String reg_spaj, String trans_no, String kolom_tanggal, String tanggal, String hh, String mm, String user_tanggal, String lus_id) {
		return reinstateDao.saveMstTransHistory(reg_spaj, trans_no, kolom_tanggal, tanggal, hh, mm, user_tanggal, lus_id);
	}
	
	public List selectDaftarReinstate(int lspd_id1,int lspd_id2,String kata,String kategori){
		return reinstateDao.selectDaftarReinstate(lspd_id1,lspd_id2,kata,kategori);
	}
	
	public Integer selectCountMstReins(String spaj){
		return (Integer)reinstateDao.selectCountMstReins(spaj);
	}
	
	public Map selectDocumentPosition(String spaj){
		return (HashMap)reinstateDao.selectDocumentPosition(spaj);
	}
	
	public Date selectMsre_next_prm_date(String spaj){
		return (Date)reinstateDao.selectMsre_next_prm_date(spaj);
	}
	
	public Date selectDate(){
		return (Date) reinstateDao.selectDate();
	}
	
	public String prosesReinsReas(String spaj,String lusId,String noReins,Integer lspdId,Integer liBulan){
		return reinstate.prosesReinsReas(spaj, lusId, noReins, lspdId, liBulan);
	}
	
	public void prosesBackToReinstate(String spaj,String noReins)throws DataAccessException{
		reinstate.prosesBackToReinstate(spaj, noReins);
	}

	public void prosesTransferReinstate(String spaj,String noReins, String lus_id)throws DataAccessException{
		reinstate.prosesTransferReinstate(spaj, noReins, lus_id);
	}
	
	public void prosesEditSurat(String spaj,String reinsNo,Integer aksep,Integer kondisi,String kondisiNote,
			Double totPremi,Date tglAccept, Double totBunga,Date tglPaid,String acceptNote, String lus_id){
		reinstate.prosesEditSurat(spaj,reinsNo,aksep,kondisi,kondisiNote,
				totPremi,tglAccept, totBunga,tglPaid,acceptNote);
		Date nowDate = commonDao.selectSysdate();
		String mi_id = "", statusAksep = "";
		HashMap inbox = null;
		Integer lspd_id_from= 0, lspd_id_from2 = 0; 
		List <Map> mapInbox = uwDao.selectMstInbox(spaj, "4");
		if(mapInbox.size()>0){			
			inbox = (HashMap) mapInbox.get(0);
			mi_id = (String) inbox.get("MI_ID");
			lspd_id_from = ((BigDecimal) inbox.get("LSPD_ID")).intValue();
			lspd_id_from2 = ((BigDecimal) inbox.get("LSPD_ID_FROM")).intValue();
			if(aksep==0)	statusAksep = "REINSTATE  DITOLAK OLEH UNDERWRITING";
			if(aksep==1)	statusAksep = "REINSTATE DITERIMA OLEH UNDERWRITING";				
			uwDao.updateMstInboxLspdId(mi_id, lspd_id_from2, lspd_id_from, 1, null, null, null,statusAksep, 0);
			MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id_from2, null, null, statusAksep, Integer.parseInt(lus_id), nowDate, null,0,0);
			uwDao.insertMstInboxHist(mstInboxHist);	
		}
		
	}
	
	public void prosesPrintSuratKonfirmasiPemulihanPolis(String spaj,String reins,String lusId,Integer msur_print,User currentUser){
		reinstate.prosesPrintSuratKonfirmasiPemulihanPolis(spaj, reins, lusId, msur_print,currentUser);
	}

	public void prosesCetakUlangSuratKonfirmasi(String spaj,String reins,Integer msurPrint,Date msurPrintDate,String desc ) throws DataAccessException{
		reinstate.prosesCetakUlangSuratKonfirmasi(spaj, reins, msurPrint, msurPrintDate, desc);
	}	

	public List selectMstReinstateLspdId(String spaj){
		return reinstateDao.selectMstReinstateLspdId(spaj);
	}

	public Map selectUwReinstate(String spaj,String noReins)throws DataAccessException{
		return (HashMap)reinstateDao.selectMstUwReinstate(spaj,noReins);
	}
	
	public Date selectSysdate()throws DataAccessException{
		return (Date)commonDao.selectSysdateTrunc();
	}
	
	
	public Edit selectSuratKonfirmasi(String spaj,String reinsNo){
		return reinstateDao.selectSuratKonfirmasi(spaj,reinsNo);
	}
	
	
	public Integer selectMstUwReinstateMsurPrint(String spaj,String reins){
		return (Integer) reinstateDao.selectMstUwReinstateMsurPrint(spaj,reins);
	}

	public Integer selectPowerSaveInterestPaid( String spajNo )
    {
        return this.refundDao.selectPowerSaveInterestPaid( spajNo );
    }

    public Integer selectPowerSaveRollOver( String spajNo )
    {
        return this.refundDao.selectPowerSaveRollOver( spajNo );
    }

	/* ======================= UwDao ======================= */
	public String selectIzinMeteraiTerakhir() {
		return this.uwDao.selectIzinMeteraiTerakhir();
	}
	
	public List selectDetailBisnis(String spaj) {
		return uwDao.selectDetailBisnis(spaj);
	}

	// yg diatas, ribet kalo dipindah
		
	/**@Fungsi:	untuk Mengetahui posisi spaj tersebut berasda
	 * @param	String spaj
	 * @return 	Map
	 * @author 	Ferry Harlim
	 * */
	public Map selectF_check_posisi(String spaj){
		return (HashMap)uwDao.selectF_check_posisi(spaj);
	}
		
	/*public Date selectFAddMonths(Date begDate,Integer i){
		return this.uwDao.selectFAddDate(begDate,i);
	}*/
	
	public Date selectFAddMonths(String s_tgl,int ai_month) throws ParseException{
		return uwDao.selectFaddMonths(s_tgl,ai_month);
	}
	
	public List selectGelar(Integer jenis) {
		return this.uwDao.selectGelar(jenis);
	}
	
	public List selectInfoEndorse(String noEndorse) {
		return this.uwDao.selectInfoEndorse(noEndorse);
	}
	
	public Map selectInfoProductInsured(String spaj, String lsbs) {
		return this.uwDao.selectInfoProductInsured(spaj, lsbs);
	}

	public String selectInsuredNumber(String spaj) {
		return this.uwDao.selectInsuredNumber(spaj);
	}
	
	public List selectJenisAplikasi() {
		return this.commonDao.selectJenisAplikasi();
	}
	
	public List selectJenisTransaksi() {
		return this.uwDao.selectJenisTransaksi();
	}

	public int selectJumlahPolisAgen(String nama, String lahir) {
		return this.uwDao.selectJumlahPolisAgen(nama, lahir);
	}
	
	public List selectKurs(){
		return this.uwDao.selectKurs();
	}
	
	public BigDecimal selectLstRiderRate(BigDecimal bisnisId,BigDecimal jenis,String kurs,int usiaPp,int usiaTt){
		return uwDao.selectLstRiderRate(bisnisId,jenis, kurs,usiaPp,usiaTt);
	}
	
	public List selectLstStatusAccept(Integer lssaId){
		return this.uwDao.selectLstStatusAccept(lssaId);
	}
	
	public List selectListStatusMuamalat(){
		return uwDao.selectListStatusMuamalat();
	}
	

	
	public List selectLstStatusAcceptAksepNFund(){
		return this.uwDao.selectLstStatusAcceptAksepNFund();
	}
	
	public List selectLstUser(){
		return this.uwDao.selectLstUser();
	}

	public List selectLstUser2(){
		return this.uwDao.selectLstUser2();
	}
	
	public Integer selectMaxMstDetMedical(String spaj,Integer insured){
		return uwDao.selectMaxMstDetMedical(spaj,insured);
	}
	
	public List selectMedical(String spaj,Integer insuredNo){
		return uwDao.selectMedical(spaj,insuredNo);
	}

	public List selectIcd(String spaj,Integer insuredNo){
		return uwDao.selectIcd(spaj,insuredNo);
	}	

	public List selectLsHslReas(String spaj,Integer insuredNo){
		return uwDao.selectLsHslReas(spaj,insuredNo);
	}	
	
	public Date selectMst_default(int id) {
		return this.uwDao.selectMst_default(id);
	}
	
	public Double selectMst_default_numeric(int id) {
		return this.uwDao.selectMst_default_numeric(id);
	}
	
	public List selectMstPositionSpaj(String spaj){
		return this.uwDao.selectMstPositionSpaj(spaj);
	}
	
	public Double selectMtuSaldoUnitLink(String spaj, int ke, int jn) {
		String lsbs = uwDao.selectBusinessId(spaj);
		if(lsbs.equals("162")) return (double) 0;
		return this.uwDao.selectMtuSaldoUnitLink(spaj, ke, jn);
	}

	public Date selectMuTglTrans(String spaj) {
		return this.uwDao.selectMuTglTrans(spaj);
	}
	
	public List<Payment> selectPaymentCount(String spaj, int tahun, int premi){
		return this.uwDao.selectPaymentCount(spaj, tahun, premi);
	}

	public List selectPayType(){
		return this.uwDao.selectPayType();
	}
	/**Fungsi:	Untuk menampilkan data pemegang polis secara detail
	 * @param 	String spaj
	 * @return	Map
	 * @author	Ferry Harlim
	 */
	public Map selectPemegang(String spaj){
		return this.uwDao.selectPemegang(spaj);
	}
	
	public String selectPolicyNumberFromSpaj(String spaj) {
		return this.uwDao.selectPolicyNumberFromSpaj(spaj);
	}
	
	public String selectCabangFromSpaj(String spaj) {
		if(spaj==null) return ""; else if(spaj.trim().equals("")) return ""; else 
		return this.uwDao.selectCabangFromSpaj(spaj);
	}
	
	public String selectCabangFromSpaj_lar(String spaj) {
		if(spaj==null) return ""; else if(spaj.trim().equals("")) return ""; else 
		return this.uwDao.selectCabangFromSpaj_lar(spaj);
	}
	
	public Date selectPolicyPrintDate(String spaj){
		return this.uwDao.selectPolicyPrintDate(spaj);
	}
	
	public String selectBegDatePowerSave(String spaj){
		return this.commonDao.selectBegDatePowerSave(spaj);
	}
	
	public Map selectPolisSpajPemegang(String spaj) {
		return this.uwDao.selectPolisSpajPemegang(spaj);
	}
	
	public List selectPositionSpaj(){
		return this.uwDao.selectPositionSpaj();
	}
	
	public Map selectPremiProdukUtama(String spaj) {
		return this.uwDao.selectPremiProdukUtama(spaj);
	}

	public List selectProductInsured(String spaj,BigDecimal insuredNo,Integer active){
		return this.uwDao.selectProductInsured(spaj,insuredNo,active);
	}
	
	public List selectRegions() {
		return this.uwDao.selectRegions(null);
	}
	
	public Double selectRekClientCount(String spaj){
		return this.uwDao.selectRekClientCount(spaj);
	}

	public List selectHistoryRekeningNasabah(String spaj, Integer type) {
		return this.uwDao.selectHistoryRekeningNasabah(spaj, type);
	}
	
	public Rekening_client selectRekeningNasabah(String spaj) {
		return this.uwDao.selectRekeningNasabah(spaj);
	}
	
	public Rekening_auto_debet selectRekeningAutoDebet(String spaj) {
		return this.uwDao.selectRekeningAutoDebet(spaj);
	}

	public Map selectInfoPribadi(String spaj) {
		return this.uwDao.selectInfoPribadi(spaj);
	}
	
	public List selectRincianInvestasi(String spaj) {
		return this.uwDao.selectRincianInvestasi(spaj);
	}

	public List selectRincianInvestasiNilaiPolis(String spaj, int ke) {
		return this.uwDao.selectRincianInvestasiNilaiPolis(spaj, ke);
	}
	
	public List selectRincianInvestasiRegister(String spaj) {
		return this.uwDao.selectRincianInvestasiRegister(spaj);
	}
	
	public List selectSPAJ(int posisi, int tipe, String kategori, String kata){
		return this.uwDao.selectSPAJ(posisi, tipe, kategori, kata);
	}

	public Integer selectSpajCancel(String spaj) {
		return this.uwDao.selectSpajCancel(spaj);
	}
	
	public Map selectStatusPolis(int lssp) {
		return this.uwDao.selectStatusPolis(lssp);
	}
	
	public Date selectSysdate(int daysAfter){
		return this.commonDao.selectSysdateTruncated(daysAfter);
	}
	
	public Date selectSysdateSimple() {
		return this.commonDao.selectSysdate();
	}
	/**@Fungsi:	Untuk menampilkan data Tertanggung secara Detail
	 * @param	String spaj
	 * @return 	Map
	 * @author 	Ferry Harlim	
	 * */
	 public Map selectTertanggung(String spaj){
		return this.uwDao.selectTertanggung(spaj);
	}
	
	 
	public Map selectTodayKurs(String lku_id, Date tanggal){
		return this.uwDao.selectTodayKurs(lku_id, tanggal);
	}
	
	public List selectTopUp(String spaj, int tahun, int premi, String urutan){
		return this.uwDao.selectTopUp(spaj, tahun, premi, urutan);
	}
	
	public TopUp selectTopUp(String spaj, String payment, int tahun, int premi){
		return this.uwDao.selectTopUp(spaj, payment, tahun, premi);
	}
	
	public Integer selectTotalCsfCall(String spaj, String lus_id) {
		return this.uwDao.selectTotalCsfCall(spaj, lus_id);
	}

	public List selectViewerBilling(String spaj) {
		return this.uwDao.selectViewerBilling(spaj);
	}
	
	public Map selectViewerDocPosition(String spaj){
		return this.uwDao.selectViewerDocPosition(spaj);
	}
	
	public List selectViewerInsured(String spaj, Integer ins) {
		return this.uwDao.selectViewerInsured(spaj, ins);
	}

	public List selectViewUlink(String spaj) {
		String lsbs = uwDao.selectBusinessId(spaj);
		if(products.stableLink(lsbs)) return this.uwDao.selectViewStableLink(spaj);
		else if(lsbs.equals("202")) return this.uwDao.selectViewExcelink(spaj);
		else return this.uwDao.selectViewUlink(spaj);
	}
	
	public Map selectWf_get_status(String spaj,int insured){
		return (HashMap)uwDao.selectWf_get_status(spaj,insured);
	}
	
	public List selectWilayah() {
		return this.uwDao.selectWilayah();
	}
	
	public int updateMst_address_billing(AddressBilling address, User user) {
		address.setLca_id(address.getRegion().substring(0,2));
		address.setLwk_id(address.getRegion().substring(2,4));
		address.setLsrg_id(address.getRegion().substring(4));
		
		address.setLska_id(bacDao.select_kabupaten(address.getKota().toUpperCase().trim()));

		this.uwDao.insertLst_ulangan(
				address.getReg_spaj(), selectSysdateSimple(),
				"EDIT ALAMAT PENAGIHAN",
				uwDao.selectStatusPolisFromSpaj(address.getReg_spaj()), 
				user.getLus_id(), "");
		
		return this.uwDao.updateMst_address_billing(address);
	}
	
	public void updateMst_policyPrintDate(String spaj, String kolom) {
		this.uwDao.updateMst_policyPrintDate(spaj, kolom);
	}

	public void insertMstPositionSpaj(String lus_id, String msps_desc, String reg_spaj, int addSecond){
		this.uwDao.insertMstPositionSpaj(lus_id, msps_desc, reg_spaj, addSecond);
	}
	
	public void updateMstDetMedical(Medical medical){
		uwDao.updateMstDetMedical(medical);
	}
	
	public void updateMstSurplusCalc(SurplusCalc surplusCalc){
		commonDao.updateMstSurplusCalc(surplusCalc);
	}
	
	public void updateMstProtectCalc(ProtectCalc protectCalc){
		commonDao.updateMstProtectCalc(protectCalc);
	}
	
	public void updateMstIncomeCalc(IncomeCalc incomeCalc){
		commonDao.updateMstIncomeCalc(incomeCalc);
	}
	
	public void updatePolicyAfterSendSoftcopy(String spaj, String lus_id, int lspd, int lssp) {
		//int jenisTerbit = uwDao.selectJenisTerbitPolis(spaj);
		if(Common.isEmpty(uwDao.selectMstPositionSpajMspsDesc(spaj, 6))){
			uwDao.updateTanggalKirimPolis(spaj);
			uwDao.saveMstTransHistory(spaj, "tgl_kirim_polis", null, null, null);
			uwDao.updateMst_policyPrintDate(spaj, "mspo_date_print");
			uwDao.saveMstTransHistory(spaj, "tgl_print_polis", null, null, null);
			//if(jenisTerbit == 1) 
			uwDao.insertMstPositionSpaj(lus_id, "KIRIM SOFTCOPY POLIS (E-LIONS)", spaj, 0);
			List list = this.uwDao.selectMeterai();
			if(list.isEmpty()) throw new RuntimeException("Data meterai tidak ada.");
			Meterai meterai = (Meterai) list.get(0);
			if(meterai.getMstm_saldo_akhir() >= 6000){
				meterai.setMsth_jumlah((double) 6000);
				this.uwDao.updateSaldoBeaMeterai(meterai);
				meterai.setMsth_ref_no(spaj);
				meterai.setMsth_jenis(2);
				meterai.setMsth_desc("BEA METERAI UNTUK SOFTCOPY POLIS NOMOR " + spaj);
				this.uwDao.insertHistoryBeaMeterai(meterai);
			}
		}
	}
	
	public void updateOtorisasiBankSinarmas(String spaj, String alasan, User currentUser){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		uwDao.updateMst_policyEmptyPrintDate(spaj);
		if(alasan.length() > 200) alasan = alasan.substring(0,200);
		uwDao.insertMstPositionSpaj(currentUser.getLus_id(), alasan, spaj, 0);
		deletemstStampHist(spaj);
		mstStampMaterai(mstm_bulan());
	}
	
	/**
	 * Fungsi ini mengupdate tanggal cetak polis, serta insert ke mst_position_spaj,
	 * Setelah 19/02/2007, fungsi ini juga berfungsi mengurangi saldo bea meterai,
	 * sekaligus menyimpan history pengurangan dananya itu
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 15, 2007 (11:23:08 AM)
	 * @param spaj
	 * @param kolom
	 * @param lus_id
	 * @param lspd
	 * @param lssp
	 * @param desc
	 */
	public void updatePolicyAndInsertPositionSpaj(String spaj, String kolom, String lus_id, int lspd, int lssp, String desc, boolean updateMeterai, User currentUser) {
		Map map = validationPrintPolis(spaj); 
		if(map.get("ORI")!=null) {
			//throw new RuntimeException("Maaf, tetapi Polis ini sudah pernah dicetak");
		}else {
			Integer jn_bank = currentUser.getJn_bank();
			uwDao.insertMstPositionSpaj(lus_id, desc, spaj, 0);
			uwDao.updateMst_policyPrintDate(spaj, kolom);
			uwDao.saveMstTransHistory(spaj, "tgl_print_polis", null, null, null);
			if(updateMeterai) {
				List list = this.uwDao.selectMeterai();
				if(list.isEmpty()) throw new RuntimeException("Data meterai tidak ada.");
				Meterai meterai = (Meterai) list.get(0);
				if(meterai.getMstm_saldo_akhir() >= 6000 && (jn_bank == 2 || jn_bank == 3)){
					meterai.setMsth_jumlah((double) 6000);
					uwDao.updateSaldoBeaMeterai(meterai);
					meterai.setMsth_ref_no(spaj);
					meterai.setMsth_jenis(2);
					meterai.setMsth_desc("BEA METERAI UNTUK PRINT POLIS NOMOR " + spaj);
					uwDao.insertHistoryBeaMeterai(meterai);
				}
			}
		}
		
		String lca_id = selectCabangFromSpaj(spaj);
		
		if(!lca_id.equals("01") && !lca_id.equals("58")) {
			Map info = this.uwDao.selectInformasiEmailSoftcopy(spaj);
			String nama = (String) info.get("MCL_FIRST");
			String no = (String) info.get("MSPO_POLICY_NO_FORMAT");
			String cab = (String) info.get("LCA_NAMA");
			if(nama != null && no != null && cab != null) {
				try {
					
					String alamat = this.uwDao.selectEmailCabangFromSpaj(spaj);
					if(alamat == null) alamat = currentUser.getEmail();
					if(alamat == null) alamat = props.getProperty("admin.ajsjava");
					
					/* Dicomment, req Hadi (feb 2011) via email
					email.send(false, alamat, new String[] {"supriyati@sinarmasmsiglife.co.id"}, null, null, 
							"Harap Cetak Simas Card untuk Polis dengan Nomor " + no + " a/n " + nama, 
							"Polis dengan nomor "+no+" a/n " + nama + " cabang " +cab+ " telah dicetak. Harap cetak Kartu Simas Disc Card.", null);
					*/
					
					String lsbs_id = uwDao.selectBusinessId(spaj);
					
					if(products.powerSave(lsbs_id) || products.stableLink(lsbs_id)) {
						String[] imel;
						if(lca_id.equals("09")){
							imel = new String[] {"supriyati@sinarmasmsiglife.co.id"};
						}else{
							imel = new String[] {"saputra@sinarmasmsiglife.co.id"};
						}
							EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
									null, 0, 0, new Date(), null, 
									false, alamat, imel, null, null, 
									"Polis dengan nomor "+no+" a/n " + nama + " cabang " +cab+ " telah dicetak", 
									"Polis dengan nomor "+no+" a/n " + nama + " cabang " +cab+ " telah dicetak", null, spaj);
						}
					
				} catch (MailException e) {
					logger.error("ERROR :", e);
				} 
			}
		}
	}
	
	public Integer countbea_perproduk( String tgl1, String tgl2)
	{
		return (Integer) this.uwDao.countbea_perproduk(tgl1, tgl2);
	}
	
	public String kode_stamp( String kode )
	{
		return (String) this.uwDao.kode_stamp(kode);
	}	
	
	public Double bulan_stamp( String bulan )
	{
		return (Double) this.uwDao.bulan_stamp(bulan);
	}
	
	public Stamp detil_kode_stamp( String kode )throws DataAccessException
	{
		return (Stamp) this.uwDao.detil_kode_stamp(kode);
	}
	
	public void updateRekeningNasabah(Rekening_client rekening) {
	//	String kurs1=null;
	//	String kurs2=null;
		Rekening_client rekening2 = null;
		rekening2 = new Rekening_client();
		rekening2 = selectRekeningNasabah(rekening.getReg_spaj());
		//kurs1=uwDao.selectSymbol(rekening2.getMrc_kurs());
	//	kurs2 = uwDao.selectSymbol(rekening.getMrc_kurs());
		//keterangan =rekening.getMrc_no_ac()+"( "+kurs2+" )";
		
				rekening.setKeterangan(FormatString.upper(rekening.getKeterangan()));
				rekening.setKuasa(FormatString.upper(rekening.getKuasa()));
				rekening.setLsbp_id(FormatString.upper(rekening.getLsbp_id()));
				rekening.setLsbp_nama(FormatString.upper(rekening.getLsbp_nama()));
				rekening.setMrc_cabang(FormatString.upper(rekening.getMrc_cabang()));
				rekening.setMrc_kota(FormatString.upper(rekening.getMrc_kota()));
				rekening.setMrc_kurs(FormatString.upper(rekening.getMrc_kurs()));
				rekening.setMrc_nama(FormatString.upper(rekening.getMrc_nama()));
				rekening.setMrc_no_ac(FormatString.upper(rekening.getMrc_no_ac()));
				rekening.setMrc_no_ac_lama(FormatString.upper(rekening.getMrc_no_ac_lama()));
		
		//rekening.setMrc_no_ac_lama(FormatString.upper(rekening.getMrc_no_ac_lama())+"( "+kurs1+" )");
		int updated = this.uwDao.updateRekeningNasabah(rekening);
		if(updated == 0) {
			if(rekening.getMrc_kuasa().intValue()!=1) rekening.setTgl_surat(null);
			this.uwDao.insertRekeningNasabah(rekening);
		}
		if(rekening2!=null){
			
			if(!rekening2.getMrc_nama().equals(rekening.getMrc_nama())){
				rekening.setMrc_no_ac_lama(FormatString.upper(rekening2.getMrc_nama()));
				rekening.setMrc_no_ac(FormatString.upper(rekening.getMrc_nama()));
				
			}else if(!rekening2.getMrc_kurs().equals(rekening.getMrc_kurs())){
				if(rekening2.getMrc_kurs().equals("01"))rekening2.setMrc_no_ac_lama("RUPIAH");
				if(rekening2.getMrc_kurs().equals("02"))rekening2.setMrc_no_ac_lama("DOLLAR");
				if(rekening.getMrc_kurs().equals("01"))rekening2.setMrc_no_ac("RUPIAH");
				if(rekening.getMrc_kurs().equals("02"))rekening2.setMrc_no_ac("DOLLAR");					
				
				rekening.setMrc_no_ac_lama(FormatString.upper(rekening2.getMrc_no_ac_lama()));
				rekening.setMrc_no_ac(FormatString.upper(rekening2.getMrc_no_ac()));
			}else{
				rekening.setMrc_no_ac(FormatString.upper(rekening.getMrc_no_ac()));
				rekening.setMrc_no_ac_lama(FormatString.upper(rekening.getMrc_no_ac_lama()));
			}
			
	     }
		this.uwDao.insertHistoryRekeningNasabah(rekening);
	}

	public void updateUlink(int flagPrint, String spaj, String tglTrans) {
		this.uwDao.updateMst_ulink(flagPrint, spaj, tglTrans);
		this.uwDao.updateMst_trans_ulink(flagPrint, spaj, tglTrans);
	}
	public Integer validationAlreadyPaid(String spaj, int tahun, int premi){
		return this.uwDao.validationAlreadyPaid(spaj, tahun, premi);
	}

	public boolean validationIsTitipanPremi(String payment_id){
		return this.uwDao.validationIsTitipanPremi(payment_id);
	}

	public Integer validationDailyCurrency(String lku_id, Date rk_date){
		return this.uwDao.validationDailyCurrency(lku_id, rk_date);
	}

	public Integer validationNAB(String spaj) {
		return this.uwDao.validationNAB(spaj);
	}

	public Integer validationPositionSPAJ(String spaj){
		return this.uwDao.validationPositionSPAJ(spaj);
	} 

	public Map validationPrintPolis(String spaj) {
		return this.uwDao.validationPrintPolis(spaj);
	}
	
	public Integer validationTopupBerkala(String spaj){
		return this.uwDao.validationTopup(spaj);
	}

	public Map validationVerify(int lv_id){
		return this.uwDao.validationVerify(lv_id);
	}

	public String savePayment(TopUp topup, int aplikasi, User currentUser, Integer i_flagCC) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
        return this.uwDao.savePayment(topup, aplikasi, currentUser, i_flagCC);        
//		if(topup.getMspa_payment_id() == null) topup.setMspa_payment_id("");
//
//		List kurs = bacDao.selectnilaikurs("02", defaultDateFormat.format(topup.getMspa_date_book()));
//		Date dateNow = selectSysdate1("dd", false, 0);
//		if(topup.getBill_lku_id().equals(topup.getLku_id())) topup.setMspa_nilai_kurs(new Double(1));
//		else topup.setMspa_nilai_kurs((Double) ((Map) kurs.get(0)).get("LKH_KURS_JUAL"));
//		
//		String tempTahunPremiKe = topup.getJenis();
//		
//		if( tempTahunPremiKe!= null && !"".equals( tempTahunPremiKe ) )
//		{
//			
//		}
//		else
//		{
//			tempTahunPremiKe = "1@1";
//		}
//		String tahunKe = tempTahunPremiKe.substring(0,tempTahunPremiKe.indexOf("@"));
//		String premiKe = tempTahunPremiKe.substring(tempTahunPremiKe.indexOf("@")+1, tempTahunPremiKe.length());
//
//		if(topup.getMspa_payment_id().equals("")){
//			topup.setMspa_active(new Integer(1));
//			topup=this.uwDao.insertTopUp(topup, aplikasi, currentUser);
//		}else {
//			this.uwDao.updateMst_payment(topup, currentUser);
//			this.uwDao.updateMst_tag_payment(topup, topup.getTahun_ke().intValue(), topup.getPremi_ke().intValue());
//			this.uwDao.updateBilling(topup, topup.getTahun_ke().intValue(), topup.getPremi_ke().intValue());
//		}
//		List mstDrekBasedNoTrx = uwDao.selectMstDrekBasedNoTrx(topup.getNo_trx());
//		Map viewMstDrekDetail=(Map) mstDrekBasedNoTrx.get(0);
//		String norek_ajs = (String) viewMstDrekDetail.get("NOREK_AJS");
//		String jenis = (String) viewMstDrekDetail.get("JENIS");
//		Date tgl_trx = (Date) viewMstDrekDetail.get("TGL_TRX");
//		String no_pre;
//		if( viewMstDrekDetail.get("NO_PRE") != null){
//			no_pre = (String) viewMstDrekDetail.get("NO_PRE");
//			topup.setNo_pre(no_pre);
//		}
//		if("insert".equals(topup.getActionTypeForDrekDet())){
//			if( topup.getNo_trx()!= null && !"".equals(topup.getNo_trx()))
//			{
//				String noUrut = uwDao.selectMaxNoUrutMstDrekDet( topup.getNo_trx() ) ;
//				if( noUrut != null && !"".equals( noUrut ) )
//				{
//					
//				}
//				else
//				{  
//					noUrut = "0";
//				}
//				this.uwDao.updateMst_payment(topup, currentUser);
//				uwDao.insertMstDrekDet(topup.getNo_trx(), tahunKe, premiKe, topup.getMspa_payment(), LazyConverter.toInt(noUrut) + 1, topup.getReg_spaj(), null, topup.getMspa_payment_id(),currentUser.getLus_id(), dateNow, topup.getNo_pre(), norek_ajs, jenis, tgl_trx);
//			}
//		}
//		else if("update".equals(topup.getActionTypeForDrekDet())){
//			List<DrekDet> mstDrekDetBasedSpaj = uwDao.selectMstDrekDet( null, topup.getReg_spaj(), null, null );
//			List<Payment> payment = uwDao.selectPaymentCount(topup.getReg_spaj(), 1, 1);
//			if(payment.get(0).getLsrek_id()!=0){
//				if(mstDrekDetBasedSpaj.size()==0){
//					String noUrut = uwDao.selectMaxNoUrutMstDrekDet( topup.getNo_trx() ) ;
//					if( noUrut != null && !"".equals( noUrut ) )
//					{
//						
//					}
//					else
//					{  
//						noUrut = "0";
//					}
//					uwDao.insertMstDrekDet(topup.getNo_trx(), tahunKe, premiKe, topup.getMspa_payment(), LazyConverter.toInt(noUrut) + 1, topup.getReg_spaj(), null, topup.getMspa_payment_id(),currentUser.getLus_id(), dateNow, topup.getNo_pre(), norek_ajs, jenis, tgl_trx);
//				}
//				else{
//					uwDao.updateMstDrekDet(tahunKe, premiKe, topup.getMspa_payment(), topup.getNo_trx(), 
//							topup.getReg_spaj(), null, topup.getMspa_payment_id(), currentUser.getLus_id(),  dateNow, jenis, topup.getNo_pre());
//				}
//			}
//		}
//		
//		//tarik data dari tabel MST_DREK, trus diupdate juga datanya
//		if(topup.getNo_trx() != null) {
//			if(!topup.getNo_trx().trim().equals("")) {
//				Integer flagTunggalGabungan = null;
//				if( "Tunggal".equals( topup.getTipe() ) )
//					{ flagTunggalGabungan = 0; }
//				else if( "Gabungan".equals( topup.getTipe() ) )
//					{ flagTunggalGabungan = 1; }
//				uwDao.updateMstDrek(topup.getReg_spaj(), currentUser.getLus_id(), topup.getNo_trx(),flagTunggalGabungan, jenis);
//				
//			}
//		}
//		
//		//MANTA
//        if(i_flagCC == 1) bacManager.updateMstDrekCc(topup.getMspa_no_rek(), topup.getMspa_payment().intValue());
//		
//		return "Data pembayaran berhasil disimpan.";
	}
	
	public void updateMstDrekRecheck(String[] daftar_trx) {
		for(String no_trx : daftar_trx) {
			this.uwDao.updateMstDrekRecheck(no_trx);
		}
	}
	public void updateMstProSaveBayar(String daftar_bayar_id, int daftar_jenis){
//		int ctt = 0;
//		for(String bayar_id : daftar_bayar_id){
			// proses daftar bayar 
			// daftar jenis -> daftar_jenis[ctt]
			this.uwDao.updateMstProSaveBayar(daftar_bayar_id, daftar_jenis);
//			ctt++;
//		}
	}
	
	public void updateMstDrekEdit(String dftno_trx, String lus_id, String reg_spaj){
		this.uwDao.updateMstDrekEdit(reg_spaj, lus_id, dftno_trx);
	}
	public void updateMscoTdkAdaKomisi(String spaj){
		this.uwDao.updateMscoTdkAdaKomisi(spaj);
	}
	public void updateMscoAktifKomisi(String spaj){
		this.uwDao.updateMscoAktifKomisi(spaj);
	}
	public void updateDrekKosongkanSpaj(String dftno_trx, String lus_id, String reg_spaj){
		this.uwDao.updateDrekKosongkanSpaj(reg_spaj, lus_id, dftno_trx);
	}
	public List selectUwInfo(String showTabel, String spaj) {
		return this.uwDao.selectUwInfo(showTabel, spaj);		
	}
	
	public List selectFilterSpaj(String tipe, String kata, String tglLahir, String pilter) {
		return this.uwDao.selectFilterSpaj(tipe, kata, tglLahir, pilter);
	}
	
	public List selectTransDobleTU(String spaj,Date msl_bdate, Double msl_premi) {
		return this.bacDao.selectTransDobleTU(spaj, msl_bdate, msl_premi);
	}

	public List selectFilterSpaj2(String posisi, String tipe, String kata, String pilter, String lssaId,String lsspId, String tgl_lahir, Map param) {
		return this.uwDao.selectFilterSpaj2(posisi, tipe, kata, pilter,lssaId,lsspId, tgl_lahir, param);
	}		
	
	public List selectFilterSpaj2_valid(String posisi, String tipe, String kata, String pilter, String lssaId,Integer lus_id,String lsspId, String tgl_lahir, Map param) {
		return this.uwDao.selectFilterSpaj2_valid(posisi, tipe, kata, pilter,lssaId,lus_id,lsspId, tgl_lahir, param);
	}		
	
	public List selectFilterSpaj2SimasPrima(String posisi, String tipe, String kata, String pilter, Integer jn_bank, String tgl_lahir, Map param ) {
		return this.uwDao.selectFilterSpaj2SimasPrima(posisi, tipe, kata, pilter, jn_bank, tgl_lahir, param );
	}	
	
	public List selectFilterOtorisasi(String tipe, String kata, String pilter, Integer jn_bank) {
		return this.uwDao.selectFilterOtorisasi(tipe, kata, pilter, jn_bank);
	}
	
	public List selectFilteragency( String tipe, String kata, String pilter) {
		return this.uwDao.selectFilteragency(tipe, kata, pilter);
	}
	
	public List selectgutri(int posisi, String tipe, String kata, String pilter) {
		return this.uwDao.selectgutri(posisi, tipe, kata, pilter);
	}
	
	public List selectgutriblacklist(int posisi, String tipe, String kata, String cari, String pilter, String tgl_lahir, String telp, String sumber) {
		return this.uwDao.selectgutriblacklist(posisi, tipe, kata, cari, pilter, tgl_lahir, telp, sumber);
	}
	
	public List selectgutri_valid(int posisi, String tipe, String kata, String pilter,Integer lus_id) {
		return this.uwDao.selectgutri_valid(posisi, tipe, kata, pilter,lus_id);
	}
	
	public List selectFilterSpaj3(int posisi, String tipe, String kata, String pilter) {
		return this.uwDao.selectFilterSpaj3(posisi, tipe, kata, pilter);
	}
	
	public List selectFilterSpaj4(String tipe, String kata, String pilter) {
		return this.uwDao.selectFilterSpaj4( tipe, kata, pilter);
	}

	public void prosesBackToBac(String spaj,String lus_id,int lspd,int lssp,int lssa,String desc){
		uwDao.insertMstPositionSpaj(lus_id,desc,spaj, 0);
		uwDao.updateMstPolicy(spaj,lspd);
		uwDao.updateMstInsured(spaj,lspd);
	}

	public void updateRider(List lsRider,int iIns){
		this.uwDao.updateRider(lsRider, iIns);
	}

	public String cekAgenTakBerpolis(String spaj) {
		return this.uwDao.cekAgenTakBerpolis(spaj);
	}

	public List selectBillingInformation(String spaj, int tahun, int premi){
		return this.uwDao.selectBillingInformation(spaj, tahun, premi);
	}

	public List<String> saveCsfCall(String spaj, String inout, String lus_id, 
			String[] s_dial, String[] s_jenis, String[] s_ket, String[] s_start, String[] s_end, String[] s_callback) {
		return this.uwDao.saveCsfCall(spaj, inout, lus_id, s_dial, s_jenis, s_ket, s_start, s_end, s_callback);
	}
	
	public List selectProductInsured2(String spaj,BigDecimal insuredNo,Integer active){
		return this.uwDao.selectProductInsured2(spaj,insuredNo,active);
	}

	public List selectSimultanDetail(String value){
		return uwDao.selectSimultanDetail(value);
	}

	/**
	 * Fungsi:	Untuk Menampilkan detil agen
	 * @param 	String msag_id
	 * @return	List
	 * @author 	Hemilda Sari Dewi
	 */
	public Agen selectdetilagen(String msag_id)
	{
		return (Agen)uwDao.selectdetilagen(msag_id);
	}
	
	public List selectSimultan(Map map){
		return uwDao.selectSimultan(map);
	}
	public List selectSimultanNew(Map map){
		return uwDao.selectSimultanNew(map);
	}
	/**
	 * Fungsi:	Untuk Menampilkan Simultan Polis berdasarkan nama dan tanggal lahir tertanggung
	 * @param 	String nama,String tglLahir
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectViewSimultan(String nama,String tglLahir){
		return uwDao.selectViewSimultan(nama,tglLahir);
	}

	/**Fungsi:	Untuk menampilkan posisi Spaj dan kode posisi spaj tersebut
	 * @param 	String spaj
	 * @return	Map
	 * @author	Ferry Harlim
	 */
	public Map selectCheckPosisi(String spaj){
		return uwDao.selectCheckPosisi(spaj);
	}

	public List selectAllLstIdentity(){
		return uwDao.selectAllLstIdentity();
	}
	
	public Integer prosesSimultan(int proses,String spaj,String lcaId,String lsClientPpOld,String lsClientTtOld)throws Exception{
		return uwDao.prosesSimultan(proses, spaj, lcaId, lsClientPpOld, lsClientTtOld);
	}

	public Integer prosesSimultanNew(Command command,String lsClientPpOld,String lsClientTtOld,String idSimultanPp,String idSimultanTt)throws Exception{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return simultan.prosesSimultan(command,lsClientPpOld, lsClientTtOld,idSimultanPp,idSimultanTt);
		//Untuk Proses ulang simultan
//		return simultan.prosesSimultanUlang(command, lsClientPpOld, lsClientTtOld, idSimultanPp, idSimultanTt);
		
	}
	
	public Double selectLstMonthlyKurs(String kurs){
		return uwDao.selectLstMonthlyKurs(kurs);
	}
	
	public Double selectCekPremi(Double ldec_kurs,String mclId,String lkuId,Integer value){
		return uwDao.selectCekPremi(ldec_kurs,mclId,lkuId,value);
	}
	
	public Double selectTahunKe(String asTgl,String asToday){
		Double thKe;
		thKe=uwDao.selectTahunKe(asTgl,asToday);
		if(thKe.intValue()<1)
			thKe=new Double(1);
		return thKe;
	}
	
	public Integer countsummaryinput(String id, String tgl1, String tgl2){
		return uwDao.countsummaryinput(id, tgl1, tgl2);
	}
	
	public List selectLaporanJamDua(List spaj, int jamdua) {
		return uwDao.selectLaporanJamDua(spaj, jamdua);
	}
	
	public Integer countsummaryinputguthrie( String tgl1, String tgl2){
		return uwDao.countsummaryinputguthrie( tgl1, tgl2);
	}
	
	public Integer selectLstReinsDesc1 (String as_kurs,int ai_bisnis_id,int ai_bisnis_no){
	return uwDao.selectLstReinsDesc1(as_kurs,ai_bisnis_id,ai_bisnis_no);
	}
	
	public Double selectLstTableAwal(int ai_type,int ai_bisnis_id,String as_kurs,int ai_cbayar,int ai_lbayar,int ai_ltanggung,int ai_year,int ai_age){
		return uwDao.selectLstTableAwal(ai_type,ai_bisnis_id,as_kurs,ai_cbayar,ai_lbayar,ai_ltanggung,ai_year,ai_age);
	}
	//proses investasi
	public List selectMstUlink(String spaj,Integer muKe){
		return uwDao.selectMstUlink(spaj,muKe);
	}
	
	public List selectMstDetUlink(String spaj,Integer muKe,Integer mduAktif){
		return uwDao.selectMstDetUlink(spaj,muKe,mduAktif);
	}
	
	public List selectMstBiayaUlink(String spaj,Integer muKe){
		return uwDao.selectMstBiayaUlink(spaj,muKe);
	}
	
	public void updateMstPolicyUnderTable(String spaj,double ldecBonus){
		uwDao.updateMstPolicyUnderTable(spaj,ldecBonus);
	}
	
	public Double selectMstPolicyUnderTable(String spaj){
		return uwDao.selectMstPolicyUnderTable(spaj);
	}

	public Date selectMaxLstBunga(String kurs,Integer jns){
		return uwDao.selectMaxLstBunga(kurs,jns);
	}
	
	public Double selectLstBungaLsbunBunga(String kurs,Integer jns,Date tgl){
		return uwDao.selectLstBungaLsbunBunga(kurs,jns,tgl);
	}
	
	public Double selectMstDefaultMsdefNumeric(Integer li_id){
		return uwDao.selectMstDefaultMsdefNumeric(li_id);
	}
	
	public Double selectLstTableLstabValue(Integer jenis, Integer li_bisnis, String kurs, Integer pmode,
			Integer pperiod,Integer insPer, Integer tahunKe, Integer umurTt){
		return uwDao.selectLstTableLstabValue(jenis,li_bisnis,kurs,pmode,pperiod,insPer,tahunKe,umurTt);
	}
	
	public List selectMstProductInsuredRider(String spaj,Integer insured,Integer active){
		return uwDao.selectMstProductInsuredRider(spaj,insured,active);
	}	
	
	public Integer selectLstJenisBiayaLjbId(Integer lsbsId,Integer lsdbsNumber){
		return uwDao.selectLstJenisBiayaLjbId(lsbsId,lsdbsNumber);
	}
	
	public List selectLstJenisInvest(){
		return uwDao.selectLstJenisInvest();
	}
	/**@Fungsi:	Untuk mengecek status dari spaj
	 * @param	String spaj,Integer insured
	 * @return 	Map
	 * @author 	Ferry Harlim
	 **/
	public Map selectWfGetStatus(String spaj,Integer insured){
		return uwDao.selectWfGetStatus(spaj,insured);
	}
	
	public List selectd_mst_prod_ins(String spaj,Integer insNo){
		return uwDao.selectd_mst_prod_ins(spaj,insNo);
	}
	/**@Fungsi:	Untuk Menampilkan data pemegang polis secara detail
	 * @param	String spaj,Integer lspdId,Integer lstbId
	 * @return 	com.ekalife.elions.web.Model.Policy
	 * @author 	Ferry Harlim
	 * */

	public Policy selectDw1Underwriting(String spaj,Integer lspdId,Integer lstbId){
		return uwDao.selectDw1Underwriting(spaj,lspdId,lstbId);
	}
	
	/**@Fungsi : Untuk menampilkan kolom MSTE_BACKUP pada table EKA.MST_INSURED
	 * @param	String spaj,Integer insuredNo
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 **/
	public Integer selectMstInsuredMsteBackup(String spaj,Integer insuredNo){
		return (Integer)uwDao.selectMstInsuredMsteBackup(spaj,insuredNo);
	}
	/**Fungsi	Untuk menampilkan jumlah persen investasi produk link pada tabel EKA.MST_DET_ULINK
	 * @param 	String spaj
	 * @return	Double
	 * @author	Ferry Harlim
	 */
	public Double selectSumPersenMstDetUlink(String spaj){
		return uwDao.selectSumPersenMstDetUlink(spaj);
	}
	/**Fungsi	Untuk Menampilkan jumlah premi pada tabel EKA.MST_ULINK
	 * @param 	String spaj
	 * @return	Double
	 * @author	Ferry Harlim
	 */
	public Double selectSumJlhPremiMstUlink(String spaj){
		return uwDao.selectSumJlhPremiMstUlink(spaj);
	}
	/**@Fungsi:	Untuk Menampilkan Data pembayaran awal/titipan premi premi pada tabel EKA.MST_DEPOSIT_PREMIUM
	 * @param	String spaj,Integer flag	
	 * @return 	List
	 * @author 	Ferry Harlim
	 * */
	public List selectMstDepositPremium(String spaj,Integer flag){
		return uwDao.selectMstDepositPremium(spaj,flag);
	}
	
	public Integer selectMstSampleUwStatusBatal(String spaj,String mclId){
		return uwDao.selectMstSampleUwStatusBatal(spaj,mclId);
	}
	
	public Date selectMstDefaultMsdefdate(Integer id){
		return uwDao.selectMstDefaultMsdefdate(id);
	}
	
	public List selectLstSampleUwTglSample(){
		return uwDao.selectLstSampleUwTglSample();
	}

	/**@Fungsi:	sebagai manager atau pengatur dari proses Akseptasi dan Fund Alocation
	 * @param 	Akseptasi akseptasi,int thn,int bln,String desc,BindException err 
	 * @return 	int
	 * @author 	Ferry Harlim
	 * */
	public int prosesAkseptasi(Akseptasi akseptasi,int thn,int bln,String desc,BindException err ){
	    //TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String lca_id = uwDao.selectCabangFromSpaj(akseptasi.getSpaj());
		//if(!lca_id.equals("09")){ Deddy(8 Juli 2015) - Simpol BSIM sudah menjual Smile Medical Provider, jadi tidak perlu dijaga lca_id 09 lagi.
			if(akseptasi.getLiAksep()==5 || akseptasi.getLiAksep()==10){
				Integer mspo_provider= uwDao.selectGetMspoProvider(akseptasi.getSpaj());
				if(mspo_provider==2){
					uwDao.updateMstInsuredKirimAdmedika(akseptasi.getSpaj(),akseptasi.getInsuredNo(),akseptasi.getCurrentUser().getLus_id());
					uwDao.saveMstTransHistory(akseptasi.getSpaj(), "tgl_kirim_admedika", null, null, null);
				}
				uwManager.prosesEndorsemen(akseptasi.getSpaj(), Integer.parseInt(uwDao.selectBusinessId(akseptasi.getSpaj())),0);
			}
//		}	
		
		desc = getKodeStatusAksep(akseptasi.getLiAksep())+desc;
		int tmp = uwDao.prosesAkseptasi(akseptasi,thn,bln,desc,err);
		
		if((akseptasi.getLiAksep()==5 || akseptasi.getLiAksep()==10) && "37,52,63".indexOf(lca_id)>-1){
			uwManager.prosesEmailSMSAkseptasi(akseptasi.getSpaj(), akseptasi.getLiAksep());
		}
		
		//Anta - Khusus untuk Program Stable Save Hadiah
		//16/10/2012
		//
		String hdspaj = akseptasi.getSpaj();
		Product produk = uwDao.selectMstProductInsuredUtamaFromSpaj(hdspaj);
		if(produk.getLsbs_id()==184){
			Tertanggung ttg = selectttg(hdspaj);
			int mgi = uwDao.selectMasaGaransiInvestasi(hdspaj,1,1);
			double pr = produk.getMspr_premium();
			long premi =  (long) pr;
			//if((akseptasi.getLiAksep()==5 || akseptasi.getLiAksep()==10) && ttg.getMste_flag_hadiah()==1 && premi >= 30000000 && mgi==36){
			if((akseptasi.getLiAksep()==5 || akseptasi.getLiAksep()==10)){
				//List<Map> hist = uwManager.selectHadiahHist(hdspaj, 1);
				//if(hist.size()<1){
					AddressBilling addrBill = selectAddressBilling(hdspaj);
					Pemegang pemegang = selectpp(hdspaj);
					Agen agen = select_detilagen(hdspaj);
					/*Hadiah hadiah = new Hadiah();
					hadiah.setLhc_id(8);
					hadiah.setMh_quantity(1);
					hadiah.setProgram_hadiah(1);
					hadiah.setReg_spaj(pemegang.getReg_spaj());
					hadiah.setMh_alamat(pemegang.getAlamat_rumah());
					hadiah.setMh_kota(pemegang.getKota_rumah());
					hadiah.setMh_kodepos(pemegang.getKd_pos_rumah());
					hadiah.setMh_telepon(pemegang.getTelpon_rumah());
					
					if(premi < 50000000){
						hadiah.setLh_id(12);
					}else if(premi >= 50000000 && premi < 60000000){
						hadiah.setLh_id(11);
					}else if(premi >= 60000000 && premi < 65000000){
						hadiah.setLh_id(9);
					}else if(premi >= 65000000 && premi < 75000000){
						hadiah.setLh_id(8);	
					}else if(premi >= 75000000 && premi < 80000000){
						hadiah.setLh_id(7);	
					}else if(premi >= 80000000 && premi < 90000000){
						hadiah.setLh_id(4);	
					}else if(premi >= 90000000 && premi < 125000000){
						hadiah.setLh_id(3);	
					}else if(premi >= 125000000 && premi < 250000000){
						hadiah.setLh_id(2);	
					}else if(premi >= 250000000){
						hadiah.setLh_id(1);	
					}*/
					
					String sts = "";
					if(akseptasi.getLiAksep()==5){
						sts = "Aksep";
					}else{
						sts = "Akseptasi Khusus";
					}
					String team_name = uwDao.selectBancassTeam(hdspaj);
					if(team_name==null){
						team_name= "";
					}
					String cb = "";
					if("37,52".indexOf(lca_id)>-1) { //Agency
					       cb = "Agency";
					}else if(lca_id.equals("46")) { //Hybrid
					       cb = "Hybrid";
					}else if(lca_id.equals("09")) { //Bancassurance
					       if(team_name.toUpperCase().equals("TEAM JAN ROSADI")) { //Bancassurance2
					    	   	  cb = "Bancassurance 2";
					       }else if(team_name.toUpperCase().equals("TEAM DEWI")) { //Bancassurance3
					              cb = "Bancassurance 3";
					       }else if(team_name.toUpperCase().equals("TEAM YANTI SUMIRKAN")) { //Bancassurance1
					              cb = "Bancassurance 1";
					       }else{
					    	      cb = "Bancassurance";
					       }
					}else if(lca_id.equals("08") || lca_id.equals("42")) { //Worksite
					       cb = "Worksite";
					}else if(lca_id.equals("55")) { //DM/TM
					       cb = "DMTM";
					}else if(lca_id.equals("58")) { //MallAssurance
					       cb = "MallAssurance";
					}else { //Regional
					       cb = "Regional";
					}
					
					//Hadiah jn_hadiah= basDao.selectLstHadiah(hadiah.getLhc_id(),hadiah.getLh_id());
					
					//Canpri - Update hadiah, bukan insert lagi
					List<Hadiah> l_hadiah = basDao.selectHadiah(pemegang.getReg_spaj());
					String get_hadiah = "";
					
					if(!l_hadiah.isEmpty()){
						if(l_hadiah.size()==1){
							Hadiah sel_hadiah = new Hadiah();
							sel_hadiah = l_hadiah.get(0);
							
							get_hadiah += sel_hadiah.mh_quantity+" unit "+sel_hadiah.lh_nama;
						}else{
							for(int i=0;i<l_hadiah.size();i++){
								Hadiah sel_hadiah = new Hadiah();
								sel_hadiah = l_hadiah.get(i);
								
								if(i==l_hadiah.size()-1){
									get_hadiah += " dan "+sel_hadiah.mh_quantity+" unit "+sel_hadiah.lh_nama;
								}else if(i>0 && i<l_hadiah.size()-1){
									get_hadiah += ", "+sel_hadiah.mh_quantity+" unit "+sel_hadiah.lh_nama;
								}else{
									get_hadiah += sel_hadiah.mh_quantity+" unit "+sel_hadiah.lh_nama;
								}
							}
						}
						
						for(int i=0;i<l_hadiah.size();i++){
							Hadiah s_hadiah = new Hadiah();
							s_hadiah = l_hadiah.get(i);
							
							s_hadiah.setLspd_id(91);
							basDao.saveHadiahBAC(s_hadiah);
						}
						//end
						
						User currentUser = akseptasi.getCurrentUser();
						String kdpos = "-";
						if(addrBill.getMsap_zip_code()!=null) kdpos = addrBill.getMsap_zip_code();
						
						StringBuffer pesan = new StringBuffer();
						//Format isi email
						pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
						pesan.append("<body><b>PRODUK STABLE SAVE BERHADIAH</b><br><br>");
						pesan.append("Mohon dilakukan pembelian hadiah untuk produk Stable Save berikut ini :<br>");
						pesan.append("Nama PP\t\t: "+ pemegang.getMcl_first() +"<br>");
						pesan.append("No.Polis\t\t: "+ pemegang.getMspo_policy_no() +"<br></p>");
						pesan.append("Status Polis\t: " + sts + " oleh Underwriting<br><br>");
						pesan.append("Alamat\t\t: " + addrBill.getMsap_address() + "<br>");
						pesan.append("     \t\t  " + addrBill.getKota_tagih() + " ("+ kdpos +")<br>");
						pesan.append("No.Telp\t\t: (" + addrBill.getMsap_area_code1() + ") " + addrBill.getMsap_phone1() + "<br>");
						pesan.append("No.HP\t\t: "+ pemegang.getNo_hp() + "<br><br>");
						pesan.append("Premi\t\t: Rp. " + FormatString.formatCurrency("", new BigDecimal(pr))+"<br>");
						//pesan.append("Hadiah\t\t: 1 Unit " + jn_hadiah.getLh_nama() + "<br>");
						pesan.append("Hadiah\t\t: " + get_hadiah + "<br>");
						pesan.append("Cabang\t\t: " + cb + "<br>");
						pesan.append("Penutup\t\t: " + agen.getMcl_first() + "<br><br><br>");
						pesan.append("Email ini terkirim otomatis oleh System E-Lions, mohon jangan lakukan Reply/Forward terhadap email yang dikirim ini.");
						
						String from = props.getProperty("admin.ajsjava");
						//String emailto = "antasari@sinarmasmsiglife.co.id";
						String emailto = "Saphry@sinarmasmsiglife.co.id;virgin@sinarmasmsiglife.co.id;FebiP@sinarmasmsiglife.co.id;meidytumewu@sinarmasmsiglife.co.id;lucianna@sinarmasmsiglife.co.id";
						
						String emailcc6 = "";
							if(cb.equals("Bancassurance 1")){
								emailcc6 = "yantisumirkan@sinarmasmsiglife.co.id;Tities@sinarmasmsiglife.co.id";
							}else if(cb.equals("DMTM")){
								emailcc6 = "Gideon@sinarmasmsiglife.co.id;Mega@sinarmasmsiglife.co.id;ningrum@sinarmasmsiglife.co.id";
							}else if(cb.equals("MallAssurance")){
								emailcc6 = "Gideon@sinarmasmsiglife.co.id;Lylianty@sinarmasmsiglife.co.id;ningrum@sinarmasmsiglife.co.id";
							}else if(cb.equals("Worksite")){
								emailcc6 = "Martono@sinarmasmsiglife.co.id;Edhy@sinarmasmsiglife.co.id";
							}
						String emailcc5 = "";
							if(agen.getLeader_email()!=null) emailcc5 = agen.getLeader_email() + ";";
						String emailcc4 = "";
							if(agen.getMspe_email()!=null) emailcc4 = agen.getMspe_email() + ";";
						String emailcc3 = "";
							if(currentUser.getEmail()!=null)emailcc3 = currentUser.getEmail() + ";";
						String emailcc2 = "";
							if("09,44,45,58,40,59".indexOf(lca_id)>-1){
								emailcc2 = "Inge@sinarmasmsiglife.co.id;";
							}else{
								emailcc2 = "Sisti@sinarmasmsiglife.co.id;";
							}
						String emailcc1 = "Jelita@sinarmasmsiglife.co.id;Hisar@sinarmasmsiglife.co.id;Sisti@sinarmasmsiglife.co.id;";
						
						String emailcc = emailcc1+emailcc2+emailcc3+emailcc4+emailcc5+emailcc6;
						String emailbcc = "antasari@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id";
						
						if(!emailto.trim().equals("")){	
							String[] to = emailto.split(";");
							String[] cc = emailcc.split(";");
							String[] bcc = emailbcc.split(";");
							
							EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
									null, 0, 0, new Date(), null, 
									true, from, to, cc, bcc,
									"Permintaan Pembelian Barang Untuk Polis Stable Save Atas Nama " + pemegang.getMcl_first() + " Dengan No.Polis " + pemegang.getMspo_policy_no(),
									pesan.toString(),
									null, pemegang.getReg_spaj());
							
							//untuk tes
							/*email.send(true, from, new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null,
									"Permintaan Pembelian Barang Untuk Polis Stable Save Atas Nama " + pemegang.getMcl_first() + " Dengan No.Polis " + pemegang.getMspo_policy_no(),
									pesan.toString(),
									null);*/
						}
					}
					
					
				//}
			}
		}
		//Anta - End Proses
		return tmp;
	}
	
	
	public Integer selectCountLstSampleUw(){
		return uwDao.selectCountLstSampleUw();
	}
	
	public Integer selectCountMstSampleUw(){
		return uwDao.selectCountMstSampleUw();
	}
	
	public List selectDSampleUwRegion(int thn,int bln,String lcaId){
		return uwDao.selectDSampleUwRegion(thn,bln,lcaId);
	}
	/**Fungsi:	Untuk menampilkan jumlah data/record yang terdapat pada EKA.MST_POWERSAVE_PROSES
	 * @param 	String spaj
	 * @return	Long
	 * @author	Ferry Harlim
	 */
	public Long selectCountMstPowerSave(String spaj){
		return (Long)uwDao.selectCountMstPowerSave(spaj);
	}
	
	public ParameterClass selectMstPowersaveDpDate(String spaj){
		return uwDao.selectMstPowersaveDpDate(spaj);
	}
	
	public Client selectAllClientNew(String spaj,int flag){
		return uwDao.selectAllClientNew(spaj,flag);
	}
	
	public Client selectAllClientBlacklist(String mcl_id){
		return uwDao.selectAllClientBlacklist(mcl_id);
	}
	
	public String selectCekBlacklist(String reg_spaj){
		return uwDao.selectCekBlacklist(reg_spaj);
	}
	
	public String selectCekBlacklistDirect(String lbl_nama, String lbl_tgl_lahir){
		return uwDao.selectCekBlacklistDirect(lbl_nama, lbl_tgl_lahir);
	}
	
	public BlackList selectAllBlacklist(String lbl_nama, Date lbl_tgl_lahir, String mcl_id){
		return uwDao.selectAllBlacklist(lbl_nama, lbl_tgl_lahir, mcl_id);
	}
	
	public List selectAllDetBlacklist(String lbl_id){
		return uwDao.selectAllDetBlacklist(lbl_id);
	}
	
//	public List selectAllDetBlacklist(String lbl_id){
//		return uwDao.selectAllDetBlacklist(lbl_id);
//	}
	
	public List selectAllBlacklistFamily(int lbl_id){
		return uwDao.selectAllBlacklistFamily(lbl_id);
	}
	
	public AddressNew selectAllAddressNew(String mcl_id) {
		AddressNew result = uwDao.selectAllAddressNew(mcl_id); 
		return result==null?(new AddressNew()):result;
	}
	
	public AddressNew selectAllAddressNew(String spaj,int flag){
		return uwDao.selectAllAddressNew(spaj,flag);
	}
	
	public List selectAllLstAgama(){
		return uwDao.selectAllLstAgama();
	}
	
	public List selectAllLstTolak(){
		return uwDao.selectAllLstTolak();
	}
		
	public List selectAllLstEducation(){
		return uwDao.selectAllLstEducation();
	}
		
	public List selectAllLstGrpJob(){
		return uwDao.selectAllLstGrpJob();
	}
		
	public List selectAllLstJabatan(){
		return uwDao.selectAllLstJabatan();
	}
		
	public List selectAllLstNegara(){
		return uwDao.selectAllLstNegara();
	}
	
	public Map selectDataUsulanDetail(String spaj){
		return bacDao.selectDataUsulanDetail(spaj);
	}
	
	public List selectAllLstKurs(){
		return bacDao.selectAllLstKurs();
	}
	
	public List selectAllLstPayMode(){
		return bacDao.selectAllLstPayMode();
	}
	/**@Fungsi:	Untuk menampilkan jumlah data dari tabel EKA.MST_MEDICAL
	 * @param	String spaj,Integer insuredNo
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 * */
	public Integer selectCountMstMedical(String spaj,Integer insuredNo){
		return uwDao.selectCountMstMedical(spaj,insuredNo);
	}
	/**@Fungsi:	Untuk menampilkan data dari tabel EKA.MST_PRODUCT_INSURED PA=800
	 * @param	String spaj,Integer insruedNo,Integer Pa
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 * */
	public Integer selectMstProductInsuredPa(String spaj,Integer insruedNo,Integer Pa){
		return uwDao.selectMstProductInsuredPa(spaj,insruedNo,Pa);
	}
	
	public Integer selectMstProductInsuredMsprClass(String spaj,Integer insured,Integer lsbsId){
		return uwDao.selectMstProductInsuredMsprClass(spaj,insured,lsbsId);
	}
	
	public void prosesDecline(String spaj,String msteInsured,Integer insuredNo, String lusId, Integer lspdId,Integer lsspId,Integer lssaId,Integer lstbId,String desc){
		uwDao.prosesDecline(spaj,msteInsured,insuredNo,lusId,lspdId,lsspId,lssaId,lstbId,desc);
	}
	/**Fungsi:	Untuk menampilkan jumlah biaya pada tabel EKA.MST_BIAYA_ULINK
	 * @param 	String spaj
	 * @return	Double
	 * @author	Ferry Harlim
	 */
	public Double selectCountMstBiayaUlink(String spaj){
		return uwDao.selectCountMstBiayaUlink(spaj);
	}
	/**@Fungsi:	Untuk menampilkan kolom MSTE_STANDARD pada tabel EKA.MST_INSURED
	 * @param	String spaj
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 * */
	public Integer selectCountMstProductInsuredCekStandard(String spaj){
		return uwDao.selectCountMstProductInsuredCekStandard(spaj);
	}
	/**@Fungsi:	Untuk Menampilkan Jumlah data yang terdapat pada tabel EKA.MST_CANCEL
	 * @param	String spaj
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 * */
	public Integer selectCountMstCancel(String spaj){
		return uwDao.selectCountMstCancel(spaj);
	}
	/**@Fungsi:	Untuk menampilkan jumlah data yang terdapat pada tabel EKA.MST_TRANS_ULINK
	 * @param	String spaj, Integer ltId//jenis transaksi  klo null  maka gak di filter
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 * */
	public Integer selectCountMstTransUlink(String spaj,Integer ltId){
		return uwDao.selectCountMstTransUlink(spaj,ltId);
	}
	
	public List selectMstProductInsuredRiderTambahan(String spaj,Integer insured,Integer active){
		return uwDao.selectMstProductInsuredRiderTambahan(spaj,insured,active);
	}
	/**@Fungsi : sebagai manager atau pengatur dari proses transfer
	 * @param 	Transfer transfer,BindException err
	 * @return 	int
	 * @author 	Ferry Harlim
	 * @throws ParseException 
	 * @throws DataAccessException 
	 * */
	public Map prosesTransferPembayaran(Transfer transfer,Integer flagNew,BindException err,HttpServletRequest request) throws DataAccessException, ParseException{
	//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return transferUw.prosesTransferPembayaran(transfer,flagNew,err, request);
	}
	
	public void prosesinsertLossMstReinsProduct(List lsSpaj, User currentUser, BindException err)throws DataAccessException, ParseException{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		transferUw.prosesinsertLossMstReinsProduct(lsSpaj, currentUser, err);
	}
	
	public List selectSpajLostInsertMstReinsProduct()throws DataAccessException{ 
		return uwDao.selectSpajLostInsertMstReinsProduct();
	}
	/*Untuk Proses Insert ke table reins dan reins_product
	 * */
	public int prosesTransferPembayaranReasUlang (Transfer transfer,BindException err) throws NoTransactionException, DataAccessException, ParseException{
		return uwDao.prosesTransferPembayaranReasUlang(transfer,err);
	}

	/**@Fungsi : Untuk menginsert tabel eka.mst_premi_ulink dari kode bisnis tertentu 
	 * 			 yang tidak terinsert.
	 * @param 	
	 * @author 	Ferry Harlim
	 * */
	public void prosesInsertPremiUlinkManual(User currentUser){
		uwDao.prosesInsertPremiUlinkManual(currentUser);
	}
	
	public List selectMSarTemp(String spaj){
		return uwDao.selectMSarTemp(spaj);
	}

	public List selectMSarTemp2(String spaj){
		return uwDao.selectMSarTemp2(spaj);
	}
	
	public List selectMReasTemp(String spaj){
		return uwDao.selectMReasTemp(spaj);
	}
	
	public List selectProductUtama(String spaj){
		return uwDao.selectProductUtama(spaj);
	}
	
	public Integer prosesReasIndividu(String spaj,String msteInsured,String mspoPolicyHolder,
			Integer insured, String lusId,BindException err)throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return uwDao.prosesReas(spaj,msteInsured,mspoPolicyHolder,insured,lusId,err);
	}
	/**Fungsi :	Untuk Proses Reas PT. Guthrie dimana UP di bawah retensi
	 * @param 	String spaj, Double ldec_tsi
	 * @return	Integer
	 * @throws 	Exception
	 * @author 	Ferry Harlim
	 * @since	30-06-2007
	 */
	public Integer prosesReasGuthrie(String spaj,Double ldecTsi, Double retensi,Integer lsbsId,Integer lsdbsNumber,Date begDate)throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Integer liReas=0;
		ReasTemp insReas=new ReasTemp();
		String kurs="01";
		Integer stsPolis=10;
		Integer medical=0;
		Integer insured=1;
		Integer liBackup=3;
		Integer jum=uwDao.selectCountMReasTemp(spaj);
		// delete table m_reas_temp dan sar temp
		if(jum>0){
			uwDao.deleteMReasTemp(spaj);
			uwDao.deleteMSarTemp(spaj);
		}
		//
		insReas.setReg_spaj(spaj);
		insReas.setPemegang(0);
		insReas.setLku_id(kurs);
		insReas.setNil_kurs(uwDao.selectGetKursReins("02", begDate).intValue());
		insReas.setMste_reas(0);
		insReas.setSts_aktif(null);
		insReas.setSimultan_tr_rd(new Double(0));
		insReas.setTsi_tr_rd(new Double(0));
		insReas.setSar_tr_rd(new Double(0));
		insReas.setRetensi_tr_rd(new Double(0));
		insReas.setReas_tr_rd(new Double(0));
		insReas.setSimultan_life(new Double(0));
		insReas.setTsi_life(ldecTsi);
		insReas.setSar_life(ldecTsi);
		insReas.setRetensi_life(retensi);
		insReas.setReas_life(new Double(0));
		insReas.setSimultan_ssp(new Double(0));
		insReas.setTsi_ssp(new Double(0));
		insReas.setSar_ssp(new Double(0));
		insReas.setRetensi_ssp(new Double(0));
		insReas.setReas_ssp(new Double(0));
		insReas.setSimultan_pa_in(new Double(0));
		insReas.setTsi_pa_in(new Double(0));
		insReas.setSar_pa_in(new Double(0));
		insReas.setRetensi_pa_in(new Double(0));
		insReas.setReas_pa_in(new Double(0));
		insReas.setSimultan_pa_rd(new Double(0));
		insReas.setTsi_pa_rd(new Double(0));
		insReas.setSar_pa_rd(new Double(0));
		insReas.setRetensi_pa_rd(new Double(0));
		insReas.setReas_pa_rd(new Double(0));
		insReas.setSimultan_pk_in(new Double(0));
		insReas.setTsi_pk_in(new Double(0));
		insReas.setSar_pk_in(new Double(0));
		insReas.setRetensi_pk_in(new Double(0));
		insReas.setReas_pk_in(new Double(0));
		insReas.setSimultan_pk_rd(new Double(0));
		insReas.setTsi_pk_rd(new Double(0));
		insReas.setSar_pk_rd(new Double(0));
		insReas.setRetensi_pk_rd(new Double(0));
		insReas.setReas_pk_rd(new Double(0));
		insReas.setSimultan_ssh(new Double(0));
		insReas.setTsi_ssh(new Double(0));
		insReas.setSar_ssh(new Double(0));
		insReas.setRetensi_ssh(new Double(0));
		insReas.setReas_ssh(new Double(0));
		insReas.setExtra_mortality(new Double(0));
		insReas.setSimultan_cash(new Double(0));
		insReas.setTsi_cash(new Double(0));
		insReas.setSar_cash(new Double(0));
		insReas.setRetensi_cash(new Double(0));
		insReas.setReas_cash(new Double(0));
		insReas.setSimultan_tpd(new Double(0));
		insReas.setTsi_tpd(new Double(0));
		insReas.setSar_tpd(new Double(0));
		insReas.setRetensi_tpd(new Double(0));
		insReas.setReas_tpd(new Double(0));
		uwDao.insertMReasTemp(insReas);
		uwDao.insertMSarTemp(spaj, null, lsbsId, lsdbsNumber, kurs, ldecTsi, stsPolis, medical, 0,spaj);
		//rider 
		List lsRider = new ArrayList();
		lsRider=uwDao.selectDataUsulanRider(spaj);
		if(lsRider.isEmpty()==false)
			for(int i=0;i<lsRider.size();i++){
				Map param=(HashMap)lsRider.get(i);
				Integer lsbsIdRider=(Integer)param.get("LSBS_ID");
				Integer lsdbsNumberRider=(Integer)param.get("LSDBS_NUMBER");
				Double msprTsiRider=(Double)param.get("MSPR_TSI");
				if(msprTsiRider<retensi)
					uwDao.insertMSarTemp(spaj, null, lsbsIdRider, lsdbsNumberRider, kurs, msprTsiRider, stsPolis, medical, 1+i,spaj);
				else{
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return 1;
					
				}
			}
		uwDao.updateMstInsuredReasnBackup(spaj,insured,liReas,liBackup,null,null);
		return liReas;
	}

	public Map prosesReasIndividuNew(Reas dataReas,BindException err)throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		dataReas.setLstbId(1);
		dataReas.setLspdId(2);
//		return reasUwNew2.prosesReasUnderwriting(dataReas, err);
		return reasIndividu.prosesReasUnderwriting(dataReas, err);
	}
	
	public void prosesUbahReas(String spaj){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		uwDao.updateMstInsuredReasnBackup(spaj,new Integer(1),new Integer(1),new Integer(2),null,null);
		uwDao.updateMReasTempMsteReas(spaj, 1, null);
	}
	
	public void updateMstInsuredMsteStandard(String spaj,Integer insured, Integer msteStandard){
		uwDao.updateMstInsuredMsteStandard(spaj,insured,msteStandard);
	}
	
	public List selectTopTenProduct(){
		return reinstateDao.selectTopTenProduct();
	}
	
	public void prosesBacktoUW(String spaj,String lusId){
		uwDao.insertMstPositionSpaj(lusId, "TRANSFER BALIK KE UW", spaj, 0);
		uwDao.updateMstPolicy(spaj,2);
		uwDao.updateMstInsured(spaj,2);
	}
	
	public String selectEmailAdmin(String spaj){
		return uwDao.selectEmailAdmin(spaj);
	}
	
	public Map selectEmailAgen(String spaj){
		return uwDao.selectEmailAgen(spaj);
	}
	
	public Map selectInfoPemegang(String spaj){
		return uwDao.selectInfoPemegang(spaj);
	}
	
	public List selectNonDeclarationProduct(String lsbsId){
		return reinstateDao.selectNonDeclarationProduct(lsbsId);
	}
	
	public Double selectBiayaMateraiFromBilling(String spaj)throws DataAccessException{
		return reinstateDao.selectBiayaMateraiFromBilling(spaj);
	}
	
	public Date selectmaxMsbiEndDateMstBilling(String spaj)throws DataAccessException{
		return reinstateDao.selectmaxMsbiEndDateMstBilling(spaj);
	}
	
	public String selectLstReinsUwNilai(Double up,Integer tahunKe,Integer umur,Integer lamaLapse,String lkuId)throws DataAccessException{
		return reinstateDao.selectLstReinsUwNilai(up, tahunKe, umur, lamaLapse,lkuId);
	}
	
	public Integer selectLstMedicalRangeSar(Double sar, String lkuId)throws DataAccessException{
		return reinstateDao.selectLstMedicalRangeSar(sar, lkuId);
	}

	public Integer selectLstMedicalRangeAge(Integer age)throws DataAccessException{
		return reinstateDao.selectLstMedicalRangeAge(age);
	}

	public List<String> selectLstJenisMedical(Integer age, Integer sar, String lkuId)throws DataAccessException{
		return reinstateDao.selectLstJenisMedical(age, sar, lkuId);
	}

	public List selectlstCabang(){
		return uwDao.selectlstCabang();
	}
	
//	public List selectAllAgen(){
//		return uwDao.selectAllAgen();
//	}
	
	
	public List selectAllLstRegion(){
		return uwDao.selectAllLstRegion();
	}
	
	public List selectProductCombined() {
		return uwDao.selectProductCombined();
	}
	
	public List selectlstCabang2(){
		return uwDao.selectlstCabang2();
	}
	
	public List selectlstlevel(){
		return uwDao.selectlstlevel();
	}

	public Map selectReferrerBii(String spaj){
		return uwDao.selectReferrerBII(spaj);
	}
	/**Fungsi:	Untuk Menampilkan Data Karyawan yang mempunyai polis.
	 * @param 	String spaj
	 * @return	String
	 * @author	Ferry Harlim
	 */
	public String selectMstEmp(String spaj){
		return uwDao.selectMstEmp(spaj);
	}
	
	public Integer countkomisiperagen(String kode_agen,String id){
		return uwDao.countkomisiperagen(kode_agen,id);
	}
	
	public Integer countkomisipertglbayar(String kode_agen, String tgl1,String tgl2,String id){
		return uwDao.countkomisipertglbayar(kode_agen, tgl1, tgl2,id);
	}
	
	public Integer countkomisiperttp(String kode_agen, String tgl1,String tgl2,String id){
		return uwDao.countkomisiperttp(kode_agen, tgl1, tgl2,id);
	}	
	
	public Integer countkomisipertglproduksi(String kode_agen, String tgl1,String tgl2,String id){
		return uwDao.countkomisipertglproduksi(kode_agen, tgl1, tgl2,id);
	}

	public Integer countkomisipercabang(String cabang, String tgl1,String tgl2,String id){
		return uwDao.countkomisipercabang(cabang, tgl1, tgl2, id);
	}
	
	public Integer countkomisipercabang_all(String tgl1,String tgl2,String id){
		return uwDao.countkomisipercabang_all( tgl1, tgl2, id);
	}
	
	public Integer countagenbonus( String kode_agen, String tgl1,String tgl2 , String id)
	{
		return uwDao.countagenbonus(kode_agen, tgl1, tgl2, id);
	}
	
	/**Fungsi:	Untuk menghitung jumlah record softcopy polis per tgl aksep.
	 * @param 	String tgl1 dan tgl2 dengan waktu pukul 4 sore sampai 4 sore di hari esoknya
	 * @return	integer
	 * @author	Hemilda Sari Dewi
	 */
	public Integer count_softcopy_perhari(String tgl1,String tgl2)
	{
		return uwDao.count_softcopy_perhari(tgl1, tgl2);
	}
	
	/**Fungsi:	Untuk menghitung jumlah record softcopy polis per kirim polis.
	 * @param 	String tgl1 dan tgl2 
	 * @return	integer
	 * @author	Hemilda Sari Dewi
	 */
	public Integer countsoftcopy( String tgl1,String tgl2)
	{
		return uwDao.countsoftcopy(tgl1, tgl2);
	}
	
	public Integer countmonitorpolis( String tgl1,String tgl2)
	{
		return uwDao.countmonitorpolis(tgl1, tgl2);
	}
	
	public Integer countservicepolis( String tgl1,String tgl2)
	{
		return this.uwDao.countservicepolis(tgl1, tgl2);
	}
	
	public List selectDaftarkodestamp() throws DataAccessException {
		return this.uwDao.selectDaftarkodestamp();
	}
	
	
	public String addmonthss(Date tgl1 , Integer bulan)
	{
		return (String) this.bacDao.addmonthss(tgl1, bulan);
	}
	
	public Integer ceklevelagen(String kode_agen){
		return uwDao.ceklevelagen(kode_agen);
	}

	public Integer countproduksiperkodeagen(String kode_agen , String tgl1 ,String tgl2, String id)
	{
		return uwDao.countproduksiperkodeagen(kode_agen, tgl1,tgl2, id);
	}
	
	public Integer countproduksiperlevel(String level , String tgl1 ,String tgl2, String id)
	{
		return uwDao.countproduksiperlevel(level, tgl1,tgl2, id);
	}
	
	public Integer countproduksipercabang(String cabang , String tgl1 ,String tgl2, String id)
	{
		return uwDao.countproduksipercabang(cabang, tgl1,tgl2, id);
	}		
	
	public Integer countproduksipercabang_all( String tgl1 ,String tgl2, String id)
	{
		return uwDao.countproduksipercabang_all(tgl1,tgl2, id);
	}

	/**Fungsi:	Untuk mengecek ada tidak data followup billing
	 * @author	Hemilda Sari Dewi
	 */
	public Integer jumlahfollowup(String tgl1,String tgl2,String admin)
	{
		return uwDao.jumlahfollowup(tgl1, tgl2, admin);
	}
	
	/**Fungsi:	Untuk mengecek ada tidak data kematian dibayar
	 * @author	Hemilda Sari Dewi
	 */
	public Integer jumlahkematianbayar(String tgl1,String tgl2,String lstb_id, String lus_id)
	{
		return uwDao.jumlahkematianbayar(tgl1, tgl2, lstb_id, lus_id);
	}	
	
	/**Fungsi:	Untuk mengecek ada tidak data kematian pending
	 * @author	Hemilda Sari Dewi
	 */
	public Integer jumlahkematianpending(String tgl1,String tgl2,String lstb_id, String lus_id)
	{
		return uwDao.jumlahkematianpending(tgl1, tgl2, lstb_id, lus_id);
	}	
	
	/**Fungsi:	Untuk mengecek ada tidak data kesehatan
	 * @author	Hemilda Sari Dewi
	 */
	public Integer jumlahkesehatan(String tgl1,String tgl2,String lstb_id, String lus_id)
	{
		return uwDao.jumlahkesehatan(tgl1, tgl2, lstb_id, lus_id);
	}
	
	/**Fungsi:	Untuk menampilkan data pada tabel eka.lst_jn_deduct
	 * @author	Ferry Harlim
	 */
	public List selectLstJnDeduct(){
		return uwDao.selectLstJnDeduct();
	}
	/**Fungsi:	Untuk Menampilkan data produk utama pada tabel eka.lst_bisnis dalam bentuk list
	 * 			Sehingga dapat ditaruh di form select
	 * @return	List 
	 * @author	Ferry Harlim
	 */
	public List selectLstBisnis(){
		return uwDao.selectLstBisnis();
	}
	/**Fungsi:	Untuk Menampilkan detail nama product berdasarkan lsbs_id dan lsdbs_number nya
	 * @param	Integer lsbs_id	
	 * @return	List 
	 * @author	Ferry Harlim
	 */
	public List selectLstDetBisnis(Integer lsbs_id){
		return uwDao.selectLstDetBisnis(lsbs_id);
	}
	/**Fungsi:	Untuk Menampilkan msco_id berdasarkan spaj
	 * @param 	String spaj,Integer tahunKe,Integer premiKe,Integer level
	 * @return	String
	 * @author	Ferry Harlim
	 */
	public String selectMstCommissionMscoId(String spaj,Integer tahunKe,Integer premiKe,Integer level){
		return uwDao.selectMstCommissionMscoId(spaj, tahunKe, premiKe, level);
	}

	public Map selectKomisiPowersave(int lscp_jenis, int lsbs_id, int lsdbs_number, int lscp_mgi, String lku_id, int lev_comm, int lscp_year, double premi, int lscp_rollover, Date tgl_kom) {
		return uwDao.selectKomisiPowersave(lscp_jenis, lsbs_id, lsdbs_number, lscp_mgi, lku_id, lev_comm, lscp_year, premi, lscp_rollover, tgl_kom);
	}
	
	public List selectMReasTempNew(String spaj)throws DataAccessException{
		return uwDao.selectMReasTempNew(spaj);
	}

	/**Fungsi:	Untuk menampilkan jumlah rider excellink new 
	 * @param 	String spaj, int lsbsId1, int lsbsId2
	 * @return	Integer
	 * @author	Ferry Harlim
	 */
	public Integer selectCountRiderExcellinkNew(String spaj,int lsbsId1,int lsbsId2){
		return bacDao.selectCountRiderExcellinkNew(spaj,lsbsId1,lsbsId2);
	}
	
//	public Double selectNilaiBonusTahapan(String spaj){
//		return bacDao.selectNilaiBonusTahapan(spaj);
//	}
	
	public Integer selectMstAccountRecur(String spaj,Integer active){
		return (Integer)bacDao.selectMstAccountRecur(spaj,active);
	}
	
	public void prosesRecurring(String spaj,int insuredNo,String lusId){
		int lsspId=10;
		int lssaId=1;
		int lstbId=1;
		int lspdId=34;
		String desc="TRANSER KE PROSES RECURR";
		uwDao.updateMstInsured(spaj,34);
		uwDao.insertMstPositionSpaj(lusId, desc, spaj, 0);
		uwDao.updateMstPolicyLspdId(spaj,new Integer(lspdId),new Integer(lstbId));
//		Map up_lspd_id=new HashMap();
//		up_lspd_id.put("txtnospaj",txtnospaj);
//		up_lspd_id.put("lspd_id",new Integer(34));
//		up_lspd_id.put("mste_insured_no",new Integer(1));
//		error="Error pada saat Update mst_insured.lspd_id=34 (ser_transfer_bac2uw-wf_trans_recur)";
//		sqlMap.update("elions.bac.update.mst_insured_lspd_id",up_lspd_id);
//		//
//		Map ins_pos_spaj=new HashMap();
//		ins_pos_spaj.put("txtnospaj",txtnospaj);
//		ins_pos_spaj.put("lspd_id",new Integer(34));
//		ins_pos_spaj.put("lssp_id",new Integer(10));
//		ins_pos_spaj.put("lus_id",gl_lus_id);
//		ins_pos_spaj.put("msps_desc","TRANSER KE PROSES RECURR");
//		error="Error pada saat Insert posisi Spaj (ser_transfer_bac2uw-wf_trans_recur)";
//		sqlMap.insert("elions.bac.insert.mst_position_spaj",ins_pos_spaj);
//		//
//		Map up_policy=new HashMap();
//		up_policy.put("txtnospaj",txtnospaj);
//		up_policy.put("lspd_id_baru",new Integer(34));
//		up_policy.put("lspd_id_lama",new Integer(1));
//		up_policy.put("lstb_id",new Integer(1));
//		error="Error pada saat Update mst policy lspd_id (ser_transfer_bac2uw-wf_trans_recur)";
//		sqlMap.update("elions.bac.update.mst_policy.lspd_id",up_policy);
		//
	}
	/**Fungsi:	Untuk Menampilkan jumlah Rekening Client atau Nasabah pada tabel EKA.MST_REK_CLIENT
	 * @param 	String spaj
	 * @return	Double
	 * @author 	Ferry Harlim
	 */
	public Double selectCountMstRekClient(String spaj){
		return bacDao.selectCountMstRekClient(spaj);	  
	}
	/**Fungsi:	Untuk Menampilkan jumlah data atau record pada table EKA.MST_POWERSAVE_RO
	 * @param 	String spaj
	 * @return	Long
	 * @author	Ferry Harlim
	 */
	public Long selectCountMstPowerSaveRo(String spaj){
		return uwDao.selectCountMstPowerSaveRo(spaj);
	}
	/**Fungsi:	Untuk menampilkan data pada kolom EKA.MST_POWERSAVE_PROSES
	 * @param 	String spaj, Integer mpsKode
	 * @param 	String spaj,Integer mpsKode
	 * @return	List
	 * @author	Ferry Harlim
	 */
	public List selectMstPowerSaveProses(String spaj, Integer mpsKode){
		return bacDao.selectMstPowerSaveProses(spaj,mpsKode);
	}
	public List<Map> selectInfoStableLink(String spaj){
		return uwDao.selectInfoStableLink(spaj);
	}
	/**Fungsi	Untuk Melakukan proses Transfer Polis dari BAC ke U/W dimana para proses ini akan dilakukan pembuatan
	 * 			jurnal dengan menginsert tabel (khusus  yang ada BANK nya) :
	 * 			<strong>Insert</strong> tabel EKA.MST_BVOUCER, EKA.MST_DBANK, EKA.MST_TBANK. 
	 * 			Pada proses ini akan di lakukan pemindahan posisi SPAJ dari BAC(LSPD_ID1) ke
	 * 			Underwriting(LSPD_ID=2). dan akan dilakukan proses insert dan update pada tabel :
	 * 			<strong>Insert</strong> tabel EKA.MST_POSITION_SPAJ 
	 * 			<strong>Update</strong> tabel EKA.MST_POLICY, EKA.MST_INSURED, EKA.MST_DEPOSIT_PREMIUM
	 * @param 	String spaj,String lusId,Integer lsbsId,String namaPemegang,BindException err
	 * @author	Ferry Harlim
	 */
	
	public void updateMstPositionSpajDesc(String spaj,Integer lssaId,String mspsDate,String desc){
		uwDao.updateMstPositionSpajDesc(spaj,lssaId,mspsDate,desc);
	}
	
	public List selectAllMstTts(String value,String tipe,String filter,String lcaId){
		return financeDao.selectAllMstTts(value,tipe,filter,lcaId);
	}
	
	public List selectMstPolicyTts(String nomor,String kurs){
		return financeDao.selectMstPolicyTts(nomor,kurs);
	}
	
	public List selectMstCaraByr(String nomor){
		return financeDao.selectMstCaraByr(nomor);
	}
		
	public Map selectMaxNomorMstTtsMstNo(){
		return financeDao.selectMaxNomorMstTtsMstNo();
	}
	
	public List selectAllLstPaymentType(){
		return financeDao.selectAllLstPaymentType();
	}

	public List selectInLstPaymentType(List value){
		return financeDao.selectInLstPaymentType(value);
	}

	public String inputTtsNew(CommandTts command,String lusId){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String nomor=null;
		Tts tts=new Tts();
		Integer flagBatal=new Integer(0);
		Integer mscoNumber=1;
		if(command.getProses().intValue()==1 || command.getProses().intValue()==4){//input atau batal
			
			//0=reset counter per bulan
			//1=tidak reset counter per bulan
			Integer flag_Counter=financeDao.selectCekCounterTtsMonthAndYear(mscoNumber, command.getKd_cabang());
			if(flag_Counter==0){
				//update eka.mst_counter_tts
				financeDao.updateResetMstCounterTts(mscoNumber, command.getKd_cabang());
			}
			Map mapCounter=financeDao.selectGetCounterTts(mscoNumber,command.getKd_cabang());
			String formatCounter=(String)mapCounter.get("FORMAT_COUNTER");
			String monthYear=(String)mapCounter.get("MSCO_MONTH_YEAR");
			BigDecimal counter=(BigDecimal)mapCounter.get("COUNTER");
			
			Date today=commonDao.selectSysdate();
			nomor=formatCounter+monthYear+command.getKd_cabang()+"IE";
			financeDao.updateMstCounterTts(new Double(counter.intValue()),mscoNumber,command.getKd_cabang());
			command.setMst_no(nomor);
		}else if(command.getProses().intValue()==2)//edit
			nomor=command.getMst_no();

		financeDao.deleteMstPolicyTts(command.getMst_no());
		financeDao.deleteMstCaraByr(command.getMst_no());
		financeDao.deleteMstTts(command.getMst_no());
		
		//
		tts.setLst_kd_cab(command.getKd_cabang());
		tts.setLus_full_name(command.getNama_admin().toUpperCase());
		tts.setMst_ket(command.getKeterangan().toUpperCase());
		tts.setMst_nm_pemegang(command.getNama().toUpperCase());
		tts.setMst_no(nomor);
		tts.setMst_no_reff_btl(command.getMstNoBatal());
		tts.setFlag_print(new Integer(0));
		tts.setMst_flag_batal(flagBatal);
		tts.setMst_tgl_rk(command.getTglRk());
		tts.setLus_id(Integer.valueOf(lusId));
		tts.setMst_no_telp(command.getNoTelp());
		financeDao.insertMstTts(tts);
		
		financeDao.insertLstHistoryTts(nomor, Integer.valueOf(lusId), command.getDesc());
		//
		//update no tts yang di batalkan 
		if(command.getProses().intValue()==4){//batal
		//	financeDao.updateMstTtsBatal(command.getMstNoBatal(),nomor,command.getAlasanBatal(), new Integer(1));
			financeDao.updateMstTtsBatal(command.getMstNoBatal(),null,command.getMst_no(),1); //update data yang lama
			financeDao.insertLstHistoryTts(command.getMstNoBatal(), Integer.valueOf(lusId), "Batalkan Tts");
		}		

		//
		List lsPolis=command.getLsPolis();
		int count=1;
		for(int i=0;i<lsPolis.size();i++){
			PolicyTts policyTts=(PolicyTts)lsPolis.get(i);
			if(policyTts.getMstah_jumlah()>0)
				policyTts.setLku_id_tahapan(command.getTahapan().getLku_id());
			else
				policyTts.setLku_id_tahapan(null);
			
			if(command.getProses().intValue()==1 || command.getProses().intValue()==4){//input
				policyTts.setMst_no(nomor);
			}else{//edit
				policyTts=(PolicyTts)lsPolis.get(i);
			}
			
			if(policyTts.getPil().equals("1")){
				policyTts.setNo_urut(new Integer(count));
				count++;
				financeDao.insertMstPolicyTts(policyTts);
			}	
		}
		//
		List lsPembayaran=command.getLsPembayaran();
		for(int i=0;i<lsPembayaran.size();i++){
			CaraBayar caraBayar=new CaraBayar();
			
			if(command.getProses().intValue()==1 || command.getProses().intValue()==4){
				caraBayar=(CaraBayar)lsPembayaran.get(i);
				caraBayar.setMst_no(nomor);
			}else{
				caraBayar=(CaraBayar)lsPembayaran.get(i);
				caraBayar.setMst_no(command.getMst_no());
//				caraBayar.t(command.getKd_cabang());
			}
			financeDao.insertMstCaraBayar(caraBayar);
		}
		
		return nomor;
	}
	
	public void editTglSetor(CommandTts command,String lusId){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		financeDao.updateMstTtsTglSetor(command.getMst_no(), command.getTglSetor());
		financeDao.insertLstHistoryTts(command.getMst_no(),  Integer.valueOf(lusId), command.getDesc());
		
	}
	
	public Map selectPolicyTtsJumBayar(String mst_no){
		return financeDao.selectPolicyTtsJumBayar(mst_no);
	}
	/**Fungsi:	Untuk menampilkan nilai under tabel pada mspo_under_table pada tabel eka.mst_policy
	 * @param 	String spaj
	 * @return	Double
	 * @author	Ferry Harlim
	 */
	public Double selectNilaiUnderTable(String spaj){
		return uwDao.selectNilaiUnderTable(spaj);
	}
	
	public void updateMstTtsFlagPrint(String mstNo,Integer flag){
		financeDao.updateMstTtsFlagPrint(mstNo,flag);
	}
	
	public void updateMstTtsFlagPrintAndKet(String mstNo,Integer flag,String desc){
		financeDao.updateMstTtsFlagPrintAndKet(mstNo,flag,desc);
	}
	
	public List selectAllBillingNotPaid(String noPolis){
		return financeDao.selectAllBillingNotPaid(noPolis);
	}
	
	public List selectAllBillingNotPaidNew(String noPolis){
		return financeDao.selectAllBillingNotPaidNew(noPolis);
	}

	
	public void prosesCetakTts(Tts tts,int flag,String desc){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		if(flag==1){//proses cetak untuk pertama kali
			financeDao.updateMstTtsFlagPrint(tts.getMst_no(),new Integer(1));
			financeDao.insertLstHistoryPrint(new Integer(1),tts.getMst_no(),tts.getLst_kd_cab(),desc);
		}else if(flag==2){//proses cetak selanjutnya
			Integer no=financeDao.selectMaxLstHistoryPrintTts(tts.getMst_no());
			financeDao.insertLstHistoryPrint(new Integer(no.intValue()+1),tts.getMst_no(),tts.getLst_kd_cab(),desc);
		}
	}
	
	public Double selectLstBungaLsbunBungaTts(Integer lsbunJenis,String lkuId){
		return financeDao.selectLstBungaLsbunBungaTts(lsbunJenis,lkuId);
	}
	
	public Map selectMstPolicyLkudIdNLsbsId(String nopolis){
		return financeDao.selectMstPolicyLkudIdNLsbsId(nopolis);
	}
	
	public String selectLstCabangNamaCabang(String lcaId){
		return financeDao.selectLstCabangNamaCabang(lcaId);
	}
	
	public Map selectMstPolicy(String nopolis){
		return financeDao.selectMstPolicy(nopolis);
	}
	
	public Integer selectLstPayModeLscbTtlMonth(Integer lscbId){
		return financeDao.selectLstPayModeLscbTtlMonth(lscbId);
	}

	/**
	 * @param lsbsId
	 * @param lsdbsNumber
	 * @param tahunKe
	 * @return
	 */
	public Double selectGetDiscountPlan(Integer lsbsId, Integer lsdbsNumber,Integer tahunKe){
		return financeDao.selectGetDiscountPlan(lsbsId,lsdbsNumber,tahunKe);
	}
	
	public List selectLstHistoryPrintTts(String mstNo){
		return financeDao.selectLstHistoryPrintTts(mstNo);
	}


	
	public void prosesEditAlamatViewer(String spaj,ViewPolis viewPolis,User currentUser,HttpServletRequest request,BindException err){
		//cek apakah no blanko di ubah ato tidak (insert lst_ulangan)
		//cek tanggal spaj di ubah atao tidak (inser lst_ulangan)
		//cek apakah PP==TT jika sama, maka hanya update 1 saja, klo tidak dua2n ya harus update
		String pil1=request.getParameter("pil1");//no blangko  "on" || null
		String pil2=request.getParameter("pil2");//tgl spaj
		String pil3=request.getParameter("pil3");//data penerima manfaat
		String pil4=request.getParameter("pil4");//data alamat pemegang
		String pil5=request.getParameter("pil5");//data alamat tertanggung
		String pil6=request.getParameter("pil6");//no identitas pemegang
		String pil7=request.getParameter("pil7");//no identitas tertanggung
		String pil8=request.getParameter("pil8");//Edit Hubungan(Relation)
		String pil9=request.getParameter("pil9");//Edit Jenis Kelamin PP
		String pil10=request.getParameter("pil10");//Edit Jenis Kelamin TT
		String pil11=request.getParameter("pil11");//Edit Alamat Penagihan
		String pil12=request.getParameter("pil12");//Edit Cuti Premi
		String pil13=request.getParameter("pil13");//Edit Cuti Premi
		String pil14=request.getParameter("pil14");//Edit Cuti Premi
		Date tgl=commonDao.selectSysdate();
		Date tgl2=commonDao.selectSysdate();
		Calendar cal=new GregorianCalendar();
		cal.setTime(tgl);
		if(pil1!=null){
			if(! viewPolis.getBlangko().equals(viewPolis.getTempBlangko())){//update blangko
				uwDao.updateMstPolicyMspoNoBlangko(spaj,viewPolis.getBlangko());
				uwDao.insertLst_ulangan(spaj,cal.getTime(),"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
						currentUser.getLus_id(),"EDIT No. Blangko dr "+viewPolis.getTempBlangko());
			}
		}
		//
		tgl=FormatDate.add(tgl, Calendar.SECOND, cal.get(Calendar.SECOND)+1);
		if(pil2!=null){
			tgl=tglAdddetik(tgl);
			if(! viewPolis.getTglSpaj().equals(viewPolis.getTempTglSpaj())){//update tgl spaj
				uwDao.updateMstPolicyMspoSpajDate(spaj,viewPolis.getTglSpaj());
				uwDao.saveMstTransHistory(spaj, "tgl_spaj_asli", FormatDate.toStampDate(viewPolis.getTglSpaj()), null, null);
				uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
						currentUser.getLus_id(),"EDIT Tgl. Blangko dr "+viewPolis.getTempTglSpaj());
			}
		}
		//
		tgl=FormatDate.add(tgl, Calendar.SECOND, cal.get(Calendar.SECOND)+2);
		if(pil3!=null){
			uwDao.deleteMstBenefeciary(spaj);
			List lsBenef=viewPolis.getDataUsulan().getDaftabenef();
			String lsreId[]=new String[lsBenef.size()+1];
			String msaw_sex[]=new String[lsBenef.size()+1];
			lsreId=request.getParameterValues("relasi");
			msaw_sex=request.getParameterValues("sex");
			
			for(int i=0;i<lsBenef.size();i++){
				Benefeciary benef=(Benefeciary)lsBenef.get(i);
				benef.setLsre_id(Integer.valueOf(lsreId[i]));
				benef.setMsaw_sex(Integer.valueOf(msaw_sex[i]));
				uwDao.insertMstBenefeciary(benef);
			}
			uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
					currentUser.getLus_id(),"EDIT Daftar Penerima Manfaat");
		}
		//
		String ket;
		tgl=FormatDate.add(tgl, Calendar.SECOND, cal.get(Calendar.SECOND)+3);
		if(pil4!=null){//update pemegang
			tgl=tglAdddetik(tgl);
			if(viewPolis.getPemegang().getLsre_id().equals("1"))//PP==TT
				ket="EDIT Alamat Pemegang dan Tertanggung";
			else
				ket="EDIT Alamat Pemegang ";
				
			AddressNew addNew=new AddressNew();
			addNew.setAlamat_kantor(viewPolis.getPemegang().getAlamat_kantor().toUpperCase());
			addNew.setAlamat_rumah(viewPolis.getPemegang().getAlamat_rumah().toUpperCase());
			addNew.setArea_code_fax(viewPolis.getPemegang().getArea_code_fax());
			addNew.setArea_code_kantor(viewPolis.getPemegang().getArea_code_kantor());
			addNew.setArea_code_kantor2(viewPolis.getPemegang().getArea_code_kantor2());
			addNew.setArea_code_rumah(viewPolis.getPemegang().getArea_code_rumah());
			addNew.setArea_code_rumah2(viewPolis.getPemegang().getArea_code_rumah2());
			addNew.setEmail(viewPolis.getPemegang().getEmail().toUpperCase());
			addNew.setKd_pos_kantor(viewPolis.getPemegang().getKd_pos_kantor());
			addNew.setKd_pos_rumah(viewPolis.getPemegang().getKd_pos_rumah());
			addNew.setKota_kantor(viewPolis.getPemegang().getKota_kantor().toUpperCase());
			addNew.setKota_rumah(viewPolis.getPemegang().getKota_rumah().toUpperCase());
			addNew.setMcl_id(viewPolis.getPemegang().getMcl_id());
			addNew.setNo_fax(viewPolis.getPemegang().getNo_fax());
			addNew.setNo_hp(viewPolis.getPemegang().getNo_hp());
			addNew.setNo_hp2(viewPolis.getPemegang().getNo_hp2());
			addNew.setTelpon_kantor(viewPolis.getPemegang().getTelpon_kantor());
			addNew.setTelpon_kantor2(viewPolis.getPemegang().getTelpon_kantor2());
			addNew.setTelpon_rumah(viewPolis.getPemegang().getTelpon_rumah());
			addNew.setTelpon_rumah2(viewPolis.getPemegang().getTelpon_rumah2());
			uwDao.updateMstAddressNew(addNew);
			uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
					currentUser.getLus_id(),ket);
			//update alamat pada simas card
			Simcard simcard=new Simcard();
			simcard.setMcl_id(viewPolis.getPemegang().getMcl_id());
			simcard.setAlamat(viewPolis.getPemegang().getAlamat_rumah().toUpperCase());
			simcard.setKota(viewPolis.getPemegang().getKota_rumah().toUpperCase());
			simcard.setKode_pos(viewPolis.getPemegang().getKd_pos_rumah().toUpperCase());
			uwDao.updateMstSimcardAlamat(simcard);
		
		}
		//
		if(pil5!=null){//update tertanggung
			ket="EDIT Alamat Tertanggung ";
			tgl=tglAdddetik(tgl);
			AddressNew addNewT=new AddressNew();
			addNewT.setAlamat_kantor(viewPolis.getTertanggung().getAlamat_kantor().toUpperCase());
			addNewT.setAlamat_rumah(viewPolis.getTertanggung().getAlamat_rumah().toUpperCase());
			addNewT.setArea_code_fax(viewPolis.getTertanggung().getArea_code_fax());
			addNewT.setArea_code_kantor(viewPolis.getTertanggung().getArea_code_kantor());
			addNewT.setArea_code_kantor2(viewPolis.getTertanggung().getArea_code_kantor2());
			addNewT.setArea_code_rumah(viewPolis.getTertanggung().getArea_code_rumah());
			addNewT.setArea_code_rumah2(viewPolis.getTertanggung().getArea_code_rumah2());
			addNewT.setEmail(viewPolis.getTertanggung().getEmail().toUpperCase());
			addNewT.setKd_pos_kantor(viewPolis.getTertanggung().getKd_pos_kantor());
			addNewT.setKd_pos_rumah(viewPolis.getTertanggung().getKd_pos_rumah());
			addNewT.setKota_kantor(viewPolis.getTertanggung().getKota_kantor().toUpperCase());
			addNewT.setKota_rumah(viewPolis.getTertanggung().getKota_rumah().toUpperCase());
			addNewT.setMcl_id(viewPolis.getTertanggung().getMcl_id());
			addNewT.setNo_fax(viewPolis.getTertanggung().getNo_fax());
			addNewT.setNo_hp(viewPolis.getTertanggung().getNo_hp());
			addNewT.setNo_hp2(viewPolis.getTertanggung().getNo_hp2());
			addNewT.setTelpon_kantor(viewPolis.getTertanggung().getTelpon_kantor());
			addNewT.setTelpon_kantor2(viewPolis.getTertanggung().getTelpon_kantor2());
			addNewT.setTelpon_rumah(viewPolis.getTertanggung().getTelpon_rumah());
			addNewT.setTelpon_rumah2(viewPolis.getTertanggung().getTelpon_rumah2());
			uwDao.updateMstAddressNew(addNewT);
			uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
					currentUser.getLus_id(),ket);
			//update alamat pada simas card
			Simcard simcard=new Simcard();
			simcard.setMcl_id(viewPolis.getTertanggung().getMcl_id());
			simcard.setAlamat(viewPolis.getTertanggung().getAlamat_rumah().toUpperCase());
			simcard.setKota(viewPolis.getTertanggung().getKota_rumah().toUpperCase());
			simcard.setKode_pos(viewPolis.getTertanggung().getKd_pos_rumah().toUpperCase());
			uwDao.updateMstSimcardAlamat(simcard);
		}	
		//
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		if(pil6!=null){
			tgl=tglAdddetik(tgl);
			if(! viewPolis.getPemegang().getMspe_no_identity().equals("")){//Update No Ktp
			//	String lside=request.getS("identitas");
				String lsidePP = ServletRequestUtils.getStringParameter(request, "identitas_pp","");
				uwDao.updateMstClientNewKtp(viewPolis.getPemegang().getMcl_id(), viewPolis.getPemegang().getMspe_no_identity(),lsidePP);
				uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
						currentUser.getLus_id(),"EDIT NO IDENTITAS PEMEGANG DARI SPAJ "+viewPolis.getPemegang().getReg_spaj());
			}
		}
		if(pil7!=null){
			tgl=tglAdddetik(tgl);
			if(! viewPolis.getTertanggung().getMspe_no_identity().equals("")){//Update No Ktp
			//	String lside=request.getS("identitas");
				String lsideTT = ServletRequestUtils.getStringParameter(request, "identitas_tt","");
				uwDao.updateMstClientNewKtp(viewPolis.getTertanggung().getMcl_id(), viewPolis.getTertanggung().getMspe_no_identity(),lsideTT);
				uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
						currentUser.getLus_id(),"EDIT NO IDENTITAS TERTANGGUNG DARI SPAJ "+viewPolis.getPemegang().getReg_spaj());
			}
		}
		if(pil8!=null){
			tgl=tglAdddetik(tgl);
			if(! viewPolis.getPemegang().getLsre_id().equals("")){//Update Relation
				String lsre_id = ServletRequestUtils.getStringParameter(request, "relation","");
				uwDao.updateMstPolicyRelation(spaj,lsre_id);
				uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
						currentUser.getLus_id(),"EDIT HUBUNGAN PEMEGANG DENGAN TERTANGGUNG "+viewPolis.getPemegang().getReg_spaj());
			}
		}
		if(pil9!=null){
			tgl=tglAdddetik(tgl);
			if(! viewPolis.getPemegang().getMspe_sex().equals("")){////Edit Jenis Kelamin
				String sex_pp = ServletRequestUtils.getStringParameter(request, "sex_pp","");
				uwDao.updateMstClientNewSatuKolom(viewPolis.getPemegang().getMcl_id(),"mspe_sex",sex_pp);
				uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
						currentUser.getLus_id(),"EDIT JENIS KELAMIN "+viewPolis.getPemegang().getReg_spaj());
			}
		}
		if(pil10!=null){
			tgl=tglAdddetik(tgl);
			if(! viewPolis.getTertanggung().getMspe_sex().equals("")){////Edit Jenis Kelamin
				String sex_tt = ServletRequestUtils.getStringParameter(request, "sex_tt","");
				uwDao.updateMstClientNewSatuKolom(viewPolis.getTertanggung().getMcl_id(),"mspe_sex",sex_tt);
				uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
						currentUser.getLus_id(),"EDIT JENIS KELAMIN "+viewPolis.getPemegang().getReg_spaj());
			}
		}
		
		if(pil11!=null){
			tgl=tglAdddetik(tgl);
			viewPolis.getAddressbilling().setReg_spaj(viewPolis.getPemegang().getReg_spaj());
			if(viewPolis.getAddressbilling().getKota_tgh() != null) {
				viewPolis.getAddressbilling().setLska_id(bacDao.select_kabupaten(viewPolis.getAddressbilling().getKota_tgh().toUpperCase().trim()));
			}
			int rowupdate=uwDao.updateMst_address_billing(viewPolis.getAddressbilling());
			if(rowupdate==1)uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
					currentUser.getLus_id(),"EDIT ALAMAT PENAGIHAN "+viewPolis.getPemegang().getReg_spaj());
		}
		if(pil12!=null){
			tgl=tglAdddetik(tgl);
			if(viewPolis.getDataUsulan().getMspo_installment()!=null){
				if(viewPolis.getDataUsulan().getMspo_installment()==0)viewPolis.getDataUsulan().setMspo_installment(null);
				uwDao.updateMstPolicySatuKolom(spaj,"mspo_installment",viewPolis.getDataUsulan().getMspo_installment().toString());
				uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
						currentUser.getLus_id(),"EDIT CUTI PREMI "+viewPolis.getDataUsulan().getReg_spaj());
			}
		}
		if(pil13!=null){
			tgl=tglAdddetik(tgl);
			if(viewPolis.getPemegang().getMspe_sts_mrt()!=null){
				String sts_pmg = ServletRequestUtils.getStringParameter(request, "status_mrt_pemegang","");
				uwDao.updateMstClientNewSatuKolom(viewPolis.getPemegang().getMcl_id(),"mspe_sts_mrt",sts_pmg);
				uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
						currentUser.getLus_id(),"EDIT JENIS KELAMIN "+viewPolis.getPemegang().getReg_spaj());
			}
		}
		if(pil14!=null){
			tgl=tglAdddetik(tgl);
			if(viewPolis.getPemegang().getMspe_sts_mrt()!=null){
				String sts_pmg = ServletRequestUtils.getStringParameter(request, "status_mrt_ttg","");
				uwDao.updateMstClientNewSatuKolom(viewPolis.getTertanggung().getMcl_id(),"mspe_sts_mrt",sts_pmg);
				uwDao.insertLst_ulangan(spaj,tgl,"EDIT DATA",viewPolis.getPemegang().getLssp_id(),
						currentUser.getLus_id(),"EDIT JENIS KELAMIN "+viewPolis.getPemegang().getReg_spaj());
			}
		}
	}
	
	private Date tglAdddetik(Date tgl) {
		Calendar cal=new GregorianCalendar();
		cal.setTime(tgl);
		cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE),cal.get(Calendar.HOUR),cal.get(Calendar.MINUTE),cal.get(cal.SECOND)+1);
		return cal.getTime();
		
	}
	private boolean wf_check_reas(String spaj) {
		boolean lb_reas = true;
		Integer ll_row;
		ll_row=(Integer)selectCountMstReins(spaj);
		
		if( ll_row.intValue() < 1)
			lb_reas = false;
		else if( ll_row.intValue() == 1)
			lb_reas = true;
		else
			lb_reas = false;
		
		return lb_reas;
	}
	
	public List selectMstHistoryTts(String mstNo){
		return financeDao.selectMstHistoryTts(mstNo);
	}
	//cek lagi masih di pake apa gak?
	public Position selectMstPositionSpajAccepted(String regSpaj,Integer lspdId,Integer lssaId,Integer lsspId){
		Map param=new HashMap();
		param.put("reg_spaj", regSpaj);
		param.put("lspd_id", lspdId);
		param.put("lssa_id", lssaId);
		param.put("lssp_id", lsspId);
		return uwDao.selectMstPositionSpajAccepted(regSpaj, lspdId, lssaId, lsspId);
	}
	
	public List selectSummaryStatusAccepted(Map map){
		List lsSpaj=new ArrayList();
		List lsHasil=new ArrayList();
		Date mspsDate=(Date)map.get("tgl");
		String lcaId=(String)map.get("cabang");
		lsHasil=uwDao.selectMstPositionSpajMspsDate(defaultDateFormat.format(mspsDate),lcaId);
		//lsHasil=uwDao.selectAdminRegion();
		return lsHasil;
	}
	
	public void update_mst_kode_dirjen(Stamp stamp) throws DataAccessException{
		uwDao.update_mst_kode_dirjen(stamp);
	}	
	
	public void prosesInputPersonal(Command cmd,User user){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();

		//1. MST_CLIENT_NEW
		if(cmd.getPersonal().getMcl_id().equals("")){
			NumberFormat nf=new DecimalFormat("0000000000");
			/*String counter=uwDao.selectGetCounter(3, user.getLca_id());
			uwDao.updateMstCounter2(counter,3,user.getLca_id());
			String mclId= user.getLca_id()+nf.format(Integer.parseInt(counter));
			*/
			String mclId = uwDao.getClientNewId(null);
			
			cmd.getPersonal().setMcl_id(mclId);
			Pemegang pemegang=new Pemegang();
			pemegang.setMcl_id(mclId);
			pemegang.setMcl_first(cmd.getPersonal().getMcl_first());
			pemegang.setCbg_lus_id(user.getLus_id());
			pemegang.setMcl_gelar(cmd.getPersonal().getMcl_gelar());
			pemegang.setMcl_jenis(new Integer(1));//untuk perusahaan
			bacDao.insertMstClientNew(pemegang);
		}else {
			cmd.getPersonal().setLca_id(cmd.getPersonal().getRegion_id().substring(0,2));
			cmd.getPersonal().setLwk_id(cmd.getPersonal().getRegion_id().substring(2,4));
			cmd.getPersonal().setLsrg_id(cmd.getPersonal().getRegion_id().substring(4,6));
			commonDao.updateMstClientNew(cmd.getPersonal().getMcl_id(), 
					cmd.getPersonal().getMcl_first(), cmd.getPersonal().getMcl_gelar());
		}
		
		//2. MST_COMPANY
		if(commonDao.updateMstCompany(cmd.getPersonal()) == 0) {
			commonDao.insertMstCompany(cmd.getPersonal());
		}
		
		//3. MST_ADDRESS_NEW
		if(commonDao.updateMstCompanyAddress(cmd.getPersonal().getAddress()) == 0) {
			cmd.getPersonal().getAddress().setMcl_id(cmd.getPersonal().getMcl_id());
			commonDao.insertMstCompanyAddress(cmd.getPersonal().getAddress());
		}
		
		//4. MST_COMPANY_CONTACT
		if(cmd.getPersonal().getFlag_gws() != null && cmd.getPersonal().getFlag_gws() > 0) {
			// Jika GWS maka delete dulu data yg lama buat jaga2 kalo ganti jenis 
			// kompensasi dari 2 layer ke 3 layer atau sebaliknya - Daru @since 19 Mar 2014
			commonDao.deleteMstCompanyContact(cmd.getPersonal().getMcl_id());
		}
		
		for(int i=0;i<cmd.getPersonal().getLsContactPerson().size();i++){
			ContactPerson contactPerson=(ContactPerson)cmd.getPersonal().getLsContactPerson().get(i);
			contactPerson.setMcl_id(cmd.getPersonal().getMcl_id());
			contactPerson.setLus_id(Integer.valueOf(user.getLus_id()));
//			contactPerson.setInput_date(commonDao.selectSysdate());
			if(cmd.getFlag_ut()==i+1)
				contactPerson.setFlag_ut(1);
			else
			contactPerson.setFlag_ut(0);
			if(!contactPerson.getNama_lengkap().equals(""))
			if(commonDao.updateMstCompanyContact(contactPerson) == 0) {
				contactPerson.setNo_urut(i+1);
				commonDao.insertMstCompanyContact(contactPerson);
			}
		}	
		
				
	}
	
	public List selectLstUlangan(String spaj){
		return uwDao.selectLstUlangan(spaj);
	}
	public String selectLstPolicyStatus(Integer lsspId){
		return uwDao.selectLstPolicyStatus(lsspId);
	}
	public void insertLstUlangan(Ulangan ulangan){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		uwDao.insertLstUlangan(ulangan);
	}
	
	public Tahapan selectMstTahapan(String nopolis,Integer lspdId){
		return financeDao.selectMstTahapan(nopolis,lspdId);
	}
	
	public List selectMstPolicyTtsNopolis(String mstNo){
		return financeDao.selectMstPolicyTtsNopolis(mstNo);
	}
	
	public List selectViewerTts(Map map){
		String lcaId,lusId,flagPrint;
		lcaId=(String) map.get("Cabang");
		lusId=(String)map.get("NamaAdmin");
		flagPrint=(String)map.get("Print");
		return financeDao.selectViewerTts(lusId, lcaId, flagPrint);
	}
	
	public Map selectPerusahaanWorksite(String spaj){
		return uwDao.selectPerusahaanWorksite(spaj);
	}
	
	/*
	 * Dian natalia
	 * u/ mencek rekening agen ada tau tidak
	 * 
	 */
	public List cekRekAgen(String reg_spaj) {
		return uwDao.cekRekAgen(reg_spaj);
	}
	
	/**
	 * Dian Natalia
	 * @param reg_spaj
	 * @return
	 * digunakan untuk mencek apakahtanggal RK dan tanggal  begdate sama  tau tidak
	 */
	public List cekTglRk(String reg_spaj) {
		return (List)uwDao.cekTglRk(reg_spaj);
	}
	
	public Date selectMstTtsTglSetor(String mstNo){
		return financeDao.selectMstTtsTglSetor(mstNo);
	}
	
	public List selectRider(String spaj){
		return bacDao.selectRider(spaj);
	}
	public List selectInvest(String spaj){
		return bacDao.selectInvest(spaj);
	}
	public Integer selectCountProductHcp(String spaj){
		return bacDao.selectCountProductHcp(spaj);
	}
	public Menu selectLstMenuNew(Integer menuId){
		return commonDao.selectLstMenuNew(menuId);
	}
	public List selectCheckChild(Integer menuId){
		return commonDao.selectCheckChild(menuId);
	}
	public void updateLstMenuNew(Menu menu){
		commonDao.updateLstMenuNew(menu);
	}
	public void deleteLstMenuNew(Integer menuId){
		commonDao.deleteLstMenuNew(menuId);
	}
	public void insertLstMenuNew(Menu menu){
		commonDao.insertLstMenuNew(menu);
	}
	public Integer selectMaxMenuId(){
		return commonDao.selectMaxMenuId();
	}
	public Integer selectTingkatMenu(Integer menuId){
		return commonDao.selectTingkatMenu(menuId);
	}
	public Integer selectMaxUrutanMenu(Integer menuId){
		return commonDao.selectMaxUrutanMenu(menuId);
	}
	/**@Fungsi: Proses Untuk menjalankan triger yang masih ada bugnya, sehingga data
	 * 			tertanggung tidak muncul, prosesnya hanya update ke diri sendiri saja.	
	 * @param :	String spaj
	 * @author 	Ferry Harlim
	 * */
	public void prosesBlankTertanggung(String spaj){
		//update salah satu kolom di eka.mst_address_new
		Map mTertanggung=uwDao.selectTertanggung(spaj);
		String kdPosRumah=(String)mTertanggung.get("KD_POS_RUMAH");
		String mclId=(String)mTertanggung.get("MCL_ID");
		uwDao.updateMstAddressNewKdPosRumah(mclId, kdPosRumah);
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
	}
	
	/**@Fungsi:	Proses untuk mereas dengan kejadian yang biasa terjadi yaitu, status medical
	 * 			salah dan polis sudah di reas, sehingga pada saat transfer ke Payment/Print Polis
	 * 			tidak dapat dilanjutkan, maka dengan itu status medis dapat di ubah, namun polis tersebut
	 * 			harus di reas ulang.
	 * @param : String spaj, String lusIdReq
	 * @throws	MessagingException 
	 * @throws 	MailException
	 * @author 	Ferry Harlim 
	 * */
	public void prosesUbahMedisSetelahReas(String spaj, String userReq,User currentUser,String medis) throws MailException, MessagingException{
		//dokumentasikan data dan dikirim ke email..
		String message="<table><tr><th><strong> Data Tabel EKA.MST_INSURED </strong><th></tr>";
		Map mInsured=uwDao.selectAllMstInsured(spaj);
		message=message+"<tr><td>"+mInsured+"</td></tr>";
		//
		message=message+"<tr><th><strong>Data Tabel EKA.MST_POSITION_SPAJ</strong></th></tr>";
		List lsPos=uwDao.selectMstPositionSpaj(spaj);
		for(int i=0;i<lsPos.size();i++){
			Map mPos=(HashMap)lsPos.get(i);
			message=message+"<tr><td>"+mPos+"</td></tr>";
		}
		//
		message=message+"<tr><th><strong>Data Tabel EKA.MREAS_TEMP</strong></th></tr>";
		List lsMReas=uwDao.selectMReasTemp(spaj);
		for(int i=0;i<lsMReas.size();i++){
			Map mReas=(HashMap)lsMReas.get(i);
			message=message+"<tr><td>"+mReas+"</td></tr>";
		}
		//
		message=message+"<tr><th><strong>Data Tabel EKA.MSAR_TEMP</strong></th></tr>";
		List lsMSar=uwDao.selectMSarTemp(spaj);
		for(int i=0;i<lsMSar.size();i++){
			Map mSar=(HashMap)lsMSar.get(i);
			message=message+"<tr><td>"+mSar+"</td></tr>";
		}
		//
		message=message+"<tr><th><strong>Data Tabel EKA.MST_SIMULTANEOUS</strong></th></tr>";
		List lsSimultan=uwDao.selectAllMstSimultaneous(spaj);
		for(int i=0;i<lsSimultan.size();i++){
			Map mSim=(HashMap)lsSimultan.get(i);
			message=message+"<tr><td>"+mSim+"</td></tr>";
		}
		message=message+"</table>";
		String subject="Proses Reas Ulang, Ubah Medis SPAJ ("+spaj+"). Permintaan : "+userReq;
		uwDao.deleteMReasTemp(spaj);
		uwDao.deleteMSarTemp(spaj);
		uwDao.deleteMstSimultaneous(spaj);
		uwDao.updateMstInsuredReasnBackup(spaj, 1, null, null, 1, medis);
		Map MPolicy=uwDao.selectMstPolicy(spaj, 1);
		String ket;
		if(medis.equals("0"))
			ket="UBAH MEDIS MENJADI NON MEDIS REAS ULANG-"+userReq;
		else
			ket="UBAH NON MEDIS MENJADI MEDIS REAS ULANG-"+userReq;
	
		uwDao.insertLst_ulangan(spaj, commonDao.selectSysdate(), "EDIT DATA", (Integer)MPolicy.get("LSSP_ID"), currentUser.getLus_id(), ket);
		
		EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
				null, 0, 0, new Date(), null, 
				false, props.getProperty("admin.ajsjava"), new String[] {props.getProperty("admin.dian")}, null, null, subject, message, null, spaj);
		
	}
	
	/**@Fungsi:	Untuk Mengupdate kolom LSPD_ID (posisi Polis) dan LSSA_ID (Status Aksep)
	 * 			pada Table EKA.MST_INSURED 
	 * @param	String spaj,Integer lspdId,Integer lssaId,Integer insuredNo
	 * @author 	Ferry Harlim
	 * */
	public void updateMstInsured(String spaj,Integer lspdId,Integer lssaId,Integer insuredNo){
		uwDao.updateMstInsured(spaj, lspdId, lssaId, insuredNo, null);
	}
	/**@Fungsi:	Untuk Menampilkan data Pada Tabel EKA.MST_DET_ULINK
	 * 			Jika ljiId =  null , data yang ditampilkan berdasarkan kondisi spaj;
	 * 				 ljiId != null , data yang ditampilkan berdasarkan kondisi spaj dan ljiI 
	 * @param 	String spaj, String ljiId
	 * @return 	List
	 * @author 	Ferry Harlim
	 * */
	public List selectAllMstDetUlink(String spaj,String ljiId){
		return uwDao.selectAllMstDetUlink(spaj,ljiId);
	}
	/**@Fungsi:	Untuk Menampilkan semua data Pada Tabel EKA.MST_TRANS_ULINK
	 * @param 	String spaj
	 * @return 	List
	 * @author 	Ferry Harlim
	 * */
	public List selectAllMstTransUlink(String spaj){
		return uwDao.selectAllMstTransUlink(spaj);
	}
	/**@Fungsi:	Untuk Mengupdate Tanggal kirim polis (MSTE_TGL_KIRIM_POLIS)
	 * 			atau tanggal terima spaj (MSTE_TGL_TERIMA_SPAJ)
	 * 			flag(0)=untuk update tgl Terima spaj.
	 * 			flag(1)=untuk update tgl Kirim polis.
	 * 
	 * @param 	String spaj,Integer insuredNo, Date tgl,Integer flag
	 * @author 	Ferry Harlim
	 */
	public void updateMstInsuredTgl(String spaj,Integer insuredNo, Date tgl,Integer flag){
		uwDao.updateMstInsuredTgl(spaj, insuredNo, tgl, flag);
	}
	
	
	public void updateMstInsuredTglAdmin(String spaj,Integer insuredNo, Date tgl,Integer flag){
		uwDao.updateMstInsuredTglAdmin(spaj, insuredNo, tgl, flag);
	}
	
	/**@Fungsi:	Untuk Menampilkan nilai NAB dari suatu polis
	 * @param :	String spaj,Integer muKe
	 * @return:	List	
	 * @author 	Ferry Harlim
	 */
	public List selectJnsLinkAndNab(String spaj,Integer muKe){
		return uwDao.selectJnsLinkAndNab(spaj, muKe);
	}
	/**@Fungsi:	Untuk melakukan proses edit tanggal spaj, dimana akan dilakukan
	 * 			pengupdatetan kolom tanggal sesuai degan pilihan update tanggal, dengan parameter
	 * 			show (0) = update EKA.MST_INSURED.MSTE_TGL_TERIMA_SPAJ
	 * 			show (1) = update EKA.MST_INSURED.MSTE_TGL_KIRIM_POLIS
	 * 			show (2) = update EKA.MST_INSURED.MSTE_TGL_SPAJ_DOC
	 * 			dan pengupdatetan tanggal ini akan tersimpan di history pada tabel
	 * 			EKA.MST_POSITION_SPAJ, jika di update > 1 maka tabel ini juga akan di update tanggalnya
	 * 			yang terbaru.
	 * @param 	String spaj,String userId,Integer insured, String tgl,Integer show
	 * @author 	Ferry Harlim
	 */
	public void prosesEditTanggalSpaj(String spaj,User user,Integer insuredNo,Integer lspdId,Integer lsspId,Integer lssaId, Date tgl,Integer show,String keterangan, String s_noresi){
		String desc="ERROR";
		String kolom_tgl = null;
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		if(show==0){
			desc="TGL TERIMA SPAJ";
			kolom_tgl = "tgl_terima_spaj_uw";
		}else if(show==1){
			desc="TGL KIRIM POLIS";
			kolom_tgl = "tgl_kirim_polis";
		}else if(show==2){
			desc="TGL SPAJ DOC";
			kolom_tgl = "tgl_berkas_terima_uw";
		}else if(show==3){
			desc="TGL TERIMA ADMEDIKA";
			kolom_tgl = "tgl_terima_admedika";
		}else if(show==4){
			desc="TGL TERIMA ADMIN";
			kolom_tgl = "tgl_terima_spaj_admin";
		}else if(show==8){
			desc="TGL KIRIM SK DEBET";
		}
		if(keterangan!=null){
			keterangan=desc+"("+keterangan+")";
		}else
			keterangan=desc;
		if(show != 8){
			uwDao.updateMstInsuredTgl(spaj, insuredNo, tgl, show);
			uwDao.saveMstTransHistory(spaj, kolom_tgl, tgl, null, null);
		}
		
		if(uwDao.updateMstpositionSpajTgl(spaj, user.getLus_id(), defaultDateFormat.format(tgl), desc,keterangan)==0){
			uwDao.insertMstPositionSpaj(user.getLus_id(), keterangan, spaj, 0);
		}
		
		if(s_noresi!=null || !s_noresi.equalsIgnoreCase("")){
			uwDao.updateMstPolicyNoResi(spaj, s_noresi.trim());
		}
		
		//apabila inputan bank (bank apapun), langsung insert data tanggal terima spaj saat insert tgl spaj doc
		//Deddy - REQ INGE : untuk inputan BSM selain simpol, Sekuritas dan mall, pengisian tanggal terima SPAJ dilakukan manual di posisi print polis U/W.
//		int isInputanBank = selectIsInputanBank(spaj); 
//		if((isInputanBank == 2 || isInputanBank == 3) && show==2) {
//			if(isInputanBank == 2)
//				desc = "TGL TERIMA SPAJ (INPUTAN BANK SINARMAS)";
//			else if(isInputanBank == 3)
//				desc = "TGL TERIMA SPAJ (INPUTAN SINARMAS SEKURITAS)";
//			
//			uwDao.updateMstInsuredTgl(spaj, 1, tgl, 0);
//			for(int i=0; i<1000; i++) {
//				try {
//					bacDao.insertMst_position_no_spaj_pb(spaj, user.getLus_id(),lspdId, 10, desc,i);
//					break;
//				} catch (DataAccessException e) {
//				}
//			}
//		}
				
	}
	
	
	
	public void prosesEditTanggalSpajAdmin(String spaj,User user,Integer insuredNo,Integer lspdId,Integer lsspId,Integer lssaId, Date tgl,Integer show,String keterangan){
		String desc="ERROR";
		String kolom_tgl = "";
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		if(show==0){
			desc="TGL TERIMA ADMIN";
			kolom_tgl = "tgl_terima_spaj_admin";
		}else if(show==1){
			desc="TGL KIRIM POLIS";
			kolom_tgl = "tgl_kirim_polis";
		}else if(show==2){
			desc="TGL SPAJ DOC";
			kolom_tgl = "tgl_berkas_terima_uw";
		}
		if(keterangan!=null){
			keterangan=desc+"("+keterangan+")";
		}else
			keterangan=desc;
		uwDao.updateMstInsuredTglAdmin(spaj, insuredNo, tgl, show);
		uwDao.saveMstTransHistory(spaj, kolom_tgl, tgl, null, null);
		if(uwDao.updateMstpositionSpajTgl(spaj, user.getLus_id(), defaultDateFormat.format(tgl), desc,keterangan)==0){
			uwDao.insertMstPositionSpaj(user.getLus_id(), keterangan, spaj, 0);
		}
		
		//apabila inputan bank (bank apapun), langsung insert data tanggal terima spaj saat insert tgl spaj doc
		int isInputanBank = selectIsInputanBank(spaj); 
		if((isInputanBank == 2 || isInputanBank == 3) && show==2) {
			if(isInputanBank == 2)
				desc = "TGL TERIMA ADMIN (INPUTAN BANK SINARMAS)";
			else if(isInputanBank == 3)
				desc = "TGL TERIMA ADMIN (INPUTAN SINARMAS SEKURITAS)";
			
			uwDao.updateMstInsuredTglAdmin(spaj, 1, tgl, 0);
			uwDao.saveMstTransHistory(spaj, "tgl_terima_spaj_admin", tgl, null, null);
			for(int i=0; i<1000; i++) {
				try {
					bacDao.insertMst_position_no_spaj_pb(spaj, user.getLus_id(),1, 10, desc,i);
					break;
				} catch (DataAccessException e) {
				}
			}
		}
				
	}
	
	public void prosesEditTanggalTTP(String spaj,User user,Integer insuredNo,Integer lspdId,Integer lsspId,Integer lssaId, Date tgl,Integer show,String keterangan){
		String desc="ERROR";
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		if(show==0){
			desc="TGL TERIMA ADMIN";
		}else if(show==1){
			desc="TGL KIRIM POLIS";
		}else if(show==2){
			desc="TGL SPAJ DOC";
		}else if(show==4){
			desc="TGL TERIMA TTP";
		}
		if(keterangan!=null){
			keterangan=desc+"("+keterangan+") ";
		}else
			keterangan=desc;
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		//Yusuf (05/11/2010) - Req Dr Ingrid, tgl terima ttp disimpan di uw info juga
		if(keterangan != null){
			keterangan += " (" + df.format(tgl) + ")"; 
		}
		
		uwDao.updateMstPolicyTtp(spaj, tgl);
		if(uwDao.updateMstpositionSpajTgl(spaj, user.getLus_id(), defaultDateFormat.format(tgl), desc,keterangan)==0){
			uwDao.insertMstPositionSpaj(user.getLus_id(), keterangan, spaj, 0);
		}
		
		//apabila inputan bank (bank apapun), langsung insert data tanggal terima spaj saat insert tgl spaj doc
		int isInputanBank = selectIsInputanBank(spaj); 
		if((isInputanBank == 2 || isInputanBank == 3) && show==2) {
			if(isInputanBank == 2)
				desc = "TGL TERIMA ADMIN (INPUTAN BANK SINARMAS)";
			else if(isInputanBank == 3)
				desc = "TGL TERIMA ADMIN (INPUTAN SINARMAS SEKURITAS)";
			
			uwDao.updateMstInsuredTglAdmin(spaj, 1, tgl, 0);
			uwDao.saveMstTransHistory(spaj, "tgl_terima_spaj_admin", tgl, null, null);
			for(int i=0; i<1000; i++) {
				try {
					bacDao.insertMst_position_no_spaj_pb(spaj, user.getLus_id(),1, 10, desc,i);
					break;
				} catch (DataAccessException e) {
				}
			}
		}
				
	}
	/**Fungsi:	Untuk mendapatkan counter dari tabel EKA.MST_COUNTER berdasarkan nama cabang dan kode counternya.
	 * @param 	int aplikasi, String cabang
	 * @return	String
	 * @throws 	DataAccessException
	 * @author	Ferry Harlim
	 */
	public String selectGetCounter(int aplikasi, String cabang) throws DataAccessException {
		return uwDao.selectGetCounter(aplikasi, cabang);
	}
	
	public Command prosesStatusFormController(Command command, HttpServletRequest request, BindException err)throws Exception{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		command.setSpaj(request.getParameter("spaj"));
		String desc[] = new String[request.getParameterValues("textarea").length];
		String tanggal[] = new String[request.getParameterValues("tanggal").length];
		String status[];
		String statusAkhir;
		Integer lssaId;
		int suc = 0;

		desc = request.getParameterValues("textarea");
		tanggal = request.getParameterValues("tanggal");
		if (request.getParameterValues("status") != null) {
			status = new String[request.getParameterValues("status").length];
			status = request.getParameterValues("status");
			String temp= status[status.length - 1];
			statusAkhir = temp.substring(0, temp.indexOf("~"));
			lssaId=Integer.valueOf(temp.substring(temp.indexOf("~")+1, temp.indexOf("+")));
		} else {
			String temp = request.getParameter("temp");
			statusAkhir = temp.substring(0, temp.indexOf("~"));
			lssaId=Integer.valueOf(temp.substring(temp.indexOf("~")+1, temp.length()));
		}

		String deskripsi = desc[desc.length - 1];
		String tglAkhir = tanggal[tanggal.length - 1];
		deskripsi = deskripsi.toUpperCase();

		String lus_id = currentUser.getLus_id();
		int lspd = 2, lssp = 10;
		int liAktif = 1;
		String flagEmail = request.getParameter("flagEmail");
		String emailAdmin = uwDao.selectEmailAdmin(command.getSpaj());
		Map agen = uwDao.selectEmailAgen(command.getSpaj());
		String emailAgen = (String) agen.get("MSPE_EMAIL");
		//emailAgen = "ryan@sinarmasmsiglife.co.id";
		Integer msagActive = (Integer) agen.get("MSAG_ACTIVE");
		Integer msagSertifikat = (Integer) agen.get("MSAG_SERTIFIKAT");
		Date tglSertifikat = (Date) agen.get("MSAG_BERLAKU");
		String msagId = (String) agen.get("MSAG_ID");
		Date msagBegDate = (Date) agen.get("MSAG_BEG_DATE");
		//
		Map mGetStatus = (HashMap) uwDao.selectWf_get_status(
				command.getSpaj(), 1);
		BigDecimal statuAksep=(BigDecimal)mGetStatus.get("LSSA_ID");
		//
		if (command.getFlag_ut() != 0) {
			f_validasi validasi = new f_validasi();
			if (command.getFlag_ut() == 1 || command.getFlag_ut() == 3) {// agen
																			// kosong
				emailAgen = command.getEmailAgen();
				if (emailAgen == null || emailAgen.trim().equals(""))
					err.reject("", "Email Agen Tidak Boleh Kosong");
				else if (validasi.f_validasi_email(emailAgen) == false)
					err.reject("", "Silahkan Masukan Email Agen Dengan Benar");
			}
			if (command.getFlag_ut() == 2 || command.getFlag_ut() == 3) {
				emailAdmin = command.getEmailAdmin();
				if (emailAdmin == null || emailAdmin.trim().equals(""))
					err.reject("", "Email Admin Tidak Boleh Kosong");
				else if (emailAdmin == null
						|| validasi.f_validasi_email(emailAdmin) == false)
					err.reject("", "Silahkan Masukan Email Admin Dengan Benar");
			}
			if (err.getErrorCount() == 0){
				if(statusAkhir == null) statusAkhir = commonDao.selectStatusAcceptFromSpaj(command.getSpaj());
				suc = bacManager.kirimEmail(command.getSpaj(),lssaId, emailAdmin, emailAgen, statusAkhir,
						tglAkhir, currentUser, deskripsi, request
								.getContextPath(), msagId, msagSertifikat,
						tglSertifikat, msagBegDate, msagActive, command
								.getLcaIdPp(), command.getNamaPemegang(),
						command.getNamaTertanggung(), command, err, null);
				
				uwDao.updateMstInsuredStatus(command.getSpaj(),new Integer(1),command.getLiAksep(),liAktif,null,null);
				deskripsi = getKodeStatusAksep(command.getLiAksep()) + deskripsi;
				
				//bagian ini dicomment karena ada tambahan kolom sub_id di tabel mst_position_spaj.Jadi pake query yang dibawahnya
				//uwDao.insertMstPositionSpaj(lus_id, deskripsi, command.getSpaj());
				uwDao.insertMstPositionSpajWithSubId(lus_id, deskripsi, command.getSpaj(), command.getSubLiAksep());
				if(command.getLiAksep()==2) uwDao.saveMstTransHistory(command.getSpaj(), "tgl_decline", null, null, null);
				if(command.getLiAksep()==3) uwDao.saveMstTransHistory(command.getSpaj(), "tgl_further", null, null, null);
				if(command.getLiAksep()==9) uwDao.saveMstTransHistory(command.getSpaj(), "tgl_postpone", null, null, null);
			}
		} else {
			if (flagEmail.equals("1")) {// kirim email dengan menggunakan tombol
										// "Send Mail" secara manual
				// if(emailAdmin==null || emailAdmin.equals("")||emailAgen==null
				// || emailAgen.equals("")){
				// command.setFlag_ut(0);
				// err.reject("","Email Admin/Agen Tidak ada Silahkan Masukan
				// Email Admin Secara Manual");
				// }
				
				if(statusAkhir == null) statusAkhir = commonDao.selectStatusAcceptFromSpaj(command.getSpaj());
				
				suc = bacManager.kirimEmail(command.getSpaj(),lssaId, emailAdmin, emailAgen, statusAkhir,
						tglAkhir, currentUser, deskripsi, request
								.getContextPath(), msagId, msagSertifikat,
						tglSertifikat, msagBegDate, msagActive, command
								.getLcaIdPp(), command.getNamaPemegang(),
						command.getNamaTertanggung(), command, err, null);
				
			} else if (command.getLiAksep() != null
					&& command.getLiAksep().intValue() > 0) {
//				if (command.getLiAksep().intValue() == 3) {// lakukan proses
//															// further
//															// requirement dan
//															// kirim email
				
				if(statusAkhir == null) statusAkhir = commonDao.selectStatusAcceptFromSpaj(command.getSpaj());
				    if(command.getLiAksep()!=17){
					suc = bacManager.kirimEmail(command.getSpaj(),lssaId, emailAdmin, emailAgen, statusAkhir,
							tglAkhir, currentUser, deskripsi, request
									.getContextPath(), msagId, msagSertifikat,
							tglSertifikat, msagBegDate, msagActive, command
									.getLcaIdPp(), command.getNamaPemegang(),
							command.getNamaTertanggung(), command, err, null);
				    }
					uwDao.updateMstInsuredStatus(command.getSpaj(),new Integer(1),command.getLiAksep(),liAktif,null,null);
					deskripsi = getKodeStatusAksep(command.getLiAksep()) + deskripsi;
//					bagian ini dicomment karena ada tambahan kolom sub_id di tabel mst_position_spaj.Jadi pake query yang dibawahnya
					//uwDao.insertMstPositionSpaj(lus_id, deskripsi, command.getSpaj());
					uwDao.insertMstPositionSpajWithSubId(lus_id, deskripsi, command.getSpaj(), command.getSubLiAksep());
					if(command.getLiAksep()==2) uwDao.saveMstTransHistory(command.getSpaj(), "tgl_decline", null, null, null);
					if(command.getLiAksep()==3) uwDao.saveMstTransHistory(command.getSpaj(), "tgl_further", null, null, null);
					if(command.getLiAksep()==9) uwDao.saveMstTransHistory(command.getSpaj(), "tgl_postpone", null, null, null);
//				} else {// bukan futher requirement tidak kirim email automatis
//					uwDao.insertMstPositionSpaj(command.getSpaj(),lus_id,lspd,lssp,command.getLiAksep(),deskripsi);
//					uwDao.updateMstInsuredStatus(command.getSpaj(),new Integer(1),command.getLiAksep(),liAktif,null,null);
//				}
			}
		}
		//mengantisipasi user menggunakan menu status , untuk ubah status - Ryan
		if(!command.getLcaIdPp().equals("09")){
			if(command.getLiAksep()==5 || command.getLiAksep()==10){
				Integer mspo_provider= uwDao.selectGetMspoProvider(command.getSpaj());
				if(mspo_provider==2){
					uwDao.updateMstInsuredKirimAdmedika(command.getSpaj(),1,currentUser.getLus_id());
					uwDao.saveMstTransHistory(command.getSpaj(), "tgl_kirim_admedika", null, null, null);
				}
				uwManager.prosesEndorsemen(command.getSpaj(), Integer.parseInt(uwDao.selectBusinessId(command.getSpaj())),0);
			}
		}	
		
		command.setFlagAdd(suc);
		return command;
	}
	
	public List prosesLaporanReasRider(){
		String begDate="01/01/2005";
		String begDate2="31/12/2005";
		List lsReas=uwDao.selectAllRiderCheckReas(begDate, 810, 818);
		List lsHasil=new ArrayList();
		Integer count=1;
		for(int i=0;i<lsReas.size();i++){
			Temp data=(Temp)lsReas.get(i);
			ParameterClass param=uwDao.selectLstBisnis(data.getLsbs_id2());
			Integer liTbisnis=param.getLstb_id();
			Integer liTreins=param.getLstr_id();
			
			Double ldecValue=uwDao.selectLstLimitReinsuranceLsliValue(liTbisnis,liTreins,1,1,
					data.getLku_id(),data.getMste_medical(),data.getMste_age(),data.getMspr_beg_date(), data.getReg_spaj());
			if(ldecValue!=null)
			if(data.getMspr_tsi()>ldecValue){
				Double reas=data.getMspr_tsi()-ldecValue;
				Temp dataAdd=new Temp();
				dataAdd=data;
				data.setNumber(count);
				dataAdd.setRetensi(ldecValue);
				dataAdd.setReas(reas);
				Temp newData=uwDao.selectAllRiderCheckReasDetail(data);
				dataAdd.setMspo_policy_no(newData.getMspo_policy_no());
				dataAdd.setMcl_first_t(newData.getMcl_first_t());
				dataAdd.setLsdbs_name(newData.getLsdbs_name());
				lsHasil.add(dataAdd);
				count++;
			}
			
		}
		return lsHasil;
	}
	
	public List prosesLaporanReasRiderNew(){

		String begDate="01/03/2007";
		String endDate="31/07/2007";
		String begDateTime="01/03/2007 00:00:00";
		String endDateTime="31/07/2007 23:59:59";

		
		List lsReas=uwDao.selectAllRiderCheckReasNew(begDate,endDate,begDateTime,endDateTime,props.getProperty("product.unitLink"));
		List lsHasil=new ArrayList();
		Integer count=1;
		for(int i=0;i<lsReas.size();i++){
			Temp data=(Temp)lsReas.get(i);
			Client client =uwDao.selectMstClientNew(null, data.getMspo_policy_holder());
			data.setMcl_first_p(client.getMcl_first());
			data.setMspe_sex_p(client.getMspe_sex());
			data.setMspe_date_birth_p(client.getMspe_date_birth());
//			if(data.getReg_spaj().equals("04200500317") && data.getLsbs_id()==802)
//				JOptionPane.showMessageDialog(null, "tes");
			ParameterClass param=uwDao.selectLstBisnis(data.getLsbs_id2());
			Integer liTbisnis=param.getLstb_id();
			Integer liTreins=param.getLstr_id();
			/** untuk extra premi basic reas => reas 
			 * 		 extra >100%
			 *  basic non reas => extra non reas salah di jumlah yang di reasnya..
				nilai reas harus cekin klo dia treaty ato fakultative nilai reas 
				ambil dari dtabase.
			 *
			 */
			Double ldecValue=uwDao.selectLstLimitReinsuranceLsliValue(liTbisnis,liTreins,1,1,
					data.getLku_id(),data.getMste_medical(),data.getMste_age(),data.getMspr_beg_date(),data.getReg_spaj());
			if(ldecValue==null)
				ldecValue=new Double(0);
			Double reas;
			if(data.getMspr_tsi()<ldecValue)
				reas=new Double(0);
			else
				reas=data.getMspr_tsi()-ldecValue;
			Temp dataAdd=new Temp();
			dataAdd=data;
			data.setNumber(count);
			dataAdd.setRetensi(ldecValue);
			dataAdd.setReas(reas);
			boolean ins=true;
			if(data.getLsbs_id2()<300){
				
			}else if(data.getLsbs_id2()==811){//HCP
				count++;
				Double reasT=data.getMspr_tsi()/2;
				Double retensi=data.getMspr_tsi()/2;
				dataAdd.setRetensi(retensi);
				dataAdd.setReas(reasT);
			}else if(data.getLsbs_id2()>300){
				if(ldecValue>dataAdd.getMspr_tsi()){
					ins=false;
				}
			}
			
			if(ins){
				count++;
				dataAdd.setNo(count);
				lsHasil.add(dataAdd);
			}	
		
		}
	return lsHasil;
	}

	public List prosesLaporanReasRiderLinkNew(String tglAwal,String tglAkhir,Integer flag) {
		return reasUtilities.prosesLaporanReasRiderLinkNew(tglAwal,tglAkhir,flag);
	}

	/**Fungsi:	Untuk menampilkan data keterangan pada tabel EKA.MST_POSITION_SPAJ
	 * 			berdasarkan nospaj dan flag terdiri dari 3 buat keterangan
	 * 			flag(0)=untuk update tgl Terima spaj.
	 * 			flag(1)=untuk update tgl Kirim polis.
	 * 			flag(2)=untuk update tgl SPAJ DOC
	 * 
	 * @param	String spaj,Integer flag
	 * @return	String
	 * @author 	Ferry Harlim
	 */
	public String selectMstPositionSpajMspsDesc(String spaj, Integer flag){
		return uwDao.selectMstPositionSpajMspsDesc(spaj, flag);
	}
	public Date selectAddMonths(String tanggal,Integer month){
		return uwDao.selectAddMonths(tanggal, month);		
	}
	public List prosesCariCaraBayar()throws Exception{
		List lsHasil=new ArrayList();
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try{

			String fileName = "D://Ferry//Daily Jobs//27042007//NB_Unit Link_Rider HCP_kirim reas_0207_0307_CB.xls";
			Class.forName( "sun.jdbc.odbc.JdbcOdbcDriver" );
			connection = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=" + fileName);
			statement = connection.createStatement();
			String query = "Select REG_SPAJ from MST_POLICY";
			rs = statement.executeQuery( query );
			
			
			while(rs.next()){
				String spaj=rs.getString(1);
				Integer lscbId=uwDao.selectCaraBayarFromSpaj(spaj);
				Map map=new HashMap();
				map.put("REG_SPAJ", spaj);
				map.put("LSCB_ID",lscbId);
				map.put("LSCB_PAY_MODE", uwDao.selectLstPayMode(lscbId));
				lsHasil.add(map);
			}	
		}catch (Exception e) {
			logger.error("ERROR :", e);
		}finally {
			try {
				if(rs!=null){
					rs.close();
				}
				if(statement!=null){
					statement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("ERROR :", e);
			}
		}
		return lsHasil;
	}
	
	/**Fungsi:	Untuk mengupdate data reas yang tidak terReas oleh sistem
	 * 			Proses pengupdate Manual dari hasil seleksi report yang di berikan oleh Aktuaria(Rena)
	 * 			NEW : Data sekarang di ambil dari table eka.m_reas_Temp_new
	 * @throws Exception
	 * @author Ferry Harlim
	 */
	public Map prosesInsertReasRiderLinkToReins()throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return reasUtilities.prosesInsertReasRiderLinkToReins();
	}
	
//	private Integer selectCountYear(Integer lsbsIdUtama,Integer lsdbsIdUtama,Integer lsbsIdtam,Integer lsdbsNumberTam,Integer li_ins_age,Integer) {
//		Integer li_contyear;
////		hitung contrak year
//		if(lsbsIdUtama.intValue()==51 || lsbsIdUtama.intValue()==61){
//			li_contyear = 59 - li_ins_age;
//			if (lsbsIdtam.intValue()==600|| lsbsIdtam.intValue()==601 )
//				li_contyear = new Integer(10);
//		}else if(lsbsIdUtama.intValue()==52 || lsbsIdUtama.intValue()==62||lsbsIdUtama.intValue()==78){
//			li_contyear = new Integer(59 - li_ins_age);
//			if(lsbsIdtam.intValue()==600|| lsbsIdtam.intValue()== 601) 
//				li_contyear = new Integer(10);
//			if (lkuId.equals("02")){
//				if(li_bisnis[1][1]==new Integer(52))
//					li_contyear = new Integer(60 - li_ins_age);
//				else if( li_bisnis[3][1].compareTo(new Integer(600))==0)
//					li_contyear = new Integer(65 - li_ins_age);
//				
//			}
//		}else if(lsbsIdTemp==47 ||lsbsIdTemp==50||lsbsIdTemp==55||lsbsIdTemp==58){
//			li_contyear=new Integer(5);
//			if(li_bisnis[3][1]==600)
//				li_contyear=new Integer(5);
//		}else if(lsbsIdTemp==56 ||lsbsIdTemp==64||lsbsIdTemp==68||lsbsIdTemp==75){
//			li_contyear=new Integer(4);
//			if(li_bisnis[3][1]==600)
//				li_contyear=new Integer(8);
//		}else if(lsbsIdTemp==65){
//			li_contyear=new Integer(79-li_ins_age);
//			if(li_bisnis[3][1]==600 || li_bisnis[8][1]==807 || li_bisnis[8][1]==808)
//				li_contyear = new Integer(59 - li_ins_age);
//			else if (li_bisnis[5][1] == 601 )
//				li_contyear = new Integer(59 - li_ins_age);
//				if (li_contyear > 20)
//					li_contyear = new Integer(0);
//			else if (li_bisnis[7][1] == 806)
//				li_contyear = new Integer(5); 
//			
//		}else if(lsbsIdTemp==66){
//			li_contyear = new Integer(55 - li_ins_age);
//			if (li_bisnis[3][1] == 600) 
//				li_contyear = new Integer(55 - li_ins_age);
//			else if(li_bisnis[7][1] == 806 ){
//				li_contyear = new Integer(10);
//				if (li_contyear + li_ins_age > 55 )
//					li_contyear = new Integer(55 - li_ins_age);
//			}
//		}else if(lsbsIdTemp==69){
//			if (li_bisnis[1][2] == 1){
//				li_contyear = new Integer(5);
//				if (li_bisnis[3][1] == 600)
//					li_contyear = new Integer(8);
//			}else if (li_bisnis[1][2] == 2){
//				li_contyear = new Integer(7);
//				if (li_bisnis[3][1] == 600)
//					li_contyear = new Integer(10);
//			}else{
//				li_contyear = new Integer(9);
//				if(li_bisnis[3][1] == 600)
//					li_contyear = new Integer(12);
//			}
//		}else if(lsbsIdTemp== 74 || lsbsIdTemp==76){
//			li_contyear = new Integer(5);
//			if (li_bisnis[3][1] == 600 )
//				li_contyear = new Integer(8);
//		}else if(lsbsIdTemp==77 ||lsbsIdTemp== 84||
//				lsbsIdTemp==100||lsbsIdTemp==102){
//			li_contyear = new Integer(6);
//			if (li_bisnis[3][1]== 600)
//				li_contyear = new Integer(18);
//		}else{
//			li_contyear = insPeriod;
//		} 
//	}


	/* ======================= BasDao ======================= */

	public Agen selectAgentByMsagId(String msag_id){
		return basDao.selectAgentByMsagId(msag_id);
	}	
	
	public List<DropDown> selectLokasiAdmin(String lus_id) {
		return basDao.selectLokasiAdmin(lus_id);
	}
	
	public List<FormSpaj> selectFormSpaj(String msf_id, String lca_id, String lus_id) {
		return basDao.selectFormSpaj(msf_id, lca_id, lus_id);
	}
	
	public List<FormSpaj> selectDaftarFormSpaj(FormSpaj formSpaj) {
		return basDao.selectDaftarFormSpaj(formSpaj);
	}
	
	public List<FormSpaj> selectDaftarFormCari(String msf_id) {
		return basDao.selectDaftarFormCari(msf_id);
	}
	
	public List<Spaj> selectStokSpaj(Spaj spaj) {
		return basDao.selectStokSpaj(spaj);
	}
	public List<Spaj> selectStokSpajAgen(Spaj spaj) {
		return basDao.selectStokSpajAgen(spaj);
	}
	
	public List<Spaj> selectStokBlanko(Spaj spaj) {
		return basDao.selectStokBlanko(spaj);
	}
	
	public List<FormHist> selectFormHistory(String msf_id) {
		return basDao.selectFormHistory(msf_id);
	}

	public FormHist selectFormHistory(FormHist formHist) {
		return basDao.selectFormHistory(formHist);
	}

	public List<Region> selectUserAdminRegion(int lus_id) {
		return basDao.selectUserAdmin(lus_id);
	}
	
	public List<Agen> selectAgentFromRegion(String lca_lwk_lsrg) {
		return basDao.selectAgentFromRegion(lca_lwk_lsrg);
	}
	
	
	public List<Agen> selectAllAgentFromRegion(int lus_id) {
		return basDao.selectAllAgentFromRegion(lus_id);
	}
	
	public List<Agen> selectAgentBranchFromRegion(String lca_lwk_lsrg) {
		return basDao.selectAgentBranchFromRegion(lca_lwk_lsrg);
	}
	
	public List<Agen> selectAllAgentBranchFromRegion(int lus_id) {
		return basDao.selectAllAgentBranchFromRegion(lus_id);
	}
	
	public Integer selectCekSpaj(FormSpaj s) {
		return basDao.selectCekSpaj(s);
	}
	
	public Integer selectSisaSpaj(FormSpaj s) {
		return basDao.selectSisaSpaj(s);
	}
	
	public List<FormSpaj> selectHistoryAgentAllRegion(int lus_id) {
		return basDao.selectHistoryAgentAllRegion(lus_id);
	}
	
	public List<FormSpaj> selectHistoryAgent(int lus_id, String lca_lwk_lsrg) {
		return basDao.selectHistoryAgent(lus_id, lca_lwk_lsrg);
	}

	public List<FormSpaj> selectWarning30Hari(int lus_id) {
		return basDao.selectWarning30Hari(lus_id);
	}
	
	public List<SpajDet> selectPertanggungjawaban(String msf_id) {
		return basDao.selectPertanggungjawaban(msf_id);
	}
	
	public List<SpajDet> selectUpdatePertanggungjawaban(String lusId, String msagId, String msabId) {
		return basDao.selectUpdatePertanggungjawaban(lusId,msagId,msabId);
	}	
	
	public List<DropDown> selectJenisPertanggungjawaban() {
		return basDao.selectJenisPertanggungjawaban();
	}
	
	public HashMap selectSpajFromBlanko(String no_blanko) {
		return basDao.selectSpajFromBlanko(no_blanko);
	}
	
	public Integer selectCekPosisiFormSpaj(String msf_id) {
		return basDao.selectCekPosisiFormSpaj(msf_id);
	}
	
	public List<Map> selectJenisSpaj() {
		return basDao.selectJenisSpaj();
	}
	
	public List<Map> selectCekBlankoDiAgen(Integer lsjs_id, String start_no_blanko, String end_no_blanko) {
		return basDao.selectCekBlankoDiAgen(lsjs_id, start_no_blanko, end_no_blanko);
	}
	
	public List<FormSpaj> selectDetailBlankoCabang(String lca_id) {
		return basDao.selectDetailBlankoCabang(lca_id);
	}
	
	public String processFormSpaj(CommandControlSpaj cmd, User currentUser) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		if("save".equals(cmd.getSubmitMode())) {
			return basDao.processNewFormSpaj(cmd, currentUser);
		}else if("cancel".equals(cmd.getSubmitMode())) {
			return basDao.processCancelFormSpaj(cmd, currentUser);
		}else if("approve".equals(cmd.getSubmitMode())) {
			return basDao.processApprovalFormSpaj(cmd, currentUser);
		}else if("reject".equals(cmd.getSubmitMode())) {
			return basDao.processRejectFormSpaj(cmd, currentUser);
		}else if("send".equals(cmd.getSubmitMode())) {
			return basDao.processSendFormSpaj(cmd, currentUser);
		}else if("received".equals(cmd.getSubmitMode())) {
			return basDao.processReceiveFormSpaj(cmd, currentUser);
		}else if("save_agen".equals(cmd.getSubmitMode())) {
			return basDao.processNewFormSpajAgen(cmd, currentUser);
		}else if("save_tgjwb".equals(cmd.getSubmitMode())) {
			return basDao.processPertanggungJawabanFormSpajAgen(cmd, currentUser);
		}/*else if("fitrah_kembali".equals(cmd.getSubmitMode())) {
			return basDao.processFitrahKembaliKePusat(cmd,currentUser);	
		}*/
		return "ERROR";
	}
	/**
	 * @param spaj
	 * @param insured
	 * @return
	 * @author Ferry Harlim
	 */
	public List selectMstKeluarga(String spaj, Integer insured)
	{
		return basDao.selectMstKeluarga(spaj, insured);
	}
	/**
	 * @param noIdentity
	 * @param nopolis
	 * @return
	 * @throws DataAccessException
	 * @author Ferry Harlim
	 */
	public List selectAllPolicy(String mclId,String nopolis) throws DataAccessException{
		return basDao.selectAllPolicy(mclId, nopolis);
	}
	public Integer prosesSimpanSlipPemotonganPolis(Deduct deduct,String ubah,User currentUser){
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return transferPolis.prosesSimpanSlipPemotonganPolis(deduct,ubah,currentUser);
	}
	/**Fungsi:	Untuk Menampilkan data pada tabel eka.mst_deduct
	 * @param	String msco_id
	 * @author 	Ferry Harlim
	 */
	public Deduct selectMstDeduct(String mscoId,Integer msddNumber){
		return uwDao.selectMstDeduct(mscoId, msddNumber);
	}
	
	public String processFormBlanko(CommandControlSpaj cmd, User currentUser) {
		if("save".equals(cmd.getSubmitMode())) {
			return blankoDao.processNewFormSpaj(cmd, currentUser);
		}else if("cancel".equals(cmd.getSubmitMode())) {
			return blankoDao.processCancelFormSpaj(cmd, currentUser);
		}else if("approve".equals(cmd.getSubmitMode())) {
			return blankoDao.processApprovalFormSpaj(cmd, currentUser);
		}else if("reject".equals(cmd.getSubmitMode())) {
			return blankoDao.processRejectFormSpaj(cmd, currentUser);
		}else if("send".equals(cmd.getSubmitMode())) {
			return blankoDao.processSendFormSpaj(cmd, currentUser);
		}else if("received".equals(cmd.getSubmitMode())) {
			return blankoDao.processReceiveFormSpaj(cmd, currentUser);
		}else if("simpan".equals(cmd.getSubmitMode())) {
			return blankoDao.processSimpanFormSpaj(cmd, currentUser);
		}else if("batal".equals(cmd.getSubmitMode())) {
			return blankoDao.processBatalFormSpaj(cmd, currentUser);
		}
		return "ERROR";
	}
	
	/**@Fungsi:	Untuk melakukan proses akseptasi produk ASM 
	 * @param 	String spaj,String begDate,String endDate
	 * @author	Ferry Harlim
	 */
	public int prosesAkseptasiAsm(String spaj,String begDate,String endDate,User currentUser,String nopolis_asm){
		return uwDao.prosesAkseptasiAsm(spaj, begDate, endDate,currentUser,nopolis_asm);
	}
	
	public Integer selectCountMstPeserta(String spaj)throws DataAccessException{
		return uwDao.selectCountMstPeserta(spaj);
	}
	
	public Integer selectCountMstProductInsuredRider(String spaj, Integer lsbsId)throws DataAccessException{
		return uwDao.selectCountMstProductInsuredRider(spaj, lsbsId);
	}


		
	/**Fungsi: Untuk menampilkan bahwa spaj yang ada di cabang telah di pertanggungjawabkan
	 * @param 	String msf_id,Integer msab_id, , String lca_id
	 * @return	Integer
	 * @author	Ferry Harlim
	 */
	public Integer selectJumlahSpajPerTgJbwn(Integer msab_id, String lca_id)throws DataAccessException{
		return basDao.selectJumlahSpajPerTgJbwn(msab_id,lca_id);
	}
	
	/**Fungsi: 	Untuk Menampilkan detail sisa stok (spaj) di cabang
	 * @param	String lcaId, Integer mssJenis, Integer lsjsId 
	 * @return 	List
	 * @author	Ferry Harlim
	 */
	public List selectDetailStokCabang(String lcaId, Integer mssJenis, Integer lsjsId, Integer lspId,Integer lusId, String no_blanko, Integer trav_ins_type){
		return basDao.selectDetailStokCabang(lcaId, mssJenis, lsjsId, lspId, lusId, no_blanko,trav_ins_type);
	}
	/**Fungsi:	Untuk Menampilkan jenis spaj
	 * @param	Integer lsjsId
	 * @return	java.util.HashMap
	 * @author 	Ferry Harlim
	 */
	public Map selectLstJenisSpaj(Integer lsjsId){
		return basDao.selectLstJenisSpaj(lsjsId);
	}
	
	/**Fungsi:	Untuk Menampilkan msf_id yang akan dipertanggungjawabkan berdasarkan kode cabang 
	 * 			dan kode agen.
	 * @param	String msagId, String lcaId
	 * @return	String
	 * @author 	Ferry Harlim
	 */
	public String selectMsfIdPertanggungjawaban(String msagId, String lcaId){
		return basDao.selectMsfIdPertanggungjawaban(msagId, lcaId);
	}
	/**Fungsi:	Untuk Menampilkan msf_id sebelumnya. 
	 * 			
	 * @param	String msagId, String lcaId
	 * @return	String
	 * @author 	Ferry Harlim
	 */
	public String selectMsfIdBef(Integer msab_id){
		return basDao.selectMsfIdBef(msab_id);
	}
	/**Fungsi:	Untuk Menampilkan data form spaj 
	 * @param	String msfId
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectSearchFormSpajExpress(String msfId){
		return basDao.selectSearchFormSpajExpress(msfId);
	}
	/**Fungsi:	Untuk Menampilkan data form spaj 
	 * @param	FormSpaj formSpaj
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectSearchFormSpajDetail(FormSpaj formSpaj){
		return basDao.selectSearchFormSpajDetail(formSpaj);
	}
	/**Fungsi:	Untuk Menampilkan data agent per cabang 
	 * @param	Agen agen
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectAllAgentFromBranch(Agen agen){
		return basDao.selectAllAgentFromBranch(agen);
	}
	
	/**Fungsi:	Untuk Mengupdate status agen 
	 * 			0 = Ok
	 * 			1 = SP-1
	 * 			2 = SP-2
	 * 			3 = SP-3 
	 * @param	Agen agen
	 * @author 	Ferry Harlim
	 */
	public void updateMstAgentStatus(Agen agen){
		basDao.updateMstAgentStatus(agen);
	}
	
	/**Fungsi:	Untuk Menampilkan Daftar user dari masing2 cabang 
	 * @param	String lcaid
	 * @author 	Ferry Harlim
	 */
	public List selectAllUserInCabang(String lcaId){
		return basDao.selectAllUserInCabang(lcaId);
	}
	
	public List<DropDown> selectJenisForm() {
		return basDao.selectJenisForm();
	}
	
	public List selectAllUserRegional(String lcaId){
		return basDao.selectAllUserRegional(lcaId);
	}
	
	public List selectAllUserWorksite(String lcaId){
		return basDao.selectAllUserWorksite(lcaId);
	}
	/*
	 * untuk menampilkan list user agency
	 * dian natalia
	 */
	public List selectAllUserAgency(){
		return basDao.selectAllUserAgency();
	}
	
	public void setReasIndividu(ReasIndividu reasIndividu) {
		this.reasIndividu = reasIndividu;
	}
	
	/**Fungsi : Untuk Upload data client history data pengkinian
	 * 
	 * @param in
	 * @param currentUser
	 * @throws Exception
	 * @author Ferry Harlim
	 */
	public Integer prosesUploadClientHistory(InputStream in,User currentUser)throws Exception {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return uploadBac.prosesUploadClientHistory(in,currentUser);
	}
	
	public Integer prosesMissingUpdateDataVendor(InputStream in, String lusId) throws IOException {
		return uploadBac.prosesUploadClientHistory(in,lusId);
	}

//    public List selectEkpedisiSpaj( String tanggalAwal, String tanggalAkhir, List< String > codeToPrintList )
//    {
//        return basDao.selectEkpedisiSpaj( tanggalAwal, tanggalAkhir, codeToPrintList );
//    }
    public List selectEkpedisiSpaj( String tanggalAwal, String tanggalAkhir, String[] codeToPrintList )
    {
        return basDao.selectEkpedisiSpaj( tanggalAwal, tanggalAkhir, codeToPrintList );
    }
    /**
     * Dian natalia
     * @param value
     * @param tipe
     * @param filter
     * @param lcaId
     * @return
     */
    public List selectAllMstBtpp(String value,String tipe,String filter,String lcaId){
		return basDao.selectAllMstBtpp(value,tipe,filter,lcaId);
	}

    /*
     * 
     * Dian natalia
     */
    public List selectLstHistoryPrintBtpp(String mstNo){
		return basDao.selectLstHistoryPrintBtpp(mstNo);
	}

    /*
     * Dian natalia
     */
//    public List selectMstHistoryBtpp(String mstNo){
//		return basDao.selectMstHistoryBtpp(mstNo);
//	}
    
    public List selectMstPolicyBtppNopolis(String mstNo){
		return basDao.selectMstPolicyBtppNopolis(mstNo);
	}
    
    public List selectMstCaraByrBtpp(String nomor){
		return basDao.selectMstCaraByrBtpp(nomor);
	}
   
    public List selectMstPolicyBtpp(String nomor,String kurs){
		return basDao.selectMstPolicyBtpp(nomor,kurs);
	}
    
    public Date selectMstBtppTglSetor(String mstNo){
		return basDao.selectMstBtppTglSetor(mstNo);
	}
    
    public List selectMstProduct_Insured(String no_spaj){
		return basDao.selectMstProduct_Insured(no_spaj);
	}
   
    public String prosesInputMstBtpp(CommandBtpp command, String lusId){
    	SimpleDateFormat sdf = new SimpleDateFormat("20MMyyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
//		Date sysdate = selectSysdate("dd", false, 0);
//		Date begdate = selectSysdate("dd", true, 0);
		Date today=commonDao.selectSysdate();
    	String nomor=null;
    	
    	Btpp btpp = command.getBtpp();
    	Integer flagBatal= new Integer(0);
    	Integer mscoNumber=1;
    	if (command.getProses().intValue() == 1 || command.getProses().intValue()==4){		
    		Map mapCounter= basDao.selectGetCounterBtpp(mscoNumber,btpp.getLst_kd_cab());
			String formatCounter=(String)mapCounter.get("FORMAT_COUNTER");
			String monthYear=(String)mapCounter.get("MSCO_MONT_YEAR");
			BigDecimal counter=(BigDecimal)mapCounter.get("COUNTER");

			
			nomor=formatCounter+monthYear+btpp.getLst_kd_cab()+"IE";
			
			basDao.updateMstCounterBtpp(new Double(counter.intValue()),mscoNumber,btpp.getLst_kd_cab());

			btpp.setMst_no(nomor);
			btpp.setKd_agen(command.getBtpp().getMsag_id());
			btpp.setNm_penutup(command.getBtpp().getMcl_first());
			btpp.setMst_ket("Bukti Pembayaran Premi Pertama");
			btpp.setMst_no_btpp(command.getMst_no());
			btpp.setPrde_byr_awal(FormatDate.toDate(command.getSprde_byr_awal().replace("/", "")));
			btpp.setPrde_byr_akhr(FormatDate.toDate(command.getSprde_byr_akhr().replace("/", "")));
			btpp.setBiaya_polis(command.getBtpp().getBiaya_polis());
			btpp.setReg_spaj(command.getBtpp().getReg_spaj());
			
			btpp.setExtra_premi(command.getBtpp().getExtra_premi());
			btpp.setMst_nm_pemegang(command.getBtpp().getNama().toUpperCase());
			btpp.setMst_tglsetor(FormatDate.toDate(command.getS_tgl_rk().replace("/", "")));  
			btpp.setFlag_print(new Integer(0));
			btpp.setLus_id(Integer.valueOf(lusId));
			btpp.setTot_byr(btpp.getPremi()+ btpp.getExtra_premi()+ btpp.getBiaya_polis());
			btpp.setMst_flag_batal(0);
//			//JOptionPane.showMessageDialog(null, ""+nomor);
			btpp.setMst_tgl_input(today);
			
			basDao.insertMstBtpp(btpp);
			if(command.getProses().intValue()==4){//batal
				
				basDao.updateMstBtppFlagbatal(nomor);
				basDao.insertLstHistoryPrint(btpp.getMst_no_reff_btl(), null,btpp.getLst_kd_cab() ,"Proses input ulang", nomor, 5 );
				btpp.setMst_no("");
				btpp.setKd_agen("");
				btpp.setNm_penutup("");				
				btpp.setReg_spaj("");
				btpp.setMst_nm_pemegang("");
				btpp.setSprde_byr_awal("00/00/0000");
				btpp.setSprde_byr_akhr("00/00/0000");
				
				
				if(btpp.getMst_flag_batal()==5){
					mapCounter= basDao.selectGetCounterBtpp(mscoNumber,btpp.getLst_kd_cab());
					formatCounter=(String)mapCounter.get("FORMAT_COUNTER");
					monthYear=(String)mapCounter.get("MSCO_MONT_YEAR");
					counter=(BigDecimal)mapCounter.get("COUNTER");

					
					String nomorNew=formatCounter+monthYear+btpp.getLst_kd_cab()+"IE";
					
					basDao.updateMstCounterBtpp(new Double(counter.intValue()),mscoNumber,btpp.getLst_kd_cab());
					
					btpp.setMst_no(nomorNew);
					btpp.setKd_agen(command.getBtpp().getMsag_id());
					btpp.setNm_penutup(command.getBtpp().getMcl_first());
					btpp.setMst_ket("Bukti Pembayaran Premi Pertama");
					btpp.setMst_no_btpp(command.getMst_no());
					btpp.setPrde_byr_awal(FormatDate.toDate(command.getSprde_byr_awal().replace("/", "")));
					btpp.setPrde_byr_akhr(FormatDate.toDate(command.getSprde_byr_akhr().replace("/", "")));
					btpp.setBiaya_polis(command.getBtpp().getBiaya_polis());
					btpp.setReg_spaj(command.getBtpp().getReg_spaj());
					
					btpp.setExtra_premi(command.getBtpp().getExtra_premi());
					btpp.setMst_nm_pemegang(command.getBtpp().getNama().toUpperCase());
					btpp.setMst_tglsetor(FormatDate.toDate(command.getS_tgl_rk().replace("/", "")));  
					btpp.setFlag_print(new Integer(0));
					btpp.setLus_id(Integer.valueOf(lusId));
					btpp.setTot_byr(btpp.getPremi()+ btpp.getExtra_premi()+ btpp.getBiaya_polis());
					btpp.setMst_flag_batal(0);
//					//JOptionPane.showMessageDialog(null, ""+nomor);
					btpp.setMst_tgl_input(today);
					basDao.insertMstBtpp(btpp);
				}
				
			}
    	}else if(command.getProses().intValue()==2){//edit
    		
			nomor=command.getMst_no();

			basDao.deleteMstBtpp(nomor);
		
			btpp.setMst_no(nomor);
			btpp.setKd_agen(command.getBtpp().getMsag_id());
			btpp.setNm_penutup(command.getBtpp().getMcl_first());
			btpp.setMst_ket("Bukti Pembayaran Premi Pertama");
			btpp.setMst_no_btpp(command.getMst_no());
			btpp.setPrde_byr_awal(FormatDate.toDate(command.getSprde_byr_awal().replace("/", "")));
			btpp.setPrde_byr_akhr(FormatDate.toDate(command.getSprde_byr_akhr().replace("/", "")));
			btpp.setBiaya_polis(command.getBtpp().getBiaya_polis());
			btpp.setReg_spaj(command.getBtpp().getReg_spaj());
			btpp.setExtra_premi(command.getBtpp().getExtra_premi());
			btpp.setMst_nm_pemegang(command.getBtpp().getNama().toUpperCase());
			btpp.setMst_tglsetor(FormatDate.toDate(command.getS_tgl_rk().replace("/", "")));  
			btpp.setFlag_print(new Integer(0));
			btpp.setLus_id(Integer.valueOf(lusId));
			btpp.setTot_byr(btpp.getPremi()+ btpp.getExtra_premi()+ btpp.getBiaya_polis());
			btpp.setMst_no_reff_btl(command.getMstNoBatal());
			btpp.setMst_flag_batal(flagBatal);
			btpp.setMst_tgl_input(today);
			btpp.setMsag_id(btpp.getKd_agen());
			btpp.setMcl_first(btpp.getNm_penutup());
			btpp.setS_tgl_rk(command.getS_tgl_rk());
			btpp.setSprde_byr_awal(command.getSprde_byr_akhr());
			btpp.setSprde_byr_akhr(command.getSprde_byr_akhr());
			basDao.insertMstBtpp(btpp);
    	}
    		
    	return nomor;
    }

    public List selectMstBtpp(String value,String tipe,String filter,String lcaId){
		return basDao.selectMstBtpp(value,tipe,filter,lcaId);
	}
    
    public List lsSelectAgent(String kd_agen){
		return basDao.lsSelectAgent(kd_agen);
	}
    
    public String selectAgenTemp() {
		return (String) this.basDao.selectAgenTemp();
	}
    
    public String selectMstNoNew(String mst_no){
    	return basDao.selectMstNoNew(mst_no);
    }

    public List lsSelectAgenTemp(){
    	return (List)this.basDao.lsSelectAgenTemp();
    }
    /**
     * untuk cetak BTPP
     * @param tts
     * @param flag
     * @param desc
     * 
     */
//	public void prosesCetakBtpp(Btpp btpp,int flag,String desc){
//		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//		if(flag==1){//proses cetak untuk pertama kali
//			basDao.updateMstBtppFlagPrint(btpp.getMst_no(),new Integer(1));
//			basDao.insertLstHistoryPrint(new Integer(1),btpp.getMst_no(),btpp.getLst_kd_cab(),desc);
//		}else if(flag==2){//proses cetak selanjutnya
//			Integer no=basDao.selectMaxLstHistoryPrintBtpp(btpp.getMst_no());
//			basDao.insertLstHistoryPrint(new Integer(no.intValue()+1),btpp.getMst_no(),btpp.getLst_kd_cab(),desc);
//		}
//	}
	
	/**Fungsi	:Untuk proses update data salah status reas di tabel 
	 * 			 EKA.MST_INSURED (MSTE_BACKUP,MSTE_REAS)
	 * 			 EKA.M_REAS_TEMP (MSTE_REAS)
	 * @param mste_reas
	 * @param mste_backup
	 */
	public Integer prosesUpdateStatusMstInsured(Integer mste_reas, Integer mste_backup) {
		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		List lsDaftarSpaj=commonDao.selectRegSpajFromNoPolis();
		String regSpaj;
		Integer count=0;
		for(int i=0;i<lsDaftarSpaj.size();i++){
			regSpaj=(String)lsDaftarSpaj.get(i);
			uwDao.updateMstInsuredReasnBackup(regSpaj, 1, mste_reas, mste_backup, null, null);
			uwDao.updateMReasTempMsteReas(regSpaj, mste_reas, null);
			uwDao.deleteMstReins(regSpaj);
			count++;
		}
		return count;
	}
	
	/**Fungsi: Untuk update proses akseptasi khusus.
	 * 
	 * @param spaj
	 * @param lspdId
	 * @param lssaId
	 * @param insuredNo
	 * @param lusId
	 * @param lsspId
	 * @param desc
	 */
	public void prosesUpdateAkseptasiKhusus(String spaj,Integer lspdId,Integer lssaId,Integer insuredNo,String lusId,
			Integer lsspId,String desc) throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		try{
			uwDao.updateMstInsured(spaj, null, lssaId, insuredNo, new Date());//null gak usah di update posisi
			uwDao.insertMstPositionSpaj(lusId, desc, spaj, 0);
			Pemegang pemegang = new Pemegang();
			DateFormat df = new SimpleDateFormat("ddMMyyyy");
			pemegang = selectpp(spaj);
			Smsserver_out sms_out = new Smsserver_out();
			sms_out.setRecipient(pemegang.getNo_hp()!=null?pemegang.getNo_hp():pemegang.getNo_hp2());
//			sms_out.setText("Yth."+(pemegang.getMspe_sex()==1?"bpk. ":"ibu. ")+ pemegang.getMcl_first()+", Polis No "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+" telah selesai di akseptasi dan akan disampaikan oleh Team Sales kami.Silahkan mengakses HTTPS://EPOLICY.SINARMASMSIGLIFE.CO.ID untuk melihat data polis, User Name : "+pemegang.getMspe_no_identity()+", Password : "+df.format(pemegang.getMspe_date_birth())+". Utk info hub CS di 021-26508300.Apabila ada perubahan data,SMS ke 0812 1111 880 - 0855 8079 403");
			sms_out.setText("Nasabah Yth, Polis No."+FormatString.nomorPolis(pemegang.getMspo_policy_no())+" diakseptasi, cek http://epolicy.sinarmasmsiglife.co.id, user id:nmr ktp,pass:tgl lahir(ddmmyyyy).Info CS 021-50609999");
			sms_out.setLjs_id(25);
////			sms_out.setText("Yth."+(pemegang.getMspe_sex()==1?"Bpk. ":"Ibu. ")+ pemegang.getMcl_first()+", Polis No "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+" telah selesai dan akan  disampaikan oleh Team Sales kami. Cek di E-Policy User Name :<no identitas>(no.KTP), Password <pass>. Utk info hub CS di 021-26508300. Apabila ada perubahan No. Telepon, HP dan alamat koresponden / penagihan, silahkan SMS ke nomor 0812 1111 880 – 0855 8079 403.");
//			String pesan = "Diberitahukan Bahwa Polis Anda dengan Nomor "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+" saat ini telah selesai dan akan  disampaikan oleh Team Sales kami.</br>Terima Kasih Atas Kepercayaan Anda untuk Bergabung bersama PT Asuransi Jiwa Sinarmas MSIG Tbk.";
//			if(pemegang.getMspe_email()!=null){
//				email.sendImageEmbeded(
//						true,"policy_service@sinarmasmsiglife.co.id", new String[] {pemegang.getMspe_email()} ,
//						null,
//	//					new String[] {"ingrid@sinarmasmsiglife.co.id","Rachel@sinarmasmsiglife.co.id","Hayatin@sinarmasmsiglife.co.id","Fouresta@sinarmasmsiglife.co.id","Dinni@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id"},
//						new String[] {props.getProperty("admin.deddy")},  
//						"Pemberitahuan Proses Pengajuan Polis", 
//						"Kepada Yth.</br>"+
//						(pemegang.getMspe_sex()==1?"Bpk. ":"Ibu. ")+ pemegang.getMcl_first()+"</br></br>"+
//						pesan, null,null);
			// Disable sms pada channel DMTM,BANCASS,MNC & WS (req wahdahniyah hd #61721) - Daru 15 Dec 2014
			String lca_id = selectLcaIdBySpaj(spaj);
			if("09,40,42,44,45,56,59,62,67,69".indexOf(lca_id) == -1)
				basDao.insertSmsServerOutWithGateway(sms_out, true);
//			}else{
//				basDao.insertSmsServerOutWithGateway(sms_out, true);
//			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
    /**
     * 
     public void editTglSetor(CommandTts command,String lusId){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		financeDao.updateMstTtsTglSetor(command.getMst_no(), command.getTglSetor());
		financeDao.insertLstHistoryTts(command.getMst_no(),  Integer.valueOf(lusId), command.getDesc());
		
	}
     * @param command
     * @param lusId
     */
	public List selectViewerBtpp(Map map){
		String lcaId,lusId,flagPrint;
		lcaId=(String) map.get("Cabang");
		lusId=(String)map.get("NamaAdmin");
		flagPrint=(String)map.get("Print");
		return basDao.selectViewerBtpp(lusId, lcaId, flagPrint);
	}
	
	public void updateMstBtppFlagbatal(String mstNo){
		basDao.updateMstBtppFlagbatal(mstNo);
	}
	
	public void updateMstBtppFlagbatal1(String mstNo){
		basDao.updateMstBtppFlagbatal1(mstNo);
	}
	
	public void updateProsesKyc2(String spaj, Integer insured,String lusId,
			Integer mste_flag_Yuw, Date mste_kyc_date) {
		mste_kyc_date=commonDao.selectSysdate();
		uwDao.updateProsesKyc(spaj, insured, lusId, mste_flag_Yuw, mste_kyc_date);
		
	}
	public void updateProsesKycResultKyc(String spaj, Integer insured,String lusId,
			String desc, Date mste_kyc_date) {
		mste_kyc_date=commonDao.selectSysdate();
		uwDao.updateProsesKycResultKyc(spaj, insured, lusId, desc, mste_kyc_date);
	}
	public Integer selectflagBatal(String mst_no){
		return basDao.selectflagBatal(mst_no);
	}
	public String selectTglRk(String mst_no){
		return basDao.selectTglRk(mst_no);
	}
	
	public String selectperiodeAwal(String mst_no){
		return basDao.selectperiodeAwal(mst_no);
	}
	
	public String selectperiodeAkhr(String mst_no){
		return basDao.selectperiodeAkhr(mst_no);
	} 
	
	public Integer selectCountHist(String mst_no){
		return basDao.selectCountHist(mst_no);
	}
	
	
	public void insertLstHistoryPrint(String mst_no,String mst_tgl,String kode_cabang,String alasan_batal, String mst_no_new,Integer flg_btl){
		this.basDao.insertLstHistoryPrint(mst_no, mst_tgl, kode_cabang, alasan_batal,  mst_no_new, flg_btl);
	
	}
//	public Map insertHstMstBtpp(String mst_no,String mst_tgl,String kode_cabang,String alasan_batal, String mst_no_new){
//		return (HashMap) basDao.insertHstMstBtpp(mst_no, mst_tgl, kode_cabang, alasan_batal, mst_no_new);
//	}	
	/**Fungsi : Untuk Update Pengkinian data..
	 * @param cmd
	 * @param err
	 * @return
	 * @author Ferry Harlim
	 */
	public DataNasabah prosesUpdateNasabah(Object cmd,User currentUser, BindException err)throws DataAccessException {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return basDao.prosesUpdateNasabah(cmd,currentUser,err);
	}
	
	/**Fungsi: 	Untuk Menampilkan jumlah history dari polis sesuai dengan status accept yang diinginkan
	 * 
	 * @param	spaj
	 * @param 	lssaId
	 * @return 	Integer
	 */
	public Integer selectCountMstPositionSpaj(String spaj, String lssaId) {
		return uwDao.selectCountMstPositionSpaj(spaj,lssaId);
	}
	
	public Integer selectCountTerimaTtp(String spaj) {
		return uwDao.selectCountTerimaTtp(spaj);
	}
	
	public List selectHighRiskCustm(){
		return uwDao.selectHighRiskCustm();
	}
	
	public void inserthighrisk_cust(String Desc){
		this.uwDao.inserthighrisk_cust(Desc);
	}
	
	/**Fungsi:	Untuk menampilkan data KYC
	 * @param 	dariTanggal
	 * @param 	sampaiTanggal
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectKYCnewBis_utama(String dariTanggal,String sampaiTanggal){
		return uwDao.selectKYCnewBis_utama(dariTanggal,sampaiTanggal);
	}
	
	public Map selectKYC(String spaj){
		return this.uwDao.selectKYC(spaj);
	}

	
	/**Fungsi:	Untuk menampilkan data KYC 
	 * @param 	dariTanggal
	 * @param 	sampaiTanggal
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectKYCnewBis_utamaPK(String dariTanggal,String sampaiTanggal){
		return uwDao.selectKYCnewBis_utamaPK(dariTanggal,sampaiTanggal);
	}
	
	public List selectProsesKyc(String reg_spaj){
		return uwDao.selectProsesKyc(reg_spaj);
	}
	
//	public List selectProsesKyc(String reg_spaj){
//		return uwDao.selectProsesKyc(reg_spaj);
//	}
	
//	public List selectProsesKyc(String reg_spaj){
//		return uwDao.selectProsesKyc(reg_spaj);
//	}
	
	public String selectKYCnewBisJnsTopUp(String spaj){
		return uwDao.selectKYCnewBisJnsTopUp(spaj);
	}
	/**Fungsi	: Utnuk menecek apakah suatu polis yang pekerjaannya termasuk dalam daftar
	 * 				High Risk Customer(HCR)
	 * @param mpnJobDesc
	 * @param mklKerja
	 * @param mklIndustri
	 * @return Integer
	 * @author Ferry Harlim
	 */
	public Integer selectCountLstHighRiskCust(String mpnJobDesc, String mklKerja, String mklIndustri){
		return uwDao.selectCountLstHighRiskCust(mpnJobDesc, mklKerja, mklIndustri);
	}
	
	public User selectMstpositionSpajUserAccept(String spaj){
		return uwDao.selectMstpositionSpajUserAccept( spaj);
	}
	
	public void updateMstPolicyLsreId(String spaj,Integer lsreId,Integer lstbId) {
		basDao.updateMstPolicyLsreId(spaj, lsreId, lstbId);
	}

	public Integer select_mst_client_old(String mcl_id) {
		return basDao.select_mst_client_old(mcl_id);
	}

	public List cari_polis_lain(String mcl_id) {
		return basDao.cari_polis_lain(mcl_id);
	}

	public Map selectbacCekAgen(String msag_id) {
		return uwDao.selectbacCekAgen(msag_id);
	}
	
	public String selectMstPolicyRegSpaj(String nopolis) {
		return basDao.selectMstPolicyRegSpaj(nopolis);
	}

	public Map cari_mcl_id(String nopolis) {
		return (HashMap)basDao.cari_mcl_id(nopolis);
	}

	public Integer select_count_client(String nopolis, String nama, String tgl) {
		return basDao.select_count_client(nopolis, nama, tgl);
	}
	
	/**
	 * @param flag  1 = Pencarian berdasarkan id agen (msag_id).
	 * 				2 = Pencarian berdasarkan nama agen (mcl_first).
	 * 				3 = Pencarian Berdasarkan No. Identitas	(mspe_no_identity)
	 * @param query
	 * @return
	 */
	public List selectAllAgent(Integer flag,String lcaId,String query){
		return basDao.selectAllAgent(flag,lcaId, query);
	}
	
	/**Fungsi : Untuk Mencek apakah suatu agen termasuk dalam akses dari admin
	 * 
	 * @param lusId
	 * @param msfId
	 * @return
	 * @throws DataAccessException
	 */
	public Integer selectCekAgenInUserAdmin(String lusId,String msfId)throws DataAccessException{
	   return basDao.selectCekAgenInUserAdmin(lusId, msfId);
	}

	
	public Integer prosesUpdateKycResult(List lsKycNewBus,User currentUser) {
		Integer total=0;
		
		for(int i=0;i<lsKycNewBus.size();i++){
			NewBusinessCase newBus=(NewBusinessCase)lsKycNewBus.get(i);
			if(newBus.getFlagKyc()==null)
				newBus.setFlagKyc(0);
			//if(newBus.getFlagKyc()==1){//update kyc jika di centang pada jsp nya
				total++;
				uwDao.updateMstInsuredKycResult(newBus.getReg_spaj(),1,newBus.getKycResult(),currentUser.getLus_id(),
						newBus.getFlagYUw(),newBus.getFlagYUkm(), newBus.getFlagYDirec());
				uwDao.updateMstClientNewKycResult(newBus.getMspo_policy_holder(),newBus.getMpn_job_desc(), newBus.getMkl_kerja(),newBus.getMkl_industri());
				uwDao.insertLst_ulangan(newBus.getReg_spaj(), new Date(), "UPDATE KYC RESULT", null, currentUser.getLus_id(), newBus.getKycResult());
			//}
		}
		return total;
	}
	
	public void prosesKyc(List lsKycNewBus,User currentUser) {
		NewBusinessCase newBus=(NewBusinessCase)lsKycNewBus;	
//		uwDao.updateProsesKyc(newBus.getReg_spaj(), 1, currentUser.getLus_id(),newBus.getFlagYUw());
		uwDao.insertLst_ulangan(newBus.getReg_spaj(), new Date(), "UPDATE KYC RESULT", null, currentUser.getLus_id(), newBus.getKycResult());
	}
	
	
	/**Fungsi : Untuk Memfilter List dari KYC result dengan batasan total premi 100 juta atau daftar HRC (High risk customer)
	 * 
	 * @param KYCnewBis
	 * @return
	 */
	public List selectLsKycNewBus(List KYCnewBis) {		
		return this. uwDao.selectLsKycNewBus(KYCnewBis);		
		
	}

	/**Fungsi : Untuk Menampilkan daftar Polis yang terkamsuk dalam Daily Monitoring KYC - Putus Kontrak
	 * 
	 * @return List
	 * @author Ferry Harlim
	 */
	public List selectLsKycNewBusPutusKontrak(List lsKyc) {
		NumberFormat f3=new DecimalFormat("000");
		List lsKycNewBus=new ArrayList();
		Double batasan=new Double(100000000);
		int  row=0;
		boolean link;
		Integer countSurender,countTransUlink;
		for(int i=0;i<lsKyc.size();i++){
			NewBusinessCase newBus=(NewBusinessCase)lsKyc.get(i);
			
			double total=newBus.getTotal_tu().doubleValue()+newBus.getPremi_pokok().doubleValue();
			//cek daftar pekerjaan,, apakah termasuk dalam daftar High Risk Customer (HCR)
			int hrcFilter=selectCountLstHighRiskCust(newBus.getMpn_job_desc(),newBus.getMkl_kerja(), newBus.getMkl_industri());
			countSurender=uwDao.selectCountMstSurrender(newBus.getReg_spaj(), 1);
			countTransUlink=uwDao.selectCountMstTransUlink(newBus.getReg_spaj(),3);
			if(total>=batasan.doubleValue() || hrcFilter>0){
				if(props.getProperty("product.unitLink").indexOf(f3.format(newBus.getLsbs_id()))>0)
					link=true;
				else 
					link=false;	
				
				//case 1. apabila bukan produk link dan ada di tabel EKA.MST_SURRENDER 
				//	      (di join dengan reg_spaj), maka dia udah pasti PK (putus kontrak) atau NT (nilai tunai)
				if(link==false && countSurender>0){
					newBus.setJns_transaksi(1);
				//case 2. apabila produk link dan ADA di tabel EKA.MST_SURRENDER, 
				//	maka dia udah pasti RD (redemption) atau tarik unit seluruhnya (dia narik unit sampe abis polisnya),
				}else if(link==true && countSurender>0){
					newBus.setJns_transaksi(2);
				//3. apabila produk link dan TIDAK ADA di tabel EKA.MST_SURRENDER, 
				//	tapi ada di tabel EKA.MST_TRANS_ULINK where LT_ID=3 (dijoin dengan reg_spaj), maka dia RD (redemption) atau TARIK UNIT SEBAGIAN (dia narik unit tapi gak seluruhnya)
				}else if(link==true && countSurender<0){
					newBus.setJns_transaksi(3);
					//NEw Bisnis
				}else{
					newBus.setJns_transaksi(4);
				}
				newBus.setRow(row);
				row++;
				lsKycNewBus.add(newBus);
			}	
		}
		return lsKycNewBus;
	}
	
	/**Fungsi : Untuk Menampilkan daftar cabang2 yang polis nya telah terakseptasi khusus
	 * 
	 * @return List
	 * @author Ferry Harlim
	 */
	public List selectlstCabangForAkseptasiKhusus() {
		return uwDao.selectlstCabangForAkseptasiKhusus();
	}
	/**Fungsi : Untuk Menampilkan daftar cabang2 yang polis nya telah terakseptasi khusus untuk hari ini
	 * 
	 * @return List
	 * @author Ferry Harlim
	 */
	public List selectlstCabangForAkseptasiKhususToday() {
		return uwDao.selectlstCabangForAkseptasiKhususToday();
	}

	public Pemegang2 selectPemegangPolisUpdateNasabah(String spaj){
		return uwDao.selectPemegangPolisUpdateNasabah(spaj);
	}
	public Tertanggung selectTertanggungUpdateNasabah(String spaj){
		return uwDao.selectTertanggungUpdateNasabah(spaj);
	}


	public void prosesUpdateMstClientHistory() {	
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		int total=0;
		String noPolis[]={  "02020200100009",
				"02027200300008",
				"02052200100035",
				"02116200700016",
				"02116200700017",
				"02116200700018",
				"04076200400157",
				"05056200000026",
				"05064200100018",
				"05076200400164",
				"11033200400003",
				"11045200400042",
				"11045200500026",
				"11063200400003",
				"11076200400025",
				"11077200500001",
				"19052200100246",
				"20084200300006",
				"32040200400010",
				"32040200400011",
				"32085200400013",
				"37159200700211"};
		for(int i=0;i<noPolis.length;i++){
			Map data=commonDao.selectDataPpAndTt(noPolis[i]);
			commonDao.updateMstClientHistory(data);
			total++;
		}
		
	}

	public void prosesInsertMReasTempNew(String begDateAwal,String begDateAkhir)throws DataAccessException {
		reasUtilities.prosesInsertMReasTempNew(begDateAwal, begDateAkhir);
	}

	public void prosesInputHrc(CommandKyc command) {
		//delete all, lalu insert baru.
		if(command.getProses()==2){//save
			uwDao.deleteLstHighRiskCust(null);
			for(int i=0;i<command.getLsHrc().size();i++){
				Hrc hrc=(Hrc)command.getLsHrc().get(i);
				uwDao.insertLstHighRiskCust(hrc);
			}
		}else if(command.getProses()==3){//delete
			for(int i=0;i<command.getLsHrc().size();i++){
				Hrc hrc=(Hrc)command.getLsHrc().get(i);
				if(hrc.getCek()!=null && hrc.getCek()==1)
					uwDao.deleteLstHighRiskCust(hrc.getLshc_id());
			}
		}
		
	}
	public Double selectMstCommissionKomisiAgen(String spaj,Integer levCom,Integer tahunKe, Integer premiKe)throws DataAccessException{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return uwDao.selectMstCommissionKomisiAgen(spaj, levCom, tahunKe, premiKe);
		
	}

	public void prosesEndorseFormAgen(String msfId, User currentUser) {
		/** tidak jadi dibuat, secara automatis sudah ke admin asal pertanggungjawabanny
		basDao.updateMstFormLusId(currentUser.getLus_id(), msfId);
		basDao.updateMstSpajLusId(currentUser.getLus_id(), msfId);
		basDao.updateMstSpajDetLusId(currentUser.getLus_id(), msfId);
		basDao.insertFormHistory(msfId, currentUser.getLus_id(), 1, "Proses Endorse Form Agen");
		*/
		
	}
	/**Fungsi : untuk menampilkan data polis yang Sudah Lengkap Terupdate datanya oleh nasabah/bas 
	 * @return
	 * @throws DataAccessException
	 */
	public List selectbasCekDataInputData(String tanggalAwal,String tanggalAkhir,String lcaId)throws DataAccessException{
		return basDao.selectbasCekDataInputData(tanggalAwal,tanggalAkhir,lcaId);
	}

	/**Fungsi : untuk menampilkan data polis yang belum terupdate polisnya oleh nasabah/bas 
	 * @return
	 * @throws DataAccessException
	 */
	public List selectbasCekDataBalik(String tanggalAwal,String tanggalAkhir,String lcaId)throws DataAccessException{
		return basDao.selectbasCekDataBalik(tanggalAwal,tanggalAkhir,lcaId);
	}

	/**Fungsi : untuk menampilkan data polis (VENDOR) yang belum terupdate polisnya oleh nasabah/bas 
	 * @return
	 * @throws DataAccessException
	 */
	public ClientHistory selectDataVendor(String mspoPolicyNo)throws DataAccessException{
		return basDao.selectDataVendor(mspoPolicyNo);
	}
	
	/**Fungsi : untuk menampilkan data polis (NASABAH) yang belum terupdate polisnya oleh nasabah/bas 
	 * @return
	 * @throws DataAccessException
	 */
	public ClientHistory selectDataNasabahUpdate(String mspoPolicyNo)throws DataAccessException{
		return basDao.selectDataNasabahUpdate(mspoPolicyNo);
	}
	
	public void setSimultan(Simultan simultan) {
		this.simultan = simultan;
	}

	public void setTransferUw(TransferUw transferUw) {
		this.transferUw = transferUw;
	}
	

	public List selectAllLstRegion1(){
		return uwDao.selectAllLstRegion1();
	}
	
	public List selectAllLstCab(){
		return uwDao.selectAllLstCab();
	}
	
	
	public String selectEmailAddr(String lca_id) throws DataAccessException{
		return uwDao.selectEmailAddr(lca_id);
	}
	public List selectStampHist(String reg_spaj) throws DataAccessException{
		return uwDao.selectStampHist(reg_spaj);
	}
	public void deletemstStampHist(String spaj) throws DataAccessException{
		uwDao.deletemstStampHist(spaj);
	}
	public void deleteMstDrek(String no_trx) throws DataAccessException{
		uwDao.deleteMstDrek(no_trx);
	}
	
	public void mstStampMaterai(String  mstm_bulan)throws DataAccessException{
		uwDao.mstStampMaterai(mstm_bulan);
	}
	public String mstm_bulan() throws DataAccessException{
		return uwDao.mstm_bulan();
	}

	/**Fungsi : untuk menampilkan rate rider link (>810)
	 * 
	 * @param lsbsId
	 * @param lsrrAge
	 * @param lkuId
	 * @return
	 */
	/*public Double selectLstRateRider(Integer lsbsId, Integer lsrrAge, String lkuId){
		return uwDao.selectLstRateRider(lsbsId, lsrrAge, lkuId);
	}*/
	
	/**Fungsi : Untuk Menampilkan data reinstatement Policy yang ada di UW
	 * 			request dari dr ingrid
	 * 
	 * @param spaj
	 * @return
	 * @throws DataAccessException
	 * @Date 05/02/2008
	 */
	public List selectReinstatementWorkSheet(String spaj)throws DataAccessException{
		return uwDao.selectReinstatementWorkSheet(spaj);
	}

	public void prosesReinstatementWorkSheet(CommandReins cmdReins,User currentUser)throws DataAccessException{
		reinstate.prosesReinstatementWorkSheet(cmdReins,currentUser);
	}

	/**Fungsi : Untuk Mencek polis produk simas prima yang terakseptasi khusus
	 * 			jika count >0 maka true
	 * 			else false
	 * @param spaj
	 * @return 
	 * @throws DataAccessException
	 */
	public Integer selectCountProductSimasPrimaAkseptasiKhusus(String spaj,Integer insuredNo,Integer lssaId, Integer jnBank)throws DataAccessException{
		return uwDao.selectCountProductSimasPrimaAkseptasiKhusus(spaj, insuredNo, lssaId, jnBank);
	}

	/**
     * Fungsi : Untuk menampilkan daftar jenis prefix medical seperti jenis A, B, D, E, F dst
     * @return list
     * @
     */
    public List<JenisMedicalVO> selectLstJenisPrefix(){
		return uwDao.selectLstJenisPrefix();
	}

    /**
     * Fungsi : Untuk menampilkan daftar jenis pemeriksaan spt: LPK, Urine Rutin, Rontgen dst
     * @return list
     */
    public List<MedicalCheckupVO> selectMedicalCheckupList(){
		return uwDao.selectMedicalCheckupList();
	}

    /**Fungsi : Untuk menampilkan daftar jenis pemeriksaan berdasarkan jenis medis
     *          mis: jenis A akan return permeriksaan 1,2,4
     * @param jnsMedis: jenis medis
     * @return list
     */
    public List<Integer> selectMedicalCheckupListByJenisMedis( Integer jnsMedis )
    {
		return uwDao.selectMedicalCheckupListByJenisMedis( jnsMedis );
	}

    /**Fungsi : Untuk menampilkan nama pemegang polis berdasarkan nomor spaj
     * @param spaj: nomor spaj
     * @return String
     */
    public String selectPolicyHolderNameBySpaj( String spaj )
    {
		return uwDao.selectPolicyHolderNameBySpaj( spaj );
	}

    /**Fungsi : Untuk menampilkan nama tertanggung berdasarkan nomor spaj
     * @param spaj: nomor spaj
     * @return String
     */
    public String selectInsuredNameBySpaj( String spaj )
    {
		return uwDao.selectInsuredNameBySpaj( spaj );
	}
    
    public void insertLstCekValid(CekValidPrintPolis cvpp) throws DataAccessException{
		this.uwDao.insertLstCekValid(cvpp);
	}
    
    public void insertMstVoidPayment(VoidPayment voidPayment) throws DataAccessException{
    	this.uwDao.insertMstVoidPayment(voidPayment);
    }
    
    public Integer selectCountCekValid( String spaj )
    {
		return uwDao.selectCountCekValid( spaj );
    }
    
    public String selectCabangEmail(String cabang) {
		StringBuffer result = new StringBuffer();
		List<String> emails = uwDao.selectCabangEmail(cabang);
		for(String email : emails) {
			if(email != null) if(!email.trim().equals("")) result.append(email+";");
		}
		return result.toString();
	}
    
	/*public void updateReferralToFact(Nasabah nasabah){
		this.commonDao.updateReferralToFact(nasabah);
	}*/
	
		
	
		
	/** Cross Selling Dao */

	//Proses menyimpan data cross-selling
	public String saveCrossSelling(CrossSelling crossSelling, int lus_id, Integer flag_lanjutan) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		//A. INSERT SPAJ BARU
		if(crossSelling.mode.equals("INSERT")) {
			return crossSellingDao.insertSpajCrossSelling(crossSelling.policyCs, lus_id, flag_lanjutan);
		//B. EDIT SPAJ LAMA
		}else if(crossSelling.mode.equals("UPDATE")) {
			return crossSellingDao.updateSpajCrossSelling(crossSelling.policyCs, lus_id);
		}else {
			return null;
		}
	}
	
	//Proses mentransfer data cross-selling ke posisi berikutnya
	public String transferCrossSelling(CrossSelling crossSelling, int lus_id) {
		return crossSellingDao.transferCrossSelling(crossSelling.policyCs, lus_id);
	}
	
	public String saveNomorPolisCrossSelling(CrossSelling crossSelling, int lus_id) {
		return crossSellingDao.saveNomorPolisCrossSelling(crossSelling, lus_id);
	}
	
	public String saveTtpCrossSelling(CrossSelling crossSelling, int lus_id) {
		return crossSellingDao.saveTtpCrossSelling(crossSelling, lus_id);
	}
	
	public List<Map> selectCariCrossSelling(String tipe, String kata) {
		return crossSellingDao.selectCariCrossSelling(tipe, kata);
	}
	
	public List<PolicyCs> selectDaftarCrossSelling(Integer lspd_id, String reg_spaj, String mscs_holder, String startDate, String endDate){
		return crossSellingDao.selectDaftarCrossSelling(lspd_id, reg_spaj, mscs_holder, startDate, endDate);
	}
	
	public String saveKonfirmasiCrossSelling(List<PolicyCs> daftarSpaj, int lus_id) {
		return crossSellingDao.saveKonfirmasiCrossSelling(daftarSpaj, lus_id);
	}
	
	public void insertMstPolicyCs(PolicyCs policyCs){
		crossSellingDao.insertMstPolicyCs(policyCs);
	}
	
	public void insertMstAgentCs(AgentCs agentCs){
		crossSellingDao.insertMstAgentCs(agentCs);
	}
	
	public void insertMstPositionCs(PositionCs positionCs){
		crossSellingDao.insertMstPositionCs(positionCs);
	}
	
	public PolicyCs selectMstPolicyCsBySpaj(String reg_spaj){
		return crossSellingDao.selectMstPolicyCsBySpaj(reg_spaj);
	}
	
	public List<AgentCs> selectMstAgentCsBySpaj(String reg_spaj){
		return crossSellingDao.selectMstAgentCsBySpaj(reg_spaj);
	}
	
	public List<PositionCs> selectMstPositionCsBySpaj(String reg_spaj){
		return crossSellingDao.selectMstPositionCsBySpaj(reg_spaj);
	}
	
	public int updateMstPolicyCs(PolicyCs policyCs){
		return crossSellingDao.updateMstPolicyCs(policyCs);
	}

	public int updateMstAgentCs(AgentCs agentCs) throws DataAccessException{
		return crossSellingDao.updateMstAgentCs(agentCs);
	}
	
	/**@Fungsi:	Untuk Menampilkan data Bank pad tabel eka.mst_tbank
	 * @param 	Integer position
	 * @return 	List
	 * @author 	Deddy
	 */
	public List selectAllMstTBank(Integer position){
		return accountingDao.selectallMstTBank(position);
	}
	
	/**@Fungsi:	Untuk Menampilkan data nasabah pada tabel
	 * 			EKA.MST_TBANK sesuai dengan nomor pre
	 * @param 	String nomor (No_Pre)
	 * @return 	List
	 * @author 	Deddy
	 */
	public List selectMstTBankByCode(String nomor){
		return accountingDao.selectMstTBankByCode(nomor);
	}
	
	public List<DBank> selectMstDBank(String nomor){
		return accountingDao.selectMstDBank(nomor);
	}
	
	public List<DropDown> selectLstKasRowFirst(){
		List <DropDown> lstKasRowFirst = new ArrayList();
		lstKasRowFirst.add(new DropDown("K","[K] Keluar"));
		lstKasRowFirst.add(new DropDown("M","[M] Masuk"));

		return lstKasRowFirst;
	}
	
	public List<DropDown> selectLstKas(){
		List <DropDown> lstKas = new ArrayList();
		lstKas.add(new DropDown("A","[A] Penerimaan Premi"));
		lstKas.add(new DropDown("B","[B] Penerimaan Lain-lain"));
		lstKas.add(new DropDown("C","[C] Pengeluaran Premi"));
		lstKas.add(new DropDown("D","[D] Pengeluaran Lain-lain"));
		
		return lstKas;
	}
	
	public List<DropDown> selectKodeCashFlow(){
		return accountingDao.selectKodeCashFlow();
	}
	
	public List<DropDown> selectLstKurs(){
		return uwDao.selectLstKurs();
	}
	
	public List<DropDown> selectLstBank(){
		return accountingDao.selectLstBank();
	}
	
	public List<DropDown> selectLstBank2(){
		return accountingDao.selectLstBank2();
	}
	
	public void updatePreGantung(String nomor, int titipan_gantung, String no_pre_old){
		TBank tbank = new TBank();
		tbank.setNo_pre(nomor);
		tbank.setNo_pre_old(no_pre_old);
		tbank.setTitipan_gantung(titipan_gantung);
		accountingDao.updatePreGantung(tbank);
	}
	
	public void updateMstTBank(String no_pre, int position, Date tgl_jurnal, Date tgl_rk, String no_voucher, String mtb_gl_no, int lus_id_trans){
		TBank tbank = new TBank();
		tbank.setNo_pre(no_pre);
		tbank.setPosition(position);
		tbank.setTgl_jurnal(tgl_jurnal);
		tbank.setTgl_rk(tgl_rk);
		tbank.setNo_voucher(no_voucher);
		tbank.setMtb_gl_no(mtb_gl_no);
		tbank.setLus_id_trans(lus_id_trans);
		accountingDao.updateMstTBank(tbank);
	}
	
	public void updateMstTBankBook(String no_pre, int position, Date tgl_rk, String mtb_gl_no, int lus_id_trans){
		TBank tbank = new TBank();
		tbank.setNo_pre(no_pre);
		tbank.setPosition(position);
		tbank.setTgl_rk(tgl_rk);
		tbank.setMtb_gl_no(mtb_gl_no);
		tbank.setLus_id_trans(lus_id_trans);
		accountingDao.updateMstTBankBook(tbank);
	}
	
	public String selectRekEkalife(String lsrek_gl_no){
		return accountingDao.selectRekEkalife(lsrek_gl_no);
	}
	
	public Date selectMstDefault(){
		return (Date) accountingDao.selectMstDefault();
	}
	
	public Date selectMstDefault2(){
		return (Date) accountingDao.selectMstDefault2();
	}
	
	public String selectMstTBankVoucher(String no_voucher,String ls_thn){
		return (String) accountingDao.selectMstTBankVoucher(no_voucher,ls_thn);
	}
	
	public List<DBank> selectMstDBankTrans(String nomor){
		return accountingDao.selectMstDBankTrans(nomor);
	}
	
	public void insertMstBVoucher(BVoucher bvoucher){
		accountingDao.insertMstBVoucher(bvoucher);
	}
	
	public void updateMstTBankPosition(String nomor){
		accountingDao.updateMstTBankPosition(nomor);
	}
	
	public void deleteMstDBank(String nomor,int no_jurnal){
		accountingDao.deleteMstDBank(nomor,no_jurnal);
	}
	
	public void deleteMstRelasiNasabahByNo(String mns_kd_nasabah,int mrn_no_relasi){
		commonDao.deleteMstRelasiNasabahByNo(mns_kd_nasabah, mrn_no_relasi);
	}
	
	public void deleteMstProdBankByNo(String mns_kd_nasabah, int mpb_no){
		commonDao.deleteMstProdBankByNo(mns_kd_nasabah, mpb_no);
	}
	
	public void deleteMstAktivitasByPertKe(String mns_kd_nasabah, int pert_ke){
		commonDao.deleteMstAktivitasByPertKe(mns_kd_nasabah, pert_ke);
	}
	
	public void deleteMstChildrenByNo(String mns_kd_nasabah,int mch_id){
		commonDao.deleteMstChildrenByNo(mns_kd_nasabah, mch_id);
	}
	
	public void updateMstDBankRow(String nomor,int no_jurnalafter, int no_jurnalbefore){
		accountingDao.updateMstDBankRow(nomor,no_jurnalafter,no_jurnalbefore);
	}
	
	public void updateMstRelasiNasabahRow(String mns_kd_nasabah, int mrn_no_relasiafter, int mrn_no_relasibefore){
		commonDao.updateMstRelasiNasabahRow(mns_kd_nasabah, mrn_no_relasiafter, mrn_no_relasibefore);
	}
	
	public void updateMstProdBankRow(String mns_kd_nasabah, int mpb_noafter, int mpb_nobefore){
		commonDao.updateMstProdBankRow(mns_kd_nasabah, mpb_noafter, mpb_nobefore);
	}
	
	public void updateMstChildrenRow(String mns_kd_nasabah, int mch_idafter, int mch_idbefore){
		commonDao.updateMstChildrenRow(mns_kd_nasabah, mch_idafter, mch_idbefore);
	}
	
	public Jiffy selectMstjiffy(String kdNasabah){
		return commonDao.selectMstJiffy(kdNasabah);
	}
	
	public List<DropDown> selectLstKarir(){
		List <DropDown> lstKarir = new ArrayList();
		lstKarir.add(new DropDown("1","Pindah Kerja"));
		lstKarir.add(new DropDown("2","Pensiun"));
		lstKarir.add(new DropDown("3","Promosi/Kenaikan Gaji"));
		return lstKarir;
	}
	
	public List<DropDown> selectLstPartner(){
		List <DropDown> lstPartner = new ArrayList();
		lstPartner.add(new DropDown("1","Sendiri"));
		lstPartner.add(new DropDown("2","Teman"));
		lstPartner.add(new DropDown("3","Keluarga"));
		return lstPartner;
	}
	
	public List<DropDown> selectLstRelasi(){
		List<DropDown> lstRelasiChild = new ArrayList();
		lstRelasiChild.add(new DropDown("1","Anak"));
		lstRelasiChild.add(new DropDown("2","Cucu"));
		lstRelasiChild.add(new DropDown("3","Lain-lain"));
		return lstRelasiChild;
	}
	
	public List<RelasiNasabah> selectMstRelasiNasabah(String mns_kd_nasabah){
		return commonDao.selectMstRelasiNasabah(mns_kd_nasabah);
	}
	
	public List selectListBii()throws DataAccessException{
		return commonDao.selectListBii();
	}
	
	public List selectListRekomendasi()throws DataAccessException{
		return commonDao.selectListRekomendasi();
	}
	
	public List selectListProdBank()throws DataAccessException{
		return commonDao.selectListProdBank();
	}
	
	public List selectListAktivitas()throws DataAccessException{
		return commonDao.selectListAktivitas();
	}
	
	
	
	public List<Children> selectMstChildren(String mns_kd_nasabah) throws DataAccessException{
		return commonDao.selectMstChildren(mns_kd_nasabah);
	}
	
	public List<Matrix> selectMstMatrix(String mns_kd_nasabah) throws DataAccessException{
		return commonDao.selectMstMatrix(mns_kd_nasabah);
	}
	
	public List<Aspirasi> selectMstAspirasi(String mns_kd_nasabah) throws DataAccessException{
		return commonDao.selectMstAspirasi(mns_kd_nasabah);
	}
	
	public List<Rekomendasi> selectMstRekomendasi(String mns_kd_nasabah) throws DataAccessException{
		return commonDao.selectMstRekomendasi(mns_kd_nasabah);
	}
	
	public List<Kebutuhan> selectMstKebutuhanPlusLjkKet(String mns_kd_nasabah) throws DataAccessException{
		return commonDao.selectMstKebutuhanPlusLjkKet(mns_kd_nasabah);
	}
	
	public List<ProdBank> selectMstProdBankPlusLpbKet(String mns_kd_nasabah) throws DataAccessException{
		return commonDao.selectMstProdBankPlusLpbKet(mns_kd_nasabah);
	}
	
	public List<Pendapatan> selectMstPendapatanPlusLspKet(String mns_kd_nasabah)throws DataAccessException{
		return commonDao.selectMstPendapatanPlusLspKet(mns_kd_nasabah);
	}
	
	public List<Aktivitas> selectMstAktivitasNext(String mns_kd_nasabah)throws DataAccessException{
		return commonDao.selectMstAktivitasNext(mns_kd_nasabah);
	}
	
	public List<Aktivitas> selectMstAktivitas(String mns_kd_nasabah)throws DataAccessException{
		return commonDao.selectMstAktivitas(mns_kd_nasabah);
	}
	
	public List<DropDown> selectLstReviewUang(){
		List<DropDown> lstReviewUang = new ArrayList();
		lstReviewUang.add(new DropDown("6","6"));
		lstReviewUang.add(new DropDown("12","12"));
		lstReviewUang.add(new DropDown("24","24"));
		return lstReviewUang;
	}
   
    public void insertMstDetRefundLampiran( String spaj, String lampiran, String checkBox, Integer noUrut )
    {
    	refundDao.insertMstDetRefundLampiran( spaj, lampiran, checkBox, noUrut );
    }
    
    // add by sam for Refund
    public void deleteThenInsertRefund( RefundDbVO refundDbVO )
    {
        refundDao.deleteMstDetRefundBySpaj( refundDbVO.getSpajNo() );
        refundDao.deleteMstRefundBySpaj( refundDbVO.getSpajNo() );
        refundDao.deleteMstDetRefundLamp( refundDbVO.getSpajNo() );

        refundDao.insertMstRefund( refundDbVO );
        refundDao.insertMstDetRefundLamp( refundDbVO );
        for( RefundDetDbVO refundDetDbVO : refundDbVO.getDetailList() )
        {
            refundDao.insertMstDetRefund( refundDetDbVO );
        }
    }

    public PolicyInfoVO selectPolicyInfoBySpaj( String spajNo )
    {
        return refundDao.selectPolicyInfoBySpaj( spajNo );    
    }
    
    public List<PolicyInfoVO> selectPolicyInfoBySpajList( String[] spajList )
    {
        return refundDao.selectPolicyInfoBySpajList( spajList );    
    }

    public List < RekapInfoVO > selectInfoForRekap( Map<String, Object> params )
    {
        return refundDao.selectInfoForRekap( params );    
    }
    
    public List < RekapInfoVO > selectInfoForRekapKeAccFinance( Map<String, Object> params )
    {
        return refundDao.selectInfoForRekapKeAccFinance( params );    
    }
    
    public InfoBatalVO selectInfoBatalBySpaj( String spajNo )
    {
        return refundDao.selectInfoBatalBySpaj( spajNo );    
    }

    public List<SetoranPremiDbVO> selectSetoranPremiBySpaj( String spajNo )
    {
        return refundDao.selectSetoranPremiBySpaj( spajNo );
    }
    
    public List<SetoranPremiDbVO> selectPenarikanUlinkSortedByMsdpNumber( String spajNo )
    {
        return refundDao.selectPenarikanUlinkSortedByMsdpNumber( spajNo );
    }

    public List<PenarikanUlinkDbVO> selectPenarikanUlink( String spajNo )
    {
        return refundDao.selectPenarikanUlink( spajNo );
    }

    public List<BiayaUlinkDbVO> selectBiayaUlink( String spajNo )
    {
        return refundDao.selectBiayaUlink( spajNo );
    }

    public List<RefundViewVO> selectRefundList( Map<String, Object> params )
    {
        return refundDao.selectRefundList( params );
    }

    public List<RefundDetDbVO> selectRefundDetList( Map<String, Object> params )
    {
        return refundDao.selectRefundDetList( params );
    }

    public String selectJenisInvestByLjiId( String ljiId )
    {
        return refundDao.selectJenisInvestByLjiId( ljiId );
    }

    public Integer selectRefundTotalOfPages( Map<String, Object> params )
    {
        return refundDao.selectRefundTotalOfPages( params );
    }

    public RefundDbVO selectRefundByCd( String regSpaj )
    {
		return refundDao.selectRefundByCd( regSpaj );
	}
    
    public List<RefundDbVO> selectMstRefund( String regSpaj )
    {
		return refundDao.selectMstRefund( regSpaj );
	}
    
    public Date selectNowDate( )
    {
		return refundDao.selectNowDate( );
	}
    
    public Date selectTglKirimDokFisik( String regSpaj )
    {
		return refundDao.selectTglKirimDokFisik( regSpaj );
	}
    
    public Integer selectNoSuratList( String noSuratDate )
    {
		return refundDao.selectNoSuratList( noSuratDate );
	}
    
    public Date selectMspoDatePrint( String regSpaj )
    {
		return refundDao.selectMspoDatePrint( regSpaj );
	}
    
    public List <SetoranPokokDanTopUpVO> selectSetoranPremiPokokDanTopUp( String regSpaj )
    {
		return refundDao.selectSetoranPremiPokokDanTopUp( regSpaj );
	}
    
    public List <LampiranListVO> selectMstDetRefundLamp( String regSpaj )
    {
		return refundDao.selectMstDetRefundLamp( regSpaj );
	}
    
    public Integer selectMaxNoUrutMstDetRefLamp( String regSpaj )
    {
		return refundDao.selectMaxNoUrutMstDetRefLamp( regSpaj );
	}
    
    public MstBatalParamsVO selectSpajCancelMstBatal( String regSpaj )
    {
		return refundDao.selectSpajCancelMstBatal( regSpaj );
	}
    
    public CheckSpajParamsVO selectSpajAlreadyCancelMstRefund( String regSpaj )
    {
		return refundDao.selectSpajAlreadyCancelMstRefund( regSpaj );
	}
    
    public CheckSpajParamsVO selectCheckSpajInDb( String regSpaj )
    {
		return refundDao.selectCheckSpajInDb( regSpaj );
	}
    
    public void updatePosisiAndCancelRefund( String spajNo, Integer posisiCd, BigDecimal cancelWho, Date cancelWhen )
    {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put( "regSpaj", spajNo );
        params.put( "posisiCd", posisiCd );
        params.put( "cancelWho", cancelWho );
        params.put( "cancelWhen", cancelWhen );
        refundDao.updatePosisiAndCancelRefund( params );   
    }

    public void updateTglKirimDokFisik( String spajNo, Date tglKirimDokFisik, String updateWho, Date updateWhen )
    {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put( "regSpaj", spajNo );
        params.put( "tglKirimDokFisik", tglKirimDokFisik );
        params.put( "updateWho", updateWho );
        params.put( "updateWhen", updateWhen );
        refundDao.updateTglKirimDokFisik( params );   
    }
    
    public void updateWhoAndWhenMstRefund( Integer updateWho, Date updateWhen, String spajNo )
    {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put( "regSpaj", spajNo );
        params.put( "updateWhen", updateWhen );
        params.put( "updateWho", updateWho );
        refundDao.updateWhoAndWhenMstRefund( params );   
    }
    
    public String generateNoBatalRefund( String lcaId )
    {
        return refundDao.generateNoBatalRefund( lcaId );
    }

    public void batalkanSpaj( String spaj, String alasan, User currentUser, Integer posisiNo, BigDecimal cancelWho, Date cancelWhen )
    {
    	uwManager.updateMstPowerSaveRo(spaj);
    	uwManager.updateMstSLink(spaj);
    	uwManager.updateMstSSave(spaj);
    	uwManager.updateMstPSave(spaj);
        cancelPolisFromTandaTerimaPolis( spaj, alasan, currentUser );
        updatePosisiAndCancelRefund( spaj, posisiNo, cancelWho, cancelWhen );
        uwDao.saveMstTransHistory(spaj, "tgl_batal_polis", cancelWhen, null, null);
        uwDao.updateCancelInbox(spaj, currentUser.getLus_id());
    }
    
    public String selectLstDocumentPosition( String regSpaj )
    {
        return refundDao.selectLstDocumentPosition( regSpaj );
    }
    
    public Date selectMsteTglKirimPolis( String regSpaj )
    {
        return refundDao.selectMsteTglKirimPolis( regSpaj );
    }

    public List selectFilterRefundSpaj(String posisi, String tipe, String kata, String pilter, String lssaId,String lsspId, String tgl_lahir, String isNewSpaj ) {
		return this.refundDao.selectFilterRefundSpaj(posisi, tipe, kata, pilter,lssaId,lsspId, tgl_lahir, isNewSpaj);
	}

    public Integer selectPosisiDocumentBySpaj( String spajNo ) 
    {
		return this.refundDao.selectPosisiDocumentBySpaj( spajNo );
	}
	
    public Integer selectCheckSpaj( String spajNo ) 
    {
		return this.refundDao.selectCheckSpaj( spajNo );
	}
    
    public String selectMtuUnitTransUlink( String regSpaj ) 
    {
		return this.refundDao.selectMtuUnitTransUlink( regSpaj );
	}
	
	public Map selectLkuIdFromMstPolicy( String regSpaj ) 
    {
		return this.refundDao.selectLkuIdFromMstPolicy( regSpaj );
	}
	
    public BigDecimal selectAksesPembatalanCabang( String regSpaj, BigDecimal lusId ) 
    {
		return this.refundDao.selectAksesPembatalanCabang( regSpaj, lusId );
	}

    public List selectTotalTagih(String spaj){
		return uwDao.selectTotalTagih(spaj);
	}
    
    public Double selectCariSukuBunga(String lku_id, Date tgl_bayar){		
		return uwDao.selectCariSukuBunga(lku_id, tgl_bayar);
	}
    
    public Double selectCekTahapan(String reg_spaj){
		return uwDao.selectCekTahapan(reg_spaj);
	}
    
   public Integer selectIsEkaLink(String reg_spaj)throws DataAccessException{
		return uwDao.selectIsEkaLink(reg_spaj);
	}
    
    public Integer selectIsUlink(String reg_spaj){
		return uwDao.selectIsUlink(reg_spaj);
	}
    
    /**
	 * QUERY2 buat HITUNG DENDA
	 */
	public List selectBillOSBunga(String reg_spaj, Integer tahun_ke, Integer premi_ke){
		return uwDao.selectBillOSBunga(reg_spaj, tahun_ke, premi_ke);
	}
	
	public List selectProductInsured1(String reg_spaj, Integer insured_no){
		return uwDao.selectProductInsured1(reg_spaj, insured_no);
	}
	
	public HashMap selectDataBilling(String reg_spaj){
		return uwDao.selectDataBilling(reg_spaj);
	}
	
	public Double selectDiskPlan(Integer lsbs_id, Integer lsdbs_number, Integer lstht_tahun_ke){
		return uwDao.selectDiskPlan(lsbs_id, lsdbs_number, lstht_tahun_ke);
	}
	
	public KursDanJumlah selectKursAndNominal(String regSpaj){
		return bacDao.selectKursAndNominal(regSpaj);
	}
	
	public int selectCekPrintUlang(String spaj,String keterangan) {
		return this.bacDao.selectCekPrintUlang(spaj,keterangan);
	}
	
	/**
	 * Method utk cek apakah jumlah premi (pokok dan/atau topup)
	 * mencukupi utk fund allocation (investasi tidak minus)
	 * Req Hanifah (#67541)
	 * @author Daru
	 * @since Mar 23, 2015
	 */
	public boolean cekDanaInvestasiMencukupi(String reg_spaj) {
		return uwDao.cekDanaInvestasiMencukupi(reg_spaj);
	}
	
	/**
	 * Method utk cek apakah jumlah usia tertanggung / peserta di atas 50
	 * Req UW - Shopiah untuk produk provestara
	 * @author Ridhaal
	 * @since Jun 28, 2016
	 */
	public List <Map> selectUsiaTT(String reg_spaj, Integer umur1 , Integer umur2){		
		return uwDao.selectUsiaTT(reg_spaj,  umur1 , umur2);
	}
}