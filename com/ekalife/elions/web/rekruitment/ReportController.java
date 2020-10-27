package com.ekalife.elions.web.rekruitment;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentMultiController;

public class ReportController extends ParentMultiController {

	public ModelAndView reportAAJIkandidat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
		return new ModelAndView("rekruitment/report_aaji_kandidat_karyawan", map);
	}
}
