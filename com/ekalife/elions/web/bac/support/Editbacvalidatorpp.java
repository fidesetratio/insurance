package com.ekalife.elions.web.bac.support;

import id.co.sinarmaslife.std.spring.util.Email;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Hadiah;
import com.ekalife.elions.model.Hcp;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.PesertaPlus_mix;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Simas;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.SumberKyc;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.Products;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.f_validasi;

/**
 * @author Canpri
 * validator editbacControllerBaru
 * untuk validasi penginputan bac
 */

public class Editbacvalidatorpp implements Validator{

	protected final Log logger = LogFactory.getLog(getClass());
	
	private ElionsManager elionsManager;
	private UwManager uwManager;
	private BacManager bacManager;
	private DateFormat defaultDateFormat;
	private Email email;
	private Products products;
	private Editbacdetvalidator detVal;
	
	public Editbacdetvalidator getDetVal() {
		return detVal;
	}

	public void setDetVal(Editbacdetvalidator detVal) {
		this.detVal = detVal;
	}

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}

	public void setEmail(Email email) {
		this.email = email;
	}
	
	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}
	
	public void setUwManager(UwManager uwManager) {
		this.uwManager = uwManager;
	}
	
	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
	}

	public boolean supports(Class data) {
		return Cmdeditbac.class.isAssignableFrom(data);
	}

	public void validate(Object cmd, Errors err) {
		logger.info("Validate Pemegang");
		detVal.Tes();
	}
	
	public void validatepp(Object cmd, Errors err) {
		Cmdeditbac pp = (Cmdeditbac) cmd;
		/*if(pp.getAlamat_kantor() == null || pp.getAlamat_kantor().trim().equals("")) {
			err.rejectValue("pemegang.alamat_kantor", "", "Alamat kantor harap diisi");
		}*/
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mcl_first", "", "Nama harap diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.alamat_kantor", "", "Alamat Kantor harap diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_no_identity", "", "No. KTP/PASPOR/KITAS/SIM harap diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_mother", "", "Nama Ibu Kandung harap diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_no_identity_expired", "", "Tanggal berlaku harap diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_place_birth", "", "Kota dan Negara Kelahiran harap diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.mspe_date_birth", "", "Tanggal Kelahiran harap diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "pemegang.no_hp", "", "No. Handphone harap diisi");
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "addressbilling.msap_address", "", "Alamat Penagihan / Korespondensi harap diisi");
		
	}
	
}