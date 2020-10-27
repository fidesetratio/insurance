package com.ekalife.elions.model;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * Command object untuk modul2 yang ada di sistem kontrol SPAJ
 * (package com.ekalife.elions.web.bas)
 * 
 * @author Yusuf Sutarko
 * @since Feb 23, 2007 (9:21:24 AM)
 */
public class CommandControlSpaj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5908931012793693347L;
	//Global
	private String submitMode;
	private boolean editable;
	private Map daftarWarna;
	private Map daftarWarnaAgen;
	private List<Map> daftarJenisSpaj;
	private String msf_id;
	private String msf_id_search;
	private String msf_id_bef;
	private FormHist formHistUser;
	private FormHist formHist;
	private Integer suc;
	private String nama_cabang;
	private User CurrentUser;
	
	private List<FormSpaj> daftarNomorFormSpaj;
	private List<FormSpaj> daftarFormSpaj;
	private List<FormHist> daftarFormHistory;
	private List<Spaj> daftarStokSpaj;
	private List daftarStokSpajAgen;
	private List<Agen> daftarAgen;
	private List<Map> daftarStatus;
	private List<Surat_history> daftarSuratHist;
	private List<Surat_history> daftarSuratHist2;
	
	private List<FormSpaj> daftarFormOverdue;
	private List daftarBlanko;
	//ApprovalSpajFormController
	private String lca_id;
	private List<Map> daftarCabang;
	
	//PermintaanAgenFormController
	private Agen agen;
	private String lca_lwk_lsrg;
	private List<Region> daftarRegion;
	private String jnsSpaj;
	private Integer size;
	private Integer ke;
	private String tanggal;
	
	//multi controller
	private List<FormSpaj> daftarForm;
	private List<DropDown> daftarCabBmi;
	private String bmi_id;
	private List<DropDown> typeTravelIns;
	private Integer admTravIns;
	
	//PertanggungjawabanFormController
	private List<SpajDet> daftarPertanggungjawaban;
	private List<DropDown> daftarJenisPertanggungjawaban;
	private Integer posisi;
	private Integer proses;
	
	//permintaan spaj baru
	private List<DropDown> LokasiCabang;
	private String keyCabang;
	private String lwk_id;
	private String lsrg_id;
	
	//untuk hidden or visible button fitrah
	private Integer button;
	
	//untuk menentukan index (dipakai di KontrolAgenFormController)
	private String index;
	
	//Untuk menentukan jenis Follow Up Surat History
	private Integer jenis;
	
	//Untuk brosur
	private List<Map> daftarJenisBrosur;
	private List<FormSpaj> daftarNomorFormBrosur;
	private List<FormSpaj> daftarFormBrosur;
	private List<Spaj> daftarStokBrosur;
	private Integer jn_brosur;
	private String busdev;
	private MultipartFile file1;
	private List<Map> ls_attachment;
	
	private String pesan;
	//Getter-setters down below
	
	public String getBusdev() {
		return busdev;
	}

	public void setBusdev(String busdev) {
		this.busdev = busdev;
	}

	public Integer getJn_brosur() {
		return jn_brosur;
	}

	public void setJn_brosur(Integer jn_brosur) {
		this.jn_brosur = jn_brosur;
	}

	public List<Map> getDaftarJenisBrosur() {
		return daftarJenisBrosur;
	}

	public void setDaftarJenisBrosur(List<Map> daftarJenisBrosur) {
		this.daftarJenisBrosur = daftarJenisBrosur;
	}

	public List<FormSpaj> getDaftarNomorFormBrosur() {
		return daftarNomorFormBrosur;
	}

	public void setDaftarNomorFormBrosur(List<FormSpaj> daftarNomorFormBrosur) {
		this.daftarNomorFormBrosur = daftarNomorFormBrosur;
	}

	public List<FormSpaj> getDaftarFormBrosur() {
		return daftarFormBrosur;
	}

	public void setDaftarFormBrosur(List<FormSpaj> daftarFormBrosur) {
		this.daftarFormBrosur = daftarFormBrosur;
	}

	public List<Spaj> getDaftarStokBrosur() {
		return daftarStokBrosur;
	}

	public void setDaftarStokBrosur(List<Spaj> daftarStokBrosur) {
		this.daftarStokBrosur = daftarStokBrosur;
	}
	
	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getLsrg_id() {
		return lsrg_id;
	}

	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}

	public String getLwk_id() {
		return lwk_id;
	}

	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}

	public String getKeyCabang() {
		return keyCabang;
	}

	public void setKeyCabang(String keyCabang) {
		this.keyCabang = keyCabang;
	}

	public Integer getProses() {
		return proses;
	}

	public void setProses(Integer proses) {
		this.proses = proses;
	}

	public List<Map> getDaftarCabang() {
		return daftarCabang;
	}

	public List getDaftarStokSpajAgen() {
		return daftarStokSpajAgen;
	}

	public List<Map> getDaftarJenisSpaj() {
		return daftarJenisSpaj;
	}

	public void setDaftarJenisSpaj(List<Map> daftarJenisSpaj) {
		this.daftarJenisSpaj = daftarJenisSpaj;
	}

	public void setDaftarStokSpajAgen(List daftarStokSpajAgen) {
		this.daftarStokSpajAgen = daftarStokSpajAgen;
	}

	public void setDaftarCabang(List<Map> daftarCabang) {
		this.daftarCabang = daftarCabang;
	}

	public List<Spaj> getDaftarStokSpaj() {
		return daftarStokSpaj;
	}

	public void setDaftarStokSpaj(List<Spaj> daftarStokSpaj) {
		this.daftarStokSpaj = daftarStokSpaj;
	}

	public List<FormSpaj> getDaftarNomorFormSpaj() {
		return daftarNomorFormSpaj;
	}

	public void setDaftarNomorFormSpaj(List<FormSpaj> daftarNomorFormSpaj) {
		this.daftarNomorFormSpaj = daftarNomorFormSpaj;
	}

	public List<FormSpaj> getDaftarFormSpaj() {
		return daftarFormSpaj;
	}

	public void setDaftarFormSpaj(List<FormSpaj> daftarFormSpaj) {
		this.daftarFormSpaj = daftarFormSpaj;
	}

	public String getSubmitMode() {
		return submitMode;
	}

	public void setSubmitMode(String submitMode) {
		this.submitMode = submitMode;
	}

	public String getMsf_id() {
		return msf_id;
	}

	public void setMsf_id(String msf_id) {
		this.msf_id = msf_id;
	}

	public String getLca_id() {
		return lca_id;
	}

	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public Map getDaftarWarna() {
		return daftarWarna;
	}

	public void setLegend(Map daftarWarna, Map daftarWarnaAgen, List<Map> daftarJenisSpaj) {
		this.daftarWarna = daftarWarna;
		this.daftarWarnaAgen = daftarWarnaAgen;
		this.daftarJenisSpaj = daftarJenisSpaj;
	}
	
	public void setLegendBrosur(Map daftarWarna, Map daftarWarnaAgen, List<Map> daftarJenisBrosur) {
		this.daftarWarna = daftarWarna;
		this.daftarWarnaAgen = daftarWarnaAgen;
		this.daftarJenisBrosur = daftarJenisBrosur;
	}

	public FormHist getFormHist() {
		return formHist;
	}

	public void setFormHist(FormHist formHist) {
		this.formHist = formHist;
	}

	public List<FormHist> getDaftarFormHistory() {
		return daftarFormHistory;
	}

	public void setDaftarFormHistory(List<FormHist> daftarFormHistory) {
		this.daftarFormHistory = daftarFormHistory;
	}

	public List<Region> getDaftarRegion() {
		return daftarRegion;
	}

	public void setDaftarRegion(List<Region> daftarRegion) {
		this.daftarRegion = daftarRegion;
	}

	public String getLca_lwk_lsrg() {
		return lca_lwk_lsrg;
	}

	public void setLca_lwk_lsrg(String lca_lwk_lsrg) {
		this.lca_lwk_lsrg = lca_lwk_lsrg;
	}

	public Agen getAgen() {
		return agen;
	}

	public void setAgen(Agen agen) {
		this.agen = agen;
	}

	public FormHist getFormHistUser() {
		return formHistUser;
	}

	public void setFormHistUser(FormHist formHistUser) {
		this.formHistUser = formHistUser;
	}

	public Map getDaftarWarnaAgen() {
		return daftarWarnaAgen;
	}

	public List<FormSpaj> getDaftarFormOverdue() {
		return daftarFormOverdue;
	}

	public void setDaftarFormOverdue(List<FormSpaj> daftarFormOverdue) {
		this.daftarFormOverdue = daftarFormOverdue;
	}

	public void setDaftarWarna(Map daftarWarna) {
		this.daftarWarna = daftarWarna;
	}

	public void setDaftarWarnaAgen(Map daftarWarnaAgen) {
		this.daftarWarnaAgen = daftarWarnaAgen;
	}

	public User getCurrentUser() {
		return CurrentUser;
	}

	public void setCurrentUser(User currentUser) {
		CurrentUser = currentUser;
	}

	public List<FormSpaj> getDaftarForm() {
		return daftarForm;
	}

	public void setDaftarForm(List<FormSpaj> daftarForm) {
		this.daftarForm = daftarForm;
	}

	public List<SpajDet> getDaftarPertanggungjawaban() {
		return daftarPertanggungjawaban;
	}

	public void setDaftarPertanggungjawaban(List<SpajDet> daftarPertanggungjawaban) {
		this.daftarPertanggungjawaban = daftarPertanggungjawaban;
	}

	public List<DropDown> getDaftarJenisPertanggungjawaban() {
		return daftarJenisPertanggungjawaban;
	}

	public void setDaftarJenisPertanggungjawaban(
			List<DropDown> daftarJenisPertanggungjawaban) {
		this.daftarJenisPertanggungjawaban = daftarJenisPertanggungjawaban;
	}

	public Integer getPosisi() {
		return posisi;
	}

	public void setPosisi(Integer posisi) {
		this.posisi = posisi;
	}

	public List getDaftarBlanko() {
		return daftarBlanko;
	}

	public void setDaftarBlanko(List daftarBlanko) {
		this.daftarBlanko = daftarBlanko;
	}

	public String getJnsSpaj() {
		return jnsSpaj;
	}

	public void setJnsSpaj(String jnsSpaj) {
		this.jnsSpaj = jnsSpaj;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getKe() {
		return ke;
	}

	public void setKe(Integer ke) {
		this.ke = ke;
	}

	public String getMsf_id_bef() {
		return msf_id_bef;
	}

	public void setMsf_id_bef(String msf_id_bef) {
		this.msf_id_bef = msf_id_bef;
	}

	public List<Agen> getDaftarAgen() {
		return daftarAgen;
	}

	public void setDaftarAgen(List<Agen> daftarAgen) {
		this.daftarAgen = daftarAgen;
	}

	public List<Map> getDaftarStatus() {
		return daftarStatus;
	}

	public void setDaftarStatus(List<Map> daftarStatus) {
		this.daftarStatus = daftarStatus;
	}

	public Integer getSuc() {
		return suc;
	}

	public void setSuc(Integer suc) {
		this.suc = suc;
	}

	public String getNama_cabang() {
		return nama_cabang;
	}

	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}

	public List<DropDown> getLokasiCabang() {
		return LokasiCabang;
	}

	public void setLokasiCabang(List<DropDown> lokasiCabang) {
		LokasiCabang = lokasiCabang;
	}

	public Integer getButton() {
		return button;
	}

	public void setButton(Integer button) {
		this.button = button;
	}
	public String getTanggal() {
		return tanggal;
	}
	public void setTanggal(String tanggal) {
		this.tanggal = tanggal;
	}

	public List<DropDown> getDaftarCabBmi() {
		return daftarCabBmi;
	}
	public void setDaftarCabBmi(List<DropDown> daftarCabBmi) {
		this.daftarCabBmi = daftarCabBmi;
	}

	public String getBmi_id() {
		return bmi_id;
	}

	public void setBmi_id(String bmi_id) {
		this.bmi_id = bmi_id;
	}

	public String getMsf_id_search() {
		return msf_id_search;
	}

	public void setMsf_id_search(String msf_id_search) {
		this.msf_id_search = msf_id_search;
	}

	public Integer getAdmTravIns() {
		return admTravIns;
	}

	public void setAdmTravIns(Integer admTravIns) {
		this.admTravIns = admTravIns;
	}

	public List<DropDown> getTypeTravelIns() {
		return typeTravelIns;
	}

	public void setTypeTravelIns(List<DropDown> typeTravelIns) {
		this.typeTravelIns = typeTravelIns;
	}

	public List<Surat_history> getDaftarSuratHist() {
		return daftarSuratHist;
	}

	public void setDaftarSuratHist(List<Surat_history> daftarSuratHist) {
		this.daftarSuratHist = daftarSuratHist;
	}

	public List<Surat_history> getDaftarSuratHist2() {
		return daftarSuratHist2;
	}

	public void setDaftarSuratHist2(List<Surat_history> daftarSuratHist2) {
		this.daftarSuratHist2 = daftarSuratHist2;
	}
	
	public MultipartFile getFile1() {
		return file1;
	}

	public void setFile1(MultipartFile file1) {
		this.file1 = file1;
	}
	
	public List<Map> getLs_attachment() {
		return ls_attachment;
	}

	public void setLs_attachment(List<Map> ls_attachment) {
		this.ls_attachment = ls_attachment;
	}
	
	public String getPesan() {
		return pesan;
	}

	public void setPesan(String pesan) {
		this.pesan = pesan;
	}
}