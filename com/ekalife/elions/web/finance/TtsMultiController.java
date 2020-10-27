package com.ekalife.elions.web.finance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.tts.Tts;
import com.ekalife.utils.parent.ParentMultiController;

public class TtsMultiController extends ParentMultiController {

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		map.put("lsTts",elionsManager.selectAllMstTts("","1",null,currentUser.getLca_id()));
		return new ModelAndView("finance/tts_main",map);
	}
	
	public ModelAndView cari_nomor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String value=request.getParameter("value");
		String tipe=request.getParameter("tipe");
		String filter=request.getParameter("filter");
		
		if(tipe==null)
			tipe="1";
		if(filter==null)
			filter="LIKE%";
		
		List lsTts=elionsManager.selectAllMstTts(value,tipe,filter,currentUser.getLca_id());
		map.put("lsTts",lsTts);
		map.put("filter",filter);
		map.put("tipe", tipe);
		map.put("value", value);
		return new ModelAndView("finance/cari_nomor",map);
	}
	public ModelAndView show_tts(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String nomor=request.getParameter("nomor");
		List lsTts=elionsManager.selectAllMstTts(nomor,"1",null,currentUser.getLca_id());
		if(lsTts.isEmpty())
			return new ModelAndView("finance/show_tts",map);
		List lsHistoryPrint=elionsManager.selectLstHistoryPrintTts(nomor);
		List lsHistoryTts=elionsManager.selectMstHistoryTts(nomor);
		if(lsTts.isEmpty()==false){
			Tts tts=(Tts)lsTts.get(0);
			map.put("tts",tts);
		}
		map.put("lsPolicyTts",elionsManager.selectMstPolicyTts(nomor,null));
		map.put("lsCaraByr",elionsManager.selectMstCaraByr(nomor));
		map.put("lsHistoryPrint",lsHistoryPrint);
		map.put("lsHistoryTts",lsHistoryTts);
		
		Integer size=null,size2=null;
		if(lsHistoryPrint.isEmpty()==false)
			size=new Integer(1);
		
		if(lsHistoryTts.isEmpty()==false)
			size2=new Integer(1);
		
		map.put("size",size);
		map.put("size2", size2);
		return new ModelAndView("finance/show_tts",map);
	}

}