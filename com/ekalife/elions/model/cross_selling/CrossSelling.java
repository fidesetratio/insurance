package com.ekalife.elions.model.cross_selling;

import java.io.Serializable;
import java.util.List;

/**
 * Class Command untuk controller2 yang berhubungan dengan cross selling
 * 
 * @author Yusuf
 * @since Jul 21, 2008 (9:17:46 AM)
 */
public class CrossSelling implements Serializable{

	private static final long serialVersionUID = 1L;

	public PolicyCs policyCs;
	public String mode; //"insert", "update"
	
	//untuk layar konfirmasi
	public String reg_spaj;
	public String mscs_holder;
	public String startDate;
	public String endDate;
	public String mscs_policy_type;
	public String getMscs_policy_type() {
		return mscs_policy_type;
	}
	public void setMscs_policy_type(String mscsPolicyType) {
		mscs_policy_type = mscsPolicyType;
	}
	public List<PolicyCs> daftarSpaj;
	
	//getter setter
	public PolicyCs getPolicyCs() {
		return policyCs;
	}
	public void setPolicyCs(PolicyCs policyCs) {
		this.policyCs = policyCs;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getMscs_holder() {
		return mscs_holder;
	}
	public void setMscs_holder(String mscs_holder) {
		this.mscs_holder = mscs_holder;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public List<PolicyCs> getDaftarSpaj() {
		return daftarSpaj;
	}
	public void setDaftarSpaj(List<PolicyCs> daftarSpaj) {
		this.daftarSpaj = daftarSpaj;
	}
	
}
