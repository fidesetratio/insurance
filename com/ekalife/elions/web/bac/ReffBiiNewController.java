package com.ekalife.elions.web.bac;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;

public class ReffBiiNewController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map cmd = new HashMap();
		
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		String aksi = ServletRequestUtils.getStringParameter(request, "aksi", "");
		String cari = ServletRequestUtils.getStringParameter(request, "cari", "");
		String lead = ServletRequestUtils.getStringParameter(request, "lead", "");
		String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
		String nama = ServletRequestUtils.getStringParameter(request, "nama", "");
		String nm_lead=ServletRequestUtils.getStringParameter(request, "nm_lead","");
		String jab_lead=ServletRequestUtils.getStringParameter(request, "jab_lead","");
		String nama_ref=ServletRequestUtils.getStringParameter(request, "nama_ref","");
		String jab_ref=ServletRequestUtils.getStringParameter(request, "jab_ref","");
		String cabang=ServletRequestUtils.getStringParameter(request, "cabang","");
		
		cmd.put("spaj", spaj);
		cmd.put("cari", cari);
		cmd.put("lead", lead);
		cmd.put("kode", kode);
		cmd.put("nama", nama);
		cmd.put("nm_lead", nm_lead);
		cmd.put("jab_lead", jab_lead);
		cmd.put("nama_ref", nama_ref);
		cmd.put("jab_ref", jab_ref);
		cmd.put("cabang", cabang);
		
		if(aksi.equals("cari")) {
			if(cari.trim().equals("")) {
				cmd.put("error", "Silahkan masukkan nama / nomor lead yang dicari");
			} else {
				cmd.put("daftarNasabah", uwManager.selectCariNasabah(cari));
			}
		}else if(aksi.equals("simpan")) {
			if(kosong(lead)) cmd.put("error", "Silahkan masukkan nomor lead");
			else if(kosong(kode) || kosong(nama)) cmd.put("error", "Nomor tidak ditemukan. Silahkan cek pada aplikasi BancAssurance");
			else {
				uwManager.saveLeadReffBii(cmd);
				cmd.put("success", "Referral BII berhasil disimpan");
			}
		}

		cmd.putAll(uwManager.selectLeadNasabahFromSpaj(spaj));
		
		return new ModelAndView("bac/reff_bii_new", "cmd", cmd);
	}

	private boolean kosong(String tes) {
		if(tes!=null) {
			if(!tes.trim().equals("")) {
				return false;
			}
		}
		return true;
	}
	
}
