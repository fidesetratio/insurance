package com.ekalife.elions.web.bac;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import produk_asuransi.n_prod;

import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentController;

public class PendingController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		map.put("spaj", spaj);
		
		Map detBisnis = (Map) elionsManager.selectDetailBisnis(spaj).get(0);
		String bisnis = (String) detBisnis.get("BISNIS");
		String bisnis_no = (String) detBisnis.get("DETBISNIS");
		
		Integer lssa_id = elionsManager.selectPositionSpaj(spaj);
		if(lssa_id == null) lssa_id = new Integer(0);
		map.put("status", lssa_id);
		
		try {
			String trimmed = FormatString.rpad("0", Integer.valueOf(bisnis).toString(), 2);
			n_prod produk = (n_prod) Class.forName("produk_asuransi.n_prod_"+trimmed).newInstance();
			if(produk.kode_flag==1) { //apabila powersave
				produk.setSqlMap(this.elionsManager.getUwDao().getSqlMapClient());
				produk.cek_flag_agen(produk.ii_bisnis_id, Integer.parseInt(bisnis_no), 0);
			}
			
			Integer flag_cc = (Integer) elionsManager.selectFlagCC(spaj);
			if(flag_cc == null) flag_cc = new Integer(0);

			if(produk.flag_worksite == 0 && flag_cc != 3) {
				map.put("initialError", "Produk ini tidak memerlukan Akseptasi Manual (Bukan Produk Worksite)");
			}
		}catch(ClassNotFoundException e) {
			map.put("initialError", "Maaf, tetapi Produk "+bisnis+" belum diimplementasikan. Harap konfirmasi dengan ITwebandmobile@sinarmasmsiglife.co.id.");
		}
		
		if(map.get("initialError") != null) {
			return new ModelAndView("bac/pending", map);
		} else if(request.getParameter("save")!=null) {
			String keterangan = ServletRequestUtils.getStringParameter(request, "keterangan", "");
			keterangan = keterangan.toUpperCase();
			Integer status = ServletRequestUtils.getIntParameter(request, "status");
			Integer substatus = null;
			if(status==3){
				substatus = ServletRequestUtils.getIntParameter(request, "substatus", 1);
			}
			map.put("keterangan", keterangan);
			map.put("status", status);
			//map.put("substatus", substatus);
			if(keterangan.trim().equals("")) map.put("error", "Silahkan isi kolom keterangan");
			else if(status == null) map.put("error", "Silahkan pilih salah satu status");
			else {
				elionsManager.savePending(spaj, keterangan, status, currentUser.getLus_id(), substatus);
				map.put("sukses", "Status Polis berhasil dirubah");
			}
		}

		return new ModelAndView("bac/pending", map);
	}
	
}