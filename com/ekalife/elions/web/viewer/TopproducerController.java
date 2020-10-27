package com.ekalife.elions.web.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;


public class TopproducerController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		List jenis_topprod = this.uwManager.jenis_topproducer();
		List tahun = new ArrayList();
		Map m = new HashMap();
		for (int k = 1 ; k < 10 ; k++)
		{
			m = new HashMap();
			m.put("thn", k+2000);
			tahun.add(m);
		}
		
		String jenis_top_producer = request.getParameter("jenis");
		String tahun_ke = request.getParameter("tahun_ke");
		if (jenis_top_producer == null)
		{
			jenis_top_producer = "";
		}
		if (tahun_ke == null)
		{
			tahun_ke = "";
		}
		if (!jenis_top_producer.equalsIgnoreCase("") && ! tahun_ke.equalsIgnoreCase(""))
		{
			Integer jumlah = this.uwManager.count_topproducer(Integer.parseInt(tahun_ke),Integer.parseInt(jenis_top_producer));
			if (jumlah.intValue() == 0)
			{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
			}else{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=view_top_producer&show=pdf&elionsManager="+this.elionsManager+"&id="+jenis_top_producer+"&tahun="+tahun_ke);
			}
		}
		map.put("jenis", jenis_top_producer);
		map.put("tahun_ke", tahun_ke);
		map.put("tahun", tahun);
		map.put("jenis_topprod", jenis_topprod);
		return new ModelAndView("uw/viewer/top_producer", map);
	}

}