package com.ekalife.elions.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TmmsDet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5820946072851155141L;
	
	private String id;                             
	private Integer urut;                          
	private String nama_peserta;                   
	private Integer sex;                           
	private Date bod_peserta;
	private String bod_peserta_dd;
	private String bod_peserta_mm;
	private String bod_peserta_yyyy;
	private Integer usia;                          
	private Integer relasi;                        
	private String product_code;                   
	private Integer plan;                          
	private Date beg_aktif;                        
	private Date end_aktif;                        
	private Date beg_date;                         
	private Integer flag_aksep;                    
	private Integer user_aksep;                    
	private String notes;                          
	private Integer flag_aktif;                    
	private Integer flag_claim;                    
	private Integer flag_pk;                       
	private Integer flag_batal;                    
	private Date tgl_batal;                        
	private Integer user_batal;
	private String alasan_batal;                   
	private Date tgl_aksep;                        
	private Integer flag_spouse;                   
	private Integer flag_renew;                    
	private String bod_tempat;                     
	private String no_identitas;                   
	private Integer konfirmasi_peserta;
	private String nama_ibu;
	
	private double premi;
	private double komisi;
	private double diskon;
	
	private Integer jenis;
	
	public double getPremi() {
		return premi;
	}
	public void setPremi(double premi) {
		this.premi = premi;
	}
	public String getNama_ibu() {
		return nama_ibu;
	}
	public void setNama_ibu(String nama_ibu) {
		this.nama_ibu = nama_ibu;
	}
	public String getAlasan_batal() {
		return alasan_batal;
	}
	public void setAlasan_batal(String alasan_batal) {
		this.alasan_batal = alasan_batal;
	}
	public Date getBeg_aktif() {
		return beg_aktif;
	}
	public void setBeg_aktif(Date beg_aktif) {
		this.beg_aktif = beg_aktif;
	}
	public Date getBeg_date() {
		return beg_date;
	}
	public void setBeg_date(Date beg_date) {
		this.beg_date = beg_date;
	}
	public Date getBod_peserta() {
		return bod_peserta;
	}
	public void setBod_peserta(Date bod_peserta) {
		this.bod_peserta = bod_peserta;
		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String[] tmp = df.format(this.bod_peserta).split("/");
			this.bod_peserta_dd = tmp[0];
			this.bod_peserta_mm = tmp[1];
			this.bod_peserta_yyyy = tmp[2];
		}catch(Exception e) {}
	}
	public String getBod_tempat() {
		return bod_tempat;
	}
	public void setBod_tempat(String bod_tempat) {
		this.bod_tempat = bod_tempat;
	}
	public Date getEnd_aktif() {
		return end_aktif;
	}
	public void setEnd_aktif(Date end_aktif) {
		this.end_aktif = end_aktif;
	}
	public Integer getFlag_aksep() {
		return flag_aksep;
	}
	public void setFlag_aksep(Integer flag_aksep) {
		this.flag_aksep = flag_aksep;
	}
	public Integer getFlag_aktif() {
		return flag_aktif;
	}
	public void setFlag_aktif(Integer flag_aktif) {
		this.flag_aktif = flag_aktif;
	}
	public Integer getFlag_batal() {
		return flag_batal;
	}
	public void setFlag_batal(Integer flag_batal) {
		this.flag_batal = flag_batal;
	}
	public Integer getFlag_claim() {
		return flag_claim;
	}
	public void setFlag_claim(Integer flag_claim) {
		this.flag_claim = flag_claim;
	}
	public Integer getFlag_pk() {
		return flag_pk;
	}
	public void setFlag_pk(Integer flag_pk) {
		this.flag_pk = flag_pk;
	}
	public Integer getFlag_renew() {
		return flag_renew;
	}
	public void setFlag_renew(Integer flag_renew) {
		this.flag_renew = flag_renew;
	}
	public Integer getFlag_spouse() {
		return flag_spouse;
	}
	public void setFlag_spouse(Integer flag_spouse) {
		this.flag_spouse = flag_spouse;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getKonfirmasi_peserta() {
		return konfirmasi_peserta;
	}
	public void setKonfirmasi_peserta(Integer konfirmasi_peserta) {
		this.konfirmasi_peserta = konfirmasi_peserta;
	}
	public String getNama_peserta() {
		return nama_peserta;
	}
	public void setNama_peserta(String nama_peserta) {
		this.nama_peserta = nama_peserta;
	}
	public String getNo_identitas() {
		return no_identitas;
	}
	public void setNo_identitas(String no_identitas) {
		this.no_identitas = no_identitas;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Integer getPlan() {
		return plan;
	}
	public void setPlan(Integer plan) {
		this.plan = plan;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public Integer getRelasi() {
		return relasi;
	}
	public void setRelasi(Integer relasi) {
		this.relasi = relasi;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Date getTgl_aksep() {
		return tgl_aksep;
	}
	public void setTgl_aksep(Date tgl_aksep) {
		this.tgl_aksep = tgl_aksep;
	}
	public Date getTgl_batal() {
		return tgl_batal;
	}
	public void setTgl_batal(Date tgl_batal) {
		this.tgl_batal = tgl_batal;
	}
	public Integer getUrut() {
		return urut;
	}
	public void setUrut(Integer urut) {
		this.urut = urut;
	}
	public Integer getUser_aksep() {
		return user_aksep;
	}
	public void setUser_aksep(Integer user_aksep) {
		this.user_aksep = user_aksep;
	}
	public Integer getUser_batal() {
		return user_batal;
	}
	public void setUser_batal(Integer user_batal) {
		this.user_batal = user_batal;
	}
	public Integer getUsia() {
		return usia;
	}
	public void setUsia(Integer usia) {
		this.usia = usia;
	}
	public String getBod_peserta_dd() {
		return bod_peserta_dd;
	}
	public void setBod_peserta_dd(String bod_peserta_dd) {
		this.bod_peserta_dd = bod_peserta_dd;
	}
	public String getBod_peserta_mm() {
		return bod_peserta_mm;
	}
	public void setBod_peserta_mm(String bod_peserta_mm) {
		this.bod_peserta_mm = bod_peserta_mm;
	}
	public String getBod_peserta_yyyy() {
		return bod_peserta_yyyy;
	}
	public void setBod_peserta_yyyy(String bod_peserta_yyyy) {
		this.bod_peserta_yyyy = bod_peserta_yyyy;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}

	public double getDiskon() {
		return diskon;
	}

	public void setDiskon(double diskon) {
		this.diskon = diskon;
	}

	public double getKomisi() {
		return komisi;
	}

	public void setKomisi(double komisi) {
		this.komisi = komisi;
	}            

	
}
