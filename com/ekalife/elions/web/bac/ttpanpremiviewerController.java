package com.ekalife.elions.web.bac;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetailPembayaran;
import com.ekalife.elions.model.DrekDet;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.ekalife.utils.parent.ParentFormController;
/**
 * @author HEMILDA
 * Controller titipan premi
 */

public class ttpanpremiviewerController extends ParentFormController {

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}	
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", ServletRequestUtils.getStringParameter(request, "editSPAJ", ""));

		Datausulan datausulan = new Datausulan();
        datausulan = elionsManager.selectDataUsulanutama(spaj);
        
		String jenisDisplay;
		Integer isUlink = this.uwManager.isUlinkBasedSpajNo( spaj );
		if( isUlink != null && isUlink > 0 ){
			jenisDisplay = "";
		}else{
			Integer isSlink = this.uwManager.isSlinkBasedSpajNo( spaj );
			if( isSlink != null && isSlink > 0 ){
				jenisDisplay = "";
			}else{
				jenisDisplay = "none";
			}
		}
	
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("listBankEkaLife", this.uwManager.selectBankEkaLife());		
		map.put("listKurs", this.elionsManager.selectKurs());
		map.put("listBankPusat", this.uwManager.selectBankPusat());
		map.put("tglsekarang", defaultDateFormat.format(new Date()));
		map.put("premike", this.elionsManager.selectpremike(spaj));
		map.put("blankon", this.elionsManager.selectBlanko(spaj));		
		map.put("jenisDisplay", jenisDisplay);
		map.put("premi", datausulan.getMspr_premium().intValue());
		map.put("userOut",this.elionsManager.selectUserTtpPremi(""));
		map.put("listMerchant", bacManager.selectLstMerchantFee(null));
		return map;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		DetailPembayaran datautama = new DetailPembayaran();
		User currentUser = (User) request.getSession().getAttribute("currentUser");		
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", ServletRequestUtils.getStringParameter(request, "editSPAJ", ""));
		String keberapa = ServletRequestUtils.getStringParameter(request, "ke", ServletRequestUtils.getStringParameter(request, "ke", ""));
		String sts = ServletRequestUtils.getStringParameter(request, "sts", ServletRequestUtils.getStringParameter(request, "sts", ""));
		Integer flag_tu = ServletRequestUtils.getIntParameter(request, "flag_tu", 0);
		if(flag_tu == 0) flag_tu = null;
		
		Datausulan datausulan = new Datausulan();
        datausulan = elionsManager.selectDataUsulanutama(spaj);
        
        JasperScriptlet jasper = new JasperScriptlet();
        
		//Cek akses titipan premi disini
		String lsbs_id = datausulan.getLsbs_id().toString();
		Integer lsdbs_number = datausulan.getLsdbs_number();
		String pesanError = "";
		if(lsbs_id.equals("157") || products.powerSave(lsbs_id) || products.stableLink(lsbs_id)){
			if(!(products.powerSave(lsbs_id) || products.stableLink(lsbs_id))){
				if(lsbs_id.equals("164") && lsdbs_number!=11){
					pesanError = "Titipan Premi tidak perlu diisi untuk produk ini, proses selanjutnya adalah Checklist dan Transfer";
				}
			}
			Integer jn_bank = elionsManager.selectIsInputanBank(spaj);
			String lca_id = elionsManager.selectLcaIdBySpaj(spaj);
			if(jn_bank == 2 || jn_bank == 3 || (lca_id.equals("58") && (products.powerSave(lsbs_id) || products.stableLink(lsbs_id) ) )){ //BSM, SMS
				if(lsbs_id.equals("164") && lsdbs_number!=11){
					pesanError = "Titipan Premi tidak perlu diisi untuk produk ini, silahkan (1) Input Referal, (2) Input Checklist, (3) Transfer";
				}
			}
			request.setAttribute("pesanError", pesanError);
		}
		
		Account_recur account_recur = elionsManager.select_account_recur(spaj);
		if(account_recur.getFlag_autodebet_nb()!=null){
			if(account_recur.getFlag_autodebet_nb()==1){
				pesanError = "Titipan Premi tidak perlu diisi apabila Premi Pertama dilakukan secara autoDebet.Silakan dilanjutkan proses selanjutnya.";
				request.setAttribute("pesanError", pesanError);
			}
		}
		
		datautama.setJenisTransaksiList(bacManager.jenisTransaksiListTitipanPremi(spaj, null));
		datautama.setTo("");
		HashMap map = Common.serializableMap(uwManager.selectLeadNasabahFromSpaj(spaj));
		Integer jnsNasabah = (Integer)map.get("JN_NASABAH");
		if(jnsNasabah==null) jnsNasabah = 0;
		if(jnsNasabah.intValue()==1){
			datautama.setTo("Jenis Nasabah Platinum!");
		}else if(jnsNasabah.intValue()==2){
			datautama.setTo("Jenis Nasabah Reguler!");
		}else if(jnsNasabah.intValue()==3){
			datautama.setTo("Jenis Nasabah Gold Link!");
		}else if(jnsNasabah.intValue()==4){
			datautama.setTo("Jenis Nasabah Prolink!");
		}

		String kurs = null;
		Integer ke = null;
		Double nilai_kurs = new Double(1);
		if(spaj==null){
			kurs = "01";
			spaj = "";
		}else{
			kurs = this.elionsManager.select_kurs(spaj);
		}
		if (kurs.equalsIgnoreCase("02"));{
			nilai_kurs = null;
		}
		datautama.setHit_err(new Integer(0));
		if(!(keberapa.equalsIgnoreCase(""))){
			//JOptionPane.showMessageDialog(null, "keberapa : " + keberapa, "PESAN", JOptionPane.ERROR_MESSAGE);
			datautama.setNo_ke(new Integer(Integer.parseInt(keberapa)));	
		}
		
		//if(lsbs_id.equals("183") && lsdbs_number >= 50 && lsdbs_number <= 60){
		BigDecimal freepayment = bacManager.selectMstSpajFree(spaj);
		if(freepayment == null) freepayment = new BigDecimal(0);
		if(freepayment.intValue()>0){
			String simbol = "Rp ";
			if(kurs.equals("02")) simbol = "USD ";
			request.setAttribute("freepayment", simbol + jasper.format2Digit(freepayment));
		}
		
		/**
		 * @author HEMILDA
		 * pada saat memilih ke berapa.
		 */
		if(keberapa!=""){
			ke = new Integer(Integer.parseInt(keberapa));
				
			DetailPembayaran listpremi = this.elionsManager.premike(spaj,ke);
			
			listpremi.setTo("");
			map = Common.serializableMap(uwManager.selectLeadNasabahFromSpaj(spaj));
			jnsNasabah = (Integer)map.get("JN_NASABAH");
			if(jnsNasabah==null) jnsNasabah = 0;
			if(jnsNasabah.intValue()==1){
				listpremi.setTo("Jenis Nasabah Platinum!");
			}else if(jnsNasabah.intValue()==2){
				listpremi.setTo("Jenis Nasabah Reguler!");
			}else if(jnsNasabah.intValue()==3){
				listpremi.setTo("Jenis Nasabah Gold Link!");
			}else if(jnsNasabah.intValue()==4){
				listpremi.setTo("Jenis Nasabah Prolink!");
			}
			
			if (listpremi!=null){
				listpremi.setJenisTransaksiList(bacManager.jenisTransaksiListTitipanPremi(spaj, null));
				listpremi.setSubmitType("update");
				List<DrekDet> mstDrekDetBasedSpaj = uwManager.selectMstDrekDet( null, spaj, null, null ); 
				String noTrx = "";
				
				if(mstDrekDetBasedSpaj!=null && mstDrekDetBasedSpaj.size()>0){
					for(int i=0; i<mstDrekDetBasedSpaj.size(); i++){
						if(!Common.isEmpty(listpremi.getKeterangan())){
							if(listpremi.getKeterangan().contains(mstDrekDetBasedSpaj.get(i).getNo_trx())){
								noTrx = mstDrekDetBasedSpaj.get(i).getNo_trx();
							}
						}
					}
				}
				if(noTrx!=null && !"".equals(noTrx)){
					listpremi.setNo_trx(noTrx);
					ArrayList mstDrekBasedNoTrx = Common.serializableList(uwManager.selectMstDrekBasedNoTrx(noTrx));
					Map viewMstDrekDetail = (Map) mstDrekBasedNoTrx.get(0);
					if(viewMstDrekDetail!=null && viewMstDrekDetail.size()>0){
						Object flagTunggalGabungan = viewMstDrekDetail.get("FLAG_TUNGGAL_GABUNGAN");
						if(flagTunggalGabungan!=null){
							if("0".equals( flagTunggalGabungan.toString() )){
								listpremi.setTipe("Tunggal");
							}else if("1".equals( flagTunggalGabungan.toString() )){
								listpremi.setTipe("Gabungan");
							}else{
								listpremi.setTipe("");
							}
						}else{
							listpremi.setTipe("");
						}
					}else{
						listpremi.setTipe("");
					}
					
					List<DrekDet> mstDrekDetBasedNoTrx = uwManager.selectMstDrekDet( noTrx, spaj, null, null );
					if(mstDrekDetBasedNoTrx!=null && mstDrekDetBasedNoTrx.size()>0){
						if(listpremi.getJenisTransaksiList()!=null && listpremi.getJenisTransaksiList().size()>0){
							if(mstDrekDetBasedNoTrx.size()==1){
								listpremi.setJenis( mstDrekDetBasedNoTrx.get(LazyConverter.toInt(1) - 1).getTahun_ke()+"@"+mstDrekDetBasedNoTrx.get(LazyConverter.toInt(1) - 1).getPremi_ke());
							}else{
								listpremi.setJenis( mstDrekDetBasedNoTrx.get(LazyConverter.toInt(keberapa) - 1).getTahun_ke()+"@"+mstDrekDetBasedNoTrx.get(LazyConverter.toInt(keberapa) - 1).getPremi_ke());
							}
						}
					}
				}			
			}
			return listpremi;
			
		}else{
			/**
			 * @author HEMILDA
			 * baru belum ada yang keberapa
			 */
            datautama.setTgl_bayar(datausulan.getMste_beg_date());
			datautama.setReg_spaj(spaj);
			datautama.setNilai_kurs(nilai_kurs);
			datautama.setKurs(kurs);
			datautama.setTgl_rk(datausulan.getMste_beg_date());			
			datautama.setTgl_jatuh_tempo(null);
			datautama.setTgl_skrg(new Date());
			datautama.setClient_bank(null);
			datautama.setTipe(null);
			datautama.setJenis(null);
			if(sts!=""){
				datautama.setSubmitType(sts);
			}else{
				datautama.setSubmitType("edit");
			}
			datautama.setLus_id(currentUser.getLus_id());
			datautama.setLus_login_name(currentUser.getName());
			datautama.setAktif(new Integer(0));
			datautama.setStatus("B");
			datautama.setMsdp_flag_topup(flag_tu);
			return datautama;
		}
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		DetailPembayaran dp = (DetailPembayaran) cmd;
		String hasil = "";
		
		if(!(dp.getSubmitType().equalsIgnoreCase("DELETE"))){
			dp.setHit_err(new Integer(0));
			f_validasi data = new f_validasi();
			String spaj1 = dp.getReg_spaj();		
			
			if(dp.getTgl_jatuh_tempo().equalsIgnoreCase("__/__/____")){
				dp.setTgl_jatuh_tempo(null);
			}

			String nama_produk = "";
			String cabang = "";
			Integer kode_produk;
			Integer number_produk;
			Integer flag_worksite = new Integer(0);
			Integer flag_gutri = new Integer(0);	
			Integer simas = new Integer(0);
			if(spaj1!=null){
				/**
				 * @author HEMILDA
				 * cari flag dan cabang.
				 */

				Datausulan detil = (Datausulan)this.elionsManager.selectDataUsulanutama(spaj1);
				kode_produk = detil.getLsbs_id();
				number_produk = detil.getLsdbs_number();
				flag_gutri = detil.getMste_flag_guthrie();
				if(flag_gutri==null){
					flag_gutri = new Integer(0);
				}
				
				if (Integer.toString(kode_produk.intValue()).trim().length()==1){
					nama_produk = "produk_asuransi.n_prod_0"+kode_produk;	
				}else{
					nama_produk = "produk_asuransi.n_prod_"+kode_produk;	
				}
				
				try{
					Class aClass = Class.forName( nama_produk );
					n_prod produk = (n_prod)aClass.newInstance();
					produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
					produk.cek_flag_agen(kode_produk.intValue(),number_produk.intValue(), 0);
					flag_worksite = new Integer(produk.flag_worksite);
					cabang = detil.getLca_id();
					simas = new Integer(produk.simas);
					
				}catch (ClassNotFoundException e){
					logger.error("ERROR :", e);
				}catch (InstantiationException e){
					logger.error("ERROR :", e);
				}catch (IllegalAccessException e){
					logger.error("ERROR :", e);
				}		
			}
		
			/**
			 * @author HEMILDA
			 * kalau worksite boleh isi boleh tidak untuk kttp nya.
			 * UPDATE : Yusuf (10/09/07) - BTPP sudah tidak perlu lagi
			 */

			/*
			if (flag_worksite.intValue() != 1 && !cabang.equalsIgnoreCase("09") && flag_gutri.intValue()==0 && simas.intValue() == 0)
			{
				if (dp.getNo_kttp().trim().equals(""))
				{
					errors.rejectValue("no_kttp","","Silahkan masukkan terlebih dahulu nomor KTTP.");
					dp.setHit_err(new Integer(1));
				}else if (!dp.getNo_kttp().trim().substring(0, 1).toUpperCase().equalsIgnoreCase("A"))
					{
					cekk=data.f_validasi_numerik(dp.getNo_kttp());
					if (cekk==false)
					{
						errors.rejectValue("no_kttp","","No KTTP harus dalam numerik dan tanpa spasi.");
						dp.setHit_err(new Integer(1));
					}else{
						if ((dp.getNo_kttp()).trim().length()!=6)
						{
							errors.rejectValue("no_kttp","","No KTTP harus 6 digit.");
							dp.setHit_err(new Integer(1));
						}else{
							int hitung = this.elionsManager.countmstcontrolpayment(dp.getNo_kttp());
							if (hitung ==0)
							{
								errors.rejectValue("no_kttp","","No. KTTP " + dp.getNo_kttp() + " Tidak Ada !!!");
								dp.setHit_err(new Integer(1));
							}else{
								String spaj_kttp = "";
								List c = this.elionsManager.ceknokttp(dp.getNo_kttp().trim());
								int ht=0;
								for(int m=0;m<c.size();m++)
								{
									Map b = (HashMap) c.get(m);
									spaj_kttp = (String)b.get("SPAJ_S");
									if (!(spaj1.equalsIgnoreCase(spaj_kttp)))
									{
										ht=ht+1;
									}
								}
								if (ht>0)
								{
									errors.rejectValue("no_kttp","","No. KTTP tersebut sudah di pakai oleh SPAJ dengan nomor " + FormatString.nomorSPAJ(spaj_kttp));
									dp.setHit_err(new Integer(1));
								}
							}
						}
					}
				}
			}
			*/
			
			Integer flag = this.elionsManager.select_flag_cc(dp.getReg_spaj());
			if(flag==null){
				flag = new Integer(0);
			}
				
			Double jumlah_bayar = new Double(0);
			if(dp.getJml_bayar()==null){
				errors.rejectValue("jml_bayar","","Silahkan masukkan terlebih dahulu jumlah bayar.");
				dp.setHit_err(new Integer(1));
			}else{
				jumlah_bayar=dp.getJml_bayar();				
			}
			
			if((dp.getCara_bayar()!=null)){
				if(dp.getCara_bayar().intValue()==11){
					if(dp.getRef_polis_no().trim().length()==0){
						errors.rejectValue("ref_polis_no","","Silahkan masukkan Ref Polis No terlebih dahulu.");
						dp.setHit_err(new Integer(1));
					}else{
						/**
						 * @author HEMILDA
						 * tahapan
						 */

						List a = this.elionsManager.tahapanttppremi(dp.getRef_polis_no());
						if(a.size()>0){
							String ktr = "";
							String ktr1 = "TAHAPAN NO. ";
							String kurs = null;
							Date tgl_konfirmasi = null;
							Double jumlah_tahap = new Double(0);
							for(int k=0; k<a.size(); k++){
								Map b = (HashMap) a.get(k);
								ktr = (String)b.get("MSTAH_NO_TAHAPAN");
								ktr1 = ktr1 + ktr.substring(0,5) + "/" + ktr.substring(6,8) + "/" + ktr.substring(9,10) + "/" + ktr.substring(11,14) + " ;";
								jumlah_tahap = new Double((jumlah_tahap.doubleValue())+(((Double)b.get("MSBAT_JUMLAH"))).doubleValue());
								kurs = (String) b.get("LKU_ID");
								tgl_konfirmasi = (Date)b.get("MSTAH_TGL_KONFIRMASI");
							}
							jumlah_bayar = jumlah_tahap;
							dp.setKurs(kurs);
							//dp.setTgl_bayar(tgl_konfirmasi);
							//dp.setJml_bayar(jumlah_tahap);
							dp.setKeterangan(ktr1);
							
						}else{
//							errors.rejectValue("ref_polis_no","","Polis ini tidak ada kompensasi tahapan !!!");
//							dp.setHit_err(new Integer(1));
						}
					}
				}
			}else{
				errors.rejectValue("cara_bayar","","Silahkan pilih cara bayar terlebih dahulu");
				dp.setHit_err(new Integer(1));
			}
			
			if(dp.getBank()==null){
				errors.rejectValue("bank","","Silahkan pilih nama Bank AJ Sinarmas terlebih dahulu.");
				dp.setBank(0);
				dp.setHit_err(new Integer(1));
			}
						
			if(dp.getJenisTransaksiList()!=null && dp.getJenisTransaksiList().size()>0){
				if(dp.getJenis()==null || "".equals(dp.getJenis())){
					errors.rejectValue("jenis","","Silahkan pilih Jenis Transaksi terlebih dahulu");
					dp.setHit_err(new Integer(1));
				}
			}
			
			/**
			 * @author HEMILDA
			 * nama client bank.
			 */
			if(flag.intValue()>=1){
				if(dp.getClient_bank()==null){
					errors.rejectValue("client_bank","","Silahkan pilih nama Client Bank terlebih dahulu.");
					dp.setHit_err(new Integer(1));
				}
				if(dp.getNo_rek().trim().length()!=0){
					hasil = data.f_validasi_no_rek(dp.getNo_rek());
					if(hasil.trim().length()!=0){
						errors.rejectValue("no_rek","",hasil);
						dp.setHit_err(new Integer(1));
					}
				}
				if(dp.getTgl_jatuh_tempo()==null){
					errors.rejectValue("tgl_jatuh_tempo","","Silahkan isi tanggal jatuh tempo terlebih dahulu.");
					dp.setHit_err(new Integer(1));
				}
			}
			
			/**
			 * @author HEMILDA
			 * pengecekan nilai kurs.
			 */
			Double total_bayar = new Double(0);
			if(dp.getNilai_kurs()==null){
				errors.rejectValue("nilai_kurs","","Tidak ada kurs untuk tanggal RK yang dimasukkan.");
				dp.setHit_err(new Integer(1));
			}
			if(dp.getNilai_kurs()!=null){
				total_bayar = new Double((jumlah_bayar.doubleValue())*((dp.getNilai_kurs()).doubleValue()));
			}
			
			/**
			 * @author Lufi
			 * pengecekan rekening iBank.
			 */
			if(dp.getBank()!=0){
				if(dp.getNo_trx()==null || "".equals(dp.getNo_trx())){
					errors.reject("payment.noIbank");
					errors.rejectValue("no_trx","","Harap mengisi Ibanknya terlebih dahulu.Silakan dikonfirmasikan ke Finance apabila Ibank tidak ada.");
					dp.setHit_err(new Integer(1));
				}else{
					Double sisa_saldo;
					if(dp.getSubmitType().equalsIgnoreCase("edit")){
						sisa_saldo = uwManager.selectCheckTotalUsedMstDrek(dp.getNo_trx(), null, dp.getReg_spaj(), dp.getNo_ke());
					}else{
						sisa_saldo = uwManager.selectCheckTotalUsedMstDrek(dp.getNo_trx(), null, null, null);
					}
					
					if(dp.getJml_bayar()>sisa_saldo){
						errors.rejectValue("jml_bayar", "", "Premi yg diinput harus lebih kecil dari jumlah ibank yang tersedia");
						dp.setHit_err(new Integer(1));
					}
				}
			}
			
			dp.setTotal_bayar(total_bayar);
		}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Integer jml = new Integer(0);

		DetailPembayaran dp = (DetailPembayaran) cmd;
		Integer i_flagCC = elionsManager.select_flag_cc(dp.getReg_spaj());
	    if(i_flagCC==1 || i_flagCC==2){
			Account_recur account_recur = elionsManager.select_account_recur(dp.getReg_spaj());
			if(account_recur!=null) dp.setNo_rek(account_recur.getMar_acc_no());
	    }			
	    
		/**
		 * @author HEMILDA
		 * kalau sudah aksep tidak bisa diubah lagi
		 */
		String lspd_id = bacManager.selectLspdId(dp.getReg_spaj());
//		Integer status_polis = this.elionsManager.selectPositionSpaj(dp.getReg_spaj());
		if(lspd_id.equals("2") || lspd_id.equals("27") || lspd_id.equals("251")){
			List b = this.elionsManager.selectpremike(dp.getReg_spaj());
			jml = new Integer(b.size());
			dp.setStatussubmit("0");
			//JOptionPane.showMessageDialog(null, "dp.getSubmitType(): " +dp.getSubmitType() +" "+(jml.intValue()+1), "PESAN", JOptionPane.ERROR_MESSAGE);
			//JOptionPane.showMessageDialog(null, "dp.getSubmitType() " +dp.getSubmitType(), "PESAN", JOptionPane.ERROR_MESSAGE);
			if(!dp.getNo_trx().isEmpty()){
				List mstDrekBasedNoTrx = uwManager.selectMstDrekBasedNoTrx(dp.getNo_trx());
				Map viewMstDrekDetail = (Map) mstDrekBasedNoTrx.get(0);
				dp.setAcc_no((String) viewMstDrekDetail.get("NOREK_AJS"));
				dp.setJenis_kredit((String) viewMstDrekDetail.get("JENIS"));
				if(viewMstDrekDetail.get("NO_PRE") != null){
					dp.setNo_pre((String) viewMstDrekDetail.get("NO_PRE"));
				}
				
				BigDecimal flagTgGb = (BigDecimal) viewMstDrekDetail.get("FLAG_TUNGGAL_GABUNGAN");
				if(flagTgGb == null || flagTgGb.intValue() == 0){
					dp.setTipe("Tunggal");
				}else if(flagTgGb.intValue() == 1){
					dp.setTipe("Gabungan");
				}
			}
				
	        User currentUser = (User) request.getSession().getAttribute("currentUser");
			dp.setLus_id(currentUser.getLus_id());
			if(dp.getSubmitType().equalsIgnoreCase("insert")){
				dp.setKe(new Integer(jml.intValue()+1));
				if(dp.getHit_err().intValue()==0){
					dp.setSubmitType("");
				}
				String tempTahunPremiKe = dp.getJenis();
				if(tempTahunPremiKe==null || "".equals(tempTahunPremiKe)){
					tempTahunPremiKe = "1@1";
				}
				String tahunKe = tempTahunPremiKe.substring(0,tempTahunPremiKe.indexOf("@"));
				String premiKe = tempTahunPremiKe.substring(tempTahunPremiKe.indexOf("@")+1, tempTahunPremiKe.length());
				this.elionsManager.insertmst_deposit(dp,tahunKe,premiKe,i_flagCC);
				dp.setStatussubmit("1");
				
			}else if (dp.getSubmitType().equalsIgnoreCase("edit")){
				Integer flag_tu = ServletRequestUtils.getIntParameter(request, "flag_tu", 0);
				if(flag_tu == 0) flag_tu = null;
				dp.setMsdp_flag_topup(flag_tu);
				
				if(dp.getHit_err().intValue()==0){
					dp.setSubmitType("");
				}
				String tempTahunPremiKe = dp.getJenis();
				if(tempTahunPremiKe==null || "".equals(tempTahunPremiKe)){
					tempTahunPremiKe = "1@1";
				}
				String tahunKe = tempTahunPremiKe.substring(0,tempTahunPremiKe.indexOf("@"));
				String premiKe = tempTahunPremiKe.substring(tempTahunPremiKe.indexOf("@")+1, tempTahunPremiKe.length());
				this.elionsManager.updatemst_deposit(dp,tahunKe,premiKe);
				dp.setStatussubmit("2");
				
			}else if (dp.getSubmitType().equalsIgnoreCase("delete")){
				if(dp.getNo_ke()!=null){
//					if(dp.getNo_ke().intValue() == 1){
//						err.rejectValue("no_ke", "","Titipan Premi ke 1 hanya bisa diedit, tidak bisa didelete");
//					}else{
						String tempTahunPremiKe = dp.getJenis();
						if(tempTahunPremiKe==null || "".equals(tempTahunPremiKe)){
							tempTahunPremiKe = "1@1";
						}
						String tahunKe = tempTahunPremiKe.substring(0,tempTahunPremiKe.indexOf("@"));
						String premiKe = tempTahunPremiKe.substring(tempTahunPremiKe.indexOf("@")+1, tempTahunPremiKe.length());
						this.elionsManager.delete_mst_deposit_premium(dp.getReg_spaj(), Integer.toString(dp.getNo_ke().intValue()), dp, tahunKe, premiKe);
						dp.setSubmitType("");
						dp = new DetailPembayaran();
						dp.setStatussubmit("3");
//					}
				}
			}
		}else{
			err.rejectValue("reg_spaj","","Posisi SPAJ sudah tidak pada proses Titipan Premi!");
		}
		return new ModelAndView("bac/titipan_premi", err.getModel()).addAllObjects(this.referenceData(request));
	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception{
		return new ModelAndView("common/duplicate");
	}

}