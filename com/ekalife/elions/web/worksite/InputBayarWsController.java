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

import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentController;

public class InputBayarWsController extends ParentController{

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map map = new HashMap();
		String day, month, year;
		String jumlahBayar;
		String mcl_id = request.getParameter("mcl_id");
		String nomor = request.getParameter("nomor");
		String periode =request.getParameter("periode");
		String tgl_invoice = request.getParameter("tgl_invoice");
		String jml_invoice = request.getParameter("jml_invoice");
		Date dfmt = new SimpleDateFormat("dd/MM/yyyy").parse(tgl_invoice);
		String dfmtToIndo = DateUtil.toIndonesian(dfmt);
		
		
		if( request.getParameter("jumlahBayar") != null && !request.getParameter("jumlahBayar").equals("")){
			jumlahBayar = request.getParameter("jumlahBayar");
		}else{
			jumlahBayar = request.getParameter("jml_invoice");
		}
		
		if( request.getParameter("dayId") != null && !request.getParameter("dayId").equals("")){
			day = request.getParameter("dayId");
			month = request.getParameter("monthId");
			year = request.getParameter("yearId");
		}else{
			day = request.getParameter("dayStart");
			month = request.getParameter("monthStart");
			year = request.getParameter("yearStart");
		}
		
		List detailperusahaan = this.uwManager.select_nama_perusahaan_by_mcl_id(mcl_id);
		Map tempDetailPerusahaan = (HashMap) detailperusahaan.get(0);
		String nama_perusahaan = tempDetailPerusahaan.get("MCL_FIRST").toString();
		if( jumlahBayar == null ){
			jumlahBayar = "0";
		}
		
		map.put("yearList", DateUtil.selectYearFromFewYearsAgoTillNow( 10, false ) );
		map.put("monthList", DateUtil.selectMonth() );
		map.put("dayList", DateUtil.selectDay());
		map.put("detailperusahaan", detailperusahaan);
		map.put("nama_perusahaan", nama_perusahaan);
        map.put("mcl_id", mcl_id);
        map.put("jumlahBayar", jumlahBayar);
        map.put("nomor", nomor);
        map.put("tgl_invoice", tgl_invoice);
        map.put("jml_invoice", jml_invoice);
        map.put("periode", periode);
        map.put("dfmtToIndo", dfmtToIndo);
        
        
        map.put("dayId", day);
        map.put("monthId", month);
        map.put("yearId", year);
        
			if(request.getParameter("submit")!=null){
				String sukses = "";
				try{
					Date tglBayar =  new SimpleDateFormat("dd/MM/yyyy").parse(day+"/"+month+"/"+year); 
	                // parameter utk insert ke history ut ==========================================================================================================================================================
	                Map<String, Object> paramsHistoryUt = new HashMap<String, Object>();
	                paramsHistoryUt.put( "MCL_ID", mcl_id ); // primary key
	                paramsHistoryUt.put( "JENIS", 1 ); // primary key
	                paramsHistoryUt.put( "REG_SPAJ", "00000000000" );    // dummy utk primary key
	                paramsHistoryUt.put( "TAHUN_KE", 0 );   // dummy utk primary key
	                paramsHistoryUt.put( "PREMI_KE", 0 );   // dummy utk primary key
	                
	             	Integer maxNo = uwManager.selectMaxNoMstHistoryUT( paramsHistoryUt );
                	if( maxNo == null ) { maxNo = 1; }
                	else { maxNo = maxNo + 1; }
                	Date createDate = elionsManager.selectNowDate();
                	
                	paramsHistoryUt.put( "PERIODE", periode ); // primary key
                	paramsHistoryUt.put( "NO", maxNo ); // primary key
                	paramsHistoryUt.put( "LUS_ID", null );
                	paramsHistoryUt.put( "DESCR", "Telah dibayar sebesar Rp."+FormatString.formatCurrency(null,new BigDecimal(jumlahBayar))+ " Pd Tgl "+ dfmtToIndo +" Oleh "+ nama_perusahaan  );
                	paramsHistoryUt.put( "CREATE_DATE", createDate );
                	paramsHistoryUt.put( "FILE_NAME", null );
	                
					uwManager.updateMstCompanyWs(tglBayar, mcl_id, new BigDecimal(jumlahBayar), periode, new BigDecimal(nomor), paramsHistoryUt);
					sukses = "sukses";
				}catch (Exception e) {
					sukses = "gagal";
					logger.error("ERROR :", e);
					// TODO: handle exception
				}
				map.put("sukses", sukses);
				
			}
			
		return new ModelAndView("worksite/input_bayar_ws", "cmd", map);
	}
	

}
