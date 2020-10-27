package com.ekalife.elions.web.bac;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Employee;
import com.ekalife.elions.model.History;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Simas;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.TtgSimasvalidator;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_replace;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentFormController;
/**
 * @author HEMILDA
 * Controller titipan premi
 */

public class TtgSimasController extends ParentFormController {

	protected void validatePage(Object cmd, Errors err, int page) {
		TtgSimasvalidator validator = (TtgSimasvalidator) this.getValidator();
		Cmdeditbac halaman= (Cmdeditbac)cmd;
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}	
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", ServletRequestUtils.getStringParameter(request, "editSPAJ", ""));
		Map map = new HashMap();
		map.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
		return map;
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String lde_id = currentUser.getLde_id();
				Cmdeditbac detiledit = new Cmdeditbac();
				String spaj = request.getParameter("showSPAJ");
				if(spaj!=null)
				{
					/**
					 * @author HEMILDA
					 * edit
					 */
					detiledit.setPemegang((Pemegang)this.elionsManager.selectpp(spaj));
					detiledit.setAddressbilling((AddressBilling)this.elionsManager.selectAddressBilling(spaj));
					detiledit.setDatausulan((Datausulan)this.elionsManager.selectDataUsulanutama(spaj));
					detiledit.getDatausulan().setLde_id(lde_id);
					detiledit.setTertanggung((Tertanggung)this.elionsManager.selectttg(spaj));
					
					InvestasiUtama investasi  = (InvestasiUtama)this.elionsManager.selectinvestasiutama(spaj);
					if (investasi == null)
					{
						InvestasiUtama inv = new InvestasiUtama();
						inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
						inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
						detiledit.setInvestasiutama(inv);
					}else{
						detiledit.setInvestasiutama(investasi);
					}
						
					detiledit.setRekening_client(this.elionsManager.select_rek_client(spaj));
					detiledit.setAccount_recur(this.elionsManager.select_account_recur(spaj));
					
					Powersave data_pwr = (Powersave)this.elionsManager.select_powersaver(spaj);
					if (data_pwr==null)
					{
						detiledit.setPowersave(new Powersave());
					}else{
						detiledit.setPowersave(data_pwr);
					}

					detiledit.setAgen(this.elionsManager.select_detilagen(spaj));

					String kode_agen = detiledit.getAgen().getMsag_id();
					String nama_agent="";
					if (kode_agen.equalsIgnoreCase("000000"))
					{
						nama_agent = (String)this.elionsManager.select_agent_temp(spaj);
					}
					detiledit.getAgen().setMcl_first(nama_agent);
					detiledit.setEmployee(this.elionsManager.select_detilemployee(spaj));
					
					detiledit.setHistory(new History());
					detiledit.getDatausulan().setDaftapeserta(this.elionsManager.select_semua_mst_peserta(spaj));
					
				}else{
					/**
					 * @author HEMILDA
					 * input
					 */
					detiledit.setPemegang(new Pemegang());
					detiledit.setAddressbilling(new AddressBilling());
					detiledit.setDatausulan(new Datausulan());
					detiledit.setTertanggung(new Tertanggung());
					InvestasiUtama inv = new InvestasiUtama();
					inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
					inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
					detiledit.setInvestasiutama(inv);
					detiledit.setRekening_client(new Rekening_client());
					detiledit.setAccount_recur(new Account_recur());
					detiledit.setPowersave(new Powersave());
					detiledit.setAgen(new Agen());
					detiledit.setHistory(new History());
					detiledit.setEmployee(new Employee());
				}
			return detiledit;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		NumberFormat nf = NumberFormat.getNumberInstance(); 
		f_validasi data = new f_validasi();
		f_replace konteks = new f_replace();
		
		Cmdeditbac a = (Cmdeditbac) cmd;
		Integer jumlah_peserta = ServletRequestUtils.getIntParameter(request, "jml_peserta");
		if (jumlah_peserta == null)
		{
			jumlah_peserta = new Integer(1);
			a.getDatausulan().setJml_peserta(jumlah_peserta);
		}
		List b= new ArrayList();
		Integer no_urut;                       
		Integer lsre_id;                       
		String nama;    
		String tgllhr;
		String blnhr;
		String thnhr;
		Integer kelamin;                       
		Integer umur;                          
		Double premi;                          
		String reg_spaj;
		String tanggal_lahir;
		if (jumlah_peserta > 0)
		{
			for (int k = 1 ;k <= jumlah_peserta; k++)
			{
				no_urut = ServletRequestUtils.getIntParameter(request, "ttg.no_urut"+k);
				lsre_id = ServletRequestUtils.getIntParameter(request, "ttg.lsre_id"+k);
				nama = request.getParameter("ttg.nama"+k);
				if (nama==null)
				{
					nama="";
				}
				tgllhr = request.getParameter("tgllhr"+k);
				blnhr = request.getParameter("blnhr"+k);
				thnhr = request.getParameter("thnhr"+k);
				thnhr = request.getParameter("thnhr"+k);
				
				tanggal_lahir = FormatString.rpad("0",tgllhr,2)+"/"+FormatString.rpad("0",blnhr,2)+"/"+thnhr;
				if ((tgllhr.trim().length()==0)||(blnhr.trim().length()==0)||(thnhr.trim().length()==0))
				{
					tanggal_lahir=null;
				}else{
					boolean cekk1= f_validasi.f_validasi_numerik(tgllhr);	
					boolean cekk2= f_validasi.f_validasi_numerik(blnhr);
					boolean cekk3= f_validasi.f_validasi_numerik(thnhr);		
					if ((cekk1==false) ||(cekk2==false) || (cekk3==false)  )
					{
						tanggal_lahir=null;
					}
				}
					
				Date tanggallahir = null;
				if (tanggal_lahir != null)
				{
					tanggallahir = defaultDateFormat.parse(tanggal_lahir);
				}
				
				kelamin = ServletRequestUtils.getIntParameter(request, "ttg.kelamin"+k);
				if (kelamin == null)
				{
					kelamin= new Integer(1);
				}
				umur = ServletRequestUtils.getIntParameter(request, "ttg.umur"+k); 
				if (umur==null)
				{
					umur = new Integer(0);
				}
				premi = ServletRequestUtils.getDoubleParameter(request,"ttg.premi"+k);
				if (premi == null)
				{
					premi = new Double(0);
				}
				reg_spaj = a.getPemegang().getReg_spaj();

				Simas sm = new Simas();
				sm.setKelamin(kelamin);
				sm.setLsre_id(lsre_id);
				sm.setNama(nama);
				sm.setNo_urut(no_urut);
				sm.setPremi(premi);
				sm.setReg_spaj(reg_spaj);
				sm.setTgl_lahir(tanggallahir);
				sm.setUmur(umur);
				b.add(sm);
			}
			a.getDatausulan().setDaftapeserta(b);
		}
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Cmdeditbac a = (Cmdeditbac) cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String status_submit="";
//		a=this.elionsManager.savingpeserta(cmd, err, status_submit, currentUser);
		status_submit = a.getDatausulan().getStatus_submit();
		if (status_submit == null)
		{
			status_submit="";
		}
		
		return new ModelAndView("bac/editttgsimas",err.getModel()).addAllObjects(this.referenceData(request));
	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}

}