/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: CellSimpleConfVO.java
 * Program Name   		: CellSimpleConfVO
 * Description         	: 
 * Environment      	: Java  1.4.2
 * Author               : Samuel Baktiar
 * Version              : 
 * Creation Date    	: Apr 3, 2007 1:00:00 PM
 *
 * Update history   Re-fix date      Person in charge      Description
 *
 * Copyright(C) 2007-TOYOTA Motor Manufacturing Indonesia. All Rights Reserved.
 ***********************************************************************/


package com.ekalife.utils.easypoi.vo;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

public class CellSimpleConfVO
{
	boolean isModeUseDb;
	int rowNo;
	int columnNo;
    String dbFieldName;
    String label;
    String defaultValue;
    HSSFCellStyle style;
	
    /**
	 * @param isModeUseDb
	 * @param rowNo
	 * @param columnNo
	 * @param defaultValue
	 */
	public CellSimpleConfVO(int columnNo, int rowNo,
			String defaultValue, String dbFieldName) {
		super();
		this.isModeUseDb = true;
		this.rowNo = rowNo;
		this.columnNo = columnNo;
		this.defaultValue = defaultValue;
		this.dbFieldName = dbFieldName;
	}
	
	
	
	/**
	 * @param isModeUseDb
	 * @param rowNo
	 * @param columnNo
	 * @param dbFieldName
	 */
	public CellSimpleConfVO( int columnNo, int rowNo,
			String defaultValue) {
		super();
		this.setModeUseDb( false );
		this.rowNo = rowNo;
		this.columnNo = columnNo;
		this.defaultValue = defaultValue;
	}
	public CellSimpleConfVO()
	{
	}
	
	/**
	 * @return Returns the columnNo.
	 */
	public int getColumnNo() {
		return columnNo;
	}
	/**
	 * @param columnNo The columnNo to set.
	 */
	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
	}
	/**
	 * @return Returns the dbFieldName.
	 */
	public String getDbFieldName() {
		return dbFieldName;
	}
	/**
	 * @param dbFieldName The dbFieldName to set.
	 */
	public void setDbFieldName(String dbFieldName) {
		this.dbFieldName = dbFieldName;
	}
	/**
	 * @return Returns the defaultValue.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue The defaultValue to set.
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @return Returns the isModeUseDb.
	 */
	public boolean isModeUseDb() {
		return isModeUseDb;
	}
	/**
	 * @param isModeUseDb The isModeUseDb to set.
	 */
	public void setModeUseDb(boolean isModeUseDb) {
		this.isModeUseDb = isModeUseDb;
	}
	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return Returns the rowNo.
	 */
	public int getRowNo() {
		return rowNo;
	}
	/**
	 * @param rowNo The rowNo to set.
	 */
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	/**
	 * @return Returns the style.
	 */
	public HSSFCellStyle getStyle() {
		return style;
	}
	/**
	 * @param style The style to set.
	 */
	public void setStyle(HSSFCellStyle style) {
		this.style = style;
	}
	
}
