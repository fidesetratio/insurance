package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.VoucherTaxi;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentMultiController;
import com.ibatis.common.resources.Resources;

/**
 * System inputan dan reporting pemakaian voucher taxi marketing
 * Req : Mba Wesni
 * @author : Daru
 * @since : Dec 18, 2013
 */
public class VoucherTaxiController extends ParentMultiController {

	private ArrayList<DropDown> deptList;
	
	public VoucherTaxiController() {
		deptList = new ArrayList<DropDown>();
		deptList.add(new DropDown("", ""));
		deptList.add(new DropDown("MNC", "MNC"));
		deptList.add(new DropDown("CORPORATE", "CORPORATE"));
		deptList.add(new DropDown("CORPORATE SBY", "CORPORATE SBY"));
		deptList.add(new DropDown("BANCASS", "BANCASS"));
		deptList.add(new DropDown("BANCASS OTHERS", "BANCASS OTHERS"));
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView input(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<Object,Object> map = new HashMap<Object,Object>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		VoucherTaxi voucherTaxi = new VoucherTaxi();
		
		ArrayList<DropDown> statusList = new ArrayList<DropDown>();
		statusList.add(new DropDown("0", "Normal"));
		statusList.add(new DropDown("1", "Dikembalikan"));
		map.put("statusList", statusList);
		
		String action = request.getParameter("action");
		if(action != null && action.equals("edit")) {
			String no = request.getParameter("no");
			if(no != null) {
				voucherTaxi = bacManager.selectVoucherTaxi(no);
			}
			
			map.put("action", action);
		}
		
		String submit = request.getParameter("submit");
		if(submit != null) {
			// Validasi
			ServletRequestDataBinder binder = createBinder(request, voucherTaxi);
			BindException err = new BindException(binder.getBindingResult());
			
			//binder.registerCustomEditor(BigDecimal.class, "msvt_cost", integerEditor);
			binder.registerCustomEditor(Date.class, "msvt_release_date", dateEditor);
			binder.registerCustomEditor(Date.class, "msvt_return_date", dateEditor);
			
			binder.bind(request);
			
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msvt_no", "", "No Voucher harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msvt_msag_id", "", "Kode Agen harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msvt_user_name", "", "Nama Marketing harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msvt_user_dept", "", "Departemen harus diisi");
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "msvt_status_flag", "", "Status Voucher harus diisi");
			// Check status voucher, 0 = normal
			if(new Integer(0).equals(voucherTaxi.getMsvt_status_flag())) {
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msvt_from", "", "Tempat Keberangkatan harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msvt_to", "", "Tempat Tujuan harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msvt_release_date", "", "Tanggal Berangkat harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msvt_return_date", "", "Tanggal Kembali harus diisi");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msvt_cost_formatted", "", "Total Biaya harus diisi");
			} else {
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "msvt_status_desc", "", "Keterangan harus diisi");
			}
			
			// Check apakah no voucher sudah pernah di input
			VoucherTaxi checkVoucher = bacManager.selectVoucherTaxi(voucherTaxi.getMsvt_no());
			if(action == null && checkVoucher != null && checkVoucher.getMsvt_no() != null) {
				err.rejectValue("msvt_no", "", "No. Voucher sudah pernah di input");
			}
			
			// Check panjang karakter kolom keterangan
			if(voucherTaxi.getMsvt_status_desc() != null) {
				if(voucherTaxi.getMsvt_status_desc().length() > 200)
					err.rejectValue("msvt_no", "", "Keterangan tidak boleh lebih dari 200 karakter");
			}
			
			try {
				String cost = request.getParameter("msvt_cost_formatted");
				if(cost != null && !cost.trim().equals("")) {
					Long lCost = (Long) NumberFormat.getInstance().parse(cost);
					if(new Long(0).equals(lCost)) {
						err.rejectValue("msvt_cost_formatted", "", "Total biaya harus diisi.");
					} else {
						voucherTaxi.setMsvt_cost(BigDecimal.valueOf(lCost));
					}
				}
			} catch(ParseException e) {
				err.rejectValue("msvt_cost_formatted", "", "Total biaya tidak valid");
			} catch(NullPointerException npe) {
				err.rejectValue("msvt_cost_formatted", "", "Total biaya harus diisi.");
			}
			
			try {
				String release_date = request.getParameter("msvt_release_date");
				if(release_date != null && !release_date.trim().equals("")) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					sdf.parse(release_date);
				}
			} catch(ParseException e) {
				err.rejectValue("msvt_release_date", "", "Harap isi Tanggal Berangkat dengan format yang benar (dd/MM/yyyy)");
			}
			
			try {
				String return_date = request.getParameter("msvt_return_date");
				if(return_date != null && !return_date.trim().equals("")) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					sdf.parse(return_date);
				}
			} catch(ParseException e) {
				err.rejectValue("msvt_return_date", "", "Harap isi Tanggal Kembali dengan format yang benar (dd/MM/yyyy)");
			}
			
			if(err.hasErrors()) {
				map.putAll(err.getModel());
			} else {
				if(action != null && action.equals("edit")) {
					bacManager.updateVoucherTaxi(voucherTaxi, currentUser.getLus_id());
					map.remove("action");
				} else {
					bacManager.insertVoucherTaxi(voucherTaxi, currentUser.getLus_id());
				}
				voucherTaxi = new VoucherTaxi();
				map.put("success", "Data berhasil disimpan");
			}
		}
		
		// Jika status flagnya null set ke default (0)
		if(voucherTaxi.getMsvt_status_flag() == null)
			voucherTaxi.setMsvt_status_flag(0);
		
		ArrayList<String> listNoVoucher = Common.serializableList(bacManager.selectAllNoVoucherTaxi(currentUser));
		map.put("listNoVoucher", listNoVoucher);
		map.put("deptList", deptList);
		map.put("command", voucherTaxi);
		
		return new ModelAndView("common/input_voucher_taxi", map);
	}
	
	public ModelAndView view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("deptList", deptList);
		
		String submit = request.getParameter("submit");
		if(submit != null) {
//			String server = request.getServerName();
//			
//			if(server.equals("localhost")) {
//				server = "http://localhost:8080/E-MNC";
//			} else if(server.equals("daru-laptop")) {
//				server = "http://daru-laptop:8080/E-MNC";
//			} else {
//				server = "http://emnc.sinarmasmsiglife.co.id";
//			}
			
			String from_date = ServletRequestUtils.getStringParameter(request, "from_date", null);
			String to_date = ServletRequestUtils.getStringParameter(request, "to_date", null);
			String dept = ServletRequestUtils.getStringParameter(request, "dept", null);
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("from_date", from_date);
			params.put("to_date", to_date);
			params.put("dept", dept);
	    	params.put("title", "Report Voucher Taxi");
			
			@SuppressWarnings("rawtypes")
			ArrayList dataSource = Common.serializableList(bacManager.selectReportVoucherTaxi(params));
			if(dataSource.size() > 0) {
				ServletOutputStream stream = null;
				try {
					stream = response.getOutputStream();
					File file = Resources.getResourceAsFile(props.getProperty("report.voucher_taxi") + ".jasper");
					JasperReport jasperReport = (JasperReport)JRLoader.loadObject(file);
	        		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(dataSource));
	        		if("View PDF".equalsIgnoreCase(submit)) {
	        			JasperExportManager.exportReportToPdfStream(jasperPrint, stream);
	        		} else if("View XLS".equalsIgnoreCase(submit)) {
	        			response.setContentType("application/vnd.ms-excel");
	        			JRXlsExporter exporter = new JRXlsExporter();
	        			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
	        			exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, stream);
	        			exporter.exportReport();
	        		}
				} catch(IOException e) {
					logger.error("ERROR :", e);
				} catch(JRException e) {
					logger.error("ERROR :", e);
				} finally {
					if(stream != null)
						stream.close();
				}
			} else {
				ServletOutputStream stream = null;
				try {
					stream = response.getOutputStream();
					stream.println("<script>alert('Tidak ada data');window.close();</script>");
				} catch(IOException e) {
					logger.error("ERROR :", e);
				} finally {
					if(stream != null)
						stream.close();
				}
			}
			
			return null;
			
//			try {
//				response.sendRedirect(
//						server + "/report.pdf?window=reportVoucherTaxi&format=pdf&attached=false&reff=E-Lions" + 
//						"&from_date=" + from_date + 
//						"&to_date=" + to_date +
//						"&dept=" + dept
//				);
//				
//				return null;
//			} catch(Exception e) {
//				//logger.error("ERROR :", e);
//			}
		}
		
		return new ModelAndView("common/view_voucher_taxi", map);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ModelAndView cariMarketing(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filter = ServletRequestUtils.getStringParameter(request, "filter", "");
		String nama = ServletRequestUtils.getStringParameter(request, "nama", "");
		String dept = ServletRequestUtils.getStringParameter(request, "dept", "MNC");
		
		// Tentukan apakah query ke ajsdb / ebdb
		String jenis = "individu";
		if("MNC,CORPORATE,CORPORATE SBY".indexOf(dept) > -1)
			jenis = "eb";
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("filter", filter);
		params.put("nama", nama);
		params.put("dept", dept);
		params.put("jenis", jenis);
		
		ArrayList listMarketing = Common.serializableList(bacManager.selectMarketing(params));
		
		Map map = new HashMap();
		map.put("deptList", deptList);
		map.put("filter", filter);
		map.put("nama", nama);
		map.put("dept", dept);
		map.put("listMarketing", listMarketing);
		
		return new ModelAndView("common/cari_marketing", map);
	}
	
}
