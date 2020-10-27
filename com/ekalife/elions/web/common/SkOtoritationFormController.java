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
public class SkOtoritationFormController extends ParentFormController {

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
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String noSurat = request.getParameter("noSurat");
		String judulSurat = request.getParameter("judulSurat");
		String jenisSurat = request.getParameter("jenisSurat");
		String thUpdate = request.getParameter("thUpdate");
		String lstBln = request.getParameter("lstBln");
		String noSuratTemp = request.getParameter("noSuratTemp");
		String judulSuratTemp = request.getParameter("judulSuratTemp");
		String jenisSuratTemp = request.getParameter("jenisSuratTemp");
		String thUpdateTemp = request.getParameter("thUpdateTemp");
		String lstBlnTemp = request.getParameter("lstBlnTemp");
		String theEvent = request.getParameter("theEvent");
		String goTo = request.getParameter("goTo");
		String currPage = request.getParameter("currPage");
		String lastPage = request.getParameter("lastPage");
		String getUrl = request.getRequestURL().toString().toLowerCase();
		String noRow = request.getParameter("noRow");
		if(getUrl.contains("http://www.sinarmaslife") || getUrl.contains("http://www.sinarmasmsiglife")){ //www.sinarmaslife.co.id
			request.setAttribute("setUrlForPdf", "http://www.sinarmasmsiglife.com/simaslifehrd/pdf/");
		}else{
			request.setAttribute("setUrlForPdf", "http://intranet/simaslifehrd/pdf/");
		}
		if(noSurat == null)noSurat = "";
		if(judulSurat == null)judulSurat = "";
		if(jenisSurat == null)jenisSurat = "";
		if(thUpdate == null)thUpdate = "";
		if(lstBln == null)lstBln = "";
		if(noSuratTemp == null)noSuratTemp = "";
		if(judulSuratTemp == null)judulSuratTemp = "";
		if(jenisSuratTemp == null)jenisSuratTemp = "";
		if(thUpdateTemp == null)thUpdateTemp = "";
		if(lstBlnTemp == null)lstBlnTemp = "";
		if(noRow == null || "".equals(noRow)){
			noRow = "15";
		}else{
			try{
				int temp = Integer.parseInt(noRow);
			}catch (Exception e) {
				noRow = "15";
			}
		}
		
		int cp = 1;
		int tp = Integer.parseInt(noRow);
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
			judulSurat = judulSuratTemp;
			jenisSurat = jenisSuratTemp;
			thUpdate = thUpdateTemp;
			lstBln = lstBlnTemp;
			request.setAttribute("noSuratTemp", noSuratTemp);
			request.setAttribute("judulSuratTemp", judulSuratTemp);
			request.setAttribute("jenisSuratTemp", jenisSuratTemp);
			request.setAttribute("thUpdateTemp", thUpdateTemp);
			request.setAttribute("lstBlnTemp", lstBlnTemp);
		}else if("".equals(theEvent)){
			request.setAttribute("noSuratTemp", noSurat);
			request.setAttribute("judulSuratTemp", judulSurat);
			request.setAttribute("jenisSuratTemp", jenisSurat);
			request.setAttribute("thUpdateTemp", thUpdate);
			request.setAttribute("lstBlnTemp", lstBln);
		}
		request.setAttribute("noSurat", noSurat);
		request.setAttribute("judulSurat", judulSurat);
		request.setAttribute("jenisSurat", jenisSurat);
		request.setAttribute("thUpdate", thUpdate);
		request.setAttribute("lstBln", lstBln);
		
		String nik = "";
		
		if(request.getParameter("search") == null){
			map.put("daftarSurat", elionsManager.selectDaftarSk(noSurat, judulSurat, jenisSurat, thUpdate, lstBln, cp, tp, "none", null, nik));
			lastPage = elionsManager.selectLastPageDaftarSk(noSurat, judulSurat, jenisSurat, thUpdate, lstBln, cp, tp, "none", nik);
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
		request.setAttribute("noRow", noRow);
		request.setAttribute("daftarSurat", (List<Surat>) map.get("daftarSurat"));
		return map;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String noSurat = request.getParameter("noSurat");
		String judulSurat = request.getParameter("judulSurat");
		String jenisSurat = request.getParameter("jenisSurat");
		String thUpdate = request.getParameter("thUpdate");
		String lstBln = request.getParameter("lstBln");
		String noSuratTemp = request.getParameter("noSuratTemp");
		String judulSuratTemp = request.getParameter("judulSuratTemp");
		String jenisSuratTemp = request.getParameter("jenisSuratTemp");
		String thUpdateTemp = request.getParameter("thUpdateTemp");
		String lstBlnTemp = request.getParameter("lstBlnTemp");
		String theEvent = request.getParameter("theEvent");
		String goTo = request.getParameter("goTo");
		String currPage = request.getParameter("currPage");
		String lastPage = request.getParameter("lastPage");
		String save = request.getParameter("save");
		String getUrl = request.getRequestURL().toString().toLowerCase();
		String noRow = request.getParameter("noRow");
		if(noRow == null || "".equals(noRow)){
			noRow = "15";
		}else{
			try{
				int temp = Integer.parseInt(noRow);
			}catch (Exception e) {
				noRow = "15";
			}
		}
		if(getUrl.contains("sinarmaslife") || getUrl.contains("sinarmasmsiglife")){ //www.sinarmaslife.co.id
			request.setAttribute("setUrlForPdf", "http://www.sinarmasmsiglife.com/simaslifehrd/pdf/");
		}else{
			request.setAttribute("setUrlForPdf", "http://intranet/simaslifehrd/pdf/");
		}
		
		if ("save".equals(save)){
			for(int i = 0 ; i < Integer.parseInt(noRow) ; i++){
				//String ynCheckRegional = request.getParameter("yncheck_regional"+i);
				String ynCheckAkm = request.getParameter("yncheck_akm"+i);
				String surat_id = request.getParameter("surat_id"+i);
				if(ynCheckAkm != null && surat_id != null){
					//AKM
					if(elionsManager.updateDaftarSk(surat_id, "1", ynCheckAkm) == 0){
						elionsManager.insertDaftarSk(surat_id, "1", ynCheckAkm);
					}
					//AGENCY (tidak diperlukan lagi - memakai sama akm)
					//elionsManager.updateDaftarSkAgency(surat_id, "1");
				}else if( ynCheckAkm == null && surat_id != null ){
					//AKM
					elionsManager.deleteDaftarSk(surat_id);
					//AGENCY (tidak diperlukan lagi - memakai sama akm)
					//elionsManager.updateDaftarSkAgency(surat_id, "0");
				}
			}
		}
		
		if(noSurat == null)noSurat = "";
		if(judulSurat == null)judulSurat = "";
		if(jenisSurat == null)jenisSurat = "";
		if(thUpdate == null)thUpdate = "";
		if(lstBln == null)lstBln = "";
		if(noSuratTemp == null)noSuratTemp = "";
		if(judulSuratTemp == null)judulSuratTemp = "";
		if(jenisSuratTemp == null)jenisSuratTemp = "";
		if(thUpdateTemp == null)thUpdateTemp = "";
		if(lstBlnTemp == null)lstBlnTemp = "";
		
		int cp = 1;
		int tp = Integer.parseInt(noRow);
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
			judulSurat = judulSuratTemp;
			jenisSurat = jenisSuratTemp;
			thUpdate = thUpdateTemp;
			lstBln = lstBlnTemp;
			request.setAttribute("noSuratTemp", noSuratTemp);
			request.setAttribute("judulSuratTemp", judulSuratTemp);
			request.setAttribute("jenisSuratTemp", jenisSuratTemp);
			request.setAttribute("thUpdateTemp", thUpdateTemp);
			request.setAttribute("lstBlnTemp", lstBlnTemp);
		}else if("".equals(theEvent)){
			request.setAttribute("noSuratTemp", noSurat);
			request.setAttribute("judulSuratTemp", judulSurat);
			request.setAttribute("jenisSuratTemp", jenisSurat);
			request.setAttribute("thUpdateTemp", thUpdate);
			request.setAttribute("lstBlnTemp", lstBln);
		}
		request.setAttribute("noSurat", noSurat);
		request.setAttribute("judulSurat", judulSurat);
		request.setAttribute("jenisSurat", jenisSurat);
		request.setAttribute("thUpdate", thUpdate);
		request.setAttribute("lstBln", lstBln);
		
		String nik = "";
		
		map.put("daftarSurat", elionsManager.selectDaftarSk(noSurat, judulSurat, jenisSurat, thUpdate, lstBln, cp, tp, "none", null, nik));
		lastPage = elionsManager.selectLastPageDaftarSk(noSurat, judulSurat, jenisSurat, thUpdate, lstBln, cp, tp, "none", nik);
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
		request.setAttribute("noRow", noRow);
		return new ModelAndView("common/surat_otorisasi",map);
	}

}
	