/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: ExportExcelConfVO.java
 * Program Name   		: ExportExcelConfVO
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

public class ExportExcelConfVO
{
    String confFilePath;
    String templateFilePath;
    String destinationFilePath;
    List simplePartConfVOList;
    List detailPartConfVOList;
    
    public ExportExcelConfVO()
    {
    }
	
	/**
	 * @return Returns the confFilePath.
	 */
	public String getConfFilePath() {
		return confFilePath;
	}
	/**
	 * @param confFilePath The confFilePath to set.
	 */
	public void setConfFilePath(String confFilePath) {
		this.confFilePath = confFilePath;
	}
	/**
	 * @return Returns the destinationFilePath.
	 */
	public String getDestinationFilePath() {
		return destinationFilePath;
	}
	/**
	 * @param destinationFilePath The destinationFilePath to set.
	 */
	public void setDestinationFilePath(String destinationFilePath) {
		this.destinationFilePath = destinationFilePath;
	}
	/**
	 * @return Returns the detailPartConfVOList.
	 */
	public List getDetailPartConfVOList() {
		return detailPartConfVOList;
	}
	/**
	 * @param detailPartConfVOList The detailPartConfVOList to set.
	 */
	public void setDetailPartConfVOList(List detailPartConfVOList) {
		this.detailPartConfVOList = detailPartConfVOList;
	}
	/**
	 * @return Returns the simplePartConfVOList.
	 */
	public List getSimplePartConfVOList() {
		return simplePartConfVOList;
	}
	/**
	 * @param simplePartConfVOList The simplePartConfVOList to set.
	 */
	public void setSimplePartConfVOList(List simplePartConfVOList) {
		this.simplePartConfVOList = simplePartConfVOList;
	}
	/**
	 * @return Returns the templateFilePath.
	 */
	public String getTemplateFilePath() {
		return templateFilePath;
	}
	/**
	 * @param templateFilePath The templateFilePath to set.
	 */
	public void setTemplateFilePath(String templateFilePath) {
		this.templateFilePath = templateFilePath;
	}
}
