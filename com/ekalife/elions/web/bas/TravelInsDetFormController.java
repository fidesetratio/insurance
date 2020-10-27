package com.ekalife.elions.web.bas;

import java.util.Calendar;
import java.util.Date;

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
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Inputan Peserta Travel Insurance
 * 
 * @author Yusuf
 * @since Mar 8, 2010 (8:23:23 AM)
 */
public class TravelInsDetFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command = new Command();
		int msti_id = ServletRequestUtils.getRequiredIntParameter(request, "msti_id");
		int msti_jenis = ServletRequestUtils.getRequiredIntParameter(request, "msti_jenis");
		command.setMsti_id(msti_id);
		command.setMsti_jenis(msti_jenis);
		command.setDaftarTravelIns(uwManager.selectTravelInsuranceDet(msti_id, msti_jenis));
		command.setPesan(ServletRequestUtils.getStringParameter(request, "info", ""));
		
		TravelInsurance master = uwManager.selectTravelInsurance(command.getMsti_id(), command.getMsti_jenis());
		command.setPeserta(new TravelInsurance());
		command.getPeserta().msti_id = command.getMsti_id();
		command.getPeserta().msti_jenis = command.getMsti_jenis();
		command.getPeserta().msid_no = -1;
		command.getPeserta().lsb_code = master.getLsb_code();
		command.getPeserta().msid_beg_date = master.getMsti_tgl();
		command.getPeserta().msid_end_date = FormatDate.add(command.getPeserta().msid_beg_date, Calendar.DATE, 3);
		command.getPeserta().msti_jml_peserta = master.msti_jml_peserta;
		command.getPeserta().validasi = uwManager.selectValidasiTravelInsurance(command.getMsti_id(), command.getMsti_jenis());
		command.getPeserta().msti_posisi = master.msti_posisi;		
		
		return command;
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if("new, edit".contains(submitMode)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors) throws Exception {
		//User currentUser = (User) request.getSession().getAttribute("currentUser");
		Command command = (Command) cmd;
		if("new".equals(command.getSubmitMode())){
			TravelInsurance master = uwManager.selectTravelInsurance(command.getMsti_id(), command.getMsti_jenis());
			command.setPeserta(new TravelInsurance());
			command.getPeserta().msti_id = command.getMsti_id();
			command.getPeserta().msti_jenis = command.getMsti_jenis();
			command.getPeserta().msid_no = -1;
			command.getPeserta().lsb_code = master.getLsb_code();
			command.getPeserta().msid_beg_date = master.getMsti_tgl();
			command.getPeserta().msid_end_date = FormatDate.add(command.getPeserta().msid_beg_date, Calendar.DATE, 3);
			command.getPeserta().msti_jml_peserta = master.msti_jml_peserta;
			command.getPeserta().validasi = uwManager.selectValidasiTravelInsurance(command.getMsti_id(), command.getMsti_jenis());
			command.getPeserta().msti_posisi = master.msti_posisi;
		}else if("edit".equals(command.getSubmitMode())){
			command.setPeserta(uwManager.selectTravelInsuranceDet(command.getMsti_id(), command.getMsti_jenis(), command.getMsid_no()));
		}
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		Command command = (Command) cmd;
		if("save".equals(command.getSubmitMode())){
			TravelInsurance peserta = command.getPeserta();
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "peserta.kode_premi", "", "Harap input Kode Premi");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "peserta.msid_blanko", "", "Harap input No. Blanko");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "peserta.msid_nama", "", "Harap input Nama");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "peserta.msid_sex", "", "Harap pilih Jenis Kelamin");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "peserta.msid_place_birth", "", "Harap input Tempat Lahir");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "peserta.msid_date_birth", "", "Harap input Tanggal Lahir");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "peserta.msid_beg_date", "", "Harap input Tanggal Mulai Asuransi");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "peserta.msid_alamat", "", "Harap input Alamat");
			
			if(peserta.getMsid_telp_rmh().equals("") && peserta.getMsid_hp().equals("")){
				errors.rejectValue("peserta.msid_telp_rmh", "", "Harap input salah satu Nomor Telepon (Rumah / HP)");
				errors.rejectValue("peserta.msid_hp", "", "");
			}
			
			peserta.validasi = uwManager.selectValidasiTravelInsurance(peserta.msti_id, peserta.msti_jenis);

			if(peserta.getMsid_no().intValue() == -1){
				if(peserta.validasi.jml_peserta_terinput.intValue() >= peserta.validasi.msti_jml_peserta.intValue()){
					errors.rejectValue("peserta.msti_jml_peserta", "", "Anda tidak dapat melakukan penginputan data baru karena jumlah peserta maksimal sudah terlewati");
				}else if(peserta.validasi.msid_premi_setor.doubleValue() >= peserta.validasi.msti_premi_setor.doubleValue()){
					errors.rejectValue("peserta.msti_premi_setor", "", "Anda tidak dapat melakukan penginputan data baru karena jumlah premi setor maksimal sudah terlewati");
				}
			}
			
			if(peserta.getMsti_posisi().intValue() > 1){
				errors.rejectValue("peserta.msti_jml_peserta", "", "Polis ini sudah FILLING, data tidak dapat diedit");
			}
			
			double upNasabah = uwManager.selectValidasiMaxUpTravelInsurance(peserta);
			if(upNasabah > 100000000){
				errors.rejectValue("peserta.msid_nama", "", "Peserta yang sama sudah melebihi limit UP Travel Insurance (Rp. 100.000.000). Penginputan tidak dapat dilanjutkan");
			}
			
		}
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors) throws Exception {
		Command command = (Command) cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String hasil = uwManager.saveTravelInsuranceDet(command, currentUser);
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/travelinsdet.htm"))
			.addObject("msti_id", command.getMsti_id())
			.addObject("msti_jenis", command.getMsti_jenis())
			.addObject("sukses", hasil);
	}
	
}
