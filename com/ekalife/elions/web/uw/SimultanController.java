package com.ekalife.elions.web.uw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentController;

public class SimultanController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		String spaj=request.getParameter("spaj");
		List list=elionsManager.selectMReasTemp(spaj);
		Map reasTemp=(HashMap)list.get(0);
		Double reas[]=new Double[10];
		double total = 0;
		reas[0]=(Double) reasTemp.get("REAS_TR_RD");
		reas[1]=(Double) reasTemp.get("REAS_LIFE");
		reas[2]=(Double) reasTemp.get("REAS_SSP");
		reas[3]=(Double) reasTemp.get("REAS_PA_IN");
		reas[4]=(Double) reasTemp.get("REAS_PA_RD");
		reas[5]=(Double) reasTemp.get("REAS_PK_IN");
		reas[6]=(Double) reasTemp.get("REAS_PK_RD");
		reas[7]=(Double) reasTemp.get("REAS_SSH");
		reas[8]=(Double) reasTemp.get("REAS_CASH");
		reas[9]=(Double) reasTemp.get("REAS_TPD");
		List lsRiderExcNew=elionsManager.selectMReasTemp(spaj);
		//List lsRiderExcNew=elionsManager.selectMReasTempNew(spaj);
		reasTemp.put("lsRiderExcNew", lsRiderExcNew);
		for(int i=0;i<reas.length;i++){
			if(reas[i]==null)
				reas[i]=new Double(0);
			total=total+reas[i].doubleValue();
		}
		for(int i=0;i<lsRiderExcNew.size();i++){
//			ReasTemp riderTemp=(ReasTemp)lsRiderExcNew.get(i);
			//ReasTempNew riderTemp=(ReasTempNew)lsRiderExcNew.get(i);
//			total+=riderTemp.getmReas();
		}
		map.put("sarTemp",elionsManager.selectMSarTemp(spaj));
		map.put("reasTemp",reasTemp);
//		map.put("total",new Double(total));
		return new ModelAndView("uw/simultanpolis", map);
	}

}