package com.ekalife.elions.web.uw;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class SimultanFormControllerNew extends ParentFormController{

	protected Map referenceData(HttpServletRequest arg0, Object arg1, Errors arg2) throws Exception {
		Map map=new HashMap();
		map.put("lsIdentity",elionsManager.selectAllLstIdentity());
		return map;
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String spaj;
		Integer lsreIdPp;
		String lcaIdPp;
		int info;
		List lsSimultanTt,lsSimultanPp;

		spaj=request.getParameter("spaj");
		info=0;
		Command command=new Command();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		command.setCurrentUser(currentUser);
		command.setCount(new Integer(0));
		//
		Map a = elionsManager.selectCheckPosisi(spaj);
		int li_pos=Integer.parseInt(a.get("LSPD_ID").toString());
		String ls_pos=a.get("LSPD_POSITION").toString();
		Map map=uwManager.selectDataUsulan(spaj);
		Integer lsbsId=(Integer)map.get("LSBS_ID");
		//validasi Posisi SPAJ harus UW (2)
		if(li_pos !=2 ){
			info=1;
			//MessageBox('Info', 'Posisi Polis Ini Ada di ' + ls_pos )
		}else if(lsbsId.intValue()==161){//produk sinarmas
			info=3;
		}
		//
		Map mPemegang=elionsManager.selectPemegang(spaj);
		
		String mclIdPp,mclFirstPp,sDateBirthPp;
		Date mspeDateBirthPp,mspeDateBirthTt;
		Integer lsreIdTt;
		String mclIdTt,mclFirstTt,sDateBirthTt,lcaIdTt;
		//data pemegang
		lsreIdPp=(Integer)mPemegang.get("LSRE_ID");
		mclIdPp=(String)mPemegang.get("MCL_ID");
		mclFirstPp=(String)mPemegang.get("MCL_FIRST");
		mspeDateBirthPp=(Date)mPemegang.get("MSPE_DATE_BIRTH");
	    sDateBirthPp=defaultDateFormatStripes.format(mspeDateBirthPp);
		lcaIdPp=(String)mPemegang.get("LCA_ID");
		//data tertanggung
		Map mTertanggung=elionsManager.selectTertanggung(spaj);
		lsreIdTt=(Integer)mTertanggung.get("LSRE_ID");
		mclIdTt=(String)mTertanggung.get("MCL_ID");
		mclFirstTt=(String)mTertanggung.get("MCL_FIRST");
		mspeDateBirthTt=(Date)mTertanggung.get("MSPE_DATE_BIRTH");
	    sDateBirthTt=defaultDateFormatStripes.format(mspeDateBirthTt);
		lcaIdTt=(String)mTertanggung.get("LCA_ID");
		//cek mcl_id
		if(!(mclIdTt.substring(0,2).equalsIgnoreCase("XX")))
			if(!(mclIdTt.substring(0,2).equalsIgnoreCase("WW")))
				if(!(mclIdPp.substring(0,2).equalsIgnoreCase("XX")))
					if(!(mclIdPp.substring(0,2).equalsIgnoreCase("WW"))){
						info=2;
						//MessageBox('Info', 'Proses Simultan Sudah pernah dilakukan untuk Pemegang & Tertanggung Polis!!!')
					}	
		int spasi,titik,koma,pjg;
		//pemegang ambil nama depan saja 
		spasi=mclFirstPp.indexOf(' ');
		titik=mclFirstPp.indexOf('.');
		koma=mclFirstPp.indexOf(',');
		pjg=mclFirstPp.length();
		if(spasi>0)
			mclFirstPp=mclFirstPp.substring(0,spasi);
		else if(titik>0)
			mclFirstPp=mclFirstPp.substring(0,titik);
		else if(koma>0)
			mclFirstPp=mclFirstPp.substring(0,koma);
		//Tertanggung ambil nama depan saja 
		spasi=mclFirstTt.indexOf(' ');
		titik=mclFirstTt.indexOf('.');
		koma=mclFirstTt.indexOf(',');
		pjg=mclFirstTt.length();
		if(spasi>0)
			mclFirstTt=mclFirstTt.substring(0,spasi);
		else if(titik>0)
			mclFirstTt=mclFirstTt.substring(0,titik);
		else if(koma>0)
			mclFirstTt=mclFirstTt.substring(0,koma);
		//
		Map param=new HashMap();
		param.put("mcl_id",mclIdPp);
		param.put("nama",mclFirstPp);
		param.put("tgl_lhr",sDateBirthPp);
		lsSimultanPp=elionsManager.selectSimultanNew(param);
		param=new HashMap(); 
		param.put("mcl_id",mclIdTt);
		param.put("nama",mclFirstTt);
		param.put("tgl_lhr",sDateBirthTt);
		lsSimultanTt=elionsManager.selectSimultanNew(param);
		if(logger.isDebugEnabled())logger.debug(""+lsSimultanPp.size());
		if(logger.isDebugEnabled())logger.debug(""+lsSimultanTt.size());
		Integer proses=null;
		if(logger.isDebugEnabled())logger.debug("isi list simultan ="+lsSimultanTt.size());

		command.setSpaj(spaj);
		command.setLsreIdPp(lsreIdPp);
		command.setLcaIdPp(lcaIdPp);
		command.setLsSimultanPp(lsSimultanPp);
		command.setLsSimultanTt(lsSimultanTt);
		command.setRowPp(lsSimultanPp.size());
		command.setRowTt(lsSimultanTt.size());
		command.setFlagAdd(lsreIdPp.intValue());
		command.setError(new Integer(info));
		command.setFlagId(ls_pos);
		command.setMapPemegang(mPemegang);
		command.setMapTertanggung(mTertanggung);
		command.setMclId(mclIdTt);
		return command;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		//err.reject("","test");
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String p=request.getParameter("choosePp");
		String t=request.getParameter("chooseTt");
		Command command=(Command)cmd;
		
		int choosePp=0;
		int chooseTt=0;
		if(!p.equals(""))
			choosePp=Integer.parseInt(p);
		
		if(!t.equals(""))
			chooseTt=Integer.parseInt(t);
		
		if(command.getLsreIdPp()==1){//hbungan diri sendiri
			choosePp=chooseTt;
		}
		String mclIdPp,mclIdTt,idSimultanPp,idSimultanTt;
		Map mapPp=(HashMap)command.getLsSimultanPp().get(choosePp);
		Map mapTt=(HashMap)command.getLsSimultanTt().get(chooseTt);
		idSimultanPp=(String)mapPp.get("ID_SIMULTAN");
		idSimultanTt=(String)mapTt.get("ID_SIMULTAN");
		
		//mcl_id ambil yang baru karena data baru ini yang akan di isi
		Map mPpOld=command.getMapPemegang();
		Map mTtOld=command.getMapTertanggung();
		mclIdPp=(String)mPpOld.get("MCL_ID");
		mclIdTt=(String)mTtOld.get("MCL_ID");
		
		boolean tt=false,pp = false;
		Integer proses=null;
		int i=0;
		proses=elionsManager.prosesSimultanNew(command,mclIdPp,mclIdTt,idSimultanPp,idSimultanTt);
		if(proses==null){
			i=3;
		}
		 i=cek_premi(mclIdPp,mclIdTt,tt,pp,cmd);
		 command.setError(new Integer(100+i));
		return new ModelAndView("uw/simultan_new", err.getModel()).addObject("submitSuccess", "true").addObject("p",""+choosePp).addObject("t",""+chooseTt).addObject("info",""+(100+i)).addAllObjects(this.referenceData(request,cmd,err));
	}
	
	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}	
	
	private int cek_premi(String mclIdPp,String mclIdTt,boolean lb_tt_lama,boolean lb_pp_lama,Object cmd)throws Exception{
		int i=0;
		Command command=(Command)cmd;
		if(lb_tt_lama || lb_pp_lama){
			Double ldec_kurs,ldec_premi;
			ldec_kurs=(Double)elionsManager.selectLstMonthlyKurs("02");
			//
			if(ldec_kurs==null)
				ldec_kurs=new Double(8000);
			//
			if(lb_tt_lama){
				ldec_premi=(Double)elionsManager.selectCekPremi(ldec_kurs,mclIdTt,"01",new Integer(1));
				
				if(ldec_premi!=null)
					if(ldec_kurs.doubleValue()>=500000000){
						//MessageBox('Informasi', 'Tertanggung ini Memiliki Premi >= Rp 500jt !!!')
						return 2;
					}
			}
			//
			if(lb_pp_lama && (command.getLsreIdPp().intValue()==1 )){
				ldec_premi=elionsManager.selectCekPremi(ldec_kurs,mclIdPp,"01",new Integer(1));
				if(ldec_premi!=null)
					if(ldec_kurs.doubleValue()>=500000000){
						//MessageBox('Informasi', 'Pemegang Polis ini Memiliki Premi >= Rp 500jt !!!')
						return 4;
					}
			}
		}
		return i;
	}

}