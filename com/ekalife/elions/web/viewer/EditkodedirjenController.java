package com.ekalife.elions.web.viewer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;


import com.ekalife.elions.model.Stamp;
import com.ekalife.utils.parent.ParentFormController;
/**
 * @author HEMILDA
 * Controller titipan premi
 */

public class EditkodedirjenController extends ParentFormController {


	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Stamp bea = new Stamp();
		String kode = ServletRequestUtils.getStringParameter(request, "kode");
		bea.setKeterangan("");
		if (kode == null)
		{
			kode = "";
		}
		if (kode.equalsIgnoreCase(" "))
		{
			kode="";
		}
		bea.setKode(kode.toUpperCase());
		bea.setMstm_kode(kode.toUpperCase());		
		
		if (!kode.equalsIgnoreCase(""))
		{
			bea = (Stamp) this.elionsManager.detil_kode_stamp(kode);
		}
		return bea;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		Stamp bea = (Stamp)cmd;
		String kode = ServletRequestUtils.getStringParameter(request, "kode");
		bea.setKeterangan("");
		if (kode == null)
		{
			kode ="";
		}
		if (kode.equalsIgnoreCase(" "))
		{
			kode="";
		}
		bea.setKode(kode.toUpperCase());
		if (kode != null)
		{
			if (bea.getMstm_kode_dirjen() == null )
			{
				bea.setMstm_kode_dirjen("");
			}
			if (bea.getMstm_kode_dirjen().equalsIgnoreCase(""))
			{
				errors.rejectValue("mstm_kode_dirjen", "", "Silahkan masukkan kode dirjen terlebih dahulu.");
			}
		}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Stamp bea = (Stamp)cmd;
		bea.setMstm_kode_dirjen(bea.getMstm_kode_dirjen().toUpperCase());
		this.elionsManager.update_mst_kode_dirjen(bea);
		bea.setKeterangan("Data sudah berhasil disimpan.");
		return new ModelAndView("uw/viewer/editkodedirjen", err.getModel()).addAllObjects(this.referenceData(request));
	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception{
		return new ModelAndView("common/duplicate");
	}

}