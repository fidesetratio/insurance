package com.ekalife.elions.web.uw;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class FakultatifFormController extends ParentFormController {
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
	
	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		String spaj = request.getParameter("spaj");
		Command command = new Command();
		command.setSpaj(spaj);
		command.setFlag_ut(0);
		command.setLspd_id(ServletRequestUtils.getIntParameter(request, "pos", 2));
		Integer lssa_id = ServletRequestUtils.getIntParameter(request, "lssa_id", 0);
		
		Date tgl = (Date) elionsManager.selectSysdateSimple();
		User currentUser = (User) request.getSession().getAttribute(
				"currentUser");
		String lus_id = currentUser.getLus_id();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		//
		List lsPosition = uwManager.selectMstPositionSpajWithSubId(spaj);
		Map mAdd = new HashMap();
		mAdd.put("LUS_ID", lus_id);
		mAdd.put("LSPD_ID", new Integer(2));
		mAdd.put("LSSA_ID", new Integer(3)); //FURTHER
		mAdd.put("LSSP_ID", new Integer(10));
		mAdd.put("MSPS_DATE", df.format(tgl));
		mAdd.put("SUB_ID", new Integer(1));
		mAdd.put("SIZE", new Integer(lsPosition.size()));
		lsPosition.add(mAdd);
		//tambahan untuk attachment
		
		if (currentUser.getEmail() == null
				|| currentUser.getEmail().trim().equals(""))
			command.setError(new Integer(2));
		
		command.setLsPosition(lsPosition);
		command.setLiAksep(new Integer(3));
		return command;
}
	
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map map = new HashMap();
		return map;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd,
			BindException err) throws Exception {
		

	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object cmd, BindException err)
			throws Exception {
		Command command = (Command) cmd;
		
		return new ModelAndView("uw/status", err.getModel()).addObject(
				"submitSuccess", "true").addAllObjects(
				this.referenceData(request, cmd, err));

	}
	

}
