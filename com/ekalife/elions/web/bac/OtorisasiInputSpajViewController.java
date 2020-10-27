/**
 * 
 */
package com.ekalife.elions.web.bac;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.KursDanJumlah;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.PemegangAndRekeningInfo;
import com.ekalife.elions.model.ReffBii;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.SumberKyc;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.ViewPolis;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentFormController;

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
public class OtorisasiInputSpajViewController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );

	protected Map referenceData(HttpServletRequest request, Object command, Errors err) throws Exception {
		User user = (User) request.getSession().getAttribute("currentUser");
		ViewPolis viewPolis=(ViewPolis)command;
		Map map = new HashMap();
		String successOrNo = null;
		String spaj = request.getParameter("showSPAJ");
		String reins=request.getParameter("reins");
		map.put("reins", reins);
		//
		Map mapNsb=uwManager.selectLeadNasabahFromSpaj(spaj);
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
	
		StringBuffer tes = new StringBuffer();
		List<Map> cabangKK = new ArrayList<Map>();
		cabangKK = uwManager.selectCabangKK(user.getCab_bank());
		String daftarDeddy="";
		for(int i=0; i<cabangKK.size();i++){
			 Map daftar = cabangKK.get(i);
			 daftarDeddy += daftar.get("LCB_NO");
			 tes.append((String) daftar.get("LCB_NO") + ",");
		}
			tes.append(user.getCab_bank()+",");
			
		if(request.getParameter("showSPAJ")!=null){
		
			//dropdowns
			map.put("select_identitas",DroplistManager.getInstance().get("IDENTITY.xml","ID",request));
			map.put("select_negara",DroplistManager.getInstance().get("WARGANEGARA.xml","ID",request));
			map.put("select_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","",request));
			map.put("select_pekerjaan",DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request));
			map.put("select_medis",DroplistManager.getInstance().get("medis.xml","ID",request));
			map.put("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
			map.put("select_agama",DroplistManager.getInstance().get("AGAMA.xml","ID",request));
			map.put("select_pendidikan",DroplistManager.getInstance().get("PENDIDIKAN.xml","ID",request));
			map.put("select_tujuan",DroplistManager.getInstance().get("TUJUAN_ASR.xml","",request));
			map.put("select_penghasilan",DroplistManager.getInstance().get("PENGHASILAN.xml","",request));
			map.put("select_relasi_premi",DroplistManager.getInstance().get("RELATION_PREMI.xml","ID",request));
			map.put("select_dana",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
			map.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
			map.put("listlsdbs", elionsManager.select_detilprodukutama_viewer(Integer.valueOf(uwManager.selectBusinessId(spaj))));
			map.put("listtipeproduk", elionsManager.select_tipeproduk());
			map.put("select_jns_top_up",DroplistManager.getInstance().get("TOPUP.xml","ID",request));	
			map.put("select_jangka_invest",DroplistManager.getInstance().get("JNSTOPUP.xml","ID",request));
			map.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER.xml","ID",request));
			map.put("select_jenisbunga",DroplistManager.getInstance().get("jenisbunga.xml","",request));
			map.put("select_karyawan",DroplistManager.getInstance().get("karyawan.xml","",request));
			map.put("lsPanes", selectLsPanes(ServletRequestUtils.getStringParameter(request, "c",""), viewPolis, user));
			map.put("select_gelar",DroplistManager.getInstance().get("GELAR.xml","ID",request));
			map.put("select_hasil",DroplistManager.getInstance().get("SUMBER_PENGHASILAN.xml","",request));
			map.put("select_relasi_premi", DroplistManager.getInstance().get("RELATION_PREMI.xml","ID",request));
			
			map.put("pin", elionsManager.selectValidasiPinjaman(spaj));
			String alert = null;
			if(request.getParameter("submitSPAJ") != null) {
				String checkSpajAlreadyOtorizeOrNot = uwManager.selectMspsDescBasedSpajAndOtorisasi(spaj);
				if( checkSpajAlreadyOtorizeOrNot != null && !"".equals( checkSpajAlreadyOtorizeOrNot ) )	
				{
					alert = "spaj sudah pernah diotorisasi";
				}
				else
				{ 	
					List spajNotOtorisasiInfo = elionsManager.selectDaftarSpajOtorisasi( tes.toString().split(","), spaj, null, "EQSPAJ" ); //error disini, ada passing pake array
					StringBuffer tes2 = new StringBuffer();
					List<Map> cabang = new ArrayList<Map>();
					Map abcd = new HashMap();
					abcd = uwManager.selectValidBank(spaj);
					String daftar="";
					cabang = spajNotOtorisasiInfo;
					
					for(int i=0; i<spajNotOtorisasiInfo.size();i++){
						Map tempSpajNotOtorisasiInfo = (HashMap)spajNotOtorisasiInfo.get(i);
						daftar +=tempSpajNotOtorisasiInfo;
					}
					
					Object valid_bank_1 = (String)(abcd.get("VALID_BANK_1")==null ? null : abcd.get("VALID_BANK_1").toString());
					//Object valid_bank_2 = (String) abcd.get("VALID_BANK_2").toString();
					Object valid_bank_2 = (String)(abcd.get("VALID_BANK_2")==null ? null : abcd.get("VALID_BANK_2").toString());
					Integer flagHakOtorisasi = 1;
					String differ = "";
					// valid_bank_1 -> spv ( tdk mempunyai hak utk otorisasi di atas 2 m )
					// valid_bank_2 -> pincab ( mempunyai hak akses otorisasi full )
					if( valid_bank_1 != null && !"".equals( valid_bank_1 ) )
					{
						if(user.getLus_id().equals(valid_bank_1.toString()))
						{
							KursDanJumlah kursAndNominal = elionsManager.selectKursAndNominal(spaj);
							if( kursAndNominal != null && "01".equals(kursAndNominal.getLku_id()) )
							{
								if(kursAndNominal.getNominal() != null && kursAndNominal.getNominal()>2000000000.0)
								{
									flagHakOtorisasi = 0;
									differ = "rupiah";
								}
							}
							else if( kursAndNominal != null && "02".equals(kursAndNominal.getLku_id()) )
							{
								if(kursAndNominal.getNominal() != null && kursAndNominal.getNominal()>200000.0)
								{
									flagHakOtorisasi = 0;
									differ = "dollar";
								}
							}
						}
					}
					
					if( valid_bank_2 != null && !"".equals( valid_bank_2 ) )
					{
						if(user.getLus_id().equals(valid_bank_2.toString()))
						{
							flagHakOtorisasi = 1;
						}
					}
					
					if( flagHakOtorisasi == 0 )
					{
						if("rupiah".equals(differ))
						{
							alert = "Maaf, Anda tidak mempunyai hak Otorisasi untuk nominal di atas Rp.2000.000.000,00";
						}
						else if("dollar".equals(differ))
						{
							alert = "Maaf, Anda tidak mempunyai hak Otorisasi untuk nominal di atas $200.000";
						}
					}
					else
					{
						try{
							elionsManager.insertMstPositionSpaj(user.getLus_id(), "OTORISASI INPUT SPAJ", spaj, 0);
							successOrNo = "yes";
							alert = "sukses";
						}
						catch( Exception e )
						{
							successOrNo = "no";
							err.reject("","SPAJ Gagal diotorisasi");
							logger.error("ERROR :", e);
							alert = "gagal";
						}
					}
				}
			}
			List checkSpajNotOtorisasi = elionsManager.selectDaftarSpajOtorisasi( tes.toString().split(","), null, null, "=" );
			if( checkSpajNotOtorisasi == null || checkSpajNotOtorisasi != null && checkSpajNotOtorisasi.size() == 0 )
			{
				successOrNo = "yes";
			}
			else{
				successOrNo = "no";
			}
			map.put("successOrNo", successOrNo);
			map.put("alert", alert);
		}
		
		//Yusuf 7 okt 09 - polis lama hasil surrender endorsement
		map.put("polisLama", uwManager.selectPolisLamaSurrenderEndorsement(spaj));
		
		return map;
	}
	
	private List selectLsPanes(String c, ViewPolis viewPolis, User currentUser) {
		List lsPanes=new ArrayList();
		Map map;
		String[] names;
		String[] pages;

		//view dobel blanko
		names=new String[]{"Pemegang Polis","Tertanggung","Agen","Data Usulan","Referral"};
		pages=new String[]{"info_pemegang.jsp","info_tertanggung.jsp","info_agen.jsp","info_datausulan.jsp","info_referral.jsp"};
		
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
		
		if(info!=null)
			viewPolis.setInfo(new Integer(1));
		viewPolis.setSuc(request.getParameter("suc"));
		//
		viewPolis.setFlagEditAll(0);
		if(request.getParameter("showSPAJ")!=null){
			String spaj = request.getParameter("showSPAJ");
			String businessID = uwManager.selectBusinessId(spaj);
			Integer kode_flag = new Integer(2);
			String msgInfo = null;
			List<PemegangAndRekeningInfo> pemegangAndRekeningInfo = uwManager.selectPemegangAndRekeningInfo(spaj);
			if( pemegangAndRekeningInfo != null && pemegangAndRekeningInfo.size()>0 )
			{
				if( pemegangAndRekeningInfo.get(0).getMrc_kuasa() != null && pemegangAndRekeningInfo.get(0).getMrc_kuasa() == 1 )
				{
					msgInfo = "Nama Pemegang Polis : "+pemegangAndRekeningInfo.get(0).getMcl_first()+"\n"+
							"Nama Pemegang Rek : "+pemegangAndRekeningInfo.get(0).getMrc_nama()+"\n"+
							"Alasan : "+pemegangAndRekeningInfo.get(0).getNotes();
				}
				else
				{
					msgInfo = null;
				}
			}
			else 
			{
				msgInfo = null;
			}
			//Data utama yang ditampilkan
			Pemegang pemegang = elionsManager.selectpp(spaj);
			Tertanggung tertanggung = elionsManager.selectttg(spaj);
			Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
			ReffBii referral = elionsManager.selectmst_reff_bii(spaj);
			dataUsulan.setWorksite(elionsManager.selectPerusahaanWorksite(spaj));
			Map docPos = elionsManager.selectViewerDocPosition(spaj);
			
			// info rekening
			Rekening_client rekening = this.elionsManager.selectRekeningNasabah(spaj);
			if(rekening!=null){
				rekening.setMrc_no_ac_split(FormatString.splitWordToCharacter(rekening.getMrc_no_ac(),21));
			}
			
			// agen penutup
			if (referral.getLrb_id() != null) {
				Map data = (HashMap) this.elionsManager.select_referrer_shinta(
						referral.getLrb_id(), kode_flag);
				if (data != null) {
					referral.setNama_reff((String) data.get("NAMA_REFF"));
					referral.setNo_rek((String) data.get("NO_REK"));
					referral.setCab_rek((String) data.get("CAB_REK"));
					referral.setAtas_nama((String) data.get("ATAS_NAMA"));
					referral.setFlag_aktif((String) data.get("FLAG_AKTIF"));
					referral.setNpk((String) data.get("NPK"));
					referral.setLcb_no((String) data.get("LCB_NO"));
					if (((String) data.get("FLAG_AKTIF")).equals("1")) {
						referral.setAktif("AKTIF");
					} else {
						referral.setAktif("TIDAK AKTIF");
					}
	
					// referral diset sama dengan agen penutup, bila dikosongin
					if (referral.getReff_id() == null
							|| "".equals(referral.getReff_id())) {
						referral.setReff_id(referral.getLrb_id());
						referral.setNama_reff2((String) data.get("NAMA_REFF"));
						referral.setNo_rek2((String) data.get("NO_REK"));
						referral.setCab_rek2((String) data.get("CAB_REK"));
						referral.setAtas_nama2((String) data.get("ATAS_NAMA"));
						referral.setFlag_aktif2((String) data.get("FLAG_AKTIF"));
						referral.setNpk2((String) data.get("NPK"));
						referral.setLcb_no2((String) data.get("LCB_NO"));
						if (((String) data.get("FLAG_AKTIF")).equals("1")) {
							referral.setAktif2("AKTIF");
						} else {
							referral.setAktif2("TIDAK AKTIF");
						}
					}
				}
			}
			
			// referral
			if (referral.getReff_id() != null) {
				Map data = (HashMap) this.elionsManager.select_referrer_shinta(
						referral.getReff_id(), kode_flag);
				if (data != null) {
					referral.setNama_reff2((String) data.get("NAMA_REFF"));
					referral.setNo_rek2((String) data.get("NO_REK"));
					referral.setCab_rek2((String) data.get("CAB_REK"));
					referral.setAtas_nama2((String) data.get("ATAS_NAMA"));
					referral.setFlag_aktif2((String) data.get("FLAG_AKTIF"));
					referral.setNpk2((String) data.get("NPK"));
					referral.setLcb_no2((String) data.get("LCB_NO"));
					if (((String) data.get("FLAG_AKTIF")).equals("1")) {
						referral.setAktif2("AKTIF");
					} else {
						referral.setAktif2("TIDAK AKTIF");
					}
				}
			}
			viewPolis.setMsgInfo(msgInfo);
			viewPolis.setSelect_kurs(DroplistManager.getInstance().get("KURS.xml","ID",request));
			viewPolis.setBang(this.uwManager.selectBankPusat());
			viewPolis.setPolicyNo(elionsManager.selectPolicyNumberFromSpaj(spaj));
			viewPolis.setPemegang(pemegang);
			viewPolis.setTertanggung(tertanggung);
			viewPolis.setDataUsulan(dataUsulan);
			viewPolis.setAddressbilling(elionsManager.selectAddressBilling(spaj));
			viewPolis.setInsured(elionsManager.selectInsuredNumber(spaj));
			viewPolis.setReferral(referral);
			viewPolis.setRekening(rekening);
			viewPolis.setVirtual_account(uwManager.selectVirtualAccountSpaj(spaj));
			//map.put("productInsured", elionsManager.selectViewerInsured(spaj, new Integer(1)));
			Map map=uwManager.selectInfoAgen2(spaj);
			viewPolis.setAgen(map);
			viewPolis.setBlangko((String)map.get("MSPO_NO_BLANKO"));
			viewPolis.setTempBlangko((String)map.get("MSPO_NO_BLANKO"));
			viewPolis.setTglSpaj(defaultDateFormat.format((Date)map.get("MSPO_SPAJ_DATE")));
			viewPolis.setTempTglSpaj(defaultDateFormat.format((Date)map.get("MSPO_SPAJ_DATE")));
			viewPolis.setBilling(elionsManager.selectViewerBilling(spaj));
			viewPolis.setDocPos(docPos);
			
			int lsbs = Integer.valueOf(businessID);
			int lsdbs = dataUsulan.getLsdbs_number();
			
			//Yusuf (1/5/08) untuk produk stable link
			if(products.stableLink(String.valueOf(lsbs))){
				viewPolis.setStableLink(elionsManager.selectInfoStableLink(spaj));
				List<Map> liststableLink =  viewPolis.getStableLink();
				Map rowStableLink = liststableLink.get(0);
				Integer flag_bulanan_stable = ((BigDecimal) rowStableLink.get("FLAG_BULANAN")).intValue();
				dataUsulan.setFlag_bulanan(flag_bulanan_stable);
			}else if(products.stableSave(lsbs, lsdbs)){
				request.setAttribute("stableSave", (uwManager.selectInfoStableSave(spaj)));
				
			}else if(products.unitLink(businessID)) {
				viewPolis.setUnitLink(uwManager.selectUnitLink(spaj, 1));
				viewPolis.setDetailUnitLink(uwManager.selectDetailUnitLink(spaj, 1));
				viewPolis.setBiayaUnitLink(uwManager.selectBiayaUnitLink(spaj, 1));
				viewPolis.setDaftarPremi(uwManager.selectDaftarPremiPertama(spaj));
			}else if(products.powerSave(businessID)){
				viewPolis.setPowerSave(elionsManager.select_powersaver(spaj));
				Integer flag_bulanan = viewPolis.getPowerSave().getFlag_bulanan();
				dataUsulan.setFlag_bulanan(flag_bulanan);
			}else {
				viewPolis.setBukanUnitLink("true");
			}
			//Jika yang buka orang underwriting, ada fitur EDIT 
			//if(ServletRequestUtils.getStringParameter(request, "p","").equals("v")) {
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				String dept = currentUser.getLde_id();
				Integer idx=currentUser.getLus_id().indexOf(props.getProperty("access.viewer.edit"));
				if(("11".indexOf(dept.trim()) > -1) || idx>=0)  {
					request.setAttribute("boleh", true);
					if(viewPolis.getTertanggung().getLspd_id()<=6 || viewPolis.getTertanggung().getLspd_id()==9){ 
						viewPolis.setEditFlag(1);
						viewPolis.setFlagEditAll(1);
					}else
						viewPolis.setFlagEditAll(2);
					
				}				
			//}
			
			//Pengecekan tambahan, khusus untuk posisi LSPD_ID=2 (Underwriting)
			int posisi = ((BigDecimal) docPos.get("LSPD_ID")).intValue();
			if(posisi==2) {
				String msteInsured, proses="(Ada kesalahan dalam sistem. Harap hubungi ITwebandmobile@sinarmasmsiglife.co.id)";
				Integer liBackup,liReas,liAksep,liActive;
				Map mStatus=elionsManager.selectWfGetStatus(spaj,new Integer(1));
				String mspoPolicyHolder=pemegang.getMspo_policy_holder();
				
				msteInsured=tertanggung.getMcl_id();
				liReas=(Integer)mStatus.get("MSTE_REAS");
				liAksep=(Integer)mStatus.get(("LSSA_ID"));
				liActive=(Integer)mStatus.get("MSTE_ACTIVE");
				Date mste_flag_aksep=(Date)mStatus.get("MSTE_FLAG_AKSEP");
				liBackup=elionsManager.selectMstInsuredMsteBackup(spaj,new Integer(1));
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
				}else if(liBackup==100 || liReas==100 || liBackup==0 ){
					proses=" Reas";
				}else if(liAksep!=5 || liAksep!=10){
					proses=" Akseptasi";
				}
				int j=elionsManager.selectCountMstTransUlink(spaj,null).intValue();
				if(liAksep==10 || liAksep==5 || (liAksep==8 && j>0 && mste_flag_aksep!=null) ) 
					proses=" Transfer";

				viewPolis.setProses(proses);
			}
			
			//warning2 untuk di print polis
			if(posisi == 6) {
				List<String> daftarPesan = new ArrayList<String>();
				
				//1. Bila ada email, harus email softcopy
				Map m = uwManager.selectInformasiEmailSoftcopy(spaj);
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
				if(currentUser.getJn_bank().intValue() != 2 && currentUser.getJn_bank().intValue() != 3 && currentUser.getJn_bank().intValue() != 16){
					int flag_print = elionsManager.selectPrintCabangAtauPusat(spaj);
					if(flag_print == 1) {
						daftarPesan.add("Polis ini Harus Dicetak di Cabang!");
					}else {
						daftarPesan.add("Polis ini Harus Dicetak di Kantor Pusat!");
					}
				}
				
				request.setAttribute("daftarPesan", daftarPesan);
			}
			
		}

		logger.info(viewPolis.getCurrentTimeMillis());
		logger.info(System.currentTimeMillis());
		double selisih =  new Double((System.currentTimeMillis() - viewPolis.getCurrentTimeMillis())) / 1000.;
		logger.info(selisih);
		request.setAttribute("selisih", selisih);
		
		return viewPolis;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		f_validasi data =new f_validasi();
		ViewPolis viewPolis = (ViewPolis) cmd;
		String flag=request.getParameter("flag");
		String spaj = request.getParameter("showSPAJ");
		Datausulan dataUsulan=viewPolis.getDataUsulan();
		Pemegang pemegang=viewPolis.getPemegang();
		List lsBeneficiary=dataUsulan.getDaftabenef();
		List lsSumberKyc=pemegang.getDaftarKyc();
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
			lsBeneficiary.add(insBene);
			err.reject("","Telah Dilakuakan Penambahan Penerima Manfaat");
			
			SumberKyc insKyc=new SumberKyc();	
			
			insKyc.setReg_spaj(spaj);

			lsSumberKyc.add(insKyc);
			err.reject("","Telah Dilakuakan Penambahan Sumber Pendanaan");
			
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
				List daftarKyc=viewPolis.getPemegang().getDaftarKyc();
				
				validasi(alamatRmh,kdPosRmh,cekKdPosRmh,kotaRumah,areaTelpRmh1,telpRmh1,areaTelpRmh2,telpRmh2,noHp1,noHp2,kdPosKantor,cekNoHp1,cekNoHp2,
						cekKdPosKantor,areaTelpKantor1,areaTelpKantor2,telpKantor1,telpKantor2,areaFax,fax,email,data,ket,err);
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
							cekKdPosKantor,areaTelpKantor1,areaTelpKantor2,telpKantor1,telpKantor2,areaFax,fax,email,data,ket,err);
					
				}
			}
		}
		//
		String info=request.getParameter("info");//jika tombol save pada edit di tekan maka akan langsung ke tab edit
		if(! err.getAllErrors().isEmpty() || info!=null)
			viewPolis.setInfo(new Integer(1));
		
		dataUsulan.setDaftabenef(lsBeneficiary);
		viewPolis.setDataUsulan(dataUsulan);
		
		pemegang.setDaftarKyc(lsSumberKyc);
		viewPolis.setPemegang(pemegang);
	}
	
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		ViewPolis viewPolis=(ViewPolis)cmd;
		String spaj = request.getParameter("showSPAJ");
		String flag=request.getParameter("flag");
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		
//		if(flag.equals("3"))
//			elionsManager.prosesEditAlamatViewer(spaj,viewPolis,currentUser,request,err);
		
		//return new ModelAndView("uw/viewer/info", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,cmd,err));
		return new ModelAndView(new RedirectView(request.getContextPath()
				+ "/bac/otorisasiInputSpajView.htm?p=v&showSPAJ="+spaj+"&info=1&suc=1"));

	}
	
	private void validasi(String alamatRmh, String kdPosRmh, boolean cekKdPosRmh, String kotaRumah, String areaTelpRmh1, String telpRmh1, String areaTelpRmh2, String telpRmh2, String noHp1, String noHp2, String kdPosKantor, boolean cekNoHp1, boolean cekNoHp2, boolean cekKdPosKantor, String areaTelpKantor1, String areaTelpKantor2, 
			String telpKantor1, String telpKantor2, String areaFax, String fax, String email,f_validasi data ,String ket,BindException err) {
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
		
	}

}