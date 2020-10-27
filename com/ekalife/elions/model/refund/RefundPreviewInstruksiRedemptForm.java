package com.ekalife.elions.model.refund;

import java.io.Serializable;

import com.ekalife.elions.web.common.DownloadFormInterface;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundPreviewInstruksiRedemptForm
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 30, 2008 9:38:38 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

public class RefundPreviewInstruksiRedemptForm implements DownloadFormInterface, Serializable{
	
	private static final long serialVersionUID = 3452652303369769930L;
	private String downloadFlag;
	private String buttonViewAttachmentDisplay;
	private String buttonViewAttachmentExcelDisplay;
	private String buttonViewAttachmentIsDisabled;
	private String buttonViewAttachmentExcelIsDisabled;
	private String buttonKirimDisplay;
	private String buttonKirimIsDisabled;
	private String namaUnderwriter;
	private Integer currentPage;

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getNamaUnderwriter() {
		return namaUnderwriter;
	}

	public void setNamaUnderwriter(String namaUnderwriter) {
		this.namaUnderwriter = namaUnderwriter;
	}

	public String getDownloadFlag()
    {
        return downloadFlag;
    }

    public void setDownloadFlag( String downloadFlag )
    {
        this.downloadFlag = downloadFlag;
    }

    public String getButtonViewAttachmentDisplay()
    {
        return buttonViewAttachmentDisplay;
    }

    public void setButtonViewAttachmentDisplay( String buttonViewAttachmentDisplay )
    {
        this.buttonViewAttachmentDisplay = buttonViewAttachmentDisplay;
    }

    public String getButtonViewAttachmentIsDisabled()
    {
        return buttonViewAttachmentIsDisabled;
    }

    public void setButtonViewAttachmentIsDisabled( String buttonViewAttachmentIsDisabled )
    {
        this.buttonViewAttachmentIsDisabled = buttonViewAttachmentIsDisabled;
    }

    public String getButtonKirimDisplay()
    {
        return buttonKirimDisplay;
    }

    public void setButtonKirimDisplay( String buttonKirimDisplay )
    {
        this.buttonKirimDisplay = buttonKirimDisplay;
    }

    public String getButtonKirimIsDisabled()
    {
        return buttonKirimIsDisabled;
    }

    public void setButtonKirimIsDisabled( String buttonKirimIsDisabled )
    {
        this.buttonKirimIsDisabled = buttonKirimIsDisabled;
    }

	public String getButtonViewAttachmentExcelDisplay() {
		return buttonViewAttachmentExcelDisplay;
	}

	public void setButtonViewAttachmentExcelDisplay(
			String buttonViewAttachmentExcelDisplay) {
		this.buttonViewAttachmentExcelDisplay = buttonViewAttachmentExcelDisplay;
	}

	public String getButtonViewAttachmentExcelIsDisabled() {
		return buttonViewAttachmentExcelIsDisabled;
	}

	public void setButtonViewAttachmentExcelIsDisabled(
			String buttonViewAttachmentExcelIsDisabled) {
		this.buttonViewAttachmentExcelIsDisabled = buttonViewAttachmentExcelIsDisabled;
	}
}
