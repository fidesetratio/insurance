package com.ekalife.elions.web.bac;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.Employee;
import com.ekalife.elions.model.History;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.Editnikvalidator;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author HEMILDA
 * Controller untuk update NIK
 */
public class EditNikController extends ParentFormController{
	
	protected void validatePage(Object cmd, Errors err, int page) {
		Editnikvalidator validator = (Editnikvalidator) this.getValidator();
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Cmdeditbac detiledit = new Cmdeditbac();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lde_id = currentUser.getLde_id();
		String spaj = request.getParameter("spaj");
		if(spaj!=null)
		{
			/**
			 * @author HEMILDA
			 * edit data bukan input baru
			 */
			detiledit.setPemegang((Pemegang)this.elionsManager.selectpp(spaj));
			detiledit.setAddressbilling((AddressBilling)this.elionsManager.selectAddressBilling(spaj));
			detiledit.setDatausulan((Datausulan)this.elionsManager.selectDataUsulanutama(spaj));
			detiledit.getDatausulan().setLde_id(lde_id);
			detiledit.setTertanggung((Tertanggung)this.elionsManager.selectttg(spaj));
			
			InvestasiUtama investasi  = (InvestasiUtama)this.elionsManager.selectinvestasiutama(spaj);
			if (investasi == null)
			{
				InvestasiUtama inv = new InvestasiUtama();
				inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
				inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
				detiledit.setInvestasiutama(inv);
				DetilTopUp  detiltopup = new DetilTopUp();
				inv.setDaftartopup(detiltopup);
			}else{
				detiledit.setInvestasiutama(investasi);
				DetilTopUp detiltopup = (DetilTopUp)this.elionsManager.select_detil_topup(spaj);
				if (detiltopup == null)
				{
					detiltopup = new DetilTopUp();
					detiledit.getInvestasiutama().setDaftartopup(detiltopup);
				}else{
					detiledit.getInvestasiutama().setDaftartopup(detiltopup);
				}
			}
				
			detiledit.setRekening_client(this.elionsManager.select_rek_client(spaj));
			detiledit.setAccount_recur(this.elionsManager.select_account_recur(spaj));
			
			Powersave data_pwr = (Powersave)this.elionsManager.select_powersaver(spaj);
			if (data_pwr==null)
			{
				detiledit.setPowersave(new Powersave());
			}else{
				detiledit.setPowersave(data_pwr);
			}

			detiledit.setAgen(this.elionsManager.select_detilagen(spaj));

			String kode_agen = detiledit.getAgen().getMsag_id();
			String nama_agent="";
			if (kode_agen.equalsIgnoreCase("000000"))
			{
				nama_agent = (String)this.elionsManager.select_agent_temp(spaj);
			}
			detiledit.getAgen().setMcl_first(nama_agent);
			detiledit.setEmployee(this.elionsManager.select_detilemployee(spaj));
			
			detiledit.setHistory(new History());
			Integer posisi_dok=null;
			Integer status_dok=null;
			Map dataa = (HashMap)this.elionsManager.selectPositiondok(spaj);
			if (dataa!=null)
			{		
				posisi_dok = new Integer(Integer.parseInt(dataa.get("LSPD_ID").toString()));
				status_dok = new Integer(Integer.parseInt(dataa.get("LSSP_ID").toString()));
			}
			detiledit.getDatausulan().setLspd_id(posisi_dok);
			
			Integer kode_produk=detiledit.getDatausulan().getLsbs_id();
			Integer number_produk=detiledit.getDatausulan().getLsdbs_number();
			String nama_produk = "";
			if (Integer.toString(kode_produk.intValue()).trim().length()==1)
			{
				nama_produk="produk_asuransi.n_prod_0"+kode_produk;	
			}else{
				nama_produk="produk_asuransi.n_prod_"+kode_produk;	
			}

			try{
				Class aClass = Class.forName( nama_produk );
				n_prod produk = (n_prod)aClass.newInstance();
				produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
				produk.cek_flag_agen(kode_produk.intValue(),number_produk.intValue(), 0);
				produk.of_set_bisnis_no(number_produk.intValue());
				produk.ii_bisnis_no=(number_produk.intValue());
				produk.ii_bisnis_id=(kode_produk.intValue());
				Integer flag_bulanan = new Integer(produk.flag_powersavebulanan);
				detiledit.getDatausulan().setFlag_bulanan(flag_bulanan);
				
				Integer flag_powersave = new Integer(produk.of_get_bisnis_no(0));
				if (produk.flag_powersavebulanan == 1)
				{
					produk.cek_flag_agen(kode_produk.intValue(), number_produk.intValue(), 0);
					flag_powersave = new Integer(produk.flag_powersave);
				}
				detiledit.getPowersave().setFlag_powersave(flag_powersave);
				Integer flag_worksite = new Integer(produk.flag_worksite);
				Integer flag_as=new Integer(produk.flag_as);
				detiledit.getDatausulan().setFlag_as(flag_as);
				detiledit.getDatausulan().setFlag_worksite(flag_worksite);
				Boolean flag_bao=new Boolean(produk.isProductBancass);
				Integer flag_bao1 = null;
				if (flag_bao.booleanValue()==true)
				{
					flag_bao1=new Integer(1);
				}else{
					flag_bao1=new Integer(0);
				}

				if (flag_worksite != null)
				{
					if (flag_worksite.intValue() == 0)
					{

					}else{
						//cek nama perusahaan kalau sudah dipilih
						String kode_perusahaan = detiledit.getPemegang().getMspo_customer();
						String nama_perusahaan="";
						if(kode_perusahaan!=null)
						{
							Map data1= (HashMap) this.elionsManager.select_namacompany(kode_perusahaan);
							if (data1!=null)
							{		
								nama_perusahaan = (String)data1.get("COMPANY_NAMA");
							}
						}else{
							kode_perusahaan="";
						}
						detiledit.getPemegang().setMspo_customer_nama(nama_perusahaan);
						
						String nama_karyawan="";
						String cabang_karyawan="";
						String dept_karyawan="";
						String nik_worksite = detiledit.getPemegang().getNik();
						if (nik_worksite ==null)
						{
							nik_worksite="";
						}
						
						/**
						 * @author HEMILDA
						 * khusus pt ekalife
						 */
						if (nama_perusahaan.toUpperCase().contains("EKA LIFE") || nama_perusahaan.toUpperCase().contains("EKALIFE"))
						{
							Map data2= (HashMap) this.elionsManager.select_nik_karyawan(nik_worksite);

							if (data2!=null)
							{		
								nama_karyawan = (String)data2.get("MCL_FIRST");
								cabang_karyawan = (String)data2.get("LCA_NAMA");
								dept_karyawan = (String)data2.get("LDE_DEPT");
								detiledit.getDatausulan().setFlag_worksite_ekalife(new Integer(1));
								detiledit.getEmployee().setNama(nama_karyawan);
								detiledit.getEmployee().setCabang(cabang_karyawan);
								detiledit.getEmployee().setDept(dept_karyawan);
								detiledit.getEmployee().setNik(nik_worksite);
								detiledit.getDatausulan().setMste_flag_el(new Integer(1));
							}	
						}
					}
				}

				if (flag_as.intValue()==2)
				{
					String nama_karyawan = "";
					String cabang_karyawan="";
					String dept_karyawan ="";
					//NIK_Karyawan
					String nik=detiledit.getEmployee().getNik();
					if (nik==null)
					{
						nik="";
						detiledit.getEmployee().setNik(nik);
					}
					if (nik.trim().length()!=0)
					{
						Map data1= (HashMap) this.elionsManager.select_nik_karyawan(nik);
						if (data1!=null)
						{		
							nama_karyawan = (String)data1.get("MCL_FIRST");
							cabang_karyawan = (String)data1.get("LCA_NAMA");
							dept_karyawan = (String)data1.get("LDE_DEPT");
						}
					}
					detiledit.getEmployee().setNama(nama_karyawan);
					detiledit.getEmployee().setCabang(cabang_karyawan);
					detiledit.getEmployee().setDept(dept_karyawan);
				}
			}catch (ClassNotFoundException e)
			{
				logger.error("ERROR :", e);
			} catch (InstantiationException e) {
				logger.error("ERROR :", e);
			} catch (IllegalAccessException e) {
				logger.error("ERROR :", e);
			}
		}else{
			/**
			 * @author HEMILDA
			 * input
			 */
			detiledit.setPemegang(new Pemegang());
			detiledit.setAddressbilling(new AddressBilling());
			detiledit.setDatausulan(new Datausulan());
			detiledit.setTertanggung(new Tertanggung());
			InvestasiUtama inv = new InvestasiUtama();
			inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
			inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
			detiledit.setInvestasiutama(inv);
			inv.setDaftartopup(new DetilTopUp());
			detiledit.setRekening_client(new Rekening_client());
			detiledit.setAccount_recur(new Account_recur());
			detiledit.setPowersave(new Powersave());
			detiledit.setAgen(new Agen());
			detiledit.setHistory(new History());
			detiledit.setEmployee(new Employee());
		}
		
	return detiledit;
	}

	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
			
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Cmdeditbac editBac = (Cmdeditbac) cmd;
		String spaj= editBac.getPemegang().getReg_spaj();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
	
		/**
		 * @author HEMILDA
		 * kalau sudah diaksep, nik sudah tidak bisa diedit
		 */
		/*Integer status_polis = this.elionsManager.selectPositionSpaj(spaj);
		if (status_polis.intValue() != 5)
		{*/
			editBac=this.elionsManager.savingnik(cmd,err,"edit",currentUser);
		/*}else{
			err.rejectValue("employee.nik","","Spaj in sudah diaksep, tidak bisa diedit.");
		}*/
		
		/**
		 * @author HEMILDA
		 * pengecekan berhasil save data atau tidak. kalau editbac ada nilai spajnya artinya berhasil di save,sebaliknya berarti gagal
		 */
		if (!editBac.equals(""))
		{
			editBac.getDatausulan().setIndeks_validasi(new Integer(1));
		}else{
			editBac.getDatausulan().setIndeks_validasi(new Integer(0));
		}
		return new ModelAndView("bac/nik","cmd", editBac).addAllObjects(this.referenceData(request,cmd,err));
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}
	
}
