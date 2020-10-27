package com.ekalife.elions.web.bac;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentFormController;

public class StatusBasFormController extends ParentFormController {
protected final Log logger = LogFactory.getLog( getClass() );	
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
	
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		HashMap map = new HashMap();
		Command cmd=(Command)command;
		ArrayList<HashMap> temp = Common.serializableList(elionsManager.selectLstStatusAccept(null));		
		Integer lssa_id = 15;
		ArrayList<HashMap> temp2 = Common.serializableList(uwManager.selectSubStatusAccept(lssa_id));
		ArrayList <HashMap> lsStatusAccept = new ArrayList<HashMap>();
		ArrayList <HashMap> lsSubStatusAccept = new ArrayList<HashMap>();		
		int pos = ServletRequestUtils.getIntParameter(request, "pos",1);
		if(pos == 1) {
			for(HashMap m : temp) {
				int lssa = ((BigDecimal) m.get("LSSA_ID")).intValue();
				logger.info(lssa);
				if(lssa == 15 || lssa == 16 ) {
					logger.info("OK");
					lsStatusAccept.add(m);
				}
			}
			for(HashMap m : temp2) {
			
					lsSubStatusAccept.add(m);
				
			}
		}else {
			lsStatusAccept.addAll(temp);
			lsSubStatusAccept.addAll(temp);
		}
		
		ArrayList lsUser =Common.serializableList(elionsManager.selectLstUser());
		map.put("lsStatusAccept", lsStatusAccept);
		map.put("lsSubStatusAccept", lsSubStatusAccept);
		map.put("lsUser", lsUser);
		
		return map;
		
	}
	
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String spaj = request.getParameter("spaj");
		Command command = new Command();
		command.setSpaj(spaj);
		
		command.setLspd_id(ServletRequestUtils.getIntParameter(request, "pos", 1));
		Integer lssa_id = ServletRequestUtils.getIntParameter(request, "lssa_id", 0);
       
		Date tgl = (Date) elionsManager.selectSysdateSimple();
		User currentUser = (User) request.getSession().getAttribute(
				"currentUser");
		String lus_id = currentUser.getLus_id();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		command.setFlag_ut(lssa_id);
		ArrayList lsPosition =Common.serializableList(bacManager.selectMstPositionSpajForStatusBas(spaj));
		Map mAdd = new HashMap();
		mAdd.put("LUS_ID", lus_id);
		mAdd.put("LSPD_ID", new Integer(1));
		mAdd.put("LSSA_ID_BAS", new Integer(15)); //FURTHER BAS
		mAdd.put("LSSP_ID", new Integer(1));
		mAdd.put("MSPS_DATE", df.format(tgl));
		mAdd.put("SUB_ID_BAS", new Integer(1));
		mAdd.put("SIZE", new Integer(lsPosition.size()));
		lsPosition.add(mAdd);
		
		
		if (currentUser.getEmail() == null
				|| currentUser.getEmail().trim().equals(""))
			command.setError(new Integer(2));

		command.setLsPosition(lsPosition);
		command.setLiAksep(new Integer(15));
		return command;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd,
			BindException err) throws Exception {
		Command command = (Command) cmd;
		String spaj = request.getParameter("spaj");
		String file1 = request.getParameter("file1");
		Integer li_insured=1;
		Integer lssa_id = ServletRequestUtils.getIntParameter(request, "lssa_id", 0);
		String emailTo = ServletRequestUtils.getStringParameter(request, "emailTo", "");
		Map mDataUsulan = (HashMap) uwManager.selectDataUsulan2(spaj);
		if (mDataUsulan.isEmpty())
			err.reject("", "Tolong hubungi ITwebandmobile@sinarmasmsiglife.co.id, Data Usulan Tidak Lengkap");
		if (mDataUsulan.get("LSBS_ID") == null)
			err.reject("", "Tolong hubungi ITwebandmobile@sinarmasmsiglife.co.id, Bisnis Id tidak ada ");
		else{
			command.setMsprTsi((Double)mDataUsulan.get("MSPR_TSI"));
			command.setMsprPremium((Double)mDataUsulan.get("MSPR_PREMIUM"));
			command.setLscbPayMode((String)mDataUsulan.get("LSCB_PAY_MODE"));
		}	
		
		String desc[] = new String[request.getParameterValues("textarea").length];
		desc = request.getParameterValues("textarea");
		String deskripsi = desc[desc.length - 1];		
		Map mTt = (HashMap) elionsManager.selectTertanggung(spaj);
		Map MPp = (HashMap) elionsManager.selectPemegang(spaj);
		command.setNamaTertanggung((String) mTt.get("MCL_FIRST"));
		command.setNamaPemegang((String) MPp.get("MCL_FIRST"));
		command.setLcaIdPp((String) MPp.get("LCA_ID"));		
		Map mGetStatus = (HashMap) elionsManager.selectWf_get_status(
				spaj, li_insured.intValue());
		if (deskripsi == null || deskripsi.equalsIgnoreCase(""))
			err.reject("", "Silahkan isi alasan proses ");

		li_insured = Integer.valueOf(mTt.get("MSTE_INSURED_NO")
						.toString());		
		
		String sLiAksep = request.getParameter("status");
		if (sLiAksep==null)sLiAksep="";
		if (sLiAksep.equals(""))
				err.reject("", "Silahkan Masukan Pilihan Anda");
		else {
				command.setLiAksep(Integer.valueOf(sLiAksep));
				if(command.getLiAksep()==15 || command.getLiAksep()==16 ){
					String sub = request.getParameter("substatus");
					Integer sub_id = Integer.valueOf(sub);
					command.setSubLiAksep(sub_id);
				}
				if (command.getLiAksep() == null)
					err.reject("", "Tidak ada pilihan status");				
		}
/*		if(sLiAksep.equals("15") && emailTo.equals("")){
			err.reject("", "Silahkan isi email ");
		}*/
		
//		Attachment file
		Upload upload = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		
		if(upload.getFile1().isEmpty()==false){
			String outputDir = props.getProperty("attachment.mailpool.dir") +"\\" +command.getLcaIdPp()+"\\"+spaj;
			String dest = outputDir +"\\"+ upload.getFile1().getOriginalFilename();
			File dirFile = new File(outputDir);
			File dirFile2 = new File(dest);
			if(!dirFile.exists()) dirFile.mkdirs();
			 FileCopyUtils.copy(upload.getFile1().getBytes(), dirFile2);
			 command.setAttachment(dirFile2);

		}
			
	}
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object cmd, BindException err)
			throws Exception {
		Command command = (Command) cmd;	
		command=bacManager.prosesStatusBas(command,request,err);
		command.setLsPosition(bacManager.selectMstPositionSpajForStatusBas(command.getSpaj()));
		return new ModelAndView("bac/status_bas", err.getModel()).addObject(
				"submitSuccess", "true").addAllObjects(
				this.referenceData(request, cmd, err));
	}
}
