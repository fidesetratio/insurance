package com.ekalife.elions.web.viewer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentMultiController;

public class ViewpolisController  extends ParentMultiController{

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("uw/viewer/summary_polis");
	}
	
//	public ModelAndView monitor(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Map map = new HashMap();
//		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		String user_id = currentUser.getName();
//		String tanggal1 = request.getParameter("tgl1");
//		String tanggal2 = request.getParameter("tgl3");
//		String formats = request.getParameter("bentuk");
//		String tgl1 = tanggal1;
//		String tgl2 = tanggal2;
//		if (tanggal1!=null)
//		{
//			tanggal1=tanggal1.replaceAll("/","");
//			tanggal2=tanggal2.replaceAll("/","");
//			String yy1,mm1,dd1,yy2,mm2,dd2;
//			yy1=tanggal1.substring(4);
//			mm1=tanggal1.substring(2,4);
//			dd1=tanggal1.substring(0,2);
//			yy2=tanggal2.substring(4);
//			mm2=tanggal2.substring(2,4);
//			dd2=tanggal2.substring(0,2);
//			tanggal1=yy1+mm1+dd1;
//			tanggal2=yy2+mm2+dd2;
//		}else{
//			tanggal1 = "";
//			tgl1 = "";
//			tanggal2 ="";
//			tgl2="";
//		}
//		if ( !tanggal1.equalsIgnoreCase("") && !tanggal2.equalsIgnoreCase(""))
//		{
//			Integer jumlah = this.elionsManager.countmonitorpolis(tanggal1, tanggal2);
//			if (jumlah.intValue() > 0)
//			{
//				response.sendRedirect(request.getContextPath()+"/report/uw."+formats+"?window=monitorpolis&show="+formats+"&tgl1="+tanggal1+"&tgl2="+tanggal2+"&tanggal1="+tgl1+"&tanggal2="+tgl2+"&user_id="+user_id);
//			}else{
//				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
//			}
//		}
//		map.put("kunci","monitor");
//		map.put("tgl1",tgl1);
//		map.put("tgl2", tgl2);
//		return new ModelAndView("uw/viewer/summary_polis", map);
//	}

	
	public ModelAndView service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String user_id = currentUser.getName();
		String tanggal1 = request.getParameter("tgl1");
		String tanggal2 = request.getParameter("tgl3");
		String tgl1 = tanggal1;
		String tgl2 = tanggal2;
		String formats = request.getParameter("bentuk");
		String kodeao=props.getProperty("cabang.ao");
		String kodews=props.getProperty("cabang.ws");
		String kodeagency=props.getProperty("cabang.agency");
		String kodeartha=props.getProperty("cabang.artha");
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
			Integer jumlah = this.elionsManager.countservicepolis(tanggal1, tanggal2);
			if (jumlah.intValue() > 0)
			{
				response.sendRedirect(request.getContextPath()+"/report/uw."+formats+"?window=servicepolis&show="+formats+"&tgl1="+tanggal1+"&tgl2="+tanggal2+"&tanggal1="+tgl1+"&tanggal2="+tgl2+"&user_id="+user_id+"&kodeao="+kodeao+"&kodews="+kodews+"&kodeagency="+kodeagency+"&kodeartha="+kodeartha);
			}else{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
			}
		}
		map.put("kunci","service");
		map.put("tgl1",tgl1);
		map.put("tgl2", tgl2);
		return new ModelAndView("uw/viewer/summary_polis", map);
	}	
	
	public ModelAndView softcopy(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String user_id = currentUser.getName();
		String tanggal1 = request.getParameter("tgl1");
		String tanggal2 = request.getParameter("tgl3");
		String formats = request.getParameter("bentuk");
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
			Integer jumlah = this.elionsManager.countsoftcopy(tanggal1, tanggal2);
			if (jumlah.intValue() > 0)
			{
				response.sendRedirect(request.getContextPath()+"/report/uw."+formats+"?window=softcopypolis&show="+formats+"&tgl1="+tanggal1+"&tgl2="+tanggal2+"&tanggal1="+tgl1+"&tanggal2="+tgl2+"&user="+user_id);
			}else{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
			}
		}
		map.put("kunci","softcopy");
		map.put("tgl1",tgl1);
		map.put("tgl2", tgl2);
		return new ModelAndView("uw/viewer/summary_polis", map);
	}
	
	public ModelAndView simassehat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String user_id = currentUser.getName();
		String tanggal1 = request.getParameter("tgl1");
		String tanggal2 = request.getParameter("tgl3");
		String formats = request.getParameter("bentuk");
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
			Integer jumlah = this.elionsManager.selectCountsimassehat(tanggal1, tanggal2);
			if (jumlah.intValue() > 0)
			{
				response.sendRedirect(request.getContextPath()+"/report/uw."+formats+"?window=simassehat&show="+formats+"&tgl1="+tanggal1+"&tgl2="+tanggal2+"&user="+user_id);
			}else{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
			}
		}
		map.put("kunci","simassehat");
		map.put("tgl1",tgl1);
		map.put("tgl2", tgl2);
		return new ModelAndView("uw/viewer/summary_polis", map);
	}	

	public ModelAndView softcopy_perhari(HttpServletRequest request, HttpServletResponse response) throws Exception {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		FormatDate dt = new FormatDate();
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String user_id = currentUser.getName();
		String tanggal2 = request.getParameter("tgl2");
		String formats = request.getParameter("bentuk");
		String tgl2 = tanggal2;
		String tgl1;
		String tanggal1;
		if (tanggal2!=null)
		{
			tanggal2=tanggal2.replaceAll("/","");
			String yy1,mm1,dd1,yy2,mm2,dd2;
			yy1=tanggal2.substring(4);
			mm1=tanggal2.substring(2,4);
			dd1=tanggal2.substring(0,2);
			tanggal2=yy1+mm1+dd1;
			Date tanggal_sementara = this.elionsManager.kurang_tanggal(tgl2, 1);
			tanggal1 = FormatDate.toString(tanggal_sementara);
			tgl1 = tanggal1;
			tanggal1=tanggal1.replaceAll("/","");
			yy2=tanggal1.substring(4);
			mm2=tanggal1.substring(2,4);
			dd2=tanggal1.substring(0,2);
			tanggal1=yy2+mm2+dd2;
		}else{
			tanggal1 = "";
			tgl1 = "";
			tanggal2="";
			tgl2="";
		}


		if ( !tanggal2.equalsIgnoreCase("") )
		{
			tanggal1 = tanggal1 + " 16:00:00";
			tanggal2 = tanggal2 + " 16:00:00";
			
			Integer jumlah = this.elionsManager.count_softcopy_perhari(tanggal1, tanggal2);
			if (jumlah.intValue() > 0)
			{
				response.sendRedirect(request.getContextPath()+"/report/uw."+formats+"?window=softcopypolis_perhari&show="+formats+"&tgl1="+tanggal1+"&tgl2="+tanggal2+"&tanggal1="+tgl1+"&tanggal2="+tgl2);
			}else{
				response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
			}
		}
		map.put("kunci","softcopy_perhari");
		map.put("tgl2",tgl2);
		return new ModelAndView("uw/viewer/summary_polis", map);
	}
	
	public ModelAndView perbaikan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String user_id = currentUser.getName();
		String nopolis = request.getParameter("nopolis");
		String formats = request.getParameter("bentuk");
		if (formats == null)
		{
			formats = "";
		}
		if (formats.equalsIgnoreCase(""))
		{
			formats = "pdf";
		}
		if (nopolis != null)
		{
			response.sendRedirect(request.getContextPath()+"/report/uw."+formats+"?window=perbaikanpolis&show="+formats+"&nopolis="+nopolis);
		}
		map.put("kunci","perbaikan");
		return new ModelAndView("uw/viewer/summary_polis", map);
	}	
}
