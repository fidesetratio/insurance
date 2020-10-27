package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class Surat_history implements Serializable{
	
	/**
	 *@author Deddy
	 *@since Aug 6, 2014
	 *@description TODO 
	 */
	private static final long serialVersionUID = 7417330377151363665L;
	private String reg_spaj;                       
	private Integer jenis;                         
	private Integer no;                            
	private Date periode;                          
	private String no_ref;                         
	private String lde_id;                         
	private Date tgl_print;                        
	private Integer user_print;                    
	private String nama_user_print;                
	private Date tgl_kirim;                        
	private Integer user_kirim;                    
	private String nama_user_kirim;                
	private Date tgl_back;                         
	private Integer user_back;                     
	private String nama_user_back;                 
	private String alasan_back;                    
	private Date tgl_fu1;                          
	private Integer user_fu1;                      
	private String nama_user_fu1;                  
	private String ket_fu1;  
	private String ket2_fu1;
	private Date tgl_fu2;                          
	private Integer user_fu2;                      
	private String nama_user_fu2;                  
	private String ket_fu2;     
	private String ket2_fu2;
	private Date tgl_fu3;                          
	private Integer user_fu3;                      
	private String nama_user_fu3;                  
	private String ket_fu3;
	private String ket2_fu3; 
	private Date tgl_fu4;                          
	private Integer user_fu4;                      
	private String nama_user_fu4;                  
	private String ket_fu4;
	private String ket2_fu4;
	private Date tgl_fu5;                          
	private Integer user_fu5;                      
	private String nama_user_fu5;                  
	private String ket_fu5;   
	private String ket2_fu5;
	private Date tgl_kirim_back;                   
	private Integer user_kirim_back;               
	private String nama_user_kirim_back;           
	private String ket_kirim_back;                 
	private Integer flag_selesai;                  
	private Integer flag_insert;                   
	private String kirim_via;                      
	private String kirim_back_via;                 
	private String mspo_policy_no;                 
	private String lde_dept;                       
	private String nama;                           
	private String nm_pp;                          
	private String lca_nama;
	
	private String tanggalAwal;
	private String tanggalAkhir;
	private String mssg;
	private String successMsg;
	private String lus_id;
	private String jenis_surat;
	
	private Integer mode;
	private MultipartFile msth_attach;
	
	private List<Map> ls_attachment;
	
	public String getJenis_surat() {
		return jenis_surat;
	}
	public void setJenis_surat(String jenis_surat) {
		this.jenis_surat = jenis_surat;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String regSpaj) {
		reg_spaj = regSpaj;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	public Integer getNo() {
		return no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public Date getPeriode() {
		return periode;
	}
	public void setPeriode(Date periode) {
		this.periode = periode;
	}
	public String getNo_ref() {
		return no_ref;
	}
	public void setNo_ref(String noRef) {
		no_ref = noRef;
	}
	public String getLde_id() {
		return lde_id;
	}
	public void setLde_id(String ldeId) {
		lde_id = ldeId;
	}
	public Date getTgl_print() {
		return tgl_print;
	}
	public void setTgl_print(Date tglPrint) {
		tgl_print = tglPrint;
	}
	public Integer getUser_print() {
		return user_print;
	}
	public void setUser_print(Integer userPrint) {
		user_print = userPrint;
	}
	public String getNama_user_print() {
		return nama_user_print;
	}
	public void setNama_user_print(String namaUserPrint) {
		nama_user_print = namaUserPrint;
	}
	public Date getTgl_kirim() {
		return tgl_kirim;
	}
	public void setTgl_kirim(Date tglKirim) {
		tgl_kirim = tglKirim;
	}
	public Integer getUser_kirim() {
		return user_kirim;
	}
	public void setUser_kirim(Integer userKirim) {
		user_kirim = userKirim;
	}
	public String getNama_user_kirim() {
		return nama_user_kirim;
	}
	public void setNama_user_kirim(String namaUserKirim) {
		nama_user_kirim = namaUserKirim;
	}
	public Date getTgl_back() {
		return tgl_back;
	}
	public void setTgl_back(Date tglBack) {
		tgl_back = tglBack;
	}
	public Integer getUser_back() {
		return user_back;
	}
	public void setUser_back(Integer userBack) {
		user_back = userBack;
	}
	public String getNama_user_back() {
		return nama_user_back;
	}
	public void setNama_user_back(String namaUserBack) {
		nama_user_back = namaUserBack;
	}
	public String getAlasan_back() {
		return alasan_back;
	}
	public void setAlasan_back(String alasanBack) {
		alasan_back = alasanBack;
	}
	public Date getTgl_fu1() {
		return tgl_fu1;
	}
	public void setTgl_fu1(Date tglFu1) {
		tgl_fu1 = tglFu1;
	}
	public Integer getUser_fu1() {
		return user_fu1;
	}
	public void setUser_fu1(Integer userFu1) {
		user_fu1 = userFu1;
	}
	public String getNama_user_fu1() {
		return nama_user_fu1;
	}
	public void setNama_user_fu1(String namaUserFu1) {
		nama_user_fu1 = namaUserFu1;
	}
	public String getKet_fu1() {
		return ket_fu1;
	}
	public void setKet_fu1(String ketFu1) {
		ket_fu1 = ketFu1;
	}
	public Date getTgl_fu2() {
		return tgl_fu2;
	}
	public void setTgl_fu2(Date tglFu2) {
		tgl_fu2 = tglFu2;
	}
	public Integer getUser_fu2() {
		return user_fu2;
	}
	public void setUser_fu2(Integer userFu2) {
		user_fu2 = userFu2;
	}
	public String getNama_user_fu2() {
		return nama_user_fu2;
	}
	public void setNama_user_fu2(String namaUserFu2) {
		nama_user_fu2 = namaUserFu2;
	}
	public String getKet_fu2() {
		return ket_fu2;
	}
	public void setKet_fu2(String ketFu2) {
		ket_fu2 = ketFu2;
	}
	public Date getTgl_fu3() {
		return tgl_fu3;
	}
	public void setTgl_fu3(Date tglFu3) {
		tgl_fu3 = tglFu3;
	}
	public Integer getUser_fu3() {
		return user_fu3;
	}
	public void setUser_fu3(Integer userFu3) {
		user_fu3 = userFu3;
	}
	public String getNama_user_fu3() {
		return nama_user_fu3;
	}
	public void setNama_user_fu3(String namaUserFu3) {
		nama_user_fu3 = namaUserFu3;
	}
	public String getKet_fu3() {
		return ket_fu3;
	}
	public void setKet_fu3(String ketFu3) {
		ket_fu3 = ketFu3;
	}
	public Date getTgl_fu4() {
		return tgl_fu4;
	}
	public void setTgl_fu4(Date tglFu4) {
		tgl_fu4 = tglFu4;
	}
	public Integer getUser_fu4() {
		return user_fu4;
	}
	public void setUser_fu4(Integer userFu4) {
		user_fu4 = userFu4;
	}
	public String getNama_user_fu4() {
		return nama_user_fu4;
	}
	public void setNama_user_fu4(String namaUserFu4) {
		nama_user_fu4 = namaUserFu4;
	}
	public String getKet_fu4() {
		return ket_fu4;
	}
	public void setKet_fu4(String ketFu4) {
		ket_fu4 = ketFu4;
	}
	public Date getTgl_fu5() {
		return tgl_fu5;
	}
	public void setTgl_fu5(Date tglFu5) {
		tgl_fu5 = tglFu5;
	}
	public Integer getUser_fu5() {
		return user_fu5;
	}
	public void setUser_fu5(Integer userFu5) {
		user_fu5 = userFu5;
	}
	public String getNama_user_fu5() {
		return nama_user_fu5;
	}
	public void setNama_user_fu5(String namaUserFu5) {
		nama_user_fu5 = namaUserFu5;
	}
	public String getKet_fu5() {
		return ket_fu5;
	}
	public void setKet_fu5(String ketFu5) {
		ket_fu5 = ketFu5;
	}
	public Date getTgl_kirim_back() {
		return tgl_kirim_back;
	}
	public void setTgl_kirim_back(Date tglKirimBack) {
		tgl_kirim_back = tglKirimBack;
	}
	public Integer getUser_kirim_back() {
		return user_kirim_back;
	}
	public void setUser_kirim_back(Integer userKirimBack) {
		user_kirim_back = userKirimBack;
	}
	public String getNama_user_kirim_back() {
		return nama_user_kirim_back;
	}
	public void setNama_user_kirim_back(String namaUserKirimBack) {
		nama_user_kirim_back = namaUserKirimBack;
	}
	public String getKet_kirim_back() {
		return ket_kirim_back;
	}
	public void setKet_kirim_back(String ketKirimBack) {
		ket_kirim_back = ketKirimBack;
	}
	public Integer getFlag_selesai() {
		return flag_selesai;
	}
	public void setFlag_selesai(Integer flagSelesai) {
		flag_selesai = flagSelesai;
	}
	public Integer getFlag_insert() {
		return flag_insert;
	}
	public void setFlag_insert(Integer flagInsert) {
		flag_insert = flagInsert;
	}
	public String getKirim_via() {
		return kirim_via;
	}
	public void setKirim_via(String kirimVia) {
		kirim_via = kirimVia;
	}
	public String getKirim_back_via() {
		return kirim_back_via;
	}
	public void setKirim_back_via(String kirimBackVia) {
		kirim_back_via = kirimBackVia;
	}
	public String getMspo_policy_no() {
		return mspo_policy_no;
	}
	public void setMspo_policy_no(String mspoPolicyNo) {
		mspo_policy_no = mspoPolicyNo;
	}
	public String getLde_dept() {
		return lde_dept;
	}
	public void setLde_dept(String ldeDept) {
		lde_dept = ldeDept;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getNm_pp() {
		return nm_pp;
	}
	public void setNm_pp(String nmPp) {
		nm_pp = nmPp;
	}
	public String getLca_nama() {
		return lca_nama;
	}
	public void setLca_nama(String lcaNama) {
		lca_nama = lcaNama;
	}
	public String getTanggalAwal() {
		return tanggalAwal;
	}
	public void setTanggalAwal(String tanggalAwal) {
		this.tanggalAwal = tanggalAwal;
	}
	public String getTanggalAkhir() {
		return tanggalAkhir;
	}
	public void setTanggalAkhir(String tanggalAkhir) {
		this.tanggalAkhir = tanggalAkhir;
	}
	public String getMssg() {
		return mssg;
	}
	public void setMssg(String mssg) {
		this.mssg = mssg;
	}
	public String getSuccessMsg() {
		return successMsg;
	}
	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}
	public String getKet2_fu1() {
		return ket2_fu1;
	}
	public void setKet2_fu1(String ket2Fu1) {
		ket2_fu1 = ket2Fu1;
	}
	public String getKet2_fu2() {
		return ket2_fu2;
	}
	public void setKet2_fu2(String ket2Fu2) {
		ket2_fu2 = ket2Fu2;
	}
	public String getKet2_fu3() {
		return ket2_fu3;
	}
	public void setKet2_fu3(String ket2Fu3) {
		ket2_fu3 = ket2Fu3;
	}
	public String getKet2_fu4() {
		return ket2_fu4;
	}
	public void setKet2_fu4(String ket2Fu4) {
		ket2_fu4 = ket2Fu4;
	}
	public String getKet2_fu5() {
		return ket2_fu5;
	}
	public void setKet2_fu5(String ket2Fu5) {
		ket2_fu5 = ket2Fu5;
	}
	public MultipartFile getMsth_attach() {
		return msth_attach;
	}
	public void setMsth_attach(MultipartFile msth_attach) {
		this.msth_attach = msth_attach;
	}
	public List<Map> getLs_attachment() {
		return ls_attachment;
	}
	public void setLs_attachment(List<Map> ls_attachment) {
		this.ls_attachment = ls_attachment;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
}
