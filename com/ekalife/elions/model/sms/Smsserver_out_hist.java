package com.ekalife.elions.model.sms;

import java.io.Serializable;
import java.util.Date;

public class Smsserver_out_hist implements Serializable {

	private static final long serialVersionUID = -956905857535467826L;
	
	private Integer id;
	private Date tgl_input;
	private String status;
	private Date sent_date;
	private Date sent_modem_date;
	private Date receive_modem_date;
	private String vendor_msg_id;
	private Integer vendor_id;
	private String host_name;
	private Date host_date;
	private String keterangan;
	private Integer length;
	private String recipient;
	private Integer total_sms;
	private Integer vendor_sts;
	private Integer vendor_sts_drpt;
	private Integer vendor_url_response;
	private String batch_id;
	private String operator;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getTgl_input() {
		return tgl_input;
	}
	public String getHost_name() {
		return host_name;
	}
	public void setHost_name(String host_name) {
		this.host_name = host_name;
	}
	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getSent_date() {
		return sent_date;
	}
	public void setSent_date(Date sent_date) {
		this.sent_date = sent_date;
	}
	public Date getSent_modem_date() {
		return sent_modem_date;
	}
	public void setSent_modem_date(Date sent_modem_date) {
		this.sent_modem_date = sent_modem_date;
	}
	public Date getReceive_modem_date() {
		return receive_modem_date;
	}
	public void setReceive_modem_date(Date receive_modem_date) {
		this.receive_modem_date = receive_modem_date;
	}
	public String getVendor_msg_id() {
		return vendor_msg_id;
	}
	public void setVendor_msg_id(String vendor_msg_id) {
		this.vendor_msg_id = vendor_msg_id;
	}
	public Integer getVendor_id() {
		return vendor_id;
	}
	public void setVendor_id(Integer vendor_id) {
		this.vendor_id = vendor_id;
	}
	public Date getHost_date() {
		return host_date;
	}
	public void setHost_date(Date host_date) {
		this.host_date = host_date;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public Integer getTotal_sms() {
		return total_sms;
	}
	public void setTotal_sms(Integer total_sms) {
		this.total_sms = total_sms;
	}
	public Integer getVendor_sts() {
		return vendor_sts;
	}
	public String getBatch_id() {
		return batch_id;
	}
	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}
	public Integer getVendor_url_response() {
		return vendor_url_response;
	}
	public void setVendor_url_response(Integer vendor_url_response) {
		this.vendor_url_response = vendor_url_response;
	}
	public Integer getVendor_sts_drpt() {
		return vendor_sts_drpt;
	}
	public void setVendor_sts_drpt(Integer vendor_sts_drpt) {
		this.vendor_sts_drpt = vendor_sts_drpt;
	}
	public void setVendor_sts(Integer vendor_sts) {
		this.vendor_sts = vendor_sts;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

}
