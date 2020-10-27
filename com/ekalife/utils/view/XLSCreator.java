/**
 * @author  : Ferry Harlim
 * @created : Feb 12, 2007 
 */
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

import com.ekalife.elions.model.Temp;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;

public class XLSCreator extends AbstractExcelView{
	@Override
	protected void buildExcelDocument(Map model, HSSFWorkbook workBook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HSSFSheet sheet = workBook.createSheet("Reas");
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
		setText(cell16,"CLASS");
		setText(cell17,"BASIC PLAN");
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
			cell16=getCell(sheet, i+1, 16);
			cell17=getCell(sheet, i+1, 17);
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
			setText(cell0, (i+1)+"");
			setText(cell1, FormatString.nomorPolis(((Temp)lsHasil.get(i)).getMspo_policy_no()));
			setText(cell2, FormatString.nomorSPAJ(((Temp)lsHasil.get(i)).getReg_spaj()));
			setText(cell3, ((Temp)lsHasil.get(i)).getMcl_first_p());
			setText(cell4, ((Temp)lsHasil.get(i)).getMcl_first_t());
			setText(cell5, ((Temp)lsHasil.get(i)).getMspe_sex_p().toString());
			setText(cell6, ((Temp)lsHasil.get(i)).getMspe_sex_t().toString());
			if(((Temp)lsHasil.get(i)).getMspe_date_birth_p()==null)
				setText(cell7, "00/00/0000");
			else
				setText(cell7, FormatDate.toString(((Temp)lsHasil.get(i)).getMspe_date_birth_p()));
			//
			if(((Temp)lsHasil.get(i)).getMspe_date_birth_t()==null)
				setText(cell8, "00/00/0000");
			else
				setText(cell8, FormatDate.toString(((Temp)lsHasil.get(i)).getMspe_date_birth_t()));
			//
			setText(cell9, ((Temp)lsHasil.get(i)).getMspo_age().toString());
			setText(cell10, ((Temp)lsHasil.get(i)).getMste_age().toString());
			if(((Temp)lsHasil.get(i)).getMspr_beg_date()==null)
				setText(cell11, "00/00/0000");
			else
				setText(cell11, FormatDate.toString(((Temp)lsHasil.get(i)).getMspr_beg_date()));
			//
			if(((Temp)lsHasil.get(i)).getMspr_end_date()==null)
				setText(cell12, "00/00/0000");
			else
				setText(cell12, FormatDate.toString(((Temp)lsHasil.get(i)).getMspr_end_date()));
			//
			setText(cell13, ((Temp)lsHasil.get(i)).getMspo_pay_period().toString());
			setText(cell14, ((Temp)lsHasil.get(i)).getMspo_ins_period().toString());
			setText(cell15, ((Temp)lsHasil.get(i)).getMste_medical().toString());
			if(((Temp)lsHasil.get(i)).getMspr_class()!=null)
				setText(cell16, ((Temp)lsHasil.get(i)).getMspr_class().toString());
			else
				setText(cell16, "");
			setText(cell17, ((Temp)lsHasil.get(i)).getLsbs_id1().toString());
			setText(cell18, ((Temp)lsHasil.get(i)).getLsbs_id2().toString());
			setText(cell19, ((Temp)lsHasil.get(i)).getLku_id().toString());
			setText(cell20, nf.format(((Temp)lsHasil.get(i)).getMspr_tsi()));
			setText(cell21, "");
			setText(cell22, nf.format(((Temp)lsHasil.get(i)).getRetensi()));
			setText(cell23, nf.format(((Temp)lsHasil.get(i)).getReas()));
			setText(cell24, ((Temp)lsHasil.get(i)).getLsdbs_name());
			if(((Temp)lsHasil.get(i)).getMste_reas()!=null){
				if(((Temp)lsHasil.get(i)).getMste_reas()==1)
					setText(cell25, "Treaty");
				else if(((Temp)lsHasil.get(i)).getMste_reas()==2)
					setText(cell25, "Facultative");
				else
					setText(cell25, "Non Reas");
			}else
				setText(cell25, "Non Reas");
			
			setText(cell26, ((Temp)lsHasil.get(i)).getLscb_pay_mode().toString());
			
//			//
//			setText(cell0, ((Temp)lsHasil.get(i)).getNo().toString());
//			setText(cell1, FormatString.nomorSPAJ(((Temp)lsHasil.get(i)).getReg_spaj()));
//			setText(cell2, FormatString.nomorPolis(((Temp)lsHasil.get(i)).getMspo_policy_no()));
//			setText(cell3, ((Temp)lsHasil.get(i)).getMcl_first());
//			if(((Temp)lsHasil.get(i)).getMspr_beg_date()==null)
//				setText(cell4, "00/00/0000");
//			else
//				setText(cell4, FormatDate.toString(((Temp)lsHasil.get(i)).getMspr_beg_date()));
//			setText(cell5, ((Temp)lsHasil.get(i)).getLsdbs_name());
//			setText(cell6, ((Temp)lsHasil.get(i)).getLsbs_id1().toString());
//			setText(cell7, ((Temp)lsHasil.get(i)).getLsbs_id2().toString());
//			setText(cell8, ((Temp)lsHasil.get(i)).getMste_age().toString());
//			setText(cell9, ((Temp)lsHasil.get(i)).getMste_medical().toString());
//			setText(cell10, nf.format(((Temp)lsHasil.get(i)).getMspr_tsi()));
//			setText(cell11, nf.format(((Temp)lsHasil.get(i)).getRetensi()));
//			setText(cell12, nf.format(((Temp)lsHasil.get(i)).getReas()));
		}
	}
	
}
