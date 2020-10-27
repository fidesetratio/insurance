package com.ekalife.elions.web.uw;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

public class NabController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		Date startDate = this.elionsManager.selectSysdate(-9);
		Date endDate = this.elionsManager.selectSysdate(0);
		
		String sDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(startDate));
		String eDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(endDate));
		String nDate = ServletRequestUtils.getStringParameter(request, "nabDate", defaultDateFormat.format(endDate));
		int pos = ServletRequestUtils.getIntParameter(request, "pos", -1);
		int jumlah = ServletRequestUtils.getIntParameter(request, "jumlah", -1);
		User user = (User) request.getSession().getAttribute("currentUser");
		map.put("startDate", sDate);
		map.put("endDate", eDate);
		map.put("nabDate", nDate);
		
		if(jumlah>0) {
			List error = this.elionsManager.prosesHitungUnit(pos, sDate, eDate, nDate,user);
			if(error.size()>0) map.put("gagal", error);
			else map.put("sukses", "true");
		}
		return new ModelAndView("uw/nab", map);
	}

}