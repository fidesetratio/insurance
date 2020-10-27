/**
 * @author  : Ferry Harlim
 * 
 */
package com.ekalife.elions.web.bac;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.FileUtil;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.dao.BacDao;
import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.parent.ParentFormController;

/**
 *	Class Ini Berfungsi Sebagai Controller untuk Proses 
 *	Transfer dari BAC to UW, dimana Proses Yang akan dijalankan 
 *	secara Umum adalah Untuk mentransfer Posisi SPAJ sari BAC ke UW
 *	dan akan dibuat jurnal jika ada pembayaran premi, dimana akan mengeluarkan
 *	no Pre dan No voucher dari Titipan Premi Tersebut
 *	
 */
public class TransferBacToUwFormController extends ParentFormController {
	
	DecimalFormat f3= new DecimalFormat ("000");
	private BacDao bacDao;
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		String spaj=request.getParameter("spaj");
		Map map=uwManager.selectLeadNasabahFromSpaj(spaj);
		Integer jnsNasabah=(Integer)map.get("JN_NASABAH");
		if(jnsNasabah==null)
			jnsNasabah=0;
		if(jnsNasabah.intValue()==1){
			command.setTo("Jenis Nasabah Platinum!");
		}else if(jnsNasabah.intValue()==2){
			command.setTo("Jenis Nasabah Reguler!");
		}else if(jnsNasabah.intValue()==3){
			command.setTo("Jenis Nasabah Gold Link!");
		}else if(jnsNasabah.intValue()==4){
			command.setTo("Jenis Nasabah Prolink!");
		}
		command.setError(new Integer(0));
		command.setSpaj(spaj);
		
		Map temp = elionsManager.selectKonfirmasiTransferBac(spaj);
		StringBuffer konfirmasi = new StringBuffer();
		
		//iga validasi surat pernyataan autosales
				Integer cek = ajaxManager.eSpajGadget(spaj);
				HashMap mapProdExc = uwManager.getUwDao().selectMstConfig(6, "suratAutosales","PROD_EXCLUDE_SP_AUTOSALES");
				String[] AutosalesExc = mapProdExc.get("NAME")!=null?mapProdExc.get("NAME").toString().split(","):null;
				String[] nonaktif = mapProdExc.get("NAME3")!=null?mapProdExc.get("NAME3").toString().split(","):null;
				
				boolean nonaktifValidasi = false;
				if(nonaktif != null){
					for(String s: nonaktif){
						if(s.equals("1"))
							nonaktifValidasi=true;
					}
				}
				
				if (cek>0 && nonaktifValidasi){
					List listSpajTemp = bacManager.selectReferensiTempSpaj(spaj);
					HashMap spajTemp = (HashMap) listSpajTemp.get(0);
					String idgadget = (String) spajTemp.get("NO_TEMP");
					String cabang = elionsManager.selectCabangFromSpaj(spaj);
					String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
							+ cabang + "\\" + spaj;
					File destDir = new File(exportDirectory);
					
					List detBisnis = elionsManager.selectDetailBisnis(spaj);
					String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
					String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
					
					String prod = lsbs+"-"+lsdbs;
					boolean Autosalesexclude = false;
					if(AutosalesExc != null){
						for(String s: AutosalesExc){
							if(s.equals(prod))
								Autosalesexclude=true;
						}
					};
					
					File[] files = destDir.listFiles();
					if(files == null){
						konfirmasi.append("TIDAK ADA DOKUMEN PENDUKUNG, MOHON UNTUK DILENGKAPI\\n");
						request.setAttribute("proses_spaj", "ditolak");
						command.setKonfirmasi(konfirmasi.toString());
					}else{
						 File suratAutosales = null;
						    for (int i = 0; i < files.length; i++) {
						       if ((files[i].toString().toUpperCase().contains("FT_SP_AUTOSALES"))) {
						    	   suratAutosales = files[i];
						       }
						    }
						if(!Autosalesexclude && suratAutosales == null){
							konfirmasi.append("SURAT PERNYATAAN AUTOSALES BELUM ADA, MOHON UNTUK DILENGKAPI\\n");
							request.setAttribute("proses_spaj", "ditolak");
							command.setKonfirmasi(konfirmasi.toString());
						}
					}
				}
		
		else if(temp != null) {
			
			Map blacklistPemegang = uwManager.selectCekBlacklist(temp.get("PEMEGANG_LAHIR").toString(), temp.get("PEMEGANG").toString());
			Map blacklistTertanggung = uwManager.selectCekBlacklist(temp.get("TERTANGGUNG_LAHIR").toString(), temp.get("TERTANGGUNG").toString());
			String blacklistPemegang_flag = "";
			String nb_processPemegang = "";
			String blacklistTertanggung_flag = "";
			String nb_processTertanggung = "";
			if(blacklistPemegang != null){
				blacklistPemegang_flag = blacklistPemegang.get("MCL_BLACKLIST").toString();
				nb_processPemegang = blacklistPemegang.get("LBL_NB_PROCESS").toString();
			}
			if(blacklistTertanggung != null){
				blacklistTertanggung_flag = blacklistTertanggung.get("MCL_BLACKLIST").toString();
				nb_processTertanggung = blacklistTertanggung.get("LBL_NB_PROCESS").toString();
			}
			NumberFormat nf = NumberFormat.getNumberInstance();
//			if(("0".equals(nb_processPemegang) && "1".equals(blacklistPemegang_flag)) || ("0".equals(nb_processTertanggung) && "1".equals(blacklistTertanggung_flag))){
//				//konfirmasi.append("SPAJ belum dapat diproses. Harap menghubungi bag.underwriting\\n");
//				konfirmasi.append("SPAJ TIDAK DAPAT DIPROSES KARENA NASABAH MASUK DALAM DAFTAR ATTENTION LIST\\n");
//				request.setAttribute("proses_spaj", "ditolak");
//			}else{
//				if("1".equals(blacklist_flag)){
//					konfirmasi.append("ALERT! BLACKLIST CUSTOMER : HARAP USER UW AWARE\\n\\n");
//				}
			//ARIN BILANG SEHARUSNYA TIDAK ADA BLOK UNTUK ATTENTIONLIST..CUMA WARNING (07/11/2012)
				if(("0".equals(nb_processPemegang) && "1".equals(blacklistPemegang_flag)) || ("0".equals(nb_processTertanggung) && "1".equals(blacklistTertanggung_flag))){
					//konfirmasi.append("SPAJ belum dapat diproses. Harap menghubungi bag.underwriting\\n");
					konfirmasi.append("NASABAH MASUK DALAM DAFTAR ATTENTION LIST\\n");
					//request.setAttribute("proses_spaj", "ditolak");
				}
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
					
					BigDecimal lsbs_id = (BigDecimal) temp.get("LSBS_ID");
					BigDecimal lsdbs_number = (BigDecimal) temp.get("LSDBS_NUMBER");
					if(products.productMallLikeBSM( lsbs_id.intValue(), lsdbs_number.intValue() )){
						konfirmasi.append("\\nData yang sudah ditransfer, tidak dapat diedit kembali. Lanjutkan?");
					}else{
						konfirmasi.append("\\nData yang sudah ditransfer, tidak dapat diedit kembali (untuk Polis Bank Sinarmas / Sinarmas Sekuritas). Lanjutkan?");
					}
				}else {
					konfirmasi.append("\\nLanjutkan?");
				}
//			}
			
			command.setKonfirmasi(konfirmasi.toString());
			//String lsbs_id = (String) temp.get("LSBS_ID");
			if(uwManager.selectLcaIdMstPolicyBasedSpaj(spaj).equals("58")/* ||Integer.parseInt(lsbs_id)==142 && user.getCab_bank().equals("")*/){
				if(uwManager.selectNonClearCaseAllMedQuest(spaj)>0 ){
					command.setLspd_id(2);
				}else{
					command.setLspd_id(6);
				}
			}
			
			/*if (currentUser.getLus_bas()==2){
				command.setLspd_id(2);
			}*/
		}
		
		
		
		return command;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		String lsAgen = null,lcaId,lwkId,lsrg_id;
		String namaPemegang;
		String mspoPlanProvider;
		String bisnisId = null;
		Integer lsbsId = null,flagCc = null,liPayMode = null, lsdbsNumber = null;
		Integer liCc=0;
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
		Integer isInputanBank = -1;
		isInputanBank = elionsManager.selectIsInputanBank(command.getSpaj());
		
		//Yusuf - 25-09-08 - harus ada validasi checklist
		//if(isInputanBank < 0) {
		if(!elionsManager.selectValidasiCheckListBySpaj(command.getSpaj()))
			err.reject("", "Harap Input CHECKLIST DOKUMEN POLIS Terlebih Dahulu!");
		//}
		
		//Yusuf - Request Yune (Helpdesk #18642) - 18 April 2011 - Tambahan validasi scan dokumen, khusus selain BSM
		//Tambahan Request : hanya berlaku untuk user BAS saja
		User user = (User) request.getSession().getAttribute("currentUser");
		
		//Deddy - Apabila user Admin Mall, flag inputanBank = 4
		if(!Common.isEmpty(user.getJn_bank())){
			if(user.getJn_bank()==4){
				isInputanBank = 4;
			}
		}
		
		//Integer lewat =bacManager.selectCekKonfirmasiSyariah(reg_spaj);
//		if(isInputanBank < 0 && user.getLde_id().equals("29")) {
//			String cabang = elionsManager.selectCabangFromSpaj(command.getSpaj());
//			String path = props.getProperty("pdf.dir.export").trim() + "\\" + 
//							cabang.trim() + "\\" + 
//							command.getSpaj().trim() + "\\" + 
//							command.getSpaj().trim() + "SPAJ 001.pdf";
//			File file = new File(path);
//			if(!file.exists()){
//				err.reject("", "Anda belum melakukan scan dokumen SPAJ! Dokumen tidak dapat ditransfer");	
//			}
//			file = null;
//		}
		
		// data tertanggung
		Map mTertanggung =elionsManager.selectTertanggung(command.getSpaj());
		lsAgen=(String) mTertanggung.get("MSAG_ID");
		Integer lssaId=(Integer)mTertanggung.get("LSSA_ID");
		command.setGuthrie((Integer)mTertanggung.get("MSTE_FLAG_GUTHRIE"));
		
		//data pemegang
		Map mPemegang =elionsManager.selectPemegang(command.getSpaj());
		lcaId=(String)mPemegang.get("LCA_ID");
		lwkId=(String)mPemegang.get("LWK_ID");
		lsrg_id=(String)mPemegang.get("LSRG_ID");
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
		
		//CREATE BY ANDY
		if((lsbsId == 196 && lsdbsNumber == 2) || lsbsId==163) {// TERM INSURANCE (SMS) & DANA SEJAHTERA
			List<MedQuest> questionare = Common.serializableList(uwManager.selectMedQuest(command.getSpaj(), 1));//insured = 1
			if(questionare == null || questionare.size() == 0){
				err.reject("","Questionare masih Kosong. Silakan mengisi questionare terlebih dahulu (Menu Pengisian Kuesionare )");
			}
		}
		
		//RYAN
		if((lsbsId == 142 && lsdbsNumber == 2)||(lsbsId == 142 && lsdbsNumber == 9)||(lsbsId == 158 && lsdbsNumber == 6)
				||(lsbsId == 158 && lsdbsNumber == 14)||(lsbsId == 164 && lsdbsNumber == 2)
				||(lsbsId == 164 && lsdbsNumber == 8)||(lsbsId == 175 && lsdbsNumber == 2)){
			
		}else{
			String flag_spaj = bacManager.selectMspoFLagSpaj(command.getSpaj());
			if ("4".indexOf(flag_spaj)>-1){
				Integer questionareNew = bacManager.selectCountQuestionareNew(command.getSpaj());
				if( questionareNew == 0){
					err.reject("","Questionare masih Kosong. Silakan mengisi questionare terlebih dahulu");
				}
			}else if ("1,2,3".indexOf(flag_spaj)>-1){
				ArrayList data_LQLPP=new ArrayList();
				ArrayList data_LQLQS=new ArrayList();
				ArrayList data_LQLTT=new ArrayList();
				ArrayList data_LQLQS2=new ArrayList();
				data_LQLPP=Common.serializableList(bacManager.selectQuestionareNew(command.getSpaj(), 1,null,null));
				data_LQLTT=Common.serializableList(bacManager.selectQuestionareNew(command.getSpaj(), 2,null,null));
				data_LQLQS =Common.serializableList(bacManager.selectQuestionareNew(command.getSpaj(), 3,105,130));
				data_LQLQS2 =Common.serializableList(bacManager.selectQuestionareNew(command.getSpaj(), 3,131,156));
				//Integer questionareNew = bacManager.selectCountQuestionareNew(command.getSpaj());
				if(data_LQLPP.isEmpty() || data_LQLPP==null){
					err.reject("","Questionare Pemegang Masih Kosong. Silakan mengisi questionare Pemegang terlebih dahulu");
				} 
				if(data_LQLTT.isEmpty() || data_LQLTT==null){
					err.reject("","Questionare Tertanggung Masih Kosong. Silakan mengisi questionare Tertanggung terlebih dahulu");
				} 
				if(data_LQLQS.isEmpty() || data_LQLQS==null){
					err.reject("","Questionare Data Kesehatan I masih Kosong. Silakan mengisi questionare Data Kesehatan I terlebih dahulu");
				}
				if(data_LQLQS2.isEmpty() || data_LQLQS2==null){
					err.reject("","Questionare Data Kesehatan II masih Kosong. Silakan mengisi questionare Data Kesehatan II terlebih dahulu");
				}
			}
		}
		
		//Untuk inputan syariah, ditambahankan validasi tambahan , harus cetak form konfirmasi sebelum transfer - Ryan
		if((lsbsId == 175 && lsdbsNumber == 2)) {
			Integer lewat = bacManager.selectCekKonfirmasiSyariah(command.getSpaj());
			if(!Common.isEmpty(user.getJn_bank())){
				if(user.getJn_bank()==16 && lewat < 1){
					err.reject("", "UNTUK PRODUK POWERSAVE SYARIAH : Harap Melakukan Proses CETAK SURAT KONFIRMASI BSM - SYARIAH Terlebih Dahulu!");
				}
			}
		}
		//kolom tgl  terima spaj dan tanggal spaj doc tidak boleh kosong request Yusuf 20/12/2006 (email)
		
		Date tglTerimaSpaj=(Date)mTertanggung.get("MSTE_TGL_TERIMA_SPAJ");
		Date tglTerimaadmin=(Date)mTertanggung.get("MSTE_TGL_TERIMA_ADMIN");
		//
		List spajDitemp =bacManager.selectReferensiTempSpaj(command.getSpaj());
		
		if(!spajDitemp.isEmpty() && lcaId.equals("73") ){
			tglTerimaadmin = new Date();
		}
		
		if(command.getGuthrie()!=1) {
//			if((isInputanBank != 2 && isInputanBank != 3 && isInputanBank!=13 && !lcaId.equals("58") && (lsbsId!=182 && lsdbsNumber != 7) && (lsbsId!=182 && lsdbsNumber != 8) && (lsbsId!=182 && lsdbsNumber != 9)) ) {
//				if(tglTerimaSpaj==null) err.reject("","Tanggal Terima Spaj masih Kosong. Update dahulu tanggal terima spaj ini");
//			}
			
			if("2,3,16".indexOf(isInputanBank.toString())<=-1){
				if ("01,58".indexOf(lcaId)<=-1 || isInputanBank==4){
					if(tglTerimaadmin==null) err.reject("","Tanggal Terima Admin masih Kosong. Silakan mengisi tanggal terima Admin terlebih dahulu");
				}
			}
		}
		
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
		
		boolean allowed = true;
		
		if((lsbsId == 118 && lsdbsNumber == 3) || (lsbsId == 118 && lsdbsNumber == 4) || (lsbsId == 212 && lsdbsNumber == 9)){
			allowed = false;
		}
				
		
		
		if(lcaId.equals("09") && allowed){
			Map mapNasabah=uwManager.selectLeadNasabahFromSpaj(command.getSpaj());
			Map mReffBii=elionsManager.selectReferrerBii(command.getSpaj());
			if(mapNasabah.isEmpty() && mReffBii==null) err.reject("","Harap Input Referral Bank Terlebih Dahulu.");
		};
		
		if(!allowed){
			Datausulan dataUtama = elionsManager.selectDataUsulanutama(command.getSpaj());
			if(dataUtama != null){
				if(dataUtama.tipeproduk == 30) {// BCHANNEL
					Map mReffBii=elionsManager.selectReferrerBii(command.getSpaj());
					if(mReffBii == null){
						err.reject("","Harap Input Referral Bank Terlebih Dahulu Khusus untuk B Channel.");
					}
				}
			}
		};
		
		if(lcaId.equals("40") && lwkId.equals("01") && lsrg_id.equals("02")){
			Map mReffBii=elionsManager.selectReferrerBii(command.getSpaj());
			if( mReffBii==null) err.reject("","Harap Input Referral Bank Terlebih Dahulu.");
		}
		
		//validasi untuk DMTM harus mengisi refferal
		if(lcaId.equals("40") && "01,02".indexOf(lwkId)<0){
			String mspo_no_kerjasama = uwManager.selectMstPolicyMspoNoKerjasama(command.getSpaj());
			if(mspo_no_kerjasama==null || mspo_no_kerjasama.equals("") ){
				err.reject("","Untuk Polis DM/TM silakan mengisi reff DM/TM dahulu sebelum melakukan transfer.");
			}
		}
		if(lcaId.equals("58") && user.getLca_id().equals("58")){
			if(isInputanBank!=4){
				//validasi proses transfer untuk Mall Assurance: pernyataan kesehatan dan cetak espaj (untuk ceklist dicek, validasi ada di validasi ceklist atas)
				//1. Untuk pernyataan kesehatan
				List<MedQuest> mq = uwManager.selectMedQuest(command.getSpaj(),null);
				if(mq.size()<=0){
					err.reject("","Silakan melakukan pengisian Questionare terlebih dahulu sebelum melakukan transfer.");
				}
				//2.cetak espaj
				String cabang = elionsManager.selectCabangFromSpaj(command.getSpaj());
				File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+command.getSpaj()+"\\"+"espaj_"+command.getSpaj()+".pdf");
				if(!userDir.exists()){
					err.reject("","Silakan cetak E-SPAJ terlebih dahulu sebelum melakukan transfer.");
				}
			}
		}
		
		Pemegang dataPemegang = elionsManager.selectpp(command.getSpaj());
		
		if(user.getLus_id().equals("2326") && lsbsId!=178 && lsbsId!=208){
			Datausulan planUtamaProposal = uwManager.selectMstProposal(dataPemegang.getMspo_plan_provider());
			Datausulan planUtamaBac = elionsManager.selectDataUsulanutama(command.getSpaj());
			if(planUtamaProposal.getMspr_tsi().compareTo(planUtamaBac.getMspr_tsi()) !=0){
				err.reject("","UP yang dimasukkan di proposal sebesar "+planUtamaProposal.getLku_symbol() +FormatNumber.convertToTwoDigit(new BigDecimal(planUtamaProposal.getMspr_tsi()) )+"sedangkan UP di input SPAJ sebesar "+planUtamaBac.getLku_symbol() +FormatNumber.convertToTwoDigit(new BigDecimal(planUtamaBac.getMspr_tsi()) )+".Silakan proses Ulang Proposal terlebih dahulu apabila terjadi perubahan UP.");
			}
			
			List<Datausulan> planRiderProposal =  uwManager.selectMstProposalRider(dataPemegang.getMspo_plan_provider());
			List<Datarider> planRiderBac =  planUtamaBac.getDaftaRider();
			if(planRiderProposal.size()!= planRiderBac.size()){
				err.reject("","Terdapat perbedaan Jumlah rider antara inputan di Proposal sebanyak "+planRiderProposal.size()+" rider, sedangkan di inputan SPAJ sebanyak "+planRiderBac.size()+"Silahkan dicrosscek lagi sebelum Melakukan Transfer");
			}else{
				if(planRiderProposal.size()>0 && planRiderBac.size()>0){
					String tampungRiderProposal = "";
					String tampungRiderBac = "";
					for(int i=0;i<planRiderProposal.size();i++){
						Datausulan riderProposal = planRiderProposal.get(i);
						if(i==planRiderProposal.size()-1){
							tampungRiderProposal+=planRiderProposal.get(i).getPlan_rider();
						}else{
							tampungRiderProposal+=planRiderProposal.get(i).getPlan_rider()+",";
						}
					}
					for(int j=0;j<planRiderBac.size();j++){
						Datarider riderBac = (Datarider)planRiderBac.get(j);
						if(j==planRiderBac.size()-1){
							tampungRiderBac+=riderBac.getPlan_rider();
						}else{
							tampungRiderBac+=riderBac.getPlan_rider()+",";
						}
					}
					
					//Comparing dari sisi inputan rider proposal
					for(int countBac=0; countBac<planRiderBac.size();countBac++){
						Datarider riderBac = (Datarider)planRiderBac.get(countBac);
						if(tampungRiderProposal.indexOf(riderBac.getPlan_rider()) <0){//jika rider inputan SPAJ tidak ada di List Rider Proposal
							List tampungBac = uwManager.select_spesifik_produk_rider(riderBac.getLsbs_id(), riderBac.getLsdbs_number(), riderBac.getLsdbs_number());
							Map map = (Map) tampungBac.get(0);
							String nama_rider = (String)map.get("lsdbs_name");
							err.reject("","Rider "+nama_rider+" yang diinput di SPAJ tidak ada di inputan Proposal,Silakan dicek & disamakan antara proposal dan SPAJ.");
						}
					}
					
					//Comparing dari sisi inputan rider bac
					for(int countProposal=0; countProposal<planRiderProposal.size();countProposal++){
						Datausulan riderProposal = (Datausulan)planRiderProposal.get(countProposal);
						if(tampungRiderBac.indexOf(riderProposal.getPlan_rider()) <0){//jika rider inputan SPAJ tidak ada di List Rider Input SPAJ
							List tampungBac = uwManager.select_spesifik_produk_rider(riderProposal.getLsbs_id(), riderProposal.getLsdbs_number(), riderProposal.getLsdbs_number());
							Map map = (Map) tampungBac.get(0);
							String nama_rider = (String)map.get("lsdbs_name");
							err.reject("","Rider "+nama_rider+" yang diinput di Proposal tidak ada di inputan SPAJ,Silakan dicek & disamakan antara proposal dan SPAJ.");
						}
					}
				}
			}
		}
				
		//validasi utk mengecek apakah spaj sudah diotorisasi apa belum
		if( isInputanBank == 2/* && user.getLus_bas() != 2*/)
		{
			if( "11,29".indexOf( user.getLde_id() )==-1 && !user.getCab_bank().equals(""))
			{
				List selectSpajOtorisasiDisabled = uwManager.selectSpajOtorisasiDisabled( command.getSpaj() );
				if( selectSpajOtorisasiDisabled == null || selectSpajOtorisasiDisabled != null && selectSpajOtorisasiDisabled.size() == 0 )
				{
//					err.reject("", "SPAJ belum diotorisasi oleh SPV/KaCab");
				}
			}
		}
		
		//validasi untuk produk agency System
		pos=0;
		kode="";
		kode=props.getProperty("product.plan_asystem");
		pos=kode.indexOf(bisnisId);
		if (pos>= 0){
			if("37, 46, 52, 60, 73".indexOf(lcaId)==-1){
//			if( !(lcaId.equals("37")) &&  !(lcaId.equals("46")) && !(lcaId.equals("60")) ){
				String kodeTemp=props.getProperty("product.plan_asystemandregional");
				int posTemp=0;
				posTemp=kodeTemp.indexOf(bisnisId);
				boolean allow = true;
				
				if((lsbsId == 118 && lsdbsNumber == 3) || (lsbsId == 118 && lsdbsNumber == 4) ){
					allow = false;
				}
				
				if(posTemp<0 && allow) {
					err.reject("","Kode Plan yang Anda pilih hanya untuk Agency System");
				}
			}
		}
		if("37, 46, 52".contains(lcaId)){
//		if(lcaId.equals("37") || lcaId.equals("46")){
			kode=props.getProperty("product.plan_asystem");
			pos=kode.indexOf(bisnisId);
			if(pos==-1){
				//err.reject("","Kode Agent Agency System. Silahkan Pilih Hanya Kode Plan untuk Agency System  ");
			}
		}
		//validasi untuk kode bisnis ini hanya untuk bancassurance
		//validasi ini di-disable, karena sekarang produk2 save bancass bisa dijual di kantor pusat (yusuf, 28/5/09)
		if(lsbsId.intValue()== 142 || lsbsId.intValue()==155){
			if(! lcaId.equals("09") && ! lcaId.equals("40")){
				//err.reject("","Kode Plan Yang Anda Pilih hanya untuk Bancassurance dan DM/TM");
			}
		}
		//validasi untuk telemarketer produk dmtm
		if(lsbsId.intValue() == 142 && lsdbsNumber.intValue() == 8) {
			Map m = uwManager.selectTelemarketerFromSpaj(command.getSpaj());
			if(m == null) {
				err.reject("", "Harap input Telemarketer pada menu REFF DM/TM terlebih dahulu.");
			}
		}
		
		//validasi untuk produk excellink karyawan
		if(lsbsId.intValue() == 113 || lsbsId.intValue()== 138 || lsbsId.intValue()== 139 ){
			String ls_nik;
			if (!(lsAgen.equals("003725")) ){
				err.reject("","Excellink karyawan, Kode Agen Harus : 003725");
			}
			ls_nik=elionsManager.selectMstEmp(command.getSpaj());
			if(ls_nik==null)
				ls_nik="";
			
			if(ls_nik.length() == 0){
				err.reject("","Excellink Karyawan NIK Belum Di Input");
			}
		}
		//validasi untuk produk excellink new
		kode="";
		pos=0;
		kode=props.getProperty("product.excellink_baru");
		pos=kode.indexOf(bisnisId);
		if (pos>=0){	
			Integer li_rider=elionsManager.selectCountRiderExcellinkNew(command.getSpaj(),800,810);
			if(li_rider==null)
				li_rider=new Integer(0);
			//
			if(li_rider.intValue() > 0){
				err.reject("","Excellink Baru, Rider pada data usulan tidak boleh di pilih");
			}
		}

		//validasi untuk produk excellink 80 plus bulanan
		kode="";
		pos=0;
		kode=props.getProperty("product.planExcellink80Plus"); 
		pos=kode.indexOf(bisnisId);
		Personal personal = (Personal)this.elionsManager.selectProfilePerusahaan(dataPemegang.getMspo_customer());
		if(pos>=0){
			if(liPayMode.compareTo(new Integer(6))==0 && ( liCc==3) && personal.getFlag_ws()!=2) {
				err.reject("","Excellink 80 Plus Bulanan harus Autodebet Credit Card/Tabungan/Tunai");		
			}
		}
		
		//validasi untuk produk Cerdas
		kode="";
		pos=0;
		kode=props.getProperty("product.plan_cerdas");
		pos=kode.indexOf(bisnisId);
		if(pos>=0){
			if("9,12,21,24".indexOf(lsdbsNumber.toString())<=-1){
				if(liCc==0 || ("1,2,4".indexOf(liCc.toString())<=-1) ){
					err.reject("","Cerdas Harus AutoDebet");
				}
			}
		}
		
		//produk plan_cerdas rider harus diinput
		kode="";
		pos=0;
		kode=props.getProperty("product.plan_cerdasRider");
		pos=kode.indexOf(bisnisId);
		if(pos>= 0){	
			Integer li_rider=elionsManager.selectCountRiderExcellinkNew(command.getSpaj(),810,900);
			if(li_rider==null)
				li_rider=new Integer(0);
			
			if (li_rider.intValue() <= 0 && !bisnisId.equals("141")){
				err.reject("","Cerdas, Rider Belum di input");
				}
		}
		
		//validasi untuk produk Simponi
		Double ldec_bunga;
		kode="";
		pos=0;
		kode=props.getProperty("product.planSimponi");
		pos=kode.indexOf(bisnisId);
		if(pos>=0){
			ldec_bunga=elionsManager.selectNilaiUnderTable(command.getSpaj());
			if(ldec_bunga==null || ldec_bunga.compareTo(new Double(0))==0){
				err.reject("","Simponi harus input investasi");
			}
				
		}
		//recuring sudah tidak ada 29/03/2006
		//Autodebet, TransferPolis ke proses recurring  // recuring sudah tidak ada 29/03/2006
	/*	if(liCc==1 || liCc==2){
			kode="";
			pos=0;
			kode=ekalife.plan_sehat_privasi_investa;
			pos=kode.indexOf(bisnisId);
			if (pos> 0){
				Integer li_jenis = null;
				li_jenis=elionsManager.selectMstAccountRecur(command.getSpaj(),new Integer(1));
				if(li_jenis==null)
					li_jenis=new Integer(0);
				
				if (li_jenis.intValue() == 0) {
					err.reject("","Data Account Belum Di Input");
	
				}
				//
				if (li_jenis.intValue() !=liCc){
					err.reject("","Ada Kesalahan Input Account");
				}
				
			}else{//confirm proses recuring
				command.setError(new Integer(2));
				//lakukan proses recuring atao jalankan wf_trans_recur
			}
		}*/
		//
		if(namaPemegang==null){
			err.reject("","Nama Pemegang Polis Kosong");
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
			}else {
				ldec_persen=elionsManager.selectSumPersenMstDetUlink(command.getSpaj());
				if(ldec_persen==null)
					ldec_persen=new Double(0);
				
				if(ldec_persen.doubleValue()==0 || ldec_persen.doubleValue()!=100){
					err.reject("","Produk unit link alokasi investasi belum lengkap");
				}
				ldec_persen=new Double(0);
				ldec_persen=elionsManager.selectCountMstBiayaUlink(command.getSpaj());
				if(ldec_persen==null)
					ldec_persen=new Double(0);
				if(ldec_persen.doubleValue()==0){
					err.reject("","Biaya Alokasi Investasi Belum Lengkap");
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
				err.reject("","Produk Unit Link, Rekening Nasabah Belum di Input. Yakin Transfer");
			}
			//
			if(lsbsId.intValue()==87){
				ldec_premi=elionsManager.selectSumJlhPremiMstUlink(command.getSpaj());
				if(ldec_premi==null)
					ldec_premi=new Double(0);
				
				if(ldec_premi.doubleValue()<50000000){
					err.reject("","Excellink Platinum Total Premi Kurang dari Rp. 50 juta");
				}
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
			
			String lsbs_id = uwManager.selectBusinessId(command.getSpaj());
			
			if(!products.stableSavePremiBulanan(lsbs_id) && !products.stableSave(Integer.parseInt(lsbs_id), uwManager.selectBusinessNumber(command.getSpaj()))){
				if(Integer.parseInt(lsbs_id)==188){
					ll_pscount = uwManager.selectCountMstPowerSaveBaru(command.getSpaj());
					if(ll_pscount.longValue() == 0){
						err.reject("","Data Plan Power Save Spaj "+command.getSpaj()+" Belum Lengkap");
					}else{
						List ls_powersave=uwManager.selectMstPowerSaveProsesBaru(command.getSpaj(),new Integer(5), new Integer(1));
						for(int i=0;i<ls_powersave.size();i++){
							Map mPower=(HashMap) ls_powersave.get(i);
							li_aups=(Integer)mPower.get("MPS_RO");
							li_jmps=(Integer)mPower.get("MPS_MGI");
							ldec_psrate=(Double)mPower.get("MPS_RATE");
						}
						if(li_aups==null)
							li_aups=new Integer(0);
						if(li_jmps==null)
							li_jmps=new Integer(0);
						if(ldec_psrate==null)
							ldec_psrate=new Double(0);
						
						if (ldec_psrate.doubleValue()==0 || li_aups.intValue() == 0 || li_jmps.intValue() == 0){
							err.reject("","Data Plan Power Save Spaj "+command.getSpaj()+" Belum Lengkap");
						}
					}
				}else{
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
			}
			
		}
//		List lds_dp=new ArrayList();
//		//validasi untuk produk endowment (tidak harus input titipan premi 14-06-06)
//		//untuk inputan bank juga tidak harus input titipan premi (Yusuf - 14/12/2007)
//		if(!lcaId.equals("58") && lsbsId.intValue()!=157 && (lsbsId.intValue()!=171 || lsdbsNumber.intValue()!=2) && (lsbsId.intValue()!=182 || lsdbsNumber.intValue()!=7) && (lsbsId.intValue()!=182 || lsdbsNumber.intValue()!=8) && (lsbsId.intValue()!=182 || lsdbsNumber.intValue()!=9) && !products.powerSave(lsbsId.toString()) && !products.stableLink(lsbsId.toString())){
//			lds_dp=elionsManager.selectMstDepositPremium(command.getSpaj(),null);
//			if(lds_dp.size()==0){
//				err.reject("","Jumlah Titipan Premi Belum Di Input");
//			}
//		}
//		if(cekTransferBackToUw(lds_dp)==false){
//			err.reject("","Polis Ini tidak bisa Di transfer 2x\n Harap gunakan Tombol BackToUW");
//		}
//		/*validasi untuk produk multi-invest
//		**Rk s/d 28/02/03 boleh pakai 74, rk >= 01/03/03 pakai 76; berlaku s/d 31/03/03; mulai 01/04/03 harus 76
//		**tgl 1oct - 31 des05 pakai 135 & 136
//		**If li_bisnis = 74 or li_bisnis = 76 Then*/
//		kode="";
//		pos=0;
//		kode=props.getProperty("product.plan_minvest");
//		pos=kode.indexOf(bisnisId);
//		if (pos>= 0 ){
//			Date ldt_rk;
//			Calendar calAwal=new GregorianCalendar(2005,10-1,01);
//			Calendar calAkhir=new GregorianCalendar(2007,1-1,31);
//
//			for(int i=0;i<lds_dp.size();i++){
//				DepositPremium depPre=(DepositPremium) lds_dp.get(i);
//				ldt_rk = depPre.getMsdp_date_book();
//				if(lsbsId.intValue()== 96 || lsbsId.intValue()== 99){
//					if(FormatDate.isDateBetween(ldt_rk,calAwal.getTime(),calAkhir.getTime())){
//						err.reject("","Tgl RK SPAJ ini : "+defaultDateFormatStripes.format(ldt_rk)+" . Khusus periode 1 Oktober 2005 sampai dengan 31 Januari 2007");
//						err.reject("","Harus Pakai Multi-Invest III (new) 135/136 ");
//
//					}
//				}else if( lsbsId.intValue()== 135 || lsbsId.intValue()== 136 ){
//					if(! (FormatDate.isDateBetween(ldt_rk,calAwal.getTime(),calAkhir.getTime())) ){ 
//						err.reject("","Tgl RK Spaj ini "+defaultDateFormatStripes.format(ldt_rk)+" Di luar Periode 1 Oktober 2005 sampai dengan 31 Januari 2007");
//						err.reject("","Harus Pakai Multi-Invest III 96/99 ");	
//					}
//				}
//			
//			}	
//		}
		
		//validasi produk maxi Deposit (103, 114, 137) 2/1/2008 (ucup)
//		lds_dp=elionsManager.selectMstDepositPremium(command.getSpaj(),1);
//		if( (!lds_dp.isEmpty())){
//			Calendar calBatas=new GregorianCalendar(2007,12-1,31);
//			String productMaxiDeposit=props.getProperty("product.maxi_deposit");
//			Integer idx=productMaxiDeposit.indexOf(bisnisId);
//			DepositPremium depPre=(DepositPremium)lds_dp.get(0);
//			if(bisnisId.equals("137") || bisnisId.equals("114")){
//				if(bisnisId.equals("137")){
//					if(depPre.getMsdp_date_book().after(calBatas.getTime()) && idx>0 && (lsdbsNumber!=3 && lsdbsNumber!=4) ){
//						err.reject("","Produk Maxi Deposit Rp dapat di proses oleh bagian Undw adalah SPAJ yang diterima oleh AJS paling lambat tgl 31 Desember 2007.");
//					}
//				}else if(bisnisId.equals("114")){
//					if(depPre.getMsdp_date_book().after(calBatas.getTime()) && idx>0 && lsdbsNumber<2){ // sebelum direvisi, nilai lsdbsNumber<3
//						err.reject("","Produk Maxi Deposit Rp dapat di proses oleh bagian Undw adalah SPAJ yang diterima oleh AJS paling lambat tgl 31 Desember 2007.");
//					}
//				}
//			}else {
//				if(depPre.getMsdp_date_book().after(calBatas.getTime()) && idx>0){
//					err.reject("","Produk Maxi Deposit Rp dapat di proses oleh bagian Undw adalah SPAJ yang diterima oleh AJS paling lambat tgl 31 Desember 2007.");
//				}
//			}
//		}
		
		if(lssaId.intValue() == 2) {
			err.reject("", "SPAJ ini Status Aksepnya : DECLINE. Tidak bisa memproses SPAJ ini lebih lanjut sebelum merubah statusnya.");
		}
		
		if(lssaId.intValue() == 9) {
			err.reject("", "SPAJ ini Status Aksepnya : POSTPONED. Tidak bisa memproses SPAJ ini lebih lanjut sebelum merubah statusnya.");
		}
		
		Account_recur account_recur = elionsManager.select_account_recur(command.getSpaj());
		if(account_recur.getFlag_autodebet_nb()!=null){
			if(account_recur.getFlag_autodebet_nb()==1){
				err.reject("", "SPAJ ini Tidak bisa Ditransfer Karena Menggunakan Autodebet Premi Pertama. Silahkan gunakan tombol AUTODEBET PREMI PERTAMA.");
//				if(uwManager.selectCountMstBillingNB(command.getSpaj())>0){
//					//Deddy - (27 Jun 2014) Req Pengky : ditambahkan kondisi apabila drek_det belum ada, tidak bisa transfer.
//					if(elionsManager.selectstsgutri(command.getSpaj())!=4 && uwManager.selectMstDrekDet(null, command.getSpaj(), null, null).size() ==0){
//						err.reject("", "SPAJ ini Tidak bisa Ditransfer karena sedang dalam proses AutoDebet Di Finance.");
//					}
//				}
			}
		}
		
		//semua powersave, kecuali powersave karyawan, hanya boleh tambahan special rate max 0,5%,
		String validasiUcup = uwManager.selectValidasiRatePowersaveNewBusinessFromSpaj(command.getSpaj());
		String lca_id = elionsManager.selectCabangFromSpaj(command.getSpaj());
		
		if(lca_id.equals("58")){
			if(!products.productMallLikeBSM(lsbsId.intValue(), lsdbsNumber.intValue())) {
				List<MedQuest> mq = uwManager.selectMedQuest(command.getSpaj(),null);
				if(mq.size()<=1){
					err.reject("", "Khusus Mall Assurance : Mohon Pernyataan Kesehatan diisi untuk Pemegang Polis maupun Tertanggung");
				}
				String tampung = props.getProperty("pdf.dir.export")+"\\"+lca_id+"\\"+command.getSpaj()+"\\";
				List<DropDown> daftarFile =FileUtils.listFilesInDirectory(tampung);
				if(daftarFile.isEmpty()){
					err.reject("", "Khusus Mall Assurance : Silakan diprint terlebih dahulu E-SPAJ");
				}else{
					for (int i = 0; i < daftarFile.size(); i++) {
						String namaFile = daftarFile.get(i).getKey();
						if(namaFile.equals("espaj_"+command.getSpaj()+".pdf")){
							FileUtils file = new FileUtils();
							long size = file.GetFileSizeInBytes(tampung+"espaj_"+command.getSpaj()+".pdf");
							if(size==0){
								err.reject("", "File E-SPAJ Corrupt/Damage!, silakan menggunakan E-SPAJ manual");
							}
						}
					}
				}
			}
		}
	
		//kecuali securitas dan tutupan kantor pusat
		if(validasiUcup != null && user.getJn_bank()!=3 && !lca_id.equals("01")){
			err.reject("", validasiUcup);
		}
		
		if("40".indexOf(lca_id.toString()) >=0 && lwkId.equals("02")){
			Date jul182012		= defaultDateFormat.parse("18/7/2012");
			if(uwManager.selectMstInbox(command.getSpaj(),"0")==null && dataPemegang.getMspo_input_date().after(jul182012)){					
						String cabang = elionsManager.selectCabangFromSpaj(command.getSpaj());
						String path = props.getProperty("pdf.dir.export").trim() + "\\" + 
										cabang.trim() + "\\" + 
										command.getSpaj().trim() + "\\" + 
										command.getSpaj().trim() + "BSB 001.pdf";
						File file = new File(path);
						if(!file.exists()){
							err.reject("", "Proses Transfer tidak dapat dilakukan sebelum melakukan upload scan(Terutama Upload Scan BSB). Silakan upload scan terlebih dahulu.");
						}
						file = null;
					
			}
		}
		
		if(lsbsId==120 && "22,23,24".indexOf(lsdbsNumber.toString()) >=0 
				|| lsbsId==202 && "4,5,6".indexOf(lsdbsNumber.toString()) >=0 
				|| lsbsId==213 && (lsdbsNumber==1 || lsdbsNumber==2) ){
			Integer lssaId_Bas=bacManager.selectStatusSpajBas(command.getSpaj());
			if (lssaId_Bas==null)lssaId_Bas=0;
			if(lssaId_Bas!=16){
				err.reject("","Status SPAJ ini masih Further Bas Harap di Aksep terlebih dahulu");
			}
			//Request Chizni Helpdesk 59871, Yusy Helpdesk 77162
			//Update hanya file new business ssaja yg divalidasi, diganti hanya file SPAJ yang divalidasi
			String s_cabang = elionsManager.selectCabangFromSpaj(command.getSpaj());
			//List spajDitemp =bacManager.selectReferensiTempSpaj(command.getSpaj());
			String s_namaFile="SPAJ 001.pdf;";
			
			String [] s_pathNamaFile=s_namaFile.split(";");
			for(int i=0;i<s_pathNamaFile.length;i++){
				String s_path=props.getProperty("pdf.dir.export").trim() + "\\" + 
						s_cabang.trim() + "\\" + 
						command.getSpaj().trim() + "\\" + 
						command.getSpaj().trim();
				s_path=s_path+ s_pathNamaFile[i];
				File file = new File(s_path);
				if(!file.exists() && spajDitemp.isEmpty()){
					err.reject("", "Proses Transfer tidak dapat dilakukan sebelum melakukan upload scan"+ "("+ s_pathNamaFile[i]+")"+" .Silakan upload scan terlebih dahulu.");
				}
				file = null;
			}
		}
		
		//selain tutupan bancass dan mall, ditambahkan validasi bahwa admin harus melakukan upload file sebelum transfer.
		if((("09,40,58".indexOf(lca_id.toString()) <0) || (lsbsId==164 && lsdbsNumber==11))){
			Date jul182012		= defaultDateFormat.parse("18/7/2012");
			if(uwManager.selectMstInbox(command.getSpaj(),"0")==null && dataPemegang.getMspo_input_date().after(jul182012)){
					if(isInputanBank < 0 || isInputanBank == 2) {
						String cabang = elionsManager.selectCabangFromSpaj(command.getSpaj());
						String path = props.getProperty("pdf.dir.export").trim() + "\\" + 
										cabang.trim() + "\\" + 
										command.getSpaj().trim() + "\\" + 
										command.getSpaj().trim() + "SPAJ 001.pdf";
						File file = new File(path);
						if(!file.exists()){
							err.reject("", "Proses Transfer tidak dapat dilakukan sebelum melakukan upload scan(Terutama Upload Scan SPAJ). Silakan upload scan terlebih dahulu.");
						}
						file = null;
					}
			}
		}
				
		/**
		 * MANTA (18-06-2015) - Req By Feri (73254)
		 * Untuk Produk Bancass harus upload scan SPAJ, KTP, BSB
		 * Ryan (10-11-2015) - Req Feri (79290) SPH dihapus dari validasi
		 */
		if(isInputanBank==2 && (lsbsId!=120 && lsbsId!=163 && lsbsId!=183 && lsbsId!=213 && lsbsId!=216 && (lsbsId==134 && lsdbsNumber!=5 && lsdbsNumber!=10))){
			String cabang = elionsManager.selectCabangFromSpaj(command.getSpaj());
			String s_namaFile = "SPAJ_BB 001.pdf;KTP_BB 001.pdf;BSB_BB 001.pdf;";
			String[] s_pathNamaFile = s_namaFile.split(";");
			
			for(int i=0; i<s_pathNamaFile.length; i++){
				String s_path = props.getProperty("pdf.dir.export").trim() + "\\" + 
								cabang.trim() + "\\" + 
								command.getSpaj().trim() + "\\" + 
								command.getSpaj().trim() + s_pathNamaFile[i];
				File file = new File(s_path);
				if(!file.exists()){
					err.reject("", "Proses Transfer tidak dapat dilakukan sebelum melakukan upload scan"+ "("+ s_pathNamaFile[i]+")"+" .Silakan upload scan terlebih dahulu");
				}
				file = null;
			}
		}
		
		if(lsbsId==175 && lsdbsNumber==2){
			String cabang = elionsManager.selectCabangFromSpaj(command.getSpaj());
			String s_namaFile = "SPAJ 001.pdf";
			String[] s_pathNamaFile = s_namaFile.split(";");
			
			for(int i=0; i<s_pathNamaFile.length; i++){
				String s_path = props.getProperty("pdf.dir.export").trim() + "\\" + 
								cabang.trim() + "\\" + 
								command.getSpaj().trim() + "\\" + 
								command.getSpaj().trim() + s_pathNamaFile[i];
				File file = new File(s_path);
				if(!file.exists()){
					err.reject("", "Proses Transfer tidak dapat dilakukan sebelum melakukan upload scan"+ "("+ s_pathNamaFile[i]+")"+" .Silakan upload scan terlebih dahulu.");
				}
				file = null;
			}
		}
		
		Boolean cekInbox = true;
		//	cekInbox = uwManager.selectMstInbox(command.getSpaj(),"1")==null?false:true;
		List<Map> datainbox = uwManager.selectMstInbox(command.getSpaj(),"1");
		if (datainbox.isEmpty()){
			cekInbox = false;
		}
		
		if(cekInbox==false){
			if(("09,58".indexOf(lca_id.toString()) <0) || ((lsbsId==164 && lsdbsNumber==11) || ((lsbsId==120 && "22,23,24".indexOf(lsdbsNumber)>=0 )))){
				cekInbox=true;
			}
		}
		//Rahmayanti - Snows sementara
		if(isInputanBank==2||isInputanBank==3||isInputanBank==16) cekInbox=false;
		if(lcaId.equals("40") && "01,02".indexOf(lwkId)>=0){
			cekInbox=false;
		}
		
		//Ridhaal - Produk ERBE Teergantung dari proses apa - System Erbe / Inputan
		//List spajDitemp =bacManager.selectReferensiTempSpaj(command.getSpaj());
		if(!spajDitemp.isEmpty() && lcaId.equals("73") ){
			cekInbox=false;
		}
		
		if(cekInbox==true){
			String cabang = elionsManager.selectCabangFromSpaj(command.getSpaj());
			String path = props.getProperty("pdf.dir.export").trim() + "\\" + 
							cabang.trim() + "\\" + 
							command.getSpaj().trim() + "\\" + 
							command.getSpaj().trim() + "SPAJ 001.pdf";
			File file = new File(path);
			if(!file.exists()){
				err.reject("", "Proses Transfer tidak dapat dilakukan sebelum melakukan upload scan (Terutama Upload Scan SPAJ). Silakan upload scan terlebih dahulu."  + lsbsId + "1" + lca_id + " ");
			}
			file = null;
		}
		
		//Ridhaal
		//validasi provestara bahwa admin harus melakukan upload file medis sebelum transfer untuk usia tertanggung 50 - 65 tahun.
		if(lsbsId == 214){
			Date jul182012 = defaultDateFormat.parse("18/7/2012");
			List ls_usia = elionsManager.selectUsiaTT(command.getSpaj(),50,65);			
			if(!ls_usia.isEmpty() && dataPemegang.getMspo_input_date().after(jul182012)){					
				String cabang = elionsManager.selectCabangFromSpaj(command.getSpaj());
				String path = props.getProperty("pdf.dir.export").trim() + "\\" + 
								cabang.trim() + "\\" + 
								command.getSpaj().trim() + "\\" + 
								command.getSpaj().trim() + "MEDIS 001.pdf";
				File file = new File(path);
				if(!file.exists()){
					err.reject("", "Proses Transfer tidak dapat dilakukan sebelum melakukan upload scan Hasil Medis dikarenakan usia tertanggung di antara 50 - 65 tahun. Silakan upload scan hasil General Check up (GCU) terakhir terlebih dahulu. Pemeriksaan yang dilakukan minimum : LPK, urin lengkap, LED, gula darah puasa, khreatinin dan Kolesterol.");
				}
				file = null;
			}
		}
		
		
		//cek SPAJ Free dari Promo Buy 1 Get 1 bukan. - Ridhaal 
//		List cekSpajPromob1g1 = bacManager.selectCekSpajPromo(  null , command.getSpaj(),  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ dan jen_promo = 1 untuk promo = Program buy 1 policy get other policy
//		
//		if(!cekSpajPromob1g1.isEmpty()){//cek jika spaj sudah terdaftar dengan spaj Free di MST_FREE_SPAJdan cek status jika 0 berarti masih menunggu approval Nb/UW apakah bisa free atau tidak
//			Map m = (HashMap) cekSpajPromob1g1.get(0);
//			Integer process_type = ((BigDecimal)m.get("PROCESS_TYPE")).intValue();
//			
//			if (process_type == 0){
//				err.reject("", "SPAJ belum bisa di transfer dikarenakan SPAJ merupakan SPAJ Promo Buy 1 Get 1. FREE SPAJ bisa di transfer setelah mendapatkan Approval dari UW.");
//			}else if (process_type == 2){
//				err.reject("", "SPAJ tidak bisa di transfer dikarenakan SPAJ merupakan SPAJ Promo Buy 1 Get 1. FREE SPAJ tidak bisa di transfer dikarenakan tidak mendapatkan Approval dari UW.");
//			}
//		}
		
		
	}
/**
 * 	Fungsi : untuk Mengecek Apakah SPAJ ini sudah pernah di transfer dari BAC ke UW
 **/
//	private boolean cekTransferBackToUw(List lsDp){
//		boolean hsl=true;
//		Integer lsRekId;
//		String msdpNoPre;
//		
//		for(int i=0;i<lsDp.size();i++){
//			
//			DepositPremium deposit=(DepositPremium)lsDp.get(i);
//			lsRekId=deposit.getLsrek_id();
//			msdpNoPre=deposit.getMsdp_no_pre();
//			if( (lsRekId.intValue()==0 && (msdpNoPre==null || msdpNoPre.equals("")) ) || 
//					((lsRekId.intValue()!=0 && (msdpNoPre==null || msdpNoPre.equals("") ) ) ) ){//
//				hsl=true; //proses transfer boleh dilakukan
//			}else{
//				hsl=false;
//				break;
//			}
//		
//		}
//		return hsl;
//	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		String pesanTambahan = null;
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		pesanTambahan = uwManager.cekRandomSamplingBii(command.getSpaj());
		if(command.getError().intValue()==1){//proses transfer ke U/W
			uwManager.prosesTransferBacToUw(command.getSpaj(),currentUser.getLus_id(),command.getLsbsId(),command.getNamaPemegang(),command.getGuthrie(),err,request);

// proses recurring sudah tidak ada (Yusuf - 14/12/2007)
//		}else if(command.getError().intValue()==2){//proses transfer ke rucurring
//			Integer liJenis=elionsManager.selectMstAccountRecur(command.getSpaj(),new Integer(1));
//			if(liJenis==null || liJenis.intValue()==0)
//				err.reject("Data Account Belum di Input");
//			if(command.getFlagAdd()!=liJenis.intValue())
//				err.reject("","Ada Kesalahan Input Account");
//			if(err.getErrorCount()==0){
//					elionsManager.prosesRecurring(command.getSpaj(),1,currentUser.getLus_id());
//			}
		
		}
		
		if(pesanTambahan == null) return new ModelAndView("bac/transferBacToUw", err.getModel()).addObject("submitSuccess", "true");
		else return new ModelAndView("bac/transferBacToUw", err.getModel()).addObject("pesanTambahan", pesanTambahan);
	}

}