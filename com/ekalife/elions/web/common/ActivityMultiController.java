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
import com.ekalife.utils.parent.ParentMultiController;

public class ActivityMultiController extends ParentMultiController {
	
	public ModelAndView main_activity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		//List<Nasabah> lsNasabah=elionsManager.selectMstNasabah();
		//Nasabah nasabah=(Nasabah)lsNasabah.get(0);
		//map.put("lsNasabah",lsNasabah);
		//map.put("nomor",nasabah.getMns_no_ref()+"~"+nasabah.getMns_kd_nasabah());
		map.put("nomor", "");
		return new ModelAndView("common/main_activity", map);
	}
	
	public ModelAndView cari_activity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		List lsCariNasabah=new ArrayList();
		String nomor = ServletRequestUtils.getStringParameter(request, "nomor", "");
		String flag= ServletRequestUtils.getStringParameter(request, "flag", "");
		Integer tipe=ServletRequestUtils.getIntParameter(request, "tipe",1);
		if(flag.equals("1")){
			lsCariNasabah=elionsManager.selectMstNasabahByCodeAll(nomor,tipe);
		}
		map.put("lsCariNasabah", lsCariNasabah);
		map.put("nomor", nomor);
		map.put("tipe", tipe);
		return new ModelAndView("common/cari_activity", map);
	}
}