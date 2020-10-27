/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: CellDetailConfVO.java
 * Program Name   		: CellDetailConfVO
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

import com.ekalife.utils.easypoi.interfaces.ExpressionInt;

public class CellDetailConfVO
{
    int columnNo;
    String dbFieldName;
    ExpressionInt expression;

    public CellDetailConfVO()
    {
    }


	public CellDetailConfVO(int columnNo, String dbFieldName) {
		super();
		this.columnNo = columnNo;
		this.dbFieldName = dbFieldName;
	}

    public CellDetailConfVO(int columnNo, String dbFieldName, ExpressionInt expression ) {
		super();
		this.columnNo = columnNo;
		this.dbFieldName = dbFieldName;
        this.expression = expression;
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

    public ExpressionInt getExpression()
    {
        return expression;
    }

    public void setExpression( ExpressionInt expression )
    {
        this.expression = expression;
    }
}
