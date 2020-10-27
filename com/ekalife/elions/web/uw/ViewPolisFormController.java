/**
 * 
 */
package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Hadiah;
import com.ekalife.elions.model.Insured;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.ViewPolis;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentFormController;

import id.co.sinarmaslife.std.model.vo.DropDown;

/**
 * @author Yusuf
 *Tambahan yang di comment di data usulan
 		<!--
	    <display:column title="Discount" >
	    	<elions:currency jumlah="${produk.DISC}" /><br/>
	    	<elions:currency jumlah="${produk.MSPR_DISCOUNT}" />
	    </display:column>
		<display:column property="TOTAL7" title="Total" format="{0, number, #,##0.00;(#,##0.00)}" total="true"/>
		<display:column title="%">
			<input class="noBorder" type="checkbox" disabled <c:if test="${produk.MSPR_PERSEN eq 1}" >checked</c:if>>
		</display:column>
		-->
*/
public class ViewPolisFormController extends ParentFormController{

	protected HashMap referenceData(HttpServletRequest request, Object command, Errors err) throws Exception {
		User user = (User) request.getSession().getAttribute("currentUser");
		ViewPolis viewPolis=(ViewPolis)command;
		HashMap map = new HashMap();
		String spaj = request.getParameter("showSPAJ");
		
		String reins=request.getParameter("reins");
		map.put("reins", reins);
		//
		HashMap mapNsb= Common.serializableMap(uwManager.selectLeadNasabahFromSpaj(spaj)) ;
		Pemegang pemegang = elionsManager.selectpp(spaj);
		Tertanggung ttg=elionsManager.selectttg(spaj);
		PembayarPremi p_premi = bacManager.selectP_premi(spaj);
		if(pemegang!=null && user.getLde_id().equals("11") ){
			ArrayList viewer = Common.serializableList(this.uwManager.selectHistoryPengajuan(pemegang.getMcl_first(),pemegang.getMspe_date_birth()));
			ArrayList blacklist = Common.serializableList(this.uwManager.selectBlacklist(pemegang.getMcl_first(), "mcl_first", pemegang.getMspe_date_birth()));
			map.put("blacklist", blacklist);
			map.put("history_pengajuan", viewer);
		}
		String no_pb= uwManager.selectNoPB(spaj);
		Integer jnsNasabah=(Integer)mapNsb.get("JN_NASABAH");
		String infoNasabah=null;
		if(jnsNasabah==null)
			jnsNasabah=0;
		if(jnsNasabah.intValue()==1){
			infoNasabah="Platinum!";
		}else if(jnsNasabah.intValue()==2){
			infoNasabah="Reguler!";
		}else if(jnsNasabah.intValue()==3){
			infoNasabah="Gold Link!";
		}else if(jnsNasabah.intValue()==4){
			infoNasabah="Prolink!";
		}
		map.put("no_pb", no_pb);
		map.put("infoNasabah", infoNasabah);
		
		Integer lstbId = uwManager.getLstbId(spaj);
		if(request.getParameter("showSPAJ")!=null && lstbId == 1){
		
			//dropdowns
			map.put("select_identitas",DroplistManager.getInstance().get("IDENTITY.xml","ID",request));
			map.put("select_negara",DroplistManager.getInstance().get("WARGANEGARA.xml","ID",request));
			map.put("select_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","",request));
			map.put("select_pekerjaan",DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request));/*DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request)*/
			map.put("select_lst_pekerjaan", elionsManager.selectDropDown("eka.lst_pekerjaan", "lsp_id", "lsp_name", "", "lsp_id", "flag_active=1"));
			map.put("select_medis",DroplistManager.getInstance().get("medis.xml","ID",request));
			map.put("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
			map.put("select_agama",DroplistManager.getInstance().get("AGAMA.xml","ID",request));
			map.put("select_pendidikan",DroplistManager.getInstance().get("PENDIDIKAN.xml","ID",request));
			map.put("select_tujuan",DroplistManager.getInstance().get("TUJUAN_ASR.xml","",request));
			map.put("select_penghasilan",DroplistManager.getInstance().get("PENGHASILAN.xml","",request));
			map.put("select_dana",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
			map.put("select_hasil",DroplistManager.getInstance().get("SUMBER_PENGHASILAN.xml","",request));
			map.put("select_dana_badan_usaha",DroplistManager.getInstance().get("SUMBER_PENDANAAN_BADAN_USAHA.xml","",request));
			map.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
			map.put("select_relasi_non_sendiri",DroplistManager.getInstance().get("RELATION_NON_SENDIRI.xml","",request));
			map.put("listlsdbs", uwManager.select_detilprodukutama_viewer(Integer.valueOf(uwManager.selectBusinessId(spaj)),spaj));
			map.put("listtipeproduk", elionsManager.select_tipeproduk());
			map.put("select_jns_top_up",DroplistManager.getInstance().get("TOPUP.xml","ID",request));	
			map.put("select_jangka_invest",DroplistManager.getInstance().get("JNSTOPUP.xml","ID",request));
			map.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER.xml","ID",request));
			map.put("select_jenisbunga",DroplistManager.getInstance().get("jenisbunga.xml","",request));
			map.put("select_karyawan",DroplistManager.getInstance().get("karyawan.xml","",request));
			map.put("lsPanes", selectLsPanes(ServletRequestUtils.getStringParameter(request, "c",""), viewPolis, user));
			map.put("select_gelar",DroplistManager.getInstance().get("GELAR.xml","ID",request));
			map.put("select_relasi_premi", DroplistManager.getInstance().get("RELATION_PREMI.xml","ID",request));
			map.put("select_cpp", elionsManager.selectDropDown("eka.lst_relation a,eka.lst_group_relation b", "a.lsre_id", "a.lsre_relation", "", "a.lsre_id", "a.lsre_id = b.lsre_id and b.gr_lsre_id=4"));
			map.put("select_relasi_badan_usaha",DroplistManager.getInstance().get("RELATION_BADAN_USAHA.xml","",request));
			map.put("select_aset",DroplistManager.getInstance().get("ASET.xml","",request));
			map.put("gelar", elionsManager.selectGelar(1));
			map.put("bang", this.uwManager.selectBankPusat());
			map.put("bang2", uwManager.selectBank());
			map.put("select_kurs",DroplistManager.getInstance().get("KURS.xml","ID",request));
			map.put("virtual_account", uwManager.selectVirtualAccountSpaj(spaj));
			map.put("select_jenis_tabungan",DroplistManager.getInstance().get("jenis_tabungan.xml","ID",request));
			map.put("select_jenis_nasabah",DroplistManager.getInstance().get("jenis_nasabah.xml","ID",request));
			ArrayList<DropDown> bidangUsahaList = Common.serializableList(((List<DropDown>)elionsManager.selectBidangUsaha(2)));
			bidangUsahaList.addAll(Common.serializableList((List<DropDown>)elionsManager.selectBidangUsaha(1)));
			map.put("bidangUsaha", bidangUsahaList);
			map.put("select_medis_desc", new ArrayList(bacManager.selectParamMedisDesc()) ); // Tambah nana medis value mst_config helpdesk 148420
			map.put("select_full_autosales", new ArrayList(bacManager.selectParamFullAutoSalesConfig()) ); // full autosales

			map.put("pin", elionsManager.selectValidasiPinjaman(spaj));
			if(ttg!=null && user.getLde_id().equals("11")){
				
				Integer spasi=ttg.getMcl_first().indexOf(' ');
				Integer titik=ttg.getMcl_first().indexOf('.');
				Integer koma=ttg.getMcl_first().indexOf(',');
				Integer pjg=ttg.getMcl_first().length();
				String nama="";
				nama=ttg.getMcl_first();
				if(spasi>0)
					nama=ttg.getMcl_first().substring(0,spasi);
				else if(titik>0)
					nama=ttg.getMcl_first().substring(0,titik);
				else if(koma>0)
					nama=ttg.getMcl_first().substring(0,koma);
				map.put("healthproduct", Common.serializableList(uwManager.selectCekHealthProduct(nama, ttg.getMspe_date_birth())));
				map.put("spaj", spaj);
			}
			
		}
		
//		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		if(currentUser.getCab_bank() != null) {
//			map.put("disableEditRekening", "true");
//		}
		
		//TANDA UNTUK BLACKLIST
		String blacklistFont = "black";
		if(viewPolis.getPemegang() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String tgl_lahir_pemegang = sdf.format(viewPolis.getPemegang().getMspe_date_birth());
			String blName = uwManager.selectCekBlacklistDirect(viewPolis.getPemegang().getMcl_first(), tgl_lahir_pemegang);
				//elionsManager.selectCekBlacklist(viewPolis.getPemegang().getReg_spaj());
			if(viewPolis.getPemegang().getMcl_first().equals(blName)){
				blacklistFont = "blue";
				request.setAttribute("ppf", "Pemegang Polis");
			}
		}
		if(viewPolis.getTertanggung() != null){
			if(viewPolis.getTertanggung().getMspe_date_birth() != null){
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String tgl_lahir_tertanggung = sdf.format(viewPolis.getTertanggung().getMspe_date_birth());
				String blName = uwManager.selectCekBlacklistDirect(viewPolis.getTertanggung().getMcl_first(), tgl_lahir_tertanggung);
				if(viewPolis.getTertanggung().getMcl_first().equals(blName)){
					blacklistFont = "blue";
					request.setAttribute("terf", "Tertanggung");
				}
			}
		}
		if(viewPolis.getPemegang() != null){
			int red_flag =uwManager.selectRedFlag(pemegang.getMcl_id());
			if(red_flag==1){
				blacklistFont = "red";
				request.setAttribute("ppf", "Pemegang Polis");
			}
		}
		if(viewPolis.getTertanggung() != null){
			Integer red_flag =uwManager.selectRedFlag(ttg.getMcl_id());
			if(red_flag==1){
					blacklistFont = "red";
					request.setAttribute("terf", "Tertanggung");
			}
		}
		
		if("blue".equals(blacklistFont)||"red".equals(blacklistFont)){
			String x = request.getParameter("halForBlacklist");
			request.setAttribute("blacklistFont", blacklistFont);
			request.setAttribute("deu", request.getParameter("halForBlacklist"));
		}
		//=========================
		if(viewPolis.getDataUsulan() != null &&
				((viewPolis.getDataUsulan().getLsbs_id() == 212 && viewPolis.getDataUsulan().getLsdbs_number() == 8)
					|| (viewPolis.getDataUsulan().getLsbs_id() == 73 && viewPolis.getDataUsulan().getLsdbs_number() == 15) 
					|| (viewPolis.getDataUsulan().getLsbs_id() == 203 && viewPolis.getDataUsulan().getLsdbs_number() == 4 )
					)) //SIO(GIO)/SIO(guaranteed)/GIO(Q)
		{
			map.put("jenisSpaj", "GIO(Q)");
		}
		//=======================
		
		//Yusuf 7 okt 09 - polis lama hasil surrender endorsement
		map.put("polisLama", Common.serializableMap(uwManager.selectPolisLamaSurrenderEndorsement(spaj)));
		
		//Ridhaal 30 Aug 16 - No E-SPAJ / No Gadget
				map.put("gadget", Common.serializableMap(bacManager.selectNoGadgetESPAJ(spaj)));
		
		String dept = user.getLde_id();
		Integer idx=user.getLus_id().indexOf(props.getProperty("access.viewer.edit"));
		if(("11".indexOf(dept.trim()) > -1) || idx>=0)  {
			request.setAttribute("boleh", true);
			if(viewPolis.getTertanggung()!=null){
				if(viewPolis.getTertanggung().getLspd_id()<=6 || viewPolis.getTertanggung().getLspd_id()==9 || viewPolis.getTertanggung().getLspd_id()==27){ 
					viewPolis.setEditFlag(1);
					viewPolis.setFlagEditAll(1);
				}else
					viewPolis.setFlagEditAll(2);
			}
		}
		
		return map;
	}
	
	private ArrayList selectLsPanes(String c, ViewPolis viewPolis, User currentUser) {
		ArrayList lsPanes=new ArrayList();
		HashMap map;
		String[] names;
		String[] pages;

		//view dobel blanko
		if(c.equals("c")) {
			names=new String[]{"Pemegang Polis","Tertanggung","Agen","Data Usulan","Billing",};
			pages=new String[]{"info_pemegang_cabang.jsp","info_tertanggung_cabang.jsp","info_agen.jsp","info_datausulan.jsp",
							"info_billing.jsp"};
		//info Blacklist
		}else if(c.equals("at")) {
			names=new String[]{"Pemegang Polis","Tertanggung","Riwayat Polis",};
			pages=new String[]{"info_pemegang_cabang.jsp","info_tertanggung_cabang.jsp","riwayat_spaj.jsp"};
		//admin cabang
		}else if(currentUser.getLde_id().equals("19") || currentUser.getLde_id().equals("20")) {
			names=new String[]{"Pemegang Polis","Tertanggung","Agen","Data Usulan","Billing", "Investasi"};
			pages=new String[]{"info_pemegang_cabang.jsp","info_tertanggung_cabang.jsp","info_agen.jsp","info_datausulan_cabang.jsp",
							"info_billing.jsp", "info_investasi_cabang.jsp"};
		//admin bsm
		}else if(currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 3) {
			names=new String[]{"Pemegang Polis","Tertanggung","Agen","Data Usulan","Billing","Investasi","Edit","History Polis"};
			pages=new String[]{"info_pemegang.jsp","info_tertanggung.jsp","info_agen.jsp","info_datausulan.jsp",
							"info_billing.jsp","info_investasi.jsp"};
		}else if(currentUser.getLde_id().equals("11")){
			/*names=new String[]{"Pemegang Polis","Tertanggung","Agen","Data Usulan","Billing","Investasi","Edit","History Polis","Riwayat Polis"};
			pages=new String[]{"info_pemegang.jsp","info_tertanggung.jsp","info_agen.jsp","info_datausulan.jsp",
							"info_billing.jsp","info_investasi.jsp","info_edit.jsp","ulangan.jsp","riwayat_spaj.jsp"};*/
			names=new String[]{"Pemegang Polis","Tertanggung","Pembayar Premi","Agen","Data Usulan","Billing","Investasi","Edit","History Polis","Riwayat Polis"};
			pages=new String[]{"info_pemegang.jsp","info_tertanggung.jsp","info_pembayar_premi.jsp","info_agen.jsp","info_datausulan.jsp",
							"info_billing.jsp","info_investasi.jsp","info_edit.jsp","ulangan.jsp","riwayat_spaj.jsp"};
		}
		else {
		/*	names=new String[]{"Pemegang Polis","Tertanggung","Agen","Data Usulan","Billing","Investasi","Edit","History Polis"};
			pages=new String[]{"info_pemegang.jsp","info_tertanggung.jsp","info_agen.jsp","info_datausulan.jsp",
							"info_billing.jsp","info_investasi.jsp","info_edit.jsp","ulangan.jsp"};*/
			names=new String[]{"Pemegang Polis","Tertanggung","Pembayar Premi","Agen","Data Usulan","Billing","Investasi","Edit","History Polis","Riwayat Polis"};
			pages=new String[]{"info_pemegang.jsp","info_tertanggung.jsp","info_pembayar_premi.jsp","info_agen.jsp","info_datausulan.jsp",
							"info_billing.jsp","info_investasi.jsp","info_edit.jsp","ulangan.jsp","riwayat_spaj.jsp"};
		}
		
		for (int i=0;i<pages.length;i++){
			map=new HashMap();
			map.put("FLAG", viewPolis.getEditFlag());
			map.put("NAMES",names[i] );
			map.put("PAGES",pages[i] );
			lsPanes.add(map);
		}
		return lsPanes;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String info=request.getParameter("info");
		String suc=request.getParameter("suc");
		ViewPolis viewPolis = new ViewPolis();
		viewPolis.setCurrentTimeMillis(System.currentTimeMillis());
		User user = (User) request.getSession().getAttribute("currentUser");
		Date jan2016 = defaultDateFormat.parse(props.getProperty("jan.2016"));
		
		if(info!=null)
			viewPolis.setInfo(new Integer(1));
		viewPolis.setSuc(request.getParameter("suc"));
		//
		viewPolis.setFlagEditAll(0);
		if(request.getParameter("showSPAJ")!=null){
			String spaj = request.getParameter("showSPAJ");
			Integer lstbId = uwManager.getLstbId(spaj);
			
			if(lstbId == 1) {
				String businessID = uwManager.selectBusinessId(spaj);
				
				//Data utama yang ditampilkan
				Pemegang pemegang = elionsManager.selectpp(spaj);
				//tambahan untuk badan usaha
				Personal personal = new Personal();
				ContactPerson contactPerson = new ContactPerson();
				if(pemegang != null){
					if(pemegang.getMcl_jenis() == 1){
						//mst_company//mst_company_contact//mst_company_contact_address//mst_company_contact_family
						personal = elionsManager.selectProfilePerusahaan(pemegang.getMcl_id());
						contactPerson = elionsManager.selectpic(pemegang.getMcl_id());
						if(pemegang.getMkl_industri() != null){
							if(pemegang.getMkl_industri().equalsIgnoreCase("LAIN - LAIN") || pemegang.getMkl_industri().equalsIgnoreCase("LAINNYA")){
								//pemegang.setIndustria("");
							}else{
								pemegang.setIndustria("");
							}
						}
					}
				}
				//lockSPAJ di inbox khusus UW
				/*if(user.getLde_id().equals("11")){
					bacManager.updateMstInboxLockId(spaj);
				}*/
				Tertanggung tertanggung = elionsManager.selectttg(spaj);
				PembayarPremi p_premi = bacManager.selectP_premi(spaj);
				Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
				if(dataUsulan == null) dataUsulan = new Datausulan();
				dataUsulan.setWorksite(Common.serializableMap(elionsManager.selectPerusahaanWorksite(spaj)) );
				dataUsulan.setPsn(Common.serializableMap(bacManager.selectIsPSNSyariah(spaj)) );
				HashMap docPos = Common.serializableMap(elionsManager.selectViewerDocPosition(spaj));
				viewPolis.setPersonal(personal);
				viewPolis.setContactPerson(contactPerson);
				viewPolis.setPolicyNo(elionsManager.selectPolicyNumberFromSpaj(spaj));
				viewPolis.setPemegang(pemegang);
				viewPolis.setRekening(elionsManager.selectRekeningNasabah(spaj));
				viewPolis.setRekening_autodebet(elionsManager.selectRekeningAutoDebet(spaj));
				viewPolis.getPemegang().setDaftarKyc(Common.serializableList(uwManager.selectDaftar_kyc(spaj)));
				viewPolis.getPemegang().setDaftarKyc2(Common.serializableList(uwManager.selectDaftar_kyc2(spaj)));
				viewPolis.setTertanggung(tertanggung);
				viewPolis.setDataUsulan(dataUsulan);
				viewPolis.setUser(user);
				viewPolis.setAddressbilling(elionsManager.selectAddressBilling(spaj));
				viewPolis.setInsured(elionsManager.selectInsuredNumber(spaj));
				//map.put("productInsured", elionsManager.selectViewerInsured(spaj, new Integer(1)));
				HashMap map= Common.serializableMap(uwManager.selectInfoAgen2(spaj));
				viewPolis.setAgen(map);
				HashMap map1=Common.serializableMap(uwManager.selectReferalInput(spaj));
				viewPolis.setReferal(map1);
				viewPolis.setBlangko((String)map.get("MSPO_NO_BLANKO"));
				viewPolis.setTempBlangko((String)map.get("MSPO_NO_BLANKO"));
				viewPolis.setTglSpaj(defaultDateFormat.format((Date)map.get("MSPO_SPAJ_DATE")));
				viewPolis.setTempTglSpaj(defaultDateFormat.format((Date)map.get("MSPO_SPAJ_DATE")));
				viewPolis.setBilling(Common.serializableList(elionsManager.selectViewerBilling(spaj)));
				viewPolis.setDocPos(docPos);
				viewPolis.setPpremi(p_premi);
				if(p_premi !=null){
				viewPolis.getPpremi().setDaftarKyc(Common.serializableList(bacManager.selectDaftar_kyc_pprm(spaj)));}
				viewPolis.getDataUsulan().setDaftaExtra(Common.serializableList(bacManager.selectDaftar_extra_premi(spaj)));
				//Anta (6/3/2013) - Untuk produk Dana Talangan Haji
				Integer flagdth = uwManager.selectFlagDTH(spaj);
				if(flagdth == 1){
					viewPolis.setDetailDTH(Common.serializableList(uwManager.selectDetailDTH(spaj)));
				}
				
				int lsbs = Integer.valueOf(businessID);
				int lsdbs = dataUsulan.getLsdbs_number();
				
				//Yusuf (1/5/08) untuk produk stable link
				if(products.stableLink(String.valueOf(lsbs))){
					//Yusuf (5 Apr 2010) - query terbaru
					viewPolis.setStableLink(Common.serializableList(uwManager.selectInfoStableLinkAllNew(spaj)));
//					viewPolis.setStableLink(uwManager.selectInfoStableLinkAll(spaj));
//					List<Map> liststable =  viewPolis.getStableLink();
//					Map row1 = liststable.get(0);
//					Integer flag_bulanan = ((BigDecimal) row1.get("FLAG_BULANAN")).intValue();
//					dataUsulan.setFlag_bulanan(flag_bulanan);
				}else if(products.stableSave(lsbs, lsdbs)){
					request.setAttribute("stableSave", (Common.serializableList(uwManager.selectInfoStableSave(spaj))));				
				}else if(products.unitLink(businessID)) {
					viewPolis.setUnitLink(Common.serializableList(uwManager.selectUnitLink(spaj, 1)));
					viewPolis.setDetailUnitLink(Common.serializableList(uwManager.selectDetailUnitLink(spaj, 1)));
					viewPolis.setBiayaUnitLink(Common.serializableList(uwManager.selectBiayaUnitLink(spaj, 1)));
					viewPolis.setDaftarPremi(Common.serializableList(uwManager.selectDaftarPremiPertama(spaj)));
				}else if(products.powerSave(businessID)){
					viewPolis.setPowerSave(elionsManager.select_powersaver(spaj));
					if(Integer.parseInt(businessID)==188){
						viewPolis.setPowerSave(uwManager.select_powersaver_baru(spaj));
					}
					Integer flag_bulanan = viewPolis.getPowerSave().getFlag_bulanan();
					dataUsulan.setFlag_bulanan(flag_bulanan);
				}else {
					viewPolis.setBukanUnitLink("true");
				}
				//Jika yang buka orang underwriting, ada fitur EDIT 
				//if(ServletRequestUtils.getStringParameter(request, "p","").equals("v")) {
					String dept = user.getLde_id();
					Integer idx=user.getLus_id().indexOf(props.getProperty("access.viewer.edit"));
					if(("11".indexOf(dept.trim()) > -1) || idx>=0)  {
						request.setAttribute("boleh", true);
						if(viewPolis.getTertanggung().getLspd_id()<=6 || viewPolis.getTertanggung().getLspd_id()==9 || viewPolis.getTertanggung().getLspd_id()==27){ 
							viewPolis.setEditFlag(1);
							viewPolis.setFlagEditAll(1);
						}else
							viewPolis.setFlagEditAll(2);
						
					}				
				//}
				
				//Pengecekan tambahan, khusus untuk posisi LSPD_ID=2 (Underwriting)
				int posisi = ((BigDecimal) docPos.get("LSPD_ID")).intValue();
				if(posisi==2) {
					String msteInsured, proses="(Ada kesalahan dalam sistem. Harap hubungi EDP)";
					Integer liBackup,liReas,liAksep,liActive;
					HashMap mStatus=Common.serializableMap(elionsManager.selectWfGetStatus(spaj,new Integer(1)));
					String mspoPolicyHolder=pemegang.getMspo_policy_holder();
					
					msteInsured=tertanggung.getMcl_id();
					liReas=(Integer)mStatus.get("MSTE_REAS");
					liAksep=(Integer)mStatus.get(("LSSA_ID"));
					liActive=(Integer)mStatus.get("MSTE_ACTIVE");
					Date mste_flag_aksep=(Date)mStatus.get("MSTE_FLAG_AKSEP");
					liBackup=elionsManager.selectMstInsuredMsteBackup(spaj,new Integer(1));
					int j=elionsManager.selectCountMstTransUlink(spaj,null).intValue();
					Insured ins = bacManager.selectMstInsuredAll(spaj);
					Integer flag_nb = 0;
					Integer flag_coll=0;
					if(ins!=null)
					{	
						if(ins.getFlag_approve_collection()!=null)flag_coll=ins.getFlag_approve_collection();
						if(ins.getFlag_approve_uw()!=null)flag_nb=ins.getFlag_approve_uw();
					}

					//List list=elionsManager.selectMReasTemp(spaj);
					if(liAksep==null)
						liAksep=new Integer(0);
					if(liBackup==null)
						liBackup=new Integer(100);
					if(liReas==null)
						liReas=new Integer(100);
					if(msteInsured.substring(0,2).equals("XX") || msteInsured.substring(0,2).equals("WW") ||
					   mspoPolicyHolder.substring(0,2).equals("XX") || mspoPolicyHolder.substring(0,2).equals("WW")){
						proses=" Simultan";
					}else if(tertanggung.getMste_kyc_date()==null){
						proses=" KYC";
					}else if(liBackup.intValue()==100 || liReas.intValue()==100 || liBackup.intValue()==0 ){
						proses=" Reas";
					}else if(liAksep.intValue()!=5 && liAksep.intValue()!=10){
						proses=" Akseptasi";
					}else if((liAksep.intValue()==10 || liAksep.intValue()==5 || (liAksep.intValue()==8 && j>0 && mste_flag_aksep!=null)) &&  pemegang.getMspo_input_date().before(jan2016) ){ 
						proses=" Transfer";
					}else if(pemegang.getMspo_input_date().after(jan2016) ){
							proses=" Approve";
					}

					viewPolis.setProses(proses);
				}
				
				//warning2 untuk di print polis
				if(posisi == 6) {
					ArrayList<String> daftarPesan = new ArrayList<String>();
					
					//1. Bila ada email, harus email softcopy
					HashMap m = Common.serializableMap(uwManager.selectInformasiEmailSoftcopy(spaj));
					String email = (String) m.get("MSPE_EMAIL");
					if(email != null) {
						if(!email.equals("")) {
							daftarPesan.add("Polis ini mempunyai alamat email " + email + ". Harap kirim softcopy polis");
						}
					}
					
					//2. Bila breakable, harus cetak surat konfirmasi breakable
					if(uwManager.selectIsBreakable(spaj) > 0) {
						daftarPesan.add("Polis ini BREAKABLE. Harap cetak surat konfirmasi BREAKABLE");
					}
					
					//3. Beri Warning apakah polis ini seharusnya dicetak di cabang / pusat
					if(user.getJn_bank().intValue() != 2 && user.getJn_bank().intValue() != 3){
						int flag_print = elionsManager.selectPrintCabangAtauPusat(spaj);
						if(flag_print == 1) {
							daftarPesan.add("Polis ini Harus Dicetak di Cabang!");
						}else {
							daftarPesan.add("Polis ini Harus Dicetak di Kantor Pusat!");
						}
					}
					
					//4. Beri Warning apakah polis ini sudah proses nab atau belum
					if(products.unitLink(businessID) || (businessID.equals("164") && lsdbs==11)){						
						ArrayList<Date> asdf = Common.serializableList(uwManager.selectSudahProsesNab(spaj));
						boolean oke = true;
						for(Date d : asdf){
							if(d == null) {
								oke = false;
								break;
							}
						}
						if(asdf.size()==0){
							oke = false;
						}
						if(!oke) {
							daftarPesan.add("NAB Belum Diproses");
						}else{
							daftarPesan.add("NAB Sudah Diproses");
						}
					}
					
					if(uwManager.selectPunyaEndors(spaj)>0){
						daftarPesan.add("Polis ini mempunyai Endorsemen, silakan dicetak dengan memilih Endorsemen");
					}
					
					request.setAttribute("daftarPesan", daftarPesan);					
							
				}
				if(posisi == 6 || posisi == 4 || posisi == 7) {
					
					//Proses Snows akan imaging ketika posisi sudah ada  diprint polis
					
					ArrayList<String> transferInbox = new ArrayList<String>();
					List <Map> inbox = uwManager.selectMstInbox(spaj, null);	
					HashMap mapInbox = null;
					BigDecimal lspd_id_inbox = null, lspd_id_inbox_from = null;
					String mi_id = null;
					List <Map> mapInbox_Uw = uwManager.selectMstInbox(spaj, "1");	
					List <Map> mapInbox_New_Product = uwManager.selectMstInbox(spaj, "3");	
					if(!inbox.isEmpty()){
											
						if(mapInbox_Uw != null || mapInbox_New_Product != null){
							if(!mapInbox_Uw.isEmpty()){
								mapInbox =(HashMap) mapInbox_Uw.get(0);
								lspd_id_inbox = (BigDecimal) mapInbox.get("LSPD_ID");
								lspd_id_inbox_from = (BigDecimal) mapInbox.get("LSPD_ID_FROM");
								mi_id = (String) mapInbox.get("MI_ID");
							}
							if(!mapInbox_New_Product.isEmpty()){
								mapInbox =(HashMap) mapInbox_New_Product.get(0);
								lspd_id_inbox = (BigDecimal) mapInbox.get("LSPD_ID");
								lspd_id_inbox_from = (BigDecimal) mapInbox.get("LSPD_ID_FROM");
								mi_id = (String) mapInbox.get("MI_ID");
							}	
							
							Integer inbox_flag_transfer_document = (Integer) request.getSession().getAttribute("inbox_flag_transfer_document");
							if(inbox_flag_transfer_document==null){
								inbox_flag_transfer_document=0;
							}
							
							if(inbox_flag_transfer_document==0){
								request.getSession().setAttribute("inbox_spaj_tampung", spaj);
							}else {
								String inbox_spaj_tampung = (String) request.getSession().getAttribute("inbox_spaj_tampung");
								if(!inbox_spaj_tampung.equals(spaj)){
									request.getSession().setAttribute("inbox_flag_transfer_document", 0);
									inbox_flag_transfer_document=0;
									request.getSession().setAttribute("inbox_spaj_tampung", spaj);
								}
							}
							if(lspd_id_inbox != null && lspd_id_inbox_from != null){
									if(((lspd_id_inbox.intValue()==202 && lspd_id_inbox_from.intValue() ==207) || lspd_id_inbox.intValue() == 211) && inbox_flag_transfer_document==0 && "11".indexOf(user.getLde_id()) >-1){
										transferInbox.add("Apakah anda mau transfer Document ke Imaging/Filling?");
										request.setAttribute("transferInbox", transferInbox);
										request.getSession().setAttribute("inbox_flag_transfer_document", 1);
									}
								}
						}
					}else{
						request.getSession().setAttribute("inbox_spaj_tampung", "");
					}					
				}			
				
				
				//hadiah power save
				if(viewPolis.getDataUsulan().getLsbs_id()==184 && viewPolis.getDataUsulan().getLsdbs_number()==6){
					ArrayList<Hadiah> hd = Common.serializableList(uwManager.selectHadiah(viewPolis.getPemegang().getReg_spaj()));
					//viewPolis.getPemegang().setDaftar_hadiah(hd);
					request.setAttribute("hadiah", hd);
				}
			}
			else if(lstbId == 2) {
				viewPolis.setProses("mri");
			}
		}
		if(viewPolis.getPemegang()!=null){
			HashMap jne_pod = Common.serializableMap(bacManager.selectDetailPOD(viewPolis.getPemegang().getReg_spaj(),viewPolis.getPemegang().getMspo_no_pengiriman()));
			if (!jne_pod.isEmpty()){
				viewPolis.setReceiver_polis((String)jne_pod.get("MSTJ_RECEIVER"));
				viewPolis.setTgl_receiver((String)jne_pod.get("MSTJ_TGL_TERIMA"));
			}else{
				viewPolis.setReceiver_polis("");
				viewPolis.setTgl_receiver("");
			} 
		}
//		logger.info(viewPolis.getCurrentTimeMillis());
//		logger.info(System.currentTimeMillis());
		double selisih =  new Double((System.currentTimeMillis() - viewPolis.getCurrentTimeMillis())) / 1000.;
//		logger.info(selisih);
		request.setAttribute("selisih", selisih);
		
		return viewPolis;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		f_validasi data =new f_validasi();
		ViewPolis viewPolis = (ViewPolis) cmd ;
		String flag=request.getParameter("flag");
		String spaj = request.getParameter("showSPAJ");
		Datausulan dataUsulan=viewPolis.getDataUsulan();
		ArrayList lsBeneficiary=Common.serializableList(dataUsulan.getDaftabenef());
		ArrayList xdaftarPlus = Common.serializableList(dataUsulan.getDaftarplus());
		Integer no=new Integer(1);
		String ket="Pemegang";
		//
		if(flag.equals("1")){//add beneficiary
//			Calendar cal=new GregorianCalendar();
//			cal.set(1900,00,01);
			String relasi[]=new String[lsBeneficiary.size()];
			relasi=request.getParameterValues("relasi");
			for(int i=0;i<lsBeneficiary.size();i++){
				Benefeciary upBene=(Benefeciary)lsBeneficiary.get(i);
				upBene.setLsre_id(Integer.valueOf(relasi[i]));
				lsBeneficiary.set(i,upBene);
			}
			
			PesertaPlus plusPlus=new PesertaPlus();
			if(! xdaftarPlus.isEmpty()){
				no=new Integer(xdaftarPlus.size()+1);
			}
			plusPlus.setNo_urut(no);
			plusPlus.setLsre_id(new Integer(0));
			plusPlus.setReg_spaj(spaj);
			plusPlus.setKelamin(1);
			xdaftarPlus.add(plusPlus);
			
			Benefeciary insBene=new Benefeciary();
			if(! lsBeneficiary.isEmpty()){
				no=new Integer(lsBeneficiary.size()+1);
			}
			insBene.setMsaw_number(no);
			insBene.setLsre_id(new Integer(0));
//			insBene.setMsaw_birth(cal.getTime());
			insBene.setSmsaw_birth("01/01/1900");
			insBene.setMste_insured_no(new Integer(1));
			insBene.setMsaw_persen(new Double(0));
			insBene.setReg_spaj(spaj);
			insBene.setMsaw_sex(1);
			lsBeneficiary.add(insBene);
			err.reject("","Telah Dilakuakan Penambahan Penerima Manfaat");
		}else if(flag.equals("2")){//delete benefeciary
			if(lsBeneficiary.isEmpty())
				err.reject("","Tidak Berhasil di hapus, Data Penerima Manfaat Tidak ada");
			else{
				lsBeneficiary.remove(lsBeneficiary.size()-1);
				err.reject("","Telah Dilakukan Penghapusan Penerima Manfaat");
			}
		}else if(flag.equals("3")){//simpan perubahan
			if(viewPolis.getFlagEditAll()==0){
				if(viewPolis.getBlangko()==null || viewPolis.getBlangko().trim().equals(""))
					err.reject("","Silahkan Masukan Nomor Blangko dengan Benar");
				else if(viewPolis.getTglSpaj().equals("00/00/000")){
					err.reject("","Silahkan masukan tanggal spaj dengan benar");
				}
				//
				String alamatRmh=viewPolis.getPemegang().getAlamat_rumah();
				String kdPosRmh=viewPolis.getPemegang().getKd_pos_rumah();
				boolean cekKdPosRmh=f_validasi.f_validasi_numerik(kdPosRmh);
				String kotaRumah=viewPolis.getPemegang().getKota_rumah();
				String areaTelpRmh1=data.f_validasi_kode_area_rmh(viewPolis.getPemegang().getArea_code_rumah());
				String telpRmh1=data.f_validasi_telp_rmh(viewPolis.getPemegang().getTelpon_rumah());
				String areaTelpRmh2=data.f_validasi_kode_area_rmh(viewPolis.getPemegang().getArea_code_rumah2());
				String telpRmh2=data.f_validasi_telp_rmh(viewPolis.getPemegang().getTelpon_rumah2());
				String noHp1=viewPolis.getPemegang().getNo_hp();
				String noHp2=viewPolis.getPemegang().getNo_hp2();
				String kdPosKantor=viewPolis.getPemegang().getKd_pos_kantor();
				boolean cekNoHp1=f_validasi.f_validasi_numerik(noHp1);
				boolean cekNoHp2=f_validasi.f_validasi_numerik(noHp2);
				boolean cekKdPosKantor=f_validasi.f_validasi_numerik(kdPosKantor);
				String areaTelpKantor1=data.f_validasi_kode_area_ktr(viewPolis.getPemegang().getArea_code_kantor());
				String areaTelpKantor2=data.f_validasi_kode_area_ktr(viewPolis.getPemegang().getArea_code_kantor2());
				String telpKantor1=data.f_validasi_telp_ktr(viewPolis.getPemegang().getTelpon_kantor());
				String telpKantor2=data.f_validasi_telp_ktr(viewPolis.getPemegang().getTelpon_kantor2());
				String areaFax=data.f_validasi_area_fax(viewPolis.getPemegang().getArea_code_fax());
				String fax=data.f_validasi_fax(viewPolis.getPemegang().getNo_fax());
				String email=viewPolis.getPemegang().getEmail();
				
				validasi(alamatRmh,kdPosRmh,cekKdPosRmh,kotaRumah,areaTelpRmh1,telpRmh1,areaTelpRmh2,telpRmh2,noHp1,noHp2,kdPosKantor,cekNoHp1,cekNoHp2,
						cekKdPosKantor,areaTelpKantor1,areaTelpKantor2,telpKantor1,telpKantor2,areaFax,fax,email,data,ket,err,viewPolis);
				//
				if(viewPolis.getPemegang().getLsre_id()!=1){//cek kalo TT bukan diri sendiri
					ket="Tertanggung";
					alamatRmh=viewPolis.getTertanggung().getAlamat_rumah();
					kdPosRmh=viewPolis.getTertanggung().getKd_pos_rumah();
					cekKdPosRmh=f_validasi.f_validasi_numerik(kdPosRmh);
					kotaRumah=viewPolis.getTertanggung().getKota_rumah();
					areaTelpRmh1=data.f_validasi_kode_area_rmh(viewPolis.getTertanggung().getArea_code_rumah());
					telpRmh1=data.f_validasi_telp_rmh(viewPolis.getTertanggung().getTelpon_rumah());
					areaTelpRmh2=data.f_validasi_kode_area_rmh(viewPolis.getTertanggung().getArea_code_rumah2());
					telpRmh2=data.f_validasi_telp_rmh(viewPolis.getTertanggung().getTelpon_rumah2());
					noHp1=viewPolis.getTertanggung().getNo_hp();
					noHp2=viewPolis.getTertanggung().getNo_hp2();
					kdPosKantor=viewPolis.getTertanggung().getKd_pos_kantor();
					cekNoHp1=f_validasi.f_validasi_numerik(noHp1);
					cekNoHp2=f_validasi.f_validasi_numerik(noHp2);
					cekKdPosKantor=f_validasi.f_validasi_numerik(kdPosKantor);
					areaTelpKantor1=data.f_validasi_kode_area_ktr(viewPolis.getTertanggung().getArea_code_kantor());
					areaTelpKantor2=data.f_validasi_kode_area_ktr(viewPolis.getTertanggung().getArea_code_kantor2());
					telpKantor1=data.f_validasi_telp_ktr(viewPolis.getTertanggung().getTelpon_kantor());
					telpKantor2=data.f_validasi_telp_ktr(viewPolis.getTertanggung().getTelpon_kantor2());
					areaFax=data.f_validasi_area_fax(viewPolis.getTertanggung().getArea_code_fax());
					fax=data.f_validasi_fax(viewPolis.getTertanggung().getNo_fax());
					email=viewPolis.getTertanggung().getEmail();
	
					validasi(alamatRmh,kdPosRmh,cekKdPosRmh,kotaRumah,areaTelpRmh1,telpRmh1,areaTelpRmh2,telpRmh2,noHp1,noHp2,kdPosKantor,cekNoHp1,cekNoHp2,
							cekKdPosKantor,areaTelpKantor1,areaTelpKantor2,telpKantor1,telpKantor2,areaFax,fax,email,data,ket,err,viewPolis);
					
				}
			}
		}
		//
		String info=request.getParameter("info");//jika tombol save pada edit di tekan maka akan langsung ke tab edit
		if(! err.getAllErrors().isEmpty() || info!=null)
			viewPolis.setInfo(new Integer(1));
		
		dataUsulan.setDaftabenef(lsBeneficiary);
		dataUsulan.setDaftarplus(xdaftarPlus);
		viewPolis.setDataUsulan(dataUsulan);
	}
	
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		ViewPolis viewPolis=(ViewPolis)cmd;
		String spaj = request.getParameter("showSPAJ");
		String flag=request.getParameter("flag");
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		
		if(flag.equals("3"))
			elionsManager.prosesEditAlamatViewer(spaj,viewPolis,currentUser,request,err);
		
		//return new ModelAndView("uw/viewer/info", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,cmd,err));
		return new ModelAndView(new RedirectView(request.getContextPath()
				+ "/uw/view.htm?p=v&showSPAJ="+spaj+"&info=1&suc=1"));

	}
	
	private void validasi(String alamatRmh, String kdPosRmh, boolean cekKdPosRmh, String kotaRumah, String areaTelpRmh1, String telpRmh1, String areaTelpRmh2, String telpRmh2, String noHp1, String noHp2, String kdPosKantor, boolean cekNoHp1, boolean cekNoHp2, boolean cekKdPosKantor, String areaTelpKantor1, String areaTelpKantor2, 
			String telpKantor1, String telpKantor2, String areaFax, String fax, String email,f_validasi data ,String ket,BindException err, ViewPolis viewPolis) {
		if(alamatRmh.trim().equals(""))
			err.reject("","Alamat rumah tidak boleh kosong ("+ket+")");
		if(! kdPosRmh.equals("")){
			if(cekKdPosRmh==false)
			err.reject("","Silahkan isi kode pos rumah dengan benar ("+ket+")");
		}	
		if(kotaRumah.trim().equals(""))
			err.reject("","Kota Rumah tidak Boleh Kosong ("+ket+")");
		if(! areaTelpRmh1.equals(""))
			err.reject("","Area Telp Rumah 1 tidak boleh kosong ("+ket+")");
		
		if(!telpRmh1.equals(""))
			err.reject("","Telp Rumah 1 tidak boleh kosong ("+ket+")");
	
		if(!telpRmh2.equals(""))
			err.reject("",telpRmh2);

		if(! noHp1.equals("")){
			if(cekNoHp1==false)
				err.reject("","Silahkan isi no Hp1 dengan benar ("+ket+")");
		}
		if(! noHp2.equals("")){
			if(cekNoHp2==false)
				err.reject("","Silahkan isi no Hp2 dengan benar ("+ket+")");
		}
		//
		if(!kdPosKantor.equals("")){
			if(cekKdPosKantor==false)
				err.reject("","Silahkan isi kd pos kantor dengan benar ("+ket+")");
		}	
		if(!areaTelpKantor1.trim().equals(""))
			err.reject("",areaTelpKantor1+("+ket+"));
		if(!areaTelpKantor2.trim().equals(""))
			err.reject("",areaTelpKantor2+("+ket+"));
		if(!telpKantor1.trim().equals(""))
			err.reject("",telpKantor1+("+ket+"));
		if(!telpKantor2.trim().equals(""))
			err.reject("",telpKantor2+("+ket+"));
		if(!areaFax.trim().equals(""))
			err.reject("",areaFax+("+ket+"));
		if(!fax.trim().equals(""))
			err.reject("",fax+("+ket+"));

		if(!email.trim().equals("")){
			if(data.f_validasi_email(email)==false)
				err.reject("","Silahkan isi email dengan benar eg. example@example.com ("+ket+")");
		}
		
		if(viewPolis.getAddressbilling().getMsap_address().trim().equals(""))err.reject("","Alamat Penagihan tidak boleh kosong ("+ket+")");
		if(!viewPolis.getAddressbilling().getE_mail().trim().equals("")){
			if(data.f_validasi_email(viewPolis.getAddressbilling().getE_mail())==false)
				err.reject("","Silahkan isi email Penagihan dengan benar eg. example@example.com ("+ket+")");
		}
	}

}