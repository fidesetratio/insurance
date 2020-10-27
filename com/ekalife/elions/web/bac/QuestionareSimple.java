package com.ekalife.elions.web.bac;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Cmdeditbac;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.Employee;
import com.ekalife.elions.model.History;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.MedQuest_tambah;
import com.ekalife.elions.model.MedQuest_tambah2;
import com.ekalife.elions.model.MedQuest_tambah3;
import com.ekalife.elions.model.MedQuest_tambah4;
import com.ekalife.elions.model.MedQuest_tambah5;
import com.ekalife.elions.model.MedQuest_tertanggung;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.bac.support.Editbacvalidatorcfl;
import com.ekalife.utils.Common;
import com.ekalife.utils.parent.ParentFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QuestionareSimple extends ParentFormController{
	protected final Log logger = LogFactory.getLog( getClass() );

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor); 
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		Cmdeditbac detiledit = new Cmdeditbac();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj");
		 List<MedQuest> mq = uwManager.selectMedQuest(spaj,1);
		
		detiledit.setCurrentUser(currentUser);
		detiledit.setPemegang(new Pemegang());
		detiledit.setDatausulan(new Datausulan());
		detiledit.setTertanggung(new Tertanggung());
		detiledit.setMedQuest(new MedQuest());
		
		if (mq!=null){
			if(mq.size() <= 0){
				detiledit.setMedQuest(new MedQuest());
			}else{
				detiledit.setMedQuest(mq.get(0));
			}
		}else{
			detiledit.setMedQuest(new MedQuest());
		}
		
		detiledit.getMedQuest().setMsadm_clear_case(1);
		detiledit.getMedQuest().setReg_spaj(spaj);
		
		return detiledit;
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Cmdeditbac edit = (Cmdeditbac) cmd;
		HttpSession session = request.getSession();
		
			validate(cmd, err);
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Cmdeditbac editBac = (Cmdeditbac) cmd;
		Map map = new HashMap();
		String spaj= editBac.getPemegang().getReg_spaj();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
			logger.info(editBac.getMedQuest().getReg_spaj());
			
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),null,null,null,null,null,null,1));
	
		
		return new ModelAndView("bac/questionareSimple","cmd", editBac).addAllObjects(map);
	}
	
	public void validate(Object cmd, Errors err){
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Integer mspe_sex = 0;
		if(edit.getMedQuest().getMsadm_pregnant()==null) mspe_sex = 1;
		
		if(edit.getMedQuest().getJenis_quest().equalsIgnoreCase("NORMAL")){
			if(edit.getMedQuest().getMsadm_berat()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan mengenai berat");
			}
			
			if(edit.getMedQuest().getMsadm_tinggi()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan mengenai tinggi");
			}
			
			if(edit.getMedQuest().getMsadm_berat_berubah()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 1b");
			}
			
			if(edit.getMedQuest().getMsadm_berubah_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 1b");
			}
			
			if(edit.getMedQuest().getMsadm_sehat()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 2.");
			}
			
			if(edit.getMedQuest().getMsadm_sehat_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 2");
			}
			
			if(edit.getMedQuest().getMsadm_penyakit()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 3");
			}
			
			if(edit.getMedQuest().getMsadm_penyakit_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 3");
			}
			
			if(edit.getMedQuest().getMsadm_medis()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 4");
			}
			
			if(edit.getMedQuest().getMsadm_medis_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 4");
			}
		
			if(edit.getMedQuest().getMsadm_pregnant()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 5");
			}
			
			if(edit.getMedQuest().getMsadm_pregnant_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 5");
			}
		}else{ //helpdesk [148055] produk DMTM Dana Sejaterah 163 26-30 & Smile Sarjana 173 7-9, untuk UP > 200jt SIO ada tambah questionare
			if(edit.getMedQuest().getMsadm_berat()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 1a");
			}			
			if(edit.getMedQuest().getMsadm_tinggi()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 1b");
			}			
			if(edit.getMedQuest().getMsadm_penyakit()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 2");
			}			
			if(edit.getMedQuest().getMsadm_penyakit_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 2");
			}
			if(edit.getMedQuest().getMsadm_medis()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 3");
			}			
			if(edit.getMedQuest().getMsadm_medis_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 3");
			}		
			if(edit.getMedQuest().getMsadm_pregnant()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 4");
			}			
			if(edit.getMedQuest().getMsadm_pregnant_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 5");
			}
		}
		
	}
	
}
