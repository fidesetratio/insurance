package com.ekalife.elions.web.bac.support;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.FormatString;
/**
 * @author HEMILDA
 * validator untuk pengeditan agen.
 */

public class Editagenvalidator implements Validator{
	
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
	
		String kode_regional="";
		String nama_regional="";
		String kode_penutup="";
		String nama_penutup="";
		String mspo_leader="";
		Integer ao=new Integer(0);
		String kode_ao="";
		String nama_ao="";
		Integer pribadi=new Integer(0);
		Integer followup=new Integer(0);
		String kode_regional_penagihan="";
		String nama_regional_penagihan="";
		String hasil_agen="";
		String hasil_agen_leader = "";
		Integer kode_produk=null;
		Integer nomor_produk = null;
		Integer ulink=new Integer(0);
		Integer sertifikasi=new Integer(0);
		String cabang="";
		Date tanggal_sertifikat=null;
		int tgl_sertifikat;
		int bln_sertifikat;
		int thn_sertifikat;
		String hasil_tanggaldibuat="";
		String hasil_ao="";
		String hasil_nama_penutup="";
		String cbg="";
		Integer komisi_bii=new Integer(0);
		Integer flag_jenis_plan = new Integer(0);

		kode_produk = edit.getDatausulan().getLsbs_id();
		nomor_produk = edit.getDatausulan().getLsdbs_number();
		kode_penutup = edit.getAgen().getMsag_id();
		mspo_leader = edit.getPemegang().getMspo_leader();
		
		if(mspo_leader==null){
			mspo_leader="";
			edit.getPemegang().setMspo_leader(mspo_leader);
		}
		if (kode_penutup ==null)
		{
			kode_penutup="";
			edit.getAgen().setMsag_id(kode_penutup);
		}
		ao = edit.getPemegang().getMspo_ref_bii();
		if (ao==null)
		{
			ao= new Integer(0);
			edit.getPemegang().setMspo_ref_bii(ao);
		}
		kode_ao = edit.getPemegang().getMspo_ao();
		if (kode_ao==null)
		{
			kode_ao="";
			edit.getPemegang().setMspo_ao(kode_ao);
		}
		nama_ao = edit.getPemegang().getNama_ao();
		if (nama_ao ==null)
		{
			nama_ao ="";
			edit.getPemegang().setNama_ao(nama_ao);
		}
		pribadi = edit.getPemegang().getMspo_pribadi();
		if (pribadi==null)
		{
			pribadi=new Integer(0);
			edit.getPemegang().setMspo_pribadi(pribadi);
		}
		followup = edit.getPemegang().getMspo_follow_up();
		if (followup==null)
		{
			followup=new Integer(0);
			edit.getPemegang().setMspo_follow_up(followup);
		}
		kode_regional_penagihan = edit.getAddressbilling().getRegion();
		if (kode_regional_penagihan==null)
		{
			kode_regional_penagihan="";
			edit.getAddressbilling().setRegion(kode_regional_penagihan);
		}
		cbg = edit.getPemegang().getLca_id();
		if(ao == null)
		{
			ao =new Integer(0);
		}
		hasil_agen=d_agen.kode_agen(kode_penutup);
		hasil_agen_leader=d_agen.kode_agen_leader(mspo_leader);
		if(edit.getDatausulan().getLsbs_id().intValue()==155 || edit.getDatausulan().getLsbs_id().intValue()==134 || (edit.getDatausulan().getLsbs_id().intValue()==142 && edit.getDatausulan().getLsdbs_number().intValue()==2) || (edit.getDatausulan().getLsbs_id().intValue()==175 && edit.getDatausulan().getLsdbs_number().intValue()==2) || (edit.getDatausulan().getLsbs_id().intValue()==164 && edit.getDatausulan().getLsdbs_number().intValue()==2)){
			hasil_agen_leader="";
			hasil_agen="";
		}
		if(!edit.getCurrentUser().getCab_bank().equals("") && (edit.getCurrentUser().getJn_bank().intValue() == 2 && edit.getCurrentUser().getJn_bank().intValue() == 3)){
			hasil_agen_leader="";
		}
		
		if(edit.getDatausulan().getLsbs_id().intValue()!=155 && edit.getDatausulan().getLsbs_id().intValue()!=134 && (edit.getDatausulan().getLsbs_id().intValue()!=142 || edit.getDatausulan().getLsdbs_number().intValue()!=2) && (edit.getDatausulan().getLsbs_id().intValue()!=175 || edit.getDatausulan().getLsdbs_number().intValue()!=2) && (edit.getDatausulan().getLsbs_id().intValue()!=164 || edit.getDatausulan().getLsdbs_number().intValue()!=2) ){
			if(edit.getCurrentUser().getCab_bank().equals("") && (edit.getCurrentUser().getJn_bank().intValue() != 2 && edit.getCurrentUser().getJn_bank().intValue() != 3)) {
				if(hasil_agen_leader.trim().length()==0){
//					String kode_leader_sementara = elionsManager.selectMsagLeader(kode_penutup);
//					//DAPETIN KODE AGENT LEADER DAHULU, APABILA NULL, BRATI KODE_LEADER HARUS = KODE_AGENT ITU SENDIRI, 
//					if(kode_leader_sementara.trim().length()==0){
//						if(mspo_leader!=kode_penutup){
//							err.rejectValue("pemegang.mspo_leader","", "HALAMAN DETIL AGEN : Untuk kode leadernya silakan diisi dengan kode agent itu sendiri.");
//						}
						
//						SELAIN ITU CEK KE TINGKATAN LEVEL DARI KODE_AGENT YG DIINPUT, DAN TIAP LEADER YG DIDAPAT DIBANDINGKAN DENGAN KODE_LEADER YG DIINPUT
//					}else{
						List<Map> tingkatAgent = elionsManager.selectTingkatanAgent(kode_penutup);
						Integer flag_true_agent_leader = 0;
						Integer flag_error = 0;
						Integer flag_total_error = 0;
						
						String spaj = edit.getAgen().getReg_spaj();
						Integer jumlah_cancel;
						/**
						 * @author HEMILDA
						 * cek mst cancel kalau >0 artinya endors
						 * kalau endors agent penutup yang sudah tidak aktif tetap bisa dipakai
						 * beberapa validasi dilewatkan
						 */
						if (!spaj.equalsIgnoreCase(""))
						{
							jumlah_cancel = (Integer)this.elionsManager.count_mst_cancel(spaj);
						}else{
							jumlah_cancel = new Integer(0);
						}
						
						for(int i=0;i<tingkatAgent.size();i++){
							Map tingkatAgentData = (Map) tingkatAgent.get(i);
							String kode_leader_sementara = (String) tingkatAgentData.get("MST_LEADER");
							if(kode_leader_sementara==null){
								kode_leader_sementara = (String) tingkatAgentData.get("MSAG_RM");
							}
							if(flag_true_agent_leader==0){
								if(kode_leader_sementara==null){
									if(!mspo_leader.equals(kode_penutup)){
										err.rejectValue("pemegang.mspo_leader","", "HALAMAN DETIL AGEN :Kode Leader tidak valid.");
									}
								}else{
//									APABILA SAMA, LANJUTKAN DENGAN VALIDASI TANGGAL SERTIFIKASI DARI LEADER TERSEBUT, MASIH AKTIF ATAU TIDAK
									if(kode_leader_sementara.equals(mspo_leader)){
										Map data;
										if (jumlah_cancel.intValue() == 0)
										{
											data = (HashMap) this.elionsManager.selectagenpenutup(mspo_leader);
										}else{
											data = (HashMap) this.elionsManager.selectagenpenutup_endors(mspo_leader);
										}
										if (data!=null)
										{		
											nama_penutup=(String)data.get("NAMA");
											kode_regional=(String)data.get("REGIONID");
											nama_regional = (String)data.get("REGION");
											ulink=((Integer)data.get("ULINK"));
											sertifikasi = ((Integer)data.get("SERTIFIKAT"));
											cabang = (String)data.get("LCAID");
											tanggal_sertifikat = (Date)data.get("BERLAKU");

											
										}else{
											hasil_agen="Agen tersebut tidak terdaftar, silahkan hubungi Agency Support atau IT div.";
											flag_error=flag_error+1;
											//err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
											
										}
										Date tanggal_beg_date = edit.getDatausulan().getMste_beg_date();
										if (hasil_agen.trim().length()==0)
										{
											if (jumlah_cancel.intValue() == 0)
											{
												hasil_agen=d_agen.sertifikasi_agen(mspo_leader,ulink.intValue(),sertifikasi.intValue(),tanggal_sertifikat, tanggal_beg_date);
												if (hasil_agen.trim().length()!=0)
												{
													if (jumlah_cancel.intValue() == 0)
													{
														flag_error=flag_error+1;
														//err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
													}
												}
											}
										}
										if(flag_error>0){
											flag_total_error= flag_error;
											flag_error = 0;
											flag_true_agent_leader = 0;
										}else{
											flag_true_agent_leader = 1;
										}
									}else{
//									APABILA TIDAK SAMA, PERINGATKAM BAHWA KODE LEADER YG DIINPUT MASIH SALAH, SILAKAN DIINPUT KODE LEADER YANG BENAR.
										if(mspo_leader.equals(kode_penutup)){
											hasil_agen_leader = "";
											flag_true_agent_leader = 1;
										}else{
											hasil_agen_leader = "Kode Leader dari Agent tersebut masih salah, silakan masukkan yang benar.";
										}
									}
								}
							}
						}
						if(flag_true_agent_leader.intValue()==0){
							if(!mspo_leader.equals(kode_penutup)){
								err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
							}
						}else{
							hasil_agen="";
							hasil_agen_leader="";
						}
//					}
						
				}else{
					err.rejectValue("pemegang.mspo_leader","","HALAMAN DETIL AGEN :" +hasil_agen_leader);
				}
			}
		}
		
		if(hasil_agen.trim().length()==0)
		{
			String spaj = edit.getAgen().getReg_spaj();
			Integer jumlah_cancel;
			/**
			 * @author HEMILDA
			 * cek mst cancel kalau >0 artinya endors
			 * kalau endors agent penutup yang sudah tidak aktif tetap bisa dipakai
			 * beberapa validasi dilewatkan
			 */
			if (!spaj.equalsIgnoreCase(""))
			{
				jumlah_cancel = (Integer)this.elionsManager.count_mst_cancel(spaj);
			}else{
				jumlah_cancel = new Integer(0);
			}
			Map data;
			if (jumlah_cancel.intValue() == 0)
			{
				data = (HashMap) this.elionsManager.selectagenpenutup(kode_penutup);
			}else{
				data = (HashMap) this.elionsManager.selectagenpenutup_endors(kode_penutup);
			}
			if (data!=null)
			{
				nama_penutup=(String)data.get("NAMA");
				kode_regional=(String)data.get("REGIONID");
				nama_regional = (String)data.get("REGION");
				ulink=((Integer)data.get("ULINK"));
				sertifikasi = ((Integer)data.get("SERTIFIKAT"));
				cabang = (String)data.get("LCAID");
				tanggal_sertifikat = (Date)data.get("BERLAKU");

				if (FormatString.rpad("0",(kode_regional.substring(0,4)),4).equalsIgnoreCase("0903"))
				{
					komisi_bii=new Integer(1);
				}

				if (FormatString.rpad("0",(kode_regional.substring(0,2)),2).equalsIgnoreCase("09") && !(FormatString.rpad("0",(kode_regional.substring(0,4)),4).equalsIgnoreCase("0914")))
				{
					ao=new Integer(1);
				}
				if (kode_regional_penagihan.equalsIgnoreCase(""))
				{
					kode_regional_penagihan = kode_regional;
					
				}
			}else{
				hasil_agen="Agen tersebut tidak terdaftar, silahkan hubungi Agency Support atau IT div.";
				err.rejectValue("agen.msag_id","",hasil_agen);
			}			
			Date tanggal_beg_date = edit.getDatausulan().getMste_beg_date();
			if (hasil_agen.trim().length()==0)
			{
				if (jumlah_cancel.intValue() == 0)
				{
					/**
					 * @author HEMILDA
					 * cek sertifikasi agen kalau ujian tidak cek tanggal, selain itu cek tanggal expired
					 */
					hasil_agen=d_agen.sertifikasi_agen(kode_penutup,ulink.intValue(),sertifikasi.intValue(),tanggal_sertifikat, tanggal_beg_date);
					if (hasil_agen.trim().length()!=0)
					{
						err.rejectValue("agen.msag_id","",hasil_agen);
					}
				}
			}				
		}else{
			err.rejectValue("agen.msag_id","",hasil_agen);
		}


		if(hasil_agen.trim().length()==0)
		{	
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
				/**
				 * @author HEMILDA
				 * cek flag2 agen yang ada
				 */
				produk1.cek_flag_agen(kode_produk.intValue(),nomor_produk.intValue(), 0);
				Integer flag_worksite = new Integer(produk1.flag_worksite);
				if (produk1.flag_as==2)
				{
					hasil_agen=d_agen.agen_link_karyawan(kode_penutup);
					if (hasil_agen.trim().length()!=0)
					{
						err.rejectValue("agen.msag_id","",hasil_agen);
					}
				}else{
					/**
					 * @author HEMILDA
					 * kode agen baru 000000
					 * cek nama penutup harus diisi
					 * set untuk regionnya berdasarkan lca id dari user
					 */
					if (kode_penutup.equalsIgnoreCase("000000"))
					{
						nama_penutup=edit.getAgen().getMcl_first();
						if (nama_penutup==null)
						{
							nama_penutup="";
							edit.getAgen().setMcl_first(nama_penutup);
						}
						hasil_nama_penutup=d_agen.nama_penutup(nama_penutup);

						if (hasil_nama_penutup.trim().length()!=0)
						{
							err.rejectValue("agen.mcl_first","",hasil_nama_penutup);
						}
						if (cbg.equalsIgnoreCase("01") || cbg.equalsIgnoreCase("07") || cbg.equalsIgnoreCase("09"))
						{
							kode_regional=cbg.concat("0100");
						}else{
							kode_regional=cbg.concat("0000");
						}

						Map data = (HashMap) this.elionsManager.selectregional(kode_regional);

						if (data!=null)
						{		
							nama_regional = (String)data.get("LSRG_NAMA");
						}
					}else{
						/**
						 * @author HEMILDA
						 * cek agen harus agency system
						 */
						if (produk1.flag_as==1)
						{
							hasil_agen=d_agen.agen_as(kode_regional);
							if (hasil_agen.trim().length()!=0)
							{
								err.rejectValue("agen.msag_id","",hasil_agen);
							}
						}else if ((produk1.flag_as==0))
							{
								if (produk1.isProductBancass==false)
								{
									if (produk1.flag_worksite == 1)
									{
										/**
										 * @author HEMILDA
										 * cek agen harus agen worksite
										 */
										hasil_agen=d_agen.agen_worksite(kode_regional);
										if (hasil_agen.trim().length()!=0)
										{
											err.rejectValue("agen.msag_id","",hasil_agen);
										}										
//									}else{
//										/**
//										 * @author HEMILDA
//										 * cek agen harus agen regional
//										 */
//										hasil_agen=d_agen.agen_rg(kode_regional);
//										if (hasil_agen.trim().length()!=0)
//										{
//											err.rejectValue("agen.msag_id","",hasil_agen);
//										}		
//									}
									}else if (produk1.flag_artha == 1)
										{
											hasil_agen=d_agen.agen_artha(kode_regional);
											if (hasil_agen.trim().length()!=0)
											{
												err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hasil_agen);
											}
											if (kode_produk.intValue()== 162)
											{
												if (kode_regional==null)
												{
													kode_regional="";
												}
												if (nomor_produk.intValue()<5)
												{
													//arthamas
													if (!(kode_regional.substring(0,2).equalsIgnoreCase("46")))
													{
														String hsl="Plan ini, agen penutupnya harus agen arthamas ."	;
														err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hsl);
													}
												}else{
													//regional
	//												if ((kode_regional.substring(0,2).equalsIgnoreCase("46")))
	//												{
	//													String hsl="Plan ini, agen penutupnya tidak boleh agen arthamas ."	;
	//													err.rejectValue("agen.msag_id","","HALAMAN DETIL AGEN :" +hsl);
	//												}
												}
											}
										}
								}else{
									/**
									 * @author HEMILDA
									 * cek agen harus agen bancassurance
									 */
									hasil_agen=d_agen.agen_bao(kode_regional);
									if (hasil_agen.trim().length()!=0)
									{
										err.rejectValue("agen.msag_id","",hasil_agen);
									}else{
										/**
										 * @author HEMILDA
										 * cek agen harus reff bii
										 */
										hasil_agen = d_agen.agen_reff_bii(kode_regional,produk1.flag_reff_bii);
										if (hasil_agen.trim().length()!=0)
										{
											err.rejectValue("agen.msag_id","",hasil_agen);
										}
									}
								}
							}else if((produk1.flag_as==3 && (kode_produk==182 && nomor_produk!=7) && (kode_produk==182 && nomor_produk!=8) && (kode_produk==182 && nomor_produk!=9)))
								{
								/**
								 * @author HEMILDA
								 * cek agen harus agen regional dan agency system
								 */
									hasil_agen=d_agen.agen_rg_as(kode_regional);
									if (hasil_agen.trim().length()!=0)
									{
										err.rejectValue("agen.msag_id","",hasil_agen);
									}	
								}
							}
						}
					
				if (FormatString.rpad("0",(kode_regional.substring(0,2)),2).equalsIgnoreCase("09"))
				{
					ao=new Integer(1);
				}
					
				if (pribadi == null)
				{
					pribadi =new Integer(0);
				}

				if (ao.intValue()==0)
				{
					kode_ao="";
					nama_ao="";
				}else{
					hasil_ao = d_agen.kode_ao(kode_ao);
					if (hasil_ao.trim().length()!=0)
					{
						err.rejectValue("pemegang.mspo_ao","",hasil_ao);
					}else{
						/**
						 * @author HEMILDA
						 * cari ao
						 */
						Map data = (HashMap) this.elionsManager.selectagenao(kode_ao);

						if (data!=null)
						{		
							nama_ao = (String)data.get("nama");
						}else{
							hasil_ao="Kode AO tidak terdaftar.";
							err.rejectValue("pemegang.mspo_ao","",hasil_ao);
						}
					}
				}
				edit.getPemegang().setMspo_ref_bii(ao);
				edit.getPemegang().setMspo_pribadi(pribadi);
				edit.getDatausulan().setMspr_discount(new Double(0));
	

				
				edit.getAgen().setKode_regional(kode_regional);
				edit.getAgen().setLsrg_nama(nama_regional);
				edit.getAgen().setMsag_id(kode_penutup);
				edit.getAgen().setMcl_first(nama_penutup);

				edit.getPemegang().setMspo_ref_bii(ao);
				edit.getPemegang().setMspo_ao(kode_ao);
				edit.getPemegang().setMspo_pribadi(pribadi);
				edit.getAddressbilling().setRegion(kode_regional_penagihan);
				edit.getPemegang().setNama_ao(nama_ao);
				edit.getPemegang().setMspo_komisi_bii(komisi_bii);
				edit.getPemegang().setMspo_follow_up(followup);
				
				/**
				 * @author HEMILDA
				 * untuk produk worksite
				 * isi nama pt
				 * isi nik karyawan
				 * kalau pt karyawan , flag pribadi = 1
				 */
				if (flag_worksite.intValue() == 1)
				{
					String hasil_worksite= "";
					String kode_perusahaan = edit.getPemegang().getMspo_customer();
					if (kode_perusahaan==null)
					{
						kode_perusahaan="";
						edit.getPemegang().setMspo_customer(kode_perusahaan);
					}
					
					if (kode_perusahaan.equalsIgnoreCase(""))
					{
						err.rejectValue("pemegang.mspo_customer","","HALAMAN DETIL AGEN :" +"Silahkan mengisi nama perusahaan terlebih dahulu.");
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
						err.rejectValue("pemegang.nik","","HALAMAN DETIL AGEN :" +"Silahkan mengisi Nik karyawan terlebih dahulu.");
					}
					edit.getPemegang().setNik(nik_worksite);
					
					if (nama_perusahaan.toUpperCase().contains("EKA LIFE") || nama_perusahaan.toUpperCase().contains("EKALIFE"))
					{
						if (kode_produk.intValue() == 141)
						{
							pribadi = new Integer(1);
						}
						edit.getPemegang().setMspo_pribadi(pribadi); // 1 kalau karyawan eka life jadi ada komisi
						
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
			}
		
		}
		
	}

}

