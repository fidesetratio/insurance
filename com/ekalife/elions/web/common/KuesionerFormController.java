package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

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
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Aktivitas;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Kuesioner;
import com.ekalife.elions.model.KuesionerBrand;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.refund.RefundEditFormConst;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentFormController;

public class KuesionerFormController extends ParentFormController {
	
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		KuesionerBrand kbrand = new KuesionerBrand();
		
		List<DropDown> corporateList = new ArrayList<DropDown>();
		corporateList.add(new  DropDown("1", "MasiG"));
		corporateList.add(new  DropDown("2", "Matsu"));
		corporateList.add(new  DropDown("3", "Narmas-G"));
		corporateList.add(new  DropDown("4", "Nasumo"));
		corporateList.add(new  DropDown("5", "Sinmitsu"));
		corporateList.add(new  DropDown("6", "Simamisu"));
		corporateList.add(new  DropDown("7", "Simasigi"));
		corporateList.add(new  DropDown("8", "Simasu"));
		corporateList.add(new  DropDown("9", "Simitsui"));
		corporateList.add(new  DropDown("10", "Sina-G"));
		corporateList.add(new  DropDown("11", "SinarMitsui"));
		corporateList.add(new  DropDown("12", "Sinatsui"));
		
		List<DropDown> emotionalList = new ArrayList<DropDown>();
		emotionalList.add(new  DropDown("1", "I-deal"));
		emotionalList.add(new  DropDown("2", "Maxii"));
		emotionalList.add(new  DropDown("3", "Maxylife"));
		emotionalList.add(new  DropDown("4", "RealSecure"));
		emotionalList.add(new  DropDown("5", "SafetyLiving"));
		emotionalList.add(new  DropDown("6", "SecureLiving"));
		emotionalList.add(new  DropDown("7", "WISHare"));
		emotionalList.add(new  DropDown("8", "Recure*"));
		
		List<DropDown> productList = new ArrayList<DropDown>();
		productList.add(new DropDown("1", "Cert+"));
		productList.add(new DropDown("2", "FLIN"));
		productList.add(new DropDown("3", "pre+"));
		productList.add(new DropDown("4", "SIMM+"));
		productList.add(new DropDown("5", "supp+"));
		productList.add(new DropDown("6", "SimmasPro"));
		productList.add(new DropDown("7", "Sinaras"));
		productList.add(new DropDown("8", "OptiTrust*"));
		productList.add(new DropDown("9", "Akurat"));
		productList.add(new DropDown("10", "Natifs"));
		productList.add(new DropDown("11", "Saives"));
		
		List<DropDown> imageList = new ArrayList<DropDown>();
		imageList.add(new DropDown("1", "Aigo"));
		imageList.add(new DropDown("2", "Ansin"));
		imageList.add(new DropDown("3", "Simasu"));
		imageList.add(new DropDown("4", "Sinari"));
		imageList.add(new DropDown("5", "Sonaeru"));
		imageList.add(new DropDown("6", "Sinji"));
		imageList.add(new DropDown("7", "simyou"));
		imageList.add(new DropDown("8", "Masai"));
		imageList.add(new DropDown("9", "Simasho"));
		imageList.add(new DropDown("10", "Simaii"));
		imageList.add(new DropDown("11", "Naru*"));
		
		
		
		KuesionerBrand temp = uwManager.selectKuesionerBrand(currentUser.getLus_id());
		if( temp != null ){
			kbrand = temp;
		}
		
		kbrand.setCorporateList(corporateList);
		kbrand.setEmotionalList(emotionalList);
		kbrand.setProductList(productList);
		kbrand.setImageList(imageList);
		return kbrand;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		KuesionerBrand kbrand=(KuesionerBrand)cmd;
		if( (kbrand.getMkb_cr_msig_1()== null || ( kbrand.getMkb_cr_msig_1() != null && new Integer("0").equals(kbrand.getMkb_cr_msig_1()))) ||
				(kbrand.getMkb_cr_msig_2()== null || ( kbrand.getMkb_cr_msig_2() != null && new Integer("0").equals(kbrand.getMkb_cr_msig_2()))) ||
				(kbrand.getMkb_cr_msig_3()== null || ( kbrand.getMkb_cr_msig_3() != null && new Integer("0").equals(kbrand.getMkb_cr_msig_3()))) ||
				(kbrand.getMkb_cr_msig_4()== null || ( kbrand.getMkb_cr_msig_4() != null && new Integer("0").equals(kbrand.getMkb_cr_msig_4()))) ||
				(kbrand.getMkb_cr_msig_5()== null || ( kbrand.getMkb_cr_msig_5() != null && new Integer("0").equals(kbrand.getMkb_cr_msig_5()))) 
				){
			err.rejectValue( "mkb_cr_msig_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda mencerimankan 'Sinarmas-MSIG' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_cr_aset_1()== null || ( kbrand.getMkb_cr_aset_1() != null && new Integer("0").equals(kbrand.getMkb_cr_aset_1()))) ||
				(kbrand.getMkb_cr_aset_2()== null || ( kbrand.getMkb_cr_aset_2() != null && new Integer("0").equals(kbrand.getMkb_cr_aset_2()))) ||
				(kbrand.getMkb_cr_aset_3()== null || ( kbrand.getMkb_cr_aset_3() != null && new Integer("0").equals(kbrand.getMkb_cr_aset_3()))) ||
				(kbrand.getMkb_cr_aset_4()== null || ( kbrand.getMkb_cr_aset_4() != null && new Integer("0").equals(kbrand.getMkb_cr_aset_4()))) ||
				(kbrand.getMkb_cr_aset_5()== null || ( kbrand.getMkb_cr_aset_5() != null && new Integer("0").equals(kbrand.getMkb_cr_aset_5()))) 
				){
			err.rejectValue( "mkb_cr_aset_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan 'kekuatan aset' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_cr_pelanggan_1()== null || ( kbrand.getMkb_cr_pelanggan_1() != null && new Integer("0").equals(kbrand.getMkb_cr_pelanggan_1()))) ||
				(kbrand.getMkb_cr_pelanggan_2()== null || ( kbrand.getMkb_cr_pelanggan_2() != null && new Integer("0").equals(kbrand.getMkb_cr_pelanggan_2()))) ||
				(kbrand.getMkb_cr_pelanggan_3()== null || ( kbrand.getMkb_cr_pelanggan_3() != null && new Integer("0").equals(kbrand.getMkb_cr_pelanggan_3()))) ||
				(kbrand.getMkb_cr_pelanggan_4()== null || ( kbrand.getMkb_cr_pelanggan_4() != null && new Integer("0").equals(kbrand.getMkb_cr_pelanggan_4()))) ||
				(kbrand.getMkb_cr_pelanggan_5()== null || ( kbrand.getMkb_cr_pelanggan_5() != null && new Integer("0").equals(kbrand.getMkb_cr_pelanggan_5()))) 
				){
			err.rejectValue( "mkb_cr_pelanggan_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan 'fokus terhadap kebutuhan pelanggan' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_cr_prima_1()== null || ( kbrand.getMkb_cr_prima_1() != null && new Integer("0").equals(kbrand.getMkb_cr_prima_1()))) ||
				(kbrand.getMkb_cr_prima_2()== null || ( kbrand.getMkb_cr_prima_2() != null && new Integer("0").equals(kbrand.getMkb_cr_prima_2()))) ||
				(kbrand.getMkb_cr_prima_3()== null || ( kbrand.getMkb_cr_prima_3() != null && new Integer("0").equals(kbrand.getMkb_cr_prima_3()))) ||
				(kbrand.getMkb_cr_prima_4()== null || ( kbrand.getMkb_cr_prima_4() != null && new Integer("0").equals(kbrand.getMkb_cr_prima_4()))) ||
				(kbrand.getMkb_cr_prima_5()== null || ( kbrand.getMkb_cr_prima_5() != null && new Integer("0").equals(kbrand.getMkb_cr_prima_5()))) 
				){
			err.rejectValue( "mkb_cr_prima_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan 'pelayanan yang prima' ?"}, "error.formMustFilled" );
		}
		
		
		if( (kbrand.getMkb_pr_terbaik_1()== null || ( kbrand.getMkb_pr_terbaik_1() != null && new Integer("0").equals(kbrand.getMkb_pr_terbaik_1()))) ||
				(kbrand.getMkb_pr_terbaik_2()== null || ( kbrand.getMkb_pr_terbaik_2() != null && new Integer("0").equals(kbrand.getMkb_pr_terbaik_2()))) ||
				(kbrand.getMkb_pr_terbaik_3()== null || ( kbrand.getMkb_pr_terbaik_3() != null && new Integer("0").equals(kbrand.getMkb_pr_terbaik_3()))) ||
				(kbrand.getMkb_pr_terbaik_4()== null || ( kbrand.getMkb_pr_terbaik_4() != null && new Integer("0").equals(kbrand.getMkb_pr_terbaik_4()))) ||
				(kbrand.getMkb_pr_terbaik_5()== null || ( kbrand.getMkb_pr_terbaik_5() != null && new Integer("0").equals(kbrand.getMkb_pr_terbaik_5()))) 
				){
			err.rejectValue( "mkb_pr_terbaik_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda mencerminkan 'yang terbaik' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_pr_fleksibilias_1()== null || ( kbrand.getMkb_pr_fleksibilias_1() != null && new Integer("0").equals(kbrand.getMkb_pr_fleksibilias_1()))) ||
				(kbrand.getMkb_pr_fleksibilias_2()== null || ( kbrand.getMkb_pr_fleksibilias_2() != null && new Integer("0").equals(kbrand.getMkb_pr_fleksibilias_2()))) ||
				(kbrand.getMkb_pr_fleksibilias_3()== null || ( kbrand.getMkb_pr_fleksibilias_3() != null && new Integer("0").equals(kbrand.getMkb_pr_fleksibilias_3()))) ||
				(kbrand.getMkb_pr_fleksibilias_4()== null || ( kbrand.getMkb_pr_fleksibilias_4() != null && new Integer("0").equals(kbrand.getMkb_pr_fleksibilias_4()))) ||
				(kbrand.getMkb_pr_fleksibilias_5()== null || ( kbrand.getMkb_pr_fleksibilias_5() != null && new Integer("0").equals(kbrand.getMkb_pr_fleksibilias_5()))) 
				){
			err.rejectValue( "mkb_pr_fleksibilias_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan 'fleksibilias' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_pr_asuransi_1()== null || ( kbrand.getMkb_pr_asuransi_1() != null && new Integer("0").equals(kbrand.getMkb_pr_asuransi_1()))) ||
				(kbrand.getMkb_pr_asuransi_2()== null || ( kbrand.getMkb_pr_asuransi_2() != null && new Integer("0").equals(kbrand.getMkb_pr_asuransi_2()))) ||
				(kbrand.getMkb_pr_asuransi_3()== null || ( kbrand.getMkb_pr_asuransi_3() != null && new Integer("0").equals(kbrand.getMkb_pr_asuransi_3()))) ||
				(kbrand.getMkb_pr_asuransi_4()== null || ( kbrand.getMkb_pr_asuransi_4() != null && new Integer("0").equals(kbrand.getMkb_pr_asuransi_4()))) ||
				(kbrand.getMkb_pr_asuransi_5()== null || ( kbrand.getMkb_pr_asuransi_5() != null && new Integer("0").equals(kbrand.getMkb_pr_asuransi_5()))) 
				){
			err.rejectValue( "mkb_pr_asuransi_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan 'asuransi' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_pr_produk_1()== null || ( kbrand.getMkb_pr_produk_1() != null && new Integer("0").equals(kbrand.getMkb_pr_produk_1()))) ||
				(kbrand.getMkb_pr_produk_2()== null || ( kbrand.getMkb_pr_produk_2() != null && new Integer("0").equals(kbrand.getMkb_pr_produk_2()))) ||
				(kbrand.getMkb_pr_produk_3()== null || ( kbrand.getMkb_pr_produk_3() != null && new Integer("0").equals(kbrand.getMkb_pr_produk_3()))) ||
				(kbrand.getMkb_pr_produk_4()== null || ( kbrand.getMkb_pr_produk_4() != null && new Integer("0").equals(kbrand.getMkb_pr_produk_4()))) ||
				(kbrand.getMkb_pr_produk_5()== null || ( kbrand.getMkb_pr_produk_5() != null && new Integer("0").equals(kbrand.getMkb_pr_produk_5()))) 
				){
			err.rejectValue( "mkb_pr_produk_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan 'keragaman produk' ?"}, "error.formMustFilled" );
		}
		
		
		if( (kbrand.getMkb_im_internasional_1()== null || ( kbrand.getMkb_im_internasional_1() != null && new Integer("0").equals(kbrand.getMkb_im_internasional_1()))) ||
				(kbrand.getMkb_im_internasional_2()== null || ( kbrand.getMkb_im_internasional_2() != null && new Integer("0").equals(kbrand.getMkb_im_internasional_2()))) ||
				(kbrand.getMkb_im_internasional_3()== null || ( kbrand.getMkb_im_internasional_3() != null && new Integer("0").equals(kbrand.getMkb_im_internasional_3()))) ||
				(kbrand.getMkb_im_internasional_4()== null || ( kbrand.getMkb_im_internasional_4() != null && new Integer("0").equals(kbrand.getMkb_im_internasional_4()))) ||
				(kbrand.getMkb_im_internasional_5()== null || ( kbrand.getMkb_im_internasional_5() != null && new Integer("0").equals(kbrand.getMkb_im_internasional_5()))) 
				){
			err.rejectValue( "mkb_im_internasional_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda mencerminkan 'kesan internasional' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_im_positif_1()== null || ( kbrand.getMkb_im_positif_1() != null && new Integer("0").equals(kbrand.getMkb_im_positif_1()))) ||
				(kbrand.getMkb_im_positif_2()== null || ( kbrand.getMkb_im_positif_2() != null && new Integer("0").equals(kbrand.getMkb_im_positif_2()))) ||
				(kbrand.getMkb_im_positif_3()== null || ( kbrand.getMkb_im_positif_3() != null && new Integer("0").equals(kbrand.getMkb_im_positif_3()))) ||
				(kbrand.getMkb_im_positif_4()== null || ( kbrand.getMkb_im_positif_4() != null && new Integer("0").equals(kbrand.getMkb_im_positif_4()))) ||
				(kbrand.getMkb_im_positif_5()== null || ( kbrand.getMkb_im_positif_5() != null && new Integer("0").equals(kbrand.getMkb_im_positif_5()))) 
				){
			err.rejectValue( "mkb_im_positif_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan 'sikap yang positif' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_im_moderen_1()== null || ( kbrand.getMkb_im_moderen_1() != null && new Integer("0").equals(kbrand.getMkb_im_moderen_1()))) ||
				(kbrand.getMkb_im_moderen_2()== null || ( kbrand.getMkb_im_moderen_2() != null && new Integer("0").equals(kbrand.getMkb_im_moderen_2()))) ||
				(kbrand.getMkb_im_moderen_3()== null || ( kbrand.getMkb_im_moderen_3() != null && new Integer("0").equals(kbrand.getMkb_im_moderen_3()))) ||
				(kbrand.getMkb_im_moderen_4()== null || ( kbrand.getMkb_im_moderen_4() != null && new Integer("0").equals(kbrand.getMkb_im_moderen_4()))) ||
				(kbrand.getMkb_im_moderen_5()== null || ( kbrand.getMkb_im_moderen_5() != null && new Integer("0").equals(kbrand.getMkb_im_moderen_5()))) 
				){
			err.rejectValue( "mkb_im_moderen_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan kesan yang 'moderen' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_im_komitmen_1()== null || ( kbrand.getMkb_im_komitmen_1() != null && new Integer("0").equals(kbrand.getMkb_im_komitmen_1()))) ||
				(kbrand.getMkb_im_komitmen_2()== null || ( kbrand.getMkb_im_komitmen_2() != null && new Integer("0").equals(kbrand.getMkb_im_komitmen_2()))) ||
				(kbrand.getMkb_im_komitmen_3()== null || ( kbrand.getMkb_im_komitmen_3() != null && new Integer("0").equals(kbrand.getMkb_im_komitmen_3()))) ||
				(kbrand.getMkb_im_komitmen_4()== null || ( kbrand.getMkb_im_komitmen_4() != null && new Integer("0").equals(kbrand.getMkb_im_komitmen_4()))) ||
				(kbrand.getMkb_im_komitmen_5()== null || ( kbrand.getMkb_im_komitmen_5() != null && new Integer("0").equals(kbrand.getMkb_im_komitmen_5()))) 
				){
			err.rejectValue( "mkb_im_komitmen_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan 'komitmen' ?"}, "error.formMustFilled" );
		}
		
		
		if( (kbrand.getMkb_em_waspada_1()== null || ( kbrand.getMkb_em_waspada_1() != null && new Integer("0").equals(kbrand.getMkb_em_waspada_1()))) ||
				(kbrand.getMkb_em_waspada_2()== null || ( kbrand.getMkb_em_waspada_2() != null && new Integer("0").equals(kbrand.getMkb_em_waspada_2()))) ||
				(kbrand.getMkb_em_waspada_3()== null || ( kbrand.getMkb_em_waspada_3() != null && new Integer("0").equals(kbrand.getMkb_em_waspada_3()))) ||
				(kbrand.getMkb_em_waspada_4()== null || ( kbrand.getMkb_em_waspada_4() != null && new Integer("0").equals(kbrand.getMkb_em_waspada_4()))) ||
				(kbrand.getMkb_em_waspada_5()== null || ( kbrand.getMkb_em_waspada_5() != null && new Integer("0").equals(kbrand.getMkb_em_waspada_5()))) 
				){
			err.rejectValue( "mkb_em_waspada_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda mencerminkan 'mitra yang waspada' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_em_kenyamanan_1()== null || ( kbrand.getMkb_em_kenyamanan_1() != null && new Integer("0").equals(kbrand.getMkb_em_kenyamanan_1()))) ||
				(kbrand.getMkb_em_kenyamanan_2()== null || ( kbrand.getMkb_em_kenyamanan_2() != null && new Integer("0").equals(kbrand.getMkb_em_kenyamanan_2()))) ||
				(kbrand.getMkb_em_kenyamanan_3()== null || ( kbrand.getMkb_em_kenyamanan_3() != null && new Integer("0").equals(kbrand.getMkb_em_kenyamanan_3()))) ||
				(kbrand.getMkb_em_kenyamanan_4()== null || ( kbrand.getMkb_em_kenyamanan_4() != null && new Integer("0").equals(kbrand.getMkb_em_kenyamanan_4()))) ||
				(kbrand.getMkb_em_kenyamanan_5()== null || ( kbrand.getMkb_em_kenyamanan_5() != null && new Integer("0").equals(kbrand.getMkb_em_kenyamanan_5()))) 
				){
			err.rejectValue( "mkb_em_kenyamanan_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan 'kenyamanan' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_em_pengertian_1()== null || ( kbrand.getMkb_em_pengertian_1() != null && new Integer("0").equals(kbrand.getMkb_em_pengertian_1()))) ||
				(kbrand.getMkb_em_pengertian_2()== null || ( kbrand.getMkb_em_pengertian_2() != null && new Integer("0").equals(kbrand.getMkb_em_pengertian_2()))) ||
				(kbrand.getMkb_em_pengertian_3()== null || ( kbrand.getMkb_em_pengertian_3() != null && new Integer("0").equals(kbrand.getMkb_em_pengertian_3()))) ||
				(kbrand.getMkb_em_pengertian_4()== null || ( kbrand.getMkb_em_pengertian_4() != null && new Integer("0").equals(kbrand.getMkb_em_pengertian_4()))) ||
				(kbrand.getMkb_em_pengertian_5()== null || ( kbrand.getMkb_em_pengertian_5() != null && new Integer("0").equals(kbrand.getMkb_em_pengertian_5()))) 
				){
			err.rejectValue( "mkb_em_pengertian_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan 'pengertian' ?"}, "error.formMustFilled" );
		}
		if( (kbrand.getMkb_em_ketulusan_1()== null || ( kbrand.getMkb_em_ketulusan_1() != null && new Integer("0").equals(kbrand.getMkb_em_ketulusan_1()))) ||
				(kbrand.getMkb_em_ketulusan_2()== null || ( kbrand.getMkb_em_ketulusan_2() != null && new Integer("0").equals(kbrand.getMkb_em_ketulusan_2()))) ||
				(kbrand.getMkb_em_ketulusan_3()== null || ( kbrand.getMkb_em_ketulusan_3() != null && new Integer("0").equals(kbrand.getMkb_em_ketulusan_3()))) ||
				(kbrand.getMkb_em_ketulusan_4()== null || ( kbrand.getMkb_em_ketulusan_4() != null && new Integer("0").equals(kbrand.getMkb_em_ketulusan_4()))) ||
				(kbrand.getMkb_em_ketulusan_5()== null || ( kbrand.getMkb_em_ketulusan_5() != null && new Integer("0").equals(kbrand.getMkb_em_ketulusan_5()))) 
				){
			err.rejectValue( "mkb_em_ketulusan_1", "error.formMustFilled", new Object[] {"Manakah yang menurut anda menggambarkan 'ketulusan' ?"}, "error.formMustFilled" );
		}
		
		
		Integer msig_1 = 0, msig_2 = 0, msig_3 = 0, msig_4 = 0, msig_5 = 0, msig_6 = 0, msig_7 = 0, msig_8 = 0, msig_9 = 0, msig_10 = 0, msig_11 = 0, msig_12 = 0, msig_13 = 0;
		if( new Integer("1").equals(kbrand.getMkb_cr_msig_1()) ){msig_1 = msig_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_msig_2()) ){msig_1 = msig_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_msig_3()) ){msig_1 = msig_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_msig_4()) ){msig_1 = msig_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_msig_5()) ){msig_1 = msig_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_cr_msig_1()) ){msig_2 = msig_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_msig_2()) ){msig_2 = msig_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_msig_3()) ){msig_2 = msig_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_msig_4()) ){msig_2 = msig_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_msig_5()) ){msig_2 = msig_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_cr_msig_1()) ){msig_3 = msig_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_msig_2()) ){msig_3 = msig_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_msig_3()) ){msig_3 = msig_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_msig_4()) ){msig_3 = msig_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_msig_5()) ){msig_3 = msig_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_cr_msig_1()) ){msig_4 = msig_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_msig_2()) ){msig_4 = msig_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_msig_3()) ){msig_4 = msig_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_msig_4()) ){msig_4 = msig_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_msig_5()) ){msig_4 = msig_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_cr_msig_1()) ){msig_5 = msig_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_msig_2()) ){msig_5 = msig_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_msig_3()) ){msig_5 = msig_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_msig_4()) ){msig_5 = msig_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_msig_5()) ){msig_5 = msig_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_cr_msig_1()) ){msig_6 = msig_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_msig_2()) ){msig_6 = msig_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_msig_3()) ){msig_6 = msig_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_msig_4()) ){msig_6 = msig_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_msig_5()) ){msig_6 = msig_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_cr_msig_1()) ){msig_7 = msig_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_msig_2()) ){msig_7 = msig_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_msig_3()) ){msig_7 = msig_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_msig_4()) ){msig_7 = msig_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_msig_5()) ){msig_7 = msig_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_cr_msig_1()) ){msig_8 = msig_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_msig_2()) ){msig_8 = msig_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_msig_3()) ){msig_8 = msig_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_msig_4()) ){msig_8 = msig_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_msig_5()) ){msig_8 = msig_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_cr_msig_1()) ){msig_9 = msig_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_msig_2()) ){msig_9 = msig_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_msig_3()) ){msig_9 = msig_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_msig_4()) ){msig_9 = msig_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_msig_5()) ){msig_9 = msig_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_cr_msig_1()) ){msig_10 = msig_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_msig_2()) ){msig_10 = msig_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_msig_3()) ){msig_10 = msig_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_msig_4()) ){msig_10 = msig_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_msig_5()) ){msig_10 = msig_10 + 1;}
		
		if( new Integer("11").equals(kbrand.getMkb_cr_msig_1()) ){msig_11 = msig_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_msig_2()) ){msig_11 = msig_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_msig_3()) ){msig_11 = msig_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_msig_4()) ){msig_11 = msig_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_msig_5()) ){msig_11 = msig_11 + 1;}
		
		if( new Integer("12").equals(kbrand.getMkb_cr_msig_1()) ){msig_12 = msig_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_msig_2()) ){msig_12 = msig_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_msig_3()) ){msig_12 = msig_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_msig_4()) ){msig_12 = msig_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_msig_5()) ){msig_12 = msig_12 + 1;}
		
		
		if( msig_1 > 1 || msig_2 > 1 || msig_3 > 1 || msig_4 > 1 || msig_5 > 1 || msig_6 > 1 || msig_7 > 1 || msig_8 > 1 || msig_9 > 1 || msig_10 > 1 || msig_11 > 1 || msig_12 > 1){
			err.rejectValue( "mkb_cr_msig_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda mencerimankan 'Sinarmas-MSIG' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		
		Integer aset_1 = 0, aset_2 = 0, aset_3 = 0, aset_4 = 0, aset_5 = 0, aset_6 = 0, aset_7 = 0, aset_8 = 0, aset_9 = 0, aset_10 = 0, aset_11 = 0, aset_12 = 0, aset_13 = 0;
		if( new Integer("1").equals(kbrand.getMkb_cr_aset_1()) ){aset_1 = aset_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_aset_2()) ){aset_1 = aset_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_aset_3()) ){aset_1 = aset_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_aset_4()) ){aset_1 = aset_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_aset_5()) ){aset_1 = aset_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_cr_aset_1()) ){aset_2 = aset_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_aset_2()) ){aset_2 = aset_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_aset_3()) ){aset_2 = aset_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_aset_4()) ){aset_2 = aset_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_aset_5()) ){aset_2 = aset_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_cr_aset_1()) ){aset_3 = aset_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_aset_2()) ){aset_3 = aset_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_aset_3()) ){aset_3 = aset_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_aset_4()) ){aset_3 = aset_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_aset_5()) ){aset_3 = aset_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_cr_aset_1()) ){aset_4 = aset_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_aset_2()) ){aset_4 = aset_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_aset_3()) ){aset_4 = aset_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_aset_4()) ){aset_4 = aset_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_aset_5()) ){aset_4 = aset_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_cr_aset_1()) ){aset_5 = aset_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_aset_2()) ){aset_5 = aset_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_aset_3()) ){aset_5 = aset_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_aset_4()) ){aset_5 = aset_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_aset_5()) ){aset_5 = aset_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_cr_aset_1()) ){aset_6 = aset_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_aset_2()) ){aset_6 = aset_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_aset_3()) ){aset_6 = aset_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_aset_4()) ){aset_6 = aset_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_aset_5()) ){aset_6 = aset_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_cr_aset_1()) ){aset_7 = aset_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_aset_2()) ){aset_7 = aset_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_aset_3()) ){aset_7 = aset_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_aset_4()) ){aset_7 = aset_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_aset_5()) ){aset_7 = aset_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_cr_aset_1()) ){aset_8 = aset_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_aset_2()) ){aset_8 = aset_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_aset_3()) ){aset_8 = aset_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_aset_4()) ){aset_8 = aset_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_aset_5()) ){aset_8 = aset_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_cr_aset_1()) ){aset_9 = aset_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_aset_2()) ){aset_9 = aset_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_aset_3()) ){aset_9 = aset_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_aset_4()) ){aset_9 = aset_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_aset_5()) ){aset_9 = aset_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_cr_aset_1()) ){aset_10 = aset_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_aset_2()) ){aset_10 = aset_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_aset_3()) ){aset_10 = aset_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_aset_4()) ){aset_10 = aset_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_aset_5()) ){aset_10 = aset_10 + 1;}
		
		if( new Integer("11").equals(kbrand.getMkb_cr_aset_1()) ){aset_11 = aset_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_aset_2()) ){aset_11 = aset_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_aset_3()) ){aset_11 = aset_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_aset_4()) ){aset_11 = aset_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_aset_5()) ){aset_11 = aset_11 + 1;}
		
		if( new Integer("12").equals(kbrand.getMkb_cr_aset_1()) ){aset_12 = aset_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_aset_2()) ){aset_12 = aset_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_aset_3()) ){aset_12 = aset_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_aset_4()) ){aset_12 = aset_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_aset_5()) ){aset_12 = aset_12 + 1;}
		
		if( aset_1 > 1 || aset_2 > 1 || aset_3 > 1 || aset_4 > 1 || aset_5 > 1 || aset_6 > 1 || aset_7 > 1 || aset_8 > 1 || aset_9 > 1 || aset_10 > 1 || aset_11 > 1 || aset_12 > 1){
			err.rejectValue( "mkb_cr_aset_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan 'kekuatan aset' ? "}, "error.fieldCantMoreThanOne" );
		}
		
		Integer pelanggan_1 = 0, pelanggan_2 = 0, pelanggan_3 = 0, pelanggan_4 = 0, pelanggan_5 = 0, pelanggan_6 = 0, pelanggan_7 = 0, pelanggan_8 = 0, pelanggan_9 = 0, pelanggan_10 = 0, pelanggan_11 = 0, pelanggan_12 = 0, pelanggan_13 = 0;
		if( new Integer("1").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_1 = pelanggan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_1 = pelanggan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_1 = pelanggan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_1 = pelanggan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_1 = pelanggan_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_2 = pelanggan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_2 = pelanggan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_2 = pelanggan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_2 = pelanggan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_2 = pelanggan_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_3 = pelanggan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_3 = pelanggan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_3 = pelanggan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_3 = pelanggan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_3 = pelanggan_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_4 = pelanggan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_4 = pelanggan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_4 = pelanggan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_4 = pelanggan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_4 = pelanggan_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_5 = pelanggan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_5 = pelanggan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_5 = pelanggan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_5 = pelanggan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_5 = pelanggan_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_6 = pelanggan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_6 = pelanggan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_6 = pelanggan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_6 = pelanggan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_6 = pelanggan_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_7 = pelanggan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_7 = pelanggan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_7 = pelanggan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_7 = pelanggan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_7 = pelanggan_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_8 = pelanggan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_8 = pelanggan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_8 = pelanggan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_8 = pelanggan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_8 = pelanggan_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_9 = pelanggan_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_9 = pelanggan_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_9 = pelanggan_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_9 = pelanggan_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_9 = pelanggan_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_10 = pelanggan_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_10 = pelanggan_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_10 = pelanggan_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_10 = pelanggan_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_10 = pelanggan_10 + 1;}
		
		if( new Integer("11").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_11 = pelanggan_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_11 = pelanggan_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_11 = pelanggan_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_11 = pelanggan_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_11 = pelanggan_11 + 1;}
		
		if( new Integer("12").equals(kbrand.getMkb_cr_pelanggan_1()) ){pelanggan_12 = pelanggan_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_pelanggan_2()) ){pelanggan_12 = pelanggan_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_pelanggan_3()) ){pelanggan_12 = pelanggan_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_pelanggan_4()) ){pelanggan_12 = pelanggan_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_pelanggan_5()) ){pelanggan_12 = pelanggan_12 + 1;}
		
		if( pelanggan_1 > 1 || pelanggan_2 > 1 || pelanggan_3 > 1 || pelanggan_4 > 1 || pelanggan_5 > 1 || pelanggan_6 > 1 || pelanggan_7 > 1 || pelanggan_8 > 1 || pelanggan_9 > 1 || pelanggan_10 > 1 || pelanggan_11 > 1 || pelanggan_12 > 1){
			err.rejectValue( "mkb_cr_pelanggan_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan 'fokus terhadap kebutuhan pelanggan' ? "}, "error.fieldCantMoreThanOne" );
		}
		
		Integer prima_1 = 0, prima_2 = 0, prima_3 = 0, prima_4 = 0, prima_5 = 0, prima_6 = 0, prima_7 = 0, prima_8 = 0, prima_9 = 0, prima_10 = 0, prima_11 = 0, prima_12 = 0, prima_13 = 0;
		if( new Integer("1").equals(kbrand.getMkb_cr_prima_1()) ){prima_1 = prima_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_prima_2()) ){prima_1 = prima_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_prima_3()) ){prima_1 = prima_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_prima_4()) ){prima_1 = prima_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_cr_prima_5()) ){prima_1 = prima_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_cr_prima_1()) ){prima_2 = prima_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_prima_2()) ){prima_2 = prima_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_prima_3()) ){prima_2 = prima_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_prima_4()) ){prima_2 = prima_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_cr_prima_5()) ){prima_2 = prima_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_cr_prima_1()) ){prima_3 = prima_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_prima_2()) ){prima_3 = prima_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_prima_3()) ){prima_3 = prima_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_prima_4()) ){prima_3 = prima_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_cr_prima_5()) ){prima_3 = prima_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_cr_prima_1()) ){prima_4 = prima_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_prima_2()) ){prima_4 = prima_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_prima_3()) ){prima_4 = prima_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_prima_4()) ){prima_4 = prima_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_cr_prima_5()) ){prima_4 = prima_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_cr_prima_1()) ){prima_5 = prima_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_prima_2()) ){prima_5 = prima_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_prima_3()) ){prima_5 = prima_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_prima_4()) ){prima_5 = prima_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_cr_prima_5()) ){prima_5 = prima_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_cr_prima_1()) ){prima_6 = prima_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_prima_2()) ){prima_6 = prima_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_prima_3()) ){prima_6 = prima_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_prima_4()) ){prima_6 = prima_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_cr_prima_5()) ){prima_6 = prima_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_cr_prima_1()) ){prima_7 = prima_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_prima_2()) ){prima_7 = prima_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_prima_3()) ){prima_7 = prima_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_prima_4()) ){prima_7 = prima_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_cr_prima_5()) ){prima_7 = prima_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_cr_prima_1()) ){prima_8 = prima_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_prima_2()) ){prima_8 = prima_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_prima_3()) ){prima_8 = prima_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_prima_4()) ){prima_8 = prima_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_cr_prima_5()) ){prima_8 = prima_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_cr_prima_1()) ){prima_9 = prima_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_prima_2()) ){prima_9 = prima_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_prima_3()) ){prima_9 = prima_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_prima_4()) ){prima_9 = prima_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_cr_prima_5()) ){prima_9 = prima_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_cr_prima_1()) ){prima_10 = prima_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_prima_2()) ){prima_10 = prima_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_prima_3()) ){prima_10 = prima_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_prima_4()) ){prima_10 = prima_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_cr_prima_5()) ){prima_10 = prima_10 + 1;}
		
		if( new Integer("11").equals(kbrand.getMkb_cr_prima_1()) ){prima_11 = prima_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_prima_2()) ){prima_11 = prima_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_prima_3()) ){prima_11 = prima_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_prima_4()) ){prima_11 = prima_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_cr_prima_5()) ){prima_11 = prima_11 + 1;}
		
		if( new Integer("12").equals(kbrand.getMkb_cr_prima_1()) ){prima_12 = prima_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_prima_2()) ){prima_12 = prima_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_prima_3()) ){prima_12 = prima_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_prima_4()) ){prima_12 = prima_12 + 1;}
		if( new Integer("12").equals(kbrand.getMkb_cr_prima_5()) ){prima_12 = prima_12 + 1;}
		
		
		if( prima_1 > 1 || prima_2 > 1 || prima_3 > 1 || prima_4 > 1 || prima_5 > 1 || prima_6 > 1 || prima_7 > 1 || prima_8 > 1 || prima_9 > 1 || prima_10 > 1 || prima_11 > 1 || prima_12 > 1){
			err.rejectValue( "mkb_cr_prima_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan 'pelayanan yang prima' ? "}, "error.fieldCantMoreThanOne" );
		}
		
		
		Integer terbaik_1 = 0, terbaik_2 = 0, terbaik_3 = 0, terbaik_4 = 0, terbaik_5 = 0, terbaik_6 = 0, terbaik_7 = 0, terbaik_8 = 0, terbaik_9 = 0, terbaik_10 = 0, terbaik_11 = 0;
		if( new Integer("1").equals(kbrand.getMkb_pr_terbaik_1()) ){terbaik_1 = terbaik_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_terbaik_2()) ){terbaik_1 = terbaik_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_terbaik_3()) ){terbaik_1 = terbaik_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_terbaik_4()) ){terbaik_1 = terbaik_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_terbaik_5()) ){terbaik_1 = terbaik_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_pr_terbaik_1()) ){terbaik_2 = terbaik_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_terbaik_2()) ){terbaik_2 = terbaik_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_terbaik_3()) ){terbaik_2 = terbaik_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_terbaik_4()) ){terbaik_2 = terbaik_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_terbaik_5()) ){terbaik_2 = terbaik_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_pr_terbaik_1()) ){terbaik_3 = terbaik_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_terbaik_2()) ){terbaik_3 = terbaik_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_terbaik_3()) ){terbaik_3 = terbaik_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_terbaik_4()) ){terbaik_3 = terbaik_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_terbaik_5()) ){terbaik_3 = terbaik_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_pr_terbaik_1()) ){terbaik_4 = terbaik_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_terbaik_2()) ){terbaik_4 = terbaik_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_terbaik_3()) ){terbaik_4 = terbaik_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_terbaik_4()) ){terbaik_4 = terbaik_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_terbaik_5()) ){terbaik_4 = terbaik_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_pr_terbaik_1()) ){terbaik_5 = terbaik_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_terbaik_2()) ){terbaik_5 = terbaik_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_terbaik_3()) ){terbaik_5 = terbaik_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_terbaik_4()) ){terbaik_5 = terbaik_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_terbaik_5()) ){terbaik_5 = terbaik_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_pr_terbaik_1()) ){terbaik_6 = terbaik_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_terbaik_2()) ){terbaik_6 = terbaik_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_terbaik_3()) ){terbaik_6 = terbaik_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_terbaik_4()) ){terbaik_6 = terbaik_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_terbaik_5()) ){terbaik_6 = terbaik_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_pr_terbaik_1()) ){terbaik_7 = terbaik_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_terbaik_2()) ){terbaik_7 = terbaik_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_terbaik_3()) ){terbaik_7 = terbaik_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_terbaik_4()) ){terbaik_7 = terbaik_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_terbaik_5()) ){terbaik_7 = terbaik_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_pr_terbaik_1()) ){terbaik_8 = terbaik_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_terbaik_2()) ){terbaik_8 = terbaik_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_terbaik_3()) ){terbaik_8 = terbaik_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_terbaik_4()) ){terbaik_8 = terbaik_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_terbaik_5()) ){terbaik_8 = terbaik_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_pr_terbaik_1()) ){terbaik_9 = terbaik_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_terbaik_2()) ){terbaik_9 = terbaik_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_terbaik_3()) ){terbaik_9 = terbaik_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_terbaik_4()) ){terbaik_9 = terbaik_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_terbaik_5()) ){terbaik_9 = terbaik_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_pr_terbaik_1()) ){terbaik_10 = terbaik_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_terbaik_2()) ){terbaik_10 = terbaik_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_terbaik_3()) ){terbaik_10 = terbaik_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_terbaik_4()) ){terbaik_10 = terbaik_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_terbaik_5()) ){terbaik_10 = terbaik_10 + 1;}
		
		if( new Integer("11").equals(kbrand.getMkb_pr_terbaik_1()) ){terbaik_11 = terbaik_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_terbaik_2()) ){terbaik_11 = terbaik_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_terbaik_3()) ){terbaik_11 = terbaik_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_terbaik_4()) ){terbaik_11 = terbaik_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_terbaik_5()) ){terbaik_11 = terbaik_11 + 1;}
		
		if( terbaik_1 > 1 || terbaik_2 > 1 || terbaik_3 > 1 || terbaik_4 > 1 || terbaik_5 > 1 || terbaik_6 > 1 || terbaik_7 > 1 || terbaik_8 > 1 || terbaik_9 > 1 || terbaik_10 > 1 || terbaik_11 > 1){
			err.rejectValue( "mkb_pr_terbaik_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda mencerminkan 'yang terbaik' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		
		Integer fleksibilias_1 = 0, fleksibilias_2 = 0, fleksibilias_3 = 0, fleksibilias_4 = 0, fleksibilias_5 = 0, fleksibilias_6 = 0, fleksibilias_7 = 0, fleksibilias_8 = 0, fleksibilias_9 = 0, fleksibilias_10 = 0, fleksibilias_11 = 0;
		if( new Integer("1").equals(kbrand.getMkb_pr_fleksibilias_1()) ){fleksibilias_1 = fleksibilias_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_fleksibilias_2()) ){fleksibilias_1 = fleksibilias_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_fleksibilias_3()) ){fleksibilias_1 = fleksibilias_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_fleksibilias_4()) ){fleksibilias_1 = fleksibilias_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_fleksibilias_5()) ){fleksibilias_1 = fleksibilias_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_pr_fleksibilias_1()) ){fleksibilias_2 = fleksibilias_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_fleksibilias_2()) ){fleksibilias_2 = fleksibilias_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_fleksibilias_3()) ){fleksibilias_2 = fleksibilias_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_fleksibilias_4()) ){fleksibilias_2 = fleksibilias_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_fleksibilias_5()) ){fleksibilias_2 = fleksibilias_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_pr_fleksibilias_1()) ){fleksibilias_3 = fleksibilias_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_fleksibilias_2()) ){fleksibilias_3 = fleksibilias_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_fleksibilias_3()) ){fleksibilias_3 = fleksibilias_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_fleksibilias_4()) ){fleksibilias_3 = fleksibilias_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_fleksibilias_5()) ){fleksibilias_3 = fleksibilias_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_pr_fleksibilias_1()) ){fleksibilias_4 = fleksibilias_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_fleksibilias_2()) ){fleksibilias_4 = fleksibilias_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_fleksibilias_3()) ){fleksibilias_4 = fleksibilias_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_fleksibilias_4()) ){fleksibilias_4 = fleksibilias_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_fleksibilias_5()) ){fleksibilias_4 = fleksibilias_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_pr_fleksibilias_1()) ){fleksibilias_5 = fleksibilias_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_fleksibilias_2()) ){fleksibilias_5 = fleksibilias_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_fleksibilias_3()) ){fleksibilias_5 = fleksibilias_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_fleksibilias_4()) ){fleksibilias_5 = fleksibilias_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_fleksibilias_5()) ){fleksibilias_5 = fleksibilias_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_pr_fleksibilias_1()) ){fleksibilias_6 = fleksibilias_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_fleksibilias_2()) ){fleksibilias_6 = fleksibilias_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_fleksibilias_3()) ){fleksibilias_6 = fleksibilias_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_fleksibilias_4()) ){fleksibilias_6 = fleksibilias_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_fleksibilias_5()) ){fleksibilias_6 = fleksibilias_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_pr_fleksibilias_1()) ){fleksibilias_7 = fleksibilias_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_fleksibilias_2()) ){fleksibilias_7 = fleksibilias_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_fleksibilias_3()) ){fleksibilias_7 = fleksibilias_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_fleksibilias_4()) ){fleksibilias_7 = fleksibilias_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_fleksibilias_5()) ){fleksibilias_7 = fleksibilias_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_pr_fleksibilias_1()) ){fleksibilias_8 = fleksibilias_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_fleksibilias_2()) ){fleksibilias_8 = fleksibilias_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_fleksibilias_3()) ){fleksibilias_8 = fleksibilias_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_fleksibilias_4()) ){fleksibilias_8 = fleksibilias_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_fleksibilias_5()) ){fleksibilias_8 = fleksibilias_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_pr_fleksibilias_1()) ){fleksibilias_9 = fleksibilias_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_fleksibilias_2()) ){fleksibilias_9 = fleksibilias_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_fleksibilias_3()) ){fleksibilias_9 = fleksibilias_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_fleksibilias_4()) ){fleksibilias_9 = fleksibilias_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_fleksibilias_5()) ){fleksibilias_9 = fleksibilias_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_pr_fleksibilias_1()) ){fleksibilias_10 = fleksibilias_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_fleksibilias_2()) ){fleksibilias_10 = fleksibilias_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_fleksibilias_3()) ){fleksibilias_10 = fleksibilias_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_fleksibilias_4()) ){fleksibilias_10 = fleksibilias_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_fleksibilias_5()) ){fleksibilias_10 = fleksibilias_10 + 1;}

		if( new Integer("11").equals(kbrand.getMkb_pr_fleksibilias_1()) ){fleksibilias_11 = fleksibilias_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_fleksibilias_2()) ){fleksibilias_11 = fleksibilias_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_fleksibilias_3()) ){fleksibilias_11 = fleksibilias_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_fleksibilias_4()) ){fleksibilias_11 = fleksibilias_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_fleksibilias_5()) ){fleksibilias_11 = fleksibilias_11 + 1;}
		
		if( fleksibilias_1 > 1 || fleksibilias_2 > 1 || fleksibilias_3 > 1 || fleksibilias_4 > 1 || fleksibilias_5 > 1 || fleksibilias_6 > 1 || fleksibilias_7 > 1 || fleksibilias_8 > 1 || fleksibilias_9 > 1 || fleksibilias_10 > 1 || fleksibilias_11 > 1){
			err.rejectValue( "mkb_pr_fleksibilias_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan 'fleksibilias' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		Integer asuransi_1 = 0, asuransi_2 = 0, asuransi_3 = 0, asuransi_4 = 0, asuransi_5 = 0, asuransi_6 = 0, asuransi_7 = 0, asuransi_8 = 0, asuransi_9 = 0, asuransi_10 = 0, asuransi_11 = 0;
		if( new Integer("1").equals(kbrand.getMkb_pr_asuransi_1()) ){asuransi_1 = asuransi_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_asuransi_2()) ){asuransi_1 = asuransi_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_asuransi_3()) ){asuransi_1 = asuransi_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_asuransi_4()) ){asuransi_1 = asuransi_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_asuransi_5()) ){asuransi_1 = asuransi_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_pr_asuransi_1()) ){asuransi_2 = asuransi_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_asuransi_2()) ){asuransi_2 = asuransi_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_asuransi_3()) ){asuransi_2 = asuransi_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_asuransi_4()) ){asuransi_2 = asuransi_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_asuransi_5()) ){asuransi_2 = asuransi_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_pr_asuransi_1()) ){asuransi_3 = asuransi_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_asuransi_2()) ){asuransi_3 = asuransi_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_asuransi_3()) ){asuransi_3 = asuransi_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_asuransi_4()) ){asuransi_3 = asuransi_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_asuransi_5()) ){asuransi_3 = asuransi_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_pr_asuransi_1()) ){asuransi_4 = asuransi_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_asuransi_2()) ){asuransi_4 = asuransi_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_asuransi_3()) ){asuransi_4 = asuransi_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_asuransi_4()) ){asuransi_4 = asuransi_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_asuransi_5()) ){asuransi_4 = asuransi_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_pr_asuransi_1()) ){asuransi_5 = asuransi_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_asuransi_2()) ){asuransi_5 = asuransi_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_asuransi_3()) ){asuransi_5 = asuransi_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_asuransi_4()) ){asuransi_5 = asuransi_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_asuransi_5()) ){asuransi_5 = asuransi_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_pr_asuransi_1()) ){asuransi_6 = asuransi_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_asuransi_2()) ){asuransi_6 = asuransi_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_asuransi_3()) ){asuransi_6 = asuransi_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_asuransi_4()) ){asuransi_6 = asuransi_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_asuransi_5()) ){asuransi_6 = asuransi_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_pr_asuransi_1()) ){asuransi_7 = asuransi_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_asuransi_2()) ){asuransi_7 = asuransi_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_asuransi_3()) ){asuransi_7 = asuransi_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_asuransi_4()) ){asuransi_7 = asuransi_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_asuransi_5()) ){asuransi_7 = asuransi_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_pr_asuransi_1()) ){asuransi_8 = asuransi_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_asuransi_2()) ){asuransi_8 = asuransi_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_asuransi_3()) ){asuransi_8 = asuransi_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_asuransi_4()) ){asuransi_8 = asuransi_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_asuransi_5()) ){asuransi_8 = asuransi_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_pr_asuransi_1()) ){asuransi_9 = asuransi_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_asuransi_2()) ){asuransi_9 = asuransi_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_asuransi_3()) ){asuransi_9 = asuransi_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_asuransi_4()) ){asuransi_9 = asuransi_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_asuransi_5()) ){asuransi_9 = asuransi_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_pr_asuransi_1()) ){asuransi_10 = asuransi_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_asuransi_2()) ){asuransi_10 = asuransi_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_asuransi_3()) ){asuransi_10 = asuransi_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_asuransi_4()) ){asuransi_10 = asuransi_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_asuransi_5()) ){asuransi_10 = asuransi_10 + 1;}
		
		if( new Integer("11").equals(kbrand.getMkb_pr_asuransi_1()) ){asuransi_11 = asuransi_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_asuransi_2()) ){asuransi_11 = asuransi_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_asuransi_3()) ){asuransi_11 = asuransi_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_asuransi_4()) ){asuransi_11 = asuransi_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_asuransi_5()) ){asuransi_11 = asuransi_11 + 1;}
		
		if( asuransi_1 > 1 || asuransi_2 > 1 || asuransi_3 > 1 || asuransi_4 > 1 || asuransi_5 > 1 || asuransi_6 > 1 || asuransi_7 > 1 || asuransi_8 > 1 || asuransi_9 > 1 || asuransi_10 > 1 || asuransi_11 > 1){
			err.rejectValue( "mkb_pr_asuransi_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan 'asuransi' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		
		Integer produk_1 = 0, produk_2 = 0, produk_3 = 0, produk_4 = 0, produk_5 = 0, produk_6 = 0, produk_7 = 0, produk_8 = 0, produk_9 = 0, produk_10 = 0, produk_11 = 0;
		if( new Integer("1").equals(kbrand.getMkb_pr_produk_1()) ){produk_1 = produk_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_produk_2()) ){produk_1 = produk_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_produk_3()) ){produk_1 = produk_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_produk_4()) ){produk_1 = produk_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_pr_produk_5()) ){produk_1 = produk_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_pr_produk_1()) ){produk_2 = produk_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_produk_2()) ){produk_2 = produk_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_produk_3()) ){produk_2 = produk_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_produk_4()) ){produk_2 = produk_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_pr_produk_5()) ){produk_2 = produk_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_pr_produk_1()) ){produk_3 = produk_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_produk_2()) ){produk_3 = produk_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_produk_3()) ){produk_3 = produk_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_produk_4()) ){produk_3 = produk_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_pr_produk_5()) ){produk_3 = produk_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_pr_produk_1()) ){produk_4 = produk_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_produk_2()) ){produk_4 = produk_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_produk_3()) ){produk_4 = produk_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_produk_4()) ){produk_4 = produk_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_pr_produk_5()) ){produk_4 = produk_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_pr_produk_1()) ){produk_5 = produk_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_produk_2()) ){produk_5 = produk_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_produk_3()) ){produk_5 = produk_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_produk_4()) ){produk_5 = produk_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_pr_produk_5()) ){produk_5 = produk_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_pr_produk_1()) ){produk_6 = produk_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_produk_2()) ){produk_6 = produk_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_produk_3()) ){produk_6 = produk_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_produk_4()) ){produk_6 = produk_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_pr_produk_5()) ){produk_6 = produk_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_pr_produk_1()) ){produk_7 = produk_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_produk_2()) ){produk_7 = produk_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_produk_3()) ){produk_7 = produk_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_produk_4()) ){produk_7 = produk_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_pr_produk_5()) ){produk_7 = produk_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_pr_produk_1()) ){produk_8 = produk_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_produk_2()) ){produk_8 = produk_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_produk_3()) ){produk_8 = produk_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_produk_4()) ){produk_8 = produk_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_pr_produk_5()) ){produk_8 = produk_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_pr_produk_1()) ){produk_9 = produk_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_produk_2()) ){produk_9 = produk_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_produk_3()) ){produk_9 = produk_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_produk_4()) ){produk_9 = produk_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_pr_produk_5()) ){produk_9 = produk_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_pr_produk_1()) ){produk_10 = produk_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_produk_2()) ){produk_10 = produk_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_produk_3()) ){produk_10 = produk_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_produk_4()) ){produk_10 = produk_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_pr_produk_5()) ){produk_10 = produk_10 + 1;}
		
		if( new Integer("11").equals(kbrand.getMkb_pr_produk_1()) ){produk_11 = produk_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_produk_2()) ){produk_11 = produk_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_produk_3()) ){produk_11 = produk_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_produk_4()) ){produk_11 = produk_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_pr_produk_5()) ){produk_11 = produk_11 + 1;}
		
		if( produk_1 > 1 || produk_2 > 1 || produk_3 > 1 || produk_4 > 1 || produk_5 > 1 || produk_6 > 1 || produk_7 > 1 || produk_8 > 1 || produk_9 > 1 || produk_10 > 1 || produk_11 > 1){
			err.rejectValue( "mkb_pr_produk_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan 'keragaman produk' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		
		Integer internasional_1 = 0, internasional_2 = 0, internasional_3 = 0, internasional_4 = 0, internasional_5 = 0, internasional_6 = 0, internasional_7 = 0, internasional_8 = 0, internasional_9 = 0, internasional_10 = 0, internasional_11 = 0;
		if( new Integer("1").equals(kbrand.getMkb_im_internasional_1()) ){internasional_1 = internasional_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_internasional_2()) ){internasional_1 = internasional_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_internasional_3()) ){internasional_1 = internasional_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_internasional_4()) ){internasional_1 = internasional_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_internasional_5()) ){internasional_1 = internasional_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_im_internasional_1()) ){internasional_2 = internasional_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_internasional_2()) ){internasional_2 = internasional_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_internasional_3()) ){internasional_2 = internasional_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_internasional_4()) ){internasional_2 = internasional_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_internasional_5()) ){internasional_2 = internasional_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_im_internasional_1()) ){internasional_3 = internasional_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_internasional_2()) ){internasional_3 = internasional_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_internasional_3()) ){internasional_3 = internasional_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_internasional_4()) ){internasional_3 = internasional_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_internasional_5()) ){internasional_3 = internasional_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_im_internasional_1()) ){internasional_4 = internasional_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_internasional_2()) ){internasional_4 = internasional_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_internasional_3()) ){internasional_4 = internasional_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_internasional_4()) ){internasional_4 = internasional_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_internasional_5()) ){internasional_4 = internasional_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_im_internasional_1()) ){internasional_5 = internasional_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_internasional_2()) ){internasional_5 = internasional_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_internasional_3()) ){internasional_5 = internasional_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_internasional_4()) ){internasional_5 = internasional_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_internasional_5()) ){internasional_5 = internasional_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_im_internasional_1()) ){internasional_6 = internasional_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_internasional_2()) ){internasional_6 = internasional_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_internasional_3()) ){internasional_6 = internasional_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_internasional_4()) ){internasional_6 = internasional_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_internasional_5()) ){internasional_6 = internasional_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_im_internasional_1()) ){internasional_7 = internasional_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_internasional_2()) ){internasional_7 = internasional_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_internasional_3()) ){internasional_7 = internasional_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_internasional_4()) ){internasional_7 = internasional_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_internasional_5()) ){internasional_7 = internasional_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_im_internasional_1()) ){internasional_8 = internasional_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_internasional_2()) ){internasional_8 = internasional_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_internasional_3()) ){internasional_8 = internasional_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_internasional_4()) ){internasional_8 = internasional_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_internasional_5()) ){internasional_8 = internasional_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_im_internasional_1()) ){internasional_9 = internasional_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_internasional_2()) ){internasional_9 = internasional_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_internasional_3()) ){internasional_9 = internasional_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_internasional_4()) ){internasional_9 = internasional_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_internasional_5()) ){internasional_9 = internasional_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_im_internasional_1()) ){internasional_10 = internasional_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_internasional_2()) ){internasional_10 = internasional_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_internasional_3()) ){internasional_10 = internasional_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_internasional_4()) ){internasional_10 = internasional_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_internasional_5()) ){internasional_10 = internasional_10 + 1;}
		
		if( new Integer("11").equals(kbrand.getMkb_im_internasional_1()) ){internasional_11 = internasional_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_internasional_2()) ){internasional_11 = internasional_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_internasional_3()) ){internasional_11 = internasional_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_internasional_4()) ){internasional_11 = internasional_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_internasional_5()) ){internasional_11 = internasional_11 + 1;}
		
		if( internasional_1 > 1 || internasional_2 > 1 || internasional_3 > 1 || internasional_4 > 1 || internasional_5 > 1 || internasional_6 > 1 || internasional_7 > 1 || internasional_8 > 1 || internasional_9 > 1 || internasional_10 > 1 || internasional_11 > 1){
			err.rejectValue( "mkb_im_internasional_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda mencerminkan 'kesan internasional' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		Integer positif_1 = 0, positif_2 = 0, positif_3 = 0, positif_4 = 0, positif_5 = 0, positif_6 = 0, positif_7 = 0, positif_8 = 0, positif_9 = 0, positif_10 = 0, positif_11 = 0;
		if( new Integer("1").equals(kbrand.getMkb_im_positif_1()) ){positif_1 = positif_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_positif_2()) ){positif_1 = positif_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_positif_3()) ){positif_1 = positif_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_positif_4()) ){positif_1 = positif_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_positif_5()) ){positif_1 = positif_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_im_positif_1()) ){positif_2 = positif_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_positif_2()) ){positif_2 = positif_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_positif_3()) ){positif_2 = positif_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_positif_4()) ){positif_2 = positif_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_positif_5()) ){positif_2 = positif_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_im_positif_1()) ){positif_3 = positif_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_positif_2()) ){positif_3 = positif_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_positif_3()) ){positif_3 = positif_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_positif_4()) ){positif_3 = positif_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_positif_5()) ){positif_3 = positif_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_im_positif_1()) ){positif_4 = positif_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_positif_2()) ){positif_4 = positif_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_positif_3()) ){positif_4 = positif_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_positif_4()) ){positif_4 = positif_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_positif_5()) ){positif_4 = positif_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_im_positif_1()) ){positif_5 = positif_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_positif_2()) ){positif_5 = positif_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_positif_3()) ){positif_5 = positif_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_positif_4()) ){positif_5 = positif_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_positif_5()) ){positif_5 = positif_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_im_positif_1()) ){positif_6 = positif_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_positif_2()) ){positif_6 = positif_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_positif_3()) ){positif_6 = positif_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_positif_4()) ){positif_6 = positif_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_positif_5()) ){positif_6 = positif_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_im_positif_1()) ){positif_7 = positif_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_positif_2()) ){positif_7 = positif_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_positif_3()) ){positif_7 = positif_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_positif_4()) ){positif_7 = positif_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_positif_5()) ){positif_7 = positif_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_im_positif_1()) ){positif_8 = positif_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_positif_2()) ){positif_8 = positif_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_positif_3()) ){positif_8 = positif_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_positif_4()) ){positif_8 = positif_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_positif_5()) ){positif_8 = positif_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_im_positif_1()) ){positif_9 = positif_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_positif_2()) ){positif_9 = positif_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_positif_3()) ){positif_9 = positif_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_positif_4()) ){positif_9 = positif_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_positif_5()) ){positif_9 = positif_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_im_positif_1()) ){positif_10 = positif_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_positif_2()) ){positif_10 = positif_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_positif_3()) ){positif_10 = positif_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_positif_4()) ){positif_10 = positif_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_positif_5()) ){positif_10 = positif_10 + 1;}
		
		if( new Integer("11").equals(kbrand.getMkb_im_positif_1()) ){positif_11 = positif_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_positif_2()) ){positif_11 = positif_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_positif_3()) ){positif_11 = positif_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_positif_4()) ){positif_11 = positif_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_positif_5()) ){positif_11 = positif_11 + 1;}
		
		if( positif_1 > 1 || positif_2 > 1 || positif_3 > 1 || positif_4 > 1 || positif_5 > 1 || positif_6 > 1 || positif_7 > 1 || positif_8 > 1 || positif_9 > 1 || positif_10 > 1 || positif_11 > 1){
			err.rejectValue( "mkb_im_positif_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan 'sikap yang positif' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		
		Integer moderen_1 = 0, moderen_2 = 0, moderen_3 = 0, moderen_4 = 0, moderen_5 = 0, moderen_6 = 0, moderen_7 = 0, moderen_8 = 0, moderen_9 = 0, moderen_10 = 0, moderen_11 = 0;
		if( new Integer("1").equals(kbrand.getMkb_im_moderen_1()) ){moderen_1 = moderen_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_moderen_2()) ){moderen_1 = moderen_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_moderen_3()) ){moderen_1 = moderen_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_moderen_4()) ){moderen_1 = moderen_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_moderen_5()) ){moderen_1 = moderen_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_im_moderen_1()) ){moderen_2 = moderen_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_moderen_2()) ){moderen_2 = moderen_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_moderen_3()) ){moderen_2 = moderen_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_moderen_4()) ){moderen_2 = moderen_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_moderen_5()) ){moderen_2 = moderen_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_im_moderen_1()) ){moderen_3 = moderen_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_moderen_2()) ){moderen_3 = moderen_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_moderen_3()) ){moderen_3 = moderen_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_moderen_4()) ){moderen_3 = moderen_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_moderen_5()) ){moderen_3 = moderen_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_im_moderen_1()) ){moderen_4 = moderen_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_moderen_2()) ){moderen_4 = moderen_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_moderen_3()) ){moderen_4 = moderen_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_moderen_4()) ){moderen_4 = moderen_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_moderen_5()) ){moderen_4 = moderen_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_im_moderen_1()) ){moderen_5 = moderen_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_moderen_2()) ){moderen_5 = moderen_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_moderen_3()) ){moderen_5 = moderen_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_moderen_4()) ){moderen_5 = moderen_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_moderen_5()) ){moderen_5 = moderen_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_im_moderen_1()) ){moderen_6 = moderen_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_moderen_2()) ){moderen_6 = moderen_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_moderen_3()) ){moderen_6 = moderen_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_moderen_4()) ){moderen_6 = moderen_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_moderen_5()) ){moderen_6 = moderen_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_im_moderen_1()) ){moderen_7 = moderen_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_moderen_2()) ){moderen_7 = moderen_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_moderen_3()) ){moderen_7 = moderen_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_moderen_4()) ){moderen_7 = moderen_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_moderen_5()) ){moderen_7 = moderen_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_im_moderen_1()) ){moderen_8 = moderen_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_moderen_2()) ){moderen_8 = moderen_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_moderen_3()) ){moderen_8 = moderen_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_moderen_4()) ){moderen_8 = moderen_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_moderen_5()) ){moderen_8 = moderen_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_im_moderen_1()) ){moderen_9 = moderen_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_moderen_2()) ){moderen_9 = moderen_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_moderen_3()) ){moderen_9 = moderen_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_moderen_4()) ){moderen_9 = moderen_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_moderen_5()) ){moderen_9 = moderen_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_im_moderen_1()) ){moderen_10 = moderen_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_moderen_2()) ){moderen_10 = moderen_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_moderen_3()) ){moderen_10 = moderen_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_moderen_4()) ){moderen_10 = moderen_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_moderen_5()) ){moderen_10 = moderen_10 + 1;}
		
		if( new Integer("11").equals(kbrand.getMkb_im_moderen_1()) ){moderen_11 = moderen_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_moderen_2()) ){moderen_11 = moderen_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_moderen_3()) ){moderen_11 = moderen_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_moderen_4()) ){moderen_11 = moderen_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_moderen_5()) ){moderen_11 = moderen_11 + 1;}
		
		if( moderen_1 > 1 || moderen_2 > 1 || moderen_3 > 1 || moderen_4 > 1 || moderen_5 > 1 || moderen_6 > 1 || moderen_7 > 1 || moderen_8 > 1 || moderen_9 > 1 || moderen_10 > 1 || moderen_11 > 1){
			err.rejectValue( "mkb_im_moderen_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan kesan yang 'moderen' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		
		Integer komitmen_1 = 0, komitmen_2 = 0, komitmen_3 = 0, komitmen_4 = 0, komitmen_5 = 0, komitmen_6 = 0, komitmen_7 = 0, komitmen_8 = 0, komitmen_9 = 0, komitmen_10 = 0, komitmen_11 = 0;
		if( new Integer("1").equals(kbrand.getMkb_im_komitmen_1()) ){komitmen_1 = komitmen_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_komitmen_2()) ){komitmen_1 = komitmen_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_komitmen_3()) ){komitmen_1 = komitmen_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_komitmen_4()) ){komitmen_1 = komitmen_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_im_komitmen_5()) ){komitmen_1 = komitmen_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_im_komitmen_1()) ){komitmen_2 = komitmen_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_komitmen_2()) ){komitmen_2 = komitmen_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_komitmen_3()) ){komitmen_2 = komitmen_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_komitmen_4()) ){komitmen_2 = komitmen_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_im_komitmen_5()) ){komitmen_2 = komitmen_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_im_komitmen_1()) ){komitmen_3 = komitmen_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_komitmen_2()) ){komitmen_3 = komitmen_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_komitmen_3()) ){komitmen_3 = komitmen_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_komitmen_4()) ){komitmen_3 = komitmen_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_im_komitmen_5()) ){komitmen_3 = komitmen_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_im_komitmen_1()) ){komitmen_4 = komitmen_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_komitmen_2()) ){komitmen_4 = komitmen_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_komitmen_3()) ){komitmen_4 = komitmen_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_komitmen_4()) ){komitmen_4 = komitmen_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_im_komitmen_5()) ){komitmen_4 = komitmen_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_im_komitmen_1()) ){komitmen_5 = komitmen_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_komitmen_2()) ){komitmen_5 = komitmen_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_komitmen_3()) ){komitmen_5 = komitmen_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_komitmen_4()) ){komitmen_5 = komitmen_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_im_komitmen_5()) ){komitmen_5 = komitmen_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_im_komitmen_1()) ){komitmen_6 = komitmen_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_komitmen_2()) ){komitmen_6 = komitmen_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_komitmen_3()) ){komitmen_6 = komitmen_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_komitmen_4()) ){komitmen_6 = komitmen_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_im_komitmen_5()) ){komitmen_6 = komitmen_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_im_komitmen_1()) ){komitmen_7 = komitmen_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_komitmen_2()) ){komitmen_7 = komitmen_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_komitmen_3()) ){komitmen_7 = komitmen_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_komitmen_4()) ){komitmen_7 = komitmen_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_im_komitmen_5()) ){komitmen_7 = komitmen_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_im_komitmen_1()) ){komitmen_8 = komitmen_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_komitmen_2()) ){komitmen_8 = komitmen_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_komitmen_3()) ){komitmen_8 = komitmen_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_komitmen_4()) ){komitmen_8 = komitmen_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_im_komitmen_5()) ){komitmen_8 = komitmen_8 + 1;}
		
		if( new Integer("9").equals(kbrand.getMkb_im_komitmen_1()) ){komitmen_9 = komitmen_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_komitmen_2()) ){komitmen_9 = komitmen_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_komitmen_3()) ){komitmen_9 = komitmen_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_komitmen_4()) ){komitmen_9 = komitmen_9 + 1;}
		if( new Integer("9").equals(kbrand.getMkb_im_komitmen_5()) ){komitmen_9 = komitmen_9 + 1;}
		
		if( new Integer("10").equals(kbrand.getMkb_im_komitmen_1()) ){komitmen_10 = komitmen_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_komitmen_2()) ){komitmen_10 = komitmen_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_komitmen_3()) ){komitmen_10 = komitmen_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_komitmen_4()) ){komitmen_10 = komitmen_10 + 1;}
		if( new Integer("10").equals(kbrand.getMkb_im_komitmen_5()) ){komitmen_10 = komitmen_10 + 1;}
		
		if( new Integer("11").equals(kbrand.getMkb_im_komitmen_1()) ){komitmen_11 = komitmen_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_komitmen_2()) ){komitmen_11 = komitmen_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_komitmen_3()) ){komitmen_11 = komitmen_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_komitmen_4()) ){komitmen_11 = komitmen_11 + 1;}
		if( new Integer("11").equals(kbrand.getMkb_im_komitmen_5()) ){komitmen_11 = komitmen_11 + 1;}
		
		if( komitmen_1 > 1 || komitmen_2 > 1 || komitmen_3 > 1 || komitmen_4 > 1 || komitmen_5 > 1 || komitmen_6 > 1 || komitmen_7 > 1 || komitmen_8 > 1 || komitmen_9 > 1 || komitmen_10 > 1 || komitmen_11 > 1){
			err.rejectValue( "mkb_im_komitmen_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan 'komitmen' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		Integer waspada_1 = 0, waspada_2 = 0, waspada_3 = 0, waspada_4 = 0, waspada_5 = 0, waspada_6 = 0, waspada_7 = 0, waspada_8 = 0;
		if( new Integer("1").equals(kbrand.getMkb_em_waspada_1()) ){waspada_1 = waspada_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_waspada_2()) ){waspada_1 = waspada_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_waspada_3()) ){waspada_1 = waspada_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_waspada_4()) ){waspada_1 = waspada_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_waspada_5()) ){waspada_1 = waspada_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_em_waspada_1()) ){waspada_2 = waspada_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_waspada_2()) ){waspada_2 = waspada_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_waspada_3()) ){waspada_2 = waspada_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_waspada_4()) ){waspada_2 = waspada_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_waspada_5()) ){waspada_2 = waspada_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_em_waspada_1()) ){waspada_3 = waspada_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_waspada_2()) ){waspada_3 = waspada_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_waspada_3()) ){waspada_3 = waspada_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_waspada_4()) ){waspada_3 = waspada_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_waspada_5()) ){waspada_3 = waspada_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_em_waspada_1()) ){waspada_4 = waspada_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_waspada_2()) ){waspada_4 = waspada_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_waspada_3()) ){waspada_4 = waspada_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_waspada_4()) ){waspada_4 = waspada_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_waspada_5()) ){waspada_4 = waspada_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_em_waspada_1()) ){waspada_5 = waspada_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_waspada_2()) ){waspada_5 = waspada_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_waspada_3()) ){waspada_5 = waspada_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_waspada_4()) ){waspada_5 = waspada_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_waspada_5()) ){waspada_5 = waspada_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_em_waspada_1()) ){waspada_6 = waspada_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_waspada_2()) ){waspada_6 = waspada_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_waspada_3()) ){waspada_6 = waspada_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_waspada_4()) ){waspada_6 = waspada_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_waspada_5()) ){waspada_6 = waspada_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_em_waspada_1()) ){waspada_7 = waspada_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_waspada_2()) ){waspada_7 = waspada_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_waspada_3()) ){waspada_7 = waspada_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_waspada_4()) ){waspada_7 = waspada_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_waspada_5()) ){waspada_7 = waspada_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_em_waspada_1()) ){waspada_8 = waspada_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_waspada_2()) ){waspada_8 = waspada_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_waspada_3()) ){waspada_8 = waspada_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_waspada_4()) ){waspada_8 = waspada_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_waspada_5()) ){waspada_8 = waspada_8 + 1;}
		
		if( waspada_1 > 1 || waspada_2 > 1 || waspada_3 > 1 || waspada_4 > 1 || waspada_5 > 1 || waspada_6 > 1 || waspada_7 > 1 || waspada_8 > 1){
			err.rejectValue( "mkb_em_waspada_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda mencerminkan 'mitra yang waspada' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		
		Integer kenyamanan_1 = 0, kenyamanan_2 = 0, kenyamanan_3 = 0, kenyamanan_4 = 0, kenyamanan_5 = 0, kenyamanan_6 = 0, kenyamanan_7 = 0, kenyamanan_8 = 0;
		if( new Integer("1").equals(kbrand.getMkb_em_kenyamanan_1()) ){kenyamanan_1 = kenyamanan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_kenyamanan_2()) ){kenyamanan_1 = kenyamanan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_kenyamanan_3()) ){kenyamanan_1 = kenyamanan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_kenyamanan_4()) ){kenyamanan_1 = kenyamanan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_kenyamanan_5()) ){kenyamanan_1 = kenyamanan_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_em_kenyamanan_1()) ){kenyamanan_2 = kenyamanan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_kenyamanan_2()) ){kenyamanan_2 = kenyamanan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_kenyamanan_3()) ){kenyamanan_2 = kenyamanan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_kenyamanan_4()) ){kenyamanan_2 = kenyamanan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_kenyamanan_5()) ){kenyamanan_2 = kenyamanan_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_em_kenyamanan_1()) ){kenyamanan_3 = kenyamanan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_kenyamanan_2()) ){kenyamanan_3 = kenyamanan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_kenyamanan_3()) ){kenyamanan_3 = kenyamanan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_kenyamanan_4()) ){kenyamanan_3 = kenyamanan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_kenyamanan_5()) ){kenyamanan_3 = kenyamanan_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_em_kenyamanan_1()) ){kenyamanan_4 = kenyamanan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_kenyamanan_2()) ){kenyamanan_4 = kenyamanan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_kenyamanan_3()) ){kenyamanan_4 = kenyamanan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_kenyamanan_4()) ){kenyamanan_4 = kenyamanan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_kenyamanan_5()) ){kenyamanan_4 = kenyamanan_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_em_kenyamanan_1()) ){kenyamanan_5 = kenyamanan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_kenyamanan_2()) ){kenyamanan_5 = kenyamanan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_kenyamanan_3()) ){kenyamanan_5 = kenyamanan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_kenyamanan_4()) ){kenyamanan_5 = kenyamanan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_kenyamanan_5()) ){kenyamanan_5 = kenyamanan_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_em_kenyamanan_1()) ){kenyamanan_6 = kenyamanan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_kenyamanan_2()) ){kenyamanan_6 = kenyamanan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_kenyamanan_3()) ){kenyamanan_6 = kenyamanan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_kenyamanan_4()) ){kenyamanan_6 = kenyamanan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_kenyamanan_5()) ){kenyamanan_6 = kenyamanan_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_em_kenyamanan_1()) ){kenyamanan_7 = kenyamanan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_kenyamanan_2()) ){kenyamanan_7 = kenyamanan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_kenyamanan_3()) ){kenyamanan_7 = kenyamanan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_kenyamanan_4()) ){kenyamanan_7 = kenyamanan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_kenyamanan_5()) ){kenyamanan_7 = kenyamanan_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_em_kenyamanan_1()) ){kenyamanan_8 = kenyamanan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_kenyamanan_2()) ){kenyamanan_8 = kenyamanan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_kenyamanan_3()) ){kenyamanan_8 = kenyamanan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_kenyamanan_4()) ){kenyamanan_8 = kenyamanan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_kenyamanan_5()) ){kenyamanan_8 = kenyamanan_8 + 1;}
		
		if( kenyamanan_1 > 1 || kenyamanan_2 > 1  || kenyamanan_3 > 1 || kenyamanan_4 > 1 || kenyamanan_5 > 1 || kenyamanan_6 > 1 || kenyamanan_7 > 1 || kenyamanan_8 > 1){
			err.rejectValue( "mkb_em_kenyamanan_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan 'kenyamanan' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		
		Integer pengertian_1 = 0, pengertian_2 = 0, pengertian_3 = 0, pengertian_4 = 0, pengertian_5 = 0, pengertian_6 = 0, pengertian_7 = 0, pengertian_8 = 0;
		if( new Integer("1").equals(kbrand.getMkb_em_pengertian_1()) ){pengertian_1 = pengertian_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_pengertian_2()) ){pengertian_1 = pengertian_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_pengertian_3()) ){pengertian_1 = pengertian_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_pengertian_4()) ){pengertian_1 = pengertian_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_pengertian_5()) ){pengertian_1 = pengertian_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_em_pengertian_1()) ){pengertian_2 = pengertian_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_pengertian_2()) ){pengertian_2 = pengertian_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_pengertian_3()) ){pengertian_2 = pengertian_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_pengertian_4()) ){pengertian_2 = pengertian_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_pengertian_5()) ){pengertian_2 = pengertian_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_em_pengertian_1()) ){pengertian_3 = pengertian_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_pengertian_2()) ){pengertian_3 = pengertian_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_pengertian_3()) ){pengertian_3 = pengertian_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_pengertian_4()) ){pengertian_3 = pengertian_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_pengertian_5()) ){pengertian_3 = pengertian_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_em_pengertian_1()) ){pengertian_4 = pengertian_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_pengertian_2()) ){pengertian_4 = pengertian_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_pengertian_3()) ){pengertian_4 = pengertian_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_pengertian_4()) ){pengertian_4 = pengertian_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_pengertian_5()) ){pengertian_4 = pengertian_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_em_pengertian_1()) ){pengertian_5 = pengertian_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_pengertian_2()) ){pengertian_5 = pengertian_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_pengertian_3()) ){pengertian_5 = pengertian_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_pengertian_4()) ){pengertian_5 = pengertian_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_pengertian_5()) ){pengertian_5 = pengertian_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_em_pengertian_1()) ){pengertian_6 = pengertian_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_pengertian_2()) ){pengertian_6 = pengertian_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_pengertian_3()) ){pengertian_6 = pengertian_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_pengertian_4()) ){pengertian_6 = pengertian_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_pengertian_5()) ){pengertian_6 = pengertian_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_em_pengertian_1()) ){pengertian_7 = pengertian_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_pengertian_2()) ){pengertian_7 = pengertian_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_pengertian_3()) ){pengertian_7 = pengertian_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_pengertian_4()) ){pengertian_7 = pengertian_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_pengertian_5()) ){pengertian_7 = pengertian_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_em_pengertian_1()) ){pengertian_8 = pengertian_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_pengertian_2()) ){pengertian_8 = pengertian_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_pengertian_3()) ){pengertian_8 = pengertian_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_pengertian_4()) ){pengertian_8 = pengertian_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_pengertian_5()) ){pengertian_8 = pengertian_8 + 1;}
		
		if( pengertian_1 > 1 || pengertian_2 > 1 || pengertian_3 > 1 || pengertian_4 > 1 || pengertian_5 > 1 || pengertian_6 > 1 || pengertian_7 > 1 || pengertian_8 > 1){
			err.rejectValue( "mkb_em_pengertian_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan 'pengertian' ?"}, "error.fieldCantMoreThanOne" );
		}
		
		
		Integer ketulusan_1 = 0, ketulusan_2 = 0, ketulusan_3 = 0, ketulusan_4 = 0, ketulusan_5 = 0, ketulusan_6 = 0, ketulusan_7 = 0, ketulusan_8 = 0;
		if( new Integer("1").equals(kbrand.getMkb_em_ketulusan_1()) ){ketulusan_1 = ketulusan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_ketulusan_2()) ){ketulusan_1 = ketulusan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_ketulusan_3()) ){ketulusan_1 = ketulusan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_ketulusan_4()) ){ketulusan_1 = ketulusan_1 + 1;}
		if( new Integer("1").equals(kbrand.getMkb_em_ketulusan_5()) ){ketulusan_1 = ketulusan_1 + 1;}
		
		if( new Integer("2").equals(kbrand.getMkb_em_ketulusan_1()) ){ketulusan_2 = ketulusan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_ketulusan_2()) ){ketulusan_2 = ketulusan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_ketulusan_3()) ){ketulusan_2 = ketulusan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_ketulusan_4()) ){ketulusan_2 = ketulusan_2 + 1;}
		if( new Integer("2").equals(kbrand.getMkb_em_ketulusan_5()) ){ketulusan_2 = ketulusan_2 + 1;}
		
		if( new Integer("3").equals(kbrand.getMkb_em_ketulusan_1()) ){ketulusan_3 = ketulusan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_ketulusan_2()) ){ketulusan_3 = ketulusan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_ketulusan_3()) ){ketulusan_3 = ketulusan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_ketulusan_4()) ){ketulusan_3 = ketulusan_3 + 1;}
		if( new Integer("3").equals(kbrand.getMkb_em_ketulusan_5()) ){ketulusan_3 = ketulusan_3 + 1;}
		
		if( new Integer("4").equals(kbrand.getMkb_em_ketulusan_1()) ){ketulusan_4 = ketulusan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_ketulusan_2()) ){ketulusan_4 = ketulusan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_ketulusan_3()) ){ketulusan_4 = ketulusan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_ketulusan_4()) ){ketulusan_4 = ketulusan_4 + 1;}
		if( new Integer("4").equals(kbrand.getMkb_em_ketulusan_5()) ){ketulusan_4 = ketulusan_4 + 1;}
		
		if( new Integer("5").equals(kbrand.getMkb_em_ketulusan_1()) ){ketulusan_5 = ketulusan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_ketulusan_2()) ){ketulusan_5 = ketulusan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_ketulusan_3()) ){ketulusan_5 = ketulusan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_ketulusan_4()) ){ketulusan_5 = ketulusan_5 + 1;}
		if( new Integer("5").equals(kbrand.getMkb_em_ketulusan_5()) ){ketulusan_5 = ketulusan_5 + 1;}
		
		if( new Integer("6").equals(kbrand.getMkb_em_ketulusan_1()) ){ketulusan_6 = ketulusan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_ketulusan_2()) ){ketulusan_6 = ketulusan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_ketulusan_3()) ){ketulusan_6 = ketulusan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_ketulusan_4()) ){ketulusan_6 = ketulusan_6 + 1;}
		if( new Integer("6").equals(kbrand.getMkb_em_ketulusan_5()) ){ketulusan_6 = ketulusan_6 + 1;}
		
		if( new Integer("7").equals(kbrand.getMkb_em_ketulusan_1()) ){ketulusan_7 = ketulusan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_ketulusan_2()) ){ketulusan_7 = ketulusan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_ketulusan_3()) ){ketulusan_7 = ketulusan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_ketulusan_4()) ){ketulusan_7 = ketulusan_7 + 1;}
		if( new Integer("7").equals(kbrand.getMkb_em_ketulusan_5()) ){ketulusan_7 = ketulusan_7 + 1;}

		if( new Integer("8").equals(kbrand.getMkb_em_ketulusan_1()) ){ketulusan_8 = ketulusan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_ketulusan_2()) ){ketulusan_8 = ketulusan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_ketulusan_3()) ){ketulusan_8 = ketulusan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_ketulusan_4()) ){ketulusan_8 = ketulusan_8 + 1;}
		if( new Integer("8").equals(kbrand.getMkb_em_ketulusan_5()) ){ketulusan_8 = ketulusan_8 + 1;}
		
		if( ketulusan_1 > 1 || ketulusan_2 > 1 || ketulusan_3 > 1 || ketulusan_4 > 1 || ketulusan_5 > 1 || ketulusan_6 > 1 || ketulusan_7 > 1 || ketulusan_8 > 1){
			err.rejectValue( "mkb_em_ketulusan_1", "error.fieldCantMoreThanOne", new Object[] {"Manakah yang menurut anda menggambarkan 'ketulusan' ?"}, "error.fieldCantMoreThanOne" );
		}
	}
		
	protected Map referenceData(HttpServletRequest request) throws Exception {
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", ServletRequestUtils.getStringParameter(request, "editSPAJ", ""));
		Map refData = null;
		refData = new HashMap();
		return refData;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		KuesionerBrand kbrand=(KuesionerBrand)cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map m = new HashMap();
		Date now = elionsManager.selectSysdate();
		String pesan = "";
		KuesionerBrand temp = uwManager.selectKuesionerBrand(currentUser.getLus_id());
		if( temp != null ){
			pesan = uwManager.updateMstKuesionerBrand(kbrand, now, currentUser.getLus_id());
		}else{
			pesan = uwManager.insertMstKuesionerBrand(kbrand, now, currentUser.getLus_id());
			
		}
		return new ModelAndView(new RedirectView(request.getContextPath()+"/common/questionnaire.htm")).addObject("pesan", pesan);
//		return new ModelAndView("common/questionnaire",m);
	}
	
}