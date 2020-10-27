/**
 * @author  : Ferry Harlim
 * @created : Aug 20, 2007 11:34:46 AM
 */
package com.ekalife.elions.web.bancass;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.CommandUploadUw;
import com.ekalife.elions.model.Drek;
import com.ekalife.elions.model.ProSaveBayar;
import com.ekalife.elions.model.Upp;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.kyc.NewBusinessCase;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentMultiController;

public class BancassMultiController extends ParentMultiController {
	
	public ModelAndView pembayaran_bunga(HttpServletRequest request, HttpServletResponse response) throws Exception{

		Date now = this.elionsManager.selectSysdate();
		
		int mpb_flag_bs = ServletRequestUtils.getIntParameter(request, "mpb_flag_bs", -1);
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(now));
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(now));
		String lcb_no = ServletRequestUtils.getStringParameter(request, "lcb_no", "");
		
		Map map = new HashMap();
		map.put("mpb_flag_bs", mpb_flag_bs);
		map.put("startDate", startDate);//
		map.put("endDate", endDate);//
		map.put("lcb_no", lcb_no);//
		
		
		
		//map.put("piyu", request.getParameter("piyu"));
		
		List<DropDown> daftarRekAjs = new ArrayList<DropDown>();
		daftarRekAjs.add(new DropDown("-1", "ALL"));
		daftarRekAjs.add(new DropDown("1", "BELUM BAYAR"));
		daftarRekAjs.add(new DropDown("2", "SUDAH BAYAR"));

		List<DropDown> daftarCab = new ArrayList<DropDown>();
		daftarCab.add(0, new DropDown("", "ALL"));
		daftarCab.addAll(this.elionsManager.selectDropDown("EKA.LST_CABANG_BII", "LCB_NO", "NAMA_CABANG", "", "NAMA_CABANG", "JENIS=3"));
		
		List<ProSaveBayar> daftar = new ArrayList<ProSaveBayar>();
		daftar = elionsManager.selectMstProSave(mpb_flag_bs, lcb_no, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate));
		
		
		if(request.getParameter("recheck") != null) {
			String[] daftar_bayar_id = ServletRequestUtils.getStringParameters(request, "bayarid");
			int[] daftar_jenis = ServletRequestUtils.getIntParameters(request, "jenis");
			
			int counter = 0;
			String tampungbayar;
			int tampungjenis;
			for(int i=0; i<daftar_bayar_id.length; i++) {
				int pilih = ServletRequestUtils.getIntParameter(request, "ucup" + i, 0);
				counter += pilih;
				if(pilih == 1) {
					tampungbayar = daftar_bayar_id[i];
					tampungjenis = daftar_jenis[i];
					elionsManager.updateMstProSaveBayar(tampungbayar, tampungjenis);
					daftar.get(i).setFlag_check(pilih);
					daftar.get(i).setStatus("SUDAH BAYAR");
				}
				map.put("pilih"+i, pilih);
				map.put("check", pilih);
			}
			
			String flag = "1";
			map.put("flag", flag);
			//elionsManager.updateMstProSaveBayar(daftarUpdate, daftarUpdateJenis);
		}
		
		if(request.getParameter("cari")!=null){
//			String a = "tes";
//			logger.info(a);
			String flag = ServletRequestUtils.getStringParameter(request, "flag", "1");
			if(daftar.size()==0){
				flag = "0";
			}else flag ="1";
			map.put("flag", flag);
		}
		
		map.put("StatusBayar", daftarRekAjs);
		map.put("daftarCab", daftarCab);
		//map.put("daftarDrek", this.elionsManager.selectMstDrek(lsrek_id, lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal)));
		map.put("daftarDrek", daftar);
		map.put("ukuran", daftar.size());
		
		return new ModelAndView("uw/pembayaran_bunga", map);
	}
	
	public ModelAndView drek(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Date now = this.elionsManager.selectSysdate();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		int lsrek_id = ServletRequestUtils.getIntParameter(request, "lsrek_id", -1);
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(now));
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(now));
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String no_spaj = ServletRequestUtils.getStringParameter(request, "no_spaj", "");
		String no_trx = ServletRequestUtils.getStringParameter(request, "no_trx", "");
		int lsbp_id = ServletRequestUtils.getIntParameter(request, "lsbp_id", 156);
		String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
		String startNominal = ServletRequestUtils.getStringParameter(request, "startNominal", "0");
		String endNominal = ServletRequestUtils.getStringParameter(request, "endNominal", "99999999999");
		int flag =0;
		
		if(request.getParameter("recheck") != null && request.getParameter("edit") == null) {
			String[] daftar_trx 	= ServletRequestUtils.getStringParameters(request, "recheck");
			String[] daftar_edit 	= ServletRequestUtils.getStringParameters(request, "rubah");
			elionsManager.updateMstDrekRecheck(daftar_trx);
		}
	
		Map map = new HashMap();
		map.put("lsrek_id", lsrek_id);
		map.put("startDate", startDate);//
		map.put("endDate", endDate);//
		map.put("lsbp_id", lsbp_id);//
		map.put("kode", kode);
		map.put("startNominal", startNominal);
		map.put("endNominal", endNominal);
		
		map.put("piyu", request.getParameter("piyu"));
		map.put("cupcup", request.getParameter("cupcup"));
		
		List<DropDown> daftarRekAjs = new ArrayList<DropDown>();
		daftarRekAjs.add(new DropDown("-1", 	"ALL"));
		
		//Yusuf (5/4/2012) Start now, tampilkan dari query saja, lebih paten, biar gak ada yg ketinggalan2 karena harus hardcode
		daftarRekAjs.addAll(uwManager.selectRekeningAjs(156,null));

		/*
		//Bank Sinarmas
		daftarRekAjs.add(new DropDown("97", 	"[0000029025] BSM MANGGA DUA RUPIAH (POLIS BSM)"));
		daftarRekAjs.add(new DropDown("98", 	"[0000029041] BSM MANGGA DUA DOLLAR (POLIS BSM)"));
		daftarRekAjs.add(new DropDown("160", 	"[0000791822] BSM WISMA EKA JIWA JAKARTA DOLLAR (POLIS BSM)"));
		daftarRekAjs.add(new DropDown("161", 	"[0000791814] BSM WISMA EKA JIWA JAKARTA RUPIAH (POLIS BSM)"));
		daftarRekAjs.add(new DropDown("169", 	"[0001238299] BSM WISMA EKA JIWA JAKARTA RUPIAH (POLIS BSM)"));
		daftarRekAjs.add(new DropDown("170", 	"[0001238288] BSM WISMA EKA JIWA JAKARTA DOLLAR (POLIS BSM)"));
		
		//Sinarmas Sekuritas
		daftarRekAjs.add(new DropDown("214", 	"[0002595777] BSM THAMRIN JAKARTA DOLLAR (POLIS SMS)"));
		daftarRekAjs.add(new DropDown("216", 	"[0002596555] BSM THAMRIN JAKARTA RUPIAH (POLIS SMS)"));
		*/
		
		List<DropDown> daftarCab = new ArrayList<DropDown>();
		daftarCab.add(0, new DropDown("", "ALL"));
		daftarCab.addAll(this.elionsManager.selectDropDown("EKA.CAB_BSM", "KODE", "NAMA_CAB  || ' (' || KODE || ')'", "", "NAMA_CAB", null));
		List<Drek> daftarDrek= this.elionsManager.selectMstDrek(lsrek_id, lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal));
		if(request.getParameter("edit") != null) {
			flag=1;
			for (int i=0; i<daftarDrek.size(); i++){
				String daftar_edit 	= ServletRequestUtils.getStringParameter(request, "rubah"+i);					
				if(daftar_edit!=null){
					String dftr_spaj = ServletRequestUtils.getStringParameter(request, "reg_spaj"+i);
					elionsManager.updateMstDrekEdit(daftar_edit, currentUser.getLus_id(), dftr_spaj);
				}
			}
		}
		if(request.getParameter("buang") != null) {
			for (int i=0; i<daftarDrek.size(); i++){
				String daftar_buang 	= ServletRequestUtils.getStringParameter(request, "rubah"+i);
				if(daftar_buang!=null){
					String dftr_spaj = ServletRequestUtils.getStringParameter(request, "reg_spaj"+i);
					if (dftr_spaj!= null)
						dftr_spaj=null;
					elionsManager.updateDrekKosongkanSpaj(daftar_buang, currentUser.getLus_id(), dftr_spaj);;
				}
			}
		}
		
		map.put("daftarRekAjs", daftarRekAjs);
		map.put("daftarBank", this.elionsManager.selectDropDown("EKA.LST_BANK_PUSAT", "LSBP_ID", "LSBP_NAMA", "", "LSBP_NAMA", null));
		map.put("daftarCab", daftarCab);
		map.put("daftarDrek", this.elionsManager.selectMstDrek(lsrek_id, lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal)));
		
		
		return new ModelAndView("uw/drek", map);
	}

	public ModelAndView drek2(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Date now = this.elionsManager.selectSysdate();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		int lsrek_id = ServletRequestUtils.getIntParameter(request, "lsrek_id", -1);
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(now));
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(now));
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String no_spaj = ServletRequestUtils.getStringParameter(request, "no_spaj", "");
		String no_trx = ServletRequestUtils.getStringParameter(request, "no_trx", "");
		int lsbp_id = ServletRequestUtils.getIntParameter(request, "lsbp_id", 156);
		String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
		String startNominal = ServletRequestUtils.getStringParameter(request, "startNominal", "0");
		String endNominal = ServletRequestUtils.getStringParameter(request, "endNominal", "99999999999");
		int flag =0;

		Map map = new HashMap();
		map.put("lsrek_id", lsrek_id);
		map.put("startDate", startDate);//
		map.put("endDate", endDate);//
		map.put("lsbp_id", lsbp_id);//
		map.put("kode", kode);
		map.put("startNominal", startNominal);
		map.put("endNominal", endNominal);
		
		map.put("piyu", request.getParameter("piyu"));
		map.put("cupcup", request.getParameter("cupcup"));
		
		List<DropDown> daftarRekAjs = new ArrayList<DropDown>();
		daftarRekAjs.add(new DropDown("-1", 	"ALL"));
		
		//Yusuf (5/4/2012) Start now, tampilkan dari query saja, lebih paten, biar gak ada yg ketinggalan2 karena harus hardcode
		daftarRekAjs.addAll(uwManager.selectRekeningAjs(156,null));

		/*
		//Bank Sinarmas
		daftarRekAjs.add(new DropDown("97", 	"[0000029025] BSM MANGGA DUA RUPIAH (POLIS BSM)"));
		daftarRekAjs.add(new DropDown("98", 	"[0000029041] BSM MANGGA DUA DOLLAR (POLIS BSM)"));
		daftarRekAjs.add(new DropDown("160", 	"[0000791822] BSM WISMA EKA JIWA JAKARTA DOLLAR (POLIS BSM)"));
		daftarRekAjs.add(new DropDown("161", 	"[0000791814] BSM WISMA EKA JIWA JAKARTA RUPIAH (POLIS BSM)"));
		daftarRekAjs.add(new DropDown("169", 	"[0001238299] BSM WISMA EKA JIWA JAKARTA RUPIAH (POLIS BSM)"));
		daftarRekAjs.add(new DropDown("170", 	"[0001238288] BSM WISMA EKA JIWA JAKARTA DOLLAR (POLIS BSM)"));
		
		//Sinarmas Sekuritas
		daftarRekAjs.add(new DropDown("214", 	"[0002595777] BSM THAMRIN JAKARTA DOLLAR (POLIS SMS)"));
		daftarRekAjs.add(new DropDown("216", 	"[0002596555] BSM THAMRIN JAKARTA RUPIAH (POLIS SMS)"));
		*/
		
		List<DropDown> daftarCab = new ArrayList<DropDown>();
		daftarCab.add(0, new DropDown("", "ALL"));
		daftarCab.addAll(this.elionsManager.selectDropDown("EKA.CAB_BSM", "KODE", "NAMA_CAB  || ' (' || KODE || ')'", "", "NAMA_CAB", null));
		List<Drek> daftarDrek= this.elionsManager.selectMstDrek(lsrek_id, lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal));
		
		map.put("daftarRekAjs", daftarRekAjs);
		map.put("daftarBank", this.elionsManager.selectDropDown("EKA.LST_BANK_PUSAT", "LSBP_ID", "LSBP_NAMA", "", "LSBP_NAMA", null));
		map.put("daftarCab", daftarCab);
		map.put("daftarDrek", daftarDrek);
		
		
		return new ModelAndView("uw/drek2", map);
	}

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("2", 1,null,null));
		return new ModelAndView("bancass/bancass", map);
	}

	public ModelAndView list_komisi(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		String dist = ServletRequestUtils.getStringParameter(request, "dist", "");
		String prod = ServletRequestUtils.getStringParameter(request, "prod", "");
		map.put("dist", dist);
		map.put("prod", prod);
		
		List<DropDown> daftarDist = new ArrayList<DropDown>();
		daftarDist.add(new DropDown("", "JALUR DISTRIBUSI >>"));
		daftarDist.add(new DropDown("1", "Regional"));
		daftarDist.add(new DropDown("3", "Agency"));
		daftarDist.add(new DropDown("4", "Hybrid"));
		map.put("daftarDist", daftarDist);
		
		List<DropDown> daftarProduk = uwManager.selectDaftarProdukKomisi(dist);
		daftarProduk.add(0, new DropDown("", "NAMA PRODUK >>"));
		map.put("daftarProduk", daftarProduk);
		
		if(request.getParameter("show") != null) {
			
			List<Map> daftarKomisi = uwManager.selectDaftarKomisi(dist, prod);
			for(Map m : daftarKomisi) {
				Upp upp = new Upp();
				
				if(dist.equals("1")) 		upp.lca_id = "01";
				else if(dist.equals("3")) 	upp.lca_id = "37";
				if(dist.equals("4")) 		upp.lca_id = "46";
				
				upp.mspro_jn_prod 	= 1;
				upp.lsgb_id 		= ((BigDecimal) m.get("LSGB_ID")).intValue();
				upp.lsbs_id 		= ((BigDecimal) m.get("LSBS_ID")).intValue();
				upp.lsdbs_number 	= ((BigDecimal) m.get("LSDBS_NUMBER")).intValue();
				upp.lscb_id 		= ((BigDecimal) m.get("LSCB_ID")).intValue();
				upp.msbi_tahun_ke 	= ((BigDecimal) m.get("LSCO_YEAR")).intValue();
				upp.lsbs_jenis 		= ((BigDecimal) m.get("LSBS_JENIS")).intValue();
				upp.mspro_prod_date = elionsManager.selectSysdate();
				upp.premi_pokok 	= (double) 1;
				upp.mspro_nilai_kurs = (double) 1;
				upp.premi_rider 	= (double) 0;
				if(upp.msbi_tahun_ke.intValue() == 0) {
					upp.msbi_premi_ke 	= 2;
					upp.flag_topup 		= 1;
				}else {
					upp.msbi_premi_ke 	= 1;
					upp.flag_topup 		= 0;
				}
				
				if(upp.lsbs_jenis.intValue() == 1) {
					map.put("IS_POWERSAVE", 1);
					upp.mgi = 1;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_1", 		upp.upp_eva * 100);					
					m.put("UPP_KONTES_1", 	upp.upp_kontes * 100);
					upp.mgi = 3;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_3", 		upp.upp_eva * 100);					
					m.put("UPP_KONTES_3", 	upp.upp_kontes * 100);
					upp.mgi = 6;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_6", 		upp.upp_eva * 100);					
					m.put("UPP_KONTES_6", 	upp.upp_kontes * 100);
					upp.mgi = 9;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_9", 		upp.upp_eva * 100);					
					m.put("UPP_KONTES_9", 	upp.upp_kontes * 100);
					upp.mgi = 12;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_12", 	upp.upp_eva * 100);					
					m.put("UPP_KONTES_12", 	upp.upp_kontes * 100);
					upp.mgi = 24;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_24", 	upp.upp_eva * 100);					
					m.put("UPP_KONTES_24", 	upp.upp_kontes * 100);
					upp.mgi = 36;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_36", 	upp.upp_eva * 100);					
					m.put("UPP_KONTES_36", 	upp.upp_kontes * 100);

				}else {
					map.put("IS_POWERSAVE", 0);
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA", 		upp.upp_eva * 100);					
					m.put("UPP_KONTES", 	upp.upp_kontes * 100);
				}
				
			}
			map.put("daftarKomisi", daftarKomisi);
			map.put("ukuran", daftarKomisi.size());
		}
		
		return new ModelAndView("uw/list_komisi", map);
	}

	// ini cara lama penarikan UPP eva dan kontes, yaitu dari hardcoding, cara baru pake tabel baru
//	public ModelAndView list_komisi(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Map map = new HashMap();
//		String dist = ServletRequestUtils.getStringParameter(request, "dist", "");
//		String prod = ServletRequestUtils.getStringParameter(request, "prod", "");
//		map.put("dist", dist);
//		map.put("prod", prod);
//		
//		List<DropDown> daftarDist = new ArrayList<DropDown>();
//		daftarDist.add(new DropDown("", "JALUR DISTRIBUSI >>"));
//		daftarDist.add(new DropDown("1", "Regional"));
//		daftarDist.add(new DropDown("3", "Agency"));
//		daftarDist.add(new DropDown("4", "Hybrid"));
//		map.put("daftarDist", daftarDist);
//		
//		List<DropDown> daftarProduk = elionsManager.selectDaftarProdukKomisi(dist);
//		daftarProduk.add(0, new DropDown("", "NAMA PRODUK >>"));
//		map.put("daftarProduk", daftarProduk);
//		
//		if(request.getParameter("show") != null) {
//			List<Map> daftarKomisi = elionsManager.selectDaftarKomisi(dist, prod);
//			JasperFunctionReport jf = new JasperFunctionReport();
//			for(Map m : daftarKomisi) {
//				if(dist.equals("1")) jf.cabang = "01";
//				else if(dist.equals("3")) jf.cabang = "37";
//				if(dist.equals("4")) jf.cabang = "46";
//				
//				int lsgb_id = ((BigDecimal) m.get("LSGB_ID")).intValue();
//				int lsbs_id = ((BigDecimal) m.get("LSBS_ID")).intValue();
//				int lsdbs_number = ((BigDecimal) m.get("LSDBS_NUMBER")).intValue();
//				int lscb_id = ((BigDecimal) m.get("LSCB_ID")).intValue();
//				int lsco_year = ((BigDecimal) m.get("LSCO_YEAR")).intValue();
//				int lsbs_jenis = ((BigDecimal) m.get("LSBS_JENIS")).intValue();
//				
//				if(products.powerSave(String.valueOf(lsbs_id))) {
//					map.put("IS_POWERSAVE", 1);
//					jf.li_jamin = 1;
//					double upp_eva_1 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 3;
//					double upp_eva_3 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 6;
//					double upp_eva_6 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 9;
//					double upp_eva_9 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 12;
//					double upp_eva_12 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 24;
//					double upp_eva_24 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 36;
//					double upp_eva_36 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					double koef_upp_kontes = jf.f_koef_upp_kontes(lsgb_id, lsbs_id, lsdbs_number, lscb_id, jf.cabang, lsbs_jenis, (lsco_year==0 ? 1 : 0));
//					m.put("UPP_EVA_1", upp_eva_1);					
//					m.put("UPP_EVA_3", upp_eva_3);					
//					m.put("UPP_EVA_6", upp_eva_6);					
//					m.put("UPP_EVA_9", upp_eva_9);					
//					m.put("UPP_EVA_12", upp_eva_12);					
//					m.put("UPP_EVA_24", upp_eva_24);					
//					m.put("UPP_EVA_36", upp_eva_36);					
//					m.put("UPP_KONTES_1", koef_upp_kontes * upp_eva_1);
//					m.put("UPP_KONTES_3", koef_upp_kontes * upp_eva_3);
//					m.put("UPP_KONTES_6", koef_upp_kontes * upp_eva_6);
//					m.put("UPP_KONTES_9", koef_upp_kontes * upp_eva_9);
//					m.put("UPP_KONTES_12", koef_upp_kontes * upp_eva_12);
//					m.put("UPP_KONTES_24", koef_upp_kontes * upp_eva_24);
//					m.put("UPP_KONTES_36", koef_upp_kontes * upp_eva_36);
//				}else {
//					map.put("IS_POWERSAVE", 0);
//					double upp_eva = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					double koef_upp_kontes = jf.f_koef_upp_kontes(lsgb_id, lsbs_id, lsdbs_number, lscb_id, jf.cabang, lsbs_jenis, (lsco_year==0 ? 1 : 0));
//					m.put("UPP_EVA", upp_eva);					
//					m.put("UPP_KONTES", koef_upp_kontes * upp_eva);
//				}
//				
//			}
//			map.put("daftarKomisi", daftarKomisi);
//			map.put("ukuran", daftarKomisi.size());
//		}
//		
//		return new ModelAndView("uw/list_komisi", map);
//	}
	
	public ModelAndView kyc_proses(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();	
		NewBusinessCase newBus=new NewBusinessCase();
		Integer info;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj");
		String proses=ServletRequestUtils.getStringParameter(request, "proses","");
		String proses2=request.getParameter("proses2");
		info=0;
		Map data= elionsManager.selectKYC(spaj);
		Integer flag=0;
		flag=(Integer)data.get("LSSP_ID");
//		Integer insured= (Integer)data.get("mspo_policy_no_format");
		String policy=(String)data.get("mspo_policy_holder");
		String lusId= currentUser.getLus_id();
		Date mste_kyc_date;
		
		if(proses2==null){//tidak
			if ( (policy.substring(0,2).equalsIgnoreCase("XX")) || (policy.substring(0,2).equalsIgnoreCase("WW")) ){
				info=2;
				proses="1";
				mste_kyc_date=null;
			}else{
				mste_kyc_date= elionsManager.selectSysdate();
				elionsManager.updateProsesKyc2(spaj, 1, lusId, 0,mste_kyc_date);
			}
		}else{//ya plus keterangan
			if ( (policy.substring(0,2).equalsIgnoreCase("XX")) || (policy.substring(0,2).equalsIgnoreCase("WW")) ){
				info=2;
				proses="1";
				mste_kyc_date=null;
			}else{
				proses="3";
				mste_kyc_date= elionsManager.selectSysdate();
				elionsManager.updateProsesKyc2(spaj, 1, lusId, 1,mste_kyc_date);
				String desc=ServletRequestUtils.getStringParameter(request, "keterangan");
				elionsManager.updateProsesKycResultKyc(spaj, 1, lusId, desc,mste_kyc_date);
			}
			
		}
		
		
		
		map.put("info", info);
		map.put("spaj", spaj);
		map.put("proses", proses);
		return new ModelAndView("uw/kyc_proses", map);
		
		
//		if (request.getParameter("tdk")!=null){
//			elionsManager.updateProsesKyc2(spaj, 1, lusId, 0,mste_kyc_date);
//		}
//		if (request.getParameter("ya")!=null){		
//			elionsManager.updateProsesKyc2(spaj, 1, lusId, 1,mste_kyc_date);	
//		}
//			
//		if (request.getParameter("save")!=null){
//			String desc=ServletRequestUtils.getStringParameter(request, "keterangan");
//			elionsManager.updateProsesKycResultKyc(spaj, 1, lusId, desc,mste_kyc_date);
//		}
	}
	
	public ModelAndView akseptasi_khusus(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("1,2,4,6,7,8,99", 1,10,null));
		return new ModelAndView("uw/akseptasi_khusus", map);
	}
	
	public ModelAndView update_akseptasi(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj");
		String desc=ServletRequestUtils.getStringParameter(request, "desc");
		String lca_id = elionsManager.selectLcaIdBySpaj(spaj);
		Integer lsspId,lspdId;
		Map mapTt=elionsManager.selectTertanggung(spaj);
		lsspId=(Integer)mapTt.get("LSSP_ID");
		lspdId=(Integer)mapTt.get("LSPD_ID");
		elionsManager.prosesUpdateAkseptasiKhusus(spaj, lspdId, 5, 1,currentUser.getLus_id(),lsspId,desc);
		map.put("msg","Berhasil Update SPAJ ("+spaj+").Status Menjadi ACCEPTED");
		map.put("lspd_id", lspdId);
		map.put("spaj", spaj);
		return new ModelAndView("uw/update_akseptasi", map);
	}
	
	public ModelAndView report_akseptasi_khusus(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		String to=ServletRequestUtils.getStringParameter(request, "to");
		String cc=ServletRequestUtils.getStringParameter(request, "cc");
		
		List lsCabang=new ArrayList();
		List lsAdmin=new ArrayList();
			lsCabang=elionsManager.selectlstCabangForAkseptasiKhusus();
			DropDown cabang=new DropDown();
			cabang.setKey("0");
			cabang.setValue("ALL");
			lsCabang.add(0, cabang);
		List lsCabangToday=elionsManager.selectlstCabangForAkseptasiKhususToday();	
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());
		Integer flag=ServletRequestUtils.getIntParameter(request, "l");
		
		if(flag==null)
			lus_id=currentUser.getLus_id();
		param.put("lsCabang", lsCabang);
		if(lsCabangToday.isEmpty())
			param.put("flagCabang",1);
		else
			param.put("flagCabang",0);
		param.put("lsCabangToday",lsCabangToday);
		param.put("lca_id",lca_id);
		param.put("to",to);
		param.put("cc",cc);
		param.put("lus_id",lus_id);
		param.put("tgl", elionsManager.selectSysdate());
//		request.getSession().setAttribute("report", report);
//		return prepareReport(request, response, report);
		return new ModelAndView("uw/report_akseptasi_khusus", param);
	}
	
	
//	public ModelAndView reportFurtherRequirement(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Map param=new HashMap();
//		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
//		List lsCabang=new ArrayList();
//		List lsAdmin=new ArrayList();
//			lsCabang=elionsManager.selectlstCabangForAkseptasiKhusus();
//			DropDown cabang=new DropDown();
//			cabang.setKey("0");
//			cabang.setValue("ALL");
//			lsCabang.add(0, cabang);
//		List lsCabangToday=elionsManager.selectlstCabangForAkseptasiKhususToday();	
//		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());
//		Integer flag=ServletRequestUtils.getIntParameter(request, "l");
//		if(flag==null)
//			lus_id=currentUser.getLus_id();
//		param.put("lsCabang", lsCabang);
//		if(lsCabangToday.isEmpty())
//			param.put("flagCabang",1);
//		else
//			param.put("flagCabang",0);
//		param.put("lsCabangToday",lsCabangToday);
//		param.put("lca_id",lca_id);
//		param.put("lus_id",lus_id);
//		param.put("tgl", elionsManager.selectSysdate());
//		return new ModelAndView("report/reportFurtherRequirement", param);
//	}
	public ModelAndView main_update_nasabah(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ(null, 1,null,null));
		return new ModelAndView("uw/main_update_nasabah", map);
	}
	
	public ModelAndView MainCetakPolisCab(HttpServletRequest request, HttpServletResponse response) throws Exception{
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Map map = new HashMap();
		String lap=ServletRequestUtils.getStringParameter(request, "All");
		return new ModelAndView("uw/MainCetakPolisCab", map);
	}
	
	public ModelAndView cetakPolisCabang(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
		Date sysdate=elionsManager.selectSysdate();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());	

				lsRegion=elionsManager.selectAllLstCab();
				Map cabang=new HashMap();
				lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
				lsRegion.add(0, cabang);
		
				if (All !=null){
					if(All.equals("1")){ // all user 
						cabang=new HashMap();
						cabang.put("KEY",currentUser.getLca_id());
						cabang.put("VALUE",currentUser.getCabang());
						lsRegion.add(cabang);
						lsAdmin=elionsManager.selectAllUserInCabang(lca_id); //all
					}else if (All.equals("2")){ //Regional User
						lsAdmin=elionsManager.selectAllUserRegional(lca_id); //reginonal
					}else if (All.equals("3")){ //agency
						lsAdmin=elionsManager.selectAllUserAgency(); //agency
					}else if (All.equals("4")){ //worksite
						lsAdmin=elionsManager.selectAllUserWorksite(lca_id); //worksite
					}
				}else{
					cabang=new HashMap();
					cabang.put("KEY",currentUser.getLca_id());
					cabang.put("VALUE",currentUser.getCabang());
					lsRegion.add(cabang);

				}
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		map.put("lock",lock);
		map.put("lsRegion", lsRegion);
		map.put("lca_id",lca_id);
		map.put("lus_id",lus_id);
		map.put("lsAgency", lsAgency);
		map.put("All", All);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/cetakPolisCabang", map);
	}

	public ModelAndView extraPremi(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
		Date sysdate=elionsManager.selectSysdate();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());	

		lsRegion=elionsManager.selectAllLstCab();
		Map cabang=new HashMap();
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		lsRegion.add(0, cabang);
		
		if (All !=null){
			if(All.equals("1")){ // all user 
				cabang=new HashMap();
				cabang.put("KEY",currentUser.getLca_id());
				cabang.put("VALUE",currentUser.getCabang());
				lsRegion.add(cabang);
				lsAdmin=elionsManager.selectAllUserInCabang(lca_id); //all
			}else{
				cabang=new HashMap();
				cabang.put("KEY",currentUser.getLca_id());
				cabang.put("VALUE",currentUser.getCabang());
				lsRegion.add(cabang);

			}
		}
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		map.put("lock",lock);
		map.put("lsRegion", lsRegion);
		map.put("lca_id",lca_id);
		map.put("lus_id",lus_id);
		map.put("lsAgency", lsAgency);
		map.put("All", All);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/extraPremi", map);
	}
	
	public ModelAndView cetakPolisCabangBas(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
		Date sysdate=elionsManager.selectSysdate();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());	

				lsRegion=elionsManager.selectAllLstCab();
				Map cabang=new HashMap();
				lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
				lsRegion.add(0, cabang);
		
				if (All !=null){
					if(All.equals("1")){ // all user 
						cabang=new HashMap();
						cabang.put("KEY",currentUser.getLca_id());
						cabang.put("VALUE",currentUser.getCabang());
						lsRegion.add(cabang);
						lsAdmin=elionsManager.selectAllUserInCabang(lca_id); //all
					}else if (All.equals("2")){ //Regional User
						lsAdmin=elionsManager.selectAllUserRegional(lca_id); //reginonal
					}else if (All.equals("3")){ //agency
						lsAdmin=elionsManager.selectAllUserAgency(); //agency
					}else if (All.equals("4")){ //worksite
						lsAdmin=elionsManager.selectAllUserWorksite(lca_id); //worksite
					}
				}else{
					cabang=new HashMap();
					cabang.put("KEY",currentUser.getLca_id());
					cabang.put("VALUE",currentUser.getCabang());
					lsRegion.add(cabang);

				}
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		map.put("lock",lock);
		map.put("lsRegion", lsRegion);
		map.put("lca_id",lca_id);
		map.put("lus_id",lus_id);
		map.put("lsAgency", lsAgency);
		map.put("All", All);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/cetakPolisCabangBas", map);
	}

	public ModelAndView reportPremiNonCash(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
		Date sysdate=elionsManager.selectSysdate();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		

		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/reportPremiNonCash", map);
	}
	
	public ModelAndView reportDataNasabah(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
		Date sysdate=elionsManager.selectSysdate();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String jenisReport=ServletRequestUtils.getStringParameter(request, "jenisReport","tanggal_input");
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		map.put("jenisReport", jenisReport);
		return new ModelAndView("uw/reportDataNasabah", map);
	}
	
	public ModelAndView listoutstandingkrg90hr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
		Date sysdate=elionsManager.selectSysdate();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());	

				lsRegion=elionsManager.selectAllLstCab();
				Map cabang=new HashMap();
				lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
				lsRegion.add(0, cabang);
		
				if (All !=null){
					if(All.equals("1")){ // all user 
						cabang=new HashMap();
						cabang.put("KEY",currentUser.getLca_id());
						cabang.put("VALUE",currentUser.getCabang());
						lsRegion.add(cabang);
						lsAdmin=elionsManager.selectAllUserInCabang(lca_id); //all
					}else if (All.equals("2")){ //Regional User
						lsAdmin=elionsManager.selectAllUserRegional(lca_id); //reginonal
					}else if (All.equals("3")){ //agency
						lsAdmin=elionsManager.selectAllUserAgency(); //agency
					}else if (All.equals("4")){ //worksite
						lsAdmin=elionsManager.selectAllUserWorksite(lca_id); //worksite
					}
				}else{
					cabang=new HashMap();
					cabang.put("KEY",currentUser.getLca_id());
					cabang.put("VALUE",currentUser.getCabang());
					lsRegion.add(cabang);

				}
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		map.put("lock",lock);
		map.put("lsRegion", lsRegion);
		map.put("lca_id",lca_id);
		map.put("lus_id",lus_id);
		map.put("lsAgency", lsAgency);
		map.put("All", All);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("report/listoutstandingkrg90hr",map);
	}
	
//	public ModelAndView report_TrkDtByTglRK(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Report report;
//		Map map = new HashMap();
//		Date now = this.elionsManager.selectSysdate();
//		Integer lock=0;
//		boolean show=false;
//		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(now));
//		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(now));
//		int lsbp_id = ServletRequestUtils.getIntParameter(request, "lsbp_id", 156);
//		String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
//		String startNominal = ServletRequestUtils.getStringParameter(request, "startNominal", "0");
//		String endNominal = ServletRequestUtils.getStringParameter(request, "endNominal", "99999999999");
//		
//		map.put("startDate", startDate);
//		map.put("endDate", endDate);
//		map.put("lsbp_id", lsbp_id);
//		map.put("kode", kode);
//		map.put("startNominal", startNominal);
//		map.put("endNominal", endNominal);
//		
//		map.put("daftarBank", this.elionsManager.selectDropDown("EKA.LST_BANK_PUSAT", "LSBP_ID", "LSBP_NAMA", "LSBP_NAMA", null));
//		
//		List<DropDown> daftarCab = new ArrayList<DropDown>();
//		daftarCab.add(0, new DropDown("", "ALL"));
//		daftarCab.addAll(this.elionsManager.selectDropDown("EKA.CAB_BSM", "KODE", "NAMA_CAB  || ' (' || KODE || ')'", "NAMA_CAB", null));
//		
//		map.put("daftarCab", daftarCab);
//		map.put("daftarDrek", this.elionsManager.selectMstDrek(lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal)));
//		
//		return new ModelAndView("uw/report_TrkDtByTglRK", map);
//	}

	public ModelAndView upload_icd_code(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommandUploadUw cmd = new CommandUploadUw();
		if(request.getParameter("upload") != null) {
			try {
				ServletRequestDataBinder binder;
				binder = createBinder(request, cmd);
				binder.bind(request);
				List<String> error = new ArrayList<String>();
				if(cmd.getFile1() == null) {
					error.add("Silahkan masukan file dengan tipe EXCEL (.xls)");
				}else if(cmd.getFile1().isEmpty() || cmd.getFile1().getOriginalFilename().toLowerCase().indexOf(".xls")==-1) {
					error.add("Silahkan masukan file dengan tipe EXCEL (.xls)");
				}else {
					cmd.setDaftarStatus(uwManager.uploadIcdCode(cmd,request));
				}
				cmd.setErrorMessages(error);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			}			
		}
		return new ModelAndView("common/upload_icd_code", "cmd", cmd);
	}
	
}