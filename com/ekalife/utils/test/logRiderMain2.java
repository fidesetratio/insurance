package com.ekalife.utils.test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.ReinsProd;
//import sms.Database;

//import com.ekalife.indosat.db.Db;

public class logRiderMain2 {
	protected final static Log logger = LogFactory.getLog( logRiderMain2.class );

	public static void main(String[] args) throws IOException, ParseException{
		BufferedReader in = null;
		SimpleDateFormat tgl=new SimpleDateFormat("MM/dd/yyyy");
//		private Log logger = LogFactory.getLog(Database.class);
		//daftar file log
		String[] daftarFile = new String[] {
				//"riderlink.txt"
//				"MainRider2.txt"
				"test.txt"
		};
		
		//directory tempat file log
		String dir = "D:/users/e-lion/";
//		String REG_SPAJ	,MSTE_INSURED_NO,	LSBS_ID	,LSDBS_NUMBER	,MSRPR_NUMBER,//(4)
//			   LKU_ID,	MSRPR_SIMULTAN,	MSRPR_TSI,	MSRPR_RESIKO_AWAL,	MSRPR_RETENSI,	//(9)
//			   MSRPR_TSI_REAS,	MSRPR_PA_CLASS,	MSRPR_PA_RISK,	MSRPR_EXTRA,	MSRPR_BEG_DATE, //(14)
//			   MSRPR_END_DATE,	MSRPR_PEMEGANG,	MSRPR_FLAG_PREMI,	MSRPR_SAR,	MSRPR_PREMIUM,	//(19)
//			   MSRPR_COMM,	MSRPR_STATUS_REAS,	MSRPR_ACTIVE,	MSRPR_CONVERT,	MSRPR_EXTRA_MORT,	//(24)
//			   MSRPR_EXTRA_HAMIL,	MRSPR_EXTRA_PA,	MSRPR_EXTRA_PA,	MSRPR_EXTRA_RISK,	//(28)
//			   MSRPR_COMMISION,	MSRPR_PRM_EXT_MORT,	MSRPR_COMM_EXT_MORT,	MSRPR_PRM_EXT_RISK,	
//			   MSRPR_COMM_EXT_RISK,	MSRPR_CONTRACT_YEAR;
		
		//baca file
		List<String> daftarUpdate = new ArrayList<String>();
		for(String namaFile : daftarFile) {
			try {
				in = new BufferedReader(new FileReader(dir+namaFile));
	
				String str;
				//Ambil dari file, hanya yang message sms saja
				while ((str = in.readLine()) != null) {
					int startIndex = 0;
					int endIndex = str.length();
					if(startIndex>-1) {
						daftarUpdate.add(str.substring(startIndex, endIndex));
	//				logger.info(str.substring(startIndex, endIndex));
						
					}
				}
			}finally {
				try {
					if(in != null) {
						in.close();
					}
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		}

		//Start Inserting
		// REG_SPAJ, MSTE_INSURED_NO, LSBS_ID, LSDBS_NUMBER, MSRPR_NUMBER,
	
		for(int d=1;d<daftarUpdate.size();d++){	
			  ReinsProd rp = new ReinsProd();
			  String tmp=daftarUpdate.get(d).toString();
			  String [] splitTemp= tmp.split("\t");
			  for(int e=0;e<splitTemp.length;e++){
				  
				  if(e==0){
					  rp.setReg_spaj(splitTemp[e]);//=splitTemp[e];
					  logger.info("REG_SPAJ "+e+ " "+splitTemp[e]);
				  }else if (e==1){
					  rp.setMste_insured_no(1);//MSTE_INSURED_NO=;
					  logger.info("MSTE_INSURED_NO "+e+ " "+splitTemp[e]);
				  }else if (e==2){
					  rp.setLsbs_id(Integer.parseInt(splitTemp[e]));//LSBS_ID=;
					  logger.info("LSBS_ID "+e+ " "+splitTemp[e]);
				  }else if (e==3){
					  rp.setLsdbs_number(Integer.parseInt(splitTemp[e]));//LSDBS_NUMBER=splitTemp[e];
					  logger.info("LSDBS_NUMBER "+e+ " "+splitTemp[e]);
				  }else if (e==4){
					  rp.setMsrpr_number(Integer.parseInt(splitTemp[e]));//()=splitTemp[e];
					  logger.info("MSRPR_NUMBER "+e+ " "+splitTemp[e]);
				  }else if (e==5){
					  rp.setLku_id("01");//LKU_ID=splitTemp[e];
					  logger.info("LKU_ID "+e+ " "+splitTemp[e]);
//				  }else if (e==6){
					  rp.setMsrpr_simultan(0.0);//=splitTemp[e];
//					  logger.info("MSRPR_SIMULTAN "+e+ " "+splitTemp[e]);
				  }else if (e==6){
					  rp.setMsrpr_tsi((new Double(splitTemp[e].replace(",", "").replace(".00", "").replace("\"", ""))));//MSRPR_TSI=splitTemp[e];
					  logger.info("MSRPR_TSI "+e+ " "+splitTemp[e]);
				  }else if (e==7){
					  rp.setMsrpr_resiko_awal((new Double(splitTemp[e].replace(",", "").replace(".00", "").replace("\"", ""))));//MSRPR_RESIKO_AWAL=splitTemp[e];
					  logger.info("MSRPR_RESIKO_AWAL "+e+ " "+splitTemp[e]);
				  }else if (e==8){
					  rp.setMsrpr_retensi((new Double(splitTemp[e].replace(",", "").replace(".00", "").replace("\"", ""))));//MSRPR_RETENSI=splitTemp[e];
					  logger.info("MSRPR_RETENSI "+e+ " "+splitTemp[e]);
//				  }else if (e==10){
					  rp.setMsrpr_tsi_reas(0.0);//MSRPR_TSI_REAS=splitTemp[e];
//					  logger.info("MSRPR_TSI_REAS "+e+ " "+splitTemp[e]);
//				  }else if (e==11){
					  rp.setMsrpr_pa_class(0);//MSRPR_PA_CLASS=splitTemp[e];
//					  logger.info("MSRPR_PA_CLASS "+e+ " "+splitTemp[e]);
//				  }else if (e==12){
					  rp.setMsrpr_pa_risk("0");//MSRPR_PA_CLASS=;
//					  logger.info("MSRPR_PA_CLASS "+e+ " "+splitTemp[e]);
//				  }else if (e==13){
					  rp.setMsrpr_extra(0.00);//=splitTemp[e];
//					  logger.info("MSRPR_PA_RISK "+e+ " "+splitTemp[e]);
				  }else if (e==9){
					  rp.setMsrpr_beg_date(tgl.parse(splitTemp[e]));//MSRPR_BEG_DATE=;
					  logger.info("MSRPR_BEG_DATE "+e+ " "+splitTemp[e]);
				  }else if (e==10){
					  rp.setMsrpr_end_date(tgl.parse(splitTemp[e]));//MSRPR_END_DATE=;
					  logger.info("MSRPR_END_DATE "+e+ " "+splitTemp[e]);
//				  }else if (e==16){
					  rp.setMsrpr_pemegang(0);//MSRPR_PEMEGANG=;
//					  logger.info("MSRPR_PEMEGANG "+e+ " "+splitTemp[e]);
//				  }else if (e==17){
					  rp.setMsrpr_flag_premi(1);//MSRPR_FLAG_PREMI=splitTemp[e];
//					  logger.info("MSRPR_FLAG_PREMI "+e+ " "+splitTemp[e]);
//				  }else if (e==18){
					  rp.setMsrpr_sar(0.0);//MSRPR_SAR=splitTemp[e];
//					  logger.info("MSRPR_SAR "+e+ " "+splitTemp[e]);
//				  }else if (e==19){
					  rp.setMsrpr_premium(0.0);//MSRPR_PREMIUM=splitTemp[e];
//					  logger.info("MSRPR_PREMIUM "+e+ " "+splitTemp[e]);
//				  }else if (e==20){
					  rp.setMsrpr_comm(0.00);//MSRPR_COMM=splitTemp[e];
//					  logger.info("MSRPR_COMM "+e+ " "+splitTemp[e]);
//				  }else if (e==21){
					  rp.setMsrpr_status_reas(0);//MSRPR_STATUS_REAS=splitTemp[e];
//					  logger.info("MSRPR_STATUS_REAS "+e+ " "+splitTemp[e]);
//				  }else if (e==22){
					  rp.setMsrpr_active(0);//MSRPR_ACTIVE=splitTemp[e];
//					  logger.info("MSRPR_ACTIVE "+e+ " "+splitTemp[e]);
//				  }else if (e==23){
					  rp.setMsrpr_convert(0);//MSRPR_CONVERT=splitTemp[e];
//					  logger.info("MSRPR_CONVERT "+e+ " "+splitTemp[e]);
//				  }else if (e==24){
					  rp.setMsrpr_extra_mort(0.0);//MSRPR_EXTRA_MORT=splitTemp[e];
//					  logger.info("MSRPR_EXTRA_MORT "+e+ " "+splitTemp[e]);
//				  }else if (e==25){
					  rp.setMsrpr_extra_hamil(0.0);//MSRPR_EXTRA_HAMIL=splitTemp[e];
//					  logger.info("MSRPR_EXTRA_HAMIL "+e+ " "+splitTemp[e]);
//				  }else if (e==26){
					  rp.setMrspr_extra_pa(0.0);//MRSPR_EXTRA_PA=splitTemp[e];
//					  logger.info("MRSPR_EXTRA_PA "+e+ " "+splitTemp[e]);
//				  }else if (e==27){
					  rp.setMsrpr_extra_pa(0.0);//MSRPR_EXTRA_PA=splitTemp[e];
//					  logger.info("MSRPR_EXTRA_PA "+e+ " "+splitTemp[e]);
//				  }else if (e==28){
					  rp.setMsrpr_extra_risk(0.0);//MSRPR_EXTRA_RISK=splitTemp[e];
//					  logger.info("MSRPR_EXTRA_RISK "+e+ " "+splitTemp[e]);
//				  }else if (e==29){
					  rp.setMsrpr_commision(0.0);//MSRPR_COMMISION=splitTemp[e];
//					  logger.info("MSRPR_COMMISION "+e+ " "+splitTemp[e]);
//				  }else if (e==30){
					  rp.setMsrpr_prm_ext_mort(0.0);//MSRPR_PRM_EXT_MORT=splitTemp[e];
//					  logger.info("MSRPR_PRM_EXT_MORT "+e+ " "+splitTemp[e]);
//				  }else if (e==31){
					  rp.setMsrpr_comm_ext_mort(0.0);//MSRPR_COMM_EXT_MORT=splitTemp[e];
//					  logger.info("MSRPR_COMM_EXT_MORT "+e+ " "+splitTemp[e]);
//				  }else if (e==32){
					  rp.setMsrpr_prm_ext_risk(0.0);//MSRPR_PRM_EXT_RISK=splitTemp[e];
//					  logger.info("MSRPR_PRM_EXT_RISK "+e+ " "+splitTemp[e]);
//				  }else if (e==33){
					  rp.setMsrpr_comm_ext_risk(0.0);//MSRPR_COMM_EXT_RISK=splitTemp[e];
//					  logger.info("MSRPR_COMM_EXT_RISK "+e+ " "+splitTemp[e]);
//				  }else if (e==34){
					  rp.setMsrpr_contract_year(0);//MSRPR_CONTRACT_YEAR=splitTemp[e];
//					  logger.info("MSRPR_CONTRACT_YEAR "+e+ " "+splitTemp[e]);
//				  }
//				  logger.info("kolomke "+e+ " "+splitTemp[e]);
				  
//				  insertMstReinsProduct(rp.getReg_spaj(),rp.getMste_insured_no(),
//						  rp.getLsbs_id(),rp.getLsdbs_number(),rp.getMsrpr_number(),//(4)
//						   rp.getLku_id(),	rp.getMsrpr_simultan(),	rp.getMsrpr_tsi(),	
//						   rp.getMsrpr_resiko_awal(),	rp.getMsrpr_retensi(),	//(9)
//						   rp.getMsrpr_tsi_reas(),	rp.getMsrpr_pa_class(),	rp.getMsrpr_pa_risk(),
//						   rp.getMsrpr_extra(),	rp.getMsrpr_beg_date(), //(14)
//						   rp.getMsrpr_end_date(),	rp.getMsrpr_pemegang(),	rp.getMsrpr_flag_premi(),
//						   rp.getMsrpr_sar(),	rp.getMsrpr_premium(),	//(19)
//						   rp.getMsrpr_comm(),	rp.getMsrpr_status_reas(),	rp.getMsrpr_active(),
//						   rp.getMsrpr_convert(),	rp.getMsrpr_extra_mort(),	//(24)
//						   rp.getMsrpr_extra_hamil(),	rp.getMrspr_extra_pa(),	rp.getMsrpr_extra_pa(),
//						   rp.getMsrpr_extra_risk(),rp.getMsrpr_commision(),	rp.getMsrpr_prm_ext_mort(),	rp.getMsrpr_comm_ext_mort(),
//						   rp.getMsrpr_prm_ext_risk(),rp.getMsrpr_comm_ext_risk(),	rp.getMsrpr_contract_year());
			  }
			  }
			  	insertMstReinsProduct(rp);
			}
	}

	
	public static Integer insertMstReinsProduct(ReinsProd rp){
	
		String url="jdbc:oracle:thin:@ebtest:1521:ekatest";
		String driver="oracle.jdbc.driver.OracleDriver";
		String user="dev";
		String pass="linkdev";
		
//		! EkaTest
//		jdbc.name=ekatest
//		jdbc.url=jdbc:oracle:thin:@ebtest:1521:ekatest
//
//	! Eka8i
//		#jdbc.name=eka8i
//		#jdbc.url=jdbc:oracle:thin:@itaniumdb:1521:ajsdb
//
//	! Setting Lainnya SAMA
//		jdbc.program=elions
//		jdbc.driver=oracle.jdbc.driver.OracleDriver
//		jdbc.user=dev
//		jdbc.password=linkdev
//		String query="insert into indosat.lst_history"+
//		 "(LSH_MSISDN,LSH_GUID,"+
//		 "LSH_MESSAGEIN,LSH_OPERATOR,LSH_TRXTIME,LSH_SCD,"+
//		 "LSH_TRXID,LSH_DATEINQUEUE,LSH_STATUS)"+
//		 "values (?,?,?,?,?,?,?,?,?)";		
		
		String query="insert into eka.mst_reins_product"+ 
			" (reg_spaj	,mste_insured_no,	lsbs_id	,lsdbs_number	,msrpr_number,"+
					   "lku_id,	msrpr_simultan,	msrpr_tsi,	msrpr_resiko_awal,	msrpr_retensi,"+
					   "msrpr_tsi_reas,	msrpr_pa_class,	msrpr_pa_risk,	msrpr_extra,	msrpr_beg_date, "+
					   "msrpr_end_date,	msrpr_pemegang,	msrpr_flag_premi,	msrpr_sar,	msrpr_premium,"+
					   "msrpr_comm,	msrpr_status_reas,	msrpr_active,	msrpr_convert,	msrpr_extra_mort,"+	
					   "msrpr_extra_hamil,	mrspr_extra_pa,	msrpr_extra_pa,	msrpr_extra_risk,	"+
					   "msrpr_commision,	msrpr_prm_ext_mort,	msrpr_comm_ext_mort,	msrpr_prm_ext_risk,	"+
					   "msrpr_comm_ext_risk,	msrpr_contract_year) "+
				"values(?,?,?,?,?,?,?,?,?,?," +
				"		?,?,?,?,to_date(?,'dd/MM/YYYY'),to_date(?,'dd/MM/YY'),?,?,?,?," +
				"		?,?,?,?,?,"+
					  "?,?,?,?,?,?,?,?,?,?)";
		
//		String query="INSERT INTO EKA.MST_REINS_PRODUCT "+ 
//        		   "(REG_SPAJ, MSTE_INSURED_NO,  LSBS_ID,LSDBS_NUMBER,MSRPR_NUMBER,LKU_ID, "+  
//		           "MSRPR_SIMULTAN,MSRPR_TSI,MSRPR_RESIKO_AWAL,MSRPR_RETENSI,MSRPR_TSI_REAS,MSRPR_PA_CLASS, "+   
//		           "MSRPR_PA_RISK,MSRPR_EXTRA_MORT,MSRPR_EXTRA_RISK,MSRPR_BEG_DATE,MSRPR_END_DATE,MSRPR_PEMEGANG, "+   
//		           "MSRPR_FLAG_PREMI,MSRPR_SAR,MSRPR_PREMIUM,MSRPR_COMMISION,MSRPR_PRM_EXT_MORT,MSRPR_COMM_EXT_MORT,  "+  
//		           "MSRPR_PRM_EXT_RISK,MSRPR_COMM_EXT_RISK,MSRPR_CONTRACT_YEAR)  "+ 
//		 " VALUES ( ?,?,?,?,?,?, "+   
//		          " ?, ?,?,?,?,?, "+   
//		          "?, ?, ?, to_date(?,'dd/MM/YY'),to_date(?,'dd/MM/YY'),?, "+   
//		          "?,?,?,?,?,?,"+    
//		          "?, ?)";


		Connection con = null;
		PreparedStatement pstmt= null;
		try{
			Class.forName(driver);			
			con = DriverManager.getConnection(url,user,pass);
			//Statement st = con.createStatement(1004,1008);			
			//st.executeUpdate(query);
			SimpleDateFormat tgl=new SimpleDateFormat("MM/dd/yyyy");
			pstmt= con.prepareStatement(query);
			pstmt.setString(1, rp.getReg_spaj());
			pstmt.setInt(2, rp.getMste_insured_no());
			pstmt.setInt(3,rp.getLsbs_id());
			pstmt.setInt(4, rp.getLsdbs_number());
			pstmt.setInt(5, rp.getMsrpr_number());
			pstmt.setString(6, rp.getLku_id());
			pstmt.setDouble(7,0.0);
			pstmt.setDouble(8,rp.getMsrpr_tsi());
			pstmt.setDouble(9, rp.getMsrpr_resiko_awal());
			pstmt.setDouble(10, rp.getMsrpr_retensi());
			pstmt.setDouble(11, 0.0);
			pstmt.setInt(12,0);
			pstmt.setString(13,"0");
			pstmt.setDouble(14, 0.0);
			pstmt.setString(15, tgl.format(rp.getMsrpr_beg_date()));
			pstmt.setString(16, tgl.format(rp.getMsrpr_end_date()));
			pstmt.setInt(17,0);
			pstmt.setInt(18,0);
			pstmt.setDouble(19, 0.0);
			pstmt.setDouble(20, 0.0);
			pstmt.setDouble(21,0.0);
			pstmt.setInt(22, 0);
			pstmt.setInt(23, 0);
			pstmt.setInt(24, 0);
			pstmt.setDouble(25, 0.0);
			pstmt.setDouble(26,0.0);
			pstmt.setDouble(27, 0.0);
			pstmt.setDouble(28, 0.0);
			pstmt.setDouble(29, 0.0);
			pstmt.setDouble(30,0.0);
			pstmt.setDouble(31, 0.0);
			pstmt.setDouble(32, 0.0);
			pstmt.setDouble(33,0.0);
			pstmt.setInt(34,0 );
			pstmt.setInt(35,0 );
			pstmt.executeUpdate();	
//			con.close();
		}catch(Exception e){
			logger.error("ERROR :", e);
		}finally {
            try {
                if(con != null) {
                	con.close();
                }
                if(pstmt != null) {
                	pstmt.close();
                }
          }catch (Exception e) {
                logger.error("ERROR :", e);
          }
		}

		return null;

	}	
}