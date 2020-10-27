package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.utils.parent.ParentMultiController;

/**
 * MultiController untuk Simas Card
 * @author Yusuf
 * @since Sep 11, 2008 (1:29:09 PM)
 */
public class SimasCardMultiController extends ParentMultiController {

	/**
	 * Cetak Simas Card
	 * @author Yusuf
	 * @since Sep 11, 2008 (1:29:22 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView print(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String format = "";
		String buttontype = "";
		
		if(request.getParameter("retrieve") != null) {
			format = "html";
			buttontype = null;
		}else if(request.getParameter("print") != null) {
			format = "pdf";
			buttontype = "print";
		}
		
		
		
		//bila cetak dokumen
		if(!format.equals("")) {
			return new ModelAndView(new RedirectView(
					request.getContextPath() + "/report/bac." + format + 
					"?window=simas_card" + 
					"&cetak=" + ServletRequestUtils.getStringParameter(request, "cetak") + 
					"&jenis=" + ServletRequestUtils.getStringParameter(request, "jenis") + 
					"&cabang=" + ServletRequestUtils.getStringParameter(request, "cabang") + 
					"&cabangBII=" + ServletRequestUtils.getStringParameter(request, "cabangBII", "") + 
					"&cabangBankSinarmas=" + ServletRequestUtils.getStringParameter(request, "cabangBankSinarmas", "") + 
					"&jumlah_print=" + ServletRequestUtils.getStringParameter(request, "jumlah_print") +
					"&format=" + format +
					"&print=" + buttontype
					));
			
		//bila tampilkan window print simas card
		}else {
			Map<String, Object> m = new HashMap<String, Object>();
			
			List<DropDown> daftarCetak = new ArrayList<DropDown>();
			daftarCetak.add(new DropDown("0", "Kartu"));
			daftarCetak.add(new DropDown("1", "Surat"));
//			daftarCetak.add(new DropDown("2", "Kartu Baru"));
			m.put("daftarCetak", daftarCetak);
			
			m.put("daftarJenis", elionsManager.selectJenisSimasCard());
			
			List<DropDown> daftarCabang = elionsManager.selectCabangSimasCard();
			daftarCabang.add(0, new DropDown("ALL", "ALL"));
			m.put("daftarCabang", daftarCabang);
			
			List<DropDown> daftarCabangBII = elionsManager.selectCabangBankSimasCard(0);
			//daftarCabangBII.add(0, new DropDown("ALL", "ALL"));
			m.put("daftarCabangBII", daftarCabangBII);
			
			List<DropDown> daftarCabangBankSinarmas = elionsManager.selectCabangBankSimasCard(1);
			//daftarCabangBankSinarmas.add(0, new DropDown("ALL", "ALL"));
			m.put("daftarCabangBankSinarmas", daftarCabangBankSinarmas);
			
			return new ModelAndView("uw/simas_card/print", m);
		}
	}
	
	
	public ModelAndView print_vip(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String format = "";
		String buttontype = "";
		
		if(request.getParameter("retrieve") != null) {
			format = "html";
			buttontype = null;
		}else if(request.getParameter("print") != null) {
			format = "pdf";
			buttontype = "print";
		}
		
		
		
		//bila cetak dokumen
		if(!format.equals("")) {
			return new ModelAndView(new RedirectView(
					request.getContextPath() + "/report/bac." + format + 
					"?window=simas_card_vip" + 
					"&cetak=" + ServletRequestUtils.getStringParameter(request, "cetak") + 
					"&jenis=" + ServletRequestUtils.getStringParameter(request, "jenis") + 
					"&cabang=" + ServletRequestUtils.getStringParameter(request, "cabang") + 
					"&cabangBII=" + ServletRequestUtils.getStringParameter(request, "cabangBII", "") + 
					"&cabangBankSinarmas=" + ServletRequestUtils.getStringParameter(request, "cabangBankSinarmas", "") + 
					"&jumlah_print=" + ServletRequestUtils.getStringParameter(request, "jumlah_print") +
					"&format=" + format +
					"&print=" + buttontype
					));
			
		//bila tampilkan window print simas card
		}else {
			Map<String, Object> m = new HashMap<String, Object>();
			
			List<DropDown> daftarCetak = new ArrayList<DropDown>();
			daftarCetak.add(new DropDown("0", "Kartu"));
			daftarCetak.add(new DropDown("1", "Surat"));
//			daftarCetak.add(new DropDown("2", "Kartu Baru"));
			m.put("daftarCetak", daftarCetak);
			
//			List<DropDown> daftarJenis = elionsManager.selectJenisSimasCard();
			List<DropDown> daftarJenis = new ArrayList<DropDown>();
			daftarJenis.add(new DropDown("9", "Syariah Card"));
			m.put("daftarJenis", daftarJenis);
			
			List<DropDown> daftarCabang = elionsManager.selectCabangSimasCard();
			daftarCabang.add(0, new DropDown("ALL", "ALL"));
			m.put("daftarCabang", daftarCabang);
			
			List<DropDown> daftarCabangBII = elionsManager.selectCabangBankSimasCard(0);
			//daftarCabangBII.add(0, new DropDown("ALL", "ALL"));
			m.put("daftarCabangBII", daftarCabangBII);
			
			List<DropDown> daftarCabangBankSinarmas = elionsManager.selectCabangBankSimasCard(1);
			//daftarCabangBankSinarmas.add(0, new DropDown("ALL", "ALL"));
			m.put("daftarCabangBankSinarmas", daftarCabangBankSinarmas);
			
			return new ModelAndView("uw/simas_card/print_vip", m);
		}
	}	
	
	
public ModelAndView distribution(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String format = "";
		String buttontype = "";
		
		if(request.getParameter("retrieve") != null) {
			format = "html";
			buttontype = null;
		}else if(request.getParameter("print") != null) {
			format = "pdf";
			buttontype = "print";
		}
		
		
		
		//bila cetak dokumen
		if(!format.equals("")) {
			return new ModelAndView(new RedirectView(
					request.getContextPath() + "/report/bac." + format + 
					"?window=simas_card_distribution" + 
					"&cabang=" + ServletRequestUtils.getStringParameter(request, "cabang") + 
					"&cabangBII=" + ServletRequestUtils.getStringParameter(request, "cabangBII", "") + 
					"&cabangBankSinarmas=" + ServletRequestUtils.getStringParameter(request, "cabangBankSinarmas", "") + 
					"&format=" + format +
					"&print=" + buttontype
					));
			
		//bila tampilkan window print simas card
		}else {
			Map<String, Object> m = new HashMap<String, Object>();
			
//			m.put("daftarJenis", elionsManager.selectJenisSimasCard());
			
			List<DropDown> daftarCabang = elionsManager.selectCabangSimasCard();
			daftarCabang.add(0, new DropDown("ALL", "ALL"));
			m.put("daftarCabang", daftarCabang);
			
			List<DropDown> daftarCabangBII = elionsManager.selectCabangBankSimasCard(0);
			//daftarCabangBII.add(0, new DropDown("ALL", "ALL"));
			m.put("daftarCabangBII", daftarCabangBII);
			
			List<DropDown> daftarCabangBankSinarmas = elionsManager.selectCabangBankSimasCard(1);
			//daftarCabangBankSinarmas.add(0, new DropDown("ALL", "ALL"));
			m.put("daftarCabangBankSinarmas", daftarCabangBankSinarmas);
			
			return new ModelAndView("uw/simas_card/print", m);
		}
	}

	/**
	 * Cari Simas Card
	 * 
	 * @author Yusuf
	 * @since Sep 11, 2008 (1:40:28 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView cari(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		
		List<DropDown> daftar = new ArrayList<DropDown>();
		daftar.add(new DropDown("0", "Individu (NB)"));
		daftar.add(new DropDown("1", "Individu (Existing)"));
		daftar.add(new DropDown("2", "Direksi"));
		daftar.add(new DropDown("3", "Rekanan Direksi"));
		daftar.add(new DropDown("4", "Merchant"));
		daftar.add(new DropDown("5", "Marketing"));
		daftar.add(new DropDown("6", "Karyawan MKL"));
		daftar.add(new DropDown("7", "Karyawan BO"));
		m.put("daftar", daftar);
		
		List<DropDown> daftarJenis = new ArrayList<DropDown>();
		daftarJenis.add(new DropDown("0", "Nama"));
		daftarJenis.add(new DropDown("1", "Nomor SPAJ"));
		daftarJenis.add(new DropDown("2", "Nomor Polis"));
		m.put("daftarJenis", daftarJenis);
		
		int jenis = ServletRequestUtils.getIntParameter(request, "jenis", 0);
		String kata = ServletRequestUtils.getStringParameter(request, "kata", "");
		m.put("jenis", jenis);
		m.put("kata", kata);
		
		if(request.getParameter("search") != null) {
			m.put("daftarSimasCard", elionsManager.selectCariSimasCard(jenis, kata));
		}
		
		return new ModelAndView("uw/simas_card/cari", m);
	}

}