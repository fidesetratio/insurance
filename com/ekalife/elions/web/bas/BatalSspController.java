package com.ekalife.elions.web.bas;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

/**
 * @author : Daru
 * @since : Apr 16, 2013
 */
public class BatalSspController extends ParentController {

	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map map = new HashMap();
		
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		map.put("user_id", lus_id);
		
		
		if(request.getParameter("id") != null){
			String id = request.getParameter("id");
			Map ssp = uwManager.selectViewBatalSsp(id);
			if(ssp.get("MCP_TGL_BATAL") == null){
				Date nextBill = (Date) ssp.get("MCP_NEXT_BILL");
				ssp.put("MCP_TGL_BATAL", nextBill);
			}
			map.put("ssp", ssp);
		}
		
		if(request.getParameter("submit") != null){
			String id = request.getParameter("id");
			List err = new ArrayList();
			Map errStat = new HashMap();
			String tgl_btl = request.getParameter("tgl_batal");
			String alasan = request.getParameter("alasan");
			Date cancelDate = null;
			
			try{
				Pattern pattern = Pattern.compile("^(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d$");
				Matcher matcher = pattern.matcher(tgl_btl);
				if(!matcher.matches()){
					throw new Exception("Invalid Date Pattern");
				}
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				cancelDate = df.parse(tgl_btl);
			}catch(Exception e){
				err.add("Format tanggal batal salah, format yang benar adalah (dd/mm/yyyy).");
				errStat.put("tgl", "error");
			}
			
			if(alasan == null || alasan.trim().length() == 0){
				err.add("Harap masukkan alasan pembatalan account.");
				errStat.put("alasan", "error");
			}
			
			if(err.size() > 0){
				map.put("errorMessage", err);
				map.put("errorStatus", errStat);
				map.put("tgl", tgl_btl);
				map.put("alasan", alasan);
			}else{
				// Proses save batal account
				Integer lssp_id = 8;
				Integer lspd_id = 25;
				Integer mcp_flag_bill = 0;
				Date mcp_tgl_batal = cancelDate;
				String mcp_alasan = alasan;
				String mcp_note = "BATAL ACCOUNT SIMAS SAVING PLAN";
				
				boolean save = uwManager.updateBatalSsp(id, lssp_id, lspd_id, mcp_flag_bill, mcp_tgl_batal, mcp_alasan, mcp_note, lus_id);
				if(save){
					map.put("successMessage", "Data Berhasil Di Save.");
				}else{
					map.put("saveErrorMessage", "Save Data Gagal.");
				}
			}
			
		}
		
		return new ModelAndView("bas/batal_ssp", map);
	}

}
