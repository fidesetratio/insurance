package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model object untuk tabel EKA.MST_FORM dan relasinya,
 * digunakan di modul sistem kontrol spaj 
 * (package com.ekalife.elions.web.bas)
 * 
 * @author Yusuf Sutarko
 * @since Feb 23, 2007 (9:22:04 AM)
 */
public class FormGetInfoRate implements Serializable {

	private static final long serialVersionUID = -3095131999735568833L;
	private Integer li_umur;
	private Date ldt_bdate;
	private Integer li_bisnis;
	private Integer ldec_up;
	
    public FormGetInfoRate( Integer li_umur, Date ldt_bdate, Integer li_bisnis, Integer ldec_up )
    {
        this.li_umur = li_umur;
        this.ldt_bdate = ldt_bdate;
        this.li_bisnis = li_bisnis;
        this.ldec_up = ldec_up;
    }


	public Integer getLdec_up() {
		return ldec_up;
	}

	public void setLdec_up(Integer ldec_up) {
		this.ldec_up = ldec_up;
	}

	public Date getLdt_bdate() {
		return ldt_bdate;
	}
	public void setLdt_bdate(Date ldt_bdate) {
		this.ldt_bdate = ldt_bdate;
	}
	public Integer getLi_bisnis() {
		return li_bisnis;
	}
	public void setLi_bisnis(Integer li_bisnis) {
		this.li_bisnis = li_bisnis;
	}
	public Integer getLi_umur() {
		return li_umur;
	}
	public void setLi_umur(Integer li_umur) {
		this.li_umur = li_umur;
	}

}