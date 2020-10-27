package com.ekalife.utils.jasper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.Products;
import com.ekalife.utils.f_hit_umur;

public class JasperFunctionReport extends JasperScriptlet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8839187258437995364L;
	public String cabang = null;
	public Integer li_jamin = null;
	
	public int f_check_premi_ke(String reg_spaj, int li_prodke,
			ElionsManager elionsManager) {
		Integer premike = new Integer(0);
		Map data = (HashMap) elionsManager.mst_billing_ke(reg_spaj, li_prodke);
		if (data == null) {
			premike = null;
		} else {
			premike = (Integer) data.get("msbi_premi_ke");

		}
		return premike;
	}

	public int f_check_newbie_prod(String as_spaj, int an_prodke,
			int an_premike, int an_plan, ElionsManager elionsManager) {
		// return 1 : new bisnis, 2: top up
		int li_prod;
		Integer li_flag = null;

		if (an_prodke <= 3) {
			// 116
			if (an_plan == 116 || an_plan == 118 || an_plan == 217 || an_plan == 218) {
				Map data = (HashMap) elionsManager.mst_billing_ke(as_spaj,
						an_premike);
				if (data == null) {
					li_flag = null;
				} else {
					li_flag = (Integer) data.get("msbi_flag_topup");
				}

				if (li_flag == null || li_flag.intValue() == 0) {
					li_prod = 1;
				} else {
					li_prod = an_prodke;
				}
			} else {
				li_prod = an_prodke;
			}
		} else {
			li_prod = an_prodke;
		}

		return li_prod;
	}

	public int f_jangka_prosave(String as_spaj, ElionsManager elionsManager, int tahun_ke, int premi_ke) {
		// Integer li_jamin = new Integer(4);
		Integer li_jamin = (Integer) elionsManager
				.selectMasaGaransiInvestasi(as_spaj, tahun_ke, premi_ke);
		if (li_jamin == null) {
			li_jamin = 0;
		}
		
		return li_jamin.intValue();
	}

	public Date f_get_tgl_rk(String as_spaj, ElionsManager elionsManager) {
		Date ldt_tglrk;
		String qry = " AND  ( EKA.MST_BILLING.MSBI_TAHUN_KE = 1 ) AND (  EKA.MST_BILLING.MSBI_PREMI_KE = 1) and EKA.MST_BILLING.MSBI_PAID = 1";
		Map data = (HashMap) elionsManager.mst_billing(as_spaj, qry);
		if (data == null) {
			ldt_tglrk = null;
		} else {
			ldt_tglrk = (Date) data.get("MSBI_PAID_DATE");
		}

		return ldt_tglrk;
	}
	
	/**
	 * koefisien UPP kontes (PERSENTASE TERHADAP UPP EVA, BUKAN TERHADAP PREMI) 
	 */
	public double f_koef_upp_kontes(int lsgb_id, int lsbs_id, int lsdbs_number, int lscb_id, String lca_id, int lsbs_jenis, int flag_topup, int paymode) {
		double hasil;
		
		//Yusuf (27/7/09) bila eka sehat dan cara bayar bulanan, maka upp eva, kontes, lain = 0
		if((lsbs_id == 183 || lsbs_id == 189 || lsbs_id == 193) && paymode == 6){
			hasil = 0;
		//arthamas non arthalink harusnya 1x upp eva, tapi malah 1.25
		}else if(lca_id.equals("46")) { //arthamas
			if(lsbs_id == 143 && lsdbs_number == 4){
				hasil = 0.07;
			}else if(lsbs_id == 153 && lsdbs_number == 5){
				hasil = 1;
			}else if(lsbs_id == 162) {
				if(lscb_id == 0 || flag_topup > 0) {
					if(lsdbs_number == 7 || lsdbs_number == 8) { //EKALINK 88 PLUS kontes 10%, eva 3,5%, reverted multiply to get 10% from 3,5%
						hasil = (double) 100 / (double) 35;
					}else {
						hasil = 2;
					}
				}else {
					hasil = 1;
				}
			}else {
				hasil = 1;
			}
		}else {
			if(lsbs_id == 143 && lsdbs_number == 4){
				hasil = 0.07;
			}else if(lsbs_id == 153 && lsdbs_number == 5){
				hasil = 1;
			}else if(lsgb_id == 17) {
				if(lsbs_id == 162) {
					if(lscb_id == 0 || flag_topup > 0) {
						if(lsdbs_number == 7 || lsdbs_number == 8) { //EKALINK 88 PLUS kontes 10%, eva 3,5%, reverted multiply to get 10% from 3,5%
							hasil = (double) 100 / (double) 35;
						}else {
							hasil = 2;
						}
					}else {
						hasil = 1;
					}
				}else {
					hasil = 1.25;
				}
			}else {
				if("13, 16, 17".indexOf(FormatString.rpad("0", String.valueOf(lsgb_id), 2)) == -1 && lscb_id == 0 && lsbs_jenis != 1 && lsbs_id != 173) {
					hasil = 0.15;
				}else {
					hasil = 1;
				}
			}
		}

		return hasil;
	}
	
	/**
	 * koefisien UPP evaluasi (PERSENTASE TERHADAP PREMI)
	 */
	public Double f_koef_upp_evaluasi(int group_biz, int biz_code, int det_biz,
			int pay_mode, int prod_type, int prod_ke, int thn_ke,
			String reg_spaj, int premi_ke, ElionsManager elionsManager) {

		Double ldec_prm = new Double(0);
		int li_kali, li_prodke;

		Date ldt_tglrk, ldt_batas;
		f_hit_umur umr = new f_hit_umur();

		if (prod_type == 3) {
			li_kali = -1;
		} else {
			li_kali = 1;
		}
		li_prodke = prod_ke;

		switch (group_biz) {
		case 13:// Power Simponi USD
			
		case 16:// Power Simponi USD
			ldec_prm = new Double(0.1);
			if (biz_code == 110 || biz_code == 112) // power simponi usd
			{
				ldec_prm = new Double(0.05);
			}
			break;
		case 17:
			if (biz_code == 77) // XLINK
			{
				if (det_biz == 1) {
					ldec_prm = new Double(0.05);
				} else if (det_biz == 2) {
					ldec_prm = new Double(0.5);
				} else if (det_biz == 3) {
					ldec_prm = new Double(1);
				}
			} else if (biz_code == 84 || biz_code == 100) // XLINKgold
			{
				if (det_biz == 1) {
					ldec_prm = new Double(0.20);
				} else if (det_biz == 2) {
					ldec_prm = new Double(0.75);
				} else if (det_biz == 3) {
					ldec_prm = new Double(1);
				}
			} else if (biz_code == 87 || biz_code == 101 || biz_code == 134
					|| biz_code == 166) // XLINKPlatinum
			{
				ldec_prm = new Double(0.10);
			} else if (biz_code == 97 || biz_code == 107) // fastXLINK
			{
				if (pay_mode == 0) {
					ldec_prm = new Double(0.20);
				} else if (pay_mode == 3) {
					ldec_prm = new Double(0.75);
				} else {// IF pay_mode = 3 THEN
					ldec_prm = new Double(0.75);
				}
			} else if (biz_code == 113) // XLINKgold Kry
			{
				if (det_biz == 2 || det_biz == 3) {
					ldec_prm = new Double(1);
				}
			} else if (biz_code == 115 || biz_code == 117 || biz_code == 152) // XLINK80
																				// ||
																				// SYARIAH
			{
				if (thn_ke == 1 || thn_ke == 2) {
					ldec_prm = new Double(0.05);
				} else {
					ldec_prm = new Double(0.025);
				}
			} else if (biz_code == 116 || biz_code == 118 || biz_code == 153){ // XLINK80+ || SYARIAH
				if (det_biz == 1 || det_biz == 3) {
					ldec_prm = new Double(0.05);
				} else if (det_biz == 2 || det_biz == 4 || det_biz == 5) {
					ldec_prm = new Double(1);
				}
			}else if (biz_code == 217  || biz_code == 218){ // SMILELINK100 || SMILELINK100SYARIAH
				if (det_biz == 1 || det_biz == 2 ) {
					ldec_prm = new Double(1);
				} 
			} else if (biz_code == 119 || biz_code == 122 || biz_code == 139) // XLINK18
			{
				if (det_biz == 1 || det_biz == 4) {
					ldec_prm = new Double(0.20);
				} else if (det_biz == 2 || det_biz == 5) {
					ldec_prm = new Double(0.75);
				} else if (det_biz == 3 || det_biz == 6) {
					ldec_prm = new Double(1);
				}
			} else if (biz_code == 159 || biz_code == 160) {// ekalink family
				ldec_prm = new Double(1);
				if (pay_mode == 0) {
					ldec_prm = new Double(0.05);
				}
			} else if (biz_code == 162) {// ekalink family
				ldec_prm = new Double(1);
				if (det_biz == 1 || det_biz == 2 || det_biz == 3 || det_biz == 4 || det_biz == 5 || det_biz == 6) {
					if(pay_mode == 0) ldec_prm = new Double(0.05);
				}else { //Ekalink 88
					if(pay_mode == 0) ldec_prm = new Double(0.035);
				}
			} else {
				ldec_prm = new Double(1);
			}
			break;
		default:
			if (pay_mode == 0) {
				ldec_prm = new Double(0.2);
			} else if (pay_mode != 0) {
				ldec_prm = new Double(1);
			}

			int jumlah_hari = 0;
			switch (biz_code) {
			case 73:// PA
				ldec_prm = new Double(0.50);
				break;
			case 79:
			case 161:
				ldec_prm = new Double(0.1);
				break;
			case 80:
				if (pay_mode == 6) {
					ldec_prm = new Double(3);
				} else if (pay_mode == 1 || pay_mode == 2 || pay_mode == 3) {
					ldec_prm = new Double(1);
				}
				break;
			case 69:
			case 82:
				if (pay_mode == 0) {
					ldec_prm = new Double(0.1);
				} else if (pay_mode != 0) {
					ldec_prm = new Double(1);
				}
				break;
			case 86:
			case 94:
			case 123:
			case 124:
				if(li_jamin == null) {
					li_jamin = f_jangka_prosave(reg_spaj, elionsManager, thn_ke, premi_ke);
					ldt_tglrk = f_get_tgl_rk(reg_spaj, elionsManager);
					ldt_batas = convertstringdate("20060216");
					// jumlah_hari = umr.hari(ldt_batas.getYear()+1900,
					// ldt_batas.getMonth()+1, ldt_batas.getDate(),
					// ldt_tglrk.getYear()+1900, ldt_tglrk.getMonth()+1,
					// ldt_tglrk.getDate());
					// if (jumlah_hari >=0)
					if (ldt_tglrk.before(ldt_batas)) {
						if (li_jamin == 6) {
							ldec_prm = new Double(0.0085);
						} else if (li_jamin == 12) {
							ldec_prm = new Double(0.025);
						} else if (li_jamin == 24) {
							ldec_prm = new Double(0.05);
						} else if (li_jamin == 36) {
							ldec_prm = new Double(0.07);
						} else {
							ldec_prm = new Double(0);
						}
					} else {
						if (li_jamin == 12) {
							ldec_prm = new Double(0.01);
						} else if (li_jamin == 36) {
							ldec_prm = new Double(0.07);
						} else {
							ldec_prm = new Double(0);
						}
					}
				}else {
					if (li_jamin == 12) {
						ldec_prm = new Double(0.01);
					} else if (li_jamin == 36) {
						ldec_prm = new Double(0.07);
					} else {
						ldec_prm = new Double(0);
					}
				}
				break;
			case 142:
			case 143:
			case 144:
			case 155:
			case 158:
				if(li_jamin == null) {
					li_jamin = f_jangka_prosave(reg_spaj, elionsManager, thn_ke, premi_ke);
					ldt_tglrk = f_get_tgl_rk(reg_spaj, elionsManager);
					ldt_batas = convertstringdate("20060216");
	
					// jumlah_hari = umr.hari(ldt_batas.getYear()+1900,
					// ldt_batas.getMonth()+1, ldt_batas.getDate(),
					// ldt_tglrk.getYear()+1900, ldt_tglrk.getMonth()+1,
					// ldt_tglrk.getDate());
					// if (jumlah_hari >=0)
					if (ldt_tglrk.before(ldt_batas)) {
						if (li_jamin == 6) {
							ldec_prm = new Double(0.0085);
						} else if (li_jamin == 12) {
							ldec_prm = new Double(0.025);
						} else if (li_jamin == 24) {
							ldec_prm = new Double(0.05);
						} else if (li_jamin == 36) {
							ldec_prm = new Double(0.07);
						} else {
							ldec_prm = new Double(0);
						}
					} else if (li_jamin == 12) {
						ldec_prm = new Double(0.01);
					} else if (li_jamin == 36) {
						ldec_prm = new Double(0.07);
					} else {
						ldec_prm = new Double(0);
					}
				}else {
					if (li_jamin == 12) {
						ldec_prm = new Double(0.01);
					} else if (li_jamin == 36) {
						ldec_prm = new Double(0.07);
					} else {
						ldec_prm = new Double(0);
					}
				}
				
				if(biz_code == 143 && det_biz == 4) {
					ldec_prm = 0.015;
				}
				
				break;
			case 164:
				if(li_jamin == null) li_jamin = elionsManager.selectMasaGaransiInvestasi(reg_spaj, 1, 1);
//				if (li_jamin == 3) {
//					ldec_prm = new Double(0.002);
//				} else if (li_jamin == 6) {
//					ldec_prm = new Double(0.004);
//				} else 
				if (li_jamin == 12) {
					ldec_prm = new Double(0.01);
				} else if (li_jamin == 24) {
					ldec_prm = new Double(0.04);
				} else if (li_jamin == 36) {
					ldec_prm = new Double(0.07);
				} else {
					ldec_prm = new Double(0);
				}
				break;
			case 174:
				if(li_jamin == null) li_jamin = elionsManager.selectMasaGaransiInvestasi(reg_spaj, 1, 1);
//				if (li_jamin == 3) {
//					ldec_prm = new Double(0.002);
//				} else if (li_jamin == 6) {
//					ldec_prm = new Double(0.004);
//				} else 
				if (li_jamin == 12) {
					ldec_prm = new Double(0.01);
				} else if (li_jamin == 24) {
					ldec_prm = new Double(0.04);
				} else if (li_jamin == 36) {
					ldec_prm = new Double(0.07);
				} else {
					ldec_prm = new Double(0);
				}
				break;
			case 173: // Eka Sarjana Mandiri
				if(det_biz == 1) {
					ldec_prm = 0.2;
				}else {
					ldec_prm = 1.;
				}
				break;
			case 172: // Eka Siswa Emas
					ldec_prm = 1.;
					break;
			case 114: 
				if(det_biz == 2) {
					ldec_prm = 0.05;
				}
				break;
			case 137: 
				if(det_biz == 4 || det_biz == 5) {
					ldec_prm = 0.05;
				}
				break;
			case 183: //Yusuf (27/7/09) bila eka sehat dan cara bayar bulanan, maka upp eva, kontes, lain = 0
				if (pay_mode == 6) {
					ldec_prm = new Double(0.);
				}else {
					//Yusuf (4/8/09), request Yosep via email, bila eka sehat dan cara bayar lainnya, pengakuannya 10%
					ldec_prm = 0.1; 
				}
				break;
			case 140: //worksite
				if (pay_mode == 0) { //sekaligus
					ldec_prm = 0.1;
				}else{
					ldec_prm = 1.;
				}
				break;
			case 141: //worksite
				if (pay_mode == 0) { //sekaligus
					ldec_prm = 0.1;
				}else{
					ldec_prm = 1.;
				}
				break;
			case 189: //Yusuf (27/7/09) bila eka sehat dan cara bayar bulanan, maka upp eva, kontes, lain = 0
				if (pay_mode == 6) {
					ldec_prm = new Double(0.);
				}else {
					//Yusuf (4/8/09), request Yosep via email, bila eka sehat dan cara bayar lainnya, pengakuannya 10%
					ldec_prm = 0.1; 
				}
				break;
			case 193: //Deddy,  bila eka sehat dan cara bayar bulanan, maka upp eva, kontes, lain = 0
				if (pay_mode == 6) {
					ldec_prm = new Double(0.);
				}else {
					//Deddy, request Yosep via email, bila eka sehat dan cara bayar lainnya, pengakuannya 10%
					ldec_prm = 0.1; 
				}
				break;	
			}
		}

		if (li_prodke > 1) {
			switch (group_biz) {
			case 17:
				if (biz_code == 87 || biz_code == 101 || biz_code == 134 || biz_code == 166) // XLINKPlatinum
				{
					ldec_prm = new Double(0.025);
				} else if (biz_code == 115 || biz_code == 117 || biz_code == 152) // XLINK80
					if (thn_ke == 1 || thn_ke == 2) {
						ldec_prm = new Double(0.05);
					} else {
						ldec_prm = new Double(0.025);
					}
				else {
					Integer flag_top_up = (Integer) elionsManager.f_prod_topup(reg_spaj, new Integer(prod_ke));
					if (flag_top_up == null) {
						flag_top_up = new Integer(0);
					}
					if (flag_top_up.intValue() > 0) {
						if(biz_code == 162 && (det_biz == 7 || det_biz == 8)) {
							ldec_prm = new Double(0.035);
						}else if(biz_code == 140 || biz_code == 141){
							ldec_prm = new Double(0.1);
						}else {
							ldec_prm = new Double(0.05);
						}
					}
				}
			}
		}

		if ((biz_code == 163 || biz_code == 167) && det_biz == 5) { //hidup bahagia sekaligus
			ldec_prm = new Double(ldec_prm.doubleValue() * 0.2);
		}

		// apabila arthamas dan bukan 161,162,163,167, dan kondisi2 lainnya, maka 70%
		if (!(biz_code == 162 && (det_biz == 1 || det_biz == 2 || det_biz == 3 || det_biz == 4 || det_biz == 7 || det_biz == 8))
				&& biz_code != 161 && biz_code != 163 && biz_code != 164 && biz_code != 167  
				&& !(biz_code == 137 && (det_biz == 4 || det_biz == 5))
				&& !(biz_code == 114 && det_biz == 2)
				&& biz_code < 170
			) {
			if(cabang == null) {
				cabang = (String) elionsManager.cabang_production(reg_spaj, prod_ke);
			}
			if (cabang.equalsIgnoreCase("46")) {
				ldec_prm = new Double(ldec_prm.doubleValue() * 0.7);
			}
		}
		// selain itu, 100%
		ldec_prm = new Double(ldec_prm.doubleValue() * li_kali);
		return ldec_prm;

	}

	public String cek_produk_link(String lsbs_id, Products products) {
		boolean a = products.unitLink(lsbs_id);
		String hasil = "";
		if (a == true) {
			hasil = "1";
		} else {
			hasil = "0";
		}

		return hasil;
	}

}
