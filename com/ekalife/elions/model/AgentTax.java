package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

/**
 * MST_AGENT_TAX
 * @since 16 Oct 09
 * @author Yusuf
 *
 */
public class AgentTax implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public Date adt_date;
	public String as_msag;
	public Double adec_comm_month;
	public Double adec_comm_year;
	public Double adec_ptkp;
	public Double adec_pkp_month;
	public Double adec_pkp_year;
	public Double adec_tax_pct;
	public Double adec_tax_paid;
	public Double adec_tax_real;
	public Date adt_pay;
	public String as_spt;
	public boolean ab_update;
	
	public boolean isAb_update() {
		return ab_update;
	}
	public void setAb_update(boolean ab_update) {
		this.ab_update = ab_update;
	}
	public Double getAdec_comm_month() {
		return adec_comm_month;
	}
	public void setAdec_comm_month(Double adec_comm_month) {
		this.adec_comm_month = adec_comm_month;
	}
	public Double getAdec_comm_year() {
		return adec_comm_year;
	}
	public void setAdec_comm_year(Double adec_comm_year) {
		this.adec_comm_year = adec_comm_year;
	}
	public Double getAdec_pkp_month() {
		return adec_pkp_month;
	}
	public void setAdec_pkp_month(Double adec_pkp_month) {
		this.adec_pkp_month = adec_pkp_month;
	}
	public Double getAdec_pkp_year() {
		return adec_pkp_year;
	}
	public void setAdec_pkp_year(Double adec_pkp_year) {
		this.adec_pkp_year = adec_pkp_year;
	}
	public Double getAdec_ptkp() {
		return adec_ptkp;
	}
	public void setAdec_ptkp(Double adec_ptkp) {
		this.adec_ptkp = adec_ptkp;
	}
	public Double getAdec_tax_paid() {
		return adec_tax_paid;
	}
	public void setAdec_tax_paid(Double adec_tax_paid) {
		this.adec_tax_paid = adec_tax_paid;
	}
	public Double getAdec_tax_pct() {
		return adec_tax_pct;
	}
	public void setAdec_tax_pct(Double adec_tax_pct) {
		this.adec_tax_pct = adec_tax_pct;
	}
	public Double getAdec_tax_real() {
		return adec_tax_real;
	}
	public void setAdec_tax_real(Double adec_tax_real) {
		this.adec_tax_real = adec_tax_real;
	}
	public Date getAdt_date() {
		return adt_date;
	}
	public void setAdt_date(Date adt_date) {
		this.adt_date = adt_date;
	}
	public Date getAdt_pay() {
		return adt_pay;
	}
	public void setAdt_pay(Date adt_pay) {
		this.adt_pay = adt_pay;
	}
	public String getAs_msag() {
		return as_msag;
	}
	public void setAs_msag(String as_msag) {
		this.as_msag = as_msag;
	}
	public String getAs_spt() {
		return as_spt;
	}
	public void setAs_spt(String as_spt) {
		this.as_spt = as_spt;
	}
	
}