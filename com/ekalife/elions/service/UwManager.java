package com.ekalife.elions.service;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import java.util.SortedMap;
import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

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
import com.ekalife.elions.dao.FilingDao;
import com.ekalife.elions.dao.FinanceDao;
import com.ekalife.elions.dao.MuamalatDao;
import com.ekalife.elions.dao.RefundDao;
import com.ekalife.elions.dao.ReinstateDao;
import com.ekalife.elions.dao.RekruitmentDao;
import com.ekalife.elions.dao.SchedulerDao;
import com.ekalife.elions.dao.SimasCardDao;
import com.ekalife.elions.dao.SnowsDao;
import com.ekalife.elions.dao.UwDao;
import com.ekalife.elions.dao.WorksiteDao;
import com.ekalife.elions.model.*;
import com.ekalife.elions.model.cross_selling.Fat;
import com.ekalife.elions.model.cross_selling.FatHistory;
import com.ekalife.elions.model.kyc.PencairanCase;
import com.ekalife.elions.model.kyc.TopUpCase;
import com.ekalife.elions.model.saveseries.Result;
import com.ekalife.elions.model.sms.Smsserver_in;
import com.ekalife.elions.model.sms.Smsserver_out;
import com.ekalife.elions.model.worksheet.UwAbdomen;
import com.ekalife.elions.model.worksheet.UwAda;
import com.ekalife.elions.model.worksheet.UwDadaPa;
import com.ekalife.elions.model.worksheet.UwDecisionRider;
import com.ekalife.elions.model.worksheet.UwEkg;
import com.ekalife.elions.model.worksheet.UwHiv;
import com.ekalife.elions.model.worksheet.UwLpk;
import com.ekalife.elions.model.worksheet.UwMedisLain;
import com.ekalife.elions.model.worksheet.UwQuestionnaire;
import com.ekalife.elions.model.worksheet.UwTreadmill;
import com.ekalife.elions.model.worksheet.UwTumor;
import com.ekalife.elions.model.worksheet.UwUrin;
import com.ekalife.elions.model.worksheet.UwWorkSheet;
import com.ekalife.elions.process.CancelPolis;
import com.ekalife.elions.process.Endorsement;
import com.ekalife.elions.process.HistoryKirimPolis;
import com.ekalife.elions.process.HitungBac;
import com.ekalife.elions.process.Komisi;
import com.ekalife.elions.process.Manfaat;
import com.ekalife.elions.process.Nab;
import com.ekalife.elions.process.NilaiTunai;
import com.ekalife.elions.process.Produksi;
import com.ekalife.elions.process.SavingBac;
import com.ekalife.elions.process.SavingBacCFL;
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
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.PrintPolisFreePa;
import com.ekalife.utils.PrintPolisPaBsm;
import com.ekalife.utils.PrintPolisPerjanjianAgent;
import com.ekalife.utils.Products;
import com.ekalife.utils.f_hit_umur;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;
import id.co.sinarmaslife.std.util.DateUtil;
import id.co.sinarmaslife.std.util.FileUtil;
import id.co.sinarmaslife.std.util.PDFToImage;
import id.co.sinarmaslife.std.util.StringUtil;
import produk_asuransi.n_prod;

/**
 * @author Yusuf
 * @since 18 Feb 2009
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
public class UwManager  {
	protected final Log logger = LogFactory.getLog( getClass() );

	//private static final long serialVersionUID = 1140653189558612007L;
	private Email email;
	private Products products;
	private DecimalFormat f3= new DecimalFormat ("000");
	private DateFormat defaultDateFormat;

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
	private SavingBacCFL savingBacCFL;
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
	private Komisi komisi;
	private EmailPool emailPool;
	//private PrintPolisMultiController printPolisMultiController;
	

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
	private FilingDao filingDao;
	private EndorsDao endorsDao;
	private SchedulerDao schedulerDao;
	private SnowsDao snowsDao;
	public void setSchedulerDao(SchedulerDao schedulerDao) {this.schedulerDao = schedulerDao;}

	/* ======================= Getter-Setter ======================= */
	
	public Properties getProps() {return this.props;}
	public void setSnowsDao(SnowsDao snowsDao) {this.snowsDao = snowsDao;}
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
	public void setSavingBacCFL(SavingBacCFL savingBacCFL) {this.savingBacCFL = savingBacCFL;}
	public void setSavingBac(SavingBac savingBac) {this.savingBac = savingBac; }
	public void setSavingBeaMeterai(SavingBeaMeterai savingBeaMeterai) { this.savingBeaMeterai = savingBeaMeterai;}
	public void setUploadBac(UploadBac uploadBac) {this.uploadBac = uploadBac;}
	public void setProduksi(Produksi produksi) {this.produksi = produksi;}
	//public void setPrintPolisMultiController(PrintPolisMultiController printPolisMultiController) {this.printPolisMultiController = printPolisMultiController;}
	
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
	public void setFilingDao(FilingDao filingDao) {this.filingDao = filingDao;}
	public void setEndorsDao(EndorsDao endorsDao) {this.endorsDao = endorsDao;}
	
	public void setDefaultDateFormat(DateFormat defaultDateFormat) {this.defaultDateFormat = defaultDateFormat;}
	public void setProducts(Products products) {this.products = products;}
	public void setEmail(Email email) {this.email = email;}

	public void setReasIndividu(ReasIndividu reasIndividu) {this.reasIndividu = reasIndividu;}
	public void setSimultan(Simultan simultan) {this.simultan = simultan;}
	public void setTransferUw(TransferUw transferUw) {this.transferUw = transferUw;}	
	public void setEmailPool(EmailPool emailPool) {this.emailPool = emailPool;}
	
	/* ======================= Functions ======================= */
	
	//Yusuf - Fungsi simple untuk testing2
	public void test(String info){
//		String hasil = this.sequence.sequenceNoRegPencairan(new Date());
//		logger.info(hasil);
//		hasil = this.sequence.sequenceNoRegPencairan(new Date());
//		logger.info(hasil);
//		hasil = this.sequence.sequenceNoRegPencairan(new Date());
//		logger.info(hasil);
		
		if (info == "Generate Outsource Scheduler"){
			this.basDao.generateKartuSimasCardOutsource();
		}
		//this.basDao.generateNomorPAS();
		//this.basDao.generateNomorPASSyariah();
		//this.basDao.generateKartuSimasCard();
		//this.basDao.generateKartuSimasCard_ver2();
		//this.basDao.generateKartuHcp();
		//this.basDao.generateKartuDbd();
		//this.basDao.generateKartuPasBp();
	}
	
	public String encryptUrlKey(String idSecure2, String keyId, int jenisId, String link) {
		return this.commonDao.encryptUrlKey(idSecure2, keyId, jenisId, link);
	}
	
	public int validateUrlKey(String idSecure, String link) {
		return this.commonDao.validateUrlKey(idSecure, link);
	}
	
	public void createFolderFromFilename(){
		String directory = props.getProperty("pdf.dir")+"\\Polis\\free_pa\\";
		
		List<DropDown> daftarFile = FileUtil.listFilesInDirectory(directory);
		
		for(DropDown d : daftarFile) {
			String name = d.getKey();
			//create directory
			String target_directory = directory + "\\" + name + "\\";
			File file = new File(target_directory);
			file.mkdir();
			//copy file source to target directory
			String srFile = directory + name;
			String dtFile = target_directory + name;
			FileUtil.copyfile(srFile, dtFile);
		}
	}
	
	public String selectKategoriUW(String spaj){
		return this.refundDao.selectKategoriUW(spaj);
	}
	
    public void updateMstPowerSaveRo( String reg_spaj )
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "reg_spaj", reg_spaj );
        refundDao.updateMstPowerSaveRo( params );   
    }
    
    public List < HashMap > selectNoPre( String reg_spaj )
    {
        return refundDao.selectNoPre( reg_spaj );    
    }
    
    
    public void updateMstSLink( String reg_spaj )
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "reg_spaj", reg_spaj );
        refundDao.updateMstSLink( params );   
    }
    
    public void updateMstSSave( String reg_spaj )
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "reg_spaj", reg_spaj );
        refundDao.updateMstSSave( params );   
    }
    
    public void updateMstPSave( String reg_spaj )
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put( "reg_spaj", reg_spaj );
        refundDao.updateMstPSave( params );   
    }                                                                                                                                                                                                                                                                                                                                                                                                                           
	public void insertManualSimasCardOld(User currentUser) throws Exception{
		
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		this.basDao.insertManualSimasCardOld(currentUser);
	}
	
	public int selectFlagSPH(String spaj) {
		return bacDao.selectFlagSPH(spaj);
	}
	
	public Tamu selectMstTamu(String kd_tamu) throws DataAccessException{
		return bacDao.selectMstTamu(kd_tamu);
	}
	
	public Datausulan selectMstProposal(String kd_tamu) throws DataAccessException{
		return bacDao.selectMstProposal(kd_tamu);
	}
	
	public List<Datausulan> selectMstProposalRider(String kd_tamu) throws DataAccessException{
		return bacDao.selectMstProposalRider(kd_tamu);
	}
	
	public Date selectTanggalLahirClient(String mcl_id) throws DataAccessException{
		return commonDao.selectTanggalLahirClient(mcl_id);
	}
	
	public Map prosesIlustrasiDiscountMi(String kurs, double premi, Date tgl_trans, Date jt_tempo, int kali){
		return financeDao.prosesIlustrasiDiscountMi(kurs, premi, tgl_trans, jt_tempo, kali);
	}
	
	public int selectIsPolisUnitlink(String reg_spaj){
		return reinstateDao.selectIsPolisUnitlink(reg_spaj);
	}
	
	public Double selectBiayaMateraiTertunggak(String reinsNo){
		return reinstateDao.selectBiayaMateraiTertunggak(reinsNo);
	}
		
	public List<DropDown> selectRekeningAjs(Integer lsbp_id, String rek){
		return uwDao.selectRekeningAjs(lsbp_id, rek);
	}
	
	/**Fungsi : untuk menampilkan rate rider link (>810)
	 * 
	 * @param lsbsId
	 * @param lsrrAge
	 * @param lkuId
	 * @return
	 */
	public Double selectLstRateRider(Integer lsbsId, Integer lsdbsNumber, Integer lsrrAge, String lkuId){
		return uwDao.selectLstRateRider(lsbsId, lsdbsNumber, lsrrAge, lkuId);
	}
		
	public List<Scan> selectLstScan(String dept,String wajib){
		return uwDao.selectLstScan(dept,wajib);
	}	
	
	public String selectMaxMiIdMstInbox(String year){
		return uwDao.selectMaxMiIdMstInbox(year);
	}
	
	public void insertSimasCard(int jenis, String spaj, String polis, String lus_id, int jumlahPolis, double totalPremi, int flag_print, String notes, int flag_aktif){
		this.uwDao.insertSimasCard(jenis, spaj, polis, lus_id, jumlahPolis, totalPremi, flag_print, notes, flag_aktif);
	}
	
	public String selectNoPolisFromSpaj(String reg_spaj){
		return uwDao.selectNoPolisFromSpaj(reg_spaj);
	}
	
	public Map prosesCetakSuratSimasCard(String spaj, String lus_id, HttpServletRequest request){
		try {
			return this.suratUnitLink.prosesCetakSuratSimasCard(spaj, lus_id, request);
		} catch (Exception e) {
			logger.error("ERROR :", e);
			return null;
		}
	}
	
	public List selectIsSimasCardClientAnAgent(String reg_spaj){
		return uwDao.selectIsSimasCardClientAnAgent(reg_spaj);
	}
	
	public List selectSimasCard(String spaj){
		return uwDao.selectSimasCard(spaj);
	}
	
	public Boolean prosesInsertSimasCardNew(String spaj, String mrc_no_kartu, User currentUser, Integer flag_insert){
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Boolean proses = uwDao.prosesInsertSimasCardNew(spaj, mrc_no_kartu, currentUser,flag_insert);
		if(!proses){
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		return proses;
	}
	
	public void sendManualSoftcopyPas(User currentUser){
		Boolean test =this.basDao.sendManualSoftcopyPas(currentUser);
		if(!test){
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
	}
	
	public Simcard selectSimasCardBySpaj(String reg_spaj){
		return simasCardDao.selectSimasCardBySpaj(reg_spaj);
	}
	
	public String selectGetMspoPolicyNo(String nopol) {
	return bacDao.selectGetMspoPolicyNo(nopol);
}
	
	public Simcard selectSimasCardByNoKartu(String no_kartu){
		return simasCardDao.selectSimasCardByNoKartu(no_kartu);
	}
	
	public boolean selectAdaRider(String reg_spaj) {
		return uwDao.selectAdaRider(reg_spaj);
	}
	
	public String prosesUploadCabang(String reg_spaj, HttpServletRequest request) throws DataAccessException, IOException, MailException, MessagingException{
		return commonDao.prosesUploadCabang(reg_spaj, request);
	}
	
	public String prosesUploadDokumenAsm(String no_blanko, HttpServletRequest request) throws DataAccessException, IOException, MailException, MessagingException{
		return commonDao.prosesUploadDokumenAsm(no_blanko, request);
	}
	
	public String prosesMoveDokumenAsm(String no_blanko, HttpServletRequest request, List<File> daftarFile, String tempCheckBoxString, String spaj) throws DataAccessException, IOException, MailException, MessagingException{
		return commonDao.prosesMoveDokumenAsm(no_blanko, request, daftarFile, tempCheckBoxString, spaj);
	}
	
	public int selectBPRatePerTransaksiSlink(String reg_spaj, int msl_tu_ke){
		return bacDao.selectBPRatePerTransaksiSlink(reg_spaj, msl_tu_ke);
	}
	
	/* Yusuf (08/03/2010) - Start of Travel Insurance */
	public TravelInsurance selectTravelInsuranceDet(int msti_id, int msti_jenis, int msid_no){
		return basDao.selectTravelInsuranceDet(msti_id, msti_jenis, msid_no);
	}
	public List<TravelInsurance> selectTravelInsuranceDet(int msti_id, int msti_jenis){
		return basDao.selectTravelInsuranceDet(msti_id, msti_jenis);
	}
	public TravelInsurance selectValidasiTravelInsurance(int msti_id, int msti_jenis){
		return basDao.selectValidasiTravelInsurance(msti_id, msti_jenis);
	}
	public double selectValidasiMaxUpTravelInsurance(TravelInsurance peserta){
		return basDao.selectValidasiMaxUpTravelInsurance(peserta);
	}
	public List<TravelInsurance> selectBandara(){
		return basDao.selectBandara();
	}
	public TravelInsurance selectTravelInsurance(int msti_id, int msti_jenis){
		return basDao.selectTravelInsurance(msti_id, msti_jenis);
	}
	public List<TravelInsurance> selectTravelInsurance(String lca_id, Integer posisi, Date tglAwal, Date tglAkhir){
		return basDao.selectTravelInsurance(lca_id, posisi, tglAwal, tglAkhir);
	}
	public String saveTravelInsurance(Command command, User currentUser){
		return basDao.saveTravelInsurance(command, currentUser);
	}
	public String saveTravelInsuranceDet(Command command, User currentUser){
		return basDao.saveTravelInsuranceDet(command, currentUser);
	}
	/* Yusuf (08/03/2010) - End of Travel Insurance */

	/* Yusuf (11/02/2010) - Start of Validasi Random Sample BII */
	public String saveMstQuestionnaire(String reg_spaj, String lus_id, List<Questionnaire> daftarQuestionnaire){
		basDao.deleteMstQuestionnaire(reg_spaj);
		boolean cleanCase = true;
		for(Questionnaire q : daftarQuestionnaire){
			q.setLus_id(Integer.valueOf(lus_id));
			basDao.insertMstQuestionnaire(q);
			if(q.msqu_jawab.intValue() == 0) cleanCase = false;
		}
		if(cleanCase) {
			uwDao.insertMstPositionSpaj(lus_id, "SAVE RANDOM SAMPLING KUESIONER BII (CLEAN CASE)", reg_spaj, 0);
		}else {
			uwDao.insertMstPositionSpaj(lus_id, "SAVE RANDOM SAMPLING KUESIONER BII (NON CLEAN CASE)", reg_spaj, 0);
		}
		return "Data Random Sampling berhasil disimpan.";
	}
	
	public List<Questionnaire> selectQuestionnaireFromSpaj(int lsqu_jenis, String reg_spaj){
		return basDao.selectQuestionnaireFromSpaj(lsqu_jenis, reg_spaj);
	}
	
	public Integer selectValidasiPinjamanKonvensional(String reg_spaj) {
		return bacDao.selectValidasiPinjamanKonvensional(reg_spaj);
	}
	
	public List select_produkutamamall() {
		return this.bacDao.select_produkutamamall();
	}	
	
	public List select_tipeprodukmall(){
		return this.bacDao.select_tipeprodukmall();
	}
	
	public String cekRandomSamplingBii(String reg_spaj) throws ParseException{
		String result 	= null;
		String ref 		= basDao.selectReferrerIdBiiFromSpaj(reg_spaj);
		Date begdate 	= basDao.selectBegDateInsuredFromSpaj(reg_spaj);
		Date feb1		= defaultDateFormat.parse("01/02/2010");
		
		//kalau bukan bancass BII, maka tidak perlu validasi ini
		if(!reg_spaj.startsWith("09") || ref == null){
			return null;
		//kalau polisnya sebelum feb 2010, maka tidak perlu validasi ini
		}else if(begdate.before(feb1)){
			return null;
		}else{
			List<Map> daftarTutupanBii 	= basDao.selectJumlahTutupanBiiPerReferrer(ref);
			int jml 					= daftarTutupanBii.size() + 1;
			if(!daftarTutupanBii.isEmpty()){
				String penutup 				= (String) daftarTutupanBii.get(0).get("NAMA_REF");
				if(jml == 1 || jml%5 == 0){
				result = 
					"Polis ini merupakan Polis tutupan ke-" + jml + " oleh " + penutup + " sejak Februari 2010.\\n" + 
					"Silahkan melakukan input Kuesioner Validasi Sample BII";
				}
			}
		}

		return result;
	}
	
	/* Yusuf (11/02/2010) - End of Validasi Random Sample BII */
	
	/* Yusuf (19/08/2011) - Start of Followup Billing Premi Lanjutan */
	public List<Followup> selectFollowupBilling(String jenis, Integer aging, String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String jn, String spaj) {
		return basDao.selectFollowupBilling(jenis, aging, begdate, enddate, lus_id, stfu, cabang, admin, jn, spaj);
	}
	/*
	public List<Followup> selectFollowupBilling(int aging, String lus_id, String stfu) {
		return basDao.selectFollowupBilling(aging, lus_id, stfu);
	}
	public List<Followup> selectFollowupBilling(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin) {
		return basDao.selectFollowupBilling(begdate, enddate, lus_id, stfu, cabang, admin);
	}
	*/
	public List<Followup> selectReportFollowupBilling(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String kat, String jn_tgl) {
		return basDao.selectReportFollowupBilling(begdate, enddate, lus_id, stfu, cabang, admin, kat, jn_tgl);
	}
	
	public List<Followup> selectReportFollowupBillingPerUser(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String kat, String rep, String jn_tgl) {
		return basDao.selectReportFollowupBillingPerUser(begdate, enddate, lus_id, stfu, cabang, admin, kat, rep, jn_tgl);
	}
	
	public List<Followup> selectDetailPolisFollowupBilling(String tahun_ke, String premi_ke, String reg_spaj) {
		return basDao.selectDetailPolisFollowupBilling(tahun_ke, premi_ke, reg_spaj);
	}
	public List<Followup> selectDetailPaymentFollowupBilling(String reg_spaj) {
		return basDao.selectDetailPaymentFollowupBilling(reg_spaj);
	}
	public List<Followup> selectHistoryFollowupBilling(String tahun_ke, String premi_ke, String reg_spaj) {
		return basDao.selectHistoryFollowupBilling(tahun_ke, premi_ke, reg_spaj);
	}
	public List<String> selectEmailAdminCabangFollowupBilling(String cabang, String admin) {
		return basDao.selectEmailAdminCabangFollowupBilling(cabang, admin);
	}
	public String selectAttachmentFollowupBilling(String tahun_ke, String premi_ke, String reg_spaj){
		return basDao.selectAttachmentFollowupBilling(tahun_ke, premi_ke, reg_spaj);
	}
	public boolean insertFollowupBilling(Followup fu, User currentUser, MultipartFile file, boolean kirimEmail, String emailto){
		return basDao.insertFollowupBilling(fu, currentUser, file, kirimEmail, emailto);
	}
	public boolean insertKontrolFollowupBilling(User currentUser, String cabang, String admin, String[] polis, String[] pp, String[] spaj, int[] thnke, int[] premike, String[] ket){
		return basDao.insertKontrolFollowupBilling(currentUser, cabang, admin, polis, pp, spaj, thnke, premike, ket);
	}
	
	/* Yusuf (19/08/2011) - End of Followup Billing Premi Lanjutan */	
	
	public int selectStatusPaidBilling(String reg_spaj, int tu_ke){
		String lsbs_id = uwDao.selectBusinessId(reg_spaj);
		if(products.stableLink(lsbs_id)){
			return bacDao.selectStatusPaidBillingStableLink(reg_spaj, tu_ke);
		}else if(products.powerSave(lsbs_id)){
			return bacDao.selectStatusPaidBilling(reg_spaj, 1, 1);
		}else{
			return 0;
		}
	}
	
	public Date selectBegDateSlinkPertama(String reg_spaj){
		return bacDao.selectBegDateSlinkPertama(reg_spaj);
	}
	
	public List<Map> selectLast10MessageOfTheDay(){
		return commonDao.selectLast10MessageOfTheDay();
	}
	
	public List<Map> selectDataPeserta(String reg_spaj){
		return uwDao.selectDataPeserta(reg_spaj);
	}
	
	public Map selectMessageOfTheDay(){ 
		return commonDao.selectMessageOfTheDay();
	}
	
	public void saveMessageOfTheDay(String tanggal, String pesan, String lus_id) throws DataAccessException, NumberFormatException, ParseException{
		commonDao.updateMstMessageDailyDeleteMessage(tanggal);
		Date sysdate = commonDao.selectSysdate();
		commonDao.insertMstMessageDaily(defaultDateFormat.parse(tanggal), pesan, sysdate, Integer.parseInt(lus_id), 1);
	}
	
	public HashMap selectJumlahBayarManfaatBulananTerakhir(String reg_spaj, int msl_no, Date msl_bdate){
		return bacDao.selectJumlahBayarManfaatBulananTerakhir(reg_spaj, msl_no, msl_bdate);
	}
	
	public Map selectPengurangManfaatBulananTerakhir(String reg_spaj, int msl_no, Date msl_edate){
		return bacDao.selectPengurangManfaatBulananTerakhir(reg_spaj, msl_no, msl_edate);
	}
	
	public List<Map> selectInfoStableLinkAll(String spaj){
		return uwDao.selectInfoStableLinkAll(spaj);
	}	
	
	public List<Map> selectInfoSlinkBayar(String reg_spaj, int msl_no){
		return uwDao.selectInfoSlinkBayar(reg_spaj, msl_no);
	}
	
	public List<Map> selectInfoStableLinkAllNew(String spaj){
		return uwDao.selectInfoStableLinkAllNew(spaj);
	}	
	
	public void testPerhitunganPajakNov2009(){
		transferPolis.testPerhitunganPajakNov2009();
	}
	
	public void prosesSavePenggantianSimasCard(String spaj, String mrc_no_kartu, String ket, String mrc_no_kartu_lama, User user){
		try{
			simasCardDao.prosesSavePenggantianSimasCard(spaj, mrc_no_kartu, ket, mrc_no_kartu_lama, user);
		}catch (Exception e) {
			logger.error("ERROR :", e);
		}
	}
	
	public void prosesKomisiROAgentPromosiKetinggalanJanuari2010(String spaj, User currentUser){
		try {
			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			
			//INI HANYA BOLEH DIGUNAKAN, BILA DI ROLLBACK
//			commonDao.delete("DELETE FROM EKA.MST_UPLOAD WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
//			commonDao.delete("DELETE FROM EKA.MST_UPLOAD_NON WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
//			commonDao.delete("DELETE FROM EKA.MST_UPLOAD_NON_NEW WHERE MSCO_ID IN (select msco_id from eka.mst_commission where reg_spaj = '" + spaj + "')");
			//
			
			transferPolis.prosesKomisiROAgentPromosiKetinggalanJanuari2010(spaj, currentUser);
			//produksi.prosesUlinkDetBill(spaj, defaultDateFormat.parse("30/04/2007"));
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
		
		
	}
	
	public int selectCountPowersaveCair(String spaj){
		return bacDao.selectCountPowersaveCair(spaj);
	}
	
	public int selectCountPremiSlink(String spaj){
		return bacDao.selectCountPremiSlink(spaj);
	}
	
	public List<Map> selectPendingBSM(String lcb_no){
		return commonDao.selectPendingBSM(lcb_no);
	}
	
	public void prosesSSKStableLinkKetinggalan(){
		try {
			List<String> daftar = commonDao.selectStableLinkRevisiSSU();
			//List<String> daftar = new ArrayList<String>();
			//daftar.add("09200930938");
			
			String path = props.getProperty("pdf.dir.export");
			for(String spaj : daftar){
				//HAPUS FILE SEBELUMNYA
				String cabang = uwDao.selectCabangFromSpaj(spaj);
				String dest = path+"\\"+cabang+"\\"+spaj;
				File dir = new File(dest+"\\"+"ssu.pdf");
				if(dir.exists()) dir.delete();			
				dir = new File(dest+"\\"+"null.pdf");
				if(dir.exists()) dir.delete();			
				dir = new File(dest+"\\"+"ssk.pdf");
				if(dir.exists()) dir.delete();
				dir = new File(dest+"\\"+"panduan_virtual_account.pdf");
				if(dir.exists()) dir.delete();
				
				//create folder, buat jaga2 aja
				dir = new File(dest);
				if(!dir.exists()) dir.mkdirs();
				
				//generate SSU nya
				File from = new File(props.getProperty("pdf.dir.syaratpolis") + "\\164-002-KHUSUS_BSM_AFTER1OCT.pdf");
				File to = new File(dest+ "\\" + props.getProperty("pdf.ssu") + ".pdf");
				FileUtils.copy(from, to);
				
				//bila dapat swineflu, generate SSK nya
				if(basDao.selectDapatSwineFlu(spaj)){
					from = new File(props.getProperty("pdf.dir.syaratpolis") + "\\RIDER\\822-001-STABLE.pdf");
					to = new File(dest + "\\" + props.getProperty("pdf.ssk") + ".pdf");
					FileUtils.copy(from, to);
				}
			}
		} catch (IOException e) {
			logger.error("ERROR :", e);
		}
	}
	
	public boolean selectDapatSwineFlu(String spaj){
		return basDao.selectDapatSwineFlu(spaj);
	}
	
	public List<Map> selectInfoStableSave(String spaj){
		return uwDao.selectInfoStableSave(spaj);
	}
	
	public void prosesBonusStableLinkYangKetinggalan() throws Exception{
		transferPolis.prosesBonusStableLinkYangKetinggalan();
	}
	
	public Map selectPolisLamaSurrenderEndorsement(String reg_spaj){
		return basDao.selectPolisLamaSurrenderEndorsement(reg_spaj);
	}
	
	public int selectAksesAdminTerhadapAgen(String lus_id, String msag_id){
		return basDao.selectAksesAdminTerhadapAgen(lus_id, msag_id);
	}
	
	public List<Map> selectSlipPajakPerAgen(String msag_id, String yyyymm){
		return basDao.selectSlipPajakPerAgen(msag_id, yyyymm);
	}

	public List<Map> selectSlipPajakPerAgenTaxRev(String msag_id, String yyyymm){
		return basDao.selectSlipPajakPerAgenTaxRev(msag_id, yyyymm);
	}

	public List<Map> selectSlipPajakPerAdminCabang(int lus_id, String yyyymm){
		return basDao.selectSlipPajakPerAdminCabang(lus_id, yyyymm);
	}

	public List<Map> selectSlipPajakPerAdminCabangTaxRev(int lus_id, String yyyymm){
		return basDao.selectSlipPajakPerAdminCabangTaxRev(lus_id, yyyymm);
	}

	public void prosesCekSimultanStableLink(String reg_spaj) {
		uwDao.prosesCekSimultanStableLink(reg_spaj);
	}
		
	public List<String> selectAllStableLink(){
		return commonDao.selectAllStableLink();
	}
	
	public List<String> selectStabilLinkAfterSept2009(){
		return commonDao.selectStabilLinkAfterSept2009();
	}
	
	public String selectTglJurnalFromPre(String nopre){
		return financeDao.selectTglJurnalFromPre(nopre);
	}
	
	public Powersave selectRolloverPowersaveTerakhir(String reg_spaj){
		return bacDao.selectRolloverPowersaveTerakhir(reg_spaj);
	}
	
	public Date selectPowerSaveRoSurrender(String reg_spaj){
		return bacDao.selectPowerSaveRoSurrender(reg_spaj);
	}
	
	
	public Date selectTanggalProduksiUntukProsesProduksi(Date tgl_rk, String lca_id, Integer jenis){
		return commonDao.selectTanggalProduksiUntukProsesProduksi(tgl_rk, lca_id, jenis);
	}
	
	public Integer selectGetMspoProvider(String spaj){
		return uwDao.selectGetMspoProvider(spaj);
	}
	
	public String selectBulanProduksi(String spaj){
		return this.uwDao.selectBulanProduksi(spaj);
	}
	
	public int selectFlagBulananStableLinkStableSave(String reg_spaj){
		return this.financeDao.selectFlagBulananStableLinkStableSave(reg_spaj);
	}
	
	public String selectPasswordFromUsername(String userName) throws DataAccessException{
		String result = this.bacDao.selectPasswordFromUsername(userName);
		return (result == null ? "_" : result.toUpperCase());
	}

	public List<Billing> selectBillingInformationSucc(String spaj, int lspd_id){
		return this.uwDao.selectBillingInformationSucc(spaj, lspd_id);
	}
	
	public List<Map> selectInfoPosisiTerakhir(String reg_spaj){
		return this.uwDao.selectInfoPosisiTerakhir(reg_spaj);
	}
	
	public List<Map> selectInfoPosisiPasTerakhir(String msp_id){
		return this.uwDao.selectInfoPosisiPasTerakhir(msp_id);
	}
	
	public List<Map> selectProduksiKe(String reg_spaj, int prod_ke){
		return this.bacDao.selectProduksiKe(reg_spaj, prod_ke);
	}
	
	public void transferMstPowersaveCair(CommandPowersaveCair cpc, User currentUser) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Date sysdate = commonDao.selectSysdateTruncated(0);
		
		String premiKe 	= "PREMI KE-";
		int posisiAwal 	= 0;
		int posisiAkhir = 0;
		
		for(PowersaveCair p : cpc.daftarPremi) {
			//proses hanya yg dicentang saja
			if(p.centang) {
				premiKe += p.mpc_tu_ke.toString() + ", ";
				
				posisiAwal = p.flag_posisi.intValue();
				
				if(p.flag_posisi == 1) {
					
					p.flag_posisi = 3;
					
					if(p.hari_kerja != null && currentUser.getJn_bank().intValue() == 2) {
						if(p.hari_kerja.intValue() <= 2) { //bila tgl pencairan maksimal 2 hari kerja lagi, maka ke pak ardy (khusus BSM)
							p.flag_posisi = 2;
						}
					}
					/*if(p.selisih_hari != null && currentUser.getJn_bank().intValue() == 2) {
						if(p.selisih_hari.intValue() <= 2) { //bila tgl pencairan maksimal 2 hari kerja lagi, Pas saat tranfer
							p.flag_posisi = 2;
						}
					}*/
										
				}else if(p.flag_posisi == 2) { //approve
					p.flag_posisi = 3;
				}

				//Yusuf (28/10/09) - Request Rudi/Himmia : user approve dan tanggalnya selalu diisi, baik itu transfer dari posisi 1 ataupun posisi 2
				p.lus_id_approve = Integer.valueOf(currentUser.getLus_id());
				p.tgl_approve = commonDao.selectSysdate();
				
				posisiAkhir = p.flag_posisi.intValue();
				
				//bila tanggal jatuh tempo adalah hari ini, langsung email ke LB
				if(p.mpc_edate.compareTo(sysdate) >= 0) {
					//String polis = this.uwDao.selectPolicyNumberFromSpaj(p.reg_spaj);
//					try {
//						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
//								null, 0, 0, new Date(), null, 
//								false, props.getProperty("admin.ajsjava"), 
//								props.getProperty("lb.emails").split(";"), 
//								null, null, 
//								"[E-Lions] Informasi : Telah dilakukan input pencairan pada hari H+0 untuk SPAJ " + p.reg_spaj + " / No Polis " + polis, 
//								"Telah dilakukan proses input pencairan pada hari H+0 untuk register SPAJ " + p.reg_spaj + " / No Polis " + polis, null, p.reg_spaj);
//					} catch (MailException e) {
//						logger.error("ERROR :", e);
//					}
				}
				
				bacDao.updateMstPowersaveCairPosisi(p);
			}
		}
		
		//setelah selesai looping semua, insert 1 entry saja di mst_position_spaj
		String bsm = "BSM";
		if(currentUser.getJn_bank().intValue()==3){
			bsm = "SMS";
		}else if(currentUser.getJn_bank().intValue()==16){
			bsm = "BSM SYARIAH";
		}
		uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Transfer pencairan "+bsm+" ("+premiKe+") dari posisi " + posisiAwal + " ke " + posisiAkhir, cpc.reg_spaj, 0);
	}
	
	public void updateMstPowersaveCairPosisi(CommandPowersaveCair cpc, User currentUser, int posisi) {
		for(PowersaveCair p : cpc.daftarPremi) {
			p.flag_posisi = posisi;
			bacDao.updateMstPowersaveCairPosisi(p);
		}
	}
	
	public String selectNoRegPencairan(String reg_spaj, int mpc_urut, Date mpc_bdate) {
		return bacDao.selectNoRegPencairan(reg_spaj, mpc_urut, mpc_bdate);
	}	
	
	public String deleteMstPowersaveCair(CommandPowersaveCair cpc, User currentUser) throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String daftarNoReg = "";
		
		for(PowersaveCair p : cpc.daftarPremi){
			//proses hanya yg dicentang saja
			if(p.centang){
				daftarNoReg += p.mpc_reg.toString() + ",";
				
				bacDao.deleteMstPowersaveCair(p);
			}			
		}
		//setelah selesai looping semua, insert 1 entry saja di mst_position_spaj
		String bsm = "BSM";
		if(currentUser.getJn_bank().intValue()==3){
			bsm="SMS";
		}
		uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Hapus pencairan "+bsm+" ("+daftarNoReg+")", cpc.reg_spaj, 0);
		return daftarNoReg;
	}
	
	public List<Map> selectSisaPremiStableLink(String reg_spaj){
		return bacDao.selectSisaPremiStableLink(reg_spaj);
	}
	
	public String insertMstPowersaveCair(CommandPowersaveCair cpc, User currentUser) {
	 // TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String daftarNoRegCair = "";
		
		for(PowersaveCair p : cpc.daftarPremi) {
			//proses hanya yg dicentang saja
			if(p.centang){

				p.mpc_note = cpc.powersaveCair.mpc_note;
				p.flag_posisi = 1; //insert 1, ardy 2, LB 3,4,5
				p.flag_proses = 0; //0 belum proses, 1 sudah proses
				p.flag_ps = products.stableLink(cpc.powersaveCair.lsbs_id.toString()) ? 2 : 1; //1 powersave, 2 stable link
				p.lus_id_input = Integer.valueOf(currentUser.getLus_id());
				p.tgl_input = commonDao.selectSysdate();
				
	//			String mpc_rider_total = bacDao.SelectTotalRiderPowersave(p.reg_spaj);
	//			if(mpc_rider_total==null || mpc_rider_total == ""){
	//				mpc_rider_total="0";
	//			}
	//			p.mpc_rider_total = Double.parseDouble(mpc_rider_total);
				
				if(p.mpc_noncash == null) 	p.mpc_noncash = 1;
	
				if(p.mpc_ktp == null) 		p.mpc_ktp = 0;
				if(p.mpc_spt == null) 		p.mpc_spt = 0;
				if(p.mpc_sph == null) 		p.mpc_sph = 0;
				if(p.mpc_polis == null) 	p.mpc_polis = 0;
				if(p.mpc_formrek == null) 	p.mpc_formrek = 0;
				if(p.mpc_jenis_noncash == null) p.mpc_jenis_noncash = 0;
							
				p.mpc_reg = this.sequence.sequenceNoRegPencairan(p.mpc_cair);
				daftarNoRegCair += p.mpc_reg + ",";
				
				bacDao.deleteMstPowersaveCair(p);
				bacDao.insertMstPowersaveCair(p);
			}
		}
		//setelah selesai looping semua, insert 1 entry saja di mst_position_spaj
		String bsm = "BSM";
		if(currentUser.getJn_bank().intValue()==3){
			bsm="SMS";
		}
		
		if(cpc.flag_update_sph!=null){
			if(cpc.flag_update_sph==1){
				bacDao.updateFlagSPH(cpc.reg_spaj, 1);
			}
		}
		
		uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Input Cair "+bsm+" ("+daftarNoRegCair+")", cpc.reg_spaj, 0);
		return daftarNoRegCair;
	}
	
	public Date selectAddWorkdays(Date tanggal, int tambah) {
		return commonDao.selectAddWorkdays(tanggal, tambah); 		
	}
	
	public double getBPrate(Date begdate, int mgi, int flag_bulanan) throws ParseException{
		return bacDao.getBPrate(begdate, mgi, flag_bulanan);
	}
	
	//public List<Map> selectDataNasabahSlink(String reg_spaj) {
	//	return bacDao.selectDataNasabahSlink(reg_spaj);
	//}
	
	public Integer selectSelisihBatasDate(PowersaveCair pc){
		String lsbs_id = uwDao.selectBusinessId(pc.reg_spaj);
		if(products.stableLink(lsbs_id)){
			return bacDao.selectSelisihBatasDateStableLink(pc);
		}else if(products.powerSave(lsbs_id)){
			return bacDao.selectSelisihBatasDatePowerSave(pc);
		}
		return null;
	}
		
	public List<PowersaveCair> selectRolloverNormal(String reg_spaj){
		String lsbs_id = uwDao.selectBusinessId(reg_spaj);
		if(products.stableLink(lsbs_id)){
			return bacDao.selectRolloverStableLinkNormal(reg_spaj);
		}else if(products.powerSave(lsbs_id)){
			return bacDao.selectRolloverPowersaveNormal(reg_spaj);
		}
		return new ArrayList<PowersaveCair>();
	}	
	
	public int selectAksesUserTerhadapSpaj(String reg_spaj, String lus_id){
		return commonDao.selectAksesUserTerhadapSpaj(reg_spaj, lus_id);
	}
	
	public List<Powersave> selectDaftarFileRolloverAtauTopup(String reg_spaj, String folder, List<DropDown> daftarFile){
		List<Powersave> daftar = null;
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		List<String> temp = new ArrayList<String>();
		
		//Hanya 1 tipe, yaitu Surat Rollover
		if(folder.equals("PowerSave")){
			
			daftar = commonDao.selectDaftarFileRolloverPowersave(reg_spaj); 
						
			for(Powersave p : daftar){
				//contoh : ROL_09142200804622_20081004.PDF
				String namaFile = "ROL_" + p.getMspo_policy_no() + "_" + df.format(p.getMps_deposit_date()) + ".PDF";
				for(DropDown d : daftarFile){
					if(d.getKey().toUpperCase().contains(namaFile.toUpperCase().trim())){ //bila ada file nya
						p.setMsl_desc("Surat Rollover Ke-" + p.getMsbi_premi_ke());
						p.setFileDirectory(d.getDesc());
						p.setFileName(d.getKey());
						p.setFileCreated(d.getValue());
						temp.add(d.getKey());
					}
				}
			}
			
		//Ada 3 tipe, yaitu Surat Rollover Pokok, Rollover Topup, dan Surat Topup Awal
		}else if(folder.equals("StableLink")){
			//1. daftar rollover baik untuk pokok maupun topup
			daftar = commonDao.selectDaftarFileRolloverStablelink(reg_spaj); 
			
			for(Powersave p : daftar){				
				//contoh : ROL_09164200800030_20090220.PDF / ROL_UTAMA_09164200800030_20090220.PDF
				String namaFileRoPokok1 = "ROL_" + p.getMspo_policy_no() + "_" + df.format(p.getMsl_bdate()) + ".PDF";
				String namaFileRoPokok2 = "ROL_UTAMA_" + p.getMspo_policy_no() + "_" + df.format(p.getMsl_bdate()) + ".PDF";
				String namaFileRoTopup = "ROL_TOP_UP_" + p.getMsl_tu_ke() + "_" + p.getMspo_policy_no() + "_" + df.format(p.getMsl_bdate()) + ".PDF";
				
				for(DropDown d : daftarFile){
					if(d.getKey().toUpperCase().contains(namaFileRoPokok1.toUpperCase().trim())){ //bila ada file rollover pokok model 1
						//p.setMsl_desc("Surat Rollover");
						p.setFileDirectory(d.getDesc());
						p.setFileName(d.getKey());
						p.setFileCreated(d.getValue());
						temp.add(d.getKey());
						break;
					}else if(d.getKey().toUpperCase().contains(namaFileRoPokok2.toUpperCase().trim())){ //bila ada file rollover pokok model 2
						//p.setMsl_desc("Surat Rollover");
						p.setFileDirectory(d.getDesc());
						p.setFileName(d.getKey());
						p.setFileCreated(d.getValue());
						temp.add(d.getKey());
						break;
					}else if(d.getKey().toUpperCase().contains(namaFileRoTopup.toUpperCase().trim())){ //bila ada file rollover topup
						//p.setMsl_desc("Surat Rollover Topup");
						p.setFileDirectory(d.getDesc());
						p.setFileName(d.getKey());
						p.setFileCreated(d.getValue());
						temp.add(d.getKey());
						break;
					}
				}
			}
			
			//2. daftar topup
			List<Powersave> daftar2 = commonDao.selectDaftarFileTopupStablelink(reg_spaj);

			for(Powersave p : daftar2){
				//contoh : TOP_UP_4_09164200901125_20090709.PDF
				String namaFileTopup = "TOP_UP_" + p.getMsl_tu_ke() + "_" + p.getMspo_policy_no() + "_" + df.format(p.getMsl_bdate()) + ".PDF";
				for(DropDown d : daftarFile){
					if(d.getKey().toUpperCase().contains(namaFileTopup.toUpperCase().trim())){ //bila ada file topup
						//p.setMsl_desc("Surat Topup");
						p.setFileDirectory(d.getDesc());
						p.setFileName(d.getKey());
						p.setFileCreated(d.getValue());
						temp.add(d.getKey());
					}
				}
			}
			
			if(!daftar2.isEmpty()) daftar.addAll(daftar2);
			
		}
		
		//file lainnya yang mungkin ada
		for(DropDown d : daftarFile){
			if(!temp.contains(d.getKey())){
				Powersave p = new Powersave();
				p.setFileDirectory(d.getDesc());
				p.setFileName(d.getKey());
				p.setFileCreated(d.getValue());
				daftar.add(p);
			}
		}
		
		return daftar;
	}
	
	public void updateEmail(String lus_id, String email){
		commonDao.updateEmail(lus_id, email);
	}
	
	public Double selectBonus(int lsco_jenis, int lev_comm, int lsco_year, int lsbs_id, int lsdbs_number){
		return uwDao.selectBonus(lsco_jenis, lev_comm, lsco_year, lsbs_id, lsdbs_number);
	}
	
	public String selectStatusAcceptFromSpaj(String reg_spaj){
		return commonDao.selectStatusAcceptFromSpaj(reg_spaj);
	}
	
	public String selectEmailUserInputFromSpaj(String reg_spaj){
		return commonDao.selectEmailUserInputFromSpaj(reg_spaj);
	}
	
	public String selectEmailUser(String lus_id){
		return commonDao.selectEmailUser(lus_id);
	}
	
	public String selectEmailCabangFromKodeAgen(String msag_id){
		return uwDao.selectEmailCabangFromKodeAgen(msag_id);
	}
	
	public String selectVirtualAccountSpaj(String reg_spaj){
		return financeDao.selectVirtualAccountSpaj(reg_spaj);
	}
	
	public List<Map> selectRateBankSinarmas(){
		return bacDao.selectRateBankSinarmas();
	}
	
	public List<Map> selectDaftarRiderEndors(String no_endors){
		return uwDao.selectDaftarRiderEndors(no_endors);
	}
	
	public List<Map> selectAutoRider(String no_endors){
		return uwDao.selectAutoRider(no_endors);
	}
	
	public String selectValidasiRatePowersaveNewBusinessFromSpaj(String reg_spaj){
		//bisa untuk powersave, stable link, stable save (LST_PWRSAVE_RATE)
		Map info = uwDao.selectRateYangDiberikan(reg_spaj);
		
		Date apr14 = null;
		try {
			apr14 = defaultDateFormat.parse("14/04/2009");
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
		
		//bila tidak ada, berarti bukan produk2 diatas
		if(info == null) return null;
		
		double rate 	= ((BigDecimal) info.get("RATE")).doubleValue();
		double premi 	= ((BigDecimal) info.get("PREMI")).doubleValue();
		int mgi 		= ((BigDecimal) info.get("MGI")).intValue();
		int lsbs		= ((BigDecimal) info.get("LSBS_ID")).intValue();
		int lsdbs 		= ((BigDecimal) info.get("LSDBS_NUMBER")).intValue();
		int flag_bulanan= ((BigDecimal) info.get("FLAG_BULANAN")).intValue();
		Date beg_date	= (Date) info.get("MSTE_BEG_DATE");
		String lku_id 	= (String) info.get("LKU_ID");
		n_prod n 		= new n_prod();
		n.setSqlMap(uwDao.getSqlMapClient());
		String lca_id = uwDao.selectLcaIdMstPolicyBasedSpaj(reg_spaj);

		int jenis = (mgi == 1 ? 60 : mgi == 3 ? 10 : mgi == 6 ? 20 : mgi == 12 ? 30 : mgi == 24 ? 40 : mgi == 36 ? 50 : 0);
		if(jenis == 0) return null;
		
		Map info2 = bacDao.selectbungaprosave(lku_id, String.valueOf(jenis), premi, beg_date, n.f_flag_rate_powersave(lsbs, lsdbs, flag_bulanan), 0);
		if(info2 == null) return null;
		
		double rate_asli 	= ((Double) info2.get("LPR_RATE")).doubleValue();
		double max			= 0.5; 
		
		if(lku_id.equals("02")){
			max = 1;
		}
		
		if(!products.isKaryawan(String.valueOf(lsbs), String.valueOf(lsdbs)) && 
				((rate - rate_asli) > max) &&
				beg_date.after(apr14) &&
				!lca_id.equals("62")){
			return "Special Rate tidak boleh melebihi " + max + "% dari rate asli. Polis TIDAK DAPAT DITRANSFER!";
		}
		
		return null;
	}
	
	public void updateRegSpajIntoNasabah(Nasabah nasabah, String reg_spaj){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String mns_kd_nasabah = nasabah.getMns_kd_nasabah();
		Integer jn_lead = nasabah.getJn_lead();
		List<Map> cek=selectBiiNonSelfGen(reg_spaj);
		String self_gen = null;
		if(!cek.isEmpty()){
			Map params = cek.get(0);
			self_gen = (String) params.get("JN_LEAD").toString();
			updateSelfIns(Integer.parseInt(self_gen), reg_spaj);
		}
		if(self_gen.equals(null)){
			self_gen = jn_lead.toString();
		}
		commonDao.updateRegSpajIntoNasabah(mns_kd_nasabah, reg_spaj, jn_lead);
		
	}
	
	public List<Powersave> selectStableLinkUntukPerhitunganBP(String reg_spaj, int msl_tu_ke){
		return bacDao.selectStableLinkUntukPerhitunganBP(reg_spaj, msl_tu_ke);
	}
	
	public Map selectValidasiSebelumEditDate(String reg_spaj){
		return bacDao.selectValidasiSebelumEditDate(reg_spaj);
	}
	
	public int selectCountRolloverPowersave(String reg_spaj){
		return bacDao.selectCountRolloverPowersave(reg_spaj);
	}
	
	public List<Map> selectReportServiceLevel(Map params) {
		return uwDao.selectReportServiceLevel(params);
	}
	
	public List<Map> selectJenisPrintPolisUlang(Map params){
		return uwDao.selectJenisPrintPolisUlang(params);
	}
	
	public List selectDaftarKomisi(String dist, String prod) {
		if(dist.equals("") || prod.equals("")) return new ArrayList();
		return uwDao.selectDaftarKomisi(dist, prod);
	}
	
	public List<Map> selectListPengirimanPolis(String startDate, String endDate, int kondisi){
		return uwDao.selectListPengirimanPolis(startDate, endDate, kondisi);
	}
	
	public List<ListPengirimanPolis> selectListPengirimanPolis(){
		return uwDao.selectListPengirimanPolis();
	}
	
	public List<ListSpajTtp> selectTtpTransferAgency(){
		return this.uwDao.selectTtpTransferAgency();
	}
	public List<ListPengirimanPolis> selectListPengirimanPolisUW(){
		return uwDao.selectListPengirimanPolisUW();
	}
		
	public List<Payment> selectListPayment(String reg_spaj){
		return uwDao.selectListPayment(reg_spaj);
	}
	
	public Payment selectMstPayment(String mspa_payment_id){
		return uwDao.selectMstPayment(mspa_payment_id);
	}
	
	public Payment selectMstDepositPremium(String reg_spaj){
		return uwDao.selectMstDepositPremium(reg_spaj);
	}
	
	public List<ListPengirimanPolis> selectListPengirimanPolisTotal(){
		return uwDao.selectListPengirimanPolisTotal();
	}
	
	public List<ListPengirimanPolis> selectListPengirimanPolisBalik(){
		return uwDao.selectListPengirimanPolisBalik();
	}
	
//	public StringBuffer selectprosesDoubleSPH(String[] daftar){
//		List<String> param = new ArrayList<String>();
//		StringBuffer SphDouble = new StringBuffer();
//		for( int i = 0 ; i < daftar.length ; i ++ )
//		{
//			String s = daftar[i].trim().replace(".", "");
//			param.add(s);
//			if(uwDao.selectCekDoubleSPH(s)>0){
//				SphDouble.append(s+",");
//			}
//		}
//		return SphDouble;
//		
//	}
	
	public void updatePengirimanPolisUW(User currentUser, String[] daftar, String kolom, Date sysdate, String[] daftarDetailSphOrSpt) {
		Command cmd = new Command();
		List<String> param = new ArrayList<String>();
		for( int i = 0 ; i < daftar.length ; i ++ )
		{
			String keterangan;
			String s = daftar[i].trim().replace(".", "");
			if( "SPH".equals( daftarDetailSphOrSpt[i] ) )
			{
				if(uwDao.selectCountKirimLB(s,"SPT KIRIM KE LIFE BENEFIT")==0){
					keterangan = "SPH KIRIM KE LIFE BENEFIT";
				}else{
					if(uwDao.selectCountKirimLB(s,"SPH KIRIM KE LIFE BENEFIT")==1){
						keterangan = "SPH KIRIM KE LIFE BENEFIT";
					}else{
						keterangan = "SPH DAN SPT KIRIM KE LIFE BENEFIT";
					}
					uwDao.deleteKirimLB(s, "KIRIM KE LIFE BENEFIT");
				}
				
			}
			else if( "SPT".equals( daftarDetailSphOrSpt[i] ) )
			{
				if(uwDao.selectCountKirimLB(s,"SPH KIRIM KE LIFE BENEFIT")==0){
					keterangan = "SPT KIRIM KE LIFE BENEFIT";
				}else{
					if(uwDao.selectCountKirimLB(s,"SPT KIRIM KE LIFE BENEFIT")==1){
						keterangan = "SPT KIRIM KE LIFE BENEFIT";
					}else{
						keterangan = "SPH DAN SPT KIRIM KE LIFE BENEFIT";
					}
					uwDao.deleteKirimLB(s, "KIRIM KE LIFE BENEFIT");
				}
			}
			else 
			{
				keterangan = "SPH DAN SPT KIRIM KE LIFE BENEFIT";
			}
			param.add(s);
			uwDao.insertMstPositionSpaj(currentUser.getLus_id(), keterangan, s, 0);
		}
//		for(String s : daftar) {
//			s = s.trim().replace(".", "");
//			param.add(s);
//			//TODO
//			uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "SPH KIRIM KE LIFE BENEFIT", s);
//		}
		cmd.setDaftar(param);
		cmd.setKolom(kolom);
		cmd.setSysdate(sysdate);
		uwDao.updatePengirimanPolis(cmd);
	}
	
	public List selectSubStatusAccept(Integer lssaId){
		return this.uwDao.selectSubStatusAccept(lssaId);
	}
	
	public List selectMstPositionSpajWithSubId(String spaj){
		return this.uwDao.selectMstPositionSpajWithSubId(spaj);
	}
	//cek status spt dan pengiriman email remainder ke direksi
	public List selectMstPositionSpajCekStatus(){
        return this.uwDao.selectMstPositionSpajCekStatus(null);
	}
	
	public String selectCabangFromSpaj(String spaj){
		return this.uwDao.selectCabangFromSpaj(spaj);
	}
	
	public String selectMspsDescBasedSpajAndOtorisasi(String spaj){
		return this.uwDao.selectMspsDescBasedSpajAndOtorisasi(spaj);
	}
	
	public Date selectTanggalCetakSertifikatAwal(String spaj, String msps_desc){
		return this.uwDao.selectTanggalCetakSertifikatAwal(spaj, msps_desc);
	}
	
	public void updatePengirimanPolisLB(User currentUser, List<Map> listUpdate, String kolom, Date sysdate, int baris) {
		Map m = listUpdate.get(baris);
		//Map n = (Map) m.get("reg_spaj");
		//logger.info(n.get("reg_spaj"));
		//for(int j =0; j<m.)
		
		String reg_spaj = (String) m.get("reg_spaj");
		String desc = (String) m.get("msps_desc");
		List<String> mspsDescBasedSpaj = uwDao.selectMspsDescBasedSpaj( reg_spaj );
		String deskripsi =null;
		List< String > saveDesc = new ArrayList<String>();
		if( mspsDescBasedSpaj != null && mspsDescBasedSpaj.size()> 0 )
		{
			for( int i = 0 ; i < mspsDescBasedSpaj.size() ; i ++ )
			{
				if( deskripsi == null )
					{ deskripsi = mspsDescBasedSpaj.get(i); }
				else 
					{ deskripsi = deskripsi + "," + mspsDescBasedSpaj.get(i); }
			}
			for( int i = 0 ; i < mspsDescBasedSpaj.size() ; i ++ )
			{
				if( mspsDescBasedSpaj.get(i) != null && mspsDescBasedSpaj.get(i).contains( "SPH DAN SPT KIRIM KE LIFE BENEFIT" ) )
				{
					if( deskripsi.contains( "SPH DAN SPT DITERIMA LIFE BENEFIT" ) )
					{
						
					}
					else
					{
						if(desc.length()==0){
							desc = "SPH DAN SPT DITERIMA LIFE BENEFIT";
						}else {
							desc = "SPH DAN SPT DITERIMA LIFE BENEFIT :" + desc.toUpperCase();
						}
						saveDesc.add( desc );
					}
				}
				else if( mspsDescBasedSpaj.get(i) != null && mspsDescBasedSpaj.get(i).contains( "SPH KIRIM KE LIFE BENEFIT" ) )
				{
					if( deskripsi.contains( "SPH DITERIMA LIFE BENEFIT" ) )
					{
						
					}
					else
					{
						if(desc.length()==0){
							desc = "SPH DITERIMA LIFE BENEFIT";
						}else {
							desc = "SPH DITERIMA LIFE BENEFIT :" + desc.toUpperCase();
						}
						saveDesc.add( desc );
					}
				}
				else if( mspsDescBasedSpaj.get(i) != null && mspsDescBasedSpaj.get(i).contains( "SPT KIRIM KE LIFE BENEFIT" ) )
				{
					if( deskripsi.contains( "SPT DITERIMA LIFE BENEFIT" ) )
					{
						
					}
					else
					{
						if(desc.length()==0){
							desc = "SPT DITERIMA LIFE BENEFIT";
						}else {
							desc = "SPT DITERIMA LIFE BENEFIT :" + desc.toUpperCase();
						}
						saveDesc.add( desc );
					}
				}
			}
		}
		else
		{
			
		}
		if( saveDesc != null && saveDesc.size() > 0 )
		{
			for( int i = 0 ; i < saveDesc.size() ; i ++ )
			{
				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), saveDesc.get(i), reg_spaj, 0);
			}
		}
		//uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "DITERIMA LIFE BENEFIT", reg_spaj);
		
		uwDao.updatePengirimanPolisTolak(reg_spaj, sysdate, kolom);
		
	}
	
	//public void tolakPengirimanPolis(User currentUser, String[] daftar, String[] keterangan, String kolom, Date sysdate) {
	public void tolakPengirimanPolis(User currentUser, List<Map> listTolak, String kolom, Date sysdate, int baris) {
		
		//for(int i=0; i<listTolak.size(); i++){
			Map m = listTolak.get(baris);
			//Map n = (Map) m.get("reg_spaj");
			//logger.info(n.get("reg_spaj"));
			//for(int j =0; j<m.)
			String reg_spaj = (String) m.get("reg_spaj");
			String desc = (String) m.get("msps_desc");
			uwDao.insertMstPositionSpaj(currentUser.getLus_id(), desc.toUpperCase(), reg_spaj, 0);
			uwDao.updatePengirimanPolisTolak(reg_spaj, sysdate, kolom);

	}
	
	public List<Map> selectListPengirimanPolis(String[] tambahan){
		return uwDao.selectListPengirimanPolis(tambahan);
	}
	
	public List selectKycNewBusiness(Date tgl_awal, Date tgl_akhir) {
		return uwDao.selectKycNewBusiness(tgl_awal, tgl_akhir);
	}
	
	public List<Maturity> selectReportMaturity(Map m) {
		return bacDao.selectReportMaturity(m);
	}
	
	public List selectReportFitrahCard(Map map) {
		return basDao.selectReportFitrahCard(map);
	}
	
	public List selectReportPrintPolis(Map map) {
		return uwDao.selectReportPrintPolis(map);
	}
	
	public List selectReportSummaryBiasa(Map map) {
		return bacDao.selectReportSummaryBiasa(map);
	}
	
	public List selectReportSimasCardDistribution(Map map) {
		return bacDao.selectReportSimasCardDistribution(map);
	}

	public List jenis_topproducer()
	{
		return this.uwDao.jenis_topproducer();
	}
	
	public List list_topproducer(Integer tahun , Integer id)
	{
		return this.uwDao.list_topproducer(tahun, id);
	}
	
	public Integer count_topproducer(Integer tahun , Integer id)
	{
		return (Integer)this.uwDao.count_topproducer(tahun, id);
	}
	
	public int selectCountOtorisasiSpaj(String spaj) {
		return bacDao.selectCountOtorisasiSpaj(spaj);
	}
	
	public List<DropDown> selectDaftarProdukKomisi(String dist){
		if(dist.trim().equals("")) return new ArrayList<DropDown>();
		return this.uwDao.selectDaftarProdukKomisi(dist);
	}
	
	/**
	 *@author Hemilda
	 *@param map
	 *@return  integer
	 *untuk mengecek flag_top_up
	 */
	public Integer f_prod_topup(String spaj, Integer prod_ke)
	{
		return (Integer)this.uwDao.f_prod_topup(spaj, prod_ke);
	}
	
	public Date selectTanggalLahirPemegang(String spaj) {
		return this.uwDao.selectTanggalLahirPemegang(spaj);
	}
	
	/**
	 *@author Hemilda
	 *@param user id
	 *@return  list
	 *untuk menampilkan list admin
	 */
	public List listadmin(String id)
	{
		return this.uwDao.listadmin(id);
	}
	
	/**
	 * Mengambil alasan pending print polis
	 */
	public List<Position> selectAlasanPendingPrintPolis(String spaj) {
		return this.uwDao.selectAlasanPendingPrintPolis(spaj);
	}
	
	/**
	 * Validasi untuk menandakan apakah spaj sudah diterima yang asli / belum
	 * @author Yusuf Sutarko
	 * @since Jun 23, 2007 (11:16:32 AM)
	 * @param spaj
	 * @return
	 */
	public int validasiSpajAsli(String spaj) {
		return uwDao.validasiSpajAsli(spaj);
	}
	
	/**
	 * validasi bea meterai
	 * 
	 * @author Yusuf
	 * */
	public String validasiBeaMeterai(Integer jn_bank) {
		List list = this.uwDao.selectMeterai();
		if(list.isEmpty()) return "Maaf, tetapi saldo bea meterai tidak ada. Silahkan ajukan surat permohonan bea meterai.";
		Meterai meterai = (Meterai) list.get(0);
		if(jn_bank != 2 || jn_bank != 3) return "Harus menggunakan meterai tera atau tempel!";
		if(meterai.getMstm_saldo_akhir() < 6000) return "Maaf, tetapi saldo bea meterai tidak mencukupi. Silahkan ajukan surat setoran tambahan bea meterai.";
		
		return null;
	}
	
//	public void SoftCopyOtomatis(String reg_spaj, User currentUser, String email){
//		try {
//			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//			printPolisMultiController.SoftCopyOtomatis(reg_spaj, currentUser, email);
//			
//		} catch (Exception e) {
//			logger.error("ERROR :", e);
//		}	
//	}
	
	/**
	 * warning untuk bea meterai < 1.000.000,-
	 * per 6 juli 2011, aturannya menjadi 3 juta, bukan 1 jt (request Ariani, helpdesk #20262)
	 * 
	 * @author Yusuf
	 * @throws ParseException 
	 * */
	public String warningBeaMeterai(Integer jn_bank) throws ParseException {
		List list = this.uwDao.selectMeterai();
		if(!list.isEmpty()) {
			Meterai meterai = (Meterai) list.get(0);
			
			Date now = this.commonDao.selectSysdate();
			Date tujuhJuli = defaultDateFormat.parse("07/07/2011");
			
			if(now.after(tujuhJuli)){
				if(jn_bank != 2 && jn_bank != 3){
					//MANTA (13/10/2014)
					return "Harap pergunakan meterai tera atau tempel sebelum mencetak Polis/Sertifikat.";
				}else if(meterai.getMstm_saldo_akhir() < 3000000 && meterai.getMstm_saldo_akhir() >= 6000){
					return "Saldo Bea Meterai saat ini adalah Rp. " + FormatString.formatCurrency("", new BigDecimal(meterai.getMstm_saldo_akhir())) + ". Silahkan pergunakan meterai tera atau tempel jika saldo sudah tidak mencukupi.";
				}else if(meterai.getMstm_saldo_akhir() < 6000){
					return "Saldo Bea Meterai saat ini sudah tidak mencukupi. Harap pergunakan meterai tera atau tempel sebelum mencetak Polis/Sertifikat.";
				}
			}else{
				if(meterai.getMstm_saldo_akhir() < 1000000) 
					return "Saldo Bea Meterai saat ini sudah dibawah Rp. 1.000.000,-. Silahkan ajukan surat setoran tambahan bea meterai.";
			}
		}
		return null;
	}
	
	public int updateTanggalKirimPolis(String spaj) {
		return uwDao.updateTanggalKirimPolis(spaj);
	}
	
	/**@Fungsi:	untuk insert stamp
	 * @param	model stamp
	 * @author 	Hemilda
	 * */
	public void insertmststamp(Stamp stamp) throws DataAccessException{
		this.uwDao.insertmststamp(stamp);
	}
	
	/**@Fungsi:	untuk insert history stamp
	 * @param	model stamp
	 * @author 	Hemilda
	 * */
	public void insertmststamp_hist(Stamp stamp) throws DataAccessException{
		this.uwDao.insertmststamp_hist(stamp);
	}		
	
	/**@Fungsi:	untuk saldo awal dan saldo akhir data terakhir
	 * @author 	Hemilda
	 * */
	public Map stamp_sekarang()
	{
		return (HashMap) this.uwDao.stamp_sekarang();
	}
	
	public Stamp cek_bea_meterai(Stamp data, User currentUser) throws ServletException,IOException
	{
		return this.savingBeaMeterai.insertbeameterai(data, currentUser);
	}	
	
	public Stamp edit_bea_meterai(Stamp data, User currentUser) throws ServletException,IOException
	{
		return this.savingBeaMeterai.updatebeameterai(data, currentUser);
	}	
	
	public int selectIsBreakable(String spaj) {
		return uwDao.selectIsBreakable(spaj);
	}
	
	public Map selectSimasCardExist(String spaj, String produk){
		return bacDao.selectSimasCardExist(spaj, produk);
	}
	
	public Date selectMaxDatePositionSpaj(String reg_spaj, String msps_desc){
		return commonDao.selectMaxDatePositionSpaj(reg_spaj, msps_desc);
	}
	
	public int selectPunyaEndors(String spaj){
		return uwDao.selectPunyaEndors(spaj);
	}
	
	public Map selectInformasiEmailSoftcopy(String spaj) {
		return uwDao.selectInformasiEmailSoftcopy(spaj);
	}

	public int selectJenisTerbitPolis(String spaj) {
		return uwDao.selectJenisTerbitPolis(spaj);
	}
	
	public String validationOtorisasiSekuritas(int lus_id) {
		return uwDao.validationOtorisasiSekuritas(lus_id);
	}
	
	public int validationCerdas(String spaj, int jn_nasabah) {
		return uwDao.validationCerdas(spaj, jn_nasabah);
	}
	
	public int validationPrintPolisCabang(String spaj) {
		return this.uwDao.validationPrintPolisCabang(spaj);
	}
	
	public String selectEmailCabang(String cabang) {
		StringBuffer result = new StringBuffer();
		List<String> emails = uwDao.selectEmailCabang(cabang);
		for(String email : emails) {
			if(email != null) if(!email.trim().equals("")) result.append(email+";");
		}
		return result.toString();
	}
	
	public String selectEmailCabangFromSpaj(String spaj) {
		return uwDao.selectEmailCabangFromSpaj(spaj);
	}

	public String selectSpajRecur(String spaj_r){
		return this.uwDao.selectSpajRecur(spaj_r);
	}
	
	public void updateValidForPrint(String spaj, String to[], String cc[], String subject, String message, User currentUser, String keterangan, String file) {
		//email.matches("^.+@[^\\.].*\\.[a-z]{2,}$")
		
		// *Andhika(29/01/2013)
		String lca_id = uwDao.selectLcaIdMstPolicyBasedSpaj(spaj);
				String outputDir = props.getProperty("pdf.dir.export") + "\\" + lca_id + "\\";
				String outputDirEmail = props.getProperty("pdf.dir.expiredemail") + "\\" + lca_id + "\\";
				String dir = props.getProperty("pdf.dir.expiredemail") + "//" + lca_id;
				String lsbs_id=this.selectLsbsId(spaj);
				File destDir = new File(dir + "//" + spaj);

				List<File> attachments = new ArrayList<File>();
				
				File sourceFile1 = new File(outputDir+"\\" + spaj + "\\" + spaj + "SPAJ 001.pdf"); // \\ebserver\pdfind\Report\60\
				File sourceFile2 = new File(outputDir+"\\" + spaj + "\\" + spaj + "PROPOSAL 001.pdf");
				File sourceFile3 = new File(outputDir+"\\" + spaj + "\\" + spaj + "FUBAH 001.pdf");
				
				if(sourceFile3.exists()){
					attachments.add(sourceFile3);
				}
				if(sourceFile2.exists()){
					attachments.add(sourceFile2);
				}
				if(sourceFile1.exists()){
					attachments.add(sourceFile1);
				}
				
				// *Rahmayanti(14/09/2015) - id helpdesk 77680
				// *Menambahkan file attachment khusus produk link
				if(file!=null&&products.unitLinkNew(lsbs_id)){
					if(!destDir.exists()) {
						destDir.mkdirs();
					}
					File embedfrom1 = new File(file);
					File embedto1 = new File(outputDirEmail+"\\" + spaj + "\\" + spaj + "DOKUMEN TAMBAHAN 001.pdf");
					try {
						FileCopyUtils.copy(embedfrom1, embedto1);
						if(embedto1.exists()){
							attachments.add(embedto1);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e);
					}
				}
			
		uwDao.updateValidForPrint(spaj);
		uwDao.saveMstTransHistory(spaj, "tgl_valid_print", null, null, null);
		uwDao.updateMst_policyEmptyPrintDate(spaj);
		String emailing =FormatString.convertArrayToOneRowString(to);
		uwDao.insertMstPositionSpaj(currentUser.getLus_id(),"VALID FOR PRINT "+keterangan+"("+emailing+")",spaj, 0);

		try {
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					false, props.getProperty("admin.ajsjava"), to, cc, null, 
					subject, message, attachments, spaj);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		}
	}
	
	public int updateAktifSimasCard(String spaj, int aktif) {
		return uwDao.updateAktifSimasCard(spaj, aktif);
	}
	
	public Integer selectJenisPenutupBII(String spaj) {
		return uwDao.selectJenisPenutupBII(spaj);
	}
	
	public void saveLeadReffBii(Map map) {
		List<Map> cek=selectBiiNonSelfGen((String) map.get("spaj"));
		if(!cek.isEmpty()){
			Map params = cek.get(0);
			int self_gen = ((BigDecimal) params.get("JN_LEAD")).intValue();
			updateSelfIns(self_gen, (String) map.get("spaj"));
		}
		uwDao.updateLeadReffBii((String) map.get("kode"), (String) map.get("spaj"));
	}
	
	public void saveTelemarketer(Map map) {
		uwDao.updateTelemarketer((String) map.get("tm_id"), (String) map.get("spv_code"),(String) map.get("spaj"));
	}
	
	public void saveSPVTelemarketer(Map map) {
		uwDao.updateSPVTelemarketer((String) map.get("spv_code"), (String) map.get("spaj"));
	}
	
	public Map selectLeadNasabahFromSpaj(String spaj) {
		Map result = uwDao.selectLeadNasabahFromSpaj(spaj);
		if(result == null) result = new HashMap();
		return result;
	}
	
	public String selectMstPolicyMspoNoKerjasama(String reg_spaj) {
		return (String)uwDao.selectMstPolicyMspoNoKerjasama(reg_spaj);
	}
	
	public String selectNoPB(String spaj) {
		return(String) uwDao.selectNoPB(spaj);
	}
	
	public Map selectTelemarketerFromSpaj(String spaj) {
		Map result = uwDao.selectTelemarketerFromSpaj(spaj);
		if(result == null) result = new HashMap();
		return result;
	}
	
	public List selectCariNasabah(String cari) {
		return uwDao.selectCariNasabah(cari);
	}
	
	public List selectCariTelemarketer(String cari) {
		return uwDao.selectCariTelemarketer(cari);
	}
	
	public List selectCariSPVTelemarketer(String cari) {
		return uwDao.selectCariSPVTelemarketer(cari);
	}
	
	public List selectDaftarPremiPertama(String spaj) {
		return uwDao.selectDaftarPremiPertama(spaj);
	}
	
	public List selectUnitLink(String spaj, int ke) {
		return uwDao.selectUnitLink(spaj, ke);
	} 
	
	public List selectDetailUnitLink(String spaj, int ke) {
		return uwDao.selectDetailUnitLink(spaj, ke);
	}
	
	public List selectBiayaUnitLink(String spaj, int ke) {
		return uwDao.selectBiayaUnitLink(spaj, ke);
	}
	
	public Integer selectMasaGaransiInvestasi(String reg_spaj, Integer tahun_ke, Integer premi_ke) {
		return this.uwDao.selectMasaGaransiInvestasi(reg_spaj, tahun_ke, premi_ke);
	}
	
	public Map selectInfoAgen2(String spaj) {
		return uwDao.selectInfoAgen2(spaj);
	}
	
	public Map selectReferalInput(String spaj) {
		return uwDao.selectReferalInput(spaj);
	}
	
	public List selectDaftarSPAJUnitLink(int lspd) {
		return this.uwDao.selectDaftarSPAJUnitLink(lspd);
	}
	
	public void updateStatusPrintUnitLink(int lspd) {
		List daftar = this.uwDao.selectDaftarSPAJUnitLink(lspd);
		for(int i=0; i<daftar.size(); i++) {
			String spaj = (String) ((HashMap) daftar.get(i)).get("REG_SPAJ");
			BigDecimal muke = (BigDecimal) ((HashMap) daftar.get(i)).get("MU_KE");
			this.uwDao.updateStatusPrintUlink(2, spaj, muke);
			this.uwDao.updateStatusPrintTransUlink(2, spaj, muke);
		}
	}
	
	public List selectLaporanKustodian(String dk, List spaj) {
		return this.uwDao.selectLaporanKustodian(dk, spaj);
	}
	
	public List selectLaporanKustodianFinance(String dk, List spaj) {
		return this.uwDao.selectLaporanKustodianFinance(dk, spaj);
	}
	
	public void deleteMstDetMedical(Medical medical){
		uwDao.deleteMstDetMedical(medical);
	}
	
	public void insertLst_ulangan(String reg_spaj, Date tanggal, String jenis, Integer status_polis, String lus_id, String ket) {
		this.uwDao.insertLst_ulangan(reg_spaj, tanggal, jenis, status_polis, lus_id, ket);
	}
	
	public void insertMstHistoryUpload(String upload_id, String code_id, String old_code_id, String filename, String temp_filename, Date revisi_date, String upload_jenis, String status, Integer revisi, Integer lus_id, String keterangan, String path, Date upload_date){
		this.commonDao.insertMstHistoryUpload(upload_id, code_id, old_code_id, filename, temp_filename, revisi_date, upload_jenis, status, revisi, lus_id, keterangan, path, upload_date);
	}
	
	public void insertLampiranMstHistoryUpload(String upload_id, String code_id, String old_code_id, String filename, String temp_filename, Date revisi_date, String upload_jenis, String status, Integer revisi, Integer lus_id, String keterangan, String path, String jabatan, String ketentuan, Date upload_date){
		this.commonDao.insertLampiranMstHistoryUpload(upload_id, code_id, old_code_id, filename, temp_filename, revisi_date, upload_jenis, status, revisi, lus_id, keterangan, path, jabatan, ketentuan, upload_date);
	}
	
	public void updateMstHistoryUpload(String temp_filename, String status){
		commonDao.updateMstHistoryUpload(temp_filename, status);
	}	
	
/*	public void updateLstReffBii(String ls_cab, Integer ref, Integer flag, String kd_agent, String nm_agent,Date tglAktif, Date tglNonAktif, String pos_code, Date tglUpdate, Integer jn_bank){
		commonDao.updateLstReffBii(ls_cab, ref, flag, kd_agent, nm_agent,tglAktif,tglNonAktif,pos_code,tglUpdate,jn_bank);
	}
	*/
	public void insertMst_position_spaj_pb(String spaj, String lus_id, int lspd, int lssp, String desc,Integer count) {
		 this.bacDao.insertMst_position_no_spaj_pb(spaj, lus_id, lspd, lssp, desc,count);
	 }
	
	public List selectUwRenewal(String spaj)throws DataAccessException{
		return uwDao.selectUwRenewal(spaj);
	}
	
	public List selectRenewalDU(String spaj, String tim)throws DataAccessException{
		return uwDao.selectRenewalDU(spaj, tim);
	}	

	public void insertMstPositionSpajRenewal(String lus_id, String reg_spaj, String msps_desc){
		this.uwDao.insertMstPositionSpajRenewal(lus_id, reg_spaj, msps_desc);
	}
	
	public void insertMst_position_spaj_ttp( String msps_desc, String reg_spaj) throws DataAccessException{
		Map p = new HashMap();
		p.put("msps_desc", msps_desc);
		p.put("reg_spaj", reg_spaj);
		this.bacDao.insertMst_position_spaj_ttp(msps_desc, reg_spaj);
}
	
	public void prosesMedical(Command command){
		
		if(command.getFlagAdd()==2){//simpan
			//hapus semua medis
			uwDao.deleteMstDetMedicalNew(command.getSpaj(),null);
			for(int i=0;i<command.getLsMedical().size();i++){
				Medical medical=(Medical)command.getLsMedical().get(i);
				medical.setMpa_number(i+1);
				uwDao.insertMstDetMedical(medical);
			}
		}else if(command.getFlagAdd()==3){//delete
			for(int i=0;i<command.getLsMedical().size();i++){
				Medical medical=(Medical)command.getLsMedical().get(i);
				if(medical.getCek()!=null && medical.getCek()==1)
					uwDao.deleteMstDetMedicalNew(command.getSpaj(),medical.getMpa_number());
			}
		}
	}
	
	public void prosesIcd(Command command){
		
		if(command.getFlagAdd()==2){//simpan
			//hapus semua icd
			uwDao.deleteMstDetIcdNew(command.getSpaj(),null);
			for(int i=0;i<command.getLsIcd().size();i++){
				Icd icdTemp =(Icd)command.getLsIcd().get(i);
				icdTemp.setMpa_number(i+1);
				uwDao.insertMstDetIcd(icdTemp);
			}
		}else if(command.getFlagAdd()==3){//delete
			for(int i=0;i<command.getLsIcd().size();i++){
				Icd icdTemp = (Icd)command.getLsIcd().get(i);
				if(icdTemp.getCek()!=null && icdTemp.getCek()==1)
					uwDao.deleteMstDetIcdNew(command.getSpaj(),icdTemp.getMpa_number());
			}
		}
	}	
	
	public void prosesHslReas(Command command){
		
		if(command.getFlagAdd()==2){//simpan
			//hapus semua icd
			uwDao.deleteMstDetHslReasNew(command.getSpaj(),null);
			for(int i=0;i<command.getLsHslReas().size();i++){
				HslReas hslReasTemp =(HslReas)command.getLsHslReas().get(i);
				hslReasTemp.setMpa_number(i+1);
				uwDao.insertMstDetHslReas(hslReasTemp);
			}
		}else if(command.getFlagAdd()==3){//delete
			for(int i=0;i<command.getLsHslReas().size();i++){
				HslReas hslReasTemp =(HslReas)command.getLsHslReas().get(i);
				if(hslReasTemp.getCek()!=null && hslReasTemp.getCek()==1)
					uwDao.deleteMstDetHslReasNew(command.getSpaj(),hslReasTemp.getMpa_number());
			}
		}
	}	
	
	public void insertRider(Rider rider){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		uwDao.insertRider(rider);
	}
	
	public List selectAdminRegion() {
		return this.uwDao.selectAdminRegion();
	}
	
	public List selectAgenCekKomisi(String msag) {
		return this.uwDao.selectAgenCekKomisi(msag);
	}
	
	public String selectMstformGetNoBlanko(String msf_id){
		return this.basDao.selectMstformGetNoBlanko(msf_id);
	}
	
	public String selectEmailCabangTtp(String lca_id) {
		return this.uwDao.selectEmailCabangTtp(lca_id);
	}

	public String selectNmCabangFromSpaj(String lca_id) {
		return this.uwDao.selectNmCabangFromSpaj(lca_id);
	}
	
	public String selectJenisReferral(String mns_kd_nasabah) {
		return this.uwDao.selectJenisReferral(mns_kd_nasabah);
	}
	
	public Integer getJumlahEkaSehat(String reg_spaj){
		return this.bacDao.getJumlahEkaSehat(reg_spaj);
	}
	
	public Integer selectCountEkaSehatAdmedikaNew(String reg_spaj, int flagJenisEkasehat){
		return this.bacDao.selectCountEkaSehatAdmedikaNew(reg_spaj,flagJenisEkasehat);
	}
	
	public Integer selectCountEkaSehatAdmedikaHCP(String reg_spaj){
		return this.bacDao.selectCountEkaSehatAdmedikaHCP(reg_spaj);
	}

	public String selectAgenFromSpaj(String spaj) {
		return this.uwDao.selectAgenFromSpaj(spaj);
	}
	public Integer selectpremiKe(String spaj) {
		return this.uwDao.selectpremiKe(spaj);
	}
	
	public String selectLsbsId(String spaj) {
		return this.uwDao.selectLsbsId(spaj);
	}
	
	public String selectLsdbsNumber(String spaj) {
		return this.uwDao.selectLsdbsNumber(spaj);
	}
	
	public Integer selectCountEkaSehatAndHCP(String spaj) {
		return this.uwDao.selectCountEkaSehatAndHCP(spaj);
	}
	
	public List selectLstPengecualian(){
		return this.uwDao.selectLstPengecualian();
	}
	
	public Map selectRegSpajMstTempDMTM(String spaj){
		return this.uwDao.selectRegSpajMstTempDMTM(spaj);
	}
	
	public Integer selectCountMstTempDMTM(String spaj){
		return this.uwDao.selectCountMstTempDMTM(spaj);
	}
	
	public Integer selectCountSwineFlu(String spaj) {
		return this.uwDao.selectCountSwineFlu(spaj);
	}
	
	public List<HashMap> selectLsbsIdRiderHCPOrEkaSehat(String spaj) {
		return uwDao.selectLsbsIdRiderHCPOrEkaSehat(spaj);
	}
	
	public List<Map> selectFilterSpajNew(String tipe, String kata, String tglLahir, String pilter, Boolean flagLifeOnly) {
		return this.uwDao.selectFilterSpajNew(tipe, kata, tglLahir, pilter, flagLifeOnly);
	}	
	
	public List<SortedMap> selectAkumulasiPolisBySpaj(String spaj) {
		return uwDao.selectAkumulasiPolisBySpaj(spaj);
	}
	
	public String getKetMedis(String spaj) {
		return uwDao.getKetMedis(spaj);
	}

	public Map selectEmBmi(Double bmi, Integer umur) {
		return uwDao.selectEmBmi(bmi, umur);
	}
	
	public Map selectEmWorksheet(Integer lw_id) {
		return uwDao.selectEmWorksheet(lw_id);
	}
	
	public String selectEmBloodPresure(Integer systolic, Integer diastolic, Integer umur) {
		return uwDao.selectEmBloodPresure(systolic,diastolic,umur);
	}
	
	public String getRatioChol(Integer satuan, Double total, Double hdl, Integer umur) {
		return uwDao.getRatioChol(satuan,total,hdl,umur);
	}
	
	public List<HashMap> selectDaftarPeserta(String spaj) {
		return uwDao.selectDaftarPeserta(spaj);
	}
	
	public List<DropDown> selectLstWorksheet(Integer jenis, Integer id) {
		return uwDao.selectLstWorksheet(jenis, id);
	}
	
	public Map getHasilReas(String spaj, Integer insured_no) {
		return uwDao.getHasilReas(spaj,insured_no);
	}
	
	public Map selectRujukanMedis(String rsnama, String nama_medis, Integer msw_sex, Integer usia, String jenis) {
		return uwDao.selectRujukanMedis(rsnama,nama_medis,msw_sex,usia,jenis);
	}

	public String selectLstDetBisnisNamaProduk(Integer lsbsId, Integer lsdbsNumber){
		return uwDao.selectLstDetBisnisNamaProduk(lsbsId,lsdbsNumber);
	}
	
	public Integer cekMstWorksheet(String reg_spaj, Integer insured_no){
		return uwDao.cekMstWorksheet(reg_spaj, insured_no);
	}
	
	public String prosesUwWorksheet(UwWorkSheet ws) {
		// input mst_worksheet
		if(ws.getMsw_medis() == 0) {
			ws.setSumMCU(null);
			ws.setJenisMedis(null);
		}
		else if(ws.getMsw_medis() == 1) {
			ws.setNonMedisKelainan(null);
			ws.setNonMedisKelainanKelainan(null);
		}
		if(uwDao.cekMstWorksheet(ws.getReg_spaj(),ws.getInsured_no()) > 0) uwDao.updateMstWorksheet(ws);
		else uwDao.insertMstWorksheet(ws);
		
		// input detil non-medis
		if(ws.getMsw_medis() == 0) {
			// input questionnaire non-medis
			for(int a=0;a<ws.getListQuest().size();a++) {
				if(uwDao.cekListQuest(ws.getReg_spaj(),ws.getInsured_no(),a+1) > 0) uwDao.updateListQuest(ws.getListQuest().get(a));
				else uwDao.insertLstWorksheetQuestionnaire(ws.getListQuest().get(a));
			}
		}// input detil medis
		else if(ws.getMsw_medis() == 1) {
			// input lpk
			for(int a=0;a<ws.getListLpk().size();a++) {
				if(ws.getListLpk().get(a).getTglMcuLpk().equals("") && ws.getListLpk().get(a).getTmpMcuLpk().equals("")) continue;
				ws.getListLpk().get(a).setReg_spaj(ws.getReg_spaj());
				ws.getListLpk().get(a).setInsured_no(ws.getInsured_no());
				if(uwDao.cekListLpk(ws.getReg_spaj(),ws.getInsured_no(),ws.getListLpk().get(a).getUrutanLpk()) > 0) uwDao.updateListLpk(ws.getListLpk().get(a));
				else uwDao.insertMedLpk(ws.getListLpk().get(a));
				
				for(int b=0;b<ws.getListLpk().get(a).getListRpd().size();b++) {
					if(!ws.getListLpk().get(a).getListRpd().get(b).getRp_desc().equals("")) {
						ws.getListLpk().get(a).getListRpd().get(b).setReg_spaj(ws.getListLpk().get(a).getReg_spaj());
						ws.getListLpk().get(a).getListRpd().get(b).setInsured_no(ws.getListLpk().get(a).getInsured_no());
						ws.getListLpk().get(a).getListRpd().get(b).setUrutanLpk(ws.getListLpk().get(a).getUrutanLpk());
						ws.getListLpk().get(a).getListRpd().get(b).setRp_urutan(b+1);
						ws.getListLpk().get(a).getListRpd().get(b).setRp_type(1);
						
						if(uwDao.cekLstRiwPenyakit(ws.getListLpk().get(a).getListRpd().get(b).getReg_spaj(),
												   ws.getListLpk().get(a).getListRpd().get(b).getInsured_no(),
												   ws.getListLpk().get(a).getListRpd().get(b).getUrutanLpk(),
												   ws.getListLpk().get(a).getListRpd().get(b).getRp_urutan(),
												   ws.getListLpk().get(a).getListRpd().get(b).getRp_type()) > 0) uwDao.updateLstRiwPenyakit(ws.getListLpk().get(a).getListRpd().get(b));
						else uwDao.insertLstRiwPenyakit(ws.getListLpk().get(a).getListRpd().get(b));
					}
				}
				for(int b=0;b<ws.getListLpk().get(a).getListRps().size();b++) {
					if(!ws.getListLpk().get(a).getListRps().get(b).getRp_desc().equals("")) {
						ws.getListLpk().get(a).getListRps().get(b).setReg_spaj(ws.getListLpk().get(a).getReg_spaj());
						ws.getListLpk().get(a).getListRps().get(b).setInsured_no(ws.getListLpk().get(a).getInsured_no());
						ws.getListLpk().get(a).getListRps().get(b).setUrutanLpk(ws.getListLpk().get(a).getUrutanLpk());
						ws.getListLpk().get(a).getListRps().get(b).setRp_urutan(b+1);
						ws.getListLpk().get(a).getListRps().get(b).setRp_type(2);
						
						if(uwDao.cekLstRiwPenyakit(ws.getListLpk().get(a).getListRps().get(b).getReg_spaj(),
								   ws.getListLpk().get(a).getListRps().get(b).getInsured_no(),
								   ws.getListLpk().get(a).getListRps().get(b).getUrutanLpk(),
								   ws.getListLpk().get(a).getListRps().get(b).getRp_urutan(),
								   ws.getListLpk().get(a).getListRps().get(b).getRp_type()) > 0) uwDao.updateLstRiwPenyakit(ws.getListLpk().get(a).getListRps().get(b));
						else uwDao.insertLstRiwPenyakit(ws.getListLpk().get(a).getListRps().get(b));
					}
				}
				for(int b=0;b<ws.getListLpk().get(a).getListRpk().size();b++) {
					if(!ws.getListLpk().get(a).getListRpk().get(b).getRp_desc().equals("")) {
						ws.getListLpk().get(a).getListRpk().get(b).setReg_spaj(ws.getListLpk().get(a).getReg_spaj());
						ws.getListLpk().get(a).getListRpk().get(b).setInsured_no(ws.getListLpk().get(a).getInsured_no());
						ws.getListLpk().get(a).getListRpk().get(b).setUrutanLpk(ws.getListLpk().get(a).getUrutanLpk());
						ws.getListLpk().get(a).getListRpk().get(b).setRp_urutan(b+1);
						ws.getListLpk().get(a).getListRpk().get(b).setRp_type(3);
						
						if(uwDao.cekLstRiwPenyakit(ws.getListLpk().get(a).getListRpk().get(b).getReg_spaj(),
								   ws.getListLpk().get(a).getListRpk().get(b).getInsured_no(),
								   ws.getListLpk().get(a).getListRpk().get(b).getUrutanLpk(),
								   ws.getListLpk().get(a).getListRpk().get(b).getRp_urutan(),
								   ws.getListLpk().get(a).getListRpk().get(b).getRp_type()) > 0) uwDao.updateLstRiwPenyakit(ws.getListLpk().get(a).getListRpk().get(b));
						else uwDao.insertLstRiwPenyakit(ws.getListLpk().get(a).getListRpk().get(b));
					}
				}
				for(int b=0;b<ws.getListLpk().get(a).getListKelainan().size();b++) {
					if(!ws.getListLpk().get(a).getListKelainan().get(b).getRp_desc().equals("")) {
						ws.getListLpk().get(a).getListKelainan().get(b).setReg_spaj(ws.getListLpk().get(a).getReg_spaj());
						ws.getListLpk().get(a).getListKelainan().get(b).setInsured_no(ws.getListLpk().get(a).getInsured_no());
						ws.getListLpk().get(a).getListKelainan().get(b).setUrutanLpk(ws.getListLpk().get(a).getUrutanLpk());
						ws.getListLpk().get(a).getListKelainan().get(b).setRp_urutan(b+1);
						ws.getListLpk().get(a).getListKelainan().get(b).setRp_type(4);
						
						if(uwDao.cekLstRiwPenyakit(ws.getListLpk().get(a).getListKelainan().get(b).getReg_spaj(),
								   ws.getListLpk().get(a).getListKelainan().get(b).getInsured_no(),
								   ws.getListLpk().get(a).getListKelainan().get(b).getUrutanLpk(),
								   ws.getListLpk().get(a).getListKelainan().get(b).getRp_urutan(),
								   ws.getListLpk().get(a).getListKelainan().get(b).getRp_type()) > 0) uwDao.updateLstRiwPenyakit(ws.getListLpk().get(a).getListKelainan().get(b));
						else uwDao.insertLstRiwPenyakit(ws.getListLpk().get(a).getListKelainan().get(b));
					}
				}
			}
			
			// input urin
			for(int a=0;a<ws.getListUrin().size();a++) {
				if(ws.getListUrin().get(a).getTglMcuUrin().equals("") && ws.getListUrin().get(a).getTmpMcuUrin().equals("")) continue;
				ws.getListUrin().get(a).setReg_spaj(ws.getReg_spaj());
				ws.getListUrin().get(a).setInsured_no(ws.getInsured_no());
				if(uwDao.cekListUrin(ws.getReg_spaj(),ws.getInsured_no(),ws.getListUrin().get(a).getUrutanUrin()) > 0) uwDao.updateListUrin(ws.getListUrin().get(a));
				else uwDao.insertMedUrin(ws.getListUrin().get(a));
			}
				
			// input ada-adal
			for(int a=0;a<ws.getListAda().size();a++) {
				if(ws.getListAda().get(a).getTglMcuAda().equals("") && ws.getListAda().get(a).getTmpMcuAda().equals("")) continue;
				ws.getListAda().get(a).setReg_spaj(ws.getReg_spaj());
				ws.getListAda().get(a).setInsured_no(ws.getInsured_no());
				if(uwDao.cekListAda(ws.getReg_spaj(),ws.getInsured_no(),ws.getListAda().get(a).getUrutanAda()) > 0) uwDao.updateListAda(ws.getListAda().get(a));
				else uwDao.insertMedAda(ws.getListAda().get(a));
			}
			
			// input hiv
			for(int a=0;a<ws.getListHiv().size();a++) {
				if(ws.getListHiv().get(a).getTglMcuHiv().equals("") && ws.getListHiv().get(a).getTmpMcuHiv().equals("")) continue;
				ws.getListHiv().get(a).setReg_spaj(ws.getReg_spaj());
				ws.getListHiv().get(a).setInsured_no(ws.getInsured_no());
				if(uwDao.cekListHiv(ws.getReg_spaj(),ws.getInsured_no(),ws.getListHiv().get(a).getUrutanHiv()) > 0) uwDao.updateListHiv(ws.getListHiv().get(a));
				else uwDao.insertMedHiv(ws.getListHiv().get(a));
			}
			
			// input tumor maker
			for(int a=0;a<ws.getListTumor().size();a++) {
				if(ws.getListTumor().get(a).getTglMcuTumor().equals("") && ws.getListTumor().get(a).getTmpMcuTumor().equals("")) continue;
				ws.getListTumor().get(a).setReg_spaj(ws.getReg_spaj());
				ws.getListTumor().get(a).setInsured_no(ws.getInsured_no());
				if(uwDao.cekListTumor(ws.getReg_spaj(),ws.getInsured_no(),ws.getListTumor().get(a).getUrutanTumor()) > 0) uwDao.updateListTumor(ws.getListTumor().get(a));
				else uwDao.insertMedTumor(ws.getListTumor().get(a));
			}
			
			// input usg abdomen
			for(int a=0;a<ws.getListAbdomen().size();a++) {
				if(ws.getListAbdomen().get(a).getTglMcuAbdomen().equals("") && ws.getListAbdomen().get(a).getTmpMcuAbdomen().equals("")) continue;
				ws.getListAbdomen().get(a).setReg_spaj(ws.getReg_spaj());
				ws.getListAbdomen().get(a).setInsured_no(ws.getInsured_no());
				if(uwDao.cekListAbdomen(ws.getReg_spaj(),ws.getInsured_no(),ws.getListAbdomen().get(a).getUrutanAbdomen()) > 0) uwDao.updateListAbdomen(ws.getListAbdomen().get(a));
				else uwDao.insertMedAbdomen(ws.getListAbdomen().get(a));
			}
			
			// input rontgen dada pa
			for(int a=0;a<ws.getListDadaPa().size();a++) {
				if(ws.getListDadaPa().get(a).getTglMcuDadaPA().equals("") && ws.getListDadaPa().get(a).getTmpMcuDadaPA().equals("")) continue;
				ws.getListDadaPa().get(a).setReg_spaj(ws.getReg_spaj());
				ws.getListDadaPa().get(a).setInsured_no(ws.getInsured_no());
				if(uwDao.cekListDadaPa(ws.getReg_spaj(),ws.getInsured_no(),ws.getListDadaPa().get(a).getUrutanDadaPA()) > 0) uwDao.updateListDadaPa(ws.getListDadaPa().get(a));
				else uwDao.insertMedDadaPa(ws.getListDadaPa().get(a));
			}
			
			// input ekg
			for(int a=0;a<ws.getListEkg().size();a++) {
				if(ws.getListEkg().get(a).getTglMcuEkg().equals("") && ws.getListEkg().get(a).getTmpMcuEkg().equals("")) continue;
				ws.getListEkg().get(a).setReg_spaj(ws.getReg_spaj());
				ws.getListEkg().get(a).setInsured_no(ws.getInsured_no());
				if(uwDao.cekListEkg(ws.getReg_spaj(),ws.getInsured_no(),ws.getListEkg().get(a).getUrutanEkg()) > 0) uwDao.updateListEkg(ws.getListEkg().get(a));
				else uwDao.insertMedEkg(ws.getListEkg().get(a));
			}
			
			// input treadmill test
			for(int a=0;a<ws.getListTreadmill().size();a++) {
				if(ws.getListTreadmill().get(a).getTglMcuTreadmill().equals("") && ws.getListTreadmill().get(a).getTmpMcuTreadmill().equals("")) continue;
				ws.getListTreadmill().get(a).setReg_spaj(ws.getReg_spaj());
				ws.getListTreadmill().get(a).setInsured_no(ws.getInsured_no());
				if(uwDao.cekListTreadmill(ws.getReg_spaj(),ws.getInsured_no(),ws.getListTreadmill().get(a).getUrutanTreadmill()) > 0) uwDao.updateListTreadmill(ws.getListTreadmill().get(a));
				else uwDao.insertMedTreadmill(ws.getListTreadmill().get(a));
			}
			
			// input medis lain
			for(int a=0;a<ws.getListMedLain().size();a++) {
				if(ws.getListMedLain().get(a).getTglMcuMedisLain().equals("") && ws.getListMedLain().get(a).getTmpMcuMedisLain().equals("")) continue;
				ws.getListMedLain().get(a).setReg_spaj(ws.getReg_spaj());
				ws.getListMedLain().get(a).setInsured_no(ws.getInsured_no());
				if(uwDao.cekListMedLain(ws.getReg_spaj(),ws.getInsured_no(),ws.getListMedLain().get(a).getUrutanMedisLain()) > 0) uwDao.updateListMedLain(ws.getListMedLain().get(a));
				else uwDao.insertMedLain(ws.getListMedLain().get(a));
			}
		}
		
		//input uw decision
		uwDao.deleteWorkDec(ws.getReg_spaj(),ws.getInsured_no());
		uwDao.deleteWorkDecRider(ws.getReg_spaj(),ws.getInsured_no());
		for(int a=0;a<ws.getListUwDec().size();a++) {
			if(uwDao.cekMstWorkDec(ws.getListUwDec().get(a).getReg_spaj(),ws.getListUwDec().get(a).getInsured_no(),
								   ws.getListUwDec().get(a).getMwd_urut()) > 0) uwDao.updateMstWorkDec(ws.getListUwDec().get(a));
			else uwDao.insertMstWorkDec(ws.getListUwDec().get(a));
			
			for(int b=0;b< ws.getListUwDec().get(a).getRider().size();b++) {
				ws.getListUwDec().get(a).getRider().get(b).setInsured_no(ws.getListUwDec().get(a).getInsured_no());
				ws.getListUwDec().get(a).getRider().get(b).setMwd_urut(ws.getListUwDec().get(a).getMwd_urut());
				ws.getListUwDec().get(a).getRider().get(b).setMwdr_urut(b+1);
				ws.getListUwDec().get(a).getRider().get(b).setUrutan_penyakit(ws.getListUwDec().get(a).getUrutan_penyakit());
				ws.getListUwDec().get(a).getRider().get(b).setUrutan_decision(ws.getListUwDec().get(a).getUrutan_decision());
				/*if(uwDao.cekMstWorkDecRider(ws.getListUwDec().get(a).getRider().get(b).getReg_spaj(),
											ws.getListUwDec().get(a).getRider().get(b).getInsured_no(),
											ws.getListUwDec().get(a).getRider().get(b).getMwd_urut(),
											ws.getListUwDec().get(a).getRider().get(b).getMwdr_urut()) > 0) uwDao.updateMstWorkDecRider(ws.getListUwDec().get(a).getRider().get(b));
				else*/ uwDao.insertMstWorkDecRider(ws.getListUwDec().get(a).getRider().get(b));
			}
		}
		
		//input financial statement
		if(uwDao.cekMstFinancialStat(ws.getReg_spaj(),ws.getInsured_no()) > 0) uwDao.updateMstFinancialStat(ws);
		else uwDao.insertMstFinancialStat(ws);
		
		return "UW Worksheet berhasil disubmit";
	}

	public UwWorkSheet getUwWorksheet(String reg_spaj, Integer insured_no) {
		return uwDao.getUwWorksheet(reg_spaj,insured_no);
	}	
	
	public String prosesPostPonedMallAss(String spaj, User user){
		String pesan = "";
		bacDao.savelssaid(spaj, 9);
		uwDao.insertMstPositionSpaj(user.getLus_id(), "POSTPONED 40 HARI KARENA HAMIL > 7 BLN", spaj, 0);
		pesan = "Polis ditunda 40 hari/ Postponed dikarenakan sedang hamil >7 bulan";
		return pesan;
	}
	
	public String prosesTransferBacToUw(String spaj,String lusId,Integer lsbsId,String namaPemegang,Integer guthrie,BindException err,HttpServletRequest request){
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		String result = "";
		String bisnisId;

		bisnisId=f3.format(lsbsId);

//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		int isInputanBank = bacDao.selectIsInputanBank(spaj);
		String lsbs_id = selectBusinessId(spaj);
		Integer lsdbs_number = selectBusinessNumber(spaj);
		String cabangUserInput=uwDao.selectCabangForUserInput(spaj);
		String lcaid = uwDao.selectCabangFromSpaj(spaj);
		Product detailProduk = akseptasiDao.selectMstProductInsuredDetail(spaj, Integer.parseInt(lsbs_id), lsdbs_number);
		List<MedQuest> mq = selectMedQuest(spaj,null);
		Integer clear_case = 1;
		if (!(lsbs_id.equals("196")&&lsdbs_number!=2)){
		for(int i=0;i<mq.size();i++){
			if(mq.get(i).getMsadm_clear_case()==0){
				clear_case = 0;
			}
		 }
		}
		Boolean transToPrintPolis = false;
		
		/** MANTA 1/6/2015
		 *  Atas permintaan OJK
		 *  Tidak boleh langsung ke Print Polis, harus melalui proses Underwriting terlebih dahulu.
		 *  Sementara untuk Bancass saja
		 */
		if( (( isInputanBank == 3 || (lcaid.equals("58") && detailProduk.getMspr_tsi()<=new Double(200000000) && clear_case==1 && (lsbsId!=184 && lsdbs_number != 2) && !cabangUserInput.equals("01")) ) && !products.productBsmFlowStandardIndividu(Integer.parseInt(lsbs_id), lsdbs_number))
				|| products.productMallLikeBSM(Integer.parseInt(lsbs_id), lsdbs_number) ){
			transToPrintPolis = true;
		}
		
		if( isInputanBank == 2 || lsbs_id.equals("164") && lsdbs_number == 11 || lsbs_id.equals("142") && currentUser.getCab_bank().equals("") || lsbs_id.equals("120") && currentUser.getLus_bas()==2 || (lsbs_id.equals("175") && lsdbs_number == 2) 
				|| lsbs_id.equals("215")|| lsbs_id.equals("216")){
			transToPrintPolis = false;
		}
		
		/** Yusuf - 14/12/2007 -
		 * 20 feb 09 - bukan hanya inputan bank, melainkan semua powersave dan stable link 
		 * 1. tidak ada titipan premi yang perlu dijurnal
		 * 2. generate nomor polis langsung
		 * 3. transfer langsung ke print polis 
		 * 	(flownya berubah dari BAC - U/W - PAYMENT - PRINT POLIS - FILLING/KOMISI 
		 *   menjadi BAC - PRINT POLIS - U/W - PAYMENT - FILLING/KOMISI) */
		// PROJECT: POWERSAVE & STABLE LINK 
		if(transToPrintPolis==true) {
		//if(products.powerSave(lsbs_id) || products.stableLink(lsbs_id)){
			
			//update posisi	
			uwDao.updateMstInsured(spaj, 6, 17, 1, null);
			uwDao.insertMstPositionSpaj(lusId, "TRANSFER DARI BAC KE PRINT POLIS", spaj, 0);		
			uwDao.updateMstPolicy(spaj, 6);
			
			uwDao.updateMstInsuredTgl(spaj, 1, commonDao.selectSysdate(), 0);
			uwDao.saveMstTransHistory(spaj, "tgl_terima_spaj_uw", commonDao.selectSysdate(), null, null);
			
			//generate nomor polis
			Akseptasi akseptasi = new Akseptasi();
			akseptasi.setSpaj(spaj);
			akseptasi.setLcaId(uwDao.selectCabangFromSpaj(spaj));
			akseptasi.setLsbsId(Integer.valueOf(uwDao.selectBusinessId(spaj)));
			akseptasi.setMsagId(uwDao.selectAgenFromSpaj(spaj));
			int setNopol = uwDao.wf_set_nopol(akseptasi, 6);
			//51 = no polis tidak berhasil generate
			//52 = no polis kembar, transfer ulang
			
			if(setNopol>0){//rollback dan tampilkan message
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				result = "Nomor Polis Tidak Berhasil Digenerate. Mohon dicoba kembali atau hubungi IT.";
				err.reject("", result);
			}else {
				if(lcaid.equals("58")){
					result = "Polis berhasil ditransfer ke Print Polis. Nomor Polis : " + FormatString.nomorPolis(akseptasi.getNoPolis());
				}else{
					result = "Polis sudah dapat dicetak. Silahkan masuk ke menu print polis untuk mencetak. Nomor Polis adalah " + FormatString.nomorPolis(akseptasi.getNoPolis());
				}
				err.reject("", result);
			}
			
			// Rahmayanti - Snows
			if(isInputanBank != 2 && isInputanBank != 3 && isInputanBank != 16 ){
				uwDao.prosesSnows(spaj, lusId, 211, 212);
			}
			
			
		/** Yusuf - 14/12/2007 - Apabila selain inputan bank -> seperti biasa saja */
		}else {
			
			boolean flagPre=true;
			boolean health=true;
			
			if(isInputanBank == 2 || (lsbs_id.equals("164") && lsdbs_number == 11) || (lsbs_id.equals("142") && currentUser.getCab_bank().equals("")) || (lsbs_id.equals("120") && currentUser.getLus_bas()==2) || lcaid.equals("40") || lcaid.equals("73") ||
				(lsbs_id.equals("175") && lsdbs_number == 2)){
				if(lsbs_id.equals("120") || lsbs_id.equals("183") || (lsbs_id.equals("134") && (lsdbs_number==5 || lsdbs_number==10 || lsdbs_number==11)) 
					 || (lsbs_id.equals("142") && lsdbs_number.equals(13)) //helpdesk [133346] produk baru 142-13 Smart Investment Protection
					 || (lsbs_id.equals("189") && (lsdbs_number>=48 && lsdbs_number<=62)) //helpdesk [133975] produk baru 189 48-62 Smile Medical Syariah BSIM
					 || lsbs_id.equals("208") || lsbs_id.equals("213") || lsbs_id.equals("215") 
				     || lsbs_id.equals("216") || (lsbs_id.equals("217") && lsdbs_number== 2) //ERBE ke Prose BAS QA - Proses Checking dulu 
				     || lsbs_id.equals("219") || lsbs_id.equals("220") || lsbs_id.equals("223") || lsbs_id.equals("224")||
						(lsbs_id.equals("226") && (lsdbs_number >= 1 && lsdbs_number <= 5))){ //helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
					uwDao.updateMstInsured(spaj, 218, 17, 1, null);
					uwDao.insertMstPositionSpaj(lusId, "TRANSFER KE PROSES CHECKING", spaj, 0);
					uwDao.saveMstTransHistory(spaj, "TGL_TRANSFER_PROSES_CHECKING", null, null, null);	
					//Transfer ke speedy
					uwDao.updateMstPolicy(spaj,218);
					// Rahmayanti - Snows
					uwDao.prosesSnows(spaj, lusId, 218, 212);
					flagPre = false;
				}else{
					//uwDao.updateMstInsured(spaj,27);//ke speedy, canpri
					uwDao.updateMstInsured(spaj, 27, 17, 1, null);
					uwDao.insertMstPositionSpaj(lusId, "TRANSFER KE SPEEDY", spaj, 0);
					uwDao.saveMstTransHistory(spaj, "tgl_transfer_uw_speedy", null, null, null);	
					//Transfer ke speedy
					uwDao.updateMstPolicy(spaj,27);
					// Rahmayanti - Snows
					uwDao.prosesSnows(spaj, lusId, 202, 212);
				}
			}else{
				//uwDao.updateMstInsured(spaj,27);//ke speedy, canpri
				uwDao.updateMstInsured(spaj, 218, 17, 1, null);
				uwDao.insertMstPositionSpaj(lusId, "TRANSFER KE PROSES CHECKING", spaj, 0);
				uwDao.saveMstTransHistory(spaj, "TGL_TRANSFER_PROSES_CHECKING", null, null, null);	
				//Transfer ke speedy
				uwDao.updateMstPolicy(spaj,218);
				// Rahmayanti - Snows
				uwDao.prosesSnows(spaj, lusId, 218, 212);
				flagPre = false;
			}
			
			//update guthrie
			if(guthrie==1)
				uwDao.updateMstInsuredTgl(spaj, 1, commonDao.selectSysdate(), 0);
			//
//			String bisnisId;
//			bisnisId=f3.format(lsbsId);
			//Jika Bukan product endowment tidak harus jurnal 14-06-06
//			if(lsbsId.intValue()==157){
//				result = "Proses Transfer Product Endowment Berhasil.";
//				err.reject("",result);
//				flagPre=false;
//			}else{
//				if(!lcaid.equals("58")){
//					if(products.unitLink(bisnisId)) {//produk unitlink, termasuk stable link
//						//kalo stable link, tetep dijurnal secara unit link
//						if(! uwDao.getJurnalBacUlink(spaj,lusId,lsbsId,namaPemegang,err,request)){
//							flagPre=false;
//							err.reject("","Gagal Jurnal Ulink.");
//							health=false;
//							TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//						}

//						err.reject("","Jurnal Ulink.");
//					}else{//bukan unit link
//						if(! uwDao.getJurnalBacIndividu(spaj,lusId,lsbsId,namaPemegang,err,request)){
//							flagPre=false;
//							result = "Gagal Jurnal Individu.";
//							health=false;
//							err.reject("", result);
//							TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//						}

//						result = "Jurnal Individu";
//						err.reject("", result);
//					}
//				}
//			}	
			
			//Menampilkan no pre yang baru di buat
			List lsDp=uwDao.selectMstDepositPremium(spaj,null);
			String nomorPre[]=new String[lsDp.size()+1];
			String pre="";
			for(int i=0;i<lsDp.size();i++){
				DepositPremium depPre=(DepositPremium)lsDp.get(i);
				nomorPre[i]=depPre.getMsdp_no_pre();
				if(lsDp.size()>0)
					pre=nomorPre[i]+", "+pre;
				else
					pre=nomorPre[i];
			}
			if(flagPre) {
//				if(!lcaid.equals("58")){
//					result = "No Pre ="+pre;
//				}else{
					result = "Proses Transfer ke New Business berhasil dilakukan.";
//				}

				err.reject("", result);
			}else{
				result = "Transfer ke Proses Checking berhasil dilakukan.";
				err.reject("", result);
			}
			
			if(health){
				Tertanggung ttg=bacDao.selectttg(spaj);
				if(ttg!=null){
					
					Integer spasi=ttg.getMcl_first().indexOf(' ');
					Integer titik=ttg.getMcl_first().indexOf('.');
					Integer koma=ttg.getMcl_first().indexOf(',');
					Integer pjg=ttg.getMcl_first().length();
					String nama="";
					nama=ttg.getMcl_first();
					if(spasi>0)
						nama=ttg.getMcl_first().substring(0,spasi);
					else if(titik>0)
						nama=ttg.getMcl_first().substring(0,titik);
					else if(koma>0)
						nama=ttg.getMcl_first().substring(0,koma);
					List prodHealth= selectCekHealthProduct(nama, ttg.getMspe_date_birth());
					if(!prodHealth.isEmpty()){
						String konfirmHealthProd="Nasabah telah memiliki polis kesehatan, cek riwayat klaim kesehatan";
						err.reject("",konfirmHealthProd);
					}
				}
			}
		}
		Map mapRegion=uwDao.selectRegion(spaj);
		String lca_id = (String)mapRegion.get("LCA_ID");
		String lwk_id = (String)mapRegion.get("LWK_ID");
		String lsrg_id = (String)mapRegion.get("LSRG_ID");
		if(lca_id.equals("09")){
			prosesEndorsemen(spaj, Integer.parseInt(lsbs_id),0);
		}
		String dir = props.getProperty("pdf.dir.export") + "/" + lca_id;
		File destDir = new File(dir + "/" + spaj);
		if(!destDir.exists()){
			updateUploadScan(spaj, 0);
		}else{
			updateUploadScan(spaj, 1);
		}
		// SIMPOL KIRIM EMAIL KE UW DARI BAC - RYAN
		if(lca_id.equals("09")&& lsbs_id.equals("120") && currentUser.getLus_bas()==2){
		try {
			
		    email.send(false, props.getProperty("admin.ajsjava"),new String[]{currentUser.getEmail()}, null, new String[]{"ryan@sinarmasmsiglife.co.id"}, //props.getProperty("uw.dmtm").split(";")
					"[E-Lions] SMiLe Info", 
					"\nINFO UNTUK UNDERWRITING: SPAJ SIMPOL dengan nomor "+FormatString.nomorSPAJ(spaj)+" telah ditransfer ke posisi Underwriting, Silakan diproses selanjutnya." +
					"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		
		} catch (MessagingException e) {
			logger.error("ERROR :", e);
		}
      }
		
//		Policy policy = uwDao.selectMstPolicyAll(spaj);
//		String no_blanko = policy.getMspo_no_blanko();	
//		Map mapCekBlanko = basDao.selectMstSpajAoFurtherOrNonFurther(no_blanko,1);
//		if(mapCekBlanko!=null){
//			basDao.updateMonitoringSpaj(no_blanko, 1, "update", null, null, null, null,null,null,null); 
//		}
		
//	    Integer count_spaj_gadget = bacDao.selectCountGadgetSpaj(spaj);
//		if(count_spaj_gadget>0){
//		String me_id = sequence.sequenceMeIdEmail();
//		 	try{
//			    EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
//						null, 0, 0, new Date(), null, 
//						true, "info@sinarmasmsiglife.co.id",												
//						new String[]{"eriza@sinarmasmsiglife.co.id"}, 
//						null, 
//						new String[]{"ryan@sinarmasmsiglife.co.id"}, 
//						"[E-Lions] Notifikasi E-SPAJ ",
//						"\nINFO UNTUK NEW BUSSINESS :  SPAJ dengan nomor "+ " "+ spaj+"   berhasil ditranfer ke Speedy, mohon proses lebih lanjut",
//						null,10);	
//		 	}catch (Exception e) {
//		 		EmailPool.send("E-Lions", 1, 0, 0, 0, null, 0, 0, new Date(), null, true, 
//						props.getProperty("admin.ajsjava"), 
//						new String[] { "ryan@sinarmasmsiglife.co.id" }, 
//						null, 
//						null, 
//						"Error Kirim Email Notifikasi E-SPAJ ke NB", 
//						null, 
//						null, 
//						null);
//			}
//		}
		
		return result;
	}
	
	/*public void generateUpload(String spaj){
		List<HashMap> reg_spaj = new ArrayList<HashMap>();
		reg_spaj =uwDao.selectSpaj();
		String daftarSPaj="";
		for(int i=0;i<reg_spaj.size();i++){
			Map daftar = reg_spaj.get(i);
			daftarSPaj= (String)daftar.get("REG_SPAJ");
			String lca_id = uwDao.selectCabangFromSpaj(daftarSPaj);
			String dir = props.getProperty("pdf.dir.export") + "//" + lca_id;
			File destDir = new File(dir + "//" + spaj);
			if(!destDir.exists()){
				updateUploadScan(daftarSPaj, 0);
			}else{
				updateUploadScan(daftarSPaj, 1);
			}
			
		}
	}*/

	public UwWorkSheet getWoksheetNonMed(String reg_spaj, Integer insured_no) {
		return uwDao.getWoksheetNonMed(reg_spaj,insured_no);
	}
	
	public List<UwQuestionnaire> getListQuestionnaire(String reg_spaj, Integer insured_no) {
		return uwDao.getListQuestionnaire(reg_spaj, insured_no);
	}
	
	public List<HashMap> getListUwDec(String reg_spaj, Integer insured_no) {
		return uwDao.getListUwDec(reg_spaj, insured_no);
	}
	
	public List<UwDecisionRider> getListWorkDecRider(String reg_spaj, Integer insured_no, Integer mwd_urut) {
		return uwDao.getListWorkDecRider(reg_spaj,insured_no,mwd_urut);
	}
	
	public UwDecisionRider getListWorkDecRiderById(String reg_spaj, Integer insured_no, Integer mwd_urut, Integer lsbs_id, Integer lsdbs_number) {
		return uwDao.getListWorkDecRiderById(reg_spaj,insured_no,mwd_urut,lsbs_id,lsdbs_number);
	}
	
	public UwWorkSheet getFinancialStat(String reg_spaj, Integer insured_no) {
		return uwDao.getFinancialStat(reg_spaj,insured_no);
	}
	
	public List<UwLpk> getListLpk(String reg_spaj, Integer insured_no) {
		return uwDao.getListLpk(reg_spaj,insured_no);
	}
	
	public List<HashMap> getListRiwPenyakit(String reg_spaj, Integer insured_no, Integer urutanLpk, Integer rp_type) {
		return uwDao.getListRiwPenyakit(reg_spaj,insured_no,urutanLpk,rp_type);
	}
	
	public List<UwUrin> getListUrin(String reg_spaj, Integer insured_no) { 
		return uwDao.getListUrin(reg_spaj,insured_no);
	}
	
	public List<UwAda> getListAda(String reg_spaj, Integer insured_no) { 
		return uwDao.getListAda(reg_spaj,insured_no);
	}
	
	public List<UwHiv> getListHiv(String reg_spaj, Integer insured_no) {
		return uwDao.getListHiv(reg_spaj,insured_no);
	}
	
	public List<UwTumor> getListTumor(String reg_spaj, Integer insured_no) {
		return uwDao.getListTumor(reg_spaj,insured_no);
	}
	
	public List<UwAbdomen> getListAbdomen(String reg_spaj, Integer insured_no) {
		return uwDao.getListAbdomen(reg_spaj,insured_no);
	}
	
	public List<UwDadaPa> getListDadaPa(String reg_spaj, Integer insured_no) {
		return uwDao.getListDadaPa(reg_spaj,insured_no);
	}
	
	public List<UwEkg> getListEkg(String reg_spaj, Integer insured_no) {
		return uwDao.getListEkg(reg_spaj,insured_no);
	}
	
	public List<UwTreadmill> getListTreadmill(String reg_spaj, Integer insured_no) {
		return uwDao.getListTreadmill(reg_spaj,insured_no);
	}
	
	public List<UwMedisLain> getListMedLain(String reg_spaj, Integer insured_no) {
		return uwDao.getListMedLain(reg_spaj,insured_no);
	}
	
	public String selectExpiredDate(String inputDate, Integer dayAdd) {
		return uwDao.selectExpiredDate(inputDate,dayAdd);
	}
	
	public List selectAkumulasiPolisBySpajList(List filters) {
		List result = new ArrayList();
		for(int i=0; i<filters.size(); i++) {
			List<SortedMap> daftar = this.uwDao.selectAkumulasiPolisBySpaj((String) filters.get(i));
			//spaj=M, tahunKe=N, lsbs=O, lku=P, lscb=Q, lamaBayar=R, lamaTanggung=S, umurTertanggung=T, lsdbs=U
			
			for(SortedMap map : daftar) {
				String spaj=(String)map.get("M");
				Integer tahunKe=(Integer)map.get("N");
				Integer lsbsId=(Integer)map.get("O");
				String lkuId=(String)map.get("P");
				Integer lscbId=(Integer)map.get("Q");
				Integer lamaBayar=(Integer)map.get("R");
				Integer lamaTanggung=(Integer)map.get("S");
				Integer umurTt=(Integer)map.get("T");
				Integer lsdbsNumber=(Integer)map.get("U");
				Double tsi=(Double)map.get("E");
				Double premium=(Double)map.get("G");
				Integer liSar;
				Double sar;
				
				if(tahunKe==1)
					liSar=1;
				else
					liSar=2;
				
				Double rateSar=uwDao.selectGetSar(liSar, lsbsId, lsdbsNumber, lkuId, lscbId, lamaBayar, lamaTanggung, tahunKe, umurTt);

				if (rateSar==null)
					rateSar= 0.0;
				
				sar= tsi* rateSar/ 1000; 
				map.put("F", sar);
				result.add(map);
			}
			
		}
		return result;
	}
	
	public Product selectMstProductInsuredDetail(String spaj, Integer lsbs_id, Integer lsdbs_number){
		return akseptasiDao.selectMstProductInsuredDetail(spaj, lsbs_id, lsdbs_number);
	}
	
	public List selectAkumulasiPolisBySpajListNew(List<Map> filters, Integer bulanKe, Boolean flagLifeOnly, String now, String tglLahir) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		List result = new ArrayList();
		List<SortedMap> beingProcess = new ArrayList<SortedMap>();
		BigDecimal retensiLife = new BigDecimal(0);
		BigDecimal retensiLifeV2 = new BigDecimal(0);
		BigDecimal retensiPa = new BigDecimal(0);
		BigDecimal retensiPaV2 = new BigDecimal(0);
		BigDecimal retensiTpd = new BigDecimal(0);
		BigDecimal retensiTpdV2 = new BigDecimal(0);
		BigDecimal kursUs = new BigDecimal(8000);
		BigDecimal kursUsSar = new BigDecimal(9000);
		// BigDecimal kursSing = new BigDecimal(5000);
		BigDecimal totRetLife = new BigDecimal(0);
		BigDecimal totRetLifeV2 = new BigDecimal(0);
		BigDecimal totRetPa = new BigDecimal(0);
		BigDecimal totRetPaV2 = new BigDecimal(0);
		BigDecimal totRetTpd = new BigDecimal(0);
		BigDecimal totRetTpdV2 = new BigDecimal(0);
		BigDecimal totRetOth = new BigDecimal(0);
		BigDecimal totRetOthV2 = new BigDecimal(0);
		BigDecimal totRetNonCat = new BigDecimal(0);
		BigDecimal totReasLife = new BigDecimal(0);
		BigDecimal totReasLifeV2 = new BigDecimal(0);
		BigDecimal totReasPa = new BigDecimal(0);
		BigDecimal totReasPaV2 = new BigDecimal(0);
		BigDecimal totReasTpd = new BigDecimal(0);
		BigDecimal totReasTpdV2 = new BigDecimal(0);
		BigDecimal totSar = new BigDecimal(0);
		BigDecimal totSarAll = new BigDecimal(0);
		BigDecimal EM = new BigDecimal(0);
 
		Boolean flagIndMri = true;
		Integer flagKurs = 0; // 1: $ 0: Rp
		Integer flagKursV2 = 0; // 1: $ 0: Rp
		
		for(int i=0; i<filters.size(); i++) {
			List<SortedMap> daftar = this.uwDao.selectAkumulasiPolisBySpaj((String) filters.get(i).get("REG_SPAJ"));// yang mesti dicek
			//spaj=M, tahunKe=N, lsbs=O, lku=P, lscb=Q, lamaBayar=R, lamaTanggung=S, umurTertanggung=T, lsdbs=U
			Integer cL = 0;
			Integer cP = 0;
			Integer cT = 0;
			Integer cO = 0;
			Integer umur = 0;
			List<SortedMap> temp = new ArrayList<SortedMap>();
			temp = sortingProduct(daftar,flagLifeOnly);
			
			Map pivot = new HashMap();
			    pivot = setReasPosition(daftar);
			for(SortedMap map : temp) {
				String spaj=(String)map.get("M");
				//logger.info("spaj : "+spaj);
				Integer tahunKe=(Integer)map.get("N");
				Integer lsbsId=(Integer)map.get("O");
				//logger.info(lsbsId);
				String lkuId=(String)map.get("P");
				Integer lscbId=(Integer)map.get("Q");
				Integer lamaBayar=(Integer)map.get("R");
				Integer lamaTanggung=(Integer)map.get("S");
				Integer umurTt=(Integer)map.get("T");
				Integer umurPP=(Integer)map.get("PP_AGE");
				Integer lsdbsNumber=(Integer)map.get("U");
				Double tsi=(Double)map.get("E");
				Double premium=(Double)map.get("G");
				Integer liSar;
				Double sar = 0.0;
				Datausulan datausulan = bacDao.selectDataUsulanutama(spaj);
			    Integer factor =1;
				/* SAR */
				if(tahunKe==1) liSar=1;
				else liSar=2;
				
				if(datausulan!=null){
				    switch (datausulan.getLscb_id()) {
					case 1:
						factor = 4;
						break;
					case 2:
						factor = 2;
						break;
					case 3:
						factor = 1;
						break;
					case 6:
						factor = 12;
						break;
					}
				    if(products.unitLinkNew(datausulan.getLsbs_id().toString())){
						ArrayList daftarPremi = Common.serializableList(uwDao.selectDaftarPremiPertama(spaj));					
						for(int x=0;x<daftarPremi.size();x++){
							HashMap mapPremi =(HashMap)daftarPremi.get(x);
							BigDecimal PremiSementara =(BigDecimal)mapPremi.get("MU_JLH_PREMI");
							String tipeTopUp =(String)mapPremi.get("LT_TRANSKSI");
							if(!tipeTopUp.toUpperCase().equals("TOP-UP TUNGGAL"))premium=premium+PremiSementara.doubleValue();
							
						}
					}else{
						premium = datausulan.getMspr_premium();
					}
				}
				if(lsbsId == 828 && (lsdbsNumber==2 || lsdbsNumber==3))umur=umurPP;
				else umur=umurTt;
				Double rateSar = 0.0;
				// Request dari Ariani per 23/08/2010
				//if(!map.get("C").toString().equals(("..."))) {
					rateSar=uwDao.selectGetSarNew(liSar, lsbsId, lsdbsNumber, lkuId, lscbId, lamaBayar, lamaTanggung, tahunKe, 
							umur, (Integer)map.get("lstb_id"), map.get("C").toString().replace(".", ""), 
							bulanKe, map.get("J").toString(),now);					
				//}
				if((Integer)map.get("lstb_id") == 1) {
					if (rateSar==null)
						rateSar= 0.0;
					if(lsbsId==213){//req mba tities, magna link SAR=UP
						rateSar= 1000.0;
					}
					//Request Hanifah
					if(lsbsId==173){
						Double tr=premium*(lamaBayar-1);
						sar=tsi+tr;
					}else if(lsbsId==813){
						sar=tsi;
					}else if(lsbsId == 837){
						sar =tsi*1.3;
					}else if(lsbsId==814 || lsbsId==815 || lsbsId==827 || lsbsId==828){
						sar = ((premium*factor)*rateSar)/1000;
				    }else if(lsbsId==835){
				    	sar = rateSar*tsi/1000;
				    }else{
						sar= tsi* rateSar/ 1000;
					}
				}
				else if((Integer)map.get("lstb_id") == 2 || (Integer)map.get("lstb_id") == 11) {
					sar = rateSar;
				}
				map.put("F", sar);
				if(flagLifeOnly) {
					if(map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
						beingProcess.add(map);
					}
					
					if(lkuId.equals("01")) {
						if(flagKurs == 1) totSar = totSar.multiply(kursUsSar).add(new BigDecimal(sar));//totSar.add(new BigDecimal(sar).multiply(kursUsSar));
						else totSar = totSar.add(new BigDecimal(sar));
					}
					else if(lkuId.equals("02")) {
						if(sar == 0.0) totSar = new BigDecimal(0);
						else if(flagKurs == 0 && totSar.compareTo(new BigDecimal(0)) == 1) totSar = totSar.divide(kursUsSar,2,BigDecimal.ROUND_HALF_UP).add(new BigDecimal(sar));//totSar.add(new BigDecimal(sar).divide(kursUsSar,2,BigDecimal.ROUND_HALF_UP));
						else totSar = totSar.add(new BigDecimal(sar));
					}
				}
				else {
					if(map.get("L").toString().equals("INFORCE") || map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
						if(map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
							beingProcess.add(map);
						}
						
						if(lkuId.equals("01")) {
							if(flagKurs == 1) totSarAll = totSarAll.multiply(kursUsSar).add(new BigDecimal(sar));//totSarAll.add(new BigDecimal(sar).multiply(kursUsSar));
							else totSarAll = totSarAll.add(new BigDecimal(sar));
						}
						else if(lkuId.equals("02")) {
							if(sar == 0.0) totSar = new BigDecimal(0);
							else if(flagKurs == 0 && totSarAll.compareTo(new BigDecimal(0)) == 1) totSarAll = totSarAll.divide(kursUsSar,2,BigDecimal.ROUND_HALF_UP).add(new BigDecimal(sar));//totSarAll.add(new BigDecimal(sar).divide(kursUsSar,2,BigDecimal.ROUND_HALF_UP));
							else totSarAll = totSarAll.add(new BigDecimal(sar));
						}						
					}
				}
				
				/* Extra Premi */
				Map data = new HashMap();
				if(((Integer)map.get("lstb_id") == 1 && lsbsId > 300) || ((Integer)map.get("lstb_id") == 2 && lsbsId >= 900)) {
					data = uwDao.getExtraPremi(spaj,map.get("V").toString());
					map.put("I", data.get("TOTAL7"));
					map.put("B1", data.get("MSPR_EXTRA"));
					if((Integer)map.get("lstb_id") == 2 && lsbsId == 900) EM = new BigDecimal(data.get("TOTAL7").toString());
				}
				else {
					map.put("I", new Double(0));
					map.put("B1", new Integer(0));					
				}
				
				/* Keterangan */
				if(((Integer)map.get("lstb_id") == 1 && lsbsId < 300) || ((Integer)map.get("lstb_id") == 2 && lsbsId < 900)) {
					if(!map.get("L").toString().equals("POLICY IS BEING PROCESSED") && !map.get("L").toString().equals("TEMPORARY CANCELED")) {
						String uw_info = this.uwDao.getKetMedis(spaj);
						String ket = "";
						if(uw_info != null) {
							if((Integer)map.get("mste_medical") == 0) ket = "Non Medis";
							else if((Integer)map.get("mste_medical") == 1) ket = "Medis";		
							if((Integer)map.get("mste_standard") == 0 && (!uw_info.contains("BORDERLINE") && !uw_info.contains("BOD STD"))) ket = ket+", Std";
							else if((Integer)map.get("mste_standard") == 1 || (uw_info.toString().contains("BORDERLINE") || uw_info.contains("BOD STD"))) ket = ket+", Non Std";
							else if((Integer)map.get("mste_standard") == 2) ket = ket+", Bod Std";							
						}
						map.put("D1", ket);
					}
				}
				
				/* Retensi & TSI Reas */
				if((Integer)map.get("lstb_id") == 1) {
					//  kel life
					if(lsbsId < 300 || lsbsId == 801 || lsbsId == 802 || 
					   lsbsId == 803 || lsbsId == 813 || lsbsId == 816 || 
					   lsbsId == 817 || lsbsId == 818) {
						if(lsbsId != 73 && lsbsId != 142 && lsbsId != 143 && lsbsId != 144 && lsbsId != 155 && lsbsId != 158 && lsbsId != 175) {
							cL++;
							if(map.get("L").toString().equals("INFORCE") || map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
								if(lkuId.equals("01")) {
									if(flagKurs == 1 && retensiLife.compareTo(new BigDecimal(0)) == 1) {
										retensiLife = retensiLife.multiply(kursUs);
									}
									// isi retensi						
									retensiLife = hitungRetensi(new BigDecimal(tsi), retensiLife, new BigDecimal(75E7), map);
									//flagKurs = 0;
								}
								else if(lkuId.equals("02")) {
									if(flagKurs == 0 && retensiLife.compareTo(new BigDecimal(0)) == 1) {
										retensiLife = retensiLife.divide(kursUs,2,BigDecimal.ROUND_HALF_UP);
									}	
									// isi retensi						
									retensiLife = hitungRetensi(new BigDecimal(tsi), retensiLife, new BigDecimal(75E3), map);
									//flagKurs = 1;
								}							
							}
							else {
								if(lkuId.equals("01")) {
									if(flagKursV2 == 1 && retensiLifeV2.compareTo(new BigDecimal(0)) == 1) {
										retensiLifeV2 = retensiLifeV2.multiply(kursUs);
									}
									// isi retensi						
									retensiLifeV2 = hitungRetensi(new BigDecimal(tsi), retensiLifeV2, new BigDecimal(75E7), map);
								}
								else if(lkuId.equals("02")) {
									if(flagKursV2 == 0 && retensiLifeV2.compareTo(new BigDecimal(0)) == 1) {
										retensiLifeV2 = retensiLifeV2.divide(kursUs,2,BigDecimal.ROUND_HALF_UP);
									}	
									// isi retensi						
									retensiLifeV2 = hitungRetensi(new BigDecimal(tsi), retensiLifeV2, new BigDecimal(75E3), map);
								}							
							}
							// yg invorce doank yg di total
							if(map.get("L").toString().equals("INFORCE") || map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
								// buat total retensi bisa berubah2 sesuai dengan kurs nya
								totRetLife = totalRetensi(totRetLife, map, lkuId, flagKurs, kursUs);
								//flagKurs = changeKurs(lkuId);
								// buat total reas bisa berubah2 sesuai dengan kurs nya 
								//if(map.get("A1") != null && new BigDecimal(map.get("A1").toString()).compareTo(new BigDecimal(0)) == 1) {
									totReasLife = totalReas(totReasLife, map, lkuId, flagKurs, kursUs);
									flagKurs = changeKurs(lkuId);
								//}
							}
							else {
								// buat total retensi bisa berubah2 sesuai dengan kurs nya
								totRetLifeV2 = totalRetensi(totRetLifeV2, map, lkuId, flagKursV2, kursUs);
								//flagKurs = changeKurs(lkuId);
								// buat total reas bisa berubah2 sesuai dengan kurs nya 
								//if(map.get("A1") != null && new BigDecimal(map.get("A1").toString()).compareTo(new BigDecimal(0)) == 1) {
									totReasLifeV2 = totalReas(totReasLifeV2, map, lkuId, flagKursV2, kursUs);
									flagKursV2 = changeKurs(lkuId);
								//}							
							}							
						}
						else map.put("W", new BigDecimal(0));
					}
					// kel PA
					else if(lsbsId == 800 || lsbsId == 810 || lsbsId == 142 || 
							lsbsId == 143 || lsbsId == 144 || lsbsId == 155 || 
							lsbsId == 158 || lsbsId == 73  || lsbsId == 175 || lsbsId  == 198) {
						cP++;
						if(map.get("L").toString().equals("INFORCE") || map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
							if(lkuId.equals("01")) {
								if(flagKurs == 1 && retensiPa.compareTo(new BigDecimal(0)) == 1) {
									retensiPa = retensiPa.multiply(kursUs);
								}
								// isi retensi						
								retensiPa = hitungRetensi(new BigDecimal(tsi), retensiPa, new BigDecimal(75E7), map);
							}	
							else if(lkuId.equals("02") ) {
								if(flagKurs == 0 && retensiPa.compareTo(new BigDecimal(0)) == 1) {
									retensiPa = retensiPa.divide(kursUs,2,BigDecimal.ROUND_HALF_UP);
								}	
								// isi retensi						
								retensiPa = hitungRetensi(new BigDecimal(tsi), retensiPa, new BigDecimal(75E3), map);
							}							
						}
						else {
							if(lkuId.equals("01")) {
								if(flagKursV2 == 1 && retensiPaV2.compareTo(new BigDecimal(0)) == 1) {
									retensiPaV2 = retensiPaV2.multiply(kursUs);
								}
								// isi retensi						
								retensiPaV2 = hitungRetensi(new BigDecimal(tsi), retensiPaV2, new BigDecimal(75E7), map);
							}	
							else if(lkuId.equals("02") ) {
								if(flagKursV2 == 0 && retensiPaV2.compareTo(new BigDecimal(0)) == 1) {
									retensiPaV2 = retensiPaV2.divide(kursUs,2,BigDecimal.ROUND_HALF_UP);
								}	
								// isi retensi						
								retensiPaV2 = hitungRetensi(new BigDecimal(tsi), retensiPaV2, new BigDecimal(75E3), map);
							}							
						}
	
						// yg invorce doank yg di total
						if(map.get("L").toString().equals("INFORCE") || map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
							// buat total retensi bisa berubah2 sesuai dengan kurs nya
							totRetPa = totalRetensi(totRetPa, map, lkuId, flagKurs, kursUs);
							//flagKurs = changeKurs(lkuId);
							// buat total reas bisa berubah2 sesuai dengan kurs nya 
							//if(map.get("A1") != null && new BigDecimal(map.get("A1").toString()).compareTo(new BigDecimal(0)) == 1) {
								totReasPa = totalReas(totReasPa, map, lkuId, flagKurs, kursUs);
								flagKurs = changeKurs(lkuId);
							//}
						}
						else {
							// buat total retensi bisa berubah2 sesuai dengan kurs nya
							totRetPaV2 = totalRetensi(totRetPaV2, map, lkuId, flagKursV2, kursUs);
							//flagKurs = changeKurs(lkuId);
							// buat total reas bisa berubah2 sesuai dengan kurs nya 
							//if(map.get("A1") != null && new BigDecimal(map.get("A1").toString()).compareTo(new BigDecimal(0)) == 1) {
								totReasPaV2 = totalReas(totReasPaV2, map, lkuId, flagKursV2, kursUs);
								flagKursV2 = changeKurs(lkuId);
							//}							
						}
					}				
					// kel TPD
					else if(lsbsId == 804 || lsbsId == 807 || lsbsId == 808 || 
					   lsbsId == 812 || lsbsId == 814 || lsbsId == 815) {
						cT++;
						if(map.get("L").toString().equals("INFORCE") || map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
							if(lkuId.equals("01")) {
								if(flagKurs == 1 && retensiTpd.compareTo(new BigDecimal(0)) == 1) {
									retensiTpd = retensiTpd.multiply(kursUs);
								}
								// isi retensi						
								retensiTpd = hitungRetensi(new BigDecimal(tsi), retensiTpd, new BigDecimal(75E7), map);
							}					
							else if(lkuId.equals("02") ) {
								if(flagKurs == 0 && retensiTpd.compareTo(new BigDecimal(0)) == 1) {
									retensiTpd = retensiTpd.divide(kursUs,2,BigDecimal.ROUND_HALF_UP);
								}	
								// isi retensi
								retensiTpd = hitungRetensi(new BigDecimal(tsi), retensiTpd, new BigDecimal(75E3), map);
							}							
						}
						else {
							if(lkuId.equals("01")) {
								if(flagKursV2 == 1 && retensiTpdV2.compareTo(new BigDecimal(0)) == 1) {
									retensiTpdV2 = retensiTpdV2.multiply(kursUs);
								}
								// isi retensi						
								retensiTpdV2 = hitungRetensi(new BigDecimal(tsi), retensiTpdV2, new BigDecimal(75E7), map);
							}					
							else if(lkuId.equals("02") ) {
								if(flagKursV2 == 0 && retensiTpdV2.compareTo(new BigDecimal(0)) == 1) {
									retensiTpdV2 = retensiTpdV2.divide(kursUs,2,BigDecimal.ROUND_HALF_UP);
								}	
								// isi retensi
								retensiTpdV2 = hitungRetensi(new BigDecimal(tsi), retensiTpdV2, new BigDecimal(75E3), map);
							}							
						}
	
						// yg invorce doank yg di total
						if(map.get("L").toString().equals("INFORCE") || map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
							// buat total retensi bisa berubah2 sesuai dengan kurs nya
							totRetTpd = totalRetensi(totRetTpd, map, lkuId, flagKurs, kursUs);
							//flagKurs = changeKurs(lkuId);
							// buat total reas bisa berubah2 sesuai dengan kurs nya 
							//if(map.get("A1") != null && new BigDecimal(map.get("A1").toString()).compareTo(new BigDecimal(0)) == 1) {
								totReasTpd = totalReas(totReasTpd, map, lkuId, flagKurs, kursUs);
								flagKurs = changeKurs(lkuId);
							//}	
						}
						else {
							// buat total retensi bisa berubah2 sesuai dengan kurs nya
							totRetTpdV2 = totalRetensi(totRetTpdV2, map, lkuId, flagKursV2, kursUs);
							//flagKurs = changeKurs(lkuId);
							// buat total reas bisa berubah2 sesuai dengan kurs nya 
							//if(map.get("A1") != null && new BigDecimal(map.get("A1").toString()).compareTo(new BigDecimal(0)) == 1) {
								totReasTpdV2 = totalReas(totReasTpdV2, map, lkuId, flagKursV2, kursUs);
								flagKursV2 = changeKurs(lkuId);
							//}							
						}
					}
					// kel lain2
					else if(lsbsId == 805 || lsbsId == 806 ||  lsbsId == 809 ||  
					   lsbsId == 811 ||  lsbsId == 821 ||  lsbsId == 819) {
						cO++;
						map.put("W", tsi*0.5);
						map.put("A1", tsi*0.5);
	
						// yg invorce doank yg di total
						if(map.get("L").toString().equals("INFORCE")) {
							// buat total retensi bisa berubah2 sesuai dengan kurs nya
							totRetOth = totalRetensi(totRetOth, map, lkuId, flagKurs, kursUs);
							flagKurs = changeKurs(lkuId);
						}
						else {
							// buat total retensi bisa berubah2 sesuai dengan kurs nya
							totRetOthV2 = totalRetensi(totRetOthV2, map, lkuId, flagKursV2, kursUs);
							flagKursV2 = changeKurs(lkuId);
						}
					}
					else {
						map.put("W", new Double(0));
						map.put("A1", new Double(0));
						totRetNonCat = totRetNonCat.add(new BigDecimal(tsi));
					}					
				}
				else if((Integer)map.get("lstb_id") == 2){
					if(lsbsId < 900) {
						cL++;
						if(map.get("L").toString().equals("INFORCE") || map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
							if(lkuId.equals("01")) {
								if(flagKurs == 1 && retensiLife.compareTo(new BigDecimal(0)) == 1) {
									retensiLife = retensiLife.multiply(kursUs);
								}
								// isi retensi						
								retensiLife = hitungRetensi(new BigDecimal(tsi), retensiLife, new BigDecimal(5E8), map);
							}
							else if(lkuId.equals("02")) {
								if(flagKurs == 0 && retensiLife.compareTo(new BigDecimal(0)) == 1) {
									retensiLife = retensiLife.divide(kursUs,2,BigDecimal.ROUND_HALF_UP);
								}	
								// isi retensi						
								retensiLife = hitungRetensi(new BigDecimal(tsi), retensiLife, new BigDecimal(5E4), map);
							}						
						}
						else {
							if(lkuId.equals("01")) {
								if(flagKursV2 == 1 && retensiLifeV2.compareTo(new BigDecimal(0)) == 1) {
									retensiLifeV2 = retensiLifeV2.multiply(kursUs);
								}
								// isi retensi						
								retensiLifeV2 = hitungRetensi(new BigDecimal(tsi), retensiLifeV2, new BigDecimal(5E8), map);
							}
							else if(lkuId.equals("02")) {
								if(flagKursV2 == 0 && retensiLifeV2.compareTo(new BigDecimal(0)) == 1) {
									retensiLifeV2 = retensiLifeV2.divide(kursUs,2,BigDecimal.ROUND_HALF_UP);
								}	
								// isi retensi						
								retensiLifeV2 = hitungRetensi(new BigDecimal(tsi), retensiLifeV2, new BigDecimal(5E4), map);
							}						
						}
	
						// yg invorce doank yg di total
						if(map.get("L").toString().equals("INFORCE") || map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
							// buat total retensi bisa berubah2 sesuai dengan kurs nya
							totRetLife = totalRetensi(totRetLife, map, lkuId, flagKurs, kursUs);
							//flagKurs = changeKurs(lkuId);
							// buat total reas bisa berubah2 sesuai dengan kurs nya 
							//if(map.get("A1") != null && new BigDecimal(map.get("A1").toString()).compareTo(new BigDecimal(0)) == 1) {
								totReasLife = totalReas(totReasLife, map, lkuId, flagKurs, kursUs);
								flagKurs = changeKurs(lkuId);
							//}
						}
						else {
							// buat total retensi bisa berubah2 sesuai dengan kurs nya
							totRetLifeV2 = totalRetensi(totRetLifeV2, map, lkuId, flagKursV2, kursUs);
							//flagKurs = changeKurs(lkuId);
							// buat total reas bisa berubah2 sesuai dengan kurs nya 
							//if(map.get("A1") != null && new BigDecimal(map.get("A1").toString()).compareTo(new BigDecimal(0)) == 1) {
								totReasLifeV2 = totalReas(totReasLifeV2, map, lkuId, flagKursV2, kursUs);
								flagKursV2 = changeKurs(lkuId);
							//}						
						}						
					}
	
				}
				
				result.add(map);
				
				/* Type Reas & Subtotal Retensi */
				/**
				 * Reas Individu 
				 * <= 750 jt : non-reas
				 * 750 jt < x <= 1M : treaty
				 * 1 M < x <=1.5 M : Fakultatif Obligatory 
				 *	> 1.5 M : fakultatif
				 *
				 * Reas MRI
				 * <= 750 jt : non-reas
				 *	750 jt < x <= 7M : treaty
				 *	> 7M atau EM > 250 % : fakultatif
				 * special case : usia masuk > 65 th : fakultatif
				 * 		 		  (x+n) > 70 th : fakultatif
				 */				
				SortedMap sub = new TreeMap();
				if(map.get("L").toString().equals("INFORCE") || map.get("L").toString().equals("POLICY IS BEING PROCESSED")) {
					if(cL == (Integer)pivot.get("lifeCount") && cL != 0) {
						sub = setStatusReas(sub, totRetLife, map, flagKurs, totReasLife, umurTt, lamaTanggung, EM);
						sub.put("flag","ok");
						result.add(sub);
						//result.add(oneRowSpace());
						cL = 0;
					}				
					if(cP == (Integer)pivot.get("paCount") && cP != 0) {
						sub = setStatusReas(sub, totRetPa, map, flagKurs, totReasPa, umurTt, lamaTanggung, EM);
						sub.put("flag","ok");
						result.add(sub);
						//result.add(oneRowSpace());
						cP = 0;
					}				
					if(cT == (Integer)pivot.get("tpdCount") && cT != 0) {
						sub = setStatusReas(sub, totRetTpd, map, flagKurs, totReasTpd, umurTt, lamaTanggung, EM);
						sub.put("flag","ok");
						result.add(sub);
						//result.add(oneRowSpace());
						cT = 0;
					}					
				}
				else {
					if(cL == (Integer)pivot.get("lifeCount") && cL != 0) {
						sub = setStatusReas(sub, totRetLifeV2, map, flagKursV2, totReasLifeV2, umurTt, lamaTanggung, EM);
						result.add(sub);
						//result.add(oneRowSpace());
						cL = 0;
					}				
					if(cP == (Integer)pivot.get("paCount") && cP != 0) {
						sub = setStatusReas(sub, totRetPaV2, map, flagKursV2, totReasPaV2, umurTt, lamaTanggung, EM);
						result.add(sub);
						//result.add(oneRowSpace());
						cP = 0;
					}				
					if(cT == (Integer)pivot.get("tpdCount") && cT != 0) {
						sub = setStatusReas(sub, totRetTpdV2, map, flagKursV2, totReasTpdV2, umurTt, lamaTanggung, EM);
						result.add(sub);
						//result.add(oneRowSpace());
						cT = 0;
					}					
				}
	
			}
			
			// spasi 1 baris untuk tiap spaj
			result.add(oneRowSpace());
			
		}
		// grand total
		SortedMap total = new TreeMap();
		total = oneRowSpace();
		total.put("E1", "Total Retensi Life");
		if(flagKurs == 0) {
			setGrandTotal(total, result, totRetLife, kursUs, "Rp.", "US$", "Total Retensi Life");
		}
		else if(flagKurs == 1) {
			setGrandTotal(total, result, totRetLife, kursUs, "US$", "Rp.", "Total Retensi Life");
		}
		
		total = new TreeMap();
		total = oneRowSpace();		
		total.put("E1", "Total Retensi PA");
		if(flagKurs == 0) {
			setGrandTotal(total, result, totRetPa, kursUs, "Rp.", "US$", "Total Retensi PA");
		}
		else if(flagKurs == 1) {
			setGrandTotal(total, result, totRetPa, kursUs, "US$", "Rp.", "Total Retensi PA");
		}

		total = new TreeMap();
		total = oneRowSpace();		
		total.put("E1", "Total Retensi TPD");
		if(flagKurs == 0) {
			setGrandTotal(total, result, totRetTpd, kursUs, "Rp.", "US$", "Total Retensi TPD");
		}
		else if(flagKurs == 1) {
			setGrandTotal(total, result, totRetTpd, kursUs, "US$", "Rp.", "Total Retensi TPD");
		}
		
		if(flagLifeOnly) {
			total = new TreeMap();
			total = oneRowSpace();		
			total.put("E1", "Total SAR");
			if(flagKurs == 0) {
				total.put("D", "Rp.");
				total.put("F", totSar);
				result.add(total);
				
				total = new TreeMap();
				total = oneRowSpace();
				total.put("E1", "Total SAR");
				total.put("D", "US$");
				total.put("F", totSar.divide(kursUsSar,2,BigDecimal.ROUND_HALF_UP));
				result.add(total);
				
				if(tglLahir != null) {
					total = new TreeMap();
					total = oneRowSpace();
					result.add(total);
					total = new TreeMap();
					total = oneRowSpace();
					total.put("E1", "Tipe Medis Calon Tertanggung :");
					result.add(total);
					for (SortedMap map : beingProcess) {
						if((Integer)map.get("lstb_id") == 1) {
							total = new TreeMap();
							total = oneRowSpace();
							String medis = uwDao.selectMedisDescNew(map.get("M").toString());
							total.put("E1", "Spaj " + 
									        map.get("M").toString() + 
									        " [Individu], Type Medis : " + medis
									       /* uwDao.getTipeMedis((Integer)map.get("lstb_id"),map.get("D").toString().replaceAll("\\$|\\.", "").toLowerCase(),
									        tglLahir,df.format(commonDao.selectSysdateTrunc()),totSarAll))*/);
							result.add(total);
						}
						else if((Integer)map.get("lstb_id") == 2) {
							
						}
					}					
				}				
			}
			else if(flagKurs == 1) {
				total.put("D", "US$");
				total.put("F", totSar);
				result.add(total);
				
				total = new TreeMap();
				total = oneRowSpace();
				total.put("E1", "Total SAR");
				total.put("D", "Rp.");
				total.put("F", totSar.multiply(kursUsSar));
				result.add(total);			
			}			
		}
		else {
			total = new TreeMap();
			total = oneRowSpace();		
			total.put("E1", "Total SAR");
			if(flagKurs == 0) {
				total.put("D", "Rp.");
				total.put("F", totSarAll);
				result.add(total);
				
				total = new TreeMap();
				total = oneRowSpace();
				total.put("E1", "Total SAR");
				total.put("D", "US$");
				total.put("F", totSarAll.divide(kursUsSar,2,BigDecimal.ROUND_HALF_UP));
				result.add(total);
				
				if(tglLahir != null) {
					total = new TreeMap();
					total = oneRowSpace();
					result.add(total);
					total = new TreeMap();
					total = oneRowSpace();
					total.put("E1", "Tipe Medis Calon Tertanggung :");
					result.add(total);
					for (SortedMap map : beingProcess) {
						if((Integer)map.get("lstb_id") == 1) {
							total = new TreeMap();
							total = oneRowSpace();
							total.put("E1", "Spaj " + 
									        map.get("M").toString() + 
									        " [Individu], Type Medis : " + 
									        uwDao.getTipeMedis((Integer)map.get("lstb_id"),map.get("D").toString().replaceAll("\\$|\\.", "").toLowerCase(),
									        tglLahir,df.format(commonDao.selectSysdateTrunc()),totSarAll));
							result.add(total);
						}
						else if((Integer)map.get("lstb_id") == 2) {
							
						}
					}					
				}
			}
			else if(flagKurs == 1) {
				total.put("D", "US$");
				total.put("F", totSarAll);
				result.add(total);
				
				total = new TreeMap();
				total = oneRowSpace();
				total.put("E1", "Total SAR");
				total.put("D", "Rp.");
				total.put("F", totSarAll.multiply(kursUsSar));
				result.add(total);			
			}			
		}
		
		return result;
	}
	
	public String getPeriodeAkhirPremiPokokTertunggak(String reg_spaj) {
		return reinstateDao.getPeriodeAkhirPremiPokokTertunggak(reg_spaj);
	}
	
	public void setGrandTotal(SortedMap total, List result, BigDecimal totRet, BigDecimal kurs, String currSymbol1, String currSymbol2, String Title) {
		total.put("D", currSymbol1);
		total.put("W", totRet);
		result.add(total);

		total = new TreeMap();
		total = oneRowSpace();
		total.put("E1", Title);
		total.put("D", currSymbol2);
		if(currSymbol1.contains("Rp")) total.put("W", totRet.divide(kurs,2,BigDecimal.ROUND_HALF_UP));
		else total.put("W", totRet.multiply(kurs));
		result.add(total);
	}	
	
	public SortedMap setStatusReas(SortedMap sub, BigDecimal totalRet, SortedMap map, Integer flagKurs, BigDecimal totReas, Integer umur, Integer lamaTanggung, BigDecimal EM) {
		sub = oneRowSpace();
		sub.put("W", totalRet);
		sub.put("C1", selectStatusReas(Integer.parseInt(map.get("lstb_id").toString()), flagKurs, totReas, umur, lamaTanggung, EM));
		
		return sub;
	}	
	
	public Integer changeKurs(String lku_id) {
		Integer flagKurs = 0;
		if(lku_id.equals("02")) flagKurs = 1;
		else if(lku_id.equals("01")) flagKurs = 0;
		
		return flagKurs;
	}
	
	public BigDecimal totalRetensi(BigDecimal totRet, SortedMap map, String lku_id, Integer flagKurs, BigDecimal kurs) {
		if(totRet.compareTo(new BigDecimal(0)) == 0) {
			totRet = totRet.add(new BigDecimal(map.get("W").toString()));
			//if(lku_id.equals("02")) flagKurs = 1;
		}	
		else {
			if(lku_id.equals("01")) {
				if(flagKurs == 0) totRet = totRet.add(new BigDecimal(map.get("W").toString()));
				else {
					//flagKurs =  0;
					totRet = totRet.multiply(kurs).add(new BigDecimal(map.get("W").toString()));
				}
			}
			else if(lku_id.equals("02")) {
				if(flagKurs == 1) totRet = totRet.add(new BigDecimal(map.get("W").toString()));
				else {
					//flagKurs =  1;
					totRet = totRet.divide(kurs,2,BigDecimal.ROUND_HALF_UP).add(new BigDecimal(map.get("W").toString()));
				}							
			}
		}	
		
		return totRet;
	}

	public BigDecimal totalReas(BigDecimal totReas, SortedMap map, String lku_id, Integer flagKurs, BigDecimal kurs) {
		if(lku_id.equals("01")) {
			if(flagKurs == 0) totReas = totReas.add(new BigDecimal(map.get("E").toString()));
			else {
				//flagKurs =  0;
				totReas = totReas.multiply(kurs).add(new BigDecimal(map.get("E").toString()));
			}
		}
		else if(lku_id.equals("02")) {
			if(flagKurs == 1) totReas = totReas.add(new BigDecimal(map.get("E").toString()));
			else {
				//flagKurs =  1;
				totReas = totReas.divide(kurs,2,BigDecimal.ROUND_HALF_UP).add(new BigDecimal(map.get("E").toString()));
			}							
		}
		
		return totReas;
	}
	
	public BigDecimal hitungRetensi(BigDecimal tsi, BigDecimal retensi, BigDecimal compareValue, SortedMap map) {
		if(tsi.add(retensi).compareTo(compareValue) == -1) map.put("W", tsi);
		else if(retensi.compareTo(compareValue) == -1 && tsi.add(retensi).compareTo(compareValue) >= 0)  {
			map.put("W", compareValue.subtract(retensi));  
			// isi tsi reas
			if(tsi.add(retensi).compareTo(compareValue) == 0)
				map.put("A1",new BigDecimal(0));
			else 
				map.put("A1",tsi.subtract(new BigDecimal(map.get("W").toString())));
		}
		else if(retensi.compareTo(compareValue) >= 0)  {
			map.put("W", 0);
			// isi tsi reas							
			map.put("A1", tsi);
		}		
		
		return retensi.add(tsi);
	}
	
	public Map setReasPosition(List<SortedMap> x) {
		Integer tempLife = 0;
		Integer tempPa = 0;
		Integer tempTpd = 0;
		Integer tempOth = 0;
		
		for(SortedMap map : x) {
			// kel life
			if((Integer)map.get("O") < 300 || (Integer)map.get("O") == 801 || (Integer)map.get("O") == 802 || 
			   (Integer)map.get("O") == 803 || (Integer)map.get("O") == 813 || (Integer)map.get("O") == 816 || 
			   (Integer)map.get("O") == 817 || (Integer)map.get("O") == 818 || ((Integer)map.get("lstb_id") == 2 && (Integer)map.get("O") < 900)) {
				tempLife++;
			}
			// kel PA
			else if((Integer)map.get("O") == 800 || (Integer)map.get("O") == 810) {
				tempPa++;
			}
			// kel TPD
			else if((Integer)map.get("O") == 804 || (Integer)map.get("O") == 807 || (Integer)map.get("O") == 808 || 
			   (Integer)map.get("O") == 812 || (Integer)map.get("O") == 814 || (Integer)map.get("O") == 815) {
				tempTpd++;
			}
			// kel lain2
			else if((Integer)map.get("O") == 805 || (Integer)map.get("O") == 806 ||  (Integer)map.get("O") == 809 ||  
			   (Integer)map.get("O") == 811 ||  (Integer)map.get("O") == 821) {	
				tempOth++;
			}
		}
		
		Map y = new HashMap();
		y.put("lifeCount", tempLife);
		y.put("paCount", tempPa);
		y.put("tpdCount", tempTpd);
		y.put("othCount", tempOth);
		
		return y;
	}
	
	public SortedMap oneRowSpace() {
		SortedMap spasi = new TreeMap();
		spasi.put("A", "&nbsp;"); spasi.put("B", "&nbsp;");	spasi.put("C", "");
		spasi.put("D", "");	spasi.put("E", "");	spasi.put("F", "");
		spasi.put("G", "");	spasi.put("H", "");	spasi.put("I", "");
		spasi.put("J", "");	spasi.put("K", "");	spasi.put("L", "");
		spasi.put("M", "");	spasi.put("N", "");	spasi.put("O", "");
		spasi.put("U", "");	spasi.put("V", "");	spasi.put("W", "");
		spasi.put("A1", "");spasi.put("B1", "");spasi.put("C1", "");	
		
		return spasi;
	}
	
	public List<SortedMap> sortingProduct(List<SortedMap> data, Boolean flagLifeOnly) {
		List<SortedMap> temp = new ArrayList<SortedMap>();
		List<SortedMap> a = new ArrayList<SortedMap>();
		List<SortedMap> b = new ArrayList<SortedMap>();
		List<SortedMap> c = new ArrayList<SortedMap>();
		List<SortedMap> d = new ArrayList<SortedMap>();
		List<SortedMap> e = new ArrayList<SortedMap>();
		List<SortedMap> f = new ArrayList<SortedMap>();
		List<SortedMap> g = new ArrayList<SortedMap>();
		int count = 0;
		
		//logger.info("size : " + data.size());
		for(SortedMap x : data) {
			Integer lsbsId = (Integer) x.get("O");
			
			if((Integer)x.get("lstb_id") == 1) {
				if(lsbsId < 300 ||  lsbsId == 801 || lsbsId == 802 || 
				   lsbsId == 803 || lsbsId == 813||  lsbsId == 814 ||
				   lsbsId == 815 || lsbsId == 816 || lsbsId == 817 ||
				   lsbsId == 818 || lsbsId == 823|| lsbsId == 827 || lsbsId == 828 ||
				   lsbsId == 830 ||	lsbsId == 835 || lsbsId == 837) {
					a.add(data.get(count++));
				}
				// kel PA
				else if(lsbsId == 800 || lsbsId == 810) {
					b.add(data.get(count++));
				}
				// kel TPD
				else if(lsbsId == 804 || lsbsId == 807 || lsbsId == 808 || 
				   lsbsId == 812 || lsbsId == 814 || lsbsId == 815) {
					c.add(data.get(count++));
				}
				// kel lain2
				else if(lsbsId == 805 || lsbsId == 806 ||  lsbsId == 809 ||  
				   lsbsId == 811 ||  lsbsId == 821) {	
					d.add(data.get(count++));
				}
				else {
					e.add(data.get(count++));
				}
			}
			else if((Integer)x.get("lstb_id") == 2) {
				f.add(data.get(count++));
			}
			else if((Integer)x.get("lstb_id") == 11) {
				g.add(data.get(count++));
			}

		}
		
		for(int loop1=0;loop1<a.size();loop1++)  temp.add(a.get(loop1));
		for(int loop1=0;loop1<f.size();loop1++)  temp.add(f.get(loop1));
		for(int loop1=0;loop1<g.size();loop1++)  temp.add(g.get(loop1));
		if(!flagLifeOnly) {
			for(int loop1=0;loop1<b.size();loop1++)  temp.add(b.get(loop1));
			for(int loop1=0;loop1<c.size();loop1++)  temp.add(c.get(loop1));
			for(int loop1=0;loop1<d.size();loop1++)  temp.add(d.get(loop1));
			for(int loop1=0;loop1<e.size();loop1++)  temp.add(e.get(loop1));
		}

		return temp;
	}
	
	public String selectStatusReas(Integer lstbId, Integer flagKurs, BigDecimal value, Integer umurTt, Integer lamaTanggung, BigDecimal EM) {
		//BigDecimal value = new BigDecimal(x);
		// Reas Individu
		if(lstbId == 1) {
			// kurs Rp
			if(flagKurs == 0) {
				if(value.compareTo(new BigDecimal(75E7)) <= 0) return "Non Reas";
				else if(value.compareTo(new BigDecimal(1E9)) <= 0) return "Treaty";
				else if(value.compareTo(new BigDecimal(15E8)) <= 0) return "Fakultatif Obligatory";
				else return "Fakultatif";							
			}
			// kurs US$
			else if(flagKurs == 1) {
				if(value.compareTo(new BigDecimal(75E3)) <= 0) return "Non Reas";
				else if(value.compareTo(new BigDecimal(1E5)) == -1) return "Treaty";
				else if(value.compareTo(new BigDecimal(15E4)) == -1) return "Fakultatif Obligatory";							
				else return "Fakultatif";							
			}
						
		}
		// Reas MRI
		else if(lstbId == 2) {
			// kurs Rp			
			if(flagKurs == 0) {
				// spesial case
				if(umurTt < 20 || umurTt > 65 || (umurTt + lamaTanggung > 70)) return "Fakultatif";
				else {
					if(value.compareTo(new BigDecimal(75E7)) <= 0) return "Non Reas";
					else if(value.compareTo(new BigDecimal(7E9)) <= 0) return "Treaty";
					else if(value.compareTo(new BigDecimal(7E9)) == 1 || EM.compareTo(new BigDecimal(25E7)) == 1) return "Fakultatif";
				}							
			}
			else if(flagKurs == 1) {
				// spesial case
				if(umurTt < 20 || umurTt > 65 || (umurTt + lamaTanggung > 70)) return "Fakultatif";
				else {
					if(value.compareTo(new BigDecimal(75E3)) <= 0) return "Non Reas";
					else if(value.compareTo(new BigDecimal(7E5)) <= 0) return "Treaty";
					else if(value.compareTo(new BigDecimal(7E5)) == 1 || EM.compareTo(new BigDecimal(25E3)) == 1) return "Fakultatif";								
				}							
			}

		}		
		
		return null;
	}
	
	public List selectAllDepartment() {
		return this.uwDao.selectAllDepartment();
	}
	
	public List selectAllEndorsements(String spaj) {
		return this.uwDao.selectAllEndorsements(spaj);
	}
	
	public List selectAllEndorse(String spaj) {
		return this.uwDao.selectAllEndorse(spaj);
	}
	
	public List selectAllLstBisnis(){
		return uwDao.selectAllLstBisnis();
	}
	
	public List selectAllLstBisnisRider(String ls_filter){
		return uwDao.selectAllLstBisnisRider(ls_filter);
	}
	
	public List selectAllLstBlackList(){
		return uwDao.selectAllLstBlackList();
	}
	
	public List selectAllLstMedicalCheckUp(){
		return uwDao.selectAllLstMedicalCheckUp();
	}

	public List selectAllLstMedicalCheckUpNew(){
		return uwDao.selectAllLstMedicalCheckUpNew();
	}

	public List selectLsInsurer(){
		return uwDao.selectLsInsurer();
	}	
	
	public List selectAllSpaj(String spaj) {
		return this.uwDao.selectAllSpaj(spaj);
	}
	
	public List selectBankEkaLife(){
		return this.uwDao.selectBankEkaLife(null);
	}
	
	public List selectBankEkaLife(String param){
		return this.uwDao.selectBankEkaLife(param);
	}
	
	public List selectBankPusat(){
		return this.uwDao.selectBankPusat();
	}
	
	public List selectViewerKontrolPinjaman(String spaj, String lusId) {
		return this.uwDao.selectViewerKontrolPinjaman(spaj,lusId);
	}

	public List selectViewerKontrolSimpanan(List spaj, String lusId) {
		return this.uwDao.selectViewerKontrolSimpanan(spaj, lusId);
	}
	
	public List selectViewerKontrolBilling(List spaj, String lusId) {
		return this.uwDao.selectViewerKontrolBilling(spaj, lusId);
	}
	
	public List selectViewerKontrolTahapan(List spaj, String lusId) {
		return this.uwDao.selectViewerKontrolTahapan(spaj, lusId);
	}
	
	public List selectViewerKontrolClaimNilaiTunai(String spaj, String lusId) {
		return this.uwDao.selectViewerKontrolClaimNilaiTunai(spaj, lusId);
	}
	
	public List selectViewerKontrolPowersave(String spaj, String lusId) {
		return this.uwDao.selectViewerKontrolPowersave(spaj, lusId);
	}
	
	public List selectViewerKontrolAgent(String spaj, String lusId) {
		return this.uwDao.selectViewerKontrolAgent(spaj, lusId);
	}
	
	public List selectViewerKontrolReferrer(String spaj) {
		return this.uwDao.selectViewerKontrolReferrer(spaj);
	}
	public List selectViewerKontrolDeduct(String spaj, String lusId) {
		return this.uwDao.selectViewerKontrolDeduct(spaj, lusId);
	}
	
	public List selectViewerKontrolReas(String spaj, int no, String lusId) {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("no", new Integer(no));
		params.put("lusId", lusId);
		return this.uwDao.selectViewerKontrolReas(params);
	}
	
	public List selectViewerKontrolMaturity(String spaj, String lusId) {
		return this.uwDao.selectViewerKontrolMaturity(spaj, lusId);
	}
	
	public List selectViewerKontrolPrivasi(String spaj, String lusId) {
		return this.uwDao.selectViewerKontrolPrivasi(spaj, lusId);
	}
	
	public List selectViewerKontrolReinstate(String spaj, String lusId){
		return this.uwDao.selectViewerKontrolReinstate(spaj, lusId);
	}
	
	public List selectViewerKontrolKomisi(String spaj, String lusId) {
		return this.uwDao.selectViewerKontrolKomisi(spaj, lusId);
	}
	
	public List selectViewerKontrolRewards(String spaj, String lusId) {
		return this.uwDao.selectViewerKontrolRewards(spaj, lusId);
	}
	
	public List selectGetYearViewerKontrolNAV(String spaj, String lusId) {
		return this.uwDao.selectGetYearViewerKontrolNAV(spaj, lusId);
	}
	
	public Map selectGetInfoForRate(String spaj) {
		return this.uwDao.selectGetInfoForRate(spaj);
	}
	
	public String selectCountRowViewerKontrolNAV(String spaj, String lusId) {
		return this.uwDao.selectCountRowViewerKontrolNAV(spaj, lusId);
	}
	
	public String selectGetLdecRateIfNoRow(String bisnisId, String thnKe, String umur) {
		return this.uwDao.selectGetLdecRateIfNoRow(bisnisId, thnKe, umur);
	}
	
	public String selectGetLdecRate(String bisnisId, String thnKe, String umur) {
		return this.uwDao.selectGetLdecRate(bisnisId, thnKe, umur);
	}
	
	public List selectViewerKontrolNAV(String spaj,String bisnisId, String thnKe, String umur, int index, String ldecUp, double multiply, int varAdd, String ldecBonus, double multiplyNon, int varAddNon, String lusId) {
		return this.uwDao.selectViewerKontrolNAV(spaj, bisnisId, thnKe, umur, index, ldecUp, multiply, varAdd, ldecBonus, multiplyNon, varAddNon, lusId);
	}
	
	public List selectViewerKontrolMedis(String spaj, int no, String lusId) {
		return this.uwDao.selectViewerKontrolMedis(spaj, no, lusId);
	}
	
	public List selectViewerKontrolBonus(String spaj, String lusId){
		return this.uwDao.selectViewerKontrolBonus(spaj, lusId);
	}
	
	public List selectViewerKontrolStableLink(String spaj, String lusId) {
		return this.uwDao.selectViewerKontrolStableLink(spaj, lusId);
	}
	
	public Double selectBiayaUlink(String spaj, int muke, int ljb){
		return this.uwDao.selectBiayaUlink(spaj, muke, ljb);
	}
	
	public Double selectRateRider(String lku, int umurTertanggung, int umurPemegang, int lsbs, int jenis)throws SQLException {
		return this.uwDao.selectRateRider(lku, umurTertanggung, umurPemegang, lsbs, jenis);
	}
	
	public Double selectResultPremi(int ljb_id, String reg_spaj)throws SQLException {
		return this.uwDao.selectResultPremi(ljb_id, reg_spaj);
	}
	
	public Double selectPremiSuperSehat(String lku, int umurTertanggung, int lsbs, int jenis) {
		return this.uwDao.selectPremiSuperSehat(lsbs, jenis, umurTertanggung, lku);
	}
	
	public Integer selectLjbIdFromBiayaUlink(String spaj, String businessID, String lsdbs_no){
		return this.uwDao.selectLjbIdFromBiayaUlink(spaj, businessID, lsdbs_no);
	}
	
	public String selectBasicName(Integer kd_rd, Integer nm_rd){
		return this.uwDao.selectBasicName(kd_rd, nm_rd);
	}
	public String selectJenisRider(Integer kd_rd, Integer nm_rd, String namaRider){
		return this.uwDao.selectJenisRider(kd_rd, nm_rd, namaRider);
	}
	
	public Double selectBiayaUlinkRider(String spaj, String businessID, String lsdbs_no){
		return this.uwDao.selectBiayaUlinkRider(spaj, businessID, lsdbs_no);
	}
	
	public List selectBillingInfoForTransfer(String spaj, int tahun, int premi){
		return this.uwDao.selectBillingInfoForTransfer(spaj, tahun, premi);
	}
		
	public List selectBisnisId(String spaj){
		return this.uwDao.selectBisnisId(spaj);
	}
	
	public List<Map<String, String>> selectMstReffBiiBySpaj(String spaj){
		return this.uwDao.selectMstReffBiiBySpaj(spaj);
	}
	
	public String selectBisnisName(BigDecimal bisnisId,BigDecimal bisnisNo){
		return uwDao.selectBisnisName(bisnisId,bisnisNo);
	}
	
	public String selectBusinessId(String spaj){
		return this.uwDao.selectBusinessId(spaj);
	}
	
	public Integer selectBusinessNumber(String spaj){
		return this.uwDao.selectBusinessNumber(spaj);
	}
	
	public String selectBusinessName(String reg_spaj){
		return this.uwDao.selectBusinessName(reg_spaj);
	}
	
	public int selectCekKomisi(String spaj) {
		return this.uwDao.selectCekKomisi(spaj);
	}
	
	public List selectChartNav(String jenis) {
		return this.uwDao.selectChartNav(jenis);
	}
	
	public List selectCsfCall(String spaj, String inout, String lus_id) {
		return this.uwDao.selectCsfCall(spaj, inout, lus_id);
	}
	
	public int selectCsfCallReminder() {
		return this.uwDao.selectCsfCallReminder();
	}
	
	public List selectCsfCallReminderList() {
		return this.uwDao.selectCsfCallReminderList();
	}	
	
	public List selectCsfCallSummary(String spaj) {
		return this.uwDao.selectCsfCallSummary(spaj);
	}
	
	public List<Date> selectSudahProsesNab(String reg_spaj){
		return this.uwDao.selectSudahProsesNab(reg_spaj);
	}
	
	public void insertLstReffBii(AddReffBii addReffBii){
		commonDao.insertLstReffBii(addReffBii);
	}
	
	public String insertMstKuesionerBrand(KuesionerBrand kbrand, Date now, String lus_id){
		String result = "";
		try {
			kbrand.setMkb_input_date(now);
			kbrand.setLus_id(lus_id);
			commonDao.insertMstKuesionerBrand(kbrand);
			result = "Data Polling berhasil disimpan";
		} catch (Exception e) {
			result = "Data Polling gagal disimpan";
		}
		return result;
	}
	
/*	public void insertLstReffBiiBSM(Integer lrb_id, String nm_agent, String lcb_no, Integer aktif, String kd_agent, Integer ref, Date tglAktif, Date tglNonAktif, String pos_code, Date tglUpdate, Integer jn_bank, String lus_id ){
		commonDao.insertLstReffBiiBSM(lrb_id, nm_agent, lcb_no, aktif, kd_agent, ref,tglAktif,tglNonAktif,pos_code,tglUpdate,jn_bank, lus_id);
	}*/
	
	public List<Map> selectCabangBsmByWilayah(String lcb_no){
		return commonDao.selectCabangBsmByWilayah(lcb_no);
	}
	
	/**
	 * Fungsi :Untuk mencari polis,dimana ada dua model pencarian
	 * 			1. Mencari polis berdasarkan posisi dan type bisnis (lssaId=null)
	 * 			2. Mencari polis berdasarkan status aksep , posisi dan type bisnis (akseptasi khusus)
	 * @param posisi
	 * @param tipe
	 * @param lssaId
	 * @param jenisTerbit
	 * @return List
	 */
	public List selectDaftarSPAJ(String posisi, int tipe,Integer lssaId,Integer jenisTerbit){
		return this.uwDao.selectDaftarSPAJ(posisi, tipe,lssaId,jenisTerbit);
	}
	
	public String selectRedeemBlock(String rdBlock){
		return this.uwDao.selectRedeemBlock(rdBlock);
	}
	
	public List selectDaftarSPAJPayment(String posisi, int tipe,Integer lssaId,Integer jenisTerbit){
		return this.uwDao.selectDaftarSPAJPayment(posisi, tipe,lssaId,jenisTerbit);
	}
	
	public List selectDaftarSPAJMall(String posisi, int tipe,Integer lssaId,Integer jenisTerbit){
		return this.uwDao.selectDaftarSPAJMall(posisi, tipe,lssaId,jenisTerbit);
	}
	
	public List selectDaftarSPAJOnline(String posisi, int tipe,Integer lssaId,String lus_id){
		return this.uwDao.selectDaftarSPAJOnline(posisi, tipe,lssaId,lus_id);
	}
	
	public List selectDaftarSPAJSimple(String posisi, int tipe,Integer lssaId,String lus_id, String jenis){
		return this.uwDao.selectDaftarSPAJSimple(posisi, tipe,lssaId,lus_id,jenis);
	}
	
	public List selectDaftarSPAJCrossSelling(String posisi, int tipe,Integer lssaId,Integer jenisTerbit, String lca_id){
		return this.uwDao.selectDaftarSPAJCrossSelling(posisi, tipe,lssaId,jenisTerbit, lca_id);
	}
	
	
	public List selectDaftarSPAJ1(String posisi, int tipe,Integer lssaId,Integer jenisTerbit){
		return this.uwDao.selectDaftarSPAJ1(posisi, tipe,lssaId,jenisTerbit);
	}
	/**
	 * Fungsi :Untuk mencari polis,dimana ada dua model pencarian
	 * 			1. Mencari polis berdasarkan posisi dan type bisnis (lssaId=null)
	 * 			2. Mencari polis berdasarkan status aksep , posisi dan type bisnis (akseptasi khusus)
	 * @param posisi
	 * @param tipe
	 * @param lssaId
	 * @return List
	 */
	public List selectDaftarSPAJ_valid(String posisi, int tipe,Integer lssaId,Integer lus_id, String cab_bank){
		return this.uwDao.selectDaftarSPAJ_valid(posisi, tipe,lssaId,lus_id, cab_bank);
	}
	
	public List selectDaftarSpajUwSimasPrima(int lspd_id, int jn_bank, int flag_approve, int lus_id, String cab_bank) {
		return this.uwDao.selectDaftarSpajUwSimasPrima(lspd_id, jn_bank, flag_approve, lus_id,cab_bank); 
	}
	
	public List selectAgentUploadNewBusinessList(String jenis, String lus_id) {
		return this.uwDao.selectAgentUploadNewBusinessList(jenis, lus_id); 
	}
	
	public List selectDaftarSpajInputanBank(Integer lus_id, Integer lstb_id, Integer lspd_id) {
		return this.uwDao.selectDaftarSpajInputanBank(lus_id, lstb_id, lspd_id);
	}
	
	public List selectDaftarSpajInputanASM(Integer lus_id, Integer lstb_id, Integer lspd_id) {
		return this.uwDao.selectDaftarSpajInputanASM(lus_id, lstb_id, lspd_id);
	}
	
	public List selectDaftarSpajInputanMall(Integer lus_id, Integer lstb_id, Integer lspd_id) {
		return this.uwDao.selectDaftarSpajInputanMall(lus_id, lstb_id, lspd_id);
	}
	
	public List selectDataNasabahSinarmasSekuritas(String lcb, String jenisReport, Date tglAwal, Date tglAkhir) {
		if(jenisReport.equals("tanggal_input")){
			return this.uwDao.selectDataNasabahSinarmasSekuritas(lcb, jenisReport, tglAwal, tglAkhir); //SUDAH INCL STABLE LINK SMS
		}else if(jenisReport.equals("tanggal_efektif")){
			return this.uwDao.selectDataNasabahSinarmasSekuritas(lcb, jenisReport, tglAwal, tglAkhir); //SUDAH INCL STABLE LINK SMS
		}else if(jenisReport.equals("tanggal_prod")){
			return this.uwDao.selectDataNasabahSinarmasSekuritas(lcb, jenisReport, tglAwal, tglAkhir); //SUDAH INCL STABLE LINK SMS
		}else if(jenisReport.equals("jatuh_tempo")){
			return this.uwDao.selectJatuhTempoSekuritas(lcb, tglAwal, tglAkhir); //SUDAH INCL STABLE LINK SMS
		}else if(jenisReport.equals("roll_over")){
			return this.uwDao.selectRolloverSekuritas(lcb, tglAwal, tglAkhir); //SUDAH INCL STABLE LINK SMS
		}else if(jenisReport.equals("cair")){
			return this.uwDao.selectSudahCairSekuritas(lcb, tglAwal, tglAkhir);
		}
		return new ArrayList();
	}
	
	public List selectDataNasabah(List spaj) {
		return this.uwDao.selectDataNasabah(spaj);
	}
	/**@Fungsi: Untuk menampikan Data Usulan Asuransi secara detail, seprti premi,up,produk yang dipilih,dll
	 * @param	String spaj
	 * @return 	Map
	 * @author 	Ferry Harlim
	 * */
	public Map selectDataUsulan(String spaj){
		return this.uwDao.selectDataUsulan(spaj);
	}
	
	public Integer selectFlagSpecial(String spaj){
		return this.uwDao.selectFlagSpecial(spaj);
	}
	
	public Map selectDataUsulan2(String spaj){
		//select daftar usulan gak join table pay mode
		return this.uwDao.selectDataUsulan2(spaj);
	}
	
	public Map selectDataUsulan3(String spaj){
		//select daftar usulan gak join table pay mode
		return this.uwDao.selectDataUsulan3(spaj);
	}
	
	public Integer selectNoSuratAtDb() {
		return this.uwDao.selectNoSuratAtDb();
	}
	
	public List<HashMap> selectNoSuratPenawaran() {
		return uwDao.selectNoSuratPenawaran();
	}
	
	public void updateNoSuratPenawaran(List<Integer> counter) {
		uwDao.updateNoSuratPenawaran(counter);
	}
	
	public void addCounterNoSurat(String no) {
		this.uwDao.addCounterNoSurat(no);
	}
	
	public List selectDetailManfaat(String spaj, Integer urut) {
		return this.uwDao.selectDetailManfaat(spaj, urut);
	}
	
	public List selectDetailManfaatTambahan(String spaj) {
		return this.uwDao.selectDetailManfaatTambahan(spaj);
	}
	
	public List<Map> selectSummaryAllNewBusiness(){
		return this.uwDao.selectSummaryAllNewBusiness();
	}
	
	public List<Map> selectPolisBelumDicetak(){
		return this.uwDao.selectPolisBelumDicetak();
	}
	
	public List<Map> selectPolisBelumDitransfer(){
		return this.uwDao.selectPolisBelumDitransfer();
	}
	
	public Filing selectSpajOrNoPolis(String reg_spaj){
		return filingDao.selectSpajOrNoPolis(reg_spaj);
	}
	
	public List selectNoBoxFromMBox(int lde_id){
		return filingDao.selectNoBoxFromMBox(lde_id);
	}
	
	public List<Filing> selectFilterSPAJInFilling(String reg_spaj){
		return filingDao.selectFilterSPAJInFilling(reg_spaj);
	}
	
	public Integer selectCountSpajAfterAcceptInFilling(String reg_spaj){
		return filingDao.selectCountSpajAfterAcceptInFilling(reg_spaj);
	}
	
	public void prosesFilingUW(Filing filing) {
		Integer countBundle = filingDao.selectCountBundleFilling(filing.getKd_box());
		Integer kd_bundle = countBundle+1;
		filing.setKd_bundle(FormatString.rpad("0", Integer.toString(kd_bundle), 3));
		
		//insert ke tabel filing.mbundle
		filingDao.insertMBundle(filing);
		
		SimpleDateFormat formatDate =new SimpleDateFormat("yyyyMMdd");
		Date now = filing.getTgl_aktif();
		//String kd_file = formatDate.format(now)+"-"+FormatString.rpad("0", Integer.toString(kd_bundle), 3);
		String kd_file = filing.getKd_box()+"-"+FormatString.rpad("0", Integer.toString(kd_bundle), 3);
		filing.setKd_file(kd_file);
		filing.setTipe_file("NB");
		filing.setNoref_file(filing.getMspo_policy_no());
		filing.setInsured("NB DOKUMEN");
		filing.setB_date(filing.getMste_beg_date());
		filing.setE_date(filing.getMste_end_date());
		filing.setUser_id(filing.getRequest_by());
		filing.setNo_index(filing.getMspo_policy_no());
		
		//insert ke tabel filing.mfile
		filingDao.insertMFile(filing);
		uwDao.insertMstPositionSpaj(filing.getUser_id(), "FILING DOC NB KE BOX : "+filing.getKd_box()+" URUTAN :"+ FormatString.rpad("0", Integer.toString(kd_bundle), 3) , filing.getReg_spaj(), 0);
	}
	
	public void cancelBanyakPolis(User currentUser) {
		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		//List<String> daftarSpaj = bacDao.selectCancelBanyakPolis();
		
		String[] daftarSpaj = {"05200900799"};		
		
		int hitung=0;
		for(String spaj : daftarSpaj) {
			hitung++;
			this.cancelPolis.cancelPolisFromTandaTerimaPolis(spaj, "BATALKAN POLIS ATAS PERMINTAAN NASABAH, REQUEST BY HAYATIN", currentUser);
		}
		logger.info("Processed : " + hitung + " polis");
		
	}
	
	public List<Icd> uploadIcdCode(CommandUploadUw cmd,  HttpServletRequest request) throws Exception{
		return uploadBac.uploadIcdCode(cmd, request); 
	}
	
	public String selectGetPDF(String status, String fileName) { 
		return bacDao.selectGetPDF(status, fileName);
	}
	
	public File prosesPerjanjianKeagenan(String fileString, Boolean scFile, String filename, Map content){
		try{
			fileString = props.getProperty("pdf.save.perjanjianKeagenan")+"\\"+filename+".pdf";
//			try{
//				  File l_file = new File(fileString);
//			      FileInputStream in = new FileInputStream(l_file);				      
//			      in.close();
//			   
//			      scFile=true;
//			}catch (Exception e) {
//				logger.error("ERROR :", e);
//			}
			
			if(!scFile) {
				
				PrintPolisPerjanjianAgent.generatePolis(content);
			}
			
			//fileString = "C:\\EkaWeb\\PDF\\POLIS\\master\\"+filename+".pdf";
			
			File l_file = new File(fileString);
			return l_file;
		}catch (Exception e) {
			logger.error("ERROR :", e);
	         throw new RuntimeException("Gagal");
		}
	}
	
	public List<Map> selectLstReffererLeader(String referrer_id){
		return this.commonDao.selectLstReferrerLeader(referrer_id);
	}
	
	public List<Map> selectLstReferrerBiiWithId(String referrer_id){
		return this.commonDao.selectLstReferrerBiiWithId(referrer_id);
	}
	
	public List selectLstCabangBii(Integer jenis){
		return commonDao.selectLstCabangBii(jenis);
	}
	
	public List selectLstRefLeader(String referrer_id){
		return commonDao.selectLstRefLeader(referrer_id);
	}
	
	public String selectRefLeader(String referrer_id){
		return commonDao.selectRefLeader(referrer_id);
	}
	
	public List selectLstReferrerLeader1(String referrer_id){
		return commonDao.selectLstReferrerLeader1(referrer_id);
	}
	
	public List selectLstReferrerLeader2(String referrer_id){
		return commonDao.selectLstReferrerLeader2(referrer_id);
	}
	
	public List selectLstReferrerLeader3(String referrer_id){
		return commonDao.selectLstReferrerLeader3(referrer_id);
	}
	
	public Integer selectGenerateLrbId()throws DataAccessException{
		return commonDao.selectGenerateLrbId();
	}
	
	public Integer selectCountKodeAgent(String kode_agent){
		return commonDao.selectCountKodeAgent(kode_agent);
	}
	
	public Integer selectLstReffBiiMaxLrbId(){
		return commonDao.selectLstReffBiiMaxLrbId();
	}
	
	
	public Integer selectCountLstCabangBii(String kd_cabang,Integer jn_bank){
		return commonDao.selectCountLstCabangBii(kd_cabang,jn_bank);
	}
	
	public Integer selectCountTotalRider(String reg_spaj){
		return commonDao.selectCountTotalRider(reg_spaj);
	}
	
	public Integer selectCountMstSurrender(String reg_spaj){
		return commonDao.selectCountMstSurrender(reg_spaj);
	}
	
	public List<Map> selectPremiRiderInMstRiderSave(String reg_spaj){
		return commonDao.selectPremiRiderInMstRiderSave(reg_spaj);
	}
	
	public Integer selectCountNewBusiness(String reg_spaj){
		return  commonDao.selectCountNewBusiness(reg_spaj);
	}
	
	public Map select_rider_save(String spaj){
		return (HashMap) this.bacDao.select_rider_save(spaj);
	}
	
	public Integer selectSudahSurrender(String reg_spaj) {
		return (Integer) this.bacDao.selectSudahSurrender(reg_spaj);		
	}
	
	public Integer selectMstPowerSaveRoCaraBayar(String reg_spaj){
		return commonDao.selectMstPowerSaveRoCaraBayar(reg_spaj);
	}
	
	public String selectCountRegSpaj(String mns_kd_nasabah){
		return commonDao.selectCountRegSpaj(mns_kd_nasabah);
	}
	
	public void prosesRewardTertinggal(String reg_spaj, User currentUser){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		/*
		msco_pay_date = besok
		paid = 0
		flag_upload = 1 bila lsbp_id = 45
		msco_trf_date = msco_pay_date
		counter msco_no = 
		edit eka.mst_counter where msco_number = 101
		*/

		String msag_id = uwDao.selectAgenFromSpaj(reg_spaj);
		String lsbs_id = uwDao.selectBusinessId(reg_spaj);
		
		Map<String, Object> rekruter = this.uwDao.selectRekruterDenganKomisi(reg_spaj, msag_id);
		
		if(rekruter != null && !products.powerSave(lsbs_id)) {
			int jenis_rekrut = Integer.valueOf((String) rekruter.get("MSRK_JENIS_REKRUT"));
			//kalau jenis_rekrut = 2 (rekrut langsung), cek status aktifnya dari mst_agent
			//kalau jenis_rekrut lainnya, cek status aktifnya dari mst_rekruter
			if(((jenis_rekrut == 2 || jenis_rekrut==3) && rekruter.get("MSAG_ACTIVE").toString().equals("1")) || 
					(!(jenis_rekrut == 2 || jenis_rekrut==3) && rekruter.get("MSRK_AKTIF").toString().equals("1"))) {
				
				Rekruter rek = uwDao.selectRekruterFromAgen(rekruter.get("MSRK_ID").toString());
				if(rek.getMsag_bay() == null) rek.setMsag_bay(0);

				double pengali = 1;
				
				//Yusuf (22/12/2010) bila business partner, maka dapat reward hanya 5%
				if(rek.getLcalwk().equals("0119")) pengali = 0.5;
				//Yusuf (23/03/09) - Per 1 April OR dan Reward tidak 35% lagi tapi full
				else if(komisi.isLinkSingle(reg_spaj, false)) pengali = 0.35;
				else if(komisi.isEkalink88Plus(reg_spaj, true)) pengali = 0.7;
				else if(products.stableLink(lsbs_id)) pengali = 0.1;
				
				Double kom = ((BigDecimal) rekruter.get("MSCO_COMM")).doubleValue();
				Double kurs = ((BigDecimal) rekruter.get("MSCO_NILAI_KURS")).doubleValue();
				Double reward = FormatNumber.rounding((kom * 0.1 * pengali), false, 25);
				Date sysdate = commonDao.selectSysdateTruncated(0);
				Double tax = FormatNumber.rounding(komisi.f_load_tax(reward, sysdate, rekruter.get("MSRK_ID").toString()), true, 25);
				rekruter.put("NO_ACCOUNT", StringUtils.replace(rekruter.get("NO_ACCOUNT").toString(), "-",""));
				if(rek.getMsag_bay().intValue() != 1){
					this.uwDao.insertMst_reward(reg_spaj, 1, 2, rekruter.get("MSRK_JENIS_REKRUT").toString(), rekruter.get("MSRK_ID").toString(),
							rekruter.get("MSRK_NAME").toString(), rekruter.get("NO_ACCOUNT").toString(), 
							rekruter.get("LBN_ID").toString(), reward, tax, kurs, currentUser.getLus_id(), bacDao.selectLineBusLstBisnis(lsbs_id));
				}
			}
		}
	}
	
	/*=======================EditDataDao===================================*/
	public List<DepositPremium> selectTitipanPremiBySpaj(String reg_spaj) {
		return this.editDataDao.selectTitipanPremiBySpaj(reg_spaj);
	}
	
	public List<Payment> selectPaymentBySpaj(String reg_spaj) {
		return this.editDataDao.selectPaymentBySpaj(reg_spaj);
	}
	/*
	public String editData(String edit, CommandEditData cmd, User currentUser) {
		
		NumberFormat nf = NumberFormat.getNumberInstance();
		
		if("TitipanPremi".equals(edit)) {
			return this.editDataDao.editTitipanPremi(cmd, currentUser);
		//
//		}else if("Payment".equals(edit)) {
//			return this.editDataDao.editPayment(cmd, currentUser);
//		}else if("AhliWaris".equals(edit)) {
//			return this.editDataDao.editAhliWaris(cmd, currentUser);
		}else {
			return "";
		}
	}
	*/

	public String editDataPowersave(Map params, User currentUser) {
		return editDataDao.editDataPowersave(params, currentUser);
	}
	
	public void setKomisi(Komisi komisi) {
		this.komisi = komisi;
	}	
	public String selectIcdDesc(String licId) {
		return uwDao.selectIcdDesc(licId);
	}
	
	/* ======================= Scheduler Fitrah ======================= */
	
	public void prosesGenerate21HariFitrah() throws DataAccessException{
		basDao.prosesGenerate21HariFitrah();
	}	
	
	/*======================BEGIN Ubah Powersave ====================================*/
	public CommandPowersaveUbah selectDataNasabah(String spaj){
		return bacDao.selectDataNasabah(spaj);
	}
	
	public PowersaveProses selectProsesPowersave(String spaj){
		return bacDao.selectProsesPowersave(spaj);
	}
	
	public Double selectRatePowerSave (PowersaveProses powersaveProses){
		return bacDao.selectRatePowerSave(powersaveProses);
	}
	
	/**
	 *
	 * @param spaj
	 * @param jns
	 * 	   0 : cek pinj konvensional yg masih aktif / belum lunas
	 *     1 : cek pinj powersave
	 * @return
	 */
	public Integer selectCekPinjaman(String spaj, Integer jns) {
		if(jns==0||jns==1){
			return bacDao.selectCekPinjaman(spaj, jns);
		}else return 0;
	}
	
	public HashMap selectProdInsured(String spaj) {
		return bacDao.selectProdInsured(spaj);
	}
	
	public Double selectmst_product_insuredUP(String spaj){
		return this.akseptasiDao.selectmst_product_insuredUP(spaj);
	}
	
	public List<Map> selectRiderSave(String spaj){
		return bacDao.selectRiderSave(spaj);
	}
	
	public Integer selectCountPwrRO(String spaj, Date depdate){
		return bacDao.selectCountPwrRO(spaj, depdate);
	}
	
	public HashMap selectCekBlacklist(String tgl_lahir, String nama){
		return bacDao.selectCekBlacklist(tgl_lahir, nama);
	}

	
	public Integer selectExistBlacklist(String tgl_lahir, String nama, String noIdentity){
		return uwDao.selectExistBlacklist(tgl_lahir, nama, noIdentity);
	}
	
	public boolean updateJnsRO_PwrRo(String spaj, Integer ro, Date depdate){
		Boolean result=false;
		try{
			bacDao.updateJnsRO_PwrRo(spaj, ro, depdate);
			result=true;
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("ERROR :", e);
		}
		return result;
	}
	
	public boolean updateJnsRO_PwrPro(String spaj, Integer ro, Date depdate){
		Boolean result=false;
		try{
			bacDao.updateJnsRO_PwrPro(spaj, ro, depdate);
			result=true;
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("ERROR :", e);
		}
		return result;
	}
	
	public Boolean insertPwrUbah(PowersaveUbah powersaveUbah){	
		Boolean result= false;
		String ld_no="";
		String ls_reg="";
		try{
			Date ldt_now=commonDao.selectSysdate();
			ld_no = uwDao.selectGetCounter(81, "01");
			ls_reg=new SimpleDateFormat("MMyyyy").format(ldt_now)+"EPS"+FormatString.rpad("0", ld_no, 5);
		
			powersaveUbah.setMpu_no_reg(ls_reg);
			
			Date depositDate=bacDao.select_max_deposit_date();
			
			//kalo deposit date >= batas proses
			
			Integer countUbah=bacDao.selectUbahCount(powersaveUbah);
			if(countUbah>0){
				if(depositDate.compareTo(powersaveUbah.getMpu_tgl_awal())<0){
					bacDao.delete_mst_powersave_ubah(powersaveUbah);
					bacDao.insertPwrUbah(powersaveUbah);
				}else{
					return false;
				}
			}else{
				bacDao.insertPwrUbah(powersaveUbah);
			}
			
			uwDao.updateCounter(ld_no, 81, "01");
			result=true;
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("ERROR :", e);
		}
		
		
		return result;
	}
	
	public void prosesEmailSMSAkseptasi(String spaj, Integer lssa_id){
		try{
			Pemegang pemegang = new Pemegang();
			pemegang = bacDao.selectpp(spaj);
			Smsserver_out sms_out = new Smsserver_out();
			sms_out.setJenis(21);
			sms_out.setLjs_id(21);
			String pesan = "Diberitahukan bahwa Polis Anda dengan Nomor "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+" saat ini telah selesai dan akan disampaikan oleh Team Sales kami.</br>Terima kasih atas kepercayaan Anda untuk bergabung bersama PT Asuransi Jiwa Sinarmas MSIG Tbk. Silakan mengakses ke https://epolicy.sinarmasmsiglife.co.id/ untuk melihat data Polis Anda.";
			sms_out.setRecipient(pemegang.getNo_hp()!=null?pemegang.getNo_hp():pemegang.getNo_hp2());
			Integer countAkseptasiKhusus = bacDao.selectCountAkseptasiKhusus(spaj);
			if(lssa_id==5){
				if(pemegang.getMspo_jenis_terbit()==1){//softcopy
					sms_out.setText("Nasabah Yth, PENGAJUAN ASURANSI SDH KAMI SETUJUI dgn No. Polis "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+". Softcopy polis silakan akses ke https://epolicy.sinarmasmsiglife.co.id/ dan sdh dikirim ke alamat email "+(pemegang.getMspe_sex()==1?"Bpk.":"Ibu.") );
				}
				
			}else if(lssa_id==10){
				if(pemegang.getMspo_jenis_terbit()==1){//softcopy
					sms_out.setText("Nasabah Yth, PENGAJUAN ASURANSI SDH KAMI SETUJUI dgn No. Polis "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+". Softcopy polis silakan akses ke https://epolicy.sinarmasmsiglife.co.id/ dan sdh dikirim ke alamat email "+(pemegang.getMspe_sex()==1?"Bpk.":"Ibu.")+"Kekurangan data akan disampaikan oleh Team Sales kami." );
					pesan = "Diberitahukan bahwa Polis Anda dengan Nomor "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+" saat ini telah selesai dan kekurangan data akan disampaikan oleh Team Sales kami.</br>Terima kasih atas kepercayaan Anda untuk bergabung bersama PT Asuransi Jiwa Sinarmas MSIG Tbk. Silakan mengakses ke https://epolicy.sinarmasmsiglife.co.id/ untuk melihat data Polis Anda.";
				}
			}
			Boolean accessGranted = true;
			if((lssa_id==5 && countAkseptasiKhusus>=1) || pemegang.getMspo_jenis_terbit()==0){
				accessGranted=false;
			}
			
			//Mendapatkan Email dari User U/W yg login pada saat memberikan Akseptasi/Akseptasi Khusus
			List<Map> useruw = new ArrayList<Map>();
			useruw = uwDao.selectAksepUserUW(spaj);
			String userbcc = "";
			for(int j=0; j<useruw.size();j++){
				Map y = (HashMap) useruw.get(j);
				if (useruw.size() >= 1){
					String email = (String) y.get("LUS_EMAIL");
					if (email==null){
						userbcc = userbcc;
					}
					else{
						if (j == (useruw.size()-1)){
							userbcc = userbcc + email;
						}
						else{
							userbcc = userbcc + email + ";";
						}
					}	
				}
			}
			String adminbcc = uwDao.selectAksepAdmin(spaj);
			String agenbcc = uwDao.selectAksepAgen(spaj);
			String emailbcc = agenbcc + ";" + adminbcc + ";" + userbcc;
			String[] bcc = emailbcc.split(";");
			
			if(accessGranted){
				if(pemegang.getMspe_email()!=null){
					email.sendImageEmbeded(
							true,"policy_service@sinarmasmsiglife.co.id", new String[] {pemegang.getMspe_email()} ,
							null, bcc,
//							new String[] {"ingrid@sinarmasmsiglife.co.id","Ariani@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id"},
//							new String[] {props.getProperty("admin.deddy")},  
							"Pemberitahuan Proses Pengajuan Polis", 
							"Kepada Yth.</br>"+
							(pemegang.getMspe_sex()==1?"Bpk. ":"Ibu. ")+ pemegang.getMcl_first()+"</br></br>"+
							pesan, null,null);
					basDao.insertSmsServerOutWithGateway(sms_out, true);
				}else{
					basDao.insertSmsServerOutWithGateway(sms_out, true);
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void prosesEndorsemen(String spaj, Integer lsbs_id, Integer endorse){
	//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Integer jumlah_rider = uwDao.selectCountTotalRider(spaj);
		Integer[] v_intRiderId;
		Integer[] v_intRiderNo;
		Integer mspo_provider = uwDao.selectGetMspoProvider(spaj);
		if(mspo_provider==null){
			mspo_provider=0;
		}
		
		if((jumlah_rider>0 || lsbs_id==183 || lsbs_id==193 || lsbs_id==195 || lsbs_id==189 || lsbs_id==204||endorse==1) && mspo_provider==2){
			Datausulan datausulan = bacDao.selectDataUsulanutama(spaj);
		
			int i =0;
			List<Datarider> rd= (List<Datarider>) bacDao.selectDataUsulan_rider(spaj);
			List<Datarider> rd_new = (List<Datarider>) bacDao.selectDataUsulan_endors(spaj);
			if(products.provider(Integer.toString(datausulan.getLsbs_id())) || 
					products.powerSave(Integer.toString(datausulan.getLsbs_id())) ||
					products.stableLink(Integer.toString(datausulan.getLsbs_id())) ||
					products.stableSave(datausulan.getLsbs_id(), datausulan.getLsdbs_number()) ||
					datausulan.getLsbs_id()==183 || datausulan.getLsbs_id()==189 || datausulan.getLsbs_id()==193 || datausulan.getLsbs_id()==195 || datausulan.getLsbs_id()==204 ||
					products.SuperSejahtera(Integer.toString(datausulan.getLsbs_id())) ||
					datausulan.getLsbs_id()==145 ||
					products.DanaSejahtera(Integer.toString(datausulan.getLsbs_id())) ||
					products.unitLink(Integer.toString(datausulan.getLsbs_id()))
					){
				v_intRiderId = new Integer[jumlah_rider.intValue()+1];
				v_intRiderNo = new Integer[jumlah_rider.intValue()+1];
				String no_endors = "";
				String cek_no_endors = null;
				if(jumlah_rider>0){
					v_intRiderId[i] = rd.get(i).getLsbs_id();
					v_intRiderNo[i] = rd.get(i).getLsdbs_number();
				}else{
//					v_intRiderId[i] = rd_new.get(i).getLsbs_id();
//					v_intRiderNo[i] = rd_new.get(i).getLsdbs_number();
				}
				String lca_id = uwDao.selectCabangFromSpaj(spaj);
				if(lca_id!=null){
					cek_no_endors = uwDao.selectGetNoEndors(spaj);
					if(cek_no_endors!=null){
						no_endors = cek_no_endors;
					}else{
						if((products.provider(Integer.toString(datausulan.getLsbs_id())) || products.unitLink(Integer.toString(datausulan.getLsbs_id())) && mspo_provider==2 || datausulan.getLsbs_id()==189) ){
							no_endors = sequence.sequenceNo_Endorse(lca_id);
						}
					}
				}
				Endors endors = new Endors();
				endors.setMsen_endors_no(no_endors);
				endors.setReg_spaj(spaj);
				endors.setMsen_internal(0);
				endors.setMsen_alasan("");
				endors.setMsen_endors_cost(null);
				
				//(Deddy)- req rudi(13/10/2009): msen_print =1, msen_active_date = tgl_transaksi(stable link), auto rider = 0, lspd_id = 99, tgl_trans = sysdate
				endors.setMsen_print(1);
				String spaj_conversi = selectCekSpajSebelumSurrender(spaj);
				if(spaj_conversi!=null){
					endors.setMsen_input_date(selectBDateSLink(spaj));
					endors.setMsen_active_date(endors.getMsen_input_date());
				}else{
					endors.setMsen_input_date(datausulan.getMste_beg_date());
					endors.setMsen_active_date(datausulan.getMste_beg_date());
				}
				if(mspo_provider==2){
					endors.setMsen_auto_rider(5);	//Endors Eka Sehat Admedika
				}else{
					endors.setMsen_auto_rider(1);	//Req Rudi(5 Nov 2009) : New Business dimajuin jadi 1(sebelumnya 0).
//					endors.setMsen_auto_rider(7);	
				}
				
				endors.setLspd_id(99);
				endors.setMsen_tgl_trans(commonDao.selectSysdate());
				endors.setLus_id(datausulan.getLus_id());
					
				if(products.powerSave(Integer.toString(datausulan.getLsbs_id()))){
					endors.setFlag_ps(1);
				}else if(products.stableLink(Integer.toString(datausulan.getLsbs_id()))){
					endors.setFlag_ps(2);
				}else if(products.stableSave(datausulan.getLsbs_id(), datausulan.getLsdbs_number()) || 
						(datausulan.getLsbs_id()==143 && (datausulan.getLsdbs_number()>=4 && datausulan.getLsdbs_number()<=6)) ||
						(datausulan.getLsbs_id()==158 && ((datausulan.getLsdbs_number()==13) || datausulan.getLsdbs_number()>=15 && datausulan.getLsdbs_number()<=16)) ||
						(datausulan.getLsbs_id()==144 && (datausulan.getLsdbs_number()==4))){
					endors.setFlag_ps(3);
				}else endors.setFlag_ps(0);
				if(lca_id.equals("09")){
					endors.setMsen_proses_bsm(1);
				}else{
					endors.setMsen_proses_bsm(0);
				}
				
				DetEndors detEndors=new DetEndors();
				detEndors.setMsen_endors_no(endors.getMsen_endors_no());
				detEndors.setMsenf_number(1);
				detEndors.setLsje_id(49);//PERUBAHAN RIDER lst_jn_endors
				detEndors.setMste_insured_no(1);
				if(datausulan.getLscb_pay_mode()==null){
					String lscb_pay_mode = "";
					if(datausulan.getLscb_id() == 1){
						lscb_pay_mode = "Triwulanan";
					}else if(datausulan.getLscb_id() == 2){
						lscb_pay_mode = "Semesteran";
					}else if(datausulan.getLscb_id() == 3){
						lscb_pay_mode = "Tahunan";
					}else if(datausulan.getLscb_id() == 6){
						lscb_pay_mode = "Bulanan";
					}else if(datausulan.getLscb_id() == 0){
						lscb_pay_mode = "Sekaligus";
					}
					datausulan.setLscb_pay_mode(lscb_pay_mode);
				}
				if(datausulan.getLku_symbol()==null){
					String lku_symbol = "";
					if(datausulan.getLku_id().equals("01")){
						lku_symbol = "Rp.";
					}else if(datausulan.getLku_id().equals("02")){
						lku_symbol = "US$";
					}
					datausulan.setLku_symbol(lku_symbol);
				}
				if(datausulan.getLsdbs_name()==null){
					String lsdbs_name = uwDao.selectNamaBusiness(datausulan.getLsbs_id(), datausulan.getLsdbs_number());
					datausulan.setLsdbs_name(lsdbs_name);
				}
				
				detEndors.setMsde_old1(datausulan.getLscb_pay_mode());
				detEndors.setMsde_old2(datausulan.getLsdbs_name());
				detEndors.setMsde_old3(datausulan.getLku_symbol());
				detEndors.setMsde_old4(FormatString.formatCurrency("", new BigDecimal(datausulan.getMspr_premium())));
				detEndors.setMsde_old5(FormatString.formatCurrency("", new BigDecimal(datausulan.getMspr_tsi())));
				detEndors.setMsde_new1(datausulan.getLscb_pay_mode());
				detEndors.setMsde_new2(datausulan.getLsdbs_name());
				detEndors.setMsde_new3(datausulan.getLku_symbol());
				detEndors.setMsde_new4(FormatString.formatCurrency("", new BigDecimal(datausulan.getMspr_premium())));
				detEndors.setMsde_new5(FormatString.formatCurrency("", new BigDecimal(datausulan.getMspr_tsi())));
				
				ProductInsEnd prod=new ProductInsEnd();
				prod.setMsen_endors_no(endors.getMsen_endors_no());
				prod.setReg_spaj(spaj);
				prod.setMste_insured_no(1);
				prod.setLsbs_id(v_intRiderId[i]);
				prod.setLsdbs_number(v_intRiderNo[i]);
				prod.setLku_id(datausulan.getLku_id());
				prod.setMspie_beg_date(datausulan.getMste_beg_date());
				prod.setMspie_end_date(FormatDate.add(FormatDate.add(prod.getMspie_beg_date(), Calendar.MONTH, 12),Calendar.DATE,-1));
				Double UP=0.0;
				if(products.unitLink(Integer.toString(datausulan.getLsbs_id()))){
					UP = new Double (datausulan.getMspr_tsi());
				}else{
					if(datausulan.getLku_id().equals("01")) {
						UP=new Double (20000000);
					}else if(datausulan.getLku_id().equals("02")) {
						UP=new Double (2000);
					}
				}
				prod.setMspie_tsi(UP);
				prod.setMspie_tsi_a(new Double(0));
				prod.setMspie_tsi_b(new Double(0));
				prod.setMspie_tsi_c(new Double(0));
				prod.setMspie_tsi_d(new Double(0));
				prod.setMspie_tsi_m(new Double(0));
				prod.setMspie_class(datausulan.getMspr_class());
				prod.setMspie_unit(datausulan.getMspr_unit());
				prod.setMspie_rate(new Double(0));
				prod.setMspie_persen(0);
				prod.setMspie_premium(new Double(0));
				prod.setMspie_discount(new Double(0));
				prod.setMspie_extra(datausulan.getMspr_extra());
				prod.setMspie_ins_period(1);
				prod.setLus_id(datausulan.getLus_id());
				prod.setLscb_id(datausulan.getLscb_id());
				prod.setLst_lsbs_id(null);
				prod.setLst_lsdbs_number(null);
				prod.setMspie_tsi_old(null);
				prod.setMspie_premium_old(null);
				prod.setMspie_disc_old(new Double(0));
				prod.setLsbs_id_old(null);
				prod.setLsdbs_num_old(null);
				prod.setMspie_rate_old(null);
				prod.setLscb_id_old(null);
				prod.setMspie_premium_prod(null);
				prod.setMspie_discount_prod(null);
				if(products.provider(Integer.toString(datausulan.getLsbs_id())) || products.unitLink(Integer.toString(datausulan.getLsbs_id())) || datausulan.getLsbs_id()==153){
					for (int j =0 ; j<jumlah_rider.intValue();j++){
						v_intRiderId[j] = rd.get(j).getLsbs_id();
						v_intRiderNo[j] = rd.get(j).getLsdbs_number();
						if(((v_intRiderId[j]==820 || v_intRiderId[j]==825) && v_intRiderNo[j]<16) ||
							(v_intRiderId[j]==823 && (v_intRiderNo[j]<16 || v_intRiderNo[j] == 211 || v_intRiderNo[j] == 212  )) ||
							(v_intRiderId[j]==826 && v_intRiderNo[j]<=12) ||
						  ((v_intRiderId[j]==832 || v_intRiderId[j]==833) && v_intRiderNo[j]<7) || v_intRiderId[j]==838){
							if(mspo_provider==2){
								cek_no_endors = uwDao.selectGetNoEndors(spaj);//buat cek no endors lagi jika mengambil hcp dan eka sehat
								if(cek_no_endors!=null){
									bacDao.deleteMstProdIns(endors.getMsen_endors_no());
									bacDao.deleteMstDetEndors(endors.getMsen_endors_no());
									bacDao.deleteMstEndors(endors.getMsen_endors_no());
									
									bacDao.insertMstEndors(endors);
									bacDao.insertMstDetEndors(detEndors);
									bacDao.insertMstProductInsEndAllProdLama(endors.getMsen_endors_no(), spaj, datausulan.getLus_id(), datausulan.getLscb_id(), datausulan.getMspr_tsi(), datausulan.getMspr_premium(), datausulan.getLsbs_id(), datausulan.getLsdbs_number());
								}else{
									bacDao.insertMstEndors(endors);
									bacDao.insertMstDetEndors(detEndors);
									bacDao.insertMstProductInsEndAllProdLama(endors.getMsen_endors_no(), spaj, datausulan.getLus_id(), datausulan.getLscb_id(), datausulan.getMspr_tsi(), datausulan.getMspr_premium(), datausulan.getLsbs_id(), datausulan.getLsdbs_number());
									//cek_no_endors=no_endors;
								}
							}
						}
					}
					// Andhika(21/03/2013)-SMiLe HOSPITAL PROTECTION (+)
					if((jumlah_rider == 0 && lsbs_id == 195) || (jumlah_rider == 0 && lsbs_id == 183) || (jumlah_rider == 0 && lsbs_id == 193) ||
						lsbs_id == 183 || lsbs_id == 193 || lsbs_id == 195 || lsbs_id == 189 || lsbs_id == 204 || endorse == 1){
						int x=0;
						if(jumlah_rider!=0){
							v_intRiderId[x] = rd_new.get(x).getLsbs_id();
							v_intRiderNo[x] = rd_new.get(x).getLsdbs_number();
						}
						if(mspo_provider==2){
							cek_no_endors = uwDao.selectGetNoEndors(spaj);
							if(cek_no_endors!=null){
								bacDao.deleteMstProdIns(endors.getMsen_endors_no());
								bacDao.deleteMstDetEndors(endors.getMsen_endors_no());
								bacDao.deleteMstEndors(endors.getMsen_endors_no());
								
								bacDao.insertMstEndors(endors);
								bacDao.insertMstDetEndors(detEndors);
								bacDao.insertMstProductInsEndAllProdLama(endors.getMsen_endors_no(), spaj, datausulan.getLus_id(), datausulan.getLscb_id(), datausulan.getMspr_tsi(), datausulan.getMspr_premium(), datausulan.getLsbs_id(), datausulan.getLsdbs_number());
							}else{
								bacDao.insertMstEndors(endors);
								bacDao.insertMstDetEndors(detEndors);
								bacDao.insertMstProductInsEndAllProdLama(endors.getMsen_endors_no(), spaj, datausulan.getLus_id(), datausulan.getLscb_id(), datausulan.getMspr_tsi(), datausulan.getMspr_premium(), datausulan.getLsbs_id(), datausulan.getLsdbs_number());
								//cek_no_endors=no_endors;
							}
						}
					}
				}
				if(products.stableLink(Integer.toString(datausulan.getLsbs_id()))){
					bacDao.updateMsen_Endors_NoSlink(endors.getMsen_endors_no(), spaj);
				}else if(products.stableSave(datausulan.getLsbs_id(), datausulan.getLsdbs_number())){
					bacDao.updateMsen_Endors_NoSsave(endors.getMsen_endors_no(), spaj);
				}
			}
		}
	}
	
	public CommandPowersaveUbah processCPU(CommandPowersaveUbah cpu){

		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		Map prodInsMap=selectProdInsured(cpu.reg_spaj);
		Integer insertFlag=0;
		Double ldec_awal=0.0;
		Double ldec_akhir=0.0;
		Double ldec_persen=0.0;
		
		Integer li_pro=0;
		for (int i = 1; i <= 4; i++) {
			if(i==1){
				cpu.powersaveUbah.setMpu_jenis(1);
				if(cpu.flagEditMGI==1){
					if(cpu.powersaveProses.mps_jangka_inv==cpu.mps_jangka_inv){
						insertFlag=0;
					}else{
						insertFlag=1;
					}
					ldec_awal=new Double(cpu.powersaveProses.mps_jangka_inv);
					ldec_akhir=new Double(cpu.mps_jangka_inv);
						
				}else{
					insertFlag=0;
				}
			}else if(i==2){
				cpu.powersaveUbah.setMpu_jenis(2);
				if(cpu.flagEditRO==1){
					if(cpu.powersaveProses.mps_roll_over==cpu.mps_roll_over){
						insertFlag=0;
					}else{
						insertFlag=1;
					}
					ldec_awal=new Double(cpu.powersaveProses.mps_roll_over);
					ldec_akhir=new Double(cpu.mps_roll_over);
				}else{
					insertFlag=0;
				}
			}else if(i==3){
				cpu.powersaveUbah.setMpu_jenis(3);
				if(cpu.flagEditRATE==1){
					if(cpu.powersaveProses.mps_rate==cpu.mps_rate){
//						hm(02/01/06)
						//insertFlag = 0;
						insertFlag=1;
					}else{
						insertFlag=1;
					}
					
					ldec_awal=cpu.powersaveProses.mps_rate;
					ldec_akhir=cpu.mps_rate;
					
					Double ls_persen=0.0;
					if(prodInsMap.get("LKU_ID").equals("01")){
						ls_persen=new Double(props.getProperty("RatePwrSave01"));
					}else{
						ls_persen=new Double(props.getProperty("RatePwrSave02"));
					}
					
					ldec_persen = ls_persen / 100;
					
					if(ldec_akhir.doubleValue()>ldec_persen.doubleValue()){
						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
						cpu.messageError="Gagal Proses Data. Untuk " + prodInsMap.get("LKU_SYMBOL") + " Maximal " + FormatString.formatCurrency("", new BigDecimal(ldec_persen)) + "%.";
						return cpu;
					}
					
					
				}else{
					insertFlag=0;
				}
				
			}else if(i==4){
				cpu.powersaveUbah.setMpu_jenis(5);
				if(cpu.flagStopRO==1){
					insertFlag=1;
					ldec_awal=null;
					ldec_akhir=null;
				}else{
					insertFlag=0;
				}
			}
			
			if(insertFlag==1){
				
				
				cpu.powersaveUbah.setReg_spaj(cpu.reg_spaj);
				cpu.powersaveUbah.setMpu_tgl_awal(cpu.powersaveProses.mps_batas_date);
				cpu.powersaveUbah.setMpu_awal(ldec_awal);
				cpu.powersaveUbah.setMpu_akhir(ldec_akhir);
				
				if(!insertPwrUbah(cpu.powersaveUbah)){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					cpu.messageError="Perubahan "+(cpu.powersaveUbah.getMpu_jenis()==1?"MGI":cpu.powersaveUbah.getMpu_jenis()==2?"Roll Over":cpu.powersaveUbah.getMpu_jenis()==3?"Rate":"Stop Roll Over")+" untuk Beg Date Next MGI "+new SimpleDateFormat("dd/MM/yyyy").format(cpu.powersaveProses.mps_batas_date)+" sudah tidak dapat dirubah kembali.";
					
					return cpu;				
				}
				
				Integer li_count=selectCountPwrRO(cpu.reg_spaj,cpu.powersaveProses.mps_batas_date);
				
				if(li_count>0){
					if(i==2){
						if(cpu.flagEditRO==1){
							if(cpu.powersaveProses.mps_roll_over!=cpu.mps_roll_over){
								
								if(!updateJnsRO_PwrRo(cpu.reg_spaj, cpu.mps_roll_over, cpu.powersaveProses.mps_batas_date)){
									TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
									cpu.messageError="Gagal Proses Data. Error update Powersave Roll Over !!!";
									return cpu;		
								}
								
								if(!updateJnsRO_PwrPro(cpu.reg_spaj, cpu.mps_roll_over, cpu.powersaveProses.mps_batas_date)){
									TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
									cpu.messageError="Gagal Proses Data. Error update Powersave Proses !!!";
									return cpu;		
								}
							}
						}
					}
				}
			}
			li_pro=li_pro+insertFlag;
		}
		
		if(li_pro>0){
			cpu.messageError="Proses Berhasil.";
		}else{
			cpu.messageError="Tidak ada perubahan";
		}
		
		return cpu;
	}
	
	public List<PowersaveUbah> selectUbahView(String spaj){
		return bacDao.selectUbahView(spaj);
	}
	
	
	
	public void insertTaxCurrency(Map param) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		commonDao.insertTaxCurrency(param);
	}
	
	/*======================END Ubah Powersave ====================================*/
	
	public List selectPincab(){
		return uwDao.selectPincab();
	}	
	
	public String selectEmailPincab_Ao(String kode) {
		return uwDao.selectEmailPincab_Ao(kode);
	}
	
	public List<Map> selectEmailPincab(String kode) {
		return uwDao.selectEmailPincab(kode);
	}	
	
	public Integer getRowSs() {
		return uwDao.getRowSs();
	}
	
	public Integer selectVersionViewMedis(String spaj) {
		return uwDao.selectVersionViewMedis(spaj);
	}
	public List<Map> selectSpajExpire() {
		return uwDao.selectSpajExpire();
	}
	
	public List selectDaftarAkseptasiNyangkutTemp(int lssa_id, boolean isEmailRequired,String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12, String lastMonthLastDay) {
		return this.uwDao.selectDaftarAkseptasiNyangkutTemp(lssa_id, isEmailRequired, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,lastMonthLastDay);
	}
	
	public List<Map> selectTahunFiling() {
		return uwDao.selectTahunFiling();
	}
	
	public List<Map> selectBulanFiling(String tahun) {
		return uwDao.selectBulanFiling(tahun);
	}	
	
	public List selectViewStableLinkTopUp(String spaj, Integer tu_ke){
		return uwDao.selectViewStableLinkTopUp(spaj, tu_ke);
	}
	
	public List selectViewPSaveTopUp(String spaj, Integer tu_ke){
		return uwDao.selectViewPSaveTopUp(spaj, tu_ke);
	}
	
	public Integer selectCheckSpajInMstSLink( String spaj ){
		return uwDao.selectCheckSpajInMstSLink( spaj );
	}
	
	public Integer selectCheckSpajInMstSLinkBasedFlagBulanan( String spaj ){
		return uwDao.selectCheckSpajInMstSLinkBasedFlagBulanan( spaj );
	}
	
	public Integer selectCheckSpajInMstPsave( String spaj ){
		return uwDao.selectCheckSpajInMstPsave( spaj );
	}
	
	public Integer selectCheckSpajInMstPsaveBasedFlagBulanan( String spaj ){
		return uwDao.selectCheckSpajInMstPsaveBasedFlagBulanan( spaj );
	}

	public Integer selectCheckSpajInLstUlangan( String spaj ){
		return uwDao.selectCheckSpajInLstUlangan( spaj );
	}
	
	public List selectForPrintEndorsemenLcaId9( String spaj, Integer tuKe ){
		return uwDao.selectForPrintEndorsemenLcaId9( spaj, tuKe );
	}
	
	public List selectForPrintEndorsemenNotLcaId9( String spaj, Integer tuKe ){
		return uwDao.selectForPrintEndorsemenNotLcaId9( spaj, tuKe );
	}
	
	public String selectLcaIdMstPolicyBasedSpaj( String spaj ){
		return uwDao.selectLcaIdMstPolicyBasedSpaj( spaj );
	}
	
	public void updatePrintStableLinkTopup(String spaj, Integer tu_ke){
		uwDao.updatePrintStableLinkTopup(spaj, tu_ke);
	}
	
	public void updatePrintPSaveTopup(String spaj, Integer tu_ke){
		uwDao.updatePrintPSaveTopup(spaj, tu_ke);
	}
	
	public List<Map> selectBiiNonSelfGen(String spaj){
		return uwDao.selectBiiNonSelfGen(spaj);
	}
	
	public int updateSelfIns(Integer mspo_self_ins, String spaj){
		return uwDao.updateSelfIns(mspo_self_ins, spaj);
	}
	
	public Integer selectNoTranSLink(String no_trx,Integer tu_ke) {
		return uwDao.selectNoTranSLink(no_trx,tu_ke);
	}
	
	public HashMap selectCabBsm(String kode){
		return uwDao.selectCabBsm(kode);
	}
	
	public List selectDetailBisnis(String spaj) {
		return uwDao.selectDetailBisnis(spaj);
	}
	
	public Map selectF_check_posisi(String spaj){
		return (HashMap)uwDao.selectF_check_posisi(spaj);
	}
	
	public Map selectTertanggung(String spaj){
		return this.uwDao.selectTertanggung(spaj);
	}
	
	public Map selectWfGetStatus(String spaj,Integer insured){
		return uwDao.selectWfGetStatus(spaj,insured);
	}
	
	public Policy selectDw1Underwriting(String spaj,Integer lspdId,Integer lstbId){
		return uwDao.selectDw1Underwriting(spaj,lspdId,lstbId);
	}
	
	public Map prosesReasIndividuNew(Reas dataReas,BindException err)throws Exception{
		//
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		dataReas.setLstbId(1);
		dataReas.setLspdId(2);
//		return reasUwNew2.prosesReasUnderwriting(dataReas, err);
		return reasIndividu.prosesReasUnderwriting(dataReas, err);
	}
	
	public Integer selectWorkDays(Date beg_date, Date end_date){
		return commonDao.selectWorkDays(beg_date, end_date);
	}
	
	
	
	public List<Map> prosesMultiTrfPaymentPas(HttpServletRequest request, String jenis_pas, List<Pas> agenList, User user)
	{
		List<Map> successMessageList = new ArrayList<Map>();
		BacDao elionsManager = null;
		String id_ref="";
		String nama_pp="";
		String msp_fire_idX="";
		int jml_err = 0;
		try{
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sts", "SUCCEED");
			map.put("msg", "** Jumlah data "+ jenis_pas +" diproses: "+agenList.size());
			map.put("noSpaj", null);
			successMessageList.add(map);
	
		for(Pas pasx : agenList){
	
			// Proses Insert Data PAS 
			if(jenis_pas.equals("BP SMS")){
				pasx = this.updateBPSMS(pasx);
				
			} else{	
				pasx = this.insertPas(pasx, user);
			}						
			
		
			map = new HashMap<String, Object>();
			String msp_fire_id = pasx.getMsp_fire_id();
			Integer lus_id = Integer.parseInt(user.getLus_id());
			
			String msp_id =  uwDao.selectMspIdFromMspFireId(msp_fire_id);
			List<Pas> pas = uwDao.selectAllPasList(msp_id, "1", null, null, null, "pas", null, "pas_aksep",null,null,null);
			Pas p = pas.get(0); 			
							
				//
				id_ref = pasx.getId_ref();
				nama_pp = p.getMsp_pas_nama_pp();
				msp_fire_idX = msp_fire_id;
				//
				
				p.setTm_id(pasx.getTm_id());
				p.setSpv_id(pasx.getSpv_id());
				// Proses Akseptasi dan Transfer ke Payment
				p.setLus_id_uw_pas(lus_id);
				p.setLus_login_name(user.getLus_full_name());
				p.setMsp_pas_accept_date(commonDao.selectSysdate());
				p.setMsp_flag_aksep(1);
				p.setLspd_id(2);
				p.setLssp_id(10);
				p.setLssa_id(5);//aksep otomatis
				p.setMsp_kode_sts_sms("00");
				p = updatePas(p);
				
				 // Process TRF to PAYMENT
				Cmdeditbac edit = new Cmdeditbac();
				//p.setLus_id(lus_id);
				p.setLus_id_uw_pas(lus_id);
				p.setLus_login_name(user.getLus_full_name());
				//langsung set ke posisi payment
				p.setLspd_id(4);
				p.setCif(pasx.getCif());
				p.setNo_va(pasx.getNo_va());
				p.setFlag_kpr(pasx.getFlag_kpr()); //tambah kolom baru. helpdesk[132502] //chandra
				
				//randy
				if(jenis_pas.equals("SMART ACCIDENT CARE")){
					p.setCf_campaign_code(pasx.getCf_campaign_code());
					p.setCf_customer_id(pasx.getCf_customer_id());
					p.setCf_job_code(pasx.getCf_job_code());
					if(p.getMsp_flag_cc()==1 || p.getMsp_flag_cc()==2){
						p.setLspd_id(118);
					}
				}
				BindException errors = new BindException(new HashMap(), "cmd");
				//BindException errors;
				//binding data
				//errors = new BindException(bindAndValidate(request, cmd, false));				
				    
					edit=this.prosesPas(request, "update", p, errors,"input",user,errors);		
								
					if(!jenis_pas.equals("PAS SYARIAH")){
						if(!(jenis_pas.equals("SMART ACCIDENT CARE") || jenis_pas.equals("NISSAN PA") || jenis_pas.equals("NISSAN DBD"))){
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
									//copyfile("\\ebserver\\pdfind\\Polis\\fire\\"+d2.getKey(), "\\ebserver\\pdfind\\Polis\\"+edit.getPemegang().getLca_id()+"\\"+d2.getKey().replace(p.getMsp_fire_id(), "SPAJ"));
									//deleteFile("\\ebserver\\pdfind\\Polis\\fire\\", d2.getKey(), null);
									String srFile = "\\ebserver\\pdfind\\Polis\\fire\\"+d2.getKey();
									String dtFile =  "\\ebserver\\pdfind\\Polis\\"+edit.getPemegang().getLca_id()+"\\"+d2.getKey().replace(p.getMsp_fire_id(), "SPAJ");
									InputStream in = null;
									OutputStream out = null;
									try{
										File f1 = new File(srFile);
										File f2 = new File(dtFile);
										in = new FileInputStream(f1);

										//For Append the file.
										//							      OutputStream out = new FileOutputStream(f2,true);

										//For Overwrite the file.
										out = new FileOutputStream(f2);

										byte[] buf = new byte[1024];
										int len;
										while ((len = in.read(buf)) > 0){
											out.write(buf, 0, len);
										}
										

									}
									catch(FileNotFoundException ex){
										jml_err++;
										map.put("sts", "FAILED");
										map.put("msg", "FAILED (TRANSFER TO PAYMENT):: No."+pasx.getId_ref().trim()+" FireId: "+p.getMsp_fire_id()+"(PAS PP: "+p.getMsp_pas_nama_pp()+" ) : Proses TRF to PAYMENT Gagal, File Polis tidak ditemukan!");							
										map.put("noSpaj", null);
										successMessageList.add(map);
										logger.error("ERROR :", ex);
									}
									catch(IOException e){
										jml_err++;
										map.put("sts", "FAILED");
										map.put("msg", "FAILED (TRANSFER TO PAYMENT):: No."+pasx.getId_ref().trim()+" FireId: "+p.getMsp_fire_id()+"(PAS PP: "+p.getMsp_pas_nama_pp()+" ) : Proses TRF to PAYMENT Gagal, Tidak berhasil cetak File Polis!");							
										map.put("noSpaj", null);
										successMessageList.add(map);
									}finally {
										try {
											if(in!=null){
												in.close();
											}
											if(out!=null){
												out.close();
											}
										}catch (Exception e) {
											logger.error("ERROR :", e);
										}
									}
									String destDir = "\\ebserver\\pdfind\\Polis\\fire\\";
									String fileName = d2.getKey();

									File file = new File(destDir + "/" + fileName);
									if (file.exists()) file.delete();

								}

							}	
						}
					}
				
				if(edit.getPemegang().getMspo_policy_no() == null){		
					jml_err++;
					 map.put("sts", "FAILED");
					 map.put("msg", "FAILED (TRANSFER TO PAYMENT):: No."+pasx.getId_ref().trim()+" FireId: "+p.getMsp_fire_id()+"(PAS PP: "+p.getMsp_pas_nama_pp()+" ) : Proses TRF to PAYMENT Gagal, No.Polis tidak terbentuk!");							
					 map.put("noSpaj", null);
					 successMessageList.add(map);					
				}else{
					//request.setAttribute("successMessage","transfer sukses dengan SPAJ : "+edit.getPemegang().getReg_spaj()+" dan No. Polis : "+edit.getPemegang().getMspo_policy_no());					
					map.put("sts", "SUCCEED");					
					map.put("msg", "Transfer ke payment Sukses:: No."+" FireId: "+p.getMsp_fire_id()+"(PAS PP: "+p.getMsp_pas_nama_pp()+ " ) dengan SPAJ : "+edit.getPemegang().getReg_spaj()+" dan No. Polis : "+edit.getPemegang().getMspo_policy_no() );									
					map.put("noSpaj", edit.getPemegang().getReg_spaj());
					successMessageList.add(map);					
				}
				
		}
		
		}	
	   catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sts", "FAILED");
			map.put("msg", "FAILED (TRANSFER TO PAYMENT) : Proses TRF to PAYMENT Di-Batalkan, karena ada Error!!  Mohon cek kembali Validitas Data!");							
			successMessageList.add(map);
		}
		
		if(jml_err > 0){
			// TODO Auto-generated catch block
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sts", "FAILED");
			map.put("msg", "FAILED (TRANSFER TO PAYMENT) : Semua data Transfer dibatalkan (Rollback-Proses), karena ada beberapa data Error!!  Mohon cek kembali Validitas Data!");							
			successMessageList.add(map);
		}
		
		return successMessageList;
	}
	
	public Cmdeditbac prosesPas(HttpServletRequest request, String jenis_proses, Pas pas, Errors err,String keterangan, User currentUser, BindException errors) throws ServletException,IOException,Exception
	{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		Cmdeditbac edit= new Cmdeditbac();
		int umurPp=0;
		try{
			Integer lus_id = Integer.parseInt(currentUser.getLus_id());
		//	if(request.getParameter("kata")!=null){
				Date beg_date = pas.getMsp_pas_beg_date();//commonDao.selectSysdateTrunc();
				Date end_date = pas.getMsp_pas_end_date();//commonDao.selectSysdateTrunc();
//				Date end_fire_date = commonDao.selectSysdateTrunc();
//				end_date.setYear(end_date.getYear()+1);
//				end_date.setDate(end_date.getDate()-1);
				//pas.setMsp_pas_beg_date(beg_date);
				//pas.setMsp_pas_end_date(end_date);
//				pas.setMsp_fire_beg_date(beg_date);
//				end_fire_date.setMonth(beg_date.getMonth()+6);
//				end_fire_date.setDate(end_fire_date.getDate()-1);
//				pas.setMsp_fire_end_date(end_fire_date);
				pas.setLus_id(lus_id);
				pas.setLus_login_name(currentUser.getLus_full_name());
				pas.setDist("05");
//				pas.setLspd_id(99);//FILLING
				pas.setLspd_id(4);
				
				//tgl beg date
				int tanggal1= beg_date.getDate();
				int bulan1 = beg_date.getMonth()+1;
				int tahun1 = beg_date.getYear()+1900;
				
				//tgl lahir ttg
				int tanggal2=pas.getMsp_date_of_birth().getDate();
				int bulan2=pas.getMsp_date_of_birth().getMonth()+1;
				int tahun2=pas.getMsp_date_of_birth().getYear()+1900;
				
				//tgl lahir pp
				int tanggal3=pas.getMsp_pas_dob_pp().getDate();
				int bulan3=pas.getMsp_pas_dob_pp().getMonth()+1;
				int tahun3=pas.getMsp_pas_dob_pp().getYear()+1900;
				
				//hit umur ttg, pp, pic
				f_hit_umur umr= new f_hit_umur();
				int umur =  umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
				umurPp =  umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
				
				pas.setMsp_age(umur);
				
	//		}
			
			CheckSum checkSum = new CheckSum();
			edit.setPemegang(new Pemegang());
			edit.setPersonal(new Personal());
			edit.setContactPerson(new ContactPerson());
			edit.setTertanggung(new Tertanggung());
			edit.setDatausulan(new Datausulan());
			edit.setAgen(new Agen());
			edit.setAddressbilling(new AddressBilling());
			edit.setRekening_client(new Rekening_client());
			edit.setAccount_recur(new Account_recur());
			
			// agen
			Map agentMap = bacDao.selectagenpenutup(pas.getMsag_id());
			String regionid = (String) agentMap.get("REGIONID");
			edit.getAgen().setMsag_id((String) agentMap.get("ID"));
			edit.getAgen().setMcl_first((String) agentMap.get("NAMA"));
			edit.getAgen().setKode_regional(regionid);
			edit.getAgen().setLca_id(regionid.substring(0, 2));
			edit.getAgen().setLwk_id(regionid.substring(2, 4));
			edit.getAgen().setLsrg_id(regionid.substring(4, 6));
			if(edit.getAgen().getLca_id() == "58"){
				edit.getAgen().setLrb_id(pas.getLrb_id().toString());
			}
			//referensi(tambang emas) agar jenis_ref tidak null
			edit.getAgen().setJenis_ref(2);
			
			edit.getContactPerson().setNama_lengkap("");
			
			//untuk produk Bank DMTM BTN Smart Accident Care set Provider otomatis - Ridhaal
//			if(edit.getAgen().getLca_id().equals("40") && edit.getAgen().getLwk_id().equals("01") && edit.getAgen().getLsrg_id().equals("04") 
//				&& "SMART ACCIDENT CARE".equals(pas.getJenis_pas())){
//				pas.setMspo_plan_provider("2");
//			}

			// pemegang
			edit.getPemegang().setMspo_plan_provider(pas.getMspo_plan_provider());
			edit.getPemegang().setNo_hp(pas.getMsp_mobile());
			edit.getPemegang().setNo_hp2(pas.getMsp_mobile2());
			edit.getPemegang().setMcl_first(pas.getMsp_pas_nama_pp());
			edit.getPemegang().setMspe_no_identity(pas.getMsp_identity_no());
			edit.getPemegang().setMspe_date_birth(pas.getMsp_pas_dob_pp());
			edit.getPemegang().setMspe_email(pas.getMsp_pas_email());
			edit.getPemegang().setEmail(pas.getMsp_pas_email());
			edit.getPemegang().setAlamat_rumah(pas.getMsp_address_1());
			edit.getPemegang().setKota_rumah(pas.getMsp_city());
			edit.getPemegang().setKd_pos_rumah(pas.getMsp_postal_code());
			edit.getPemegang().setMkl_red_flag(0);
			edit.getPemegang().setApplication_id(pas.getApplication_id());
			edit.getPemegang().setMspo_no_kerjasama(pas.getTm_id());
			edit.getPemegang().setSpv(pas.getSpv_id());
			edit.getPemegang().setFlag_kpr(pas.getFlag_kpr()); //tambah kolom baru. helpdesk[132502] //chandra
			
			//randy
			edit.getPemegang().setCf_job_code(pas.getCf_job_code());
			edit.getPemegang().setCf_customer_id(pas.getCf_customer_id());
			edit.getPemegang().setCf_campaign_code(pas.getCf_campaign_code());
			edit.getPemegang().setMspo_nasabah_dcif(pas.getCif());
			
			
//			if(("AP/BP,DBD BP,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
//				edit.getPemegang().setMspo_ao(pas.getMsag_id_pp());
//			}else{
//				edit.getPemegang().setMspo_ao(pas.getKode_ao());
//			}
			if(("AP/BP,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
				edit.getPemegang().setMspo_ao(pas.getMsag_id_pp());
			}else if(("DBD BP").indexOf(pas.getJenis_pas()) > -1){
				if (pas.getMsag_id_pp() != null){
					edit.getPemegang().setMspo_ao(pas.getMsag_id_pp());
				}else{
					edit.getPemegang().setMspo_ao(pas.getMsag_id());
				}
			}else{
				edit.getPemegang().setMspo_ao(pas.getMsag_id());
			}
//			else{
//				edit.getPemegang().setMspo_ao(pas.getKode_ao());
//			}
			edit.getPemegang().setMspo_pribadi(pas.getPribadi());
			edit.getPemegang().setMspe_sex(pas.getMsp_sex_pp());
			edit.getPemegang().setReg_spaj(pas.getReg_spaj());
			edit.getPemegang().setMspo_policy_no(pas.getMspo_policy_no());
			edit.getPemegang().setMspo_spaj_date(commonDao.selectSysdate());
			edit.getPemegang().setMste_tgl_recur(pas.getMsp_tgl_debet());
			edit.getPemegang().setLus_id(pas.getLus_id());
			edit.getPemegang().setMspo_age(umurPp);
			edit.getPemegang().setUsiapp(umurPp);
			edit.getPemegang().setMspe_place_birth(pas.getMsp_pas_tmp_lhr_pp());
			edit.getPemegang().setArea_code_rumah(pas.getMsp_area_code_rumah());
			if(pas.getLside_id() != null){
				edit.getPemegang().setLside_id(pas.getLside_id());
			}
			edit.getPemegang().setTelpon_rumah(pas.getMsp_pas_phone_number());
			edit.getPemegang().setLsre_id(pas.getLsre_id());
			edit.getPemegang().setMkl_tujuan("");
			edit.getPemegang().setTujuana("");
			edit.getPemegang().setMkl_pendanaan("");
			edit.getPemegang().setDanaa("");
			edit.getPemegang().setMkl_smbr_penghasilan("");
			edit.getPemegang().setShasil("");
			edit.getPemegang().setDanaa2("");
			edit.getPemegang().setMkl_kerja("");
			edit.getPemegang().setKerjaa("");
			edit.getPemegang().setKerjab("");
			edit.getPemegang().setMkl_industri("");
			edit.getPemegang().setIndustria("");
			edit.getPemegang().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
			edit.getPemegang().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
			edit.getPemegang().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
			
			if(pas.getMsag_id_pp() == null)pas.setMsag_id_pp("");
			if(("AP/BP,AP/BP ONLINE,DBD AGENCY,DBD SYARIAH,DBD BP, DBD MALL,PAS BPD,BP SMS,BP CARD").indexOf(pas.getJenis_pas()) > -1){
				edit.getPemegang().setLsne_id(pas.getMsp_warga());
				edit.getPemegang().setMspe_sts_mrt(pas.getMsp_status_nikah().toString());
				edit.getPemegang().setLsag_id(pas.getLsag_id());
				edit.getPemegang().setMcl_agama(pas.getMsp_agama());
				edit.getPemegang().setLsed_id(pas.getMsp_pendidikan());
				edit.getPemegang().setMkl_kerja(pas.getMsp_occupation());
				if(pas.getMsp_occupation_else() == null)pas.setMsp_occupation_else("");
				if(pas.getMsp_occupation_else() == null){
					edit.getPemegang().setKerjaa("");
				}else{
					edit.getPemegang().setKerjaa(pas.getMsp_occupation_else());
				}
				
				if(("AP/BP,AP/BP ONLINE,BP SMS,BP CARD").indexOf(pas.getJenis_pas()) > -1){
					if(pas.getMsp_company_jabatan() == null)pas.setMsp_company_jabatan("");
					edit.getPemegang().setKerjab(pas.getMsp_company_jabatan());
					edit.setPersonal(new Personal());
					//set mcl id terbaru ada dibawah
					//edit.getPersonal().setMcl_id(mcl_id);
					edit.getPersonal().setMcl_first(pas.getMsp_company_name());
//					edit.getPersonal().setLju_id(pas.getMsp_company_usaha());
					if(pas.getLju_id() != null){
						edit.getPersonal().setLju_id(pas.getLju_id());
					}
					edit.getPemegang().setAlamat_kantor(pas.getMsp_company_address());
					edit.getPemegang().setKd_pos_kantor(pas.getMsp_company_postal_code());
					edit.getPemegang().setTelpon_kantor(pas.getMsp_company_phone_number());
				}
				
			}
			
			// address billing
			edit.getAddressbilling().setNo_hp(pas.getMsp_mobile());
			edit.getAddressbilling().setNo_hp2(pas.getMsp_mobile2());
			edit.getAddressbilling().setMsap_contact(pas.getMsp_full_name());
			edit.getAddressbilling().setMsap_address(pas.getMsp_address_1());
			edit.getAddressbilling().setMsap_area_code1(pas.getMsp_area_code_rumah());
			edit.getAddressbilling().setKota(pas.getMsp_city());
			edit.getAddressbilling().setMsap_zip_code(pas.getMsp_postal_code());
			edit.getAddressbilling().setE_mail(pas.getMsp_pas_email());
			edit.getAddressbilling().setReg_spaj(pas.getReg_spaj());
			edit.getAddressbilling().setRegion(regionid);
			edit.getAddressbilling().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
			edit.getAddressbilling().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
			edit.getAddressbilling().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
			
			// rekening client
			if("DBD BP".equals(pas.getJenis_pas())){
				edit.getRekening_client().setNotes("DBD BP");
			}else if("DBD AGENCY".equals(pas.getJenis_pas())){
				edit.getRekening_client().setNotes("DBD AGENCY");
			}else if("DBD SYARIAH".equals(pas.getJenis_pas())){
				edit.getRekening_client().setNotes("DBD SYARIAH");
			}else if("DBD MALL".equals(pas.getJenis_pas())){
				edit.getRekening_client().setNotes("DBD MALL");
			}else if(("INDIVIDU,AP/BP,AP/BP ONLINE,MALLINSURANCE,PAS BPD,BP SMS,BP CARD,PAS SYARIAH,SMART ACCIDENT CARE").indexOf(pas.getJenis_pas()) > -1){
				edit.getRekening_client().setNotes("PAS");
			}else if(("NISSAN PA").indexOf(pas.getJenis_pas()) > -1){
				edit.getRekening_client().setNotes("NISSAN PA");
			}else if(("NISSAN DBD").indexOf(pas.getJenis_pas()) > -1){
				edit.getRekening_client().setNotes("NISSAN DBD");
			}else{//simple bisnis
				edit.getRekening_client().setNotes(pas.getJenis_pas());
			}
			//edit.getRekening_client().setNotes("PAS");
			// tabungan ataupun kartu kredit, kedua rekening harus diisi & untuk tunai, rekening tabungan harus diisi
			
			if("".equals(pas.getLsbp_id()) || "0".equals(pas.getLsbp_id()) || pas.getLsbp_id() == null){
				pas.setLsbp_id("0");
				pas.setMsp_no_rekening("-");
				pas.setMsp_rek_cabang("-");
				pas.setMsp_rek_kota("-");
				pas.setMsp_rek_nama("-");
			}
			
			edit.getRekening_client().setLsbp_id(pas.getLsbp_id());
			//if(pas.getMsp_flag_cc() == 2){//tabungan
				if(pas.getMsp_rek_nama() == null)edit.getRekening_client().setMrc_nama("-");
				else edit.getRekening_client().setMrc_nama(pas.getMsp_rek_nama().toUpperCase());
				if(pas.getMsp_rek_cabang() == null)edit.getRekening_client().setMrc_cabang("-");
				else edit.getRekening_client().setMrc_cabang(pas.getMsp_rek_cabang().toUpperCase());
				if(pas.getMsp_rek_kota() == null)edit.getRekening_client().setMrc_kota("-");
				else edit.getRekening_client().setMrc_kota(pas.getMsp_rek_kota().toUpperCase());
				
				
			//}
			edit.getRekening_client().setMrc_jn_nasabah(0);//none
			edit.getRekening_client().setMrc_kurs("01");//rupiah
			edit.getRekening_client().setNo_account(pas.getMsp_no_rekening());
			edit.getRekening_client().setMrc_no_ac(pas.getMsp_no_rekening());
			edit.getRekening_client().setMrc_no_ac_lama(pas.getMsp_no_rekening());
			edit.getRekening_client().setMrc_jenis(2);// rek client
			
			// account recur
			edit.getAccount_recur().setLbn_id(pas.getLsbp_id_autodebet());
			edit.getAccount_recur().setLus_id(pas.getLus_id());
			edit.getAccount_recur().setMar_number(1);
			edit.getAccount_recur().setMar_acc_no(pas.getMsp_no_rekening_autodebet());
			edit.getAccount_recur().setMar_holder(pas.getMsp_rek_nama_autodebet());
			edit.getAccount_recur().setMar_expired(pas.getMsp_tgl_valid());
			edit.getAccount_recur().setMar_active(1);
			if( ("SMART ACCIDENT CARE".equals(pas.getJenis_pas()) 
					|| "NISSAN PA".equals(pas.getJenis_pas()) 
					|| "NISSAN DBD".equals(pas.getJenis_pas()) 
					)
					&& (pas.getMsp_flag_cc()==1 || pas.getMsp_flag_cc()==2)){ //randy (belum ditambah kalau dia pilihnya flag_cc = 1. idambil dr eccel)
				edit.getAccount_recur().setFlag_autodebet_nb(1);
			}
			
			// data usulan
//			String lsdbs_number = uwDao.selectCekPin(pas.getPin(), 1);
//			if(lsdbs_number == null)lsdbs_number = "x";
			String lsdbs_number = "x";
			/*if(pas.getPin() != null){
				lsdbs_number = uwDao.selectCekPin(pas.getPin(), 1);
			}else{
				lsdbs_number = uwDao.selectCekNoKartu(pas.getNo_kartu(), "", 1);
			}*/
			
			if(pas.getNo_kartu() != null){
			    // Cek kartu baru
			    String old_card = uwDao.selectCekNoKartu(pas.getNo_kartu(), "PAS", 1);
			    String new_card = uwDao.selectCekNoKartu(pas.getNo_kartu(), "CARD", 1);
			    if(old_card == null && "05".equals(new_card)) {
			        pas.setNew_card(1);
			        lsdbs_number = (pas.getProduk()<10 ? "0"+pas.getProduk().toString() : pas.getProduk().toString());
			    } else {
	                lsdbs_number = uwDao.selectCekNoKartu(pas.getNo_kartu(), "", 1);
			    }
			}else{
				lsdbs_number = uwDao.selectCekPin(pas.getPin(), 1);
			}
			
			if(("AP/BP,AP/BP ONLINE,BP SMS,BP CARD").indexOf(pas.getJenis_pas()) > -1){
				lsdbs_number = "05";
			}else if("PAS BPD".equals(pas.getJenis_pas())){
				lsdbs_number = "06";
			}
//			if(!"".equals(pas.getMsag_id_pp())){
//				if(pas.getProduk() == 5){
//				  lsdbs_number = "05";
//				} else {
//			      lsdbs_number = "06";
//				}
//			}
			
			if("DBD BP".equals(pas.getJenis_pas())){
				lsdbs_number = "01";
			}else if("DBD AGENCY".equals(pas.getJenis_pas())){
				lsdbs_number = "02";
			}else if("DBD SYARIAH".equals(pas.getJenis_pas())){
				lsdbs_number = "01";
			}else if("DBD MALL".equals(pas.getJenis_pas())){
				lsdbs_number = "03";
			}
			int mspo_pay_period = 1;
		
			Double mspr_tsi = null;
			Double mspr_premium = null;
			
			if("DBD BP".equals(pas.getJenis_pas()) || "DBD AGENCY".equals(pas.getJenis_pas()) || "DBD SYARIAH".equals(pas.getJenis_pas())){
				if("50000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(2000000);
				}else if("100000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(4000000);
				}else if("150000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(6000000);
				}else if("200000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(8000000);
				}else if("250000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(10000000);
				}else{
					mspr_tsi = new Double(0);
				}
				mspr_premium = new Double(pas.getMsp_premi());
			}else if("DBD MALL".equals(pas.getJenis_pas()) ){
				if("100000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(2000000);
				}else{
					mspr_tsi = new Double(0);
				}
				mspr_premium = new Double(pas.getMsp_premi());
			}else if(("INDIVIDU,AP/BP,AP/BP ONLINE,MALLINSURANCE,PAS BPD,BP SMS,BP CARD").indexOf(pas.getJenis_pas()) > -1){
				//kode plan 187
				if("01".equals(lsdbs_number)){
					mspr_tsi = new Double(100000000);
				}else if("02".equals(lsdbs_number)){
					mspr_tsi = new Double(50000000);
				}else if("03".equals(lsdbs_number)){
					mspr_tsi = new Double(100000000);
				}else if("04".equals(lsdbs_number)){
					mspr_tsi = new Double(200000000);
				}else if("05".equals(lsdbs_number)){
					mspr_tsi = new Double(50000000);
				}else if("06".equals(lsdbs_number)){
					mspr_tsi = new Double(50000000);
				}else{
					mspr_tsi = new Double(100000000);
				}
				if("01".equals(lsdbs_number) && pas.getLscb_id() == 3){
					mspr_premium = new Double(150000);
				}else if("01".equals(lsdbs_number) && pas.getLscb_id() == 6){
					mspr_premium = new Double(15000);
				}else if("01".equals(lsdbs_number) && pas.getLscb_id() == 1){
					mspr_premium = new Double(150000 * 0.27);
				}else if("01".equals(lsdbs_number) && pas.getLscb_id() == 2){
					mspr_premium = new Double(150000 * 0.525);
				}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 3){
					mspr_premium = new Double(300000);
				}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 6){
					mspr_premium = new Double(30000);
				}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 1){
					mspr_premium = new Double(300000 * 0.27);
				}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 2){
					mspr_premium = new Double(300000 * 0.525);
				}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 3){
					mspr_premium = new Double(500000);
				}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 6){
					mspr_premium = new Double(50000);
				}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 1){
					mspr_premium = new Double(500000 * 0.27);
				}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 2){
					mspr_premium = new Double(500000 * 0.525);
				}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 3){
					mspr_premium = new Double(900000);
				}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 6){
					mspr_premium = new Double(90000);
				}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 1){
					mspr_premium = new Double(900000 * 0.27);
				}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 2){
					mspr_premium = new Double(900000 * 0.525);
				}else if("05".equals(lsdbs_number)){
					mspr_premium = new Double(74000);
				}else if("06".equals(lsdbs_number)){
					mspr_premium = new Double(74000);
				} else{
					mspr_premium = new Double(0);
				}
				
				// New card
				if(pas.getNew_card() == 1) {
				    mspr_premium = new Double(pas.getMsp_premi());
				}
			}else if(pas.getProduct_code().equals("205") && ("PAS SYARIAH".equals(pas.getJenis_pas())||"PABSMSY".equals(pas.getJenis_pas()))){
				// Product_Code = Lsbs_id= Kode Plan 205 --> PAS Syariah
				//set Product=Lsdbs_number= (1->PAS Syariah Perdana (NEW)/2->PAS Syariah Single (NEW)/3->PAS Syariah Ceria (NEW)/4->PAS Syariah Ideal (NEW))
				//(5->PAS Syariah Perdana/6->PAS Syariah Single/7->PAS Syariah Ceria/8->PAS Syariah Ideal)
				double faktor_premi=1;
				if(pas.getLscb_id()==3){
					faktor_premi = 1;
				}
				else if(pas.getLscb_id()==2){
					faktor_premi = 0.525;
				}
				else if(pas.getLscb_id()==1){
					faktor_premi = 0.27;
				}
				else  if(pas.getLscb_id()==6){
					faktor_premi = 0.1;
				}
				
				if(pas.getProduk()==1 || pas.getProduk()==5 || pas.getProduk()==9){
					mspr_tsi = new Double(100000000);
					mspr_premium = new Double(faktor_premi*300000);
				}else if(pas.getProduk()==2 || pas.getProduk()==6 || pas.getProduk()==10){
					mspr_tsi = new Double(50000000);
					mspr_premium = new Double(faktor_premi*500000);
				}else if(pas.getProduk()==3 || pas.getProduk()==7 || pas.getProduk()==11){
					mspr_tsi = new Double(100000000);
					mspr_premium = new Double(faktor_premi*900000);
				}else if(pas.getProduk()==4 || pas.getProduk()==8 || pas.getProduk()==12){
					mspr_tsi = new Double(200000000);
					mspr_premium = new Double(faktor_premi*1600000);
				}
				
				if("PABSMSY".equals(pas.getJenis_pas())){
					edit.getDatausulan().setTipeproduk(new Integer(24));
				}
				
//				else if(pas.getProduk()==5){
//					mspr_tsi = new Double(100000000);
//					mspr_premium = new Double(faktor_premi*250000);
//				}else if(pas.getProduk()==6){
//					mspr_tsi = new Double(50000000);
//					mspr_premium = new Double(faktor_premi*450000);
//				}else if(pas.getProduk()==7){
//					mspr_tsi = new Double(100000000);
//					mspr_premium = new Double(faktor_premi*800000);
//				}else if(pas.getProduk()==8){
//					mspr_tsi = new Double(200000000);
//					mspr_premium = new Double(faktor_premi*1350000);
//				}
				
				//update no VA dari kolom excelnya
				edit.getTertanggung().setMste_no_vacc(pas.getNo_va());
				
			}else if(pas.getProduct_code().equals("187") && "SMART ACCIDENT CARE".equals(pas.getJenis_pas())){

				double faktor_premi=1;
				if(pas.getLscb_id()==3){
					faktor_premi = 1;
				}
				else if(pas.getLscb_id()==2){
					faktor_premi = 0.525;
				}
				else if(pas.getLscb_id()==1){
					faktor_premi = 0.27;
				}
				else  if(pas.getLscb_id()==6){
					faktor_premi = 0.1;
				}
				
				if(pas.getProduk()==11 || pas.getProduk()==15){
					mspr_tsi = new Double(100000000);
					mspr_premium = new Double(faktor_premi*300000);
				}else if(pas.getProduk()==12 || pas.getProduk()==16){
					mspr_tsi = new Double(50000000);
					mspr_premium = new Double(faktor_premi*500000);
				}else if(pas.getProduk()==13 || pas.getProduk()==17){
					mspr_tsi = new Double(100000000);
					mspr_premium = new Double(faktor_premi*900000);
				}else if(pas.getProduk()==14 || pas.getProduk()==18){
					mspr_tsi = new Double(200000000);
					mspr_premium = new Double(faktor_premi*1600000);
				}
				
				if(pas.getMsp_flag_cc()==1 || pas.getMsp_flag_cc()==2){
					pas.setLspd_id(118);
				}
			}else if("NISSAN PA".equals(pas.getJenis_pas())){
				double faktor_premi=1;
				if(pas.getLscb_id()==3){
					faktor_premi = 1;
				}
				else if(pas.getLscb_id()==2){
					faktor_premi = 0.525;
				}
				else if(pas.getLscb_id()==1){
					faktor_premi = 0.27;
				}
				else  if(pas.getLscb_id()==6){
					faktor_premi = 0.1;
				}
				
				mspr_tsi = new Double(70000000);
				mspr_premium = new Double(faktor_premi*50000);
				
				if(pas.getMsp_flag_cc()==1 || pas.getMsp_flag_cc()==2){
					pas.setLspd_id(118);
				}
			}else if("NISSAN DBD".equals(pas.getJenis_pas())){
				double faktor_premi=1;
				if(pas.getLscb_id()==3){
					faktor_premi = 1;
				}
				else if(pas.getLscb_id()==2){
					faktor_premi = 0.525;
				}
				else if(pas.getLscb_id()==1){
					faktor_premi = 0.27;
				}
				else  if(pas.getLscb_id()==6){
					faktor_premi = 0.1;
				}
				
				mspr_tsi = new Double(4500000);
				mspr_premium = new Double(faktor_premi*150000);
				
				if(pas.getMsp_flag_cc()==1 || pas.getMsp_flag_cc()==2){
					pas.setLspd_id(118);
				}
			}
			else{// simple bisnis
				mspr_tsi = new Double(pas.getMsp_up());
				mspr_premium = new Double(pas.getMsp_premi());
			}
			edit.getDatausulan().setIsBungaSimponi(0);
			edit.getDatausulan().setIsBonusTahapan(0);
			edit.getDatausulan().setLssp_id(10);
			edit.getDatausulan().setLssa_id(5);
			edit.getDatausulan().setLspd_id(4);
			if("187".equals(pas.getProduct_code()) && "SMART ACCIDENT CARE".equals(pas.getJenis_pas())){
				if(pas.getMsp_flag_cc()==1 || pas.getMsp_flag_cc()==2){
					edit.getDatausulan().setLspd_id(118);
				}
			}
			if("NISSAN PA".equals(pas.getJenis_pas()) || "NISSAN DBD".equals(pas.getJenis_pas())){
				edit.getDatausulan().setTipeproduk(new Integer(13));
			}
			
			edit.getDatausulan().setMspo_age(umurPp);
			edit.getDatausulan().setMspo_spaj_date(commonDao.selectSysdate());
			edit.getDatausulan().setMspo_beg_date(pas.getMsp_pas_beg_date());
			edit.getDatausulan().setMspo_end_date(pas.getMsp_pas_end_date());
			edit.getDatausulan().setMste_medical(0);
			edit.getDatausulan().setLscb_id(pas.getLscb_id());
			edit.getDatausulan().setMspr_tsi(mspr_tsi);
			edit.getDatausulan().setMspr_premium(mspr_premium);
			edit.getDatausulan().setMspr_discount(new Double(0));
			edit.getDatausulan().setMste_flag_cc(pas.getMsp_flag_cc());// rek client = 0.tunai, 2.tabungan, 1.CC
			edit.getDatausulan().setFlag_worksite(0);
			//edit.getDatausulan().setFlag_account(pas.getMsp_flag_cc());
			edit.getDatausulan().setFlag_account(2);// 0 untuk umum  1 untuk account recur 2 untuk rek client 3 untuk account recur dan rek client
			edit.getDatausulan().setKode_flag(0);//default
			edit.getDatausulan().setMspo_beg_date(pas.getMsp_pas_beg_date());
			edit.getDatausulan().setMspo_end_date(pas.getMsp_pas_end_date());
			edit.getDatausulan().setMste_beg_date(pas.getMsp_pas_beg_date());
			edit.getDatausulan().setMste_end_date(pas.getMsp_pas_end_date());
			edit.getDatausulan().setMspo_ins_period(1);
			edit.getDatausulan().setMspr_ins_period(1);
			edit.getDatausulan().setMspo_pay_period(mspo_pay_period);
			if("DBD BP".equals(pas.getJenis_pas())){
				edit.getDatausulan().setFlag_jenis_plan(5);//DBD
				edit.getDatausulan().setLsbs_id(203);//DBD
				edit.getDatausulan().setLsdbs_number(1);//DBD BP
			}else if("DBD AGENCY".equals(pas.getJenis_pas())){
				edit.getDatausulan().setFlag_jenis_plan(5);//DBD
				edit.getDatausulan().setLsbs_id(203);//DBD
				edit.getDatausulan().setLsdbs_number(2);//DBD AGENCY
			}else if("DBD SYARIAH".equals(pas.getJenis_pas())){
				edit.getDatausulan().setFlag_jenis_plan(5);//DBD
				edit.getDatausulan().setLsbs_id(209);//DBD SYARIAH
				edit.getDatausulan().setLsdbs_number(1);//DBD SYARIAH
			}else if("DBD MALL".equals(pas.getJenis_pas())){
				edit.getDatausulan().setFlag_jenis_plan(5);//DBD
				edit.getDatausulan().setLsbs_id(203);//DBD
				edit.getDatausulan().setLsdbs_number(3);//DBD MALL
			}else if(("INDIVIDU,AP/BP,AP/BP ONLINE,MALLINSURANCE,PAS BPD,BP SMS,BP CARD").indexOf(pas.getJenis_pas()) > -1){
				edit.getDatausulan().setFlag_jenis_plan(5);//PAS
				edit.getDatausulan().setLsbs_id(187);//PAS
				edit.getDatausulan().setLsdbs_number(Integer.parseInt(lsdbs_number));//PAS
			}else{
				if(pas.getProduct_code().equals("45") || pas.getProduct_code().equals("130") || pas.getProduct_code().equals("205")){//super protection
					edit.getDatausulan().setFlag_jenis_plan(5);//SIMPLE BISNIS/ BAC SIMPLE --untuk PA
				}else{
					edit.getDatausulan().setFlag_jenis_plan(0);//SIMPLE BISNIS/ BAC SIMPLE
				}
				edit.getDatausulan().setLsbs_id(Integer.parseInt(pas.getProduct_code()));//SIMPLE BISNIS/ BAC SIMPLE
				edit.getDatausulan().setLsdbs_number(pas.getProduk());//SIMPLE BISNIS/ BAC SIMPLE
			}
			edit.getDatausulan().setKurs_p("01");
			edit.getDatausulan().setLku_id("01");
			edit.getDatausulan().setMspo_nasabah_acc(pas.getNo_kartu());
			edit.getDatausulan().setJenis_pemegang_polis(0);
			
			//untuk produk Bank DMTM BTN Smart Accident Care set Provider otomatis - Ridhaal
//			if(edit.getAgen().getLca_id().equals("40") && edit.getAgen().getLwk_id().equals("01") && edit.getAgen().getLsrg_id().equals("04") 
//				&& "SMART ACCIDENT CARE".equals(pas.getJenis_pas())){
//				edit.getDatausulan().setMspo_provider(2);
//			}
						
			// tertanggung
//			if(pas.getLsre_id() == 1){
				edit.getTertanggung().setMspo_plan_provider(pas.getMspo_plan_provider());
				edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
				edit.getTertanggung().setNo_hp2(pas.getMsp_mobile2());
				edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no_tt());
				edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
				edit.getTertanggung().setKota_rumah(pas.getMsp_city());
				edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
				edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
				edit.getTertanggung().setEmail(pas.getMsp_pas_email());
//			}
//			edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
			edit.getTertanggung().setMkl_red_flag(0);
			edit.getTertanggung().setMcl_first(pas.getMsp_full_name());
//			edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no());
			edit.getTertanggung().setMspe_date_birth(pas.getMsp_date_of_birth());
//			edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
//			edit.getTertanggung().setKota_rumah(pas.getMsp_city());
//			edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
			edit.getTertanggung().setMste_age(pas.getMsp_age());
			edit.getTertanggung().setUsiattg(pas.getMsp_age());
//			edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
//			edit.getTertanggung().setEmail(pas.getMsp_pas_email());
			
			edit.getTertanggung().setDanaa("");
			edit.getTertanggung().setDanaa("");
			edit.getTertanggung().setDanaa2("");
			edit.getTertanggung().setShasil("");
			edit.getTertanggung().setMkl_pendanaan("");
			edit.getTertanggung().setMkl_smbr_penghasilan("");
			edit.getTertanggung().setMkl_kerja("");
			edit.getTertanggung().setMkl_industri("");
			edit.getTertanggung().setKerjaa("");
			edit.getTertanggung().setKerjab("");
			edit.getTertanggung().setIndustria("");
			edit.getTertanggung().setMspe_place_birth(pas.getMsp_pas_tmp_lhr_tt());
			edit.getTertanggung().setTujuana("");
			edit.getTertanggung().setMkl_tujuan("");
			edit.getTertanggung().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
			edit.getTertanggung().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
			edit.getTertanggung().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
			edit.getTertanggung().setReg_spaj(pas.getReg_spaj());
			edit.getTertanggung().setMspo_policy_no(pas.getMspo_policy_no());
			edit.getTertanggung().setLus_id(pas.getLus_id());
			edit.getTertanggung().setArea_code_rumah(pas.getMsp_area_code_rumah());
			edit.getTertanggung().setMspe_sex(pas.getMsp_sex_tt());
			if(pas.getLside_id() != null){
				edit.getTertanggung().setLside_id(pas.getLside_id());
			}
			if(pas.getLsre_id() == 1){
				edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
				edit.getTertanggung().setNo_hp2(pas.getMsp_mobile2());
				edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no());
				edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
				edit.getTertanggung().setKota_rumah(pas.getMsp_city());
				edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
				edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
				edit.getTertanggung().setEmail(pas.getMsp_pas_email());
				edit.getTertanggung().setMspe_sex(pas.getMsp_sex_pp());
				edit.getTertanggung().setTelpon_rumah(pas.getMsp_pas_phone_number());
				if(("AP/BP,AP/BP ONLINE,DBD AGENCY,DBD SYARIAH,DBD BP, DBD MALL,PAS BPD,BP SMS,BP CARD").indexOf(pas.getJenis_pas()) > -1){
					edit.getTertanggung().setLsne_id(pas.getMsp_warga());
					edit.getTertanggung().setMspe_sts_mrt(pas.getMsp_status_nikah().toString());
					edit.getTertanggung().setLsag_id(pas.getLsag_id());
					edit.getTertanggung().setMcl_agama(pas.getMsp_agama());
					edit.getTertanggung().setLsed_id(pas.getMsp_pendidikan());
					edit.getTertanggung().setMkl_kerja(pas.getMsp_occupation());
					if(pas.getMsp_occupation_else() == null)pas.setMsp_occupation_else("");
					if(pas.getMsp_occupation_else() == null){
						edit.getTertanggung().setKerjaa("");
					}else{
						edit.getTertanggung().setKerjaa(pas.getMsp_occupation_else());
					}
					if(("AP/BP,AP/BP ONLINE,BP SMS,BP CARD").indexOf(pas.getJenis_pas()) > -1){
						if(pas.getMsp_company_jabatan() == null)pas.setMsp_company_jabatan("");
						edit.getTertanggung().setKerjab(pas.getMsp_company_jabatan());
						edit.getTertanggung().setAlamat_kantor(pas.getMsp_company_address());
						edit.getTertanggung().setKd_pos_kantor(pas.getMsp_company_postal_code());
						edit.getTertanggung().setTelpon_kantor(pas.getMsp_company_phone_number());
					}
				}
			}
			
			// No Virtual Account
			if(pas.getNo_kartu() != null) {
				HashMap<String, Object> kartu = selectDetailKartuPas(pas.getNo_kartu());
				if(kartu != null && kartu.get("NO_VA") != null)
					edit.getTertanggung().setMste_no_vacc((String) kartu.get("NO_VA"));
			}
			
			int hasil = 0;
			edit = savingBac.insertspajpas(edit,currentUser, pas.getJenis_pas());
			if(!"".equals(edit.getPemegang().getReg_spaj())){
				
				// reas
				
				Reas reas=new Reas();
				reas.setLstbId(new Integer(1));
				String las_reas[]=new String[3];
				las_reas[0]="Non-Reas";
				las_reas[1]="Treaty";
				las_reas[2]="Facultative";
				reas.setCurrentUser(currentUser);   
				reas.setSpaj(edit.getPemegang().getReg_spaj());
				
				Map mPosisi=uwDao.selectF_check_posisi(reas.getSpaj());
				Integer lspdIdTemp=(Integer)mPosisi.get("LSPD_ID");
				reas.setLspdId(lspdIdTemp);
				String lspdPosTemp=(String)mPosisi.get("LSPD_POSITION");
				//produk asm
				Map mMap=selectDataUsulan(reas.getSpaj());
				Integer lsbsId=(Integer)mMap.get("LSBS_ID");

				//tertanggung
				Map mTertanggung=uwDao.selectTertanggung(reas.getSpaj());
				reas.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
				reas.setMsteInsured((String)mTertanggung.get("MCL_ID"));
				reas.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
				//
				Map mStatus=uwDao.selectWfGetStatus(reas.getSpaj(),reas.getInsuredNo());
				reas.setLiAksep((Integer)mStatus.get("LSSA_ID"));
				reas.setLiReas((Integer)mStatus.get("MSTE_REAS"));
				if (reas.getLiAksep()==null) 
					reas.setLiAksep( new Integer(1));
				
				
				//dw1 //pemegang
				Policy policy=uwDao.selectDw1Underwriting(reas.getSpaj(),reas.getLspdId(),reas.getLstbId());
				if(policy!=null){
					reas.setMspoPolicyHolder(policy.getMspo_policy_holder());
					reas.setNoPolis(policy.getMspo_policy_no());
					reas.setInsPeriod(policy.getMspo_ins_period());
					reas.setPayPeriod(policy.getMspo_pay_period());
					reas.setLkuId(policy.getLku_id());
					reas.setUmurPp(policy.getMspo_age());
					reas.setLcaId(policy.getLca_id());
					reas.setLcaId(policy.getLca_id());
					reas.setMste_kyc_date(policy.getMste_kyc_date());
				}
				
				//simultan===========================================================================================================
				//Integer sim = uwDao.prosesSimultan(1, edit.getPemegang().getReg_spaj(), edit.getAgen().getLca_id(), policy.getMspo_policy_holder(), reas.getMsteInsured());
				String spaj;
				Integer lsreIdPp;
				String lcaIdPp;
				int info;
				List lsSimultanTt,lsSimultanPp;

				spaj=edit.getPemegang().getReg_spaj();
				info=0;
				Command command=new Command();
				command.setCurrentUser(currentUser);
				command.setCount(new Integer(0));
				//
				Map a = uwDao.selectCheckPosisi(spaj);
				int li_pos=Integer.parseInt(a.get("LSPD_ID").toString());
				String ls_pos=a.get("LSPD_POSITION").toString();
				Map map=uwDao.selectDataUsulan(spaj);
				lsbsId=(Integer)map.get("LSBS_ID");
				//validasi Posisi SPAJ harus UW (2)
				if(li_pos !=2 ){
					info=1;
					//MessageBox('Info', 'Posisi Polis Ini Ada di ' + ls_pos )
				}else if(lsbsId.intValue()==161){//produk sinarmas
					info=3;
				}
				//
				Map mPemegang=uwDao.selectPemegang(spaj);
				
				String mclIdPp,mclFirstPp,sDateBirthPp;
				Date mspeDateBirthPp,mspeDateBirthTt;
				Integer lsreIdTt;
				String mclIdTt,mclFirstTt,sDateBirthTt,lcaIdTt;
				//data pemegang
				DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
				lsreIdPp=(Integer)mPemegang.get("LSRE_ID");
				mclIdPp=(String)mPemegang.get("MCL_ID");
				mclFirstPp=(String)mPemegang.get("MCL_FIRST");
				mspeDateBirthPp=(Date)mPemegang.get("MSPE_DATE_BIRTH");
			    sDateBirthPp=fd.format(mspeDateBirthPp);
				lcaIdPp=(String)mPemegang.get("LCA_ID");
				//data tertanggung
				//Map mTertanggung=uwDao.selectTertanggung(spaj);
				lsreIdTt=(Integer)mTertanggung.get("LSRE_ID");
				mclIdTt=(String)mTertanggung.get("MCL_ID");
				mclFirstTt=(String)mTertanggung.get("MCL_FIRST");
				mspeDateBirthTt=(Date)mTertanggung.get("MSPE_DATE_BIRTH");
			    sDateBirthTt=fd.format(mspeDateBirthTt);
				lcaIdTt=(String)mTertanggung.get("LCA_ID");
				//cek mcl_id
				if(!(mclIdTt.substring(0,2).equalsIgnoreCase("XX")))
					if(!(mclIdTt.substring(0,2).equalsIgnoreCase("WW")))
						if(!(mclIdPp.substring(0,2).equalsIgnoreCase("XX")))
							if(!(mclIdPp.substring(0,2).equalsIgnoreCase("WW"))){
								info=2;
								//MessageBox('Info', 'Proses Simultan Sudah pernah dilakukan untuk Pemegang & Tertanggung Polis!!!')
							}	
				int spasi,titik,koma,pjg;
				//pemegang ambil nama depan saja 
				spasi=mclFirstPp.indexOf(' ');
				titik=mclFirstPp.indexOf('.');
				koma=mclFirstPp.indexOf(',');
				pjg=mclFirstPp.length();
				if(spasi>0)
					mclFirstPp=mclFirstPp.substring(0,spasi);
				else if(titik>0)
					mclFirstPp=mclFirstPp.substring(0,titik);
				else if(koma>0)
					mclFirstPp=mclFirstPp.substring(0,koma);
				//Tertanggung ambil nama depan saja 
				spasi=mclFirstTt.indexOf(' ');
				titik=mclFirstTt.indexOf('.');
				koma=mclFirstTt.indexOf(',');
				pjg=mclFirstTt.length();
				if(spasi>0)
					mclFirstTt=mclFirstTt.substring(0,spasi);
				else if(titik>0)
					mclFirstTt=mclFirstTt.substring(0,titik);
				else if(koma>0)
					mclFirstTt=mclFirstTt.substring(0,koma);
				//
				Map param=new HashMap();
				param.put("mcl_id",mclIdPp);
				param.put("nama",mclFirstPp);
				param.put("tgl_lhr",sDateBirthPp);
				lsSimultanPp=uwDao.selectSimultanNew(param);
				param=new HashMap(); 
				param.put("mcl_id",mclIdTt);
				param.put("nama",mclFirstTt);
				param.put("tgl_lhr",sDateBirthTt);
				lsSimultanTt=uwDao.selectSimultanNew(param);
//				if(logger.isDebugEnabled())logger.debug(""+lsSimultanPp.size());
//				if(logger.isDebugEnabled())logger.debug(""+lsSimultanTt.size());
				Integer proses=null;
//				if(logger.isDebugEnabled())logger.debug("isi list simultan ="+lsSimultanTt.size());

				command.setSpaj(spaj);
				command.setLsreIdPp(lsreIdPp);
				command.setLcaIdPp(lcaIdPp);
				command.setLsSimultanPp(lsSimultanPp);
				command.setLsSimultanTt(lsSimultanTt);
				command.setRowPp(lsSimultanPp.size());
				command.setRowTt(lsSimultanTt.size());
				command.setFlagAdd(lsreIdPp.intValue());
				command.setError(new Integer(info));
				command.setFlagId(ls_pos);
				command.setMapPemegang(mPemegang);
				command.setMapTertanggung(mTertanggung);
				
				int choosePp=0;
				int chooseTt=0;
				
				if(command.getLsreIdPp()==1){//hbungan diri sendiri
					choosePp=chooseTt;
				}
				String idSimultanPp,idSimultanTt;
				idSimultanPp=(String)mPemegang.get("ID_SIMULTAN");
				idSimultanTt=(String)mTertanggung.get("ID_SIMULTAN");
				
				//mcl_id ambil yang baru karena data baru ini yang akan di isi
				Map mPpOld=command.getMapPemegang();
				Map mTtOld=command.getMapTertanggung();
				mclIdPp=(String)mPpOld.get("MCL_ID");
				mclIdTt=(String)mTtOld.get("MCL_ID");
		
				if(!("SMART ACCIDENT CARE".equals(pas.getJenis_pas()) || "NISSAN PA".equals(pas.getJenis_pas()) || "NISSAN DBD".equals(pas.getJenis_pas()) )){
					Integer sim = simultan.prosesSimultan(command, mclIdPp, mclIdTt, idSimultanPp, idSimultanTt);
				}
				//====================================================================================================================
				//Integer sim = uwDao.prosesSimultan(1, edit.getPemegang().getReg_spaj(), edit.getAgen().getLca_id(), policy.getMspo_policy_holder(), reas.getMsteInsured());
				
				//reas
				reas.setLstbId(1);
				reas.setLspdId(2);

				reasIndividu.prosesReasUnderwritingPas(reas, errors);
								
				//billing
				List lsDp = new ArrayList();
				uwDao.wf_ins_bill_pas(edit.getPemegang().getReg_spaj(), new Integer(1), new Integer(1), edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number(), lspdIdTemp, new Integer(1), lsDp, lus_id.toString(), policy, errors);
				
				//=================================================
//				uwManager.prosesAkseptasiPas(akseptasi, pas, user, 0, 0, "", errors);
				pas.setReg_spaj(edit.getPemegang().getReg_spaj());
				//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
				//akseptasi======================================
				int result = 0;
				//try{
					Akseptasi akseptasi = new Akseptasi();
					
					akseptasi.setLiAksep(5);
					akseptasi.setLspdId(4);
					if(pas.getJenis_pas().equals("SMART ACCIDENT CARE") && (map.get("MSTE_FLAG_CC").toString().equals("1") || map.get("MSTE_FLAG_CC").toString().equals("2"))){
						akseptasi.setLspdId(118);
						pas.setLspd_id(118);
					}
					akseptasi.setLsspId(1);
					akseptasi.setLssaId(5);
					akseptasi.setCurrentUser(currentUser);
					akseptasi.setSpaj(pas.getReg_spaj());
					akseptasi.setInsuredNo(1);
					akseptasi.setLcaId(regionid.substring(0, 2));
					akseptasi.setLsbsId(edit.getDatausulan().getLsbs_id());
					akseptasi.setProses("1");
					int thn = 0;
					int bln = 0;
					String desc = "";
					result =  uwDao.prosesAkseptasiPas(akseptasi,thn,bln,desc,errors);
				//}catch (Exception e){
				//	TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				//}
				
				//=======================================================================================
				//transfer ke payment
				Transfer transfer = new Transfer();
				DecimalFormat fmt = new DecimalFormat ("000");
				Map mDataUsulan=uwDao.selectDataUsulan(edit.getPemegang().getReg_spaj());
				transfer.setSpaj(edit.getPemegang().getReg_spaj());
				transfer.setPModeId((Integer)mDataUsulan.get("LSCB_ID"));
				transfer.setBegDate((Date)mDataUsulan.get("MSTE_BEG_DATE"));
				transfer.setEndDate((Date)mDataUsulan.get("MSTE_END_DATE"));
				transfer.setMedical((Integer)mDataUsulan.get("MSTE_MEDICAL"));
				transfer.setLsbsId((Integer)mDataUsulan.get("LSBS_ID"));
				transfer.setLsdbsNumber((Integer)mDataUsulan.get("LSDBS_NUMBER"));
				transfer.setBisnisId(fmt.format(transfer.getLsbsId().intValue()));
				transfer.setPremium((Double)mDataUsulan.get("MSPR_PREMIUM"));
				transfer.setTsi((Double)mDataUsulan.get("MSPR_TSI"));
				//tertanggung
				Map mTertanggung2=uwDao.selectTertanggung(transfer.getSpaj());
				transfer.setInsuredNo((Integer)mTertanggung2.get("MSTE_INSURED_NO"));
				transfer.setMsteInsured((String)mTertanggung2.get("MCL_ID"));
				transfer.setMsagId((String)mTertanggung2.get("MSAG_ID"));
				transfer.setUmurTt((Integer)mTertanggung2.get("MSTE_AGE"));
				transfer.setCurrentUser((User) request.getSession().getAttribute("currentUser"));
				transfer.setLsDp(new ArrayList());
				transfer.setLiLama(0);
				Map mStatus2=uwDao.selectWfGetStatus(transfer.getSpaj(),transfer.getInsuredNo());
				transfer.setLiAksep((Integer)mStatus.get("LSSA_ID"));
				transfer.setLiReas((Integer)mStatus.get("MSTE_REAS"));
				transfer.setLspdId(new Integer(4));
				transfer.setLstbId(new Integer(1));
				transfer.setLiPosisi(4);//pembayaran
				policy=uwDao.selectDw1Underwriting(transfer.getSpaj(),transfer.getLspdId(),transfer.getLstbId());
				if(policy!=null){
					transfer.setMspoPolicyHolder(policy.getMspo_policy_holder());
					transfer.setNoPolis(policy.getMspo_policy_no());
					transfer.setInsPeriod(policy.getMspo_ins_period());
					transfer.setPayPeriod(policy.getMspo_pay_period());
					transfer.setLkuId(policy.getLku_id());
					transfer.setUmurPp(policy.getMspo_age());
					transfer.setLcaId(policy.getLca_id());
				}
				transfer.setPolicy(policy);
				//hasil=transferUw.prosesTransferPembayaran(transfer,1,errors);
				//=================================================================================
				// checklist
				if(("AP/BP,AP/BP ONLINE,DBD BP,PAS BPD,BP SMS,BP CARD").indexOf(pas.getJenis_pas()) > -1){
					
					String mclIdNew = uwDao.selectMclIDPemegangPolis(edit.getPemegang().getReg_spaj());
					edit.getPersonal().setMcl_id(mclIdNew);
					
					commonDao.insertMstCompany(edit.getPersonal());
					
					Checklist cl = new Checklist();
					List<String> lcList = uwDao.selectLcChecklistWoMedis();
					
					int ktp_adm = pas.getMsp_cek_ktp();//lc_id=3
					int kk_adm = pas.getMsp_cek_kk();//lc_id=7
					int npwp_adm = pas.getMsp_cek_npwp();//lc_id=10
					int bukti_bayar_adm = pas.getMsp_cek_bukti_bayar();//lc_id=16
					int ktp_uw = pas.getMsp_cek_ktp_uw();//lc_id=3
					int kk_uw = pas.getMsp_cek_kk_uw();//lc_id=7
					int npwp_uw = pas.getMsp_cek_npwp_uw();//lc_id=10
					int bukti_bayar_uw = pas.getMsp_cek_bukti_bayar_uw();//lc_id=16
					
					Date tgl = commonDao.selectSysdate();
					
					for(int li = 0 ; li < lcList.size() ; li++){
						cl = new Checklist();
						cl.setReg_spaj(edit.getPemegang().getReg_spaj());
						cl.setLc_id(Integer.parseInt(lcList.get(li)));
						cl.setMc_no(li+1);
						cl.setLus_id_adm(pas.getLus_id());
						cl.setLus_id_uw(lus_id);
						cl.setTgl_adm(tgl);
						cl.setTgl_uw(tgl);
						cl.setLc_active(1);
						if(lcList.get(li).equals("3") && ktp_adm == 1){
							cl.setFlag_adm(1);
						}else if(lcList.get(li).equals("7") && kk_adm == 1){
							cl.setFlag_adm(1);
						}else if(lcList.get(li).equals("10") && npwp_adm == 1){
							cl.setFlag_adm(1);
						}else if(lcList.get(li).equals("16") && bukti_bayar_adm == 1){
							cl.setFlag_adm(1);
						}else{
							cl.setFlag_adm(0);
						}
						if(lcList.get(li).equals("3") && ktp_uw == 1){
							cl.setFlag_uw(1);
						}else if(lcList.get(li).equals("7") && kk_uw == 1){
							cl.setFlag_uw(1);
						}else if(lcList.get(li).equals("10") && npwp_uw == 1){
							cl.setFlag_uw(1);
						}else if(lcList.get(li).equals("16") && bukti_bayar_uw == 1){
							cl.setFlag_uw(1);
						}else{
							cl.setFlag_uw(0);
						}
						checklistDao.saveChecklistPas(cl);
					}
					
				}
				
				if(pas.getJenis_pas().equals("SMART ACCIDENT CARE") && (map.get("MSTE_FLAG_CC").toString().equals("1") || map.get("MSTE_FLAG_CC").toString().equals("2"))){
					policy=uwDao.selectDw1Underwriting(reas.getSpaj(),118,1);
				}else{
					policy=uwDao.selectDw1Underwriting(reas.getSpaj(),4,1);
				}
				
				edit.getPemegang().setMspo_policy_no(policy.getMspo_policy_no());
				pas.setMspo_policy_no(policy.getMspo_policy_no());
				String x ="";
				uwDao.updateKartuPas2(pas.getNo_kartu(), 1, edit.getPemegang().getReg_spaj());
				// update spaj ke det va
				if(pas.getNo_kartu() != null) {
	                HashMap<String, Object> kartu = selectDetailKartuPas(pas.getNo_kartu());
	                if(Common.isEmpty(kartu)) kartu = (HashMap<String, Object>) bacDao.selectDetailKartuPasBSMIB(pas.getNo_kartu()).get(0);
	                if(!Common.isEmpty(kartu) && !Common.isEmpty(kartu.get("NO_VA")))
                        bacDao.updateDetVa((String) kartu.get("NO_VA"), currentUser.getLus_id(), edit.getPemegang().getReg_spaj());
	            }
				if("insert".equals(jenis_proses)){
					uwDao.insertPas(pas);
				}else{
					uwDao.updatePas(pas);
				}
								
				//reff bii pabsm
//				if(pas.getLrb_id() != null && pas.getReff_id() != null){
				if("73".equals(pas.getProduct_code())) {
				    HashMap<String, Object> kartu = selectDetailKartuPas(pas.getNo_kartu());
	                if(kartu != null) {
    				    ReffBii rb = new ReffBii();
    					rb.setLcb_no(pas.getLcb_no());
    					rb.setLcb_no2(pas.getLcb_reff());
    					rb.setReg_spaj(pas.getReg_spaj());				
    					rb.setLrb_id(pas.getLrb_id().toString());
    					rb.setReff_id(pas.getReff_id().toString());
    					
    					String cabangKK = "";
    					List<Map> listCabangKK = selectCabangKK(rb.getLcb_no2());
    					if(listCabangKK.size() > 0)
    						cabangKK = (String) listCabangKK.get(0).get("LCB_NO");
    					rb.setLcb_no_kk(cabangKK);
    					ShintabiiSubmit(currentUser, rb, cabangKK);
	                }
				}				
				
				//DBD MALLAssurance: Copy Dok.SPAJ, Bukti Identitas & BSB upload berkas utk yg sudah ada SPAJ
				// dari \\EbServer\pdfind\Polis\dbd  ke  \\EbServer\pdfind\Polis\58				
				try {
				if(pas.getJenis_pas().equalsIgnoreCase("DBD MALL")){
					
					SimpleDateFormat df = new SimpleDateFormat("yyyyMM");	
					String directory = props.getProperty("pdf.dir.export")+"\\dbd";
					directory = directory+"\\" + df.format(commonDao.selectSysdate()) + "\\" + pas.getMsp_fire_id();
					
					List<DropDown> daftarFile = FileUtil.listFilesInDirectory(directory);
					
					if(daftarFile.size()>0){
					File destDir = new File(props.getProperty("pdf.dir.export") + "\\" + "58" + "\\" + edit.getPemegang().getReg_spaj());
					if(!destDir.exists()) destDir.mkdir();	
					String dest = destDir.getPath();
					
					
					List<DropDown> daftarFile2 = new ArrayList<DropDown>();
					
					List<DropDown> boleh = new ArrayList<DropDown>();
					boleh.add(new DropDown(pas.getMsp_fire_id(), "DBD MALL"));
					
					//if(p.getMsp_fire_id()!=null && edit.getPemegang().getMspo_policy_no()!=null){
					if(pas.getMsp_fire_id()!=null){
						for(DropDown d : daftarFile) {
							for(DropDown s : boleh) {
								if(d.getKey().toLowerCase().contains(s.getKey()) && !d.getKey().toLowerCase().contains("decrypted")) {
									d.setDesc(s.getValue());
									daftarFile2.add(d);
								}
							}
						}
						
						for(DropDown d2 : daftarFile2){
							FileUtil.copyfile(directory+"\\"+d2.getKey(), dest+"\\"+d2.getKey().replace(pas.getMsp_fire_id()+" ", edit.getPemegang().getReg_spaj()));
							//deleteFile("\\ebserver\\pdfind\\Polis\\fire\\", d2.getKey(), null);
						}					
					}
				}
				}
				}catch (Exception e){
					logger.error("ERROR :", e);
				}
				
			}else{
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				pas.setMspo_policy_no(null);
				pas.setReg_spaj(null);
			}
			uwDao.insertMstPositionSpajPas(pas.getLus_id().toString(), "TRANSFER DATA", pas.getMsp_fire_id(), 5);
		}
		catch (Exception e){
			logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			pas.setMspo_policy_no(null);
			pas.setReg_spaj(null);
			edit.getPemegang().setReg_spaj(null);
			edit.getPemegang().setMspo_policy_no(null);
		}
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		return edit;
	}
	
	
	public Cmdeditbac prosesHcp(HttpServletRequest request, String jenis_proses, Pas pas, Errors err,String keterangan, User currentUser, BindException errors) throws ServletException,IOException,Exception
	{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		Cmdeditbac edit= new Cmdeditbac();
		int umurPp=0;
		try{
			Integer lus_id = Integer.parseInt(currentUser.getLus_id());
		//	if(request.getParameter("kata")!=null){
				Date beg_date = pas.getMsp_pas_beg_date();//commonDao.selectSysdateTrunc();
				Date end_date = pas.getMsp_pas_end_date();//commonDao.selectSysdateTrunc();
//				Date end_fire_date = commonDao.selectSysdateTrunc();
//				end_date.setYear(end_date.getYear()+1);
//				end_date.setDate(end_date.getDate()-1);
				//pas.setMsp_pas_beg_date(beg_date);
				//pas.setMsp_pas_end_date(end_date);
//				pas.setMsp_fire_beg_date(beg_date);
//				end_fire_date.setMonth(beg_date.getMonth()+6);
//				end_fire_date.setDate(end_fire_date.getDate()-1);
//				pas.setMsp_fire_end_date(end_fire_date);
				pas.setLus_id(lus_id);
				pas.setLus_login_name(currentUser.getLus_full_name());
				pas.setDist("05");
				pas.setLspd_id(99);
				
				//tgl beg date
				int tanggal1= beg_date.getDate();
				int bulan1 = beg_date.getMonth()+1;
				int tahun1 = beg_date.getYear()+1900;
				
				//tgl lahir ttg
				int tanggal2=pas.getMsp_date_of_birth().getDate();
				int bulan2=pas.getMsp_date_of_birth().getMonth()+1;
				int tahun2=pas.getMsp_date_of_birth().getYear()+1900;
				
				//tgl lahir pp
				int tanggal3=pas.getMsp_pas_dob_pp().getDate();
				int bulan3=pas.getMsp_pas_dob_pp().getMonth()+1;
				int tahun3=pas.getMsp_pas_dob_pp().getYear()+1900;
				
				//hit umur ttg, pp, pic
				f_hit_umur umr= new f_hit_umur();
				int umur =  umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
				umurPp =  umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
				
				pas.setMsp_age(umur);
				
		//	}
			
			CheckSum checkSum = new CheckSum();
			edit.setPemegang(new Pemegang());
			edit.setPersonal(new Personal());
			edit.setContactPerson(new ContactPerson());
			edit.setTertanggung(new Tertanggung());
			edit.setDatausulan(new Datausulan());
			edit.setAgen(new Agen());
			edit.setAddressbilling(new AddressBilling());
			edit.setRekening_client(new Rekening_client());
			edit.setAccount_recur(new Account_recur());
			
			// agen
			Map agentMap = bacDao.selectagenpenutup(pas.getMsag_id());
			String regionid = (String) agentMap.get("REGIONID");
			edit.getAgen().setMsag_id((String) agentMap.get("ID"));
			edit.getAgen().setMcl_first((String) agentMap.get("NAMA"));
			edit.getAgen().setKode_regional(regionid);
			edit.getAgen().setLca_id(regionid.substring(0, 2));
			edit.getAgen().setLwk_id(regionid.substring(2, 4));
			edit.getAgen().setLsrg_id(regionid.substring(4, 6));
			if(edit.getAgen().getLca_id() == "58"){
				edit.getAgen().setLrb_id(pas.getLrb_id().toString());
			}
			
			edit.getContactPerson().setNama_lengkap("");
			
			// pemegang
			edit.getPemegang().setMspo_plan_provider(pas.getMspo_plan_provider());
			edit.getPemegang().setNo_hp(pas.getMsp_mobile());
			edit.getPemegang().setNo_hp2(pas.getMsp_mobile2());
			edit.getPemegang().setMcl_first(pas.getMsp_pas_nama_pp());
			edit.getPemegang().setMspe_no_identity(pas.getMsp_identity_no());
			edit.getPemegang().setMspe_date_birth(pas.getMsp_pas_dob_pp());
			edit.getPemegang().setMspe_email(pas.getMsp_pas_email());
			edit.getPemegang().setEmail(pas.getMsp_pas_email());
			edit.getPemegang().setAlamat_rumah(pas.getMsp_address_1());
			edit.getPemegang().setKota_rumah(pas.getMsp_city());
			edit.getPemegang().setKd_pos_rumah(pas.getMsp_postal_code());
//			if(pas.getMsag_id_pp() != null){
//				edit.getPemegang().setMspo_ao(pas.getMsag_id_pp());
//			}else{
//				edit.getPemegang().setMspo_ao(pas.getKode_ao());
//			}
			if(("AP/BP,DBD BP,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
				edit.getPemegang().setMspo_ao(pas.getMsag_id_pp());
			}else{
				edit.getPemegang().setMspo_ao(pas.getMsag_id());
			}
//			else{
//				edit.getPemegang().setMspo_ao(pas.getKode_ao());
//			}
			edit.getPemegang().setMspo_pribadi(pas.getPribadi());
			edit.getPemegang().setMspe_sex(pas.getMsp_sex_pp());
			edit.getPemegang().setReg_spaj(pas.getReg_spaj());
			edit.getPemegang().setMspo_policy_no(pas.getMspo_policy_no());
			edit.getPemegang().setMspo_spaj_date(commonDao.selectSysdate());
			edit.getPemegang().setMste_tgl_recur(pas.getMsp_tgl_valid());
			edit.getPemegang().setLus_id(pas.getLus_id());
			edit.getPemegang().setMspo_age(umurPp);
			edit.getPemegang().setUsiapp(umurPp);
			edit.getPemegang().setMspe_place_birth(pas.getMsp_pas_tmp_lhr_pp());
			edit.getPemegang().setArea_code_rumah(pas.getMsp_area_code_rumah());
			if(pas.getLside_id() != null){
				edit.getPemegang().setLside_id(pas.getLside_id());
			}
			edit.getPemegang().setTelpon_rumah(pas.getMsp_pas_phone_number());
			edit.getPemegang().setLsre_id(pas.getLsre_id());
			edit.getPemegang().setMkl_tujuan("");
			edit.getPemegang().setTujuana("");
			edit.getPemegang().setMkl_pendanaan("");
			edit.getPemegang().setDanaa("");
			edit.getPemegang().setMkl_smbr_penghasilan("");
			edit.getPemegang().setShasil("");
			edit.getPemegang().setDanaa2("");
			edit.getPemegang().setMkl_kerja("");
			edit.getPemegang().setKerjaa("");
			edit.getPemegang().setKerjab("");
			edit.getPemegang().setMkl_industri("");
			edit.getPemegang().setIndustria("");
			edit.getPemegang().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
			edit.getPemegang().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
			edit.getPemegang().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
			
			if(pas.getMsag_id_pp() == null)pas.setMsag_id_pp("");
			if(("AP/BP,AP/BP ONLINE,DBD BP,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
				edit.getPemegang().setLsne_id(pas.getMsp_warga());
				edit.getPemegang().setMspe_sts_mrt(pas.getMsp_status_nikah().toString());
				edit.getPemegang().setLsag_id(pas.getLsag_id());
				edit.getPemegang().setMcl_agama(pas.getMsp_agama());
				edit.getPemegang().setLsed_id(pas.getMsp_pendidikan());
				edit.getPemegang().setMkl_kerja(pas.getMsp_occupation());
				if(pas.getMsp_occupation_else() == null)pas.setMsp_occupation_else("");
				if(pas.getMsp_occupation_else() == null){
					edit.getPemegang().setKerjaa("");
				}else{
					edit.getPemegang().setKerjaa(pas.getMsp_occupation_else());
				}
				if(pas.getMsp_company_jabatan() == null)pas.setMsp_company_jabatan("");
				edit.getPemegang().setKerjab(pas.getMsp_company_jabatan());
				
				edit.setPersonal(new Personal());
				//set mcl id terbaru ada dibawah
				//edit.getPersonal().setMcl_id(mcl_id);
				edit.getPersonal().setMcl_first(pas.getMsp_company_name());
//				edit.getPersonal().setLju_id(pas.getMsp_company_usaha());
				if(pas.getLju_id() != null){
					edit.getPersonal().setLju_id(pas.getLju_id());
				}
				edit.getPemegang().setAlamat_kantor(pas.getMsp_company_address());
				edit.getPemegang().setKd_pos_kantor(pas.getMsp_company_postal_code());
				edit.getPemegang().setTelpon_kantor(pas.getMsp_company_phone_number());
				
			}
			
			// address billing
			edit.getAddressbilling().setNo_hp(pas.getMsp_mobile());
			edit.getAddressbilling().setNo_hp2(pas.getMsp_mobile2());
			edit.getAddressbilling().setMsap_contact(pas.getMsp_full_name());
			edit.getAddressbilling().setMsap_address(pas.getMsp_address_1());
			edit.getAddressbilling().setMsap_area_code1(pas.getMsp_area_code_rumah());
			edit.getAddressbilling().setKota(pas.getMsp_city());
			edit.getAddressbilling().setMsap_zip_code(pas.getMsp_postal_code());
			edit.getAddressbilling().setReg_spaj(pas.getReg_spaj());
			edit.getAddressbilling().setRegion(regionid);
			edit.getAddressbilling().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
			edit.getAddressbilling().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
			edit.getAddressbilling().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
			
			// rekening client
			edit.getRekening_client().setNotes("HCP");
			edit.getRekening_client().setLsbp_id(pas.getLsbp_id());
			if(pas.getMsp_flag_cc() == 2){//tabungan
				edit.getRekening_client().setMrc_nama(pas.getMsp_rek_nama().toUpperCase());
				edit.getRekening_client().setMrc_cabang(pas.getMsp_rek_cabang().toUpperCase());
				edit.getRekening_client().setMrc_kota(pas.getMsp_rek_kota().toUpperCase());
			}
			edit.getRekening_client().setMrc_jn_nasabah(0);//none
			edit.getRekening_client().setMrc_kurs("01");//rupiah
			edit.getRekening_client().setNo_account(pas.getMsp_no_rekening());
			edit.getRekening_client().setMrc_no_ac(pas.getMsp_no_rekening());
			edit.getRekening_client().setMrc_no_ac_lama(pas.getMsp_no_rekening());
			edit.getRekening_client().setMrc_jenis(2);// rek client
			
			// account recur
			edit.getAccount_recur().setLbn_id(pas.getLsbp_id_autodebet());
			edit.getAccount_recur().setLus_id(pas.getLus_id());
			edit.getAccount_recur().setMar_number(1);
			edit.getAccount_recur().setMar_acc_no(pas.getMsp_no_rekening_autodebet());
			edit.getAccount_recur().setMar_holder(pas.getMsp_rek_nama_autodebet());
			edit.getAccount_recur().setMar_expired(pas.getMsp_tgl_valid());
			edit.getAccount_recur().setMar_active(1);
			
			// data usulan
//			String lsdbs_number = uwDao.selectCekPin(pas.getPin(), 1);
//			if(lsdbs_number == null)lsdbs_number = "x";
			String lsdbs_number = pas.getProduk().toString();
//			if(pas.getPin() != null){
//				lsdbs_number = uwDao.selectCekPin(pas.getPin(), 1);
//			}else{
//			}
			int mspo_pay_period = 1;
		
			Double mspr_tsi = null;
			if("1".equals(lsdbs_number)){
				mspr_tsi = new Double(100000);
			}else if("2".equals(lsdbs_number)){
				mspr_tsi = new Double(200000);
			}else if("3".equals(lsdbs_number)){
				mspr_tsi = new Double(300000);
			}else if("4".equals(lsdbs_number)){
				mspr_tsi = new Double(400000);
			}else if("5".equals(lsdbs_number)){
				mspr_tsi = new Double(500000);
			}else if("6".equals(lsdbs_number)){
				mspr_tsi = new Double(600000);
			}else if("7".equals(lsdbs_number)){
				mspr_tsi = new Double(700000);
			}else if("8".equals(lsdbs_number)){
				mspr_tsi = new Double(800000);
			}else if("9".equals(lsdbs_number)){
				mspr_tsi = new Double(900000);
			}else if("10".equals(lsdbs_number)){
				mspr_tsi = new Double(1000000);
			}else{
				mspr_tsi = new Double(100000);
			}
			
			Double mspr_premium = null;
			Double premi_tahunan = new Double(pas.getMsp_premi());// premi tahunan
			if(pas.getLscb_id() == 3){
				mspr_premium = premi_tahunan;
			}else if(pas.getLscb_id() == 6){
				mspr_premium = premi_tahunan * 0.10;
			}else if(pas.getLscb_id() == 1){
				mspr_premium = premi_tahunan * 0.27;
			}else if(pas.getLscb_id() == 2){
				mspr_premium = premi_tahunan * 0.525;
			}else{
				mspr_premium = new Double(0);
			}
			edit.getDatausulan().setIsBungaSimponi(0);
			edit.getDatausulan().setIsBonusTahapan(0);
			edit.getDatausulan().setLssp_id(10);
			edit.getDatausulan().setLssa_id(5);
			edit.getDatausulan().setLspd_id(4);
			edit.getDatausulan().setMspo_age(umurPp);
			edit.getDatausulan().setMspo_spaj_date(commonDao.selectSysdate());
			edit.getDatausulan().setMspo_beg_date(pas.getMsp_pas_beg_date());
			edit.getDatausulan().setMspo_end_date(pas.getMsp_pas_end_date());
			edit.getDatausulan().setMste_medical(0);
			edit.getDatausulan().setLscb_id(pas.getLscb_id());
			edit.getDatausulan().setMspr_tsi(mspr_tsi);
			edit.getDatausulan().setMspr_premium(mspr_premium);
			edit.getDatausulan().setMspr_discount(new Double(0));
			edit.getDatausulan().setMste_flag_cc(pas.getMsp_flag_cc());
			edit.getDatausulan().setFlag_as(3);
			edit.getDatausulan().setMspo_provider(2);//untuk yg HCP
			edit.getDatausulan().setFlag_worksite(0);
			edit.getDatausulan().setFlag_account(pas.getMsp_flag_cc());// rek client = 0.tunai, 2.tabungan, 1.CC
			edit.getDatausulan().setKode_flag(0);//default
			edit.getDatausulan().setMspo_beg_date(pas.getMsp_pas_beg_date());
			edit.getDatausulan().setMspo_end_date(pas.getMsp_pas_end_date());
			edit.getDatausulan().setMste_beg_date(pas.getMsp_pas_beg_date());
			edit.getDatausulan().setMste_end_date(pas.getMsp_pas_end_date());
			edit.getDatausulan().setMspo_ins_period(1);
			edit.getDatausulan().setMspr_ins_period(1);
			edit.getDatausulan().setMspo_pay_period(mspo_pay_period);
			edit.getDatausulan().setFlag_jenis_plan(5);//PAS
			edit.getDatausulan().setLsbs_id(195);//HCP
			edit.getDatausulan().setLsdbs_number(Integer.parseInt(lsdbs_number));//HCP-ekatest
			edit.getDatausulan().setKurs_p("01");
			edit.getDatausulan().setLku_id("01");
			edit.getDatausulan().setMspo_nasabah_acc(pas.getNo_kartu());
			edit.getDatausulan().setJenis_pemegang_polis(0);
			
			// tertanggung
//			if(pas.getLsre_id() == 1){
				edit.getTertanggung().setMspo_plan_provider(pas.getMspo_plan_provider());
				edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
				edit.getTertanggung().setNo_hp2(pas.getMsp_mobile2());
				edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no_tt());
				edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
				edit.getTertanggung().setKota_rumah(pas.getMsp_city());
				edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
				edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
				edit.getTertanggung().setEmail(pas.getMsp_pas_email());
//			}
//			edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
			edit.getTertanggung().setMcl_first(pas.getMsp_full_name());
//			edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no());
			edit.getTertanggung().setMspe_date_birth(pas.getMsp_date_of_birth());
//			edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
//			edit.getTertanggung().setKota_rumah(pas.getMsp_city());
//			edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
			edit.getTertanggung().setMste_age(pas.getMsp_age());
			edit.getTertanggung().setUsiattg(pas.getMsp_age());
//			edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
//			edit.getTertanggung().setEmail(pas.getMsp_pas_email());
			
			edit.getTertanggung().setDanaa("");
			edit.getTertanggung().setDanaa("");
			edit.getTertanggung().setDanaa2("");
			edit.getTertanggung().setMkl_pendanaan("");
			edit.getTertanggung().setMkl_smbr_penghasilan("");
			edit.getTertanggung().setMkl_kerja("");
			edit.getTertanggung().setMkl_industri("");
			edit.getTertanggung().setKerjaa("");
			edit.getTertanggung().setKerjab("");
			edit.getTertanggung().setIndustria("");
			edit.getTertanggung().setMspe_place_birth(pas.getMsp_pas_tmp_lhr_tt());
			edit.getTertanggung().setTujuana("");
			edit.getTertanggung().setMkl_tujuan("");
			edit.getTertanggung().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
			edit.getTertanggung().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
			edit.getTertanggung().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
			edit.getTertanggung().setReg_spaj(pas.getReg_spaj());
			edit.getTertanggung().setMspo_policy_no(pas.getMspo_policy_no());
			edit.getTertanggung().setLus_id(pas.getLus_id());
			edit.getTertanggung().setArea_code_rumah(pas.getMsp_area_code_rumah());
			if(pas.getLside_id() != null){
				edit.getTertanggung().setLside_id(pas.getLside_id());
			}
			if(pas.getLsre_id() == 1){
				edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
				edit.getTertanggung().setNo_hp2(pas.getMsp_mobile2());
				edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no());
				edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
				edit.getTertanggung().setKota_rumah(pas.getMsp_city());
				edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
				edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
				edit.getTertanggung().setEmail(pas.getMsp_pas_email());
				edit.getTertanggung().setMspe_sex(pas.getMsp_sex_pp());
				edit.getTertanggung().setTelpon_rumah(pas.getMsp_pas_phone_number());
				if(("AP/BP,AP/BP ONLINE,DBD BP,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
					edit.getTertanggung().setLsne_id(pas.getMsp_warga());
					edit.getTertanggung().setMspe_sts_mrt(pas.getMsp_status_nikah().toString());
					edit.getTertanggung().setLsag_id(pas.getLsag_id());
					edit.getTertanggung().setMcl_agama(pas.getMsp_agama());
					edit.getTertanggung().setLsed_id(pas.getMsp_pendidikan());
					edit.getTertanggung().setMkl_kerja(pas.getMsp_occupation());
					if(pas.getMsp_occupation_else() == null)pas.setMsp_occupation_else("");
					if(pas.getMsp_occupation_else() == null){
						edit.getTertanggung().setKerjaa("");
					}else{
						edit.getTertanggung().setKerjaa(pas.getMsp_occupation_else());
					}
					if(pas.getMsp_company_jabatan() == null)pas.setMsp_company_jabatan("");
					edit.getTertanggung().setKerjab(pas.getMsp_company_jabatan());
					edit.getTertanggung().setAlamat_kantor(pas.getMsp_company_address());
					edit.getTertanggung().setKd_pos_kantor(pas.getMsp_company_postal_code());
					edit.getTertanggung().setTelpon_kantor(pas.getMsp_company_phone_number());
				}
			}
			
			int hasil = 0;
			
			edit = savingBac.insertspajhcp(edit,currentUser);
			if(!"".equals(edit.getPemegang().getReg_spaj())){
				
				// reas
				
				Reas reas=new Reas();
				reas.setLstbId(new Integer(1));
				String las_reas[]=new String[3];
				las_reas[0]="Non-Reas";
				las_reas[1]="Treaty";
				las_reas[2]="Facultative";
				reas.setCurrentUser(currentUser);   
				reas.setSpaj(edit.getPemegang().getReg_spaj());
				
				Map mPosisi=uwDao.selectF_check_posisi(reas.getSpaj());
				Integer lspdIdTemp=(Integer)mPosisi.get("LSPD_ID");
				reas.setLspdId(lspdIdTemp);
				String lspdPosTemp=(String)mPosisi.get("LSPD_POSITION");
				//produk asm
				Map mMap=selectDataUsulan(reas.getSpaj());
				Integer lsbsId=(Integer)mMap.get("LSBS_ID");

				//tertanggung
				Map mTertanggung=uwDao.selectTertanggung(reas.getSpaj());
				reas.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
				reas.setMsteInsured((String)mTertanggung.get("MCL_ID"));
				reas.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
				//
				Map mStatus=uwDao.selectWfGetStatus(reas.getSpaj(),reas.getInsuredNo());
				reas.setLiAksep((Integer)mStatus.get("LSSA_ID"));
				reas.setLiReas((Integer)mStatus.get("MSTE_REAS"));
				if (reas.getLiAksep()==null) 
					reas.setLiAksep( new Integer(1));
				
				
				//dw1 //pemegang
				Policy policy=uwDao.selectDw1Underwriting(reas.getSpaj(),reas.getLspdId(),reas.getLstbId());
				if(policy!=null){
					reas.setMspoPolicyHolder(policy.getMspo_policy_holder());
					reas.setNoPolis(policy.getMspo_policy_no());
					reas.setInsPeriod(policy.getMspo_ins_period());
					reas.setPayPeriod(policy.getMspo_pay_period());
					reas.setLkuId(policy.getLku_id());
					reas.setUmurPp(policy.getMspo_age());
					reas.setLcaId(policy.getLca_id());
					reas.setLcaId(policy.getLca_id());
					reas.setMste_kyc_date(policy.getMste_kyc_date());
				}
				
				//simultan===========================================================================================================
				//Integer sim = uwDao.prosesSimultan(1, edit.getPemegang().getReg_spaj(), edit.getAgen().getLca_id(), policy.getMspo_policy_holder(), reas.getMsteInsured());
				String spaj;
				Integer lsreIdPp;
				String lcaIdPp;
				int info;
				List lsSimultanTt,lsSimultanPp;

				spaj=edit.getPemegang().getReg_spaj();
				info=0;
				Command command=new Command();
				command.setCurrentUser(currentUser);
				command.setCount(new Integer(0));
				//
				Map a = uwDao.selectCheckPosisi(spaj);
				int li_pos=Integer.parseInt(a.get("LSPD_ID").toString());
				String ls_pos=a.get("LSPD_POSITION").toString();
				Map map=uwDao.selectDataUsulan(spaj);
				lsbsId=(Integer)map.get("LSBS_ID");
				//validasi Posisi SPAJ harus UW (2)
				if(li_pos !=2 ){
					info=1;
					//MessageBox('Info', 'Posisi Polis Ini Ada di ' + ls_pos )
				}else if(lsbsId.intValue()==161){//produk sinarmas
					info=3;
				}
				//
				Map mPemegang=uwDao.selectPemegang(spaj);
				
				String mclIdPp,mclFirstPp,sDateBirthPp;
				Date mspeDateBirthPp,mspeDateBirthTt;
				Integer lsreIdTt;
				String mclIdTt,mclFirstTt,sDateBirthTt,lcaIdTt;
				//data pemegang
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				lsreIdPp=(Integer)mPemegang.get("LSRE_ID");
				mclIdPp=(String)mPemegang.get("MCL_ID");
				mclFirstPp=(String)mPemegang.get("MCL_FIRST");
				mspeDateBirthPp=(Date)mPemegang.get("MSPE_DATE_BIRTH");
			    sDateBirthPp=df.format(mspeDateBirthPp);
				lcaIdPp=(String)mPemegang.get("LCA_ID");
				//data tertanggung
				//Map mTertanggung=uwDao.selectTertanggung(spaj);
				lsreIdTt=(Integer)mTertanggung.get("LSRE_ID");
				mclIdTt=(String)mTertanggung.get("MCL_ID");
				mclFirstTt=(String)mTertanggung.get("MCL_FIRST");
				mspeDateBirthTt=(Date)mTertanggung.get("MSPE_DATE_BIRTH");
			    sDateBirthTt=df.format(mspeDateBirthTt);
				lcaIdTt=(String)mTertanggung.get("LCA_ID");
				//cek mcl_id
				if(!(mclIdTt.substring(0,2).equalsIgnoreCase("XX")))
					if(!(mclIdTt.substring(0,2).equalsIgnoreCase("WW")))
						if(!(mclIdPp.substring(0,2).equalsIgnoreCase("XX")))
							if(!(mclIdPp.substring(0,2).equalsIgnoreCase("WW"))){
								info=2;
								//MessageBox('Info', 'Proses Simultan Sudah pernah dilakukan untuk Pemegang & Tertanggung Polis!!!')
							}	
				int spasi,titik,koma,pjg;
				//pemegang ambil nama depan saja 
				spasi=mclFirstPp.indexOf(' ');
				titik=mclFirstPp.indexOf('.');
				koma=mclFirstPp.indexOf(',');
				pjg=mclFirstPp.length();
				if(spasi>0)
					mclFirstPp=mclFirstPp.substring(0,spasi);
				else if(titik>0)
					mclFirstPp=mclFirstPp.substring(0,titik);
				else if(koma>0)
					mclFirstPp=mclFirstPp.substring(0,koma);
				//Tertanggung ambil nama depan saja 
				spasi=mclFirstTt.indexOf(' ');
				titik=mclFirstTt.indexOf('.');
				koma=mclFirstTt.indexOf(',');
				pjg=mclFirstTt.length();
				if(spasi>0)
					mclFirstTt=mclFirstTt.substring(0,spasi);
				else if(titik>0)
					mclFirstTt=mclFirstTt.substring(0,titik);
				else if(koma>0)
					mclFirstTt=mclFirstTt.substring(0,koma);
				//
				Map param=new HashMap();
				param.put("mcl_id",mclIdPp);
				param.put("nama",mclFirstPp);
				param.put("tgl_lhr",sDateBirthPp);
				lsSimultanPp=uwDao.selectSimultanNew(param);
				param=new HashMap(); 
				param.put("mcl_id",mclIdTt);
				param.put("nama",mclFirstTt);
				param.put("tgl_lhr",sDateBirthTt);
				lsSimultanTt=uwDao.selectSimultanNew(param);
//				if(logger.isDebugEnabled())logger.debug(""+lsSimultanPp.size());
//				if(logger.isDebugEnabled())logger.debug(""+lsSimultanTt.size());
				Integer proses=null;
//				if(logger.isDebugEnabled())logger.debug("isi list simultan ="+lsSimultanTt.size());

				command.setSpaj(spaj);
				command.setLsreIdPp(lsreIdPp);
				command.setLcaIdPp(lcaIdPp);
				command.setLsSimultanPp(lsSimultanPp);
				command.setLsSimultanTt(lsSimultanTt);
				command.setRowPp(lsSimultanPp.size());
				command.setRowTt(lsSimultanTt.size());
				command.setFlagAdd(lsreIdPp.intValue());
				command.setError(new Integer(info));
				command.setFlagId(ls_pos);
				command.setMapPemegang(mPemegang);
				command.setMapTertanggung(mTertanggung);
				
				int choosePp=0;
				int chooseTt=0;
				
				if(command.getLsreIdPp()==1){//hbungan diri sendiri
					choosePp=chooseTt;
				}
				String idSimultanPp,idSimultanTt;
				idSimultanPp=(String)mPemegang.get("ID_SIMULTAN");
				idSimultanTt=(String)mTertanggung.get("ID_SIMULTAN");
				
				//mcl_id ambil yang baru karena data baru ini yang akan di isi
				Map mPpOld=command.getMapPemegang();
				Map mTtOld=command.getMapTertanggung();
				mclIdPp=(String)mPpOld.get("MCL_ID");
				mclIdTt=(String)mTtOld.get("MCL_ID");
				
				Integer sim = simultan.prosesSimultan(command, mclIdPp, mclIdTt, idSimultanPp, idSimultanTt);
				//====================================================================================================================
				//Integer sim = uwDao.prosesSimultan(1, edit.getPemegang().getReg_spaj(), edit.getAgen().getLca_id(), policy.getMspo_policy_holder(), reas.getMsteInsured());
				
				//reas
				reas.setLstbId(1);
				reas.setLspdId(2);
				reasIndividu.prosesReasUnderwritingPas(reas, errors);
				
				
				//billing
				List lsDp = new ArrayList();
				uwDao.wf_ins_bill_pas(edit.getPemegang().getReg_spaj(), new Integer(1), new Integer(1), edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number(), lspdIdTemp, new Integer(1), lsDp, lus_id.toString(), policy, errors);
				
				//=================================================
//				uwManager.prosesAkseptasiPas(akseptasi, pas, user, 0, 0, "", errors);
				pas.setReg_spaj(edit.getPemegang().getReg_spaj());
				//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
				//akseptasi======================================
				int result = 0;
				try{
					Akseptasi akseptasi = new Akseptasi();
					
					akseptasi.setLiAksep(5);
					akseptasi.setLspdId(4);
					akseptasi.setLsspId(1);
					akseptasi.setLssaId(5);
					akseptasi.setCurrentUser(currentUser);
					akseptasi.setSpaj(pas.getReg_spaj());
					akseptasi.setInsuredNo(1);
					akseptasi.setLcaId(regionid.substring(0, 2));
					akseptasi.setLsbsId(edit.getDatausulan().getLsbs_id());
					akseptasi.setProses("1");
					int thn = 0;
					int bln = 0;
					String desc = "";
					result =  uwDao.prosesAkseptasiPas(akseptasi,thn,bln,desc,errors);
				}catch (Exception e){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}
				
				//=======================================================================================
				//transfer ke payment
				Transfer transfer = new Transfer();
				DecimalFormat fmt = new DecimalFormat ("000");
				Map mDataUsulan=uwDao.selectDataUsulan(edit.getPemegang().getReg_spaj());
				transfer.setSpaj(edit.getPemegang().getReg_spaj());
				transfer.setPModeId((Integer)mDataUsulan.get("LSCB_ID"));
				transfer.setBegDate((Date)mDataUsulan.get("MSTE_BEG_DATE"));
				transfer.setEndDate((Date)mDataUsulan.get("MSTE_END_DATE"));
				transfer.setMedical((Integer)mDataUsulan.get("MSTE_MEDICAL"));
				transfer.setLsbsId((Integer)mDataUsulan.get("LSBS_ID"));
				transfer.setLsdbsNumber((Integer)mDataUsulan.get("LSDBS_NUMBER"));
				transfer.setBisnisId(fmt.format(transfer.getLsbsId().intValue()));
				transfer.setPremium((Double)mDataUsulan.get("MSPR_PREMIUM"));
				transfer.setTsi((Double)mDataUsulan.get("MSPR_TSI"));
				//tertanggung
				Map mTertanggung2=uwDao.selectTertanggung(transfer.getSpaj());
				transfer.setInsuredNo((Integer)mTertanggung2.get("MSTE_INSURED_NO"));
				transfer.setMsteInsured((String)mTertanggung2.get("MCL_ID"));
				transfer.setMsagId((String)mTertanggung2.get("MSAG_ID"));
				transfer.setUmurTt((Integer)mTertanggung2.get("MSTE_AGE"));
				transfer.setCurrentUser((User) request.getSession().getAttribute("currentUser"));
				transfer.setLsDp(new ArrayList());
				transfer.setLiLama(0);
				Map mStatus2=uwDao.selectWfGetStatus(transfer.getSpaj(),transfer.getInsuredNo());
				transfer.setLiAksep((Integer)mStatus.get("LSSA_ID"));
				transfer.setLiReas((Integer)mStatus.get("MSTE_REAS"));
				transfer.setLspdId(new Integer(4));
				transfer.setLstbId(new Integer(1));
				transfer.setLiPosisi(4);//pembayaran
				policy=uwDao.selectDw1Underwriting(transfer.getSpaj(),transfer.getLspdId(),transfer.getLstbId());
				if(policy!=null){
					transfer.setMspoPolicyHolder(policy.getMspo_policy_holder());
					transfer.setNoPolis(policy.getMspo_policy_no());
					transfer.setInsPeriod(policy.getMspo_ins_period());
					transfer.setPayPeriod(policy.getMspo_pay_period());
					transfer.setLkuId(policy.getLku_id());
					transfer.setUmurPp(policy.getMspo_age());
					transfer.setLcaId(policy.getLca_id());
				}
				transfer.setPolicy(policy);
				//hasil=transferUw.prosesTransferPembayaran(transfer,1,errors);
				//=================================================================================
				// checklist
				if(("AP/BP,AP/BP ONLINE,DBD BP,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
					
					String mclIdNew = uwDao.selectMclIDPemegangPolis(edit.getPemegang().getReg_spaj());
					edit.getPersonal().setMcl_id(mclIdNew);
					
					commonDao.insertMstCompany(edit.getPersonal());
					
					Checklist cl = new Checklist();
					List<String> lcList = uwDao.selectLcChecklistWoMedis();
					
					int ktp_adm = pas.getMsp_cek_ktp();//lc_id=3
					int kk_adm = pas.getMsp_cek_kk();//lc_id=7
					int npwp_adm = pas.getMsp_cek_npwp();//lc_id=10
					int bukti_bayar_adm = pas.getMsp_cek_bukti_bayar();//lc_id=16
					int ktp_uw = pas.getMsp_cek_ktp_uw();//lc_id=3
					int kk_uw = pas.getMsp_cek_kk_uw();//lc_id=7
					int npwp_uw = pas.getMsp_cek_npwp_uw();//lc_id=10
					int bukti_bayar_uw = pas.getMsp_cek_bukti_bayar_uw();//lc_id=16
					
					Date tgl = commonDao.selectSysdate();
					
					for(int li = 0 ; li < lcList.size() ; li++){
						cl = new Checklist();
						cl.setReg_spaj(edit.getPemegang().getReg_spaj());
						cl.setLc_id(Integer.parseInt(lcList.get(li)));
						cl.setMc_no(li+1);
						cl.setLus_id_adm(pas.getLus_id());
						cl.setLus_id_uw(lus_id);
						cl.setTgl_adm(tgl);
						cl.setTgl_uw(tgl);
						cl.setLc_active(1);
						if(lcList.get(li).equals("3") && ktp_adm == 1){
							cl.setFlag_adm(1);
						}else if(lcList.get(li).equals("7") && kk_adm == 1){
							cl.setFlag_adm(1);
						}else if(lcList.get(li).equals("10") && npwp_adm == 1){
							cl.setFlag_adm(1);
						}else if(lcList.get(li).equals("16") && bukti_bayar_adm == 1){
							cl.setFlag_adm(1);
						}else{
							cl.setFlag_adm(0);
						}
						if(lcList.get(li).equals("3") && ktp_uw == 1){
							cl.setFlag_uw(1);
						}else if(lcList.get(li).equals("7") && kk_uw == 1){
							cl.setFlag_uw(1);
						}else if(lcList.get(li).equals("10") && npwp_uw == 1){
							cl.setFlag_uw(1);
						}else if(lcList.get(li).equals("16") && bukti_bayar_uw == 1){
							cl.setFlag_uw(1);
						}else{
							cl.setFlag_uw(0);
						}
						checklistDao.saveChecklistPas(cl);
					}
					
				}
				
				policy=uwDao.selectDw1Underwriting(reas.getSpaj(),4,1);
				
				edit.getPemegang().setMspo_policy_no(policy.getMspo_policy_no());
				pas.setMspo_policy_no(policy.getMspo_policy_no());
				String x ="";
				uwDao.updateKartuPas2(pas.getNo_kartu(), 1, edit.getPemegang().getReg_spaj());
				if("insert".equals(jenis_proses)){
					uwDao.insertPas(pas);
				}else{
					uwDao.updatePas(pas);
				}
			}else{
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
			uwDao.insertMstPositionSpajPas(pas.getLus_id().toString(), "TRANSFER DATA", pas.getMsp_fire_id(), 5);
		}
		catch (Exception e){
			logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		return edit;
	}
	
	public boolean wf_ins_bill(String spaj,Integer insured,Integer active,Integer lsbsId,Integer lsdbsNumber,Integer lspdId,Integer lstbId,List lsDp,
			String lusId,Policy policy,BindException err)throws DataAccessException{
		return uwDao.wf_ins_bill(spaj, insured, active, lsbsId, lsdbsNumber, lspdId, lstbId, lsDp, lusId, policy, err);
	}
	
	public void updateAutodebetNasabah(Rekening_auto_debet rekening) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Account_recur recur = new Account_recur();
		try {
			recur.setReg_spaj(rekening.getReg_spaj());
			recur.setMar_number(1);
			//if(rekening.getLbn_id().equals("")) recur.setLbn_id(rekening.getLsbp_id().substring(0,rekening.getLsbp_id().indexOf('-')));
			//else recur.setLbn_id(rekening.getLbn_id());
			recur.setLbn_id(rekening.getLbn_id());
			recur.setMar_jenis(rekening.getMar_jenis());
			recur.setMar_acc_no(rekening.getMrc_no_ac());
			recur.setMar_holder(rekening.getMar_holder());
			recur.setFlag_jn_tabungan(rekening.getFlag_jn_tabungan());
			recur.setMar_active(1);
			recur.setLus_id(rekening.getLus_id());
			recur.setFlag_autodebet_nb(rekening.getFlag_autodebet_nb());
			if(!rekening.getMar_expired().equals("")) recur.setMar_expired(df.parse(rekening.getMar_expired()));
			int updated = this.uwDao.updateAutodebetNasabah(recur);
			if(updated == 0) {
				this.bacDao.insertAutodebetNasabah(recur);
			}
			
			Rekening_client rek_client =  new Rekening_client();
			rek_client.setReg_spaj(rekening.getReg_spaj());
			rek_client.setLus_id(rekening.getLus_id());
			rek_client.setNotes("");
			rek_client.setMrc_no_ac(rekening.getMrc_no_ac());
			rek_client.setMrc_no_ac_lama(rekening.getMrc_no_ac_lama());
			rek_client.setKeterangan(rekening.getKeterangan());
			rek_client.setTypeRek(rekening.getTypeRek());
			
			this.uwDao.insertHistoryRekeningNasabah(rek_client);			
		}catch (Exception e) {
			logger.error("ERROR :", e);
		}
	}
	
	public List<Map> selectBank() {
		return uwDao.selectBank();
	}
	
	public List<Map> selectBankData(Integer jenis ) {
		return uwDao.selectBankData(jenis);
	}
	
	public String getBankPusat(String lbn_id) {
		return uwDao.getBankPusat(lbn_id);
	}	
	
	public Integer getFlagSpecial(String reg_spaj){
		return uwDao.getFlagSpecial(reg_spaj);
	}
	
	public List selectswineflu(String spaj) {
		return uwDao.selectswineflu(spaj);
	}
	
	public Integer selectMstEndorsAutoRider(String spaj){
		return uwDao.selectMstEndorsAutoRider(spaj);
	}
	
	public String selectCekSpajSebelumSurrender(String reg_spaj){
		return uwDao.selectCekSpajSebelumSurrender(reg_spaj);
	}
	
	public Integer countregbponline(){
		return uwDao.countregbponline();
	}
	
	public List<Pas> selectAllPasList(String msp_id, String lspd_id, String tipe, String kata, String pilter, String jenis, String jenisDist, String jenisQuery, Integer lus_admin, String lus_id, Map param){
		return uwDao.selectAllPasList(msp_id, lspd_id, tipe, kata, pilter, jenis, jenisDist, jenisQuery, lus_admin, lus_id, param);
	}
	
	public String selectTotalAllPasList(String msp_id, String lspd_id, String tipe, String kata, String pilter, String jenis, String jenisDist, String jenisQuery, Integer lus_admin, String lus_id, Map param){
		return uwDao.selectTotalAllPasList(msp_id, lspd_id, tipe, kata, pilter, jenis, jenisDist, jenisQuery, lus_admin, lus_id, param);
	}
	
	public String selectKodeAgentFromMstKusioner(String fire_id){
		return uwDao.selectKodeAgentFromMstKusioner(fire_id);
	}
	
	public List<Map> selectStatusPasList(String msp_id, String reg_id){
		return uwDao.selectStatusPasList(msp_id, reg_id);
	}
	
	public List<Tmms> selectFreePaTmmsList(String tmms_id, String msag_id, String tipe, String kata, String pilter, Map param){
		return uwDao.selectFreePaTmmsList(tmms_id, msag_id, tipe, kata, pilter, param);
	}
	
	public String selectTotalFreePaTmmsList(String tmms_id, String msag_id, String tipe, String kata, String pilter){
		return uwDao.selectTotalFreePaTmmsList(tmms_id, msag_id, tipe, kata, pilter);
	}
	
	public Integer selectGetPasIdFromFireId(String fireId){
		return uwDao.selectGetPasIdFromFireId(fireId);
	}
	
	public List<Pas> selectViewerPasList(String msp_id, String lspd_id, String tipe, String kata, String pilter, String jenis){
		return uwDao.selectViewerPasList(msp_id, lspd_id, tipe, kata, pilter, jenis);
	}
	
	public List selectFilterSpajSimple(String posisi, String tipe, String kata, String pilter, Integer jn_bank, String tgl_lahir, String jenis) {
		return this.uwDao.selectFilterSpajSimple(posisi, tipe, kata, pilter, jn_bank, tgl_lahir, jenis);
	}
	
	public List selectgutriSimple(int posisi, String tipe, String kata, String pilter, String jenis) {
		return this.uwDao.selectgutriSimple(posisi, tipe, kata, pilter, jenis);
	}
	
	public List selectgutri_simple_valid(int posisi, String tipe, String kata, String pilter,Integer lus_id, String jenis) {
		return this.uwDao.selectgutri_simple_valid(posisi, tipe, kata, pilter,lus_id, jenis);
	}
	
	public List selectgutripas(int posisi, String tipe, String kata, String cari, String pilter, String tgl_lahir, String telp, String sumber) {
		return this.uwDao.selectgutripas(posisi, tipe, kata, cari, pilter, tgl_lahir, telp, sumber);
	}
	
	public List selectReportPas(){
		return uwDao.selectReportPas();
	}
	
	public Date selectSysdateTruncated(int daysAfter) throws DataAccessException {
		return commonDao.selectSysdateTruncated(daysAfter);
	}
	
	public List selectFurtherRequirementPas(int lssa_id, boolean isEmailRequired, String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12){
		return uwDao.selectFurtherRequirementPas(lssa_id, isEmailRequired, yearbefore,month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12);
	}
	
	public String selectCekPin(String pin, Integer flag_active){
		return uwDao.selectCekPin(pin, flag_active);
	}
	
	public String selectCekNoKartu(String no_kartu, String produk, Integer flag_active){
		return uwDao.selectCekNoKartu(no_kartu, produk, flag_active);
	}
	
	public List selectReportFireList(){
		return uwDao.selectReportFireList();
	}
	
	public List selectReportPasList(){
		return uwDao.selectReportPasList();
	}
	
	public List selectReportBatalPasList(){
		return uwDao.selectReportBatalPasList();
	}
	
	public List selectReportPesertaPasList(){
		return uwDao.selectReportPesertaPasList();
	}
	
	public List selectReportAksepPasList(){
		return uwDao.selectReportAksepPasList();
	}
	
	public List selectReportPasEmailList(int lus_id){
		return uwDao.selectReportPasEmailList(lus_id);
	}
	
	public String selectPasEmailAdminCabang(String no_kartu){
		return uwDao.selectPasEmailAdminCabang(no_kartu);
	}
	
	public Map selectEmailAgen2(String msag_id){
		return uwDao.selectEmailAgen2(msag_id);
	}
	
	public List<Map> selectDetailAgen(String msag_id){
		return uwDao.selectDetailAgen(msag_id);
	}
	
	public Pas updatePas(Pas pas){
		pas.setStatus(0);
		try{
			if(pas.getMsp_fire_id() == null){
				Long intIDCounter = (Long) this.bacDao.select_counter(113, "01");
					int intID = intIDCounter.intValue();
					Calendar tgl_sekarang = Calendar.getInstance(); 
					Integer inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
					if (intIDCounter.longValue() == 0)
					{
						intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 1000000));
					}else{
						Integer inttgl1=new Integer(uwDao.selectGetCounterMonthYear(113, "01"));
	
						if (inttgl1.intValue() != inttgl2.intValue())
						{
							intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
							//ganti dengan tahun skarang
							uwDao.updateCounterMonthYear(inttgl2.toString(), 113, "01");
							//reset nilai counter dengan 0
							intID = 0;
							uwDao.updateMstCounter2("0", 113, "01");
							//logger.info("update mst counter start di tahun baru");
						}else{
							intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000"))+intIDCounter);
						}
					}
					
					//--------------------------------------------
					Long intFireId = new Long(intIDCounter.longValue() + 1);
					intID = intID + 1;
					uwDao.updateMstCounter3(intID, 113, "01");
					String mspFireId = intFireId.toString();
					pas.setMsp_fire_id(mspFireId);
				//====================
			}
			uwDao.updatePas(pas);
			if(pas.getMsp_pas_accept_date() != null){
				uwDao.insertMstPositionSpajPas(pas.getLus_id_uw_pas().toString(), "ACCEPT DATA", pas.getMsp_fire_id(), 5);
			}else{
				uwDao.insertMstPositionSpajPas(pas.getLus_id().toString(), "EDIT DATA", pas.getMsp_fire_id(), 5);
			}
		}catch(Exception e){
			logger.info(e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			pas.setStatus(1);
		}
		return pas;
	}
	
	public Pas updatePas(Pas pas, String action){
		pas.setStatus(0);
		try{
			if(pas.getMsp_fire_id() == null){
				Long intIDCounter = (Long) this.bacDao.select_counter(113, "01");
					int intID = intIDCounter.intValue();
					Calendar tgl_sekarang = Calendar.getInstance(); 
					Integer inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
					if (intIDCounter.longValue() == 0)
					{
						intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 1000000));
					}else{
						Integer inttgl1=new Integer(uwDao.selectGetCounterMonthYear(113, "01"));
	
						if (inttgl1.intValue() != inttgl2.intValue())
						{
							intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
							//ganti dengan tahun skarang
							uwDao.updateCounterMonthYear(inttgl2.toString(), 113, "01");
							//reset nilai counter dengan 0
							intID = 0;
							uwDao.updateMstCounter2("0", 113, "01");
							//logger.info("update mst counter start di tahun baru");
						}else{
							intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000"))+intIDCounter);
						}
					}
					
					//--------------------------------------------
					Long intFireId = new Long(intIDCounter.longValue() + 1);
					intID = intID + 1;
					uwDao.updateMstCounter3(intID, 113, "01");
					String mspFireId = intFireId.toString();
					pas.setMsp_fire_id(mspFireId);
				//====================
			}
			uwDao.updatePas(pas);
			if("aksep".equals(action)){
				uwDao.insertMstPositionSpajPas(pas.getLus_id_uw_fire().toString(), "ACCEPT DATA FIRE", pas.getMsp_fire_id(), 5);
			}else if("resend".equals(action)){
				uwDao.insertMstPositionSpajPas(pas.getLus_id_uw_fire().toString(), "RESEND DATA FIRE", pas.getMsp_fire_id(), 5);
			}else if("reject".equals(action)){
				uwDao.insertMstPositionSpajPas(pas.getLus_id_uw_fire().toString(), "REJECT DATA FIRE : "+pas.getMsp_fire_fail_desc(), pas.getMsp_fire_id(), 5);
			}
		}catch(Exception e){
			logger.info(e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			pas.setStatus(1);
		}
		return pas;
	}
	
	public Pas updateAgenPas(Pas pas, String action, String new_msag_id, Integer lus_id){
		pas.setStatus(0);
		try{
			if("leader".equals(action)){
				String desc = "UBAH KODE AGEN "+pas.getMsag_id()+"(TIDAK AKTIF) KE LEADERNYA "+new_msag_id;
				pas.setMsag_id(new_msag_id);
				uwDao.updatePas(pas);
				uwDao.insertMstPositionSpajPas(lus_id.toString(), desc, pas.getMsp_fire_id(), 5);
			}
		}catch(Exception e){
			logger.info(e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			pas.setStatus(1);
		}
		return pas;
	}
	
	public int rejectSelectedFire(String[] mspIdList, int lus_id, String lus_full_name, String alasan, String jenisDist, Date nowDate){
		int r = 0;
		try{
			for(int i = 0 ; i < mspIdList.length ; i++){
				List<Pas> pas = new ArrayList<Pas>();
				Pas p = new Pas();
				pas = selectAllPasList(mspIdList[i], "2", null, null, null, "fire", jenisDist, "other",null,null,null);
				p = pas.get(0);
				p.setLus_id(lus_id);
				p.setLus_id_uw_fire(lus_id);
				p.setLus_login_name(lus_full_name);
				p.setMsp_fire_export_flag(6);
				p.setMsp_fire_fail_desc(alasan);
				p.setMsp_fire_fail_date(nowDate);
				uwDao.updatePas(p);
			}
		}catch(Exception e){
			r = 1;
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		return r;
	}
	
	public Pas batalPas(Pas pas){
		pas.setStatus(0);
		try{
			//update pas
			pas.setLspd_id(95);
			//pas.setLssp_id(8);
			uwDao.updatePas(pas);
			uwDao.updateKartuPas1(pas.getNo_kartu(), 2, null, null);
			uwDao.insertMstPositionSpajPas(pas.getLus_id_uw_batal().toString(), "PEMBATALAN", pas.getMsp_fire_id(), 5);
		}catch(Exception e){
			pas.setStatus(1);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		return pas;
	}
	
	public int updateNoKartuPas(String msp_id, String no_kartu, String lus_id){
		int result = 0;
		
		try{
			List<Pas> pasList = uwDao.selectAllPasList(msp_id, null, null, null, null, null, null, null,null,null,null);
			Pas pas = pasList.get(0);
			String lsdbs_number_no_kartu = "x";
			if(pas.getNo_kartu() != null){
				lsdbs_number_no_kartu = uwDao.selectCekNoKartu(no_kartu, "", 0);
				if(lsdbs_number_no_kartu == null)lsdbs_number_no_kartu = "x";
			}
			if(!lsdbs_number_no_kartu.equals("0" + pas.getProduk())){
				return 2;
			}
			
			Map params = new HashMap();
			params.put("msp_id", msp_id);
			params.put("no_kartu", no_kartu);
			params.put("reg_spaj", pas.getReg_spaj());
			uwDao.updateKartuPas1(no_kartu, 1, pas.getReg_spaj(), null);
			uwDao.updateNoKartuMstPasSms(params);
			uwDao.updateNasabahAccMstPolicy(params);
			if(pas.getReg_spaj() == null)pas.setReg_spaj("");		
			Date sysdate = commonDao.selectSysdate();
			if("".equals(pas.getReg_spaj())){
				uwDao.insertMstPositionSpajPas(lus_id, "PERGANTIAN NO KARTU DARI "+pas.getNo_kartu()+" KE "+no_kartu, pas.getMsp_fire_id(), 5);
			}else{
			  //uwDao.insertMstPositionSpaj(pas.getLus_id().toString(), "PERGANTIAN NO KARTU DARI "+pas.getNo_kartu()+" KE "+no_kartu, pas.getReg_spaj(), 5);
				uwDao.insertMstPositionSpajPas(lus_id, "PERGANTIAN NO KARTU DARI "+pas.getNo_kartu()+" KE "+no_kartu, pas.getMsp_fire_id(), 5);
			    uwDao.insertLst_ulangan(pas.getReg_spaj(), sysdate, "UBAH DATA", 1, lus_id, "PERGANTIAN NO KARTU DARI "+pas.getNo_kartu()+" KE "+no_kartu);			 
			}			
		}catch(Exception e){
			result = 1;
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return result;
	}
	
	public String selectNoBlankoPas(String reg_spaj, String no_kartu){
		return bacDao.selectNoBlankoPas(reg_spaj, no_kartu);
	}
	
	public int updateNoKartu(String reg_spaj, String pas_id, String no_kartu_lama, String no_kartu, String lus_id, String jenis){
		int result = 0;
		
		try{
			if("1".equals(jenis) || "2".equals(jenis)){
				Map params = new HashMap();
				params.put("no_kartu", no_kartu);
				params.put("reg_spaj", reg_spaj);
				params.put("msp_id", pas_id);
				uwDao.updateKartuPas1(no_kartu, 1, reg_spaj, null);
				uwDao.updateNoKartuMstPasSms(params);
				uwDao.updateNasabahAccMstPolicy(params);
				String mspo_no_blanko = bacDao.selectNoBlankoPas(reg_spaj, no_kartu);
				params.put("no_blanko", mspo_no_blanko);
				uwDao.updateNoBlankoMstPolicy(params);
			}
			
			//insert ke simcard
			/** 9. Proses Cek dan generate Simas Card **/
			List daftarSebelumnya = uwDao.selectSimasCard(reg_spaj); //daftar simas card sebelumnya yang masih aktif
			
			String polis = uwDao.selectNoPolisFromSpaj(reg_spaj);
			polis = FormatString.nomorPolis(polis);
			
			if(!StringUtil.isEmpty(polis)){
				int jumlahPolis = uwDao.selectJumlahPolis(reg_spaj).size(); //ambil jumlah polis yang dia punya
				double totalPremi = uwDao.selectTotalPremi(reg_spaj); //total semua premi nya
				
				String notes = "";
				int jenis_pemegang = 0; //apabila pemegang new business
				int flag_aktif = 1;
				
				List isAgen = uwDao.selectIsSimasCardClientAnAgent(reg_spaj); 
				if(isAgen.size()>0) { //apabila pemegang seorang agent
					jenis_pemegang=5; 
					String msag_id = (String) ((Map) isAgen.get(0)).get("MSAG_ID");
					
					if(uwDao.selectIsAgentCertified(msag_id)==0) { //apabila agen TIDAK certified
						notes = "[AGEN BELUM CERTIFIED] ";
						flag_aktif=0;
					}else{ //apabila agen certified
						notes = "[AGEN] ";
					}
				}
					
				if(!isAgen.isEmpty()){
					if(daftarSebelumnya.isEmpty()) { //kalo gak ada simas card sebelumnya
					//tidak print -> flag_print=4, print -> flag_print=0
						if("1".equals(jenis)){
//							if(!businessId.equals("187")){
							if(uwDao.selectJenisTerbitPolis(reg_spaj)==1) //apabila softcopy, kasih notes aja
								uwDao.insertSimasCard(jenis_pemegang, reg_spaj, polis, lus_id, 
										jumlahPolis, totalPremi, 0, notes + "POLIS SOFTCOPY", flag_aktif);  
							else 
								uwDao.insertSimasCard(jenis_pemegang, reg_spaj, polis, lus_id, 
										jumlahPolis, totalPremi, 0, "POLIS HARDCOPY", flag_aktif); //belum print
						}
					}
				}
				//=====================
				
				if(reg_spaj == null)reg_spaj = "";		
				Date sysdate = commonDao.selectSysdate();
				if(!"".equals(reg_spaj)){
//					if(!StringUtils.isEmpty(reg_id_pas)){
//						uwDao.insertMstPositionSpajPas(lus_id, "PENGISIAN NO KARTU KE "+no_kartu, reg_id_pas, 5);
//					}
					uwDao.insertMstPositionSpaj(lus_id, "PERGANTIAN NO KARTU DARI NO:'"+no_kartu_lama+"' KE NO:'"+no_kartu+"'", reg_spaj, 5);
				}
			}else{
				result = 1;
			}
		}catch(Exception e){
			result = 1;
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return result;
	}
	
	public Pas updatePas1(Pas pas, Integer lus_id){
		pas.setStatus(0);
		try{
		// update counter	
			if(pas.getMsp_fire_id() == null){
				Long intIDCounter = (Long) this.bacDao.select_counter(113, "01");
					int intID = intIDCounter.intValue();
					Calendar tgl_sekarang = Calendar.getInstance(); 
					Integer inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
					if (intIDCounter.longValue() == 0)
					{
						intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 1000000));
					}else{
						Integer inttgl1=new Integer(uwDao.selectGetCounterMonthYear(113, "01"));
	
						if (inttgl1.intValue() != inttgl2.intValue())
						{
							intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
							//ganti dengan tahun skarang
							uwDao.updateCounterMonthYear(inttgl2.toString(), 113, "01");
							//reset nilai counter dengan 0
							intID = 0;
							uwDao.updateMstCounter2("0", 113, "01");
							//logger.info("update mst counter start di tahun baru");
						}else{
							intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000"))+intIDCounter);
						}
					}
					
					//--------------------------------------------
					Long intFireId = new Long(intIDCounter.longValue() + 1);
					intID = intID + 1;
					uwDao.updateMstCounter3(intID, 113, "01");
					String mspFireId = intFireId.toString();
					pas.setMsp_fire_id(mspFireId);
				//====================
			}
			//update pas
			pas.setLspd_id(2);
			pas.setLssp_id(10);
			pas.setMsp_kode_sts_sms("00");
			uwDao.updatePas(pas);
			uwDao.updateBukuPas(pas.getPin(), 1);
			uwDao.insertMstPositionSpajPas(lus_id.toString(), "TRANSFER DATA", pas.getMsp_fire_id(), 5);
//			uwDao.updateKartuPas1(pas.getNo_kartu(), 1, null);
		}catch(Exception e){
			pas.setStatus(1);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return pas;
	}
	
	public Cmdeditbac prosesBisnisSimple(HttpServletRequest request, String jenis_proses, Pas pas, Errors err,String keterangan, User currentUser, BindException errors) throws ServletException,IOException,Exception
	{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		Cmdeditbac edit= new Cmdeditbac();
		int umurPp=0;
		try{
			Integer lus_id = Integer.parseInt(currentUser.getLus_id());
		//	if(request.getParameter("kata")!=null){
				Date beg_date = pas.getMsp_pas_beg_date();//commonDao.selectSysdateTrunc();
				Date end_date = pas.getMsp_pas_end_date();//commonDao.selectSysdateTrunc();
//				Date end_fire_date = commonDao.selectSysdateTrunc();
//				end_date.setYear(end_date.getYear()+1);
//				end_date.setDate(end_date.getDate()-1);
				//pas.setMsp_pas_beg_date(beg_date);
				//pas.setMsp_pas_end_date(end_date);
//				pas.setMsp_fire_beg_date(beg_date);
//				end_fire_date.setMonth(beg_date.getMonth()+6);
//				end_fire_date.setDate(end_fire_date.getDate()-1);
//				pas.setMsp_fire_end_date(end_fire_date);
				pas.setLus_id(lus_id);
				pas.setLus_login_name(currentUser.getLus_full_name());
				pas.setDist("05");
				pas.setLspd_id(99);
				
				//tgl beg date
				int tanggal1= beg_date.getDate();
				int bulan1 = beg_date.getMonth()+1;
				int tahun1 = beg_date.getYear()+1900;
				
				//tgl lahir ttg
				int tanggal2=pas.getMsp_date_of_birth().getDate();
				int bulan2=pas.getMsp_date_of_birth().getMonth()+1;
				int tahun2=pas.getMsp_date_of_birth().getYear()+1900;
				
				//tgl lahir pp
				int tanggal3=pas.getMsp_pas_dob_pp().getDate();
				int bulan3=pas.getMsp_pas_dob_pp().getMonth()+1;
				int tahun3=pas.getMsp_pas_dob_pp().getYear()+1900;
				
				//hit umur ttg, pp, pic
				f_hit_umur umr= new f_hit_umur();
				int umur =  umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
				umurPp =  umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
				
				pas.setMsp_age(umur);
				
		//	}
			
			CheckSum checkSum = new CheckSum();
			edit.setPemegang(new Pemegang());
			edit.setPersonal(new Personal());
			edit.setContactPerson(new ContactPerson());
			edit.setTertanggung(new Tertanggung());
			edit.setDatausulan(new Datausulan());
			edit.setAgen(new Agen());
			edit.setAddressbilling(new AddressBilling());
			edit.setRekening_client(new Rekening_client());
			edit.setAccount_recur(new Account_recur());
			
			// agen
			Map agentMap = bacDao.selectagenpenutup(pas.getMsag_id());
			String regionid = (String) agentMap.get("REGIONID");
			edit.getAgen().setMsag_id((String) agentMap.get("ID"));
			edit.getAgen().setMcl_first((String) agentMap.get("NAMA"));
			edit.getAgen().setKode_regional(regionid);
			edit.getAgen().setLca_id(regionid.substring(0, 2));
			edit.getAgen().setLwk_id(regionid.substring(2, 4));
			edit.getAgen().setLsrg_id(regionid.substring(4, 6));
			if(edit.getAgen().getLca_id() == "58"){
				edit.getAgen().setLrb_id(pas.getLrb_id().toString());
			}
			//referensi(tambang emas) agar jenis_ref tidak null
			edit.getAgen().setJenis_ref(2);
			
			edit.getContactPerson().setNama_lengkap("");
			
			// pemegang
			edit.getPemegang().setMcl_id(pas.getMcl_id_pp());
			edit.getPemegang().setMspo_plan_provider(pas.getMspo_plan_provider());
			edit.getPemegang().setNo_hp(pas.getMsp_mobile());
			edit.getPemegang().setNo_hp2(pas.getMsp_mobile2());
			edit.getPemegang().setMcl_first(pas.getMsp_pas_nama_pp());
			edit.getPemegang().setMspe_no_identity(pas.getMsp_identity_no());
			edit.getPemegang().setMspe_date_birth(pas.getMsp_pas_dob_pp());
			edit.getPemegang().setMspe_email(pas.getMsp_pas_email());
			edit.getPemegang().setEmail(pas.getMsp_pas_email());
			edit.getPemegang().setAlamat_rumah(pas.getMsp_address_1());
			edit.getPemegang().setKota_rumah(pas.getMsp_city());
			edit.getPemegang().setKd_pos_rumah(pas.getMsp_postal_code());
			edit.getPemegang().setMkl_red_flag(0);
//			if(pas.getMsag_id_pp() != null){
//				edit.getPemegang().setMspo_ao(pas.getMsag_id_pp());
//			}else{
//				edit.getPemegang().setMspo_ao(pas.getKode_ao());
//			}
			if(("AP/BP,DBD BP,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
				edit.getPemegang().setMspo_ao(pas.getMsag_id_pp());
			}else{
				edit.getPemegang().setMspo_ao(pas.getMsag_id());
			}
//			else{
//				edit.getPemegang().setMspo_ao(pas.getKode_ao());
//			}
			edit.getPemegang().setMspo_pribadi(pas.getPribadi());
			edit.getPemegang().setMspe_sex(pas.getMsp_sex_pp());
			edit.getPemegang().setReg_spaj(pas.getReg_spaj());
			edit.getPemegang().setMspo_policy_no(pas.getMspo_policy_no());
			edit.getPemegang().setMspo_spaj_date(commonDao.selectSysdate());
			edit.getPemegang().setMste_tgl_recur(pas.getMsp_tgl_valid());
			edit.getPemegang().setLus_id(pas.getLus_id());
			edit.getPemegang().setMspo_age(umurPp);
			edit.getPemegang().setUsiapp(umurPp);
			edit.getPemegang().setMspe_place_birth(pas.getMsp_pas_tmp_lhr_pp());
			edit.getPemegang().setArea_code_rumah(pas.getMsp_area_code_rumah());
			if(pas.getLside_id() != null){
				edit.getPemegang().setLside_id(pas.getLside_id());
			}
			edit.getPemegang().setTelpon_rumah(pas.getMsp_pas_phone_number());
			edit.getPemegang().setLsre_id(pas.getLsre_id());
			edit.getPemegang().setMkl_tujuan("");
			edit.getPemegang().setTujuana("");
			edit.getPemegang().setMkl_pendanaan("");
			edit.getPemegang().setDanaa("");
			edit.getPemegang().setMkl_smbr_penghasilan("");
			edit.getPemegang().setShasil("");
			edit.getPemegang().setDanaa2("");
			edit.getPemegang().setMkl_kerja("");
			edit.getPemegang().setKerjaa("");
			edit.getPemegang().setKerjab("");
			edit.getPemegang().setMkl_industri("");
			edit.getPemegang().setIndustria("");
			edit.getPemegang().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
			edit.getPemegang().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
			edit.getPemegang().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
			
			if(pas.getMsag_id_pp() == null)pas.setMsag_id_pp("");
			//if(!"".equals(pas.getMsag_id_pp())){
			if(("AP/BP,AP/BP ONLINE,DBD AGENCY,DBD BP, DBD MALL,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
				edit.getPemegang().setLsne_id(pas.getMsp_warga());
				edit.getPemegang().setMspe_sts_mrt(pas.getMsp_status_nikah().toString());
				edit.getPemegang().setLsag_id(pas.getLsag_id());
				edit.getPemegang().setMcl_agama(pas.getMsp_agama());
				edit.getPemegang().setLsed_id(pas.getMsp_pendidikan());
				edit.getPemegang().setMkl_kerja(pas.getMsp_occupation());
				if(pas.getMsp_occupation_else() == null)pas.setMsp_occupation_else("");
				if(pas.getMsp_occupation_else() == null){
					edit.getPemegang().setKerjaa("");
				}else{
					edit.getPemegang().setKerjaa(pas.getMsp_occupation_else());
				}
				
				if("AP/BP".equals(pas.getJenis_pas()) || "AP/BP ONLINE".equals(pas.getJenis_pas())){
					if(pas.getMsp_company_jabatan() == null)pas.setMsp_company_jabatan("");
					edit.getPemegang().setKerjab(pas.getMsp_company_jabatan());
					edit.setPersonal(new Personal());
					//set mcl id terbaru ada dibawah
					//edit.getPersonal().setMcl_id(mcl_id);
					edit.getPersonal().setMcl_first(pas.getMsp_company_name());
//					edit.getPersonal().setLju_id(pas.getMsp_company_usaha());
					if(pas.getLju_id() != null){
						edit.getPersonal().setLju_id(pas.getLju_id());
					}
					edit.getPemegang().setAlamat_kantor(pas.getMsp_company_address());
					edit.getPemegang().setKd_pos_kantor(pas.getMsp_company_postal_code());
					edit.getPemegang().setTelpon_kantor(pas.getMsp_company_phone_number());
				}
				
			}
			
			// address billing
			edit.getAddressbilling().setNo_hp(pas.getMsp_mobile());
			edit.getAddressbilling().setNo_hp2(pas.getMsp_mobile2());
			edit.getAddressbilling().setMsap_contact(pas.getMsp_full_name());
			edit.getAddressbilling().setMsap_address(pas.getMsp_address_1());
			edit.getAddressbilling().setMsap_area_code1(pas.getMsp_area_code_rumah());
			edit.getAddressbilling().setKota(pas.getMsp_city());
			edit.getAddressbilling().setMsap_zip_code(pas.getMsp_postal_code());
			edit.getAddressbilling().setE_mail(pas.getMsp_pas_email());
			edit.getAddressbilling().setReg_spaj(pas.getReg_spaj());
			edit.getAddressbilling().setRegion(regionid);
			edit.getAddressbilling().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
			edit.getAddressbilling().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
			edit.getAddressbilling().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
			
			// rekening client
			if("DBD BP".equals(pas.getJenis_pas())){
				edit.getRekening_client().setNotes("DBD BP");
			}else if("DBD AGENCY".equals(pas.getJenis_pas())){
				edit.getRekening_client().setNotes("DBD AGENCY");
			}else if("DBD MALL".equals(pas.getJenis_pas())){
				edit.getRekening_client().setNotes("DBD MALL");
			}else if("INDIVIDU".equals(pas.getJenis_pas()) || "AP/BP".equals(pas.getJenis_pas()) || "AP/BP ONLINE".equals(pas.getJenis_pas())  
					|| "MALLINSURANCE".equals(pas.getJenis_pas()) || "PAS BPD".equals(pas.getJenis_pas())){
				edit.getRekening_client().setNotes("PAS");
			}else{//simple bisnis
				edit.getRekening_client().setNotes(pas.getJenis_pas());
			}
			//edit.getRekening_client().setNotes("PAS");
			// tabungan ataupun kartu kredit, kedua rekening harus diisi & untuk tunai, rekening tabungan harus diisi
			
			if("".equals(pas.getLsbp_id()) || "0".equals(pas.getLsbp_id()) || pas.getLsbp_id() == null){
				pas.setLsbp_id("0");
				pas.setMsp_no_rekening("-");
				pas.setMsp_rek_cabang("-");
				pas.setMsp_rek_kota("-");
				pas.setMsp_rek_nama("-");
			}
			
			edit.getRekening_client().setLsbp_id(pas.getLsbp_id());
			//if(pas.getMsp_flag_cc() == 2){//tabungan
				edit.getRekening_client().setMrc_nama(pas.getMsp_rek_nama().toUpperCase());
				edit.getRekening_client().setMrc_cabang(pas.getMsp_rek_cabang().toUpperCase());
				edit.getRekening_client().setMrc_kota(pas.getMsp_rek_kota().toUpperCase());
			//}
			edit.getRekening_client().setMrc_jn_nasabah(0);//none
			edit.getRekening_client().setMrc_kurs("01");//rupiah
			edit.getRekening_client().setNo_account(pas.getMsp_no_rekening());
			edit.getRekening_client().setMrc_no_ac(pas.getMsp_no_rekening());
			edit.getRekening_client().setMrc_no_ac_lama(pas.getMsp_no_rekening());
			edit.getRekening_client().setMrc_jenis(2);// rek client
			
			// account recur
			edit.getAccount_recur().setLbn_id(pas.getLsbp_id_autodebet());
			edit.getAccount_recur().setLus_id(pas.getLus_id());
			edit.getAccount_recur().setMar_number(1);
			edit.getAccount_recur().setMar_acc_no(pas.getMsp_no_rekening_autodebet());
			edit.getAccount_recur().setMar_holder(pas.getMsp_rek_nama_autodebet());
			edit.getAccount_recur().setMar_expired(pas.getMsp_tgl_valid());
			edit.getAccount_recur().setMar_active(1);
			
			// data usulan
//			String lsdbs_number = uwDao.selectCekPin(pas.getPin(), 1);
//			if(lsdbs_number == null)lsdbs_number = "x";
			String lsdbs_number = "x";
			/*if(pas.getPin() != null){
				lsdbs_number = uwDao.selectCekPin(pas.getPin(), 1);
			}else{
				lsdbs_number = uwDao.selectCekNoKartu(pas.getNo_kartu(), "", 1);
			}*/
			
			if(pas.getNo_kartu() != null){
				lsdbs_number = uwDao.selectCekNoKartu(pas.getNo_kartu(), "", 1);
			}else{
				lsdbs_number = uwDao.selectCekPin(pas.getPin(), 1);
			}
			
			if("AP/BP".equals(pas.getJenis_pas()) || "AP/BP ONLINE".equals(pas.getJenis_pas())){
				lsdbs_number = "05";
			}else if("PAS BPD".equals(pas.getJenis_pas())){
				lsdbs_number = "06";
			}
//			if(!"".equals(pas.getMsag_id_pp())){
//				if(pas.getProduk() == 5){
//				  lsdbs_number = "05";
//				} else {
//			      lsdbs_number = "06";
//				}
//			}
			
			if("DBD BP".equals(pas.getJenis_pas())){
				lsdbs_number = "01";
			}else if("DBD AGENCY".equals(pas.getJenis_pas())){
				lsdbs_number = "02";
			}else if("DBD MALL".equals(pas.getJenis_pas())){
				lsdbs_number = "03";
			}
			int mspo_pay_period = 1;
		
			Double mspr_tsi = null;
			Double mspr_premium = null;
			
			if("DBD BP".equals(pas.getJenis_pas()) || "DBD AGENCY".equals(pas.getJenis_pas())){
				if("50000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(2000000);
				}else if("100000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(4000000);
				}else if("150000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(6000000);
				}else if("200000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(8000000);
				}else if("250000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(10000000);
				}else{
					mspr_tsi = new Double(0);
				}
				mspr_premium = new Double(pas.getMsp_premi());
			}else if("DBD MALL".equals(pas.getJenis_pas()) ){
				if("100000".equals(pas.getMsp_premi())){
					mspr_tsi = new Double(2000000);
				}else{
					mspr_tsi = new Double(0);
				}
				mspr_premium = new Double(pas.getMsp_premi());
			}else if("INDIVIDU".equals(pas.getJenis_pas()) || "AP/BP".equals(pas.getJenis_pas()) || "AP/BP ONLINE".equals(pas.getJenis_pas()) 
						|| "MALLINSURANCE".equals(pas.getJenis_pas()) || "PAS BPD".equals(pas.getJenis_pas())){
				if("01".equals(lsdbs_number)){
					mspr_tsi = new Double(100000000);
				}else if("02".equals(lsdbs_number)){
					mspr_tsi = new Double(50000000);
				}else if("03".equals(lsdbs_number)){
					mspr_tsi = new Double(100000000);
				}else if("04".equals(lsdbs_number)){
					mspr_tsi = new Double(200000000);
				}else if("05".equals(lsdbs_number)){
					mspr_tsi = new Double(50000000);
				}else if("06".equals(lsdbs_number)){
					mspr_tsi = new Double(50000000);
				}else{
					mspr_tsi = new Double(100000000);
				}
				if("01".equals(lsdbs_number) && pas.getLscb_id() == 3){
					mspr_premium = new Double(150000);
				}else if("01".equals(lsdbs_number) && pas.getLscb_id() == 6){
					mspr_premium = new Double(15000);
				}else if("01".equals(lsdbs_number) && pas.getLscb_id() == 1){
					mspr_premium = new Double(150000 * 0.27);
				}else if("01".equals(lsdbs_number) && pas.getLscb_id() == 2){
					mspr_premium = new Double(150000 * 0.525);
				}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 3){
					mspr_premium = new Double(300000);
				}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 6){
					mspr_premium = new Double(30000);
				}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 1){
					mspr_premium = new Double(300000 * 0.27);
				}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 2){
					mspr_premium = new Double(300000 * 0.525);
				}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 3){
					mspr_premium = new Double(500000);
				}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 6){
					mspr_premium = new Double(50000);
				}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 1){
					mspr_premium = new Double(500000 * 0.27);
				}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 2){
					mspr_premium = new Double(500000 * 0.525);
				}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 3){
					mspr_premium = new Double(900000);
				}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 6){
					mspr_premium = new Double(90000);
				}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 1){
					mspr_premium = new Double(900000 * 0.27);
				}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 2){
					mspr_premium = new Double(900000 * 0.525);
				}else if("05".equals(lsdbs_number)){
					mspr_premium = new Double(74000);
				}else if("06".equals(lsdbs_number)){
					mspr_premium = new Double(74000);
				} else{
					mspr_premium = new Double(0);
				}
			}else{// simple bisnis
				mspr_tsi = new Double(pas.getMsp_up());
				mspr_premium = new Double(pas.getMsp_premi());
			}
			edit.getDatausulan().setIsBungaSimponi(0);
			edit.getDatausulan().setIsBonusTahapan(0);
			edit.getDatausulan().setLssp_id(10);
			edit.getDatausulan().setLssa_id(5);
			edit.getDatausulan().setLspd_id(4);
			edit.getDatausulan().setMspo_age(umurPp);
			edit.getDatausulan().setMspo_spaj_date(commonDao.selectSysdate());
			edit.getDatausulan().setMspo_beg_date(pas.getMsp_pas_beg_date());
			edit.getDatausulan().setMspo_end_date(pas.getMsp_pas_end_date());
			edit.getDatausulan().setMste_medical(0);
			edit.getDatausulan().setLscb_id(pas.getLscb_id());
			edit.getDatausulan().setMspr_tsi(mspr_tsi);
			edit.getDatausulan().setMspr_premium(mspr_premium);
			edit.getDatausulan().setMspr_discount(new Double(0));
			edit.getDatausulan().setMste_flag_cc(pas.getMsp_flag_cc());// rek client = 0.tunai, 2.tabungan, 1.CC
			edit.getDatausulan().setFlag_worksite(0);
			//edit.getDatausulan().setFlag_account(pas.getMsp_flag_cc());
			edit.getDatausulan().setFlag_account(2);// 0 untuk umum  1 untuk account recur 2 untuk rek client 3 untuk account recur dan rek client
			edit.getDatausulan().setKode_flag(0);//default
			edit.getDatausulan().setMspo_beg_date(pas.getMsp_pas_beg_date());
			edit.getDatausulan().setMspo_end_date(pas.getMsp_pas_end_date());
			edit.getDatausulan().setMste_beg_date(pas.getMsp_pas_beg_date());
			edit.getDatausulan().setMste_end_date(pas.getMsp_pas_end_date());
			edit.getDatausulan().setMspo_ins_period(1);
			edit.getDatausulan().setMspr_ins_period(1);
			edit.getDatausulan().setMspo_pay_period(mspo_pay_period);
			if("DBD BP".equals(pas.getJenis_pas())){
				edit.getDatausulan().setFlag_jenis_plan(5);//DBD
				edit.getDatausulan().setLsbs_id(203);//DBD
				edit.getDatausulan().setLsdbs_number(1);//DBD BP
			}else if("DBD AGENCY".equals(pas.getJenis_pas())){
				edit.getDatausulan().setFlag_jenis_plan(5);//DBD
				edit.getDatausulan().setLsbs_id(203);//DBD
				edit.getDatausulan().setLsdbs_number(2);//DBD AGENCY
			}else if("DBD MALL".equals(pas.getJenis_pas())){
				edit.getDatausulan().setFlag_jenis_plan(5);//DBD
				edit.getDatausulan().setLsbs_id(203);//DBD
				edit.getDatausulan().setLsdbs_number(3);//DBD MALL
			}else if("INDIVIDU".equals(pas.getJenis_pas()) || "AP/BP".equals(pas.getJenis_pas()) || "AP/BP ONLINE".equals(pas.getJenis_pas()) 
					|| "MALLINSURANCE".equals(pas.getJenis_pas()) || "PAS BPD".equals(pas.getJenis_pas())){
				edit.getDatausulan().setFlag_jenis_plan(5);//PAS
				edit.getDatausulan().setLsbs_id(187);//PAS
				edit.getDatausulan().setLsdbs_number(Integer.parseInt(lsdbs_number));//PAS
			}else{
				if(pas.getProduct_code().equals("45") || pas.getProduct_code().equals("130")){//super protection
					edit.getDatausulan().setFlag_jenis_plan(5);//SIMPLE BISNIS/ BAC SIMPLE --untuk PA
				}else{
					edit.getDatausulan().setFlag_jenis_plan(0);//SIMPLE BISNIS/ BAC SIMPLE
				}
				edit.getDatausulan().setLsbs_id(Integer.parseInt(pas.getProduct_code()));//SIMPLE BISNIS/ BAC SIMPLE
				edit.getDatausulan().setLsdbs_number(pas.getProduk());//SIMPLE BISNIS/ BAC SIMPLE
			}
			edit.getDatausulan().setKurs_p("01");
			edit.getDatausulan().setLku_id("01");
			edit.getDatausulan().setMspo_nasabah_acc(pas.getNo_kartu());
			edit.getDatausulan().setJenis_pemegang_polis(0);
			
			// tertanggung
			edit.getTertanggung().setMcl_id(pas.getMcl_id_tt());
//			if(pas.getLsre_id() == 1){
				edit.getTertanggung().setMspo_plan_provider(pas.getMspo_plan_provider());
				edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
				edit.getTertanggung().setNo_hp2(pas.getMsp_mobile2());
				edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no_tt());
				edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
				edit.getTertanggung().setKota_rumah(pas.getMsp_city());
				edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
				edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
				edit.getTertanggung().setEmail(pas.getMsp_pas_email());
//			}
//			edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
			edit.getTertanggung().setMkl_red_flag(0);
			edit.getTertanggung().setMcl_first(pas.getMsp_full_name());
//			edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no());
			edit.getTertanggung().setMspe_date_birth(pas.getMsp_date_of_birth());
//			edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
//			edit.getTertanggung().setKota_rumah(pas.getMsp_city());
//			edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
			edit.getTertanggung().setMste_age(pas.getMsp_age());
			edit.getTertanggung().setUsiattg(pas.getMsp_age());
//			edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
//			edit.getTertanggung().setEmail(pas.getMsp_pas_email());
			
			edit.getTertanggung().setDanaa("");
			edit.getTertanggung().setDanaa("");
			edit.getTertanggung().setDanaa2("");
			edit.getTertanggung().setShasil("");
			edit.getTertanggung().setMkl_pendanaan("");
			edit.getTertanggung().setMkl_smbr_penghasilan("");
			edit.getTertanggung().setMkl_kerja("");
			edit.getTertanggung().setMkl_industri("");
			edit.getTertanggung().setKerjaa("");
			edit.getTertanggung().setKerjab("");
			edit.getTertanggung().setIndustria("");
			edit.getTertanggung().setMspe_place_birth(pas.getMsp_pas_tmp_lhr_tt());
			edit.getTertanggung().setTujuana("");
			edit.getTertanggung().setMkl_tujuan("");
			edit.getTertanggung().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
			edit.getTertanggung().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
			edit.getTertanggung().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
			edit.getTertanggung().setReg_spaj(pas.getReg_spaj());
			edit.getTertanggung().setMspo_policy_no(pas.getMspo_policy_no());
			edit.getTertanggung().setLus_id(pas.getLus_id());
			edit.getTertanggung().setArea_code_rumah(pas.getMsp_area_code_rumah());
			if(pas.getLside_id() != null){
				edit.getTertanggung().setLside_id(pas.getLside_id());
			}
			if(pas.getLsre_id() == 1){
				edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
				edit.getTertanggung().setNo_hp2(pas.getMsp_mobile2());
				edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no());
				edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
				edit.getTertanggung().setKota_rumah(pas.getMsp_city());
				edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
				edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
				edit.getTertanggung().setEmail(pas.getMsp_pas_email());
				edit.getTertanggung().setMspe_sex(pas.getMsp_sex_pp());
				edit.getTertanggung().setTelpon_rumah(pas.getMsp_pas_phone_number());
				if(("AP/BP,AP/BP ONLINE,DBD AGENCY,DBD BP, DBD MALL,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
					edit.getTertanggung().setLsne_id(pas.getMsp_warga());
					edit.getTertanggung().setMspe_sts_mrt(pas.getMsp_status_nikah().toString());
					edit.getTertanggung().setLsag_id(pas.getLsag_id());
					edit.getTertanggung().setMcl_agama(pas.getMsp_agama());
					edit.getTertanggung().setLsed_id(pas.getMsp_pendidikan());
					edit.getTertanggung().setMkl_kerja(pas.getMsp_occupation());
					if(pas.getMsp_occupation_else() == null)pas.setMsp_occupation_else("");
					if(pas.getMsp_occupation_else() == null){
						edit.getTertanggung().setKerjaa("");
					}else{
						edit.getTertanggung().setKerjaa(pas.getMsp_occupation_else());
					}
					if("AP/BP".equals(pas.getJenis_pas()) || "AP/BP ONLINE".equals(pas.getJenis_pas())){
						if(pas.getMsp_company_jabatan() == null)pas.setMsp_company_jabatan("");
						edit.getTertanggung().setKerjab(pas.getMsp_company_jabatan());
						edit.getTertanggung().setAlamat_kantor(pas.getMsp_company_address());
						edit.getTertanggung().setKd_pos_kantor(pas.getMsp_company_postal_code());
						edit.getTertanggung().setTelpon_kantor(pas.getMsp_company_phone_number());
					}
				}
			}
			
			int hasil = 0;
			
			edit = savingBac.insertspajBisnisSimple(edit,currentUser, pas.getJenis_pas());
			if(!"".equals(edit.getPemegang().getReg_spaj())){
				
				// reas
				
				Reas reas=new Reas();
				reas.setLstbId(new Integer(1));
				String las_reas[]=new String[3];
				las_reas[0]="Non-Reas";
				las_reas[1]="Treaty";
				las_reas[2]="Facultative";
				reas.setCurrentUser(currentUser);   
				reas.setSpaj(edit.getPemegang().getReg_spaj());
				
				Map mPosisi=uwDao.selectF_check_posisi(reas.getSpaj());
				Integer lspdIdTemp=(Integer)mPosisi.get("LSPD_ID");
				reas.setLspdId(lspdIdTemp);
				String lspdPosTemp=(String)mPosisi.get("LSPD_POSITION");
				//produk asm
				Map mMap=selectDataUsulan(reas.getSpaj());
				Integer lsbsId=(Integer)mMap.get("LSBS_ID");

				//tertanggung
				Map mTertanggung=uwDao.selectTertanggung(reas.getSpaj());
				reas.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
				reas.setMsteInsured((String)mTertanggung.get("MCL_ID"));
				reas.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
				//
				Map mStatus=uwDao.selectWfGetStatus(reas.getSpaj(),reas.getInsuredNo());
				reas.setLiAksep((Integer)mStatus.get("LSSA_ID"));
				reas.setLiReas((Integer)mStatus.get("MSTE_REAS"));
				if (reas.getLiAksep()==null) 
					reas.setLiAksep( new Integer(1));
				
				
				//dw1 //pemegang
				Policy policy=uwDao.selectDw1Underwriting(reas.getSpaj(),reas.getLspdId(),reas.getLstbId());
				if(policy!=null){
					reas.setMspoPolicyHolder(policy.getMspo_policy_holder());
					reas.setNoPolis(policy.getMspo_policy_no());
					reas.setInsPeriod(policy.getMspo_ins_period());
					reas.setPayPeriod(policy.getMspo_pay_period());
					reas.setLkuId(policy.getLku_id());
					reas.setUmurPp(policy.getMspo_age());
					reas.setLcaId(policy.getLca_id());
					reas.setLcaId(policy.getLca_id());
					reas.setMste_kyc_date(policy.getMste_kyc_date());
				}
				
				//simultan===========================================================================================================
				//Integer sim = uwDao.prosesSimultan(1, edit.getPemegang().getReg_spaj(), edit.getAgen().getLca_id(), policy.getMspo_policy_holder(), reas.getMsteInsured());
//				String spaj;
//				Integer lsreIdPp;
//				String lcaIdPp;
//				int info;
//				List lsSimultanTt,lsSimultanPp;
//
//				spaj=edit.getPemegang().getReg_spaj();
//				info=0;
//				Command command=new Command();
//				command.setCurrentUser(currentUser);
//				command.setCount(new Integer(0));
//				//
//				Map a = uwDao.selectCheckPosisi(spaj);
//				int li_pos=Integer.parseInt(a.get("LSPD_ID").toString());
//				String ls_pos=a.get("LSPD_POSITION").toString();
//				Map map=uwDao.selectDataUsulan(spaj);
//				lsbsId=(Integer)map.get("LSBS_ID");
//				//validasi Posisi SPAJ harus UW (2)
//				if(li_pos !=2 ){
//					info=1;
//					//MessageBox('Info', 'Posisi Polis Ini Ada di ' + ls_pos )
//				}else if(lsbsId.intValue()==161){//produk sinarmas
//					info=3;
//				}
//				//
//				Map mPemegang=uwDao.selectPemegang(spaj);
//				
//				String mclIdPp,mclFirstPp,sDateBirthPp;
//				Date mspeDateBirthPp,mspeDateBirthTt;
//				Integer lsreIdTt;
//				String mclIdTt,mclFirstTt,sDateBirthTt,lcaIdTt;
//				//data pemegang
//				lsreIdPp=(Integer)mPemegang.get("LSRE_ID");
//				mclIdPp=(String)mPemegang.get("MCL_ID");
//				mclFirstPp=(String)mPemegang.get("MCL_FIRST");
//				mspeDateBirthPp=(Date)mPemegang.get("MSPE_DATE_BIRTH");
//			    sDateBirthPp=defaultDateFormat.format(mspeDateBirthPp);
//				lcaIdPp=(String)mPemegang.get("LCA_ID");
//				//data tertanggung
//				//Map mTertanggung=uwDao.selectTertanggung(spaj);
//				lsreIdTt=(Integer)mTertanggung.get("LSRE_ID");
//				mclIdTt=(String)mTertanggung.get("MCL_ID");
//				mclFirstTt=(String)mTertanggung.get("MCL_FIRST");
//				mspeDateBirthTt=(Date)mTertanggung.get("MSPE_DATE_BIRTH");
//			    sDateBirthTt=defaultDateFormat.format(mspeDateBirthTt);
//				lcaIdTt=(String)mTertanggung.get("LCA_ID");
//				//cek mcl_id
//				if(!(mclIdTt.substring(0,2).equalsIgnoreCase("XX")))
//					if(!(mclIdTt.substring(0,2).equalsIgnoreCase("WW")))
//						if(!(mclIdPp.substring(0,2).equalsIgnoreCase("XX")))
//							if(!(mclIdPp.substring(0,2).equalsIgnoreCase("WW"))){
//								info=2;
//								//MessageBox('Info', 'Proses Simultan Sudah pernah dilakukan untuk Pemegang & Tertanggung Polis!!!')
//							}	
//				int spasi,titik,koma,pjg;
//				//pemegang ambil nama depan saja 
//				spasi=mclFirstPp.indexOf(' ');
//				titik=mclFirstPp.indexOf('.');
//				koma=mclFirstPp.indexOf(',');
//				pjg=mclFirstPp.length();
//				if(spasi>0)
//					mclFirstPp=mclFirstPp.substring(0,spasi);
//				else if(titik>0)
//					mclFirstPp=mclFirstPp.substring(0,titik);
//				else if(koma>0)
//					mclFirstPp=mclFirstPp.substring(0,koma);
//				//Tertanggung ambil nama depan saja 
//				spasi=mclFirstTt.indexOf(' ');
//				titik=mclFirstTt.indexOf('.');
//				koma=mclFirstTt.indexOf(',');
//				pjg=mclFirstTt.length();
//				if(spasi>0)
//					mclFirstTt=mclFirstTt.substring(0,spasi);
//				else if(titik>0)
//					mclFirstTt=mclFirstTt.substring(0,titik);
//				else if(koma>0)
//					mclFirstTt=mclFirstTt.substring(0,koma);
//				//
//				Map param=new HashMap();
//				param.put("mcl_id",mclIdPp);
//				param.put("nama",mclFirstPp);
//				param.put("tgl_lhr",sDateBirthPp);
//				lsSimultanPp=uwDao.selectSimultanNew(param);
//				param=new HashMap(); 
//				param.put("mcl_id",mclIdTt);
//				param.put("nama",mclFirstTt);
//				param.put("tgl_lhr",sDateBirthTt);
//				lsSimultanTt=uwDao.selectSimultanNew(param);
////				if(logger.isDebugEnabled())logger.debug(""+lsSimultanPp.size());
////				if(logger.isDebugEnabled())logger.debug(""+lsSimultanTt.size());
//				Integer proses=null;
////				if(logger.isDebugEnabled())logger.debug("isi list simultan ="+lsSimultanTt.size());
//
//				command.setSpaj(spaj);
//				command.setLsreIdPp(lsreIdPp);
//				command.setLcaIdPp(lcaIdPp);
//				command.setLsSimultanPp(lsSimultanPp);
//				command.setLsSimultanTt(lsSimultanTt);
//				command.setRowPp(lsSimultanPp.size());
//				command.setRowTt(lsSimultanTt.size());
//				command.setFlagAdd(lsreIdPp.intValue());
//				command.setError(new Integer(info));
//				command.setFlagId(ls_pos);
//				command.setMapPemegang(mPemegang);
//				command.setMapTertanggung(mTertanggung);
//				
//				int choosePp=0;
//				int chooseTt=0;
//				
//				if(command.getLsreIdPp()==1){//hbungan diri sendiri
//					choosePp=chooseTt;
//				}
//				String idSimultanPp,idSimultanTt;
//				idSimultanPp=(String)mPemegang.get("ID_SIMULTAN");
//				idSimultanTt=(String)mTertanggung.get("ID_SIMULTAN");
//				
//				//mcl_id ambil yang baru karena data baru ini yang akan di isi
//				Map mPpOld=command.getMapPemegang();
//				Map mTtOld=command.getMapTertanggung();
//				mclIdPp=(String)mPpOld.get("MCL_ID");
//				mclIdTt=(String)mTtOld.get("MCL_ID");
//				
//				Integer sim = simultan.prosesSimultan(command, mclIdPp, mclIdTt, idSimultanPp, idSimultanTt);
				//====================================================================================================================
				//Integer sim = uwDao.prosesSimultan(1, edit.getPemegang().getReg_spaj(), edit.getAgen().getLca_id(), policy.getMspo_policy_holder(), reas.getMsteInsured());
				
				//reas
				reas.setLstbId(1);
				reas.setLspdId(2);
				reasIndividu.prosesReasUnderwritingPas(reas, errors);
				
				
				//billing
				List lsDp = new ArrayList();
				uwDao.wf_ins_bill_pas(edit.getPemegang().getReg_spaj(), new Integer(1), new Integer(1), edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number(), lspdIdTemp, new Integer(1), lsDp, lus_id.toString(), policy, errors);
				
				//=================================================
//				uwManager.prosesAkseptasiPas(akseptasi, pas, user, 0, 0, "", errors);
				pas.setReg_spaj(edit.getPemegang().getReg_spaj());
				//hasil=this.elionsManager.prosesTransferPembayaran(transfer,flagNew,errors);
				//akseptasi======================================
				int result = 0;
				//try{
					Akseptasi akseptasi = new Akseptasi();
					
					akseptasi.setLiAksep(5);
					akseptasi.setLspdId(4);
					akseptasi.setLsspId(1);
					akseptasi.setLssaId(5);
					akseptasi.setCurrentUser(currentUser);
					akseptasi.setSpaj(pas.getReg_spaj());
					akseptasi.setInsuredNo(1);
					akseptasi.setLcaId(regionid.substring(0, 2));
					akseptasi.setLsbsId(edit.getDatausulan().getLsbs_id());
					akseptasi.setProses("1");
					int thn = 0;
					int bln = 0;
					String desc = "";
					result =  uwDao.prosesAkseptasiPas(akseptasi,thn,bln,desc,errors);
				//}catch (Exception e){
				//	TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				//}
				
				//=======================================================================================
				//transfer ke payment
				Transfer transfer = new Transfer();
				DecimalFormat fmt = new DecimalFormat ("000");
				Map mDataUsulan=uwDao.selectDataUsulan(edit.getPemegang().getReg_spaj());
				transfer.setSpaj(edit.getPemegang().getReg_spaj());
				transfer.setPModeId((Integer)mDataUsulan.get("LSCB_ID"));
				transfer.setBegDate((Date)mDataUsulan.get("MSTE_BEG_DATE"));
				transfer.setEndDate((Date)mDataUsulan.get("MSTE_END_DATE"));
				transfer.setMedical((Integer)mDataUsulan.get("MSTE_MEDICAL"));
				transfer.setLsbsId((Integer)mDataUsulan.get("LSBS_ID"));
				transfer.setLsdbsNumber((Integer)mDataUsulan.get("LSDBS_NUMBER"));
				transfer.setBisnisId(fmt.format(transfer.getLsbsId().intValue()));
				transfer.setPremium((Double)mDataUsulan.get("MSPR_PREMIUM"));
				transfer.setTsi((Double)mDataUsulan.get("MSPR_TSI"));
				//tertanggung
				Map mTertanggung2=uwDao.selectTertanggung(transfer.getSpaj());
				transfer.setInsuredNo((Integer)mTertanggung2.get("MSTE_INSURED_NO"));
				transfer.setMsteInsured((String)mTertanggung2.get("MCL_ID"));
				transfer.setMsagId((String)mTertanggung2.get("MSAG_ID"));
				transfer.setUmurTt((Integer)mTertanggung2.get("MSTE_AGE"));
				transfer.setCurrentUser((User) request.getSession().getAttribute("currentUser"));
				transfer.setLsDp(new ArrayList());
				transfer.setLiLama(0);
				Map mStatus2=uwDao.selectWfGetStatus(transfer.getSpaj(),transfer.getInsuredNo());
				transfer.setLiAksep((Integer)mStatus.get("LSSA_ID"));
				transfer.setLiReas((Integer)mStatus.get("MSTE_REAS"));
				transfer.setLspdId(new Integer(4));
				transfer.setLstbId(new Integer(1));
				transfer.setLiPosisi(4);//pembayaran
				policy=uwDao.selectDw1Underwriting(transfer.getSpaj(),transfer.getLspdId(),transfer.getLstbId());
				if(policy!=null){
					transfer.setMspoPolicyHolder(policy.getMspo_policy_holder());
					transfer.setNoPolis(policy.getMspo_policy_no());
					transfer.setInsPeriod(policy.getMspo_ins_period());
					transfer.setPayPeriod(policy.getMspo_pay_period());
					transfer.setLkuId(policy.getLku_id());
					transfer.setUmurPp(policy.getMspo_age());
					transfer.setLcaId(policy.getLca_id());
				}
				transfer.setPolicy(policy);
				//hasil=transferUw.prosesTransferPembayaran(transfer,1,errors);
				//=================================================================================
				// checklist
				if(("AP/BP,AP/BP ONLINE,DBD BP,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
					
					String mclIdNew = uwDao.selectMclIDPemegangPolis(edit.getPemegang().getReg_spaj());
					edit.getPersonal().setMcl_id(mclIdNew);
					
					commonDao.insertMstCompany(edit.getPersonal());
					
					Checklist cl = new Checklist();
					List<String> lcList = uwDao.selectLcChecklistWoMedis();
					
					int ktp_adm = pas.getMsp_cek_ktp();//lc_id=3
					int kk_adm = pas.getMsp_cek_kk();//lc_id=7
					int npwp_adm = pas.getMsp_cek_npwp();//lc_id=10
					int bukti_bayar_adm = pas.getMsp_cek_bukti_bayar();//lc_id=16
					int ktp_uw = pas.getMsp_cek_ktp_uw();//lc_id=3
					int kk_uw = pas.getMsp_cek_kk_uw();//lc_id=7
					int npwp_uw = pas.getMsp_cek_npwp_uw();//lc_id=10
					int bukti_bayar_uw = pas.getMsp_cek_bukti_bayar_uw();//lc_id=16
					
					Date tgl = commonDao.selectSysdate();
					
					for(int li = 0 ; li < lcList.size() ; li++){
						cl = new Checklist();
						cl.setReg_spaj(edit.getPemegang().getReg_spaj());
						cl.setLc_id(Integer.parseInt(lcList.get(li)));
						cl.setMc_no(li+1);
						cl.setLus_id_adm(pas.getLus_id());
						cl.setLus_id_uw(lus_id);
						cl.setTgl_adm(tgl);
						cl.setTgl_uw(tgl);
						cl.setLc_active(1);
						if(lcList.get(li).equals("3") && ktp_adm == 1){
							cl.setFlag_adm(1);
						}else if(lcList.get(li).equals("7") && kk_adm == 1){
							cl.setFlag_adm(1);
						}else if(lcList.get(li).equals("10") && npwp_adm == 1){
							cl.setFlag_adm(1);
						}else if(lcList.get(li).equals("16") && bukti_bayar_adm == 1){
							cl.setFlag_adm(1);
						}else{
							cl.setFlag_adm(0);
						}
						if(lcList.get(li).equals("3") && ktp_uw == 1){
							cl.setFlag_uw(1);
						}else if(lcList.get(li).equals("7") && kk_uw == 1){
							cl.setFlag_uw(1);
						}else if(lcList.get(li).equals("10") && npwp_uw == 1){
							cl.setFlag_uw(1);
						}else if(lcList.get(li).equals("16") && bukti_bayar_uw == 1){
							cl.setFlag_uw(1);
						}else{
							cl.setFlag_uw(0);
						}
						checklistDao.saveChecklistPas(cl);
					}
					
				}
				
				policy=uwDao.selectDw1Underwriting(reas.getSpaj(),4,1);
				
				edit.getPemegang().setMspo_policy_no(policy.getMspo_policy_no());
				pas.setMspo_policy_no(policy.getMspo_policy_no());
				String x ="";
				uwDao.updateKartuPas2(pas.getNo_kartu(), 1, edit.getPemegang().getReg_spaj());
				if("insert".equals(jenis_proses)){
					uwDao.insertPas(pas);
				}else{
					uwDao.updatePas(pas);
				}
			}else{
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				pas.setMspo_policy_no(null);
				pas.setReg_spaj(null);
			}
			uwDao.insertMstPositionSpajPas(pas.getLus_id().toString(), "TRANSFER DATA", pas.getMsp_fire_id(), 5);
		}
		catch (Exception e){
			logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			pas.setMspo_policy_no(null);
			pas.setReg_spaj(null);
		}
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		return edit;
	}
	
	public Pas transferSimpleBac(Pas pas, User currentUser){
		pas.setStatus(0);
		try{
			//update pas -> transfer pas
			pas.setLspd_id(2);
			pas.setLssp_id(10);
			pas.setMsp_kode_sts_sms("00");
			
			//-------
			Cmdeditbac edit= new Cmdeditbac();
			int umurPp=0;
//			try{
				Integer lus_id = Integer.parseInt(currentUser.getLus_id());
//				if(request.getParameter("kata")!=null){
					Date beg_date = pas.getMsp_pas_beg_date();//commonDao.selectSysdateTrunc();
					Date end_date = pas.getMsp_pas_end_date();//commonDao.selectSysdateTrunc();
//					Date end_fire_date = commonDao.selectSysdateTrunc();
//					end_date.setYear(end_date.getYear()+1);
//					end_date.setDate(end_date.getDate()-1);
					//pas.setMsp_pas_beg_date(beg_date);
					//pas.setMsp_pas_end_date(end_date);
//					pas.setMsp_fire_beg_date(beg_date);
//					end_fire_date.setMonth(beg_date.getMonth()+6);
//					end_fire_date.setDate(end_fire_date.getDate()-1);
//					pas.setMsp_fire_end_date(end_fire_date);
					pas.setLus_id(lus_id);
					pas.setLus_login_name(currentUser.getLus_full_name());
					pas.setDist("05");
					//pas.setLspd_id(99);
					
					//tgl beg date
					int tanggal1= beg_date.getDate();
					int bulan1 = beg_date.getMonth()+1;
					int tahun1 = beg_date.getYear()+1900;
					
					//tgl lahir ttg
					int tanggal2=pas.getMsp_date_of_birth().getDate();
					int bulan2=pas.getMsp_date_of_birth().getMonth()+1;
					int tahun2=pas.getMsp_date_of_birth().getYear()+1900;
					
					//tgl lahir pp
					int tanggal3=pas.getMsp_pas_dob_pp().getDate();
					int bulan3=pas.getMsp_pas_dob_pp().getMonth()+1;
					int tahun3=pas.getMsp_pas_dob_pp().getYear()+1900;
					
					//hit umur ttg, pp, pic
					f_hit_umur umr= new f_hit_umur();
					int umur =  umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
					umurPp =  umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
					
					pas.setMsp_age(umur);
					
//				}
				
				CheckSum checkSum = new CheckSum();
				edit.setPemegang(new Pemegang());
				edit.setPersonal(new Personal());
				edit.setContactPerson(new ContactPerson());
				edit.setTertanggung(new Tertanggung());
				edit.setDatausulan(new Datausulan());
				edit.setAgen(new Agen());
				edit.setAddressbilling(new AddressBilling());
				edit.setRekening_client(new Rekening_client());
				edit.setAccount_recur(new Account_recur());
				
				// agen
				Map agentMap = bacDao.selectagenpenutup(pas.getMsag_id());
				String regionid = (String) agentMap.get("REGIONID");
				edit.getAgen().setMsag_id((String) agentMap.get("ID"));
				edit.getAgen().setMcl_first((String) agentMap.get("NAMA"));
				edit.getAgen().setKode_regional(regionid);
				edit.getAgen().setLca_id(regionid.substring(0, 2));
				edit.getAgen().setLwk_id(regionid.substring(2, 4));
				edit.getAgen().setLsrg_id(regionid.substring(4, 6));
				if(edit.getAgen().getLca_id() == "58"){
					edit.getAgen().setLrb_id(pas.getLrb_id().toString());
				}
				
				edit.getContactPerson().setNama_lengkap("");
				
				// pemegang
				edit.getPemegang().setMspo_plan_provider(pas.getMspo_plan_provider());
				edit.getPemegang().setNo_hp(pas.getMsp_mobile());
				edit.getPemegang().setNo_hp2(pas.getMsp_mobile2());
				edit.getPemegang().setMcl_first(pas.getMsp_pas_nama_pp());
				edit.getPemegang().setMspe_no_identity(pas.getMsp_identity_no());
				edit.getPemegang().setMspe_date_birth(pas.getMsp_pas_dob_pp());
				edit.getPemegang().setMspe_email(pas.getMsp_pas_email());
				edit.getPemegang().setEmail(pas.getMsp_pas_email());
				edit.getPemegang().setAlamat_rumah(pas.getMsp_address_1());
				edit.getPemegang().setKota_rumah(pas.getMsp_city());
				edit.getPemegang().setKd_pos_rumah(pas.getMsp_postal_code());
				edit.getPemegang().setMkl_red_flag(0);
//				if(pas.getMsag_id_pp() != null){
//					edit.getPemegang().setMspo_ao(pas.getMsag_id_pp());
//				}else{
//					edit.getPemegang().setMspo_ao(pas.getKode_ao());
//				}
				if(("AP/BP,DBD BP,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
					edit.getPemegang().setMspo_ao(pas.getMsag_id_pp());
				}else{
					edit.getPemegang().setMspo_ao(pas.getMsag_id());
				}
//				else{
//					edit.getPemegang().setMspo_ao(pas.getKode_ao());
//				}
				edit.getPemegang().setMspo_pribadi(pas.getPribadi());
				edit.getPemegang().setMspe_sex(pas.getMsp_sex_pp());
				edit.getPemegang().setReg_spaj(pas.getReg_spaj());
				edit.getPemegang().setMspo_policy_no(pas.getMspo_policy_no());
				edit.getPemegang().setMspo_spaj_date(commonDao.selectSysdate());
				edit.getPemegang().setMste_tgl_recur(pas.getMsp_tgl_valid());
				edit.getPemegang().setLus_id(pas.getLus_id());
				edit.getPemegang().setMspo_age(umurPp);
				edit.getPemegang().setUsiapp(umurPp);
				edit.getPemegang().setMspe_place_birth(pas.getMsp_pas_tmp_lhr_pp());
				edit.getPemegang().setArea_code_rumah(pas.getMsp_area_code_rumah());
				if(pas.getLside_id() != null){
					edit.getPemegang().setLside_id(pas.getLside_id());
				}
				edit.getPemegang().setTelpon_rumah(pas.getMsp_pas_phone_number());
				edit.getPemegang().setLsre_id(pas.getLsre_id());
				edit.getPemegang().setMkl_tujuan("");
				edit.getPemegang().setTujuana("");
				edit.getPemegang().setMkl_pendanaan("");
				edit.getPemegang().setDanaa("");
				edit.getPemegang().setMkl_smbr_penghasilan("");
				edit.getPemegang().setShasil("");
				edit.getPemegang().setDanaa2("");
				edit.getPemegang().setMkl_kerja("");
				edit.getPemegang().setKerjaa("");
				edit.getPemegang().setKerjab("");
				edit.getPemegang().setMkl_industri("");
				edit.getPemegang().setIndustria("");
				edit.getPemegang().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
				edit.getPemegang().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
				edit.getPemegang().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
				
				if(pas.getMsag_id_pp() == null)pas.setMsag_id_pp("");
				//if(!"".equals(pas.getMsag_id_pp())){
				// AP/BP,AP/BP ONLINE,DBD AGENCY,DBD BP,PAS BPD
//				if(("AP/BP,AP/BP ONLINE,DBD AGENCY,DBD BP,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
					edit.getPemegang().setLsne_id(pas.getMsp_warga());
					edit.getPemegang().setMspe_sts_mrt(pas.getMsp_status_nikah().toString());
					edit.getPemegang().setLsag_id(pas.getLsag_id());
					edit.getPemegang().setMcl_agama(pas.getMsp_agama());
					edit.getPemegang().setLsed_id(pas.getMsp_pendidikan());
					edit.getPemegang().setMkl_kerja(pas.getMsp_occupation());
					if(pas.getMsp_occupation_else() == null)pas.setMsp_occupation_else("");
					if(pas.getMsp_occupation_else() == null){
						edit.getPemegang().setKerjaa("");
					}else{
						edit.getPemegang().setKerjaa(pas.getMsp_occupation_else());
					}
					
					
//				}
				
				// address billing
				edit.getAddressbilling().setNo_hp(pas.getMsp_mobile());
				edit.getAddressbilling().setNo_hp2(pas.getMsp_mobile2());
				edit.getAddressbilling().setMsap_contact(pas.getMsp_full_name());
				edit.getAddressbilling().setMsap_address(pas.getMsp_address_1());
				edit.getAddressbilling().setMsap_area_code1(pas.getMsp_area_code_rumah());
				edit.getAddressbilling().setKota(pas.getMsp_city());
				edit.getAddressbilling().setMsap_zip_code(pas.getMsp_postal_code());
				edit.getAddressbilling().setE_mail(pas.getMsp_pas_email());
				edit.getAddressbilling().setReg_spaj(pas.getReg_spaj());
				edit.getAddressbilling().setRegion(regionid);
				edit.getAddressbilling().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
				edit.getAddressbilling().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
				edit.getAddressbilling().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
				
				// rekening client
				//simple bisnis
					edit.getRekening_client().setNotes(pas.getJenis_pas());
				//edit.getRekening_client().setNotes("PAS");
				// tabungan ataupun kartu kredit, kedua rekening harus diisi & untuk tunai, rekening tabungan harus diisi
				
				if("".equals(pas.getLsbp_id()) || "0".equals(pas.getLsbp_id()) || pas.getLsbp_id() == null){
					pas.setLsbp_id("0");
					pas.setMsp_no_rekening("-");
					pas.setMsp_rek_cabang("-");
					pas.setMsp_rek_kota("-");
					pas.setMsp_rek_nama("-");
				}
				
				edit.getRekening_client().setLsbp_id(pas.getLsbp_id());
				//if(pas.getMsp_flag_cc() == 2){//tabungan
					edit.getRekening_client().setMrc_nama(pas.getMsp_rek_nama().toUpperCase());
					edit.getRekening_client().setMrc_cabang(pas.getMsp_rek_cabang().toUpperCase());
					edit.getRekening_client().setMrc_kota(pas.getMsp_rek_kota().toUpperCase());
				//}
				edit.getRekening_client().setMrc_jn_nasabah(0);//none
				edit.getRekening_client().setMrc_kurs("01");//rupiah
				edit.getRekening_client().setNo_account(pas.getMsp_no_rekening());
				edit.getRekening_client().setMrc_no_ac(pas.getMsp_no_rekening());
				edit.getRekening_client().setMrc_no_ac_lama(pas.getMsp_no_rekening());
				edit.getRekening_client().setMrc_jenis(2);// rek client
				
				// account recur
				edit.getAccount_recur().setLbn_id(pas.getLsbp_id_autodebet());
				edit.getAccount_recur().setLus_id(pas.getLus_id());
				edit.getAccount_recur().setMar_number(1);
				edit.getAccount_recur().setMar_acc_no(pas.getMsp_no_rekening_autodebet());
				edit.getAccount_recur().setMar_holder(pas.getMsp_rek_nama_autodebet());
				edit.getAccount_recur().setMar_expired(pas.getMsp_tgl_valid());
				edit.getAccount_recur().setMar_active(1);
				
				// data usulan
//				String lsdbs_number = uwDao.selectCekPin(pas.getPin(), 1);
//				if(lsdbs_number == null)lsdbs_number = "x";
				String lsdbs_number = "x";
//				if(pas.getPin() != null){
//					lsdbs_number = uwDao.selectCekPin(pas.getPin(), 1);
//				}else{
//					lsdbs_number = uwDao.selectCekNoKartu(pas.getNo_kartu(), "", 1);
//				}
//				if("AP/BP".equals(pas.getJenis_pas()) || "AP/BP ONLINE".equals(pas.getJenis_pas())){
//					lsdbs_number = "05";
//				}else if("PAS BPD".equals(pas.getJenis_pas())){
//					lsdbs_number = "06";
//				}
//				if(!"".equals(pas.getMsag_id_pp())){
//					if(pas.getProduk() == 5){
//					  lsdbs_number = "05";
//					} else {
//				      lsdbs_number = "06";
//					}
//				}
				
//				if("DBD BP".equals(pas.getJenis_pas())){
//					lsdbs_number = "01";
//				}else if("DBD AGENCY".equals(pas.getJenis_pas())){
//					lsdbs_number = "02";
//				}
				int mspo_pay_period = 1;
			
				Double mspr_tsi = null;
				Double mspr_premium = null;
				
//				if("DBD BP".equals(pas.getJenis_pas()) || "DBD AGENCY".equals(pas.getJenis_pas())){
//					if("50000".equals(pas.getMsp_premi())){
//						mspr_tsi = new Double(2000000);
//					}else if("100000".equals(pas.getMsp_premi())){
//						mspr_tsi = new Double(4000000);
//					}else if("150000".equals(pas.getMsp_premi())){
//						mspr_tsi = new Double(6000000);
//					}else if("200000".equals(pas.getMsp_premi())){
//						mspr_tsi = new Double(8000000);
//					}else if("250000".equals(pas.getMsp_premi())){
//						mspr_tsi = new Double(10000000);
//					}else{
//						mspr_tsi = new Double(0);
//					}
//					mspr_premium = new Double(pas.getMsp_premi());
//				}else if("INDIVIDU".equals(pas.getJenis_pas()) || "AP/BP".equals(pas.getJenis_pas()) || "AP/BP ONLINE".equals(pas.getJenis_pas()) 
//							|| "MALLINSURANCE".equals(pas.getJenis_pas()) || "PAS BPD".equals(pas.getJenis_pas())){
//					if("01".equals(lsdbs_number)){
//						mspr_tsi = new Double(100000000);
//					}else if("02".equals(lsdbs_number)){
//						mspr_tsi = new Double(50000000);
//					}else if("03".equals(lsdbs_number)){
//						mspr_tsi = new Double(100000000);
//					}else if("04".equals(lsdbs_number)){
//						mspr_tsi = new Double(200000000);
//					}else if("05".equals(lsdbs_number)){
//						mspr_tsi = new Double(50000000);
//					}else if("06".equals(lsdbs_number)){
//						mspr_tsi = new Double(50000000);
//					}else{
//						mspr_tsi = new Double(100000000);
//					}
//					if("01".equals(lsdbs_number) && pas.getLscb_id() == 3){
//						mspr_premium = new Double(150000);
//					}else if("01".equals(lsdbs_number) && pas.getLscb_id() == 6){
//						mspr_premium = new Double(15000);
//					}else if("01".equals(lsdbs_number) && pas.getLscb_id() == 1){
//						mspr_premium = new Double(150000 * 0.27);
//					}else if("01".equals(lsdbs_number) && pas.getLscb_id() == 2){
//						mspr_premium = new Double(150000 * 0.525);
//					}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 3){
//						mspr_premium = new Double(300000);
//					}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 6){
//						mspr_premium = new Double(30000);
//					}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 1){
//						mspr_premium = new Double(300000 * 0.27);
//					}else if("02".equals(lsdbs_number) && pas.getLscb_id() == 2){
//						mspr_premium = new Double(300000 * 0.525);
//					}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 3){
//						mspr_premium = new Double(500000);
//					}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 6){
//						mspr_premium = new Double(50000);
//					}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 1){
//						mspr_premium = new Double(500000 * 0.27);
//					}else if("03".equals(lsdbs_number) && pas.getLscb_id() == 2){
//						mspr_premium = new Double(500000 * 0.525);
//					}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 3){
//						mspr_premium = new Double(900000);
//					}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 6){
//						mspr_premium = new Double(90000);
//					}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 1){
//						mspr_premium = new Double(900000 * 0.27);
//					}else if("04".equals(lsdbs_number) && pas.getLscb_id() == 2){
//						mspr_premium = new Double(900000 * 0.525);
//					}else if("05".equals(lsdbs_number)){
//						mspr_premium = new Double(74000);
//					}else if("06".equals(lsdbs_number)){
//						mspr_premium = new Double(74000);
//					} else{
//						mspr_premium = new Double(0);
//					}
//				}else{// simple bisnis
					mspr_tsi = new Double(pas.getMsp_up());
					mspr_premium = new Double(pas.getMsp_premi());
//				}
				edit.getDatausulan().setIsBungaSimponi(0);
				edit.getDatausulan().setIsBonusTahapan(0);
				edit.getDatausulan().setLssp_id(10);
				edit.getDatausulan().setLssa_id(5);
				edit.getDatausulan().setLspd_id(4);
				edit.getDatausulan().setMspo_age(umurPp);
				edit.getDatausulan().setMspo_spaj_date(commonDao.selectSysdate());
				edit.getDatausulan().setMspo_beg_date(pas.getMsp_pas_beg_date());
				edit.getDatausulan().setMspo_end_date(pas.getMsp_pas_end_date());
				edit.getDatausulan().setMste_medical(0);
				edit.getDatausulan().setLscb_id(pas.getLscb_id());
				edit.getDatausulan().setMspr_tsi(mspr_tsi);
				edit.getDatausulan().setMspr_premium(mspr_premium);
				edit.getDatausulan().setMspr_discount(new Double(0));
				edit.getDatausulan().setMste_flag_cc(pas.getMsp_flag_cc());// rek client = 0.tunai, 2.tabungan, 1.CC
				edit.getDatausulan().setFlag_worksite(0);
				//edit.getDatausulan().setFlag_account(pas.getMsp_flag_cc());
				edit.getDatausulan().setFlag_account(2);// 0 untuk umum  1 untuk account recur 2 untuk rek client 3 untuk account recur dan rek client
				edit.getDatausulan().setKode_flag(0);//default
				edit.getDatausulan().setMspo_beg_date(pas.getMsp_pas_beg_date());
				edit.getDatausulan().setMspo_end_date(pas.getMsp_pas_end_date());
				edit.getDatausulan().setMste_beg_date(pas.getMsp_pas_beg_date());
				edit.getDatausulan().setMste_end_date(pas.getMsp_pas_end_date());
				edit.getDatausulan().setMspo_ins_period(1);
				edit.getDatausulan().setMspr_ins_period(1);
				edit.getDatausulan().setMspo_pay_period(mspo_pay_period);
//				if("DBD BP".equals(pas.getJenis_pas())){
//					edit.getDatausulan().setFlag_jenis_plan(5);//DBD
//					edit.getDatausulan().setLsbs_id(203);//DBD
//					edit.getDatausulan().setLsdbs_number(1);//DBD BP
//				}else if("DBD AGENCY".equals(pas.getJenis_pas())){
//					edit.getDatausulan().setFlag_jenis_plan(5);//DBD
//					edit.getDatausulan().setLsbs_id(203);//DBD
//					edit.getDatausulan().setLsdbs_number(2);//DBD AGENCY
//				}else if("INDIVIDU".equals(pas.getJenis_pas()) || "AP/BP".equals(pas.getJenis_pas()) || "AP/BP ONLINE".equals(pas.getJenis_pas()) 
//						|| "MALLINSURANCE".equals(pas.getJenis_pas()) || "PAS BPD".equals(pas.getJenis_pas())){
//					edit.getDatausulan().setFlag_jenis_plan(5);//PAS
//					edit.getDatausulan().setLsbs_id(187);//PAS
//					edit.getDatausulan().setLsdbs_number(Integer.parseInt(lsdbs_number));//PAS
//				}else{
					if(pas.getProduct_code().equals("45") || pas.getProduct_code().equals("130")){//super protection
						edit.getDatausulan().setFlag_jenis_plan(5);//SIMPLE BISNIS/ BAC SIMPLE --untuk PA
					}else{
						edit.getDatausulan().setFlag_jenis_plan(0);//SIMPLE BISNIS/ BAC SIMPLE
					}
					edit.getDatausulan().setLsbs_id(Integer.parseInt(pas.getProduct_code()));//SIMPLE BISNIS/ BAC SIMPLE
					edit.getDatausulan().setLsdbs_number(pas.getProduk());//SIMPLE BISNIS/ BAC SIMPLE
//				}
				edit.getDatausulan().setKurs_p("01");
				edit.getDatausulan().setLku_id("01");
				edit.getDatausulan().setMspo_nasabah_acc(pas.getNo_kartu());
				edit.getDatausulan().setJenis_pemegang_polis(0);
				
				// tertanggung
//				if(pas.getLsre_id() == 1){
					edit.getTertanggung().setMspo_plan_provider(pas.getMspo_plan_provider());
					edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
					edit.getTertanggung().setNo_hp2(pas.getMsp_mobile2());
					edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no_tt());
					edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
					edit.getTertanggung().setKota_rumah(pas.getMsp_city());
					edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
					edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
					edit.getTertanggung().setEmail(pas.getMsp_pas_email());
//				}
//				edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
				edit.getTertanggung().setMkl_red_flag(0);
				edit.getTertanggung().setMcl_first(pas.getMsp_full_name());
//				edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no());
				edit.getTertanggung().setMspe_date_birth(pas.getMsp_date_of_birth());
//				edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
//				edit.getTertanggung().setKota_rumah(pas.getMsp_city());
//				edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
				edit.getTertanggung().setMste_age(pas.getMsp_age());
				edit.getTertanggung().setUsiattg(pas.getMsp_age());
//				edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
//				edit.getTertanggung().setEmail(pas.getMsp_pas_email());
				
				edit.getTertanggung().setDanaa("");
				edit.getTertanggung().setDanaa("");
				edit.getTertanggung().setDanaa2("");
				edit.getTertanggung().setShasil("");
				edit.getTertanggung().setMkl_pendanaan("");
				edit.getTertanggung().setMkl_smbr_penghasilan("");
				edit.getTertanggung().setMkl_kerja("");
				edit.getTertanggung().setMkl_industri("");
				edit.getTertanggung().setKerjaa("");
				edit.getTertanggung().setKerjab("");
				edit.getTertanggung().setIndustria("");
				edit.getTertanggung().setMspe_place_birth(pas.getMsp_pas_tmp_lhr_tt());
				edit.getTertanggung().setTujuana("");
				edit.getTertanggung().setMkl_tujuan("");
				edit.getTertanggung().setLca_id(FormatString.rpad("0",(regionid.substring(0,2)),2));
				edit.getTertanggung().setLwk_id(FormatString.rpad("0",(regionid.substring(2,4)),2));
				edit.getTertanggung().setLsrg_id(FormatString.rpad("0",(regionid.substring(4,6)),2));
				edit.getTertanggung().setReg_spaj(pas.getReg_spaj());
				edit.getTertanggung().setMspo_policy_no(pas.getMspo_policy_no());
				edit.getTertanggung().setLus_id(pas.getLus_id());
				edit.getTertanggung().setArea_code_rumah(pas.getMsp_area_code_rumah());
				if(pas.getLside_id() != null){
					edit.getTertanggung().setLside_id(pas.getLside_id());
				}
				if(pas.getLsre_id() == 1){
					edit.getTertanggung().setNo_hp(pas.getMsp_mobile());
					edit.getTertanggung().setNo_hp2(pas.getMsp_mobile2());
					edit.getTertanggung().setMspe_no_identity(pas.getMsp_identity_no());
					edit.getTertanggung().setAlamat_rumah(pas.getMsp_address_1());
					edit.getTertanggung().setKota_rumah(pas.getMsp_city());
					edit.getTertanggung().setKd_pos_rumah(pas.getMsp_postal_code());
					edit.getTertanggung().setMspe_email(pas.getMsp_pas_email());
					edit.getTertanggung().setEmail(pas.getMsp_pas_email());
					edit.getTertanggung().setMspe_sex(pas.getMsp_sex_pp());
					edit.getTertanggung().setTelpon_rumah(pas.getMsp_pas_phone_number());
					// AP/BP,AP/BP ONLINE,DBD AGENCY,DBD BP,PAS BPD
//					if(("AP/BP,AP/BP ONLINE,DBD AGENCY,DBD BP,PAS BPD").indexOf(pas.getJenis_pas()) > -1){
						edit.getTertanggung().setLsne_id(pas.getMsp_warga());
						edit.getTertanggung().setMspe_sts_mrt(pas.getMsp_status_nikah().toString());
						edit.getTertanggung().setLsag_id(pas.getLsag_id());
						edit.getTertanggung().setMcl_agama(pas.getMsp_agama());
						edit.getTertanggung().setLsed_id(pas.getMsp_pendidikan());
						edit.getTertanggung().setMkl_kerja(pas.getMsp_occupation());
						if(pas.getMsp_occupation_else() == null)pas.setMsp_occupation_else("");
						if(pas.getMsp_occupation_else() == null){
							edit.getTertanggung().setKerjaa("");
						}else{
							edit.getTertanggung().setKerjaa(pas.getMsp_occupation_else());
						}
//						if("AP/BP".equals(pas.getJenis_pas()) || "AP/BP ONLINE".equals(pas.getJenis_pas())){
//							if(pas.getMsp_company_jabatan() == null)pas.setMsp_company_jabatan("");
//							edit.getTertanggung().setKerjab(pas.getMsp_company_jabatan());
//							edit.getTertanggung().setAlamat_kantor(pas.getMsp_company_address());
//							edit.getTertanggung().setKd_pos_kantor(pas.getMsp_company_postal_code());
//							edit.getTertanggung().setTelpon_kantor(pas.getMsp_company_phone_number());
//						}
//					}
				}
				
//				int hasil = 0;
//			}
			//-------
			
			uwDao.insertMstPositionSpajPas(currentUser.getLus_id(), "TRANSFER DATA", pas.getMsp_fire_id(), 5);
			Map result = savingBac.insertClientSimpleBac(edit, currentUser);
			String err = result.get("err").toString();
			if("1".equals(err)){
				pas.setStatus(1);
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}else{
				pas.setMcl_id_pp(result.get("mcl_id_pp").toString());
				pas.setMcl_id_tt(result.get("mcl_id_tt").toString());
				uwDao.updatePas(pas);
			}
			
		}catch(Exception e){
			pas.setStatus(1);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return pas;
	}
	
	public Pas transferPas(Pas pas, Integer lus_id){
		pas.setStatus(0);
		try{
			//update pas -> transfer pas
			pas.setLspd_id(2);
			pas.setLssp_id(10);
			pas.setMsp_kode_sts_sms("00");
			uwDao.updatePas(pas);
			uwDao.insertMstPositionSpajPas(lus_id.toString(), "TRANSFER DATA", pas.getMsp_fire_id(), 5);
		}catch(Exception e){
			pas.setStatus(1);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return pas;
	}
	
	public Pas generateRegIdPas(Pas pas, User user){
		pas.setStatus(0);
		try{
			// update counter	
				if(pas.getMsp_fire_id() == null){
					Long intIDCounter = (Long) this.bacDao.select_counter(113, "01");
						int intID = intIDCounter.intValue();
						Calendar tgl_sekarang = Calendar.getInstance(); 
						Integer inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
						if (intIDCounter.longValue() == 0)
						{
							intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 1000000));
						}else{
							Integer inttgl1=new Integer(uwDao.selectGetCounterMonthYear(113, "01"));
		
							if (inttgl1.intValue() != inttgl2.intValue())
							{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
								//ganti dengan tahun skarang
								uwDao.updateCounterMonthYear(inttgl2.toString(), 113, "01");
								//reset nilai counter dengan 0
								intID = 0;
								uwDao.updateMstCounter2("0", 113, "01");
								//logger.info("update mst counter start di tahun baru");
							}else{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000"))+intIDCounter);
							}
						}
						
						//--------------------------------------------
						Long intFireId = new Long(intIDCounter.longValue() + 1);
						intID = intID + 1;
						uwDao.updateMstCounter3(intID, 113, "01");
						String mspFireId = intFireId.toString();
						pas.setMsp_fire_id(mspFireId);
					//====================
				}
				//insert pas
				uwDao.updateRegPas(pas);
				uwDao.insertMstPositionSpajPas(user.getLus_id(), "NEW ENTRY DATA", pas.getMsp_fire_id(), 5);
				uwDao.updateKartuPas1(pas.getNo_kartu(), 1, null, null);
				//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}catch(Exception e){
				
				logger.info(e);
				pas.setStatus(1);
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
			
			return pas;
	}
	
	public Pas insertPas(Pas pas, User user){
		pas.setStatus(0);
		try{
			// update counter	
				if(pas.getMsp_fire_id() == null){
					Long intIDCounter = (Long) this.bacDao.select_counter(113, "01");
						int intID = intIDCounter.intValue();
						Calendar tgl_sekarang = Calendar.getInstance(); 
						Integer inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
						if (intIDCounter.longValue() == 0)
						{
							intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 1000000));
						}else{
							Integer inttgl1=new Integer(uwDao.selectGetCounterMonthYear(113, "01"));
		
							if (inttgl1.intValue() != inttgl2.intValue())
							{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
								//ganti dengan tahun skarang
								uwDao.updateCounterMonthYear(inttgl2.toString(), 113, "01");
								//reset nilai counter dengan 0
								intID = 0;
								uwDao.updateMstCounter2("0", 113, "01");
								//logger.info("update mst counter start di tahun baru");
							}else{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000"))+intIDCounter);
							}
						}
						
						//--------------------------------------------
						Long intFireId = new Long(intIDCounter.longValue() + 1);
						intID = intID + 1;
						uwDao.updateMstCounter3(intID, 113, "01");
						String mspFireId = intFireId.toString();
						pas.setMsp_fire_id(mspFireId);
					//====================
				}
				//insert pas
				pas.setLspd_id(1);
				pas.setLssp_id(10);
				pas.setDist("05");
				pas.setMsp_kode_sts_sms("00");
				pas.setMsp_pas_create_date(commonDao.selectSysdate());
				uwDao.insertPas(pas);
				uwDao.insertMstPositionSpajPas(pas.getLus_id().toString(), "NEW ENTRY DATA", pas.getMsp_fire_id(), 5);
				//PA BP, DBD BP, DBD AGENCY tidak menggunakan no kartu dan pin (menggunakan table yg sama dengan pas sebagai temp nya)
				if(!("58".equals(user.getLca_id()) || pas.getJenis_pas().equals("HCP"))){
					uwDao.updateBukuPas(pas.getPin(), 1);
				}
				uwDao.updateKartuPas1(pas.getNo_kartu(), 1, null, null);
				//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}catch(Exception e){
				
				logger.info(e);
				pas.setStatus(1);
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
			
			return pas;
	}
	
	public Pas insertFire(Pas pas){
		pas.setStatus(0);
		try{
			// update counter	
				if(pas.getMsp_fire_id() == null){
					Long intIDCounter = (Long) this.bacDao.select_counter(113, "01");
						int intID = intIDCounter.intValue();
						Calendar tgl_sekarang = Calendar.getInstance(); 
						Integer inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
						if (intIDCounter.longValue() == 0)
						{
							intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 1000000));
						}else{
							Integer inttgl1=new Integer(uwDao.selectGetCounterMonthYear(113, "01"));
		
							if (inttgl1.intValue() != inttgl2.intValue())
							{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
								//ganti dengan tahun skarang
								uwDao.updateCounterMonthYear(inttgl2.toString(), 113, "01");
								//reset nilai counter dengan 0
								intID = 0;
								uwDao.updateMstCounter2("0", 113, "01");
								//logger.info("update mst counter start di tahun baru");
							}else{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000"))+intIDCounter);
							}
						}
						
						//--------------------------------------------
						Long intFireId = new Long(intIDCounter.longValue() + 1);
						intID = intID + 1;
						uwDao.updateMstCounter3(intID, 113, "01");
						String mspFireId = intFireId.toString();
						pas.setMsp_fire_id(mspFireId);
					//====================
				}
				//insert fire
				pas.setLspd_id(2);
				pas.setLssp_id(10);
				pas.setMsp_flag_pas(0);
				pas.setMsp_fire_create_date(commonDao.selectSysdate());
				pas.setDist("05");
				pas.setMsp_kode_sts_sms("00");
				uwDao.insertPas(pas);
				uwDao.insertMstPositionSpajPas(pas.getLus_id().toString(), "NEW ENTRY DATA", pas.getMsp_fire_id(), 5);
			}catch(Exception e){
				logger.info(e);
				pas.setStatus(1);
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
			
			return pas;
	}
	
	public String sequenceTMMS(Tmms tmms){
		String id="";
		try{
			Date today=commonDao.selectSysdate("dd", true, 0);
			Integer yearNow=Integer.parseInt(new SimpleDateFormat("yyyy").format(today));
			Integer yearCount=0;
			Integer li_ctl=0;
			
			Map params = new HashMap();
			//params.put("aplikasi", 183);
			params.put("aplikasi", 243);
			String jenis_cp = StringUtil.rpad("0", String.valueOf(1), 2);
			params.put("cabang",jenis_cp );
			Map ctr= uwDao.selectCounterTmms(params);
			//Map ctr= (HashMap) querySingle("selectCounterAll", params);
			BigDecimal counter=(BigDecimal) ctr.get("MSCO_VALUE");
			
			if(counter.compareTo(new BigDecimal(0))==0){ 
				yearCount=yearNow;
				li_ctl=1;
			}else if(counter.toString().length()==9){
				yearCount= Integer.parseInt(counter.toString().substring(0, 4));
				li_ctl= Integer.parseInt(counter.toString().substring(4, 9));
				li_ctl++;
				
				if(yearCount.compareTo(yearNow)!=0){
					yearCount=yearNow;
					li_ctl=1;
				}
			}
			
			counter= new BigDecimal((yearCount*100000)+li_ctl);
			params.put("nilai", counter);
			uwDao.updateCounterTmms(params);
			//update("updateCounter", params);
			
//			if(tmms.getProd().getProduct_code().equals("019")){//simas siaga
//				id="19"+new SimpleDateFormat("MMyy").format(today)+StringUtil.rpad("0", String.valueOf(li_ctl), 5);
//			}else if(tmms.getProd().getProduct_code().equals("022")){//smart kid
//				id="22"+new SimpleDateFormat("MMyy").format(today)+StringUtil.rpad("0", String.valueOf(li_ctl), 5);
//			}else if(tmms.getProd().getProduct_code().equals("025")){//smart kid
//				id="25"+new SimpleDateFormat("MMyy").format(today)+StringUtil.rpad("0", String.valueOf(li_ctl), 5);
//			}else if(tmms.getProd().getProduct_code().equals("003")){//smart kid
//				id="01"+new SimpleDateFormat("MMyy").format(today)+StringUtil.rpad("0", String.valueOf(li_ctl), 5);
//			}else if(tmms.getProd().getProduct_code().equals("031")){
				//FREE PA
				id="42"+new SimpleDateFormat("MMyy").format(today)+StringUtil.rpad("0", String.valueOf(li_ctl), 5);
//			}else 
//				if(tmms.getProd().getProduct_code().equals("032")){//PA RISIKO !
//				id="32"+new SimpleDateFormat("MMyy").format(today)+StringUtil.rpad("0", String.valueOf(li_ctl), 5);
//			}else if(tmms.getProd().getProduct_code().equals("040")){//PAS
//				id="40"+new SimpleDateFormat("MMyy").format(today)+StringUtil.rpad("0", String.valueOf(li_ctl), 5);
//			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("ERROR :", e);
			
		}
		
		return id;
	}
	
	public String sertifikatTmms(Tmms tmms){
		String id="";
		try{
			HashMap tm=(HashMap) uwDao.selectTMProduct("042");
			//querySingle("selectTMProduct", tmms.getProd().getProduct_code());
			BigDecimal nomor=(BigDecimal) tm.get("NOMOR");
			BigDecimal nomor2=(BigDecimal) tm.get("NOMOR2");
			String no_polis=(String) tm.get("NO_POLIS");
			
			nomor=nomor.add(new BigDecimal(1));
			id=no_polis+"-"+FormatString.rpad("0", nomor.toString(), 6);
			Map param=new HashMap<String, Object>();
			param.put("nomor", nomor);
			param.put("nomor2", nomor2);
			param.put("product_code", "042");
			uwDao.updateTmProduct(param);
			//update("updateTmProduct", param);
		}catch (Exception e) {
//			 TODO: handle exception
			logger.error("ERROR :", e);
		}
		return id;
	}
	
	public String sertifikatPaCplan(Cplan cplan){
		String id="";
		try{
			HashMap tm=(HashMap) uwDao.selectTMProduct("046");
			//querySingle("selectTMProduct", tmms.getProd().getProduct_code());
			BigDecimal nomor=(BigDecimal) tm.get("NOMOR");
			BigDecimal nomor2=(BigDecimal) tm.get("NOMOR2");
			String no_polis=(String) tm.get("NO_POLIS");
			
			nomor=nomor.add(new BigDecimal(1));
			id=no_polis+"-"+FormatString.rpad("0", nomor.toString(), 6);
			Map param=new HashMap<String, Object>();
			param.put("nomor", nomor);
			param.put("nomor2", nomor2);
			param.put("product_code", "046");
			uwDao.updateTmProduct(param);
			//update("updateTmProduct", param);
		}catch (Exception e) {
//			 TODO: handle exception
			logger.error("ERROR :", e);
		}
		return id;
	}
	
	public PaTmmsFree updatePaTmmsFree(PaTmmsFree paTmmsFree){
		
		try{
			uwDao.updateTmms(paTmmsFree.getTmms());
			
			paTmmsFree.setTmmsDet(new TmmsDet());
			paTmmsFree.getTmmsDet().setId(paTmmsFree.getTmms().getId());
			paTmmsFree.getTmmsDet().setNama_peserta(paTmmsFree.getTmms().getHolder_name());
			paTmmsFree.getTmmsDet().setSex(paTmmsFree.getTmms().getSex());
			//paTmmsFree.getTmmsDet().setUsia(paTmmsFree.getTmms().getUsia());
			paTmmsFree.getTmmsDet().setBod_peserta(paTmmsFree.getTmms().getBod_holder());
			paTmmsFree.getTmmsDet().setBod_tempat(paTmmsFree.getTmms().getBod_tempat());
			paTmmsFree.getTmmsDet().setNo_identitas(paTmmsFree.getTmms().getNo_identitas());
			
			Integer umur = 0;
			try{
				f_hit_umur umr = new f_hit_umur();
				
				SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
				SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
				SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
				
				if(paTmmsFree.getTmms().getBod_holder() != null){
					Date nowDate = commonDao.selectSysdate();
					int tahun1 = Integer.parseInt(sdfYear.format(paTmmsFree.getTmms().getBod_holder()));
					int bulan1 = Integer.parseInt(sdfMonth.format(paTmmsFree.getTmms().getBod_holder()));
					int tanggal1 = Integer.parseInt(sdfDay.format(paTmmsFree.getTmms().getBod_holder()));
					int tahun2 = Integer.parseInt(sdfYear.format(nowDate));
					int bulan2 = Integer.parseInt(sdfMonth.format(nowDate));
					int tanggal2 = Integer.parseInt(sdfDay.format(nowDate));
				umur=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
			}
			}catch(Exception e){
				
			}
			paTmmsFree.getTmmsDet().setUsia(umur);
			
			uwDao.updateTmmsDet(paTmmsFree.getTmmsDet());
			
			//GENERATE POLIS
			PrintPolisFreePa printPolis = new PrintPolisFreePa();
			//List<Tmms> tmmsList = selectFreePaTmmsList(paTmmsFree.getTmms().getId(), "1", "", "LIKE");
			Tmms tmms = paTmmsFree.getTmms();
			String outputName = props.getProperty("pdf.dir.export") + "\\pa\\" + "MI_PA_" + tmms.getNo_sertifikat() + ".pdf";
			String ingrid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
			Date nowDate = commonDao.selectSysdate();
			printPolis.generateFreePa(outputName, tmms, paTmmsFree.getTmmsDet(), nowDate, ingrid);
		}catch(Exception e){
			logger.info(e);
			paTmmsFree.getTmms().setId(null);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return paTmmsFree;
	}
	
	public PaTmmsFree insertPaTmmsFree(PaTmmsFree paTmmsFree){
		
//		product_code="042";
//		product_name="Personal Accident Free";
//		campaign= "";
//		bank_id="";
//		jenis_cp = 1;
//		
//		age_type = 0;
//		age_from = 17;
//		age_to = 59;
//		max_ins_period = 59;
//		plan_type = 0;
//		
//		msco_number=183;
		try{
		
		Date sysdate = commonDao.selectSysdate("dd", false, 0);
		Date begdate = commonDao.selectSysdate("dd", true, 0);
		
		Date enddate = DateUtil.add(commonDao.selectSysdate("mm", true, 6), Calendar.DATE, -1); //lama tanggung dalam 6 bulan, dikurang 1 hari;
		
		paTmmsFree.getTmms().setBeg_date(begdate);
		paTmmsFree.getTmms().setEnd_date(enddate);
		
//		if(paTmmsFree.getGc().getGcr().getMcr_id_tmms()!=null){
//			paTmmsFree.getTmms().setId(paTmmsFree.getGc().getGcr().getMcr_id_tmms());
//		}else{
			paTmmsFree.getTmms().setId(sequenceTMMS(paTmmsFree.getTmms()));
//		}
		
		paTmmsFree.getTmms().setNo_sertifikat(sertifikatTmms(paTmmsFree.getTmms()));
		
		//paTmmsFree.getTmms().setCampaign(mainModel.getTmms().getProd().getCampaign());
		
		//paTmmsFree.getTmms().setMst_id(mainModel.getTmms().getId());
		
		paTmmsFree.getTmms().setPosisi(99);
		paTmmsFree.getTmms().setStatus_polis(1);
		paTmmsFree.getTmms().setFlag_edit(0);
		paTmmsFree.getTmms().setFlag_next_bill(0);
		paTmmsFree.getTmms().setTgl_input(sysdate);
		
		paTmmsFree.getTmms().setProduct_code("042");
		paTmmsFree.getTmms().setFlag_pa(0);
		paTmmsFree.getTmms().setFlag_cek(1);
		
		paTmmsFree.getTmms().setMst_id(paTmmsFree.getTmms().getId());
		
//		paTmmsFree.getTmms().setBank_id(mainModel.getTmms().getProd().getBank_id());
		paTmmsFree.getTmms().setTgl_awal(paTmmsFree.getTmms().getBeg_date());
		
		//paTmmsFree.getTmms().setHolder_name(mainModel.getUser().getLus_full_name());
		//paTmmsFree.getTmms().setSex(mainModel.getUser().getLus_sex());
		//paTmmsFree.getTmms().setBod_holder(mainModel.getUser().getLus_tgl_lhr());
		
		paTmmsFree.getTmms().setLump_sum(10000000.0);
		//paTmmsFree.getTmms().setBod_tempat(mainModel.getUser().getLus_tmp_lhr());
		//paTmmsFree.getTmms().setNo_identitas(paTmmsFree.getTmms().getNo_identitas());
		paTmmsFree.getTmms().setApplication_id("MALLINSURANCE_FP");
		//paTmmsFree.getTmms().setEmail(mainModel.getUser().getLus_email());
		paTmmsFree.getTmms().setLside_id(1);//KTP
		paTmmsFree.getTmms().setBill_freq(0);//sekaligus
		//paTmmsFree.getTmms().setVirtual_account("80062"+mainModel.getTmms().getId());
		
	
							
		uwDao.insertTmms(paTmmsFree.getTmms());
		
		TmmsDet tmmsDet=paTmmsFree.getTmmsDet();
		tmmsDet.setId(paTmmsFree.getTmms().getId());
		tmmsDet.setUrut(1);
		tmmsDet.setNama_peserta(paTmmsFree.getTmms().getHolder_name());
		tmmsDet.setBod_peserta(paTmmsFree.getTmms().getBod_holder());
		tmmsDet.setSex(paTmmsFree.getTmms().getSex());
		tmmsDet.setRelasi(1);
		tmmsDet.setProduct_code("042");
		tmmsDet.setBeg_aktif(paTmmsFree.getTmms().getBeg_date());
		tmmsDet.setEnd_aktif(paTmmsFree.getTmms().getEnd_date());
		tmmsDet.setBeg_date(paTmmsFree.getTmms().getBeg_date());
		tmmsDet.setFlag_aksep(5);
		tmmsDet.setUser_aksep(1);//ekalife
		tmmsDet.setFlag_aktif(1);
		tmmsDet.setFlag_claim(0);
		tmmsDet.setFlag_batal(0);
		tmmsDet.setFlag_pk(0);
		tmmsDet.setTgl_aksep(paTmmsFree.getTmms().getBeg_date());
		tmmsDet.setFlag_spouse(0);
		//tmmsDet.setNama_ibu(mainModel.getUser().getLus_mother_name());
		tmmsDet.setPlan(1);
		
		Integer umur = 0;
		try{
			f_hit_umur umr = new f_hit_umur();
			
			SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
			SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
			SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
			
			if(paTmmsFree.getTmms().getBod_holder() != null){
				Date nowDate = commonDao.selectSysdate();
				int tahun1 = Integer.parseInt(sdfYear.format(paTmmsFree.getTmms().getBod_holder()));
				int bulan1 = Integer.parseInt(sdfMonth.format(paTmmsFree.getTmms().getBod_holder()));
				int tanggal1 = Integer.parseInt(sdfDay.format(paTmmsFree.getTmms().getBod_holder()));
				int tahun2 = Integer.parseInt(sdfYear.format(nowDate));
				int bulan2 = Integer.parseInt(sdfMonth.format(nowDate));
				int tanggal2 = Integer.parseInt(sdfDay.format(nowDate));
			umur=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
		}
		}catch(Exception e){
			
		}
		tmmsDet.setUsia(umur);
		
		uwDao.insertTmmsDet(tmmsDet);
		
//		TmmsBill tmmsBill=mainModel.getTmms().getTmmsBill();
//		tmmsBill.setBeg_date(paTmmsFree.getTmms().getBeg_date());
//		tmmsBill.setEnd_date(paTmmsFree.getTmms().getEnd_date());
//		tmmsBill.setId(mainModel.getTmms().getId());
//		tmmsBill.setPremi_ke(1);
//		tmmsBill.setTahun_ke(1);
//		tmmsBill.setFlag_paid(1);
//		tmmsBill.setJumlah_premi(0.0);
//		tmmsBill.setTgl_input(paTmmsFree.getTmms().getTgl_input());
//		tmmsBill.setUser_input(1);
//		
//		insertTmmsBill(tmmsBill);
//		
//		TmmsDBill tmmsDBill=new TmmsDBill();
//		tmmsDBill.setId(tmmsBill.getId());
//		tmmsDBill.setPlan(tmmsDet.getPlan());
//		tmmsDBill.setPremi_ke(tmmsBill.getPremi_ke());
//		tmmsDBill.setTahun_ke(tmmsBill.getTahun_ke());
//		tmmsDBill.setUrut(tmmsDet.getUrut());
//		tmmsDBill.setProduct_code(tmmsDet.getProduct_code());
//		tmmsDBill.setRate_premi(0.0);
//		tmmsDBill.setDiskon(0.0);
//		tmmsDBill.setPremi(0.0);
//		tmmsDBill.setKomisi(0.0);
//		tmmsDBill.setUsia(tmmsDet.getUsia());
//		
//		insertTmmsDBill(tmmsDBill);
//		
//		
//		
//		if(paTmmsFree.getGc().getGcr().getMcr_id_tmms()==null){
//			//insert eins_glag_ws
//			CPlan plan=new CPlan();
//			plan.setNo_id(paTmmsFree.getTmms().getId());
//			plan.setLus_id(paTmmsFree.getTmms().getLus_id());
//			insertEinsFlagWs(plan);
//		}
		
		//GENERATE POLIS
		PrintPolisFreePa printPolis = new PrintPolisFreePa();
		List<Tmms> tmmsList = selectFreePaTmmsList(paTmmsFree.getTmms().getId(), null, "1", "", "LIKE", null);
		Tmms tmms = tmmsList.get(0);
		String outputName = props.getProperty("pdf.dir.export") + "\\pa\\" + "MI_PA_" + tmms.getNo_sertifikat() + ".pdf";
		String ingrid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
		Date nowDate = commonDao.selectSysdate();
		printPolis.generateFreePa(outputName, tmms, tmmsDet, nowDate, ingrid);
		
			}catch(Exception e){
				logger.info(e);
				paTmmsFree.getTmms().setId(null);
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			return paTmmsFree;
	}
	
	public Pas updatePaTemp(Pas pas, User user){
		
		pas.setStatus(0);
		try{
		
		//pindahin ke mst_pas_sms buat tempat penampungan sementara
			//insert pas
			
			pas.setLspd_id(1);
			pas.setLssp_id(10);
			pas.setDist("05");
			pas.setMsp_kode_sts_sms("00");
			uwDao.updatePas(pas);
			if(!"73".equals(pas.getProduct_code()) ||!"205".equals(pas.getProduct_code())   )
				pas = updateTransferCplan(pas, user);//request, pas, errors,"input",user,errors);
			else {
				// Set lcb_no & lcb_reff pada pa bsm
				if(pas.getLcb_no() == null)
					pas.setLcb_no(uwDao.selectLcbNo(pas.getLrb_id()));
				if(pas.getLcb_reff() == null)
					pas.setLcb_reff(uwDao.selectLcbNo(pas.getReff_id()));
			}
			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}catch(Exception e){
			
			logger.info(e);
			pas.setStatus(1);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return pas;
	}
	
	public Pas insertPaTemp(Pas pas, User user){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		pas.setStatus(0);
		pas  = uwDao.insertPaTemp(pas,user);
		if(pas.getStatus()!=0)TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
//		try{
//		//mst_counter
//			//cplan tambah kolom spaj dummy buat konek ke mst_reff_bii
//			
//			//waktu transfer mst_comm_reff_bii (20% dr premi (sk))
//		//pindahin ke mst_pas_sms buat tempat penampungan sementara
//			//insert pas
//			// update counter	
//			// 130 = NO REFF SPAJ PA BSM
//			if("".equals(pas.getReg_spaj()))pas.setReg_spaj(null);
//			if(pas.getReg_spaj() == null && !"73".equals(pas.getProduct_code())){
//				Long intIDCounter = (Long) this.bacDao.select_counter(130, "01");
//					int intID = intIDCounter.intValue();
//					String inttgl2Str = "";
//					Calendar tgl_sekarang = Calendar.getInstance(); 
//					Integer bulan = new Integer(tgl_sekarang.get(Calendar.MONTH)) + 1;
//					if(bulan < 10){
//						inttgl2Str = new Integer(tgl_sekarang.get(Calendar.YEAR)).toString() + "0" + bulan.toString();
//					}else{
//						inttgl2Str = new Integer(tgl_sekarang.get(Calendar.YEAR)).toString() + bulan.toString();
//					}
//					
//					Integer inttgl2 = new Integer(inttgl2Str);
//					if (intIDCounter.longValue() == 0)
//					{
//						intIDCounter = new Long ((long)(inttgl2)* 100000);
//					}else{
//						Integer inttgl1=new Integer(uwDao.selectGetCounterMonthYear(130, "01"));
//	
//						if (inttgl1.intValue() != inttgl2.intValue())
//						{
//							intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("00000")));
//							//ganti dengan tahun skarang
//							uwDao.updateCounterMonthYear(inttgl2.toString(), 130, "01");
//							//reset nilai counter dengan 0
//							intID = 0;
//							uwDao.updateMstCounter2("0", 130, "01");
//							//logger.info("update mst counter start di bulan dan tahun baru");
//						} else{
//							intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("00000"))+intIDCounter);
//						}
//					}
//					
//					//--------------------------------------------
//					Long intRegSpajPaBsm = new Long(intIDCounter.longValue() + 1);
//					intID = intID + 1;
//					uwDao.updateMstCounter3(intID, 130, "01");
//					String regSpajPaBsm = intRegSpajPaBsm.toString();
//					pas.setReg_spaj(regSpajPaBsm);
//				//====================
//			}
//			
//			// Generate Fire Id jika PA BSM - Daru 10 April 2015
//			if(pas.getMsp_fire_id() == null && "73".equals(pas.getProduct_code())){
//				Long intIDCounter = (Long) this.bacDao.select_counter(113, "01");
//					int intID = intIDCounter.intValue();
//					Calendar tgl_sekarang = Calendar.getInstance(); 
//					Integer inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
//					if (intIDCounter.longValue() == 0)
//					{
//						intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 1000000));
//					}else{
//						Integer inttgl1=new Integer(uwDao.selectGetCounterMonthYear(113, "01"));
//	
//						if (inttgl1.intValue() != inttgl2.intValue())
//						{
//							intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
//							//ganti dengan tahun skarang
//							uwDao.updateCounterMonthYear(inttgl2.toString(), 113, "01");
//							//reset nilai counter dengan 0
//							intID = 0;
//							uwDao.updateMstCounter2("0", 113, "01");
//							//logger.info("update mst counter start di tahun baru");
//						}else{
//							intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000"))+intIDCounter);
//						}
//					}
//					
//					//--------------------------------------------
//					Long intFireId = new Long(intIDCounter.longValue() + 1);
//					intID = intID + 1;
//					uwDao.updateMstCounter3(intID, 113, "01");
//					String mspFireId = intFireId.toString();
//					pas.setMsp_fire_id(mspFireId);
//				//====================
//			}
//			
//			pas.setLspd_id(1);
//			pas.setLssp_id(10);
//			pas.setDist("05");
//			pas.setMsp_kode_sts_sms("00");
//			pas.setMsp_pas_create_date(commonDao.selectSysdate());
//			
//			// Set lcb_no & lcb_reff pada pa bsm
//			if("73".equals(pas.getProduct_code())) {
//				pas.setLcb_no(uwDao.selectLcbNo(pas.getLrb_id()));
//				pas.setLcb_reff(uwDao.selectLcbNo(pas.getReff_id()));
//			}
//			
//			uwDao.insertPas(pas);
//			uwDao.insertMstPositionSpajPas(pas.getLus_id().toString(), "NEW ENTRY DATA", pas.getMsp_fire_id(), 5);
//			
//			if("73".equals(pas.getProduct_code()) && pas.getNo_kartu() != null && !pas.getNo_kartu().trim().equals("")) {
//				uwDao.updateKartuPas1(pas.getNo_kartu(), 1, pas.getReg_spaj(), null);
//			}
//			
//			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//			//TRANSFER===================================
//			if(!"73".equals(pas.getProduct_code()))
//				pas = transferCplan(pas, user);//request, pas, errors,"input",user,errors);
//			//============================================
//		}catch(Exception e){
//			
//			logger.info(e);
//			pas.setStatus(1);
//			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//		}
		
		return pas;
	}
	
//	public Pas transferCplan(Pas pas, User user){
//		
//		pas.setStatus(0);
//		try{
//			Date sysdate = commonDao.selectSysdate();
//			int produk = pas.getProduk();
//			Double up = new Double(pas.getMsp_up());
////			double rate = 0.00;
////			if(produk == 1){
////				rate = 0.65;
////			}else if(produk == 2){
////				rate = 1.45;
////			}else if(produk == 3){
////				rate = 3.05;
////			}
////			Double premi = (up * rate) / new Double(1000);
//			Double premi = new Double(pas.getMsp_premi());
//			//================================
//			Integer umurPp = 0;
//			Integer umurTt = 0;
//			try{
//				f_hit_umur umr = new f_hit_umur();
//				
//				SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
//				SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
//				SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
//				
//				int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
//				int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
//				int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
//				
//				if(pas.getMsp_pas_dob_pp() != null){
//					
//					int tahun1 = Integer.parseInt(sdfYear.format(pas.getMsp_pas_dob_pp()));
//					int bulan1 = Integer.parseInt(sdfMonth.format(pas.getMsp_pas_dob_pp()));
//					int tanggal1 = Integer.parseInt(sdfDay.format(pas.getMsp_pas_dob_pp()));
//					
//					umurPp=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
//				}
//				if(pas.getMsp_date_of_birth() != null){
//					int tahun1 = Integer.parseInt(sdfYear.format(pas.getMsp_date_of_birth()));
//					int bulan1 = Integer.parseInt(sdfMonth.format(pas.getMsp_date_of_birth()));
//					int tanggal1 = Integer.parseInt(sdfDay.format(pas.getMsp_date_of_birth()));
//					umurTt=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
//				}
//			}catch(Exception e){
//				
//			}
//			//==================================
//			//generate id di cplan
//			Long intIDCounter = (Long) this.bacDao.select_counter_eb(246, "01");
//			int intID = Integer.parseInt(intIDCounter.toString().substring(4));
//			Calendar tgl_sekarang = Calendar.getInstance(); 
//			Integer inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
//			String subIdYear = "25" + inttgl2.toString().substring(2);
//			if (intID == 0)
//			{
//				intIDCounter =  new Long (Integer.parseInt(subIdYear) * new Long(1000000));
//			}else{
//				Long inttgl1=new Long(uwDao.selectGetCounterValueEb(246, "01"));
//				String subtgl1 = inttgl1.toString().substring(0,4);
//
//				if (!subIdYear.equals(subtgl1))
//				{
//					//intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
//					intIDCounter=new Long(Long.parseLong(subIdYear.concat("000000")));
//					//ganti dengan tahun skarang
//					uwDao.updateCounterValueEb(intIDCounter.toString(), 246, "01");
//					//logger.info("update mst counter start di tahun baru");
//				}else{
//					intIDCounter=new Long(Long.parseLong(subIdYear.concat("000000"))+intID);
//				}
//			}
//			
//			//--------------------------------------------
//			Long no_id = new Long(intIDCounter.longValue() + 1);
//			intID = intID + 1;
//			uwDao.updateCounterValueEb(no_id.toString(), 246, "01");
//			String noId = no_id.toString();
//			//==========================
//			//=========================
//			//pas.setLus_id(Integer.parseInt(user.getLus_id()));
//			//================================================
//			//CPLAN
//			Cplan cplan = new Cplan();
//			cplan.setNo_id(noId);
//			cplan.setJenis_cp(25);//pa bsm
//			cplan.setNo_rek(pas.getMsp_no_rekening());
//			if(cplan.getNo_rek() == null || cplan.getNo_rek().equals(""))cplan.setNo_rek("0");
//			//cplan.setKanwill_id("");	
//			cplan.setLsbp_id(156);
//			cplan.setFull_name(pas.getMsp_pas_nama_pp());
//			cplan.setLku_id("01");
//			//cplan.setSet_bulanan(null);
//			//cplan.setSet_cplan(null);
//			cplan.setTotal_premi(new Long(premi.toString().replace(".0", "")));
//			cplan.setLump_sum(new Long(pas.getMsp_up().toString().replace(".0", "")));
//			cplan.setMcp_insper(1);// 1 tahun
//			//cplan.setMcp_sex(totalPremi);
//			cplan.setMcp_umur(umurPp);
//			cplan.setMcp_tgl_lahir(pas.getMsp_pas_dob_pp());
//			cplan.setMcp_tgl_lahir_pp(pas.getMsp_pas_dob_pp());
//			cplan.setMcp_beg_date(pas.getMsp_pas_beg_date());
//			cplan.setMcp_end_date(pas.getMsp_pas_end_date());
//			cplan.setLssp_id(10);
//			cplan.setMcp_sts_aksep(5);
//			cplan.setMcp_tgl_aksep(sysdate);
//			//cplan.setMcp_next_bill(null);// ga ada nextbill
//			cplan.setLspd_id(99);
//			cplan.setMsag_id(pas.getMsag_id());
//			//cplan.setMcp_flag_sim(null);
//			//cplan.setLc_id(null);
//			//cplan.setMcp_flag_bill(null);
//			cplan.setLscb_id(pas.getLscb_id());
//			cplan.setNo_sertifikat(sertifikatPaCplan(cplan));
//			cplan.setAddress1(pas.getMsp_address_1());
//			cplan.setCity(pas.getMsp_city());
//			cplan.setPostal_code(pas.getMsp_postal_code());
//			cplan.setReg_spaj(pas.getReg_spaj());
//			cplan.setEmail(pas.getMsp_pas_email());
//			uwDao.insertCplan(cplan);
//			//=================================================
//			//CPLAN_DET
//			CplanDet cplanDet = new CplanDet();
//			cplanDet.setNo_id(noId);
//			cplanDet.setUrut(1);
//			cplanDet.setNama_peserta(pas.getMsp_full_name());
//			//cplanDet.setSex();
//			cplanDet.setTmp_lahir_peserta(pas.getMsp_pas_tmp_lhr_tt());
//			cplanDet.setBod_peserta(pas.getMsp_date_of_birth());
//			cplanDet.setUmur(umurTt);
//			//cplanDet.setRelasi(relasi);
//			cplanDet.setProduct_code("046");
//			cplanDet.setPlan(pas.getProduk());
//			cplanDet.setBeg_aktif(pas.getMsp_pas_beg_date());
//			cplanDet.setEnd_aktif(pas.getMsp_pas_end_date());
//			cplanDet.setBeg_date(pas.getMsp_pas_beg_date());
//			cplanDet.setFlag_aksep(5);
//			cplanDet.setFlag_aktif(1);
//			cplanDet.setFlag_batal(0);
//			cplanDet.setUser_aksep(Integer.parseInt(user.getLus_id()));
//			cplanDet.setPremi(Integer.parseInt(premi.toString().replace(".0", "")));
//			cplanDet.setTgl_aksep(sysdate);
//			uwDao.insertCplanDet(cplanDet);
//			
//			//GENERATE POLIS
//			PrintPolisPaBsm printPolis = new PrintPolisPaBsm();
//			File userDir = new File(props.getProperty("pdf.dir.export") + "\\cplan\\25\\" + cplan.getNo_sertifikat());
//	        if(!userDir.exists()) {
//	            userDir.mkdirs();
//	        }
//			String outputName = props.getProperty("pdf.dir.export") + "\\cplan\\25\\" + cplan.getNo_sertifikat() + "\\" + cplan.getNo_sertifikat() + ".pdf";
//			String hamid = props.getProperty("pdf.template.admedika")+"\\hamid.bmp";
//			Date nowDate = commonDao.selectSysdate();
//			if(cplanDet.getPremi() == 0){
//				printPolis.generatePaBsm_free(outputName, cplan, cplanDet, nowDate, hamid);
//			}else{
//				printPolis.generatePaBsm(outputName, cplan, cplanDet, nowDate, hamid);
//			}
//			//=================================================
//			//transfer email otomatis
//			Boolean EmailOtomatis = uwDao.emailSoftcopyPaBsm(cplan, user);
//			//==================================================
//			uwDao.insertMstPositionSpajPas(pas.getLus_id().toString(), "TRANSFER DATA", pas.getMsp_fire_id(), 5);
//			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//		}catch(Exception e){
//			
//			logger.info(e);
//			pas.setStatus(2);//transfer gagal
//			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//		}
//		
//		return pas;
//	}
	
	public Pas updateTransferCplan(Pas pas, User user){		
		pas.setStatus(0);
		try{
			Date sysdate = commonDao.selectSysdate();
			int produk = pas.getProduk();
			Double up = new Double(pas.getMsp_up());
//			double rate = 0.00;
//			if(produk == 1){
//				rate = 0.65;
//			}else if(produk == 2){
//				rate = 1.45;
//			}else if(produk == 3){
//				rate = 3.05;
//			}
//			Double premi = (up * rate) / new Double(1000);
			Double premi = new Double(pas.getMsp_premi());
			//================================
			Integer umurPp = 0;
			Integer umurTt = 0;
			try{
				f_hit_umur umr = new f_hit_umur();
				
				SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
				SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
				SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
				
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
			//==================================
			//Select id di cplan
			
		
			
			Map cPlan = new HashMap();
			cPlan =	uwDao.selectCplan(pas.getReg_spaj());
			String no_Id = cPlan.get("NO_ID").toString();
			String no_sertifikat = cPlan.get("NO_SERTIFIKAT").toString();
			//==========================
			//=========================
			//pas.setLus_id(Integer.parseInt(user.getLus_id()));
			//================================================
			//CPLAN
			Cplan cplan = new Cplan();
			cplan.setNo_id(no_Id);	
			cplan.setFull_name(pas.getMsp_pas_nama_pp());
			cplan.setMcp_umur(umurPp);
			cplan.setMcp_tgl_lahir(pas.getMsp_pas_dob_pp());
			cplan.setMcp_tgl_lahir_pp(pas.getMsp_pas_dob_pp());
			cplan.setMcp_beg_date(pas.getMsp_pas_beg_date());
			cplan.setMcp_end_date(pas.getMsp_pas_end_date());
			cplan.setLump_sum(new Long(pas.getMsp_up().toString().replace(".0", "")));
			cplan.setNo_rek(pas.getMsp_no_rekening());
			if(cplan.getNo_rek() == null || cplan.getNo_rek().equals(""))cplan.setNo_rek("0");			
						
			cplan.setMsag_id(pas.getMsag_id());			
			cplan.setNo_sertifikat(no_sertifikat);
			cplan.setAddress1(pas.getMsp_address_1());
			cplan.setCity(pas.getMsp_city());
			cplan.setPostal_code(pas.getMsp_postal_code());
			cplan.setReg_spaj(pas.getReg_spaj());
			cplan.setEmail(pas.getMsp_pas_email());
			uwDao.updateCplan(cplan);
			//=================================================
			//CPLAN_DET
			CplanDet cplanDet = new CplanDet();
			cplanDet.setNo_id(no_Id);
			cplanDet.setUrut(1);
			cplanDet.setNama_peserta(pas.getMsp_full_name());
			//cplanDet.setSex();
			cplanDet.setTmp_lahir_peserta(pas.getMsp_pas_tmp_lhr_tt());
			cplanDet.setBod_peserta(pas.getMsp_date_of_birth());
			cplanDet.setUmur(umurTt);
			//cplanDet.setRelasi(relasi);
			cplanDet.setPlan(pas.getProduk());		
			cplanDet.setFlag_aktif(1);
			cplanDet.setFlag_batal(0);
			cplanDet.setUser_aksep(Integer.parseInt(user.getLus_id()));
			cplanDet.setPremi(Integer.parseInt(premi.toString().replace(".0", "")));
			uwDao.updateCplanDet(cplanDet);
			
			//GENERATE POLIS
			PrintPolisPaBsm printPolis = new PrintPolisPaBsm();
			File userDir = new File(props.getProperty("pdf.dir.export") + "\\cplan\\25\\" + cplan.getNo_sertifikat());
	              
	        if(!userDir.exists()) {
	            userDir.mkdir();
	        }	    	
	        
			String outputName = props.getProperty("pdf.dir.export") + "\\cplan\\25\\" + cplan.getNo_sertifikat() + "\\" + cplan.getNo_sertifikat() + ".pdf";
			
			String hamid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
			Date nowDate = commonDao.selectSysdate();
			if(cplanDet.getPremi() == 0){
				printPolis.generatePaBsm_free(outputName, cplan, cplanDet, nowDate, hamid);
			}else{
				printPolis.generatePaBsm(outputName, cplan, cplanDet, nowDate, hamid);
			}
			//=================================================
			//transfer email otomatis
			Boolean EmailOtomatis = uwDao.emailSoftcopyPaBsm(cplan, user);
			//==================================================
			uwDao.insertMstPositionSpajPas(pas.getLus_id().toString(), "TRANSFER DATA", pas.getMsp_fire_id(), 5);
			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}catch(Exception e){
			
			logger.info(e);
			pas.setStatus(2);//transfer gagal
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return pas;
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
	
	public String prosesInputBlacklist(CmdInputBlacklist cmd, User user)throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String lsClientNew=uwDao.wf_get_client_id(/*cabang*/"01");
		Integer blacklistIdnew=uwDao.wf_get_blacklist_id();
		Integer blacklistFamilyIdnew=uwDao.wf_get_blacklistFamily_id();
		logger.info("--------------" + lsClientNew);
		//insert client_new
		cmd.getClient().setMcl_id_new(lsClientNew);
		cmd.getClient().setMcl_first(cmd.getClient().getMcl_first().toUpperCase());
		if(!"".equals(cmd.getBlacklist().getLbl_no_identity())){cmd.getClient().setLside_id(1);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity().toUpperCase());}
		else if(!"".equals(cmd.getBlacklist().getLbl_no_identity_sim())){cmd.getClient().setLside_id(2);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity_sim().toUpperCase());}
		else if(!"".equals(cmd.getBlacklist().getLbl_no_identity_paspor())){cmd.getClient().setLside_id(3);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity_paspor().toUpperCase());}
		else if(!"".equals(cmd.getBlacklist().getLbl_no_identity_kk())){cmd.getClient().setLside_id(4);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity_kk().toUpperCase());}
		else if(!"".equals(cmd.getBlacklist().getLbl_no_identity_akte_lahir())){cmd.getClient().setLside_id(5);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity_akte_lahir().toUpperCase());}
		else if(!"".equals(cmd.getBlacklist().getLbl_no_identity_kims_kitas())){cmd.getClient().setLside_id(7);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity_kims_kitas().toUpperCase());}
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
		cmd.getBlacklist().setLbl_no_identity(cmd.getBlacklist().getLbl_no_identity().toUpperCase());
		cmd.getBlacklist().setLbl_no_identity_akte_lahir(cmd.getBlacklist().getLbl_no_identity_akte_lahir().toUpperCase());
		cmd.getBlacklist().setLbl_no_identity_kims_kitas(cmd.getBlacklist().getLbl_no_identity_kims_kitas().toUpperCase());
		cmd.getBlacklist().setLbl_no_identity_kk(cmd.getBlacklist().getLbl_no_identity_kk().toUpperCase());
		cmd.getBlacklist().setLbl_no_identity_paspor(cmd.getBlacklist().getLbl_no_identity_paspor().toUpperCase());
		cmd.getBlacklist().setLbl_no_identity_sim(cmd.getBlacklist().getLbl_no_identity_sim().toUpperCase());
		cmd.getBlacklist().setLbl_tempat(cmd.getClient().getMspe_place_birth());
		cmd.getBlacklist().setLbl_tgl_lahir(cmd.getClient().getMspe_date_birth());
		cmd.getBlacklist().setLus_login_name(user.getLus_full_name());
		cmd.getBlacklist().setLbl_id(blacklistIdnew);
		cmd.getBlacklist().setMcl_id(lsClientNew);
		cmd.getBlacklist().setLbl_tgl_input(commonDao.selectSysdate());
		cmd.getBlacklist().setLus_id(Integer.parseInt(user.getLus_id()));
		uwDao.insertLstBlacklist(cmd.getBlacklist());
		//insert det blacklist family
		int ldblNumber = 1;
		for(int i = 0 ; i < cmd.getDetBlackListAll().size() ; i++){
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
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		//logger.info("--------------" + lsClientNew);
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
			uwDao.insertMstPositionSpaj(user.getLus_id(), "ATTENTION LIST" , cmd.getBlacklist().getReg_spaj(), 0);
		}else{
			if(!"".equals(cmd.getBlacklist().getLbl_no_identity())){cmd.getClient().setLside_id(1);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity().toUpperCase());}
			else if(!"".equals(cmd.getBlacklist().getLbl_no_identity_sim())){cmd.getClient().setLside_id(2);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity_sim().toUpperCase());}
			else if(!"".equals(cmd.getBlacklist().getLbl_no_identity_paspor())){cmd.getClient().setLside_id(3);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity_paspor().toUpperCase());}
			else if(!"".equals(cmd.getBlacklist().getLbl_no_identity_kk())){cmd.getClient().setLside_id(4);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity_kk().toUpperCase());}
			else if(!"".equals(cmd.getBlacklist().getLbl_no_identity_akte_lahir())){cmd.getClient().setLside_id(5);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity_akte_lahir().toUpperCase());}
			else if(!"".equals(cmd.getBlacklist().getLbl_no_identity_kims_kitas())){cmd.getClient().setLside_id(7);cmd.getClient().setMspe_no_identity(cmd.getBlacklist().getLbl_no_identity_kims_kitas().toUpperCase());}
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
		cmd.getBlacklist().setLbl_no_identity(cmd.getBlacklist().getLbl_no_identity().toUpperCase());
		cmd.getBlacklist().setLbl_no_identity_akte_lahir(cmd.getBlacklist().getLbl_no_identity_akte_lahir().toUpperCase());
		cmd.getBlacklist().setLbl_no_identity_kims_kitas(cmd.getBlacklist().getLbl_no_identity_kims_kitas().toUpperCase());
		cmd.getBlacklist().setLbl_no_identity_kk(cmd.getBlacklist().getLbl_no_identity_kk().toUpperCase());
		cmd.getBlacklist().setLbl_no_identity_paspor(cmd.getBlacklist().getLbl_no_identity_paspor().toUpperCase());
		cmd.getBlacklist().setLbl_no_identity_sim(cmd.getBlacklist().getLbl_no_identity_sim().toUpperCase());
		cmd.getBlacklist().setLbl_flag_alasan(cmd.getBlacklist().getLbl_flag_alasan().toUpperCase());
		cmd.getBlacklist().setLbl_alasan(cmd.getBlacklist().getLbl_alasan().toUpperCase());
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
		
	/*public List<Map> selectRekClient() {
		return commonDao.selectRekClient();
	}*/

	public Date selectBegDateInsured(String spaj) {
		return uwDao.selectBegDateInsured(spaj); 		
	}
	
	public Date selectBegDateProductInsured(String spaj) {
		return uwDao.selectBegDateProductInsured(spaj); 		
	}
	
	public Date selectBDateSLink(String spaj){
		return uwDao.selectBDateSLink(spaj);
	}

	public Map selectExtraMortalita(String reg_spaj) {
		return uwDao.selectExtraMortalita(reg_spaj);
	}
	
	public Map selectDataAdmedika(String reg_spaj){
		return uwDao.selectDataAdmedika(reg_spaj);
	}
		
	public Integer selectCountFreeFluBabi(String spaj){
		return bacDao.selectCountFreeFluBabi(spaj);
	}
	
//	public List saveTglKirim(String[] spaj,String lus_id,Date tgl_kirim) throws DataAccessException{
//
//		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();		
//		
////		List daftar = this.uwDao.selectDaftarSPAJUnitLink2(lspd, spaj);
//		
//		List errors = new ArrayList();
//		
//		for(int i=0; i<spaj.length; i++) {			
//			String ls_spaj =spaj[i];
//			this.uwDao.insertMstPositionSpajKustodi(lus_id, "KIRIM BERKAS KUSTODIAN DARI UW KE FINANCE", ls_spaj,tgl_kirim);
//		}
//		
//		return errors;
//	}
	
	public HashMap selectPrintBabiRider(String reg_spaj){
		return this.bacDao.selectPrintBabiRider(reg_spaj);
	}
	
	public void updatePrintEndors(String noEndors){
		bacDao.updatePrintEndors(noEndors);
	}
	
	public Double selectSumPremiSlink(String spaj)throws DataAccessException{
		return bacDao.selectSumPremiSlink(spaj);
	}
	
	public Double selectSumPremiSlinkLama(String spaj)throws DataAccessException{
		return bacDao.selectSumPremiSlinkLama(spaj);
	}
	
	public Endors selectMstEndors(String no_endors) {
		return endorsDao.selectMstEndors(no_endors);
	}
	
	public List<DetEndors> selectMstDetEndors(String no_endors){
		return endorsDao.selectMstDetEndors(no_endors);
	}
	
	//public List<DropDown> selectListJnEndors(){
	//	return this.endorsDao.selectListJnEndors();
	//}

    public List<Map<String, Object>> selectStableRO( Map<String, Object> params )
    {
        return basDao.selectStableRO( params );  
    }
    
    public List<DropDown> selectCabBmi() {
    	return basDao.selectCabBmi();
    }
    
    public List<DropDown> selectCabBmiForReport() {
    	return basDao.selectCabBmiForReport();
    }
    
    public List<DropDown> selectPermintaan(String lus_id) {
    	return basDao.selectPermintaan(lus_id);
    }
    
    public List<DropDown> selectAdminBandara() {
    	return basDao.selectAdminBandara();
    }     
    
    public List<DropDown> selectNoBlankoBandara(String lus_id, String lca_id) {
    	return basDao.selectNoBlankoBandara(lus_id, lca_id);
    }
    
    public List<Map> selectHealthClaim(String reg_claim, String klaim){
		return uwDao.selectHealthClaim(reg_claim, klaim);
	}
    
    public List<Map> selectTrackingClaimHealth(String req_claim){
		return uwDao.selectTrackingClaimHealth(req_claim);
    }
    
    public List<Map> selectHealthClaimTM(String spaj){
		return uwDao.selectHealthClaimTM(spaj);
	}
    
    public List<Map> selectDaftarIcd(String id, String desc, Integer type) {
    	return uwDao.selectDaftarIcd(id,desc,type);
    }
    
    public List<Map> selectIcdByClassy(String data1, String data2) {
    	return uwDao.selectIcdByClassy(data1,data2);
    }
    
    public List<Map> selectDaftarProvider(String name, String addr) {
    	return uwDao.selectDaftarProvider(name,addr);
    }    
 
    public List<Map> selectHealthClaimEBTolak(String spaj, String insured_no){
		return uwDao.selectHealthClaimEBTolak(spaj, insured_no);
	}
	
	public List<Map> selectHealthClaimEBAccept(String spaj, String insured_no){
		return uwDao.selectHealthClaimEBAccept(spaj, insured_no);
	}
	
	public List<Map> selectHealthClaimEBPREAccept(String spaj, String insured_no){
		return uwDao.selectHealthClaimEBPREAccept(spaj, insured_no);
	}

	public List<Map> selectHealthClaimEBPU(String spaj, String insured_no){
		return uwDao.selectHealthClaimEBPU(spaj, insured_no);
	}
	
    public List<Map> selectCekHealthProduct(String nama, Date bod){
		return uwDao.selectCekHealthProduct(nama, bod);
	}
    
	public List<Map> selectBlomFlagPrintTopup(){
		return uwDao.selectBlomFlagPrintTopup();
	}
	public List selectSpajOtorisasiDisabled(String spaj){
		return bacDao.selectSpajOtorisasiDisabled(spaj);
	}
	
	public List selectAccessMenuOtorisasiSpaj( String cabBank, String jn_bank ){
		return bacDao.selectAccessMenuOtorisasiSpaj( cabBank, jn_bank );
	}
		
	public List<Linkdetail> uLinkDescrAndDetail( String spaj ){
		return uwDao.uLinkDescrAndDetail( spaj );
	}
	
	public List<Linkdetail> sLinkDescrAndDetail( String spaj ){
		return uwDao.sLinkDescrAndDetail( spaj );
	}
	
	public String selectMaxNoUrutMstDrekDet( String noTrx ){
		return uwDao.selectMaxNoUrutMstDrekDet( noTrx );
	}
	
	public Integer isUlinkBasedSpajNo( String regSpaj ){
		return uwDao.isUlinkBasedSpajNo( regSpaj );
	}
	
	public Integer isSlinkBasedSpajNo( String regSpaj ){
		return uwDao.isSlinkBasedSpajNo( regSpaj );
	}		
	public List selectMstDrekBasedNoTrx( String noTrx ){
		return uwDao.selectMstDrekBasedNoTrx( noTrx );
	}
	
	public List<DrekDet> selectMstDrekDet( String noTrx, String regSpaj, String paymentId, String norek_ajs ){
		return uwDao.selectMstDrekDet( noTrx ,regSpaj, paymentId, norek_ajs );
	}
	
	public List selectMstDrekDet2( String noTrx, String regSpaj, String paymentId, String norek_ajs ){
		return uwDao.selectMstDrekDet2( noTrx ,regSpaj, paymentId, norek_ajs );
	}
	
	public Double selectSumPremiMstDrekAndDet( String noTrx, String spaj ){
		return uwDao.selectSumPremiMstDrekAndDet( noTrx ,spaj );
	}
   	 
   	public List<Smsserver_in> selectSmsserver_in(String beg_date, String end_date, Integer update,Integer process,Integer id,Integer lus_id,Integer followup, Map params){
    	return basDao.selectSmsserver_in(beg_date, end_date, update, process,id,lus_id,followup, params);
    }
   	      	
   	public List<Smsserver_out> selectSmsserver_out(Integer id,Integer lus_id){
    	return basDao.selectSmsserver_out(id, lus_id);
    }
    
    public Boolean smsProsesUpdate(Smsserver_in sms_in,Smsserver_out sms_out){
    	
    	try{
	    	basDao.updateSmsserver_in(sms_in);
	    	if(sms_out!=null){
	    		basDao.insertSmsserver_out(sms_out, true);
	    	}
	    	return true;
    	}catch (Exception e) {
    		logger.error("ERROR :", e);
    		return false;
		}
    }
    
    public List<Smsserver_in> selectSmsserver_in_by_originator(String originator){
      	return basDao.selectSmsserver_in_by_originator(originator);
    }
    
    public List<Smsserver_out> selectSmsserver_out_by_recipient(String recipient){
  		return basDao.selectSmsserver_out_by_recipient(recipient);
    }
    
    public Integer selectMonthBetween(Date nextdate, Date begDate){
		return uwDao.selectMonthBetween(nextdate, begDate);
	}
    
	public List<Result> selectLaporanSaveSeries(String beg_date, String end_date){
		return commonDao.selectLaporanSaveSeries(beg_date, end_date);
	}
	
	public List<Result> selectLaporanBIIBSM(String beg_date, String end_date,String bank){
		return commonDao.selectLaporanBIIBSM(beg_date, end_date,bank);
	}
	
	public List<SpajDet> selectMstSpajDetBasedBlanko(String no_blanko){
		return commonDao.selectMstSpajDetBasedBlanko(no_blanko);
	}
	
	public List selectSpajMallBelumTrans(){
		return uwDao.selectSpajMallBelumTrans();
	}
	
	public List selectTopSellerBSM(String lcb){
		return crossSellingDao.selectTopSellerBSM(lcb);
	}
	
	public List selectPolisPending(String lus_id){
		return crossSellingDao.selectPolisPending(lus_id);
	}
	
	public void kirimUlangEmailJatis() {
		crossSellingDao.kirimUlangEmailJatis();
	}

	public Integer selectValidasiDoubleNoPolASM(String reg_spaj, String pol) {
		return crossSellingDao.selectValidasiDoubleNoPolASM(reg_spaj, pol);
	}
	
	public List<CabangBii> selectAllCabangBsm(Integer flag_aktif){
		return crossSellingDao.selectAllCabangBsm(flag_aktif);
	}
	
	public List<Fat> selectListFat(Date begdate, Date enddate, String nama, Integer posisi, String lcb_no) {
		return crossSellingDao.selectListFat(begdate, enddate, nama, posisi, lcb_no);
	}
	
	public Fat selectFat(String fatid) {
		return crossSellingDao.selectFat(fatid);
	}
	
	public List<FatHistory> selectFatHistory(String fatid) {
		return crossSellingDao.selectFatHistory(fatid);
	}
	
	public String saveFat(Fat fat, User user){
		return crossSellingDao.saveFat(fat, user);
	}
	
	public void insertLstFatHistory(FatHistory fh){
		crossSellingDao.insertLstFatHistory(fh);
	}
	
	public void saveFatLetter(String fatid, User user){
		crossSellingDao.saveFatLetter(fatid, user);
	}
	
	public String otorisasiFat(List<Fat> listFat, User user){
		return crossSellingDao.otorisasiFat(listFat, user);
	}
	
	public String konfirmasiFat(List<Fat> listFat, User user){
		return crossSellingDao.konfirmasiFat(listFat, user);
	}
	
	public String fillingFat(List<Fat> listFat, User user){
		return crossSellingDao.fillingFat(listFat, user);
	}
	
	public String terminasiFat(List<Fat> listFat, User user, String[] ket_term){
		return crossSellingDao.terminasiFat(listFat, user, ket_term);
	}
	
	public String konftermFat(List<Fat> listFat, User user){
		return crossSellingDao.konftermFat(listFat, user);
	}
	
	public Integer countFatNameTagHist(String fatid){
		return crossSellingDao.selectCountFatNameTagHist(fatid);
	}
	
	public Integer countFatCetakHist(String fatid){
		return crossSellingDao.selectCountFatCetakHist(fatid);
	}
	
	public List<String> selectSeluruhEmailCabBsm(String lcb_no) {
		return crossSellingDao.selectSeluruhEmailCabBsm(lcb_no);
	}
	
	public String selectValidasiInputFatDouble(String nama, Date tgl_lahir){
		return crossSellingDao.selectValidasiInputFatDouble(nama, tgl_lahir);
	}
	
	public void schedulerPenerimaanDanKlaimDanamasPrima(){
		String msh_name = "SCHEDULER PENERIMAAN DAN KLAIM DANAMAS PRIMA";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerPenerimaanDanKlaimDanamasPrima(msh_name);
		}
	}
	
	public void schedulerPengirimanSPHSPTKeLB(){
		schedulerDao.schedulerPengirimanSPHSPTKeLB();
	}
	
	public void schedulerSummaryCompanyWs(){
		schedulerDao.schedulerSummaryCompanyWs();
	}
	
	public void schedulerPengirimanPolisKeDelta(){
		schedulerDao.schedulerPengirimanPolisKeDelta();
	}
	
	/* Yusuf (29 Mar 2010) - Scheduler2 */
	public void insertMstSchedulerHist(String machine, String name, Date bdate, Date edate, String desc, String full_desc){
		schedulerDao.insertMstSchedulerHist(machine, name, bdate, edate, desc,full_desc);
	}
	
	public void schedulerPas(int followUpPas, int followUpPasAll, int aksepPas, int aksepPasAll, int pesertaPas, int fire, int batalPas, int batalPasAll){
		String msh_name = "SCHEDULER PAS FOLLOW UP PEMBAYARAN";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerPas(followUpPas, followUpPasAll, aksepPas, aksepPasAll, pesertaPas, fire, batalPas, batalPasAll, msh_name);
		}
	}
	
	public void schedulerEmailStatusPabsm(){
		String msh_name = "SCHEDULER EMAIL STATUS PA BSM";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerEmailStatusPabsm(msh_name);
		}
	}
	
	public void schedulerAutoUpdatePasColumn(){
		String msh_name = "SCHEDULER AUTOUPDATE PAS COLUMN";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerAutoUpdatePasColumn(msh_name);
		}
	}
	
	
	public void schedulerSummaryRK(Date tanggal){
		String msh_name = "SCHEDULER SUMMARY RK";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerSummaryRK(tanggal, msh_name);
		}
	}
	
	public void schedulerPrata(){
		String msh_name = "SCHEDULER PRATA";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerPrata(msh_name);
		}
	}
	
	public void schedulerBmi() throws DataAccessException, IOException{
		String msh_name = "SCHEDULER BMI";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerBmi(msh_name);
		}
	}
	
	public void schedulerBlacklist(){
		String msh_name = "SCHEDULER ATTENTION LIST";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerBlacklist(msh_name);
		}
	}
	
	public void schedulerFurtherRequirementBp(){
		String msh_name = "SCHEDULER FURTHER REQUIREMENT PAS BP";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerFurtherRequirementBp(msh_name);
		}
	}
	
	public void schedulerFurtherRequirementDbd(){
		String msh_name = "SCHEDULER FURTHER REQUIREMENT DBD BP/AGENCY";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerFurtherRequirementDbd(msh_name);
		}
		
	}
	
	public void schedulerFurtherRequirementMallAssurance(){
		String msh_name = "SCHEDULER FURTHER REQUIREMENT PAS MALLASSURANCE";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerFurtherRequirementMallAssurance(msh_name);
		}
		
	}
	
	public void schedulerKomisi30Hari(){
		String msh_name = "SCHEDULER KOMISI 30 HARI";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerKomisi30Hari(msh_name);
		}
		
	}
	
	public void schedulerRekapPembatalanUw(){
		String msh_name = "SCHEDULER REKAP PEMBATALAN UW";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerRekapPembatalanUw(msh_name);
		}
	}
	
	public void schedulerSummaryExpired(){
		String msh_name = "SCHEDULER SUMMARY EXPIRED";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerSummaryExpired(msh_name);
		}
	}
	
	public void schedulerSummaryAkseptasi(){
		String msh_name = "SCHEDULER SUMMARY AKSEPTASI";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerSummaryAkseptasi(msh_name);
		}
	}
	
	public void schedulerSummaryFurtherRequirementPas(){
		String msh_name = "SCHEDULER PAS FURTHER REQUIREMENTS";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerSummaryFurtherRequirementPas(msh_name);
		}
	}
	
	public Integer schedulerSPT(){
		String msh_name="SCHEDULER REMINDER AKSEPTASI SPT";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			return 1;		
		}
		return 0;
	}

	public void schedulerSummaryTerlambatCetakPolis(){
		String msh_name = "SCHEDULER SUMMARY TERLAMBAT CETAK POLIS";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerSummaryTerlambatCetakPolis(msh_name);
		}
	}

	public void summaryAkseptasiBSM(){
		String msh_name = "SCHEDULER SUMMARY AKSEPTASI BSM";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.summaryAkseptasiBSM(msh_name);
		}
		
	}
	
	public void schedulerPermintaanSpaj() {
		String msh_name = "SCHEDULER CEK PERMINTAAN SPAJ";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerCekPermintaanSpaj(msh_name);
		}
		msh_name ="SCHEDULER SPAJ BLM PERTANGGUNGJAWABAN";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerBlmPertgjwbnSpaj(msh_name);
		}
	}
	
	public Integer selectAlreadyExistScheduler(String msh_name){
		return schedulerDao.selectAlreadyExistScheduler(msh_name);
	}
	
	public void schedulerDailyCurr() {
		String msh_name ="SCHEDULER KURS BI";
		/*20200915 - as requested by Pak Kohan this scheduler is taken down
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerDailyCurr(msh_name);
		}*/
	}
	
	public void schedulercetakPolisMa() {
		String msh_name ="SCHEDULER CETAK POLIS MALLASSURANCE";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulercetakPolisMa(msh_name);
		}
	}
	
	public void schedulerOutstandingBSM(){
		String msh_name = "SCHEDULER OUTSTANDING BSM";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerOutstandingBSM(msh_name);
		}
	}
	
	public void schedulerResetCounter(){
		String msh_name = "SCHEDULER RESET COUNTER";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerResetCounter(msh_name);
		}
	}
	
	public void testEmail() {
		try {
			email.send(false, 
					"ajsjava@sinarmasmsiglife.co.id", 
					new String[] {"ariani@sinarmasmsiglife.co.id"},
//					new String[] {"deddy@sinarmasmsiglife.co.id"}, new String[]{},							
					new String[] {"deddy@sinarmasmsiglife.co.id"}, new String[]{}, 
					"Email Testing", 
					"Rin, sampai ga email ini?dari deddy.Kalau sampai, minta tolong kabarin ya.", 
					null);
		} catch (MailException e1) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e1);
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e1);
		}
	}
	

	public void schedulerDailyTTP(){
		String msh_name = "SCHEDULER PROSES INPUT TTP";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerDailyTTP(msh_name);
		}
	}
	
	public void schedulerAutoCancelPolisMallAssurance(){
		String msh_name = "SCHEDULER PEMBATALAN OTOMATIS H+14 MALLASSURANCE";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerAutoCancelPolisMallAssurance(msh_name);
		}
	}
	//Canpri - scheduler Rekap Referensi (Tambang Emas)
	/*public void schedulerRekapReferensi(){
		String msh_name = "SCHEDULER REKAP REFERENSI";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerRekapReferensi(msh_name);
		}
	}*/
	
	//Canpri - scheduler Summary Akseptasi & Further Bancass Syariah
	public void schedulerSummaryAkseptasiBancassSyariah(){
		String msh_name = "SCHEDULER SUMMARY AKSEPTASI BANCASS SYARIAH";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerSummaryAkseptasiBancassSyariah(msh_name);
		}
	}
	
	//Canpri - scheduler Reminder pengiriman hadiah pada program hadiah hari H
	public void schedulerAppointment (){
		String msh_name = "SCHEDULER REMINDER PROGRAM HADIAH";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerAppointment(msh_name);
		}
	}
	
	//Ryan - scheduler untuk SIMPOL
	public void schedulerSimpolExpired(){
		schedulerDao.schedulerSimpolExpired();
	}
	
	//Anta - scheduler untuk SMS&Email proses Polis
	public void schedulerAksepSmsEmail(){
		String msh_name = "SCHEDULER AKSEPTASI SMS DAN EMAIL";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerAksepSmsEmail(msh_name);
		}
	}
	
	//Anta - scheduler untuk reminder Event di E-MNC
	public void schedulerQuotation(){
		String msh_name = "SCHEDULER QUOTATION (E-MNC)";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerQuotation(msh_name);
		}
	}
	
	//Anta - scheduler untuk recent Event di E-MNC
	public void schedulerHistoryQuot(){
		String msh_name = "SCHEDULER QUOTATION HISTORY (E-MNC)";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerHistoryQuot(msh_name);
		}
	}
	
	//Anta - scheduler kirim report E-MNC
	public void schedulerTargetListReport(){
		String msh_name = "SCHEDULER TARGET LIST REPORT (E-MNC)";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerTargetListReport(msh_name);
		}
	}
	//lufi - scheduler kirim report pending kartu admedika
	public void schedulerpendingKartuAdmedika(){
		String msh_name = "SCHEDULER ADMEDIKA";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.pendingKartuAdmedika(msh_name);
		}	
	}
	
	//lufi - scheduler kirim report pending kartu admedika
		public void schedulerAksepSpt(){
			String msh_name = "SCHEDULER REPORT STATUS SPT";
			if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
				schedulerDao.schedulerAksepSPT(msh_name);
			}	
		}
	
	//Deddy - Scheduler untuk menginformasikan apakah mail pool berhasil berjalan atau tidak.	
	public void schedulerCekMailPool(){
		schedulerDao.schedulerCekMailPool();
	}
	
	//Yusuf - 8 Mei 2013 - Scheduler untuk insert nilai IHSG terkini ke LST_NAB_ULINK dari website
	public void schedulerDailyIHSG() {
		String msh_name ="SCHEDULER IHSG";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerIHSG(msh_name);
		}
	}
	
	/* End of Scheduler */  
	
	public String getMaxPermintaanId(String lus_id) {
		return basDao.getMaxPermintaanId(lus_id);
	}
	
	public String getKodeBandara(String lus_id) {
		return basDao.getKodeBandara(lus_id);
	}
	
	public Boolean cekNoBlankoIsExist(String lus_id,String lca_id, Integer lsjs_id, String no_blanko,Integer msab_id, Integer mss_jenis, Integer jnsTravIns) {
		return basDao.cekNoBlankoIsExist(lus_id,lca_id,lsjs_id,no_blanko,msab_id,mss_jenis,jnsTravIns);
	}
	
	public void insertFormBandara(String id,String date,String nama,String jumlh,String noBlanko,String lus_id, Integer jenis) {
		basDao.insertFormBandara(id,date,nama,jumlh,noBlanko,lus_id,jenis);
	}
	
	public void updateFormBandara(String id,String date,String nama,String jumlh,String noBlanko,String lus_id) {
		basDao.updateFormBandara(id,date,nama,jumlh,noBlanko,lus_id);
	}
	
	public void updateStockCabang(Spaj spajCabang) {
		basDao.updateStockCabang(spajCabang);
	}
	
	public String updateMstKuesionerBrand(KuesionerBrand kbrand, Date now, String lus_id) {
		String result = "";
		try {
			kbrand.setMkb_input_date(now);
			kbrand.setLus_id(lus_id);
			commonDao.updateMstKuesionerBrand(kbrand);
			result = "Data Polling berhasil disimpan";
		} catch (Exception e) {
			result = "Data Polling gagal disimpan";
		}
		return result;
	}
	
	public void updateSpajDet(SpajDet spajDet) {
		basDao.updateSpajDet(spajDet);
	}
	
	public HashMap selectFormPermintaanBandara(String id) {
		return basDao.selectFormPermintaanBandara(id);
	}
	
	public Integer cekAdmTravIns(String lus_id) {
		return basDao.cekAdmTravIns(lus_id);
	}

    public List<Map<String, Object>> selectStrukturAgen( String msag_id )
    {
		return bacDao.selectStrukturAgen( msag_id );
	}

    public List<Map<String, Object>> selectHistoryUT( Map<String, Object> params )
    {
        return worksiteDao.selectHistoryUT( params );
    }
    
    public List selectListSummaryKomisi( String periode, String bank, String transfer ,  String nama )
    {
        return worksiteDao.selectListSummaryKomisi( periode, bank, transfer,nama );
    }
    
    public List<Map<String, String>> selectListSummaryKomisiForPrint( String periode, String bank )
    {
        return worksiteDao.selectListSummaryKomisiForPrint( periode, bank );
    }
    
    public List selectBankForJurnal()
    {
        return worksiteDao.selectBankForJurnal();
    }  
	 
    public List selectJenisBdnDanNpwp(String mcl_id)
    {
        return worksiteDao.selectJenisBdnDanNpwp(mcl_id);
    } 
    
    public List selectInfoDBankForJurnal(String no_pre)
    {
        return worksiteDao.selectInfoDBankForJurnal(no_pre);
    }    
    
    public List selectDetailUpdateJurnalDiskonPerusahaan(String mcl_id)
    {
        return worksiteDao.selectDetailUpdateJurnalDiskonPerusahaan(mcl_id);
    }    
    
    public Integer getLstbId(String spaj) {
    	return uwDao.getLstbId(spaj);
    }
    
    public Agen select_detilagen2(String msag_id){
		Agen a = this.bacDao.select_detilagen2(msag_id);
		if(a == null) return new Agen(); else return a;
	}
    
    public String select_region(String lca_id, String lwk_id, String lsrg_id){
		String lsrg_nama = this.bacDao.select_region(lca_id, lwk_id, lsrg_id);
		return lsrg_nama;
	}
    
    public Agen select_detilagen_bp(String msag_id){
		Agen a = this.bacDao.select_detilagen_bp(msag_id);
		if(a == null) return new Agen(); else return a;
	}
    
    public Agen select_detilagen_bpd(String msag_id){
    	Agen a = this.bacDao.select_detilagen_bpd(msag_id);
    	if(a == null) return new Agen(); else return a;
    }
    
    public Integer selectCountMstKuesionerPelayanan(){
    	return uwDao.selectCountMstKuesionerPelayanan();
    }
    
	public KuesionerPelayananByQuestion selectPercentageQuestionnaireByQuestion(){
		return uwDao.selectPercentageQuestionnaireByQuestion();
	}
	
	public KuesionerPelayananByGroup selectPercentageQuestionnaireByGroup(){
		return uwDao.selectPercentageQuestionnaireByGroup();
	}
	
	public KuesionerPelayananAll selectPercentageQuestionnaireAll(){
		return uwDao.selectPercentageQuestionnaireAll();
	}
	
	public void  insertSpajASM(String lsjs_id,String no_blanko,String lsp_id,String lca_id, String mssd_lus_id,String mssd_desc){
		this.basDao.insertSpajASM(lsjs_id, no_blanko, lsp_id, lca_id,  mssd_lus_id, mssd_desc);
	
	}
	
	public List selectHistoryBlankoASM(String mssd_lus_id) {
		return bacDao.selectHistoryBlankoASM(mssd_lus_id);
	}
	
	public List<DropDown> getLstJnsAlasan() {
		return basDao.getLstJnsAlasan();
	}	
	
	  public List<Surat_history> selectSuratHistByDate(String begDate, String endDate, Integer allAccess, String lus_id, Integer jenis, String polis, String searchbypolis) {
	    	return basDao.selectSuratHistByDate(begDate,endDate,allAccess,lus_id,jenis,polis,searchbypolis);
	    }
    
    public String updateSuratHist(List<Surat_history> lsh) {
//    	TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
    	for(int a=0;a<lsh.size();a++) {
    		basDao.updateSuratHist(lsh.get(a));
    		uwDao.insertCsfCall(lsh.get(a).getReg_spaj(), "O", lsh.get(0).getLus_id(), null, null, lsh.get(a).getKet_fu1(), null, null, null, 1, 3);
    	}
    	return "Surat History berhasil diupdate";
    }
    
    public List<Map> selectInvReksadanaName() {
		return this.financeDao.selectInvReksadanaName();
	}
    
    public Double selectDataSubscribeUnitReksadana(String ire_reksa_no, String irt_rtrans_jn, Date irt_trans_date){
		return this.financeDao.selectDataSubscribeUnitReksadana(ire_reksa_no, irt_rtrans_jn, irt_trans_date);
	}
	
	public Double selectUnitTerakhir(String ire_reksa_no, Date irt_trans_date){
		return this.financeDao.selectUnitTerakhir(ire_reksa_no, irt_trans_date);
	}
	
	public List<Map> selectMstPolicyBasedRegSpaj(String reg_spaj){
		return this.bacDao.selectMstPolicyBasedRegSpaj(reg_spaj);
	}
	
	public int prosesStatusPasFormController(Pas pas, HttpServletRequest request, BindException err, String action)throws Exception{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String fire_id = pas.getMsp_fire_id();
		String msp_id = pas.getMsp_id();
		String msag_id = pas.getMsag_id();
		String no_kartu = pas.getNo_kartu();
//		String desc[] = new String[request.getParameterValues("textarea").length];
//		String tanggal[] = new String[request.getParameterValues("tanggal").length];
		String desc;
		String tanggal;
		String status[];
		String substatus;
		String statusAkhir;
		Integer lssaId;
		int suc = 0;
		
		Pas pas2 = uwDao.selectPasFromMspId(pas.getMsp_id());
		if(pas2 == null){
			pas2 = new Pas();
		}
		//pas2.setCabang("JAKARTA I");

		desc = pas.getMsp_ket_status();
		//substatus = request.getParameter("substatus");
		tanggal = pas.getMsp_tgl_status2();

		String deskripsi = desc;
		String tglAkhir = tanggal;
		deskripsi = deskripsi.toUpperCase();

		String lus_id = currentUser.getLus_id();
		int lspd = 2, lssp = 10;
		int liAktif = 1;
		//String flagEmail = request.getParameter("flagEmail");
		String emailAdmin = "";//uwDao.selectEmailAdmin(command.getSpaj());
		emailAdmin = pas.getEmailAdmin();//uwDao.selectPasEmailAdminCabang(pas.getNo_kartu());
		 
//		List<String> emailUser = uwDao.selectReportPasEmailList(pas.getLus_id());
//		if(emailAdminList.size() > 0){
//			emailAdmin = emailAdminList.get(0);
//		}
		//Map agen = uwDao.selectEmailAgen2(pas.getMsag_id());
		//String emailAgen = (String) agen.get("MSPE_EMAIL")+";"+currentUser.getEmail();
		String emailAgen = pas.getEmailAgen()+";"+currentUser.getEmail();
		if (err.getErrorCount() == 0){
			if("DBD AGENCY".equals(pas.getJenis_pas())){
				statusAkhir = "FURTHER REQUIREMENT DBD";
			}else if("DBD SYARIAH".equals(pas.getJenis_pas())){
				statusAkhir = "FURTHER REQUIREMENT DBD SYARIAH";
			}else if("INDIVIDU".equals(pas.getJenis_pas())){
				statusAkhir = "FURTHER REQUIREMENT PAS";
			}else{
				statusAkhir = "FURTHER REQUIREMENT"; //commonDao.selectStatusAcceptFromSpaj(command.getSpaj());
			}
					
			if("emailsave".equals(action)){
				if("DBD AGENCY".equals(pas.getJenis_pas()) || "DBD SYARIAH".equals(pas.getJenis_pas())){
					suc = kirimEmailDbd(no_kartu, fire_id, emailAdmin, emailAgen, statusAkhir,
									tglAkhir, currentUser, deskripsi, request
											.getContextPath(), pas2.getCabang(), pas.getMsp_pas_nama_pp(),
									pas.getMsp_full_name(), pas,err);
				}else{
					suc = kirimEmail(no_kartu, fire_id, emailAdmin, emailAgen, statusAkhir,
									tglAkhir, currentUser, deskripsi, request
											.getContextPath(), pas2.getCabang(), pas.getMsp_pas_nama_pp(),
									pas.getMsp_full_name(), pas,err);
				}
						
			}
			uwDao.updateMstPasSmsStatus(pas.getMsp_id(), pas.getMsp_status(), pas.getMsp_tgl_status(), pas.getMsp_ket_status(), currentUser.getLus_id());
			deskripsi = "FR: " + deskripsi;//getKodeStatusAksep(command.getLiAksep()) + deskripsi;
		}
		uwDao.insertMstPositionSpajPas(currentUser.getLus_id(), "FURTHER REQUIREMENT : "+pas.getMsp_ket_status(), pas.getMsp_fire_id(), 5);
//		}
		//command.setFlagAdd(suc);
		suc = 1;
		return suc;
	}
	
	public int prosesStatusPasPartnerFormController(Pas pas, HttpServletRequest request, BindException err, String action)throws Exception{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String fire_id = pas.getMsp_fire_id();
		String msp_id = pas.getMsp_id();
		String msag_id = pas.getMsag_id();
		String no_kartu = pas.getNo_kartu();
//		String desc[] = new String[request.getParameterValues("textarea").length];
//		String tanggal[] = new String[request.getParameterValues("tanggal").length];
		String desc;
		String tanggal;
		String status[];
		String substatus;
		String statusAkhir;
		Integer lssaId;
		int suc = 0;
		
		Pas pas2 = uwDao.selectBpFromMspId(pas.getMsp_id());
		if(pas2 == null){
			pas2 = new Pas();
		}
		//pas2.setCabang("JAKARTA I");

		desc = pas.getMsp_ket_status();
		//substatus = request.getParameter("substatus");
		tanggal = pas.getMsp_tgl_status2();

		String deskripsi = desc;
		String tglAkhir = tanggal;
		deskripsi = deskripsi.toUpperCase();

		String lus_id = currentUser.getLus_id();
		int lspd = 2, lssp = 10;
		int liAktif = 1;
		//String flagEmail = request.getParameter("flagEmail");
		//Siti Maulani, Cahyani Prajaningrum
		String emailTo = pas.getEmailTo();//"siti_m@sinarmasmsiglife.co.id;cahyani@sinarmasmsiglife.co.id";
		//String emailTo = "andy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id";
		//Stephanus Rudy, Underwriting Agency & Worksite
		//String emailCc = "stephanus@sinarmasmsiglife.co.id;underwritingagency&worksite@sinarmasmsiglife.co.id" + currentUser.getEmail();
		String emailCc = pas.getEmailCc();//currentUser.getEmail();
		//String emailCc = "andy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id;" + currentUser.getEmail();
		
//		List<String> emailUser = uwDao.selectReportPasEmailList(pas.getLus_id());
//		if(emailAdminList.size() > 0){
//			emailAdmin = emailAdminList.get(0);
//		}
		//Map agen = uwDao.selectEmailAgen2(pas.getMsag_id());
		//String emailAgen = (String) agen.get("MSPE_EMAIL");
		if (err.getErrorCount() == 0){
			if("DBD BP".equals(pas.getJenis_pas())){
				statusAkhir = "FURTHER REQUIREMENT DBD BP";
			}else if("AP/BP".equals(pas.getJenis_pas()) || "AP/BP ONLINE".equals(pas.getJenis_pas())){
				statusAkhir = "FURTHER REQUIREMENT PAS BP";
			}else{
				statusAkhir = "FURTHER REQUIREMENT BP"; //commonDao.selectStatusAcceptFromSpaj(command.getSpaj());
			}
					
			if("emailsave".equals(action)){
				if("DBD BP".equals(pas.getJenis_pas())){
					suc = kirimEmailDbd(no_kartu, fire_id, emailTo, emailCc, statusAkhir, tglAkhir,
								currentUser, deskripsi, request.getContextPath(), pas2.getCabang(),
								pas.getMsp_pas_nama_pp(), pas.getMsp_full_name(), pas,err);
				}else{
					suc = kirimEmailBp(no_kartu, fire_id, emailTo, emailCc, statusAkhir, tglAkhir,
								currentUser, deskripsi, request.getContextPath(), pas2.getCabang(),
								pas.getMsp_pas_nama_pp(),pas.getMsp_full_name(), pas,err);
				}
			}
			uwDao.updateMstPasSmsStatus(pas.getMsp_id(), pas.getMsp_status(), pas.getMsp_tgl_status(), pas.getMsp_ket_status(), currentUser.getLus_id());
			deskripsi = "FR: " + deskripsi;//getKodeStatusAksep(command.getLiAksep()) + deskripsi;
		}
		uwDao.insertMstPositionSpajPas(currentUser.getLus_id(), "FURTHER REQUIREMENT : "+pas.getMsp_ket_status(), pas.getMsp_fire_id(), 5);
//		}
		//command.setFlagAdd(suc);
		suc = 1;
		return suc;
	}
	
	public int prosesStatusPasMallFormController(Pas pas, HttpServletRequest request, BindException err, String action)throws Exception{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String fire_id = pas.getMsp_fire_id();
		String msp_id = pas.getMsp_id();
		String msag_id = pas.getMsag_id();
		String no_kartu = pas.getNo_kartu();
//		String desc[] = new String[request.getParameterValues("textarea").length];
//		String tanggal[] = new String[request.getParameterValues("tanggal").length];
		String desc;
		String tanggal;
		String status[];
		String substatus;
		String statusAkhir;
		Integer lssaId;
		int suc = 0;
		
		Pas pas2 = uwDao.selectBpFromMspId(pas.getMsp_id());
		if(pas2 == null){
			pas2 = new Pas();
		}
		//pas2.setCabang("JAKARTA I");

		desc = pas.getMsp_ket_status();
		//substatus = request.getParameter("substatus");
		tanggal = pas.getMsp_tgl_status2();

		String deskripsi = desc;
		String tglAkhir = tanggal;
		deskripsi = deskripsi.toUpperCase();

		String lus_id = currentUser.getLus_id();
		int lspd = 2, lssp = 10;
		int liAktif = 1;
		//String flagEmail = request.getParameter("flagEmail");
		//Siti Maulani, Cahyani Prajaningrum
		//String emailTo = "Agustina@sinarmasmsiglife.co.id;damianus@sinarmasmsiglife.co.id;yuli_s@sinarmasmsiglife.co.id;Lylianty@sinarmasmsiglife.co.id";
		String emailTo = pas.getEmailTo();//"mallassurance@sinarmasmsiglife.co.id";
		//String emailTo = "andy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id";
		//Stephanus Rudy, Underwriting Agency & Worksite
		//String emailCc = "stephanus@sinarmasmsiglife.co.id;underwritingagency&worksite@sinarmasmsiglife.co.id" + currentUser.getEmail();
		String emailCc = pas.getEmailCc();//"apriyani@sinarmasmsiglife.co.id;inge@sinarmasmsiglife.co.id;ningrum@sinarmasmsiglife.co.id";
		//String emailCc = "andy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id;" + currentUser.getEmail();
		
//		List<String> emailUser = uwDao.selectReportPasEmailList(pas.getLus_id());
//		if(emailAdminList.size() > 0){
//			emailAdmin = emailAdminList.get(0);
//		}
		//Map agen = uwDao.selectEmailAgen2(pas.getMsag_id());
		//String emailAgen = (String) agen.get("MSPE_EMAIL");
		if (err.getErrorCount() == 0){
			statusAkhir = "FURTHER REQUIREMENT MALL"; //commonDao.selectStatusAcceptFromSpaj(command.getSpaj());
					
			if("emailsave".equals(action)){
				if("DBD MALL".equals(pas.getJenis_pas())){
					suc = kirimEmailDbd(no_kartu, fire_id, emailTo, emailCc, statusAkhir, tglAkhir,
								currentUser, deskripsi, request.getContextPath(), "MALL DISTRIBUTION(DM/TM)",
								pas.getMsp_pas_nama_pp(), pas.getMsp_full_name(), pas,err);
				}else{
					suc = kirimEmailBp(no_kartu, fire_id, emailTo, emailCc, statusAkhir, tglAkhir,
								currentUser, deskripsi, request.getContextPath(), "MALL DISTRIBUTION(DM/TM)",
								pas.getMsp_pas_nama_pp(), pas.getMsp_full_name(), pas,err);
				}
			}
			uwDao.updateMstPasSmsStatus(pas.getMsp_id(), pas.getMsp_status(), pas.getMsp_tgl_status(), pas.getMsp_ket_status(), currentUser.getLus_id());
			deskripsi = "FR: " + deskripsi;//getKodeStatusAksep(command.getLiAksep()) + deskripsi;
		}
		uwDao.insertMstPositionSpajPas(currentUser.getLus_id(), "FURTHER REQUIREMENT : "+pas.getMsp_ket_status(), pas.getMsp_fire_id(), 5);
//		}
		//command.setFlagAdd(suc);
		suc = 1;
		return suc;
	}
	
	public int kirimEmail(String no_kartu, String fire_id, String emailAdmin, String emailAgen,
			String statusAkhir, String tglAkhir, User currentUser,
			String deskripsi, String path,
			String cabang, String pemegang,
			String tertanggung, Pas pas, BindException err)throws Exception {
		int ok = 1;
		boolean kirim=true;
		
		String css = props.getProperty("email.uw.css.satu")
				+ props.getProperty("email.uw.css.dua");
		String footer = props.getProperty("email.uw.footer");
		DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
		
		String subject=statusAkhir+" No. Kartu "+no_kartu+" a/n "+pemegang+"/"+tertanggung;
		
		String pesan = css + 
			"<table width=100% class=satu>"
				+ (cabang!=null	? 	"<tr><td>Cabang   	</td><td>:</td><td>" + cabang + "</td></tr>" : "")
				+ "<tr><td>Status   	</td><td>:</td><td>" + statusAkhir + "</td><td> Tanggal:" + tglAkhir + "</td></tr>"
				+ "<tr><td>Akseptor 	</td><td>:</td><td>" + currentUser.getName() + " [" + currentUser.getDept() + " ]<td></tr>" 
				+ "<tr><td>No. Kartu 	</td><td>:</td><td>" + no_kartu + "<td></tr>"
				+ "<tr><td>Fire Id 	</td><td>:</td><td>" + fire_id + "<td></tr>"
				+ "<tr><td>Produk		</td><td>:</td><td>"+"PAS"+"(187)"+"<td colspan=2></tr>" 
				+ "<tr><td>A/N			</td><td>:</td><td colspan=2>" + pemegang + " (Pemegang) -- " + tertanggung + " (Tertanggung) " + "</td></tr>" 
				+ "<tr><td>Keterangan	</td><td>:</td><td>" + deskripsi + "<td></tr>" 
			+"</table>";
		
		// 1.email admin dan agen ada.
		// 2.email admin ada dan agen tidak ada.
		// 3.email admin tidak ada dan agen ada.
		// 4.email admin tidak ada dan agen tidak ada.
		boolean admin, agen;
		if (emailAdmin == null || emailAdmin.equals(""))
			admin = false;
		else
			admin = true;
		if (emailAgen == null || emailAgen.equals(""))
			agen = false;
		else
			agen = true;
		
		String noSurat=null;
		String mm,yy,dir = "temp";
		Integer mscoNumDec = null;
		String IdPusat="01";
		
		String outputDir=null;

		sendAgenNAdmin(this, "",subject, pesan, emailAdmin, emailAgen,
						currentUser, footer, agen);
		

		return ok;
	}
	
	public int kirimEmailBp(String no_kartu, String fire_id, String emailTo, String emailCc,
			String statusAkhir, String tglAkhir, User currentUser,
			String deskripsi, String path,
			String cabang, String pemegang,
			String tertanggung, Pas pas, BindException err)throws Exception {
		int ok = 1;
		boolean kirim=true;
		
		String css = props.getProperty("email.uw.css.satu")
				+ props.getProperty("email.uw.css.dua");
		String footer = props.getProperty("email.uw.footer");
		DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
		
		String subject=statusAkhir+" a/n "+pemegang+"/"+tertanggung;
		
		String pesan = css + 
			"<table width=100% class=satu>"
				+ (cabang!=null	? 	"<tr><td>Cabang   	</td><td>:</td><td>" + cabang + "</td></tr>" : "")
				+ "<tr><td>Status   	  </td><td>:</td><td>" + statusAkhir + "</td><td> Tanggal:" + tglAkhir + "</td></tr>"
				+ "<tr><td>Akseptor 	  </td><td>:</td><td>" + currentUser.getName() + " [" + currentUser.getDept() + " ]<td></tr>" 
				+ "<tr><td>No. Registrasi </td><td>:</td><td>" + fire_id + "<td></tr>"
				+ "<tr><td>Produk		  </td><td>:</td><td>"+"PAS"+"(187)"+"<td colspan=2></tr>" 
				+ "<tr><td>A/N			  </td><td>:</td><td colspan=2>" + pemegang + " (Pemegang) -- " + tertanggung + " (Tertanggung) " + "</td></tr>" 
				+ "<tr><td>Keterangan	  </td><td>:</td><td>" + deskripsi + "<td></tr>" 
			+"</table>";
		
		// 1.email admin dan agen ada.
		// 2.email admin ada dan agen tidak ada.
		// 3.email admin tidak ada dan agen ada.
		// 4.email admin tidak ada dan agen tidak ada.
		boolean admin, agen;
		if (emailTo == null || emailTo.equals(""))
			admin = false;
		else
			admin = true;
		if (emailCc == null || emailCc.equals(""))
			agen = false;
		else
			agen = true;
		
		String noSurat=null;
		String mm,yy,dir = "temp";
		Integer mscoNumDec = null;
		String IdPusat="01";
		
		String outputDir=null;

		sendAgenNAdmin(this, "",subject, pesan, emailTo, emailCc,
						currentUser, footer, agen);
		

		return ok;
	}
	
	public int kirimEmailDbd(String no_kartu, String fire_id, String emailTo, String emailCc,
			String statusAkhir, String tglAkhir, User currentUser,
			String deskripsi, String path,
			String cabang, String pemegang,
			String tertanggung, Pas pas, BindException err)throws Exception {
		int ok = 1;
		boolean kirim=true;
		
		String css = props.getProperty("email.uw.css.satu")
				+ props.getProperty("email.uw.css.dua");
		String footer = props.getProperty("email.uw.footer");
		DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
		
		String subject=statusAkhir+" a/n "+pemegang+"/"+tertanggung;
		
		String pesan = css + 
			"<table width=100% class=satu>"
				+ (cabang!=null	? 	"<tr><td>Cabang   	</td><td>:</td><td>" + cabang + "</td></tr>" : "")
				+ "<tr><td>Status   	  </td><td>:</td><td>" + statusAkhir + "</td><td> Tanggal:" + tglAkhir + "</td></tr>"
				+ "<tr><td>Akseptor 	  </td><td>:</td><td>" + currentUser.getName() + " [" + currentUser.getDept() + " ]<td></tr>" 
				+ "<tr><td>No. Registrasi </td><td>:</td><td>" + fire_id + "<td></tr>"
				+ "<tr><td>Produk		  </td><td>:</td><td>"+"DBD"+"(203)"+"<td colspan=2></tr>" 
				+ "<tr><td>A/N			  </td><td>:</td><td colspan=2>" + pemegang + " (Pemegang) -- " + tertanggung + " (Tertanggung) " + "</td></tr>" 
				+ "<tr><td>Keterangan	  </td><td>:</td><td>" + deskripsi + "<td></tr>" 
			+"</table>";
		
		// 1.email admin dan agen ada.
		// 2.email admin ada dan agen tidak ada.
		// 3.email admin tidak ada dan agen ada.
		// 4.email admin tidak ada dan agen tidak ada.
		boolean admin, agen;
		if (emailTo == null || emailTo.equals(""))
			admin = false;
		else
			admin = true;
		if (emailCc == null || emailCc.equals(""))
			agen = false;
		else
			agen = true;
		
		String noSurat=null;
		String mm,yy,dir = "temp";
		Integer mscoNumDec = null;
		String IdPusat="01";
		
		String outputDir=null;

		sendAgenNAdmin(this, "",subject, pesan, emailTo, emailCc,
						currentUser, footer, agen);
		

		return ok;
	}
	
	private void sendAgenNAdmin(final UwManager uwManager, final String noSurat,final String subject,final String pesan,final String emailAdmin,final String emailAgen,
			final User currentUser,final String footer,final boolean agen)throws MessagingException{

//		 email.getMailSender().send(new MimeMessagePreparator() {
//			   public void prepare(MimeMessage mimeMessage) throws MessagingException {
//			     MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			     String from = null;
			     String to[] = null;
			     String cc[]=null;emailAgen.split(";");
			     String bcc[] = null;
//			     message.setFrom(currentUser.getEmail());
//			     message.setTo(emailAdmin.split(";"));
			     from =currentUser.getEmail();
			     to = emailAdmin.split(";");
			     bcc = new String[]{"andy@sinarmasmsiglife.co.id"};
			     
				 if(emailAgen!=null && !emailAgen.trim().equals(""))cc = emailAgen.split(";");
//				 message.setCc(cc);
//				 message.setBcc("andy@sinarmasmsiglife.co.id");
//			     message.setSubject(subject);
//			     message.setText(pesan+footer,true);
//			     message.addInline("myLogo", new ClassPathResource(props.getProperty("images.ttd.ekalife")));
			     List<DropDown> ListImage = new ArrayList<DropDown>();
			     ListImage.add(new DropDown("myLogo",props.getProperty("pdf.dir.syaratpolis")+"\\images\\ekalife.gif"));
			     email.sendImageEmbeded(true, from, to, cc, bcc, subject, pesan+footer, null, ListImage);
//			    }
//			 });	
	}
	
	public Reinsurer getDataReinsurer(Integer lsre_id) {
		return uwDao.getDataReinsurer(lsre_id);
	}
	
	public String prosesReinsurer(Reinsurer reinsurer) {
		if(reinsurer.getType_reinsurer() == 0) {
			uwDao.insertNewReinsurer(reinsurer);
			return "Insert data baru berhasil";
		}
		else if(reinsurer.getType_reinsurer() == 1) {
			uwDao.updateReinsurer(reinsurer);
			return "Update data berhasil";
		} 
		
		return "";
	}
	
    public String inputJournalProcess( String mclId, String periode, String bankId, String noGiro )
    {
    	return worksiteDao.inputJournalProcess(mclId, periode, bankId, noGiro);
    }
    
    public void insertMstCompanyWs( String mcl_id, Date tgl_invoice, Date tgl_bayar, BigDecimal jumlah_invoice, BigDecimal jumlah_bayar,
    		String periode, BigDecimal nomor, String lku_id)
    {
		this.uwDao.insertMstCompanyWs( mcl_id, tgl_invoice, tgl_bayar, jumlah_invoice, jumlah_bayar, periode, nomor, lku_id );
	}
    
    public void insertMstInbox( MstInbox mstInbox, MstInboxHist mstInboxHist ){
    	this.uwDao.insertMstInbox( mstInbox );
    	this.uwDao.insertMstInboxHist( mstInboxHist );
    }
    
    public void insertMstInboxHist( MstInboxHist mstInboxHist ){
    	this.uwDao.insertMstInboxHist( mstInboxHist );
    }
    
    public void insertMstInboxChecklist( MstInboxChecklist mstInboxChecklist ){
    	this.uwDao.insertMstInboxChecklist( mstInboxChecklist );
    }
    
	public List selectCompanyWsList(String mcl_id, String jenis){
		return this.worksiteDao.selectCompanyWsList(mcl_id, jenis);
	}
	
	public List<Company_ws> selectMstSummaryWsDet(String mcl_id, String jenis){
		List<Company_ws> result = this.uwDao.selectMstSummaryWsDet(mcl_id, jenis);
		if( result != null && result.size() > 0 ){
			for( int i = 0 ; i < result.size() ; i++ ){
				result.get(i).setJumlah_invoice_conv(FormatNumber.convertToTwoDigit(result.get(i).getJumlah_invoice()));
				result.get(i).setTgl_invoice_indo( FormatDate.toIndonesian( result.get(i).getTgl_invoice() ) );
			}
		}
		return result;
	}
	
	public void updateMstCompanyWs(Date tgl_bayar, String mcl_id, BigDecimal jumlah_bayar, String periode, BigDecimal nomor, Map<String, Object> paramsHistoryUt){
		insertMstHistoryUT( paramsHistoryUt );
		this.uwDao.updateMstCompanyWs(tgl_bayar, mcl_id, jumlah_bayar, periode, nomor);
	}
    
    public void insertMstHistoryUT( Map params )
    {
		this.uwDao.insertMstHistoryUT( params );
	}
	
    public String selectMaxPeriodeMstDiskonPerusahaan( Map params )
    {
        return worksiteDao.selectMaxPeriodeMstDiskonPerusahaan( params );
    }
    
    public BigDecimal selectNettDiskonPerusahaan( BigDecimal nominal, String periode, String mcl_id, String no_pre )
    {
        return worksiteDao.selectNettDiskonPerusahaan( nominal, periode, mcl_id, no_pre );
    }
    
    public BigDecimal selectMaxNomorCompanyWs( String periode, String mcl_id )
    {
        return worksiteDao.selectMaxNomorCompanyWs( periode, mcl_id );
    }
    
    public BigDecimal selectPajakDiskonPerusahaan( BigDecimal nominal, String periode, String mcl_id, String no_pre )
    {
        return worksiteDao.selectPajakDiskonPerusahaan( nominal, periode, mcl_id, no_pre );
    }
    
    public Map selectInfoDiskonPerusahaan( String jenis, String mcl_id )
    {
        return worksiteDao.selectInfoDiskonPerusahaan( jenis, mcl_id );
    }
    
    public List<DropDown> selectPeriodeList(String mcl_id){
    	return worksiteDao.selectPeriodeList(mcl_id);
    }
    
    public String allJournalProcess( String mclId, String periode, String bankId, String noGiro,  Map<String, Object> paramsHistoryUt )
    {
    	String noPre = null;
    	insertMstHistoryUT( paramsHistoryUt );
    	noPre = worksiteDao.inputJournalProcess(mclId, periode, bankId, noGiro);
    	return noPre;
    }
    
    public Integer selectMaxNoMstHistoryUT( Map params )
    {
        return worksiteDao.selectMaxNoMstHistoryUT( params );
    }
    
    public User selectLoginModelAuthentication(User user) {
    	return this.commonDao.selectLoginModelAuthentication(user);
	}
    
    public List<Map> selectDaftarAkseptasiNyangkut(int lssa_id, boolean isEmailRequired, String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12, boolean isProductSekuritas) throws DataAccessException{
    	return uwDao.selectDaftarAkseptasiNyangkut(lssa_id, isEmailRequired, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,isProductSekuritas);
    }
    
    public List selectDetailStockSimasCard() {
		return basDao.selectDetailStockSimasCard();
	}

	/*public List<Map> getListJenisInvest() {
		return financeDao.getListJenisInvest();
	}
	
	public void insertLstNabUlink(String lji_id, Integer lnu_type, String lnu_tgl, BigDecimal lnu_nilai) {
		financeDao.insertLstNabUlink(lji_id, lnu_type, lnu_tgl, lnu_nilai);
	}*/
    
    public List selectdetilinvestasimallspaj(String spaj){
		return this.bacDao.selectdetilinvestasimallspaj(spaj);
	}
    
    public List selectdetilinvestasisyariah(String spaj){
		return this.bacDao.selectdetilinvestasisyariah(spaj);
	}
    
    public String insertMedQuest(MedQuest medQuest) {
    	List<MedQuest> mq = selectMedQuest(medQuest.getReg_spaj(),medQuest.getMste_insured_no());
    	if(mq.size() != 0) return uwDao.updateMedQuest(medQuest);
    	return uwDao.insertMedQuest(medQuest);
    } 
    
    public List<MedQuest> selectMedQuest(String spaj, Integer insured_no) {
    	return uwDao.selectMedQuest(spaj,insured_no);
    }
    
    public List<Map> selectHealthChecklist(String spaj, Integer insured_no){
    	return uwDao.selectHealthChecklist(spaj,insured_no);
    }
    
    public List<Map> selectListHealth(){
    	return uwDao.selectListHealth();
    }
    
        public List selectDetailInvestasiMall(Integer kode_flag){
		//apabila bukan produk link, return null
		if(kode_flag != null) if(kode_flag.intValue() == 0 || kode_flag.intValue() == 1) return null;
		return this.bacDao.selectdetilinvestasimall(kode_flag);
	}
    
    public InvestasiUtama selectinvestasiutamamall(String spaj) {
		return (InvestasiUtama) this.bacDao.selectinvestasiutamamall(spaj);
	}
    
    public Integer selectPolicyRelation(String reg_spaj){
    	return uwDao.selectPolicyRelation(reg_spaj);
    }
    
    public Boolean agenIsExist(String msag_id, String birth_date){
    	Integer count = commonDao.agenIsExist(msag_id,birth_date);
    	
    	return count == 0 ? false : true;
    }
    
	public String saveDataAbsenAgent(String msag_id, String birth_date, String lus_id){
		String hasil = null;
		try{
			commonDao.insertMstAgentAbsen(msag_id,lus_id);
		}catch(DataAccessException e){
			//logger.error("ERROR :", e);
			int result = commonDao.updateMstAgentAbsen(msag_id,lus_id);
			if(result > 0) hasil = "Data absensi BERHASIL disimpan.";
			else hasil = "Data absensi GAGAL disimpan. Mohon dicoba kembali, atau hubungi IT kami. Terima kasih.";
		}
		if(hasil == null) hasil = "Data absensi BERHASIL disimpan.";
		return hasil;
	}
	
	public Integer selectAbsenAgen(String msag_id) {
		return commonDao.selectAbsenAgen(msag_id);
	}
	
	public List<Absen> selectMstAgentAbsen(Integer tipe,String lus_id) {
		return commonDao.selectMstAgentAbsen(tipe,lus_id);
	}
	
	/*public List<DropDown> selectLstRider() {
		return uwDao.selectLstRider();
	}
	
	public void insertDelRider(String spaj, List<DelRider> delRider, Integer flagAdd) {
		if(flagAdd ==2){//simpan
			//hapus semua delete rider
			uwDao.deleteMstPositionSpaj(spaj,"DELETE RIDER");
			int i = 0;
			while(i<delRider.size()) {
				String desc = "DELETE RIDER ["+delRider.get(i).getRider()+"] "+delRider.get(i).getDescription();
				try {
					uwDao.insertMstPositionSpaj(delRider.get(i).getLus_id(), desc, spaj, i);
					i++;
				} catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		}else if(flagAdd ==3){//delete
			for(int i=0;i<delRider.size();i++){
				if(delRider.get(i).getCek()!=null && delRider.get(i).getCek()==1)
					uwDao.deleteMstPositionSpaj(spaj,"DELETE RIDER ["+delRider.get(i).getRider()+"] ");
			}
		}
	}
	
	public List<DelRider> selectLsDelRider(String spaj, String kata) {
		return  uwDao.selectLsDelRider(spaj,kata);
	}*/
	
	public void insertMstBiayaUlink(Product productIns) {
		bacDao.insertMstBiayaUlink(productIns);
	}
	
	public List<DropDown> selectMiaMsagId() {
		return basDao.selectMiaMsagId();
	}
	
	public List<DropDown> selectlsRegion(String lca_id) {
		return basDao.selectlsRegion(lca_id);
	}
	
	public List<DropDown> selectlsBank() {
		return basDao.selectlsBank();
	}
	
	public String isAgentExist(String name, String birth_date) {
		return basDao.isAgentExist(name,birth_date);
	}
	
	public String saveMasterInputAgen(Mia mia) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		
		Long li_urut = bacDao.select_counter(65, "01")+1;
		Map param = new HashMap();
		param.put("intIDCounter", li_urut);
		param.put("no", 65);
		param.put("kodecbg", "01");
		worksiteDao.update_mst_counter(param);
		mia.setMia_agensys_id(df.format(commonDao.selectSysdate())+FormatString.rpad("0",li_urut.toString(),4));
		
		String ls_recruit = mia.getMsag_id();
		if(ls_recruit != null) {
			String ls_recruiter = mia.getMia_recruiter();
			
			Integer li_temp = basDao.selectCountMstRecuiter(ls_recruiter);
			if(li_temp <= 0) {
				return "Proses Insert Rekruter Dulu di Master Agent untuk kode Rekruter : " + ls_recruiter + "!!!";
			}
			basDao.updateMstDetRecruiter(ls_recruiter,ls_recruit);
		}
		basDao.insertMstInputAgensys(mia);
		
		return mia.getMia_agensys_id();
	}
	
	//insert ke eka.mst_input_agencys di ganti ke eka.mst_kuesioner
	public String saveMasterInputAgenUpload(Mia mia, String pas_reg_id) throws ParseException {
		Kuesioner mks = new Kuesioner();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat db = new SimpleDateFormat("dd-MMM-yyyy");
		
		//inisialisasi model kuesioner
		mks.setMku_region("660000");
		mks.setMku_regional("660000");
		mks.setMku_jenis_rekrut("3");
		mks.setMku_first(mia.getMia_nama());
		mks.setMku_place_birth(mia.getMia_birth_place());
		mks.setMku_date_birth(db.parse(mia.getMia_birth_date()));
		mks.setMku_alamat(mia.getMia_alamat());
		mks.setMku_no_identity(mia.getMia_ktp());
		mks.setMsrk_id(mia.getMia_recruiter());
		mks.setLus_id(mia.getLus_id());
		mks.setMku_tglinput(dt.parse(mia.getMia_input_date()));
		mks.setTgl_aktif(dt.parse(mia.getMia_tgl_aktif()));
		mks.setBeg_date(dt.parse(mia.getMia_awal_kontrak()));
		mks.setEnd_date(dt.parse(mia.getMia_akhir_kontrak()));
		mks.setMku_tglkues(dt.parse(mia.getMia_awal_kontrak()));
		mks.setMku_tgl_berkas(dt.parse(mia.getMia_tgl_berkas()));
		
		Date now = commonDao.selectSysdate();
		Date birth = mks.getMku_date_birth();
		int tanggal1 = now.getDate();
		int bulan1 = now.getMonth()+1;
		int tahun1 = now.getYear()+1900;
		
		int tanggal2 = birth.getDate();
		int bulan2 = birth.getMonth()+1;
		int tahun2 = birth.getYear()+1900;
		
		//hit umur ttg, pp, pic
		f_hit_umur umr= new f_hit_umur();
		Integer umur =  umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
		
		if(umur<23)mks.setMku_usia("1");
		else if(umur >= 23 && umur <= 24)mks.setMku_usia("2");
		else if(umur >= 25 && umur <= 29)mks.setMku_usia("3");
		else if(umur >= 30 && umur <= 39)mks.setMku_usia("4");
		else if(umur >= 40)mks.setMku_usia("5");
		
		mks.setMku_jenis_cabang(2);
		
		//pendidikan
		if(mia.getPendidikan().equalsIgnoreCase("SD"))mks.setMku_pendidikan("5");
		else if(mia.getPendidikan().equalsIgnoreCase("SLTP"))mks.setMku_pendidikan("5");
		else if(mia.getPendidikan().equalsIgnoreCase("SLTA"))mks.setMku_pendidikan("3");
		else if(mia.getPendidikan().equalsIgnoreCase("D1"))mks.setMku_pendidikan("2");
		else if(mia.getPendidikan().equalsIgnoreCase("D2"))mks.setMku_pendidikan("2");
		else if(mia.getPendidikan().equalsIgnoreCase("D3"))mks.setMku_pendidikan("2");
		else if(mia.getPendidikan().equalsIgnoreCase("D4"))mks.setMku_pendidikan("2");
		else if(mia.getPendidikan().equalsIgnoreCase("S1"))mks.setMku_pendidikan("1");
		else if(mia.getPendidikan().equalsIgnoreCase("S2"))mks.setMku_pendidikan("1");
		else if(mia.getPendidikan().equalsIgnoreCase("S3"))mks.setMku_pendidikan("1");
		
		//status nikah
		if(mia.getStatus_nikah().equalsIgnoreCase("S"))mks.setMku_status("2");
		else if(mia.getStatus_nikah().equalsIgnoreCase("M"))mks.setMku_status("1");
		
		//mku_no_reg
		Calendar tgl_sekarang = Calendar.getInstance();
		Integer bln1 = tgl_sekarang.getTime().getMonth()+1;
		Integer thn1 = tgl_sekarang.getTime().getYear()+1900;
		String tahun = Integer.toString(thn1.intValue());
		tahun = tahun.substring(2, 4);
		String waktu = FormatString.rpad("0",Integer.toString(bln1.intValue()),2)+FormatString.rpad("0",tahun ,2);
		String region = mks.getMku_regional();
		String lca_id = region.substring(0,2);
		String kunci = lca_id + waktu; // lca_id | bulan | tahun
		String max = (String) rekruitmentDao.no_reg_s(kunci);
		/*Double dmax = rekruitmentDao.no_reg(kunci);
		String max = null;
		if(dmax!=null)max = dmax.toString();*/		
		
		Integer juml_x = mks.getJuml_daftarTanggungan();
		if(juml_x==null){
			juml_x=0;
		}
		
		if (max == null)
		{
			max = "";
		}
		String no_reg = null;
		if (max.equalsIgnoreCase(""))
		{
			Integer no = new Integer(1);
			no_reg = kunci+FormatString.rpad("0",Integer.toString(no.intValue()),4);
		}else{
			
			//Integer nilai =Integer.parseInt(max);
			String register = max.substring(6,10);
			Integer count1 = Integer.parseInt(register);
			count1 = new Integer(count1.intValue() + 1);
			register = FormatString.rpad("0",Integer.toString(count1.intValue()),4);
			no_reg = FormatString.rpad("0",kunci+register,10);
			//no_reg = FormatString.rpad("0",Integer.toString(nilai.intValue()+1),10);
		}
		mks.setMku_no_reg(no_reg);
		
		//ini untuk data kedepan tidak diperlukan
		mks.setMku_pekerjaan("0");
		mks.setMku_penghasilan("0");
		mks.setMku_pengalaman("0");
		mks.setMku_kendaraan("0");
		mks.setMku_organisasi("0");
		mks.setMku_tinggal("0");
		//end inisialisasi
	
		Long li_urut = bacDao.select_counter(65, "01")+1;
		Map param = new HashMap();
		param.put("intIDCounter", li_urut);
		param.put("no", 65);
		param.put("kodecbg", "01");
		worksiteDao.update_mst_counter(param);
		mia.setMia_agensys_id(df.format(commonDao.selectSysdate())+FormatString.rpad("0",li_urut.toString(),4));
		
		String ls_recruit = mia.getMsag_id();
		if(ls_recruit != null) {
			String ls_recruiter = mia.getMia_recruiter();
			
			Integer li_temp = basDao.selectCountMstRecuiter(ls_recruiter);
			if(li_temp <= 0) {
				return "Proses Insert Rekruter Dulu di Master Agent untuk kode Rekruter : " + ls_recruiter + "!!!";
			}
			basDao.updateMstDetRecruiter(ls_recruiter,ls_recruit);
		}
		//basDao.insertMstInputAgensys(mia);
		rekruitmentDao.insertmst_kuesioner(mks);
		
		if(pas_reg_id != null){
			Map paramPas = new HashMap();
			//paramPas.put("no_register", mia.getMia_agensys_id());
			paramPas.put("no_register", mks.getMku_no_reg());
			paramPas.put("reg_id", pas_reg_id);
			uwDao.updatePasNoRegister(paramPas);
		}
		
		//return mia.getMia_agensys_id();
		return mks.getMku_no_reg();
	}
	
	public void updateMasterInputAgen(Mia mia) {
		basDao.updateMasterInputAgen(mia);
	}
	
	public void update_mst_diskon_perusahaan(String mcl_id, String periode, BigDecimal nominal, BigDecimal jumlah_edit_nominal, String jenis_edit_nominal,
			String user_edit_nominal){
		Map param = new HashMap();
		param.put("mcl_id", mcl_id);
		param.put("periode", periode);
		param.put("nominal", nominal);
		param.put("jumlah_edit_nominal", jumlah_edit_nominal);
		param.put("jenis_edit_nominal", jenis_edit_nominal);
		param.put("user_edit_nominal", user_edit_nominal);
		worksiteDao.update_mst_diskon_perusahaan(param);
	}
	
	public void deleteMasterInputAgen(String agensys_id) {
		basDao.deleteMasterInputAgen(agensys_id);
	}
	
	public Integer getAgenBlackList(String name, String birth_date) {
		return basDao.getAgenBlackList(name,birth_date);
	}
	
	public Integer wf_cek_rekruter(String as_agent) {
		return basDao.wf_cek_rekruter(as_agent);
	}
	
	public Integer getLevelRecruiter(String ls_agent, Integer ls_lca) {
		return basDao.getLevelRecruiter(ls_agent,ls_lca);                                                                                                                                                                                           
	}
	
	public List select_nama_perusahaan_by_mcl_id(String mcl_id){
		return this.worksiteDao.select_nama_perusahaan_by_mcl_id(mcl_id);
	}
	
	public Mia getMstInputAgensys(String agensys_id) {
		return basDao.getMstInputAgensys(agensys_id);
	}
	
	public List getMisFromName(String nama) {
		return basDao.getMisFromName(nama);
	}	
	
	public List getMisFromSpaj(String spaj) {
		return basDao.getMisFromSpaj(spaj);
	}
	
	public List<DropDown> selectLsAgentAbsen(String lus_id) {
		return commonDao.selectLsAgentAbsen(lus_id);
	}
	
	public List<DropDown> selectLsAdminActive() {
		return commonDao.selectLsAdminActive();
	}
	
	public KuesionerBrand selectKuesionerBrand(String lus_id) {
		return commonDao.selectKuesionerBrand(lus_id);
	}
	
	public Double selectSumPremiExtra(String spaj){
		return uwDao.selectSumPremiExtra(spaj);
	}
	
	public Double selectSumPremiRider(String spaj){
		return uwDao.selectSumPremiRider(spaj);
	}
	
	public String sequenceKuitansi() {
		return this.sequence.sequenceKuitansi();
	}
	
	public void updateVirtualAccountBySpaj(String reg_spaj) {
		bacDao.updateVirtualAccountBySpaj(reg_spaj);
	}

	public String selectNoKartuFromId(String msp_id) {
		return this.uwDao.selectNoKartuFromId(msp_id);
	}
	
	public String selectNoKartuPasFromSpaj(String reg_spaj) {
		return this.uwDao.selectNoKartuPasFromSpaj(reg_spaj);
	}
	
	public Map selectKartuPas(String no_kartu) {
		return this.uwDao.selectKartuPas(no_kartu);
	}
	
	public List selectUwPasInfo(String showTabel, String msp_id) {
		return this.uwDao.selectUwPasInfo(showTabel, msp_id);		
	}
		
	public List<Map> selectCetakPolisMaKemarin() {
		return uwDao.selectCetakPolisMaKemarin();
	}
	
	public List<Map> selectPolisMaBlmAcp() {
		return uwDao.selectPolisMaBlmAcp();
	}
	
	public List selectFrPasBp(){
		Map params=new HashMap();
		return uwDao.selectFrPasBp();
	}
	
	public Powersave select_powersaver_baru(String spaj){
		Powersave result = (Powersave) this.bacDao.select_powersaver_baru(spaj); 
		return result;
	}
	
	public InputTopup selectInputTopupBaruPowerSave(String reg_spaj) throws DataAccessException{
		return bacDao.selectInputTopupBaruPowerSave(reg_spaj);
	}
	
	public Integer selectNoTranPsave(String no_trx,Integer tu_ke) {
		return uwDao.selectNoTranPsave(no_trx,tu_ke);
	}
	
	public Long selectCountMstPowerSaveBaru(String spaj){
		return (Long)uwDao.selectCountMstPowerSaveBaru(spaj);
	}
	
	public List selectMstPowerSaveProsesBaru(String spaj, Integer mpsKode, Integer mpsNew){
		return bacDao.selectMstPowerSaveProsesBaru(spaj,mpsKode,mpsNew);
	}
	
	public ParameterClass selectMstPowersaveBaruDpDate(String spaj){
		return uwDao.selectMstPowersaveBaruDpDate(spaj);
	}
	
	public Map selectBackupReas(String spaj){
		return uwDao.selectBackupReas(spaj);
	}
	
	public List select_produkutama_admin_mall() {
		return this.bacDao.select_produkutama_admin_mall();
	}
	
	public List selectReportJTPowersave(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id, String kategori) {
		return this.basDao.selectReportJTPowersave(lca, lwk, lsrg, bdate, edate, lus_id, kategori);
	}
	
	public List selectreportPrintPolisCabang(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id) {
		return this.basDao.selectreportPrintPolisCabang(lca, lwk, lsrg, bdate, edate, lus_id);
	}
	
	public List selectreportSimascardCabang(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id) {
		return this.basDao.selectreportSimascardCabang(lca, lwk, lsrg, bdate, edate, lus_id);
	}
	
	public List selectreportSummaryInput(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id) {
		return this.basDao.selectreportSummaryInput(lca, lwk, lsrg, bdate, edate, lus_id);
	}
	
	public List<Followup> selectReportSummaryInputRegisterAgen(String cabang, String userinput, String bdate, String edate, String userBas, String jn_report) {
		return this.basDao.selectReportSummaryInputRegisterAgen(cabang, userinput, bdate, edate, userBas, jn_report);
	}
	
	public Map selectHitungBungaPinjaman(String spaj){
		return this.basDao.selectHitungBungaPinjaman(spaj);
	}
	
	public Map selectMstPinjaman(String reg_spaj){
		return this.basDao.selectMstPinjaman(reg_spaj);
	}
	
	public List selectReportSummaryAgentContract(String bdate, String edate, String userBas, String jn_report){
		return this.basDao.selectReportSummaryAgentContract(bdate, edate, userBas, jn_report);
	}
	
	public void saveAgentContractHist(User currentUser, Map<String, Object> content){
		this.basDao.saveAgentContractHist(currentUser, content);
	}
	
	public Map selectAgentContract(String mac_id) {
		return this.basDao.selectAgentContract(mac_id);
	}	
	
	public List<DropDown> selectReksadanaByName(String fund) {
		return this.financeDao.selectReksadanaByName(fund);
	}
		
	public String selectprodukBSM (int lsbs_id, int lsdbs_number) {
		return this.bacDao.selectprodukBSM(lsbs_id, lsdbs_number);
	}

	public List selectReportDemografiBSM(Map m, Integer jn_bank) {
		return this.basDao.selectReportDemografiBSM(m,jn_bank);
	}

	public List selectReportSummaryJapaneseDesk(String bdate, String edate) {
		return this.basDao.selectReportSummaryJapaneseDesk(bdate, edate);
	}
	
	public Map selectPolisBiasa(String reg_spaj){
		return this.uwDao.selectPolisBiasa(reg_spaj);
	}
	
	public List selectViewerEndors(String noendors) {
		return this.uwDao.selectViewerEndors(noendors);
	}
	
	public List selectHistoryPengajuan(String mcl_first, Date birth) {
		return this.uwDao.selectHistoryPengajuan(mcl_first, birth);
	}
	
	public List selectBlacklist(String mcl, String type, Date tgl_lahir) {		
		return this.uwDao.selectBlacklist(mcl, type, tgl_lahir);
	}
	
	public List selectProdukRider(String lsbs_id) {
		return this.bacDao.selectProdukRider(lsbs_id);
	}
	
	public void deleteCekValid(String reg_spaj){
		uwDao.deleteCekValid(reg_spaj);
	}
	
//	public void schedulerAdmissionBridge(){
//		schedulerDao.schedulerAdmissionBridge();
//	}
	
	public Boolean selectPesertaUtamaOrTambahan(Integer lsbs_id, Integer lsdbs_number){
		return this.bacDao.selectPesertaUtamaOrTambahan(lsbs_id, lsdbs_number);
	}
		
	public List <Map> selectCabangKK(String lcb_no){
		return bacDao.selectCabangKK(lcb_no);
	}

	public List<Map> selectCabangBridge() {
		return basDao.selectCabangBridge();
	
	}
	public List getNamaBridge(String nama) {
		return basDao.getNamaBridge(nama);
	}	
	
	public List getKodeBridge(String kode) {
		return basDao.getKodeBridge(kode);
	}
	
	public Map dataAgenBridge(String msag_id){
		return basDao.selectDataAgenBridge(msag_id);
	}
	
	public void updateKodeBridge(String msag_id, String lsrg_id )
	{
		basDao.updateKodeBridge(msag_id, lsrg_id);
	}

	public String saveHadiah(Hadiah hadiah, User currentUser) {
		return basDao.saveHadiah(hadiah, currentUser);
	}
	
	public String transferDocInputHadiah(Hadiah hadiah, User currentUser) {
		return basDao.transferDocInputHadiah(hadiah, currentUser);
	}
	
	public String cancelHadiah(Hadiah hadiah, User currentUser) {
		return basDao.cancelHadiah(hadiah, currentUser);
	}
	
	public String printMemoHadiah(Hadiah hadiah, User currentUser) {
		return basDao.printMemoHadiah(hadiah, currentUser);
	}

	public Hadiah selectLstHadiah(int lhc_id, int lh_id) {
		return basDao.selectLstHadiah(lhc_id, lh_id);
	}

	public List<Map> selectHadiahHist(String reg_spaj, Integer mh_no) {
		return basDao.selectHadiahHist(reg_spaj, mh_no);
	}
	
	//show tanggal history
	public List<Map> selecttglHistory(String reg_spaj) {
		return basDao.selecttglHistory(reg_spaj);
	}
	
	public Hadiah selectMstHadiah(String reg_spaj, Integer mh_no) {
		return basDao.selectMstHadiah(reg_spaj, mh_no);
	}
	
	public Hadiah selectMstHadiahRE(String reg_spaj, Integer mh_no) {
		return basDao.selectMstHadiahRE(reg_spaj, mh_no);
	}
	
	public List<Map<String, Object>> select_produkutamacfl() {
		return this.bacDao.select_produkutamacfl();
	}
	
	public Map selectNamaPlan(Map map){
		return bacDao.selectNamaPlan(map);
	}
	
	public Cmdeditbac savingspaj(Object cmd,Errors err,String keterangan, User currentUser) throws ServletException,IOException,Exception
	{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		if (keterangan.equals("input"))
		{
			return this.savingBacCFL.insertspajbaru(cmd,currentUser);
		}else{
			return this.savingBacCFL.editspaj(cmd,currentUser);
		}
	}
	
	public List<PemegangAndRekeningInfo> selectPemegangAndRekeningInfo(String regSpaj){
		return bacDao.selectPemegangAndRekeningInfo(regSpaj);
	}
	
	public List<Map<String, Object>> select_produkutama_online() {
		return this.bacDao.select_produkutama_online();
	}	
	
	
	public List<Map<String, Object>> select_tipeprodukonline()
	{
		return this.bacDao.select_tipeprodukonline();
	}
	
	//lufi (14/09/2012)khusus produk Bank Sinarmas Syariah Berbeda dengan Bank Sinarmas
	public List<Map<String, Object>> select_produkutama_bsim() {
		return this.bacDao.select_produkutama_bsim();
	}
	
	public List<Map<String, Object>> select_tipeprodukbsim()
	{
		return this.bacDao.select_tipeprodukbsim();
	}
	
	public List<Map> select_listPaket(String lsbs_id,String lsdbs_number){
		return this.bacDao.select_listPaket(lsbs_id,lsdbs_number);
	}
	
	public Map selectValidBank(String spaj) {
		return uwDao.selectValidBank(spaj);
	}
	
	public List<Map> selectTitle() {
		return uwDao.selectTitle();
	
	}
	
	public void updateMstAddressBiling(AddressBilling address, Pemegang pemegang, User currentUser)
	{
		Date tgl=commonDao.selectSysdate();
		uwDao.updateMst_address_billing(address);
		uwDao.insertLst_ulangan(address.getReg_spaj(), tgl, "EDIT BY SYSTEM", pemegang.getLssp_id(), currentUser.getLus_id(), "UBAH ALAMAT PENAGIHAN SPAJ "+ address.getReg_spaj());
		
	}
	
	public Map selectDataVirtualAccount(String reg_spaj){
		return this.bacDao.selectDataVirtualAccount(reg_spaj);
	}
	public List selectDaftar_kyc2(String spaj){
		return this.bacDao.selectDaftar_kyc2(spaj);
	}
	public List selectDaftar_kyc(String spaj) {
		return this.bacDao.selectDaftar_kyc(spaj);
	}
	
	public List<DropDown> selectPersenUP(){
		List<DropDown> persenUp = new ArrayList();
		persenUp.add(new DropDown("0", ""));
		persenUp.add(new DropDown("1", "50%"));
		persenUp.add(new DropDown("2", "60%"));
		persenUp.add(new DropDown("3", "70%"));
		persenUp.add(new DropDown("4", "80%"));
		persenUp.add(new DropDown("5", "90%"));
		persenUp.add(new DropDown("6", "100%"));
		return persenUp;
	}
	
	public List select_all_mst_peserta(String reg_spaj)
	{
		return this.bacDao.select_all_mst_peserta(reg_spaj);
	}
	
	public void updateUploadScan( String spaj, Integer flag ){
	    	this.uwDao.updateUploadScan( spaj,flag );
	 }
	public int flag_upload_scan(String spaj)
	{
		return this.uwDao.flagUpdateMste_Upload_Scan(spaj);
	}
	
	public void updateNamaPemegangTertanggung(String mcl_id, String mcl_first)
	{
		uwDao.updateMstClientNewNamaPemegangTertanggung(mcl_id, mcl_first);
	}
	
	public void updateBlanko(String spaj, String noBlangko)
	{
		uwDao.updateMstPolicyMspoNoBlangko(spaj, noBlangko);
	}
	
	
	public void updateUmurPDanTgl(String spaj, Integer umurP,String mcl_id, String tgl ,String tempat)
	{
		uwDao.updateMstPolicyUmurPemegang(spaj, umurP);
		uwDao.updateMstInsuredUmurTertanggung(spaj, umurP);
		uwDao.updateMstClientTgl(mcl_id, tgl, tempat);
	}
	
	public void updateUmurTDanTgl(String spaj, Integer umurT,String mcl_id, String tgl ,String tempat)
	{
		uwDao.updateMstInsuredUmurTertanggung(spaj, umurT);
		uwDao.updateMstClientTgl(mcl_id, tgl, tempat);
	}
	
	public List selectlstCabangForBancass() {
		return uwDao.selectlstCabangForBancass();
	}
	
	public String kodeProductUtama(String spaj) {
		return uwDao.kodeProductUtama(spaj);
	}
	public String noEndor(String lca_id) {
		return sequence.sequenceNo_Endorse2(lca_id);
	}
	
	public String noTest(String lca_id) {
		return sequence.sequenceNo_Endorse3(lca_id);
	}
	
	public String noTestSyariah(String lca_id) {
		return sequence.sequenceNo_Endorse4(lca_id);
	}
	
	/*public String noMall(String lca_id) {
		return sequence.sequenceNo_Endorse4(lca_id);
	}*/
	public AddressBilling selectAddressBilling(String spaj) {
		return (AddressBilling ) this.bacDao.selectAddressBilling(spaj);
	}
	/**
	 * fungsi untuk proses endors yang bersifat Non Material - Untuk User2 UW - Bancas
	 * @author ryan
	 * @param reg_spaj,lus_id,alasan,aplikasi,lsje_id,currentUser,kata1,kata2,kata3,no1
	 * @return no endors
	 */
	public String endorseNonMaterial (String reg_spaj,Integer lus_id, String alasan,String aplikasi,Integer lsje_id,User currentUser,
			String kata1,String kata2,String kata3,Integer no1){
				f_hit_umur umr= new f_hit_umur();
				Pemegang2 pmg=basDao.selectpemegangpolis(reg_spaj);
				Tertanggung ttg =selectttg(reg_spaj);
				Map mTertanggung=uwDao.selectTertanggung(reg_spaj);
				AddressBilling ab 	= selectAddressBilling(reg_spaj);
				Datausulan dataUsulan = bacDao.selectDataUsulanutama(reg_spaj);
				Date ldt_now = commonDao.selectSysdate();
				String noEndors =noEndor(pmg.getLca_id());
				String datalama=null;
				try{
				if(lsje_id.equals(2)){
					 datalama = pmg.getMcl_first();
					 updateNamaPemegangTertanggung(pmg.getMcl_id(), aplikasi);
				}else if(lsje_id.equals(1)){
					 datalama = ttg.getMcl_first();
					 updateNamaPemegangTertanggung(ttg.getMcl_id(), aplikasi);
				}else if (lsje_id.equals(11)){
					SimpleDateFormat dateFormatDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
					/*String tgl="17/02/2012";
					Date dt = dateFormatDDMMYYYY.parse(tgl);
					Date dt1 ="17/02/2012";*/
					String tgl1 = dateFormatDDMMYYYY.format(pmg.getMspe_date_birth());
					Date tgl2 = dateFormatDDMMYYYY.parse(kata1);
					//StringBuilder nowYYYYMMDD = new StringBuilder(dateFormatDDMMYYYY.format(pmg.getMspe_date_birth()));
					datalama = pmg.getMspe_place_birth()+", "+tgl1+"("+pmg.getMste_age()+" Tahun)";
					Integer tanggal1=new Integer(0);
					Integer bulan1=new Integer(0);
					Integer tahun1=new Integer(0);
					Integer tanggal3=new Integer(0);
					Integer bulan3=new Integer(0);
					Integer tahun3=new Integer(0);
					Integer li_umur_pp=new Integer(0);
					tanggal1= dataUsulan.getMste_beg_date().getDate();
					bulan1 = dataUsulan.getMste_beg_date().getMonth()+1;
					tahun1 = dataUsulan.getMste_beg_date().getYear()+1900;
					tanggal3=tgl2.getDate();
					bulan3=tgl2.getMonth()+1;
					tahun3=tgl2.getYear()+1900;
					li_umur_pp = umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
					updateUmurPDanTgl(reg_spaj, li_umur_pp,pmg.getMcl_id(), kata1,kata2);
					aplikasi = kata2+", "+kata1+" ("+li_umur_pp+ " Tahun)";
				}else if (lsje_id.equals(10)){
					SimpleDateFormat dateformatDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
					String tgl1 = dateformatDDMMYYYY.format((ttg.getMspe_date_birth()));
					//StringBuilder nowYYYYMMDD = new StringBuilder(dateformatDDMMYYYY.format(ttg.getMspe_date_birth()));
					Date tgl2 = dateformatDDMMYYYY.parse(kata1);
					datalama = ttg.getMspe_place_birth()+", "+tgl1+"("+ttg.getMste_age()+" Tahun)";
					Integer tanggal1=new Integer(0);
					Integer bulan1=new Integer(0);
					Integer tahun1=new Integer(0);
					Integer tanggal3=new Integer(0);
					Integer bulan3=new Integer(0);
					Integer tahun3=new Integer(0);
					Integer li_umur_tt=new Integer(0);
					tanggal1= dataUsulan.getMste_beg_date().getDate();
					bulan1 = dataUsulan.getMste_beg_date().getMonth()+1;
					tahun1 = dataUsulan.getMste_beg_date().getYear()+1900;
					tanggal3=tgl2.getDate();
					bulan3=tgl2.getMonth()+1;
					tahun3=tgl2.getYear()+1900;
					li_umur_tt = umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
					updateUmurTDanTgl(reg_spaj,li_umur_tt,ttg.getMcl_id(),kata1,kata2);
					aplikasi = kata2+", "+kata1+" ("+li_umur_tt+ " Tahun)";
				}else if (lsje_id.equals(60)){
					datalama=pmg.getMspo_no_blanko();
					updateBlanko(reg_spaj, aplikasi);
				}else if (lsje_id.equals(3)){
					datalama= pmg.getAlamat_rumah()+" , "+pmg.getKota_rumah()+" " + pmg.getKd_pos_rumah();
					prosesEditAlamat(reg_spaj, currentUser, kata1, kata2, kata3, 1);
				}else if (lsje_id.equals(24)){
					datalama= ttg.getAlamat_rumah()+" , "+ttg.getKota_rumah()+" " + ttg.getKd_pos_rumah();
					prosesEditAlamat(reg_spaj, currentUser, kata1, kata2, kata3, 2);
				}else if (lsje_id.equals(4)){
					datalama= ab.getMsap_address()+" , "+ab.getKota()+" " + ab.getMsap_zip_code();
					updateAddressBillingEndors(kata1,kata2,kata3,reg_spaj);
				}
				//String aplikasi = pmg.getMspe_place_birth()+"/"+pmg.getMspe_date_birth();
				Endors endors=new Endors();
				endors.setMsen_endors_no(noEndors);
				endors.setReg_spaj(reg_spaj);
				endors.setMsen_internal(0);
				endors.setMsen_alasan(alasan);
				endors.setMsen_input_date(commonDao.selectSysdate());
				endors.setMsen_surat("INSERT OTOMATIS");
				endors.setLus_id(lus_id);
				endors.setMsen_input_date(commonDao.selectSysdate());
				bacDao.insertMstEndors(endors);
				
				DetEndors detEndors=new DetEndors();
				detEndors.setMsen_endors_no(endors.getMsen_endors_no());
				detEndors.setMsenf_number(1);
				detEndors.setLsje_id(lsje_id);
				detEndors.setMste_insured_no(1);
				detEndors.setMsde_old1(datalama);
				detEndors.setMsde_new1(aplikasi);
				bacDao.insertMstDetEndors(detEndors);
				insertLst_ulangan(reg_spaj, ldt_now, "EDIT BY SYSTEM", pmg.getLssp_id(),currentUser.getLus_id(), alasan);
				
				return noEndors;
				
				}catch (Exception e){
					logger.error("ERROR :", e);
					
				return null;
				}
			}	
	
	public Tertanggung selectttg(String spaj) {
		return (Tertanggung) this.bacDao.selectttg(spaj);
	}
	/**
	 * fungsi untuk edit alamat (Pemegang dan Tertanggung Untuk Proses Endors)
	 * @author ryan
	 * @return 
	 */
	public void prosesEditAlamat(String spaj,User currentUser,String alamat,String kota,String kdpos,Integer kode){
		Pemegang2 pmg=basDao.selectpemegangpolis(spaj);
		Tertanggung ttg =selectttg(spaj);
		Date tgl=commonDao.selectSysdate();
		if(kode ==1){
		AddressNew addNew=new AddressNew();
		addNew.setAlamat_kantor(pmg.getAlamat_kantor());
		addNew.setAlamat_rumah(alamat);
		addNew.setArea_code_fax(pmg.getArea_code_fax());
		addNew.setArea_code_kantor(pmg.getArea_code_kantor());
		addNew.setArea_code_kantor2(pmg.getArea_code_kantor2());
		addNew.setArea_code_rumah(pmg.getArea_code_rumah());
		addNew.setArea_code_rumah2(pmg.getArea_code_rumah2());
		addNew.setEmail(pmg.getEmail());
		addNew.setKd_pos_kantor(pmg.getKd_pos_kantor());
		addNew.setKd_pos_rumah(kdpos);
		addNew.setKota_kantor(pmg.getKota_kantor());
		addNew.setKota_rumah(kota);
		addNew.setMcl_id(pmg.getMcl_id());
		addNew.setNo_fax(pmg.getNo_fax());
		addNew.setNo_hp(pmg.getNo_hp());
		addNew.setNo_hp2(pmg.getNo_hp2());
		addNew.setTelpon_kantor(pmg.getTelpon_kantor());
		addNew.setTelpon_kantor2(pmg.getTelpon_kantor2());
		addNew.setTelpon_rumah(pmg.getTelpon_rumah());
		addNew.setTelpon_rumah2(pmg.getTelpon_rumah2());
		uwDao.updateMstAddressNew(addNew);
		//update alamat pada simas card
		Simcard simcard=new Simcard();
		simcard.setMcl_id(pmg.getMcl_id());
		simcard.setAlamat(alamat);
		simcard.setKota(kota);
		simcard.setKode_pos(kdpos);
		uwDao.updateMstSimcardAlamat(simcard);
		}
		else{
			AddressNew addNewT=new AddressNew();
			addNewT.setAlamat_kantor(ttg.getAlamat_kantor());
			addNewT.setAlamat_rumah(alamat);
			addNewT.setArea_code_fax(ttg.getArea_code_fax());
			addNewT.setArea_code_kantor(ttg.getArea_code_kantor());
			addNewT.setArea_code_kantor2(ttg.getArea_code_kantor2());
			addNewT.setArea_code_rumah(ttg.getArea_code_rumah());
			addNewT.setArea_code_rumah2(ttg.getArea_code_rumah2());
			addNewT.setEmail(ttg.getEmail());
			addNewT.setKd_pos_kantor(ttg.getKd_pos_kantor());
			addNewT.setKd_pos_rumah(kdpos);
			addNewT.setKota_kantor(ttg.getKota_kantor());
			addNewT.setKota_rumah(kota);
			addNewT.setMcl_id(ttg.getMcl_id());
			addNewT.setNo_fax(ttg.getNo_fax());
			addNewT.setNo_hp(ttg.getNo_hp());
			addNewT.setNo_hp2(ttg.getNo_hp2());
			addNewT.setTelpon_kantor(ttg.getTelpon_kantor());
			addNewT.setTelpon_kantor2(ttg.getTelpon_kantor2());
			addNewT.setTelpon_rumah(ttg.getTelpon_rumah());
			addNewT.setTelpon_rumah2(ttg.getTelpon_rumah2());
			uwDao.updateMstAddressNew(addNewT);
			//update alamat pada simas card
			Simcard simcard2=new Simcard();
			simcard2.setMcl_id(ttg.getMcl_id());
			simcard2.setAlamat(alamat);
			simcard2.setKota(kota);
			simcard2.setKode_pos(kdpos);
			uwDao.updateMstSimcardAlamat(simcard2);
		}
		
    }
	
	//questionare DMTM
	public String insertMedQuestDMTM(MedQuest medQuest,MedQuest_tertanggung medQuest_tertanggung,MedQuest_tambah medQuest_tambah,MedQuest_tambah2 medQuest_tambah2,MedQuest_tambah3 medQuest_tambah3,MedQuest_tambah4 medQuest_tambah4,MedQuest_tambah5 medQuest_tambah5,Integer relasi) {
    	return uwDao.insertMedQuestDMTM(medQuest,medQuest_tertanggung,medQuest_tambah,medQuest_tambah2,medQuest_tambah3,medQuest_tambah4,medQuest_tambah5,relasi);
    }

	//for report claim-
	public List selectreportClaimBasedAgeDeath(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectreportClaimBasedAgeDeath(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List selectreportClaimBasedPolicyDuration(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectreportClaimBasedPolicyDuration(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List selectreportDetailClaim(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectreportDetailClaim(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List selectreportCODByBranch(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectreportCODByBranch(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List selectreportClaimByCOD(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectreportClaimByCOD(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public Integer selectreportClaimByCOD_TotalCase(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return (Integer) this.basDao.selectreportClaimByCOD_TotalCase(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List selectreportClaimByMedis(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectreportClaimByMedis(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List selectGetTotalClaimByMedis(String bdate, String edate, String lus_id, String polis, String medis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectGetTotalClaimByMedis(bdate, edate, lus_id, polis, medis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List selectreportClaimBasedEntryAge(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectreportClaimBasedEntryAge(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List selectreportExGratiaClaim(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectreportExGratiaClaim(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List selectreportClaimByProduct(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectreportClaimByProduct(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List selectreportClaimBySA(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectreportClaimBySA(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List selectreportClaimByPaid(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) {
		return this.basDao.selectreportClaimByPaid(bdate, edate, lus_id, polis, grup_bank, nama_bank, jenis_periode);
	}
	
	public List<Map> selectPolicyRelationDMTM(String spaj) {
		return basDao.selectPolicyRelationDMTM(spaj);
	}
	/**
	 * fungsi untuk edit alamat billing (Pemegang dan Tertanggung Untuk Proses Endors)
	 * @author ryan
	 * @return 
	 */
	public void updateAddressBillingEndors(String address, String kota,String kdpos, String spaj){
		uwDao.updateAddressBillingEndors(address, kota,kdpos,spaj);
	}
	
	public Integer selectProsesUploadScan (String reg_spaj, Upload upload, File destDir, List<Scan> daftarScan,StringBuffer filenames,User currentUser){
		try{
            DateFormat df = new SimpleDateFormat("yyyy");
			
			Date nowDate = commonDao.selectSysdate();
			String year =  df.format(nowDate);
			String lde_id=currentUser.getLde_id();
			String lca_id=reg_spaj.substring(0, 2);
			String mi_id ="";
			String from = props .getProperty("admin.ajsjava");
//			String sekuens = this.uwDao.selectGetCounter(119, "01");
//			String maxMiId = FormatString.rpad("0", sekuens, 6);
			
			for(int i=0; i<upload.getDaftarFile().size(); i++) {
				MultipartFile file = (MultipartFile) upload.getDaftarFile().get(i);
				Integer mi_flag = 0;
				if(file.isEmpty()==false){
					mi_flag = 1;
					File fileDir = new File(destDir.getPath());
					//String fileName = reg_spaj + daftarScan.get(i).getNmfile() + " " + "WWW.pdf";
					String fileName = reg_spaj + daftarScan.get(i).getNmfile() + " " + FormatString.getFileNo( destDir.list(), daftarScan.get(i).getNmfile())+".pdf";
					String dest = destDir.getPath() +"/"+ fileName; //file.getOriginalFilename();
					String destSPT = destDir.getPath() +"//";
					File outputFile = new File(dest);
				    FileCopyUtils.copy(file.getBytes(), outputFile);
				    
				    if(!filenames.toString().equals("")){
				    	filenames.append(", ");
				    }
				    filenames.append(daftarScan.get(i).getNmfile());
				    
//				    Scan scan = new Scan(Integer.parseInt(sequence.sequenceKdScan()), currentUser.getLus_id(), currentUser.getLde_id(), reg_spaj, null, destDir.getPath()+"//"+fileName, daftarScan.get(i).getNmfile(), 0, 1, 0);
				    Scan scan = new Scan();
				   
				    scan.setLus_id(currentUser.getLus_id());
				    scan.setLde_id(currentUser.getLde_id());
				    scan.setNo_indek(reg_spaj);
				    scan.setTgl_input(nowDate);
				    scan.setFiles_ad(destDir.getPath()+"\\"+fileName);
				    scan.setTipe_file(daftarScan.get(i).getNmfile());
				    scan.setJml_page(0);
				    scan.setFlag_aktif(1);
				    scan.setJenis(2);//0 : program upload seno, 1 program SNOWS rudy, 2 program E-Lions deddy, 3 program E-Lion Pengajuan Biaya Ridhaal
				    boolean a=true;
				    while(a) {
				    	try{
					   		scan.setKd_scan(Integer.parseInt(sequence.sequenceKdScan()) );
						    uwDao.insertMstScan(scan);
						    a=false;
					    }catch (Exception e) {
							// TODO: handle exception
						}
				   	}
				    String fileName2 = reg_spaj + daftarScan.get(i).getNmfile() ; //" " + FormatString.getFileNo(new String[] {destDir.getPath()}, daftarScan.get(i).getNmfile());
//				    updateUploadScan(reg_spaj, 1);
//					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Upload Scan NB (" + filenames.toString() + ")", reg_spaj, 0);

				    //Deddy (10 Sep 2012) - Jika upload scan SPH, maka update otomatis mspo_flag_sph jadi 1.
				    if(daftarScan.get(i).getNmfile().equals("SPH")){
				    	bacDao.updateFlagSPH(reg_spaj, 1);
				    }
				    
				    //Deddy (25 Jan 2013) - Req Ariani : Apabila ada upload TTP dari user selain U/W, dikirimkan email ke U/W sebagai pemberitahuan bahwa TTP sudah diupload dan bisa langsung dijalankan proses Filling.
				    if(scan.getTipe_file().equals("TTP") && !currentUser.getLde_id().equals("11")){
				    	String distribusi = "Regional";
				    	if(lca_id.equals("09")){
				    		distribusi = "Bancass";
				    	}else if("37,52".indexOf(lca_id)>=0){
				    		distribusi = "Agency";
				    	}else if(lca_id.equals("40")){
				    		distribusi = "DMTM";
				    	}else if(lca_id.equals("42")){
				    		distribusi = "Worksite";
				    	}else if(lca_id.equals("62")){
				    		distribusi = "MNC";
				    	}else if(lca_id.equals("65")){
				    		distribusi = "FCD";
				    	}
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
								null, 0, 0, nowDate, null, 
								true, "ajsjava@sinarmasmsiglife.co.id", 
								new String[]{"timmy@sinarmasmsiglife.co.id;Maret@sinarmasmsiglife.co.id;rizky_a@sinarmasmsiglife.co.id;endri@sinarmasmsiglife.co.id;uwprinting@sinarmasmsiglife.co.id"}, 
//								new String[]{"deddy@sinarmasmsiglife.co.id"}, 
								null,
								null, 
								"ADA FILE SCAN "+fileName2+" Jalur Distribusi :"+distribusi,
								"Telah dilakukan Upload Scan TTP untuk SPAJ No."+reg_spaj+", Silakan dilakukan proses input TTP", 
								null, reg_spaj);
					}
				    
					/*
					 * Proses Seleksi tipe file untuk SPT
					 * Pada proses ini dilakukan convert pdf ke dalam bentuk gambar setelah itu, gambar tersebut dikirim melalui E-Mail ke Atasan 
					 * 			   
					*/				    
				    if(scan.getTipe_file().equals("SPT")){
				        
				        if(lde_id.equals("11")){
				        
				    	String ada = uwDao.selectSptUpload(reg_spaj);
				    	if (ada==null){
				    		ada="";
				    	}
                         
				    	if(ada.equals("Validasi SPT")){	//proses upload khusus SPT
				    		Integer statusProses = 0;
				    		 try{
				    			 statusProses  = kirimEmailPermohonanSPT(reg_spaj,currentUser,fileName,destSPT,fileName2,new Integer(1),"");//flag 1 SPT yg diproses oleh UW 
				    			 uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), "SPT - WAITING APPROVAL", reg_spaj, 1,"WAITING APPROVAL SPT");
				    			 if(statusProses == 1){
				    				 String fileSPTName = "SPT_"+reg_spaj.trim()+"_001.pdf";
				    				 dest = destDir.getPath() +"/"+ fileSPTName;
				    				 outputFile = new File(dest);
				 				     FileCopyUtils.copy(file.getBytes(), outputFile);
				    				 updateUploadScan(reg_spaj, 1);
									 uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Upload Scan NB (" + filenames.toString() + ")", reg_spaj, 0); 
				    				 return 3;
				    			 }else{
				    				 return 0;
				    			 }
				    		 }catch (Exception e) {
								TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
								return statusProses;
							 }					     					
							 			   			     
				       }else{
				    	   	updateUploadScan(reg_spaj, 1);
							uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Upload Scan NB (" + filenames.toString() + ")", reg_spaj, 0); 
							return 2;
				       }
				    	
				      //end of if cek user dari uw atau bukan
				      }else{
				    	  updateUploadScan(reg_spaj, 1);
						  uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Upload Scan NB (" + filenames.toString() + ")", reg_spaj, 0);
						  return 1; 
				      }
				    }
				}
			}
			
			updateUploadScan(reg_spaj, 1);
			uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Upload Scan NB (" + filenames.toString() + ")", reg_spaj, 0);
			//String to="Ariani;novie;hanifah;shopiah;Fouresta";
//			if(!lca_id.equals("09") && !lca_id.equals("58")){
//				String to="shopiah";
//				String[] emailTo=to.split(";");
//				String distribution="";
//				for(int y=0; y<emailTo.length; y++){
//					emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
//				}
//				if (lca_id.equals("37")||lca_id.equals("52") ){
//					distribution="Agency";
//				}else if(lca_id.equals("42")){
//					distribution="Worksite";
//				}else if(!lca_id.equals("09") && !lca_id.equals("58")){
//					distribution="Regional";
//				}
//			    String me_id = sequence.sequenceMeIdEmail();
//				//email.send(true, from, emailTo, null, null, "Ada File Scan SPAJ"+reg_spaj+" "+"Jalur Distribusi:"+" "+distribution+" "+"Yang Di Upload", "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", null);
//				EmailPool.send(me_id,"SMiLe E-Lions Info", 1, 1, 0, 0, 
//						null, 0, 0, nowDate, null, 
//						false, from,												
//						emailTo, 
//						null, 
//						null, 
//						"Ada File Scan SPAJ"+reg_spaj+" "+"Jalur Distribusi:"+" "+distribution+" "+"Yang Di Upload", 
//						"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//						null);
//			}
			return 1;
		}catch (Exception e){
			logger.error("ERROR :", e);
			File outputFile = new File(destDir.getPath());
			outputFile.delete();
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
	}

	//Method untuk update serta pemberitahuan ke uw tentang akseptasi SPT by Email oleh direksi  *lufi
	public HashMap aksepSPT(String spaj, User currentUser, String pp, String status, String email,String flag ){
		String namaUserAksep ="";
		HashMap map = new HashMap();
		HashMap mapCekUser = new HashMap();
		ArrayList <Map> user=  new ArrayList<Map>();
				
		if(flag.equals("1")){
			user = Common.serializableList( uwDao.selectMstPositionSpajCekAksep(spaj,flag));	
			if(user.isEmpty()){
				if(status.equals("1")){
				   uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), "SPT TELAH DISETUJUI",spaj, 1,"SPT HAS APPROVED");
				   map.put("status", status);
				}else{
				   uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), "SPT TIDAK DISETUJUI", spaj, 1,"SPT HASNT APPROVED");
				   map.put("status", status);
				}
				 
				 map.put("nama", namaUserAksep);
			}else{
				 mapCekUser=(HashMap)user.get(0);
				 namaUserAksep=(String)mapCekUser.get("NAMA");
			     map.put("status", "3");
			     map.put("nama", namaUserAksep);
			}
			
		}else{
				user = Common.serializableList( uwDao.selectMstPositionSpajCekAksep(spaj,flag));
				if(user.isEmpty()){
						String from = props .getProperty("admin.ajsjava");
						String emailto = email;
						String []cc=new String[]{};
						cc=new String[] { "MieYoen@sinarmasmsiglife.co.id","Ety@sinarmasmsiglife.co.id","achmad_r@sinarmasmsiglife.co.id","yayuk@sinarmasmsiglife.co.id","jajat@sinarmasmsiglife.co.id",
						 		   "kristina@sinarmasmsiglife.co.id","bunga@sinarmasmsiglife.co.id","eny@sinarmasmsiglife.co.id","maria_i@sinarmasmsiglife.co.id","rosdiana@sinarmasmsiglife.co.id","noor@sinarmasmsiglife.co.id",
						 		   "Kuncoro@sinarmasmsiglife.co.id","harnisya@sinarmasmsiglife.co.id","idris@sinarmasmsiglife.co.id","mario@sinarmasmsiglife.co.id"};
						String subjectTolak="[URGENT!]PENOLAKAN SPT UNTUK SPAJ NO:"+ FormatString.nomorSPAJ(spaj)+" "+"Pemegang Polis: "+pp;
						String subjectTerima="[URGENT!]PERSETUJUAN SPT UNTUK SPAJ NO:"+ FormatString.nomorSPAJ(spaj)+" "+"Pemegang Polis: "+pp;
						String subjek = "";
						String pesanAksep = "";
						List <Map> dataPolis=selectDataPemegangPolis(spaj);
						HashMap mapDataPolis=(HashMap)dataPolis.get(0);
						String namaPemegang=(String)mapDataPolis.get("PEMEGANG");
						String namaTertanggung=(String)mapDataPolis.get("TERTANGGUNG");
						String prod=(String)mapDataPolis.get("LSDBS_NAME");
						BigDecimal up1 = (BigDecimal) mapDataPolis.get("UP");
				        String up = FormatString.formatCurrency("", up1);
						BigDecimal pr=(BigDecimal)mapDataPolis.get("PREMI_RUPIAH");
						BigDecimal pd=(BigDecimal)mapDataPolis.get("PREMI_DOLLAR");	
						String premi_rupiah=null;
						String premi_dollar=null;
						String mgi=(String)mapDataPolis.get("MGI");
						String no_rek=(String)mapDataPolis.get("NO_REK");
						if (pr!=null){							
							premi_rupiah=FormatString.formatCurrency(" ", pr);
						}
						if (pd!=null){
							premi_dollar=FormatString.formatCurrency(" ", pd);
						}
						if(mgi==null){
							mgi="-";
						}
						if(no_rek==null){
							no_rek="-";
						}
						
						String tanggal=(String)mapDataPolis.get("TANGGAL_MULAI");
						if(tanggal==null)tanggal="-";
						String mrc_nama=(String)mapDataPolis.get("MRC_NAMA");
						if(mrc_nama==null)mrc_nama="-";
						String cabang=(String)mapDataPolis.get("CABANG");
						if(cabang==null)cabang="-";
						String bayar=(String)mapDataPolis.get("CARA_BAYAR");
						if(bayar==null)bayar="-";	
						String currency=null;
						
						if(status.equals("1")){
							subjek=subjectTerima;
							pesanAksep = "DISETUJUI";
						    uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), "SPT DISETUJUI CSFL",spaj, 1,"SPT HAS APPROVED");
							map.put("status", status);
							
						}else{
							subjek=subjectTolak;
							pesanAksep = "DITOLAK";
							uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), "SPT DITOLAK CSFL",spaj, 1,"SPT HASNT APPROVED");
							map.put("status", status);
						}
						 map.put("nama", namaUserAksep);
						try {				
							if(!emailto.trim().equals("")){										
																			
									String[] to = emailto.split(";");
									StringBuffer pesan = new StringBuffer();
									pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: yellow;} table td{border: 1px solid black;}</style></head>");
									pesan.append("<body>Email ini terkirim otomatis oleh System E-Lions, mohon jangan lakukan Reply/Forward terhadap email yang dikirim ini.<br><br>");
									pesan.append("SPT untuk no SPAJ "+FormatString.nomorSPAJ(spaj)+"&nbsp"+",Nama Pemegang Polis:&nbsp"+pp+"&nbsp,telah <b>"+pesanAksep+"</b>.<br><br>");
									pesan.append("Berikut data dari pemegang polis tersebut :<br><br>");
									pesan.append("<ul><li>Nama Pemegang Polis\t\t:\t"+namaPemegang+"</li>");
									pesan.append("<li>Nama Tertanggung\t\t:\t"+namaTertanggung+"</li>");
									pesan.append("<li>Produk\t\t\t:\t"+prod+"</li>");
				
									if (premi_rupiah!=null){
										currency="Rp";
										pesan.append("<li>Uang Pertanggungan\t\t:\t"+currency+" "+up+"</li>");
										pesan.append("<li>Premi/Cara Bayar\t\t:\t"+currency+" "+premi_rupiah+"/"+bayar+"</li>");
									}
									if (premi_dollar!=null){
										currency="$";
										pesan.append("<li>Uang Pertanggungan\t\t:\t"+currency+" "+up+"</li>");
										pesan.append("<li>Premi\t\t\t:\t"+currency+" "+premi_dollar+"/"+bayar+"</li>");
		
									}
									pesan.append("<li>MGI\t\t\t:\t"+mgi+"</li>");
									pesan.append("<li>Mulai Asuransi\t\t:\t"+tanggal+"</li>");
									pesan.append("<li>No.Rek Pembayaran Manfaat\t:\t"+no_rek+" "+"a/n"+" "+mrc_nama+"</li>");
									pesan.append("<li>Cabang\t\t\t:\t"+cabang+"</li></ul>");
		
									pesan.append("Untuk melihat Info lebih detail silahkan lihat record di UW Info<br><br>");
									pesan.append( "<br>" + "</body></html>");		    		
									
									
									String me_id = sequence.sequenceMeIdEmail();
										
									EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
												null, 0, 0, commonDao.selectSysdate(), null, 
												true, from,												
												to, 
												cc, 
												null, 
												subjek, 
												pesan.toString(), 
												null,8);			
			
							}
					
					} catch (MailException e) {
						logger.error("ERROR :", e);
					} 
					
					
				}else{
					mapCekUser=(HashMap)user.get(0);
					namaUserAksep=(String)mapCekUser.get("NAMA");
				    map.put("status", "3");
				    map.put("nama", namaUserAksep);
				}
		
		
		
		}				
		return map;
	}
	
	public Integer rekklaim(User currentUser,String reg_spaj,String dir, Integer flag_aksep, String inbox) throws Exception{
		Date nowDate = commonDao.selectSysdate();
		Integer months = nowDate.getMonth()+1;
		Integer years = nowDate.getYear()+1900;
		String dest=dir+"//"+reg_spaj.trim()+"//";
	    String fileName=reg_spaj.trim()+"REKKLAIM"+" 001.pdf";
	    String from="ajsjava@sinarmasmsiglife.co.id";
	    try {
	    	List <Map> dataPolis=selectDataPemegangPolis(reg_spaj);
			HashMap mapDataPolis=(HashMap)dataPolis.get(0);
			String no_polis = (String) mapDataPolis.get("MSPO_POLICY_NO_FORMAT");
			String namaPemegang=(String)mapDataPolis.get("PEMEGANG");
			String namaTertanggung=(String)mapDataPolis.get("TERTANGGUNG");
			String prod=(String)mapDataPolis.get("LSDBS_NAME");
			BigDecimal lsbs_id = (BigDecimal) mapDataPolis.get("LSBS_ID");
			BigDecimal lsdbs_number = (BigDecimal) mapDataPolis.get("LSDBS_NUMBER");
			BigDecimal up1 = (BigDecimal) mapDataPolis.get("UP");
            String up = FormatString.formatCurrency("", up1);
            String tanggal_mulai=(String)mapDataPolis.get("TANGGAL_MULAI");
//            ambil data dari table mst_claim/mst_det_claim/mst_bayar_claim
//            tgl meninggal : mdc_tgl_mati
//            sebab meninggal : mdc_sebab
//            nilai tahapan : mscl_bonus_tahap + claim_nilaio
//			  no_rek_tahapan : mbc_no_account
//			  bank : mbc_nm_bank
//			  cabang bank : mbc_cab_bank
            
//	    	Double jumlah_bayar_claim = basDao.selectTotalBayarKlaim(reg_spaj);
            List listClaim = basDao.selectMstClaim(reg_spaj);
            Map mapClaim = (Map) listClaim.get(0);
            BigDecimal jumlah_bayar_claim = (BigDecimal) mapClaim.get("MSCL_UP");
            Date tgl_meninggal = (Date)  mapClaim.get("MDC_TGL_MATI");
            String sebab_meninggal = (String) mapClaim.get("MDC_SEBAB");
            BigDecimal nilai_bonus_tahapan = (BigDecimal) mapClaim.get("MSCL_BONUS_TAHAP");
            BigDecimal nilai_claim = (BigDecimal) mapClaim.get("CLAIM_NILAIO");
            BigDecimal nilai_tahapan = nilai_bonus_tahapan.add(nilai_claim);
            String no_rek_tahapan = (String) mapClaim.get("MBC_NO_ACCOUNT");
            String bank = (String) mapClaim.get("MBC_NM_BANK");
            String cabang_bank = (String) mapClaim.get("MBC_CAB_BANK");
            
            
//	    	- < 100jt ke dr sisti only
//	    	- 100-300 ke dr sisti, bu mar
//	    	- > 300jt ke dr sisti, bu mar, pak hamid
            
	    	String emailto ="deddy@sinarmasmsiglife.co.id;";
	    	String []cc =null;
	    	String []bcc=null;
	    	if(flag_aksep==0){
//	    		if(jumlah_bayar_claim.doubleValue() < new Double(100000000)){
//		    		emailto ="Sunarti@sinarmasmsiglife.co.id;nilamsari@sinarmasmsiglife.co.id;nintia@sinarmasmsiglife.co.id;Sisti@sinarmasmsiglife.co.id;";		
//		    	}else if(jumlah_bayar_claim.doubleValue() >= new Double(100000000) && jumlah_bayar_claim.doubleValue() <= new Double(300000000)){
//		    		emailto ="Sunarti@sinarmasmsiglife.co.id;nilamsari@sinarmasmsiglife.co.id;nintia@sinarmasmsiglife.co.id;Sisti@sinarmasmsiglife.co.id;marliana@sinarmasmsiglife.co.id;";
//		    	}else if(jumlah_bayar_claim.doubleValue() > new Double(300000000)){
//		    		emailto ="Sunarti@sinarmasmsiglife.co.id;nilamsari@sinarmasmsiglife.co.id;nintia@sinarmasmsiglife.co.id;Sisti@sinarmasmsiglife.co.id;marliana@sinarmasmsiglife.co.id;hamid@sinarmasmsiglife.co.id;";
//		    	}
	    		//Deddy (1 Sept 2014) : Req Avnel From Helpdesk No 55643, email rekomendasi klaim hanya berdasarkan jumlah bayar klaim >=3M, dan dikirimkan emailnya ke Kui San.
	    		//Deddy (7 Okt 2014) : Req Komeng From Helpdesk No 56764, email rekomendasi klaim berdasarkan jumlah bayar klaim >=3M, dikirimkanjuga emailnya ke nyoman_ayu (Biro Direksi).
	    		if(jumlah_bayar_claim.doubleValue() >= new Double("3000000000")){
	    			emailto ="hidenori.kui@sinarmasmsiglife.co.id;nyoman_ayu@sinarmasmsiglife.co.id;";
	    		}
	    		
            }else{
            	emailto=currentUser.getEmail();//untuk proses aksep/tolak, kirim email to ke user yg aksep/tolak.
//            	cc =new String[]{"Sunarti@sinarmasmsiglife.co.id;nilamsari@sinarmasmsiglife.co.id;nintia@sinarmasmsiglife.co.id;eri@sinarmasmsiglife.co.id;Evi_R@sinarmasmsiglife.co.id;taufik_r@sinarmasmsiglife.co.id;Helena@sinarmasmsiglife.co.id;"};
            	//Deddy (1 Sept 2014) : Req Avnel From Helpdesk No 55643, untuk email setelah aksep/tolak di arahkan berdasarkan plan produk stabil link/ powersave dengan cc masing2 PIC terkait seperti di bawah.
            	if(products.stableLink(lsbs_id.toString())){
            		cc = new String[]{"Kamarudinsyah@sinarmasmsiglife.co.id;Ety@sinarmasmsiglife.co.id;"};
            	}else{
            		cc =new String[]{"eri@sinarmasmsiglife.co.id;Evi_R@sinarmasmsiglife.co.id;taufik_r@sinarmasmsiglife.co.id;Helena@sinarmasmsiglife.co.id;"};
            	}
            	
            	bcc = new String[]{"Deddy@sinarmasmsiglife.co.id;"};
//            	cc = new String[]{"deddy@sinarmasmsiglife.co.id;"};
            }
	    	
	    	
	    	String subject= "[URGENT!] PERMOHONAN APPROVAL REKOMENDASI KLAIM untuk SPAJ NO: "+FormatString.nomorSPAJ(reg_spaj);
	    	if(flag_aksep ==1){
	    		subject= "APPROVE REKOMENDASI KLAIM oleh "+currentUser.getName()+" untuk SPAJ NO: "+FormatString.nomorSPAJ(reg_spaj);
	    	}else if(flag_aksep == 2){
	    		subject= "DECLINE REKOMENDASI KLAIM oleh "+currentUser.getName()+" untuk SPAJ NO: "+FormatString.nomorSPAJ(reg_spaj);
	    	}
	    	if(!emailto.trim().equals("")){
		   		String[] to = emailto.split(";");						   		
		   		List<DropDown> ListImage = new ArrayList<DropDown>();								
				String name=null;
				String lus_akseptor=null;
				
				if(flag_aksep ==0){
					uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), "REKKLAIM SENT", reg_spaj, 1,"WAITING APPROVAL REKKLAIM");
		    	}else if(flag_aksep ==1){
		    		uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), "REKKLAIM PROCESS ACCEPT", reg_spaj, 1,"APPROVAL REKKLAIM");
		    		MstInbox mstInbox = new MstInbox(inbox, null, null, null, null, null, null, reg_spaj, null, null, null, null, null, null, null, null, null, 2, Integer.parseInt(currentUser.getLus_id()), 1, null, null, null, null, null, null, null,null, 0,null);
		    		uwDao.updateMstInbox(mstInbox);
		    	}else if(flag_aksep ==2){
		    		uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), "REKKLAIM PROCESS DECLINE", reg_spaj, 1,"DECLINE REKKLAIM");
		    		MstInbox mstInbox = new MstInbox(inbox, null, null, null, null, null, null, reg_spaj, null, null, null, null, null, null, null, null, null, 3, Integer.parseInt(currentUser.getLus_id()), 1, null, null, null, null, null, null, null,null, 0,null);
		    		uwDao.updateMstInbox(mstInbox);
		    	}
				
				for(int j=0;j<to.length;j++){
					String me_id = sequence.sequenceMeIdEmail();
					String destTo=props.getProperty("embedded.mailpool.dir")+"\\" +years+"\\"+FormatString.rpad("0", months.toString(), 2)+"\\"+me_id+"\\";
					File destDir = new File(destTo);
					if(!destDir.exists()) destDir.mkdirs();
					String fileName2=reg_spaj+"REKKLAIM";
					String imageKlaim=fileName2+"1";
					PDFToImage.convertPdf2Image(dest,  destTo, fileName, fileName2,null,null);
					
					StringBuffer pesan2 = new StringBuffer();	
		   		    String [] sendingTo = new String[] {to[j]};	
		   			if(to[j].equals("Sisti@sinarmasmsiglife.co.id")){
			   			lus_akseptor="639";
			   			name="Sisti";
			   		}
			   		else if(to[j].equals("hafri@sinarmasmsiglife.co.id")){
			   			lus_akseptor="538";
			   			name="Hafriansyar";
			   		}		
			   		else if(to[j].equals("hamid@sinarmasmsiglife.co.id")){
			   			lus_akseptor="543";
			   			name="Hamid";
			   		}
			   		else if(to[j].equals("hidenori.kui@sinarmasmsiglife.co.id")){
			   			lus_akseptor="3963";
			   			name="Kui";
			   		}
			   		else if(to[j].equals("ayu_nyoman@sinarmasmsiglife.co.id")){
			   			lus_akseptor="4745";
			   			name="AYU_NYOMAN";
			   		}
			   		else if(to[j].equals("deddy@sinarmasmsiglife.co.id")){
			   			lus_akseptor="1255";
			   			name="Deddy";
			   		}
			   		else if(to[j].equals("berto@sinarmasmsiglife.co.id")){
			   			lus_akseptor="761";
			   			name="BraisBoy";
			   		}
		   			
//			    	SP Bayar untuk No. Polis : ……………….., Nama Tertanggung : ………………….. telah disetujui oleh Direksi
//			    	Berikut data dari Tertanggung tersebut :
//			    	•	Nama Pemegang Polis                                   :
//			    	•	Nama Tertanggung                                         :
//			    	•	Produk                                                                 :
//			    	•	Uang Pertanggungan                                     : 
//			    	•	Nilai Tunai/Tahapan/Nilai Polis                   :
//			    	•	Mulai Asuransi                                                  :
//			    	•	Tgl. dan penyebab meninggal                     :
//			    	•	No. rekening pembayaran Manfaat         :
//			    	•	Bank dan Cabang                                             :
		//
//			    	Untuk melihat info lebih detail silahkan lihat viewer, klaim kematian / klaim kesehatan
		   			pesan2.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: yellow;} table td{border: 1px solid black;}</style></head>");
	   		 		pesan2.append("<body bgcolor='#ffffc9'>Email ini terkirim otomatis oleh System E-Lions, mohon jangan lakukan Reply/Forward terhadap email yang dikirim ini.<br><br>");
	   		 		String pesan_aksep = " telah dilakukan rekomendasi";
		   		 	if(flag_aksep ==1){
		   		 		pesan_aksep = " telah disetujui";
			    	}else if(flag_aksep ==2){
			    		pesan_aksep = " tidak disetujui";
			    	}
	   		 		pesan2.append("SP Bayar untuk No. Polis : "+no_polis+", Nama Tertanggung : "+namaTertanggung+pesan_aksep+".<br><br>");
	   		 		pesan2.append("Berikut data dari Tertanggung tersebut :");
	   		 		pesan2.append("<ul><li>Nama Pemegang Polis\t\t\t:\t"+namaPemegang+"</li>");
	   		 		pesan2.append("<li>Nama Tertanggung\t\t\t:\t"+namaTertanggung+"</li>");
	   		 		pesan2.append("<li>Produk\t\t\t\t:\t"+prod+"</li>");
	   		 		pesan2.append("<li>Uang Pertanggungan\t\t\t:\t"+up+"</li>");
	   		 		pesan2.append("<li>Nilai Tunai/Tahapan/Nilai Polis\t\t:\t"+nilai_tahapan+"</li>");
	   		 		pesan2.append("<li>Mulai Asuransi\t\t\t:\t"+tanggal_mulai+"</li>");
		   		 	pesan2.append("<li>Tgl dan Penyebab meninggal\t\t:\t"+tgl_meninggal+", "+sebab_meninggal+"</li>");
			   		pesan2.append("<li>No. Rekening pembayaran Manfaat\t:\t"+no_rek_tahapan+"</li>");
			   		pesan2.append("<li>Bank dan Cabang\t\t\t:\t"+bank+(Common.isEmpty(cabang_bank)?"":", "+cabang_bank)+"</li></ul>");
			   		pesan2.append("<center><img border='0' src='cid:"+imageKlaim+".jpg'></center><br><br>");
			   		if(flag_aksep ==0){
			   			if("Sisti@sinarmasmsiglife.co.id,hafri@sinarmasmsiglife.co.id,hamid@sinarmasmsiglife.co.id,hidenori.kui@sinarmasmsiglife.co.id".indexOf(to[j])>-1){
			   				pesan2.append("Untuk proses selanjutnya, silahkan klik link dibawah:<br><br>");
				   			pesan2.append("<a href='http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=rekklaim&id="+lus_akseptor+"&spaj="+reg_spaj+"&inbox="+inbox+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"1"+"'><font size='10'>Setuju</font></a>\t");						   		 		
					   		pesan2.append("<a href='http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=rekklaim&id="+lus_akseptor+"&spaj="+reg_spaj+"&inbox="+inbox+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"2"+"'><font color='#fd020e' size='10'>Tidak Setuju</font></a><br><br>");
			   		 		pesan2.append("Jika anda mengalami kesulitan dengan link diatas, gunakan link dibawah:<br><br><br>");                 	            		
				   		 	pesan2.append("<a href='http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=rekklaim&id="+lus_akseptor+"&spaj="+reg_spaj+"&inbox="+inbox+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"1"+"'><font size='10'>Setuju</font></a>\t");						   		 		
					   		pesan2.append("<a href='http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=rekklaim&id="+lus_akseptor+"&spaj="+reg_spaj+"&inbox="+inbox+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"2"+"'><font color='#fd020e' size='10'>Tidak Setuju</font></a><br><br>");
//					   		pesan2.append("<a href='http://localhost:8080/E-Lions/uw/uw.htm?window=rekklaim&id="+lus_akseptor+"&spaj="+reg_spaj+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"1"+"&flag_aksep="+"1"+"'><font size='10'>Setuju</font></a>\t");						   		 		
//					   		pesan2.append("<a href='http://localhost:8080/E-Lions/uw/uw.htm?window=rekklaim&id="+lus_akseptor+"&spaj="+reg_spaj+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"2"+"&flag_aksep="+"2"+"'><font color='#fd020e' size='10'>Tidak Setuju</font></a><br><br>");
//			   		 		pesan2.append("Jika anda mengalami kesulitan dengan link diatas, gunakan link dibawah:<br><br><br>");                 	            		
//				   		 	pesan2.append("<a href='http://localhost:8080/E-Lions/uw/uw.htm?window=rekklaim&id="+lus_akseptor+"&spaj="+reg_spaj+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"1"+"&flag_aksep="+"1"+"'><font size='10'>Setuju</font></a>\t");						   		 		
//					   		pesan2.append("<a href='http://localhost:8080/E-Lions/uw/uw.htm?window=rekklaim&id="+lus_akseptor+"&spaj="+reg_spaj+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"2"+"&flag_aksep="+"2"+"'><font color='#fd020e' size='10'>Tidak Setuju</font></a><br><br>");
				   		}
			   		}
			   		pesan2.append("<br>Pengirim: " + currentUser.getName() + "<br>" + "</body></html>");
			   		EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, nowDate, null, 
							true, from, 
//							new String[]{"deddy@sinarmasmsiglife.co.id"}, 
//							new String[]{"deddy@sinarmasmsiglife.co.id"},  
							sendingTo, 
							cc, 
							bcc, 
							subject, 
							pesan2.toString(), 
							null,9);
				}
	    	}
	    }catch (MailException e) {
			logger.error("ERROR :", e);
		} catch (MessagingException e) {
			logger.error("ERROR :", e);
		}
	   
	    return flag_aksep;
	    
	    
	}
		
	//lufi Akseptasi SPT untuk LB req Deddy
	public Integer aksepsptlb(User currentUser,String reg_spaj,String dir) throws Exception{
		   String destSPT=dir+"//"+reg_spaj.trim()+"//";
		   String fileName="SPT_"+reg_spaj.trim()+"_001.pdf";
		   String fileName2=reg_spaj+"SPT";
		   String from="ajsjava@sinarmasmsiglife.co.id";
		   Integer statusProses = 1;
		   try{
			  statusProses = kirimEmailPermohonanSPT(reg_spaj, currentUser, fileName, destSPT, fileName2, new Integer(2),"");  
			  uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), "SPT DIKIRIM KE CSFL", reg_spaj, 1,"WAITING APPROVAL SPT");
		   }catch (Exception e) {
			 TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			 statusProses = 0;
		   }
		   
		  
		   return statusProses;
	}
	public Integer selectNonClearCaseAllMedQuest(String reg_spaj){
		return (Integer) uwDao.selectNonClearCaseAllMedQuest(reg_spaj);
	}
	
	public Integer selectRedFlag(String mcl_id){
		return uwDao.selectRedFlag(mcl_id);
	}
	
/*	public void insertmst_reff_bii2(String spaj, String level, String lcb_no , String lrb_id, String reff_id,String lcb_from_lrb, String lcb_penutup,
			String lus_id,String lcb_reff, String jn_nasabah) throws DataAccessException {
		this.bacDao.insertmst_reff_bii2(spaj,level,lcb_no,lrb_id, reff_id, lcb_from_lrb,lcb_penutup, lus_id,lcb_reff , jn_nasabah);
	}*/
	
	public String selectNamaRiderNya(Integer kd_rd, Integer nm_rd){
		return this.uwDao.selectNamaRiderNya(kd_rd, nm_rd);
	}
	
	public ReffBii ShintabiiSubmit(User currentUser, ReffBii datautama, String cabangKK){
		
		/*reff
		 * List<Map> cabangKK = new ArrayList<Map>();
				cabangKK = uwManager.selectCabangKK(datautama.getLcb_no2());
		 */
		
		//Yusuf (7 Aug 09) 
		//Request Joni BSM : MST_REFF_BII diisi LCB_NO dari USER nya, bukan LCB_NO dari REFERALnya
		//Sehingga hak untuk mengubah data tetap ada di usernya, yg berubah adalah report2nya
		String lcb_no = datautama.getLcb_no();
		//Deddy (16 Dec 2009)
		//Req by BSM(via Ko Yusuf)
		//Proses insert ke 
//		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if(!currentUser.getCab_bank().trim().equals("") && !currentUser.getCab_bank().trim().equals("SSS")){
			lcb_no = currentUser.getCab_bank();
		}
		
		//Agent BC - Di liat Dari Penutup aja untuk menentukan FIELD JN_NASABAH = 78.
		if(!Common.isEmpty(datautama.getLrbj_id1())){
			if(datautama.getLrbj_id1().equals("366")){
				datautama.setJn_nasabah("78");
			}
		}
		if(!cabangKK.equals("") && !cabangKK.equals(datautama.getLcb_no2())){
			String lcbNyaLrb = cabangKK;
			bacDao.deletemstreff_bii(datautama.getReg_spaj());
			bacDao.insertmst_reff_bii2(datautama.getReg_spaj(),
					"4", lcb_no, datautama.getLrb_id(), datautama.getReff_id(), lcbNyaLrb,datautama.getLcb_no_kk(),currentUser.getLus_id(),datautama.getLcb_no2(),datautama.getJn_nasabah());
		datautama.setStatussubmit("1");}
		else{
			Map referrer = bacDao.select_referrer(datautama.getLrb_id());			
			String lcbNyaLrb = (String) referrer.get("LCB_NO");
			bacDao.deletemstreff_bii(datautama.getReg_spaj());
			bacDao.insertmst_reff_bii2(datautama.getReg_spaj(),
					"4", lcb_no, datautama.getLrb_id(), datautama.getReff_id(), lcbNyaLrb,datautama.getLcb_no(), currentUser.getLus_id(),datautama.getLcb_no2(),datautama.getJn_nasabah());
			// logger.info("insert reff bii");
			datautama.setStatussubmit("1");
		}
		
		return datautama;
	}
	
	//for report UW Individu
	//Lufi 25/8/2014 report UW Individu sekarang digabung menjadi 1 kecuali Payroll(Req Ningrum helpdesk 55335)
//	public List selectreportUWIndividu_Agency(String bdate, String edate, String lus_id, String status, String produk) {
//		return this.uwDao.selectreportUWIndividu_Agency(bdate, edate, lus_id, status, produk);
//	}
//	
//	public List selectreportUWIndividu_Bancass1(String bdate, String edate, String lus_id, String status, String produk) {
//		return this.uwDao.selectreportUWIndividu_Bancass1(bdate, edate, lus_id, status, produk);
//	}
//	
//	public List selectreportUWIndividu_Bancass2(String bdate, String edate, String lus_id, String status, String produk) {
//		return this.uwDao.selectreportUWIndividu_Bancass2(bdate, edate, lus_id, status, produk);
//	}
//	
//	public List selectreportUWIndividu_Sekuritas(String bdate, String edate, String lus_id, String status, String produk) {
//		return this.uwDao.selectreportUWIndividu_Sekuritas(bdate, edate, lus_id, status, produk);
//	}
//	
	public List selectreportUWIndividu(Map params) {
		return this.uwDao.selectreportUWIndividu(params);
	}
	
	public List selectreportUWIndividu_ws_payroll(String bdate, String edate, String lus_id, String status, String produk) {
		return this.uwDao.selectreportUWIndividu_ws_payroll(bdate, edate, lus_id, status, produk);
	}
	
//	public List selectreportUWIndividu_WS_MNC_FCD(String bdate, String edate, String lus_id, String status, String produk) {
//		return this.uwDao.selectreportUWIndividu_WS_MNC_FCD(bdate, edate, lus_id, status, produk);
//	}
//	
	public List select_spesifik_produk_rider(Integer lsbs_id, Integer lsdbs_from, Integer lsdbs_to){
		return bacDao.select_spesifik_produk_rider(lsbs_id, lsdbs_from, lsdbs_to);
	}
	
	public Integer selectMst_reas(String spaj)
	{
		return this.uwDao.selectMst_reas(spaj);
	}
	/**
	 * fungsi untuk update informasi medis dengan kondisi sebelum melakukan REAS (Pemegang dan Tertanggung Untuk Proses Endors)
	 * @author ryan
	 * @return 
	 */
	public void updateflagMedis(String spaj, String medis, User currentUser, String alasan)
	{	Date ldt_now = commonDao.selectSysdate();
		Pemegang2 pmg=basDao.selectpemegangpolis(spaj);
		uwDao.updateflagMedis(spaj,medis);
		insertLst_ulangan(spaj, ldt_now, "EDIT BY SYSTEM", pmg.getLssp_id(),currentUser.getLus_id(), alasan);
	}
	
	public void updatePenghasilan(String mamah2, String penghasilan, String spaj,User currentUser, String mamah)
	{	Date ldt_now = commonDao.selectSysdate();
		Pemegang2 pmg=basDao.selectpemegangpolis(spaj);
		Tertanggung ttg =selectttg(spaj);
		if (pmg.getLsre_id()==1){
		uwDao.updatePenghasilan(pmg.getMcl_id(),penghasilan,mamah);}
		else{
		uwDao.updatePenghasilan(pmg.getMcl_id(),penghasilan,mamah);
		uwDao.updatePenghasilan(ttg.getMcl_id(),penghasilan,mamah2);
		}

		insertLst_ulangan(spaj, ldt_now, "EDIT BY SYSTEM", pmg.getLssp_id(),currentUser.getLus_id(), "EDIT DATA-DATA POLIS");
	}
	public void updateMspoCallDate(String spaj , Date tglCall)
	{	
		uwDao.updateMspoCallDate(spaj,tglCall);
		
	}
	
	//Select spaj questionare untuk mengetahui sudah isi apa belum, canpri
		public Integer selectFlagQuestionare(String spaj)
		{
			return this.uwDao.selectFlagQuestionare(spaj);
		}
		
		public Integer selectCallDate(String spaj)
		{
			return this.uwDao.selectCallDate(spaj);
		}
	//contoh spaj = 42200900812
	public List selectquestionareDMTM(String spaj) {
		return this.uwDao.selectquestionareDMTM(spaj);
	}	
	public List selectDataPemegangPolis(String spaj){
		return this.uwDao.selectDataPemegangPolisSpt(spaj);
		
	}
	/**
	 * fungsi untuk select data agen beserta atasan agen tersebut untuk E-Konfirmasi (SPAJ ONLINE)
	 * @author ryan
	 * @return 
	 */
	public Agen select_detilagenWIthLv(String spaj){
		Agen a = this.bacDao.select_detilagenWIthLv(spaj);
		if(a == null) return new Agen(); else return a;
	}
	
	public List selectreportDetailSPAJRefundBatal(String bdate, String lus_id) {
		return this.uwDao.selectreportDetailSPAJRefundBatal(bdate, lus_id);
	}
	
	public Date selectPrintDate(String spaj){
		return uwDao.selectPrintDate(spaj);
	}
	
	public List selectreportSummarySPAJRefundBatal(String bdate, String lus_id) {
		return this.uwDao.selectreportSummarySPAJRefundBatal(bdate, lus_id);
	}
	
	public Integer selectLsbpIdFromNoRek(int lsrek_id){
		return this.bacDao.selectLsbpIdFromNoRek(lsrek_id);
	}
	
	public List<Map> selectHealthClaimEBTolakSum(String spaj, String insured_no){
		return uwDao.selectHealthClaimEBTolakSum(spaj, insured_no);
	}
	
	public List<Map> selectHealthClaimEBAcceptSum(String spaj, String insured_no){
		return uwDao.selectHealthClaimEBAcceptSum(spaj, insured_no);
	}
	
	public List<Map> selectHealthClaimEBPREAcceptSum(String spaj, String insured_no){
		return uwDao.selectHealthClaimEBPREAcceptSum(spaj, insured_no);
	}
	
	public List<Map> selectHealthClaimEBPUSum(String spaj, String insured_no){
		return uwDao.selectHealthClaimEBPUSum(spaj, insured_no);
	}
	
	public List<Map> selectHealthClaimSum(String spaj){
		return uwDao.selectHealthClaimSum(spaj);
	}
	
	public List<Map> selectHealthClaimTMSum(String spaj){
		return uwDao.selectHealthClaimTMSum(spaj);
	}
	
	public List<Map> selectRekAJSMSIG(){
		return bacDao.selectRekAJSMSIG();
	}
	
	/**
	 * fungsi untuk update no blanko, dimana no nya itu digenerate dari counter(SPAJ ONLINE)
	 * @author ryan
	 * @return 
	 */
	public String spajOnline (String reg_spaj,String lca_id){
		Pemegang2 pmg=basDao.selectpemegangpolis(reg_spaj);
		String noE=null;
		if (pmg.getMspo_no_blanko()!=null){
			/*noE =noTest(lca_id);
			updateBlanko(reg_spaj, noE);*/
			noE=pmg.getMspo_no_blanko();
		}else{
			noE =noTest(lca_id);
			updateBlanko(reg_spaj, noE);
		}
		return noE;		
	}

	/**
	 * fungsi untuk update no blanko, dimana no nya itu digenerate dari counter(SPAJ ONLINE SYARIAH)
	 * @author MANTA
	 * @return 
	 */
	public String spajOnlineSyariah(String reg_spaj,String lca_id){
		Pemegang2 pmg=basDao.selectpemegangpolis(reg_spaj);
		String noE=null;
		if(pmg.getMspo_no_blanko()!=null){
			/*noE =noTest(lca_id);
			updateBlanko(reg_spaj, noE);*/
			noE=pmg.getMspo_no_blanko();
		}else{
			noE =noTestSyariah(lca_id);
			updateBlanko(reg_spaj, noE);
		}
		return noE;		
	}
	
	/*public String spajMall (String reg_spaj,String lca_id){
		Pemegang2 pmg=basDao.selectpemegangpolis(reg_spaj);
		String noE=null;
				if (pmg.getMspo_no_blanko()!=null){
				noE =noTest(lca_id);
				updateBlanko(reg_spaj, noE);
					noE=pmg.getMspo_no_blanko();
				}else{
					noE =noMall(lca_id);
					updateBlanko(reg_spaj, noE);
				}
				return noE;
			
		}*/
	public Integer prosesAutoDebetNB(String reg_spaj, User currentUser){
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		int setNopol = 0;
		Akseptasi akseptasi = new Akseptasi();
		akseptasi.setSpaj(reg_spaj);
		akseptasi.setLsbsId(Integer.parseInt(uwDao.selectLsbsId(reg_spaj)) );
		akseptasi.setLsdbsNumber(Integer.parseInt(uwDao.selectLsdbsNumber(reg_spaj)) );
		String lca_id = uwDao.selectLcaIdMstPolicyBasedSpaj(reg_spaj);
		akseptasi.setLcaId(lca_id);
		setNopol=uwDao.wf_set_nopol(akseptasi, 1);
		if(setNopol>0){//rollback dan tampilkan message
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			return setNopol;
		}
		Policy policy=uwDao.selectDw1Underwriting(akseptasi.getSpaj(),1,1);
		List lsDp=uwDao.selectMstDepositPremium(akseptasi.getSpaj(),new Integer(1));
		BindException err = null;
		if(uwDao.wf_ins_bill(reg_spaj,1,new Integer(1),akseptasi.getLsbsId(),akseptasi.getLsdbsNumber(),56,1,lsDp,currentUser.getLus_id(),policy,err)){			
			uwDao.updateMstInsured(reg_spaj,118);
			uwDao.updateMstPolicy(reg_spaj,118);			
			uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Transfer Proses AutoDebet Ke Finance", reg_spaj, 0);
			uwDao.saveMstTransHistory(reg_spaj, "tgl_transfer_autodebet_nb", null, null, null);
		}		
		return setNopol;
	}
	
	public Integer selectCountMstBillingNB(String reg_spaj){
		return  uwDao.selectCountMstBillingNB(reg_spaj);
	}
	
	/**
	 * fungsi untuk select date scheduler
	 * @author lufi
	 * @return 
	 */
	public String selectStatusScheduler(){
		return this.uwDao.selectStatusScheduler();
	}
	
	/**
	 * fungsi untuk select Direksi yg sudah aksep Spt by Email
	 * @author lufi
	 * @return 
	 */
	public String selectDireksi(String reg_spaj){
		return this.uwDao.selectDireksi(reg_spaj);
	}
	
	public Integer selectCekRef(String nama_p, String tgl_lahir_p, String nama_t, String tgl_lahir_t) {
		return bacDao.selectCekRef(nama_p, tgl_lahir_p, nama_t, tgl_lahir_t);
	}

	//Referensi
	public List<Map> selectDataPolisRef(String spaj, String polis) {
		return bacDao.selectDataPolisRef(spaj, polis);
	}

	public List selectAgentByPolis(String policy_no) {
		return bacDao.selectAgentByPolis(policy_no);
	}

	public String selectMsag_id(String id) {
		return bacDao.selectMsag_id(id);
	}

	public String selectIdSeller(String nama, Date tgl_lahir, String id) {
		return bacDao.selectIdSeller(nama, tgl_lahir, id);
	}

	public PrwSeller selectDataPs(String id) {
		return bacDao.selectDataPs(id);
	}

	public String getIdSeller() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
		String id = null;
		
		Long counter = bacDao.selectCounterIDSeller();
		/*Integer cnt = String.valueOf(counter).length();
		if(cnt==1){
			id = "000"+String.valueOf(counter);
		}else if(cnt==2){
			id = "00"+String.valueOf(counter);
		}else if(cnt==3){
			id = "0"+String.valueOf(counter);
		}else{
			id = String.valueOf(counter);
		}
		
		id = df.format(uwDao.selectSysdate("dd", true, 0))+""+id;*/
		
		id = "GM"+StringUtils.leftPad(counter.toString(), 8, '0');
		
		counter  += 1;
		
		bacDao.UpdateCounterIDSeller(counter);
		
		return id;
	}

	public void insertDataPWRSeller(PrwSeller ps) {
		bacDao.insertDataPWRSeller(ps);
	}

	public void updateMsag_idUserExternal(String ids, String id) {
		bacDao.updateMsag_idUserExternal(ids, id);
	}

	public void insertReferensi(String ids, String nama, String tgl_lahir, String no_telp, String kd_agen, String agen, String jenis, String lus_id){
		bacDao.insertReferensi(ids, nama, tgl_lahir, no_telp, kd_agen, agen, jenis, lus_id);
	}

	public List selectReference(String id){
		return bacDao.selectReference(id);
	}

	public void updateIdRef(String spaj, String ids) {
		bacDao.updateIdRef(spaj, ids);
	}

	public String selectCekRef(String nama, String tgl_lahir, String jenis) {
		return bacDao.selectCekRef(nama, tgl_lahir, jenis);
	}
	
	public List namaBank(String lbn_id){
		return this.uwDao.namaBank(lbn_id);
	}

	public void updateFlagDTRX(String spaj) {
		bacDao.updateFlagDTRX(spaj);
	}

	public String selectKodeTambangEmas(String kode) {
		return bacDao.selectKodeTambangEmas(kode);
	}

	public void insertKodeTE(String kode, String lus_id) {
		bacDao.insertKodeTE(kode, lus_id);
	}

	public List selectKodeGenerate(String nama, String tgl_lahir) {
		return bacDao.selectKodeGenerate(nama,tgl_lahir);
	}
	
	/**
	 * view auto debet di menu follow up billing
	 * @author Canpri
	 * @since 22 Jun 2012
	 * @param reg_spaj
	 * @return List
	 * 
	 */
	public List selectAutoDebetFollowupBilling(String reg_spaj) {
		return basDao.selectAutoDebetFollowupBilling(reg_spaj);
	}
	
	/**
	 * view billing di menu follow up billing
	 * @author Canpri
	 * @since 25 Jul 2012
	 * @param reg_spaj
	 * @return List
	 * 
	 */
	public List selectViewBillingFollowup(String reg_spaj) {
		return basDao.selectViewBillingFollowup(reg_spaj);
	}
	
	/**
	 * view tahapan di menu follow up billing
	 * @author Canpri
	 * @since 25 Jul 2012
	 * @param reg_spaj
	 * @return List
	 * 
	 */
	public List selectViewTahapanFollowup(String reg_spaj) {
		return basDao.selectViewTahapanFollowup(reg_spaj);
	}
	
	/**
	 * view simpanan di menu follow up billing
	 * @author Canpri
	 * @since 25 Jul 2012
	 * @param reg_spaj
	 * @return List
	 * 
	 */
	public List selectViewSimpananFollowup(String reg_spaj) {
		return basDao.selectViewSimpananFollowup(reg_spaj);
	}
	
	/**
	 * view call summary di menu follow up billing
	 * @author Canpri
	 * @since 01 Aug 2012
	 * @param reg_spaj
	 * @return List
	 * 
	 */
	public List selectViewCallSummary(String reg_spaj) {
		return basDao.selectViewCallSummary(reg_spaj);
	}
	
	public List <Map> selectMstInbox(String reg_spaj, String tipe){		
		return uwDao.selectMstInbox(reg_spaj, tipe);
	}

	public String selectKYCnewBisJnsTopUp(String spaj){
		return uwDao.selectKYCnewBisJnsTopUp(spaj);
	}
	public Integer selectCountLstHighRiskCust(String mpnJobDesc, String mklKerja, String mklIndustri){
		return uwDao.selectCountLstHighRiskCust(mpnJobDesc, mklKerja, mklIndustri);
	}
	
	public Double selectJumTop_x(String reg_spaj){
		return (Double) this.uwDao.selectJumTop_x(reg_spaj);
	}
	
	/**Fungsi:	Untuk menampilkan data KYC
	 * @param 	dariTanggal
	 * @param 	sampaiTanggal
	 * @return	List
	 * @author 	Andhika
	 */
	public List selectKYCtopup_main(String dariTanggal,String sampaiTanggal){
		return uwDao.selectKYCtopup_main(dariTanggal,sampaiTanggal);
	}
	
	/**Fungsi : Untuk Memfilter List dari KYC result dengan batasan total premi 100 juta atau daftar HRC (High risk customer)
	 * 
	 * @param KYCtopUp
	 * @author 	Andhika
	 */
	public List selectLsKycTopUp(List KYCtopUp){
		List lsKycTopUp=new ArrayList();
		Double batasan=new Double(100000000);
		int  row=0;
		for(int i=0;i<KYCtopUp.size();i++){
			TopUpCase ktu =(TopUpCase)KYCtopUp.get(i);
			if(ktu.getLsbs_id().intValue() == 164){
				ktu.setNama_topup("TUNGGAL");
			}else if(ktu.getJmlh_tu()==2){
				ktu.setNama_topup("TUNGGAL DAN BERKALA");
//				ktu.setNama_topup(selectKYCnewBisJnsTopUp(ktu.getReg_spaj()));
			}else if(ktu.getJmlh_tu()==1){
				ktu.setNama_topup(selectKYCnewBisJnsTopUp(ktu.getReg_spaj()));
			}
			
			double total=ktu.getTotal_tu().doubleValue()+ktu.getPremi_pokok().doubleValue();
			//cek daftar pekerjaan,, apakah termasuk dalam daftar High Risk Customer (HCR)
			int hrcFilter=selectCountLstHighRiskCust(ktu.getMpn_job_desc(),ktu.getMkl_kerja(), ktu.getMkl_industri());

			
			List Tu_x =new ArrayList();
			
			Tu_x = uwDao.selectKe(ktu.getReg_spaj());
			logger.info(Tu_x.size());
			for(int z=0; z<Tu_x.size(); z++){
				if(total>=batasan.doubleValue() || hrcFilter>0){//pengecekan apakah total(premi + top up lebih besar dari 100jt ato masuk daftar HRC)
					ktu.setFlagKyc(0);
					ktu.setRow(row);
					ktu.setTop_ke(z+1);
					User user= uwDao.selectMstpositionSpajUserAccept(ktu.getReg_spaj());
					if(user!=null)
						ktu.setLus_full_name(user.getLus_full_name());
					row++;
					lsKycTopUp.add(ktu);
				}
			}
		}
		return lsKycTopUp;
	}
	
	public Integer selectExistingSimpol(String pp_mcl_first,String t_mcl_first,String pp_mspe_date_birth,String t_mspe_date_birth)
	{
		return this.bacDao.selectExistingSimpol(pp_mcl_first,t_mcl_first,pp_mspe_date_birth,t_mspe_date_birth);
	}
	
	public Map proc_hit_additional_unit(Object cmd,Errors err, String kurs,Double premi_pokok)throws ServletException,IOException,Exception{
		return this.hitungBac.proseshitungadditionalunit(cmd, err, kurs, premi_pokok);
	}
	
	public Integer selectInboxCheckingLspdId(String reg_spaj, Integer lspd_id){
		return uwDao.selectInboxCheckingLspdId(reg_spaj, lspd_id);
	}

	public Map selectPemegangSimpleBac(String reg_id){
		return this.uwDao.selectPemegangSimpleBac(reg_id);
	} 
	
	public Map selectTertanggungSimpleBac(String reg_id){
		return this.uwDao.selectTertanggungSimpleBac(reg_id);
	}
	
	public Integer prosesSimultanSimpleBacNew(Command command,String lsClientPpOld,String lsClientTtOld,String idSimultanPp,String idSimultanTt)throws Exception{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return simultan.prosesSimultanSimpleBac(command,lsClientPpOld, lsClientTtOld,idSimultanPp,idSimultanTt);
		//Untuk Proses ulang simultan
//		return simultan.prosesSimultanUlang(command, lsClientPpOld, lsClientTtOld, idSimultanPp, idSimultanTt);
		
	}
	
	public List selectNik(String nik){
		
		return  this.commonDao.selectNik(nik);
	}
	
	public Integer selectExistingNik(String nik) {
		return this.commonDao.selectExistingNik(nik);
	}
	public Integer updateNik(String nik, String lus_id){
		return this.commonDao.updateNik(nik,lus_id);
	}
	
	public List selectProdukTE() {
		return uwDao.selectProdukTE();
	}

	/**
	 * Cek yang direferensikan agen atau bukan
	 * @author Canpri
	 * @since 13 Sep 2012
	 * @param nama dan tanggal lahir
	 * @return Integer
	 */
	public Integer cekRef(String nama, String tgl_lahir) {
		return uwDao.cekRef(nama,tgl_lahir);
	}

	/**
	 * Select report Referensi (Tambang Emas)
	 * @author Canpri
	 * @since 13 Sep 2012
	 * @param bdate, edate
	 * @return List
	 */
	public List selectReportRef(String bdate, String edate) {
		return uwDao.selectReportRef(bdate, edate);
	}

	/**
	 * Select id Trx (Tambang Emas)
	 * @author Canpri
	 * @since 18 Sep 2012
	 * @param id_ref, spaj, jenis
	 * @return List
	 */
	public String selectIdTrx(String id_ref, String reg_spaj, String jenis) {
		return bacDao.selectIdTrx(id_ref, reg_spaj, jenis);
	}

	/**
	 * Select insert Prw Trx (Tambang Emas)
	 * @author Canpri
	 * @since 18 Sep 2012
	 * @param id_trx, id_ref, spaj, jenis_trx
	 * @return List
	 */
	public void insertPwrTrx(String id_trx, String id_ref, String reg_spaj, String jenis_trx) {
		bacDao.insertPwrTrx(id_trx, id_ref, reg_spaj, jenis_trx);
	}
	
	/**
	 * Select insert Prw DTrx (Tambang Emas)
	 * @author Canpri
	 * @since 18 Sep 2012
	 * @param id_trx, id_ref, spaj
	 * @return List
	 */
	public void insertPwrDTrx(String id_trx, String item, String reg_spaj, Double premi, Integer point, String lus_id) {
		bacDao.insertPwrDTrx(id_trx, item, reg_spaj, premi, point, lus_id);
	}

	/**
	 * Select update flag kode generate (Tambang Emas)
	 * @author Canpri
	 * @since 18 Sep 2012
	 * @param id_ref, spaj, nama, tgl_lahir
	 * @return List
	 */
	public void updateKodeTE(String id_ref, String mcl_first, String format,
			String reg_spaj) {
		bacDao.updateKodeTE(id_ref, mcl_first, format, reg_spaj);
	}

	/**
	 * Update fire_id di prw_trx menjadi reg_spaj (Tambang Emas)
	 * @author Canpri
	 * @since 18 Sep 2012
	 * @param id_ref, fire_id
	 */
	public void updatePrwTrx(String reg_spaj, String fire_id) {
		bacDao.updatePrwTrx(reg_spaj, fire_id);
	}

	/**
	 * Cek apakah ada topup premi
	 * 
	 * @author Canpri 
	 * @since Sep 24, 2012
	 * @param followup
	 * @return premi_ke
	 */
	public Integer selectFollowupPremiKeTopup(Followup followup) {
		return basDao.selectFollowupPremiKeTopup(followup);
	}
	
	public List selectViewKomisiAsli(String reg_spaj){
		return this.uwDao.selectViewKomisiAsli(reg_spaj);
	}
	
	public Map selectLifeRating_ggtsgptsgot(BigDecimal GGTx, BigDecimal SGOTx_SGPTx){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ggt", GGTx);
		params.put("sgot_sgpt", SGOTx_SGPTx);
		return this.uwDao.selectLifeRating_ggtsgptsgot(params);
	}
	
	public List selectLifeRating(Map life_rating){
		
		List<Map> result = new ArrayList<Map>();
		
		String lr_1 = (String) life_rating.get("VALUE_1");
		String lr_2 = (String) life_rating.get("VALUE_2");
		
		String lr_temp_1 = lr_1.replaceAll(" ", "").replace("+", "").replace("up", "");
		if(!(lr_temp_1.equals("Std") || lr_temp_1.equals("P"))){
			int lr_value_1 = Integer.parseInt(lr_temp_1);
			if(lr_value_1 > 100){
				lr_1 = "> +100";
			}
		}
		
		String lr_temp_2 = lr_2.replaceAll(" ", "").replace("+", "").replace("up", "");
		if(!(lr_temp_2.equals("Std") || lr_temp_2.equals("P"))){
			int lr_value_2 = Integer.parseInt(lr_temp_2);
			if(lr_value_2 > 100){
				lr_2 = "> +100";
			}
		}
		
		if(lr_1.equals(lr_2)){
			result = uwDao.selectLifeRating(lr_1);
		}else{
			result = uwDao.selectLifeRating(lr_1);
			result.addAll(uwDao.selectLifeRating(lr_2));
		}
		
		return result;
	}
	
	public String selectMspIdFromMspFireId(String msp_fire_id) {
		return this.uwDao.selectMspIdFromMspFireId(msp_fire_id);
	}

	/*
	 * Insert redeem Tambang Emas
	 * 
	 * @author Canpri 
	 * @since Sep 27, 2012
	 * @param redeem
	 */
	public void insertRedeemTE(Redeem redeem) {
		bacDao.insertRedeemTE(redeem);
	}

	/**
	 * Select jumlah poin yang belum di aksep redeemnya
	 * 
	 * @author Canpri 
	 * @since Sep 28, 2012
	 * @param kd_ref
	 * @return jumlah poin
	 */
	public Long selectPoinRedeem(String kd_ref) {
		return bacDao.selectPoinRedeem(kd_ref);
	}

	/**
	 * Select redeem yang di ambil
	 * 
	 * @author Canpri 
	 * @since Sep 28, 2012
	 * @param kd_ref, status approve, id_redeem
	 * @return List
	 */
	public List selectRedeemTE(String kd, String status, String id_redeem) {
		return bacDao.selectRedeemTE(kd, status, id_redeem);
	}

	/**
	 * Update redeem yang sudah di approve
	 * 
	 * @author Canpri 
	 * @since Oct 01, 2012
	 * @param id_redeem, status (1=approve, 2=tolak), lus_id
	 */
	public void updateRedeem(String id_redeem, String status, String lus_id) {
		bacDao.updateRedeem(id_redeem, status, lus_id);
	}
	
	//=============
	
	public Map fixBegDatePas(String reg_id, Date beg_date, Date end_date, Integer lus_id){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		int err = 0;
		Map<String, Object> dataPas = new HashMap<String, Object>();
		Map<String, Object> dataPolicy = new HashMap<String, Object>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("reg_id", reg_id);
			param.put("beg_date", beg_date);
			param.put("end_date", end_date);
			param.put("lus_id", lus_id);
			
			dataPas = uwDao.selectDataPasForFix(reg_id);
			if(dataPas == null){
				result.put("position", "-");
				result.put("err", 2);// DATA TIDAK DITEMUKAN
			}else if(dataPas != null){
				String spaj = "";
				if(dataPas.get("REG_SPAJ") != null){
					spaj = dataPas.get("REG_SPAJ").toString();
				}else{
					spaj = "";
				}
				Date begdate_from = (Date) dataPas.get("MSP_PAS_BEG_DATE");
				Date enddate_from = (Date) dataPas.get("MSP_PAS_END_DATE");
				Integer lspd_id = Integer.parseInt(dataPas.get("LSPD_ID").toString());
				int proses = 0;
				if(Integer.parseInt(dataPas.get("LSPD_ID").toString()) <= 4){
					proses = 1;
				}else{
					result.put("err", 3);// DATA TIDAK DAPAT DIPROSES KARENA POSISI DOKUMEN
				}
				
				if(proses == 1){
					
					Map<String, Object> paramFix = new HashMap<String, Object>();
					paramFix.put("reg_id", reg_id);
					paramFix.put("reg_spaj", spaj);
					paramFix.put("beg_date", beg_date);
					paramFix.put("end_date", end_date);
					//dicomment agar di product insured semua produk keupdate semua (asumsi begdate dan enddate sama)
					//apabila ada kasus yg berbeda, jgn lupa dimodif dan ditambahkan di ulangan untuk historinya
					//paramFix.put("lsbs_id", 187);//PAS 
					
					//MST_PAS_SMS
						uwDao.fixBegDatePas(paramFix);
					//MST_PAS_POSITION_SPAJ_PAS
						String h_begdateFrom = sdf.format(begdate_from);
						String h_begdateto = sdf.format(beg_date);
						String h_enddateFrom = sdf.format(enddate_from);
						String h_enddateto = sdf.format(end_date);
						uwDao.insertMstPositionSpajPas(lus_id.toString(), "FIX BY UPLOAD:UBAH BEGDATE "+h_begdateFrom+" KE "+h_begdateto+" & ENDDATE "+h_enddateFrom+" KE "+h_enddateto, reg_id, 5);
					if(!Common.isEmpty(spaj)){
						dataPolicy = uwDao.selectDataPolicyForFix(spaj);
						Date beg_date_bill = (Date) beg_date.clone();
						Date end_date_bill = null;
						SimpleDateFormat sdfDd=new SimpleDateFormat("dd");
						Integer liPmode,liPperiod,liMonth;
						Date due_date = null;
						Calendar calTemp=new GregorianCalendar(2006,06-1,1);
						liPmode = Integer.parseInt(dataPolicy.get("LSCB_ID").toString());
						if(liPmode!=0){
							liMonth=uwDao.selectLstPayModeLscbTtlMonth(liPmode);
							Date dTemp=uwDao.selectAddMonths(defaultDateFormat.format(beg_date_bill),liMonth);
							end_date_bill=FormatDate.add(dTemp,Calendar.DATE,-1);
							//Himmia 30/01/2001
							if(sdfDd.format(end_date_bill).equals(sdfDd.format(beg_date_bill))) {
								end_date_bill=FormatDate.add(end_date_bill,Calendar.DATE,-1);
							}
						}
						//yusuf 02062006 due date lebih besar dari 1 juni 2006 ditambah 1 minggu
						// direvisi jadi ditambah 30 hari
						if(beg_date_bill.compareTo(calTemp.getTime()) >0){
							//ldtDueDate=FormatDate.add(ldtBegDate,Calendar.DATE,7);
							due_date=FormatDate.add(beg_date_bill,Calendar.DATE,30);
						}else{
							due_date = uwDao.selectAddMonths(defaultDateFormat.format(beg_date_bill),new Integer(1));
						}
						Date next_bill=FormatDate.add(end_date_bill,Calendar.DATE,1);
						String h_begdatebillFrom = sdf.format((Date) dataPolicy.get("MSBI_BEG_DATE"));
						String h_begdatebillto = sdf.format(beg_date_bill);
						String h_enddatebillFrom = sdf.format((Date) dataPolicy.get("MSBI_END_DATE"));
						String h_enddatebillto = sdf.format(end_date_bill);
						String h_duedateFrom = sdf.format((Date) dataPolicy.get("MSBI_DUE_DATE"));
						String h_duedateto = sdf.format(due_date);
						String h_nextbillFrom = sdf.format((Date) dataPolicy.get("MSPO_NEXT_BILL"));
						String h_nextbillto = sdf.format(next_bill);
						
						paramFix.put("next_bill", next_bill);
						paramFix.put("beg_date_bill", beg_date_bill);
						paramFix.put("end_date_bill", end_date_bill);
						paramFix.put("due_date_bill", due_date);
						
						//MST_POLICY
							uwDao.fixBegDatePolicy(paramFix);
						//MST_INSURED
							uwDao.fixBegDateInsured(paramFix);
						//MST_PRODUCT_INSURED
							uwDao.fixBegDateProductInsured(paramFix);
						//MST_BILLING
							uwDao.fixBegDateBilling(paramFix);
						//LST_ULANGAN
							Date nowDate = commonDao.selectSysdate();
							uwDao.insertLst_ulangan(spaj, nowDate, "UBAH DATA", 1, lus_id.toString(), "FIX BY UPLOAD:UBAH BEGDATE "+h_begdateFrom+" KE "+h_begdateto+" & ENDDATE "+h_enddateFrom+" KE "+h_enddateto+" & NEXTBILL "+h_nextbillFrom+" KE "+h_nextbillto);
							nowDate.setMinutes(nowDate.getMinutes()+1);
							uwDao.insertLst_ulangan(spaj, nowDate, "UBAH DATA", 1, lus_id.toString(), "FIX BY UPLOAD:UBAH BEGDATE BILL "+h_begdatebillFrom+" KE "+h_begdatebillto+" & ENDDATE BILL "+h_enddatebillFrom+" KE "+h_enddatebillto+" & DUEDATE BILL "+h_duedateFrom+" KE "+h_duedateto);
							
					}
					result.put("err", 0);// PROSES UPDATE SUKSES
				}
				result.put("position", dataPas.get("NAMA_POSISI"));
			}
		} catch (Exception e) {
			logger.error("ERROR :", e);
			if(dataPas != null){
				if(dataPas.get("LSPD_ID") != null){
					result.put("position", dataPas.get("NAMA_POSISI"));
				}else{
					result.put("position", "-");
				}
			}else{
				result.put("position", "-");
			}
			result.put("err", 1);// PROSES UPDATE ERROR
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return result;
	}
	
	//==============
	// *VIPcard
	public List<Simcard> selectCetakVipCard(int msc_jenis, String lca_id, int flag_print, int jumlah_print) {
		return this.simasCardDao.selectCetakVipCard(msc_jenis, lca_id, flag_print, jumlah_print);
	}
	// *Kuesioner
	public List select_tertanggung_rekrut(String kode_rekrut)
	{
		return this.rekruitmentDao.select_tertanggung_rekrut(kode_rekrut);
	}
	
//	public List selecttm(String query){
//	
//	return basDao.selectTM(query);
// }
	
	public void insertMstHistDownload(Map map)
	{
		this.commonDao.insertMstHistDownload(map);
	}
	
	public List<HashMap> selectdaftarRegKuesioner(String msag_id)
	{
		return  rekruitmentDao.selectdaftarRegKuesioner(msag_id);
	}
	
	public Map selectagenrekrut(String tipe, String kode) 
	{
		
		return bacDao.selectagenrekrut(tipe, kode);
	}
	
	public void updateKuesioner(Kuesioner kuesioner) {
		rekruitmentDao.updateKuesioner(kuesioner);
	}
	
	public String selectEmailAdmin(String msag_id)
	{
		return rekruitmentDao.selectEmailAdmin(msag_id);
	}

	public Integer selectLibur(String time) {
		return basDao.selectLibur(time);
	}

	/**
	 * Select spaj batal/refund
	 * 
	 * @author Canpri 
	 * @since Oct 11, 2012
	 * @param spaj
	 */
	public List selectDocBatal(String spaj) {
		return refundDao.selectDocBatal(spaj);
	}

	public String selectBancassTeam(String reg_spaj) {
		return uwDao.selectBancassTeam(reg_spaj);
	}

	public Product selectMstProductInsuredUtamaFromSpaj(String reg_spaj) {
		return uwDao.selectMstProductInsuredUtamaFromSpaj(reg_spaj);
	}
	
	public List selectReportDataUpload(String bdate, String edate, String jenis, Integer jn_transfer) {
		return basDao.selectReportDataUpload(bdate, edate, jenis, jn_transfer);
	}
	
	public List reportFollowupBillingPL(String bdate, String edate, String lca, Integer jn_report) {
		return basDao.reportFollowupBillingPL(bdate, edate, lca, jn_report);
	}

	/**
	 * Select email leader agen lvl 1 dan 2
	 * 
	 * @author Canpri 
	 * @since Nov 13, 2012
	 * @param reg_spaj
	 */
	public List selectEmailLeader(String reg_spaj) {
		return basDao.selectEmailLeader(reg_spaj);
	}

	/**
	 * Select list Hadiah dari Pogram Hadiah Stable Save
	 * 
	 * @author Canpri 
	 * @since Nov 14, 2012
	 * @param reg_spaj
	 */
	public List<DropDown> selectHadiahStableSave(String reg_spaj) {
		return basDao.selectHadiahStableSave(reg_spaj);
	}
	
	/**
	 * Print surat tanda terima Hadiah
	 * 
	 * @author Canpri 
	 * @since Nov 14, 2012
	 * @param reg_spaj
	 */
	public String printHadiahStableSave(Hadiah hadiah, User currentUser) {
		return basDao.printHadiahStableSave(hadiah, currentUser);
	}

	/**
	 * Select user Bas untuk report Summary Input
	 * 
	 * @author Canpri 
	 * @since Dec 03, 2012
	 * @param 
	 */
	public List<DropDown> selectUserBasSummaryInput(String user) {
		return basDao.selectUserBasSummaryInput(user);
	}
	
	public List<DropDown> selectUserBasSummaryInputNew(int pusat) {
		return basDao.selectUserBasSummaryInputNew(pusat);
	}
	
	public Integer selectAgentActiveOrNot(String msag_id){
		return bacDao.selectAgentActiveOrNot(msag_id);
	}
	
	/**
	 * Update PAS BP-SMS untuk kelengkapan data sebelum Transfer/UW
	 * 
	 * @author Adrian N
	 * @since Dec 03, 2012
	 * @param 
	 */
	public Pas updateBPSMS(Pas pas){
		pas.setStatus(0);
		try{			
			Integer cekUpd = uwDao.updateBPSMS(pas);			
			// Return Update: 1 berhasil // 0 gagal
			if(cekUpd==0){
				pas.setStatus(1);					
			}else{
				uwDao.insertMstPositionSpajPas(pas.getLus_id().toString(), "EDIT DATA", pas.getMsp_fire_id(), 5);
			}				
		}catch(Exception e){
			logger.info(e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			pas.setStatus(1);
		}
		return pas;
	}
	
	
	public List select_detilprodukutama_viewer(Integer kode,String spaj)
	{
		return this.uwDao.select_detilprodukutama_viewer(kode,spaj);
	}
	
	public List selectListNab(){
		return financeDao.selectListNab();
	}
	
	public String selectJenisInvestNab(String id){
		return financeDao.selectJenisInvestNab(id);
	}
	
	/**@Fungsi:	Untuk menampilkan jumlah data yang terdapat pada tabel EKA.MST_SLINK berdasarkan msl_posisi
	 * @param	String spaj, Integer lspd_id
	 * @return 	Integer
	 * @author 	Deddy
	 * */
	public Integer selectCountMstSlinkBasedPosition(String spaj,Integer lspd_id){
		return uwDao.selectCountMstSlinkBasedPosition(spaj,lspd_id);
	}
	
	// *Andhika - Cek scheduler untuk auto transfer
	public void schedulerTransPolToUw(){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String msh_name = "SCHEDULER AUTOTRANSFER BSM DARI PRINTPOLIS TO UW";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerTransPolToUw(msh_name);
		}	
	}
	
	public Integer selectCountPowerSaveSudahPinjaman(String reg_spaj){
		return bacDao.selectCountPowerSaveSudahPinjaman(reg_spaj);
	}
	
	public Integer selectCountKoefisienUpp(String lsbs_id, String lsdbs_number){
		return uwDao.selectCountKoefisienUpp(lsbs_id, lsdbs_number);
	}

	/**
	 * Select Cabang bank sinarmas di ebdb
	 * 
	 * @author Canpri 
	 * @since Dec 14, 2012
	 * @param 
	 */
	public List<DropDown> selectCabangBSM() {
		return basDao.selectCabangBSM();
	}	
	
	public Integer selectLineBusLstBisnis(String lsbs_id){
		return bacDao.selectLineBusLstBisnis(lsbs_id);
	}
	
	public List<Map> selectValidasiNewStableLink(String spaj){
		return this.bacDao.selectValidasiNewStableLink(spaj);
	}
	
	public Integer selectjumlahAdmedika(String reg_spaj) {
		return uwDao.selectjumlahAdmedika(reg_spaj);
	}
	
	public List selectListMstHistoryUploadByStatus(String jenis, String upload_jenis){
		return commonDao.selectListMstHistoryUploadByStatus(jenis, upload_jenis);
	}
	
	public List selectListMstHistoryUploadByDate(String jenis, String upload_jenis,String tglawal, String tglakhir){
		return commonDao.selectListMstHistoryUploadByDate(jenis, upload_jenis,tglawal,tglakhir);
	}
	
	public List<Map> selectListSpajPremiLanjutan(String lus_id, String lde_id) {
		return basDao.selectListSpajPremiLanjutan(lus_id, lde_id);
	}
	
	public List<Billing> selectMstBillingFlagKuitansi(String spaj, String msbi_bill_no){
		return basDao.selectMstBillingFlagKuitansi(spaj, msbi_bill_no);
	}
	
	public void updateMstBillingBillNo(String spaj, String msbi_bill_no, Integer flag_kuitansi){
		basDao.updateMstBillingBillNo(spaj, msbi_bill_no, flag_kuitansi);
	}
	
	public Boolean prosesKuitansiPremiLanjutan(String spaj, String dirFile, String filename,String msbi_bill_no, HttpServletResponse response){
		try {
			FileUtils.silentPrint(dirFile, filename, null, 1, response);
			basDao.updateMstBillingBillNo(spaj, msbi_bill_no, 2);
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public List selectUserCSsms(String user) {
		return uwDao.selectUserCSsms(user);
	}
	
	public List selectReportSMSHarian(String user_id,String bdate, String edate) {
		return uwDao.selectReportSMSHarian(user_id, bdate, edate);
	}
	public List selectReportSMSBulanan(String user_id,String bdate, String edate){
		return uwDao.selectReportSMSBulanan(user_id, bdate, edate);
	}
	
	public String selectPathMstHistoryUpload2(String upload_id,String code_id){
		return commonDao.selectPathMstHistoryUpload2(upload_id,code_id);
	}
	
	public String selectFilenameMstHistoryUpload2(String upload_id,String code_id){
		return commonDao.selectFilenameMstHistoryUpload2(upload_id,code_id);
	}
	
	public Boolean prosesPermintaanBSB(String spaj, Integer lsbs_id, Integer lsdbs_number, User currentUser, Datausulan datausulan){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date nowDate = commonDao.selectSysdate();
		// - Produk Danamas Prima : yancindy.malino@sinarmas-am.co.id, yancindy.malino@sinarmassekuritas.co.id, cindy@sinarmassekuritas.co.id, dan natalia listiawati ; CC : user uw
		// - Produk Simas Prima Bank Sinarmas : email cs sesuai cabang BSM (terlampir) dan AO sesuai cabang BSM ; CC : user UW
		// - Produk Power Save Syariah BSM : cs0910@banksinarmas.com, taufik.rudiyanto@banksinarmas.com, sadewo@sinarmasmsiglife.co.id, dan siti nihayatun ; CC : user UW
		// - Produk Personal Accident ABD
		String[] emailto = new String[]{currentUser.getEmail()};
		String[] emailcc = new String[]{""};
		String mspo_policy_no = uwDao.selectNoPolisFormat(spaj);
		Pemegang dataPP = bacDao.selectpp(spaj);
		Tertanggung dataTT = bacDao.selectttg(spaj);
		String premi = FormatString.formatCurrency(datausulan.getLku_symbol(), new BigDecimal(datausulan.getMspr_premium()));
		Integer mgi = uwDao.selectMasaGaransiInvestasi(spaj, 1, 1);
		String nama_cabang = bacDao.selectCabangBiiPolis(spaj);
		String subject = "Permohonan Pengiriman BSB untuk Polis No."+ mspo_policy_no;
		String message = "Dear,<br><br>Mohon dapat mengirimkan Bukti Setor Bank/Bukti Pembayaran Premi (dalam bentuk PDF) untuk membantu user Underwriting dalam menginput premi ke dalam sistem.<br><br>"+
						"Nama Pemegang Polis   	: "+dataPP.getMcl_first()+"<br>"+
						"Nama Tertanggung      	: "+dataTT.getMcl_first()+"<br>"+
						"Jumlah Premi          	: "+premi+"<br>"+
						"MGI                   	: "+mgi+" Bulan"+"<br>"+
						"Tanggal Efektif Polis 	: "+df.format(datausulan.getMste_beg_date())+"<br>"+
						"Nama Produk           	: "+datausulan.getLsdbs_name()+"<br>"+
						"Nama Cabang           	: "+nama_cabang+"<br>"+
						"User Input           	: "+currentUser.getLus_full_name()+"<br><br><br>"+
						"Keterlambatan penginputan premi, dapat mengakibatkan keterlambatan pembayaran bunga dan pencairan polis.<br><br>"+
						"Hormat Kami<br><br>"+
						currentUser.getLus_full_name()+"<br>"+
						"PT Asuransi Jiwa Sinarmas MSIG Tbk.";
		if((lsbs_id==175 && lsdbs_number==2)){
			message = "Dear,<br><br>Mohon dapat mengirimkan Bukti Setor Bank/Bukti Pembayaran Kontribusi/Premi (dalam bentuk PDF) untuk membantu user Underwriting dalam menginput kontribusi/premi ke dalam sistem.<br><br>"+
					"Nama Pemegang Polis   	: "+dataPP.getMcl_first()+"<br>"+
					"Nama Peserta	      	: "+dataTT.getMcl_first()+"<br>"+
					"Jumlah Kontribusi/Premi: "+premi+"<br>"+
					"MPI                   	: "+mgi+" Bulan"+"<br>"+
					"Tanggal Efektif Polis 	: "+df.format(datausulan.getMste_beg_date())+"<br>"+
					"Nama Produk           	: "+datausulan.getLsdbs_name()+"<br>"+
					"Nama Cabang           	: "+nama_cabang+"<br>"+
					"User Input           	: "+currentUser.getLus_full_name()+"<br><br><br>"+
					"Keterlambatan penginputan kontribusi/premi, dapat mengakibatkan keterlambatan pembayaran nilai akhir MPI dan pencairan polis.<br><br>"+
					"Hormat Kami<br><br>"+
					currentUser.getLus_full_name()+"<br>"+
					"PT Asuransi Jiwa Sinarmas MSIG Tbk. Unit Syariah";
		}
		if((lsbs_id==142 && lsdbs_number==2) || (lsbs_id==158 && lsdbs_number==6)){
			String email_cab = uwDao.selectEmailCabangBankSinarmas(spaj);
			String email_ao = uwDao.selectEmailAoBankSinarmas(spaj);
			emailto = new String[]{email_cab+";"+email_ao+";"+currentUser.getEmail()};
		}else if((lsbs_id==142 && lsdbs_number==9) || (lsbs_id==158 && lsdbs_number==14)){
			emailto = new String[]{"yancindy.malino@sinarmas-am.co.id;yancindy.malino@sinarmassekuritas.co.id;cindy@sinarmassekuritas.co.id;natalia@sinarmasmsiglife.co.id;"+currentUser.getEmail()};
		}else if((lsbs_id==175 && lsdbs_number==2) ){
			emailto = new String[]{"cs0910@banksinarmas.com;taufik.rudiyanto@banksinarmas.com;sadewo@sinarmasmsiglife.co.id;bancass_bsk01@sinarmasmsiglife.co.id;"+currentUser.getEmail()};
		}else if(lsbs_id==73 && ("4,7,11,14".indexOf(lsdbs_number.toString())>-1)){
			subject = "Permohonan Pengiriman BSB untuk SPAJ No."+ spaj;
			message = "Dear,<br><br>Mohon dapat mengirimkan Bukti Setor Bank/Bukti Pembayaran Premi (dalam bentuk PDF) untuk membantu user Collection dalam menginput premi ke dalam sistem.<br><br>"+
						"Nama Pemegang Polis   	: "+dataPP.getMcl_first()+"<br>"+
						"Nama Tertanggung      	: "+dataTT.getMcl_first()+"<br>"+
						"Jumlah Premi          	: "+premi+"<br>"+
						"Tanggal Efektif Polis 	: "+df.format(datausulan.getMste_beg_date())+"<br>"+
						"Nama Produk           	: "+datausulan.getLsdbs_name()+"<br>"+
						"Nama Cabang           	: "+nama_cabang+"<br>"+
						"User Input           	: "+currentUser.getLus_full_name()+"<br><br><br>"+
						"Hormat Kami<br><br>"+
						currentUser.getLus_full_name()+"<br>"+
						"PT Asuransi Jiwa Sinarmas MSIG Tbk.";
			
			String email_cab = uwDao.selectEmailCabangBankSinarmas(spaj);
			String email_ao = uwDao.selectEmailAoBankSinarmas(spaj);
			if(email_cab == null) email_cab = "";
			if(email_ao == null) email_ao = "";
			emailto = new String[]{email_cab+";"+email_ao};
			emailcc = new String[]{"alif_bam@sinarmasmsiglife.co.id;rizkiyano@sinarmasmsiglife.co.id;Sylvia@sinarmasmsiglife.co.id;Retno@sinarmasmsiglife.co.id;" +
								   "Liana@sinarmasmsiglife.co.id;ningrum@sinarmasmsiglife.co.id;Iriana@sinarmasmsiglife.co.id"};
		}
		
		try {
			EmailPool.send("SMiLe E-Lions", 1, 0, 0, 0, 
					null, 0, 0, nowDate, null, 
					true, currentUser.getEmail(), 
					emailto, 
					emailcc, 
					new String[]{"deddy@sinarmasmsiglife.co.id"}, 
					subject, 
					message, 
					null, spaj);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("ERROR :", e);
			return false;
		}
		
	}
	
	/**
	 * Validasi double input di PAS BP & DBD AGENCY
	 * 
	 * @author Daru
	 * @since Feb 28, 2013
	 * 
	 */
	public List selectValidasiInputPasBp(String msag_id_pp, String msp_pas_nama_pp, String msp_full_name, Date msp_pas_beg_date){
		return uwDao.selectValidasiInputPasBp(msag_id_pp, msp_pas_nama_pp, msp_full_name, msp_pas_beg_date);
	}
	
	public List selectValidasiInputDbd(String msp_pas_nama_pp, String msp_full_name, Date msp_pas_beg_date){
		return uwDao.selectValidasiInputDbd(msp_pas_nama_pp, msp_full_name, msp_pas_beg_date);
	}
	
	public List select_dth(String spaj){
		return bacDao.select_dth(spaj);
	}
	
	public Cmdeditbac savingspajBacSimple(Object cmd,Errors err,String keterangan, User currentUser) throws ServletException,IOException,Exception
	{
	//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		if (keterangan.equals("input"))
		{
			return this.savingBac.insertspajBacSimple(cmd,currentUser);
		}else{
			return this.savingBac.editspajBacSimple(cmd,currentUser);
		}
	}
	
	/**
	 * Select jenis brosur untuk permintaan brosur
	 * 
	 * @author Canpri 
	 * @since Feb 4, 2013
	 * @param 
	 */
	public List<Map> selectJenisBrosur() {
		return basDao.selectJenisBrosur();
	}

	/**
	 * Query untuk menarik data yang sudah ada, atau untuk penyimpanan data baru 
	 * dari permintaan brosur
	 * @author Canpri
	 * @since Feb 04, 2013 (11:42:53 AM)
	 * @param msf_id
	 * @param lca_id
	 * @param lus_id
	 * @return
	 */
	public List<FormSpaj> selectFormBrosur(String msf_id, String lca_id, String lus_id, Integer jenis) {
		return basDao.selectFormBrosur(msf_id, lca_id, lus_id, jenis);
	}

	public String processFormBrosur(CommandControlSpaj cmd, User currentUser) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		if("save".equals(cmd.getSubmitMode())) {
			return basDao.processNewFormBrosur(cmd, currentUser);
		}else if("cancel".equals(cmd.getSubmitMode())) {
			return basDao.processCancelFormBrosur(cmd, currentUser);
		}else if("approve".equals(cmd.getSubmitMode())) {
			return basDao.processApprovalFormBrosur(cmd, currentUser);
		}else if("reject".equals(cmd.getSubmitMode())) {
			return basDao.processRejectFormBrosur(cmd, currentUser);
		}else if("send".equals(cmd.getSubmitMode())) {
			return basDao.processSendFormBrosur(cmd, currentUser);
		}else if("received".equals(cmd.getSubmitMode())) {
			return basDao.processReceiveFormBrosur(cmd, currentUser);
		}
		return "ERROR";
	}

	public List<Spaj> selectStokBrosur(Spaj brosur) {
		return basDao.selectStokBrosur(brosur);
	}

	public List<FormSpaj> selectDaftarFormBrosur(FormSpaj formBrosur) {
		return basDao.selectDaftarFormBrosur(formBrosur);
	}

	public List selectSearchFormBrosurExpress(String msfId){
		return basDao.selectSearchFormBrosurExpress(msfId);
	}

	public List selectSearchFormBrosurDetail(FormSpaj brosur){
		return basDao.selectSearchFormBrosurDetail(brosur);
	}

	/**
	 * Select data kuesioner agen by no_reg (mku_no_reg)
	 * 
	 * @author Canpri 
	 * @since Feb 26, 2013
	 * @param no_reg
	 */
	public List selectKuesionerByNoReg(String no_reg) {
		return bacDao.selectKuesionerByNoReg(no_reg);
	}
	
	//Anta - Memeriksa polis Stable Link yg dimiliki PP
	public List selectPunyaStableLinkAll(Date pp_dob, String pp_name, Date tt_dob, String tt_name) {
		pp_name = "%" + pp_name.replaceAll(" ", "%").toUpperCase().trim() + "%";
		tt_name = "%" + tt_name.replaceAll(" ", "%").toUpperCase().trim() + "%";
		List<Map> tmp = bacDao.selectPunyaStableLinkAll(pp_dob, pp_name, tt_dob, tt_name);
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
	
	//Anta - Memeriksa polis PowerSave dan StableSave yg dimiliki PP
	public List selectPunyaPowStabSaveAll(Date pp_dob, String pp_name, Date tt_dob, String tt_name) {
		pp_name = "%" + pp_name.replaceAll(" ", "%").toUpperCase().trim() + "%";
		tt_name = "%" + tt_name.replaceAll(" ", "%").toUpperCase().trim() + "%";
		List<Map> tmp = bacDao.selectPunyaPowStabSaveAll(pp_dob, pp_name, tt_dob, tt_name);
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

	//Anta - Untuk validasi total premi stabil link
	public Map selectPremiStableLink(String spaj){
		return bacDao.selectPremiStableLink(spaj);
	}
	//Anta - Untuk validasi total premi power save + stable save
	public Map selectPremiPowStabSave(String spaj){
		return bacDao.selectPremiPowStabSave(spaj);
	}
	
	public String selectNamaCabang(String id){
		return uwDao.selectNamaCabang(id);
	}
	
	//Anta - Untuk select Cover Letter
	public ArrayList<CoverLetter> selectPolisCoverLetter(String bdate, String edate, Integer stpolis, Integer larid,
			String lusid, Integer jalur_dist) {
		return Common.serializableList(uwDao.selectPolisCoverLetter(bdate, edate, stpolis, larid, lusid, jalur_dist));
	}
	
	public ArrayList selectReportCoverLetter(String tanggalAwal, String tanggalAkhir, Integer lar_id, Integer jalur_dist) {
		return Common.serializableList(uwDao.selectReportCoverLetter(tanggalAwal, tanggalAkhir, lar_id, jalur_dist));
	}
	
	public List<Map> selectCabCoverLetter(String spaj) {
		return uwDao.selectCabCoverLetter(spaj);
	}
	
	public HashMap selectAddrRegion(String lar_id) {
		return Common.serializableMap(uwDao.selectAddrRegion(lar_id));
	}
	
	public void updateCoverLetter(String nopol, String stpolis){
		uwDao.updateCoverLetter(nopol, stpolis);
	}
	
	public String selectTglCoverLetter(String nopol, String stpolis){
		return uwDao.selectTglCoverLetter(nopol, stpolis);
	}
	
	//  Anta - Untuk Last User Proses Cover Letter
	public String selectLusCoverLetterPositionSpaj(String reg_spaj){
		return commonDao.selectLusCoverLetterPositionSpaj(reg_spaj);
	}
	
	//Anta - Untuk data Dana Talangan Haji
	public List selectDetailDTH(String spaj) {
		return uwDao.selectDetailDTH(spaj);
	}
	public int selectFlagDTH(String spaj) {
		return uwDao.selectFlagDTH(spaj);
	}
	
	public List selectListJabodetabekBSM(){
		return uwDao.selectListJabodetabekBSM();
	}
	
	public String selectEmailCabangBankSinarmas(String spaj){
		return uwDao.selectEmailCabangBankSinarmas(spaj);
	}
	
	public String selectEmailAoBankSinarmas(String spaj){
		return uwDao.selectEmailAoBankSinarmas(spaj);
	}
	
	public Integer selectMaxSlink(String reg_spaj) {
		return uwDao.selectMaxSlink(reg_spaj);
	}

	public List<Hadiah> selectHadiah(String reg_spaj) {
		return basDao.selectHadiah(reg_spaj);
	}

	//select jenis hadiah PS berdasarkan Premi
	public List selectHadiahPS(Double premi) {
		return basDao.selectHadiahPS(premi);
	}

	//select lh_id berdasarkan premi untuk hadiah standart
	public Hadiah selectLh_id(Double premi) {
		return basDao.selectLh_id(premi);
	}

	//insert jenis hadiah power save
	public String saveJenisHadiahPS(String nama_hadiah, String premi, String standard, String aktif, String lus_id) {
		return basDao.saveJenisHadiahPS(nama_hadiah, premi, standard, aktif, lus_id);
	}

	//update jenis hadiah
	public String updateJenisHadiah(String lh_id, String lhc_id, String mode) {
		return basDao.updateJenisHadiah(lh_id, lhc_id, mode);
	}

	//Canpri select jatuh tempo power save yg mau 4 tahun
	public List selectReportJTPowersave4thn(String bdate, String edate) {
		return basDao.selectReportJTPowersave4thn(bdate, edate);
	}
	
	/**
	 * Untuk pencarian Simas Saving Plan
	 * @return List
	 * @author Daru
	 * @since Apr 15, 2013
	 */
	public List selectSearchSsp(String id, String nama, String no_kanwill, String no_rek){
		return basDao.selectSearchSsp(id, nama, no_kanwill, no_rek);
	}
	
	/**
	 * Untuk menampilkan data Simas Saving Plan
	 * @param id
	 * @return Map
	 * @author Daru
	 * @since Apr 15, 2013
	 */
	public Map selectViewSsp(String id){
		return basDao.selectViewSsp(id);
	}
	
	/**
	 * Untuk menampilkan data billing Simas Saving Plan
	 * @param id
	 * @return List
	 * @author Daru
	 * @since Apr 16, 2013
	 */
	public List selectViewSspBill(String id){
		return basDao.selectViewSspBill(id);
	}
	
	/**
	 * Untuk menampilkan data Simas Saving Plan yg ingin dibatalkan
	 * @param id
	 * @return Map
	 * @author Daru
	 * @since Apr 16, 2013
	 */
	public Map selectViewBatalSsp(String id){
		return basDao.selectViewBatalSsp(id);
	}
	
	/**
	 * Untuk update proses batal Simas Saving Plan
	 * @param id
	 * @param lssp_id
	 * @param lspd_id
	 * @param mcp_flag_bill
	 * @param mcp_tgl_batal
	 * @param mcp_alasan
	 * @return boolean
	 * @author Daru
	 * @since Apr 17, 2013
	 */
	public boolean updateBatalSsp(String id, Integer lssp_id, Integer lspd_id, Integer mcp_flag_bill, Date mcp_tgl_batal, String mcp_alasan, String mcp_note, Integer lus_id){
		return basDao.updateBatalSsp(id, lssp_id, lspd_id, mcp_flag_bill, mcp_tgl_batal, mcp_alasan, mcp_note, lus_id);
	}
	
	public List selectReportSuratReins(String spaj, String noreins) {
		return uwDao.selectReportSuratReins(spaj,noreins);
	}
	
	//Canpri select jatuh tempo power save per cabang
	public List selectReport_Powersave(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id, String msag_id) {
		return this.basDao.selectReport_Powersave(lca, lwk, lsrg, bdate, edate, lus_id, msag_id);
	}
	
	//Canpri select jatuh tempo stable link per cabang
	public List selectReport_Stablelink(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id, String msag_id) {
		return this.basDao.selectReport_Stablelink(lca, lwk, lsrg, bdate, edate, lus_id, msag_id);
	}
	
	public Double selectCheckTotalUsedMstDrek(String noTrx, String payment_id, String no_spaj, Integer no_ke) {
		return uwDao.selectCheckTotalUsedMstDrek(noTrx, payment_id, no_spaj, no_ke);
	}
	//Canpri report produksi mingguan
	public List selectReport_Produksi_Mingguan(String bdate, String edate, String lus_id) {
		return this.basDao.selectReport_Produksi_Mingguan(bdate, edate, lus_id);
	}

	public String deleteHadiahBAC(Hadiah hadiah) {
		return this.basDao.deleteHadiahBAC(hadiah);
	}

	public String saveHadiahBAC(Hadiah hadiah) {
		return this.basDao.saveHadiahBAC(hadiah);
	}

	public Broker select_detilbroker(String spaj){
		Broker a = this.bacDao.select_detilbroker(spaj);
		if(a == null) return new Broker(); else return a;
	}	

	/**Fungsi:	semua transaksi  PENARIKAN / PENCAIRAN  SEBAGIAN   yang memenuhi salah satu  kriteria ini :
	 * 			- polis yang "pekerjaan" PP atau Tertanggung  nya adalah PNS, pejabat negara, jaksa, hakim, pengacara,/advokat, akuntan/konsultan keuangan, TNI, POLRI, PELAJAR, IBU RUMAH TANGGA
	 * 			- total dana yang dibayarkan ke klien > =  Rp. 100 juta 
	 * @param 	dariTanggal
	 * @param 	sampaiTanggal
	 * @return	List
	 * @author 	Andhika
	 */
	public List selectKYCpencairan_main(String dariTanggal,String sampaiTanggal){
		return uwDao.selectKYCpencairan_main(dariTanggal,sampaiTanggal);
	}
	
	public void prosesAksepEndors(Integer aksep_uw, String spaj, User currentUser, String keter)
	{	
		Date ldt_now = commonDao.selectSysdate();
		Pemegang2 pmg=basDao.selectpemegangpolis(spaj);
		Tertanggung ttg =selectttg(spaj);
		
		uwDao.updateAksepEndors(aksep_uw, spaj, currentUser.getLus_id());
		
		uwDao.insertMstPositionSpaj(currentUser.getLus_id(), keter , spaj, 0);
		
		insertLst_ulangan(spaj, ldt_now, "AKSEPTASI ENDORS BY UW", pmg.getLssp_id(), currentUser.getLus_id(), keter);
		
		String mi_id = "", statusAksep = "";
		HashMap inbox = null;
		Integer lspd_id_from= 0, lspd_id_from2 = 0; 
		List <Map> mapInbox = uwDao.selectMstInbox(spaj, "5");
		if(aksep_uw==2)	statusAksep = "ENDORSMENT DITERIMA OLEH UNDERWRITING";
		if(aksep_uw==3)	statusAksep = "ENDORSMENT DITOLAK OLEH UNDERWRITING";	
		if(mapInbox.size()>0){
			for(int i=0;i<mapInbox.size();i++){
				inbox = (HashMap) mapInbox.get(i);
				mi_id = (String) inbox.get("MI_ID");
				lspd_id_from = ((BigDecimal) inbox.get("LSPD_ID")).intValue();
				lspd_id_from2 = ((BigDecimal) inbox.get("LSPD_ID_FROM")).intValue();
				uwDao.updateMstInboxLspdId(mi_id, lspd_id_from2, lspd_id_from, 1, null, null, null,statusAksep, 0);
				MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id_from2, null, null, statusAksep, Integer.parseInt(currentUser.getLus_id()), ldt_now, null,0,0);
				uwDao.insertMstInboxHist(mstInboxHist);					
			}
		}
	}
	
	public List selectDaftarSPAJ_aksepEndors(String posisi, int tipe, Integer lssaId, Integer jenisTerbit){
		return this.uwDao.selectDaftarSPAJ_aksepEndors(posisi, tipe, lssaId, jenisTerbit);
	}
	
	public Integer selectMsen_aksep_uw(String spaj){
		return this.uwDao.selectMsen_aksep_uw(spaj);
	}
	
	public Map selectMsen_aksep_uw_new(String spaj){
		return uwDao.selectMsen_aksep_uw_new(spaj);
	}
	
	public String selectKete(String spaj){
		return this.uwDao.selectKete(spaj);
	}
	
	public Endors selectEndorsNew(String spaj) {
		return (Endors) this.uwDao.selectEndorsNew(spaj);
	}
	
	public List selectFilterSpajEndorsment(String posisi, String tipe, String kata, String pilter, String lssaId,String lsspId) {
		return this.uwDao.selectFilterSpajEndorsment(posisi, tipe, kata, pilter,lssaId,lsspId);
	}	
	/**
	 * req : novie
	 * @author 	Andhika
	 */
	public List selectLsKycPencairan(List KYCpencairan){
		List lsKycPencairan=new ArrayList();
		int  row=0;
		for(int i=0;i<KYCpencairan.size();i++){
			PencairanCase liquid =(PencairanCase)KYCpencairan.get(i);
//			double total=liquid.getTotal_tu().doubleValue()+liquid.getPremi_pokok().doubleValue();
			//cek daftar pekerjaan,, apakah termasuk dalam daftar High Risk Customer (HCR)
			int hrcFilter=selectCountLstHighRiskCust(liquid.getMpn_job_desc(),liquid.getMkl_kerja(), liquid.getMkl_industri());

//			List Tu_x =new ArrayList();
//			
//			Tu_x = uwDao.selectKe(liquid.getReg_spaj());
//			logger.info(Tu_x.size());
//			for(int z=0; z<Tu_x.size(); z++){
//				if(total>=batasan.doubleValue() || hrcFilter>0){//pengecekan apakah total(premi + top up lebih besar dari 100jt ato masuk daftar HRC)
				if(hrcFilter>0){
					liquid.setFlagKyc(0);
					liquid.setRow(row);
					User user= uwDao.selectMstpositionSpajUserAccept(liquid.getReg_spaj());
					if(user!=null)
						liquid.setLus_full_name(user.getLus_full_name());
					row++;
					lsKycPencairan.add(liquid);
//				}
			}
		}
		return lsKycPencairan;
	}	
	
	public String selectIDinbox(String spaj) {
		return uwDao.selectIDinbox(spaj);
	}
	//MANTA - Untuk Pengiriman Invoice Payroll
    public List<Map> selectInvoicePayroll(String customer, String tgl1, String tgl2)
    {
        return worksiteDao.selectInvoicePayroll(customer, tgl1, tgl2);
    }
	/**
	 * req : Novie
	 * @author 	Andhika
	 */
	public void schedulerFollowUpValidPolis(){
		String msh_name = "SCHEDULER FOLLOW UP POLIS NEW STABIL LINK";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerFollowUpValidPolis(msh_name);
		}
	}
    //canpri - untuk list polis klaim kesehatan di menu Akum New
	public List selectPolisKlaimKesehatan(String tipe, String operator, String nama, String tglLahir) {
		return this.uwDao.selectPolisKlaimKesehatan(tipe, operator, nama, tglLahir);
	}
	//canpri - untuk list polis attention di menu Akum New
	public List selectAttentionList(String tipe, String operator, String nama, String tglLahir) {
		return this.uwDao.selectAttentionList(tipe, operator, nama, tglLahir);
	}
	//ryan , proses copy scan.
	public Integer selectProsesUploadScanExist (String reg_spaj, String spajlama, String[] copy,User currentUser, String dir,String dir2, String spajProdukLama){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		try{
            DateFormat df = new SimpleDateFormat("yyyy");
			
			Date nowDate = commonDao.selectSysdate();
			String year =  df.format(nowDate);
			String lde_id=currentUser.getLde_id();
			String lca_id=reg_spaj.substring(0, 2);
			String mi_id ="";
			String mi_idlama="";
			Integer ljj_id=null;
			String from = props .getProperty("admin.ajsjava");
			String sekuens = this.uwDao.selectGetCounter(119, "01");
			String maxMiId = FormatString.rpad("0", sekuens, 6);
			if("09,58".indexOf(lca_id.toString()) <0){
				
				if (spajProdukLama!=null){
					ljj_id = 25;//Beli produk Baru
				}else {
					ljj_id = 21;//New businness
				}
				
				List<Map> mapInboxUw = uwDao.selectMstInbox(reg_spaj,"1");
				HashMap inbox =(HashMap) mapInboxUw.get(0);
				Map inboxExisting = new HashMap();
				Integer checkpositionUw = uwDao.selectInboxCheckingLspdId(reg_spaj, 202);
				Integer checkpositionImaging = uwDao.selectInboxCheckingLspdId(reg_spaj, 201);
				if(inbox==null){//checking inbox dgn posisi masih di admin, apakah sebelumnya belum pernah ada.
					if(checkpositionImaging<=0 || checkpositionUw<=0){//jika sebelumnya belum pernah diinsert ke inbox, create baru.
						this.uwDao.updateCounter(sekuens, 119, "01");
						mi_id = year+maxMiId;
					}else{//jika sudah pernah diinsert ke inbox di lspd_id apapun, ambil ID tersebut.
						inboxExisting = uwDao.selectMstInboxExisting(reg_spaj);
						mi_id = (String) inboxExisting.get("MI_ID");
					}
					
				}else{
					mi_id=(String) inbox.get("MI_ID");
				}
				
				MstInbox mstInbox = new MstInbox(mi_id, ljj_id, 202, null, 207, 
						null, null, reg_spaj, null, 
						null, 1, nowDate, null, 
						null, null, Integer.parseInt(currentUser.getLus_id()), nowDate, null, null, 1, null, null, null, null, null, null, null,null, 0,null);
				
				MstInboxHist mstInboxHist = new MstInboxHist(mi_id, 207, 202, null, null, null, Integer.parseInt(currentUser.getLus_id()), nowDate, null,0,0);
				if(inbox==null){
					if(checkpositionImaging<=0 || checkpositionUw<=0){
						uwDao.insertMstInbox(mstInbox);
					}else{
						mstInbox = new MstInbox(mi_id, ljj_id, (Integer)inboxExisting.get("LSPD_ID"), null, (Integer)inboxExisting.get("LSPD_ID_FROM"), 
								null, null, reg_spaj, null, 
								null, 1, nowDate, null, 
								null, null, Integer.parseInt(currentUser.getLus_id()), nowDate, null, null, 1, null, null, null, null, null, null, null,null, 0,null);
						mstInboxHist = new MstInboxHist(mi_id, (Integer)inboxExisting.get("LSPD_ID_FROM"), (Integer)inboxExisting.get("LSPD_ID"), null, null, null, Integer.parseInt(currentUser.getLus_id()), nowDate, null,0,0);
						uwDao.updateMstInbox(mstInbox);
					}
					
				}else{
					uwDao.updateMstInbox(mstInbox);
				}
				uwDao.insertMstInboxHist(mstInboxHist);
				List<Map> inbox2 = uwDao.selectMstInbox(spajlama, "1");
				if(inbox2!=null){
					//update spaj lama
					MstInbox mstInbox2 = new MstInbox(mi_id, 25, null, null, 208, 
							null, null, spajlama, null, 
							null, 2, nowDate, null, 
							null, null, Integer.parseInt(currentUser.getLus_id()), nowDate, null, null, 1, null, null, null, null, null, null, null,null, 0,null);
					uwDao.updateMstInbox(mstInbox2);
				}
			}
			
			List<Scan> daftarScan =null;
			Integer lsbsId =null;
			Map mDataUsulan=selectDataUsulan(reg_spaj);
			if(mDataUsulan!=null){
				lsbsId=(Integer)mDataUsulan.get("LSBS_ID");
			}
			if (currentUser.getJn_bank()==2){
				daftarScan = selectLstScan("BB",null);
			}else if (currentUser.getCab_bank().equals("") && (lsbsId==142 || lsbsId==163))
			{
				daftarScan =selectLstScan("BB",null);
			}else{
				daftarScan =selectLstScan("UW",null);
			}
			//Insert ke Mst_scan
			for(int i=0;i<copy.length;i++){
				
			String lh_id = copy[i];
			String lh_id2 = lh_id.replace(spajlama, reg_spaj);
			
			File dataLama = new File(dir2 + "//" +spajlama+"//"+ lh_id);
			
			String tDest_pic2 = dir + "//" +reg_spaj+"//";
			String tDest_pic = dir2 + "//" +spajlama+"//";
			File destDir_pic = new File(tDest_pic);
			File destDir_pic2 = new File(tDest_pic2);
			
			if(!destDir_pic.exists()) {
				destDir_pic.mkdirs();
			}
			
			if(!destDir_pic2.exists()) {
				destDir_pic2.mkdirs();
			}
			
			File databaru =new File(dir + "//" +reg_spaj+"//"+ lh_id2);
			FileUtils.copy(dataLama, databaru);
			
		    Scan scan = new Scan();
		    scan.setLus_id(currentUser.getLus_id());
		    scan.setLde_id(currentUser.getLde_id());
		    scan.setNo_indek(reg_spaj);
		    scan.setTgl_input(nowDate);
		    scan.setFiles_ad(dir+"/"+reg_spaj+"/"+ lh_id2);
		    scan.setTipe_file(null);
		    scan.setJml_page(0);
		    scan.setFlag_aktif(1);
		    scan.setJenis(2);//0 : program upload seno, 1 program SNOWS rudy, 2 program E-Lions deddy, 3 program E-Lion Pengajuan Biaya Ridhaal
		    
		    //get Tipe File
		    for(int j=0;j<daftarScan.size();j++){
		    	Scan a = daftarScan.get(j);
		    	if(lh_id2.indexOf(a.getNmfile()) != -1){
		    		scan.setTipe_file(a.getNmfile());
		    	}
		    }

		    boolean a=true;
			    while(a) {
			    	try{
				   		scan.setKd_scan(Integer.parseInt(sequence.sequenceKdScan()) );
					    uwDao.insertMstScan(scan);
					    a=false;
				    }catch (Exception e) {
						// TODO: handle exception
					}
		    	}
			}
			updateUploadScan(reg_spaj, 1);
			uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Copy Scan NB ", reg_spaj, 0);
			return 1;
		}catch (Exception e){
			logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
	}
		
	public String selectMstInboxNoReff (String blanko){
		return uwDao.selectMstInboxNoReff(blanko);
	}
	
	public String selectMstInboxMiUrl (String mi_id){
		return uwDao.selectMstInboxMiUrl(mi_id);
	}
	
	public Integer selectCountCancel(String spaj){
		return uwDao.selectCountCancel(spaj);
	}
	
	public Endors selectMstEndors(String spaj, String lspd_id, String msen_flag_issue){
		return bacDao.selectMstEndors(spaj, lspd_id, msen_flag_issue);
	}
	//Report Claim kesehatan
	public List selectreportDetailHealthClaim(String bdate, String edate, String lus_id, String jenis_periode) {
		return this.uwDao.selectreportDetailHealthClaim(bdate, edate, lus_id, jenis_periode);
	}

	public List selectreportHealthClaimBasedEntryAge(String bdate, String edate, String lus_id, String jenis_periode) {
		return this.uwDao.selectreportHealthClaimBasedEntryAge(bdate, edate, lus_id, jenis_periode);
	}

	public List selectreportHealthClaimByMedis(String bdate, String edate, String lus_id, String jenis_periode) {
		return this.uwDao.selectreportHealthClaimByMedis(bdate, edate, lus_id, jenis_periode);
	}

	public List selectGetTotalHealthClaimByMedis(String bdate, String edate,	String lus_id, String medis, String jenis_periode) {
		return this.uwDao.selectGetTotalHealthClaimByMedis(bdate, edate, lus_id, medis, jenis_periode);
	}

	public List selectreportHealthClaimBasedDurationPolicy(String bdate, String edate, String lus_id, String jenis_periode) {
		return this.uwDao.selectreportHealthClaimBasedDurationPolicy(bdate, edate, lus_id, jenis_periode);
	}

	public List selectreportHealthClaimByBranch(String bdate, String edate, String lus_id, String jenis_periode) {
		return this.uwDao.selectreportHealthClaimByBranch(bdate, edate, lus_id, jenis_periode);
	}

	public List selectreportHealthClaimByCOD(String bdate, String edate,	String lus_id, String jenis_periode) {
		return this.uwDao.selectreportHealthClaimByCOD(bdate, edate, lus_id, jenis_periode);
	}

	public Integer selectreportHealthClaimByCOD_TotalCase(String bdate, String edate, String lus_id, String jenis_periode) {
		return this.uwDao.selectreportHealthClaimByCOD_TotalCase(bdate, edate, lus_id, jenis_periode);
	}
	public List selectreportHealthClaimBasedAge(String bdate, String edate, String lus_id, String jenis_periode) {
		return this.uwDao.selectreportHealthClaimBasedAge(bdate, edate, lus_id, jenis_periode);
	}
	public List selectreportExGratiaHealthClaim(String bdate, String edate,	String lus_id, String jenis_periode) {
		return this.uwDao.selectreportExGratiaHealthClaim(bdate, edate, lus_id, jenis_periode);
	}
	public List selectreportClaimBySAKesehatan(String bdate, String edate, String lus_id, String jenis_periode) {
		return this.uwDao.selectreportClaimBySAKesehatan(bdate, edate, lus_id, jenis_periode);
	}
	public List selectreportHealthClaimByProduct(String bdate, String edate,	String lus_id, String jenis_periode) {
		return this.uwDao.selectreportHealthClaimByProduct(bdate, edate, lus_id, jenis_periode);
	}	
	public int selectLusSpecial(String lusid) {
		return uwDao.selectLusSpecial(lusid);
	}
	public List monitorPolisIssued(Map map){
		String dist,lsgb,provider;
		Date tanggalAwal, tanggalAkhir;
		dist=(String) map.get("dist");
		lsgb=(String)map.get("lsgb");
		provider=(String)map.get("provider");
		tanggalAwal = (Date)map.get("tanggalAwal");
		tanggalAkhir = (Date)map.get("tanggalAkhir");
		return basDao.monitorPolisIssued(dist, lsgb, provider, tanggalAwal, tanggalAkhir);
	}
	
	public void insertMstPrintHistory(String spaj, String print_desc, Integer page_count, String lus_id){
		this.uwDao.insertMstPrintHistory(spaj, print_desc, page_count, lus_id);
	}
	
	public void insertEmailUpdateStaus(String reg_spaj){
		Date nowDate = commonDao.selectSysdate();
		EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
				null, 0, 0, nowDate, null, 
				true, "ajsjava@sinarmasmsiglife.co.id", 
//				new String[]{"riyadi@sinarmasmsiglife.co.id"}, 
				new String[]{"rahmayanti@sinarmasmsiglife.co.id"}, 
				null,
				null, 
				"ADA UPDATE STATUS "+reg_spaj+" Jalur Distribusi : Agency",
				"Telah dilakukan Update tatus untuk SPAJ No."+reg_spaj+", Silakan dilakukan proses input TTP", 
				null, reg_spaj);
		
	}
	
	public HashMap<String, Object> selectDetailKartuPas(String no_kartu) {
		return uwDao.selectDetailKartuPas(no_kartu);
	}
	
	public Integer select_panj_rek1(String query) throws DataAccessException {
		return bacDao.select_panj_rek1(query);
	}
	
	public HashMap<String, Object> selectKotaBank(String lbn_id) {
		return uwDao.selectKotaBank(lbn_id);
	}
	
	public Integer kirimEmailPermohonanSPT(String reg_spaj, User currentUser,
			String fileName, String destSPT, String fileName2, Integer flagProses,
			String subjek) {		
		return uwDao.kirimEmailPermohonanSPT(reg_spaj,currentUser,fileName,destSPT,fileName2,flagProses,subjek);
	}
	
	public List<Map> selectProductionBSIMInBranch(Map params){
		return uwDao.selectProductionBSIMInBranch(params);
	}
}