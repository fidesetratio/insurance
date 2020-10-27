/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: SimplePartConfVO.java
 * Program Name   		: SimplePartConfVO
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

import java.util.List;

public class SimplePartConfVO
{
	private int sheetNo;
	private List cellSimpleConfVOList;
	private String query;
	private List params;
	
	/**
	 * @return Returns the cellSimpleConfVOList.
	 */
	public List getCellSimpleConfVOList() {
		return cellSimpleConfVOList;
	}
	/**
	 * @param cellSimpleConfVOList The cellSimpleConfVOList to set.
	 */
	public void setCellSimpleConfVOList(List cellSimpleConfVOList) {
		this.cellSimpleConfVOList = cellSimpleConfVOList;
	}
	/**
	 * @return Returns the params.
	 */
	public List getParams() {
		return params;
	}
	/**
	 * @param params The params to set.
	 */
	public void setParams(List params) {
		this.params = params;
	}
	/**
	 * @return Returns the query.
	 */
	public String getQuery() {
		return query;
	}
	/**
	 * @param query The query to set.
	 */
	public void setQuery(String query) {
		this.query = query;
	}
	/**
	 * @return Returns the sheetNo.
	 */
	public int getSheetNo() {
		return sheetNo;
	}
	/**
	 * @param sheetNo The sheetNo to set.
	 */
	public void setSheetNo(int sheetNo) {
		this.sheetNo = sheetNo;
	}
}
