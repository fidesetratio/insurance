package com.ekalife.elions.web.uw;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.CariBlacklist;
import com.ekalife.elions.model.CariPas;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

/**
 * @author Andy
 *
 */
public class PasgutriController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
	
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		map.put("user_id", lus_id);
		
		
		if(request.getParameter("search")!=null){
			//String x = request.getParameter("cari");
			String telp = request.getParameter("telp");
			String sumber = request.getParameter("sumber");
			String tgl_lahir = "";
			
			int centang = ServletRequestUtils.getIntParameter(request, "centang", 0);
			if(centang == 1) {
				tgl_lahir = request.getParameter("tgl_lahir");
			}
			
			List<CariPas> cp = uwManager.selectgutripas(
					ServletRequestUtils.getRequiredIntParameter(request, "posisi"),
					request.getParameter("tipe").toString(), 
					request.getParameter("kata"), 
					request.getParameter("cari"), 
					ServletRequestUtils.getStringParameter(request, "pilter", "="),
					tgl_lahir, telp, sumber);
			DateFormat myDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			for(int i = 0 ; i < cp.size() ; i++){
				if(cp.get(i).getMsp_date_of_birth() != null){
					cp.get(i).setMsp_date_of_birth2(myDateFormat.format(cp.get(i).getMsp_date_of_birth()));
				}
			}
			map.put("listPas", cp);
//				map.put("listMcl", 
//						elionsManager.selectgutriblacklist(
//							ServletRequestUtils.getRequiredIntParameter(request, "posisi"),
//							request.getParameter("tipe").toString(), 
//							request.getParameter("kata"), 
//							ServletRequestUtils.getStringParameter(request, "pilter", "=")));
		}
		
		map.put("flag", ServletRequestUtils.getStringParameter(request, "flag", "0"));
		
		return new ModelAndView("uw/pasgutri", "cmd", map);
	}

}