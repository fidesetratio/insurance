package com.ekalife.elions.model.worksheet;

import java.io.Serializable;

public class UwTumor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String nv_tumor_marker_cea;
	private String nv_tumor_marker_afp;
	private String nv_tumor_marker_psa;
	private String nv_tumor_marker_ca_15_3;

	private String reg_spaj;
	private Integer insured_no;
	private Integer urutanTumor;
	private String tglMcuTumor;
	private String tmpMcuTumor;
	private String cea;
	private String afp;
	private String psa;
	private String ca_15_3;
	
	public UwTumor() {
		setNv_tumor_marker_cea("<= 2.5 ng/ml");
		setNv_tumor_marker_afp("<= 7,0 ng/ml");
		setNv_tumor_marker_psa("<= 4 ng/ml");
		setNv_tumor_marker_ca_15_3("< 49 U/ml");
	}

	public String getNv_tumor_marker_afp() { return nv_tumor_marker_afp; }
	public void setNv_tumor_marker_afp(String nv_tumor_marker_afp) { this.nv_tumor_marker_afp = nv_tumor_marker_afp; }

	public String getNv_tumor_marker_ca_15_3() { return nv_tumor_marker_ca_15_3; }
	public void setNv_tumor_marker_ca_15_3(String nv_tumor_marker_ca_15_3) { this.nv_tumor_marker_ca_15_3 = nv_tumor_marker_ca_15_3; }

	public String getNv_tumor_marker_cea() { return nv_tumor_marker_cea; }
	public void setNv_tumor_marker_cea(String nv_tumor_marker_cea) { this.nv_tumor_marker_cea = nv_tumor_marker_cea; }

	public String getNv_tumor_marker_psa() { return nv_tumor_marker_psa; }
	public void setNv_tumor_marker_psa(String nv_tumor_marker_psa) { this.nv_tumor_marker_psa = nv_tumor_marker_psa; }

	public Integer getInsured_no() { return insured_no; }
	public void setInsured_no(Integer insured_no) { this.insured_no = insured_no; }

	public String getReg_spaj() { return reg_spaj; }
	public void setReg_spaj(String reg_spaj) { this.reg_spaj = reg_spaj; }

	public String getAfp() { return afp; }
	public void setAfp(String afp) { this.afp = afp; }

	public String getCa_15_3() { return ca_15_3; }
	public void setCa_15_3(String ca_15_3) { this.ca_15_3 = ca_15_3; }

	public String getCea() { return cea; }
	public void setCea(String cea) { this.cea = cea; }

	public String getPsa() { return psa; }
	public void setPsa(String psa) { this.psa = psa; }

	public String getTglMcuTumor() { return tglMcuTumor; }
	public void setTglMcuTumor(String tglMcuTumor) { this.tglMcuTumor = tglMcuTumor; }

	public String getTmpMcuTumor() { return tmpMcuTumor; }
	public void setTmpMcuTumor(String tmpMcuTumor) { this.tmpMcuTumor = tmpMcuTumor; }

	public Integer getUrutanTumor() { return urutanTumor; }
	public void setUrutanTumor(Integer urutanTumor) { this.urutanTumor = urutanTumor; }
	
	 
}
