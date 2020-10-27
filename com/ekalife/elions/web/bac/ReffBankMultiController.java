package com.ekalife.elions.web.bac;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentMultiController;

/**
 * Layar Utama Input Reff Bank ke SPAJ
 * Termasuk dari BII, Bank Sinarmas, dan bank-bank lainnya
 * @author Yusuf Sutarko
 * @since Apr 1, 2008 (11:04:15 AM)
 */
public class ReffBankMultiController extends ParentMultiController {

	private boolean kosong(String tes) {
		if(tes!=null) {
			if(!tes.trim().equals("")) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Halaman utama referrer bank -> akan mengarahkan ke method masing-masing sesuai bank
	 * 
	 * @author Yusuf Sutarko
	 * @since Apr 1, 2008 (1:54:41 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", ServletRequestUtils.getStringParameter(request, "reg_spaj"));
		String pabsm = ServletRequestUtils.getStringParameter(request, "pabsm", ServletRequestUtils.getStringParameter(request, "pabsm"));
		Map info = this.elionsManager.selectInformasiRegionUntukReferral(spaj);
		String kodeRegion = "";
		int lsbs, lsdbs;
		String lsrg_id ="";
		if("y".equals(pabsm)){//produk pa bsm
			String produk = ServletRequestUtils.getStringParameter(request, "produk", ServletRequestUtils.getStringParameter(request, "produk"));
			lsbs = 46;
			lsdbs = Integer.parseInt(produk);
		}else{
			kodeRegion = (String) info.get("region");
			lsbs = (Integer) info.get("lsbs_id");
			lsdbs = (Integer) info.get("lsdbs_number");
			HashMap mapregion = Common.serializableMap(bacManager.selectRegion(spaj));
			lsrg_id =(String)mapregion.get("LSRG_ID");
		}

		Integer jnbankdetbisnis = bacManager.selectJnBankDetBisnis(lsbs, lsdbs);
		
		String reffothers = ServletRequestUtils.getStringParameter(request, "reffothers","");
		if(reffothers.trim().toLowerCase().equals("y")){
			//INSERT KE MST_REFF_BII dgn JENIS = 12(reff_id)
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=biiothers&editSPAJ="+spaj));						
		}else if(lsbs == 142 && (lsdbs == 2 || lsdbs == 3 || lsdbs == 5 || lsdbs == 6 || lsdbs == 7)
				|| (lsbs == 158 && lsdbs == 6)|| (lsbs == 183 &&  (lsdbs > 45 && lsdbs <61 )) || (lsbs == 208 && (lsdbs >= 5 && lsdbs <= 8)) 
				|| (lsbs == 208 && (lsdbs >= 25 && lsdbs <= 28)) ){
			//INSERT KE MST_REFF_BII dgn JENIS = 2
			
			//Ridhaal-Promo buy 1 get 1 untuk free produk lsbs == 183 &&   (lsdbs > 50 && lsdbs <60 ) dimana lakukan pengecekan terlebih dahulu di mst_free_spaj
			// dikarenakan kendala pada produk ini harus menginput kode referal sama dengan kode referal di produk utama
			//pastikan jika produk termasuk program promo, lakukan "Promo Activation", jika tidak data Agen dan Referal tidak bisa di input
//			List cekSpajPromo = bacManager.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
//			if(!cekSpajPromo.isEmpty()){//cek jika spaj Utama sudah terdaftar dengan spaj Free di MST_FREE_SPAJ
//				return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=shintacekbc&editSPAJ="+spaj+"&show_ajspusat=true"));
//			}else{
//				return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=shintacekbc&editSPAJ="+spaj+"&show_ajspusat=false"));
//				}
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=shintacekbc&editSPAJ="+spaj+"&show_ajspusat=false"));
		}else if((lsbs == 120 && (lsdbs == 22 || lsdbs == 23 || lsdbs == 24)) || lsbs == 213){
			//INSERT KE MST_REFF_BII dgn JENIS = 2
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=shintacekbc&editSPAJ="+spaj+"&show_ajspusat=true"));
		}else if((lsbs == 202 && (lsdbs == 4 || lsdbs == 5 || lsdbs == 6)) || lsbs == 216){
			//INSERT KE MST_REFF_BII dgn JENIS = 2
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=shintacekbcsyh&editSPAJ="+spaj+"&show_ajspusat=true"));
		}else if(jnbankdetbisnis == 16){
			//INSERT KE MST_REFF_BII dgn JENIS = 16
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=syariah&editSPAJ="+spaj+"&show_ajspusat=false"));
		}else if((lsbs == 190 && (lsdbs == 5 || lsdbs == 6)) || (lsbs == 183 && (lsdbs >= 91 && lsdbs <= 105)) ||
				(lsbs == 208 && (lsdbs >= 13 && lsdbs <= 16)) || (lsbs == 195 && (lsdbs >= 49 && lsdbs <= 60))){
			//INSERT KE MST_REFF_BII dgn JENIS = 20
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=victoria&editSPAJ="+spaj));
		}else if(lsbs == 200 && (lsdbs == 5 || lsdbs == 6)){
			//INSERT KE MST_REFF_BII dgn JENIS = 25
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=harda&editSPAJ="+spaj));
		}else if(jnbankdetbisnis == 43){//BTN Syariah //helpdesk [133346] produk baru 142-13 Smart Investment Protection
			//INSERT KE MST_REFF_BII dgn JENIS = 43
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=btnsyh&editSPAJ="+spaj));
		}else if(jnbankdetbisnis == 44){//BJB 
			//INSERT KE MST_REFF_BII dgn JENIS = 44
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=bjb&editSPAJ="+spaj));
		}else if(jnbankdetbisnis == 51){//BANK JATIM 
			//INSERT KE MST_REFF_BII dgn JENIS = 51
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=bjatim&editSPAJ="+spaj));
		}else if(jnbankdetbisnis == 58){//BANK JATIM SYARIAH
			//INSERT KE MST_REFF_BII dgn JENIS = 58
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=bjatimsyh&editSPAJ="+spaj));
		}else if(jnbankdetbisnis == 50){//BANK BUKOPIN 
			//INSERT KE MST_REFF_BII dgn JENIS = 50
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=bukopin&editSPAJ="+spaj));
		}else if(jnbankdetbisnis == 56){//BANK BTN SYARIAH
			//INSERT KE MST_REFF_BII dgn JENIS = 56
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=btnsyariah&editSPAJ="+spaj));
		}else if(jnbankdetbisnis == 60){//BANK BUKOPIN SYARIAH
			//INSERT KE MST_REFF_BII dgn JENIS = 60
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=bukopinsyariah&editSPAJ="+spaj));
		}else if(jnbankdetbisnis == 63){//BTN //helpdesk [142003] produk baru Smart Platinum Link RPUL BEL (134-13)
			//INSERT KE MST_REFF_BII dgn JENIS = 63
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=btn&editSPAJ="+spaj));
		}
		else if(kodeRegion.startsWith("09")){
			if (kodeRegion.equals("0915")){
				//INSERT KE MST_REFF_CIC
				return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/cicreff_bii.htm?sts=awal&editSPAJ="+spaj));
			}else if (kodeRegion.equals("0921") || kodeRegion.equals("0916") || kodeRegion.equals("0926")){
				//INSERT KE MST_REFF_BII dgn JENIS = 2 
				return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=shinta&editSPAJ="+spaj));
			}else if (kodeRegion.equals("0902") && lsrg_id.equals("03")){//Insurance Specialist
				//INSERT KE MST_REFF_BII dgn JENIS = 2 
				return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=shinta&editSPAJ="+spaj));
			}else if (kodeRegion.equals("0902") || kodeRegion.equals("0922") || kodeRegion.equals("0930") || jnbankdetbisnis==3){
				//INSERT KE MST_REFF_BII dgn JENIS = 3
				return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=simas&editSPAJ="+spaj));
			}else if (kodeRegion.equals("0938")){
				//INSERT KE MST_REFF_BII dgn jenis = 42 (provestara BRI)
				return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=bri&editSPAJ="+spaj));
			}else if (kodeRegion.equals("0940")){
				//INSERT KE MST_REFF_BII dgn jenis = 51 (Bank Jatim)
				return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=jatim&editSPAJ="+spaj));
			}
			else{
				//UPDATE KE MST_POLICY SET mspo_plan_provider 
				//return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/reff_bank.htm?window=reff_new&spaj="+spaj));
				return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=shinta&editSPAJ="+spaj));
			}
		}else if(kodeRegion.equals("4001") && lsrg_id.equals("02")){
			//INSERT KE MST_REFF_BII dgn JENIS = 2 
			   return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=shinta&editSPAJ="+spaj));
		}else if ("y".equals(pabsm)){
			//INSERT KE MST_REFF_BII dgn JENIS = 2 
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/shintareff_bii.htm?sts=awal&flag=shinta&pabsm=y&editSPAJ="+spaj));
		}else{
			return new ModelAndView(new RedirectView(request.getContextPath()+"/include/page/blocked.jsp?jenis=referral"));
		}
	}

	/**
	 * Referral untuk Polis Powersave yang dijual di DM/TM (142-8), bukan bank sih, tapi masukin sini aja
	 * Komisi tm (berupa report) ambil dari lst_sales_tm@eb link dgn mst_policy.mspo_no_kerjasama
	 * 
	 * @author Yusuf
	 * @since Aug 1, 2008 (9:02:14 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView reff_dmtm(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		Map cmd = new HashMap();
		
		String spaj 	= ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		String aksi 	= ServletRequestUtils.getStringParameter(request, "aksi", "");
		String cari 	= ServletRequestUtils.getStringParameter(request, "cari", "");
		String tm_id 	= ServletRequestUtils.getStringParameter(request, "tm_id", "");
		String tm_code 	= ServletRequestUtils.getStringParameter(request, "tm_code", "");
		String spv_code	= ServletRequestUtils.getStringParameter(request, "spv_code", "");
		String spv_name	= ServletRequestUtils.getStringParameter(request, "spv_name", "");
		
		cmd.put("spaj", 	spaj);
		cmd.put("cari", 	cari);
		cmd.put("tm_id", 	tm_id);
		cmd.put("tm_code", 	tm_code);
		cmd.put("spv_code", spv_code);
		cmd.put("spv_name", spv_name);
		
		String lca_id = uwManager.selectLcaIdMstPolicyBasedSpaj(spaj);
		if(!lca_id.equals("40")){
			cmd.put("error", "Polis ini bukan polis DMTM, jadi tidak dapat input reff DMTM");
		}
		
		if(aksi.equals("cari")) {
			if(cari.trim().equals("")) {
				cmd.put("error", "Silahkan masukkan ID / NAMA TELEMARKETER yang dicari");
			} else {
				cmd.put("daftarTelemarketer", uwManager.selectCariTelemarketer(cari));
			}
		}else if(aksi.equals("simpan")) {
			if(kosong(tm_id)) cmd.put("error", "Silahkan masukkan ID Telemarketer");
			else {
				uwManager.saveTelemarketer(cmd);
				cmd.put("success", "Telemarketer berhasil disimpan");
			}
		}

		cmd.putAll(uwManager.selectTelemarketerFromSpaj(spaj));
		
		return new ModelAndView("bac/reff_dmtm", "cmd", cmd);
	}
	
	public ModelAndView spv_dmtm(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		Map cmd = new HashMap();
		
		String spaj 	= ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		String aksi 	= ServletRequestUtils.getStringParameter(request, "aksi", "");
		String cari 	= ServletRequestUtils.getStringParameter(request, "cari", "");
		String spv_code	= ServletRequestUtils.getStringParameter(request, "spv_code", "");
		String spv_name	= ServletRequestUtils.getStringParameter(request, "spv_name", "");
		
		cmd.put("spaj", 	spaj);
		cmd.put("cari", 	cari);
		cmd.put("spv_code", spv_code);
		cmd.put("spv_name", spv_name);
		
		String lca_id = uwManager.selectLcaIdMstPolicyBasedSpaj(spaj);
		if(!lca_id.equals("40")){
			cmd.put("error", "Polis ini bukan polis DMTM, jadi tidak dapat input SPV reff DMTM");
		}
		
		if(aksi.equals("cari")) {
			if(cari.trim().equals("")) {
				cmd.put("error", "Silahkan masukkan ID / NAMA TELEMARKETER yang dicari");
			} else {
				cmd.put("daftarTelemarketer", uwManager.selectCariSPVTelemarketer(cari));
			}
		}else if(aksi.equals("simpan")) {
			if(kosong(spv_code)) cmd.put("error", "Silahkan masukkan ID SPV Telemarketer");
			else {
				uwManager.saveSPVTelemarketer(cmd);
				cmd.put("success", "SPV Telemarketer berhasil disimpan");
			}
		}

		cmd.putAll(uwManager.selectTelemarketerFromSpaj(spaj));
		
		return new ModelAndView("bac/spv_dmtm", "cmd", cmd);
	}
	
	/**
	 * Reff untuk Bank BII -> EKA.MST_POLICY.MSPO_PLAN_PROVIDER yang mereferensikan EKA.MST_NASABAH
	 * 
	 * @author Yusuf Sutarko
	 * @since Apr 1, 2008 (1:51:56 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView reff_new(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		Map cmd = new HashMap();
		
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		String aksi = ServletRequestUtils.getStringParameter(request, "aksi", "");
		String cari = ServletRequestUtils.getStringParameter(request, "cari", "");
		String lead = ServletRequestUtils.getStringParameter(request, "lead", "");
		String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
		String nama = ServletRequestUtils.getStringParameter(request, "nama", "");
		String nm_lead=ServletRequestUtils.getStringParameter(request, "nm_lead","");
		String jab_lead=ServletRequestUtils.getStringParameter(request, "jab_lead","");
		String nama_ref=ServletRequestUtils.getStringParameter(request, "nama_ref","");
		String jab_ref=ServletRequestUtils.getStringParameter(request, "jab_ref","");
		String cabang=ServletRequestUtils.getStringParameter(request, "cabang","");
		
		cmd.put("spaj", spaj);
		cmd.put("cari", cari);
		cmd.put("lead", lead);
		cmd.put("kode", kode);
		cmd.put("nama", nama);
		cmd.put("nm_lead", nm_lead);
		cmd.put("jab_lead", jab_lead);
		cmd.put("nama_ref", nama_ref);
		cmd.put("jab_ref", jab_ref);
		cmd.put("cabang", cabang);
		
		if(aksi.equals("cari")) {
			if(cari.trim().equals("")) {
				cmd.put("error", "Silahkan masukkan nama / nomor lead yang dicari");
			} else {
				cmd.put("daftarNasabah", uwManager.selectCariNasabah(cari));
			}
		}else if(aksi.equals("simpan")) {
			if(kosong(lead)) cmd.put("error", "Silahkan masukkan nomor lead");
			else if(kosong(kode) || kosong(nama)) cmd.put("error", "Nomor tidak ditemukan. Silahkan cek pada aplikasi BancAssurance");
			else {
				uwManager.saveLeadReffBii(cmd);
				cmd.put("success", "Referral BII berhasil disimpan");
			}
		}

		cmd.putAll(uwManager.selectLeadNasabahFromSpaj(spaj));
		
		return new ModelAndView("bac/reff_bii_new", "cmd", cmd);
	}

}
	