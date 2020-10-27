/**
 * @author  : Ferry Harlim
 * @created : Apr 3, 2007 
 */
package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Ttp;
import com.ekalife.elions.model.User;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TransferTandaTerimaPolisFormController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		Ttp ttp=new Ttp();
		String spaj=request.getParameter("spaj");
		ttp.setSpaj(spaj);
		Map tertanggung= this.elionsManager.selectTertanggung(spaj);
		ttp.setLcaId((String)tertanggung.get("LCA_ID"));
		ttp.setFlagKomisi((Integer)tertanggung.get("MSTE_FLAG_KOMISI"));
		ttp.setMsagId(this.uwManager.selectAgenFromSpaj(spaj));
		ttp.setTgl(this.elionsManager.selectSysdate());
		ttp.setFax(0); 
		ttp.setSimpan(true);
		ttp.setProses(0);
		Map mPosisi=elionsManager.selectF_check_posisi(spaj);
		Integer lspdId=(Integer)mPosisi.get("LSPD_ID");
		String lspdPos=(String)mPosisi.get("LSPD_POSITION");

		List lCekRekAgen=elionsManager.cekRekAgen(spaj);

		Integer jmlY=lCekRekAgen.size();
		String agen="";
		for(int i = 0; i<jmlY; i++){
			Map map=(HashMap)lCekRekAgen.get(i);
			String msag_tabungan=(String)map.get("MSAG_TABUNGAN");
			String lev_comm=(String)map.get("LEV_COMM");
			Integer lbn_id= (Integer)map.get("LBN_ID");
			
			if (ttp.getLcaId().equals("09")||(ttp.getLcaId().equals(" "))|| lev_comm== null){
//				logger.info("boleh kok");
				
			}else{
	
				if (lev_comm.equals("")){
//					logger.info("syah.. syahhhh.. saja.....");
				}else{
					if(msag_tabungan == null)msag_tabungan = "";
					if(msag_tabungan.trim().equals("")){
						agen+=(String)map.get("AGEN")+", ";
					}
					if(msag_tabungan.trim().equals("0000000000")){
						agen+=(String)map.get("AGEN")+", ";
					}
					if(msag_tabungan.trim().equals("0000000000")|| lev_comm==null){
						agen+=(String)map.get("AGEN")+", ";
					}
					if (lbn_id==null|| lbn_id.equals(0)){
						agen+=(String)map.get("AGEN")+", ";
					}
				}
			}
			
		}
		
		if(lspdId!= 7) {
			ttp.setPosisi("Posisi Polis ada di "+lspdPos);
		}
		String lsbs_id = uwManager.selectBusinessId(spaj);
		if( products.unitLink(lsbs_id)){
			Double totalPremiBilling = bacManager.selectTotalPremiNewBusiness(spaj);
			if(totalPremiBilling.intValue()>=50000000){
				ttp.setFlagWC(1);
			}
		}
		return ttp;
	}
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Ttp ttp=(Ttp)cmd;
		String errorRekening = elionsManager.cekRekAgen2(ttp.getSpaj());
		Date tanggal = elionsManager.selectSysdate();
		String spaj= ttp.getSpaj();
		
//		CEK REKENING AGEN
		boolean emailCabangValid = false;
		boolean emailAgenValid = false;
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String emailCabang = uwManager.selectEmailCabangFromSpaj(ttp.getSpaj());
		
		Map infoAgen = elionsManager.selectEmailAgen(ttp.getSpaj());
		String emailAgen = (String) infoAgen.get("MSPE_EMAIL");
		String msag_id = (String) infoAgen.get("MSAG_ID");
		
		Map infopemegang =elionsManager.selectInfoPemegang(ttp.getSpaj());
		String pemegang=(String)infopemegang.get("MCL_FIRST");
		String product= (String)infopemegang.get("LSBS_NAME");
		String no_polis= (String)infopemegang.get("MSPO_POLICY_NO");
		String tgl_kirim= (String)infopemegang.get("MSTE_TGL_KIRIM_POLIS");
		String lca_id= (String)infopemegang.get("LCA_ID");
		
		String alamat = "";
		String cc = "";
		String bcc="";
		
		//untuk welcome Call jika flag = 2 berarti berhasil, 3 gagal. dengan ljj_id = 63
		String flag_validasi = "";
		
		//validasi email agen
		if(emailAgen!=null) {
			if(!emailAgen.trim().equals("")) {
				if(emailAgen.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
					emailAgenValid = true;
					cc =emailAgen;
					
				}
			}
		}
		//validasi email cabang
		if(emailCabang!=null) {
			if(!emailCabang.trim().equals("")) {
				if(emailCabang.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
					emailCabangValid = true;
					alamat= emailCabang;
					
				}
			}
		}
		String errorsertifikat = elionsManager.cekSetifikatAgen(ttp.getSpaj());
		String errorKdRek = elionsManager.cekKdBankRekAgen2(ttp.getSpaj());
		
		Integer premike = uwManager.selectpremiKe(ttp.getSpaj());
		
		Integer cekTglTerima= elionsManager.selectCountTerimaTtp(ttp.getSpaj());
		
		//pengecekan validasi data welcome call
		List <Map> dataWCSuccess = bacManager.selectWelcomeCallSuccess(ttp.getSpaj(),1);
		if(!dataWCSuccess.isEmpty()){
			Map dataInbox_uw = dataWCSuccess.get(0);
			flag_validasi =  dataInbox_uw.get("FLAG_VALIDASI").toString();
		}
		
		if (cekTglTerima.equals(0)){
			err.reject( "", "TTP  tidak dapat diproses. Harap input dahulu  Tgl.Terima TTP.");
		}else if (flag_validasi.equals("1") || flag_validasi.equals("3")){//validasi welcome call - tidak bisa melakukan proses transfer sebelum welcome call sukses
			err.reject( "", "TTP  tidak dapat diproses. SPAJ belum berhasil divalidasi welcome call  oleh cs.");
		}else{
		
		Integer countWarning= elionsManager.count_ttp_komisigaDiproses(ttp.getSpaj());
		if(lca_id.equals("09")||lca_id.equals("40")){
			logger.info("ok aja");
		}else{
			if((lca_id.equals("42"))||(lca_id.equals("40"))){
				bcc="santi@sinarmasmsiglife.co.id;";
			}else{
				bcc="santi@sinarmasmsiglife.co.id;";
			}
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, tanggal, null,
					true, 
				props.getProperty("admin.ajsjava"), 
				alamat.split(";"), 
				cc.split(";"),
				bcc.split(";"), 
				"Pemberitahuan bahwa TTP telah diterima dan diproses oleh Bagian Underwriting",
				"<table width=100% class=satu>"
				+"<tr><td colspan='3'>TTP untuk nasabah dibawah ini : </td></tr>"
				+"<tr><td colspan='3'>&nbsp;</td></tr>"
				+ "<tr><td width='236'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No. Polis 	  </td><td width='5'>:</td><td width='767'>" + FormatString.nomorPolis(no_polis) + "</td></tr>"
				+ "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nama Pemegang Polis </td><td>:</td><td>" + pemegang + ""+ "</td></tr>" 
				+ "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Produk		  </td><td>:</td><td>" +product + "</td></tr>"
				+ "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Tanggal kirim Polis</td><td>:</td><td>" +tgl_kirim+ "</td></tr>"
				+"<tr><td colspan='3'>&nbsp;</td></tr>"
				+"<tr><td colspan='3'>Telah diterima dan diproses oleh bagian Underwriting.</td></tr>"
				+"<tr><td colspan='3'>&nbsp;</td></tr>"
				+"<tr><td colspan='3'>Terima kasih .</td></tr>"
				+"<tr><td colspan='3'>&nbsp;</td></tr>"
				+"<tr><td colspan='3'>&nbsp;</td></tr>"
				+"<tr><td><img src='cid:myLogo'></td><td colspan='2'>&nbsp;</td></tr>"
				+"<tr><td><font size='2'>"+currentUser.getLus_full_name()+"</font></td><td colspan='2'>&nbsp;</td></tr>"
				+"<tr><td><font size='2'>"+currentUser.getDept()+"</font> </td><td colspan='2'>&nbsp;</td></tr>"
				+"<tr><td><font size='2'>PT Asuransi Jiwa Sinarmas MSIG Tbk. </font></td><td colspan='2'>&nbsp;</td></tr>"
				+"<tr><td><font size='2'>Wisma Eka Jiwa Lt.8 </font></td><td colspan='2'>&nbsp;</td></tr>"
				+"<tr><td><font size='2'>JL. Mangga Dua Raya, Jkt 10730</font> </td><td colspan='2'>&nbsp;</td></tr>"
				+"<tr><td><font size='2'>Telp.+62(021)6257808 </font></td><td colspan='2'>&nbsp;</td></tr>"
				+"<tr><td><font size='2'>Fax. +62(021)6257779</font> </td><td colspan='2'>&nbsp;</td></tr>"
				+"<tr><td colspan='3'><font size='1'>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.</font></td></tr>"
				+"</table>", 
				null, ttp.getSpaj());
		}
		
		List <Map> inbox = uwManager.selectMstInbox(spaj, "0");
		Integer lspd_id = 0;
		if(inbox!=null){
			List <Map> inbox_uw = uwManager.selectMstInbox(spaj, "1");
			List <Map> inbox_prodbaru = uwManager.selectMstInbox(spaj, "3");
			if(!inbox_uw.isEmpty()){
				Map dataInbox_uw = inbox_uw.get(0);
				lspd_id = ((BigDecimal) dataInbox_uw.get("LSPD_ID")).intValue();
			}
			if(!inbox_prodbaru.isEmpty()){
				Map dataInbox_prodbaru = inbox_prodbaru.get(0);
				lspd_id = ((BigDecimal) dataInbox_prodbaru.get("LSPD_ID")).intValue();
			}
			
			if (lspd_id!=201){
				err.reject( "","Transfer Polis tidak bisa dilakukan, karena belum melakukan Transfer dokumen ke Imaging.");
			}
		}
		
		if (cekTglTerima.equals(0)){
			err.reject( "", "TTP  tidak dapat diproses. Harap input dahulu  Tgl.Terima TTP.");
		}
		//TTP 40 hari
		if (ttp.getFlagKomisi().equals(0)&& premike.equals(1)&&(countWarning>0)){
			err.reject( "", "Polis ini tidak dapat diproses karena sudah lewat TTP 40 hari");
//			err.reject( "", "KALO PAKE KOMPUTER ORANG IJIN DLU SAMA YANG PUNYA!!!!");
		}
		
		else{
		if((!errorRekening.equals(""))||(!errorKdRek.equals(""))||(!errorsertifikat.equals(""))){
			//Deddy(1 okt 2014) - Dipindahkan dalam proses Submit.
		}else {
			
//			int isInputanBank = -1;
//			isInputanBank = elionsManager.selectIsInputanBank(spaj);
			
//			Yusuf - 25-09-08 - harus ada validasi checklist
//			if(isInputanBank < 0) {
//				if(!elionsManager.selectValidasiCheckListBySpaj(spaj))
//					err.reject("", "Harap Input CHECKLIST DOKUMEN POLIS Terlebih Dahulu!");
//			}
			
			//Yusuf - cegat semua yang akseptasi khusus, gak boleh lewat dari sini!
			int lssa_id = elionsManager.selectStatusAksep(spaj);
			if(lssa_id != 5) {
				err.reject("", "Status Polis ini Belum AKSEPTASI. Polis yang dapat diproses komisinya adalah polis yang sudah di-AKSEPTASI");
			}
//			ttp.setFlagKomisi(1);//U/te
			if(ttp.getProses()==0){
				ttp.setSimpan(false);
				if(ttp.getFlagKomisi()==0){
					if(ttp.getFax()==1){//fax
						ttp.setProses(7);
						ttp.setSimpan(true);
						err.reject("","Silahkan Terima Ttp dalam bentuk fax.");
					}else{
						ttp.setProses(this.elionsManager.prosesCheckingTtp(ttp,err));
						err.reject("","proses checking ttp.");
					}
				}else{
					ttp.setProses(this.elionsManager.prosesCheckingTtp(ttp,err));
					err.reject("","proses checking ttp.");
				}
			}else{
				ttp.setSimpan(true);
			}
			}
		}}
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Ttp ttp=(Ttp)cmd;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String hsl = this.elionsManager.transferTandaTerimaPolisToKomisiOrFillingNew(ttp,currentUser);
		return new ModelAndView("uw/transfer_ttp", err.getModel()).addObject("hsl", hsl).addObject("submitSuccess", 1).addAllObjects(this.referenceData(request,cmd,err));
	}
	/*
	private Integer prosesCheckingTtp(Ttp ttp) {
		Integer proses=0;
		Integer countComm=this.elionsManager.selectCekKomisi(ttp.getSpaj());
		if ( countComm ==0 ) {//gak ada komisi
			ttp.setInfo("Tidak Punya Komisi, Transfer Ke Filling?");
			proses=1;
		}else{//ada komisi
			List tmp = this.elionsManager.selectAgenCekKomisi(ttp.getMsagId());
			Date ldt_birth = new Date();
			String ls_nama = "";
			Double ldec_kom = new Double(0);
			int li_polis = 0 ;
			if( tmp.size() > 0 ) {
				ldt_birth = (Date) ((Map) tmp.get(tmp.size()-1)).get("MSPE_DATE_BIRTH");
				ls_nama = (String) ((Map) tmp.get(tmp.size()-1)).get("MCL_FIRST");
				ldec_kom = (Double) ((Map) tmp.get(tmp.size()-1)).get("KOMISI");
				li_polis = this.elionsManager.selectJumlahPolisAgen(ls_nama, FormatDate.toString(ldt_birth));
			}
			//worksite langsung transfer ke finance
			String cabang="37,09,42";
			Integer a=cabang.indexOf(ttp.getLcaId());
			if(a>0){
				ttp.setInfo("Kode cabang ("+cabang+")"+ " Yakin Transfer ke Finance?");
				proses=5;
			}else{
				if(ttp.getFlagKomisi()==1 ){//proses transfer balikan dari Agency
					ttp.setInfo("Ada Komisi, Anda Ingin Langsung Transfer ke Finance?");
					proses=2;
				}else if(ttp.getFlagKomisi()==2){//
					ttp.setInfo("Ada Komisi, TTP Asli telah Ada dan Anda Ingin Langsung Transfer ke Finance?");
					proses=3;
				}else if(ldec_kom.doubleValue()<500000 || li_polis>0){
					ttp.setInfo("Agen sudah punya polis atau komisi < 500 rb,\\nYakin Transfer ke Finance?");
					proses=4;
				}else{//transfer ke Proses Checking TTP{
					ttp.setInfo("Agen tidak punya polis atau komisi > 500 rb \\nAnda Ingin Transfer Polis ke Agent Commission(Agency Support)?");
					proses=6;
				}
			}
		
		}
	
		return proses;
	}*/

}