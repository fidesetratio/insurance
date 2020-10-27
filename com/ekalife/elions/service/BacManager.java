package com.ekalife.elions.service;
 
import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;
import id.co.sinarmaslife.std.util.PDFToImage;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Encoder;

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
import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.AddressRegion;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Akseptasi;
import com.ekalife.elions.model.AksesHist;
import com.ekalife.elions.model.App;
import com.ekalife.elions.model.AutoPaymentVA;
import com.ekalife.elions.model.Begdate;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.BiayaUlink;
import com.ekalife.elions.model.Billing;
import com.ekalife.elions.model.Client;
import com.ekalife.elions.model.CmdAutoPaymentVA;
import com.ekalife.elions.model.CmdCoverLetter;
import com.ekalife.elions.model.CmdPromo;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.CommandChecklist;
import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.Comment;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.CoverLetter;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DepositPremium;
import com.ekalife.elions.model.DetBilling;
import com.ekalife.elions.model.DetEndors;
import com.ekalife.elions.model.DetUlink;
import com.ekalife.elions.model.DrekDet;
import com.ekalife.elions.model.Endors;
import com.ekalife.elions.model.Followup;
import com.ekalife.elions.model.FormSpaj;
import com.ekalife.elions.model.Icd;
import com.ekalife.elions.model.Insured;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.KartuNama;
import com.ekalife.elions.model.Linkdetail;
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.Meterai;
import com.ekalife.elions.model.MstInbox;
import com.ekalife.elions.model.MstInboxChecklist;
import com.ekalife.elions.model.MstInboxDet;
import com.ekalife.elions.model.MstInboxHist;
import com.ekalife.elions.model.MstOfacScreeningResult;
import com.ekalife.elions.model.MstQuestionAnswer;
import com.ekalife.elions.model.MstTransUlink;
import com.ekalife.elions.model.OfacSertifikat;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Payment;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Pemegang2;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.PesertaPlus_mix;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Position;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.PowersaveCair;
import com.ekalife.elions.model.Premi;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.ProductInsEnd;
import com.ekalife.elions.model.Production;
import com.ekalife.elions.model.Promo;
import com.ekalife.elions.model.Reas;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Scan;
import com.ekalife.elions.model.Simcard;
import com.ekalife.elions.model.Stamp;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.TmSales;
import com.ekalife.elions.model.TopUp;
import com.ekalife.elions.model.TransUlink;
import com.ekalife.elions.model.Transfer;
import com.ekalife.elions.model.Ulangan;
import com.ekalife.elions.model.Ulink;
import com.ekalife.elions.model.UlinkBill;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.Utama;
import com.ekalife.elions.model.VirtualAccount;
import com.ekalife.elions.model.VoucherTaxi;
import com.ekalife.elions.model.sms.Smsserver_in;
import com.ekalife.elions.model.sms.Smsserver_out;
import com.ekalife.elions.model.sms.Smsserver_out_hist;
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
import com.ekalife.elions.process.SavingBacSpajNew;
import com.ekalife.elions.process.SavingBeaMeterai;
import com.ekalife.elions.process.SavingRekruitment;
import com.ekalife.elions.process.Sequence;
import com.ekalife.elions.process.SuratUnitLink;
import com.ekalife.elions.process.TransferPolis;
import com.ekalife.elions.process.UploadBac;
import com.ekalife.elions.process.UploadSpajTemp;
import com.ekalife.elions.process.upload.InputDataTemp;
import com.ekalife.elions.process.upload.UploadSetDataTemp;
import com.ekalife.elions.process.upload.ValidateUploadTM;
import com.ekalife.elions.process.uw.ReasIndividu;
import com.ekalife.elions.process.uw.ReasUtilities;
import com.ekalife.elions.process.uw.Reinstate;
import com.ekalife.elions.process.uw.Simultan;
import com.ekalife.elions.process.uw.TransferUw;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.elions.web.refund.vo.MstPtcTmVO;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;
import com.ekalife.elions.web.refund.vo.RefundCentrix;
import com.ekalife.elions.web.refund.vo.RefundDbVO;
import com.ekalife.elions.web.uw.PrintPolisMultiController;
import com.ekalife.elions.web.uw.PrintPolisPrintingController;
import com.ekalife.utils.AngkaTerbilang;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.ExcelRead;
import com.ekalife.utils.ExternalDatabase;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.ITextPdf;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.MergePDF;
import com.ekalife.utils.PdfUtils;
import com.ekalife.utils.Print;
import com.ekalife.utils.PrintPolisPerjanjianAgent;
import com.ekalife.utils.Products;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.view.PDFViewer;
import com.google.gson.Gson;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

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
public class BacManager {
	protected final Log logger = LogFactory.getLog( getClass() );
	private Email email;
	private Products products;
	private DecimalFormat f3= new DecimalFormat ("000");
	private DateFormat defaultDateFormat;
	private DateFormat defaultDateFormatStripes;

	/* ======================= Proses2 (Pada dasarnya, sama seperti DAO juga) ======================= */
	private AjaxManager ajaxManager;
	private UwManager uwManager;
	private ElionsManager elionsManager;
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
	private SavingBacSpajNew savingBacSpajNew;
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
	private UploadSpajTemp uploadSpajTemp;
	private ValidateUploadTM validateUploadTM;
	private InputDataTemp inputDataTemp;
	private Print print;
	//private PrintPolisMultiController printPolisMultiController;
	
	//untuk setingan google URL Shortener API
	private static final String GOOGLE_URL_SHORT_API = "https://www.googleapis.com/urlshortener/v1/url";
	private static final String GOOGLE_API_KEY = "AIzaSyC_oEMl-p_f0M3pB-9mlV3ZSsgFvvOsFOc";
		

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
	private UploadSetDataTemp uploadSetDataTemp;
	public void setSchedulerDao(SchedulerDao schedulerDao) {this.schedulerDao = schedulerDao;}

	/* ======================= Getter-Setter ======================= */
	
	public Properties getProps() {return this.props;}
	public void setSnowsDao(SnowsDao snowsDao) {this.snowsDao = snowsDao;}

	public void setProps(Properties props) {this.props = props;}
	public void setCancelPolis(CancelPolis cancelPolis) {this.cancelPolis = cancelPolis;}
	public void setKomisi(Komisi komisi) {this.komisi = komisi;}

	public void setEndorsement(Endorsement endorsement) {this.endorsement = endorsement;}
	public void setManfaat(Manfaat manfaat) {this.manfaat = manfaat;}
	public void setNab(Nab nab) {this.nab = nab;}
	public Print getPrint() {return print;}
	public void setPrint(Print print) {this.print = print;}

	public void setSavingBacSpajNew(SavingBacSpajNew savingBacSpajNew) {
		this.savingBacSpajNew = savingBacSpajNew;
	}
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
	public void setUploadSpajTemp(UploadSpajTemp uploadSpajTemp) {this.uploadSpajTemp = uploadSpajTemp;}	
	public void setValidateUploadTM(ValidateUploadTM validateTM) {this.validateUploadTM = validateUploadTM;}
	public void setInputDataTemp(InputDataTemp inputDataTemp) {this.inputDataTemp = inputDataTemp;}
	public void setUploadSetDataTemp(UploadSetDataTemp uploadSetDataTemp) {this.uploadSetDataTemp =  uploadSetDataTemp;}
//	public void setUwManager(UwManager uwManager) {this.uwManager = uwManager;}
//	public void setBacManager(BacManager bacManager) {this.elionsManager = elionsManager;}
	
	/* ======================= Functions ======================= */
	
	public String updateTanggalPertanggungjawaban(String kodeAgent, String noBlanko, String tanggal) {
    	return basDao.updateTanggalPertanggungjawaban(kodeAgent,noBlanko,tanggal);
    }
    
    public String selectLusFullName( BigDecimal lusId ){
		return this.refundDao.selectLusFullName( lusId );
	}
	
	public Date selectSysdate(){
		return (Date) commonDao.selectSysdate();
	}

	public List selectReportSummaryInfoDanaSejahteraBSM(String bdate, String edate) {
		return uwDao.selectReportSummaryInfoDanaSejahteraBSM(bdate, edate);
	}
	
	public Map ProsesAutoAccept(String no_reg, Integer flag_reg, Pemegang pmg, Tertanggung ttg, User user, HttpServletRequest request, ElionsManager elionsManager2) throws Exception{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Policy policy = new Policy();
		
		Map map = new HashMap();
		
		Integer lsbs_id = Integer.parseInt(uwDao.selectLsbsId(no_reg));
		Integer lsdbs_number = Integer.parseInt(uwDao.selectLsdbsNumber(no_reg));
		String lsbs_3digit = FormatString.rpad("0", lsbs_id.toString(), 3);
		
		try{
			//Khusus BTN,DKI,JATIM, AUTODEBET PREMI PERTAMA
			if((pmg.getLca_id().equals("40") && pmg.getLwk_id().equals("01") && pmg.getLsrg_id().equals("04"))
					|| (pmg.getLca_id().equals("40") && pmg.getLwk_id().equals("01") && pmg.getLsrg_id().equals("08"))
					|| (pmg.getLca_id().equals("40") && pmg.getLwk_id().equals("01") && pmg.getLsrg_id().equals("11"))
					|| (pmg.getLca_id().equals("40") && pmg.getLwk_id().equals("01") && pmg.getLsrg_id().equals("12"))
					|| (pmg.getLca_id().equals("40") && pmg.getLwk_id().equals("01") && pmg.getLsrg_id().equals("13"))
					){
				Integer setNopol = 0;
				Akseptasi akseptasi = new Akseptasi();
				akseptasi.setSpaj(no_reg);
				akseptasi.setLsbsId(Integer.parseInt(uwDao.selectLsbsId(no_reg)));
				akseptasi.setLsdbsNumber(Integer.parseInt(uwDao.selectLsdbsNumber(no_reg)));
				akseptasi.setLcaId(pmg.getLca_id());
				setNopol = uwDao.wf_set_nopol(akseptasi, 1);
				//Rollback dan tampilkan message
				if(setNopol>0){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					map.put("success", pmg.getReg_spaj()+" "+"GAGAL DI TRANSFER KE Proses AUTODEBET FINANCE");	
				}
				policy = uwDao.selectDw1Underwriting(akseptasi.getSpaj(), 1, 1);
				List lsDp = uwDao.selectMstDepositPremium(akseptasi.getSpaj(), new Integer(1));
				BindException err = null;
				if(uwDao.wf_ins_bill(no_reg, 1, new Integer(1), akseptasi.getLsbsId(), akseptasi.getLsdbsNumber(), 56, 1, lsDp, user.getLus_id(), policy, err)){			
					uwDao.updateMstInsured(no_reg,118);
					uwDao.updateMstPolicy(no_reg,118);			
					uwDao.insertMstPositionSpaj(user.getLus_id(), "Transfer Proses AutoDebet Ke Finance", no_reg, 0);
					uwDao.saveMstTransHistory(no_reg, "tgl_transfer_autodebet_nb", null, null, null);
					
//					if(pmg.getLsrg_id().equals("04") || pmg.getLsrg_id().equals("11")){
//						prosesSMSAkseptasi(no_reg);
//					}
				}
				map.put("success", pmg.getReg_spaj()+" "+"Transfer Proses AutoDebet Ke Finance");
				
			}else{
				if(lsbs_id!=169){
					Map mReffBii = uwDao.selectReferrerBII(no_reg);
					 //Khusus internal , external (redbery,arco,ssp,dll) & HCP + tidak dicek.
					if(mReffBii==null && ( pmg.getLca_id().equals("40") && pmg.getLwk_id().equals("01") && ("01,02,03,07".indexOf(pmg.getLsrg_id())>-1) ) && lsbs_id!=195){
						map.put("success", pmg.getReg_spaj()+" "+" Data Referral Kosong, Mohon Dicek");
					}else{
						Datausulan dataUsulan = bacDao.selectDataUsulanutama(no_reg);
						List lsSimultanPp, lsSimultanTt;
						String idSimultanPp = "", idSimultanTt = "";
						
						//Data pemegang
						String mclIdPp = pmg.getMcl_id();
						String mclFirstPp = pmg.getMcl_first();
						String sDateBirthPp = df.format(pmg.getMspe_date_birth());
						String mspeMotherPp = pmg.getMspe_mother();

						//Data tertanggung
						String mclIdTt = ttg.getMcl_id();
						String mclFirstTt = ttg.getMcl_first();
						String sDateBirthTt = df.format(ttg.getMspe_date_birth());
						String mspeMotherTt = ttg.getMspe_mother();

						Integer spasi, titik, koma;
						//Pemegang ambil nama depan saja 
						spasi = mclFirstPp.indexOf(' ');
						titik = mclFirstPp.indexOf('.');
						koma = mclFirstPp.indexOf(',');
						if(spasi>0)
							mclFirstPp = mclFirstPp.substring(0, spasi);
						else if(titik>0)
							mclFirstPp = mclFirstPp.substring(0, titik);
						else if(koma>0)
							mclFirstPp = mclFirstPp.substring(0, koma);
						
						//Tertanggung ambil nama depan saja 
						spasi = mclFirstTt.indexOf(' ');
						titik = mclFirstTt.indexOf('.');
						koma = mclFirstTt.indexOf(',');
						if(spasi>0)
							mclFirstTt = mclFirstTt.substring(0, spasi);
						else if(titik>0)
							mclFirstTt = mclFirstTt.substring(0, titik);
						else if(koma>0)
							mclFirstTt = mclFirstTt.substring(0, koma);

						//Set id simultan
						Map param = new HashMap();
						param.put("mcl_id", mclIdPp);
						param.put("nama", mclFirstPp);
						param.put("tgl_lhr", sDateBirthPp);
						param.put("mspe_mother", mspeMotherPp);
						lsSimultanPp = uwDao.selectSimultanNew(param);
						Map mapPp = (HashMap) lsSimultanPp.get(0);
						idSimultanPp = (String) mapPp.get("ID_SIMULTAN");
						
						Map param2 = new HashMap(); 
						param2.put("mcl_id", mclIdTt);
						param2.put("nama", mclFirstTt);
						param2.put("tgl_lhr", sDateBirthTt);
						param2.put("mspe_mother", mspeMotherTt);
						lsSimultanTt = uwDao.selectSimultanNew(param2);
						Map mapTT = (HashMap) lsSimultanTt.get(0);
						idSimultanTt = (String) mapTT.get("ID_SIMULTAN");
						
						//Cek id simultan
						//Untuk upload DMTM Clear/Non Clear ditentukan dari id simultan
						uwDao.updateMstInsured(no_reg, 27, 17, 1, null);
						uwDao.insertMstPositionSpaj(user.getLus_id(), "TRANSFER KE SPEEDY", no_reg, 0);
						uwDao.saveMstTransHistory(no_reg, "tgl_transfer_uw_speedy", null, null, null);
						uwDao.updateMstInsuredTgl(no_reg, 1, commonDao.selectSysdate(), 0);							
						uwDao.updateMstPolicy(no_reg, 27);	
						prosesSnows(no_reg, user.getLus_id(), 202, 212);
						boolean oke = true;					
						Integer clear = 0;
						clear = uwDao.selectCountCleanCaseInsured(no_reg);
						/**
						 * Patar Timotius
						 * 2018/08/08
						 */
				        boolean isDMTMSimasKidBSIM1 = dataUsulan.getLsbs_id()==208 && (dataUsulan.getLsdbs_number()>=33 && dataUsulan.getLsdbs_number()<=36);
					
						if(clear>0) oke = false;
						if(idSimultanPp!=null || idSimultanTt!=null) oke = false;
						
						//Jika non clear langsung transfer ke speedy
						if( oke && (("118,120,142,163,177,190,195,214,223,225".indexOf(lsbs_3digit)<0 &&
								!(dataUsulan.getLsbs_id()==189 && dataUsulan.getLsdbs_number()>=33 && dataUsulan.getLsdbs_number()<=47) &&
								!(dataUsulan.getLsbs_id()==189 && (dataUsulan.getLsdbs_number()>=48 && dataUsulan.getLsdbs_number()<=62)) && //helpdesk [133975] produk baru 189 48-62 Smile Medical Syariah BSIM
								!(dataUsulan.getLsbs_id()==200 && dataUsulan.getLsdbs_number()==7) && // ULTIMATE SYARIAH Langsung proses SPEEDY
								!(dataUsulan.getLsbs_id()==204 && dataUsulan.getLsdbs_number()>=37 && dataUsulan.getLsdbs_number()<=48) &&
								!(dataUsulan.getLsbs_id()==212 && (dataUsulan.getLsdbs_number()==6 || dataUsulan.getLsdbs_number()==8 || dataUsulan.getLsdbs_number()==9|| dataUsulan.getLsdbs_number()==14) )) && //add lsdbs_number 14 nana add smile proteksi 212-14 helpdesk 147672
								!(dataUsulan.getLsbs_id()==217 && dataUsulan.getLsdbs_number()==2) &&
								!(dataUsulan.getLsbs_id()==223 && dataUsulan.getLsdbs_number()==2) && //helpdesk [138638] produk baru SLP Syariah (223-2)
								!(dataUsulan.getLsbs_id()==226 && (dataUsulan.getLsdbs_number()>=1 && dataUsulan.getLsdbs_number()<=5)) && //helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
								!(isDMTMSimasKidBSIM1)
								) ||
								(dataUsulan.getLsbs_id()==183 && dataUsulan.getMspo_flag_spaj()!=4))  { 

							prosesEmailNotify(no_reg,pmg.getMcl_first(),0);	
							map.put("success", pmg.getReg_spaj()+" "+"Ditransfer ke SPEEDY");	

						}else{				
							if(!(dataUsulan.getLsbs_id()==223 && dataUsulan.getLsdbs_number()==1) && false){
								BindException err = null;
								map = ProsesParalel(1, no_reg, 1,pmg,ttg, dataUsulan, user, request, err, elionsManager, uwManager);
							}else{
								//Erbe package masuk kesini 217 2 (Ridhaal)
								
								//untuk produk ERBE akan di proses langsung ke Print Polis tanpa melewati Payment (belum ada Payment)
								//untuk produk lain (selain ERBE) proses normal ke Payment/UW
								String cabang = elionsManager2.selectCabangFromSpaj(no_reg);
								map = ProsesSpeedy(no_reg, 1, pmg, ttg, dataUsulan, user, request, elionsManager2 );
							}
							
							Integer i_flag_error = Integer.parseInt(map.get("flag_error").toString());
							if(i_flag_error>0){
								TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
								map.put("success", pmg.getReg_spaj()+" "+"GAGAL DI TRANSFER");	
							}else{
								map.put("success", pmg.getReg_spaj()+" "+"berhasil di transfer");
								prosesEmailNotify(no_reg,pmg.getMcl_first(),2);	
							}
						}	
					}
				}else{//eka waktu langsung transfer ke speedy
					uwDao.updateMstInsured(no_reg, 27);
					uwDao.insertMstPositionSpaj(user.getLus_id(), "TRANSFER KE SPEEDY", no_reg, 0);
					uwDao.saveMstTransHistory(no_reg, "tgl_transfer_uw_speedy", null, null, null);
					uwDao.updateMstInsuredTgl(no_reg, 1, commonDao.selectSysdate(), 0);							
					uwDao.updateMstPolicy(no_reg, 27);
					prosesSnows(no_reg, user.getLus_id(), 202, 212);
					prosesEmailNotify(no_reg, pmg.getMcl_first(), 0);	
					map.put("success", pmg.getReg_spaj()+" "+"ditransfer ke SPEEDY");	
				}	
			}
		}catch (Exception e) {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			map.put("success", pmg.getReg_spaj()+" "+"ERROR PROSES TRANSFER ");	
		}
		return map;
	}
	
	/**
	 * @param spaj
	 * helpdesk [133187], tambah kegiatan sms pada saat spaj inforce //chandra
	 */
	public void prosesSMSAkseptasi(String spaj){
		try{
			Pemegang pemegang = new Pemegang();
			pemegang = bacDao.selectpp(spaj);
			String polis_no = pemegang.getMspo_policy_no();
			String pesan = "Nasabah Yth, Polis No " + FormatString.nomorPolis(polis_no) + " sudah aktif. Premi lanjutan mendebet rekening tabungan anda. Info 021-50163977 atau kunjungi epolicy.sinarmasmsiglife.co.id.";
							
			Smsserver_out sms_out = new Smsserver_out();
			sms_out.setReg_spaj(spaj);
			sms_out.setMspo_policy_no(polis_no);
			sms_out.setJenis(21);
			sms_out.setLjs_id(21);
			sms_out.setRecipient(pemegang.getNo_hp() != null ? pemegang.getNo_hp() : pemegang.getNo_hp2());
			sms_out.setText(pesan);

			basDao.insertSmsServerOutWithGateway(sms_out, true);
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("kirim sms error [prosesSMSAkseptasi]");
		}
	}

	/**
	 * @since 26 Feb 2014
	 * @author 	Canpri
	 * @beans proses email untuk auto accept
	 */
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
							//true,"policy_service@sinarmasmsiglife.co.id", new String[] {"canpri@sinarmasmsiglife.co.id"} ,//test
							null, bcc,
//							new String[] {"ingrid@sinarmasmsiglife.co.id","Ariani@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id"},
//							new String[] {props.getProperty("admin.deddy")},  
							"Pemberitahuan Proses Pengajuan Polis", 
							"Nasabah Yth.</br>"+
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
	
	public List selectreportSummaryInputTotal(String lus_id, String bdate, String edate) {
		return this.basDao.selectreportSummaryInputTotal(lus_id, bdate, edate);
	}
	
	/**
	 * req : Inge (22/07/2013)
	 * @author 	Andhika
	 */
	public void schedulerAutomailSummaryFurther(){
		String msh_name = "AUTOMAIL REPORT SUMMARY FURTHER";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerAutomailSummaryFurther(msh_name);
		}
	}	
	
	/**
	 * Autoreport List SPAJ Posisi Waiting Process NB dan SPAJ Gagal Proses
	 * req : Titis (21/03/2016)
	 * @author 	Randy
	 */
	public void schedulerWaitingProses(){
		String msh_name = "AUTOMAIL REPORT SPAJ WAITING PROSES";
//		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerWaitingProses(msh_name);
//		}
	}

	/**
	 * Auto Reminder Smile Baby
	 * @author 	Randy (29-08-2016)
	 */
	public void schedulerReminderSmileBaby(){
		String msh_name = "REMINDER SMILE BABY";
			schedulerDao.schedulerReminderSmileBaby(msh_name);
	}
	
	/**
	 * view detail power save di menu follow up billing
	 * @author Canpri
	 * @since 15 Aug 2013
	 * @param reg_spaj
	 * @return List
	 * 
	 */
	public List selectDetailPowerSaveFU(String reg_spaj) {
		return basDao.selectDetailPowerSaveFU(reg_spaj);
	}

	/**
	 * view detail stable link di menu follow up billing
	 * @author Canpri
	 * @since 15 Aug 2013
	 * @param reg_spaj
	 * @return List
	 * 
	 */
	public Object selectDetailStableLinkFU(String reg_spaj) {
		return basDao.selectDetailStableLinkFU(reg_spaj);
	}

	/**
	 * view data kontes proklamasi
	 * @author Canpri
	 * @since 19 Aug 2013
	 * @param 
	 * @return List
	 * 
	 */
	public List selectDataKontesProklamasi() {
		return bacDao.selectDataKontesProklamasi();
	}
	
	/**
	 * Select History Cetak Konfirmasi Syariah
	 * @author Ryan
	 * @since 27 Aug 2013
	 * @param 
	 * @return Integer
	 * 
	 */
	public Integer selectCekKonfirmasiSyariah(String reg_spaj){
		return uwDao.selectCekKonfirmasiSyariah(reg_spaj);
	}

	/**
	 * view data kontes photo family
	 * @author Canpri
	 * @since 05 Sep 2013
	 * @param 
	 * @return List
	 * 
	 */
	public List selectDataKontesPhoto() {
		return bacDao.selectDataKontesPhoto();
	}
	
	public List selectPolisBatalNonProduction(){
		return this.bacDao.selectPolisBatalNonProduction();
	}
	
	public void prosesProductionBatal(Date tgl, String spaj){
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		uwDao.insertProductionFromCancel(tgl, spaj);
		uwDao.insertDetProductionFromCancel(tgl, spaj);
	}
	/**
	 * Select Investasi Setelah Dikurangi Extra Premi (Khusus SIMPOL)
	 * @author MANTA
	 * @since 5 Sep 2013
	 */
    public ArrayList selectDetailInvestasi(String reg_spaj){
        return Common.serializableList(uwDao.selectDetailInvestasi(reg_spaj));
    }
    
	public Integer selectTotBiayaMuKe(String reg_spaj, Integer mu_ke){
		return uwDao.selectTotBiayaMuKe(reg_spaj, mu_ke);
	}
    
	public void insertMstBiayaUlink2(String reg_spaj, Integer mu_ke, Integer ljb_id, Integer mbu_jumlah, Integer mbu_persen, Date ldt_endpay) {
		bacDao.insertMstBiayaUlink2(reg_spaj, mu_ke, ljb_id, mbu_jumlah, mbu_persen, ldt_endpay);
	}
    
	public void updateMduJumlah(String reg_spaj, String lji_id, String mu_ke, Double mdu_jumlah){	
		uwDao.updateMduJumlah(reg_spaj, lji_id, mu_ke, mdu_jumlah);
	}
	
	public void updatePrmExt(String reg_spaj, Integer lsbs_id, Integer lsdbs_number){	
		uwDao.updatePrmExt(reg_spaj, lsbs_id, lsdbs_number);
	}
	
	public Integer updateKekuranganPrm(String reg_spaj, Integer prmbaru){	
		return uwDao.updateKekuranganPrm(reg_spaj, prmbaru);
	}
	
	public int selectLusSpecial(String lusid) {
		return uwDao.selectLusSpecial(lusid);
	}
	
	public List selectreportProduksiCair(Date bdate, Date edate, Integer jn_report){
		return uwDao.selectreportProduksiCair(bdate, edate, jn_report);
	}
	
	public List selectCabBII() {
		return this.uwDao.selectCabBII();
	}
	
	public List selectPolisCoverLetterBsm(String bdate, String edate, String stpolis, String lcb_no) {
		return uwDao.selectPolisCoverLetterBsm(bdate, edate, stpolis, lcb_no);
	}
	
	public List selectDataCoverBsmAll(String tanggalAwal, String tanggalAkhir) {
		return uwDao.selectDataCoverBsmAll(tanggalAwal, tanggalAkhir);
	}
	
	public List selectDataCoverBsmCab(String tanggalAwal, String tanggalAkhir, String lcb_no) {
		return uwDao.selectDataCoverBsmCab(tanggalAwal, tanggalAkhir, lcb_no);
	}
	
//====================================Upload Excel SPAJ===============================//
	public List<DropDown> selectCompanyUpload(String lca) {		
		return bacDao.selectCompanyUpload(lca);
	}

	public List<Map> terimaDataXls(ArrayList<List> SpajExcelList, User user, String company, String lca) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return this.uploadSpajTemp.terimaDataXls(SpajExcelList,user,company,lca);
	}

	public Pemegang selectppTemp(String no_temp) {
		return (Pemegang) this.bacDao.selectppTemp(no_temp);
	}

	public AddressBilling selectAddressBillingTemp(String no_temp) {
		
		return (AddressBilling) this.bacDao.selectAddressBillingTemp(no_temp);
	}

	public Tertanggung selectttgTemp(String no_temp) {		
		return (Tertanggung) this.bacDao.selectttTemp(no_temp);
	}

	public Datausulan selectMstProductTemp(String no_temp) {		
		return bacDao.selectMstProductTemp(no_temp);
	}

	public List<Datarider> selectMstProductTempRider(String no_temp) {		
		 return bacDao.selectMstProductTempRider(no_temp);
	}

	public Account_recur selectAccRecurTemp(String no_temp) {
		// TODO Auto-generated method stub
		return bacDao.selectAccRecurTemp(no_temp);
	}

	public List<PesertaPlus_mix> selectPesertaTemp(String no_temp) {
		// TODO Auto-generated method stub
		return bacDao.selectPesertaTemp(no_temp);
	}

	public List selectdataTempAll(String bdate, String edate, Integer cmp_id,
			String company1,String stpolis) {
		// TODO Auto-generated method stub
		return bacDao.selectdataTempAll(bdate,edate,cmp_id,company1,stpolis);
	}
	
	public List<DropDown> selectDistribusiData() {		
		return bacDao.selectDistribusiData();
	}
	
	public List<DropDown> selectDistribusi2Data() {		
		return bacDao.selectDistribusi2Data();
	}
	
//==================================== END OF Upload Excel SPAJ===============================//
	public List selectReportSummaryInputDanaSejahtera(String bdate, String edate) {
		return uwDao.selectReportSummaryInputDanaSejahtera(bdate, edate);
	}	
	public List selectReportFurtherDanaSejahteraBSM(String bdate, String edate) {
		return uwDao.selectReportFurtherDanaSejahteraBSM(bdate, edate);
	}
	//lufi--khusus bancass1
	public List selectAllLstCab3(){
	  return uwDao.selectAllLstCab3();
	}
	
	public Map selectEmailHybrid(String msag_id){
		return uwDao.selectEmailHybrid(msag_id);
	}
	
	public Map selectInformasiSpajExpired(String spaj) {
		return uwDao.selectInformasiSpajExpired(spaj);
	}
	
	public List selectReinstatementWorkSheet2(String spaj)throws DataAccessException{
		return uwDao.selectReinstatementWorkSheet2(spaj);
	}
	
	public void schedulerSummaryArco(){
		String msh_name = "SCHEDULER SUMMARY AKSEPTASI ARCO";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerSummaryBuatArco(msh_name);
		}
	}
	
	/**
	 * Update Posisi SK Debet / Kredit
	 * @author Daru
	 * @since 03 Dec 2013
	 */
	public void updatePosisiSkDebetKredit(String reg_spaj, Integer posisi, String lus_id, String keterangan) {
		uwDao.updatePosisiSkDebetKredit(reg_spaj, posisi, lus_id,keterangan);
	}
	
	public List select_produkutama_banksinarmas_simpol() {
			return this.bacDao.select_produkutama_banksinarmas_simpol();
	}
	
	public List selectMstProductInsuredExtra(String spaj) {
		return this.uwDao.selectMstProductInsuredExtra(spaj);
	}
	
	public List selectDaftarPolisOtorisasiUwSimasPrimaUW(Integer dari, Integer sampai) {
		return bacDao.selectDaftarPolisOtorisasiUwSimasPrimaUW(dari, sampai);
	}
	
	public List selectStokBrosurBusDev(String busdev, String jenis) {
		return basDao.selectStokBrosurBusDev(busdev, jenis);
	}

	public void updateStokBrosur(FormSpaj formbrosur) {
		basDao.updateStokBrosur(formbrosur);
	}

	public String TambahBrosur(String prefix, String nm_brosur, String busdev, User currentUser, String jenis) {
		return basDao.TambahBrosur(prefix,nm_brosur,busdev,currentUser,jenis);
	}
	
	public Integer selectJenisFormBrosur(String msf_id) {
		return basDao.selectJenisFormBrosur(msf_id);
	}

	public List selectCatatanPolis(String pemegang, String bdate) {
		return uwDao.selectCatatanPolis(pemegang, bdate);
	}

	public List selectReport_LisensiAgent(String bdate, String edate, String jenis, String lus_id, String lus_full_name, 
			String lca, String lwk, String lsrg) {
		return basDao.selectReport_LisensiAgent(bdate, edate, jenis, lus_id, lus_full_name, lca, lwk, lsrg);
	}
	
	/**
	 * Scheduler warning upload and transfer spaj (H+1) (17/12/2013)
	 * @author 	Canpri
	 */
	public void schedulerWarningUploadTransferSpaj(){
		String msh_name = "AUTOMAIL WARNING UPLOAD TRANSFER SPAJ";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerWarningUploadTransferSpaj(msh_name);
		}
	}
	
	/**
	 * Scheduler expired lisensi agent (02/12/2013)
	 * @author 	Canpri
	 */
	public void schedulerExpiredLisensiAgent(){
		String msh_name = "AUTOMAIL EXPIRED LISENSI AGENT";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerExpiredLisensiAgent(msh_name);
		}
	}

	public List<KartuNama> selectDaftarKartuNama(KartuNama cmd) {
		return basDao.selectDaftarKartuNama(cmd);
	}
	
	public List<KartuNama> selectDaftarNoKartuNama(KartuNama cmd) {
		return basDao.selectDaftarNoKartuNama(cmd);
	}

	public String processKartuNama(KartuNama cmd, User currentUser, HttpSession session) {
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		if(cmd.getMode()==0){
			return basDao.processKartuNama(cmd, currentUser, session);
		}else{
			return basDao.processKartuNamaNext(cmd, currentUser);
		}
	}

	public List<KartuNama> selectHistoryKartuNama(KartuNama cmd) {
		return basDao.selectHistoryKartuNama(cmd);
	}
	
	public Map ProsesAutoAcceptHPlusSatu(String no_reg, Integer flag_reg, Pemegang pmg,Tertanggung tt, User user,HttpServletRequest request) throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		//Pemegang pmg=elionsManager.selectpp(no_reg);
		Policy policy = new Policy();
		Integer error = 0;
		StringBuffer pesan = new StringBuffer();
		String psn = "";
		String mdesc = "";
		Integer lspdIdTemp = 0;
		String lspdPosTemp="";
		DecimalFormat fmt = new DecimalFormat ("000");
		Map mTertanggung =uwDao.selectTertanggung(no_reg);
		CommandChecklist cmd = new CommandChecklist();
		Date nowDate = commonDao.selectSysdate();		Map map= new HashMap();
		Map map2= new HashMap();
		List lsSimultanTt,lsSimultanPp;
		int info;
		
		//PROSES UNDERWRITER
				try{
					// Tgl Terima Doc 
					uwDao.updateMstInsuredTgl(no_reg, 1, nowDate, 2);
					uwDao.saveMstTransHistory(no_reg, "tgl_berkas_terima_uw", nowDate, null, null);
					if(uwDao.updateMstpositionSpajTgl(no_reg, user.getLus_id(), defaultDateFormat.format(nowDate), "TGL SPAJ DOC","TGL SPAJ DOC")==0){
						uwDao.insertMstPositionSpaj(user.getLus_id(), "TGL SPAJ DOC", no_reg, 0);
					}
					
					// Tgl Terima SPAJ
					uwDao.updateMstInsuredTgl(no_reg, 1, nowDate, 0);
					uwDao.saveMstTransHistory(no_reg, "tgl_terima_spaj_uw", nowDate, null, null);
					if(uwDao.updateMstpositionSpajTgl(no_reg, user.getLus_id(), defaultDateFormat.format(nowDate), "TGL TERIMA SPAJ","TGL TERIMA SPAJ")==0){
						uwDao.insertMstPositionSpaj(user.getLus_id(), "TGL TERIMA SPAJ", no_reg, 0);
					}
					
					//KYC
					uwDao.updateProsesKyc(no_reg, 1, user.getLus_id(), 0, nowDate);
					
					//Copy Checklist untuk Update flag_uw =1 , PROSES CHECKLIST DI UW
					
					cmd.setLspd_id(2);
					cmd.setReg_spaj(no_reg);
					cmd.setListChecklist(checklistDao.selectCheckListBySpaj(no_reg));
					checklistDao.saveChecklist(cmd, user);
					
					pesan.append("<br>- Proses Underwriting Berhasil!");
				}catch(Exception e){
					error = 1;
					e.printStackTrace();
					pesan.append("<br>- Proses Underwriting Error!");
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}
				
				// Prose Simultan 
				try{	
					String lsClientPpNew = null,lsClientTtNew=null;
					String lsClientPp = null,lsClientTt=null;
					String lsClientPpOld = pmg.getMcl_id(),lsClientTtOld = tt.getMcl_id(), idSimultanPp = null,idSimultanTt=null;
					Integer insured=1;
					Map mPemegang=uwDao.selectPemegang(no_reg);
					String mclIdPp,mclFirstPp,sDateBirthPp;
					Date mspeDateBirthPp,mspeDateBirthTt;
					Integer lsreIdTt;
					Integer lsreIdPp;
					String lcaIdPp;
					String mclIdTt,mclFirstTt,sDateBirthTt,lcaIdTt;
					//data pemegang
					lsreIdPp=(Integer)mPemegang.get("LSRE_ID");
					mclIdPp=(String)mPemegang.get("MCL_ID");
					mclFirstPp=(String)mPemegang.get("MCL_FIRST");
					mspeDateBirthPp=(Date)mPemegang.get("MSPE_DATE_BIRTH");
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				    sDateBirthPp=df.format(mspeDateBirthPp);
					lcaIdPp=(String)mPemegang.get("LCA_ID");
					//data tertanggung
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
					
					//set id simultan
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
					
					Map mapPp=(HashMap)lsSimultanPp.get(0);
					Map mapTT=(HashMap)lsSimultanTt.get(0);
					idSimultanPp=(String)mapPp.get("ID_SIMULTAN");
					idSimultanTt=(String)mapTT.get("ID_SIMULTAN");
					
						//cek medis
						Datausulan dataUsulan = bacDao.selectDataUsulanutama(no_reg);
						mdesc = reinstateDao.selectMedisSimultan(no_reg, pmg.getMste_age(), dataUsulan);
						
					//hubungan diri sendiri mapu
					if(pmg.getLsre_id()==1){//hubungan diri sendiri
						if (lsClientPpOld.substring(0,2).equalsIgnoreCase("00") ){ // sudah mengunakan pac_conter dari awal proses BAC , jadi tidak perlu generate MCL_ID yang baru.
							lsClientPpNew = lsClientPpOld;
						}else{
															
							lsClientPpNew=uwDao.wf_get_client_id(pmg.getLca_id());
							if(lsClientPpNew==null){
								TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
								return null;
							}
							//
							Client client=new Client();
							AddressNew addressNew=new AddressNew();
							Personal personal = new Personal();
							ContactPerson contactPerson = new ContactPerson();
							client=uwDao.selectMstClientNew(lsClientPpNew,lsClientPpOld);
							addressNew=uwDao.selectMstAddressNew(lsClientPpNew,lsClientPpOld);
							uwDao.insertMstClientNew(client);
							uwDao.insertMstAddressNew(addressNew);
							uwDao.updateMstPolicyMspoPolicyHolder(no_reg,lsClientPpNew);
							uwDao.updateMstInsuredMsteInsured(no_reg,lsClientPpNew);
				//			 tambahan untuk badan usaha
							if(client.getMcl_jenis() == 1){
								personal = commonDao.selectProfilePerusahaan(lsClientPpOld);
								contactPerson = bacDao.selectpic(lsClientPpOld);
								personal.setMcl_id(lsClientPpNew);
								contactPerson.setMcl_id(lsClientPpNew);
								bacDao.insertMstCompanyId(personal, lsClientPpOld);
								bacDao.insertMstCompanyContactId(contactPerson, lsClientPpOld);
								bacDao.updateMstCompanyContactAddressId(contactPerson, lsClientPpOld);
								bacDao.deleteMstCompanyContactFamily(lsClientPpOld);
								String nama_suamiistri = contactPerson.getNama_si();
								if(nama_suamiistri == null)nama_suamiistri = "";
								if (!nama_suamiistri.equalsIgnoreCase(""))
								{
									Date tanggal_lahir_suamiistri = contactPerson.getTgllhr_si();
									Map param1=new HashMap();
									param1.put("mcl_id", lsClientPpNew);
									param1.put("nama", contactPerson.getNama_si());
									param1.put("lsre_id",5);
									param1.put("tanggal_lahir", tanggal_lahir_suamiistri );
									param1.put("insured", 1);
									param1.put("no", 0);
									bacDao.insertMstCompanyContactFamily(param1);
								}
								
								String nama_anak1 = contactPerson.getNama_anak1();
								if(nama_anak1 == null)nama_anak1 = "";
								if (!nama_anak1.equalsIgnoreCase(""))
								{
									Date tanggal_lahir_anak1 = contactPerson.getTgllhr_anak1();
									Map param1=new HashMap();
									param1.put("mcl_id", lsClientPpNew);
									param1.put("nama", contactPerson.getNama_anak1());
									param1.put("lsre_id",4);
									param1.put("tanggal_lahir", tanggal_lahir_anak1);
									param1.put("insured", 1);
									param1.put("no", 1);
									bacDao.insertMstCompanyContactFamily(param1);
								}
								
								String nama_anak2 = contactPerson.getNama_anak2();
								if(nama_anak2 == null)nama_anak2 = "";
								if (!nama_anak2.equalsIgnoreCase(""))
								{
									Date tanggal_lahir_anak2 = contactPerson.getTgllhr_anak2();
									Map param1=new HashMap();
									param1.put("mcl_id", lsClientPpNew);
									param1.put("nama", contactPerson.getNama_anak2());
									param1.put("lsre_id",4);
									param1.put("tanggal_lahir", tanggal_lahir_anak2);
									param1.put("insured", 1);
									param1.put("no", 2);
									bacDao.insertMstCompanyContactFamily(param1);
								}
								
								String nama_anak3 = contactPerson.getNama_anak2();
								if(nama_anak3 == null)nama_anak3 = "";
								if (!nama_anak3.equalsIgnoreCase(""))
								{
									Date tanggal_lahir_anak3 = contactPerson.getTgllhr_anak3();
									Map param1=new HashMap();
									param1.put("mcl_id", lsClientPpNew);
									param1.put("nama", contactPerson.getNama_anak3());
									param1.put("lsre_id",4);
									param1.put("tanggal_lahir", tanggal_lahir_anak3);
									param1.put("insured", 1);
									param1.put("no", 3);
									bacDao.insertMstCompanyContactFamily(param1);
								}
								bacDao.deleteMstCompanyContactId(contactPerson, lsClientPpOld);
								bacDao.deleteMstCompanyId(personal, lsClientPpOld);
							}
						}
						//===
						lsClientPp=lsClientPpNew;
						lsClientTt=lsClientPpNew;
						if(idSimultanPp==null){//tidak simultan
							idSimultanPp=uwDao.createSimultanId();
							idSimultanTt=idSimultanPp;
						}
						lsClientTtNew=lsClientPpNew;
					}else{//hubungan orang lain
						//pemegang
						if (lsClientPpOld.substring(0,2).equalsIgnoreCase("00") ){ // sudah mengunakan pac_conter dari awal proses BAC , jadi tidak perlu generate MCL_ID yang baru.
							lsClientPpNew = lsClientPpOld;
							lsClientTtNew = lsClientTtOld;
						}else{
							lsClientPpNew=uwDao.wf_get_client_id(pmg.getLca_id());
							if(lsClientPpNew==null){
								TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
								return null;
							}
							Client client=new Client();
							AddressNew addressNew=new AddressNew();
							Personal personal = new Personal();
							ContactPerson contactPerson = new ContactPerson();
							client=uwDao.selectMstClientNew(lsClientPpNew,lsClientPpOld);
							addressNew=uwDao.selectMstAddressNew(lsClientPpNew,lsClientPpOld);
							uwDao.insertMstClientNew(client);
							uwDao.insertMstAddressNew(addressNew);
							uwDao.updateMstPolicyMspoPolicyHolder(no_reg,lsClientPpNew);
				//			 tambahan untuk badan usaha
							if(client.getMcl_jenis() == 1){
								personal = commonDao.selectProfilePerusahaan(lsClientPpOld);
								contactPerson = bacDao.selectpic(lsClientPpOld);
								personal.setMcl_id(lsClientPpNew);
								contactPerson.setMcl_id(lsClientPpNew);
								bacDao.insertMstCompanyId(personal, lsClientPpOld);
								bacDao.insertMstCompanyContactId(contactPerson, lsClientPpOld);
								bacDao.updateMstCompanyContactAddressId(contactPerson, lsClientPpOld);
								bacDao.deleteMstCompanyContactFamily(lsClientPpOld);
								String nama_suamiistri = contactPerson.getNama_si();
								if(nama_suamiistri == null)nama_suamiistri = "";
								if (!nama_suamiistri.equalsIgnoreCase(""))
								{
									Date tanggal_lahir_suamiistri = contactPerson.getTgllhr_si();
									Map param1=new HashMap();
									param1.put("mcl_id", lsClientPpNew);
									param1.put("nama", contactPerson.getNama_si());
									param1.put("lsre_id",5);
									param1.put("tanggal_lahir", tanggal_lahir_suamiistri );
									param1.put("insured", 1);
									param1.put("no", 0);
									bacDao.insertMstCompanyContactFamily(param1);
								}
								
								String nama_anak1 = contactPerson.getNama_anak1();
								if(nama_anak1 == null)nama_anak1 = "";
								if (!nama_anak1.equalsIgnoreCase(""))
								{
									Date tanggal_lahir_anak1 = contactPerson.getTgllhr_anak1();
									Map param1=new HashMap();
									param1.put("mcl_id", lsClientPpNew);
									param1.put("nama", contactPerson.getNama_anak1());
									param1.put("lsre_id",4);
									param1.put("tanggal_lahir", tanggal_lahir_anak1);
									param1.put("insured", 1);
									param1.put("no", 1);
									bacDao.insertMstCompanyContactFamily(param1);
								}
								
								String nama_anak2 = contactPerson.getNama_anak2();
								if(nama_anak2 == null)nama_anak2 = "";
								if (!nama_anak2.equalsIgnoreCase(""))
								{
									Date tanggal_lahir_anak2 = contactPerson.getTgllhr_anak2();
									Map param1=new HashMap();
									param1.put("mcl_id", lsClientPpNew);
									param1.put("nama", contactPerson.getNama_anak2());
									param1.put("lsre_id",4);
									param1.put("tanggal_lahir", tanggal_lahir_anak2);
									param1.put("insured", 1);
									param1.put("no", 2);
									bacDao.insertMstCompanyContactFamily(param1);
								}
								
								String nama_anak3 = contactPerson.getNama_anak2();
								if(nama_anak3 == null)nama_anak3 = "";
								if (!nama_anak3.equalsIgnoreCase(""))
								{
									Date tanggal_lahir_anak3 = contactPerson.getTgllhr_anak3();
									Map param1=new HashMap();
									param1.put("mcl_id", lsClientPpNew);
									param1.put("nama", contactPerson.getNama_anak3());
									param1.put("lsre_id",4);
									param1.put("tanggal_lahir", tanggal_lahir_anak3);
									param1.put("insured", 1);
									param1.put("no", 3);
									bacDao.insertMstCompanyContactFamily(param1);
								}
								bacDao.deleteMstCompanyContactId(contactPerson, lsClientPpOld);
								bacDao.deleteMstCompanyId(personal, lsClientPpOld);
							}
							//===
							//tertanggung
							lsClientTtNew=uwDao.wf_get_client_id(pmg.getLca_id());
							if(lsClientTtNew==null){
								TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
								return null;
							}
							Client client2=new Client();
							AddressNew addressNew2=new AddressNew();
							client2=uwDao.selectMstClientNew(lsClientTtNew,lsClientTtOld);
							addressNew2=uwDao.selectMstAddressNew(lsClientTtNew,lsClientTtOld);
							uwDao.insertMstClientNew(client2);
							uwDao.insertMstAddressNew(addressNew2);
							uwDao.updateMstInsuredMsteInsured(no_reg,lsClientTtNew);
						}
						lsClientPp=lsClientPpNew;
						lsClientTt=lsClientTtNew;
						if(idSimultanPp==null){//tidak simultan
							idSimultanPp=uwDao.createSimultanId();
						}
						if(idSimultanTt==null){//tidak simultan
							idSimultanTt=uwDao.createSimultanId();
						}
			
					}
			
					uwDao.wf_sts_client(lsClientPp,new Integer(1));
					uwDao.wf_sts_client(lsClientTt,new Integer(1));
					uwDao.wfInsSimultanNew(no_reg, lsClientTtNew, lsClientPpNew,insured,idSimultanPp,idSimultanTt);
					
					//insert mst_position_spaj untuk medis - Ket : Auto Accept SImultan : Medis (#desc#)
					uwDao.insertMstPositionSpaj(user.getLus_id(), "( AUTOMATED ACCEPT ) HASIL SIMULTAN : "+mdesc, no_reg, 0);
					
					pesan.append("<br>- Proses Simultan Berhasil!");
				}catch(Exception e){
					error = 1;
					pesan.append("<br>- Proses Simultan Error!");
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}
				
				//simultan pake function oracle
				/*String result_simultan = uwDao.prosesSimultanMedis(no_reg, user.getLus_id());
				if(!result_simultan.equalsIgnoreCase("sukses")){
					error = 1;
					pesan.append("<br>- Proses Simultan Error!");
				}else{
					pesan.append("<br>- Proses Simultan Berhasil!");
				}*/
				
				// Proses Reas - Ini yang susah.
				Reas reas=new Reas();
				Integer lsbsId = 0;
				try{
					reas.setInfo(new Integer(0));
					reas.setLspdId(new Integer(2));
					reas.setLstbId(new Integer(1));
					String las_reas[]=new String[3];
					las_reas[0]="Non-Reas";
					las_reas[1]="Treaty";
					las_reas[2]="Facultative";
					reas.setCurrentUser((User) request.getSession().getAttribute("currentUser"));        
			
					reas.setSpaj(no_reg);
					Map mPosisi=uwDao.selectF_check_posisi(no_reg);
					lspdIdTemp=reas.getLspdId();//(Integer)mPosisi.get("LSPD_ID");
					lspdPosTemp=(String)mPosisi.get("LSPD_POSITION");
					//produk asm
					map2=uwDao.selectDataUsulan(reas.getSpaj());
					lsbsId=(Integer)map2.get("LSBS_ID");
			
					//validasi Posisi dokumen
					if(lspdIdTemp.intValue()!=2){
						reas.setInfo(new Integer(1));
						reas.setLsPosDoc(lspdPosTemp);
						//MessageBox('Info', 'Posisi Polis Ini Ada di ' + ls_pos )
					}
					//
					//tertanggung
					reas.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
					reas.setMsteInsured((String)mTertanggung.get("MCL_ID"));
					reas.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
					//
					Map mStatus=uwDao.selectWfGetStatus(no_reg,reas.getInsuredNo());
					reas.setLiAksep((Integer)mStatus.get("LSSA_ID"));
					reas.setLiReas((Integer)mStatus.get("MSTE_REAS"));
					if (reas.getLiAksep()==null) 
						reas.setLiAksep( new Integer(1));
					
					
					//dw1 //pemegang
					policy=uwDao.selectDw1Underwriting(reas.getSpaj(),reas.getLspdId(),reas.getLstbId());
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
						if(reas.getMste_kyc_date()==null){
							reas.setInfo(new Integer(6));
						}		
						if ( (reas.getMspoPolicyHolder().substring(0,2).equalsIgnoreCase("XX")) || (reas.getMspoPolicyHolder().substring(0,2).equalsIgnoreCase("WW")) ){
							reas.setInfo(new Integer(2));
							//MessageBox('Info', 'Proses Simultan Belum Dilakukan !!!')
						}else if (reas.getLiReas()!=null){
							Integer liBackup=(Integer)uwDao.selectMstInsuredMsteBackup(reas.getSpaj(),reas.getInsuredNo());
							if(liBackup==null)
								liBackup=new Integer(100);
							if(liBackup.intValue()!=0 || (liBackup.intValue()==0 && reas.getLiReas().intValue()==2) ){
								reas.setInfo(new Integer(3));
								reas.setLsPosDoc(las_reas[reas.getLiReas().intValue()]);
				//				If MessageBox( 'Information', 'Proses Reas sudah pernah dilakukan~r~nType Reas = ' + las_reas[li_reas+1] &
				//				+  '~r~nView hasil proses sebelumnya ?', Exclamation!, OKCancel!, 1 ) = 1 Then OpenWithParm(w_simultan,lstr_polis.no_spaj)
							}
							
						}
						//cek standard
						if(policy.getMste_standard().intValue()==1){
						Integer liCount=uwDao.selectCountMstProductInsuredCekStandard(reas.getSpaj());
							if(liCount.intValue()==0){
								//li_count = Messagebox('Info', 'Polis ini non-standard, Extra Premi Belum Ada !!!~n~nYakin Lanjutkan ?', Question!, Yesno!, 2)
								reas.setInfo(new Integer(4));
								//info=4;
							}
						}
					}
					reas.setLstbId(1);
					reas.setLspdId(2);
					Map proses=reasIndividu.prosesReasUnderwriting(reas, null);
					
					pesan.append("<br>- Proses Reas Berhasil!");
				}catch(Exception e){
					error = 1;
					e.printStackTrace();
					pesan.append("<br>- Proses Reas Error!");
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}
					
				//Proses Akseptasi , langsung Accepted
				Akseptasi akseptasi=new Akseptasi();
				String lca_id = "";
				try{
					akseptasi.setLbUlink(false);
					akseptasi.setLspdId(new Integer(2));
					akseptasi.setLsspId(new Integer(10));
					akseptasi.setLssaId(new Integer(5));
					akseptasi.setLstbId(new Integer(1));
					akseptasi.setSpaj(no_reg);
					akseptasi.setLsbsId(lsbsId);
					akseptasi.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
					akseptasi.setNoPolis(policy.getMspo_policy_no());
					List lsStatusAksep=akseptasiDao.selectMstPositionSpaj(akseptasi.getSpaj());
					//
					akseptasi.setCurrentUser(user);        
					
					Position position=new Position();
					position.setReg_spaj(akseptasi.getSpaj());
					//position.setMsps_date(akseptasiDao.selectSysdate(new Integer(2)));
					position.setMsps_date(commonDao.selectSysdate());
					position.setLus_id(akseptasi.getCurrentUser().getLus_id());
					position.setLus_login_name(akseptasi.getCurrentUser().getName());
					//cek lagi untuk pengesetan info pake karena karena untuk sementara kondisi ini gak terpakai seutuhnya
					
					if(akseptasi.getInfo1()==10)
						position.setLssa_id(new Integer(8));
					else
						position.setLssa_id(new Integer(5));
					lsStatusAksep.add(position);
					//cekin untuk produk yang masih pending
					Map mapTt=uwDao.selectTertanggung(akseptasi.getSpaj());
					Integer lssaId=(Integer)mapTt.get("LSSA_ID");
					Integer flag_investasi=(Integer) mapTt.get("MSTE_FLAG_INVESTASI");
					akseptasi.setFlag_investasi(flag_investasi);
					Map mAksep=(HashMap)uwDao.selectLstStatusAccept(lssaId).get(0);
					String statusAksep=(String) mAksep.get("STATUS_ACCEPT");
					Integer countSts=uwDao.selectCountMstPositionSpaj(akseptasi.getSpaj(),"3,4,9");
					
					if(lspdIdTemp.intValue()!=2){
						akseptasi.setInfo1(1);
						akseptasi.setLsposDoc(lspdPosTemp);
						//polis ini ada di lspdPosTemp.
					}else if(lsbsId.intValue()==161){//produk asm
						akseptasi.setInfo1(2);
					}else if(countSts>0){ //akseptasi khusus (further requirement, extra premi, postponed spaj)
						akseptasi.setInfo1(3);
						akseptasi.setPesan("Status Polis masih "+statusAksep+"\nSilahkan gunakan Akseptasi Khusus untuk Mengaksep Polis ini.");
						//akseptasi.set
					}	
					
					List<Datarider> listRider = (List<Datarider>) bacDao.selectDataUsulan_rider(akseptasi.getSpaj());
					Integer total_ekasehat = bacDao.selectCountEkaSehatAdmedikaNew(akseptasi.getSpaj(),2);
					
					for(int i=0;i<total_ekasehat;i++){
						List<Map> listPeserta =uwDao.selectDataPeserta(akseptasi.getSpaj());
						if(listPeserta.isEmpty())continue;
						Map mapPeserta = listPeserta.get(i);
						for(int j=0; j<listPeserta.size();j++){
							Integer umur = Integer.parseInt(mapPeserta.get("UMUR").toString());
							if( umur>=50 && umur<=55){
								akseptasi.setInfo1(4);
							}
						}
					}
					
					akseptasi.setListSize(lsStatusAksep.size());
					akseptasi.setLsStatusAksep(lsStatusAksep);
					akseptasi.setSize(new Integer(lsStatusAksep.size()));
					lca_id = uwDao.selectCabangFromSpaj(akseptasi.getSpaj());
					akseptasi.setProses("1");
					akseptasi.setLiAksep(10);
					akseptasi.setLcaId(lca_id);
					
					if(!lca_id.equals("09")){
						//if(reas.getLiAksep()==5 | reas.getLiAksep()==10){
							Integer mspo_provider= uwDao.selectGetMspoProvider(akseptasi.getSpaj());
							if(mspo_provider==2){
								uwDao.updateMstInsuredKirimAdmedika(akseptasi.getSpaj(),akseptasi.getInsuredNo(),akseptasi.getCurrentUser().getLus_id());
								uwDao.saveMstTransHistory(akseptasi.getSpaj(), "tgl_kirim_admedika", null, null, null);
							}
							
							prosesEndorsemen(akseptasi.getSpaj(), Integer.parseInt(uwDao.selectBusinessId(akseptasi.getSpaj())));
							
						//}
					}	
					
					//String desc = "( AUTOMATED ACCEPT ) AC : NM, STD";//perubahan keterangan liat #mdesc
					String desc = "( AUTOMATED ACCEPT ) AK : SPAJ BELUM DITERIMA UW";
					int tmp = uwDao.prosesAkseptasi(akseptasi,0,0,desc,null);
					
					pesan.append("<br>- Proses Akseptasi Berhasil!");
				}catch(Exception e){
					error = 1;
					pesan.append("<br>- Proses Akseptasi Error!");
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}
				
				//Proses transfer ke payment
				Transfer transfer=new Transfer();
				try{
					//formbacking
					transfer.setInsuredNo(new Integer(1));
					transfer.setSpaj(no_reg);
					transfer.setInfo(new Integer(0));
					transfer.setLiPosisi(0);
					transfer.setLspdId(new Integer(2));
					transfer.setLstbId(new Integer(1));
					transfer.setCurrentUser(user);
					//cek posisi
					Map mtPosisi = uwDao.selectF_check_posisi(transfer.getSpaj());
					Integer lspdIdTemp_transfer=(Integer)mtPosisi.get("LSPD_ID");
					String lspdPosTemp_transfer=(String)mtPosisi.get("LSPD_POSITION");
					if(lspdIdTemp_transfer.intValue()!=2){
						transfer.setInfo(new Integer(1));
						transfer.setLsposDoc(lspdPosTemp_transfer);
						//posisi document ada di lspdPosTemp
					}
					//
					Map mtStatus=uwDao.selectWfGetStatus(transfer.getSpaj(),transfer.getInsuredNo());
					transfer.setLiAksep((Integer)mtStatus.get("LSSA_ID"));
					transfer.setLiReas((Integer)mtStatus.get("MSTE_REAS"));
					if (transfer.getLiAksep()==null) 
						transfer.setLiAksep(new Integer(1));
					transfer.setLiBackup((Integer)uwDao.selectMstInsuredMsteBackup(transfer.getSpaj(),transfer.getInsuredNo()));

					//
					if(transfer.getLiAksep().intValue()==2){
						transfer.setInfo(new Integer(2));
						//status spaj decline , Transfer Status Decline ke Policy Canceled ?
					}
					//dw1 //pemegang
					policy=uwDao.selectDw1Underwriting(transfer.getSpaj(),transfer.getLspdId(),transfer.getLstbId());
					if(policy!=null){
						transfer.setMspoPolicyHolder(policy.getMspo_policy_holder());
						transfer.setNoPolis(policy.getMspo_policy_no());
						transfer.setInsPeriod(policy.getMspo_ins_period());
						transfer.setPayPeriod(policy.getMspo_pay_period());
						transfer.setLkuId(policy.getLku_id());
						transfer.setUmurPp(policy.getMspo_age());
						transfer.setLcaId(policy.getLca_id());
					//

						//cek standard
						if(policy.getMste_standard().intValue()==1){
						Integer liCount=uwDao.selectCountMstProductInsuredCekStandard(transfer.getSpaj());
							if(liCount.intValue()==0){
								//Polis ini non-standard, Extra Premi Belum Ada !!!~n~nYakin Lanjutkan ?'
								transfer.setInfo(new Integer(3));
							}
						}
						transfer.setPolicy(policy);
					}
					//
					List lsProdInsured = akseptasiDao.selectMstProductInsured(transfer.getSpaj(),new Integer(1),new Integer(1));
					String lsdbs_number = uwDao.selectLsdbsNumber(transfer.getSpaj());
					if(!lsProdInsured.isEmpty()){
						Product prodIns=(Product)lsProdInsured.get(0);
						transfer.setLsbsId(prodIns.getLsbs_id());
						transfer.setLsdbsNumber(prodIns.getLsdbs_number());
					}
					//
					transfer.setLiLama(uwDao.selectCountMstCancel(transfer.getSpaj()));
					transfer.setLsDp(uwDao.selectMstDepositPremium(transfer.getSpaj(),null));
					//
					if(transfer.getLsbsId().intValue()==157 || (transfer.getLsbsId().intValue()==196)&&lsdbs_number.equals("2")){//jika product endowment langsung transfer ke print polis 14-06-06
						transfer.setLiPosisi(6);
						transfer.setTo("Print Polis ?");
					}else{
//						if(transfer.getLsDp().isEmpty() && transfer.getLiLama().intValue() == 0){
//							transfer.setLiPosisi(10);
//							transfer.setTo("Print Speciment");
//						}else
						if (transfer.getLiLama().intValue() > 0){
							transfer.setLiPosisi(6);
							transfer.setTo("Print Polis ?");
							transfer.setLiLama(new Integer(1));
						}else{
							transfer.setLiPosisi(4);
							transfer.setTo("Pembayaran ?");
						}
					}	
					
					transfer.setInfo(new Integer(100));
					//
					
					//data usulan asuransi
					Map mDataUsulan=uwDao.selectDataUsulan(transfer.getSpaj());
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
					//Map mTertanggung=elionsManager.selectTertanggung(transfer.getSpaj());
					transfer.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
					transfer.setMsteInsured((String)mTertanggung.get("MCL_ID"));
					transfer.setMsagId((String)mTertanggung.get("MSAG_ID"));
					transfer.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
					//end form backing
					
					Integer flagNew = 1;//(0=old, 1=New) proses pembayaran baru atao lama.
					int hasil = 0;
					String pesanTambahan = null;
					Map allResult = new HashMap();
					//Transfer transfer=(Transfer)cmd;
					
					BindException err = null;
					
					allResult = transferUw.prosesTransferPembayaran(transfer,flagNew,err,request);
					pesanTambahan = (String) allResult.get("pesanTambahan");
					hasil = (Integer) allResult.get("proses");
					
					pesan.append("<br>- Proses Transfer ke Payment Berhasil!");
				}catch(Exception e){
					error = 1;
					pesan.append("<br>- Proses Transfer ke Payment Error!");
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}
				
//				jika sukses, tampung value dalam success. jika gagal, tampung value dalam error.
				
				if(error>0){//ada error
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					psn = "Error : "+pesan.toString();
				}else{//tidak ada error
					psn = "Sukses : "+pesan.toString();
					if((akseptasi.getLiAksep()==5 || akseptasi.getLiAksep()==10) && "37,52,63".indexOf(lca_id)>-1){
						prosesEmailSMSAkseptasi(akseptasi.getSpaj(), akseptasi.getLiAksep());
					}
				}
				pesan.append("<br> - Proses ( AutoProse H+1 ) No : "+no_reg+" telah berhasil");
			//	psn = "Error : "+pesan.toString();
				map.put("success", psn);
				map.put("error", "Error");
				
				return map;
	}
	
	public List selectFilterOtorisasiHPlusSatu(String tipe, String kata, String pilter, Integer jn_bank) throws DataAccessException {
		return this.uwDao.selectFilterOtorisasiHPlusSatu(tipe, kata, pilter, jn_bank);
	}
	
	public List<Map> deleteDatatemp(String notemp) throws DataAccessException {
		return this.uploadSpajTemp.deleteDatatemp(notemp);
	}

	/**
	 * select jenis surat
	 * @author 	Canpri
	 * @param flag cabang
	 */
	public List selectJenisSurat(String cabang) throws DataAccessException {
		return this.bacDao.selectJenisSurat(cabang);
	}

	//insert ke mst_req_surat
	public String insertRequestPrintSurat(User currentUser, String spaj, String polis, String jenis_surat, String surat) throws DataAccessException, MailException, MessagingException {
		return this.bacDao.insertRequestPrintSurat(currentUser, spaj, polis, jenis_surat, surat);
	}
	
	//MANTA - BegDate Editor
	public Integer selectNewBussiness(String reg_spaj) {
		return uwDao.selectNewBussiness(reg_spaj);
	}
	
	public Policy selectMstPolicyAll(String reg_spaj) {
		return uwDao.selectMstPolicyAll(reg_spaj);
	}
	
	public Insured selectMstInsuredAll(String reg_spaj) {		
		return uwDao.selectMstInsuredAll(reg_spaj);
	}
	
	public ArrayList<Product> selectMstProdInsAll(String reg_spaj) {		
		return Common.serializableList(uwDao.selectMstProdInsAll(reg_spaj));
	}
	
	public ArrayList<Production> selectMstProductionAll(String reg_spaj) {		
		return Common.serializableList(uwDao.selectMstProductionAll(reg_spaj));
	}
	
	public ArrayList<Billing> selectMstBillingAll(String reg_spaj) {		
		return Common.serializableList(uwDao.selectMstBillingAll(reg_spaj));
	}
	
	public HashMap selectMstPasSMSAll(String reg_spaj) {		
		return Common.serializableMap(uwDao.selectMstPasSMSAll(reg_spaj));
	}
	
	public ArrayList<Ulink> selectMstUlinkAll(String reg_spaj) {		
		return Common.serializableList(uwDao.selectMstUlinkAll(reg_spaj));
	}
	
	public ArrayList<TransUlink> selectMstTransUlinkAll(String reg_spaj) {		
		return Common.serializableList(uwDao.selectMstTransUlinkAll(reg_spaj));
	}
	
	public ArrayList<DetUlink> selectMstDetUlinkAll(String reg_spaj) {		
		return Common.serializableList(uwDao.selectMstDetUlinkAll(reg_spaj));
	}
	
	public ArrayList<BiayaUlink> selectMstBiayaUlinkAll(String reg_spaj) {		
		return Common.serializableList(uwDao.selectMstBiayaUlinkAll(reg_spaj));
	}
	
	public ArrayList<UlinkBill> selectMstUlinkBillAll(String reg_spaj) {		
		return Common.serializableList(uwDao.selectMstUlinkBillAll(reg_spaj));
	}
	
	public void updateMstPasSMSBegDate(String reg_spaj, Date msp_pas_beg_date, Date msp_pas_end_date){	
		uwDao.updateMstPasSMSBegDate(reg_spaj, msp_pas_beg_date, msp_pas_end_date);
	}
	
	public Map selectNoKartuPas(String reg_spaj) {		
		return uwDao.selectNoKartuPas(reg_spaj);
	}
	
	/**
	 * select dropdown peserta
	 * @author 	Lufi
	 * @param spaj
	 */
	public List<DropDown> selectDropDownDaftarPeserta(
			String spaj) {
		return this.uwDao.selectDropDownDaftarPeserta(spaj);
	}
    
	/**
	 * select medical questionare
	 * @author 	Lufi
	 * @param spaj,flag_jenis_peserta
	 */
	public List selectJawabanMedical(String spaj, int flag_jenis_peserta) {
		// TODO Auto-generated method stub
		return this.uwDao.selectJawabanMedical(spaj,flag_jenis_peserta);
	}	
	
	public List selectJawabanMedicalsSIOtambahan(String spaj, int flag_jenis_peserta) {
		return this.uwDao.selectJawabanMedicalsSIOtambahan(spaj,flag_jenis_peserta);
	}	

	/**
	 * select polis berdasarkan pemegang
	 * @author 	Canpri
	 * @param pemegang
	 */
	public List selectFindPolis(String cari) {
		return bacDao.selectFindPolis(cari);
	}

	/**
	 * select request surat dari cabang yang belum diproses
	 * @author 	Canpri
	 * @param lus_id
	 */
	public List selectReqPrintSurat(String lus_id) {
		return bacDao.selectReqPrintSurat(lus_id);
	}
	
	/**
	 * select komisi cross selling
	 * @author 	Canpri
	 * @param no polis/nama pemegang
	 */
	public List selectKomisiCrossSelling(String cari) {
		return bacDao.selectKomisiCrossSelling(cari);
	}
	
	/**
	 * select Data Proposal 
	 * @author 	MANTA
	 * @param no id proposal
	 */
	public Map selectDataMstProposal(String id_props){
		return bacDao.selectDataMstProposal(id_props);
	}
	
	public HashMap selectMstDataProposal(String id_props){
		return Common.serializableMap(bacDao.selectMstDataProposal(id_props));
	}
	
	public HashMap selectMstProposalProduct(String id_props){
		return Common.serializableMap(bacDao.selectMstProposalProduct(id_props));
	}
	
	public ArrayList selectMstProposalProductRider(String id_props){
		return Common.serializableList(bacDao.selectMstProposalProductRider(id_props));
	}
	
	public ArrayList selectMstProposalProductPeserta(String id_props){
		return Common.serializableList(bacDao.selectMstProposalProductPeserta(id_props));
	}
	
	public ArrayList selectMstProposalProductTopUp(String id_props){
		return Common.serializableList(bacDao.selectMstProposalProductTopUp(id_props));
	}

	public String selectNoPreMstPerusahaan( String MclIdl, String periode ){
			return this.worksiteDao.selectNoPreMstPerusahaan( MclIdl, periode );
	}

	/*	Kode VA berdasarkan produk 
	 *  UNIT LINK = 01			UNIT LINK SYARIAH = 09
		NON UNIT LINK = 02		NON UNIT LINK SYARIAH = 10
		SIMPOL = 03				SIMPOL SYARIAH = 11
		SIMAS PRIMA = 04		POWER SAVE SYARIAH  = 12
		PRODUK SAVE = 05		PRODUK SAVE SYARIAH = 13
		SMILE LINK SATU = 06	SPAJ KECELAKAAN SYARIAH = 14
		SPAJ KECELAKAAN = 07	SPAJ DANAMAS PRIMA = 15
		VIP FAMILY PLAN = 08	
	 * 
	 */
	public String prosesPermintaanVA(VirtualAccount va, User currentUser) throws DataAccessException, ParseException, MailException, MessagingException{
		String result = uwDao.prosesPermintaanVA(va, currentUser);
		
		//Update Counter
		HashMap paper_ul = (HashMap) uwDao.selectMstCounter(171, "01");
		HashMap paper_nul = (HashMap) uwDao.selectMstCounter(177, "01");
		HashMap paper_simpol = (HashMap) uwDao.selectMstCounter(179, "01");
		HashMap paper_sprima = (HashMap) uwDao.selectMstCounter(181, "01");
		HashMap paper_psave = (HashMap) uwDao.selectMstCounter(183, "01");
		HashMap paper_slinksatu = (HashMap) uwDao.selectMstCounter(185, "01");
		HashMap paper_kcl = (HashMap) uwDao.selectMstCounter(187, "01");
		HashMap paper_familyplan = (HashMap) uwDao.selectMstCounter(189, "01");
		HashMap paper_harda = (HashMap) uwDao.selectMstCounter(213, "01");
		HashMap paper_smultimate = (HashMap) uwDao.selectMstCounter(224, "01");
		HashMap paper_btn_syariah_life = (HashMap) uwDao.selectMstCounter(230, "01");
		HashMap paper_btn_syariah_link = (HashMap) uwDao.selectMstCounter(242, "01"); //change from 231 to 242
		//tambahan
		HashMap paper_ul_syariah = (HashMap) uwDao.selectMstCounter(198, "01");
		HashMap paper_nul_syariah = (HashMap) uwDao.selectMstCounter(240, "01"); //change from 200 to 240
		HashMap paper_simpol_syariah = (HashMap) uwDao.selectMstCounter(202, "01");
		HashMap paper_powersave_syariah = (HashMap) uwDao.selectMstCounter(241, "01"); //change from 204 to 241
		HashMap paper_psave_syariah = (HashMap) uwDao.selectMstCounter(206, "01");
		HashMap paper_kcl_syariah = (HashMap) uwDao.selectMstCounter(208, "01");
		HashMap paper_danamas_prima = (HashMap) uwDao.selectMstCounter(210, "01");
		HashMap paper_primemagna = (HashMap) uwDao.selectMstCounter(238, "01"); //change from 215 to 238
		
		HashMap gadget_ul = (HashMap) uwDao.selectMstCounter(172, "01");
		HashMap gadget_nul = (HashMap) uwDao.selectMstCounter(178, "01");
		HashMap gadget_simpol = (HashMap) uwDao.selectMstCounter(180, "01");
		HashMap gadget_sprima = (HashMap) uwDao.selectMstCounter(182, "01");
		HashMap gadget_psave = (HashMap) uwDao.selectMstCounter(184, "01");
		HashMap gadget_slinksatu = (HashMap) uwDao.selectMstCounter(186, "01");
		HashMap gadget_kcl = (HashMap) uwDao.selectMstCounter(188, "01");
		HashMap gadget_familyplan = (HashMap) uwDao.selectMstCounter(190, "01");
		HashMap gadget_smultimate = (HashMap) uwDao.selectMstCounter(223, "01");
		//tambahan
		HashMap gadget_ul_syariah = (HashMap) uwDao.selectMstCounter(239, "01"); //change from 199 to 239
		HashMap gadget_nul_syariah = (HashMap) uwDao.selectMstCounter(201, "01");
		HashMap gadget_simpol_syariah = (HashMap) uwDao.selectMstCounter(203, "01");
		HashMap gadget_powersave_syariah = (HashMap) uwDao.selectMstCounter(205, "01");
		HashMap gadget_psave_syariah = (HashMap) uwDao.selectMstCounter(207, "01");
		HashMap gadget_kcl_syariah = (HashMap) uwDao.selectMstCounter(209, "01");
		HashMap gadget_danamas_prima = (HashMap) uwDao.selectMstCounter(211, "01");
		
		HashMap l_konven = (HashMap) uwDao.selectMstCounter(192, "01");
		HashMap lainnya = (HashMap) uwDao.selectMstCounter(193, "01");
		
		Integer value = 0;
		Integer no = 0;
		
		if(va.getJenis_va()==2 || va.getJenis_va()==3){//gadget
			//Integer value = ((Double)gadget.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
			
			if(va.getJenis_spaj().equals("01")){//unit link
				value = ((Double)gadget_ul.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 172;
			}else if(va.getJenis_spaj().equals("02")){//non unit link
				value = ((Double)gadget_nul.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 178;
			}else if(va.getJenis_spaj().equals("03")){//simpol
				value = ((Double)gadget_simpol.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 180;
			}else if(va.getJenis_spaj().equals("04")){//simas prima
				value = ((Double)gadget_sprima.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 182;
			}else if(va.getJenis_spaj().equals("05")){//produk save
				value = ((Double)gadget_psave.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 184;
			}else if(va.getJenis_spaj().equals("06")){//smile link satu
				value = ((Double)gadget_slinksatu.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 186;
			}else if(va.getJenis_spaj().equals("07")){//kecelakaan
				value = ((Double)gadget_kcl.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 188;
			}else if(va.getJenis_spaj().equals("08")){//vip family plan
				value = ((Double)gadget_familyplan.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 190;
			}else if(va.getJenis_spaj().equals("09")){//unit link syariah
				value = ((Double)gadget_ul_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 239;
			}else if(va.getJenis_spaj().equals("10")){//non unit link syariah
				value = ((Double)gadget_nul_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 201;
			}else if(va.getJenis_spaj().equals("11")){//simpol syariah
				value = ((Double)gadget_simpol_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 203;
			}else if(va.getJenis_spaj().equals("12")){//power save syariah
				value = ((Double)gadget_powersave_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 205;
			}else if(va.getJenis_spaj().equals("13")){//produk save syariah
				value = ((Double)gadget_psave_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 207;
			}else if(va.getJenis_spaj().equals("14")){//kecelakaan syariah
				value = ((Double)gadget_kcl_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 209;
			}else if(va.getJenis_spaj().equals("15")){//danamas prima
				value = ((Double)gadget_danamas_prima.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 211;
			}else if(va.getJenis_spaj().equals("17")){//smile link ultimate
				value = ((Double)gadget_smultimate.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 223;
			}else if(va.getJenis_spaj().equals("21")){//prime magna
				value = ((Double)paper_primemagna.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 238;
			}else if(va.getJenis_spaj().equals("22")){//smile life syariah
				value = ((Double)paper_btn_syariah_life.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 230;	
			}	
			
			Map param = new HashMap();
			param.put("intIDCounter", value);
			param.put("no", no);
			param.put("kodecbg", "01");
			worksiteDao.update_mst_counter(param);
			
		}else if(va.getJenis_va()==0 || va.getJenis_va()==1){//paper
			//Integer value = ((Double)paper.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
			
			if(va.getJenis_spaj().equals("01")){//unit link
				value = ((Double)paper_ul.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 171;
			}else if(va.getJenis_spaj().equals("02")){//non unit link
				value = ((Double)paper_nul.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 177;
			}else if(va.getJenis_spaj().equals("03")){//simpol
				value = ((Double)paper_simpol.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 179;
			}else if(va.getJenis_spaj().equals("04")){//simas prima
				value = ((Double)paper_sprima.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 181;
			}else if(va.getJenis_spaj().equals("05")){//produk save
				value = ((Double)paper_psave.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 183;
			}else if(va.getJenis_spaj().equals("06")){//smile link satu
				value = ((Double)paper_slinksatu.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 185;
			}else if(va.getJenis_spaj().equals("07")){//kecelakaan
				value = ((Double)paper_kcl.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 187;
			}else if(va.getJenis_spaj().equals("08")){//vip family plan
				value = ((Double)paper_familyplan.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 189;
			}else if(va.getJenis_spaj().equals("09")){//unit link syariah
				value = ((Double)paper_ul_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 198;
			}else if(va.getJenis_spaj().equals("10")){//non unit link syariah
				value = ((Double)paper_nul_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 240;
			}else if(va.getJenis_spaj().equals("11")){//simpol syariah
				value = ((Double)paper_simpol_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 202;
			}else if(va.getJenis_spaj().equals("12")){//power save syariah
				value = ((Double)paper_powersave_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 241;
			}else if(va.getJenis_spaj().equals("13")){//produk save syariah
				value = ((Double)paper_psave_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 206;
			}else if(va.getJenis_spaj().equals("14")){//kecelakaan syariah
				value = ((Double)paper_kcl_syariah.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 208;
			}else if(va.getJenis_spaj().equals("15")){//danamas prima
				value = ((Double)paper_danamas_prima.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 210;
			}else if(va.getJenis_spaj().equals("17")){//smile link ultimate
				value = ((Double)paper_smultimate.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 224;
			}else if(va.getJenis_spaj().equals("20")){//Harda
				value = ((Double)paper_harda.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 213;
			}else if(va.getJenis_spaj().equals("21")){//prime magna
				value = ((Double)paper_primemagna.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 238;
			}else if(va.getJenis_spaj().equals("22")){//btn syariah
				value = ((Double)paper_btn_syariah_life.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 230;
			}else if(va.getJenis_spaj().equals("23")){//btn syariah link
				value = ((Double)paper_btn_syariah_link.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 242;
			}
			
			Map param = new HashMap();
			param.put("intIDCounter", value);
			param.put("no", no);
			param.put("kodecbg", "01");
			worksiteDao.update_mst_counter(param);
			
		}else if(va.getJenis_va()==4){
			if(va.getJenis_spaj().equals("07")){
				value = ((Double)l_konven.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 192;
			}
			
			Map param = new HashMap();
			param.put("intIDCounter", value);
			param.put("no", no);
			param.put("kodecbg", "01");
			worksiteDao.update_mst_counter(param);
			
		}else if(va.getJenis_va()==5){//belum ada
			if(va.getJenis_spaj().equals("07")){
				value = ((Double)l_konven.get("MSCO_VALUE")).intValue() + Integer.parseInt(va.getMsv_amount_req());
				no = 193;
			}
			
			Map param = new HashMap();
			param.put("intIDCounter", value);
			param.put("no", no);
			param.put("kodecbg", "01");
			worksiteDao.update_mst_counter(param);
		}
		
		return result;
	}

	public List selectPermintaanVA(String msv_id) {
		return uwDao.selectPermintaanVA(msv_id);
	}
	
	public List selectJenisBank (String j_bank){
		return uwDao.selectJenisBank(j_bank);
	}
	

	public List selectAddressRegionLcaId(String lca) {
		return basDao.selectAddressRegionLcaId(lca);
	}
	
	public String selectno_virtual_sama(String no_va)
	{
		return this.bacDao.no_virtual_sama( no_va );
	}
	
	public List<Map> selectBankDataSub(Integer jenis ) {
		return uwDao.selectBankDataSub(jenis);
	}
	
	// Select list no voucher taxi - Daru 19 Des 2013
	public List<String> selectAllNoVoucherTaxi(User currentUser) {
		return commonDao.selectAllNoVoucherTaxi(currentUser);
	}
	
	// Insert voucher taxi - Daru 03 Jan 2014
	public void insertVoucherTaxi(VoucherTaxi voucherTaxi, String lus_id) {
		commonDao.insertVoucherTaxi(voucherTaxi, lus_id);
	}
	
	// Select voucher taxi - Daru 03 Jan 2014
	public VoucherTaxi selectVoucherTaxi(String msvt_no) {
		return commonDao.selectVoucherTaxi(msvt_no);
	}
	
	// Update voucher taxi - Daru 07 Jan 2014
	public void updateVoucherTaxi(VoucherTaxi voucherTaxi, String lus_id) {
		commonDao.updateVoucherTaxi(voucherTaxi, lus_id);
	}
	
	// Select report voucher taxi - Daru 30 Mar 2015
	public List selectReportVoucherTaxi(HashMap<String, Object> params) {
		return commonDao.selectReportVoucherTaxi(params);
	}
	
	// Cari Marketing - Daru 19 Feb 2014
	public List selectMarketing(HashMap<String, Object> params) {
		return commonDao.selectMarketing(params);
	}
	
	//insert fee rider - Ryan	
	public void insertFeeRider(Date tgl_proses, String no_polis, String lsdbs_name, Integer jml , String status, String noKloter,Double tax) {
		uwDao.insertBiayaFee(tgl_proses, no_polis , lsdbs_name, jml,status, noKloter, tax);
	}
	//generate no kloter - Ryan
	public String noKloterUpload(String lca_id) {
		return sequence.sequenceNoKloter(lca_id);
	}
	// view data upload - RYan
	public List selectDataUploadAdmedika(String bdate, String edate, Integer cmp_id,
			String company1,String stpolis) {
		// TODO Auto-generated method stub
		return Common.serializableList(uwDao.selectDataUploadAdmedika(bdate,edate,cmp_id,company1,stpolis));
	}
	//List PSN - Ryan
	public List<Map> selectJenisPSN( ) {
		return bacDao.selectJenisPSN();
	}
	// List Lembaga PSN - Ryan
	public List selectDataLembagaPsn(String id_psn) {
		return bacDao.selectDataLembagaPsn(id_psn);
	}
	
	// List Harga ITem PSN - Ryan
	public List selectDataHargaItem(String id_psn) {
			return bacDao.selectDataHargaItem(id_psn);
		}
	// List Jenis Religi - Ryan
		public List selectDataReligi(String id_psn) {
				return bacDao.selectDataReligi(id_psn);
			}
	//insert PSN - Ryan
	public void insertPSN(String reg_spaj, Integer id_item , Integer id_psn, Integer id_lbg, Date tgl_pot, Integer flag_pot, Integer freq_pot,
			Integer jml_pot,Integer jml_item , Integer jenis_wakaf, Integer lus_id,Date tgl_input, Integer flag_aktif) {
		bacDao.insertPSN(reg_spaj, id_item, id_psn, id_lbg, tgl_pot, flag_pot , freq_pot, jml_pot, jml_item,jenis_wakaf,lus_id, tgl_input,flag_aktif);
	}
	
	public String inputDataTemp(String notemp, User user) throws ServletException, IOException, ParseException {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return inputDataTemp.inputDatatemp(notemp,user);
	}
	
	private void prosesEmailNotify(String no_reg, String mcl_first, int i) {
		StringBuffer pesan2 = new StringBuffer();
		String pesan_aksep ;
		if(i==0){
			pesan_aksep= "berhasil di Transfer Ke Speedy";
		}else if(i==1){
			pesan_aksep= "berhasil di Transfer Ke UW";
		}else{
			pesan_aksep= "berhasil di Transfer Ke Payment";
		}
    	
	 	Date nowDate = commonDao.selectSysdate();
	 	String me_id = sequence.sequenceMeIdEmail();
	 	
//	 	 try {
//	 		 
//	 		 
//	 		EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
//					null, 0, 0, nowDate, null, 
//					true, props.getProperty("admin.ajsjava"),												
//					new String[]{"ryan@sinarmasmsiglife.co.id"}, 
//					null, 
//					null, 
//					"[E-Lions] Smile DMTM " , 
//					"\nINFO UNTUK UNDERWRITING: SPAJ"+ " dengan nomor "+FormatString.nomorSPAJ(no_reg)+" "+pesan_aksep +
//					"\n\nINFO: Pesan ini dikirim secara otomatis melalui sistem E-LIONS.", 
//					null,11);
//			
//		} catch (MailException e) {
//			// TODO Auto-generated catch block
//			logger.error("ERROR :", e);
//		} 

	}
	
	public List selectReportInputHarian(String bdate, String edate, String lsbs_id) {
		return basDao.selectReportInputHarian(bdate, edate, lsbs_id);
	}
	
	public void prosesEndorsemen(String spaj, Integer lsbs_id){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			Integer jumlah_rider = uwDao.selectCountTotalRider(spaj);
			Integer[] v_intRiderId;
			Integer[] v_intRiderNo;
			Integer mspo_provider = uwDao.selectGetMspoProvider(spaj);
			if(mspo_provider==null){
				mspo_provider=0;
			}
			
			if((jumlah_rider>0 || lsbs_id==183 || lsbs_id==193 || lsbs_id==195 || lsbs_id==189 || lsbs_id==204) && mspo_provider==2){
				Datausulan datausulan = bacDao.selectDataUsulanutama(spaj);
			
				//for (int i =0 ; i<jumlah_rider.intValue();i++){
					int i =0;
					List<Datarider> rd= (List<Datarider>) bacDao.selectDataUsulan_rider(spaj);
					List<Datarider> rd_new = (List<Datarider>) bacDao.selectDataUsulan_endors(spaj);
//					if (rd.get(i).getLsbs_id() == 822){
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
//							v_intRiderId[i] = rd_new.get(i).getLsbs_id();
//							v_intRiderNo[i] = rd_new.get(i).getLsdbs_number();
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
						//endors.setLspd_id(datausulan.getLspd_id());
						
						//(Deddy)- req rudi(13/10/2009): msen_print =1, msen_active_date = tgl_transaksi(stable link), auto rider = 0, lspd_id = 99, tgl_trans = sysdate
						endors.setMsen_print(1);
						String spaj_conversi = uwDao.selectCekSpajSebelumSurrender(spaj);
						if(spaj_conversi!=null){
							endors.setMsen_input_date(uwDao.selectBDateSLink(spaj));
							endors.setMsen_active_date(endors.getMsen_input_date());
						}else{
							endors.setMsen_input_date(datausulan.getMste_beg_date());
							endors.setMsen_active_date(datausulan.getMste_beg_date());
						}
						if(mspo_provider==2){
							endors.setMsen_auto_rider(5);	//Endors Eka Sehat Admedika
						}else{
							endors.setMsen_auto_rider(1);	//Req Rudi(5 Nov 2009) : New Business dimajuin jadi 1(sebelumnya 0).
//							endors.setMsen_auto_rider(7);	
						}
						
						
						endors.setLspd_id(99);
						endors.setMsen_tgl_trans(commonDao.selectSysdate());
						
						//endors.setMsen_tahun_ke(1);
						//endors.setMsen_premi_ke(i+1);
						//endors.setMsen_prod_ke(i+1);
						endors.setLus_id(datausulan.getLus_id());
						//endors.setMsen_ke(i+1);
						
//						if(v_intRiderId[i]==822 && v_intRiderNo[i]==1){
//							endors.setMsen_auto_rider(1);
//						}else {
//							endors.setMsen_auto_rider(0);
//						}
						
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
//						detEndors.setMsde_old6(msde_old1);
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
								if(((v_intRiderId[j]==820||v_intRiderId[j]==825) && v_intRiderNo[j]<16) ||
										  (v_intRiderId[j]==823 && (v_intRiderNo[j]<16 || v_intRiderNo[j] == 211 || v_intRiderNo[j] == 212  )) ||
								  (v_intRiderId[j]==826 && v_intRiderNo[j]<=12) ||
								  ((v_intRiderId[j]==832||v_intRiderId[j]==833) && v_intRiderNo[j]<7)){
									if(mspo_provider==2){
										if(cek_no_endors!=null){
											bacDao.deleteMstProdIns(endors.getMsen_endors_no());
											bacDao.deleteMstDetEndors(endors.getMsen_endors_no());
											bacDao.deleteMstEndors(endors.getMsen_endors_no());
											
											bacDao.insertMstEndors(endors);
											bacDao.insertMstDetEndors(detEndors);
											bacDao.insertMstProductInsEndAllProdLama(endors.getMsen_endors_no(), spaj, datausulan.getLus_id(), datausulan.getLscb_id(), datausulan.getMspr_tsi(), datausulan.getMspr_premium(), datausulan.getLsbs_id(), datausulan.getLsdbs_number());
									}else{
//										bacDao.deleteMstProdIns(endors.getMsen_endors_no());
//										bacDao.deleteMstDetEndors(endors.getMsen_endors_no());
//										bacDao.deleteMstEndors(endors.getMsen_endors_no());
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
									lsbs_id == 183 || lsbs_id == 193 || lsbs_id == 195 || lsbs_id == 189 || lsbs_id == 204){
								int x=0;
							   if(jumlah_rider!=0){
								v_intRiderId[x] = rd_new.get(x).getLsbs_id();
								v_intRiderNo[x] = rd_new.get(x).getLsdbs_number();
							   }
									if(mspo_provider==2){
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
//							if(v_intRiderId[i]==820 || v_intRiderId[i]==822){
//								if(cek_no_endors==null){
//									if(products.unitLink(Integer.toString(datausulan.getLsbs_id()))){
//										if(mspo_provider==2){
//												bacDao.insertMstEndors(endors);
//												bacDao.insertMstDetEndors(detEndors);
//												bacDao.insertMstProductInsEndAllProdLama(endors.getMsen_endors_no(), spaj, datausulan.getLus_id(), datausulan.getLscb_id(), datausulan.getMspr_tsi(), datausulan.getMspr_premium(), datausulan.getLsbs_id(), datausulan.getLsdbs_number());
//										}
//									}else if(v_intRiderId[i]==822){
//										bacDao.insertMstEndors(endors);
//										bacDao.insertMstDetEndors(detEndors);
//										bacDao.insertMstProductInsEndAllProdLama(endors.getMsen_endors_no(), spaj, datausulan.getLus_id(), datausulan.getLscb_id(), datausulan.getMspr_tsi(), datausulan.getMspr_premium(), datausulan.getLsbs_id(), datausulan.getLsdbs_number());
//									}
//									
//								}
								if(products.stableLink(Integer.toString(datausulan.getLsbs_id()))){
									bacDao.updateMsen_Endors_NoSlink(endors.getMsen_endors_no(), spaj);
								}else if(products.stableSave(datausulan.getLsbs_id(), datausulan.getLsdbs_number())){
									bacDao.updateMsen_Endors_NoSsave(endors.getMsen_endors_no(), spaj);
								}
							////}
							//insert("insertMstProductInsEnd", prod);
							
						}
					//}
				}
			}
	
	public void emailboosters(String no_reg, Integer flag, String noEndors, Integer flag_sts, String option) {
		Integer flag_email = 1;
		
		//Flag 1 -- Aktivasi Untuk Proses Pengiriman Informasi Polis Melalui Email
		if(flag == 1){
			if(option.equals("yes")){
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Date nowDate = commonDao.selectSysdate();
				Pemegang pemegang = new Pemegang();
				pemegang = bacDao.selectpp(no_reg);
				String pesan_aksep;
				String emailto = pemegang.getMspe_email();
				String emailcc = "";
				String emailbcc = "ryan@sinarmasmsiglife.co.id;avnel@sinarmasmsiglife.co.id;antasari@sinarmasmsiglife.co.id";
			 	String[] to = emailto.split(";");
			 	String[] cc = emailcc.split(";");
			 	String[] bcc = emailbcc.split(";");
				StringBuffer pesan = new StringBuffer();
				pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: grey;} table td{border: 1px solid black;}</style></head>");
				pesan.append("<body>Jakarta ,"+FormatDate.toIndonesian(nowDate)+"<br><br>");
				pesan.append("\nKepada Yth, <br>");
				pesan.append("Pemegang Polis PT Asuransi Jiwa Sinarmas MSIG Tbk.<br><br>");
				pesan.append("\nTerima kasih kepada Bapak / Ibu yang telah melakukan <b>Aktivasi</b><br><br>");
				pesan.append("\nUntuk selanjutnya secara otomatis PT Asuransi Jiwa Sinarmas MSIG Tbk. akan mengirimkan informasi<br>");
				pesan.append("polis Bapak / Ibu melalui email yang sudah di <b>Aktivasi</b> oleh Bapak / Ibu. <br><br>");
				pesan.append("Apabila Bapak / Ibu ada perubahan alamat email mohon dapat di konfirmasikan ke bagian<br>");
				pesan.append("Customer Service kami melalui email : cs@sinarmasmsiglife.co.id <br><br>");
				pesan.append("Atas perhatian dan kerjasama yang baik kami ucapkan terima kasih.<br><br>");
				pesan.append("Hormat kami,");
				pesan.append("\nPT Asuransi Jiwa Sinarmas MSIG Tbk.<br>");
				pesan.append("\n #F_1#");
				pesan.append("</body></html>");
			 	
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, nowDate, null, 
						true, "cs@sinarmasmsiglife.co.id", 
						cc, 
						cc,  
						bcc, 
						"[E-Lions] SMiLe Info ", 
						pesan.toString(), 
						null, no_reg);
			}else{
				flag_email = 2;
			}
		}
		
		//Flag 2 -- Tanda Terima Endorsment
		if(flag == 2){
			Pemegang pemegang = new Pemegang();
			pemegang = bacDao.selectpp(no_reg);
			Tertanggung tt = bacDao.selectttg(no_reg);
			String emailto = "cs@sinarmasmsiglife.co.id";
			String pesan_aksep;
			String emailbcc = "ryan@sinarmasmsiglife.co.id;avnel@sinarmasmsiglife.co.id";
	    	String[] bcc = null;
		 	String[] to = emailto.split(";");
		 	Date nowDate = commonDao.selectSysdate();
	    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			StringBuffer pesan = new StringBuffer();
			String[] cc = emailbcc.split(";");
			pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: grey;} table td{border: 1px solid black;}</style></head>");
			pesan.append("<body>Email ini terkirim otomatis oleh SYSTEM , Jangan reply/forward email ini<br><br>");
			pesan.append("\nFYI, Telah Diterima Endorsment oleh Pemegang polis dengan detail sebagai berikut:<br>");
			pesan.append("NOMOR POLIS			:"+FormatString.nomorPolis(pemegang.getMspo_policy_no())+"<br>");
			pesan.append("NOMOR ENDORSMENT	:"+noEndors+"<br>");
			pesan.append("NAMA PEMEGANG POLIS	:"+pemegang.getMcl_first()+"<br>");
			pesan.append("NAMA TERTANGGUNG	:"+tt.getMcl_first()+"<br><br>");
			pesan.append("\nTerima kasih");
			pesan.append("\n #F_1#");
			pesan.append("</body></html>");
		 	
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, nowDate, null, 
					true, "ajsjava@sinarmasmsiglife.co.id", 
					to, 
					to/*new String[]{"csbokonvensional@sinarmasmsiglife.co.id"}*/,  
					cc, 
					"[E-Lions] SMiLe Info ", 
					pesan.toString(), 
					null, no_reg);
		}
		
		//Flag 3 -- Privasi
		if(flag == 3){
			Pemegang pemegang = new Pemegang();
			pemegang = bacDao.selectpp(no_reg);
			String emailto = "cs@sinarmasmsiglife.co.id";
			String pesan_aksep;
			String emailbcc = "ryan@sinarmasmsiglife.co.id;avnel@sinarmasmsiglife.co.id;canpri@sinarmasmsiglife.co.id";
	    	String[] bcc = null;
		 	String[] to = emailto.split(";");
		 	Date nowDate = commonDao.selectSysdate();
	    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			StringBuffer pesan = new StringBuffer();
			String[] cc = emailbcc.split(";");
			if(flag_sts == 3){//setuju
				pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: grey;} table td{border: 1px solid black;}</style></head>");
				pesan.append("<body> Konfirmasi Setuju melanjutkan pembayaran premi privasi tahun ke-11 sampai tahun ke-20<br>");
				pesan.append("NOMOR POLIS	:"+FormatString.nomorPolis(pemegang.getMspo_policy_no())+"<br>");
				pesan.append("NAMA PEMEGANG POLIS	:"+pemegang.getMcl_first()+"<br>");
				pesan.append("\nTerima kasih");
				pesan.append("\n #F_1#");
				pesan.append("</body></html>");
			}else{//tidak setuju
				pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: grey;} table td{border: 1px solid black;}</style></head>");
				pesan.append("<body> Konfirmasi Tidak Setuju melanjutkan pembayaran premi privasi tahun ke-11 sampai tahun ke-20<br>");
				pesan.append("NOMOR POLIS	:"+FormatString.nomorPolis(pemegang.getMspo_policy_no())+"<br>");
				pesan.append("NAMA PEMEGANG POLIS	:"+pemegang.getMcl_first()+"<br>");
				pesan.append("\nTerima kasih");
				pesan.append("\n #F_1#");
				pesan.append("</body></html>");
			}
		 	
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, nowDate, null, 
					true, "ajsjava@sinarmasmsiglife.co.id", 
					to, 
					to/*new String[]{"csbokonvensional@sinarmasmsiglife.co.id"}*/,  
					cc, 
					"[E-Lions] SMiLe Info ", 
					pesan.toString(), 
					null, no_reg);
		}
		
		//Flag 4 -- SSH SSP
		if(flag == 4){
			Pemegang pemegang = new Pemegang();
			pemegang = bacDao.selectpp(no_reg);
			String emailto = "cs@sinarmasmsiglife.co.id";
			String pesan_aksep;
			String emailbcc = "ryan@sinarmasmsiglife.co.id;avnel@sinarmasmsiglife.co.id;canpri@sinarmasmsiglife.co.id";
	    	String[] bcc = null;
		 	String[] to = emailto.split(";");
		 	Date nowDate = commonDao.selectSysdate();
	    	DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			StringBuffer pesan = new StringBuffer();
			String[] cc = emailbcc.split(";");
		
			pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: grey;} table td{border: 1px solid black;}</style></head>");
			pesan.append("<body>KONFIRMASI PERPANJANGAN<br><br>");
			pesan.append("\nSaya setuju untuk memperpanjang program Asuransi kesehatan SUPER PROTECTION<br>");
			pesan.append("\nNAMA PEMEGANG POLIS	:"+pemegang.getMcl_first()+"<br>");
			pesan.append("\nNOMOR POLIS	:"+FormatString.nomorPolis(pemegang.getMspo_policy_no())+"<br>");
			pesan.append("\nTerima kasih");
			pesan.append("\n #F_1#");
			pesan.append("</body></html>");
	
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, nowDate, null, 
					true, "ajsjava@sinarmasmsiglife.co.id", 
					to, 
					to/*new String[]{"csbokonvensional@sinarmasmsiglife.co.id"}*/,  
					cc, 
					"[E-Lions] SMiLe Info ", 
					pesan.toString(), 
					null, no_reg);
		}
		
		//Flag 6 -- Konfirmasi Perubahan Alamat Nasabah
		if(flag == 6){
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		 	Date nowDate = commonDao.selectSysdate();
			Pemegang pemegang = new Pemegang();
			pemegang = bacDao.selectpp(no_reg);
			String pesan_aksep;
			String emailto = "cs@sinarmasmsiglife.co.id";
			String emailcc = ""; //"csbokonvensional@sinarmasmsiglife.co.id"
			String emailbcc = "ryan@sinarmasmsiglife.co.id;avnel@sinarmasmsiglife.co.id;antasari@sinarmasmsiglife.co.id";
		 	String[] to = emailto.split(";");
		 	String[] cc = emailcc.split(";");
		 	String[] bcc = emailbcc.split(";");
			StringBuffer pesan = new StringBuffer();
			pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: grey;} table td{border: 1px solid black;}</style></head>");
			pesan.append("<body>INFORMASI PERUBAHAN ALAMAT <br><br>");
			pesan.append("\n<br>");
			pesan.append("\nNAMA PEMEGANG POLIS	:"+pemegang.getMcl_first()+"<br>");
			pesan.append("\nNOMOR POLIS	:"+FormatString.nomorPolis(pemegang.getMspo_policy_no())+"<br>");
			pesan.append("\nTerima kasih");
			pesan.append("\n #F_1#");
			pesan.append("</body></html>");
		 	
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, nowDate, null, 
					true, "ajsjava@sinarmasmsiglife.co.id", 
					cc, 
					cc,  
					bcc, 
					"[E-Lions] SMiLe Info ", 
					pesan.toString(), 
					null, no_reg);
		}
		
		updateEmailFlagBooster(no_reg, flag_email);
		
	}
	
	//MANTA - Scheduler untuk Proses Refund Premi
	public void schedulerRefundPremiAuto(){
		String msh_name = "SCHEDULER REFUND PREMI AUTO (E-LIONS)";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerRefundPremiAuto(msh_name);
		}
	}
	
	public String selectNoDuplikatUlangan(String spaj, String flag) {
		return commonDao.selectNoDuplikatUlangan(spaj, flag);
	}
	
	public void updateEmailFlagBooster( String spaj, Integer flag ){
		this.uwDao.updateEmailFlagBooster( spaj, flag );
	}
	
	public Date selectTglRkFromTbank(String no_pre){
		return worksiteDao.selectTglRkFromTbank(no_pre);
	}
	
	public Integer selectCekBayarKomisi(String reg_spaj){
		return refundDao.selectCekBayarKomisi(reg_spaj);
	}
	
	public void insert_aaji_calon_karyawan(Map m){
		this.rekruitmentDao.insert_aaji_calon_karyawan(m);
	}
	
	public void updateAAJICalonKaryawan(int blacklist, int perusahaan_lama, int status_join, String ket, String ktp){
		this.rekruitmentDao.updateAAJICalonKaryawan(blacklist, perusahaan_lama, status_join, ket, ktp);
	}
	
	public List selectAAJICalonKaryawan(Date tglA, Date tglB, String ktp){
		return this.rekruitmentDao.selectAAJICalonKaryawan(tglA, tglB, ktp);
	}
	
	public List selectProsesAAJICalonKaryawan(){
		return this.rekruitmentDao.selectProsesAAJICalonKaryawan();
	}
	
	public void schedulerStatusAAJICalonKaryawan(){
		String msh_name = "SCHEDULER STATUS AAJI CALON KARYAWAN";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			//schedulerDao.schedulerStatusAAJICalonKaryawan(msh_name);
		}
	}
	
	public List selectLstAddrRegion(String nm_kota){
		return this.basDao.selectLstAddrRegion(nm_kota);
	}
	
	public void updateLstAddrRegion(String lar_id, String nama, String alamat, String admin,
			String telp, String email, String no, String aktif, String lar_speedy){
		this.basDao.updateLstAddrRegion(lar_id, nama, alamat, admin, telp, email, no, aktif, lar_speedy);
	}
	
	public void insertLstAddrRegionHist(String lar_id, String lus_id, String keterangan){
		this.basDao.insertLstAddrRegionHist(lar_id, lus_id, keterangan);
	}
	
	public void insertLstAutobetSpeedyHist(String lar_id, String lar_telpon, String lar_speedy, String tgl_autodebet, String status, String keterangan){
		this.basDao.insertLstAutobetSpeedyHist(lar_id, lar_telpon, lar_speedy, tgl_autodebet, status, keterangan);
	}
	
	public List selectAutodebetSpeedy(String tglAwal, String tglAkhir, String status){
		return this.basDao.selectAutodebetSpeedy(tglAwal, tglAkhir, status);
	}
	
	/**
	 * Scheduler retur KPL (02/04/2014)
	 * @author 	Canpri
	 */
	public void schedulerReturKpl(){
		String msh_name = "AUTOMAIL RETUR KPL";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerReturKpl(msh_name);
		}
	}

	/**
	 * Report Gagal debet dan outstanding aging (02/04/2014)
	 * @author 	Canpri
	 */
	public List selectReportGagalDebetdanAging(String bdate, String edate, String lca_id, String jenis) {
		return basDao.selectReportGagalDebetdanAging(bdate, edate, lca_id, jenis);
	}
	
	public Integer selectValidForInput(String lus_id) {
		return uwDao.selectValidForInput(lus_id);
	}
	
	
	//request pabaliet - ryan
	public String selectNamaBankData(String id_bank)
	{
		return this.uwDao.selectNamaBankData( id_bank );
	}

	public List selectBillingInformation(String reg_spaj, Integer tahun_ke,
			Integer premi_ke) {
		return this.uwDao.selectBillingInformation2(reg_spaj, tahun_ke, premi_ke);
	}

	public List <DropDown> selectProdKes(String spaj) {		
		return this.uwDao.selectProdKes(spaj);
	}
	
	public void updateKloterKePeserta( String spaj, String noKloter){
    	this.uwDao.updateKloterKePeserta( spaj,noKloter );
	}
	
	public void updateUmurMstPeserta( String spaj, Integer umur, String noreg){
    	this.uwDao.updateUmurMstPeserta( spaj,umur,noreg );
	}	
	
	public String sequenceMeIdEmail(){
		return this.sequenceMeIdEmail();
	}

	public List select_produkKesehatanDitolak(String kode_prod,
			String number_prod,String spaj) {		
		return this.uwDao.selectDataKes(kode_prod,number_prod,spaj);
	}
	
	public ArrayList<String> selectDaftarSpajYangMauDiProses(){
		return Common.serializableList(commonDao.selectDaftarSpajYangMauDiProses());
	}
	
	public void ProsesJurnalIndividuKetinggalanNoPre(List<String> daftarEspeaje, User user){
		try {
			int i = 0;
			for(String spaj : daftarEspeaje){
//				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				transferPolis.testJurnalProduksiIndividuKetinggalanNoPre(spaj, user);
				i++;
			}
		}catch (Exception e) {
			logger.error("ERROR :", e);
		}
		
	}
	public String sequenceNoSuratTolakKesehatan(int msco_number, String lca) {
		return this.sequence.sequenceNoSuratTolakKesehatan(msco_number,lca);
	}
	
	public void prosesTransferAdmedika(String spaj , String noKloter, String polis) {
		// update2 in flag n penandanya
		//PrintPolisPerjanjianAgent printPolis = new PrintPolisPerjanjianAgent();
		this.uwDao.updateKloterKePeserta( spaj,noKloter );
		this.uwDao.updateKloterKePeserta2( polis,noKloter );
	}
	
	public void prosesTransferAdmedika2(String spaj , String noKloter, String polis) {
		
	
		Date sysdate = commonDao.selectSysdateTrunc();
		String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+noKloter.trim();
		String outputName = props.getProperty("pdf.dir.export")+"\\"+noKloter.trim()+"\\FormBiayaAdmedika"+noKloter.trim()+".pdf";
		
		try{
			
			File file = null;
			PdfReader reader=null ;
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+noKloter.trim());
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
			
//			 adding some metadata
			HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "Admedika");
			moreInfo.put("Subject", "EKA SEHAT Admedika");
			
			PdfContentByte over;
			BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\TIMES.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			file = new File(outputName);
			reader = new PdfReader("\\\\ebserver\\pdfind\\Template\\Endors_Eka_Sehat\\MargePermintaanBiaya.pdf");
			PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(file));
			
			stamp.setMoreInfo(moreInfo);
			
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman,11);

				
				over = stamp.getOverContent(1);
				over.beginText();
				over.setFontAndSize(times_new_roman,11);
				

				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Jumlah", 240, 760, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Terbilang", 399, 711, 0);
				over.endText();
				over.stroke();
			
			if(stamp!=null){
				stamp.close();	
			}
		}catch (Exception e) {
			logger.error("ERROR :", e);
    	}
		
		// ngirim E-Mail
		File sourceFile = new File(outputName);
		 ArrayList<File> attachments = new ArrayList<File>();
		
		 attachments.add(sourceFile);
		EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
				null, 0, 0, commonDao.selectSysdate(), null, 
				true, "ajsjava@sinarmasmsiglife.co.id", 
				new String[]{"ryan@sinarmasmsiglife.co.id"}, 
				new String[]{"ryan@sinarmasmsiglife.co.id"}/*new String[]{"csbokonvensional@sinarmasmsiglife.co.id"}*/,  
				new String[]{"ryan@sinarmasmsiglife.co.id"}, 
				"[E-Lions] SMiLe Info ", 
				"Berikut Lampiran Attachment Form Pembayaran Biaya", 
				attachments, null);
		
	}
	
	public List select_benef_temp(String spaj)
	{
		return  this.bacDao.select_benef_temp(spaj);
	}
	
	public Rekening_client select_rek_client_temp(String no_temp){
		Rekening_client r = this.bacDao.select_rek_client_temp(no_temp);
		if(r == null) return new Rekening_client(); else return r;
	}	
	
	/**
	 * Proses Kirim Exclude ke Admedika (21/05/2014)
	 * @author 	Lufi
	 * @param payorId 
	 */
	
	public void prosesICDExclude(String desc, String dp, Command command, User currentUser, String payorId,String cardNo) {
		 ArrayList mapIcd = new ArrayList();
		 String dir = props.getProperty("pdf.dir.export") + "\\Admedika_Exclude\\";
		 String reg_spaj=null;
		 Icd icdTemp =new Icd();
		 
		 for(int i=0;i<command.getLsIcd().size();i++){
			icdTemp =(Icd)command.getLsIcd().get(i); 
			icdTemp.setLic_desc(uwDao.selectIcdDesc(icdTemp.getLic_id()));
			icdTemp.setLic_category(uwDao.selectIcdCategory(icdTemp.getLic_id()));
			icdTemp.setLic_type(3);
			icdTemp.setMste_insured_no(0);
			icdTemp.setMpa_number(i+1);
			reg_spaj=icdTemp.getReg_spaj();
			Map m=new HashMap();
			m.put("LIC_ID",icdTemp.getLic_id());
			m.put("LIC_DESC",icdTemp.getLic_desc());
			m.put("LIC_TYPE",icdTemp.getLic_type());
			m.put("LIC_CATEGORY",icdTemp.getLic_category());
			uwDao.insertMstDetIcd(icdTemp);
			mapIcd.add(m);
		 }
		 
		 String outputFilename = "exclude_" + reg_spaj + ".xls";
		 File sourceFile = new File(dir+"\\"+outputFilename);
		 ArrayList<File> attachments = new ArrayList<File>();
		
		 attachments.add(sourceFile);
		 String emailto ="dataindividu@admedika.co.id;customer.care@admedika.co.id";
	     String pesan_aksep ;
	     String emailbcc = "Underwriting@sinarmasmsiglife.co.id;Claim@sinarmasmsiglife.co.id;Provider@sinarmasmsiglife.co.id";
	     String []bcc=null;
		 String[] to = emailto.split(";");
		 Date nowDate = commonDao.selectSysdate();			 
	     DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		 StringBuffer pesan = new StringBuffer();
		 String[] cc = emailbcc.split(";");
		 HashMap dataPolis=Common.serializableMap(uwDao.selectPemegangExclude(reg_spaj,dp));
		 HashMap<String, Object> params = new HashMap<String, Object>();
		 params.put("payorId",payorId);
		 pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: yellow;} table td{border: 1px solid black;}</style></head>");
		 pesan.append("<body bgcolor='#ffffc9'><br><br>");
		 pesan.append("Dear admedika team, <br>");
		 pesan.append("Terlampir kami sampaikan data pengecualian Diagnosa.<br><br>");	
		 pesan.append("<b>MEMBER ENQUIRY</b> <br>");
		 pesan.append("<ul><li>Card Member\t\t:\t"+cardNo+"</li>");
		 pesan.append("<li>Members Name\t\t:\t"+dataPolis.get("PP").toString()+"</li>");
		 pesan.append("<li>Payor Members ID\t\t:\t"+payorId+"</li>");
   		 pesan.append("<li>Policy Number\t\t:\t"+FormatString.nomorPolis(dataPolis.get("NO_POLIS").toString())+"</li>");
   		 pesan.append("<li>Payor\t\t\t:\t"+"ASURANSI JIWA SINARMAS MSIG"+"</li>");
   		 pesan.append("<li>Corporate\t\t\t:\t"+dataPolis.get("NAMA_PLAN").toString()+"</li>");
   		 pesan.append("<li>Exclusion Coverage\t\t:\t"+desc+"</li>");
   		 pesan.append("<li>Begdate\t\t\t:\t"+dataPolis.get("BEGDATE").toString()+"</li></ul> <br><br>");
   		 pesan.append("Mohon Diproses, <br><br>");
   		 pesan.append("Terima Kasih, <br>");
  		 pesan.append( currentUser.getLus_full_name()+ "<br>");
   		 pesan.append("</body></html>");
   		 
   		
   		 
   		try {
			JasperUtils.exportReportToXls(props.getProperty("report.list_exclude")+".jasper",dir,outputFilename,params, mapIcd,null);
			EmailPool.send("SMilE E-Lions", 1, 1, 0, 0, 
					null, 0, 0, commonDao.selectSysdate(), null, 
					true,
					currentUser.getEmail(), 
					to, 
					cc, 
					new String[]{"ryan@sinarmasmsiglife.co.id"}, 
					dataPolis.get("PP").toString()+" "+dataPolis.get("NO_POLIS").toString()+""+"EXCLUSION", 
					pesan.toString(), 
					attachments, reg_spaj);
		} catch (IOException e) {			
			logger.error("ERROR :", e);
		} catch (JRException e) {			
			logger.error("ERROR :", e);
		} catch (MailException e){			
			logger.error("ERROR :", e);
		} 
   		
   		uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "KIRIM EXCLUDE KE ADMEDIKA", reg_spaj, 0);		
	}   
	
	public String processFormPromo(CommandControlSpaj cmd, User currentUser) throws DataAccessException, IOException {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		if("save".equals(cmd.getSubmitMode())) {
			return basDao.processNewFormPromo(cmd, currentUser);
		}else if("cancel".equals(cmd.getSubmitMode())) {
			return basDao.processCancelFormPromo(cmd, currentUser);
		}/*else if("approve".equals(cmd.getSubmitMode())) {
			return basDao.processApprovalFormBrosur(cmd, currentUser);
		}*/else if("reject".equals(cmd.getSubmitMode())) {
			return basDao.processRejectFormPromo(cmd, currentUser);
		}else if("send".equals(cmd.getSubmitMode())) {
			return basDao.processSendFormPromo(cmd, currentUser);
		}else if("received".equals(cmd.getSubmitMode())) {
			return basDao.processReceiveFormPromo(cmd, currentUser);
		}
		return "ERROR";
	}

	public String updateKantorPemasaran(AddressRegion cmd, User currentUser) {
		if("update".equals(cmd.getSubmitMode())) {
			return basDao.updateKantorPemasaran(cmd, currentUser);
		}
		
		return "ERROR";
	}
	
	public String insertMonitoringSpaj(String no_blanko, String msag_id, String pemegang,String informasi, String fdm, User currentUser, Integer jenis, String note, String jenis_further, Date tgl_kembali_ke_agen, 
			Date tgl_terima_agen, String keterangan_further, Date tgl_further, Integer flag_further, String emailcc) throws Exception {
		return basDao.insertMonitoringSpaj(no_blanko, msag_id, pemegang,informasi, fdm, currentUser, jenis, note, jenis_further, tgl_kembali_ke_agen, tgl_terima_agen, keterangan_further, tgl_further, flag_further, emailcc);
	}
	
	public void updateMonitoringSpaj(String no_blanko, Integer jenis, String type, Date tgl_kembali_ke_agen, Date tgl_terima_dari_agen, String jenis_further, String keterangan_further,String msag_id , String nama_pemegang, String emailcc) {
		basDao.updateMonitoringSpaj(no_blanko, jenis, type, tgl_kembali_ke_agen, tgl_terima_dari_agen, jenis_further, keterangan_further,msag_id,nama_pemegang,emailcc);
	}
	
	public Map selectBlankoMonitoringSpaj(String no_blanko, String type){
		return (HashMap) basDao.selectBlankoMonitoringSpaj(no_blanko, type);
	}
	
	public Map selectMstSpajAoFurtherOrNonFurther(String no_blanko, String status ){
		return basDao.selectMstSpajAoFurtherOrNonFurther(no_blanko, status);
	}

	public List selectReportMonitoringSPAJ(String bdate, String edate, String lus_id, Integer jn_report, Integer admin, String searchList, String searchText ) {
		return basDao.selectReportMonitoringSPAJ(bdate, edate, lus_id, jn_report, admin, searchList, searchText );
	}
	
	public List selectReportMonitoringSPAJFurther(String bdate, String edate, String lus_id, Integer admin, Integer jn_report) {
		return basDao.selectReportMonitoringSPAJFurther(bdate, edate, lus_id, admin, jn_report);
	}

	//canpri, select history update lst_addr_region
	public List<Map> selectHistoryUpdateRegion(String lar_id, String order) {
		return basDao.selectHistoryUpdateRegion(lar_id, order);
	}
	
	public List selectLstAgenAKM(String lca_id) {
		return bacDao.selectLstAgenAKM(lca_id);
	}
	
	/**
	 * Scheduler NOTIF SURAT DOMISILI (02/04/2014)
	 * @author 	Canpri
	 */
	public void schedulerSuratDomisili(){
		String msh_name = "AUTOMAIL NOTIF SURAT DOMISILI";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerSuratDomisili(msh_name);
		}
	}
	
	public Double selectGetKursJb(Date tglKurs, String asJb) {
		return (Double) uwDao.selectGetKursJb(tglKurs, asJb);
	}
	
	//MANTA - Scheduler untuk Follow Up SPAJ yang statusnya masih Further Requirement
	public void schedulerFollowUpFR(){
		String msh_name = "SCHEDULER FOLLOW UP SPAJ FR (E-LIONS)";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerFollowUpFR(msh_name);
		}
	}
	
	public String uploadReferralBSM(User currentUser, Upload upload) throws IOException{
	    String ls_kode_cabang = "";
	    Integer ll_update = 0;
	    Integer ll_insert = 0;
	    SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
	    StringBuffer err = new StringBuffer();
	    SimpleDateFormat df2=new SimpleDateFormat("dd/MM/yyyy ");
		InputStreamReader is = null;
		BufferedReader br = null;
		Date tglAktif=null,tglNonAktif=null,tglUpdate=null;
		String fileName = upload.getFile1().getOriginalFilename();
		Date sysdate = commonDao.selectSysdate();
        SimpleDateFormat df3=new SimpleDateFormat("yyyyMMddhhmmss");
        String id = df3.format(sysdate);
		String pathFile = "\\\\ebserver\\pdfind\\BSIM\\Upload_Referral\\"+id+"_"+fileName.trim();
		
		try {
//			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			is = new InputStreamReader(upload.getFile1().getInputStream());
			File outputFile = new File(pathFile);
			FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
			br = new BufferedReader(is);
			String line = br.readLine();
	        while(line != null) {
	        	 String f[] = line.split("\t");
	        	 String kd_cabang = f[0];
	        	 String nm_cabang = f[1];
	        	 String kd_agent = f[2];
	        	 String nm_agent = f[3];

	             if(!kd_cabang.equals("kd_cabang")) {
	            	 Integer ref = (f[4].equals("")? null : Integer.parseInt(f[4]));
	        	     Integer aktif = (f[5].equals("")? null : Integer.parseInt(f[5]));
	        	     String pos_code = (f[6].equals("")? null : (f[6]));
	        	     String tgl_aktif = (f[7].equals("")? null : (f[7]));
	        	     String tgl_update = (f[8].equals("")? null : (f[8]));
	        	     String tgl_non_aktif = (f[9].equals("")? null : (f[9]));
	        	     String flag_update = (f[10].equals("")? null : (f[10]));
	        	     String nik = (f[11].equals("-")? null : (f[11]));
	        	     String prenik = (f[12].equals("-")? null : (f[12]));        	    
	        	     if(tgl_aktif!=null && !tgl_aktif.equals("")) tglAktif = df.parse(tgl_aktif);
	        	     if(tgl_non_aktif!=null && !tgl_non_aktif.equals(""))tglNonAktif=df.parse(tgl_non_aktif);
	        	     if(tgl_update!=null &&  !tgl_update.equals(""))tglUpdate=df.parse(tgl_update);
	            	 Integer jumlahcabangByKode = commonDao.selectCountLstCabangBiiByKode(kd_cabang,currentUser.getJn_bank());
		             Integer tes="U,D".indexOf(flag_update);
		             if(jumlahcabangByKode > 0){
		            	 commonDao.updateLstCabangBii(kd_cabang, nm_cabang,currentUser.getJn_bank());
		             }else {
		            	 try{
			            	 Integer jumlahcabang =commonDao.selectCountLstCabangBii(kd_cabang,currentUser.getJn_bank());
			            	 jumlahcabang = jumlahcabang+1;
			            	 ls_kode_cabang = "S" + FormatString.rpad("0", jumlahcabang.toString(), 3);
			            	 commonDao.insertLstCabangBii(ls_kode_cabang, nm_cabang, 4, 2, kd_cabang, currentUser.getJn_bank());
		            	 }catch(Exception e){
		            		 //logger.error("ERROR :", e);
		            		 throw new RuntimeException("gagal");
		            	 }
		             }		             
		             
		             Integer lrb_id ;
		             //untuk yg flagnya U atau D cek dulu datanya sudah ada atau belum, kalau belum insert baru
		             if("U,D".indexOf(flag_update)>=0){
		            	 String lcb_no = commonDao.selectLstReferralBiiLcbNo(kd_cabang,currentUser.getJn_bank());
		            	 Integer jumlahreferralByKode =commonDao.selectCountLstReffBiiByKode(kd_agent,currentUser.getJn_bank());
//		            	 if(jumlahreferralByKode<0){
//		            		 lrb_id= commonDao.selectLstReffBiiMaxLrbId();
//			            	 lrb_id = lrb_id + 1;
//			            	 commonDao.insertLstReffBiiBSM(lrb_id, nm_agent, lcb_no, aktif, kd_agent, ref,tglAktif,tglNonAktif,pos_code,tglUpdate,currentUser.getJn_bank());
//			            	 ll_insert = ll_insert + 1;
//		            	 }else{
		            	 if(!kd_agent.equals("")){
		            		 commonDao.updateLstReffBii(lcb_no, ref, aktif, kd_agent, nm_agent,tglAktif,tglNonAktif,pos_code,tglUpdate,currentUser.getJn_bank(),nik,prenik);
		            		 ll_update = ll_update + 1;
		            	 }else{
		            		 err.append("< >- Data Atas Nama : "+nm_agent +" Tidak ada Kode agentnya");
		            	 }
			             
			             //tambahan jika refferal telemarketing bsim
			             if(currentUser.getJn_bank()==2){
				             if(pos_code.trim().equals("36")){
				            	 if(nik==null && prenik!=null)nik=prenik;
				            	 //02 callcenter_code khusus TM BSIM
				            	 Integer count_tm_id = commonDao.selectCountTmid(nik,"02");
//				            	 if(count_tm_id==0){
//				            		 commonDao.insertLstSalesTm(nik,"02",nm_agent,aktif);
//				            	 }else{
//				            		 commonDao.updateLstSalesTm(nik,"02",nm_agent,aktif);
//				            	 }
				             }
			             }
//			             }
			             
		             }else {
		            	 lrb_id= commonDao.selectLstReffBiiMaxLrbId();
		            	 lrb_id = lrb_id + 1;
		            	 Integer jumlahreferralByKode =commonDao.selectCountLstReffBiiByKode(kd_agent,currentUser.getJn_bank());
		            	 String lcb_no = commonDao.selectLstReferralBiiLcbNo(kd_cabang,currentUser.getJn_bank());
		            	 if(jumlahreferralByKode < 1){
		            		 commonDao.insertLstReffBiiBSM(lrb_id, nm_agent, lcb_no, aktif, kd_agent, ref,tglAktif,tglNonAktif,pos_code,tglUpdate,currentUser.getJn_bank(), currentUser.getLus_id(),nik,prenik);
		            		 ll_insert = ll_insert + 1;
		            		 if(currentUser.getJn_bank()==2){
		            			 if( pos_code.equals("36")){
		            				 if(nik==null && prenik!=null)nik=prenik;
		            				 //commonDao.insertLstSalesTm(nik,"02",nm_agent,aktif);				            	
		            			 }
		            		 }
		            	 }else{
		            		 err.append("< >- Data Upload  Kode Agent : "+kd_agent+" Sudah Pernah di Upload");
		            	 }
		             }
	             }
	             line = br.readLine();
	        }
	        if(br!=null){
	        	br.close();
	        }
	        //commonDao.insertLampiranMstHistoryUpload( id, id, id, fileName, "", null, "REFFERAL BSIM", "AKTIF", null,Integer.parseInt(currentUser.getLus_id()), "UPLOAD REFF BSIM", pathFile, "", "", sysdate);
			
		}
		catch( Exception e) { 
			//logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			throw new RuntimeException(e.getLocalizedMessage());
	    }
		finally{
			if(br!=null){
				br.close();
			}
			if(is!=null){
				is.close();
			}
		}				
        return "Berhasil Update Data Sebanyak " +ll_update+ " dan Menambah Data Sebanyak " +ll_insert+ 
        		" Info : "+err;
	}
	public List selectDataListPendingBas(String cabang, String bdate,
			String edate) {		
		return basDao.selectDataListPendingBas(cabang,bdate,edate);
	}


	public List selectDataPersentasePendingBas(String cabang, String bdate,
			String edate) {
		
		return basDao.selectDataPersentasePendingBas(cabang,bdate,edate);
	}

	public Integer selectStatusSpajBas(String spaj) {		
		return basDao.selectStatusSpajBas(spaj);
	}

	public Command prosesStatusBas(Command command, HttpServletRequest request,
			BindException err) {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		command.setSpaj(request.getParameter("spaj"));
		String desc[] = new String[request.getParameterValues("textarea").length];		
		String status;
		String statusAkhir;
		Integer lssaId;
		int suc = 0;
		String[] cc;String[] to;String[] toExtra;
		String emailTo="";String ccemail="";
		List<File> attachments = new ArrayList<File>();
		//ambil attachment untuk FR
		String outputDir = props.getProperty("attachment.mailpool.dir") +"\\" +command.getLcaIdPp()+"\\"+command.getSpaj();
		File dirFile = new File(outputDir);
		if(dirFile.exists()) {
			attachments.add(command.getAttachment());}
		
		String flagEmail = request.getParameter("flagEmail");		
		desc = request.getParameterValues("textarea");
		
		if (request.getParameterValues("status") != null) {			
			status = request.getParameter("status");					
			lssaId=Integer.parseInt(status);
		} else {
			String temp = request.getParameter("temp");			
			lssaId=Integer.parseInt(temp);
		}
		String deskripsi = desc[desc.length - 1];
		
		deskripsi = deskripsi.toUpperCase();
		if(flagEmail.equals("1")){//untuk further bas krim email
		    String emailToExtra=request.getParameter("emailTo");
		    toExtra=emailToExtra.split(";");
		    String sub_desc=uwDao.selectDetailStatus(command.getLiAksep(),command.getSubLiAksep());
		    
		    if ("09,58".indexOf(command.getLcaIdPp()) >= 0 ) {// Khusus Simpol
		    	String emailCabangBank = uwDao.selectEmailCabangBankSinarmas(command.getSpaj());
		    	String emailAoBank = uwDao.selectEmailAoBankSinarmas(command.getSpaj());
		    	Map emailBAC = uwDao.selectReffNonLisensi(command.getSpaj()); 
		    	if(emailCabangBank != null) emailTo = emailTo.concat(emailCabangBank+";");
		    	if(emailAoBank != null) emailTo = emailTo.concat( emailAoBank +";" );
		    	if(emailBAC !=null){
		    		String emailBacAjs = (String) emailBAC.get("EMAIL_AJS");
		    		if(emailBacAjs != null)emailTo = emailTo.concat(emailBacAjs+";");		    		
		    		String emailPIC = (String) emailBAC.get("EMAIL_PIC");
		    		if(emailPIC !=null)emailTo = emailTo.concat(emailPIC+";");
		    		String emailReff = (String) emailBAC.get("EMAIL_REFF");
		    		if(emailReff !=null)emailTo = emailTo.concat(emailReff+";");
		    		
		    	}
		    	if(!currentUser.getEmail().equals(""))emailTo = emailTo.concat( currentUser.getEmail()+";");
		    	if(!emailToExtra.equals(""))emailTo = emailTo.concat( emailToExtra +";");
//		    	ccemail="simpol@sinarmasmsiglife.co.id";
		    	ccemail="basbancass@sinarmasmsiglife.co.id";
		    } else {// Khusus Selain Simpol
		    	String emailAdmin = uwDao.selectEmailAdmin(command.getSpaj());
		    	Map agen = uwDao.selectEmailAgen(command.getSpaj());
				String emailAgen = (String) agen.get("MSPE_EMAIL");
		    	if (emailAdmin != null){
					emailTo = emailTo.concat(emailAdmin +";" );
				}
		    	if (emailAgen != null){
					emailTo = emailTo.concat( emailAgen+ ";");
				}
		    	if(!currentUser.getEmail().equals(""))emailTo = emailTo.concat(currentUser.getEmail()+";");
		    	if(!emailToExtra.equals(""))emailTo = emailTo.concat(  emailToExtra +";");
		    	ccemail="BAS@sinarmasmsiglife.co.id";
		    	 
		    }
		    to= emailTo.split(";");
		    cc= ccemail.split(";");
		    String css = props.getProperty("email.uw.css.satu")
			             + props.getProperty("email.uw.css.dua");
		    String footer = props.getProperty("email.uw.footer");
		    
		    HashMap info = Common.serializableMap(uwDao.selectInformasiCabangFromSpaj(command.getSpaj()));		    
			String cabang = (String) info.get("NAMA_CABANG");	
			HashMap gen = Common.serializableMap(uwDao.selectEmailAgen(command.getSpaj()));
			String namaAgen = (String) gen.get("MCL_FIRST");
			DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");			
			List lsProduk=akseptasiDao.selectMstProductInsured(command.getSpaj());
			Product produk=(Product)lsProduk.get(0);
			List<DropDown> ListImage = new ArrayList<DropDown>();
			ListImage.add(new DropDown("myLogo",props.getProperty("pdf.dir.syaratpolis")+"\\images\\ekalife.gif"));
			String pesan = css+"<table width=100% >"
						+ (cabang!=null	? 	"<tr><td>Cabang   	</td><td>:</td><td>" + cabang + "</td></tr>" : "")+ "</td><td> Tanggal:" + commonDao.selectSysdateTrunc() + "</td></tr>"						
						+ "<tr><td>Agen   		</td><td>:</td><td>" + namaAgen + "</td></tr>"
						+ "<tr><td>No. Spaj 	</td><td>:</td><td>" + FormatString.nomorPolis(command.getSpaj()) + "<td></tr>"
						+ "<tr><td>Produk		</td><td>:</td><td>"+produk.getLsdbs_name()+"("+produk.getLsbs_id()+")"+"<td colspan=2></tr>" 
						+ "<tr><td>A/N			</td><td>:</td><td colspan=2>" + command.getNamaPemegang() + " (Pemegang) -- " + command.getNamaTertanggung() + " (Tertanggung) " + "</td></tr>" 						
						+ "<tr><td>UP</td><td>:</td><td>"+produk.getLku_symbol()+" "+ df.format(command.getMsprTsi()) +"</td></tr>"
						+ "<tr><td>Premi</td><td>:</td><td>"+produk.getLku_symbol()+" "+ df.format(command.getMsprPremium())+"</td></tr>"
						+ "<tr><td>Cara Bayar</td><td>:</td><td>"+command.getLscbPayMode()+"</td></tr>"		
						+ "<tr><td>Status   	</td><td>:</td><td>" + "INCOMPLETE DOCUMENT" + "<td></tr>" 
						+ "<tr><td>Detail Status   	</td><td>:</td><td>" + sub_desc + "<td></tr>" 
						+ "<tr><td>Keterangan	</td><td>:</td><td>" + deskripsi + "<td></tr>" 
						+ "<tr><td>User Proses 	</td><td>:</td><td>" + currentUser.getName() + " [" + currentUser.getDept() + " ]<td></tr></br>" 									
					    +"</table>";
		EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, Integer.parseInt(currentUser.getLus_id()), commonDao.selectSysdateTrunc(),
				null, true, currentUser.getEmail(), to, cc, new String[]{"ryan@sinarmasmsiglife.co.id"}, "Kelengkapan Dokumen Awal", pesan, attachments, command.getSpaj());

		}
		uwDao.updateMstInsuredStatusBas(command.getSpaj(),new Integer(1),command.getLiAksep(),1,null,null);
		uwDao.insertMstPositionSpajWithSubIdBas(currentUser.getLus_id(), deskripsi, command.getSpaj(), command.getSubLiAksep(),null);
		
		return command;
	}
	
	public List selectMstPositionSpajForStatusBas(String spaj) {
		return this.bacDao.selectMstPositionSpajForStatusBas(spaj);
	}	

	public List<DropDown> selectEmailAutoComplete(String spaj) {
		// TODO Auto-generated method stub
		return bacDao.selectEmailAutoComplete(spaj);
	}
	
	public List selectFollowupKategori(String s_bdate, String s_edate, String s_jenis, String s_jn_tgl) {
		return basDao.selectFollowupKategori(s_bdate, s_edate, s_jenis, s_jn_tgl);
	}
	
	public void insertVisaCampTertinggal(String reg_spaj, User currentUser ){
	//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		HashMap datavisa =Common.serializableMap(uwDao.selectMstPaymentVisa(reg_spaj));
		String businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(reg_spaj), 2);
		if (datavisa != null){
				Integer flag_med=0;
				Double ann = ((BigDecimal) datavisa.get("ANN")).doubleValue();
					if(ann > 6000000){
						Integer vNominal = 300000;
							if (businessId.equals("183")||businessId.equals("189")||businessId.equals("193")||businessId.equals("201")){
								vNominal=400000;
								flag_med=1;
								
							}
							
							uwDao.insertMstVisaCamp(reg_spaj, vNominal, currentUser.getLus_id(), flag_med,1);
					}
			}
	}

	public Integer selectValidAdminKantorPemasaran(String lus_id, String lar_id) {
		return uwDao.selectValidAdminKantorPemasaran(lus_id, lar_id);
	}
	
	public void updateMstDrekCc(String no_kartu, Integer jumlah){	
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		bacDao.updateMstDrekCc(no_kartu, jumlah);
	}
	
	public String selectSeqEmailId(){
		return this.uwDao.selectSeqEmailId();
	}
	
	/**
	 * fungsi untuk edit alamat (Pemegang dan Tertanggung Untuk Perubahan Alamat Nasabah)
	 * @author ryan
	 * @return 
	 */
	public void prosesEditAlamatNasabah(String spaj, User currentUser, String alamat, String kdpos, String telp, String telpWork, String hape, String hape2){
		Pemegang2 pmg=basDao.selectpemegangpolis(spaj);
		Tertanggung ttg =bacDao.selectttg(spaj);
		Date tgl=commonDao.selectSysdate();
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
		addNew.setKota_rumah(pmg.getKota_rumah());
		addNew.setMcl_id(pmg.getMcl_id());
		addNew.setNo_fax(pmg.getNo_fax());
		addNew.setNo_hp(hape);
		addNew.setNo_hp2(hape2);
		addNew.setTelpon_kantor(telpWork);
		addNew.setTelpon_kantor2(pmg.getTelpon_kantor2());
		addNew.setTelpon_rumah(telp);
		addNew.setTelpon_rumah2(pmg.getTelpon_rumah2());
		uwDao.updateMstAddressNew(addNew);
		//update alamat pada simas card
		Simcard simcard=new Simcard();
		simcard.setMcl_id(pmg.getMcl_id());
		simcard.setAlamat(alamat);
		simcard.setKota(pmg.getKota_rumah());
		simcard.setKode_pos(pmg.getKd_pos_rumah());
		uwDao.updateMstSimcardAlamat(simcard);
		
    }
	
	public Integer selectMedPlusRiderAddon(String spaj) {		
		return uwDao.selectMedPlusRiderAddon(spaj);
	}
	
	// view data Gandget - RYan
		public List selectGadgetisHere() {
			// TODO Auto-generated method stub
			return Common.serializableList(bacDao.selectGadgetisHere());
		}

	public List selectReportESpaj(String bdate, String edate) {
		// TODO Auto-generated method stub
		return Common.serializableList(bacDao.selectReportESpaj(bdate, edate));
	}
	
	public List selectReportUserDailyAksep() throws DataAccessException{
		return this.basDao.selectReportUserDailyAksep();
	}
	
	//randy - report daily aksep (req. alif)
	public List selectReportDailyAksep(String bdate, String edate,String lus_id) {
		// TODO Auto-generated method stub
		return Common.serializableList(basDao.selectReportDailyAksep(bdate, edate, lus_id));
	}
	
	//randy - report follow up billing (req. septa)
	public List selectReportFollowUpBillingCol(String bdate, String edate, String prod, String tipe, String status) {
		return Common.serializableList(basDao.selectReportFollowUpBillingCol(bdate, edate, prod, tipe, status));
	}
	
	public List selectReportHasilFollowUpBilling(String bdate, String edate, String prod, String status) {
		return Common.serializableList(basDao.selectReportHasilFollowUpBilling(bdate, edate, prod, status));
	}
	
	public List selectReportFollowUpBillingSum(String bdate, String edate, String prod, String tipe, String status) {
		return Common.serializableList(basDao.selectReportFollowUpBillingSum(bdate, edate, prod, tipe, status));
	}
	
	public List selectHistoryUploadSsu() {
		return Common.serializableList(basDao.selectHistoryUploadSsu());
	}
	
	/**
	 * Scheduler sisa stok virtual account (19/06/2014)
	 * @author 	Canpri
	 */
	public void schedulerVA(){
		String msh_name = "AUTOMAIL SISA VIRTUAL ACCOUNT";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerVA(msh_name);
		}
	}

	public String createSurattsr(HashMap data) throws DocumentException, IOException {
		
		String s_hasil = null;
		try {
				String exportDirectory ;
				String importDirectory;
				String sKeterangan;
				String outputFileName;
				SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat df2=new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat df3 = new SimpleDateFormat("yyyyMM");
				Date tgl_awal = null,tgl_akhir;
				String namaKaryawan=(String)data.get("nama");
				String noKtp=(String)data.get("ktp");
				String alamat=(String)data.get("alamat");
				String namaAtasan=(String)data.get("atasan");
				String jabatanAtasan=(String)data.get("jbt");
				String jabatanKaryawan = "";				
				String nip=(String)data.get("nip");
				String dept=(String)data.get("dept");
				String eva=(String)data.get("eva");
				String jangka=(String)data.get("jangka");
				String sk=(String)data.get("sk");	
				String noSurat=(String)data.get("shasil");
				String tl=(String)data.get("tl");
				String tgll=(String)data.get("tgll");
				String jkl=(String)data.get("jkl");
				String now2,bdate;				
				tgl_awal=df.parse((String)data.get("bdate"));
				tgl_akhir=df.parse((String)data.get("edate"));			
				Date sys=commonDao.selectSysdate();
				String now=FormatDate.toIndonesian(tgl_awal);
				now2=df2.format(tgl_awal);
				bdate=df3.format(tgl_awal);
				String tanggal=FormatDate.toStringWithOutSlash(tgl_awal);
				String hari=tanggal.substring(0, 2);
				String bulan=tanggal.substring(2, 4);
				String tahun=tanggal.substring(6, 8);
				String hari2=AngkaTerbilang.indonesian(hari);
				String bulan2=FormatDate.periodeFormatDate2(bdate);
				String tahun2=AngkaTerbilang.indonesian(tahun);
				HashMap counter=new HashMap();
				Integer i_counter=null;
				File file = null;
				PdfReader reader=null ;
				String outputName = null;	
				String no_surat=null;
				String bln_surat=null;
				

				

				if(dept.equalsIgnoreCase("49")){
					reader=new PdfReader(props.getProperty("pdf.surat.tsr_dmtm.template")+"\\templateDMTM.pdf");
					outputName=props.getProperty("pdf.surat.tsr_dmtm")+"\\"+nip+".pdf";	
					jabatanKaryawan=(String)data.get("jabatan");
				}else if(dept.equalsIgnoreCase("63")){
					reader=new PdfReader(props.getProperty("pdf.surat.tsr_mall.template")+"\\templateMALL.pdf");
					outputName=props.getProperty("pdf.surat.tsr_mall")+"\\"+nip.trim()+".pdf";
					jabatanKaryawan=(String)data.get("jabatan");
				}else if(dept.equalsIgnoreCase("7B")){
					jabatanKaryawan=(String)data.get("jbtBancass");
					String template = "";
					if (jabatanKaryawan.equalsIgnoreCase("Junior FA")){
						template="templateBancassJr.pdf";
					}else if(jabatanKaryawan.equalsIgnoreCase("Senior FA")){
						template="templateBancassSr.pdf";
					}else if(jabatanKaryawan.equalsIgnoreCase("Executive FA")){
						template="templateBancassEx.pdf";
					}
					reader=new PdfReader(props.getProperty("pdf.surat.tsr_bancass.template")+"\\"+template);
					outputName=props.getProperty("pdf.surat.tsr_bancass")+"\\"+nip.trim()+".pdf";
					
				}
				PdfContentByte over;
				BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\TIMES.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				file=new File(outputName);
				PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));	
				if("49,63".indexOf(dept)>=0){
					over = stamp.getOverContent(1);
					over.beginText();
					over.setFontAndSize(times_new_roman,10);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, noSurat, 257, 666, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, hari2, 225, 615, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, bulan2, 327, 615, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, tahun2, 475, 615, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, now2, 98, 601, 0);
				
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, namaAtasan, 165, 552, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, jabatanAtasan, 350, 552, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, sk, 310, 540, 0);
					
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, namaKaryawan, 100, 467, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, noKtp, 120, 455, 0);
					over.setFontAndSize(times_new_roman,9);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat, 90, 442, 0);			
					over.endText();
					
					over = stamp.getOverContent(2);
					over.beginText();
					over.setFontAndSize(times_new_roman,10);				
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, jabatanKaryawan, 187, 645, 0);				
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, namaAtasan, 187, 615, 0);
					over.endText();
					
					over = stamp.getOverContent(3);
					over.beginText();
					over.setFontAndSize(times_new_roman,10);				
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,jabatanKaryawan , 287, 172, 0);				
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, eva, 420, 83, 0);
					over.endText();
					
					over = stamp.getOverContent(4);
					over.beginText();
					over.setFontAndSize(times_new_roman,10);				
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,jangka , 300, 642, 0);				
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, now, 450, 642, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatDate.toIndonesian(tgl_akhir), 148, 627, 0);				
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, namaAtasan, 70, 80, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, namaKaryawan, 290, 80, 0);
					over.endText();
				}else{
					times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
					over = stamp.getOverContent(1);
					over.beginText();
					over.setFontAndSize(times_new_roman,11);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, noSurat, 248, 654, 0);
					over.setFontAndSize(times_new_roman,8);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, hari2.toUpperCase(), 110, 602, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, bulan2.toUpperCase(), 196, 602, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, tahun2.toUpperCase(), 320, 602, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, now2, 400, 602, 0);
					over.setFontAndSize(times_new_roman,8);					
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, namaKaryawan, 235, 479, 0);	
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, jkl, 235, 465, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, tl+",", 235, 450, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, tgll, 295, 450, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, noKtp, 235, 438, 0);
					over.setFontAndSize(times_new_roman,8);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat, 235, 426, 0);					
					over.endText();
					over = stamp.getOverContent(2);
					over.beginText();
					over.setFontAndSize(times_new_roman,8);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,jabatanKaryawan , 186, 540, 0);			
					over.endText();
					over = stamp.getOverContent(4);
					over.beginText();
					over.setFontAndSize(times_new_roman,8);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, now, 450, 339, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatDate.toIndonesian(tgl_akhir), 233, 327, 0);			
					over.endText();
					over = stamp.getOverContent(5);
					over.beginText();
					over.setFontAndSize(times_new_roman,8);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, namaKaryawan, 317, 300, 0);			
					over.endText();
				}
				if(stamp!=null){
					stamp.close();
				}
				s_hasil=noSurat;
		} catch (ParseException e) {
			
				s_hasil="GAGAL";
		}
		
		return s_hasil;
	}

	public ArrayList selectDataTsr(String bdate, String edate, String dist1, String flag, String nip) {
		
		return rekruitmentDao.selectDataTsr(bdate,edate,dist1,flag,nip);
	}

	public String sendEmailTSR(HashMap map, String shasil,String nip, String dept) {
		String email=null;
		String emailcc=null;
		String pesanKemenangan="Email Berhasil Dikirim";
		String from="ajsjava@sinarmasmsiglife.co.id";
		Date tgl_awal = null,tgl_akhir;
		SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
		String filename=null;
		File src = null;
		File cpy = null;
		Date nowDate = commonDao.selectSysdate();
	    Integer months = nowDate.getMonth()+1;
	    Integer years = nowDate.getYear()+1900;		 	
		
		try{
			StringBuffer pesan= new StringBuffer();
			pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}</style></head>");
			pesan.append("<body>");			
			pesan.append("#F_1#\n\n\n");
			pesan.append("Harap untuk melakukan Printing Surat Kesepakatan Kemitraan TSR/Mitra/FA  terlampir ,dengan informasi TSR/Mitra/FA seperti berikut:\n\n");
			pesan.append("<table>");			
			pesan.append("<tr><td>TSR/Mitra</td><td>:</td><td>" +(String) map.get("nama") + "</td></tr>");
			pesan.append("<tr><td>Alamat</td><td>:</td><td>" +(String) map.get("alamat") + "</td></tr>");
			tgl_awal=df.parse((String)map.get("bdate"));
			tgl_akhir=df.parse((String)map.get("edate"));
			pesan.append("<tr><td>TSR/Mitra</td><td>:</td><td>" +FormatDate.toIndonesian(tgl_awal) +" s/d "+FormatDate.toIndonesian(tgl_akhir) + "</td></tr>");			
			pesan.append("<tr><td>Keterangan</td><td>:</td><td><b>Hanya boleh Print 1 (satu) kali</b></td></tr>");
			pesan.append("</table>");
			pesan.append("\n<b>Manual Guide Pencetakan Surat:</b>");
			pesan.append("\n");
			pesan.append("\n1.Print PDF Terlampir");
			pesan.append("\n2.TTD Kontrak oleh YBS");
			pesan.append("\n3.Scan Halaman Terakhir (yang ada tandatangannya) ke PDF");
			pesan.append("\n4.Upload scan kontrak yang telah ditandatangani ke Link http://elions.sinarmasmsiglife.co.id");			
			pesan.append("\n\nTerima Kasih");
			pesan.append("\n\n<b>Asuransi Jiwa Sinarmas MSIG</b>");
			pesan.append("</body>");
			pesan.append("</html>");
			
			String me_id = selectSeqEmailId();	
			if(dept.equalsIgnoreCase("49")){
				filename=props.getProperty("pdf.surat.tsr_dmtm")+"\\"+nip+".pdf";
				email="Fery@sinarmasmsiglife.co.id;Lylianty@sinarmasmsiglife.co.id;Gideon@sinarmasmsiglife.co.id;maria_p@sinarmasmsiglife.co.id ";
				emailcc="juni@sinarmasmsiglife.co.id;Yurika@sinarmasmsiglife.co.id;Jelita@sinarmasmsiglife.co.id;Karunia@sinarmasmsiglife.co.id";
			}else if(dept.equalsIgnoreCase("63")){
				filename=props.getProperty("pdf.surat.tsr_mall")+"\\"+nip+".pdf";
				email="audi.reynold@sinarmasmsiglife.co.id;mega@sinarmasmsiglife.co.id;";
				emailcc="juni@sinarmasmsiglife.co.id;Yurika@sinarmasmsiglife.co.id;Jelita@sinarmasmsiglife.co.id;Karunia@sinarmasmsiglife.co.id";
			}else if(dept.equalsIgnoreCase("7B")){
				filename=props.getProperty("pdf.surat.tsr_bancass")+"\\"+nip+".pdf";
				email="mega@sinarmasmsiglife.co.id";
				emailcc="Jelita@sinarmasmsiglife.co.id;helmy@sinarmasmsiglife.co.id;Shima@sinarmasmsiglife.co.id";
			}
			src=new File(filename);
			String destTo=props.getProperty("attachment.mailpool.dir")+"\\" +years+"\\"+FormatString.rpad("0", months.toString(), 2)+"\\"+me_id+"\\";
		   	File destDir2 = new File(destTo);
		    if(!destDir2.exists()) destDir2.mkdirs();
		    String filename2=nip+".pdf";
		   	cpy=new File(destTo +filename2);
		   	FileUtils.copy(src, cpy);
		   	String []emailto=email.split(";");
		   	String []cc=emailcc.split(";");
		   	try{
				EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, nowDate, null, 
						true, from,												
						emailto, 
						cc, 
						new String[]{"ryan@sinarmasmsiglife.co.id;"}, 
						"[SMiLe E-Lions]Print Surat Kemitraan/TSR/FA", 
						pesan.toString(), 
						null,12);
	   		 		
			
			} catch (MailException e) {
				logger.error("ERROR :", e);
				pesanKemenangan="GAGAL KIRIM EMAIL";
			} 
		   	
		   	commonDao.insertDataSuratTsr(map);
			
		}catch (Exception e) {
			logger.info(false);
		}
		
		return pesanKemenangan;
	}
	
	public void updateMstCounter(Double ld_cnt,int ai_id,String ls_cabang){
		uwDao.updateMstCounter(ld_cnt, ai_id, ls_cabang);
	}
	
	
	/**
	 * Report Jatuh Tempo (17/07/2014)
	 * @author 	Rahmayanti
	 */

	public HashMap select_det_va_spaj(String spaj) {
		return bacDao.select_det_va_spaj(spaj);
	}
	
	public List reportFollowupTempo(String bdate, String edate, String jenis, String jn_tgl)
	{
		return basDao.reportFollowupTempo(bdate, edate,jenis, jn_tgl);
	}
//	public List selectDataTmms(String s_kata, String s_tipe,
//			String s_pilter) {
//		
//		return uwDao.selectDataTmms(s_kata,s_tipe,s_pilter);
//	}
	// view data PSN - RYan
	public List selectDataPSN(String reg_spaj) {
		// TODO Auto-generated method stub
		return Common.serializableList(bacDao.selectDataPSN(reg_spaj));
	}	
	
	public String selectSeqUrlSecureId(){
		return this.uwDao.selectSeqUrlSecureId();
	}
	
	public HashMap selectMstUrlSecure(String id1, String id2) {
		return Common.serializableMap(commonDao.selectMstUrlSecure(id1, id2));
	}
	
	public HashMap selectMstUrlSecure2(String no_polis, String link) {
		return Common.serializableMap(commonDao.selectMstUrlSecure2(no_polis, link));
	}
	
	public void deleteMstUrlSecure(String no_polis, String flag){
		commonDao.deleteMstUrlSecure(no_polis, flag);
	}
	
//	public void updateMstUrlSecure(String no_polis, String flag, String link){
//		commonDao.updateMstUrlSecure(no_polis, flag, link);
//	}
		
	public void insertMstUrlSecure(String id1, String id2, String no_polis, String link){
		commonDao.insertMstUrlSecure(id1, id2, no_polis, link);
	}
	
	/*public List selectDataLQG(Integer type) {
		// TODO Auto-generated method stub
		return Common.serializableList(uwDao.selectDataLQG(type));
	}

	public List selectDataLQL(Integer type) {
		// TODO Auto-generated method stub
		return Common.serializableList(uwDao.selectDataLQL(type));
	}*/
		
	public ArrayList selectDataSpajDmtmdiPayment() {
		// TODO Auto-generated method stub
		return Common.serializableList(uwDao.selectDataSpajDmtmdiPayment());
	}

	public Map selectDataFromDrekCC(String reg_spaj) {
		
		return this.uwDao.selectDataFromDrekCC(reg_spaj);
	}

	public double selectBillingRemain(String spaj) {
		Map temp = this.uwDao.selectBillingRemain(spaj);
		double ldec_sisa = Double.parseDouble(temp.get("MSBI_REMAIN").toString());
		return ldec_sisa;
	}	
	
	public Agen select_detilagen3(String s_msag_id) {
		return bacDao.select_detilagen3(s_msag_id);
	}
	
	public Map ProsesSpeedy(String s_spaj, Integer i_flag_reg, Pemegang pmg,Tertanggung tt, Datausulan du, User user,HttpServletRequest request, ElionsManager elionsManager2) throws Exception {
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		HashMap map= new HashMap();
		StringBuffer err_speedy = new StringBuffer();
		StringBuffer suc_speedy = new StringBuffer();
		String result_simultan = "";
		String result_kuesioner = "";
		String result_akseptasi = "";
		 
		Policy policy = new Policy();
		Integer error = 0;
		Integer flag_print = 0;
		Integer uw_helpdesk = 0;
		Integer substandart = 0;
		Integer lspdIdTemp = 0;
		String lspdPosTemp="";
		String mdesc = "";
		String idSimultanPp = null;
		String idSimultanTt = null;
		DecimalFormat fmt = new DecimalFormat ("000");
		HashMap hm_speedy = new HashMap();
		Integer i_speedy = 1;
		String s_medis = "";
		Date nowDate = commonDao.selectSysdate();
		Map mTertanggung =uwDao.selectTertanggung(s_spaj);
		CommandChecklist cmd = new CommandChecklist();
		
		Integer i_owner = ServletRequestUtils.getIntParameter(request, "owner",1);
		Integer i_owner2 = ServletRequestUtils.getIntParameter(request, "owner2",0);
		Integer i_parent = ServletRequestUtils.getIntParameter(request, "parent",1);
		Integer i_agent = ServletRequestUtils.getIntParameter(request, "agent",1);
		Integer i_reviewed = ServletRequestUtils.getIntParameter(request, "reviewed",1);
		Integer i_hq = ServletRequestUtils.getIntParameter(request, "health_quest",1);
		
		String lsbsid = uwDao.selectBusinessId(s_spaj);
		String lsdbsnumber = uwDao.selectLsdbsNumber(s_spaj);

		uwDao.updateMstInsuredHealthQuest(s_spaj, i_hq);
		
		//compare signature in all form
		if(i_owner == 0){
			i_speedy = 0;
			err_speedy.append("<br>- Owner's & Insured's Signature tidak sama");
		}
		
		if(tt.getMste_age() < 17){
			if(i_parent == 0){
				i_speedy = 0;
				err_speedy.append("<br>- Parent's Signature tidak ada");
			}
		}
		
		if(i_agent == 0){
			i_speedy = 0;
			err_speedy.append("<br>- Agent's Signature tidak sama/tidak ada");
		}
		
		if(i_reviewed == 0){
			i_speedy = 0;
			err_speedy.append("<br>- Belum di review oleh user");
		}
		
		if(i_owner2 == 1){
			uwDao.insertMstPositionSpaj(user.getLus_id(), "Tanda Tangan Sesuai Dengan Bukti Identitas", s_spaj, 0);
		}
		
		//simultan dan medis via prosedur oracle
		result_simultan = uwDao.prosesSimultanMedis(s_spaj, user.getLus_id());
		/*if(!result_simultan.equalsIgnoreCase("sukses")){
			err_speedy.append("<br>- Proses simultan error");
			error = 1;
		}*/
		Insured ins = uwDao.selectMstInsuredAll(s_spaj);
		if(ins.getFlag_speedy()==null){
			err_speedy.append("<br>- Proses simultan error");
			error = 1;
		}else if(ins.getFlag_speedy()==0){
			suc_speedy.append("<br>- "+result_simultan);
		}
		
		String s_valMedis = "";
		
		//if nya di nonaktifkan permintaan dokter asri
		//if(ins.getFlag_speedy()==1){
			//validasi questioner via prosedur oracle
		
		if((lsbsid.equalsIgnoreCase("217") && lsdbsnumber.equalsIgnoreCase("2"))
			|| (lsbsid.equalsIgnoreCase("200") && lsdbsnumber.equalsIgnoreCase("7")) 
			|| (lsbsid.equalsIgnoreCase("190") && lsdbsnumber.equalsIgnoreCase("9"))){
			//proses kuesionare untuk ERBE Pro 100 / SMILE ULTIMATE KONVEN dan SYARIAH melakukan pengecekan penyakit, tidak bisa ke transfer jika ada penyakit
			//Data  yang di terima dari ERBE harus Clean Case.
			result_kuesioner = uwDao.prosesValQuest(s_spaj, user.getLus_id());
			
			//select hasil validasi medis
			ArrayList l_histSpeedy = Common.serializableList(uwDao.selectHistorySpeedy(s_spaj));
			if(l_histSpeedy.size()>0){
				for(int i=0; i<l_histSpeedy.size();i++){
					HashMap m = (HashMap) l_histSpeedy.get(i);
					err_speedy.append("<br> - "+(String)m.get("DESCRIPTION"));
	
					if(((String)m.get("DESCRIPTION")).toUpperCase().contains("UNCLEAN CASE"))result_kuesioner ="tidak suskes karena data Unclean Case";
					else if(((String)m.get("DESCRIPTION")).toUpperCase().contains("FURTHER"))result_kuesioner ="tidak sukses karena Further";
					else if(((String)m.get("DESCRIPTION")).toUpperCase().contains("CLIENT FAILED")){
						//tidak menjadikan gagal transferapabila hanya perubahan data kecuali penyakit						
					}
				}
			}
			//ERBE tidak ada Further sehingga karena pada saat proses medis / overwheight ada perubahan lssa_id dari 17 ke 3 sehingga mengakibatkan gagal auto accept
			if(bacDao.selectPositionSpaj(s_spaj) == 3){
				bacDao.savelssaid(s_spaj, 17);	
			}
			
		}else{
			result_kuesioner = uwDao.prosesValQuest(s_spaj, user.getLus_id());
			
			//Produk AutoMatching tidak ada Further sehingga karena pada saat proses medis / overwheight ada perubahan lssa_id dari 17 ke 3 sehingga mengakibatkan gagal auto accept
			if (lsbsid.equalsIgnoreCase("212")){
				if(bacDao.selectPositionSpaj(s_spaj) == 3){
					bacDao.savelssaid(s_spaj, 17);	
				}
			}

			if (lsbsid.equalsIgnoreCase("223") && lsdbsnumber.equalsIgnoreCase("1")){
				if(bacDao.selectPositionSpaj(s_spaj) == 3){
					bacDao.savelssaid(s_spaj, 17);	
				}
				
				result_kuesioner = "sukses";
			}
			
		}
			
			if((lsbsid.equalsIgnoreCase("212") && lsdbsnumber.equalsIgnoreCase("9")) || //Chandra A - 20180517: jika smile proteksi result = sukses
			   (lsbsid.equalsIgnoreCase("212") && lsdbsnumber.equalsIgnoreCase("14")) || // add lsdbsnumber 14 nana add smile proteksi 212-14 helpdesk 147672
			   (lsbsid.equalsIgnoreCase("142") && lsdbsnumber.equalsIgnoreCase("13"))){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				result_kuesioner = "sukses";
			}
			//28-8-2015(Deddy) - Disini ditambahkan untuk pengiriman email further otomatis untuk kuisioner SPAJ FULL periode 201509.
			//Caranya : 
			//1. Cek dulu apakah SPAJ ini bagian question kesehatan SPAJ FULL menggunakan periode 201509
			//2. Apabila bukan, tidak perlu dijalankan.Apabila iya, cek ke table mst_speedy_history dan distinct di kolom ATT_FILE_FURTHER, apabila ada isi maka semua file itu diattach dan dikirimkan email Further.
			//3. Email Further yg dikirimkan, disamakan dengan com.ekalife.elions.service.ElionsManager.kirimEmail. 
			Integer lssa_id = uwDao.selectStatusAksep(s_spaj);
			if(!result_kuesioner.equalsIgnoreCase("sukses")){
				err_speedy.append("<br>- Proses kuesioner error. ("+result_kuesioner+")");
				error = 1;
			} else {
				// Jika proses kuisioner sukses, kirim email
				if("SEP2015".equals(uwDao.selectPeriodQHealth(s_spaj)) 
						&& lssa_id == 3) {
					Command command = new Command();
					command.setSpaj(s_spaj);
					command.setFlag_ut(0);
					command.setLspd_id(ins.getLspd_id());
					
					Map mDataUsulan = (HashMap) uwDao.selectDataUsulan2(s_spaj);
					if(!mDataUsulan.isEmpty() && mDataUsulan.get("LSBS_ID") != null) {
						command.setMsprTsi((Double)mDataUsulan.get("MSPR_TSI"));
						command.setMsprPremium((Double)mDataUsulan.get("MSPR_PREMIUM"));
						command.setLscbPayMode((String)mDataUsulan.get("LSCB_PAY_MODE"));
					}
					
					String emailAdmin = uwDao.selectEmailAdmin(s_spaj);
					Map agen = uwDao.selectEmailAgen(s_spaj);
					String namaAgen = (String) agen.get("MCL_FIRST");
					String emailAgen = (String) agen.get("MSPE_EMAIL");
					String msagId = (String) agen.get("MSAG_ID");
					Integer msagSertifikat = (Integer) agen.get("MSAG_SERTIFIKAT");
					Date tglSertifikat = (Date) agen.get("MSAG_BERLAKU");
					Date msagBegDate = (Date) agen.get("MSAG_BEG_DATE");
					Integer msagActive = (Integer) agen.get("MSAG_ACTIVE");
					
					List listPositionSpaj = uwDao.selectMstPositionSpajWithSubId(s_spaj);
					command.setLsPosition(listPositionSpaj);
					
					Map a = (HashMap) listPositionSpaj.get(listPositionSpaj.size() - 1);
					String mspsDate = (String) a.get("MSPS_DATE");
					String status = (String) a.get("STATUS_ACCEPT");
					command.setSubLiAksep(0);
					
					if(status == null) {
						status = commonDao.selectStatusAcceptFromSpaj(s_spaj);
					}

					Map MPp = (HashMap) uwDao.selectPemegang(s_spaj);
					command.setLcaIdPp((String) MPp.get("LCA_ID"));
					command.setNamaPemegang((String) MPp.get("MCL_FIRST"));

					Map mTt = (HashMap) uwDao.selectTertanggung(s_spaj);
					command.setNamaTertanggung((String) mTt.get("MCL_FIRST"));
					
					String deskripsi = "FURTHER DOKUMEN KUISIONER KESEHATAN :";
					ArrayList<File> listAttachment = Common.serializableList(uwDao.selectListAttFileFurtherSpeedy(s_spaj));
					if(listAttachment.size()>0){
						for(int i=0;i<listAttachment.size();i++){
							String nama_file_ori = listAttachment.get(i).getName();
							String checking="0123456789";
							String nama_file_rev = ""; 
							for(int j=0;j<nama_file_ori.length();j++){
								if ( checking.indexOf(nama_file_ori.substring(j, j+1))==-1)
								{
									nama_file_rev += nama_file_ori.substring(j,j+1); 
								}
							}
							deskripsi += nama_file_rev.replace("_", " ").replace(".pdf", "")+"; ";
						}
					}
				//	emailAdmin = "ningrum@sinarmasmsiglife.co.id;";
				//	emailAgen = "ryan@sinarmasmsiglife.co.id";
					this.kirimEmail(s_spaj, ins.getLssa_id(), emailAdmin, emailAgen, 
							status, mspsDate, user, deskripsi, null, msagId, msagSertifikat, 
							tglSertifikat, msagBegDate, msagActive, command.getLcaIdPp(), command.getNamaPemegang(), 
							command.getNamaTertanggung(), command, new BindException(command, "cmd"), listAttachment);
				}
			}
			ins = uwDao.selectMstInsuredAll(s_spaj);
		
		//}
		
		//select medis
		s_medis = uwDao.selectMedisDesc(ins.getJns_medis()); 
		
		//select hasil validasi medis
		ArrayList l_histSpeedy = Common.serializableList(uwDao.selectHistorySpeedy(s_spaj));
		if(l_histSpeedy.size()>0){
			for(int i=0; i<l_histSpeedy.size();i++){
				HashMap m = (HashMap) l_histSpeedy.get(i);
				err_speedy.append("<br> - "+(String)m.get("DESCRIPTION"));
				
				if (!(lsbsid.equalsIgnoreCase("217") && lsdbsnumber.equalsIgnoreCase("2")) &&
					!(lsbsid.equalsIgnoreCase("200") && lsdbsnumber.equalsIgnoreCase("7")) &&
					!(lsbsid.equalsIgnoreCase("190") && lsdbsnumber.equalsIgnoreCase("9"))){
					if(((String)m.get("DESCRIPTION")).toUpperCase().contains("FORM PERUBAHAN DAN PENAMBAHAN DATA"))uw_helpdesk = 1;
				}
				else if(((String)m.get("DESCRIPTION")).toUpperCase().contains("UNCLEAN CASE"))uw_helpdesk = 1;
				else if(((String)m.get("DESCRIPTION")).toUpperCase().contains("FURTHER"))uw_helpdesk = 1;
			}
		}
		
		i_speedy = ins.getFlag_speedy();
		
		/*//kelengkapan brosur
		Boolean chk_bsb = ServletRequestUtils.getBooleanParameter(request, "chk_bsb", false);
		Boolean chk_identitas = ServletRequestUtils.getBooleanParameter(request, "chk_identitas", false);
		Boolean chk_proposal = ServletRequestUtils.getBooleanParameter(request, "chk_proposal", false);
		Boolean chk_spaj = ServletRequestUtils.getBooleanParameter(request, "chk_spaj", false);
		Boolean chk_pap = ServletRequestUtils.getBooleanParameter(request, "chk_pap", false);
		//end
		
		//jika ada kekurangan berkas langsung non speedy (further)
		Integer kurang_berkas = 0;
		if(chk_bsb == true || chk_identitas == true || chk_proposal == true || chk_spaj == true || chk_pap == true){
			kurang_berkas = 1;
			i_speedy = 0;
			
			//update jadi non speedy dan insert history speedy
			uwDao.updateFlagSpeedy(s_spaj, 0);
			uwDao.insertMstSpeedyHistory(s_spaj, "", user.getLus_id());
		}*/
		
//		String lsbsid = uwDao.selectBusinessId(s_spaj);
//		String lsdbsnumber = uwDao.selectLsdbsNumber(s_spaj);

		//untuk produk SMiLe Life Care Plus (external) TROPS (212-7) VASCO/ DMTM SIO langsung di bypass tanpa liat data clean/unclean
		if((lsbsid.equalsIgnoreCase("212") && lsdbsnumber.equalsIgnoreCase("7"))||
			(lsbsid.equalsIgnoreCase("197") && lsdbsnumber.equalsIgnoreCase("2"))){
			i_speedy = 1;
		}
		//Produk di bawah ini tidak ada further akibat hasil medis.
		else if((lsbsid.equalsIgnoreCase("177") && lsdbsnumber.equalsIgnoreCase("4")) ||
				(lsbsid.equalsIgnoreCase("190") && lsdbsnumber.equalsIgnoreCase("9")) ||
				(lsbsid.equalsIgnoreCase("223") && lsdbsnumber.equalsIgnoreCase("1")) ||
				(lsbsid.equalsIgnoreCase("223") && lsdbsnumber.equalsIgnoreCase("2")) || //helpdesk [138638] produk baru SLP Syariah (223-2)
				(lsbsid.equalsIgnoreCase("212") && (lsdbsnumber.equalsIgnoreCase("6") || lsdbsnumber.equalsIgnoreCase("9")|| lsdbsnumber.equalsIgnoreCase("14"))) || // add lsdbsnumber 14 nana add smile proteksi 212-14 helpdesk 147672
				(lsbsid.equalsIgnoreCase("142") && lsdbsnumber.equalsIgnoreCase("13"))){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
			i_speedy = 1;
			uwDao.updateMstInsuredStatus(s_spaj, new Integer(1), new Integer(17), new Integer(1), null, null);
		}
		
		//Khusus Provestara dan Smile Medical Plus. jika usia Tertanggung >= 50, transfer ke uw proses -Ridhaal
		boolean smilmedplus50an = false;
		if(du.getLsbs_id()==214 || du.getLsbs_id()==225){
				if(tt.getMste_age() >= 50){
					smilmedplus50an = true;
					i_speedy = 0;
				}
								
				//jika ada data peserta rider Provestara dengan usia Tertanggung >= 50 maka transfer ke uw proses
				List<Map> listPeserta = uwDao.selectDataPeserta(s_spaj);
				if (listPeserta.size() != 0) {
					for (int i=0;i<listPeserta.size();i++) {
						Map peserta = (HashMap) listPeserta.get(i);
						Integer umur = Integer.parseInt(peserta.get("UMUR").toString());
						
						if (umur >= 50 ) {
							smilmedplus50an = true;
							i_speedy = 0;
						}
					}
				}
		}
				
		if((du.getLsbs_id()==223 && du.getLsdbs_number()==1) // SMILE LIFE SYARIAH
			|| (du.getLsbs_id()==212 && du.getLsdbs_number()==8) // NISSAN TERM ROP
			|| (du.getLsbs_id()==73 && du.getLsdbs_number()==15) // NISSAN PA A
			|| (du.getLsbs_id()==203 && du.getLsdbs_number()==4) // NISSAN DBD
			|| (du.getLsbs_id()==200 && du.getLsdbs_number()==7) // Ultimate Syariah
			|| (du.getLsbs_id()==217 && du.getLsdbs_number()==2)
			
				)  //ERBE PACKAGE
		{
			i_speedy = 1;
		}
		
		if(i_speedy==0){//transfer ke uw (posisi 2)
			/*if(kurang_berkas == 1){
				//update status jadi further
				uwDao.updateMstInsuredStatus(s_spaj, new Integer(1), new Integer(3), new Integer(1), null, null);
				
				
			}else*/ 
				//update status jadi further
				//uwDao.updateMstInsuredStatus(s_spaj, new Integer(1), new Integer(3), new Integer(1), null, null);
				uwDao.insertMstPositionSpaj(user.getLus_id(), "TRANSFER KE U/W", s_spaj, 0);
				uwDao.updateMstInsured(s_spaj,2);
				uwDao.updateMstPolicy(s_spaj,2);
				uwDao.saveMstTransHistory(s_spaj, "tgl_transfer_uw", null, null, null);
								
				if(smilmedplus50an) uwDao.insertMstPositionSpaj(user.getLus_id(), "TRANSFER KE U/W PROSES KARENA ADANYA TERTANGGUNG UTAMA/TAMBAHAN UNTUK PRODUK INI DENGAN USIA 50 - 65 TAHUN ", s_spaj, 0);
				
				suc_speedy.append("<br>- Polis Non speedy, dikirim ke UW PROSES");
				
				String to = "";
				String lca_id = uwDao.selectLcaIdMstPolicyBasedSpaj(s_spaj);
//				Rahmayanti - Snows				
				uwDao.prosesSnows(s_spaj, user.getLus_id(), 210, 212);
				
				if("09,44,45,56,69".indexOf(lca_id) > 0)to = "UWHelpdeskBancassurance@sinarmasmsiglife.co.id";
				else if("40,59".indexOf(lca_id) > 0)to = "UWHelpdeskDMTM@sinarmasmsiglife.co.id";
				else to = "UWHelpdeskAgency@sinarmasmsiglife.co.id";
				
				//kirim email ke UW Proses
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, nowDate, null, 
						true, "ajsjava@sinarmasmsiglife.co.id", 
						new String[]{to}, 
						null,  
						null, 
						"[E-Lions] Automate UW dengan SPAJ "+s_spaj, 
						"Medis : "+s_medis+" \n"+suc_speedy.toString()+err_speedy.toString(), 
						null, s_spaj);
			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			this.uwDao.batchingSimasPrimelink(s_spaj); //update batching simas primelink (rider save) //Chandra - 20181012
			//this.uwDao.blacklistChecking(reg_spaj); //helpdesk 127356, cek blacklist dan kirim email. //Chandra - 20181011
		}else{
			//PROSES UNDERWRITER
			try{
				// Tgl Terima Doc 
				uwDao.updateMstInsuredTgl(s_spaj, 1, nowDate, 2);
				uwDao.saveMstTransHistory(s_spaj, "tgl_berkas_terima_uw", nowDate, null, null);
				if(uwDao.updateMstpositionSpajTgl(s_spaj, user.getLus_id(), defaultDateFormat.format(nowDate), "TGL SPAJ DOC","TGL SPAJ DOC")==0){
					uwDao.insertMstPositionSpaj(user.getLus_id(), "TGL SPAJ DOC", s_spaj, 0);
				}
				
				// Tgl Terima SPAJ
				uwDao.updateMstInsuredTgl(s_spaj, 1, nowDate, 0);
				uwDao.saveMstTransHistory(s_spaj, "tgl_terima_spaj_uw", nowDate, null, null);
				if(uwDao.updateMstpositionSpajTgl(s_spaj, user.getLus_id(), defaultDateFormat.format(nowDate), "TGL TERIMA SPAJ","TGL TERIMA SPAJ")==0){
					uwDao.insertMstPositionSpaj(user.getLus_id(), "TGL TERIMA SPAJ", s_spaj, 0);
				}
				
				//KYC
				uwDao.updateProsesKyc(s_spaj, 1, user.getLus_id(), 0, nowDate);
				
				//Copy Checklist untuk Update flag_uw =1 , PROSES CHECKLIST DI UW
				
				cmd.setLspd_id(27);
				cmd.setReg_spaj(s_spaj);
				cmd.setListChecklist(checklistDao.selectCheckListBySpaj(s_spaj));
				checklistDao.saveChecklist(cmd, user);
				
				suc_speedy.append("<br>- Proses Underwriting Berhasil!");
			}catch(Exception e){
				error = 1;
				e.printStackTrace();
				err_speedy.append("<br>- Proses Underwriting Error!");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
			
			// Proses Reas - Ini yang susah.
			Reas reas=new Reas();
			Integer lsbsId = 0;
			try{
				reas.setInfo(new Integer(0));
				reas.setLspdId(new Integer(27));
				reas.setLstbId(new Integer(1));
				String las_reas[]=new String[3];
				las_reas[0]="Non-Reas";
				las_reas[1]="Treaty";
				las_reas[2]="Facultative";
				reas.setCurrentUser((User) request.getSession().getAttribute("currentUser"));        
		
				reas.setSpaj(s_spaj);
				HashMap mPosisi= Common.serializableMap(uwDao.selectF_check_posisi(s_spaj));
				lspdIdTemp=reas.getLspdId();//(Integer)mPosisi.get("LSPD_ID");
				lspdPosTemp=(String)mPosisi.get("LSPD_POSITION");
				//produk asm
				HashMap hm_map = Common.serializableMap(uwDao.selectDataUsulan(reas.getSpaj()));
				lsbsId=(Integer)hm_map.get("LSBS_ID");
		
				//validasi Posisi dokumen
				if(lspdIdTemp.intValue()!=27){
					reas.setInfo(new Integer(1));
					reas.setLsPosDoc(lspdPosTemp);
					//MessageBox('Info', 'Posisi Polis Ini Ada di ' + ls_pos )
				}
				//
				//tertanggung
				reas.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
				reas.setMsteInsured((String)mTertanggung.get("MCL_ID"));
				reas.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
				//
				HashMap mStatus = Common.serializableMap(uwDao.selectWfGetStatus(s_spaj,reas.getInsuredNo()));
				reas.setLiAksep((Integer)mStatus.get("LSSA_ID"));
				reas.setLiReas((Integer)mStatus.get("MSTE_REAS"));
				if (reas.getLiAksep()==null) 
					reas.setLiAksep( new Integer(1));
				
				
				//dw1 //pemegang
				policy=uwDao.selectDw1Underwriting(reas.getSpaj(),reas.getLspdId(),reas.getLstbId());
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
					if(reas.getMste_kyc_date()==null){
						reas.setInfo(new Integer(6));
					}		
					if ( (reas.getMspoPolicyHolder().substring(0,2).equalsIgnoreCase("XX")) || (reas.getMspoPolicyHolder().substring(0,2).equalsIgnoreCase("WW")) ){
						reas.setInfo(new Integer(2));
						//MessageBox('Info', 'Proses Simultan Belum Dilakukan !!!')
					}else if (reas.getLiReas()!=null){
						Integer liBackup=(Integer)uwDao.selectMstInsuredMsteBackup(reas.getSpaj(),reas.getInsuredNo());
						if(liBackup==null)
							liBackup=new Integer(100);
						if(liBackup.intValue()!=0 || (liBackup.intValue()==0 && reas.getLiReas().intValue()==2) ){
							reas.setInfo(new Integer(3));
							reas.setLsPosDoc(las_reas[reas.getLiReas().intValue()]);
			//				If MessageBox( 'Information', 'Proses Reas sudah pernah dilakukan~r~nType Reas = ' + las_reas[li_reas+1] &
			//				+  '~r~nView hasil proses sebelumnya ?', Exclamation!, OKCancel!, 1 ) = 1 Then OpenWithParm(w_simultan,lstr_polis.no_spaj)
						}
						
					}
					//cek standard
					if(policy.getMste_standard().intValue()==1){
					Integer liCount=uwDao.selectCountMstProductInsuredCekStandard(reas.getSpaj());
						if(liCount.intValue()==0){
							//li_count = Messagebox('Info', 'Polis ini non-standard, Extra Premi Belum Ada !!!~n~nYakin Lanjutkan ?', Question!, Yesno!, 2)
							reas.setInfo(new Integer(4));
							//info=4;
						}
					}
				}
				reas.setLstbId(1);
				reas.setLspdId(27);
				HashMap proses = Common.serializableMap(reasIndividu.prosesReasUnderwriting(reas, null));
				
				suc_speedy.append("<br>- Proses Reas Berhasil!");
			}catch(Exception e){
				e.getLocalizedMessage();
				logger.error("ERROR :", e);
				error = 1;
				e.printStackTrace();
				err_speedy.append("<br>- Proses Reas Error!");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
				
			//Proses Akseptasi , langsung Accepted
			//(8 SEPT 2015) - apabila status akseptasi nya further, tidak perlu otomatis menjalankan akseptasi.(terkait dengan perubahan questionare kesehatan).
			//(7 OKT 2015) - apabila produk save series  lakukan proses akseptasi
			String lsbs_id = uwDao.selectBusinessId(s_spaj);		
			int a = bacDao.selectPositionSpaj(s_spaj);
			if(bacDao.selectPositionSpaj(s_spaj) != 3 || products.powerSave(lsbs_id)){
				result_akseptasi = uwDao.prosesAkseptasiSpeedy(s_spaj, user.getLus_id());
				if(!result_akseptasi.equalsIgnoreCase("sukses")){
					err_speedy.append("<br>- Proses akseptasi error");
					error = 1;
				}
			}
//			try{
//				uwDao.prosesAkseptasiSpeedy(s_spaj, user.getLus_id());
//			}catch(Exception e){
//				error = 1;
//				err_speedy.append("<br>- Proses Akseptasi Error!");
//				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//			}
			/*Akseptasi akseptasi=new Akseptasi();
			String lca_id = "";
			try{
				akseptasi.setLbUlink(false);
				akseptasi.setLspdId(new Integer(27));
				akseptasi.setLsspId(new Integer(10));
				akseptasi.setLssaId(new Integer(5));
				akseptasi.setLstbId(new Integer(1));
				akseptasi.setSpaj(s_spaj);
				akseptasi.setLsbsId(lsbsId);
				akseptasi.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
				
				policy = uwDao.selectDw1Underwriting(akseptasi.getSpaj(),akseptasi.getLspdId(),akseptasi.getLstbId());
				
				akseptasi.setNoPolis(policy.getMspo_policy_no());
				List lsStatusAksep=akseptasiDao.selectMstPositionSpaj(akseptasi.getSpaj());
				//
				akseptasi.setCurrentUser(user);        
				
				Position position=new Position();
				position.setReg_spaj(akseptasi.getSpaj());
				position.setMsps_date(akseptasiDao.selectSysdate(new Integer(2)));
				position.setLus_id(akseptasi.getCurrentUser().getLus_id());
				position.setLus_login_name(akseptasi.getCurrentUser().getName());
				//cek lagi untuk pengesetan info pake karena karena untuk sementara kondisi ini gak terpakai seutuhnya
				
				if(akseptasi.getInfo1()==10)
					position.setLssa_id(new Integer(8));
				else
					position.setLssa_id(new Integer(5));
				lsStatusAksep.add(position);
				//cekin untuk produk yang masih pending
				HashMap mapTt = Common.serializableMap(uwDao.selectTertanggung(akseptasi.getSpaj()));
				Integer lssaId=(Integer)mapTt.get("LSSA_ID");
				Integer flag_investasi=(Integer) mapTt.get("MSTE_FLAG_INVESTASI");
				akseptasi.setFlag_investasi(flag_investasi);
				HashMap mAksep = (HashMap)uwDao.selectLstStatusAccept(lssaId).get(0);
				String statusAksep=(String) mAksep.get("STATUS_ACCEPT");
				Integer countSts=uwDao.selectCountMstPositionSpaj(akseptasi.getSpaj(),"3,4,9");
				
				if(lspdIdTemp.intValue()!=27){
					akseptasi.setInfo1(1);
					akseptasi.setLsposDoc(lspdPosTemp);
					//polis ini ada di lspdPosTemp.
				}else if(lsbsId.intValue()==161){//produk asm
					akseptasi.setInfo1(2);
				}else if(countSts>0){ //akseptasi khusus (further requirement, extra premi, postponed spaj)
					akseptasi.setInfo1(3);
					akseptasi.setPesan("Status Polis masih "+statusAksep+"\nSilahkan gunakan Akseptasi Khusus untuk Mengaksep Polis ini.");
					//akseptasi.set
				}	
				
				List<Datarider> listRider = (List<Datarider>) bacDao.selectDataUsulan_rider(akseptasi.getSpaj());
				Integer total_ekasehat = bacDao.selectCountEkaSehatAdmedikaNew(akseptasi.getSpaj(),2);
				
				for(int i=0;i<total_ekasehat;i++){
					ArrayList<Map> listPeserta = Common.serializableList(uwDao.selectDataPeserta(akseptasi.getSpaj()));
					if(listPeserta.isEmpty())continue;
					HashMap mapPeserta = (HashMap) listPeserta.get(i);
					for(int j=0; j<listPeserta.size();j++){
						Integer umur = Integer.parseInt(mapPeserta.get("UMUR").toString());
						if( umur>=50 && umur<=55){
							akseptasi.setInfo1(4);
						}
					}
				}
				
				akseptasi.setListSize(lsStatusAksep.size());
				akseptasi.setLsStatusAksep(lsStatusAksep);
				akseptasi.setSize(new Integer(lsStatusAksep.size()));
				lca_id = uwDao.selectCabangFromSpaj(akseptasi.getSpaj());
				akseptasi.setProses("1");
				akseptasi.setLiAksep(5);
				akseptasi.setLcaId(lca_id);
				
				if(!lca_id.equals("09")){
					//if(reas.getLiAksep()==5 | reas.getLiAksep()==10){
						Integer mspo_provider= uwDao.selectGetMspoProvider(akseptasi.getSpaj());
						if(mspo_provider==2){
							uwDao.updateMstInsuredKirimAdmedika(akseptasi.getSpaj(),akseptasi.getInsuredNo(),akseptasi.getCurrentUser().getLus_id());
							uwDao.saveMstTransHistory(akseptasi.getSpaj(), "tgl_kirim_admedika", null, null, null);
						}
							prosesEndorsemen(akseptasi.getSpaj(), Integer.parseInt(uwDao.selectBusinessId(akseptasi.getSpaj())));
						//}
				}	
				
				//String desc = "( AUTOMATED ACCEPT ) AC : NM, STD";//perubahan keterangan liat #mdesc
				String desc = "( AUTOMATED ACCEPT ) AC : "+s_medis;
				int tmp = uwDao.prosesAkseptasi(akseptasi,0,0,desc,null);
				
				suc_speedy.append("<br>- Proses Akseptasi Berhasil!");
			}catch(Exception e){
				e.getLocalizedMessage();
				error = 1;
				err_speedy.append("<br>- Proses Akseptasi Error!");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}*/
			
			//Proses transfer ke payment
			Transfer transfer=new Transfer();
			try{
				//formbacking
				transfer.setInsuredNo(new Integer(1));
				transfer.setSpaj(s_spaj);
				transfer.setInfo(new Integer(0));
				transfer.setLiPosisi(0);
				transfer.setLspdId(new Integer(27));
				transfer.setLstbId(new Integer(1));
				transfer.setCurrentUser(user);
				//cek posisi
				HashMap mtPosisi = Common.serializableMap(uwDao.selectF_check_posisi(transfer.getSpaj()));
				Integer lspdIdTemp_transfer=(Integer)mtPosisi.get("LSPD_ID");
				String lspdPosTemp_transfer=(String)mtPosisi.get("LSPD_POSITION");
				if(lspdIdTemp_transfer.intValue()!=27){
					transfer.setInfo(new Integer(1));
					transfer.setLsposDoc(lspdPosTemp_transfer);
					//posisi document ada di lspdPosTemp
				}
				//
				HashMap mtStatus = Common.serializableMap(uwDao.selectWfGetStatus(transfer.getSpaj(),transfer.getInsuredNo()));
				transfer.setLiAksep((Integer)mtStatus.get("LSSA_ID"));
				transfer.setLiReas((Integer)mtStatus.get("MSTE_REAS"));
				if (transfer.getLiAksep()==null) 
					transfer.setLiAksep(new Integer(1));
				transfer.setLiBackup((Integer)uwDao.selectMstInsuredMsteBackup(transfer.getSpaj(),transfer.getInsuredNo()));

				//
				if(transfer.getLiAksep().intValue()==2){
					transfer.setInfo(new Integer(2));
					//status spaj decline , Transfer Status Decline ke Policy Canceled ?
				}
				//dw1 //pemegang
				policy=uwDao.selectDw1Underwriting(transfer.getSpaj(),transfer.getLspdId(),transfer.getLstbId());
				if(policy!=null){
					transfer.setMspoPolicyHolder(policy.getMspo_policy_holder());
					transfer.setNoPolis(policy.getMspo_policy_no());
					transfer.setInsPeriod(policy.getMspo_ins_period());
					transfer.setPayPeriod(policy.getMspo_pay_period());
					transfer.setLkuId(policy.getLku_id());
					transfer.setUmurPp(policy.getMspo_age());
					transfer.setLcaId(policy.getLca_id());
				//

					//cek standard
					if(policy.getMste_standard().intValue()==1){
					Integer liCount=uwDao.selectCountMstProductInsuredCekStandard(transfer.getSpaj());
						if(liCount.intValue()==0){
							//Polis ini non-standard, Extra Premi Belum Ada !!!~n~nYakin Lanjutkan ?'
							transfer.setInfo(new Integer(3));
						}
					}
					transfer.setPolicy(policy);
				}
				//
				ArrayList lsProdInsured = Common.serializableList(akseptasiDao.selectMstProductInsured(transfer.getSpaj(),new Integer(1),new Integer(1)));
				String lsdbs_number = uwDao.selectLsdbsNumber(transfer.getSpaj());
				if(!lsProdInsured.isEmpty()){
					Product prodIns=(Product)lsProdInsured.get(0);
					transfer.setLsbsId(prodIns.getLsbs_id());
					transfer.setLsdbsNumber(prodIns.getLsdbs_number());
				}
				//
				transfer.setLiLama(uwDao.selectCountMstCancel(transfer.getSpaj()));
				transfer.setLsDp(uwDao.selectMstDepositPremium(transfer.getSpaj(),null));
				//
				if(transfer.getLsbsId().intValue()==157 || (transfer.getLsbsId().intValue()==196)&&lsdbs_number.equals("2")){//jika product endowment langsung transfer ke print polis 14-06-06
					transfer.setLiPosisi(6);
					transfer.setTo("Print Polis ?");
				}else{
//					if(transfer.getLsDp().isEmpty() && transfer.getLiLama().intValue() == 0){
//						transfer.setLiPosisi(10);
//						transfer.setTo("Print Speciment");
//					}else
					if (transfer.getLiLama().intValue() > 0){
						transfer.setLiPosisi(6);
						transfer.setTo("Print Polis ?");
						transfer.setLiLama(new Integer(1));
					}else{
						transfer.setLiPosisi(4);
						transfer.setTo("Pembayaran ?");
					}
				}	
				
				transfer.setInfo(new Integer(100));
				//
				
				//data usulan asuransi
				HashMap mDataUsulan = Common.serializableMap(uwDao.selectDataUsulan(transfer.getSpaj()));
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
				//Map mTertanggung=elionsManager.selectTertanggung(transfer.getSpaj());
				transfer.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
				transfer.setMsteInsured((String)mTertanggung.get("MCL_ID"));
				transfer.setMsagId((String)mTertanggung.get("MSAG_ID"));
				transfer.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
				//end form backing
				
				Integer flagNew = 1;//(0=old, 1=New) proses pembayaran baru atao lama.
				int hasil = 0;
				String pesanTambahan = null;
				HashMap allResult = new HashMap();
				//Transfer transfer=(Transfer)cmd;
				
				BindException err = null;
				
				allResult = Common.serializableMap(transferUw.prosesTransferPembayaran(transfer,flagNew,err,request));
				pesanTambahan = (String) allResult.get("pesanTambahan");
				hasil = (Integer) allResult.get("proses");
				
				//produk ERBE langsung transfer ke Print Polis (karena payment di bayar dibelakang)
				//proses ini mengikuti alur proses menu payment saat tranfer ke print polis
				String infotransfer = "";
				if (transfer.getLsbsId().intValue()==217 &&lsdbs_number.equals("2") ){
//					BindException errors = null;
//					List billInfo = this.uwDao.selectBillingInfoForTransfer(s_spaj, 1, 1);
//					if(err == null) err = new BindException(new HashMap(), "");
//					boolean hasil_to_printPolis = transferPolis.transferToPrintPolis(s_spaj, err, -1, billInfo, user, "3", "3", "payment", elionsManager, uwManager);
					String cabang = elionsManager2.selectCabangFromSpaj(s_spaj);
					boolean hasil_to_printPolis = elionsManager2.transferToPrintPolis(s_spaj, err, -1, user,  "3",  "3", "payment");
										
					if(hasil_to_printPolis==false){
						err.reject("payment.transferFailed");
						error = 1;
						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();						
					}else{
						infotransfer = "<br>- Proses Transfer ke Print Polis Berhasil!" ;
					}
					
				}else{
					
					infotransfer = "<br>- Proses Transfer ke Payment Berhasil!";
				}
				suc_speedy.append(infotransfer);
				
			}catch(Exception e){
				error = 1;
				e.printStackTrace();
				err_speedy.append("<br>- Proses Transfer ke Payment Error!");
				logger.error("ERROR :", e);
				e.getLocalizedMessage();
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
			
//			jika sukses, tampung value dalam success. jika gagal, tampung value dalam error.
			
			if(error>0){//ada error
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				//psn = "Error : "+pesan.toString();
				
				//tambahan khusus dmtm
				if(pmg.getLca_id().equals("40")){
					prosesEmailNotify(s_spaj,pmg.getMcl_first(),1);	
					//map.put("success", pmg.getReg_spaj()+" "+ "GAGAL DITRANSFER");				;
					//return map;
					err_speedy.append("<br>- "+pmg.getReg_spaj()+" "+ "GAGAL DITRANSFER");
				}
			}else{//tidak ada error
			//   TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				//psn = "Sukses : "+pesan.toString();
//				if((akseptasi.getLiAksep()==5 || akseptasi.getLiAksep()==10) && "37,52,63".indexOf(lca_id)>-1){
//					prosesEmailSMSAkseptasi(akseptasi.getSpaj(), akseptasi.getLiAksep());
//				}
				
				//tambahan khusus dmtm
				if(pmg.getLca_id().equals("40")){
					prosesEmailNotify(s_spaj,pmg.getMcl_first(),2);	
					//map.put("success", pmg.getReg_spaj()+" "+ "Ditransfer Ke Payment(CLEAR CASE)");				;
					//return map;
					suc_speedy.append("<br>- "+pmg.getReg_spaj()+" "+ "Ditransfer Ke Payment(CLEAR CASE)");
				}
			}
		}
		
		map.put("success", suc_speedy);
		map.put("error", err_speedy);
		map.put("flag_error", error);
		map.put("flag_print", flag_print);
		return map;
	}
	
	public void transferPosisi(User user, String spaj, Integer lspd_id_tujuan, String keterangan, String kolom_trans_history, Integer flag_further) {
		//status aksep jadi further dan keterangan di tambah keterangan further
		if (lspd_id_tujuan==1){
        	uwDao.updateMstInsured(spaj, lspd_id_tujuan, 1, 1, null);
        }else if(lspd_id_tujuan==218){
        	uwDao.updateMstInsured(spaj, lspd_id_tujuan, 17, 1, null);
        }else{
        	uwDao.updateMstInsured(spaj, lspd_id_tujuan);
        }
        
        if(lspd_id_tujuan==209){
        	uwDao.updateMstInsuredStatus(spaj, new Integer(1), new Integer(3), new Integer(1), null, null);
        	
        	uwDao.insertMstPositionSpaj(user.getLus_id(), "FR: "+keterangan, spaj, 0);
        	uwDao.insertMstPositionSpaj(user.getLus_id(), "TRANSFER KE U/W HELPDESK", spaj, 1);
        	
        	uwDao.saveMstTransHistory(spaj, "tgl_further", null, null, null);
        }else{
        	if(flag_further!=null){
        		if(flag_further==0){
        			if(lspd_id_tujuan==1) uwDao.insertMstPositionSpajWithSubIdBas(user.getLus_id(), keterangan, spaj, null, 15);
        			if(lspd_id_tujuan==27) uwDao.insertMstPositionSpajWithSubIdBas(user.getLus_id(), keterangan, spaj, null, 16);
        		}
        	}else{
        		uwDao.insertMstPositionSpaj(user.getLus_id(), keterangan, spaj, 0);
        	}
        }
        
		uwDao.updateMstPolicy(spaj,lspd_id_tujuan);
		
		if(kolom_trans_history != null) uwDao.saveMstTransHistory(spaj, kolom_trans_history, null, null, null);
		
		if(lspd_id_tujuan==1) uwDao.prosesSnows(spaj, user.getLus_id(), 207, 212);
		else if(lspd_id_tujuan==209) uwDao.prosesSnows(spaj, user.getLus_id(), 209, 212);
		else if(lspd_id_tujuan==2) uwDao.prosesSnows(spaj, user.getLus_id(), 210, 212);
		else if(lspd_id_tujuan==218) uwDao.prosesSnows(spaj, user.getLus_id(), 218, 212);
		else if(lspd_id_tujuan==27) uwDao.prosesSnows(spaj, user.getLus_id(), 202, 212);
	}
	
	/**
	 * Select Komisi Pending Agent
	 * @author 	Canpri
	 * @since 22 Aug 2014
	 */
	public List selectKomisiPendingAgent(String s_msag_id, String s_polis) {
		return basDao.selectKomisiPendingAgent(s_msag_id,s_polis);
	}

	//cek fund sudah apa belum
	public Integer cekFund(String spaj) {
		return uwDao.cekFund(spaj);
	}
	
//	public List selectDataLQG(Integer type, Date question_valid_date) {
//		// TODO Auto-generated method stub
//		return Common.serializableList(uwDao.selectDataLQG(type, question_valid_date));
//	}
	
	public List selectDataLQG(Integer type, Date question_valid_date ,Integer index, Integer index2) {
		// TODO Auto-generated method stub
		return Common.serializableList(uwDao.selectDataLQG(type, question_valid_date,index, index2));
	}
	
	public List selectDataLQL(Integer type , Date question_valid_date, Integer index, Integer index2) {
		// TODO Auto-generated method stub
		return Common.serializableList(uwDao.selectDataLQL(type,question_valid_date,index,index2));
	}
	
	public MedQuest selectHasilQuest(String spaj, int flag_jenis_peserta) {
		// TODO Auto-generated method stub
		return (MedQuest) this.uwDao.selectHasilQuest(spaj,flag_jenis_peserta);
	}	
	
	public List selectJawabanMedicalALL(String spaj, int flag_jenis_peserta) {
		// TODO Auto-generated method stub
		return this.uwDao.selectJawabanMedicalALL(spaj,flag_jenis_peserta);
	}	
	
	public void insertQuestionareNew ( Integer key_type , String key_id, Integer q_type_id , Integer q_id, Date q_valid_date, Integer op_type, Integer op_grp,
			Integer op_order,Integer an_order , String answer, Date input_date,Integer lus_id) {
		bacDao.insertQuestionareNew( key_type ,key_id, q_type_id, q_id, q_valid_date,  op_type, op_grp, op_order,an_order, answer,input_date, lus_id);
	}
	
	public List selectQuestionareNew(String spaj , Integer type ,  Integer index, Integer index2) {
		// TODO Auto-generated method stub
		return Common.serializableList(bacDao.selectQuestionareNew(spaj, type, index,index2));
	}
	
	public List <MstQuestionAnswer> selectQuestionareGadget(String spaj , Integer type ,  Integer index, Integer index2) {
		return bacDao.selectQuestionareGadget(spaj, type, index,index2);
	}
	
	public void deleteQuestionareNew ( String key_id, String kata) {
		bacDao.deleteQuestionareNew(key_id, kata);
	}
		
	public void hitungReward(String spaj, User currentUser) {
		try {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			
			//INI HANYA BOLEH DIGUNAKAN, BILA DI ROLLBACK
			commonDao.delete("DELETE FROM EKA.MST_REWARD WHERE REG_SPAJ = '" + spaj + "' AND (msbi_tahun_ke,msbi_premi_ke) in (select msbi_tahun_ke, msbi_premi_ke from eka.mst_billing where reg_spaj = '" + spaj + "' and msbi_nb = 1)");
			String lca_id = uwDao.selectCabangFromSpaj(spaj);
			if("58, 63, 65".indexOf(lca_id)==-1){
				transferPolis.testHitungReward(spaj, currentUser);
				
				Ulangan ulangan = new Ulangan();
				ulangan.setReg_spaj(spaj);
				ulangan.setJenis("ADD REWARD");
				ulangan.setStatus_polis(null);
				ulangan.setLus_id(Integer.parseInt(currentUser.getLus_id()));
				ulangan.setKeterangan("INSERT KETINGGALAN REWARD DIKARENAKAN FLAG_ACTIVE YG DILIHAT DARI TABLE REKRUTER, BUKAN TABLE MASTER AGENT");
				uwDao.insertLstUlangan(ulangan);
			}
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
	}
	
	public Integer selectPengaliCaraBayar(String reg_spaj) {		
		return uwDao.selectPengaliCaraBayar(reg_spaj);
	}
	
	public Double selectRateUpScholarship(Integer lsbs_id, Integer umurTertanggung, Integer lsbs_number) {
		return uwDao.selectRateUpScholarship(lsbs_id, umurTertanggung, lsbs_number);
	}
	
	public void updateQuestionareNewPPTT(String mcl_id, String kolom, Double answer){	
		bacDao.updateQuestionareNewPPTT(mcl_id, kolom, answer);
	}

	public Integer selectCountQuestionareNew(String key_id) {
		return bacDao.selectCountQuestionareNew(key_id);
	}
	
	public void saveMstTransHistory(String reg_spaj, String kolom_tgl, Date tgl, String kolom_user, String lus_id) {
		uwDao.saveMstTransHistory(reg_spaj, kolom_tgl, tgl, kolom_user, lus_id);
	}	

	public List<DropDown> selectLstPekerjaan() {
		return bacDao.selectLstPekerjaan();
	}
	
	public String getUrlDatabase(String url) {
		
		return basDao.getUrlDatabase(url);
	}
	
	/**@Fungsi:	sebagai manager atau pengatur dari endorsment polis
	 * @param 	spaj ,BindException err 
	 * @author 	Rahmayanti
	 * */
	
	public void updateMspoProvider(String spaj, Integer  mspo_provider)
	{
		uwDao.updateMspoProvider(spaj, mspo_provider);				
	}
	
	public void updateMstProductInsured(String spaj, Integer up)
	{
		bacDao.updateMstProductInsured(spaj, up);
	}
	
	public List reportSimasSpt(String bdate, String edate)
	{
		return basDao.reportSimasSpt(bdate, edate);
	}
	
	public List reportSimasNonSpt(String bdate, String edate)
	{
		return basDao.reportSimasNonSpt(bdate, edate);
	}
			
	public Integer snowsdireksi(User currentUser,String reg_spaj,String dir, Integer flag_aksep, String inbox, String jns, String idx_aksep) throws Exception{
		Date nowDate = commonDao.selectSysdate();
		StringBuffer pesan = new StringBuffer();	
		Integer months = nowDate.getMonth()+1;
		Integer years = nowDate.getYear()+1900;
		String dest="";//dir+"//"+reg_spaj.trim()+"//";
	    String fileName="test";//reg_spaj.trim()+"REKKLAIM"+" 001.pdf";
	    String from="ajsjava@sinarmasmsiglife.co.id";
	    
	    String ket ="";
    	String email_kirim_cc ="";
    	String email_kirim_to ="";
    	String subject_kirim ="";
    	String subject_kirim_info ="";
    	String email_approve_to ="";
    	String email_approve_cc ="";
    	String subject_approve ="";

    	String sender_name ="";
    	String post_desc ="";
    	String post_jn_desc ="";
    	String post_desc_approve ="";
    	String email_cc_akseptor ="";
    	
    	String lus_akseptor ="";
    	String path_ttd ="";
    	String email_akseptor ="";
    	String name_akseptor ="";
    	
    	try {
    	List<Map> m = bacDao.selectSnowsDireksi(jns);
		if(!m.isEmpty()){
			
			if(StringUtils.isEmpty(idx_aksep)){
				idx_aksep = "1";
			}
			
			Map snDireksi= (Map)m.get(0);
			ket=(String)snDireksi.get("KET");
			email_kirim_cc=(String)snDireksi.get("EMAIL_KIRIM_CC");
			email_kirim_to=(String)snDireksi.get("EMAIL_KIRIM_TO");
			subject_kirim=(String)snDireksi.get("SUBJECT_KIRIM");
			subject_kirim_info=(String)snDireksi.get("SUBJECT_KIRIM_INFO");
			email_approve_to=(String)snDireksi.get("EMAIL_APPROVE_TO");
			email_approve_cc=(String)snDireksi.get("EMAIL_APPROVE_CC");
			subject_approve=(String)snDireksi.get("SUBJECT_APPROVE");
			
			sender_name=(String)snDireksi.get("SENDER_NAME");
			post_desc=(String)snDireksi.get("POST_DESC");
			post_jn_desc=(String)snDireksi.get("POST_JN_DESC");
			post_desc_approve=(String)snDireksi.get("POST_DESC_APPROVE");
			email_cc_akseptor=(String)snDireksi.get("EMAIL_CC_AKSEPTOR_" + idx_aksep);
			
			lus_akseptor=(String)snDireksi.get("LUS_AKSEPTOR_" + idx_aksep).toString();
			path_ttd=(String)snDireksi.get("PATH_TTD_" + idx_aksep);
			email_akseptor=(String)snDireksi.get("EMAIL_AKSEPTOR_" + idx_aksep);
			name_akseptor =(String)snDireksi.get("NAME_AKSEPTOR_" + idx_aksep);
			
		}
	    
	    	//String emailto ="trifena_y@sinarmasmsiglife.co.id;";
	    	String emailto ="deddy@sinarmasmsiglife.co.id;";
	    	String cc =null;
	    	//String []cc =null;
	    	String []bcc=null;
	    	
	    	String subject = "";
	    	String desc_1 = "";
	    	String desc_2 = "";
	    	
			emailto = email_akseptor;
    		cc = email_kirim_cc;
    		desc_1 = post_desc;
    		desc_2 = post_jn_desc;
    		
    		//bcc = new String[]{"trifena_y@sinarmasmsiglife.co.id;randy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id;"};
    		bcc = new String[]{"deddy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id;avnel@sinarmasmsiglife.co.id;"};
    		
    		
	    	if(!emailto.trim().equals("")){
		   		String[] to = (emailto+cc+email_cc_akseptor).split(";");						   		
		   		List<DropDown> ListImage = new ArrayList<DropDown>();								
				String name=null;
				String act_lus_akseptor=null;
				
				for(int j=0;j<to.length;j++){
					act_lus_akseptor=null;
					subject = subject_kirim_info;
					String me_id = sequence.sequenceMeIdEmail();
					String destTo=props.getProperty("embedded.mailpool.dir")+"\\" +years+"\\"+FormatString.rpad("0", months.toString(), 2)+"\\"+me_id+"\\";
					File destDir = new File(destTo);
					if(!destDir.exists()) destDir.mkdirs();
					String fileName2 = "";//reg_spaj+"REKKLAIM";
					//String imageKlaim=fileName2+"1";
					
					uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), desc_1, reg_spaj, 1, desc_2);
					
					//dest = "\\\\ebserver\\pdfind\\Polis\\05\\05201200065\\CAIR_SBG_STBLE_2014039332\\";
					dest = uwDao.selectMstInboxMiUrl(inbox);
					int ln_inbox = inbox.length();
					int dest_idx = dest.indexOf(inbox);
					fileName = dest.substring(dest.indexOf(inbox)+ln_inbox+1);
					fileName2 = fileName;
					dest = dest.replace(fileName, "");
//					dest = dest.replace("\\", "\\\\");
					
					PDFToImage.convertPdf2Image(dest,  destTo, fileName, fileName2,null,null);
					
					StringBuffer pesan2 = new StringBuffer();	
		   		    String [] sendingTo = new String[] {to[j]};	
		   		    
		   		    if (to[j].equals(email_akseptor.replaceAll(";", ""))){
		   		    	act_lus_akseptor = lus_akseptor;
		   		    	name= name_akseptor;
		   		    	subject = subject_kirim;
		   		    }
		//
//			    	Untuk melihat info lebih detail silahkan lihat viewer, klaim kematian / klaim kesehatan
		   		    pesan2.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: yellow;} table td{border: 1px solid black;}</style></head>");
	   		 		pesan2.append("<body bgcolor='#ffffc9'>"+subject+".<br><br>");
	   		 		pesan2.append("<left><img border='0' src='cid:"+fileName+"1.jpg'></left><br><br>");
//		   		 	if("putri_ayu@sinarmasmsiglife.co.id".indexOf(to[j])>-1 || "anna_yulia@sinarmasmsiglife.co.id".indexOf(to[j])>-1){
//		   		 	if("andy@sinarmasmsiglife.co.id".indexOf(to[j])>-1 || "avnel@sinarmasmsiglife.co.id".indexOf(to[j])>-1){
	   		 		if(!StringUtil.isEmpty(act_lus_akseptor)){
	   		 			
		   		 		String link1 = "http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=snowsdireksiaksep&id="+lus_akseptor+"&spaj="+reg_spaj+"&inbox="+inbox+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"1"+"&jns="+jns+"&idx="+idx_aksep;
	   		 			String link2 = "http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=snowsdireksiaksep&id="+lus_akseptor+"&spaj="+reg_spaj+"&inbox="+inbox+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"2"+"&jns="+jns+"&idx="+idx_aksep;
		   		 		String tkey1 = "";
		   		 		String tkey2 = "";
				   		try {
				   			tkey1 = commonDao.encryptUrlKey("snowsdireksi", reg_spaj, App.ID, link1);
				   			tkey2 = commonDao.encryptUrlKey("snowsdireksi", reg_spaj, App.ID, link2);
				   		}catch (Exception e) {
							logger.error("ERROR", e);
						}
				   		link1 = link1 + "&tkey="+tkey1;
				   		link2 = link2 + "&tkey="+tkey2;
	   		 			
		   		 		/*pesan2.append("<a href='http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=snowsdireksiaksep&id="+lus_akseptor+"&spaj="+reg_spaj+"&inbox="+inbox+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"1"+"&jns="+jns+"&tkey="+tkey+"'><font size='5'>Approve</font></a>\t");						   		 		
		   		 		pesan2.append("<a href='http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=snowsdireksiaksep&id="+lus_akseptor+"&spaj="+reg_spaj+"&inbox="+inbox+"&email="+currentUser.getEmail()+"&lde="+currentUser.getLde_id()+"&name="+name+"&status="+"2"+"&jns="+jns+"&tkey="+tkey+"'><font color='#fd020e' size='5'>Decline</font></a><br><br>");*/
		   		 		pesan2.append("<a href='"+link1+"'><font size='5'>Approve</font></a>\t");		
		   		 		pesan2.append("<a href='"+link2+"'><font color='#fd020e' size='5'>Decline</font></a><br><br>");
		   		 	}
	   		 		pesan2.append("<br>Sender: "+sender_name);
	   		 		pesan2.append("<br>To: "+emailto);
	   		 		pesan2.append("<br>Cc: "+cc);
	   		 		pesan2.append("<br></body></html>");
			   		EmailPool.send(me_id,"SMiLe E-Lions", 0, 0, 0, 0, 
							null, 0, 0, nowDate, null, 
							true, from, 
							sendingTo, 
							null, 
							bcc, 
							subject, 
							pesan2.toString(), 
							null,8);
				}
	    	}
	    	String mi_id = "";
	    	HashMap hist = null;
	    	List <Map> mapInbox = uwDao.selectMstInbox(reg_spaj, null);
	    	hist = (HashMap) mapInbox.get(0);
	    	mi_id = (String) hist.get("MI_ID");
	    	MstInboxHist mstInboxHist = new MstInboxHist(mi_id, null, null, null, null, desc_1, Integer.parseInt(lus_akseptor), nowDate, null,0,0);
	    	uwDao.insertMstInboxHist(mstInboxHist);
	    }catch (MailException e) {
			logger.error("ERROR :", e);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, from, 
					new String[]{"andy@sinarmasmsiglife.co.id"}, 
					new String[]{"trifena_y@sinarmasmsiglife.co.id"},
					null, 
					"Notifikasi Pengecekkan Snows Direksi",
					pesan.toString(), 
					null, null);
		} catch (MessagingException e) {
			logger.error("ERROR :", e);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, from, 
					new String[]{"andy@sinarmasmsiglife.co.id"}, 
					new String[]{"trifena_y@sinarmasmsiglife.co.id"},
					null, 
					"Notifikasi Pengecekkan Snows Direksi",
					pesan.toString(), 
					null, null);
		} catch (Exception e) {
			logger.error("ERROR :", e);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, from, 
					new String[]{"andy@sinarmasmsiglife.co.id"}, 
					new String[]{"trifena_y@sinarmasmsiglife.co.id"},
					null, 
					"Notifikasi Pengecekkan Snows Direksi",
					pesan.toString(), 
					null, null);
		}finally{
			AksesHist a = new AksesHist();
			a.setLus_id(Integer.parseInt(lus_akseptor));
			a.setMsah_date(nowDate);
			a.setMsah_jenis(1);
			a.setMsah_spaj(reg_spaj);
			a.setMsah_uri("/UW/UW.HTM?window=snowsdireksi");
			
			commonDao.insertAksesHist(a);
		}
	   
	    return flag_aksep;
	}
	
	public Integer snowsdireksiaksep(User currentUser,String reg_spaj,String dir, Integer status, String inbox, String jns, String idx_aksep) throws Exception{
		Date nowDate = commonDao.selectSysdate();
		StringBuffer pesan = new StringBuffer();	
		Integer months = nowDate.getMonth()+1;
		Integer years = nowDate.getYear()+1900;
		String dest="";//dir+"//"+reg_spaj.trim()+"//";
	    String fileName="test";//reg_spaj.trim()+"REKKLAIM"+" 001.pdf";
	    String from="ajsjava@sinarmasmsiglife.co.id";
	    
    	String email_kirim_cc ="";
    	String email_kirim_to ="";
    	String subject_kirim ="";
    	String subject_kirim_info ="";
    	String email_approve_to ="";
    	String email_approve_cc ="";
    	String subject_approve ="";
    	String lus_akseptor ="";
    	String path_ttd ="";
    	String email_akseptor_1 ="";
    	String email_akseptor ="";
    	String sender_name ="";
    	String post_desc ="";
    	String post_jn_desc ="";
    	String post_desc_approve ="";
    	String name_akseptor ="";
    	String email_cc_akseptor ="";
    	int exist_aksep_next = 0;
    	int idx_aksep_next = 0;
    	
    	try {
    	
    	List<Map> m = bacDao.selectSnowsDireksi(jns);
		if(!m.isEmpty()){
			Map snDireksi= (Map)m.get(0);
			
			email_kirim_cc=(String)snDireksi.get("EMAIL_KIRIM_CC");
			email_kirim_to=(String)snDireksi.get("EMAIL_KIRIM_TO");
			subject_kirim=(String)snDireksi.get("SUBJECT_KIRIM");
			subject_kirim_info=(String)snDireksi.get("SUBJECT_KIRIM_INFO");
			email_approve_to=(String)snDireksi.get("EMAIL_APPROVE_TO");
			email_approve_cc=(String)snDireksi.get("EMAIL_APPROVE_CC");
			subject_approve=(String)snDireksi.get("SUBJECT_APPROVE");
			
			sender_name=(String)snDireksi.get("SENDER_NAME");
			post_desc=(String)snDireksi.get("POST_DESC");
			post_jn_desc=(String)snDireksi.get("POST_JN_DESC");
			post_desc_approve=(String)snDireksi.get("POST_DESC_APPROVE");
			email_cc_akseptor=(String)snDireksi.get("EMAIL_CC_AKSEPTOR_2");
			email_akseptor_1 = (String)snDireksi.get("EMAIL_AKSEPTOR_1");
			
			lus_akseptor=(String)snDireksi.get("LUS_AKSEPTOR_" + idx_aksep).toString();
			path_ttd=(String)snDireksi.get("PATH_TTD_" + idx_aksep);
			email_akseptor=(String)snDireksi.get("EMAIL_AKSEPTOR_" + idx_aksep);
			name_akseptor =(String)snDireksi.get("NAME_AKSEPTOR_" + idx_aksep);
			
			idx_aksep_next = Integer.parseInt(idx_aksep) + 1;
			if(snDireksi.get("LUS_AKSEPTOR_" + idx_aksep_next) != null){
				exist_aksep_next = 1;
			}
			
		}
	    
	    
            
	    	String emailto ="deddy@sinarmasmsiglife.co.id;";
	    	//String emailto ="trifena_y@sinarmasmsiglife.co.id;";
	    	String []cc =null;
	    	String []bcc=null;
//	    			emailto ="Eri@sinarmasmsiglife.co.id;Evi_r@sinarmasmsiglife.co.id;";
//	    			cc = new String[]{"helena@sinarmasmsiglife.co.id;mieyoen@sinarmasmsiglife.co.id;"};
	    	
	    	/*
	    	if("1".equals(jns) || "2".equals(jns)){
	    		emailto ="Eri@sinarmasmsiglife.co.id;";
		    	cc = new String[]{"helena@sinarmasmsiglife.co.id;indrasanjaya@sinarmasmsiglife.co.id;"};
//	    		emailto ="andy@sinarmasmsiglife.co.id;";
	    	}else if("3".equals(jns) || "4".equals(jns)){
	    		emailto ="Kamarudinsyah@sinarmasmsiglife.co.id;";
		    	cc = new String[]{"Ety@sinarmasmsiglife.co.id;indrasanjaya@sinarmasmsiglife.co.id;"};
//	    		emailto ="avnel@sinarmasmsiglife.co.id;";
	    	}
	    	*/
	    	
	    	String desc_1 = "";
	    	
	    	emailto =email_approve_to;
	    	cc = new String[]{email_approve_cc};
	    	desc_1 = post_desc_approve;
	    	
	    	/*if("1".equals(jns)){
	    		emailto ="Eri@sinarmasmsiglife.co.id;";
		    	cc = new String[]{"helena@sinarmasmsiglife.co.id;anna@sinarmasmsiglife.co.id;"};
//	    		emailto ="andy@sinarmasmsiglife.co.id;";
		    	
	    		desc_1 = "POLICY LOAN / WITHDRAWAL PROCESS";
	    	}else if("2".equals(jns)){
	    		emailto ="Kamarudinsyah@sinarmasmsiglife.co.id;";
		    	cc = new String[]{"Ety@sinarmasmsiglife.co.id;anna@sinarmasmsiglife.co.id;"};
//	    		emailto ="avnel@sinarmasmsiglife.co.id;";
		    	desc_1 = "POLICY LOAN / WITHDRAWAL PROCESS";
	    	}else if("3".equals(jns)){
	    		emailto ="Kamarudinsyah@sinarmasmsiglife.co.id;";
		    	cc = new String[]{"Ety@sinarmasmsiglife.co.id;taufik_r@sinarmasmsiglife.co.id;budiaji@sinarmasmsiglife.co.id;anna@sinarmasmsiglife.co.id;"};
//	    		emailto ="avnel@sinarmasmsiglife.co.id;";
		    	desc_1 = "POLICY LOAN / WITHDRAWAL PROCESS";
	    	}else if("4".equals(jns) || "5".equals(jns) || "6".equals(jns)){
	    		emailto ="kartika@sinarmasmsiglife.co.id;";
		    	cc = new String[]{"sunarti@sinarmasmsiglife.co.id;"};
//	    		emailto ="avnel@sinarmasmsiglife.co.id;";
		    	desc_1 = "PAYMENT HEALTHY / DEATH CLAIM PROCESS";
	    	}*/
	    	
//	    	emailto ="andy@sinarmasmsiglife.co.id;avnel@sinarmasmsiglife.co.id;agustinus@sinarmasmsiglife.co.id;";
//	    	cc = new String[]{"andy@sinarmasmsiglife.co.id;avnel@sinarmasmsiglife.co.id;agustinus@sinarmasmsiglife.co.id;"};
	    	
	    			bcc = new String[]{"deddy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id;avnel@sinarmasmsiglife.co.id;"};
	    			//bcc = new String[]{"trifena_y@sinarmasmsiglife.co.id;randy@sinarmasmsiglife.co.id;andy@sinarmasmsiglife.co.id;"};
	    			
	    	String subject= subject_approve +FormatString.nomorSPAJ(reg_spaj);
	    	
	    	if(!emailto.trim().equals("")){
		   		List<DropDown> ListImage = new ArrayList<DropDown>();								
				String name=null;
		    		
				String keputusan = "";
		    	if(status ==1){//approve
		    		uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), desc_1+" ACCEPT", reg_spaj, 1,"APPROVAL "+desc_1);
		    		int wait = 1;
		    		int app = 2;
		    		if("1".equals(idx_aksep)){
		    			MstInbox mstInbox = new MstInbox(inbox, null, null, null, null, null, null, reg_spaj, null, null, null, null, null, null, null, null, null, app, Integer.parseInt(currentUser.getLus_id()), 1, null, null, null, null, null, null, null,null, 0,null);
		    			if(exist_aksep_next == 1){
		    				mstInbox.setApp_4(wait);
		    			}
		    			uwDao.updateMstInbox(mstInbox);
		    		}else if("2".equals(idx_aksep)){
		    			MstInbox mstInbox = new MstInbox(inbox, null, null, null, null, null, null, reg_spaj, null, null, null, null, null, null, null, null, null, null, null, 1, null, null, null, null, null, null, null,null, 0,null);
		    			mstInbox.setApp_4(app);
		    			mstInbox.setUser_app_4(Integer.parseInt(currentUser.getLus_id()));
		    			uwDao.updateMstInbox(mstInbox);
		    		}
		    		
		    		keputusan = "diapprove";
		    		
		    		//kondisi untuk rise up
		    		if(exist_aksep_next == 1){
		    			currentUser.setLus_id(lus_akseptor);
		    			currentUser.setName(name_akseptor);
		    			snowsdireksi(currentUser, reg_spaj.trim(), dir, 0, inbox, jns, idx_aksep_next+"");
		    		}
		    		
		    		
		    	}else if(status ==2){//decline
		    		uwDao.insertMstPositionSpajSpt(currentUser.getLus_id(), desc_1+" DECLINE", reg_spaj, 1,"DECLINE "+desc_1);
		    		int decline = 3;
		    		if("1".equals(idx_aksep)){
		    			MstInbox mstInbox = new MstInbox(inbox, null, null, null, null, null, null, reg_spaj, null, null, null, null, null, null, null, null, null, decline, Integer.parseInt(currentUser.getLus_id()), 1, null, null, null, null, null, null, null,null, 0,null);
		    			uwDao.updateMstInbox(mstInbox);
		    		}else if("2".equals(idx_aksep)){
		    			MstInbox mstInbox = new MstInbox(inbox, null, null, null, null, null, null, reg_spaj, null, null, null, null, null, null, null, null, null, null, null, 1, null, null, null, null, null, null, null,null, 0,null);
		    			mstInbox.setApp_4(decline);
		    			mstInbox.setUser_app_4(Integer.parseInt(currentUser.getLus_id()));
		    			uwDao.updateMstInbox(mstInbox);
		    		}
		    		
		    		keputusan = "didecline";
		    	}
				
		    	if("2".equals(idx_aksep)){
		    		emailto = emailto + email_akseptor_1 + email_cc_akseptor;
		    	}
		    	String[] to = emailto.split(";");
				//for(int j=0;j<to.length;j++){
					String me_id = sequence.sequenceMeIdEmail();
					
					StringBuffer pesan2 = new StringBuffer();	
		   		    //String [] sendingTo = new String[] {to[j]};	
		//
//			    	Untuk melihat info lebih detail silahkan lihat viewer, klaim kematian / klaim kesehatan
		   			pesan2.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: yellow;} table td{border: 1px solid black;}</style></head>");
	   		 		pesan2.append("<body bgcolor='#ffffc9'>Telah "+keputusan+" oleh direksi untuk SPAJ NO: "+FormatString.nomorSPAJ(reg_spaj)+" <br><br>");
	   		 		pesan2.append("<left>#F_"+path_ttd+"#</left><br><br>");
	   		 		pesan2.append("<body bgcolor='#ffffc9'><left>"+name_akseptor+"</left><br><br>");
	   		 		pesan2.append("</body></html>");
			   		EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, nowDate, null, 
							true, from, 
//							new String[]{"deddy@sinarmasmsiglife.co.id"}, 
//							new String[]{"deddy@sinarmasmsiglife.co.id"},  
							to, 
							cc, 
							bcc, 
							subject, 
							pesan2.toString(), 
							null,8);
				//}
	    	} 
	    	
	    	String mi_id = "";
	    	HashMap hist = null;
	    	List <Map> mapInbox = uwDao.selectMstInbox(reg_spaj, null);
	    	hist = (HashMap) mapInbox.get(0);
	    	mi_id = (String) hist.get("MI_ID");
	    	MstInboxHist mstInboxHist = new MstInboxHist(mi_id, null, null, null, null, desc_1, Integer.parseInt(lus_akseptor), nowDate, null,0,0);
	    	uwDao.insertMstInboxHist(mstInboxHist);
	    }catch (MailException e) {
	    	logger.error("ERROR :", e);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, from, 
					new String[]{"andy@sinarmasmsiglife.co.id"}, 
					new String[]{"trifena_y@sinarmasmsiglife.co.id"},
					null, 
					"Notifikasi Pengecekkan Snows Direksi",
					pesan.toString(), 
					null, null);
	    } catch (MessagingException e) {
			logger.error("ERROR :", e);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, from, 
					new String[]{"andy@sinarmasmsiglife.co.id"}, 
					new String[]{"trifena_y@sinarmasmsiglife.co.id"},
					null, 
					"Notifikasi Pengecekkan Snows Direksi",
					pesan.toString(), 
					null, null);
		} catch (Exception e) {
			logger.error("ERROR :", e);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, from, 
					new String[]{"andy@sinarmasmsiglife.co.id"}, 
					new String[]{"trifena_y@sinarmasmsiglife.co.id"},
					null, 
					"Notifikasi Pengecekkan Snows Direksi",
					pesan.toString(), 
					null, null);
		} finally{
			AksesHist a = new AksesHist();
			a.setLus_id(Integer.parseInt(lus_akseptor));
			a.setMsah_date(nowDate);
			a.setMsah_jenis(1);
			a.setMsah_spaj(reg_spaj);
			a.setMsah_uri("/UW/UW.HTM?window=snowsdireksiaksep");
			
			commonDao.insertAksesHist(a);
		}
	   
	    return status;
	    
	}

	public boolean prosesPolisRetur(String spaj, String bdate, String proses, String alasan, String s_noresi,  User currentUser) throws ParseException {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		boolean hasil = false;
		Integer lssa_id,lssp_id,lspd_id;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		ArrayList<HashMap> dataPolis=Common.serializableList(uwDao.selectDataPemegangPolisSpt(spaj));
		if(!dataPolis.isEmpty()){
			
			if(proses.equals("01")){
				
				String from = currentUser.getEmail();
//				String emailto = "" ;//= "qadmtm@sinarmasmsiglife.co.id";
				//String emailcc = ""; //= "uwprinting@sinarmasmsiglife.co.id;TeamLeaderCS-FL@sinarmasmsiglife.co.id;tri.w.utami@banksinarmas.com;Maria_P@sinarmasmsiglife.co.id; Lylianty@sinarmasmsiglife.co.id;";
				
				String [] email_to;
				String [] email_cc;
				String [] email_bcc;
				StringBuffer pesan = new StringBuffer();
				String subject ="";
				HashMap mapEmail = new HashMap();
				mapEmail = null;
				HashMap mapDataPolis=(HashMap)dataPolis.get(0);
				String lca_id = (String)mapDataPolis.get("LCA_ID");
				String lwk_id = (String)mapDataPolis.get("LWK_ID");
				String lsrg_id = (String)mapDataPolis.get("LSRG_ID");
				subject = "Polis Retur "+(String)mapDataPolis.get("MSPO_POLICY_NO_FORMAT");
				if(lca_id.equalsIgnoreCase("40")){
					//CABANG DMTM
					if(lwk_id.equalsIgnoreCase("01")){
						if(lsrg_id.equals("00")){//Roxy
							mapEmail = uwDao.selectMstConfig(6, "prosesPolisRetur", "PROSES_POLIS_RETUR_DMTM");
						}else if(lsrg_id.equals("02")){
							mapEmail = uwDao.selectMstConfig(6, "prosesPolisRetur", "PROSES_POLIS_RETUR_BSIMDMTM");
						}
					}else if(lwk_id.equalsIgnoreCase("02")){
						if(lsrg_id.equals("00")){
							mapEmail = uwDao.selectMstConfig(6, "prosesPolisRetur", "PROSES_POLIS_RETUR_ARCO");
						}else if(lsrg_id.equals("01")){
							mapEmail = uwDao.selectMstConfig(6, "prosesPolisRetur", "PROSES_POLIS_RETUR_SMP");
						}else if(lsrg_id.equals("02")){
							mapEmail = uwDao.selectMstConfig(6, "prosesPolisRetur", "PROSES_POLIS_RETUR_SSS");
						}else if(lsrg_id.equals("03")){
							mapEmail = uwDao.selectMstConfig(6, "prosesPolisRetur", "PROSES_POLIS_RETUR_SIP");
						}else if(lsrg_id.equals("04")){
							mapEmail = uwDao.selectMstConfig(6, "prosesPolisRetur", "PROSES_POLIS_RETUR_REDBERRY");
						}else if(lsrg_id.equals("05")){
							mapEmail = uwDao.selectMstConfig(6, "prosesPolisRetur", "PROSES_POLIS_RETUR_PSJS");
						}
					}
					
					if(mapEmail==null){
						mapEmail = uwDao.selectMstConfig(6, "prosesPolisRetur", "PROSES_POLIS_RETUR_DEFAULT");
					}
					
					email_to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
					email_cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
					email_bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
					
				}else{
					//CABANG SELAIN DMTM
					
					Map mapBCAM = uwDao.selectEmailBCAM(spaj);
//					if(mapBCAM != null){
						email_to = mapBCAM.get("EMAIL_BC")!=null?mapBCAM.get("EMAIL_BC").toString().split(";"):null;
						email_cc = new String[]{"uwprinting@sinarmasmsiglife.co.id;"};
						email_bcc = null;
//					}
				}
				

				pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
				pesan.append("<body>Dear Team,<br><br>");
				pesan.append("Berikut data polis retur yang kami terima.<br><br>");
			
				pesan.append("<table cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1000px'>");
				pesan.append("<tr bgcolor='#A8B8EE'>");
				pesan.append("<th>Pemegang Polis</th>");
				pesan.append("<th>Nomor Polis</th>");
				pesan.append("<th>Tanggal Terima</th>");
				pesan.append("<th>No.Resi</th>");
				pesan.append("<th>Informasi Alasan Return</th></th></tr>");				
				pesan.append("<tr><td nowrap>"+ mapDataPolis.get("PEMEGANG") + "</td>");
				pesan.append("<td nowrap>"+ mapDataPolis.get("MSPO_POLICY_NO_FORMAT") + "</td>");
				pesan.append("<td nowrap>"+ bdate + "</td>");
				pesan.append("<td nowrap>"+ mapDataPolis.get("MSPO_NO_PENGIRIMAN") + "</td>");
				pesan.append("<td nowrap>"+ alasan+ "</td></tr>");
				pesan.append("</table><br><br>");
				
				pesan.append("Mohon dikirim kekurangan informasi ke Underwriting Printing untuk dikirim ulang.<br><br>");
				pesan.append("Terima Kasih.<br><br>");
			
				pesan.append("</body></html>");
				
				String me_id = sequence.sequenceMeIdEmail();
				 
		 		hasil = EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, commonDao.selectSysdate(), null, 
						true, from,												
						email_to, 
						email_cc, 
						email_bcc, 
						subject, 
						pesan.toString(),
						null,11);
		 		
		 		if(hasil){
					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "POLIS RETUR: "+alasan, spaj, 0);
					uwDao.saveMstTransHistory(spaj, "tgl_polis_retur", df.parse(bdate), null, null);
//					uwDao.saveMstTransHistory(spaj, "tgl_kirim_ulang_polis_retur", null, null, null);
					uwDao.updateTransHistoryDelPolisRetur(spaj);
					
		 		}
		 		
			}else if(proses.equals("02")){
				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Kirim Ulang Polis Retur", spaj, 0);
				uwDao.saveMstTransHistory(spaj, "tgl_kirim_ulang_polis_retur", df.parse(bdate), null, null);
				uwDao.updateMstPolicyNoResi(spaj, s_noresi);
				hasil=true;
			}
		}
		
		return hasil;
	}

	public ArrayList selectReportPolisDataRetur(String bdate, String edate,String proses) {
		
		return Common.serializableList(uwDao.selectReportPolisDataRetur(bdate,edate,proses));
	}
	
	public String prosesPrint(String spaj,String cabang,String ipAddress,String printerName) throws IOException, Exception{
		String pesan ="";
		
		String allowPrint = this.bacDao.getAllowPrint(printerName);
		
		if(Print.checkPrinterName(printerName)){
			if(Print.testConnection(ipAddress)){
				String path = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+"PolisAll.pdf";
				File f = new File(path);
					if((f.exists() && !f.isDirectory())) { 
					pesan = "Polis berhasil di print";
				}else pesan = "Polis gagal di print";
			}else pesan = "Printer tidak bisa diakses,lakukan cetak melalui print polis";
		}else pesan = "Printer tidak tersedia / tidak support untuk melakukan proses ini";
	
		if (allowPrint.equals("0")){
			pesan = "";
		}
		
		return pesan;
		
	}
	
	public void insertPrintHistory(String spaj,String description,Integer Count,String LusId) {
		uwDao.insertMstPrintHistory(spaj, description, Count, LusId);
	}
	
	public List select_report_upload_scan(Map params) {
		return bacDao.select_report_upload_scan(params);
	}

	public List selectReportUwCollection(Map params) {		
		return uwDao.selectReportUwCollection(params);
	}
	
	public HashMap<String, Object> selectPropertiesPrinter(){
		return this.bacDao.selectPropertiesPrinter();
	}
	
	public Map selectIsPSNSyariah(String spaj){
		return bacDao.selectIsPSNSyariah(spaj);
	}
	
	public  String getAllowPrint (String printerName){
		return this.bacDao.getAllowPrint(printerName);
	}
	
	public String prosesAutopayment(TopUp topup, int flag, User currentUser, ElionsManager elionsManager2, UwManager uwManager2, Integer i_flagCC, ArrayList billInfo) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String status = "BERHASIL";
		BindException error = new BindException(new HashMap(), "cmd");
		try{
			ArrayList billInfoForTransfer = Common.serializableList( this.uwDao.selectBillingInfoForTransfer(topup.getReg_spaj(), 1, 1));
			if(topup.getMspa_payment_id() == null) topup.setMspa_payment_id("");	
			String hasilsavePayment = this.uwDao.savePayment(topup, flag, currentUser,i_flagCC);			
			this.bacDao.insertMst_position_no_spaj_pb(topup.getReg_spaj(),currentUser.getLus_id(), 4, 1, "AUTO PROSES PAYMENT", 1);		
			boolean hasil = this.transferPolis.transferToPrintPolis(topup.getReg_spaj(), error, 0, billInfoForTransfer, currentUser, "1", "3", "payment", elionsManager2, uwManager2);			
			if(!hasil){
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				status = "GAGAL PROSES";
			}
		}catch (Exception e) {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			status = "GAGAL PROSES";
			return status;
		}
		return status;
	}
	
	/**@Fungsi:	Proses email hasil auto payment
	 * @param 	List Spaj ,flag(default 0)
	 * @author 	Lufi
	 * */
	public void prosesEmailAutoPayment(ArrayList dataSpajAutoPayment, Integer flag) {
		StringBuffer pesan = new StringBuffer();
		String subject = "Laporan Hasil Proses Auto Payment";		
		if(dataSpajAutoPayment!=null){			
			pesan.append("<html><body>");
			pesan.append("Dear Team,<br><br>");
			pesan.append("Berikut hasil proses auto payment per tanggal:  "+ commonDao.selectSysdate()+"<br><br>");
			pesan.append("<table cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1000px'>");
			pesan.append("<tr bgcolor='#A8B8EE'><th>SPAJ</th>");			
			pesan.append("<th>NO.CC</th>");			
			pesan.append("<th>NAMA_PLAN</th>");
			pesan.append("<th>SITE</th>");
			pesan.append("<th>PEMEGANG</th>");
			pesan.append("<th>STATUS</th>");
			pesan.append("<th>SISA BAYAR</th></tr>");
			for(int i=0;i<dataSpajAutoPayment.size();i++){
				HashMap mapData=(HashMap)dataSpajAutoPayment.get(i);
				//Table
				pesan.append("<tr><td nowrap>"+mapData.get("SPAJ")+ "</td>");
				pesan.append("<td nowrap>"+ mapData.get("NO_KARTU_KREDIT") + "</td>");
				pesan.append("<td nowrap>"+ mapData.get("NAMA_PLAN") + "</td>");
				pesan.append("<td nowrap>"+ mapData.get("REGION") + "</td>");
				pesan.append("<td nowrap>"+ mapData.get("PEMEGANG") + "</td>");
				pesan.append("<td nowrap>"+mapData.get("STATUS") + "</td>");				
				pesan.append("<td>"+ mapData.get("REMAIN") + "</td></tr>");
				
			}
			pesan.append("</table></body></html>");
			EmailPool.send("E-Lions", 1, 1, 0, 0, 
					null, 0, 0, commonDao.selectSysdate(), null, 
					true,
					"ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"alif_bam@sinarmasmsiglife.co.id"}, 
					new String[]{"rizkiyano@sinarmasmsiglife.co.id"}, 
					null, 
					subject, 
					pesan.toString(), 
					null, null);
		}		
	}

	public void schedulerNeonBox() throws ParseException {
		String msh_name = "AUTOMAIL PENGECEKAN NEON BOX";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerNeonBox(msh_name);
		}
	}
	
	public HashMap<String, Object> selectFilterSpajLockIdInbox(String posisi, String tipe, String kata, String pilter, String lssaId,String lsspId, String tgl_lahir) {
		return this.uwDao.selectFilterSpajLockIdInbox(posisi, tipe, kata, pilter,lssaId,lsspId, tgl_lahir);
	}
	
	public void updateMstInboxLockId(String lus_id, String mi_id){	
		uwDao.updateMstInboxLockId(lus_id, mi_id);
	}

	public int selectLineBusLstBisnis(String lsbs_id) {
		
		return bacDao.selectLineBusLstBisnis(lsbs_id);
	}

	public Integer selectno_virtual_Exist(String no_virtual_acc) {		
		return bacDao.selectno_virtual_Exist(no_virtual_acc);
	}
	
	public HashMap selectMstConfig(Integer app_id, String section, String sub_section){
		return Common.serializableMap(uwDao.selectMstConfig(app_id, section,sub_section));
	}

	public List selectDataSmilePrioritas(String bdate, String edate, String jn_report, int flag) {		
		return basDao.selectDataSmilePrioritas(bdate,edate,jn_report,flag);
	}
	
	public void insertLstUlangan3(String reg_spaj, int addSecond, String jenis, Integer lus_id, String keterangan){
		uwDao.insertLstUlangan3(reg_spaj, addSecond, jenis, lus_id, keterangan);
	}
	
	public List reportError(String bdate, String edate){
		return basDao.reportError(bdate, edate);
	}
	
	public void schedulerFRkeCabang() throws ParseException {
		String msh_name = "AUTOMAIL FURTHER REQUIREMENT KE CABANG H+1";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerFRkeCabang(msh_name);
		}
	}
	
	public PembayarPremi selectP_premi(String spaj) {
		return (PembayarPremi) this.bacDao.selectP_premi(spaj);
	}
	
	public void schedulerNotProceedWith(){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String msh_name = "SCHEDULER PROSES NOT PROCEED WITH";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerNotProceedWith(msh_name);
		}	
	}
	
	public String updateDataPolicy(User currentUser, Upload upload, String s_jenisEdit) throws IOException {
		
		InputStreamReader is = null;
		BufferedReader br = null;
		try {
//			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			is = new InputStreamReader(upload.getFile1().getInputStream());		
			br = new BufferedReader(is);
			String line = br.readLine();
	        while(line != null) {
	        	 String f[] = line.split("\t");
	        	       String s_nopol = f[0];
	        	       String s_update = f[1];     	       
      	       	
	             if(!s_nopol.equals("nopol")) {		            	 
	        	       commonDao.updateDataPolicy(s_nopol.trim(),s_update.trim(),s_jenisEdit);
	        	       //start-->  igaukiarwan 03-02-2020 req update filling untuk Bancass dan DMTM
	        	       HashMap mapLcaFilling = selectMstConfig(16, "RDS", "RDS_UPDATE_FILLING");
	        	       String[] list_filling = mapLcaFilling.get("NAME")!=null?mapLcaFilling.get("NAME").toString().split(","):null;
	        	       
	        	       if (s_update != null){
	        	       String polis = commonDao.selectMspoPolicyNo(s_nopol.trim());
	        	       String duaDigit = StringUtils.substring(polis, 0, 2);
	        	       
	        	       boolean bt = false;
	       				for(String s: list_filling){
	       				if(s.equals(duaDigit))
	       					bt=true;
	       				}
	  	             
	       				if (s_nopol.trim().equals(polis) && bt){
	  	            	commonDao.updateLspdIdWhereNopol(polis);
	       				}
	        	       }
	        	       //<--end igaukiarwan 03-02-2020 req update filling untuk Bancass dan DMTM
	             }

	             line = br.readLine();
	        }
	        if(br!=null){
	        	br.close();
	        }			
		}catch( Exception e) { 
	         //logger.error("ERROR :", e);
			 String tes=e.getLocalizedMessage();
	         throw new RuntimeException(tes);
	    }
		finally{
			if(br!=null){
				br.close();
			}
			if(is!=null){
				is.close();
			}
		}				
        return "Berhasil Update Data ";
	}	
	
	public String upload_sms_polis_retur(User currentUser, Upload upload) throws IOException{ //helpdesk [149354] Project SMS Polis Retur
		String result = "Proses Upload SMS Polis Retur Berhasil.", 
				kumpul_error = "",  
				file_type = "",
				no_polis = "", 
				alasan = "";
		InputStreamReader ISReader = null;
		BufferedReader BFReader = null;
		
		try{
			file_type = upload.getFile1().getContentType();
			if(file_type.equalsIgnoreCase("text/plain")){ //.txt
				ISReader = new InputStreamReader(upload.getFile1().getInputStream());		
				BFReader = new BufferedReader(ISReader);
				
				String txt_line = BFReader.readLine();
				boolean is_first_line = true;
				
				while(txt_line != null){
					if(is_first_line){
						is_first_line = false;
						txt_line = BFReader.readLine();
						continue;
					}
					
					String data[] = txt_line.split("\t");
					no_polis = data[0];
				    alasan = data[1];
									
					kumpul_error += bacDao.prosesDataPolisRetur(currentUser, no_polis, alasan);
					
					txt_line = BFReader.readLine();
				}
			}else if(file_type.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || //excel .xlsx
					 file_type.equalsIgnoreCase("application/vnd.ms-excel")){											//excel .xls
				File local_file = new File(String.format("%s/%s", props.getProperty("pdf.dir.export"), upload.getFile1().getOriginalFilename()));
				FileCopyUtils.copy(upload.getFile1().getBytes(), local_file);
				
				ExcelRead excelRead = new ExcelRead();
				ArrayList<List> excel_data = Common.serializableList(excelRead.readAllExcelFile(local_file));
				
				if(excel_data.size() > 0){
					for(int i = 1; i < excel_data.size(); i++){
						no_polis = excel_data.get(i).get(0).toString();
						alasan = excel_data.get(i).get(1).toString();
						
						kumpul_error += bacDao.prosesDataPolisRetur(currentUser, no_polis, alasan);
					}
				}else{
					kumpul_error += "\\nERROR : [tidak ada data pada excel]";
				}
				
				if(local_file.exists()){
					local_file.delete();
				}
			}else{
				result = "Harap gunakan file excel/text";
			}

			if(kumpul_error.length() > 0){
				result = String.format("Proses Upload SMS Polis Retur Berhasil dengan error berikut :%s", kumpul_error);
			}
		}catch(Exception ex) { 
	         result = ex.getLocalizedMessage();
	    }finally{
			if(BFReader != null) BFReader.close();
			if(ISReader != null) ISReader.close();
		}
		
		return result;
	}
	
	public List select_nama_perusahaan_by_filter(String kode){
		return this.worksiteDao.select_nama_perusahaan_by_filter(kode);
	}
	
	public List<Map> selectLspin(String productCode) {
		return uwDao.selectLspin(productCode);
	}
	
	public void updateTmProduct(BigDecimal nomor,BigDecimal nomor2,String product_code) {
		Map param=new HashMap<String, Object>();
		param.put("nomor", nomor);
		param.put("nomor2", nomor2);
		param.put("product_code", product_code);
		uwDao.updateTmProduct(param);	
	}
	
	public void insertTmSales(TmSales tmSales) {
		Map param=new HashMap<String, Object>();
		param.put("campaign", tmSales.getCampaign().trim());
		param.put("no_sertifikat", tmSales.getNo_sertifikat().trim());
		param.put("card_type", tmSales.getCard_type().trim());
		param.put("card_no", tmSales.getCard_no().trim());
		param.put("cust_no", tmSales.getCust_no().trim());
		param.put("holder_name", tmSales.getHolder_name().trim());
		param.put("sex", tmSales.getSex());
		param.put("bod_holder", tmSales.getBod_holder());
		param.put("age", tmSales.getAge());
		param.put("mobile_no", tmSales.getMobile_no());
		param.put("product_code", tmSales.getProduct_code().trim());
		param.put("premium", tmSales.getPremium());
		param.put("sum_insured", tmSales.getSum_insured());
		param.put("beg_date", tmSales.getBeg_date());
		param.put("end_date", tmSales.getEnd_date());
		param.put("bill_mode", tmSales.getBill_mode());
		param.put("bill_freq", tmSales.getBill_freq());
		param.put("flag_cek", tmSales.getFlag_cek());
		param.put("posisi", tmSales.getPosisi());
		param.put("status", tmSales.getStatus());
		param.put("application_id", tmSales.getApplication_id().trim());
		param.put("email", tmSales.getEmail());
		param.put("no_akun", tmSales.getNo_akun());
		param.put("tipe_akun", tmSales.getTipe_akun());
		param.put("id_customer", tmSales.getId_customer());
		param.put("tgl_buka_akun", tmSales.getTgl_buka_akun());
        param.put("sts_nikah", tmSales.getSts_nikah());
        param.put("kd_cabang", tmSales.getKd_cabang());
        param.put("pekerjaan", tmSales.getPekerjaan());
        param.put("tempat_lahir", tmSales.getTempat_lahir());
        param.put("address1", tmSales.getAddress1());
        param.put("address2", tmSales.getAddress2());
        param.put("home_phone", tmSales.getHome_phone());
        param.put("work_phone", tmSales.getWork_phone());
        
		uwDao.insertTmSales(param);	
	}	
		
	public void  insertMstUrlSecure(String id1, String id2, String key_id, String link, String idEncrypt1, String idEncrypt2) throws SQLException{
		Map param=new HashMap<String, Object>();
		param.put("id_secure_1", id1);
		param.put("id_secure_2", id2);
		param.put("id_encrypt_1", idEncrypt1);
		param.put("id_encrypt_2", idEncrypt2);
		param.put("key_id", key_id);
		param.put("link", link);
		uwDao.insertMstUrlSecure(param);	
	}
	
	public PembayarPremi selectPembayarPremi(String spaj) {
		return (PembayarPremi) this.bacDao.selectPembayarPremi(spaj);
	}
	
	public Integer selectJenisPemegangPolis(String reg_spaj){
		return bacDao.selectPemegangPolis(reg_spaj);
	}
	
	public String selectKeteranganKerja(String idKerja){
		return bacDao.selectKeteranganKerja(idKerja);
	}
	
	public HashMap<String, Object> selectJenisPihakKetiga(String spaj){
		return this.bacDao.selectJenisPihakKetiga(spaj);
	}
	
	public String selectIdLstPekerjaan(String lsp_name) {
		return bacDao.selectIdLstPekerjaan(lsp_name);
	}
	
	public List<Map> selectSpajPrint() throws DataAccessException {
		return bacDao.selectSpajPrint();
	}
	
	public Integer selectAlreadyExistScheduler(String msh_name){
		return schedulerDao.selectAlreadyExistScheduler(msh_name);
	}
	
	public void insertMstSchedulerHist(String machine, String name, Date bdate, Date edate, String desc, String full_desc) throws DataAccessException {
		schedulerDao.insertMstSchedulerHist(machine, name, bdate, edate, desc, full_desc);
	}

	public List selectHistorySpeedy(String s_spaj) {
		return uwDao.selectHistorySpeedy(s_spaj);
	}

	public String selectEmailAdminInputter(String spaj) {
		return bacDao.selectEmailAdminInputter(spaj);
	}

	//canpri, select medis dari mst_position_spaj untuk e-mail further yg dikirim ke uw helpdesk
	public String selectMedisDescNew(String s_spaj) {
		return uwDao.selectMedisDescNew(s_spaj);
	}
	
	public Map selectMstAgent(String no_blanko, String msag_id, Integer flag) {
		return (HashMap) basDao.selectMstAgent(no_blanko, msag_id, flag);
	}
	
	public List<Pas> selectAllUploadPasList( Map param ){
		return bacDao.selectAllUploadPasList(param);
	}
	
	public List<TmSales> selectAllUploadPasFreeList( Map param ){
		return bacDao.selectAllUploadPasFreeList(param);
	}
	
	public String selectTotalAllUploadPasList(Map params){
		return bacDao.selectTotalAllUploadPasList( params);
	}
	
	public String selectTotalAllUploadPasFreeList(Map params){
		return bacDao.selectTotalAllUploadPasFreeList( params);
	}
	
	public void prosesSchedulerPrint(String spaj){
		
		Map map = uwDao.validationPrintPolis(spaj); 
		if(map.get("ORI")!=null) {
			//throw new RuntimeException("Maaf, tetapi Polis ini sudah pernah dicetak");
		}else {
			uwDao.insertMstPositionSpaj("1", "SCHEDULE PRINT E-LIONS", spaj, 0);
			uwDao.updateMst_policyPrintDate(spaj, "mspo_date_print");
			uwDao.saveMstTransHistory(spaj, "tgl_print_polis", null, null, null);
		}
		
		String lca_id = uwDao.selectCabangFromSpaj(spaj);
		
		if(!lca_id.equals("01") && !lca_id.equals("58")) {
			Map info = this.uwDao.selectInformasiEmailSoftcopy(spaj);
			String nama = (String) info.get("MCL_FIRST");
			String no = (String) info.get("MSPO_POLICY_NO_FORMAT");
			String cab = (String) info.get("LCA_NAMA");
			if(nama != null && no != null && cab != null) {
				try {
					
					String alamat = this.uwDao.selectEmailCabangFromSpaj(spaj);
					if(alamat == null) alamat = props.getProperty("admin.ajsjava");
					
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
	
	public Map prosesCetakManfaat(String spaj, String lus_id){
		try {
			return this.manfaat.prosesCetakManfaat(spaj, lus_id);
		} catch (Exception e) {
			logger.error("ERROR :", e);
			return null;
		}
	}
	
	public void schedulerPendingSmilePrioritas() throws IOException, JRException {
		String msh_name ="SCHEDULER PENDING SMILE PRIORITAS";
		if(selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerPendingPrioritas(msh_name);
		}
		
	}
	
	public ArrayList selectReportCoverLetterJne(String spaj, String dist) {
		return Common.serializableList(uwDao.selectReportCoverLetterJne(spaj, dist));
	}
	
	/**
	 * Proses Save Cover Letter
	 * @author 	MANTA
	 */
	public String prosesCoverLetter(CmdCoverLetter command, User currentUser){
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date sysdate = selectSysdate();
		try{
			//Update dan insert data 
			for(int i=0;i<command.getDatapolis().size();i++){
				if(command.getDatapolis().get(i).getCek() == 1){
					String spaj = command.getDatapolis().get(i).getReg_spaj();
					uwDao.updateCoverLetter(command.getDatapolis().get(i).getNo_polis(), command.getStatus_polis().toString());
					saveMstTransHistory(spaj, command.getDatapolis().get(i).getTranshistory(), null, null, null);
					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), command.getDatapolis().get(i).getKeterangan(), spaj, 0);
				}
			}
			
			/*
			 * Poin 1
			 * Set cabang yang akan dikirimkan email
			 */
			Boolean isDuplikat = false;
			Boolean isDuplikat2 = false;
			Integer index = 0;
			Integer[] idcabang = new Integer[command.getCek_polis().length];
			idcabang[index] = command.getDatapolis().get(0).getCabanginput();
			
			for(int i=0;i<command.getCek_polis().length;i++){
				Integer tempcabanginput = command.getDatapolis().get(i).getCabanginput();
				
				for(int j=0;j<command.getCek_polis().length;j++){
					isDuplikat = false;
					isDuplikat2 = false;
					if(i != j && tempcabanginput == command.getDatapolis().get(j).getCabanginput()){
						isDuplikat = true;
					}
					if(!isDuplikat){
						for(int k=0;k<index+1;k++){
							if(idcabang[k].equals(command.getDatapolis().get(j).getCabanginput())){
								isDuplikat2 = true;
							}
						}
						//Jika larid cabang blm pernah disimpan dalam array idcabang,
						//maka larid tersebut ditambahkan ke dalam array idcabang
						if(!isDuplikat2){
							index = index + 1;
							idcabang[index] = command.getDatapolis().get(j).getCabanginput();
						}
					}
				}
			}
			/*
			 * Poin 1 END
			 */
			
			/*
			 * Poin 2
			 * Proses pengiriman email
			 */
			HashMap datasurat = new HashMap();
			HashMap datacabang = new HashMap();
			
			Integer stpolis = command.getStatus_polis();
			if(stpolis == 0 || stpolis == 2){
				for(int i=0;i<index+1;i++){
					datacabang = Common.serializableMap(uwDao.selectAddrRegion(idcabang[i].toString()));

					StringBuffer pesan = new StringBuffer();
					pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
					if(stpolis == 0){
						pesan.append("<body>Pada tanggal <b>" + df.format(sysdate) + "</b> telah dikirimkan polis-polis tersebut kepada Dept. GA, ");
						pesan.append("untuk dapat dikirimkan ke Cabang <b>" + datacabang.get("CABANG") + "</b> dengan alamat <b>" + datacabang.get("ALAMAT") + "</b>.<br><br>");
					}else if(stpolis == 2){
						pesan.append("<body>Pada tanggal <b>" + df.format(sysdate) + "</b> telah dikirimkan polis-polis tersebut kepada ");
						pesan.append("Cabang <b>" + datacabang.get("CABANG") + "</b>dengan alamat <b>" + datacabang.get("ALAMAT") + "</b>.<br><br>");
					}
					pesan.append("<table cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1000px'>");
					pesan.append("<tr bgcolor='#A8B8EE'><th>No</th>");
					pesan.append("<th>No. Polis</th>");
					pesan.append("<th>Pemegang Polis</th>");
					pesan.append("<th>Tertanggung</th>");
					pesan.append("<th>Admin Input</th>");
					pesan.append("<th>Produk</th></th>");
					pesan.append("<th>Tanggal Cetak</th>");
					pesan.append("<th>SimasCard</th>");
					pesan.append("<th>Kartu Admedika</th>");
					if(stpolis == 2) pesan.append("<th>No. Ekspedisi</th></tr>");
					
					Integer number = 0;
					String lastuser_email = "";
					for(int j=0; j<command.getDatapolis().size();j++){
						CoverLetter dpolis = command.getDatapolis().get(j);
						if(dpolis.getCek() == 1){
							if(dpolis.getCabanginput().equals(idcabang[i])){
								
								String lastuser = commonDao.selectLusCoverLetterPositionSpaj(dpolis.getReg_spaj());
								if(lastuser!=null) lastuser_email = lastuser_email + commonDao.selectEmailUser(lastuser) + ";";
								
								number = number + 1;
								if(number % 2 == 0){
									pesan.append("<tr bgcolor='#E0EEFE'><td align='center' nowrap>"+ number + "</td>");
								}
								else{
									pesan.append("<tr bgcolor='#BAD8FB'><td align='center' nowrap>"+ number + "</td>");
								}
								pesan.append("<td nowrap>"+ dpolis.getNo_polis() + "</td>");
								pesan.append("<td>"+ dpolis.getPemegangpolis() + "</td>");
								pesan.append("<td>"+ dpolis.getTertanggung() + "</td>");
								pesan.append("<td nowrap>"+ dpolis.getAdmininput() + "</td>");
								pesan.append("<td nowrap>"+ dpolis.getProduk() + "</td>");
								pesan.append("<td nowrap>"+ dpolis.getTgl_printpolis() + "</td>");
								pesan.append("<td nowrap>"+ dpolis.getSimascard() + "</td>");
								pesan.append("<td nowrap>"+ dpolis.getAdmedika() + "</td></tr>");
							}
						}
					}
					pesan.append("</table> <br><br>");
					pesan.append("Pengiriman polis akan memerlukan waktu sekitar 2-3 hari kerja.<br>");
					pesan.append("Apabila setelah melewati waktu tersebut polis tidak diterima, ");
					if(stpolis == 0){
						pesan.append("harap kirimkan email ke Dept. Underwriting - UP <b>" + currentUser.getLus_full_name() + "</b> (<u>" + currentUser.getEmail() + "</u>) dan Dept. GA - UP <b>Erik</b>/<b>Ito</b> (<u>GA-ekspedisi@sinarmasmsiglife.co.id</u>).<br><br><br>");
						pesan.append("Tgl Proses\t: " + df.format(sysdate) + "<br>");
						pesan.append("User\t\t: " + currentUser.getLus_full_name() + "<br>");
						pesan.append("Email\t\t: " + currentUser.getEmail() + "<br>");
						pesan.append("Dept. Underwriting");
					}else if(stpolis == 2){
						pesan.append("harap kirimkan email ke Dept. GA - UP <b>Erik</b>/<b>Ito</b> (<u>GA-ekspedisi@sinarmasmsiglife.co.id</u>).<br><br><br>");
						pesan.append("Tgl Proses\t: " + df.format(sysdate) + "<br>");
						pesan.append("User\t\t: " + currentUser.getLus_full_name() + "<br>");
						pesan.append("Email\t\t: <u>GA-ekspedisi@sinarmasmsiglife.co.id</u><br>");
						pesan.append("Dept. GA");
					}
					
					String subject = "INFORMASI PENGIRIMAN POLIS INDIVIDU";
					String from = props.getProperty("admin.ajsjava");
					String emailto = "GA-ekspedisi@sinarmasmsiglife.co.id";
					String emailcc = datacabang.get("EMAIL").toString();
					String emailbcc = currentUser.getEmail();
					if(stpolis == 2){
						emailto = datacabang.get("EMAIL").toString();
						emailcc = lastuser_email;
						emailbcc = "GA-ekspedisi@sinarmasmsiglife.co.id";
					}
					
					if(!emailto.trim().equals("")){	
						String[] to = emailto.split(";");
						String[] cc = emailcc.split(";");
						String[] bcc = emailbcc.split(";");

						try {
							EmailPool.send("E-Lions", 1, 1, 0, 0, 
									null, 0, 0, sysdate, null, 
									true,
									from, 
									to, 
									cc, 
									bcc, 
									subject, 
									pesan.toString(), 
									null, null);
						}
						catch (MailException e1) {
							// TODO Auto-generated catch block
							command.setPesan("Terjadi kesalahan pada proses Pengiriman Email Cover Letter!");
							logger.error("ERROR :", e1);
						}
					}
				}
			}
			/* 
			 * Poin 2 END
			 */
			
			command.setPesan("Proses transfer polis pada sistem Cover Letter berhasil dilakukan!");
			
		}catch(Exception e){
			command.setPesan("Terjadi kesalahan pada proses Cover Letter!");
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return command.getPesan();
	}
	
//	Rahmayanti - Penggantian Simas Card
//	public List selectPenggantianSimCard(String reg_spaj, String no_kartu)
//	{
//		return simasCardDao.selectPenggantianSimCard(reg_spaj, no_kartu);
//	}	
	
	//MANTA
	public Begdate editBegDateBySystem(Begdate begdate, String lus_id){
		
		try{
			if(begdate.getPolicy().getMspo_beg_date() != null || begdate.getPolicy().getMspo_end_date() != null || begdate.getPolicy().getMspo_next_bill() != null){
				uwDao.updateMstPolicyBegDate(begdate.getPolicy());
			}
			if(begdate.getInsured().getMste_beg_date() != null || begdate.getInsured().getMste_end_date() != null){
				uwDao.updateMstInsuredBegDate(begdate.getInsured());
			}
			if(begdate.getLsprodins().size()>0){
				for(int i=0;i<begdate.getLsprodins().size();i++){
					if(begdate.getLsprodins().get(i).getMspr_beg_date() != null || begdate.getLsprodins().get(i).getMspr_end_date() != null || begdate.getLsprodins().get(i).getMspr_end_pay() != null){
						uwDao.updateMstProdInsBegDate(begdate.getLsprodins().get(i));
					}
				}
			}
			if(begdate.getLsprod().size()>0){
				for(int i=0;i<begdate.getLsprod().size();i++){
					if(begdate.getLsprod().get(i).getMspro_beg_date() != null || begdate.getLsprod().get(i).getMspro_end_date() != null){
						uwDao.updateMstProductionBegDate(begdate.getLsprod().get(i));
					}
				}
			}
			if(begdate.getLsbilling().size()>0){
				for(int i=0;i<begdate.getLsbilling().size();i++){
					if(begdate.getLsbilling().get(i).getMsbi_beg_date() != null || begdate.getLsbilling().get(i).getMsbi_end_date() != null || begdate.getLsbilling().get(i).getMsbi_due_date() != null){
						uwDao.updateMstBillingBegDate(begdate.getLsbilling().get(i));
					}
				}
			}
			if(begdate.getMsp_pas_beg_date() != null || begdate.getMsp_pas_end_date() != null){
				uwDao.updateMstPasSMSBegDate(begdate.getReg_spaj(), begdate.getMsp_pas_beg_date(), begdate.getMsp_pas_end_date());
			}
			
			uwDao.updateMstPesertaBegDate(begdate.getReg_spaj(), begdate.getInsured().getMste_beg_date());
			
			if(begdate.getLsulink().size()>0){
				for(int i=0;i<begdate.getLsulink().size();i++){
					if(begdate.getLsulink().get(i).getMu_tgl_surat() != null){
						uwDao.updateMstUlinkBegDate(begdate.getLsulink().get(i));
					}
				}
			}
			if(begdate.getLsdetulink().size()>0){
				for(int i=0;i<begdate.getLsdetulink().size();i++){
					if(begdate.getLsdetulink().get(i).getMdu_last_trans() != null){
						uwDao.updateMstDetUlinkBegDate(begdate.getLsdetulink().get(i));
					}
				}
			}
			if(begdate.getLsbiayaulink().size()>0){
				for(int i=0;i<begdate.getLsbiayaulink().size();i++){
					if(begdate.getLsbiayaulink().get(i).getMbu_end_pay() != null){
						uwDao.updateMstBiayaUlinkBegDate(begdate.getLsbiayaulink().get(i));
					}
				}
			}
			if(begdate.getLsulinkbill().size()>0){
				for(int i=0;i<begdate.getLsulinkbill().size();i++){
					if(begdate.getLsulinkbill().get(i).getNext_bill() != null){
						uwDao.updateMstUlinkBillBegDate(begdate.getLsulinkbill().get(i));
					}
				}
			}
			
			Ulangan ulangan = new Ulangan();
			ulangan.setReg_spaj(begdate.getReg_spaj());
			ulangan.setJenis("EDIT BEG DATE [BY SYSTEM]");
			ulangan.setStatus_polis(null);
			ulangan.setLus_id(Integer.parseInt(lus_id));
			ulangan.setKeterangan("REQ : "+ begdate.getKet_bdate().toUpperCase() + " EDIT BEGDATE DARI " + begdate.getOld_bdate() + " MENJADI " + begdate.getNew_bdate());
			ulangan.setId_ticket(begdate.getNo_helpdesk());
			uwDao.insertLstUlangan(ulangan);
			
			begdate.setOld_bdate(begdate.getNew_bdate());
			begdate.setKet_bdate("");
			begdate.setNo_helpdesk("");
			begdate.setPesan("Proses edit begdate berhasil!");
			
		}catch (Exception e) {
			begdate.setPesan("Proses edit begdate gagal!");
			logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return begdate;
	}
	
	public ArrayList selectReportFollowUpCsfCall(String tanggalAwal, String tanggalAkhir) {
		return Common.serializableList(uwDao.selectReportFollowUpCsfCall(tanggalAwal, tanggalAkhir));
	}
	
	public String prosesFollowUpCsfCall(User currentUser, String spaj, String chbox1, String chbox2, String chbox3, String chboxAdmin, String keterangan, String flag){
		String desc = "";
		String css = props.getProperty("email.uw.css.satu") + props.getProperty("email.uw.css.dua");
		String footer = props.getProperty("email.uw.footer");
		String ket_followup = "";
		try{
			if(flag.equals("0")){
				HashMap dataPp = (HashMap) uwDao.selectPemegang(spaj);
				HashMap dataTt = (HashMap) uwDao.selectTertanggung(spaj);
				HashMap info = (HashMap) uwDao.selectInformasiCabangFromSpaj(spaj);
				HashMap gen = (HashMap) uwDao.selectEmailAgen(spaj);
				List listProduk = akseptasiDao.selectMstProductInsured(spaj);
				String pp = (String) dataPp.get("MCL_FIRST");
				String lcaId = (String) dataPp.get("LCA_ID");
				String tt = (String) dataTt.get("MCL_FIRST");
				String cabang = (String) info.get("NAMA_CABANG");
				String namaAgen = (String) gen.get("MCL_FIRST");
				String emailAgen = (String) gen.get("MSPE_EMAIL");
				String emailAdmin = uwDao.selectEmailAdmin(spaj);
				if(chboxAdmin.equals("")) emailAdmin = "";
				Product produk = (Product) listProduk.get(0);
				boolean cc_banc = false;
				String lsdbs_3digit = FormatString.rpad("0", produk.getLsdbs_number().toString(), 3);

				String subject = "INFO GAGAL VALIDASI SPAJ NO." + FormatString.nomorPolis(spaj) + " a/n " + pp;
				String pesan = "";
				pesan = css + 
						"<table width=100% class=satu>"
						+ "<tr><td colspan=3>Terima kasih atas update kelengkapan dokumen yang sudah disampaikan. Namun berdasarkan informasi further requirement yang diberikan oleh New Business masih terdapat kekurangan untuk :</td></tr>"
						+ "<tr><td></td></tr>"
						+ "<tr><td>Cabang   	  </td><td>:</td><td>" + cabang + "</td></tr>"
						+ "<tr><td>Agen   		  </td><td>:</td><td>" + namaAgen + "</td></tr>"
						+ "<tr><td>No. SPAJ 	  </td><td>:</td><td>" + FormatString.nomorPolis(spaj) + "</td></tr>"
						+ "<tr><td>Produk		  </td><td>:</td><td>" + produk.getLsdbs_name() + "("+produk.getLsbs_id() + ")" + "</td></tr>"
						+ "<tr><td>Pemegang Polis </td><td>:</td><td>" + pp + "</td></tr>"
						+ "<tr><td>Tertanggung	  </td><td>:</td><td>" + tt + "</td></tr>"
						+ "<tr><td></td></tr>"
						+ "<tr><td colspan=3>Kami informasikan bahwa Nasabah tidak berhasil kami hubungi untuk validasi :</td></tr>"
						+ (!chbox1.equals("")? "<tr><td colspan=3>- " + chbox1 + "</td></tr>" : "")
						+ (!chbox2.equals("")? "<tr><td colspan=3>- " + chbox2 + "</td></tr>" : "")
						+ (!chbox3.equals("")? "<tr><td colspan=3>- " + chbox3 + "</td></tr>" : "")
						+ "<tr><td></td></tr>"
						+ "<tr><td>Info	  		  </td><td>:</td><td>" + keterangan + "</td></tr>"
						+ "<tr><td></td></tr>"
						+"</table>";

				String check[] = new String[]{chbox1, chbox2, chbox3};

				for(int i=0;i<check.length;i++){
					if(!check[i].equals("")) ket_followup = ket_followup + check[i] + ",";
				}

				ket_followup = "(" + ket_followup.substring(0,ket_followup.length()-1) + ")";

				Map regionMap = selectRegion(spaj);
				String lwk_id = (String) regionMap.get("LWK_ID");
				String lsrg_id = (String) regionMap.get("LSRG_ID");
				String kode = akseptasiDao.selectMstProductInsured3(spaj);
				String further;
				Integer i_jenisbank = bacDao.selectIsInputanBank(spaj);
				HashMap mapEmail = new HashMap();

				if(kode.equals("155")){//Permintaan Ariani
					further = props.getProperty("bancassuance.further_requirementspecial"); 
				}else if(kode.equals("120") || kode.equals("134")){//Permintaan Ariani
					if(produk.getLsbs_id()==120 && "019,020,021".indexOf(lsdbs_3digit)>-1){//Andhika (23/05/2013)
						further = props.getProperty("bancassuance.further_requirement_cerdas_sekuritas");
					}else{
						further = props.getProperty("bancassuance.further_requirement_cerdas_platimum");
					}
				}else if(kode.equals("166")){
					further = props.getProperty("bancassuance.further_requirement_amanah");
				}else if(produk.getLsbs_id()==190 && "005,006".indexOf(lsdbs_3digit)>=0){
					mapEmail=Common.serializableMap(uwDao.selectMstConfig(6, "sendAgenNAdmin","bancass.vipplan"));
					String toBancass = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					String emailAo = uwDao.selectEmailAoBankSinarmas(spaj);
					if(emailAo!=null)toBancass+=emailAo+";";
					//String[] emailTo = toBancass.split(";");
					further = toBancass;
				}else{
					further = props.getProperty("bancassuance.further_requirement");
				}

				if(i_jenisbank==2) {
					further = props.getProperty("bancassuance.further_requirement_simasprima");
					// Andhika - request Sari (44415) - danamas prima
					if(produk.getLsbs_id()==163 && "006,007,008,009,010".indexOf(lsdbs_3digit)>-1){
						further = props.getProperty("bancassuance.further_requirement_danasejahtera");
					}
					//Request Hadi (No 41067) - SIMPOL kirim ke Hisar Janwar, Iriana Wijayanti, Shima, Edy Kohar
					if (kode.equals("120") && "019,020,021".indexOf(lsdbs_3digit)<1) further = "Iriana@sinarmasmsiglife.co.id";

					//Email cabang Bank Sinarmas bersangkutan + email ao nya bila ada
					//Andhika (16/12/2013) - update emailAoHrd, untuk ambil email agent dengan joint ke table hrd_mst
					String emailCabangBank = uwDao.selectEmailCabangBankSinarmas(spaj);
					String emailAoBank = uwDao.selectEmailAoBankSinarmas(spaj);
					Map Aohrd = null;//uwDao.selectEmailAoHrd(spaj);
					Map emailBAC = uwDao.selectReffNonLisensi(spaj); 
					if (Aohrd != null){
						String emailAoHrd = (String) Aohrd.get("EMAIL");
						if(emailAoHrd != null) further = further.concat(";" + emailAoHrd);
					}
					if(emailCabangBank != null) further = further.concat(";" + emailCabangBank);
					//if(emailAoBank != null) further = further.concat(";" + emailAoBank);
					if(further.startsWith(";")) further = further.substring(1);
					if(emailBAC !=null){
						String emailBacAjs = (String) emailBAC.get("EMAIL_AJS");
						if(emailBacAjs != null) further = further.concat(";" + emailBacAjs);
					}
					
					//Ridhaal (15/8/2016) Request Sari Sutini (No 92747) - Edit user2 penerima email gagal validasi, extra premi, postponed, dan decline produk Simpol (120) dan Simpol Syariah (220) sesuai dengan data mapping bancass yang terdapat pada sistem distribution support.
			    	if ((kode.equals("120") && "010,011,012,022,023,024".indexOf(lsdbs_3digit)>-1 ) ||
			    			(kode.equals("202") && "004,005,006".indexOf(lsdbs_3digit)>-1 ) ||
			    			(kode.equals("213") && "001".indexOf(lsdbs_3digit)>-1 ) ||
			    			(kode.equals("216") && "001".indexOf(lsdbs_3digit)>-1 ) ||
			    			(kode.equals("134") && "005,010".indexOf(lsdbs_3digit)>-1 ) ||
			    			(kode.equals("215") && "001".indexOf(lsdbs_3digit)>-1 )){
			    		Map emailBCAM = uwDao.selectEmailBCAM(spaj);
			    		if(emailBCAM !=null){
				    		String emailBC = (String) emailBCAM.get("AO_EMAIL");
				    		String emailAM = (String) emailBCAM.get("LEADER_EMAIL");
				    		
				    		if(emailBC != null) further = further.concat(";" + emailBC);
				    		if(emailAM != null) further = further.concat(";" + emailAM);
				    		cc_banc = true;

				    	}
			    	}

				}else if(i_jenisbank==3) {
					if(produk.getLsbs_id()==120 && "019,020,021".indexOf(lsdbs_3digit)>-1){// Andhika (23/05/2013)
						further = props.getProperty("bancassuance.further_requirement_cerdas_sekuritas");
					}else{
						further = props.getProperty("bancassuance.further_requirement_danamasprima");
					}
				}else if(i_jenisbank==16 || (produk.getLsbs_id()==182 && "019,020,021".indexOf(lsdbs_3digit)>-1)) {
					further = props.getProperty("bancassuance.further_requirement_bsm");
				}else if(i_jenisbank==25 || (produk.getLsbs_id()==200 && "005,006".indexOf(lsdbs_3digit)>-1)) {// Ridhaal untuk bank Harda / produk superlink - Req Dewi Andriyati (10/20/2016)
		    		mapEmail=Common.serializableMap(uwDao.selectMstConfig(6, "sendAgenNAdmin","bancass.harda"));
					further = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					
					Map emailBCAM = uwDao.selectEmailBCAM(spaj);
		    		if(emailBCAM !=null){
			    		String emailBC = (String) emailBCAM.get("AO_EMAIL");
			    		String emailAM = (String) emailBCAM.get("LEADER_EMAIL");
			    		
			    		if(emailBC != null) further = further.concat(";" + emailBC);
			    		if(emailAM != null) further = further.concat(";" + emailAM);
			    	}		    
				}

				String from = "uwhelpdesk@sinarmasmsiglife.co.id";
				String to[] = null;
				String cc[] = null;
				String bancassurance[] = further.split(";");
				String AdminNBancassurance[] = (emailAdmin+";"+further).split(";");

				if(emailAdmin!=null && !emailAdmin.trim().equals("")){
					String[] emailTo = emailAdmin.split(";");
					if(lcaId.equals("09")){
						to = AdminNBancassurance;
					}else{
						to = emailTo;
					}
				}else if(lcaId.equals("09")){
					if(emailAdmin!=null){
						to = AdminNBancassurance;
					}else{
						to = bancassurance;
					}
				}else if(lcaId.equals("58")){
					String adminMall = props.getProperty("bas.email.mallassurance");
					String[] emailTo = adminMall.split(";");
					to = emailTo;
				}


				String s_DaftarEmailTo = "";

				//Jalur Dist. Agency
				if("37,39,49,52,60,73".indexOf(lcaId)>-1){

				}
				//Jalur Dist. Corporate
				else if("08,42,62,67".indexOf(lcaId)>-1){
					if(lcaId.equals("42")){
						s_DaftarEmailTo = "CorpMarketing@sinarmasmsiglife.co.id;CorpMkt2@sinarmasmsiglife.co.id;";
					}else{
						s_DaftarEmailTo = "CorpMarketing@sinarmasmsiglife.co.id;";
					}
				}
				//Jalur Dist. DM/TM
				else if(lcaId.equals("40")){
					s_DaftarEmailTo = emailAdmin;
					if(lwk_id.equals("01")){
						if(lsrg_id.equals("00")){//Roxy
							s_DaftarEmailTo = "DMTM@sinarmasmsiglife.co.id;";
						}else if(lsrg_id.equals("02")){//BSIM
							if(produk.getLsbs_id().equals("163")){
								s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_bsim");
							}
						}
					}else if(lwk_id.equals("02")){
						if(lsrg_id.equals("00")){//ARCO
							s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_arco");
						}else if(lsrg_id.equals("01")){//SMP
							s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_smp");
						}else if(lsrg_id.equals("02")){//SSS
							s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_sss");
						}else if(lsrg_id.equals("03")){//SIP
							s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_sip");
						}else if(lsrg_id.equals("04")){//RedBerry
							s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_redberry");
						}else if(lsrg_id.equals("05")){//PSJS
							s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_psjs");
						}
					}
				}
				//Jalur Dist. EFC
				else if(lcaId.equals("65")){
					s_DaftarEmailTo = emailAgen;

					Map agen2 = uwDao.selectEmailAgen(spaj);
					String msagId = (String) agen2.get("MSAG_ID");
					Map emailHybrid = uwDao.selectEmailHybrid(msagId);
					String emailSiHybrid = "";
					if(emailHybrid != null){
						emailSiHybrid = (String) emailHybrid.get("EMAIL");
					}
					s_DaftarEmailTo = s_DaftarEmailTo + ";" + emailSiHybrid;
				}

				if(!s_DaftarEmailTo.equals("")){
					String[] emailTo = s_DaftarEmailTo.split(";");
					to = emailTo;
				}

				String emailUser = currentUser.getEmail();
				if(emailUser == null) emailUser = "";

				String[] emailCc = ("qabas@sinarmasmsiglife.co.id;rizkah@sinarmasmsiglife.co.id;" + emailUser).split(";");
				cc = emailCc;
				if(cc_banc){
					cc = (emailUser+";windi_wulan@sinarmasmsiglife.co.id;nuraini@sinarmasmsiglife.co.id;mega@sinarmasmsiglife.co.id;lusi_susanti@sinarmasmsiglife.co.id;helmy@sinarmasmsiglife.co.id;yudi@sinarmasmsiglife.co.id").split(";");
				}
				
				//cc untuk bank harda / Produk SUPERLINK SYARIAH
				if(i_jenisbank==25 || (produk.getLsbs_id()==200 && "005,006".indexOf(lsdbs_3digit)>-1)) {// Ridhaal untuk bank Harda / produk superlink - Req Dewi Andriyati (10/20/2016)
		    		mapEmail=Common.serializableMap(uwDao.selectMstConfig(6, "sendAgenNAdmin","bancass.harda"));
					String ccHarda =mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					ccHarda = ccHarda.concat(";" + emailUser);
					emailCc = ccHarda.split(";");
					cc = emailCc;
				}

				try{
					EmailPool.send("E-Lions", 1, 1, 0, 0, 
							null, 0, 0, commonDao.selectSysdate(), null,
							true, 
							from, 
							to, 
							cc, 
							null, 
							subject, 
							pesan+footer, 
							null, null);

				}catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
					desc = "Terjadi Kesalahan Pada Proses Pengiriman Email!";
				}

				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "GAGAL VALIDASI " + ket_followup, spaj, 0);
				desc = "Proses Pengiriman Email Follow Up Berhasil Dilakukan!";}
			else{
				String check[] = new String[]{chbox1, chbox2, chbox3};

				for(int i=0;i<check.length;i++){
					if(!check[i].equals("")) ket_followup = ket_followup + check[i] + ",";
				}
				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "BERHASIL VALIDASI ( "+ket_followup+" )", spaj, 0);
				desc = "Berhasil Proses Follow Up!";
			}
		}catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "Terjadi Kesalahan Pada Proses Follow Up!";
		}
		
		return desc;
	}
	
	
	/*=======================SavingBACSpajNew===================================*/
	
	public Cmdeditbac savingspajnew(Object cmd,Errors err,String keterangan, User currentUser) throws ServletException,IOException,Exception
	{
	//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
	  if (keterangan.equals("input"))
		{
			return this.savingBacSpajNew.insertspajbaru(cmd,currentUser);
		}else{
			return this.savingBacSpajNew.editspaj(cmd,currentUser);
		}
	}
	
	public String selectMspoFLagSpaj(String spaj){
		return bacDao.selectMspoFlagSpaj(spaj);
	}
	
	public PembayarPremi selectPemPremiTemp(String no_temp) {
		return (PembayarPremi) this.bacDao.selectPemPremiTemp(no_temp);
	}
	
	public List selectDaftarInvestasiTemp(String no_temp){
		return this.bacDao.selectDaftarInvestasiTemp(no_temp);
	}

	public Integer selectCountKeluarga(String no_temp) {		
		return bacDao.selectCountKeluarga(no_temp);
	}
	
	public String selectTotalSmsserver_in(String beg_date, String end_date, Integer update,Integer process,Integer id,Integer lus_id,Integer followup, Map params){
		return basDao.selectTotalSmsserver_in(beg_date, end_date, update, process,id,lus_id,followup, params);
	}
	
	public void updateMstInsuredHealthQuest(String spaj, Integer health_quest) {
		uwDao.updateMstInsuredHealthQuest(spaj, health_quest);
	}
	
	public List<Map> selectCountUserUW(Integer lspd_id, Integer product){
		return this.uwDao.selectCountUserUW(lspd_id, product);
	}
	
	public List<Map> selectPemegangPolis2(String tipe, String reg_spaj, String mcl_first, Date mspe_date_birth, String mspe_no_identity, String mspe_mother){
		return uwDao.selectPemegangPolis2(tipe, reg_spaj, mcl_first, mspe_date_birth, mspe_no_identity, mspe_mother);
	}
	
	public Integer selectLockID(String reg_spaj, Integer lspd_id){
		return uwDao.selectLockID(reg_spaj, lspd_id);
	}
	
	public String selectSeqInboxId(){
		return this.uwDao.selectSeqInboxId();		
	}
	
	public void updateMstInboxLspdId(String mi_id, Integer lspd_id, Integer lspd_id_from, Integer flag_mi_pos, Integer flag_pending, String reg_spaj, String old_reg_spaj, String mi_desc, Integer flag_validasi){
		this.uwDao.updateMstInboxLspdId(mi_id, lspd_id, lspd_id_from, flag_mi_pos, flag_pending, reg_spaj, old_reg_spaj, mi_desc, flag_validasi);
	}
	
	public Map selectTransferImaging(String mi_id) throws DataAccessException {
		return this.uwDao.selectTransferImaging(mi_id);
	}
	
	public void prosesSnows(String spaj, String lusId, Integer lspd_id, Integer lspd_id_coll){
		this.uwDao.prosesSnows(spaj, lusId, lspd_id, lspd_id_coll);
	}
	
	public List<Map> selectLstDetBisnisPAS() throws DataAccessException {
		return this.basDao.selectLstDetBisnisPAS();
	}
	
	public void generateNoKartuPAS(HashMap map) throws DataAccessException {
		this.basDao.generateNoKartuPAS(map);
	}
	
	public List<Map> selectKartuPasbyTglInput(String tgl_input) throws DataAccessException{
		return this.uwDao.selectKartuPasbyTglInput(tgl_input);
	}
	
	public List selectDynamicReportCs(HashMap<String, Object> param) {
		return commonDao.selectDynamicReportCs(param);
	}
	
	public Boolean prosesSaveQuestionare(HttpServletRequest request, String spaj, Integer customFields, User currentUser, Integer page) throws ServletRequestBindingException, ParseException{
		Boolean proses = true;

		String mspo_flag = ServletRequestUtils.getStringParameter(request, "mspo_flag");
		Integer an_order=0;
		Datausulan du = bacDao.selectDataUsulanutama(spaj);
		Date question_valid_date = null;
		Date sept2015 =  defaultDateFormat.parse("01/01/2015");
		Integer index=null;
		Integer index2=null;
	
		//Set Data Questionare , apakah > AUG2015 || < AUG2015
		if(du.getMste_beg_date().before(sept2015)){
				question_valid_date = defaultDateFormat.parse("01/08/2014");
		}else{
			question_valid_date = null;
			if(page==3){
				index=105;
				index2=130;
			}else if(page==4){
				index=131;
				index2=156;
			}
		}
		
		if(mspo_flag.equals("4")){
			try{
				//delete dulu datanya , krn ga pake system update, dan constraint.. dipergunakan untuk edit juga
				deleteQuestionareNew(spaj,"and option_type not in (3)");
				
				ArrayList data_LQLSIO=Common.serializableList(selectDataLQL(12,question_valid_date,index,index2));
				for(int i=0;i<data_LQLSIO.size();i++){
					HashMap m = (HashMap) data_LQLSIO.get(i);
					Integer question_id = ((BigDecimal)m.get("QUESTION_ID")).intValue();
					Integer question_type_id = ((BigDecimal)m.get("QUESTION_TYPE_ID")).intValue();
					Integer option_type = ((BigDecimal)m.get("OPTION_TYPE")).intValue();
					Integer option_group = ((BigDecimal)m.get("OPTION_GROUP")).intValue();
					Date valid_date= (Date)m.get("QUESTION_VALID_DATE");
					Integer option_order = ((BigDecimal)m.get("OPTION_ORDER")).intValue();
					String dataForSubstring =ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group+"_"+option_order,"");
					
					/**
					 * krn ga bisa diset value, sehingga value untuk radio butt diset coding
					 * contoh : jika opt_ord =1 (ya), dicentang, maka valuenya 1 , jika tidak dicentang diset 0
					 */
					
					//PP :1, TT :2
					if ("1,2".indexOf(option_group.toString())>-1){
						if (option_type==1 ){
							if(option_order==1){
								dataForSubstring=ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group,"");
								if(dataForSubstring.equals("0")){
									dataForSubstring="0";
								}
							}
	
							if(option_order==2){
								dataForSubstring=ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group,"");
								if(dataForSubstring.equals("0")){
									dataForSubstring="1";
								}else if (dataForSubstring.equals("1")){
									dataForSubstring="0";
								}else{
									dataForSubstring="";
								}
							}
						}
					}

					//update tinggi dan berat si PP & TT
					if(option_type==4){
						if(option_group==1){//PP
							Pemegang pmg = bacDao.selectpp(spaj);
							if(!dataForSubstring.equals("")){
								if(option_order==1){
									updateQuestionareNewPPTT(pmg.getMcl_id(), "MCL_HEIGHT", Double.parseDouble(dataForSubstring));
								}else{
									updateQuestionareNewPPTT(pmg.getMcl_id(), "MCL_WEIGHT", Double.parseDouble(dataForSubstring));
								}
							}
						}else if(option_group==2){//TT
							Tertanggung tgg = bacDao.selectttg(spaj);
							if(!dataForSubstring.equals("")){
								if(option_order==1){
									updateQuestionareNewPPTT(tgg.getMcl_id(), "MCL_HEIGHT", Double.parseDouble(dataForSubstring));
								}else{
									updateQuestionareNewPPTT(tgg.getMcl_id(), "MCL_WEIGHT", Double.parseDouble(dataForSubstring));
								}
							}
						}
					}
					
					//save datanya si Question PP
					insertQuestionareNew(1, spaj, 12, question_id , valid_date, option_type, option_group, option_order, 1, 
							dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));
				}
				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "input data quesioner", spaj, 0);
			}catch(Exception e){
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				proses=false;
			}
		}else{
			try{
				if(page==1){
					
					//Save Data Questionare Tertanggung
					deleteQuestionareNew(spaj,"and option_type not in (3) AND QUESTION_TYPE_ID=2");
					ArrayList data_LQLTT=Common.serializableList(selectDataLQL(2,question_valid_date,index,index2));
					for(int i=0;i<data_LQLTT.size();i++){		
						HashMap m = (HashMap) data_LQLTT.get(i);
						Integer question_id = ((BigDecimal)m.get("QUESTION_ID")).intValue();
						Integer option_type = ((BigDecimal)m.get("OPTION_TYPE")).intValue();
						Integer option_group = ((BigDecimal)m.get("OPTION_GROUP")).intValue();
						Date valid_date= (Date)m.get("QUESTION_VALID_DATE");
						Integer option_order = ((BigDecimal)m.get("OPTION_ORDER")).intValue();
						String dataForSubstring =ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group+"_"+option_order,"");
						logger.info( question_id+"_"+option_type+"_"+option_group+"_"+option_order);
						logger.info(dataForSubstring);

						/**
						 * krn ga bisa diset value, sehingga value untuk radio butt diset coding
						 * contoh : jika opt_ord =1 (ya), dicentang, maka valuenya 1 , jika tidak dicentang diset 0
						 */
						if (option_type==1 ){
							if(option_order==1){
								dataForSubstring=ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group,"");
								if(dataForSubstring.equals("0")){
									dataForSubstring="0";
								}
							}

							if(option_order==2){
								dataForSubstring=ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group,"");
								if(dataForSubstring.equals("0")){
									dataForSubstring="1";
								}else if (dataForSubstring.equals("1")){
									dataForSubstring="0";
								}else{
									dataForSubstring="";
								}
							}
						}

						if (option_type==3 && question_id==3){customFields = ServletRequestUtils.getIntParameter(request, "jml");
						customFields=customFields-1;
						an_order=bacDao.count_answer_table(spaj, 2, question_id,null);
						if(an_order==null){
							an_order=0;
						}
						if(customFields<=0){
							if (an_order==0){
							//save data jawaban table
							deleteQuestionareNew(spaj,"and option_type=3 and answer is null and question_id=3");
							insertQuestionareNew(1, spaj, 2, question_id , valid_date, option_type, option_group, option_order, 0, 
									dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));}
						}else{
							deleteQuestionareNew(spaj,"and option_type=3 and answer is null and question_id=3");
							deleteQuestionareNew(spaj,"and option_type=3 and answer is not null and question_id=3");
							for(int z=1;z<=customFields;z++){
								String namaPT = ServletRequestUtils.getStringParameter(request, "namaPT_"+z);
								String up = ServletRequestUtils.getStringParameter(request, "up_"+z);
								String selectProduk = ServletRequestUtils.getStringParameter(request, "selectProduk"+z);
								String tgl = ServletRequestUtils.getStringParameter(request, "tgl_"+z);

								dataForSubstring=z+"~"+namaPT+"~"+selectProduk+"~"+up+"~"+tgl;

								logger.info(dataForSubstring);
								//save data jawaban table
								insertQuestionareNew(1, spaj, 2, question_id , valid_date, option_type, option_group, option_order, z, 
										dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));
							}
						}
						}

						if (option_type==3 && question_id==5){Integer customFields2 = ServletRequestUtils.getIntParameter(request, "jml3");
						customFields2=customFields2-1;
						an_order=bacDao.count_answer_table(spaj, 2, question_id,null);
						if(an_order==null){
							an_order=0;
						}
						if(customFields2<=0){
							if (an_order==0){
							//save data jawaban table
							deleteQuestionareNew(spaj,"and option_type=3 and answer is null and question_id=5");
							insertQuestionareNew(1, spaj, 2, question_id , valid_date, option_type, option_group, option_order, 0, 
									dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));}
						}else{
							deleteQuestionareNew(spaj,"and option_type=3 and answer is null and question_id=5");
							deleteQuestionareNew(spaj,"and option_type=3 and answer is not null and question_id=5");
							for(int z=1;z<=customFields2;z++){
								String namaPT2 = ServletRequestUtils.getStringParameter(request, "namaPT2_"+z);
								String up = ServletRequestUtils.getStringParameter(request, "up2_"+z);
								String selectProduk = ServletRequestUtils.getStringParameter(request, "selectProduk2"+z);
								String tgl = ServletRequestUtils.getStringParameter(request, "tgl2_"+z);
								String claim = ServletRequestUtils.getStringParameter(request, "claim"+z);

								dataForSubstring=z+"~"+namaPT2+"~"+selectProduk+"~"+up+"~"+tgl+"~"+claim;

								logger.info(dataForSubstring);
								//save data jawaban table
								insertQuestionareNew(1, spaj, 2, question_id , valid_date, option_type, option_group, option_order, z, 
										dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));
							}
						}
						}
						//update tinggi dan berat si TT
						if(option_type==4 && question_id==13){
							Tertanggung tgg = bacDao.selectttg(spaj);

							if(!dataForSubstring.equals("")){
								if(option_order==1){
									updateQuestionareNewPPTT(tgg.getMcl_id(), "MCL_HEIGHT", Double.parseDouble(dataForSubstring));
								}else{
									updateQuestionareNewPPTT(tgg.getMcl_id(), "MCL_WEIGHT", Double.parseDouble(dataForSubstring));
								}
							}

						}
						//save datanya si Question TT
						if(option_type!=3){
							insertQuestionareNew(1, spaj, 2, question_id , valid_date, option_type, option_group, option_order, 1, 
									dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));}
					}
				}else if(page==2){
					
					//Save Data Questionare Pemegang
					//delete dulu datanya , krn ga pake system update, dan constraint.. dipergunakan untuk edit juga
					deleteQuestionareNew(spaj,"and option_type not in (3) AND QUESTION_TYPE_ID=1");
					
					ArrayList data_LQLPP=Common.serializableList(selectDataLQL(1,question_valid_date,index,index2));
					for(int i=0;i<data_LQLPP.size();i++){		
						HashMap m = (HashMap) data_LQLPP.get(i);
						Integer question_id = ((BigDecimal)m.get("QUESTION_ID")).intValue();
						Integer question_type_id = ((BigDecimal)m.get("QUESTION_TYPE_ID")).intValue();
						Integer option_type = ((BigDecimal)m.get("OPTION_TYPE")).intValue();
						Integer option_group = ((BigDecimal)m.get("OPTION_GROUP")).intValue();
						Date valid_date= (Date)m.get("QUESTION_VALID_DATE");
						Integer option_order = ((BigDecimal)m.get("OPTION_ORDER")).intValue();
						String dataForSubstring =ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group+"_"+option_order,"");

						logger.info(dataForSubstring);
						/**
						 * krn ga bisa diset value, sehingga value untuk radio butt diset coding
						 * contoh : jika opt_ord =1 (ya), dicentang, maka valuenya 1 , jika tidak dicentang diset 0
						 */
						if (option_type==1 ){
							if(option_order==1){
								dataForSubstring=ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group,"");
								if(dataForSubstring.equals("0")){
									dataForSubstring="0";
								}
							}

							if(option_order==2){
								dataForSubstring=ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group,"");
								if(dataForSubstring.equals("0")){
									dataForSubstring="1";
								}else if (dataForSubstring.equals("1")){
									dataForSubstring="0";
								}else{
									dataForSubstring="";
								}
							}
						}

						/*	if (option_type==1){
											if(option_order==2){
												dataForSubstring=ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group,"");
												if(dataForSubstring.equals("1")){
													dataForSubstring="0";
												}
											}
										}*/

						//update tinggi dan berat si PP
						if(option_type==4){
							Pemegang pmg = bacDao.selectpp(spaj);

							if(!dataForSubstring.equals("")){
								if(option_order==1){
									updateQuestionareNewPPTT(pmg.getMcl_id(), "MCL_HEIGHT", Double.parseDouble(dataForSubstring));
								}else{
									updateQuestionareNewPPTT(pmg.getMcl_id(), "MCL_WEIGHT", Double.parseDouble(dataForSubstring));
								}
							}

						}


						//save datanya si Question PP
						insertQuestionareNew(1, spaj, 1, question_id , valid_date, option_type, option_group, option_order, 1, 
								dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));
					}
				}else if(page==3){
					//Save Questionare Data Kesehatan I
					deleteQuestionareNew(spaj,"and option_type not in (3) AND QUESTION_TYPE_ID=3 AND QUESTION_ID BETWEEN "+index+" AND "+index2);
					ArrayList data_LQLQS=Common.serializableList(selectDataLQL(3,question_valid_date,index,index2));
					for(int i=0;i<data_LQLQS.size();i++){		
						HashMap m = (HashMap) data_LQLQS.get(i);
						Integer question_id = ((BigDecimal)m.get("QUESTION_ID")).intValue();
						Integer question_type_id = ((BigDecimal)m.get("QUESTION_TYPE_ID")).intValue();
						Integer option_type = ((BigDecimal)m.get("OPTION_TYPE")).intValue();
						Integer option_group = ((BigDecimal)m.get("OPTION_GROUP")).intValue();
						Date valid_date= (Date)m.get("QUESTION_VALID_DATE");
						Integer option_order = ((BigDecimal)m.get("OPTION_ORDER")).intValue();
						String dataForSubstring =ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group,"");
						String id_quest = ServletRequestUtils.getStringParameter(request, "id_quest");
						/**
						 * krn ga bisa diset value, sehingga value untuk radio butt diset coding
						 * contoh : jika opt_ord =1 (ya), dicentang, maka valuenya 1 , jika tidak dicentang diset 0
						 */
						//PP :1, TT :2, TT Tambahan1 :3, TT tambahan2 :4, TT tambahan3 :5 
						if ("1,2,3,4,5".indexOf(option_group.toString())>-1){
							if (option_type==1 ){
								if(option_order==1){
									if(dataForSubstring.equals("0")){
										dataForSubstring="0";
									}
								}

								if(option_order==2){
									if(dataForSubstring.equals("1")){
										dataForSubstring="0";
									}else if (dataForSubstring.equals("0")){
										dataForSubstring="1";
									}else{
										dataForSubstring="";
									}
								}
							}
						}
						if (option_type==0){
							dataForSubstring =ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group+"_"+option_order,"");
						}
						if(option_type!=3){
							//save datanya si Question QS
							insertQuestionareNew(1, spaj, 3, question_id , valid_date, option_type, option_group, option_order, 1, 
									dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));}
					}
				}else if(page==4){
					//Save Questionare Data Kesehatan II
					deleteQuestionareNew(spaj,"and option_type not in (3) AND QUESTION_TYPE_ID=3 AND QUESTION_ID BETWEEN "+index+" AND "+index2);
					ArrayList data_LQLQS=Common.serializableList(selectDataLQL(3,question_valid_date,index,index2));
					for(int i=0;i<data_LQLQS.size();i++){		
						HashMap m = (HashMap) data_LQLQS.get(i);
						Integer question_id = ((BigDecimal)m.get("QUESTION_ID")).intValue();
						Integer question_type_id = ((BigDecimal)m.get("QUESTION_TYPE_ID")).intValue();
						Integer option_type = ((BigDecimal)m.get("OPTION_TYPE")).intValue();
						Integer option_group = ((BigDecimal)m.get("OPTION_GROUP")).intValue();
						Date valid_date= (Date)m.get("QUESTION_VALID_DATE");
						Integer option_order = ((BigDecimal)m.get("OPTION_ORDER")).intValue();
						String dataForSubstring =ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group,"");
						String id_quest = ServletRequestUtils.getStringParameter(request, "id_quest");
						/**
						 * krn ga bisa diset value, sehingga value untuk radio butt diset coding
						 * contoh : jika opt_ord =1 (ya), dicentang, maka valuenya 1 , jika tidak dicentang diset 0
						 */
						//PP :1, TT :2, TT Tambahan1 :3, TT tambahan2 :4, TT tambahan3 :5 
						if ("1,2,3,4,5".indexOf(option_group.toString())>-1){
							if (option_type==1 ){
								if(option_order==1){
									if(dataForSubstring.equals("0")){
										dataForSubstring="0";
									}
								}

								if(option_order==2){
									if(dataForSubstring.equals("1")){
										dataForSubstring="0";
									}else if (dataForSubstring.equals("0")){
										dataForSubstring="1";
									}else{
										dataForSubstring="";
									}
								}
							}
						}
						if (option_type==0){
							dataForSubstring =ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group+"_"+option_order,"");
						}
						if (option_type==3 ){
							if("56,155".indexOf(question_id.toString())> -1){
								if(option_order==1){
									customFields = ServletRequestUtils.getIntParameter(request, "jml2");
									customFields = customFields-1;
									an_order=bacDao.count_answer_table(spaj, 3, question_id,option_order);
									if(an_order==null){
										an_order=0;
									}
									if(customFields<=0){
										if (an_order==0){
										//save data jawaban table
										deleteQuestionareNew(spaj,"and option_type=3 and answer is null and question_id="+question_id+" and option_order=1");
										insertQuestionareNew(1, spaj, 3, question_id , valid_date, option_type, option_group, option_order, 0, 
												dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));}
									}else{
										deleteQuestionareNew(spaj,"and option_type=3 and answer is null and question_id="+question_id+" and option_order=1");
										deleteQuestionareNew(spaj,"and option_type=3 and answer is not null and question_id="+question_id+" and option_order=1");
										for(int z=1;z<=customFields;z++){
											String umurCT_ = ServletRequestUtils.getStringParameter(request, "umurCT_"+z,"");
											String kondisit_ = ServletRequestUtils.getStringParameter(request, "kondisit_"+z,"");
											String selectKategori = ServletRequestUtils.getStringParameter(request, "selectKategori"+z,"");
											String penyebabt_ = ServletRequestUtils.getStringParameter(request, "penyebabt_"+z,"");

											logger.info(selectKategori+"~"+umurCT_+"~"+kondisit_+"~"+penyebabt_);
											dataForSubstring=selectKategori+"~"+umurCT_+"~"+kondisit_+"~"+penyebabt_;
											//save data jawaban table
											insertQuestionareNew(1, spaj, 3, question_id , valid_date, option_type, option_group, option_order, z, 
													dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));
										}
									}
								}else {
									customFields = ServletRequestUtils.getIntParameter(request, "jml5");
									customFields=customFields-1;
									an_order=bacDao.count_answer_table(spaj, 3, question_id,option_order);
									if(an_order==null){
										an_order=0;
									}
									if(customFields<=0){
										if (an_order==0){
										//save data jawaban table
										deleteQuestionareNew(spaj,"and option_type=3 and answer is null and question_id="+question_id+" and option_order=2");
										insertQuestionareNew(1, spaj, 3, question_id , valid_date, option_type, option_group, option_order, 0, 
												dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));}
									}else{
										deleteQuestionareNew(spaj,"and option_type=3 and answer is null and question_id="+question_id+" and option_order=2");
										deleteQuestionareNew(spaj,"and option_type=3 and answer is not null and question_id="+question_id+" and option_order=2");
										for(int z=1;z<=customFields;z++){
											String umurCP_ = ServletRequestUtils.getStringParameter(request, "umurCP_"+z, "");
											String kondisip_ = ServletRequestUtils.getStringParameter(request, "kondisip_"+z,"");
											String selectKategoriP = ServletRequestUtils.getStringParameter(request, "selectKategoriP"+z,"");
											String penyebabp_ = ServletRequestUtils.getStringParameter(request, "penyebabp_"+z,"");

											dataForSubstring=selectKategoriP+"~"+umurCP_+"~"+kondisip_+"~"+penyebabp_;
											//save data jawaban table
											insertQuestionareNew(1, spaj, 3, question_id , valid_date, option_type, option_group, option_order, z, 
													dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));
										}
									}
								}
							}else if ("57,156".indexOf(question_id.toString()) >-1 ){
								customFields = ServletRequestUtils.getIntParameter(request, "jml4");
								customFields=customFields-1;
								an_order=bacDao.count_answer_table(spaj, 3, question_id,null);
								if(an_order==null){
									an_order=0;
								}
								if(customFields<=0){
									if (an_order==0){
									//save data jawaban table
									deleteQuestionareNew(spaj,"and option_type=3 and answer is null and question_id="+question_id);
									insertQuestionareNew(1, spaj, 3, question_id , valid_date, option_type, option_group, option_order, 0, 
											dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));}
								}else{
									deleteQuestionareNew(spaj,"and option_type=3 and answer is null and question_id="+question_id);
									deleteQuestionareNew(spaj,"and option_type=3 and answer is not null and question_id="+question_id);
									for(int z=1;z<=customFields;z++){
										String keterangansht_ = ServletRequestUtils.getStringParameter(request, "keterangansht_"+z);

										dataForSubstring=z+"~"+keterangansht_;
										//save data jawaban table
										insertQuestionareNew(1, spaj, 3, question_id , valid_date, option_type, option_group, option_order, z, 
												dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));
									}
								}
							}
						}
						if(option_type!=3){
							//save datanya si Question QS
							insertQuestionareNew(1, spaj, 3, question_id , valid_date, option_type, option_group, option_order, 1, 
									dataForSubstring, valid_date, Integer.parseInt(currentUser.getLus_id()));}
					}
					uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "input data quesioner", spaj, 0);
				}
			}catch(Exception e){
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				logger.error("ERROR :", e);
				proses=false;
			}
		}
		
		return proses;
	}
	
	public List selectDaftar_kyc_pprm(String spaj) {
		return this.bacDao.selectDaftar_kyc_pprm(spaj);
	}
	
	public List select_semua_mst_peserta2(String reg_spaj) {
		return this.bacDao.select_semua_mst_peserta2(reg_spaj);
	}
	
	public List selectMstPositionQc(String reg_spaj, Integer jenis) {
		return this.uwDao.selectMstPositionQc(reg_spaj, jenis);
	}
	
	public List reportQc(String bdate, String edate){
		return this.uwDao.reportQc(bdate, edate);
	}

	public List <Map> selectMstBenefeciary(String reg_spaj) throws DataAccessException {
		return this.uwDao.selectMstBenefeciary(reg_spaj);
	} 
	
	public ArrayList selectDataIcdExclude(String spaj) {
		ArrayList data =new ArrayList();
		data = uwDao.selectDataIcdExclude(spaj);
		return data;
	}
	
	public Map selectInformasiCabangFromSpaj(String spaj){
		return uwDao.selectInformasiCabangFromSpaj(spaj);
	}
	
	/*
	 * MANTA (04-03-2015)
	 * Proses pengurangan unit investasi pada produk unit link apabila ada extra premi
	 */
	public Utama prosesExtraPremi(Utama command, String ket, Integer flag_jenis/*,HttpServletRequest request*/){
		try{
			Product productIns = (Product) command.getLsProduk().get(command.getLsProduk().size()-1);
			Product productUtm = (Product) command.getLsProduk().get(0);
			Integer lsgb_id = reinstateDao.selectIsPolisUnitlink(command.getSpaj());
			productIns.setMspr_flag_jenis(flag_jenis);
			
			akseptasiDao.insertMstProductInsert(productIns);
			//uwDao.insertMstPositionSpajRedFlag("0", ket, productIns.getReg_spaj(), 5,productIns.getLsbs_id().toString());
			
			//Khusus untuk produk unit link
			if(lsgb_id == 17){
//			if(productUtm.getLsbs_id() == 120 && ("10,11,12,19,20,21,22,23,24".indexOf(productUtm.getLsdbs_number().toString())>-1)){
				productIns.setMspr_premium(FormatNumber.round(productIns.getMspr_premium(), 2));
				productIns.setMspr_rate(0.0);
				bacDao.insertMstBiayaUlink(productIns);
				
				//Cek apakah biaya lebih besar dari premi pokok
				//Jika lebih besar maka tambahkan kedalam biaya kekurangan premi
				Integer totalbiaya = selectTotBiayaMuKe(productIns.getReg_spaj(), 1);
				if(totalbiaya > productUtm.getMspr_premium()){
					Integer prmbaru = -(totalbiaya - productUtm.getMspr_premium().intValue());
					Integer updated = updateKekuranganPrm(productIns.getReg_spaj(), prmbaru);
					if(updated == 0){
						insertMstBiayaUlink2(productIns.getReg_spaj(), 1, 99, prmbaru, 0, null);
					}
				}

				ArrayList<HashMap> listInvest = new ArrayList<HashMap>();
				listInvest = selectDetailInvestasi(productIns.getReg_spaj());
				Double invMu1 = 0.0;
				Double invMu2 = 0.0;
				Double biayaextr = productIns.getMspr_premium().doubleValue();
				
				for(int i=0; i<listInvest.size(); i++){
					HashMap inv = listInvest.get(i);
					Double invInves = ((BigDecimal) inv.get("MDU_JUMLAH")).doubleValue();
					Integer invMu = ((BigDecimal) inv.get("MU_KE")).intValue();
					if(invMu == 1){
						invMu1 = invMu1 + invInves;
					}else if(invMu == 2){
						invMu2 = invMu2 + invInves;
					}
				}
				
				if(invMu1 != 0){
					if(invMu1 > biayaextr){
						for(int i=0; i<listInvest.size(); i++){
							HashMap inv = listInvest.get(i);
							Integer invMu = ((BigDecimal) inv.get("MU_KE")).intValue();
							Double invPersen = ((BigDecimal) inv.get("MDU_PERSEN")).doubleValue();
							if(invMu == 1){
								Double invUpd = ((invMu1 - biayaextr) * invPersen)/100;
								updateMduJumlah(productIns.getReg_spaj(), inv.get("LJI_ID").toString(), invMu.toString(), invUpd);
							}
						}
					}else{
						Double biayaextr2 = biayaextr - invMu1;
						for(int i=0; i<listInvest.size(); i++){
							HashMap inv = listInvest.get(i);
							Integer invMu = ((BigDecimal) inv.get("MU_KE")).intValue();
							Double invPersen = ((BigDecimal) inv.get("MDU_PERSEN")).doubleValue();
							if(invMu == 1){
								updateMduJumlah(productIns.getReg_spaj(), inv.get("LJI_ID").toString(), invMu.toString(), 0.0);
							}else if(invMu == 2){
								Double invUpd = ((invMu2 - biayaextr2) * invPersen)/100;
								updateMduJumlah(productIns.getReg_spaj(), inv.get("LJI_ID").toString(), invMu.toString(), invUpd);
							}
						}
					}
				}else{
					for(int i=0; i<listInvest.size(); i++){
						HashMap inv = listInvest.get(i);
						Integer invMu = ((BigDecimal) inv.get("MU_KE")).intValue();
						Double invPersen = ((BigDecimal) inv.get("MDU_PERSEN")).doubleValue();
						if(invMu == 2){
							Double invUpd = ((invMu2 - biayaextr) * invPersen) / 100;
							updateMduJumlah(productIns.getReg_spaj(), inv.get("LJI_ID").toString(), invMu.toString(), invUpd);
						}
					}
				}
				//MSPR_PREMIUM untuk Extra Premi harus 0
				updatePrmExt(productIns.getReg_spaj(), productIns.getLsbs_id(), productIns.getLsdbs_number());
			}
		}catch(Exception e){
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		return command;
	}
	
	public void deleteExtraPremi(Utama command, Product delproduk){
		try{
			Product productUtm = (Product) command.getLsProduk().get(0);
			uwDao.deleteMstProductInsured(delproduk.getReg_spaj(), delproduk.getLsbs_id(), delproduk.getLsdbs_number());

			Integer lsgb_id = reinstateDao.selectIsPolisUnitlink(delproduk.getReg_spaj());
			
			//Khusus untuk produk unit link
			if(lsgb_id == 17){
//			if(productUtm.getLsbs_id() == 120 && ("10,11,12,19,20,21,22,23,24".indexOf(productUtm.getLsdbs_number().toString())>-1)){
				uwDao.deleteMstBiayaUlink(delproduk.getReg_spaj(), delproduk.getLsbs_id(), delproduk.getLsdbs_number(), null);
				
				ArrayList<Ulink> listUlink = selectMstUlinkAll(delproduk.getReg_spaj());
				ArrayList<HashMap> listInvest = new ArrayList<HashMap>();
				listInvest = selectDetailInvestasi(delproduk.getReg_spaj());
				Double totalbiaya = selectTotBiayaMuKe(delproduk.getReg_spaj(), 1).doubleValue();
				Double totalbiaya2 = selectTotBiayaMuKe(delproduk.getReg_spaj(), 2).doubleValue();
				Double kurangpremi = 0.0;
				Double invUpdate = 0.0;
				Double premipokok = 0.0;
				Double premitopup = 0.0;
				
				for(int i=0; i<listUlink.size(); i++){
					if(listUlink.get(i).getLt_id() == 1){
						premipokok = listUlink.get(i).getMu_jlh_premi();
					}else{
						premitopup = premitopup + listUlink.get(i).getMu_jlh_premi();
					}
				}
				
				uwDao.deleteMstBiayaUlink(delproduk.getReg_spaj(), null, null, 99);
				Double invPokok = premipokok - totalbiaya;
				Double invTopup = premitopup - totalbiaya2;
				
				if(totalbiaya > productUtm.getMspr_premium()){
					kurangpremi = -(totalbiaya - productUtm.getMspr_premium().intValue());
					insertMstBiayaUlink2(delproduk.getReg_spaj(), 1, 99, kurangpremi.intValue(), 0, null);
					invTopup = invTopup + kurangpremi;
					for(int i=0; i<listInvest.size(); i++){
						HashMap inv = listInvest.get(i);
						Integer invMu = ((BigDecimal) inv.get("MU_KE")).intValue();
						Double invPersen = ((BigDecimal) inv.get("MDU_PERSEN")).doubleValue();
						
						if(invMu == 2){
							invUpdate = (invTopup * invPersen) / 100;
						}
						updateMduJumlah(delproduk.getReg_spaj(), inv.get("LJI_ID").toString(), invMu.toString(), invUpdate);
					}
					
				}else{
					for(int i=0; i<listInvest.size(); i++){
						HashMap inv = listInvest.get(i);
						Integer invMu = ((BigDecimal) inv.get("MU_KE")).intValue();
						Double invPersen = ((BigDecimal) inv.get("MDU_PERSEN")).doubleValue();
						if(invMu == 1){
							invUpdate = (invPokok * invPersen) / 100;
						}else{
							invUpdate = (invTopup * invPersen) / 100;
						}
						updateMduJumlah(delproduk.getReg_spaj(), inv.get("LJI_ID").toString(), invMu.toString(), invUpdate);
					}
				}
			}
		}catch(Exception e){
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
	}
	
	public HashMap selectMstTransHistoryNewBussines(String reg_spaj) {
		return uwDao.selectMstTransHistoryNewBussines(reg_spaj);
	}

	public void prosesEndorsKetinggalanNew(String spaj, Integer lsbs) {
		this.uwDao.prosesEndorsKetinggalanNew(spaj,lsbs);
		
	}

	public Integer selectPunyaEndorsEkaSehatAdmedika(String spaj) {
		return uwDao.selectPunyaEndorsEkaSehatAdmedika(spaj);
	}
	
	public String insertDataJne(User currentUser, Upload upload) throws IOException {
		
		InputStreamReader is = null;
		BufferedReader br = null;
		SimpleDateFormat df=new SimpleDateFormat("dd/MM/yyyy");
		Date d_tglTerima=null;Integer i_insert =0, i_update=0,i_gagal=0;
		try {
//			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			is = new InputStreamReader(upload.getFile1().getInputStream());		
			br = new BufferedReader(is);
			String line = br.readLine();
	        while(line != null) {
	        	 String f[] = line.split("\t");
	        	       String s_connote = f[0];
	        	       String s_dest = f[1];
	        	       String s_consignee = f[2];
	        	       int lenConsignee = 0;
	        	       lenConsignee = s_consignee.length();
	        	       if(lenConsignee > 100){
	        	    	   s_consignee = s_consignee.substring(0, 99);
	        	       }
	        	      // String s_tglTerima = f[3];
	        	       String s_penerima = f[3];
	        	       String s_tglTerima = f[4];
      	       	
	             if(!s_connote.trim().equals("connoted")) {
	            	 HashMap map = new HashMap();	            	 
	            	 map.put("s_connote",s_connote.trim());
	            	 map.put("s_dest",s_dest.trim());
	            	 map.put("s_consignee",s_consignee.trim());
	            	 map.put("s_penerima",s_penerima.trim());
	            	 map.put("d_tglTerima",s_tglTerima.trim());
	            	 map.put("lus_id",currentUser.getLus_id());
	            	// map.put("spaj",currentUser.getLus_id());	            	
	            	 ArrayList spaj = Common.serializableList(commonDao.selectSPAJByConnote(s_connote.trim()));
	            	 if(!spaj.isEmpty()){
	            		 String reg_spaj ="";
	            		
	            		 for(int i=0;i<spaj.size();i++){
	            			HashMap list_spaj = (HashMap)spaj.get(i);
	            			reg_spaj =(String)list_spaj.get("REG_SPAJ");
	            			Integer jumlahConnote = commonDao.selectJumlahConnote(reg_spaj);
	            			map.put("spaj",reg_spaj);
		            		if(s_tglTerima!=null && !s_tglTerima.equals(""))d_tglTerima=df.parse(s_tglTerima);
		            		if(jumlahConnote>0){
		 	            	   commonDao.updateDataJne(map);
		 	            	   i_update++;
		 	            	}else{
		 	            	   commonDao.insertDataJne(map);
		 	            	   i_insert++;
		 	            	 }
		            		 uwDao.saveMstTransHistory(reg_spaj, "tgl_pod_jne", d_tglTerima, null, null);
		            		 map.remove("spaj");
	            		 }	            		
	            	 }else{
	            		 i_gagal++;
	            	 }
	            	 
	        	     
	             }

	             line = br.readLine();
	        }
	        if(br!=null){
	        	br.close();
	        }			
		}catch( Exception e) { 
	         //logger.error("ERROR :", e);
			 String tes=e.getLocalizedMessage();
	         throw new RuntimeException(tes);
	    }
		finally{
			if(br!=null){
				br.close();
			}
			if(is!=null){
				is.close();
			}
		}				
        return "Berhasil Insert =" +i_insert+ " ,  Update ="+i_update +","+"  Gagal = " +i_gagal + " data" ;
	}
	
	public String selectLusId(String reg_spaj){
		return this.uwDao.selectLusId(reg_spaj);	
	}

	public List<Map> selectSnowsError(Integer lus_id){		
		return uwDao.selectSnowsError(lus_id);
	}
	
	public List selectDaftar_extra_premi(String spaj) {
		return this.bacDao.selectDaftar_extra_premi(spaj);
	}
	
	public List reportAutomatedUw(String bdate, String edate){
		return this.uwDao.reportAutomatedUw(bdate, edate);
	}
	
	/**
	 * Proses Input Setoran Premi Temp dan Refund Premi Non SPAJ
	 * @author 	MANTA
	 */
	public String prosesTempPremiRefund(PolicyInfoVO dataRefund, Integer jenis, String lus_id) {
		DateFormat yearfr = new SimpleDateFormat("yyyy");
		DateFormat stripfr = new SimpleDateFormat("dd-MM-yyyy");
		NumberFormat nf = new DecimalFormat("#.00;(#,##0.00)");
		Date now = selectSysdate();
		Date ldt_voc = null;
		String pesan = "", nopre = "", nojm = "", rk_simbol = "", ldt_thn = "", profitcenter = "", profitcenter2 = "";
		String ket = "";
		String noakun = "41111";
		String ls_ket[] = new String[8];
		String ls_acc[] = new String[2];
		String nospaj = "";
		Double jmlsetor = dataRefund.getPremitmp().doubleValue();
		Double jmlrefund = dataRefund.getPremirefundtmp().doubleValue();
		Double merchant_fee = jmlrefund - jmlsetor;
		Long rk_nocr = null;
		Premi premi = new Premi();
		HashMap tmp =  new HashMap();
		
		List listmstdrek = uwDao.selectMstDrekBasedNoTrx(dataRefund.getNo_trx());
		HashMap mstdrek = (HashMap) listmstdrek.get(0);
		
		List listrekekalife = uwDao.selectBankEkaLife(mstdrek.get("NOREK_AJS").toString());
		HashMap rekekalife = (HashMap) listrekekalife.get(0);
		Integer lsrek_id = ((BigDecimal) rekekalife.get("LSREK_ID")).intValue();
		
		if(dataRefund.getPotongan().equals("1")){
			ket = "MERCHANT FEE BNI (1,8 %)";
		}else if(dataRefund.getPotongan().equals("2")){
			ket = "MERCHANT FEE BCA (0,9 %)";
		}else if(dataRefund.getPotongan().equals("3")){
			ket = "MERCHANT FEE VISA MASTER (2,5 %)";
		}
		
		/*
		 * MANTA
		 * Penjurnalan Lama
		 */
		if(StringUtil.isEmpty((String) mstdrek.get("NO_PRE"))){
			//Jenis --> Belum ada SPAJ = 0, Sudah ada SPAJ = 1
			if(jenis == 0){
				nospaj = sequence.sequenceNoRegRefundNonSPAJ();
				dataRefund.setLsbsId(99999);
				noakun = "486";
			}else{
//				nospaj = dataRefund.getSpajNo().replace(".", "");
			}
			
			//Ambil data rekening AJS
			tmp = Common.serializableMap(this.uwDao.selectCounterRekEkalife(lsrek_id));
			premi.setRek_id(lsrek_id);
			if(props.getProperty("product.syariah").equals(dataRefund.getLsbsId())){
				profitcenter = "801";
				if(dataRefund.getNamaProduk().equals("POWER SAVE SYARIAH BSM")){
					ls_acc[0] = "851";
				}else{
					ls_acc[0] = "801";
				}
			}else{
				profitcenter = "001";
				ls_acc[0] = "0" + nospaj.substring(0,2);
			}
			
			if(tmp==null){
				pesan = "Proses input setoran premi gagal!";
			}else{
				try{
					//Generate No Voucher
					rk_simbol = (String) tmp.get("LSREK_SYMBOL");
					//rk_nocr = new Long(((BigDecimal) tmp.get("LSREK_NO_CR")).longValue());
					rk_nocr = this.accountingDao.selectNewCrNo("");		// counterAvnel
					if(rk_simbol == null) rk_simbol = "";
					if(rk_nocr == null) rk_nocr = new Long(0);
					
					//premi.setNo_cr(new Long(rk_nocr.longValue()+1));				
					premi.setNo_cr(rk_nocr);
					premi.setAccno((String) tmp.get("LSREK_GL_NO"));
					premi.setVoucher(rk_simbol.trim() + FormatString.rpad("0", premi.getNo_cr().toString(), 5) + "R");
					
					ldt_thn = yearfr.format(now);
					ldt_voc = this.uwDao.selectTanggalJurnal(premi.getVoucher(), ldt_thn);
					if(ldt_voc != null) {
						pesan = "No Voucher " + premi.getVoucher() + " kembar. Mohon diberitahukan ke ITwebandmobile@sinarmasmsiglife.co.id.";
						return pesan;
					}
					
					//Generate No Pre
					nopre = this.uwDao.selectGetPacGl("nopre");
					
					//Proses input jurnal
					this.uwDao.insertMst_tbank(nopre, 2, now, (Date) mstdrek.get("TGL_TRX"), premi, "M", jmlsetor, "0");
						
					if(dataRefund.getPolicyNo().isEmpty()) dataRefund.setPolicyNo(nospaj);
					
					if(jenis == 0){
						ls_ket[0] = "BANK";
						ls_ket[1] = "PREMI A/N " + dataRefund.getNamaPp().trim() + " " + stripfr.format((Date) mstdrek.get("TGL_TRX"));
						ls_ket[2] = "BANK";
						ls_ket[3] = "REFUND PREMI A/N " + dataRefund.getNamaPp().trim() + " " + stripfr.format((Date) mstdrek.get("TGL_TRX"));
					}else if(jenis == 1){
//						ls_ket[0] = "BANK " + dataRefund.getNamaPp().trim() + " " + stripfr.format(now);
//						ls_ket[1] = "TITIPAN PREMI NO. SPAJ " + nospaj.trim();
//						ls_ket[2] = "BK " + dataRefund.getNamaPp().trim() + " " + stripfr.format(now);
//						ls_ket[3] = "TP " + nospaj.trim() + " " + stripfr.format(now);
					}
					ls_ket[4] = "TP " + dataRefund.getPolicyNo().trim() + " " + premi.getVoucher();
					ls_ket[5] = "PP " + dataRefund.getPolicyNo().trim() + " " + premi.getVoucher();
					
					Integer lscb = 0;
//					if(jenis == 1) lscb = uwDao.selectCaraBayarFromSpaj(nospaj);
					
					if(lscb == 0){
						ls_acc[1] = bacDao.selectGetAccPremi(nospaj, 1);
					}else{
						ls_acc[1] = bacDao.selectGetAccPremi(nospaj, 3);
					}
					profitcenter2 = premi.getAccno().substring(0,3);
					premi.setProject(new String[]{profitcenter2, profitcenter});
					premi.setBudget(new String[]{premi.getAccno().substring(3), noakun});
					
					//Proses Insert IBANK
					String no_urutdrek = uwDao.selectMaxNoUrutMstDrekDet( dataRefund.getNo_trx() ) ;
					if(no_urutdrek != null && !"".equals(no_urutdrek)){
						no_urutdrek = no_urutdrek;
					}else{
						no_urutdrek = "0";
					}
					
					BigDecimal flagtgb = (BigDecimal) mstdrek.get("FLAG_TUNGGAL_GABUNGAN");
					if(flagtgb == null) flagtgb = new BigDecimal(0);
					Integer flagtunggalgabungan = flagtgb.intValue();
					uwDao.updateMstDrekListRk(nospaj, lus_id, mstdrek.get("NO_TRX").toString(), flagtunggalgabungan, mstdrek.get("JENIS").toString());
					uwDao.insertMstDrekDet(mstdrek.get("NO_TRX").toString(), "1", "1", jmlsetor, LazyConverter.toInt(no_urutdrek)+1, nospaj, 1, null, lus_id, now, nopre, mstdrek.get("NOREK_AJS").toString(), mstdrek.get("JENIS").toString(), (Date)mstdrek.get("TGL_TRX"));
					
					this.refundDao.insertMstTempPremiRefund(nospaj, 0, jenis, dataRefund.getKliNoRek(), dataRefund.getKliAtasNama(), dataRefund.getKliNamaBank(),
						 dataRefund.getKliCabangBank(), dataRefund.getKliKotaBank(), jmlrefund, dataRefund.getLkuId(), nopre, premi.getVoucher(), dataRefund.getNilaiKurs(), lus_id, mstdrek.get("NO_TRX").toString(), ket, null);
					
					this.uwDao.insertMst_dbank(nopre, 1, ls_ket[0], "M", jmlsetor, null, nospaj.trim());
					this.uwDao.insertMst_dbank(nopre, 2, ls_ket[1], "B", jmlsetor, new Integer(1), nospaj.trim());
					
					this.uwDao.insertMst_bvoucher(nopre, 1, ls_ket[0], jmlsetor, 0.0, premi.getProject()[0], premi.getBudget()[0], nospaj.trim());
					this.uwDao.insertMst_bvoucher(nopre, 2, ls_ket[1], 0.0, jmlsetor, premi.getProject()[1], premi.getBudget()[1], nospaj.trim());
					
//					if(jenis == 1){
//						//Generate No GM
//						nojm = this.uwDao.selectGetPacGl("nojm");
//						
//						this.uwDao.insertMst_ptc_tm(nojm, 2, now, nopre, lus_id);
//						this.uwDao.insertMst_ptc_jm(nojm, 1, ls_ket[4], jmlsetor, premi.getProject()[1], premi.getBudget()[1], "D", nospaj);
//						this.uwDao.insertMst_ptc_jm(nojm, 2, ls_ket[5], jmlsetor, ls_acc[0], ls_acc[1], "C", nospaj);
//					}
					
					//this.uwDao.updateLst_rek_ekalife(premi);
					
					//Untuk Proses Refund Non SPAJ langsung dibuatkan jurnal keluarnya
					if(jenis == 0 && nopre != ""){
						String nopre_new = this.uwDao.selectGetPacGl("nopre");
						String nojm_new = "";
						String voucher_new = "";
						
						MstPtcTmVO selectMstPtcTm = refundDao.selectMstPtcTm(nopre);
						
						if(nopre != null && !"".equals(nopre)){
							refundDao.insertMstTbankJurnalBalik(nopre_new, "0", nopre, jmlrefund);
							this.uwDao.insertMst_dbank(nopre_new, 1, ls_ket[2], "K", jmlrefund, null, nospaj.trim());
							if(dataRefund.getPotongan().equals("0")){
								this.uwDao.insertMst_dbank(nopre_new, 2, ls_ket[3] + " " + premi.getVoucher(), "C", jmlrefund, new Integer(1), nospaj.trim());
							}else{
								this.uwDao.insertMst_dbank(nopre_new, 2, ls_ket[3] + " " + premi.getVoucher(), "C", jmlsetor, new Integer(1), nospaj.trim());
								this.uwDao.insertMst_dbank(nopre_new, 3, "MERCHANT FEE", "C", merchant_fee, new Integer(1), nospaj.trim());
							}
						}
						
//						//Insert ke Tabel MST_PEMBAYARAN
//						Map temp = uwDao.selectMstCounter(154,"01");
//						Double hasil = (Double) temp.get("MSCO_VALUE");
//						Integer counter = new Integer(hasil.intValue());
//						refundDao.insertMstPembayaran(counter, nopre_new, dataRefund.getLkuId(), dataRefund.getKliNamaBank(), dataRefund.getKliAtasNama(), dataRefund.getKliCabangBank(), dataRefund.getKliKotaBank(), dataRefund.getKliNoRek(), jmlrefund.intValue(), "0");
//						counter = counter + 1;
//						bacDao.update_counter(counter.toString(), 154, "01");
						
						refundDao.updateMstTempPremiRefund(nospaj, nopre_new, null, null);
						
						pesan = "Proses refund premi Non SPAJ dengan No. "+ nospaj + " telah berhasil!";
					}else if(jenis == 1){
						pesan = "Proses input setoran premi dengan No. "+ nospaj +" telah berhasil!";
					}
					
				}catch(Exception e){
					pesan = "Terjadi kesalahan pada proses input setoran premi!";
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}
			}
			
		/*
		 * MANTA
		 * Penjurnalan Baru
		 */
		}else{
			try{
				nospaj = sequence.sequenceNoRegRefundNonSPAJ();
				Date dt_tglrk = (Date) mstdrek.get("TGL_TRX");
				String s_lkuid = (String) mstdrek.get("LKU_ID");
				String s_nopre = (String) mstdrek.get("NO_PRE");
				String s_novoc = refundDao.selectNoVoucher(s_nopre);
				String s_nojmsa, s_nojmsa_refund, s_budget, s_year, s_month, s_ketkurs;
				Double d_kursbulanan = new Double(1), d_jmlsetor = new Double(0), d_jmlrefund = new Double(0), d_merchant_fee = new Double(0);
				
				//Proses Insert IBANK
				String no_urutdrek = uwDao.selectMaxNoUrutMstDrekDet( dataRefund.getNo_trx() ) ;
				if(no_urutdrek != null && !"".equals(no_urutdrek)){
					no_urutdrek = no_urutdrek;
				}else{
					no_urutdrek = "0";
				}
				BigDecimal flagtgb = (BigDecimal) mstdrek.get("FLAG_TUNGGAL_GABUNGAN");
				if(flagtgb == null) flagtgb = new BigDecimal(0);
				Integer flagtunggalgabungan = flagtgb.intValue();
				if(flagtunggalgabungan == null) flagtunggalgabungan = 0;
				uwDao.updateMstDrekListRk(nospaj, lus_id, mstdrek.get("NO_TRX").toString(), flagtunggalgabungan, mstdrek.get("JENIS").toString());
				uwDao.insertMstDrekDet(mstdrek.get("NO_TRX").toString(), "1", "1", jmlsetor, LazyConverter.toInt(no_urutdrek)+1, nospaj, 1, null, lus_id, now, s_nopre, mstdrek.get("NOREK_AJS").toString(), mstdrek.get("JENIS").toString(), (Date)mstdrek.get("TGL_TRX"));
				
				refundDao.insertMstTempPremiRefund(nospaj, 1, jenis, dataRefund.getKliNoRek(), dataRefund.getKliAtasNama(), dataRefund.getKliNamaBank(),
					 dataRefund.getKliCabangBank(), dataRefund.getKliKotaBank(), jmlrefund, dataRefund.getLkuId(), s_nopre, s_novoc, dataRefund.getNilaiKurs(), lus_id, mstdrek.get("NO_TRX").toString(), ket, null);
				
				//Buat Jurnal Suspend Account terlebih dahulu
				ArrayList mstbvoucher = Common.serializableList(refundDao.selectMstBvoucher(s_nopre, 2));
				HashMap bvoucher = (HashMap) mstbvoucher.get(0);
				s_budget = "";
				s_ketkurs = " ";
				if(s_lkuid.equals("01")){
					s_budget = "486";
					d_jmlsetor = jmlsetor;
					d_jmlrefund = jmlrefund;
					d_merchant_fee = merchant_fee;
				}else if(s_lkuid.equals("02")){
					s_budget = "486";
					s_year = stripfr.format(dt_tglrk).substring(6,stripfr.format(dt_tglrk).length());
					s_month = stripfr.format(dt_tglrk).substring(3,5);
					d_kursbulanan = uwDao.selectLstMonthlyKursLmkNilai(s_year, s_month, s_lkuid);
					s_ketkurs = " US $ " + nf.format(jmlsetor) + " " + d_kursbulanan + " ";
					d_jmlsetor = jmlsetor * d_kursbulanan;
					d_jmlrefund = jmlrefund * d_kursbulanan;
					d_merchant_fee = merchant_fee * d_kursbulanan;
				}
				ls_ket[0] = "TITIPAN TIDAK DIKETAHUI " + stripfr.format((Date) dt_tglrk) + s_ketkurs + dataRefund.getNo_trx() + " " + s_novoc;
				ls_ket[1] = "TITIPAN PREMI NO. SPAJ " + nospaj.trim();
				ls_ket[2] = "REFUND PREMI " + dataRefund.getKliAtasNama() + " " + nospaj.trim() + " " + s_novoc;
				
				s_nojmsa = this.uwDao.selectGetPacGl("nojm");
				uwDao.insertMst_ptc_tm(s_nojmsa, 1, now, s_nopre, "0");
				uwDao.insertMst_ptc_jm(s_nojmsa, 1, ls_ket[0], d_jmlsetor, (String) bvoucher.get("PROJECT_NO"), (String) bvoucher.get("BUDGET_NO"), "D", nospaj);
				uwDao.insertMst_ptc_jm(s_nojmsa, 2, ls_ket[1], d_jmlsetor, (String) bvoucher.get("PROJECT_NO"), s_budget, "C", nospaj);
				uwDao.updateNoPreMstDrekDet(dataRefund.getNo_trx(), nospaj, null, s_nopre, s_nojmsa);
				
				//Jurnal Batal Suspend Account
				String s_nopre_new = this.uwDao.selectGetPacGl("nopre");
				refundDao.insertMstTbankJurnalBalik(s_nopre_new, "0", s_nopre, d_jmlrefund);
				refundDao.insertMstDbankJurnalBalikSA(nospaj, s_nopre, s_nopre_new, s_nojmsa, ls_ket[2], d_jmlrefund, d_merchant_fee);
				
				refundDao.updateMstTempPremiRefund(nospaj, s_nopre_new, s_nojmsa, null);
				
				pesan = "Proses refund premi Non SPAJ dengan No. "+ nospaj + " telah berhasil!";
			}catch(Exception e){
				pesan = "Terjadi kesalahan pada proses refund non SPAJ!";
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
		}
		
		try{
			ExternalDatabase centrix = new ExternalDatabase("ajs1_tss");
			ExternalDatabase centrix2 = new ExternalDatabase("ajs2_tss");
			centrix.doExec("exec tss_Application_CancelProcessing " + dataRefund.getIdCentrix() + ", 'CANCELED', 'TEAMLEADER', 11, 'Premi sudah di refund'");
			centrix2.doExec("exec tss_Application_CancelProcessing " + dataRefund.getIdCentrix() + ", 'CANCELED', 'TEAMLEADER', 11, 'Premi sudah di refund'");
		} catch (Exception e) {
			pesan = "Update Data ke Centrix gagal!";
			logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		return pesan;
	}
	
	public String UpdateDataNIK(User currentUser, Upload upload) throws IOException {
		InputStreamReader is = null;
		BufferedReader br = null;
		try {
//			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			is = new InputStreamReader(upload.getFile1().getInputStream());		
			br = new BufferedReader(is);
			String line = br.readLine();
	        while(line != null) {
	        	 String f[] = line.split("\t");
	        	       String s_nopol = f[0];
	        	       String s_update = f[1];     	       
      	       	
	             if(!s_nopol.equals("nopol")) {		            	 
	            	 String reg_spaj = uwDao.selectMstPolicyRegSpaj(s_nopol);
	            	 commonDao.updateNikWorksite(reg_spaj,s_update.trim());
	             }

	             line = br.readLine();
	        }
	        if(br!=null){
	        	br.close();
	        }			
		}catch( Exception e) { 
	         //logger.error("ERROR :", e);
			 String tes=e.getLocalizedMessage();
	         throw new RuntimeException(tes);
	    }
		finally{
			if(br!=null){
				br.close();
			}
			if(is!=null){
				is.close();
			}
		}				
        return "Berhasil Update Data ";
	}
	
	public Map selectRegion(String spaj) {		
		return this.uwDao.selectRegion(spaj);
	}
	
	public HashMap selectDataRKVA(String reg_spaj) {
		return Common.serializableMap(uwDao.selectDataRKVA(reg_spaj));
	}
	
	public ArrayList<HashMap> selectSpajPAPosisiPayment() {
		return Common.serializableList(uwDao.selectSpajPAPosisiPayment());
	}

	public void insertAgingFollowup(String polis, String pemegang,
			String beg_date, String end_date) {
		basDao.insertAgingFollowup(polis, pemegang, beg_date, end_date);
	}

	public Integer selectAgingFollowup(String polis, String beg_date, String end_date) {
		return basDao.selectAgingFollowup(polis, beg_date, end_date);
	}

	public List selectReportAgingFollowup(String a_beg_date, String a_end_date) {
		return basDao.selectReportAgingFollowup(a_beg_date, a_end_date);
	}

	public String prosesNpwAktif(User currentUser, String spaj,
			String keterangan, String lspdIdBefore, Integer proses) {	
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String success="BERHASIL";
		try{
			if(proses==1){
				List <Map> inbox = null, inbox_uw = null;
				inbox = Common.serializableList(uwDao.selectMstInbox(spaj, null));
				inbox_uw = Common.serializableList(uwDao.selectMstInbox(spaj, "1"));
				uwDao.updateMstPolicy(spaj, Integer.parseInt(lspdIdBefore));
				uwDao.updateMstPolicyLsspdId(spaj,10);
				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "SPAJ DIAKTIFKAN KEMBALI DENGAN ALASAN : "+keterangan, spaj, 0);
				if(inbox.size()>0){				

					if(!inbox_uw.isEmpty()){
						HashMap m= (HashMap) inbox_uw.get(0);					
						BigDecimal lspd_id = (BigDecimal) m.get("LSPD_ID_FROM");					 
						uwDao.prosesSnows(spaj, currentUser.getLus_id().toString(), lspd_id.intValue(), 212);
					}
				}
			}else{
				uwDao.updateMstPolicy(spaj, 999);
				uwDao.updateMstPolicyLsspdId(spaj, 29);
				uwDao.insertMstPositionSpaj("0", "AUTOPROSES UPDATE POSISI KE NOT PROCEED WITH : "+keterangan, spaj, 0);
				uwDao.prosesSnows(spaj, "0", 999, 212);
			}
		}catch (Exception e) {
			success="GAGAL MENGAKTIFKAN POLIS!";
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}

		return success;
	}

	public Map selectDataKuisionerCerdas(String reg_spaj) {
		// TODO Auto-generated method stub
		return uwDao.selectDataKuisionerCerdas(reg_spaj);
	}
	
	public ArrayList<HashMap> selectDaftarPolisBSMBelumPrint(String lus_id) {
		return Common.serializableList(bacDao.selectDaftarPolisBSMBelumPrint(lus_id));
	}
	
	public void schedulerTransferToFillingBSM(){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String msh_name = "SCHEDULER AUTOTRANSFER BSM DARI PRINTPOLIS TO FILLING";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerTransferToFillingBSM(msh_name);
		}	
	}
	
	/*Digunakan Oleh Snows*/
	public List<DropDown> selectLstJnJob() {		
		return snowsDao.selectLstJnJob();
	}
	
	public List selectMstPolicy(String polis, String spaj){
		return snowsDao.selectMstPolicy(polis, spaj);
	}
	
	public List selectMstInbox(String mi_id, String polis, Integer lus_id, String lde_id) {
		return snowsDao.selectMstInbox(mi_id, polis, lus_id, lde_id);
	}
	
	public List selectMstInboxChecklist(String mi_id, String mi_flag){
		return snowsDao.selectMstInboxChecklist(mi_id, mi_flag);
	}
	
	public String selectCheckPending(String spaj, String polis, String lstb_id){
		return snowsDao.selectCheckPending(spaj, polis, lstb_id);
	}
	public List selectNmFileChecklist(String mi_id, String lc_id){
		return snowsDao.selectNmFileChecklist(mi_id, lc_id);
	}
	
	public String selectCountChecklist(String mi_id, String mi_flag){
		return snowsDao.selectCountChecklist(mi_id, mi_flag);
	}
	
	public List selectListLeft(Integer lus_id, String lde_id){
		return snowsDao.selectListLeft(lus_id, lde_id);
	}
	
	public List selectMstTmms(String polis, String spaj){
		return snowsDao.selectMstTmms(polis, spaj);
	}
	
	public List selectLstChecklist(String ljj_id, String mi_id){
		return snowsDao.selectLstChecklist(ljj_id, mi_id);
	}
	
	public List<DropDown> selectLstDocumentPosition(){
		return snowsDao.selectLstDocumentPosition();
	}
    
    public void insertMstInbox( MstInbox mstInbox, MstInboxHist mstInboxHist ){
    	snowsDao.insertMstInbox( mstInbox );
    	snowsDao.insertMstInboxHist( mstInboxHist );
    }
    
    public void insertMstInboxHist( MstInboxHist mstInboxHist ){
    	snowsDao.insertMstInboxHist( mstInboxHist );
    }
    
    public void insertMstInboxChecklist( MstInboxChecklist mstInboxChecklist ){
    	snowsDao.insertMstInboxChecklist( mstInboxChecklist );
    }
    
    public String selectGenMIID(){
    	return snowsDao.selectGenMIID();
    }
    
    public void updateMstInbox(MstInbox mstInbox){
    	snowsDao.updateMstInbox(mstInbox);
    }
    
    public void updateMstInboxChecklist(MstInboxChecklist mstInbox){
    	snowsDao.updateMstInboxChecklist(mstInbox);
    }
	
	public List selectWithdrawStableLink(String spaj){
		return snowsDao.selectWithdrawStableLink(spaj);
	}
	
	public List selectWithdrawUnitLink(String spaj){
		return snowsDao.selectWithdrawUnitLink(spaj);
	}
	
	public List selectselectSwitchingUnitLink(String spaj){
		return snowsDao.selectselectSwitchingUnitLink(spaj);
	}
	
	public List selectEndorsmentRollover(String spaj){
		return snowsDao.selectEndorsmentRollover(spaj);
	}
	
	public List selectReturSLink(String spaj){
		return snowsDao.selectReturSLink(spaj);
	}
	
	public String selectJmlhTahapan(String spaj, String ljj_id){
		return snowsDao.selectJmlhTahapan(spaj, ljj_id);
	}
	
	public List selectJtTempoTahapan(String spaj, String ljj_id, String tggl){
		return snowsDao.selectJtTempoTahapan(spaj, ljj_id, tggl);
	}
	
	public List selectJtTempoTahapanMulti(String spaj, String ljj_id){
		return snowsDao.selectJtTempoTahapanMulti(spaj, ljj_id);
	}
	
	public List selectJtTempoSimpananMulti(String spaj, String ljj_id){
		return snowsDao.selectJtTempoSimpananMulti(spaj, ljj_id);
	}
	
	public List selectMutliInvest(String spaj){
		return snowsDao.selectMutliInvest(spaj);
	}
	
	public Date selectBegDateInsured(String spaj, String ljj_id){
		return snowsDao.selectBegDateInsured(spaj, ljj_id);
	}
	
	public String selectTmmsTHP(String spaj, String ljj_id){
		return snowsDao.selectTmmsTHP(spaj, ljj_id);
	}
	
	public List selectJtTempoTmmsTHP(String spaj, String ljj_id){
		return snowsDao.selectJtTempoTmmsTHP(spaj, ljj_id);
	}
	
	public List selectTmmsDet(String spaj){
		return snowsDao.selectTmmsDet(spaj);
	}
	
	public String selectJmlhSimpanan(String spaj, String ljj_id){
		return snowsDao.selectJmlhSimpanan(spaj, ljj_id);
	}
	
	public List selectWithdrawULSnows(String spaj){
		return snowsDao.selectWithdrawULSnows(spaj);
	}
	
	public List selectWithdrawULSnowsMuKe(String spaj, String ulke){
		return snowsDao.selectWithdrawULSnowsMuKe(spaj, ulke);
	}
	
	public List selectJtTempoSimpanan(String spaj, String ljj_id, String tggl){
		return snowsDao.selectJtTempoSimpanan(spaj, ljj_id, tggl);
	}
	
	public String selectJmlhMaturity(String spaj, String ljj_id){
		return snowsDao.selectJmlhMaturity(spaj, ljj_id);
	}
	
	public List selectJtTempoMaturity(String spaj, String ljj_id){
		return snowsDao.selectJtTempoMaturity(spaj, ljj_id);
	}
	
	public String selectTmmsMaturity(String spaj, String ljj_id){
		return snowsDao.selectTmmsMaturity(spaj, ljj_id);
	}
	
	public List selectJtTempoTmmsMaturity(String spaj, String ljj_id){
		return snowsDao.selectJtTempoTmmsMaturity(spaj, ljj_id);
	}
	
	public String selectJmlPencairanAllStableLink(String spaj){
		return snowsDao.selectJmlPencairanAllStableLink(spaj);
	}
	
	public String selectJmlNilaiTunaiMRI(String spaj, String ljj_id){
		return snowsDao.selectJmlNilaiTunaiMRI(spaj, ljj_id);
	}
	
	public List selectJtTempoMstSurrender(String spaj, String ljj_id){
		return snowsDao.selectJtTempoMstSurrender(spaj, ljj_id);
	}
	
	public List selectMstInboxHist(String mi_id){
		return snowsDao.selectMstInboxHist(mi_id);
	}
	
	public void schedulerNotifDashboard() {
		String msh_name = "SCHEDULER NOTIF DASHBOARD NB";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name) <= 0) {
			schedulerDao.schedulerNotifDashboard(msh_name);
		}
	}
	
	public void schedulerCountPolicyIssueHours() {
		String msh_name = "SCHEDULER COUNT POLICY ISSUE HOURS";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name) <= 0) {
			schedulerDao.schedulerCountPolicyIssueHours(msh_name);
		}
	}
	
	public List selectReportSLPolicyIssued(Map params) {
		return uwDao.selectReportSLPolicyIssued(params);
	}
	
	public List selectReportSnowsNB(Map params) {		
		return uwDao.selectReportSnowsNB(params);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public int kirimEmail(String spaj, Integer lssaId,String emailAdmin, String emailAgen,
			String statusAkhir, String tglAkhir, User currentUser,
			String deskripsi, String path, String msagId,
			Integer msagSertifikat, Date tglSertifikat, Date tglBegDate,
			Integer msagActive, String lcaId, String pemegang,
			String tertanggung, Command command, 
			BindException err, List<File> listAttachment)throws Exception {
		int ok = 1;
		boolean kirim=true;
		List lsProduk=akseptasiDao.selectMstProductInsured(spaj);
		Product produk=(Product)lsProduk.get(0);
		
		String css = props.getProperty("email.uw.css.satu")
				+ props.getProperty("email.uw.css.dua");
		String footer = props.getProperty("email.uw.footer");
		if(bacDao.selectIsInputanBank(spaj)==16 || (produk.getLsbs_id()==182 && "19,20,21".indexOf(produk.getLsdbs_number())>-1)) footer = props.getProperty("email.uw.syariah.footer");//Anta - Khusus Power Save Syariah BSM dan Multi Invest Syariah BSM
		DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
		
		Map info = uwDao.selectInformasiCabangFromSpaj(spaj);
		String cabang = (String) info.get("NAMA_CABANG");
		String kci =  (info.get("KCI")==null?null:(String)info.get("KCI"));
		String nama_ao = (info.get("NAMA_AO")==null?null:(String)info.get("NAMA_AO"));
		Map gen = uwDao.selectEmailAgen(command.getSpaj());
		String namaAgen = (String) gen.get("MCL_FIRST");
		
		String pesan = "";
		
		if(lssaId == 3 && command.getSubLiAksep() == 5){
			statusAkhir = "UPDATE FURTHER REQUIREMENT";
			pesan = css + 
				"<table width=100% class=satu>"
					+ "<tr><td colspan=3>Terima kasih atas update kelengkapan dokumen yang sudah disampaikan. Namun berdasarkan informasi further requirement, masih terdapat kekurangan untuk :</td></tr>"
					+ "<tr><td></td></tr>"
					+ "<tr><td>Cabang   	  </td><td>:</td><td>" + cabang + "</td></tr>"
					+ "<tr><td>Agen   		  </td><td>:</td><td>" + namaAgen + "</td></tr>"
					+ "<tr><td>No. Spaj 	  </td><td>:</td><td>" + FormatString.nomorPolis(spaj) + "</td></tr>"
					+ "<tr><td>Produk		  </td><td>:</td><td>" + produk.getLsdbs_name() + "("+produk.getLsbs_id() + ")" + "</td></tr>"
					+ "<tr><td>Pemegang Polis </td><td>:</td><td>" + pemegang + "</td></tr>"
					+ "<tr><td>Tertanggung	  </td><td>:</td><td>" + tertanggung + "</td></tr>"
					+ "<tr><td>UP             </td><td>:</td><td>" + produk.getLku_symbol() + " " + df.format(command.getMsprTsi()) +"</td></tr>"
					+ "<tr><td>Premi          </td><td>:</td><td>" + produk.getLku_symbol() + " " + df.format(command.getMsprPremium()) +"</td></tr>"
					+ "<tr><td>Kekurangan Dok </td><td>:</td><td>" + deskripsi + "</td></tr>"
					+ "<tr><td></td></tr>"
					+ "<tr><td colspan=3>Mohon untuk dapat melengkapi kekurangan dokumen tersebut agar pengajuan pertanggungan asuransi dapat kami teruskan ke UW Proses.<b>Kekurangan Dokumen Mohon diupload ke dalam sistem <b>.</td></tr>"
				+"</table>";
		}else{
			pesan = css + 
				"<table width=100% class=satu>"
					+ (cabang!=null	? 	"<tr><td>Cabang   	</td><td>:</td><td>" + cabang + "</td></tr>" : "")
					+ (kci!=null 	?	"<tr><td>KCI   	</td><td>:</td><td>" + kci + "</td></tr>" : "")
					+ "<tr><td>Agen   		</td><td>:</td><td>" + namaAgen + "</td></tr>"
					+ "<tr><td>Status   	</td><td>:</td><td>" + statusAkhir + "</td><td> Tanggal:" + tglAkhir + "</td></tr>"
					+ "<tr><td>Akseptor 	</td><td>:</td><td>" + currentUser.getName() + " [" + currentUser.getDept() + " ]<td></tr>" 
					+ "<tr><td>No. Spaj 	</td><td>:</td><td>" + FormatString.nomorPolis(spaj) + "<td></tr>"
					+ "<tr><td>Produk		</td><td>:</td><td>"+produk.getLsdbs_name()+"("+produk.getLsbs_id()+")"+"<td colspan=2></tr>" 
					+ "<tr><td>A/N			</td><td>:</td><td colspan=2>" + pemegang + " (Pemegang) -- " + tertanggung + " (Tertanggung) " + "</td></tr>" 
					+ "<tr><td>Keterangan	</td><td>:</td><td>" + deskripsi + "<td></tr>" 
					+ "<tr><td>UP</td><td>:</td><td>"+produk.getLku_symbol()+" "+ df.format(command.getMsprTsi()) +"</td></tr>"
					+ "<tr><td>Premi</td><td>:</td><td>"+produk.getLku_symbol()+" "+ df.format(command.getMsprPremium())+"</td></tr>"
					+ "<tr><td>Cara Bayar</td><td>:</td><td>"+command.getLscbPayMode()+"</td></tr>"
					+ (nama_ao!=null	?	"<tr><td>Nama AO   	</td><td>:</td><td>" + nama_ao + "</td></tr>" : "")
				+"</table>";
		}
		
		
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

		//Generate file PDF to server
		if(lssaId==2 || lssaId==4 || lssaId==9){ //decline,extra premi,postponed spaj 
			JasperUtils jasperUtils=new JasperUtils();
			String reportPath=null;
			String outputFile=null;
			Map<String, String> params=new HashMap<String, String>();
			params.put("nospaj", spaj);
			//decline dan postponed spaj 1 report file
			if(lssaId==2 ){//decline 
				dir="Decline";
				reportPath=props.get("report.uw.decline").toString()+".jasper";
				noSurat=getNoSurat(4, "01");
				params.put("flag", "0");
			}else if(lssaId==4){//extra premi /mortalita
				//deskripsi="";
				List lsExtra=uwDao.selectMstProductInsuredExtra(spaj);
				//bila extra > 1
				if(lsExtra.size() > 1) {
					double totalPremi = (double) 0;
					String premi=null;
					for(int y=0; y<lsExtra.size(); y++) {
						Product p = (Product) lsExtra.get(y);
						totalPremi += p.getMspr_premium();
						if(p.getLku_id().equals("01")) premi="Rp. ";
						else if (p.getLku_id().equals("02")) premi="US$ ";
					}
					JasperScriptlet jasper=new JasperScriptlet();
					//deskripsi="karena riwayat kesehatan.";
					params.put("premi", premi+jasper.format2Digit(new BigDecimal(totalPremi)));
					
				//bila extra = 1
				}else if(lsExtra.size() == 1){
					Product product=(Product)lsExtra.get(0);
					if(product.getLsbs_id()==900 || product.getLsbs_id()==903){//Extra Mortalita extra PC sekaligus
						//deskripsi="karena riwayat kesehatan.";
					}else if(product.getLsbs_id()==901){//Extra Job
						//deskripsi="karena resiko pekerjaan.";
					}else if(product.getLsbs_id()==902){//Extra PA
						//deskripsi="karena resiko kegiatan yang beresiko tinggi.";
					}else if(product.getLsbs_id()==904){//Extra Hamil
						//deskripsi="karena kehamilan.";
					}else if(product.getLsbs_id()==905){//Extra Mortalita Bayi
						//deskripsi="karena usia tertanggung kurang dari 6 bulan.";
					}
					JasperScriptlet jasper=new JasperScriptlet();
					String premi=null;
					if(product.getLku_id().equals("01"))
						premi="Rp. ";
					else if (product.getLku_id().equals("02"))
						premi="US$ ";
					params.put("premi", premi+jasper.format2Digit(new BigDecimal(product.getMspr_premium())));
				}else{
					err.reject("","Tidak Bisa Simpan Extra, Extra Belum di Input");
					kirim=false;
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}
				dir="Extra Premi";
				reportPath=props.get("report.uw.extra").toString()+".jasper";
				noSurat=getNoSurat(2, "01");
				params.put("flag", "2");
			}else if(lssaId==9){//postponed spaj
				dir="Postponed Spaj";
				reportPath=props.get("report.uw.decline").toString()+".jasper";
				noSurat=getNoSurat(3, "01");
				params.put("flag", "1");
			}
			params.put("periode",command.getPeriode());
			params.put("desc", deskripsi);
			params.put("no_surat", noSurat);
	
			outputFile="\\"+noSurat.replace("/", ".")+".pdf";
			outputDir=props.getProperty("pdf.dir.surat")+"\\"+lcaId+"\\"+dir;
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+lcaId+"\\"+spaj;
			
//				if(connection==null) {
//					try {
//						connection = this.uwDao.getDataSource().getConnection();
//					} catch (SQLException e) {
//						logger.error("ERROR :", e);
//					}
//				}
				if(kirim){
					Connection conn = null;
					try{
						conn  = this.uwDao.getDataSource().getConnection();
						JasperUtils.exportReportToPdf(reportPath, outputDir, outputFile, params, conn, PdfWriter.AllowPrinting, null, null);
						//simpan ke dalam folder polis
						JasperUtils.exportReportToPdf(reportPath, exportDirectory, outputFile, params, conn, PdfWriter.AllowPrinting, null, null);
					}catch (Exception  e) {
						this.uwDao.closeConn(conn);
			            throw e;
					}finally{
			            this.uwDao.closeConn(conn);
					}
				}
		}
		
		//
		if(lssaId == 5) {
			err.reject("", "Berhasil update menjadi ACCEPTED");
		}else if (ok < 4 && kirim){
			if ("09,58".indexOf(lcaId) == -1 ) {
				if (admin == true && agen == true) {
					if (command.getFlag_ut() == 0) {// jika kirim secara manual
													// (Tidak cek agen)
						agen = cekAgen(msagId, msagActive, msagSertifikat,
								tglSertifikat, tglBegDate, err);
						err.reject("", "Email Telah di kirim ke Admin");
					} else
						err.reject("", "Email Telah Berhasil di Kirim");
					ok = 1;
				} else if (admin == true && agen == false) {
					err.reject("", "Email Telah di kirim ke Admin");
					err.reject("","Email tidak dapat di kirim ke agen (Email agen tidak ada");
					command.setFlag_ut(1);
					ok = 2;
				} else if (admin == false && agen == true) {
					err.reject("","Email tidak dapat di kirim ke admin (Email tidak ada)");
					agen = cekAgen(msagId, msagActive, msagSertifikat,
							tglSertifikat, tglBegDate, err);
					command.setFlag_ut(2);
					ok = 3;
				} else if (admin == false && agen == false) {
					err.reject("","Email tidak dapat di kirim ke admin dan agen (Email Keduanya tidak ada)");
					command.setFlag_ut(3);
					ok = 4;
				}
			} else {// agen ada di kantor pusat (bancassurance)
				if(lcaId.equals("09")){
					err.reject("", "Email Telah Di kirim ke bagian bancassurance");
				}else{
					err.reject("", "Email Telah Di kirim ke bagian mallassurance");
				}
				
				ok = 1;
			}
			String subject=statusAkhir+" SPAJ No."+FormatString.nomorSPAJ(spaj)+" a/n "+pemegang+"/"+tertanggung;
			
			if(command.getUnduh()!=null){
				if(!command.getUnduh().isEmpty()) {
					//kalau ada upload file dokumen tambahan, save file nya
			        File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+lcaId+"\\"+spaj);
			        if(!userDir.exists()) userDir.mkdirs();
					
					File file1 = new File(props.getProperty("pdf.dir.export") + "\\" + lcaId + "\\" + spaj + "\\" + command.getUnduh().getOriginalFilename()); 
					command.getUnduh().transferTo(file1);
					command.setAttachment(file1);
				}
			}
			
			
			if(lssaId == 5) {
				
			}else if(lssaId==2 || lssaId==4 || lssaId==9){
				sendAgenNAdmin(elionsManager, spaj, noSurat.replace("/", "."), subject, pesan, emailAdmin, emailAgen,
						currentUser, path, footer, agen, lcaId, dir, command.getAttachment(), command.getSubLiAksep(), listAttachment);
			}else{
				if(lssaId==3){
					if(command.getSubLiAksep()!=null){
						if(command.getSubLiAksep()==4){
							emailAdmin+=";marvin@sinarmasmsiglife;makruf@sinarmasmsiglife.co.id;kuncoro@sinarmasmsiglife.co.id";
						}
					}
				}
				sendAgenNAdmin(elionsManager, spaj, "", subject, pesan, emailAdmin, emailAgen,
						currentUser, path, footer, agen, lcaId, dir, command.getAttachment(), command.getSubLiAksep(), listAttachment);
			}
		}else
			err.reject("", "silahkan Masukan Email dan kirim secara manual");

		return ok;
	}
	
	private void sendAgenNAdmin(final ElionsManager elionsManager, final String spaj, final String noSurat, final String subject, final String pesan, final String emailAdmin, final String emailAgen,
			final User currentUser, final String loc, final String footer, final boolean agen, final String lcaId, final String dir, final File attachment, final Integer sublssa_id, List<File> listAttachment) throws MessagingException{

	    List lsProduk = akseptasiDao.selectMstProductInsured(spaj);
	    Product produk = (Product) lsProduk.get(0);
	    Map regionMap = uwDao.selectRegion(spaj);
	    String lwk_id = (String) regionMap.get("LWK_ID");
	    String lsrg_id = (String) regionMap.get("LSRG_ID");
		String kode = akseptasiDao.selectMstProductInsured3(spaj);
		String from = currentUser.getEmail();
		String lsdbs_no = FormatString.rpad("0", produk.getLsdbs_number().toString(), 3);
//	    String further;
	    Integer i_jenisbank = bacDao.selectIsInputanBank(spaj);
	    HashMap mapEmail = new HashMap();
//	    boolean cc_banc = false;

/*	    
	    if(kode.equals("155")){//Permintaan Ariani
	    	further = props.getProperty("bancassuance.further_requirementspecial"); 
	    }else if(kode.equals("120") || kode.equals("134")){//Permintaan Ariani
	    	if(produk.getLsbs_id()==120 && "19,20,21".indexOf(produk.getLsdbs_number())>-1){//Andhika (23/05/2013)
	    		further = props.getProperty("bancassuance.further_requirement_cerdas_sekuritas");
	    	}else{
	    		further = props.getProperty("bancassuance.further_requirement_cerdas_platimum");
	    	}
	    }else if(kode.equals("166")){
	    	further = props.getProperty("bancassuance.further_requirement_amanah");
	    }else{
	    	further = props.getProperty("bancassuance.further_requirement");
	    }
	    
	    if(i_jenisbank==2) {
	    	ReffBii datautama = (ReffBii) bacDao.selectmst_reff_bii(spaj);
	    	further = props.getProperty("bancassuance.further_requirement_simasprima");
	    	// Andhika - request Sari (44415) - danamas prima
	    	if(produk.getLsbs_id()==163 && "6,7,8,9,10".indexOf(produk.getLsdbs_number())>-1){
	    		further = props.getProperty("bancassuance.further_requirement_danasejahtera");
	    	}
	    	//Request Hadi (No 41067) - SIMPOL kirim ke Hisar Janwar, Iriana Wijayanti, Shima, Edy Kohar
	    	if (kode.equals("120") && "19,20,21".indexOf(produk.getLsdbs_number())<1) further = "Iriana@sinarmasmsiglife.co.id;shima@sinarmasmsiglife.co.id";
	    	 
	    	//Email cabang Bank Sinarmas bersangkutan + email ao nya bila ada
	    	//Andhika (16/12/2013) - update emailAoHrd, untuk ambil email agent dengan joint ke table hrd_mst
	    	String emailCabangBank = uwDao.selectEmailCabangBankSinarmas(spaj);
	    	String emailAoBank = uwDao.selectEmailAoBankSinarmas(spaj);
	    	Map Aohrd = null;//uwDao.selectEmailAoHrd(spaj);
	    	Map emailBAC = uwDao.selectReffNonLisensi(spaj); 
	    	if (Aohrd != null){
	    		String emailAoHrd = (String) Aohrd.get("EMAIL");
				if(emailAoHrd != null) further = further.concat(";" + emailAoHrd);
			}
	    	if(emailCabangBank != null) further = further.concat(";" + emailCabangBank);
	    	//if(emailAoBank != null) further = further.concat(";" + emailAoBank);
	    	if(further.startsWith(";")) further = further.substring(1);
	    	if(emailBAC !=null){
	    		String emailBacAjs = (String) emailBAC.get("EMAIL_AJS");
	    		if(emailBacAjs != null) further = further.concat(";" + emailBacAjs);
	    	}
	    	HashMap kanwil911 = this.selectMstConfig(6, "kanwil911", "kanwil_911");
	    	if(kanwil911.get("NAME").toString().indexOf(datautama.getLcb_no())>-1){// BSiM Kanwil 9 dan 11 
	    		further = further.concat(";" + "yuddy@sinarmasmsiglife.co.id ");
	    	}
	    	HashMap kanwil7 = this.selectMstConfig(6, "kanwil7", "kanwil_7");
	    	if(kanwil7.get("NAME").toString().indexOf(datautama.getLcb_no())>-1){// kanwil 7 BSiM
	    		further = further.concat(";" + "patdes@sinarmasmsiglife.co.id;hennys@sinarmasmsiglife.co.id;");
	    	}
	    	
	    	//Ridhaal (15/8/2016) Request Sari Sutini (No 92747) - Edit user2 penerima email gagal validasi, extra premi, postponed, dan decline produk Simpol (120) dan Simpol Syariah (220) sesuai dengan data mapping bancass yang terdapat pada sistem distribution support.
	    	if ((kode.equals("120") && "10,11,12,22,23,24".indexOf(produk.getLsdbs_number().toString())>-1 ) ||
	    			(kode.equals("202") && "4,5,6".indexOf(produk.getLsdbs_number().toString())>-1 ) ||
	    			(kode.equals("213") && "1".indexOf(produk.getLsdbs_number().toString())>-1 ) ||
	    			(kode.equals("216") && "1".indexOf(produk.getLsdbs_number().toString())>-1 ) ||
	    			(kode.equals("134") && "5".indexOf(produk.getLsdbs_number().toString())>-1 ) ||
	    			(kode.equals("215") && "1".indexOf(produk.getLsdbs_number().toString())>-1 )){
	    		Map emailBCAM = uwDao.selectEmailBCAM(spaj);
	    		if(emailBCAM !=null){
		    		String emailBC = (String) emailBCAM.get("AO_EMAIL");
		    		String emailAM = (String) emailBCAM.get("LEADER_EMAIL");
		    		
		    		if(emailBC != null) further = further.concat(";" + emailBC);
		    		if(emailAM != null) further = further.concat(";" + emailAM);
		    		cc_banc = true;
		    	}
	    	}
	    	 
	    }else if(i_jenisbank==3) {
	    	if(produk.getLsbs_id()==120 && "19,20,21".indexOf(produk.getLsdbs_number())>-1){// Andhika (23/05/2013)
	    		further = props.getProperty("bancassuance.further_requirement_cerdas_sekuritas");
	    	}else{
	    		further = props.getProperty("bancassuance.further_requirement_danamasprima");
	    	}
	    }else if(i_jenisbank==16 || (produk.getLsbs_id()==182 && "19,20,21".indexOf(produk.getLsdbs_number())>-1)) {
	    	further = props.getProperty("bancassuance.further_requirement_bsm");
	    }else if(i_jenisbank==25 || (produk.getLsbs_id()==200 && "5,6".indexOf(produk.getLsdbs_number().toString())>-1)) {// Ridhaal untuk bank Harda / produk superlink - Req Dewi Andriyati (10/20/2016)
    		mapEmail=Common.serializableMap(uwDao.selectMstConfig(6, "sendAgenNAdmin","bancass.harda"));
			further = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
			
			Map emailBCAM = uwDao.selectEmailBCAM(spaj);
    		if(emailBCAM !=null){
	    		String emailBC = (String) emailBCAM.get("AO_EMAIL");
	    		String emailAM = (String) emailBCAM.get("LEADER_EMAIL");
	    		
	    		if(emailBC != null) further = further.concat(";" + emailBC);
	    		if(emailAM != null) further = further.concat(";" + emailAM);
	    	}
	    }
*/	    
	    
	    String to[] = null;
	    String cc[] = null;
	    String bcc[] = null;
	    
/*	    
	    String bancassurance[] = further.split(";");
	    String AdminNBancassurance[] = (emailAdmin+";"+further).split(";");
	    
	    if(emailAdmin!=null && !emailAdmin.trim().equals("")){
	    	String[] emailTo = emailAdmin.split(";");
	    	if(lcaId.equals("09")){
	    		to = AdminNBancassurance;
	    	}else{
	    		to = emailTo;
	    	}
	    }else if(lcaId.equals("09")){
	    	if(emailAdmin!=null){
	    		to = AdminNBancassurance;
	    	}else{
	    		to = bancassurance;
	    	}
	    }else if(lcaId.equals("58")){
	    	String adminMall = props.getProperty("bas.email.mallassurance");
	    	String leaderMall = props.getProperty("leader.email.mallassurance");
	    	String[] emailTo = adminMall.split(";");
	    	String[] emailCc = leaderMall.split(";");
	    	to = emailTo;
	    	cc = emailCc;
	    }
	    
	    if(lcaId.equals("09")){//DEDDY - 24 Jul 2012 - REQ HENDRY : UNTUK SEMUA TUTUPAN BANCASS, TIDAK PERLU DIKIRIM KE EMAIL AGENT TUTUPAN YG DIINPUT. UNTUK MENCEGAH KEBOCORAN DATA.
			if(produk.getLsbs_id()==163 && "6,7,8,9,10".indexOf(produk.getLsdbs_number())>-1){
				String ds = props.getProperty("bancassuance.further_requirement_danasejahtera");
		    	String[] emailCc = ds.split(";");
				cc = emailCc;
			}else if (kode.equals("120") && "19,20,21".indexOf(produk.getLsdbs_number())<0){
				mapEmail=Common.serializableMap(uwDao.selectMstConfig(6, "sendAgenNAdmin","uw.helpdesk.bancass"));
				String ccUW =mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
				String[] emailCc = ccUW.split(";");
				cc = emailCc;
			}else if (kode.equals("190") && "5,6".indexOf(produk.getLsdbs_number().toString())>=0){
				mapEmail=Common.serializableMap(uwDao.selectMstConfig(6, "sendAgenNAdmin","bancass.vipplan"));
				String toBancass = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
				String emailAo = uwDao.selectEmailAoBankSinarmas(spaj);
				if(emailAo!=null)toBancass+=emailAo+";";
				String[] emailTo = toBancass.split(";");
				String[] emailCC = (currentUser.getEmail()+";").split(";");
				to = emailTo;
				cc = emailCC;
			}else if (kode.equals("200") && "5,6".indexOf(produk.getLsdbs_number().toString())>=0){
				mapEmail=Common.serializableMap(uwDao.selectMstConfig(6, "sendAgenNAdmin","bancass.harda"));
				String ccUW =mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
			//	String toBancass = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
			//	ccUW+=currentUser.getEmail()+";";
			//	String[] emailTo = toBancass.split(";");
			//	to = emailTo;	
				
				String emailUser = currentUser.getEmail();
				if(emailUser == null) emailUser = "";
				ccUW = ccUW.concat(";" + emailUser);
				String[] emailCc = ccUW.split(";");			
				cc = emailCc;
			}else{
				cc = null;
			}
		}
*/
	    
		/** MANTA (25-08-2014) - Helpdesk No.54817, Req Sari Sutini
		  * Update Email Tujuan dan Email Cc
		  * Untuk Jalur Dist. Agency, Bancass (BSIM), Bancass (Sekuritas), Bancass 1, Corporate
		  * DMTM (Roxy, BSIM, ARCO, SMP, SSS, SIP, RedBerry), dan EFC
		**/
		
		String s_DaftarEmailTo = "";
		String s_DaftarEmailCc = "";
		
		//Jalur Dist. Agency
		if("37,39,49,52,60,73".indexOf(lcaId)>-1){
			String emailCabangPenutup = uwDao.selectEmailCabangPenutup(spaj,1);
			s_DaftarEmailTo = emailAdmin;
			s_DaftarEmailCc = "uwhelpdeskAGENCY@sinarmasmsiglife.co.id;iway@sinarmasmsiglife.co.id;Yudi@sinarmasmsiglife.co.id;";
			if(emailAgen!=null && !emailAgen.trim().equals("")){
				s_DaftarEmailCc = s_DaftarEmailCc + emailAgen;
			}
			if(emailCabangPenutup != null && !emailCabangPenutup.trim().equals("")){
				s_DaftarEmailCc = s_DaftarEmailCc + ";"+ emailCabangPenutup;
			}

			if(lcaId.equals("73")){//ERBE
				s_DaftarEmailTo = "efesty@erbecorp.com;";
				bcc  = (from+";"+"ridhaal@sinarmasmsiglife.co.id;").split(";");
			}
			
		}
		//Jalur Dist. Bancassurance
		else if(lcaId.equals("09")){
			//Sekuritas
			if(i_jenisbank==3){
		    	if(produk.getLsbs_id()==120 && "019,020,021".indexOf(lsdbs_no)>-1){
		    		s_DaftarEmailTo = props.getProperty("bancassuance.further_requirement_cerdas_sekuritas");
		    	}else{
		    		s_DaftarEmailTo = props.getProperty("bancassuance.further_requirement_danamasprima");
		    	}
		    }
			//BSM Syariah
			else if(i_jenisbank==16 || (produk.getLsbs_id()==182 && "019,020,021".indexOf(lsdbs_no)>-1)) {
				s_DaftarEmailTo = props.getProperty("bancassuance.further_requirement_bsm");
		    }
			else{
				HashMap mapBCAM = selectEmailBCAM2(spaj);
				if(mapBCAM != null){
					s_DaftarEmailTo = mapBCAM.get("EMAIL_BC")!=null? mapBCAM.get("EMAIL_BC").toString():"";
					s_DaftarEmailCc = mapBCAM.get("EMAIL_AM")!=null? mapBCAM.get("EMAIL_AM").toString():"";
				}
		    }
			
//			s_DaftarEmailCc = props.getProperty("bancassurance.further_requirement_cc");
//			String s_team = uwDao.selectBancassTeam(spaj);
//			if(s_team == null) s_team = "";
//			
//			if(i_jenisbank == 2){
//				if(s_team.toUpperCase().equals("TEAM YANTI SUMIRKAN")) {//Bancass 1
//					s_DaftarEmailTo = "";
//					s_DaftarEmailCc = s_DaftarEmailCc + "Support.Bancass1@sinarmasmsiglife.co.id";
//				}
//			}else if(i_jenisbank == 3){//Sekuritas
//				if((produk.getLsbs_id()==120 && "19,20,21".indexOf(produk.getLsdbs_number())>-1) || (produk.getLsbs_id()==142 && produk.getLsdbs_number()==9) ||
//				   (produk.getLsbs_id()==158 && produk.getLsdbs_number()==14) || (produk.getLsbs_id()==188 && produk.getLsdbs_number()==8)){
//					s_DaftarEmailTo = "";
//				}
//			}
			
		}
		//Jalur Dist. Corporate
		else if("08,42,62,67".indexOf(lcaId)>-1){
			if(lcaId.equals("42")){
				s_DaftarEmailTo = "CorpMarketing@sinarmasmsiglife.co.id;CorpMkt2@sinarmasmsiglife.co.id;";
			}else{
				s_DaftarEmailTo = "CorpMarketing@sinarmasmsiglife.co.id;";
			}
			s_DaftarEmailCc = "uwhelpdeskCORPORATE@sinarmasmsiglife.co.id;Asti@sinarmasmsiglife.co.id;";
		}
		//Jalur Dist. DM/TM
		else if(lcaId.equals("40")){
			s_DaftarEmailTo = emailAdmin;
			s_DaftarEmailCc = props.getProperty("DMTM.further_requirement_cc");
			if(lwk_id.equals("01")){
				if(lsrg_id.equals("00")){//Roxy
					s_DaftarEmailTo = "DMTM@sinarmasmsiglife.co.id;";
					s_DaftarEmailCc = s_DaftarEmailCc + ";"+"Maria_P@sinarmasmsiglife.co.id";
				}else if(lsrg_id.equals("02")){//BSIM
					if(produk.getLsbs_id().equals("163")){
						s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_bsim");
					}
				}
			}else if(lwk_id.equals("02")){
				if(lsrg_id.equals("00")){//ARCO
					s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_arco");
				}else if(lsrg_id.equals("01")){//SMP
					s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_smp");
				}else if(lsrg_id.equals("02")){//SSS
					s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_sss");
				}else if(lsrg_id.equals("03")){//SIP
					s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_sip");
				}else if(lsrg_id.equals("04")){//RedBerry
					s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_redberry");
					s_DaftarEmailCc = s_DaftarEmailCc + ";"+"nengerna88fatmawati@gmail.com;eric@genseas.co.id;p.haromunthe@redberry.co.id;t.djafry@redberrycc.com;ayda5758@gmail.com;maria_surjadi@yahoo.com";
				}else if(lsrg_id.equals("05")){//PSJS
					s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_psjs");
				}else if(lsrg_id.equals("07")){//DENA
					s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_dena");
				}else if(lsrg_id.equals("09")){//MARZ
					s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_marz");
				}else if(lsrg_id.equals("12")){//SYNERGYS
					s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_synergys");
				}else if(lsrg_id.equals("13")){//Seven Stars Indonesia SSI
					s_DaftarEmailTo = props.getProperty("DMTM.further_requirement_to_sevenstarindo");
				}
			}
	    }
		//Jalur Dist. EFC
		else if(lcaId.equals("65")){
			s_DaftarEmailTo = emailAgen;
			s_DaftarEmailCc = props.getProperty("FCD.further_requirement_cc");
			
			Map agen2 = uwDao.selectEmailAgen(spaj);
			String msagId = (String) agen2.get("MSAG_ID");
			Map emailHybrid = uwDao.selectEmailHybrid(msagId);
			String emailSiHybrid = "";
			if(emailHybrid != null){
				emailSiHybrid = (String) emailHybrid.get("EMAIL");
			}
			s_DaftarEmailTo = s_DaftarEmailTo + ";" + emailSiHybrid;
		}
		else if(lcaId.equals("37") && lwk_id.equals("D6") && lsrg_id.equals("00")){
			s_DaftarEmailTo = s_DaftarEmailTo+";"+"agustinaw@sinarmasmsiglife.co.id;";
		}
		
		if(!s_DaftarEmailTo.equals("")){
			String[] emailTo = s_DaftarEmailTo.split(";");
			to = emailTo;
		}
		
		if(!s_DaftarEmailCc.equals("")){
			int lssa_id = uwDao.selectStatusAksep(spaj);
			if(emailAgen != null && !emailAgen.trim().equals("") && lssa_id == 3){
				s_DaftarEmailCc = s_DaftarEmailCc + ";" +props.getProperty("admin.BAP");
				
				if(sublssa_id != null){
					if(sublssa_id == 5) s_DaftarEmailCc = "bas@sinarmasmsiglife.co.id;uwhelpdesk@sinarmasmsiglife.co.id";
				}
			}
			String[] emailCc = s_DaftarEmailCc.split(";");
			cc = emailCc;
		}
		
		//Produk Bancassurance
		if(lcaId.equals("09")){
			bcc = (from+";"+"qabas@sinarmasmsiglife.co.id;").split(";");
		}
		//Produk Agency
		else if("37,52".indexOf(lcaId)>-1){
			bcc = (from+";"+props.getProperty("rmc.ani")+";"+props.getProperty("rmc.siti")+";").split(";");
		}
		//Worksite (Request Novie)
		else if(lcaId.equals("42")){
			bcc = from.split(";");
		}
		//Regional
		else if("37,09,58,01,42,52,65".indexOf(lcaId) == -1){
			bcc = (from+";"+props.getProperty("rmc.ani")+";"+props.getProperty("rmc.siti")+";").split(";");
		}
		
		List<File> ListAttachment = new ArrayList<File>();
		File sourceFile = new File(props.getProperty("pdf.dir.surat")+"\\"+lcaId+"\\"+dir+"\\"+noSurat+".pdf");
		if(sourceFile.isFile()) {
			ListAttachment.add(sourceFile);
		}
		if(attachment != null) {
			if(attachment.exists()) {
				ListAttachment.add(attachment);
			}
		}
		if(listAttachment != null) {
			for(File att : listAttachment) {
				if(att.exists()) {
					ListAttachment.add(att);
				}
			}
		}
		
//		if(cc_banc){
//			String emailUser = currentUser.getEmail();
//			if(emailUser == null) emailUser = "";
//			cc = (emailUser+";windi_wulan@sinarmasmsiglife.co.id;nuraini@sinarmasmsiglife.co.id;mega@sinarmasmsiglife.co.id;lusi_susanti@sinarmasmsiglife.co.id;helmy@sinarmasmsiglife.co.id;yudi@sinarmasmsiglife.co.id").split(";");
//		}
		
		EmailPool.send("E-Lions", 1, 0, 0, 0, 
				null, 0, 0, commonDao.selectSysdate(), null, 
	    		true, from, 
	    		to, cc, bcc, 
//	    		new String[]{"randy@sinarmasmsiglife.co.id"},null,null,
//	    		new String[]{"deddy@sinarmasmsiglife.co.id"},null,null,
	    		subject, pesan+footer, 
	    		ListAttachment, spaj);
	}
	
	/**Fungsi:	Untuk membuat no-no surat seperti Surat Extra, Decline, Postponed Spaj.
	 * @param Integer mscoNum, String id
	 * 			mscoNum = 2 = Extra Mortalita/premi;
	 * 					  3 = Postponed Spaj.
	 * 					  4 = Decline.		
	 * @return String	
	 */
	private String getNoSurat(Integer mscoNum,String id){
		String noSurat;
		String ket;
		if(mscoNum==2){//Extra Mortalita/Premi
			ket="/EM/UND/";
		}else if(mscoNum==3){//Postponded SPaj
			ket="/PP/UND/";
		}else if(mscoNum==4){//Decline
			ket="/DEC/UND/";
		}else 
			return null;
		//0=reset counter per bulan
		//1=tidak reset counter per bulan
		Integer flag_Counter=financeDao.selectCekCounterTtsMonthAndYear(mscoNum, id);
		if(flag_Counter==0){
			//update eka.mst_counter_tts
			financeDao.updateResetMstCounterTts(mscoNum,id);
		}
		Map mapCounter=financeDao.selectGetCounterTts(mscoNum,id);
		String formatCounter=(String)mapCounter.get("FORMAT_COUNTER");
		BigDecimal counter=(BigDecimal)mapCounter.get("COUNTER");
		Date today=commonDao.selectSysdate();
		financeDao.updateMstCounterTts(new Double(counter.intValue()),mscoNum,id);
		Date sysdate=commonDao.selectSysdate();
		String mm=FormatDate.getMonth(sysdate);
		String yy=FormatDate.getYearFourDigit(sysdate);
		noSurat=formatCounter+ket+mm+"/"+yy;
		return noSurat;
	}
	
	private boolean cekAgen(String msagId, Integer msagActive,
			Integer msagSertifikat, Date tglSertifikat, Date tglBegDate,
			BindException err) {
		boolean agen = true;

		if (msagActive.intValue() == 1) {
			form_agen formAgen = new form_agen();
			String hsl = formAgen.sertifikasi_agen(msagId, 0, msagSertifikat
					.intValue(), tglSertifikat, tglBegDate);
			if (hsl.equals(""))
				err.reject("", "Email Berhasil di kirim ke agen");
			else {
				err
						.reject("",
								"Agen Tidak Berlisensi, Email tidak dapat di kirim ke agen");
				agen = false;
			}
		} else {
			err.reject("",
					"Agen Tidak Aktif, Email Tidak dapat di kirim ke agen");
			agen = false;
		}
		return agen;
	}
	
	public Double selectTotalPremiNewBusiness(String spaj) {
		return uwDao.selectTotalPremiNewBusiness(spaj);
	}
	
	public Integer selectCountMstBillingNB(String spaj)  {
		return uwDao.selectCountMstBillingNB(spaj);
	}
	
	public Integer selectCountMstBillingSucc(String spaj)  {
		return uwDao.selectCountMstBillingSucc(spaj);
	}
	
	/**
	 * req : Hayatin (02/09/2015)
	 * @author 	Lufi
	 */
	public void schedulerReportKycNewBussiness(){
		String msh_name = "SCHEDULER REPORT KYC NEW BUSSINESS";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerReportKycNewBussiness(msh_name);
		}
	}
	
	public void schedulerReportPep(){
		String msh_name = "SCHEDULER REPORT PEP";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerReportPep(msh_name);
		}
	}
	
	public List selectReportDataBackToBas(String bdate, String edate, String jenis, Integer jn_transfer, String lus_id) {
		return basDao.selectReportDataBackToBas(bdate, edate, jenis, jn_transfer,lus_id);
	}
	
	
	public List selectStokSPAJ(String jenis, Integer lsjs_id) {
		return Common.serializableList(basDao.selectStokSpaj(jenis,lsjs_id));
	}
	
	public List selectStokSPAJSummary(String b_date, String e_date, Integer filter) {
		return Common.serializableList(basDao.selectStokSPAJSummary(b_date,e_date,filter));
	}
	
	public void updateStokSpajUw(FormSpaj formspaj) {
		basDao.updateStokSpajUw(formspaj);
	}
	public void insertSpajHist(FormSpaj formspaj) {
		basDao.insertSpajHist(formspaj);
		basDao.updatePenguranganSpajUw(formspaj);
	}
	
	public List selectReportSlaUwIndividu(Map params) {		
		return uwDao.selectReportSlaUwIndividu(params);
	}
	/**
	 * Scheduler sisa stok virtual account (19/09/2015)
	 * @author 	Ryan
	 */
	public void schedulerStockSPAJ(){
		String msh_name = "AUTOMAIL SISA STOCK SPAJ";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerStockSPAJ(msh_name);
		}
	}
	
	public int updateMst_policyEmptyPrintDate(String spaj) {		
		return uwDao.updateMst_policyEmptyPrintDate(spaj);
	}


//	public void kirimEmailSoftcopyClickForlife(String spaj) {
//		// TODO Auto-generated method stub
//		
//	}
    
	public List<Linkdetail> jenisTransaksiListTitipanPremi( String spaj, Integer premike ){
		List<Linkdetail> result = new ArrayList<Linkdetail>();
		List<Linkdetail> tempresult = new ArrayList<Linkdetail>();
		Integer isUlink = uwDao.isUlinkBasedSpajNo( spaj );
		if(isUlink != null && isUlink > 0){
			tempresult = uwDao.uLinkDescrAndDetail(spaj);
		}else{
			Integer isSlink = uwDao.isSlinkBasedSpajNo( spaj );
			if(isSlink != null && isSlink > 0){
				tempresult = uwDao.sLinkDescrAndDetail(spaj);
			}else{
				tempresult = null;
			}
		}
		if(tempresult != null && tempresult.size() > 0){
			for(int i=0; i<tempresult.size(); i++){
				if("Alokasi Investasi".equals(tempresult.get(i).getDescr())){
					tempresult.get(i).setDescr("Premi Pokok");
				}
				if((premike == null) || (premike == tempresult.get(i).getPremiKe().intValue()) && tempresult.get(i).getPremiKe().intValue() != 0){
					result.add(tempresult.get(i));
				}
			}
		}
		return result;
	}
	
	public void insertSmsServerOutWithGateWay(Smsserver_out sms_out, boolean default_param) {
		basDao.insertSmsServerOutWithGateway(sms_out, default_param);
	}
	
	public String selectTotalSmsserver_out(String beg_date, String end_date, String reg_spaj,String no_polis,Integer id,Integer lus_id, Map params){
		return bacDao.selectTotalSmsserver_out(beg_date, end_date, reg_spaj, no_polis,id,lus_id, params);
	}
  	 
  	public List<Smsserver_in> selectSmsserver_out(String beg_date, String end_date, String reg_spaj,String no_polis,Integer id,Integer lus_id, Map params){
  		return bacDao.selectSmsserver_out(beg_date, end_date, reg_spaj, no_polis,id,lus_id, params);
    }
	
	public String selectTotalResultSmsserver_out(String beg_date, String end_date, String reg_spaj,String no_polis,Integer id,Integer lus_id, Map params){
		return bacDao.selectTotalResultSmsserver_out(beg_date, end_date, reg_spaj, no_polis,id,lus_id, params);
	}
  	
  	public String processValidasiDataBMaterai(String [] spaj, User currentUser, HttpServletRequest request) throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String cabang ="";String pic="";String norek="";
		String sukses="";
		try{
			Integer jmlSpaj =spaj.length;
			Double nominal = (double) (jmlSpaj.intValue()*6000);
			/** 1. insert mst_stamp_fee */
			List result =selectCabangSinarmasAll(currentUser.getJn_bank().toString(),currentUser.getCab_bank().trim());
			if (!result.isEmpty()){
				Map getData = (HashMap) result.get(0);
				cabang=(String) getData.get("NAMA_CABANG");
				pic=(String) getData.get("NAMA_REK_PIC");
				norek=(String) getData.get("NO_REK_PIC");
			}
			Date nowDate = commonDao.selectSysdate();
			DateFormat df = new SimpleDateFormat("yyyyMM");
			String periode =df.format(nowDate);
			//Ambil No Counter Proses
			String cp=sequence.sequenceNoAntrian("");

			//set Ke model
			Stamp stamp = new Stamp();
			stamp.setMsf_no(cp);
			stamp.setMsf_lcb_no(currentUser.getCab_bank());
			stamp.setMsf_cabang(cabang);
			stamp.setMsf_pic(pic);
			stamp.setMsf_norek(norek);
			stamp.setMsf_nominal(nominal);
			stamp.setMsf_posisi(0);
			stamp.setMsf_lusid(Integer.parseInt(currentUser.getLus_id()));
			stamp.setMsf_date(periode);
			stamp.setMsf_date_validasi(nowDate);
			snowsDao.insertMstStampFee (stamp);
			/** 2. insert mst_stamp_det */
			for(int i=0;i<spaj.length;i++){
				stamp.setReg_spaj(spaj[i]);
				snowsDao.insertMstStampDet (stamp);
			}
			sukses="Data Berhasi Di Simpan ";
		}catch (Exception e) {
			sukses="Data Tidak Berhasi Di Simpan ";
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			logger.error("ERROR :", e);
		}
		return sukses;
	}
  	
	public List selectDataBmaterai(String periode , String periode2 , String status, String jenis, String lcb_no, String filter){
		return Common.serializableList(snowsDao.selectDataBmaterai( periode ,periode2 , status, jenis, lcb_no,filter));
	}
	
	public List selectDataBmateraiDet(String no , String lcb_no){
		return Common.serializableList(snowsDao.selectDataBmateraiDet( no , lcb_no));
	}
	
	public String processBayarDataBMaterai(String [] data, User currentUser, HttpServletRequest request, Integer flag_proses, String date, String date2) throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		/**
		 * Flag Proses :  1&3. Kirim / Kirim Susulan Data Ke Finance , 2. Update tolakan / Sukses 
		 */
		String sukses="";String subject="";
		String outputFilename="";String outputFilename2="";String outputFilename3="";String outputFilename4="";
		try{
			if(flag_proses==1 || flag_proses==3 ){
				//Untuk Proses Kirim
				if (flag_proses==1){
					for(int i=0;i<data.length;i++){
						snowsDao.updatePosisiMstSFee(data[i], "1", "MSF_DATE_SEND" ,commonDao.selectSysdate());
					}
				}
				//set nama file secara global 
				if (flag_proses==1){
					outputFilename = "permohonan_reimburse_materai_" +date+ ".xls";
					outputFilename2 = "permohonan_reimburse_materai_syh_" +date+ ".xls";
					outputFilename3 = "upload_" +date+ ".xls";
					outputFilename4 = "upload_syh_" +date+ ".xls";
					subject="[E-Lions] Permohonan Pembayaran Penggantian Biaya Materai Periode"+date;
				}else if (flag_proses==3){
					outputFilename = "permohonan_reimburse_materai_susulan" +date+ ".xls";
					outputFilename2 = "permohonan_reimburse_materai_syh_susulan" +date+ ".xls";
					outputFilename3 = "upload_susulan_" +date+ ".xls";
					outputFilename4 = "upload_syh_susulan_" +date+ ".xls";
					subject="[E-Lions] Permohonan Pembayaran Penggantian Biaya Susulan Materai Periode"+date;
				}
				List<File> attachments = new ArrayList<File>();
				String outputDir = props.getProperty("pdf.dir.report") + "\\reimburse_materai\\";
				//Generate File Konven
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				params.put("periode", date);
				params.put("jenis","BANK SINARMAS");
				List result=selectDataBmaterai(date,date2,flag_proses.toString(),"2",null,null);
				JasperUtils.exportReportToXls(props.getProperty("report.uw.biayaMaterai") + ".jasper", 
						outputDir, outputFilename, params, result, null);

				File sourceFile = new File(outputDir+"\\"+outputFilename);
				attachments.add(sourceFile);

				//Generate File Syariah
				String outputDir2 = props.getProperty("pdf.dir.report") + "\\reimburse_materai\\";
				Map<String, Comparable> params2 = new HashMap<String, Comparable>();
				params2.put("periode", date);
				params2.put("jenis","BANK SINARMAS SYARIAH");
				List result2=selectDataBmaterai(date,date2,flag_proses.toString(),"16",null,null);
				JasperUtils.exportReportToXls(props.getProperty("report.uw.biayaMaterai") + ".jasper", 
						outputDir2, outputFilename2, params2, result2, null);
				File sourceFile2 = new File(outputDir2+"\\"+outputFilename2);
				attachments.add(sourceFile2);


				//Data Finance Konven
				String outputDir3 = props.getProperty("pdf.dir.report") + "\\reimburse_materai\\";
				outputFilename3 = "upload_" +date+ ".xls";
				String filename = outputDir3+outputFilename3;
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet("FirstSheet");  

				HSSFRow rowhead = sheet.createRow((short)0);
				rowhead.createCell((short) 0).setCellValue("NO");
				rowhead.createCell((short) 1).setCellValue("EMPLOYEE NAME");
				rowhead.createCell((short) 2).setCellValue("CURRENCY");
				rowhead.createCell((short) 3).setCellValue("ACCOUNT NO");
				rowhead.createCell((short) 4).setCellValue("AMOUNT");

				List result3=selectDataBmaterai(date,date2,flag_proses.toString(),"2",null,null);
				for(int i=0;i<result3.size();i++){
					Map getData = (HashMap) result3.get(i);
					String norek=(String) getData.get("MSF_NOREK");
					BigDecimal nominal=(BigDecimal) getData.get("MSF_NOMINAL");
					HSSFRow row = sheet.createRow((short)i+1);
					row.createCell((short) 0).setCellValue(i+1);
					row.createCell((short) 1).setCellValue((String) getData.get("MSF_PIC"));
					row.createCell((short) 2).setCellValue("IDR");
					row.createCell((short) 3).setCellValue(norek);
					row.createCell((short) 4).setCellValue(nominal.toString());

				}
				FileOutputStream fileOut = new FileOutputStream(filename);
				workbook.write(fileOut);
				fileOut.close();
				File sourceFile3 = new File(outputDir3+"\\"+outputFilename3);
				attachments.add(sourceFile3);

				//Data Finance Syariah
				String outputDir4 = props.getProperty("pdf.dir.report") + "\\reimburse_materai\\";
				outputFilename4 = "upload_syh_" +date+ ".xls";
				String filenamesyh = outputDir4+outputFilename4;
				HSSFWorkbook workbook2 = new HSSFWorkbook();
				HSSFSheet sheet2 = workbook2.createSheet("FirstSheet");  

				HSSFRow rowhead2 = sheet2.createRow((short)0);
				rowhead2.createCell((short) 0).setCellValue("NO");
				rowhead2.createCell((short) 1).setCellValue("EMPLOYEE NAME");
				rowhead2.createCell((short) 2).setCellValue("CURRENCY");
				rowhead2.createCell((short) 3).setCellValue("ACCOUNT NO");
				rowhead2.createCell((short) 4).setCellValue("AMOUNT");

				List result4=selectDataBmaterai(date,date2,flag_proses.toString(),"16",null,null);
				for(int i=0;i<result4.size();i++){
					Map getData = (HashMap) result4.get(i);
					String norek=(String) getData.get("MSF_NOREK");
					BigDecimal nominal=(BigDecimal) getData.get("MSF_NOMINAL");
					HSSFRow row2 = sheet2.createRow((short)i+1);
					row2.createCell((short) 0).setCellValue(i+1);
					row2.createCell((short) 1).setCellValue((String) getData.get("MSF_PIC"));
					row2.createCell((short) 2).setCellValue("IDR");
					row2.createCell((short) 3).setCellValue(norek);
					row2.createCell((short) 4).setCellValue(nominal.toString());

				}
				FileOutputStream fileOut2 = new FileOutputStream(filenamesyh);
				workbook2.write(fileOut2);
				fileOut2.close();
				File sourceFile4 = new File(outputDir4+"\\"+outputFilename4);
				attachments.add(sourceFile4);

				HashMap mapEmail = this.selectMstConfig(6, "email_biaya_materai", "email_biaya_materai");
				String emailto = mapEmail.get("NAME")!=null? mapEmail.get("NAME").toString():null;
				String emailcc = mapEmail.get("NAME2")!=null? mapEmail.get("NAME2").toString():null;
				String emailbcc = mapEmail.get("NAME3")!=null? mapEmail.get("NAME3").toString():null;
				String[] to = emailto.split(";");
				String[] cc = emailcc.split(";");
				String[] bcc = emailbcc.split(";");
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, commonDao.selectSysdate(), null, 
						true, "ajsjava@sinarmasmsiglife.co.id", 
						to, 
						cc,  
						bcc, 
						subject, 
						"Mohon diproses pengajuan permohonan pembayaran pengantian biaya Materai periode "+date
						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachments, null);
				sukses="Data Berhasi Di Kirim ";

				//Kirim Susulan , di update belakangan
				if(flag_proses==3){
					//update posisi
					for(int i=0;i<data.length;i++){
						snowsDao.updatePosisiMstSFee(data[i], "1","MSF_DATE_SEND2" ,commonDao.selectSysdate());
					}
				}
			}else if(flag_proses==2){
				for(int i=0;i<data.length;i++){
					String posisi = ServletRequestUtils.getStringParameter(request, "s_"+data[i],"");
					String kolom;
					if(posisi.equals("2")){
						kolom="MSF_DATE_ACC";
					}else{
						kolom="MSF_DATE_DEC";
					}
					snowsDao.updatePosisiMstSFee(data[i], posisi,kolom,commonDao.selectSysdate());
					/*//Kalau Tolakan, Kirim Email Kecabang
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, commonDao.selectSysdate(), null, 
							true, "ajsjava@sinarmasmsiglife.co.id", 
							to, 
							cc,  
							bcc, 
							subject, 
							"Mohon diproses pengajuan permohonan pembayaran pengantian biaya Materai periode "+date
							+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
							attachments, null);*/
				}
				sukses="Data Berhasi Di Update ";
			}
		}catch (Exception e) {
			sukses="Data Tidak Berhasi Di Simpan ";
			//logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		return sukses;
	}
	
	public List<PowersaveCair> selectReportCairSLinkNew(Map m) {
		return bacDao.selectReportCairSLinkNew(m);
	}
	
	public List selectDaftarPolisValidasiMaterai(String cab_bank, Integer dari, Integer sampai) {
		return bacDao.selectDaftarPolisValidasiMaterai(cab_bank, dari, sampai);
	}
	
	public List selectCabangSinarmasAll(String id_bank , String lcb_no){
		return Common.serializableList(uwDao.selectCabangSinarmasAll( id_bank , lcb_no));
	}
	
	public void updateDataPicRekening(String lcb_no , String no_rekening, String nama_pic, String email_pic){
		try{
			snowsDao.updateDataPicRekening(lcb_no, no_rekening, nama_pic , email_pic);
	    	snowsDao.updateDataMstStampFee(lcb_no, no_rekening, nama_pic);
		}catch (Exception e) {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
    }

	public HashMap CekNoHpNasabah(String hp, String bdate, String nama_pp, String no_blanko, User currentUser) {
		ArrayList listNasabahAgen = new ArrayList();
		HashMap mapData = new HashMap();
		HashMap mapParam = new HashMap();
		Integer countExist = 0;
		String namaPpSebelumnya="", flag="",no_register="",no_polis="",user_input="",tgl_input="",tgl_lahir_pp_sebelumnya="";
		String pesan="";
		listNasabahAgen =Common.serializableList(uwDao.selectDataNasabahBerdasarkanNoHp(hp,bdate,nama_pp));	
		if(listNasabahAgen.isEmpty()){
			   listNasabahAgen =Common.serializableList(uwDao.selectDataAgenBerdasarkanNoHp(hp));		
		}		
		try{
			
				if(!listNasabahAgen.isEmpty()){
					mapData = (HashMap)listNasabahAgen.get(0);
					namaPpSebelumnya = (String)mapData.get("NAMA");
					flag = (String)mapData.get("FLAG");
					no_register = (String)mapData.get("NO_REGISTER");
					no_polis = (String)mapData.get("NO_POLIS");
					user_input = (String)mapData.get("USER_INPUT");
					tgl_input = (String)mapData.get("TGL_INPUT");
					tgl_lahir_pp_sebelumnya = (String)mapData.get("TGL_LAHIR");
					countExist = uwDao.selectCountDataCekPhone(hp,nama_pp,namaPpSebelumnya, tgl_lahir_pp_sebelumnya,bdate,"1");
					if(countExist==0){
						pesan = "NO";
						uwDao.insertMstCheckPhone(nama_pp,namaPpSebelumnya,tgl_lahir_pp_sebelumnya,hp,bdate,no_blanko,flag,no_polis);
					}else{
						pesan ="EXIST";
					}
					StringBuffer pesanEmail = new StringBuffer();
					pesanEmail.append("Data Notifikasi ");
					if(flag.equals("0")){
						pesanEmail.append("\n\n No Hp =" + hp);
						pesanEmail.append("\n\n Nama PP Sebelumnya =" +namaPpSebelumnya);
						pesanEmail.append("\n\n Nomor SPAJ Sebelumnya =" +no_register);
						pesanEmail.append("\n\n Nomor Polis Sebelumnya =" +no_polis);
						pesanEmail.append("\n\n Tgl Input =" +tgl_input);
						pesanEmail.append("\n\n User Admin Input =" +currentUser.getLus_full_name());
					}else{
						pesanEmail.append("\n\n No Hp =" + hp);
						pesanEmail.append("\n\n Kode Agen = " +no_register);
						pesanEmail.append("\n\n Nama Agen= " +namaPpSebelumnya);
						pesanEmail.append("\n\n User Admin Input =" +currentUser.getLus_full_name());
						//pesanEmail.append("\n\n Nomor Polis Sebelumnya =" +no_polis);
					}
					
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
								null, 0, 0, new Date(), null, 
								true, "info@sinarmasmsiglife.co.id", 
								new String[]{"desy@sinarmasmsiglife.co.id","pratidina@sinarmasmsiglife.co.id","Yusy@sinarmasmsiglife.co.id","tedi@sinarmasmsiglife.co.id","iway@sinarmasmsiglife.co.id","Yudi@sinarmasmsiglife.co.id"}, 
								new String[]{"ryan@sinarmasmsiglife.co.id"},
								null, 
								"Notifikasi Pengecekkan No Hp",
								pesanEmail.toString(), 
								null, null);
						
				}else{
					pesan ="YES";
				}			
		}catch (Exception e) {
			e.getLocalizedMessage();
			pesan ="GAGAL";
			
		}
		mapParam.put("PESAN", pesan);
		mapParam.put("FLAG", flag);
		return mapParam;
	}
	
	/**
	 * @author MANTA
	 * @param tanggal
	 * @return
	 * Cek tgl, apakah ada pada hari kerja atau tidak.
	 * Jika tidak maka tgl tersebut akan di tambah 1 hari,
	 * sampai tgl tersebut berada pada hari kerja
	 */
    public Date cekHariKerja(Date tanggal){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(tanggal);
		
    	Date dt_tanggal = new Date();
    	String s_tgl = "";
    	Integer i = 0;
    	Integer j = 0;
    	Boolean libur = false;
    	
    	while(i==0){
    		s_tgl = df.format(cal.getTime());
			j = basDao.selectLibur(s_tgl);
			if(j > 0){
				libur = true;
			}else{
				libur = false;
			}
			
    		if((cal.get(Calendar.DAY_OF_WEEK)==1) || (cal.get(Calendar.DAY_OF_WEEK)==7) || libur==true){
				cal.add(Calendar.DATE, 1); 
    		}else{
    			i = 1;
    		}
    	}
    	
    	try{
    		dt_tanggal = df.parse( df.format(cal.getTime()) );
    	}catch (Exception e) {
    		logger.error("ERROR :", e);
		}
    	
    	return dt_tanggal;
    }
	
	public String prosesRedeemUnitForRefund(String lus_id, String reg_spaj){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(selectSysdate());
		
		String errorMessage = "";
		Date dt_trans = new Date();
		if(cal.get(Calendar.HOUR_OF_DAY) >= 10){
			cal.add(Calendar.DATE, 1);
		}

		dt_trans = cekHariKerja(cal.getTime());
		try{
		    refundDao.insertMstUlinkRedeem(reg_spaj, dt_trans);
		    refundDao.insertMstDetUlinkRedeem(reg_spaj);
		    refundDao.insertMstTransUlinkRedeem(reg_spaj, lus_id, dt_trans);
		}catch (Exception e) {
			logger.error("ERROR :", e);
			errorMessage = "PROSES PERMINTAAN REDEEM UNIT GAGAL!";
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
	    return errorMessage;
	}
	
	public ArrayList<Smsserver_out_hist> selectSmsserver_out_hist(Integer id) {
		return bacDao.selectSmsserver_out_hist(id);
	}
	
	public ArrayList<HashMap<String, Object>> selectReportTotalSmsOutBulanan(String month, String year) {
		return bacDao.selectReportTotalSmsOutBulanan(month, year);
	}
	
	public List daftarSPAJ(User currentUser, String lspd_id){
		List daftarSPAJ = uwDao.selectDaftarSPAJ(lspd_id, 1,null,null);
    	String spajList = null;
    	String spajList4EditButton = null;
    	if( !"29".equals( currentUser.getLde_id()) || !"01".equals( currentUser.getLde_id()) )
		{
			for( int i = 0 ; i < daftarSPAJ.size() ; i ++ )
			{
				HashMap temp = (HashMap)daftarSPAJ.get(i);
				String spaj = temp.get("REG_SPAJ").toString();	
				String warnaBgSpaj = temp.get("OTORISASI_BG").toString();
				List transferDisabledOrNot = bacDao.selectSpajOtorisasiDisabled(spaj);
				if( transferDisabledOrNot != null && transferDisabledOrNot.size() > 0 )
				{
					if( spajList == null )
					{
						spajList = spaj;
					}
					else
					{
						spajList = spajList + "," + spaj;
					}
				}
				if( warnaBgSpaj != null && "rgb(255,255,128)".equals(warnaBgSpaj))
				{
					if( spajList4EditButton == null )
					{
						spajList4EditButton = spaj;
					}
					else
					{
						spajList4EditButton = spajList4EditButton + "," + spaj;
					}
				}
			}									
		}
		return daftarSPAJ;
	}
	
	public void updateMstInboxHistLpsdId(String mi_id, String mi_desc, Integer lspd_before, Integer lspd_after, Integer flag_kategori){
		bacDao.updateMstInboxHistLpsdId(mi_id,mi_desc,lspd_before,lspd_after, flag_kategori);
	}
	
	public List<Map> selectMstInbox (String reg_spaj, String tipe){
		return uwDao.selectMstInbox(reg_spaj, tipe); 
	}
	
	public List<Map> selectUserQa (String reg_spaj, Integer lspd_after){
		return uwDao.selectUserQa(reg_spaj, lspd_after); 
	}
	
	public List<Map> selectUserQa1 (String reg_spaj, Integer lspd_after){
		return uwDao.selectUserQa1(reg_spaj, lspd_after); 
	}
		
	public Integer prosesBackToLb(String spaj, User currentUser, String keterangan, Integer flag_jenis)
	{	
		Integer flag_pesan = 1;
		Date ldt_now = commonDao.selectSysdate();		
		String mi_id = "", statusAksep = "";
		HashMap inbox = null;
		Integer lspd_id_from= 0, lspd_id_from2 = 0; 
		List <Map> mapInbox = null;
		if(flag_jenis==1){
			mapInbox = uwDao.selectMstInbox(spaj, "4");
		}
		if(flag_jenis==2){
			mapInbox = uwDao.selectMstInbox(spaj, "5");
		}
		if(mapInbox.size()>0){
			flag_pesan = 0;
			for(int i=0;i<mapInbox.size();i++){
				inbox = (HashMap) mapInbox.get(i);
				mi_id = (String) inbox.get("MI_ID");
				lspd_id_from = ((BigDecimal) inbox.get("LSPD_ID")).intValue();
				lspd_id_from2 = ((BigDecimal) inbox.get("LSPD_ID_FROM")).intValue();
				uwDao.updateMstInboxLspdId(mi_id, lspd_id_from2, lspd_id_from, 1, null, null, null,keterangan, 0);
				MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id_from2, null, null, keterangan, Integer.parseInt(currentUser.getLus_id()), ldt_now, null,0,0);
				uwDao.insertMstInboxHist(mstInboxHist);					
			}
		}
		return flag_pesan;
	}
	
	public void schedulerJatuhTempoVisaCampBas() {
		String msh_name = "SCHEDULER JATUH TEMPO VISA CAMP";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerJatuhTempoVisaCampBas(msh_name);
		}
	}

	public Map selectDetailPOD(String reg_spaj, String mspo_no_pengiriman) {
	   Map result = new HashMap();		
	   result = uwDao.selectDetailPod(reg_spaj,mspo_no_pengiriman);
	   if(result ==null)result = new HashMap();
	   return result;
	}
	
	public void updateListAgingFollowup(String a_beg_date, String a_end_date) {		
		this.basDao.updateListAgingFollowup(a_beg_date, a_end_date);
	}

	public Integer selectCountSpajMstRefund(String spaj) {		
		return uwDao.selectCountSpajMstRefund(spaj);
	}
	
	public void deleteDraftPembatalanPolis(RefundDbVO refundDbVO, User user){
        refundDao.deleteMstDetRefundLamp(refundDbVO.getSpajNo());
        refundDao.deleteMstDetRefundBySpaj(refundDbVO.getSpajNo());
        refundDao.deleteMstRefundBySpaj(refundDbVO.getSpajNo());
        uwDao.insertMstPositionSpaj(user.getLus_id(), "Penghapusan Draft Permintaan Batal Polis ", refundDbVO.getSpajNo(), 0);
	}
	
	public List selectReferensiTempSpaj( String spaj){
		return bacDao.selectReferensiTempSpaj(spaj);
	}
	
	public List selectReportCekNoHp(Map params) {		
		return basDao.selectReportCekNoHp(params);
	}
	
	public HashMap selectLstLevelDistRek(String dist, String inisial) {
		return Common.serializableMap(rekruitmentDao.selectLstLevelDistRek(dist, inisial));
	}
	
	public void insert_mstkuesioner_hist(String mku_no_reg, String mkh_desc, String posisi, String lus_id){
		Map map = new HashMap();
		map.put("mku_no_reg", mku_no_reg);
		map.put("mkh_desc", mkh_desc);
		map.put("posisi", posisi);
		map.put("lus_id", lus_id);
		this.rekruitmentDao.insert_mstkuesioner_hist(map);
	}
	
	public List selectMstKuesioner_hist(String mku_no_reg){
		return this.rekruitmentDao.selectMstKuesioner_hist(mku_no_reg);
	}
	
	public List selectkuesionerBy(String lus_id, String posisi, String no_reg){
		return this.rekruitmentDao.selectkuesionerBy(lus_id, posisi, no_reg);
	}
	
	public List selectLstScan(String dept, String ID){
		return this.rekruitmentDao.selectLstScan(dept, ID);
	}
	
	public void schedulerNotProceedWithRecurring(){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String msh_name = "SCHEDULER PROSES NOT PROCEED DMTM WITH RECURRING";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerNotProceedWithRecurring(msh_name);
		}	
	}
	
	public String selectLspdId(String reg_spaj){
		return commonDao.selectLspdId(reg_spaj);
	}
	
	public ArrayList<HashMap> selectLstMerchantFee(Integer flag_merchant) {
		return Common.serializableList(uwDao.selectLstMerchantFee(flag_merchant));
	}
	
	public ArrayList<AutoPaymentVA> selectListPaymentVA(){
		return Common.serializableList(uwDao.selectListPaymentVA());
	}
	
	public CmdAutoPaymentVA prosesAutoPaymentVA(CmdAutoPaymentVA command, User currentUser, HttpServletRequest request, HttpServletResponse response, ElionsManager eliosManager2, UwManager uwManager2) {
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String s_spaj = "";
		String s_spajOK = "";
		String s_spajNTGV = "";	//gagal validasi	
		String s_spajNTGPP = "";//gagal proses paralel		
		
//		CmdAutoPaymentVA data = new CmdAutoPaymentVA();
//		data.setListSpaj(new ArrayList<AutoPaymentVA>());
		
		try{
		    for(int i=0;i<command.getListSpaj().size();i++){
		    	AutoPaymentVA apva = command.getListSpaj().get(i);
		    	DepositPremium dp = new DepositPremium();
		    	s_spaj = apva.getReg_spaj();
		    	
		    	if(apva.getCheck() != null){
		    		Integer premiKe = 1;
		    		dp.setReg_spaj( apva.getReg_spaj() );
		    		dp.setMsdp_jtp( 1 );
		    		dp.setMsdp_flag( "B" );
		    		dp.setLsjb_id( 1 );
		    		dp.setMsdp_pay_date( df.parse(apva.getTgl_trx()) );
		    		dp.setMsdp_date_book( df.parse(apva.getTgl_trx()) );
		    		dp.setLku_id( apva.getLku_id() );
		    		dp.setMsdp_selisih_kurs( new Double(apva.getNilai_kurs()) );
		    		dp.setMsdp_desc( "CEK IBANK (No Transaksi " + apva.getNo_trx() + ")" );
		    		dp.setLus_id( Integer.parseInt(currentUser.getLus_id()) );
		    		dp.setMsdp_active( 0 );
		    		dp.setLsrek_id( Integer.parseInt(accountingDao.selectRekEkalifeByRekAJS(apva.getNorek_ajs())) );
		    		dp.setMsdp_no_pre( apva.getNo_pre() );
		    		dp.setMsdp_jurnal( 0 );
		    		
		    		for(int j=0;j<3;j++){
			    		Double jml_bayar = new Double(0);
			    		Integer flag_topup = null;
						if(j==0){
							jml_bayar = apva.getPremi_pokok().doubleValue();
//				    		dp.setMsdp_flag_merchant( apva.getFlag_merchant() );
						}else if(j==1){
							jml_bayar = apva.getPremi_topup_berkala().doubleValue();
							flag_topup = 2;
						}else{
							jml_bayar = apva.getPremi_topup_tunggal().doubleValue();
							flag_topup = 1;
						}
						
						if(jml_bayar.doubleValue() != new Double(0)){
			    			Date nowDate = commonDao.selectSysdate("dd", false, 0);
				    		String no_urut = uwDao.selectMaxNoUrutMstDrekDet(apva.getNo_trx()) ;
							if(StringUtil.isEmpty(no_urut)){
								no_urut = "0";
							}
							dp.setMsdp_number(premiKe);
							dp.setMsdp_payment(jml_bayar);
							dp.setMsdp_input_date(nowDate);
							dp.setMsdp_flag_topup(flag_topup);
							
							bacDao.insertMst_deposit_premium(dp);
							uwDao.updateMstDrekListRk(apva.getReg_spaj(), currentUser.getLus_id(), apva.getNo_trx(), apva.getFlag_tunggal_gabungan(), apva.getJenis_trx());
							uwDao.insertMstDrekDet(apva.getNo_trx(), "1", premiKe.toString(), jml_bayar, LazyConverter.toInt(no_urut) + 1, apva.getReg_spaj(), premiKe,
									null, currentUser.getLus_id(), nowDate, apva.getNo_pre(), apva.getNorek_ajs(), apva.getJenis_trx(), df.parse(apva.getTgl_trx()));
							
							premiKe = premiKe + 1;
						}
		    		}
		    		
		    		HashMap validasi = new HashMap();
		    		validasi=(HashMap) this.validasiNewBusiness(s_spaj, currentUser,2);
		    		Integer stat_gagal = 0;
		    		
		    		if(!validasi.isEmpty()){
		    			s_spajNTGV = s_spajNTGV + "<br>"+ s_spaj + validasi.toString() + "; "; 
						stat_gagal = 1;
					}
					
					if(stat_gagal == 0){
						Pemegang pmg = eliosManager2.selectpp(s_spaj);
						Tertanggung tertanggung = eliosManager2.selectttg(s_spaj);
						Datausulan dataUsulan = eliosManager2.selectDataUsulanutama(s_spaj);
						BindException err = null;
						Map mapAutoAccept=null;
						mapAutoAccept =this.ProsesParalel(2, s_spaj, 1,pmg,tertanggung, dataUsulan, currentUser,request,err,eliosManager2,uwManager2);
						String ket_sukses = mapAutoAccept.get("success").toString();
						String error = mapAutoAccept.get("error").toString();
						if(mapAutoAccept.get("error").toString().isEmpty())
						{
							prosesSnows(s_spaj, currentUser.getLus_id(), null, 201);
							s_spajOK = s_spajOK  + "<br>"+ s_spaj +"<br> - " + mapAutoAccept.get("success").toString().replaceAll("<br>-", " ") + "; "; 
						}else{
							eliosManager2.insertMstPositionSpaj(currentUser.getLus_id(), "[GAGAL PROSES ] "+mapAutoAccept.get("error").toString().replaceAll("<br>-", " "), s_spaj, 0);
							s_spajNTGPP = s_spajNTGPP  + "<br>"+ s_spaj + mapAutoAccept.get("error").toString().replaceAll("<br>-", " ") + "; "; 
							stat_gagal = 2;
						}
						
						PrintPolisPrintingController ppc = new PrintPolisPrintingController();
						//Proses Direct Print 
						/// jika ada perubahan disini , lakukan juga perubahan di proses prosesAutoPaymentVA (bacManager.prosesAutoPaymentVA) - Ridhaal
						if(mapAutoAccept!=null){
							if(mapAutoAccept.get("flag_print").toString().equals("1")){
								int isInputanBank = eliosManager2.selectIsInputanBank(s_spaj);
								Integer count = eliosManager2.selectCountProductSimasPrimaAkseptasiKhusus(s_spaj, 1,10, isInputanBank);			
								String flag2 = "0";
								String businessId = uwManager2.selectBusinessId(s_spaj);
								String businessNo = uwManager2.selectLsdbsNumber(s_spaj);
								String pesanKemenangan="";
								Map tambahan = new HashMap();
								// PROJECT: POWERSAVE & STABLE LINK 			
								if(isInputanBank==2 || isInputanBank==3 || (businessId.equals("175") && businessNo.equals("2")) || (businessId.equals("73") && businessNo.equals("14"))) {
									//if(products.powerSave(businessId) || products.stableLink(businessId)){
									if(businessId.equals("73") && businessNo.equals("14")){//MANTA (13/01/2014) - Request Andy
										pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Filling.";
									}else if(isInputanBank==2){
										pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis.";
									}else if(products.productBsmFlowStandardIndividu(Integer.parseInt(businessId), Integer.parseInt(businessNo))){
										pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis.";
									}else{
										if(count>0){ //AKSEPTASI KHUSUS
											pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis. Status masih Akseptasi Khusus.";
										}else { //AKSEPTASI BIASA
											pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Input Tanda Terima.";
										}
										tambahan.put("transferTTP", true);
										tambahan.put("spaj", s_spaj);
										flag2 ="1";
									}
								}else if(businessId.equals("157") ) {
									pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Input Tanda Terima.";
								}else if("187,203,209".indexOf(businessId) > -1) {
									Map m = uwManager2.selectInformasiEmailSoftcopy(s_spaj);
									String errorRekening = eliosManager2.cekRekAgen2(s_spaj);
									String email = (String) m.get("MSPE_EMAIL");
									String keterangan = null;
									if(email != null) {
										if((businessId.equals("187") && "5,6".indexOf(businessNo)>-1)){
											keterangan = "Filling.Softcopy Telah dikirimkan ke : " + email;
										}else{
											if(!errorRekening.equals("")) {
												keterangan = "PROSES CHECKING TTP (REKENING AGEN MASIH KOSONG).Softcopy Telah dikirimkan.";
											}else{
												keterangan = "Komisi (Finance).Softcopy Telah dikirimkan.";
											}
										}
										pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke " + keterangan;
									}else{
										if((businessId.equals("187") && "5,6".indexOf(businessNo)>-1)){
											keterangan = "Filling.";
										}else{
											if(!errorRekening.equals("")) {
												keterangan = "PROSES CHECKING TTP (REKENING AGEN MASIH KOSONG).Softcopy Telah dikirimkan.";
											}else{
												keterangan = "Komisi (Finance).";
											}	
										}
										pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke " + keterangan;
									}
								}else {
									pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis.";
								}

								Integer mspo_provider= uwManager2.selectGetMspoProvider(s_spaj);
								String LusId = currentUser.getLus_id();
								String lde_id = currentUser.getLde_id();
								String lca_id = eliosManager2.selectCabangFromSpaj(s_spaj);

								//20190408 Mark Valentino DISABLE DIRECT PRINT
//								try{
//									String pesanDirectPrint = "";
//									if( businessId.equals("163") || ( businessId.equals("143") && ("1,2,3,7".indexOf(businessNo)>-1) && lca_id.equals("01") ) || (businessId.equals("144") && businessNo.equals("1") && lca_id.equals("01") ) ||
//										businessId.equals("183") || (businessId.equals("142") && lca_id.equals("01")) || (businessId.equals("177") && businessNo.equals("4")))
//									{
//										if("11,39".indexOf(lde_id)>-1){
//											String email = currentUser.getEmail();
//											Integer flagprint = 1;
//											if(businessId.equals("177") && businessNo.equals("4")) flagprint = 4; //Khusus PT INTI
//
//											eliosManager2.updatePolicyAndInsertPositionSpaj(s_spaj, "mspo_date_print", LusId, 6, 1, "PRINT POLIS (E-LIONS) DIRECT PRINT", true, currentUser);
//											
//											generateReport(request, mspo_provider, flagprint, eliosManager2, uwManager2, 1, s_spaj);
//
//											HashMap<String, Object> printer = (HashMap<String, Object>) this.selectPropertiesPrinter();
//											String ipAddress = (String) printer.get("IP_ADDRESS");
//											String printerName = (String) printer.get("PRINTER_NAME");
//
//											String cabang = eliosManager2.selectCabangFromSpaj(s_spaj);
//											String allowPrint = this.getAllowPrint(printerName);
//											String pdfFile = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+s_spaj+"\\"+"PolisAll.pdf";
//
//											try{
//												ThreadPrint T1 = new ThreadPrint(printerName,"directPrint",pdfFile,allowPrint,s_spaj,currentUser.getLus_id(),Print.getCountPrint(pdfFile));
//												T1.start();
//											}catch(Exception e){
//												EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
//														null, 0, 0, new Date(), null, false, props.getProperty("admin.ajsjava"), new String[]{"ryan@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id;ridhaal@sinarmasmsiglife.co.id","antasari@sinarmasmsiglife.co.id"}, null, null,  
//														"Error Thread print when AutoPayment VA processing", 
//														e+"", null, s_spaj);
//											}
//
//											pesanDirectPrint = prosesPrint(s_spaj,cabang,ipAddress,printerName);
//										//	hm_map.put("pesanDirectPrint", pesanDirectPrint);
//											s_spajOK = s_spajOK  + "<br> - "+ pesanDirectPrint  + "; "; 
//										}
//									}
//								}catch(Exception e){
//									EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
//											null, 0, 0, new Date(), null, false, props.getProperty("admin.ajsjava"), new String[]{"ryan@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id;ridhaal@sinarmasmsiglife.co.id","antasari@sinarmasmsiglife.co.id"}, null, null,  
//											"Error Direct Print Transfer saat Proses AutoPayment VA "+s_spaj, 
//											e+"", null, s_spaj);
//									logger.error("ERROR :", e);
//								}
							//	hm_map.put("pesanKemenangan", pesanKemenangan);
								s_spajOK = s_spajOK  + "<br> - "+ pesanKemenangan  + "; "; 
							}
						}
					}
		    	}
		    }

			if (s_spajOK.equals("")){
				s_spajOK = "<b> TIDAK ADA </b>";
			}
			if (s_spajNTGV.equals("")){
				s_spajNTGV = "<b> TIDAK ADA </b>";
			}
			if (s_spajNTGPP.equals("")){
				s_spajNTGPP = "<b> TIDAK ADA </b>";
			}
		    
		    command.setPesan("<u>(1)Proses Auto Payment dan Approved by Collection  Untuk SPAJ Berikut Telah BERHASIL : </u><b>" + s_spajOK +"</b><br><br>"+
		    		"<u>(2)Proses Auto Payment berhasil, tetapi Approved by Collection GAGAL. silahkan lakukan APPROVE(PROSES NB) secara manual !</u><br><br><i>Gagal Validasi : <i><b> " + s_spajNTGV  +
		    		"</b><br><br><i>Gagal Proses Paralel : <i><b>"+ s_spajNTGPP +"</b><br>");
		    
		    
		}catch (Exception e) {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			logger.error("ERROR :", e);
			command.setPesan("Terjadi Permasalahan Pada Proses Auto Payment untuk SPAJ No. " + s_spaj);
		}
		return command;
	}
	
	class ThreadPrint extends Thread implements Serializable{
		private static final long serialVersionUID = -2208935165403596233L;
		private Thread t;
		   private String printerNameThread;
		   private String pdfFileThread;
		   private String allowPrintThread;
		   private String ThreadPrint;
		   private String spajPrint;
		   private Integer CountPrint;
		   private String lusIdPrint;
		   
		   ThreadPrint(String printerName,String name,String pdfFile,String allowPrint,String spaj,String lusId,Integer Count){
			   ThreadPrint = name;
			   printerNameThread = printerName;
		       pdfFileThread =pdfFile;
			   allowPrintThread = allowPrint;
			   spajPrint = spaj;
			   CountPrint = Count;
			   lusIdPrint = lusId;
		   }
		   
		   public void run() {
		      if(Print.directPrint(pdfFileThread, printerNameThread, allowPrintThread)){
		    	  insertPrintHistory(spajPrint, "POLIS ALL", CountPrint, lusIdPrint);
			  }

		   }
		   
		   public void start ()
		   {
		      if (t == null)
		      {
		         t = new Thread (this, ThreadPrint);
		         t.start ();
		      }
		   }
	}
	
	public PolicyInfoVO getDataRefundCentrix(PolicyInfoVO command){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		command.setListCentrix(new ArrayList<RefundCentrix>());
		try{
			ExternalDatabase centrix = new ExternalDatabase("ajs1_tss");
			ArrayList<HashMap> ls_data = Common.serializableList(
											centrix.doQuery(
											"select * from vw_tss_RefundApplication " +
											"where isActive = ? and Status = ? and " +
											"Taskgroup = ?",
											new Object[] {"1", "REFUND", "TEAMLEADER"}
											));
			
			for(int i=0; i<ls_data.size(); i++){
				HashMap data = new HashMap(ls_data.get(i));
				RefundCentrix dataCentrix = new RefundCentrix();
				dataCentrix.setApplicationID( ((Integer) data.get("ApplicationID")).toString() );
				dataCentrix.setPolicyHolder( (String) data.get("PolicyHolder") );
				dataCentrix.setCcHolderName( (String) data.get("CCHolderName") );
				dataCentrix.setCcNo( (String) data.get("CCNo") );
				dataCentrix.setCcCardType( (String) data.get("CCCardType") );
				dataCentrix.setCcBankName( (String) data.get("CCBankName") );
				dataCentrix.setPaymentMethod( (String) data.get("PaymentMethod") );
				dataCentrix.setSite( (String) data.get("Site") );
				dataCentrix.setProduct( (String) data.get("Product") );
				dataCentrix.setTaskgroup( (String) data.get("Taskgroup") );
				dataCentrix.setStatus( (String) data.get("Status") );
				dataCentrix.setIsActive( (Boolean) data.get("IsActive") );
				dataCentrix.setIsSubmitted( (Boolean) data.get("IsSubmitted") );
				dataCentrix.setInitialPremium( ((BigDecimal) data.get("InitialPremium")).setScale(0, BigDecimal.ROUND_HALF_UP) );
				dataCentrix.setTrxDate( df.format((Date) data.get("TrxDate")) );
				
				command.getListCentrix().add(i, dataCentrix);
			}
			
		} catch (Exception e) {
			logger.info("Terjadi masalah pada proses penarikan data ke CENTRIX!");
			logger.error("ERROR :", e);
			command.setPesan("Terjadi masalah pada proses penarikan data ke CENTRIX!");
			return command;
		}
		
		return command;
	}
	
	public ArrayList<HashMap<String, Object>> selectDetailPermintaanVa(String no_va, String begDate, String endDate) {
		return uwDao.selectDetailPermintaanVa(no_va, begDate, endDate);
	}
	
	public Map ProsesParalel(Integer flag , String s_spaj, Integer i_flag_reg, Pemegang pmg,Tertanggung tt, Datausulan du, User user,HttpServletRequest request, BindException errors,ElionsManager elionsManager2, UwManager uwManager2) throws Exception {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		HashMap map= new HashMap();
		StringBuffer err_speedy = new StringBuffer();
		StringBuffer suc_speedy = new StringBuffer();
		String result_simultan = "";
		String result_kuesioner = "";
		String result_akseptasi = "";
		
		Policy policy = new Policy();
		Integer error = 0;
		Integer flag_print = 0;
		Integer uw_helpdesk = 0;
		Integer substandart = 0;
		Integer lspdIdTemp = 0;
		String lspdPosTemp="";
		String mdesc = "";
		String idSimultanPp = null;
		String idSimultanTt = null;
		DecimalFormat fmt = new DecimalFormat ("000");
		HashMap hm_speedy = new HashMap();
		Integer i_speedy = 1;
		String s_medis = "";
		Date nowDate = commonDao.selectSysdate();
		Map mTertanggung =uwDao.selectTertanggung(s_spaj);
		CommandChecklist cmd = new CommandChecklist();
		
		Integer i_owner = ServletRequestUtils.getIntParameter(request, "owner",1);
		Integer i_owner2 = ServletRequestUtils.getIntParameter(request, "owner2",0);
		Integer i_parent = ServletRequestUtils.getIntParameter(request, "parent",1);
		Integer i_agent = ServletRequestUtils.getIntParameter(request, "agent",1);
		Integer i_reviewed = ServletRequestUtils.getIntParameter(request, "reviewed",1);
		Integer i_hq = ServletRequestUtils.getIntParameter(request, "health_quest",1);

		String lsbsid = uwDao.selectBusinessId(s_spaj);
		String lsdbsnumber = uwDao.selectLsdbsNumber(s_spaj);
				
		if(flag==1){
			//compare signature in all form
			if(i_owner == 0){
				i_speedy = 0;
				err_speedy.append("<br>- Owner's & Insured's Signature tidak sama");
			}

			if(tt.getMste_age() < 17){
				if(i_parent == 0){
					i_speedy = 0;
					err_speedy.append("<br>- Parent's Signature tidak ada");
				}
			}

			if(i_agent == 0){
				i_speedy = 0;
				err_speedy.append("<br>- Agent's Signature tidak sama/tidak ada");
			}

			if(i_reviewed == 0){
				i_speedy = 0;
				err_speedy.append("<br>- Belum di review oleh user");
			}
 
			if(i_owner2 == 1){
				uwDao.insertMstPositionSpaj(user.getLus_id(), "Tanda Tangan Sesuai Dengan Bukti Identitas", s_spaj, 0);
			}
			uwDao.updateMstInsuredHealthQuest(s_spaj, i_hq);
			//simultan dan medis via prosedur oracle
			if(du.getLsbs_id()==187 && (du.getLsdbs_number()>=11 && du.getLsdbs_number()<=14)){
				uwDao.deleteMstSimultaneous(s_spaj);
			}
			
			result_simultan = uwDao.prosesSimultanMedis(s_spaj, user.getLus_id());
			/*if(!result_simultan.equalsIgnoreCase("sukses")){
			err_speedy.append("<br>- Proses simultan error");
			error = 1;
		}*/
			Insured ins = uwDao.selectMstInsuredAll(s_spaj);
			if(ins.getFlag_speedy()==null){
				err_speedy.append("<br>- Proses simultan error");
				error = 1;
			} else if(ins.getFlag_speedy()==0){
				suc_speedy.append("<br>- "+result_simultan);
			}

			String s_valMedis = "";

			//if nya di nonaktifkan permintaan dokter asri
			//if(ins.getFlag_speedy()==1){
			//validasi questioner via prosedur oracle

			result_kuesioner = uwDao.prosesValQuest(s_spaj, user.getLus_id());
			
			if((lsbsid.equalsIgnoreCase("212") && (lsdbsnumber.equalsIgnoreCase("9") || lsdbsnumber.equalsIgnoreCase("10")|| lsdbsnumber.equalsIgnoreCase("14"))) || //Chandra A - 20180517: jika smile proteksi/proteksi lkpli result = sukses // add lsdbs_number = 14 nana
			   (lsbsid.equalsIgnoreCase("142") && lsdbsnumber.equalsIgnoreCase("13"))){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
				result_kuesioner = "sukses";
			}
			if(lsbsid.equalsIgnoreCase("208") && (lsdbsnumber.equalsIgnoreCase("45") || lsdbsnumber.equalsIgnoreCase("46") || lsdbsnumber.equalsIgnoreCase("47") || lsdbsnumber.equalsIgnoreCase("48"))){ 
				result_kuesioner = "sukses";
			}
				
			//28-8-2015(Deddy) - Disini ditambahkan untuk pengiriman email further otomatis untuk kuisioner SPAJ FULL periode 201509.
			//Caranya : 
			//1. Cek dulu apakah SPAJ ini bagian question kesehatan SPAJ FULL menggunakan periode 201509
			//2. Apabila bukan, tidak perlu dijalankan.Apabila iya, cek ke table mst_speedy_history dan distinct di kolom ATT_FILE_FURTHER, apabila ada isi maka semua file itu diattach dan dikirimkan email Further.
			//3. Email Further yg dikirimkan, disamakan dengan com.ekalife.elions.service.ElionsManager.kirimEmail. 
			Integer lssa_id = uwDao.selectStatusAksep(s_spaj);
			if(!result_kuesioner.equalsIgnoreCase("sukses")){
				err_speedy.append("<br>- Proses kuesioner error. ("+result_kuesioner+")");
				error = 1;
			} else {
				// Jika proses kuisioner sukses, kirim email
				if("SEP2015".equals(uwDao.selectPeriodQHealth(s_spaj)) 
						&& lssa_id == 3) {
					Command command = new Command();
					command.setSpaj(s_spaj);
					command.setFlag_ut(0);
					command.setLspd_id(ins.getLspd_id());

					Map mDataUsulan = (HashMap) uwDao.selectDataUsulan2(s_spaj);
					if(!mDataUsulan.isEmpty() && mDataUsulan.get("LSBS_ID") != null) {
						command.setMsprTsi((Double)mDataUsulan.get("MSPR_TSI"));
						command.setMsprPremium((Double)mDataUsulan.get("MSPR_PREMIUM"));
						command.setLscbPayMode((String)mDataUsulan.get("LSCB_PAY_MODE"));
					}

					String emailAdmin = uwDao.selectEmailAdmin(s_spaj);
					Map agen = uwDao.selectEmailAgen(s_spaj);
					String namaAgen = (String) agen.get("MCL_FIRST");
					String emailAgen = (String) agen.get("MSPE_EMAIL");
					String msagId = (String) agen.get("MSAG_ID");
					Integer msagSertifikat = (Integer) agen.get("MSAG_SERTIFIKAT");
					Date tglSertifikat = (Date) agen.get("MSAG_BERLAKU");
					Date msagBegDate = (Date) agen.get("MSAG_BEG_DATE");
					Integer msagActive = (Integer) agen.get("MSAG_ACTIVE");

					List listPositionSpaj = uwDao.selectMstPositionSpajWithSubId(s_spaj);
					command.setLsPosition(listPositionSpaj);

					Map a = (HashMap) listPositionSpaj.get(listPositionSpaj.size() - 1);
					String mspsDate = (String) a.get("MSPS_DATE");
					String status = (String) a.get("STATUS_ACCEPT");
					command.setSubLiAksep(0);

					if(status == null) {
						status = commonDao.selectStatusAcceptFromSpaj(s_spaj);
					}

					Map MPp = (HashMap) uwDao.selectPemegang(s_spaj);
					command.setLcaIdPp((String) MPp.get("LCA_ID"));
					command.setNamaPemegang((String) MPp.get("MCL_FIRST"));

					Map mTt = (HashMap) uwDao.selectTertanggung(s_spaj);
					command.setNamaTertanggung((String) mTt.get("MCL_FIRST"));

					String deskripsi = "FURTHER DOKUMEN KUISIONER KESEHATAN :";
					ArrayList<File> listAttachment = Common.serializableList(uwDao.selectListAttFileFurtherSpeedy(s_spaj));
					if(listAttachment.size()>0){
						for(int i=0;i<listAttachment.size();i++){
							String nama_file_ori = listAttachment.get(i).getName();
							String checking="0123456789";
							String nama_file_rev = ""; 
							for(int j=0;j<nama_file_ori.length();j++){
								if ( checking.indexOf(nama_file_ori.substring(j, j+1))==-1)
								{
									nama_file_rev += nama_file_ori.substring(j,j+1); 
								}
							}
							deskripsi += nama_file_rev.replace("_", " ").replace(".pdf", "")+"; ";
						}
					}
					//	emailAdmin = "ningrum@sinarmasmsiglife.co.id;";
					//	emailAgen = "ryan@sinarmasmsiglife.co.id";
//					this.kirimEmail(s_spaj, ins.getLssa_id(), emailAdmin, emailAgen, 
//							status, mspsDate, user, deskripsi, null, msagId, msagSertifikat, 
//							tglSertifikat, msagBegDate, msagActive, command.getLcaIdPp(), command.getNamaPemegang(), 
//							command.getNamaTertanggung(), command, null, new BindException(command, "cmd"), listAttachment);
				}
			}
			ins = uwDao.selectMstInsuredAll(s_spaj);

			//}

			//select medis
			s_medis = uwDao.selectMedisDesc(ins.getJns_medis()); 

			//select hasil validasi medis
			ArrayList l_histSpeedy = Common.serializableList(uwDao.selectHistorySpeedy(s_spaj));
			if(l_histSpeedy.size()>0){
				for(int i=0; i<l_histSpeedy.size();i++){
					HashMap m = (HashMap) l_histSpeedy.get(i);
					err_speedy.append("<br> - "+(String)m.get("DESCRIPTION"));

					if(((String)m.get("DESCRIPTION")).toUpperCase().contains("FORM PERUBAHAN DAN PENAMBAHAN DATA"))uw_helpdesk = 1;
					else if(((String)m.get("DESCRIPTION")).toUpperCase().contains("UNCLEAN CASE"))uw_helpdesk = 1;
					else if(((String)m.get("DESCRIPTION")).toUpperCase().contains("FURTHER"))uw_helpdesk = 1;
				}
			}

			i_speedy = ins.getFlag_speedy();
			
			//Khusus simas/smile kid. jika SAR > 100 juta, transfer ke uw proses -randy
			boolean smilekid = false;
			boolean smilebaby = false;
			if(du.getLsbs_id()==208 && ((du.getLsdbs_number()>=5 && du.getLsdbs_number()<=28) || (du.getLsdbs_number()>=37 && du.getLsdbs_number()<=44)) ){
				if(du.getMspr_tsi()> 100000000){
					i_speedy = 0;
					smilekid = true;
				}
				
				//jika ada rider smile baby maka transfer ke uw proses
				List<Datarider> xx = du.getDaftaRider();
				if (du.getDaftaRider().size() != 0) {
					for (Datarider datarider : xx) {
						if (datarider.getLsbs_id() == 836) {
							i_speedy = 0;
							smilebaby = true;
						}
					}
				}
			}
			
			//Khusus Provestara dan Smile Medical Plus. jika usia Tertanggung >= 50, transfer ke uw proses -Ridhaal
			boolean provestPst50an = false;
			if(du.getLsbs_id()==214 || du.getLsbs_id()==225){
					if(tt.getMste_age() >= 50){
						i_speedy = 0;
						provestPst50an = true;
					}
									
					//jika ada data peserta rider Provestara dengan usia Tertanggung >= 50 maka transfer ke uw proses
					List<Map> listPeserta = uwDao.selectDataPeserta(s_spaj);
					if (listPeserta.size() != 0) {
						for (int i=0;i<listPeserta.size();i++) {
							Map peserta = (HashMap) listPeserta.get(i);
							Integer umur = Integer.parseInt(peserta.get("UMUR").toString());
							
							if (umur >= 50 ) {
								i_speedy = 0;
								provestPst50an = true;
							}
						}
					}
			}
			
			// SMILE LIFE SYARIAH - AUTOACCEPTED apapun kondisinya
			if( (du.getLsbs_id()==200 && du.getLsdbs_number()==7) || 
					(du.getLsbs_id()==223 && du.getLsdbs_number()==1) ||
					(du.getLsbs_id()==217 && du.getLsdbs_number()==2) ||
					(du.getLsbs_id()==142 && du.getLsdbs_number()==13)){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
					i_speedy = 1;
				}
			
			
			if(i_speedy==0){//transfer ke uw (posisi 2)
					uwDao.insertMstPositionSpaj(user.getLus_id(), "TRANSFER KE U/W", s_spaj, 0);
					if(smilebaby) uwDao.insertMstPositionSpaj(user.getLus_id(), "TRANSFER KE U/W PROSES KARENA RIDER SMILE BABY", s_spaj, 0);
					if(smilekid) uwDao.insertMstPositionSpaj(user.getLus_id(), "SAR > Rp 100.000.000; wajib mengisi Declaration Heath", s_spaj, 0);
					
					if(provestPst50an) uwDao.insertMstPositionSpaj(user.getLus_id(), "TRANSFER KE U/W PROSES KARENA ADANYA TERTANGGUNG UTAMA/TAMBAHAN UNTUK PRODUK INI DENGAN USIA 50 - 65 TAHUN ", s_spaj, 0);
					
					uwDao.updateMstInsured(s_spaj,2);
					uwDao.updateMstPolicy(s_spaj,2);
					uwDao.saveMstTransHistory(s_spaj, "tgl_transfer_uw", null, null, null);

					suc_speedy.append("<br>- Polis Non speedy, dikirim ke UW PROSES");

					String to = "";
					String lca_id = uwDao.selectLcaIdMstPolicyBasedSpaj(s_spaj);
					//				Rahmayanti - Snows				
					uwDao.prosesSnows(s_spaj, user.getLus_id(), 210, 212);

					if("09,44,45,56,69".indexOf(lca_id) > 0)to = "UWHelpdeskBancassurance@sinarmasmsiglife.co.id";
					else if("40,59".indexOf(lca_id) > 0)to = "UWHelpdeskDMTM@sinarmasmsiglife.co.id";
					else to = "UWHelpdeskAgency@sinarmasmsiglife.co.id";

					//kirim email ke UW Proses
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, nowDate, null, 
							true, "ajsjava@sinarmasmsiglife.co.id", 
							new String[]{to}, 
							null,  
							null, 
							"[E-Lions] Automate UW dengan SPAJ "+s_spaj, 
							"Medis : "+s_medis+" \n"+suc_speedy.toString()+err_speedy.toString(), 
							null, s_spaj);
				//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				this.uwDao.batchingSimasPrimelink(s_spaj); //update batching simas primelink (rider save) //Chandra - 20181012
				//this.uwDao.blacklistChecking(reg_spaj); //helpdesk 127356, cek blacklist dan kirim email. //Chandra - 20181011
			}else{
				//PROSES UNDERWRITER
				try{
					// Tgl Terima Doc 
					uwDao.updateMstInsuredTgl(s_spaj, 1, nowDate, 2);
					uwDao.saveMstTransHistory(s_spaj, "tgl_berkas_terima_uw", nowDate, null, null);
					if(uwDao.updateMstpositionSpajTgl(s_spaj, user.getLus_id(), defaultDateFormat.format(nowDate), "TGL SPAJ DOC","TGL SPAJ DOC")==0){
						uwDao.insertMstPositionSpaj(user.getLus_id(), "TGL SPAJ DOC", s_spaj, 0);
					}

					// Tgl Terima SPAJ
					uwDao.updateMstInsuredTgl(s_spaj, 1, nowDate, 0);
					uwDao.saveMstTransHistory(s_spaj, "tgl_terima_spaj_uw", nowDate, null, null);
					if(uwDao.updateMstpositionSpajTgl(s_spaj, user.getLus_id(), defaultDateFormat.format(nowDate), "TGL TERIMA SPAJ","TGL TERIMA SPAJ")==0){
						uwDao.insertMstPositionSpaj(user.getLus_id(), "TGL TERIMA SPAJ", s_spaj, 0);
					}

					//KYC
					uwDao.updateProsesKyc(s_spaj, 1, user.getLus_id(), 0, nowDate);

					//Copy Checklist untuk Update flag_uw =1 , PROSES CHECKLIST DI UW

					cmd.setLspd_id(27);
					cmd.setReg_spaj(s_spaj);
					cmd.setListChecklist(checklistDao.selectCheckListBySpaj(s_spaj));
					checklistDao.saveChecklist(cmd, user);

					suc_speedy.append("<br>- Proses Underwriting Berhasil!");
				}catch(Exception e){
					error = 1;
					e.printStackTrace();
					err_speedy.append("<br>- Proses Underwriting Error!");
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}

				// Proses Reas - Ini yang susah.
				Reas reas=new Reas();
				Integer lsbsId = 0;
				try{
					reas.setInfo(new Integer(0));
					reas.setLspdId(new Integer(27));
					reas.setLstbId(new Integer(1));
					String las_reas[]=new String[3];
					las_reas[0]="Non-Reas";
					las_reas[1]="Treaty";
					las_reas[2]="Facultative";
					reas.setCurrentUser((User) request.getSession().getAttribute("currentUser"));        

					reas.setSpaj(s_spaj);
					HashMap mPosisi= Common.serializableMap(uwDao.selectF_check_posisi(s_spaj));
					lspdIdTemp=reas.getLspdId();//(Integer)mPosisi.get("LSPD_ID");
					lspdPosTemp=(String)mPosisi.get("LSPD_POSITION");
					//produk asm
					HashMap hm_map = Common.serializableMap(uwDao.selectDataUsulan(reas.getSpaj()));
					lsbsId=(Integer)hm_map.get("LSBS_ID");

					//validasi Posisi dokumen
					if(lspdIdTemp.intValue()!=27){
						reas.setInfo(new Integer(1));
						reas.setLsPosDoc(lspdPosTemp);
						//MessageBox('Info', 'Posisi Polis Ini Ada di ' + ls_pos )
					}
					//
					//tertanggung
					reas.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
					reas.setMsteInsured((String)mTertanggung.get("MCL_ID"));
					reas.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
					//
					HashMap mStatus = Common.serializableMap(uwDao.selectWfGetStatus(s_spaj,reas.getInsuredNo()));
					reas.setLiAksep((Integer)mStatus.get("LSSA_ID"));
					reas.setLiReas((Integer)mStatus.get("MSTE_REAS"));
					if (reas.getLiAksep()==null) 
						reas.setLiAksep( new Integer(1));


					//dw1 //pemegang
					policy=uwDao.selectDw1Underwriting(reas.getSpaj(),reas.getLspdId(),reas.getLstbId());
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
						if(reas.getMste_kyc_date()==null){
							reas.setInfo(new Integer(6));
						}		
						if ( (reas.getMspoPolicyHolder().substring(0,2).equalsIgnoreCase("XX")) || (reas.getMspoPolicyHolder().substring(0,2).equalsIgnoreCase("WW")) ){
							reas.setInfo(new Integer(2));
							//MessageBox('Info', 'Proses Simultan Belum Dilakukan !!!')
						}else if (reas.getLiReas()!=null){
							Integer liBackup=(Integer)uwDao.selectMstInsuredMsteBackup(reas.getSpaj(),reas.getInsuredNo());
							if(liBackup==null)
								liBackup=new Integer(100);
							if(liBackup.intValue()!=0 || (liBackup.intValue()==0 && reas.getLiReas().intValue()==2) ){
								reas.setInfo(new Integer(3));
								reas.setLsPosDoc(las_reas[reas.getLiReas().intValue()]);
								//				If MessageBox( 'Information', 'Proses Reas sudah pernah dilakukan~r~nType Reas = ' + las_reas[li_reas+1] &
								//				+  '~r~nView hasil proses sebelumnya ?', Exclamation!, OKCancel!, 1 ) = 1 Then OpenWithParm(w_simultan,lstr_polis.no_spaj)
							}

						}
						//cek standard
						if(policy.getMste_standard().intValue()==1){
							Integer liCount=uwDao.selectCountMstProductInsuredCekStandard(reas.getSpaj());
							if(liCount.intValue()==0){
								//li_count = Messagebox('Info', 'Polis ini non-standard, Extra Premi Belum Ada !!!~n~nYakin Lanjutkan ?', Question!, Yesno!, 2)
								reas.setInfo(new Integer(4));
								//info=4;
							}
						}
					}
					reas.setLstbId(1);
					reas.setLspdId(27);
					HashMap proses = Common.serializableMap(reasIndividu.prosesReasUnderwriting(reas, null));

					suc_speedy.append("<br>- Proses Reas Berhasil!");
				}catch(Exception e){
					e.printStackTrace();
					e.getLocalizedMessage();
					error = 1;
					err_speedy.append("<br>- Proses Reas Error!");
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				}

				//Proses Akseptasi , langsung Accepted
				//(8 SEPT 2015) - apabila status akseptasi nya further, tidak perlu otomatis menjalankan akseptasi.(terkait dengan perubahan questionare kesehatan).
				//(7 OKT 2015) - apabila produk save series  lakukan proses akseptasi
				String lsbs_id = uwDao.selectBusinessId(s_spaj);
				Integer lsdbs_number = uwDao.selectBusinessNumber(s_spaj);
				if(bacDao.selectPositionSpaj(s_spaj) != 3 || products.powerSave(lsbs_id)){
					result_akseptasi = uwDao.prosesAkseptasiSpeedy(s_spaj, user.getLus_id());
					if(!result_akseptasi.equalsIgnoreCase("sukses")){
						err_speedy.append("<br>- Proses akseptasi error");
						error = 1;
					}else{
						//Udh Akseptasi , Update Flag done untuk NB
						uwDao.updateFlagAprove(s_spaj,1,"flag_approve_uw");
						uwDao.saveMstTransHistory(s_spaj, "tgl_approve_uw", nowDate, null, null);
						uwDao.insertMstPositionSpaj(user.getLus_id(),  "Approve By New Business/UW", s_spaj, 1);
						uwDao.prosesSnows(s_spaj, user.getLus_id(), 211, 212);
					}
				}
			}	
		}else if (flag==2){//Collection
			try{
				uwDao.updateFlagAprove(s_spaj,1,"flag_approve_collection");
				uwDao.saveMstTransHistory(s_spaj, "tgl_approve_collection", nowDate, null, null);
				uwDao.insertMstPositionSpaj(user.getLus_id(), "Approve By Collection", s_spaj, 0);
			}catch(Exception e){
				error = 1;
				err_speedy.append("<br>- Proses NB Error!");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
		}	
		
			//Proses transfer ke payment
			Transfer transfer=new Transfer();
			try{
				//formbacking
				transfer.setInsuredNo(new Integer(1));
				transfer.setSpaj(s_spaj);
				transfer.setInfo(new Integer(0));
				transfer.setLiPosisi(0);
				transfer.setLstbId(new Integer(1));
				transfer.setCurrentUser(user);
				//cek posisi
				HashMap mtPosisi = Common.serializableMap(uwDao.selectF_check_posisi(transfer.getSpaj()));
				Integer lspdIdTemp_transfer=(Integer)mtPosisi.get("LSPD_ID");
				String lspdPosTemp_transfer=(String)mtPosisi.get("LSPD_POSITION");
				transfer.setLspdId(lspdIdTemp_transfer);
				if(lspdIdTemp_transfer.intValue()!=27){
					transfer.setInfo(new Integer(1));
					transfer.setLsposDoc(lspdPosTemp_transfer);
					//posisi document ada di lspdPosTemp
				}
				//
				HashMap mtStatus = Common.serializableMap(uwDao.selectWfGetStatus(transfer.getSpaj(),transfer.getInsuredNo()));
				transfer.setLiAksep((Integer)mtStatus.get("LSSA_ID"));
				transfer.setLiReas((Integer)mtStatus.get("MSTE_REAS"));
				if (transfer.getLiAksep()==null) 
					transfer.setLiAksep(new Integer(1));
				transfer.setLiBackup((Integer)uwDao.selectMstInsuredMsteBackup(transfer.getSpaj(),transfer.getInsuredNo()));

				//
				if(transfer.getLiAksep().intValue()==2){
					transfer.setInfo(new Integer(2));
					//status spaj decline , Transfer Status Decline ke Policy Canceled ?
				}
				//dw1 //pemegang
				policy=uwDao.selectDw1Underwriting(transfer.getSpaj(),transfer.getLspdId(),transfer.getLstbId());
				if(policy!=null){
					transfer.setMspoPolicyHolder(policy.getMspo_policy_holder());
					transfer.setNoPolis(policy.getMspo_policy_no());
					transfer.setInsPeriod(policy.getMspo_ins_period());
					transfer.setPayPeriod(policy.getMspo_pay_period());
					transfer.setLkuId(policy.getLku_id());
					transfer.setUmurPp(policy.getMspo_age());
					transfer.setLcaId(policy.getLca_id());
					//

					//cek standard
					if(policy.getMste_standard().intValue()==1){
						Integer liCount=uwDao.selectCountMstProductInsuredCekStandard(transfer.getSpaj());
						if(liCount.intValue()==0){
							//Polis ini non-standard, Extra Premi Belum Ada !!!~n~nYakin Lanjutkan ?'
							transfer.setInfo(new Integer(3));
						}
					}
					transfer.setPolicy(policy);
				}
				//
				ArrayList lsProdInsured = Common.serializableList(akseptasiDao.selectMstProductInsured(transfer.getSpaj(),new Integer(1),new Integer(1)));
				String lsdbs_number = uwDao.selectLsdbsNumber(transfer.getSpaj());
				if(!lsProdInsured.isEmpty()){
					Product prodIns=(Product)lsProdInsured.get(0);
					transfer.setLsbsId(prodIns.getLsbs_id());
					transfer.setLsdbsNumber(prodIns.getLsdbs_number());
				}
				//
				transfer.setLiLama(uwDao.selectCountMstCancel(transfer.getSpaj()));
				transfer.setLsDp(uwDao.selectMstDepositPremium(transfer.getSpaj(),null));
				//
				if(transfer.getLsbsId().intValue()==157 || (transfer.getLsbsId().intValue()==196)&&lsdbs_number.equals("2")){//jika product endowment langsung transfer ke print polis 14-06-06
					transfer.setLiPosisi(6);
					transfer.setTo("Print Polis ?");
				}else{
					if (transfer.getLiLama().intValue() > 0){
						transfer.setLiPosisi(6);
						transfer.setTo("Print Polis ?");
						transfer.setLiLama(new Integer(1));
					}else{
						transfer.setLiPosisi(4);
						transfer.setTo("Pembayaran ?");
					}
				}	

				transfer.setInfo(new Integer(100));

				//data usulan asuransi
				HashMap mDataUsulan = Common.serializableMap(uwDao.selectDataUsulan(transfer.getSpaj()));
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
				//Map mTertanggung=elionsManager.selectTertanggung(transfer.getSpaj());
				transfer.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
				transfer.setMsteInsured((String)mTertanggung.get("MCL_ID"));
				transfer.setMsagId((String)mTertanggung.get("MSAG_ID"));
				transfer.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
				//end form backing
				
				Integer flagNew = 1;//(0=old, 1=New) proses pembayaran baru atao lama.
				int hasil = 0;
				String pesanTambahan = null;
				HashMap allResult = new HashMap();
				BindException err = null;
				//Update Data Reinstate (Jika NB yang Approve , Collection Not) , Update Jurnal TITIPAN PREMI (Jika Coll Approve , NB not)
				if(flag==1){
					allResult = Common.serializableMap(transferUw.prosesTransferPembayaranNewFlow(transfer,flagNew,err,request,flag,flag));
				}else if(flag==2){
					allResult = Common.serializableMap(transferUw.prosesTransferPembayaranNewFlow(transfer,flagNew,err,request,flag,flag));
				}
				if(!allResult.isEmpty()){
					if(!allResult.get("pesanErorr").toString().isEmpty()){
						error = 1;
						err_speedy.append("<br>- "+allResult.get("pesanErorr").toString());
					}else{
						suc_speedy.append("<br>- "+allResult.get("pesanTambahan").toString());
					}
				}
				
				hm_speedy=Common.serializableMap(ProsesTransferToPrintPolis(flag, s_spaj, i_flag_reg, pmg, tt, du, user, request, errors, elionsManager2, uwManager2,transfer));
					if(!hm_speedy.get("error").toString().isEmpty()){
						error = 1;
						err_speedy.append("<br>- "+hm_speedy.get("error").toString());
					}else{
						suc_speedy.append("<br>- "+hm_speedy.get("success").toString());
					}
				
			}catch(Exception e){
				error = 1;
				err_speedy.append("<br>- Proses Transfer ke Print Polis Error!");
				logger.error("ERROR :", e);
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
			
//			jika sukses, tampung value dalam success. jika gagal, tampung value dalam error.
			if(error>0){//ada error
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();

				//tambahan khusus dmtm
				if(pmg.getLca_id().equals("40")){
					prosesEmailNotify(s_spaj,pmg.getMcl_first(),1);	
					err_speedy.append("<br>- "+pmg.getReg_spaj()+" "+ "GAGAL DITRANSFER");
				}
			}else{//tidak ada error
				//   TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				//tambahan khusus dmtm
				if(pmg.getLca_id().equals("40")){
					prosesEmailNotify(s_spaj,pmg.getMcl_first(),2);	
					//map.put("success", pmg.getReg_spaj()+" "+ "Ditransfer Ke Payment(CLEAR CASE)");				;
					//return map;
					suc_speedy.append("<br>- "+pmg.getReg_spaj()+" "+ "Ditransfer Ke Print Polis (CLEAR CASE)");
				}
		 }
		
		map.put("success", suc_speedy);
		map.put("error", err_speedy);
		map.put("flag_error", error);
		map.put("flag_print", hm_speedy.get("flag_print").toString());
		
		return map;
	}
	
	public Map ProsesParalelUnclean(Integer flag , String s_spaj, Integer i_flag_reg, Pemegang pmg,Tertanggung tt, Datausulan du, User user,HttpServletRequest request, BindException errors,ElionsManager elionsManager2, UwManager uwManager2) throws Exception {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		HashMap map= new HashMap();
		StringBuffer err_speedy = new StringBuffer();
		StringBuffer suc_speedy = new StringBuffer();
		
		Policy policy = new Policy();
		Integer error = 0;
		Integer flag_print = 0;
		Integer uw_helpdesk = 0;
		Integer substandart = 0;
		Integer lspdIdTemp = 0;
		String lspdPosTemp="";
		String mdesc = "";
		String idSimultanPp = null;
		String idSimultanTt = null;
		DecimalFormat fmt = new DecimalFormat ("000");
		HashMap hm_speedy = new HashMap();
		Integer i_speedy = 1;
		String s_medis = "";
		Date nowDate = commonDao.selectSysdate();
		Map mTertanggung =uwDao.selectTertanggung(s_spaj);
		
		
			Transfer transfer=new Transfer();
			try{
				//Update Flag ApproveUW kondisi Unclean 
				uwDao.updateFlagAprove(s_spaj,1,"flag_approve_uw");
				uwDao.saveMstTransHistory(s_spaj, "tgl_approve_uw", nowDate, null, null);
				uwDao.insertMstPositionSpaj(user.getLus_id(),  "Approve By New Business/UW", s_spaj, 1);
				
				//formbacking
				transfer.setInsuredNo(new Integer(1));
				transfer.setSpaj(s_spaj);
				transfer.setInfo(new Integer(0));
				transfer.setLiPosisi(0);
				transfer.setLstbId(new Integer(1));
				transfer.setCurrentUser(user);
				//cek posisi
				HashMap mtPosisi = Common.serializableMap(uwDao.selectF_check_posisi(transfer.getSpaj()));
				Integer lspdIdTemp_transfer=(Integer)mtPosisi.get("LSPD_ID");
				String lspdPosTemp_transfer=(String)mtPosisi.get("LSPD_POSITION");
				transfer.setLspdId(lspdIdTemp_transfer);
				if(lspdIdTemp_transfer.intValue()!=27){
					transfer.setInfo(new Integer(1));
					transfer.setLsposDoc(lspdPosTemp_transfer);
					//posisi document ada di lspdPosTemp
				}
				//
				HashMap mtStatus = Common.serializableMap(uwDao.selectWfGetStatus(transfer.getSpaj(),transfer.getInsuredNo()));
				transfer.setLiAksep((Integer)mtStatus.get("LSSA_ID"));
				transfer.setLiReas((Integer)mtStatus.get("MSTE_REAS"));
				if (transfer.getLiAksep()==null) 
					transfer.setLiAksep(new Integer(1));
				transfer.setLiBackup((Integer)uwDao.selectMstInsuredMsteBackup(transfer.getSpaj(),transfer.getInsuredNo()));

				//
				if(transfer.getLiAksep().intValue()==2){
					transfer.setInfo(new Integer(2));
					//status spaj decline , Transfer Status Decline ke Policy Canceled ?
				}
				//dw1 //pemegang
				policy=uwDao.selectDw1Underwriting(transfer.getSpaj(),transfer.getLspdId(),transfer.getLstbId());
				if(policy!=null){
					transfer.setMspoPolicyHolder(policy.getMspo_policy_holder());
					transfer.setNoPolis(policy.getMspo_policy_no());
					transfer.setInsPeriod(policy.getMspo_ins_period());
					transfer.setPayPeriod(policy.getMspo_pay_period());
					transfer.setLkuId(policy.getLku_id());
					transfer.setUmurPp(policy.getMspo_age());
					transfer.setLcaId(policy.getLca_id());
					//

					//cek standard
					if(policy.getMste_standard().intValue()==1){
						Integer liCount=uwDao.selectCountMstProductInsuredCekStandard(transfer.getSpaj());
						if(liCount.intValue()==0){
							//Polis ini non-standard, Extra Premi Belum Ada !!!~n~nYakin Lanjutkan ?'
							transfer.setInfo(new Integer(3));
						}
					}
					transfer.setPolicy(policy);
				}
				//
				ArrayList lsProdInsured = Common.serializableList(akseptasiDao.selectMstProductInsured(transfer.getSpaj(),new Integer(1),new Integer(1)));
				String lsdbs_number = uwDao.selectLsdbsNumber(transfer.getSpaj());
				if(!lsProdInsured.isEmpty()){
					Product prodIns=(Product)lsProdInsured.get(0);
					transfer.setLsbsId(prodIns.getLsbs_id());
					transfer.setLsdbsNumber(prodIns.getLsdbs_number());
				}
				//
				transfer.setLiLama(uwDao.selectCountMstCancel(transfer.getSpaj()));
				transfer.setLsDp(uwDao.selectMstDepositPremium(transfer.getSpaj(),null));
				//
				if(transfer.getLsbsId().intValue()==157 || (transfer.getLsbsId().intValue()==196)&&lsdbs_number.equals("2")){//jika product endowment langsung transfer ke print polis 14-06-06
					transfer.setLiPosisi(6);
					transfer.setTo("Print Polis ?");
				}else{
					if (transfer.getLiLama().intValue() > 0){
						transfer.setLiPosisi(6);
						transfer.setTo("Print Polis ?");
						transfer.setLiLama(new Integer(1));
					}else{
						transfer.setLiPosisi(4);
						transfer.setTo("Pembayaran ?");
					}
				}	

				transfer.setInfo(new Integer(100));

				//data usulan asuransi
				HashMap mDataUsulan = Common.serializableMap(uwDao.selectDataUsulan(transfer.getSpaj()));
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
				transfer.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
				transfer.setMsteInsured((String)mTertanggung.get("MCL_ID"));
				transfer.setMsagId((String)mTertanggung.get("MSAG_ID"));
				transfer.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
				//end form backing
				hm_speedy=Common.serializableMap(ProsesTransferToPrintPolis(flag, s_spaj, i_flag_reg, pmg, tt, du, user, request, errors, elionsManager2, uwManager2,transfer));
				if(!hm_speedy.get("error").toString().isEmpty()){
					error = 1;
					err_speedy.append("<br>- "+hm_speedy.get("error").toString());
				}else{
					suc_speedy.append("<br>- "+hm_speedy.get("success").toString());
				}
			}catch(Exception e){
				error = 1;
				err_speedy.append("<br>- Proses Transfer ke Print Polis Error!");
				logger.error("ERROR :", e);
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
			
//			jika sukses, tampung value dalam success. jika gagal, tampung value dalam error.
			if(error>0){//ada error
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();

				//tambahan khusus dmtm
				if(pmg.getLca_id().equals("40")){
					prosesEmailNotify(s_spaj,pmg.getMcl_first(),1);	
					err_speedy.append("<br>- "+pmg.getReg_spaj()+" "+ "GAGAL DITRANSFER");
				}
			}else{//tidak ada error
				//   TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				//tambahan khusus dmtm
				if(pmg.getLca_id().equals("40")){
					prosesEmailNotify(s_spaj,pmg.getMcl_first(),2);	
					//map.put("success", pmg.getReg_spaj()+" "+ "Ditransfer Ke Payment(CLEAR CASE)");				;
					//return map;
					suc_speedy.append("<br>- "+pmg.getReg_spaj()+" "+ "Ditransfer Ke Print Polis (CLEAR CASE)");
				}
		 }
		
		map.put("success", suc_speedy);
		map.put("error", err_speedy);
		map.put("flag_error", error);
		map.put("flag_print", hm_speedy.get("flag_print").toString());
		
		return map;
	}

	public Map ProsesTransferToPrintPolis(Integer flag , String s_spaj, Integer i_flag_reg, Pemegang pmg,Tertanggung tt, Datausulan du, User user,HttpServletRequest request, BindException errors,ElionsManager elionsManager2, UwManager uwManager2, Transfer transfer) throws Exception {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		HashMap map= new HashMap();
		StringBuffer err_speedy = new StringBuffer();
		StringBuffer suc_speedy = new StringBuffer();
		
		Policy policy = new Policy();
		Integer error = 0;
		Integer flag_print = 0;
		DecimalFormat fmt = new DecimalFormat ("000");
		Integer i_speedy = 1;
		String s_medis = "";
		Date nowDate = commonDao.selectSysdate();
		Map mTertanggung =uwDao.selectTertanggung(s_spaj);
			try{
				Integer flagNew = 1;//(0=old, 1=New) proses pembayaran baru atao lama.
				int hasil = 0;
				String pesanTambahan = null;
				HashMap allResult = new HashMap();
				BindException err = null;
				//Cek Data Insured ALL untuk Ambil Flag Approve Collection & NB
				Map mInsured=uwDao.selectAllMstInsured(s_spaj);
				Integer flag_coll = 0;
				Integer flag_nb = 0;
				if(!mInsured.isEmpty())
				{	
					if((BigDecimal)mInsured.get("FLAG_APPROVE_COLLECTION")!=null)flag_coll=((BigDecimal)mInsured.get("FLAG_APPROVE_COLLECTION")).intValue();
					if((BigDecimal)mInsured.get("FLAG_APPROVE_UW")!=null)flag_nb=((BigDecimal)mInsured.get("FLAG_APPROVE_UW")).intValue();
				}

				if (flag_coll.intValue()==1 && flag_nb.intValue()==1 ){
					
					List billInfo = this.uwDao.selectBillingInfoForTransfer(s_spaj, 1, 1);
					Map mapBill = (HashMap) billInfo.get(0);
					Integer error_bill = 0;
					//cek ada selisih kurs/ kelebihan bayar
					Integer paid=(Integer)mapBill.get("MSBI_PAID");
					if(paid==0){
						if(Double.parseDouble(mapBill.get("MSBI_REMAIN").toString())>0){//masih kurang bayar
							error = 1;
							error_bill = 1;
							err_speedy.append("<br>- Ada Selisih Kurs( Kurang Bayar) Sebesar "+Double.parseDouble(mapBill.get("MSBI_REMAIN").toString()));
						}else if(Double.parseDouble(mapBill.get("MSBI_REMAIN").toString())<0){//kelebihan bayar
							error = 1;
							error_bill = 1;
							err_speedy.append("<br>- Ada Selisih Kurs( Lebih Bayar) Sebesar "+Double.parseDouble(mapBill.get("MSBI_REMAIN").toString()));
						}
					}
					//cek Untuk TOP-UP
					Integer countBilling = uwDao.selectCountMstBillingNB(s_spaj);
					if(countBilling>1){
						for(int i=2;i<=countBilling;i++){
							List billInfoTopUp = this.uwDao.selectBillingInfoForTransfer(s_spaj, 1, i);
							Map mapTopup = (HashMap) billInfoTopUp.get(0);
							Integer paidTopUP=(Integer)mapTopup.get("MSBI_PAID");
							if(paidTopUP==0){
								if(Double.parseDouble(mapTopup.get("MSBI_REMAIN").toString())>0){//masih kurang bayar
									error = 1;
									error_bill = 1;
									err_speedy.append("<br>- Ada kekurangan pembayaran TopUp sebesar "+mapTopup.get("LKU_SYMBOL")+mapTopup.get("MSBI_REMAIN")+" .");
								}else if(Double.parseDouble(mapTopup.get("MSBI_REMAIN").toString())<0){//kelebihan bayar
									error = 1;
									error_bill = 1;
									err_speedy.append("<br>- Ada kelebihan pembayaran TopUp sebesar "+mapTopup.get("LKU_SYMBOL")+mapTopup.get("MSBI_REMAIN")+" .");
								}
							}
						}
					}
					if(error_bill==0){
						//Transfer Ke Print Polis
						if(errors == null) errors = new BindException(new HashMap(), "");
						boolean hasil_to_printPolis = this.transferPolis.transferToPrintPolis(s_spaj, errors, -1, billInfo, user, "3", "3", "payment", elionsManager2, uwManager2);
						if(!hasil_to_printPolis) {
							try {
								error = 1;
								err_speedy.append("<br>- Proses Transfer ke Print Polis Error!");
								EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
										null, 0, 0, new Date(), null, 
										false, props.getProperty("admin.ajsjava"), new String[] {props.getProperty("admin.yusuf")}, null, null, 
										"GAGAL Transfer SPAJ " + s_spaj + " oleh "+ user.getName() + " ["+user.getDept()+"]", s_spaj+"\n"+errors, null, s_spaj);
							} catch (MailException e) {
								logger.error("ERROR :", e);
							}

						}else{							
								//start - untuk melakukan proses BTN - Link SMS
								//Ridhaal - Bank BTN - Pembuatan ShortUrl dengan melakukan pengeneratan file yang akan di simpan di link untuk di download oleh pemegang polis (SURL-1/2)
								//Metode ini di jalankan di proses BacManager - ProsesTransferToPrintPolis (SURL-1/2)
								//Metode juga di jalankan di proses TransferToPrintPolisFormController.java - onSubmit (SURL-2/2)
								List tmp = elionsManager2.selectDetailBisnis(s_spaj);
								String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
								String lsdbs_id = (String) ((Map) tmp.get(0)).get("DETBISNIS");
								if((lsbs.equals("195") &&  "013,014,015,016,017,018,019,020,021,022,023,024".indexOf(lsdbs_id) > 0 ) 
									|| (lsbs.equals("183") &&  "076,077,078,079,080,081,082,083,084,085,086,087,088,089,090".indexOf(lsdbs_id) > 0 )
									|| (lsbs.equals("169") &&  "034,035".indexOf(lsdbs_id) > 0 )
									|| (lsbs.equals("212") &&  "001".indexOf(lsdbs_id) > 0 )) 
								{
									
									Integer mspo_provider= uwManager2.selectGetMspoProvider(s_spaj);
									PdfReader reader = null;
									String cabang = elionsManager2.selectCabangFromSpaj(s_spaj);
									
									//untuk produk pas tergenerate pada saat pengiriman softcopy
									if (!lsbs.equals("187")){//non produk PAS	
										//generate report PolisAll
										 generateReport(request, mspo_provider, 3, elionsManager2, uwManager2, 1, s_spaj);
									}										
									
									//untuk set password IText PDF										 
									if (lsbs.equals("187")){//Produk PAS
										 reader = new PdfReader(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+s_spaj+"\\"+"polis_pas.pdf");												
									}else{//Non Produk PAS
										 reader = new PdfReader(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+s_spaj+"\\"+"PolisAll.pdf");
									}								 
									 
								     String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+s_spaj+"\\"+"PolisSMS.pdf";
								     PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
								     String PwsdPolisSMSPP = selectTglLhrPP(s_spaj);
								     stamp.setEncryption(PwsdPolisSMSPP.getBytes(), "417M1N".getBytes(), PdfWriter.AllowPrinting | PdfWriter.AllowCopy, PdfWriter.STRENGTH40BITS);
								     stamp.close(); 							     
								     FileUtils.deleteFile(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+s_spaj+"\\", "PolisAll.pdf");
								     
								     //Create link URL
								     String id =  elionsManager2.selectEncryptDecrypt(s_spaj, "en");
								     //cek jika hasil enkripsi SPAJ ada tanda #, jika ada di encrypsi ulang karena longURL Id tidak boleh ada tanda #
								     while(id.contains("#")){
								    	 id =  elionsManager2.selectEncryptDecrypt(s_spaj, "en");
								     }							     
								   
								     String LongURL   = "https://elions.sinarmasmsiglife.co.id/common/util.htm?window=publics&id="+id;
								   //  String LongURL   = "http://localhost:8080/E-Lions/common/util.htm?window=publics&id="+id;
								     
								     //pemendekan URL untuk di SMS (URLSHORTENER) menggunakan Google	
								     //cek jika shortlink sudah ada atau belom di data base EKA.MST_URL_SECURE_FILE
								     List Flag_linkBtn = cekUrlSecureFile(s_spaj, "LINK SMS BTN" );
								     if (Flag_linkBtn.isEmpty()){
									     String ShortURL = prosesUrlShortener(LongURL); 
									     logger.info(prosesUrlShortener(LongURL));	
									     Date sysdate = commonDao.selectSysdateTrunc();
									     insertUrlSecureFile(sysdate ,s_spaj, "LINK SMS BTN", 1, id , LongURL, null, ShortURL);	
									     uwDao.insertMstPositionSpaj(user.getLus_id(), "Proses Pengeneretan Short URL : " + ShortURL , s_spaj, 1);
											
								     }
							
							} //end - untuk melakukan proses BTN - Link SMS
							
							flag_print=1;
							suc_speedy.append("<br>- Proses Transfer ke Print Polis Berhasil!");
						}
					}
				}else{
					allResult = Common.serializableMap(transferUw.prosesTransferPembayaranNewFlow(transfer, flagNew, err, request,3,flag));
					pesanTambahan = (String) allResult.get("pesanTambahan");
					hasil = (Integer) allResult.get("proses");
					if(!pesanTambahan.isEmpty()){
						suc_speedy.append(pesanTambahan);
					}
				}
				
			}catch(Exception e){
				error = 1;
				err_speedy.append("<br>- Proses Transfer ke Print Polis Error!");
				logger.error("ERROR :", e);
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
			
		map.put("success", suc_speedy);
		map.put("error", err_speedy);
		map.put("flag_error", error);
		map.put("flag_print", flag_print);
		
		return map;
	}
	
	public Map validasiNewBusiness(String spaj ,User currentUser, Integer flag) throws ParseException {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		HashMap map= new HashMap();
		Double ldecPremi = null,ldecBill = null,ldecPremiTopUp;
		StringBuffer err_speedy = new StringBuffer();
		List bill;
		List billInfo = this.uwDao.selectBillingInfoForTransfer(spaj, 1, 1);
		Datausulan dataUsulan = bacDao.selectDataUsulanutama(spaj);
		Pemegang pmg = bacDao.selectpp(spaj);
		Tertanggung tertanggung = bacDao.selectttg(spaj);
		HashMap mTertanggung = Common.serializableMap(uwDao.selectTertanggung(spaj));
		Date jan2016 = defaultDateFormat.parse(props.getProperty("jan.2016"));
		List lds_dp=uwDao.selectMstDepositPremium(spaj,null);
		Map mInsured=uwDao.selectAllMstInsured(spaj);
		String businessId = FormatString.rpad("0", this.uwDao.selectBusinessId(spaj), 2);
		String businessNumber = uwDao.selectLsdbsNumber(spaj);
		Integer lssa_id = uwDao.selectStatusAksep(spaj);
		Integer lssa_id_bas = uwDao.selectStatusFurtherColection(spaj);
		if (lssa_id_bas == null){lssa_id_bas = 0;}
		Integer flag_coll = 0;
		Integer flag_nb = 0;
						
		if(!mInsured.isEmpty())
		{	
			if((BigDecimal)mInsured.get("FLAG_APPROVE_COLLECTION")!=null)flag_coll=((BigDecimal)mInsured.get("FLAG_APPROVE_COLLECTION")).intValue();
			if((BigDecimal)mInsured.get("FLAG_APPROVE_UW")!=null)flag_nb=((BigDecimal)mInsured.get("FLAG_APPROVE_UW")).intValue();
		}
		if (flag==1){//validasi untuk New Business
			if (lssa_id==3){
				err_speedy.append("<br>- No Spaj "+spaj+" Masih FURTHER , Mohon Di ubah ke INPUT SPAJ or PROSES SPAJ.");
				map.put("",err_speedy);
			}
			
			if (lssa_id==8){
				err_speedy.append("<br>- No Spaj "+spaj+" Masih Berstatus FUND , Mohon Di Accept Terlebih Dahulu");
				map.put("",err_speedy);
			}
			
			if (flag_nb==1){
				err_speedy.append("<br>- No Spaj "+spaj+" Sudah Pernah Di Approve Sebelumnya.");
				map.put("",err_speedy);
			}else{
				//Selain Powersave Series
				if(!products.powerSave(dataUsulan.getLsbs_id().toString()) && !products.unitLink(dataUsulan.getLsbs_id().toString()) && ("183,189,193,201,214".indexOf(dataUsulan.getLsbs_id().toString())< 0 )){
					if(billInfo.size()!=0){
						ldecPremi = uwDao.selectMstProductInsuredPremiDiscount(spaj,1,1);
						bill = akseptasiDao.selectMstDetBilling(spaj, 1, 1);
						if(!bill.isEmpty()){
							double ldecBills = 0.0;
							ldecBill = 0.0;
							for(int i=0; i<bill.size(); i++){
								DetBilling x = (DetBilling) bill.get(i);
								ldecBills = x.getMsdb_premium();
								ldecBill += ldecBills;
							}
						}
						//compare
						if(ldecPremi.intValue()!=ldecBill.intValue()){
							err_speedy.append("<br>- Jumlah Premi Pada Polis, Tidak Sesuai Dengan Jumlah Premi Pada Collection(Tagihan).");
							map.put("",err_speedy);
						}
					}
				} 
				
				//Smile Medical Series dan Provestara
				if(("183,189,193,201,214".indexOf(dataUsulan.getLsbs_id().toString())>-1 )){
					if(billInfo.size()!=0){
						Double billsehat=0.0;
						ldecPremi = bacDao.selectTotalKomisiEkaSehat(spaj);
						bill = akseptasiDao.selectMstDetBilling(spaj, 1, 1);
						if(!bill.isEmpty()){
							for (int i=0;i<bill.size();i++){
								DetBilling a=(DetBilling) bill.get(i);
								ldecBill=a.getMsdb_premium();
								billsehat+=ldecBill;
							}
						}
						//compare
						if(ldecPremi.intValue()!=billsehat.intValue()){
							err_speedy.append("<br>- Jumlah Premi Pada Polis, Tidak Sesuai Dengan Jumlah Premi Pada Collection(Tagihan).");
							map.put("",err_speedy);
						}
					}
				}
				//Powersave Series
				if(products.powerSave(dataUsulan.getLsbs_id().toString())){
					List<Map> listRiderPSave = bacDao.selectRiderSave(spaj);
					if(listRiderPSave.size()>0){
						Powersave daftarPSave = (Powersave) this.bacDao.select_powersaver(spaj);
						if(Integer.parseInt(dataUsulan.getLsbs_id().toString())==188){
							daftarPSave = (Powersave) this.bacDao.select_powersaver_baru(spaj);
						}
						bill=akseptasiDao.selectMstDetBilling(spaj, 1, 1);
						if(!bill.isEmpty()){
							DetBilling a=(DetBilling) bill.get(0);
							ldecBill=a.getMsdb_premium();
						}
						if(daftarPSave!=null){
							ldecPremi=daftarPSave.getMps_prm_deposit();
						}

						//compare
						if(ldecPremi.intValue()!=ldecBill.intValue()){
							err_speedy.append("<br>- Jumlah Premi Pada Polis, Tidak Sesuai Dengan Jumlah Premi Pada Collection(Tagihan).");
							map.put("",err_speedy);
						}
					}else{
						if(billInfo.size()!=0){
							ldecPremi = uwDao.selectMstProductInsuredPremiDiscount(spaj,1,1);
							bill=akseptasiDao.selectMstDetBilling(spaj, 1, 1);
							if(!bill.isEmpty()){
								DetBilling a=(DetBilling) bill.get(0);
								ldecBill=a.getMsdb_premium();
							}
							//compare
							if(ldecBill!=null){
								if(ldecPremi.intValue()!=ldecBill.intValue()){
									err_speedy.append("<br>- Jumlah Premi Pada Polis, Tidak Sesuai Dengan Jumlah Premi Pada Collection(Tagihan).");
									map.put("",err_speedy);
								}
							}
						}
					}

				}

				//stable link diambil dari MST_SLINK
				if(products.stableLink(dataUsulan.getLsbs_id().toString())) {
					List<Map> daftarStableLink = uwDao.selectInfoStableLink(spaj);
					for(Map info : daftarStableLink) {
						int msl_no = ((BigDecimal) info.get("MSL_NO")).intValue(); 

						ldecPremi= ((BigDecimal) info.get("MSL_PREMI")).doubleValue();

						bill=akseptasiDao.selectMstDetBilling(spaj, 1, msl_no);
						if(!bill.isEmpty()){
							DetBilling a=(DetBilling) bill.get(0);
							ldecBill=a.getMsdb_premium();
						}
						//compare
						if(ldecBill!=null){
							if(ldecPremi.intValue()!=ldecBill.intValue()){
								err_speedy.append("<br>- Jumlah Premi Pada Polis, Tidak Sesuai Dengan Jumlah Premi Pada Collection(Tagihan).");
								map.put("",err_speedy);
							}
						}
					}	
				}

				if(products.unitLink(dataUsulan.getLsbs_id().toString())) {
					//Pokok
					if(billInfo.size()!=0){
						ldecPremi = uwDao.selectMstProductInsuredPremiDiscount(spaj,1,1);
						bill=akseptasiDao.selectMstDetBilling(spaj, 1, 1);
						if(!bill.isEmpty()){
							DetBilling a=(DetBilling) bill.get(0);
							ldecBill=a.getMsdb_premium();
						}
						//compare
						if(ldecPremi.intValue()!=ldecBill.intValue()){
							err_speedy.append("<br>- Jumlah Premi Pokok Pada Polis, Tidak Sesuai Dengan Jumlah Premi Pokok Pada Collection(Tagihan).");
							map.put("",err_speedy);
						}
					}

					//Top Up
					List daftarTopUp = uwDao.selectMstUlinkTopupNewForDetBilling(spaj);
					Integer topupBerkala = this.uwDao.validationTopup(spaj);
					Integer countBilling = uwDao.selectCountMstBillingNB(spaj);
					if(daftarTopUp.size()>0){
						for(int i=0;i<daftarTopUp.size();i++){
							Map mapTopup = (HashMap) daftarTopUp.get(i);
							ldecPremi = (Double) mapTopup.get("MU_JLH_PREMI");
							Integer premiKe = (Integer) mapTopup.get("MU_PREMI_KE");
							bill=akseptasiDao.selectMstDetBilling(spaj, 1, premiKe);
							if(!bill.isEmpty()){
								DetBilling a=(DetBilling) bill.get(0);
								ldecBill=a.getMsdb_premium();
							}
							//compare
							if(ldecBill!=null){
								if(ldecPremi.intValue()!=ldecBill.intValue()){
									err_speedy.append("<br>- Jumlah Top Up Pada Polis, Tidak Sesuai Dengan Jumlah Top Up Pada Collection(Tagihan).");
									map.put("",err_speedy);
								}
							}
						}
					}	
				}
			}

			if(uwDao.selectCountKoefisienUpp(businessId, businessNumber)<=0){
				err_speedy.append("<br>- Rate UPP Kode Plan ("+businessId+"-"+businessNumber+") belum dimasukkan, Silakan menghubungi IT mengenai perihal ini.");
				map.put("",err_speedy);
			}

			List<DrekDet> mstDrekDetBasedSpaj = uwDao.selectMstDrekDet( null, spaj, null, null );
			List<Payment> payment = this.uwDao.selectPaymentCount(spaj, 1, 1);
			Account_recur account_recur = bacDao.select_account_recur(spaj);
			if(!payment.isEmpty()){
				if(payment.get(0).getLsrek_id()!=0){
					if(mstDrekDetBasedSpaj.size()==0){
						if(account_recur.getFlag_autodebet_nb() != null){
							if(account_recur.getFlag_autodebet_nb()!=1){
								err_speedy.append("<br>- Silakan menginput I-Bank terlebih dahulu sebelum transfer. Apabila Ibank tidak ada, silakan dikonfirmasikan ke pihak Finance.");
								map.put("",err_speedy);
							}
						}
					}
				}
			}
		}else{
			//cek apakah status further collection ato tidak
			if(lssa_id_bas == 18 ){
				err_speedy.append("<br>- No Spaj "+spaj+" Masih status FURTHER COLECTION ,Silahkan melakukan proses akseptasi terlebih dahulu di menu STATUS FURTHER COLECTION. ");
				map.put("",err_speedy);
			}
			
			if(lds_dp.size()==0){
				err_speedy.append("<br>- Jumlah Titipan Premi/Payment Belum Di Input.Silakan diinput terlebih dahulu.");
				map.put("",err_speedy);
			}
			if(pmg.getMspo_input_date().before(jan2016)){
				err_speedy.append("<br>- Proses Ini Hanya Berlaku Untuk Polis > " + jan2016);
				map.put("",err_speedy);
			}
			if(flag_coll==1){
				err_speedy.append("<br>- No Spaj "+spaj+" Sudah Pernah Di Approve Sebelumnya.");
				map.put("",err_speedy);
			}
			if(uwDao.selectCountKoefisienUpp(businessId, businessNumber)<=0){
				err_speedy.append("<br>- Rate UPP Kode Plan ("+businessId+"-"+businessNumber+") belum dimasukkan, Silakan menghubungi IT mengenai perihal ini.");
				map.put("",err_speedy);
			}
			
			List<DrekDet> mstDrekDetBasedSpaj = uwDao.selectMstDrekDet( null, spaj, null, null );
			List<Payment> payment = this.uwDao.selectPaymentCount(spaj, 1, 1);
			Account_recur account_recur = bacDao.select_account_recur(spaj);
			if(!payment.isEmpty()){
				if(payment.get(0).getLsrek_id()!=0){
					if(mstDrekDetBasedSpaj.size()==0){
						if(account_recur.getFlag_autodebet_nb() != null){
							if(account_recur.getFlag_autodebet_nb()!=1){
								err_speedy.append("<br>- Silakan menginput I-Bank terlebih dahulu sebelum transfer. Apabila Ibank tidak ada, silakan dikonfirmasikan ke pihak Finance.");
								map.put("",err_speedy);
							}
						}
					}
				}
			}
		}
		return map;
	}
	
	public void generateReport(HttpServletRequest request, Integer mspoProvider, int flagPrePrinted, ElionsManager elionsManager2, UwManager uwManager2, int jnNSR, String vSpaj) throws Exception{
	
		String spaj = "";
		String jpu = "";
		
		//flag jnNSR (flag yang nenentukan apakah mengambil value dari HttpServletRequest atau (lemparan dari controller lain) )
		//jika jnNSR = 0 (mengacu ke HttpServletRequest request dimana nilai/value langsung di ambil dari JSP) 
		//jika jnNSR = 1 (nila/ value mrupakan lemparan dari controllan lain bukan langsung dari JSP)
		if (jnNSR == 0){
			spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			jpu = ServletRequestUtils.getStringParameter(request, "seq", "");
		}else if (jnNSR == 1){
			spaj = vSpaj;
		}

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String cabang = elionsManager2.selectCabangFromSpaj(spaj);
		String businessId = FormatString.rpad("0", uwManager2.selectBusinessId(spaj), 3);
		Integer businessNumber = uwManager2.selectBusinessNumber(spaj);
		
		Integer punyaSimascard = 0;
		String flagUlink ="0";
		//1. Polis
		Map paramsPolis = printPolis(spaj, request, PrintPolisMultiController.POLIS_QUADRUPLEX , elionsManager2, uwManager2);
		
		//2. Manfaat
		Map paramsManfaat = elionsManager2.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
		String pathManfaat = (String) paramsManfaat.get("reportPath");
		pathManfaat = pathManfaat.substring(17);
		paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
		paramsManfaat.remove("reportPath");
		paramsManfaat.put("lsdbs", businessNumber);
		paramsPolis.putAll(paramsManfaat);
		
		paramsPolis.put("koneksi", this.uwDao.getDataSource().getConnection());
		List temp = new ArrayList(); Map map = new HashMap();
		map.put("halaman", "1"); temp.add(map);//polis
		map.put("halaman", "2"); temp.add(map);//manfaat
		map.put("halaman", "3"); temp.add(map);//surat
		if(products.unitLink(uwDao.selectBusinessId(spaj))) {
			map.put("halaman", "4"); temp.add(map);//kosong
			map.put("halaman", "5"); temp.add(map);//alokasi dana (bisa 1 / 2 halaman)
		}
		if(products.unitLink(uwManager2.selectBusinessId(spaj))) {//surat simas card
			map.put("halaman", "7"); temp.add(map);
		}else{
			map.put("halaman", "4"); temp.add(map);
		}
		map.put("halaman", "6"); temp.add(map);
		map.put("halaman", "8"); temp.add(map);
		map.put("halaman", "9"); temp.add(map);
		map.put("halaman", "10"); temp.add(map);
		paramsPolis.put("dsManfaat", JasperReportsUtils.convertReportData(temp));
		
		//3. Surat Polis
		String va = uwManager2.selectVirtualAccountSpaj(spaj);
		if(cabang.equals("58")){
			if(elionsManager2.selectValidasiTransferPbp(spaj)==0){//ini mste_flag_cc
				if(va==null){
					uwManager2.updateVirtualAccountBySpaj(spaj);
				}
			}
		}
		
		Map data = uwManager2.selectDataVirtualAccount(spaj);
		String lku_id = (String) data.get("LKU_ID");
		Integer lscb_id = ((BigDecimal) data.get("LSCB_ID")).intValue();
		Integer flag_cc = elionsManager2.select_flag_cc(spaj);
		
		if(lku_id.equals("01") && !products.syariah(businessId,businessNumber.toString()) && lscb_id != 0 && flag_cc ==0 && !businessId.equals("196")&& !(businessId.equals("190") && "5,6".indexOf(businessNumber.toString())>=0) ){
			
			List cekSpajPromo = selectCekSpajPromo(null, spaj, "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
			
			if(!cekSpajPromo.isEmpty()){
				paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va_promo") + ".jasper");
			}else if(businessId.equals("217") && businessNumber== 2){ //untuk ERbe di set menggunakan surat_polis
				paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
			}else{
				paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
			}
			
			//paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
		}else if(products.syariah(businessId, businessNumber.toString())){
			if(flag_cc==0 && !businessId.equals("175")){
				paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah_va") + ".jasper");
			}else{
				paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
			}
		}else if(cabang.equals("58")){
			paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.mall") + ".jasper");
		}
		else{
			if(businessId.equals("196")&& businessNumber==002){
				paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.term") + ".jasper");
			}else{
				paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
			}
		}
		
		paramsPolis.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid (Yusuf - 04/05/2006)

		
		//4. Alokasi Dana
		List viewUlink = elionsManager2.selectViewUlink(spaj);
		if(viewUlink.size()!=0){
			Map paramsAlokasi = elionsManager2.cetakSuratUnitLink(viewUlink, spaj, true, 1, 1,0);
			paramsAlokasi.put("elionsManager", elionsManager2);
			
			String pathAlokasi = (String) paramsAlokasi.get("reportPath");
			pathAlokasi = pathAlokasi.substring(17);
			paramsAlokasi.put("pathAlokasiDana", pathAlokasi + ".jasper");
			paramsAlokasi.remove("reportPath");
			paramsAlokasi.put("dsAlokasiDana", JasperReportsUtils.convertReportData(viewUlink));
			
			paramsPolis.putAll(paramsAlokasi);
		}
		
		
		//5 Tanda Terima Polis
		Integer referal = ServletRequestUtils.getIntParameter(request, "referal", 0);
		
		List detBisnis = elionsManager2.selectDetailBisnis(spaj);
		String lsbs_id = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
//		String lsbs_id = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
		String reportPath;
		String namaFile = props.getProperty("pdf.tanda_terima_polis");
		int isInputanBank = elionsManager2.selectIsInputanBank(spaj); 
		if(((isInputanBank == 2 || isInputanBank == 3) && referal == 0 && !products.productBsmFlowStandardIndividu(Integer.parseInt(lsbs_id), Integer.parseInt(lsdbs))) || (lsbs_id.equals("175")&&lsdbs.equals("002"))) {
			reportPath = props.getProperty("report.tandaterimasertifikat");
			namaFile = props.getProperty("pdf.tanda_terima_sertifikat");
		}else if(products.unitLink(lsbs_id) && !products.stableLink(lsbs_id)) {
			if(products.syariah(lsbs_id, lsdbs)){
				reportPath = props.getProperty("report.tandaterimapolis.syariah");
			}else{
				reportPath = props.getProperty("report.tandaterimapolis.link");
			}
		}else if(lsbs_id.equals("187")){
			reportPath = props.getProperty("report.tandaterimapolis.pas");
		}else {
			reportPath = props.getProperty("report.tandaterimapolis"); //ini udah include biasa + syariah
		}
		
		paramsPolis.put("referal", referal);
		paramsPolis.put("pathTandaTerimaPolis", reportPath + ".jasper");
		
		 	
		if(flagPrePrinted==2){
			
			List daftarSebelumnya = uwManager2.selectSimasCard(spaj);
			List isAgen = uwManager2.selectIsSimasCardClientAnAgent(spaj);
			
			if(!daftarSebelumnya.isEmpty() && isAgen.isEmpty()){
				Map SimasCardSebelumnya = (Map) daftarSebelumnya.get(0);
				if(!Common.isEmpty(SimasCardSebelumnya.get("REG_SPAJ"))){
					if(SimasCardSebelumnya.get("REG_SPAJ").equals(spaj)){
						punyaSimascard=1;
					}else{
						elionsManager2.insertMstPositionSpaj(currentUser.getLus_id(), "SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO SPAJ"+ SimasCardSebelumnya.get("REG_SPAJ"), spaj, 0);
					}
				}
			}	
				
		}
		String reportPathSimasCard = props.get("report.surat_simcard").toString();
		paramsPolis.put("pathSuratSimasCard", reportPathSimasCard + ".jasper");
		
		for(Iterator iter = paramsPolis.keySet().iterator(); iter.hasNext();){
			String nama = (String) iter.next();
			logger.info(nama + " = " + paramsPolis.get(nama));
		}
		
		//SSU	
		//update 19 November 2015 Semua SSU produk utama tidak diikutsertakan(REQ TIMMY-Hanya rider yg diikutsertakan)
		if(	PDFViewer.checkFileProduct(elionsManager2, uwManager2, props, spaj));{
			List<File> pdfFiles = new ArrayList<File>();
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;						
		    String dirSsk = props.getProperty("pdf.dir.syaratpolis");					
		    for(int i=0; i<detBisnis.size(); i++) {
				Map m = (HashMap) detBisnis.get(i);
				File file = PDFViewer.riderFile(elionsManager2,uwManager2, dirSsk, lsbs_id, lsdbs, m,spaj, props);
				if(file!=null) if(file.exists()) pdfFiles.add(file);
			}
			PdfUtils.combinePdf(pdfFiles, exportDirectory, "pathSS.pdf");
		}
		
		if(flagPrePrinted==3){ // khusus produk BTN
			//SSU untuk produk BTN (SSU + SSK)
			if(	PDFViewer.checkFileProduct(elionsManager2, uwManager2, props, spaj));{
				List<File> pdfFiles = new ArrayList<File>();
				String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;						
			    String dirSsk = props.getProperty("pdf.dir.syaratpolis");	
			   
			    for(int i=0; i<detBisnis.size(); i++) {
					Map m = (HashMap) detBisnis.get(i);
					File file =  PDFViewer.productFile(elionsManager2, uwManager2, dirSsk, spaj, m,props);
					if(file!=null) if(file.exists()) pdfFiles.add(file);
				}
			    
			    for(int i=0; i<detBisnis.size(); i++) {
					Map m = (HashMap) detBisnis.get(i);
					File file = PDFViewer.riderFile(elionsManager2,uwManager2, dirSsk, lsbs_id, lsdbs, m,spaj, props);				
					if(file!=null) if(file.exists()) pdfFiles.add(file);
				}		    
			   
				PdfUtils.combinePdf(pdfFiles, exportDirectory, "pathSSU_SSK.pdf");
			}
			
			List tmp = elionsManager2.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
			String lsdbs_id = (String) ((Map) tmp.get(0)).get("DETBISNIS");
			if((lsbs.equals("195") &&  "013,014,015,016,017,018,019,020,021,022,023,024".indexOf(lsdbs_id) > 0 ) 
				|| (lsbs.equals("183") &&  "076, 077,078,079,080,081,082,083,084,085,086,087,088,089,090".indexOf(lsdbs_id) > 0 )) 
			{
				//SURAT PENJAMINAN PROVIDER
				File file = generateSuratPengajuanProvider(elionsManager2,uwManager2, spaj, props);	
				List<File> pdfFiles = new ArrayList<File>();
				String exportDirect = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;	
				if(file!=null) if(file.exists()) pdfFiles.add(file);
				PdfUtils.combinePdf(pdfFiles, exportDirect, "pathSPP.pdf");
			}
		}
		
		if(flagPrePrinted==4){ // khusus produk SMiLe Progressive Save Inti
			//SSU
			if(	PDFViewer.checkFileProduct(elionsManager2, uwManager2, props, spaj));{
				List<File> pdfFiles = new ArrayList<File>();
				String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;						
			    String dirSsk = props.getProperty("pdf.dir.syaratpolis");	
			   
			    for(int i=0; i<detBisnis.size(); i++) {
					Map m = (HashMap) detBisnis.get(i);
					File file =  PDFViewer.productFile(elionsManager2, uwManager2, dirSsk, spaj, m, props);
					if(file!=null) if(file.exists()) pdfFiles.add(file);
				}    
				PdfUtils.combinePdf(pdfFiles, exportDirectory, "pathSSU.pdf");
			}
		}

			
		//ENDORS SMILE MEDICAL
		if(mspoProvider==2 && flagPrePrinted != 3 && flagPrePrinted != 4){
			PrintPolisPerjanjianAgent printPolis = new PrintPolisPerjanjianAgent();
			List<String> pdfs = new ArrayList<String>();
			Boolean suksesMerge = false;
			Boolean scFile=false;
			String endorsPolis = "";
			String Kartuadmedika = "";
			String Pesertaadmedika = "";
			String provider = "";
			String filename = "pathAdmedika";
			Date sysdate = commonDao.selectSysdateTrunc();
			Boolean syariah = products.syariah(lsbs_id, lsdbs);
			
			Integer ekaSehatBaru = bacDao.selectCountEkaSehatAdmedikaNew(spaj,0);
			Integer ekaSehatHCP = bacDao.selectCountEkaSehatAdmedikaHCP(spaj);
			Integer ekaSehatPlus = bacDao.selectCountEkaSehatAdmedikaNew(spaj,1);
			Integer s=Integer.parseInt(lsdbs.substring(1));
			Integer punyaEndorsAdmedika = selectPunyaEndorsEkaSehatAdmedika(spaj);
			
			if(punyaEndorsAdmedika == 0)prosesEndorsKetinggalanNew(spaj, Integer.parseInt(lsbs_id));
			
			if(ekaSehatBaru>=1){
				if(lsbs_id.equals("189")|| products.syariah(lsbs_id, lsdbs)){
					if(lsbs_id.equals("189") && Integer.parseInt(lsdbs.substring(1))>15){
						endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariahSMP.pdf";
					}else{
						endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariah.pdf";
					}
				}else{
					endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedical.pdf"; // EndorsemenPolisBaru
				}
			}else if(ekaSehatPlus>=1){				
				endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedicalPlus.pdf";
			}else{
				endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolis.pdf";
			}
			if(ekaSehatHCP>=1 && ekaSehatBaru<1 && ekaSehatPlus<1){
				Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuHCP.pdf";
				provider = props.getProperty("pdf.template.admedika")+"\\ProviderHCP.pdf";
			}else{
				Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuAdmedika.pdf";
				provider = props.getProperty("pdf.template.admedika")+"\\Provider.pdf";
			}
			Pesertaadmedika = props.getProperty("pdf.template.admedika")+"\\PesertaAdmedika.pdf";
			
			if (ekaSehatBaru>=1 || ekaSehatPlus>=1){
				pdfs.add(endorsPolis);
			}
			if(flagPrePrinted==1){
				//pdfs.add(Kartuadmedika);
				//pdfs.add(provider);
				if(businessId.equals("183")){
					pdfs.remove(0);
					pdfs.add(0, Kartuadmedika);
				}
			}
			Integer total_ekasehat = bacDao.getJumlahEkaSehat(spaj);
			List<Map> datapeserta = uwDao.selectDataPeserta(spaj);
			
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
			
			if(ekaSehatHCP>=1){
				OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaHCP.pdf");
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}else if(lsbs_id.equals("189") || products.syariah(lsbs_id, lsdbs)){
				OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatSyariah.pdf");
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}else if(ekaSehatPlus>=1){
				OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaMedicalPlus.pdf");
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}else{
				// Andhika
				OutputStream output;
				if(businessId.equals("183")){
					output = new FileOutputStream(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+filename+".pdf");
				}else{
					output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatNew.pdf");
				}
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}		
			
			String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+filename+".pdf";
			
			if(!scFile) {
				Map dataAdmedika = uwDao.selectDataAdmedika(spaj);
				if(dataAdmedika==null){
					uwManager2.prosesEndorsemen(spaj, Integer.parseInt(lsbs_id),0);
					dataAdmedika = uwDao.selectDataAdmedika(spaj);
				}
				String ingrid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
				if(!businessId.equals("183")){
					printPolis.generateEndorseAdmedikaEkaSehat(spaj,total_ekasehat, outputName, dataAdmedika, datapeserta, sysdate, ingrid,lsbs_id,lsdbs,syariah,ekaSehatHCP,ekaSehatBaru,ekaSehatPlus);
				}
			}
		}
		if(products.unitLink(uwManager2.selectBusinessId(spaj))) {
			flagUlink="1";
		}
		
		String path ="";
		path = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
		String pathTemplate = props.getProperty("pdf.template")+"\\BlankPaper.pdf";
		Connection conn = null;
		try {
			conn = this.uwDao.getDataSource().getConnection();
			JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathSurat").toString(), path, "pathSurat.pdf", paramsPolis, conn);
			JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathManfaat").toString(), path, "pathManfaat.pdf", paramsPolis, conn);
			JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathPolis").toString(), path, "pathPolis.pdf", paramsPolis, conn);
			JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathTandaTerimaPolis").toString(), path, "pathTandaTerimaPolis.pdf", paramsPolis, conn);
			if(viewUlink.size()!=0) JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathAlokasiDana").toString(), path, "pathAlokasiDana.pdf", paramsPolis, conn);
			if(flagPrePrinted==2) JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathSuratSimasCard").toString(), path, "pathSuratSimasCard.pdf", paramsPolis, conn);
			Print.generateReportMergeAndDelete(cabang, path, pathTemplate, lsbs_id, mspoProvider, flagUlink, punyaSimascard, flagPrePrinted);
		}catch(Exception e){
	        this.uwDao.closeConn(conn);
	        throw e;
		}finally{
		    this.uwDao.closeConn(conn);
		}
	}
	
	public Map printPolis(String spaj, HttpServletRequest request, int singleDuplexQuadruplex , ElionsManager elionsManager2, UwManager uwManager2) {
		Map params = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		//String a = props.toString();
		params.put("spaj", spaj);
		params.put("props", props);
		String validasiMeterai = validasiBeaMeterai(currentUser.getJn_bank());
		if(validasiMeterai != null){
			params.put("meterai", null);
			params.put("izin", "");
		}else{
			params.put("meterai", "Rp. 6.000,-");
			params.put("izin", uwDao.selectIzinMeteraiTerakhir());
		}
		
		//jenis print ulang
		int jpu = ServletRequestUtils.getIntParameter(request, "jpu", -1);
		
		if(jpu == 1) { //PRINT ULANG POLIS
			params.put("tipePolis", "O R I G I N A L");
		}else if(jpu == 2) { //PRINT DUPLIKAT POLIS
			String seq = ServletRequestUtils.getStringParameter(request, "seq", "");
			params.put("tipePolis", "D U P L I C A T E " + "("+seq+")");
			
		}else if(jpu == 3) { //PRINT ULANG POLIS
			params.put("tipePolis", "O R I G I N A L");
		}
		params.put("ingrid", props.get("images.ttd.direksi")); //ttd dr. ingrid (Yusuf - 04/05/2006)
		
		String kategori = products.kategoriPrintPolis(spaj);
		//String kategori = "guthrie";
		logger.info("JENIS POLIS : " + kategori);

		if(PrintPolisMultiController.POLIS == singleDuplexQuadruplex) { //single
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis."+kategori));
		}else if(PrintPolisMultiController.POLIS_DUPLEX == singleDuplexQuadruplex) { //duplex
			params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.duplex"));
		}else if(PrintPolisMultiController.POLIS_QUADRUPLEX == singleDuplexQuadruplex) { //quadruplex
			params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex"));
		}else if(PrintPolisMultiController.POLIS_QUADRUPLEX_PLUS_HADIAH == singleDuplexQuadruplex) { //quadruplex
			params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex_plus_hadiah"));
		}else if(PrintPolisMultiController.POLIS_QUADRUPLEX_MEDICAL_PLUS== singleDuplexQuadruplex) { //quadruplex				
			params.put("pathPolis", props.getProperty("report.polis."+kategori+".medplus")+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex_medical_plus"));
		}
		return params;
	}
	public String validasiBeaMeterai(Integer jn_bank) {
		List list = this.uwDao.selectMeterai();
		if(list.isEmpty()) return "Maaf, tetapi saldo bea meterai tidak ada. Silahkan ajukan surat permohonan bea meterai.";
		Meterai meterai = (Meterai) list.get(0);
		if(jn_bank != 2 || jn_bank != 3) return "Harus menggunakan meterai tera atau tempel!";
		if(meterai.getMstm_saldo_akhir() < 6000) return "Maaf, tetapi saldo bea meterai tidak mencukupi. Silahkan ajukan surat setoran tambahan bea meterai.";
		
		return null;
	}
	
	/*public List selectReportDataPenerimaanBSPAJ(String bdate, String edate, String jenis) {
	return basDao.selectReportDataPenerimaanBSPAJ(bdate, edate, jenis);
}*/
	
	/**
	 * @author Ridhaal
	 * @since 04 April 2016
	 * kelas di bawah di gunakan dalam UserAdministrationController
	 */
	
	public List selectCabangBiiUA(String cab_bank,Integer jn_cab) {
		return bacDao.selectCabangBiiUA(cab_bank, jn_cab);
	}
	
	public String selectNewLusIDUA() {
		return bacDao.selectNewLusIDUA();
	}
	
	public String cekSpv(String lus_id, Integer jn_spv) {
		return bacDao.cekSpv(lus_id,jn_spv);
	}
	
	public String cekLoginName(String user_name) throws DataAccessException {
		return bacDao.cekLoginName(user_name);
	}
	
	public String cekFullName(String full_name) throws DataAccessException {
		return bacDao.cekFullName(full_name);
	}
	
	public List selectDaftarUserUA(String cab_bank, Integer jn_user, String lus_login_name) {
		return bacDao.selectDaftarUserUA(cab_bank, jn_user,lus_login_name);
	}
	
	public List selectDetUserUA(Integer jn_pil,String lus_id, String cab_bank, String lca_id) {
		// jn_pil = untuk menampilkan pencarian by cab_bank (1), lus_id (2) , dan New (0)
		return bacDao.selectDetUserUA(jn_pil,lus_id,cab_bank,lca_id);
	}
	
	public String processUserAdministration(User cmd, User currentUser) {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		if("save".equals(cmd.getSubmitMode())) {
			
			if (cmd.getButton() == 1){
			try{
				 bacDao.insertNewUserId(cmd, currentUser);
				 bacDao.updateCounterUserId(cmd);
				 bacDao.insertNewUserIdAdmin(cmd);
				 bacDao.insertNewUserIdCab(cmd);
				 bacDao.insertNewUserIdMenu(cmd);
				 
				 return "User berhasil ditambahkan - default password : P45sword";
			}catch(Exception e){
				
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				logger.error("ERROR :", e);
				return "Data user belum bisa ditambahkan karena Error";
			}
			}else{
				try{
				bacDao.updateUserId(cmd);
				bacDao.deleteMstSsave(cmd);
				bacDao.insertNewUserIdMenu(cmd);
				return "User id berhasil diperbarui.";
				}catch(Exception e){
					
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					logger.error("ERROR :", e);
					return "Data user belum bisa diperbarui karena Error";
				}
			}
			
		}else if("reset".equals(cmd.getSubmitMode())) {
				try{
					bacDao.updatePasswordUser(cmd);
					return "Password berhasil di perbarui. Silahkan informasikan ke user untuk menggunakan default password : P45sword";	
				}catch(Exception e){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					logger.error("ERROR :", e);
					return "Password belum bisa diganti karena Error";
				}
		}
		
		return "ERROR";
	}
	
	public Double selectPaymentMode(String spaj) throws DataAccessException{
		return(Double) uwDao.selectPaymentMode(spaj);
	}

	public String selectNoPolisFormat(String spaj){
		return this.uwDao.selectNoPolisFormat(spaj);
	}
	
	public Pemegang selectpp(String spaj) throws DataAccessException {
		return this.bacDao.selectpp(spaj);
	}
	
	public String selectCabangBiiPolis(String spaj) throws DataAccessException{
		return this.bacDao.selectCabangBiiPolis(spaj);
	}
	
	public Map selectEmailAgen(String spaj)
	{
		return uwDao.selectEmailAgen(spaj);
	}
	
	public void insertMstPositionSpajWithSubIdBas(String lus_id,String deskripsi, String spaj, Integer subLiAksep, Integer lssa_id_bas) throws DataAccessException {		
			
		uwDao.insertMstPositionSpajWithSubIdBas(lus_id,deskripsi,spaj,subLiAksep,lssa_id_bas );
	}
	
	public Boolean prosesFurtherCollection(String spaj, Integer lsbs_id, Integer lsdbs_number, User currentUser, Datausulan datausulan, String flagStatusFR,String emailto, String emailcc,String detailFR){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date nowDate = commonDao.selectSysdate();
		// - Produk Danamas Prima : yancindy.malino@sinarmas-am.co.id, yancindy.malino@sinarmassekuritas.co.id, cindy@sinarmassekuritas.co.id, dan natalia listiawati ; CC : user uw
		// - Produk Simas Prima Bank Sinarmas : email cs sesuai cabang BSM (terlampir) dan AO sesuai cabang BSM ; CC : user UW
		// - Produk Power Save Syariah BSM : cs0910@banksinarmas.com, taufik.rudiyanto@banksinarmas.com, sadewo@sinarmasmsiglife.co.id, dan siti nihayatun ; CC : user UW
		// - Produk Personal Accident ABD
		
		String[] to = emailto.split(";");
		String[] cc = emailcc.split(";");
		String deskripsi = "";
		String mspo_policy_no = uwDao.selectNoPolisFormat(spaj);
		Pemegang dataPP = bacDao.selectpp(spaj);
		Tertanggung dataTT = bacDao.selectttg(spaj);
		String premi = FormatString.formatCurrency(datausulan.getLku_symbol(), new BigDecimal(datausulan.getMspr_premium()));
		Integer mgi = uwDao.selectMasaGaransiInvestasi(spaj, 1, 1);
		String nama_cabang = bacDao.selectCabangBiiPolis(spaj);
		String subject = "";
		String message = "";
		
		
		
		if (flagStatusFR.equalsIgnoreCase("F1")){// further Permintaan BSB
			deskripsi = "Mohon dapat mengirimkan Bukti Setor Bank/Bukti Pembayaran Premi (dalam bentuk PDF)";
			
			subject = "Permohonan Pengiriman BSB untuk SPAJ No."+ spaj;
					
			if((lsbs_id==175 && lsdbs_number==2)){
				message = "Dear,<br><br>Mohon dapat mengirimkan Bukti Setor Bank/Bukti Pembayaran Kontribusi/Premi (dalam bentuk PDF) untuk membantu user Underwriting dalam menginput kontribusi/premi ke dalam sistem.<br><br>"+
						"No. SPAJ / No. Polis	: "+spaj+" / "+mspo_policy_no+"<br>"+
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
			}else{
							
				message = "Dear,<br><br>Mohon dapat mengirimkan Bukti Setor Bank/Bukti Pembayaran Premi (dalam bentuk PDF) untuk membantu user Collection dalam menginput premi ke dalam sistem.<br><br>"+
							"No. SPAJ / No. Polis	: "+spaj+" / "+mspo_policy_no+"<br>"+	
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
					    		    	
			}
			
			
		}else if (flagStatusFR.equalsIgnoreCase("F2")){ // Further Collection Lain Lain
			deskripsi = detailFR;
			
			subject = "Permohonan Pelengkapan Further Collection untuk SPAJ No."+ spaj;
			
			if((lsbs_id==175 && lsdbs_number==2)){
				message = "Dear,<br><br>Mohon dapat melengkapi furtheran yang dibutuhkan untuk membantu user Underwriting dalam menginput kontribusi/premi ke dalam sistem.<br><br>"+
						"Detail Further        	: <b>"+detailFR+"</b><br><br>"+
						"No. SPAJ / No. Polis	: "+spaj+" / "+mspo_policy_no+"<br>"+
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
			else{
				message = "Dear,<br><br>Mohon dapat melengkapi furtheran yang dibutuhkan untuk membantu user dalam penginputan titipan Premi.<br><br>"+
							"Detail Further        	: <b>"+detailFR+"</b><br><br>"+
							"No. SPAJ / No. Polis	: "+spaj+" / "+mspo_policy_no+"<br>"+
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
					    		    	
			}
		}
		
		try {
			EmailPool.send("SMiLe E-Lions", 1, 0, 0, 0, 
					null, 0, 0, nowDate, null, 
					true, currentUser.getEmail(), 
					to, 
					cc, 
					//new String[]{"deddy@sinarmasmsiglife.co.id"}, 
					new String[]{"ridhaal@sinarmasmsiglife.co.id"}, 
					subject, 
					message, 
					null, spaj);
			
//			String a  =  currentUser.getEmail();
//			email.send(true, currentUser.getEmail(),new String[] {"ridhaal@sinarmasmsiglife.co.id"}, new String[] {"ridhaal@sinarmasmsiglife.co.id"}, null,
//					subject,
//					message,
//					null);
			
						
			uwDao.insertMstPositionSpajWithSubIdBas(currentUser.getLus_id(), "FC: " +deskripsi, spaj, 1 ,18);
			
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("ERROR :", e);
			return false;
		}
		
	}
	/*public void insertTM_ID(){
		try {
			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			List<Map> datanya = bacDao.selectDataSusulan();
			for(int i=0;i<datanya.size();i++){
				Map isiDatanya = (HashMap) datanya.get(i);
				List<Map> reff = bacDao.selectReffTm((String)isiDatanya.get("NO_TEMP"));
				if(!reff.isEmpty()){
					Map mapReff= (Map)reff.get(0);
					String no_kerjasama=(String)mapReff.get("TM_CODE");
					String spv=(String)mapReff.get("SPV_CODE");
					String app=(String)mapReff.get("APPLICATION_ID_DMTM");
					String reff_tm=(String)mapReff.get("REFF_TM_ID");
					bacDao.updateReffTm((String)isiDatanya.get("REG_SPAJ"),spv,no_kerjasama,app,reff_tm);
				}
			}
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
	}*/
	
	public List selectdetilinvestasi2(String spaj){
		return this.bacDao.selectdetilinvestasi2(spaj);
	}
	
	public Map selectAgenESPAJSimasPrima(String spaj) {		
		return bacDao.selectAgenESPAJSimasPrima(spaj);
	}
	
	public Date selectSysdateTime()throws DataAccessException{
		//return (Date) akseptasiDao.selectSysdateTime();
		return (Date) commonDao.selectSysdate();
	}
	
	public List<Map> selectProdRenewal(String spaj, String kode)throws DataAccessException{
		return uwDao.selectProdRenewal(spaj, kode);
	}
	
	public String selectUsedMstSpajTemp(String no_temp) {
		return bacDao.selectUsedMstSpajTemp(no_temp);
	}
	
	public double selectSumPremiTemp(String no_temp) {
		return (Double) bacDao.selectSumPremiTemp(no_temp);
	}	
	
	public Integer selectCountAreaBC(String lrb_id , String lcb_no) {		
		return bacDao.selectCountAreaBC(lrb_id, lcb_no);
	}
	
	/*public void updateMstPowersaveCairTglCair(String reg_spaj , Date tgl_cair_aktual) {
		bacDao.updateMstPowersaveCairTglCair(reg_spaj,tgl_cair_aktual);
		
		//ulangan 
		Ulangan ulangan = new Ulangan();
		ulangan.setReg_spaj(reg_spaj);
		ulangan.setJenis("EDIT DATA");
		ulangan.setStatus_polis(null);
		ulangan.setLus_id(0);
		ulangan.setKeterangan("UBAH TGL CAIR BY SYSTEM");
		uwDao.insertLstUlangan(ulangan);
	}*/
	
	public String selectLcaIdAB(String noBerkas, String kota, Integer jnlcaid) {
		return snowsDao.selectLcaIdAB(noBerkas, kota,jnlcaid);
	}

	public List selectListAjuanBiaya(String lca_id, String lcaName, Integer detail, String mi_id) {
		return snowsDao.selectListAjuanBiaya(lca_id, lcaName, detail, mi_id);
	}	
	
	public List selectDataNoBerkasDet(String mi_id) {
		return snowsDao.selectDataNoBerkasDet(mi_id);
	}	
	
	public String selectSysdateddmmyyy(){
		//return snowsDao.selectSysdateddmmyyy();
		return commonDao.selectSysdateTruncToString();
	}

	public List selectHistoryBerkas(Integer jnsearch, String noBerkas, String cabang) {
		return snowsDao.selectHistoryBerkas(jnsearch,noBerkas,cabang);
	}

	public List selectListInboxCheck(String noBerkas, Integer jnupl, Integer jnc) {
		return snowsDao.selectListInboxCheck(noBerkas,jnupl,jnc);
	}

	public List selectCekRolAB(String jenis, String tgl_pemakaian, String kota, Integer role, Integer jnUpd, String no_berkas) {
		return snowsDao.selectCekRolAB(jenis,tgl_pemakaian,kota,role,jnUpd,no_berkas);
	}

	public String selectKotaInboxDet(String noBerkas) {
		return snowsDao.selectKotaInboxDet(noBerkas);
	}

	public List selectAllLstCabangAB(String lca_id, Integer jnuselcaid) {
		return snowsDao.selectAllLstCabangAB(lca_id,jnuselcaid);
	}

	public List selectLstScanAB(String NoBerkas) {
		return snowsDao.selectLstScanAB(NoBerkas);
	}
	
	public void insertMstInboxDet(MstInboxDet mstInboxDet) {
		snowsDao.insertMstInboxDet(mstInboxDet);
	}
	
	public void deleteMstInboxDet(String mi_id){
	    snowsDao.deleteMstInboxDet( mi_id );
	}

	public Integer selectProsesUploadScanAB(String noBerkas, String lca_id, Upload upload, File destDir, List<Scan> daftarScan,StringBuffer filenames,User currentUser){
		try{
            DateFormat df = new SimpleDateFormat("yyyy");
			
			Date nowDate = commonDao.selectSysdate();
			String NameDoc = "";
			
			for(int i=0; i<upload.getDaftarFile().size(); i++) {
				MultipartFile file = (MultipartFile) upload.getDaftarFile().get(i);
				Integer mi_flag = 1;
				if(file.isEmpty()==false){
					mi_flag = 1;
					File fileDir = new File(destDir.getPath());
					String fileName = noBerkas + daftarScan.get(i).getNmfile() + " " + FormatString.getFileNo( destDir.list(), daftarScan.get(i).getNmfile())+".pdf";
					String dest = destDir.getPath() +"/"+ fileName; //file.getOriginalFilename();
					File outputFile = new File(dest);
				    FileCopyUtils.copy(file.getBytes(), outputFile);
				    
				    if(!filenames.toString().equals("")){
				    	filenames.append(", ");
				    }
				    filenames.append(daftarScan.get(i).getNmfile());
				    
				    Scan scan = new Scan();
				   
				    scan.setLus_id(currentUser.getLus_id());
				    scan.setLde_id(currentUser.getLde_id());
				    scan.setNo_indek(noBerkas);
				    scan.setTgl_input(nowDate);
				    scan.setFiles_ad(destDir.getPath()+"\\"+fileName);
				    scan.setTipe_file(daftarScan.get(i).getNmfile());
				    scan.setJml_page(1);
				    scan.setFlag_aktif(1);
				    scan.setJenis(3);//0 : program upload seno, 1 program SNOWS rudy, 2 program E-Lions deddy, 3 program E-Lion Pengajuan Biaya Ridhaal
				    boolean a=true;
				    while(a) {
				    	try{
					   		scan.setKd_scan(Integer.parseInt(sequence.sequenceKdScan()) );
						    uwDao.insertMstScan(scan);
						    a=false;
					    }catch (Exception e) {
					    	logger.error("ERROR :", e);
							TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
							return 0;
						}
				   	}
				    String fileName2 = noBerkas + daftarScan.get(i).getNmfile() ; 
				    NameDoc = daftarScan.get(i).getKet() + " , " + NameDoc;
				    
				    try{
				    MstInboxChecklist inboxChecklist = new MstInboxChecklist(noBerkas, daftarScan.get(i).getLjj_id(), daftarScan.get(i).getLc_id(), mi_flag, null, null, null);
					snowsDao.updateMstInboxChecklist(inboxChecklist);
				    }catch (Exception e) {
				    	logger.error("ERROR :", e);
						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
						return 0;
					}
				}
			}
			
			try{
				MstInboxHist mstInboxHist = new MstInboxHist(noBerkas, 207, 207, null, null, "UPLOAD DOCUMENT : "+NameDoc+" (OK) ", Integer.valueOf(currentUser.getLus_id()), nowDate, null,0,0);
				snowsDao.insertMstInboxHist( mstInboxHist );		
				return 1;
			}catch (Exception e) {
			    	logger.error("ERROR :", e);
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return 0;
				}
			
		}catch (Exception e){
			logger.error("ERROR :", e);
			File outputFile = new File(destDir.getPath());
			outputFile.delete();
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
	}

	public void updateTglPengirimanBerkas(String noBerkas, String tgl_berkas_masuk) {
		snowsDao.updateTglPengirimanBerkas(noBerkas, tgl_berkas_masuk);
	}
	
	public List selectreportSummaryInputUserBas() {
		return this.basDao.selectreportSummaryInputUserBas();
	}
	
	public List selectreportSummaryInputUser(String idbas2, String bdate, String edate, String prods, String preview) {
		return this.basDao.selectreportSummaryInputUser(idbas2, bdate, edate, prods, preview);
	}
	
	public void insertMstCsfDial(
			String mscsf_ket, String reg_spaj, String mscsf_dial_ke, String flag_inout,
			String lus_id, String mscfl_no_ref, String flag_finance, String lscsf_jenis, String mscsf_ocr,
			String mscsf_dir, String mscsf_tujuan){
		snowsDao.insertMstCsfDial(mscsf_ket, reg_spaj, mscsf_dial_ke, flag_inout, lus_id, mscfl_no_ref, 
				flag_finance, lscsf_jenis, mscsf_ocr, mscsf_dir, mscsf_tujuan);
	}
	
	public String selectMaxMscsfDialKe(String reg_spaj){
		return snowsDao.selectMaxMscsfDialKe(reg_spaj);
	}
	
	//randy
	public int prosesUwRenewal(User user,String nospaj,String dest,String nama_file,String subject,String jenis,String msps_desc) throws DataAccessException{		
		List<File> attachments = new ArrayList<File>();
        StringBuffer pesan = new StringBuffer();
        List<Map> data2 = new ArrayList<Map>();	
        
        try {
	        String lus_id = user.getLus_id();
	        data2 = uwDao.selectUwRenewal(nospaj);
	        Map x = data2.get(0);
	        String no_polis = x.get("NO_POLIS").toString();
	        String nama_pp = x.get("NAMA_PP").toString();
	        
	        HashMap alamat = selectMstConfig(6, "UW Renewal", "uw_renewal");
	        String from = props.getProperty("admin.ajsjava");
	        String[] to = alamat.get("NAME")!=null?alamat.get("NAME").toString().split(";"):null;
	        String[] cc = alamat.get("NAME2")!=null?alamat.get("NAME2").toString().split(";"):null;   
	        String[] bcc = alamat.get("NAME3")!=null?alamat.get("NAME3").toString().split(";"):null;  
//	        String[] to = {"randy@sinarmasmsiglife.co.id"};
//	        String[] cc = {"randy@sinarmasmsiglife.co.id"};   
//	        String[] bcc = {"randy@sinarmasmsiglife.co.id"};
	        pesan.append("Telah dilakukan Proses Renewal dengan keterangan: <br/>"+"<br/>Nomor Polis    : "+no_polis+"<br/>Pemegang Polis : "+nama_pp);
	        
	        if (jenis.equals("4")){
		        File file = new File(dest+"\\"+nama_file);
				if (!file.exists()){
					logger.info("===== FILE TIDAK ADA =====");
					return 1;
				}else{
					logger.info("===== FILE ADA ======");
					attachments.add(new File(dest + nama_file));
				}
	        }
	
			try { 		
				if (jenis.equals("4")){
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, selectSysdate(), null, true, 
							from, to, cc, bcc, subject, pesan.toString(),  attachments, null);
//					email.send(true, from, new String[]{"randy@sinarmasmsiglife.co.id"}, 
//					new String[]{"randy@sinarmasmsiglife.co.id"}, 
//					new String[]{"randy@sinarmasmsiglife.co.id"}, subject, pesan.toString(), attachments);
				}else{
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, selectSysdate(), null, true, 
				 			from, to, cc, bcc, subject, pesan.toString(),  null, null);
//					email.send(true, from, new String[]{"randy@sinarmasmsiglife.co.id"}, 
//					new String[]{"randy@sinarmasmsiglife.co.id"}, 
//					new String[]{"randy@sinarmasmsiglife.co.id"}, subject, pesan.toString(), null);
				}
			} catch (MailException e) {
				logger.error("ERROR :", e);
			} 
//			catch (MessagingException e) {
//				logger.error("ERROR :", e);
//			} 
			
			if (jenis.equals("4")){
				getNoSuratUwRenewal(5,"01",1);
			}
			uwDao.insertMstPositionSpajRenewal(lus_id,nospaj,msps_desc);
        }
        catch (Exception e) {
            logger.error("ERROR :", e);
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return 0;
        }
        return 2;
	}
	
	public String getNoSuratUwRenewal(Integer mscoNum,String id,Integer pros){
		String noSurat;
		String ket="/SRT/UND/";
		
		//0=reset counter per bulan
		//1=tidak reset counter per bulan
		Integer flag_Counter=financeDao.selectCekCounterTtsMonthAndYear(mscoNum, id);
		if(flag_Counter==0){//update eka.mst_counter_tts
			financeDao.updateResetMstCounterTts(mscoNum,id);
		}
		Map mapCounter=financeDao.selectGetCounterTts(mscoNum,id);
		String formatCounter=(String)mapCounter.get("FORMAT_COUNTER");
		BigDecimal counter=(BigDecimal)mapCounter.get("COUNTER");
		Date today=commonDao.selectSysdate();
		if(pros==1){//update counter nmor surat
			financeDao.updateMstCounterTts(new Double(counter.intValue()),mscoNum,id);
		}
		Date sysdate=commonDao.selectSysdate();
		String mm=FormatDate.getMonth(sysdate);
			if(mm.equals("1"))mm="I";
			if(mm.equals("2"))mm="II";
			if(mm.equals("3"))mm="III";
			if(mm.equals("4"))mm="IV";
			if(mm.equals("5"))mm="V";
			if(mm.equals("6"))mm="VI";
			if(mm.equals("7"))mm="VII";
			if(mm.equals("8"))mm="VIII";
			if(mm.equals("9"))mm="IX";
			if(mm.equals("10"))mm="X";
			if(mm.equals("11"))mm="XI";
			if(mm.equals("12"))mm="XII";
		String yy=FormatDate.getYearFourDigit(sysdate);
		noSurat=formatCounter+ket+mm+"/"+yy;
		return noSurat;
	}
	
	public String getCounterSsu(Integer mscoNum,String id,Integer pros){
		String ctr;
		
		//0=reset counter per bulan
		//1=tidak reset counter per bulan
//		Integer flag_Counter=financeDao.selectCekCounterTtsMonthAndYear(mscoNum, id);
//		if(flag_Counter==0){//update eka.mst_counter_tts
//			financeDao.updateResetMstCounterTts(mscoNum,id); //utk update msco_value set=0
//		}
		Map mapCounter=financeDao.selectGetCounterTts(mscoNum,id);
		String formatCounter=(String)mapCounter.get("FORMAT_COUNTER");
		BigDecimal counter=(BigDecimal)mapCounter.get("COUNTER");
		Date today=commonDao.selectSysdate();
		
		if(pros==1){//update counter nmor surat
			financeDao.updateMstCounterTts(new Double(counter.intValue()),mscoNum,id);
		}
		
		Date sysdate=commonDao.selectSysdate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		ctr = "SSU/" + sdf.format(commonDao.selectSysdate()) +"/"+ formatCounter;
		return ctr;
	}
	
	
	//  REPORT FOLLOW UP BILLING BAS - Req. Septa (98281)- Randy
	public int report_fu_bas(String tgl1, String tgl2, String prod, String tipe, String status) throws Exception {
//		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Report report;
    	Map<String, Object> m = new HashMap<String, Object>();
		int proses = 0;
		report = new Report("Report Follow Up Billing",props.getProperty("report.uw.report_followup_billing_hasil"),Report.XLS, null);
		
		String prod_p = "ALL PRODUCTS";
		if(prod.equals("1")){prod_p = "SMiLe PRIORITAS";}
		else if(prod.equals("2")){prod_p = "SMiLe NON-PRIORITAS";}
		
		String tipe_p = "FOLLOW UP";
		if(tipe.equals("2")){tipe_p = "OUTSTANDING";}
		else if(tipe.equals("3")){tipe_p = "HASIL FOLLOW UP BAS";}
		
		String status_p = "ALL STATUS";
		if(status.equals("2")){status_p = "INFORCE";}
		else if(status.equals("3")){status_p = "LAPSE";}
		
		Map<String, Comparable> params = new HashMap<String, Comparable>();
		params.put("bdate", tgl1);
		params.put("edate", tgl2);
		params.put("prod_p", prod_p);
		params.put("tipe_p", tipe_p);
		params.put("status_p", status_p);
		
		//prod	1:prioritas 	2:non-prioritas 3:All
		//tipe	1:followup  	2:outstanding 	3:hasil follow up
		//stat	1:all   		2:inforce     	3:lapse
		
		String prod_q = "1=1";
		if(prod.equals("1")){
			prod_q = "((c.LCA_ID IN (37) AND c.LWK_ID IN('M1') AND c.LSRG_ID IN('00','01','02','03','04')) "+
                    	"or (c.LCA_ID IN (66) AND c.LWK_ID IN('00') AND c.LSRG_ID IN('B2')))";
		}else if(prod.equals("2")){
			prod_q = "not ((c.LCA_ID IN (37) AND c.LWK_ID IN('M1') AND c.LSRG_ID IN('00','01','02','03','04')) "+
                    	"or (c.LCA_ID IN (66) AND c.LWK_ID IN('00') AND c.LSRG_ID IN('B2')))";
		}
		
		String tipe_q = "1=1";
		if(tipe.equals("1")){
			tipe_q = "(b.MSBI_BEG_DATE BETWEEN TO_DATE('"+tgl1+"','DD/MM/YYYY') AND TO_DATE('"+tgl2+"','DD/MM/YYYY'))";
		}else if(tipe.equals("2")){
			tipe_q = "(b.MSBI_BEG_DATE between add_months(to_date('"+tgl1+"','dd/MM/yyyy'),-1) and add_months(to_date('"+tgl2+"','dd/MM/yyyy'),-1))";
		}else if(tipe.equals("3")){
			tipe_q = "(b.MSBI_BEG_DATE BETWEEN TO_DATE('"+tgl1+"','DD/MM/YYYY') AND TO_DATE('"+tgl2+"','DD/MM/YYYY'))";
		}
		
		String status_q = "1=1";
		if(status.equals("2")){status_q = "C.LSSP_ID=1";}
		else if(status.equals("3")){status_q = "C.LSSP_ID=14";}
		
		params.put("prod_q", prod_q);
		params.put("tipe_q", tipe_q);
		params.put("status_q", status_q);
		
//		String dest =  "C:\\EkaWeb\\";
		String dest =  "\\\\ebserver\\pdfind\\Report\\temp\\";
		
		String nama_file = "Report_Follow_Up_Billing.xls";
		
		Connection conn = null;
		
		try{
			conn = this.elionsManager.getUwDao().getDataSource().getConnection();
			JasperUtils.exportReportToXls(report.getReportPath()+".jasper", dest, nama_file, params, conn);
		}finally {
			this.elionsManager.getUwDao().closeConn(conn);
		}
			
		File file = new File(dest+"\\"+nama_file);
		
		if (file.exists()){
			proses = prosesFuBilling(dest, nama_file, tgl1, tgl2);
		}
		return proses;
	}
	
	public int prosesFuBilling(String dest, String nama_file, String tgl1, String tgl2) throws DataAccessException{		
		List<File> attachments = new ArrayList<File>();
        StringBuffer pesan = new StringBuffer();
        
        try {
//	        String lus_id = user.getLus_id();
	        String subject = "Report Data Follow Up Periode "+tgl1+" - "+tgl2;
	        		
	        HashMap alamat = selectMstConfig(6, "FU Billing", "fu_billing");
	        String from = props.getProperty("admin.ajsjava");
	        String[] to = alamat.get("NAME")!=null?alamat.get("NAME").toString().split(";"):null;
	        String[] cc = alamat.get("NAME2")!=null?alamat.get("NAME2").toString().split(";"):null;   
	        String[] bcc = alamat.get("NAME3")!=null?alamat.get("NAME3").toString().split(";"):null;  
//	        String[] to = {"randy@sinarmasmsiglife.co.id"};
//	        String[] cc = {"randy@sinarmasmsiglife.co.id"};   
//	        String[] bcc = {"randy@sinarmasmsiglife.co.id"};
	        pesan.append("Dear Team Channel,<br/><br/>");
	        pesan.append("Terlampir disampaikan hasil follow up untuk nasabah-nasabah Agency yang telah dilakukan follow up dari BAS.<br/>");
	        pesan.append("Mohon bantuannya untuk dapat dilakukan follow up dari Tim Channel.<br/><br/>");
	        pesan.append("Demikian yang dapat disampaikan. Atas kerjasamanya yang baik terima kasih.<br/>");
	        attachments.add(new File(dest + nama_file));
	        
			try { 		
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, selectSysdate(), null, true, 
							from, to, cc, bcc, subject, pesan.toString(),  attachments, null);
//					email.send(true, from, new String[]{"randy@sinarmasmsiglife.co.id"}, 
//					new String[]{"randy@sinarmasmsiglife.co.id"}, 
//					new String[]{"randy@sinarmasmsiglife.co.id"}, subject, pesan.toString(), attachments);
			} catch (MailException e) {
				logger.error("ERROR :", e);
				return 0;
			} 
//			catch (MessagingException e) {
//				logger.error("ERROR :", e);
//			} 
			
        }
        catch (Exception e) {
            logger.error("ERROR :", e);
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            return 0;
        }
        return 1;
	}
	
	public String selectLstDocumentPositionByLSPDID(String lspd_id){
		return snowsDao.selectLstDocumentPositionByLSPDID(lspd_id);
	}
	
	public void updateTglSmileBaby(String spaj, String hpl){
		bacDao.updateTglSmileBaby(spaj, hpl);
	}
	
	public String selectTanggalHpl(String spaj){
		return this.bacDao.selectTanggalHpl(spaj);
	}
	
	public List<Followup> selectReportFollowupBillingPerUser2(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String kat, String rep, String jn_tgl) {
		return basDao.selectReportFollowupBillingPerUser2(begdate, enddate, lus_id, stfu, cabang, admin, kat, rep, jn_tgl);
	}
	public List<Followup> selectReportFollowupBilling2(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String kat, String jn_tgl) {
		return basDao.selectReportFollowupBilling2(begdate, enddate, lus_id, stfu, cabang, admin, kat, jn_tgl);
	}
	
	public Map selectNoGadgetESPAJ(String reg_spaj){
		return basDao.selectNoGadgetESPAJ(reg_spaj);
	}
	
	public List <Map> selectWelcomeCallSuccess(String reg_spaj, Integer jn_cek){		
		return basDao.selectWelcomeCallSuccess(reg_spaj,jn_cek);
	}
	
	public void schedulerWelcomeCall(){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String msh_name = "SCHEDULER PROSES WELCOME CALL";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerWelcomeCall(msh_name);
		}	
	}
	

	public void schedulerWelcomeCallBankAs(){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String msh_name = "SCHEDULER PROSES WELCOME CALL BANK AS";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerWelcomeCallBankAs(msh_name);
		}	
	}
	public void schedulerWelcomeCallCorona(){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String msh_name = "SCHEDULER PROSES WELCOME CALL CORONA";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerWelcomeCallCorona(msh_name);
		}	
	}
	public List<DropDown> selectDropDownFU(String spaj, String lsfuId){
		return this.uwDao.selectDropDownFU(spaj, lsfuId);
	}
	
	public Integer selectJnBankDetBisnis(Integer lsbs, Integer lsdbs){
		return bacDao.selectJnBankDetBisnis(lsbs, lsdbs);
	}
	
	/**
	 * Proses Pengembalian Premi Polis Successive
	 * @author 	MANTA
	 * edited by Ridhaal (11/10/2016)
	 * R. Perubahan untuk upload document dijadikan satu dan boleh kosong (optional)
	 * R. Penambahan untuk pengecekan inputan sudah ada atau belum pada tabel eka.mst_temp_premi_refund berdasarkan SPAJ dan Premi Ke.
	 */
	public String prosesRefundPolisSucc(PolicyInfoVO dataRefund, Upload upl, User currentUser) {
		String hasil = "Proses Refund Gagal.";
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		try {
			HashMap datapremike = Common.serializableMap(refundDao.selectPaymentKe(dataRefund.getSpajNo(), dataRefund.getPremi_ke()));
			Integer Flag_CekDataPremiRefund=refundDao.CekDataPremiRefund(dataRefund.getSpajNo(), dataRefund.getPremi_ke());
			//0=data belum di input
			//1=data sudah pernah di input
			
			if(datapremike == null){
				hasil = "Proses Refund Gagal. Data Premi Ke-" + dataRefund.getPremi_ke().toString() + " tidak ada.";
			}else if(Flag_CekDataPremiRefund > 0){
				hasil = "Proses Refund Gagal. Data Premi Ke-" + dataRefund.getPremi_ke().toString() + " untuk SPAJ ini sudah pernah di input.";
			}else{
				dataRefund.setNo_trx((String) datapremike.get("NO_TRX"));
				dataRefund.setTgl_rk((Date) datapremike.get("TGL_TRX"));
				
				refundDao.insertMstTempPremiRefundMod(dataRefund, new Integer(0), new Integer(2), currentUser.getLus_id(),
						(String) datapremike.get("NO_PRE"), (String) datapremike.get("NO_VOC"), (String) datapremike.get("NO_JM_SA"));
				
//				if(!upl.getFile1().isEmpty() && !upl.getFile2().isEmpty() && !upl.getFile3().isEmpty()){
				if(!upl.getFile1().isEmpty()){//update by Ridhaal - upload dokumen dijadikan 1 dan bisa di upload atau kosong (optional)
					String uplDir = props.getProperty("pdf.dir")+"\\BSB_Refund_Succ\\"+dataRefund.getSpajNo()+"\\";
					//String uplDir = props.getProperty("pdf.dir.export")+"\\BSB_Refund_Succ\\"+dataRefund.getSpajNo()+"\\";
					
					File dir = new File(uplDir);
					if(!dir.exists()) dir.mkdirs();
					
					String oriname = upl.getFile1().getOriginalFilename().trim();
					String filetype = oriname.substring(oriname.lastIndexOf("."), oriname.length());
					if (!filetype.contains("pdf")){
						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
						hasil = "Proses Refund Gagal. Format File Upload bukan File PDF.";
					}else{
					File sourceFile1 = new File(uplDir + df.format(this.selectSysdate()) + "Berkas_Refund_Successive" + filetype);
					FileCopyUtils.copy(upl.getFile1().getBytes(), sourceFile1);
					
//					String oriname = upl.getFile1().getOriginalFilename().trim();
//					String filetype = oriname.substring(oriname.lastIndexOf("."), oriname.length());
//					File sourceFile1 = new File(uplDir + df.format(this.selectSysdate()) + "BSB" + filetype);
//					FileCopyUtils.copy(upl.getFile1().getBytes(), sourceFile1);
					
//					String oriname2 = upl.getFile2().getOriginalFilename().trim();
//					String filetype2 = oriname2.substring(oriname2.lastIndexOf("."), oriname2.length());
//					File sourceFile2 = new File(uplDir + df.format(this.selectSysdate()) + "SKR" + filetype2);
//					FileCopyUtils.copy(upl.getFile2().getBytes(), sourceFile2);
//					
//					String oriname3 = upl.getFile3().getOriginalFilename().trim();
//					String filetype3 = oriname3.substring(oriname3.lastIndexOf("."), oriname3.length());
//					File sourceFile3 = new File(uplDir + df.format(this.selectSysdate()) + "CBT" + filetype3);
//					FileCopyUtils.copy(upl.getFile3().getBytes(), sourceFile3);
					
					hasil = "Proses Refund Dengan Penyimpanan Dokumen Berhasil.";
					}
				}else{
					//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					//hasil = "Proses Refund Gagal. File Upload tidak terdeteksi.";
					hasil = "Proses Refund Tanpa Penyimpanan Dokumen Berhasil.";
				}
				snowsDao.insertMstCsfDial(dataRefund.getKeterangan(), dataRefund.getSpajNo(), null, "I", currentUser.getLus_id(), null, null, "37", null, null, null);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return hasil;
	}
	
	//MANTA - Scheduler untuk Proses Refund Premi
	public void schedulerRefundPremiSuccessive(){
		String msh_name = "SCHEDULER REFUND PREMI LANJUTAN (E-LIONS)";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerRefundPremiSuccessive(msh_name);
		}
	}
	
	public ArrayList<HashMap> selectMstTempPremiRefund(String reg_spaj, Integer jenis, Integer premi_ke){
		return Common.serializableList(refundDao.selectMstTempPremiRefund(reg_spaj, jenis, premi_ke));
	}
	
	public HashMap selectEmailBCAM2(String spaj){
		return Common.serializableMap(uwDao.selectEmailBCAM2(spaj));
	}
	
	public String selectTglLhrPP(String spaj){
		return bacDao.selectTglLhrPP(spaj);
	}	

	//Ridhaal - Short URL
	public String prosesUrlShortener(String longUrl) throws Exception{
			
		String result="";
    	try {
    			
		    	 String apiURL = GOOGLE_URL_SHORT_API+"?key="+GOOGLE_API_KEY;
		    	 HttpClient httpClient = new HttpClient();
		    	 PostMethod  method = new PostMethod(apiURL);
		    	 JSONObject inputJsonObj = new JSONObject();
		    	 StringRequestEntity requestEntity = new StringRequestEntity(
														    				"{\"longUrl\": \""+longUrl+"\"}",
														    			    "application/json",
														    			    null);
		    		
		    	 method.setRequestEntity(requestEntity);
		    		
		    	      try{
		    	    	  httpClient.executeMethod(method);		    	    	  
		    	    	  logger.info("HTTP status " + method.getStatusCode()+ " creating con\n\n");
		
		    				if (method.getStatusCode() == HttpStatus.SC_OK) {
		    					try {
		    						JSONObject responsePost = new JSONObject(new JSONTokener(
		    								new InputStreamReader( method.getResponseBodyAsStream())));
		    						logger.info("Create response: "+ responsePost.toString());
		    						Gson gson = new Gson();
		    						result=responsePost.getString("id");
		    					} catch (JSONException e) {
		    						logger.error("ERROR :", e);
		    					}
		    				}
		    	      }catch(Exception e) { 
			    	          System.err.println(e); 
			    	  }finally { 
			    	          method.releaseConnection(); 
			    	  }
		    	} catch (Exception e) {
		    		logger.error("ERROR :", e);
		    	}
    		return result;
		}
	
	public void insertUrlSecureFile(Date input_date, String reg_spaj, String note,Integer VendorId, String KeyId, String LongURL, String URLEncrypt, String ShortUrl ) {
		bacDao.insertUrlSecureFile( input_date,  reg_spaj,  note, VendorId,  KeyId,  LongURL,  URLEncrypt,  ShortUrl );
	}
	
	public List cekUrlSecureFile( String reg_spaj, String note) {
		return bacDao.cekUrlSecureFile(  reg_spaj,  note);
	}
	
	//Ridhaal - generate Surat Pengajuan Provider - BTN
	public File generateSuratPengajuanProvider(ElionsManager elionsManager2, UwManager uwManager2, String spaj, Properties props) {
		
		String cabang = elionsManager2.selectCabangFromSpaj(spaj);
		String outputNameSPP = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+"SURAT_PENJAMINAN_PROVIDER.pdf";
	       
		try{	List rider = elionsManager2.selectRiderPolisPas(spaj);				
			//	Date sysdate =  elionsManager2.selectSysdate();	
				Date tglTransferToPrint = selectTglTransToPrintPolis(spaj);
				String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;		
		        
		        Pemegang dataPP = elionsManager2.selectpp(spaj);
		        Tertanggung dataTT = elionsManager2.selectttg(spaj);
		        AddressBilling addrBill = elionsManager2.selectAddressBilling(spaj);
		        Datausulan dataUsulan = elionsManager2.selectDataUsulanutama(spaj);
		        InvestasiUtama inv  = elionsManager2.selectinvestasiutama(spaj);
		        Rekening_client rekClient = elionsManager2.select_rek_client(spaj);
		        Account_recur accRecur = elionsManager2.select_account_recur(spaj);
		        List detInv = uwManager2.selectdetilinvestasimallspaj(spaj);
		        List benef = elionsManager2.select_benef(spaj);
		        Integer lsre_id = uwManager2.selectPolicyRelation(spaj);
		        List<MedQuest> mq = uwManager2.selectMedQuest(spaj,null);
		        Agen agen = elionsManager2.select_detilagen(spaj);
		        dataUsulan.setDaftaRider(elionsManager2.selectDataUsulan_rider(spaj));
		        List peserta= select_semua_mst_peserta2(spaj);
		        
		       
		        
				Map premiProdukUtama = elionsManager2.selectPremiProdukUtama(spaj);
				String kurs = (String) premiProdukUtama.get("LKU_ID");
				Calendar cal = Calendar.getInstance(); 
				cal.add(Calendar.MONTH, 1);
				//Generate Welcome Letter
				DateFormat yy = new SimpleDateFormat("yyyy");
				DateFormat MM = new SimpleDateFormat("MM");
				
				// Get the date today using Calendar object.
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String reportDateMM = MM.format(tglTransferToPrint);
				String reportDateYYYY = yy.format(tglTransferToPrint);
				File userDirSPP = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
					
		        if(!userDirSPP.exists()) {
		        	userDirSPP.mkdirs();
		        }
		        
		        HashMap moreInfo = new HashMap();
				moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
				moreInfo.put("Title", "DMTM");
				moreInfo.put("Subject", "SURAT KONFIRMASI DMTM");
				
				PdfContentByte over;
				BaseFont calibri = BaseFont.createFont("C:\\WINDOWS\\FONTS\\CALIBRI.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				
		        PdfReader reader = new PdfReader(props.getProperty("pdf.template.term")+"\\SP_PROV_MERGE.pdf");
		        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputNameSPP));
		     
		        String noSPP = sequence.sequenceNoSuratPenjaminanProvider();	
		        
		        over = stamp.getOverContent(1);
				over.beginText();
				over.setFontAndSize(calibri,9);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, noSPP, 138, 687, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, reportDateYYYY, 277, 687, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, AngkaTerbilang.romawiBerjaya(reportDateMM), 251, 687, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(elionsManager2.selectFAddMonths(FormatDate.toString(tglTransferToPrint),new Integer(1))), 127, 560, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorPolis(dataPP.getMspo_policy_no()), 203, 513, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 203, 501, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) dataPP.getMspe_date_birth()), 203, 490, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian((Date) dataUsulan.getMste_beg_date()), 203, 468, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLsdbs_name().replaceAll("SMiLe MEDICAL AC", "").toUpperCase(), 203, 479, 0);
			 
			    if(peserta.size()>0){
					int vertikal=416;
					for(int z=0;z<peserta.size();z++){
						vertikal = vertikal-16;
					Map m = (HashMap) peserta.get(z);				
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String) m.get("NAMA"),113, vertikal, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER,FormatDate.toIndonesian((Date) m.get("TGL_LAHIR")),447, vertikal, 0);
					}
				}
			    
//			    String drIndra = props.getProperty("pdf.template.admedika2")+"\\drindra2.jpg";
//			    Image img = Image.getInstance(drIndra);					
//				img.setAbsolutePosition(380, 300);		
//				img.scaleAbsolute(120, 34);
//				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 95, 129);
//				over.stroke();
//			    over.endText();
//				stamp.close();	
				
			    String ttd = props.getProperty("pdf.template.admedika2")+"\\suryanto.jpg";
			    Image img = Image.getInstance(ttd);					
				img.setAbsolutePosition(380, 300);		
//				img.scaleAbsolute(90, 34);
				img.scalePercent(20);
				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 95, 130);
				over.stroke();
			    over.endText();
				stamp.close();	
				
		        File l_file = new File(outputNameSPP);
		        return new File(outputNameSPP);
	}catch (Exception e) {
		logger.error("ERROR :", e);
	}		
		//selain itu, format default
		return new File(outputNameSPP);
	}
	
	public Date selectTglTransToPrintPolis(String spaj)throws DataAccessException{
		return (Date)bacDao.selectTglTransToPrintPolis(spaj);
	}
	
	public String selectKycDesc(String spaj, Integer no_urut,Integer kyc_id , Integer kyc_pp ){
		return bacDao.selectKycDesc(spaj,no_urut,kyc_id,kyc_pp);
	}
	
	// Pindahan dari PaPartnerUploadFormController
	public List<Map> processRegFreePaDmtm(List<TmSales> tmSaless, String tanggalAwal, String jenis_pas)
    {
        List<Map> successMessageList = new ArrayList<Map>();
        Integer result=0;
        String err="";
        List<TmSales> tmSalesListPDF = new ArrayList<TmSales>();
        String txtReply="test masuk";
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat df2 = new SimpleDateFormat("ddMMyyyy");
        
        TmSales tmSales = new TmSales();
        String application_id = "PAS_FREE";
////        
        for(TmSales pasx : tmSaless){
            try{
                PreparedStatement cmd=null;
                ResultSet rs=null;
                
                String name =   pasx.getHolder_name();
                Integer jnskelamin  =   pasx.getSex();
                String sex   = jnskelamin.toString();
                String identity =   pasx.getCard_no();
                                                        
                Date tglLahir   =   pasx.getBod_holder();               
                String dobStr    =  df.format(tglLahir);
                try{
                    Date date = df.parse(dobStr);
                }
                catch (Exception e) {
                    logger.error("ERROR :", e);                        
                }                                       
                String email=pasx.getEmail();
                String nohp = pasx.getMobile_no();
                
                //validasi param
                //validasi length maximal
                boolean isBig=false;
                if(identity.length()>50){
                    isBig=true;
                }else if(name.length()>150){
                    isBig=true;
                }else if(sex.length()>1){
                    isBig=true;
                }
                
                if(!isBig){
                    String jns_pas = "PAS-FREE";
                    if("060".equals(pasx.getProduct_code())) {
                        jns_pas = "FREE-PA-BANK-DKI";
                    } else if("061".equals(pasx.getProduct_code())) {
                        jns_pas = "FREE-DBD-BANK-DKI";
                    }
                    
                    //format tanggal
                    Date dob=null;
                    try {
                        // dob=new SimpleDateFormat("ddMMyyyy").parse(dobStr);
                        // Date begdate=new SimpleDateFormat("dd/MM/yyyy").parse(tanggalAwal);                           
                        //Date enddate=FormatDate.add(FormatDate.add(begdate, Calendar.MONTH, 12),Calendar.DATE,-1);
                        Date begdate = new SimpleDateFormat("dd/MM/yyyy").parse("20/12/2014");  
                        Date enddate = new SimpleDateFormat("dd/MM/yyyy").parse("19/12/2015");
                        
                        // Set begdate & enddate produk bank dki
                        if("060,061".indexOf(pasx.getProduct_code()) > -1) {
//                          begdate = bacManager.selectSysdate();
                            begdate = new SimpleDateFormat("dd/MM/yyyy").parse(tanggalAwal);
                            Calendar temp = Calendar.getInstance();
                            temp.setTime(begdate);
                            temp.add(Calendar.MONTH, 6);// 6 bln
                            temp.add(Calendar.DATE, -1);// -1 hari
                            
                            enddate = temp.getTime();
                        }
                         
                        Integer umurPp = 0;                             
                        Integer umurBlnTt = 0;                              
                        f_hit_umur umr = new f_hit_umur();
                                
                        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
                        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
                        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
                        Date sysdate = selectSysdate();
                        int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
                        int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
                        int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
                                
                        if(pasx.getBod_holder() != null){       
                            int tahun1 = Integer.parseInt(sdfYear.format(pasx.getBod_holder()));
                            int bulan1 = Integer.parseInt(sdfMonth.format(pasx.getBod_holder()));
                            int tanggal1 = Integer.parseInt(sdfDay.format(pasx.getBod_holder()));                                       
                            umurPp=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
                        }                   
                        
                        if(umurPp>=1 && umurPp<=65){
                                String campaign = "PARISKAINCDAY";
                                
                                if("060".equals(pasx.getProduct_code())) {
                                    campaign = "FREEPABANKDKI";
                                } else if("061".equals(pasx.getProduct_code())) {
                                    campaign = "FREEDBDBANKDKI";
                                }
                                
                                tmSales = new TmSales();
                                            
                                tmSales.setTgl_sales(sysdate);
                                tmSales.setCampaign(campaign);
                                tmSales.setCard_type("00");
                                tmSales.setCard_no("1234567890123456");
                                tmSales.setCust_no(identity);
                                tmSales.setHolder_name(name);
                                tmSales.setSex(jnskelamin);
                                tmSales.setBod_holder(pasx.getBod_holder());
                                tmSales.setAge(umurPp);
                                tmSales.setMobile_no(pasx.getMobile_no());
//                              tmSales.setProduct_code("056");
                                
                                if(!Common.isEmpty(pasx.getProduct_code())) {
                                    tmSales.setProduct_code(pasx.getProduct_code());
                                } else {
                                    tmSales.setProduct_code("056");
                                }
                                
                                tmSales.setPremium(new BigDecimal(0));
//                              tmSales.setSum_insured(new BigDecimal(50000000));
                                
                                if(Common.isEmpty(pasx.getSum_insured())) {
                                    tmSales.setSum_insured(new BigDecimal(50000000));
                                }
                                
                                tmSales.setBeg_date(begdate);
                                tmSales.setEnd_date(enddate);
                                tmSales.setBill_mode("2");
                                tmSales.setBill_freq(0);
                                tmSales.setCall_date(null);
                                tmSales.setTgl_input(sysdate);
                                tmSales.setFlag_cek(1);
                                tmSales.setPosisi(99);
                                tmSales.setStatus(10);
                                tmSales.setEmail(email);
                                tmSales.setNo_sertifikat(genSertifikatTmSales(tmSales));
                                
                                if("060".equals(tmSales.getProduct_code())) {
                                    application_id = "FREEPA_BANK_DKI";
                                } else if("061".equals(tmSales.getProduct_code())) {
                                    application_id = "FREEDBD_BANK_DKI";
                                }
                                
                                tmSales.setApplication_id(application_id);
                                
                                if(!Common.isEmpty(pasx.getNo_akun())) tmSales.setNo_akun(pasx.getNo_akun());
                                if(!Common.isEmpty(pasx.getTipe_akun())) tmSales.setTipe_akun(pasx.getTipe_akun());
                                if(!Common.isEmpty(pasx.getId_customer())) tmSales.setId_customer(pasx.getId_customer());
                                if(!Common.isEmpty(pasx.getTgl_buka_akun())) tmSales.setTgl_buka_akun(pasx.getTgl_buka_akun());
                                if(!Common.isEmpty(pasx.getSts_nikah())) tmSales.setSts_nikah(pasx.getSts_nikah());
                                if(!Common.isEmpty(pasx.getKd_cabang())) tmSales.setKd_cabang(pasx.getKd_cabang());
                                if(!Common.isEmpty(pasx.getPekerjaan())) tmSales.setPekerjaan(pasx.getPekerjaan());
                                if(!Common.isEmpty(pasx.getTempat_lahir())) tmSales.setTempat_lahir(pasx.getTempat_lahir());
                                if(!Common.isEmpty(pasx.getAddress1())) tmSales.setAddress1(pasx.getAddress1());
                                if(!Common.isEmpty(pasx.getAddress2())) tmSales.setAddress2(pasx.getAddress2());
                                if(!Common.isEmpty(pasx.getHome_phone())) tmSales.setHome_phone(pasx.getHome_phone());
                                if(!Common.isEmpty(pasx.getWork_phone())) tmSales.setWork_phone(pasx.getWork_phone());
                                                                                        
                                result=20;//berhasil                                                
                                //proses utama
                                //insertTmSales(tmSales, con);
                                insertTmSales(tmSales);  
                                tmSalesListPDF.add(tmSales);
                                            /*
                                                //String s_urlid = selectSeqUrlSecureId(con);
                                                String s_urlid =  bacManager.selectSeqUrlSecureId();
                                                
                                                //insertMstUrlSecure(con, s_urlid, "regFreePaDmtm", identity, "campaign="+campaign+"~cust="+identity+"~mobile="+originator+"~sertifikat="+tmSales.getNo_sertifikat());
                                                String id_encrypt_1 = elionsManager.selectEncryptDecrypt(s_urlid, "e");
                                                String id_encrypt_2 = elionsManager.selectEncryptDecrypt("regFreePaDmtm", "e");
                                                bacManager.insertMstUrlSecure(s_urlid, "regFreePaDmtm", identity, "campaign="+campaign+"~cust="+identity+"~mobile="+tmSales.getMobile_no()+"~sertifikat="+tmSales.getNo_sertifikat(), id_encrypt_1, id_encrypt_2);
                                                
                                                //List<Map> lsSecure=db.querySelect("select ID_ENCRYPT_1, ID_ENCRYPT_2 from eka.mst_url_secure where ID_SECURE_1='"+s_urlid+"'");
                                                //String id_encrypt_1 = (String) lsSecure.get(0).get("ID_ENCRYPT_1");
                                                //String id_encrypt_2 = (String) lsSecure.get(0).get("ID_ENCRYPT_2");
                                                                                                
//                                              getServer().emailin_nasabah(true, getMainProperty("email.admin"), tmSales.getEmail().split(";"), null, new String[]{"andy@sinarmasmsiglife.co.id"}, "Komfirmasi Aktivasi Asuransi Kecelakaan Diri dari Sinarmas MSIG Life", "Dear Nasabah, \nSelamat! Anda mendapatkan Asuransi Kecelakaan Diri senilai Rp 50juta dari Sinarmas MSIG Life. \nAktivasi paling lambat 25 Okt 2014. Salam SMiLe \n\n"+"http://ews.sinarmasmsiglife.co.id/freepa/"+id_encrypt_1+"/"+id_encrypt_2, null);
                                                                                                
                                                EmailPool.send("SMiLe SMS Service", 0, 0, 0, 0, 
                                                        null, 0, 0, sysdate, null, 
                                                        true,
                                                        "info@sinarmasmsiglife.co.id",                                                      
                                                        new String[]{email}, 
                                                        null, new String[]{"andy@sinarmasmsiglife.co.id","adrian_n@sinarmasmsiglife.co.id"}, 
                                                        "Aktivasi Polis SMiLe Personal Accident", 
                                                        "Nasabah Terhormat,<br><br>"+
                                                                "Anda telah terdaftar sebagai pemegang polis asuransi Kecelakaan Diri (SMiLe Personal Accident) senilai Rp. 50 juta dari Sinarmas MSIG Life.<br>"+
                                                                "Segera lindungi diri Anda melalui manfaat asuransi yang dibutuhkan dengan melakukan aktivasi polis.<br>"+
                                                                "Untuk mengaktifkannya, klik link <a href='http://ews.sinarmasmsiglife.co.id/api/activate/freepa/"+id_encrypt_1+"/"+id_encrypt_2+"' >disini</a>  !! <br><br>"+
                                                                "Terima kasih.<br><br>"+
                                                                "Salam Hangat,<br>"+
                                                                "Sinarmas MSIG Life" 
                                                        , null, null);*/                                                
                                            //==========
                                     }  
                    } catch (Exception e) {             // TODO Auto-generated catch block
                        err = "error";
                        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();                    
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("sts", "FAILED");
                        map.put("msg", "FAILED (CETAK POLIS " + jns_pas + ") : Proses Cetak Polis Di-Batalkan, karena ada Error!!  Mohon cek kembali Validitas Data!");                               
                        successMessageList.add(map);                
                        
                        logger.error("ERROR :", e);
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        String error=sw.toString();
                        try {
                            sw.close();
                            pw.close();
                        } catch (IOException e1) {
                            logger.error("ERROR :", e1);
                        }
                        //  getServer().emailin(true, getMainProperty("email.admin"), getMainProperty("email.send_error_to").split(";"), null, new String[]{"andy@sinarmasmsiglife.co.id"}, "SMSServer PROSES SMS FREE PA Error", "Dear Admin, \n Terjadi kesalahan pada aplikasi saat melakukan proses registrasi. \nBerikut detailnya : \n\nNO HP : "+originator+"\nISI SMS : "+txt+"\nError : \n"+error, null);
                        //  pasSMS.setMsp_kode_sts_sms("09");
                       //   pasSMS.setMsp_desc_sts_sms("WRONG FORMAT");
                    }       
                }       
    //      OutboundMessage msgOut=new OutboundMessage(originator,txtReply);
    //      getService().sendMessage(msgOut);
    //      pasSMS.setId_ref(idnext);
    //      pasSMS.setMsp_text(txt);
    //      pasSMS.setDist("01");
    //      pasSMS.setMsp_mobile(originator);
    //      pasSMS.setMsp_message_date(msg.getDate());
    //      pasSMS.setMsp_receive_date(new Date());
    //      pasSMS.setLspd_id(1);
    //      pasSMS.setLssp_id(1);
    //      pasSMS.setJenis_pas(jenis);
            } catch (Exception e) {         // TODO Auto-generated catch block
                err = "error";
                TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sts", "FAILED");
                map.put("msg", "FAILED (CETAK POLIS " + jenis_pas + ") : Proses Cetak Polis Di-Batalkan, karena ada Error!!  Mohon cek kembali Validitas Data!");                               
                successMessageList.add(map);
                
                logger.error("ERROR :", e);
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String error=sw.toString();
                try {
                    sw.close();
                    pw.close();
                } catch (IOException e1) {
                    logger.error("ERROR :", e1);
                }
    //          insertSMSOut(originator, "error : "+error,idnext, db,msg.getGatewayId());
    //          getServer().emailin(true, getMainProperty("email.admin"), getMainProperty("email.send_error_to").split(";"), null, new String[]{"andy@sinarmasmsiglife.co.id"}, "SMSServer PROSES SMS FREE PA Error", "Dear Admin, \n Terjadi kesalahan pada aplikasi saat melakukan proses registrasi. \nBerikut detailnya : \n\nNO HP : "+originator+"\nISI SMS : "+txt+"\nError : \n"+error, null);
    //          txtReply=getMainProperty("sms.freepa.reply.reg.wrong_format");
                result=29;
    //          pasSMS.setMsp_kode_sts_sms("09");
    //          pasSMS.setMsp_desc_sts_sms("WRONG FORMAT");
            }       
        //db.comit();   
        }
        
        if("".equals(err)){
            for(TmSales pasx : tmSalesListPDF){ 
                String no_sertifikat = pasx.getNo_sertifikat();
                String[] no_sertifikat_arr = no_sertifikat.split("-");
                String no_polis_induk = no_sertifikat_arr[0] + "-" + no_sertifikat_arr[1] + "-" + no_sertifikat_arr[2];
                String nama_pp = "CMC Pasar Keuangan Rakyat OJK 2014";
                String nama_tt = pasx.getHolder_name();
                Date tgl_lahir = pasx.getBod_holder();
                Date sysdate = selectSysdate();
                String email = pasx.getEmail();
                Date beg_date = pasx.getBeg_date();
                Date end_date = pasx.getEnd_date();         
            
                try {
                        if("060".equals(pasx.getProduct_code())) {
                            nama_pp = "Bank DKI";
                            String upText = "Rp. 25.000.000,- (dua puluh lima juta rupiah)";
                            String insperText = FormatDate.toIndonesian(pasx.getBeg_date()) + " s/d " + FormatDate.toIndonesian(pasx.getEnd_date());
                            ITextPdf.generateSertifikatFreePaDmtm(no_sertifikat, no_polis_induk, nama_pp, nama_tt, tgl_lahir, sysdate, pasx.getProduct_code(), upText, insperText);
                        } else if("061".equals(pasx.getProduct_code())) {
                            nama_pp = "Bank DKI";
                            String upText = "Rp. 1.000.000,- (satu juta rupiah)";
                            String insperText = FormatDate.toIndonesian(pasx.getBeg_date()) + " s/d " + FormatDate.toIndonesian(pasx.getEnd_date());
                            ITextPdf.generateSertifikatFreeDbdDmtm(no_sertifikat, no_polis_induk, nama_pp, nama_tt, tgl_lahir, sysdate, pasx.getProduct_code(), upText, insperText);
                        } else {
                            ITextPdf.generateSertifikatFreePaDmtm(no_sertifikat, no_polis_induk, nama_pp, nama_tt, tgl_lahir, sysdate);
                        }
                        
                        List<File> attachments = new ArrayList<File>();
                        String polisPath = "\\\\ebserver\\pdfind\\Polis";
//                        String polisPath = "\\\\ebserver\\pdfind\\Polis_Testing";
                        String docPath = "\\free_pa\\"+no_sertifikat+".pdf";
                        
                        if("060".equals(pasx.getProduct_code())) {
                            docPath = "\\free_pa\\060\\"+no_sertifikat+".pdf";
                        } else if ("061".equals(pasx.getProduct_code())) {
                            docPath = "\\free_dbd\\061\\"+no_sertifikat+".pdf";
                        }
                        
                        File file = new File(polisPath + docPath);
                        attachments.add( file );
                        
                        String emailSubject = "Polis SMiLe Personal Accident";
                        String emailMessage = "Nasabah Terhormat,<br><br>"+
                                "Terima kasih telah melakukan aktivasi polis dan memberikan kepercayaan Anda kepada kami melalui produk perlindungan kecelakaan diri SMiLe Personal Accident<br>"+
                                "Terlampir adalah sertifikat polis sebagai panduan Anda dalam memahami ketentuan produk secara ringkas.<br><br>"+
                                "Terima kasih.<br><br>"+
                                "Salam Hangat,<br>"+
                                "Sinarmas MSIG Life";
                        
                        if("060".equals(pasx.getProduct_code())) {
                            emailMessage = "Nasabah Terhormat,<br><br>"+
                                    "Selamat, Anda telah terdaftar sebagai pemegang polis asuransi Kecelakaan Diri (SMiLe Personal Accident) dari Sinarmas MSIG Life.<br>"+
                                    "Terlampir adalah sertifikat polis sebagai panduan Anda dalam memahami ketentuan produk secara ringkas.<br><br>"+
                                    "Terima kasih.<br><br>"+
                                    "Salam Hangat,<br>"+
                                    "Sinarmas MSIG Life";
                        } else if("061".equals(pasx.getProduct_code())) {
                            emailSubject = "Polis SMiLe Demam Berdarah";
                            emailMessage = "Nasabah Terhormat,<br><br>"+
                                    "Selamat, Anda telah terdaftar sebagai pemegang polis asuransi Demam Berdarah dari Sinarmas MSIG Life.<br>"+
                                    "Terlampir adalah sertifikat polis sebagai panduan Anda dalam memahami ketentuan produk secara ringkas.<br><br>"+
                                    "Terima kasih.<br><br>"+
                                    "Salam Hangat,<br>"+
                                    "Sinarmas MSIG Life";
                        }
                        
                        String[] emailCc = new String[]{"ningrum@sinarmasmsiglife.co.id"};
                        String[] emailBcc = new String[]{"andy@sinarmasmsiglife.co.id;adrian_n@sinarmasmsiglife.co.id"};
                        
                        if("060,061".indexOf(pasx.getProduct_code()) > -1) {
                            emailCc = new String[]{"ningrum@sinarmasmsiglife.co.id", "dwidharmap@bankdki.co.id", "dwidharmaprasetyo@gmail.com", "gpd@bankdki.co.id", "fiqadki@yahoo.com", "lylianty@sinarmasmsiglife.co.id", "yantisumirkan@sinarmasmsiglife.co.id"};
                            emailBcc = new String[] {"daru@sinarmasmsiglife.co.id"};
                        }
                        
                        EmailPool.send(selectSeqEmailId(),"SMiLe E-Lions", 0, 0, 0, 0, 
                                null, 0, 0, sysdate, null, true, "info@sinarmasmsiglife.co.id",                                             
                                new String[]{email}, 
//                              new String[]{"ningrum@sinarmasmsiglife.co.id"},
                                emailCc,
//                              new String[]{"andy@sinarmasmsiglife.co.id;adrian_n@sinarmasmsiglife.co.id"}, 
                                emailBcc,
                                emailSubject, 
                                emailMessage,
                                attachments,11);
                        
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        logger.error("ERROR :", e);
                        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("sts", "FAILED");
                        map.put("msg", "FAILED (CETAK POLIS " + jenis_pas + ") : Proses Cetak Polis Di-Batalkan, karena ada Error!!  Mohon cek kembali Validitas Data!");                           
                        successMessageList.add(map);            
                } catch (DocumentException e) {
                        // TODO Auto-generated catch block
                        logger.error("ERROR :", e);
                        TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("sts", "FAILED");
                        map.put("msg", "FAILED (CETAK POLIS " + jenis_pas + ") : Proses Cetak Polis Di-Batalkan, karena ada Error!!  Mohon cek kembali Validitas Data!");                               
                        successMessageList.add(map);
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sts", "SUCCEED");
                map.put("msg", "Sukses Cetak Polis " + jenis_pas + " dengan No.Sertifikat: "+pasx.getNo_sertifikat()+" ,Nama TTG: "+pasx.getHolder_name());                         
                successMessageList.add(map);
            }
                 
        }else{
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sts", "FAILED");
            map.put("msg", "FAILED (CETAK POLIS " + jenis_pas + ") : Proses Cetak Polis Di-Batalkan, karena ada Error!!  Mohon cek kembali Validitas Data!");                               
            successMessageList.add(map);
        }
        
        return successMessageList;
    }   
    
	// Pindahan dari PaPartnerUploadFormController
	public String genSertifikatTmSales( TmSales tmSales){
        String id="";
        try{//select * from eka.lst_tm_product where product_code=#value#
        List<Map> lspin =  selectLspin(tmSales.getProduct_code());
        //  List<Map> lspin=db.querySelect("select NOMOR, NOMOR2, NO_POLIS from eka.lst_tm_product@EB where product_code='"+tmSales.getProduct_code()+"'");
            Map tm=lspin.get(0);
            BigDecimal nomor=(BigDecimal) tm.get("NOMOR");
            BigDecimal nomor2=(BigDecimal) tm.get("NOMOR2");
            String no_polis=(String) tm.get("NO_POLIS");
                
            nomor=nomor.add(new BigDecimal(1));
            id=no_polis+"-"+FormatString.rpad("0", nomor.toString(), 6);
//              Map param=new HashMap<String, Object>();
//              param.put("nomor", nomor);
//              param.put("nomor2", nomor2);
//              param.put("product_code", tmms.getProduct_code());              
                // updateTmProduct(nomor, nomor2, tmSales.getProduct_code(), con);
                
            updateTmProduct(nomor, nomor2, tmSales.getProduct_code());
        }catch (Exception e) {
//               TODO: handle exception
            TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
            logger.error("ERROR :", e);
        }
        return id;
    }
	
	public List<Followup> selectFollowupBilling2(String jenis, Integer aging, String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String jn, String spaj) {
		return basDao.selectFollowupBilling2(jenis, aging, begdate, enddate, lus_id, stfu, cabang, admin, jn, spaj);
	}
	
	public List<Followup> selectReportFollowupBillingPerUserCount(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String kat, String rep, String jn_tgl) {
		return basDao.selectReportFollowupBillingPerUserCount(begdate, enddate, lus_id, stfu, cabang, admin, kat, rep, jn_tgl);
	}
	
	public ArrayList<AutoPaymentVA> selectListPaymentBSM(){
		return Common.serializableList(uwDao.selectListPaymentBSM());
	}
	
	public CmdAutoPaymentVA prosesAutoPaymentBSM(CmdAutoPaymentVA command, User currentUser){
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String s_spaj = "";
		String s_spaj2 = "";
		
		try{
		    for(int i=0;i<command.getListSpaj().size();i++){
		    	AutoPaymentVA apbsm = command.getListSpaj().get(i);
		    	TopUp tp = new TopUp();
		    	s_spaj = apbsm.getReg_spaj();
		    	
		    	if(apbsm.getCheck() != null){
		    		Double saldo = uwDao.selectCheckTotalUsedMstDrek(apbsm.getNo_trx(), null, null, null);

		    		if(saldo.doubleValue() >= apbsm.getPremi_pokok().doubleValue()){
			    		tp.setMspa_payment_id( "" );			
						tp.setLca_polis( "40" );
						tp.setReg_spaj( s_spaj );
						tp.setLku_id( apbsm.getLku_id() );
						tp.setLsjb_id( 1 );
						tp.setClient_bank( "156" );
						tp.setMspa_no_rek( apbsm.getNo_rek() );
						tp.setPay_date( df.parse(apbsm.getTgl_trx()) );
						tp.setMspa_due_date( null );
						tp.setMspa_date_book( df.parse(apbsm.getTgl_trx()) );
						tp.setMspa_payment( new Double(apbsm.getPremi_pokok().doubleValue()) );
						tp.setMspa_desc( "CEK IBANK (No Transaksi " + apbsm.getNo_trx() + ")" );
						tp.setLus_id( currentUser.getLus_id() );
						tp.setMspa_active( 1 );
						tp.setLsrek_id( accountingDao.selectRekEkalifeByRekAJS(apbsm.getNorek_ajs()) );
						tp.setMspa_nilai_kurs( new Double(apbsm.getNilai_kurs()) );
						tp.setMspa_no_pre( apbsm.getNo_pre() );
						tp.setMspa_flag_merchant( null );

						tp.setTahun_ke(1);
						tp.setPremi_ke(1);
						
		    			Date nowDate = commonDao.selectSysdate("dd", false, 0);
			    		String no_urut = uwDao.selectMaxNoUrutMstDrekDet(apbsm.getNo_trx()) ;
						if(StringUtil.isEmpty(no_urut)){
							no_urut = "0";
						}
						
						tp = uwDao.insertTopUp(tp, 9, currentUser);
						uwDao.updateMstDrekListRk(apbsm.getReg_spaj(), currentUser.getLus_id(), apbsm.getNo_trx(), apbsm.getFlag_tunggal_gabungan(), apbsm.getJenis_trx());
						uwDao.insertMstDrekDet(apbsm.getNo_trx(), "1", "1", apbsm.getPremi_pokok().doubleValue(), LazyConverter.toInt(no_urut) + 1, apbsm.getReg_spaj(), 1,
								tp.getMspa_payment_id(), currentUser.getLus_id(), nowDate, apbsm.getNo_pre(), apbsm.getNorek_ajs(), apbsm.getJenis_trx(), df.parse(apbsm.getTgl_trx()));

				    	s_spaj2 = s_spaj2 + s_spaj + "; ";
		    		}
		    	}
		    }
		    command.setPesan("Proses Auto Payment Untuk SPAJ Berikut Telah Berhasil. " + s_spaj2);
		    
		}catch (Exception e) {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			logger.error("ERROR :", e);
			command.setPesan("Terjadi Permasalahan Pada Proses Auto Payment untuk SPAJ No. " + s_spaj);
		}
		return command;
	}
	
	public List<Map> selectCekSpajPromoUF( String beg_date_promo, String end_date_promo, String spaj, String jn_promo , Integer jn_spaj) {
		return bacDao.selectCekSpajPromoUF(  beg_date_promo,  end_date_promo,  spaj, jn_promo, jn_spaj);
	}
	
	public void insertPromoFreeSpaj(String reg_spaj_primary,Integer row_num, String reg_spaj_free, Integer process_type, Integer program_type) {
		bacDao.insertPromoFreeSpaj(reg_spaj_primary, row_num, reg_spaj_free, process_type, program_type);
	}
	
	public List<Map> selectCekSpajPromo( String spajReff ,String spajFree, String jn_promo) {
		return bacDao.selectCekSpajPromo(  spajReff , spajFree,  jn_promo);
	}
	
	public void updatePromoFreeSpaj(String spajReff ,String spajFree, Integer process_type) {
		bacDao.updatePromoFreeSpaj(spajReff , spajFree,  process_type);
	}
	
	public List<Map> selectCountandvalidSpajPromo( String beg_date_promo, String end_date_promo, String wil_no, String jn_promo, Integer jn_cek) {
		return bacDao.selectCountandvalidSpajPromo(  beg_date_promo,  end_date_promo,  wil_no, jn_promo,  jn_cek);
	}
	
	public void updateflagSpecialOffer(String spaj ,Integer flag_special_offer) {
		bacDao.updateflagSpecialOffer(spaj ,  flag_special_offer);
	}
	
	public ArrayList<Promo> selectListPromoChecklistFreeProd(){
		return Common.serializableList(bacDao.selectListPromoChecklistFreeProd());
	}
	
	public CmdPromo prosesApproveSpajFree(CmdPromo command, User currentUser, HttpServletRequest request, HttpServletResponse response, ElionsManager eliosManager2, UwManager uwManager2) {
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String s_spaj_utama = "";
		String s_spaj_free = "";
		Integer proses_tipe = 0;
		String stat_fin = "";
//		CmdAutoPaymentVA data = new CmdAutoPaymentVA();
//		data.setListSpaj(new ArrayList<AutoPaymentVA>());
		
		if(request.getParameter("btnApprove") != null) {
			proses_tipe = 1;
			stat_fin = "APPROVED";
		}else if (request.getParameter("btnReject") != null){
			proses_tipe = 2;
			stat_fin = "REJECTED";
		}
		
		try{
		    for(int i=0;i<command.getListSpaj().size();i++){
		    	Promo pls = command.getListSpaj().get(i);
		    	DepositPremium dp = new DepositPremium();
		    	s_spaj_utama = pls.getSpaj_utama();
		    	s_spaj_free = pls.getSpaj_free();
		    	
		    	if(pls.getCheck() != null){
		    		bacDao.updatePromoFreeSpaj(s_spaj_utama , s_spaj_free,  proses_tipe);
		    		eliosManager2.insertMstPositionSpaj(currentUser.getLus_id(), "Persetujuan Program Buy 1 Get 1 Free Smile Medical dengan Reff SPAJ "+s_spaj_utama +" : " + stat_fin, s_spaj_free, 1);
			    }
		    }	
		    
		    command.setPesan("Proses "+ stat_fin +" Checklist Free Produk berhasil d proses</b><br>");
		    	
		}catch (Exception e) {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			logger.error("ERROR :", e);
			command.setPesan("Terjadi Permasalahan Pada Proses "+stat_fin+" Checklist Free Produk untuk SPAJ No. " + s_spaj_utama);
		}
		return command;
	}
	
	public Integer selectHitungApe(String reg_spaj){
		return bacDao.selectHitungApe(reg_spaj);
	}
	
	public List selectKanwil711(){
		return bacDao.selectKanwil711();
	}
	
	public List selectProducFreeP1(){
		return bacDao.selectProducFreeP1();
	}
	
	public List selectReportProductFreeSmileM(Map map) {
		return bacDao.selectReportProductFreeSmileM(map);
	}
	
	public BigDecimal selectMstSpajFree(String reg_spaj){
		return uwDao.selectMstSpajFree(reg_spaj);
	}
	
	public List selectProductDMTMDS(){
		return bacDao.selectProductDMTMDS();
	}
	
	public List selectReportProductDMTMDS(Map map) {
		return bacDao.selectReportProductDMTMDS(map);
	}
	
	public List selectDetailKartuPasBSMIB(String no_kartu) {
		return bacDao.selectDetailKartuPasBSMIB(no_kartu);
	}
	
	/*public void insertMstPositionSpajProc(String lus_id, String msps_desc, String reg_spaj, String jenis){
		this.uwDao.insertMstPositionSpajProc(lus_id, msps_desc, reg_spaj, jenis);
	}*/
	
	public List<Map> selectMst_Crp_Result( String regspaj) {
		return bacDao.selectMst_Crp_Result( regspaj );
	}
	
	public String insertMst_Crp_Result(String regspaj , Integer question_id , Integer answer_id ) throws DataAccessException {
		return this.bacDao.insertMst_Crp_Result(regspaj,question_id,answer_id );
	}
	
	public String updateMst_Crp_Result(String regspaj , Integer question_id , Integer answer_id) throws DataAccessException  {
    	return this.bacDao.updateMst_Crp_Result(regspaj,question_id,answer_id );
    }
	
	public Map cekInfoScoreRiskProfile(Integer score){
		
		String tinkrisk ="", riskproduk="",profilrisk = "";
		Map params=new HashMap();
		
		if(score >= 20 && score <= 50 ){
			tinkrisk = "Conservative";
			riskproduk ="Rendah";
			profilrisk = "Menggambarkan profil investor yang hanya dapat menerima kelas aset dengan risiko dan fluktuasi harga yang relatif rendah serta mendapatkan hasil investasi yang lebih baik daripada deposito dan tingkat inflasi.";
		}else if(score >= 51 && score <= 70 ){
			tinkrisk = "Balance";
			riskproduk ="Menengah";
			profilrisk = "Menggambarkan profil investor yang cocok untuk kelas aset dengan risiko menengah dan fluktuasi harga yang akan dicapai melalui modal laba jangka panjang.";
		}else if(score >= 71 && score <= 100 ){
			tinkrisk = "Aggressive";
			riskproduk ="Tinggi";
			profilrisk = "Menggambarkan profil investor yang cocok untuk kelas aset dengan risiko tinggi dan fluktuasi harga yang signifikan untuk mencapai pertumbuhan modal maksimal.";
		}
		
		params.put("score",score);
	    params.put("tinkrisk",tinkrisk);
	    params.put("riskproduk",riskproduk);
	    params.put("profilrisk",profilrisk);
		
		return params;
	}
	
	public void updateMst_policy_CRPResult(String regspaj , Integer mspocrpscore , String mspocrpresult ) throws DataAccessException {
		bacDao.updateMst_policy_CRPResult(regspaj,mspocrpscore,mspocrpresult);
	}
	
	public String selectTotalFilterSpaj2SimasPrima(String posisi, String tipe, String kata, String pilter, Integer jn_bank, String tgl_lahir) {
		return this.uwDao.selectTotalFilterSpaj2SimasPrima(posisi, tipe, kata, pilter, jn_bank, tgl_lahir);
	}
	
	public String selectTotalFilterSpaj2(String posisi, String tipe, String kata, String pilter, String lssaId,String lsspId, String tgl_lahir) {
		return this.uwDao.selectTotalFilterSpaj2(posisi, tipe, kata, pilter,lssaId,lsspId, tgl_lahir);
	}
	
	public String selectTotalFilterSpaj2_valid(String posisi, String tipe, String kata, String pilter, String lssaId,Integer lus_id,String lsspId, String tgl_lahir) {
		return this.uwDao.selectTotalFilterSpaj2_valid(posisi, tipe, kata, pilter,lssaId,lus_id,lsspId, tgl_lahir);
	}
	
	public Integer retrieveAnswerIdtoValue(String answerid){
		
		Integer valueanswer = 0;
		if("002,008,013,019,024,029,035,041,046,052".indexOf(answerid)>-1 ){
			valueanswer = 2;
		}else if("003,009,014,020,025,030,036,042,047,053".indexOf(answerid)>-1 ){
			valueanswer = 4;
		}else if("004,010,015,021,026,031,037,043,048,054".indexOf(answerid)>-1 ){
			valueanswer = 6;
		}else if("005,011,016,022,027,032,038,044,049,055".indexOf(answerid)>-1 ){
			valueanswer = 8;
		}else if("006,017,033,039,050".indexOf(answerid)>-1 ){
			valueanswer = 10;
		}
		
		return valueanswer;
	}

	public List<Map> selectMst_Crp_Result_ESPAJ( String regspaj) {
		return bacDao.selectMst_Crp_Result_ESPAJ( regspaj );
	}
	
	public String transferDataMstCrpResultFromMstCrpTemp(String regspaj ) throws DataAccessException {
		
		String pesan = "";
		try{
			bacDao.insertMstCrpResultFromMstCrpTemp(regspaj);
			
			List dataQuestionareRPtransferEspaj = selectMst_Crp_Result(regspaj);
			int score = 0 ; 	
			
			if(!dataQuestionareRPtransferEspaj.isEmpty()){
				for(int j=0; j<dataQuestionareRPtransferEspaj.size();j++){
					
					Map y = (HashMap) dataQuestionareRPtransferEspaj.get(j);
					String answerid = FormatString.rpad("0",y.get("ANSWER_ID").toString(), 3);
					Integer valueanswer = retrieveAnswerIdtoValue(answerid);
					score = score + valueanswer ;
				
				}
			}
			
			//Score detail Information setelah ada pembaharuan/ penambahan score
			Map info = cekInfoScoreRiskProfile(score);
			//update data MSPO_CRP_SCORE dan MSPO_CRP_RESULT di mst_policy
			String infoDesc = (String) info.get("tinkrisk")+"-"+(String) info.get("riskproduk")+"-"+(String) info.get("profilrisk");
			updateMst_policy_CRPResult(regspaj,score,infoDesc);
			
		}catch (Exception e) {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			logger.error("ERROR :", e);
			pesan = "ERROR";
		}
		return pesan;
	}
	
	/**Fungsi:	Untuk menampilkan data AMLCFT_Monitoring
	 * @param 	dariTanggal
	 * @param 	sampaiTanggal
	 * @return	List
	 * @author 	Ridhaal
	 */
	public List selectlistAMLCFT_Monitoring(String dariTanggal,String sampaiTanggal){
		return bacDao.selectlistAMLCFT_Monitoring(dariTanggal,sampaiTanggal);
	}
	
	public List selectDataEspajBas(Map params) {		
		return basDao.selectDataEspajBas(params);
	}
	
	public ArrayList<AutoPaymentVA> selectListPaymentBancass(){
		return Common.serializableList(uwDao.selectListPaymentBancass());
	}
	
	public CmdAutoPaymentVA prosesAutoPaymentBancass(CmdAutoPaymentVA command, User currentUser, HttpServletRequest request, HttpServletResponse response, ElionsManager elionsManager2, UwManager uwManager2){
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String s_spaj = "";
		
		String s_spaj_ok = "";
		String s_spaj_gtp = ""; //gagal transfer ke payment
		
		String s_spaj_ok2 = "";
		String s_spaj_ntgv = ""; //gagal validasi	
		String s_spaj_ntgpp = ""; //gagal proses paralel		
		
		try{
		    for(int i=0;i<command.getListSpaj().size();i++){
		    	AutoPaymentVA data = command.getListSpaj().get(i);
		    	s_spaj = data.getReg_spaj();
		    	String lca_id = uwDao.selectLcaIdMstPolicyBasedSpaj(s_spaj);
		    	Integer lspd = data.getLspd_id();
		    	
		    	if(data.getCheck() != null){
		    		Double saldo = uwDao.selectCheckTotalUsedMstDrek(data.getNo_trx(), null, null, null);
		    		if(saldo.doubleValue() >= data.getTotal_premi().doubleValue()){
			    		if(lspd == 4){//Posisi di Payment
					    	TopUp tp = new TopUp();
					    	
					    	Integer premiKe = 1;
				    		tp.setMspa_payment_id( "" );			
							tp.setLca_polis( lca_id );
							tp.setReg_spaj( s_spaj );
							tp.setLku_id( data.getLku_id() );
							tp.setLsjb_id( 1 );
							tp.setClient_bank( String.valueOf(data.getLsbp_id()) );
							tp.setMspa_no_rek( data.getNo_rek() );
							tp.setPay_date( df.parse(data.getTgl_trx()) );
							tp.setMspa_due_date( null );
							tp.setMspa_date_book( df.parse(data.getTgl_trx()) );
							tp.setMspa_desc( "CEK IBANK (No Transaksi " + data.getNo_trx() + ")" );
							tp.setLus_id( currentUser.getLus_id() );
							tp.setMspa_active( 1 );
							tp.setLsrek_id( accountingDao.selectRekEkalifeByRekAJS(data.getNorek_ajs()) );
							tp.setMspa_nilai_kurs( new Double(data.getNilai_kurs()) );
							tp.setMspa_no_pre( data.getNo_pre() );
							tp.setMspa_flag_merchant( null );

				    		for(int j=0; j<3; j++){
					    		Double jml_bayar = new Double(0);
					    		Integer flag_topup = null;
								if(j==0){
									jml_bayar = data.getPremi_pokok().doubleValue();
								}else if(j==1){
									jml_bayar = data.getPremi_topup_berkala().doubleValue();
									flag_topup = 2;
								}else{
									jml_bayar = data.getPremi_topup_tunggal().doubleValue();
									flag_topup = 1;
								}
								
								if(jml_bayar.doubleValue() != new Double(0)){
					    			Date nowDate = commonDao.selectSysdate("dd", false, 0);
						    		String no_urut = uwDao.selectMaxNoUrutMstDrekDet(data.getNo_trx()) ;
									if(StringUtil.isEmpty(no_urut)){
										no_urut = "0";
									}

									tp.setMspa_payment( jml_bayar );
									tp.setTahun_ke( 1 );
									tp.setPremi_ke( premiKe );
									tp = uwDao.insertTopUp(tp, 9, currentUser);
									uwDao.updateMstDrekListRk(data.getReg_spaj(), currentUser.getLus_id(), data.getNo_trx(), data.getFlag_tunggal_gabungan(), data.getJenis_trx());
									uwDao.insertMstDrekDet(data.getNo_trx(), "1", "1", data.getPremi_pokok().doubleValue(), LazyConverter.toInt(no_urut) + 1, data.getReg_spaj(), 1,
											tp.getMspa_payment_id(), currentUser.getLus_id(), nowDate, data.getNo_pre(), data.getNorek_ajs(), data.getJenis_trx(), df.parse(data.getTgl_trx()));
									
									premiKe = premiKe + 1;
								}
				    		}

							BindException error = new BindException(new HashMap(), "cmd");
							ArrayList billInfoForTransfer = Common.serializableList( this.uwDao.selectBillingInfoForTransfer(tp.getReg_spaj(), 1, 1));
							boolean hasil = this.transferPolis.transferToPrintPolis(tp.getReg_spaj(), error, 0, billInfoForTransfer, currentUser, "1", "3", "payment", elionsManager2, uwManager2);		
							if(!hasil){
								s_spaj_gtp = s_spaj_gtp  + "<br>"+ s_spaj + ";<br>";
							}else{
								s_spaj_ok = s_spaj_ok  + "<br>"+ s_spaj + ";<br>";
							}
			    			
			    		}else{//Posisi selain di Payment
	    			    	DepositPremium dp = new DepositPremium();

				    		Integer premiKe = 1;
				    		dp.setReg_spaj( data.getReg_spaj() );
				    		dp.setMsdp_jtp( 1 );
				    		dp.setMsdp_flag( "B" );
				    		dp.setLsjb_id( 1 );
				    		dp.setMsdp_pay_date( df.parse(data.getTgl_trx()) );
				    		dp.setMsdp_date_book( df.parse(data.getTgl_trx()) );
				    		dp.setLku_id( data.getLku_id() );
				    		dp.setMsdp_selisih_kurs( new Double(data.getNilai_kurs()) );
				    		dp.setMsdp_desc( "CEK IBANK (No Transaksi " + data.getNo_trx() + ")" );
				    		dp.setLus_id( Integer.parseInt(currentUser.getLus_id()) );
				    		dp.setMsdp_active( 0 );
				    		dp.setLsrek_id( Integer.parseInt(accountingDao.selectRekEkalifeByRekAJS(data.getNorek_ajs())) );
				    		dp.setMsdp_no_pre( data.getNo_pre() );
				    		dp.setMsdp_jurnal( 0 );
				    		
				    		for(int j=0; j<3; j++){
					    		Double jml_bayar = new Double(0);
					    		Integer flag_topup = null;
								if(j==0){
									jml_bayar = data.getPremi_pokok().doubleValue();
								}else if(j==1){
									jml_bayar = data.getPremi_topup_berkala().doubleValue();
									flag_topup = 2;
								}else{
									jml_bayar = data.getPremi_topup_tunggal().doubleValue();
									flag_topup = 1;
								}
								
								if(jml_bayar.doubleValue() != new Double(0)){
					    			Date nowDate = commonDao.selectSysdate("dd", false, 0);
						    		String no_urut = uwDao.selectMaxNoUrutMstDrekDet(data.getNo_trx()) ;
									if(StringUtil.isEmpty(no_urut)){
										no_urut = "0";
									}
									dp.setMsdp_number(premiKe);
									dp.setMsdp_payment(jml_bayar);
									dp.setMsdp_input_date(nowDate);
									dp.setMsdp_flag_topup(flag_topup);
									
									bacDao.insertMst_deposit_premium(dp);
									uwDao.updateMstDrekListRk(data.getReg_spaj(), currentUser.getLus_id(), data.getNo_trx(), data.getFlag_tunggal_gabungan(), data.getJenis_trx());
									uwDao.insertMstDrekDet(data.getNo_trx(), "1", premiKe.toString(), jml_bayar, LazyConverter.toInt(no_urut) + 1, data.getReg_spaj(), premiKe,
											null, currentUser.getLus_id(), nowDate, data.getNo_pre(), data.getNorek_ajs(), data.getJenis_trx(), df.parse(data.getTgl_trx()));
									
									premiKe = premiKe + 1;
								}
				    		}
				    		
				    		Map validasi = new HashMap();
				    		validasi = this.validasiNewBusiness(s_spaj, currentUser, 2);
				    		Integer stat_gagal = 0;
				    		
				    		if(!validasi.isEmpty()){
				    			s_spaj_ntgv = s_spaj_ntgv + "<br>"+ s_spaj + validasi.toString() + "; "; 
								stat_gagal = 1;
							}
			    						
							if(stat_gagal == 0){
								Pemegang pmg = elionsManager2.selectpp(s_spaj);
								Tertanggung tertanggung = elionsManager2.selectttg(s_spaj);
								Datausulan dataUsulan = elionsManager2.selectDataUsulanutama(s_spaj);
								BindException err = null;
								Map mapAutoAccept = null;
								mapAutoAccept = this.ProsesParalel(2, s_spaj, 1,pmg,tertanggung, dataUsulan, currentUser,request,err,elionsManager2,uwManager2);
								String ket_sukses = mapAutoAccept.get("success").toString();
								String error = mapAutoAccept.get("error").toString();
								if(mapAutoAccept.get("error").toString().isEmpty()){
									prosesSnows(s_spaj, currentUser.getLus_id(), null, 201);
									s_spaj_ok2 = s_spaj_ok2  + "<br>"+ s_spaj +"<br> - " + mapAutoAccept.get("success").toString().replaceAll("<br>-", " ") + "; ";
								}else{
									elionsManager2.insertMstPositionSpaj(currentUser.getLus_id(), "[GAGAL PROSES ] "+mapAutoAccept.get("error").toString().replaceAll("<br>-", " "), s_spaj, 0);
									s_spaj_ntgpp = s_spaj_ntgpp  + "<br>"+ s_spaj + mapAutoAccept.get("error").toString().replaceAll("<br>-", " ") + "; "; 
									stat_gagal = 2;
								}
			    			}
			    		}
		    		}
		    	}
		    }

			if (s_spaj_ok.equals("")){
				s_spaj_ok = "<b> TIDAK ADA </b>";
			}
			if (s_spaj_gtp.equals("")){
				s_spaj_gtp = "<b> TIDAK ADA </b>";
			}
			if (s_spaj_ok2.equals("")){
				s_spaj_ok2 = "<b> TIDAK ADA </b>";
			}
			if (s_spaj_ntgv.equals("")){
				s_spaj_ntgv = "<b> TIDAK ADA </b>";
			}
			if (s_spaj_ntgpp.equals("")){
				s_spaj_ntgpp = "<b> TIDAK ADA </b>";
			}
		    
		    command.setPesan("<u>(1) Proses Auto Payment dan Transfer ke Print Polis untuk SPAJ berikut telah BERHASIL : </u><b>" + s_spaj_ok +"</b><br><br>"+
		    		"<u>(2) Proses Auto Payment dan Transfer ke Print Polis untuk SPAJ berikut GAGAL : </u><b>" + s_spaj_gtp +"</b><br><br>"+
		    		"<u>(3) Proses Auto Payment dan Approved by Collection untuk SPAJ berikut telah BERHASIL : </u><b>" + s_spaj_ok2 +"</b><br><br>"+
		    		"<u>(4) Proses Auto Payment berhasil, tetapi Approved by Collection GAGAL. silahkan lakukan APPROVE(PROSES NB) secara manual !</u><br><br><i>Gagal Validasi : <i><b> " + s_spaj_ntgv  +
		    		"</b><br><br><i>Gagal Proses Paralel : <i><b>"+ s_spaj_ntgpp +"</b><br>");
		}catch (Exception e) {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			command.setPesan("Terjadi Permasalahan Pada Proses Auto Payment untuk SPAJ No. " + s_spaj);
		}
		return command;
	}
	
    public String selectNoSertifikat(String reg_spaj){
		return bacDao.selectNoSertifikat(reg_spaj);
	}
    
	public String selectWakilFromSpaj(String spaj) {
		if(spaj==null) return ""; else if(spaj.trim().equals("")) return ""; else 
		return this.uwDao.selectWakilFromSpaj(spaj);
	}
	
	public String selectSertifikatTemp(String lca_id, String lwk_id, String lsbs_id){
		return bacDao.selectSertifikatTemp(lca_id, lwk_id, lsbs_id);
	}
	
	public void insertMstSpajCrt(HashMap param){
		bacDao.insertMstSpajCrt(param);
	}
	
	/**
	 * select dropdown peserta Smile Link Plus DMTM
	 * @author 	Ridhaal
	 * @param spaj
	 */
	public List<DropDown> selectDropDownDaftarPesertaSMP(String spaj) {
		return this.bacDao.selectDropDownDaftarPesertaSMP(spaj);
	}
	
	public int update_counter(String IDCounter , Integer number , String lca_id)
	{
		return bacDao.update_counter(IDCounter,number,lca_id);		
	}
	
	public Double hitungBunga(Integer flag_bulanan, Integer mgi, Double premi, Double rate, Integer jumlah_hari, Integer flag_powersave){
		return bacDao.hitungBunga(flag_bulanan, mgi, premi, rate, jumlah_hari, flag_powersave);
	}
	
	public Integer selectFlagPowersave(int lsbs, int lsdbs, int flag_bulanan) {
		return bacDao.selectFlagPowersave(lsbs, lsdbs, flag_bulanan);
	}	
	
	/**
	 * cek SPAJ PEGA / Non PEGA (E-Lions)
	 * @author Ridhaal
	 * @since 21 Feb 2018
	 * @param 
	 * @return Integer
	 * 
	 */
	public Integer selectCekSpajNonPega(String reg_spaj){
		return bacDao.selectCekSpajNonPega(reg_spaj);
	}	
	
	public List selectJumlahContract() { //Chandra A - 20180413
		return bacDao.selectJumlahContract(); 
	}
	
	public List selectDataBelumProd() { //Chandra A - 20180417
		return bacDao.selectDataBelumProd(); 
	}
	
	public int ultimateJobFixing(String reg_spaj) //Chandra A - 20180418
	{
		return bacDao.ultimateJobFixing(reg_spaj);		
	}
	
	public int retryProduction(String noid) //Patar Timotius - 20180418
	{
		return bacDao.retryProduction(noid);		
	}

	public int retryPayment(String noid) //Patar Timotius - 20180418
	{
		return bacDao.retryPayment(noid);		
	}
	
	public List selectDataJsonTemp() { //Chandra A - 20180425
		return bacDao.selectDataJsonTemp(); 
	}
	public List selectFailSiap2UProd() { //Patar Timotius Tambunan - 20180425
		return bacDao.selectFailSiap2UProd(); 
	}

	public List selectFailPayment() { //Patar Timotius Tambunan - 20180425
		return bacDao.selectFailPayment(); 
	}
	public List selectDepositPremiumFailed() { //Patar Timotius Tambunan - 20180425
		return bacDao.selectDepositPremiumFailed(); 
	}

	
	public List selectDaftarInvestasiTemp2(String no_temp){
		return this.bacDao.selectDaftarInvestasiTemp2(no_temp);
	}
	
	public void prosesKomisiErbeAndJurnalKomisiManual(String reg_spaj, BindException errors, User currentUser){
		try {	
			String lca_id = uwDao.selectCabangFromSpaj(reg_spaj);
			
			if (lca_id.equals("73")){
			transferPolis.prosesKomisiErbeAndJurnalKomisiManual(reg_spaj, errors,currentUser);
			}
			
		}catch (Exception e) {
			logger.error("ERROR :", e);
		}
		
	}
	
	public void prosesAkseptasiSpeedy (String s_spaj, String lus_id){
		uwDao.prosesAkseptasiSpeedy(s_spaj, lus_id );
	}
	
	//Mark Valentino 20181121 Kode Penutup
	public HashMap selectMstSpajTempAutoSales(String no_temp) {
		return bacDao.selectMstSpajTempAutoSales(no_temp);
	}	
	
	public String selectKodePenutup(Integer lsbs_id, Integer lsdbs_number) {		
		//return bacDao.selectKodePenutup(lca_id, lwk_id, lsrg_id, lsbs_id, lsdbs_number);
		return bacDao.selectKodePenutup(lsbs_id, lsdbs_number);
	}
	
	public void updateFlagKonfirmasiMstEksternal(String spaj){
		try{
			uwDao.updateFlagKonfirmasiMstEksternal(spaj);
		}catch (Exception e) {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			logger.error("ERROR :", e);
		}
	}	
	
	//project Smile Medical Extra (848-1~70) helpdesk [129135] //Chandra
	public int selectRiderMedicalExtra(String reg_spaj){
		return this.bacDao.selectRiderMedicalExtra(reg_spaj);
	}
	
	/**
	 * helpdesk [133348] email validasi transaksi Direksi/ Dept Head	
	 * @param currentUser
	 * @param reg_spaj
	 * @param ls_jenis
	 * @param idx_aksep
	 * @throws Exception
	 */
	public int paymentintegration(User currentUser,String kode_batch, String ls_jenis) throws Exception{
		int status = 1;
		Boolean is_first_approver = true;
		List<Map> map_kode_id = bacDao.selectMstBatchPembayaran(kode_batch, 0);
		
		if(map_kode_id.size() < 1)
			status = 0;
		else{
			Date nowDate = commonDao.selectSysdate();
			StringBuffer pesan = new StringBuffer();	
			Integer months = nowDate.getMonth()+1;
			Integer years = nowDate.getYear()+1900;
		    String from = "ajsjava@sinarmasmsiglife.co.id";
		    
		    String ket = "";
	    	String email_kirim_cc = "";
	    	String email_kirim_to = "";
	    	String subject_kirim = "";
	    	String subject_kirim_info = "";
	    	String email_approve_to = "";
	    	String email_approve_cc = "";
	    	String subject_approve = "";
	    	String sender_name = "";
	    	String post_desc = "";
	    	String post_jn_desc = "";
	    	String post_desc_approve = "";
	    	String email_cc_akseptor = "";
	    	
	    	String lus_akseptor = "";
	    	String path_ttd = "";
	    	String email_akseptor = "";
	    	String name_akseptor = "";
	    	
	    	try {
		    	List<Map> m = bacDao.selectMstPembayaranApproval(ls_jenis);
		    	
	    		String idx_aksep = "";    		
	    		for(int i = 1; i <= 2; i++){
	    			if(!m.isEmpty()){		
		    			idx_aksep = Integer.toString(i);
						
						Map snDireksi = (Map)m.get(0);
						ket = (snDireksi.get("KET") == null ? "" : (String)snDireksi.get("KET"));
						email_kirim_cc = (snDireksi.get("EMAIL_KIRIM_CC") == null ? "" : (String)snDireksi.get("EMAIL_KIRIM_CC"));
						email_kirim_to = (snDireksi.get("EMAIL_KIRIM_TO") == null ? "" : (String)snDireksi.get("EMAIL_KIRIM_TO"));
						subject_kirim = (snDireksi.get("SUBJECT_KIRIM") == null ? "" : (String)snDireksi.get("SUBJECT_KIRIM"));
						subject_kirim_info = (snDireksi.get("SUBJECT_KIRIM_INFO") == null ? "" : (String)snDireksi.get("SUBJECT_KIRIM_INFO"));
						email_approve_to = (snDireksi.get("EMAIL_APPROVE_TO") == null ? "" : (String)snDireksi.get("EMAIL_APPROVE_TO"));
						email_approve_cc = (snDireksi.get("EMAIL_APPROVE_CC") == null ? "" : (String)snDireksi.get("EMAIL_APPROVE_CC"));
						subject_approve = (snDireksi.get("SUBJECT_APPROVE") == null ? "" : (String)snDireksi.get("SUBJECT_APPROVE"));
						
						sender_name = (snDireksi.get("SENDER_NAME") == null ? "" : (String)snDireksi.get("SENDER_NAME"));
						post_desc = (snDireksi.get("POST_DESC") == null ? "" : (String)snDireksi.get("POST_DESC"));
						post_jn_desc = (snDireksi.get("POST_JN_DESC") == null ? "" : (String)snDireksi.get("POST_JN_DESC"));
						post_desc_approve = (snDireksi.get("POST_DESC_APPROVE") == null ? "" : (String)snDireksi.get("POST_DESC_APPROVE"));
						email_cc_akseptor = (snDireksi.get("EMAIL_CC_AKSEPTOR_" + idx_aksep) == null ? "" : (String)snDireksi.get("EMAIL_CC_AKSEPTOR_" + idx_aksep));
						
						lus_akseptor = (snDireksi.get("LUS_AKSEPTOR_" + idx_aksep) == null ? "" : (String)snDireksi.get("LUS_AKSEPTOR_" + idx_aksep).toString());
						path_ttd = (snDireksi.get("PATH_TTD_" + idx_aksep) == null ? "" : (String)snDireksi.get("PATH_TTD_" + idx_aksep));
						email_akseptor = (snDireksi.get("EMAIL_AKSEPTOR_" + idx_aksep) == null ? "" : (String)snDireksi.get("EMAIL_AKSEPTOR_" + idx_aksep));
						name_akseptor = (snDireksi.get("NAME_AKSEPTOR_" + idx_aksep) == null ? "" : (String)snDireksi.get("NAME_AKSEPTOR_" + idx_aksep));			
					}
			    	
			    	String emailto = "avnel@sinarmasmsiglife.co.id;";
			    	String[] to = null;
			    	String cc = null;
			    	String []bcc = null;
			    	
			    	String subject = "";
			    	String desc_1 = "";
			    	String desc_2 = "";
			    	
					emailto = email_akseptor;
		    		cc = email_kirim_cc;
		    		desc_1 = post_desc;
		    		desc_2 = post_jn_desc;
		    		
//		    		bcc = new String[]{"avnel@sinarmasmsiglife.co.id;"};    		
		    		
			    	if(!emailto.trim().equals("")){
				   		to = (emailto + (cc == null ? "" : cc) + email_cc_akseptor).split(";");						   		
				   		List<DropDown> ListImage = new ArrayList<DropDown>();								
						String name = null;
						String act_lus_akseptor = null;
						
						//update => helpdesk []
	   		 			List<Map> map_spaj = bacDao.selectSpajBatchPembayaranByJns(ls_jenis, kode_batch);
	   		 									
						for(int j = 0; j < to.length; j++){
							act_lus_akseptor = null;
							subject = subject_kirim_info;
							String me_id = sequence.sequenceMeIdEmail();
							StringBuffer pesan2 = new StringBuffer();	
				   		    String [] sendingTo = new String[] {to[j]};	
				   		    
				   		    if (to[j].equals(email_akseptor.replaceAll(";", ""))){
				   		    	act_lus_akseptor = lus_akseptor;
				   		    	name= name_akseptor;
				   		    	subject = subject_kirim;
				   		    }

		//			    	Untuk melihat info lebih detail silahkan lihat viewer, klaim kematian / klaim kesehatan
				   			pesan2.append("<html><title>" + me_id + "</title><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black; border-collapse:collapse;}table th{border: 1px solid black; background-color: yellow;} table th{padding:10px;} td{border: 1px solid black; padding:0 5px 0 5px;} .button {background-color: #4CAF50; border: none; color: white; padding: 15px 32px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer;} .button_approve {background-color: transparent; color: #4CAF50; border: 2px solid #4CAF50; border-radius: 12px; font-size:24px;} .button_approve:hover {background-color: #4CAF50; color: white;} .button_decline {background-color: transparent; color: #f44336; border: 2px solid #f44336; border-radius: 12px; font-size:24px;} .button_decline:hover {background-color: #f44336; color: white;}</style></head>");
			   		 		pesan2.append("<body bgcolor='#ffffc9'>"+subject+".<br><br>");
			   			 
				   		 	if(map_spaj != null){
			   		 			String pattern = "###,###.##";
			   		 			DecimalFormat df = new DecimalFormat(pattern);
		   		 				
			   		 			pesan2.append("<table>" +
												"<tr>" +
													"<th>JENIS TRX</th>" +
													"<th>POLIS</th>" +
													"<th>PEMEGANG POLIS</th>" +
													"<th>REK. ATAS NAMA</th>");
								//if(ls_jenis.trim().equals("3") || ls_jenis.trim().equals("4")) //helpdesk [148610] update body email approval Finance untuk jenis 1 dan 2, query ada di SRS, poinnya jadi sama dengan jenis 3 & 4
									  pesan2.append("<th>REK. NAMA VALIDASI</th>");
									  pesan2.append("<th>NO. REK.</th>" +
													"<th>NOMINAL</th>" +
													"<th>BANK</th>" +
													"<th>NO. PRE</th> ");
								//if(ls_jenis.trim().equals("3") || ls_jenis.trim().equals("4")) //helpdesk [148610] update body email approval Finance untuk jenis 1 dan 2, query ada di SRS, poinnya jadi sama dengan jenis 3 & 4
									  pesan2.append("<th>STATUS</th>");
								  pesan2.append("</tr>");
		   		 				
			   		 			for(int ii = 0; ii < map_spaj.size(); ii++){
			   		 				Map data_spaj = (Map)map_spaj.get(ii);
			   		 				pesan2.append("<tr>");
			   		 				pesan2.append("	<td>" + (String)data_spaj.get("JENIS_TRX") + "</td>");
			   		 				pesan2.append("	<td>" + (String)data_spaj.get("POLIS") + "</td>");
			   		 				pesan2.append("	<td>" + (String)data_spaj.get("PEMEGANG_POLIS") + "</td>");
			   		 				pesan2.append("	<td>" + (String)data_spaj.get("REK_ATAS_NAMA") + "</td>");
			   		 				//if(ls_jenis.trim().equals("3") || ls_jenis.trim().equals("4")) //helpdesk [148610] update body email approval Finance untuk jenis 1 dan 2, query ada di SRS, poinnya jadi sama dengan jenis 3 & 4
				   		 				pesan2.append("	<td>" + (String)data_spaj.get("REK_NAMA_VALIDASI") + "</td>");
			   		 				pesan2.append("	<td>" + (String)data_spaj.get("NO_REK") + "</td>");
			   		 				pesan2.append("	<td align='right'>" + df.format((BigDecimal)data_spaj.get("NOMINAL")) + "</td>");
			   		 				pesan2.append("	<td>" + (String)data_spaj.get("BANK") + "</td>");
			   		 				pesan2.append("	<td>" + (String)data_spaj.get("NO_PRE") + "</td>");
			   		 				//if(ls_jenis.trim().equals("3") || ls_jenis.trim().equals("4")) //helpdesk [148610] update body email approval Finance untuk jenis 1 dan 2, query ada di SRS, poinnya jadi sama dengan jenis 3 & 4
				   		 				pesan2.append("	<td>" + (String)data_spaj.get("STATUS") + "</td>");
			   		 				pesan2.append("</tr>");
			   		 			}
			   		 			
			   		 			pesan2.append("</table><br><br>");
				   		    	
				   		    	if(is_first_approver){
				   		 			bacDao.updateMstPembayaranEmail(kode_batch, me_id);
				   		    		is_first_approver = false;
				   		    	}
		   		 			}
		   		 			
			   		 		if(!StringUtil.isEmpty(act_lus_akseptor)){
			   		 			//production
				   		 		String link1 = "http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=paymentintegrationaksep&id=" + lus_akseptor + "&batch_no=" + kode_batch + "&email=" + currentUser.getEmail() + "&lde=" + currentUser.getLde_id() + "&name=" + name + "&status=" + "1" + "&ls_jenis=" + ls_jenis + "&idx=" + idx_aksep;
			   		 			String link2 = "http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=paymentintegrationaksep&id=" + lus_akseptor + "&batch_no=" + kode_batch + "&email=" + currentUser.getEmail() + "&lde=" + currentUser.getLde_id() + "&name=" + name + "&status=" + "2" + "&ls_jenis=" + ls_jenis + "&idx=" + idx_aksep;
				   		 		//testing
//			   		 			String link1 = "http://test.sinarmasmsiglife.co.id:8800/E-LionsXC-TEST/uw/uw.htm?window=paymentintegrationaksep&id=" + lus_akseptor + "&batch_no=" + kode_batch + "&email=" + currentUser.getEmail() + "&lde=" + currentUser.getLde_id() + "&name=" + name + "&status=" + "1" + "&ls_jenis=" + ls_jenis + "&idx=" + idx_aksep;
//			   		 			String link2 = "http://test.sinarmasmsiglife.co.id:8800/E-LionsXC-TEST/uw/uw.htm?window=paymentintegrationaksep&id=" + lus_akseptor + "&batch_no=" + kode_batch + "&email=" + currentUser.getEmail() + "&lde=" + currentUser.getLde_id() + "&name=" + name + "&status=" + "2" + "&ls_jenis=" + ls_jenis + "&idx=" + idx_aksep;
				   		 		
			   		 			String tkey1 = "";
				   		 		String tkey2 = "";
				   		 		
						   		try {
						   			tkey1 = commonDao.encryptUrlKey("paymentintegration", kode_batch, 1, link1);
						   			tkey2 = commonDao.encryptUrlKey("paymentintegration", kode_batch, 1, link2);
						   		}catch (Exception e) {
									logger.error("ERROR", e);
								}
						   		
						   		link1 += "&tkey=" + tkey1;
						   		link2 += "&tkey=" + tkey2;
			   		 			
				   		 		pesan2.append("<a href='" + link1 + "' class='button button_approve'><b>Approve</b></a>\t");		
				   		 		pesan2.append("<a href='" + link2 + "' class='button button_decline'><b>Decline</b></a><br><br>");
				   		 	}
			   		 		pesan2.append("<br>Sender: " + sender_name);
			   		 		pesan2.append("<br>To: " + emailto);
//			   		 		pesan2.append("<br>Cc: " + cc);
			   		 		pesan2.append("<br></body></html>");
			   		 		
					   		EmailPool.send(me_id,"SMiLe E-Lions", 0, 0, 0, 0, 
									null, 0, 0, nowDate, null, 
									true, from, 
									sendingTo, 
									null, 
									bcc, 
									subject, 
									pesan2.toString(), 
									null,8);
						}	
			    	}
		    	}
		   		bacDao.updateMstBatchPembayaranAfterEmail(kode_batch);
		    }catch (MailException e) {
		    	status = 2;
		    	
				logger.error("ERROR :", e);
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, new Date(), null, 
						true, from, 
						new String[]{"avnel@sinarmasmsiglife.co.id", "chandra.aripin@sinarmasmsiglife.co.id"}, 
						null,
						null, 
						"ERROR @ [paymentintegration] class [BacManager]",
						pesan.toString(), 
						null, null);
			} catch (Exception e) {
		    	status = 2;
		    	
				logger.error("ERROR :", e);
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, new Date(), null, 
						true, from, 
						new String[]{"avnel@sinarmasmsiglife.co.id", "chandra.aripin@sinarmasmsiglife.co.id"}, 
						null,
						null, 
						"ERROR @ [paymentintegration] class [BacManager]",
						pesan.toString(), 
						null, null);
			}finally{
				AksesHist a = new AksesHist();
				a.setLus_id(Integer.parseInt((lus_akseptor.length() < 1 ? "1" : lus_akseptor)));
				a.setMsah_date(nowDate);
				a.setMsah_jenis(1);
				a.setMsah_spaj(kode_batch);
				a.setMsah_uri("/UW/UW.HTM?window=snowsdireksi");
				
				commonDao.insertAksesHist(a);
			}
		}
    	return status;
	}
	
	/**
	 * helpdesk [133348] email validasi transaksi Direksi/ Dept Head	
	 * @param currentUser
	 * @param kode_batch
	 * @param status
	 * @param ls_jenis
	 * @param idx_aksep
	 * @return
	 * @throws Exception
	 */
	public Integer paymentintegrationaksep(User currentUser, String kode_batch, Integer status, String ls_jenis, String idx_aksep) throws Exception{
		Date nowDate = commonDao.selectSysdate();
		StringBuffer pesan = new StringBuffer();	
	    String from="ajsjava@sinarmasmsiglife.co.id";	
    	String lus_akseptor = "";
    	int result = -1;

    	try {    	
			List<Map> list_approver = bacDao.selectMstBatchPembayaranApprover(kode_batch, idx_aksep);
			
			String approver = "5"; //query null
			if(list_approver != null){
				Map map_app = list_approver.get(0);
				approver = (String)map_app.get("APP_" + idx_aksep).toString();
			}
			
			if(approver.equalsIgnoreCase("1")){
				Integer months = nowDate.getMonth()+1;
				Integer years = nowDate.getYear()+1900;    
		    	String email_kirim_cc = "";
		    	String email_kirim_to = "";
		    	String subject_kirim = "";
		    	String subject_kirim_info = "";
		    	String email_approve_to = "";
		    	String email_approve_cc = "";
		    	String subject_approve = "";
		    	String path_ttd = "";
		    	String email_akseptor_1 = "";
		    	String email_akseptor = "";
		    	String sender_name = "";
		    	String post_desc = "";
		    	String post_jn_desc = "";
		    	String post_desc_approve = "";
		    	String name_akseptor = "";
		    	String email_cc_akseptor = "";
		    	int exist_aksep_next = 0;
		    	int idx_aksep_next = 0;
		    	
				List<Map> m = bacDao.selectMstPembayaranApproval(ls_jenis);
	    	
				if(!m.isEmpty()){
					Map snDireksi= (Map)m.get(0);
					
					email_kirim_cc = (snDireksi.get("EMAIL_KIRIM_CC") == null ? "" : (String)snDireksi.get("EMAIL_KIRIM_CC"));
					email_kirim_to = (snDireksi.get("EMAIL_KIRIM_TO") == null ? "" : (String)snDireksi.get("EMAIL_KIRIM_TO"));
					subject_kirim = (snDireksi.get("SUBJECT_KIRIM") == null ? "" : (String)snDireksi.get("SUBJECT_KIRIM"));
					subject_kirim_info = (snDireksi.get("SUBJECT_KIRIM_INFO") == null ? "" : (String)snDireksi.get("SUBJECT_KIRIM_INFO"));
					email_approve_to = (snDireksi.get("EMAIL_APPROVE_TO") == null ? "" : (String)snDireksi.get("EMAIL_APPROVE_TO"));
					email_approve_cc = (snDireksi.get("EMAIL_APPROVE_CC") == null ? "" : (String)snDireksi.get("EMAIL_APPROVE_CC"));
					subject_approve = (snDireksi.get("SUBJECT_APPROVE") == null ? "" : (String)snDireksi.get("SUBJECT_APPROVE"));
					
					sender_name = (snDireksi.get("SENDER_NAME") == null ? "" : (String)snDireksi.get("SENDER_NAME"));
					post_desc = (snDireksi.get("POST_DESC") == null ? "" : (String)snDireksi.get("POST_DESC"));
					post_jn_desc = (snDireksi.get("POST_JN_DESC") == null ? "" : (String)snDireksi.get("POST_JN_DESC"));
					post_desc_approve = (snDireksi.get("POST_DESC_APPROVE") == null ? "" : (String)snDireksi.get("POST_DESC_APPROVE"));
					email_cc_akseptor = (snDireksi.get("EMAIL_CC_AKSEPTOR_2") == null ? "" : (String)snDireksi.get("EMAIL_CC_AKSEPTOR_2"));
					email_akseptor_1 = (snDireksi.get("EMAIL_AKSEPTOR_1") == null ? "" : (String)snDireksi.get("EMAIL_AKSEPTOR_1"));
					
					lus_akseptor = (snDireksi.get("LUS_AKSEPTOR_" + idx_aksep) == null ? "" : (String)snDireksi.get("LUS_AKSEPTOR_" + idx_aksep).toString());
					path_ttd = (snDireksi.get("PATH_TTD_" + idx_aksep) == null ? "" : (String)snDireksi.get("PATH_TTD_" + idx_aksep));
					email_akseptor = (snDireksi.get("EMAIL_AKSEPTOR_" + idx_aksep) == null ? "" : (String)snDireksi.get("EMAIL_AKSEPTOR_" + idx_aksep));
					name_akseptor = (snDireksi.get("NAME_AKSEPTOR_" + idx_aksep) == null ? "" : (String)snDireksi.get("NAME_AKSEPTOR_" + idx_aksep));
				}
			
		    	String emailto = "avnel@sinarmasmsiglife.co.id;";
		    	String []cc = null;
		    	String []bcc = null;	    	
		    	String desc_1 = "";
		    	
		    	emailto = email_kirim_to; //email_approve_to;
		    	if(email_approve_cc != null) cc = new String[]{email_approve_cc};
		    	desc_1 = post_desc_approve;
		    		    	
//				bcc = new String[]{"avnel@sinarmasmsiglife.co.id;"};
		    			
				String subject = subject_approve + " BATCH NO " + kode_batch;
	    	
		    	if(!emailto.trim().equals("")){							
//					String name = null;		    		
					String keputusan = "";
				
			    	if(status == 1){//approve		    		
			    		keputusan = "diapprove";
			    		result = 1;
			    	}else if(status == 2){//decline
			    		keputusan = "didecline";
			    		result = 2;
			    	}
			    	
		    		String[] to = (emailto + email_akseptor_1 + email_cc_akseptor).split(";");
					String me_id = sequence.sequenceMeIdEmail();					
					StringBuffer pesan2 = new StringBuffer();	
	
	//			    	Untuk melihat info lebih detail silahkan lihat viewer, klaim kematian / klaim kesehatan
		   			pesan2.append("<html><title>" + me_id + "</title><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black; border-collapse:collapse;}table th{border: 1px solid black; background-color: yellow;} table th{padding:10px;} td{border: 1px solid black; padding:0 5px 0 5px;}</style></head>");
	   		 		pesan2.append("<body bgcolor='#ffffc9'>Telah <strong>" + keputusan + "</strong> oleh direksi untuk BATCH NO: " + kode_batch + " <br><br>");
	   		 		
	   		 		//update => helpdesk []
   		 			List<Map> map_spaj = bacDao.selectSpajBatchPembayaranByJns(ls_jenis, kode_batch);
		 			
	   		 		if(map_spaj != null){
			 			String pattern = "###,###.##";
			 			DecimalFormat df = new DecimalFormat(pattern);
		 				
			 			pesan2.append("<table>" +
										"<tr>" +
											"<th>JENIS TRX</th>" +
											"<th>POLIS</th>" +
											"<th>PEMEGANG POLIS</th>" +
											"<th>REK. ATAS NAMA</th>");
						//if(ls_jenis.trim().equals("3") || ls_jenis.trim().equals("4")) //helpdesk [148610] update body email approval Finance untuk jenis 1 dan 2, query ada di SRS, poinnya jadi sama dengan jenis 3 & 4
							  pesan2.append("<th>REK. NAMA VALIDASI</th>");
							  pesan2.append("<th>NO. REK.</th>" +
											"<th>NOMINAL</th>" +
											"<th>BANK</th>" +
											"<th>NO. PRE</th> ");
						//if(ls_jenis.trim().equals("3") || ls_jenis.trim().equals("4")) //helpdesk [148610] update body email approval Finance untuk jenis 1 dan 2, query ada di SRS, poinnya jadi sama dengan jenis 3 & 4
							  pesan2.append("<th>STATUS</th>");
						  pesan2.append("</tr>");
		 				
					    for(int ii = 0; ii < map_spaj.size(); ii++){
			 				Map data_spaj = (Map)map_spaj.get(ii);
			 				pesan2.append("<tr>");
	   		 				pesan2.append("	<td>" + (String)data_spaj.get("JENIS_TRX") + "</td>");
	   		 				pesan2.append("	<td>" + (String)data_spaj.get("POLIS") + "</td>");
	   		 				pesan2.append("	<td>" + (String)data_spaj.get("PEMEGANG_POLIS") + "</td>");
	   		 				pesan2.append("	<td>" + (String)data_spaj.get("REK_ATAS_NAMA") + "</td>");
	   		 				//if(ls_jenis.trim().equals("3") || ls_jenis.trim().equals("4")) //helpdesk [148610] update body email approval Finance untuk jenis 1 dan 2, query ada di SRS, poinnya jadi sama dengan jenis 3 & 4
		   		 				pesan2.append("	<td>" + (String)data_spaj.get("REK_NAMA_VALIDASI") + "</td>");
	   		 				pesan2.append("	<td>" + (String)data_spaj.get("NO_REK") + "</td>");
	   		 				pesan2.append("	<td align='right'>" + df.format((BigDecimal)data_spaj.get("NOMINAL")) + "</td>");
	   		 				pesan2.append("	<td>" + (String)data_spaj.get("BANK") + "</td>");
	   		 				pesan2.append("	<td>" + (String)data_spaj.get("NO_PRE") + "</td>");
	   		 				//if(ls_jenis.trim().equals("3") || ls_jenis.trim().equals("4")) //helpdesk [148610] update body email approval Finance untuk jenis 1 dan 2, query ada di SRS, poinnya jadi sama dengan jenis 3 & 4
		   		 				pesan2.append("	<td>" + (String)data_spaj.get("STATUS") + "</td>");
	   		 				pesan2.append("</tr>");
			 			}
			 			
			 			pesan2.append("</table><br><br>");
		 			}
		 			
		 			if(path_ttd.length() > 0){
			 			File ttd = new File(path_ttd);
			 			if(ttd.exists()){
			 				String image_string = encodeToString(path_ttd, "png");
			 				pesan2.append("<left><img src=\"data:image/png;base64," + image_string + "\"></left><br><br>");
			 			}else
			 				pesan2.append("<left>#__#</left><br><br>");
		 			}else
		 				pesan2.append("<left>#__#</left><br><br>");
	   		 		
		 			pesan2.append("<body bgcolor='#ffffc9'><left>" + name_akseptor + "</left><br><br>");
	   		 		pesan2.append("</body></html>");
	   		 		
	   		 		//ditutup karena komplen klo kebanyakan email, tinggal uncomment klo mau ada email after approve/decline
			   		/*EmailPool.send(me_id, "SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, nowDate, null, 
							true, from, 
							to, 
							cc, 
							bcc, 
							subject, 
							pesan2.toString(), 
							null,8);*/

		    		bacDao.updateMstBatchPembayaran(kode_batch, currentUser.getLus_id(), idx_aksep, status + 1, me_id);
	    		}
		    	List<Map> all_accept = bacDao.selectMstBatchPembayaran(kode_batch, 2);
		    	
		    	List<Map> IsUpdated;
		    	if(!all_accept.isEmpty()){
		    		Boolean success = false;
		    		
		    		if(ls_jenis.trim().equals("3") || ls_jenis.trim().equals("4")){ //helpdesk []
		    			try{
		    				IsUpdated = bacDao.selectMstPembayaranApiIsUpdated(kode_batch, 3, 1);
		    				
		    				if(IsUpdated.isEmpty()){
				    			bacDao.updateMstPembayaranApi(kode_batch);
				    			success = true;
		    				} else {
		    					result = 16;
		    				}
		    			} catch (Exception ex){
		    				result = 6;
		    			}
		    		} else if (ls_jenis.trim().equals("1") || ls_jenis.trim().equals("2")){
			    		Map data = all_accept.get(0);
			    		success = movefilepaymentintegration((data.get("LOCATION") == null ? "" : (String)data.get("LOCATION")));
		    		}
		    		
		    		if(!success)
		    			result = 4;
		    	}
	    		if(result == 2 && (ls_jenis.trim().equals("3") || ls_jenis.trim().equals("4"))){  //helpdesk []
	    			try {
	    				IsUpdated = bacDao.selectMstPembayaranApiIsUpdated(kode_batch, 3, 2);
	    				
	    				if(IsUpdated.isEmpty()){
	    					bacDao.updateMstPembayaranApiRejected(kode_batch);
	    				} else {
	    					result = 17;
	    				}
	    			} catch (Exception ex){
	    				result = 7;
	    			}
	    		}		    	
			} else if(approver.equalsIgnoreCase("5")){
				result = 5;
			} else
				result = 0;
	    }catch (MailException e) {
	    	logger.error("ERROR :", e);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, from, 
					new String[]{"avnel@sinarmasmsiglife.co.id", "chandra.aripin@sinarmasmsiglife.co.id"}, 
					null,
					null, 
					"ERROR @ [paymentintegrationaksep] class [BacManager]",
					pesan.toString(), 
					null, null);
	    } catch (Exception e) {
			logger.error("ERROR :", e);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, from, 
					new String[]{"avnel@sinarmasmsiglife.co.id", "chandra.aripin@sinarmasmsiglife.co.id"}, 
					null,
					null, 
					"ERROR @ [paymentintegrationaksep] class [BacManager]",
					pesan.toString(), 
					null, null);
		} finally{
			AksesHist a = new AksesHist();
			a.setLus_id(Integer.parseInt((lus_akseptor.length() < 1 ? "1" : lus_akseptor)));
			a.setMsah_date(nowDate);
			a.setMsah_jenis(1);
			a.setMsah_spaj(kode_batch);
			a.setMsah_uri("/UW/UW.HTM?window=snowsdireksiaksep");
			
			commonDao.insertAksesHist(a);
		}
	   
	    return result;	    
	}
	
	/**
	 * helpdesk [133348] email validasi transaksi Direksi/ Dept Head
	 * untuk pindahin file paymentintegration //chandra
	 * @param filename
	 */
	public Boolean movefilepaymentintegration(String filename){
		Boolean result = true;
		
		if(!filename.isEmpty()){
			String source = props.getProperty("payment.integration.path") + "Pra Payment/";
			String destination = props.getProperty("payment.integration.path") + "Payment Execution/";
			
			try{
				File source_file = new File(source + filename);
				
				if(source_file.exists()){
					source_file.renameTo(new File(destination + source_file.getName()));
				} else
					result = false;
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}else
			result = false;
		
		return result;
	}
	
	/**
	 * helpdesk [133348] email validasi transaksi Direksi/ Dept Head
	 * untuk encode image jadi string //chandra
	 * @param image
	 * @param type
	 * @return
	 */
	private String encodeToString(String filepath, String filetype) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
//        	BufferedImage file = ImageIO.read(new File(Resources.getResourceAsFile(props.getProperty("images.ttd.direksi").trim()).getAbsolutePath()));
        	BufferedImage fileimage = ImageIO.read(new File(filepath));
            ImageIO.write(fileimage, filetype, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

	public List<PowersaveCair> selectRolloverPeriodeLibur(String reg_spaj){	
		return bacDao.selectRolloverPowersavePeriodeLibur(reg_spaj);
	}	


	public String select_agen_reff_bii(String lrb_id){
		return this.bacDao.select_agen_reff_bii(lrb_id);
	}

	
	public int update_no_va(String no_va,String no_temp){
		return this.bacDao.update_no_va(no_va, no_temp);
	}
	
	
	public String select_bsim_no_va_syariah(){
		return this.bacDao.select_bsim_no_va_syariah();
	}
	
	public Map select_det_bsim_syariah(String reg_spaj){
		return this.bacDao.select_det_bsim_syariah(reg_spaj);
	}
	public Map select_det_prod(String lsbs_id, int lsdbs_number){
		return this.bacDao.select_det_prod(lsbs_id, lsdbs_number);
	}
	public Map select_det_agen_by_mclid(String mclid){
		return this.bacDao.select_det_agen_by_mclid(mclid);
	}

	public Map select_det_reff_by_lrb_id(String reff_id){
		return this.bacDao.select_det_reff_by_lrb_id(reff_id);
	}
	public Map select_by_lbn(String lbn_id){
		return this.bacDao.select_by_lbn(lbn_id);
	}
	
	public void updatePas(Pas pas){
		 this.bacDao.updatePas(pas);
	}
	
	
public Pas updatePaTempNew(Pas pas, User user){
		
		pas.setStatus(0);
		try{
		
			
			pas.setLspd_id(1);
			pas.setLssp_id(10);
			pas.setDist("05");
			pas.setMsp_kode_sts_sms("00");
			updatePas(pas);
		}catch(Exception e){
			
			logger.info(e);
			pas.setStatus(1);
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return pas;
	}

public List selectReportPep(String dariTanggal,String sampaiTanggal){
	return uwDao.selectReportPep(dariTanggal,sampaiTanggal);
} 

	//helpdesk [137730] tambahin pilihan campaign
	public List selectMstCampaignProduct(String kode_bisnis, String lsdbs_number) {
		return bacDao.selectMstCampaignProduct(kode_bisnis, lsdbs_number);
	}
	
	//covid patar timotius
		public List selectFaceToFaceCategory() {
			return bacDao.selectFaceToFaceCategory();
		}
	
	//helpdesk [143180] autosales tambah campaign_id
	public List selectMstCampaignProductAutoSales(String campaign_id, String kode_bisnis, String lsdbs_number) {
		return bacDao.selectMstCampaignProductAutoSales(campaign_id, kode_bisnis, lsdbs_number);
	}
	
	public List selectOfacList(String nopol, String spaj, String noRow, String noPage, String sertifikat, String possible, String matched) {
		return bacDao.selectOfacList(nopol, spaj, noRow, noPage, sertifikat, possible, matched);
	}

	public List<MstOfacScreeningResult> selectMstoFacresultScreening(String spaj,String type,String name) {
		return bacDao.selectMstoFacresultScreening(spaj,type,name);
	}
	public Integer selectPositionPolicy(String reg_spaj){
		return uwDao.selectPositionPolicy(reg_spaj);
	}
	
	public Integer selectSertifikatYN(String reg_spaj){
		return uwDao.selectSertifikatYN(reg_spaj);
	}
	
	public List<OfacSertifikat> selectSertifikat(String reg_spaj){
		return uwDao.selectSertifikat(reg_spaj);
	}
	
	public List<MstTransUlink> selectCheckMstTransUlink(String reg_spaj){
		return uwDao.selectCheckMstTransUlink(reg_spaj);
	}
	
	public Integer selectPositionInsured(String reg_spaj){
		return uwDao.selectPositionInsured(reg_spaj);
	}
	//Meeting Customer Declaration 27 Maret 2020 IGA
	public void schedulerPendingPrintSIAP2U(){
		String msh_name = "SCHEDULER PENDING PRINT SIAP2U";
		if(schedulerDao.selectAlreadyExistScheduler(msh_name)<=0){
			schedulerDao.schedulerPendingPrintSIAP2U(msh_name);
		}
	}
	
	
	public List selectrdsscreening(String reg_spaj) {
		return bacDao.selectrdsscreening(reg_spaj);
	}
	//IGA UPDATE RDS SCREENING
	public List selecthistoryrds(String reg_spaj) {
		return bacDao.selecthistoryrds(reg_spaj);
	}
	public void updateMstDatePrintToNull(String reg_spaj){
		bacDao.updateMstDatePrintToNull(reg_spaj);
	}
	
	public void updateTanggalKonfirmasiToNull(String reg_spaj,String tgl_konfirmasi, String tgl_generate){
		bacDao.updateTanggalKonfirmasiToNull(reg_spaj,tgl_konfirmasi, tgl_generate);
	}
	
	public void updateFlagKonfirmasi(String reg_spaj, String tgl_generate,Integer flag_konfirmasi,Integer flag_konfirmasi_target){
		bacDao.updateFlagKonfirmasi(reg_spaj, tgl_generate,flag_konfirmasi,flag_konfirmasi_target);
	}
	
	public void deletePositionSpajGenerate(String reg_spaj){
		bacDao.deletePositionSpajGenerate(reg_spaj);
	}
	public Pemegang selectHolder(String spaj){
		return uwDao.selectHolder(spaj);
	}
	
	public Pemegang selectHolderSertifikat(String spaj){
		return uwDao.selectHolderSertifikat(spaj);
	}
	
	public Tertanggung selectTertanggungDetail(String spaj){
		return uwDao.selectTertanggungDetail(spaj);
	}
	
	public Tertanggung selectTertanggungDetailSertifikat(String spaj){
		return uwDao.selectTertanggungDetailSertifikat(spaj);
	}
	
	public Comment selectComment(String spaj){
		return uwDao.selectComment(spaj);
	}
	
	public void insertComment(Integer add_num, Integer mofs_id, String spaj, String type, String comment, String nama) {
		uwDao.insertComment(add_num,  mofs_id, spaj, type, comment, nama);
	}
	
	public void ApproveMstPolicy(String reg_spaj, String lus_id){	
		uwDao.ApproveMstPolicy(reg_spaj, lus_id);
	}
	
	public void ApproveRejectSertifikat(String reg_spaj, Integer mofs_status, String mofs_status_message, String lus_id){	
		uwDao.ApproveRejectSertifikat(reg_spaj, mofs_status, mofs_status_message, lus_id);
	}
	
	public void ApproveRejectProcessed(String reg_spaj, String lus_id, Integer lspd_id){	
		uwDao.ApproveRejectProcessed(reg_spaj, lus_id, lspd_id);
	}
	
	public void updateMstTransUlink(String reg_spaj, Integer mu_ke, Integer mtu_ke, String lus_id){	
		uwDao.updateMstTransUlink(reg_spaj, mu_ke, mtu_ke, lus_id);
	}
	
	public void updateMsUlink(String reg_spaj, String lus_id){	
		uwDao.updateMsUlink(reg_spaj, lus_id);
	}
	
	public void RejectMstPolicy(String reg_spaj, String lus_id){	
		uwDao.RejectMstPolicy(reg_spaj, lus_id);
	}
	
	public void ApproveMstInsured(String reg_spaj, String lus_id){	
		uwDao.ApproveMstInsured(reg_spaj, lus_id);
	}
	
	public void RejectMstInsured(String reg_spaj, String lus_id){	
		uwDao.RejectMstInsured(reg_spaj, lus_id);
	}
	
	public void insertMstPositionSpaj(String lus_id, String msps_desc, String reg_spaj, int addSecond) {
		uwDao.insertMstPositionSpaj(lus_id, msps_desc, reg_spaj, addSecond);
	}
	
	public List<Benefeciary> selectBenefeciary(String spaj){
		return uwDao.selectBenefeciary(spaj);
	}
	
	public List<Benefeciary> selectBenefeciarySertifikat(String spaj){
		return uwDao.selectBenefeciarySertifikat(spaj);
	}
	
	public List<PesertaPlus> selectPeserta(String spaj){
		return uwDao.selectPeserta(spaj);
	}
	
	public List<PesertaPlus> selectPesertaSertifikat(String spaj){
		return uwDao.selectPesertaSertifikat(spaj);
	}
	
	public String prosesofacscreening(String s_spaj,int type, int lus_id) {				
		return uwDao.prosesofacscreening(s_spaj, type, lus_id);		
	}
	
	public String selectTotalListSPAJ(String nopol, String spaj, String noRow, String sertifikat, String possible, String matched){
		return uwDao.selectTotalListSPAJ(nopol, spaj, noRow, sertifikat, possible, matched);
	}
	
	// NANA DESC MEDIS
	public List selectParamMedisDesc() {
		return bacDao.selectParamMedisDesc();
	}
	
	// Simpol 
	public double selectSumPremiPokokAndBerkalaTemp(String no_temp) {
		return (Double) bacDao.selectSumPremiPokokAndBerkalaTemp(no_temp);
	}	
	
	// Simpol 
	public double selectPremiTunggalTemp(String no_temp) {
		return (Double) bacDao.selectPremiTunggalTemp(no_temp);
	}
	
	// Flag Full Autosales Nana
	public List selectParamFullAutoSalesConfig(){
		return bacDao.selectParamFullAutoSalesConfig();
	}
	//IGA UPDATE RDS SCREENING
	public void updateKetGenerate(String reg_spaj, String tgl_generate, String keterangan){
		bacDao.updateKetGenerate(reg_spaj, tgl_generate, keterangan);
	}
		
	//Ticket [SD-572] tambah validasi agent expired pada saat submit spaj
	public List selectDaftarAgentBlacklist(){
		return bacDao.selectDaftarAgentBlacklist();
	}
	//NCR/2020/10/037
	public Map selectExtraMortalitaNew(String reg_spaj) {
		return uwDao.selectExtraMortalitaNew(reg_spaj);
	}
	//NCR/2020/10/037
	public Date selectSpajDate(String reg_spaj) {
		return uwDao.selectSpajDate(reg_spaj);
	}
}