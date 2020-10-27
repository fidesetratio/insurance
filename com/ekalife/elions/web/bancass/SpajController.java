package com.ekalife.elions.web.bancass;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

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
			
			String tgl_lahir = "";
			
			int centang = ServletRequestUtils.getIntParameter(request, "centang", 0);
			if(centang == 1) {
				tgl_lahir = request.getParameter("tgl_lahir");
			}
			
//			if("SSS".equals(user.getCab_bank().trim()) || user.getJn_bank().intValue() == 3) {
			if("SSS,M35,M01".indexOf(user.getCab_bank().trim())>-1){
				map.put("listSpaj", 
						elionsManager.selectFilterSpaj2SimasPrima(
							ServletRequestUtils.getRequiredStringParameter(request, "posisi"),
							request.getParameter("tipe").toString(), 
							request.getParameter("kata"), 
							ServletRequestUtils.getStringParameter(request, "pilter", "="), jn_bank, tgl_lahir, null));
			}else if(cab_bank.equalsIgnoreCase("")) {
				map.put("listSpaj", 
						elionsManager.selectFilterSpaj2(
							ServletRequestUtils.getRequiredStringParameter(request, "posisi"),
							request.getParameter("tipe").toString(), 
							request.getParameter("kata"), 
							ServletRequestUtils.getStringParameter(request, "pilter", "="),lssaId,lsspId, tgl_lahir, null));
			}else{
				map.put("listSpaj", 
						elionsManager.selectFilterSpaj2_valid(
							ServletRequestUtils.getRequiredStringParameter(request, "posisi"),
							request.getParameter("tipe").toString(), 
							request.getParameter("kata"), 
							ServletRequestUtils.getStringParameter(request, "pilter", "="),lssaId,lus_id,lsspId, tgl_lahir, null));
			}
		}
		map.put("lssaId", lssaId);
		map.put("lssp_id", lsspId);
		map.put("flag", ServletRequestUtils.getStringParameter(request, "flag", "0"));
		
		return new ModelAndView("bancass/spaj", "cmd", map);
	}

}