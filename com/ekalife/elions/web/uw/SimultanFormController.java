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
import com.ekalife.utils.parent.ParentFormController;

public class SimultanFormController extends ParentFormController{

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
		command.setCount(new Integer(0));
		boolean sim=false;
		//
		Map a = elionsManager.selectCheckPosisi(spaj);
		int li_pos=Integer.parseInt(a.get("LSPD_ID").toString());
		String ls_pos=a.get("LSPD_POSITION").toString();
		Map map=uwManager.selectDataUsulan(spaj);
		Integer lsbsId=(Integer)map.get("LSBS_ID");
		//validasi Posisi SPAJ harus UW (2)
		if(li_pos !=2 ){
			sim=true;
			info=1;
			//MessageBox('Info', 'Posisi Polis Ini Ada di ' + ls_pos )
		}else if(lsbsId.intValue()==161){//produk sinarmas
			sim=true;
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
		//
		//cek mcl_id
		if(!(mclIdTt.substring(0,2).equalsIgnoreCase("XX")))
			if(!(mclIdTt.substring(0,2).equalsIgnoreCase("WW")))
				if(!(mclIdPp.substring(0,2).equalsIgnoreCase("XX")))
					if(!(mclIdPp.substring(0,2).equalsIgnoreCase("WW"))){
						sim=true;
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
		lsSimultanPp=elionsManager.selectSimultan(param);
		param=new HashMap();
		param.put("mcl_id",mclIdTt);
		param.put("nama",mclFirstTt);
		param.put("tgl_lhr",sDateBirthTt);
		lsSimultanTt=elionsManager.selectSimultan(param);
		if(logger.isDebugEnabled())logger.debug(""+lsSimultanPp.size());
		if(logger.isDebugEnabled())logger.debug(""+lsSimultanTt.size());
		Integer proses=null;
		if(logger.isDebugEnabled())logger.debug("isi list simultan ="+lsSimultanTt.size());
		if(sim==false)
		if(lsSimultanTt.size()==1 && lsSimultanPp.size()==1){//tidak ada simultan dengan polis lain (CLient New)
			//langsung proses
			//jika pemegang == tertanggung (diri sendiri)
			if(lsreIdPp.intValue()==1){//
				proses=elionsManager.prosesSimultan(1,spaj,lcaIdPp,mclIdPp,null);
				info=100;
				if(proses==null){
					info=103;
					//MessageBox('F_Get_Counter (Error)', 'Empty Number When Get Counter ...')
				}
				//1 generate mcl id baru
				//2 insert client_new, address new
				//3 update policy and insured
				
			}else{//jika beda
				info=100;
				proses=elionsManager.prosesSimultan(2,spaj,lcaIdPp,mclIdPp,mclIdTt);
				//proses=elionsManager.prosesSimultan(3,spaj,lcaIdPp,mclIdTt,null);
				if(proses==null){
					info=103;
					//MessageBox('F_Get_Counter (Error)', 'Empty Number When Get Counter ...')
				}
				//pemegang
				//1 generate mcl id baru
				//2 insert client_new, address new
				//3 update policy 
				//tertanggung
				//1 generate mcl id baru
				//2 insert client_new, address new
				//3 update insured
			}
			
		}
		command.setSpaj(spaj);
		command.setLsreIdPp(lsreIdPp);
		command.setLcaIdPp(lcaIdPp);
		command.setLsSimultanPp(lsSimultanPp);
		command.setLsSimultanTt(lsSimultanTt);
		command.setFlagAdd(lsreIdPp.intValue());
		command.setError(new Integer(info));
		command.setFlagId(ls_pos);
		
		return command;
	}
	
//	public static String str(String input, String nol) {
//		int pjg_in, pjg_nol;
//		String satu = "";
//
//		pjg_in = input.length();
//		pjg_nol = nol.length();
//
//		if (pjg_in < 10)
//			satu = nol.substring(0, pjg_nol - pjg_in);
//
//		input = satu + input;
//
//		return input;
//
//	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		String p=request.getParameter("choosePp");
		String t=request.getParameter("chooseTt");
		Command command=(Command)cmd;
		
		int choosePp=0;
		int chooseTt=0;
		if(!p.equals(""))
			choosePp=Integer.parseInt(p);
		
		if(!t.equals(""))
			chooseTt=Integer.parseInt(t);
		String mclIdPp,mclIdTt;
		Map mapPp=(HashMap)command.getLsSimultanPp().get(choosePp);
		Map mapTt=(HashMap)command.getLsSimultanTt().get(chooseTt);
		mclIdPp=(String)mapPp.get("MCL_ID");
		if(logger.isDebugEnabled())logger.debug(mclIdPp);
		mclIdTt=(String)mapTt.get("MCL_ID");
		//String mclIdPp=request.getParameter("mclIdPp");
		//String mclIdTt=request.getParameter("mclIdTt");
		
		boolean tt=false,pp = false;
		Integer proses=null;
		int i=0;
		//ada simultan dengan polis lain 
		//jika pemegang == tertanggung (diri sendiri)
		if(command.getLsreIdPp().intValue()==1){
				if(mclIdTt.substring(0,2).equalsIgnoreCase("XX")|| mclIdTt.substring(0,2).equalsIgnoreCase("WW")){
					proses=elionsManager.prosesSimultan(3,command.getSpaj(),command.getLcaIdPp(),mclIdTt,null);
					//info=100;
					if(proses==null){
						i=3;
					}
					//jika yang dipilih client nya new
					//1 generate mcl id baru
					//2 insert client_new, address new
					//3 update policy and insured
				}else{
					proses=elionsManager.prosesSimultan(4,command.getSpaj(),command.getLcaIdPp(),mclIdTt,null);
					//info=100;
					if(proses==null){
						i=3;
					}
					pp=true;
					tt=true;
					//jika client old
					//1 update policy and insured
				}
		}else{//jika beda
			proses=elionsManager.prosesSimultan(5,command.getSpaj(),command.getLcaIdPp(),mclIdPp,mclIdTt);
			//info=100;
			if(proses==null){
				i=3;
				//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}
			//proses pemegang
			if(mclIdPp.substring(0,2).equalsIgnoreCase("XX")|| mclIdPp.substring(0,2).equalsIgnoreCase("WW")){
				//jika yang dipilih client nya new
				//1 generate mcl id baru
				//2 insert client_new, address new
				//3 update policy 
			}else{
				pp=true;
				//jika client old
				//1 update policy 
			}
			//proses tertanggung
			if(mclIdTt.substring(0,2).equalsIgnoreCase("XX")|| mclIdTt.substring(0,2).equalsIgnoreCase("WW")){
				//jika yang dipilih client nya new
				//1 generate mcl id baru
				//2 insert client_new, address new
				//3 update insured
			}else{
				tt=true;
				//jika client old
				//1 update insured
			}
		}
		 i=cek_premi(mclIdPp,mclIdTt,tt,pp,cmd);
		 command.setError(new Integer(100+i));
		 if(i==0){
			return new ModelAndView("uw/simultan", err.getModel()).addObject("submitSuccess", "true").addObject("p",""+choosePp).addObject("t",""+chooseTt).addObject("info",""+(100+i)).addAllObjects(this.referenceData(request,cmd,err));
		}else{
			return new ModelAndView("uw/simultan", err.getModel()).addObject("submitSuccess", "true").addObject("p",""+choosePp).addObject("t",""+chooseTt).addObject("info",""+(100+i)).addAllObjects(this.referenceData(request,cmd,err));
		}
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