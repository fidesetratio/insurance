package com.ekalife.elions.web.bac;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.FilePdf;
import com.ekalife.utils.parent.ParentController;

public class CustomerSignUploadController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Comparable> params = new HashMap<String, Comparable>();
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		params.put("spaj", spaj);

		FilePdf f = new FilePdf();
		f.setKey(products.kategoriPrintSpaj(spaj));
		
		if(!f.getKey().equals("")) {
			f = products.kategoriPosisiPrintSpaj(f);
			
			params.put("fileSpaj", f.getKey());
			params.put("x", f.getX());
			params.put("y", f.getY());
			params.put("f", f.getFontSize());
		}
		
		return new ModelAndView("bac/sign", params);
	}

}