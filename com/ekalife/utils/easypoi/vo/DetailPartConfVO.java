/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: DetailPartConfVO.java
 * Program Name   		: DetailPartConfVO
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

public class DetailPartConfVO
{
	private boolean isModeRemoveDuplicateRow;
	private boolean isModeNumbering;
	private boolean isModeInsertRow;
	private boolean isModeGotoNewSheetIfRowTooLarge;
	private int maxRowsBeforeThrowError;
	private int maxRowsBeforeAutomaticlyGotoNewSheet;
	private int numberingColumn;
	private int detailStartRow;
	private int sheetNo;
	private List cellDetailConfList;
	private String query;
	private List params;
    
	
	/**
	 * @param isModeRemoveDuplicateRow
	 * @param isModeNumbering
	 * @param isModeInsertRow
	 */
	public DetailPartConfVO()
	{
		this.isModeRemoveDuplicateRow = false;
		this.isModeNumbering = false;
		this.isModeInsertRow = false;
		this.isModeGotoNewSheetIfRowTooLarge = false;
		this.maxRowsBeforeThrowError = 6000;
		this.maxRowsBeforeAutomaticlyGotoNewSheet = 6000;
	}
	
	
	
	/**
	 * @return Returns the isModeGotoNewSheetIfRowTooLarge.
	 */
	public boolean isModeGotoNewSheetIfRowTooLarge() {
		return isModeGotoNewSheetIfRowTooLarge;
	}
	/**
	 * @param isModeGotoNewSheetIfRowTooLarge The isModeGotoNewSheetIfRowTooLarge to set.
	 */
	public void setModeGotoNewSheetIfRowTooLarge(
			boolean isModeGotoNewSheetIfRowTooLarge) {
		this.isModeGotoNewSheetIfRowTooLarge = isModeGotoNewSheetIfRowTooLarge;
	}
	/**
	 * @return Returns the cellDetailConfList.
	 */
	public List getCellDetailConfList() {
		return cellDetailConfList;
	}
	/**
	 * @param cellDetailConfList The cellDetailConfList to set.
	 */
	public void setCellDetailConfList(List cellDetailConfList) {
		this.cellDetailConfList = cellDetailConfList;
	}
	/**
	 * @return Returns the detailStartRow.
	 */
	public int getDetailStartRow() {
		return detailStartRow;
	}
	/**
	 * @param detailStartRow The detailStartRow to set.
	 */
	public void setDetailStartRow(int detailStartRow) {
		this.detailStartRow = detailStartRow;
	}
	/**
	 * @return Returns the isModeNumbering.
	 */
	public boolean isModeNumbering() {
		return isModeNumbering;
	}
	/**
	 * @param isModeNumbering The isModeNumbering to set.
	 */
	public void setModeNumbering(boolean isModeNumbering) {
		this.isModeNumbering = isModeNumbering;
	}
	/**
	 * @return Returns the isModeRemoveDuplicateRow.
	 */
	public boolean isModeRemoveDuplicateRow() {
		return isModeRemoveDuplicateRow;
	}
	/**
	 * @param isModeRemoveDuplicateRow The isModeRemoveDuplicateRow to set.
	 */
	public void setModeRemoveDuplicateRow(boolean isModeRemoveDuplicateRow) {
		this.isModeRemoveDuplicateRow = isModeRemoveDuplicateRow;
	}
	/**
	 * @return Returns the maxRow.
	 */
	public int getMaxRowsBeforeThrowError() {
		return maxRowsBeforeThrowError;
	}
	/**
	 * @param maxRow The maxRow to set.
	 */
	public void setMaxRowsBeforeThrowError(int maxRowsBeforeThrowError) {
		this.maxRowsBeforeThrowError = maxRowsBeforeThrowError;
	}
	/**
	 * @return Returns the numberingColumn.
	 */
	public int getNumberingColumn() {
		return numberingColumn;
	}
	/**
	 * @param numberingColumn The numberingColumn to set.
	 */
	public void setNumberingColumn(int numberingColumn) {
		this.numberingColumn = numberingColumn;
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
	/**
	 * @return Returns the isModeInsertRow.
	 */
	public boolean isModeInsertRow() {
		return isModeInsertRow;
	}
	/**
	 * @param isModeInsertRow The isModeInsertRow to set.
	 */
	public void setModeInsertRow(boolean isModeInsertRow) {
		this.isModeInsertRow = isModeInsertRow;
	}
	
	
	/**
	 * @return Returns the maxRowsBeforeAutomaticlyGotoNewSheet.
	 */
	public int getMaxRowsBeforeAutomaticlyGotoNewSheet() {
		return maxRowsBeforeAutomaticlyGotoNewSheet;
	}
	/**
	 * @param maxRowsBeforeAutomaticlyGotoNewSheet The maxRowsBeforeAutomaticlyGotoNewSheet to set.
	 */
	public void setMaxRowsBeforeAutomaticlyGotoNewSheet(
			int maxRowsBeforeAutomaticlyGotoNewSheet) {
		this.maxRowsBeforeAutomaticlyGotoNewSheet = maxRowsBeforeAutomaticlyGotoNewSheet;
	}
}
