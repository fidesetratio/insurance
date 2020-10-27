package com.ekalife.elions.web.uw;

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
public class SpajgutriController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
	
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		map.put("user_id", lus_id);
		
		String cab_bank="";
		Integer jn_bank = -1;
		Map data_valid = (HashMap)this.elionsManager.select_validbank(lus_id);
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
		map.put("cabang", user.getLca_id());
		
		if(request.getParameter("search")!=null){
			int posisi = ServletRequestUtils.getIntParameter(request, "posisi", -1);
			String tipe = request.getParameter("tipe").toString();
			String kata = request.getParameter("kata");
			String  spajList4EditButton= request.getParameter("spajList4EditButton");
//			if("SSS".equals(user.getCab_bank().trim()) || user.getJn_bank().intValue() == 3) {
			if("SSS,M35,M01".indexOf(user.getCab_bank().trim())>-1){
				map.put("listSpaj", 
						elionsManager.selectFilterSpaj2SimasPrima(
							String.valueOf(posisi), tipe, kata, 
							ServletRequestUtils.getStringParameter(request, "pilter", "="), jn_bank, null, null));
			}else if (cab_bank.equalsIgnoreCase("")){
				map.put("listSpaj", 
						elionsManager.selectgutri(
							posisi, tipe, kata, 
							ServletRequestUtils.getStringParameter(request, "pilter", "=")));
			}else{
				map.put("listSpaj", 
						elionsManager.selectgutri_valid(
							posisi, tipe, kata, 
							ServletRequestUtils.getStringParameter(request, "pilter", "="),lus_id));
			}
			map.put("spajList4EditButton", spajList4EditButton);
		}
		
		map.put("flag", ServletRequestUtils.getStringParameter(request, "flag", "0"));
		return new ModelAndView("uw/spajgutri", "cmd", map);
	}

}