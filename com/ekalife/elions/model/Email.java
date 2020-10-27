package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String me_id;
	public String me_system;
	public Integer me_show_system;
	public Integer me_show_footer;
	public String me_from;
	public String me_from_alias;
	public String me_to;
	public String me_cc;
	public String me_bcc;
	public String me_subject;
	public String me_message;
	public Integer mec_id;
	public Integer me_html;
	public Integer me_status;
	public String me_status_message;
	public Integer me_count;
	public Integer me_lus_id;
	public Date me_input_date;
	public Date me_sent_date;
	public String me_reg_spaj;
	public Integer lje_id;
	
	public String getMe_reg_spaj() {
		return me_reg_spaj;
	}
	public void setMe_reg_spaj(String me_reg_spaj) {
		this.me_reg_spaj = me_reg_spaj;
	}
	public String getMe_id() {
		return me_id;
	}
	public void setMe_id(String me_id) {
		this.me_id = me_id;
	}
	public String getMe_system() {
		return me_system;
	}
	public void setMe_system(String me_system) {
		this.me_system = me_system;
	}
	public Integer getMe_show_system() {
		return me_show_system;
	}
	public void setMe_show_system(Integer me_show_system) {
		this.me_show_system = me_show_system;
	}
	public Integer getMe_show_footer() {
		return me_show_footer;
	}
	public void setMe_show_footer(Integer me_show_footer) {
		this.me_show_footer = me_show_footer;
	}
	public String getMe_from() {
		return me_from;
	}
	public void setMe_from(String me_from) {
		this.me_from = me_from;
	}
	public String getMe_from_alias() {
		return me_from_alias;
	}
	public void setMe_from_alias(String me_from_alias) {
		this.me_from_alias = me_from_alias;
	}
	public String getMe_to() {
		return me_to;
	}
	public void setMe_to(String me_to) {
		this.me_to = me_to;
	}
	public String getMe_cc() {
		return me_cc;
	}
	public void setMe_cc(String me_cc) {
		this.me_cc = me_cc;
	}
	public String getMe_bcc() {
		return me_bcc;
	}
	public void setMe_bcc(String me_bcc) {
		this.me_bcc = me_bcc;
	}
	public String getMe_subject() {
		return me_subject;
	}
	public void setMe_subject(String me_subject) {
		this.me_subject = me_subject;
	}
	public String getMe_message() {
		return me_message;
	}
	public void setMe_message(String me_message) {
		this.me_message = me_message;
	}
	public Integer getMec_id() {
		return mec_id;
	}
	public void setMec_id(Integer mec_id) {
		this.mec_id = mec_id;
	}
	public Integer getMe_html() {
		return me_html;
	}
	public void setMe_html(Integer me_html) {
		this.me_html = me_html;
	}
	public Integer getMe_status() {
		return me_status;
	}
	public void setMe_status(Integer me_status) {
		this.me_status = me_status;
	}
	public String getMe_status_message() {
		return me_status_message;
	}
	public void setMe_status_message(String me_status_message) {
		this.me_status_message = me_status_message;
	}
	public Integer getMe_count() {
		return me_count;
	}
	public void setMe_count(Integer me_count) {
		this.me_count = me_count;
	}
	public Integer getMe_lus_id() {
		return me_lus_id;
	}
	public void setMe_lus_id(Integer me_lus_id) {
		this.me_lus_id = me_lus_id;
	}
	public Date getMe_input_date() {
		return me_input_date;
	}
	public void setMe_input_date(Date me_input_date) {
		this.me_input_date = me_input_date;
	}
	public Date getMe_sent_date() {
		return me_sent_date;
	}
	public void setMe_sent_date(Date me_sent_date) {
		this.me_sent_date = me_sent_date;
	}
	public Integer getLje_id() {
		return lje_id;
	}
	public void setLje_id(Integer lje_id) {
		this.lje_id = lje_id;
	}
}
