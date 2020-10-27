package com.ekalife.elions.web.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;

public class ViewerproduksiController  extends ParentMultiController{

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("uw/viewer/produksi");
	}
	
	public ModelAndView perkodeagen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String id = currentUser.getLus_id();
		String kode_agen = request.getParameter("kode_agen");
		if (kode_agen == null)
		{
			kode_agen="";
		}
		
		String tanggal1 = request.getParameter("tgl1");
		String tgl1 = tanggal1;
		String tanggal_sementara = tanggal1;
		String tanggal2 = request.getParameter("tgl3");
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
		
		if (!kode_agen.equalsIgnoreCase("") && !tanggal1.equalsIgnoreCase("") && !tanggal2.equalsIgnoreCase(""))
		{
			/*Integer level = this.elionsManager.ceklevelagen(kode_agen);
			if (level == null)
			{
				level = new Integer(0);
			}
			if (level.intValue() != 0)
			{
				if (level.intValue() == 1)
				{
					response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=komisiperagen&show=pdf&kode_agen="+kode_agen+"&id="+id);
				}else{*/
					Integer jumlah = this.elionsManager.countproduksiperkodeagen(kode_agen, tanggal1, tanggal2, id);
					if (jumlah.intValue() > 0)
					{
						response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=viewer_produksi_perkodeagen&show=pdf&elionsManager="+this.elionsManager+"&kode_agen="+kode_agen+"&id="+id+"&tgl1="+tanggal1+"&tgl2="+tanggal2+"&tanggal1="+tgl1+"&tanggal2="+tgl2);
					}else{
						response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
					}
				/*}
			}*/
		}
		map.put("kunci","perkode");
		map.put("kode_agen",kode_agen);
		//map.put("tgl1",tanggal_sementara);
		map.put("tgl1",tgl1);
		map.put("tgl2", tgl2);
		return new ModelAndView("uw/viewer/produksi", map);
	}
	
	public ModelAndView perlevel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		List value=new ArrayList();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List listlevel = this.elionsManager.selectlstlevel();
		
		String id = currentUser.getLus_id();
		String level = request.getParameter("level");
		if (level == null)
		{
			level="";
		}
		
		String tanggal1 = request.getParameter("tgl1");
		String tgl1 = tanggal1;
		String tanggal_sementara = tanggal1;
		String tanggal2 = request.getParameter("tgl3");
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
		
		if (!level.equalsIgnoreCase("") && !tanggal1.equalsIgnoreCase("") && !tanggal2.equalsIgnoreCase(""))
		{
				Integer jumlah = this.elionsManager.countproduksiperlevel(level, tanggal1, tanggal2, id);
				if (jumlah.intValue() > 0)
				{
					response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=viewer_produksi_perlevel&show=pdf&elionsManager="+this.elionsManager+"&id="+id+"&tgl1="+tanggal1+"&tgl2="+tanggal2+"&tanggal1="+tgl1+"&tanggal2="+tgl2+"&level="+level);
				}else{
					response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
				}				
		}
		map.put("kunci","perlevel");
		map.put("level",level);
		//map.put("tgl1",tanggal_sementara);
		map.put("listlevel", listlevel);
		map.put("tgl1",tgl1);
		map.put("tgl2", tgl2);
		return new ModelAndView("uw/viewer/produksi", map);
	}

	public ModelAndView percabang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		List value=new ArrayList();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List listcabang = this.elionsManager.selectlstCabang2();
		
		String id = currentUser.getLus_id();
		String cabang = request.getParameter("cabang");
		if (cabang == null)
		{
			cabang="";
		}
		
		String tanggal1 = request.getParameter("tgl1");
		String tgl1 = tanggal1;
		String tanggal_sementara = tanggal1;
		String tanggal2 = request.getParameter("tgl3");
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
		
		if (!cabang.equalsIgnoreCase("") && !tanggal1.equalsIgnoreCase("") && !tanggal2.equalsIgnoreCase(""))
		{
			if (cabang.equalsIgnoreCase("00"))
			{
				Integer jumlah = this.elionsManager.countproduksipercabang_all( tanggal1, tanggal2, id);
				if (jumlah.intValue() > 0)
				{
					response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=viewer_produksi_percabang_all&show=pdf&elionsManager="+this.elionsManager+"&id="+id+"&tgl1="+tanggal1+"&tgl2="+tanggal2+"&tanggal1="+tgl1+"&tanggal2="+tgl2);
				}else{
					response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
				}				
			}else{
				Integer jumlah = this.elionsManager.countproduksipercabang(cabang, tanggal1, tanggal2, id);
				if (jumlah.intValue() > 0)
				{
					response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=viewer_produksi_percabang&show=pdf&elionsManager="+this.elionsManager+"&cabang="+cabang+"&id="+id+"&tgl1="+tanggal1+"&tgl2="+tanggal2+"&tanggal1="+tgl1+"&tanggal2="+tgl2);
				}else{
					response.sendRedirect(request.getContextPath()+"/report/uw.pdf?window=tidak_ada_data&show=pdf");
				}
			}
		}
		map.put("kunci","percabang");
		map.put("cabang",cabang);
		//map.put("tgl1",tanggal_sementara);
		map.put("listcabang", listcabang);
		map.put("tgl1",tgl1);
		map.put("tgl2", tgl2);
		return new ModelAndView("uw/viewer/produksi", map);
	}
	
}
