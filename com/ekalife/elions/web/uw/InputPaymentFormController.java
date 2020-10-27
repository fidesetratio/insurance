/**
 * 
 */
package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.DrekDet;
import com.ekalife.elions.model.Linkdetail;
import com.ekalife.elions.model.Payment;
import com.ekalife.elions.model.TopUp;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author Yusuf
 * Window input payment new business dan topup new business
 */
public class InputPaymentFormController extends ParentFormController {

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}	

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception{
		return new ModelAndView("common/duplicate");
	}	

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Integer tahun_ke = ServletRequestUtils.getIntParameter(request, "tahun_ke", -1);
		Integer premi_ke = ServletRequestUtils.getIntParameter(request, "premi_ke", -1);
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");

		ArrayList<String> initialErrors = new ArrayList<String>();
		TopUp topup = new TopUp();
		Date sysDate = elionsManager.selectSysdate();
		String businessId = FormatString.rpad("0", uwManager.selectBusinessId(spaj), 3);
		
		if(tahun_ke==-1 || premi_ke==-1 || spaj.equals("")) {
			initialErrors.add("Silahkan pilih nomor SPAJ terlebih dahulu");
			topup.setInitialErrors(initialErrors);
			return topup;
		}
		
		if(tahun_ke==1 && premi_ke==1){
			topup.setJudul("New Business");
		} else if(tahun_ke==1 && premi_ke>=2){
			topup.setJudul("Top Up");
		}
		
		List billInfo = this.elionsManager.selectBillingInformation(spaj, tahun_ke, premi_ke);
		Map x = (HashMap) billInfo.get(0);
		Date begdate = (Date) x.get("MSBI_BEG_DATE");
		
		topup.setTahun_ke(new Integer(tahun_ke));
		topup.setPremi_ke(new Integer(premi_ke));
		topup.setReg_spaj(spaj);
		topup.setMspa_date_book(begdate);
		topup.setPay_date(begdate);
		
		request.setAttribute("sysDate", defaultDateFormat.format(sysDate));
		
		//Validasi posisi spaj
		if(this.elionsManager.validationPositionSPAJ(spaj)!=4) initialErrors.add("Harap cek posisi dokumen");
		
		//Validasi tambahan untuk topup  
		if(premi_ke>=2){
			if(products.stableLink(Integer.valueOf(businessId).toString())) {
				List<Map> stable = elionsManager.selectInfoStableLink(spaj);
				if(stable.size()<2) initialErrors.add("Tidak ada pembayaran Topup");
			}else {
				if(!products.unitLink(businessId)) {
					initialErrors.add("Produk bersangkutan bukan produk Unit Link");
				}
				if(this.elionsManager.validationTopupBerkala(spaj)<1) {
					initialErrors.add("Tidak ada pembayaran Topup");
				}
			}
		}

		topup.setInitialErrors(initialErrors);
		return topup;
	}

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors errors) throws Exception {
		String actionType = ServletRequestUtils.getStringParameter(request, "actionType", "");
		Map refData = new HashMap();
		TopUp topup = (TopUp) cmd;
		Boolean initError = false;
		if(topup.getInitialErrors()!=null) {
			if(topup.getInitialErrors().size()>0) initError = true;
		}
			
		if(!initError) {
			String payment_id = ServletRequestUtils.getStringParameter(request, "payment_id", "");
			
			List billInfo = new ArrayList();
			billInfo = this.elionsManager.selectBillingInformation(topup.getReg_spaj(), topup.getTahun_ke(), topup.getPremi_ke());

			Map tmp = (HashMap) billInfo.get(0);
			refData.put("billInfo", billInfo);
			refData.put("lca_polis", (String) tmp.get("LCA_ID"));
			
			List<Payment> paymentInfo = this.elionsManager.selectPaymentCount(topup.getReg_spaj(), topup.getTahun_ke(), topup.getPremi_ke());
			
			//Add or save
			if(request.getParameter("add")!=null || request.getParameter("save")!=null){
				if(request.getParameter("add")!=null){
					topup.setActionTypeForDrekDet("insert");
				}
				Payment p = new Payment();
				p.setKe(paymentInfo.size());
				p.setTipe("Penambahan");
				paymentInfo.add(p);
				topup.setLku_id((String) tmp.get("LKU_ID"));
				topup.setBill_lku_id((String) tmp.get("LKU_ID"));
				topup.setMspa_payment(new Double(((BigDecimal) tmp.get("SISA")).doubleValue()));
				String lsbs = uwManager.selectBusinessId(topup.getReg_spaj());
				
				if(elionsManager.selectIsInputanBank(topup.getReg_spaj()) == 2){ //BANK SINARMAS
					Date begdate = (Date) tmp.get("MSBI_BEG_DATE");
					topup.setMspa_date_book(begdate);
					topup.setPay_date(begdate);
					
					if(products.stableLink(lsbs)){ //simas stabil link
						if(topup.getLku_id().equals("01")){
							topup.setLsrek_id("261"); //rup
							topup.setKode("38299");
						}else{
							topup.setLsrek_id("266"); //dollar
							topup.setKode("38288");
						}
					}else if(products.powerSave(lsbs)){ //simas prima
						if(topup.getLku_id().equals("01")){
							topup.setLsrek_id("161"); //rup
							topup.setKode("91814");
						}else{
							topup.setLsrek_id("160"); //dollar
							topup.setKode("91822");
						}
					}
					
				}else if(elionsManager.selectIsInputanBank(topup.getReg_spaj()) == 3){ //SINARMAS SEKURITAS
					Date begdate = (Date) tmp.get("MSBI_BEG_DATE");
					topup.setMspa_date_book(begdate);
					topup.setPay_date(begdate);
			
					if(products.stableLink(lsbs)){ //Danamas stable link
						if(topup.getLku_id().equals("01")){
							topup.setLsrek_id("223"); //rupiah
							topup.setKode("4435");
						}else{
							topup.setLsrek_id("278"); //dollar
							topup.setKode("0443");
						}
					}else if(products.powerSave(lsbs)){//Danamas prima
						/*
						 * (24/02/2012) Req Hayatin via Helpdesk #23812, rubah rek danamas prima
						 * IDR -> 227 -> 00021 84548, USD -> 98 -> 00000.29041
						 */
						if(topup.getLku_id().equals("01")){
							//topup.setLsrek_id("214"); //IDR
							//topup.setKode("5777");
							topup.setLsrek_id("227"); //IDR
							topup.setKode("84548");
						}else{
							//topup.setLsrek_id("216"); //USD
							//topup.setKode("6555");
							topup.setLsrek_id("98"); //USD
							topup.setKode("29041");
						}
					}
					
				}else if(elionsManager.selectIsInputanBank(topup.getReg_spaj()) == 16){ //BANK SINARMAS SYARIAH
					if(products.powerSave(lsbs)){ //POWERSAVE BSM SYARIAH
						if(topup.getLku_id().equals("01")){
							topup.setLsrek_id("293"); //rup
							topup.setKode("03592"); //Req dev
							//topup.setKode("75388");
						} 
					}
				}else{
					Integer lsdbs = uwManager.selectBusinessNumber(topup.getReg_spaj());
					if(lsbs.equals("187") && lsdbs==5){
						topup.setKode("4702");
					}
				}
				topup.setLsjb_id(1);
				
			}else if(!payment_id.equals("")){
				TopUp temp = elionsManager.selectTopUp(
						topup.getReg_spaj(), 
						payment_id, 
						topup.getTahun_ke(), 
						topup.getPremi_ke());
				
				temp.setActionTypeForDrekDet("update");
				temp = setTempForTopUp(temp,topup.getReg_spaj(), payment_id);
				PropertyUtils.copyProperties(topup, temp); //(Dest, Source)
	
			}else if(paymentInfo.size()>0) {
				TopUp temp = elionsManager.selectTopUp(
						topup.getReg_spaj(), 
						paymentInfo.get(0).getMspa_payment_id(), 
						topup.getTahun_ke(), 
						topup.getPremi_ke());
				temp = setTempForTopUp(temp,topup.getReg_spaj(), paymentInfo.get(0).getMspa_payment_id());
				temp.setActionTypeForDrekDet("update");
				PropertyUtils.copyProperties(topup, temp); // (Dest, Source)
				
			}else{
				topup.setActionTypeForDrekDet("insert");
				String lsbs = uwManager.selectBusinessId(topup.getReg_spaj());
				Integer lsdbs = uwManager.selectBusinessNumber(topup.getReg_spaj());
				if(lsbs.equals("187") && lsdbs==5){
					topup.setKode("4702");
				}
				topup.setLsjb_id(1);
			}
			
			String jenis;
			List<Linkdetail> jenisTransaksiList = new ArrayList<Linkdetail>(); 
			Integer isUlink = this.uwManager.isUlinkBasedSpajNo( topup.getReg_spaj() );
			if(isUlink != null && isUlink > 0){
				jenis = "";
				jenisTransaksiList = this.uwManager.uLinkDescrAndDetail( topup.getReg_spaj() );
			}else{
				Integer isSlink = this.uwManager.isSlinkBasedSpajNo( topup.getReg_spaj() );
				if(isSlink != null && isSlink > 0){
					jenis = "";
					jenisTransaksiList = this.uwManager.sLinkDescrAndDetail( topup.getReg_spaj() );
				}else{
					jenis = "none";
					jenisTransaksiList = null;
				}
			}
			if(jenisTransaksiList!= null && jenisTransaksiList.size()>0){
				for(int i=0; i<jenisTransaksiList.size(); i++){
					if("Alokasi Investasi".equals(jenisTransaksiList.get(i).getDescr())){
						jenisTransaksiList.get(i).setDescr("Premi Pokok");
					}
				}
			}
				
			if("edit".equals(actionType)){
				topup.setTanggalRkDisabled(1);
			}else{
				topup.setTanggalRkDisabled(0);
			}
			
			refData.put("paymentInfo", paymentInfo);
	
			//dropdowns
			refData.put("jenisTransaksiList", jenisTransaksiList);
			refData.put("jenis", jenis);
			refData.put("listKurs", this.elionsManager.selectKurs());
			refData.put("listPayType", this.elionsManager.selectPayType());
			refData.put("listBankPusat", this.uwManager.selectBankPusat());
			refData.put("listMerchant", bacManager.selectLstMerchantFee(null));
		}
		
		refData.put("blankon", elionsManager.selectBlanko(topup.getReg_spaj()));
		return refData;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		TopUp topup = (TopUp) cmd;
		
		List<Linkdetail> jenisTransaksiList = new ArrayList<Linkdetail>(); 
		Integer isUlink = this.uwManager.isUlinkBasedSpajNo( topup.getReg_spaj() );
		if(isUlink != null && isUlink > 0){
			jenisTransaksiList = this.uwManager.uLinkDescrAndDetail(topup.getReg_spaj());
		}else{
			Integer isSlink = this.uwManager.isSlinkBasedSpajNo( topup.getReg_spaj() );
			if(isSlink != null && isSlink > 0){
				jenisTransaksiList = this.uwManager.sLinkDescrAndDetail(topup.getReg_spaj());
			}else{
				jenisTransaksiList = null;
			}
		}
		
		if(jenisTransaksiList != null && jenisTransaksiList.size() > 0){
			if(topup.getJenis()==null || "".equals(topup.getJenis())){
				err.reject("payment.jenisTrxEmpty");
			}
		}

		if(this.elionsManager.validationIsTitipanPremi(topup.getMspa_payment_id())) {
			err.reject("payment.unableEditTitipanPremi");
		}
		
		if(topup.getMspa_date_book()!=null){
			if(topup.getMspa_date_book().after(new Date())){
				err.reject("payment.invalidRK");
			}
		}
		
		if(!topup.getLku_id().equals("01")){
			if(this.elionsManager.validationDailyCurrency(topup.getLku_id(), topup.getMspa_date_book())==0){
				err.reject("payment.noDailyCurrency", new Object[] { defaultDateFormat.format(topup.getMspa_date_book()) }, "Kurs tanggal {0} tidak ada, harap hubungi CSF");
			}
		}
		
		if(topup.getMspa_payment()==null){
			err.reject("payment.noPayment");
		}else if(topup.getMspa_payment().doubleValue()==0){
			err.reject("payment.noPayment");
		}
		
		if(topup.getLsjb_id()==null){
			err.reject("payment.noPaymentMethod");
		}
		
		if(!topup.getLsrek_id().equals("0")){
			if( topup.getNo_trx()== null || "".equals(topup.getNo_trx())){
				err.reject("payment.noIbank");
			}else{
				Double sisa_saldo;
				if("update".equals(topup.getActionTypeForDrekDet())){
					sisa_saldo = uwManager.selectCheckTotalUsedMstDrek(topup.getNo_trx(), topup.getMspa_payment_id(), null, null);
				}else{
					sisa_saldo = uwManager.selectCheckTotalUsedMstDrek(topup.getNo_trx(), null, null, null);
				}
				
				if(topup.getMspa_payment()>sisa_saldo){
					err.reject("payment.sisaIbank");
				}
			}
		}
		
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        User currentUser = (User) request.getSession().getAttribute("currentUser");
		TopUp topup = (TopUp) command;
        
        Integer i_flagCC = elionsManager.select_flag_cc(topup.getReg_spaj());
        if(i_flagCC==1 || i_flagCC==2){
	        Account_recur account_recur = elionsManager.select_account_recur(topup.getReg_spaj());
			if(account_recur!=null) topup.setMspa_no_rek(account_recur.getMar_acc_no());
        }
        topup.setLca_polis(request.getParameter("lca_polis").toString());

        elionsManager.savePayment(topup, 9, currentUser, i_flagCC);

        return new ModelAndView(new RedirectView(request.getContextPath()+"/uw/inputpayment.htm?tahun_ke="+topup.getTahun_ke()
        		+"&premi_ke="+topup.getPremi_ke()
        		+"&spaj="+topup.getReg_spaj()));
	}
	
	protected TopUp setTempForTopUp( TopUp temp, String spaj, String payment_id ){
		List<DrekDet> mstDrekDetBasedSpaj = uwManager.selectMstDrekDet( null, spaj, null, null ); 
		String noTrx = "";
		
		if(mstDrekDetBasedSpaj != null && mstDrekDetBasedSpaj.size() > 0)
		{
			for(int i=0; i<mstDrekDetBasedSpaj.size(); i++){
				if( temp.getMspa_desc() != null && mstDrekDetBasedSpaj.get(i).getNo_trx() != null ){
					if(temp.getMspa_desc().contains(mstDrekDetBasedSpaj.get(i).getNo_trx())){
						noTrx = mstDrekDetBasedSpaj.get(i).getNo_trx();
					}
				}
			}
		}
		
		if(noTrx != null && !"".equals(noTrx)){
			temp.setNo_trx(noTrx);
			List mstDrekBasedNoTrx = uwManager.selectMstDrekBasedNoTrx(noTrx);
			Map viewMstDrekDetail= new HashMap();
			if(mstDrekBasedNoTrx != null){
				viewMstDrekDetail=(Map) mstDrekBasedNoTrx.get(0);
				if(viewMstDrekDetail.get("NO_PRE")!=null){
					temp.setNo_pre((String) viewMstDrekDetail.get("NO_PRE"));
				}
				
			}
			if(viewMstDrekDetail != null && viewMstDrekDetail.size() > 0){
				Object flagTunggalGabungan = viewMstDrekDetail.get("FLAG_TUNGGAL_GABUNGAN");
				if(flagTunggalGabungan != null && !"".equals(flagTunggalGabungan)){
					if("0".equals( flagTunggalGabungan.toString() )){
						temp.setTipe("Tunggal");
					}else if("1".equals( flagTunggalGabungan.toString() )){
						temp.setTipe("Gabungan");
					}else{
						temp.setTipe("");
					}
				}else{
					temp.setTipe("");
				}
				
			}else{
				temp.setTipe("");
			}
			
			List<DrekDet> mstDrekDetBasedNoTrx = uwManager.selectMstDrekDet( noTrx, null, payment_id, null );
			if( mstDrekDetBasedNoTrx != null && mstDrekDetBasedNoTrx.size() > 0 ){
				temp.setJenis( mstDrekDetBasedNoTrx.get(0).getTahun_ke()+"@"+mstDrekDetBasedNoTrx.get(0).getPremi_ke());	
			}
		}
		return temp;
	}
	
}