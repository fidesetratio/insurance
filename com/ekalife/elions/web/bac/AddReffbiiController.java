package com.ekalife.elions.web.bac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.ReffBii;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author HEMILDA
 * Controller untuk menambah reff bii
 */
public class AddReffbiiController extends ParentFormController{

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		ReffBii reff = (ReffBii) cmd;
		Map refData= new HashMap();
		Long max = this.elionsManager.max_reff_bii();
		reff.setLrb_id(Long.toString(max.longValue()));
		reff.setAtas_nama("");
		reff.setCab_rek("");
		reff.setNama_reff("");
		reff.setNo_rek("");
		reff.setNpk("");		
		List daftarcabangbii = this.elionsManager.list_cabang_bii();
		refData.put("daftarcabangbii", daftarcabangbii);
		return refData ;
	}	
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		ReffBii reff = new ReffBii();
		Long max = this.elionsManager.max_reff_bii();
		reff.setLrb_id(Long.toString(max.longValue()));
		reff.setAtas_nama("");
		reff.setCab_rek("");
		reff.setNama_reff("");
		reff.setNo_rek("");
		reff.setNpk("");
		return reff;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		ReffBii datautama = (ReffBii)cmd;
		String nama_reff="";
		String lcb_no="";
		String no_rek="";
		String atas_nama="";
		String flag_aktif="";
		String npk="";
		String lrb_id="";
		String cab_rek="";
		nama_reff = datautama.getNama_reff();
		lcb_no = datautama.getLcb_no();
		no_rek = datautama.getNo_rek();
		atas_nama = datautama.getAtas_nama();
		flag_aktif = datautama.getFlag_aktif();
		npk = datautama.getNpk();
		lrb_id = datautama.getLrb_id();
		cab_rek = datautama.getCab_rek();
		datautama.setHit_err(new Integer(0));
				
		if (nama_reff==null)
		{
			nama_reff="";
		}
		if (nama_reff.trim().length()==0)
		{
			datautama.setHit_err(new Integer(1));
			errors.rejectValue("nama_reff","","Silahkan masukkan Nama Reff terlebih dahulu");
		}
		datautama.setNama_reff(nama_reff);
				
		if (no_rek==null)
		{
			no_rek="";
		}
		datautama.setNo_rek(no_rek);
				
		if (atas_nama==null)
		{
			atas_nama="";
		}
		datautama.setAtas_nama(atas_nama);
				
		if (npk==null)
		{
			npk="";
		}
		datautama.setNpk(npk);
				
		if (cab_rek==null)
		{
			cab_rek="";
		}
		datautama.setCab_rek(cab_rek);
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		ReffBii datautama = (ReffBii)cmd;
		String nama_reff="";
		String lcb_no="";
		String no_rek="";
		String atas_nama="";
		String flag_aktif="";
		String npk="";
		String lrb_id="";
		String cab_rek="";
		if (datautama.getHit_err().intValue()==0)
		{
			nama_reff = datautama.getNama_reff().toUpperCase();
			lcb_no = datautama.getLcb_no();
			no_rek = datautama.getNo_rek().toUpperCase();
			atas_nama = datautama.getAtas_nama().toUpperCase();
			flag_aktif = datautama.getFlag_aktif();
			npk = datautama.getNpk().toUpperCase();
			lrb_id = datautama.getLrb_id();
			cab_rek = datautama.getCab_rek().toUpperCase();
			this.elionsManager.insert_lst_reff_bii(nama_reff,lcb_no,no_rek,atas_nama,flag_aktif,npk,lrb_id,cab_rek);
			datautama.setStatussubmit("1");
		}
		return new ModelAndView("bac/addreffbii","cmd", datautama).addAllObjects(this.referenceData(request,cmd,err));
	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}
}