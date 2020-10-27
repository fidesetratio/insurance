/**
 * @author  : Ferry Harlim
 * @created : Dec 26, 2006 
 */
package com.ekalife.elions.web.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Nasabah;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentMultiController;

public class ReferralMultiController extends ParentMultiController {
	
	public ModelAndView main_referral(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		//List lsNasabah=elionsManager.selectAllMstNasabah(1);
		//Nasabah nasabah=(Nasabah)lsNasabah.get(0);
		Nasabah nasabah = new Nasabah();
		//map.put("lsNasabah",lsNasabah);
		map.put("nomor","");
		return new ModelAndView("common/main_referral", map);
	}
	public ModelAndView cari_referral(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		List lsCariNasabah=new ArrayList();
		String nomor = ServletRequestUtils.getStringParameter(request, "nomor", "");
		String flag= ServletRequestUtils.getStringParameter(request, "flag", "");
		Integer tipe=ServletRequestUtils.getIntParameter(request, "tipe",1);
		if(flag.equals("1")){
			lsCariNasabah=elionsManager.selectMstNasabahByCode(nomor,tipe,1);
		}
		map.put("lsCariNasabah", lsCariNasabah);
		map.put("nomor", nomor);
		map.put("tipe", tipe);
		return new ModelAndView("common/cari_referral", map);
	}
	public ModelAndView pilih_referral(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		String nomor = ServletRequestUtils.getStringParameter(request, "nomor", "");
		String flag= ServletRequestUtils.getStringParameter(request, "flag", "");
		Integer p= ServletRequestUtils.getIntParameter(request, "p");
		String jenis = ServletRequestUtils.getStringParameter(request, "jenis", "");
		if(jenis.equals("cek")){
			Integer nilai = ServletRequestUtils.getIntParameter(request, "nilai", 0);
			Integer check = elionsManager.selectFlagLeadJnNasabah(nilai);
			if (check==0){
				Integer autoNoRef = elionsManager.selectCountReferralNumber();
				String formatNoRef = autoNoRef.toString()+"L";
				formatNoRef = FormatString.rpad("0", formatNoRef, 6);
				formatNoRef = "NEW";
				map.put("auto", formatNoRef);
			}
			map.put("kode", nilai);
		}
		map.put("lsJnNasabah", elionsManager.selectLstJnNasabah());
		map.put("nomor", nomor);
		map.put("flag", flag);
		map.put("p",p);
		return new ModelAndView("common/pilih_referral", map);
	}
	
}
