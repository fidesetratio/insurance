package com.ekalife.elions.web.bac;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 *	Class Ini Berfungsi Sebagai Controller untuk Proses 
 *	Transfer dari BAC to UW, dimana Proses Yang akan dijalankan 
 *	secara Umum adalah Untuk mentransfer Posisi SPAJ sari BAC ke UW
 *	dan akan dibuat jurnal jika ada pembayaran premi, dimana akan mengeluarkan
 *	no Pre dan No voucher dari Titipan Premi Tersebut
 *	
 */
public class TransferBacToUwBankSinarmasFormController extends ParentFormController {
	
	DecimalFormat f3= new DecimalFormat ("000");

	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map tmp = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if(currentUser.getJn_bank().intValue() == 3) {
			tmp.put("daftarOtor", elionsManager.selectDropDown(
					"EKA.LST_USER", "LUS_ID", "LUS_LOGIN_NAME", "", "LUS_LOGIN_NAME", 
					"cab_bank = '"+currentUser.getCab_bank()+"' AND jn_bank = 3 AND flag_approve = 1 AND lus_id <> " + currentUser.getLus_id()));
		}
		return tmp;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		String spaj=request.getParameter("spaj");
		command.setSpaj(spaj);
		
		Map temp = elionsManager.selectKonfirmasiTransferBac(spaj);
		if(temp != null) {
			StringBuffer konfirmasi = new StringBuffer();
			
			NumberFormat nf = NumberFormat.getNumberInstance();
			
			konfirmasi.append("Harap pastikan kebenaran data dibawah sebelum melakukan proses transfer:\\n");
			konfirmasi.append("================================================\\n\\n");
			konfirmasi.append("Produk: [" + temp.get("LSBS_ID") + "-" + temp.get("LSDBS_NUMBER") + "] " + temp.get("LSDBS_NAME") + "\\n\\n");
			konfirmasi.append("No Register SPAJ (No Blanko): " + temp.get("REG_SPAJ") + " (" + temp.get("MSPO_NO_BLANKO") + ")\\n");
			konfirmasi.append("Tanggal Pertanggungan: " + defaultDateFormat.format((Date) temp.get("MSTE_BEG_DATE")) + " s/d " + defaultDateFormat.format((Date) temp.get("MSTE_END_DATE")) + "\\n");
			konfirmasi.append("UP / Premi: " + temp.get("LKU_SYMBOL") + " " + nf.format((BigDecimal) temp.get("MSPR_TSI")) + " / " + temp.get("LKU_SYMBOL") + " " + nf.format((BigDecimal) temp.get("MSPR_PREMIUM")) + "\\n");
			konfirmasi.append("Pemegang / Tertanggung: " + temp.get("PEMEGANG") + " (" + defaultDateFormat.format((Date) temp.get("PEMEGANG_LAHIR")) + ") / " + temp.get("TERTANGGUNG") + " (" + defaultDateFormat.format((Date) temp.get("TERTANGGUNG_LAHIR")) + ")\\n");

			if(temp.get("MGI") != null) {
				int ro = ((BigDecimal) temp.get("RO")).intValue();
				
				konfirmasi.append("\\n");
				konfirmasi.append("Data khusus produk SAVE / STABLE LINK\\n");
				konfirmasi.append("================================================\\n\\n");
				konfirmasi.append("Jenis Rollover: " + (ro == 1 ? "ALL" : ro == 2 ? "PREMI" : "AUTOBREAK") + "\\n");
				konfirmasi.append("Masa Garansi/Target Investasi: " + temp.get("MGI") + " Bulan\\n");
				konfirmasi.append("Rate: " + temp.get("RATE") + "%\\n");
				konfirmasi.append("Bunga:" + temp.get("LKU_SYMBOL") + " " + nf.format((BigDecimal) temp.get("BUNGA")) + "\\n");

				konfirmasi.append("\\nData yang sudah ditransfer, tidak dapat diedit kembali (untuk Polis Bank Sinarmas / Sinarmas Sekuritas). Lanjutkan?");
			}else {
				konfirmasi.append("\\nLanjutkan?");
			}
			
			command.setKonfirmasi(konfirmasi.toString());
		}
		
		return command;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		String lsAgen = null,lcaId;
		String namaPemegang;
		String mspoPlanProvider;
		String bisnisId = null;
		Integer lsbsId = null,flagCc = null,liPayMode = null, lsdbsNumber = null;
		int liCc=0;
		command.setError(new Integer(1));
		Map a = elionsManager.selectCheckPosisi(command.getSpaj());
		int li_pos=Integer.parseInt(a.get("LSPD_ID").toString());
		String ls_pos=a.get("LSPD_POSITION").toString();
		//validasi Posisi SPAJ
		if(li_pos !=1 ){
			err.reject("","Posisi Polis ini ada di "+ls_pos);
		}
		if(command.getSpaj() == null) {
			err.reject("", "Harap pilih nomor register SPAJ terlebih dahulu di atas. Terima kasih.");
		}
		//Yusuf - 14/12/2007 - Apabila > -1 maka inputan bank -> dipakai untuk disable titipan premi
		int isInputanBank = -1;
		isInputanBank = elionsManager.selectIsInputanBank(command.getSpaj());
		
		//Yusuf - 25-09-08 - harus ada validasi checklist
		//if(isInputanBank < 0) {
		if(!elionsManager.selectValidasiCheckListBySpaj(command.getSpaj()))
			err.reject("", "Harap Input CHECKLIST DOKUMEN POLIS Terlebih Dahulu!");
		//}
		
		// data tertanggung
		Map mTertanggung =elionsManager.selectTertanggung(command.getSpaj());
		lsAgen=(String) mTertanggung.get("MSAG_ID");
		Integer lssaId=(Integer)mTertanggung.get("LSSA_ID");
		command.setGuthrie((Integer)mTertanggung.get("MSTE_FLAG_GUTHRIE"));
		
		//data pemegang
		Map mPemegang =elionsManager.selectPemegang(command.getSpaj());
		lcaId=(String)mPemegang.get("LCA_ID");
		namaPemegang=(String)mPemegang.get("MCL_FIRST");
		mspoPlanProvider=(String)mPemegang.get("MSPO_PLAN_PROVIDER");
		command.setNamaPemegang(namaPemegang);
		//data usulan
		Map mDataUsulan=uwManager.selectDataUsulan(command.getSpaj());
		
		if(mDataUsulan!=null){
			lsbsId=(Integer)mDataUsulan.get("LSBS_ID");
			lsdbsNumber=(Integer)mDataUsulan.get("LSDBS_NUMBER");
			
			command.setLsbsId(lsbsId);
			bisnisId=f3.format(lsbsId);
			if(mDataUsulan.get("MSTE_FLAG_CC")!=null)
				flagCc=(Integer)mDataUsulan.get("MSTE_FLAG_CC");
			liPayMode=(Integer)mDataUsulan.get("LSCB_ID");
		}
		//kolom tgl  terima spaj dan tanggal spaj doc tidak boleh kosong request Yusuf 20/12/2006 (email)
		
		Date tglTerimaSpaj=(Date)mTertanggung.get("MSTE_TGL_TERIMA_SPAJ");
		//
		User user = (User) request.getSession().getAttribute("currentUser");
		
		if(flagCc!=null )
			liCc=flagCc.intValue();
		command.setFlagAdd(liCc);
		//
		if(lsAgen==null || lsAgen.equals("000000")){
			err.reject("","Kode Agen masih Kosong atau Belum di Ganti");
		}
		/*	validasi ref bii :yusuf
			apabila produk : 
			maka jalankan query elions.uw.selectReferrerBII (ada di com/ekalife/elions/uw/dao/sql-map-select.xml)
			apabila tidak ada hasilnya, maka error: "Harap input Referrer BII terlebih dahulu"
		*/
		int pos=0;
		String kode=props.getProperty("product.planRefBii");
		pos=kode.indexOf(bisnisId);
		// untuk lsbs_id=142 dan lsdbsNumber 1 bukan produk bancassurance
		//Yusuf - Polis Inputan Admin Bank tidak divalidasi reff BII nya karena bisa langsung print (14/12/2007) 
		//Map reg = elionsManager.selectRegion(command.getSpaj());
		//String region = (String) reg.get("LSRG_NAMA");
		//Yusuf (3/4/08) else : All Bancassurance, harus ada referral
		if(lcaId.equals("09")){
			Map mapNasabah=uwManager.selectLeadNasabahFromSpaj(command.getSpaj());
			Map mReffBii=elionsManager.selectReferrerBii(command.getSpaj());
			if(mapNasabah.isEmpty() && mReffBii==null) err.reject("","Harap Input Referral Bank Terlebih Dahulu.");
		}

		//validasi untuk produk power save
		if(products.unitLink(bisnisId)){// validasi untuk produk unitlink
			Double ldec_persen, ldec_premi;
			//Yusuf (1/5/08) - stable link
			if(products.stableLink(lsbsId.toString())){
				List<Map> slink = elionsManager.selectValidasiStableLink(command.getSpaj());
				if(lsdbsNumber==11){
					slink = uwManager.selectValidasiNewStableLink(command.getSpaj());
				}
				if(slink.isEmpty()) {
					err.reject("", "Ada kesalahan pada data Stable Link. Harap hubungi IT");
				}else {
					for(Map m : slink) {
						int validasi = ((BigDecimal) m.get("V")).intValue();
						if(validasi == 0) {
							err.reject("", "Ada kesalahan pada data Stable Link. Harap hubungi IT");
							break;
						}
					}
				}
			}
			//
			ldec_persen=new Double(0);
			ldec_persen=elionsManager.selectCountMstRekClient(command.getSpaj());
			if(ldec_persen==null)
				ldec_persen=new Double(0);
			if(ldec_persen.doubleValue()==0 && !command.isRekNas()){
				command.setError(new Integer(100));
				command.setRekNas(true);
				err.reject("","Produk Unit LInk, Rekening Nsabah Belum di iNput. Yakin Transfer");
			}
			//
		}else if(products.powerSave(bisnisId)){//powersave
			//bc 010604
			Integer li_aups = null,li_jmps = null;
			Long ll_pscount,ll_ps_ro_count;
			Double ldec_psrate = null;
			ll_pscount=elionsManager.selectCountMstPowerSave(command.getSpaj());
			//hemilda 21042006 (powesave ro count)
			ll_ps_ro_count=elionsManager.selectCountMstPowerSaveRo(command.getSpaj());
			if(ll_pscount.longValue() == 0 || ll_ps_ro_count.longValue()==0){
				err.reject("","Data Plan Power Save Spaj "+command.getSpaj()+" Belum Lengkap");

			}else{
				List ls_powersave=elionsManager.selectMstPowerSaveProses(command.getSpaj(),new Integer(5));
				for(int i=0;i<ls_powersave.size();i++){
					Map mPower=(HashMap) ls_powersave.get(i);
					li_aups=(Integer)mPower.get("MPS_ROLL_OVER");
					li_jmps=(Integer)mPower.get("MPS_JANGKA_INV");
					ldec_psrate=(Double)mPower.get("MPS_RATE");
				}
				if(li_aups==null)
					li_aups=new Integer(0);
				if(li_jmps==null)
					li_jmps=new Integer(0);
				if(ldec_psrate==null)
					ldec_psrate=new Double(0);
				
				if (ldec_psrate.doubleValue()==0 || li_aups.intValue() == 0 || li_jmps.intValue() == 0){
					err.reject("","Data Plan Power SAVE Spaj "+command.getSpaj()+" Belum Lengkap");
				}
			}
		}
		if(lssaId.intValue() == 2) {
			err.reject("", "SPAJ ini Status Aksepnya : DECLINE. Tidak bisa memproses SPAJ ini lebih lanjut sebelum merubah statusnya.");
		}
	
		//validasi2 password, baru untuk sinarmas sekuritas saja
		if(isInputanBank == 3) {
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String password = ServletRequestUtils.getStringParameter(request, "password", "");
			Map data_valid = (HashMap) this.elionsManager.select_validbank(Integer.parseInt(currentUser.getLus_id()));
			String pass_spv = (String)data_valid.get("PASS1");
			
			if(currentUser.getJn_bank().intValue() == 3) {
				int lus = ServletRequestUtils.getIntParameter(request, "otorotor", -1);
				if(lus == -1) err.reject("", "Harap pilih salah satu user otorisasi terlebih dahulu");
				else pass_spv = uwManager.validationOtorisasiSekuritas(lus);
				if(!password.equalsIgnoreCase(pass_spv)){
					err.reject("", "Password yang anda masukkan salah");
				}
			}
			
		}
		
		//Deddy (27 Dec 2012) - Sesuai Flow yang diberikan Dr.Ingrid Via Email, untuk produk new Simas Stabil Link harus melakukan Upload Scan sebelum transfer.
		if(lsbsId==164 && lsdbsNumber==11){
			if(uwManager.selectMstInbox(command.getSpaj(),null)==null){
				if(isInputanBank ==2) {
					String cabang = elionsManager.selectCabangFromSpaj(command.getSpaj());
					String path = props.getProperty("pdf.dir.export").trim() + "\\" + 
									cabang.trim() + "\\" + 
									command.getSpaj().trim() + "\\" + 
									command.getSpaj().trim() + "SPAJ 001.pdf";
					File file = new File(path);
					if(!file.exists()){
						err.reject("", "Proses Transfer tidak dapat dilakukan sebelum melakukan upload scan(Terutama Upload Scan SPAJ).Silakan upload scan terlebih dahulu.");
					}
					file = null;
				}
			}
		}
		
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		if(command.getError().intValue()==1){//proses transfer ke U/W
			command.setPesan(uwManager.prosesTransferBacToUw(command.getSpaj(),currentUser.getLus_id(),command.getLsbsId(),command.getNamaPemegang(),command.getGuthrie(),err,request));
		}
		
		return new ModelAndView("bac/transferBacToUwBankSinarmas", "cmd", command);
	}

}