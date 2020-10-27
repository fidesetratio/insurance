package com.ekalife.elions.web.uw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;

public class DataUsulanController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String spaj=request.getParameter("spaj");
		Map map=new HashMap();
		map.put("detail",elionsManager.selectDataUsulanDetail(spaj));
		map.put("lsKurs",elionsManager.selectAllLstKurs());
		map.put("lsCaraBayar",elionsManager.selectAllLstPayMode());
		map.put("lsAutoDebet",autodebet());
		map.put("productInsured", elionsManager.selectViewerInsured(spaj, new Integer(1)));
		map.put("benefeciary",elionsManager.select_benef(spaj));
		return new ModelAndView("uw/datausulan",map);
	}
	private List autodebet(){
		List list=new ArrayList();
		Map a=new HashMap();
		a.put("ID","0");
		a.put("VALUE","None");
		list.add(a);
		a=new HashMap();
		a.put("ID","1");
		a.put("VALUE","Credit Card");
		list.add(a);
		a=new HashMap();
		a.put("ID","2");
		a.put("VALUE","Tabungan");
		list.add(a);
		return list;
	}

}