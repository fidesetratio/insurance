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

public class backup_XLSCreatorReportBIIBSM extends AbstractExcelView{
	
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
			 * GROUPING BY KURS
			 */
			String kurstmp="";
			List<GroupByKurs> lsGroupByKurs=new ArrayList<GroupByKurs>();
			
			GroupByKurs gbKurs=new GroupByKurs();
			List<Result> lstmresult=new ArrayList<Result>();
			for (int i = 0; i < lsHasil.size(); i++) {
				Result resulttmp=lsHasil.get(i);
				String kurs=resulttmp.getKurs();		
				
				if(!kurs.equals(kurstmp) && i!=0 && i!=lsHasil.size()-1){				
					gbKurs.setLsGroupByKurs(lstmresult);
					gbKurs.setKurs(kurstmp);
					lsGroupByKurs.add(gbKurs);
					gbKurs=new GroupByKurs();
					lstmresult=new ArrayList<Result>();
				}
				
				if(i==lsHasil.size()-1){
					if(kurs.equals(kurstmp)){
						lstmresult.add(resulttmp);
						gbKurs.setLsGroupByKurs(lstmresult);
						gbKurs.setKurs(kurstmp);
						lsGroupByKurs.add(gbKurs);
						gbKurs=new GroupByKurs();
						lstmresult=new ArrayList<Result>();
						
					}else{
						gbKurs=new GroupByKurs();
						lstmresult=new ArrayList<Result>();
						lstmresult.add(resulttmp);
						gbKurs.setLsGroupByKurs(lstmresult);					
						gbKurs.setKurs(kurstmp);
						lsGroupByKurs.add(gbKurs);
					}
					bank=result.getLsbp_nama();
				}else{
					kurstmp=kurs;
					lstmresult.add(resulttmp);
				}
				
				
			}
			result.setLsgroupbykurs(lsGroupByKurs);
			Integer lastKurs=0;
			for (int i = 0; i < result.getLsgroupbykurs().size(); i++) {
				GroupByKurs groupbykurs=result.getLsgroupbykurs().get(i);
				
	
				String kurs=groupbykurs.getKurs();
				 
				
				
				 /**
				  * set column size
				  */
				 sheet.setColumnWidth((short)(0+5*indexBank),(short)(25*256));
				 sheet.setColumnWidth((short)(1+5*indexBank),(short)(15*256));
				 sheet.setColumnWidth((short)(2+5*indexBank),(short)(7*256));
				 sheet.setColumnWidth((short)(3+5*indexBank),(short)(25*256));
				 
				
				
				 
				/*
				 * GROUP BY PLAN
				 */
				String nama_plan_tmp="";
				List<GroupByPlan> lsGroupByPlan=new ArrayList<GroupByPlan>();
				lstmresult=new ArrayList<Result>();
				GroupByPlan groupByPlan=new GroupByPlan();
				
				for (int j = 0; j < groupbykurs.getLsGroupByKurs().size(); j++) {
					Result resulttmp=groupbykurs.getLsGroupByKurs().get(j);
					String nama_plan=resulttmp.getNm_plan();
					
					 
					
					if(!nama_plan.equals(nama_plan_tmp) && j!=0 && j!=groupbykurs.getLsGroupByKurs().size()-1){				
						groupByPlan.setLsGroupByPlan(lstmresult);
						groupByPlan.setNm_plan(nama_plan_tmp);
						lsGroupByPlan.add(groupByPlan);
						groupByPlan=new GroupByPlan();
						lstmresult=new ArrayList<Result>();
					}
					
					if(j==groupbykurs.getLsGroupByKurs().size()-1){
						if(nama_plan.equals(nama_plan_tmp)){
							lstmresult.add(resulttmp);
							groupByPlan.setLsGroupByPlan(lstmresult);
							groupByPlan.setNm_plan(nama_plan_tmp);
							lsGroupByPlan.add(groupByPlan);
							groupByPlan=new GroupByPlan();
							lstmresult=new ArrayList<Result>();
							
						}else{
							groupByPlan=new GroupByPlan();
							lstmresult=new ArrayList<Result>();
							lstmresult.add(resulttmp);
							groupByPlan.setLsGroupByPlan(lstmresult);
							groupByPlan.setNm_plan(nama_plan_tmp);
							lsGroupByPlan.add(groupByPlan);
						}
					}else{
						nama_plan_tmp=nama_plan;
						lstmresult.add(resulttmp);
					}			
				}
				result.getLsgroupbykurs().get(i).setLsGroupByPlan(lsGroupByPlan);
				
				for (int j = 0; j < lsGroupByPlan.size(); j++) {
					groupByPlan=lsGroupByPlan.get(j);
					
					/*
					 * GROUP BY PLAN
					 */
					lastKurs=(4+28)* lsGroupByPlan.size();
					
					HSSFCell cell0=getCell(sheet,0+(4+28)*j+(lastKurs)*i,0+5*indexBank);
					cell0.setCellValue("Data Per "+periodeIndo);
					cell0.setCellStyle((styles.get("standar")));
					
					HSSFCell cell1=getCell(sheet,1+(4+28)*j+(lastKurs)*i,0+5*indexBank);
					cell1.setCellValue("KET");
					cell1.setCellStyle((styles.get("titleRight")));
					
					HSSFCell cell2=getCell(sheet,1+(4+28)*j+(lastKurs)*i,1+5*indexBank);
					cell2.setCellValue("NAMA PLAN");
					cell2.setCellStyle((styles.get("titleRight")));
					
					HSSFCell cell3=getCell(sheet,1+(4+28)*j+(lastKurs)*i,2+5*indexBank);
					cell3.setCellValue("KURS");
					cell3.setCellStyle((styles.get("titleRight")));
					
					HSSFCell cell4=getCell(sheet,1+(4+28)*j+(lastKurs)*i,3+5*indexBank);
					cell4.setCellValue("BANK "+groupByPlan.getLsGroupByPlan().get(0).getLsbp_nama());
					cell4.setCellStyle((styles.get("titleRight"))); 
					
					
					String kettmp="";
					List<GroupByKet> lsGroupByKet=new ArrayList<GroupByKet>();
					lstmresult=new ArrayList<Result>();
					GroupByKet groupByKet=new GroupByKet();
					for (int k = 0; k < groupByPlan.getLsGroupByPlan().size(); k++) {
						Result resulttmp=groupByPlan.getLsGroupByPlan().get(k);
						String ket=resulttmp.getKet();
						
						if(!ket.substring(0, 3).equals(kettmp.equals("")?"":kettmp.substring(0, 3)) && k!=0 && k!=groupByPlan.getLsGroupByPlan().size()-1){				
							groupByKet.setLsGroupByKet(lstmresult);
							groupByKet.setKet(kettmp);
							lsGroupByKet.add(groupByKet);
							groupByKet=new GroupByKet();
							lstmresult=new ArrayList<Result>();
						}
						
						if(k== groupByPlan.getLsGroupByPlan().size()-1){
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
					result.getLsgroupbykurs().get(i).getLsGroupByPlan().get(j).setLsGroupByKet(lsGroupByKet);
					Integer last=0;

					for (int k = 0; k < lsGroupByKet.size(); k++) {
						groupByKet=lsGroupByKet.get(k);
						
						
						
						for (int index = 0; index < groupByKet.getLsGroupByKet().size(); index++) {
							Result rs=groupByKet.getLsGroupByKet().get(index);
							int xyz=2;
							
						
							
							HSSFCell cell14=getCell(sheet,2+xyz*k+last+1*index+(4+28)*j+(lastKurs)*i,0+5*indexBank);
							cell14.setCellValue(rs.getKet());
							cell14.setCellStyle(styles.get("standar"));
							
							HSSFCell cell15=getCell(sheet,2+xyz*k+last+1*index+(4+28)*j+(lastKurs)*i,1+5*indexBank);
							cell15.setCellValue(rs.getNm_plan());
							cell15.setCellStyle(styles.get("standar"));	
							
							HSSFCell cell16=getCell(sheet,2+xyz*k+last+1*index+(4+28)*j+(lastKurs)*i,2+5*indexBank);
							cell16.setCellValue(rs.getKurs());
							cell16.setCellStyle(styles.get("standar"));
							
							HSSFCell cell17=getCell(sheet,2+xyz*k+last+1*index+(4+28)*j+(lastKurs)*i,3+5*indexBank);
							cell17.setCellValue(rs.getBancass());
							cell17.setCellStyle(styles.get("curr"));
							

						
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
