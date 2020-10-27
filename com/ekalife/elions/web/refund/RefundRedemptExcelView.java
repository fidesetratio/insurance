package com.ekalife.elions.web.refund;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.web.refund.vo.PolicyInfoVO;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Bancass
 * Function Id         	: 
 * Program Name   		: Cebc01030202ExcelView
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Nov 15, 2007 3:47:35 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import com.ekalife.utils.easypoi.createexcel.CreateExcel;
import com.ekalife.utils.easypoi.createexcel.ExportExcelQueryBuilder;
import com.ekalife.utils.easypoi.interfaces.ExpressionInt;
import com.ekalife.utils.easypoi.specific.ExportExcelConfigUtil;
import com.ekalife.utils.easypoi.specific.ExportExcelDbUtil;
import com.ekalife.utils.easypoi.vo.CellDetailConfVO;
import com.ekalife.utils.easypoi.vo.CellSimpleConfVO;
import com.ekalife.utils.easypoi.vo.DetailPartConfVO;
import com.ekalife.utils.easypoi.vo.DetailResultInfoVO;
import com.ekalife.utils.easypoi.vo.ExportExcelConfVO;
import com.ekalife.utils.easypoi.vo.ExportExcelResultInfoVO;
import com.ekalife.utils.easypoi.vo.ParameterizedQueryVO;
import com.ekalife.utils.easypoi.vo.SimplePartConfVO;

/**
 * @author Samuel
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class RefundRedemptExcelView extends AbstractExcelView
{
    private ElionsManager elionsManager;
    private Connection connection;

    public RefundRedemptExcelView( ElionsManager elionsManager, Connection connection )
    {
        this.elionsManager = elionsManager;
        this.connection = connection;
    }

    protected void buildExcelDocument( Map model, HSSFWorkbook workBook, HttpServletRequest request, HttpServletResponse response )
            throws SQLException, IOException
    {
        ExportExcelDbUtil dbUtil = new ExportExcelDbUtil( elionsManager, connection );
        ExportExcelConfigUtil configUtil = new ExportExcelConfigUtil( dbUtil );
        CreateExcel createExcel = new CreateExcel();
        ExportExcelConfVO exportExcelConfVO = new ExportExcelConfVO();
        CellDetailConfVO cellDetailConfVO = new CellDetailConfVO();

        String spaj = request.getParameter( "spaj" );
        String alasan = request.getParameter( "alasan" );
        String invest = request.getParameter( "invest" );
        alasan = alasan.replace("_", " ");
        String templateFileName = "Redempt_report.xls";
        String stFileName = "REDEMPT_" + spaj + configUtil.getTimeStamp() + ".xls";
        String templateDirPath = request.getSession().getServletContext().getRealPath( configUtil.getTemplateDirectoryPath() );
        String templatePath = templateDirPath + "\\" + templateFileName;

        // check whether template dir and template file exist
        if( configUtil.isExistDirectory( templateDirPath ) )
        {
            if( !configUtil.isExistFile( templatePath ) )
            {
                throw new RuntimeException( ": Excel file template not found" );
            }
        }
        else
        {
            throw new RuntimeException( ": Folder of excel template not found" );
        }

        exportExcelConfVO.setTemplateFilePath( templatePath );

        ParameterizedQueryVO queryVO = buildQuery( spaj );

        DetailPartConfVO detailPartConfVO = new DetailPartConfVO();
        detailPartConfVO.setMaxRowsBeforeThrowError( 10000 );
        detailPartConfVO.setModeNumbering( false );
        detailPartConfVO.setModeRemoveDuplicateRow( false );
        detailPartConfVO.setNumberingColumn( 1 );
        detailPartConfVO.setDetailStartRow( 7 );
        detailPartConfVO.setSheetNo( 1 );
        detailPartConfVO.setParams( queryVO.getParams() );
        detailPartConfVO.setQuery( queryVO.getQuery() );
        detailPartConfVO.setCellDetailConfList( buildCellDetailConfList() );

        workBook = createExcel.getTemplate( exportExcelConfVO );

        // setting header example
//        HeaderFooterConfVO headerFooterConfVO = new HeaderFooterConfVO();
//        headerFooterConfVO.setSheetNo( 1 );
//        headerFooterConfVO.setHeaderLeftContent( "Printed: " + dbUtil.getCompleteCurrentDateInLanguage() );

        List simplePartConfVOList = new ArrayList();
        List detailPartConfVOList = new ArrayList();

        detailPartConfVOList.add( detailPartConfVO );
        exportExcelConfVO.setDetailPartConfVOList( detailPartConfVOList );

        SimplePartConfVO simplePartConfVO =  new SimplePartConfVO();
		simplePartConfVO.setSheetNo( 1 );
		simplePartConfVO.setQuery( "" );

        List cellSimpleConfVOList = new ArrayList();
        CellSimpleConfVO cellSimpleConfVO;
        cellSimpleConfVO = new CellSimpleConfVO( 1, 1, "Printed: " + dbUtil.getCompleteCurrentDateInLanguage() );
    	cellSimpleConfVOList.add( cellSimpleConfVO );

        simplePartConfVO.setCellSimpleConfVOList( cellSimpleConfVOList );
        simplePartConfVOList.add( simplePartConfVO );
        exportExcelConfVO.setSimplePartConfVOList( simplePartConfVOList );

        ExportExcelResultInfoVO resultInfoVO;
        resultInfoVO = createExcel.generateAllToWorkBook( workBook, exportExcelConfVO, dbUtil );

        // add last summary afer detail
        PolicyInfoVO policyInfoVO = elionsManager.selectPolicyInfoBySpaj( spaj );
        String bankName = "";
        String bankAccount = "";
        
        if( RefundConst.KURS_DOLLAR.equals( policyInfoVO.getLkuId() ) )
        {
        	bankName = "Bank Niaga cab. Mangga Dua Mall";
        	bankAccount = "A/C. 022-02-12690-005";
        }
        else if( RefundConst.KURS_RUPIAH.equals( policyInfoVO.getLkuId() ) )
        {
        	if( policyInfoVO.getLsbsLineBus() != null && policyInfoVO.getLsbsLineBus() == 3) // ( policyInfoVO.getLsbsLineBus() == 3 ) ==> adalah syariah
        	{
        		bankName = "Bank Niaga Syariah cab. Sudirman";
        		bankAccount = "A/C. 520-01-00112-001";
        	}
        	else // selain syariah
        	{
        		if ( invest != null && "excellink".equals( invest ) )
        		{
            		bankName = "Bank Niaga Mangga Dua Mall";
            		bankAccount = "A/C.220111748002";
        		}
        		else if ( invest != null && "ekalink".equals( invest ) )
        		{
            		bankName = "BII cab. Wisma Eka Jiwa";
            		bankAccount = "A/C. 2001-501193";
        		}

        	}
        }
        cellSimpleConfVOList.clear();
        int totalRow = ( ( DetailResultInfoVO ) resultInfoVO.getDetailResultInfoVOList().get( 0 ) ).getNoOfRecord();
        logger.info( "*-*-*-* totalRow = " + totalRow );
        String nb = "Note: Penarikan dilakukan karena "+ alasan +". hasil penarikan polis tsb diatas, ditransfer ke rek. PT.Asuransi Jiwa Sinarmas :";
        
        cellSimpleConfVO = new CellSimpleConfVO( 1, 7 + totalRow + 2, nb );
        cellSimpleConfVOList.add(  cellSimpleConfVO );

        cellSimpleConfVO = new CellSimpleConfVO( 1, 7 + totalRow + 3, bankName );
        cellSimpleConfVOList.add(  cellSimpleConfVO );
        
        cellSimpleConfVO = new CellSimpleConfVO( 1, 7 + totalRow + 4, bankAccount );
        cellSimpleConfVOList.add(  cellSimpleConfVO );
        
        createExcel.generateSimpleToWorkBook( workBook, simplePartConfVO, dbUtil );
        
        logger.debug( "Start Downloading to Excel File..." );
        response.setContentType( "application/vnd.ms-excel" );
        response.setHeader( "Content-Disposition", "inline;filename=" + stFileName );
        ServletOutputStream servletOutputStream = response.getOutputStream();
        workBook.write( servletOutputStream );
        servletOutputStream.flush();
    }

    public static ParameterizedQueryVO buildQuery( String spaj)
    {
        ParameterizedQueryVO queryVO;

        StringBuffer selectClause = new StringBuffer( "" );
        StringBuffer whereClause = new StringBuffer( "" );
        StringBuffer orderClause = new StringBuffer( "" );

        // select clause
        selectClause.append( "       SELECT rownum as row_no,                               			 	" );
        selectClause.append( "       lji_id,                                               		" );
        selectClause.append( "       lji_invest,                                              	" );
        selectClause.append( "       jumlah_unit,                                               " );
        selectClause.append( "       mspo_policy_no,                                            " );
        selectClause.append( "      'Penarikan' as penarikan                                    " );
        selectClause.append( "       FROM( SELECT c.mspo_policy_no,                             " );
        selectClause.append( "       a.lji_id,                									" );
        selectClause.append( "       b.lji_invest,     											" );
        selectClause.append( "       sum( mtu_unit ) as jumlah_unit     						" );
        selectClause.append( "       FROM eka.mst_trans_ulink a,                                " );
        selectClause.append( "       eka.lst_jenis_invest b,                                    " );
        selectClause.append( "       eka.mst_policy c                                           " );

        // where clause
        whereClause.append( "  WHERE a.reg_spaj = '"+ spaj +"'" );
        whereClause.append( " AND a.lji_id = b.lji_id " );
        whereClause.append( " AND a.reg_spaj = c.reg_spaj " );
        whereClause.append( " GROUP BY a.lji_id, b.lji_invest, c.mspo_policy_no )" );

        // order clause
        orderClause.append( " ORDER BY row_no " );
        // used it for filter
        TreeMap filterMap = new TreeMap();
//        filterMap.put( "A.BUYER_CD", formBean.getName() );
//        filterMap.put( "A.BUYER_CD", formBean.getBuyerCode() );

        queryVO = ExportExcelQueryBuilder.constructQuery( selectClause, whereClause, orderClause, filterMap );
        return queryVO;
    }

    public static List buildCellDetailConfList()
    {
        List result = new ArrayList();
        CellDetailConfVO confVO;
        ExpressionInt expression;
        confVO = new CellDetailConfVO( 1, "row_no" ); result.add( confVO );
        confVO = new CellDetailConfVO( 2, "lji_invest" ); result.add( confVO );
        confVO = new CellDetailConfVO( 3, "mspo_policy_no" ); result.add( confVO );
        confVO = new CellDetailConfVO( 4, "penarikan" ); result.add( confVO );
        confVO = new CellDetailConfVO( 5, "jumlah_unit" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 7, "bundle" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 8, "unbundle" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 9, "investment" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 10, "sum_jml_polis" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 11, "sum_tot_up" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 12, "sum_tot_premi" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 13, "sum_premium" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 14, "sum_frek_klaim_terima" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 15, "sum_nilai_klaim_terima" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 16, "sum_frek_klaim_ga" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 17, "sum_nilai_klaim_ga" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 18, "sum_nav" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 19, "sum_nav_per_unit" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 20, "sum_saham" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 21, "sum_obligasi" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 22, "sum_pasar_uang" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 23, "sum_reksadana" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 24, "sum_lain" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 25, "fee_bundle" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 26, "fee_unbundle" ); result.add( confVO );
//        confVO = new CellDetailConfVO( 27, "fee_investment" ); result.add( confVO );

        return result;
    }

    public static List buildCellSimpleConfList()
    {
        return null;
    }
}


