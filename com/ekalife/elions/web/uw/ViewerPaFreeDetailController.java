package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.spring.util.Email;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.BindUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;


import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.PaTmmsFree;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Reas;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.Tmms;
import com.ekalife.elions.model.TmmsBill;
import com.ekalife.elions.model.TmmsDBill;
import com.ekalife.elions.model.TmmsDet;
import com.ekalife.elions.model.Transfer;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.utils.Common;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentController;
import com.ekalife.utils.parent.ParentFormController;

public class ViewerPaFreeDetailController extends ParentController{
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.debug("EditBacController : formBackingObject");
		Map map = new HashMap();
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		
		String tmms_id=ServletRequestUtils.getStringParameter(request, "tmms_id",null);
		
		// Filter search jenis PA (Free PA / PA BSM) - Daru 05 Dec 2013
		Integer paType = ServletRequestUtils.getIntParameter(request, "paType", 0);
		map.put("paType", paType);
		
		if(paType.equals(0)) {//Free PA
			PaTmmsFree paTmmsFree=new PaTmmsFree();
			
			List<Tmms> tmmsList = uwManager.selectFreePaTmmsList(tmms_id, null, "1", "", "LIKE", null);
			
			paTmmsFree.setTmms(tmmsList.get(0));
			paTmmsFree.setTmmsBill(new TmmsBill());
			paTmmsFree.setTmmsDBill(new TmmsDBill());
			paTmmsFree.setTmmsDet(new TmmsDet());
			
			map.put("cmd", paTmmsFree);
		} else {//PA BSM
			List<Pas> pasList = uwManager.selectAllPasList(tmms_id, null, null, null, null, "pabsm", null, "pabsm",null,null,null);
			map.put("cmd", pasList.get(0));
		}
		
		return new ModelAndView(
		        "uw/viewer/viewer_pa_free_detail", map);
	}
	

}