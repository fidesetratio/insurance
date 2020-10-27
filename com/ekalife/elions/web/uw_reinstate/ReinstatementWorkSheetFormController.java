/**ReinstatementWorkSheetFormController.java
 * @author  : Ferry Harlim
 * @created : Feb 5, 2008 9:50:56 AM
 */
package com.ekalife.elions.web.uw_reinstate;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.reinstate.CommandReins;
import com.ekalife.elions.model.reinstate.Reinstatement;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentFormController;

public class ReinstatementWorkSheetFormController extends ParentFormController {
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CommandReins cmdReins=new CommandReins();
		cmdReins.setReg_spaj(ServletRequestUtils.getStringParameter(request, "reg_spaj"));
		//buat narik data antara non link dan link
		String businessId = FormatString.rpad("0", uwManager.selectBusinessId(cmdReins.getReg_spaj()), 3);
		if (products.unitLink(businessId)){
			cmdReins.setLsReinstatement(bacManager.selectReinstatementWorkSheet2(cmdReins.getReg_spaj()));//link
		}else {
			cmdReins.setLsReinstatement(elionsManager.selectReinstatementWorkSheet(cmdReins.getReg_spaj())); //non link
		}
		String tglBayar=FormatDate.toString(elionsManager.selectSysdate());
		cmdReins.setTglBayar(tglBayar);
		
		if(cmdReins.getLsReinstatement().isEmpty()==false){
			Reinstatement reins=(Reinstatement)cmdReins.getLsReinstatement().get(0);
			Integer lama=reins.getLama_lapse();
			Map map=FormatDate.getDay(lama);
			cmdReins.setDay((Integer)map.get("day"));
			cmdReins.setMonth((Integer)map.get("month"));
			cmdReins.setYear((Integer)map.get("year"));
		}
		return cmdReins;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		CommandReins cmdReins=(CommandReins)cmd;
		//proses insert ke position_spaj trus update, tgl kirim ,tgl terima spaj 
		Reinstatement reins=(Reinstatement)cmdReins.getLsReinstatement().get(0);
		//cekin format tanggal dahulu ada error gak.
		reins.setMste_tgl_kirim_LB(ServletRequestUtils.getStringParameter(request, "mste_tgl_kirim_LB",""));
		reins.setMste_tgl_terima_LB(ServletRequestUtils.getStringParameter(request, "mste_tgl_terima_LB",""));
		if(reins.getMste_tgl_kirim_LB()=="")
			err.reject("","Tanggal Kirim Life Benefit Masih Kosong.");
		if(reins.getMste_tgl_terima_LB()=="")
			err.reject("","Tanggal Terima Life Benefit Masih Kosong.");
		if(reins.getPut_uw_new()==null || reins.getPut_uw_new().equals(""))
			err.reject("","Silahkan Isi Komentar/Usulan/Keputusan UW.");
		if(ServletRequestUtils.getIntParameter(request, "flag")==1){
			String tanggal=ServletRequestUtils.getStringParameter(request, "tglBayar");
			Date tglByr=FormatDate.toDateWithSlash(tanggal);
			long lamaLapse=FormatDate.dateDifference(reins.getMsbi_aktif_date(),tglByr,true);
			Map map=FormatDate.getDay((int)lamaLapse);
			cmdReins.setDay((Integer)map.get("day"));
			cmdReins.setMonth((Integer)map.get("month"));
			cmdReins.setYear((Integer)map.get("year"));
			reins.setLama_lapse((int)lamaLapse+1);
			cmdReins.getLsReinstatement().set(0, reins);
			
			err.reject("","Halaman Telah Refresh ");
		}	
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		CommandReins cmdReins =(CommandReins) cmd;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		
		Reinstatement reins=(Reinstatement)cmdReins.getLsReinstatement().get(0);
		elionsManager.prosesReinstatementWorkSheet(cmdReins,currentUser);
		return new ModelAndView("uw_reinstate/reinstatement_work_sheet", err.getModel()).addObject(
				"submitSuccess", "true").addAllObjects(
				this.referenceData(request, cmd, err));
	}
}
