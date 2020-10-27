package com.ekalife.elions.web.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Aktivitas;
import com.ekalife.elions.model.Bfa;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Nasabah;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;

public class InputActivityFormController extends ParentFormController {
	NumberFormat f2=new DecimalFormat("00");
	NumberFormat f5=new DecimalFormat("00000");
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
		//binder.registerCustomEditor(BigDecimal.class, null, decimalEditor);
		binder.registerCustomEditor(BigDecimal.class, null, new CustomNumberEditor( BigDecimal.class, new DecimalFormat("###,##0.00") , true ));
	}
	
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Command command=(Command)cmd;
		List lsReferrer,lsReferrerBii;
		Nasabah nasabah=command.getNasabah();
		if(nasabah.getLjl_jenis()==null)
			nasabah.setLjl_jenis(0);
		
		if(nasabah.getMns_ok_saran()==null){
			nasabah.setMns_ok_saran(0);
		}
		
		List lsLead, lsAktivitas;
//		if(nasabah.getPlatinum()==1)
//			lsLead=elionsManager.selectLstJnLead(null);
//		else if(nasabah.getPlatinum()==3)
//			lsLead=elionsManager.selectLstJnLead("4,5");
//		else
			lsLead=elionsManager.selectLstJnLead(null);
		Map map=new HashMap();
		map.put("lsCabBii",elionsManager.selectLstCabBii("02"));
		map.put("lsLead", lsLead);
		if(nasabah.getLjl_jenis()>=5){
			Integer levelId=elionsManager.selectLstJnLeadLevelId(nasabah.getLjl_jenis());
			if(levelId!=null && levelId !=0)
				lsReferrerBii=elionsManager.selectLstReferrerBii(levelId);
			else
				lsReferrerBii=elionsManager.selectLstReferrerBii(null);
		}else
			lsReferrerBii=elionsManager.selectLstReferrerBii(null);
		lsReferrer=elionsManager.selectMstBfaLevel(4);
//		nasabah.setCabang_detail(elionsManager.selectCabangBii(nasabah.getKode()));
//		nasabah.setBfa_detail(elionsManager.selectNamaBfa(nasabah.getMsag_id(),nasabah.getKode()));
		List lsBfa=new ArrayList();
		//if(command.getNasabah().getPlatinum()<6){
			lsBfa=elionsManager.selectMstBfaKode(nasabah.getKode());
		//}
		
		lsAktivitas = elionsManager.selectListAktivitas();
		map.put("lsBfa", lsBfa);
		map.put("lsReferrer", lsReferrer);
		map.put("lsReferrerBii", lsReferrerBii);
		map.put("lsAktivitas", lsAktivitas);

		return map;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command command=new Command();
		command.setShowTab(1);
		String flag=ServletRequestUtils.getStringParameter(request, "flag","");
		Integer p=ServletRequestUtils.getIntParameter(request, "p",0);
		Nasabah nasabah=new Nasabah();
		Aktivitas aktivitas = new Aktivitas();
		nasabah.setPlatinum(p);
		String nomor=ServletRequestUtils.getStringParameter(request,"nomor","");
		
			String kdNasabah=null,noReferral=null;
			if(! nomor.equals("")){
				int pos=nomor.indexOf('~');
				noReferral=nomor.substring(0,pos);
				kdNasabah=nomor.substring(pos+1,nomor.length());
			}
			
			nasabah=elionsManager.selectMstNasabah(kdNasabah);
			if(nasabah == null){
				nasabah = new Nasabah();
				if(nasabah.getMns_ok_saran()==null){
					nasabah.setMns_ok_saran(0);
				}
				command.setNasabah(nasabah);
				return command;
			}
			nasabah.setPlatinum(0);
			nasabah.setCabang_detail(elionsManager.selectCabangBii(nasabah.getKode()));
			nasabah.setBfa_detail(elionsManager.selectNamaBfa(nasabah.getMsag_id(),nasabah.getKode()));
			nasabah.setFlag(1); //untuk menampilkan data Informasi Pemberi lead 
			List list=elionsManager.selectMstBfa(nasabah.getMsag_id(),nasabah.getKode());
			if(list.isEmpty()==false){
				Bfa bfa1=(Bfa)list.get(0);
				nasabah.setNama_bfa(bfa1.getNama_bfa());
			}	
			String kdRef=props.getProperty("kode.referral");
			String ljlJenis=f2.format(nasabah.getLjl_jenis());
			if( nasabah.getLjl_jenis().compareTo(2)==0){
				nasabah.setNamaLead("Nama Agen");

			}else if(kdRef.indexOf(ljlJenis)>=0){
				nasabah.setNamaLead("Nama Referrer");
			}else{ 
				nasabah.setNamaLead("Nama");
			}
			if(nasabah.getMns_ok_saran()==null){
				nasabah.setMns_ok_saran(0);
			}
			
			aktivitas.setListAktivitas(elionsManager.selectMstAktivitas(kdNasabah));
			command.setAktivitas(aktivitas);
			command.setNasabah(nasabah);
		
		
		return command;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Command command=(Command)cmd;
		Aktivitas aktivitas = command.getAktivitas();
		if(request.getParameter("save")!= null){
			elionsManager.prosesInputActivity(aktivitas);
		}
		
		return new ModelAndView("common/input_activity",err.getModel()).addObject("submitSuccess","true").addAllObjects(this.referenceData(request,command,err));
	}
	
}
	