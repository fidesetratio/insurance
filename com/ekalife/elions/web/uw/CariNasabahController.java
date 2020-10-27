package com.ekalife.elions.web.uw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

/**
 * @author Yusuf
 *
 */
public class CariNasabahController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		List list = new ArrayList();
		list.add("Nomor SPAJ"); //0
		list.add("Nomor Polis"); //1
		list.add("Nama Pemegang"); //2
		list.add("Nama Tertanggung"); //3
		list.add("Nomor Blanko"); //4
		list.add("Kode Plan"); //5
		map.put("listTipe", list);
		map.put("sysdate", this.elionsManager.selectSysdate(0));
		if(!ServletRequestUtils.getStringParameter(request, "kata","").trim().equals("")){
			List result = null;
			if("2,3".indexOf(request.getParameter("tipe").toString())>-1) {
				String tglLahir = null;
				if(request.getParameter("cekTglLahir")!=null) tglLahir = request.getParameter("tglLahir");
				result = this.elionsManager.selectFilterSpaj(request.getParameter("tipe").toString(), request.getParameter("kata"), tglLahir, ServletRequestUtils.getStringParameter(request, "pilter", "="));
			}else {
				result = this.elionsManager.selectFilterSpaj(request.getParameter("tipe").toString(), request.getParameter("kata"), null, ServletRequestUtils.getStringParameter(request, "pilter", "="));
			}
			List dataNasabah = new ArrayList();
			if(result.size()!=0)
				dataNasabah = this.uwManager.selectDataNasabah(result);
			StringBuffer aksesList = new StringBuffer();
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			for(int i=0; i<currentUser.getAksesCabang().size(); i++) {
				aksesList.append(currentUser.getAksesCabang().get(i)+", ");
			}
			if("17, 07, 19, 18, 09, 05".indexOf(currentUser.getLde_id())>-1) {
				for(int i=0; i<dataNasabah.size(); i++) {
					Map m = (HashMap) dataNasabah.get(i);
					if(m.get("LCA_ID")!=null)
						if(aksesList.toString().indexOf((String) m.get("LCA_ID"))>-1)
							m.put("AKSES_FLAG", "Y");
				}			
			}
			if(!result.isEmpty()) map.put("listNasabah", dataNasabah);
		}
		
		return new ModelAndView("uw/carinasabah", "cmd", map);
	}

}