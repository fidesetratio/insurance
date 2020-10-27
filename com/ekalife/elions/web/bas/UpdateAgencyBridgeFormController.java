package com.ekalife.elions.web.bas;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;
import com.google.gwt.http.client.Request;

public class UpdateAgencyBridgeFormController extends ParentFormController{ 
		@Override
		protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
			binder.registerCustomEditor(Double.class, null, doubleEditor); 
			binder.registerCustomEditor(Integer.class, null, integerEditor); 
			binder.registerCustomEditor(Date.class, null, dateEditor); 
		}
		
		@Override
		protected Map referenceData(HttpServletRequest request, Object comand, Errors err) throws Exception {
			Map<String, List> map = new HashMap<String, List>();
			
			List<DropDown> lsStatus = new ArrayList<DropDown>();
			List<Map> lsBridge = new ArrayList<Map>();
			lsBridge = uwManager.selectCabangBridge();
			
			map.put("lsBridge", lsBridge);
			map.put("lsStatus", lsStatus);
			return map;
		}
		
		@Override
		protected Object formBackingObject(HttpServletRequest request) throws Exception {
			Command cmd = new Command();
			Map map = new HashMap();
			String msag_id = ServletRequestUtils.getStringParameter(request, "msag_id","");
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Map data = (HashMap) this.uwManager.dataAgenBridge(msag_id);
			//cmd.setMsag_id((String)data.get("MSAG_ID"));
			cmd.setMsag_id(msag_id);
			cmd.setMapBridge(uwManager.dataAgenBridge(msag_id));
			cmd.getMapBridge();
			cmd.getBridge();
			return cmd;
		}
		
		@Override
		protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException err) throws Exception {
			Map map = new HashMap();
			Command cmd = (Command)command; //casting parameter
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode","");
			String msag_id = cmd.getMsag_id();
			String bridge =  ServletRequestUtils.getStringParameter(request, "lsBridge","");
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			if(!cmd.getMsag_id().equals("")){
			uwManager.updateKodeBridge(cmd.getMsag_id(), bridge);
			cmd.setBridge(bridge);
			map.put("pesan", "Data Agen Berhasil Diupdate");
			map.put("msag_id", cmd.getMsag_id());
			}else{
				map.put("pesan", "Kode Agen/ Data Agen Kosong, Silakan Lakukan Pencarian Terlebih Dahulu");
			}
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/updatebridge.htm")).addAllObjects(map);
		}

}
