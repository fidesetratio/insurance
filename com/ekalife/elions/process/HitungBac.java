package com.ekalife.elions.process;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletException;

import org.springframework.mail.MailException;
import org.springframework.validation.Errors;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Biayainvestasi;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.DetilInvestasi;
import com.ekalife.elions.model.Hcp;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.PesertaPlus_mix;
import com.ekalife.elions.model.Simas;
import com.ekalife.elions.web.bac.support.form_investasi;
import com.ekalife.elions.web.bac.support.hit_biaya_powersave;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("unchecked")
public class HitungBac extends ParentDao{
	protected final Log logger = LogFactory.getLog( getClass() );
	private long accessTime;

	public long getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(long accessTime) {
		this.accessTime = accessTime;
	}
	
	public Map proseshitungpowersave(Object cmd,Errors err, String kurs, Double up ,Integer flag_account , String lca_id , Integer autodebet , Integer flag_bao1, Double premi , Integer flag_powersave , Integer flag_bulanan) throws ServletException,IOException
	{
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Map m =null;
		m = new HashMap();
		try {
			m=proc_hitung_powersave(edit,m,flag_powersave ,kurs ,premi ,flag_bulanan,err, flag_account,lca_id,autodebet,flag_bao1);
		} catch (ServletException e) {
			logger.error("ERROR :", e);
		} catch (IOException e) {
			logger.error("ERROR :", e);
		}	
	return m;
	}
	
	public Map proseshitungadditionalunit(Object cmd, Errors err, String kurs, Double premi_pokok)throws ServletException,IOException{
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Map m =null;
		m = new HashMap();
		try {
			m=proc_hit_additional_unit(edit, m, kurs, premi_pokok);
		} catch (ServletException e) {
			logger.error("ERROR :", e);
		} catch (IOException e) {
			logger.error("ERROR :", e);
		}	
	return m;
	}
	
	public Map proseshitungrider(Object cmd,Errors err, Integer kode_flag , Integer flag_rider, Integer pmode , String kurs , Double premi , Double up ,Double premi_berkala,Double premi_tunggal ,Integer flag_as, Integer pil_berkala, Integer pil_tunggal) throws ServletException,IOException,Exception
	{
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Map m =null;
		m = new HashMap();
			m = proc_rider(edit,m,kode_flag ,flag_rider,kurs ,premi,pmode,up,pil_berkala, pil_tunggal, flag_as,premi_berkala,premi_tunggal);
		return m;
	}
	

	public Map proseshitunghcp(Object cmd,Errors err, Integer kode_flag , Integer flag_rider, Integer pmode , String kurs , Double premi , Double up ,Double premi_berkala,Double premi_tunggal ,Integer flag_as, Integer pil_berkala, Integer pil_tunggal) throws ServletException,IOException,Exception
	{
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Map m =null;
		m = new HashMap();
			m = proc_rider_hcp(edit,m,kode_flag ,flag_rider,kurs ,premi,pmode,up,pil_berkala, pil_tunggal, flag_as,premi_berkala,premi_tunggal);
		return m;
	}
	
	public Map validhcp(Object cmd,Errors err,String status) throws ServletException,IOException,Exception
	{
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Map m =null;
		m = new HashMap();
			m = proc_validasi_hcp(edit,status, m,err);
		return m;
	}
	
	public Map validsimas(Object cmd,Errors err,String status) throws ServletException,IOException,Exception
	{
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Map m =null;
		m = new HashMap();
			m = proc_validasi_simas(edit,status, m,err);
		return m;
	}
	
	
	public Map proc_rider(Cmdeditbac edit,Map m,Integer kode_flag , Integer flag_rider,String kurs ,Double premi, Integer pmode, Double up, Integer pil_berkala, Integer pil_tunggal, Integer flag_as, Double premi_berkala, Double premi_tunggal)throws ServletException,IOException,Exception
	{
		String reg_spaj = edit.getDatausulan().getReg_spaj();
		Integer kode_produk= edit.getDatausulan().getLsbs_id();
		Integer number_produk=edit.getDatausulan().getLsdbs_number();
		Integer payperiod = edit.getDatausulan().getMspo_pay_period();
		Integer insperiod =  edit.getDatausulan().getMspr_ins_period();
		Integer umurttg =  edit.getTertanggung().getUsiattg();
		Integer cara_bayar = edit.getDatausulan().getLscb_id();
		Integer indeks_biaya=new Integer(0);
		Integer[] mbu_jenis;
		String[] mbu_nama_jenis;
		Double[] mbu_jumlah;
		Double[] mbu_persen;
		Double ldec_awal=new Double(0);
		Double ld_bia_ass=new Double(0);
		Double ld_bia_akui=new Double(0);
		Integer li_insper=new Integer(0);
		String hasil_biaya="";
		Double mbu_jumlah1=new Double(0);
		String mbu_jumlah1_1="";
		Double mbu_persen1=new Double(0);
		Double mbu_jumlah2=new Double(0);
		String mbu_jumlah2_2="";
		Double mbu_persen2=new Double(0);
		Double mbu_jumlah3=new Double(0);
		String mbu_jumlah3_3="";		
		Double mbu_persen3=new Double(0);
		Integer[] klas_1;
		Integer[] unit_1;
		String[] kode_rider;
		String[] number_rider;	
		Integer[] f_Up;
		Double jumlah_minus = new Double(0);
		boolean flag_minus = false;

		Double total_mbu_jumlah=new Double(0);
		Double ld_premi_invest=new Double(0);
		Integer ldec_pct=new Integer(1);
		
		String nama_produk="";
		if (umurttg==null)
		{
			umurttg=new Integer(0);
		}
		Integer umurpp = edit.getPemegang().getUsiapp();
		if (umurpp==null)
		{
			umurpp=new Integer(0);
		}
		Integer jmlh_rider = edit.getDatausulan().getJumlah_seluruh_rider();
		if (jmlh_rider==null)
		{
			jmlh_rider= new Integer(0);
		}

		if (Integer.toString(kode_produk.intValue()).trim().length()==1)
		{
			nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
		}else{
			nama_produk="produk_asuransi.n_prod_"+kode_produk;	
		}

			Class aClass = Class.forName( nama_produk );
			n_prod produk = (n_prod)aClass.newInstance();
			produk.setSqlMap(this.bacDao.getSqlMapClient());
			produk.is_kurs_id=kurs;
			if(produk.ii_bisnis_no==0){
				produk.ii_bisnis_id_utama= edit.getDatausulan().getLsbs_id();
				produk.ii_bisnis_no_utama= edit.getDatausulan().getLsdbs_number();
				
				if((produk.ii_bisnis_id_utama == 220 && produk.ii_bisnis_no_utama == 4) || //Chandra - biaya 5% untuk B Smile Protection
				   (produk.ii_bisnis_id_utama == 224 && produk.ii_bisnis_no_utama == 3) || //Chandra - biaya 5% untuk B Smile Protection
				   (produk.ii_bisnis_id_utama == 134 && produk.ii_bisnis_no_utama == 13)){ //helpdesk [142003] produk baru Smart Platinum Link RPUL BEL (134-13)
					produk.li_pct_biaya = 5; 
				}
			}
			
			/** ambil param dari mst_config **/
			Date begDateParam = bacDao.selectBegDateCoiFromConfig();
			
			/** START project COI, edit.getDatausulan().getMste_beg_date() lebih besar dari **/ 
			if(edit.getDatausulan().getMste_beg_date().after(begDateParam)) {
				Integer sex = edit.getTertanggung().getMspe_sex(); 
				if(sex == null){
					sex = 9; 
				}
				produk.ii_sex_tt_coi = sex;
			}
			/** END project COI **/ 
			
			//Biaya
			Integer idx=new Integer(0);
			if (kode_flag.intValue()<=1 || kode_flag.intValue() == 11 || kode_flag.intValue() == 16 )
			{
				edit.getInvestasiutama().setJmlh_biaya(new Integer(0));
			}

				if (produk.flag_rider==0)
				{
					indeks_biaya=new Integer(4);	
				}else{
					if (flag_rider.intValue()==1)
					{
							indeks_biaya=new Integer(3 + jmlh_rider.intValue() + 4);
					}else{
							indeks_biaya=new Integer(6);
					}
				}
				edit.getInvestasiutama().setJmlh_biaya(indeks_biaya);
				
				mbu_jumlah = new Double[indeks_biaya.intValue()];
				mbu_persen = new Double[indeks_biaya.intValue()];
				mbu_jenis = new Integer[indeks_biaya.intValue()];
				mbu_nama_jenis = new String[indeks_biaya.intValue()];
				
				edit.getDatausulan().setLi_trans_berkala(new Integer(produk.li_trans));
				edit.getDatausulan().setLi_trans_tunggal(new Integer(produk.li_trans));
				if (pil_tunggal.intValue()==2)
				{
					edit.getDatausulan().setLi_trans_tunggal(new Integer(2));
				}else if (pil_berkala.intValue()==3)
					{
						edit.getDatausulan().setLi_trans_berkala(new Integer(6));
					}
				
				// untuk arthalink eka link tidak ada biaya
				if ((kode_produk.intValue() == 162 && number_produk.intValue() > 2) || (kode_produk.intValue() == 165) || (kode_produk.intValue()==121) || (kode_produk.intValue() == 190 && number_produk.intValue() == 2))
				{
						ldec_awal = new Double(0);
				}else{
					ldec_awal=new Double(produk.cek_awal());
				}
				li_insper=new Integer(produk.li_insper);
				// Yusuf : kalao update kondisi2 ini, tolong kabarin ke gw ya, soalnya print polis manggil fungsi disini juga
//				if (edit.getDatausulan().getFlag_worksite_ekalife().intValue() == 0)
//				{
//					Integer byr = payperiod;
//					if(edit.getDatausulan().getLsbs_id().intValue() == 138 || edit.getDatausulan().getLsbs_id().intValue() == 190 || edit.getDatausulan().getLsbs_id().intValue() == 191){
//						byr = 80;
//					}
//					if ((edit.getDatausulan().getLsbs_id().intValue() == 162) ||(edit.getDatausulan().getLsbs_id().intValue() == 140) || (edit.getDatausulan().getLsbs_id().intValue() == 116) || (edit.getDatausulan().getLsbs_id().intValue() == 118) || (edit.getDatausulan().getLsbs_id().intValue() == 159)|| (edit.getDatausulan().getLsbs_id().intValue() == 160) || (edit.getDatausulan().getLsbs_id().intValue() == 153))
//					{
//						//byr= 80;
//						byr = produk.ii_contract_period;
//					}
//					Integer number = number_produk;
//					if (kode_produk.intValue() == 162 )
//					{
//						if (number_produk.intValue() == 3 || number_produk.intValue() == 5 || number_produk.intValue() == 7)
//						{
//							number = new Integer(1);
//						}else if (number_produk.intValue() == 4 || number_produk.intValue() == 6 || number_produk.intValue() == 8)
//						{
//							number = new Integer(2);
//						}
//					}
//					if (cara_bayar.intValue()==0)
//					{
//						byr=new Integer(1);
//					}
//					//ld_bia_akui=new Double(produk.f_get_bia_akui(payperiod.intValue(),1));
//					ld_bia_akui = produk.cek_bia_akui(kode_produk.intValue(),number.intValue(),cara_bayar.intValue(),byr.intValue(),1);
//				}else{
////					if (kode_produk.intValue() == 141)
////					{
////						ld_bia_akui=new Double(produk.f_get_bia_akui_ekalife(payperiod.intValue(),1));
////					}else{
////						ld_bia_akui=new Double(produk.f_get_bia_akui(payperiod.intValue(),1));
////						if(edit.getDatausulan().getMste_flag_el()==1){
////							ld_bia_akui=0.;
////						}
////					}
//					ld_bia_akui=0.;
//				}
				
				ld_bia_akui = suratUnitLink.getBiayaAkuisisi(cara_bayar, edit.getDatausulan().getMste_flag_el().toString(), kode_produk.toString(), number_produk.intValue(), payperiod, 1, produk);
				
				if (ld_bia_akui.doubleValue() == 0 && ((kode_produk.intValue() != 113 && kode_produk.intValue() != 134 && kode_produk.intValue() !=138 && kode_produk.intValue() != 139 && kode_produk.intValue() != 166 && kode_produk.intValue() != 215) && edit.getDatausulan().getMste_flag_el()!=1) )
				{
					hasil_biaya="Biaya Akuisisi untuk Plan ini tidak ada ";
					m.put("total_persen",hasil_biaya);
				}
				if ((kode_produk.intValue() == 162 && number_produk.intValue() > 2)||(kode_produk.intValue() == 165 ) || (kode_produk.intValue() == 190 && number_produk.intValue()== 2) )
				{
					ld_bia_ass = new Double(0);
				}else{
					ld_bia_ass=new Double(produk.cek_rate(kode_produk.intValue(),pmode.intValue(),payperiod.intValue(),li_insper.intValue(),umurttg.intValue(),kurs,insperiod.intValue()));

					if (ld_bia_ass.doubleValue()==0 && produk.flag_platinumlink ==0)
					{
						hasil_biaya="Biaya Asuransi untuk Plan ini tidak ada ";
						m.put("total_persen",hasil_biaya);
						//err.rejectValue("investasiutama.total_persen","",hasil_biaya);
					}
				}
				
					List dtby = new ArrayList();
					Integer jumlah_r =indeks_biaya;
					if (jumlah_r==null)
					{
						jumlah_r=new Integer(0);
					}
					
					//ambil dari list biaya
					Biayainvestasi by1= new Biayainvestasi();
					
					produk.biaya1(ld_bia_akui.doubleValue(),premi.doubleValue());
					mbu_jumlah1=new Double(produk.mbu_jumlah1);
					mbu_persen1=new Double(produk.mbu_persen1);
					BigDecimal bd1 = new BigDecimal(mbu_jumlah1.doubleValue());
					mbu_jumlah1_1=bd1.setScale(3,BigDecimal.ROUND_HALF_UP).toString();
					mbu_jenis[0]=new Integer(1);
					mbu_nama_jenis[0]="BIAYA AKUISISI";
					mbu_jumlah[0]=new Double(Double.parseDouble(mbu_jumlah1_1));
					mbu_persen[0]=mbu_persen1;
					idx=new Integer(1);
					by1.setLjb_biaya(mbu_nama_jenis[0]);
					by1.setLjb_id((mbu_jenis[0]));
					by1.setMbu_jumlah((mbu_jumlah[0]));
					by1.setMbu_persen((mbu_persen[0]));
					by1.setMu_ke(new Integer(1));
					by1.setFlag_jenis("BIAYA AKUISISI");					
					dtby.add(by1);
			
					by1= new Biayainvestasi();
					produk.biaya2(ld_bia_ass.doubleValue(),premi.doubleValue(),ldec_pct.intValue(),up.doubleValue());
					mbu_jumlah2=new Double(produk.mbu_jumlah2);
					mbu_persen2=new Double(produk.mbu_persen2);
					BigDecimal bd2 = new BigDecimal(mbu_jumlah2.doubleValue());
					mbu_jumlah2_2=bd2.setScale(3,BigDecimal.ROUND_HALF_UP).toString();		
					mbu_jenis[1]=new Integer(2);
					mbu_nama_jenis[1]="BIAYA ASURANSI";
					mbu_jumlah[1]=new Double(Double.parseDouble(mbu_jumlah2_2));
					mbu_persen[1]=mbu_persen2;	
					idx=new Integer(2);
					by1.setLjb_biaya(mbu_nama_jenis[1]);
					by1.setLjb_id((mbu_jenis[1]));
					by1.setMbu_jumlah((mbu_jumlah[1]));
					by1.setMbu_persen((mbu_persen[1]));
					by1.setMu_ke(new Integer(1));
					by1.setFlag_jenis("BIAYA ASURANSI");
					if (kode_produk==190 || kode_produk==191 ){	//untuk SMile link 99 biaya ass dan admin dibebaskan pada tahun pertama					
						by1.setMbu_jumlah((0.0));
						mbu_jumlah[1]=0.0;
						by1.setMbu_persen((0.0));
						
					}
					dtby.add(by1);
					
					by1= new Biayainvestasi();
					produk.biaya3(ldec_awal.doubleValue());
					mbu_jumlah3=new Double(produk.mbu_jumlah3);
					mbu_persen3=new Double(produk.mbu_persen3);
					BigDecimal bd3 = new BigDecimal(mbu_jumlah3.doubleValue());
					mbu_jumlah3_3=bd3.setScale(3,BigDecimal.ROUND_HALF_UP).toString();		
					mbu_jenis[2]=new Integer(3);
					mbu_nama_jenis[2]="BIAYA ADMINISTRASI";
					mbu_jumlah[2]=new Double(Double.parseDouble(mbu_jumlah3_3));
					mbu_persen[2]=mbu_persen3;	
					idx=new Integer(3);
					by1.setLjb_biaya(mbu_nama_jenis[2]);
					by1.setLjb_id((mbu_jenis[2]));
					by1.setMbu_jumlah((mbu_jumlah[2]));
					by1.setMbu_persen((mbu_persen[2]));
					by1.setMu_ke(new Integer(1));
					by1.setFlag_jenis("BIAYA ADMINISTRASI");
					if (kode_produk==190 || kode_produk==191 ){						
						by1.setMbu_jumlah((0.0));
						mbu_jumlah[2]=0.0;
						by1.setMbu_persen((0.0));
						
					}
					dtby.add(by1);
					
					if (produk.flag_platinumlink==1 || kode_produk.intValue() == 213 || kode_produk.intValue() == 215 || kode_produk.intValue() == 216)
					{
						by1= new Biayainvestasi();
						mbu_jenis[3]=new Integer(12);
						mbu_nama_jenis[3]="BIAYA POKOK";
						mbu_jumlah[3]=new Double(0);
						mbu_persen[3]=new Double(0);	
						idx=new Integer(4);
						by1.setLjb_biaya(mbu_nama_jenis[3]);
						by1.setLjb_id((mbu_jenis[3]));
						by1.setMbu_jumlah((mbu_jumlah[3]));
						by1.setMbu_persen((mbu_persen[3]));
						by1.setMu_ke(new Integer(1));
						by1.setFlag_jenis("BIAYA POKOK");
						dtby.add(by1);					
					}
					
//					ArrayList daftarBiaya = edit.getInvestasiutama().getDaftarbiaya();
					Double total_extra_premi = 0.;
					Integer count_extra_premi = 0;
					if(edit.getInvestasiutama().getDaftarbiaya()!=null){
						if(edit.getPemegang().getStatus().equalsIgnoreCase("edit")){
							for (int i=0 ;i<edit.getInvestasiutama().getDaftarbiaya().size() ; i++)
							{
								Biayainvestasi bi = (Biayainvestasi) edit.getInvestasiutama().getDaftarbiaya().get(i);
								String jenisExtra = bi.getFlag_jenis().toUpperCase();
								if(jenisExtra.equals("BIAYA EXTRA")){
									total_extra_premi=total_extra_premi+bi.getMbu_jumlah();
									count_extra_premi++;
									dtby.add(bi);
								}							
							}
						}
					}
					
					
					edit.getInvestasiutama().setJmlh_biaya((idx+count_extra_premi));
					ld_premi_invest= new Double(premi.doubleValue()-total_mbu_jumlah.doubleValue());
					total_mbu_jumlah=new Double(mbu_jumlah[0].doubleValue()+mbu_jumlah[1].doubleValue()+mbu_jumlah[2].doubleValue()+total_extra_premi.doubleValue());
					edit.getInvestasiutama().setTotal_biaya_premi((total_mbu_jumlah));
			
				if (produk.flag_rider==0)
				{
					ld_premi_invest = new Double(premi.doubleValue()-total_mbu_jumlah.doubleValue()); 	
				}else{
					
					if ( (produk.flag_platinumlink == 0 && (kode_produk!=190 && kode_produk!=191)) || (kode_produk==134 && number_produk>=5) )//platinum & bridge & Ultimate Syariah tidak dikenakan biaya asuransi di 1 tahun pertama 
					//if (produk.flag_platinumlink == 0 && "190,191".indexOf(kode_produk) <=-1)//platinum & bridge tidak dikenakan biaya asuransi di 1 tahun pertama
					{
						List dtrd= edit.getDatausulan().getDaftaRider();
						
//						 biaya rider
						kode_rider = new String[dtrd.size()];
						number_rider = new String[dtrd.size()];
						klas_1 = new Integer[dtrd.size()];
						unit_1 = new Integer[dtrd.size()];
						f_Up = new Integer[dtrd.size()];
						reg_spaj = edit.getDatausulan().getReg_spaj();
						Double premi_pokok=premi;
						Double d_biayaSmileMedical=0.0;
						for (int i=0 ;i<dtrd.size() ; i++)
						{
							premi=premi_pokok;
							by1= new Biayainvestasi();
							Datarider rd1= (Datarider)dtrd.get(i);
							if (i>0)
							{
								idx=new Integer(idx.intValue()+1);
							}
							kode_rider[i]=Integer.toString(rd1.getLsbs_id());
							
							number_rider[i]=Integer.toString(rd1.getLsdbs_number().intValue());
							// *Hitung umur tertanggung tambahan
							if((Integer.parseInt(kode_rider[i]) == 823 && Integer.parseInt(number_rider[i]) > 15 )||
								(Integer.parseInt(kode_rider[i]) == 848 && Integer.parseInt(number_rider[i]) > 10 )||
								(Integer.parseInt(kode_rider[i]) == 825 && Integer.parseInt(number_rider[i]) > 15 )||
								(Integer.parseInt(kode_rider[i]) == 820 && Integer.parseInt(number_rider[i]) > 15 ) ||
								(Integer.parseInt(kode_rider[i]) == 819 && (Integer.parseInt(number_rider[i]) > 160 && Integer.parseInt(number_rider[i]) < 281)) ||
								(Integer.parseInt(kode_rider[i]) == 819 && (Integer.parseInt(number_rider[i]) > 300 && Integer.parseInt(number_rider[i]) < 381)) ||
								(Integer.parseInt(kode_rider[i]) == 819 && (Integer.parseInt(number_rider[i]) > 450 && Integer.parseInt(number_rider[i]) < 531)) ||
								(Integer.parseInt(kode_rider[i]) == 819 && (Integer.parseInt(number_rider[i]) > 390 && Integer.parseInt(number_rider[i]) < 431)) ||
								(Integer.parseInt(kode_rider[i]) == 826 && Integer.parseInt(number_rider[i]) > 12)||(Integer.parseInt(kode_rider[i]) == 838 && Integer.parseInt(number_rider[i]) > 4)){
								List ptx = edit.getDatausulan().getDaftarplus();
								Integer jumlah_d = new Integer(ptx.size());
								for (int c=0; c<jumlah_d.intValue(); c++)
								{
									PesertaPlus plus = (PesertaPlus)ptx.get(c);
									Integer mur = plus.getUmur();
									Integer lsdbsno = plus.getLsdbs_number();
										if(number_rider[i].compareTo(Integer.toString(lsdbsno.intValue())) == 0){
									umurttg = mur;
									}
								}
							}else{
								umurttg =  edit.getTertanggung().getUsiattg();
							}
							
							if(edit.getDatausulan().getDaftaRiderAddOn()>0 && Integer.parseInt(kode_rider[i]) == 839)	{
							List peserta = edit.getDatausulan().getDaftarplus_mix(); //FIXIN
							Integer daftarMix = new Integer(peserta.size());
							for (int z=0; z<daftarMix.intValue(); z++)
							{
								PesertaPlus_mix ppm = (PesertaPlus_mix)peserta.get(z);
								Integer i_flagJenis = ppm.getFlag_jenis_peserta();
								ArrayList add=edit.getDatausulan().getDaftaRiderAddOnTtg();
								for(int x=0;x<add.size(); x++){
									Datarider riderMed=(Datarider)add.get(x);
									if((i_flagJenis==riderMed.getJenis()) && (riderMed.getLsbs_id().compareTo(Integer.parseInt(kode_rider[i])) == 0 && riderMed.getLsdbs_number().compareTo(Integer.parseInt(number_rider[i])) == 0) ){
										umurttg = ppm.getUmur();
							        }
							    }
						     }		
						}
							
							klas_1[i]=rd1.getMspr_class();
							unit_1[i]=rd1.getMspr_unit();
							f_Up[i]=rd1.getPersenUp();
							reg_spaj = edit.getDatausulan().getReg_spaj();
							
							String nama_produk1="";
							if (Integer.parseInt(kode_rider[i]) <900 )
							{
								if (kode_rider[i].trim().length()==1)
								{
									nama_produk1="produk_asuransi.n_prod_0"+kode_rider[i];	
								}else{
									nama_produk1="produk_asuransi.n_prod_"+kode_rider[i];	
								}
									Class aClass1 = Class.forName( nama_produk1 );
									n_prod produk1 = (n_prod)aClass1.newInstance();
									produk1.setSqlMap(this.bacDao.getSqlMapClient());
									if(produk1.ii_bisnis_no_utama==0){
										produk1.ii_bisnis_id_utama= edit.getDatausulan().getLsbs_id();
										produk1.ii_bisnis_no_utama= edit.getDatausulan().getLsdbs_number();
									}
									if(reg_spaj == null){
										reg_spaj = "";
									}
									
									logger.info(rd1.getMspr_tsi());
									// *UP untuk CI
									double upForCI = 0.;
									double upForScholarship = 0.;
									double upForTerm = 0.;
									if(produk1.ii_bisnis_id == 835 ){
										upForScholarship=rd1.getMspr_tsi();
									}
									if(produk1.ii_bisnis_id == 818 ){
										upForTerm=rd1.getMspr_tsi();
									}
									
									if(produk1.ii_bisnis_id == 813 ||produk1.ii_bisnis_id == 830){
										
											   upForCI = up;
										double factor_x=0;
										 
										 if (f_Up[i]==1){
											 factor_x = 0.5;
										 }else if(f_Up[i]==2){
											 factor_x = 0.6;
										 }else if(f_Up[i]==3){
											 factor_x = 0.7;
										 }else if(f_Up[i]==4){
											 factor_x = 0.8;
										 }else if(f_Up[i]==5){
											 factor_x = 0.9;
										 }else if(f_Up[i]==6){
											 factor_x = 1.0;
										 }
										 if(reg_spaj == ""){
											 upForCI = upForCI * factor_x;
										 }else{
											 upForCI = rd1.getMspr_tsi();
										 }
									}								
									
									// Premi Total
									Double premi_tot = premi+premi_berkala+premi_tunggal;
									Double premiPayor=premi+premi_berkala;
									
									//Term Link yang baru, hitung rate biaya rider bukan dari UP pokok, tapi dari UP term
									//Jangan lupa di coding juga di dalem n_prod_lsbs yang count_rate_lsdbs nya, karena gak boleh dikali unit lagi!!!
									if((kode_produk.intValue() == 159 || kode_produk.intValue() == 160 || kode_produk.intValue() == 162 ) && (produk1.ii_bisnis_id == 810 || produk1.ii_bisnis_id == 818)) {
										produk1.count_rate(klas_1[i].intValue(),unit_1[i].intValue(),kode_produk.intValue(),Integer.parseInt(number_rider[i]),kurs,umurttg.intValue(),umurpp.intValue(),
												rd1.getMspr_tsi(),premi.doubleValue(),pmode.intValue(),1,insperiod.intValue(),payperiod.intValue());
									}else if((produk1.ii_bisnis_id == 822 && produk1.ii_bisnis_no==2) || (produk1.ii_bisnis_id == 810) || (produk1.ii_bisnis_id == 837) ){
										produk1.count_rate(klas_1[i].intValue(),unit_1[i].intValue(),kode_produk.intValue(),Integer.parseInt(number_rider[i]),kurs,umurttg.intValue(),umurpp.intValue(),
												rd1.getMspr_tsi(),premi.doubleValue(),pmode.intValue(),1,insperiod.intValue(),payperiod.intValue());
									}else if(produk1.ii_bisnis_id == 813 || produk1.ii_bisnis_id == 830){
										produk1.count_rate(klas_1[i].intValue(),unit_1[i].intValue(),kode_produk.intValue(),Integer.parseInt(number_rider[i]),kurs,umurttg.intValue(),umurpp.intValue(),
												upForCI,premi.doubleValue(),pmode.intValue(),1,insperiod.intValue(),payperiod.intValue());
									}else if("814,815,816,817,827,828".indexOf(Integer.toString(produk1.ii_bisnis_id))> -1){//Request Novi Semua Payor dan Waiver premi=premi pokok+PTB
										if(produk1.ii_bisnis_id==815 &&(Integer.parseInt(number_rider[i]) == 6||Integer.parseInt(number_rider[i]) == 13 )){
											produk1.count_rate(klas_1[i].intValue(),unit_1[i].intValue(),kode_produk.intValue(),Integer.parseInt(number_rider[i]),kurs,umurttg.intValue(),umurpp.intValue(),
													up.doubleValue(),premi.doubleValue(),pmode.intValue(),1,insperiod.intValue(),payperiod.intValue());
										}else{
											produk1.count_rate(klas_1[i].intValue(),unit_1[i].intValue(),kode_produk.intValue(),Integer.parseInt(number_rider[i]),kurs,umurttg.intValue(),umurpp.intValue(),
												up.doubleValue(),premiPayor.doubleValue(),pmode.intValue(),1,insperiod.intValue(),payperiod.intValue());
										}
									}else if("835".indexOf(Integer.toString(produk1.ii_bisnis_id))> -1){
										produk1.count_rate(klas_1[i].intValue(),unit_1[i].intValue(),kode_produk.intValue(),Integer.parseInt(number_rider[i]),kurs,umurttg.intValue(),umurpp.intValue(),
												upForScholarship,premiPayor.doubleValue(),pmode.intValue(),1,insperiod.intValue(),payperiod.intValue());
									}else if("818".indexOf(Integer.toString(produk1.ii_bisnis_id))> -1){
										produk1.count_rate(klas_1[i].intValue(),unit_1[i].intValue(),kode_produk.intValue(),Integer.parseInt(number_rider[i]),kurs,umurttg.intValue(),umurpp.intValue(),
												upForTerm,premiPayor.doubleValue(),pmode.intValue(),1,insperiod.intValue(),payperiod.intValue());																	
									}else {
										produk1.count_rate(klas_1[i].intValue(),unit_1[i].intValue(),kode_produk.intValue(),Integer.parseInt(number_rider[i]),kurs,umurttg.intValue(),umurpp.intValue(),
												up.doubleValue(),premi.doubleValue(),pmode.intValue(),1,insperiod.intValue(),payperiod.intValue());
									}
									
									edit.getDatausulan().setFlag_as(produk.flag_as); 
									
									mbu_jumlah[idx.intValue()]=new Double(produk1.mbu_jumlah);
									if(produk1.ii_bisnis_id== 823&&edit.getDatausulan().getLsbs_id()!=138 &&edit.getDatausulan().getMste_flag_el()==1){
										mbu_jumlah[idx.intValue()]=new Double(produk1.mbu_jumlah * 0.7);
									}
									BigDecimal mbu1 = new BigDecimal(mbu_jumlah[idx.intValue()].doubleValue());
									mbu1=mbu1.setScale(2,BigDecimal.ROUND_HALF_UP);
									mbu_jumlah[idx.intValue()]=new Double(mbu1.doubleValue());
		
									mbu_persen[idx.intValue()]=new Double(produk1.mbu_persen);
									//BigDecimal mbu2 = new BigDecimal(mbu_persen[idx.intValue()].doubleValue());
									//mbu2 =mbu2.setScale(2,BigDecimal.ROUND_HALF_UP);
									//mbu_persen[idx.intValue()]=new Double(mbu2.doubleValue());	
									f_hit_umur n_date=  new f_hit_umur();
									Date tgl_input = edit.getPemegang().getMspo_input_date();
									Integer tanggal = tgl_input.getDate();
									Integer bulan = tgl_input.getMonth()+1;
									Integer tahun = tgl_input.getYear()+1900;
									Integer hari = n_date.hari1( 2007,9, 17,tahun, bulan, tanggal);
									
									//per tgl 17/09/2007 ada biaya rider
									if (kode_produk.intValue() == 162 && number_produk.intValue() > 2)
									{
										if (hari >= 0)
										{
											if ((Integer.parseInt(kode_rider[i]) == 810) || (Integer.parseInt(kode_rider[i]) == 811) || (Integer.parseInt(kode_rider[i]) == 819) || (Integer.parseInt(kode_rider[i]) == 820))
											{
												by1.setMbu_jumlah((mbu_jumlah[idx.intValue()]));
												by1.setMbu_persen((mbu_persen[idx.intValue()]));
											}else{
												by1.setMbu_jumlah(new Double(0));
												by1.setMbu_persen(new Double(0));
											}
										}else{
											by1.setMbu_jumlah(new Double(0));
											by1.setMbu_persen(new Double(0));
										}
									}else{
										by1.setMbu_jumlah((mbu_jumlah[idx.intValue()]));
										by1.setMbu_persen((mbu_persen[idx.intValue()]));
									}
									by1.setMu_ke(new Integer(1));
									if (kode_produk.intValue() == 162 && number_produk.intValue() > 2)
									{
										if (hari >= 0)
										{
											if ((Integer.parseInt(kode_rider[i]) == 810) || (Integer.parseInt(kode_rider[i]) == 811) || (Integer.parseInt(kode_rider[i]) == 819) || (Integer.parseInt(kode_rider[i]) == 820))
											{
												total_mbu_jumlah=new Double(total_mbu_jumlah.doubleValue()+mbu_jumlah[idx.intValue()].doubleValue());
											}
										}
									}else{
										
										total_mbu_jumlah=new Double(total_mbu_jumlah.doubleValue()+mbu_jumlah[idx.intValue()].doubleValue());
									}
									Map data = null;
									data = (HashMap) this.bacDao.selectjenisbiaya((String)kode_rider[i],(String)number_rider[i]);
																		
									if (data!=null && Integer.parseInt(kode_rider[i]) != 838 && Integer.parseInt(kode_rider[i]) != 839)
									{
										mbu_jenis[idx.intValue()]=new Integer(Integer.parseInt(data.get("LJB_ID").toString()));
										mbu_nama_jenis[idx.intValue()]=data.get("LJB_BIAYA").toString();	
										by1.setLjb_biaya(mbu_nama_jenis[idx.intValue()]);
										by1.setLjb_id((mbu_jenis[idx.intValue()]));
										by1.setFlag_jenis("BIAYA ASURANSI");
										dtby.add(by1);
									}	
									//request actuary biaya smile medical + digabung jadi 1									
									if(Integer.parseInt(kode_rider[i]) == 838 || Integer.parseInt(kode_rider[i]) == 839){
										d_biayaSmileMedical=d_biayaSmileMedical+mbu_jumlah[idx.intValue()];
									}
									
									// SK Direksi No. 026/AJS-SK/V/2010 : Biaya admin & dan Biaya admedika ditiadakan (per tanggal 15 Juni 2010)
									if(Integer.parseInt(kode_rider[i])==820){
										by1= new Biayainvestasi();
										produk.biaya3(ldec_awal.doubleValue());
										//comment : (22/7/2009) req : Dr.Ingrid untuk biaya admin eka sehat diset 37000.
//										if(premi >=1000000){
//											mbu_jumlah3=new Double(37000.0);
//										}else if(premi <1000000){
//											mbu_jumlah3=new Double(31000.0);
//										}
										mbu_jumlah3=new Double(37000.0);
										mbu_persen3=new Double(0.0);
										bd3 = new BigDecimal(mbu_jumlah3.doubleValue());
										mbu_jumlah3_3=bd3.setScale(3,BigDecimal.ROUND_HALF_UP).toString();		
										mbu_jenis[2]=new Integer(435);
										mbu_nama_jenis[2]="BIAYA ADMIN EKA SEHAT";
										mbu_jumlah[2]=new Double(Double.parseDouble(mbu_jumlah3_3));
										mbu_persen[2]=mbu_persen3;	
										by1.setLjb_biaya(mbu_nama_jenis[2]);
										by1.setLjb_id((mbu_jenis[2]));
										by1.setMbu_jumlah((mbu_jumlah[2]));
										by1.setMbu_persen((mbu_persen[2]));
										by1.setMu_ke(new Integer(1));
										by1.setFlag_jenis("BIAYA ASURANSI");
										dtby.add(by1);
										total_mbu_jumlah=new Double(total_mbu_jumlah.doubleValue()+mbu_jumlah[2].doubleValue());
										if(edit.getDatausulan().getMspo_provider()!=null){
											if(edit.getDatausulan().getMspo_provider()==2){
												by1= new Biayainvestasi();
												produk.biaya3(ldec_awal.doubleValue());
												//comment : (22/7/2009) req : Dr.Ingrid untuk biaya admin eka sehat diset 37000.
//												if(premi >=1000000){
//													mbu_jumlah3=new Double(37000.0);
//												}else if(premi <1000000){
//													mbu_jumlah3=new Double(31000.0);
//												}
												mbu_jumlah3=new Double(30000.0);
												mbu_persen3=new Double(0.0);
												bd3 = new BigDecimal(mbu_jumlah3.doubleValue());
												mbu_jumlah3_3=bd3.setScale(3,BigDecimal.ROUND_HALF_UP).toString();		
												String tertanggung = "";
												if(Integer.parseInt(number_rider[i])>=1 && Integer.parseInt(number_rider[i])<=15){
													mbu_jenis[2]=new Integer(443);
													tertanggung = "UTAMA";
												}else if(Integer.parseInt(number_rider[i])>=16 && Integer.parseInt(number_rider[i])<=30){
													mbu_jenis[2]=new Integer(444);
													tertanggung = "TAMBAHAN-1";
												}else if(Integer.parseInt(number_rider[i])>=31 && Integer.parseInt(number_rider[i])<=45){
													mbu_jenis[2]=new Integer(445);
													tertanggung = "TAMBAHAN-2";
												}else if(Integer.parseInt(number_rider[i])>=46 && Integer.parseInt(number_rider[i])<=60){
													mbu_jenis[2]=new Integer(446);
													tertanggung = "TAMBAHAN-3";
												}else if(Integer.parseInt(number_rider[i])>=61 && Integer.parseInt(number_rider[i])<=75){
													mbu_jenis[2]=new Integer(447);
													tertanggung = "TAMBAHAN-4";
												}else if(Integer.parseInt(number_rider[i])>=76 && Integer.parseInt(number_rider[i])<=90){
													mbu_jenis[2]=new Integer(448);
													tertanggung = "TAMBAHAN-5";
												}
												mbu_nama_jenis[2]="BIAYA ADMIN KLAIM EKA SEHAT PROVIDER(TERTANGGUNG "+tertanggung+")";
												mbu_jumlah[2]=new Double(Double.parseDouble(mbu_jumlah3_3));
												mbu_persen[2]=mbu_persen3;	
												by1.setLjb_biaya(mbu_nama_jenis[2]);
												by1.setLjb_id((mbu_jenis[2]));
												by1.setMbu_jumlah((mbu_jumlah[2]));
												by1.setMbu_persen((mbu_persen[2]));
												by1.setMu_ke(new Integer(1));
												by1.setFlag_jenis("BIAYA ASURANSI");
												dtby.add(by1);
												total_mbu_jumlah=new Double(total_mbu_jumlah.doubleValue()+mbu_jumlah[2].doubleValue());
												
											}
										}
									}
							}
						}
						if(d_biayaSmileMedical>0){
							Map data2 = null;
							data2 = (HashMap) this.bacDao.selectjenisbiaya("838","1");
							by1.setLjb_id(Integer.parseInt(data2.get("LJB_ID").toString()));
							by1.setLjb_biaya(data2.get("LJB_BIAYA").toString());
							by1.setMbu_jumlah(d_biayaSmileMedical);
							by1.setMbu_persen(new Double(0));
							by1.setFlag_jenis("BIAYA ASURANSI");
							dtby.add(by1);
						}
					}
					
					ld_premi_invest = new Double(premi.doubleValue()-total_mbu_jumlah.doubleValue()); 
					jumlah_minus = ld_premi_invest;
					
					if ( (ld_premi_invest.doubleValue()< 0))
					{
						if ( (premi_berkala.doubleValue()<= 0)&&(premi_tunggal.doubleValue()<= 0))
						{
							if (pmode.intValue() == 1)
							{
								m.put("daftartopup.premi_tunggal","Silahkan menambahkan top up terlebih dahulu, karena dana yang tersedia tidak cukup untuk biaya .");
								m.put("daftartopup.pil_tunggal","Silahkan memilih jenis top up terlebih dahulu, karena dana yang tersedia tidak cukup untuk biaya .");
							}else{
								m.put("daftartopup.premi_berkala","Silahkan menambahkan top up terlebih dahulu, karena dana yang tersedia tidak cukup untuk biaya .");
								m.put("daftartopup.pil_berkala","Silahkan memilih jenis top up terlebih dahulu, karena dana yang tersedia tidak cukup untuk biaya .");
							}
						}else{
							Double jumlah_top_up = new Double(premi_tunggal.doubleValue() + premi_berkala.doubleValue());
							if (jumlah_top_up.doubleValue()< (0 - jumlah_minus.doubleValue()))
							{
								if (pmode.intValue() == 1)
								{
									m.put("daftartopup.premi_tunggal","Silahkan menambahkan jumlah top up lebih besar daripada kekurangan premi .");
								}else{
									m.put("daftartopup.premi_berkala","Silahkan menambahkan jumlah top up lebih besar daripada kekurangan premi .");
								}
							}
						}
					}
					
					
//					minus
					if (ld_premi_invest.doubleValue()< 0)
					{
						by1= new Biayainvestasi();
						idx=new Integer(idx.intValue()+1);
						flag_minus = true;
						
						mbu_jumlah[idx.intValue()]=ld_premi_invest;

						mbu_persen[idx.intValue()]=new Double(0);
						by1.setMbu_jumlah((mbu_jumlah[idx.intValue()]));
						by1.setMbu_persen((mbu_persen[idx.intValue()]));
						
						mbu_jenis[idx.intValue()]=new Integer(99);
						mbu_nama_jenis[idx.intValue()]="KEKURANGAN PREMI";	
						by1.setLjb_biaya(mbu_nama_jenis[idx.intValue()]);
						by1.setLjb_id((mbu_jenis[idx.intValue()]));
						by1.setMu_ke(new Integer(1));
						by1.setFlag_jenis("Biaya Lain2");
						dtby.add(by1);

						if ((pil_berkala.intValue()>0) && (pil_tunggal.intValue()>0))
						{
							Double kekurangan =new Double(0 - ld_premi_invest.doubleValue());
							Double biaya_berkala = new Double(0);
							if (pil_berkala.intValue()>0)
							{
								by1= new Biayainvestasi();
								Double biaya_topup = new Double(0);
								Double biaya_topup_sementara = new Double(premi_berkala.doubleValue() * produk.li_pct_biaya / 100);
								biaya_topup = new Double(premi_berkala.doubleValue() - biaya_topup_sementara.doubleValue() - kekurangan.doubleValue());
								if (biaya_topup.doubleValue() < 0)
								{
									biaya_topup = new Double(premi_berkala.doubleValue() - biaya_topup_sementara.doubleValue());
								}else{
									biaya_topup =kekurangan.doubleValue();
								}
								biaya_berkala = biaya_topup;
								String biaya_topup1=null;
								BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
								biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
								idx=new Integer(idx.intValue()+1);
								by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
								by1.setMbu_persen(new Double(0));						
								by1.setLjb_biaya("KEKURANGAN PREMI");
								by1.setLjb_id(new Integer(99));
								by1.setMu_ke(new Integer(2));
								by1.setFlag_jenis("Biaya Lain2");
								dtby.add(by1);
							}
							if (pil_tunggal.intValue()>0)
							{
								if ((biaya_berkala.doubleValue()- kekurangan.doubleValue())<0)
								{
									by1= new Biayainvestasi();
									Double biaya_topup = new Double(0);
									biaya_topup = new Double(( kekurangan.doubleValue() - biaya_berkala.doubleValue()));
									String biaya_topup1=null;
									BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
									biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
									idx=new Integer(idx.intValue()+1);
									by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
									by1.setMbu_persen(new Double(0));						
									by1.setLjb_biaya("KEKURANGAN PREMI");
									by1.setLjb_id(new Integer(99));
									by1.setMu_ke(new Integer(3));
									by1.setFlag_jenis("Biaya Lain2");
									dtby.add(by1);
								}
							}							
						}else{
							by1= new Biayainvestasi();
							idx=new Integer(idx.intValue()+1);
							mbu_jumlah[idx.intValue()]=new Double(0 - ld_premi_invest.doubleValue());
							mbu_persen[idx.intValue()]=new Double(0);
							mbu_jenis[idx.intValue()]=new Integer(99);
							mbu_nama_jenis[idx.intValue()]="KEKURANGAN PREMI";	
							by1.setMbu_jumlah(mbu_jumlah[idx.intValue()]);
							by1.setMbu_persen((mbu_persen[idx.intValue()]));						
							by1.setLjb_biaya(mbu_nama_jenis[idx.intValue()]);
							by1.setLjb_id((mbu_jenis[idx.intValue()]));
							by1.setMu_ke(new Integer(2));
							by1.setFlag_jenis("Biaya Lain2");
							dtby.add(by1);
							edit.getInvestasiutama().setDaftarbiaya(dtby);
							edit.getInvestasiutama().setJmlh_biaya(idx);
						}
	
						edit.getInvestasiutama().setTotal_biaya_premi(new Double(0));
						total_mbu_jumlah=premi;
						
					}
					
					edit.getInvestasiutama().setDaftarbiaya(dtby);
					edit.getInvestasiutama().setJmlh_biaya(idx);
					
					BigDecimal mbu = new BigDecimal(total_mbu_jumlah.doubleValue());
					mbu = mbu.setScale(2,BigDecimal.ROUND_HALF_UP);
					total_mbu_jumlah=new Double(mbu.doubleValue());	
					
					ld_premi_invest = new Double(premi.doubleValue()-total_mbu_jumlah.doubleValue()); 	
					edit.getInvestasiutama().setTotal_biaya_premi((ld_premi_invest));	
				
				}
				edit.getInvestasiutama().setDaftarbiaya(dtby);
				edit.getInvestasiutama().setJmlh_biaya(idx);
				//if (flag_as.intValue() != 2)
				//{
				
					Integer flag_topup = 0;
					if (pil_berkala.intValue()>0)
					{
						by1= new Biayainvestasi();
						Double biaya_topup = new Double(0);
						biaya_topup = new Double(premi_berkala.doubleValue() * produk.li_pct_biaya / 100);
						String biaya_topup1=null;
						BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
						biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
						idx=new Integer(idx.intValue()+1);
						if (flag_as.intValue() == 2 || produk.ii_bisnis_id==191)
						{
							by1.setMbu_jumlah(new Double(new Double(0)));
							by1.setMbu_persen(new Double(0));		
						}else{
							by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
							by1.setMbu_persen(new Double(produk.li_pct_biaya));		
						}
						by1.setLjb_biaya("BIAYA TOP-UP");
						by1.setLjb_id(new Integer(4));
						by1.setMu_ke(new Integer(2));
						by1.setFlag_jenis("Biaya TopUp");
						dtby.add(by1);
						flag_topup = 1;
					}
					if (pil_tunggal.intValue()>0)
					{
						by1= new Biayainvestasi();
						Double biaya_topup = new Double(0);
						biaya_topup = new Double(premi_tunggal.doubleValue() * produk.li_pct_biaya / 100);
						String biaya_topup1=null;
						BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
						biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
						idx=new Integer(idx.intValue()+1);
						if (flag_as.intValue() == 2 || produk.ii_bisnis_id==191)
						{
							by1.setMbu_jumlah(new Double(new Double(0)));
							by1.setMbu_persen(new Double(0));		
						}else{
							by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
							by1.setMbu_persen(new Double(produk.li_pct_biaya));	
						}
						by1.setLjb_biaya("BIAYA TOP-UP");
						by1.setLjb_id(new Integer(4));
						if (flag_topup == 0)
						{
							by1.setMu_ke(new Integer(2));
						}else{
							by1.setMu_ke(new Integer(3));
						}
						by1.setFlag_jenis("Biaya TopUp");
						dtby.add(by1);
					}
				//}
				
				edit.getInvestasiutama().setJmlh_biaya((idx));
				edit.getInvestasiutama().setTotal_biaya_premi((ld_premi_invest));	
				edit.getInvestasiutama().setDaftarbiaya(dtby);
				m.put("ld_premi_invest",ld_premi_invest);
				m.put("flag_minus",new Boolean(flag_minus));
				m.put("jumlah_minus",jumlah_minus);

				List by2= edit.getInvestasiutama().getDaftarbiaya();
				if (kode_flag.intValue()>1  )
				{
					edit.getInvestasiutama().setJmlh_biaya(new Integer(by2.size()));
				}
				
			
			return m;
	}
	
	public Map proc_rider_hcp(Cmdeditbac edit,Map m,Integer kode_flag , Integer flag_rider,String kurs ,Double premi, Integer pmode, Double up, Integer pil_berkala, Integer pil_tunggal, Integer flag_as, Double premi_berkala, Double premi_tunggal)throws ServletException,IOException,Exception
	{
		//dari plan utama
		String reg_spaj = edit.getDatausulan().getReg_spaj();
		Integer kode_produk= edit.getDatausulan().getLsbs_id();
		Integer number_produk=edit.getDatausulan().getLsdbs_number();
		Integer payperiod = edit.getDatausulan().getMspo_pay_period();
		Integer insperiod =  edit.getDatausulan().getMspr_ins_period();
		Integer umurttg =  edit.getTertanggung().getMste_age();
		
		//define variabel
		Integer indeks_biaya=new Integer(0);
		Integer[] mbu_jenis;
		String[] mbu_nama_jenis;
		Double[] mbu_jumlah;
		Double[] mbu_persen;
		Double mbu_jumlah1=new Double(0);
		String mbu_jumlah1_1="";
		Double mbu_persen1=new Double(0);
		Double mbu_jumlah2=new Double(0);
		String mbu_jumlah2_2="";
		Double mbu_persen2=new Double(0);
		Double mbu_jumlah3=new Double(0);
		String mbu_jumlah3_3="";		
		Double mbu_persen3=new Double(0);
		Integer[] klas_1;
		Integer[] unit_1;
		String[] kode_rider;
		String[] number_rider;	
		Integer[] f_Up;
		Double jumlah_minus = new Double(0);
		boolean flag_minus = false;
		Double total_mbu_jumlah=new Double(0);
		String ttl_mbu_jumlah="";
		Double ld_premi_invest=new Double(0);
		Integer ldec_pct=new Integer(1);
		String hasil_jns_top_up="";
		String nama_produk="";
		Integer jangka_investasi = new Integer(0);
		Double bunga=new Double(0);
		if (umurttg==null)
		{
			umurttg=new Integer(0);
		}
		Integer umurpp = edit.getPemegang().getMste_age();
		if (umurpp==null)
		{
			umurpp=new Integer(0);
		}
		
		//jumlah rider
		List hcp = edit.getDatausulan().getDaftahcp();
		Integer jmlh_rider = hcp.size();
		if (jmlh_rider==null)
		{
			jmlh_rider= new Integer(0);
		}
		Integer jmlh_rider_old = edit.getDatausulan().getJmlrider();
		if (jmlh_rider_old == null)
		{
			jmlh_rider_old = new Integer (0);
		}else if (jmlh_rider_old.intValue() > 0)
		{
			jmlh_rider_old = new Integer(jmlh_rider_old.intValue());
		}
		Integer jumlah_rider_semua = new Integer(jmlh_rider.intValue() + jmlh_rider_old.intValue());
		Integer umur_rider = new Integer(0);
		
		if (Integer.toString(kode_produk.intValue()).trim().length()==1)
		{
			nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
		}else{
			nama_produk="produk_asuransi.n_prod_"+kode_produk;	
		}

			Class aClass = Class.forName( nama_produk );
			n_prod produk = (n_prod)aClass.newInstance();
			produk.setSqlMap(this.bacDao.getSqlMapClient());
			produk.is_kurs_id=kurs;
			
			//Biaya
			Integer idx=new Integer(0);
			if (produk.flag_rider==0)
			{
				indeks_biaya=new Integer(4);	
			}else{
				if (flag_rider.intValue()==1)
				{
					indeks_biaya=new Integer(3 + jumlah_rider_semua.intValue() + 4);
				}else{
					indeks_biaya=new Integer(6);
				}
			}

			edit.getInvestasiutama().setJmlh_biaya(indeks_biaya);
				
			mbu_jumlah = new Double[indeks_biaya.intValue()];
			mbu_persen = new Double[indeks_biaya.intValue()];
			mbu_jenis = new Integer[indeks_biaya.intValue()];
			mbu_nama_jenis = new String[indeks_biaya.intValue()];
				
			edit.getDatausulan().setLi_trans_berkala(new Integer(produk.li_trans));
			edit.getDatausulan().setLi_trans_tunggal(new Integer(produk.li_trans));
			if (pil_tunggal.intValue()==2)
			{
				edit.getDatausulan().setLi_trans_tunggal(new Integer(2));
			}else if (pil_berkala.intValue()==3)
				{
					edit.getDatausulan().setLi_trans_berkala(new Integer(6));
				}
				
			List dtby = new ArrayList();
			Integer jumlah_r =indeks_biaya;
			if (jumlah_r==null)
			{
				jumlah_r=new Integer(0);
			}
			
			//ambil dari list biaya
			Biayainvestasi by1= new Biayainvestasi();
			List biaya_link= edit.getInvestasiutama().getDaftarbiaya();

			// untuk akuisisi dan asuransi ambil dari yang sudah ada.. tidak dihitung lagi
			if(!Common.isEmpty(biaya_link)){
			for (int k=0 ; k < (biaya_link.size()) ; k++)
				{
					Biayainvestasi bf= (Biayainvestasi)biaya_link.get(k);
					if (bf.getLjb_id().intValue() == 1)
					{
						by1= new Biayainvestasi();
						mbu_jumlah1=bf.getMbu_jumlah();
						mbu_persen1=bf.getMbu_persen();
						BigDecimal bd1 = new BigDecimal(mbu_jumlah1.doubleValue());
						mbu_jumlah1_1=bd1.setScale(3,BigDecimal.ROUND_HALF_UP).toString();
						mbu_jenis[0]=bf.getLjb_id();
						mbu_nama_jenis[0]="BIAYA AKUISISI";
						mbu_jumlah[0]=new Double(Double.parseDouble(mbu_jumlah1_1));
						mbu_persen[0]=mbu_persen1;
						idx=new Integer(1);
						by1.setLjb_biaya(mbu_nama_jenis[0]);
						by1.setLjb_id((mbu_jenis[0]));
						by1.setMbu_jumlah((mbu_jumlah[0]));
						by1.setMbu_persen((mbu_persen[0]));
						total_mbu_jumlah=new Double(total_mbu_jumlah.doubleValue() + by1.getMbu_jumlah().doubleValue());
						by1.setFlag_jenis("BIAYA AKUISISI");
						by1.setMu_ke(new Integer(1));
						dtby.add(by1);
					}else if (bf.getLjb_id().intValue() == 2){
							by1= new Biayainvestasi();
							mbu_jumlah2=bf.getMbu_jumlah();
							mbu_persen2=bf.getMbu_persen();
							BigDecimal bd2 = new BigDecimal(mbu_jumlah2.doubleValue());
							mbu_jumlah2_2=bd2.setScale(3,BigDecimal.ROUND_HALF_UP).toString();		
							mbu_jenis[1]=bf.getLjb_id();
							mbu_nama_jenis[1]="BIAYA ASURANSI";
							mbu_jumlah[1]=new Double(Double.parseDouble(mbu_jumlah2_2));
							mbu_persen[1]=mbu_persen2;	
							idx=new Integer(2);
							by1.setLjb_biaya(mbu_nama_jenis[1]);
							by1.setLjb_id((mbu_jenis[1]));
							by1.setMbu_jumlah((mbu_jumlah[1]));
							by1.setMbu_persen((mbu_persen[1]));
							by1.setMu_ke(new Integer(1));
							total_mbu_jumlah=new Double(total_mbu_jumlah.doubleValue() + by1.getMbu_jumlah().doubleValue());
							by1.setFlag_jenis("BIAYA ASURANSI");
							dtby.add(by1);
						}else if (bf.getLjb_id().intValue() == 3){
								by1= new Biayainvestasi();
								mbu_jumlah3=bf.getMbu_jumlah();
								mbu_persen3=bf.getMbu_persen();
								BigDecimal bd3 = new BigDecimal(mbu_jumlah3.doubleValue());
								mbu_jumlah3_3=bd3.setScale(3,BigDecimal.ROUND_HALF_UP).toString();		
								mbu_jenis[2]=bf.getLjb_id();
								mbu_nama_jenis[2]="BIAYA ADMINISTRASI";
								mbu_jumlah[2]=new Double(Double.parseDouble(mbu_jumlah3_3));
								mbu_persen[2]=mbu_persen3;	
								idx=new Integer(3);
								by1.setLjb_biaya(mbu_nama_jenis[2]);
								by1.setLjb_id((mbu_jenis[2]));
								by1.setMbu_jumlah((mbu_jumlah[2]));
								by1.setMbu_persen((mbu_persen[2]));
								by1.setMu_ke(new Integer(1));
								total_mbu_jumlah=new Double(total_mbu_jumlah.doubleValue() + by1.getMbu_jumlah().doubleValue());
								by1.setFlag_jenis("BIAYA ADMINISTRASI");
								dtby.add(by1);
							}else if (bf.getLjb_id().intValue() == 12){
									by1= new Biayainvestasi();
									mbu_jenis[3]=bf.getLjb_id();
									mbu_nama_jenis[3]="BIAYA POKOK";
									mbu_jumlah[3]=bf.getMbu_jumlah();
									mbu_persen[3]=bf.getMbu_persen();	
									idx=new Integer(4);
									by1.setLjb_biaya(mbu_nama_jenis[3]);
									by1.setLjb_id((mbu_jenis[3]));
									by1.setMbu_jumlah((mbu_jumlah[3]));
									by1.setMbu_persen((mbu_persen[3]));
									by1.setMu_ke(new Integer(1));
									total_mbu_jumlah=new Double(total_mbu_jumlah.doubleValue() + by1.getMbu_jumlah().doubleValue());
									by1.setFlag_jenis("BIAYA POKOK");
									dtby.add(by1);	
								}
					edit.getInvestasiutama().setJmlh_biaya((idx));
					edit.getInvestasiutama().setTotal_biaya_premi((total_mbu_jumlah));
	
				}
				Double total_extra_premi = 0.;
				Integer count_extra_premi = 0;
				if(edit.getInvestasiutama().getDaftarbiaya()!=null){
					if(edit.getPemegang().getStatus().equalsIgnoreCase("edit")){
						for (int i=0 ;i<edit.getInvestasiutama().getDaftarbiaya().size() ; i++)
						{
							Biayainvestasi bi = (Biayainvestasi) edit.getInvestasiutama().getDaftarbiaya().get(i);
							String jenisExtra = bi.getFlag_jenis().toUpperCase();
							if(jenisExtra.equals("BIAYA EXTRA")){
								total_extra_premi=total_extra_premi+bi.getMbu_jumlah();
								count_extra_premi++;
								dtby.add(bi);
							}							
						}
					}
				}
				edit.getInvestasiutama().setJmlh_biaya((idx+count_extra_premi));
				edit.getInvestasiutama().setTotal_biaya_premi((total_mbu_jumlah+total_extra_premi));
			}
			//Integer jumlah_rider_semua = new Integer(jmlh_rider.intValue() + jmlh_rider_old.intValue());
			
			List rider_old = edit.getDatausulan().getDaftaRider();
			List rider_new = edit.getDatausulan().getDaftariderhcp();
			List rider = new ArrayList();
			Datarider datarider = new Datarider();
			//rider lama yang diset dari program utama selain 819
			for (int k = 0 ; k <rider_old.size() ; k++)
			{
				datarider = new Datarider();
				Datarider dr= (Datarider)rider_old.get(k);
				if (dr.getLsbs_id().intValue() != 820 && dr.getLsbs_id().intValue() != 823 && dr.getLsbs_id().intValue() != 838) // dr.getLsbs_id().intValue() != 819 && 
				{
					datarider = dr;
					rider.add(datarider);
				}
			}
			//ride baru yang diset menu hcpf semua 819, 823
//			for (int k= 0 ; k< rider_new.size() ; k++)
//			{
//				datarider = new Datarider();
//				Datarider dr= (Datarider)rider_new.get(k);
//				datarider = dr;
//				rider.add(datarider);
//			}
			edit.getDatausulan().setDaftariderall(rider);
			jmlh_rider = rider.size();
			if (produk.flag_rider==0)
			{
				ld_premi_invest = new Double(premi.doubleValue()-total_mbu_jumlah.doubleValue()); 	
			}else{
//				if (produk.flag_platinumlink == 0 || (produk.flag_platinumlink == 1 && number_produk.intValue()==2))
				if ((produk.flag_platinumlink == 0 && kode_produk.intValue()!=190 &&kode_produk.intValue()!=191) || (produk.flag_platinumlink == 1 && number_produk.intValue()==2 && !(kode_produk.intValue()==166 && number_produk.intValue()==2) && !(kode_produk.intValue()==134 && number_produk.intValue()==3) ) )
				{
					kode_rider = new String[jmlh_rider.intValue()];
					number_rider = new String[jmlh_rider.intValue()];
					klas_1 = new Integer[jmlh_rider.intValue()];
					unit_1 = new Integer[jmlh_rider.intValue()];
					f_Up = new Integer[jmlh_rider.intValue()];
							
					List dtrd= edit.getDatausulan().getDaftariderall();
					for (int i=0 ;i<dtrd.size() ; i++)
					{
						by1= new Biayainvestasi();
						Datarider rd1= (Datarider)dtrd.get(i);
						//if (i>0)
						//{
							idx=new Integer(idx.intValue()+1);
						//}
						kode_rider[i]=Integer.toString(rd1.getLsbs_id().intValue());
						number_rider[i]=Integer.toString(rd1.getLsdbs_number().intValue());
						klas_1[i]=rd1.getMspr_class();
						unit_1[i]=rd1.getMspr_unit();
						f_Up[i]=rd1.getPersenUp();
							
						if (kode_rider[i].equalsIgnoreCase("819") || kode_rider[i].equalsIgnoreCase("820")  || kode_rider[i].equalsIgnoreCase("823") || kode_rider[i].equalsIgnoreCase("825"))
						{
							for(int j = 0 ; j <hcp.size() ; j++)
							{
								Hcp dthcp = (Hcp)hcp.get(j);
								if (number_rider[i].equalsIgnoreCase(Integer.toString(dthcp.getLsdbs_number())))
								{
									umur_rider = dthcp.getUmur();
								}
							}
						}else{
							umur_rider = edit.getTertanggung().getMste_age();
						}
						String nama_produk1="";
						if ((Integer.parseInt(kode_rider[i]) <900 ) && (Integer.parseInt(kode_rider[i]) > 0 ))
						{
							if (kode_rider[i].trim().length()==1)
							{
								nama_produk1="produk_asuransi.n_prod_0"+kode_rider[i];	
							}else{
								nama_produk1="produk_asuransi.n_prod_"+kode_rider[i];	
							}
							Class aClass1 = Class.forName( nama_produk1 );
							n_prod produk1 = (n_prod)aClass1.newInstance();
							produk1.setSqlMap(this.bacDao.getSqlMapClient());
							if(kode_rider[i].equals("815") && Integer.parseInt(number_rider[i])!=6){
								produk1.count_rate(klas_1[i].intValue(),unit_1[i].intValue(),kode_produk.intValue(),Integer.parseInt(number_rider[i]),kurs,umur_rider.intValue(),umurpp.intValue(),up.doubleValue(),premi.doubleValue()+premi_berkala.doubleValue(),pmode.intValue(),1,insperiod.intValue(),payperiod.intValue());
							}else if("814,816,817,827,828".indexOf(kode_rider[i])>=0){
								produk1.count_rate(klas_1[i].intValue(),unit_1[i].intValue(),kode_produk.intValue(),Integer.parseInt(number_rider[i]),kurs,umur_rider.intValue(),umurpp.intValue(),up.doubleValue(),premi.doubleValue()+premi_berkala.doubleValue(),pmode.intValue(),1,insperiod.intValue(),payperiod.intValue());
							}else{
								produk1.count_rate(klas_1[i].intValue(),unit_1[i].intValue(),kode_produk.intValue(),Integer.parseInt(number_rider[i]),kurs,umur_rider.intValue(),umurpp.intValue(),up.doubleValue(),premi.doubleValue(),pmode.intValue(),1,insperiod.intValue(),payperiod.intValue());
							}
							mbu_jumlah[idx.intValue()]=new Double(produk1.mbu_jumlah);
							BigDecimal mbu1 = new BigDecimal(mbu_jumlah[idx.intValue()].doubleValue());
							mbu1=mbu1.setScale(2,BigDecimal.ROUND_HALF_UP);
							mbu_jumlah[idx.intValue()]=new Double(mbu1.doubleValue());
		
							mbu_persen[idx.intValue()]=new Double(produk1.mbu_persen);
							f_hit_umur n_date=  new f_hit_umur();
							Date tgl_input = edit.getPemegang().getMspo_input_date();
							Integer tanggal = tgl_input.getDate();
							Integer bulan = tgl_input.getMonth()+1;
							Integer tahun = tgl_input.getYear()+1900;
							Integer hari = n_date.hari(2007,9, 17, tahun, bulan, tanggal);
							//per tgl 17/09/2007 ada biaya rider permintaan dr lindawati
							if (kode_produk.intValue() == 162 && number_produk.intValue() > 2)
							{
								if (hari >= 0)
								{
									if ((Integer.parseInt(kode_rider[i]) == 810) || (Integer.parseInt(kode_rider[i]) == 811) || (Integer.parseInt(kode_rider[i]) == 819) )
									{
										by1.setMbu_jumlah((mbu_jumlah[idx.intValue()]));
										by1.setMbu_persen((mbu_persen[idx.intValue()]));
					
									}else{
										by1.setMbu_jumlah(new Double(0));
										by1.setMbu_persen(new Double(0));
									}
								}else{
									by1.setMbu_jumlah(new Double(0));
									by1.setMbu_persen(new Double(0));
								}
							}else{
								by1.setMbu_jumlah((mbu_jumlah[idx.intValue()]));
								by1.setMbu_persen((mbu_persen[idx.intValue()]));
							}
							by1.setMu_ke(new Integer(1));
							if (kode_produk.intValue() == 162 && number_produk.intValue() > 2)
							{
								if (hari >= 0)
								{
									if ((Integer.parseInt(kode_rider[i]) == 810) || (Integer.parseInt(kode_rider[i]) == 811) || (Integer.parseInt(kode_rider[i]) == 819) )
									{
										total_mbu_jumlah=new Double(total_mbu_jumlah.doubleValue()+mbu_jumlah[idx.intValue()].doubleValue());
									}
								}
							}else{
								total_mbu_jumlah=new Double(total_mbu_jumlah.doubleValue()+mbu_jumlah[idx.intValue()].doubleValue());
							}
							
							Map data = (HashMap) this.bacDao.selectjenisbiaya((String)kode_rider[i],(String)number_rider[i]);
							if (data!=null)
							{
								mbu_jenis[idx.intValue()]=new Integer(Integer.parseInt(data.get("LJB_ID").toString()));
								mbu_nama_jenis[idx.intValue()]=data.get("LJB_BIAYA").toString();	
								by1.setLjb_biaya(mbu_nama_jenis[idx.intValue()]);
								by1.setLjb_id((mbu_jenis[idx.intValue()]));
								by1.setFlag_jenis("Biaya Asuransi");
								dtby.add(by1);
							}	
						}
					}
				}
					
				ld_premi_invest = new Double(premi.doubleValue()-total_mbu_jumlah.doubleValue()); 
				jumlah_minus = ld_premi_invest;
					
				if ( (ld_premi_invest.doubleValue()< 0))
				{
					if ( (premi_berkala.doubleValue()<= 0)&&(premi_tunggal.doubleValue()<= 0))
					{
						if (pmode.intValue() == 1)
						{
							m.put("daftartopup.premi_tunggal","Silahkan menambahkan top up terlebih dahulu, karena dana yang tersedia tidak cukup untuk biaya .");
							m.put("daftartopup.pil_tunggal","Silahkan memilih jenis top up terlebih dahulu, karena dana yang tersedia tidak cukup untuk biaya .");
						}else{
							m.put("daftartopup.premi_berkala","Silahkan menambahkan top up terlebih dahulu, karena dana yang tersedia tidak cukup untuk biaya .");
							m.put("daftartopup.pil_berkala","Silahkan memilih jenis top up terlebih dahulu, karena dana yang tersedia tidak cukup untuk biaya .");
							}
					}else{
						Double jumlah_top_up = new Double(premi_tunggal.doubleValue() + premi_berkala.doubleValue());
						if (jumlah_top_up.doubleValue()< (0 - jumlah_minus.doubleValue()))
						{
							if (pmode.intValue() == 1)
							{
								m.put("daftartopup.premi_tunggal","Silahkan menambahkan jumlah top up lebih besar daripada kekurangan premi .");
							}else{
								m.put("daftartopup.premi_berkala","Silahkan menambahkan jumlah top up lebih besar daripada kekurangan premi .");
							}
						}
					}
				}
					
					
//					minus
				if (ld_premi_invest.doubleValue()< 0)
				{
					by1= new Biayainvestasi();
					idx=new Integer(idx.intValue()+1);
					flag_minus = true;
						
					mbu_jumlah[idx.intValue()]=ld_premi_invest;

					mbu_persen[idx.intValue()]=new Double(0);
					by1.setMbu_jumlah((mbu_jumlah[idx.intValue()]));
					by1.setMbu_persen((mbu_persen[idx.intValue()]));
						
					mbu_jenis[idx.intValue()]=new Integer(99);
					mbu_nama_jenis[idx.intValue()]="KEKURANGAN PREMI";	
					by1.setLjb_biaya(mbu_nama_jenis[idx.intValue()]);
					by1.setLjb_id((mbu_jenis[idx.intValue()]));
					by1.setMu_ke(new Integer(1));
					by1.setFlag_jenis("Biaya Lain2");
					dtby.add(by1);

					//ambil top up berkala dan top up tunggal
					if ((pil_berkala.intValue()>0) && (pil_tunggal.intValue()>0))
					{
						Double kekurangan =new Double(0 - ld_premi_invest.doubleValue());
						Double biaya_berkala = new Double(0);
						if (pil_berkala.intValue()>0)
						{
							by1= new Biayainvestasi();
							Double biaya_topup = new Double(0);
							Double biaya_topup_sementara = new Double(premi_berkala.doubleValue() * produk.li_pct_biaya / 100);
							biaya_topup = new Double(premi_berkala.doubleValue() - biaya_topup_sementara.doubleValue());
							biaya_berkala = biaya_topup;
							String biaya_topup1=null;
							BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
							biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
							idx=new Integer(idx.intValue()+1);
							by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
							by1.setMbu_persen(new Double(0));						
							by1.setLjb_biaya("KEKURANGAN PREMI");
							by1.setLjb_id(new Integer(99));
							by1.setMu_ke(new Integer(2));
							by1.setFlag_jenis("Biaya Lain2");
							dtby.add(by1);
						}
						if (pil_tunggal.intValue()>0)
						{
							by1= new Biayainvestasi();
							Double biaya_topup = new Double(0);
							biaya_topup = new Double(kekurangan - biaya_berkala);
							String biaya_topup1=null;
							BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
							biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
							idx=new Integer(idx.intValue()+1);
							by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
							by1.setMbu_persen(new Double(0));						
							by1.setLjb_biaya("KEKURANGAN PREMI");
							by1.setLjb_id(new Integer(99));
							by1.setMu_ke(new Integer(3));
							by1.setFlag_jenis("Biaya Lain2");
							dtby.add(by1);
						}							
					}else{
						//belum top up
						by1= new Biayainvestasi();
						idx=new Integer(idx.intValue()+1);
						mbu_jumlah[idx.intValue()]=new Double(0 - ld_premi_invest.doubleValue());
						mbu_persen[idx.intValue()]=new Double(0);
						mbu_jenis[idx.intValue()]=new Integer(99);
						mbu_nama_jenis[idx.intValue()]="KEKURANGAN PREMI";	
						by1.setMbu_jumlah(mbu_jumlah[idx.intValue()]);
						by1.setMbu_persen((mbu_persen[idx.intValue()]));						
						by1.setLjb_biaya(mbu_nama_jenis[idx.intValue()]);
						by1.setLjb_id((mbu_jenis[idx.intValue()]));
						by1.setMu_ke(new Integer(2));
						by1.setFlag_jenis("Biaya Lain2");
						dtby.add(by1);
						edit.getInvestasiutama().setDaftarbiaya(dtby);
						edit.getInvestasiutama().setJmlh_biaya(idx);
					}
					edit.getInvestasiutama().setTotal_biaya_premi(new Double(0));
					total_mbu_jumlah=premi;
				}
					
				edit.getInvestasiutama().setDaftarbiaya(dtby);
				edit.getInvestasiutama().setJmlh_biaya(idx);
					
				BigDecimal mbu = new BigDecimal(total_mbu_jumlah.doubleValue());
				mbu = mbu.setScale(2,BigDecimal.ROUND_HALF_UP);
				total_mbu_jumlah=new Double(mbu.doubleValue());	
					
				ttl_mbu_jumlah=Double.toString(total_mbu_jumlah.doubleValue());	
				ld_premi_invest = new Double(premi.doubleValue()-total_mbu_jumlah.doubleValue()); 	
				edit.getInvestasiutama().setTotal_biaya_premi((ld_premi_invest));	
				
			}
			edit.getInvestasiutama().setDaftarbiaya(dtby);
			edit.getInvestasiutama().setJmlh_biaya(idx);
		//	if (flag_as.intValue() != 2)
		//	{
				Integer flag_topup = 0;
				if (pil_berkala.intValue()>0)
				{
					by1= new Biayainvestasi();
					Double biaya_topup = new Double(0);
					biaya_topup = new Double(premi_berkala.doubleValue() * produk.li_pct_biaya / 100);
					String biaya_topup1=null;
					BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
					biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
					idx=new Integer(idx.intValue()+1);
					if (flag_as.intValue() == 2)
					{
						by1.setMbu_jumlah(new Double(new Double(0)));
						by1.setMbu_persen(new Double(0));		
					}else{
						by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
						by1.setMbu_persen(new Double(produk.li_pct_biaya));	
						
					}
					by1.setLjb_biaya("BIAYA TOP-UP");
					by1.setLjb_id(new Integer(4));
					by1.setMu_ke(new Integer(2));
					by1.setFlag_jenis("Biaya TopUp");
					dtby.add(by1);
					flag_topup = 1;
				}
				if (pil_tunggal.intValue()>0)
				{
					by1= new Biayainvestasi();
					Double biaya_topup = new Double(0);
					biaya_topup = new Double(premi_tunggal.doubleValue() * produk.li_pct_biaya / 100);
					String biaya_topup1=null;
					BigDecimal bd5 = new BigDecimal(biaya_topup.doubleValue());
					biaya_topup1=bd5.setScale(2,BigDecimal.ROUND_HALF_UP).toString();
					idx=new Integer(idx.intValue()+1);
					if (flag_as.intValue() == 2)
					{
						by1.setMbu_jumlah(new Double(new Double(0)));
						by1.setMbu_persen(new Double(0));		
					}else{
						by1.setMbu_jumlah(new Double(Double.parseDouble(biaya_topup1)));
						by1.setMbu_persen(new Double(produk.li_pct_biaya));						
					}
					by1.setLjb_biaya("BIAYA TOP-UP");
					by1.setFlag_jenis("Biaya TopUp");
					by1.setLjb_id(new Integer(4));
					if (flag_topup == 0)
					{
						by1.setMu_ke(new Integer(2));
					}else{
						by1.setMu_ke(new Integer(3));
					}
					dtby.add(by1);
				}
			//}
				
			edit.getInvestasiutama().setJmlh_biaya((idx));
			edit.getInvestasiutama().setTotal_biaya_premi((ld_premi_invest));	
			edit.getInvestasiutama().setDaftarbiaya(dtby);

				
			m.put("ld_premi_invest",ld_premi_invest);
			m.put("flag_minus",new Boolean(flag_minus));
			m.put("jumlah_minus",jumlah_minus);
			
			List by2= edit.getInvestasiutama().getDaftarbiaya();
			if (kode_flag.intValue()>1 && kode_flag.intValue() != 11 && kode_flag.intValue() != 16)
			{
				edit.getInvestasiutama().setJmlh_biaya(new Integer(by2.size()));
			}
	
			return m;
	}	
	
	/**
	 * Jenis Invest per Kode Flag
	 * 
	 * @author Yusuf
	 * @since May 12, 2008 (9:43:07 AM)
	 * @param kode_flag
	 * @param lsbs
	 * @param lsdbs
	 * @return
	 */
	public Map getJenisInvestPerProduk(int kode_flag, int lsbs, int lsdbs) {
		Map lji = new HashMap();
		int kelipatan = 10; //default adalah kelipatan 10
		
		if(kode_flag == 2) { //buka fixed & dyna
			lji.put("01", null);
			lji.put("02", null);
		}else if(kode_flag == 3) { //buka fixed & dyna
			lji.put("03", null);
		}else if(kode_flag == 4) { //buka fixed & dyna & aggresive, kelipatan 10
			if((lsbs==120 && (lsdbs>=19 && lsdbs<=21)) || (lsbs==134 && lsdbs==9) || (lsbs==220 && lsdbs==3)){
				lji.put("35", null);
				lji.put("36", null);
				lji.put("37", null);
				lji.put("63", null);
			}else if((lsbs==120 && (lsdbs>=22 && lsdbs<=24)) || (lsbs==134 && (lsdbs==5 || lsdbs==13))){ //helpdesk [142003] produk baru Smart Platinum Link RPUL BEL (134-13)
				lji.put("01", null);
				lji.put("02", null);
				lji.put("03", null);
				lji.put("35", null);
				lji.put("36", null);
				lji.put("37", null);
				lji.put("61", null);
				lji.put("63", null);
			}else if ((lsbs==134 && lsdbs==11) || (lsbs==190 && (lsdbs>=5 && lsdbs<=6)) 
					|| (lsbs==140 && (lsdbs>=1 && lsdbs<=2)) 
					|| (lsbs==138 && lsdbs==4)){
				lji.put("01", null);
				lji.put("02", null);
				lji.put("03", null);
				lji.put("61", null);
				lji.put("63", null);
			}else if (lsbs==217  && lsdbs ==2) {
				lji.put("03", null);
			}
			//helpdesk 126900, Penambahan 3 Fund Simas Pada Produk UL Bank Bukopin dan Agency - Chandra - Start
			else if ((lsbs==190  && (lsdbs ==3 || lsdbs ==4)) || //smile link 99 single/reguler
					(lsbs==116 && (lsdbs==3 || lsdbs==4)) || //smile link 88 single/reguler
					(lsbs==118 && (lsdbs==3 || lsdbs==4)) || //smile link 88 single/reguler (AS)
					(lsbs==134 && lsdbs==6) || //smile link proasset
					(lsbs==217  && lsdbs ==1)) { //smile link pro 100
				lji.put("01", null);
				lji.put("02", null);
				lji.put("03", null);
				lji.put("35", null);
				lji.put("36", null);
				lji.put("37", null);
				lji.put("61", null);
				lji.put("63", null);
			}
			//helpdesk 126900, Penambahan 3 Fund Simas Pada Produk UL Bank Bukopin dan Agency - Chandra - End
			else if (lsbs==190  && (lsdbs ==1 || lsdbs ==2)) { //smile link bridge single/reguler
				lji.put("01", null);
				lji.put("02", null);
				lji.put("03", null);
				//helpdesk 126900, Penambahan 3 Fund Simas Pada Produk UL Bank Bukopin dan Agency - Chandra - Start
				lji.put("35", null);
				lji.put("36", null);
				lji.put("37", null);
				//helpdesk 126900, Penambahan 3 Fund Simas Pada Produk UL Bank Bukopin dan Agency - Chandra - End
				lji.put("63", null);
			}else if ((lsbs==220 && lsdbs==4) || (lsbs==134 && lsdbs==12)){//bukopin smile protection/insurance
				lji.put("01", null);
				lji.put("02", null);
				lji.put("03", null);
				//helpdesk 126900, Penambahan 3 Fund Simas Pada Produk UL Bank Bukopin dan Agency - Chandra - Start
				lji.put("35", null);
				lji.put("36", null);
				lji.put("37", null);
				//helpdesk 126900, Penambahan 3 Fund Simas Pada Produk UL Bank Bukopin dan Agency - Chandra - End
				lji.put("61", null);
				lji.put("63", null);
			}else if (lsbs==134 && lsdbs==10){ //helpdesk 125061, request untuk simas primelink hanya buka 1 fund - Chandra
				lji.put("37", null);
			}
			//helpdesk 125227, Penambahan 3 Fund Simas Pada Produk UL - Chandra - Start
			else if ((lsbs==190  && (lsdbs ==7 || lsdbs ==8))) {
				lji.put("01", null);
				lji.put("02", null);
				lji.put("03", null);
				lji.put("35", null);
				lji.put("36", null);
				lji.put("37", null);
			}else if ((lsbs==220 && (lsdbs==1 || lsdbs==2)) ||
				(lsbs==134 && (lsdbs==7 || lsdbs==8))) {
				lji.put("01", null);
				lji.put("02", null);
				lji.put("03", null);
				lji.put("35", null);
				lji.put("36", null);
				lji.put("37", null);
				lji.put("61", null);
				lji.put("63", null);
			}
			//helpdesk 125227, Penambahan 3 Fund Simas Pada Produk UL - Chandra - End
			else{
				lji.put("01", null);
				lji.put("02", null);
				lji.put("03", null);
			}
		}else if(kode_flag == 5) { //buka secured & dyna dollar, kelipatan 10
			lji.put("04", null);
			lji.put("05", null);
		}else if(kode_flag == 6) { //buka Syariah fixed & syariah dyna, kelipatan 10
			lji.put("06", null);
			lji.put("07", null);
		}else if(kode_flag == 7) { //excellink syariah - fixed, dina, aggre
			if((lsbs==215 && lsdbs==4)){ //helpdesk [133899], produk B Smile Insurance Syariah 215-4
				lji.put("06", null);
				lji.put("07", null);
				lji.put("08", null);
				lji.put("58", null);
				lji.put("59", null);
				lji.put("60", null);
				lji.put("62", null);
			}else if((lsbs==224 && lsdbs==3)){ //helpdesk [133899], produk B Smile Protection Syariah 224-3
				lji.put("06", null);
				lji.put("07", null);
				lji.put("08", null);
				lji.put("35", null);
				lji.put("36", null);
				lji.put("37", null);
				lji.put("62", null);
			}else{
				lji.put("06", null);
				lji.put("07", null);
				lji.put("08", null);
				lji.put("62", null);
			}
		}else if(kode_flag == 8) { //excellink syariah dollar - secure, dina
			lji.put("09", null);
			lji.put("10", null);
		}else if(kode_flag == 9) { //arthalink, ekalink88, ekalink88+ - fixed, dina, aggre
			if(lsbs == 162 && lsdbs <= 4){ //arthalink
				lji.put("11", null);
				lji.put("12", null);
				lji.put("13", null);
			}else if(lsbs == 162 && (lsdbs == 5 || lsdbs == 6)){ //ekalink 88
				lji.put("16", null);
				lji.put("17", null);
				lji.put("18", null);
			}else if(lsbs == 162 && (lsdbs == 7 || lsdbs == 8)){ //ekalink 88+
				lji.put("24", null);
				lji.put("25", null);
				lji.put("26", null);
			}
		}else if(kode_flag == 10) { //arthalink, ekalink88, ekalink88+ dollar - secure, dina
			if(lsbs == 162 && lsdbs <= 4){ //arthalink
				lji.put("14", null);
				lji.put("15", null);
			}else if(lsbs == 162 && (lsdbs == 5 || lsdbs == 6)){ //ekalink 88
				lji.put("19", null);
				lji.put("20", null);
			}else if(lsbs == 162 && (lsdbs == 7 || lsdbs == 8)){ //ekalink 88+
				lji.put("27", null);
				lji.put("28", null);
			}
		}else if(kode_flag == 11) { // stabil link rupiah & dollar
			lji.put("22", null);
			lji.put("23", null);
		}else if(kode_flag == 12) {
			if(lsbs == 165 && lsdbs == 1){ //investimax 1
				lji.put("21", null);
			}else if(lsbs == 165 && lsdbs == 2){ //investimax 2
				lji.put("29", null);
			}
		}else if(kode_flag == 13) { //amanah link syariah
			lji.put("07", null);
			lji.put("08", null);
		}else if(kode_flag == 14) { //muamalat - mabrur (153-5)
			lji.put("07", null);
		}else if(kode_flag == 15) { //stable link syariah rupiah & dollar
			lji.put("30", null);
			lji.put("31", null);
		}else if(kode_flag == 16) { // stabil link 2 rupiah
			lji.put("34", null);
		}else if(kode_flag == 17) { // simas magna rupiah
			lji.put("42", null);
			lji.put("43", null);
			lji.put("44", null);
			lji.put("47", null);
			lji.put("48", null);
			lji.put("49", null);
		}else if(kode_flag == 18) { // simas magna dollar
			if((lsbs==134 && lsdbs==9) || (lsbs==220 && lsdbs==3)){
				lji.put("45", null);
				lji.put("46", null);
			}else{
				lji.put("45", null);
				lji.put("46", null);
				lji.put("56", null);
				lji.put("57", null);
			}
		}else if(kode_flag == 19) { // simas magna rupiah syariah
			lji.put("50", null);
			lji.put("51", null);
			lji.put("52", null);
			lji.put("53", null);
			lji.put("54", null);
			lji.put("55", null);
		}else if(kode_flag == 20) { // excellink simas rupiah syariah
			lji.put("06", null);
			lji.put("07", null);
			lji.put("08", null);
			lji.put("58", null);
			lji.put("59", null);
			lji.put("60", null);
		}
		lji.put("kelipatan", kelipatan);
		return lji;
	}
	
	public Map proseshitungfund(Object cmd,Errors err, Double li_pct_biaya, Integer kode_flag , Double ld_premi_invest , Boolean flag_minus , Double premi_berkala, Double premi_tunggal , Double jumlah_minus) throws ServletException,IOException
	{
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Map m = new HashMap();
		Integer flag_ekalink = edit.getDatausulan().getFlag_ekalink();
		//daftar fund
		List invvl = edit.getInvestasiutama().getDaftarinvestasi();
	
		//fund
		try {
			/* VALIDASI FUND BARU, oleh Yusuf */
			
			int total = 0;
			int lsbs = edit.getDatausulan().getLsbs_id().intValue();
			int lsdbs = edit.getDatausulan().getLsdbs_number().intValue();	
			Map lji = getJenisInvestPerProduk(kode_flag, lsbs, lsdbs);
			int kelipatan = (Integer) lji.get("kelipatan");
			
			if(products.unitLink(Integer.toString(lsbs))){
				//validasi fund 1 = kelipatan
				for (int k=0; k<invvl.size();k++){
					DetilInvestasi iv= (DetilInvestasi)invvl.get(k);
					if(lji.containsKey(iv.getLji_id1())) {
						//validasi untuk paket dilock pada Exellink dynamic income
						if(("129".indexOf(edit.getDatausulan().getLsbs_id().toString())>-1 && edit.getDatausulan().getFlag_paket()!=0) ||
						    "24,25,26".indexOf(edit.getDatausulan().getFlag_paket().toString())>-1	){
							if(iv.getMdu_persen1() == null) iv.setMdu_persen1(0);
							if((iv.getLji_id1().equals("01")&&iv.getMdu_persen1()!=0) || (iv.getLji_id1().equals("03")&&iv.getMdu_persen1()!=0) ){
								err.rejectValue("investasiutama.daftarinvestasi["+k+"].mdu_persen1","","HALAMAN INVESTASI : Untuk Paket hanya boleh memilih Excellink Dynamic Fund Income sebesar 100%");
							}
						}
						if(iv.getMdu_persen1() == null) iv.setMdu_persen1(0);
						if(iv.getMdu_persen1() % kelipatan != 0) {
							err.rejectValue("investasiutama.daftarinvestasi["+k+"].mdu_persen1","","HALAMAN INVESTASI : Harap masukkan investasi dalam kelipatan " + kelipatan);
						}
						total += iv.getMdu_persen1();
					}
				}
				//validasi fund 2 = total 100%
				if(total != 100) err.rejectValue("investasiutama.total_persen", "", "HALAMAN INVESTASI : Jumlah persentase harus 100%");
				
				/* End of VALIDASI FUND BARU, oleh Yusuf */
	
				proc_fund(edit, kode_flag,flag_minus,ld_premi_invest,premi_berkala, premi_tunggal,jumlah_minus,li_pct_biaya);
			}
		} catch (ServletException e) {
			logger.error("ERROR :", e);
		} catch (IOException e) {
			logger.error("ERROR :", e);
		}
		return m;
	}
	
	/**
	 * Bedanya dengan proseshitungfund, yang ini skip validation
	 * 
	 * @author Yusuf
	 * @since May 12, 2008 (8:13:51 AM)
	 * @param cmd
	 * @param err
	 * @param li_pct_biaya
	 * @param kode_flag
	 * @param ld_premi_invest
	 * @param flag_minus
	 * @param premi_berkala
	 * @param premi_tunggal
	 * @param jumlah_minus
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public Map proseshitungfund1(Object cmd,Errors err, Double li_pct_biaya, Integer kode_flag , Double ld_premi_invest , Boolean flag_minus , Double premi_berkala,Double premi_tunggal , Double jumlah_minus)throws ServletException,IOException
	{
		Map m =null;
		m = new HashMap();
		Cmdeditbac edit= (Cmdeditbac) cmd;
		try {
			proc_fund(edit, kode_flag,flag_minus,ld_premi_invest,premi_berkala,premi_tunggal,jumlah_minus,li_pct_biaya);
		} catch (ServletException e) {
			logger.error("ERROR :", e);
		} catch (IOException e) {
			logger.error("ERROR :", e);
		}
		return m;
	}	
	
	/**
	 * proc_fund ini, menghitung alokasi yang diinvestasikan dari premi pokok dan topup, sesuai 
	 * dengan biaya yang dikenakan, dan jenis investasinya masing-masing
	 * 
	 * @author Yusuf
	 * @since May 12, 2008 (11:22:33 AM)
	 * @param edit
	 * @param kode_flag
	 * @param flag_minus
	 * @param ld_premi_invest
	 * @param premi_berkala
	 * @param premi_tunggal
	 * @param jumlah_minus
	 * @param li_pct_biaya
	 * @throws ServletException
	 * @throws IOException
	 */
	private void proc_fund(Cmdeditbac edit, Integer kode_flag,Boolean flag_minus, Double ld_premi_invest, Double premi_berkala, 
			Double premi_tunggal, Double jumlah_minus, Double li_pct_biaya)throws ServletException,IOException{
		//	fund
		List invvl = edit.getInvestasiutama().getDaftarinvestasi();

		//logger.info("================ INFORMASI2 PARAMETER ================");
		//logger.info("KODE_FLAG 		= " + kode_flag);
		//logger.info("FLAG_MINUS 		= " + flag_minus);
		//logger.info("LD_PREMI_INVEST = " + ld_premi_invest);
		//logger.info("PREMI_BERKALA 	= " + premi_berkala);
		//logger.info("PREMI_TUNGGAL 	= " + premi_tunggal);
		//logger.info("JUMLAH_MINUS 	= " + jumlah_minus);
		//logger.info("LI_PCT_BIAYA 	= " + li_pct_biaya);
		
		//logger.info("================ SEBELUM PROC_FUND ================");
		//TestUtils.printDetailInvestasi(invvl);

		Map lji = getJenisInvestPerProduk(kode_flag, edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number());
		
		for(int i=0; i<invvl.size(); i++) {
			DetilInvestasi di = (DetilInvestasi) invvl.get(i);
			if(lji.containsKey(di.getLji_id1())) {
				if(di.getMdu_persen1() != null) {
					if(di.getMdu_persen1() > 0) {
						di.setMdu_jumlah1(
								ld_premi_invest * di.getMdu_persen1()/100); //premi pokok
						di.setMdu_jumlah_top(
								premi_tunggal 	* di.getMdu_persen1()/100 * (100-li_pct_biaya)/100); //premi topup tunggal
						di.setMdu_jumlah_top_tunggal(
								premi_berkala 	* di.getMdu_persen1()/100 * (100-li_pct_biaya)/100); //premi topup berkala
					}
				}
			}
		}
		
//		logger.info("================ SESUDAH PROC_FUND ================");
	}
	
	private Map proc_hitung_powersave(Cmdeditbac edit,Map m,Integer flag_powersave , String kurs , Double premi , Integer flag_bulanan,Errors err,Integer flag_account, String lca_id,Integer autodebet,Integer flag_bao1)throws ServletException,IOException
	{

		form_investasi a =new form_investasi();
		hit_biaya_powersave powersave = new hit_biaya_powersave();
		f_hit_umur n_date=  new f_hit_umur();
		Integer flag_as = edit.getDatausulan().getFlag_as();
		
		Integer jangka_investasi = new Integer(0);
		Double rate = new Double(0);
		Double bunga = new Double(0);
		String jns_top_up1="";
		String jns_top_up0=edit.getPowersave().getMps_jangka_inv();
		if (jns_top_up0==null)
		{
			jns_top_up0="";
			edit.getPowersave().setMps_jangka_inv(jns_top_up0);
		}
		edit.getPowersave().setMpr_nett_tax(new Integer(0));

		Integer jns_rate = edit.getPowersave().getMps_jenis_plan();
		
		if (jns_rate==null)
		{
			jns_rate=new Integer(0);
			edit.getPowersave().setMps_jenis_plan(new Integer(0));
		}
		
		//1 gross 2 =nett , STANDARD = 0
		// nett
//		if ( jns_rate.intValue() == 2)
//		{
//			edit.getPowersave().setMpr_nett_tax(new Integer(4));
//		}else if ( jns_rate.intValue() == 1)
//			{
//			//gross
//				edit.getPowersave().setMpr_nett_tax(new Integer(3));
//			}else if ( jns_rate.intValue() == 0)
//			{
//				//normal
//				edit.getPowersave().setMpr_nett_tax(new Integer(0));
//			}

		//Yusuf - request rudy h - net_tax isinya hanya 0 normal, 1 gross, 2 net saja (19/02/2008)
		edit.getPowersave().setMpr_nett_tax(jns_rate);
		
		String memo = edit.getPowersave().getMpr_note();
		if (memo==null)
		{
			memo="";
			edit.getPowersave().setMpr_note(memo);
		}
		
		String hasil_jns_top_up0=a.cek_jangkawaktu(jns_top_up0);
		if (hasil_jns_top_up0.trim().length()!=0)
		{
			m.put("mps_jangka_inv",hasil_jns_top_up0);
		}

		Date tglbunga=null;
		Date tgl_next=null;
		Integer rolloover = edit.getPowersave().getMps_roll_over();
		if (rolloover==null)
		{
			jns_top_up1="";
		}else{
			jns_top_up1=Integer.toString(rolloover.intValue());
		}
		String hasil_jns_top_up1=a.cek_rolloover(jns_top_up1);
		if (hasil_jns_top_up1.trim().length()!=0)
		{
			m.put("mps_roll_over",hasil_jns_top_up1);
		}else{
			if (edit.getDatausulan().getLsbs_id().intValue() == 158 || edit.getDatausulan().getLsbs_id().intValue() == 176 || edit.getPowersave().getFlag_bulanan().intValue() > 0){
				if (jns_top_up1.equalsIgnoreCase("1")){
					hasil_jns_top_up1 = "Untuk Produk Manfaat Bulanan, tidak boleh ROLLOVER NILAI TUNAI.";
					m.put("mps_roll_over",hasil_jns_top_up1);
				}
			}else if (products.stableSavePremiBulanan(edit.getDatausulan().getLsbs_id().toString())){ //stable save premi bulanan / cicilan
				if (jns_top_up1.equalsIgnoreCase("3")){
					hasil_jns_top_up1 = "Untuk Produk Stable Save (Premi Bulanan), tidak boleh ROLLOVER AUTOBREAK.";
					m.put("mps_roll_over",hasil_jns_top_up1);
				}
			}else if(products.stableSave(edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number()) && Integer.parseInt(jns_top_up0)<36 && rolloover==3){
				hasil_jns_top_up1 = "Untuk Produk Stable Save dengan MGI <36, tidak boleh ROLLOVER AUTOBREAK.";
				m.put("mps_roll_over",hasil_jns_top_up1);
			}
		}
		
		//APABILA BANK SINARMAS
//		if (edit.getPemegang().getJn_bank().intValue() == 2)
//		{
//			if (rolloover != null)
//			{
//				if (rolloover.intValue() == 2 )
//				{
//					hasil_jns_top_up1 = "Tidak boleh ROLLOVER PREMI.";
//					m.put("mps_roll_over",hasil_jns_top_up1);
//				}
//			}
//				if (jns_rate.intValue() != 0)
//			{
//				hasil_jns_top_up0="Tidak boleh bunga special";
//				m.put("mps_jangka_inv",hasil_jns_top_up0);
//			}
//		}
		Integer jumlah_hari_mgi = 0;
		//tgl beg date
		Integer tanggal1= new Integer(edit.getDatausulan().getMste_beg_date().getDate());
		Integer bulan1 = new Integer((edit.getDatausulan().getMste_beg_date().getMonth())+1);
		Integer tahun1 = new Integer((edit.getDatausulan().getMste_beg_date().getYear())+1900);
		String tgl_beg_date1 = FormatString.rpad("0",Integer.toString(tanggal1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan1.intValue()),2)+"/"+Integer.toString(tahun1.intValue());
		
		
		// PILIH JANGKA WAKTU
		if (!jns_top_up0.trim().equalsIgnoreCase(""))
		{			

			jangka_investasi = new Integer(powersave.hit_jangka_invest(jns_top_up0));

			try {						
				//tanggal next (tanggal beg date polis + jangka waktu bunga)
				tgl_next = (n_date.f_add_months(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),Integer.parseInt(jns_top_up0)));
				Date Tgl_next;
				
				Tgl_next = defaultDateFormat.parse(Integer.toString(tgl_next.getDate())+"/"+(tgl_next.getMonth()+1)+"/"+(tgl_next.getYear()+1900));
				if (bulan1.intValue() == 2 || tanggal1.intValue() == 30) 
				 {
					if  ((tgl_next.getMonth()+1) != 2) 
					{
					   Tgl_next = defaultDateFormat.parse(Integer.toString(tanggal1.intValue())+"/"+(tgl_next.getMonth()+1)+"/"+(tgl_next.getYear()+1900));
					}
				  }	
				Date tanggal_mature=null;
				//STABLE LINK (YUSUF 28/04/08)
				if (products.stableLink(edit.getDatausulan().getLsbs_id().toString()) || products.powerSave(edit.getDatausulan().getLsbs_id().toString()) || products.stableSave(edit.getDatausulan().getLsbs_id(), edit.getDatausulan().getLsdbs_number())){
					Date begdate_topup = null;
					Date tgl_next_topup = null;
					 begdate_topup = edit.getPowersave().getBegdate_topup();
					 if (begdate_topup == null)
					 {
						 begdate_topup = edit.getDatausulan().getMste_beg_date();
					 }
					 
					//Yusuf (31 Aug 2009) - Bagian dari proses Konversi Psave ke Slink
					if(edit.getDatausulan().isPsave || !Common.isEmpty(edit.getPowersave().getMsl_spaj_lama()) || edit.getDatausulan().isPlatinumSave){
						begdate_topup = edit.getPowersave().getBegdateTerbaru();
						if(begdate_topup==null){
							begdate_topup = bacDao.selectPowerSaveRoSurrender(edit.getPowersave().getMsl_spaj_lama());
						}
						tgl_beg_date1 = defaultDateFormat.format(begdate_topup);
						
					}
					 
					Integer tanggal_1= new Integer(begdate_topup.getDate());
					Integer bulan_1 = new Integer((begdate_topup.getMonth())+1);
					Integer tahun_1 = new Integer((begdate_topup.getYear())+1900);
					
					Date Tgl_next_topup=null;
					
					if(!Common.isEmpty(edit.getPowersave().getMsl_spaj_lama())){
						tgl_next = ( n_date.f_add_months(tahun_1.intValue(),bulan_1.intValue(),tanggal_1.intValue(),0));
						Tgl_next = defaultDateFormat.parse(Integer.toString(tgl_next.getDate())+"/"+(tgl_next.getMonth()+1)+"/"+(tgl_next.getYear()+1900));
						if (bulan_1.intValue() == 2 || tanggal_1.intValue() == 30) 
						 {
							if  ((tgl_next.getMonth()+1) != 2) 
							{
							   Tgl_next = defaultDateFormat.parse(Integer.toString(tanggal_1.intValue())+"/"+(tgl_next.getMonth()+1)+"/"+(tgl_next.getYear()+1900));
							}
						  }	
					}	
					 tgl_next_topup = (n_date.f_add_months(tahun_1.intValue(),bulan_1.intValue(),tanggal_1.intValue(),Integer.parseInt(jns_top_up0)));
					 if(edit.getDatausulan().getMssur_se()!=null){
						 if(edit.getDatausulan().getMssur_se()==3){
							 tanggal_mature = bacDao.selectMprMatureDate(edit.getPowersave().getMsl_spaj_lama());
							 if(begdate_topup.after(tanggal_mature)){
								 tgl_next_topup= tgl_next_topup;
							 }else{
								 tgl_next_topup = tanggal_mature;
							 }
						 }
					 }
					 
						
						Tgl_next_topup = defaultDateFormat.parse(Integer.toString(tgl_next_topup.getDate())+"/"+(tgl_next_topup.getMonth()+1)+"/"+(tgl_next_topup.getYear()+1900));
						if (bulan_1.intValue() == 2 || tanggal_1.intValue() == 30) 
						 {
							if  ((tgl_next_topup.getMonth()+1) != 2) 
							{
							   Tgl_next_topup = defaultDateFormat.parse(Integer.toString(tanggal_1.intValue())+"/"+(tgl_next_topup.getMonth()+1)+"/"+(tgl_next_topup.getYear()+1900));
							}
						  }	
						 edit.getPowersave().setBegdate_topup(begdate_topup);
						 edit.getPowersave().setEnddate_topup(Tgl_next_topup);
						 edit.getPowersave().setEnddate_topup1(Tgl_next_topup);
					
					//set bunga powersave TU

					double premi_tu_stable = 0;
					if(edit.getInvestasiutama().getDaftartopup().getPremi_tunggal() != null) premi_tu_stable = edit.getInvestasiutama().getDaftartopup().getPremi_tunggal();
					double premiStable = edit.getDatausulan().getMspr_premium() + premi_tu_stable;
					     
					Map d =  this.bacDao.selectbungaprosave(kurs,Integer.toString(jangka_investasi.intValue()), premiStable, begdate_topup, flag_powersave, 0);
					if (d!=null || edit.getPowersave().getMps_jenis_plan().intValue() == 2) {
						//Integer selisih_hari =new Integer( n_date.hari1(2005,12,20,tahun1.intValue(),bulan1.intValue(),tanggal1.intValue()));
						if(edit.getPowersave().getMps_jenis_plan().intValue() == 0) {
						    edit.getPowersave().setMps_rate(new Double(Double.parseDouble(d.get("LPR_RATE").toString())));
							edit.getPowersave().setMpr_note("");
						}
						rate = edit.getPowersave().getMps_rate();
						jumlah_hari_mgi =new Integer( n_date.hari1(tahun_1.intValue(),bulan_1.intValue(),tanggal_1.intValue(),Tgl_next_topup.getYear()+1900,Tgl_next_topup.getMonth()+1,Tgl_next_topup.getDate()));
						if(edit.getDatausulan().getMssur_se()!=null){
							if(edit.getDatausulan().getMssur_se()==3){
								jumlah_hari_mgi =new Integer( n_date.hari1(tahun_1.intValue(),bulan_1.intValue(),tanggal_1.intValue(),tgl_next_topup.getYear()+1900,tgl_next_topup.getMonth()+1,tgl_next_topup.getDate()));
								jumlah_hari_mgi = jumlah_hari_mgi.intValue()+1;
							}
						}
						
						if(edit.getInvestasiutama().getDaftartopup().getPremi_tunggal()!=null){
							if (jns_rate == 0)
							{
								if (lca_id.equals("01"))		
								{
									//(12 May 2011) Rudi : untuk flag mste_flag_el sekarang ada 3. 0 : non karyawan, 1:karyawan(+1%), 2:keluarga/kerabat karyawan(+0.5%)
									//(21 Feb 2012) Aktuari(Pak Herman) by phone dgn RUdy : 0 : non karyawan, 1& 2 : karyawan dan keluarga (+1%), 3 : teman karyawan (+0.5%)
									//(25 Sep 2012) Deddy : REQ FROM IWEN(ACC FROM INDRA BY PHONE) untuk karyawan dan keluarga(semua yg +1%) dirubah jadi 0.5% semua.
									//(11 mar 2013) Deddy : berdasarkan SE NO. 096/AJS-SE/XII/2012, ditambahkan rate untuk Agent berlisensi
									if("1,2,3,4".indexOf(edit.getDatausulan().getMste_flag_el().toString())>-1){
										rate = new Double((0.5)+rate.doubleValue() );
									}
									
									edit.getPowersave().setMps_rate(rate);
								}
							}
							edit.getPowersave().setMps_prm_interest_tu(
									proc_hit_rate(flag_bulanan,jns_top_up0, edit.getInvestasiutama().getDaftartopup().getPremi_tunggal(), rate,jumlah_hari_mgi,flag_powersave, 0)
								);  
						}
						
					}
					
				}
				if(edit.getDatausulan().getMssur_se()!=null || edit.getDatausulan().isSlink==true){
					if(edit.getDatausulan().getMssur_se()==2 || edit.getDatausulan().getMssur_se()==3 || edit.getDatausulan().isSlink==true){
						 Date enddate = null; 
						 edit.getPowersave().setMps_prm_deposit(premi);
						 edit.getPowersave().setMps_deposit_date(Tgl_next);
						 enddate = bacDao.selectEndDatePolisEndors(edit.getPowersave().getMsl_spaj_lama());
						 Integer tanggal_end_1= new Integer(enddate.getDate());
						 Integer bulan_end_1 = new Integer(edit.getPowersave().getEnddate_topup().getMonth()+1);
						 Integer tahun_end_1 = new Integer(edit.getPowersave().getEnddate_topup().getYear()+1900);
						 
						 enddate = defaultDateFormat.parse(Integer.toString(tanggal_end_1.intValue())+"/"+(bulan_end_1)+"/"+(tahun_end_1));
						 
						 if(edit.getDatausulan().getMssur_se()==3 && edit.getDatausulan().getLsbs_id().intValue()!=158 && uwDao.selectLsbsId(edit.getPowersave().getMsl_spaj_lama()).equals("158")){
							 if(tgl_next.getDate()==1){
								 bulan_end_1 = new Integer(tgl_next.getMonth());
								 if(bulan_end_1<1){
									 bulan_end_1=1;
								 }
							 }else bulan_end_1 = new Integer(tgl_next.getMonth()+1);
							 tahun_end_1 = new Integer(tgl_next.getYear()+1900);
							 enddate = (n_date.f_add_months(    tahun_end_1.intValue(),bulan_end_1.intValue(),tanggal_end_1.intValue(),Integer.parseInt(edit.getPowersave().getMps_jangka_inv())));
						 }
						 
						 edit.getPowersave().setMpr_mature_date(enddate);
						 edit.getPowersave().setMps_batas_date(FormatDate.add(edit.getPowersave().getMpr_mature_date(),Calendar.DAY_OF_MONTH,1));
					}
				}else{
					edit.getPowersave().setMps_batas_date(Tgl_next);
					 edit.getPowersave().setMps_prm_deposit(premi);
					 edit.getPowersave().setMps_deposit_date(edit.getDatausulan().getMste_beg_date());
					 
				}
				

				 

//				tanggal next bulanan
				 Date tgl_deposit =null;
				 Date tgl_mature = null;
				 Integer jumlah_hari_mgi_bulanan = null;
					if (flag_bulanan.intValue() > 0)
					{
						tgl_deposit =edit.getPowersave().getMps_deposit_date();
						tgl_mature = FormatDate.add(tgl_deposit,Calendar.MONTH,flag_bulanan);
						tgl_mature = FormatDate.add(tgl_mature,Calendar.DAY_OF_MONTH,-1);
						 jumlah_hari_mgi_bulanan =new Integer( n_date.hari1(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),tgl_mature.getYear()+1900,tgl_mature.getMonth()+1,tgl_mature.getDate()));
						 jumlah_hari_mgi_bulanan = new Integer( jumlah_hari_mgi_bulanan.intValue() );
					}

					
				Integer flag_pwr=new Integer(0);
				//RETRIEVE BUNGA POWERSAVE DARI TABEL
			 
				//tentuin flag powersave untuk mencari bunga di tabel eka.lst_pwrsave_rate
				 //normal
				if (jns_rate.intValue() == 0)
				{
					Map data =null;
					Map bundle=null;				
					Integer flag_bundle=0;			
					flag_pwr = flag_powersave;
					edit.getPowersave().setMpr_nett_tax(0);
						
					double premi_tu_stable = 0;
					if(edit.getInvestasiutama().getDaftartopup().getPremi_tunggal() != null) premi_tu_stable = edit.getInvestasiutama().getDaftartopup().getPremi_tunggal();
					double premiStable = edit.getDatausulan().getMspr_premium() + premi_tu_stable;
					
					//proses spaj bundle PS+LINK
					if(edit.getPemegang().getMspo_spaj_bundle()!=null && !edit.getPemegang().getMspo_spaj_bundle().equalsIgnoreCase("")){
						bundle=this.bacDao.selectDataSpajBundle(edit.getPemegang().getMspo_spaj_bundle(),edit.getPemegang().getMcl_first(),edit.getPemegang().getMspe_date_birth());					
							if(bundle!=null && products.powerSave(edit.getDatausulan().getLsbs_id().toString())){
								Date beg_date = new Date();
								Date satudes = (new GregorianCalendar(2013,10,30)).getTime(); 
								//Date  limabelasdes = (new GregorianCalendar(2013,11,31)).getTime();
								DateFormat df = new SimpleDateFormat("dd/MM/yyyy");								
								beg_date= df.parse(df.format(edit.getDatausulan().getMste_beg_date()));
								Integer lssa_id=null;						
								Double premi_bundle=null;
								Double total_premi_bundle=null;
								Double total_premi_bundle_tahunan=null;
								Integer flag_ulink=null;
								Integer flag_persen=1;
								String mspo_spaj_bundle=edit.getPemegang().getMspo_spaj_bundle().toString().trim().replace(".", "");
								edit.getPemegang().setMspo_spaj_bundle(mspo_spaj_bundle);
								lssa_id=Integer.parseInt(bundle.get("STATUS").toString());
								premi_bundle=Double.parseDouble(bundle.get("PREMI_POKOK").toString());
								total_premi_bundle=Double.parseDouble(bundle.get("TOTAL_PREMI").toString());
								total_premi_bundle_tahunan=Double.parseDouble(bundle.get("TOTAL_PREMI_TAHUNAN").toString());
								flag_ulink=Integer.parseInt(bundle.get("FLAG_ULINK").toString());
								Integer flag_boleh=Integer.parseInt(bundle.get("FLAG_BOLEH").toString());
								String bisnis=bundle.get("LSBS_ID").toString();
								String sub_bisnis=bundle.get("LSDBS_NUMBER").toString();
								if(bisnis.equals("120") && "22,23,24".indexOf(sub_bisnis)>0){
									Double persen=(premi_bundle/(total_premi_bundle/100));
									if(persen!=50){
										flag_persen=0;
									}
									
								}	
								 if(flag_ulink==1 && edit.getDatausulan().getMspr_premium()>=100000000 && lssa_id==5 &&beg_date.after(satudes)&&
								    Integer.parseInt(jns_top_up0)==1 && total_premi_bundle_tahunan>=12000000 && flag_boleh<=0 && flag_persen==1
									){
								    	flag_bundle=1;
								 }else{
									 m.put("mps_jangka_inv","Untuk SPAJ ini Belum Bisa Mengambil Paket PS+LINK dengan Rate 7.75 %. Periksa Kembali Data Pemegang Polis.");
								 }
							}else{
								 m.put("mps_jangka_inv","Untuk SPAJ ini Belum Bisa Mengambil Paket PS+LINK dengan Rate 7.75 %. Periksa Kembali Data Pemegang Polis.");
							}
					}
					//ambil bunga dan tanggal bunga dari tabel
					//Integer nett_tax = edit.getPowersave().getMpr_nett_tax();
					if(flag_pwr >= 12 && flag_pwr <= 16) {
						data =  this.bacDao.selectbungaprosave(kurs,Integer.toString(jangka_investasi.intValue()),premiStable,defaultDateFormat.parse(tgl_beg_date1),flag_pwr, 0);
					}else {
						int flag_breakable = 0;
						if(edit.getPowersave().getMpr_breakable() != null) flag_breakable = edit.getPowersave().getMpr_breakable();  
						data =  this.bacDao.selectbungaprosave(kurs,Integer.toString(jangka_investasi.intValue()),premi,defaultDateFormat.parse(tgl_beg_date1),flag_pwr, flag_breakable);
					}
					if (data!=null || flag_bundle==1)
					{		
						//Integer selisih_hari =new Integer( n_date.hari1(2005,12,20,tahun1.intValue(),bulan1.intValue(),tanggal1.intValue()));					
					   
					    edit.getPowersave().setMpr_note("");	
					    if(flag_bundle==1){					    	
					    	rate=7.75;
					    	edit.getPowersave().setMps_rate(rate);
					    	edit.getPowersave().setMpr_jangka_invest("1");
					    }else{
					    	edit.getPowersave().setMps_rate(new Double(Double.parseDouble(data.get("LPR_RATE").toString())));
					    	rate = new Double((Double.parseDouble(data.get("LPR_RATE").toString())));
					    }	
						//Penambahan bunga untuk Power Save karyawan adalah sebesar 1 % p.a dari bunga yang berlaku (dipublikasikan oleh bagian investasi ).
						//Yusuf - sejak 16 mei 2009, powersave karyawan nambahnya hanya 0.5%
						//Deddy - 3 des 2010, rate untuk karyawan + 1%
						//Deddy - 3 des 2010, rate untuk nonkaryawan & lca_id 01 + 0.5%
						if (jns_rate == 0)
						{
							if (lca_id.equals("01"))		
							{
								//(12 May 2011) Rudi : untuk flag mste_flag_el sekarang ada 3. 0 : non karyawan, 1:karyawan(+1%), 2:keluarga/kerabat karyawan(+0.5%)
								//(21 Feb 2012) Aktuari(Pak Herman) by phone dgn RUdy : 0 : non karyawan, 1& 2 : karyawan dan keluarga (+1%), 3 : teman karyawan (+0.5%)
								//(25 Sep 2012) Deddy : REQ FROM IWEN(ACC FROM INDRA BY PHONE) untuk karyawan dan keluarga(semua yg +1%) dirubah jadi 0.5% semua.
								//(11 mar 2013) Deddy : berdasarkan SE NO. 096/AJS-SE/XII/2012, ditambahkan rate untuk Agent berlisensi
								if("1,2,3,4".indexOf(edit.getDatausulan().getMste_flag_el().toString())>-1){
									rate = new Double((0.5)+rate.doubleValue() );
								}
								
								edit.getPowersave().setMps_rate(rate);
							}
						}
						
						//BEDA CARA PERHITUNGAN DI TANGGAL 20/12/2005 SESUDAH DAN SEBELUM
						/*  if (selisih_hari.intValue() >=0)
						{
						  bunga= new Double(up.doubleValue() * (Math.pow((1 + (rate.doubleValue() / 100)) , (Double.parseDouble(jns_top_up0) /12))-1));
						 }else{
						 bunga=new Double((((Double.parseDouble(jns_top_up0) /12) * up.doubleValue()) *  rate.doubleValue() ) / 100);
						 }*/
						 if(flag_bundle==1){
							 tglbunga=edit.getDatausulan().getMste_beg_date();
						 }else{
							 tglbunga=(Date)data.get("LPR_BEGDATE");
							 
						 }
						 edit.getPowersave().setMps_rate_date(tglbunga);
						// edit.getPowersave().setMps_prm_interest(bunga);
						 
						jumlah_hari_mgi =new Integer( n_date.hari1(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),Tgl_next.getYear()+1900,Tgl_next.getMonth()+1,Tgl_next.getDate()));
						if(!Common.isEmpty(edit.getPowersave().getMsl_spaj_lama())){
							Integer tanggal_edate = new Integer(edit.getPowersave().getEnddate_topup().getDate());
							Integer bulan_edate = new Integer((edit.getPowersave().getEnddate_topup().getMonth())+1);
							Integer tahun_edate = new Integer((edit.getPowersave().getEnddate_topup().getYear())+1900);
							String tgl_edate = FormatString.rpad("0",Integer.toString(tanggal1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan1.intValue()),2)+"/"+Integer.toString(tahun1.intValue());
							Date tgl_next_topup = null;
							 tgl_next_topup = (n_date.f_add_months(tahun_edate.intValue(),bulan_edate.intValue(),tanggal_edate.intValue(),Integer.parseInt(jns_top_up0)));
							 if(edit.getDatausulan().getMssur_se()!=null){
								 if(edit.getDatausulan().getMssur_se()==3){
									 tgl_next_topup = bacDao.selectMprMatureDate(edit.getPowersave().getMsl_spaj_lama());
								 }
							 }
							 Date Tgl_next_topup=null;
								
								Tgl_next_topup = defaultDateFormat.parse(Integer.toString(tgl_next_topup.getDate())+"/"+(tgl_next_topup.getMonth()+1)+"/"+(tgl_next_topup.getYear()+1900));
								if (bulan_edate.intValue() == 2 || tanggal_edate.intValue() == 30) 
								 {
									if  ((tgl_next_topup.getMonth()+1) != 2) 
									{
									   Tgl_next_topup = defaultDateFormat.parse(Integer.toString(tanggal_edate.intValue())+"/"+(tgl_next_topup.getMonth()+1)+"/"+(tgl_next_topup.getYear()+1900));
									}
								  }	
							
							edit.getPowersave().setEnddate_topup(Tgl_next_topup);  
							if(edit.getDatausulan().getMssur_se()!=null){
								 if(edit.getDatausulan().getMssur_se()==3){
									 tanggal_edate = new Integer(edit.getPowersave().getEnddate_topup().getDate());
								 }
							}
							jumlah_hari_mgi = new Integer( n_date.hari1(Tgl_next.getYear()+1900,Tgl_next.getMonth()+1,Tgl_next.getDate(),tahun_edate.intValue(),bulan_edate.intValue(),tanggal_edate.intValue()));
						
							if(edit.getDatausulan().getMssur_se()!=null){
								if(edit.getDatausulan().getMssur_se()==3){
									//jumlah_hari_mgi = new Integer( n_date.hari1(Tgl_next.getYear()+1900,Tgl_next.getMonth()+1,Tgl_next.getDate(),tgl_next_topup.getYear()+1900,tgl_next_topup.getMonth()+1,tgl_next_topup.getDate()));
									jumlah_hari_mgi = new Integer( n_date.hari1(edit.getPowersave().getMps_deposit_date().getYear()+1900,edit.getPowersave().getMps_deposit_date().getMonth()+1,edit.getPowersave().getMps_deposit_date().getDate(),edit.getPowersave().getMpr_mature_date().getYear()+1900,edit.getPowersave().getMpr_mature_date().getMonth()+1,edit.getPowersave().getMpr_mature_date().getDate()));
									jumlah_hari_mgi = new Integer( jumlah_hari_mgi.intValue()+1);
								}
							}
						}
						jumlah_hari_mgi = new Integer( jumlah_hari_mgi.intValue() );
						

						//helpdesk [133346] produk baru 142-13 Smart Investment Protection
						Integer lsbs_id = edit.getDatausulan().getLsbs_id();
						Integer lsdbs_number = edit.getDatausulan().getLsdbs_number();
						if(lsbs_id.equals(142) && lsdbs_number.equals(13)){
							bunga = proc_hit_rate_6_bulan(premi, rate);
						} else {
							bunga = proc_hit_rate(flag_bulanan,jns_top_up0, premi, rate,jumlah_hari_mgi,flag_powersave,jumlah_hari_mgi_bulanan);
						}
						//bunga = proc_hit_rate(flag_bulanan,jns_top_up0, premi, rate,jumlah_hari_mgi,flag_powersave,jumlah_hari_mgi_bulanan);
						edit.getPowersave().setMps_prm_interest(bunga);
		
					}else{
						m.put("mps_jangka_inv","Bunga untuk tanggal ["+tgl_beg_date1+"] belum terdaftar. Silahkan konfirmasi dengan bagian INVESTMENT.");
						
						//Yusuf (18 Aug 2011) Apabila Bank Sinarmas, untuk bbrp produk, ada warning rate untuk pak sandy apabila belum diinput
						//142, 158, 164, 186 + 175
						try {
							if (edit.getPemegang().getJn_bank().intValue() == 2){
								
								int lsbs_id = edit.getDatausulan().getLsbs_id();
								if(lsbs_id == 142 || lsbs_id == 158 || lsbs_id == 164 || lsbs_id == 175 || lsbs_id == 186){
									int lsdbs_number = edit.getDatausulan().getLsdbs_number();
									int fbul = edit.getPowersave().getFlag_bulanan();
									if(lsbs_id == 158) fbul = 1;
									DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
									String begdate = df.format(edit.getDatausulan().getMste_beg_date());
									String lku_id = edit.getDatausulan().getLku_id();
									int mgi = Integer.parseInt(edit.getPowersave().getMps_jangka_inv());
									
										int tot = bacDao.selectCountRateBankSinarmas(lsbs_id, lsdbs_number, fbul, begdate, lku_id, mgi);
										
										//APABILA KOSONG, EMAIL!
										if(tot == 0){
												this.email.send(false, props.getProperty("admin.ajsjava"),
														new String[] {"investment@sinarmasmsiglife.co.id"}, 
														null, 
														new String[] {"ryan@sinarmasmsiglife.co.id","antasari@sinarmasmsiglife.co.id"},
														"[System Warning] Rate Bank Sinarmas (Simas Prima / Simas Stabil Link / SSP Pro)", 
														"Kepada Yth.\n"+
														"Bagian Investment di tempat.\n\n"+
														"Mohon bantuannya untuk melakukan proses upload rate produk Bank Sinarmas sesuai informasi dibawah ini, karena ada Inputan SPAJ di Cabang Bank Sinarmas.\n"+
														"- Flag Produk: " + flag_powersave + "\n"+
														"- Produk: " + (lsbs_id == 142 ? "Simas Prima" : lsbs_id == 175 ? "Simas Prima Syariah" : lsbs_id == 158 ? "Simas Prima Manfaat Bulanan" : lsbs_id == 164 ? "Simas Stabil Link" : "SSP Pro") + "\n" +
														"- Jenis: " + (fbul == 0 ? "Biasa" : "Manfaat Bulanan") + "\n" +
														"- Begdate: " + begdate + "\n" +
														"- Kurs: " + (lku_id.equals("01") ? "IDR" : "USD")  + "\n" +
														"- MGI: " + mgi + "\n" +
														"- lsbs_id, lsdbs_number, fbul, begdate, lku_id, mgi: " + lsbs_id + ", " + lsdbs_number + ", " + fbul + ", " + begdate + ", " + lku_id + ", " + mgi +"\n" + 
														"- User Login:  "+ edit.getCurrentUser().getLus_full_name()+"\n" +
														"\n\nTerima kasih.", null);
										}
								}
								
							}
						} catch (Exception e) {
							logger.error("ERROR :", e);
						}
						
					}
			  }else{
				  edit.getPowersave().setMps_rate_date(null);
						
				//UNTUK SPESIAL DAN KHUSUS
							 
				  rate = edit.getPowersave().getMps_rate();
				  if (rate==null)
				  {
					  rate = new Double(0);
					  edit.getPowersave().setMps_rate(rate);
				  }
							 
				//3 BULAN SUDAH TIDAK BERLAKU
				/*if (jns_top_up0.equalsIgnoreCase("3") )
				{
					m.put("mps_jangka_inv","Investasi 3 Bulan sudah tidak berlaku.");
				}else{*/
					// UNTUK KHUSUS wajib isi memo
				  String hasil_memo = a.cek_memo(edit.getPowersave().getMpr_note());
				  if (hasil_memo.trim().length()!=0)
				  {
					 //m.put("mpr_note",hasil_memo);
					  err.rejectValue("powersave.mpr_note","",hasil_memo);
				  }
				  
				  
				  // hitung jumlah hari dan bunga dari tabel
				if (jns_rate.intValue()==2)
				{
					jumlah_hari_mgi = new Integer(0);
					Date ldt_end   = edit.getPowersave().getMps_batas_date();
					//ll_day 	 = DaysAfter ( date(ldt_begin), date(ldt_end) )
					//Integer selisih_hari =new Integer( n_date.hari1(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),ldt_end.getYear()+1900,ldt_end.getMonth()+1,ldt_end.getDate()));
					if(!Common.isEmpty(edit.getPowersave().getMsl_spaj_lama())){
						    Integer tanggal_edate = new Integer(edit.getPowersave().getEnddate_topup().getDate());
						Integer bulan_edate = new Integer((edit.getPowersave().getEnddate_topup().getMonth())+1);
						Integer tahun_edate = new Integer((edit.getPowersave().getEnddate_topup().getYear())+1900);
						String tgl_edate = FormatString.rpad("0",Integer.toString(tanggal1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan1.intValue()),2)+"/"+Integer.toString(tahun1.intValue());
						Date tgl_next_topup = null;
						 tgl_next_topup = (n_date.f_add_months(tahun_edate.intValue(),bulan_edate.intValue(),tanggal_edate.intValue(),Integer.parseInt(jns_top_up0)));
						 Date Tgl_next_topup=null;
							 if(edit.getDatausulan().getMssur_se()!=null){
								 if(edit.getDatausulan().getMssur_se()==3){
									 tgl_next_topup = bacDao.selectMprMatureDate(edit.getPowersave().getMsl_spaj_lama());
								 }
							 }
							Tgl_next_topup = defaultDateFormat.parse(Integer.toString(tgl_next_topup.getDate())+"/"+(tgl_next_topup.getMonth()+1)+"/"+(tgl_next_topup.getYear()+1900));
							if (bulan_edate.intValue() == 2 || tanggal_edate.intValue() == 30) 
							 {
								if  ((tgl_next_topup.getMonth()+1) != 2) 
								{
								   Tgl_next_topup = defaultDateFormat.parse(Integer.toString(tanggal_edate.intValue())+"/"+(tgl_next_topup.getMonth()+1)+"/"+(tgl_next_topup.getYear()+1900));
								}
							  }	
						
						edit.getPowersave().setEnddate_topup(Tgl_next_topup);  
						if(edit.getDatausulan().getMssur_se()!=null){
							 if(edit.getDatausulan().getMssur_se()==3){
								 tanggal_edate = new Integer(edit.getPowersave().getEnddate_topup().getDate());
							 }
						}
						jumlah_hari_mgi = new Integer( n_date.hari1(Tgl_next.getYear()+1900,Tgl_next.getMonth()+1,Tgl_next.getDate(),tahun_edate.intValue(),bulan_edate.intValue(),tanggal_edate.intValue()));
						if(edit.getDatausulan().getMssur_se()!=null){
							if(edit.getDatausulan().getMssur_se()==3){
								//jumlah_hari_mgi = new Integer( n_date.hari1(Tgl_next.getYear()+1900,Tgl_next.getMonth()+1,Tgl_next.getDate(),tgl_next_topup.getYear()+1900,tgl_next_topup.getMonth()+1,tgl_next_topup.getDate()));
								jumlah_hari_mgi = new Integer( n_date.hari1(edit.getPowersave().getMps_deposit_date().getYear()+1900,edit.getPowersave().getMps_deposit_date().getMonth()+1,edit.getPowersave().getMps_deposit_date().getDate(),edit.getPowersave().getMpr_mature_date().getYear()+1900,edit.getPowersave().getMpr_mature_date().getMonth()+1,edit.getPowersave().getMpr_mature_date().getDate()));
								jumlah_hari_mgi = new Integer( jumlah_hari_mgi.intValue()+1);
							}
						}
					}else{
						jumlah_hari_mgi =new Integer( n_date.hari1(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),Tgl_next.getYear()+1900,Tgl_next.getMonth()+1,Tgl_next.getDate()));
					}
					jumlah_hari_mgi = new Integer( jumlah_hari_mgi.intValue() );

					bunga = proc_hit_rate(flag_bulanan,jns_top_up0, premi, rate,jumlah_hari_mgi,flag_powersave,jumlah_hari_mgi_bulanan);
						
					edit.getPowersave().setMps_prm_interest(bunga);
				}else{
					 // edit.getPowersave().setMps_prm_interest(bunga);
					
					 jumlah_hari_mgi =new Integer( n_date.hari1(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),Tgl_next.getYear()+1900,Tgl_next.getMonth()+1,Tgl_next.getDate()));
					 jumlah_hari_mgi = new Integer( jumlah_hari_mgi.intValue() );

					 bunga = proc_hit_rate(flag_bulanan,jns_top_up0, premi, rate,jumlah_hari_mgi,flag_powersave,jumlah_hari_mgi_bulanan);
					 edit.getPowersave().setMps_prm_interest(bunga);
				}
				//}			
			 }
			if(!Common.isEmpty(edit.getPowersave().getMsl_spaj_lama())){
				if((jumlah_hari_mgi!= uwDao.selectJumlahHariMGI(edit.getPowersave().getMps_deposit_date(), edit.getPowersave().getMpr_mature_date()))){
					logger.info(uwDao.selectTotalBunga(edit.getPowersave().getMps_deposit_date(), edit.getPowersave().getMpr_mature_date(), rate, premi));
					m.put("mps_prm_interest","Terdapat kesalahan perhitungan Bunga, silakan hubungi pihak ITwebandmobile@sinarmasmsiglife.co.id.");
					String message =" HITUNGAN BY FUNCTION" +
									"\n Tanggal Mulai(Deposit Date) : " + new SimpleDateFormat("dd/MM/yyyy").format(edit.getPowersave().getMps_deposit_date())+
									"\n Tanggal Akhir (Mature Date) : " + new SimpleDateFormat("dd/MM/yyyy").format(edit.getPowersave().getMpr_mature_date())+
									"\n Jumlah hari : " + jumlah_hari_mgi+
									"\n Total Bunga : " + new Double(FormatNumber.round(bunga.doubleValue(),2))+
									"\n No SPAJ LAMA :" + edit.getPowersave().getMsl_spaj_lama()+ 
									"\n\n HITUNGAN BY QUERY" +
									"\n Jumlah hari : " + uwDao.selectJumlahHariMGI(edit.getPowersave().getMps_deposit_date(), edit.getPowersave().getMpr_mature_date())+
									"\n Total Bunga : " + uwDao.selectTotalBunga(edit.getPowersave().getMps_deposit_date(), edit.getPowersave().getMpr_mature_date(), rate, premi); 
									
					try {
						email.send(true, props.getProperty("admin.ajsjava"), new String[]{"deddy@sinarmasmsiglife.co.id"},null, null, "SALAH PERHITUNGAN BUNGA DAN JUMLAH HARI"+edit.getPowersave().getMsl_spaj_lama()!=null?" CASE SURRENDER ENDORS DENGAN SPAJ ":" DENGAN SPAJ " + edit.getPemegang().getReg_spaj(), message, null);
					} catch (MailException e) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e);
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e);
					}
				}
			}
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
			 
			//PEMBULATAN BERDASARKAN KURS - Yusuf 20 Oct 2009 - Req Dr Ingrid, semua pembulatan 2 digit
			bunga = new Double(FormatNumber.round(bunga.doubleValue(),2));
			
			edit.getPowersave().setMps_prm_interest(bunga);
		}
		
		// KALAU ACCOUNT RECUR DAN REC_CLIENT
		if ((flag_account.intValue()==2)||(flag_account.intValue()==3) )
		{
			// ROLLOVER TANPA + BUNGA
			/*if(jns_top_up1.equalsIgnoreCase("2"))
			{*/
				Integer jenis_nasabah = edit.getRekening_client().getMrc_jn_nasabah();
			
				String kota_rek = edit.getRekening_client().getMrc_kota();
				if (kota_rek == null)
				{
					kota_rek ="";
				}else{
					kota_rek = f_validasi.convert_karakter(kota_rek);
				}
				edit.getRekening_client().setMrc_kota(kota_rek);
				
				if (jenis_nasabah ==null)
				{
					jenis_nasabah = new Integer(0);
					edit.getRekening_client().setMrc_jn_nasabah(jenis_nasabah);
				}
				String rek_bank_pp1=edit.getRekening_client().getMrc_no_ac();
				if (rek_bank_pp1==null)
				{
					rek_bank_pp1="";
					edit.getRekening_client().setMrc_no_ac(rek_bank_pp1);
				}
				String bank_pp1=edit.getRekening_client().getLsbp_id();
				if (bank_pp1==null)
				{
					bank_pp1="";
					edit.getRekening_client().setLsbp_id(bank_pp1);
				}
				String nama_bank_rekclient="";
				Integer max_digit=0;
				Integer min_digit=0;
				if(bank_pp1!=null)
				{
					Map data1= (HashMap) this.bacDao.select_bankrekclient(bank_pp1);
					if (data1!=null)
					{		
						nama_bank_rekclient = (String)data1.get("BANK_NAMA");
						max_digit = ((BigDecimal) data1.get("MAX_DIGIT")).intValue();
						min_digit = ((BigDecimal) data1.get("MIN_DIGIT")).intValue();
					}
				}
				edit.getRekening_client().setLsbp_nama(nama_bank_rekclient);
					
					
				String atasnama1=edit.getRekening_client().getMrc_nama();
				if (atasnama1==null)
				{
					atasnama1="";
					edit.getRekening_client().setMrc_nama(atasnama1);
				}
				Integer jenis_tab = edit.getRekening_client().getMrc_jenis();
				if (jenis_tab==null)
				{
					jenis_tab=new Integer(0);
					edit.getRekening_client().setMrc_jenis(jenis_tab);
				}
				String kota_bank=edit.getRekening_client().getMrc_kota();
				if (kota_bank==null)
				{
					kota_bank="";
					edit.getRekening_client().setMrc_kota(kota_bank);
				}
				String cabang_bank=edit.getRekening_client().getMrc_cabang();			
				if (cabang_bank==null)
				{
					cabang_bank="";
					edit.getRekening_client().setMrc_cabang(cabang_bank);
				}
				String hasil_jenis_tab=a.jenis_tab(Integer.toString(jenis_tab.intValue()),lca_id,flag_account.intValue());
				if (hasil_jenis_tab.trim().length()!=0)
				{
					m.put("mrc_jenis",hasil_jenis_tab);
					//err.rejectValue("rekening_client.mrc_jenis","",hasil_jenis_tab);
				}
				
				String hasil_cabang_bank=a.cek_cabang_bank(cabang_bank,lca_id,flag_account.intValue(),Integer.toString(autodebet.intValue()));
				if (hasil_cabang_bank.trim().length()!=0)
				{
					m.put("mrc_cabang",hasil_cabang_bank);
					//err.rejectValue("rekening_client.mrc_cabang","",hasil_cabang_bank);
				}
	
				String hasil_rek_bank_pp1 = a.no_rek1(rek_bank_pp1, lca_id, flag_account.intValue(), Integer.toString(autodebet.intValue()), max_digit, min_digit);
				if (hasil_rek_bank_pp1.trim().length()!=0)
				{
					m.put("mrc_no_ac",hasil_rek_bank_pp1);
					//err.rejectValue("rekening_client.mrc_no_ac","",hasil_rek_bank_pp1);
				}
				String hasil_bank1 = a.cek_bank1(bank_pp1,lca_id,flag_account.intValue(),Integer.toString(autodebet.intValue()));
				if (hasil_bank1.trim().length()!=0)
				{
					m.put("lsbp_id",hasil_bank1);
					//err.rejectValue("rekening_client.lsbp_id","",hasil_bank1);
				}
					
				String hasil_atasnama1=a.cek_atas_nama1(atasnama1,lca_id,flag_account.intValue(),Integer.toString(autodebet.intValue()));
				if (hasil_atasnama1.trim().length()!=0)
				{
					m.put("mrc_nama",hasil_atasnama1);
					//err.rejectValue("rekening_client.mrc_nama","",hasil_atasnama1);
				}
				
				Integer kuasa = edit.getRekening_client().getMrc_kuasa();
				if (kuasa == null)
				{
					kuasa = new Integer(0);
				}

				if (kuasa.intValue()== 1)
				{
					Date tgl_surat = edit.getRekening_client().getTgl_surat();
					
					if (tgl_surat == null)
					{
						m.put("tgl","Tanggal surat belum diisi, silahkan isi tanggal surat terlebih dahulu.");
					}
				}
				edit.getRekening_client().setMrc_kuasa(kuasa);
				
				String keterangan_rek = edit.getRekening_client().getNotes();
				if (keterangan_rek==null)
				{
					keterangan_rek = "";
					edit.getRekening_client().setNotes(keterangan_rek);
				}
				
				if ((flag_bao1.intValue()==1) && ((flag_account.intValue()==2) || (flag_account.intValue()==3)))
				{
					
					if (edit.getAgen().getKode_regional()!=null )
					{
						if (!(edit.getAgen().getKode_regional().trim().equalsIgnoreCase("")))
						{
							if (FormatString.rpad("0",(edit.getAgen().getKode_regional().substring(0,4)),4).equalsIgnoreCase("0914"))
							{							
								String flag_jenis_nasabah=a.cek_jenis_nasabah(Integer.toString(jenis_nasabah.intValue()));
								if (flag_jenis_nasabah.trim().length()!=0)
								{
									m.put("mrc_jn_nasabah",flag_jenis_nasabah);
									//err.rejectValue("rekening_client.mrc_jn_nasabah","",flag_jenis_nasabah);
								}
							}
						}
					}
				}
			}/*else{
				Integer jenis_nasabah = edit.getRekening_client().getMrc_jn_nasabah();
				
				String kota_rek = edit.getRekening_client().getMrc_kota();
				if (kota_rek == null)
				{
					kota_rek ="";
				}else{
					kota_rek = f_validasi.convert_karakter(kota_rek);
				}
				edit.getRekening_client().setMrc_kota(kota_rek);
				
				if (jenis_nasabah ==null)
				{
					jenis_nasabah = new Integer(0);
					edit.getRekening_client().setMrc_jn_nasabah(jenis_nasabah);
				}
				String rek_bank_pp1=edit.getRekening_client().getMrc_no_ac();
				if (rek_bank_pp1==null)
				{
					rek_bank_pp1="";
					edit.getRekening_client().setMrc_no_ac(rek_bank_pp1);
				}
				String bank_pp1=edit.getRekening_client().getLsbp_id();
				if (bank_pp1==null)
				{
					bank_pp1="";
					edit.getRekening_client().setLsbp_id(bank_pp1);
				}
				String nama_bank_rekclient="";
				if(bank_pp1!=null)
				{
					Map data1= (HashMap) this.bacDao.select_bankrekclient(bank_pp1);
					if (data1!=null)
					{		
						nama_bank_rekclient = (String)data1.get("BANK_NAMA");
					}
				}
				edit.getRekening_client().setLsbp_nama(nama_bank_rekclient);
					
					
				String atasnama1=edit.getRekening_client().getMrc_nama();
				if (atasnama1==null)
				{
					atasnama1="";
					edit.getRekening_client().setMrc_nama(atasnama1);
				}
				Integer jenis_tab = edit.getRekening_client().getMrc_jenis();
				if (jenis_tab==null)
				{
					jenis_tab=new Integer(0);
					edit.getRekening_client().setMrc_jenis(jenis_tab);
				}
				String kota_bank=edit.getRekening_client().getMrc_kota();
				if (kota_bank==null)
				{
					kota_bank="";
					edit.getRekening_client().setMrc_kota(kota_bank);
				}
				String cabang_bank=edit.getRekening_client().getMrc_cabang();			
				if (cabang_bank==null)
				{
					cabang_bank="";
					edit.getRekening_client().setMrc_cabang(cabang_bank);
				}
				
				Integer kuasa = edit.getRekening_client().getMrc_kuasa();
				if (kuasa == null)
				{
					kuasa = new Integer(0);
				}

				if (kuasa.intValue()== 1)
				{
					Date tgl_surat = edit.getRekening_client().getTgl_surat();
					
					if (tgl_surat == null)
					{
						m.put("tgl","Tanggal surat belum diisi, silahkan isi tanggal surat terlebih dahulu.");
					}
				}
				edit.getRekening_client().setMrc_kuasa(kuasa);
				
				String keterangan_rek = edit.getRekening_client().getNotes();
				if (keterangan_rek==null)
				{
					keterangan_rek = "";
					edit.getRekening_client().setNotes(keterangan_rek);
				}
			}
		}*/
		return m;
	}
	
	//helpdesk [133346] produk baru 142-13 Smart Investment Protection
	private double proc_hit_rate_6_bulan(Double premi, Double rate){
		Double bunga = new Double(0);
		
		bunga = premi * (rate / 100) * 0.5;
		
		return bunga;
	}

	private Double proc_hit_rate(Integer flag_bulanan, String jns_top_up0, Double premi, Double rate, Integer jumlah_hari_mgi,
			Integer flag_powersave, Integer jumlah_hari_mgi_bulanan) {

		Double bunga = new Double(0);
		bunga = bacDao.hitungBunga(flag_bulanan, Integer.parseInt(jns_top_up0), premi, rate, jumlah_hari_mgi, flag_powersave);
		
		return bunga;
	}
	
	private Map proc_validasi_hcp(Cmdeditbac edit,String status,Map m,Errors err)throws ServletException,IOException
	{
		form_investasi a =new form_investasi();
		Integer flag_suamiistri = new Integer(0);
		Integer flag_anak = new Integer(0);
		int letak=0;

		//define begdate dan end date polis plan utama
		Integer tanggal1=null;
		Integer bulan1=null;
		Integer tahun1 =null;
		Integer tanggal = null;
		Integer bulan = null;
		Integer tahun = null;
		
		//tgl beg date plan utama
		tanggal1= edit.getDatausulan().getMste_beg_date().getDate();
		bulan1 = (edit.getDatausulan().getMste_beg_date().getMonth())+1;
		tahun1 = (edit.getDatausulan().getMste_beg_date().getYear())+1900;
		
		//tgl end date plan utama
		tanggal= edit.getDatausulan().getMste_end_date().getDate();
		bulan = edit.getDatausulan().getMste_end_date().getMonth()+1;
		tahun = edit.getDatausulan().getMste_end_date().getYear()+1900;
		
		//plan utama
		Integer kode_produk=null;
		Integer number_produk = null;
		kode_produk = edit.getDatausulan().getLsbs_id();
		number_produk = edit.getDatausulan().getLsdbs_number();
		String nama_produk="";
		String kurs = edit.getDatausulan().getKurs_p();
		Double premi_utama = edit.getDatausulan().getMspr_premium();
		Integer flag_gutri = edit.getDatausulan().getFlag_gutri();
		if (flag_gutri == null)
		{
			flag_gutri = new Integer(0);
		}
		Integer mste_flag_guthrie = edit.getPemegang().getMste_flag_guthrie();
		if (mste_flag_guthrie == null)
		{
			mste_flag_guthrie = new Integer(0);
		}
		String spaj = edit.getPemegang().getReg_spaj();
		Integer kode_flag = new Integer(0);
		Double up = edit.getDatausulan().getMspr_tsi();
		
		//List peserta = this.elionsManager.select_hcp(spaj);
		List peserta = edit.getDatausulan().getDaftahcp();
		Integer jumlah_r =new Integer(peserta.size());
		if (jumlah_r==null)
		{
			jumlah_r=new Integer(0);
		}
		Integer jumlah_rider_hcp = jumlah_r;
		List peserta_hcp = edit.getDatausulan().getDaftapeserta();
		Integer jmlh_peserta=peserta_hcp.size();
		if (jmlh_peserta == null)
		{
			jmlh_peserta = new Integer(0);
		}
		edit.getDatausulan().setJml_peserta(new Integer(jmlh_peserta.intValue()));
		
		//define peserta array
		Integer[] no_urut;                       
		Integer[] lsre_id;                       
		String[] nama;    
		Integer[] kelamin;                       
		Integer[] umur;                          
		Double[] premi;                          
		String[] reg_spaj;
		Date[] tanggal_lahir;
		String[] plan;
		Integer[] lspc_no;
		Integer[] lsbs_id;
		Integer[] lsdbs_number;
		Integer[] factor_up;
			
		no_urut = new Integer[(jumlah_rider_hcp.intValue())+1];
		lsre_id = new Integer[(jumlah_rider_hcp.intValue())+1];
		nama = new String[(jumlah_rider_hcp.intValue())+1];
		kelamin = new Integer[(jumlah_rider_hcp.intValue())+1];
		umur = new Integer[(jumlah_rider_hcp.intValue())+1];
		premi = new Double[(jumlah_rider_hcp.intValue())+1];
		reg_spaj = new String[(jumlah_rider_hcp.intValue())+1];
		tanggal_lahir = new Date[(jumlah_rider_hcp.intValue())+1];
		plan = new String[(jumlah_rider_hcp.intValue())+1];
		lsbs_id = new Integer[(jumlah_rider_hcp.intValue())+1];
		lsdbs_number = new Integer[(jumlah_rider_hcp.intValue())+1];
		lspc_no = new Integer[(jumlah_rider_hcp.intValue())+1];
		factor_up = new Integer[(jumlah_rider_hcp.intValue())+1];//xxx

		//top up
		Double premi_berkala = new Double(0);
		Double total_premi_tu = new Double(0);
		premi_berkala = edit.getInvestasiutama().getDaftartopup().getPremi_berkala();
		if (premi_berkala == null)
		{
			premi_berkala = new Double(0);
			edit.getInvestasiutama().getDaftartopup().setPremi_berkala(premi_berkala);
		}
		total_premi_tu =  new Double ( total_premi_tu.doubleValue() + premi_berkala.doubleValue());
		Integer pil_berkala = new Integer(0);
		pil_berkala = edit.getInvestasiutama().getDaftartopup().getPil_berkala();
		if (pil_berkala == null)
		{
			pil_berkala = new Integer(0);
			edit.getInvestasiutama().getDaftartopup().setPil_berkala(pil_berkala);
		}
		
		Double premi_tunggal = new Double(0);
		premi_tunggal = edit.getInvestasiutama().getDaftartopup().getPremi_tunggal();
		if (premi_tunggal == null)
		{
			premi_tunggal = new Double(0);
			edit.getInvestasiutama().getDaftartopup().setPremi_tunggal(premi_tunggal);
		}
		total_premi_tu =  new Double ( total_premi_tu.doubleValue() + premi_tunggal.doubleValue());
		Integer pil_tunggal = edit.getInvestasiutama().getDaftartopup().getPil_tunggal();
		if (pil_tunggal == null)
		{
			pil_tunggal = new Integer(0);
			edit.getInvestasiutama().getDaftartopup().setPil_tunggal(pil_tunggal);
		}
		Boolean flag_minus1 = false;
		//define rider
		Integer number_plan1= new Integer(0);
		Double li_pct_biaya = new Double(0); 
		Double total_premi_rider=new Double(0);
		Double total_premi_sementara = new Double(0);

		Integer tgl_awal_rider = new Integer(0);
		Integer bln_awal_rider = new Integer(0);
		Integer thn_awal_rider = new Integer(0);
		String tanggal_awal_rider =null;
		Integer ins_period= new Integer(0);
		
		Integer[] unit;
		String hasil_unit1;
		String hasil_class1;
		Integer[] klases;
		Integer[] f_Up;
			
		String[] nama_rider;
		String hasil_rider;
		Double[] sum;
		Date[] end_date_rider;
		Date[] beg_date_rider;
		Date[] end_pay_rider;
		Double[] rate;
		Integer[] percent;
		Integer[] ins_rider;
		Double[] up_pa_rider;
		Double[] up_pb_rider;
		Double[] up_pc_rider;
		Double[] up_pd_rider;
		Double[] up_pm_rider;
		Integer[] insured;
		Double[] mspr_extra;
				
		unit = new Integer[(jumlah_rider_hcp.intValue())+1];
		klases =  new Integer[(jumlah_rider_hcp.intValue())+1];
		f_Up = new Integer[(jumlah_rider_hcp.intValue())+1];
		nama_rider= new String[(jumlah_rider_hcp.intValue())+1];
		sum = new Double[(jumlah_rider_hcp.intValue())+1];
		end_date_rider = new Date[(jumlah_rider_hcp.intValue())+1];
		beg_date_rider= new Date[(jumlah_rider_hcp.intValue())+1];
		end_pay_rider= new Date[(jumlah_rider_hcp.intValue())+1];
		rate= new Double[(jumlah_rider_hcp.intValue())+1];
		percent=new Integer[(jumlah_rider_hcp.intValue())+1];
		ins_rider= new Integer[(jumlah_rider_hcp.intValue())+1];
		up_pa_rider = new Double[(jumlah_rider_hcp.intValue())+1];	
		up_pb_rider = new Double[(jumlah_rider_hcp.intValue())+1];	
		up_pc_rider = new Double[(jumlah_rider_hcp.intValue())+1];	
		up_pd_rider = new Double[(jumlah_rider_hcp.intValue())+1];	
		up_pm_rider = new Double[(jumlah_rider_hcp.intValue())+1];	
		insured = new Integer[(jumlah_rider_hcp.intValue())+1];	
		mspr_extra = new Double[(jumlah_rider_hcp.intValue())+1];
		
		
		hasil_rider="";
		//n_prod plan utama
		if (Integer.toString(kode_produk.intValue()).trim().length()==1)
		{
			nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
		}else{
			nama_produk="produk_asuransi.n_prod_"+kode_produk;	
		}
		try{
			Class aClass1 = Class.forName( nama_produk );
			n_prod produk = (n_prod)aClass1.newInstance();
			produk.setSqlMap(this.uwDao.getSqlMapClient());
			produk.cek_flag_agen(kode_produk.intValue(),number_produk.intValue(), 0);

			produk.of_set_bisnis_no(number_produk.intValue());
			produk.ii_bisnis_no=(number_produk.intValue());
			Integer pay_period = edit.getDatausulan().getMspo_pay_period();
			ins_period = new Integer(produk.of_get_conperiod(number_produk.intValue()));
			Integer autodebet=edit.getDatausulan().getMste_flag_cc();
			if (autodebet == null)
			{
				autodebet = new Integer(0);
			}

			
			int flag_platinum = produk.flag_platinumlink;
			Integer flag_horison = new Integer(produk.flag_horison);
			edit.getDatausulan().setFlag_horison(flag_horison);
			
			Integer flag_simas = new Integer(produk.simas);
			edit.getDatausulan().setFlag_simas(flag_simas);

			int flag_cutipremi = produk.flag_cuti_premi; 
			//kode produk
			produk.of_set_bisnis_no(number_produk.intValue());
			produk.ii_bisnis_no=(number_produk.intValue());
			produk.ii_bisnis_id=(kode_produk.intValue());
			Integer flag_ekalink = produk.flag_ekalink;
			edit.getDatausulan().setFlag_ekalink(flag_ekalink);
			
			Integer flag_bulanan = new Integer(produk.flag_powersavebulanan);
			edit.getDatausulan().setFlag_bulanan(flag_bulanan);
			produk.setSqlMap(getSqlMapClient());

			Integer flag_powersave = new Integer(produk.of_get_bisnis_no(0));
			if (produk.flag_powersavebulanan == 1)
			{
				produk.cek_flag_agen(kode_produk.intValue(), number_produk.intValue(), 0);
				flag_powersave = new Integer(produk.flag_powersave);
			}
			edit.getPowersave().setFlag_powersave(flag_powersave);
			

			//flag
			Integer flag_account=new Integer(produk.flag_account);
			if (autodebet.intValue() == 3)
			{
				flag_account = new Integer(2);
			}
			Boolean isBungaSimponi=new Boolean(produk.isBungaSimponi);
			Integer flag_worksite = new Integer(produk.flag_worksite);
			Integer flag_endowment = new Integer(produk.flag_endowment);
			if (edit.getDatausulan().getFlag_worksite_ekalife() == null)
			{
				edit.getDatausulan().setFlag_worksite_ekalife(new Integer(0));
			}
			Integer flag_bungasimponi=new Integer(0);
			if (isBungaSimponi.booleanValue()==true)
			{
				flag_bungasimponi=new Integer(1);
			}else{
				flag_bungasimponi=new Integer(0);
			}
				
			Integer flag_jenis_plan = new Integer(produk.flag_jenis_plan);
			edit.getDatausulan().setFlag_jenis_plan(flag_jenis_plan);
			Boolean isBonusTahapan=new Boolean(produk.isBonusTahapan);
			Integer flag_bonustahapan = new Integer(0);
			if (isBonusTahapan.booleanValue()==true)
			{
				flag_bonustahapan=new Integer(1);
			}else{
				flag_bonustahapan=new Integer(0);
			}
			Integer flag_as=new Integer(produk.flag_as);
			edit.getDatausulan().setFlag_as(flag_as);
			edit.getDatausulan().setIsBonusTahapan(flag_bonustahapan);
			edit.getDatausulan().setIsBungaSimponi(flag_bungasimponi);
			edit.getDatausulan().setFlag_worksite(flag_worksite);
			edit.getDatausulan().setFlag_endowment(flag_endowment);
			
			produk.of_set_kurs(kurs);
			kode_flag=new Integer(produk.kode_flag);
			edit.getDatausulan().setKode_flag(kode_flag);
			
			Integer flag_rider = new Integer(produk.flag_rider);
			edit.getDatausulan().setFlag_rider(flag_rider);
			String hasil_jns_top_up ="";
			String hasil_topup1 ="";
			Integer pmode_id = edit.getDatausulan().getLscb_id();

			
			if (((pil_berkala.intValue())==1 && (pmode_id.intValue())==0))
			{
				hasil_jns_top_up="Cara Bayar Sekaligus hanya untuk pilihan Top-Up Tunggal ";
				if (status.equalsIgnoreCase("windowhcp"))
				{
					err.rejectValue("investasiutama.daftartopup.pil_tunggal","",hasil_jns_top_up);
				}else{
					m.put("pil_tunggal", hasil_jns_top_up);
				}
			}
			
			if (pil_tunggal.intValue()==2)
			{
				edit.getDatausulan().setLi_trans_tunggal(new Integer(2));
			}else if (pil_berkala.intValue()==3)
				{
				edit.getDatausulan().setLi_trans_berkala(new Integer(6));
				}
			
			hasil_jns_top_up=a.jenis_topup(premi_tunggal,Integer.toString(pil_tunggal.intValue()));
			if (hasil_jns_top_up.trim().length()!=0)
			{
				if (status.equalsIgnoreCase("windowhcp"))
				{
					err.rejectValue("investasiutama.daftartopup.pil_tunggal","",hasil_jns_top_up);	
				}else{
					m.put("pil_tunggal", hasil_jns_top_up);
				}
			}else{
				if (pil_tunggal.intValue()==0)
				{
					hasil_topup1 = a.cek_topup(premi_tunggal,Integer.toString(pil_tunggal.intValue()));
					if (hasil_topup1.trim().length()!=0)
					{
						if (status.equalsIgnoreCase("windowhcp"))
						{
							err.rejectValue("investasiutama.daftartopup.pil_tunggal","",hasil_topup1);
						}else{
							m.put("pil_tunggal", hasil_topup1);
						}
					}
				}else{
					hasil_topup1 = a.jmlh_topup(premi_tunggal.toString());
					if (hasil_topup1.trim().length()!=0)
					{
						if (status.equalsIgnoreCase("windowhcp"))
						{
							err.rejectValue("investasiutama.daftartopup.pil_tunggal","",hasil_topup1);
						}else{
							m.put("pil_tunggal", hasil_topup1);
						}
					}
				}
			}
			
			hasil_jns_top_up=a.jenis_topup(premi_berkala,Integer.toString(pil_berkala.intValue()));
			if (hasil_jns_top_up.trim().length()!=0)
			{
				if (status.equalsIgnoreCase("windowhcp"))
				{
					err.rejectValue("investasiutama.daftartopup.pil_berkala","",hasil_jns_top_up);	
				}else{
					m.put("pil_berkala", hasil_jns_top_up);
				}
			}else{
				if (pil_berkala.intValue()==0)
				{
					hasil_topup1 = a.cek_topup(premi_berkala,Integer.toString(pil_berkala.intValue()));
					if (hasil_topup1.trim().length()!=0)
					{
						if (status.equalsIgnoreCase("windowhcp"))
						{
							err.rejectValue("investasiutama.daftartopup.pil_berkala","",hasil_topup1);
						}else{
							m.put("pil_berkala",hasil_topup1);
						}
					}
				}else{
					hasil_topup1 = a.jmlh_topup(premi_berkala.toString());
					if (hasil_topup1.trim().length()!=0)
					{
						if (status.equalsIgnoreCase("windowhcp"))
						{
							err.rejectValue("investasiutama.daftartopup.pil_berkala","",hasil_topup1);
						}else{
							m.put("pil_berkala", hasil_topup1);
						}
					}
				}
			}
			
		if (hasil_jns_top_up.trim().length()==0)
			{
				if (pil_tunggal.intValue() >0)
				{
					hasil_jns_top_up=produk.min_topup(pmode_id,premi_utama , premi_tunggal, kurs, pil_tunggal.intValue());
					if (hasil_jns_top_up.trim().length()!=0)
					{
						if (status.equalsIgnoreCase("windowhcp"))
						{
							err.rejectValue("investasiutama.daftartopup.pil_tunggal","",hasil_jns_top_up);	
						}else{
							m.put("pil_tunggal", hasil_jns_top_up);
						}
					}
				}
				if (pil_berkala.intValue() >0)
				{
					hasil_jns_top_up=produk.min_topup(pmode_id,premi_utama , premi_berkala, kurs, pil_berkala.intValue());
					if (hasil_jns_top_up.trim().length()!=0)
					{
						if (status.equalsIgnoreCase("windowhcp"))
						{
							err.rejectValue("investasiutama.daftartopup.pil_berkala","",hasil_jns_top_up);	
						}else{
							m.put("pil_berkala", hasil_jns_top_up);
						}
					}
				}
				
				if (pil_tunggal.intValue()  == 0 && pil_berkala.intValue() == 0)
				{
					hasil_jns_top_up=produk.min_total_premi(pmode_id, premi_utama , kurs);
					if (hasil_jns_top_up.trim().length()!=0)
					{
						if (status.equalsIgnoreCase("windowhcp"))
						{
							err.rejectValue("investasiutama.daftartopup.pil_berkala","",hasil_jns_top_up);	
						}else{
							m.put("pil_berkala", hasil_jns_top_up);
						}
					}
				}
			}	
			
			List peserta2 = new ArrayList();
			List rider =  new ArrayList();
			List peserta1 = new ArrayList();
			for (int k=0 ; k < (jumlah_rider_hcp.intValue()) ; k++)
			{
				Hcp pp = new Hcp();
				//set awal rider
				unit[k] = new Integer(0);
				klases[k] =  new Integer(0);
				f_Up[k] = new Integer(0);
				nama_rider[k]= "";
				sum[k] = new Double(0);
				end_date_rider[k] = null;
				beg_date_rider[k]=null;
				end_pay_rider[k]=null;
				rate[k]= new Double(0);
				percent[k]=new Integer(0);
				ins_rider[k]= new Integer(0);
				up_pa_rider[k] = new Double(0);	
				up_pb_rider[k] = new Double(0);	
				up_pc_rider[k] = new Double(0);	
				up_pd_rider[k] = new Double(0);	
				up_pm_rider[k] = new Double(0);	
				insured[k] = new Integer(0);	
				mspr_extra[k] = new Double(0); 
				
				//set awal peserta ( untuk hcpf yang basic mengikuto ttg)
				Hcp bf1= (Hcp)peserta.get(k);
				if (jmlh_peserta.intValue() == 0)
				{
					no_urut[k]=new Integer(1);
					lsre_id[k]=edit.getTertanggung().getLsre_id();				
					nama[k]=edit.getTertanggung().getMcl_first();
					tanggal_lahir[k]=edit.getTertanggung().getMspe_date_birth();	
					kelamin[k] = edit.getTertanggung().getMspe_sex();
					umur[k] = edit.getTertanggung().getMste_age();
					premi[k] = bf1.getMspr_premium();
					reg_spaj[k] = edit.getTertanggung().getReg_spaj();
					plan[k] = bf1.getPlan_rider();
					if(bf1.getLspc_no()==null){
						bf1.setLspc_no(0);
					}
					lspc_no[k] = bf1.getLspc_no();
					jmlh_peserta = new Integer((jmlh_peserta.intValue()) + 1);
				}else{
					no_urut[k]=bf1.getNo_urut();
					lsre_id[k]=bf1.getLsre_id();				
					nama[k]=bf1.getNama();
					tanggal_lahir[k]=bf1.getTgl_lahir();	
					kelamin[k] = bf1.getKelamin();
					umur[k] = bf1.getUmur();
					premi[k] = bf1.getPremi();
					reg_spaj[k] = bf1.getReg_spaj();
					tanggal_lahir[k] = bf1.getTgl_lahir();
					plan[k] = bf1.getPlan_rider();
					if(bf1.getLspc_no()==null){
						bf1.setLspc_no(0);
					}
					lspc_no[k] = bf1.getLspc_no();
				}
				
				if (plan[k] == null)
				{
					plan[k] = "0~X0";
				}
				if (plan[k].equalsIgnoreCase("0~X0"))
				{
					if (status.equalsIgnoreCase("windowhcp"))
					{
						err.rejectValue("datausulan.daftahcp["+k+"].plan_rider","","Silahkan  pilih terlebih dahulu plan ke " +(k+1) +".");
					}else{
						m.put("plan_rider", "Silahkan  pilih terlebih dahulu plan ke " +(k+1) +".");
					}
				}
				//dari plan utama
				produk.of_set_up(up.doubleValue());	
				produk.of_set_pmode(pmode_id.intValue());
				pay_period = new Integer(produk.of_get_payperiod());
				
				letak=0;
				letak=plan[k].indexOf("X");
				String koderider = (plan[k].substring(0,letak-1));
				lsbs_id[k] = new Integer(Integer.parseInt(koderider));
				lsdbs_number[k] =Integer.parseInt(plan[k].substring(letak+1,plan[k].length()));				
				if (k == 0)
				{
					number_plan1 = lsdbs_number[k];
				}
				//cek hubungan dengan pemegang polis

				if (lsre_id[k].intValue() == 5)
				{
					flag_suamiistri = new Integer(flag_suamiistri.intValue() + 1);
				}
				
				if (flag_suamiistri.intValue() > 1)
				{
					if(lsbs_id[k].intValue()==820 || lsbs_id[k].intValue()==821 || lsbs_id[k].intValue()==823 || lsbs_id[k].intValue()==825|| lsbs_id[k].intValue()==826){
						
					}else{
						if (status.equalsIgnoreCase("windowhcp"))
						{
							err.rejectValue("datausulan.daftahcp["+k+"].lsre_id","","Relasi ke " +(k+1) +" tidak boleh lebih dari satu untuk suami istri, silahkan relasinya lagi.");
						}else{
							m.put("lsre_id", "Relasi ke " +(k+1) +" tidak boleh lebih dari satu untuk suami istri, silahkan ubah relasinya.");
						}
					}
				}else{
					if (lsre_id[k].intValue() == 5)
					{
						//(Deddy) tambahan untuk tambahan HCP, yang HCP dasar, tidak perlu divalidasi
						//Ada case pp istri, tt suami.HCP basicnya berarti utk suami, untuk HCP tambahan istri tidak bisa lewat validasi kalo tidak melewati row awal(HCP dasar)
						if(k==0){
							
						}else{
							if(lsbs_id[k].intValue()==820 || lsbs_id[k].intValue()==821 || lsbs_id[k].intValue()==823 || lsbs_id[k].intValue()==825 || lsbs_id[k].intValue()==826){
								
							}else{
								if((lsdbs_number[k].intValue() >= 21 && lsdbs_number[k].intValue() <= 40) || (lsdbs_number[k].intValue() >= 161 && lsdbs_number[k].intValue() <= 180) || (lsdbs_number[k].intValue() >=301 && lsdbs_number[k].intValue() <=320) || (lsdbs_number[k].intValue() >=391 && lsdbs_number[k].intValue() <=400)) {
									//21-40 atau 161-180 adl HCPF Spouse, 
								}else {
									if (status.equalsIgnoreCase("windowhcp"))
									{
										err.rejectValue("datausulan.daftahcp["+k+"].plan_rider","","Plan ke " +(k+1) +" tidak boleh dipilih untuk hubungan suami istri, silahkan memilih plan HCPF SPOUSE.");
									}else{
										m.put("plan_rider","Plan ke " +(k+1) +" tidak boleh dipilih untuk hubungan suami istri, silahkan memilih plan HCPF SPOUSE.");
									}
								}
							}
						}
					}
				}
				
				if (k > 0)
				{
					//1 - 20 atau 141 - 160 adalah HCP basic
					if (lsbs_id[k].intValue() == 819 
							&& ((lsdbs_number[k].intValue() >= 1 && lsdbs_number[k].intValue() <= 20) 
									|| (lsdbs_number[k].intValue() >= 141 && lsdbs_number[k].intValue() <= 160) || (lsdbs_number[k].intValue() >= 281 && lsdbs_number[k].intValue() <= 300) || (lsdbs_number[k].intValue() >= 381 && lsdbs_number[k].intValue() <= 390))){
						if (status.equalsIgnoreCase("windowhcp")){
							err.rejectValue("datausulan.daftahcp["+k+"].plan_rider","","Plan ke " +(k+1) +" tidak boleh dipilih. HCPF BASIC hanya bisa dipilih untuk tertanggung utama.");
						}else{
							m.put("plan_rider", "Plan ke " +(k+1) +" tidak boleh dipilih. HCPF BASIC hanya bisa dipilih untuk tertanggung utama.");
						}
					}
					//Yusuf - tertanggung boleh diri sendiri
//					if ((lsre_id[k].intValue() != 4) && (lsre_id[k].intValue() != 5) && (lsre_id[k].intValue() != 8))
//					{
//						if (status.equalsIgnoreCase("windowhcp"))
//						{
//							err.rejectValue("datausulan.daftahcp["+k+"].lsre_id","","Relasi ke " +(k+1) +" tidak boleh dipilih. Hanya bisa dipilih anak atau suami istri.");
//						}else{
//							m.put("lsre_id", "Relasi ke " +(k+1) +" tidak boleh dipilih. Hanya bisa dipilih anak atau suami istri.");
//						}
//					}
				}
				
				//cek nama
				String hsl_nama_m = "";
				if (nama[k] == null)
				{
					nama[k] = "";
				}
				if (nama[k].trim().length()==0)
				{
					hsl_nama_m="Nama ke " +(k+1) +" harus diisi.";
				}
				if (hsl_nama_m.trim().length()!=0)
				{
					if (status.equalsIgnoreCase("windowhcp"))
					{
						err.rejectValue("datausulan.daftahcp["+k+"].nama","",hsl_nama_m);
					}else{
						m.put("nama", hsl_nama_m);
					}
				}
					
				//cek tanggal lahir dan umur
				String hsl_tanggal_lahir = "";
				if (tanggal_lahir[k]==null)
				{
					hsl_tanggal_lahir="Tanggal Lahir ke " +(k+1) +" harus diisi.";
				}
				if (hsl_tanggal_lahir.trim().length()!=0)
				{
					umur[k] = new Integer(0);
					premi[k] =new Double(0);
					if (status.equalsIgnoreCase("windowhcp"))
					{
						err.rejectValue("datausulan.daftahcp["+k+"].tgl_lahir","",hsl_tanggal_lahir);
					}else{
						m.put("tgl_lahir", hsl_tanggal_lahir);
					}
				}else{
					//tgl lahir ttg
					Integer tanggal2=tanggal_lahir[k].getDate();
					Integer bulan2=tanggal_lahir[k].getMonth()+1;
					Integer tahun2=tanggal_lahir[k].getYear()+1900;
										
					//hit umur ttg, pp 
					f_hit_umur umr= new f_hit_umur();
					Integer li_umur_ttg =  umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);

					if ( produk.usia_nol == 1)
					{
						if (li_umur_ttg.intValue() == 0 )
						{
							li_umur_ttg = 1;
						}
					}
					produk.of_set_usia_tt(li_umur_ttg.intValue());
					umur[k] = li_umur_ttg;
					
					//edit.getTertanggung().setMste_age(li_umur_ttg);
					//edit.getDatausulan().setLi_umur_ttg((li_umur_ttg));
					
					//tgl lahir pp
					Integer tanggal3=edit.getPemegang().getMspe_date_birth().getDate();
					Integer bulan3=(edit.getPemegang().getMspe_date_birth().getMonth())+1;
					Integer tahun3=(edit.getPemegang().getMspe_date_birth().getYear())+1900;	
					
					Integer li_umur_pp = umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
					produk.of_set_usia_pp(li_umur_pp.intValue());
					
					produk.of_set_age();
					Integer age=new Integer(produk.ii_age);
					
					String hsl_umur = "";
					if (umur[k]==null)
					{
						hsl_umur="Umur ke " +(k+1) +" harus diisi, silahkan cek kembali tanggal lahir yang dimasukkan.";
						umur[k] = new Integer(0);
					}
					if (hsl_umur.trim().length()!=0)
					{
						if (status.equalsIgnoreCase("windowhcp"))
						{
							err.rejectValue("datausulan.daftahcp["+k+"].umur","",hsl_umur);
						}else{
							m.put("umur", hsl_umur);
						}
					}
					if(lsbs_id[k].intValue()<300){
						String hasil_cek_usia=produk.of_check_usia(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pay_period.intValue(),number_produk.intValue());
						if (hasil_cek_usia.trim().length()!=0)
						{
							if (status.equalsIgnoreCase("windowhcp"))
							{
								err.rejectValue("datausulan.daftahcp["+k+"].umur","",hasil_cek_usia);
							}else{
								m.put("umur", hasil_cek_usia);
							}
						}
					}
					
					if(plan[k].equalsIgnoreCase("0~X0"))
					{
						if (status.equalsIgnoreCase("windowhcp"))
						{
							err.rejectValue("datausulan.daftahcp["+k+"].plan_rider","","Plan ke " +(k+1) +" belum dipilih, silahkan memilih plan terlebih dahulu.");
						}else{
							m.put("plan_rider", "Plan ke " +(k+1) +" belum dipilih, silahkan memilih plan terlebih dahulu.");
						}
					}else{
						//n_prod dari rider
						if ((Integer.toString(lsbs_id[k].intValue())).trim().length()==1)
							{
								nama_rider[k]="produk_asuransi.n_prod_0"+lsbs_id[k];	
							}else{
								nama_rider[k]="produk_asuransi.n_prod_"+lsbs_id[k];	
							}
			
							if (!nama_rider[k].equalsIgnoreCase("produk_asuransi.n_prod_00"))
							{
								//cek rider 
								try{
									Class aClass = Class.forName(nama_rider[k]);
									n_prod produk1 = (n_prod)aClass.newInstance();
									produk1.setSqlMap(this.uwDao.getSqlMapClient());
		
									Boolean type_rider=new Boolean(false);
									produk1.li_lama=(ins_rider[k].intValue());
									li_pct_biaya = new Double(produk1.li_pct_biaya); 
									//set klas dan unit
									if (produk.flag_rider==0)
									{
										klases[k] = new Integer(produk1.set_klas(klases[k].intValue()));
										unit[k] = new Integer(produk1.set_unit(unit[k].intValue()));
									}
									
									//cek rider boleh dipilih atau ga, dibandingin dengan array di n_prod produk utama
									for ( int i=0 ; i< produk.indeks_rider_list; i++ )
									{
										if (lsbs_id[k].intValue()==produk.ii_rider[i])
										{
											type_rider=new Boolean(true);
										}
									}
									
									//produk number 6 atau 3 
									if (produk.flag_jenis_plan==1 && type_rider.booleanValue()==true)
									{
										if(lsbs_id[k].intValue()!=825){
											if (lsdbs_number[k].intValue()!= number_produk.intValue())
											{
												type_rider=new Boolean(false);
											}
										}
									}							
											
									if (type_rider.booleanValue()==false)
									{
										hasil_rider="Untuk Plan utama ini tidak bisa memilih rider ke "+(k+1);
										if (status.equalsIgnoreCase("windowhcp"))
										{
											err.rejectValue("datausulan.daftahcp["+k+"].plan_rider","",hasil_rider);
										}else{
											m.put("plan_rider", hasil_rider);
										}
									}else{
										hasil_rider = produk.cek_rider_number(lsdbs_number[k].intValue(),lsbs_id[k].intValue(),li_umur_ttg.intValue(),li_umur_pp.intValue(),up.doubleValue(),premi_utama.doubleValue(),pmode_id.intValue(),lsre_id[k].intValue(),kurs, pay_period);
										if (hasil_rider.trim().length()!=0)
										{
											if (status.equalsIgnoreCase("windowhcp"))
											{
												err.rejectValue("datausulan.daftahcp["+k+"].plan_rider","",hasil_rider);
											}else{
												m.put("plan_rider", hasil_rider);
											}
										}
										produk1.ii_bisnis_no=lsdbs_number[k].intValue();
										produk1.of_set_bisnis_no(produk1.ii_bisnis_no);
										produk1.ii_bisnis_id=lsbs_id[k].intValue();
										produk1.ii_bisnis_id_utama=lsbs_id[k].intValue();
										produk1.of_set_usia_tt(li_umur_ttg.intValue());
										produk1.of_set_usia_pp(li_umur_pp.intValue());
										produk1.of_set_pmode(pmode_id.intValue());
										produk1.li_lbayar=pay_period.intValue();
										produk1.is_kurs_id=kurs;
										if (unit[k]==null)
										{
											unit[k]=new Integer(0);
										}
										if (unit[k].intValue()==0)
										{
											produk1.set_unit(unit[k].intValue());
											unit[k]=new Integer(produk1.iiunit);
										}
										if (klases[k]==null)
										{
											klases[k] = new Integer(0);
										}
										if (klases[k].intValue()==0)
										{
											produk1.set_klas(klases[k].intValue());
											klases[k] = new Integer(produk1.iiclass);
										}
								
										end_date_rider[k]=defaultDateFormat.parse(FormatString.rpad("0",Integer.toString(tanggal.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan.intValue()),2)+"/"+Integer.toString(tahun.intValue()));

										beg_date_rider[k]=edit.getDatausulan().getMste_beg_date();
											
											if (hasil_rider.trim().length()==0)
											{
												int lama_bayar1 = ins_rider[k].intValue();
												hasil_rider=produk1.of_check_usia_rider(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),lama_bayar1,number_produk.intValue(),lsdbs_number[k].intValue());
												if (hasil_rider.trim().length()!=0)
												{
													if (status.equalsIgnoreCase("windowhcp"))
													{
														err.rejectValue("datausulan.daftahcp["+k+"].plan_rider","", hasil_rider);
													}else{
														m.put("plan_rider", hasil_rider);
													}
												}else{
													if (flag_gutri.intValue() == 0 && mste_flag_guthrie.intValue() == 0)
													{
														hasil_rider=produk1.cek_hubungan(Integer.toString(lsre_id[k].intValue()));
													}
														if (hasil_rider.trim().length()!=0)
														{
															if (status.equalsIgnoreCase("windowhcp"))
															{
																err.rejectValue("datausulan.daftahcp["+k+"].plan_rider","", hasil_rider);
															}else{
																m.put("plan_rider", hasil_rider);
															}
														}else{
															if (hasil_rider.trim().length()!=0)
															{
																hasil_rider=hasil_rider +(k+1);
																if (status.equalsIgnoreCase("windowhcp"))
																{
																	err.rejectValue("datausulan.daftahcp["+k+"].plan_rider","", hasil_rider);
																}else{
																	m.put("plan_rider", hasil_rider);
																}
															}else{
																hasil_class1=produk1.range_class(li_umur_ttg.intValue(),klases[k].intValue());
																if (hasil_class1.trim().length()!=0)
																{
																	String hasil_class=hasil_class1+" "+(k+1);
																	if (status.equalsIgnoreCase("windowhcp"))
																	{
																		err.rejectValue("datausulan.daftahcp["+k+"].mspr_class","", hasil_class);
																	}else{
																		m.put("mspr_class", hasil_class);
																	}
																}
													
																hasil_unit1=produk1.range_unit(unit[k].intValue());
																if (hasil_unit1.trim().length()!=0)
																{
																	hasil_unit1=hasil_unit1+" "+(k+1);
																	if (status.equalsIgnoreCase("windowhcp"))
																	{
																		err.rejectValue("datausulan.daftahcp["+k+"].mspr_unit","", hasil_unit1);
																	}else{
																		m.put("mspr_unit", hasil_unit1);
																	}
																}
															}
														}
												}
											}
											// cek hcpf selain basic harus sama typenya dengan basic
											if (k>0)
											{
												if (hasil_rider.trim().length()==0)
												{
													Integer number =  new Integer(0);
													 if (lsdbs_number[k] == null)
													 {
														 number = new Integer(0);
													 }else{
														 number = lsdbs_number[k];
													 }
													int jumlah_sementara = number.intValue() - number_plan1.intValue();
													int hasil_mod = jumlah_sementara % 20;
													if(number_plan1>380){
														hasil_mod = jumlah_sementara % 10;
													}
													if (hasil_mod  > 0)
													{
														if (status.equalsIgnoreCase("windowhcp"))
														{
															if(koderider.equals("820") || koderider.equals("821") || koderider.equals("823") || koderider.equals("825") || koderider.equals("826")){
																
															}else{
															err.rejectValue("datausulan.daftahcp["+k+"].plan_rider","", "Plan ke "+(k+1)+" tidak boleh berbeda jenis R / D nya dengan plan BASIC");
															}
														}else{
															m.put("plan_rider", "Plan ke "+(k+1)+" tidak boleh berbeda jenis R / D nya dengan plan BASIC");
														}
													}
												}
												if(lsbs_id[k].intValue()!=820 || lsbs_id[k].intValue()!=821 || lsbs_id[k].intValue()!=823 || lsbs_id[k].intValue()!=825 || lsbs_id[k].intValue()!=826){
												
												}else { 
													if (lsdbs_number[k].intValue() <= 20)
													{
														if (status.equalsIgnoreCase("windowhcp"))
														{
															err.rejectValue("datausulan.daftahcp["+k+"].plan_rider","", "Plan ke "+(k+1)+" tidak boleh berbeda jenis R / D nya dengan plan BASIC");
														}else{
															m.put("plan_rider", "Plan ke "+(k+1)+" tidak boleh ditambahkan, penambahan plan BASIC hanya boleh di window utama penginputan spaj. ");
														}
													}
												}
											}
										}
										
									// cari rate, up ,premi , persentase rider
										rate[k]=new Double(produk1.of_get_rate1(klases[k].intValue(),produk.flag_jenis_plan,lsdbs_number[k].intValue(),li_umur_ttg.intValue(),li_umur_pp.intValue() ));	
										Double up_sementara= up;
										up_sementara=	produk1.cek_maks_up_rider(up.doubleValue(),produk1.is_kurs_id);
										if(lsbs_id[k].intValue()==820 || lsbs_id[k].intValue()==823 || lsbs_id[k].intValue()==825 || lsbs_id[k].intValue()==826 || lsbs_id[k].intValue()==821 || lsbs_id[k].intValue()==838){
											premi[k]=new Double(produk1.hit_premi_rider(rate[k].doubleValue(),up_sementara,produk1.idec_pct_list[pmode_id.intValue()],premi[k].doubleValue()));	
										}else premi[k]=new Double(produk1.hit_premi_rider(rate[k].doubleValue(),up_sementara,produk1.idec_pct_list[pmode_id.intValue()],premi_utama.doubleValue()));			
										percent[k]=new Integer(0);
				
										Integer tanggal_akhir_bayar1=new Integer(0);
										Integer bulan_akhir_bayar1 = new Integer(0);
										Integer tahun_akhir_bayar1=new Integer(0);
										String tgl_akhir_bayar1 ="";
										String tanggal_1_1="";
										
										Integer tanggal_akhir_polis1=null;
										Integer bulan_akhir_polis1=null;
										Integer tahun_akhir_polis1 = null;
										String tgl_akhir_polis1=null;
										Integer flag_platinumlink = new Integer(produk.flag_platinumlink);
										if (flag_platinumlink == null)
										{
											flag_platinumlink = new Integer(0);
										}
										edit.getDatausulan().setFlag_platinumlink(flag_platinumlink);
										if (flag_platinumlink.intValue()==1 && !(kode_produk.intValue()==134 && number_produk>=5))
										{
											//rider platinum link tanggal end date berdasarkan cuti premi
											int cuti_premi = edit.getDatausulan().getMspo_installment().intValue();
											produk.wf_set_rider(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(), cuti_premi,li_umur_ttg.intValue(),li_umur_pp.intValue());
											tanggal_akhir_polis1=new Integer(produk.ldt_edate.getTime().getDate());
											bulan_akhir_polis1=new Integer(produk.ldt_edate.getTime().getMonth()+1);
											tahun_akhir_polis1=new Integer(produk.ldt_edate.getTime().getYear()+1900);
											tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
											tgl_awal_rider = new Integer(produk.adt_bdate.getTime().getDate());
											bln_awal_rider = new Integer(produk.adt_bdate.getTime().getMonth()+1);
											thn_awal_rider = new Integer(produk.adt_bdate.getTime().getYear()+1900);
											tanggal_awal_rider = FormatString.rpad("0",Integer.toString(tgl_awal_rider.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bln_awal_rider.intValue()),2)+"/"+Integer.toString(thn_awal_rider.intValue());
											
											if ( number_produk.intValue()==2)
											{
												beg_date_rider[k]=defaultDateFormat.parse(tanggal_awal_rider);
											}
											pp.setMspr_beg_date(beg_date_rider[k]);
											
											if (produk1.ldt_epay!=null)
											{
												tanggal_akhir_bayar1=new Integer(produk.ldt_epay.getTime().getDate());
												bulan_akhir_bayar1=new Integer(produk.ldt_epay.getTime().getMonth()+1);
												tahun_akhir_bayar1=new Integer(produk.ldt_epay.getTime().getYear()+1900);
												tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
												tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;					
											}else{
												tgl_akhir_bayar1="";
												tanggal_1_1="";
											}
											
										}else{
											int flag_sementara = produk.flag_jenis_plan;
											if(kode_produk.intValue()==134 && number_produk>=5){
												flag_sementara = 0;
											}
											if (produk.flag_worksite == 1)
											{
												/*if (produk.flag_medivest == 1)
												{
													flag_sementara = 5;
												}*/
											}
											
											//nentuin akhir pertanggungan , akhir pembayaran rider
											produk1.wf_set_premi(tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pmode_id.intValue(),tahun.intValue(),bulan.intValue(),tanggal.intValue(),ins_period.intValue(),flag_sementara,age.intValue(),pay_period.intValue(),produk.flag_cerdas_siswa, li_umur_pp.intValue(),produk1.ii_bisnis_id,produk1.ii_bisnis_no);
													
											tanggal_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getDate());
											bulan_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getMonth()+1);
											tahun_akhir_polis1=new Integer(produk1.ldt_edate.getTime().getYear()+1900);
											tgl_akhir_polis1=FormatString.rpad("0",Integer.toString(tanggal_akhir_polis1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_polis1.intValue()),2)+"/"+Integer.toString(tahun_akhir_polis1.intValue());
																		
											if (produk1.ldt_epay!=null)
											{
												tanggal_akhir_bayar1=new Integer(produk1.ldt_epay.getTime().getDate());
												bulan_akhir_bayar1=new Integer(produk1.ldt_epay.getTime().getMonth()+1);
												tahun_akhir_bayar1=new Integer(produk1.ldt_epay.getTime().getYear()+1900);
												tgl_akhir_bayar1=Integer.toString(tanggal_akhir_bayar1.intValue())+"/"+Integer.toString(bulan_akhir_bayar1.intValue())+"/"+Integer.toString(tahun_akhir_bayar1.intValue());
												tanggal_1_1=FormatString.rpad("0",Integer.toString(tanggal_akhir_bayar1.intValue()),2)+"/"+FormatString.rpad("0",Integer.toString(bulan_akhir_bayar1.intValue()),2)+"/"+Integer.toString(tahun_akhir_bayar1.intValue())	;					
												if (produk.flag_jenis_plan == 6)
												{
													tgl_akhir_bayar1=tgl_akhir_polis1;
													tanggal_1_1=tgl_akhir_polis1;
												}
											}else{
												tgl_akhir_bayar1="";
												tanggal_1_1="";
											}
											pp.setMspr_beg_date(edit.getDatausulan().getMste_beg_date() );
										}
										
										
										if ( tgl_akhir_polis1.trim().length()!=0)
										{
											if(defaultDateFormat.parse(tgl_akhir_polis1).after(end_date_rider[k]) ){
												pp.setMspr_end_date(end_date_rider[k]);
											}else{
												pp.setMspr_end_date(defaultDateFormat.parse(tgl_akhir_polis1));
											}
										}else{
											pp.setMspr_end_date(null);
										}
										 if (tgl_akhir_bayar1.trim().length()!=0)
										 {
											 pp.setMspr_end_pay(defaultDateFormat.parse(tgl_akhir_bayar1));
										 }else{
											 pp.setMspr_end_pay(null);
										 }
										 if (produk.flag_platinumlink==1 && !(kode_produk.intValue()==134 && number_produk>=5))
										 {
											 int cuti_premi = edit.getDatausulan().getMspo_installment().intValue();
											 pp.setMspr_ins_period(new Integer(produk.li_insured));
										 }else{
											 pp.setMspr_ins_period(new Integer(produk1.li_insured));
										 }
									
													
									sum[k]= new Double(produk1.of_get_up(premi_utama.doubleValue(),up.doubleValue(),unit[k].intValue(),produk.flag_jenis_plan,lsbs_id[k].intValue(),lsdbs_number[k].intValue(),flag_platinum));
									sum[k] = produk1.cek_maks_up_rider(sum[k],produk1.is_kurs_id);
									up_pa_rider[k] = new Double(produk1.up_pa);	
									up_pb_rider[k] = new Double(produk1.up_pb);	
									up_pc_rider[k] = new Double(produk1.up_pc);	
									up_pd_rider[k] = new Double(produk1.up_pd);	
									up_pm_rider[k] = new Double(produk1.up_pm);	
									
									pp.setMspr_tsi(sum[k]);
									pp.setLku_id(kurs);
									pp.setMspr_rate(rate[k]);
		

										if (produk.flag_rider==1)
										{
											if (produk.flag_platinumlink ==0 && (kode_produk.intValue()!=190 && kode_produk.intValue()!=191))
											{
												// selain platinum link semua rider premi persentase 0 karena ada biaya
												//Yusuf (25/02/2008) Khusus Hidup Bahagia, Dana Sejahtera New dan simas maxi save , ridernya ada preminya!
												if(produk.ii_bisnis_id == 167 || produk.ii_bisnis_id == 163 || produk.ii_bisnis_id==191 || produk.ii_bisnis_id == 188 || produk.ii_bisnis_id==194) { 
													BigDecimal bulat = new BigDecimal(premi[k]);
													bulat = bulat.setScale(0,BigDecimal.ROUND_HALF_UP);
													pp.setMspr_premium(bulat.doubleValue());
												}else {
													pp.setMspr_premium(new Double(0));
												}
												pp.setMspr_persen(percent[k]);
												pp.setMspr_tt(insured[k]);
											}else{
												// platinum link semua rider premi persentase ada nilainya karena tidak ada biaya
//												if ( number_produk.intValue()==1)
												if ( produk.flag_platinumlink==1 || (produk.ii_bisnis_id==166 && produk.ii_bisnis_no==2) || (produk.ii_bisnis_id==134 && produk.ii_bisnis_no==3) )
												{
													produk1.count_rate(klases[k].intValue(),unit[k].intValue(),kode_produk.intValue(),lsdbs_number[k].intValue(),kurs,li_umur_ttg.intValue(),li_umur_pp.intValue(),up.doubleValue(),premi_utama.doubleValue(),pmode_id.intValue(),1,ins_period.intValue(),pay_period.intValue());
													String mbu_jml="";
													Double mbu_jumlah=new Double(produk1.mbu_jumlah);
													BigDecimal mbu1 = new BigDecimal(mbu_jumlah.doubleValue());
													mbu1=mbu1.setScale(2,BigDecimal.ROUND_HALF_UP);
													mbu_jumlah=new Double(mbu1.doubleValue());
													mbu_jml=Double.toString(mbu_jumlah.doubleValue());
													premi[k] = mbu_jumlah;
													pp.setMspr_premium(premi[k]);
													pp.setMspr_persen(percent[k]);
													pp.setMspr_tt(insured[k]);		
													
													mbu_jumlah=new Double(produk1.mbu_persen);
													mbu1 = new BigDecimal(mbu_jumlah.doubleValue());
													mbu1=mbu1.setScale(2,BigDecimal.ROUND_HALF_UP);
													mbu_jumlah=new Double(mbu1.doubleValue());
													mbu_jml=Double.toString(mbu_jumlah.doubleValue());
													pp.setMspr_rate(mbu_jumlah);
												}else{
													pp.setMspr_premium(new Double(0));
													pp.setMspr_persen(percent[k]);
													pp.setMspr_tt(insured[k]);
												}
											}
										}else{
											pp.setMspr_premium(premi[k]);
											pp.setMspr_persen(percent[k]);
											pp.setMspr_tt(insured[k]);	
										}

									total_premi_rider=new Double(total_premi_rider.doubleValue() + pp.getMspr_premium().doubleValue());	

							}	//tutup try produk rider
							
							catch (ClassNotFoundException e)
							{
								logger.error("ERROR :", e);
							} catch (InstantiationException e) {
								logger.error("ERROR :", e);
							} catch (IllegalAccessException e) {
								logger.error("ERROR :", e);
							}	
						}
					}

				}
				//set data k model Hcp
				pp.setPlan_rider(plan[k]);
				pp.setMspr_unit(unit[k]);
				pp.setMspr_class(klases[k]);
				pp.setMspr_tsi_pa_a(up_pa_rider[k]);
				pp.setMspr_tsi_pa_b(up_pb_rider[k]);
				pp.setMspr_tsi_pa_c(up_pc_rider[k]);
				pp.setMspr_tsi_pa_d(up_pd_rider[k]);
				pp.setMspr_tsi_pa_m(up_pm_rider[k]);
				pp.setLsbs_id(lsbs_id[k]);
				pp.setLsdbs_number(lsdbs_number[k]);
				pp.setPlan_rider(plan[k]);
				pp.setMspr_extra(mspr_extra[k]);
				pp.setLspc_no(lspc_no[k]);
				
				pp.setKelamin(kelamin[k]);
				pp.setLsre_id(lsre_id[k]);
				pp.setNama(nama[k]);
				pp.setNo_urut(no_urut[k]);
				pp.setPremi(premi[k]);
				pp.setReg_spaj(reg_spaj[k]);
				pp.setTgl_lahir(tanggal_lahir[k]);
				pp.setUmur(umur[k]);
				
				Simas bf= new Simas();
				bf.setKelamin(pp.getKelamin());
				if(bf1.getLsbs_id().intValue()==183 || bf1.getLsbs_id().intValue()==189 || bf1.getLsbs_id().intValue()==193 || bf1.getLsbs_id().intValue()==195 || bf1.getLsbs_id().intValue()==204){
					bf.setLsbs_id(bf1.getLsbs_id());
					bf.setLsdbs_number(bf1.getLsdbs_number());
				}else{
					bf.setLsbs_id(pp.getLsbs_id());
					bf.setLsdbs_number(pp.getLsdbs_number());
				}
				bf.setLsre_id(pp.getLsre_id());
				bf.setNama(pp.getNama());
				bf.setNo_urut(pp.getNo_urut());
				bf.setPlan_rider(pp.getPlan_rider());
				bf.setPremi(pp.getPremi());
				bf.setReg_spaj(pp.getReg_spaj());
				bf.setTgl_lahir(pp.getTgl_lahir());
				bf.setUmur(pp.getUmur());
				if (pp.getLsbs_id() == 819 && 
						((pp.getLsdbs_number() >= 1 && pp.getLsdbs_number() <= 20) || (pp.getLsdbs_number() >= 141 && pp.getLsdbs_number() <= 160) || (pp.getLsdbs_number() >= 281 && pp.getLsdbs_number() <= 300) || (pp.getLsdbs_number() >= 381 && pp.getLsdbs_number() <= 390))){
					bf.setDiscount(new Double(0));
				}else{
					bf.setDiscount(new Double(10));
				}
				
				Datarider bf2 = new Datarider();
				bf2.setLku_id(pp.getLku_id());
				bf2.setLsbs_id(pp.getLsbs_id());
				bf2.setLsdbs_number(pp.getLsdbs_number());
				bf2.setMspr_beg_date(pp.getMspr_beg_date());
				bf2.setMspr_class(pp.getMspr_class());
				bf2.setMspr_end_date(pp.getMspr_end_date());
				bf2.setMspr_end_pay(pp.getMspr_end_pay());
				bf2.setMspr_extra(pp.getMspr_extra());
				bf2.setMspr_ins_period(pp.getMspr_ins_period());
				bf2.setMspr_persen(pp.getMspr_persen());
				bf2.setMspr_premium(pp.getMspr_premium());
				bf2.setMspr_rate(pp.getMspr_rate());
				bf2.setMspr_tsi(pp.getMspr_tsi());
				bf2.setMspr_tsi_pa_a(pp.getMspr_tsi_pa_a());
				bf2.setMspr_tsi_pa_b(pp.getMspr_tsi_pa_b());
				bf2.setMspr_tsi_pa_c(pp.getMspr_tsi_pa_c());
				bf2.setMspr_tsi_pa_d(pp.getMspr_tsi_pa_d());
				bf2.setMspr_tsi_pa_m(pp.getMspr_tsi_pa_m());
				bf2.setMspr_tt(pp.getMspr_tt());
				bf2.setMspr_unit(pp.getMspr_unit());
				bf2.setPlan_rider(pp.getPlan_rider());
				
				peserta2.add(pp);
				peserta1.add(bf);
				rider.add(bf2);
				

			}
				edit.getDatausulan().setDaftahcp(peserta2);
				edit.getDatausulan().setDaftapeserta(peserta1);
				edit.getDatausulan().setDaftariderall(rider);
				edit.getDatausulan().setDaftariderhcp(rider);
				edit.getDatausulan().setJml_peserta(jmlh_peserta);
				List daftar_rider = edit.getDatausulan().getDaftaRider();
				
				List data_sementara = new ArrayList();
				//balikin set ke daftar rider
				Integer index_daftar_rider = new Integer(0);
				if (daftar_rider != null)
				{
					index_daftar_rider = new Integer(daftar_rider.size());
				}
				Integer index_rider = new Integer(0);
				if (rider != null)
				{
					index_rider = new Integer(rider.size());
				}
				for (int k = 0 ; k < index_daftar_rider.intValue() ; k++)
				{
					Datarider dr = (Datarider)daftar_rider.get(k);
					
					// rider sekalin hcpf
					if (dr.getLsbs_id().intValue() != 819 && dr.getLsbs_id().intValue() != 826)
					{
						Datarider dr2 = new Datarider();
						dr2 = dr;
						data_sementara.add(dr2);
					}else{
						// rider hcpf saja
						for (int n= 0 ; n < index_rider.intValue() ; n ++)
						{
							Datarider dr1 = (Datarider)rider.get(n);
							if ((dr.getLsdbs_number().intValue() == dr1.getLsdbs_number().intValue()))
							{
								Datarider dr2 = new Datarider();
								dr2 = dr1;
								data_sementara.add(dr2);	
							}
						}
					}
				}
				edit.getDatausulan().setDaftaRider(data_sementara);
				Integer index_data = new Integer(0);
				if (data_sementara != null)
				{
					index_data = new Integer(data_sementara.size());
				}
				
				//validasi hcpf
				if (index_data.intValue() >0)
				{
					for (int r=0 ; r <(data_sementara.size()); r++)
					{
						Datarider rd1 = (Datarider)data_sementara.get(r);
						
						for (int w=(r+1); w <=(index_data.intValue()-1); w++)
						{
							Datarider rd2 = (Datarider)data_sementara.get(w);
							Integer lsdbs_number_sementara = rd1.getLsdbs_number();
							if(rd1.getLsdbs_number().intValue()>16 && (rd1.getLsbs_id()==183 || rd1.getLsbs_id()==193)){
								lsdbs_number_sementara -=15;
							}
							if (lsdbs_number_sementara==rd2.getLsdbs_number().intValue())
							{
								if ((rd1.getLsdbs_number().intValue() >= 21 && rd1.getLsdbs_number().intValue() < 41) || (rd1.getLsdbs_number().intValue() >= 301 && rd1.getLsdbs_number().intValue() < 321) || (rd1.getLsdbs_number().intValue() >= 391 && rd1.getLsdbs_number().intValue() < 401) )
								{
									if (status.equalsIgnoreCase("windowhcp"))
									{
										err.rejectValue("datausulan.daftahcp["+w+"].plan_rider",""," plan ke "+(w+1)+" tidak boleh sama. Cukup 1 yang mengambil SPOUSE.");
									}else{
										m.put("plan_rider", " plan ke "+(w+1)+" tidak boleh sama. Cukup 1 yang mengambil SPOUSE.");
									}
								}else if (rd1.getLsdbs_number().intValue() >= 41 )
									{
										if (status.equalsIgnoreCase("windowhcp"))
										{
											err.rejectValue("datausulan.daftahcp["+w+"].plan_rider",""," plan ke "+(w+1)+" tidak boleh sama. Silahkan memilih CHILD yang kesekian untuk jenis R / D yang sama.");
										}else{
											m.put("plan_rider"," plan ke "+(w+1)+" tidak boleh sama. Silahkan memilih CHILD yang kesekian untuk jenis R / D yang sama.");
										}
									}
							}
						}
					}
				}
				
					total_premi_sementara=new Double(total_premi_rider.doubleValue()+premi_utama.doubleValue() + total_premi_tu.doubleValue());
					edit.getDatausulan().setTotal_premi_rider((total_premi_rider));
					edit.getDatausulan().setTotal_premi_sementara((total_premi_sementara));
					edit.getInvestasiutama().setTotal_premi_sementara(total_premi_sementara);

					Map data2 = this.proseshitunghcp(edit, err, kode_flag, flag_rider, pmode_id, kurs, premi_utama, up, premi_berkala, premi_tunggal, flag_as, pil_berkala, pil_tunggal);
					
					Double ld_premi_invest = (Double)data2.get("ld_premi_invest");
					flag_minus1 = (Boolean)data2.get("flag_minus");
					Double jumlah_minus = (Double)data2.get("jumlah_minus");
					
					String hasil_biaya = (String)data2.get("total_persen");
					if (hasil_biaya !=null)
					{
						if (!hasil_biaya.equalsIgnoreCase(""))
						{
							if (status.equalsIgnoreCase("windowhcp"))
							{
								err.rejectValue("investasiutama.total_persen","",hasil_biaya);	
							}else{
								m.put("total_persen",hasil_biaya);
							}
						}
					}
					
					String mu_jlh_tu = (String)data2.get("daftartopup.premi_tunggal");
					if (mu_jlh_tu !=null)
					{
						if (!mu_jlh_tu.equalsIgnoreCase(""))
						{
							if (status.equalsIgnoreCase("windowhcp"))
							{
								err.rejectValue("investasiutama.daftartopup.premi_tunggal","",mu_jlh_tu);
							}else{
								m.put("premi_tunggal",mu_jlh_tu);
							}
						}
					}
					
					mu_jlh_tu = (String)data2.get("daftartopup.premi_berkala");
					if (mu_jlh_tu !=null)
					{
						if (!mu_jlh_tu.equalsIgnoreCase(""))
						{
							if (status.equalsIgnoreCase("windowhcp"))
							{
								err.rejectValue("investasiutama.daftartopup.premi_berkala","",mu_jlh_tu);
							}else{
								m.put("premi_berkala",mu_jlh_tu);
							}
						}
					}
					
					String mu_periodic_tu= (String)data2.get("daftartopup.pil_tunggal");
					if (mu_periodic_tu !=null)
					{
						if (!mu_periodic_tu.equalsIgnoreCase(""))
						{
							if (status.equalsIgnoreCase("windowhcp"))
							{
								err.rejectValue("investasiutama.daftartopup.pil_tunggal","",mu_periodic_tu);
							}else{
								m.put("pil_tunggal",mu_periodic_tu);
							}
						}
					}
					
					mu_periodic_tu= (String)data2.get("daftartopup.pil_berkala");
					if (mu_periodic_tu !=null)
					{
						if (!mu_periodic_tu.equalsIgnoreCase(""))
						{
							if (status.equalsIgnoreCase("windowhcp"))
							{
								err.rejectValue("investasiutama.daftartopup.pil_berkala","",mu_periodic_tu);
							}else{
								m.put("pil_berkala",mu_periodic_tu);
							}
						}
					}
					
					Map data3 = this.proseshitungfund(edit, err, li_pct_biaya, kode_flag, ld_premi_invest, flag_minus1, premi_berkala, premi_tunggal, jumlah_minus);
			
					String hasil_total= (String)data3.get("total_persen");
					if (hasil_total !=null)
					{
						if (!hasil_total.equalsIgnoreCase(""))
						{
							if (status.equalsIgnoreCase("windowhcp"))
							{
								err.rejectValue("investasiutama.total_persen","",hasil_total);
							}else{
								m.put("total_persen",hasil_total);
							}
						}
					}
		 
		}
		catch (ClassNotFoundException e)
		{
			logger.error("ERROR :", e);
		} catch (InstantiationException e) {
			logger.error("ERROR :", e);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		} catch (ServletException e) {
			logger.error("ERROR :", e);
		} catch (IOException e) {
			logger.error("ERROR :", e);
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
		return m;
	}
	
	private Map proc_hit_additional_unit(Cmdeditbac cmd,Map m, String kurs,Double premi_pokok)throws ServletException,IOException {
		Double premi_pokok_additional_unit = 0.;
		premi_pokok_additional_unit = premi_pokok * 0.4;
		m.put("premi_pokok_additional_unit", premi_pokok_additional_unit);
		return m;
	}
	
	
	private Map proc_validasi_simas(Cmdeditbac edit,String status,Map m,Errors err)throws ServletException,IOException
	{
		List peserta = edit.getDatausulan().getDaftapeserta();
		Integer jumlah_r =new Integer(peserta.size());
		if (jumlah_r==null)
		{
			jumlah_r=new Integer(0);
		}
		Integer jmlh_peserta=jumlah_r;
		edit.getDatausulan().setJml_peserta(new Integer(jmlh_peserta.intValue()));
		
		if (jumlah_r.intValue() > 0)
		{
			List peserta1 = new ArrayList();
			Simas data1 = new Simas();
			for (int i = 0 ; i < jumlah_r.intValue() ; i++)
			{
				//untuk set mst keluarga
				Simas data = (Simas)peserta.get(i);
				if (data.getNo_urut().intValue() == 1)
				{
					data1.setDiscount(null);
					data1.setKelamin(edit.getTertanggung().getMspe_sex());
					data1.setLsbs_id(edit.getDatausulan().getLsbs_id());
					data1.setLsdbs_number(edit.getDatausulan().getLsdbs_number());
					data1.setLsre_id(edit.getTertanggung().getLsre_id());
					data1.setNama(edit.getTertanggung().getMcl_first());
					data1.setNo_urut(new Integer(1));
					data1.setPremi(edit.getDatausulan().getMspr_premium());
					data1.setPlan_rider(edit.getDatausulan().getPlan());
					data1.setReg_spaj(edit.getTertanggung().getReg_spaj());
					data1.setTgl_lahir(edit.getTertanggung().getMspe_date_birth());
					data1.setUmur(edit.getTertanggung().getMste_age());
				}else{
					data1 = data;
				}
				peserta1.add(data1);
				
			}
			edit.getDatausulan().setDaftapeserta(peserta1);
			peserta = edit.getDatausulan().getDaftapeserta();
		}

		Integer kode_produk=null;
		Integer number_produk = null;
		kode_produk = edit.getDatausulan().getLsbs_id();
		number_produk = edit.getDatausulan().getLsdbs_number();
		String nama_produk="";
		String hasil_bisnis="";
	
		if (Integer.toString(kode_produk.intValue()).trim().length()==1)
		{
			nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
		}else{
			nama_produk="produk_asuransi.n_prod_"+kode_produk;	
		}
		try{
			Class aClass1 = Class.forName( nama_produk );
			n_prod produk1 = (n_prod)aClass1.newInstance();
			produk1.setSqlMap(this.uwDao.getSqlMapClient());
			produk1.cek_flag_agen(kode_produk.intValue(),number_produk.intValue(), 0);

			produk1.of_set_bisnis_no(number_produk.intValue());
			produk1.ii_bisnis_no=(number_produk.intValue());
			Integer pay_period = edit.getDatausulan().getMspo_pay_period();
			
			Integer tanggal1=null;
			Integer bulan1=null;
			Integer tahun1 =null;
			//tgl beg date
			tanggal1= edit.getDatausulan().getMste_beg_date().getDate();
			bulan1 = (edit.getDatausulan().getMste_beg_date().getMonth())+1;
			tahun1 = (edit.getDatausulan().getMste_beg_date().getYear())+1900;
			
			
			Integer[] no_urut;                       
			Integer[] lsre_id;                       
			String[] nama;    
			Integer[] kelamin;                       
			Integer[] umur;                          
			Double[] premi;                          
			String[] reg_spaj;
			Date[] tanggal_lahir;
			
			no_urut = new Integer[(jmlh_peserta.intValue())+1];
			lsre_id = new Integer[(jmlh_peserta.intValue())+1];
			nama = new String[(jmlh_peserta.intValue())+1];
			kelamin = new Integer[(jmlh_peserta.intValue())+1];
			umur = new Integer[(jmlh_peserta.intValue())+1];
			premi = new Double[(jmlh_peserta.intValue())+1];
			reg_spaj = new String[(jmlh_peserta.intValue())+1];
			tanggal_lahir = new Date[(jmlh_peserta.intValue())+1];
			
			List peserta2 = new ArrayList();
			
			
			//validasi khusus produk simas
			for (int k=0 ; k < (jmlh_peserta.intValue()) ; k++)
			{
				Simas bf1= (Simas)peserta.get(k);
				
				no_urut[k]=bf1.getNo_urut();
				lsre_id[k]=bf1.getLsre_id();				
				nama[k]=bf1.getNama();
				tanggal_lahir[k]=bf1.getTgl_lahir();	
				kelamin[k] = bf1.getKelamin();
				umur[k] = bf1.getUmur();
				premi[k] = bf1.getPremi();
				reg_spaj[k] = bf1.getReg_spaj();
				tanggal_lahir[k] = bf1.getTgl_lahir();
				
				String hsl_nama_m = "";
				if (nama[k].trim().length()==0)
				{
					hsl_nama_m="Nama ke " +(k+1) +" harus diisi.";
				}
				if (hsl_nama_m.trim().length()!=0)
				{
					if (status.equalsIgnoreCase("windowsimas"))
					{
						err.rejectValue("datausulan.daftapeserta["+k+"].nama","",hsl_nama_m);
					}else{
						m.put("nama", hsl_nama_m);
					}
				}
				
				String hsl_tanggal_lahir = "";
				if (tanggal_lahir[k]==null)
				{
					hsl_tanggal_lahir="Tanggal Lahir ke " +(k+1) +" harus diisi.";
				}
				if (hsl_tanggal_lahir.trim().length()!=0)
				{
					umur[k] = new Integer(0);
					premi[k] =new Double(0);
					if (status.equalsIgnoreCase("windowsimas"))
					{
						err.rejectValue("datausulan.daftapeserta["+k+"].tgl_lahir","",hsl_tanggal_lahir);
					}else{
						m.put("tgl_lahir", hsl_tanggal_lahir);
					}
				}else{
					//tgl lahir ttg
					Integer tanggal2=tanggal_lahir[k].getDate();
					Integer bulan2=tanggal_lahir[k].getMonth()+1;
					Integer tahun2=tanggal_lahir[k].getYear()+1900;
									
					//hit umur ttg, pp 
					f_hit_umur umr= new f_hit_umur();
					Integer li_umur_ttg =  umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1);
					if ( produk1.usia_nol == 1)
					{
						if (li_umur_ttg.intValue() == 0 )
						{
							li_umur_ttg = 1;
						}
					}
					produk1.of_set_usia_tt(li_umur_ttg.intValue());
					umur[k] = li_umur_ttg;
					
					edit.getTertanggung().setMste_age(li_umur_ttg);
					edit.getDatausulan().setLi_umur_ttg((li_umur_ttg));
					
					//tgl lahir pp
					Integer tanggal3=edit.getPemegang().getMspe_date_birth().getDate();
					Integer bulan3=(edit.getPemegang().getMspe_date_birth().getMonth())+1;
					Integer tahun3=(edit.getPemegang().getMspe_date_birth().getYear())+1900;	
					
					Integer li_umur_pp = umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
					produk1.of_set_usia_pp(li_umur_pp.intValue());
					
					produk1.of_set_age();
					//Integer age=new Integer(produk1.ii_age);
					
					String hsl_umur = "";
					if (umur[k]==null)
					{
						hsl_umur="Umur ke " +(k+1) +" harus diisi.";
						umur[k] = new Integer(0);
					}
					if (hsl_umur.trim().length()!=0)
					{
						if (status.equalsIgnoreCase("windowsimas"))
						{
							err.rejectValue("datausulan.daftapeserta["+k+"].umur","",hsl_umur);
						}else{
							m.put("umur", hsl_umur);
						}
					}
					
					String hasil_cek_usia=produk1.of_check_usia(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pay_period.intValue(),number_produk.intValue());
					if (hasil_cek_usia.trim().length()!=0)
					{
						if (status.equalsIgnoreCase("windowsimas"))
						{
							err.rejectValue("datausulan.daftapeserta["+k+"].umur","",hasil_cek_usia);
						}else{
							m.put("umur", hasil_cek_usia);
						}
					}
					Integer pmode_id = edit.getDatausulan().getLscb_id();
					Double up = edit.getDatausulan().getMspr_tsi();
					produk1.of_set_up(up.doubleValue());	
					produk1.of_set_pmode(pmode_id.intValue());
					pay_period = new Integer(produk1.of_get_payperiod());
					if (produk1.flag_uppremi==0)
					{
						Double rate_plan=new Double(produk1.of_get_rate());
						if (rate_plan.doubleValue()==0 && produk1.flag_endowment == 1)
						{
							if (status.equalsIgnoreCase("windowsimas"))
							{
								err.rejectValue("datausulan.daftapeserta["+k+"].premi","","Rate Premi ke " +(k+1) +" ini belum ada.");
							}else{
								m.put("premi", "Rate Premi ke " +(k+1) +" ini belum ada.");
							}
						}
						Double premi1=new Double(produk1.idec_premi_main);

						if (premi1.doubleValue() == 0)
						{
							if (status.equalsIgnoreCase("windowsimas"))
							{
								err.rejectValue("datausulan.daftapeserta["+k+"].premi","","Premi ke " +(k+1) +" masih nol, silahkan cek rate, dan up nya lagi.");
							}else{
								m.put("premi", "Premi ke " +(k+1) +" masih nol, silahkan cek rate, dan up nya lagi.");
							}
						}
						premi[k] = premi1;

					}
				}
				Simas pp = new Simas();
				pp.setKelamin(kelamin[k]);
				pp.setLsre_id(lsre_id[k]);
				pp.setNama(nama[k]);
				pp.setNo_urut(no_urut[k]);
				pp.setPremi(premi[k]);
				pp.setReg_spaj(reg_spaj[k]);
				pp.setTgl_lahir(tanggal_lahir[k]);
				pp.setUmur(umur[k]);
				pp.setLsbs_id(kode_produk);
				pp.setLsdbs_number(number_produk);
				pp.setDiscount(new Double(0));
				peserta2.add(pp);
			}
			
			edit.getDatausulan().setDaftapeserta(peserta2);
		}
		catch (ClassNotFoundException e)
		{
			logger.error("ERROR :", e);
		} catch (InstantiationException e) {
			logger.error("ERROR :", e);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		}
		return m;
	}
	
}