package com.ekalife.elions.web.refund.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RefundCentrix implements Serializable{

	/**
	 * MANTA (15-03-2016)
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String applicationID;
	private String policyHolder;
	private String ccHolderName;
	private String ccNo;
	private String ccCardType;
	private String ccBankName;
	private String paymentMethod;
	private String site;
	private String product;
	private String taskgroup;
	private String status;
	private BigDecimal initialPremium;
	private String trxDate;
	private Boolean isActive;
	private Boolean isSubmitted;
	
	

	public String getApplicationID() {
		return applicationID;
	}
	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}
	public String getPolicyHolder() {
		return policyHolder;
	}
	public void setPolicyHolder(String policyHolder) {
		this.policyHolder = policyHolder;
	}
	public String getCcHolderName() {
		return ccHolderName;
	}
	public void setCcHolderName(String ccHolderName) {
		this.ccHolderName = ccHolderName;
	}
	public String getCcNo() {
		return ccNo;
	}
	public void setCcNo(String ccNo) {
		this.ccNo = ccNo;
	}
	public String getCcCardType() {
		return ccCardType;
	}
	public void setCcCardType(String ccCardType) {
		this.ccCardType = ccCardType;
	}
	public String getCcBankName() {
		return ccBankName;
	}
	public void setCcBankName(String ccBankName) {
		this.ccBankName = ccBankName;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getTaskgroup() {
		return taskgroup;
	}
	public void setTaskgroup(String taskgroup) {
		this.taskgroup = taskgroup;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BigDecimal getInitialPremium() {
		return initialPremium;
	}
	public void setInitialPremium(BigDecimal initialPremium) {
		this.initialPremium = initialPremium;
	}
	public String getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsSubmitted() {
		return isSubmitted;
	}
	public void setIsSubmitted(Boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}
}
