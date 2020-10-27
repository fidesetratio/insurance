
package com.ekalife.elions.web.uw;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;

public class RdsScreeningController extends ParentMultiController{
	public ModelAndView list_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		logger.debug("EditBacController : formBackingObject");
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String lus_id = null;
		String lde_id = currentUser.getLde_id();
		
				String pilihan = ServletRequestUtils.getStringParameter(request, "pilihan", "0");	
				//String pilihan = request.getParameter("nomorspajpolis").trim();	
		    	String action = request.getParameter("action");
		    	String spaj = "";
		    	String nopol = "";
		    	
				String tipe = request.getParameter("tipe");
				String view_spaj = null;
				//String refresh = request.getParameter("refresh");
				String ref = ServletRequestUtils.getStringParameter(request, "ref", "0");
				String tipe1 = ServletRequestUtils.getStringParameter(request, "tipe1", null);
				
				if(pilihan.contains("1")){
					request.setAttribute("pilihan", pilihan);
					view_spaj = ServletRequestUtils.getStringParameter(request, "view_spaj", "0");
					request.setAttribute("view_spaj", view_spaj);
				    tipe = null;
				}
				
				if((ref.contains("1"))&&(tipe1==null)){
					
					request.setAttribute("ref", ref);
					request.setAttribute("tipe1", tipe1);
					
				}else{
					
					if (!(tipe==null))
					{
						if(tipe.trim().equalsIgnoreCase("nopol")){
							 nopol = request.getParameter("nomorspajpolis").trim();
							 spaj = elionsManager.selectSpajFromPolis(nopol);
						}else if(tipe.trim().equalsIgnoreCase("spaj")){
							spaj = request.getParameter("nomorspajpolis").trim();
						}
					}
				}
				
				if (!(view_spaj==null))
				{
					spaj = view_spaj.trim();
				}
				int spajPega = 0;
				spajPega = bacManager.selectCekSpajNonPega(spaj);
				if(spajPega > 0){
						logger.info(request.getContextPath());
						return new ModelAndView( new RedirectView( request.getContextPath()+"/include/page/blocked.jsp?jenis=pegaprod" ) );
				}
				
				List rdsList = new ArrayList();
				
				rdsList = bacManager.selectrdsscreening(spaj);
			
				List rdsHist = new ArrayList();
				
				rdsHist = bacManager.selecthistoryrds(spaj);
				
				request.setAttribute("rdslist", rdsList);
				request.setAttribute("rdshist", rdsHist);


		request.setAttribute("lde_id", lde_id);
		
		
		return new ModelAndView("uw/rds_screening");
	}
	
	
	public ModelAndView detail_rds(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		List lsCariNasabah=new ArrayList();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String mspo_date_print = ServletRequestUtils.getStringParameter(request, "mspo_date_print", "");
		String tgl_konfirmasi = ServletRequestUtils.getStringParameter(request, "tgl_konfirmasi", "");
		String tgl_generate =  ServletRequestUtils.getStringParameter(request, "tgl_generate", "");
		String flag_konfirmasi =  ServletRequestUtils.getStringParameter(request, "flag_konfirmasi", "0");
		String tgl_generate_latest =  ServletRequestUtils.getStringParameter(request, "tgl_generate_latest", "");
		String flag= ServletRequestUtils.getStringParameter(request, "flag", "");
		String keterangan = ServletRequestUtils.getStringParameter(request, "keterangan", "");
		List msgSukses = new ArrayList();
		request.setAttribute("spaj", spaj);
		request.setAttribute("mspo_date_print", mspo_date_print);

		request.setAttribute("tgl_konfirmasi", tgl_konfirmasi);

		request.setAttribute("tgl_generate", tgl_generate);

		request.setAttribute("flag_konfirmasi", flag_konfirmasi);
		request.setAttribute("tgl_generate_latest",tgl_generate_latest);
		request.setAttribute("keterangan", keterangan);
		
		if(flag.equals("1")){
			
				int tgl_print_polis_date_change = ServletRequestUtils.getIntParameter(request, "tgl_print_polis_date_change", 0);
				int tgl_konfirmasi_change = ServletRequestUtils.getIntParameter(request, "tgl_konfirmasi_change",0);
				int tgl_generate_latest_change = ServletRequestUtils.getIntParameter(request, "tgl_generate_latest_change",0);
				int flag_konfirmasi_change = ServletRequestUtils.getIntParameter(request, "flag_konfirmasi_change",0);
				String keterangan_change = ServletRequestUtils.getStringParameter(request, "keterangan_change","");
				
				if(!(keterangan.equals("") && keterangan_change.equals(""))){
				
					if(tgl_print_polis_date_change != 0){
						if(tgl_print_polis_date_change < 0){
							bacManager.updateMstDatePrintToNull(spaj);
						}
				}
				
				if(tgl_konfirmasi_change != 0){
					if(tgl_konfirmasi_change < 0){
						Date d =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
		                .parse(tgl_generate);
						String fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
						Date d1 =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
		                .parse(tgl_konfirmasi);
						String fd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d1);
						bacManager.updateTanggalKonfirmasiToNull(spaj,fd1,fd);
						System.out.println("tgl_konfirmasi_change ==> to null"+tgl_generate+" ==> "+fd+" reg_spaj="+spaj+ "tgl konfirmasi:"+tgl_konfirmasi);
					}
				}
			
				
				if(tgl_generate_latest_change != 0){
					if(tgl_generate_latest_change < 0){
						System.out.println("tgl_generate_latest_change ==> to remove");
						bacManager.deletePositionSpajGenerate(spaj);
					}
				}
			
				if(flag_konfirmasi_change != 0){
					Date d =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
	                .parse(tgl_generate);
					String fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
				/*	Date d1 =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
	                .parse(tgl_konfirmasi);
					String fd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d1);
					*/
					
					if(flag_konfirmasi_change < 0){
						System.out.println("flag_konfirmasi_change ==> to remove");
						
						bacManager.updateFlagKonfirmasi(spaj, fd,-1,Integer.parseInt(flag_konfirmasi));
					}
					if(flag_konfirmasi_change > 0){
					
						bacManager.updateFlagKonfirmasi(spaj, fd,flag_konfirmasi_change,Integer.parseInt(flag_konfirmasi));
					}
				}
				  if(!keterangan_change.equals("")){
					  Date d =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")
		              .parse(tgl_generate);
						String fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
						bacManager.updateKetGenerate(spaj,fd,keterangan_change);  
				  }
					
		  		try {
					ServletOutputStream os = response.getOutputStream();
					os.print("<script>" +
							" var doc = window.opener.document;"+
							" var theForm = doc.getElementById(\"formpost\");"+
							" var theField = doc.getElementById(\"nomorspajpolis\");"+
							"  theField.value='"+spaj+"';"+
							" window.close();"+
							"  theForm.submit();"+
							"</script>");
					os.close();
					
				} catch (IOException e) {
					logger.error("ERROR :", e);
				}
		  		return null;
			}else{
				
				msgSukses.add("Keterangan/Alasan Generate Ulang wajib diisi");
				//msgSukses.add("MANTAP!!");				
				return new ModelAndView("uw/detail_rds", "error", msgSukses);
			}
			}
		return new ModelAndView("uw/detail_rds");
	}
}
