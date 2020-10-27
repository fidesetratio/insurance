package com.ekalife.elions.web.finance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentMultiController;

public class ListKomisiMultiController extends ParentMultiController {

	public ModelAndView komisiindividu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("finance/komisi_individu");
	}

	public ModelAndView bayarkomisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*String pil=request.getParameter("pil");
		String start=request.getParameter("awal");
		String end=request.getParameter("akhir");
		
		if(pil==null || pil.equals(""))
			return new ModelAndView("listkomisi.bayarkomisi");
		else{
			String awal=start.replaceAll("/","");
			String akhir=end.replaceAll("/","");
			String yy1,mm1,dd1,yy2,mm2,dd2;
			yy1=awal.substring(4);
			mm1=awal.substring(2,4);
			dd1=awal.substring(0,2);
			yy2=akhir.substring(4);
			mm2=akhir.substring(2,4);
			dd2=akhir.substring(0,2);
			awal=yy1+mm1+dd1;
			akhir=yy2+mm2+dd2;
			int tipe=0;
			if(pil.equals("0")){//bii
				
			}else if(pil.equals("1")){//non bii
				
			}else if(pil.equals("2")){//bii fa
				response.sendRedirect("/ReportServer/?rs=FINANCE/LaporanPembayaranKomisiAgenFa&new=true&awal="+awal+
						"&akhir="+akhir) ;
			}else if(pil.equals("3")){//fp
				response.sendRedirect("/ReportServer/?rs=FINANCE/LaporanPembayaranKomisiAgenFp&new=true&awal="+awal+
						"&akhir="+akhir) ;
			}else if(pil.equals("4")){//reward
				response.sendRedirect("/ReportServer/?rs=FINANCE/LaporanPembayaranKomisiReward&new=true&awal="+awal+
						"&akhir="+akhir+"tipe="+tipe) ;
			}else if(pil.equals("5")){//reward non bii
				response.sendRedirect("/ReportServer/?rs=FINANCE/LaporanPembayaranKomisiRewardNonBii&new=true&awal="+awal+
						"&akhir="+akhir+"tipe="+tipe) ;
			}else if(pil.equals("6")){//reward leader
				tipe=1;
				response.sendRedirect("/ReportServer/?rs=FINANCE/LaporanPembayaranKomisiRewardLeader&new=true&awal="+awal+
						"&akhir="+akhir+"tipe="+tipe) ;
			}
			return null;
		}*/
		return new ModelAndView("finance/bayar_komisi");
	}

	public ModelAndView pajakkomisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("finance/pajak_komisi");
	}
	public ModelAndView slippajakkomisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("finance/slip_pajak_komisi");
	}
	public ModelAndView akumulasipajakkomisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("finance/akumulasi_pajak_komisi");
	}
	public ModelAndView bayarbonus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("finance/bayar_bonus");
	}
	public ModelAndView slippajakbonus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("finance/slip_pajak_bonus");
	}
	public ModelAndView pajakreward(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("finance/pajak_reward");
	}
	public ModelAndView slippajakreward(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("finance/slip_pajak_reward");
	}

}
