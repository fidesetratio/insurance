package com.ekalife.elions.reports.bas.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: PemegangPolisVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Jun 26, 2007 4:48:59 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PemegangPolisVO
{
    protected final Log logger = LogFactory.getLog( getClass() );
    private boolean isChecked;
    private String noRegistrasi;
    private String namaPemegangPolis;
    private String inputDate;

    public PemegangPolisVO()
    {
    }

    public PemegangPolisVO( boolean checked, String noRegistrasi, String namaPemegangPolis )
    {
        isChecked = checked;
        this.noRegistrasi = noRegistrasi;
        this.namaPemegangPolis = namaPemegangPolis;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked( boolean checked )
    {
        isChecked = checked;
    }

    public String getNoRegistrasi()
    {
        return noRegistrasi;
    }

    public void setNoRegistrasi( String noRegistrasi )
    {
        this.noRegistrasi = noRegistrasi;
    }

    public String getNamaPemegangPolis()
    {
        return namaPemegangPolis;
    }

    public void setNamaPemegangPolis( String namaPemegangPolis )
    {
        this.namaPemegangPolis = namaPemegangPolis;
    }

    public String getInputDate()
    {
        return inputDate;
    }

    public void setInputDate( String inputDate )
    {
        this.inputDate = inputDate;
    }
}
