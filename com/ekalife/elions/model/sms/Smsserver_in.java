package com.ekalife.elions.model.sms;

import java.io.Serializable;
import java.util.Date;

public class Smsserver_in implements Serializable{
	
	/**
	 * Field type : long
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : 2:00:19 PM
	 */
	private static final long serialVersionUID = 426397926174512481L;

	private Integer id;                            
	private Integer process;                       
	private String originator;                     
	private String type;                           
	private String encoding;                       
	private Date message_date;                         
	private Date receive_date;                         
	private String text;                           
	private String original_ref_no;                
	private Date original_receive_date;                
	private String gateway_id;
	private Date process_date;
	private Integer lus_id;
	private String proses_ket;
	
	private String username;
	private Integer id_kirim; 
	private String msg_reply;
	private String  status_delivery;
	
	
	public String getStatus_delivery() {
		return status_delivery;
	}
	public void setStatus_delivery(String status_delivery) {
		this.status_delivery = status_delivery;
	}
	public String getProses_ket() {
		return proses_ket;
	}
	public void setProses_ket(String proses_ket) {
		this.proses_ket = proses_ket;
	}
	public String getEncoding() {
		return encoding;
	}
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	public String getGateway_id() {
		return gateway_id;
	}
	public void setGateway_id(String gateway_id) {
		this.gateway_id = gateway_id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getMessage_date() {
		return message_date;
	}
	public void setMessage_date(Date message_date) {
		this.message_date = message_date;
	}
	public Date getOriginal_receive_date() {
		return original_receive_date;
	}
	public void setOriginal_receive_date(Date original_receive_date) {
		this.original_receive_date = original_receive_date;
	}
	public String getOriginal_ref_no() {
		return original_ref_no;
	}
	public void setOriginal_ref_no(String original_ref_no) {
		this.original_ref_no = original_ref_no;
	}
	public String getOriginator() {
		return originator;
	}
	public void setOriginator(String originator) {
		this.originator = originator;
	}
	public Integer getProcess() {
		return process;
	}
	/**
	 * 0=belum diproses;1=follow up by phone;2=follow up by sms;3=tidak perlu di follow up;
	 * @param process
	 * Filename : Smsserver_in.java
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : Jan 28, 2010 4:43:44 PM
	 */
	public void setProcess(Integer process) {
		this.process = process;
	}
	public Date getReceive_date() {
		return receive_date;
	}
	public void setReceive_date(Date receive_date) {
		this.receive_date = receive_date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getLus_id() {
		return lus_id;
	}
	public void setLus_id(Integer lus_id) {
		this.lus_id = lus_id;
	}
	public Date getProcess_date() {
		return process_date;
	}
	public void setProcess_date(Date process_date) {
		this.process_date = process_date;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getId_kirim() {
		return id_kirim;
	}
	public void setId_kirim(Integer id_kirim) {
		this.id_kirim = id_kirim;
	}
	public String getMsg_reply() {
		return msg_reply;
	}
	public void setMsg_reply(String msg_reply) {
		this.msg_reply = msg_reply;
	}                     

	
	
	


}

