package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundDetDbVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 7, 2008 1:42:51 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.math.BigDecimal;

public class RefundDetDbVO implements Serializable
{
	private static final long serialVersionUID = 2570488719050578804L;

	public enum Tipe
    {
        PREMI( 1 ),
        BIAYA( 2 ),
    	TOPUP( 3 ),
    	WITHDRAW( 4 ),
        MERCHANTFEE( 5 );

    	private final int cd;

        Tipe( int cd )
        {
        	this.cd = cd;
        }

        public int tipe()
        {
        	return cd;
        }

        public static Tipe getTipeByCd( int cd )
        {
        	Tipe result;

        	switch( cd )
        	{
        		case 1: result = PREMI;
        			break;
        		case 2: result = BIAYA;
    				break;
        		case 3: result = TOPUP;
					break;
        		case 4: result = WITHDRAW;
					break;
        		case 5: result = MERCHANTFEE;
					break;
        		default:
        			throw new RuntimeException( "tipeCd di kelas RefundDetDbVO tidak terdaftar" );
        	}

        	return result;
        }

    }
    
    String spajNo;
    Integer itemNo;
    Integer ljbId;
    BigDecimal jumlah;
    String deskripsi;
    Tipe tipe;
    BigDecimal unit;
    String ljiId;
    String lkuId;
//    tipeNo ada getternya

    public Integer getTipeNo()
    {
        return this.tipe.cd;
    }

	public void setTipeNo( Integer tipeNo )
    {
        setTipe( Tipe.getTipeByCd( tipeNo ) );
    }

    public String getLkuId() {
		return lkuId;
	}

	public void setLkuId(String lkuId) {
		this.lkuId = lkuId;
	}

	public String getSpajNo()
    {
        return spajNo;
    }

    public void setSpajNo( String spajNo )
    {
        this.spajNo = spajNo;
    }

    public Integer getItemNo()
    {
        return itemNo;
    }

    public void setItemNo( Integer itemNo )
    {
        this.itemNo = itemNo;
    }

    public Integer getLjbId()
    {
        return ljbId;
    }

    public void setLjbId( Integer ljbId )
    {
        this.ljbId = ljbId;
    }

    public BigDecimal getJumlah()
    {
        return jumlah;
    }

    public void setJumlah( BigDecimal jumlah )
    {
        this.jumlah = jumlah;
    }

    public String getDeskripsi()
    {
        return deskripsi;
    }

    public void setDeskripsi( String deskripsi )
    {
        this.deskripsi = deskripsi;
    }

    public Tipe getTipe()
    {
        return tipe;
    }

    public void setTipe( Tipe tipe )
    {
        this.tipe = tipe;
    }

    public BigDecimal getUnit()
    {
        return unit;
    }

    public void setUnit( BigDecimal unit )
    {
        this.unit = unit;
    }

    public String getLjiId()
    {
        return ljiId;
    }

    public void setLjiId( String ljiId )
    {
        this.ljiId = ljiId;
    }
}
