package com.ekalife.elions.web.refund.vo;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RincianPolisVO
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 10, 2008 2:42:34 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import com.ekalife.utils.Common;

public class RincianPolisVO implements Serializable
{
	private static final long serialVersionUID = 5750985433789158917L;
	private ArrayList<HashMap<String, String>> setoranMapList;
    private ArrayList<HashMap<String, String>> penarikanUlinkMapList;
    private ArrayList<HashMap<String, String>> biayaUlinkMapList;
    private ArrayList<HashMap<String, String>> biayaStandarMapList;
    private ArrayList<HashMap<String, String>> summaryMapList;
    private ArrayList<HashMap<String, String>> allDescrAndJumlah;
    private ArrayList<HashMap<String, String>> noPreAndVoucher;
    private ArrayList< SetoranPremiDbVO > setoranPokokAndTopUp;
    private String spajAlreadyInDbOrNot;
    private String noVoucher;
    private BigDecimal total_setor;

    private BigDecimal totalPremiDikembalikan;


	public BigDecimal getTotal_setor() {
		return total_setor;
	}

	public void setTotal_setor(BigDecimal totalSetor) {
		total_setor = totalSetor;
	}

	public ArrayList<SetoranPremiDbVO> getSetoranPokokAndTopUp() {
		return setoranPokokAndTopUp;
	}

	public void setSetoranPokokAndTopUp(ArrayList<SetoranPremiDbVO> setoranPokokAndTopUp) {
		this.setoranPokokAndTopUp = Common.serializableList(setoranPokokAndTopUp);
	}

	public ArrayList<HashMap<String, String>> getSetoranMapList()
    {
        return setoranMapList;
    }

    public void setSetoranMapList( ArrayList<HashMap<String, String>> setoranMapList )
    {
        this.setoranMapList = Common.serializableList(setoranMapList);
    }

    public ArrayList<HashMap<String, String>> getPenarikanUlinkMapList()
    {
        return penarikanUlinkMapList;
    }

    public void setPenarikanUlinkMapList( ArrayList<HashMap<String, String>> penarikanUlinkMapList )
    {
        this.penarikanUlinkMapList = Common.serializableList(penarikanUlinkMapList);
    }

    public ArrayList<HashMap<String, String>> getBiayaUlinkMapList()
    {
        return biayaUlinkMapList;
    }

    public void setBiayaUlinkMapList( ArrayList<HashMap<String, String>> biayaUlinkMapList )
    {
        this.biayaUlinkMapList = Common.serializableList(biayaUlinkMapList);
    }

    public ArrayList<HashMap<String, String>> getBiayaStandarList()
    {
        return biayaStandarMapList;
    }

    public void setBiayaStandarList( ArrayList<HashMap<String, String>> biayaStandarList )
    {
        this.biayaStandarMapList = Common.serializableList(biayaStandarList);
    }

    public ArrayList<HashMap<String, String>> getSummaryMapList()
    {
        return summaryMapList;
    }

    public void setSummaryMapList( ArrayList<HashMap<String, String>> summaryMapList )
    {
        this.summaryMapList = Common.serializableList(summaryMapList);
    }

    public BigDecimal getTotalPremiDikembalikan()
    {
        return totalPremiDikembalikan;
    }

    public void setTotalPremiDikembalikan( BigDecimal totalPremiDikembalikan )
    {
        this.totalPremiDikembalikan = totalPremiDikembalikan;
    }

	public ArrayList<HashMap<String, String>> getAllDescrAndJumlah() {
		return allDescrAndJumlah;
	}

	public void setAllDescrAndJumlah(ArrayList<HashMap<String, String>> allDescrAndJumlah) {
		this.allDescrAndJumlah = Common.serializableList(allDescrAndJumlah);
	}

	public String getSpajAlreadyInDbOrNot() {
		return spajAlreadyInDbOrNot;
	}

	public void setSpajAlreadyInDbOrNot(String spajAlreadyInDbOrNot) {
		this.spajAlreadyInDbOrNot = spajAlreadyInDbOrNot;
	}

	public ArrayList<HashMap<String, String>> getNoPreAndVoucher() {
		return noPreAndVoucher;
	}

	public void setNoPreAndVoucher(ArrayList<HashMap<String, String>> noPreAndVoucher) {
		this.noPreAndVoucher = Common.serializableList(noPreAndVoucher);
	}

	public String getNoVoucher() {
		return noVoucher;
	}

	public void setNoVoucher(String noVoucher) {
		this.noVoucher = noVoucher;
	}
}
