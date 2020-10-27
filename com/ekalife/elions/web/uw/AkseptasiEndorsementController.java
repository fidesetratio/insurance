package com.ekalife.elions.web.uw;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Endors;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.parent.ParentMultiController;

/**
 * @author  : Andhika
 * @created : Mar 29, 2013, 8:37:46 AM
 * 
 */
public class AkseptasiEndorsementController extends ParentMultiController {
		
	public ModelAndView aksependors (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		String keter = ServletRequestUtils.getStringParameter(request, "ket_aksep_uw","").toUpperCase();
		Integer pilihan_aksep_uw = ServletRequestUtils.getIntParameter(request, "pilaksepuw");
		
		Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
		Endors endors = uwManager.selectEndorsNew(spaj);
		Pemegang pemegang =elionsManager.selectpp(spaj);
		Tertanggung tertanggung =elionsManager.selectttg(spaj);
		Integer aksep_uw = uwManager.selectMsen_aksep_uw(spaj);
		String keterangan = uwManager.selectKete(spaj);
		Date tgl_skrg = elionsManager.selectSysdateSimple();
//		map.put("infoEndorsNew", uwManager.selectMsen_aksep_uw_new(spaj));
		List<String> error = new ArrayList<String>();

			if(request.getParameter("save")!= null) {
     				uwManager.prosesAksepEndors(pilihan_aksep_uw, spaj, currentUser, keter);
     				dataUsulan.setMsen_aksep_uw(aksep_uw);
     				dataUsulan.setKeterangan(keter);
     				
     				if(aksep_uw != 1){
     					error.add(" Akseptasi Endorsment tidak dapat dilakukan karena sudah di lakukan akseptasi ");
     				}else{
     					error.add(" Akseptasi Endorsment berhasil dilakukan ");
     				}
     		}
				
			if(!error.equals("")){
				map.put("pesanError", error);
			}
		map.put("dataUsulan", dataUsulan);
		map.put("pemegang", pemegang);
		map.put("tertanggung",tertanggung);
		map.put("select_aksep_endors",DroplistManager.getInstance().get("aksep_endors.xml","",request));
//		map.put("select_hasil",DroplistManager.getInstance().get("SUMBER_PENGHASILAN.xml","",request));
		map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
		map.put("infoEndorsNew", uwManager.selectMsen_aksep_uw_new(spaj));
		return new ModelAndView("uw/aksep_endors", map);
	}
	
	public ModelAndView akum_new(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();

		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		if(currentUser.getLca_id().equals("01")) {
//		}else {
//			map.put("pesanError", "Anda tidak mempunyai akses terhadap menu ini");
//		}
		return new ModelAndView("uw/viewer/viewkontrolspaj_akum_new", map);
	}
}