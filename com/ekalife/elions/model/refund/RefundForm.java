package com.ekalife.elions.model.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundForm
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Sep 23, 2008 2:43:58 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;

import com.ekalife.elions.web.refund.vo.RefundDocumentVO;

public class RefundForm implements Serializable{
	
	private static final long serialVersionUID = 3653951758913466876L;
	private RefundLookupForm lookupForm;
    private RefundEditForm editForm;
    private RefundPreviewLamp1Form lamp1Form;
    private RefundPreviewLamp3Form lamp3Form;
    private RefundPreviewInstruksiRedemptForm redemptForm;
    private RefundSignInForm signInForm;
    private String theEvent;
    private RefundDocumentVO refundDocumentVO;

    public RefundLookupForm getLookupForm()
    {
        return lookupForm;
    }

    public void setLookupForm( RefundLookupForm lookupForm )
    {
        this.lookupForm = lookupForm;
    }

    public RefundEditForm getEditForm()
    {
        return editForm;
    }

    public void setEditForm( RefundEditForm editForm )
    {
        this.editForm = editForm;
    }

    public RefundPreviewLamp1Form getLamp1Form()
    {
        return lamp1Form;
    }

    public void setLamp1Form( RefundPreviewLamp1Form lamp1Form )
    {
        this.lamp1Form = lamp1Form;
    }

    public RefundPreviewLamp3Form getLamp3Form()
    {
        return lamp3Form;
    }

    public void setLamp3Form( RefundPreviewLamp3Form lamp3Form )
    {
        this.lamp3Form = lamp3Form;
    }

    public RefundPreviewInstruksiRedemptForm getRedemptForm()
    {
        return redemptForm;
    }

    public void setRedemptForm( RefundPreviewInstruksiRedemptForm redemptForm )
    {
        this.redemptForm = redemptForm;
    }

    public String getTheEvent()
    {
        return theEvent;
    }

    public void setTheEvent( String theEvent )
    {
        this.theEvent = theEvent;
    }

    public RefundDocumentVO getRefundDocumentVO()
    {
        return refundDocumentVO;
    }

    public void setRefundDocumentVO( RefundDocumentVO refundDocumentVO )
    {
        this.refundDocumentVO = refundDocumentVO;
    }

    public RefundSignInForm getSignInForm()
    {
        return signInForm;
    }

    public void setSignInForm( RefundSignInForm signInForm )
    {
        this.signInForm = signInForm;
    }
}
