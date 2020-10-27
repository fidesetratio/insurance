/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: CommonExport.java
 * Program Name   		: CommonExport
 * Description         	: 
 * Environment      	: Java  1.4.2
 * Author               : Samuel Baktiar
 * Version              : 
 * Creation Date    	: Apr 3, 2007 1:00:00 PM
 *
 * Update history   Re-fix date      Person in charge      Description
 *
 * Copyright(C) 2007-TOYOTA Motor Manufacturing Indonesia. All Rights Reserved.
 ***********************************************************************/

package com.ekalife.utils.easypoi.createexcel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class CommonExport
{
    public static HSSFWorkbook getTemplate( String templatePath )
            throws IOException
    {
        HSSFWorkbook result;
        POIFSFileSystem fs = null;
        try
        {
            fs = new POIFSFileSystem( new FileInputStream( templatePath ) );
            result = new HSSFWorkbook( fs );
        }
        catch (IOException e)
        {
//            String stMessage = "Error getting the template file :" + e.getMessage();
//            throw new ExportException(stMessage);
            throw e;
        }
        return result;
    }

    public void copyRow(HSSFSheet sheet, int source, int dest) {
        HSSFRow rowSource = sheet.getRow(source);
        HSSFRow rowDest = sheet.getRow(dest);
        if (rowDest == null) {
            rowDest = sheet.createRow(dest);
        }
        if (rowSource != null && rowDest != null) {
            for (short col = 0; col <= rowSource.getLastCellNum(); col++) {
                HSSFCell cellSource = rowSource.getCell(col);
                HSSFCell cellDest = rowDest.getCell(col);
                if (cellDest == null) {
                    cellDest = rowDest.createCell(col);
                }
                if (cellSource != null) {
                	int cellType = cellSource.getCellType();
                	
                	cellDest.setCellStyle( cellSource.getCellStyle() );
                    
                    switch (cellType) {
                        case HSSFCell.CELL_TYPE_STRING:
//                            cellDest.setCellValue(cellSource.getStringCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC:
//                            cellDest.setCellValue(cellSource.getNumericCellValue());
                            break;
                        default :
                            break;
                    }
                }
            }
        }
    }

    public void copySheetByRow(HSSFWorkbook wbsource, HSSFWorkbook wbdest, int intSheetSource, int intSheetDest) {

        HSSFSheet sheetSource = wbsource.getSheetAt(intSheetSource);
        HSSFSheet sheetDest = wbdest.createSheet();
        sheetDest = wbdest.getSheetAt(intSheetDest);
        sheetDest.setDisplayGridlines(false);

        int intMergedReg = sheetSource.getNumMergedRegions();
        for (int i = 0; i < intMergedReg; i++) {
            sheetDest.addMergedRegion(sheetSource.getMergedRegionAt(i));
        }

        int intNumOfRows = sheetSource.getLastRowNum();
        if (intNumOfRows != 0) {
            for (short row = 0; row < intNumOfRows; row++) {

                HSSFRow rowSource = sheetSource.getRow(row);
                HSSFRow rowDest = sheetDest.createRow(row);
//                rowDest.setHeight(rowSource.getHeight());
                if (rowSource != null) {
                    for (short col = 0; col <= rowSource.getLastCellNum(); col++) {

                        HSSFCell cellSource = rowSource.getCell(col);
                        HSSFCell cellDest = rowDest.createCell(col);
                        sheetDest.setColumnWidth(col, sheetSource.getColumnWidth(col));
                        if (cellSource != null) {
                            int cellType = cellSource.getCellType();
                            switch (cellType) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    cellDest.setCellValue(cellSource.getStringCellValue());
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    cellDest.setCellValue(cellSource.getNumericCellValue());
                                    break;
                                default :
                                    break;
                            }
                            rowDest.getCell(col).setCellStyle(rowSource.getCell(col).getCellStyle());
                        }
                    }
                }

            }
        }
    }

    public void setObject(HSSFSheet sheet, int row, int col, Object obj)
    {
    	setObject(sheet, row, col, obj, null);
    }
    
    public void setObject(HSSFSheet sheet, int row, int col, Object obj, HSSFCellStyle style ) {
        if (obj!=null) {
            HSSFCell cell = createCell(sheet, row, col);
            
            if( style != null ) cell.setCellStyle( style );
            if (obj instanceof String)
                cell.setCellValue((String) obj);
            else if (obj instanceof Number)
                cell.setCellValue(((Number) obj).doubleValue());
            else if (obj instanceof Date)
                cell.setCellValue((Date) obj);
        }
        else {
            HSSFCell cell = sheet.getRow(row).getCell((short)col);
            if (cell!=null) {
                cell.setCellValue((String) null);
            }
        }
    }
    
    public void setCellObjects(HSSFCellStyle style, HSSFCell cell, Object obj) {
        if (obj == null) {
            cell.setCellValue("");
        } else if (obj instanceof String) {
            cell.setCellValue((String) obj);
        } else if (obj instanceof Number) {
            style.setDataFormat((short) 1);
            cell.setCellValue(((Number) obj).doubleValue());
            cell.setCellStyle(style);
        } else if (obj instanceof Date) {
            cell.setCellValue((Date) obj);
            cell.setCellStyle(style);
        }
    }

    private static HSSFCell createCell(HSSFSheet sheet, int intRow, int intCol) {
        HSSFRow row = sheet.getRow(intRow);
        if (row == null) row = sheet.createRow(intRow);
        HSSFCell cell = row.getCell((short) intCol);
        if (cell == null) cell = row.createCell((short) intCol);
        return cell;
    }

    public int hlpAlpha2IntCol(String strValue) {
        int intValue = 0;
        int intRealValue = 0;
        String intRef = "1234567890";
        String strRef = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char arrChrValue[] = strValue.toUpperCase().toCharArray();
        char chrValue = arrChrValue[0];
        if (strRef.indexOf(chrValue) != -1) {
            if (arrChrValue.length >= 2) {
                int intTemp = (strRef.indexOf(chrValue)) + 1;  //==0)?1:strRef.indexOf(chrValue);
                int Kalian = (25 * intTemp);
                intValue = (strRef.indexOf(chrValue) + (Kalian)) + 1;
                for (int i = 0; i < arrChrValue.length; i++) {
                    chrValue = arrChrValue[i];
                    intRealValue = intValue + strRef.indexOf(chrValue);
                }
            } else {
                if (strRef.indexOf(chrValue) != -1)
                    intRealValue = strRef.indexOf(chrValue);
            }
        } else if (intRef.indexOf(chrValue) != -1) {
            intRealValue = 0;
        }
        return (intRealValue > 255) ? 0 : intRealValue;
    }

    public void insertRow( HSSFSheet sheet, int rowNo )
	{
    	HSSFRow row = sheet.getRow( rowNo );
    	
    	if( row != null )
    	{
    	    // shift rows
    	    sheet.shiftRows( rowNo + 1, rowNo + 2, 1 );
    	}
    	else
    	{
    	     // create row
    	     sheet.createRow( rowNo + 1 );
    	}
	}


}

