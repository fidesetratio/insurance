package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.mail.MailException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.AksesHist;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Surat;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentFormController;
import com.ekalife.utils.parent.ParentMultiController;

/**
 * @author Andy
 *
 */
public class SkFormController extends ParentFormController {

	/**
	 * Akses ke tabel HRD.HDR_SURAT@ABSEN untuk menampilkan dokumen2 HRD, dimana tipe PDF tersebut adalah : 
		 1 = SK
		 2 = SE
		 3 = IM
	 * @author Andy
	 * @since Nov 5, 2009 (9:43:12 AM)
	 * @param request
	 * @param response
	 * @return
	 */
	
	/* HARDCODED AKSES SURAT UNTUK BAS (AKTIF) (LUS_ADMIN = 1) YANG PUNYA AKSES KE E-LIONS & E-HRD
 	LUS_ID	LUS_FULL_NAME				NIK
	5		YUNE						1994060164
	55		HENDRA						1996060241
	113		MARTINO						2001020597
	283		HULUK						2002060803
	690		DESY EKA RAHMASARI			2007031713
	1128	ZEKE SEN SARI				2008072191
	1478	MARTIN HADI SYAPUTRA		2009102389
	52		LIM ARIEF					1999040428
	3041    PRATIDINA INDAH				2012063100
	 */
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");	
		String noSurat = request.getParameter("noSurat");
		String viewSurat = request.getParameter("viewSurat");
		String judulSurat = request.getParameter("judulSurat");
		String jenisSurat = request.getParameter("jenisSurat");
		String thUpdate = request.getParameter("thUpdate");
		String lstBln = request.getParameter("lstBln");
		String noSuratTemp = request.getParameter("noSuratTemp");
		String viewSuratTemp = request.getParameter("viewSuratTemp");
		String judulSuratTemp = request.getParameter("judulSuratTemp");
		String jenisSuratTemp = request.getParameter("jenisSuratTemp");
		String thUpdateTemp = request.getParameter("thUpdateTemp");
		String lstBlnTemp = request.getParameter("lstBlnTemp");
		String theEvent = request.getParameter("theEvent");
		String goTo = request.getParameter("goTo");
		String currPage = request.getParameter("currPage");
		String lastPage = request.getParameter("lastPage");
		String getUrl = request.getRequestURL().toString().toLowerCase();
		if(getUrl.contains("sinarmaslife") || getUrl.contains("sinarmasmsiglife")){ //www.sinarmasmsiglife.co.id
			request.setAttribute("setUrlForPdf", "http://www.sinarmasmsiglife.com/simaslifehrd/pdf/");
		}else{
			request.setAttribute("setUrlForPdf", "http://intranet/simaslifehrd/pdf/");
		}
		if(theEvent == null)theEvent = "";
		if(noSurat == null)noSurat = "";
		if(viewSurat == null)viewSurat = "0";
		if(judulSurat == null)judulSurat = "";
		if(jenisSurat == null)jenisSurat = "";
		if(thUpdate == null)thUpdate = "";
		if(lstBln == null)lstBln = "";
		if(noSuratTemp == null)noSuratTemp = "";
		if(viewSuratTemp == null)viewSuratTemp = "";
		if(judulSuratTemp == null)judulSuratTemp = "";
		if(jenisSuratTemp == null)jenisSuratTemp = "";
		if(thUpdateTemp == null)thUpdateTemp = "";
		if(lstBlnTemp == null)lstBlnTemp = "";
		
		int cp = 1;
		int tp = 10;
		if ("gotoPage".equals(theEvent)){
			if(!"".equals(goTo)){
				if(Integer.parseInt(goTo) > Integer.parseInt(lastPage))goTo = lastPage;
				if(Integer.parseInt(goTo) < 1)goTo = currPage;
				cp = Integer.parseInt(goTo);
			}
		}else if ("first".equals(theEvent)){
			cp = 1;
		}else if ("prev".equals(theEvent)){
			if(!"1".equals(currPage)){
				cp = Integer.parseInt(currPage) - 1;
			}
		}else if ("next".equals(theEvent)){
			if(!lastPage.equals(currPage)){
				cp = Integer.parseInt(currPage) + 1;
			}else {cp = Integer.parseInt(lastPage);}
		}else if ("last".equals(theEvent)){
			cp = Integer.parseInt(lastPage);
		}
		if(currPage == null){
			currPage = "0";
		}else{
			request.setAttribute("currPage", cp + "");
		}
		if(!"".equals(theEvent)){
			noSurat = noSuratTemp;
			viewSurat = viewSuratTemp;
			judulSurat = judulSuratTemp;
			jenisSurat = jenisSuratTemp;
			thUpdate = thUpdateTemp;
			lstBln = lstBlnTemp;
			request.setAttribute("noSuratTemp", noSuratTemp);
			request.setAttribute("viewSuratTemp", viewSuratTemp);
			request.setAttribute("judulSuratTemp", judulSuratTemp);
			request.setAttribute("jenisSuratTemp", jenisSuratTemp);
			request.setAttribute("thUpdateTemp", thUpdateTemp);
			request.setAttribute("lstBlnTemp", lstBlnTemp);
		}else if("".equals(theEvent)){
			request.setAttribute("noSuratTemp", noSurat);
			request.setAttribute("viewSuratTemp", viewSurat);
			request.setAttribute("judulSuratTemp", judulSurat);
			request.setAttribute("jenisSuratTemp", jenisSurat);
			request.setAttribute("thUpdateTemp", thUpdate);
			request.setAttribute("lstBlnTemp", lstBln);
		}
		request.setAttribute("noSurat", noSurat);
		request.setAttribute("viewSurat", viewSurat);
		request.setAttribute("judulSurat", judulSurat);
		request.setAttribute("jenisSurat", jenisSurat);
		request.setAttribute("thUpdate", thUpdate);
		request.setAttribute("lstBln", lstBln);
		
		String filter = "";
		String filterEx = "";
		
		String lusId = currentUser.getLus_id();
		
		if("461".equals(lusId) || "592".equals(lusId) || "1530".equals(lusId)
				 || "1531".equals(lusId) || "771".equals(lusId) || "1378".equals(lusId)
				 || "1532".equals(lusId) || "1533".equals(lusId) || "1461".equals(lusId)
				 || "1272".equals(lusId) || "1380".equals(lusId) || "1534".equals(lusId)
				 || "1379".equals(lusId) || "1401".equals(lusId) ){
			filter = "akm";
			if("0".equals(viewSurat)){
				filter = "none";
				filterEx = "akm";
			}
		}else{
			filter = "regional";
			if("0".equals(viewSurat)){
				filter = "none";
				filterEx = "regional";
			}
		}
		
		// full akses
		// 48 - anna yulia 
		if("48".equals(lusId)  ){
			filter = "none";
			if("0".equals(viewSurat)){
				filter = "none";
				filterEx = "none";
			}
		}
		
		String nik = "";
		
		if(request.getParameter("search") == null){
			//HARDCODED
			List<String> lus_id_list = new ArrayList<String>();
			List<String> nik_list = new ArrayList<String>();
			lus_id_list.add("5");nik_list.add("1994060164");
			lus_id_list.add("55");nik_list.add("1996060241");
			lus_id_list.add("113");nik_list.add("2001020597");
			lus_id_list.add("283");nik_list.add("2002060803");
			lus_id_list.add("690");nik_list.add("2007031713");
//			lus_id_list.add("726");nik_list.add("2009OS0332");
//			lus_id_list.add("1229");nik_list.add("2009012299");
//			lus_id_list.add("708");nik_list.add("2009OS0313");
			lus_id_list.add("1128");nik_list.add("2008072191");
//			lus_id_list.add("721");nik_list.add("2009OS0318");
//			lus_id_list.add("1375");nik_list.add("2009052356");
//			lus_id_list.add("736");nik_list.add("2007031700");
			lus_id_list.add("1478");nik_list.add("2009102389");
//			lus_id_list.add("1468");nik_list.add("2009102388");
			lus_id_list.add("52");nik_list.add("1999040428");
			lus_id_list.add("3041");nik_list.add("2012063100");
			//
			int id = lus_id_list.indexOf(lusId);
			if(id != -1){
				nik = nik_list.get(id);
				filter = "bas";
				if("0".equals(viewSurat)){
					filter = "none";
					filterEx = "bas";
				}
			}
			
			if(!"".equals(filter)){
				map.put("daftarSurat", elionsManager.selectDaftarSk(noSurat, judulSurat, jenisSurat, thUpdate, lstBln, cp, tp, filter, filterEx, nik));
				lastPage = elionsManager.selectLastPageDaftarSk(noSurat, judulSurat, jenisSurat, thUpdate, lstBln, cp, tp, filter, nik);
			}else{
				map.put("daftarSurat", new ArrayList<Surat>());
				lastPage = null;
			}
		}else{
			map.put("daftarSurat", new ArrayList<Surat>());
			lastPage = null;
		}
		
		if(lastPage == null){
			lastPage = "0"; 
			currPage="0";
			request.setAttribute("currPage", currPage);
		}
		if(Integer.parseInt(lastPage) > 0 && Integer.parseInt(currPage) == 0){
			currPage = "1";
			request.setAttribute("currPage", currPage);
		}
		request.setAttribute("lastPage", lastPage);
		request.setAttribute("filter", filter);
		request.setAttribute("filterEx", filterEx);
		request.setAttribute("nik", nik);
		request.setAttribute("daftarSurat", (List<Surat>) map.get("daftarSurat"));
		return map;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		
		String theEvent = request.getParameter("theEvent");
		
//		if ("download".equals(theEvent)){
//			String getUrl = request.getRequestURL().toString().toLowerCase();
//			String direktori = "//intranet/simaslifehrd/pdf/";
//			if(getUrl.contains("sinarmasmsiglife")){ //www.sinarmasmsiglife.co.id
//				direktori = "http://www.sinarmasmsiglife.com/simaslifehrd/pdf/";
//			}
//			String filename = request.getParameter("filename");
//			FileUtils.downloadFile("inline;", direktori, filename, response);
//		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");	
		String noSurat = request.getParameter("noSurat");
		String viewSurat = request.getParameter("viewSurat");
		String judulSurat = request.getParameter("judulSurat");
		String jenisSurat = request.getParameter("jenisSurat");
		String thUpdate = request.getParameter("thUpdate");
		String lstBln = request.getParameter("lstBln");
		String noSuratTemp = request.getParameter("noSuratTemp");
		String viewSuratTemp = request.getParameter("viewSuratTemp");
		String judulSuratTemp = request.getParameter("judulSuratTemp");
		String jenisSuratTemp = request.getParameter("jenisSuratTemp");
		String thUpdateTemp = request.getParameter("thUpdateTemp");
		String lstBlnTemp = request.getParameter("lstBlnTemp");
		String goTo = request.getParameter("goTo");
		String currPage = request.getParameter("currPage");
		String lastPage = request.getParameter("lastPage");
		
		if(theEvent == null)theEvent = "";
		if(noSurat == null)noSurat = "";
		if(viewSurat == null)viewSurat = "0";
		if(judulSurat == null)judulSurat = "";
		if(jenisSurat == null)jenisSurat = "";
		if(thUpdate == null)thUpdate = "";
		if(lstBln == null)lstBln = "";
		if(noSuratTemp == null)noSuratTemp = "";
		if(viewSuratTemp == null)viewSuratTemp = "";
		if(judulSuratTemp == null)judulSuratTemp = "";
		if(jenisSuratTemp == null)jenisSuratTemp = "";
		if(thUpdateTemp == null)thUpdateTemp = "";
		if(lstBlnTemp == null)lstBlnTemp = "";
		
		int cp = 1;
		int tp = 10;
		if ("gotoPage".equals(theEvent)){
			if(!"".equals(goTo)){
				if(Integer.parseInt(goTo) > Integer.parseInt(lastPage))goTo = lastPage;
				if(Integer.parseInt(goTo) < 1)goTo = currPage;
				cp = Integer.parseInt(goTo);
			}
		}else if ("first".equals(theEvent)){
			cp = 1;
		}else if ("prev".equals(theEvent)){
			if(!"1".equals(currPage)){
				cp = Integer.parseInt(currPage) - 1;
			}
		}else if ("next".equals(theEvent)){
			if(!lastPage.equals(currPage)){
				cp = Integer.parseInt(currPage) + 1;
			}else {cp = Integer.parseInt(lastPage);}
		}else if ("last".equals(theEvent)){
			cp = Integer.parseInt(lastPage);
		}
		if(currPage == null){
			currPage = "0";
		}else{
			request.setAttribute("currPage", cp + "");
		}
		if(!"".equals(theEvent)){
			noSurat = noSuratTemp;
			viewSurat = viewSuratTemp;
			judulSurat = judulSuratTemp;
			jenisSurat = jenisSuratTemp;
			thUpdate = thUpdateTemp;
			lstBln = lstBlnTemp;
			request.setAttribute("noSuratTemp", noSuratTemp);
			request.setAttribute("viewSuratTemp", viewSuratTemp);
			request.setAttribute("judulSuratTemp", judulSuratTemp);
			request.setAttribute("jenisSuratTemp", jenisSuratTemp);
			request.setAttribute("thUpdateTemp", thUpdateTemp);
			request.setAttribute("lstBlnTemp", lstBlnTemp);
		}else if("".equals(theEvent)){
			request.setAttribute("noSuratTemp", noSurat);
			request.setAttribute("viewSuratTemp", viewSurat);
			request.setAttribute("judulSuratTemp", judulSurat);
			request.setAttribute("jenisSuratTemp", jenisSurat);
			request.setAttribute("thUpdateTemp", thUpdate);
			request.setAttribute("lstBlnTemp", lstBln);
		}
		request.setAttribute("noSurat", noSurat);
		request.setAttribute("viewSurat", viewSurat);
		request.setAttribute("judulSurat", judulSurat);
		request.setAttribute("jenisSurat", jenisSurat);
		request.setAttribute("thUpdate", thUpdate);
		request.setAttribute("lstBln", lstBln);
		
		String filter = "";
		String filterEx = "";
		
		String lusId = currentUser.getLus_id();
		
		if("461".equals(lusId) || "592".equals(lusId) || "1530".equals(lusId)
				 || "1531".equals(lusId) || "771".equals(lusId) || "1378".equals(lusId)
				 || "1532".equals(lusId) || "1533".equals(lusId) || "1461".equals(lusId)
				 || "1272".equals(lusId) || "1380".equals(lusId) || "1534".equals(lusId)
				 || "1379".equals(lusId) || "1401".equals(lusId) ){
			filter = "akm";
			if("0".equals(viewSurat)){
				filter = "none";
				filterEx = "akm";
			}
		}else{
			filter = "regional";
			if("0".equals(viewSurat)){
				filter = "none";
				filterEx = "regional";
			}
		}
		
		String nik = "";
		
		//	HARDCODED
		List<String> lus_id_list = new ArrayList<String>();
		List<String> nik_list = new ArrayList<String>();
		lus_id_list.add("5");nik_list.add("1994060164");
		lus_id_list.add("55");nik_list.add("1996060241");
		lus_id_list.add("113");nik_list.add("2001020597");
		lus_id_list.add("283");nik_list.add("2002060803");
		lus_id_list.add("690");nik_list.add("2007031713");
//		lus_id_list.add("726");nik_list.add("2009OS0332");
//		lus_id_list.add("1229");nik_list.add("2009012299");
//		lus_id_list.add("708");nik_list.add("2009OS0313");
		lus_id_list.add("1128");nik_list.add("2008072191");
//		lus_id_list.add("721");nik_list.add("2009OS0318");
//		lus_id_list.add("1375");nik_list.add("2009052356");
//		lus_id_list.add("736");nik_list.add("2007031700");
		lus_id_list.add("1478");nik_list.add("2009102389");
//		lus_id_list.add("1468");nik_list.add("2009102388");
		lus_id_list.add("52");nik_list.add("1999040428");
		//
		int id = lus_id_list.indexOf(lusId);
		if(id != -1){
			nik = nik_list.get(id);
			filter = "bas";
			if("0".equals(viewSurat)){
				filter = "none";
				filterEx = "bas";
			}
		}
		
		if(!"".equals(filter)){
			map.put("daftarSurat", elionsManager.selectDaftarSk(noSurat, judulSurat, jenisSurat, thUpdate, lstBln, cp, tp, filter, filterEx, nik));
			lastPage = elionsManager.selectLastPageDaftarSk(noSurat, judulSurat, jenisSurat, thUpdate, lstBln, cp, tp, filter, nik);
		}else{
			map.put("daftarSurat", new ArrayList<Surat>());
			lastPage = null;
		}
		
		if(lastPage == null){
			lastPage = "0"; 
			currPage="0";
			request.setAttribute("currPage", currPage);
		}
		if(Integer.parseInt(lastPage) > 0 && Integer.parseInt(currPage) == 0){
			currPage = "1";
			request.setAttribute("currPage", currPage);
		}
		request.setAttribute("lastPage", lastPage);
		request.setAttribute("filter", filter);
		request.setAttribute("filterEx", filterEx);
		request.setAttribute("nik", nik);
		return new ModelAndView("common/list_sk",map);
	}

}
	