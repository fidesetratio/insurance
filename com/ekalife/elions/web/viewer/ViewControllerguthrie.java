package com.ekalife.elions.web.viewer;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;

public class ViewControllerguthrie  extends ParentMultiController{

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String id = currentUser.getLus_id();

		String tanggal1 = request.getParameter("tgl1");
		String tanggal2 = request.getParameter("tgl3");
		String tgl1 = tanggal1;
		String tgl2 = tanggal2;
		if (tanggal1!=null)
		{
			tanggal1=tanggal1.replaceAll("/","");
			tanggal2=tanggal2.replaceAll("/","");
			String yy1,mm1,dd1,yy2,mm2,dd2;
			yy1=tanggal1.substring(4);
			mm1=tanggal1.substring(2,4);
			dd1=tanggal1.substring(0,2);
			yy2=tanggal2.substring(4);
			mm2=tanggal2.substring(2,4);
			dd2=tanggal2.substring(0,2);
			tanggal1=yy1+mm1+dd1;
			tanggal2=yy2+mm2+dd2;
		}else{
			tanggal1 = "";
			tgl1 = "";
			tanggal2 ="";
			tgl2="";
		}
		if ( !tanggal1.equalsIgnoreCase("") && !tanggal2.equalsIgnoreCase(""))
		{
			Integer jumlah = this.elionsManager.countsummaryinputguthrie(tanggal1, tanggal2);
			if (jumlah.intValue() > 0)
			{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=viewer_summary_input_guthrie&show=pdf&id="+id+"&tgl1="+tanggal1+"&tgl2="+tanggal2+"&tanggal1="+tgl1+"&tanggal2="+tgl2);
			}else{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
			}
		}
		map.put("kunci","peruser");
	//	map.put("kode_agen",kode_agen);
		map.put("tgl1",tgl1);
		map.put("tgl2", tgl2);
		return new ModelAndView("uw/viewer/summary_input_guthrie", map);
	}
	


}
