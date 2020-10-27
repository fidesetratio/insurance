package com.ekalife.elions.web.rekruitment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;

import id.co.sinarmaslife.std.model.vo.DropDown;

/**
 * 
 * @author Fadly
 * @since 18 August 2011
 */


@SuppressWarnings("unchecked")
public class UploadRekruitmentController extends ParentMultiController{
	
	/*
	protected Connection connection = null;

	protected Connection getConnection() {
		if(this.connection==null) {
			try {
				this.connection = this.elionsManager.getUwDao().getDataSource().getConnection();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		}
		return this.connection;
	}
	*/
	
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lus_id = currentUser.getLus_id();
		Map m = new HashMap();
		String jenis = request.getParameter("jenis");
		String agent = request.getParameter("agent");
		m.put("jenis", jenis);
		m.put("agent", agent);
		//Fadly		
		
		List agentList = new ArrayList();
		if( jenis != null && !"".equals(jenis)){
			agentList = this.uwManager.selectAgentUploadNewBusinessList(jenis, lus_id);
		}
		List<DropDown> jenisList = new ArrayList<DropDown>();
		jenisList.add(new DropDown("1", "Regional"));
		jenisList.add(new DropDown("2", "Agency"));
		
		return new ModelAndView("rekruitment/upload_rekruitment", m).addObject("agentList", agentList).addObject("jenisList", jenisList);
	}


	//DATA MENU
	private Map referenceData(String spaj, String businessId, boolean printAll, boolean isViewer, boolean isBancass, User currentUser) {
		Map map = new HashMap();
		return map;
	}
	
}