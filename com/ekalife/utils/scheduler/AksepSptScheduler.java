package com.ekalife.utils.scheduler;


import id.co.sinarmaslife.std.util.PDFToImage;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ekalife.elions.dao.SchedulerDao;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.parent.ParentScheduler;

public class AksepSptScheduler extends ParentScheduler {
	
	private SchedulerDao schedulerDao;

	public void main() throws Exception{		
		
		
		
		Date bdate 	= new Date();
		String desc	= "OK";	
		String err="";
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVA") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64") || 
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSClUS1"))
				) {	
			    
			    int aksepSpt=uwManager.schedulerSPT();	
			    Date nowDate = elionsManager.selectSysdateSimple();
		 		
		 		Integer statusProses;
				if(aksepSpt == 1){			    	
					try{				
							
							try {		        				
					        	uwManager.insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
					        	"SCHEDULER REMINDER AKSEPTASI SPT", bdate, new Date(), desc,err);
					    	}
					    	catch (UnknownHostException e) {
					    		//logger.error("ERROR :", e);
					    	}
				            ArrayList<Map> spaj = Common.serializableList(uwManager.selectMstPositionSpajCekStatus());
							ArrayList tampungspaj = new ArrayList<String>();					
							ArrayList tampungNamaUser=new ArrayList<String>();
							ArrayList tampungEmailUser=new ArrayList<String>();
							ArrayList tampungDeptuser=new ArrayList<String>();
							String from = props .getProperty("admin.ajsjava");
					    	 	
							for(int i=0;i<spaj.size();i++){
								Map mapSpaj= (Map)spaj.get(i);
								String spajtampung= (String) mapSpaj.get("REG_SPAJ");
								String usertampung= (String) mapSpaj.get("LUS_LOGIN_NAME");
								String emailtampung= (String) mapSpaj.get("LUS_EMAIL");
								String depttampung= (String) mapSpaj.get("LDE_ID");
								if(spajtampung !=null ){
									tampungspaj.add(spajtampung);							
									tampungNamaUser.add(usertampung);
									tampungEmailUser.add(emailtampung);
									tampungDeptuser.add(depttampung);
								}
							}
							for(int j=0;j<tampungspaj.size();j++){
								 	String reg_spaj=(String) tampungspaj.get(j);
								 	reg_spaj=reg_spaj.trim();
								 	String user=(String) tampungNamaUser.get(j);
								 	String email=(String) tampungEmailUser.get(j);
								 	String dept=(String) tampungDeptuser.get(j);
								 	String lca_id = uwManager.selectCabangFromSpaj(reg_spaj);
								 	String dir = props.getProperty("pdf.dir.export") + "//" +lca_id;
								 	
									File destDir = new File(dir + "//" + reg_spaj.trim());
									String destSPT = destDir.getPath() +"//";
									String filename=reg_spaj.trim()+"SPT"+"1"+".jpg";
									String lus_id=uwManager.selectDireksi(reg_spaj);
									String namaDireksi = "";
									File src = null;
									src = new File(destSPT +filename);
									String fileSPT="SPT_"+reg_spaj.trim()+"_001.pdf";
									String fileGambarSPT=reg_spaj.trim()+"SPT";
									String subjek="<b>Email ini adalah REMINDER untuk permohonan akseptasi SPT di atas,  yang  BELUM di aksep.</b>";
									if(!src.exists()){										
										PDFToImage.convertPdf2Image(destSPT,  destSPT, fileSPT, fileGambarSPT,null,null);
									}
									User currentUser = new User();
									currentUser.setName(user);
									currentUser.setPass("Ajs123456");
									currentUser.setLus_nik("1");
									
									
									if(dept.equals("11")){
										statusProses  =uwManager.kirimEmailPermohonanSPT(reg_spaj,currentUser,fileSPT,destSPT,fileGambarSPT,new Integer(1),subjek);
									}else{
										statusProses  =uwManager.kirimEmailPermohonanSPT(reg_spaj,currentUser,fileSPT,destSPT,fileGambarSPT,new Integer(2),subjek);
									}
			
							 }						
					   }catch (Exception e) {
							desc = "ERROR";
							err=e.getLocalizedMessage();
							EmailPool.send(bacManager.selectSeqEmailId(),"SMiLe E-Lions", 1, 1, 0, 0, 
									null, 0, 0, nowDate, null, true, "ajsjava@sinarmasmsiglife.co.id",												
									new String[]{"ryan@sinarmasmsiglife.co.id"}, 
									null, 
									null, 
									"Error Scheduler SPT", 
									"Scheduler SPT Gak jalan Tolong dicek"+""+err, 
									null,15);
							logger.error("ERROR :", e);
						}	        			
						
			     }	
			}
			
	  }
}
								
								
	
