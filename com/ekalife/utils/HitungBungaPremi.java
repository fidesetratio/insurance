package com.ekalife.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.BillingWS;
import com.ekalife.elions.model.NSIAPAYDET;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.beans.Email;

public class HitungBungaPremi {
	protected final Log logger = LogFactory.getLog( getClass() );
	private Properties props;	
	private Email email;
	private ElionsManager elionsManager;
	
	public void setEmail(Email email) {
		this.email = email;
	}
	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}
	public void setProps(Properties props) {
		this.props = props;
	}
	
	public  Double cariSukuBunga(Date tglBayar,String lsbs_id, String lku_id){
		Double bunga=new Double(0.0);
		
		if("079, 080, 091, 092".contains(lsbs_id)){
			bunga=new Double(18.0);
		}else{
			bunga=elionsManager.selectCariSukuBunga(lku_id, tglBayar);
		}
		
		
		return bunga;
	}
	
	/**
	 * Fungsi buat hitung bunga tunggakan premi
	 * Klo mo dipakai ditempat lain intinya cuma return bunga dan lama tunggakan
	 * @author Berto
	 * @since Jun 30, 2008 1:37:14 PM
	 * @param bill
	 * @return
	 */
	public BillingWS hitungBungaPremi(BillingWS bill, Date dateNow) {
		Double ldec_premi=new Double(0.0);
		Double tahapan=new Double(0.0);
		String lsbs_id="";
		Double ldec_persen=new Double(0.0);
		Double ldec_premi_min=new Double(0.0);
		Double li_hari=new Double(0.0);
		Double ldec_bunga=new Double(0.0);
		Double bungaPremi=new Double(0.0);
		
		try{
			
			Date tglBayar=dateNow;
			tahapan=elionsManager.selectCekTahapan(bill.getReg_spaj());
			tahapan=tahapan==null?new Double(0.0): tahapan;
			
//			Map lds_1=(Map) policyManager.selectBillOSBunga(bill.getReg_spaj(),bill.getMsbi_tahun_ke(),bill.getMsbi_premi_ke()).get(0);
//			if(bill.getLku_id().equals("01")){
//				bungaPremi=new Double(props.getProperty("nilai.bungapremirp"));
//			}else{
//				bungaPremi=new Double(props.getProperty("nilai.bungapremielse"));
//			}
			
			
			Integer li_bisnis=bill.getLsbs_id();
			lsbs_id=FormatString.rpad("0", li_bisnis.toString() , 3);
			Integer msbi_tahun_ke=0;//((BigDecimal) lds_1.get("MSBI_TAHUN_KE")).intValue(); 
//			Integer msbi_premi_ke=((BigDecimal) lds_1.get("MSBI_PREMI_KE")).intValue();
//			Double msdb_premium=((BigDecimal) lds_1.get("MSDB_PREMIUM")).doubleValue();
//			Double msdb_discount=((BigDecimal) lds_1.get("MSDB_DISCOUNT")).doubleValue();				
//			Date msbi_beg_date=(Date) lds_1.get("MSBI_BEG_DATE");
//			Date msbi_end_date=(Date) lds_1.get("MSBI_END_DATE");
//			Double msbi_stamp=((BigDecimal) lds_1.get("MSBI_STAMP")).doubleValue();
			
			ldec_premi=bill.getTotal_premi()- bill.getDisc()-tahapan;
			
			bungaPremi=cariSukuBunga(tglBayar, lsbs_id, bill.getLku_id());
			
			if("074, 076, 096, 099, 135, 136".contains(lsbs_id)){
				msbi_tahun_ke=bill.getMsbi_tahun_ke();
				if(msbi_tahun_ke==1){
					ldec_persen=new Double(1.0);
				}else if(msbi_tahun_ke==2){
					ldec_persen=new Double(0.2);
				}else if(msbi_tahun_ke==3){
					ldec_persen=new Double(0.1);
				}else if(msbi_tahun_ke==4){
					ldec_persen=new Double(0.05);
				}else {
					ldec_persen=new Double(0.0);
				}
				ldec_premi_min=ldec_premi*ldec_persen;
				ldec_premi=ldec_premi_min;
			}
			
			li_hari= new Double(FormatDate.dateDifference(bill.getMsbi_beg_date(), tglBayar, false)+1);
			
			if(li_hari<0)li_hari=new Double(0.0);
			
			if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("13082003"))<=0){
				ldec_bunga=(li_hari/365)*ldec_premi*bungaPremi/100;					
			}else if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("13082003"))>0 && tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31102003"))<=0){
				ldec_bunga=li_hari*bungaPremi/100*ldec_premi;					
			}else if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31102003"))>0 && tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31082005"))<=0){
				if(bill.getLku_id().equals("02")){
					bungaPremi=new Double(0.085);
					if(li_hari<=60){
						ldec_bunga = li_hari * bungaPremi / 100 * ldec_premi;
					}else{
						ldec_bunga = ( 60 * bungaPremi / 100 * ldec_premi ) + ((li_hari - 60) * 0.05 / 100 * ldec_premi );//IM No. 083/IM-DIR/X/03						
					}
				}else{
					bungaPremi=new Double(0.1);
					ldec_bunga = li_hari * bungaPremi / 100 * ldec_premi ;
				}
									
			}else{
				ldec_bunga = (li_hari/360) * (bungaPremi/100) * ldec_premi;
			}
			
			if(tglBayar.compareTo(bill.getMsbi_due_date())>0){
				Integer flag_ekalink=elionsManager.selectIsEkaLink(bill.getReg_spaj());
				Integer flag_unitLink=elionsManager.selectIsUlink(bill.getReg_spaj());
				
				if(flag_ekalink==0 || flag_unitLink==0){
					bill.setHari_denda(li_hari.intValue());
					bill.setBesar_denda(ldec_bunga);
				}else{
					bill.setHari_denda(0);
					bill.setBesar_denda(0.0);
				}
			}else{
				bill.setHari_denda(0);
				bill.setBesar_denda(0.0);
			}
			
			bill.setTahapan(tahapan);
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("ERROR :", e);
		}
		return bill;
	}
	
	public NSIAPAYDET hitungBungaPremi(NSIAPAYDET bill, Date dateNow) {
		Double ldec_premi=new Double(0.0);
		Double tahapan=new Double(0.0);
		String lsbs_id="";
		Double ldec_persen=new Double(0.0);
		Double ldec_premi_min=new Double(0.0);
		Double li_hari=new Double(0.0);
		Double ldec_bunga=new Double(0.0);
		Double bungaPremi=new Double(0.0);
		
		try{
			
			Date tglBayar=dateNow;
			tahapan=elionsManager.selectCekTahapan(bill.getReg_spaj());
			tahapan=tahapan==null?new Double(0.0): tahapan;
			
//			Map lds_1=(Map) policyManager.selectBillOSBunga(bill.getReg_spaj(),bill.getMsbi_tahun_ke(),bill.getMsbi_premi_ke()).get(0);
//			if(bill.getLku_id().equals("01")){
//				bungaPremi=new Double(props.getProperty("nilai.bungapremirp"));
//			}else{
//				bungaPremi=new Double(props.getProperty("nilai.bungapremielse"));
//			}
			
			
			Integer li_bisnis=bill.getLsbs_id();
			lsbs_id=FormatString.rpad("0", li_bisnis.toString() , 3);
			Integer msbi_tahun_ke=0;//((BigDecimal) lds_1.get("MSBI_TAHUN_KE")).intValue(); 
//			Integer msbi_premi_ke=((BigDecimal) lds_1.get("MSBI_PREMI_KE")).intValue();
//			Double msdb_premium=((BigDecimal) lds_1.get("MSDB_PREMIUM")).doubleValue();
//			Double msdb_discount=((BigDecimal) lds_1.get("MSDB_DISCOUNT")).doubleValue();				
//			Date msbi_beg_date=(Date) lds_1.get("MSBI_BEG_DATE");
//			Date msbi_end_date=(Date) lds_1.get("MSBI_END_DATE");
//			Double msbi_stamp=((BigDecimal) lds_1.get("MSBI_STAMP")).doubleValue();
			
			ldec_premi=bill.getTotal_premi()- bill.getDisc()-tahapan;
			
			bungaPremi=cariSukuBunga(tglBayar, lsbs_id, bill.getLku_id());
			
			if("074, 076, 096, 099, 135, 136".contains(lsbs_id)){
				msbi_tahun_ke=bill.getMsbi_tahun_ke();
				if(msbi_tahun_ke==1){
					ldec_persen=new Double(1.0);
				}else if(msbi_tahun_ke==2){
					ldec_persen=new Double(0.2);
				}else if(msbi_tahun_ke==3){
					ldec_persen=new Double(0.1);
				}else if(msbi_tahun_ke==4){
					ldec_persen=new Double(0.05);
				}else {
					ldec_persen=new Double(0.0);
				}
				ldec_premi_min=ldec_premi*ldec_persen;
				ldec_premi=ldec_premi_min;
			}
			
			li_hari= new Double(FormatDate.dateDifference(bill.getMsbi_beg_date(), tglBayar, false)+1);
			
			if(li_hari<0)li_hari=new Double(0.0);
			
			if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("13082003"))<=0){
				ldec_bunga=(li_hari/365)*ldec_premi*bungaPremi/100;					
			}else if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("13082003"))>0 && tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31102003"))<=0){
				ldec_bunga=li_hari*bungaPremi/100*ldec_premi;					
			}else if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31102003"))>0 && tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31082005"))<=0){
				if(bill.getLku_id().equals("02")){
					bungaPremi=new Double(0.085);
					if(li_hari<=60){
						ldec_bunga = li_hari * bungaPremi / 100 * ldec_premi;
					}else{
						ldec_bunga = ( 60 * bungaPremi / 100 * ldec_premi ) + ((li_hari - 60) * 0.05 / 100 * ldec_premi );//IM No. 083/IM-DIR/X/03						
					}
				}else{
					bungaPremi=new Double(0.1);
					ldec_bunga = li_hari * bungaPremi / 100 * ldec_premi ;
				}
									
			}else{
				ldec_bunga = (li_hari/360) * (bungaPremi/100) * ldec_premi;
			}
			
			if(tglBayar.compareTo(bill.getMsbi_due_date())>0){
				Integer flag_ekalink=elionsManager.selectIsEkaLink(bill.getReg_spaj());
				Integer flag_unitLink=elionsManager.selectIsUlink(bill.getReg_spaj());
				
				if(flag_ekalink==0 || flag_unitLink==0){
					bill.setHari_denda(li_hari.intValue());
					bill.setBesar_denda(ldec_bunga);
				}else{
					bill.setHari_denda(0);
					bill.setBesar_denda(0.0);
				}
			}else{
				bill.setHari_denda(0);
				bill.setBesar_denda(0.0);
			}
			
			bill.setTahapan(tahapan);
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("ERROR :", e);
		}
		return bill;
	}
	
	public BillingWS w_hitungBungaPremi(BillingWS bill) {
		Date dateNow=elionsManager.selectSysdate1("dd", false, 0);
		Double bungaPremi=new Double(0.0);
		Integer lamaLapse=0;
		Date tglBayar=dateNow;
		Double ldec_persen=new Double(0.0);
		Double ldec_premi_min=new Double(0.0);
		Double ldec_premi=new Double(0.0);
		Double ldec_tahap=new Double(0.0);
		Double ldec_bunga=new Double(0.0);
		List dw1=new ArrayList();
		String lsbs_id="";
		Integer msbi_tahun_ke=0;
		Double msbi_stamp=new Double(0.0);
		
		try{
			if(bill.getLku_id().equals("01")){
				bungaPremi=new Double(props.getProperty("nilai.bungapremirp"));
			}else{
				bungaPremi=new Double(props.getProperty("nilai.bungapremielse"));
			}
			
			List lds_1=elionsManager.selectBillOSBunga(bill.getReg_spaj(),bill.getMsbi_tahun_ke(),bill.getMsbi_premi_ke());
			
			if(lds_1.size()>0){
				for (int i = 0; i < lds_1.size(); i++) {
					Map lds_1Map=(Map) lds_1.get(i);
					Map dwMap=new HashMap();
					Integer li_bisnis=(Integer) lds_1Map.get("LSBS_ID");
					lsbs_id=FormatString.rpad("0", li_bisnis.toString() , 3);
					msbi_tahun_ke=(Integer) lds_1Map.get("MSBI_TAHUN_KE"); 
					Integer msbi_premi_ke=(Integer) lds_1Map.get("MSBI_PREMI_KE");
					Double msdb_premium=(Double) lds_1Map.get("MSDB_PREMIUM");
					Double msdb_discount=(Double) lds_1Map.get("MSDB_DISCOUNT");				
					Date msbi_beg_date=(Date) lds_1Map.get("MSBI_BEG_DATE");
					Date msbi_end_date=(Date) lds_1Map.get("MSBI_END_DATE");
					msbi_stamp=(Double) lds_1Map.get("MSBI_STAMP");		
					
					if("074, 076, 096, 099, 135, 136".contains(lsbs_id)){
						if(msbi_tahun_ke==1){
							ldec_persen=new Double(1.0);
						}else if(msbi_tahun_ke==2){
							ldec_persen=new Double(0.2);
						}else if(msbi_tahun_ke==3){
							ldec_persen=new Double(0.1);
						}else if(msbi_tahun_ke==4){
							ldec_persen=new Double(0.05);
						}else {
							ldec_persen=new Double(0.0);
						}
						ldec_premi_min=msdb_premium*ldec_persen;
						if(msbi_tahun_ke>1)dwMap.put("prm_min", 1);
					}
					dwMap.put("TAHUN_KE", msbi_tahun_ke);
					dwMap.put("PREMI_KE", msbi_premi_ke);
					dwMap.put("BEG_AKTIF",msbi_beg_date);
					dwMap.put("END_AKTIF", msbi_end_date);
					dwMap.put("PREMI", msdb_premium);
					dwMap.put("DISCOUNT",msdb_discount);
					dwMap.put("MATERAI", msbi_stamp);
					dwMap.put("TGL_BAYAR2", tglBayar);
					
					ldec_tahap=elionsManager.selectCekTahapan(bill.getReg_spaj());
					dwMap.put("TAHAPAN", ldec_tahap);
					
					dw1.add(lds_1Map);
				}
				
			}
			
			dw1=wf_ins_bill(dw1, bill, tglBayar);
			
			Integer li_hari=0;
			Date ld_tgl=null;
			for (int i = 0; i < dw1.size(); i++) {
				Map dwMap=(Map) dw1.get(i);
				ld_tgl=(Date) dwMap.get("BEG_AKTIF");
				ldec_premi=(Double) dwMap.get("PREMI")- (Double)dwMap.get("DISCOUNT")-(Double)dwMap.get("TAHAPAN");
				
				if("074, 076, 096, 099, 135, 136".contains(lsbs_id)){
					msbi_tahun_ke=(Integer) dwMap.get("TAHUN_KE");
					if(msbi_tahun_ke==1){
						ldec_persen=new Double(1.0);
					}else if(msbi_tahun_ke==2){
						ldec_persen=new Double(0.2);
					}else if(msbi_tahun_ke==3){
						ldec_persen=new Double(0.1);
					}else if(msbi_tahun_ke==4){
						ldec_persen=new Double(0.05);
					}else {
						ldec_persen=new Double(0.0);
					}
					ldec_premi_min=ldec_premi*ldec_persen;
					ldec_premi=ldec_premi_min;
				}
				
				li_hari= (int)FormatDate.dateDifference(ld_tgl, tglBayar, false)+1;
				
				if(li_hari<0)li_hari=0;
				
				if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("13082003"))<=0){
					ldec_bunga=(li_hari/365)*ldec_premi*bungaPremi/100;					
				}else if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("13082003"))>0 && tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31102003"))<=0){
					ldec_bunga=li_hari*bungaPremi/100*ldec_premi;					
				}else if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31102003"))>0 && tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31082005"))<=0){
					if(bill.getLku_id().equals("02")){
						bungaPremi=new Double(0.085);
						if(li_hari<=60){
							ldec_bunga = li_hari * bungaPremi / 100 * ldec_premi;
						}else{
							ldec_bunga = ( 60 * bungaPremi / 100 * ldec_premi ) + ((li_hari - 60) * 0.05 / 100 * ldec_premi );//IM No. 083/IM-DIR/X/03						
						}
					}else{
						bungaPremi=new Double(0.1);
						ldec_bunga = li_hari * bungaPremi / 100 * ldec_premi ;
					}
										
				}else{
					ldec_bunga = (li_hari/360) * (bungaPremi/100) * ldec_premi;
				}
				
				dwMap.put("jlh_hari", li_hari);
				dwMap.put("bunga", ldec_bunga);
				dwMap.put("materai",msbi_stamp );
				
				if(i==0)lamaLapse=li_hari;
			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("ERROR :", e);
		}
		
		return bill;
	}
	
	public List wf_ins_bill(List dw1, BillingWS bill, Date tglBayar){
		Map dataBilling=elionsManager.selectDataBilling(bill.getReg_spaj());
		Integer lscb_id=(Integer) dataBilling.get("LSCB_ID"); 
		Integer mspo_pay_period=(Integer) dataBilling.get("MSPO_PAY_PERIOD");
		Date mspo_next_bill =(Date) dataBilling.get("MSPO_NEXT_BILL");
		Date mste_beg_date =(Date) dataBilling.get("MSTE_BEG_DATE");
		Integer lscb_ttl_month=(Integer) dataBilling.get("LSCB_TTL_MONTH");
		Date end_date=null;
		Integer li_thn_ke=0;
		Integer li_premi_ke=0;
		Double ldec_premi = new Double(0.0);
		Double ldec_discount = new Double(0.0);
		Double ldec_hamil = new Double(0.0);
		Double ldec_pct = new Double(0.0);
		Double ldec_tahapan = new Double(0.0);
		Double ldec_stamp = new Double(0.0);
		Double ldec_disc = new Double(0.0);
		Integer li_bisnis_id=0;
		Integer li_bisnis_no=0;
		
		if(tglBayar.compareTo(mspo_next_bill)>=0){
			List lds_pi=elionsManager.selectProductInsured1(bill.getReg_spaj(), 1);
			
			do {
				Map dwMap=new HashMap();
				if(!FormatDate.getDay(mspo_next_bill).equals(FormatDate.getDay(mste_beg_date))){
					mspo_next_bill=F_check_end_bill.check_end_bill(mspo_next_bill, mste_beg_date, mspo_pay_period*12);
				}
				end_date=FormatDate.add(FormatDate.add(mspo_next_bill, Calendar.MONTH, lscb_ttl_month), Calendar.DAY_OF_MONTH, -1);
				f_check_end_aktif.end_aktif(end_date, mste_beg_date);
				li_thn_ke=F_hit_tahun_ke.hitTahunKe(mspo_next_bill, mste_beg_date, mspo_pay_period);
				li_premi_ke=F_hit_premi_ke.hitPremiKe(mspo_next_bill, mste_beg_date, lscb_ttl_month);
				
				for (int i = 0; i < lds_pi.size(); i++) {
					Map lds_idMap=(Map) lds_pi.get(i);
					li_bisnis_id=(Integer) lds_idMap.get("LSBS_ID");
					li_bisnis_no=(Integer) lds_idMap.get("LSDBS_NUMBER");

					if(li_thn_ke>1 && li_bisnis_id==904 && li_bisnis_no==1){
						ldec_hamil=(Double) lds_idMap.get("MSPR_PREMIUM");
					}
					
//					Discount rider = discount premi basic
					if(li_bisnis_id<300){
						ldec_pct=elionsManager.selectDiskPlan(li_bisnis_id, li_bisnis_no, li_thn_ke);
						ldec_pct=ldec_pct==null?new Double(0.0):ldec_pct;
					}
					
//					Discount rider hanya untuk WPD & PC (kata si Ayti) 19/12/2000
					if(li_bisnis_id==804 || li_bisnis_id==802 || li_bisnis_id<300){
						ldec_disc=(Double) lds_idMap.get("MSPR_PREMIUM")*ldec_pct/100;
					}
					
					ldec_premi+=(Double) lds_idMap.get("MSPR_PREMIUM");
					ldec_discount+=ldec_disc;
				}
				ldec_premi-=ldec_hamil;
				
				dwMap.put("TAHUN_KE", li_thn_ke);
				dwMap.put("PREMI_KE", li_premi_ke);
				dwMap.put("BEG_AKTIF",mspo_next_bill);
				dwMap.put("END_AKTIF", end_date);
				dwMap.put("PREMI", ldec_premi);
				dwMap.put("DISCOUNT",ldec_discount);
				dwMap.put("TAHAPAN", ldec_tahapan);
				dwMap.put("TGL_BAYAR2",tglBayar );
				
				mspo_next_bill=F_check_end_bill.check_end_bill(end_date, mste_beg_date, mspo_pay_period*12);
			} while (mspo_next_bill.compareTo(tglBayar)>0 || mspo_next_bill==null);
				
		
		}
		
		return dw1;
	}
}
