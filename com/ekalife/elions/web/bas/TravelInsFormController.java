package com.ekalife.elions.web.bas;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.TravelInsurance;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Inputan Data Global Travel Insurance
 * 
 * @author Yusuf
 * @since Mar 8, 2010 (8:23:23 AM)
 */
public class TravelInsFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}	
	
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map<String, List> m = new HashMap<String, List>();
		m.put("listBandara", elionsManager.selectDropDown("EKA.LST_BANDARA", "LSB_ID", "'[' || LSB_CODE || '] ' || LSB_DESC || ', ' || LSB_KOTA", "", "LSB_CODE", ""));
		
		List<DropDown> daftarPosisi = new ArrayList<DropDown>();
		daftarPosisi.add(new DropDown("1", "Input"));
		daftarPosisi.add(new DropDown("2", "Filling"));
		m.put("listPosisi", daftarPosisi);
		
		return m;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command = new Command();
		command.setSysdate(elionsManager.selectSysdate());
		command.setTglAwal(defaultDateFormat.format(elionsManager.selectSysdate(-7)));
		command.setTglAkhir(defaultDateFormat.format(command.getSysdate()));
		command.setI(1); //Posisi 1 Input / 2 Filling (tdk bisa diedit lg oleh inputter)
		return command;
	}
	
	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if("show".equals(submitMode)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Command command = (Command) cmd;
		if(command.getSubmitMode().equals("show")){
			command.setDaftarTravelIns(uwManager.selectTravelInsurance(
					currentUser.getLca_id(), command.getI(), 
					defaultDateFormat.parse(command.getTglAwal()), defaultDateFormat.parse(command.getTglAkhir())));
			for(TravelInsurance t : command.getDaftarTravelIns()){
				t.validasi = uwManager.selectValidasiTravelInsurance(t.msti_id, t.msti_jenis);
			}
		}
		
		//inputan barunya, tambahkan hanya bila di pilihan posisi "INPUT"
		if(command.getI().intValue() == 1){
			TravelInsurance baru = new TravelInsurance();
			baru.msti_posisi = 1;
			baru.lsb_id = currentUser.getLsb_id();
			command.getDaftarTravelIns().add(baru);
		}
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		Command command = (Command) cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		//VALIDASI UNTUK PROSES SIMPAN TRAVEL INSURANCE
		if(command.getSubmitMode().equals("save")){
			for(int i=0; i<command.getDaftarTravelIns().size(); i++){
				TravelInsurance t = command.getDaftarTravelIns().get(i);
				if(t.flag.intValue() == 1){
					errors.setNestedPath("daftarTravelIns["+i+"]");
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lsb_id", "", 			"Mohon input BANDARA");
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "msti_tgl", "", 			"Mohon input TANGGAL");
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "msti_tgl_rk", "", 		"Mohon input TANGGAL RK");
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "msti_jml_peserta", "", 	"Mohon input JUMLAH PESERTA");
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "msti_premi_setor", "", 	"Mohon input PREMI DISETOR");

					//validasi2 lain
					if(!errors.hasErrors()){
						if(t.msti_jml_peserta.intValue() <= 0) {
							errors.rejectValue("msti_jml_peserta", "", "Jumlah Peserta tidak boleh Nol (0)");
						}else if(t.msti_premi_setor.doubleValue() <= 0) {
							errors.rejectValue("msti_premi_setor", "", "Jumlah Premi Disetor tidak boleh Nol (0)");
						}else if(t.msti_posisi.intValue() != 1){
							errors.rejectValue("msti_posisi", "", "Polis ini sudah FILLING, data tidak dapat diedit.");
						}else if(t.msti_tgl_aksep != null){
							errors.rejectValue("msti_tgl_aksep", "", "Polis ini sudah AKSEPTASI, data tidak dapat diedit.");
						}
					}
					
					break;
				}
			}
			errors.setNestedPath("");

		//VALIDASI UNTUK PROSES TRANSFER TRAVEL INSURANCE KE FILLING
		}else if(command.getSubmitMode().equals("transfer")){
			for(int i=0; i<command.getDaftarTravelIns().size(); i++){
				TravelInsurance t = command.getDaftarTravelIns().get(i);
				if(t.flag.intValue() == 2){
					errors.setNestedPath("daftarTravelIns["+i+"]");
					t.validasi = uwManager.selectValidasiTravelInsurance(t.msti_id, t.msti_jenis);
					
					if(t.validasi.msti_jml_peserta.intValue() != t.validasi.jml_peserta_terinput.intValue()){
						errors.rejectValue("msti_jml_peserta", "", 
								"Jumlah Peserta tidak sama. Harap cek kembali data Global (" + t.validasi.msti_jml_peserta + 
								" org) dan data Peserta (" + t.validasi.jml_peserta_terinput + " org).");
					}else if(t.validasi.msti_premi_setor.doubleValue() != t.validasi.msid_premi_setor.doubleValue()){
						errors.rejectValue("msti_premi_setor", "", 
								"Jumlah Premi Disetor tidak sama. Harap cek kembali data Global (" + 
								twoDecimalNumberFormat.format(t.validasi.msti_premi_setor) + 
								" disetor) dan data Peserta (" + 
								twoDecimalNumberFormat.format(t.validasi.msid_premi_setor) +  
								" disetor).");
					}else if(t.msti_posisi.intValue() != 1){
						errors.rejectValue("msti_posisi", "", "Polis sudah pernah ditransfer");
					}else if(t.msti_tgl_aksep == null){
						errors.rejectValue("msti_tgl_aksep", "", "Polis ini belum AKSEPTASI, data tidak dapat ditransfer");
					}
					break;
				}
			}
			errors.setNestedPath("");

		//VALIDASI UNTUK PROSES AKSEPTASI TRAVEL INSURANCE OLEH UW
		}else if(command.getSubmitMode().equals("aksep")){
			for(int i=0; i<command.getDaftarTravelIns().size(); i++){
				TravelInsurance t = command.getDaftarTravelIns().get(i);
				if(t.flag.intValue() == 3){
					errors.setNestedPath("daftarTravelIns["+i+"]");
					if(!currentUser.getLde_id().equals("11")){
						errors.rejectValue("msti_posisi", "", "Akseptasi hanya dapat dilakukan oleh user UNDERWRITING");
					}else if(t.msti_posisi.intValue() != 1){
						errors.rejectValue("msti_posisi", "", "Polis sudah FILLING, tidak dapat dilakukan AKSEPTASI");
					}
					break;
				}
			}
			errors.setNestedPath("");
		}		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors) throws Exception {
		Command command = (Command) cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String hasil = uwManager.saveTravelInsurance(command, currentUser);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/travelins.htm"))
			.addObject("sukses", hasil);
	}
	
}
