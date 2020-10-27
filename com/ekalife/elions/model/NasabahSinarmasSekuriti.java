/**
 * @author  : Ferry Harlim
 * @created : Dec 22, 2006 
 */
package com.ekalife.elions.model;

import java.io.Serializable;

public class NasabahSinarmasSekuriti implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String TGL_PRODUKSI;
	private String PREMI_PRODUKSI;
	
	private String NO_SERI;
	private String NO_CIF;
	private String NO_POLIS;
	private String NO_SPAJ;
	private String KODE_PRODUK;
	private String NAMA_PRODUK;
	private String PP_NAMA;
	private String PP_GELAR;
	private String PP_MOTHER;
	private String PP_JENIS_ID;
	private String PP_ID;
	private String PP_BIRTHPLACE;
	private String PP_BIRTHDATE;
	private String PP_WN;
	private String PP_SEX;
	private String PP_AGAMA;
	private String PP_PENDIDIKAN;

	private String PP_JOB_DESC;
	private String PP_TUJUAN;
	private String PP_PENGHASILAN;
	private String PP_PENDANAAN;
	private String PP_KERJA;
	private String PP_SMBR_PENGHASILAN;
	private String PP_INDUSTRI;

	private String PP_ALAMAT_RUMAH;
	private String PP_KD_POS_RUMAH;
	private String PP_KOTA_RUMAH;
	private String PP_AREA_CODE_RUMAH;
	private String PP_TELPON_RUMAH;
	private String PP_AREA_CODE_RUMAH2;
	private String PP_TELPON_RUMAH2;
	private String PP_ALAMAT_KANTOR;
	private String PP_KD_POS_RUMAH2;
	private String PP_KOTA_KANTOR;
	private String PP_AREA_CODE_KANTOR;
	private String PP_TELPON_KANTOR;
	private String PP_AREA_CODE_KANTOR2;
	private String PP_TELPON_KANTOR2;
	private String PP_EMAIL;
	private String PP_HP;
	private String PP_AREA_CODE_FAX;
	private String PP_NO_FAX;

	private String TT_NAMA;
	private String TT_GELAR;
	private String TT_MOTHER;
	private String PP_JENIS_ID2;
	private String TT_ID;
	private String TT_BIRTHPLACE;
	private String TT_BIRTHDATE;
	private String TT_WN;
	private String TT_SEX;
	private String TT_AGAMA;
	private String TT_PENDIDIKAN;

	private String PP_JOB_DESC2;
	private String PP_TUJUAN2;
	private String PP_PENGHASILAN2;
	private String PP_PENDANAAN2;
	private String PP_KERJA2;
	private String PP_SMBR_PENGHASILAN2;
	private String PP_INDUSTRI2;

	private String TT_ALAMAT_RUMAH;
	private String TT_KD_POS_RUMAH;
	private String TT_KOTA_RUMAH;
	private String TT_AREA_CODE_RUMAH;
	private String TT_TELPON_RUMAH;
	private String TT_AREA_CODE_RUMAH2;
	private String TT_TELPON_RUMAH2;
	private String TT_ALAMAT_KANTOR;
	private String TT_KD_POS_RUMAH2;
	private String TT_KOTA_KANTOR;
	private String TT_AREA_CODE_KANTOR;
	private String TT_TELPON_KANTOR;
	private String TT_AREA_CODE_KANTOR2;
	private String TT_TELPON_KANTOR2;
	private String TT_EMAIL;
	private String TT_HP;
	private String TT_AREA_CODE_FAX;
	private String TT_NO_FAX;

	private String INV_EFFECTIVEDATE;
	private String INV_EXPIRYDATE;
	private String INV_RETURN;
	private String INV_PERIOD;
	private String INV_TOT_NOMINAL;
	private String INV_TOT_BUNGA;
	private String INV_JENIS_RO;

	private String INV_BANK;
	private String INV_BANK_BRANCH;
	private String INV_BANK_CITY;
	private String INV_BANK_ONBEHALF;
	private String INV_BANK_NOREK;

	private String POSISI_DOKUMEN;
	private String STATUS_POLIS;
	private String STATUS_AKSEP;

	private String TAGIHAN_ALAMAT;
	private String TAGIHAN_KD_POS;
	private String TAGIHAN_TELP;
	private String TAGIHAN_AREA_CODE;
	private String TAGIHAN_TELP2;
	private String TAGIHAN_AREA_CODE2;
	private String TAGIHAN_EMAIL;
	private String TAGIHAN_HP;
	private String TAGIHAN_KOTA;
	
	private String LKU_ID;
	private String JENIS_TRANS;

	public String getTGL_PRODUKSI() {
		return TGL_PRODUKSI;
	}

	public void setTGL_PRODUKSI(String tGLPRODUKSI) {
		TGL_PRODUKSI = tGLPRODUKSI;
	}

	public String getPREMI_PRODUKSI() {
		return PREMI_PRODUKSI;
	}

	public void setPREMI_PRODUKSI(String pREMIPRODUKSI) {
		PREMI_PRODUKSI = pREMIPRODUKSI;
	}

	public String getLKU_ID() {
		return LKU_ID;
	}

	public void setLKU_ID(String lKUID) {
		LKU_ID = lKUID;
	}

	public String getJENIS_TRANS() {
		return JENIS_TRANS;
	}

	public void setJENIS_TRANS(String jENISTRANS) {
		JENIS_TRANS = jENISTRANS;
	}

	public String getTAGIHAN_ALAMAT() {
		return TAGIHAN_ALAMAT;
	}

	public void setTAGIHAN_ALAMAT(String tagihan_alamat) {
		TAGIHAN_ALAMAT = tagihan_alamat;
	}

	public String getTAGIHAN_AREA_CODE() {
		return TAGIHAN_AREA_CODE;
	}

	public void setTAGIHAN_AREA_CODE(String tagihan_area_code) {
		TAGIHAN_AREA_CODE = tagihan_area_code;
	}

	public String getTAGIHAN_AREA_CODE2() {
		return TAGIHAN_AREA_CODE2;
	}

	public void setTAGIHAN_AREA_CODE2(String tagihan_area_code2) {
		TAGIHAN_AREA_CODE2 = tagihan_area_code2;
	}

	public String getTAGIHAN_EMAIL() {
		return TAGIHAN_EMAIL;
	}

	public void setTAGIHAN_EMAIL(String tagihan_email) {
		TAGIHAN_EMAIL = tagihan_email;
	}

	public String getTAGIHAN_HP() {
		return TAGIHAN_HP;
	}

	public void setTAGIHAN_HP(String tagihan_hp) {
		TAGIHAN_HP = tagihan_hp;
	}

	public String getTAGIHAN_KD_POS() {
		return TAGIHAN_KD_POS;
	}

	public void setTAGIHAN_KD_POS(String tagihan_kd_pos) {
		TAGIHAN_KD_POS = tagihan_kd_pos;
	}

	public String getTAGIHAN_KOTA() {
		return TAGIHAN_KOTA;
	}

	public void setTAGIHAN_KOTA(String tagihan_kota) {
		TAGIHAN_KOTA = tagihan_kota;
	}

	public String getTAGIHAN_TELP() {
		return TAGIHAN_TELP;
	}

	public void setTAGIHAN_TELP(String tagihan_telp) {
		TAGIHAN_TELP = tagihan_telp;
	}

	public String getTAGIHAN_TELP2() {
		return TAGIHAN_TELP2;
	}

	public void setTAGIHAN_TELP2(String tagihan_telp2) {
		TAGIHAN_TELP2 = tagihan_telp2;
	}

	public String getINV_BANK() {
		return INV_BANK;
	}

	public void setINV_BANK(String inv_bank) {
		INV_BANK = inv_bank;
	}

	public String getINV_BANK_BRANCH() {
		return INV_BANK_BRANCH;
	}

	public void setINV_BANK_BRANCH(String inv_bank_branch) {
		INV_BANK_BRANCH = inv_bank_branch;
	}

	public String getINV_BANK_CITY() {
		return INV_BANK_CITY;
	}

	public void setINV_BANK_CITY(String inv_bank_city) {
		INV_BANK_CITY = inv_bank_city;
	}

	public String getINV_BANK_NOREK() {
		return INV_BANK_NOREK;
	}

	public void setINV_BANK_NOREK(String inv_bank_norek) {
		INV_BANK_NOREK = inv_bank_norek;
	}

	public String getINV_BANK_ONBEHALF() {
		return INV_BANK_ONBEHALF;
	}

	public void setINV_BANK_ONBEHALF(String inv_bank_onbehalf) {
		INV_BANK_ONBEHALF = inv_bank_onbehalf;
	}

	public String getINV_EFFECTIVEDATE() {
		return INV_EFFECTIVEDATE;
	}

	public void setINV_EFFECTIVEDATE(String inv_effectivedate) {
		INV_EFFECTIVEDATE = inv_effectivedate;
	}

	public String getINV_EXPIRYDATE() {
		return INV_EXPIRYDATE;
	}

	public void setINV_EXPIRYDATE(String inv_expirydate) {
		INV_EXPIRYDATE = inv_expirydate;
	}

	public String getINV_JENIS_RO() {
		return INV_JENIS_RO;
	}

	public void setINV_JENIS_RO(String inv_jenis_ro) {
		INV_JENIS_RO = inv_jenis_ro;
	}

	public String getINV_PERIOD() {
		return INV_PERIOD;
	}

	public void setINV_PERIOD(String inv_period) {
		INV_PERIOD = inv_period;
	}

	public String getINV_RETURN() {
		return INV_RETURN;
	}

	public void setINV_RETURN(String inv_return) {
		INV_RETURN = inv_return;
	}

	public String getINV_TOT_BUNGA() {
		return INV_TOT_BUNGA;
	}

	public void setINV_TOT_BUNGA(String inv_tot_bunga) {
		INV_TOT_BUNGA = inv_tot_bunga;
	}

	public String getINV_TOT_NOMINAL() {
		return INV_TOT_NOMINAL;
	}

	public void setINV_TOT_NOMINAL(String inv_tot_nominal) {
		INV_TOT_NOMINAL = inv_tot_nominal;
	}

	public String getKODE_PRODUK() {
		return KODE_PRODUK;
	}

	public void setKODE_PRODUK(String kode_produk) {
		KODE_PRODUK = kode_produk;
	}

	public String getNAMA_PRODUK() {
		return NAMA_PRODUK;
	}

	public void setNAMA_PRODUK(String nama_produk) {
		NAMA_PRODUK = nama_produk;
	}

	public String getNO_CIF() {
		return NO_CIF;
	}

	public void setNO_CIF(String no_cif) {
		NO_CIF = no_cif;
	}

	public String getNO_POLIS() {
		return NO_POLIS;
	}

	public void setNO_POLIS(String no_polis) {
		NO_POLIS = no_polis;
	}

	public String getNO_SERI() {
		return NO_SERI;
	}

	public void setNO_SERI(String no_seri) {
		NO_SERI = no_seri;
	}

	public String getNO_SPAJ() {
		return NO_SPAJ;
	}

	public void setNO_SPAJ(String no_spaj) {
		NO_SPAJ = no_spaj;
	}

	public String getPOSISI_DOKUMEN() {
		return POSISI_DOKUMEN;
	}

	public void setPOSISI_DOKUMEN(String posisi_dokumen) {
		POSISI_DOKUMEN = posisi_dokumen;
	}

	public String getPP_AGAMA() {
		return PP_AGAMA;
	}

	public void setPP_AGAMA(String pp_agama) {
		PP_AGAMA = pp_agama;
	}

	public String getPP_ALAMAT_KANTOR() {
		return PP_ALAMAT_KANTOR;
	}

	public void setPP_ALAMAT_KANTOR(String pp_alamat_kantor) {
		PP_ALAMAT_KANTOR = pp_alamat_kantor;
	}

	public String getPP_ALAMAT_RUMAH() {
		return PP_ALAMAT_RUMAH;
	}

	public void setPP_ALAMAT_RUMAH(String pp_alamat_rumah) {
		PP_ALAMAT_RUMAH = pp_alamat_rumah;
	}

	public String getPP_AREA_CODE_FAX() {
		return PP_AREA_CODE_FAX;
	}

	public void setPP_AREA_CODE_FAX(String pp_area_code_fax) {
		PP_AREA_CODE_FAX = pp_area_code_fax;
	}

	public String getPP_AREA_CODE_KANTOR() {
		return PP_AREA_CODE_KANTOR;
	}

	public void setPP_AREA_CODE_KANTOR(String pp_area_code_kantor) {
		PP_AREA_CODE_KANTOR = pp_area_code_kantor;
	}

	public String getPP_AREA_CODE_KANTOR2() {
		return PP_AREA_CODE_KANTOR2;
	}

	public void setPP_AREA_CODE_KANTOR2(String pp_area_code_kantor2) {
		PP_AREA_CODE_KANTOR2 = pp_area_code_kantor2;
	}

	public String getPP_AREA_CODE_RUMAH() {
		return PP_AREA_CODE_RUMAH;
	}

	public void setPP_AREA_CODE_RUMAH(String pp_area_code_rumah) {
		PP_AREA_CODE_RUMAH = pp_area_code_rumah;
	}

	public String getPP_AREA_CODE_RUMAH2() {
		return PP_AREA_CODE_RUMAH2;
	}

	public void setPP_AREA_CODE_RUMAH2(String pp_area_code_rumah2) {
		PP_AREA_CODE_RUMAH2 = pp_area_code_rumah2;
	}

	public String getPP_BIRTHDATE() {
		return PP_BIRTHDATE;
	}

	public void setPP_BIRTHDATE(String pp_birthdate) {
		PP_BIRTHDATE = pp_birthdate;
	}

	public String getPP_BIRTHPLACE() {
		return PP_BIRTHPLACE;
	}

	public void setPP_BIRTHPLACE(String pp_birthplace) {
		PP_BIRTHPLACE = pp_birthplace;
	}

	public String getPP_EMAIL() {
		return PP_EMAIL;
	}

	public void setPP_EMAIL(String pp_email) {
		PP_EMAIL = pp_email;
	}

	public String getPP_GELAR() {
		return PP_GELAR;
	}

	public void setPP_GELAR(String pp_gelar) {
		PP_GELAR = pp_gelar;
	}

	public String getPP_HP() {
		return PP_HP;
	}

	public void setPP_HP(String pp_hp) {
		PP_HP = pp_hp;
	}

	public String getPP_ID() {
		return PP_ID;
	}

	public void setPP_ID(String pp_id) {
		PP_ID = pp_id;
	}

	public String getPP_INDUSTRI() {
		return PP_INDUSTRI;
	}

	public void setPP_INDUSTRI(String pp_industri) {
		PP_INDUSTRI = pp_industri;
	}

	public String getPP_INDUSTRI2() {
		return PP_INDUSTRI2;
	}

	public void setPP_INDUSTRI2(String pp_industri2) {
		PP_INDUSTRI2 = pp_industri2;
	}

	public String getPP_JENIS_ID() {
		return PP_JENIS_ID;
	}

	public void setPP_JENIS_ID(String pp_jenis_id) {
		PP_JENIS_ID = pp_jenis_id;
	}

	public String getPP_JENIS_ID2() {
		return PP_JENIS_ID2;
	}

	public void setPP_JENIS_ID2(String pp_jenis_id2) {
		PP_JENIS_ID2 = pp_jenis_id2;
	}

	public String getPP_JOB_DESC() {
		return PP_JOB_DESC;
	}

	public void setPP_JOB_DESC(String pp_job_desc) {
		PP_JOB_DESC = pp_job_desc;
	}

	public String getPP_JOB_DESC2() {
		return PP_JOB_DESC2;
	}

	public void setPP_JOB_DESC2(String pp_job_desc2) {
		PP_JOB_DESC2 = pp_job_desc2;
	}

	public String getPP_KD_POS_RUMAH() {
		return PP_KD_POS_RUMAH;
	}

	public void setPP_KD_POS_RUMAH(String pp_kd_pos_rumah) {
		PP_KD_POS_RUMAH = pp_kd_pos_rumah;
	}

	public String getPP_KD_POS_RUMAH2() {
		return PP_KD_POS_RUMAH2;
	}

	public void setPP_KD_POS_RUMAH2(String pp_kd_pos_rumah2) {
		PP_KD_POS_RUMAH2 = pp_kd_pos_rumah2;
	}

	public String getPP_KERJA() {
		return PP_KERJA;
	}

	public void setPP_KERJA(String pp_kerja) {
		PP_KERJA = pp_kerja;
	}

	public String getPP_KERJA2() {
		return PP_KERJA2;
	}

	public void setPP_KERJA2(String pp_kerja2) {
		PP_KERJA2 = pp_kerja2;
	}

	public String getPP_KOTA_KANTOR() {
		return PP_KOTA_KANTOR;
	}

	public void setPP_KOTA_KANTOR(String pp_kota_kantor) {
		PP_KOTA_KANTOR = pp_kota_kantor;
	}

	public String getPP_KOTA_RUMAH() {
		return PP_KOTA_RUMAH;
	}

	public void setPP_KOTA_RUMAH(String pp_kota_rumah) {
		PP_KOTA_RUMAH = pp_kota_rumah;
	}

	public String getPP_MOTHER() {
		return PP_MOTHER;
	}

	public void setPP_MOTHER(String pp_mother) {
		PP_MOTHER = pp_mother;
	}

	public String getPP_NAMA() {
		return PP_NAMA;
	}

	public void setPP_NAMA(String pp_nama) {
		PP_NAMA = pp_nama;
	}

	public String getPP_NO_FAX() {
		return PP_NO_FAX;
	}

	public void setPP_NO_FAX(String pp_no_fax) {
		PP_NO_FAX = pp_no_fax;
	}

	public String getPP_PENDANAAN() {
		return PP_PENDANAAN;
	}

	public void setPP_PENDANAAN(String pp_pendanaan) {
		PP_PENDANAAN = pp_pendanaan;
	}

	public String getPP_PENDANAAN2() {
		return PP_PENDANAAN2;
	}

	public void setPP_PENDANAAN2(String pp_pendanaan2) {
		PP_PENDANAAN2 = pp_pendanaan2;
	}

	public String getPP_PENDIDIKAN() {
		return PP_PENDIDIKAN;
	}

	public void setPP_PENDIDIKAN(String pp_pendidikan) {
		PP_PENDIDIKAN = pp_pendidikan;
	}

	public String getPP_PENGHASILAN() {
		return PP_PENGHASILAN;
	}

	public void setPP_PENGHASILAN(String pp_penghasilan) {
		PP_PENGHASILAN = pp_penghasilan;
	}

	public String getPP_PENGHASILAN2() {
		return PP_PENGHASILAN2;
	}

	public void setPP_PENGHASILAN2(String pp_penghasilan2) {
		PP_PENGHASILAN2 = pp_penghasilan2;
	}

	public String getPP_SEX() {
		return PP_SEX;
	}

	public void setPP_SEX(String pp_sex) {
		PP_SEX = pp_sex;
	}

	public String getPP_SMBR_PENGHASILAN() {
		return PP_SMBR_PENGHASILAN;
	}

	public void setPP_SMBR_PENGHASILAN(String pp_smbr_penghasilan) {
		PP_SMBR_PENGHASILAN = pp_smbr_penghasilan;
	}

	public String getPP_SMBR_PENGHASILAN2() {
		return PP_SMBR_PENGHASILAN2;
	}

	public void setPP_SMBR_PENGHASILAN2(String pp_smbr_penghasilan2) {
		PP_SMBR_PENGHASILAN2 = pp_smbr_penghasilan2;
	}

	public String getPP_TELPON_KANTOR() {
		return PP_TELPON_KANTOR;
	}

	public void setPP_TELPON_KANTOR(String pp_telpon_kantor) {
		PP_TELPON_KANTOR = pp_telpon_kantor;
	}

	public String getPP_TELPON_KANTOR2() {
		return PP_TELPON_KANTOR2;
	}

	public void setPP_TELPON_KANTOR2(String pp_telpon_kantor2) {
		PP_TELPON_KANTOR2 = pp_telpon_kantor2;
	}

	public String getPP_TELPON_RUMAH() {
		return PP_TELPON_RUMAH;
	}

	public void setPP_TELPON_RUMAH(String pp_telpon_rumah) {
		PP_TELPON_RUMAH = pp_telpon_rumah;
	}

	public String getPP_TELPON_RUMAH2() {
		return PP_TELPON_RUMAH2;
	}

	public void setPP_TELPON_RUMAH2(String pp_telpon_rumah2) {
		PP_TELPON_RUMAH2 = pp_telpon_rumah2;
	}

	public String getPP_TUJUAN() {
		return PP_TUJUAN;
	}

	public void setPP_TUJUAN(String pp_tujuan) {
		PP_TUJUAN = pp_tujuan;
	}

	public String getPP_TUJUAN2() {
		return PP_TUJUAN2;
	}

	public void setPP_TUJUAN2(String pp_tujuan2) {
		PP_TUJUAN2 = pp_tujuan2;
	}

	public String getPP_WN() {
		return PP_WN;
	}

	public void setPP_WN(String pp_wn) {
		PP_WN = pp_wn;
	}

	public String getSTATUS_AKSEP() {
		return STATUS_AKSEP;
	}

	public void setSTATUS_AKSEP(String status_aksep) {
		STATUS_AKSEP = status_aksep;
	}

	public String getSTATUS_POLIS() {
		return STATUS_POLIS;
	}

	public void setSTATUS_POLIS(String status_polis) {
		STATUS_POLIS = status_polis;
	}

	public String getTT_AGAMA() {
		return TT_AGAMA;
	}

	public void setTT_AGAMA(String tt_agama) {
		TT_AGAMA = tt_agama;
	}

	public String getTT_ALAMAT_KANTOR() {
		return TT_ALAMAT_KANTOR;
	}

	public void setTT_ALAMAT_KANTOR(String tt_alamat_kantor) {
		TT_ALAMAT_KANTOR = tt_alamat_kantor;
	}

	public String getTT_ALAMAT_RUMAH() {
		return TT_ALAMAT_RUMAH;
	}

	public void setTT_ALAMAT_RUMAH(String tt_alamat_rumah) {
		TT_ALAMAT_RUMAH = tt_alamat_rumah;
	}

	public String getTT_AREA_CODE_FAX() {
		return TT_AREA_CODE_FAX;
	}

	public void setTT_AREA_CODE_FAX(String tt_area_code_fax) {
		TT_AREA_CODE_FAX = tt_area_code_fax;
	}

	public String getTT_AREA_CODE_KANTOR() {
		return TT_AREA_CODE_KANTOR;
	}

	public void setTT_AREA_CODE_KANTOR(String tt_area_code_kantor) {
		TT_AREA_CODE_KANTOR = tt_area_code_kantor;
	}

	public String getTT_AREA_CODE_KANTOR2() {
		return TT_AREA_CODE_KANTOR2;
	}

	public void setTT_AREA_CODE_KANTOR2(String tt_area_code_kantor2) {
		TT_AREA_CODE_KANTOR2 = tt_area_code_kantor2;
	}

	public String getTT_AREA_CODE_RUMAH() {
		return TT_AREA_CODE_RUMAH;
	}

	public void setTT_AREA_CODE_RUMAH(String tt_area_code_rumah) {
		TT_AREA_CODE_RUMAH = tt_area_code_rumah;
	}

	public String getTT_AREA_CODE_RUMAH2() {
		return TT_AREA_CODE_RUMAH2;
	}

	public void setTT_AREA_CODE_RUMAH2(String tt_area_code_rumah2) {
		TT_AREA_CODE_RUMAH2 = tt_area_code_rumah2;
	}

	public String getTT_BIRTHDATE() {
		return TT_BIRTHDATE;
	}

	public void setTT_BIRTHDATE(String tt_birthdate) {
		TT_BIRTHDATE = tt_birthdate;
	}

	public String getTT_BIRTHPLACE() {
		return TT_BIRTHPLACE;
	}

	public void setTT_BIRTHPLACE(String tt_birthplace) {
		TT_BIRTHPLACE = tt_birthplace;
	}

	public String getTT_EMAIL() {
		return TT_EMAIL;
	}

	public void setTT_EMAIL(String tt_email) {
		TT_EMAIL = tt_email;
	}

	public String getTT_GELAR() {
		return TT_GELAR;
	}

	public void setTT_GELAR(String tt_gelar) {
		TT_GELAR = tt_gelar;
	}

	public String getTT_HP() {
		return TT_HP;
	}

	public void setTT_HP(String tt_hp) {
		TT_HP = tt_hp;
	}

	public String getTT_ID() {
		return TT_ID;
	}

	public void setTT_ID(String tt_id) {
		TT_ID = tt_id;
	}

	public String getTT_KD_POS_RUMAH() {
		return TT_KD_POS_RUMAH;
	}

	public void setTT_KD_POS_RUMAH(String tt_kd_pos_rumah) {
		TT_KD_POS_RUMAH = tt_kd_pos_rumah;
	}

	public String getTT_KD_POS_RUMAH2() {
		return TT_KD_POS_RUMAH2;
	}

	public void setTT_KD_POS_RUMAH2(String tt_kd_pos_rumah2) {
		TT_KD_POS_RUMAH2 = tt_kd_pos_rumah2;
	}

	public String getTT_KOTA_KANTOR() {
		return TT_KOTA_KANTOR;
	}

	public void setTT_KOTA_KANTOR(String tt_kota_kantor) {
		TT_KOTA_KANTOR = tt_kota_kantor;
	}

	public String getTT_KOTA_RUMAH() {
		return TT_KOTA_RUMAH;
	}

	public void setTT_KOTA_RUMAH(String tt_kota_rumah) {
		TT_KOTA_RUMAH = tt_kota_rumah;
	}

	public String getTT_MOTHER() {
		return TT_MOTHER;
	}

	public void setTT_MOTHER(String tt_mother) {
		TT_MOTHER = tt_mother;
	}

	public String getTT_NAMA() {
		return TT_NAMA;
	}

	public void setTT_NAMA(String tt_nama) {
		TT_NAMA = tt_nama;
	}

	public String getTT_NO_FAX() {
		return TT_NO_FAX;
	}

	public void setTT_NO_FAX(String tt_no_fax) {
		TT_NO_FAX = tt_no_fax;
	}

	public String getTT_PENDIDIKAN() {
		return TT_PENDIDIKAN;
	}

	public void setTT_PENDIDIKAN(String tt_pendidikan) {
		TT_PENDIDIKAN = tt_pendidikan;
	}

	public String getTT_SEX() {
		return TT_SEX;
	}

	public void setTT_SEX(String tt_sex) {
		TT_SEX = tt_sex;
	}

	public String getTT_TELPON_KANTOR() {
		return TT_TELPON_KANTOR;
	}

	public void setTT_TELPON_KANTOR(String tt_telpon_kantor) {
		TT_TELPON_KANTOR = tt_telpon_kantor;
	}

	public String getTT_TELPON_KANTOR2() {
		return TT_TELPON_KANTOR2;
	}

	public void setTT_TELPON_KANTOR2(String tt_telpon_kantor2) {
		TT_TELPON_KANTOR2 = tt_telpon_kantor2;
	}

	public String getTT_TELPON_RUMAH() {
		return TT_TELPON_RUMAH;
	}

	public void setTT_TELPON_RUMAH(String tt_telpon_rumah) {
		TT_TELPON_RUMAH = tt_telpon_rumah;
	}

	public String getTT_TELPON_RUMAH2() {
		return TT_TELPON_RUMAH2;
	}

	public void setTT_TELPON_RUMAH2(String tt_telpon_rumah2) {
		TT_TELPON_RUMAH2 = tt_telpon_rumah2;
	}

	public String getTT_WN() {
		return TT_WN;
	}

	public void setTT_WN(String tt_wn) {
		TT_WN = tt_wn;
	}

}
