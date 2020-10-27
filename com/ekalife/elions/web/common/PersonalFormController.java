package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class PersonalFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Integer.class, null, integerEditor);
	}

	@Override
	protected Map referenceData(HttpServletRequest arg0, Object command, Errors errors) throws Exception {
		Map<String, Collection> map = new HashMap<String, Collection>();
		List<DropDown> daftarAdminWs = new ArrayList<DropDown>();
		daftarAdminWs.add( new DropDown("",""));
		daftarAdminWs.add( new DropDown("649", "Dwi Asti"));
		daftarAdminWs.add( new DropDown("1468", "Fabry Fahlevi"));
		daftarAdminWs.add( new DropDown("1065", "Setyo Utami"));
		daftarAdminWs.add( new DropDown("500", "Yusy Octavia"));
		daftarAdminWs.add( new DropDown("3323", "Bimo Hario Tejo"));
		map.put("daftarAdminWs", daftarAdminWs);
		map.put("gelar", elionsManager.selectGelar(1));
		map.put("jenisUsaha", elionsManager.selectJenisUsaha());
		return map;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command cmd = new Command();
		int size=2;//jumlah contact person
		Personal personal = new Personal();
		//set perusahaan
		personal.setMcl_jenis(new Integer(1));
		List lsContactPerson=new ArrayList();
		for(int i=1;i<=size;i++)
			lsContactPerson.add(new ContactPerson());
		
		personal.setLsContactPerson(lsContactPerson);
		personal.setAddress(new AddressNew());
		cmd.setPersonal(personal);
		cmd.setRow(size);
		cmd.setFlag_ut(new Integer(1));
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
		
		if(cmd.getPersonal().getMcl_jenis()!=null){
			if(cmd.getPersonal().getMcl_jenis()==1) {

				String mcl_id = cmd.getPersonal().getMcl_id();
				Personal personal=elionsManager.selectProfilePerusahaan(mcl_id);
				List lsContactPerson=elionsManager.selectContactPerson(mcl_id);
				AddressNew address = elionsManager.selectAllAddressNew(mcl_id);
				cmd.setPersonal(personal);
				cmd.getPersonal().setStatus(elionsManager.selectStatusPersonal(mcl_id));
				cmd.getPersonal().setLsContactPerson(lsContactPerson);
				cmd.getPersonal().setAddress(address);
				for(int i=0;i<lsContactPerson.size();i++){
					ContactPerson contactPerson=(ContactPerson)lsContactPerson.get(i);
					if(contactPerson.getFlag_ut()==1)
						cmd.setFlag_ut(i+1);
				}
				if(lsContactPerson.isEmpty()){
					for(int i=1;i<=cmd.getRow();i++)
						lsContactPerson.add(new ContactPerson());
				}
				if(lsContactPerson.size()<cmd.getRow()){
					for(int i=lsContactPerson.size();i<cmd.getRow();i++)
						lsContactPerson.add(new ContactPerson());
				}	
			}
		}else{
			
		}
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException err) throws Exception {
		Command cmd=(Command)command;
		
		if(cmd.getPersonal().getMcl_id().equals("")){//input baru data personal
			if(cmd.getPersonal().getMcl_first().equals(""))
				err.reject("","Nama Perusahaan Harus di isi");
			//
//			if(cmd.getPersonal().getRegion_id().equals(""))
//				err.reject("","Nama wilayah Harus di isi");
			else{//lca-lwk-lsrg
				if(!cmd.getPersonal().getRegion_id().equals("") && cmd.getPersonal().getRegion_id()!=null){
					cmd.getPersonal().setLca_id(cmd.getPersonal().getRegion_id().substring(0,2));
					cmd.getPersonal().setLwk_id(cmd.getPersonal().getRegion_id().substring(2,4));
					cmd.getPersonal().setLsrg_id(cmd.getPersonal().getRegion_id().substring(4,6));
				}else if(cmd.getPersonal().getRegion_id().equals("")){
					err.reject("","Wilayah harus diisi atau Wilayah tidak terdaftar");
				}else{
					err.reject("","Ada Kesalahan System. Mohon hubungi ITwebandmobile@sinarmasmsiglife.co.id");
				}
			}
			//
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
				}
			}else{
				err.reject("","Nama Perusahaan Tidak terdaftar Silahkan Hubungi ITwebandmobile@sinarmasmsiglife.co.id");
			}
//			if(cmd.getPersonal().getRegion_id()==null || cmd.getPersonal().getRegion_id().equals(""))
//				err.reject("","Nama wilayah Harus di isi");
		}
		//
		for(int i=0;i<cmd.getPersonal().getLsContactPerson().size();i++){
			ContactPerson contactPerson=(ContactPerson)cmd.getPersonal().getLsContactPerson().get(i);
			if(!contactPerson.getNama_lengkap().equals("")){//isi data personel
				if(contactPerson.getTelp_kantor().equals(""))
					err.reject("","No Telp Kantor harus di isi (Contact Person "+(i+1)+")");
				if(contactPerson.getCara_bayar()==null)
					err.reject("","SIlahkan Pilih Cara Pembayaran.(Contact Person "+(i+1)+")");
				else if(contactPerson.getCara_bayar().intValue()==0){//klo tabungan harus isi
					if(contactPerson.getRek_nama().equals(""))
						err.reject("","Silahkan Isi Nama Pemegang Rekening. (Contact Person "+(i+1)+")");
					else if(contactPerson.getRek_no().equals(""))
						err.reject("","Silahkan Masukan No Rekening. (Contact Person "+(i+1)+")");
					else if(contactPerson.getRek_bank().equals(""))
						err.reject("","Silahkan Masukan Nama Bank. (Contact Person "+(i+1)+")");
					else if(contactPerson.getRek_bank_cabang().equals(""))
						err.reject("","Silahkan Masukan Nama Cabang Bank. (Contact Person "+(i+1)+")");
					else if(contactPerson.getRek_bank_kota().equals(""))
						err.reject("","Silahkan Masukan Nama Kota Bank. (Contact Person "+(i+1)+")");
				}
				if(contactPerson.getNpwp().equals("")){
//					err.reject("","NPWP harus di isi (Contact Person "+(i+1)+")");
				}
				if(contactPerson.getJenis_badan() == null){
					err.reject("","Jenis Badan harus di Pilih (Contact Person "+(i+1)+")");
				}
				
					
			}
		}	
			
		//err.reject("","Tes");
	}
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException err) throws Exception {
		Command cmd=(Command)command;
		User user=(User)request.getSession().getAttribute("currentUser");
		
		elionsManager.prosesInputPersonal(cmd,user);
		return new ModelAndView("common/personal", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request,command,err));
	}
	
}