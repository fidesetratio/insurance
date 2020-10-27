package com.ekalife.elions.web.bac.support;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ekalife.elions.model.DetailPembayaran;
import com.ekalife.elions.service.ElionsManager;

/**
 * @author HEMILDA
 * validator ttpanpremiviewerController
 */	
public class Ttpanpremivalidator implements Validator {

	private ElionsManager elionsManager;
	
	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

	public boolean supports(Class data) {
		return DetailPembayaran.class.isAssignableFrom(data);
	}

	public void validate(Object cmd, Errors err) {
		DetailPembayaran det = (DetailPembayaran)cmd;
		/*if (det.getKeterangan().equalsIgnoreCase(""))
		{
			err.rejectValue("keterangan","","Keterangan tidak boleh kosong");
		}*/


	}



}
