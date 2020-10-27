package com.ekalife.elions.web.uw;

/**
 * @author  : Andhika
 * @created : Apr 22, 2013, 8:37:46 AM
 * 
 */

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

public class SpajControllerEndorsment extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String lssaId=ServletRequestUtils.getStringParameter(request, "lssaId",null);
		String lsspId =ServletRequestUtils.getStringParameter(request, "lssp_id",null);
		String msenaksep =ServletRequestUtils.getStringParameter(request, "msenaksep",null);
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
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
		
		if(request.getParameter("search")!=null){
			
//			if(msenaksep==null){
			map.put("listSpaj", 
					uwManager.selectFilterSpajEndorsment(
						ServletRequestUtils.getRequiredStringParameter(request, "posisi"),
						request.getParameter("tipe").toString(), 
						request.getParameter("kata"), 
						ServletRequestUtils.getStringParameter(request, "pilter", "="),lssaId,lsspId));
			
			//tambahan query untuk menandai blacklist atau bukan
			List<CariSpaj> listSpaj = (List) map.get("listSpaj");
			for(CariSpaj cariSpaj : listSpaj){
				String bl = uwManager.selectCekBlacklist(cariSpaj.getReg_spaj());
				if(cariSpaj.getReg_spaj().equals(bl)){
					cariSpaj.setMcl_blacklist(1);
				}
			}
			//========================================
		}
		map.put("lssaId", lssaId);
		map.put("lssp_id", lsspId);
		map.put("msenaksep", msenaksep);
		map.put("flag", ServletRequestUtils.getStringParameter(request, "flag", "0"));
		
		return new ModelAndView("uw/spaj_aksependorse", "cmd", map);
	}

}