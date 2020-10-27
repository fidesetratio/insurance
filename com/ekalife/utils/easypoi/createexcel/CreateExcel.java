/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: CreateExcel.java
 * Program Name   		: CreateExcel
 * Description         	: 
 * Environment      	: Java  1.4.2
 * Author               : Samuel Baktiar
 * Version              : 
 * Creation Date    	: Apr 3, 2007 1:00:00 PM
 *
 * Update history   Re-fix date      Person in charge      Description
 * 1.1				27 April 2007	 FID) Irwan Wijaya	   overloading method getTemplate(String templateFilePath)
 * 
 * Copyright(C) 2007-TOYOTA Motor Manufacturing Indonesia. All Rights Reserved.
 ***********************************************************************/


package com.ekalife.utils.easypoi.createexcel;



import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.ekalife.utils.easypoi.vo.*;
import com.ekalife.utils.easypoi.interfaces.ExportExcelDbUtilInt;
import com.ekalife.utils.easypoi.interfaces.ExpressionInt;


public class CreateExcel
{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	public HSSFWorkbook getTemplate( ExportExcelConfVO exportConfVO )
		throws IOException
	{
		logger.debug( "CreateExcel.getTemplate" );
		return CommonExport.getTemplate( exportConfVO.getTemplateFilePath() );
	}

	public HSSFWorkbook getTemplate(String templateFilePath)
		throws IOException {
			return CommonExport.getTemplate(templateFilePath);
	}
	
	public DetailResultInfoVO generateDetailToWorkbook( HSSFWorkbook wb, DetailPartConfVO confVO, ExportExcelDbUtilInt dbUtil )
    throws IOException, SQLException
	{
		logger.debug( "CreateExcel.generateDetailToWorkbook" );
		DetailResultInfoVO detailResultInfoVO = new DetailResultInfoVO();
    	
    	HSSFSheet sheet;
    	int templateSheetIndex = 0;
    	String defSheetName = "SheetName";
    	
        // note: maxRecordsPerSheet = 1 is not allowed;
        int maxRecordsPerSheet = confVO.getMaxRowsBeforeAutomaticlyGotoNewSheet();
        int sheetNo = confVO.getSheetNo() - 1; 
        if( sheetNo < 0 )
		{
			throw( new RuntimeException( "CreateExcel.generateDetailToWorkbook error. Sheet number is not defined." ) );
		}
        
      
        if( confVO.isModeGotoNewSheetIfRowTooLarge() )
        {
        	try
			{
        		wb.cloneSheet( sheetNo );
        		templateSheetIndex = wb.getNumberOfSheets() - 1;
        		wb.setSheetName( templateSheetIndex, "SammyTempo" );
			}
        	catch( RuntimeException e )
			{
        		logger.error("ERROR :", e);
			}
        }
        
        sheet = wb.getSheetAt( sheetNo );
        
    	
        

        int rowNo = confVO.getDetailStartRow();
        if( rowNo > 2 ) rowNo = rowNo - 2;
        
        int intNumberCol = confVO.getNumberingColumn() - 1;
        int intStartRow = rowNo + 1;
        int recNo = 0;
        try
        {

            // will used on remove duplicate mode
            // begin
            List backupRowList = new ArrayList();
            List rowList = new ArrayList();
            int z;
            Object content;
            // end

            CommonExport commonExport = new CommonExport();
            ResultSet rs = dbUtil.executeQuery( confVO.getQuery(), confVO.getParams().toArray() );
            String tempSheetName;
            boolean everIn = false;

            ExpressionInt expression;
            Map<String, Object> backupMap = new HashMap();
            while( rs.next() )
            {
                z = 0;
                recNo++;

                if( confVO.isModeGotoNewSheetIfRowTooLarge() )
                {
                if( ( recNo > maxRecordsPerSheet ) && ( recNo % maxRecordsPerSheet ) == 1 )
                {
                	if( !everIn )
                	{
                		sheetNo++; 
                		everIn = true;
                	}
                	
                	sheetNo++;
                	if( sheetNo + 1 > wb.getNumberOfSheets() )
                	{
                		tempSheetName = defSheetName + Integer.toString( sheetNo );
                		sheet = wb.cloneSheet( templateSheetIndex );
                		wb.setSheetName( sheetNo, tempSheetName );
                	}
                	else
                	{
                		sheet = wb.getSheetAt( sheetNo );
                	}
                	rowNo = confVO.getDetailStartRow();
                    if( rowNo > 2 ) rowNo = rowNo - 2;
                }
                }
                
                rowNo++;
                
                if( confVO.isModeInsertRow() )
                {
                	commonExport.insertRow( sheet, rowNo );
                }
                
                commonExport.copyRow( sheet, intStartRow, rowNo );
                
                if( confVO.isModeRemoveDuplicateRow() )
                {
                    rowList.clear();
                    for( int i = 0; i < backupRowList.size(); i++ )
                    {
                        rowList.add( backupRowList.get( i ) );
                    }
                    backupRowList.clear();
                }

                List lstColumns = confVO.getCellDetailConfList();
                CellDetailConfVO cellConfVO;
                int columnNo;
                boolean isDuplicatedInFirstColumn = false;

                backupMap.clear();
                while( z < lstColumns.size() )
                {
                    cellConfVO = ( CellDetailConfVO ) lstColumns.get( z );
                    columnNo = cellConfVO.getColumnNo() - 1;

                    expression = cellConfVO.getExpression();
                    if( expression == null )
                    {
                        content = rs.getObject( cellConfVO.getDbFieldName() );
                    }
                    else
                    {
                        content = cellConfVO.getExpression().express( cellConfVO.getDbFieldName(), backupMap, rs );
                    }

                    backupMap.put( cellConfVO.getDbFieldName(), content );
                    if( confVO.isModeRemoveDuplicateRow() )
                    {
                        backupRowList.add( content );
                        if( rowList.size() - 1 >= z )
                        {
                            if( content.equals( rowList.get( z ) ) )
                            {
                                // the part to make empty string to duplicate content
                                content = null;
                                if( z == 0 )
                            	{
                            		isDuplicatedInFirstColumn = true;
                            	}
                            }
                        }
                    }

                    commonExport.setObject( sheet, rowNo, columnNo, content );
                    z++;
                }
                
                if( confVO.isModeNumbering() )
                {
                    if( isDuplicatedInFirstColumn )
                    {
                    	recNo--;
                    }
                    else
                    {
                    	commonExport.setObject( sheet, rowNo, intNumberCol, new Integer( recNo ) );
                    }
                }
                
                if( recNo > confVO.getMaxRowsBeforeThrowError() )
                {
                	throw new RuntimeException( ": The number of records can not be higher than " + confVO.getMaxRowsBeforeThrowError() );
                }
            }
        }
        catch (SQLException e)
        {
            logger.info("SQL Error : " + e.getMessage());
            throw e;
        }



// it's used for batch process
// FileOutputStream destinationFile = new FileOutputStream( exportConfVO.getDestinationFilePath() );
// wb.write( destinationFile );
// destinationFile.close();
        
        if( confVO.isModeGotoNewSheetIfRowTooLarge() )
        {
        	try
			{
        		wb.removeSheetAt( templateSheetIndex );
			}
        	catch( RuntimeException e )
			{
        		logger.error("ERROR :", e);
			}
        }
        
        detailResultInfoVO.setNoOfRecord( recNo );
		return detailResultInfoVO;
	}
	
	public SimpleResultInfoVO generateSimpleToWorkBook( HSSFWorkbook workBook, SimplePartConfVO simplePartConfVO, ExportExcelDbUtilInt dbUtil )
	throws SQLException
	{
		logger.debug( "CreateExcel.generateSimpleToWorkBook" );
		
		SimpleResultInfoVO simpleResultInfoVO = new SimpleResultInfoVO();
		
		CellSimpleConfVO confVO;
		CommonExport commonExport = new CommonExport();
		
		if( simplePartConfVO.getSheetNo() == 0 )
		{
			throw( new RuntimeException( "CreateExcel.generateSimpleToWorkBook error. Sheet number is not defined." ) );
		}
		
		HSSFSheet sheet = workBook.getSheetAt( simplePartConfVO.getSheetNo() - 1 );
		List list = simplePartConfVO.getCellSimpleConfVOList();
		int size = list.size();
		Object content;

		ResultSet rs = null;
		
		if( simplePartConfVO.getQuery() != null && !simplePartConfVO.getQuery().trim().equals( "" ) )
		{
			List params = simplePartConfVO.getParams() == null ? null : simplePartConfVO.getParams();
			rs = dbUtil.executeQuery( simplePartConfVO.getQuery(), params );
			rs.next();
		}
        
		for( int i = 0; i < size; i++ )
		{
			confVO = ( CellSimpleConfVO ) list.get( i );
			if( confVO.isModeUseDb() )
			{
				if( rs != null )
				{
					content = rs.getObject( confVO.getDbFieldName() );
				}
				else
				{
					content = confVO.getDefaultValue();
				}
			}
			else
			{
				content = confVO.getDefaultValue();
			}
			
			commonExport.setObject( sheet, confVO.getRowNo() - 1, confVO.getColumnNo() - 1, content, confVO.getStyle() );
		}
		
		return simpleResultInfoVO;
	}
	
	public void generateHeaderFooterToWorkbook( HSSFWorkbook wb, HeaderFooterConfVO confVO )
	{
		logger.debug( "CreateExcel.generateHeaderFooterToWorkbook" );
		HSSFSheet sheet;
		HSSFFooter footerSheet;
		HSSFHeader headerSheet;
		
		if( confVO != null )
		{
			if( confVO.getSheetNo() == 0 )
			{
				throw( new RuntimeException( "CreateExcel.generateHeaderFooterToWorkbook error. Sheet number is not defined." ) );
			}
			sheet = wb.getSheetAt( confVO.getSheetNo() - 1 );
			headerSheet = sheet.getHeader();
			footerSheet = sheet.getFooter();
		
			if( confVO.getHeaderLeftContent() != null ) headerSheet.setLeft( confVO.getHeaderLeftContent() );
			if( confVO.getHeaderCenterContent() != null ) headerSheet.setCenter( confVO.getHeaderCenterContent() );
			if( confVO.getHeaderRightContent() != null ) headerSheet.setRight( confVO.getHeaderRightContent() );
		
			if( confVO.getFooterLeftContent() != null ) footerSheet.setLeft( confVO.getFooterLeftContent() );
			if( confVO.getFooterCenterContent() != null ) footerSheet.setCenter( confVO.getFooterCenterContent() );
			if( confVO.getFooterRightContent() != null ) footerSheet.setRight( confVO.getFooterRightContent() );
		}	
	}
	
	public ExportExcelResultInfoVO generateAllToWorkBook( HSSFWorkbook workBook, ExportExcelConfVO exportExcelConfVO, HeaderFooterConfVO headerFooterConfVO, ExportExcelDbUtilInt dbUtil )
	throws IOException, SQLException
	{
		logger.debug( "CreateExcel.generateAllToWorkBook" );
		ExportExcelResultInfoVO resultInfoVO = new ExportExcelResultInfoVO();
		
		List simplePartConfVOList;
		List detailPartConfVOList;
		SimplePartConfVO simplePartConfVO;
		DetailPartConfVO detailPartConfVO;
		List cellSimpleConfVOList;
		List cellDetailConfVOList;
		CellSimpleConfVO cellSimpleConfVO;
		CellDetailConfVO cellDetailConfVO;
		
		simplePartConfVOList = exportExcelConfVO.getSimplePartConfVOList();
		detailPartConfVOList = exportExcelConfVO.getDetailPartConfVOList();
		
		generateHeaderFooterToWorkbook( workBook, headerFooterConfVO );
		
		List simpleResultInfoVOList = new ArrayList();
		SimpleResultInfoVO simpleResultInfoVO;
		if( simplePartConfVOList != null )
		{
			for( int i = 0; i < simplePartConfVOList.size(); i++ )
			{
				simplePartConfVO = ( SimplePartConfVO ) simplePartConfVOList.get( i );
				simpleResultInfoVO = generateSimpleToWorkBook( workBook, simplePartConfVO, dbUtil );
				simpleResultInfoVOList.add( simpleResultInfoVO );	
			}
		}
		resultInfoVO.setSimpleResultInfoVOList( simpleResultInfoVOList );
		
		List detailResultInfoVOList = new ArrayList();
		DetailResultInfoVO detailResultInfoVO;
		if( detailPartConfVOList != null )
		{
			for( int i = 0; i < detailPartConfVOList.size(); i++ )
			{
				detailPartConfVO = ( DetailPartConfVO ) detailPartConfVOList.get( i );
				detailResultInfoVO = generateDetailToWorkbook( workBook, detailPartConfVO, dbUtil );
				detailResultInfoVOList.add( detailResultInfoVO );	
			}
		}
		resultInfoVO.setDetailResultInfoVOList( detailResultInfoVOList );
		
		return resultInfoVO;
	}
	
	public ExportExcelResultInfoVO generateAllToWorkBook( HSSFWorkbook workBook, ExportExcelConfVO exportExcelConfVO, ExportExcelDbUtilInt dbUtil )
	throws IOException, SQLException
	{
		return generateAllToWorkBook( workBook, exportExcelConfVO, null, dbUtil );
	}
	
}
