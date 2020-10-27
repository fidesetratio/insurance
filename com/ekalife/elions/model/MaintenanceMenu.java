package com.ekalife.elions.model;

import java.io.Serializable;

public class MaintenanceMenu implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//luar query
	private Integer menu_id;
	private Integer tag2;
	private String proses;
	private Menu menu;
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	public String getProses() {
		return proses;
	}
	public void setProses(String proses) {
		this.proses = proses;
	}
	public Integer getTag2() {
		return tag2;
	}
	public void setTag2(Integer tag2) {
		this.tag2 = tag2;
	}
	public Integer getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(Integer menu_id) {
		this.menu_id = menu_id;
	}
}
