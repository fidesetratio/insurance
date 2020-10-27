/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: HeaderFooterConfVO.java
 * Program Name   		: HeaderFooterConfVO
 * Description         	: 
 * Environment      	: Java  1.4.2
 * Author               : Samuel Baktiar
 * Version              : 
 * Creation Date    	: Apr 9, 2007 1:00:00 PM
 *
 * Update history   Re-fix date      Person in charge      Description
 *
 * Copyright(C) 2007-TOYOTA Motor Manufacturing Indonesia. All Rights Reserved.
 ***********************************************************************/

package com.ekalife.utils.easypoi.vo;

public class HeaderFooterConfVO
{
	private int sheetNo;
	private String headerLeftContent;
	private String headerCenterContent;
	private String headerRightContent;
	private String footerLeftContent;
	private String footerCenterContent;
	private String footerRightContent;
	
	
	public HeaderFooterConfVO()
	{
	}
	
	/**
	 * @return Returns the footerCenterContent.
	 */
	public String getFooterCenterContent() {
		return footerCenterContent;
	}
	/**
	 * @param footerCenterContent The footerCenterContent to set.
	 */
	public void setFooterCenterContent(String footerCenterContent) {
		this.footerCenterContent = footerCenterContent;
	}
	/**
	 * @return Returns the footerLeftContent.
	 */
	public String getFooterLeftContent() {
		return footerLeftContent;
	}
	/**
	 * @param footerLeftContent The footerLeftContent to set.
	 */
	public void setFooterLeftContent(String footerLeftContent) {
		this.footerLeftContent = footerLeftContent;
	}
	/**
	 * @return Returns the footerRightContent.
	 */
	public String getFooterRightContent() {
		return footerRightContent;
	}
	/**
	 * @param footerRightContent The footerRightContent to set.
	 */
	public void setFooterRightContent(String footerRightContent) {
		this.footerRightContent = footerRightContent;
	}
	/**
	 * @return Returns the headerCenterContent.
	 */
	public String getHeaderCenterContent() {
		return headerCenterContent;
	}
	/**
	 * @param headerCenterContent The headerCenterContent to set.
	 */
	public void setHeaderCenterContent(String headerCenterContent) {
		this.headerCenterContent = headerCenterContent;
	}
	/**
	 * @return Returns the headerLeftContent.
	 */
	public String getHeaderLeftContent() {
		return headerLeftContent;
	}
	/**
	 * @param headerLeftContent The headerLeftContent to set.
	 */
	public void setHeaderLeftContent(String headerLeftContent) {
		this.headerLeftContent = headerLeftContent;
	}
	/**
	 * @return Returns the headerRightContent.
	 */
	public String getHeaderRightContent() {
		return headerRightContent;
	}
	/**
	 * @param headerRightContent The headerRightContent to set.
	 */
	public void setHeaderRightContent(String headerRightContent) {
		this.headerRightContent = headerRightContent;
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
