package com.ekalife.elions.web.bac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.CariSpaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

/**
 * @author Yusuf
 *
 */
public class SpajController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String lssaId=ServletRequestUtils.getStringParameter(request, "lssaId",null);
		String lsspId =ServletRequestUtils.getStringParameter(request, "lssp_id",null);
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		String cabBank = user.getCab_bank();
		map.put("user_id", lus_id);
		
		String cab_bank="";
		Integer jn_bank = -1;
		Map data_valid = (HashMap) this.elionsManager.select_validbank(lus_id);
		if (data_valid != null)
		{
			cab_bank = (String)data_valid.get("CAB_BANK");
			jn_bank = (Integer) data_valid.get("JN_BANK");
		}
		
		if (cab_bank == null)
		{
			cab_bank = "";
		}
		map.put("cabang_bank", cab_bank);
		map.put("jn_bank", jn_bank);
		
		String tgl_lahir = "";
			
		int centang = ServletRequestUtils.getIntParameter(request, "centang", 0);
		if(centang == 1) {
			tgl_lahir = request.getParameter("tgl_lahir");
		}
		//Ryan, req - deddy ,inputan SPAJ kantor kas biar keluar di list kantor cabang utama. tes.toString().split(",")
		StringBuffer tes = new StringBuffer();
		if(user.getJn_bank().equals(2) || user.getJn_bank().equals(16)){
			List<Map> cabangKK = new ArrayList<Map>();
			cabangKK = uwManager.selectCabangKK(user.getCab_bank());
			String daftarDeddy="";
			for(int i=0; i<cabangKK.size();i++){
				Map daftar = cabangKK.get(i);
				daftarDeddy += daftar.get("LCB_NO");
				tes.append((String) daftar.get("LCB_NO") + ",");
			}
			tes.append(cab_bank+",");
		}

		if( user.getJn_bank() != null && user.getJn_bank().equals(3))
		{
			map.put("listSpaj", elionsManager.selectDaftarSpajOtorisasiSpajDanaSekuritas( cabBank, request.getParameter("kata"), request.getParameter("tipe"), ServletRequestUtils.getStringParameter(request, "pilter", "=") ) );
		}
		else
		{
			map.put("listSpaj", elionsManager.selectDaftarSpajOtorisasi( tes.toString().split(","), request.getParameter("kata"), request.getParameter("tipe"), ServletRequestUtils.getStringParameter(request, "pilter", "=") ) );
		}
		map.put("lssaId", lssaId);
		map.put("lssp_id", lsspId);
		map.put("flag", ServletRequestUtils.getStringParameter(request, "flag", "0"));
		
		return new ModelAndView("bac/spaj", "cmd", map);
	}

}