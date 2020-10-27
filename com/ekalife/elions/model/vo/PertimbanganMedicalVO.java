package com.ekalife.elions.model.vo;

import java.io.Serializable;

public class PertimbanganMedicalVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
    String flag;
    Integer cd;
    String descr;

    public PertimbanganMedicalVO( Integer cd, String descr )
    {
        this.cd = cd;
        this.descr = descr;
    }

    public String getFlag()
    {
        return flag;
    }

    public void setFlag( String flag )
    {
        this.flag = flag;
    }

    public Integer getCd()
    {
        return cd;
    }

    public void setCd( Integer cd )
    {
        this.cd = cd;
    }

    public String getDescr()
    {
        return descr;
    }

    public void setDescr( String descr )
    {
        this.descr = descr;
    }
}
