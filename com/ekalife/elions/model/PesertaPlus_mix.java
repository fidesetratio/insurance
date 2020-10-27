package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class PesertaPlus_mix implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer no_urut;                       
	private Integer lsre_id;
	private Integer lsre_id_x;
	private String lsre_relation;
	private String nama;                           
	private Date tgl_lahir;                        
	private Integer kelamin;                       
	private String sex;
	private Integer umur;                          
	private Double premi;                          
	private String reg_spaj;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private String plan_rider;
	private Double discount;
	private String no_reg;
	private Integer lspc_no;
	private Integer flag_val_send;
	private Integer flag_admedika;
	private Date next_send;
	private Integer flag_jenis_peserta;
	private Integer flag_jenis_peserta_x;
	private String jenis_peserta;
	private Date tanggal_lahir;
	private Integer lsbs_id2;
	private Integer lsdbs_number2;
	private Double mspr_premium;
	private Integer lsbs_id_plus;	
	private Integer mspo_provider;
	private Integer tinggi;
	private Integer berat;
	private String lsne;
	private String kerja;
	
	
	public String getKerja() {
		return kerja;
	}
	public void setKerja(String kerja) {
		this.kerja = kerja;
	}
	
	
	public Integer getTinggi() {
		return tinggi;
	}
	public void setTinggi(Integer tinggi) {
		this.tinggi = tinggi;
	}
	public Integer getBerat() {
		return berat;
	}
	public void setBerat(Integer berat) {
		this.berat = berat;
	}
	public String getLsne() {
		return lsne;
	}
	public void setLsne(String lsne) {
		this.lsne = lsne;
	}
	
	public Double getMspr_premium() {
		return mspr_premium;
	}
	public void setMspr_premium(Double mspr_premium) {
		this.mspr_premium = mspr_premium;
	}
	
	public Date getNext_send() {
		return next_send;
	}
	public void setNext_send(Date nextSend) {
		next_send = nextSend;
	}
	public Integer getFlag_admedika() {
		return flag_admedika;
	}
	public void setFlag_admedika(Integer flagAdmedika) {
		flag_admedika = flagAdmedika;
	}
	public Integer getFlag_val_send() {
		return flag_val_send;
	}
	public void setFlag_val_send(Integer flagValSend) {
		flag_val_send = flagValSend;
	}
	public Integer getLspc_no() {
		return lspc_no;
	}
	public void setLspc_no(Integer lspc_no) {
		this.lspc_no = lspc_no;
	}
	public String getNo_reg() {
		return no_reg;
	}
	public void setNo_reg(String no_reg) {
		this.no_reg = no_reg;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public String getPlan_rider() {
		return plan_rider;
	}
	public void setPlan_rider(String plan_rider) {
		this.plan_rider = plan_rider;
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
	public Integer getKelamin() {
		return kelamin;
	}
	public void setKelamin(Integer kelamin) {
		this.kelamin = kelamin;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public Integer getLsre_id_x() {
		return lsre_id_x;
	}
	public void setLsre_id_x(Integer lsre_id_x) {
		this.lsre_id_x = lsre_id_x;
	}
	public String getLsre_relation() {
		return lsre_relation;
	}
	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public Integer getNo_urut() {
		return no_urut;
	}
	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}
	public Double getPremi() {
		return premi;
	}
	public void setPremi(Double premi) {
		this.premi = premi;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public Date getTgl_lahir() {
		return tgl_lahir;
	}
	public void setTgl_lahir(Date tgl_lahir) {
		this.tgl_lahir = tgl_lahir;
	}
	public Integer getUmur() {
		return umur;
	}
	public void setUmur(Integer umur) {
		this.umur = umur;
	}
	public Integer getFlag_jenis_peserta(){
		return flag_jenis_peserta;
	}
	public void setFlag_jenis_peserta(Integer flag_jenis_peserta) {
		this.flag_jenis_peserta = flag_jenis_peserta;
	}
	public Integer getFlag_jenis_peserta_x(){
		return flag_jenis_peserta_x;
	}
	public void setFlag_jenis_peserta_x(Integer flag_jenis_peserta_x) {
		this.flag_jenis_peserta_x = flag_jenis_peserta_x;
	}
	public String getJenis_peserta(){
		return jenis_peserta;
	}
	public void setJenis_peserta(String jenis_peserta) {
		this.jenis_peserta = jenis_peserta;
	}
	
	public Date getTanggal_lahir(){
		return tanggal_lahir;
	}
	public void setTanggal_lahir(Date tanggal_lahir){
		this.tanggal_lahir = tanggal_lahir;
	}
	
	public Integer getLsbs_id2(){
		return lsbs_id2;
	}
	public void setLsbs_id2(Integer lsbs_id2){
		this.lsbs_id2 = lsbs_id2;
	}
	
	public Integer getLsdbs_number2(){
		return lsdbs_number2;
	}
	public void setLsdbs_number2(Integer lsdbs_number2){
		this.lsdbs_number2 = lsdbs_number2;
	}
	public Integer getLsbs_id_plus() {
		return lsbs_id_plus;
	}
	public void setLsbs_id_plus(Integer lsbs_id_plus) {
		this.lsbs_id_plus = lsbs_id_plus;
	}
	
	public Integer getMspo_provider() {
		return mspo_provider;
	}
	public void setMspo_provider(Integer mspo_provider) {
		this.mspo_provider = mspo_provider;
	}
	
}

