package com.ekalife.elions.web.bac;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;

public class SummaryController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<DropDown> list = new ArrayList<DropDown>();
		list.add(new DropDown("0", "Summary Biasa"));
		list.add(new DropDown("1", "Summary Recurring"));
		list.add(new DropDown("2", "Summary Per Plan"));
		list.add(new DropDown("3", "Summary Biasa Guthrie"));
		Map<String, List> map = new HashMap<String, List>();
		map.put("daftar", list);
		return new ModelAndView("bac/summary", map);
	}
	
}
