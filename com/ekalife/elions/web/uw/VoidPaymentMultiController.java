package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.DepositPremium;
import com.ekalife.elions.model.Payment;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.VoidPayment;
import com.ekalife.utils.AngkaTerbilang;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentMultiController;

public class VoidPaymentMultiController extends ParentMultiController {
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
		binder.registerCustomEditor(BigDecimal.class, null, new CustomNumberEditor( BigDecimal.class, new DecimalFormat("###,##0.00") , true ));
	}	
	
	public ModelAndView main_voidpayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		map.put("nomor","");
		return new ModelAndView("uw/main_voidpayment", map);
	}
	
	public ModelAndView cari_voidpayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		List lsCariPayment=new ArrayList();
		String nomor = ServletRequestUtils.getStringParameter(request, "nomor", "");
		String flag= ServletRequestUtils.getStringParameter(request, "flag", "");
		Integer tipe=ServletRequestUtils.getIntParameter(request, "tipe",1);
		if(flag.equals("1")){
			lsCariPayment=elionsManager.selectMstPolicyByCode(nomor, tipe);
		}
		List<Map> tampung = lsCariPayment;
		if(tampung.size()!=0){
			for(int i=0;i< tampung.size();i++){
				Map hasilMap = tampung.get(i);
				String spaj_format = (String) hasilMap.get("SPAJ_FORMATTED");
				String polis_format = (String) hasilMap.get("POLICY_FORMATTED");
				String fusion = spaj_format + "~" + polis_format;
				map.put("fusion", fusion);
			}
		}
		
		map.put("lsCariPayment", lsCariPayment);
		map.put("nomor", nomor);
		map.put("tipe", tipe);
		return new ModelAndView("uw/cari_voidpayment", map);
	}
	
	public ModelAndView proses_voidpayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		List<String> err = new ArrayList<String>();
		String nomor = ServletRequestUtils.getStringParameter(request, "nomor", "");
		String pembayaran = ServletRequestUtils.getStringParameter(request, "pembayaran", "");
		HttpSession session = request.getSession();
		Date sysdate = elionsManager.selectSysdateSimple();
		SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
		User currentUser = (User) session.getAttribute("currentUser");
		String flag = ServletRequestUtils.getStringParameter(request, "flag", "");
		String spaj=null,nopolis=null, payDate=null;
		Payment payment = new Payment();
		DepositPremium dPremium = new DepositPremium();
		
		if(! nomor.equals("")){
			int pos=nomor.indexOf('~');
			spaj=nomor.substring(0,pos);
			nopolis=nomor.substring(pos+1,nomor.length());
			if(nopolis.equals("null")){
				nopolis = "-";
			}
			spaj = spaj.replace(".", "");
		}
		
		if(!pembayaran.equals("")){
			payment = uwManager.selectMstPayment(pembayaran);
			payDate = ServletRequestUtils.getStringParameter(request, "payDate", defaultDateFormat.format(FormatDate.add(payment.getMspa_pay_date(), Calendar.DATE, 0)));
			if(payment.getLku_symbol().equals("Rp.")){
				map.put("urutan", "ada ni");
			}
		}else {
			//bagian ini dibuat apabila belum sampai tahap payment (baru DP), maka cek ke mst_deposit_premium
			payment = uwManager.selectMstDepositPremium(spaj);
			payDate = ServletRequestUtils.getStringParameter(request, "payDate", defaultDateFormat.format(FormatDate.add(payment.getMspa_pay_date(), Calendar.DATE, 0)));
			if(payment.getLku_symbol().equals("Rp.")){
				map.put("urutan", "ada ni");
			}
		}
		
		
		
//		proses sending Email
		if(flag.equals("1")){
			String jenis1 = ServletRequestUtils.getStringParameter(request, "jenis1", "");
			String jenis2 = ServletRequestUtils.getStringParameter(request, "jenis2", "");
			String jenis3 = ServletRequestUtils.getStringParameter(request, "jenis3", "");
			String account = ServletRequestUtils.getStringParameter(request, "account", "");
			payDate = ServletRequestUtils.getStringParameter(request, "payDate", defaultDateFormat.format(FormatDate.add(sysdate, Calendar.DATE, 0)));
			String kurs = ServletRequestUtils.getStringParameter(request, "kurs", "");
			Double jumlah2 = ServletRequestUtils.getDoubleParameter(request, "jumlah", 0);
			String jumlah = ServletRequestUtils.getStringParameter(request, "jumlah", "");
			nopolis = nopolis.replace(".", "");
			String lku_id = null, lku_symbol = null;
			String keterangan = null;
			int jn_revisi = 0;
			if(! kurs.equals("")){
				int a=kurs.indexOf('~');
				lku_id=kurs.substring(0,a);
				lku_symbol=kurs.substring(a+1,kurs.length());
				
			}
			
//			if(jenis1.equals("true"))jn_revisi = 1;
//			if(jenis2.equals("true"))jn_revisi = 2;
//			if(jenis3.equals("true"))jn_revisi = 3;
			
//			keterangan jn_revisi
//			1 = NO ACCOUNT
//			2 = MATA UANG
//			3 = JUMLAH PREMI
//			4 = NO ACCOUNT, MATA UANG
//			5 = NO ACCOUNT, JUMLAH PREMI
//			6 = MATA UANG, JUMLAH PREMI
//			7 = NO ACCOUNT, MATA UANG, JUMLAH PREMI
			
			if(jenis1.equals("true")){
				keterangan = "NO ACCOUNT";
				jn_revisi = 1;
				if(jenis2.equals("true")){
					jn_revisi = 4;
					if(jenis3.equals("true")){
						jn_revisi = 7;
					}
				}else if(jenis3.equals("true")){
					jn_revisi = 5;
				}
			}
			
			if(jenis2.equals("true")){
				if(keterangan!=null){
					keterangan = keterangan + ", " + "MATA UANG";
				}else {
					keterangan = "MATA UANG";
					jn_revisi = 2;
				}
				
				if(jenis3.equals("true")){
					if(jenis1.equals("false")){
						jn_revisi = 6;
					}
				}
			}
			if(jenis3.equals("true")){
				if(keterangan!=null){
					keterangan = keterangan + " dan " + "JUMLAH PREMI";
				}else {
					keterangan = "JUMLAH PREMI";
					jn_revisi = 3;
				}
			}
			
			if( (!jenis1.equals("false") || !jenis2.equals("false") || !jenis3.equals("false") ) && !account.equals("") && !kurs.equals("") && !jumlah.equals("") ){
				VoidPayment voidPayment = new VoidPayment();
				voidPayment.setMvp_payment_id(pembayaran);
				voidPayment.setReg_spaj(spaj);
				voidPayment.setMspo_policy_no(nopolis);
				voidPayment.setLku_id(lku_id);
				voidPayment.setMsdp_number(payment.getMsdp_number());
				voidPayment.setMvp_pay_date(formatDate.parse(payDate));
				voidPayment.setMvp_payment(jumlah2);
				voidPayment.setInput_date_uw(sysdate);
				voidPayment.setLus_id(Integer.parseInt(currentUser.getLus_id()));
				voidPayment.setMvp_no_acc(account);
				voidPayment.setMvp_no_pre(payment.getMspa_no_pre());
				voidPayment.setMvp_no_voucher(payment.getMspa_no_voucher());
				voidPayment.setFlag(1);
				voidPayment.setJn_revisi(jn_revisi);
				
				String NamaPP = elionsManager.selectPolicyHolderNameBySpaj(spaj);
				
				if(payment.getMspa_no_pre() == null){
					payment.setMspa_no_pre("-");
				}
				
				if(payment.getMspa_no_voucher() == null){
					payment.setMspa_no_voucher("-");
				}
				elionsManager.insertMstVoidPayment(voidPayment);
				
				//email ini untuk data ekatest
//				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
//			    		new String[] {"Deddy@sinarmasmsiglife.co.id"}, new String[]{"Deddy@sinarmasmsiglife.co.id", currentUser.getEmail()}, 
//			    		null, "VOID PAYMENT", "Kepada Yth.<br/> <b>Pihak Terkait</b> <br/>Di Tempat <br/> <br/>Mohon divoid inputan titipan premi pertama dari SPAJ no : " +spaj+
//			    		"(Pemegang Polis "+NamaPP+" ) Karena adanya kesalahan input : "+ keterangan +"<br/>No. Pre : " +payment.getMspa_no_pre()+
//			    		" <br/>No Voucher : "+payment.getMspa_no_voucher()+" <br/><br/>Inputan yang benar sebagai berikut : <br/>No.Account : "+account+
//			    		"<br/>Tgl Bayar/RK :"+payDate+"<br/>Jumlah : "+lku_symbol+" "+AngkaTerbilang.dot(jumlah)+
//			    		"<br/>User UW yang proses payment : "+currentUser.getName()+".", null);
				
				//email ini untuk data asli
				email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
			    		new String[] {"Aprina@sinarmasmsiglife.co.id", "vonny_t@sinarmasmsiglife.co.id", "Sadar@sinarmasmsiglife.co.id", "julina.hasan@sinarmasmsiglife.co.id", "Bonardo@sinarmasmsiglife.co.id", currentUser.getEmail()}, new String[]{"inge@sinarmasmsiglife.co.id","asriwulan@sinarmasmsiglife.co.id","Deddy@sinarmasmsiglife.co.id" }, 
			    		null, "VOID PAYMENT", "Kepada Yth.<br/> <b>Pihak Terkait</b> <br/>Di Tempat <br/> <br/>Mohon divoid inputan titipan premi pertama dari SPAJ no : " +spaj+
			    		"(Pemegang Polis "+NamaPP+" ) Karena adanya kesalahan input : "+ keterangan +"<br/>No. Pre : " +payment.getMspa_no_pre()+
			    		" <br/>No Voucher : "+payment.getMspa_no_voucher()+" <br/><br/>Inputan yang benar sebagai berikut : <br/>No.Account : "+account+
			    		"<br/>Tgl Bayar/RK :"+payDate+"<br/>Jumlah : "+lku_symbol+" "+AngkaTerbilang.dot(jumlah)+
			    		"<br/>User UW yang proses payment : "+currentUser.getName()+".", null);
				
				//map.put("jenis", jenis);
				map.put("account", account);
				map.put("kurs", kurs);
				map.put("jumlah", jumlah);
				map.put("informasi", "informasi");
			
			}else {
				err.add("Semua keterangan Void Payment harus diisi.");
				map.put("errors", err);
			}
			
			
		}
		
		
		List<DropDown> lsKurs = elionsManager.selectLstKurs();
		
		map.put("payDate", payDate);
		map.put("lsKurs",lsKurs);
		map.put("proses", "hore");
		map.put("nomor", nomor);
		map.put("pembayaran", pembayaran);
		map.put("spaj", spaj);
		map.put("nopolis", nopolis);
		map.put("payment", payment);
		
		
		return new ModelAndView("uw/main_voidpayment", map);
	}
	
	
}