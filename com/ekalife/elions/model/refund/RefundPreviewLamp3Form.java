package com.ekalife.elions.model.refund;

import java.io.Serializable;

import com.ekalife.elions.web.common.DownloadFormInterface;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundPreviewLamp3Form
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 30, 2008 8:48:30 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

public class RefundPreviewLamp3Form implements DownloadFormInterface, Serializable {

	private static final long serialVersionUID = -6152688574794052644L;

	private String downloadFlag;
	private String buttonPreviewRedemptDisplay;
	private String buttonBatalkanSpajIsDisabled;
	private String buttonSaveDraftIsDisabled;
	private String buttonViewAttachmentIsDisabled;
	private String buttonAccBatalSpajIsDisabled;


	public String getDownloadFlag() {
        return downloadFlag;
    }

    public void setDownloadFlag( String downloadFlag ) {
        this.downloadFlag = downloadFlag;
    }

    public String getButtonPreviewRedemptDisplay() {
		return buttonPreviewRedemptDisplay;
	}

	public void setButtonPreviewRedemptDisplay(String buttonPreviewRedemptDisplay) {
		this.buttonPreviewRedemptDisplay = buttonPreviewRedemptDisplay;
	}
    public String getButtonBatalkanSpajIsDisabled() {
        return buttonBatalkanSpajIsDisabled;
    }

    public void setButtonBatalkanSpajIsDisabled( String buttonBatalkanSpajIsDisabled ) {
        this.buttonBatalkanSpajIsDisabled = buttonBatalkanSpajIsDisabled;
    }

    public String getButtonSaveDraftIsDisabled() {
        return buttonSaveDraftIsDisabled;
    }

    public void setButtonSaveDraftIsDisabled( String buttonSaveDraftIsDisabled ) {
        this.buttonSaveDraftIsDisabled = buttonSaveDraftIsDisabled;
    }

    public String getButtonViewAttachmentIsDisabled() {
        return buttonViewAttachmentIsDisabled;
    }

    public void setButtonViewAttachmentIsDisabled( String buttonViewAttachmentIsDisabled ) {
        this.buttonViewAttachmentIsDisabled = buttonViewAttachmentIsDisabled;
    }
    
	public String getButtonAccBatalSpajIsDisabled() {
		return buttonAccBatalSpajIsDisabled;
	}

	public void setButtonAccBatalSpajIsDisabled(String buttonAccBatalSpajIsDisabled) {
		this.buttonAccBatalSpajIsDisabled = buttonAccBatalSpajIsDisabled;
	}

}
