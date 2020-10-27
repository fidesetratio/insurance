package com.ekalife.elions.web.bac.support;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.service.ElionsManager;

/**
 * @author HEMILDA
 * validator untuk pengeditan agen.
 */

public class Editnikvalidator implements Validator{
	
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
		form_agen d_agen= new form_agen(); 

		Integer kode_produk=null;
		Integer nomor_produk = null;
		Integer flag_as=new Integer(0);
		Integer flag_jenis_plan = new Integer(0);
		String hasil_nik="";
		String nama_karyawan="";
		String cabang_karyawan="";
		String dept_karyawan="";
		Integer flag_worksite = new Integer(0);
		
		// flag untuk  worksite  yang karyawan eka life
		Integer flag_karyawan = edit.getDatausulan().getMste_flag_el();
		if (flag_karyawan==null)
		{
			flag_karyawan= new Integer(0);
			edit.getDatausulan().setMste_flag_el(flag_karyawan);
		}
		
		// flag pribadi 1 tidak mendapatkan komisi
		Integer pribadi=new Integer(0);
		pribadi = edit.getPemegang().getMspo_pribadi();
		if (pribadi==null)
		{
			pribadi=new Integer(0);
			edit.getPemegang().setMspo_pribadi(pribadi);
		}
		
		//karyawan eka life
		if (flag_karyawan.intValue() == 1)
		{
			String kode_perusahaan = edit.getPemegang().getMspo_customer();
			if (kode_perusahaan==null)
			{
				kode_perusahaan="";
				edit.getPemegang().setMspo_customer(kode_perusahaan);
			}
			
			String nama_perusahaan="";
			if(kode_perusahaan!=null)
			{
				Map data1= (HashMap) this.elionsManager.select_namacompany(kode_perusahaan);
				if (data1!=null)
				{		
					nama_perusahaan = (String)data1.get("COMPANY_NAMA");
				}
			}
			edit.getPemegang().setMspo_customer(kode_perusahaan);
			edit.getPemegang().setMspo_customer_nama(nama_perusahaan);
			
			if (nama_perusahaan.toUpperCase().contains("EKA LIFE") || nama_perusahaan.toUpperCase().contains("EKALIFE"))
			{
//				edit.getDatausulan().setMste_flag_el(new Integer(1));
			}
		}
		
		kode_produk = edit.getDatausulan().getLsbs_id();
		nomor_produk = edit.getDatausulan().getLsdbs_number();
	
		String nama_produk="";
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
			
			if(edit.getPowersave() != null) {
				if(edit.getPowersave().getFlag_bulanan() != null){
					produk1.cek_flag_agen(kode_produk.intValue(),nomor_produk.intValue(), edit.getPowersave().getFlag_bulanan());
				}else{
					produk1.cek_flag_agen(kode_produk.intValue(),nomor_produk.intValue(), 0);
				}
			}else{
				produk1.cek_flag_agen(kode_produk.intValue(),nomor_produk.intValue(), 0);
			}
		
			flag_worksite = new Integer(produk1.flag_worksite);
			Integer flag_ekalink = new Integer(produk1.flag_ekalink);
			edit.getDatausulan().setFlag_ekalink(flag_ekalink);
			produk1.of_set_bisnis_no(nomor_produk.intValue());
			produk1.ii_bisnis_no=(nomor_produk.intValue());
			flag_as=new Integer(produk1.flag_as);
			flag_jenis_plan = new Integer(produk1.flag_jenis_plan);
			edit.getDatausulan().setFlag_jenis_plan(flag_jenis_plan);
			edit.getDatausulan().setMspr_discount(new Double(0));
			
			if (flag_as.intValue()==2)
			{
				//NIK_Karyawan
				String nik=edit.getEmployee().getNik();
				if (nik==null)
				{
					nik="";
					edit.getEmployee().setNik(nik);
				}
				if (nik.trim().length()==0)
				{
					hasil_nik="Khusus produk karyawan harus mengisi NIK Karyawan terlebih dahulu";
					err.rejectValue("employee.nik","",hasil_nik);
				}else{
					hasil_nik = d_agen.nik_karyawan(nik);
					if (hasil_nik.trim().length()!=0)
					{
						err.rejectValue("employee.nik","",hasil_nik);
					}else{
						Map data1= (HashMap) this.elionsManager.select_nik_karyawan(nik);
						if (data1!=null)
						{		
							nama_karyawan = (String)data1.get("MCL_FIRST");
							cabang_karyawan = (String)data1.get("LCA_NAMA");
							dept_karyawan = (String)data1.get("LDE_DEPT");
						}else{
							hasil_nik="NIK tidak terdaftar.";
							err.rejectValue("employee.nik","",hasil_nik);
						}
					}
				}
				edit.getEmployee().setNama(nama_karyawan);
				edit.getEmployee().setCabang(cabang_karyawan);
				edit.getEmployee().setDept(dept_karyawan);
				
				Integer jns_top_up_berkala=edit.getInvestasiutama().getDaftartopup().getPil_berkala();
				if (jns_top_up_berkala == null)
				{
					jns_top_up_berkala = new Integer(0);
				}
				Integer jns_top_up_tunggal=edit.getInvestasiutama().getDaftartopup().getPil_tunggal();
				if (jns_top_up_tunggal == null)
				{
					jns_top_up_tunggal = new Integer(0);
				}
				Double premi_pokok = edit.getDatausulan().getMspr_premium();
				if (premi_pokok ==null)
				{
					premi_pokok = new Double(0);
				}
				if ((jns_top_up_berkala.intValue() == 2 || jns_top_up_berkala.intValue() == 0 ) && jns_top_up_tunggal.intValue() == 0)
				{
					edit.getEmployee().setPotongan(premi_pokok);
				}
				if (jns_top_up_tunggal.intValue() == 1)
				{
					Double topup1 = edit.getInvestasiutama().getDaftartopup().getPremi_tunggal();
					if (topup1 == null)
					{
						topup1 = new Double(0);
					}
					Double total_potongan = new Double(premi_pokok.doubleValue() + topup1.doubleValue() );
					edit.getEmployee().setPotongan(total_potongan);
				}
			}
			
			if (flag_worksite.intValue() == 1)
			{
				String kode_perusahaan = edit.getPemegang().getMspo_customer();
				if (kode_perusahaan==null)
				{
					kode_perusahaan="";
					edit.getPemegang().setMspo_customer(kode_perusahaan);
				}
				
				if (kode_perusahaan.equalsIgnoreCase(""))
				{
					err.rejectValue("pemegang.mspo_customer","","Silahkan mengisi nama perusahaan terlebih dahulu.");
				}
				
				String nama_perusahaan="";
				if(kode_perusahaan!=null)
				{
					Map data1= (HashMap) this.elionsManager.select_namacompany(kode_perusahaan);
					if (data1!=null)
					{		
						nama_perusahaan = (String)data1.get("COMPANY_NAMA");
					}
				}
				edit.getPemegang().setMspo_customer(kode_perusahaan);
				edit.getPemegang().setMspo_customer_nama(nama_perusahaan);
				
				// 1 masih karyawan aktif
				edit.getPemegang().setDewasa(new Integer(1));
				
				String nik_worksite = edit.getPemegang().getNik();
				if (nik_worksite ==null)
				{
					nik_worksite="";
				}
				if (nik_worksite.trim().equalsIgnoreCase(""))
				{
					err.rejectValue("pemegang.nik","","Silahkan mengisi Nik karyawan terlebih dahulu.");
				}
				edit.getPemegang().setNik(nik_worksite);
				
				if (nama_perusahaan.toUpperCase().contains("EKA LIFE") || nama_perusahaan.toUpperCase().contains("EKALIFE"))
				{
//					edit.getDatausulan().setMste_flag_el(new Integer(1));
					
					pribadi = new Integer(1);
					edit.getPemegang().setMspo_pribadi(pribadi); // 1 kalau karyawan eka life jadi ada komisi
					Map data2= (HashMap) this.elionsManager.select_nik_karyawan(nik_worksite);
					if (data2!=null)
					{		
						nama_karyawan = (String)data2.get("MCL_FIRST");
						cabang_karyawan = (String)data2.get("LCA_NAMA");
						dept_karyawan = (String)data2.get("LDE_DEPT");
						edit.getEmployee().setNik(nik_worksite);
						edit.getDatausulan().setFlag_worksite_ekalife(new Integer(1));
						edit.getEmployee().setNama(nama_karyawan);
						edit.getEmployee().setCabang(cabang_karyawan);
						edit.getEmployee().setDept(dept_karyawan);
						edit.getDatausulan().setMste_flag_el(new Integer(1));
					}else{
						hasil_nik="NIK tidak terdaftar.";
						err.rejectValue("pemegang.nik","","HALAMAN DETIL AGEN :" +hasil_nik);
					}	
				}else{
					edit.getDatausulan().setFlag_worksite_ekalife(new Integer(0));
				}
			}else{
				edit.getPemegang().setMspo_customer("");
				edit.getPemegang().setMspo_customer_nama("");
				edit.getPemegang().setDewasa(null);
				edit.getPemegang().setNik("");
			}				
		}
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


