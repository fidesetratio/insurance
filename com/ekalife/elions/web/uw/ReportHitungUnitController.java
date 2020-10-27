package com.ekalife.elions.web.uw;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;

public class ReportHitungUnitController extends ParentController {

	DateFormat df2 = new SimpleDateFormat("yyyyMMdd");

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(request.getParameter("show")!=null) {
			return showReport(request, response);
		}else {
			Map map = new HashMap();
			Date sysdate = this.elionsManager.selectSysdate(0);
			map.put("sysDate", sysdate);
			return new ModelAndView("uw/hitungunit", map);
		}
	}

	public ModelAndView showReport(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String jenis = ServletRequestUtils.getRequiredStringParameter(request,"jenis");
		
		String startDate = df2.format(defaultDateFormat.parse(ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(new Date()))));
		String endDate = df2.format(defaultDateFormat.parse(ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(new Date()))));
		
		response.sendRedirect("/ReportServer/?rs=UW/ReportHitungUnit&new=true" + //&cetak=true
			"&1jenis="+jenis+
			"&1tanggalAwal="+startDate+
			"&1tanggalAkhir="+endDate
		);

		return null;		
	}
	
}