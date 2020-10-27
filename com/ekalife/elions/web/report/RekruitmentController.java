package com.ekalife.elions.web.report;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentJasperReportingController;
import com.ibatis.common.resources.Resources;

public class RekruitmentController extends ParentJasperReportingController {

	/**
	 * @author alfian_h
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * http://localhost/E-Lions/report/rekrut.htm?window=reportAAJIkandidat
	 */
	public ModelAndView reportAAJIkandidat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String tglA = ServletRequestUtils.getStringParameter(request, "tglA");
		String tglB = ServletRequestUtils.getStringParameter(request, "tglB");
		
		if(request.getParameter("print")!=null){
			@SuppressWarnings("rawtypes")
			List  data = bacManager.selectAAJICalonKaryawan(df.parse(tglA), df.parse(tglB), null);
			if(data.size()<=0){
//				map.put("pesan", "Data tidak ditemukan");
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
			}else{
				ServletOutputStream sos = response.getOutputStream();
				File sourceFile = Resources.getResourceAsFile(props.getProperty("report.status_aaji") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		
	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("tglA", tglA);
	    		params.put("tglB", tglB);
	    		
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));
	    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos); // save as pdf
	    		sos.close();
			}
			return null;
		}
		
		return new ModelAndView("report/report_aaji_kandidat_karyawan", map);
	}
}
