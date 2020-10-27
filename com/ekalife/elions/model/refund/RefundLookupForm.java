package com.ekalife.elions.model.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundLookupForm
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Oct 29, 2008 3:08:30 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ekalife.elions.web.common.DownloadFormInterface;
import com.ekalife.utils.Common;

public class RefundLookupForm implements DownloadFormInterface, Serializable{
	
	private static final long serialVersionUID = 5985446290302188773L;
	private String selectedRowCd;
    private String selectedRowNewSpaj;
    private String selectedRowTindakan;
    private String selectedUpdateWho;
    private Integer currentPage;
    private Integer totalOfPages;
    private Integer noOfRowsPerPage;
    private Integer gotoPage;
    private ArrayList<DropDown> noOfRowsPerPageList;
    private ArrayList<DropDown> tindakanFilterList;
    private String dowwloadFlag;

    private String linkAddDisplay;
    private String linkSignInDisplay;
    private String linkDeleteDisplay;
    private String checkBoxDisplay;
    private String batalSpajDisplay;
    private String linkMoveToMstDetRefundLampDisplay;
    private String acceptBatalDisplay;

    private String downloadFlag;

    private String polisFilter;
    private String spajFilter;
    private String posisiFilter;
    private String tindakanFilterCd;
    private String lastUpdateFilter;
    private Date updateFromFilter;
    private Date updateToFilter;
    private Date tglKirimDokFisikFrom;
    private Date tglKirimDokFisikTo;

    private String linkFirstDisplay;
    private String linkLastDisplay;
    private String linkNextDisplay;
    private String linkPrevDisplay;

    public String getSelectedRowCd()
    {
        return selectedRowCd;
    }

    public void setSelectedRowCd( String selectedRowCd )
    {
        this.selectedRowCd = selectedRowCd;
    }

    public Integer getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage( Integer currentPage )
    {
        this.currentPage = currentPage;
    }

    public Integer getTotalOfPages()
    {
        return totalOfPages;
    }

    public void setTotalOfPages( Integer totalOfPages )
    {
        this.totalOfPages = totalOfPages;
    }

    public Integer getNoOfRowsPerPage()
    {
        return noOfRowsPerPage;
    }

    public void setNoOfRowsPerPage( Integer noOfRowsPerPage )
    {
        this.noOfRowsPerPage = noOfRowsPerPage;
    }

    public Integer getGotoPage()
    {
        return gotoPage;
    }

    public void setGotoPage( Integer gotoPage )
    {
        this.gotoPage = gotoPage;
    }

    public ArrayList<DropDown> getNoOfRowsPerPageList()
    {
        return noOfRowsPerPageList;
    }

    public void setNoOfRowsPerPageList( List<DropDown> noOfRowsPerPageList )
    {
        this.noOfRowsPerPageList = Common.serializableList(noOfRowsPerPageList);
    }

    public ArrayList<DropDown> getTindakanFilterList()
    {
        return tindakanFilterList;
    }

    public void setTindakanFilterList( List<DropDown> tindakanFilterList )
    {
        this.tindakanFilterList = Common.serializableList(tindakanFilterList);
    }

    public String getDowwloadFlag()
    {
        return dowwloadFlag;
    }

    public void setDowwloadFlag( String dowwloadFlag )
    {
        this.dowwloadFlag = dowwloadFlag;
    }

    public String getLinkAddDisplay()
    {
        return linkAddDisplay;
    }

    public void setLinkAddDisplay( String linkAddDisplay )
    {
        this.linkAddDisplay = linkAddDisplay;
    }

    public String getLinkDeleteDisplay()
    {
        return linkDeleteDisplay;
    }

    public void setLinkDeleteDisplay( String linkDeleteDisplay )
    {
        this.linkDeleteDisplay = linkDeleteDisplay;
    }

    public String getCheckBoxDisplay()
    {
        return checkBoxDisplay;
    }

    public void setCheckBoxDisplay( String checkBoxDisplay )
    {
        this.checkBoxDisplay = checkBoxDisplay;
    }

    public String getDownloadFlag()
    {
        return downloadFlag;
    }

    public void setDownloadFlag( String downloadFlag )
    {
        this.downloadFlag = downloadFlag;
    }

    public String getSpajFilter()
    {
        return spajFilter;
    }

    public void setSpajFilter( String spajFilter )
    {
        this.spajFilter = spajFilter;
    }

    public String getPosisiFilter()
    {
        return posisiFilter;
    }

    public void setPosisiFilter( String posisiFilter )
    {
        this.posisiFilter = posisiFilter;
    }

    public String getTindakanFilterCd()
    {
        return tindakanFilterCd;
    }

    public void setTindakanFilterCd( String tindakanFilterCd )
    {
        this.tindakanFilterCd = tindakanFilterCd;
    }

    public Date getUpdateFromFilter()
    {
        return updateFromFilter;
    }

    public void setUpdateFromFilter( Date updateFromFilter )
    {
        this.updateFromFilter = updateFromFilter;
    }

    public Date getUpdateToFilter()
    {
        return updateToFilter;
    }

    public void setUpdateToFilter( Date updateToFilter )
    {
        this.updateToFilter = updateToFilter;
    }

    public String getLinkSignInDisplay()
    {
        return linkSignInDisplay;
    }

    public void setLinkSignInDisplay( String linkSignInDisplay )
    {
        this.linkSignInDisplay = linkSignInDisplay;
    }

    public String getLinkFirstDisplay()
    {
        return linkFirstDisplay;
    }

    public void setLinkFirstDisplay( String linkFirstDisplay )
    {
        this.linkFirstDisplay = linkFirstDisplay;
    }

    public String getLinkLastDisplay()
    {
        return linkLastDisplay;
    }

    public void setLinkLastDisplay( String linkLastDisplay )
    {
        this.linkLastDisplay = linkLastDisplay;
    }

    public String getLinkNextDisplay()
    {
        return linkNextDisplay;
    }

    public void setLinkNextDisplay( String linkNextDisplay )
    {
        this.linkNextDisplay = linkNextDisplay;
    }

    public String getLinkPrevDisplay()
    {
        return linkPrevDisplay;
    }

    public void setLinkPrevDisplay( String linkPrevDisplay )
    {
        this.linkPrevDisplay = linkPrevDisplay;
    }

	public String getLastUpdateFilter() {
		return lastUpdateFilter;
	}

	public void setLastUpdateFilter(String lastUpdateFilter) {
		this.lastUpdateFilter = lastUpdateFilter;
	}

	public String getSelectedRowNewSpaj() {
		return selectedRowNewSpaj;
	}

	public void setSelectedRowNewSpaj(String selectedRowNewSpaj) {
		this.selectedRowNewSpaj = selectedRowNewSpaj;
	}

	public String getSelectedRowTindakan() {
		return selectedRowTindakan;
	}

	public void setSelectedRowTindakan(String selectedRowTindakan) {
		this.selectedRowTindakan = selectedRowTindakan;
	}

	public String getSelectedUpdateWho() {
		return selectedUpdateWho;
	}

	public void setSelectedUpdateWho(String selectedUpdateWho) {
		this.selectedUpdateWho = selectedUpdateWho;
	}

	public String getPolisFilter() {
		return polisFilter;
	}

	public void setPolisFilter(String polisFilter) {
		this.polisFilter = polisFilter;
	}

	public Date getTglKirimDokFisikFrom() {
		return tglKirimDokFisikFrom;
	}

	public void setTglKirimDokFisikFrom(Date tglKirimDokFisikFrom) {
		this.tglKirimDokFisikFrom = tglKirimDokFisikFrom;
	}

	public Date getTglKirimDokFisikTo() {
		return tglKirimDokFisikTo;
	}

	public void setTglKirimDokFisikTo(Date tglKirimDokFisikTo) {
		this.tglKirimDokFisikTo = tglKirimDokFisikTo;
	}

	public String getBatalSpajDisplay() {
		return batalSpajDisplay;
	}

	public void setBatalSpajDisplay(String batalSpajDisplay) {
		this.batalSpajDisplay = batalSpajDisplay;
	}

	public String getLinkMoveToMstDetRefundLampDisplay() {
		return linkMoveToMstDetRefundLampDisplay;
	}

	public void setLinkMoveToMstDetRefundLampDisplay(
			String linkMoveToMstDetRefundLampDisplay) {
		this.linkMoveToMstDetRefundLampDisplay = linkMoveToMstDetRefundLampDisplay;
	}

    public String getAcceptBatalDisplay()
    {
        return acceptBatalDisplay;
    }

    public void setAcceptBatalDisplay( String acceptBatalDisplay )
    {
        this.acceptBatalDisplay = acceptBatalDisplay;
    }
}
