package com.ekalife.elions.web.bac;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.ekalife.utils.parent.ParentFormController;

public class ReferralFormController extends ParentFormController {

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		return new HashMap();
	}
	
}
