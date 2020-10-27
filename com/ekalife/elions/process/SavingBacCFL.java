package com.ekalife.elions.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.ekalife.elions.dao.BacDao;
import com.ekalife.elions.dao.UwDao;
import com.ekalife.elions.model.Agentrec;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Biayainvestasi;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.DetailPembayaran;
import com.ekalife.elions.model.DetilInvestasi;
import com.ekalife.elions.model.Hcp;
import com.ekalife.elions.model.Kesehatan;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.ReffBii;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Simas;
import com.ekalife.elions.model.SlinkBayar;
import com.ekalife.elions.model.TransUlink;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.parent.ParentDao;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.spring.util.Email;

@SuppressWarnings({ "unchecked", "deprecation" })
public class SavingBacCFL extends ParentDao{
	private Email email;
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private long accessTime;
	private BacDao bacDao;
	public long getAccessTime() { return accessTime;}
	public void setAccessTime(long accessTime) {this.accessTime = accessTime;}
	public BacDao getBacDao() {return bacDao;}
	public void setBacDao(BacDao bacDao) {this.bacDao = bacDao;}
	
	public void setEmail(Email email) {
		this.email = email;
	}
	private UwDao uwDao;
	public void setUwDao(UwDao uwDao) {
		this.uwDao = uwDao;
	}
	
	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.bac.";
	}
		
	private void prosesError(User currentUser, Exception e) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy (hh:mm:ss)");
		Date now = new Date();
		
		StringBuffer stackTrace = new StringBuffer();

		stackTrace.append("\n===== START ERROR [INPUT SPAJ][" + df.format(now) + "] ===============");
		if(currentUser!=null) stackTrace.append("\n- User : " + currentUser.getName() + " [" + currentUser.getLus_id() + "] ");
		stackTrace.append("\n- Exception : \n");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		stackTrace.append(sw);
		stackTrace.append("\n===== END ERROR [E-Lions][" + df.format(now) + "] ===============");

		try {
			if(sw!=null){
				sw.close();
			}
			if(pw!=null){
				pw.close();
			}
		} catch (IOException e1) {
			logger.error("ERROR :", e1);
		}

		try {
			EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, new Date(), null, false, props.getProperty("admin.ajsjava"), new String[] {props.getProperty("admin.yusuf")}, null, null, "ERROR pada INPUT SPAJ [E-LIONS]", stackTrace.toString(), null, null);
		} catch (MailException e1) {
			logger.error("ERROR :", e1);
		}
		
	}
		
		// proses submit	
		public Cmdeditbac insertspajbaru(Object cmd, User currentUser) throws ServletException,IOException
		{
			Cmdeditbac edit= (Cmdeditbac) cmd;
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String strTmpSPAJ=null;
			try {
				
				String strPOClientID =null;
				String strInsClientID = null;
				Long intIDCounter =null;
				
				String strAgentBranch = null;
				String strBranch = null;
				String strWilayah = null;
				String strRegion = null;
	
				String gc_strTmpBranch= "WW";
				Long intSPAJ = null;
				Integer inttgl1	=null;
				Integer inttgl2 =null;
				Integer li_tahun =null;
				String ldt_endpay="";
				Date ldt_endpay1=null; 
				Date ldt_endpay4 =null;
				Date ldt_endpay5 =null;
				Integer ai_month=null;			
				String kode_id = null;
				
				Integer flag_gutri = edit.getDatausulan().getFlag_gutri();
				if (flag_gutri  == null)
				{
					flag_gutri =new Integer(0);
				}
				if (flag_gutri.intValue() == 1)
				{
					edit.getPemegang().setMste_flag_guthrie(flag_gutri);
					String nama ="GUTHRIE  PECCONINA  INDONESIA";
					String tgl = "19951013";
					kode_id = this.bacDao.selectkodegutri(nama, tgl);
				}
				
				Calendar tgl_sekarang = Calendar.getInstance(); 
				Integer v_intRelation = edit.getPemegang().getLsre_id();
				Integer v_intActionBy = edit.getPemegang().getLus_id();
				
				Integer	flag_rider = edit.getDatausulan().getFlag_rider();
				Integer	flag_jenis_plan = edit.getDatausulan().getFlag_jenis_plan();
				Integer flag_platinumlink = edit.getDatausulan().getFlag_platinumlink();
				if (flag_platinumlink ==null)
				{
					flag_platinumlink = new Integer(0);
				}
				Integer	kode_flag = edit.getDatausulan().getKode_flag();
				Integer	flag_as = edit.getDatausulan().getFlag_as();
				
				Date v_strBeginDate = edit.getDatausulan().getMste_beg_date();
				Integer v_intInsPeriod = edit.getDatausulan().getMspr_ins_period();
				Date v_strEndDate = edit.getDatausulan().getMste_end_date();
				Integer jumlah_rider = edit.getDatausulan().getJmlrider();
				
				Date tanggal = commonDao.selectSysdate();
				
				String sysdate = defaultDateFormat.format(new Date());
				
				DateFormat dfh = new SimpleDateFormat("HH");
				DateFormat dfm = new SimpleDateFormat("mm");
				
//				String hh = ServletRequestUtils.setStringParameter("hh", dfh.format(tanggal));
//				String mm = ServletRequestUtils.getStringParameter(request, "mm", dfm.format(tanggal));
				String tanggalTerimaAdmin;
				
				
				
				String tgl_s =  FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.DATE)),2);
				tgl_s = tgl_s.concat("/");
				tgl_s = tgl_s.concat(FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.MONTH)+1),2));
				tgl_s = tgl_s.concat("/");
				tgl_s = tgl_s.concat(Integer.toString(tgl_sekarang.get(Calendar.YEAR)));	
				Date v_strInputDate = df.parse(tgl_s);
				Date v_strDateNow = v_strInputDate;
				Integer index_biaya = edit.getInvestasiutama().getJmlh_biaya();
				if (index_biaya==null)
				{
					index_biaya = new Integer(0);
				}

				String v_strregionid = edit.getAgen().getKode_regional().toUpperCase();
				String v_strAgentId = edit.getAgen().getMsag_id().toUpperCase();
				String v_stragentnama = edit.getAgen().getMcl_first().toUpperCase();
				
				Integer lssa_id = new Integer(1);
				edit.getDatausulan().setLssa_id(lssa_id);
				
				//----------------------------------
				// Get the Agent Branch information
				if  (v_strAgentId.equalsIgnoreCase("000000"))
				{
				       strBranch = FormatString.rpad("0",(v_strregionid.substring(0,2)),2);
				       strWilayah = FormatString.rpad("0",(v_strregionid.substring(2,4)),2);
				       strRegion = FormatString.rpad("0",(v_strregionid.substring(4,6)),2);
				       strAgentBranch = strBranch.concat("00");
				}
				
				edit.getAgen().setLca_id(strBranch);
				edit.getAgen().setLwk_id(strWilayah);
				edit.getAgen().setLsrg_id(strRegion);
				edit.getAgen().setKode_regional(strAgentBranch);

				//------------------------------------
				//cari & update counter
				/*
				Map m = new HashMap();
				m.put("kodecbg", strBranch);
				intIDCounter = (Long) querySingle("select.counter", m);
				
				if(edit.getFlag_gmanual_spaj()!=null){
					if(edit.getFlag_gmanual_spaj()>=1){
						strTmpSPAJ = edit.getTertanggung().getReg_spaj();
					}else{
						if (intIDCounter.longValue() == 0)
						{
							intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 100000));
						}else{
							if ((intIDCounter).toString().length()==9)
							{
								inttgl1=new Integer(Integer.parseInt((intIDCounter).toString().substring(0,4)));
								inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));

								if (inttgl1.intValue() != inttgl2.intValue())
								{
									intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("00000")));
								
									Map param=new HashMap();
									param.put("intIDCounter", intIDCounter);
									param.put("kodecbg", strBranch);
									update("update.mst_counter", param);
									//logger.info("update mst counter start di tahun baru");
								}
							}
						}
						
						//--------------------------------------------
						//Increase current SPAJ No by 1 and
						//update the value to MST_COUNTER table
						Map param=new HashMap();
						param.put("IDCounter", new Long(intIDCounter.longValue()+1));
						param.put("kodecbg", strBranch);
						update("update.mst_counter_up", param);
						//logger.info("update mst counter naik");
						intSPAJ = new Long(intIDCounter.longValue() + 1);
						strTmpSPAJ = strBranch.concat(intSPAJ.toString());
					}
				}else{
					if (intIDCounter.longValue() == 0)
					{
						intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 100000));
					}else{
						if ((intIDCounter).toString().length()==9)
						{
							inttgl1=new Integer(Integer.parseInt((intIDCounter).toString().substring(0,4)));
							inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));

							if (inttgl1.intValue() != inttgl2.intValue())
							{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("00000")));
							
								Map param=new HashMap();
								param.put("intIDCounter", intIDCounter);
								param.put("kodecbg", strBranch);
								update("update.mst_counter", param);
								//logger.info("update mst counter start di tahun baru");
							}
						}
					}
					
					//--------------------------------------------
					//Increase current SPAJ No by 1 and
					//update the value to MST_COUNTER table
					Map param=new HashMap();
					param.put("IDCounter", new Long(intIDCounter.longValue()+1));
					param.put("kodecbg", strBranch);
					update("update.mst_counter_up", param);
					//logger.info("update mst counter naik");
					intSPAJ = new Long(intIDCounter.longValue() + 1);
					strTmpSPAJ = strBranch.concat(intSPAJ.toString());
				}*/
				
				//MANTA (23/06/2017) - Generate No SPAJ langsung dari sequence
				if(edit.getFlag_gmanual_spaj()!=null){
					if(edit.getFlag_gmanual_spaj()>=1){
						strTmpSPAJ = edit.getTertanggung().getReg_spaj();
					}else{
						strTmpSPAJ = bacDao.selectSeqNoSpaj(strBranch);
					}
				}else{
					strTmpSPAJ = bacDao.selectSeqNoSpaj(strBranch);
				}
				
				String no_pb=edit.getPemegang().getNo_pb();
				//----------------------------------------------------------
				//Insert no spaj dan nama agent untuk agent baru di MST_AGENT_TEMP table
				if (v_strAgentId.equalsIgnoreCase("000000"))
				{
					Map param1=new HashMap();
					param1.put("strTmpSPAJ", strTmpSPAJ);
					param1.put("v_stragentnama", v_stragentnama);
					insert("insert.mst_agent_temp", param1);
					//logger.info("insert kalau agent baru");
				}

				String nomor=null;
				if (flag_gutri.intValue() ==1)
				{
					strInsClientID=edit.getTertanggung().getMcl_id();
				}else{
					//----------------------------------------------------
					//Get Insured Client ID counter from MST_COUNTER table
			/*		Map param2=new HashMap();
					param2.put("gc_strTmpBranch", gc_strTmpBranch);
					intIDCounter = (Long) querySingle("select.counter_client_id", param2);
			*/		//logger.info("select counter mcl id ttg");
	
					/* Increase current Insured Client ID by 1 and
					 update the value to MST_COUNTER table */
			/*		Map param3=new HashMap();
					param3.put("gc_strTmpBranch", gc_strTmpBranch);
					param3.put("IDCounter", new Long(intIDCounter.longValue()+1));
					update("update.clientid", param3);
			*/		//logger.info(intIDCounter);
					//logger.info("update counter mcl id ttg");
	
					/* Combine Branch Information and Client Id Counter
					 to get the temporary Insured Client ID */
			/*		nomor =("000000000").concat(Long.toString(intIDCounter.longValue() + 1));
				//	logger.info(nomor);
					strInsClientID = gc_strTmpBranch.concat(nomor.substring((nomor.length()-10),nomor.length()));
			*/	//	logger.info(strInsClientID);
//					input data tertanggung di mst client new dan mst address new
					
					strInsClientID = (String) querySingle("selectSequenceClientID", null);
					proc_save_data_ttg (edit,strInsClientID,v_strDateNow);
					
				}
				
				
				//-------------------------------------------------
				// Check Policy Holder and Insured relation
	
				if (v_intRelation.intValue() != 1)
				{
					if (flag_gutri.intValue() ==1)
					{
						strPOClientID=kode_id;
					}else{	
						/* If Policy Holder and Insured is not the same person
						 	then create the new Client ID
							Get Policy Holder Client ID counter from MST_COUNTER table*/
					/*		Map param6 = new HashMap();
							param6.put("gc_strTmpBranch", gc_strTmpBranch);
							intIDCounter = (Long) querySingle("select.counter_client_id", param6);
					*/		//logger.info("select counter mcl id pp");
		
						/* Increase current Policy Holder Client ID by 1 and
						   update the value to MST_COUNTER table*/
					/*		Map param7=new HashMap();
							param7.put("gc_strTmpBranch", gc_strTmpBranch);
							param7.put("IDCounter", new Long(intIDCounter.longValue()+1));
							update("update.clientid", param7);
					*/		//logger.info("update mcl id pp");
		
						/* Combine Branch Information and Client Id Counter
						 to get the temporary Policy Holder Client ID*/
					/*		nomor =("000000000").concat(Long.toString(intIDCounter.longValue() + 1));
							strPOClientID = gc_strTmpBranch.concat(nomor.substring((nomor.length()-10),nomor.length()));
					*/		//input data pemegang polis di mst client new dan mst_address new
						
							strPOClientID = (String) querySingle("selectSequenceClientID", null);
							proc_save_data_pp(edit,strPOClientID,v_strDateNow );
						}						

					}else{
						if (flag_gutri.intValue() ==1)
						{
							strPOClientID=kode_id;
						}else{	
	
						// If Policy Holder and Insured is the same person
							strPOClientID = strInsClientID;
						}
					}	
				
					//insert mst_policy
					proc_save_mst_policy(currentUser, edit,strPOClientID,strTmpSPAJ,v_strDateNow ,v_strAgentId,strAgentBranch,strBranch,strWilayah,strRegion,v_strregionid);
					
					proc_save_worksite_flag(edit,strTmpSPAJ);
					proc_save_noblanko(edit,strTmpSPAJ);
					
//					insert mst insured
					proc_save_mst_insured(edit,strInsClientID,strTmpSPAJ);			
		
					proc_save_suamiistri_ttg(edit,strTmpSPAJ);
					
					proc_save_suamiistri_pp(edit,strTmpSPAJ);
					
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						proc_save_data_pic(edit,strPOClientID);
					}
					
					//------------------------------------------------------------
					// Process Application closing and Agent Commission
					proc_save_agen(edit,strTmpSPAJ, v_strAgentId,strAgentBranch,strBranch,strWilayah,strRegion,v_strregionid);
		
					//input  address billing
					proc_save_addr_bill(edit,strTmpSPAJ);
						
					//-------------------------------------------------
					// Insert rekening baik account recur maupun rek client
					proc_save_rekening(edit,strTmpSPAJ,kode_flag);
										
					// Insert information to MST_POSITION_SPAJ
					uwDao.insertMstPositionSpaj(v_intActionBy.toString(), "INPUT SPAJ", strTmpSPAJ, 0);
					uwDao.updateMstInsuredTglAdmin(strTmpSPAJ, 1, tanggal, 0);
					uwDao.saveMstTransHistory(strTmpSPAJ, "tgl_input_spaj_admin", tanggal, null, null);
//					//logger.info("insert posisi spaj");
//			        try {
//			        	Thread.sleep(2000); 
//			        } catch (InterruptedException e) {
//			        	logger.error("ERROR :", e);
//			        }
					
					if(edit.getAgen().getLca_id().equals("58")){
						proc_save_reff_mall(edit, strTmpSPAJ,currentUser);
					}
						
					
			        //insert ke mst_position_spaj u/history no PB(dian)
					bacDao.insertMst_position_no_spaj_pb(strTmpSPAJ, currentUser.getLus_id(),1, 10, "NO PB: "+no_pb,2);

					if(edit.getPemegang().getInfo_special_case() != null && !"".equals(edit.getPemegang().getInfo_special_case())) {
						//insert ke mst_position_spaj u/ info special case (yusuf)
						bacDao.insertMst_position_no_spaj_pb(strTmpSPAJ, currentUser.getLus_id(),1, 10, "SC:"+edit.getPemegang().getInfo_special_case(),5);
					}
					
					//------------------------------------------------------------
					// Insert Insured information to MST_STS_CLIENT
					if (flag_gutri.intValue() !=1)
					{
						bacDao.insertMst_sts_client(strInsClientID);
						//logger.info("insert status client");
					}
					
					if (flag_rider.intValue()==1)
					{
						li_tahun =v_intInsPeriod;
						if(!Common.isEmpty(flag_jenis_plan)){
							if ((flag_jenis_plan.intValue()==1) )
							{
								li_tahun = new Integer(6);
							}
						}else if(Common.isEmpty(flag_jenis_plan)){
//							flag_jenis_plan=pro
						}
						ai_month=new Integer((li_tahun.intValue() * 12) - 1);
						//ai_month=new Integer((li_tahun.intValue() * 12) );
							
						Map param27=new HashMap();
						param27.put("v_strBeginDate",v_strBeginDate);	
						param27.put("ai_month",ai_month);	
						//logger.info("add month");
						
						Date ldt_endpay2 =null;
						DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						ldt_endpay2=df1.parse((String)querySingle("select.addmonths", param27));
									
						int tgl_endpay = ldt_endpay2.getDate();
						int bln_endpay = ldt_endpay2.getMonth()+1;
						int thn_endpay = ldt_endpay2.getYear()+1900;
						ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
						ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
						ldt_endpay = ldt_endpay.concat("/");
						ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
						ldt_endpay1 = df.parse(ldt_endpay);
									
						//end pay untuk biaya administrasi
						
							Calendar tanggal_sementara = Calendar.getInstance();
							Date ldt_endpay3 =null;
							Integer ai_month1=new Integer(-1);
							Map param28=new HashMap();
							param28.put("v_strBeginDate",v_strEndDate);	
							param28.put("ai_month",ai_month1);	
							ldt_endpay3=df1.parse((String)querySingle("select.addmonths", param28));
							
							tgl_endpay = ldt_endpay3.getDate();
							bln_endpay = ldt_endpay3.getMonth()+1;
							thn_endpay = ldt_endpay3.getYear()+1900;
							ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
							ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
							ldt_endpay = ldt_endpay.concat("/");
							ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
							tanggal_sementara.set(thn_endpay,bln_endpay,tgl_endpay);	
							tanggal_sementara.add(Calendar.DATE,1);
							tgl_endpay = tanggal_sementara.getTime().getDate();
							bln_endpay = tanggal_sementara.getTime().getMonth();
							thn_endpay = tanggal_sementara.getTime().getYear()+1900;
							ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
							ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
							ldt_endpay = ldt_endpay.concat("/");
							ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
							ldt_endpay4 = df.parse(ldt_endpay);
						if (flag_jenis_plan.intValue()!=1)
						{
							ldt_endpay4 =ldt_endpay1;
						}
						
						//biaya pokok
						Integer ai_month3=new Integer((10 * 12) - 1);
						if(edit.getDatausulan().getLsbs_id()==191){
							ai_month3=new Integer((8 * 12) - 1);
						}
						Date tgl_beg_date_polis = edit.getDatausulan().getMste_beg_date();
						Integer tahun_beg_date_polis = tgl_beg_date_polis.getYear() + 1900;
						if ((flag_platinumlink.intValue() == 1) && (tahun_beg_date_polis.intValue() > 2006))
						{
							 ai_month3 =  new Integer(ai_month3.intValue() + 1);
						}
								
						Map param29=new HashMap();
						param29.put("v_strBeginDate",v_strBeginDate);	
						param29.put("ai_month",ai_month3);	
						//logger.info("add month");
						
						Date ldt_endpay6 =null;
						ldt_endpay6=df1.parse((String)querySingle("select.addmonths", param29));
							
						tgl_endpay = ldt_endpay6.getDate();
						bln_endpay = ldt_endpay6.getMonth()+1;
						thn_endpay = ldt_endpay6.getYear()+1900;
						ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
						ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
						ldt_endpay = ldt_endpay.concat("/");
						ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
						ldt_endpay5 = df.parse(ldt_endpay);
	
					}			

					//------------------------------------------------------------
					// Insert Basic Plan information to MST_PRODUCT_INSURED
					proc_save_product_insured(edit,strTmpSPAJ,v_intActionBy ,flag_jenis_plan, ldt_endpay1,currentUser);						
					
						if (jumlah_rider.intValue()>0)
						{
							proc_save_rider(edit, strTmpSPAJ,v_intActionBy );
						}
					
					Double jumlah_premi = this.bacDao.sum_premi(strTmpSPAJ);
					if (jumlah_premi == null)
					{
						jumlah_premi = new Double(0);
					}

					if (flag_gutri.intValue() == 1)
					{
						DetailPembayaran dp = new DetailPembayaran();
						dp.setReg_spaj(strTmpSPAJ);
						dp.setKe(new Integer(1));
						dp.setJenis_ttp(new Integer(1));
						dp.setCara_bayar(new Integer(5));
						dp.setTgl_bayar(df.parse("01/11/2006"));
						dp.setTgl_jatuh_tempo(null);
						dp.setTgl_rk(df.parse("01/11/2006"));
						dp.setKurs(edit.getDatausulan().getLku_id());
						dp.setJml_bayar(jumlah_premi);
						dp.setNilai_kurs(new Double(0));
						dp.setTgl_skrg(new Date());
						dp.setRef_polis_no(null);
						dp.setKeterangan(null);
						dp.setLus_login_name(Integer.toString(edit.getDatausulan().getLus_id()));
						dp.setAktif(new Integer(1));
						dp.setBank(new Integer(42));
						dp.setStatus("B");
						dp.setNo_kttp(null);
						this.bacDao.insertmst_deposit(dp);
					}
					
					//produk karyawan, nik
					if (flag_as.intValue()==2)
					{
						proc_save_karyawan(edit, strTmpSPAJ,v_intActionBy );
					}
							
					//-----------------------------------------------------------
					//-- Insert Excellink information to MST_ULINK and MST_DET_ULINK
					if (kode_flag.intValue() > 1 && kode_flag.intValue() != 11 && kode_flag.intValue() != 15)
					{
						proc_unitlink(edit,strTmpSPAJ,v_strDateNow,v_intActionBy ,currentUser ,ldt_endpay1 ,ldt_endpay4 ,ldt_endpay5);
					}						
						
					//Power Save
					if (kode_flag.intValue() == 1 && edit.getDatausulan().getLsbs_id().intValue()!=188)
					{
						proc_powersave(edit,strTmpSPAJ,v_strDateNow,v_intActionBy );
					}
							
					if (kode_flag.intValue() == 11 || kode_flag.intValue() == 15  || edit.getDatausulan().getLsbs_id().intValue()==188)
					{
						proc_powersave_stable(edit,strTmpSPAJ,v_strDateNow,v_intActionBy );
					}
					//------------------------------------------------------------
					// Insert Beneficiary information to MST_BENEFICIARY
					proc_save_benef(edit, strTmpSPAJ );
					
					//insert peserta simas
					Integer flag_simas = edit.getDatausulan().getFlag_simas();
					if (flag_simas == null)
					{
						flag_simas = new Integer(0);
					}
					if (flag_simas.intValue() == 1)
					{
						Simas simas = new Simas();
						simas.setLsbs_id(edit.getDatausulan().getLsbs_id());
						simas.setLsdbs_number(edit.getDatausulan().getLsdbs_number());
						//simas.setDiscount(new Double(0));
						proc_save_peserta(edit,strTmpSPAJ,simas,"utama");
					}	
					
					if(edit.getMedQuest() !=null){
						edit.getMedQuest().setReg_spaj(strTmpSPAJ);
						edit.getMedQuest().setMste_insured_no(1);
						edit.getMedQuest().setMsadm_berat_berubah(0);
						 uwDao.insertMedQuest(edit.getMedQuest());
					}
					
					
			} catch (Exception e){
				strTmpSPAJ="";
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				prosesError(currentUser, e);
			}
			//return strTmpSPAJ;
			edit.getPemegang().setReg_spaj(strTmpSPAJ);
			 return edit;
		}
		
			
		public Cmdeditbac editspaj(Object cmd, User currentUser) throws ServletException,IOException,Exception
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Cmdeditbac edit= (Cmdeditbac) cmd;
			String strTmpSPAJ=null;
			try {
				
				String strPOClientID =null;
				String strInsClientID = null;
				Long intIDCounter =null;
				
				String strAgentBranch = null;
				String strBranch = null;
				String strWilayah = null;
				String strRegion = null;
				String gc_strTmpBranch= "WW";
				Integer li_tahun =null;
				String ldt_endpay="";
				Date ldt_endpay1=null;
				Date ldt_endpay4 =null;
				Date ldt_endpay5=null;
				Integer ai_month=null;
				
				if(!edit.getCurrentUser().getCab_bank().equals("") && edit.getCurrentUser().getJn_bank().intValue() == 2) {
					if(products.powerSave(edit.getDatausulan().getLsbs_id().toString())){
						edit.getAgen().setMsag_id("016409");
					}else if(edit.getDatausulan().getLsbs_id().intValue()==164&& edit.getDatausulan().getLsdbs_number().intValue()==2){
						edit.getAgen().setMsag_id("021052");
					}
				}
				
				Calendar tgl_sekarang = Calendar.getInstance(); 
				Integer v_intRelation = edit.getPemegang().getLsre_id();
				
				Integer v_intActionBy = edit.getPemegang().getLus_id();
				Integer	flag_rider = edit.getDatausulan().getFlag_rider();
				Integer flag_platinumlink = edit.getDatausulan().getFlag_platinumlink();
				if (flag_platinumlink ==null)
				{
					flag_platinumlink = new Integer(0);
				}
				Integer	flag_jenis_plan = edit.getDatausulan().getFlag_jenis_plan();
				if (flag_jenis_plan == null)
				{
					flag_jenis_plan = new Integer (0);
				}
				Integer	kode_flag = edit.getDatausulan().getKode_flag();
				Integer	flag_as = edit.getDatausulan().getFlag_as();
					
				Date v_strBeginDate = edit.getDatausulan().getMste_beg_date();
				Integer v_intInsPeriod = edit.getDatausulan().getMspr_ins_period();
				Date v_strEndDate = edit.getDatausulan().getMste_end_date();
				Integer jumlah_rider = edit.getDatausulan().getJmlrider();
					
				String tgl_s =  FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.DATE)),2);
				tgl_s = tgl_s.concat("/");
				tgl_s = tgl_s.concat(FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.MONTH)+1),2));
				tgl_s = tgl_s.concat("/");
				tgl_s = tgl_s.concat(Integer.toString(tgl_sekarang.get(Calendar.YEAR)));	
				Date v_strInputDate = df.parse(tgl_s);
				Date v_strDateNow = v_strInputDate;
				Integer index_biaya = edit.getInvestasiutama().getJmlh_biaya();
				if (index_biaya==null)
				{
					index_biaya = new Integer(0);
				}

				String v_strregionid = edit.getAgen().getKode_regional().toUpperCase();
				String v_strAgentId = edit.getAgen().getMsag_id().toUpperCase();
				String v_stragentnama = edit.getAgen().getMcl_first().toUpperCase();
				
				Integer flag_gutri = edit.getDatausulan().getFlag_gutri();
				if (flag_gutri.intValue() == 1)
				{
					edit.getPemegang().setMste_flag_guthrie(flag_gutri);
				}
					
				//----------------------------------
				// Get the Agent Branch information
				if  (v_strAgentId.equalsIgnoreCase("000000"))
				{
				       strBranch = FormatString.rpad("0",(v_strregionid.substring(0,2)),2);
				       strWilayah = FormatString.rpad("0",(v_strregionid.substring(2,4)),2);
				       strRegion = FormatString.rpad("0",(v_strregionid.substring(4,6)),2);
				       strAgentBranch = strBranch.concat("00");
				}else{
						Map m = new HashMap();
						m.put("kodeagen", v_strAgentId);
						Map data = (HashMap) querySingle("select.regionalagen",m);
						if (data!=null)
						{		
							 strBranch = (String)data.get("STRBRANCH");
						     strWilayah = (String)data.get("STRWILAYAH");
						     strRegion = (String)data.get("STRREGION");
						     strAgentBranch = (String)data.get("STRAGENTBRANCH");
						}
					}
				edit.getAgen().setLca_id(strBranch);
				edit.getAgen().setLwk_id(strWilayah);
				edit.getAgen().setLsrg_id(strRegion);
				edit.getAgen().setKode_regional(strAgentBranch);
				strTmpSPAJ = edit.getPemegang().getReg_spaj();

				//----------------------------------------------------------
				//Insert no spaj dan nama agent untuk agent baru di MST_AGENT_TEMP table
				if (v_strAgentId.equalsIgnoreCase("000000"))
				{
					Map param1=new HashMap();
					param1.put("strTmpSPAJ", strTmpSPAJ);
					param1.put("v_stragentnama", v_stragentnama);
					//insert("insert.mst_agent_temp", param1);
					//logger.info("insert kalau agent baru");
					int rowupdated=update("update.mst_agent_temp",param1);
					if (rowupdated ==0)
					{
						insert("insert.mst_agent_temp", param1);
					}
				}
				
				//----------------------------------------------------
				//Get Insured Client ID counter from MST_COUNTER table
				strInsClientID =edit.getTertanggung().getMcl_id();			

//				edit data tertanggung di mst client new dan mst address new
				proc_save_data_ttg (edit,strInsClientID,v_strDateNow );
								
				//-------------------------------------------------
				// Check Policy Holder and Insured relation
				
				if (v_intRelation.intValue() != 1)
				{
					if (edit.getPemegang().getMcl_id().equalsIgnoreCase(edit.getTertanggung().getMcl_id()))
					{
						/* If Policy Holder and Insured is not the same person
						 	then create the new Client ID
							Get Policy Holder Client ID counter from MST_COUNTER table*/
								
					/*		Map param1 = new HashMap();
							param1.put("gc_strTmpBranch", gc_strTmpBranch);
							intIDCounter = (Long) querySingle("select.counter_client_id", param1);
					*/		//logger.info("select counter mcl id pp");
		
						/* Increase current Policy Holder Client ID by 1 and
						   update the value to MST_COUNTER table*/
					/*		Map param4=new HashMap();
							param4.put("gc_strTmpBranch", gc_strTmpBranch);
							param4.put("IDCounter", new Long(intIDCounter.longValue()+1));
							update("update.clientid", param4);
					*/		//logger.info("edit mcl id pp");
		
						/* Combine Branch Information and Client Id Counter
						 to get the temporary Policy Holder Client ID*/
					/*		String nomor =("000000000").concat(Long.toString(intIDCounter.longValue() + 1));
							strPOClientID = gc_strTmpBranch.concat(nomor.substring((nomor.length()-10),nomor.length()));
					*/
//								input data pemegang polis di mst client new dan mst_address new
							
							strPOClientID = (String) querySingle("selectSequenceClientID", null);
							proc_save_data_pp(edit,strPOClientID,v_strDateNow  );							
					}else{
							strPOClientID = edit.getPemegang().getMcl_id();
							
//							edit data pemegang polis di mst client new dan mst_address new
							proc_save_data_pp(edit,strPOClientID,v_strDateNow  );
					}
				}else{
					// If Policy Holder and Insured is the same person
						strPOClientID = strInsClientID;
				}	
				
				//edit mst_policy
				proc_save_mst_policy(currentUser, edit,strPOClientID,strTmpSPAJ,v_strDateNow ,v_strAgentId,strAgentBranch,strBranch,strWilayah,strRegion,v_strregionid);
				proc_save_worksite_flag(edit,strTmpSPAJ);
				proc_save_noblanko(edit,strTmpSPAJ);
				//edit mst insured
				proc_save_mst_insured(edit,strInsClientID,strTmpSPAJ);
		
				proc_save_suamiistri_ttg(edit,strTmpSPAJ);
				
				proc_save_suamiistri_pp(edit,strTmpSPAJ);
				
				if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
					proc_save_data_pic(edit,strPOClientID);
				}

				String msen_endors_no = (String) querySingle("selectEndorsNo",strTmpSPAJ);
				if(msen_endors_no!=null){
					edit.getPowersave().setMsen_endors_no(msen_endors_no);
				}
				
				Integer posisi_dok =null;
				Integer status_polis =null;
				Integer status_dok = null;
				status_polis = (Integer) querySingle("selectPositionSpaj",strTmpSPAJ);
				Map dataa = (HashMap) querySingle("selectPositiondok",strTmpSPAJ);
				if (dataa!=null)
				{		
					posisi_dok = new Integer(Integer.parseInt(dataa.get("LSPD_ID").toString()));
					status_dok = new Integer(Integer.parseInt(dataa.get("LSSP_ID").toString()));
				}
				edit.getHistory().setStatus_polis(status_polis);
				edit.getDatausulan().setLspd_id(posisi_dok);
	
				Map param35 = new HashMap();
				param35.put("strTmpSPAJ",strTmpSPAJ);				
				delete("delete.mst_benef", param35);	
							
				delete("delete.mst_agent_prod", param35);	
							
				delete("delete.mst_agent_comm", param35);	
				
				delete("delete.mst_agent_artha",param35);
				
				delete("delete.mst_agent_ba", param35);	
							
				delete("delete.mst_account_recur", param35);
				
				delete("delete.mst_peserta", param35);
							
				delete("delete.mst_emp", param35);	
							
				int v = bacDao.selectValidasiEditUnitLink(strTmpSPAJ);
				if(v == 0) {
					delete("delete.mst_biaya_ulink", param35);	
					
					delete("delete.mst_det_ulink", param35);	
							
					Integer lspd_id = edit.getDatausulan().getLspd_id();
					Integer jumlah_trans = (Integer)querySingle("counttransulink",strTmpSPAJ);
					if (jumlah_trans == null)
					{
						jumlah_trans = new Integer(0);
					}
					if (jumlah_trans.intValue() == 0)
					{
						delete("delete.mst_ulink", param35);	
					}
				}
				
				//logger.info("delete.mst_ulink");		
							
				delete("delete.mst_powersave_ro", param35);	
				//logger.info("delete.mst_powersave_ro");
				
				delete("delete.mst_rider_save", param35);
							
				delete("delete.mst_powersave_proses", param35);	
				//logger.info("delete.mst_powersave_proses");	
				
				delete ("delete.mst_psave_bayar.nb",param35);
				delete("delete.mst_psave", param35);
				
				delete ("delete.mst_slink_bayar.nb",param35);
				delete ("delete.mst_slink",param35);

//				logger.info("delete.mst_slink");	

				delete("delete.mst_ssave_bayar", strTmpSPAJ);
				delete("delete.mst_ssave", strTmpSPAJ);
				
				Integer flag_hcp =edit.getDatausulan().getFlag_hcp();
				if (flag_hcp == null)
				{
					flag_hcp = new Integer(0);
				}
				
				Integer flag_rider_hcp =edit.getDatausulan().getFlag_rider_hcp();
				if (flag_rider_hcp == null)
				{
					flag_rider_hcp = new Integer(0);
				}
					
				/*if ((edit.getDatausulan().getLsbs_id().intValue() != 161) && flag_hcp.intValue() == 0 )
				{	*/
					delete("delete.mst_product_insured", param35);	
					//logger.info("delete mst_product_insured");

//					this.bacDao.delete_mst_peserta_all(strTmpSPAJ);
	//				logger.info("delete peserta simas");
			/*	}else{
					if (flag_hcp.intValue() == 0 )
					{
						delete("delete.mst_product_insured", param35);	
						//logger.info("delete mst_product_insured");
					}else{
						delete("delete.mst_product_insured_hcp_std", strTmpSPAJ);	
						//delete mst_product_insured hcpf basic dan utama 
					}
				}*/
							
				//------------------------------------------------------------
				// Process Application closing and Agent Commission
				proc_save_agen(edit,strTmpSPAJ, v_strAgentId,strAgentBranch,strBranch,strWilayah,strRegion,v_strregionid);

//				edit  address billing
				proc_save_addr_bill(edit,strTmpSPAJ);

				//-------------------------------------------------
				// Insert rekening baik account recur maupun rek client
				proc_save_rekening(edit,strTmpSPAJ,kode_flag);
				
				if(edit.getAgen().getLca_id().equals("58")){
					proc_save_reff_mall(edit, strTmpSPAJ,currentUser);
				}
								
				
				if(flag_rider !=null){
					if (flag_rider.intValue()==1)
					{
						li_tahun =v_intInsPeriod;
						if ((flag_jenis_plan.intValue()==1) )
						{
							li_tahun = new Integer(6);
						}
						ai_month=new Integer((li_tahun.intValue() * 12) - 1);
									
						Map param20=new HashMap();
						param20.put("v_strBeginDate",v_strBeginDate);	
						param20.put("ai_month",ai_month);	
						//logger.info("add month");
		
						Date ldt_endpay2 =null;
					    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						ldt_endpay2=df1.parse((String)querySingle("select.addmonths", param20));
						int tgl_endpay = ldt_endpay2.getDate();
						int bln_endpay = ldt_endpay2.getMonth()+1;
						int thn_endpay = ldt_endpay2.getYear()+1900;
						ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
						ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
						ldt_endpay = ldt_endpay.concat("/");
						ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
						ldt_endpay1 = df.parse(ldt_endpay);
						edit.getDatausulan().setMspr_end_pay(ldt_endpay1);
							
						//end pay untuk biaya administrasi
						Calendar tanggal_sementara = Calendar.getInstance();
						Date ldt_endpay3 =null;
						Integer ai_month1=new Integer(-1);
						Map param28=new HashMap();
						param28.put("v_strBeginDate",v_strEndDate);	
						param28.put("ai_month",ai_month1);	
						ldt_endpay3=df1.parse((String)querySingle("select.addmonths", param28));
						tgl_endpay = ldt_endpay3.getDate();
						bln_endpay = ldt_endpay3.getMonth()+1;
						thn_endpay = ldt_endpay3.getYear()+1900;
						ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
						ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
						ldt_endpay = ldt_endpay.concat("/");
						ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
						tanggal_sementara.set(thn_endpay,bln_endpay,tgl_endpay);	
						tanggal_sementara.add(Calendar.DATE,1);
						tgl_endpay = tanggal_sementara.getTime().getDate();
						bln_endpay = tanggal_sementara.getTime().getMonth();
						thn_endpay = tanggal_sementara.getTime().getYear()+1900;
						ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
						ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
						ldt_endpay = ldt_endpay.concat("/");
						ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
						ldt_endpay4 = df.parse(ldt_endpay);
						
						if (flag_jenis_plan.intValue()!=1)
						{
							ldt_endpay4 =ldt_endpay1;
						}
						
						//biaya pokok
						Integer ai_month3=new Integer((10 * 12) - 1);
						if(edit.getDatausulan().getLsbs_id()==191){
							ai_month3=new Integer((8 * 12) - 1);
						}
						Date tgl_beg_date_polis = edit.getDatausulan().getMste_beg_date();
						Integer tahun_beg_date_polis = tgl_beg_date_polis.getYear() + 1900;
						if ((flag_platinumlink.intValue() == 1) && (tahun_beg_date_polis.intValue() > 2006))
						{
							 ai_month3 =  new Integer(ai_month3.intValue() + 1);
						}
							
						Map param29=new HashMap();
						param29.put("v_strBeginDate",v_strBeginDate);	
						param29.put("ai_month",ai_month3);	
						//logger.info("add month");

						Date ldt_endpay6 =null;
						ldt_endpay6=df1.parse((String)querySingle("select.addmonths", param29));
									
						tgl_endpay = ldt_endpay6.getDate();
						bln_endpay = ldt_endpay6.getMonth()+1;
						thn_endpay = ldt_endpay6.getYear()+1900;
						ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
						ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
						ldt_endpay = ldt_endpay.concat("/");
						ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
						ldt_endpay5 = df.parse(ldt_endpay);
					}	
				}

				//------------------------------------------------------------
				// Insert Basic Plan information to MST_PRODUCT_INSURED
				proc_save_product_insured(edit,strTmpSPAJ,v_intActionBy ,flag_jenis_plan, ldt_endpay1,currentUser);						
				
					if (jumlah_rider.intValue()>0)
					{
							proc_save_rider(edit, strTmpSPAJ,v_intActionBy );
					}
				
				Double jumlah_premi = this.bacDao.sum_premi(strTmpSPAJ);
				if (jumlah_premi == null)
				{
					jumlah_premi = new Double(0);
				}
				
				if (flag_gutri.intValue() == 1 || edit.getPemegang().getMste_flag_guthrie().intValue() == 1)
				{
					DetailPembayaran dp = new DetailPembayaran();
					dp.setReg_spaj(strTmpSPAJ);
					dp.setKe(new Integer(1));
					dp.setJenis_ttp(new Integer(1));
					dp.setNo_ke(new Integer(1));
					dp.setCara_bayar(new Integer(5));
					dp.setTgl_bayar(df.parse("01/11/2006"));
					dp.setTgl_jatuh_tempo(null);
					dp.setTgl_rk(df.parse("01/11/2006"));
					dp.setKurs(edit.getDatausulan().getLku_id());
					dp.setJml_bayar(jumlah_premi);
					dp.setNilai_kurs(new Double(0));
					dp.setTgl_skrg(new Date());
					dp.setRef_polis_no(null);
					dp.setKeterangan(null);
					dp.setLus_login_name(Integer.toString(edit.getDatausulan().getLus_id()));
					dp.setAktif(new Integer(1));
					dp.setBank(new Integer(42));
					dp.setStatus("B");
					dp.setNo_kttp(null);
					this.bacDao.updatemst_deposit(dp);
				}
				
				//produk karyawan, nik
				if (flag_as.intValue()==2)
				{
					proc_save_karyawan(edit, strTmpSPAJ,v_intActionBy );
				}
						
				if(v == 0) {
					//-----------------------------------------------------------
					//-- Insert Excellink information to MST_ULINK and MST_DET_ULINK
					if (kode_flag.intValue() > 1 && kode_flag.intValue() != 11)
					{
						proc_unitlink(edit,strTmpSPAJ,v_strDateNow,v_intActionBy ,currentUser ,ldt_endpay1 ,ldt_endpay4 ,ldt_endpay5);
					
					}						
					proc_save_trans_ulink(edit,strTmpSPAJ,status_polis ,v_intActionBy,posisi_dok,status_dok,currentUser);
				}
				
				//Power Save
				if (kode_flag.intValue() == 1 && edit.getDatausulan().getLsbs_id().intValue()!=188)
				{
					proc_powersave(edit,strTmpSPAJ,v_strDateNow,v_intActionBy );
				}
						
				if (kode_flag.intValue() == 11 || kode_flag.intValue() == 15 || edit.getDatausulan().getLsbs_id().intValue()==188)
				{
					proc_powersave_stable(edit,strTmpSPAJ,v_strDateNow,v_intActionBy );
				}
				//------------------------------------------------------------
				// Insert Beneficiary information to MST_BENEFICIARY
				proc_save_benef(edit, strTmpSPAJ );
				
				//insert peserta simas
				Integer flag_simas = edit.getDatausulan().getFlag_simas();
				if (flag_simas == null)
				{
					flag_simas = new Integer(0);
				}
				if (flag_simas.intValue() == 1)
				{
					Simas simas = new Simas();
					simas.setLsbs_id(edit.getDatausulan().getLsbs_id());
					simas.setLsdbs_number(edit.getDatausulan().getLsdbs_number());
					proc_save_peserta(edit,strTmpSPAJ,simas,"edit");
				}
				
			} catch (Exception e) {
				strTmpSPAJ="";
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				prosesError(currentUser, e);
			}
				
			//return strTmpSPAJ;
			edit.getPemegang().setReg_spaj(strTmpSPAJ);
			return edit;
			
		}	
			
			
		private Agentrec[] proc_process_agent(String v_strAgentId) throws ServletException,IOException
			{
				Integer intCounter = new Integer(0);
				Agentrec[]  arrAgentRec;
				arrAgentRec  = new Agentrec[5];
				String strTmpAgentId=null;
				String strLeaderId=null;
				Integer intLevelComm = new Integer(4);
				Integer intBisnisId =null ;
				Integer intLevelId =null;
				Integer strbm=null;
				Integer strsbm=null;
				Integer strjenis =null;
				Integer intCommId =null;
				Integer ii =new Integer(1);
				Integer iii = new Integer(1);

			     strLeaderId = v_strAgentId;
			     if (v_strAgentId.equalsIgnoreCase("000000"))
			     {
						strTmpAgentId = strLeaderId;
						intBisnisId = new Integer(1);
						intLevelId = new Integer(2);
						intCommId = new Integer(1);
						intCounter = new Integer(intCounter.intValue() + 1);
						arrAgentRec[intCounter.intValue()]= new Agentrec();
						arrAgentRec[intCounter.intValue()].setBisnis_id(intBisnisId);
						arrAgentRec[intCounter.intValue()].setLevel_id(new Integer( 5 - intCounter.intValue()));
						arrAgentRec[intCounter.intValue()].setAgent_id(strTmpAgentId);
					    if (intCommId==null)
					    {
					    	intCommId = new Integer(0);
					    }
						if (intCommId.intValue() == 1)
						{
							arrAgentRec[intCounter.intValue()].setComm_id(intLevelComm);
						}else{
						    arrAgentRec[intCounter.intValue()].setComm_id(null);
						}
						intLevelComm = new Integer(intLevelComm.intValue() - 1);
						iii = ii;
											
						while (intLevelId.intValue() < iii.intValue())
						{
							intCounter = new Integer(intCounter.intValue() + 1);
							arrAgentRec[intCounter.intValue()]= new Agentrec();
							arrAgentRec[intCounter.intValue()].setBisnis_id(arrAgentRec[intCounter.intValue()-1].getBisnis_id());
							arrAgentRec[intCounter.intValue()].setAgent_id(arrAgentRec[intCounter.intValue()-1].getAgent_id());
							arrAgentRec[intCounter.intValue()].setLevel_id(new Integer(5 - intCounter.intValue()));
							arrAgentRec[intCounter.intValue()].setComm_id(null);	
							iii = new Integer(iii.intValue() - 1);	
			     		}
				
			     }else{
					for (int i = 4 ; i>=1 ; i--)
					{
						
					   if ((4 - i) == (intCounter.intValue()))
					   {

							intCounter = new Integer(intCounter.intValue() + 1);
							strTmpAgentId = strLeaderId;
							
								Map param = new HashMap();							
								param.put("strTmpAgentId",strTmpAgentId);
								Map data2 = (HashMap) querySingle("select.mst_agent", param);
								if (data2!=null)
								{		
									intBisnisId = (Integer)data2.get("LSTB_ID");
									intLevelId = (Integer)data2.get("LSLE_ID");
									strLeaderId = (String)data2.get("MST_LEADER");
									intCommId = (Integer)data2.get("MSAG_KOMISI");
									strbm = (Integer)data2.get("MSAG_FLAG_BM");
									strsbm = (Integer)data2.get("MSAG_SBM");
									strjenis = (Integer)data2.get("MSAG_JENIS");
								}
								//logger.info("select mst agent");		
								if (i == 4 && strLeaderId.equalsIgnoreCase("") ) 
								{
									strLeaderId = "000606";
								}
								
							arrAgentRec[intCounter.intValue()]= new Agentrec();
						    arrAgentRec[intCounter.intValue()].setBisnis_id(intBisnisId);
						    arrAgentRec[intCounter.intValue()].setLevel_id(new Integer(5 - intCounter.intValue()));
						    arrAgentRec[intCounter.intValue()].setAgent_id(strTmpAgentId);
						    if (intCommId==null)
						    {
						    	intCommId = new Integer(0);
						    }
						    if (intCommId.intValue() == 1)
						    {
						    	arrAgentRec[intCounter.intValue()].setComm_id(intLevelComm);
						    }else{
						    	arrAgentRec[intCounter.intValue()].setComm_id(null);
						    }
						    arrAgentRec[intCounter.intValue()].setBm(strbm);
						    arrAgentRec[intCounter.intValue()].setSbm(strsbm);
						    intLevelComm = new Integer(intLevelComm.intValue() - 1);
		
						   // iii = new Integer(i);
						    while (intLevelId.intValue() < i)
						    {
								intCounter = new Integer(intCounter.intValue() + 1);
								arrAgentRec[intCounter.intValue()]= new Agentrec();
								arrAgentRec[intCounter.intValue()].setBisnis_id(arrAgentRec[intCounter.intValue()-1].getBisnis_id());
								arrAgentRec[intCounter.intValue()].setAgent_id(arrAgentRec[intCounter.intValue()-1].getAgent_id());
								arrAgentRec[intCounter.intValue()].setLevel_id(new Integer(5 - intCounter.intValue()));
								arrAgentRec[intCounter.intValue()].setComm_id(null);
								//AGENCY SYSTEM strjenis.intValue() == 7
							    if (strjenis.intValue() == 7 && i == 4)
								{
									if (intLevelId.intValue() < 4 && intCommId.intValue() == 1)
									{
										arrAgentRec[intCounter.intValue()].setComm_id(intLevelComm);
									 intLevelComm = new Integer(intLevelComm.intValue() - 1);
									}
								}
								arrAgentRec[intCounter.intValue()].setBm(arrAgentRec[intCounter.intValue()-1].getBm());
								arrAgentRec[intCounter.intValue()].setSbm(arrAgentRec[intCounter.intValue()-1].getSbm());

								i = (i - 1);
						    }
					   }
					}
			    }
				
			    return arrAgentRec;
			}
		
		public Agentrec[] proc_process_agent_2007(String v_strAgentId) throws ServletException,IOException
		{
			Integer intCounter = new Integer(0);
			Agentrec[]  arrAgentRec;
			arrAgentRec  = new Agentrec[5];
			String strTmpAgentId=null;
			String strLeaderId=null;
			Integer intLevelComm = new Integer(4);
			Integer intBisnisId =null ;
			Integer intLevelId =null;
			Integer strbm=null;
			Integer strsbm=null;
			Integer strjenis =null;
			Integer intCommId =null;
			Integer ii =new Integer(1);
			Integer iii = new Integer(1);
			String lcaid =null;
			String strrm = null;
			String cabang =null;

		     strLeaderId = v_strAgentId;
		     if (v_strAgentId.equalsIgnoreCase("000000"))
		     {
					strTmpAgentId = strLeaderId;
					intBisnisId = new Integer(1);
					intLevelId = new Integer(2);
					intCommId = new Integer(1);
					intCounter = new Integer(intCounter.intValue() + 1);
					arrAgentRec[intCounter.intValue()]= new Agentrec();
					arrAgentRec[intCounter.intValue()].setBisnis_id(intBisnisId);
					arrAgentRec[intCounter.intValue()].setLevel_id(new Integer( 5 - intCounter.intValue()));
					arrAgentRec[intCounter.intValue()].setAgent_id(strTmpAgentId);
				    if (intCommId==null)
				    {
				    	intCommId = new Integer(0);
				    }
					if (intCommId.intValue() == 1)
					{
						arrAgentRec[intCounter.intValue()].setComm_id(intLevelComm);
					}else{
					    arrAgentRec[intCounter.intValue()].setComm_id(null);
					}
					intLevelComm = new Integer(intLevelComm.intValue() - 1);
					iii = ii;
										
					while (intLevelId.intValue() < iii.intValue())
					{
						intCounter = new Integer(intCounter.intValue() + 1);
						arrAgentRec[intCounter.intValue()]= new Agentrec();
						arrAgentRec[intCounter.intValue()].setBisnis_id(arrAgentRec[intCounter.intValue()-1].getBisnis_id());
						arrAgentRec[intCounter.intValue()].setAgent_id(arrAgentRec[intCounter.intValue()-1].getAgent_id());
						arrAgentRec[intCounter.intValue()].setLevel_id(new Integer(5 - intCounter.intValue()));
						arrAgentRec[intCounter.intValue()].setComm_id(null);	
						iii = new Integer(iii.intValue() - 1);	
		     		}
			
		     }else{
		    	 
		    	 	Map param1 = new HashMap();							
					param1.put("strTmpAgentId",v_strAgentId);
					Map datautama = (HashMap) querySingle("select.mst_agent", param1);
					if (datautama!=null)
					{		
						cabang=(String)datautama.get("LCA_ID");
					}
					
				for (int i = 4 ; i>=1 ; i--)
				{
					
				   if ((4 - i) == (intCounter.intValue()))
				   {

						intCounter = new Integer(intCounter.intValue() + 1);
						strTmpAgentId = strLeaderId;
						
							Map param = new HashMap();							
							param.put("strTmpAgentId",strTmpAgentId);
							Map data2 = (HashMap) querySingle("select.mst_agent", param);
							if (data2!=null)
							{		
								intBisnisId = (Integer)data2.get("LSTB_ID");
								intLevelId = (Integer)data2.get("LSLE_ID");
								strLeaderId = (String)data2.get("MST_LEADER");
								intCommId = (Integer)data2.get("MSAG_KOMISI");
								strbm = (Integer)data2.get("MSAG_FLAG_BM");
								strsbm = (Integer)data2.get("MSAG_SBM");
								strjenis = (Integer)data2.get("MSAG_JENIS");
								lcaid = (String)data2.get("LCA_ID");
								strrm = (String)data2.get("MSAG_RM");
							}
							//logger.info("select mst agent");		
							if (i == 4 && strLeaderId.equalsIgnoreCase("") ) 
							{
								strLeaderId = "000606";
							}
							
						arrAgentRec[intCounter.intValue()]= new Agentrec();
					    arrAgentRec[intCounter.intValue()].setBisnis_id(intBisnisId);
					    arrAgentRec[intCounter.intValue()].setLevel_id(new Integer(5 - intCounter.intValue()));
					    arrAgentRec[intCounter.intValue()].setAgent_id(strTmpAgentId);
					    arrAgentRec[intCounter.intValue()].setLca_id(lcaid);
					    arrAgentRec[intCounter.intValue()].setMsag_rm(strrm);
					    if (intCommId==null)
					    {
					    	intCommId = new Integer(0);
					    }
					    if (intCommId.intValue() == 1)
					    {
					    	arrAgentRec[intCounter.intValue()].setComm_id(intLevelComm);
					    }else{
					    	arrAgentRec[intCounter.intValue()].setComm_id(null);
					    }
					    arrAgentRec[intCounter.intValue()].setBm(strbm);
					    arrAgentRec[intCounter.intValue()].setSbm(strsbm);
					    intLevelComm = new Integer(intLevelComm.intValue() - 1);
	
					   // iii = new Integer(i);
					    while (intLevelId.intValue() < i)
					    {
					    	try {
								intCounter = new Integer(intCounter.intValue() + 1);
								arrAgentRec[intCounter.intValue()]= new Agentrec();
								arrAgentRec[intCounter.intValue()].setBisnis_id(arrAgentRec[intCounter.intValue()-1].getBisnis_id());
								arrAgentRec[intCounter.intValue()].setAgent_id(arrAgentRec[intCounter.intValue()-1].getAgent_id());
								arrAgentRec[intCounter.intValue()].setLevel_id(new Integer(5 - intCounter.intValue()));
								arrAgentRec[intCounter.intValue()].setComm_id(null);
							    arrAgentRec[intCounter.intValue()].setLca_id(lcaid);
							    arrAgentRec[intCounter.intValue()].setMsag_rm(strrm);
								//AGENCY SYSTEM strjenis.intValue() == 7
							    if ( (strjenis.intValue() == 7 || strjenis.intValue() == 1 ||(cabang.equalsIgnoreCase("46")) || !(cabang.equalsIgnoreCase("08")|| cabang.equalsIgnoreCase("09")  || cabang.equalsIgnoreCase("42") ) )&& i == 4)
								//  if ( (strjenis.intValue() == 7 || strjenis.intValue() == 1 || !(cabang.equalsIgnoreCase("08")|| cabang.equalsIgnoreCase("09")  || cabang.equalsIgnoreCase("42") ) )&& i == 4)
				
								{
									if (intLevelId.intValue() < 4 && intCommId.intValue() == 1)
									{
										arrAgentRec[intCounter.intValue()].setComm_id(intLevelComm);
									 intLevelComm = new Integer(intLevelComm.intValue() - 1);
									}
								}
								arrAgentRec[intCounter.intValue()].setBm(arrAgentRec[intCounter.intValue()-1].getBm());
								arrAgentRec[intCounter.intValue()].setSbm(arrAgentRec[intCounter.intValue()-1].getSbm());

					    	}catch(ArrayIndexOutOfBoundsException aioobe) {
					    		
					    	}
							i = (i - 1);					    		
					    }
				   }
				}
		    }
			
		    return arrAgentRec;
		}
		
		private Agentrec[] proc_process_agent_artha_2007(String v_strAgentId) throws ServletException,IOException
		{
			Integer intCounter = new Integer(0);
			Agentrec[]  arrAgentartha1;
			arrAgentartha1  = new Agentrec[5];
			String strTmpAgentId=null;
			String strLeaderId=null;
			Integer intLevelComm = new Integer(4);
			Integer intBisnisId =null ;
			Integer intLevelId =null;
			Integer strbm=null;
			Integer strsbm=null;
			Integer strjenis =null;
			Integer intCommId =null;
			Integer ii =new Integer(1);
			Integer iii = new Integer(1);
			String lcaid =null;
			String strrm = null;
			String cabang =null;

		     strLeaderId = v_strAgentId;
		     if (v_strAgentId.equalsIgnoreCase("000000"))
		     {
					strTmpAgentId = strLeaderId;
					intBisnisId = new Integer(1);
					intLevelId = new Integer(2);
					intCommId = new Integer(1);
					intCounter = new Integer(intCounter.intValue() + 1);
					arrAgentartha1[intCounter.intValue()]= new Agentrec();
					arrAgentartha1[intCounter.intValue()].setBisnis_id(intBisnisId);
					arrAgentartha1[intCounter.intValue()].setLevel_id(new Integer( 5 - intCounter.intValue()));
					arrAgentartha1[intCounter.intValue()].setAgent_id(strTmpAgentId);
				    if (intCommId==null)
				    {
				    	intCommId = new Integer(0);
				    }
					if (intCommId.intValue() == 1)
					{
						arrAgentartha1[intCounter.intValue()].setComm_id(intLevelComm);
					}else{
					    arrAgentartha1[intCounter.intValue()].setComm_id(null);
					}
					intLevelComm = new Integer(intLevelComm.intValue() - 1);
					iii = ii;
										
					while (intLevelId.intValue() < iii.intValue())
					{
						intCounter = new Integer(intCounter.intValue() + 1);
						arrAgentartha1[intCounter.intValue()]= new Agentrec();
						arrAgentartha1[intCounter.intValue()].setBisnis_id(arrAgentartha1[intCounter.intValue()-1].getBisnis_id());
						arrAgentartha1[intCounter.intValue()].setAgent_id(arrAgentartha1[intCounter.intValue()-1].getAgent_id());
						arrAgentartha1[intCounter.intValue()].setLevel_id(new Integer(5 - intCounter.intValue()));
						arrAgentartha1[intCounter.intValue()].setComm_id(null);	
						iii = new Integer(iii.intValue() - 1);	
		     		}
			
		     }else{
		    	 
		    	 	Map param1 = new HashMap();							
					param1.put("strTmpAgentId",v_strAgentId);
					Map datautama = (HashMap) querySingle("select.mst_agent", param1);
					if (datautama!=null)
					{		
						cabang=(String)datautama.get("LCA_ID");
					}
					
				for (int i = 4 ; i>=1 ; i--)
				{
					
				   if ((4 - i) == (intCounter.intValue()))
				   {

						intCounter = new Integer(intCounter.intValue() + 1);
						strTmpAgentId = strLeaderId;
						
							Map param = new HashMap();							
							param.put("strTmpAgentId",strTmpAgentId);
							Map data2 = (HashMap) querySingle("select.mst_agent", param);
							if (data2!=null)
							{		
								intBisnisId = (Integer)data2.get("LSTB_ID");
								intLevelId = (Integer)data2.get("LSLE_ID");
								strLeaderId = (String)data2.get("MST_LEADER");
								intCommId = (Integer)data2.get("MSAG_KOMISI");
								strbm = (Integer)data2.get("MSAG_FLAG_BM");
								strsbm = (Integer)data2.get("MSAG_SBM");
								strjenis = (Integer)data2.get("MSAG_JENIS");
								lcaid = (String)data2.get("LCA_ID");
								strrm = (String)data2.get("MSAG_RM");
								//intLevelId = new Integer(intLevelId.intValue() - 2);
								if (intLevelId.intValue() <= 0 )
								{
									intLevelId = new Integer(1);
								}
							}
							if (strLeaderId == null)
							{
								strLeaderId="";
							}
							//logger.info("select mst agent");	
							if (intLevelId.intValue() == 0   && strsbm.intValue() == 0) 
							{
								strLeaderId = "";
							}
							
							if (intLevelId.intValue() == 0   && strsbm.intValue() == 1 && strLeaderId.equalsIgnoreCase("")) 
							{
								strLeaderId = strTmpAgentId;
							}
							
							Integer level_leader = null;
							if (!strLeaderId.equalsIgnoreCase(""))
							{
								
								Map param4 = new HashMap();							
								param4.put("strTmpAgentId",strLeaderId);
								Map data4 = (HashMap) querySingle("select.mst_agent", param4);
								if (data4!=null)
								{
									level_leader = (Integer) data4.get("LSLE_ID");
									if ((level_leader.intValue() == 0)  && intLevelId.intValue() == 0 )
									{
										if (i > 0)
										strLeaderId = "";
									}
								}
							}
							
							if (i == 4 && strLeaderId.equalsIgnoreCase("") ) 
							{
								strLeaderId = "000606";
							}
							
						if (intLevelId.intValue() <= i)
						{
							arrAgentartha1[intCounter.intValue()]= new Agentrec();
						    arrAgentartha1[intCounter.intValue()].setBisnis_id(intBisnisId);
						    arrAgentartha1[intCounter.intValue()].setLevel_id(new Integer(5 - intCounter.intValue()));
						    arrAgentartha1[intCounter.intValue()].setAgent_id(strTmpAgentId);
						    arrAgentartha1[intCounter.intValue()].setLca_id(lcaid);
						    arrAgentartha1[intCounter.intValue()].setMsag_rm(strrm);
						    if (intCommId==null)
						    {
						    	intCommId = new Integer(0);
						    }
						    if (intCommId.intValue() == 1)
						    {
						    	arrAgentartha1[intCounter.intValue()].setComm_id(intLevelComm);
						    }else{
						    	arrAgentartha1[intCounter.intValue()].setComm_id(null);
						    }
						    arrAgentartha1[intCounter.intValue()].setBm(strbm);
						    arrAgentartha1[intCounter.intValue()].setSbm(strsbm);
						    intLevelComm = new Integer(intLevelComm.intValue() - 1);

						   // iii = new Integer(i);
						    while ((intLevelId.intValue()) < i)
						    {
								intCounter = new Integer(intCounter.intValue() + 1);
								arrAgentartha1[intCounter.intValue()]= new Agentrec();
								arrAgentartha1[intCounter.intValue()].setBisnis_id(arrAgentartha1[intCounter.intValue()-1].getBisnis_id());
								arrAgentartha1[intCounter.intValue()].setAgent_id(arrAgentartha1[intCounter.intValue()-1].getAgent_id());
								arrAgentartha1[intCounter.intValue()].setLevel_id(new Integer(5 - intCounter.intValue()));
								arrAgentartha1[intCounter.intValue()].setComm_id(null);
							    arrAgentartha1[intCounter.intValue()].setLca_id(lcaid);
							    arrAgentartha1[intCounter.intValue()].setMsag_rm(strrm);
								//AGENCY SYSTEM strjenis.intValue() == 7
							    if ( (cabang.equalsIgnoreCase("46")) && i == 4)
								{
									if (intLevelId.intValue() < 4 && intCommId.intValue() == 1)
									{
										arrAgentartha1[intCounter.intValue()].setComm_id(intLevelComm);
									 intLevelComm = new Integer(intLevelComm.intValue() - 1);
									}
								}
								arrAgentartha1[intCounter.intValue()].setBm(arrAgentartha1[intCounter.intValue()-1].getBm());
								arrAgentartha1[intCounter.intValue()].setSbm(arrAgentartha1[intCounter.intValue()-1].getSbm());
	
								i = (i - 1);
						    }
				   		}else{
							i=i+1;
							intCounter = intCounter - 1;
						}
				   }
				}
		    }
			
		    return arrAgentartha1;
		}
		
		private Agentrec[] proc_agent_artha(String v_strAgentId) throws ServletException,IOException
		{
			Integer intCounter = new Integer(0);
			Agentrec[]  arrAgentArtha;
			arrAgentArtha  = new Agentrec[7];
			String strTmpAgentId=null;
			String strLeaderId=null;
			Integer intLevelComm = new Integer(6);
			Integer intBisnisId =null ;
			Integer intLevelId =null;
			Integer intLevelId1 = new Integer(2);
			Integer strbm=null;
			Integer strsbm=null;
			Integer strjenis =null;
			Integer intCommId =null;
			Integer ii =new Integer(1);
			Integer iii = new Integer(1);
			String lcaid =null;
			String strrm = null;
			String cabang =null;

		     strLeaderId = v_strAgentId;
		     if (v_strAgentId.equalsIgnoreCase("000000"))
		     {
					strTmpAgentId = strLeaderId;
					intBisnisId = new Integer(1);
					intLevelId = new Integer(2);
					intCommId = new Integer(1);
					intCounter = new Integer(intCounter.intValue() + 1);
					arrAgentArtha[intCounter.intValue()]= new Agentrec();
					arrAgentArtha[intCounter.intValue()].setBisnis_id(intBisnisId);
					arrAgentArtha[intCounter.intValue()].setLevel_id(new Integer( 7 - intCounter.intValue()));
					arrAgentArtha[intCounter.intValue()].setAgent_id(strTmpAgentId);
				    if (intCommId==null)
				    {
				    	intCommId = new Integer(0);
				    }
					if (intCommId.intValue() == 1)
					{
						arrAgentArtha[intCounter.intValue()].setComm_id(intLevelComm);
					}else{
					    arrAgentArtha[intCounter.intValue()].setComm_id(null);
					}
					intLevelComm = new Integer(intLevelComm.intValue() - 1);
					iii = ii;
										
					while (intLevelId.intValue() < iii.intValue())
					{
						intCounter = new Integer(intCounter.intValue() + 1);
						arrAgentArtha[intCounter.intValue()]= new Agentrec();
						arrAgentArtha[intCounter.intValue()].setBisnis_id(arrAgentArtha[intCounter.intValue()-1].getBisnis_id());
						arrAgentArtha[intCounter.intValue()].setAgent_id(arrAgentArtha[intCounter.intValue()-1].getAgent_id());
						arrAgentArtha[intCounter.intValue()].setLevel_id(new Integer(7 - intCounter.intValue()));
						arrAgentArtha[intCounter.intValue()].setComm_id(null);	
						iii = new Integer(iii.intValue() - 1);	
		     		}
			
		     }else{
		    	 
		    	 	Map param1 = new HashMap();							
					param1.put("strTmpAgentId",v_strAgentId);
					Map datautama = (HashMap) querySingle("select.mst_agent", param1);
					if (datautama!=null)
					{		
						cabang=(String)datautama.get("LCA_ID");
					}
					
				for (int i = 6 ; i>=1 ; i--)
				{
					
				   if ((6 - i) == (intCounter.intValue()))
				   {

						intCounter = new Integer(intCounter.intValue() + 1);
						strTmpAgentId = strLeaderId;
						
							Map param = new HashMap();							
							param.put("strTmpAgentId",strTmpAgentId);
							Map data2 = (HashMap) querySingle("select.mst_agent", param);
							if (data2!=null)
							{		
								intBisnisId = (Integer)data2.get("LSTB_ID");
								intLevelId = (Integer)data2.get("LSLE_ID");
								strLeaderId = (String)data2.get("MST_LEADER");
								intCommId = (Integer)data2.get("MSAG_KOMISI");
								strbm = (Integer)data2.get("MSAG_FLAG_BM");
								strsbm = (Integer)data2.get("MSAG_SBM");
								strjenis = (Integer)data2.get("MSAG_JENIS");
								lcaid = (String)data2.get("LCA_ID");
								strrm = (String)data2.get("MSAG_RM");
								if (intLevelId.intValue()>0)
								{
									intLevelId1 = new Integer(intLevelId.intValue() + 2);
								}else{
									if (strsbm.intValue() == 1)
									{
										intLevelId1  = new Integer(2);
									}else{
										intLevelId1 = new Integer(1);
									}
								}
							}
							

							//logger.info("select mst agent");		
							if (i == 6 && strLeaderId.equalsIgnoreCase("") ) 
							{
								strLeaderId = strTmpAgentId;
								if (intLevelId.intValue() == 0  && strsbm.intValue() == 1)
								{
									strLeaderId = strrm;
								}
							}
							
							if (intLevelId.intValue() == 0   && strsbm.intValue() == 1 && strLeaderId.equalsIgnoreCase("")) 
							{
								strLeaderId = strrm;
							}
					int b =i-2;
					if (b <0)
					{
						b= 1;
					}
					if (intLevelId.intValue() <= b)
					{		
						arrAgentArtha[intCounter.intValue()]= new Agentrec();
					    arrAgentArtha[intCounter.intValue()].setBisnis_id(intBisnisId);
					    arrAgentArtha[intCounter.intValue()].setLevel_id(new Integer(7 - intCounter.intValue()));
					    arrAgentArtha[intCounter.intValue()].setAgent_id(strTmpAgentId);
					    arrAgentArtha[intCounter.intValue()].setLca_id(lcaid);
					    arrAgentArtha[intCounter.intValue()].setMsag_rm(strrm);
					    if (intCommId==null)
					    {
					    	intCommId = new Integer(0);
					    }
					    if (intCommId.intValue() == 1)
					    {
					    	arrAgentArtha[intCounter.intValue()].setComm_id(intLevelComm);
					    }else{
					    	arrAgentArtha[intCounter.intValue()].setComm_id(null);
					    }
					    arrAgentArtha[intCounter.intValue()].setBm(strbm);
					    arrAgentArtha[intCounter.intValue()].setSbm(strsbm);
					    intLevelComm = new Integer(intLevelComm.intValue() - 1);
	
					   // iii = new Integer(i);
					    while (intLevelId1.intValue() < i)
					    {
							intCounter = new Integer(intCounter.intValue() + 1);
							arrAgentArtha[intCounter.intValue()]= new Agentrec();
							arrAgentArtha[intCounter.intValue()].setBisnis_id(arrAgentArtha[intCounter.intValue()-1].getBisnis_id());
							arrAgentArtha[intCounter.intValue()].setAgent_id(arrAgentArtha[intCounter.intValue()-1].getAgent_id());
							arrAgentArtha[intCounter.intValue()].setLevel_id(new Integer(7 - intCounter.intValue()));
							arrAgentArtha[intCounter.intValue()].setComm_id(null);
						    arrAgentArtha[intCounter.intValue()].setLca_id(lcaid);
						    arrAgentArtha[intCounter.intValue()].setMsag_rm(strrm);
							//AGENCY SYSTEM strjenis.intValue() == 7
						    if ( cabang.equalsIgnoreCase("46") && i == 6)
							{
								if (intLevelId1.intValue() < 6 && intCommId.intValue() == 1)
								{
									arrAgentArtha[intCounter.intValue()].setComm_id(intLevelComm);
								 intLevelComm = new Integer(intLevelComm.intValue() - 1);
								}
							}
							arrAgentArtha[intCounter.intValue()].setBm(arrAgentArtha[intCounter.intValue()-1].getBm());
							arrAgentArtha[intCounter.intValue()].setSbm(arrAgentArtha[intCounter.intValue()-1].getSbm());

							i = (i - 1);
					    }
					}else{
						i=i+1;
						intCounter = intCounter - 1;
					}
				   }
				}
		    }
			
		    return arrAgentArtha;
		}		
			
		private Agentrec[] proc_process_agentao(String v_strAgentId) throws ServletException,IOException
			{
				Integer intCounter = new Integer(0);
				Agentrec[]  arrAgentRec1;
				arrAgentRec1  = new Agentrec[5];
				String strTmpAgentId=null;
				String strLeaderId=null;
				Integer intLevelComm = new Integer(4);
				Integer intBisnisId =null ;
				Integer intLevelId =null;
				Integer strbm=null;
				Integer strsbm=null;
				Integer strjenis =null;
				Integer intCommId =null;
				strLeaderId = v_strAgentId;
				Map mapAgentCodeAO = bacDao.selectAgentCodeAO(v_strAgentId);
				String code_lvl3 = (String) mapAgentCodeAO.get("AGENT_3");
				String code_lvl2 = (String) mapAgentCodeAO.get("AGENT_2");
				String code_lvl1 = (String) mapAgentCodeAO.get("AGENT_1");
				
					for (int i = 4 ; i>=1 ; i--)
					{
						
					   if ((4 - i) == (intCounter.intValue()))
					   {

							intCounter = new Integer(intCounter.intValue() + 1);
							strTmpAgentId = strLeaderId;
							if(Common.isEmpty(strTmpAgentId)){
								if(i==3){
									strTmpAgentId=code_lvl3;
								}else if(i==2){
									strTmpAgentId=code_lvl2;
								}else if(i==1){
									strTmpAgentId=code_lvl1;
								}
							}
								Map param = new HashMap();							
								param.put("strTmpAgentId",strTmpAgentId);
								Map data2 = (HashMap) querySingle("select.mst_agent", param);
								if (data2!=null)
								{		
									intBisnisId = (Integer)data2.get("LSTB_ID");
									intLevelId = (Integer)data2.get("LSLE_ID");
									strLeaderId = (String)data2.get("MST_LEADER");
									intCommId = (Integer)data2.get("MSAG_KOMISI");
									strbm = (Integer)data2.get("MSAG_FLAG_BM");
									strsbm = (Integer)data2.get("MSAG_SBM");
									strjenis = (Integer)data2.get("MSAG_JENIS");
								}
								//logger.info("select mst agent");		
								if (i == 4 && strLeaderId.equalsIgnoreCase("") ) 
								{
									strLeaderId = "000606";
								}
								
							arrAgentRec1[intCounter.intValue()]= new Agentrec();
						    arrAgentRec1[intCounter.intValue()].setBisnis_id(intBisnisId);
						    arrAgentRec1[intCounter.intValue()].setLevel_id(new Integer(5 - intCounter.intValue()));
						    arrAgentRec1[intCounter.intValue()].setAgent_id(strTmpAgentId);
						    if (intCommId.intValue() == 1)
						    {
						    	arrAgentRec1[intCounter.intValue()].setComm_id(intLevelComm);
						    }else{
						    	arrAgentRec1[intCounter.intValue()].setComm_id(null);
						    }
						    arrAgentRec1[intCounter.intValue()].setBm(strbm);
						    arrAgentRec1[intCounter.intValue()].setSbm(strsbm);
				    
						    intLevelComm = new Integer(intLevelComm.intValue() - 1);
		
						    //iii = new Integer(i);
						    while (intLevelId.intValue() < i)
						    {
								intCounter = new Integer(intCounter.intValue() + 1);
								arrAgentRec1[intCounter.intValue()]= new Agentrec();
								arrAgentRec1[intCounter.intValue()].setBisnis_id(arrAgentRec1[intCounter.intValue()-1].getBisnis_id());
								arrAgentRec1[intCounter.intValue()].setAgent_id(arrAgentRec1[intCounter.intValue()-1].getAgent_id());
								arrAgentRec1[intCounter.intValue()].setLevel_id(new Integer(5 - intCounter.intValue()));
								arrAgentRec1[intCounter.intValue()].setComm_id(null);
								if (strjenis.intValue() == 7 && i == 4)
								{
									if (intLevelId.intValue() < 4 && intCommId.intValue() == 1)
									{
										arrAgentRec1[intCounter.intValue()].setComm_id(intLevelComm);
									 intLevelComm = new Integer(intLevelComm.intValue() - 1);
									}
								}
								arrAgentRec1[intCounter.intValue()].setBm(arrAgentRec1[intCounter.intValue()-1].getBm());
								arrAgentRec1[intCounter.intValue()].setSbm(arrAgentRec1[intCounter.intValue()-1].getSbm());
								i = (i - 1);
						    }
					   }
					}
			    return arrAgentRec1;
			}		

		private void proc_save_data_ttg (Cmdeditbac edit, String strInsClientID, Date v_strDateNow )throws ServletException,IOException
		{
		
				//*******data tertanggung******
			
				//tujuan
				String tujuan_asr_ttg =null;//--
				if (edit.getTertanggung().getTujuana()=="")
				{
					tujuan_asr_ttg=edit.getTertanggung().getMkl_tujuan().toUpperCase();
				}else{
					tujuan_asr_ttg=edit.getTertanggung().getTujuana().toUpperCase();
				}
				edit.getTertanggung().setMkl_tujuan(tujuan_asr_ttg);
				
				//sumber dana
				String sumber_dana_ttg=null;//--
				if (edit.getTertanggung().getDanaa()=="")
				{
					sumber_dana_ttg = edit.getTertanggung().getMkl_pendanaan().toUpperCase();
				}else{
					sumber_dana_ttg = edit.getTertanggung().getDanaa().toUpperCase();
				}
				edit.getTertanggung().setMkl_pendanaan(sumber_dana_ttg);
				
				//sumber penghasilan
				String sumber_hasil_ttg=null;//--
				if (edit.getTertanggung().getDanaa2()=="")
				{
					sumber_hasil_ttg = edit.getTertanggung().getMkl_smbr_penghasilan().toUpperCase();
				}else{
					sumber_hasil_ttg = edit.getTertanggung().getDanaa2().toUpperCase();
				}
				edit.getTertanggung().setMkl_smbr_penghasilan((sumber_hasil_ttg));
				
			
				//pekerjaan
				String pekerjaan_ttg =null;//--
				if (edit.getTertanggung().getKerjaa()=="")
				{
					pekerjaan_ttg = edit.getTertanggung().getMkl_kerja().toUpperCase();
				}else{
					pekerjaan_ttg = edit.getTertanggung().getKerjaa().toUpperCase();
				}
				edit.getTertanggung().setMkl_kerja(pekerjaan_ttg);
				
				//group job
				String groupjob_ttg = null;
				Map data1= (HashMap) bacDao.select_groupjob(pekerjaan_ttg);
				if (data1!=null)
				{		
					groupjob_ttg = (String)data1.get("LGJ_ID");
				}else{
					groupjob_ttg="";
				}
				edit.getTertanggung().setLgj_id(groupjob_ttg);
				
				//jabatan
				String jabatan_ttg = edit.getTertanggung().getKerjab().toUpperCase();
				String jbtn_ttg ="";
				if (jabatan_ttg.trim().length()!=0)
				{
					pekerjaan_ttg = jabatan_ttg;
					
					Map data2= (HashMap) bacDao.select_jabatan(jabatan_ttg);
					if (data2!=null)
					{		
						jbtn_ttg = (String)data2.get("LJB_ID");
					}else{
						jbtn_ttg="";
					}
				}
				edit.getTertanggung().setLjb_id(jbtn_ttg);
				
				//bidang industri
				String bidang_ttg = null;//--
				if (edit.getTertanggung().getIndustria()=="")
				{
					bidang_ttg=edit.getTertanggung().getMkl_industri().toUpperCase();
				}else{
					bidang_ttg=edit.getTertanggung().getIndustria().toUpperCase();
				}
				edit.getTertanggung().setMkl_industri(bidang_ttg);
				if (edit.getTertanggung().getMkl_kerja().equalsIgnoreCase("KARYAWAN SWASTA"))
				{
					edit.getTertanggung().setMpn_job_desc(jabatan_ttg);
				}else{
					edit.getTertanggung().setMpn_job_desc(bidang_ttg);
				}
				edit.getTertanggung().setLus_id(edit.getPemegang().getLus_id());
				edit.getTertanggung().setMcl_id(strInsClientID);
				edit.getTertanggung().setMcl_jenis(new Integer(0));
				edit.getTertanggung().setMcl_blacklist(new Integer(0));
				if (edit.getPemegang().getLsre_id().intValue() == 1)
				{
					edit.getTertanggung().setEmail(edit.getPemegang().getEmail());
				}
				edit.getTertanggung().setMspe_email(edit.getTertanggung().getEmail());
				
		//		-------------------------------------------------
				// Insert Insured Client information to MST_CLIENT_NEW
				
				int rowupdated = update("update.mst_clientttg", edit.getTertanggung());
				if (rowupdated ==0)
				{
					insert("insert.mst_clientttg", edit.getTertanggung());
					//logger.info("insert mst client ttg");
				}

				//		-------------------------------------------------
				// Insert Insured Client information to MST_ADDRESS_NEW
				int rowupdated1 = update("update.mst_addressttg", edit.getTertanggung());
				if (rowupdated1 ==0)
				{
					insert("insert.mst_addressttg", edit.getTertanggung());
					//logger.info("insert mst address ttg");
				}
		}
		
		private void proc_save_data_pp(Cmdeditbac edit, String strPOClientID,Date v_strDateNow  )throws ServletException,IOException
		{
			//**********data pemegang polis************

			//tujuan
			String tujuan_asr =null;//--
			if (edit.getPemegang().getTujuana()=="")
			{
				tujuan_asr=edit.getPemegang().getMkl_tujuan().toUpperCase();
			}else{
				tujuan_asr=edit.getPemegang().getTujuana().toUpperCase();
			}
			edit.getPemegang().setMkl_tujuan(tujuan_asr);
			
			//sumber dana
			String sumber_dana=null;//--
			if (edit.getPemegang().getDanaa()=="")
			{
				sumber_dana = edit.getPemegang().getMkl_pendanaan().toUpperCase();
			}else{
				sumber_dana = edit.getPemegang().getDanaa().toUpperCase();
			}
			edit.getPemegang().setMkl_pendanaan(sumber_dana);
			
			String sumber_dana2=null;//--
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				edit.getPemegang().setMkl_smbr_penghasilan("");
			}else{
				if (edit.getPemegang().getDanaa2()=="" || edit.getPemegang().getDanaa2()== null)
				{
					sumber_dana2 = edit.getPemegang().getMkl_smbr_penghasilan().toUpperCase();
				}else{
					sumber_dana2 = edit.getPemegang().getDanaa2().toUpperCase();
				}
				edit.getPemegang().setMkl_smbr_penghasilan(sumber_dana2);
			}
			
			//pekerjaan
			String pekerjaan =null;//--
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				edit.getPemegang().setMkl_kerja("");
			}else{
				if (edit.getPemegang().getKerjaa()=="")
				{
					pekerjaan = edit.getPemegang().getMkl_kerja().toUpperCase();
				}else{
					pekerjaan = edit.getPemegang().getKerjaa().toUpperCase();
				}
				edit.getPemegang().setMkl_kerja(pekerjaan);
			}

			//group job
			String groupjob_pp = null;
			Map data3= (HashMap) bacDao.select_groupjob(pekerjaan);
			if (data3!=null)
			{		
				groupjob_pp = (String)data3.get("LGJ_ID");
			}else{
				groupjob_pp="";
			}
			edit.getPemegang().setLgj_id(groupjob_pp);
			
			//jabatan
			String jabatan = edit.getPemegang().getKerjab().toUpperCase();
			String jbtn_pp ="";
			if (jabatan.trim().length()!=0)
			{
				pekerjaan = jabatan;
				Map data4= (HashMap) bacDao.select_jabatan(jabatan);
				if (data4!=null)
				{		
					jbtn_pp = (String)data4.get("LJB_ID");
				}else{
					jbtn_pp = "";
				}
			}
			edit.getPemegang().setLjb_id(jbtn_pp);
			
			//bidang industri
			String bidang = null;//--
			if (edit.getPemegang().getIndustria()=="")
			{
				bidang=edit.getPemegang().getMkl_industri().toUpperCase();
			}else{
				bidang=edit.getPemegang().getIndustria().toUpperCase();
			}	
			edit.getPemegang().setMkl_industri(bidang);
			if (edit.getPemegang().getMkl_kerja().equalsIgnoreCase("KARYAWAN SWASTA"))
			{
				edit.getPemegang().setMpn_job_desc(jabatan);
			}else{
				edit.getPemegang().setMpn_job_desc(bidang);
			}
			edit.getPemegang().setMcl_id(strPOClientID);
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
				edit.getPemegang().setMcl_jenis(new Integer(1));
			}else{
				edit.getPemegang().setMcl_jenis(new Integer(0));
			}
			edit.getPemegang().setMcl_blacklist(new Integer(0));
			edit.getPemegang().setMspe_email(edit.getPemegang().getEmail());
			
			//TAMBAHAN UNTUK INPUT BADAN HUKUM / USAHA
			edit.getPersonal().setMcl_id(edit.getPemegang().getMcl_id());
			edit.getPersonal().setFlag_ws(0);
			edit.getPersonal().setLca_id(edit.getAgen().getLca_id());
			edit.getPersonal().setLwk_id(edit.getAgen().getLwk_id());
			edit.getPersonal().setLsrg_id(edit.getAgen().getLsrg_id());
			edit.getPersonal().setLsrg_nama(edit.getAgen().getLsrg_nama());
			edit.getPersonal().setMpt_contact(edit.getContactPerson().getNama_lengkap().toUpperCase());
			edit.getPersonal().setMcl_first(edit.getPemegang().getMcl_first());
			//List<DropDown> gelar = ((List<DropDown>)query("selectGelar", 1));
			Map<String, String> params = new HashMap<String, String>();
			params.put("flag", "0");
			List<DropDown> bidangUsaha = ((List<DropDown>)query("selectBidangUsaha", params));
			if(bidangUsaha != null){
				for(int i = 0 ; i < bidangUsaha.size() ; i++){
					if((bidangUsaha.get(i).getValue().toUpperCase()).equals(edit.getPemegang().getMkl_industri())){
						edit.getPersonal().setLju_id(Integer.parseInt(bidangUsaha.get(i).getKey()));
						i = bidangUsaha.size();
					}
				}
			}
			edit.getPersonal().setMpt_usaha_desc(edit.getPemegang().getMkl_industri());
			edit.getPersonal().setMpt_contact(edit.getContactPerson().getNama_lengkap().toUpperCase());
			if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
//				if(edit.getPemegang().getLti_id() != null && gelar != null){
//					for(int i = 0 ; i < gelar.size() ; i++){
//						if((gelar.get(i).getKey()).equals(edit.getPemegang().getLti_id().toString())){
//							edit.getPemegang().setMcl_gelar(gelar.get(i).getValue());
//							i = gelar.size();
//						}
//					}
//				}
				edit.getPemegang().setLside_id(7);
				edit.getPemegang().setMspe_no_identity(edit.getPersonal().getMpt_npwp());
				edit.getPemegang().setAlamat_kantor(edit.getPemegang().getAlamat_rumah());
				edit.getPemegang().setArea_code_kantor(edit.getPemegang().getArea_code_rumah());
				if("-".equals(edit.getPemegang().getArea_code_kantor()))edit.getPemegang().setArea_code_kantor("");
				edit.getPemegang().setArea_code_kantor2(edit.getPemegang().getArea_code_rumah2());
				edit.getPemegang().setTelpon_kantor(edit.getPemegang().getTelpon_rumah());
				if("-".equals(edit.getPemegang().getTelpon_kantor()))edit.getPemegang().setTelpon_kantor("");
				edit.getPemegang().setTelpon_kantor2(edit.getPemegang().getTelpon_rumah2());
				edit.getPemegang().setKd_pos_kantor(edit.getPemegang().getKd_pos_rumah());
				edit.getPemegang().setKota_kantor(edit.getPemegang().getKota_rumah());
			}
			//
			
			/*------------------------------------------------------------
			 Insert Policy Holder Client information to MST_CLIENT*/
				int rowupdated = update("update.mst_clientpp", edit.getPemegang());
//				logger.info("update mst client pp");
				if (rowupdated==0)
				{
					insert("insert.mst_clientpp", edit.getPemegang());
					//logger.info("insert mst client pp");
				}
				
			//------------------------------------------------------------
			// Insert Policy Holder Home Address information to MST_ADDRESS
				int rowupdated1 = update("update.mst_addresspp", edit.getPemegang());
//				logger.info("update mst address pp");
				if (rowupdated1==0)
				{
					insert("insert.mst_addresspp", edit.getPemegang());
					//logger.info("insert mst address pp");
				}
				
				/*------------------------------------------------------------
				 Insert Policy Holder Company information to MST_COMPANY*/
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						int rowupdated2 = update("updateMstCompany",edit.getPersonal());
						//logger.info("update mst client pp");
						if (rowupdated2==0)
						{
							insert("insertMstCompany", edit.getPersonal());
							//logger.info("insert mst company pp");
						}
					}
				//------------------------------------------------------------
					//Insert Policy Holder Home Address Company information to MST_ADDRESS_NEW
					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
						int rowupdated3 = update("updateMstCompanyAddress", edit.getPemegang());
						//logger.info("update mst client pp");
						if (rowupdated3==0)
						{
							insert("insertMstCompanyAddress", edit.getPemegang());
							//logger.info("insert mst company pp");
						}
					}
				//------------------------------------------------------------
					//Insert PIC Company information to MST_COMPANY_CONTACT
//					if(edit.getDatausulan().getJenis_pemegang_polis() == 1){
//						int rowupdated4 = update("updateMstCompanyAddress", edit.getPemegang());
//						//logger.info("update mst client pp");
//						if (rowupdated4==0)
//						{
//							insert("insertMstCompanyAddress", edit.getPemegang());
//							//logger.info("insert mst company pp");
//						}
//					}	
//			
		}
		
		private void proc_save_mst_insured(Cmdeditbac edit,String strInsClientID,String strTmpSPAJ)throws ServletException,IOException
		{
			
			//**********insert mst insured ***************
			edit.getDatausulan().setReg_spaj(strTmpSPAJ);
			edit.getTertanggung().setMste_insured(strInsClientID);
			edit.getTertanggung().setMste_age(edit.getTertanggung().getUsiattg());
			Date v_strPaymentDate=null;
			Date strDebitDate= null;
			v_strPaymentDate = edit.getPemegang().getMste_tgl_recur();			
			if (v_strPaymentDate != null)
			{
				strDebitDate = v_strPaymentDate;
			}	
			edit.getPemegang().setMste_tgl_recur(strDebitDate);
			edit.getDatausulan().setLus_id(edit.getPemegang().getLus_id());
			
			Integer v_intAutoDebet = edit.getDatausulan().getMste_flag_cc();
			Integer lssa_id = new Integer(0);
			Integer flag_worksite = edit.getDatausulan().getFlag_worksite();
			if (flag_worksite.intValue()==1 && v_intAutoDebet.intValue() == 3)
			{
				lssa_id = new Integer(3);
			}else{
				lssa_id = new Integer(1);
			}
			edit.getDatausulan().setLssa_id(lssa_id);
			
			Integer flag_el = edit.getDatausulan().getMste_flag_el();
			if (flag_el == null)
			{
				edit.getDatausulan().setMste_flag_el(new Integer(0));
			}
			
			Integer flag_investasi = edit.getDatausulan().getMste_flag_investasi();
			if (flag_investasi ==null)
			{
				edit.getDatausulan().setMste_flag_investasi(new Integer(0));
			}
			
			Map param = new HashMap();
			
			//(Yusuf) flag menandakan bahwa SPAJ asli atau fotokopian
			//kalau fotokopian, gak boleh print tanda terima polis (di print polis) - khusus bancass
			if(edit.getPemegang().getMste_spaj_asli() == null) edit.getPemegang().setMste_spaj_asli(1);
			
			//(Deddy) Req Rudi : Apabila cabang 01, maka otomatis set flag_karyawan(mste_flag_el) = 1, selain itu 0
			//(4 May 2011) REQ Rudi : untuk cabang 01, mste_flag_el tidak otomatis diset 1.namun berdasarkan inputan user.
//			if(edit.getAgen().getLca_id().equals("01")){
//				edit.getDatausulan().setMste_flag_el(1);
//			}else{
//				edit.getDatausulan().setMste_flag_el(0);
//			}
			if(Common.isEmpty(edit.getDatausulan().getMste_flag_el())){
				edit.getDatausulan().setMste_flag_el(0);
			}
			
			param.put("pemegang",edit.getPemegang());
			param.put("tertanggung",edit.getTertanggung());
			param.put("datausulan",edit.getDatausulan());
			param.put("agen",edit.getAgen());
			int rowupdated = update("update.mst_insured", param);
			//edit mst insured
			if ( rowupdated==0)
			{
				
				insert("insert.mst_insured", param);
				//logger.info("insert mst insured");	
			}
		}
		
		private void proc_save_mst_insured_pas(Cmdeditbac edit,String strInsClientID,String strTmpSPAJ)throws ServletException,IOException
		{
			
			//**********insert mst insured ***************
			edit.getDatausulan().setReg_spaj(strTmpSPAJ);
			edit.getTertanggung().setMste_insured(strInsClientID);
			edit.getTertanggung().setMste_age(edit.getTertanggung().getUsiattg());
			Date v_strPaymentDate=null;
			Date strDebitDate= null;
			v_strPaymentDate = edit.getPemegang().getMste_tgl_recur();			
			if (v_strPaymentDate != null)
			{
				strDebitDate = v_strPaymentDate;
			}	
			edit.getPemegang().setMste_tgl_recur(strDebitDate);
			edit.getDatausulan().setLus_id(edit.getPemegang().getLus_id());
			
			Integer v_intAutoDebet = edit.getDatausulan().getMste_flag_cc();
			Integer lssa_id = new Integer(0);
			Integer flag_worksite = edit.getDatausulan().getFlag_worksite();
//			if (flag_worksite.intValue()==1 && v_intAutoDebet.intValue() == 3)
//			{
				lssa_id = new Integer(5);
//			}else{
//				lssa_id = new Integer(1);
//			}
			edit.getDatausulan().setLssa_id(lssa_id);
			
			Integer flag_el = edit.getDatausulan().getMste_flag_el();
			if (flag_el == null)
			{
				edit.getDatausulan().setMste_flag_el(new Integer(0));
			}
			
			Integer flag_investasi = edit.getDatausulan().getMste_flag_investasi();
			if (flag_investasi ==null)
			{
				edit.getDatausulan().setMste_flag_investasi(new Integer(0));
			}
			
			Map param = new HashMap();
			
			//(Yusuf) flag menandakan bahwa SPAJ asli atau fotokopian
			//kalau fotokopian, gak boleh print tanda terima polis (di print polis) - khusus bancass
			if(edit.getPemegang().getMste_spaj_asli() == null) edit.getPemegang().setMste_spaj_asli(1);
			
			//(Deddy) Req Rudi : Apabila cabang 01, maka otomatis set flag_karyawan(mste_flag_el) = 1, selain itu 0
			if(edit.getAgen().getLca_id().equals("01")){
				edit.getDatausulan().setMste_flag_el(1);
			}else{
				edit.getDatausulan().setMste_flag_el(0);
			}
			
			param.put("pemegang",edit.getPemegang());
			param.put("tertanggung",edit.getTertanggung());
			param.put("datausulan",edit.getDatausulan());
			param.put("agen",edit.getAgen());
			int rowupdated = update("update.mst_insured", param);
			//edit mst insured
			if ( rowupdated==0)
			{
				
				insert("insert.mst_insured_pas", param);
				//logger.info("insert mst insured");	
			}
		}
		
		private void proc_save_addr_bill(Cmdeditbac edit,String strTmpSPAJ)throws ServletException,IOException
		{
			String v_strBillRegion=edit.getAddressbilling().getRegion();
			edit.getAddressbilling().setReg_spaj(strTmpSPAJ);
			if(edit.getAddressbilling().getKota() != null) {
				edit.getAddressbilling().setLska_id(bacDao.select_kabupaten(edit.getAddressbilling().getKota().toUpperCase().trim()));
			}
			edit.getAddressbilling().setLca_id(FormatString.rpad("0",(v_strBillRegion.substring(0,2)),2));
			edit.getAddressbilling().setLwk_id(FormatString.rpad("0",(v_strBillRegion.substring(2,4)),2));
			edit.getAddressbilling().setLsrg_id(FormatString.rpad("0",(v_strBillRegion.substring(4,6)),2));
			edit.getAddressbilling().setKota(edit.getAddressbilling().getKota_tgh());
			edit.getAddressbilling().setFlag_cc(edit.getDatausulan().getMste_flag_cc());
			//------------------------------------------------------------
			// Insert Insured information to MST_ADDRESS_BILLING
				int rowupdated = update("update.mst_address_billing", edit.getAddressbilling());
//				logger.info("update mst billing");
				if (rowupdated ==0)
				{
					insert("insert.mst_address_billing", edit.getAddressbilling());
					//logger.info("insert mst billing");
				}
		}
		
		
		private void proc_save_region_bill(Cmdeditbac edit,String strTmpSPAJ)throws ServletException,IOException
		{
			//*********address billing *****************
			String v_strBillRegion=edit.getAddressbilling().getRegion();
			edit.getAddressbilling().setReg_spaj(strTmpSPAJ);
			edit.getAddressbilling().setLca_id(FormatString.rpad("0",(v_strBillRegion.substring(0,2)),2));
			edit.getAddressbilling().setLwk_id(FormatString.rpad("0",(v_strBillRegion.substring(2,4)),2));
			edit.getAddressbilling().setLsrg_id(FormatString.rpad("0",(v_strBillRegion.substring(4,6)),2));

			//------------------------------------------------------------
			// Insert Insured information to MST_ADDRESS_BILLING
				int rowupdated = update("update.region_billing", edit.getAddressbilling());
		}			
		
		private void proc_save_rek_client(Cmdeditbac edit,String strTmpSPAJ)throws ServletException,IOException
		{

			String keterangan = edit.getRekening_client().getNotes();
	
			String v_strregionid = edit.getAgen().getKode_regional().toUpperCase();
			Integer jns_nsbh=null;
			if ((FormatString.rpad("0",(v_strregionid.substring(0,4)),4).equalsIgnoreCase("0914")))
			{
				jns_nsbh=edit.getRekening_client().getMrc_jn_nasabah();
			}
			edit.getRekening_client().setMrc_jn_nasabah(jns_nsbh);

			if (keterangan == null)
			{
				keterangan ="";
			}
			if (keterangan.equalsIgnoreCase(""))
			{
				keterangan = "Input Rekening Client";
			}
			edit.getRekening_client().setNotes(keterangan);
			edit.getRekening_client().setReg_spaj(strTmpSPAJ);
			edit.getRekening_client().setLus_id(edit.getPemegang().getLus_id());
			int rowupdated = update("update.mst_rek_client",edit.getRekening_client());
			//edit mst rek_client
			if (rowupdated==0)
			{
				insert("insert.mst_rek_client", edit.getRekening_client());
				//logger.info("insert rek cliet");
			}
			
			insert("insert.mst_rek_client_hist", edit.getRekening_client());
			//logger.info("insert rek cliet hist");
		}
		
		private void proc_save_account_recur(Cmdeditbac edit,String strTmpSPAJ)throws ServletException,IOException
		{
			edit.getAccount_recur().setReg_spaj(strTmpSPAJ);
			edit.getAccount_recur().setMar_jenis(edit.getDatausulan().getMste_flag_cc());
			edit.getAccount_recur().setLus_id(edit.getPemegang().getLus_id());
			
			//yusuf - 08/10/08
			//bila jenis = tabungan, dan bank = bca, maka status active = 0, 
			//karena bca require surat kuasa terlebih dahulu sebelum diperbolehkan autodebet
			//Map infoBank = bacDao.select_bankautodebet(edit.getAccount_recur().getLbn_id());
			//String lsbp_id = ((BigDecimal) infoBank.get("BANK_PUSAT_ID")).toString();
			//if(edit.getAccount_recur().getMar_jenis().intValue() == 1 && lsbp_id.equals("28")) {
			//	edit.getAccount_recur().setMar_active(0);
			//}

			//yusuf - 12/01/11
			//disabled. mulai sekarang, bisa diinput oleh UW, dengan default = 1 (aktif) 
			
			
			insert("insert.mst_account_recur", edit.getAccount_recur());
			//logger.info("insert account recur");	
		}
		
		private void proc_save_reff_mall(Cmdeditbac edit,String strTmpSPAJ,User currentUser)throws ServletException,IOException
		{
			ReffBii datautama = new ReffBii();
			Map map = new HashMap();
			datautama.setReg_spaj(strTmpSPAJ);
			datautama.setStatus("");
			datautama.setHit_err(new Integer(0));
			if (currentUser.getMall_lcb_no() != null) {
				Map data = (HashMap) this.bacDao.select_referrer_shinta(
						currentUser.getMall_lcb_no(), 13);
				if (data != null) {
					datautama.setNama_reff((String) data.get("NAMA_REFF"));
					datautama.setNo_rek((String) data.get("NO_REK"));
					datautama.setCab_rek((String) data.get("CAB_REK"));
					datautama.setAtas_nama((String) data.get("ATAS_NAMA"));
					datautama.setFlag_aktif((String) data.get("FLAG_AKTIF"));
					datautama.setNpk((String) data.get("NPK"));
					datautama.setLcb_no((String) data.get("LCB_NO"));
					datautama.setLcb_penutup((String) data.get("LCB_NO"));
					datautama.setReff_id(currentUser.getMall_lcb_no());
					if (((String) data.get("FLAG_AKTIF")).equals("1")) {
						datautama.setAktif("AKTIF");
					} else {
						datautama.setAktif("TIDAK AKTIF");
					}

					// referral diset sama dengan agen penutup, bila dikosongin
					if (datautama.getReff_id() == null
							|| "".equals(datautama.getReff_id())) {
						datautama.setReff_id(datautama.getLrb_id());
						datautama.setNama_reff2((String) data.get("NAMA_REFF"));
						datautama.setNo_rek2((String) data.get("NO_REK"));
						datautama.setCab_rek2((String) data.get("CAB_REK"));
						datautama.setAtas_nama2((String) data.get("ATAS_NAMA"));
						datautama.setFlag_aktif2((String) data.get("FLAG_AKTIF"));
						datautama.setNpk2((String) data.get("NPK"));
						datautama.setLcb_no2((String) data.get("LCB_NO"));
						datautama.setLcb_penutup2((String) data.get("LCB_NO"));
						if (((String) data.get("FLAG_AKTIF")).equals("1")) {
							datautama.setAktif2("AKTIF");
						} else {
							datautama.setAktif2("TIDAK AKTIF");
						}
					}
				}
			}else if (edit.getAgen().getLrb_id() != null) {
				Map data = (HashMap) this.bacDao.select_referrer_shinta(
						edit.getAgen().getLrb_id(), 13);
				if (data != null) {
					datautama.setNama_reff((String) data.get("NAMA_REFF"));
					datautama.setNo_rek((String) data.get("NO_REK"));
					datautama.setCab_rek((String) data.get("CAB_REK"));
					datautama.setAtas_nama((String) data.get("ATAS_NAMA"));
					datautama.setFlag_aktif((String) data.get("FLAG_AKTIF"));
					datautama.setNpk((String) data.get("NPK"));
					datautama.setLcb_no((String) data.get("LCB_NO"));
					datautama.setLcb_penutup((String) data.get("LCB_NO"));
					datautama.setReff_id(edit.getAgen().getLrb_id());
					if (((String) data.get("FLAG_AKTIF")).equals("1")) {
						datautama.setAktif("AKTIF");
					} else {
						datautama.setAktif("TIDAK AKTIF");
					}

					// referral diset sama dengan agen penutup, bila dikosongin
					if (datautama.getReff_id() == null
							|| "".equals(datautama.getReff_id())) {
						datautama.setReff_id(datautama.getLrb_id());
						datautama.setNama_reff2((String) data.get("NAMA_REFF"));
						datautama.setNo_rek2((String) data.get("NO_REK"));
						datautama.setCab_rek2((String) data.get("CAB_REK"));
						datautama.setAtas_nama2((String) data.get("ATAS_NAMA"));
						datautama.setFlag_aktif2((String) data.get("FLAG_AKTIF"));
						datautama.setNpk2((String) data.get("NPK"));
						datautama.setLcb_no2((String) data.get("LCB_NO"));
						datautama.setLcb_penutup2((String) data.get("LCB_NO"));
						if (((String) data.get("FLAG_AKTIF")).equals("1")) {
							datautama.setAktif2("AKTIF");
						} else {
							datautama.setAktif2("TIDAK AKTIF");
						}
					}
				}
			}
			String lcb_no = datautama.getLcb_no();
			this.bacDao.deletemstreff_bii(datautama.getReg_spaj());
			if (currentUser.getMall_lcb_no() != null) {
				this.bacDao.insertmst_reff_bii(datautama.getReg_spaj(),
						"4", lcb_no, currentUser.getMall_lcb_no(),
						datautama.getReff_id(),lcb_no);
			}else if (edit.getAgen().getLrb_id() != null) {
				this.bacDao.insertmst_reff_bii(datautama.getReg_spaj(),
						"4", lcb_no, edit.getAgen().getLrb_id(),
						datautama.getReff_id(),lcb_no);
			}
			
		}
		
		private void proc_save_rekening(Cmdeditbac edit,String strTmpSPAJ,Integer kode_flag)throws ServletException,IOException
		{
			Integer	kode_account=edit.getDatausulan().getFlag_account();
			Integer v_intAutoDebet = edit.getDatausulan().getMste_flag_cc();
			String v_pil_invest = null;
			if (kode_flag.intValue() == 1)
			{
				Integer rollover = edit.getPowersave().getMps_roll_over();
				v_pil_invest = Integer.toString(rollover.intValue());
			}
			if (kode_account.intValue() == 2 || kode_account.intValue() ==3 ) 
			{
				if (kode_flag.intValue() != 1) 
				{
//					insert mst rek client dan mst rek hist
					proc_save_rek_client(edit,strTmpSPAJ);
					
				}else{
					if (v_pil_invest.equalsIgnoreCase("2"))
					{
//						insert mst rek client dan mst rek hist
						proc_save_rek_client(edit,strTmpSPAJ);
					}else{
						String v_strAcctHolder1 = edit.getRekening_client().getMrc_no_ac().toUpperCase();
						String v_bank1 = edit.getRekening_client().getLsbp_id();
						String atasnama1 = edit.getRekening_client().getMrc_nama().toUpperCase();
						String cabang_bank = edit.getRekening_client().getMrc_cabang().toUpperCase();
						String kota_rek = edit.getRekening_client().getMrc_kota().toUpperCase();
						if (!v_strAcctHolder1.equalsIgnoreCase("") || !v_bank1.equalsIgnoreCase("") || !atasnama1.equalsIgnoreCase("") || !cabang_bank.equalsIgnoreCase("") || !kota_rek.equalsIgnoreCase(""))
						{
//							insert mst rek client dan mst rek hist
							proc_save_rek_client(edit,strTmpSPAJ);
						}
					}
				}
			}
		}
		
		private void proc_save_mst_policy(User currentUser, Cmdeditbac edit, String strPOClientID,String strTmpSPAJ,Date v_strDateNow , String v_strAgentId, String strAgentBranch,String strBranch,String strWilayah,String strRegion,String v_strregionid)throws ServletException,IOException
		{
			try {
				//****** mst policy ****************

				edit.getPemegang().setMcl_id(strPOClientID);
				edit.getDatausulan().setReg_spaj(strTmpSPAJ);
				edit.getPemegang().setMspo_age(edit.getPemegang().getUsiapp());
				edit.getDatausulan().setLus_id(edit.getPemegang().getLus_id());
				edit.getDatausulan().setLstp_id(edit.getDatausulan().getTipeproduk());
				edit.getPemegang().setMspo_policy_holder(strPOClientID);
				edit.getDatausulan().setMspo_ins_period(edit.getDatausulan().getMspr_ins_period());
				if(edit.getDatausulan().getLsbs_id()==191){
					if(!Common.isEmpty(edit.getDatausulan().getMspo_installment())){
						edit.getDatausulan().setMspo_pay_period(edit.getDatausulan().getMspo_installment());
					}
				}
				
//				if(edit.getDatausulan().isPsave || edit.getPowersave().getMsl_spaj_lama()!=null){
//					edit.getDatausulan().setKopiSPAJ(edit.getPowersave().getMsl_spaj_lama());
//				}else{
//					edit.getDatausulan().setKopiSPAJ("");
//				}
				if(edit.getDatausulan().getConvert()==null){
					edit.getDatausulan().setKopiSPAJ("");
				}else{
					if(edit.getDatausulan().getMssur_se()!=null){
						if(edit.getDatausulan().getMssur_se()==1 || edit.getDatausulan().getMssur_se()==2 || edit.getDatausulan().getMssur_se()==3){	
							edit.getDatausulan().setKopiSPAJ(edit.getPowersave().getMsl_spaj_lama());
						}else {
							edit.getDatausulan().setKopiSPAJ("");
						}
					}else{
						edit.getDatausulan().setKopiSPAJ("");
					}
					
				}
				
				if (!FormatString.rpad("0",(strBranch),2).equalsIgnoreCase("09"))
				{
					edit.getPemegang().setMspo_pribadi(new Integer(0));
				}

				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Calendar tgl_sekarang = Calendar.getInstance(); 
				String tgl_s =  FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.DATE)),2);
				tgl_s = tgl_s.concat("/");
				tgl_s = tgl_s.concat(FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.MONTH)+1),2));
				tgl_s = tgl_s.concat("/");
				tgl_s = tgl_s.concat(Integer.toString(tgl_sekarang.get(Calendar.YEAR)));	
				Date v_strInputDate;
				
				v_strInputDate = df.parse(tgl_s);
				edit.getDatausulan().setMspo_spaj_date(edit.getPemegang().getMspo_spaj_date());
				
				//tambahan Yusuf - 12 feb 08
				//mspo_flat diisi 0 apabila individu biasa, 1 bila inputan bank (bii), 2 bila inputan bank (sinarmas - simas prima)
				
				if(currentUser.getJn_bank().intValue() == 0 || currentUser.getJn_bank().intValue() == 1) {
					edit.getPemegang().setMspo_flat(1);
				}else if(currentUser.getJn_bank().intValue() == 2) {
					edit.getPemegang().setMspo_flat(2);
				}else if(currentUser.getJn_bank().intValue() == 3) {
					edit.getPemegang().setMspo_flat(3);
				}else {
					edit.getPemegang().setMspo_flat(0);
				}
				
				if(edit.getAgen().getLca_id().equals("42")){
					edit.getPemegang().setMspo_customer(edit.getPemegang().getMspo_customer());
					//edit.getPemegang().setMspo_follow_up(0);
				}else{
					if(!(edit.getPemegang().getSumber_id()==null?"":edit.getPemegang().getSumber_id()).equals("")){
						edit.getPemegang().setMspo_follow_up(4);
						edit.getPemegang().setMspo_customer(edit.getPemegang().getSumber_id());
					}else{
						if(!(edit.getPemegang().getMspo_customer()==null?"":edit.getPemegang().getMspo_customer()).equals("")){
							edit.getPemegang().setMspo_customer(edit.getPemegang().getMspo_customer());
						}else{
							edit.getPemegang().setMspo_customer(edit.getPemegang().getSumber_id());
						}
						
					}
					if(edit.getFlag_gmanual_spaj()!=null){
						edit.getPemegang().setMspo_customer("");
					}
				}
				
				//tambahan Yusuf - muamalat
				//TIDAK JADI DITAMBAH : DIINPUT DIDEPAN
				//boolean muamalat = products.muamalat(edit.getDatausulan().getLsbs_id().intValue(), edit.getDatausulan().getLsdbs_number().intValue());
				//if(muamalat) {
					//edit.getDatausulan().setMspo_nasabah_acc(edit.getAccount_recur().getMar_acc_no());
				//}
				
				Map param = new HashMap();
				param.put("pemegang",edit.getPemegang());
				param.put("tertanggung",edit.getTertanggung());
				param.put("datausulan",edit.getDatausulan());
				param.put("agen",edit.getAgen());
				int rowupdated = update("update.mst_policy", param);
				//logger.info("edit mst policy");
				if (rowupdated == 0)
				{logger.info(param);
					insert("insert.mst_policy", param);
					//logger.info("insert mst policy");
				}
				if(edit.getAgen().getLca_id().equals("58")){
					//untuk masukkan kode appointmentID
					uwDao.updateLeadReffBii(edit.getPemegang().getMspo_plan_provider(), strTmpSPAJ);
				}
				if(edit.getPemegang().getMspo_spaj_date() != null) uwDao.saveMstTransHistory(strTmpSPAJ, "tgl_spaj_asli", edit.getPemegang().getMspo_spaj_date(), null, null);
				Integer	flag_simponi=edit.getDatausulan().getIsBungaSimponi();
				Integer	flag_tahapan=edit.getDatausulan().getIsBonusTahapan();
				edit.getPemegang().setReg_spaj(strTmpSPAJ);
				//update bunga simponi
				if (flag_simponi.intValue()==1)
				{
					update("update.bungamstpolicy", edit.getPemegang());
				}
				
				//update bonus  tahapan
				if (flag_tahapan.intValue()==1)
				{
					Double bonus =edit.getPemegang().getBonus_tahapan();
					edit.getPemegang().setMspo_under_table(bonus);
					update("update.bungamstpolicy",edit.getPemegang());
				}
				//bagian ini untuk Bonus Maxi Deposit.
				if((edit.getDatausulan().getLsbs_id()==137 && edit.getDatausulan().getLsdbs_number()==3) || (edit.getDatausulan().getLsbs_id()==137 && edit.getDatausulan().getLsdbs_number()==4) ){
					Double bonus = 5.00;
					edit.getPemegang().setMspo_under_table(bonus);
					update("update.bungamstpolicy",edit.getPemegang());
				}else if((edit.getDatausulan().getLsbs_id()==114 && edit.getDatausulan().getLsdbs_number()>=2)  ){
					Double bonus = 2.92;
					edit.getPemegang().setMspo_under_table(bonus);
					update("update.bungamstpolicy",edit.getPemegang());
				}
			} catch (ParseException e) {
				logger.error("ERROR :", e);
			}
		}
		
		private void proc_save_mst_policy_pas(User currentUser, Cmdeditbac edit, String strPOClientID,String strTmpSPAJ,Date v_strDateNow , String v_strAgentId, String strAgentBranch,String strBranch,String strWilayah,String strRegion,String v_strregionid)throws ServletException,IOException
		{
			try {
				//****** mst policy ****************

				edit.getPemegang().setMcl_id(strPOClientID);
				edit.getDatausulan().setReg_spaj(strTmpSPAJ);
				edit.getPemegang().setMspo_age(edit.getPemegang().getUsiapp());
				edit.getDatausulan().setLus_id(edit.getPemegang().getLus_id());
				edit.getDatausulan().setLstp_id(edit.getDatausulan().getTipeproduk());
				edit.getPemegang().setMspo_policy_holder(strPOClientID);
				edit.getDatausulan().setMspo_ins_period(edit.getDatausulan().getMspr_ins_period());
				
//				if(edit.getDatausulan().isPsave || edit.getPowersave().getMsl_spaj_lama()!=null){
//					edit.getDatausulan().setKopiSPAJ(edit.getPowersave().getMsl_spaj_lama());
//				}else{
//					edit.getDatausulan().setKopiSPAJ("");
//				}
				if(edit.getDatausulan().getConvert()==null){
					edit.getDatausulan().setKopiSPAJ("");
				}else{
					if(edit.getDatausulan().getMssur_se()!=null){
						if(edit.getDatausulan().getMssur_se()==1 || edit.getDatausulan().getMssur_se()==2 || edit.getDatausulan().getMssur_se()==3){	
							edit.getDatausulan().setKopiSPAJ(edit.getPowersave().getMsl_spaj_lama());
						}else {
							edit.getDatausulan().setKopiSPAJ("");
						}
					}else{
						edit.getDatausulan().setKopiSPAJ("");
					}
					
				}
				
				if (!FormatString.rpad("0",(strBranch),2).equalsIgnoreCase("09"))
				{
					edit.getPemegang().setMspo_pribadi(new Integer(0));
				}

				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Calendar tgl_sekarang = Calendar.getInstance(); 
				String tgl_s =  FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.DATE)),2);
				tgl_s = tgl_s.concat("/");
				tgl_s = tgl_s.concat(FormatString.rpad("0",Integer.toString(tgl_sekarang.get(Calendar.MONTH)+1),2));
				tgl_s = tgl_s.concat("/");
				tgl_s = tgl_s.concat(Integer.toString(tgl_sekarang.get(Calendar.YEAR)));	
				Date v_strInputDate;
				
				v_strInputDate = df.parse(tgl_s);
				edit.getDatausulan().setMspo_spaj_date(edit.getPemegang().getMspo_spaj_date());
				
				//tambahan Yusuf - 12 feb 08
				//mspo_flat diisi 0 apabila individu biasa, 1 bila inputan bank (bii), 2 bila inputan bank (sinarmas - simas prima)
				
				if(currentUser.getJn_bank().intValue() == 0 || currentUser.getJn_bank().intValue() == 1) {
					edit.getPemegang().setMspo_flat(1);
				}else if(currentUser.getJn_bank().intValue() == 2) {
					edit.getPemegang().setMspo_flat(2);
				}else if(currentUser.getJn_bank().intValue() == 3) {
					edit.getPemegang().setMspo_flat(3);
				}else {
					edit.getPemegang().setMspo_flat(0);
				}
				
				if(edit.getAgen().getLca_id().equals("42")){
					edit.getPemegang().setMspo_customer(edit.getPemegang().getMspo_customer());
					//edit.getPemegang().setMspo_follow_up(0);
				}else{
					if(!(edit.getPemegang().getSumber_id()==null?"":edit.getPemegang().getSumber_id()).equals("")){
						edit.getPemegang().setMspo_follow_up(4);
						edit.getPemegang().setMspo_customer(edit.getPemegang().getSumber_id());
					}else{
						if(!(edit.getPemegang().getMspo_customer()==null?"":edit.getPemegang().getMspo_customer()).equals("")){
							edit.getPemegang().setMspo_customer(edit.getPemegang().getMspo_customer());
						}else{
							edit.getPemegang().setMspo_customer(edit.getPemegang().getSumber_id());
						}
						
					}
					if(edit.getFlag_gmanual_spaj()!=null){
						edit.getPemegang().setMspo_customer("");
					}
				}
				
				//tambahan Yusuf - muamalat
				//TIDAK JADI DITAMBAH : DIINPUT DIDEPAN
				//boolean muamalat = products.muamalat(edit.getDatausulan().getLsbs_id().intValue(), edit.getDatausulan().getLsdbs_number().intValue());
				//if(muamalat) {
					//edit.getDatausulan().setMspo_nasabah_acc(edit.getAccount_recur().getMar_acc_no());
				//}
				
				Map param = new HashMap();
				param.put("pemegang",edit.getPemegang());
				param.put("tertanggung",edit.getTertanggung());
				param.put("datausulan",edit.getDatausulan());
				param.put("agen",edit.getAgen());
				int rowupdated = update("update.mst_policy", param);
				//logger.info("edit mst policy");
				if (rowupdated == 0)
				{logger.info(param);
					insert("insert.mst_policy_pas", param);
					//logger.info("insert mst policy");
				}
				if(edit.getAgen().getLca_id().equals("58")){
					//untuk masukkan kode appointmentID
					uwDao.updateLeadReffBii(edit.getPemegang().getMspo_plan_provider(), strTmpSPAJ);
				}
				if(edit.getPemegang().getMspo_spaj_date() != null) uwDao.saveMstTransHistory(strTmpSPAJ, "tgl_spaj_asli", edit.getPemegang().getMspo_spaj_date(), null, null);
				Integer	flag_simponi=edit.getDatausulan().getIsBungaSimponi();
				Integer	flag_tahapan=edit.getDatausulan().getIsBonusTahapan();
				edit.getPemegang().setReg_spaj(strTmpSPAJ);
				//update bunga simponi
				if (flag_simponi.intValue()==1)
				{
					update("update.bungamstpolicy", edit.getPemegang());
				}
				
				//update bonus  tahapan
				if (flag_tahapan.intValue()==1)
				{
					Double bonus =edit.getPemegang().getBonus_tahapan();
					edit.getPemegang().setMspo_under_table(bonus);
					update("update.bungamstpolicy",edit.getPemegang());
				}
				//bagian ini untuk Bonus Maxi Deposit.
				if((edit.getDatausulan().getLsbs_id()==137 && edit.getDatausulan().getLsdbs_number()==3) || (edit.getDatausulan().getLsbs_id()==137 && edit.getDatausulan().getLsdbs_number()==4) ){
					Double bonus = 5.00;
					edit.getPemegang().setMspo_under_table(bonus);
					update("update.bungamstpolicy",edit.getPemegang());
				}else if((edit.getDatausulan().getLsbs_id()==114 && edit.getDatausulan().getLsdbs_number()>=2)  ){
					Double bonus = 2.92;
					edit.getPemegang().setMspo_under_table(bonus);
					update("update.bungamstpolicy",edit.getPemegang());
				}
			} catch (ParseException e) {
				logger.error("ERROR :", e);
			}
		}
		
		public void proc_save_agen(Cmdeditbac edit, String strTmpSPAJ ,String v_strAgentId, String strAgentBranch,String strBranch,String strWilayah,String strRegion,String v_strregionid)throws ServletException,IOException
		{
			Integer intBII =new Integer(0);
			Integer v_intPribadi = edit.getPemegang().getMspo_pribadi();
			if(v_intPribadi == null)v_intPribadi = 0;
			String v_kode_ao = edit.getPemegang().getMspo_ao().toUpperCase();
			Integer v_intFollowUp = edit.getPemegang().getMspo_follow_up();
			
			Agentrec[]  arrAgentRec;
			Agentrec[]  arrAgentArtha;
			Agentrec[]  arrAgentartha1;
			
			Date tgl_beg_date_polis = edit.getDatausulan().getMste_beg_date();
			Integer tahun_beg_date_polis = tgl_beg_date_polis.getYear() + 1900;
			if (!FormatString.rpad("0",(strAgentBranch.substring(0,2)),2).equalsIgnoreCase("46") || v_strAgentId.equalsIgnoreCase("000000"))
			{
				if (tahun_beg_date_polis.intValue() > 2006)
				{
					arrAgentRec = proc_process_agent_2007(v_strAgentId);
				}else{
					arrAgentRec = proc_process_agent(v_strAgentId);
				}
			}else{
				arrAgentRec=null;
			}
			if (FormatString.rpad("0",(strAgentBranch.substring(0,2)),2).equalsIgnoreCase("09"))
			{
				intBII = new Integer(1);
				
			}else{
				v_intPribadi = new Integer(0);
				edit.getPemegang().setMspo_pribadi(new Integer(0));
			}

			if (v_strAgentId.equalsIgnoreCase("000000"))
			{
				
				proc_save_agen_prod(edit,arrAgentRec,strTmpSPAJ ,new Integer(1),new Integer(0));
				//logger.info("insert mst_agent_prod");
				
				//insert mst agent comm
				proc_save_agen_comm(edit,arrAgentRec, strTmpSPAJ ,new Integer(1));

			}else{
				if(FormatString.rpad("0",(strAgentBranch.substring(0,2)),2).equalsIgnoreCase("58")){
					for (int i = 1 ; i<=arrAgentRec.length-1 ; i++)
					{
						proc_save_agen_comm(edit,arrAgentRec, strTmpSPAJ ,new Integer(i));
					}
				}
				if (FormatString.rpad("0",(strAgentBranch.substring(0,2)),2).equalsIgnoreCase("46"))
				{
					arrAgentArtha = proc_agent_artha(v_strAgentId);
					arrAgentartha1 = proc_process_agent_artha_2007(v_strAgentId);

					for (int i = 1 ; i<=4 ; i++)
					{
						Integer ssbm=new Integer(0);
						
						proc_save_agen_prod(edit,arrAgentartha1,strTmpSPAJ ,new Integer(i),ssbm);
						
	
						if (intBII.intValue() == 0  &&  arrAgentartha1[i].getComm_id() != null ) 
						{
	//						insert mst agent comm
							proc_save_agen_comm(edit,arrAgentartha1, strTmpSPAJ ,new Integer(i));
							//logger.info("insert mst_agent_comm" + i);
						}else{
	
							if (v_intPribadi.intValue() == 1) 
							{
								continue;
							}
	
							if ((strAgentBranch.equalsIgnoreCase("0905") && v_intFollowUp.intValue() == 1))
							{
								continue;
							}
	
							if (strAgentBranch.equalsIgnoreCase("0903") && v_kode_ao.equalsIgnoreCase(v_strAgentId))	
							{
							
							}else{
								if  (arrAgentartha1[i].getComm_id()!=null)
								{
									if  (arrAgentartha1[i].getComm_id().intValue() == 4) 
									{
	//									insert mst agent comm
										proc_save_agen_comm(edit,arrAgentartha1, strTmpSPAJ ,new Integer(i));
										//logger.info("insert mst_agent_comm" + i);

									}else{
										if (arrAgentartha1[i].getComm_id().intValue() == 3 && ( strAgentBranch.equalsIgnoreCase("0905") && v_intFollowUp.intValue() == 2))
										{
	//										insert mst agent comm
											proc_save_agen_comm(edit,arrAgentartha1, strTmpSPAJ ,new Integer(i));
											//logger.info("insert mst_agent_comm" + i);
										}
									}
								}
							}
						}

					}
					for (int i = 1 ; i<=6 ; i++)
					{
						proc_save_agen_artha(edit,arrAgentArtha, strTmpSPAJ,new Integer(i));
					}
				}else{
					for (int i = 1 ; i<=4 ; i++)
					{
						Integer ssbm=new Integer(0);
						if ((arrAgentRec[i].getLevel_id()).intValue() == 1)
						{
							ssbm=(arrAgentRec[i].getSbm());
						}else{
							if ((arrAgentRec[i].getLevel_id()).intValue() == 2)
							{
								ssbm=(arrAgentRec[i].getBm());
							}
						}
	
						
						proc_save_agen_prod(edit,arrAgentRec,strTmpSPAJ ,new Integer(i),ssbm);
						//logger.info("insert mst_agent_prod");
						//logger.info("insert mst_agent_prod" + i);
	
						if (intBII.intValue() == 0  &&  arrAgentRec[i].getComm_id() != null ) 
						{
	//						insert mst agent comm
							proc_save_agen_comm(edit,arrAgentRec, strTmpSPAJ ,new Integer(i));
							//logger.info("insert mst_agent_comm" + i);
						}else{
	
							if (v_intPribadi.intValue() == 1) 
							{
								continue;
							}
	
							if ((strAgentBranch.equalsIgnoreCase("0905") && v_intFollowUp.intValue() == 1))
							{
								continue;
							}
	
							if (strAgentBranch.equalsIgnoreCase("0903") && v_kode_ao.equalsIgnoreCase(v_strAgentId))	
							{
							
							}else{
								if  (arrAgentRec[i].getComm_id()!=null)
								{
									if  (arrAgentRec[i].getComm_id().intValue() == 4) 
									{
	//									insert mst agent comm
										proc_save_agen_comm(edit,arrAgentRec, strTmpSPAJ ,new Integer(i));
										//logger.info("insert mst_agent_comm" + i);
	
									}else{
										if (arrAgentRec[i].getComm_id().intValue() == 3 && ( strAgentBranch.equalsIgnoreCase("0905") && v_intFollowUp.intValue() == 2))
										{
	//										insert mst agent comm
											proc_save_agen_comm(edit,arrAgentRec, strTmpSPAJ ,new Integer(i));
											//logger.info("insert mst_agent_comm" + i);
										}
									}
								}
							}
						}
						if (i==4)
						{
							if (!(strBranch.equalsIgnoreCase("08") || strBranch.equalsIgnoreCase("09") || strBranch.equalsIgnoreCase("37") || strBranch.equalsIgnoreCase("42") || strBranch.equalsIgnoreCase("52")))
							{
								proc_save_agen_comm_rm(edit,arrAgentRec, strTmpSPAJ ,new Integer(i));
							}
						}
					}
				}
			}
			if (intBII.intValue() == 1 )
			{
				Integer ssbm=new Integer(0);
				Agentrec[]  arrAgentRec1;
				arrAgentRec1 = proc_process_agentao(v_kode_ao);
				for (int i = 1 ; i<=4 ; i++)
				{
	
					if ((arrAgentRec1[i].getLevel_id()).intValue() == 1)
					{
						ssbm=(arrAgentRec1[i].getSbm());
					}else{
						if ((arrAgentRec1[i].getLevel_id()).intValue() == 2)
						{
							ssbm=(arrAgentRec1[i].getBm());
						}
					}
					
					edit.getAgen().setReg_spaj(strTmpSPAJ);
					edit.getAgen().setMsag_id(arrAgentRec1[i].getAgent_id());
					edit.getAgen().setLstb_id(arrAgentRec1[i].getBisnis_id());
					edit.getAgen().setLsle_id(arrAgentRec1[i].getLevel_id());
					edit.getAgen().setLev_comm(arrAgentRec1[i].getComm_id());
					edit.getAgen().setFlag_sbm(ssbm);
					insert("insert.mst_agent_ba", edit.getAgen());	
					//logger.info("insert mst_agent_ba");
				}
			}
			edit.getAgen().setMsag_id(v_strAgentId);
		}
		
		public void proc_save_agen_pas(Cmdeditbac edit, String strTmpSPAJ ,String v_strAgentId, String strAgentBranch,String strBranch,String strWilayah,String strRegion,String v_strregionid)throws ServletException,IOException
		{
			Integer intBII =new Integer(0);
			Integer v_intPribadi = edit.getPemegang().getMspo_pribadi();
			String v_kode_ao = edit.getPemegang().getMspo_ao().toUpperCase();
			Integer v_intFollowUp = edit.getPemegang().getMspo_follow_up();
			
			Agentrec[]  arrAgentRec;
			Agentrec[]  arrAgentArtha;
			Agentrec[]  arrAgentartha1;
			
			Date tgl_beg_date_polis = edit.getDatausulan().getMste_beg_date();
			Integer tahun_beg_date_polis = tgl_beg_date_polis.getYear() + 1900;
			if (!FormatString.rpad("0",(strAgentBranch.substring(0,2)),2).equalsIgnoreCase("46") || v_strAgentId.equalsIgnoreCase("000000"))
			{
				if (tahun_beg_date_polis.intValue() > 2006)
				{
					arrAgentRec = proc_process_agent_2007(v_strAgentId);
				}else{
					arrAgentRec = proc_process_agent(v_strAgentId);
				}
			}else{
				arrAgentRec=null;
			}
			if (FormatString.rpad("0",(strAgentBranch.substring(0,2)),2).equalsIgnoreCase("09"))
			{
				intBII = new Integer(1);
				
			}else{
				v_intPribadi = new Integer(0);
				edit.getPemegang().setMspo_pribadi(new Integer(0));
			}

			if (v_strAgentId.equalsIgnoreCase("000000"))
			{
				
				proc_save_agen_prod(edit,arrAgentRec,strTmpSPAJ ,new Integer(1),new Integer(0));
				//logger.info("insert mst_agent_prod");
				
				//insert mst agent comm
				proc_save_agen_comm(edit,arrAgentRec, strTmpSPAJ ,new Integer(1));

			}else{
				if (FormatString.rpad("0",(strAgentBranch.substring(0,2)),2).equalsIgnoreCase("46"))
				{
					arrAgentArtha = proc_agent_artha(v_strAgentId);
					arrAgentartha1 = proc_process_agent_artha_2007(v_strAgentId);

					for (int i = 1 ; i<=4 ; i++)
					{//hanya level 4 yg disave (i=1)
						if(i == 1){
							Integer ssbm=new Integer(0);
							
							proc_save_agen_prod(edit,arrAgentartha1,strTmpSPAJ ,new Integer(i),ssbm);
							
		
							if (intBII.intValue() == 0  &&  arrAgentartha1[i].getComm_id() != null ) 
							{
		//						insert mst agent comm
								proc_save_agen_comm(edit,arrAgentartha1, strTmpSPAJ ,new Integer(i));
								//logger.info("insert mst_agent_comm" + i);
							}else{
		
								if (v_intPribadi.intValue() == 1) 
								{
									continue;
								}
		
								if ((strAgentBranch.equalsIgnoreCase("0905") && v_intFollowUp.intValue() == 1))
								{
									continue;
								}
		
								if (strAgentBranch.equalsIgnoreCase("0903") && v_kode_ao.equalsIgnoreCase(v_strAgentId))	
								{
								
								}else{
									if  (arrAgentartha1[i].getComm_id()!=null)
									{
										if  (arrAgentartha1[i].getComm_id().intValue() == 4) 
										{
		//									insert mst agent comm
											proc_save_agen_comm(edit,arrAgentartha1, strTmpSPAJ ,new Integer(i));
											//logger.info("insert mst_agent_comm" + i);
	
										}else{
											if (arrAgentartha1[i].getComm_id().intValue() == 3 && ( strAgentBranch.equalsIgnoreCase("0905") && v_intFollowUp.intValue() == 2))
											{
		//										insert mst agent comm
												proc_save_agen_comm(edit,arrAgentartha1, strTmpSPAJ ,new Integer(i));
												//logger.info("insert mst_agent_comm" + i);
											}
										}
									}
								}
							}
						}

					}
					for (int i = 1 ; i<=6 ; i++)
					{
						proc_save_agen_artha(edit,arrAgentArtha, strTmpSPAJ,new Integer(i));
					}
				}else{
					for (int i = 1 ; i<=4 ; i++)
					{//hanya level 4 yg disave (i=1)
						Integer ssbm=new Integer(0);
						if(i == 1){
							if ((arrAgentRec[i].getLevel_id()).intValue() == 1)
							{
								ssbm=(arrAgentRec[i].getSbm());
							}else{
								if ((arrAgentRec[i].getLevel_id()).intValue() == 2)
								{
									ssbm=(arrAgentRec[i].getBm());
								}
							}
						}
	
						if(strBranch.equals("37") && strWilayah.equals("A8") ){
							if((arrAgentRec[i].getLevel_id()).intValue() == 4){
								proc_save_agen_prod(edit,arrAgentRec,strTmpSPAJ ,new Integer(i),ssbm);
							}
						}else if(strBranch.equals("37") && strWilayah.equals("A9") ){
							if((arrAgentRec[i].getLevel_id()).intValue() != 1){
								proc_save_agen_prod(edit,arrAgentRec,strTmpSPAJ ,new Integer(i),ssbm);
							}
						}else{
							proc_save_agen_prod(edit,arrAgentRec,strTmpSPAJ ,new Integer(i),ssbm);
						}
						
						//logger.info("insert mst_agent_prod");
						//logger.info("insert mst_agent_prod" + i);
	
						if (intBII.intValue() == 0  &&  arrAgentRec[i].getComm_id() != null ) 
						{
	//						insert mst agent comm
							proc_save_agen_comm(edit,arrAgentRec, strTmpSPAJ ,new Integer(i));
							//logger.info("insert mst_agent_comm" + i);
						}else{
	
							if (v_intPribadi.intValue() == 1) 
							{
								continue;
							}
	
							if ((strAgentBranch.equalsIgnoreCase("0905") && v_intFollowUp.intValue() == 1))
							{
								continue;
							}
	
							if (strAgentBranch.equalsIgnoreCase("0903") && v_kode_ao.equalsIgnoreCase(v_strAgentId))	
							{
		//						insert mst agent comm
								proc_save_agen_comm(edit,arrAgentRec, strTmpSPAJ ,new Integer(i));
								//logger.info("insert mst_agent_comm" + i);
							}else{
		
								if (v_intPribadi.intValue() == 1) 
								{
									continue;
								}
		
								if ((strAgentBranch.equalsIgnoreCase("0905") && v_intFollowUp.intValue() == 1))
								{
									continue;
								}
		
								if (strAgentBranch.equalsIgnoreCase("0903") && v_kode_ao.equalsIgnoreCase(v_strAgentId))	
								{
								
								}else{
									if  (arrAgentRec[i].getComm_id()!=null)
									{
										if  (arrAgentRec[i].getComm_id().intValue() == 4) 
										{
		//									insert mst agent comm
											proc_save_agen_comm(edit,arrAgentRec, strTmpSPAJ ,new Integer(i));
											//logger.info("insert mst_agent_comm" + i);
		
										}else{
											if (arrAgentRec[i].getComm_id().intValue() == 3 && ( strAgentBranch.equalsIgnoreCase("0905") && v_intFollowUp.intValue() == 2))
											{
		//										insert mst agent comm
												proc_save_agen_comm(edit,arrAgentRec, strTmpSPAJ ,new Integer(i));
												//logger.info("insert mst_agent_comm" + i);
											}
										}
									}
								}
							}
						}
						if (i==4)
						{
							if (!(strBranch.equalsIgnoreCase("08") || strBranch.equalsIgnoreCase("09") || strBranch.equalsIgnoreCase("37") || strBranch.equalsIgnoreCase("42") || strBranch.equalsIgnoreCase("52")))
							{
								proc_save_agen_comm_rm(edit,arrAgentRec, strTmpSPAJ ,new Integer(i));
							}
						}
					}
				}
			}
			if (intBII.intValue() == 1 )
			{
				Integer ssbm=new Integer(0);
				Agentrec[]  arrAgentRec1;
				arrAgentRec1 = proc_process_agentao(v_kode_ao);
				for (int i = 1 ; i<=4 ; i++)
				{//hanya level 4 yg disave (i=1)
					if(i == 1){
		
						if ((arrAgentRec1[i].getLevel_id()).intValue() == 1)
						{
							ssbm=(arrAgentRec1[i].getSbm());
						}else{
							if ((arrAgentRec1[i].getLevel_id()).intValue() == 2)
							{
								ssbm=(arrAgentRec1[i].getBm());
							}
						}
						
						edit.getAgen().setReg_spaj(strTmpSPAJ);
						edit.getAgen().setMsag_id(arrAgentRec1[i].getAgent_id());
						edit.getAgen().setLstb_id(arrAgentRec1[i].getBisnis_id());
						edit.getAgen().setLsle_id(arrAgentRec1[i].getLevel_id());
						edit.getAgen().setLev_comm(arrAgentRec1[i].getComm_id());
						edit.getAgen().setFlag_sbm(ssbm);
						insert("insert.mst_agent_ba", edit.getAgen());	
						//logger.info("insert mst_agent_ba");
					}
				}
			}
			edit.getAgen().setMsag_id(v_strAgentId);
		}

		
		private void proc_save_agen_comm(Cmdeditbac edit, Agentrec[]  arrAgentRec,String strTmpSPAJ , Integer i)throws ServletException,IOException
		{
			edit.getAgen().setReg_spaj(strTmpSPAJ);
			edit.getAgen().setMsag_id(arrAgentRec[i.intValue()].getAgent_id());
			if(edit.getAgen().getLca_id().equals("58")){
				edit.getAgen().setLev_comm(arrAgentRec[i.intValue()].getLevel_id());
			}else{
				edit.getAgen().setLev_comm(arrAgentRec[i.intValue()].getComm_id());
			}
			

			boolean dapatKomisi = true;
			
			//Yusuf - 16 Juli 09 - Request mba Wesni helpdesk #13943
			//bila yg nutup itu, bukan level ME (1,2,3), khusus worksite, maka OR dan komisi hilang semua
			if(arrAgentRec.length > 1){
				if(arrAgentRec[1].getAgent_id() != null){
					Map m = bacDao.selectDataAgenValidasiWorksite(arrAgentRec[1].getAgent_id());
					if(m != null){
						String lca_id 		= (String) m.get("LCA_ID");
						BigDecimal lsle_id 	= (BigDecimal) m.get("LSLE_ID");
						if(lca_id != null && lsle_id != null){
							if(lca_id.equals("42")){
								m = bacDao.selectDataAgenValidasiWorksite(arrAgentRec[i].getAgent_id());
								lca_id 		= (String) m.get("LCA_ID");
								lsle_id 	= new BigDecimal(arrAgentRec[i].getLevel_id());
								
							}
							if(lca_id.equals("42") && lsle_id.intValue() < 4){
								dapatKomisi = false;
							}
							if(lca_id.equals("42") && financeDao.selectIsAgenCorporate(arrAgentRec[1].getAgent_id())==1 && lsle_id.intValue()==3){
								dapatKomisi=true;
							}
							
							if(lca_id.equals("42") && financeDao.selectIsAgenCorporate(arrAgentRec[1].getAgent_id())==0 && lsle_id.intValue()==3 && financeDao.selectIsAgenKaryawan(arrAgentRec[i].getAgent_id())!=1){
								dapatKomisi=true;
							}
							
							if(lca_id.equals("42") && financeDao.selectIsAgenKaryawan(arrAgentRec[i].getAgent_id())==1){
								Date TigaSatuJanuaryDuaRibuSepuluh=null;
								try {
									TigaSatuJanuaryDuaRibuSepuluh = defaultDateFormat.parse("31/1/2010");
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									logger.error("ERROR :", e);
								}
								if(edit.getDatausulan().getMste_beg_date().after(TigaSatuJanuaryDuaRibuSepuluh)){
									dapatKomisi=false;
								}
							}
							if(lca_id.equals("37") && edit.getAgen().getLwk_id().equals("A8")){
								lsle_id 	= new BigDecimal(arrAgentRec[i].getLevel_id());
								if(lsle_id.intValue()==4){
									dapatKomisi=true;
								}else{
									dapatKomisi=false;
								}
							}else if(lca_id.equals("37") && edit.getAgen().getLwk_id().equals("A9")){
								lsle_id 	= new BigDecimal(arrAgentRec[i].getLevel_id());
								if(lsle_id.intValue()!=1){
									dapatKomisi=true;
								}else{
									dapatKomisi=false;
								}
							}
							//Deddy : Semua lca_id 09, tidak perlu proses komisi individu, hanya proses komisi reff bii
							if(lca_id.equals("09")){
								dapatKomisi = false;
							}
						}
					}
				}
			}

			//bila powersave, gak ada OR
			boolean flag_pwr = products.powerSave(edit.getDatausulan().getLsbs_id().toString());
			if (flag_pwr && edit.getAgen().getLev_comm() <  4){
				dapatKomisi = false;
			}
			if(edit.getAgen().getMsag_id().equals("022902") && ("170".equals(edit.getDatausulan().getLsbs_id().toString())) && edit.getDatausulan().getLsdbs_number()==1) {
				//Req Pak Him : Untuk kode agent 022902 dan produk Iklas tidak mendapatkan komisi
				dapatKomisi = false;
			}
			
			if((edit.getDatausulan().getLsbs_id()==183 || edit.getDatausulan().getLsbs_id()==193) && edit.getAgen().getLev_comm() <  4){
				dapatKomisi = false;
			}

			if(dapatKomisi){
				insert("insert.mst_agent_comm", edit.getAgen());	
				//logger.info("insert mst_agent_comm");
			}
			
		}
		
		private void proc_save_agen_comm_rm(Cmdeditbac edit, Agentrec[]  arrAgentRec,String strTmpSPAJ , Integer i)throws ServletException,IOException
		{
			Date tgl_beg_date_polis = edit.getDatausulan().getMste_beg_date();
			Integer tahun_beg_date_polis = tgl_beg_date_polis.getYear() + 1900;
			if (tahun_beg_date_polis.intValue() > 2006)
			{
				Integer lev_comm = null;
				if (arrAgentRec[4] != null)
				{
					lev_comm = arrAgentRec[4].getComm_id();
				}
				if (lev_comm == null)
				{
					if (arrAgentRec[3] != null)
					{
						lev_comm = arrAgentRec[3].getComm_id();
					}
					if (lev_comm == null)
					{
						if (arrAgentRec[2] != null)
						{
							lev_comm = arrAgentRec[2].getComm_id();
						}
						if (lev_comm == null)
						{
							if ( arrAgentRec[1] != null)
							{
								lev_comm = arrAgentRec[1].getComm_id();
							}
							if (lev_comm == null)
							{
								if (arrAgentRec[0] != null)
								{
									lev_comm = arrAgentRec[0].getComm_id();
								}
							}
						}
					}
				}
				String rm =arrAgentRec[4].getMsag_rm();
				if (rm==null)
				{
					rm ="";
				}
				if (!rm.equalsIgnoreCase(""))
				{
					edit.getAgen().setReg_spaj(strTmpSPAJ);
					edit.getAgen().setMsag_id(rm);
					edit.getAgen().setLev_comm(new Integer(lev_comm.intValue() - 1));
					boolean flag_pwr = products.powerSave(edit.getDatausulan().getLsbs_id().toString());
					if (flag_pwr && edit.getAgen().getLev_comm() <  4)
					{
						
					}else{	
						insert("insert.mst_agent_comm", edit.getAgen());	
						//logger.info("insert mst_agent_comm"); THN 2007
					}

				}
			}
		}		
		
		private void proc_save_agen_prod(Cmdeditbac edit, Agentrec[]  arrAgentRec,String strTmpSPAJ , Integer i, Integer flag_sbm)throws ServletException,IOException
		{
			edit.getAgen().setReg_spaj(strTmpSPAJ);
			edit.getAgen().setLstb_id(arrAgentRec[i.intValue()].getBisnis_id());
			edit.getAgen().setMsag_id(arrAgentRec[i.intValue()].getAgent_id());
			edit.getAgen().setLev_comm(arrAgentRec[i.intValue()].getComm_id());
			edit.getAgen().setLsle_id(arrAgentRec[i.intValue()].getLevel_id());
			edit.getAgen().setFlag_sbm(flag_sbm);
			
			insert("insert.mst_agent_prod", edit.getAgen());	
			//logger.info("insert mst_agent_prod");
		}			
	
		
		private void proc_save_agen_artha(Cmdeditbac edit, Agentrec[]  arrAgentArtha,String strTmpSPAJ , Integer i)throws ServletException,IOException
		{
			edit.getAgen().setReg_spaj(strTmpSPAJ);
			edit.getAgen().setMsag_id(arrAgentArtha[i.intValue()].getAgent_id());
			edit.getAgen().setLev_comm(arrAgentArtha[i.intValue()].getComm_id());
			edit.getAgen().setLsla_id(arrAgentArtha[i.intValue()].getLevel_id());
			insert("insert_mst_agent_artha", edit.getAgen());	
			//logger.info("insert mst_agent_artha");
		}		
		
		private void proc_save_benef(Cmdeditbac edit, String strTmpSPAJ )throws ServletException,IOException
		{
			//data penerima manfaat
			List benef = edit.getDatausulan().getDaftabenef();
			Integer jmlpenerima = edit.getDatausulan().getJml_benef();

			for (int i=0; i<jmlpenerima.intValue();i++)
			{
				Benefeciary bf1= (Benefeciary)benef.get(i);
				bf1.setReg_spaj(strTmpSPAJ);
				bf1.setMsaw_number(new Integer(i+1));
			}
			//------------------------------------------------------------
			// Insert Beneficiary information to MST_BENEFICIARY
			if (jmlpenerima.intValue() >0)
			{
				for (int i=0; i<jmlpenerima.intValue();i++)
				{
					Benefeciary bf1= (Benefeciary)benef.get(i);
					bacDao.insertMst_benefeciary(bf1);
					//logger.info("insert mst_benefficiery");
				}
			}
		}
		
		private void proc_powersave(Cmdeditbac edit, String strTmpSPAJ,Date v_strDateNow,Integer v_intActionBy )throws ServletException,IOException, ParseException
		{
			edit.getPowersave().setLus_id(edit.getPemegang().getLus_id());
			edit.getPowersave().setReg_spaj(strTmpSPAJ);
			edit.getPowersave().setMpr_jangka_invest(edit.getPowersave().getMps_jangka_inv());
			edit.getPowersave().setMpr_deposit(edit.getPowersave().getMps_prm_deposit());
			edit.getPowersave().setMpr_interest(edit.getPowersave().getMps_prm_interest());
			edit.getPowersave().setMpr_rate(edit.getPowersave().getMps_rate());
			edit.getPowersave().setMpr_jns_ro(edit.getPowersave().getMps_roll_over());
			edit.getPowersave().setMpr_tgl_rate(edit.getPowersave().getMps_rate_date());
			
			int jangka_invest = Integer.parseInt(edit.getPowersave().getMps_jangka_inv());
			
			Date v_jth_tempo = edit.getPowersave().getMps_batas_date();
			Date ldt_last = FormatDate.add(v_jth_tempo,Calendar.DATE,-1);

			edit.getPowersave().setMpr_mature_date(ldt_last);
			
			if (edit.getDatausulan().getFlag_bulanan().intValue()==1)
			{
				Date tgl_deposit =edit.getPowersave().getMps_deposit_date();
				Date tgl_mature = FormatDate.add(tgl_deposit,Calendar.MONTH,1);
				tgl_mature = FormatDate.add(tgl_mature,Calendar.DAY_OF_MONTH,-1);
				edit.getPowersave().setMpr_mature_date(tgl_mature);
			}
			
			
			Date tgldeposit = edit.getPowersave().getMps_deposit_date();
			Date tglmature = edit.getPowersave().getMpr_mature_date();
			f_hit_umur umr =new f_hit_umur();
			int hari=umr.hari_powersave(tgldeposit.getYear()+1900,tgldeposit.getMonth()+1,tgldeposit.getDate(),tglmature.getYear()+1900,tglmature.getMonth()+1,tglmature.getDate());
			
			//if(edit.getDatausulan().isPsave || edit.getPowersave().getMsl_spaj_lama()!=null){
			//	edit.getPowersave().setMps_deposit_date(edit.getPowersave().getBegdate_topup());
			//	edit.getPowersave().setMpr_mature_date(FormatDate.add(edit.getPowersave().getEnddate_topup(), Calendar.DATE, -1));
			//	v_jth_tempo = bacDao.selectPowerSaveRoSurrender(edit.getPowersave().getMsl_spaj_lama());
			//	ldt_last = FormatDate.add(v_jth_tempo,Calendar.DATE,-1);
//
			//	edit.getPowersave().setMpr_mature_date(ldt_last);
			//}
			//
			//if(edit.getPowersave().getMsl_spaj_lama()!=null){
			//	hari=umr.hari_powersave(
			//			edit.getPowersave().getMps_deposit_date().getYear()+1900,edit.getPowersave().getMps_deposit_date().getMonth()+1,edit.getPowersave().getMps_deposit_date().getDate(),edit.getPowersave().getMpr_mature_date().getYear()+1900,edit.getPowersave().getMpr_mature_date().getMonth()+1,edit.getPowersave().getMpr_mature_date().getDate());
			//}
			
			String mgi = edit.getPowersave().getMpr_jangka_invest();

			//powersave bulanan
			if (edit.getDatausulan().getFlag_bulanan().intValue()==1){
				int lsbs_id = edit.getDatausulan().getLsbs_id().intValue();
				int lsdbs_number = edit.getDatausulan().getLsdbs_number();
				
				//Yusuf - Mulai 1 Oktober 2008, semua bulanan pakai rumus hari
				if(tgldeposit.compareTo(defaultDateFormat.parse("01/10/2008")) >= 0) {
					
					hari = hari + 1;
					edit.getPowersave().setMpr_jns_rumus(new Integer(1));
					edit.getPowersave().setMpr_hari(new Integer(hari));					
					
				}else {

					//platinum save bulanan, pakai rumus hari
					if(lsbs_id == 158 && (lsdbs_number==5 || lsdbs_number==8 || lsdbs_number==9)) {
						
						hari = hari + 1;
						edit.getPowersave().setMpr_jns_rumus(new Integer(1));
						edit.getPowersave().setMpr_hari(new Integer(hari));
						
					}else if(lsbs_id == 176){
						
						hari = hari + 1;
						edit.getPowersave().setMpr_jns_rumus(new Integer(1));
						edit.getPowersave().setMpr_hari(new Integer(hari));
						
//						yang lain, pakai rumus bulan
					}else {
						edit.getPowersave().setMpr_jns_rumus(new Integer(3));					
					}				

				}
				
			}else{
				if (Integer.parseInt(mgi) > 12){
					//Yusuf (7/4/2008) Rumus pangkat (2) sudah tidak dipakai, ganti rumus hari (1)
					//edit.getPowersave().setMpr_jns_rumus(new Integer(2));
					hari = hari + 1;
					edit.getPowersave().setMpr_jns_rumus(new Integer(2));
					edit.getPowersave().setMpr_hari(new Integer(hari));
				}else{
					hari =hari + 1;
					edit.getPowersave().setMpr_jns_rumus(new Integer(1));
					edit.getPowersave().setMpr_hari(new Integer(hari));
				}
			}
			
			//(Deddy) : hitung total rider apabila ada
			//Double tot_rider = 0.0;
			//if(edit.getDatausulan().getDaftaRider().size()!=0){
			//	
			//	List<Datarider> xx = edit.getDatausulan().getDaftaRider();
			//	for(Datarider datarider : xx){
			//		tot_rider =tot_rider + datarider.getMspr_premium();
			//	}
			//	
			//	//Cara Bayar langsung premi rider total = 0; potong bunga premi_rider_total diisi
			//	if(edit.getPowersave().getMpr_cara_bayar_rider()==0 || edit.getPowersave().getMpr_cara_bayar_rider()==2){
			//		edit.getPowersave().setMpr_rider_total(0.0);	
			//	}else if(edit.getPowersave().getMpr_cara_bayar_rider()==1){
			//		edit.getPowersave().setMpr_rider_total(0.0);	
			//	}
				
			//}else edit.getPowersave().setMpr_rider_total(0.0);

			//YUSUF (7/apr/08)
			//APABILA POWERSAVE BIASA, insert powersave proses dgn kode 5 saja, set end period = mature date dari powersave RO, lalu insert powersave ro
			if(edit.getDatausulan().getLsbs_id().intValue() != 158) {
				
				edit.getPowersave().setMps_kode(5);
				
				edit.getPowersave().setMps_end_period(edit.getPowersave().getMpr_mature_date());
				
				//PEMBULATAN BERDASARKAN KURS - Yusuf 20 Oct 2009 - Req Dr Ingrid, semua pembulatan 2 digit
				edit.getPowersave().setMpr_interest(new Double(FormatNumber.round(edit.getPowersave().getMpr_interest(),2)));
				edit.getPowersave().setMps_prm_interest(new Double(FormatNumber.round(edit.getPowersave().getMps_prm_interest(),2)));
				
				bacDao.insertPowersave(edit.getPowersave(), "mst_powersave_proses", edit.getDatausulan().getLku_id(), edit.getRekening_client());
				
				if(edit.getPowersave().getMpr_breakable() == null) edit.getPowersave().setMpr_breakable(0);
				
				//PROSES INSERT POWERSAVE RO
				bacDao.insertPowersave(edit.getPowersave(), "mst_powersave_ro", edit.getDatausulan().getLku_id(), edit.getRekening_client());
				
			//APABILA POWERSAVE BULANAN, insert powersave proses dgn kode 5 untuk bulan pertama, dan kode 7 untuk bulan2 berikutnya
			}else {
				DateFormat h = new SimpleDateFormat("dd");
				int jangka = Integer.valueOf(edit.getPowersave().getMps_jangka_inv()); //looping mulai dari 1 s/d mgi - 1
				Date initialDepositDate = edit.getPowersave().getMps_deposit_date();
				int tglAwal = Integer.valueOf(h.format(initialDepositDate));
				Date depositDate = edit.getPowersave().getMps_deposit_date();
				Date nextDepositDate;
				for(int i=0; i<jangka; i++) {
					int tglSkrg;
					if(i == 0) {
						edit.getPowersave().setMps_kode(5); 
					}else {
						edit.getPowersave().setMps_kode(7);

						depositDate = uwDao.selectFaddMonths(defaultDateFormat.format(depositDate), 1); //bertambah x bulan dari deposit date
						tglSkrg = Integer.valueOf(h.format(depositDate)).intValue();
						if(tglSkrg > tglAwal) {                 
							depositDate = FormatDate.add(depositDate, Calendar.DATE, tglAwal-tglSkrg);
						}					
						edit.getPowersave().setMps_deposit_date(depositDate);
					}
					
					nextDepositDate = uwDao.selectFaddMonths(defaultDateFormat.format(depositDate), 1); //bertambah x bulan dari deposit date
					tglSkrg = Integer.valueOf(h.format(nextDepositDate)).intValue();
					if(tglSkrg > tglAwal) {
						nextDepositDate = FormatDate.add(nextDepositDate, Calendar.DATE, tglAwal-tglSkrg);
					}
					edit.getPowersave().setMps_end_period(FormatDate.add(nextDepositDate, Calendar.DATE, -1));
					
					if (edit.getPowersave().getMpr_jns_rumus().intValue() == 1) { //rumus hari
						int jumlah_hari_mgi_bulanan = umr.hari_powersave(
								edit.getPowersave().getMps_deposit_date().getYear()+1900,
								edit.getPowersave().getMps_deposit_date().getMonth()+1,
								edit.getPowersave().getMps_deposit_date().getDate(),
								edit.getPowersave().getMps_end_period().getYear()+1900,
								edit.getPowersave().getMps_end_period().getMonth()+1,
								edit.getPowersave().getMps_end_period().getDate());
						edit.getPowersave().setMps_prm_interest((((Double.parseDouble(Integer.toString(jumlah_hari_mgi_bulanan+1)) / 365) * edit.getPowersave().getMps_prm_deposit().doubleValue())* edit.getPowersave().getMps_rate().doubleValue()) / 100);
					}else{ //rumus bulan
						edit.getPowersave().setMps_prm_interest(edit.getPowersave().getMps_prm_deposit().doubleValue()*(( edit.getPowersave().getMps_rate().doubleValue() / 100) /12));
					}
					//kopi perhitungannya
					edit.getPowersave().setMpr_interest(edit.getPowersave().getMps_prm_interest());
					
					//PEMBULATAN BERDASARKAN KURS - Yusuf 20 Oct 2009 - Req Dr Ingrid, semua pembulatan 2 digit
					edit.getPowersave().setMpr_interest(new Double(FormatNumber.round(edit.getPowersave().getMpr_interest(),2)));
					edit.getPowersave().setMps_prm_interest(new Double(FormatNumber.round(edit.getPowersave().getMps_prm_interest(),2)));
					
					bacDao.insertPowersave(edit.getPowersave(), "mst_powersave_proses", edit.getDatausulan().getLku_id(), edit.getRekening_client());
					
					//PROSES INSERT POWERSAVE RO
					if(i == 0) {
						if(edit.getPowersave().getMpr_breakable() == null) edit.getPowersave().setMpr_breakable(0);
						bacDao.insertPowersave(edit.getPowersave(), "mst_powersave_ro", edit.getDatausulan().getLku_id(), edit.getRekening_client());
					}
				}
			}
			
			//PROSES INSERT POWERSAVE PROSES (MPS_KODE = 0)
			Date v_strBeginDate = edit.getDatausulan().getMste_beg_date();
			//f_hit_umur umr= new f_hit_umur();
			Date tgl_max_powersave = bacDao.select_max_deposit_date();
			Date ldt_deposit=null;
			Double ldec_intrs;
			Double v_bunga = edit.getPowersave().getMps_rate();
			if(v_bunga==null){
				v_bunga=0.0;
			}
			Double v_jmlh_invest = edit.getPowersave().getMps_prm_deposit();
			int jml_hr = umr.hari_powersave(v_strBeginDate.getYear()+1900, v_strBeginDate.getMonth()+1 , v_strBeginDate.getDate(),tgl_max_powersave.getYear()+1900 , tgl_max_powersave.getMonth()+1, tgl_max_powersave.getDate());
			if (jml_hr >0)
			{
				for (int i=1 ; i <= jml_hr ; i++)
				{
					ldt_deposit = FormatDate.add(v_strBeginDate,Calendar.DATE,i);
					
					//rumus hari
					ldec_intrs = new Double(v_jmlh_invest.doubleValue() *  v_bunga.doubleValue() / 100  *  i / 365 );

					//rumus pangkat udah gak dipake
//					if (jangka_invest <12)
//					{
//						//rumus hari
//						ldec_intrs = new Double(v_jmlh_invest.doubleValue() *  v_bunga.doubleValue() / 100  *  i / 365 );
//					}else
//					{
//						//rumus pangkat
//						ldec_intrs = new Double(v_jmlh_invest.doubleValue() * ( Math.pow(( 1 + ( v_bunga.doubleValue() / 100 ) ) , ( jangka_invest / 12 ))	- 1 )  * ( i / 365.25 ) * ( 12 / jangka_invest)) ;
//					}


					edit.getPowersave().setMps_deposit_date(ldt_deposit);
					edit.getPowersave().setMps_prm_interest(ldec_intrs);
					
					//insert powersave bunga
					edit.getPowersave().setMps_kode(0);
					bacDao.insertPowersave(edit.getPowersave(), "mst_powersave_proses", edit.getDatausulan().getLku_id(), edit.getRekening_client());
					//logger.info("insert mst powersave proses");
				}
			}
		}
		
		//stable link
		private void proc_powersave_stable(Cmdeditbac edit, String strTmpSPAJ,Date v_strDateNow,Integer v_intActionBy )throws ServletException,IOException, DataAccessException, ParseException{
			String kurs = edit.getDatausulan().getKurs_p();
			Powersave stable = edit.getPowersave();
			Date v_jth_tempo = stable.getMps_batas_date();
			Date ldt_last1 = null;
			if(v_jth_tempo!=null){
				ldt_last1 =FormatDate.add(v_jth_tempo,Calendar.DATE,-1);
			}
			stable.setMpr_mature_date(ldt_last1);

			stable.setReg_spaj(strTmpSPAJ);
			stable.setMsl_no(1);
			stable.setLji_id(kurs.equals("01") ? "22" : "23");
			stable.setMsl_kode(5);
			stable.setMsl_ro(stable.getMps_roll_over());
			stable.setNo_reg(sequence.sequenceNoRegStableLink()); //MST_COUNTER WHERE MSCO_NUMBER = 83
			/*
			73 INPUT TOP UP SLINK
			74 INPUT WITHDRAW SLINK
			75 PRINT KUSTODIAN SLINK
			76 PROSES NAB SLINK
			77 SURAT TRANSAKSI SLINK
			78 PRINT 6 BULANAN SLINK 
			 */			
			stable.setMsl_posisi(78);
			stable.setMsl_bdate(edit.getDatausulan().getMste_beg_date());
			if(stable.getMps_batas_date()!=null){
				stable.setMsl_edate(FormatDate.add(stable.getMps_batas_date(), Calendar.DATE, -1));
			}
			if(stable.getMps_employee()==1){
				stable.setMsl_employee(1);
			}
			
			if(products.progressiveLink(edit.getDatausulan().getLsbs_id().toString())){//Req Rudi : untuk progressive save dan progressive link, mss_letter_date/msl_letter_date diisi 1 tahun dari begdate dan mss_no/msl_no diisi 1
				stable.setMsl_letter_date(FormatDate.add(stable.getMsl_bdate(), Calendar.YEAR,1));
			}else{
				stable.setMsl_letter_date(FormatDate.add(stable.getMsl_bdate(), Calendar.MONTH, 6));
			}
			stable.setMsl_premi(edit.getDatausulan().getMspr_premium());
			stable.setMsl_rate(stable.getMps_rate());
			stable.setMsl_mgi(Integer.parseInt(stable.getMps_jangka_inv()));
			stable.setMsl_bunga(stable.getMps_prm_interest());
			stable.setMsl_tax(0.);
			//stable.setMsl_tgl_nab(msl_tgl_nab); //3 point ini, dihitung di EditbacValidator
			//stable.setMsl_nab(msl_nab);
			//stable.setMsl_unit(msl_unit);
			stable.setMsl_saldo_unit(stable.getMsl_unit());
			//stable.setMsl_bp_rate(msl_bp_rate);
			stable.setMsl_tgl_nab_bp(null);
			stable.setMsl_nab_bp(0.);
			stable.setMsl_nilai_polis(null);
			stable.setMsl_bp(0.);
			stable.setMsl_bp_pt(0.);
			stable.setMsl_up(edit.getDatausulan().getMspr_tsi());
			Date tglmature = stable.getMpr_mature_date();
			f_hit_umur umr =new f_hit_umur();
			Date v_jth_tempo1 = null ;
			int hari=umr.hari_powersave(
					stable.getMsl_bdate().getYear()+1900,stable.getMsl_bdate().getMonth()+1,stable.getMsl_bdate().getDate(),tglmature.getYear()+1900,tglmature.getMonth()+1,tglmature.getDate());
			//if(edit.getDatausulan().isPsave){
			if(edit.getDatausulan().isPsave || edit.getPowersave().getMsl_spaj_lama()!=null || edit.getDatausulan().isPlatinumSave){
				stable.setMsl_bdate(edit.getPowersave().getBegdate_topup());
				//if(stable.getEnddate_topup()!=null){
					//stable.setMsl_edate(FormatDate.add(stable.getEnddate_topup(), Calendar.DATE, -1));
				//}else{
				if(stable.getBegdate_topup()!=null){
					stable.setMsl_edate(FormatDate.add(FormatDate.add(stable.getBegdate_topup(), Calendar.MONTH, stable.getMsl_mgi()), Calendar.DATE, -1));
				}
					//stable.setMsl_edate(FormatDate.add(stable.getMps_batas_date(), Calendar.DATE, -1));
				//}
				v_jth_tempo1 = bacDao.selectPowerSaveRoSurrender(edit.getPowersave().getMsl_spaj_lama());
			}else{
				v_jth_tempo1 = edit.getDatausulan().getMste_beg_date();
			}
			
			if(edit.getPowersave().getMsl_spaj_lama()!=null){
				hari=umr.hari_powersave(
						stable.getMsl_bdate().getYear()+1900,stable.getMsl_bdate().getMonth()+1,stable.getMsl_bdate().getDate(),stable.getMsl_edate().getYear()+1900,stable.getMsl_edate().getMonth()+1,stable.getMsl_edate().getDate());
			}
			hari =hari + 1;
			stable.setMsl_hari(new Integer(hari));	
			Date ldt_last = null;
			if(v_jth_tempo1!=null){
				ldt_last = FormatDate.add(v_jth_tempo1,Calendar.MONTH,3);
			}
			if(edit.getDatausulan().getLsbs_id().intValue()!=188){
				stable.setMsl_tarik_pertama( stable.getTarik_bunga() == 0 ? 0. : stable.getMps_prm_interest());
				stable.setMsl_next_date(stable.getTarik_bunga() == 0 ? ldt_last : null);
			}else{
				stable.setMsl_next_date(FormatDate.add(stable.getMsl_edate(), Calendar.DATE, 1));
			}
			
			stable.setMsl_bayar_bunga(0);
			stable.setMsl_bayar_bp(0);
			stable.setMsl_trans_date(null);
			stable.setMsl_proses_date(null);
			stable.setMsl_confirm_date(null);
			stable.setMsl_prod_date(null);
			stable.setFlag_rate(stable.getMps_jenis_plan());
			stable.setMsl_tahun_ke(1);
			stable.setMsl_premi_ke(1);
			stable.setMsl_note(stable.getMpr_note());
			stable.setLus_id(edit.getPemegang().getLus_id());
			stable.setMsl_flag_print(null);
			stable.setMsl_print_date(null);
			stable.setMsl_aktif(1);
			stable.setMsl_tu_ke(0);
			stable.setMsl_flag_up(0);
			stable.setMsl_jn_rumus(1);
			stable.setMsl_flag_comm(0);
			stable.setMsl_print(0);
			stable.setNo_reg_ref(null);
			stable.setMsl_bayar_tarik(0);
			if(products.progressiveLink(edit.getDatausulan().getLsbs_id().toString())){
				stable.setMsl_desc("Premi ke-1");
			}else{
				stable.setMsl_desc("Premi Utama");
			}
			stable.setMsl_spaj_lama(edit.getDatausulan().getKopiSPAJ());
			stable.msl_spaj_lama = stable.getMsl_spaj_lama();
			
			//Yusuf 28 Jul 09 - Tambahan Perhitungan bila Stable Link Manfaat Bulanan (FLAG_BULANAN = 1)
			//Perhitungan ini berlaku untuk Premi Pokok dan Premi Topup sejumlah MGI
			//Contoh : MGI = 12 bulan, maka akan ada 2 x 12 row yg diinsert = 24 row, 12 untuk pokok, 12 untuk TU
			if(stable.flag_bulanan.intValue() > 0){

				stable.msl_bunga = 0.; //bunga di nol kan bila manfaat bulanan
				
				if(edit.getCurrentUser().getJn_bank().intValue() == 2){
					stable.msl_proses_bsm = 1;
				}else if(edit.getCurrentUser().getJn_bank().intValue() == 3){
					stable.msl_proses_bsm = 2;
				}else{
					stable.msl_proses_bsm = 0;
				}
				
				//INSERT UNTUK PREMI POKOK
				if(edit.getDatausulan().getLsbs_id().intValue()==188){
					insert("insert.mst_psave", stable);
				}else{
					insert("insert.mst_slink", stable);
				}
				Integer loop_slink_bayar = stable.msl_mgi/stable.flag_bulanan.intValue();
				
				for(int i=0; i<loop_slink_bayar; i++){
					SlinkBayar sb = new SlinkBayar();
					sb.reg_spaj = stable.reg_spaj;
					sb.msl_no = stable.msl_no;                        
					sb.lji_id = stable.lji_id;
					
					//Yusuf (11/09/09) Request Rudy : tidak usah generate disini, diisi angka urut aja
					//sb.mslb_bayar_id = sequence.sequenceNoRegSlinkBayar();                  
					sb.mslb_bayar_id = String.valueOf(i);
					
					sb.mslb_desc = "Bunga"; //CASE SENSITIVE, karena query rudy banyak yg -> where mslb_desc = 'Bunga'                      
					sb.mslb_tu_ke = stable.msl_tu_ke;                    
					sb.lus_id = stable.lus_id;                        
					sb.lku_id = kurs;                         
					sb.mslb_print_date = null;       
					
					sb.mslb_beg_period = uwDao.selectFaddMonths(defaultDateFormat.format(stable.msl_bdate), stable.flag_bulanan.intValue()*i); //bertambah 1 bulan dari deposit date
					sb.mslb_end_period = FormatDate.add(uwDao.selectFaddMonths(defaultDateFormat.format(stable.msl_bdate), (stable.flag_bulanan.intValue()*(i+1))), Calendar.DATE, -1);
					sb.mslb_paid_date = commonDao.selectAddWorkdays(sb.mslb_end_period, 1);                   
					sb.mslb_due_date = sb.mslb_end_period;                    
					
					Rekening_client rek = edit.getRekening_client();
					
					sb.mslb_rekening =           
						rek.getLsbp_nama() + 
						" CAB. " + rek.getMrc_cabang() + 
						"-" + rek.getMrc_kota() + 
						", A/C." + rek.getMrc_no_ac() + 
						"(" + (kurs.equals("01") ? "Rp." : "US$") + 
						"), A/N. " + rek.getMrc_nama();
					
					sb.mslb_up = stable.msl_up;                        
					sb.mslb_premi = stable.msl_premi;                     
					sb.mslb_pinalti = 0.;          
					
					sb.mslb_rate = stable.msl_rate;
					
					hari = umr.hari_powersave(
							sb.mslb_beg_period.getYear()+1900,sb.mslb_beg_period.getMonth()+1,sb.mslb_beg_period.getDate(),sb.mslb_end_period.getYear()+1900,sb.mslb_end_period.getMonth()+1,sb.mslb_end_period.getDate());
					hari = hari + 1;
					sb.mslb_jml_hari = hari;			
					
					sb.mslb_bunga = (new Double(hari)/365) * (sb.mslb_rate/100) * sb.mslb_premi;                     
					
					sb.mslb_bp = 0.;                        
					sb.mslb_tarik = 0.;                     
					sb.mslb_jum_bayar = sb.mslb_bunga;                 
					sb.mslb_tgl_nab = null;                     
					sb.mslb_nab = 0.;                       
					sb.mslb_unit = 0.;                      
					sb.mslb_tambah = 0.;                    
					sb.mslb_hari = 0;                     
					sb.mslb_notes = null;                     
					sb.mslb_flag_bayar = 0;               
					sb.mslb_filing = 0;                   
					sb.mslb_filing_date = null;                 
					sb.mslb_bp_pt = 0.;                     
					
					sb.lsbp_id = Integer.valueOf(rek.getLsbp_id());                       
					sb.mrc_cabang = rek.getMrc_cabang();                     
					sb.mrc_atas_nama = rek.getMrc_nama();                  
					sb.mrc_no_ac = rek.getMrc_no_ac();  
					
					sb.mslb_kurang = 0.;   
					sb.flag_bulanan = stable.flag_bulanan;        
					sb.flag_proses = 0;
					
					if(edit.getDatausulan().getLsbs_id().intValue()==188){
						insert("insert.mst_psave_bayar", sb);
					}else{
						insert("insert.mst_slink_bayar", sb);
					}
					
				}
			}else{
				
				if(edit.getCurrentUser().getJn_bank().intValue() == 2){
					stable.msl_proses_bsm = 1;
				}else if(edit.getCurrentUser().getJn_bank().intValue() == 3){
					stable.msl_proses_bsm = 2;
				}else{
					stable.msl_proses_bsm = 0;
				}
				
				//INSERT UNTUK PREMI POKOK
				if(edit.getDatausulan().getLsbs_id().intValue()==188){
					insert("insert.mst_psave", stable);
				}else{
					insert("insert.mst_slink", stable);
				}
			}			
			
			if(edit.getDatausulan().getLsbs_id().intValue()!=188){
				//INSERT UNTUK PREMI TU
				if (edit.getInvestasiutama().getDaftartopup().getPil_tunggal().intValue() != 0){
					stable.setMsl_no(2);
					stable.setNo_reg(sequence.sequenceNoRegStableLink()); //MST_COUNTER WHERE MSCO_NUMBER = 83
					stable.setMsl_bdate(stable.getBegdate_topup());
					if(stable.getEnddate_topup()!=null){
//						stable.setMsl_edate(FormatDate.add(stable.getEnddate_topup(), Calendar.DATE, -1));
						stable.setMsl_edate(FormatDate.add(FormatDate.add(stable.getBegdate_topup(), Calendar.MONTH, stable.getMsl_mgi()), Calendar.DATE, -1));
					}
					stable.setMsl_premi(edit.getInvestasiutama().getDaftartopup().getPremi_tunggal());
					stable.setMsl_bunga(stable.getMps_prm_interest_tu());
					stable.setMsl_tgl_nab(stable.getMsl_tgl_nab_tu()); //3 point ini, dihitung di editbacvalidator
					stable.setMsl_nab(stable.getMsl_nab_tu());
					stable.setMsl_unit(stable.getMsl_unit_tu());
					stable.setMsl_saldo_unit(stable.getMsl_unit());
					umr =new f_hit_umur();
					if(edit.getPowersave().getMsl_spaj_lama()!=null){
						hari=umr.hari_powersave(
								stable.getMsl_bdate().getYear()+1900,stable.getMsl_bdate().getMonth()+1,stable.getMsl_bdate().getDate(),stable.getMsl_edate().getYear()+1900,stable.getMsl_edate().getMonth()+1,stable.getMsl_edate().getDate());
					}else {
						hari=umr.hari_powersave(
						stable.getMsl_bdate().getYear()+1900,stable.getMsl_bdate().getMonth()+1,stable.getMsl_bdate().getDate(),tglmature.getYear()+1900,tglmature.getMonth()+1,tglmature.getDate());
					}
					hari=hari + 1;
					stable.setMsl_hari(new Integer(hari));			
					stable.setMsl_premi_ke(2);
					stable.setMsl_tu_ke(1);
					stable.setMsl_desc("Premi Top Up Ke-1");
					//(Deddy)
					//stable.setFlag_rider(0);
					
					if(stable.getMsl_premi() != null && stable.getMsl_bunga() != null ) {
						
						//Yusuf 28 Jul 09 - Tambahan Perhitungan bila Stable Link Manfaat Bulanan (FLAG_BULANAN = 1)
						//Perhitungan ini berlaku untuk Premi Pokok dan Premi Topup sejumlah MGI
						//Contoh : MGI = 12 bulan, maka akan ada 2 x 12 row yg diinsert = 24 row, 12 untuk pokok, 12 untuk TU
						if(stable.flag_bulanan.intValue() >  0){
							
							stable.msl_bunga = 0.; //bunga di nol kan bila manfaat bulanan
							
							if(edit.getCurrentUser().getJn_bank().intValue() == 2){
								stable.msl_proses_bsm = 1;
							}else if(edit.getCurrentUser().getJn_bank().intValue() == 3){
								stable.msl_proses_bsm = 2;
							}else{
								stable.msl_proses_bsm = 0;
							}
	
							//INSERT UNTUK PREMI TU
							insert("insert.mst_slink", stable);
							Integer loop_slink_bayar = stable.msl_mgi/stable.flag_bulanan.intValue();
							for(int i=0; i<loop_slink_bayar; i++){
								SlinkBayar sb = new SlinkBayar();
								sb.reg_spaj = stable.reg_spaj;
								sb.msl_no = stable.msl_no;                        
								sb.lji_id = stable.lji_id;                         
	
								//Yusuf (11/09/09) Request Rudy : tidak usah generate disini, diisi angka urut aja
								//sb.mslb_bayar_id = sequence.sequenceNoRegSlinkBayar();                  
								sb.mslb_bayar_id = String.valueOf(i);
								
								sb.mslb_desc = "Bunga";                      
								sb.mslb_tu_ke = stable.msl_tu_ke;                    
								sb.lus_id = stable.lus_id;                        
								sb.lku_id = kurs;                         
								sb.mslb_print_date = null;       
								
								sb.mslb_beg_period = uwDao.selectFaddMonths(defaultDateFormat.format(stable.msl_bdate), stable.flag_bulanan.intValue()*i); //bertambah 1 bulan dari deposit date
								sb.mslb_end_period = FormatDate.add(uwDao.selectFaddMonths(defaultDateFormat.format(stable.msl_bdate), stable.flag_bulanan.intValue()*(i+1)), Calendar.DATE, -1);
								sb.mslb_paid_date = commonDao.selectAddWorkdays(sb.mslb_end_period, 1);                   
								sb.mslb_due_date = sb.mslb_end_period;                    
								
								Rekening_client rek = edit.getRekening_client();
								
								sb.mslb_rekening =           
									rek.getLsbp_nama() + 
									" CAB. " + rek.getMrc_cabang() + 
									"-" + rek.getMrc_kota() + 
									", A/C." + rek.getMrc_no_ac() + 
									"(" + (kurs.equals("01") ? "Rp." : "US$") + 
									"), A/N. " + rek.getMrc_nama();
								
								sb.mslb_up = stable.msl_up;                        
								sb.mslb_premi = stable.msl_premi;                     
								sb.mslb_pinalti = 0.;          
								
								sb.mslb_rate = stable.msl_rate;
								
								hari = umr.hari_powersave(
										sb.mslb_beg_period.getYear()+1900,sb.mslb_beg_period.getMonth()+1,sb.mslb_beg_period.getDate(),sb.mslb_end_period.getYear()+1900,sb.mslb_end_period.getMonth()+1,sb.mslb_end_period.getDate());
								hari = hari + 1;
								sb.mslb_jml_hari = hari;			
								
								sb.mslb_bunga = (new Double(hari)/365) * (sb.mslb_rate/100) * sb.mslb_premi;                     
								
								sb.mslb_bp = 0.;                        
								sb.mslb_tarik = 0.;                     
								sb.mslb_jum_bayar = sb.mslb_bunga;                 
								sb.mslb_tgl_nab = null;                     
								sb.mslb_nab = 0.;                       
								sb.mslb_unit = 0.;                      
								sb.mslb_tambah = 0.;                    
								sb.mslb_hari = 0;                     
								sb.mslb_notes = null;                     
								sb.mslb_flag_bayar = 0;               
								sb.mslb_filing = 0;                   
								sb.mslb_filing_date = null;                 
								sb.mslb_bp_pt = 0.;                     
								
								sb.lsbp_id = Integer.valueOf(rek.getLsbp_id());                       
								sb.mrc_cabang = rek.getMrc_cabang();                     
								sb.mrc_atas_nama = rek.getMrc_nama();                  
								sb.mrc_no_ac = rek.getMrc_no_ac();  
								
								sb.mslb_kurang = 0.;   
								sb.flag_bulanan = stable.flag_bulanan;        
								sb.flag_proses = 0;
								
								insert("insert.mst_slink_bayar", sb);
							}
							stable.msl_bunga = 0.; //bunga di nol kan bila manfaat bulanan
						}else{
							if(edit.getDatausulan().getLsbs_id().intValue()!=188){
								if(edit.getCurrentUser().getJn_bank().intValue() == 2){
									stable.msl_proses_bsm = 1;
								}else if(edit.getCurrentUser().getJn_bank().intValue() == 3){
									stable.msl_proses_bsm = 2;
								}else{
									stable.msl_proses_bsm = 0;
								}
		
								//INSERT UNTUK PREMI TU
								insert("insert.mst_slink", stable);
							}
						}
					}
				}
			}
					
		}

		private void proc_unitlink(Cmdeditbac edit, String strTmpSPAJ,Date v_strDateNow,Integer v_intActionBy , User currentUser ,Date ldt_endpay1,  Date ldt_endpay4,  Date ldt_endpay5)throws ServletException,IOException
		{
			
			logger.info("================== START PROC_UNITLINK ==================");

			//Variables 
			Date v_tglsurat = edit.getInvestasiutama().getMu_tgl_surat();
			Date v_strBeginDate = edit.getDatausulan().getMste_beg_date();
			Date v_strEndDate = edit.getDatausulan().getMste_end_date();
			Double v_curBasePremium = edit.getDatausulan().getMspr_premium();

			Integer v_topup_tunggal = edit.getInvestasiutama().getDaftartopup().getPil_tunggal();
			Double v_jmlhtopup_tunggal = edit.getInvestasiutama().getDaftartopup().getPremi_tunggal();
			Integer v_topup_berkala = edit.getInvestasiutama().getDaftartopup().getPil_berkala();
			Double v_jmlhtopup_berkala = edit.getInvestasiutama().getDaftartopup().getPremi_berkala();
			
			Integer lt_id_tunggal = edit.getDatausulan().getLi_trans_tunggal();
			Integer lt_id_berkala = edit.getDatausulan().getLi_trans_berkala();

			List invvl = edit.getInvestasiutama().getDaftarinvestasi();

			//Hitung Biaya Ulink dulu
			double[] biayaUlink = {0, 0, 0, 0};
			
			for(int i=0; i<edit.getInvestasiutama().getDaftarbiaya().size(); i++) {
				Biayainvestasi bi = (Biayainvestasi) edit.getInvestasiutama().getDaftarbiaya().get(i);
				biayaUlink[bi.getMu_ke()] += bi.getMbu_jumlah();
			}
			
			
			int mu_ke = 1;
			
			//Save MST_ULINK untuk Premi Pokok
			proc_save_mst_ulink(edit,strTmpSPAJ, v_intActionBy ,v_tglsurat,1,1,1,v_curBasePremium,v_jmlhtopup_berkala,v_jmlhtopup_tunggal , v_topup_berkala,v_topup_tunggal,v_strBeginDate,"");
			//Save MST_DET_ULINK untuk Premi Pokok
			for(int i=0; i<invvl.size(); i++) {
				DetilInvestasi di = (DetilInvestasi) invvl.get(i);
				if(di.getMdu_persen1() != null) {
					if(di.getMdu_persen1() > 0) {
						//int persen_tu = (v_topup_berkala.intValue() > 0 || v_topup_tunggal.intValue() > 0) ? di.getMdu_persen1() : 0; 
						int persen_tu = 0; 
						proc_save_det_ulink(edit, strTmpSPAJ, v_intActionBy, di.getMdu_persen1(), persen_tu, di.getMdu_persen1() * (v_curBasePremium - biayaUlink[mu_ke]) /100, mu_ke, di.getLji_id1(), v_strBeginDate);
					}
				}
			}
			if (v_topup_berkala.intValue() > 0 ){
				mu_ke++;
				//Save MST_ULINK untuk Top-Up Berkala
				proc_save_mst_ulink(edit,strTmpSPAJ,v_intActionBy ,null,2,lt_id_tunggal,lt_id_berkala ,v_jmlhtopup_berkala  , 0., 0.,0 , 0,v_strBeginDate,"berkala");	
				//Save MST_DET_ULINK untuk Top-Up Berkala
				for(int i=0; i<invvl.size(); i++) {
					DetilInvestasi di = (DetilInvestasi) invvl.get(i);
					if(di.getMdu_persen1() != null) {
						if(di.getMdu_persen1() > 0) {
							//int persen_tu = (v_topup_berkala.intValue() > 0 || v_topup_tunggal.intValue() > 0) ? di.getMdu_persen1() : 0; 
							int persen_tu = 0; 
							proc_save_det_ulink(edit, strTmpSPAJ, v_intActionBy, di.getMdu_persen1(), di.getMdu_persen1(), di.getMdu_persen1() * (v_jmlhtopup_berkala - biayaUlink[mu_ke]) /100, mu_ke, di.getLji_id1(), v_strBeginDate);
						}
					}
				}
			}
			if (v_topup_tunggal.intValue() > 0 ){
				mu_ke++;
				//Save MST_ULINK untuk Top-Up Tunggal
				proc_save_mst_ulink(edit,strTmpSPAJ,v_intActionBy ,null,mu_ke,lt_id_tunggal,lt_id_berkala ,v_jmlhtopup_tunggal , 0., 0. , 0, 0, v_strBeginDate, "tunggal");	
				//Save MST_DET_ULINK untuk Top-Up Tunggal
				for(int i=0; i<invvl.size(); i++) {
					DetilInvestasi di = (DetilInvestasi) invvl.get(i);
					if(di.getMdu_persen1() != null) {
						if(di.getMdu_persen1() > 0) {
							//int persen_tu = (v_topup_berkala.intValue() > 0 || v_topup_tunggal.intValue() > 0) ? di.getMdu_persen1() : 0; 
							int persen_tu = 0; 
							proc_save_det_ulink(edit, strTmpSPAJ, v_intActionBy, di.getMdu_persen1(), di.getMdu_persen1(), di.getMdu_persen1() * (v_jmlhtopup_tunggal - biayaUlink[mu_ke]) /100, mu_ke, di.getLji_id1(), v_strBeginDate);
						}
					}
				}
			}
			
			//Save MST_BIAYA_ULINK
			for(int i=0; i<edit.getInvestasiutama().getDaftarbiaya().size();i++){
				for(int j=i+1; j<edit.getInvestasiutama().getDaftarbiaya().size();j++){
					Biayainvestasi bi = (Biayainvestasi) edit.getInvestasiutama().getDaftarbiaya().get(i);
					Biayainvestasi bi2 = (Biayainvestasi) edit.getInvestasiutama().getDaftarbiaya().get(j);
					logger.info(bi.getLjb_id());
					logger.info(bi2.getLjb_id());
					if(bi.getLjb_id().equals(bi2.getLjb_id())){
						edit.getInvestasiutama().getDaftarbiaya().remove(j);
						j--;
					}
				}
			}
			
			for(int i=0; i<edit.getInvestasiutama().getDaftarbiaya().size(); i++) {
				Biayainvestasi bi = (Biayainvestasi) edit.getInvestasiutama().getDaftarbiaya().get(i);
				
				if(edit.getDatausulan().getLsbs_id().intValue() != 162 && !products.stableLink(edit.getDatausulan().getLsbs_id().toString()) && 
						edit.getDatausulan().getLsbs_id().intValue() != 159 && edit.getDatausulan().getLsbs_id().intValue() != 160 && edit.getDatausulan().getLsbs_id().intValue() != 191 &&
						!edit.getAgen().getLca_id().equals("46") 
						&& bi.getLjb_id().intValue()!=43 && bi.getLjb_id().intValue()!=437 && bi.getLjb_id().intValue() >= 20 
						&& bi.getMbu_jumlah().intValue() == 0 && bi.getMbu_persen().intValue() == 0) {
					throw new RuntimeException("BIAYA UNIT LINK = 0 UNTUK LJB_ID = " + bi.getLjb_id());
				}
				
				Map param30 = new HashMap();
				param30.put("strTmpSPAJ", strTmpSPAJ);
				param30.put("mu_ke", bi.getMu_ke());
				param30.put("ljb_id", bi.getLjb_id());
				param30.put("mbu_jumlah", bi.getMbu_jumlah());
				param30.put("mbu_persen", bi.getMbu_persen());
				int ljb_id = bi.getLjb_id();
				param30.put("ldt_endpay", 
						ljb_id == 2 ? ldt_endpay1 : 
							ljb_id == 3 ? ldt_endpay4 : 
								ljb_id == 12 ? ldt_endpay5 : null);				
				insert("insert.mst_biaya_ulink", param30);						
			}
	
			
			logger.info("================== END PROC_UNITLINK ==================");
				
		}
		
		private void proc_save_karyawan(Cmdeditbac edit, String strTmpSPAJ,Integer v_intActionBy )throws ServletException,IOException
		{
			Integer v_intBaseBusinessId = edit.getDatausulan().getLsbs_id();
			Integer v_intBaseBusinessNo = edit.getDatausulan().getLsdbs_number();
			Double v_curBasePremium = edit.getDatausulan().getMspr_premium();
			Date v_strBeginDate = edit.getDatausulan().getMste_beg_date();
			
			String nama_plan="";
			Map param22=new HashMap();
			param22.put("kode_bisnis",v_intBaseBusinessId);
			param22.put("no_bisnis",v_intBaseBusinessNo);
			Map data4 = (HashMap) querySingle("select.nama_plan", param22);
			if (data4!=null)
			{		
				nama_plan=((String)data4.get("LSDBS_NAME")).toUpperCase();
			}
			//logger.info("select nama plan");
			
			if( edit.getDatausulan().getMste_flag_cc()==3){
				edit.getEmployee().setPlan(nama_plan);
				edit.getEmployee().setNo_urut(new Integer(1));
				edit.getEmployee().setReg_spaj(strTmpSPAJ);
				//edit.getEmployee().setPotongan(v_curBasePremium);
				edit.getEmployee().setTgl_proses(v_strBeginDate);
				edit.getEmployee().setStatus(new Integer(1));
				edit.getEmployee().setKeterangan("");
		
				insert("insert.mst_emp", edit.getEmployee());
				//logger.info("edit mst emp");
			}
		}
		
		private void proc_save_rider(Cmdeditbac edit, String strTmpSPAJ,Integer v_intActionBy )throws ServletException,IOException
		{
			String v_strKurs = edit.getDatausulan().getKurs_p().toUpperCase();
			Integer jumlah_rider = edit.getDatausulan().getJmlrider();
			Integer[] v_intRiderId;
			v_intRiderId = new Integer[jumlah_rider.intValue()+1];
			Integer[] v_intRiderNo;
			v_intRiderNo = new Integer[jumlah_rider.intValue()+1];
			Integer[] unitRider;
			unitRider = new Integer[jumlah_rider.intValue()+1];
			Integer[] classRider;
			classRider = new Integer[jumlah_rider.intValue()+1];
			Integer[] insperiodRider;
			insperiodRider = new Integer[jumlah_rider.intValue()+1];
			Double[] upRider;
			upRider = new Double[jumlah_rider.intValue()+1];
			Date[] end_dateRider;
			end_dateRider = new Date[jumlah_rider.intValue()+1];
			Date[] beg_dateRider;
			beg_dateRider = new Date[jumlah_rider.intValue()+1];
			Date[] end_payRider;
			end_payRider = new Date[jumlah_rider.intValue()+1];
			Double[] rateRider;
			rateRider = new Double[jumlah_rider.intValue()+1];
			Integer[] percentRider;
			percentRider = new Integer[jumlah_rider.intValue()+1];
			Double[] premi_rider;
			premi_rider = new Double[jumlah_rider.intValue()+1];
			//(Deddy)			
			Double[] premi_tahunan;
			premi_tahunan = new Double[jumlah_rider.intValue()+1];
			String[] kursRider;
			kursRider = new String[jumlah_rider.intValue()+1];
			Double[] premi_arider;
			premi_arider = new Double[jumlah_rider.intValue()+1];
			Double[] premi_brider;
			premi_brider = new Double[jumlah_rider.intValue()+1];
			Double[] premi_crider;
			premi_crider = new Double[jumlah_rider.intValue()+1];
			Double[] premi_drider;
			premi_drider = new Double[jumlah_rider.intValue()+1];
			Double[] premi_mrider;
			premi_mrider = new Double[jumlah_rider.intValue()+1];
			Integer[] ins_rider;
			ins_rider = new Integer[jumlah_rider.intValue()+1];
			Double[] mspr_extra;
			mspr_extra = new Double[jumlah_rider.intValue()+1];
			//(Deddy)
			Integer[] mpr_cara_bayar_rider;
			Integer[] lscb_id_rider;
			Integer flag_ekasehat = 0;
			mpr_cara_bayar_rider = new Integer[jumlah_rider.intValue()+1];
			lscb_id_rider = new Integer[jumlah_rider.intValue()+1];
			String status = edit.getPemegang().getStatus();
			if (status == null)
			{
				status = "input";
			}
			Integer flag_hcp= new Integer(0);
			
			Integer flag_rider_hcp =edit.getDatausulan().getFlag_rider_hcp();
			if (flag_rider_hcp == null)
			{
				flag_rider_hcp = new Integer(0);
			}
			
			List dtrd = edit.getDatausulan().getDaftaRider();
			if (jumlah_rider.intValue()>0)
			{
				for (int i =0 ; i<dtrd.size();i++)
				{
					Datarider rd= (Datarider)dtrd.get(i);
					v_intRiderId[i] = rd.getLsbs_id();
					v_intRiderNo[i] = rd.getLsdbs_number();
					unitRider[i] = rd.getMspr_unit();
					classRider[i] = rd.getMspr_class();
					insperiodRider[i] = rd.getMspr_ins_period();
					upRider[i] =rd.getMspr_tsi();
					end_dateRider[i] = rd.getMspr_end_date();
					if(end_dateRider[i].after(edit.getDatausulan().getMste_end_date())){
						end_dateRider[i]=edit.getDatausulan().getMste_end_date();
					}
					beg_dateRider[i] = rd.getMspr_beg_date();
					end_payRider[i] = rd.getMspr_end_pay();
//					if(end_payRider[i].after(edit.getDatausulan().getMspr_end_pay())){
//						end_payRider[i]=edit.getDatausulan().getMspr_end_pay();
//					}
					rateRider[i] =rd.getMspr_rate();
					percentRider[i] = rd.getMspr_persen();
					if(rd.getMspr_persen()==null){
						percentRider[i] = 0;
					}
					premi_rider[i] =rd.getMspr_premium();
					if(premi_rider[i]==null || premi_rider[i].equals("")){
						premi_rider[i]=0.0;
					}
					//(Deddy)
					premi_tahunan[i] =rd.getMrs_premi_tahunan();
					kursRider[i] = v_strKurs;
					premi_arider[i] = rd.getMspr_tsi_pa_a();
					premi_brider[i] = rd.getMspr_tsi_pa_b();
					premi_drider[i] = rd.getMspr_tsi_pa_d();
					premi_mrider[i] = rd.getMspr_tsi_pa_m();
					ins_rider[i] = rd.getMspr_tt();
					mspr_extra [i] = rd.getMspr_extra();
					if (v_intRiderId[i] == 819)
					{
						flag_hcp = new Integer(1);
						edit.getDatausulan().setFlag_hcp(flag_hcp);
					}
				}
			}
			for (int i =0 ; i<dtrd.size();i++)
			{
					Map param28=new HashMap();
					param28.put("strTmpSPAJ",strTmpSPAJ);	
					param28.put("v_intBaseBusinessId",v_intRiderId[i]);
					param28.put("v_intBaseBusinessNo",v_intRiderNo[i]);
					param28.put("v_strKurs",kursRider[i]);
					param28.put("v_strBeginDate",beg_dateRider[i]);
					param28.put("v_strEndDate",end_dateRider[i]);
					param28.put("intClass",classRider[i]);
					param28.put("v_intBaseRate",rateRider[i]);
					//(Deddy) - Cara bayar langsung : premi utama & rider diisi semua, sedangkan potong bunga : premi utama diisi & premi rider 0
					if(edit.getPowersave().getMpr_cara_bayar_rider()!=null  ){
						if(edit.getPowersave().getMpr_cara_bayar_rider()==1 || edit.getPowersave().getMpr_cara_bayar_rider()==3){
							param28.put("v_curBasePremium",0);
						}else param28.put("v_curBasePremium",premi_rider[i]);
						
					}else 
						param28.put("v_curBasePremium",premi_rider[i]);
					
					param28.put("v_intInsPeriod",insperiodRider[i]);
					param28.put("v_curUP",upRider[i]);
					param28.put("intPAA",premi_arider[i]);
					param28.put("intPAB",premi_brider[i]);
					param28.put("intPAC",premi_crider[i]);
					param28.put("intPAD",premi_drider[i]);
					param28.put("intPAMotor",premi_mrider[i]);
					param28.put("v_intUnit",unitRider[i]);
					if(v_intRiderId[i]==822){
						param28.put("ldt_endpay",null);
					}else{
						param28.put("ldt_endpay",end_payRider[i]);
					}
					
					Integer flagBikinPusing = 0; // 0 = bukan rider tertanggung tambahan, 1 rider tertanggung tambahan
					if((v_intRiderId[i]==820 && v_intRiderNo[i] >105) || (v_intRiderId[i]==826 && v_intRiderNo[i] >10) || (v_intRiderId[i]==823 && v_intRiderNo[i] >15) || (v_intRiderId[i]==825 && v_intRiderNo[i] >15) || (v_intRiderId[i]==819 && ((v_intRiderNo[i] >=20 && v_intRiderNo[i] <=140) || (v_intRiderNo[i] >160 && v_intRiderNo[i] <=280) || (v_intRiderNo[i] >300 && v_intRiderNo[i] <=380) || (v_intRiderNo[i] >390 && v_intRiderNo[i] <=430) || (v_intRiderNo[i] >450 && v_intRiderNo[i] <=530))) ){
						flagBikinPusing = 1;
					}
					
					param28.put("v_percent",percentRider[i]);
					param28.put("ins_rider",ins_rider[i]);
					param28.put("mspr_extra",mspr_extra[i]);
					if(flagBikinPusing==0){
						insert("insert.mst_product_insured_rider", param28);
					}
					
					//logger.info("insert mst product insured rider");
					//(Deddy) untuk bagian ini dipastiin dulu apakah stable link nantinya akan memakai ini juga.klo iya, di tabel utama mst_slink perlu ditambah kolom total ridernya ga.
					if(products.powerSave(Integer.toString(edit.getDatausulan().getLsbs_id()))  || 
							products.stableLink(Integer.toString(edit.getDatausulan().getLsbs_id())) || 
							products.stableSavePremiBulanan(Integer.toString(edit.getDatausulan().getLsbs_id())) || 
							products.stableSave(edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number()) ||
							products.SuperSejahtera(Integer.toString(edit.getDatausulan().getLsbs_id())) ||
							products.DanaSejahtera(Integer.toString(edit.getDatausulan().getLsbs_id())) ||
							edit.getDatausulan().getLsbs_id()==183 ||  edit.getDatausulan().getLsbs_id()==189 || edit.getDatausulan().getLsbs_id()==193){
						if(products.powerSave(Integer.toString(edit.getDatausulan().getLsbs_id()))){
							param28.put("mps_kode", 5);
							param28.put("msl_no", 0);
							param28.put("lji_id", 0);
						
						}else if(products.stableLink(Integer.toString(edit.getDatausulan().getLsbs_id()))){
							param28.put("mps_kode", 5);
							param28.put("msl_no", 1);
							param28.put("lji_id", (edit.getDatausulan().getLku_id().equals("01") ? "22" : "23" ) );
						
						}else if(products.stableSavePremiBulanan(Integer.toString(edit.getDatausulan().getLsbs_id())) || products.stableSave(edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number())){
							param28.put("mps_kode", 5);
							param28.put("msl_no", 0);
							param28.put("lji_id", 0);
						}else {
							//if(edit.getDatausulan().getLsbs_id()==183){
							param28.put("mps_kode", 0);
							param28.put("msl_no", 0);
							param28.put("lji_id", 0);
						}
						
						if(premi_tahunan[i]==null){
							premi_tahunan[i]=0.0;
						}
						//cara bayar langsung maka premi
						if(edit.getPowersave().getMpr_cara_bayar_rider() !=null){
							if(edit.getPowersave().getMpr_cara_bayar_rider()==0 || edit.getPowersave().getMpr_cara_bayar_rider()==2){
								param28.put("v_curBasePremium", premi_rider[i]);
								param28.put("v_curBasePremiumTahunan", premi_tahunan[i]);
							}else if(edit.getPowersave().getMpr_cara_bayar_rider()==1 || edit.getPowersave().getMpr_cara_bayar_rider()==3){
								param28.put("v_curBasePremium", premi_rider[i]);
								param28.put("v_curBasePremiumTahunan", premi_tahunan[i]);
							}
						}else{
							if(v_intRiderId[i]==822 && v_intRiderNo[i]==1){
								param28.put("v_curBasePremium", 0);
								param28.put("v_curBasePremiumTahunan", 0);
							}else{
								Double factor = 1.0;
								if(edit.getDatausulan().getLscb_id()==1){
									factor = 0.27;
								}else if(edit.getDatausulan().getLscb_id()==2){
									factor = 0.525;
								}else if(edit.getDatausulan().getLscb_id()==6){
									factor = 0.1;
								}premi_tahunan[i]= premi_rider[i]/factor;
								param28.put("v_curBasePremium", premi_rider[i]);
								param28.put("v_curBasePremiumTahunan", premi_tahunan[i]);
							}
						}
						param28.put("mpr_cara_bayar_rider", edit.getPowersave().getMpr_cara_bayar_rider());
						Double premi_rider2 = premi_rider[i];
						if(premi_rider2==null){
							premi_rider2=0.0;
						}
						if(edit.getPowersave().getMps_prm_interest()==null){
							edit.getPowersave().setMps_prm_interest(0.0);
						}
						Double kurang_bayar = 0.0;
						if(edit.getPowersave().getMpr_cara_bayar_rider()!=null){
								kurang_bayar = premi_rider2;
						}
						param28.put("kurang_bayar", kurang_bayar);
						
//						if(edit.getPowersave().getMpr_cara_bayar_rider()==0){
//							param28.put("mrs_premi_bayar", premi_rider[i]);
//						}else{
							param28.put("mrs_premi_bayar", 0.0);
//						}
						
//						if(kurang_bayar>=0){
//							param28.put("kurang_bayar", 0);
//							edit.getPowersave().setMps_prm_interest(kurang_bayar);
//						}else {
//							if(edit.getPowersave().getMps_prm_interest()<0){
//								kurang_bayar = 0.0 - premi_rider2;
//							}else{
//								kurang_bayar = premi_rider2- edit.getPowersave().getMps_prm_interest();
//							}
//							param28.put("kurang_bayar", kurang_bayar);
//						}
						param28.put("lus_id", edit.getCurrentUser().getLus_id());
						param28.put("tgl_input", commonDao.selectSysdateTrunc());
						if(v_intRiderId[i]==822){
							param28.put("lscb_id_rider", 9);
						}else if((v_intRiderId[i] == 813 && v_intRiderNo[i] == 5) || (v_intRiderId[i] == 818 && v_intRiderNo[i] == 4)){
							param28.put("lscb_id_rider", 0);
						}else{
							param28.put("lscb_id_rider", edit.getDatausulan().getLscb_id_rider());
						}
						insert("insert.mst_rider_save", param28);
					}
					
					if (v_intRiderId[i] == 819 || v_intRiderId[i]==820 || v_intRiderId[i]==823 || v_intRiderId[i]==825  || v_intRiderId[i]==826)
					{
						if(v_intRiderId[i]==819  || v_intRiderId[i]==826){
//							insert peserta HCP Family (utama)
							if ((flag_hcp.intValue() == 1) || (flag_rider_hcp.intValue() == 0))
							{
								Simas simas = new Simas();
								simas.setLsbs_id(v_intRiderId[i]);
								simas.setLsdbs_number(v_intRiderNo[i]);
								if(v_intRiderId[i]==819){
									if ((v_intRiderNo[i].intValue() >= 1 && v_intRiderNo[i].intValue() <= 20) || (v_intRiderNo[i].intValue() >= 141 && v_intRiderNo[i].intValue() <= 160)){
										simas.setDiscount(new Double(0));
									}else{
										simas.setDiscount(new Double(10));
									}
								}else if(v_intRiderId[i]==826){
									if ((v_intRiderNo[i].intValue() >= 1 && v_intRiderNo[i].intValue() <= 10)){
										simas.setDiscount(new Double(0));
									}else{
										simas.setDiscount(new Double(10));
									}
								}
								
								simas.setPremi(premi_rider[i]);
								
								if (status.equalsIgnoreCase("input") || (flag_rider_hcp.intValue() == 0))
								{
									proc_save_peserta(edit,strTmpSPAJ,simas,"utama");
								}
							}
						}else if(v_intRiderId[i]==820 || v_intRiderId[i]==823 || v_intRiderId[i]==825){
							Simas simas = new Simas();
							simas.setLsbs_id(v_intRiderId[i]);
							simas.setLsdbs_number(v_intRiderNo[i]);
							simas.setDiscount(new Double(0));
							simas.setPremi(premi_rider[i]);
							if(edit.getDatausulan().getLsbs_id().intValue()==183 || edit.getDatausulan().getLsbs_id().intValue()==189 || edit.getDatausulan().getLsbs_id().intValue()==193 || edit.getDatausulan().getLsbs_id().intValue()==195 || edit.getDatausulan().getLsbs_id().intValue()==204){
								if (status.equalsIgnoreCase("input")){
									proc_save_peserta(edit,strTmpSPAJ,simas,"utama");
								}
							}else{
								if(flagBikinPusing==0){
									if (status.equalsIgnoreCase("input")){
										proc_save_peserta(edit,strTmpSPAJ,simas,"utama");
									}else if (status.equalsIgnoreCase("edit")){
										proc_save_peserta(edit,strTmpSPAJ,simas,"edit");
										flag_ekasehat = 1;
									}
								}
							}
						}
						
						
					}
			}
//			if (jumlah_rider.intValue()>0)
//			{
//				for (int i =0 ; i<jumlah_rider.intValue();i++)
//				{
//					Datarider rd= (Datarider)dtrd.get(i);
//					v_intRiderId[i] = rd.getLsbs_id();
//					v_intRiderNo[i] = rd.getLsdbs_number();
//					unitRider[i] = rd.getMspr_unit();
//					classRider[i] = rd.getMspr_class();
//					insperiodRider[i] = rd.getMspr_ins_period();
//					upRider[i] =rd.getMspr_tsi();
//					end_dateRider[i] = rd.getMspr_end_date();
//					beg_dateRider[i] = rd.getMspr_beg_date();
//					end_payRider[i] = rd.getMspr_end_pay();
//					rateRider[i] =rd.getMspr_rate();
//					percentRider[i] = rd.getMspr_persen();
//					premi_rider[i] =rd.getMspr_premium();
//					if(premi_rider[i]==null || premi_rider[i].equals("")){
//						premi_rider[i]=0.0;
//					}
//					//(Deddy)
//					premi_tahunan[i] =rd.getMrs_premi_tahunan();
//					kursRider[i] = v_strKurs;
//					premi_arider[i] = rd.getMspr_tsi_pa_a();
//					premi_brider[i] = rd.getMspr_tsi_pa_b();
//					premi_drider[i] = rd.getMspr_tsi_pa_d();
//					premi_mrider[i] = rd.getMspr_tsi_pa_m();
//					ins_rider[i] = rd.getMspr_tt();
//					mspr_extra [i] = rd.getMspr_extra();
//					if (v_intRiderId[i] == 819)
//					{
//						flag_hcp = new Integer(1);
//						edit.getDatausulan().setFlag_hcp(flag_hcp);
//					}
//					
////					if(status.equals("edit")){
////						if (v_intRiderId[i] == 822){
//						if(products.powerSave(Integer.toString(edit.getDatausulan().getLsbs_id())) ||
//								products.stableLink(Integer.toString(edit.getDatausulan().getLsbs_id())) ||
//								products.stableSave(edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number())){
//							String no_endors = "";
//							if(status.equals("edit")){
//								no_endors = bacDao.selectMstEndorsGetEndorsNo(strTmpSPAJ, v_intRiderId[i], v_intRiderNo[i]);
//							}else no_endors = sequence.sequenceNo_Endorse(edit.getAgen().getLca_id());
//							
//							Endors endors = new Endors();
//							endors.setMsen_endors_no(no_endors);
//							endors.setReg_spaj(strTmpSPAJ);
//							endors.setMsen_internal(0);
//							endors.setMsen_alasan("");
//							endors.setMsen_input_date(edit.getDatausulan().getMspo_input_date());
//							endors.setMsen_endors_cost(null);
//							endors.setMsen_active_date(edit.getDatausulan().getMspo_beg_date());
//							endors.setLspd_id(edit.getDatausulan().getLspd_id());
//							//endors.setMsen_tahun_ke(1);
//							//endors.setMsen_premi_ke(i+1);
//							//endors.setMsen_prod_ke(i+1);
//							endors.setLus_id(edit.getDatausulan().getLus_id());
//							//endors.setMsen_ke(i+1);
//							if(v_intRiderId[i]==822 && v_intRiderNo[i]==1){
//								endors.setMsen_auto_rider(1);
//							}else {
//								endors.setMsen_auto_rider(0);
//							}
//							if(products.powerSave(Integer.toString(edit.getDatausulan().getLsbs_id()))){
//								endors.setFlag_ps(1);
//							}else if(products.stableLink(Integer.toString(edit.getDatausulan().getLsbs_id()))){
//								endors.setFlag_ps(2);
//							}else if(products.stableSave(edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number()) || 
//									(edit.getDatausulan().getLsbs_id()==143 && (edit.getDatausulan().getLsdbs_number()>=4 && edit.getDatausulan().getLsdbs_number()<=6)) ||
//									(edit.getDatausulan().getLsbs_id()==158 && ((edit.getDatausulan().getLsdbs_number()==13) || edit.getDatausulan().getLsdbs_number()>=15 && edit.getDatausulan().getLsdbs_number()<=16)) ||
//									(edit.getDatausulan().getLsbs_id()==144 && (edit.getDatausulan().getLsdbs_number()==4))){
//								endors.setFlag_ps(3);
//							}else endors.setFlag_ps(0);
//							
//							
//							DetEndors detEndors=new DetEndors();
//							detEndors.setMsen_endors_no(endors.getMsen_endors_no());
//							detEndors.setMsenf_number(1);
//							detEndors.setLsje_id(49);//PERUBAHAN RIDER lst_jn_endors
//							detEndors.setMste_insured_no(1);
//							if(edit.getDatausulan().getLscb_pay_mode()==null){
//								String lscb_pay_mode = "";
//								if(edit.getDatausulan().getLscb_id() == 1){
//									lscb_pay_mode = "Triwulanan";
//								}else if(edit.getDatausulan().getLscb_id() == 2){
//									lscb_pay_mode = "Semesteran";
//								}else if(edit.getDatausulan().getLscb_id() == 3){
//									lscb_pay_mode = "Tahunan";
//								}else if(edit.getDatausulan().getLscb_id() == 6){
//									lscb_pay_mode = "Bulanan";
//								}else if(edit.getDatausulan().getLscb_id() == 0){
//									lscb_pay_mode = "Sekaligus";
//								}
//								edit.getDatausulan().setLscb_pay_mode(lscb_pay_mode);
//							}
//							if(edit.getDatausulan().getLku_symbol()==null){
//								String lku_symbol = "";
//								if(edit.getDatausulan().getLku_id().equals("01")){
//									lku_symbol = "Rp.";
//								}else if(edit.getDatausulan().getLku_id().equals("02")){
//									lku_symbol = "US$";
//								}
//								edit.getDatausulan().setLku_symbol(lku_symbol);
//							}
//							if(edit.getDatausulan().getLsdbs_name()==null){
//								String lsdbs_name = uwDao.selectNamaBusiness(edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number());
//								edit.getDatausulan().setLsdbs_name(lsdbs_name);
//							}
//							
//							detEndors.setMsde_old1(edit.getDatausulan().getLscb_pay_mode());
//							detEndors.setMsde_old2(edit.getDatausulan().getLsdbs_name());
//							detEndors.setMsde_old3(edit.getDatausulan().getLku_symbol());
//							detEndors.setMsde_old4(FormatString.formatCurrency("", new BigDecimal(edit.getDatausulan().getMspr_premium())));
//							detEndors.setMsde_old5(FormatString.formatCurrency("", new BigDecimal(edit.getDatausulan().getMspr_tsi())));
//	//						detEndors.setMsde_old6(msde_old1);
//							detEndors.setMsde_new1(edit.getDatausulan().getLscb_pay_mode());
//							detEndors.setMsde_new2(edit.getDatausulan().getLsdbs_name());
//							detEndors.setMsde_new3(edit.getDatausulan().getLku_symbol());
//							detEndors.setMsde_new4(FormatString.formatCurrency("", new BigDecimal(edit.getDatausulan().getMspr_premium())));
//							detEndors.setMsde_new5(FormatString.formatCurrency("", new BigDecimal(edit.getDatausulan().getMspr_tsi())));
//							
//							
//							ProductInsEnd prod=new ProductInsEnd();
//							prod.setMsen_endors_no(endors.getMsen_endors_no());
//							prod.setReg_spaj(strTmpSPAJ);
//							prod.setMste_insured_no(1);
//							prod.setLsbs_id(v_intRiderId[i]);
//							prod.setLsdbs_number(v_intRiderNo[i]);
//							prod.setLku_id(edit.getDatausulan().getLku_id());
//							prod.setMspie_beg_date(edit.getDatausulan().getMste_beg_date());
//							prod.setMspie_end_date(FormatDate.add(FormatDate.add(prod.getMspie_beg_date(), Calendar.MONTH, 12),Calendar.DATE,-1));
//							Double UP=0.0;
//							if(edit.getDatausulan().getLku_id().equals("01")) {
//								UP=new Double (20000000);
//							}else if(edit.getDatausulan().getLku_id().equals("02")) {
//								UP=new Double (2000);
//							}
//							prod.setMspie_tsi(UP);
//							prod.setMspie_tsi_a(new Double(0));
//							prod.setMspie_tsi_b(new Double(0));
//							prod.setMspie_tsi_c(new Double(0));
//							prod.setMspie_tsi_d(new Double(0));
//							prod.setMspie_tsi_m(new Double(0));
//							prod.setMspie_class(edit.getDatausulan().getMspr_class());
//							prod.setMspie_unit(edit.getDatausulan().getMspr_unit());
//							prod.setMspie_rate(new Double(0));
//							prod.setMspie_persen(0);
//							prod.setMspie_premium(new Double(0));
//							prod.setMspie_discount(new Double(0));
//							prod.setMspie_extra(edit.getDatausulan().getMspr_extra());
//							prod.setMspie_ins_period(1);
//							prod.setLus_id(edit.getDatausulan().getLus_id());
//							prod.setLscb_id(edit.getPemegang().getLscb_id());
//							prod.setLst_lsbs_id(null);
//							prod.setLst_lsdbs_number(null);
//							prod.setMspie_tsi_old(null);
//							prod.setMspie_premium_old(null);
//							prod.setMspie_disc_old(new Double(0));
//							prod.setLsbs_id_old(null);
//							prod.setLsdbs_num_old(null);
//							prod.setMspie_rate_old(null);
//							prod.setLscb_id_old(null);
//							prod.setMspie_premium_prod(null);
//							prod.setMspie_discount_prod(null);
//							
//							if(status.equals("edit")){
//								if(no_endors==null){
//									no_endors = sequence.sequenceNo_Endorse(edit.getAgen().getLca_id());
//									endors.setMsen_endors_no(no_endors);
//									detEndors.setMsen_endors_no(no_endors);
//									prod.setMsen_endors_no(no_endors);
//									insert("insertMstEndors", endors);
//									insert("insertMstDetEndors", detEndors);
//									bacDao.insertMstProductInsEndAllProdLama(endors.getMsen_endors_no(), strTmpSPAJ, edit.getDatausulan().getLus_id(), edit.getPemegang().getLscb_id(), edit.getDatausulan().getMspr_tsi(), edit.getDatausulan().getMspr_premium(), edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number());
//									
//								}else {
//									update("updateMstEndors", endors);
//									update("updateMstDetEndors", detEndors);
//									delete("delete.MstProductInsEnd", endors.getMsen_endors_no());
//									bacDao.insertMstProductInsEndAllProdLama(endors.getMsen_endors_no(), strTmpSPAJ, edit.getDatausulan().getLus_id(), edit.getPemegang().getLscb_id(), edit.getDatausulan().getMspr_tsi(), edit.getDatausulan().getMspr_premium(), edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number());
//								}
//							//update("updateMstProductInsEnd", prod);
//							}else {
//							insert("insertMstEndors", endors);
//							insert("insertMstDetEndors", detEndors);
//							bacDao.insertMstProductInsEndAllProdLama(endors.getMsen_endors_no(), strTmpSPAJ, edit.getDatausulan().getLus_id(), edit.getPemegang().getLscb_id(), edit.getDatausulan().getMspr_tsi(), edit.getDatausulan().getMspr_premium(), edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number());
//							//insert("insertMstProductInsEnd", prod);
//							}
//						}
////					}
//				}
//			}
			
			if(edit.getDatausulan().getLsbs_id()!=183 && edit.getDatausulan().getLsbs_id()!=189 && edit.getDatausulan().getLsbs_id()!=193 && edit.getDatausulan().getLsbs_id()!=195 && edit.getDatausulan().getLsbs_id()!=204 && flag_ekasehat!=1){
				if (status.equalsIgnoreCase("edit") && (flag_rider_hcp.intValue() == 1))
				{
					Simas simas = new Simas();
					proc_save_peserta(edit,strTmpSPAJ,simas,"edit");
				}
			}
		}
		
		private void proc_save_product_insured(Cmdeditbac edit, String strTmpSPAJ,Integer v_intActionBy ,Integer flag_jenis_plan, Date ldt_endpay1, User currentUser)throws ServletException,IOException
		{
			Integer intClass =null;
			Integer v_intClass = edit.getDatausulan().getMspr_class();
			Integer v_intBaseBusinessId = edit.getDatausulan().getLsbs_id();
			Integer v_intBaseBusinessNo = edit.getDatausulan().getLsdbs_number();
			Double v_curBasePremium = edit.getDatausulan().getMspr_premium();
			Date v_strBeginDate = edit.getDatausulan().getMste_beg_date();
			String v_strKurs = edit.getDatausulan().getKurs_p().toUpperCase();
			Date v_strEndDate = edit.getDatausulan().getMste_end_date();
			Double v_intBaseRate = edit.getDatausulan().getRate_plan();	
			Integer v_intInsPeriod = edit.getDatausulan().getMspr_ins_period();
			Double v_curUP = edit.getDatausulan().getMspr_tsi();
			intClass = v_intClass;
			Double disc = edit.getDatausulan().getMspr_discount();
			if(disc==null){
				disc=0.;
			}

			//request dr. ingrid - untuk semua produk powersave, apabila usia tertanggung >= 69, maka UP = 0.5 PREMI, dengan nilai max Rp. 100 jt / $10.000 (Yusuf - 11/03/2008)
			if(products.powerSave(FormatString.rpad("0", v_intBaseBusinessId.toString(), 3)) && edit.getTertanggung().getMste_age().intValue() >= 69) {
				v_curUP = 0.5 * v_curBasePremium;
				if(v_strKurs.equals("01") && v_curUP.intValue() >= 100000000) {
					v_curUP = (double) 100000000;
				}else if(v_strKurs.equals("02") && v_curUP.intValue() >= 10000) {
					v_curUP = (double) 10000;
				}
			}
//			------------------------------------------------------------
			// Insert Basic Plan information to MST_PRODUCT_INSURED
			if (flag_jenis_plan.intValue()==4)
			{
				intClass = v_intClass;
				Map param28=new HashMap();
				param28.put("strTmpSPAJ",strTmpSPAJ);	
				param28.put("v_intBaseBusinessId",v_intBaseBusinessId);
				param28.put("v_intBaseBusinessNo",v_intBaseBusinessNo);
				param28.put("v_strKurs",v_strKurs);
				param28.put("v_strBeginDate",v_strBeginDate);
				param28.put("v_strEndDate",v_strEndDate);
				param28.put("intClass",intClass);
				param28.put("v_intBaseRate",v_intBaseRate);
				param28.put("v_curBasePremium",v_curBasePremium);
				param28.put("v_intInsPeriod",v_intInsPeriod);
				param28.put("v_curUP",v_curUP);
				param28.put("mspr_discount",disc);
				insert("insert.mst_product_insured45", param28);
				//logger.info("insert mst product insured");
			}else if (flag_jenis_plan.intValue() == 5)
					{
						v_intClass = new Integer(1);
						Double v_curUP_A = new Double(0);
						Double v_curUP_B = new Double(0);
						Double v_curUP_D = new Double(0);
						if (v_intBaseBusinessNo.intValue() == 2)
						{
							if(FormatString.rpad("0", v_intBaseBusinessId.toString(), 3).equals("134") || FormatString.rpad("0", v_intBaseBusinessId.toString(), 3).equals("166")){
								v_curUP_A = null;
							}else{
								v_curUP_A = v_curUP;
							}
						}else if (v_intBaseBusinessNo.intValue() == 3)
							{
							if(FormatString.rpad("0", v_intBaseBusinessId.toString(), 3).equals("134") || FormatString.rpad("0", v_intBaseBusinessId.toString(), 3).equals("166")){
								v_curUP_A = null;
								v_curUP_B = null;
							}else{
								v_curUP_A = v_curUP;
								v_curUP_B = v_curUP;
							}
							}else{
								
								if(FormatString.rpad("0", v_intBaseBusinessId.toString(), 3).equals("134") || FormatString.rpad("0", v_intBaseBusinessId.toString(), 3).equals("166")){
									v_curUP_A = null;
									v_curUP_B = null;
									v_curUP_D = null;
								}else{
									v_curUP_A = v_curUP;
									v_curUP_B = v_curUP;
									v_curUP_D = v_curUP;
								}
								
							}
						intClass = v_intClass;
						Map param28=new HashMap();
						param28.put("strTmpSPAJ",strTmpSPAJ);	
						param28.put("v_intBaseBusinessId",v_intBaseBusinessId);
						param28.put("v_intBaseBusinessNo",v_intBaseBusinessNo);
						param28.put("v_strKurs",v_strKurs);
						param28.put("v_strBeginDate",v_strBeginDate);
						param28.put("v_strEndDate",v_strEndDate);
						param28.put("intClass",intClass);
						param28.put("v_intBaseRate",v_intBaseRate);
						param28.put("v_curBasePremium",v_curBasePremium);
						param28.put("v_intInsPeriod",v_intInsPeriod);
						param28.put("v_curUP",v_curUP);
						param28.put("v_curUP_A",v_curUP_A);
						param28.put("v_curUP_B",v_curUP_B);
						param28.put("v_curUP_D",v_curUP_D);
						param28.put("mspr_discount",disc);
						insert("insert.mst_product_insuredPA", param28);
						//logger.info("insert mst product insured");
					}else{
						Map param28=new HashMap();
						param28.put("strTmpSPAJ",strTmpSPAJ);	
						param28.put("v_intBaseBusinessId",v_intBaseBusinessId);
						param28.put("v_intBaseBusinessNo",v_intBaseBusinessNo);
						param28.put("v_strKurs",v_strKurs);
						param28.put("v_strBeginDate",v_strBeginDate);
						param28.put("v_strEndDate",v_strEndDate);
						param28.put("v_intBaseRate",v_intBaseRate);
						param28.put("v_curBasePremium",v_curBasePremium);
						param28.put("v_intInsPeriod",v_intInsPeriod);
						param28.put("v_curUP",v_curUP);
						param28.put("ldt_endpay",ldt_endpay1);
						param28.put("mspr_discount",disc);
						insert("insert.mst_product_insured1", param28);
						//logger.info("insert mst product insured");
						/*if (v_intBaseBusinessId.intValue()==134)
						{
							if (ldt_endpay1.getYear() != v_strEndDate.getYear())
							{
								email.send(
										new String[] {props.getProperty("admin.yusuf")}, null,
										"PLATINUM LINK SALAH END PAY DATE NYA CEK LAGI, end pay tahunnya tidak sama dengan end date polis dengan spaj no " + strTmpSPAJ + " oleh "+ currentUser.getName() + " ["+currentUser.getDept()+"]",
										strTmpSPAJ, currentUser);
								logger.info("PLATINUM LINK SALAH END PAY DATE NYA CEK LAGI, end pay tahunnya tidak sama dengan end date polis");
								throw new RuntimeException("Tanggal End Pay tidak sama dengan tanggal End Date Polis.");
							}
						}*/
					}
		}
		
		
		private void proc_save_mst_ulink(Cmdeditbac edit, String strTmpSPAJ,Integer v_intActionBy ,Date v_tglsurat,Integer mu_ke, Integer lt_id_tunggal,Integer lt_id_berkala , Double v_premiexcell , Double v_jmlhtopup_berkala, Double v_jmlhtopup_tunggal , Integer v_topup_berkala, Integer v_topup_tunggal , Date tgl_trans, String keterangan)throws ServletException,IOException
		{
			Integer lt_id = new Integer(1);
			Integer v_topup = new Integer(0);
			Double v_jmlhtopup =new Double(0);
			Integer bulan = new Integer(6);
			if (keterangan.equalsIgnoreCase("tunggal"))
			{
				lt_id  = lt_id_tunggal;
				v_topup = v_topup_tunggal;
				v_jmlhtopup = v_jmlhtopup_tunggal;
			}
			if (keterangan.equalsIgnoreCase("berkala"))
			{
				lt_id  = lt_id_berkala;
				v_topup = v_topup_berkala;
				v_jmlhtopup = v_jmlhtopup_berkala;
			}
			if (keterangan.equalsIgnoreCase(""))
			{
				if (v_topup_berkala > 0)
				{
					lt_id  = new Integer(1);
					v_topup = v_topup_berkala;
					v_jmlhtopup = v_jmlhtopup_berkala;
				}
				if (v_topup_tunggal > 0)
				{
					if (v_topup_berkala == 0)
					{
						lt_id  = new Integer(1);
						v_topup = v_topup_tunggal;
						v_jmlhtopup = v_jmlhtopup_tunggal;
					}
				}
			}
			if (mu_ke.intValue() == 3)
			{
				bulan = null;
			}
			edit.getInvestasiutama().setReg_spaj(strTmpSPAJ);
			edit.getInvestasiutama().setMu_ke(mu_ke);
			edit.getInvestasiutama().setMu_jlh_premi(v_premiexcell);
			edit.getInvestasiutama().setMu_tgl_surat(v_tglsurat);
			edit.getInvestasiutama().setMu_bulan_surat(bulan);
			edit.getInvestasiutama().setLt_id(lt_id);
			edit.getInvestasiutama().setMu_periodic_tu(v_topup);
			edit.getInvestasiutama().setMu_jlh_tu(v_jmlhtopup);
			edit.getInvestasiutama().setMu_tgl_trans(tgl_trans);
			int rowupdated=update("update_mst_ulink",edit.getInvestasiutama());
			if (rowupdated ==0)
			{
				insert("insert.mst_ulink", edit.getInvestasiutama());
			}
		}
		
		private void proc_save_det_ulink(Cmdeditbac edit, String strTmpSPAJ,Integer v_intActionBy ,Integer value,Integer persen, Double jumlah , Integer mu_ke , String id ,  Date tgl_trans)throws ServletException,IOException
		{
//			Excellink Secure $	
			Map param=new HashMap();
			param.put("strTmpSPAJ",strTmpSPAJ);
			param.put("v_fixedvalue",value);
			param.put("jmlhfixed",jumlah);
			param.put("mu_ke",mu_ke);
			param.put("v_persen_tu",persen);
			param.put("v_last_trans",tgl_trans);
			param.put("nilai",id);
			insert("insert.fixed", param);
		}
		
		private void proc_save_worksite_flag(Cmdeditbac edit, String strTmpSPAJ)throws ServletException,IOException
		{
			Integer flag_worksite = edit.getDatausulan().getFlag_worksite();
			if (flag_worksite.intValue() == 1)
			{
				edit.getPemegang().setReg_spaj(strTmpSPAJ);
				int rowupdate = update("update_mst_worksite_flag",edit.getPemegang());
				if (rowupdate == 0)
				{
					insert("insert_mst_worksite_flag",edit.getPemegang());
				}	
			}
		}

		private void proc_save_peserta(Cmdeditbac edit, String strTmpSPAJ,Simas simas,String keterangan)throws ServletException,IOException
		{
			Integer flag_rider_hcp = edit.getDatausulan().getFlag_rider_hcp();
			if (flag_rider_hcp == null)
			{
				flag_rider_hcp = new Integer(0);
			}
			Double diskon_karyawan = 1.0;
			if(edit.getDatausulan().getFlag_as()==2){
				diskon_karyawan = 0.7;
			}
			if(keterangan.equalsIgnoreCase("utama") || (flag_rider_hcp.intValue() == 0))
			{
				
				simas.setReg_spaj(strTmpSPAJ);
				simas.setKelamin(edit.getTertanggung().getMspe_sex());
				simas.setLsre_id(edit.getPemegang().getLsre_id());
				simas.setNama(edit.getTertanggung().getMcl_first().toUpperCase());
				simas.setNo_urut(new Integer(1));
				simas.setNo_reg("1a");
				simas.setFlag_val_send(new Integer(1));
				simas.setFlag_admedika(new Integer(0));
				simas.setNext_send(edit.getDatausulan().getMste_beg_date());
				
				
				if (edit.getDatausulan().getLsbs_id().intValue() == 161 || edit.getDatausulan().getLsbs_id().intValue() == 183 || edit.getDatausulan().getLsbs_id().intValue() == 189 || edit.getDatausulan().getLsbs_id().intValue() == 193 || edit.getDatausulan().getLsbs_id().intValue() == 195 || edit.getDatausulan().getLsbs_id().intValue() == 204)
				{
					simas.setPremi(edit.getDatausulan().getMspr_premium() * diskon_karyawan.doubleValue());
				}
				simas.setTgl_lahir(edit.getTertanggung().getMspe_date_birth());
				simas.setUmur(edit.getTertanggung().getMste_age());
				if (simas.getLsbs_id().intValue() == 819 && ((simas.getLsdbs_number().intValue() >= 1 && simas.getLsdbs_number().intValue() <= 20) || (simas.getLsdbs_number().intValue() >= 141 && simas.getLsdbs_number().intValue() <= 160) || (simas.getLsdbs_number().intValue() >= 281 && simas.getLsdbs_number().intValue() <= 300) || (simas.getLsdbs_number().intValue() >= 381 && simas.getLsdbs_number().intValue() <= 390) || (simas.getLsdbs_number().intValue() >= 431 && simas.getLsdbs_number().intValue() <= 450)))
				{
					simas.setDiscount(new Double(0));
				}else if(simas.getLsbs_id().intValue()==183 || simas.getLsbs_id().intValue()==189 || simas.getLsbs_id().intValue()==193 || ((simas.getLsbs_id().intValue()==820 || simas.getLsbs_id().intValue()==823 || simas.getLsbs_id().intValue()==825) && (simas.getLsdbs_number().intValue()<=15)) || 
						(simas.getLsbs_id().intValue()==195 || (simas.getLsbs_id().intValue()==826 && simas.getLsdbs_number().intValue()<=10)) ||
						(simas.getLsbs_id().intValue()==204 || (simas.getLsbs_id().intValue()==826 && simas.getLsdbs_number().intValue()<=10)) ){
					simas.setDiscount(new Double(0));
				}
				Integer no_urut = (Integer) this.bacDao.selectmax_peserta(edit.getPemegang().getReg_spaj());
				if (no_urut==null)
				{
					no_urut = new Integer(0);
				}
				if (no_urut.intValue() == 0)
				{
					this.bacDao.insert_mst_peserta(simas);
				}else{
					no_urut = new Integer(1);
					this.bacDao.update_mst_peserta(simas);
				}
				if (edit.getDatausulan().getLsbs_id().intValue() == 161)
				{
					this.bacDao.update_mst_peserta1(strTmpSPAJ);
				}
			}else if (keterangan.equalsIgnoreCase("edit") )
				{
					List peserta = edit.getDatausulan().getDaftapeserta();
					for (int i = 0 ; i < peserta.size() ; i++)
					{
						Simas simas1 = (Simas) peserta.get(i);
						simas1.setFlag_val_send(new Integer(1));
						simas1.setFlag_admedika(new Integer(0));
						simas1.setNext_send(edit.getDatausulan().getMste_beg_date());
						if((simas1.getLsbs_id().intValue() == 820 || simas1.getLsbs_id().intValue() == 823) && (simas1.getLsdbs_number().intValue()<=15 || (simas1.getLsdbs_number().intValue()>90 && simas1.getLsdbs_number().intValue()<=105))){
							simas1.setNo_reg("1a");
						}
						if (simas1.getLsbs_id().intValue() == 819 && ((simas1.getLsdbs_number().intValue() >= 1 && simas1.getLsdbs_number().intValue() <= 20) || (simas1.getLsdbs_number().intValue() >= 141 && simas1.getLsdbs_number().intValue() <= 160) || (simas1.getLsdbs_number().intValue() >= 281 && simas1.getLsdbs_number().intValue() <= 300) || (simas1.getLsdbs_number().intValue() >= 381 && simas1.getLsdbs_number().intValue() <= 390)))
						{
							simas1.setDiscount(new Double(0));
						}else if(simas1.getLsbs_id().intValue()==183 || simas1.getLsbs_id().intValue()==189 ||simas1.getLsbs_id().intValue()==193 || ((simas1.getLsbs_id().intValue()==820 || simas1.getLsbs_id().intValue()==823 || simas1.getLsbs_id().intValue()==825) && (simas1.getLsdbs_number().intValue()<=15 || (simas1.getLsdbs_number().intValue()>90 && simas1.getLsdbs_number().intValue()<=105))) || (simas1.getLsbs_id().intValue()==178 && (simas1.getLsdbs_number().intValue()<=16)) ||
								(simas1.getLsbs_id().intValue()==195 || (simas1.getLsbs_id().intValue()==826 && simas1.getLsdbs_number().intValue()<=10)) ||
								(simas1.getLsbs_id().intValue()==204 || (simas1.getLsbs_id().intValue()==826 && simas1.getLsdbs_number().intValue()<=10)) ){
							simas.setDiscount(new Double(0));
						} 
						if(i>=1){
							if(simas1.getLsbs_id().intValue()==183 || simas1.getLsbs_id().intValue()==189 || simas1.getLsbs_id().intValue()==193 || simas1.getLsbs_id().intValue()==195 || simas1.getLsbs_id().intValue()==204){
								Date LimaBelasJuniDuaRibuSepuluh=null;
								try {
									LimaBelasJuniDuaRibuSepuluh = defaultDateFormat.parse("15/6/2010");
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									logger.error("ERROR :", e);
								}
								
//								if(LimaBelasJuniDuaRibuSepuluh.before(edit.getDatausulan().getMste_beg_date())){
//									simas1.setLsbs_id(820);
//								}else{
//									simas1.setLsbs_id(823);
//								}
								int penambah=15*i;
								if(simas1.getLsbs_id().intValue()==195 || simas1.getLsbs_id().intValue()==204){
									penambah=10*i;
								}
								if(i==1){
									simas1.setLsdbs_number(simas1.getLsdbs_number()+penambah);
									if(edit.getDatausulan().getMspo_provider()==2){
										simas1.setNo_reg("1b");
									}
								}else if(i==2){
									simas1.setLsdbs_number(simas1.getLsdbs_number()+penambah);
									if(edit.getDatausulan().getMspo_provider()==2){
										simas1.setNo_reg("1c");
									}
								}else if(i==3){
									simas1.setLsdbs_number(simas1.getLsdbs_number()+penambah);
									if(edit.getDatausulan().getMspo_provider()==2){
										simas1.setNo_reg("1d");
									}
								}else if(i==4){
									simas1.setLsdbs_number(simas1.getLsdbs_number()+penambah);
									if(edit.getDatausulan().getMspo_provider()==2){
										simas1.setNo_reg("1e");
									}
								}
								
							}
						}
						if(i==0){
							if(simas1.getLsbs_id().intValue()==823 || simas1.getLsbs_id().intValue()==819 || simas1.getLsbs_id().intValue()==825 || simas1.getLsbs_id().intValue()==826){
								this.bacDao.insert_mst_peserta(simas1);
							}else{
								this.bacDao.update_mst_peserta(simas1);
							}
//							this.bacDao.insert_mst_peserta(simas1);
							
						}else{
							this.bacDao.insert_mst_peserta(simas1);
						}
						if(simas1.getLsbs_id().intValue()!=820 && simas1.getLsbs_id().intValue()!=819){
							Date SebelasBelasOktoberDuaRibuSembilan=null;
							try {
								SebelasBelasOktoberDuaRibuSembilan = defaultDateFormat.parse("11/10/2009");
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								logger.error("ERROR :", e);
							}
							Date SatuJanuariDuaRibuSepuluh=null;
							try {
								SatuJanuariDuaRibuSepuluh = defaultDateFormat.parse("1/1/2010");
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								logger.error("ERROR :", e);
							}
							if(edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh)){
								simas1.setLsbs_id(822);
								simas1.setLsdbs_number(1);
								simas1.setPremi(0.0);
								Integer no_urut = (Integer) this.bacDao.selectmax_peserta(edit.getPemegang().getReg_spaj());
								simas1.setNo_urut(no_urut+1);
								this.bacDao.insert_mst_peserta(simas1);
							}
						}
						
					}
					if (edit.getDatausulan().getLsbs_id().intValue() == 161)
					{
						this.bacDao.update_mst_peserta1(strTmpSPAJ);
					}
				}
		}
		
		public  Cmdeditbac save_peserta(Object cmd, String keterangan, User User) throws ServletException,IOException
		{
			Cmdeditbac edit= (Cmdeditbac) cmd;
			try {
				this.bacDao.delete_mst_peserta(edit.getPemegang().getReg_spaj());
				Integer jmlh_peserta = edit.getDatausulan().getJml_peserta();
				if (jmlh_peserta == null)
				{
					jmlh_peserta = 0;
				}
				if (jmlh_peserta.intValue() > 1)
				{
					List peserta = edit.getDatausulan().getDaftapeserta();
					for (int k=1 ; k < (jmlh_peserta.intValue()) ; k++)
					{
						Simas bf1= (Simas)peserta.get(k);
						bf1.setNext_send(edit.getDatausulan().getMste_beg_date());
						if (bf1.getLsbs_id().intValue() == 819 && ((bf1.getLsdbs_number().intValue() >= 1 && bf1.getLsdbs_number().intValue() <= 20) || (bf1.getLsdbs_number().intValue() >= 141 && bf1.getLsdbs_number().intValue() <= 160) || (bf1.getLsdbs_number().intValue() >= 281 && bf1.getLsdbs_number().intValue() <= 300) || (bf1.getLsdbs_number().intValue() >= 381 && bf1.getLsdbs_number().intValue() <= 390)))
						{
							bf1.setDiscount(new Double(0));
						}
						this.bacDao.insert_mst_peserta(bf1);
					}
				}
				this.bacDao.update_mst_peserta1(edit.getDatausulan().getReg_spaj());

				edit.getDatausulan().setStatus_submit("berhasil");
				
			} catch (Exception e) {
				edit.getDatausulan().setStatus_submit("gagal");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				logger.error("ERROR :", e);
			}

			return edit;
			
		}
		
		public Cmdeditbac save_hcp(Object cmd, String keterangan, User User) throws ServletException,IOException
		{
			Cmdeditbac edit= (Cmdeditbac) cmd;
			try {
				// hapus semua row kecuali row 1
				this.bacDao.delete_mst_peserta(edit.getPemegang().getReg_spaj());
				Map param = new HashMap();
				param.put("strTmpSPAJ", edit.getPemegang().getReg_spaj());
				List rider = edit.getDatausulan().getDaftariderhcp();
				Datarider datarider = (Datarider) rider.get(0);
				if(edit.getDatausulan().getLsbs_id().intValue()==183 || edit.getDatausulan().getLsbs_id().intValue()==189 || edit.getDatausulan().getLsbs_id().intValue()==193 || edit.getDatausulan().getLsbs_id().intValue()==195 || edit.getDatausulan().getLsbs_id().intValue()==204 || products.multiInvest(edit.getDatausulan().getLsbs_id().toString()) || 
					edit.getDatausulan().getLsbs_id().intValue()==820 || datarider.getLsbs_id().intValue()==820 || edit.getDatausulan().getLsbs_id().intValue()==823 || datarider.getLsbs_id().intValue()==823 || edit.getDatausulan().getLsbs_id().intValue()==825 || datarider.getLsbs_id().intValue()==825 ||
					edit.getDatausulan().getLsbs_id().intValue()==826 || datarider.getLsbs_id().intValue()==826){
//					if(edit.getDatausulan().getLsbs_id().intValue()==183 || edit.getDatausulan().getLsbs_id().intValue()==823 || datarider.getLsbs_id().intValue()==823){
//						param.put("kode", 823);
//					}else{
						param.put("kode", 820);
//					}
//					param.put("number1", 21);
//					param.put("number2", 140);
//					param.put("number1", 16);
//					param.put("number2", 90);
					param.put("number1", 106);
					param.put("number2", 195);
					if(edit.getDatausulan().getLsbs_id().intValue()==193|| datarider.getLsbs_id().intValue()==823 || datarider.getLsbs_id().intValue()==825){
						if (datarider.getLsbs_id().intValue()==823){
							param.put("kode", 823);
						}else{
							param.put("kode", 825);
						}
						param.put("number1", 16);
						param.put("number2", 90);
					}else if(edit.getDatausulan().getLsbs_id().intValue()==195|| datarider.getLsbs_id().intValue()==826 || datarider.getLsbs_id().intValue()==826){
						param.put("kode", 826);
						param.put("number1", 11);
						param.put("number2", 70);
					}
				}else if(edit.getDatausulan().getLsbs_id().intValue()==178 || datarider.getLsbs_id().intValue()==821){
					param.put("kode", 821);
					param.put("number1", 1);
					param.put("number2", 16);
				}else if(edit.getDatausulan().getLsbs_id().intValue()==195 || datarider.getLsbs_id().intValue()==826 ){
					param.put("kode", 826);
//					param.put("number1", 21);
//					param.put("number2", 140);
					param.put("number1", 11);
					param.put("number2", 70);
				}
				else{
				param.put("kode", 819);
//				param.put("number1", 21);
//				param.put("number2", 140);
				param.put("number1", 161);
				param.put("number2", 280);
				}
				param.put("kodeswineflu", 822);
				param.put("number1swineflu", 3);
				param.put("number2swineflu", 7);
				
				delete("delete.mst_product_insured_swineflu", param);
				delete("delete.mst_product_insured_hcp", param);
				if(edit.getDatausulan().getLsbs_id().intValue()==183 || edit.getDatausulan().getLsbs_id().intValue()==189 || products.multiInvest(edit.getDatausulan().getLsbs_id().toString()) || edit.getDatausulan().getLsbs_id().intValue()==820 || datarider.getLsbs_id().intValue()==820 || edit.getDatausulan().getLsbs_id().intValue()==823 || datarider.getLsbs_id().intValue()==823){
					param.put("kode", 823);
					param.put("number1", 16);
					param.put("number2", 105);
					delete("delete.mst_product_insured_hcp", param);
				}
				delete("delete.mst_rider_save_ekasehatswineflu",param);
				delete("delete.mst_rider_save",param);
				
				//delete mst_product_insured hcpf selain basic

	
				List hcp = edit.getDatausulan().getDaftahcp();
				List simas = edit.getDatausulan().getDaftapeserta();
				if(edit.getDatausulan().getLsbs_id().intValue()==819 || edit.getDatausulan().getLsbs_id().intValue()==826){
					rider = edit.getDatausulan().getDaftariderhcp();
					//untuk Eka Sehat
				}else if(edit.getDatausulan().getLsbs_id().intValue()==183 || edit.getDatausulan().getLsbs_id().intValue()==189 || edit.getDatausulan().getLsbs_id().intValue()==193 || products.multiInvest(edit.getDatausulan().getLsbs_id().toString()) || edit.getDatausulan().getLsbs_id().intValue()==820 || edit.getDatausulan().getLsbs_id().intValue()==823 || edit.getDatausulan().getLsbs_id().intValue()==825 || edit.getDatausulan().getLsbs_id().intValue()==178 || edit.getDatausulan().getLsbs_id().intValue()==821){
					rider = edit.getDatausulan().getDaftaRider();
				}
				
				Integer jmlh_peserta = hcp.size();
				if (jmlh_peserta == null)
				{
					jmlh_peserta = 0;
				}
				if (jmlh_peserta.intValue() > 0)
				{
					Map param35 = new HashMap();

					String strTmpSPAJ = edit.getPemegang().getReg_spaj();
					
					param35.put("strTmpSPAJ",strTmpSPAJ);
					List<Biayainvestasi> daftarbiaya =bacDao.selectdetilinvbiaya(edit.getPemegang().getReg_spaj());
					for(int i=0;i<daftarbiaya.size();i++){
						Biayainvestasi db =   daftarbiaya.get(i);
						Integer ljb_id = (Integer) db.getLjb_id();
						if((ljb_id>=375 && ljb_id<=434) || (ljb_id>=443 && ljb_id <=448)){
							daftarbiaya.remove(db);
							i--;
						}
					}
					
					//TODO(DEDDY):HAPUS BIAYA ULINK UNTUK EKA SEHAT SERTA BIAYA ADMIN ADMEDIKANYA APABILA ADA. 
//					if(daftarbiaya.size()!=0){
//						for(int i=0;i<simas.size();i++){
//							Biayainvestasi bi = (Biayainvestasi) daftarbiaya.get(i);
//							if(bi.getLjb_id()==1 || bi.getLjb_id()==2 || bi.getLjb_id()==3){
//								
//							}else{
//								
//							}
//						}
//						edit.getInvestasiutama().setDaftarbiaya(daftarbiaya);
//					}
					
					delete("delete.mst_biaya_ulink", param35);	
					//logger.info("delete.mst_biaya_ulink");	
								
					delete("delete.mst_det_ulink", param35);	
					//logger.info("delete.mst_det_ulink");	
					
					Hcp hcp2= (Hcp)hcp.get(0);
					this.bacDao.update_mst_peserta_lspc_no(strTmpSPAJ, hcp2.getLspc_no());
					
					
					for (int k=0 ; k < (jmlh_peserta.intValue()) ; k++)
					{
						Hcp hcp1= (Hcp)hcp.get(k);
						Simas bf1= (Simas)simas.get(k);
						Datarider bf2  = (Datarider) rider.get(k);
						Double diskon_karyawan =1.0;
						if(edit.getDatausulan().getFlag_as()==2){
							diskon_karyawan = 0.7;// karyawan mendapat diskon premi 30% untuk produk eka sehat (diskon 2.5% didapat lagi untuk tambahan)
						}
						
						if (bf1.getLsbs_id().intValue() == 819 && ((bf1.getLsdbs_number().intValue() >= 1 && bf1.getLsdbs_number().intValue() <= 20) || (bf1.getLsdbs_number().intValue() >= 141 && bf1.getLsdbs_number().intValue() <= 160) || (bf1.getLsdbs_number().intValue() >= 281 && bf1.getLsdbs_number().intValue() <= 300) || (bf1.getLsdbs_number().intValue() >= 381 && bf1.getLsdbs_number().intValue() <= 390)))
						{
							bf1.setDiscount(new Double(0));
						}else if(bf1.getLsbs_id().intValue() == 820 || bf1.getLsbs_id().intValue() == 823 || bf1.getLsbs_id().intValue() == 825){
							if((bf1.getLsdbs_number().intValue()>=1 && bf1.getLsdbs_number().intValue()<=15) || (bf1.getLsdbs_number().intValue()>=91 && bf1.getLsdbs_number().intValue()<=105)){
								bf1.setDiscount(new Double(0));
								bf1.setNo_reg("1a");
							}else if (bf1.getLsdbs_number().intValue()>=16 ){
								bf1.setDiscount(new Double(2.5));
							}
						}else if(bf1.getLsbs_id().intValue() == 821){
							if(bf1.getLsdbs_number().intValue()>=1 && bf1.getLsdbs_number().intValue()<=16){
								bf1.setDiscount(new Double(0));
							}
						}else if(bf1.getLsbs_id().intValue() == 826){
							if((bf1.getLsdbs_number().intValue()>=1 && bf1.getLsdbs_number().intValue()<=10)){
								bf1.setDiscount(new Double(0));
								bf1.setNo_reg("1a");
							}else if (bf1.getLsdbs_number().intValue()>=11 ){
								bf1.setDiscount(new Double(10));
							}
						}
						
						bf1.setLspc_no(hcp1.getLspc_no());
						
//						khusus untuk produk utama link
						if(products.unitLink(edit.getDatausulan().getLsbs_id().toString())){
//							edit.getInvestasiutama().setDaftarbiaya(daftarbiaya);
							List sementara = edit.getInvestasiutama().getDaftarbiaya();
							Biayainvestasi bf = new Biayainvestasi();
							Biayainvestasi bf3 = new Biayainvestasi();;
							if(bf1.getLsbs_id().intValue() == 820 || bf1.getLsbs_id().intValue() == 823 || bf1.getLsbs_id().intValue() == 825){
								if (bf1.getLsdbs_number().intValue()>=16){
									//tambahkan perhitungan biaya tambahan untuk rider yg ditambah
									Double biaya = uwDao.selectRateRider(edit.getDatausulan().getLku_id(), bf1.getUmur(), 0, bf1.getLsbs_id(), bf1.getLsdbs_number());
									Double diskon = 0.0;
									//untuk produk utama link, faktornya dikalikan faktor bulanan semua untuk tiap cara bayarnya
									Double faktor = new Double(0.12);
									//Double faktor = new Double(1.0);
									if(bf1.getDiscount().doubleValue()==0.0){
										diskon = new Double(1.0);
									}else if(bf1.getDiscount().doubleValue()==2.5){
										diskon = new Double(0.975);
									}
									
//									if(edit.getDatausulan().getLscb_id().intValue()==6 || edit.getDatausulan().getLscb_id().intValue()==0){
//										faktor = new Double(0.12);
//									}else if(edit.getDatausulan().getLscb_id().intValue()==2){
//										faktor = new Double(0.65);
//									}else if(edit.getDatausulan().getLscb_id().intValue()==1){
//										faktor = new Double(0.35);
//									}
									if (bf1.getLsdbs_number().intValue()>=16 && bf1.getLsdbs_number().intValue()<=30){
										bf1.setNo_reg("1b");
									}else if (bf1.getLsdbs_number().intValue()>=31 && bf1.getLsdbs_number().intValue()<=45){
										bf1.setNo_reg("1c");
									}else if (bf1.getLsdbs_number().intValue()>=46 && bf1.getLsdbs_number().intValue()<=60){
										bf1.setNo_reg("1d");
									}else if (bf1.getLsdbs_number().intValue()>=61 && bf1.getLsdbs_number().intValue()<=75){
										bf1.setNo_reg("1e");
									}else if (bf1.getLsdbs_number().intValue()>=76 && bf1.getLsdbs_number().intValue()<=90){
										bf1.setNo_reg("1f");
									}
									
									if(edit.getDatausulan().getFlag_as()==2){
										bf.setMbu_jumlah(biaya * diskon_karyawan.doubleValue());
									}
									
									bf.setMbu_jumlah(biaya * faktor.doubleValue() * diskon.doubleValue());
									if(edit.getDatausulan().getLsbs_id() == 167 || edit.getDatausulan().getLsbs_id() == 163 || edit.getDatausulan().getLsbs_id()==191 || edit.getDatausulan().getLsbs_id() == 188 || edit.getDatausulan().getLsbs_id()==194) { 
										if(edit.getDatausulan().getLscb_id()==3){
											faktor = new Double(1.);
										}else if(edit.getDatausulan().getLscb_id()==6){
											faktor = new Double(0.12);
										}else if(edit.getDatausulan().getLscb_id()==2){
											faktor = new Double(0.65);
										}else if(edit.getDatausulan().getLscb_id()==1){
											faktor = new Double(0.35);
										}
										bf.setMbu_jumlah(biaya * faktor.doubleValue() * diskon.doubleValue());
										BigDecimal bulat = new BigDecimal(bf.getMbu_jumlah());
										bf.setMbu_jumlah(0.);
										bulat = bulat.setScale(0,BigDecimal.ROUND_HALF_UP);
										bf2.setMspr_premium(bulat.doubleValue());
									}else {
										bf2.setMspr_premium(0.);
										bf.setLjb_id(uwDao.selectLstJenisBiayaLjbId(bf1.getLsbs_id().intValue(), bf1.getLsdbs_number().intValue()));
										bf.setLjb_biaya(uwDao.selectLstJenisBiayaLjbBiaya(bf.getLjb_id()));
										bf.setReg_spaj(bf1.getReg_spaj());
										bf.setMu_ke(1);
										bf.setMbu_persen(0.0);
										if(edit.getDatausulan().getLsbs_id() != 190 && edit.getDatausulan().getLsbs_id() != 191){
											sementara.add(bf);
										}
									}
									
//									 SK Direksi No. 026/AJS-SK/V/2010 : Biaya admin & dan Biaya admedika ditiadakan (per tanggal 15 Juni 2010)
									if(bf1.getLsbs_id().intValue() == 820){
										if(edit.getDatausulan().getMspo_provider()!=null){
											if(edit.getDatausulan().getMspo_provider()==2){
												//comment : (22/7/2009) req : Dr.Ingrid untuk biaya admin eka sehat diset 37000.
//												if(premi >=1000000){
//													mbu_jumlah3=new Double(37000.0);
//												}else if(premi <1000000){
//													mbu_jumlah3=new Double(31000.0);
//												}
												if(bf1.getLsdbs_number().intValue()>=1 && bf1.getLsdbs_number().intValue()<=15){
													bf3.setLjb_id(new Integer(443));
												}else if(bf1.getLsdbs_number().intValue()>=16 && bf1.getLsdbs_number().intValue()<=30){
													bf3.setLjb_id(new Integer(444));
												}else if(bf1.getLsdbs_number().intValue()>=31 && bf1.getLsdbs_number().intValue()<=45){
													bf3.setLjb_id(new Integer(445));
												}else if(bf1.getLsdbs_number().intValue()>=46 && bf1.getLsdbs_number().intValue()<=60){
													bf3.setLjb_id(new Integer(446));
												}else if(bf1.getLsdbs_number().intValue()>=61 && bf1.getLsdbs_number().intValue()<=75){
													bf3.setLjb_id(new Integer(447));
												}else if(bf1.getLsdbs_number().intValue()>=76 && bf1.getLsdbs_number().intValue()<=90){
													bf3.setLjb_id(new Integer(448));
												}
												bf3.setMbu_jumlah(new Double(30000.0));
												bf3.setLjb_biaya(uwDao.selectLstJenisBiayaLjbBiaya(bf3.getLjb_id()));
												bf3.setReg_spaj(bf1.getReg_spaj());
												bf3.setMu_ke(1);
												bf3.setMbu_persen(0.0);
												sementara.add(bf3);
												
											}
										}
									}
								}else{
									if (bf1.getLsdbs_number().intValue()>=1 && bf1.getLsdbs_number().intValue()<=15){
										bf1.setNo_reg("1a");
									}
//									 SK Direksi No. 026/AJS-SK/V/2010 : Biaya admin & dan Biaya admedika ditiadakan (per tanggal 15 Juni 2010)
									if(bf1.getLsbs_id().intValue() == 820){
										if(edit.getDatausulan().getMspo_provider()!=null){
											if(edit.getDatausulan().getMspo_provider()==2){
												//comment : (22/7/2009) req : Dr.Ingrid untuk biaya admin eka sehat diset 37000.
//												if(premi >=1000000){
//													mbu_jumlah3=new Double(37000.0);
//												}else if(premi <1000000){
//													mbu_jumlah3=new Double(31000.0);
//												}
												if(bf1.getLsdbs_number().intValue()>=1 && bf1.getLsdbs_number().intValue()<=15){
													bf3.setLjb_id(new Integer(443));
												}else if(bf1.getLsdbs_number().intValue()>=16 && bf1.getLsdbs_number().intValue()<=30){
													bf3.setLjb_id(new Integer(444));
												}else if(bf1.getLsdbs_number().intValue()>=31 && bf1.getLsdbs_number().intValue()<=45){
													bf3.setLjb_id(new Integer(445));
												}else if(bf1.getLsdbs_number().intValue()>=46 && bf1.getLsdbs_number().intValue()<=60){
													bf3.setLjb_id(new Integer(446));
												}else if(bf1.getLsdbs_number().intValue()>=61 && bf1.getLsdbs_number().intValue()<=75){
													bf3.setLjb_id(new Integer(447));
												}else if(bf1.getLsdbs_number().intValue()>=76 && bf1.getLsdbs_number().intValue()<=90){
													bf3.setLjb_id(new Integer(448));
												}
												bf3.setMbu_jumlah(new Double(30000.0));
												bf3.setLjb_biaya(uwDao.selectLstJenisBiayaLjbBiaya(bf3.getLjb_id()));
												bf3.setReg_spaj(bf1.getReg_spaj());
												bf3.setMu_ke(1);
												bf3.setMbu_persen(0.0);
												sementara.add(bf3);
												
											}
										}
									}
								}
							}else if(bf1.getLsbs_id().intValue() == 819){
								if(bf1.getLsdbs_number().intValue()>=161){
									Double biaya = uwDao.selectRateRider(edit.getDatausulan().getLku_id(), bf1.getUmur(), 0, bf1.getLsbs_id(), bf1.getLsdbs_number());
									Double diskon = 0.9;
									Double faktor = new Double(0.1);//untuk produk utama link, faktornya dikalikan faktor bulanan semua untuk tiap cara bayarnya
//									if(edit.getDatausulan().getLscb_id().intValue()==6){
//										faktor = new Double(0.1);
//									}else if(edit.getDatausulan().getLscb_id().intValue()==2){
//										faktor = new Double(0.525);
//									}else if(edit.getDatausulan().getLscb_id().intValue()==1){
//										faktor = new Double(0.27);
//									}
									bf.setMbu_jumlah(biaya * faktor.doubleValue() * diskon.doubleValue());
									bf.setLjb_id(uwDao.selectLstJenisBiayaLjbId(bf1.getLsbs_id().intValue(), bf1.getLsdbs_number().intValue()));
									bf.setLjb_biaya(uwDao.selectLstJenisBiayaLjbBiaya(bf.getLjb_id()));
									bf.setReg_spaj(bf1.getReg_spaj());
									bf.setMu_ke(1);
									bf.setMbu_persen(0.0);
//									sementara.add(bf);
								}
							}else if(bf1.getLsbs_id().intValue() == 826){
								if(bf1.getLsdbs_number().intValue()>=11){
									Double biaya = uwDao.selectRateRider(edit.getDatausulan().getLku_id(), bf1.getUmur(), 0, bf1.getLsbs_id(), bf1.getLsdbs_number());
									Double diskon = 0.9;
									Double faktor = new Double(0.1);//untuk produk utama link, faktornya dikalikan faktor bulanan semua untuk tiap cara bayarnya
//									if(edit.getDatausulan().getLscb_id().intValue()==6){
//										faktor = new Double(0.1);
//									}else if(edit.getDatausulan().getLscb_id().intValue()==2){
//										faktor = new Double(0.525);
//									}else if(edit.getDatausulan().getLscb_id().intValue()==1){
//										faktor = new Double(0.27);
//									}
									bf.setMbu_jumlah(biaya * faktor.doubleValue() * diskon.doubleValue());
									bf.setLjb_id(uwDao.selectLstJenisBiayaLjbId(bf1.getLsbs_id().intValue(), bf1.getLsdbs_number().intValue()));
									bf.setLjb_biaya(uwDao.selectLstJenisBiayaLjbBiaya(bf.getLjb_id()));
									bf.setReg_spaj(bf1.getReg_spaj());
									bf.setMu_ke(1);
									bf.setMbu_persen(0.0);
//									sementara.add(bf);
								}
							}
						}else if(bf1.getLsbs_id().intValue() == 820 || bf1.getLsbs_id().intValue() == 823 || bf1.getLsbs_id().intValue() == 825){
							if ((bf1.getLsdbs_number().intValue()>=16 && bf1.getLsdbs_number().intValue()<=90) || (bf1.getLsdbs_number().intValue()>=106 && bf1.getLsdbs_number().intValue()<=195 )){
								//tambahkan perhitungan biaya tambahan untuk rider yg ditambah
								Double biaya = uwDao.selectRateRider(edit.getDatausulan().getLku_id(), bf1.getUmur(), 0, bf1.getLsbs_id(), bf1.getLsdbs_number());
								Double diskon = 0.0;
								Double rate = new Double(1.);
								if(edit.getDatausulan().getLscb_id()==3){
									rate = new Double(1.);
								}else if(edit.getDatausulan().getLscb_id()==6){
									rate = new Double(0.12);
								}else if(edit.getDatausulan().getLscb_id()==2){
									rate = new Double(0.65);
								}else if(edit.getDatausulan().getLscb_id()==1){
									rate = new Double(0.35);
								}
								Double faktor = new Double(1.0);
								if(bf1.getDiscount().doubleValue()==0.0){
									diskon = new Double(1.0);
								}else if(bf1.getDiscount().doubleValue()==2.5){
									diskon = new Double(0.975);
								}
								
//								if(edit.getDatausulan().getLscb_id().intValue()==6){
//									faktor = new Double(0.12);
//								}else if(edit.getDatausulan().getLscb_id().intValue()==2){
//									faktor = new Double(0.65);
//								}else if(edit.getDatausulan().getLscb_id().intValue()==1){
//									faktor = new Double(0.35);
//								}
								if ((bf1.getLsdbs_number().intValue()>=16 && bf1.getLsdbs_number().intValue()<=30) || (bf1.getLsdbs_number().intValue()>=106 && bf1.getLsdbs_number().intValue()<=120 )){
									bf1.setNo_reg("1b");
								}else if ((bf1.getLsdbs_number().intValue()>=31 && bf1.getLsdbs_number().intValue()<=45) || (bf1.getLsdbs_number().intValue()>=121 && bf1.getLsdbs_number().intValue()<=135 )){
									bf1.setNo_reg("1c");
								}else if ((bf1.getLsdbs_number().intValue()>=46 && bf1.getLsdbs_number().intValue()<=60) || (bf1.getLsdbs_number().intValue()>=136 && bf1.getLsdbs_number().intValue()<=150 )){
									bf1.setNo_reg("1d");
								}else if ((bf1.getLsdbs_number().intValue()>=61 && bf1.getLsdbs_number().intValue()<=75) || (bf1.getLsdbs_number().intValue()>=151 && bf1.getLsdbs_number().intValue()<=165 )){
									bf1.setNo_reg("1e");
								}else if ((bf1.getLsdbs_number().intValue()>=76 && bf1.getLsdbs_number().intValue()<=90) || (bf1.getLsdbs_number().intValue()>=166 && bf1.getLsdbs_number().intValue()<=180 )){
									bf1.setNo_reg("1f");
								}else if ((bf1.getLsdbs_number().intValue()>=181 && bf1.getLsdbs_number().intValue()<=195 )){
									bf1.setNo_reg("1g");
								}
								
								
								bf2.setMspr_premium(diskon_karyawan.doubleValue() * biaya * faktor.doubleValue() * diskon.doubleValue() * rate.doubleValue());
								hcp1.setPremi(bf2.getMspr_premium());
								bf1.setPremi(bf2.getMspr_premium());
							}else if (bf1.getLsdbs_number().intValue()<16){
								Double biaya = uwDao.selectRateRider(edit.getDatausulan().getLku_id(), bf1.getUmur(), 0, bf1.getLsbs_id(), bf1.getLsdbs_number());
								Double diskon = 1.0;
								Double rate = new Double(1.);
								if(edit.getDatausulan().getLscb_id()==3){
									rate = new Double(1.);
								}else if(edit.getDatausulan().getLscb_id()==6){
									rate = new Double(0.12);
								}else if(edit.getDatausulan().getLscb_id()==2){
									rate = new Double(0.65);
								}else if(edit.getDatausulan().getLscb_id()==1){
									rate = new Double(0.35);
								}
								Double faktor = new Double(1.0);
								bf1.setNo_reg("1a");
								bf2.setMspr_premium(diskon_karyawan.doubleValue() * biaya * faktor.doubleValue() * diskon.doubleValue());
								bf1.setPremi(bf2.getMspr_premium());
							}
						}else if(bf1.getLsbs_id().intValue() == 821){
								//tambahkan perhitungan biaya tambahan untuk rider yg ditambah
								Double biaya = uwDao.selectRateRider(edit.getDatausulan().getLku_id(), bf1.getUmur(), 0, bf1.getLsbs_id(), bf1.getLsdbs_number());
								Double diskon = 0.0;
								Double rate = new Double(0.12);
								Double faktor = new Double(1.0);
								if(bf1.getDiscount().doubleValue()==0.0){
									diskon = new Double(1.0);
								}else if(bf1.getDiscount().doubleValue()==10.0){
									diskon = new Double(1.0);
								}
								
								if(edit.getDatausulan().getLscb_id().intValue()==6){
									faktor = new Double(12.0);
								}else if(edit.getDatausulan().getLscb_id().intValue()==2){
									faktor = new Double(2.0);
								}else if(edit.getDatausulan().getLscb_id().intValue()==1){
									faktor = new Double(4.0);
								}
								bf2.setMspr_premium((biaya / faktor.doubleValue()) * diskon.doubleValue());
								bf1.setPremi(bf2.getMspr_premium());
						}else if(products.multiInvest(edit.getDatausulan().getLsbs_id().toString())){
							if(bf1.getLsbs_id().intValue() == 820 || bf1.getLsbs_id().intValue() == 823 || bf1.getLsbs_id().intValue() == 825){
								if ((bf1.getLsdbs_number().intValue()>=16 && bf1.getLsdbs_number().intValue()<=90) || (bf1.getLsdbs_number().intValue()>=106 && bf1.getLsdbs_number().intValue()<=195 )){
									//tambahkan perhitungan biaya tambahan untuk rider yg ditambah
									Double biaya = uwDao.selectRateRider(edit.getDatausulan().getLku_id(), bf1.getUmur(), 0, bf1.getLsbs_id(), bf1.getLsdbs_number());
									Double diskon = 0.0;
									Double rate = new Double(1.);
									if(edit.getDatausulan().getLscb_id()==3){
										rate = new Double(1.);
									}else if(edit.getDatausulan().getLscb_id()==6){
										rate = new Double(0.12);
									}else if(edit.getDatausulan().getLscb_id()==2){
										rate = new Double(0.65);
									}else if(edit.getDatausulan().getLscb_id()==1){
										rate = new Double(0.35);
									}
									Double faktor = new Double(1.0);
									if(bf1.getDiscount().doubleValue()==0.0){
										diskon = new Double(1.0);
									}else if(bf1.getDiscount().doubleValue()==2.5){
										diskon = new Double(0.975);
									}
									
//									if(edit.getDatausulan().getLscb_id().intValue()==6){
//										faktor = new Double(0.12);
//									}else if(edit.getDatausulan().getLscb_id().intValue()==2){
//										faktor = new Double(0.65);
//									}else if(edit.getDatausulan().getLscb_id().intValue()==1){
//										faktor = new Double(0.35);
//									}
									if ((bf1.getLsdbs_number().intValue()>=16 && bf1.getLsdbs_number().intValue()<=30) || (bf1.getLsdbs_number().intValue()>=106 && bf1.getLsdbs_number().intValue()<=120 )){
										bf1.setNo_reg("1b");
									}else if ((bf1.getLsdbs_number().intValue()>=31 && bf1.getLsdbs_number().intValue()<=45) || (bf1.getLsdbs_number().intValue()>=121 && bf1.getLsdbs_number().intValue()<=135 )){
										bf1.setNo_reg("1c");
									}else if ((bf1.getLsdbs_number().intValue()>=46 && bf1.getLsdbs_number().intValue()<=60) || (bf1.getLsdbs_number().intValue()>=136 && bf1.getLsdbs_number().intValue()<=150 )){
										bf1.setNo_reg("1d");
									}else if ((bf1.getLsdbs_number().intValue()>=61 && bf1.getLsdbs_number().intValue()<=75) || (bf1.getLsdbs_number().intValue()>=151 && bf1.getLsdbs_number().intValue()<=165 )){
										bf1.setNo_reg("1e");
									}else if ((bf1.getLsdbs_number().intValue()>=76 && bf1.getLsdbs_number().intValue()<=90) || (bf1.getLsdbs_number().intValue()>=166 && bf1.getLsdbs_number().intValue()<=180 )){
										bf1.setNo_reg("1f");
									}else if ((bf1.getLsdbs_number().intValue()>=181 && bf1.getLsdbs_number().intValue()<=195 )){
										bf1.setNo_reg("1g");
									}
									
									
									bf2.setMspr_premium(diskon_karyawan.doubleValue() * biaya * faktor.doubleValue() * diskon.doubleValue() * rate.doubleValue());
									hcp1.setPremi(bf2.getMspr_premium());
									bf1.setPremi(bf2.getMspr_premium());
								}else if (bf1.getLsdbs_number().intValue()<16){
									Double biaya = uwDao.selectRateRider(edit.getDatausulan().getLku_id(), bf1.getUmur(), 0, bf1.getLsbs_id(), bf1.getLsdbs_number());
									Double diskon = 1.0;
									Double rate = new Double(0.12);
									Double faktor = new Double(1.0);
									bf1.setNo_reg("1a");
									bf2.setMspr_premium(diskon_karyawan.doubleValue() * biaya * faktor.doubleValue() * diskon.doubleValue());
									bf1.setPremi(bf2.getMspr_premium());
								}
							}else if(bf1.getLsbs_id().intValue() == 819){
								Double biaya = uwDao.selectRateRider(edit.getDatausulan().getLku_id(), bf1.getUmur(), 0, bf1.getLsbs_id(), bf1.getLsdbs_number());
								Double diskon = 0.9;
								Double faktor = new Double(1);//untuk produk utama link, faktornya dikalikan faktor bulanan semua untuk tiap cara bayarnya
								if(edit.getDatausulan().getLscb_id().intValue()==6){
									faktor = new Double(0.1);
								}else if(edit.getDatausulan().getLscb_id().intValue()==2){
									faktor = new Double(0.525);
								}else if(edit.getDatausulan().getLscb_id().intValue()==1){
									faktor = new Double(0.27);
								}
								bf2.setMspr_premium(diskon_karyawan.doubleValue() * biaya * faktor.doubleValue() * diskon.doubleValue() );
								hcp1.setPremi(bf2.getMspr_premium());
								bf1.setPremi(bf2.getMspr_premium());
							}
						}else if(bf1.getLsbs_id().intValue() == 826){
							Double biaya = uwDao.selectRateRider(edit.getDatausulan().getLku_id(), bf1.getUmur(), 0, bf1.getLsbs_id(), bf1.getLsdbs_number());
							Double diskon = 0.0;
							Double rate = new Double(1.);
							if(edit.getDatausulan().getLscb_id()==3){
								rate = new Double(1.);
							}else if(edit.getDatausulan().getLscb_id()==6){
								rate = new Double(0.1);
							}else if(edit.getDatausulan().getLscb_id()==2){
								rate = new Double(0.525);
							}else if(edit.getDatausulan().getLscb_id()==1){
								rate = new Double(0.27);
							}
							Double faktor = new Double(1.0);
							if(bf1.getDiscount().doubleValue()==0.0){
								diskon = new Double(1.0);
							}else if(bf1.getDiscount().doubleValue()==10){
								diskon = new Double(0.9);
							}
							
//							if(edit.getDatausulan().getLscb_id().intValue()==6){
//								faktor = new Double(0.12);
//							}else if(edit.getDatausulan().getLscb_id().intValue()==2){
//								faktor = new Double(0.65);
//							}else if(edit.getDatausulan().getLscb_id().intValue()==1){
//								faktor = new Double(0.35);
//							}
							if ((bf1.getLsdbs_number().intValue()>=11 && bf1.getLsdbs_number().intValue()<=20)){
								bf1.setNo_reg("1b");
							}else if ((bf1.getLsdbs_number().intValue()>=21 && bf1.getLsdbs_number().intValue()<=30)){
								bf1.setNo_reg("1c");
							}else if ((bf1.getLsdbs_number().intValue()>=31 && bf1.getLsdbs_number().intValue()<=40)){
								bf1.setNo_reg("1d");
							}else if ((bf1.getLsdbs_number().intValue()>=41 && bf1.getLsdbs_number().intValue()<=50)){
								bf1.setNo_reg("1e");
							}else if ((bf1.getLsdbs_number().intValue()>=51 && bf1.getLsdbs_number().intValue()<=60)){
								bf1.setNo_reg("1f");
							}else if ((bf1.getLsdbs_number().intValue()>=61 && bf1.getLsdbs_number().intValue()<=70 )){
								bf1.setNo_reg("1g");
							}
							
							
							bf2.setMspr_premium(diskon_karyawan.doubleValue() * biaya * faktor.doubleValue() * diskon.doubleValue() * rate.doubleValue());
							hcp1.setPremi(bf2.getMspr_premium());
							bf1.setPremi(bf2.getMspr_premium());
						}
						
						f_hit_umur umr= new f_hit_umur();
						Integer tahun1,tahun2, bulan1, bulan2, tanggal1, tanggal2;
						tanggal1= edit.getDatausulan().getMste_beg_date().getDate();
						bulan1 = (edit.getDatausulan().getMste_beg_date().getMonth())+1;
						tahun1 = (edit.getDatausulan().getMste_beg_date().getYear())+1900;
						
						//tgl lahir ttg
						tanggal2=bf1.getTgl_lahir().getDate();
						bulan2=(bf1.getTgl_lahir().getMonth())+1;
						tahun2=(bf1.getTgl_lahir().getYear())+1900;
						Integer umur = umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
						bf1.setUmur(umur);
						bf1.setNo_urut(uwDao.selectCountMstPeserta(edit.getPemegang().getReg_spaj())+1);
						bf1.setFlag_val_send(new Integer(1));
						bf1.setFlag_admedika(new Integer(0));
						bf1.setNext_send(edit.getDatausulan().getMste_beg_date());
						if(bf1.getLsbs_id().intValue() == 819 || bf1.getLsbs_id().intValue() == 820 || bf1.getLsbs_id().intValue() == 823 || bf1.getLsbs_id().intValue() == 825 || bf1.getLsbs_id().intValue() == 826){
							if(bf1.getLsbs_id().intValue() == 820 || bf1.getLsbs_id().intValue() == 823 || bf1.getLsbs_id().intValue() == 825){
								if((bf1.getLsdbs_number().intValue()>=16 && bf1.getLsdbs_number().intValue()<=90) || (bf1.getLsdbs_number().intValue()>=106 && bf1.getLsdbs_number().intValue()<=195)){
									if(products.unitLink(edit.getDatausulan().getLsbs_id().toString()) && edit.getDatausulan().getFlag_platinumlink()==0){
										if(edit.getDatausulan().getLsbs_id() == 167 || edit.getDatausulan().getLsbs_id() == 163 || edit.getDatausulan().getLsbs_id()==191 || edit.getDatausulan().getLsbs_id() == 188 || edit.getDatausulan().getLsbs_id()==194) { 
											BigDecimal bulat = new BigDecimal(bf2.getMspr_premium());
											bulat = bulat.setScale(0,BigDecimal.ROUND_HALF_UP);
											bf1.setPremi(bulat.doubleValue());
										}else {
											bf1.setPremi(0.);
										}
									}
									this.bacDao.insert_mst_peserta(bf1);
								}
							}else if(bf1.getLsbs_id().intValue() == 826){
								if ((bf1.getLsdbs_number().intValue() >=11 && bf1.getLsdbs_number().intValue()<=70))
								{
									if(products.unitLink(edit.getDatausulan().getLsbs_id().toString()) && edit.getDatausulan().getFlag_platinumlink()==0){
										if(edit.getDatausulan().getLsbs_id() == 167 || edit.getDatausulan().getLsbs_id() == 163 || edit.getDatausulan().getLsbs_id()==191 || edit.getDatausulan().getLsbs_id() == 188 || edit.getDatausulan().getLsbs_id()==194) { 
											BigDecimal bulat = new BigDecimal(bf2.getMspr_premium());
											bulat = bulat.setScale(0,BigDecimal.ROUND_HALF_UP);
											bf1.setPremi(bulat.doubleValue());
										}else {
											bf1.setPremi(0.);
										}
										bf1.setPremi(0.);
									}
									this.bacDao.insert_mst_peserta(bf1);
								}
							}else {
								if ((bf1.getLsdbs_number().intValue() >=21 && bf1.getLsdbs_number().intValue()<=140) || (bf1.getLsdbs_number().intValue()>=161 && bf1.getLsdbs_number().intValue()<=280) || (bf1.getLsdbs_number().intValue()>=301 && bf1.getLsdbs_number().intValue()<=380) || (bf1.getLsdbs_number().intValue()>=391 && bf1.getLsdbs_number().intValue()<=430) )
								{
									if(products.unitLink(edit.getDatausulan().getLsbs_id().toString()) && edit.getDatausulan().getFlag_platinumlink()==0){
										if(edit.getDatausulan().getLsbs_id() == 167 || edit.getDatausulan().getLsbs_id() == 163 || edit.getDatausulan().getLsbs_id()==191 || edit.getDatausulan().getLsbs_id() == 188 || edit.getDatausulan().getLsbs_id()==194) { 
											BigDecimal bulat = new BigDecimal(bf2.getMspr_premium());
											bulat = bulat.setScale(0,BigDecimal.ROUND_HALF_UP);
											bf1.setPremi(bulat.doubleValue());
										}else {
											bf1.setPremi(0.);
										}
										bf1.setPremi(0.);
									}
									this.bacDao.insert_mst_peserta(bf1);
								}
							}
							
						}
						
						Date SebelasBelasOktoberDuaRibuSembilan = defaultDateFormat.parse("11/10/2009");
						Date SatuJanuariDuaRibuSepuluh = defaultDateFormat.parse("1/1/2010");
						if(bf1.getLsbs_id().intValue() == 820 && (edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh))){
							bf1.setLsbs_id(822);
							bf1.setNo_urut(uwDao.selectCountMstPeserta(edit.getPemegang().getReg_spaj())+1);
							if(bf1.getLsdbs_number().intValue()>=1 && bf1.getLsdbs_number().intValue()<=15){
								bf1.setLsdbs_number(1);
								bf1.setDiscount(0.0);
								bf1.setPremi(0.0);
							}else if(bf1.getLsdbs_number().intValue()>=16 && bf1.getLsdbs_number().intValue()<=30){
								bf1.setLsdbs_number(3);
								bf1.setDiscount(0.0);
								bf1.setPremi(0.0);
							}else if(bf1.getLsdbs_number().intValue()>=31 && bf1.getLsdbs_number().intValue()<=45){
								bf1.setLsdbs_number(4);
								bf1.setDiscount(0.0);
								bf1.setPremi(0.0);
							}else if(bf1.getLsdbs_number().intValue()>=46 && bf1.getLsdbs_number().intValue()<=60){
								bf1.setLsdbs_number(5);
								bf1.setDiscount(0.0);
								bf1.setPremi(0.0);
							}else if(bf1.getLsdbs_number().intValue()>=61 && bf1.getLsdbs_number().intValue()<=75){
								bf1.setLsdbs_number(6);
								bf1.setDiscount(0.0);
								bf1.setPremi(0.0);
							}else if(bf1.getLsdbs_number().intValue()>=76 && bf1.getLsdbs_number().intValue()<=90){
								bf1.setLsdbs_number(7);
								bf1.setDiscount(0.0);
								bf1.setPremi(0.0);
							}
							this.bacDao.insert_mst_peserta(bf1);
						}
						
						Map param28=new HashMap();
						if(bf2.getMspr_persen()==null){
							bf2.setMspr_persen(0);
						}
						if(bf2.getMspr_premium()==null){
							bf2.setMspr_premium(0.0);
						}
						
						param28.put("strTmpSPAJ",edit.getPemegang().getReg_spaj());	
						param28.put("v_intBaseBusinessId",bf2.getLsbs_id());
						param28.put("v_intBaseBusinessNo",bf2.getLsdbs_number());
						param28.put("v_strKurs",bf2.getLku_id());
						param28.put("v_strBeginDate",bf2.getMspr_beg_date());
						param28.put("v_strEndDate",bf2.getMspr_end_date());
						param28.put("intClass",bf2.getMspr_class());
						param28.put("v_intBaseRate",bf2.getMspr_rate());
						param28.put("v_curBasePremium",bf2.getMspr_premium());
						param28.put("v_intInsPeriod",bf2.getMspr_ins_period());
						param28.put("v_curUP",bf2.getMspr_tsi());
						param28.put("intPAA",bf2.getMspr_tsi_pa_a());
						param28.put("intPAB",bf2.getMspr_tsi_pa_b());
						param28.put("intPAC",bf2.getMspr_tsi_pa_c());
						param28.put("intPAD",bf2.getMspr_tsi_pa_d());
						param28.put("intPAMotor",bf2.getMspr_tsi_pa_m());
						param28.put("v_intUnit",bf2.getMspr_unit());
						param28.put("ldt_endpay",bf2.getMspr_end_pay());
						param28.put("v_percent",bf2.getMspr_persen());
						param28.put("ins_rider",bf2.getMspr_tt());
						param28.put("mspr_extra",bf2.getMspr_extra());
						
						if(!products.unitLink(edit.getDatausulan().getLsbs_id().toString())){
							if(bf2.getLsbs_id().intValue()==820 || bf2.getLsbs_id().intValue()==823 || bf2.getLsbs_id().intValue()==825 || bf2.getLsbs_id().intValue()==826 ){
								param28.put("mps_kode", 0);
								param28.put("msl_no", 0);
								param28.put("lji_id", 0);
								param28.put("v_curBasePremiumTahunan", bf2.getMspr_premium());
								param28.put("mpr_cara_bayar_rider", edit.getPowersave().getMpr_cara_bayar_rider());
								param28.put("kurang_bayar", 0);
								param28.put("lus_id", User.getLus_id());
								param28.put("tgl_input", commonDao.selectSysdateTrunc());
								param28.put("lscb_id_rider", edit.getDatausulan().getLscb_id_rider());
								insert("insert.mst_rider_save", param28);
							}
						}
							
						
						if(bf2.getLsbs_id().intValue()==819 || bf2.getLsbs_id().intValue()==826){
							if(bf2.getLsbs_id().intValue()==819){
								if ((bf2.getLsdbs_number().intValue() >=21 && bf2.getLsdbs_number().intValue()<=140) || (bf2.getLsdbs_number().intValue()>=161 && bf2.getLsdbs_number().intValue()<=280) || (bf2.getLsdbs_number().intValue()>=301 && bf2.getLsdbs_number().intValue()<=380) || (bf2.getLsdbs_number().intValue()>=391 && bf2.getLsdbs_number().intValue()<=430))
								{
									insert("insert.mst_product_insured_rider", param28);
									//logger.info("insert mst product insured rider hcp");
								}
							}else{
								if (bf2.getLsdbs_number().intValue() >=11 && bf2.getLsdbs_number().intValue()<=70){
									insert("insert.mst_product_insured_rider", param28);
								}
							}
							
						}else if(bf2.getLsbs_id().intValue()==820 || bf2.getLsbs_id().intValue()==823 || bf2.getLsbs_id().intValue()==825){
								if (bf2.getLsdbs_number().intValue() >=16)
								{
									insert("insert.mst_product_insured_rider", param28);
									if (edit.getDatausulan().getMste_beg_date().after(SebelasBelasOktoberDuaRibuSembilan) && edit.getDatausulan().getMste_beg_date().before(SatuJanuariDuaRibuSepuluh)){
										param28.put("v_intBaseRate",0);
										param28.put("v_curBasePremium",0);
										param28.put("ldt_endpay",null);
										param28.put("v_curUP",bf2.getMspr_tsi()*10);
										//(Deddy) - Insert swine Flu untuk tertanggung tambahan
										if(bf2.getLsdbs_number().intValue()>=16 && bf2.getLsdbs_number().intValue()<=30){
											param28.put("v_intBaseBusinessId",822);
											param28.put("v_intBaseBusinessNo",3);
											insert("insert.mst_product_insured_rider", param28);
											insert("insert.mst_rider_save", param28);
										}else if(bf2.getLsdbs_number().intValue()>=31 && bf2.getLsdbs_number().intValue()<=45){
											param28.put("v_intBaseBusinessId",822);
											param28.put("v_intBaseBusinessNo",4);
											insert("insert.mst_product_insured_rider", param28);
											insert("insert.mst_rider_save", param28);
										}else if(bf2.getLsdbs_number().intValue()>=46 && bf2.getLsdbs_number().intValue()<=60){
											param28.put("v_intBaseBusinessId",822);
											param28.put("v_intBaseBusinessNo",5);
											insert("insert.mst_product_insured_rider", param28);
											insert("insert.mst_rider_save", param28);
										}else if(bf2.getLsdbs_number().intValue()>=61 && bf2.getLsdbs_number().intValue()<=75){
											param28.put("v_intBaseBusinessId",822);
											param28.put("v_intBaseBusinessNo",6);
											insert("insert.mst_product_insured_rider", param28);
											insert("insert.mst_rider_save", param28);
										}else if(bf2.getLsdbs_number().intValue()>=76 && bf2.getLsdbs_number().intValue()<=90){
											param28.put("v_intBaseBusinessId",822);
											param28.put("v_intBaseBusinessNo",7);
											insert("insert.mst_product_insured_rider", param28);
											insert("insert.mst_rider_save", param28);
										}
									}
									//logger.info("insert mst product insured rider hcp");
								}
						}else if(bf2.getLsbs_id().intValue()==821){
								insert("insert.mst_product_insured_rider", param28);
						}
						
					}
				
					Integer v_intActionBy = edit.getPemegang().getLus_id();
					Integer	flag_rider = edit.getDatausulan().getFlag_rider();
					Integer v_intInsPeriod = edit.getDatausulan().getMspr_ins_period();
					Integer li_tahun =null;
					Integer	flag_jenis_plan = edit.getDatausulan().getFlag_jenis_plan();
					if (flag_jenis_plan == null)
					{
						flag_jenis_plan = new Integer (0);
					}
					
					String ldt_endpay="";
					Date ldt_endpay1=null;
					Date ldt_endpay4 =null;
					Date ldt_endpay5=null;
					Integer ai_month=null;
					Integer flag_platinumlink = edit.getDatausulan().getFlag_platinumlink();
					if (flag_platinumlink ==null)
					{
						flag_platinumlink = new Integer(0);
					}
					Date v_strBeginDate = edit.getDatausulan().getMste_beg_date();
					String v_strKurs = edit.getDatausulan().getKurs_p().toUpperCase();
					Date v_strEndDate = edit.getDatausulan().getMste_end_date();
					Integer	kode_flag = edit.getDatausulan().getKode_flag();
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					
				
						if (flag_rider.intValue()==1)
						{
							li_tahun =v_intInsPeriod;
							if ((flag_jenis_plan.intValue()==1) )
							{
								li_tahun = new Integer(6);
							}
							ai_month=new Integer((li_tahun.intValue() * 12) - 1);
										
							Map param20=new HashMap();
							param20.put("v_strBeginDate",v_strBeginDate);	
							param20.put("ai_month",ai_month);	
							//logger.info("add month");
			
							Date ldt_endpay2 =null;
						    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							ldt_endpay2=df1.parse((String)querySingle("select.addmonths", param20));
							int tgl_endpay = ldt_endpay2.getDate();
							int bln_endpay = ldt_endpay2.getMonth()+1;
							int thn_endpay = ldt_endpay2.getYear()+1900;
							ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
							ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
							ldt_endpay = ldt_endpay.concat("/");
							ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
							ldt_endpay1 = df.parse(ldt_endpay);
								
								//end pay untuk biaya administrasi
								Calendar tanggal_sementara = Calendar.getInstance();
								Date ldt_endpay3 =null;
								Integer ai_month1=new Integer(-1);
								Map param28=new HashMap();
								param28.put("v_strBeginDate",v_strEndDate);	
								param28.put("ai_month",ai_month1);	
								ldt_endpay3=df1.parse((String)querySingle("select.addmonths", param28));
								tgl_endpay = ldt_endpay3.getDate();
								bln_endpay = ldt_endpay3.getMonth()+1;
								thn_endpay = ldt_endpay3.getYear()+1900;
								ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
								ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
								ldt_endpay = ldt_endpay.concat("/");
								ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
								tanggal_sementara.set(thn_endpay,bln_endpay,tgl_endpay);	
								tanggal_sementara.add(Calendar.DATE,1);
								tgl_endpay = tanggal_sementara.getTime().getDate();
								bln_endpay = tanggal_sementara.getTime().getMonth();
								thn_endpay = tanggal_sementara.getTime().getYear()+1900;
								ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
								ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
								ldt_endpay = ldt_endpay.concat("/");
								ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
								ldt_endpay4 = df.parse(ldt_endpay);
							if (flag_jenis_plan.intValue()!=1)
							{
								ldt_endpay4 =ldt_endpay1;
							}
							
							//biaya pokok
							Integer ai_month3=new Integer((10 * 12) - 1);
							if(edit.getDatausulan().getLsbs_id()==191){
								ai_month3=new Integer((8 * 12) - 1);
							}
							Date tgl_beg_date_polis = edit.getDatausulan().getMste_beg_date();
							Integer tahun_beg_date_polis = tgl_beg_date_polis.getYear() + 1900;
							if ((flag_platinumlink.intValue() == 1) && (tahun_beg_date_polis.intValue() > 2006))
							{
								 ai_month3 =  new Integer(ai_month3.intValue() + 1);
							}
								
							Map param29=new HashMap();
							param29.put("v_strBeginDate",v_strBeginDate);	
							param29.put("ai_month",ai_month3);	
							//logger.info("add month");
	
							Date ldt_endpay6 =null;
							ldt_endpay6=df1.parse((String)querySingle("select.addmonths", param29));
										
							tgl_endpay = ldt_endpay6.getDate();
							bln_endpay = ldt_endpay6.getMonth()+1;
							thn_endpay = ldt_endpay6.getYear()+1900;
							ldt_endpay = (FormatString.rpad("0",Integer.toString(tgl_endpay),2)).concat("/");
							ldt_endpay = ldt_endpay.concat(FormatString.rpad("0",Integer.toString(bln_endpay),2));
							ldt_endpay = ldt_endpay.concat("/");
							ldt_endpay = ldt_endpay.concat(Integer.toString(thn_endpay));
							ldt_endpay5 = df.parse(ldt_endpay);

						}
					
					//if(datarider.getLsbs_id().intValue()==819){
						if (kode_flag.intValue() > 1 && kode_flag.intValue() != 11 && kode_flag.intValue() != 15)
						{
								proc_unitlink(edit,strTmpSPAJ,null,v_intActionBy ,User ,ldt_endpay1 ,ldt_endpay4 ,ldt_endpay5);
						}	
					//}
				}
				edit.getDatausulan().setStatus_submit("berhasil");
				Cmdeditbac edit2= (Cmdeditbac) cmd;
				edit.getDatausulan().setDaftahcp(edit2.getDatausulan().getDaftahcp());
				//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			} catch (Exception e) {
				edit.getDatausulan().setStatus_submit("gagal");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				logger.error("ERROR :", e);
			}
			
			return edit;
			
		}
		
		private void proc_save_data_pic(Cmdeditbac edit, String strPOClientID)throws ServletException,IOException
		{
			edit.getContactPerson().setMcl_id(strPOClientID);
			edit.getContactPerson().setFlag_ut(1);
			edit.getContactPerson().setNo_urut(1);
			edit.getContactPerson().setTelp_kantor(edit.getContactPerson().getTelpon_kantor());
			edit.getContactPerson().setTelp_hp(edit.getContactPerson().getNo_hp());
			edit.getContactPerson().setLus_id(edit.getPemegang().getLus_id());
			/*------------------------------------------------------------
			 Insert Policy Holder PIC information to MST_COMPANY_CONTACT*/
				int rowupdated = update("update.mst_clientpic", edit.getContactPerson());
//				logger.info("update mst client pic");
				if (rowupdated==0)
				{
					insert("insert.mst_clientpic", edit.getContactPerson());
					//logger.info("insert mst client pic");
				}
				
			//------------------------------------------------------------
			// Insert Policy Holder PIC Home Address information to MST_COMPANY_CONTACT_ADDRESS
				int rowupdated1 = update("update.mst_addresspic", edit.getContactPerson());
//				logger.info("update mst address pic");
				if (rowupdated1==0)
				{
					insert("insert.mst_addresspic", edit.getContactPerson());
					//logger.info("insert mst address pic");
				}
			
			delete("delete.mst_company_contact_family", strPOClientID);
			String nama_suamiistri = edit.getContactPerson().getNama_si();
			if(nama_suamiistri == null)nama_suamiistri = "";
			if (!nama_suamiistri.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_suamiistri = edit.getContactPerson().getTgllhr_si();
				Map param1=new HashMap();
				param1.put("mcl_id", strPOClientID);
				param1.put("nama", edit.getContactPerson().getNama_si());
				param1.put("lsre_id",5);
				param1.put("tanggal_lahir", tanggal_lahir_suamiistri );
				param1.put("insured", 1);
				param1.put("no", 0);
				insert("insert.mst_company_contact_family", param1);
				//logger.info("insert mst keluarga");
			}
			
			String nama_anak1 = edit.getContactPerson().getNama_anak1();
			if(nama_anak1 == null)nama_anak1 = "";
			if (!nama_anak1.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_anak1 = edit.getContactPerson().getTgllhr_anak1();
				Map param1=new HashMap();
				param1.put("mcl_id", strPOClientID);
				param1.put("nama", edit.getContactPerson().getNama_anak1());
				param1.put("lsre_id",4);
				param1.put("tanggal_lahir", tanggal_lahir_anak1);
				param1.put("insured", 1);
				param1.put("no", 1);
				insert("insert.mst_company_contact_family", param1);
				//logger.info("insert mst keluarga");
			}
			
			String nama_anak2 = edit.getContactPerson().getNama_anak2();
			if(nama_anak2 == null)nama_anak2 = "";
			if (!nama_anak2.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_anak2 = edit.getContactPerson().getTgllhr_anak2();
				Map param1=new HashMap();
				param1.put("mcl_id", strPOClientID);
				param1.put("nama", edit.getContactPerson().getNama_anak2());
				param1.put("lsre_id",4);
				param1.put("tanggal_lahir", tanggal_lahir_anak2);
				param1.put("insured", 1);
				param1.put("no", 2);
				insert("insert.mst_company_contact_family", param1);
				//logger.info("insert mst keluarga");
			}
			
			String nama_anak3 = edit.getContactPerson().getNama_anak2();
			if(nama_anak3 == null)nama_anak3 = "";
			if (!nama_anak3.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_anak3 = edit.getContactPerson().getTgllhr_anak3();
				Map param1=new HashMap();
				param1.put("mcl_id", strPOClientID);
				param1.put("nama", edit.getContactPerson().getNama_anak3());
				param1.put("lsre_id",4);
				param1.put("tanggal_lahir", tanggal_lahir_anak3);
				param1.put("insured", 1);
				param1.put("no", 3);
				insert("insert.mst_company_contact_family", param1);
				//logger.info("insert mst keluarga");
			}
		}

		private void proc_save_suamiistri_pp(Cmdeditbac edit, String strTmpSPAJ)throws ServletException,IOException
		{
			//delete("delete.mst_keluarga", strTmpSPAJ);
			String nama_suamiistri = edit.getPemegang().getNama_si();
			if (!nama_suamiistri.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_suamiistri = edit.getPemegang().getTgllhr_si();
				Map param1=new HashMap();
				param1.put("strTmpSPAJ", strTmpSPAJ);
				param1.put("nama", edit.getPemegang().getNama_si());
				param1.put("lsre_id",5);
				param1.put("tanggal_lahir", tanggal_lahir_suamiistri );
				param1.put("insured", 1);
				param1.put("no", 0);
				insert("insert_mst_keluarga", param1);
				//logger.info("insert mst keluarga");
			}
			
			String nama_anak1 = edit.getPemegang().getNama_anak1();
			if (!nama_anak1.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_anak1 = edit.getPemegang().getTgllhr_anak1();
				Map param1=new HashMap();
				param1.put("strTmpSPAJ", strTmpSPAJ);
				param1.put("nama", edit.getPemegang().getNama_anak1());
				param1.put("lsre_id",4);
				param1.put("tanggal_lahir", tanggal_lahir_anak1);
				param1.put("insured", 1);
				param1.put("no", 1);
				insert("insert_mst_keluarga", param1);
				//logger.info("insert mst keluarga");
			}
			
			String nama_anak2 = edit.getPemegang().getNama_anak2();
			if (!nama_anak2.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_anak2 = edit.getPemegang().getTgllhr_anak2();
				Map param1=new HashMap();
				param1.put("strTmpSPAJ", strTmpSPAJ);
				param1.put("nama", edit.getPemegang().getNama_anak2());
				param1.put("lsre_id",4);
				param1.put("tanggal_lahir", tanggal_lahir_anak2);
				param1.put("insured", 1);
				param1.put("no", 2);
				insert("insert_mst_keluarga", param1);
				//logger.info("insert mst keluarga");
			}
			
			String nama_anak3 = edit.getPemegang().getNama_anak3();
			if (!nama_anak3.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_anak3 = edit.getPemegang().getTgllhr_anak3();
				Map param1=new HashMap();
				param1.put("strTmpSPAJ", strTmpSPAJ);
				param1.put("nama", edit.getPemegang().getNama_anak3());
				param1.put("lsre_id",4);
				param1.put("tanggal_lahir", tanggal_lahir_anak3);
				param1.put("insured", 1);
				param1.put("no", 3);
				insert("insert_mst_keluarga", param1);
				//logger.info("insert mst keluarga");
			}
		}
		
		private void proc_save_suamiistri_ttg(Cmdeditbac edit, String strTmpSPAJ)throws ServletException,IOException
		{
			delete("delete.mst_keluarga", strTmpSPAJ);
			String nama_suamiistri = edit.getTertanggung().getNama_si();
			if (!nama_suamiistri.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_suamiistri = edit.getTertanggung().getTgllhr_si();
				Map param1=new HashMap();
				param1.put("strTmpSPAJ", strTmpSPAJ);
				param1.put("nama", edit.getTertanggung().getNama_si());
				param1.put("lsre_id",5);
				param1.put("tanggal_lahir", tanggal_lahir_suamiistri );
				param1.put("insured", 0);
				param1.put("no", 0);
				insert("insert_mst_keluarga", param1);
				//logger.info("insert mst keluarga");
			}
			
			String nama_anak1 = edit.getTertanggung().getNama_anak1();
			if (!nama_anak1.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_anak1 = edit.getTertanggung().getTgllhr_anak1();
				Map param1=new HashMap();
				param1.put("strTmpSPAJ", strTmpSPAJ);
				param1.put("nama", edit.getTertanggung().getNama_anak1());
				param1.put("lsre_id",4);
				param1.put("tanggal_lahir", tanggal_lahir_anak1);
				param1.put("insured", 0);
				param1.put("no", 1);
				insert("insert_mst_keluarga", param1);
				//logger.info("insert mst keluarga");
			}
			
			String nama_anak2 = edit.getTertanggung().getNama_anak2();
			if (!nama_anak2.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_anak2 = edit.getTertanggung().getTgllhr_anak2();
				Map param1=new HashMap();
				param1.put("strTmpSPAJ", strTmpSPAJ);
				param1.put("nama", edit.getTertanggung().getNama_anak2());
				param1.put("lsre_id",4);
				param1.put("tanggal_lahir", tanggal_lahir_anak2);
				param1.put("insured", 0);
				param1.put("no", 2);
				insert("insert_mst_keluarga", param1);
				//logger.info("insert mst keluarga");
			}
			
			String nama_anak3 = edit.getTertanggung().getNama_anak3();
			if (nama_anak3 == null)
			{
				nama_anak3 = "";
			}
			if (!nama_anak3.equalsIgnoreCase(""))
			{
				Date tanggal_lahir_anak3 = edit.getTertanggung().getTgllhr_anak3();
				Map param1=new HashMap();
				param1.put("strTmpSPAJ", strTmpSPAJ);
				param1.put("nama", edit.getPemegang().getNama_anak3());
				param1.put("lsre_id",4);
				param1.put("tanggal_lahir", tanggal_lahir_anak3);
				param1.put("insured", 0);
				param1.put("no", 3);
				insert("insert_mst_keluarga", param1);
				//logger.info("insert mst keluarga");
			}
		}		

		private void proc_save_noblanko(Cmdeditbac edit, String strTmpSPAJ)throws ServletException,IOException
		{
			Integer flag_worksite = edit.getDatausulan().getFlag_worksite();
			if (flag_worksite.intValue() == 1)
			{
				//no_blanko
				String no_blanko = edit.getPemegang().getMspo_no_blanko();
				if (no_blanko.equalsIgnoreCase(""))
				{
					//Calendar tgl_sekarang = Calendar.getInstance(); 
					String lca_id = edit.getAgen().getLca_id();
					//Integer inttgl1	=null;
					//Integer inttgl2 =null;
					Map data = new HashMap();
					data.put("number",new Integer(71));
					data.put("lca_id", lca_id);
					Long intIDCounter = (Long) querySingle("select_counter",data);
					
					//--------------------------------------------
					//Increase current SPAJ No by 1 and
					//update the value to MST_COUNTER table
					intIDCounter = new Long(intIDCounter.longValue()+1);
					Map param=new HashMap();
					param.put("IDCounter", intIDCounter);
					param.put("lca_id", lca_id);
					param.put("number", new Integer(71));
					//int b =update("update_counter", param);
					String blanko = FormatString.rpad("0",Long.toString(intIDCounter),7);
					//edit.getPemegang().setMspo_no_blanko(blanko);
				}	
			}
			if(edit.getCurrentUser().getLca_id().equals("58")){
				Map data = new HashMap();
				data.put("number",new Integer(71));
				data.put("lca_id", edit.getCurrentUser().getLca_id());
				Long intIDCounter = (Long) querySingle("select_counter",data);
				intIDCounter = new Long(intIDCounter.longValue()+1);
				Map param=new HashMap();
				param.put("IDCounter", intIDCounter);
				param.put("lca_id", edit.getCurrentUser().getLca_id());
				param.put("number", new Integer(71));
				int b =update("update_counter", param);
			}
			
			//
			String no_blanko = edit.getPemegang().getMspo_no_blanko();
			if (no_blanko== null) no_blanko = "";
			String nomor = no_blanko.replaceAll(" ", "").toUpperCase();
			List<DropDown> daftar = this.basDao.selectJenisForm();			
			for(DropDown d : daftar){
				if(nomor.startsWith(d.getKey())){
					this.bacDao.update_no_blanko(nomor.substring(d.getKey().length()), strTmpSPAJ, Integer.valueOf(d.getValue()));
				}
			}
			
		}
		
		private void proc_save_trans_ulink(Cmdeditbac edit, String strTmpSPAJ,Integer status_polis ,Integer v_intActionBy,Integer posisi_dok, Integer status_dok , User currentUser)throws ServletException,IOException
		{
			String keterangan_edit =null;
			//if (status_polis.intValue() == 8)
			Integer lspd_id = edit.getDatausulan().getLspd_id();
			//Integer lssa_id = edit.getHistory().getStatus_polis();
			if (lspd_id.intValue() != 1)
			//if ( lssa_id.intValue() == 8)
			{
				List transulink =  query("selecttransulink",strTmpSPAJ);
				if (transulink.size()!=0)
				{
					List invvl = edit.getInvestasiutama().getDaftarinvestasi();
					Integer indeks_investasi = edit.getInvestasiutama().getJmlh_invest();
				
					
					if ( indeks_investasi==null)
					{
						indeks_investasi=new Integer(0);
					}
					int jumlah_error = 0;
					Double jumlah_link1 = new Double(0);
					Double jumlah_link2 = new Double(0);
					if (transulink.size()!=0)
					{		
						for (int j = 0 ; j < invvl.size() ; j++){
							DetilInvestasi iv= (DetilInvestasi)invvl.get(j);
							if(iv.getMdu_jumlah1() != null) jumlah_link2 = new Double(jumlah_link2.doubleValue() + iv.getMdu_jumlah1().doubleValue());
						}
						//logger.info(transulink.size());
						for (int k = 0 ; k < transulink.size() ; k++)
						{
							TransUlink datab= (TransUlink)transulink.get(k);
							logger.info(datab.getMu_ke());
							jumlah_link1 = new Double(jumlah_link1.doubleValue() + datab.getMtu_jumlah().doubleValue());
							for (int j = 0 ; j <invvl.size() ; j++)
							{
								DetilInvestasi iv= (DetilInvestasi)invvl.get(j);
								if ( datab.getLji_id().equalsIgnoreCase(iv.getLji_id1()))
								{
									if (datab.getMtu_jumlah().doubleValue() != iv.getMdu_jumlah1().doubleValue())
									{
										jumlah_error = jumlah_error +1;
									}
								}
							}
						}
					}
					if ( jumlah_link1.doubleValue() != jumlah_link2.doubleValue() )
					{
						jumlah_error = jumlah_error +1;
					}
							
					if (jumlah_error !=0)
					{
						keterangan_edit	= "EDIT FUND SPAJ";	
						edit.getDatausulan().setKeterangan_fund(keterangan_edit);

						try {
							email.send(false, props.getProperty("admin.ajsjava"), new String[] {props.getProperty("admin.yusuf")}, null, null, 
									"Spaj ini sudah di fund, tetapi telah dilakukan perubahan nilai dari fund tersebut untuk no spaj " + strTmpSPAJ + " oleh "+ currentUser.getName() + " ["+currentUser.getDept()+"]",
									strTmpSPAJ, null);
						} catch (MailException e1) {
							logger.error("ERROR :", e1);
						} catch (MessagingException e1) {
							logger.error("ERROR :", e1);
						}
						
					}else{
						keterangan_edit	= "EDIT SPAJ";	
						edit.getDatausulan().setKeterangan_fund(keterangan_edit);
					}
				}else{
					keterangan_edit	= "EDIT SPAJ";	
					edit.getDatausulan().setKeterangan_fund(keterangan_edit);
				}
				
				uwDao.insertMstPositionSpaj(v_intActionBy.toString(), keterangan_edit, strTmpSPAJ, 0);
				
			}
		}

		
		public Cmdeditbac editagenspaj(Object cmd, User User) throws ServletException,IOException
		{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Cmdeditbac edit= (Cmdeditbac) cmd;
			String strTmpSPAJ=null;
			try {
				String strAgentBranch = null;
				String strBranch = null;
				String strWilayah = null;
				String strRegion = null;
				String gc_strTmpBranch= "WW";
				
				Integer v_intActionBy = new Integer(Integer.parseInt(User.getLus_id()));
				String v_strregionid = edit.getAgen().getKode_regional().toUpperCase();
				String v_strAgentId = edit.getAgen().getMsag_id().toUpperCase();
				String v_stragentnama = edit.getAgen().getMcl_first().toUpperCase();
					
				//----------------------------------
				// Get the Agent Branch information
				if  (v_strAgentId.equalsIgnoreCase("000000"))
				{
				       strBranch = FormatString.rpad("0",(v_strregionid.substring(0,2)),2);
				       strWilayah = FormatString.rpad("0",(v_strregionid.substring(2,4)),2);
				       strRegion = FormatString.rpad("0",(v_strregionid.substring(4,6)),2);
				       strAgentBranch = strBranch.concat("00");
				}else{
						Map m = new HashMap();
						m.put("kodeagen", v_strAgentId);
						Map data = (HashMap) querySingle("select.regionalagen",m);
						if (data!=null)
						{		
							 strBranch = (String)data.get("STRBRANCH");
						     strWilayah = (String)data.get("STRWILAYAH");
						     strRegion = (String)data.get("STRREGION");
						     strAgentBranch = (String)data.get("STRAGENTBRANCH");
						}
					}
				edit.getAgen().setLca_id(strBranch);
				edit.getAgen().setLwk_id(strWilayah);
				edit.getAgen().setLsrg_id(strRegion);
				edit.getAgen().setKode_regional(strAgentBranch);
				strTmpSPAJ = edit.getAgen().getReg_spaj();
			
				if (v_strAgentId.equalsIgnoreCase("000000"))
				{
					Map param1=new HashMap();
					param1.put("strTmpSPAJ", strTmpSPAJ);
					param1.put("v_stragentnama", v_stragentnama);
					//insert("insert.mst_agent_temp", param1);
					//logger.info("insert kalau agent baru");
					int rowupdated=update("update.mst_agent_temp",param1);
					if (rowupdated ==0)
					{
						insert("insert.mst_agent_temp", param1);
					}
				}
				
				//edit mst_policy
				Map param = new HashMap();
				param.put("pemegang",edit.getPemegang());
				param.put("agen",edit.getAgen());
				int rowupdated = update("update.editmst_policy", param);
				
				Integer posisi_dok =null;
				Integer status_polis =null;
				Integer status_dok = null;
				status_polis = (Integer) querySingle("selectPositionSpaj",strTmpSPAJ);
				Map dataa = (HashMap) querySingle("selectPositiondok",strTmpSPAJ);
				if (dataa!=null)
				{		
					posisi_dok = new Integer(Integer.parseInt(dataa.get("LSPD_ID").toString()));
					status_dok = new Integer(Integer.parseInt(dataa.get("LSSP_ID").toString()));
				}
				
				edit.getDatausulan().setLspd_id(posisi_dok);
	
				Map param9=new HashMap();
				param9.put("strTmpSPAJ",strTmpSPAJ);				
				delete("delete.mst_agent_prod", param9);	
				//logger.info("delete mst_agent_prod");
							
				Map param13=new HashMap();
				param13.put("strTmpSPAJ",strTmpSPAJ);				
				delete("delete.mst_agent_comm", param13);	
				//logger.info("delete mst_agent_prod");	
							
				Map param15=new HashMap();
				param15.put("strTmpSPAJ",strTmpSPAJ);				
				delete("delete.mst_agent_ba", param15);	
				//logger.info("delete mst_agent_ba");
							
				delete("delete.mst_agent_artha",param9);
				//logger.info("delete mst_agent_artha");	
				
				//------------------------------------------------------------
				// Process Application closing and Agent Commission
				proc_save_agen(edit,strTmpSPAJ, v_strAgentId,strAgentBranch,strBranch,strWilayah,strRegion,v_strregionid);

//				edit  address billing
				proc_save_addr_bill(edit,strTmpSPAJ);
				String keterangan_edit	= "EDIT KODE AGEN.";
				uwDao.insertMstPositionSpaj(v_intActionBy.toString(), keterangan_edit, strTmpSPAJ, 0);				
				
														
			} catch (Exception e) {
				strTmpSPAJ="";
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				logger.error("ERROR :", e);
			}
				
			//return strTmpSPAJ;
			edit.getPemegang().setReg_spaj(strTmpSPAJ);
			return edit;
			
		}	
		
	public Kesehatan insertkesehatan(Object cmd, User currentUser) throws ServletException,IOException
	{
		Kesehatan edit= (Kesehatan) cmd;
		try {
			int rowupdate = update("update_mst_non_medical",edit);
			if (rowupdate == 0)
			{
				insert("insert_mst_non_medical",edit);
			}
		} catch (Exception e) {
			edit.setMsnm_keterangan("");
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			logger.error("ERROR :", e);
		}

		return edit;
	}
	
	public Cmdeditbac editnik(Object cmd, User User) throws ServletException,IOException
	{
		Cmdeditbac edit= (Cmdeditbac) cmd;
		String strTmpSPAJ=null;
		try {
			Integer v_intActionBy = new Integer(Integer.parseInt(User.getLus_id()));

			Integer	flag_jenis_plan = edit.getDatausulan().getFlag_jenis_plan();
			if (flag_jenis_plan == null)
			{
				flag_jenis_plan = new Integer (0);
			}
			Integer	flag_as = edit.getDatausulan().getFlag_as();

			strTmpSPAJ = edit.getPemegang().getReg_spaj();

			proc_save_worksite_flag(edit,strTmpSPAJ);
			
			Integer flag_worksite = edit.getDatausulan().getFlag_worksite();
			if (flag_worksite.intValue() == 1)
			{
				Map param = new HashMap();
				param.put("pemegang",edit.getPemegang());
				param.put("datausulan",edit.getDatausulan());
				int rowupdated = update("update_mst_perusahaan", param);
			}	
				
			Integer posisi_dok =null;
			Integer status_polis =null;
			Integer status_dok = null;
			status_polis = (Integer) querySingle("selectPositionSpaj",strTmpSPAJ);
			Map dataa = (HashMap) querySingle("selectPositiondok",strTmpSPAJ);
			if (dataa!=null)
			{		
				posisi_dok = new Integer(Integer.parseInt(dataa.get("LSPD_ID").toString()));
				status_dok = new Integer(Integer.parseInt(dataa.get("LSSP_ID").toString()));
			}
			edit.getHistory().setStatus_polis(status_polis);
			edit.getDatausulan().setLspd_id(posisi_dok);

			Map param23=new HashMap();
			param23.put("strTmpSPAJ",strTmpSPAJ);				
			delete("delete.mst_emp", param23);	
							
			//produk karyawan, nik
			if (flag_as.intValue()==2)
			{
				proc_save_karyawan(edit, strTmpSPAJ,v_intActionBy );
			}
													
		} catch (Exception e) {
			strTmpSPAJ="";
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			logger.error("ERROR :", e);
		}
		//return strTmpSPAJ;
	//	TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		edit.getPemegang().setReg_spaj(strTmpSPAJ);
		return edit;
		
		
	}	
	
}