/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: CellVO.java
 * Program Name   		: CellVO
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

public class CellVO
{
	private int columnNo;
	private int rowNo;
	private Object content;
	private HSSFCellStyle style;
	
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
	 * @return Returns the content.
	 */
	public Object getContent() {
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent(Object content) {
		this.content = content;
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
