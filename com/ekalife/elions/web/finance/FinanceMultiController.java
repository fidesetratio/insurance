package com.ekalife.elions.web.finance;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;
 
/**
 * @author Yusuf
 * @since Mar 3, 2006
 */
public class FinanceMultiController extends ParentMultiController{

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("finance/komisi_main");
	}
	
	public ModelAndView jurnal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		
		if(request.getParameter("jurnal")!=null){
			Date startDate = defaultDateFormat.parse(request.getParameter("startDate"));
			Date endDate = defaultDateFormat.parse(request.getParameter("endDate"));
			int jenis = ServletRequestUtils.getIntParameter(request, "jenis", 0);
			cmd.put("startDate", startDate);
			cmd.put("endDate", endDate);
			cmd.put("jenis", new Integer(jenis));
		}else{
			Date sysDate = this.elionsManager.selectSysdateSimple();
			cmd.put("startDate", sysDate);
			cmd.put("endDate", sysDate);
		}
		
		return new ModelAndView("finance/komisi_jurnal", cmd);
	}
	
	public ModelAndView komisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		String spaj = request.getParameter("spaj");
		if(spaj!=null) {
			List daftarKomisi = this.elionsManager.selectDaftarKomisiAgen(spaj);
			int jml = daftarKomisi.size(); 
			if(jml==0) return new ModelAndView("komisi.komisi").addObject("kosong", "true");
			int posisi = ServletRequestUtils.getIntParameter(request, "posisi", 0);
			
			int th = ServletRequestUtils.getIntParameter(request, "tahun", 0);
			int pr = ServletRequestUtils.getIntParameter(request, "premi", 0);

			if(this.uwManager.selectBusinessId(spaj).equals("89") && th==1 && pr==1) {
				cmd.put("flagPalembang", "true"); //flag insert potongan untuk ultra sejahtera palembang
			}
			if(request.getParameter("transfer")!=null) {
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				Map errorMessage = this.elionsManager.transferKomisiToFilling(spaj, th, pr, currentUser.getLus_id(), request.getParameter("palembang"));
				if(errorMessage.get("error")==null) posisi=0;
				cmd.put("errorMessage", errorMessage);
			}
			
			if(request.getParameter("prev")!=null) posisi--;
			else if(request.getParameter("next")!=null) posisi++;
			
			if(posisi==0) cmd.put("prev", "disabled");
			if(posisi==(jml-1)) cmd.put("next", "disabled");
			
			int tahun = ((Integer) ((Map) daftarKomisi.get(posisi)).get("TAHUN"));
			int premi = ((Integer) ((Map) daftarKomisi.get(posisi)).get("PREMI"));
			List komisi = this.elionsManager.selectKomisiAgen(spaj, tahun, premi);
			cmd.put("posisi", new Integer(posisi));
			cmd.put("tahun", new Integer(tahun));
			cmd.put("premi", new Integer(premi));
			cmd.put("komisiAgen", komisi);
		}
		return new ModelAndView("finance/komisi_komisi", "cmd", cmd);
	}

	public ModelAndView deduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		String msco_id = request.getParameter("msco_id");
		if(msco_id!=null) {
			if(request.getParameter("save")!=null) {
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				this.elionsManager.saveDeduct(request, currentUser);
			}
			cmd.put("jenisDeduct", this.elionsManager.selectJenisDeduct());
			List deductList = this.elionsManager.selectDeductKomisiAgen(msco_id);
			if(request.getParameter("add")!=null) {
				Map map = new HashMap();
				map.put("FLAG", "I");
				map.put("MSDD_DATE", new Date());
				map.put("MSDD_DEDUCT", new Double(0));
				deductList.add(map);
			}
			cmd.put("deductList", deductList);
		}
		if(request.getParameter("disabled")!=null) cmd.put("disabled", "yes");
		return new ModelAndView("finance/komisi_deduct", "cmd", cmd);
	}
	
	public ModelAndView listBayarKomisi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		Date startDate = defaultDateFormat.parse(request.getParameter("startDate"));
		Date endDate = defaultDateFormat.parse(request.getParameter("endDate"));
		
		return new ModelAndView("finance/komisi_deduct", "cmd", cmd);
	}

}