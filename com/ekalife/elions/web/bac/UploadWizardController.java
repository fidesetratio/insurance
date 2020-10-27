package com.ekalife.elions.web.bac;

import static com.ekalife.utils.Common.isEmpty;
import id.co.sinarmaslife.std.model.vo.DropDown;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.CommandUploadBac;
import com.ekalife.elions.model.DepositPremium;
import com.ekalife.elions.model.Insured;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.utils.parent.ParentWizardController;

public class UploadWizardController extends ParentWizardController {

	DateFormat tempFileFormat = new SimpleDateFormat("yyyyMMddhhmmss");
	
	@Override
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		CommandUploadBac cmd = (CommandUploadBac) command;
		if(cmd.getMode().equals("upload") && getCurrentPage(request)==0) {
			if(cmd.getFile1().isEmpty()) {
				errors.reject("", "Anda belum mengisi file yang akan di-upload");
			}else {
				elionsManager.bindSpajForUpload(cmd, request);
			}
		}
	}
	
	@Override
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		if(page==0) {
			CommandUploadBac cmd = (CommandUploadBac) command;
			if(cmd.getMode().equals("upload")) {
				if(!cmd.getFile1().getOriginalFilename().endsWith(".xls")) {
					errors.reject("", "Silahkan upload file dalam format .xls (Microsoft Excel)");
				}else {
					boolean[] errFlag = new boolean[] {false, false, false, false, false, false, false, false, false, false, false, false, false};
					for(int i=0; i<cmd.getDaftarSpaj().size(); i++) {
						Pemegang pemegang = cmd.getDaftarSpaj().get(i).getPemegang();
						if(isEmpty(pemegang.getMcl_first()) 					&& !errFlag[0]) errFlag[0] = true;
						if(isEmpty(pemegang.getMspe_date_birth()) 	&& !errFlag[1]) errFlag[1] = true;
						if(isEmpty(pemegang.getMspe_place_birth()) 	&& !errFlag[2]) errFlag[2] = true;
						if(isEmpty(pemegang.getMspe_sex())				&& !errFlag[3]) errFlag[3] = true;
						AddressNew address = cmd.getDaftarSpaj().get(i).getAddress();
						if(isEmpty(address.getAlamat_rumah())				&& !errFlag[4]) errFlag[4] = true;
						if(isEmpty(address.getKota_rumah())				&& !errFlag[5]) errFlag[5] = true;
						List<Benefeciary> benef = cmd.getDaftarSpaj().get(i).getDaftarBenefeciary();
						for(int j=0; j<benef.size(); j++) {
							Benefeciary b = benef.get(j);
							if(!isEmpty(b.getMsaw_first()) || !isEmpty(b.getMsaw_birth()) || !isEmpty(b.getLsre_id()) || !isEmpty(b.getMsaw_persen()))	{
								if(isEmpty(b.getMsaw_first()) || isEmpty(b.getMsaw_birth()) || isEmpty(b.getLsre_id()) || isEmpty(b.getMsaw_persen()))	{
									if(!errFlag[6]) errFlag[6] = true;
								}
							}
						}
						Policy polis = cmd.getDaftarSpaj().get(i).getPolis();
						if(isEmpty(polis.getMsag_id())				&& !errFlag[7]) errFlag[7] = true;
						if(isEmpty(polis.getMspo_spaj_date())	&& !errFlag[8]) errFlag[8] = true;
						Insured insured = cmd.getDaftarSpaj().get(i).getInsured();
						if(isEmpty(insured.getMste_beg_date())&& !errFlag[9]) errFlag[9] = true;
						DepositPremium ttp = cmd.getDaftarSpaj().get(i).getTtp();
						if(isEmpty(ttp.getLsrek_id())					&& !errFlag[10]) errFlag[10] = true;
						if(isEmpty(ttp.getMsdp_pay_date())		&& !errFlag[11]) errFlag[11] = true;
						if(isEmpty(ttp.getMsdp_date_book())	&& !errFlag[12]) errFlag[12] = true;
					}
					if(errFlag[0]) errors.reject("", "Pemegang Polis: Nama tidak lengkap");
					if(errFlag[1]) errors.reject("", "Pemegang Polis: Tanggal Lahir tidak lengkap");
					if(errFlag[2]) errors.reject("", "Pemegang Polis: Tempat Lahir tidak lengkap");
					if(errFlag[3]) errors.reject("", "Pemegang Polis: Jenis Kelamin tidak lengkap");
					if(errFlag[4]) errors.reject("", "Alamat: Alamat Rumah tidak lengkap");
					if(errFlag[5]) errors.reject("", "Alamat: Kota Rumah tidak lengkap");
					if(errFlag[6]) errors.reject("", "Ahli Waris: Data tidak lengkap");
					if(errFlag[7]) errors.reject("", "Polis: Kode Agen tidak lengkap");
					if(errFlag[8]) errors.reject("", "Polis: Tanggal SPAJ tidak lengkap");
					if(errFlag[9]) errors.reject("", "Polis: Tanggal Mulai Berlaku Polis tidak lengkap");
					if(errFlag[10]) errors.reject("", "Titipan Premi: Silahkan masukkan kode Bank yang benar (4 digit terakhir rekening)");
					if(errFlag[11]) errors.reject("", "Titipan Premi: Silahkan masukkan tanggal pembayaran");
					if(errFlag[12]) errors.reject("", "Titipan Premi: Silahkan masukkan tanggal RK");					
				}
			}
		}
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, int page) throws Exception {
		Map<String, List> map = new HashMap<String, List>();
		if(page==0) {
			List<DropDown> daftarForm = new ArrayList<DropDown>();
			DropDown dd = new DropDown("form_spaj_130", "Super Protection - PT. Karya Kompos Bagas, Malang");
			daftarForm.add(dd);
			map.put("daftarForm", daftarForm);
		}
		return map;
	}
	
	@Override
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mode = ServletRequestUtils.getStringParameter(request, "mode", ""); 
		if(mode.equals("download")) {
			CommandUploadBac cmd = new CommandUploadBac();
			ServletRequestDataBinder binder = new ServletRequestDataBinder(cmd);
			binder.bind(request);
			return new ModelAndView("xlsViewer", "cmd", cmd);
		}
		return super.handleInvalidSubmit(request, response);
	}

	@Override
	protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandUploadBac cmd = (CommandUploadBac) command;
		if(cmd.getMode().equals("upload")) {
			elionsManager.uploadSpaj(cmd, request);
			return new ModelAndView("bac/upload_finish", "cmd", cmd);
		}else if(cmd.getMode().equals("download")) {
			return new ModelAndView("xlsViewer", "cmd", cmd);
		}
		return null;
	}

}