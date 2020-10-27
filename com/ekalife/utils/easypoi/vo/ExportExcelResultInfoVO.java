/*
 * Created on Apr 30, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ekalife.utils.easypoi.vo;
import java.util.List;

/**
 * @author Samuel
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExportExcelResultInfoVO
{
	List detailResultInfoVOList;
	List simpleResultInfoVOList;
	
	/**
	 * @return Returns the detailResultInfoVOList.
	 */
	public List getDetailResultInfoVOList() {
		return detailResultInfoVOList;
	}
	/**
	 * @param detailResultInfoVOList The detailResultInfoVOList to set.
	 */
	public void setDetailResultInfoVOList(List detailResultInfoVOList) {
		this.detailResultInfoVOList = detailResultInfoVOList;
	}
	/**
	 * @return Returns the simpleResultInfoVOList.
	 */
	public List getSimpleResultInfoVOList() {
		return simpleResultInfoVOList;
	}
	/**
	 * @param simpleResultInfoVOList The simpleResultInfoVOList to set.
	 */
	public void setSimpleResultInfoVOList(List simpleResultInfoVOList) {
		this.simpleResultInfoVOList = simpleResultInfoVOList;
	}
}
