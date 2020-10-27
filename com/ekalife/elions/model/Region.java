package com.ekalife.elions.model;

import java.io.Serializable;

/**
 * Domain Object untuk region
 * relasinya adalah sbb : 
 * 1. LST_USER_ADMIN menandakan hak akses seorang user terhadap suatu region (LUS_ID - LAR_ID)
 * 2. di-join dengan LST_REGION, akan mendapatkan akses seorang user terhadap cabang, wakil, dan region (LCA_ID, LWK_ID, LSRG_ID)
 * 3. dari tiga akses tersebut, dapat mengakses ke seluruh aspek seperti polis, spaj, agen, viewer, dan lainnya
 * 
 * @author Yusuf Sutarko
 * @since Feb 27, 2007 (8:33:04 AM)
 */
public class Region implements Serializable {


	private static final long serialVersionUID = -5255341952172196864L;
	private Integer lus_id;                        
	private Integer lar_id;                        
	private String lca_id;                         
	private String lwk_id;                         
	private String lsrg_id;                        
	private String lsrg_nama;                      
	private String lsrg_alamat;
	private String lca_lwk_lsrg;
	private String nm_kota;
	
	
	public String getNm_kota() {
		return nm_kota;
	}
	public void setNm_kota(String nm_kota) {
		this.nm_kota = nm_kota;
	}
	public String getLca_lwk_lsrg() {
		return lca_lwk_lsrg;
	}
	public void setLca_lwk_lsrg(String lca_lwk_lsrg) {
		this.lca_lwk_lsrg = lca_lwk_lsrg;
	}
	public Integer getLar_id() {
		return lar_id;
	}
	public void setLar_id(Integer lar_id) {
		this.lar_id = lar_id;
	}
	public String getLca_id() {
		return lca_id;
	}
	public void setLca_id(String lca_id) {
		this.lca_id = lca_id;
	}
	public String getLsrg_alamat() {
		return lsrg_alamat;
	}
	public void setLsrg_alamat(String lsrg_alamat) {
		this.lsrg_alamat = lsrg_alamat;
	}
	public String getLsrg_id() {
		return lsrg_id;
	}
	public void setLsrg_id(String lsrg_id) {
		this.lsrg_id = lsrg_id;
	}
	public String getLsrg_nama() {
		return lsrg_nama;
	}
	public void setLsrg_nama(String lsrg_nama) {
		this.lsrg_nama = lsrg_nama;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public String getLwk_id() {
		return lwk_id;
	}
	public void setLwk_id(String lwk_id) {
		this.lwk_id = lwk_id;
	}                    

}