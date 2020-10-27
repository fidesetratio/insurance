package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.spring.util.Email;
import id.co.sinarmaslife.std.util.StringUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;


import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.BlackList;
import com.ekalife.elions.model.BlackListFamily;
import com.ekalife.elions.model.Client;
import com.ekalife.elions.model.CmdInputBlacklist;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.DetBlackList;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.ReffBii;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentController;
import com.ekalife.utils.parent.ParentFormController;

public class ViewerBpOnlineController extends ParentController{
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//Pas detiledit = (Pas) cmd;
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		String tipe = request.getParameter("tipe");
		String kata = request.getParameter("kata");
		String pilter = request.getParameter("pilter");
		
		List<Pas> pas = uwManager.selectAllPasList(null, null, tipe, kata, pilter, null, null, "bp_online",null,null,null);
		
		//map.put("listPas", pas);
		
		return new ModelAndView(
        "uw/viewer/viewer_bp_online", "cmd", pas);

    }
	
}
