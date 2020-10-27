package com.ekalife.elions.model;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class Command implements Serializable {

	private static long serialVersionUID = 1L;
	
	private String reg_spaj;
	
	private List<Position> daftarPosisi;
	private Position position;
	
	private String pesan;
	
	private List<Pbp> daftarPbp;
	
	private Powersave powersave;
	
	private String otorotor;
	private String password;
	private String warning;
	private Integer posisi;
	
	private List lsRider;
	private List lsBisnisIdnName;
	private List lsBisnisIdnNameRider;
	private List lsBisnisIdnNameRider2;
	private List lsMedical;
	private List<MedQuest> lsMedQuests;
	private List lsBlackList;
	private List lsSimultanPp;
	private List lsSimultanTt;
	private List lsUlink;
	private List lsDetUlink;
	private List lsBiayaUlink;
	private List lsTransUlink;
	private List lsTransUlinkNew;
	private List lsNab;
	private List lsStatusAksep;
	private List lsProduct;
	private List lsPosition;
	private List lsUlangan;
	private List daftarKurs;
	private List lsNasabah;
	private List lsBfa;
	private List<Icd> lsIcd;
	private List<HslReas> lsHslReas;
	private Nasabah nasabah;
	private Integer error;
	private Integer row;
	private Integer i;
	private int flagAdd;
	private String flagId;
	private String spaj;
	private Integer insuredNo;
	private BigDecimal bisnisIdUtama;
	private Integer count;
	private String tglAwal;
	private String tglAkhir;
	private Edit edit;
	private String reins;
	private String to;
	private Integer lsreIdPp;
	private String lcaIdPp;
	private User currentUser;
	private Integer lsbsId;
	private Integer lsdbs_number;
	private String namaPemegang;
	private String namaTertanggung;
	private Integer liAksep;
	private Integer tudoub;
	private Integer subLiAksep;
	private boolean rekNas;
	private Personal personal;
	private List reimender;
	private Integer flag_ut;
	private String emailAdmin;
	private String emailAgen;
	private String periode;
	private Integer guthrie;
	private Double msprTsi;
	private Double msprPremium;
	private String lscbPayMode;
	private Map mapPemegang;
	private Map mapTertanggung;
	private Map mapBridge;
	private Integer rowPp;
	private Integer rowTt;
	private String alamatFile;
	private File attachment;
	private int lspd_id;
	private DBank dbank;
	private DBank dbankDefault;
	private List<DBank> daftardbank;
	private List<DBank> daftardbankDefault;
	private TBank tbank;
	private Integer showTab;
	private Jiffy jiffy;
	private RelasiNasabah relasiNasabah;
	private Children children;
	private Matrix matrix;
	private Aspirasi aspirasi;
	private Rekomendasi rekomendasi;
	private Kebutuhan kebutuhan;
	private ProdBank prodBank;
	private Pendapatan pendapatan;
	private Aktivitas aktivitas;
	private SurplusCalc surplusCalc;
	private ProtectCalc protectCalc;
	private IncomeCalc incomeCalc;
	private String nomor;
	private Payment payment;
	private String konfirmasi;
	private String konfirmHealthProd;
	private AddReffBii addReffBii;
	private String nama_produk;
	private Filing filing;
	private String mode;
	private Integer version; // 0 - old, 1 - new 
	private Mia mia;
	
	private MultipartFile unduh; 
	
	private List daftar;
	private String kolom;
	private Date sysdate;
	private Date bdate;
	private Double msl_premi;
	private InputTopup trans;
	private List<InputTopup> daftarTopup;
	private String valid1;
	private String valid2;
	
	private List<Questionnaire> daftarQuestionnaire;
	
	private String submitMode;
	private TravelInsurance peserta;
	private List<TravelInsurance> daftarTravelIns;
	private Integer msti_id;
	private Integer msti_jenis;
	private Integer msid_no;
	private Boolean premitu;
	private String confNm;
	private String mclId;
	private String msag_id;
	private Product product;
	private String bridge;
	
	
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	public String getMclId() {
		return mclId;
	}
	public void setMclId(String mclId) {
		this.mclId = mclId;
	}
	public String getConfNm() {
		return confNm;
	}
	public void setConfNm(String confNm) {
		this.confNm = confNm;
	}
	public Boolean getPremitu() {
		return premitu;
	}
	public void setPremitu(Boolean premitu) {
		this.premitu = premitu;
	}
	public TravelInsurance getPeserta() {
		return peserta;
	}
	public void setPeserta(TravelInsurance peserta) {
		this.peserta = peserta;
	}
	public Integer getMsid_no() {
		return msid_no;
	}
	public void setMsid_no(Integer msid_no) {
		this.msid_no = msid_no;
	}
	public Integer getMsti_id() {
		return msti_id;
	}
	public void setMsti_id(Integer msti_id) {
		this.msti_id = msti_id;
	}
	public Integer getMsti_jenis() {
		return msti_jenis;
	}
	public void setMsti_jenis(Integer msti_jenis) {
		this.msti_jenis = msti_jenis;
	}
	public String getSubmitMode() {
		return submitMode;
	}
	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}
	public List<TravelInsurance> getDaftarTravelIns() {
		return daftarTravelIns;
	}
	public void setDaftarTravelIns(List<TravelInsurance> daftarTravelIns) {
		this.daftarTravelIns = daftarTravelIns;
	}
	public List<Questionnaire> getDaftarQuestionnaire() {
		return daftarQuestionnaire;
	}
	public void setDaftarQuestionnaire(List<Questionnaire> daftarQuestionnaire) {
		this.daftarQuestionnaire = daftarQuestionnaire;
	}
	/**
	 * @return the valid1
	 */
	public String getValid1() {
		return valid1;
	}
	/**
	 * @param valid1 the valid1 to set
	 */
	public void setValid1(String valid1) {
		this.valid1 = valid1;
	}
	/**
	 * @return the valid2
	 */
	public String getValid2() {
		return valid2;
	}
	/**
	 * @param valid2 the valid2 to set
	 */
	public void setValid2(String valid2) {
		this.valid2 = valid2;
	}
	
	
	public InputTopup getTrans() {
		return trans;
	}
	public void setTrans(InputTopup trans) {
		this.trans = trans;
	}
	public List<InputTopup> getDaftarTopup() {
		return daftarTopup;
	}
	public void setDaftarTopup(List<InputTopup> daftarTopup) {
		this.daftarTopup = daftarTopup;
	}
	public List<Pbp> getDaftarPbp() {
		return daftarPbp;
	}
	public void setDaftarPbp(List<Pbp> daftarPbp) {
		this.daftarPbp = daftarPbp;
	}
	public List getDaftar() {
		return daftar;
	}
	public void setDaftar(List daftar) {
		this.daftar = daftar;
	}
	public String getKolom() {
		return kolom;
	}
	public void setKolom(String kolom) {
		this.kolom = kolom;
	}
	public Date getSysdate() {
		return sysdate;
	}
	public void setSysdate(Date sysdate) {
		this.sysdate = sysdate;
	}
	public int getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(int lspd_id) {
		this.lspd_id = lspd_id;
	}
	public File getAttachment() {
		return attachment;
	}
	public void setAttachment(File attachment) {
		this.attachment = attachment;
	}
	public String getLscbPayMode() {
		return lscbPayMode;
	}
	public void setLscbPayMode(String lscbPayMode) {
		this.lscbPayMode = lscbPayMode;
	}
	public Double getMsprPremium() {
		return msprPremium;
	}
	public void setMsprPremium(Double msprPremium) {
		this.msprPremium = msprPremium;
	}
	public Double getMsprTsi() {
		return msprTsi;
	}
	public void setMsprTsi(Double msprTsi) {
		this.msprTsi = msprTsi;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public List<Position> getDaftarPosisi() {
		return daftarPosisi;
	}
	public void setDaftarPosisi(List<Position> daftarPosisi) {
		this.daftarPosisi = daftarPosisi;
	}
	public Integer getGuthrie() {
		return guthrie;
	}
	public void setGuthrie(Integer guthrie) {
		this.guthrie = guthrie;
	}
	public String getEmailAdmin() {
		return emailAdmin;
	}
	public void setEmailAdmin(String emailAdmin) {
		this.emailAdmin = emailAdmin;
	}
	public String getEmailAgen() {
		return emailAgen;
	}
	public void setEmailAgen(String emailAgen) {
		this.emailAgen = emailAgen;
	}
	public Integer getFlag_ut() {
		return flag_ut;
	}
	public void setFlag_ut(Integer flag_ut) {
		this.flag_ut = flag_ut;
	}
	public Personal getPersonal() {
		return personal;
	}
	public void setPersonal(Personal personal) {
		this.personal = personal;
	}
	public boolean isRekNas() {
		return rekNas;
	}
	public void setRekNas(boolean rekNas) {
		this.rekNas = rekNas;
	}
	public String getNamaPemegang() {
		return namaPemegang;
	}
	public void setNamaPemegang(String namaPemegang) {
		this.namaPemegang = namaPemegang;
	}
	
	public Integer getLsbsId() {
		return lsbsId;
	}
	public void setLsbsId(Integer lsbsId) {
		this.lsbsId = lsbsId;
	}
	public User getCurrentUser() {
		return currentUser;
	}
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}
	public Edit getEdit() {
		return edit;
	}
	public void setEdit(Edit edit) {
		this.edit = edit;
	}
	public BigDecimal getBisnisIdUtama() {
		return bisnisIdUtama;
	}
	public void setBisnisIdUtama(BigDecimal bisnisIdUtama) {
		this.bisnisIdUtama = bisnisIdUtama;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getError() {
		return error;
	}
	public void setError(Integer error) {
		this.error = error;
	}
	public int getFlagAdd() {
		return flagAdd;
	}
	public void setFlagAdd(int flagAdd) {
		this.flagAdd = flagAdd;
	}
	public String getFlagId() {
		return flagId;
	}
	public void setFlagId(String flagId) {
		this.flagId = flagId;
	}
	public List getLsBiayaUlink() {
		return lsBiayaUlink;
	}
	public void setLsBiayaUlink(List lsBiayaUlink) {
		this.lsBiayaUlink = lsBiayaUlink;
	}
	public List getLsBisnisIdnName() {
		return lsBisnisIdnName;
	}
	public void setLsBisnisIdnName(List lsBisnisIdnName) {
		this.lsBisnisIdnName = lsBisnisIdnName;
	}
	public List getLsBisnisIdnNameRider() {
		return lsBisnisIdnNameRider;
	}
	public void setLsBisnisIdnNameRider(List lsBisnisIdnNameRider) {
		this.lsBisnisIdnNameRider = lsBisnisIdnNameRider;
	}
	public List getLsBisnisIdnNameRider2() {
		return lsBisnisIdnNameRider2;
	}
	public void setLsBisnisIdnNameRider2(List lsBisnisIdnNameRider2) {
		this.lsBisnisIdnNameRider2 = lsBisnisIdnNameRider2;
	}
	public List getLsBlackList() {
		return lsBlackList;
	}
	public void setLsBlackList(List lsBlackList) {
		this.lsBlackList = lsBlackList;
	}
	public List getLsDetUlink() {
		return lsDetUlink;
	}
	public void setLsDetUlink(List lsDetUlink) {
		this.lsDetUlink = lsDetUlink;
	}
	public List getLsMedical() {
		return lsMedical;
	}
	public void setLsMedical(List lsMedical) {
		this.lsMedical = lsMedical;
	}
	public List getLsRider() {
		return lsRider;
	}
	public void setLsRider(List lsRider) {
		this.lsRider = lsRider;
	}
	public List getLsSimultanPp() {
		return lsSimultanPp;
	}
	public void setLsSimultanPp(List lsSimultanPp) {
		this.lsSimultanPp = lsSimultanPp;
	}
	public List getLsSimultanTt() {
		return lsSimultanTt;
	}
	public void setLsSimultanTt(List lsSimultanTt) {
		this.lsSimultanTt = lsSimultanTt;
	}
	public List getLsUlink() {
		return lsUlink;
	}
	public void setLsUlink(List lsUlink) {
		this.lsUlink = lsUlink;
	}
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	public String getSpaj() {
		return spaj;
	}
	public void setSpaj(String spaj) {
		this.spaj = spaj;
	}
	public Integer getI() {
		return i;
	}
	public void setI(Integer i) {
		this.i = i;
	}
	public String getTglAkhir() {
		return tglAkhir;
	}
	public void setTglAkhir(String tglAkhir) {
		this.tglAkhir = tglAkhir;
	}
	public String getTglAwal() {
		return tglAwal;
	}
	public void setTglAwal(String tglAwal) {
		this.tglAwal = tglAwal;
	}
	public List getLsStatusAksep() {
		return lsStatusAksep;
	}
	public void setLsStatusAksep(List lsStatusAksep) {
		this.lsStatusAksep = lsStatusAksep;
	}
	public List getLsProduct() {
		return lsProduct;
	}
	public void setLsProduct(List lsProduct) {
		this.lsProduct = lsProduct;
	}
	public String getReins() {
		return reins;
	}
	public void setReins(String reins) {
		this.reins = reins;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public List getLsPosition() {
		return lsPosition;
	}
	public void setLsPosition(List lsPosition) {
		this.lsPosition = lsPosition;
	}
	public String getLcaIdPp() {
		return lcaIdPp;
	}
	public void setLcaIdPp(String lcaIdPp) {
		this.lcaIdPp = lcaIdPp;
	}
	public Integer getLsreIdPp() {
		return lsreIdPp;
	}
	public void setLsreIdPp(Integer lsreIdPp) {
		this.lsreIdPp = lsreIdPp;
	}
	public Integer getInsuredNo() {
		return insuredNo;
	}
	public void setInsuredNo(Integer insuredNo) {
		this.insuredNo = insuredNo;
	}
	public Integer getLiAksep() {
		return liAksep;
	}
	public void setLiAksep(Integer liAksep) {
		this.liAksep = liAksep;
	}
	public String getNamaTertanggung() {
		return namaTertanggung;
	}
	public void setNamaTertanggung(String namaTertanggung) {
		this.namaTertanggung = namaTertanggung;
	}
	public List getDaftarKurs() {
		return daftarKurs;
	}
	public void setDaftarKurs(List daftarKurs) {
		this.daftarKurs = daftarKurs;
	}
	public List getLsUlangan() {
		return lsUlangan;
	}
	public void setLsUlangan(List lsUlangan) {
		this.lsUlangan = lsUlangan;
	}
	public List getLsTransUlink() {
		return lsTransUlink;
	}
	public void setLsTransUlink(List lsTransUlink) {
		this.lsTransUlink = lsTransUlink;
	}
	public List getLsTransUlinkNew() {
		return lsTransUlinkNew;
	}
	public void setLsTransUlinkNew(List lsTransUlinkNew) {
		this.lsTransUlinkNew = lsTransUlinkNew;
	}
	public List getLsNab() {
		return lsNab;
	}
	public void setLsNab(List lsNab) {
		this.lsNab = lsNab;
	}
	public List getLsNasabah() {
		return lsNasabah;
	}
	public void setLsNasabah(List lsNasabah) {
		this.lsNasabah = lsNasabah;
	}
	public Nasabah getNasabah() {
		return nasabah;
	}
	public void setNasabah(Nasabah nasabah) {
		this.nasabah = nasabah;
	}
	public List getLsBfa() {
		return lsBfa;
	}
	public void setLsBfa(List lsBfa) {
		this.lsBfa = lsBfa;
	}
	public String getPeriode() {
		return periode;
	}
	public void setPeriode(String periode) {
		this.periode = periode;
	}
	public Map getMapPemegang() {
		return mapPemegang;
	}
	public void setMapPemegang(Map mapPemegang) {
		this.mapPemegang = mapPemegang;
	}
	public Map getMapTertanggung() {
		return mapTertanggung;
	}
	public void setMapTertanggung(Map mapTertanggung) {
		this.mapTertanggung = mapTertanggung;
	}
	public Integer getRowPp() {
		return rowPp;
	}
	public void setRowPp(Integer rowPp) {
		this.rowPp = rowPp;
	}
	public Integer getRowTt() {
		return rowTt;
	}
	public void setRowTt(Integer rowTt) {
		this.rowTt = rowTt;
	}
	public String getAlamatFile() {
		return alamatFile;
	}
	public void setAlamatFile(String alamatFile) {
		this.alamatFile = alamatFile;
	}
	public DBank getDbank() {
		return dbank;
	}
	public void setDbank(DBank dbank) {
		this.dbank = dbank;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public static void setSerialVersionUID(long serialVersionUID) {
		Command.serialVersionUID = serialVersionUID;
	}
	public List<DBank> getDaftardbank() {
		return daftardbank;
	}
	public void setDaftardbank(List<DBank> daftardbank) {
		this.daftardbank = daftardbank;
	}
	public List<DBank> getDaftardbankDefault() {
		return daftardbankDefault;
	}
	public void setDaftardbankDefault(List<DBank> daftardbankDefault) {
		this.daftardbankDefault = daftardbankDefault;
	}
	public DBank getDbankDefault() {
		return dbankDefault;
	}
	public void setDbankDefault(DBank dbankDefault) {
		this.dbankDefault = dbankDefault;
	}
	public TBank getTbank() {
		return tbank;
	}
	public void setTbank(TBank tbank) {
		this.tbank = tbank;
	}
	public Integer getShowTab() {
		return showTab;
	}
	public void setShowTab(Integer showTab) {
		this.showTab = showTab;
	}
	public Jiffy getJiffy() {
		return jiffy;
	}
	public void setJiffy(Jiffy jiffy) {
		this.jiffy = jiffy;
	}
	public RelasiNasabah getRelasiNasabah() {
		return relasiNasabah;
	}
	public void setRelasiNasabah(RelasiNasabah relasiNasabah) {
		this.relasiNasabah = relasiNasabah;
	}
	public Children getChildren() {
		return children;
	}
	public void setChildren(Children children) {
		this.children = children;
	}
	public Matrix getMatrix() {
		return matrix;
	}
	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
	public Aspirasi getAspirasi() {
		return aspirasi;
	}
	public void setAspirasi(Aspirasi aspirasi) {
		this.aspirasi = aspirasi;
	}
	public Rekomendasi getRekomendasi() {
		return rekomendasi;
	}
	public void setRekomendasi(Rekomendasi rekomendasi) {
		this.rekomendasi = rekomendasi;
	}
	public Kebutuhan getKebutuhan() {
		return kebutuhan;
	}
	public void setKebutuhan(Kebutuhan kebutuhan) {
		this.kebutuhan = kebutuhan;
	}
	public ProdBank getProdBank() {
		return prodBank;
	}
	public void setProdBank(ProdBank prodBank) {
		this.prodBank = prodBank;
	}
	public Pendapatan getPendapatan() {
		return pendapatan;
	}
	public void setPendapatan(Pendapatan pendapatan) {
		this.pendapatan = pendapatan;
	}
	public Aktivitas getAktivitas() {
		return aktivitas;
	}
	public void setAktivitas(Aktivitas aktivitas) {
		this.aktivitas = aktivitas;
	}
	public SurplusCalc getSurplusCalc() {
		return surplusCalc;
	}
	public void setSurplusCalc(SurplusCalc surplusCalc) {
		this.surplusCalc = surplusCalc;
	}
	public ProtectCalc getProtectCalc() {
		return protectCalc;
	}
	public void setProtectCalc(ProtectCalc protectCalc) {
		this.protectCalc = protectCalc;
	}
	public IncomeCalc getIncomeCalc() {
		return incomeCalc;
	}
	public void setIncomeCalc(IncomeCalc incomeCalc) {
		this.incomeCalc = incomeCalc;
	}
	public Powersave getPowersave() {
		return powersave;
	}
	public void setPowersave(Powersave powersave) {
		this.powersave = powersave;
	}
	public String getNomor() {
		return nomor;
	}
	public void setNomor(String nomor) {
		this.nomor = nomor;
	}
	public MultipartFile getUnduh() {
		return unduh;
	}
	public void setUnduh(MultipartFile unduh) {
		this.unduh = unduh;
	}

	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	public String getKonfirmasi() {
		return konfirmasi;
	}
	public void setKonfirmasi(String konfirmasi) {
		this.konfirmasi = konfirmasi;
	}
	public String getOtorotor() {
		return otorotor;
	}
	public void setOtorotor(String otorotor) {
		this.otorotor = otorotor;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPesan() {
		return pesan;
	}
	public void setPesan(String pesan) {
		this.pesan = pesan;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getSubLiAksep() {
		return subLiAksep;
	}
	public void setSubLiAksep(Integer subLiAksep) {
		this.subLiAksep = subLiAksep;
	}
	public AddReffBii getAddReffBii() {
		return addReffBii;
	}
	public void setAddReffBii(AddReffBii addReffBii) {
		this.addReffBii = addReffBii;
	}	
	public List<Icd> getLsIcd() {
		return lsIcd;
	}
	public void setLsIcd(List<Icd> lsIcd) {
		this.lsIcd = lsIcd;
	}
	public List<HslReas> getLsHslReas() {
		return lsHslReas;
	}
	public void setLsHslReas(List<HslReas> lsHslReas) {
		this.lsHslReas = lsHslReas;
	}
	public String getNama_produk() {
		return nama_produk;
	}
	public void setNama_produk(String nama_produk) {
		this.nama_produk = nama_produk;
	}
	public Filing getFiling() {
		return filing;
	}
	public void setFiling(Filing filing) {
		this.filing = filing;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getKonfirmHealthProd() {
		return konfirmHealthProd;
	}
	public void setKonfirmHealthProd(String konfirmHealthProd) {
		this.konfirmHealthProd = konfirmHealthProd;
	}
	public List getReimender() {
		return reimender;
	}
	public void setReimender(List reimender) {
		this.reimender = reimender;
	}
	public String getWarning() {
		return warning;
	}
	public void setWarning(String warning) {
		this.warning = warning;
	}
	public Date getBdate() {
		return bdate;
	}
	public void setBdate(Date bdate) {
		this.bdate = bdate;
	}
	public Double getMsl_premi() {
		return msl_premi;
	}
	public void setMsl_premi(Double msl_premi) {
		this.msl_premi = msl_premi;
	}
	public Integer getTudoub() {
		return tudoub;
	}
	public void setTudoub(Integer tudoub) {
		this.tudoub = tudoub;
	}
	
	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}
	public List<MedQuest> getLsMedQuests() {
		return lsMedQuests;
	}
	public void setLsMedQuests(List<MedQuest> lsMedQuests) {
		this.lsMedQuests = lsMedQuests;
	}
	public Mia getMia() {
		return mia;
	}
	public void setMia(Mia mia) {
		this.mia = mia;
	}
	
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msagId) {
		msag_id = msagId;
	}
	
	public Map getMapBridge() {
		return mapBridge;
	}
	public void setMapBridge(Map mapBridge) {
		this.mapBridge = mapBridge;
	}
	
	public String getBridge() {
		return bridge;
	}
	public void setBridge(String bridge) {
		this.bridge = bridge;
	}
	
}
