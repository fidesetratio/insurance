package com.ekalife.elions.model;

import java.io.Serializable;

public class Menu implements Serializable {

	private static final long serialVersionUID = -5347955233097641457L;
	
	private Integer parent_menu_id;                
	private Integer lst_menu_id;                   
	private Integer menu_id;                       
	private String nama_menu;                      
	private String link_menu;                      
	private String flag_aktif;                     
	private String flag_public;                    
	private Integer tingkat;                       
	private Integer urutan;                        
	private Integer jenis;                         
	private String icon;
	private Integer level;
	private String lus_id;
	private String id;
	
	public String getFlag_aktif() {
		return flag_aktif;
	}
	public void setFlag_aktif(String flag_aktif) {
		this.flag_aktif = flag_aktif;
	}
	public String getFlag_public() {
		return flag_public;
	}
	public void setFlag_public(String flag_public) {
		this.flag_public = flag_public;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getLink_menu() {
		return link_menu;
	}
	public void setLink_menu(String link_menu) {
		this.link_menu = link_menu;
	}
	public Integer getLst_menu_id() {
		return lst_menu_id;
	}
	public void setLst_menu_id(Integer lst_menu_id) {
		this.lst_menu_id = lst_menu_id;
	}
	public String getLus_id() {
		return lus_id;
	}
	public void setLus_id(String lus_id) {
		this.lus_id = lus_id;
	}
	public Integer getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}
	public String getNama_menu() {
		return nama_menu;
	}
	public void setNama_menu(String nama_menu) {
		this.nama_menu = nama_menu;
	}
	public Integer getParent_menu_id() {
		return parent_menu_id;
	}
	public void setParent_menu_id(Integer parent_menu_id) {
		this.parent_menu_id = parent_menu_id;
	}
	public Integer getTingkat() {
		return tingkat;
	}
	public void setTingkat(Integer tingkat) {
		this.tingkat = tingkat;
	}
	public Integer getUrutan() {
		return urutan;
	}
	public void setUrutan(Integer urutan) {
		this.urutan = urutan;
	}

	
}
