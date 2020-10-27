package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class KuesionerTanggungan implements Serializable{
	
	/**
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Oct 11, 2012 2:59:57 PM
	 * @Description :
	 * 
	 */
	private static final long serialVersionUID = 7629475004834801326L;
	private String mku_no_reg;
	private String mkt_nama;
	private String mkt_jenis;
	private Date mkt_tgl_lahir;
	private String mkt_tempat_lahir;
	private String mkt_pendidikan;
	private Integer mkt_umur;
	private String mkt_hubungan;
	private Integer mkt_urut;
	
	
	private String nama;
	private Date tgl_lhr;
	private String tempat_lhr;
	private String pendidikan;
	private Integer umur;
	private String relasi;
	private Integer urut;
	
	public KuesionerTanggungan() {
	}
	
	public KuesionerTanggungan(String mku_no_reg, String mkt_nama, String mkt_jenis, Date mkt_tgl_lahir, String mkt_tempat_lahir, String mkt_pendidikan, Integer mkt_umur, String mkt_hubungan) {
		this.mku_no_reg = mku_no_reg;
		this.mkt_nama = mkt_nama;
		this.mkt_jenis = mkt_jenis;
		this.mkt_tgl_lahir = mkt_tgl_lahir;
		this.mkt_tempat_lahir = mkt_tempat_lahir;
		this.mkt_pendidikan = mkt_pendidikan;
		this.mkt_umur = mkt_umur;
		this.mkt_hubungan = mkt_hubungan;
		
		this.nama = mkt_nama;
		this.tgl_lhr = mkt_tgl_lahir;
		this.tempat_lhr = mkt_tempat_lahir;
		this.pendidikan = mkt_pendidikan;
		this.umur = mkt_umur;
		this.relasi = mkt_hubungan;
	}
	
	public String getMku_no_reg() {
		return mku_no_reg;
	}
	public void setMku_no_reg(String mku_no_reg) {
		this.mku_no_reg = mku_no_reg;
	}
	
	public String getMkt_nama() {
		return mkt_nama;
	}
	public void setMkt_nama(String mkt_nama) {
		this.mkt_nama = mkt_nama;
	}
	
	public String getMkt_jenis() {
		return mkt_jenis;
	}
	public void setMkt_jenis(String mkt_jenis) {
		this.mkt_jenis = mkt_jenis;
	}
	
	public Date getMkt_tgl_lahir() {
		return mkt_tgl_lahir;
	}
	public void setMkt_tgl_lahir(Date mkt_tgl_lahir) {
		this.mkt_tgl_lahir = mkt_tgl_lahir;
	}
	
	public String getMkt_tempat_lahir() {
		return mkt_tempat_lahir;
	}
	public void setMkt_tempat_lahir(String mkt_tempat_lahir) {
		this.mkt_tempat_lahir = mkt_tempat_lahir;
	}
	
	public String getMkt_pendidikan() {
		return mkt_pendidikan;
	}
	public void setMkt_pendidikan(String mkt_pendidikan) {
		this.mkt_pendidikan = mkt_pendidikan;
	}
	
	public Integer getMkt_umur() {
		return mkt_umur;
	}
	public void setMkt_umur(Integer mkt_umur) {
		this.mkt_umur = mkt_umur;
	}
	
	public String getMkt_hubungan() {
		return mkt_hubungan;
	}
	public void setMkt_hubungan(String mkt_hubungan) {
		this.mkt_hubungan = mkt_hubungan;
	}
	
	public Integer getMkt_urut() {
		return mkt_urut;
	}
	public void setMkt_urut(Integer mkt_urut) {
		this.mkt_urut = mkt_urut;
	}

	/**
	 * @return the nama
	 */
	public String getNama() {
		return nama;
	}

	/**
	 * @param nama the nama to set
	 */
	public void setNama(String nama) {
		this.nama = nama;
	}

	/**
	 * @return the tgl_lhr
	 */
	public Date getTgl_lhr() {
		return tgl_lhr;
	}

	/**
	 * @param tgl_lhr the tgl_lhr to set
	 */
	public void setTgl_lhr(Date tgl_lhr) {
		this.tgl_lhr = tgl_lhr;
	}

	/**
	 * @return the tempat_lhr
	 */
	public String getTempat_lhr() {
		return tempat_lhr;
	}

	/**
	 * @param tempat_lhr the tempat_lhr to set
	 */
	public void setTempat_lhr(String tempat_lhr) {
		this.tempat_lhr = tempat_lhr;
	}

	/**
	 * @return the pendidikan
	 */
	public String getPendidikan() {
		return pendidikan;
	}

	/**
	 * @param pendidikan the pendidikan to set
	 */
	public void setPendidikan(String pendidikan) {
		this.pendidikan = pendidikan;
	}

	/**
	 * @return the umur
	 */
	public Integer getUmur() {
		return umur;
	}

	/**
	 * @param umur the umur to set
	 */
	public void setUmur(Integer umur) {
		this.umur = umur;
	}

	/**
	 * @return the relasi
	 */
	public String getRelasi() {
		return relasi;
	}

	/**
	 * @param relasi the relasi to set
	 */
	public void setRelasi(String relasi) {
		this.relasi = relasi;
	}

	/**
	 * @return the urut
	 */
	public Integer getUrut() {
		return urut;
	}

	/**
	 * @param urut the urut to set
	 */
	public void setUrut(Integer urut) {
		this.urut = urut;
	}

	
	
	
			
}
