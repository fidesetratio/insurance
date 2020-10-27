package com.ekalife.elions.web.bac.support;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ekalife.elions.model.Cmdeditbac;

import com.ekalife.elions.service.ElionsManager;

/**
 * @author HEMILDA
 * validator ttpanpremiviewerController
 */	
public class TtgSimasvalidator implements Validator {
	
	protected final Log logger = LogFactory.getLog( getClass() );

	private ElionsManager elionsManager;
	
	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}
	
	public boolean supports(Class data) {
		return Cmdeditbac.class.isAssignableFrom(data);
	}

	public void validate(Object cmd, Errors err) {
		Cmdeditbac edit= (Cmdeditbac) cmd;

		try {
			Map data = this.elionsManager.validbac(edit, err, "simas","windowsimas");
		} catch (ServletException e) {
			logger.error("ERROR :", e);
		} catch (IOException e) {
			logger.error("ERROR :", e);
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
		
	/*	Cmdeditbac edit= (Cmdeditbac) cmd;

		List peserta = edit.getDatausulan().getDaftapeserta();
		Integer jumlah_r =new Integer(peserta.size());
		if (jumlah_r==null)
		{
			jumlah_r=new Integer(0);
		}
		Integer jmlh_peserta=jumlah_r;
		edit.getDatausulan().setJml_peserta(new Integer(jmlh_peserta.intValue()));

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
			produk1.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
			produk1.cek_flag_agen(kode_produk.intValue(),number_produk.intValue());

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
					err.rejectValue("datausulan.daftapeserta["+k+"].nama","",hsl_nama_m);
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
					err.rejectValue("datausulan.daftapeserta["+k+"].tgl_lahir","",hsl_tanggal_lahir);
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
					Integer age=new Integer(produk1.ii_age);
					
					String hsl_umur = "";
					if (umur[k]==null)
					{
						hsl_umur="Umur ke " +(k+1) +" harus diisi.";
						umur[k] = new Integer(0);
					}
					if (hsl_umur.trim().length()!=0)
					{
						err.rejectValue("datausulan.daftapeserta["+k+"].umur","",hsl_umur);
					}
					
					String hasil_cek_usia=produk1.of_check_usia(tahun2.intValue(),bulan2.intValue(),tanggal2.intValue(),tahun1.intValue(),bulan1.intValue(),tanggal1.intValue(),pay_period.intValue(),number_produk.intValue());
					if (hasil_cek_usia.trim().length()!=0)
					{
						err.rejectValue("datausulan.daftapeserta["+k+"].umur","",hasil_cek_usia);
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
							err.rejectValue("datausulan.daftapeserta["+k+"].premi","","Rate Premi ke " +(k+1) +" ini belum ada.");
						}
						Double premi1=new Double(produk1.idec_premi_main);

						if (premi1.doubleValue() == 0)
						{
							err.rejectValue("datausulan.daftapeserta["+k+"].premi","","Premi ke " +(k+1) +" masih nol, silahkan cek rate, dan up nya lagi.");
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
		}*/
	
	}



}
