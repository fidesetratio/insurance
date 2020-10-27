package com.ekalife.elions.model;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;

public class FileItem extends DropDown implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public String name;
	public String dateModified;
	public String dir;
	public String desc;
	
	public FileItem() {
	}
	
	public FileItem(String name, String dateModified, String dir, String desc) {
		this.name = name;
		this.dateModified = dateModified;
		this.dir = dir;
		this.desc = desc;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDateModified() {
		return dateModified;
	}
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}