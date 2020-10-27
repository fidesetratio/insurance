/**
 * 
 */
package com.ekalife.elions.web.worksite;

import id.co.sinarmaslife.std.util.DateUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;

public class InputInvoiceController extends ParentController{

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map map = new HashMap();
		String day, month, year;
		String mcl_id = request.getParameter("mcl_id");
		String jumlahInvoice = request.getParameter("jumlahInvoice");
		
		
		if( request.getParameter("dayId") != null && !request.getParameter("dayId").equals("")){
			day = request.getParameter("dayId");
			month = request.getParameter("monthId");
			year = request.getParameter("yearId");
		}else{
			day = request.getParameter("dayStart");
			month = request.getParameter("monthStart");
			year = request.getParameter("yearStart");
		}
		
		String monthPeriode = request.getParameter("monthPeriode");
		String yearPeriode = request.getParameter("yearPeriode");
		List detailperusahaan = this.uwManager.select_nama_perusahaan_by_mcl_id(mcl_id);
		Map tempDetailPerusahaan = (HashMap) detailperusahaan.get(0);
		String nama_perusahaan = tempDetailPerusahaan.get("MCL_FIRST").toString();
		if( jumlahInvoice == null ){
			jumlahInvoice = "0";
		}
		
		map.put("yearList", DateUtil.selectYearFromFewYearsAgoTillNow( 10, false ) );
		map.put("monthList", DateUtil.selectMonth() );
		map.put("dayList", DateUtil.selectDay());
		map.put("detailperusahaan", detailperusahaan);
		map.put("nama_perusahaan", nama_perusahaan);
        map.put("mcl_id", mcl_id);
        map.put("jumlahInvoice", jumlahInvoice);
        
        map.put("dayId", day);
        map.put("monthId", month);
        map.put("yearId", year);
        map.put("monthPeriode", monthPeriode);
        map.put("yearPeriode", yearPeriode);
        
			if(request.getParameter("submit")!=null){
				String sukses = "";
				try{
					String periode = yearPeriode + monthPeriode;
					BigDecimal maxNomor = uwManager.selectMaxNomorCompanyWs(periode, mcl_id);
					if( maxNomor == null ){
						maxNomor = new BigDecimal("1");
					}else{
						maxNomor = maxNomor.add(new BigDecimal("1"));
					}
					Date tglInvoice =  new SimpleDateFormat("dd/MM/yyyy").parse(day+"/"+month+"/"+year); 
					uwManager.insertMstCompanyWs(mcl_id, tglInvoice, null, new BigDecimal(jumlahInvoice), BigDecimal.ZERO, periode, maxNomor, "01");
					sukses = "sukses";
				}catch (Exception e) {
					sukses = "gagal";
					logger.error("ERROR :", e);
					// TODO: handle exception
				}
				map.put("sukses", sukses);
				
			}
			
		return new ModelAndView("worksite/input_invoice", "cmd", map);
	}
	

}
