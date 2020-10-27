package com.ekalife.elions.model.refund;

import java.io.Serializable;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: CabangForm
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Jul 16, 2009 1:56:45 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/


public class CabangForm implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private CabangEditForm editForm;
    private String theEvent;

    public CabangEditForm getEditForm() {
        return editForm;
    }

    public void setEditForm(CabangEditForm editForm) {
        this.editForm = editForm;
    }

    public String getTheEvent() {
        return theEvent;
    }

    public void setTheEvent(String theEvent) {
        this.theEvent = theEvent;
    }
}