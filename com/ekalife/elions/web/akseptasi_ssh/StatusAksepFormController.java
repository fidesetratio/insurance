package com.ekalife.elions.web.akseptasi_ssh;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.DataUsulan2;
import com.ekalife.elions.model.Position;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.Utama;
import com.ekalife.utils.parent.ParentFormController;

public class StatusAksepFormController extends ParentFormController {
	List lsStatAksep;
	private Utama utama;
	Integer lspdId=new Integer(31),lstbId=new Integer(1);
	List list;
	int flag;
	String lwkId,lsrgId,mspoPolicyNo,lcaId;
	Integer lsbsId;

	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map map=new HashMap();
		List lsStatus=elionsManager.selectLstStatusAccept(null);
		map.put("lsStatus",lsStatus);
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		list=new ArrayList();
		utama=new Utama();
		Position position=new Position();
		lwkId=null;lsrgId=null;mspoPolicyNo=null;
		flag=0;
		
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj=request.getParameter("spaj");
		lsStatAksep=elionsManager.selectMstPositionSpajAkseptasi(spaj);
		//add new
		position.setReg_spaj(spaj);
		position.setMsps_date(elionsManager.selectSysdate(new Integer(2)));
		position.setLus_id(currentUser.getLus_id());
		position.setLus_login_name(currentUser.getName());
		lsStatAksep.add(position);
		utama.setLsStatAksep(lsStatAksep);
		return utama;
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}

	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		DataUsulan2 dataUsulan=new DataUsulan2();
		String desc,statAksep;
		String spaj=request.getParameter("spaj");
		desc=request.getParameter("txtmsps_desc");
		statAksep=request.getParameter("statAksep");
		//
		list=elionsManager.selectMstPolicy(lspdId,lstbId,new Integer(10),new Integer(1),spaj,"4");
		Map a=(HashMap)list.get(0);
		lcaId=(String)a.get("LCA_ID");
		lwkId=(String)a.get("LWK_ID");
		lsrgId=(String)a.get("LSRG_ID");
		mspoPolicyNo=(String)a.get("MSPO_POLICY_NO");
		//
		dataUsulan=elionsManager.selectDataUsulan(spaj,new Integer(1)); //lstb =1=individu
		lsbsId=dataUsulan.getLsbs_id();
		//
		if(statAksep!=null){
			if(statAksep.equals("5")){//aksep
				if(mspoPolicyNo==null){
					mspoPolicyNo=elionsManager.f_get_nopolis(lcaId,lsbsId);
					if(mspoPolicyNo.length()<= 0 || mspoPolicyNo==null){ 
						err.reject("aksep.polis");
					}
					flag=1; //insert mst_position and update mst_policy
				}else
					flag=2; //insert mst_position
			}else if(statAksep.equals("2")){//tolak
				flag=3; //insert mst_position and update mst_policy
			}
		}else
			err.reject("aksep.status");
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		String statAksep=request.getParameter("statAksep");
		String spaj=request.getParameter("spaj");
		String desc=request.getParameter("txtmsps_desc");
		Position position=new Position();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		
		Map a=(HashMap)list.get(0);
		lcaId=(String)a.get("LCA_ID");
		lwkId=(String)a.get("LWK_ID");
		lsrgId=(String)a.get("LSRG_ID");
		mspoPolicyNo=(String)a.get("MSPO_POLICY_NO");
		position.setReg_spaj(spaj);
		position.setLus_id(currentUser.getLus_id());
		position.setMsps_desc(desc.toUpperCase());
		
		if(flag==1){//aksep = insert mst_position and update mst_policy
			position.setLspd_id(new Integer(31));
			position.setLssa_id(new Integer(5));
			elionsManager.prosesAksep1(mspoPolicyNo,spaj,lspdId,lstbId,position);
		}else if(flag==2){//aksep insert mst_position
			position.setLspd_id(new Integer(31));
			position.setLssa_id(new Integer(5));
			elionsManager.prosesAksep2(position);
		}else if(flag==3){//tolak =insert mst_position and update mst_policy
			position.setLspd_id(new Integer(31));
			position.setLssa_id(new Integer(2));
			elionsManager.prosesAksep3(mspoPolicyNo,new Integer(0),lspdId,lstbId,position);
			
		}
		lsStatAksep=elionsManager.selectMstPositionSpajAkseptasi(spaj);
		utama.setLsStatAksep(lsStatAksep);
		return new ModelAndView("akseptasi_ssh/w_aksep", err.getModel()).addObject("submitSuccess", "true").addObject("sukses","1").addAllObjects(this.referenceData(request,cmd,err));
	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}	

	
	
	
}
