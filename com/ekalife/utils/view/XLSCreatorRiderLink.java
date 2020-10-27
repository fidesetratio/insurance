package com.ekalife.utils.view;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.ekalife.elions.model.reas.DataRiderLink;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;

public class XLSCreatorRiderLink extends AbstractExcelView {
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workBook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HSSFSheet sheet = workBook.createSheet("Rider Link");
		sheet.setDefaultColumnWidth((short)12);
		NumberFormat nf=new DecimalFormat("#,##0.00");
		List lsHasil=(List)model.get("lsHasil");
		HSSFCell cell0=getCell(sheet,0,0);
		HSSFCell cell1=getCell(sheet,0,1);
		HSSFCell cell2=getCell(sheet,0,2);
		HSSFCell cell3=getCell(sheet,0,3);
		HSSFCell cell4=getCell(sheet,0,4);
		HSSFCell cell5=getCell(sheet,0,5);
		HSSFCell cell6=getCell(sheet,0,6);
		HSSFCell cell7=getCell(sheet,0,7);
		HSSFCell cell8=getCell(sheet,0,8);
		HSSFCell cell9=getCell(sheet,0,9);
		HSSFCell cell10=getCell(sheet,0,10);
		HSSFCell cell11=getCell(sheet,0,11);
		HSSFCell cell12=getCell(sheet,0,12);
		HSSFCell cell13=getCell(sheet,0,13);
		HSSFCell cell14=getCell(sheet,0,14);
		HSSFCell cell15=getCell(sheet,0,15);
		HSSFCell cell16=getCell(sheet,0,16);
		HSSFCell cell17=getCell(sheet,0,17);
		HSSFCell cell18=getCell(sheet,0,18);
		HSSFCell cell19=getCell(sheet,0,19);
		HSSFCell cell20=getCell(sheet,0,20);
		HSSFCell cell21=getCell(sheet,0,21);
		HSSFCell cell22=getCell(sheet,0,22);
		HSSFCell cell23=getCell(sheet,0,23);
		HSSFCell cell24=getCell(sheet,0,24);
		HSSFCell cell25=getCell(sheet,0,25);
		HSSFCell cell26=getCell(sheet,0,26);
		HSSFCell cell27=getCell(sheet,0,27);
		HSSFCell cell28=getCell(sheet,0,28);
		HSSFCell cell29=getCell(sheet,0,29);
		HSSFCell cell30=getCell(sheet,0,30);
		//
		setText(cell0, "NO.");
		setText(cell1, "NO. POLIS");
		setText(cell2, "NO. SPAJ");
		setText(cell3, "NAMA PEMEGANG");
		setText(cell4, "NAMA TERTANGGUNG");
		setText(cell5, "SEX HOLDER");
		setText(cell6, "SEX INSURED");
		setText(cell7, "BIRTH DATE HLD");
		setText(cell8, "BIRTH DATE INS");
		setText(cell9, "AGE HLD");
		setText(cell10,"AGE INS");
		setText(cell11,"BEG DATE");
		setText(cell12,"END DATE");
		setText(cell13,"INS PERIOD");
		setText(cell14,"PAY PERIOD");
		setText(cell15,"MEDICAL");
		//setText(cell16,"CLASS");
		//setText(cell17,"BASIC PLAN");
		setText(cell18,"PLAN");
		setText(cell19,"CURR");
		setText(cell20,"TSI");
		setText(cell21,"****");
		setText(cell22,"RETENSI");
		setText(cell23,"TSI REAS");
		setText(cell24,"PRODUCT NAME");
		setText(cell25,"TYPE REINSURANCE");
		setText(cell26,"CARA BAYAR");
		
		for(int i=0;i<lsHasil.size();i++){
			cell0=getCell(sheet, i+1, 0);
			cell1=getCell(sheet, i+1, 1);
			cell2=getCell(sheet, i+1, 2);
			cell3=getCell(sheet, i+1, 3);
			cell4=getCell(sheet, i+1, 4);
			cell5=getCell(sheet, i+1, 5);
			cell6=getCell(sheet, i+1, 6);
			cell7=getCell(sheet, i+1, 7);
			cell8=getCell(sheet, i+1, 8);
			cell9=getCell(sheet, i+1, 9);
			cell10=getCell(sheet, i+1, 10);
			cell11=getCell(sheet, i+1, 11);
			cell12=getCell(sheet, i+1, 12);
			cell13=getCell(sheet, i+1, 13);
			cell14=getCell(sheet, i+1, 14);
			cell15=getCell(sheet, i+1, 15);
			//cell16=getCell(sheet, i+1, 16);
			//cell17=getCell(sheet, i+1, 17);
			cell18=getCell(sheet, i+1, 18);
			cell19=getCell(sheet, i+1, 19);
			cell20=getCell(sheet, i+1, 20);
			cell21=getCell(sheet, i+1, 21);
			cell22=getCell(sheet, i+1, 22);
			cell23=getCell(sheet, i+1, 23);
			cell24=getCell(sheet, i+1, 24);
			cell25=getCell(sheet, i+1, 25);
			cell26=getCell(sheet, i+1, 26);
			//
			DataRiderLink dataRider=(DataRiderLink)lsHasil.get(i);
			
			setText(cell0, (i+1)+"");
			setText(cell1, FormatString.nomorPolis(dataRider.getMspo_policy_no()));
			setText(cell2, FormatString.nomorSPAJ(dataRider.getReg_spaj()));
			setText(cell3, dataRider.getNama_pemegang());
			setText(cell4, dataRider.getNama_tertanggung());
//			if(dataRider.getReg_spaj().equals("46200700022"))
//				JOptionPane.showMessageDialog(null, "tes");
			if(dataRider.getSex_holder()!=null)
				setText(cell5, dataRider.getSex_holder().toString());
			else
				setText(cell5, null);
			if(dataRider.getSex_insured()!=null)
				setText(cell6, dataRider.getSex_insured().toString());
			else
				setText(cell6, null);
			
			if(dataRider.getBirth_date_hld()==null)
				setText(cell7, null);
			else
				setText(cell7, FormatDate.toString(dataRider.getBirth_date_hld()));
			//
			if(((DataRiderLink)lsHasil.get(i)).getBirth_date_ins()==null)
				setText(cell8, null);
			else
				setText(cell8, FormatDate.toString(((DataRiderLink)lsHasil.get(i)).getBirth_date_ins()));
			if(dataRider.getAge_hld()!=null)
				setText(cell9, (dataRider.getAge_hld().toString()));
			else
				setText(cell9, null);
			if(dataRider.getAge_ins()!=null)
				setText(cell10, (dataRider.getAge_ins().toString()));
			else
				setText(cell10, null);
			
			if(dataRider.getMspr_beg_date()==null)
				setText(cell11, null);
			else
				setText(cell11, FormatDate.toString(dataRider.getMspr_beg_date()));
			//
			if(dataRider.getMspr_end_date()==null)
				setText(cell12, null);
			else
				setText(cell12, FormatDate.toString(dataRider.getMspr_end_date()));
			
			setText(cell13, dataRider.getMspr_ins_period().toString());
			if(dataRider.getMspo_pay_period()!=null)
				setText(cell14, dataRider.getMspo_pay_period().toString());
			else
				setText(cell14, null);
			if(dataRider.getMste_medical()!=null)
				setText(cell15, dataRider.getMste_medical().toString());
			else
				setText(cell15, null);
			
			setText(cell18, dataRider.getLsbs_id().toString());
			setText(cell19, dataRider.getLku_id().toString());
			
			if(dataRider.getLsbs_id()<300){
				if(dataRider.getTsi_life()!=null)
					setText(cell20, nf.format(dataRider.getTsi_life()));
				else
					setText(cell20, null);
				if(dataRider.getSar_life()!=null)
					setText(cell21, nf.format(dataRider.getSar_life()));
				else
					setText(cell21, nf.format(dataRider.getSar_life()));
				if(dataRider.getRetensi_life()!=null)
					setText(cell22, nf.format(dataRider.getRetensi_life()));
				else
					setText(cell22, null);
				if(dataRider.getReas_life()!=null)
					setText(cell23, nf.format(dataRider.getReas_life()));
				else
					setText(cell23, null);
			}else{
				setText(cell20, nf.format(dataRider.getTsi()));
				setText(cell21, nf.format(dataRider.getSar()));
				if(dataRider.getRetensi()!=null)
					setText(cell22, nf.format(dataRider.getRetensi()));
				else
					setText(cell22, null);
				setText(cell23, nf.format(dataRider.getReas()));
			}
			setText(cell24, dataRider.getLsdbs_name());
			setText(cell25, dataRider.getTipe_reas());
			if(dataRider.getLscb_id()!=null)
				setText(cell26, dataRider.getLscb_id().toString());
			else
				setText(cell26, null);

			
		}
	}

}
