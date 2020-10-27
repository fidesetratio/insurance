package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * eka.mst_powersave_cair
 * 
 * @author Yusuf
 * @since Dec 22, 2008 (3:28:47 PM)
 */
public class PowersaveCair implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Double mpc_nab;
	public Integer jml_hari;			   
	
	//data generic
	public String mpc_reg;
	public Integer lssp_id;                       
	public Integer lssa_id;                       
	public Integer lspd_id;                       
	public String reg_spaj;                       
	public String mspo_policy_no_format;          
	public Integer lsbs_id;                       
	public Integer lsdbs_number;                  
	public String lsbs_name;                      
	public String lsdbs_name;                     
	public Date mste_beg_date;                    
	public Date mste_end_date;                    
	public String lku_symbol;                     
	public Double mspr_premium;                   
	public Double mspr_tsi;                       
	public String pemegang;                       
	public String tertanggung;                    
	public String lssp_status;
	
	//data tabel mst_powersave_cair
	public Integer mpc_urut;                      
	public Date mpc_bdate;                        
	public Date mpc_edate;              
	public Date mpc_cair;
	public Double mpc_premi;                      
	public Double mpc_bunga;                      
	public Integer mpc_hari;                      
	public Double mpc_tambah;                     
	public Double mpc_kurang;                     
	public String mpc_note;                       
	public Integer flag_posisi;                   
	public Integer flag_bulanan;                   
	public Integer flag_ps;                       
	public Integer flag_proses;                   
	public Integer lus_id_input;                  
	public Integer lus_id_proses;                 
	public Date tgl_input;                        
	public Date tgl_proses;
	public Integer lus_id_approve;
	public Date tgl_approve;
	public Integer mpc_ro;
	public Double mpc_rate;
	
	public Integer mpc_ktp;
	public Integer mpc_spt;
	public Integer mpc_sph;
	public Integer mpc_polis;
	public Integer mpc_formrek;
	public String mpc_desc;
	public Integer mpc_noncash;
	public Double mpc_rider_total;

	public Integer mpc_tu_ke;
	public Date mpc_tgl_nab_bp;
	public Double mpc_nab_bp;
	public Double mpc_nilai_polis;
	public Double mpc_bp;
	public Double mpc_bp_pt;

	public Integer mpc_bayar; // : flag sudah bayar / blom : ini belum ditampilkan di bagian manapun dari elions
	public Integer mpc_jenis_noncash; //0 = lainnya, 1 = peralihan psave ke slink
	
	//tambahan
	public boolean centang;
	public Integer mgi;                           
	public Double total_bunga;        
	public String lsbp_nama;
	public String mrc_cabang;
	public String mrc_kota;
	public String mrc_nama;
	public String mrc_no_ac;
	public String lku_id;
	public Double mpr_bayar_prm;
	public Integer selisih_hari;
	public String warna;
	public String nama_cabang;
	public String lus_full_name;
	public String lus_login_name;
	public String mspo_nasabah_dcif;              
	public String cabang_induk;                   
	public String wilayah;                        
	public String ro;                             
	public String posisi;                 
	public Integer hari_kerja; 
	public Date mpc_cair_baru;
	public Integer mps_kode;
	public Integer mpr_rider_cb;
	
	//Tambahan untuk Stabil Link
	public Integer msl_no;
	public String lji_id;
	public Integer msl_posisi;
	public Integer msl_new;
	
	public Integer msbi_tahun_ke;
	public Integer msbi_premi_ke;
	
	public String spaj_pinj;
	public String spaj_sur;
	public Integer flag_ps_pinj;
	public Integer flag_ps_sur;
	
	
	public String getSpaj_pinj() {
		return spaj_pinj;
	}
	public void setSpaj_pinj(String spaj_pinj) {
		this.spaj_pinj = spaj_pinj;
	}
	public String getSpaj_sur() {
		return spaj_sur;
	}
	public void setSpaj_sur(String spaj_sur) {
		this.spaj_sur = spaj_sur;
	}
	public Integer getFlag_ps_pinj() {
		return flag_ps_pinj;
	}
	public void setFlag_ps_pinj(Integer flag_ps_pinj) {
		this.flag_ps_pinj = flag_ps_pinj;
	}
	public Integer getFlag_ps_sur() {
		return flag_ps_sur;
	}
	public void setFlag_ps_sur(Integer flag_ps_sur) {
		this.flag_ps_sur = flag_ps_sur;
	}
	public Integer getMsbi_premi_ke() {
		return msbi_premi_ke;
	}
	public void setMsbi_premi_ke(Integer msbi_premi_ke) {
		this.msbi_premi_ke = msbi_premi_ke;
	}
	public Integer getMsbi_tahun_ke() {
		return msbi_tahun_ke;
	}
	public String getMpc_reg() {
		return mpc_reg;
	}
	public void setMpc_reg(String mpcReg) {
		mpc_reg = mpcReg;
	}
	public void setMsbi_tahun_ke(Integer msbi_tahun_ke) {
		this.msbi_tahun_ke = msbi_tahun_ke;
	}
	public Integer getMsl_new() {
		return msl_new;
	}
	public void setMsl_new(Integer msl_new) {
		this.msl_new = msl_new;
	}
	public Integer getMsl_posisi() {
		return msl_posisi;
	}
	public void setMsl_posisi(Integer msl_posisi) {
		this.msl_posisi = msl_posisi;
	}
	public Integer getMpc_bayar() {
		return mpc_bayar;
	}
	public void setMpc_bayar(Integer mpc_bayar) {
		this.mpc_bayar = mpc_bayar;
	}
	public String getLji_id() {
		return lji_id;
	}
	public Integer getMpc_jenis_noncash() {
		return mpc_jenis_noncash;
	}
	public void setMpc_jenis_noncash(Integer mpc_jenis_noncash) {
		this.mpc_jenis_noncash = mpc_jenis_noncash;
	}
	public void setLji_id(String lji_id) {
		this.lji_id = lji_id;
	}
	public Double getMpc_bp() {
		return mpc_bp;
	}
	public void setMpc_bp(Double mpc_bp) {
		this.mpc_bp = mpc_bp;
	}
	public Double getMpc_bp_pt() {
		return mpc_bp_pt;
	}
	public void setMpc_bp_pt(Double mpc_bp_pt) {
		this.mpc_bp_pt = mpc_bp_pt;
	}
	public Integer getFlag_bulanan() {
		return flag_bulanan;
	}
	public void setFlag_bulanan(Integer flag_bulanan) {
		this.flag_bulanan = flag_bulanan;
	}
	public Double getMpc_nab_bp() {
		return mpc_nab_bp;
	}
	public void setMpc_nab_bp(Double mpc_nab_bp) {
		this.mpc_nab_bp = mpc_nab_bp;
	}
	public Double getMpc_nilai_polis() {
		return mpc_nilai_polis;
	}
	public void setMpc_nilai_polis(Double mpc_nilai_polis) {
		this.mpc_nilai_polis = mpc_nilai_polis;
	}
	public Date getMpc_tgl_nab_bp() {
		return mpc_tgl_nab_bp;
	}
	public void setMpc_tgl_nab_bp(Date mpc_tgl_nab_bp) {
		this.mpc_tgl_nab_bp = mpc_tgl_nab_bp;
	}
	public Integer getMpc_tu_ke() {
		return mpc_tu_ke;
	}
	public void setMpc_tu_ke(Integer mpc_tu_ke) {
		this.mpc_tu_ke = mpc_tu_ke;
	}
	public Integer getMsl_no() {
		return msl_no;
	}
	public void setMsl_no(Integer msl_no) {
		this.msl_no = msl_no;
	}
	public Integer getMpr_rider_cb() {
		return mpr_rider_cb;
	}
	public void setMpr_rider_cb(Integer mpr_rider_cb) {
		this.mpr_rider_cb = mpr_rider_cb;
	}
	public Integer getMps_kode() {
		return mps_kode;
	}
	public void setMps_kode(Integer mps_kode) {
		this.mps_kode = mps_kode;
	}
	public Date getMpc_cair_baru() {
		return mpc_cair_baru;
	}
	public void setMpc_cair_baru(Date mpc_cair_baru) {
		this.mpc_cair_baru = mpc_cair_baru;
	}
	public Integer getHari_kerja() {
		return hari_kerja;
	}
	public void setHari_kerja(Integer hari_kerja) {
		this.hari_kerja = hari_kerja;
	}
	public String getMspo_nasabah_dcif() {
		return mspo_nasabah_dcif;
	}
	public void setMspo_nasabah_dcif(String mspo_nasabah_dcif) {
		this.mspo_nasabah_dcif = mspo_nasabah_dcif;
	}
	public String getCabang_induk() {
		return cabang_induk;
	}
	public void setCabang_induk(String cabang_induk) {
		this.cabang_induk = cabang_induk;
	}
	public String getWilayah() {
		return wilayah;
	}
	public void setWilayah(String wilayah) {
		this.wilayah = wilayah;
	}
	public String getRo() {
		return ro;
	}
	public void setRo(String ro) {
		this.ro = ro;
	}
	public String getPosisi() {
		return posisi;
	}
	public void setPosisi(String posisi) {
		this.posisi = posisi;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}
	public Integer getMpc_ktp() {
		return mpc_ktp;
	}
	public void setMpc_ktp(Integer mpc_ktp) {
		this.mpc_ktp = mpc_ktp;
	}
	public Integer getMpc_spt() {
		return mpc_spt;
	}
	public void setMpc_spt(Integer mpc_spt) {
		this.mpc_spt = mpc_spt;
	}
	public Integer getMpc_sph() {
		return mpc_sph;
	}
	public void setMpc_sph(Integer mpc_sph) {
		this.mpc_sph = mpc_sph;
	}
	public Integer getMpc_polis() {
		return mpc_polis;
	}
	public void setMpc_polis(Integer mpc_polis) {
		this.mpc_polis = mpc_polis;
	}
	public Integer getMpc_formrek() {
		return mpc_formrek;
	}
	public void setMpc_formrek(Integer mpc_formrek) {
		this.mpc_formrek = mpc_formrek;
	}
	public String getLus_full_name() {
		return lus_full_name;
	}
	public void setLus_full_name(String lus_full_name) {
		this.lus_full_name = lus_full_name;
	}
	public String getWarna() {
		return warna;
	}
	public void setWarna(String warna) {
		this.warna = warna;
	}
	public Integer getSelisih_hari() {
		return selisih_hari;
	}
	public void setSelisih_hari(Integer selisih_hari) {
		this.selisih_hari = selisih_hari;
	}
	public Double getMpr_bayar_prm() {
		return mpr_bayar_prm;
	}
	public void setMpr_bayar_prm(Double mpr_bayar_prm) {
		this.mpr_bayar_prm = mpr_bayar_prm;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public String getLssp_status() {
		return lssp_status;
	}
	public void setLssp_status(String lssp_status) {
		this.lssp_status = lssp_status;
	}
	public Integer getLssp_id() {
		return lssp_id;
	}
	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}
	public String getMspo_policy_no_format() {
		return mspo_policy_no_format;
	}
	public void setMspo_policy_no_format(String mspo_policy_no_format) {
		this.mspo_policy_no_format = mspo_policy_no_format;
	}
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public String getLsbs_name() {
		return lsbs_name;
	}
	public void setLsbs_name(String lsbs_name) {
		this.lsbs_name = lsbs_name;
	}
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public Date getMste_beg_date() {
		return mste_beg_date;
	}
	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}
	public Date getMste_end_date() {
		return mste_end_date;
	}
	public void setMste_end_date(Date mste_end_date) {
		this.mste_end_date = mste_end_date;
	}
	public Integer getMgi() {
		return mgi;
	}
	public void setMgi(Integer mgi) {
		this.mgi = mgi;
	}
	public String getLku_symbol() {
		return lku_symbol;
	}
	public void setLku_symbol(String lku_symbol) {
		this.lku_symbol = lku_symbol;
	}
	public Double getMspr_premium() {
		return mspr_premium;
	}
	public void setMspr_premium(Double mspr_premium) {
		this.mspr_premium = mspr_premium;
	}
	public Double getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(Double mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
	public Date getMpc_cair() {
		return mpc_cair;
	}
	public void setMpc_cair(Date mpc_cair) {
		this.mpc_cair = mpc_cair;
	}
	public String getPemegang() {
		return pemegang;
	}
	public void setPemegang(String pemegang) {
		this.pemegang = pemegang;
	}
	public String getTertanggung() {
		return tertanggung;
	}
	public void setTertanggung(String tertanggung) {
		this.tertanggung = tertanggung;
	}
	public Double getTotal_bunga() {
		return total_bunga;
	}
	public void setTotal_bunga(Double total_bunga) {
		this.total_bunga = total_bunga;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Integer getMpc_urut() {
		return mpc_urut;
	}
	public void setMpc_urut(Integer mpc_urut) {
		this.mpc_urut = mpc_urut;
	}
	public Date getMpc_bdate() {
		return mpc_bdate;
	}
	public void setMpc_bdate(Date mpc_bdate) {
		this.mpc_bdate = mpc_bdate;
	}
	public Date getMpc_edate() {
		return mpc_edate;
	}
	public void setMpc_edate(Date mpc_edate) {
		this.mpc_edate = mpc_edate;
	}
	public Double getMpc_premi() {
		return mpc_premi;
	}
	public void setMpc_premi(Double mpc_premi) {
		this.mpc_premi = mpc_premi;
	}
	public Double getMpc_bunga() {
		return mpc_bunga;
	}
	public void setMpc_bunga(Double mpc_bunga) {
		this.mpc_bunga = mpc_bunga;
	}
	public Integer getMpc_hari() {
		return mpc_hari;
	}
	public void setMpc_hari(Integer mpc_hari) {
		this.mpc_hari = mpc_hari;
	}
	public Double getMpc_tambah() {
		return mpc_tambah;
	}
	public void setMpc_tambah(Double mpc_tambah) {
		this.mpc_tambah = mpc_tambah;
	}
	public String getMpc_note() {
		return mpc_note;
	}
	public void setMpc_note(String mpc_note) {
		this.mpc_note = mpc_note;
	}
	public Integer getFlag_posisi() {
		return flag_posisi;
	}
	public void setFlag_posisi(Integer flag_posisi) {
		this.flag_posisi = flag_posisi;
	}
	public Integer getFlag_ps() {
		return flag_ps;
	}
	public void setFlag_ps(Integer flag_ps) {
		this.flag_ps = flag_ps;
	}
	public Integer getFlag_proses() {
		return flag_proses;
	}
	public void setFlag_proses(Integer flag_proses) {
		this.flag_proses = flag_proses;
	}
	public Integer getLus_id_input() {
		return lus_id_input;
	}
	public void setLus_id_input(Integer lus_id_input) {
		this.lus_id_input = lus_id_input;
	}
	public Integer getLus_id_proses() {
		return lus_id_proses;
	}
	public void setLus_id_proses(Integer lus_id_proses) {
		this.lus_id_proses = lus_id_proses;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}
	public Date getTgl_proses() {
		return tgl_proses;
	}
	public void setTgl_proses(Date tgl_proses) {
		this.tgl_proses = tgl_proses;
	}
	public Integer getLssa_id() {
		return lssa_id;
	}
	public void setLssa_id(Integer lssa_id) {
		this.lssa_id = lssa_id;
	}
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public boolean isCentang() {
		return centang;
	}
	public void setCentang(boolean centang) {
		this.centang = centang;
	}
	public String getLsbp_nama() {
		return lsbp_nama;
	}
	public void setLsbp_nama(String lsbp_nama) {
		this.lsbp_nama = lsbp_nama;
	}
	public String getMrc_cabang() {
		return mrc_cabang;
	}
	public void setMrc_cabang(String mrc_cabang) {
		this.mrc_cabang = mrc_cabang;
	}
	public String getMrc_kota() {
		return mrc_kota;
	}
	public void setMrc_kota(String mrc_kota) {
		this.mrc_kota = mrc_kota;
	}
	public String getMrc_nama() {
		return mrc_nama;
	}
	public void setMrc_nama(String mrc_nama) {
		this.mrc_nama = mrc_nama;
	}
	public String getMrc_no_ac() {
		return mrc_no_ac;
	}
	public void setMrc_no_ac(String mrc_no_ac) {
		this.mrc_no_ac = mrc_no_ac;
	}
	public Integer getMpc_ro() {
		return mpc_ro;
	}
	public void setMpc_ro(Integer mpc_ro) {
		this.mpc_ro = mpc_ro;
	}
	public Double getMpc_rate() {
		return mpc_rate;
	}
	public void setMpc_rate(Double mpc_rate) {
		this.mpc_rate = mpc_rate;
	}
	public Double getMpc_kurang() {
		return mpc_kurang;
	}
	public void setMpc_kurang(Double mpc_kurang) {
		this.mpc_kurang = mpc_kurang;
	}
	public String getLus_login_name() {
		return lus_login_name;
	}
	public void setLus_login_name(String lus_login_name) {
		this.lus_login_name = lus_login_name;
	}
	public Integer getLus_id_approve() {
		return lus_id_approve;
	}
	public void setLus_id_approve(Integer lus_id_approve) {
		this.lus_id_approve = lus_id_approve;
	}
	public Date getTgl_approve() {
		return tgl_approve;
	}
	public void setTgl_approve(Date tgl_approve) {
		this.tgl_approve = tgl_approve;
	}
	public String getMpc_desc() {
		return mpc_desc;
	}
	public void setMpc_desc(String mpc_desc) {
		this.mpc_desc = mpc_desc;
	}
	public Integer getMpc_noncash() {
		return mpc_noncash;
	}
	public void setMpc_noncash(Integer mpc_noncash) {
		this.mpc_noncash = mpc_noncash;
	}
	public Double getMpc_rider_total() {
		return mpc_rider_total;
	}
	public void setMpc_rider_total(Double mpc_rider_total) {
		this.mpc_rider_total = mpc_rider_total;
	}
	public Integer getJml_hari() {
		return jml_hari;
	}
	public void setJml_hari(Integer jml_hari) {
		this.jml_hari = jml_hari;
	}
	public Double getMpc_nab() {
		return mpc_nab;
	}
	public void setMpc_nab(Double mpc_nab) {
		this.mpc_nab = mpc_nab;
	}
}