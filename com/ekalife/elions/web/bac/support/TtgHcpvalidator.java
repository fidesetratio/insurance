package com.ekalife.elions.web.bac.support;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.service.ElionsManager;

/**
 * @author HEMILDA
 * validator ttpanpremiviewerController
 */	
public class TtgHcpvalidator implements Validator {
	
	protected final Log logger = LogFactory.getLog( getClass() );

	private ElionsManager elionsManager;
	private DateFormat defaultDateFormat;
	
	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}
	
	public boolean supports(Class data) {
		return Cmdeditbac.class.isAssignableFrom(data);
	}

	public void validate(Object cmd, Errors err) {
	Cmdeditbac edit= (Cmdeditbac) cmd;
	try {
		Map data = this.elionsManager.validbac(edit, err, "hcp","windowhcp");
		/*String a = "";
		a= (String)data.get("lsre_id");
		if (a != null)
		{
			err.rejectValue("datausulan.daftahcp[1].lsre_id", "", a);
		}
		a = (String)data.get("plan_rider");
		if (a != null)
		{
			err.rejectValue("datausulan.daftahcp[1].plan_rider", "", a);
		}
		a = (String) data.get("nama");
		if (a != null)
		{
			err.rejectValue("datausulan.daftahcp[1].nama", "", a);
		}
		a= (String) data.get("tgl_lahir");
		if (a != null)
		{
			err.rejectValue("datausulan.daftahcp[1].tgl_lahir", "", a);
		}
		a= (String) data.get("umur");
		if (a != null)
		{
			err.rejectValue("datausulan.daftahcp[1].umur", "", a);
		}
		a= (String)data.get("total_persen");
		if (a != null)
		{
			err.rejectValue("investasiutama.total_persen", "", a);
		}
		a=(String) data.get("premi_tunggal");
		if (a != null)
		{
			err.rejectValue("investasiutama.daftartopup.premi_tunggal", "", a);
		}
		a = (String)data.get("premi_berkala");
		if (a != null)
		{
			err.rejectValue("investasiutama.daftartopup.premi_berkala", "", a);
		}
		a= (String)data.get("pil_berkala");
		if (a != null)
		{
			err.rejectValue("investasiutama.daftartopup.pil_berkala", "", a);
		}
		a= (String)data.get("pil_tunggal");
		if (a != null)
		{
			err.rejectValue("investasiutama.daftartopup.pil_tunggal", "", a);
		}*/
	} catch (ServletException e) {
		logger.error("ERROR :", e);
	} catch (IOException e) {
		logger.error("ERROR :", e);
	} catch (Exception e) {
		logger.error("ERROR :", e);
	}
	
	}

}
