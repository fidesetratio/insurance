package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;

public class DetailSimultanController extends ParentController {
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		int info=0;
		String sInfo=request.getParameter("info");
		if(sInfo==null)
			info=0;
		else 
			info=Integer.parseInt(sInfo);
		
		String spajAwal=request.getParameter("spajAwal");
		String flag_tt=request.getParameter("flag_tt");
		String flag_pp=request.getParameter("flag_pp");
		String flags=request.getParameter("brs");
		
		String txtmclid_tt,txtnama_tt,txttgllhr_tt = null,txtlcaid_tt;
		String txtmclid_pp,txtnama_pp,txttgllhr_pp = null,txtlcaid_pp;
		Date tgl_lhr_tt,tgl_lhr_pp;
		List lsSimultanPp,lsSimultanTt;
		Map mapPp,mapTt;
		mapPp=elionsManager.selectPemegang(spajAwal);
		mapTt=elionsManager.selectTertanggung(spajAwal);
		//
		txtmclid_tt=(String)mapTt.get("MCL_ID");
		if(mapTt.get("MCL_FIRST")!=null)
			txtnama_tt=(String)mapTt.get("MCL_FIRST");	
		else
			txtnama_tt=null;
		tgl_lhr_tt=(Date) mapTt.get("MSPE_DATE_BIRTH");
		if(tgl_lhr_tt!=null)
			txttgllhr_tt=defaultDateFormatStripes.format(tgl_lhr_tt);
		if(mapTt.get("LCA_ID")!=null)
			txtlcaid_tt=(String)mapTt.get("LCA_ID");
		//
		txtmclid_pp=(String)mapPp.get("MCL_ID");
		if(mapPp.get("MCL_FIRST")!=null)
			txtnama_pp=(String)mapPp.get("MCL_FIRST");	
		else
			txtnama_pp=null;
		tgl_lhr_pp=(Date) mapPp.get("MSPE_DATE_BIRTH");
		if(tgl_lhr_pp!=null)
			txttgllhr_pp=defaultDateFormatStripes.format(tgl_lhr_pp);
		if(mapPp.get("LCA_ID")!=null)
			txtlcaid_pp=(String)mapPp.get("LCA_ID");
		//
		int spasi,titik,koma,pjg;
		//pemegang ambil nama depan saja 
		spasi=txtnama_pp.indexOf(' ');
		titik=txtnama_pp.indexOf('.');
		koma=txtnama_pp.indexOf(',');
		pjg=txtnama_pp.length();
		if(spasi>0)
			txtnama_pp=txtnama_pp.substring(0,spasi);
		else if(titik>0)
			txtnama_pp=txtnama_pp.substring(0,titik);
		else if(koma>0)
			txtnama_pp=txtnama_pp.substring(0,koma);
		//Tertanggung ambil nama depan saja 
		spasi=txtnama_tt.indexOf(' ');
		titik=txtnama_tt.indexOf('.');
		koma=txtnama_tt.indexOf(',');
		pjg=txtnama_tt.length();
		if(spasi>0)
			txtnama_tt=txtnama_tt.substring(0,spasi);
		else if(titik>0)
			txtnama_tt=txtnama_tt.substring(0,titik);
		else if(koma>0)
			txtnama_tt=txtnama_tt.substring(0,koma);
		//
		Map param=new HashMap();
		param.put("mcl_id",txtmclid_pp);
		param.put("nama",txtnama_pp);
		param.put("tgl_lhr",txttgllhr_pp);
		lsSimultanPp=elionsManager.selectSimultan(param);
		param=new HashMap();
		param.put("mcl_id",txtmclid_tt);
		param.put("nama",txtnama_tt);
		param.put("tgl_lhr",txttgllhr_tt);
		lsSimultanTt=elionsManager.selectSimultan(param);
		
		String mclIdChoose = request.getParameter("mclId");
		int flag = 0,flagTt,flagPp;
		String spaj;
		int brs=0;
		String sBrs=request.getParameter("brs");
		if(sBrs!=null)
			brs=Integer.parseInt(sBrs);
		
		if(flag_tt!=null){
			flagTt=Integer.parseInt(flag_tt);
			Map a=(HashMap)lsSimultanTt.get(flagTt);
			mclIdChoose=a.get("MCL_ID").toString();
			flag=flagTt;
			info=1;
		}else if(flag_pp!=null){
			flagPp=Integer.parseInt(flag_pp);
			Map a=(HashMap)lsSimultanPp.get(flagPp);
			mclIdChoose=a.get("MCL_ID").toString();
			flag=flagPp;
			info=2;
		}else if(flags!=null){
			flag=Integer.parseInt(flags);
//			if(info==1){
//				Map a=(HashMap)lsSimultanTt.get(flag);
//				mclIdChoose=a.get("MCL_ID").toString();
//			}else if(info==2){
//				Map a=(HashMap)lsSimultanPp.get(flag);
//				mclIdChoose=a.get("MCL_ID").toString();
//			}
//			flag=brs;
		}
		
		List lsSimultanDetail=new ArrayList();
		lsSimultanDetail=elionsManager.selectSimultanDetail(mclIdChoose);
		for(int i=0;i<lsSimultanDetail.size();i++){
			Map temp=(HashMap)lsSimultanDetail.get(i);
			Double sarPolis=setSarPolis(temp);
			temp.put("SAR_POLIS",sarPolis);
			lsSimultanDetail.set(i,temp);
			
		}
		if(lsSimultanDetail.isEmpty()==false){
			Map b;
			if(lsSimultanDetail.size()>flag)
				b=(HashMap)lsSimultanDetail.get(flag);
			else
				b=(HashMap)lsSimultanDetail.get(0);
			spaj=b.get("REG_SPAJ").toString();
			
			List lsStatus=elionsManager.selectMstPositionSpaj(spaj);
			List lsPremi=elionsManager.selectProductInsured2(spaj,new BigDecimal(1),new Integer(1));
			map.put("lsSimultan",lsSimultanDetail);
			map.put("lsStatus",lsStatus);
			map.put("lsPremi",lsPremi);
			map.put("flag_tt",""+flag);
			map.put("spajAwal",spajAwal);
			map.put("spajTemp",spaj);
			map.put("mclId",mclIdChoose);
			map.put("info",new Integer(info));//1=tertanggung 2=pemegang;
		}	
		return new ModelAndView("uw/detailsimultan", map);
	}
	private Double setSarPolis(Map temp) throws Exception{
		Double sar = null;
		String lsKurs;
		Double ldecTsi=null,liThKe=null,ldec_rate_sar=null;
		Date ldtBegDate=null;
		Integer liBisnisId=null,liBisnisNo = null,liLBayar=null,liLTanggung=null,liCBayar=null,liAge=null;
		lsKurs=(String) temp.get("LKU_ID");
		ldecTsi=(Double)temp.get("MSPR_TSI");
		ldtBegDate=(Date)temp.get("MSTE_BEG_DATE");
		liBisnisId=(Integer)temp.get("LSBS_ID");
		liBisnisNo=(Integer)temp.get("LSDBS_NUMBER");
		liLBayar=(Integer)temp.get("MSPO_PAY_PERIOD");
		liLTanggung=(Integer)temp.get("MSPO_INS_PERIOD");
		liCBayar=(Integer)temp.get("LSCB_ID");
		liAge=(Integer)temp.get("MSTE_AGE");
		Date today=elionsManager.selectSysdate(0);
		liThKe=elionsManager.selectTahunKe(defaultDateFormatStripes.format(ldtBegDate),defaultDateFormatStripes.format(today));
		ldec_rate_sar=elionsManager.f_get_sar(2,liBisnisId.intValue(),liBisnisNo.intValue(),lsKurs,liCBayar.intValue(),liLBayar.intValue(),liLTanggung.intValue(),liThKe.intValue(),liAge.intValue());

		if (ldec_rate_sar==null) 
			ldec_rate_sar = new Double(0);
		sar= new Double(ldecTsi.doubleValue() * ldec_rate_sar.doubleValue() / 1000);
		
		return sar;
	}
		
}