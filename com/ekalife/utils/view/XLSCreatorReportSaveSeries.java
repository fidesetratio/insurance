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
import org.apache.poi.hssf.util.Region;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ekalife.elions.model.saveseries.GroupByKet;
import com.ekalife.elions.model.saveseries.GroupByKurs;
import com.ekalife.elions.model.saveseries.GroupByPlan;
import com.ekalife.elions.model.saveseries.Result;

public class XLSCreatorReportSaveSeries extends AbstractExcelView{
	
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workBook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String[] kursGroup=new String[]{"Rupiah","Dollar"};
		String[][] nmPlanGroup;
		String[][][] ketGroup;
				
		NumberFormat nf=new DecimalFormat("#,##0.00");
		Map mapHasil=(Map) model.get("map");
		
		List<Result> lsHasil=(List<Result>) mapHasil.get("lsHasil");
		
		Map<String, HSSFCellStyle> styles = createStyles(workBook);
		
		Date beg_date= (Date) mapHasil.get("begDate");
		String thn=DateUtil.getYearFourDigit(beg_date);
		String bln=DateUtil.getMonth(beg_date);
		String periodeIndo=DateUtil.toIndonesian(thn+bln);
		
		if(mapHasil.get("attach")!=null)
		response.setHeader("Content-Disposition", "attachment; filename=LaporanSaveSeries_"+periodeIndo.replace(" ","")+".xls");
		
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
			}else{
				kurstmp=kurs;
				lstmresult.add(resulttmp);
			}
			
			
		}
		result.setLsgroupbykurs(lsGroupByKurs);
		
		for (int i = 0; i < result.getLsgroupbykurs().size(); i++) {
			GroupByKurs groupbykurs=result.getLsgroupbykurs().get(i);
			

			String kurs=groupbykurs.getKurs();
			/**
			 * Generate sheet
			 */
			HSSFSheet sheet = workBook.createSheet(kurs.equals("Rp")?"Rupiah":kurs.equals("US$")?"Dollar":kurs);
				
			 sheet.setDefaultColumnWidth((short)12);
			 sheet.addMergedRegion(new Region(1, (short) 0, 1, (short) 3));
			 HSSFCell cell0=getCell(sheet,1,0);
			 cell0.setCellValue("Total OS "+thn);
			 cell0.setCellStyle((styles.get("title")));
			 sheet.setColumnWidth((short)(0),(short)(20*256));
			 sheet.setColumnWidth((short)(1),(short)(6*256));
			 sheet.setColumnWidth((short)(2),(short)(6*256));
			 for (int j = 0; j < 5; j++) {
				 sheet.setColumnWidth((short)(4+j),(short)(25*256));
			 }
			 
			 HSSFCell cell1=getCell(sheet,0,4);
			 cell1.setCellValue("AGENCY");
			 cell1.setCellStyle((styles.get("title")));
			 
			 HSSFCell cell2=getCell(sheet,0,5);
			 cell2.setCellValue("BANCASSURANCE");
			 cell2.setCellStyle((styles.get("title")));
			 
			 HSSFCell cell3=getCell(sheet,0,6);
			 cell3.setCellValue("DMTM");
			 cell3.setCellStyle((styles.get("title")));
			 
			 HSSFCell cell4=getCell(sheet,0,7);
			 cell4.setCellValue("WORKSITE");
			 cell4.setCellStyle((styles.get("title")));
			 
			 HSSFCell cell5=getCell(sheet,0,8);
			 cell5.setCellValue("TOTAL");
			 cell5.setCellStyle((styles.get("title")));
			 
			 //TOTAL OS
			 
			 for(Result resulttmp:groupbykurs.getLsGroupByKurs()){
//				sum os
					if(resulttmp.getKet().contains("1OS")){
						agencyOSSum+=resulttmp.getAgency();
						bancasOSSum+=resulttmp.getBancass();
						tmOSSum+=resulttmp.getTm();
						wsOSSum+=resulttmp.getWsite();
						totOSSum+=resulttmp.getTotal();
					}
				
			 }
			 HSSFCell cell1a=getCell(sheet,1,4);
			 cell1a.setCellValue(agencyOSSum);
			 cell1a.setCellStyle((styles.get("currTOT1")));
			 
			 HSSFCell cell2a=getCell(sheet,1,5);
			 cell2a.setCellValue(bancasOSSum);
			 cell2a.setCellStyle((styles.get("currTOT1")));
			 
			 HSSFCell cell3a=getCell(sheet,1,6);
			 cell3a.setCellValue(tmOSSum);
			 cell3a.setCellStyle((styles.get("currTOT1")));
			 
			 HSSFCell cell4a=getCell(sheet,1,7);
			 cell4a.setCellValue(wsOSSum);
			 cell4a.setCellStyle((styles.get("currTOT1")));
			 
			 HSSFCell cell5a=getCell(sheet,1,8);
			 cell5a.setCellValue(totOSSum);
			 cell5a.setCellStyle((styles.get("currTOT1")));
			 
			 
			 
			 HSSFCell cell6=getCell(sheet,3,4);
			 cell6.setCellValue("AGENCY");
			 cell6.setCellStyle((styles.get("title")));
			 
			 HSSFCell cell7=getCell(sheet,3,5);
			 cell7.setCellValue("BANCASSURANCE");
			 cell7.setCellStyle((styles.get("title")));
			 
			 HSSFCell cell8=getCell(sheet,3,6);
			 cell8.setCellValue("DMTM");
			 cell8.setCellStyle((styles.get("title")));
			 
			 HSSFCell cell9=getCell(sheet,3,7);
			 cell9.setCellValue("WORKSITE");
			 cell9.setCellStyle((styles.get("title")));
			 
			 HSSFCell cell10=getCell(sheet,3,8);
			 cell10.setCellValue("TOTAL");
			 cell10.setCellStyle((styles.get("title")));
////			freeze the first row
	        sheet.createFreezePane(0, 4);

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
			
			Double agencyGrandTotal=new Double(0);
			Double bancasGrandTotal=new Double(0);
			Double tmGrandTotal=new Double(0);
			Double wsGrandTotal=new Double(0);
			Double totGrandTotal=new Double(0);
			Double agencyDeltaTotal=new Double(0);
			Double bancasDeltaTotal=new Double(0);
			Double tmDeltaTotal=new Double(0);
			Double wsDeltaTotal=new Double(0);
			Double totDeltaTotal=new Double(0);
			for (int j = 0; j < lsGroupByPlan.size(); j++) {
				groupByPlan=lsGroupByPlan.get(j);
				
				/*
				 * GROUP BY PLAN
				 */
				
				HSSFCell cell11=getCell(sheet,4+28*j,0);
				 cell11.setCellValue(groupByPlan.getNm_plan());
				 cell11.setCellStyle(styles.get("standar"));
				
				 HSSFCell cell12=getCell(sheet,4+28*j,1);
				 cell12.setCellValue(thn);
				 cell12.setCellStyle(styles.get("standar"));
				 
				 HSSFCell cell13=getCell(sheet,4+28*j,2);
				 cell13.setCellValue(bln);
				 cell13.setCellStyle(styles.get("standar"));
				 
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
//				Delta
				Double agencyDelta=new Double(0);
				Double bancasDelta=new Double(0);
				Double tmDelta=new Double(0);
				Double wsDelta=new Double(0);
				Double totDelta=new Double(0);
				
				Double agencyPlanTot=new Double(0);
				Double bancasPlanTot=new Double(0);
				Double tmPlanTot=new Double(0);
				Double wsPlanTot=new Double(0);
				Double totPlanTot=new Double(0);
				
				for (int k = 0; k < lsGroupByKet.size(); k++) {
					groupByKet=lsGroupByKet.get(k);
					//sum
					Double agencySum=new Double(0);
					Double bancasSum=new Double(0);
					Double tmSum=new Double(0);
					Double wsSum=new Double(0);
					Double totSum=new Double(0);
					
					
					
					for (int index = 0; index < groupByKet.getLsGroupByKet().size(); index++) {
						Result rs=groupByKet.getLsGroupByKet().get(index);
						int xyz=2;
						
						HSSFCell cell14=getCell(sheet,4+28*j+xyz*k+last+1*index,3);
						cell14.setCellValue(rs.getKet());
						 cell14.setCellStyle(styles.get("standar"));
						
						HSSFCell cell15=getCell(sheet,4+28*j+xyz*k+last+1*index,4);
						cell15.setCellValue(rs.getAgency());
						cell15.setCellStyle(styles.get("curr"));
						agencySum+=rs.getAgency();
						
						HSSFCell cell16=getCell(sheet,4+28*j+xyz*k+last+1*index,5);
						cell16.setCellValue(rs.getBancass());
						cell16.setCellStyle(styles.get("curr"));
						bancasSum+=rs.getBancass();
						
						HSSFCell cell17=getCell(sheet,4+28*j+xyz*k+last+1*index,6);
						cell17.setCellValue(rs.getTm());
						cell17.setCellStyle(styles.get("curr"));
						tmSum+=rs.getTm();
						
						HSSFCell cell18=getCell(sheet,4+28*j+xyz*k+last+1*index,7);
						cell18.setCellValue(rs.getWsite());
						cell18.setCellStyle(styles.get("curr"));
						wsSum+=rs.getWsite();
						
						totSum+=rs.getTotal();
						
						if(rs.getKet().contains("NB")&index==groupByKet.getLsGroupByKet().size()-1){
							agencyDelta=agencySum;
							bancasDelta=bancasSum;
							tmDelta=tmSum;
							wsDelta=wsSum;							
						}
						
						if(rs.getKet().contains("OS")&index==groupByKet.getLsGroupByKet().size()-1){
							agencyPlanTot=agencySum;
							bancasPlanTot=bancasSum;
							tmPlanTot=tmSum;
							wsPlanTot=wsSum;							
						}
						
						
						if(index==groupByKet.getLsGroupByKet().size()-1){
							if(rs.getKet().contains("BR")){
								agencyDelta-=agencySum;
								bancasDelta-=bancasSum;
								tmDelta-=tmSum;
								wsDelta-=wsSum;	
								
								totDelta=agencyDelta+bancasDelta+tmDelta+wsDelta;
								
								//delta
								sheet.addMergedRegion(new Region(4+28*j+xyz*k+last+1*index+3, (short) 2, 4+28*j+xyz*k+last+1*index+3, (short) 3));
								HSSFCell cell23a=getCell(sheet,4+28*j+xyz*k+last+1*index+3,2);
								cell23a.setCellValue("Delta - *");
								 
								 

								
								
								HSSFCell cell23b=getCell(sheet,4+28*j+xyz*k+last+1*index+3,4);
								cell23b.setCellValue(agencyDelta);
								cell23b.setCellStyle(styles.get("currTOT2"));
								
								HSSFCell cell23c=getCell(sheet,4+28*j+xyz*k+last+1*index+3,5);
								cell23c.setCellValue(bancasDelta);
								cell23c.setCellStyle(styles.get("currTOT2"));
								
								HSSFCell cell23d=getCell(sheet,4+28*j+xyz*k+last+1*index+3,6);
								cell23d.setCellValue(tmDelta);
								cell23d.setCellStyle(styles.get("currTOT2"));
								
								HSSFCell cell23e=getCell(sheet,4+28*j+xyz*k+last+1*index+3,7);
								cell23e.setCellValue(wsDelta);
								cell23e.setCellStyle(styles.get("currTOT2"));
								
								HSSFCell cell23f=getCell(sheet,4+28*j+xyz*k+last+1*index+3,8);
								cell23f.setCellValue(totDelta);
								cell23f.setCellStyle(styles.get("currTOT2"));
								
								//total
								
								agencyPlanTot+=agencyDelta;
								bancasPlanTot+=bancasDelta;
								tmPlanTot+=tmDelta;
								wsPlanTot+=wsDelta;	
								
								totPlanTot=agencyPlanTot+bancasPlanTot+tmPlanTot+wsPlanTot;
								sheet.addMergedRegion(new Region(4+28*j+xyz*k+last+1*index+5, (short) 0, 4+28*j+xyz*k+last+1*index+5, (short) 3));
								HSSFCell cell24=getCell(sheet,4+28*j+xyz*k+last+1*index+5,0);
								cell24.setCellValue(rs.getNm_plan()+" Total *");
								cell24.setCellStyle(styles.get("standar"));
								
								HSSFCell cell24a=getCell(sheet,4+28*j+xyz*k+last+1*index+5,4);
								cell24a.setCellValue(agencyPlanTot);
								cell24a.setCellStyle(styles.get("currTOT1"));
								
								HSSFCell cell24b=getCell(sheet,4+28*j+xyz*k+last+1*index+5,5);
								cell24b.setCellValue(bancasPlanTot);
								cell24b.setCellStyle(styles.get("currTOT1"));
								
								HSSFCell cell24c=getCell(sheet,4+28*j+xyz*k+last+1*index+5,6);
								cell24c.setCellValue(tmPlanTot);
								cell24c.setCellStyle(styles.get("currTOT1"));
								
								HSSFCell cell24d=getCell(sheet,4+28*j+xyz*k+last+1*index+5,7);
								cell24d.setCellValue(wsPlanTot);
								cell24d.setCellStyle(styles.get("currTOT1"));
								
								HSSFCell cell24e=getCell(sheet,4+28*j+xyz*k+last+1*index+5,8);
								cell24e.setCellValue(totPlanTot);
								cell24e.setCellStyle(styles.get("currTOT1"));
								
								//grand total
								agencyGrandTotal+=agencyPlanTot;
								bancasGrandTotal+=bancasPlanTot;
								tmGrandTotal+=tmPlanTot;
								wsGrandTotal+=wsPlanTot;
								
								agencyDeltaTotal+=agencyDelta;
								bancasDeltaTotal+=bancasDelta;
								tmDeltaTotal+=tmDelta;
								wsDeltaTotal+=wsDelta;
								
								if(j==lsGroupByPlan.size()-1){
									//grand total
									totGrandTotal+=agencyGrandTotal+bancasGrandTotal+tmGrandTotal+wsGrandTotal;
									sheet.addMergedRegion(new Region(4+28*j+xyz*k+last+1*index+7, (short) 0, 4+28*j+xyz*k+last+1*index+7, (short) 3));
									HSSFCell cell26=getCell(sheet,4+28*j+xyz*k+last+1*index+7,0);
									cell26.setCellValue("Grand Total *");
									cell26.setCellStyle(styles.get("titleRight"));
									
									
									HSSFCell cell26a=getCell(sheet,4+28*j+xyz*k+last+1*index+7,4);
									cell26a.setCellValue(agencyGrandTotal);
									cell26a.setCellStyle(styles.get("currTOT1"));
									
									HSSFCell cell26b=getCell(sheet,4+28*j+xyz*k+last+1*index+7,5);
									cell26b.setCellValue(bancasGrandTotal);
									cell26b.setCellStyle(styles.get("currTOT1"));
									
									HSSFCell cell26c=getCell(sheet,4+28*j+xyz*k+last+1*index+7,6);
									cell26c.setCellValue(tmGrandTotal);
									cell26c.setCellStyle(styles.get("currTOT1"));
									
									HSSFCell cell26d=getCell(sheet,4+28*j+xyz*k+last+1*index+7,7);
									cell26d.setCellValue(wsGrandTotal);
									cell26d.setCellStyle(styles.get("currTOT1"));
									
									HSSFCell cell26e=getCell(sheet,4+28*j+xyz*k+last+1*index+7,8);
									cell26e.setCellValue(totGrandTotal);
									cell26e.setCellStyle(styles.get("currTOT1"));
									
									//delta total
									sheet.addMergedRegion(new Region(4+28*j+xyz*k+last+1*index+9, (short) 0, 4+28*j+xyz*k+last+1*index+9, (short) 3));
									totDeltaTotal=agencyDeltaTotal+bancasDeltaTotal+tmDeltaTotal+wsDeltaTotal;
									HSSFCell cell27=getCell(sheet,4+28*j+xyz*k+last+1*index+9,0);
									cell27.setCellValue("Total DELTA");
									cell27.setCellStyle(styles.get("title"));
									
									
									HSSFCell cell27a=getCell(sheet,4+28*j+xyz*k+last+1*index+9,4);
									cell27a.setCellValue(agencyDeltaTotal);
									cell27a.setCellStyle(styles.get("currTOT1"));
									
									HSSFCell cell27b=getCell(sheet,4+28*j+xyz*k+last+1*index+9,5);
									cell27b.setCellValue(bancasDeltaTotal);
									cell27b.setCellStyle(styles.get("currTOT1"));
									
									HSSFCell cell27c=getCell(sheet,4+28*j+xyz*k+last+1*index+9,6);
									cell27c.setCellValue(tmDeltaTotal);
									cell27c.setCellStyle(styles.get("currTOT1"));
									
									HSSFCell cell27d=getCell(sheet,4+28*j+xyz*k+last+1*index+9,7);
									cell27d.setCellValue(wsDeltaTotal);
									cell27d.setCellStyle(styles.get("currTOT1"));
									
									HSSFCell cell27e=getCell(sheet,4+28*j+xyz*k+last+1*index+9,8);
									cell27e.setCellValue(totDeltaTotal);
									cell27e.setCellStyle(styles.get("currTOT1"));
									
								}
							}
							
							if(!rs.getKet().contains("OS")){
								HSSFCell cell19=getCell(sheet,4+28*j+xyz*k+last+1*index+1,4);
								cell19.setCellValue(agencySum);
								
								HSSFCell cell20=getCell(sheet,4+28*j+xyz*k+last+1*index+1,5);
								cell20.setCellValue(bancasSum);
								
								HSSFCell cell21=getCell(sheet,4+28*j+xyz*k+last+1*index+1,6);
								cell21.setCellValue(tmSum);
								
								HSSFCell cell22=getCell(sheet,4+28*j+xyz*k+last+1*index+1,7);
								cell22.setCellValue(wsSum);
								
								HSSFCell cell23=getCell(sheet,4+28*j+xyz*k+last+1*index+1,8);
								cell23.setCellValue(totSum);
								
								if(!rs.getKet().contains("BR")){
									cell19.setCellStyle(styles.get("currTOT1"));
									cell20.setCellStyle(styles.get("currTOT1"));
									cell21.setCellStyle(styles.get("currTOT1"));
									cell22.setCellStyle(styles.get("currTOT1"));
									cell23.setCellStyle(styles.get("currTOT1"));
								}else{
									cell19.setCellStyle(styles.get("currTOT2"));
									cell20.setCellStyle(styles.get("currTOT2"));
									cell21.setCellStyle(styles.get("currTOT2"));
									cell22.setCellStyle(styles.get("currTOT2"));
									cell23.setCellStyle(styles.get("currTOT2"));
								}
							}else{
								HSSFCell cell23=getCell(sheet,4+28*j+xyz*k+last+1*index,8);
								cell23.setCellValue(totSum);
								
								if(!rs.getKet().contains("BR")){								
									cell23.setCellStyle(styles.get("currTOT1"));
								}else{								
									cell23.setCellStyle(styles.get("currTOT2"));
								}
							}
						}
					}
					
					

				   

					
					last+=groupByKet.getLsGroupByKet().size();
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
