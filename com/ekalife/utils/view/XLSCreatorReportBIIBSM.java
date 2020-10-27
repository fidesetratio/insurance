/**
 * Yusuf (22/02/2010) - Report Save Series req by Iwen
 */
package com.ekalife.utils.view;

import id.co.sinarmaslife.std.util.DateUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ekalife.elions.model.saveseries.GroupByKet;
import com.ekalife.elions.model.saveseries.GroupByKurs;
import com.ekalife.elions.model.saveseries.GroupByPlan;
import com.ekalife.elions.model.saveseries.Result;

public class XLSCreatorReportBIIBSM extends AbstractExcelView{
	
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workBook, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		String[] kursGroup=new String[]{"Rupiah","Dollar"};
		String[][] nmPlanGroup;
		String[][][] ketGroup;
				
		NumberFormat nf=new DecimalFormat("#,##0.00");
		Map mapHasil=(Map) model.get("map");
		Date beg_date= (Date) mapHasil.get("begDate");
		String thn=DateUtil.getYearFourDigit(beg_date);
		String bln=DateUtil.getMonth(beg_date);
		String periodeIndo=DateUtil.toIndonesian(thn+bln);
		
		if(mapHasil.get("attach")!=null)		
		response.setHeader("Content-Disposition", "attachment; filename=Laporan_BII_BSM_"+periodeIndo.replace(" ","")+".xls");
		
		List<List<Result>> lsHasilBank=(List<List<Result>>) mapHasil.get("lsHasil");
		Map<String, HSSFCellStyle> styles = createStyles(workBook);
		/**
		 * Generate sheet
		 */
		HSSFSheet sheet = workBook.createSheet("BII & BSM");
		 sheet.setDefaultColumnWidth((short)12);
		
		 String bank="";
		
		for(int indexBank=0;indexBank<lsHasilBank.size();indexBank++){
			List<Result> lsHasil=lsHasilBank.get(indexBank);
			
			
			Double agencyOSSum=new Double(0);
			Double bancasOSSum=new Double(0);
			Double tmOSSum=new Double(0);
			Double wsOSSum=new Double(0);
			Double totOSSum=new Double(0);
			
			Result result=new Result();
			
			/**
			 * GROUPING BY Plan
			 */
			String plantmp="";
			List<GroupByPlan> lsGroupByPlan=new ArrayList<GroupByPlan>();
			
			GroupByPlan gbPlan=new GroupByPlan();
			List<Result> lstmresult=new ArrayList<Result>();
			for (int i = 0; i < lsHasil.size(); i++) {
				Result resulttmp=lsHasil.get(i);
				String nama_plan=resulttmp.getNm_plan();		
				
				if(!nama_plan.equals(plantmp) && i!=0 && i!=lsHasil.size()-1){				
					gbPlan.setLsGroupByPlan(lstmresult);
					gbPlan.setNm_plan(plantmp);
					lsGroupByPlan.add(gbPlan);
					gbPlan=new GroupByPlan();
					lstmresult=new ArrayList<Result>();
				}
				
				if(i==lsHasil.size()-1){
					if(nama_plan.equals(plantmp)){
						lstmresult.add(resulttmp);
						gbPlan.setLsGroupByPlan(lstmresult);
						gbPlan.setNm_plan(plantmp);
						lsGroupByPlan.add(gbPlan);
						gbPlan=new GroupByPlan();
						lstmresult=new ArrayList<Result>();
						
					}else{
						gbPlan=new GroupByPlan();
						lstmresult=new ArrayList<Result>();
						lstmresult.add(resulttmp);
						gbPlan.setLsGroupByPlan(lstmresult);					
						gbPlan.setNm_plan(plantmp);
						lsGroupByPlan.add(gbPlan);
					}
					bank=result.getLsbp_nama();
				}else{
					plantmp=nama_plan;
					lstmresult.add(resulttmp);
				}
				
				
			}
			result.setLsgroupbyPlan(lsGroupByPlan);
			int lastPlan=0;

			for (int i = 0; i < result.getLsgroupbyPlan().size(); i++) {
				GroupByPlan groupbyPlan=result.getLsgroupbyPlan().get(i);
				
	
				String namaPlan=groupbyPlan.getNm_plan();
				 
				
				 /**
				  * set column size
				  */
				 sheet.setColumnWidth((short)(0+5*indexBank+5*i),(short)(25*256));
				 sheet.setColumnWidth((short)(1+5*indexBank+5*i),(short)(15*256));
				 sheet.setColumnWidth((short)(2+5*indexBank+5*i),(short)(7*256));
				 sheet.setColumnWidth((short)(3+5*indexBank+5*i),(short)(25*256));
				 
				
				
				 
				/*
				 * GROUP BY KURS
				 */
				String kurs_tmp="";
				List<GroupByKurs> lsGroupByKurs=new ArrayList<GroupByKurs>();
				lstmresult=new ArrayList<Result>();
				GroupByKurs groupByKurs=new GroupByKurs();
				
				for (int j = 0; j < groupbyPlan.getLsGroupByPlan().size(); j++) {
					Result resulttmp=groupbyPlan.getLsGroupByPlan().get(j);
					String kurs=resulttmp.getKurs();
					
					 
					
					if(!kurs.equals(kurs_tmp) && j!=0 && j!=groupbyPlan.getLsGroupByPlan().size()-1){				
						groupByKurs.setLsGroupByKurs(lstmresult);
						groupByKurs.setKurs(kurs_tmp);
						lsGroupByKurs.add(groupByKurs);
						groupByKurs=new GroupByKurs();
						lstmresult=new ArrayList<Result>();
					}
					
					if(j==groupbyPlan.getLsGroupByPlan().size()-1){
						if(kurs.equals(kurs_tmp)){
							lstmresult.add(resulttmp);
							groupByKurs.setLsGroupByKurs(lstmresult);
							groupByKurs.setKurs(kurs_tmp);
							lsGroupByKurs.add(groupByKurs);
							groupByKurs=new GroupByKurs();
							lstmresult=new ArrayList<Result>();
							
						}else{
							groupByKurs=new GroupByKurs();
							lstmresult=new ArrayList<Result>();
							lstmresult.add(resulttmp);
							groupByKurs.setLsGroupByKurs(lstmresult);
							groupByKurs.setKurs(kurs_tmp);
							lsGroupByKurs.add(groupByKurs);
						}
					}else{
						kurs_tmp=kurs;
						lstmresult.add(resulttmp);
					}			
				}
				result.getLsgroupbyPlan().get(i).setLsGroupByKurs(lsGroupByKurs);
				
				for (int j = 0; j < lsGroupByKurs.size(); j++) {
					groupByKurs=lsGroupByKurs.get(j);
					
					/*
					 * GROUP BY PLAN
					 */
					lastPlan=(4+28);
					
					HSSFCell cell0=getCell(sheet,0+(lastPlan)*j,0+5*indexBank+5*i);
					cell0.setCellValue("Data Per "+periodeIndo);
					cell0.setCellStyle((styles.get("standar")));
					
					HSSFCell cell1=getCell(sheet,1+(lastPlan)*j,0+5*indexBank+5*i);
					cell1.setCellValue("KET");
					cell1.setCellStyle((styles.get("titleRight")));
					
					HSSFCell cell2=getCell(sheet,1+(lastPlan)*j,1+5*indexBank+5*i);
					cell2.setCellValue("NAMA PLAN");
					cell2.setCellStyle((styles.get("titleRight")));
					
					HSSFCell cell3=getCell(sheet,1+(lastPlan)*j,2+5*indexBank+5*i);
					cell3.setCellValue("KURS");
					cell3.setCellStyle((styles.get("titleRight")));
					
					HSSFCell cell4=getCell(sheet,1+(lastPlan)*j,3+5*indexBank+5*i);
					cell4.setCellValue("BANK "+groupByKurs.getLsGroupByKurs().get(0).getLsbp_nama());
					cell4.setCellStyle((styles.get("titleRight"))); 
					
					
					String kettmp="";
					List<GroupByKet> lsGroupByKet=new ArrayList<GroupByKet>();
					lstmresult=new ArrayList<Result>();
					GroupByKet groupByKet=new GroupByKet();
					for (int k = 0; k < groupByKurs.getLsGroupByKurs().size(); k++) {
						Result resulttmp=groupByKurs.getLsGroupByKurs().get(k);
						String ket=resulttmp.getKet();
						
						if(!ket.substring(0, 3).equals(kettmp.equals("")?"":kettmp.substring(0, 3)) && k!=0 && k!=groupByKurs.getLsGroupByKurs().size()-1){				
							groupByKet.setLsGroupByKet(lstmresult);
							groupByKet.setKet(kettmp);
							lsGroupByKet.add(groupByKet);
							groupByKet=new GroupByKet();
							lstmresult=new ArrayList<Result>();
						}
						
						if(k== groupByKurs.getLsGroupByKurs().size()-1){
							if(ket.substring(0, 3).equals(kettmp.equals("")?"":kettmp.substring(0, 3))){
								lstmresult.add(resulttmp);
								groupByKet.setLsGroupByKet(lstmresult);
								groupByKet.setKet(kettmp);
								lsGroupByKet.add(groupByKet);
								groupByKet=new GroupByKet();
								lstmresult=new ArrayList<Result>();
								
							}else{
								groupByKet=new GroupByKet();
								lstmresult=new ArrayList<Result>();
								lstmresult.add(resulttmp);
								groupByKet.setLsGroupByKet(lstmresult);
								groupByKet.setKet(kettmp);
								lsGroupByKet.add(groupByKet);
							}
						}else{
							kettmp=ket;
							lstmresult.add(resulttmp);
						}			
					}
					result.getLsgroupbyPlan().get(i).getLsGroupByKurs().get(j).setLsGroupByKet(lsGroupByKet);
					Integer last=0;
					
					Double bancasSaldo=new Double(0);
					Double bancasDelta=new Double(0);
					for (int k = 0; k < lsGroupByKet.size(); k++) {
						groupByKet=lsGroupByKet.get(k);
						
						
						Double bancasSum=new Double(0);
						for (int index = 0; index < groupByKet.getLsGroupByKet().size(); index++) {
							Result rs=groupByKet.getLsGroupByKet().get(index);
							int xyz=2;
							
							
							
						
							
							HSSFCell cell14=getCell(sheet,2+xyz*k+last+1*index+(lastPlan)*j,0+5*indexBank+5*i);
							cell14.setCellValue(rs.getKet());
							cell14.setCellStyle(styles.get("standar"));
							
							HSSFCell cell15=getCell(sheet,2+xyz*k+last+1*index+(lastPlan)*j,1+5*indexBank+5*i);
							cell15.setCellValue(rs.getNm_plan());
							cell15.setCellStyle(styles.get("standar"));	
							
							HSSFCell cell16=getCell(sheet,2+xyz*k+last+1*index+(lastPlan)*j,2+5*indexBank+5*i);
							cell16.setCellValue(rs.getKurs());
							cell16.setCellStyle(styles.get("standar"));
							
							HSSFCell cell17=getCell(sheet,2+xyz*k+last+1*index+(lastPlan)*j,3+5*indexBank+5*i);
							cell17.setCellValue(rs.getBancass());
							
							if(!rs.getKet().contains("OS")){	
								cell17.setCellStyle(styles.get("curr"));
							}else{
								cell17.setCellStyle(styles.get("currTOT1"));
							}
							
							bancasSum+=rs.getBancass();
							
							if(!rs.getKet().contains("OS")){								
								
								HSSFCell cell20=getCell(sheet,2+xyz*k+last+1*index+(lastPlan)*j+1,3+5*indexBank+5*i);
								cell20.setCellValue(bancasSum);
								cell20.setCellStyle(styles.get("currTOT1"));
							
								
								
							}
							
							
							
							if(index==groupByKet.getLsGroupByKet().size()-1){
								if(rs.getKet().contains("OS")){
									bancasSaldo=bancasSum;
								}else if(rs.getKet().contains("NB")){
									bancasSaldo+=bancasSum;
									bancasDelta+=bancasSum;
								}else if(rs.getKet().contains("BR")){
									bancasSaldo-=bancasSum;
									bancasDelta-=bancasSum;
								}
								
								if(k==lsGroupByKet.size()-1){
									HSSFCell cel21=getCell(sheet,2+xyz*k+last+1*index+(lastPlan)*j+3,0+5*indexBank+5*i);
									cel21.setCellValue("Saldo Akhir");
									cel21.setCellStyle((styles.get("standar")));
									
									HSSFCell cell21a=getCell(sheet,2+xyz*k+last+1*index+(lastPlan)*j+3,1+5*indexBank+5*i);
									cell21a.setCellValue(rs.getNm_plan());
									cell21a.setCellStyle(styles.get("standar"));
									
									HSSFCell cell21b=getCell(sheet,2+xyz*k+last+1*index+(lastPlan)*j+3,2+5*indexBank+5*i);
									cell21b.setCellValue(rs.getKurs());
									cell21b.setCellStyle(styles.get("standar"));
									
									HSSFCell cell21c=getCell(sheet,2+xyz*k+last+1*index+(lastPlan)*j+3,3+5*indexBank+5*i);
									cell21c.setCellValue(bancasSaldo);
									cell21c.setCellStyle(styles.get("currTOT1"));
									
									HSSFCell cel22=getCell(sheet,2+xyz*k+last+1*index+(lastPlan)*j+4,0+5*indexBank+5*i);
									cel22.setCellValue("Total Delta");
									cel22.setCellStyle((styles.get("standar")));
									
									HSSFCell cel22a=getCell(sheet,2+xyz*k+last+1*index+(lastPlan)*j+4,3+5*indexBank+5*i);
									cel22a.setCellValue(bancasDelta);
									cel22a.setCellStyle((styles.get("currTOT1")));
								}
							}
						
						}
						
						
						
						
	
					   
	
						
						last+=groupByKet.getLsGroupByKet().size();
						
						
						
					}
					
					
				}
			}

		}

		
	}
	
	/**
     * cell styles used for formatting calendar sheets
     */
    private static Map<String, HSSFCellStyle> createStyles(HSSFWorkbook wb){
        Map<String, HSSFCellStyle> styles = new HashMap<String, HSSFCellStyle>();

        short borderColor = HSSFColor.GREY_50_PERCENT.index;

        HSSFCellStyle style;
        HSSFFont titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)10);
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setFont(titleFont);        
        styles.put("title", style);
        
        style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)10);
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(titleFont); 
        style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styles.put("titleRight", style);
        
        style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)10);
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style.setFont(titleFont);        
        styles.put("standar", style);
        
        
        
        style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)10);
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style.setFont(titleFont);        
        style.setDataFormat(wb.createDataFormat().getFormat("##,##0.00"));
      
        styles.put("curr", style);
        
        style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)10);
        titleFont.setColor(HSSFColor.BLACK.index);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(titleFont);  
        style.setDataFormat(wb.createDataFormat().getFormat("##,##0.00"));
       
        styles.put("currTOT1", style);
        
        style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)10);
        titleFont.setColor(HSSFColor.RED.index);
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(titleFont);        
        style.setDataFormat(wb.createDataFormat().getFormat("##,##0.00"));
       

        styles.put("currTOT2", style);

        
       

        return styles;
    }
	
}
