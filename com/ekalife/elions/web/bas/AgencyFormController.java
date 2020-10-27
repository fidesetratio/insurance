package com.ekalife.elions.web.bas;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Mia;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;
import com.google.gwt.http.client.Request;

public class AgencyFormController extends ParentFormController{
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor); 
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object comand, Errors err) throws Exception {
		Map<String, List> map = new HashMap<String, List>();
		
		List<DropDown> lsKodeAgen = uwManager.selectMiaMsagId();
		List<DropDown> lsRegion = uwManager.selectlsRegion("37");
		List<DropDown> lsBank = uwManager.selectlsBank();
		List<DropDown> lsStatus = new ArrayList<DropDown>();
		lsStatus.add(new DropDown("Active","1"));
		lsStatus.add(new DropDown("Non Active","0"));
		
		map.put("lsKodeAgen", lsKodeAgen);
		map.put("lsRegion", lsRegion);
		map.put("lsBank", lsBank);
		map.put("lsStatus", lsStatus);
		return map;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command cmd = new Command();
		cmd.setMia(new Mia());
		String agensys_id = ServletRequestUtils.getStringParameter(request, "agensys_id","");
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if(agensys_id.equals("")) {
			cmd.getMia().setMia_tgl_aktif(df.format(elionsManager.selectSysdate()));		
			cmd.getMia().setStatus(0);
			
		}
		else {
			cmd.setMia(uwManager.getMstInputAgensys(agensys_id));
			cmd.getMia().setIsNew(1);
			cmd.getMia().setStatus(1);
		}
		// khusus agency
		cmd.getMia().setLsLevel(new ArrayList<DropDown>());
		cmd.getMia().getLsLevel().add(new DropDown("Agency Director","1"));
		cmd.getMia().getLsLevel().add(new DropDown("Agency Manager","2"));
		cmd.getMia().getLsLevel().add(new DropDown("Sales Manager","3"));
		cmd.getMia().getLsLevel().add(new DropDown("Sales Executive","4"));		

		
		return cmd;
	}
	
	/*@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if(submitMode.equals("region")) return true;
		return false;
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
		Command cmd = (Command) command;
		cmd.getMia().setLsLevel(getLevel(cmd.getMia().getLca_id()));
	}*/
	
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException err) throws Exception {
		Command cmd = (Command) command;
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode","");
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		if(!submitMode.equals("miaDelete")) {
			if(!cmd.getMia().getMia_awal_kontrak().equals("")) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(elionsManager.selectFAddMonths(cmd.getMia().getMia_awal_kontrak(), 12));
				cal.add(Calendar.DATE, -1);
				cmd.getMia().setMia_akhir_kontrak(df.format(cal.getTime()));
			}
			
			if(!cmd.getMia().getMia_nama().equals("") && !cmd.getMia().getMia_birth_date().equals("")) {
				Integer ll_row = uwManager.getAgenBlackList(cmd.getMia().getMia_nama(),cmd.getMia().getMia_birth_date());
				if(ll_row > 0)
					err.reject("","Agent ini masuk Attention List..!!!");
			}
			
			if(!cmd.getMia().getMia_recruiter().equals("")) {
				Integer ll_row = uwManager.wf_cek_rekruter(cmd.getMia().getMia_recruiter());
				if(ll_row <= 0)
					err.reject("","Kode Rekruiter Tidak Ada / Tidak Aktif");
				else 
					cmd.getMia().setMia_level_recruit(uwManager.getLevelRecruiter(cmd.getMia().getMia_recruiter(),37));
			}
			
			if(cmd.getMia().getFlagExist() == 0) {
				if(!cmd.getMia().getMia_nama().equals("") && !cmd.getMia().getMia_birth_date().equals("")) {
					String msag_id = uwManager.isAgentExist(cmd.getMia().getMia_nama(),cmd.getMia().getMia_birth_date());
					if(!msag_id.equals("")) {
						cmd.getMia().setFlagExist(1);
						err.reject("","Agent "+msag_id+ " memiliki data nama dan tanggal lahir yang sama dengan agent yang diinput. Tekan Simpan jika tetep ingin melanjutkan");
					}					
				}
			}
			
			if( cmd.getMia().getMia_tgl_berkas()!= null ){
				String tgl_berkas_str  = cmd.getMia().getMia_tgl_berkas();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date tgl_berkas = dateFormat.parse(tgl_berkas_str);
				 if(tgl_berkas.before(elionsManager.selectSysdate(-30)) || tgl_berkas.after(elionsManager.selectSysdate(0))){
					 err.reject("","Tgl Terima Berkas Tdk blh lbh kecil dr "+ FormatDate.toIndonesian(elionsManager.selectSysdate(-30)) + " dan tdk blh lbh besar dr "+FormatDate.toIndonesian(elionsManager.selectSysdate(0)));
				 }
				}
		}
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException err) throws Exception {
		Map map = new HashMap();
		Command cmd = (Command) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode","");
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		cmd.getMia().setLus_id(Integer.parseInt(currentUser.getLus_id()));
		cmd.getMia().setMia_input_date(df.format(elionsManager.selectSysdate()));
		
		if(submitMode.equals("miaDelete")) {
			uwManager.deleteMasterInputAgen(cmd.getMia().getMia_agensys_id()); 
			map.put("pesan", "Data Agen Berhasil Dihapus");
		}
		else {
			if(cmd.getMia().getMia_agensys_id().equals(""))
				cmd.getMia().setMia_agensys_id(uwManager.saveMasterInputAgen(cmd.getMia()));
			else uwManager.updateMasterInputAgen(cmd.getMia()); 
			
			if(cmd.getMia().getMia_agensys_id().length() > 10) {
				map.put("pesan", cmd.getMia().getMia_agensys_id());
			}
			else {
				map.put("agensys_id", cmd.getMia().getMia_agensys_id());
				map.put("pesan", "Simpan Data Agen Berhasil");
			}			
		}

		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/agency.htm")).addAllObjects(map);
	}
	
	private List<DropDown> getLevel(String lca_id) {
		List<DropDown> ls = new ArrayList<DropDown>();
		if("37,52".contains(lca_id)){
//		if(lca_id.equals("37")) {
			ls.add(new DropDown("Agency Director","1"));
			ls.add(new DropDown("Agency Manager","2"));
			ls.add(new DropDown("Sales Manager","3"));
			ls.add(new DropDown("Sales Executive","4"));
		}
		else if(lca_id.equals("42")) {
			ls.add(new DropDown("AVP","1"));
			ls.add(new DropDown("TL","2"));
			ls.add(new DropDown("EBAM","3"));
			ls.add(new DropDown("EBFE","4"));
		}
		else if(lca_id.equals("46")) {
			ls.add(new DropDown("Regional Director","1"));
			ls.add(new DropDown("Regional Manager","2"));
			ls.add(new DropDown("District Manager","3"));
			ls.add(new DropDown("Branch Manager","4"));
			ls.add(new DropDown("Sales Manager","5"));
			ls.add(new DropDown("Financial Consultant","6"));
		}		
		
		return ls;
	}
	
}
