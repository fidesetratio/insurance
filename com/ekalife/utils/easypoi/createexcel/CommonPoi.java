/**********************************************************************
 * Program History
 *
 * Project Name      	: Inventory and Costing System
 * Client Name       	: Toyota Motor Manufacturing Indonesia 
 * Program Id         	: CommonPoi.java
 * Program Name   		: CommonPoi
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;


public class CommonPoi
{


    public static void copyRow(HSSFSheet sheet, int source, int dest) {
       copyRow(sheet, source, dest, false);
    }

    public static void copyRow(HSSFSheet sheet, int source, int dest, boolean blSameHeight) {

        HSSFRow rowSource = sheet.getRow(source);
        HSSFRow rowDest = sheet.createRow(dest);

        for (short col=0; col<=rowSource.getLastCellNum(); col++) {
            HSSFCell cellSource = rowSource.getCell(col);
            HSSFCell cellDest = rowDest.createCell(col);

            if (cellSource != null)
            {
                int cellType = cellSource.getCellType();

                switch (cellType) {
                    case HSSFCell.CELL_TYPE_STRING :
                        cellDest.setCellValue(cellSource.getStringCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC :
                        cellDest.setCellValue(cellSource.getNumericCellValue());
                        break;
                    default :
                        break;
                }
                cellDest.setCellStyle(cellSource.getCellStyle());
            }
        }

        if (blSameHeight) {
            rowDest.setHeightInPoints(rowSource.getHeightInPoints());
        }

    }

    private static HSSFCell createCell(HSSFSheet sheet, int intRow, int intCol) {
        HSSFRow row = sheet.getRow(intRow);
        if (row==null) row = sheet.createRow(intRow);
        HSSFCell cell = row.getCell((short)intCol);
        if (cell==null) cell = row.createCell((short)intCol);
        return cell;
    }

    public static void setText(HSSFSheet sheet, int row, int col, String text) {
        HSSFCell cell = createCell(sheet, row, col);
        cell.setCellValue(text);
    }

    public static void setInt(HSSFSheet sheet, int row, int col, int value) {
        HSSFCell cell = createCell(sheet, row, col);
        cell.setCellValue(value);
    }

    public static void setDate(HSSFSheet sheet, int row, int col, java.util.Date date) {
        HSSFCell cell = createCell(sheet, row, col);
        if (date==null) cell.setCellValue("");
        else cell.setCellValue(date);
    }

    public static void writeToResponse(HSSFWorkbook report, HttpServletResponse response, String stFileName)
            throws IOException {
        response.setContentType("application/vnd.ms-excel");
//        String stFileName = stTemplate.substring(stTemplate.lastIndexOf(File.separator)+1);
//        stFileName = DateUtil.format(new Date(),"yyyyMMddHHmmss") + "_" + stFileName;
//        String currentDate = DirectSQLAdapter.getInstance().getCurrentDate("DD/MM/YYYY HH:MI:SS");
        String currentDate = "";
//        try {
//            currentDate = DirectSQLAdapter.getInstance().getCurrentDate("yyyyMMddHHmmss");
            //currentDate = DirectSQLAdapter.getInstance().getCurrentDate("yyyyMMddHH24miss");
//        } catch (SQLException e) {
//            logger.error("ERROR :", e);  //To change body of catch statement use Options | File Templates.
//        }
        stFileName = stFileName + currentDate + "_R.xls";
        response.setHeader("Content-Disposition", "attachment;filename=" + stFileName);
        ServletOutputStream fileOut = response.getOutputStream();
        report.write(fileOut);
        fileOut.flush();

//        BufferedOutputStream bos = new BufferedOutputStream(fileOut);
//        report.write(bos);

    }

//----------------------------------------------------------

    public static void setObject(HSSFSheet sheet, int row, int col, Object obj) {
        HSSFCell cell = createCell(sheet, row, col);
        if (obj==null) cell.setCellValue("");
        else if (obj instanceof String)
            cell.setCellValue((String)obj);
//        else if (obj instanceof Number)
//            cell.setCellValue(((Number)obj).intValue());
        else if (obj instanceof Number)
            cell.setCellValue(((Number)obj).doubleValue());
        else if (obj instanceof Date)
            cell.setCellValue((Date)obj);
    }

    public static List getColNames(HSSFSheet sheet, int intRow, int startCol) {
        List colNames = new ArrayList();
        HSSFRow row = sheet.getRow(intRow-1);
        for (short col = (short)(startCol-1); col < row.getLastCellNum(); col++) {
            HSSFCell cell = row.getCell(col);
            if (cell==null) break;
            String colName = row.getCell(col).getStringCellValue();
            if (colName == null || colName.trim().equals( "" ) ) break;
            colNames.add(colName);
        }
        return colNames;
    }

    public static void lastRow(HSSFWorkbook wb, int intSheet, int intRow) {
        HSSFRow row = wb.getSheetAt(intSheet).getRow(intRow);
        for (short i=0; i<row.getLastCellNum()-2; i++) {
            try {
                HSSFCellStyle oldStyle = row.getCell(i).getCellStyle();
                HSSFCellStyle newStyle = wb.createCellStyle();
                newStyle.setAlignment(oldStyle.getAlignment());
                newStyle.setBorderLeft(oldStyle.getBorderLeft());
                newStyle.setBorderRight(oldStyle.getBorderRight());
                newStyle.setDataFormat(oldStyle.getDataFormat());
                newStyle.setFont(wb.getFontAt(oldStyle.getFontIndex()));
                newStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
                row.getCell(i).setCellStyle(newStyle);
            } catch(NullPointerException npe) {}
        }
    }

    public static void lastRow(HSSFWorkbook wb, int intRow) {
        HSSFRow row = wb.getSheetAt(0).getRow(intRow);
        for (short i=0; i<row.getLastCellNum()-2; i++) {
            try {
                HSSFCellStyle oldStyle = row.getCell(i).getCellStyle();
                HSSFCellStyle newStyle = wb.createCellStyle();
                newStyle.setAlignment(oldStyle.getAlignment());
                newStyle.setBorderLeft(oldStyle.getBorderLeft());
                newStyle.setBorderRight(oldStyle.getBorderRight());
                newStyle.setDataFormat(oldStyle.getDataFormat());
                newStyle.setFont(wb.getFontAt(oldStyle.getFontIndex()));
                newStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
                row.getCell(i).setCellStyle(newStyle);
            } catch(NullPointerException npe) {}
        }
    }
/*    public static void lastRow(HSSFSheet sheet, int intRow) {
        HSSFRow row = sheet.createRow(intRow);
        HSSFRow rowPrev = sheet.getRow(intRow-1);
        for (short i=0; i<rowPrev.getLastCellNum(); i++) {
            try {
                HSSFCellStyle style = row.getCell(i).getCellStyle();
                style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
                row.getCell(i).setCellStyle(style);
            } catch(NullPointerException npe) {}
        }
    } */

    public static void lastRow(HSSFRow row) {
        for (short i=0; i<row.getLastCellNum(); i++) {
            try {
                HSSFCellStyle style = row.getCell(i).getCellStyle();
                style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
                row.getCell(i).setCellStyle(style);
            } catch(NullPointerException npe) {}
        }
    }

    public static void copyFormat(HSSFRow rowSource, HSSFRow rowDest) {
        rowDest.setHeight(rowSource.getHeight());
        for (short i=0; i<rowSource.getLastCellNum(); i++) {
            try {
                rowDest.getCell(i).setCellStyle(rowSource.getCell(i).getCellStyle());
//                HSSFCellStyle styleSource  = rowSource.getCell(i).getCellStyle();
//                HSSFCellStyle styleDest    = rowDest.getCell(i).getCellStyle();
//                styleDest.get
            } catch(NullPointerException npe) {}
        }
    }

    public static HSSFRow getRow(HSSFSheet sheet, int intRow) {
        HSSFRow row = sheet.getRow(intRow);
        if (row==null) row = sheet.createRow(intRow);
        return row;
    }

    public static HSSFCell getCell(HSSFRow row, int intCol) {
        HSSFCell cell = row.getCell((short)intCol);
        if (cell==null) cell = row.createCell((short)intCol);
        return cell;
    }

//    public static void copySheet(HSSFWorkbook wb, String sheetTitle) {
//        HSSFSheet newSheet = wb.createSheet(sheetTitle);
//        newSheet = wb.cloneSheet(0);
//    }

    public static void deleteRow(HSSFSheet sheet, int from, int count) {
        deleteRow(sheet, from, count, false);
    }

    public static void deleteRow(HSSFSheet sheet, int from, int count, boolean blSameHeight) {
		for (int i=from+count; i<=sheet.getLastRowNum(); i++) {
			copyRow(sheet,i,from++, blSameHeight);
        }
		for ( ; from<=sheet.getLastRowNum(); from++) {
			HSSFRow rowDelete = sheet.getRow(from);
			if (rowDelete!=null) sheet.removeRow(rowDelete);
		}
	}

    public static void mergeCells(HSSFSheet sheet, int rowFrom, int colFrom, int rowTo, int colTo) {
        sheet.addMergedRegion(new Region(rowFrom, (short) colFrom, rowTo, (short) colTo));
    }

}

