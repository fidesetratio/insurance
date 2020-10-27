package com.ekalife.elions.process;
 

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Biayainvestasi;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilInvestasi;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.MedQuest_tambah;
import com.ekalife.elions.model.MedQuest_tambah2;
import com.ekalife.elions.model.MedQuest_tambah3;
import com.ekalife.elions.model.MedQuest_tambah4;
import com.ekalife.elions.model.MedQuest_tambah5;
import com.ekalife.elions.model.MstQuestionAnswer;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.PesertaPlus_mix;
import com.ekalife.elions.model.ReffBii;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.process.upload.UploadSetDataTemp;
import com.ekalife.elions.process.upload.ValidateUploadTM;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_replace;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentDao;

import produk_asuransi.n_prod;

public class UploadSpajTemp  extends ParentDao {    
		protected final Log logger = LogFactory.getLog( getClass() );
		
		private ValidateUploadTM validateUploadTM;
		private UploadSetDataTemp uploadSetDataTemp;		
		public void setValidateUploadTM(ValidateUploadTM validateUploadTM) {this.validateUploadTM = validateUploadTM;}
		public void setUploadSetDataTemp(UploadSetDataTemp uploadSetDataTemp) {this.uploadSetDataTemp = uploadSetDataTemp;}
		SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
		SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
		SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMyyyy");
		

public List<Map> terimaDataXls(ArrayList<List> SpajExcelList, User user, String company, String lca)  {
		List<Map> pesanList = new ArrayList<Map>();
		List listdata = new ArrayList();
		
		ReffBii reffbii=new ReffBii();
		String err = "";
		String nama_produk=null;

		Integer tanggal=new Integer(0);
		Integer bulan=new Integer(0);
		Integer tahun=new Integer(0);
		Integer tanggalEd=new Integer(0);
		Integer bulanEd=new Integer(0);
		Integer tahunEd=new Integer(0);
		Integer tanggal0=new Integer(0);
		Integer bulan0=new Integer(0);
		Integer tahun0=new Integer(0);
		Integer tanggal1=new Integer(0);
		Integer bulan1=new Integer(0);
		Integer tahun1=new Integer(0);
		Integer tanggal2=new Integer(0);
		Integer bulan2=new Integer(0);
		Integer tahun2=new Integer(0);	
		Integer tanggal3=new Integer(0);
		Integer bulan3=new Integer(0);
		Integer tahun3=new Integer(0);	
		Integer tanggal4=new Integer(0);
		Integer bulan4=new Integer(0);
		Integer umurPp=new Integer(0);
		Integer umurTtg=new Integer(0);
		Integer payperiod=new Integer(0);
		Integer insperiod=new Integer(0);
		Integer umurTt1=new Integer(0);
		Integer umurTt2=new Integer(0);
		Integer umurTt3=new Integer(0);
		Integer umurTt4=new Integer(0);
		Integer umurTt5=new Integer(0);
		Integer jumlahRider=new Integer(0);
		String reff_bank=null;
		String agent_bank=null;
		Date ttTambahan1_bod= null;;
		Date ttTambahan2_bod = null;
		Date ttTambahan3_bod= null;;
		Date ttTambahan4_bod= null;;
		Date ttTambahan5_bod= null;;
		Class aClass;				
		n_prod produk = new n_prod();
		n_prod produk1 = new n_prod();
		ValidateUploadTM validatorTM=new ValidateUploadTM();
		
		//=======Mulai Perulangan===========================//
		for(int i = 1 ; i < SpajExcelList.size() ; i++){			
			if(!SpajExcelList.get(i).get(0).toString().equals("")){
				validateUploadTM = new ValidateUploadTM();	
				uploadSetDataTemp = new UploadSetDataTemp();
				ArrayList<Datarider> dtrd = new ArrayList<Datarider>();
				ArrayList<Benefeciary> benef = new ArrayList<Benefeciary>();
				ArrayList<PesertaPlus_mix>psrt = new ArrayList<PesertaPlus_mix>();
				Agen excelListagen = new Agen();	
				err = "";
				Cmdeditbac excelList = new Cmdeditbac();
				Pemegang excelListPp = new Pemegang();
				AddressBilling excelListAddrBilling = new AddressBilling();
				Datausulan excelListDatausulan = new Datausulan();
				Tertanggung excelListTtg = new Tertanggung();					
				PesertaPlus_mix peserta = new PesertaPlus_mix();
				Account_recur rekAutodebet = new Account_recur();
				PembayarPremi excelListpayer = new PembayarPremi();
				InvestasiUtama excelListInvestasi = new InvestasiUtama();
				Benefeciary excelListBenefeciary = new Benefeciary();
				Rekening_client excelListrekClient = new Rekening_client(); 
				SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				Calendar cal = Calendar.getInstance();	
				int jumlahPeserta = 0, jumlahRiderKesehatan = 0;		  
				 
				//==========Retrieve && set Value=======================//
				if("04,05,06,07,08,10,11,15,16".indexOf(lca)>=0){//HCP & Smile Medical(Arco,SMP,REDBERRY,SIP)
					err=validateUploadTM.validateNilaiDefault(SpajExcelList,i); 
				    if("".equals(err)){
				    	//retrieve & setValue
						if(lca.equals("04")){
							excelListPp.setCampaign_id(2);
						}else if(lca.equals("05")){
							excelListPp.setCampaign_id(3);
						}else if(lca.equals("06")){
							excelListPp.setCampaign_id(5);
						}else if(lca.equals("07")){
							excelListPp.setCampaign_id(6);
						}else if(lca.equals("08")){
							excelListPp.setCampaign_id(7);
						}else if(lca.equals("10")){
							excelListPp.setCampaign_id(9);
						}else if(lca.equals("11")){
							excelListPp.setCampaign_id(10);
						}else if(lca.equals("15")){
							excelListPp.setCampaign_id(12);
						}else if(lca.equals("16")){
							excelListPp.setCampaign_id(13);
						}else{
							excelListPp.setCampaign_id(Integer.parseInt(SpajExcelList.get(i).get(0).toString().substring(0, 1)));
						}
						
						//==========Retrieve && set Value Pemegang=======================//								
						excelListPp.setMspo_nasabah_dcif(SpajExcelList.get(i).get(1).toString());
						excelListPp.setMcl_first(SpajExcelList.get(i).get(2).toString());
						String no_blanko=generateNoBlanko(excelListPp.getCampaign_id().toString(),183);
						excelListPp.setMspo_no_blanko(no_blanko);
						excelListPp.setMspe_sex(Integer.parseInt(SpajExcelList.get(i).get(3).toString().substring(0, 1)));
						excelListPp.setMspe_place_birth(SpajExcelList.get(i).get(4).toString());
						excelListPp.setLside_id(Integer.parseInt(SpajExcelList.get(i).get(6).toString().substring(0, 1)));
						excelListPp.setMspe_no_identity(SpajExcelList.get(i).get(7).toString().replace(".0", ""));
						excelListPp.setMspe_sts_mrt(SpajExcelList.get(i).get(8).toString().replace(".0", ""));
						excelListPp.setLsre_id(Integer.parseInt(SpajExcelList.get(i).get(9).toString().replace(".0", "")));
						excelListPp.setAlamat_rumah(SpajExcelList.get(i).get(10).toString());
						excelListPp.setKota_rumah(SpajExcelList.get(i).get(11).toString());
						excelListPp.setKd_pos_rumah(SpajExcelList.get(i).get(12).toString().replace(".0", ""));
						if(excelListPp.getKd_pos_rumah().equals("-"))excelListPp.setKd_pos_rumah("");
						excelListPp.setAlamat_kantor(SpajExcelList.get(i).get(13).toString());
						excelListPp.setKota_kantor(SpajExcelList.get(i).get(14).toString());
						excelListPp.setKd_pos_kantor(SpajExcelList.get(i).get(15).toString().replace(".0", ""));
						if(excelListPp.getKd_pos_kantor().equals("-"))excelListPp.setKd_pos_kantor("");
						excelListPp.setTelpon_rumah(FormatString.getRightPart(SpajExcelList.get(i).get(21).toString().replace(".0", ""), "-"));
						if(excelListPp.getTelpon_rumah().startsWith("0"))excelListPp.setTelpon_rumah("");								
						excelListPp.setArea_code_rumah(FormatString.getLeftPart(SpajExcelList.get(i).get(21).toString().replace(".0", ""),"-"));								
						if("000,0000".indexOf(excelListPp.getArea_code_rumah())>=0)excelListPp.setArea_code_rumah("");
						excelListPp.setArea_code_kantor(FormatString.getLeftPart(SpajExcelList.get(i).get(22).toString().replace(".0", ""),"-"));
						excelListPp.setTelpon_kantor(FormatString.getRightPart(SpajExcelList.get(i).get(22).toString().replace(".0", ""),"-"));
						if(!excelListPp.getArea_code_kantor().isEmpty() && !excelListPp.getTelpon_kantor().isEmpty()){
							if(excelListPp.getTelpon_kantor().startsWith("0"))excelListPp.setTelpon_kantor("");	
							if("000,0000".indexOf(excelListPp.getArea_code_kantor())>=0)excelListPp.setArea_code_kantor("");
						}
						excelListPp.setNo_hp(SpajExcelList.get(i).get(23).toString().replace(".0", ""));
						excelListPp.setNo_fax(SpajExcelList.get(i).get(24).toString().replace(".0", ""));
						excelListPp.setEmail(SpajExcelList.get(i).get(26).toString());	
						excelListPp.setLscb_id(Integer.parseInt(SpajExcelList.get(i).get(139).toString().replace(".0", "")));
						excelListPp.setLus_id( Integer.parseInt(user.getLus_id()));
						excelListPp.setSpv(SpajExcelList.get(i).get(154).toString().trim());
						excelListPp.setMspe_mother(SpajExcelList.get(i).get(152).toString());
						excelListPp.setApplication_id(SpajExcelList.get(i).get(155).toString().replace(".0", "").trim());
						excelListPp.setReff_tm_id(SpajExcelList.get(i).get(156).toString().replace(".0", "").trim());
						excelListPp.setCf_job_code(SpajExcelList.get(i).get(157).toString().replace(".0", "").trim());
						excelListPp.setCf_customer_id(SpajExcelList.get(i).get(158).toString().replace(".0", "").trim());
						excelListPp.setCf_campaign_code(SpajExcelList.get(i).get(159).toString().replace(".0", "").trim());
						excelListPp.setFlag_free_pa(Integer.parseInt(SpajExcelList.get(i).get(161).toString().replace(".0", "")));
						//==========End of Retrieve && set Value Pemegang=======================//
						
						excelList.setCurrentUser(user);
						
						//==========Retrieve && set Value Address Billing(Korespondensi)=======================//				
						excelListAddrBilling.setMsap_address(SpajExcelList.get(i).get(16).toString());
						excelListAddrBilling.setKota(SpajExcelList.get(i).get(17).toString());
						excelListAddrBilling.setKota_tagih(excelListAddrBilling.getKota());
						excelListAddrBilling.setMsap_zip_code(SpajExcelList.get(i).get(18).toString().replace(".0", ""));
						if(excelListAddrBilling.getMsap_zip_code().equals("-"))excelListAddrBilling.setMsap_zip_code("");
						excelListAddrBilling.setMsap_phone1(FormatString.getRightPart(SpajExcelList.get(i).get(19).toString().replace(".0", ""),"-"));
						if(excelListAddrBilling.getMsap_phone1().startsWith("0"))excelListAddrBilling.setMsap_phone1("");
						excelListAddrBilling.setMsap_area_code1(FormatString.getLeftPart(SpajExcelList.get(i).get(19).toString().replace(".0", ""),"-"));
						if("000,0000".indexOf(excelListAddrBilling.getMsap_area_code1())>=0)excelListAddrBilling.setMsap_area_code1("");
						excelListAddrBilling.setNo_hp(SpajExcelList.get(i).get(20).toString().replace(".0", ""));
						excelListAddrBilling.setMsap_contact(excelListPp.getMcl_first());
						if(excelListAddrBilling.getMsap_address().trim().equalsIgnoreCase(excelListPp.getAlamat_rumah().trim())){
							excelListAddrBilling.setTagih("2");
						}else if(excelListAddrBilling.getMsap_address().trim().equalsIgnoreCase(excelListPp.getAlamat_kantor().trim())){
							excelListAddrBilling.setTagih("3");
						}else{
							excelListAddrBilling.setTagih("1");
						}
						//==========End Of Retrieve && set Value Address Billing(Korespondensi)=======================//	
						
						//==========Retrieve && set Value Tertanggung=======================//
						excelListTtg.setMcl_first(SpajExcelList.get(i).get(27).toString());
						excelListTtg.setMspe_sex(Integer.parseInt(SpajExcelList.get(i).get(28).toString().substring(0, 1)));
						excelListTtg.setMspe_place_birth(SpajExcelList.get(i).get(29).toString());
						excelListTtg.setLside_id(Integer.parseInt(SpajExcelList.get(i).get(31).toString().substring(0, 1)));
						excelListTtg.setMspe_no_identity(SpajExcelList.get(i).get(32).toString().replace(".0", ""));
						excelListTtg.setLsre_id(Integer.parseInt(SpajExcelList.get(i).get(33).toString().replace(".0", "")));;					
						excelListTtg.setAlamat_rumah(SpajExcelList.get(i).get(34).toString());	
						excelListTtg.setKota_rumah(SpajExcelList.get(i).get(35).toString());	
						excelListTtg.setKd_pos_rumah(SpajExcelList.get(i).get(36).toString().replace(".0", "").trim());
						if(excelListTtg.getKd_pos_rumah().equals("-"))excelListPp.setKd_pos_rumah("");
						excelListTtg.setKd_pos_kantor(SpajExcelList.get(i).get(39).toString().replace(".0", "").trim());	
						if(excelListTtg.getKd_pos_kantor().equals("-"))excelListPp.setKd_pos_kantor("");
						excelListTtg.setAlamat_kantor(SpajExcelList.get(i).get(37).toString());	
						excelListTtg.setKota_kantor(SpajExcelList.get(i).get(38).toString());
						excelListTtg.setMste_no_vacc(SpajExcelList.get(i).get(160).toString().replace(".0", "").trim());
						excelListDatausulan.setMste_insured_no(1);

						//==========End of Retrieve && set Value Tertanggung=======================//
						 
						//==========Retrieve && set Value Agen and Regional Polis=======================//
						excelListagen.setMsag_id(SpajExcelList.get(i).get(149).toString().replace(".0", ""));
						if(excelListagen.getMsag_id() == null)excelListagen.setMsag_id("");							     
						if(!"".equals(excelListagen.getMsag_id())){
							excelListagen = bacDao.select_detilagen3(excelListagen.getMsag_id());									
							if(excelListagen!=null){
								excelListPp.setMsag_id(excelListagen.getMsag_id());
								excelListPp.setLca_id(excelListagen.getLca_id());
								excelListPp.setLwk_id(excelListagen.getLwk_id());
								excelListPp.setLsrg_id(excelListagen.getLsrg_id());
								excelListAddrBilling.setLca_id(excelListagen.getLca_id());
								excelListAddrBilling.setLwk_id(excelListagen.getLwk_id());
								excelListAddrBilling.setLsrg_id(excelListagen.getLsrg_id());
							}
						}
						 
						//========== End Of Retrieve && set Value Agen and Regional Polis=======================//
						 
						//==========Retrieve && set Value Data Usulan=======================//
						Integer lsbs_id_utama=Integer.parseInt(SpajExcelList.get(i).get(136).toString().substring(0, 3));
						Integer lsdbs_number_utama=Integer.parseInt(SpajExcelList.get(i).get(136).toString().substring(4));
					    excelListDatausulan.setLsbs_id(lsbs_id_utama); 
					    excelListDatausulan.setLsdbs_number(lsdbs_number_utama);
					    excelListDatausulan.setKurs_premi("01");
					    excelListDatausulan.setLscb_id(Integer.parseInt(SpajExcelList.get(i).get(139).toString().replace(".0", "")));
					    excelListDatausulan.setMspo_no_kerjasama(SpajExcelList.get(i).get(153).toString().trim());//							    
					    excelListDatausulan.setMste_flag_cc(Integer.parseInt(SpajExcelList.get(i).get(140).toString().replace(".0", "")));							    
					    String kode_produk=Integer.toString(excelListDatausulan.getLsbs_id());
						nama_produk="produk_asuransi.n_prod_"+kode_produk;
						try {
							aClass = Class.forName( nama_produk );
							 produk = (n_prod)aClass.newInstance();
							 produk.setSqlMap(uwDao.getSqlMapClient());
							 
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e1);
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e);
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e);
						}
						
						produk.ii_pmode=excelListDatausulan.getLscb_id();
					    produk.ii_bisnis_id=lsbs_id_utama;
					    produk.ii_bisnis_no=lsdbs_number_utama;
					    produk.is_kurs_id=excelListDatausulan.getKurs_premi();
						
					    try{
					    	Date sysdate = commonDao.selectSysdateTrunc();				    	
							Date pp_bod = df.parse(SpajExcelList.get(i).get(5).toString());	
							Date tt_bod = df.parse(SpajExcelList.get(i).get(30).toString());
							Date bdate = df.parse(SpajExcelList.get(i).get(147).toString());
							Date tgl_recur=df.parse(SpajExcelList.get(i).get(147).toString());	
							excelListDatausulan.setMspr_beg_date(bdate);
							excelListPp.setMspe_date_birth(pp_bod);
							excelListPp.setMste_tgl_recur(tgl_recur);
							excelListTtg.setMspe_date_birth(tt_bod);
							excelListPp.setTgl_upload(sysdate);
							if(bdate!=null){//tahun untuk begdate polis							
								tahun = Integer.parseInt(sdfYear.format(excelListDatausulan.getMspr_beg_date()));
								bulan = Integer.parseInt(sdfMonth.format(excelListDatausulan.getMspr_beg_date()));
								tanggal = Integer.parseInt(sdfDay.format(excelListDatausulan.getMspr_beg_date()));
								produk.of_set_begdate(tahun,bulan,tanggal);	
								
								tanggalEd = new Integer(produk.idt_end_date.getTime().getDate());
								bulanEd = new Integer(produk.idt_end_date.getTime().getMonth()+1);
								tahunEd = new Integer(produk.idt_end_date.getTime().getYear()+1900);
								
								String tgl_end = Integer.toString(tanggalEd.intValue());
								String bln_end = Integer.toString(bulanEd.intValue());
								String thn_end = Integer.toString(tahunEd.intValue());
								if ((tgl_end.equalsIgnoreCase("0")==true) || (bln_end.equalsIgnoreCase("0")==true) || (thn_end.equalsIgnoreCase("0")==true))
								{
									excelListDatausulan.setMste_end_date(null);
								}else{
									String tanggal_end_date = FormatString.rpad("0",tgl_end,2)+"/"+FormatString.rpad("0",bln_end,2)+"/"+thn_end;
									excelListDatausulan.setMspr_end_date(defaultDateFormat.parse(tanggal_end_date));
								}
								
								cal.setTime(excelListDatausulan.getMspr_end_date());
								cal.add(Calendar.MONTH, -1);
								cal.add(Calendar.DATE, 1);
								Date end_pay=cal.getTime();						
								excelListDatausulan.setMspr_end_pay(end_pay);
								
							}
							payperiod=produk.of_get_payperiod();
							if(lsbs_id_utama==183 || lsbs_id_utama==189 || lsbs_id_utama==195 || lsbs_id_utama==204 || lsbs_id_utama==214 ||  lsbs_id_utama==221 ||  lsbs_id_utama==225 ){
								insperiod = 1;
								excelListDatausulan.setMspr_end_pay(null);
							}else{
								insperiod = produk.of_get_conperiod(lsbs_id_utama);
								if(lsbs_id_utama==212){									
									if(payperiod < 8 || insperiod < 8)
									{	
										err = "Untuk produk SMART Life Care Plus, masa Asuransi yg diperbolehkan hanya 8-15 Tahun.";
									}
								
								}
							}
							excelListDatausulan.setMspo_pay_period(payperiod);
							excelListDatausulan.setMspo_ins_period(insperiod);
							
							//0 untuk sysdate
							 tahun0 = Integer.parseInt(sdfYear.format(sysdate));
							 bulan0 = Integer.parseInt(sdfMonth.format(sysdate));
							 tanggal0 = Integer.parseInt(sdfDay.format(sysdate));
							
							f_hit_umur umr = new f_hit_umur();
							
							if(pp_bod!=null){//1 untuk pemegang polis
								tahun1 = Integer.parseInt(sdfYear.format(excelListPp.getMspe_date_birth()));
								bulan1 = Integer.parseInt(sdfMonth.format(excelListPp.getMspe_date_birth()));
								tanggal1 = Integer.parseInt(sdfDay.format(excelListPp.getMspe_date_birth()));
								umurPp=umr.umur(tahun1, bulan1, tanggal1, tahun, bulan, tanggal);
								excelListDatausulan.setLi_umur_pp(umurPp);
								excelListPp.setMste_age(umurPp);
								produk.of_set_usia_pp(umurPp);
							}
							
							if(tt_bod!=null){ //2 untuk tertanggung utama 
								 tahun2 = Integer.parseInt(sdfYear.format(excelListTtg.getMspe_date_birth()));
								 bulan2 = Integer.parseInt(sdfMonth.format(excelListTtg.getMspe_date_birth()));
								 tanggal2 = Integer.parseInt(sdfDay.format(excelListTtg.getMspe_date_birth()));
								 umurTtg=umr.umur(tahun2, bulan2, tanggal2, tahun, bulan, tanggal);
								 int hari=umr.hari(tahun2, bulan2, tanggal2, tahun, bulan, tanggal);
								 if("183,189,195,204,214,221,225".indexOf(lsbs_id_utama.toString())>-1 && hari>15 && umurTtg == 0 ){
									 umurTtg = 1;
								 }
								 excelListDatausulan.setLi_umur_ttg(umurTtg);
								 excelListTtg.setMste_age(umurTtg);
								 produk.of_set_usia_tt(umurTtg);
							
							}					
					    }catch (Exception e) {
							logger.error("ERROR :", e);
							TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
							Map<String,String> map = new HashMap<String, String>();
							map.put("sts", "FAILED");
							map.put("msg", "FAILED:Format Tanggal Ada Yang salah!!!" +SpajExcelList.get(i).get(2).toString());
							pesanList.add(map);		
							return pesanList;
						}				    
					    //--FIX ME
					    if("183,189,195,204,221".indexOf(lsbs_id_utama.toString())>-1){
					    	produk.ii_age=produk.ii_usia_tt;
					    	produk.of_hit_premi();
					    	Double premi=produk.idec_premi;
					    	Double up=produk.idec_up;
					    	excelListDatausulan.setMspr_tsi(up);
					    	excelListDatausulan.setMspr_premium(premi);
					    	excelListDatausulan.setMspo_provider(2);
					    	err=err+produk.of_check_usia(tahun1, bulan1, tanggal1, tahun, bulan, tanggal, 1, lsbs_id_utama);
					    	
					    }			   
					    //==========End of Retrieve && set Value Data Usulan=======================//
							    
					    //PROSES RIDER
					    String kode_rider=SpajExcelList.get(i).get(137).toString();			     
						if(!kode_rider.equals("")){
							String[] dataRider=kode_rider.split(";");
							List<String> listRider = Arrays.asList(dataRider);				 
							int tahun_rider,bulan_rider,tanggal_rider;String nama_produk_rider=null;

							//cek jumlah rider kesehatan dan jumlah peserta
							for(int k=0; k<listRider.size(); k++){
								String kode=listRider.get(k);
								Integer lsbs_id_rider=Integer.parseInt(kode.substring(0, 3));
								Integer lsdbs_number_rider=Integer.parseInt(kode.substring(4));
								if("819,820,823,825,826,831,832,833,844".indexOf(lsbs_id_rider.toString())>-1){
									if("183,189,195,204,221".indexOf(lsbs_id_utama.toString())<=-1){
										String namePesertaTambahan1=SpajExcelList.get(i).get(27).toString();
										if(!namePesertaTambahan1.equals("")){									    		    
											jumlahPeserta=jumlahPeserta+1;				    	
										}										    	
									} 
									jumlahRiderKesehatan=jumlahRiderKesehatan+1; 										 
								}	
							}
					    	if("183,189,195,204,221".indexOf(lsbs_id_utama.toString())>-1){
					    		String namePesertaTambahan2=SpajExcelList.get(i).get(56).toString();
					    		String namePesertaTambahan3=SpajExcelList.get(i).get(76).toString();
					    		String namePesertaTambahan4=SpajExcelList.get(i).get(96).toString();
					    		String namePesertaTambahan5=SpajExcelList.get(i).get(116).toString();
					    		String namePesertaTambahan6=SpajExcelList.get(i).get(136).toString();
					    		if(!namePesertaTambahan2.equals("")) jumlahPeserta=jumlahPeserta+1;
					    		if(!namePesertaTambahan3.equals("")) jumlahPeserta=jumlahPeserta+1;
					    		if(!namePesertaTambahan4.equals("")) jumlahPeserta=jumlahPeserta+1;
					    		if(!namePesertaTambahan5.equals("")) jumlahPeserta=jumlahPeserta+1;
					    		if(!namePesertaTambahan6.equals("")) jumlahPeserta=jumlahPeserta+1;
					    	}

					    	if( jumlahRiderKesehatan!=jumlahPeserta-1 )err="Jumlah rider kesehatan  tidak sama dengan jumlah peserta.Harap lengkapi data, ";

					    	for(int j=0; j<listRider.size(); j++){
					    		Datarider dtr=new Datarider();
					    		String kode=listRider.get(j);
					    		Integer lsbs_id_rider=Integer.parseInt(kode.substring(0, 3));
					    		Integer lsdbs_number_rider=Integer.parseInt(kode.substring(4));							
					    		Integer[]unit = null;
					    		Integer[] klas=null; 
					    		Double premi = null;
					    		Double up = null;
					    		Double rate_rider = null;

					    		try {							  
					    			nama_produk_rider="produk_asuransi.n_prod_"+lsbs_id_rider;
					    			aClass = Class.forName( nama_produk_rider);
					    			produk1 = (n_prod)aClass.newInstance();	
					    			produk1.setSqlMap(uwDao.getSqlMapClient());
					    			produk1.ii_bisnis_id=lsbs_id_rider;
					    			produk1.ii_bisnis_no=lsdbs_number_rider;
					    			produk1.ii_usia_pp=excelListDatausulan.getLi_umur_pp();
			    			 		tahun_rider = Integer.parseInt(sdfYear.format(excelListDatausulan.getMspr_beg_date()));
				    			 	bulan_rider= Integer.parseInt(sdfMonth.format(excelListDatausulan.getMspr_beg_date()));
				    			 	tanggal_rider = Integer.parseInt(sdfDay.format(excelListDatausulan.getMspr_beg_date()));
				    			 	produk1.of_set_begdate(tahun_rider,bulan_rider,tanggal_rider);	
				    			 	dtr.setMspr_beg_date(excelListDatausulan.getMspr_beg_date());	
				    			 	dtr.setLsbs_id(lsbs_id_rider);
					    			dtr.setLsdbs_number(lsdbs_number_rider);
					    			//unit[j]=excelListDatausulan.getMspr_unit();
					    			//klas[j]=excelListDatausulan.getMspr_class();
					    			//if(unit[j]==null)unit[j]=0;
					    			//if(klas[j]==null)klas[j]=0;

							    	if("819,820,823,825,826,831,832,833,844".indexOf(lsbs_id_rider.toString())>-1){										
							    		String basic;
							    		String spouse;
							    		String child1;
							    		String child2;
							    		String child3;
							    		String child4;

							    		Date sysdate = commonDao.selectSysdateTrunc();	
							    		f_hit_umur umr = new f_hit_umur();
							    		int hub=0;
							    		int jenis = 0;
							    		basic = uwDao.selectBasicName(lsbs_id_rider, lsdbs_number_rider);
							    		if(basic != null){
							    			jenis = 0;											
							    			ttTambahan1_bod = df.parse(SpajExcelList.get(i).get(30).toString());
							    			tahun2 = Integer.parseInt(sdfYear.format(excelListTtg.getMspe_date_birth()));
							    			bulan2 = Integer.parseInt(sdfMonth.format(excelListTtg.getMspe_date_birth()));
							    			tanggal2 = Integer.parseInt(sdfDay.format(excelListTtg.getMspe_date_birth()));
							    			umurTt1=umr.umur(tahun2, bulan2, tanggal2, tahun, bulan, tanggal);
							    			produk1.ii_usia_tt=umurTt1;
							    			produk1.ii_age=produk1.ii_usia_tt;
							    			///err=produk1.of_check_usia_kesehatan(0, 1, tahun2, bulan2, tanggal2, tahun, bulan, tanggal, 1, lsbs_id_rider, lsdbs_number_rider);
							    		}
							    		spouse = uwDao.selectJenisRider(lsbs_id_rider, lsdbs_number_rider, "TERTANGGUNG II)");
							    		if(spouse != null){
							    			jenis = 1;
							    			dtr.setJenis(jenis);
							    			ttTambahan2_bod = df.parse(SpajExcelList.get(i).get(58).toString());
							    			tahun2 = Integer.parseInt(sdfYear.format(ttTambahan2_bod));
							    			bulan2 = Integer.parseInt(sdfMonth.format(ttTambahan2_bod));
							    			tanggal2 = Integer.parseInt(sdfDay.format(ttTambahan2_bod));
							    			umurTt2=umr.umur(tahun2, bulan2, tanggal2, tahun, bulan, tanggal);
							    			produk1.ii_usia_tt=umurTt2;
							    			produk1.ii_age=produk1.ii_usia_tt;
							    			hub=(Integer.parseInt(SpajExcelList.get(i).get(59).toString().substring(0, 1)));														
							    			err=produk1.of_check_usia_kesehatan(1, hub, tahun2, bulan2, tanggal2, tahun, bulan, tanggal, 1, lsbs_id_rider, lsdbs_number_rider);
							    		}
							    		child1 = uwDao.selectJenisRider(lsbs_id_rider, lsdbs_number_rider, "TERTANGGUNG III)");
							    		if(child1 != null){
							    			jenis = 2;
							    			dtr.setJenis(jenis);
							    			ttTambahan3_bod = df.parse(SpajExcelList.get(i).get(78).toString());
							    			tahun2 = Integer.parseInt(sdfYear.format(ttTambahan3_bod));
							    			bulan2 = Integer.parseInt(sdfMonth.format(ttTambahan3_bod));
							    			tanggal2 = Integer.parseInt(sdfDay.format(ttTambahan3_bod));
							    			umurTt3=umr.umur(tahun2, bulan2, tanggal2, tahun, bulan, tanggal);
							    			produk1.ii_usia_tt=umurTt3;
							    			produk1.ii_age=produk1.ii_usia_tt;
							    			hub=(Integer.parseInt(SpajExcelList.get(i).get(79).toString().substring(0, 1)));
							    			err=produk1.of_check_usia_kesehatan(1, hub, tahun2, bulan2, tanggal2, tahun, bulan, tanggal, 1, lsbs_id_rider, lsdbs_number_rider);
							    		}
					    				child2 = uwDao.selectJenisRider(lsbs_id_rider, lsdbs_number_rider, "TERTANGGUNG IV)");
					    				if(child2 != null){
					    					jenis = 3;
					    					dtr.setJenis(jenis);
					    					ttTambahan4_bod = df.parse(SpajExcelList.get(i).get(98).toString());
					    					tahun2 = Integer.parseInt(sdfYear.format(ttTambahan4_bod));
					    					bulan2 = Integer.parseInt(sdfMonth.format(ttTambahan4_bod));
					    					tanggal3 = Integer.parseInt(sdfDay.format(ttTambahan4_bod));
					    					umurTt4=umr.umur(tahun2, bulan2, tanggal2, tahun, bulan, tanggal);
					    					produk1.ii_usia_tt=umurTt4;
					    					produk1.ii_age=produk1.ii_usia_tt;
					    					hub=(Integer.parseInt(SpajExcelList.get(i).get(99).toString().substring(0, 1)));
					    					err=produk1.of_check_usia_kesehatan(1, hub, tahun2, bulan2, tanggal2, tahun, bulan, tanggal, 1, lsbs_id_rider, lsdbs_number_rider);
					    				}
					    				child3 = uwDao.selectJenisRider(lsbs_id_rider, lsdbs_number_rider, "TERTANGGUNG V)");
					    				if(child3 != null){
					    					jenis = 4;
					    					ttTambahan5_bod = df.parse(SpajExcelList.get(i).get(118).toString());
					    					tahun2 = Integer.parseInt(sdfYear.format(ttTambahan5_bod));
					    					bulan2 = Integer.parseInt(sdfMonth.format(ttTambahan5_bod));
					    					tanggal2 = Integer.parseInt(sdfDay.format(ttTambahan5_bod));
					    					umurTt5=umr.umur(tahun2, bulan2, tanggal2, tahun, bulan, tanggal);
					    					produk1.ii_usia_tt=umurTt5;
					    					produk1.ii_age=produk1.ii_usia_tt;
					    					hub=(Integer.parseInt(SpajExcelList.get(i).get(119).toString().substring(0, 1)));
					    					err=produk1.of_check_usia_kesehatan(1, hub, tahun2, bulan2, tanggal2, tahun, bulan, tanggal, 1, lsbs_id_rider, lsdbs_number_rider);
					    				}
					    				child4 = uwDao.selectJenisRider(lsbs_id_rider, lsdbs_number_rider, "TERTANGGUNG VI)");
					    				if(child4 != null){
					    					jenis = 5;
					    					dtr.setJenis(jenis);
					    					Date ttTambahan6_bod = df.parse(SpajExcelList.get(i).get(138).toString());
					    					tahun2 = Integer.parseInt(sdfYear.format(excelListTtg.getMspe_date_birth()));
					    					bulan2 = Integer.parseInt(sdfMonth.format(excelListTtg.getMspe_date_birth()));
					    					tanggal3= Integer.parseInt(sdfDay.format(excelListTtg.getMspe_date_birth()));
					    					Integer umurTt6=umr.umur(tahun2, bulan2, tanggal, tahun, bulan, tanggal);
					    					produk1.ii_usia_tt=umurTt6;
					    					produk1.ii_age=produk1.ii_usia_tt;
					    					hub=(Integer.parseInt(SpajExcelList.get(i).get(139).toString().substring(0, 1)));
					    					err=produk1.of_check_usia_kesehatan(1, hub, tahun2, bulan2, tanggal2, tahun, bulan, tanggal, 1, lsbs_id_rider, lsdbs_number_rider);
					    				}
					    				dtr.setJenis(jenis);
					    			}else{
					    				produk1.ii_usia_tt=excelListDatausulan.getLi_umur_ttg();
					    				produk1.ii_age=produk1.ii_usia_tt;
					    			}
							    	if(produk1.ii_age==0)produk1.ii_age=1;
							    	if(produk1.ii_usia_tt==0)produk1.ii_usia_tt=1;

							    	produk1.is_kurs_id=excelListDatausulan.getKurs_premi();										
							    	//ENDATE DAN PAYDATE RIDER								
							    	produk1.wf_set_premi(tahun.intValue(),bulan.intValue(),tanggal.intValue(),excelListDatausulan.getLscb_id().intValue(),tahunEd.intValue(),bulanEd.intValue(),tanggalEd.intValue(),insperiod.intValue(),produk.flag_jenis_plan,produk1.ii_age,payperiod.intValue(),produk.flag_cerdas_siswa, excelListDatausulan.getLi_umur_pp().intValue(),produk1.ii_bisnis_id,produk1.ii_bisnis_no);
							    	Integer tanggal_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getDate());
							    	Integer bulan_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getMonth()+1);
							    	Integer tahun_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getYear()+1900);
							    	Integer tanggal5= new Integer(produk.idt_end_date.getTime().getDate());
							    	Integer bulan5= new Integer(produk.idt_end_date.getTime().getMonth()+1);
							    	Integer tahun5= new Integer(produk.idt_end_date.getTime().getYear()+1900);											
							    	String tgl_end = Integer.toString(tanggal5.intValue());
							    	String bln_end = Integer.toString(bulan5.intValue());
							    	String thn_end = Integer.toString(tahun5.intValue());	
							    	String tgl_akhir_polis1=null;
							    	if(produk1.ii_bisnis_id==829){
							    		tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal5.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan5.intValue()),2)+"/"+Integer.toString(tahun5.intValue());
					    			}else{
					    				tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
					    			}
					    			if ( tgl_akhir_polis1.trim().length()!=0)
					    			{
					    				dtr.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis1));
					    			}else{
					    				dtr.setMspr_end_date(null);
					    			}
					    			Calendar cal2 = Calendar.getInstance();	
					    			cal2.setTime(dtr.getMspr_end_date());
					    			cal2.add(Calendar.MONTH, -1);
					    			cal2.add(Calendar.DATE, 1);
					    			Date end_pay_rider=cal2.getTime();

					    			if ( end_pay_rider!=null)
					    			{
					    				dtr.setMspr_end_pay(end_pay_rider);
					    			}else{
					    				dtr.setMspr_end_pay(null);
					    			}

							    			 //khusus eka sehat dulu
							    			 if("819,820,823,825,826,831,832,833,844".indexOf(lsbs_id_rider.toString())>-1){	
							    				 up=produk1.of_get_up(0, 0, 0, produk.flag_jenis_plan, produk1.ii_bisnis_id, produk1.ii_bisnis_no, 0);	
							    				 rate_rider=produk1.of_get_rate1(0, produk.flag_jenis_plan, produk1.ii_bisnis_no, produk1.ii_usia_tt, excelListPp.getMste_age());
							    				 dtr.setMspr_rate(rate_rider);
							    				 if((lsbs_id_rider==820 && (lsdbs_number_rider==1 || lsdbs_number_rider<=15 || lsdbs_number_rider>=91 || lsdbs_number_rider<=105)) ||
							    						 (lsbs_id_rider==823 && lsdbs_number_rider<=15) ||
							    						 (lsbs_id_rider==825 && lsdbs_number_rider<=15)){
							    					 premi=produk1.hit_premi_rider(dtr.getMspr_rate(), up, produk1.idec_pct_list[excelListDatausulan.getLscb_id().intValue()], premi)  ;																																
							    				 }else if((lsbs_id_rider==820 && (lsdbs_number_rider>=16 && lsdbs_number_rider<=90 && lsdbs_number_rider>=106))||
							    						 (lsbs_id_rider==823 && lsdbs_number_rider>=16) || 
							    						 (lsbs_id_rider==825 && lsdbs_number_rider>=16)){//																	
							    					 premi=produk1.hit_premi_rider(dtr.getMspr_rate(), up, produk1.idec_pct_list[excelListDatausulan.getLscb_id().intValue()], 0)  ;
							    				 }else if(lsbs_id_rider==826 || lsbs_id_rider==844){
							    					 premi=produk1.hit_premi_rider(dtr.getMspr_rate(), up, produk1.idec_pct_list[excelListDatausulan.getLscb_id().intValue()], 0) ;
							    				 }
							    				 dtr.setMspr_ins_period(produk1.li_insured);
							    			 }else{
							    				 //buat rider lain
							    			 }
							    			 dtr.setMspr_premium(premi);
							    			 dtr.setMspr_tsi(up);


							    		 } catch (ClassNotFoundException e1) {
							    			 // TODO Auto-generated catch block
							    			 logger.error("ERROR :", e1);
							    		 } catch (InstantiationException e) {
							    			 // TODO Auto-generated catch block
							    			 logger.error("ERROR :", e);
							    		 } catch (IllegalAccessException e) {
							    			 // TODO Auto-generated catch block
							    			 logger.error("ERROR :", e);
							    		 } catch (ParseException e) {
							    			 TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
							    			 Map<String,String> map = new HashMap<String, String>();
							    			 map.put("sts", "FAILED");
							    			 map.put("msg", "FAILED:Format Tanggal Ada Yang salah!!!" +SpajExcelList.get(i).get(2).toString());
							    			 pesanList.add(map);		
							    			 return pesanList;
							    		 }
							    		 dtrd.add(dtr);

							    	 }	
							    	 if(excelListPp.getFlag_free_pa()!=null){
							    		 if (excelListPp.getFlag_free_pa()==2){
							    			 Datarider dtr=new Datarider();
							    			 Integer lsbs_id_rider=824;
							    			 Integer lsdbs_number_rider=1;							
							    			 Integer unit = 0;
							    			 Integer klas=0; 
							    			 Double premi = 0.;
							    			 Double upRider = 0.;

							    			 Integer factor = 0;
							    			 Double rate_rider = 0.;

							    			 dtr.setMspr_beg_date(excelListDatausulan.getMspr_beg_date());	
							    			 dtr.setLsbs_id(lsbs_id_rider);
							    			 dtr.setLsdbs_number(lsdbs_number_rider);

							    			 //ENDATE DAN PAYDATE RIDER								
							    			 //produk2.wf_set_premi(tahun.intValue(),bulan.intValue(),tanggal.intValue(),datausulan.getLscb_id().intValue(),tahunEd.intValue(),bulanEd.intValue(),tanggalEd.intValue(),insperiod.intValue(),produk.flag_jenis_plan,produk2.ii_age,payperiod.intValue(),produk.flag_cerdas_siswa, datausulan.getLi_umur_pp().intValue(),produk2.ii_bisnis_id,produk2.ii_bisnis_no);
							    			 Integer tanggal_akhir_polis1=tanggalEd;
							    			 Integer bulan_akhir_polis1=bulanEd;
							    			 Integer tahun_akhir_polis1=new Integer(2017);																	

							    			 String tgl_akhir_polis1=null;
							    			 tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
							    			 if ( tgl_akhir_polis1.trim().length()!=0)
							    			 {
							    				 try {
							    					 dtr.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis1));
							    				 } catch (ParseException e) {
							    					 // TODO Auto-generated catch block
							    					 logger.error("ERROR :", e);
							    				 }
							    			 }else{
							    				 dtr.setMspr_end_date(null);
							    			 }
							    			 dtr.setMspr_end_pay(null);

							    			 premi=0.;//link premi rider diset 0			
							    			 dtr.setMspr_ins_period(1);
							    			 dtr.setMspr_tsi_pa_a(1000000.0);
							    			 dtr.setMspr_tsi_pa_b(1000000.0);
							    			 dtr.setMspr_tsi_pa_c(0.0);
							    			 dtr.setMspr_tsi_pa_d(1000000.0);
							    			 dtr.setMspr_rate(null);
							    			 dtr.setMspr_premium(premi);
							    			 dtr.setMspr_tsi(1000000.0);
							    			 dtr.setMspr_unit(0);
							    			 dtr.setMspr_class(1);
							    			 dtr.setLku_id("01");	
							    			 dtrd.add(dtr);
							    		 }
							    	 }
							    	 jumlahRider=dtrd.size();
							    	 excelListDatausulan.setDaftaRider(dtrd);
							    	 excelListDatausulan.setJumlah_seluruh_rider(jumlahRider);
							     }else{
							   //Khusus Campaign, setelah itu dihapus
							    	 if(excelListPp.getFlag_free_pa()!=null){
							    		 if (excelListPp.getFlag_free_pa()==2){
							    			 int tahun_rider,bulan_rider,tanggal_rider;String nama_produk_rider=null;
							    			 Datarider dtr=new Datarider();
							    			 Integer lsbs_id_rider=824;
							    			 Integer lsdbs_number_rider=1;							
							    			 Integer unit = 0;
							    			 Integer klas=0; 
							    			 Double premi = 0.;
							    			 Double upRider = 0.;

							    			 Integer factor = 0;
							    			 Double rate_rider = 0.;

							    			 dtr.setMspr_beg_date(excelListDatausulan.getMspr_beg_date());	
							    			 dtr.setLsbs_id(lsbs_id_rider);
							    			 dtr.setLsdbs_number(lsdbs_number_rider);

							    			 //ENDATE DAN PAYDATE RIDER								
							    			 //produk2.wf_set_premi(tahun.intValue(),bulan.intValue(),tanggal.intValue(),datausulan.getLscb_id().intValue(),tahunEd.intValue(),bulanEd.intValue(),tanggalEd.intValue(),insperiod.intValue(),produk.flag_jenis_plan,produk2.ii_age,payperiod.intValue(),produk.flag_cerdas_siswa, datausulan.getLi_umur_pp().intValue(),produk2.ii_bisnis_id,produk2.ii_bisnis_no);
							    			 Integer tanggal_akhir_polis1=tanggalEd;
							    			 Integer bulan_akhir_polis1=bulanEd;
							    			 Integer tahun_akhir_polis1=new Integer(2017);																	

							    			 String tgl_akhir_polis1=null;
							    			 tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
							    			 if ( tgl_akhir_polis1.trim().length()!=0)
							    			 {
							    				 try {
							    					 dtr.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis1));
							    				 } catch (ParseException e) {
							    					 // TODO Auto-generated catch block
							    					 logger.error("ERROR :", e);
							    				 }
							    			 }else{
							    				 dtr.setMspr_end_date(null);
							    			 }
							    			 dtr.setMspr_end_pay(null);

							    			 premi=0.;//link premi rider diset 0			
							    			 dtr.setMspr_ins_period(1);
							    			 dtr.setMspr_tsi_pa_a(1000000.0);
							    			 dtr.setMspr_tsi_pa_b(1000000.0);
							    			 dtr.setMspr_tsi_pa_c(0.0);
							    			 dtr.setMspr_tsi_pa_d(1000000.0);
							    			 dtr.setMspr_rate(null);
							    			 dtr.setMspr_premium(premi);
							    			 dtr.setMspr_tsi(1000000.0);
							    			 dtr.setMspr_unit(0);
							    			 dtr.setMspr_class(1);
							    			 dtr.setLku_id("01");	
							    			 dtrd.add(dtr);
							    			 excelListDatausulan.setDaftaRider(dtrd);
							    			 excelListDatausulan.setJmlrider(dtrd.size());
							    		 }
							    	 }
							     }
							    //peserta			     
							     if("183,189,195,204,221".indexOf(lsbs_id_utama.toString())>-1){
							    	peserta.setNama(excelListTtg.getMcl_first());
							    	peserta.setLsbs_id(excelListDatausulan.getLsbs_id());
							    	peserta.setLsdbs_number(excelListDatausulan.getLsdbs_number());
							    	peserta.setNo_reg("1a");
							    	peserta.setFlag_jenis_peserta(0);
							    	peserta.setNo_urut(0);
							    	peserta.setUmur(umurTtg);
							    	peserta.setMspr_premium(excelListDatausulan.getMspr_premium());
							    	peserta.setLsre_id(Integer.parseInt(SpajExcelList.get(i).get(33).toString().replace(".0", "")));
							    	peserta.setNext_send(excelListDatausulan.getMspr_beg_date());
							    	peserta.setKelamin(excelListTtg.getMspe_sex());
							    	peserta.setPremi(excelListDatausulan.getMspr_premium());
							    	peserta.setTanggal_lahir(excelListTtg.getMspe_date_birth());
							        psrt.add(peserta);
							     }
							     if(jumlahPeserta>0 && jumlahRiderKesehatan>0 &&(jumlahPeserta-1==jumlahRiderKesehatan)){
							    	 
							    	 for(int l=0; l<jumlahPeserta-1; l++){
							    		 Datarider dtr2=new Datarider();
							    		 PesertaPlus_mix peserta2=new PesertaPlus_mix();
							    		 dtr2=dtrd.get(l);							    		
							    		 if("819,820,823,825,826,831,832,833,840,841,842,843,844,846,847".indexOf(dtr2.getLsbs_id().toString())>-1){	
							    			Integer jenisPeserta=dtr2.getJenis();
							    			switch (jenisPeserta)
							    			{
							    				case 0 :
							    					peserta2.setNama(excelListTtg.getMcl_first());
							    					peserta2.setNo_reg("1a");
							    					peserta2.setFlag_jenis_peserta(0);
							    					peserta2.setNo_urut(0);
							    					peserta2.setLsre_id(excelListTtg.getLsre_id());
							    					peserta2.setUmur(umurTtg);
							    					peserta2.setKelamin(excelListTtg.getMspe_sex());
							    					peserta2.setTanggal_lahir(ttTambahan1_bod);
							    					break;	
							    				case 1 :
							    					peserta2.setNama(SpajExcelList.get(i).get(56).toString());
							    					peserta2.setNo_reg("1b");
							    					peserta2.setFlag_jenis_peserta(1);
							    					peserta2.setNo_urut(1);
							    					peserta2.setLsre_id(Integer.parseInt(SpajExcelList.get(i).get(59).toString().replace(".0", "")));
							    					peserta2.setUmur(umurTt2);
							    					peserta2.setKelamin(Integer.parseInt(SpajExcelList.get(i).get(57).toString().replace(".0", "")));
							    					peserta2.setTanggal_lahir(ttTambahan2_bod);
							    					break;
							    				case 2 : 
							    					peserta2.setNama(SpajExcelList.get(i).get(76).toString());
							    					peserta2.setNo_reg("1c");
							    					peserta2.setFlag_jenis_peserta(2);
							    					peserta2.setNo_urut(2);
							    					peserta2.setLsre_id(Integer.parseInt(SpajExcelList.get(i).get(79).toString().replace(".0", "")));
							    					peserta2.setUmur(umurTt3);
							    					peserta2.setKelamin(Integer.parseInt(SpajExcelList.get(i).get(77).toString().replace(".0", "")));
							    					peserta2.setTanggal_lahir(ttTambahan3_bod);
							    					break;
							    				case 3 :
							    					peserta2.setNama(SpajExcelList.get(i).get(96).toString());
							    					peserta2.setNo_reg("1d");
							    					peserta2.setFlag_jenis_peserta(3);
							    					peserta2.setNo_urut(3);
							    					peserta2.setLsre_id(Integer.parseInt(SpajExcelList.get(i).get(99).toString().replace(".0", "")));
							    					peserta2.setUmur(umurTt4);
							    					peserta2.setKelamin(Integer.parseInt(SpajExcelList.get(i).get(97).toString().replace(".0", "")));
							    					peserta2.setTanggal_lahir(ttTambahan4_bod);
							    					break;
							    				case 4 :
							    					peserta2.setNama(SpajExcelList.get(i).get(116).toString());
							    					peserta2.setNo_reg("1e");
							    					peserta2.setFlag_jenis_peserta(4);
							    					peserta2.setNo_urut(4);
							    					peserta2.setLsre_id(Integer.parseInt(SpajExcelList.get(i).get(119).toString().replace(".0", "")));
							    					peserta2.setUmur(umurTt5);
							    					peserta2.setKelamin(Integer.parseInt(SpajExcelList.get(i).get(117).toString().replace(".0", "")));
							    					peserta2.setTanggal_lahir(ttTambahan5_bod);
							    					break;
							    				
							    			}
							    			
								    			peserta2.setPremi(dtr2.getMspr_premium());
								    			peserta2.setNext_send(dtr2.getMspr_beg_date());
								    			peserta2.setLsbs_id(dtr2.getLsbs_id());
								    			peserta2.setLsdbs_number(dtr2.getLsdbs_number());
							    			
							    		 }
							    		 psrt.add(peserta2);
							    		 
							    	 }
							     }
							     if(!psrt.isEmpty())excelListDatausulan.setDaftapeserta(psrt);
							     if( jumlahRiderKesehatan!=psrt.size()-1 )err="Jumlah rider kesehatan  tidak sama dengan jumlah peserta.Harap lengkapi data, ";
							     
							     //Medquest
							     if( psrt.size()>0 ){
							    	
							    	String yes="" ;
							    	Integer ya=null;
							    	int jml=2;
							    	BigDecimal heigth =new BigDecimal(0);
							    	BigDecimal bmi =new BigDecimal(0);
							    	BigDecimal bmi2 =new BigDecimal(100);
							    	Map em=new HashMap();
							    	PesertaPlus_mix p=new PesertaPlus_mix();
							 		MedQuest mq=new MedQuest();
							 		MedQuest_tambah mqt=new MedQuest_tambah();
							 		MedQuest_tambah2 mqt2=new MedQuest_tambah2();
							 		MedQuest_tambah3 mqt3=new MedQuest_tambah3();
							 		MedQuest_tambah4 mqt4=new MedQuest_tambah4();
							 		MedQuest_tambah5 mqt5=new MedQuest_tambah5();						 	
							 		
							 		for(int m=0; m<psrt.size(); m++){
							 			String namaPenyakit,dirawat,waktu,lamanya,obat,dokter,konsumsi,tempatBerobat,mcu,waktuMcu,tempatMcu,hasil,pemeriksaan,desc = null,hamil=null ;
							 			Integer lama_hamil=0;
							 			p=psrt.get(m);
							 			Integer jenisPeserta=p.getFlag_jenis_peserta();
							 			switch (jenisPeserta)
							 			{
							 				case 0 ://ttg tambah 1(medquest tambah)
							 					mqt.setMsadm_berat(Integer.parseInt(SpajExcelList.get(i).get(40).toString().replace(".0", "")))	;	
							 					mqt.setMsadm_tinggi(Integer.parseInt(SpajExcelList.get(i).get(41).toString().replace(".0", "")));
							 					yes=SpajExcelList.get(i).get(42).toString().trim();
							 					if(yes.equalsIgnoreCase("YA")){
							 						ya=1;
							 					}else if(yes.equalsIgnoreCase("TIDAK")){
							 						ya=0;
							 					}else{
							 						ya=null;
							 					}
							 					mqt.setMsadm_sehat(ya);
							 					if(mqt.getMsadm_sehat()==0){
							 						namaPenyakit=SpajExcelList.get(i).get(43).toString()+",";
							 						if(namaPenyakit!=null){
							 							mqt.setMsadm_penyakit(1);						 							
							 						}
							 						
							 					}else{
							 						mqt.setMsadm_penyakit(0);
							 					}
							 					dirawat=SpajExcelList.get(i).get(44).toString().trim();				
						 						waktu=SpajExcelList.get(i).get(45).toString()+",";
						 						lamanya="Lama:"+""+SpajExcelList.get(i).get(46).toString()+",";
						 						obat="Obat yg Di Konsumsi:"+""+SpajExcelList.get(i).get(47).toString()+",";
						 						konsumsi="Obat itu Masih Dikonsumsi:"+""+SpajExcelList.get(i).get(48).toString()+",";
						 						tempatBerobat=dokter="Berobat Di:"+""+SpajExcelList.get(i).get(49).toString()+",";
						 						dokter="Nama Dokter:"+""+SpajExcelList.get(i).get(50).toString()+"";
						 						namaPenyakit="Nama Penyakit:"+SpajExcelList.get(i).get(43).toString()+",";
						 						desc=namaPenyakit+waktu+lamanya+obat+konsumsi+tempatBerobat+dokter;							 							
						 						
						 						mqt.setMsadm_penyakit_desc(desc);
							 					mcu=SpajExcelList.get(i).get(51).toString();//klo ga da kosongin aja exelnya						 					
							 					
							 					if(!mcu.equals("")){
							 						pemeriksaan=SpajExcelList.get(i).get(51).toString()+",";
							 						waktuMcu=SpajExcelList.get(i).get(52).toString()+",";
							 						hasil=SpajExcelList.get(i).get(53).toString();
							 						mqt.setMsadm_medis(1);
							 						mqt.setMsadm_medis_desc("Pemeriksaan:"+pemeriksaan+" "+"Tempat:"+waktuMcu+" "+"Hasil:"+hasil);
							 						
							 					}else{
							 						mqt.setMsadm_medis(0);
							 					}
							 					mqt.setMsadm_sehat_desc(desc);
							 					mqt.setMste_insured_no(0);
							 					mqt.setSex(p.getKelamin());
							 					if(p.getKelamin()==0){
							 						hamil=SpajExcelList.get(i).get(54).toString().trim();
							 						
							 						if(hamil.equalsIgnoreCase("YA")){
							 								lama_hamil=Integer.parseInt(SpajExcelList.get(i).get(55).toString().replace(".0", ""));
								 							mqt.setMsadm_pregnant(1);
								 							mqt.setMsadm_pregnant_time(lama_hamil);							 						
							 						}else if(hamil.equalsIgnoreCase("TIDAK")){
							 								mqt.setMsadm_pregnant(0);
							 						}else{
							 							err=err+"Untuk Wanita harus mengisi kolom pertanyaan khusus wanita";
							 						}
							 					}
							 					if(mqt.getMsadm_berat()!=null && mqt.getMsadm_tinggi()!=null){
							 						heigth= new BigDecimal(mqt.getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
							 					    bmi = new BigDecimal(mqt.getMsadm_berat()).divide(heigth,0,RoundingMode.HALF_UP);
							 					    mqt.setMsadm_bmi(bmi.doubleValue());
							 					    em = uwDao.selectEmBmi(bmi.doubleValue(),p.getUmur());
							 					    mqt.setMsadm_em(em);
							 					    mqt.setMsadm_em_life( new Double(em.get("LIFE").toString()) );
							 					   if(mqt.getMsadm_em_life()!=0){
								 						mqt.setMsadm_clear_case(0);
								 					}else{
								 						mqt.setMsadm_clear_case(1);
								 					}
							 					   
							 					    
							 					}else{
							 						err=err+"Kolom berat/tinggi badan tertanggung tambahan 1 masih kosong,";
							 					}
							 					
							 					break;	
							 				case 1 ://tt_tambah2
							 					mqt2.setMsadm_berat(Integer.parseInt(SpajExcelList.get(i).get(60).toString().replace(".0", "")))	;	
							 					mqt2.setMsadm_tinggi(Integer.parseInt(SpajExcelList.get(i).get(61).toString().replace(".0", "")));
							 					yes=SpajExcelList.get(i).get(62).toString().trim();
							 					if(yes.equalsIgnoreCase("YA")){
							 						ya=1;
							 					}else if(yes.equalsIgnoreCase("TIDAK")){
							 						ya=0;
							 					}else{
							 						ya=null;
							 					}
							 					mqt2.setMsadm_sehat(ya);
							 					if(mqt2.getMsadm_sehat()==0){
							 						namaPenyakit=SpajExcelList.get(i).get(63).toString()+",";
							 						if(namaPenyakit!=null){
							 							mqt2.setMsadm_penyakit(1);						 							
							 						}
							 						
							 					}else{
							 						mqt2.setMsadm_penyakit(0);
							 					}
							 					dirawat=SpajExcelList.get(i).get(64).toString().trim();						 						
//						 						if(dirawat.equalsIgnoreCase("YA")){
						 							waktu=SpajExcelList.get(i).get(65).toString()+",";
						 							lamanya="Lama :"+""+SpajExcelList.get(i).get(66).toString()+",";
						 							obat="Obt yg Dikonsumsi:"+""+SpajExcelList.get(i).get(67).toString()+",";
						 							konsumsi="Obat itu msh Dikonsumsi:"+""+SpajExcelList.get(i).get(68).toString()+",";
						 							tempatBerobat=dokter="Berobat Di:"+""+SpajExcelList.get(i).get(69).toString()+",";
						 							dokter="Nama Dokter:"+""+SpajExcelList.get(i).get(70).toString()+"";
						 							namaPenyakit="Nama Penyakit:"+SpajExcelList.get(i).get(63).toString()+",";
						 							desc=namaPenyakit+waktu+lamanya+obat+konsumsi+tempatBerobat+dokter;							 							
//						 						}
						 						mqt2.setMsadm_penyakit_desc(desc);
							 					mcu=SpajExcelList.get(i).get(71).toString();//klo ga da kosongin aja exelnya						 					
							 					
							 					if(!mcu.equals("")){
							 						pemeriksaan=SpajExcelList.get(i).get(71).toString()+",";
							 						waktuMcu=SpajExcelList.get(i).get(72).toString()+",";
							 						hasil=SpajExcelList.get(i).get(73).toString();
							 						mqt2.setMsadm_medis(1);
							 						mqt2.setMsadm_medis_desc("Pemeriksaan"+pemeriksaan+"Waktu Mcu:"+waktuMcu+""+"Hasil:"+hasil);
							 						
							 					}else{
							 						mqt2.setMsadm_medis(0);
							 					}
							 					mqt2.setMsadm_sehat_desc(desc);
							 					mqt2.setMste_insured_no(1);
							 					mqt2.setSex(p.getKelamin());
							 					if(p.getKelamin()==0){
							 						hamil=SpajExcelList.get(i).get(74).toString().trim();
							 						
							 						if(hamil.equalsIgnoreCase("YA")){
							 								lama_hamil=Integer.parseInt(SpajExcelList.get(i).get(75).toString().replace(".0", ""));
								 							mqt2.setMsadm_pregnant(1);
								 							mqt2.setMsadm_pregnant_time(lama_hamil);							 						
							 						}else if(hamil.equalsIgnoreCase("TIDAK")){
							 								mqt2.setMsadm_pregnant(1);
							 						}else{
							 							err=err+"Untuk Wanita harus mengisi kolom pertanyaan khusus wanita";
							 						}
							 					}
							 					if(mqt2.getMsadm_berat()!=null && mqt.getMsadm_tinggi()!=null){
							 						heigth= new BigDecimal(mqt.getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
							 					    bmi = new BigDecimal(mqt.getMsadm_berat()).divide(heigth,0,RoundingMode.HALF_UP);
							 					    mqt2.setMsadm_bmi(bmi.doubleValue());
							 					    em = uwDao.selectEmBmi(bmi.doubleValue(),p.getUmur());
							 					    mqt2.setMsadm_em(em);
							 					    mqt2.setMsadm_em_life( new Double(em.get("LIFE").toString()) );
							 					   if(mqt2.getMsadm_em_life()!=0){
								 						mqt2.setMsadm_clear_case(0);
								 					}else{
								 						mqt2.setMsadm_clear_case(1);
								 					}	 					   
							 					    
							 					}else{
							 						err=err+"Kolom berat/tinggi badan tertanggung tambahan 1 masih kosong,";
							 					}
							 					 
							 					break;
							 				case 2 ://tt_tambah 3 
							 					mqt3.setMsadm_berat(Integer.parseInt(SpajExcelList.get(i).get(80).toString().replace(".0", "")))	;	
							 					mqt3.setMsadm_tinggi(Integer.parseInt(SpajExcelList.get(i).get(81).toString().replace(".0", "")));
							 					yes=SpajExcelList.get(i).get(82).toString().trim();
							 					if(yes.equalsIgnoreCase("YA")){
							 						ya=1;
							 					}else if(yes.equalsIgnoreCase("TIDAK")){
							 						ya=0;
							 					}else{
							 						ya=null;
							 					}
							 					mqt3.setMsadm_sehat(ya);
							 					if(mqt3.getMsadm_sehat()==0){
							 						namaPenyakit=SpajExcelList.get(i).get(83).toString()+",";
							 						if(namaPenyakit!=null){
							 							mqt3.setMsadm_penyakit(1);						 							
							 						}
							 						
							 					}else{
							 						mqt3.setMsadm_penyakit(0);
							 					}
							 					dirawat=SpajExcelList.get(i).get(84).toString().trim();						 						
//						 						if(dirawat.equalsIgnoreCase("YA")){
						 							waktu=SpajExcelList.get(i).get(85).toString()+",";
						 							lamanya="Lama :"+""+SpajExcelList.get(i).get(86).toString()+",";
						 							obat="Obat yg Dikonsumsi:"+""+SpajExcelList.get(i).get(87).toString()+",";
						 							konsumsi="Obat itu Masih Dikonsumsi:"+""+SpajExcelList.get(i).get(88).toString()+",";
						 							tempatBerobat=dokter="Berobat Di:"+""+SpajExcelList.get(i).get(89).toString()+",";
						 							dokter="Nama Dokter:"+""+SpajExcelList.get(i).get(90).toString()+"";
						 							namaPenyakit="Nama Penyakit:"+SpajExcelList.get(i).get(83).toString()+",";
						 							desc=namaPenyakit+waktu+lamanya+obat+konsumsi+tempatBerobat+dokter;							 							
//						 						}
						 						mqt3.setMsadm_penyakit_desc(desc);
							 					mcu=SpajExcelList.get(i).get(91).toString();//klo ga da kosongin aja exelnya						 					
							 					
							 					if(!mcu.equals("")){
							 						pemeriksaan=SpajExcelList.get(i).get(91).toString()+",";
							 						waktuMcu=SpajExcelList.get(i).get(92).toString()+",";
							 						hasil=SpajExcelList.get(i).get(93).toString();
							 						mqt3.setMsadm_medis(1);
							 						mqt3.setMsadm_medis_desc("Pemeriksaan"+pemeriksaan+"Waktu Mcu:"+waktuMcu+""+"Hasil:"+hasil);							 					
							 					}else{
							 						mqt3.setMsadm_medis(0);
							 					}
							 					mqt3.setMsadm_sehat_desc(desc);
							 					mqt3.setMste_insured_no(2);
							 					mqt3.setSex(p.getKelamin());
							 					if(p.getKelamin()==0){
							 						hamil=SpajExcelList.get(i).get(94).toString().trim();
							 						
							 						if(hamil.equalsIgnoreCase("YA")){
							 								lama_hamil=Integer.parseInt(SpajExcelList.get(i).get(95).toString().replace(".0", ""));
								 							mqt3.setMsadm_pregnant(1);
								 							mqt3.setMsadm_pregnant_time(lama_hamil);							 						
							 						}else if(hamil.equalsIgnoreCase("TIDAK")){
							 								mqt3.setMsadm_pregnant(1);
							 						}else{
							 							err=err+"Untuk Wanita harus mengisi kolom pertanyaan khusus wanita";
							 						}
							 					}
							 					if(mqt3.getMsadm_berat()!=null && mqt.getMsadm_tinggi()!=null){
							 						heigth= new BigDecimal(mqt.getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
							 					    bmi = new BigDecimal(mqt.getMsadm_berat()).divide(heigth,0,RoundingMode.HALF_UP);
							 					    mqt3.setMsadm_bmi(bmi.doubleValue());
							 					    em = uwDao.selectEmBmi(bmi.doubleValue(),p.getUmur());
							 					    mqt3.setMsadm_em(em);
							 					    mqt3.setMsadm_em_life( new Double(em.get("LIFE").toString()) );
							 					   if(mqt3.getMsadm_em_life()!=0){
								 						mqt3.setMsadm_clear_case(0);
								 					}else{
								 						mqt3.setMsadm_clear_case(1);
								 					}	 					   
							 					    
							 					}else{
							 						err=err+"Kolom berat/tinggi badan tertanggung tambahan 1 masih kosong,";
							 					}
							 					break;
							 				case 3 ://tttambah 4
							 					mqt4.setMsadm_berat(Integer.parseInt(SpajExcelList.get(i).get(100).toString().replace(".0", "")))	;	
							 					mqt4.setMsadm_tinggi(Integer.parseInt(SpajExcelList.get(i).get(101).toString().replace(".0", "")));
							 					yes=SpajExcelList.get(i).get(102).toString().trim();
							 					if(yes.equalsIgnoreCase("YA")){
							 						ya=1;
							 					}else{
							 						ya=0;
							 					}
							 					mqt4.setMsadm_sehat(ya);
							 					if(mqt4.getMsadm_sehat()==0){
							 						namaPenyakit=SpajExcelList.get(i).get(103).toString()+",";
							 						if(namaPenyakit!=null){
							 							mqt4.setMsadm_penyakit(1);						 							
							 						}
							 						
							 					}else{
							 						mqt4.setMsadm_penyakit(0);
							 					}
							 					dirawat=SpajExcelList.get(i).get(104).toString().trim();						 						
//						 						if(dirawat.equalsIgnoreCase("YA")){
						 							waktu=SpajExcelList.get(i).get(105).toString()+",";
						 							lamanya="Lama :"+""+SpajExcelList.get(i).get(106).toString()+",";
						 							obat="Obat yg Dikonsumsi:"+""+SpajExcelList.get(i).get(107).toString()+",";
						 							konsumsi="Obat itu Masih Dikonsumsi:"+""+SpajExcelList.get(i).get(108).toString()+",";
						 							tempatBerobat=dokter="Berobat Di:"+""+SpajExcelList.get(i).get(109).toString()+",";
						 							dokter="Nama Dokter:"+""+SpajExcelList.get(i).get(110).toString()+"";
						 							namaPenyakit="Nama Penyakit:"+SpajExcelList.get(i).get(103).toString()+",";
						 							desc=namaPenyakit+waktu+lamanya+obat+konsumsi+tempatBerobat+dokter;							 							
//						 						}
						 						mqt4.setMsadm_penyakit_desc(desc);
							 					mcu=SpajExcelList.get(i).get(111).toString();//klo ga da kosongin aja exelnya						 					
							 					
							 					if(!mcu.equals("")){
							 						pemeriksaan=SpajExcelList.get(i).get(111).toString()+",";
							 						waktuMcu=SpajExcelList.get(i).get(112).toString()+",";
							 						hasil=SpajExcelList.get(i).get(113).toString();
							 						mqt4.setMsadm_medis(1);
							 						mqt4.setMsadm_medis_desc("Pemeriksaan"+pemeriksaan+"Waktu Mcu:"+waktuMcu+""+"Hasil:"+hasil);						 						
							 						
							 					}else{
							 						mqt4.setMsadm_medis(0);
							 					}
							 					mqt4.setMsadm_sehat_desc(desc);
							 					mqt4.setMste_insured_no(3);
							 					mqt4.setSex(p.getKelamin());
							 					if(p.getKelamin()==0){
							 						hamil=SpajExcelList.get(i).get(114).toString().trim();
							 						
							 						if(hamil.equalsIgnoreCase("YA")){
							 								lama_hamil=Integer.parseInt(SpajExcelList.get(i).get(115).toString().replace(".0", ""));
								 							mqt4.setMsadm_pregnant(1);
								 							mqt4.setMsadm_pregnant_time(lama_hamil);							 						
							 						}else if(hamil.equalsIgnoreCase("TIDAK")){
							 								mqt4.setMsadm_pregnant(1);
							 						}else{
							 							err=err+"Untuk Wanita harus mengisi kolom pertanyaan khusus wanita";
							 						}
							 					}
							 					if(mqt4.getMsadm_berat()!=null && mqt.getMsadm_tinggi()!=null){
							 						heigth= new BigDecimal(mqt.getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
							 					    bmi = new BigDecimal(mqt.getMsadm_berat()).divide(heigth,0,RoundingMode.HALF_UP);
							 					    mqt.setMsadm_bmi(bmi.doubleValue());
							 					    em = uwDao.selectEmBmi(bmi.doubleValue(),p.getUmur());
							 					    mqt4.setMsadm_em(em);
							 					    mqt4.setMsadm_em_life( new Double(em.get("LIFE").toString()) );
							 					   if(mqt4.getMsadm_em_life()!=0){
								 						mqt4.setMsadm_clear_case(0);
								 					}else{
								 						mqt4.setMsadm_clear_case(1);
								 					}
							 					   
							 					    
							 					}else{
							 						err=err+"Kolom berat/tinggi badan tertanggung tambahan 1 masih kosong,";
							 					}
							 					
							 					break;				
							 					
							 				case 4 ://tt_5
							 					mqt5.setMsadm_berat(Integer.parseInt(SpajExcelList.get(i).get(120).toString().replace(".0", "")))	;	
							 					mqt5.setMsadm_tinggi(Integer.parseInt(SpajExcelList.get(i).get(121).toString().replace(".0", "")));
							 					yes=SpajExcelList.get(i).get(122).toString().trim();
							 					if(yes.equalsIgnoreCase("YA")){
							 						ya=1;
							 					}else{
							 						ya=0;
							 					}
							 					mqt5.setMsadm_sehat(ya);
							 					if(mqt5.getMsadm_sehat()==0){
							 						namaPenyakit=SpajExcelList.get(i).get(123).toString()+",";
							 						if(namaPenyakit!=null){
							 							mqt5.setMsadm_penyakit(1);						 							
							 						}
							 						
							 					}else{
							 						mqt5.setMsadm_penyakit(0);
							 					}
							 					dirawat=SpajExcelList.get(i).get(124).toString().trim();						 						
//						 						if(dirawat.equalsIgnoreCase("YA")){
						 							waktu=SpajExcelList.get(i).get(125).toString()+",";
						 							lamanya="Lama:"+""+SpajExcelList.get(i).get(126).toString()+",";
						 							obat="Obat yg Dikonsumsi:"+""+SpajExcelList.get(i).get(127).toString()+",";
						 							konsumsi="Obat itu Masih Dikonsumsi:"+""+SpajExcelList.get(i).get(128).toString()+",";
						 							tempatBerobat=dokter="Berobat Di:"+""+SpajExcelList.get(i).get(129).toString()+",";
						 							dokter="Nama Dokter:"+""+SpajExcelList.get(i).get(130).toString()+"";
						 							namaPenyakit="Nama Penyakit:"+SpajExcelList.get(i).get(123).toString()+",";
						 							desc=namaPenyakit+waktu+lamanya+obat+konsumsi+tempatBerobat+dokter;							 							
//						 						}
						 						mqt5.setMsadm_penyakit_desc(desc);
							 					mcu=SpajExcelList.get(i).get(121).toString();//klo ga da kosongin aja exelnya						 					
							 					
							 					if(!mcu.equals("")){
							 						pemeriksaan=SpajExcelList.get(i).get(121).toString()+",";
							 						waktuMcu=SpajExcelList.get(i).get(122).toString()+",";
							 						hasil=SpajExcelList.get(i).get(123).toString();
							 						mqt5.setMsadm_medis(1);
							 						mqt5.setMsadm_medis_desc("Pemeriksaan"+pemeriksaan+"Waktu Mcu:"+waktuMcu+""+"Hasil:"+hasil);			 						
							 						
							 					}else{
							 						mqt5.setMsadm_medis(0);
							 					}
							 					mqt5.setMsadm_sehat_desc(desc);
							 					mqt5.setMste_insured_no(4);
							 					mqt5.setSex(p.getKelamin());
							 					if(p.getKelamin()==0){
							 						hamil=SpajExcelList.get(i).get(134).toString().trim();	
							 						hamil="TIDAK";
							 						if(hamil.equalsIgnoreCase("YA")){
							 								lama_hamil=Integer.parseInt(SpajExcelList.get(i).get(125).toString().replace(".0", ""));
								 							mqt5.setMsadm_pregnant(1);
								 							mqt5.setMsadm_pregnant_time(lama_hamil);							 						
							 						}else if(hamil.equalsIgnoreCase("TIDAK")){
							 								mqt5.setMsadm_pregnant(1);
							 						}else{
							 							err=err+"Untuk Wanita harus mengisi kolom pertanyaan khusus wanita";
							 						}
							 					}
							 					if(mqt5.getMsadm_berat()!=null && mqt.getMsadm_tinggi()!=null){
							 						heigth= new BigDecimal(mqt.getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
							 					    bmi = new BigDecimal(mqt.getMsadm_berat()).divide(heigth,0,RoundingMode.HALF_UP);
							 					    mqt5.setMsadm_bmi(bmi.doubleValue());
							 					    em = uwDao.selectEmBmi(bmi.doubleValue(),p.getUmur());
							 					    mqt5.setMsadm_em(em);
							 					    mqt5.setMsadm_em_life( new Double(em.get("LIFE").toString()) );
							 					   if(mqt.getMsadm_em_life()!=0){
								 						mqt5.setMsadm_clear_case(0);
								 					}else{
								 						mqt5.setMsadm_clear_case(1);
								 					}
							 					   
							 					    
							 					}else{
							 						err=err+"Kolom berat/tinggi badan tertanggung tambahan 1 masih kosong,";
							 					}
							 					
							 					break;	
							 					
							 			}
							 		}
							 		if(excelListPp.getLsre_id()==1){
							 			mq.setMsadm_berat(mqt.getMsadm_berat());
							 			mq.setMsadm_tinggi(mqt.getMsadm_tinggi());
							 			mq.setMsadm_sehat(mqt.getMsadm_sehat());
							 			mq.setMsadm_sehat_desc(mqt.getMsadm_sehat_desc());
							 			mq.setMsadm_penyakit(mqt.getMsadm_penyakit());
			 							mq.setMsadm_penyakit_desc(mqt.getMsadm_penyakit_desc());
			 							mq.setMsadm_medis(mqt.getMsadm_medis());			 							
			 							if(excelListPp.getMspe_sex()==0){
			 								mq.setMsadm_pregnant(mqt.getMsadm_pregnant());
				 							mq.setMsadm_pregnant_time(mq.getMsadm_pregnant_time());
				 							mq.setMsadm_bmi(mqt.getMsadm_bmi());				 							
			 							}
			 							mq.setMsadm_em_life(mqt.getMsadm_em_life());
			 							mq.setMsadm_clear_case(mqt.getMsadm_clear_case());
			 							mq.setSex(mqt.getSex());
			 							mq.setMste_insured_no(0);
							 		}
							 		excelList.setMedQuest(new MedQuest());
							 		excelList.setMedQuest_tambah(mqt);
							 		excelList.setMedQuest_tambah2(mqt2);
							 		excelList.setMedQuest_tambah3(mqt3);
							 		excelList.setMedQuest_tambah4(mqt4);
							 		excelList.setMedQuest_tambah5(mqt5);
							     }
							     
							     //proses set Account Recur
							     Integer autodebet = new Integer(0);
							     autodebet=Integer.parseInt(SpajExcelList.get(i).get(140).toString().replace(".0", ""));
							     if(autodebet==1 || autodebet==2){	
							    	
							    	 String relation_CC=SpajExcelList.get(i).get(145).toString().replace(".0", "");
							    	 if(relation_CC.equals("1")){
							    		 rekAutodebet.setMar_holder(SpajExcelList.get(i).get(144).toString());				    		 
							    	 }
							    	 rekAutodebet.setMar_jenis(autodebet);
							    	 rekAutodebet.setMar_active(1);
							    	 try {
										Date cc_expired = df.parse(SpajExcelList.get(i).get(143).toString());
										rekAutodebet.setMar_expired(cc_expired);
									} catch (ParseException e) {							
										logger.error("ERROR :", e);
									}
							    	rekAutodebet.setMar_acc_no(SpajExcelList.get(i).get(142).toString()); 
							    	String lsbp_id=SpajExcelList.get(i).get(146).toString().replace(".0", "");
							    	String lbn_id=bacDao.selectLbn_id(lsbp_id);
							    	rekAutodebet.setLbn_id(lbn_id);
							    	
							    	 //validasi no rekening
									 if(rekAutodebet!=null){
										 if(rekAutodebet.getLbn_id()!=null)
										 err=err+validatorTM.checkDataAutodebet(rekAutodebet);
									 }
							     }			
								//end of setValue			
								
								//agen
								form_agen agn=new form_agen();
								if(excelListagen!=null){
								    String hasil=agn.sertifikasi_agen(excelListagen.getMsag_id(),excelListagen.getMsag_ulink(), excelListagen.getMsag_sertifikat(), excelListagen.getMsag_berlaku(), excelListDatausulan.getMspr_beg_date());
								    if("".equals(excelListagen.getMsag_id()) || !"".equals(hasil))err=err+hasil+", ";
								    err=err+validatorTM.validateAgenTutupan(lca,excelListagen);
								}else{
									err=err+"Data Agent Tidak Ditemukan";
								}
								
								if(excelListPp.getReff_tm_id()!=null){
									if(!excelListPp.getReff_tm_id().equals("")){
										reffbii=new ReffBii();
										List<Map> reffBank = bacDao.selectReffSinarmas(excelListPp.getReff_tm_id(),0);
										BigDecimal lrbidReff;
										Map mapReff= (Map)reffBank.get(0);					 
										lrbidReff=(BigDecimal)mapReff.get("LRB_ID");
										if(lrbidReff!=null){										
											reffbii.setLevel_id("4");
											reffbii.setLrb_id(lrbidReff.toString());
											reffbii.setReff_id(lrbidReff.toString());
											reffbii.setLcb_no("J001");
											reffbii.setLcb_penutup("J001");
											reffbii.setLcb_penutup2("J001");

										}
									}
								}
								err=err+validatorTM.checkHubunganPP(excelListPp,excelListTtg);
			
								if("".equals(err)){
									excelList.setPemegang(excelListPp);
									excelList.setAddressbilling(excelListAddrBilling);
									excelList.setTertanggung(excelListTtg);
									excelList.setDatausulan(excelListDatausulan);
									excelList.setAccount_recur(rekAutodebet);
									excelList.setAgen(excelListagen);
								}else{
									Map<String,String> map2= new HashMap<String, String>();
									map2.put("sts", "FAILED");
									map2.put("msg", "Error: Nama PP :" +" "+SpajExcelList.get(i).get(2).toString()+" "+err);
									pesanList.add(map2);						
								}
								if(pesanList.isEmpty()){ 
									insertSpajtemp(excelList,null,null,reffbii);
					            }		
				    }else{	
				    	TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				    	Map<String,String> map2= new HashMap<String, String>();
						map2.put("sts", "FAILED");
						map2.put("msg", "Error: Nama PP :" +" "+SpajExcelList.get(i).get(2).toString()+" "+err);
						pesanList.add(map2);	
					}
				}else if("12".indexOf(lca)>=0){
					//ekawaktu
					//pemegang polis
					excelListPp.setCampaign_id(Integer.parseInt(SpajExcelList.get(i).get(0).toString().substring(0, 1)));
					//excelListPp.setCampaign_id(8);
					excelListPp.setMspo_nasabah_dcif(SpajExcelList.get(i).get(1).toString());
					excelListPp.setMcl_first(SpajExcelList.get(i).get(2).toString());
					String no_blanko=generateNoBlanko(excelListPp.getCampaign_id().toString(),169);
					excelListPp.setMspo_no_blanko(no_blanko);
					excelListPp.setMspe_sex(Integer.parseInt(SpajExcelList.get(i).get(3).toString().substring(0, 1)));
					excelListPp.setMspe_place_birth(SpajExcelList.get(i).get(4).toString());
					excelListPp.setLside_id(Integer.parseInt(SpajExcelList.get(i).get(6).toString().substring(0, 1)));
					excelListPp.setMspe_no_identity(SpajExcelList.get(i).get(7).toString().replace(".0", ""));
					excelListPp.setMspe_sts_mrt(SpajExcelList.get(i).get(8).toString().replace(".0", ""));
					excelListPp.setLsre_id(Integer.parseInt(SpajExcelList.get(i).get(9).toString().replace(".0", "")));
					excelListPp.setAlamat_rumah(SpajExcelList.get(i).get(10).toString());
					excelListPp.setKota_rumah(SpajExcelList.get(i).get(11).toString());
					excelListPp.setKd_pos_rumah(SpajExcelList.get(i).get(12).toString().replace(".0", ""));
					if(excelListPp.getKd_pos_rumah().equals("-"))excelListPp.setKd_pos_rumah("");
					excelListPp.setAlamat_kantor(SpajExcelList.get(i).get(13).toString());
					excelListPp.setKota_kantor(SpajExcelList.get(i).get(14).toString());
					excelListPp.setKd_pos_kantor(SpajExcelList.get(i).get(15).toString().replace(".0", ""));
					if(excelListPp.getKd_pos_kantor().equals("-"))excelListPp.setKd_pos_kantor("");
					excelListPp.setTelpon_rumah(FormatString.getRightPart(SpajExcelList.get(i).get(21).toString().replace(".0", ""), "-"));
					if(excelListPp.getTelpon_rumah().startsWith("0"))excelListPp.setTelpon_rumah("");								
					excelListPp.setArea_code_rumah(FormatString.getLeftPart(SpajExcelList.get(i).get(21).toString().replace(".0", ""),"-"));								
					if("000,0000".indexOf(excelListPp.getArea_code_rumah())>=0)excelListPp.setArea_code_rumah("");
					excelListPp.setArea_code_kantor(FormatString.getLeftPart(SpajExcelList.get(i).get(22).toString().replace(".0", ""),"-"));
					excelListPp.setTelpon_kantor(FormatString.getRightPart(SpajExcelList.get(i).get(22).toString().replace(".0", ""),"-"));
					if(!excelListPp.getArea_code_kantor().isEmpty() && !excelListPp.getTelpon_kantor().isEmpty()){
						if(excelListPp.getTelpon_kantor().startsWith("0"))excelListPp.setTelpon_kantor("");	
						if("000,0000".indexOf(excelListPp.getArea_code_kantor())>=0)excelListPp.setArea_code_kantor("");
					}
					excelListPp.setLscb_id(Integer.parseInt(SpajExcelList.get(i).get(87).toString().replace(".0", "")));
					excelListPp.setNo_hp(SpajExcelList.get(i).get(23).toString().replace(".0", ""));
					excelListPp.setNo_hp2(SpajExcelList.get(i).get(24).toString().replace(".0", ""));
					excelListPp.setEmail(SpajExcelList.get(i).get(25).toString());
					excelListPp.setMspe_mother(SpajExcelList.get(i).get(93).toString());
					excelListPp.setApplication_id(SpajExcelList.get(i).get(94).toString().replace(".0", "").trim());
					excelListPp.setMkl_penghasilan(SpajExcelList.get(i).get(95).toString());
					excelListPp.setReff_tm_id(SpajExcelList.get(i).get(115).toString().replace(".0", "").trim());
					excelListPp.setSpv(SpajExcelList.get(i).get(117).toString());
					excelListPp.setCf_job_code(SpajExcelList.get(i).get(118).toString().replace(".0", "").trim());
					excelListPp.setCf_customer_id(SpajExcelList.get(i).get(119).toString().replace(".0", "").trim());
					excelListPp.setCf_campaign_code(SpajExcelList.get(i).get(120).toString().replace(".0", "").trim());
					
					excelList.setCurrentUser(user);
					
					//==========Retrieve && set Value Address Billing(Korespondensi)=======================//				
					excelListAddrBilling.setMsap_address(SpajExcelList.get(i).get(16).toString());
					excelListAddrBilling.setKota(SpajExcelList.get(i).get(17).toString());
					excelListAddrBilling.setKota_tagih(excelListAddrBilling.getKota());
					excelListAddrBilling.setMsap_zip_code(SpajExcelList.get(i).get(18).toString().replace(".0", ""));
					if(excelListAddrBilling.getMsap_zip_code().equals("-"))excelListAddrBilling.setMsap_zip_code("");
					excelListAddrBilling.setMsap_phone1(FormatString.getRightPart(SpajExcelList.get(i).get(19).toString().replace(".0", ""),"-"));
					if(excelListAddrBilling.getMsap_phone1().startsWith("0"))excelListAddrBilling.setMsap_phone1("");
					excelListAddrBilling.setMsap_area_code1(FormatString.getLeftPart(SpajExcelList.get(i).get(19).toString().replace(".0", ""),"-"));
					if("000,0000".indexOf(excelListAddrBilling.getMsap_area_code1())>=0)excelListAddrBilling.setMsap_area_code1("");
					excelListAddrBilling.setNo_hp(SpajExcelList.get(i).get(20).toString().replace(".0", ""));
					excelListAddrBilling.setMsap_contact(excelListPp.getMcl_first());
					if(excelListAddrBilling.getMsap_address().trim().equalsIgnoreCase(excelListPp.getAlamat_rumah().trim())){
						excelListAddrBilling.setTagih("2");
					}else if(excelListAddrBilling.getMsap_address().trim().equalsIgnoreCase(excelListPp.getAlamat_kantor().trim())){
						excelListAddrBilling.setTagih("3");
					}else{
						excelListAddrBilling.setTagih("1");
					}
					//==========End Of Retrieve && set Value Address Billing(Korespondensi)=======================//
					
					//==========Retrieve && set Value Tertanggung=======================//
					excelListTtg.setMcl_first(SpajExcelList.get(i).get(26).toString());
					excelListTtg.setMspe_sex(Integer.parseInt(SpajExcelList.get(i).get(27).toString().substring(0, 1)));
					excelListTtg.setMspe_place_birth(SpajExcelList.get(i).get(28).toString());
					excelListTtg.setLside_id(Integer.parseInt(SpajExcelList.get(i).get(29).toString().substring(0, 1)));
					excelListTtg.setMspe_no_identity(SpajExcelList.get(i).get(32).toString().replace(".0", ""));
					excelListTtg.setLsre_id(Integer.parseInt(SpajExcelList.get(i).get(33).toString().replace(".0", "")));				
					excelListTtg.setAlamat_rumah(SpajExcelList.get(i).get(34).toString());	
					excelListTtg.setKota_rumah(SpajExcelList.get(i).get(35).toString());	
					excelListTtg.setKd_pos_rumah(SpajExcelList.get(i).get(36).toString().replace(".0", "").trim());
					if(excelListTtg.getKd_pos_rumah().equals("-"))excelListPp.setKd_pos_rumah("");
					excelListTtg.setKd_pos_kantor(SpajExcelList.get(i).get(39).toString().replace(".0", "").trim());	
					if(excelListTtg.getKd_pos_kantor().equals("-"))excelListPp.setKd_pos_kantor("");
					excelListTtg.setAlamat_kantor(SpajExcelList.get(i).get(37).toString());	
					excelListTtg.setKota_kantor(SpajExcelList.get(i).get(38).toString());
					excelListDatausulan.setMste_insured_no(1);
					//==========End of Retrieve && set Value Tertanggung=======================//
					
					//==========Retrieve && set Value Data Usulan=======================//
					Integer lsbs_id_utama=Integer.parseInt(SpajExcelList.get(i).get(50).toString().substring(0, 3));
					Integer lsdbs_number_utama=Integer.parseInt(SpajExcelList.get(i).get(50).toString().substring(4));
				    excelListDatausulan.setLsbs_id(lsbs_id_utama); 
				    excelListDatausulan.setLsdbs_number(lsdbs_number_utama);
				    excelListDatausulan.setKurs_premi(SpajExcelList.get(i).get(52).toString());			   
				    excelListDatausulan.setLscb_id(Integer.parseInt(SpajExcelList.get(i).get(87).toString().replace(".0", "")));			    							    
				    excelListDatausulan.setMste_flag_cc(Integer.parseInt(SpajExcelList.get(i).get(86).toString().replace(".0", "")));	
				    BigDecimal up=new BigDecimal(Integer.parseInt(SpajExcelList.get(i).get(54).toString().replace(".0", "")));
				    excelListDatausulan.setMspr_tsi(up.doubleValue());
				    excelListDatausulan.setMspo_no_kerjasama(SpajExcelList.get(i).get(116).toString());
				    String kode_produk=Integer.toString(excelListDatausulan.getLsbs_id());
					nama_produk="produk_asuransi.n_prod_"+kode_produk;
					try {
						aClass = Class.forName( nama_produk );
						 produk = (n_prod)aClass.newInstance();
						 produk.setSqlMap(uwDao.getSqlMapClient());
						 
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e1);
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e);
					}
					
					produk.ii_pmode=excelListDatausulan.getLscb_id();
				    produk.ii_bisnis_id=lsbs_id_utama;
				    produk.ii_bisnis_no=lsdbs_number_utama;
				    produk.is_kurs_id=excelListDatausulan.getKurs_premi();
				    produk.idec_up=up.doubleValue();
				    produk.of_set_bisnis_no(lsdbs_number_utama);
				    produk.of_get_conperiod(lsdbs_number_utama);
					
				    try{
				    	Date sysdate = commonDao.selectSysdateTrunc();				    	
						Date pp_bod = df.parse(SpajExcelList.get(i).get(5).toString());	
						Date tt_bod = df.parse(SpajExcelList.get(i).get(29).toString());
						Date bdate = df.parse(SpajExcelList.get(i).get(92).toString());
						Date tgl_recur=df.parse(SpajExcelList.get(i).get(92).toString());	
						excelListDatausulan.setMspr_beg_date(bdate);
						excelListPp.setMspe_date_birth(pp_bod);
						excelListPp.setMste_tgl_recur(tgl_recur);
						excelListTtg.setMspe_date_birth(tt_bod);
						excelListPp.setTgl_upload(sysdate);
						tahun = Integer.parseInt(sdfYear.format(excelListDatausulan.getMspr_beg_date()));
						bulan = Integer.parseInt(sdfMonth.format(excelListDatausulan.getMspr_beg_date()));
						tanggal = Integer.parseInt(sdfDay.format(excelListDatausulan.getMspr_beg_date()));
						f_hit_umur umr = new f_hit_umur();					
						
						if(pp_bod!=null){//1 untuk pemegang polis
							tahun1 = Integer.parseInt(sdfYear.format(excelListPp.getMspe_date_birth()));
							bulan1 = Integer.parseInt(sdfMonth.format(excelListPp.getMspe_date_birth()));
							tanggal1 = Integer.parseInt(sdfDay.format(excelListPp.getMspe_date_birth()));
							umurPp=umr.umur(tahun1, bulan1, tanggal1, tahun, bulan, tanggal);
							if(umurPp==0)umurPp=1;
							excelListDatausulan.setLi_umur_pp(umurPp);
							excelListPp.setMste_age(umurPp);
							produk.of_set_usia_pp(umurPp);
						}
						
						if(tt_bod!=null){ //2 untuk tertanggung utama 
							 tahun2 = Integer.parseInt(sdfYear.format(excelListTtg.getMspe_date_birth()));
							 bulan2 = Integer.parseInt(sdfMonth.format(excelListTtg.getMspe_date_birth()));
							 tanggal2 = Integer.parseInt(sdfDay.format(excelListTtg.getMspe_date_birth()));
							 umurTtg=umr.umur(tahun2, bulan2, tanggal2, tahun, bulan, tanggal);
							 if(umurTtg==0)umurTtg=1;
							 excelListDatausulan.setLi_umur_ttg(umurTtg);
							 excelListTtg.setMste_age(umurTtg);
							 produk.of_set_usia_tt(umurTtg);					
							 produk.ii_age=umurTtg;
						}					
						if(bdate!=null){//tahun untuk begdate polis
							
							produk.of_set_begdate(tahun,bulan,tanggal);	
							
							tanggalEd= new Integer(produk.idt_end_date.getTime().getDate());
							bulanEd= new Integer(produk.idt_end_date.getTime().getMonth()+1);
							tahunEd= new Integer(produk.idt_end_date.getTime().getYear()+1900);
							
							String tgl_end=Integer.toString(tanggalEd.intValue());
							String bln_end= Integer.toString(bulanEd.intValue());
							String thn_end = Integer.toString(tahunEd.intValue());
							if ((tgl_end.equalsIgnoreCase("0")==true) || (bln_end.equalsIgnoreCase("0")==true) || (thn_end.equalsIgnoreCase("0")==true))
							{
								excelListDatausulan.setMste_end_date(null);
							}else{
								String tanggal_end_date = FormatString.rpad("0",tgl_end,2)+"/"+FormatString.rpad("0",bln_end,2)+"/"+thn_end;
								excelListDatausulan.setMspr_end_date(defaultDateFormat.parse(tanggal_end_date));
							}
							
							cal.setTime(excelListDatausulan.getMspr_end_date());
							cal.add(Calendar.MONTH, -1);
							cal.add(Calendar.DATE, 1);
							Date end_pay=cal.getTime();						
							excelListDatausulan.setMspr_end_pay(end_pay);
							
						}
						payperiod=produk.of_get_payperiod();
						if(lsbs_id_utama==183 || lsbs_id_utama==189 || lsbs_id_utama==195 ||lsbs_id_utama==204||lsbs_id_utama==214||lsbs_id_utama==221 ||lsbs_id_utama==225){
							insperiod=1;
						}else{
							insperiod=produk.ii_contract_period;
						}
						//cek usia pp
						err=err+produk.of_check_usia(tahun1, bulan1, tanggal1, tahun, bulan, tanggal, payperiod, lsdbs_number_utama);
						//cek Usia tgg
						err=err+produk.of_check_usia(tahun2, bulan2, tanggal2, tahun, bulan, tanggal, payperiod, lsdbs_number_utama);
						excelListDatausulan.setMspo_pay_period(payperiod);
						excelListDatausulan.setMspo_ins_period(insperiod);
						
						//0 untuk sysdate
						 tahun0 = Integer.parseInt(sdfYear.format(sysdate));
						 bulan0 = Integer.parseInt(sdfMonth.format(sysdate));
						 tanggal0 = Integer.parseInt(sdfDay.format(sysdate));
						
					
				    }catch (Exception e) {
						//logger.error("ERROR :", e);
						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
						Map<String,String> map = new HashMap<String, String>();
						map.put("sts", "FAILED");
						map.put("msg", "FAILED : Format Tanggal Ada Yang salah!!!" +SpajExcelList.get(i).get(2).toString());
						pesanList.add(map);		
						return pesanList;
					}
				    excelListDatausulan.setLi_umur_pp(umurPp);
				    excelListDatausulan.setLi_umur_ttg(umurTtg);
				    double pct_add= produk.idec_pct_list[excelListDatausulan.getLscb_id()];
				    produk.idec_add_pct=pct_add;
				    produk.of_hit_premi();			    
				    excelListDatausulan.setMspr_tsi(produk.idec_up);
				    excelListDatausulan.setMspr_premium(produk.idec_premi);
				    Double rate =0.0;
				    rate=produk.idec_rate;
				    excelListDatausulan.setMspr_rate(rate.intValue());			    
				    
				    Integer jumlahBenef=0;
				    if(!SpajExcelList.get(i).get(63).toString().equals(""))jumlahBenef+=1;
				    if(!SpajExcelList.get(i).get(68).toString().equals(""))jumlahBenef+=1;
				    if(!SpajExcelList.get(i).get(73).toString().equals(""))jumlahBenef+=1;
				    if(!SpajExcelList.get(i).get(78).toString().equals(""))jumlahBenef+=1;
				    if(jumlahBenef>0){
					  
							try {
								benef=Common.serializableList(prosesBenefeciary(SpajExcelList,i,jumlahBenef));
							} catch (ParseException e) {
								
								//logger.error("ERROR :", e);
							}
						
				    }
				    
				    dtrd=Common.serializableList(prosesRider(SpajExcelList,i,excelListDatausulan,tanggalEd,bulanEd,tahunEd,tahun,bulan,tanggal,produk.flag_jenis_plan,produk.flag_cerdas_siswa,insperiod,payperiod));			    			    
			 		MedQuest mq=new MedQuest();
			 		if(excelListDatausulan.getLsbs_id()==163){
			 			mq=prosesQuestionareDS(SpajExcelList,excelListDatausulan.getLi_umur_ttg(),i,excelListTtg.getMspe_sex());
			 		}else if(excelListDatausulan.getLsbs_id()==169){
			 			mq=prosesQuestionareEkaWaktu(SpajExcelList,excelListDatausulan.getLi_umur_ttg(),i,excelListTtg.getMspe_sex());
			 		}
				   
				    
				    //account_recur
				    Integer autodebet = new Integer(0);
				    autodebet=Integer.parseInt(SpajExcelList.get(i).get(86).toString().replace(".0", ""));
				    if(autodebet==1 || autodebet==2){		    	
				    	 rekAutodebet.setMar_jenis(autodebet);
				    	 try {
				    		if(autodebet==1){
				    			Date tgl_debet = df.parse(SpajExcelList.get(i).get(92).toString());
				    			rekAutodebet.setTgl_debet(tgl_debet);
				    		}
							if(excelListDatausulan.getLsbs_id()==169 || excelListDatausulan.getLsbs_id()==210){
								Date tgl_expired = df.parse(SpajExcelList.get(i).get(114).toString());	
								rekAutodebet.setMar_expired(tgl_expired);
							}
						} catch (ParseException e) {							
							logger.error("ERROR :", e);
							TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
							Map<String,String> map = new HashMap<String, String>();
							map.put("sts", "FAILED");
							map.put("msg", "FAILED:Format Tanggal Kartu kredit salah!!!" +SpajExcelList.get(i).get(2).toString());
							pesanList.add(map);		
							return pesanList;
						}
				    	rekAutodebet.setMar_acc_no(SpajExcelList.get(i).get(90).toString()); 			    	
				    	String lbn_id="";
				        if(excelListDatausulan.getLsbs_id()==163){
				        	lbn_id=SpajExcelList.get(i).get(89).toString();
				        	//rekAutodebet.setFlag_autodebet_nb(1);
				        	rekAutodebet.setFlag_set_auto(0);
				        	rekAutodebet.setFlag_jn_tabungan(0);	
				        }else{
				        	lbn_id=bacDao.selectLbn_id(SpajExcelList.get(i).get(88).toString());
				        	rekAutodebet.setFlag_autodebet_nb(0);
				        	rekAutodebet.setFlag_set_auto(0);
				        }
				    	
				        
				    	rekAutodebet.setLbn_id(lbn_id);			    		    	
				    	rekAutodebet.setMar_active(1);	
				    	rekAutodebet.setMar_number(1);
				    	rekAutodebet.setLus_id(Integer.parseInt(user.getLus_id()));
				    	rekAutodebet.setMar_holder(SpajExcelList.get(i).get(91).toString());
				    	
				    	 //validasi no rekening
						 if(rekAutodebet!=null){
							 err=err+validatorTM.checkDataAutodebet(rekAutodebet);
						 }
				     }
				    
				    //==========Retrieve && set Value Agen and Regional Polis=======================//
				     excelListagen.setMsag_id(SpajExcelList.get(i).get(83).toString().replace(".0", ""));
				     if(excelListagen.getMsag_id() == null)excelListagen.setMsag_id("");							     
					 if(!"".equals(excelListagen.getMsag_id())){
						excelListagen = bacDao.select_detilagen3(excelListagen.getMsag_id());									
								if(excelListagen!=null){
									excelListPp.setMsag_id(excelListagen.getMsag_id());
									excelListPp.setLca_id(excelListagen.getLca_id());
									excelListPp.setLwk_id(excelListagen.getLwk_id());
									excelListPp.setLsrg_id(excelListagen.getLsrg_id());
									excelListAddrBilling.setLca_id(excelListagen.getLca_id());
									excelListAddrBilling.setLwk_id(excelListagen.getLwk_id());
									excelListAddrBilling.setLsrg_id(excelListagen.getLsrg_id());
						}
					}
				    
				    //agen
				    form_agen agn=new form_agen();
					String hasil=agn.sertifikasi_agen(excelListagen.getMsag_id(),excelListagen.getMsag_ulink(), excelListagen.getMsag_sertifikat(), excelListagen.getMsag_berlaku(), excelListDatausulan.getMspr_beg_date());
					if("".equals(excelListagen.getMsag_id()) || !"".equals(hasil))err=err+hasil+", ";
					err=err+validatorTM.validateAgenTutupan(lca,excelListagen);
				    if(excelListDatausulan.getLsbs_id()==163){
						reff_bank=SpajExcelList.get(i).get(85).toString();
						agent_bank=SpajExcelList.get(i).get(84).toString();
						
						
						
						List<Map> reffBank = bacDao.selectReffSinarmas(reff_bank,0);
						reffbii=new ReffBii();
						BigDecimal lrbidReff;				
						Map mapReff= (Map)reffBank.get(0);					 
						lrbidReff=(BigDecimal)mapReff.get("LRB_ID");
						if(lrbidReff!=null ){					
							 reffbii.setLevel_id("4");
							 reffbii.setLrb_id("149236");
							 reffbii.setReff_id(lrbidReff.toString());
							 reffbii.setLcb_no("S471");
							 reffbii.setLcb_penutup("S471");
							 reffbii.setLcb_penutup2("S471");
						 }else{
							 err=err+"Data Refferal tidak ditemukan. Polis ini harus mempunyai refferal yang valid,";
						 }
				    }
					
					if("".equals(err)){
						if(!dtrd.isEmpty())excelListDatausulan.setDaftaRider(dtrd);
					    excelListDatausulan.setJmlrider(jumlahRider);
					    excelList.setPemegang(excelListPp);
						excelList.setAddressbilling(excelListAddrBilling);
						excelList.setTertanggung(excelListTtg);
						excelList.setDatausulan(excelListDatausulan);
						excelList.setAccount_recur(rekAutodebet);
						excelList.setAgen(excelListagen);
						excelList.getDatausulan().setDaftabenef(benef);
						excelList.setMedQuest(mq);
						excelList.setMedQuest_tambah(new MedQuest_tambah());
						excelList.setMedQuest_tambah2(new MedQuest_tambah2());
						excelList.setMedQuest_tambah3(new MedQuest_tambah3());
						excelList.setMedQuest_tambah4(new MedQuest_tambah4());
						excelList.setMedQuest_tambah5(new MedQuest_tambah5());
						insertSpajtemp(excelList,reff_bank,agent_bank,reffbii);
					}else{
						HashMap<String,String> map2= new HashMap<String, String>();
						map2.put("sts", "FAILED");
						map2.put("msg", "Error: Nama PP :" +" "+SpajExcelList.get(i).get(2).toString()+" "+err);
						pesanList.add(map2);						
					}
				}else if("18".equals(lca)){//gio
					
					String jenisSpaj = (SpajExcelList.get(i).get(1).toString());
					if( !(jenisSpaj.equals("5.0") || jenisSpaj.equals("5")) ){
						err = " Jenis SPAJ bukan GIO (5)";
					}
					
					HashMap detilInvest = new HashMap();
					HashMap mapReffBii = new HashMap();
					HashMap mapPemegang = uploadSetDataTemp.prosesSetDataPemegangSIO(SpajExcelList.get(i),bacDao,uwDao,user);
					excelListPp = (Pemegang) mapPemegang.get("PEMEGANG");
					HashMap mapTertanggung = uploadSetDataTemp.prosesSetDataTertanggungSIO(SpajExcelList.get(i), i, excelListPp,bacDao,uwDao);
					excelListTtg = (Tertanggung) mapTertanggung.get("TERTANGGUNG");	
					HashMap mapPayer = uploadSetDataTemp.prosesSetDataPemPremiSIO(SpajExcelList.get(i), i, excelListPp,bacDao);
					excelListpayer = (PembayarPremi) mapPayer.get("PAYER");	
					HashMap mapAgent = uploadSetDataTemp.prosesSetDataAgenSIO(SpajExcelList.get(i), i,bacDao,uwDao);
					excelListagen = (Agen) mapAgent.get("AGN");
					HashMap mapRekClient = uploadSetDataTemp.prosesSetDataRekClientSIO(SpajExcelList.get(i), i,bacDao,uwDao);
					excelListrekClient = (Rekening_client) mapRekClient.get("REKCLIENT");
					HashMap mapAccountRecur = uploadSetDataTemp.prosesSetDataAccountRecurSIO(SpajExcelList.get(i), i,bacDao,uwDao);
					rekAutodebet = (Account_recur) mapAccountRecur.get("RECUR");					
					HashMap mapDataUsulan = uploadSetDataTemp.prosesSetDataUsulanSIO(SpajExcelList.get(i), excelListTtg, excelListPp, i,bacDao,uwDao,suratUnitLink,defaultDateFormat);				
					excelListDatausulan = (Datausulan) mapDataUsulan.get("DATAUSULAN");
					if(excelListDatausulan.getLsbs_id()==120){
						detilInvest = uploadSetDataTemp.setDataInvestasi(SpajExcelList.get(i), excelListDatausulan, bacDao, suratUnitLink);
						excelListInvestasi = (InvestasiUtama) detilInvest.get("DATA");					
						mapReffBii.put("ERR", "");
					}
					if (detilInvest.isEmpty()){
						detilInvest.put("ERR", "");
					}
					HashMap mapBenef = uploadSetDataTemp.prosesSetDataBeneficiarySIO(SpajExcelList.get(i), i,bacDao,uwDao);
					if(mapBenef.isEmpty()){
						if(excelListDatausulan.getLsbs_id()==212 && (excelListDatausulan.getLsdbs_number()==4 || excelListDatausulan.getLsdbs_number()==5)){
							mapBenef.put("ERR", "Harus ada Penerima Manfaat!");
						}else{
							mapBenef.put("ERR", "");
						}
					}else{
						benef = (ArrayList<Benefeciary>) mapBenef.get("BENEF");
					}
//					HashMap mapQuestionare = uploadSetDataTemp.setDataQuestionareSIO(SpajExcelList.get(i),bacDao,uwDao,excelListDatausulan,user);
					ArrayList<Map> data_LQAPP = new ArrayList<Map>();				
					HashMap mapAddrBilling = uploadSetDataTemp.prosesSetDataAlamatPenagihanLink(SpajExcelList.get(i), excelListInvestasi, excelListDatausulan, bacDao, excelListPp, i);
					excelListAddrBilling = (AddressBilling) mapAddrBilling.get("ADDR");
					mapReffBii = uploadSetDataTemp.prosesSetDataReffBii(SpajExcelList.get(i),bacDao,user,uwDao,excelListagen);
					reffbii = (ReffBii) mapReffBii.get("REFF");
					mapReffBii.put("ERR", "");
					
					if(reffbii==null && excelListagen.getLca_id().equals("40") && excelListagen.getLwk_id().equals("01") && (excelListagen.getLsrg_id().equals("02")|| excelListagen.getLsrg_id().equals("11") || excelListagen.getLsrg_id().equals("12")) ){
						err += "Data Refferal tidak ditemukan. Polis ini harus mempunyai refferal yang valid.";
					}
					
					if(excelListDatausulan.getLsbs_id()==163){
						rekAutodebet.setFlag_autodebet_nb(0);
						rekAutodebet.setFlag_set_auto(0);
						rekAutodebet.setFlag_jn_tabungan(0);
						detilInvest.put("ERR", "");
					}
					
					//randy - validasi tanggal lahir pp dan ttg
					tanggal1 = Integer.parseInt(sdfDay.format(excelListPp.getMspe_date_birth()));
					bulan1 = Integer.parseInt(sdfMonth.format(excelListPp.getMspe_date_birth()));
					tahun1 = Integer.parseInt(sdfYear.format(excelListPp.getMspe_date_birth()));	
					tanggal2 = Integer.parseInt(sdfDay.format(excelListDatausulan.getMspr_beg_date()));	
					bulan2 = Integer.parseInt(sdfMonth.format(excelListDatausulan.getMspr_beg_date()));
					tahun2 = Integer.parseInt(sdfYear.format(excelListDatausulan.getMspr_beg_date()));
					try{
						String np ="produk_asuransi.n_prod_"+excelListDatausulan.getLsbs_id();
		    			aClass = Class.forName( np);
		    			produk1 = (n_prod)aClass.newInstance();
		    			produk1.ii_usia_pp = excelListPp.getMste_age();
		    			produk1.ii_age = excelListTtg.getMste_age();
		    			String hsl = "";
		    			produk1.ii_bisnis_no=excelListDatausulan.getLsdbs_number();
		    			produk1.of_set_bisnis_no(excelListDatausulan.getLsdbs_number());
						hsl = produk1.of_check_usia(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2, excelListDatausulan.getMspo_pay_period() , excelListDatausulan.getLsbs_id());
						if(!hsl.equals("")){ err += "; "+ hsl + " ";}
					}
					catch (ClassNotFoundException e1) {e1.printStackTrace();}
					catch (InstantiationException e) {e.printStackTrace();} 
					catch (IllegalAccessException e) {e.printStackTrace();}
					
					HashMap<String,String> map2= new HashMap<String, String>();
					err +=(String)mapPemegang.get("ERR")+(String)mapTertanggung.get("ERR")+(String)mapPayer.get("ERR")+
						  (String)mapAgent.get("ERR")+(String)mapBenef.get("ERR")+(String)mapRekClient.get("ERR")+(String)mapReffBii.get("ERR")+
						  (String)mapAccountRecur.get("ERR")+(String)mapDataUsulan.get("ERR")+(String)detilInvest.get("ERR")+(String)mapAddrBilling.get("ERR");	
					Integer jnbankdetbisnis = bacDao.selectJnBankDetBisnis(excelListDatausulan.getLsbs_id(), excelListDatausulan.getLsdbs_number());
					if("".equals(err)) err += validateUploadTM.validateCerdasCare(excelListPp,excelListTtg,excelListpayer,excelListagen,excelListDatausulan,excelListInvestasi,jnbankdetbisnis);
					if("".equals(err)){
						if(excelListagen.getMcl_first()!=null){
							excelListPp.setLca_id(excelListagen.getLca_id());
							excelListPp.setLwk_id(excelListagen.getLwk_id());
							excelListPp.setLsrg_id(excelListagen.getLsrg_id());
							excelListPp.setMsag_id(excelListagen.getMsag_id());
							excelListPp.setCampaign_id(Integer.parseInt(SpajExcelList.get(i).get(0).toString()));
							excelListPp.setMspo_no_blanko(generateNoBlanko(excelListPp.getCampaign_id().toString(),excelListDatausulan.getLsbs_id()));
							excelListAddrBilling.setLca_id(excelListagen.getLca_id());
							excelListAddrBilling.setLwk_id(excelListagen.getLwk_id());
							excelListAddrBilling.setLsrg_id(excelListagen.getLsrg_id());
							excelListDatausulan.setDaftabenef(benef);
							excelList.setPemegang(excelListPp);
							excelList.setTertanggung(excelListTtg);
							excelList.setDatausulan(excelListDatausulan);
							excelList.setAddressbilling(excelListAddrBilling);
							excelList.setAccount_recur(rekAutodebet);
							excelList.setPembayarPremi(new PembayarPremi());
							excelList.setInvestasiutama(excelListInvestasi);
							excelList.setRekening_client(excelListrekClient);
							excelList.setPembayarPremi(excelListpayer);
							insertSpajtempNew(excelList, null, null, reffbii, data_LQAPP);
							
						}else{
							err = "Data Agen Tidak ditemukan";
							map2.put("sts", "FAILED");
							map2.put("msg", "Error: Nama PP :" +" "+SpajExcelList.get(i).get(5).toString()+" "+err);
							pesanList.add(map2);	
						}
					}else{
						map2.put("sts", "FAILED");
						map2.put("msg", "Error: Nama PP :" +" "+SpajExcelList.get(i).get(5).toString()+" "+err);
						pesanList.add(map2);				
					}
				}else{//sio
					HashMap detilInvest = new HashMap();
					HashMap mapReffBii = new HashMap();
					HashMap mapPemegang = uploadSetDataTemp.prosesSetDataPemegangSIO(SpajExcelList.get(i),bacDao,uwDao,user);
					excelListPp = (Pemegang) mapPemegang.get("PEMEGANG");
					HashMap mapTertanggung = uploadSetDataTemp.prosesSetDataTertanggungSIO(SpajExcelList.get(i), i, excelListPp,bacDao,uwDao);
					excelListTtg = (Tertanggung) mapTertanggung.get("TERTANGGUNG");	
					HashMap mapPayer = uploadSetDataTemp.prosesSetDataPemPremiSIO(SpajExcelList.get(i), i, excelListPp,bacDao);
					excelListpayer = (PembayarPremi) mapPayer.get("PAYER");	
					HashMap mapAgent = uploadSetDataTemp.prosesSetDataAgenSIO(SpajExcelList.get(i), i,bacDao,uwDao);
					excelListagen = (Agen) mapAgent.get("AGN");
					HashMap mapBenef = uploadSetDataTemp.prosesSetDataBeneficiarySIO(SpajExcelList.get(i), i,bacDao,uwDao);
					if(mapBenef.isEmpty()){
						mapBenef.put("ERR", "");
					}else{
						benef = (ArrayList<Benefeciary>) mapBenef.get("BENEF");
					}
					HashMap mapRekClient = uploadSetDataTemp.prosesSetDataRekClientSIO(SpajExcelList.get(i), i,bacDao,uwDao);
					excelListrekClient = (Rekening_client) mapRekClient.get("REKCLIENT");
					HashMap mapAccountRecur = uploadSetDataTemp.prosesSetDataAccountRecurSIO(SpajExcelList.get(i), i,bacDao,uwDao);
					rekAutodebet = (Account_recur) mapAccountRecur.get("RECUR");					
					HashMap mapDataUsulan = uploadSetDataTemp.prosesSetDataUsulanSIO(SpajExcelList.get(i), excelListTtg, excelListPp, i,bacDao,uwDao,suratUnitLink,defaultDateFormat);				
					excelListDatausulan = (Datausulan) mapDataUsulan.get("DATAUSULAN");
					if(excelListDatausulan.getLsbs_id()==120){
						detilInvest = uploadSetDataTemp.setDataInvestasi(SpajExcelList.get(i), excelListDatausulan, bacDao, suratUnitLink);
						excelListInvestasi = (InvestasiUtama) detilInvest.get("DATA");					
						mapReffBii.put("ERR", "");
					}
					if (detilInvest.isEmpty()){
						detilInvest.put("ERR", "");
					}
					if(excelListDatausulan.getLsbs_id()==212 && (excelListDatausulan.getLsdbs_number()==4 || excelListDatausulan.getLsdbs_number()==5 ||  excelListDatausulan.getLsdbs_number()==7)){
						if(benef.isEmpty()){
							mapBenef.put("ERR", "Harus ada Penerima Manfaat!");
						}
					}
					HashMap mapQuestionare = uploadSetDataTemp.setDataQuestionareSIO(SpajExcelList.get(i),bacDao,uwDao,excelListDatausulan,user);
					ArrayList<Map> data_LQAPP = (ArrayList) mapQuestionare.get("DATA");				
					HashMap mapAddrBilling = uploadSetDataTemp.prosesSetDataAlamatPenagihanLink(SpajExcelList.get(i), excelListInvestasi, excelListDatausulan, bacDao, excelListPp, i);
					excelListAddrBilling = (AddressBilling) mapAddrBilling.get("ADDR");
					mapReffBii = uploadSetDataTemp.prosesSetDataReffBii(SpajExcelList.get(i),bacDao,user,uwDao, excelListagen);
					reffbii = (ReffBii) mapReffBii.get("REFF");
			//		mapReffBii.put("ERR", "");

					MedQuest_tambah SimpleSIO = new MedQuest_tambah();
					if(lca.equalsIgnoreCase("19") && (SpajExcelList.get(i).size() - 1) >= 210){
						//helpdesk [148055] produk DMTM Dana Sejaterah 163 26-30 & Smile Sarjana 173 7-9, untuk UP > 200jt SIO ada tambah questionare
						uploadSetDataTemp.setDataSimpleQuestionareSIO(SpajExcelList.get(i), excelListDatausulan, SimpleSIO);
					}
					
					if(excelListDatausulan.getLsbs_id()==163){					
						rekAutodebet.setFlag_autodebet_nb(0);
						rekAutodebet.setFlag_set_auto(0);
						rekAutodebet.setFlag_jn_tabungan(0);
						detilInvest.put("ERR", "");
					}
					
					//randy - validasi tanggal lahir pp dan ttg
					tanggal1 = Integer.parseInt(sdfDay.format(excelListPp.getMspe_date_birth()));
					bulan1 = Integer.parseInt(sdfMonth.format(excelListPp.getMspe_date_birth()));
					tahun1 = Integer.parseInt(sdfYear.format(excelListPp.getMspe_date_birth()));	
					tanggal2 = Integer.parseInt(sdfDay.format(excelListDatausulan.getMspr_beg_date()));	
					bulan2 = Integer.parseInt(sdfMonth.format(excelListDatausulan.getMspr_beg_date()));
					tahun2 = Integer.parseInt(sdfYear.format(excelListDatausulan.getMspr_beg_date()));
					try{
						String np ="produk_asuransi.n_prod_"+excelListDatausulan.getLsbs_id();
		    			aClass = Class.forName( np);
		    			produk1 = (n_prod)aClass.newInstance();
		    			produk1.ii_usia_pp = excelListPp.getMste_age();
		    			produk1.ii_age = excelListTtg.getMste_age();
		    			produk1.ii_bisnis_no = excelListDatausulan.getLsdbs_number();
		    			String hsl = "";
						hsl = produk1.of_check_usia(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2, excelListDatausulan.getMspo_pay_period() , excelListDatausulan.getLsbs_id());
						if(!hsl.equals("")){ err += "; "+ hsl + " ";}
						
						/**
						 * DMTM SMART KID BTN 208 => 29,30,31,32
						 * Patar Timotius 
						 * 02/08/2018
						 * cek tertanggung nya ya
						 */
						boolean isSmartKidBTN = excelListDatausulan.getLsbs_id() == 208 && (excelListDatausulan.getLsdbs_number() >= 29 && excelListDatausulan.getLsdbs_number() <= 32) ;
						
						/**
						 * DMTM Smile KID BJB 208 => 45,46,47,48
						 * Patar Timotius 
						 * 05/09/2019
						 * cek tertanggung nya ya
						 */
						boolean isSmileKidBJB = excelListDatausulan.getLsbs_id() == 208 && (excelListDatausulan.getLsdbs_number() >= 45 && excelListDatausulan.getLsdbs_number() <= 48) ;
					
						
						/**
						 * DMTM SIMAS KID BSIM 208 => 33,34,35,36
						 * Patar Timotius 
						 * 08/08/2018
						 * cek tertanggung nya ya
						 */
						boolean isSimasKidBSIM = excelListDatausulan.getLsbs_id() == 208 && (excelListDatausulan.getLsdbs_number() >= 33 && excelListDatausulan.getLsdbs_number() <= 36) ;

						if(isSmartKidBTN || isSmileKidBJB){				
							/**
							 * please cek usia tertanggung nya
							 * Patar Timotius
							 * 02/08/2018
							 */
							tanggal1 = Integer.parseInt(sdfDay.format(excelListTtg.getMspe_date_birth()));
							bulan1 = Integer.parseInt(sdfMonth.format(excelListTtg.getMspe_date_birth()));
							tahun1 = Integer.parseInt(sdfYear.format(excelListTtg.getMspe_date_birth()));	
							hsl = produk1.of_check_usia(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2, excelListDatausulan.getMspo_pay_period() , excelListDatausulan.getLsbs_id());
									
							
							if(!hsl.equals("")){ err += "; "+ hsl + " ";};
						}
						
						if(isSimasKidBSIM){				
							/**
							 * please cek usia tertanggung nya
							 * Patar Timotius
							 * 08/08/2018
							 */
							tanggal1 = Integer.parseInt(sdfDay.format(excelListTtg.getMspe_date_birth()));
							bulan1 = Integer.parseInt(sdfMonth.format(excelListTtg.getMspe_date_birth()));
							tahun1 = Integer.parseInt(sdfYear.format(excelListTtg.getMspe_date_birth()));	
							hsl = produk1.of_check_usia(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2, excelListDatausulan.getMspo_pay_period() , excelListDatausulan.getLsbs_id());
									
							
							if(!hsl.equals("")){ err += "; "+ hsl + " ";};
						}
						
						
						
						if(excelListDatausulan.getLsbs_id() == 212 && excelListDatausulan.getLsdbs_number() == 8 ) {//NISSAN TERM ROP
							//validasi premi
							hsl = "";
							hsl = produk1.of_alert_min_premi(excelListDatausulan.getMspr_premium());
							if(!hsl.equals("")){ err += "; "+ hsl + " ";}
						}else if( (excelListDatausulan.getLsbs_id() == 73 && excelListDatausulan.getLsdbs_number() == 15)
								|| (excelListDatausulan.getLsbs_id() == 203 && excelListDatausulan.getLsdbs_number() == 4)) {//NISSAN PA A & DBD
							//up
							hsl = "";
							hsl = produk1.of_alert_min_up(excelListDatausulan.getMspr_tsi());
							if(!hsl.equals("")){ err += "; "+ hsl + " ";}
						}else if(isSmartKidBTN || isSmileKidBJB){
							/**
							 * DMTM SMARTKID BTN
							 * Patar Timoitus
							 * 02/08/2018
							 * 
							 */
							hsl = "";
							hsl = produk1.of_alert_min_up(excelListDatausulan.getMspr_tsi());
							if(!hsl.equals("")){ err += "; "+ hsl + " ";}
							if(hsl.trim().equals("")){
								hsl = produk1.of_alert_max_up(excelListDatausulan.getMspr_tsi());
								if(!hsl.equals("")){ err += "; "+ hsl + " ";}
							}
							
							
						}else if(isSimasKidBSIM){
							/**
							 * DMTM SMARTKID BTN
							 * Patar Timoitus
							 * 08/08/2018
							 * 
							 */
							hsl = "";
							hsl = produk1.of_alert_min_up(excelListDatausulan.getMspr_tsi());
							if(!hsl.equals("")){ err += "; "+ hsl + " ";}
							if(hsl.trim().equals("")){
								hsl = produk1.of_alert_max_up(excelListDatausulan.getMspr_tsi());
								if(!hsl.equals("")){ err += "; "+ hsl + " ";}
							}
							
						}
						
					}
					catch (ClassNotFoundException e1) {logger.error("ERROR :", e1);}
					catch (InstantiationException e) {logger.error("ERROR :", e);} 
					catch (IllegalAccessException e) {logger.error("ERROR :", e);}
					
					HashMap<String,String> map2= new HashMap<String, String>();
					err +=(String)mapPemegang.get("ERR")+(String)mapTertanggung.get("ERR")+(String)mapPayer.get("ERR")+
						  (String)mapAgent.get("ERR")+(String)mapBenef.get("ERR")+(String)mapRekClient.get("ERR")+(String)mapReffBii.get("ERR")+
						  (String)mapAccountRecur.get("ERR")+(String)mapDataUsulan.get("ERR")+(String)detilInvest.get("ERR")+(String)mapAddrBilling.get("ERR");	
					Integer jnbankdetbisnis = bacDao.selectJnBankDetBisnis(excelListDatausulan.getLsbs_id(), excelListDatausulan.getLsdbs_number());
					if("".equals(err))err +=validateUploadTM.validateCerdasCare(excelListPp,excelListTtg,excelListpayer,excelListagen,excelListDatausulan,excelListInvestasi,jnbankdetbisnis);
					if("".equals(err)){					
						if(excelListagen.getMcl_first()!=null){
							excelListPp.setLca_id(excelListagen.getLca_id());
							excelListPp.setLwk_id(excelListagen.getLwk_id());
							excelListPp.setLsrg_id(excelListagen.getLsrg_id());
							excelListPp.setMsag_id(excelListagen.getMsag_id());
							excelListPp.setCampaign_id(Integer.parseInt(SpajExcelList.get(i).get(0).toString()));
							excelListPp.setMspo_no_blanko(generateNoBlanko(excelListPp.getCampaign_id().toString(),excelListDatausulan.getLsbs_id()));
							excelListAddrBilling.setLca_id(excelListagen.getLca_id());
							excelListAddrBilling.setLwk_id(excelListagen.getLwk_id());
							excelListAddrBilling.setLsrg_id(excelListagen.getLsrg_id());
							excelListDatausulan.setDaftabenef(benef);
							excelList.setPemegang(excelListPp);
							excelList.setTertanggung(excelListTtg);
							excelList.setDatausulan(excelListDatausulan);
							excelList.setAddressbilling(excelListAddrBilling);
							excelList.setAccount_recur(rekAutodebet);
							excelList.setPembayarPremi(new PembayarPremi());
							excelList.setInvestasiutama(excelListInvestasi);
							excelList.setRekening_client(excelListrekClient);
							excelList.setPembayarPremi(excelListpayer);		
							excelList.setMedQuest_tambah(SimpleSIO); //helpdesk [148055] produk DMTM Dana Sejaterah 163 26-30 & Smile Sarjana 173 7-9, untuk UP > 200jt SIO ada tambah questionare
							insertSpajtempNew(excelList, null, null, reffbii, data_LQAPP);
							
						}else{
							err = "Data Agen Tidak ditemukan";
							map2.put("sts", "FAILED");
							map2.put("msg", "Error: Nama PP :" +" "+SpajExcelList.get(i).get(5).toString()+" "+err);
							pesanList.add(map2);	
						}
					}else{
						map2.put("sts", "FAILED");
						map2.put("msg", "Error: Nama PP :" +" "+SpajExcelList.get(i).get(5).toString()+" "+err);
						pesanList.add(map2);				
					}
				}
			}
		}//end for
		return pesanList;
	}
	

	private MedQuest prosesQuestionareEkaWaktu(List<List> spajExcelList,
		Integer li_umur_ttg, int i, Integer mspe_sex) {
		BigDecimal heigth =new BigDecimal(0);
    	BigDecimal bmi =new BigDecimal(0);
    	BigDecimal bmi2 =new BigDecimal(100);
    	Map em=new HashMap();
    	String yes=null;
    	Integer ya,lama_hamil;
 		MedQuest mquest=new MedQuest();
 		String medis,penyakit,hamil,berat,dirawat,waktu,lamanya,obat,konsumsi,tempatBerobat,dokter,desc = null,mcu= null,pemeriksaan,waktuMcu,hasil ;
 		mquest.setMsadm_berat(Integer.parseInt(spajExcelList.get(i).get(98).toString().replace(".0", "")))	;	
 		mquest.setMsadm_tinggi(Integer.parseInt(spajExcelList.get(i).get(99).toString().replace(".0", "")));
		yes=spajExcelList.get(i).get(100).toString().trim();
	    if(yes.equalsIgnoreCase("YA")){
		   ya=1;
		}else if(yes.equalsIgnoreCase("TIDAK")){
		   ya=0;
		}else{
		   ya=null;
		}
		mquest.setMsadm_sehat(ya);
		if(mquest.getMsadm_sehat()==0){
		   penyakit=spajExcelList.get(i).get(101).toString()+",";
		   if(penyakit!=null){
			  mquest.setMsadm_penyakit(1);						 							
		   }
				
		}else{
			 mquest.setMsadm_penyakit(0);
		}
		dirawat=spajExcelList.get(i).get(102).toString().trim();						 						
		if(dirawat.equalsIgnoreCase("YA")){
			waktu=spajExcelList.get(i).get(103).toString()+",";
			lamanya="Lama Dirawat:"+""+spajExcelList.get(i).get(104).toString()+",";
			obat="Obat yang Dikonsumsi:"+""+spajExcelList.get(i).get(105).toString()+",";
			konsumsi="Obat itu Masih Dikonsumsi Sampai Sekarang:"+""+spajExcelList.get(i).get(106).toString()+",";
			tempatBerobat=dokter="Berobat Di:"+""+spajExcelList.get(i).get(107).toString()+",";
			dokter="Nama Dokter:"+""+spajExcelList.get(i).get(108).toString()+"";
			desc=waktu+lamanya+obat+konsumsi+tempatBerobat+dokter;							 							
		}
		mquest.setMsadm_penyakit_desc(desc);
		mcu=spajExcelList.get(i).get(109).toString();//klo ga da kosongin aja exelnya						 					
			
		if(!mcu.equals("")){
			pemeriksaan=spajExcelList.get(i).get(109).toString()+",";
			waktuMcu=spajExcelList.get(i).get(110).toString()+",";
			hasil=spajExcelList.get(i).get(111).toString();
			mquest.setMsadm_medis(1);
			mquest.setMsadm_medis_desc("Pemeriksaan"+pemeriksaan+"Waktu Mcu:"+waktuMcu+""+"Hasil:"+hasil);							 					
		}else{
			mquest.setMsadm_medis(0);
		}
		mquest.setMsadm_sehat_desc(desc);
		mquest.setMste_insured_no(1);
		mquest.setSex(mspe_sex);
		if(mspe_sex==0){  
		   hamil=spajExcelList.get(i).get(112).toString().trim();				
		   if(hamil.equalsIgnoreCase("YA")){
			  lama_hamil=Integer.parseInt(spajExcelList.get(i).get(113).toString().replace(".0", ""));
			  mquest.setMsadm_pregnant(1);
			  mquest.setMsadm_pregnant_time(lama_hamil);							 						
		   }else if(hamil.equalsIgnoreCase("TIDAK")){
			  mquest.setMsadm_pregnant(0);
		   }else{
			//err=err+"Untuk Wanita harus mengisi kolom pertanyaan khusus wanita";
		   }
	    }
			if(mquest.getMsadm_berat()!=null && mquest.getMsadm_tinggi()!=null){
				heigth= new BigDecimal(mquest.getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
			    bmi = new BigDecimal(mquest.getMsadm_berat()).divide(heigth,0,RoundingMode.HALF_UP);
			    mquest.setMsadm_bmi(bmi.doubleValue());
			    em = uwDao.selectEmBmi(bmi.doubleValue(),li_umur_ttg);
			    mquest.setMsadm_em(em);
			    mquest.setMsadm_em_life( new Double(em.get("LIFE").toString()) );
			   if(mquest.getMsadm_em_life()!=0){
				   mquest.setMsadm_clear_case(0);
				}else{
					mquest.setMsadm_clear_case(1);
				}	 					   
			    
			}else{
				//err=err+"Kolom berat/tinggi badan tertanggung tambahan 1 masih kosong,";
			}
	
 		return mquest;
	}
	
	private MedQuest prosesQuestionareDS(List<List> spajExcelList, Integer li_umur_ttg, int i, Integer sex) {
		BigDecimal heigth =new BigDecimal(0);
    	BigDecimal bmi =new BigDecimal(0);
    	BigDecimal bmi2 =new BigDecimal(100);
    	Map em=new HashMap();    
 		MedQuest mquest=new MedQuest();
 		String medis,penyakit,hamil,berat= null ;
 		
 		mquest.setMsadm_berat(Integer.parseInt(spajExcelList.get(i).get(98).toString().replace(".0", "")))	;	
 		mquest.setMsadm_tinggi(Integer.parseInt(spajExcelList.get(i).get(99).toString().replace(".0", "")));
		berat=spajExcelList.get(i).get(100).toString();	
		if(berat.equals("YA")){
			mquest.setMsadm_berat_berubah(1);
			mquest.setMsadm_berubah_desc(spajExcelList.get(i).get(101).toString());
		}else{
			 
			mquest.setMsadm_berat_berubah(0);
		}
		medis=spajExcelList.get(i).get(102).toString();
		if(medis.equals("YA")){
			mquest.setMsadm_medis(1);
			mquest.setMsadm_berubah_desc(spajExcelList.get(i).get(103).toString());
		}else{
			mquest.setMsadm_medis(0);
		}
		penyakit=spajExcelList.get(i).get(104).toString();
		if(penyakit.equals("YA")){
			mquest.setMsadm_penyakit(1);
			mquest.setMsadm_penyakit_desc(spajExcelList.get(i).get(105).toString());
			mquest.setMsadm_sehat(0);
		}else{
			mquest.setMsadm_penyakit(0);
			mquest.setMsadm_sehat(1);
		}
		if(sex==0){
		    hamil=spajExcelList.get(i).get(104).toString();
			if(hamil.equals("YA")){
				mquest.setMsadm_pregnant(1);
				mquest.setMsadm_pregnant_desc(spajExcelList.get(i).get(105).toString());
			}else{
				mquest.setMsadm_pregnant(0);
			}
		}
		heigth= new BigDecimal(mquest.getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
		bmi = new BigDecimal(mquest.getMsadm_berat()).divide(heigth,0,RoundingMode.HALF_UP);
		mquest.setMsadm_bmi(bmi.doubleValue());
	    em = uwDao.selectEmBmi(bmi.doubleValue(),li_umur_ttg);
	    mquest.setMsadm_em(em);
	    mquest.setMsadm_em_life( new Double(em.get("LIFE").toString()) );
		if(mquest.getMsadm_em_life()==0){
			mquest.setMsadm_clear_case(0);
		}else{
			mquest.setMsadm_clear_case(1);
		}				
		mquest.setSex(sex);
		mquest.setMste_insured_no(0);
		return mquest;
	}

	private List<Benefeciary> prosesBenefeciary(List<List> spajExcelList, int i,
		Integer jumlahBenef) throws ParseException {
		
		List<Benefeciary> benef2 = new ArrayList<Benefeciary>();
		
		SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
		f_replace konteks = new f_replace();
		for(int j=1;j<=jumlahBenef;j++){
			String msaw_first = null ,lsre_id = null, msaw_sex = null,msaw_persen = null;
			Date bod = null ;
			Benefeciary bf = new Benefeciary();
			Integer nobf = 0;
			String bod1=spajExcelList.get(i).get(64).toString();
			String bod2=spajExcelList.get(i).get(69).toString();
			String bod3=spajExcelList.get(i).get(74).toString();
			String bod4=spajExcelList.get(i).get(79).toString();
			
			switch (j) {
			case 1:
				msaw_first=spajExcelList.get(i).get(63).toString();
				if(!bod1.equals("") || bod1!=null)bod = df2.parse(spajExcelList.get(i).get(64).toString());
				msaw_sex=spajExcelList.get(i).get(65).toString().replace(".0", "");
				lsre_id=spajExcelList.get(i).get(66).toString().replace(".0", "");
				msaw_persen=spajExcelList.get(i).get(67).toString();
				nobf=1;
				break;

			case 2:
				msaw_first=spajExcelList.get(i).get(68).toString();
				if(!bod2.equals("") || bod2!=null)bod = df2.parse(spajExcelList.get(i).get(69).toString());
				msaw_sex=spajExcelList.get(i).get(70).toString().replace(".0", "");
				lsre_id=spajExcelList.get(i).get(71).toString().replace(".0", "");
				msaw_persen=spajExcelList.get(i).get(72).toString().replace(".0", "");
				nobf=2;
				break;
			case 3:
				msaw_first=spajExcelList.get(i).get(73).toString();
				if(!bod3.equals("") || bod3!=null)bod = df2.parse(spajExcelList.get(i).get(74).toString());
				msaw_sex=spajExcelList.get(i).get(75).toString().replace(".0", "");
				lsre_id=spajExcelList.get(i).get(76).toString().replace(".0", "");
				msaw_persen=spajExcelList.get(i).get(77).toString().replace(".0", "");
				nobf=3;
				break;
				
			case 4:
				msaw_first=spajExcelList.get(i).get(78).toString();
				if(!bod4.equals("") || bod4!=null)bod = df2.parse(spajExcelList.get(i).get(79).toString());
				msaw_sex=spajExcelList.get(i).get(80).toString().replace(".0", "");
				lsre_id=spajExcelList.get(i).get(81).toString().replace(".0", "");
				msaw_persen=spajExcelList.get(i).get(82).toString().replace(".0", "");
				nobf=4;
				break;
			}
			
			if (msaw_first==null){
				msaw_first="";
			}

			if (lsre_id==null){
				lsre_id="1";
			}
			
			if (msaw_persen.trim().length()==0){
				msaw_persen="0";
			}else{
				msaw_persen=konteks.f_replace_persen(msaw_persen);
				boolean cekk1 = f_validasi.f_validasi_numerik1(msaw_persen);
				if (cekk1 == false){
					msaw_persen="0";
				}
			}

			
			bf.setMsaw_first(msaw_first);
			bf.setMsaw_birth(bod);
			bf.setLsre_id(Integer.parseInt(lsre_id));
			bf.setMsaw_persen(Double.parseDouble(msaw_persen));
			bf.setMsaw_sex(Integer.parseInt(msaw_sex));
			bf.setMsaw_number(nobf);
			bf.setMste_insured_no(1);
			benef2.add(bf);
		}
		
		return benef2;
}

	private List<Datarider> prosesRider(List<List> spajExcelList, int i,
		Datausulan excelListDatausulan, Integer tanggalEd, Integer bulanEd,
		Integer tahunEd, Integer tahun, Integer bulan, Integer tanggal, int flag_jenis_plan, int flag_cerdas_siswa, Integer insperiod, Integer payperiod) {
		
		List<Datarider> dtrd2 = new ArrayList<Datarider>();
		String kode_rider=spajExcelList.get(i).get(51).toString();		
		Class aClass2;	
		n_prod produk2 = new n_prod();
		
	    if(!kode_rider.equals("")){
		    String[] dataRider=kode_rider.split(";");
			List<String> listRider = Arrays.asList(dataRider);				 
			int tahun_rider,bulan_rider,tanggal_rider;String nama_produk_rider=null;
			for(int j=0; j<listRider.size(); j++){
			 	 Datarider dtr=new Datarider();
				 String kode=listRider.get(j);
				 Integer lsbs_id_rider=Integer.parseInt(kode.substring(0, 3));
				 Integer lsdbs_number_rider=Integer.parseInt(kode.substring(4));							
				 Integer unit = null;
				 Integer klas=null; 
				 Double premi = null;
				 Double up = null;
				 
				 Integer factor = null;
				 Double rate_rider = null;
				
				 try {							  
						nama_produk_rider="produk_asuransi.n_prod_"+lsbs_id_rider;
						aClass2 = Class.forName( nama_produk_rider);
						produk2 = (n_prod)aClass2.newInstance();	
						produk2.setSqlMap(uwDao.getSqlMapClient());
						produk2.of_set_bisnis_no(lsbs_id_rider);
						produk2.ii_bisnis_id=lsbs_id_rider;
						produk2.ii_bisnis_no=lsdbs_number_rider;
						produk2.ii_usia_pp=excelListDatausulan.getLi_umur_pp();
						produk2.ii_usia_tt=excelListDatausulan.getLi_umur_ttg();
						produk2.ii_age=produk2.ii_usia_tt;
						tahun_rider = Integer.parseInt(sdfYear.format(excelListDatausulan.getMspr_beg_date()));
						bulan_rider= Integer.parseInt(sdfMonth.format(excelListDatausulan.getMspr_beg_date()));
						tanggal_rider = Integer.parseInt(sdfDay.format(excelListDatausulan.getMspr_beg_date()));
						produk2.of_set_begdate(tahun_rider,bulan_rider,tanggal_rider);	
						dtr.setMspr_beg_date(excelListDatausulan.getMspr_beg_date());	
						dtr.setLsbs_id(lsbs_id_rider);
						dtr.setLsdbs_number(lsdbs_number_rider);
						if(!spajExcelList.get(i).get(55).toString().equals(""))unit=Integer.parseInt(spajExcelList.get(i).get(55).toString().replace(".0", ""));
						
						if(!spajExcelList.get(i).get(57).toString().equals(""))klas=Integer.parseInt(spajExcelList.get(i).get(57).toString().replace(".0", ""));
						if(unit==null)unit=0;
						if(klas==null)klas=0;
						if(!spajExcelList.get(i).get(56).toString().equals(""))factor=Integer.parseInt(spajExcelList.get(i).get(56).toString().replace(".0", ""));
					    if(factor==null)factor=0;							
						
					   Date sysdate = commonDao.selectSysdateTrunc();	
					   f_hit_umur umr = new f_hit_umur();
					   int hub=0;
					   int jenis = 0;
							
						produk2.is_kurs_id=excelListDatausulan.getKurs_premi();										
						//ENDATE DAN PAYDATE RIDER								
						produk2.wf_set_premi(tahun.intValue(),bulan.intValue(),tanggal.intValue(),excelListDatausulan.getLscb_id().intValue(),tahunEd.intValue(),bulanEd.intValue(),tanggalEd.intValue(),insperiod.intValue(),flag_jenis_plan,produk2.ii_age,payperiod.intValue(),flag_cerdas_siswa, excelListDatausulan.getLi_umur_pp().intValue(),produk2.ii_bisnis_id,produk2.ii_bisnis_no);
						Integer tanggal_akhir_polis1=new Integer(produk2.ldt_edate.getTime().getDate());
						Integer bulan_akhir_polis1=new Integer(produk2.ldt_edate.getTime().getMonth()+1);
						Integer tahun_akhir_polis1=new Integer(produk2.ldt_edate.getTime().getYear()+1900);																	
						
						String tgl_akhir_polis1=null;
						if(produk2.ii_bisnis_id==829){
							tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tahunEd.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulanEd.intValue()),2)+"/"+Integer.toString(tahunEd.intValue());
						}else{
							tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
						}
						if ( tgl_akhir_polis1.trim().length()!=0)
						{
							dtr.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis1));
						}else{
							dtr.setMspr_end_date(null);
						}
						Calendar cal2 = Calendar.getInstance();	
						cal2.setTime(dtr.getMspr_end_date());
						cal2.add(Calendar.MONTH, -1);
						cal2.add(Calendar.DATE, 1);
						Date end_pay_rider=cal2.getTime();
						
						if ( end_pay_rider!=null)
						{
							dtr.setMspr_end_pay(end_pay_rider);
						}else{
							dtr.setMspr_end_pay(null);
						}
						if(dtr.getLsbs_id()==813){
							rate_rider=produk2.of_get_rate1(klas, flag_jenis_plan, produk2.ii_bisnis_no, produk2.ii_usia_tt, produk2.ii_usia_pp) ;
							up=produk2.of_get_up_with_factor(0, excelListDatausulan.getMspr_tsi(), unit, flag_jenis_plan,produk2.ii_bisnis_id, produk2.ii_bisnis_no,  0, factor);
							premi=produk2.hit_premi_rider(rate_rider, excelListDatausulan.getMspr_tsi(), 0.1, 0);;
							
						}else{
							rate_rider=produk2.of_get_rate1(klas, flag_jenis_plan, produk2.ii_bisnis_no, produk2.ii_usia_tt, produk2.ii_usia_pp) ;
							up=produk2.of_get_up(0,  excelListDatausulan.getMspr_tsi(), unit, flag_jenis_plan, produk2.ii_bisnis_id, produk2.ii_bisnis_no, 0);
							premi=produk2.hit_premi_rider(rate_rider, up, 0.1, 0);
							
						}
						
						dtr.setMspr_ins_period(produk2.li_insured);
						dtr.setMspr_tsi_pa_a(produk2.up_pa);
						dtr.setMspr_tsi_pa_b(produk2.up_pb);
						dtr.setMspr_tsi_pa_c(produk2.up_pc);
						dtr.setMspr_tsi_pa_d(produk2.up_pd);
						dtr.setMspr_rate(rate_rider);
						dtr.setMspr_premium(premi);
						dtr.setMspr_tsi(up);
						dtr.setMspr_unit(unit);
						dtr.setMspr_class(klas);
						dtr.setLku_id(produk2.is_kurs_id);
						
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e1);
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e);
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e);
					} catch (ParseException e) {
						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
						
					}
				 
				dtrd2.add(dtr);		
			}     
	    }    
	 return dtrd2;
}

	private String generateNoBlanko(String id, Integer lsbs) {
		Long counter=bacDao.selectCounterBlanko(id);
		String no_blanko=null;
		if (id.equals("2")){
			no_blanko = "ARC"+StringUtils.leftPad(counter.toString(), 5, '0');
		}else if(id.equals("3")){
			no_blanko = "SMP"+StringUtils.leftPad(counter.toString(), 5, '0');
		}else if(id.equals("5")){
			if(lsbs==120){
				no_blanko = "DMTM"+StringUtils.leftPad("0000"+counter.toString(), 5, '0');
			}else{
				no_blanko = "DMTM"+StringUtils.leftPad(counter.toString(), 5, '0');
			}
			
		}else if(id.equals("6")){
			no_blanko = "SSS"+StringUtils.leftPad(counter.toString(), 5, '0');
		}else if(id.equals("7")){
			no_blanko = "SIP"+StringUtils.leftPad(counter.toString(), 5, '0');
		}else if(id.equals("8")){
			no_blanko = "BSM"+StringUtils.leftPad(counter.toString(), 5, '0');
		}else if(id.equals("9")){
			no_blanko = "RB"+StringUtils.leftPad(counter.toString(), 5, '0');
		}else if(id.equals("10")){
			no_blanko = "PSJS"+StringUtils.leftPad(counter.toString(), 5, '0');
		}else if(id.equals("12")){
			no_blanko = "SAP"+StringUtils.leftPad(counter.toString(), 5, '0');
		}else if(id.equals("13")){
			no_blanko = "DEN"+StringUtils.leftPad(counter.toString(), 5, '0');
		}else if(id.equals("14")){
			no_blanko = "MARZ"+StringUtils.leftPad(counter.toString(), 8, '0');
		}else if(id.equals("15")){
			no_blanko = "VALDO"+StringUtils.leftPad(counter.toString(), 7, '0');
		}else if(id.equals("16")){
			no_blanko = "GOS"+StringUtils.leftPad(counter.toString(), 9, '0');
		}else if(id.equals("20")){
			no_blanko = "VASCO"+StringUtils.leftPad(counter.toString(), 7, '0');
		}else if(id.equals("21")){
			no_blanko = "NISSAN"+StringUtils.leftPad(counter.toString(), 6, '0');
		}else if(id.equals("22")){
			no_blanko = "SYNERG"+StringUtils.leftPad(counter.toString(), 6, '0');
		}else if(id.equals("23")){
			no_blanko = "SSI"+StringUtils.leftPad(counter.toString(), 9, '0');
		}else if(id.equals("24")){
			no_blanko = "APK"+StringUtils.leftPad(counter.toString(), 9, '0');
		}else if(id.equals("25")){
			no_blanko = "ABH"+StringUtils.leftPad(counter.toString(), 9, '0');
		}
		counter+=1;
		bacDao.UpdateCounterBlanko(counter,id);
		
		
		return no_blanko;
	}

	public List<Map> insertSpajtemp(Cmdeditbac excelList, String reff_bank, String agent_bank, ReffBii reffbii) {
		String no_temp = null;
		List<Map> pesanList = null;
		
		//proses insert
		//update no_temp
		Long intIDCounter = (Long) this.bacDao.select_counter(158, "01");
	    int intID = intIDCounter.intValue();
		Calendar tgl_sekarang = Calendar.getInstance(); 
		Integer inttgl2 = new Integer(tgl_sekarang.get(Calendar.YEAR));
		if (intIDCounter.longValue() == 0)
		{
			intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 1000000));
		}else{
			Integer inttgl1 = new Integer(uwDao.selectGetCounterMonthYear(113, "01"));

			if (inttgl1.intValue() != inttgl2.intValue()){					
					intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
					
					//ganti dengan tahun skarang
					uwDao.updateCounterMonthYear(inttgl2.toString(), 113, "01");
					 
					//reset nilai counter dengan 0
					intID = 0;
					uwDao.updateMstCounter2("0", 113, "01");
					logger.info("update mst counter start di tahun baru");
			}else{
				intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000"))+intIDCounter);
			}
		}
		
		//--------------------------------------------
		Long intFireId = new Long(intIDCounter.longValue() + 1);
		intID = intID + 1;
		uwDao.updateMstCounter3(intID, 158, "01");
		no_temp= intFireId.toString();
		excelList.setNo_temp(no_temp);
		
		//insert mst_spaj_temp
		prosesInsertSpajTemp(excelList,no_temp,reff_bank,agent_bank);
		
		//insert mst_address_billing_temp
		prosesInsertAddressBilling(excelList,no_temp);
		
		//insert mst_produt_temp
		prosesInsertProductTemp(excelList,no_temp);
		
		//insert rider
		if(excelList.getDatausulan().getDaftaRider()!=null){
			prosesSaveRider(excelList,no_temp);
		}
		
		//insert_peserta_temp
		if(excelList.getDatausulan().getDaftapeserta()!=null){
			prosesInsertPeserta(excelList,no_temp);
		}
		
		if(excelList.getDatausulan().getLsbs_id()!=120){
			prosesInsertMedQuest(excelList,no_temp);
		}
		
		//insert_acc_recur
		if(excelList.getAccount_recur()!=null && (excelList.getDatausulan().getLsbs_id()==163 || excelList.getDatausulan().getLsbs_id()==169 || excelList.getDatausulan().getLsbs_id()==120)){
			prosesInsertAccount_recur(excelList,no_temp);
		}
		if(reffbii.getLrb_id()!=null){
			prosesInsertReffBii(reffbii,no_temp);
		
		}
		if( excelList.getDatausulan().getLsbs_id()==169 ){
			prosesSaveBenef(excelList, no_temp);
		}
				
//				if(excelList.getDatausulan().getLsbs_id()==120){
//					prosesSaveRekeningClient(excelList,no_temp);
//					prosesSaveUlink(excelList,excelList.getInvestasiutama(),no_temp);
//				}
				
		return pesanList;
	}
	
	private void prosesInsertReffBii(ReffBii reffbii, String no_temp) {
		bacDao.insertReffbiiTemp(reffbii,no_temp);
	}

	private void prosesInsertAccount_recur(Cmdeditbac excelList, String no_temp) {
		bacDao.insertAccount_recur(excelList,no_temp);
	}

	public void prosesInsertMedQuest(Cmdeditbac excelList, String no_temp) {		
		bacDao.insertMedQuestTemp(excelList.getMedQuest(),excelList.getMedQuest_tambah(),excelList.getMedQuest_tambah2(),excelList.getMedQuest_tambah3(),excelList.getMedQuest_tambah4(),excelList.getMedQuest_tambah5(),no_temp);	
	}

	public void prosesSaveRider(Cmdeditbac excelList, String no_temp) {
		List dtrd = excelList.getDatausulan().getDaftaRider();	
		Datarider rd= new Datarider();		
		for(int i=0; i<dtrd.size(); i++){			
			rd= (Datarider)dtrd.get(i);		
			bacDao.insertProductTempRider(rd,no_temp);		
		}
	}

	public void prosesSaveBenef(Cmdeditbac excelList, String no_temp) {
		List benefeciary = excelList.getDatausulan().getDaftabenef();		
		Benefeciary bf= new Benefeciary();		
		for(int i=0; i<benefeciary.size(); i++){			
			bf= (Benefeciary)benefeciary.get(i);		
			bacDao.insertBenefeciaryTemp(bf,no_temp);		
		}
	}
	
	public void prosesInsertPeserta(Cmdeditbac excelList, String no_temp) {
		List peserta=excelList.getDatausulan().getDaftapeserta();
		for(int i=0; i<peserta.size(); i++){
			PesertaPlus_mix psrt= (PesertaPlus_mix)peserta.get(i);
			bacDao.insertPesertaTemp(psrt,no_temp);
		}
	}

	public void prosesInsertProductTemp(Cmdeditbac excelList, String no_temp) {
		bacDao.insertProductTemp(excelList,no_temp);
	}

	public void prosesInsertAddressBilling(Cmdeditbac excelList, String no_temp) {
		bacDao.insertAddressBilling(excelList,no_temp);
	}

	public void prosesInsertSpajTemp(Cmdeditbac spaj, String no_temp, String reff_bank, String agent_bank) {
		bacDao.insertMstSpajTemp(spaj,no_temp,reff_bank,agent_bank);
	}
	
	public List<Map> deleteDatatemp(String notemp) {
		List<Map> pesanList = new ArrayList<Map>();
		Map<String,String> map = new HashMap<String, String>();
		
		bacDao.deleteMstAddressNewTempPemegang(notemp);
		bacDao.deleteMstAddressNewTempTtg(notemp);
		bacDao.deleteMstAddressNewTempPembayarPremi(notemp);
		
		bacDao.deleteMstClientNewTempPemegang(notemp);
		bacDao.deleteMstClientNewTempTtg(notemp);
		bacDao.deleteMstClientNewTempPembayarPremi(notemp);
		
		bacDao.deleteMstRekcLientTemp(notemp);
		bacDao.deleteMstAdditionalTemp(notemp);
		
		bacDao.deleteMstBiayaUlinkTemp(notemp);
		bacDao.deleteMstDetUlinkTemp(notemp);
		bacDao.deleteMstUlinkTemp(notemp);
		
		bacDao.deleteMstBenefTemp(notemp);
		bacDao.deleteMstAccounRecurTemp(notemp);
		bacDao.deleteMstQuestionAnswerTemp(notemp);
		bacDao.deleteMstReffBiiTemp(notemp);
		
		bacDao.deleteMstaddrBillTemp(notemp);
		bacDao.deleteMstPesertaTemp(notemp);
		bacDao.deleteMstProductTemp(notemp);			
		bacDao.deleteMstSpajTemp(notemp);
		return pesanList;
	}
	
	private void prosesSaveUlink(Cmdeditbac excelList, InvestasiUtama investasiUtama, String no_temp) {
		f_hit_umur umr= new f_hit_umur();
		//Variables 		
		Date v_strBeginDate = excelList.getDatausulan().getMspr_beg_date();		
		Double v_curBasePremium = excelList.getDatausulan().getMspr_premium();
		Integer v_topup_tunggal = excelList.getInvestasiutama().getDaftartopup().getPil_tunggal();
		Double v_jmlhtopup_tunggal = excelList.getInvestasiutama().getDaftartopup().getPremi_tunggal();
		Integer v_topup_berkala = excelList.getInvestasiutama().getDaftartopup().getPil_berkala();
		Double v_jmlhtopup_berkala = excelList.getInvestasiutama().getDaftartopup().getPremi_berkala();
		
		Integer lt_id_tunggal = 2;
		Integer lt_id_berkala = 5;

		ArrayList invvl = investasiUtama.getDaftarinvestasi();
		Date tanggal_surat = umr.f_add_months(v_strBeginDate.getYear()+1900,v_strBeginDate.getMonth()+1, v_strBeginDate.getDate(),6);
		double[] biayaUlink = {0, 0, 0, 0};
		
		for(int i=0; i<excelList.getInvestasiutama().getDaftarbiaya().size(); i++) {
			Biayainvestasi bi = (Biayainvestasi) excelList.getInvestasiutama().getDaftarbiaya().get(i);
			biayaUlink[bi.getMu_ke()] += bi.getMbu_jumlah();
		}
		int mu_ke = 1;
		//Save MST_ULINK untuk Premi Pokok
		proc_save_mst_ulink_temp(excelList,no_temp, excelList.getPemegang().getLus_id() ,tanggal_surat,1,1,1,v_curBasePremium,v_jmlhtopup_berkala,v_jmlhtopup_tunggal , v_topup_berkala,v_topup_tunggal,v_strBeginDate,"");
		//Save MST_DET_ULINK untuk Premi Pokok
		for(int i=0; i<invvl.size(); i++) {
			DetilInvestasi di = (DetilInvestasi) invvl.get(i);
			if(di.getMdu_persen1() != null) {
				if(di.getMdu_persen1() > 0) {
					//int persen_tu = (v_topup_berkala.intValue() > 0 || v_topup_tunggal.intValue() > 0) ? di.getMdu_persen1() : 0; 
					int persen_tu = 0; 
					Double jumlahUnit=di.getMdu_persen1() * (v_curBasePremium - biayaUlink[mu_ke]) /100;
					if(jumlahUnit<0)jumlahUnit=0.;
					proc_save_det_ulink_temp(excelList, no_temp, excelList.getPemegang().getLus_id(), di.getMdu_persen1(), persen_tu, jumlahUnit, mu_ke, di.getLji_id1(), v_strBeginDate);
				}
			}
		}
		if (v_topup_berkala.intValue() > 0 ){
			mu_ke++;
			//Save MST_ULINK untuk Top-Up Berkala
			proc_save_mst_ulink_temp(excelList,no_temp, excelList.getPemegang().getLus_id() ,null,mu_ke,lt_id_tunggal,lt_id_berkala ,v_jmlhtopup_berkala  , 0., 0.,0 , 0,v_strBeginDate,"berkala");	
			//Save MST_DET_ULINK untuk Top-Up Berkala
			for(int i=0; i<invvl.size(); i++) {
				DetilInvestasi di = (DetilInvestasi) invvl.get(i);
				if(di.getMdu_persen1() != null) {
					if(di.getMdu_persen1() > 0) {
						//int persen_tu = (v_topup_berkala.intValue() > 0 || v_topup_tunggal.intValue() > 0) ? di.getMdu_persen1() : 0; 
						int persen_tu = 0; 
						Double jumlahUnitBerkala=di.getMdu_persen1() * (v_jmlhtopup_berkala - biayaUlink[mu_ke]) /100;
						if(jumlahUnitBerkala<0)jumlahUnitBerkala=0.;
						proc_save_det_ulink_temp(excelList, no_temp, excelList.getPemegang().getLus_id(), di.getMdu_persen1(), di.getMdu_persen1(), jumlahUnitBerkala, mu_ke, di.getLji_id1(), v_strBeginDate);
					}
				}
			}
		}
		if (v_topup_tunggal.intValue() > 0 ){
			mu_ke++;
			//Save MST_ULINK untuk Top-Up Tunggal
			proc_save_mst_ulink_temp(excelList,no_temp,excelList.getPemegang().getLus_id() ,null,mu_ke,lt_id_tunggal,lt_id_berkala ,v_jmlhtopup_tunggal , 0., 0. , 0, 0, v_strBeginDate, "tunggal");	
			//Save MST_DET_ULINK untuk Top-Up Tunggal
			for(int i=0; i<invvl.size(); i++) {
				DetilInvestasi di = (DetilInvestasi) invvl.get(i);
				if(di.getMdu_persen1() != null) {
					if(di.getMdu_persen1() > 0) {
						//int persen_tu = (v_topup_berkala.intValue() > 0 || v_topup_tunggal.intValue() > 0) ? di.getMdu_persen1() : 0; 
						int persen_tu = 0; 
						Double jumlahUnitTunggal =di.getMdu_persen1() * (v_jmlhtopup_tunggal - biayaUlink[mu_ke]) /100;
						if(jumlahUnitTunggal<0)jumlahUnitTunggal=0.;
						proc_save_det_ulink_temp(excelList, no_temp, excelList.getPemegang().getLus_id(), di.getMdu_persen1(), di.getMdu_persen1(), jumlahUnitTunggal, mu_ke, di.getLji_id1(), v_strBeginDate);
					}
				}
			}
		}
		
		for(int i=0; i<excelList.getInvestasiutama().getDaftarbiaya().size(); i++) {
			Biayainvestasi bi = (Biayainvestasi) excelList.getInvestasiutama().getDaftarbiaya().get(i);
			prosesInsertBiayaulink(no_temp,bi,excelList);
		}
	}
	
	private void prosesInsertBiayaulink(String no_temp, Biayainvestasi bi, Cmdeditbac excelList) {
		HashMap<String, Comparable> m= new HashMap();
		m.put("no_temp", no_temp);
		m.put("mu_ke", bi.getMu_ke());
		m.put("ljb_id", bi.getLjb_id());
		m.put("mbu_jumlah", bi.getMbu_jumlah());
		m.put("mbu_persen", bi.getMbu_persen());
		int ljb_id = bi.getLjb_id();
		m.put("ldt_endpay", 
				ljb_id == 2 ? excelList.getDatausulan().getMspr_end_pay() : 
					ljb_id == 3 ? excelList.getDatausulan().getMspr_end_pay() : 
						ljb_id == 12 ? excelList.getDatausulan().getMspr_end_pay() : null);	
		
		bacDao.insertMstBiayaUlinkTemp(m);						
	}
	
	private void proc_save_det_ulink_temp(Cmdeditbac excelList, String no_temp,
			Integer lus_id, Integer value, int persen_tu, double jumlah,
			int mu_ke, String lji_id1, Date v_strBeginDate) {
		
		HashMap<String, Comparable> param= new HashMap();
		param.put("no_temp",no_temp);
		param.put("v_fixedvalue",value);
		param.put("jmlhfixed",jumlah);
		param.put("mu_ke",mu_ke);
		param.put("v_persen_tu",persen_tu);
		param.put("v_last_trans",v_strBeginDate);
		param.put("nilai",lji_id1);
		bacDao.insertDetUlinkTemp(param);	
	}
	
	private void proc_save_mst_ulink_temp(Cmdeditbac excelList, String no_temp,
			Integer lus_id, Date tanggal_surat, Integer mu_ke, Integer lt_id_tunggal, Integer lt_id_berkala,
			Double v_curBasePremium, Double v_jmlhtopup_berkala,
			Double v_jmlhtopup_tunggal, Integer v_topup_berkala,
			Integer v_topup_tunggal, Date v_strBeginDate, String keterangan) {
		
		Integer lt_id = new Integer(1);
		Integer v_topup = new Integer(0);
		Double v_jmlhtopup =new Double(0);
		Integer bulan = new Integer(6);
		Integer premi_ke = mu_ke;
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
		if (keterangan.equalsIgnoreCase("Additional Unit"))
		{
			lt_id  = lt_id_tunggal;
			v_topup = v_topup_berkala;
			v_jmlhtopup = v_jmlhtopup_berkala;
			premi_ke=0;
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
		
		excelList.getInvestasiutama().setMu_ke(mu_ke);
		excelList.getInvestasiutama().setMu_jlh_premi(v_curBasePremium);
		excelList.getInvestasiutama().setMu_tgl_surat(tanggal_surat);
		excelList.getInvestasiutama().setMu_bulan_surat(bulan);
		excelList.getInvestasiutama().setLt_id(lt_id);
		excelList.getInvestasiutama().setMu_periodic_tu(v_topup);
		excelList.getInvestasiutama().setMu_jlh_tu(v_jmlhtopup);
		excelList.getInvestasiutama().setMu_tgl_trans(v_strBeginDate);
		excelList.getInvestasiutama().setMu_premi_ke(premi_ke);
		bacDao.insertMst_ulink_temp(no_temp,excelList.getInvestasiutama());
	}
	
	private void prosesSaveRekeningClient(Cmdeditbac excelList, String no_temp) {
		bacDao.insertRekening_client_temp(excelList, no_temp);
	}

	private List<Map> insertSpajtempNew(Cmdeditbac excelList, String reff_bank, String agent_bank, ReffBii reffBii, ArrayList<Map> data_LQAPP) {
		String no_temp = null;
		List<Map> pesanList = null;
		
		//proses insert
		//update no_temp
		Calendar tgl_sekarang = Calendar.getInstance(); 
		Integer inttgl2 = new Integer(tgl_sekarang.get(Calendar.YEAR));
		Long intIDCounter = (Long) this.bacDao.select_counter(158, "01");
	    int intID = intIDCounter.intValue();
		if (intIDCounter.longValue() == 0)
		{
			intIDCounter = new Long((long)((tgl_sekarang.get(Calendar.YEAR))* 1000000));
		}else{
			Integer inttgl1 = new Integer(uwDao.selectGetCounterMonthYear(113, "01"));

			if (inttgl1.intValue() != inttgl2.intValue()){					
				intIDCounter = new Long(Long.parseLong(inttgl2.toString().concat("000000")));
				
				//ganti dengan tahun skarang
				uwDao.updateCounterMonthYear(inttgl2.toString(), 113, "01");
				
				//reset nilai counter dengan 0							 
				intID = 0;
				uwDao.updateMstCounter2("0", 113, "01");
				logger.info("update mst counter start di tahun baru");
			}else{
				intIDCounter = new Long(Long.parseLong(inttgl2.toString().concat("000000"))+intIDCounter);
			}
		}
		
		//--------------------------------------------
		Long intFireId = new Long(intIDCounter.longValue() + 1);
		intID = intID + 1;
		uwDao.updateMstCounter3(intID, 158, "01");
		no_temp = intFireId.toString();
		excelList.setNo_temp(no_temp);
		
		String lsbs_3digit = FormatString.rpad("0", excelList.getDatausulan().getLsbs_id().toString(), 3);
		String lsdbs_3digit = FormatString.rpad("0", excelList.getDatausulan().getLsdbs_number().toString(), 3);
		
		//insert mst_spaj_temp			
		prosesInsertSpajTemp(excelList,no_temp,reff_bank,agent_bank);
		
		//insert mst_address_billing_temp	
		prosesInsertAddressBilling(excelList,no_temp);
		
		//insert mst_produt_temp	
		prosesInsertProductTemp(excelList,no_temp);
		
		//proses pemegang clientNew
		prosesInsertDataNewPemegang(excelList,no_temp);
		
		//proses tertanggung clientNew
		prosesInsertDataNewTertanggung(excelList,no_temp);
		
		//proses pembayarpremi clientNew
		prosesInsertDataNewPembayarPremi(excelList,no_temp);
		
		//insert rider
		if(excelList.getDatausulan().getDaftaRider()!=null){
			prosesSaveRider(excelList,no_temp);
		}
		
		//insert_peserta_temp
		if(excelList.getDatausulan().getDaftapeserta()!=null){
			prosesInsertPeserta(excelList,no_temp);
		}
		
		//insert_acc_recur
		if(excelList.getAccount_recur()!=null && ("120,163,169".indexOf(lsbs_3digit)>-1)){
			prosesInsertAccount_recur(excelList,no_temp);
		}

		/** tambahin sini 208 dan 001,029,030,031,032BTN ya patar timotius  **/
		boolean IsSmartKidBtn = "208".equals(lsbs_3digit) && ("029".equals(lsdbs_3digit) || "030".equals(lsdbs_3digit) || "031".equals(lsdbs_3digit) || "032".equals(lsdbs_3digit));
		/** tambahin sini 208 dan 033,034,035,036BSIM ya patar timotius  **/
		boolean IsSimasKidBSIM = "208".equals(lsbs_3digit) && ("033".equals(lsdbs_3digit) || "034".equals(lsdbs_3digit) || "035".equals(lsdbs_3digit) || "036".equals(lsdbs_3digit));
		if( ("120,142,163,169,177".indexOf(lsbs_3digit)>-1) ||
			("212".equals(lsbs_3digit) && ("004,005,007,012,001,013".indexOf(lsdbs_3digit)>-1)) || /** Nana Tambah 012,013 dan 001 case tidak menyimpan beneficiary */		
			(IsSmartKidBtn) || (IsSimasKidBSIM) ||
			excelList.getPemegang().getCampaign_id()==19){
			prosesSaveBenef(excelList, no_temp);
		}
		
		if("120".indexOf(lsbs_3digit)>-1){
			prosesSaveUlink(excelList,excelList.getInvestasiutama(),no_temp);
		}
		
		//question_answer_temp
		prosesInsertQuestionAnswerTemp(data_LQAPP,no_temp);
		
		//helpdesk [148055] produk DMTM Dana Sejaterah 163 26-30 & Smile Sarjana 173 7-9, untuk UP > 200jt SIO ada tambah questionare
		//helpdesk [150296] DMTM BSIM 163 21-25 tambah simple questionare SIO+
		if(("163".equals(lsbs_3digit) && ("021,022,023,024,025".indexOf(lsdbs_3digit) > -1)) ||
		   ("173".equals(lsbs_3digit) && ("007,008,009".indexOf(lsdbs_3digit) > -1))){
			excelList.setMedQuest(new MedQuest());			
			excelList.setMedQuest_tambah2(new MedQuest_tambah2());			
			excelList.setMedQuest_tambah3(new MedQuest_tambah3());			
			excelList.setMedQuest_tambah4(new MedQuest_tambah4());			
			excelList.setMedQuest_tambah5(new MedQuest_tambah5());
			
			if(excelList.getMedQuest_tambah() == null)
				excelList.setMedQuest_tambah(new MedQuest_tambah());
			
			bacDao.insertMedQuestTemp(excelList.getMedQuest(), excelList.getMedQuest_tambah(), excelList.getMedQuest_tambah2(), excelList.getMedQuest_tambah3(), excelList.getMedQuest_tambah4(), excelList.getMedQuest_tambah5(), no_temp);
		}
	
		if(reffBii.getLrb_id()!=null) prosesInsertReffBii(reffBii,no_temp);
		prosesSaveRekeningClient(excelList,no_temp);
				
		prosesSetKycTemp(excelList,no_temp);
		
		return pesanList;
	}
	
	private void prosesSetKycTemp(Cmdeditbac excelList, String no_temp) {
		int counter=0;
		String []pendapatanRutinBulanpayer = excelList.getPemegang().getPendapatanBulan();
		String []tujuanInvestasipayer = excelList.getPemegang().getTujuanInvestasi();
		
		if( !StringUtil.isEmpty(excelList.getPembayarPremi().getNama_pihak_ketiga())){
			for(int i=0;i<pendapatanRutinBulanpayer.length;i++){
				if(!pendapatanRutinBulanpayer[i].contains("-")){
					bacDao.insertKycTemp(no_temp, counter++, "3", "0", pendapatanRutinBulanpayer[i]);
				}
			}
			
			for(int i=0;i<tujuanInvestasipayer.length;i++){
				if(!tujuanInvestasipayer[i].contains("-")){
					bacDao.insertKycTemp(no_temp, counter++, "5", "0", tujuanInvestasipayer[i]);
				}
			}
			
			bacDao.insertKycTemp(no_temp, counter++, "7", "0", excelList.getPembayarPremi().getTotal_rutin());
			bacDao.insertKycTemp(no_temp, counter++, "8", "0", excelList.getPembayarPremi().getTotal_non_rutin());
		}
		
		//ttg
		String []pendapatanRutinBulantt = excelList.getTertanggung().getPendapatanBulan();
		String []tujuanInvestasitt = excelList.getTertanggung().getTujuanInvestasi();
		
		for(int i=0;i<pendapatanRutinBulantt.length;i++){
			if(!pendapatanRutinBulantt[i].contains("-")){
				bacDao.insertKycTemp(no_temp, counter++, "3", "1", pendapatanRutinBulantt[i]);
			}
		}
		
		for(int i=0;i<tujuanInvestasitt.length;i++){
			if(!tujuanInvestasitt[i].contains("-")){
				bacDao.insertKycTemp(no_temp, counter++, "5", "1", tujuanInvestasitt[i]);
			}
		}
		
		//pemegang
		String []pendapatanRutinBulanpp = excelList.getPemegang().getPendapatanBulan();
		String []tujuanInvestasipp = excelList.getPemegang().getTujuanInvestasi();
		
		for(int i=0;i<pendapatanRutinBulanpp.length;i++){
			if(!pendapatanRutinBulanpp[i].contains("-")){
				bacDao.insertKycTemp(no_temp, counter++, "3", "2", pendapatanRutinBulanpp[i]);
			}
		}
		
		for(int i=0;i<tujuanInvestasipp.length;i++){
			if(!tujuanInvestasipp[i].contains("-")){
				bacDao.insertKycTemp(no_temp, counter++, "5", "2", tujuanInvestasipp[i]);
			}
		}
		if(excelList.getPemegang().getLsre_id_premi()==40){
			bacDao.insertKycTemp(no_temp, counter++, "7", "0", excelList.getPemegang().getMkl_penghasilan());
			bacDao.insertKycTemp(no_temp, counter++, "8", "0", excelList.getPemegang().getMkl_penghasilan());
		}
	}
	
	private void prosesInsertQuestionAnswerTemp(ArrayList <Map>data_LQAPP, String no_temp) {
		for(int i=0;i<data_LQAPP.size();i++){
			MstQuestionAnswer question = new MstQuestionAnswer();
			question =(MstQuestionAnswer)data_LQAPP.get(i);			
			bacDao.insertQuestionAnswerTemp(question,no_temp);
		}
	}
	
	private void prosesInsertDataNewPembayarPremi(Cmdeditbac excelList, String no_temp) {
		if(excelList.getPemegang().getLsre_id_premi()!=40){
		   bacDao.insertClientNewPayerTemp(excelList,no_temp);
		   bacDao.insertAddressNewPayerTemp(excelList,no_temp);
		}		
	}
	
	private void prosesInsertDataNewTertanggung(Cmdeditbac excelList, String no_temp) {
		if(excelList.getPemegang().getLsre_id()!=1){
			bacDao.insertClientNewTtgTemp(excelList,no_temp);
			bacDao.insertAddressNewTtgTemp(excelList,no_temp);
		}
	}
	
	private void prosesInsertDataNewPemegang(Cmdeditbac excelList, String no_temp) {
		bacDao.insertClientNewPemegangTemp(excelList,no_temp);
		bacDao.insertAddressNewPemegangTemp(excelList,no_temp);
	}
}
	