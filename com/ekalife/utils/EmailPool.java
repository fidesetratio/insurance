package com.ekalife.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileCopyUtils;

import com.ekalife.elions.dao.UwDao;
import com.ekalife.elions.model.Email;
import com.ekalife.elions.process.Sequence;
import com.ekalife.utils.parent.ParentDao;

public class EmailPool extends ParentDao {
	
	protected static final Log logger = LogFactory.getLog( EmailPool.class );
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
	private static UwDao uwDao;
	private static Sequence sequence;
	
	public static Boolean send(String me_id, String system_name, Integer show_system_name, Integer show_footer, Integer mec_id, Integer me_status, String me_status_message, 
					 Integer me_count, Integer lus_id, Date me_input_date, Date me_sent_date, boolean isHtml, String from, 
					 String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments, Integer lje_id) {
		String desc	= "OK";
		try{
			Email email = new Email();
			String emailTo =null;
			String emailCc =null;
			String emailBcc =null;
			Integer me_html = 1;
			email.setMe_id(me_id);
			email.setMe_system(system_name);
			email.setMe_show_system(show_system_name);
			email.setMe_show_footer(show_footer); 
			email.setMe_from(from);
			for(int i=0;i<to.length;i++){
                if (!to[i].toLowerCase().contains("@agency")){
                    if(i==0){
                        emailTo = to[i]+";";
                    }else{
                        emailTo += to[i]+";";
                    }
                }
            }
            if(cc!=null)for(int i=0;i<cc.length;i++){
                if (!cc[i].toLowerCase().contains("@agency")){
                    if(i==0){
                        emailCc = cc[i]+";";
                    }else{
                        emailCc += cc[i]+";";
                    }
                }
            }
            if(bcc!=null)for(int i=0;i<bcc.length;i++){
                if (!bcc[i].toLowerCase().contains("@agency")){
                    if(i==0){
                        emailBcc = bcc[i]+";";
                    }else{
                        emailBcc += bcc[i]+";";
                    }
                }
            }
			email.setMe_to(emailTo);
			email.setMe_cc(emailCc);
			email.setMe_bcc(emailBcc);
			email.setMe_subject(subject);
			email.setMe_message(message);
			email.setMec_id(mec_id);
			if(isHtml==false)me_html=0;
			email.setMe_html(me_html);
			email.setMe_status(me_status);
			email.setMe_status_message(me_status_message);
			email.setMe_count(me_count);
			email.setMe_lus_id(lus_id);
			email.setMe_input_date(me_input_date);
			email.setMe_sent_date(me_sent_date);
			email.setLje_id(lje_id);
			if(!Common.isEmpty(from)) {
				if(from.contains("@")){
					uwDao.insertMstEmail(email);
				}
			}
			
			if(attachments!=null){
			//ATTACHMENT==============================
				Integer months = me_input_date.getMonth()+1;
				Integer years = me_input_date.getYear()+1900;
				String outputDir = props_static.getProperty("attachment.mailpool.dir") +"\\" +years+"\\"+FormatString.rpad("0", months.toString(), 2)+"\\"+me_id;
				File dirFile = new File(outputDir);
				if(!dirFile.exists()) dirFile.mkdirs();
				//save file
				for(File file : attachments){
					File outputFile = new File(outputDir +"/"+ file.getName());
				    FileCopyUtils.copy(file, outputFile);
				}
			//=========================================
			}
			return true;
		}catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			return false;
		}
		
	}
	
	public static Boolean send(String system_name, Integer show_system_name, Integer show_footer, Integer mec_id, Integer me_status, String me_status_message, 
            Integer me_count, Integer lus_id, Date me_input_date, Date me_sent_date, boolean isHtml, String from, 
            String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments, String reg_spaj) {
	    
	    return send(system_name, show_system_name, show_footer, mec_id, me_status, me_status_message, me_count, lus_id, me_input_date, me_sent_date, isHtml, from, to, cc, bcc, subject, message, attachments, reg_spaj, 0);
	    
	}
	
	public static Boolean send(String system_name, Integer show_system_name, Integer show_footer, Integer mec_id, Integer me_status, String me_status_message, 
			 					Integer me_count, Integer lus_id, Date me_input_date, Date me_sent_date, boolean isHtml, String from, 
			 					String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments, String reg_spaj, Integer lje_id) {
		String desc	= "OK";
		try{
			Email email = new Email();
			String emailTo =null;
			String emailCc =null;
			String emailBcc =null;
			Integer me_html = 1;
			String me_id = uwDao.selectSeqEmailId();
			email.setMe_id(me_id);
			email.setMe_system(system_name);
			email.setMe_show_system(show_system_name);
			email.setMe_show_footer(show_footer); 
			email.setMe_from(from);
			for(int i=0;i<to.length;i++){
				if (!to[i].toLowerCase().contains("@agency")){
					if(i==0){
						emailTo = to[i]+";";
					}else{
						emailTo += to[i]+";";
					}
				}
			}
			if(cc!=null)for(int i=0;i<cc.length;i++){
				if (!cc[i].toLowerCase().contains("@agency")){
					if(i==0){
						emailCc = cc[i]+";";
					}else{
						emailCc += cc[i]+";";
					}
				}
			}
			if(bcc!=null)for(int i=0;i<bcc.length;i++){
				if (!bcc[i].toLowerCase().contains("@agency")){
					if(i==0){
						emailBcc = bcc[i]+";";
					}else{
						emailBcc += bcc[i]+";";
					}
				}
			}
			email.setMe_to(emailTo);
			email.setMe_cc(emailCc);
			email.setMe_bcc(emailBcc);
			email.setMe_subject(subject);
			email.setMe_message(message);
			email.setMec_id(mec_id);
			if(isHtml==false)me_html=0;
			email.setMe_html(me_html);
			email.setMe_status(me_status);
			email.setMe_status_message(me_status_message);
			email.setMe_count(me_count);
			email.setMe_lus_id(lus_id);
			email.setMe_input_date(me_input_date);
			email.setMe_sent_date(me_sent_date);
			email.setMe_reg_spaj(reg_spaj);
			email.setLje_id(lje_id);
			if(!Common.isEmpty(from)) {
				if(from.contains("@")){
					uwDao.insertMstEmail(email);
				}
			}
			
			if(attachments!=null){
			//ATTACHMENT==============================
				Integer months = me_input_date.getMonth()+1;
				Integer years = me_input_date.getYear()+1900;
				String outputDir = props_static.getProperty("attachment.mailpool.dir") +"\\" +years+"\\"+FormatString.rpad("0", months.toString(), 2)+"\\"+me_id;
				File dirFile = new File(outputDir);
				if(!dirFile.exists()) dirFile.mkdirs();
				//save file
				for(File file : attachments){
					File outputFile = new File(outputDir +"/"+ file.getName());
				    FileCopyUtils.copy(file, outputFile);
				}
			//=========================================
			}
			return true;
		}catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			return false;
		}
		
		}
	
	public void setUwDao(UwDao uwDao) {
		this.uwDao = uwDao;
	}
	
	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}
}
