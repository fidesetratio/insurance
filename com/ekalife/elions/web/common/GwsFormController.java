package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
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

import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author : Daru
 * @since : Mar 17, 2014
 */
public class GwsFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Integer.class, null, integerEditor);
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gelar", elionsManager.selectGelar(1));
		map.put("jenisUsaha", elionsManager.selectJenisUsaha());
		List<DropDown> options = new ArrayList<DropDown>();
		options.add(new DropDown("1", "Option 1"));
		options.add(new DropDown("2", "Option 2"));
		map.put("options", options);
		
		return map;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command cmd = new Command();
		int size = 4; // Max size contact person
		Personal personal = new Personal();
		// set Perusahaan
		personal.setMcl_jenis(new Integer(1));
		// set flag gws
		personal.setFlag_gws(new Integer(0));
		List lsContactPerson=new ArrayList();
		for(int i=1;i<=size;i++)
			lsContactPerson.add(new ContactPerson());
		personal.setLsContactPerson(lsContactPerson);
		personal.setAddress(new AddressNew());
		cmd.setPersonal(personal);
		cmd.setRow(size);
		cmd.setFlag_ut(new Integer(2));
		
		return cmd;
	}
	
	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		if(request.getParameter("show").equals("true")) 
			return true;
		else 
			return false;
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Command cmd = (Command) command;
		
		if(request.getParameter("pilih_gws").equals("true")) {
			Integer flag_gws = ServletRequestUtils.getIntParameter(request, "personal.flag_gws", 0);
			if(!flag_gws.equals(cmd.getPersonal().getFlag_gws())) cmd.getPersonal().setFlag_gws(flag_gws);
			if(flag_gws > 0) {
				Integer size = flag_gws.equals(new Integer(1)) ? 3 : 2;
				List lsContactPerson = cmd.getPersonal().getLsContactPerson();
				// Apus semua contact kecuali yg pertama
				int contactPersonSize = lsContactPerson.size();
				for(int i = 0; i < contactPersonSize; i++) {
					if(i > 0) lsContactPerson.remove(1);
				}
				
				for(int i = 0; i < size; i++) {
					lsContactPerson.add(new ContactPerson());
				}
				
				cmd.setRow(lsContactPerson.size());
			}
		} else {
			if(cmd.getPersonal().getMcl_jenis()!=null){
				if(cmd.getPersonal().getMcl_jenis()==1) {
					if(cmd.getPersonal().getMcl_id() != "") {
						String mcl_id = cmd.getPersonal().getMcl_id();
						Personal personal=elionsManager.selectProfilePerusahaan(mcl_id);
						List lsContactPerson=elionsManager.selectContactPerson(mcl_id);
						AddressNew address = elionsManager.selectAllAddressNew(mcl_id);
						cmd.setPersonal(personal);
						cmd.getPersonal().setStatus(elionsManager.selectStatusPersonal(mcl_id));
						cmd.getPersonal().setLsContactPerson(lsContactPerson);
						cmd.getPersonal().setAddress(address);
						cmd.setRow(lsContactPerson.size());
						for(int i=0;i<lsContactPerson.size();i++){
							ContactPerson contactPerson=(ContactPerson)lsContactPerson.get(i);
							if(contactPerson.getFlag_ut()==1)
								cmd.setFlag_ut(i+1);
						}
						if(lsContactPerson.isEmpty()){
							for(int i=0;i<4;i++)
								lsContactPerson.add(new ContactPerson());
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException err) throws Exception {
		Command cmd=(Command)command;
		
		if(cmd.getPersonal().getMcl_id().equals("")){//input baru data personal
			if(cmd.getPersonal().getMcl_first().equals(""))
				err.reject("","Nama Perusahaan Harus di isi");
		}else{//edit
			if(cmd.getPersonal().getMcl_id()!=null){
				String s=request.getParameter("s");
				if(s==null)
					s="0";
				if(s.equals("0")){
					List lsContactPerson=cmd.getPersonal().getLsContactPerson();
					AddressNew address=cmd.getPersonal().getAddress();
					List status=cmd.getPersonal().getStatus();
					Personal personal=elionsManager.selectProfilePerusahaan(cmd.getPersonal().getMcl_id());
					if(personal.getMcl_id()==null)
						err.reject("","Nama Perusahaan Tidak Terdaftar");
					personal.setLsContactPerson(lsContactPerson);
					personal.setStatus(status);
					personal.setAddress(address);
					
					cmd.setPersonal(personal);
				}else if(s.equals("1")){
					Personal temp = new Personal();
					temp = elionsManager.selectProfilePerusahaan(cmd.getPersonal().getMcl_id());
					cmd.getPersonal().setLju_id(temp.getLju_id());
					if(new Integer(0).equals(cmd.getPersonal().getFlag_gws()))
						err.reject("", "Harap pilih Jenis Kompensasi Terlebih Dahulu");
				}
			}else{
				err.reject("","Nama Perusahaan Tidak terdaftar Silahkan Hubungi ITwebandmobile@sinarmasmsiglife.co.id");
			}
		}

		if(!request.getParameter("show").equals("true")) {
			for(int i=0;i<cmd.getPersonal().getLsContactPerson().size();i++){
				ContactPerson contactPerson=(ContactPerson)cmd.getPersonal().getLsContactPerson().get(i);
				if(i == 0) {
					if(!contactPerson.getNama_lengkap().equals("")){
						if(contactPerson.getTelp_kantor().equals(""))
							err.reject("","No Telp Kantor harus di isi (Contact Person)");
					}
				} else {
					if(contactPerson.getCara_bayar()==null)
						err.reject("","SIlahkan Pilih Cara Pembayaran.(Layer "+(i)+")");
					else if(contactPerson.getCara_bayar().intValue()==0){//klo tabungan harus isi
						if(contactPerson.getRek_nama().equals(""))
							err.reject("","Silahkan Isi Nama Pemegang Rekening. (Layer "+(i)+")");
						else if(contactPerson.getRek_no().equals(""))
							err.reject("","Silahkan Masukan No Rekening. (Layer "+(i)+")");
						else if(contactPerson.getRek_bank().equals(""))
							err.reject("","Silahkan Masukan Nama Bank. (Layer "+(i)+")");
						else if(contactPerson.getRek_bank_cabang().equals(""))
							err.reject("","Silahkan Masukan Nama Cabang Bank. (Layer "+(i)+")");
						else if(contactPerson.getRek_bank_kota().equals(""))
							err.reject("","Silahkan Masukan Nama Kota Bank. (Layer "+(i)+")");
					}
					if(contactPerson.getJenis_badan() == null){
						err.reject("","Jenis Badan harus di Pilih (Layer "+(i)+")");
					}
					// Set nama lengkap sesuai dengan nama rek
					contactPerson.setNama_lengkap(contactPerson.getRek_nama());
				}
			}
		}
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException err) throws Exception {
		Command cmd = (Command) command;
		User user=(User)request.getSession().getAttribute("currentUser");
		
		elionsManager.prosesInputPersonal(cmd,user);
		return new ModelAndView("common/gws", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,command,err));
	}
	
}
