package com.ekalife.elions.web.bac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.ReffBii;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author HEMILDA
 * Controller reff bii
 */
public class EditReffbiiController extends ParentFormController{

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		ReffBii reff = (ReffBii) cmd;
		Map refData= new HashMap();
		String kunci = reff.getKunci();
		if (kunci == null)
		{
			kunci ="";
		}
		if (kunci.equalsIgnoreCase(" "))
		{
			kunci = "";
		}
		reff.setKunci(kunci);
		Integer jumlah_row = this.elionsManager.count_reff(kunci);
		reff.setJumlah_row(jumlah_row);
		Integer jumlah_halaman = new Integer(jumlah_row.intValue() / 20);
		Integer sisa_halaman = new Integer(jumlah_row.intValue() % 20);
		if (sisa_halaman.intValue()>0)
		{
			jumlah_halaman = new Integer(jumlah_halaman.intValue()+1);
		}
		reff.setJumlah_halaman(jumlah_halaman);
		//reff.setStatussubmit("0");
		/*if (reff.getAktif()==null)
		{
			reff.setAktif("0");
		}*/
		
		List daftarcabangbii = this.elionsManager.list_cabang_bii();
		refData.put("daftarcabangbii", daftarcabangbii);
		return refData ;
	}	
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		ReffBii reff = new ReffBii();
		Integer halaman = ServletRequestUtils.getIntParameter(request, "halaman");
		String kunci = ServletRequestUtils.getStringParameter(request, "kunci");
		
		//String kunci = reff.getKunci();
		if (kunci == null)
		{
			kunci = "";
		}
		if (kunci.equalsIgnoreCase(" "))
		{
			kunci="";
		}
		reff.setKunci(kunci);
		if (halaman == null)
		{
			halaman = new Integer(1);
		}
		reff.setHalaman_aktif(halaman);
		Integer number1 = new Integer(0);
		Integer number2 = new Integer(0);
		number1 = new Integer(((halaman.intValue()-1)*20)+1);
		number2 = new Integer(number1.intValue()+19);
		if (kunci =="")
		{
			reff.setDaftarreffbii(this.elionsManager.list_reff1(number1,number2));
		}else{
			reff.setDaftarreffbii(this.elionsManager.list_reff(number1,number2,kunci));
		}
		return reff;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		ReffBii datautama = (ReffBii)cmd;
		Integer halaman = ServletRequestUtils.getIntParameter(request, "halaman");
		String kunci = ServletRequestUtils.getStringParameter(request, "kunci");
		//String kunci = datautama.getKunci();
		if (kunci == null)
		{
			kunci ="";
		}
		if (kunci.equalsIgnoreCase(" "))
		{
			kunci="";
		}
		datautama.setKunci(kunci);
		if (halaman == null)
		{
			halaman = new Integer(1);
		}
		datautama.setHalaman_aktif(halaman);
		
		List listreffbii = datautama.getDaftarreffbii();
		String nama_reff="";
		String lcb_no="";
		String no_rek="";
		String atas_nama="";
		String flag_aktif="";
		String npk="";
		String lrb_id="";
		String cab_rek="";
		datautama.setHit_err(new Integer(0));
		
		Integer row = new Integer(20);
		Integer jumlah_row = this.elionsManager.count_reff(kunci);
		Integer jumlah_halaman = new Integer(jumlah_row.intValue() / 20);
		Integer sisa_halaman = new Integer(jumlah_row.intValue() % 20);
		if (sisa_halaman.intValue()>0)
		{
			jumlah_halaman = new Integer(jumlah_halaman.intValue()+1);
			if (halaman.intValue()== jumlah_halaman.intValue())
			{
				row = sisa_halaman;
			}
		}		
		
		for (int k = 0 ;k < row.intValue(); k++)
		{
			ReffBii dftreffbii= (ReffBii)listreffbii.get(k);
			if (dftreffbii.getFlag_edit().intValue()==1)
			{
				nama_reff = dftreffbii.getNama_reff();
				lcb_no = dftreffbii.getLcb_no();
				no_rek = dftreffbii.getNo_rek();
				atas_nama = dftreffbii.getAtas_nama();
				flag_aktif = dftreffbii.getFlag_aktif();
				npk = dftreffbii.getNpk();
				lrb_id = dftreffbii.getLrb_id();
				cab_rek = dftreffbii.getCab_rek();
				dftreffbii.setHit_err(new Integer(0));
				
				if (nama_reff==null)
				{
					nama_reff="";
				}
				if (nama_reff.trim().length()==0)
				{
					dftreffbii.setHit_err(new Integer(1));
					errors.rejectValue("nama_reff","","Silahkan masukkan Nama Reff ke "+(k+1)+" terlebih dahulu");
				}
				dftreffbii.setNama_reff(nama_reff);
				
				if (no_rek==null)
				{
					no_rek="";
				}
				dftreffbii.setNo_rek(no_rek);
				
				if (atas_nama==null)
				{
					atas_nama="";
				}
				dftreffbii.setAtas_nama(atas_nama);
				
				if (npk==null)
				{
					npk="";
				}
				dftreffbii.setNpk(npk);
				
				if (cab_rek==null)
				{
					cab_rek="";
				}
				dftreffbii.setCab_rek(cab_rek);
			}
		}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		ReffBii datautama = (ReffBii)cmd;
		List listreffbii = datautama.getDaftarreffbii();
		datautama.setStatussubmit("0");
		String nama_reff="";
		String lcb_no="";
		String no_rek="";
		String atas_nama="";
		String flag_aktif="";
		String npk="";
		String lrb_id="";
		String cab_rek="";

		String kunci = datautama.getKunci();
		Integer halaman = datautama.getHalaman_aktif();
		Integer row = new Integer(20);
		Integer jumlah_row = this.elionsManager.count_reff(kunci);
		Integer jumlah_halaman = new Integer(jumlah_row.intValue() / 20);
		Integer sisa_halaman = new Integer(jumlah_row.intValue() % 20);
		if (sisa_halaman.intValue()>0)
		{
			jumlah_halaman = new Integer(jumlah_halaman.intValue()+1);
			if (halaman.intValue()== jumlah_halaman.intValue())
			{
				row = sisa_halaman;
			}
		}		
		
		for (int k = 0 ;k <  row.intValue(); k++)
		{
			ReffBii dftreffbii= (ReffBii)listreffbii.get(k);

			if (dftreffbii.getFlag_edit().intValue()==1)
			{
				if (dftreffbii.getHit_err().intValue()==0)
				{
					nama_reff = dftreffbii.getNama_reff().toUpperCase();
					lcb_no = dftreffbii.getLcb_no();
					no_rek = dftreffbii.getNo_rek().toUpperCase();
					atas_nama = dftreffbii.getAtas_nama().toUpperCase();
					flag_aktif = dftreffbii.getFlag_aktif();
					npk = dftreffbii.getNpk().toUpperCase();
					lrb_id = dftreffbii.getLrb_id();
					cab_rek = dftreffbii.getCab_rek().toUpperCase();
					this.elionsManager.update_lst_reff_bii(nama_reff,lcb_no,no_rek,atas_nama,flag_aktif,npk,lrb_id,cab_rek);
					datautama.setStatussubmit("1");
				}
			}
		}
		return new ModelAndView("bac/reff_bii_maintenance","cmd", datautama).addAllObjects(this.referenceData(request,cmd,err));
		//return new ModelAndView("reffbii_maintenance","cmd",this.formBackingObject(request));
	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}
	
}
