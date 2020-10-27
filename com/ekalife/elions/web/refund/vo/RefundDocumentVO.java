package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundDocumentVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 9, 2008 11:23:53 AM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ekalife.utils.Common;

public class RefundDocumentVO implements Serializable
{
	private static final long serialVersionUID = -5406151192744371401L;
	private String downloadUrlSession;
    private String jasperFile;
    private HashMap<String, Object> params;

    public String getDownloadUrlSession()
    {
        return downloadUrlSession;
    }

    public void setDownloadUrlSession( String downloadUrlSession )
    {
        this.downloadUrlSession = downloadUrlSession;
    }

    public String getJasperFile()
    {
        return jasperFile;
    }

    public void setJasperFile( String jasperFile )
    {
        this.jasperFile = jasperFile;
    }

    public HashMap<String, Object> getParams()
    {
        return params;
    }

    public void setParams( HashMap<String, Object> params )
    {
        this.params = Common.serializableMap(params);
    }
}
