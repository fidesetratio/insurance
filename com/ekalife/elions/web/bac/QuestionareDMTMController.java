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

public class QuestionareDMTMController extends ParentFormController{
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
		
		detiledit.setCurrentUser(currentUser);
		detiledit.setPemegang(new Pemegang());
		detiledit.setDatausulan(new Datausulan());
		detiledit.setTertanggung(new Tertanggung());
		detiledit.setMedQuest(new MedQuest());
		detiledit.setMedQuest_tertanggung(new MedQuest_tertanggung());
		detiledit.setMedQuest_tambah(new MedQuest_tambah());
		detiledit.setMedQuest_tambah2(new MedQuest_tambah2());
		detiledit.setMedQuest_tambah3(new MedQuest_tambah3());
		detiledit.setMedQuest_tambah4(new MedQuest_tambah4());
		detiledit.setMedQuest_tambah5(new MedQuest_tambah5());
		
		List list = uwManager.selectPolicyRelationDMTM(spaj);
		Map m = (HashMap) list.get(0);
		int jengkel_pp =0;
		  int jengkel_tt =0;
		  int jengkel_t1 =0;
		  int jengkel_t2 =0;
		  int jengkel_t3 =0;
		  int jengkel_t4 =0;
		  int jengkel_t5 =0;
		if( m.get("JENKEL_PP")==null){
			jengkel_pp = 0;
		}else{
			jengkel_pp=((BigDecimal) m.get("JENKEL_PP")).intValue();
		}
		
		if( m.get("JENKEL_TERTANGGUNG")==null){
			jengkel_tt = 0;
		}else{
			jengkel_tt=((BigDecimal) m.get("JENKEL_TERTANGGUNG")).intValue();
		}
		
		if( m.get("JENKEL_TT1")==null){
			jengkel_t1 = 0;
		}else{
			jengkel_t1 =((BigDecimal) m.get("JENKEL_TT1")).intValue();
		}
		
		if( m.get("JENKEL_TT2")==null){
			jengkel_t2 = 0;
		}else{
			jengkel_t2 =((BigDecimal) m.get("JENKEL_TT2")).intValue();
		}
		
		if( m.get("JENKEL_TT3")==null){
			jengkel_t3 = 0;
		}else{
			jengkel_t3=((BigDecimal) m.get("JENKEL_TT3")).intValue();
		}
		
		if( m.get("JENKEL_TT4")==null){
			jengkel_t4 = 0;
		}else{
			jengkel_t4=((BigDecimal) m.get("JENKEL_TT4")).intValue();
		}
		
		if( m.get("JENKEL_TT5")==null){
			jengkel_t5 = 0;
		}else{
			jengkel_t5=((BigDecimal) m.get("JENKEL_TT5")).intValue();
		}
		
		int lsre_id=((BigDecimal) m.get("LSRE_ID")).intValue();
		Integer jml_peserta=0;
		Integer umurPP=((BigDecimal) m.get("UMUR_PP")).intValue();
		
		if(m.get("PESERTA")==null){
			detiledit.getPemegang().setLsre_id(lsre_id);
			detiledit.getDatausulan().setJml_peserta(jml_peserta);
			
			if(m.get("UMUR_PP")!=null){
				int age_pp = ((BigDecimal) m.get("UMUR_PP")).intValue();
				detiledit.getPemegang().setMste_age(age_pp);
			}
			if(m.get("JENKEL_PP")!=null){
				int jenkel_pp = ((BigDecimal) m.get("JENKEL_PP")).intValue();
				detiledit.getPemegang().setMspe_sex(jenkel_pp);
			}
			
			if(m.get("UMUR_TERTANGGUNG")!=null){
				int age_tertanggung = ((BigDecimal) m.get("UMUR_TERTANGGUNG")).intValue();
				detiledit.getTertanggung().setMste_age(age_tertanggung);
			}
			if(m.get("JENKEL_TERTANGGUNG")!=null){
				int jenkel_tertanggung = ((BigDecimal) m.get("JENKEL_TERTANGGUNG")).intValue();
				detiledit.getTertanggung().setMspe_sex(jenkel_tertanggung);
			}
		}else{
			if(m.get("UMUR_TT1")==null){
			}else{
				int age_tambah = ((BigDecimal) m.get("UMUR_TT1")).intValue();
				session.setAttribute("umur_tt1", age_tambah);
			}
			
			if(m.get("UMUR_TT2")==null){
			}else{
				int age_tambah2 = ((BigDecimal) m.get("UMUR_TT2")).intValue();
				session.setAttribute("umur_tt2", age_tambah2);
			}
			
			if(m.get("UMUR_TT3")==null){
			}else{
				int age_tambah3 = ((BigDecimal) m.get("UMUR_TT3")).intValue();
				session.setAttribute("umur_tt3", age_tambah3);
			}
			
			if(m.get("UMUR_PP")!=null){
				int age_pp = ((BigDecimal) m.get("UMUR_PP")).intValue();
				detiledit.getPemegang().setMste_age(age_pp);
			}
			if(m.get("JENKEL_PP")!=null){
				int jenkel_pp = ((BigDecimal) m.get("JENKEL_PP")).intValue();
				detiledit.getPemegang().setMspe_sex(jenkel_pp);
			}
			
			if(m.get("UMUR_TERTANGGUNG")!=null){
				int age_tertanggung = ((BigDecimal) m.get("UMUR_TERTANGGUNG")).intValue();
				detiledit.getTertanggung().setMste_age(age_tertanggung);
			}
			if(m.get("JENKEL_TERTANGGUNG")!=null){
				int jenkel_tertanggung = ((BigDecimal) m.get("JENKEL_TERTANGGUNG")).intValue();
				detiledit.getTertanggung().setMspe_sex(jenkel_tertanggung);
			}
			
			int peserta=((BigDecimal) m.get("PESERTA")).intValue();
			jml_peserta=peserta;
			detiledit.getPemegang().setLsre_id(lsre_id);
			detiledit.getDatausulan().setJml_peserta(jml_peserta);
		}
		
		detiledit.getMedQuest().setReg_spaj(spaj);
		detiledit.getMedQuest_tertanggung().setReg_spaj(spaj);
		detiledit.getMedQuest_tambah().setReg_spaj(spaj);
		detiledit.getMedQuest_tambah2().setReg_spaj(spaj);
		detiledit.getMedQuest_tambah3().setReg_spaj(spaj);
		detiledit.getPemegang().setMste_age(umurPP);
		detiledit.getMedQuest().setSex(jengkel_pp);
		detiledit.getMedQuest_tertanggung().setSex(jengkel_tt);
		detiledit.getMedQuest_tambah().setSex(jengkel_t1);
		detiledit.getMedQuest_tambah2().setSex(jengkel_t2);
		detiledit.getMedQuest_tambah3().setSex(jengkel_t3);
		detiledit.getMedQuest_tambah4().setSex(jengkel_t4);
		detiledit.getMedQuest_tambah5().setSex(jengkel_t5);
		
		logger.info(detiledit.getMedQuest().getReg_spaj());
		
		return detiledit;
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Cmdeditbac edit = (Cmdeditbac) cmd;
		HttpSession session = request.getSession();
		List list = uwManager.selectPolicyRelationDMTM(edit.getMedQuest().getReg_spaj());
		Map m = (HashMap) list.get(0);
		
		int relasi = edit.getPemegang().getLsre_id();
		int jml = edit.getDatausulan().getJml_peserta();
		
		if(relasi==1 && jml==0){//PP==Tertanggung,tambahan=0
			validate(cmd, err);
		}else if(relasi==1 && jml==1){//PP==Tertanggung,tambahan=1
			Integer age = ((BigDecimal) m.get("UMUR_TT1")).intValue();
			validate(cmd, err);
			validate_tambah(cmd, err, age);
		}else if(relasi==1 && jml==2){//PP==Tertanggung,tambahan=2
			Integer age = (Integer) session.getAttribute("umur_tt1");
			Integer age2 = (Integer) session.getAttribute("umur_tt2");
			validate(cmd, err);
			validate_tambah(cmd, err, age);
			validate_tambah2(cmd, err, age2);
		}else if(relasi==1 && jml==3){//PP==Tertanggung,tambahan=3
			Integer age = (Integer) session.getAttribute("umur_tt1");
			Integer age2 = (Integer) session.getAttribute("umur_tt2");
			Integer age3 = (Integer) session.getAttribute("umur_tt3");
			validate(cmd, err);
			validate_tambah(cmd, err, age);
			validate_tambah2(cmd, err, age2);
			validate_tambah3(cmd, err, age3);
		}else if(relasi==1 && jml==4){//PP==Tertanggung,tambahan=4
			Integer age = (Integer) session.getAttribute("umur_tt1");
			Integer age2 = (Integer) session.getAttribute("umur_tt2");
			Integer age3 = (Integer) session.getAttribute("umur_tt3");
			Integer age4 = (Integer) session.getAttribute("umur_tt4");
			validate(cmd, err);
			validate_tambah(cmd, err, age);
			validate_tambah2(cmd, err, age2);
			validate_tambah3(cmd, err, age3);
			validate_tambah4(cmd, err, age4);
		}else if(relasi==1 && jml==5){//PP==Tertanggung,tambahan=5
			Integer age = (Integer) session.getAttribute("umur_tt1");
			Integer age2 = (Integer) session.getAttribute("umur_tt2");
			Integer age3 = (Integer) session.getAttribute("umur_tt3");
			Integer age4 = (Integer) session.getAttribute("umur_tt4");
			Integer age5 = (Integer) session.getAttribute("umur_tt5");
			validate(cmd, err);
			validate_tambah(cmd, err, age);
			validate_tambah2(cmd, err, age2);
			validate_tambah3(cmd, err, age3);
			validate_tambah4(cmd, err, age4);
			validate_tambah5(cmd, err, age5);
		}else if(jml==0){//PP!=Tertanggung,tambahan=0
			validate(cmd, err);
			validate_tertanggung(cmd, err);
		}else if(jml==1){//PP!=Tertanggung,tambahan=1
			Integer age = (Integer) session.getAttribute("umur_tt1");
			validate(cmd, err);
			validate_tertanggung(cmd, err);
			validate_tambah(cmd, err, age);
		}else if(jml==2){//PP!=Tertanggung,tambahan=2
			Integer age = (Integer) session.getAttribute("umur_tt1");
			Integer age2 = (Integer) session.getAttribute("umur_tt2");
			validate(cmd, err);
			validate_tertanggung(cmd, err);
			validate_tambah(cmd, err, age);
			validate_tambah2(cmd, err, age2);
		}else if(jml==3){//PP!=Tertanggung,tambahan=3
			Integer age = (Integer) session.getAttribute("umur_tt1");
			Integer age2 = (Integer) session.getAttribute("umur_tt2");
			Integer age3 = (Integer) session.getAttribute("umur_tt3");
			validate(cmd, err);
			validate_tertanggung(cmd, err);
			validate_tambah(cmd, err, age);
			validate_tambah2(cmd, err, age2);
			validate_tambah3(cmd, err, age3);
		}else if(jml==4){//PP!=Tertanggung,tambahan=4
			Integer age = (Integer) session.getAttribute("umur_tt1");
			Integer age2 = (Integer) session.getAttribute("umur_tt2");
			Integer age3 = (Integer) session.getAttribute("umur_tt3");
			Integer age4 = (Integer) session.getAttribute("umur_tt4");
			validate(cmd, err);
			validate_tertanggung(cmd, err);
			validate_tambah(cmd, err, age);
			validate_tambah2(cmd, err, age2);
			validate_tambah3(cmd, err, age3);
			validate_tambah4(cmd, err, age4);
		}else if(jml==5){//PP!=Tertanggung,tambahan=5
			Integer age = (Integer) session.getAttribute("umur_tt1");
			Integer age2 = (Integer) session.getAttribute("umur_tt2");
			Integer age3 = (Integer) session.getAttribute("umur_tt3");
			Integer age4 = (Integer) session.getAttribute("umur_tt4");
			Integer age5 = (Integer) session.getAttribute("umur_tt5");
			validate(cmd, err);
			validate_tertanggung(cmd, err);
			validate_tambah(cmd, err, age);
			validate_tambah2(cmd, err, age2);
			validate_tambah3(cmd, err, age3);
			validate_tambah4(cmd, err, age4);
			validate_tambah5(cmd, err, age5);
		}else{
		}
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Cmdeditbac editBac = (Cmdeditbac) cmd;
		Map map = new HashMap();
		String spaj= editBac.getPemegang().getReg_spaj();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		int relasi = editBac.getPemegang().getLsre_id();
		int jml = editBac.getDatausulan().getJml_peserta();
		//get Validate
		if(relasi==1 && jml==0){//PP==Tertanggung,tambahan=0
			logger.info(editBac.getMedQuest().getReg_spaj());
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),null,null,null,null,null,null,relasi));
		}else if(relasi==1 && jml==1){//PP==Tertanggung,tambahan=1
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),null,editBac.getMedQuest_tambah(),null,null,null,null,relasi));
		}else if(relasi==1 && jml==2){//PP==Tertanggung,tambahan=2
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),null,editBac.getMedQuest_tambah(),editBac.getMedQuest_tambah2(),null,null,null,relasi));
		}else if(relasi==1 && jml==3){//PP==Tertanggung,tambahan=3
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),null,editBac.getMedQuest_tambah(),editBac.getMedQuest_tambah2(),editBac.getMedQuest_tambah3(),null,null,relasi));
		}else if(relasi==1 && jml==4){//PP==Tertanggung,tambahan=4
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),null,editBac.getMedQuest_tambah(),editBac.getMedQuest_tambah2(),editBac.getMedQuest_tambah3(),editBac.getMedQuest_tambah4(),null,relasi));
		}else if(relasi==1 && jml==5){//PP==Tertanggung,tambahan=5
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),null,editBac.getMedQuest_tambah(),editBac.getMedQuest_tambah2(),editBac.getMedQuest_tambah3(),editBac.getMedQuest_tambah4(),editBac.getMedQuest_tambah5(),relasi));
		}else if(jml==0){//PP!=Tertanggung,tambahan=0
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),editBac.getMedQuest_tertanggung(),null,null,null,null,null,relasi));
		}else if(jml==1){//PP!=Tertanggung,tambahan=1
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),editBac.getMedQuest_tertanggung(),editBac.getMedQuest_tambah(),null,null,null,null,relasi));
		}else if(jml==2){//PP!=Tertanggung,tambahan=2
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),editBac.getMedQuest_tertanggung(),editBac.getMedQuest_tambah(),editBac.getMedQuest_tambah2(),null,null,null,relasi));
		}else if(jml==3){//PP!=Tertanggung,tambahan=3
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),editBac.getMedQuest_tertanggung(),editBac.getMedQuest_tambah(),editBac.getMedQuest_tambah2(),editBac.getMedQuest_tambah3(),null,null,relasi));
		}else if(jml==4){//PP!=Tertanggung,tambahan=4
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),editBac.getMedQuest_tertanggung(),editBac.getMedQuest_tambah(),editBac.getMedQuest_tambah2(),editBac.getMedQuest_tambah3(),editBac.getMedQuest_tambah4(),null,relasi));
		}else if(jml==5){//PP!=Tertanggung,tambahan=5
			map.put("pesan",uwManager.insertMedQuestDMTM(editBac.getMedQuest(),editBac.getMedQuest_tertanggung(),editBac.getMedQuest_tambah(),editBac.getMedQuest_tambah2(),editBac.getMedQuest_tambah3(),editBac.getMedQuest_tambah4(),editBac.getMedQuest_tambah5(),relasi));
		}else{
		}
		
		//return new ModelAndView("bac/questionareDMTM","cmd", editBac).addAllObjects(this.referenceData(request,cmd,err));
		return new ModelAndView("bac/questionareDMTM","cmd", editBac).addAllObjects(map);
	}
	
	public void validate(Object cmd, Errors err){
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Integer mspe_sex = 0;
		if(edit.getMedQuest().getMsadm_pregnant()==null) mspe_sex = 1;
		
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
			edit.getMedQuest().setMsadm_sehat(0);
		}
		
		if(edit.getMedQuest().getMsadm_sehat_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
		if(edit.getMedQuest().getMsadm_penyakit()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 3");
			edit.getMedQuest().setMsadm_penyakit(0);
		}
		
		if(edit.getMedQuest().getMsadm_penyakit_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 3");
		}
		
		if(edit.getMedQuest().getMsadm_medis()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 4a");
			edit.getMedQuest().setMsadm_medis(0);
		}
		
		if(edit.getMedQuest().getMsadm_medis_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 4a");
		}
		
		if(edit.getMedQuest().getMsadm_medis_alt()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 4b");
			edit.getMedQuest().setMsadm_medis_alt(0);
		}
		
		if(edit.getMedQuest().getMsadm_medis_alt()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 4b");
		}
		
		if(edit.getMedQuest().getMsadm_family_sick()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 5");
		}
		
		if(edit.getMedQuest().getMsadm_family_sick_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 5");
		}
		if(mspe_sex==0){
			if(edit.getMedQuest().getMsadm_pregnant()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 6a");
			}
			
			if(edit.getMedQuest().getMsadm_pregnant_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 6a");
			}
			
			if(edit.getMedQuest().getMsadm_abortion()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 6b");
			}
			
			if(edit.getMedQuest().getMsadm_abortion_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 6b");
			}
			
			if(edit.getMedQuest().getMsadm_usg()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 6c");
			}
			
			if(edit.getMedQuest().getMsadm_usg_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 6c");
			}
		}
		
		if(edit.getMedQuest().getMsadd_hobby()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 1");
		}
		
		if(edit.getMedQuest().getMsadd_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 2");
		}
		
		if(edit.getMedQuest().getMsadd_desc_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
	/*	if(edit.getMedQuest().getMsadd_smoke()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 3a");
		}*/
		
		/*if(edit.getMedQuest().getNsadd_many_cig()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 3a");
		}*/
		
		if(edit.getMedQuest().getMsadd_drink_beer()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 3b");
		}
		
		if(edit.getMedQuest().getMsadd_drink_beer_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 3b");
		}
		
		if(edit.getMedQuest().getMsadd_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 3c");
		}
		
		if(edit.getMedQuest().getMsadd_reason_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 3c");
		}
		
		if(edit.getMedQuest().getMsadd_life_ins()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Pemegang Polis : Silakan diisi terlebih dahulu pernyataan point 4");
		}
		
		if(edit.getMedQuest().getMsadd_life_ins_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Pemegang Polis : Silakan diisi terlebih dahulu keterangan point 4");
		}
		
		if(edit.getMedQuest().getMsadm_tinggi()!=null && edit.getMedQuest().getMsadm_berat()!=null){
			BigDecimal height =  new BigDecimal(edit.getMedQuest().getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
			BigDecimal bmi = new BigDecimal(edit.getMedQuest().getMsadm_berat()).divide(height,0,RoundingMode.HALF_UP);
			BigDecimal bmi2 =bmi.divide(new BigDecimal(100));
			int umur =edit.getPemegang().getMste_age();
			edit.getMedQuest().setMsadm_bmi(bmi.doubleValue());
			Map Em = uwManager.selectEmBmi(bmi.doubleValue(),umur);
			edit.getMedQuest().setMsadm_em(Em);
			edit.getMedQuest().setMsadm_em_life( new Double(Em.get("LIFE").toString()) );
			
			//test
			edit.getMedQuest().setMsadm_penyakit_std(0);
			edit.getMedQuest().setMsadm_sehat_std(0);
			
			//pernah pengecekan medis maupun alternatif, hamil, abortion;
			//TT& BB ratio = <=50 % = kondisi standar, >50% = kondisi non standar
			//tidak sehat && sakit tidak standar
			//terkena penyakit && bukan penyakit standar
			//Jika wanita, pernah MCU, dan hasilnya tidak baik
			//Jika wanita, 
			
			/*Integer mspe_sex = 0;
			if(edit.getMedQuest().getMsadm_pregnant()==null) mspe_sex = 1;*/
			if(mspe_sex==0){
				if(edit.getMedQuest().getMsadm_medis()==1 || edit.getMedQuest().getMsadm_medis_alt()==1 || 
						edit.getMedQuest().getMsadm_pregnant()==1  || (edit.getMedQuest().getMsadm_usg()==1 && edit.getMedQuest().getMsadm_usg_mcu()==1 && edit.getMedQuest().getMsadm_usg_mcu_std()==0 ) ||
						(edit.getMedQuest().getMsadm_usg()==1 && edit.getMedQuest().getMsadm_usg_mcu()==0) ||
						edit.getMedQuest().getMsadm_abortion()==1 || edit.getMedQuest().getMsadm_em_life()>50 || (edit.getMedQuest().getMsadm_sehat()==0 && edit.getMedQuest().getMsadm_sehat_std()==0) ||
						(edit.getMedQuest().getMsadm_penyakit()==1 && edit.getMedQuest().getMsadm_penyakit_std()==0)
						
					){
						edit.getMedQuest().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
					}else{
						edit.getMedQuest().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
					}
			}else{
				if( edit.getMedQuest().getMsadm_medis()==1 || edit.getMedQuest().getMsadm_medis_alt()==1 || 
					edit.getMedQuest().getMsadm_em_life()>50 || (edit.getMedQuest().getMsadm_sehat()==0 && edit.getMedQuest().getMsadm_sehat_std()==0) ||
					(edit.getMedQuest().getMsadm_penyakit()==1 && edit.getMedQuest().getMsadm_penyakit_std()==0)
				){
					edit.getMedQuest().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
				}else{
					edit.getMedQuest().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
				}
			}
		}
	}
	
	public void validate_tertanggung(Object cmd, Errors err){
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Integer mspe_sex = 0;
		if(edit.getMedQuest_tertanggung().getMsadm_pregnant()==null) mspe_sex = 1;
		
		if(edit.getMedQuest_tertanggung().getMsadm_berat()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan mengenai berat");
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_tinggi()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan mengenai tinggi");
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_berat_berubah()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 1b");
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_berubah_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 1b");
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_sehat()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 2.");
			edit.getMedQuest_tertanggung().setMsadm_sehat(0);
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_sehat_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 2");
			edit.getMedQuest_tertanggung().setMsadm_sehat_desc("");
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_penyakit()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 3");
			edit.getMedQuest_tertanggung().setMsadm_penyakit(0);
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_penyakit_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 3");
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_medis()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 4a");
			edit.getMedQuest_tertanggung().setMsadm_medis(0);
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_medis_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 4a");
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_medis_alt()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 4b");
			edit.getMedQuest_tertanggung().setMsadm_medis_alt(0);
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_medis_alt_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 4b");
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_family_sick()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 5");
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_family_sick_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 5");
		}
		if(mspe_sex==0){
			if(edit.getMedQuest_tertanggung().getMsadm_pregnant()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 6a");
			}
			
			if(edit.getMedQuest_tertanggung().getMsadm_pregnant_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 6a");
			}
			
			if(edit.getMedQuest_tertanggung().getMsadm_abortion()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 6b");
			}
			
			if(edit.getMedQuest_tertanggung().getMsadm_abortion_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 6b");
			}
			
			if(edit.getMedQuest_tertanggung().getMsadm_usg()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 6c");
			}
			
			if(edit.getMedQuest_tertanggung().getMsadm_usg_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 6c");
			}
		}
		
		if(edit.getMedQuest().getMsadd_hobby()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 1");
		}
		
		if(edit.getMedQuest().getMsadd_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 2");
		}
		
		if(edit.getMedQuest().getMsadd_desc_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
/*		if(edit.getMedQuest().getMsadd_smoke()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 3a");
		}
		
		if(edit.getMedQuest().getNsadd_many_cig()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 3a");
		}*/
		
		if(edit.getMedQuest().getMsadd_drink_beer()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 3b");
		}
		
		if(edit.getMedQuest().getMsadd_drink_beer_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 3b");
		}
		
		if(edit.getMedQuest().getMsadd_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 3c");
		}
		
		if(edit.getMedQuest().getMsadd_reason_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tertanggung Utama : Silakan diisi terlebih dahulu keterangan point 3c");
		}
		
		if(edit.getMedQuest().getMsadd_life_ins()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tertanggung Utama : Silakan diisi terlebih dahulu pernyataan point 4");
		}
		
		if(edit.getMedQuest().getMsadd_life_ins_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tertanggung Utama: Silakan diisi terlebih dahulu keterangan point 4");
		}
		
		if(edit.getMedQuest_tertanggung().getMsadm_tinggi()!=null && edit.getMedQuest_tertanggung().getMsadm_berat()!=null){
			BigDecimal height =  new BigDecimal(edit.getMedQuest_tertanggung().getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
			BigDecimal bmi = new BigDecimal(edit.getMedQuest_tertanggung().getMsadm_berat()).divide(height,0,RoundingMode.HALF_UP);
			BigDecimal bmi2 =bmi.divide(new BigDecimal(100));
			edit.getMedQuest_tertanggung().setMsadm_bmi(bmi.doubleValue());
			Map Em = uwManager.selectEmBmi(bmi.doubleValue(),edit.getTertanggung().getMste_age());
			edit.getMedQuest_tertanggung().setMsadm_em(Em);
			edit.getMedQuest_tertanggung().setMsadm_em_life( new Double(Em.get("LIFE").toString()) );
			
			//test
			edit.getMedQuest_tertanggung().setMsadm_penyakit_std(0);
			edit.getMedQuest_tertanggung().setMsadm_sehat_std(0);
			
			//pernah pengecekan medis maupun alternatif, hamil, abortion;
			//TT& BB ratio = <=50 % = kondisi standar, >50% = kondisi non standar
			//tidak sehat && sakit tidak standar
			//terkena penyakit && bukan penyakit standar
			//Jika wanita, pernah MCU, dan hasilnya tidak baik
			//Jika wanita, 
			
		/*	Integer mspe_sex = 0;
			if(edit.getMedQuest_tertanggung().getMsadm_pregnant()==null) mspe_sex = 1;*/
			if(mspe_sex==0){
				if(edit.getMedQuest_tertanggung().getMsadm_medis()==1 || edit.getMedQuest_tertanggung().getMsadm_medis_alt()==1 || 
						edit.getMedQuest_tertanggung().getMsadm_pregnant()==1  || (edit.getMedQuest_tertanggung().getMsadm_usg()==1 && edit.getMedQuest_tertanggung().getMsadm_usg_mcu()==1 && edit.getMedQuest_tertanggung().getMsadm_usg_mcu_std()==0 ) ||
						(edit.getMedQuest_tertanggung().getMsadm_usg()==1 && edit.getMedQuest_tertanggung().getMsadm_usg_mcu()==0) ||
						edit.getMedQuest_tertanggung().getMsadm_abortion()==1 || edit.getMedQuest_tertanggung().getMsadm_em_life()>50 || (edit.getMedQuest_tertanggung().getMsadm_sehat()==0 && edit.getMedQuest_tertanggung().getMsadm_sehat_std()==0) ||
						(edit.getMedQuest_tertanggung().getMsadm_penyakit()==1 && edit.getMedQuest_tertanggung().getMsadm_penyakit_std()==0)
						
					){
						edit.getMedQuest_tertanggung().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
					}else{
						edit.getMedQuest_tertanggung().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
					}
			}else{
				if( edit.getMedQuest_tertanggung().getMsadm_medis()==1 || edit.getMedQuest_tertanggung().getMsadm_medis_alt()==1 || 
					edit.getMedQuest_tertanggung().getMsadm_em_life()>50 || (edit.getMedQuest_tertanggung().getMsadm_sehat()==0 && edit.getMedQuest_tertanggung().getMsadm_sehat_std()==0) ||
					(edit.getMedQuest_tertanggung().getMsadm_penyakit()==1 && edit.getMedQuest_tertanggung().getMsadm_penyakit_std()==0)
				){
					edit.getMedQuest_tertanggung().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
				}else{
					edit.getMedQuest_tertanggung().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
				}
			}
		}
	}
	
	public void validate_tambah(Object cmd, Errors err,int age){
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Integer mspe_sex = 0;
		if(edit.getMedQuest_tambah().getMsadm_pregnant()==null) mspe_sex = 1;
		
		if(edit.getMedQuest_tambah().getMsadm_berat()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu pernyataan mengenai berat");
		}
		
		if(edit.getMedQuest_tambah().getMsadm_tinggi()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu pernyataan mengenai tinggi");
		}
		
		if(edit.getMedQuest_tambah().getMsadm_berat_berubah()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu pernyataan point 1b");
		}
		
		if(edit.getMedQuest_tambah().getMsadm_berubah_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu keterangan point 1b");
		}
		
		if(edit.getMedQuest_tambah().getMsadm_sehat()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN  Tambahan I : Silakan diisi terlebih dahulu pernyataan point 2.");
			edit.getMedQuest_tambah().setMsadm_sehat(0);
		}
		
		if(edit.getMedQuest_tambah().getMsadm_sehat_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
		if(edit.getMedQuest_tambah().getMsadm_penyakit()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu pernyataan point 3");
			edit.getMedQuest_tambah().setMsadm_penyakit(0);
		}
		
		if(edit.getMedQuest_tambah().getMsadm_penyakit_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu keterangan point 3");
		}
		
		if(edit.getMedQuest_tambah().getMsadm_medis()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu pernyataan point 4a");
			edit.getMedQuest_tambah().setMsadm_medis(0);
		}
		
		if(edit.getMedQuest_tambah().getMsadm_medis_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu keterangan point 4a");
		}
		
		if(edit.getMedQuest_tambah().getMsadm_medis_alt()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu pernyataan point 4b");
			edit.getMedQuest_tambah().setMsadm_medis_alt(0);
		}
		
		if(edit.getMedQuest_tambah().getMsadm_medis_alt_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu keterangan point 4b");
		}
		
		if(edit.getMedQuest_tambah().getMsadm_family_sick()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu pernyataan point 5");
		}
		
		if(edit.getMedQuest_tambah().getMsadm_family_sick_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu keterangan point 5");
		}
		if(mspe_sex==0){
			if(edit.getMedQuest_tambah().getMsadm_pregnant()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu pernyataan point 6a");
			}
			
			if(edit.getMedQuest_tambah().getMsadm_pregnant_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu keterangan point 6a");
			}
			
			if(edit.getMedQuest_tambah().getMsadm_abortion()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu pernyataan point 6b");
			}
			
			if(edit.getMedQuest_tambah().getMsadm_abortion_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu keterangan point 6b");
			}
			
			if(edit.getMedQuest_tambah().getMsadm_usg()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu pernyataan point 6c");
			}
			
			if(edit.getMedQuest_tambah().getMsadm_usg_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan I : Silakan diisi terlebih dahulu keterangan point 6c");
			}
		}
		
		if(edit.getMedQuest_tambah().getMsadd_hobby()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan I : Silakan diisi terlebih dahulu pernyataan point 1");
		}
		
		if(edit.getMedQuest_tambah().getMsadd_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan I : Silakan diisi terlebih dahulu pernyataan point 2");
		}
		
		if(edit.getMedQuest_tambah().getMsadd_desc_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan I : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
		if(edit.getMedQuest_tambah().getMsadd_smoke()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan I : Silakan diisi terlebih dahulu pernyataan point 3a");
		}
		
		/*if(edit.getMedQuest_tambah().getNsadd_many_cig()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan I : Silakan diisi terlebih dahulu keterangan point 3a");
		}*/
		
		if(edit.getMedQuest_tambah().getMsadd_drink_beer()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan I : Silakan diisi terlebih dahulu pernyataan point 3b");
		}
		
		if(edit.getMedQuest_tambah().getMsadd_drink_beer_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan I : Silakan diisi terlebih dahulu keterangan point 3b");
		}
		
		if(edit.getMedQuest_tambah().getMsadd_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan I : Silakan diisi terlebih dahulu pernyataan point 3c");
		}
		
		if(edit.getMedQuest_tambah().getMsadd_reason_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan I : Silakan diisi terlebih dahulu keterangan point 3c");
		}
		
		if(edit.getMedQuest_tambah().getMsadd_life_ins()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan I : Silakan diisi terlebih dahulu pernyataan point 4");
		}
		
		if(edit.getMedQuest_tambah().getMsadd_life_ins_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan I : Silakan diisi terlebih dahulu keterangan point 4");
		}
		if(edit.getMedQuest_tambah().getMsadm_tinggi()!=null && edit.getMedQuest_tambah().getMsadm_berat()!=null){
			BigDecimal height =  new BigDecimal(edit.getMedQuest_tambah().getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
			BigDecimal bmi = new BigDecimal(edit.getMedQuest_tambah().getMsadm_berat()).divide(height,0,RoundingMode.HALF_UP);
			edit.getMedQuest_tambah().setMsadm_bmi(bmi.doubleValue());
			BigDecimal bmi2 =bmi.divide(new BigDecimal(100));
			Map Em = uwManager.selectEmBmi(bmi.doubleValue(),age);
			edit.getMedQuest_tambah().setMsadm_em(Em);
			edit.getMedQuest_tambah().setMsadm_em_life( new Double(Em.get("LIFE").toString()) );
			
			//test
			edit.getMedQuest_tambah().setMsadm_penyakit_std(0);
			edit.getMedQuest_tambah().setMsadm_sehat_std(0);
			
			//pernah pengecekan medis maupun alternatif, hamil, abortion;
			//TT& BB ratio = <=50 % = kondisi standar, >50% = kondisi non standar
			//tidak sehat && sakit tidak standar
			//terkena penyakit && bukan penyakit standar
			//Jika wanita, pernah MCU, dan hasilnya tidak baik
			//Jika wanita, 
			
		/*	Integer mspe_sex = 0;
			if(edit.getMedQuest_tambah().getMsadm_pregnant()==null) mspe_sex = 1;*/
			if(mspe_sex==0){
				if(edit.getMedQuest_tambah().getMsadm_medis()==1 || edit.getMedQuest_tambah().getMsadm_medis_alt()==1 || 
						edit.getMedQuest_tambah().getMsadm_pregnant()==1  || (edit.getMedQuest_tambah().getMsadm_usg()==1 && edit.getMedQuest_tambah().getMsadm_usg_mcu()==1 && edit.getMedQuest_tambah().getMsadm_usg_mcu_std()==0 ) ||
						(edit.getMedQuest_tambah().getMsadm_usg()==1 && edit.getMedQuest_tambah().getMsadm_usg_mcu()==0) ||
						edit.getMedQuest_tambah().getMsadm_abortion()==1 || edit.getMedQuest_tambah().getMsadm_em_life()>50 || (edit.getMedQuest_tambah().getMsadm_sehat()==0 && edit.getMedQuest_tambah().getMsadm_sehat_std()==0) ||
						(edit.getMedQuest_tambah().getMsadm_penyakit()==1 && edit.getMedQuest_tambah().getMsadm_penyakit_std()==0)
						
					){
						edit.getMedQuest_tambah().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
					}else{
						edit.getMedQuest_tambah().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
					}
			}else{
				if( edit.getMedQuest_tambah().getMsadm_medis()==1 || edit.getMedQuest_tambah().getMsadm_medis_alt()==1 || 
					edit.getMedQuest_tambah().getMsadm_em_life()>50 || (edit.getMedQuest_tambah().getMsadm_sehat()==0 && edit.getMedQuest_tambah().getMsadm_sehat_std()==0) ||
					(edit.getMedQuest_tambah().getMsadm_penyakit()==1 && edit.getMedQuest_tambah().getMsadm_penyakit_std()==0)
				){
					edit.getMedQuest_tambah().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
				}else{
					edit.getMedQuest_tambah().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
				}
			}
		}
	}
	
	public void validate_tambah2(Object cmd, Errors err,int age){
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Integer mspe_sex = 0;
		if(edit.getMedQuest_tambah2().getMsadm_pregnant()==null) mspe_sex = 1;
		
		if(edit.getMedQuest_tambah2().getMsadm_berat()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu pernyataan mengenai berat");
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_tinggi()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu pernyataan mengenai tinggi");
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_berat_berubah()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu pernyataan point 1b");
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_berubah_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu keterangan point 1b");
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_sehat()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu pernyataan point 2.");
			edit.getMedQuest_tambah2().setMsadm_sehat(0);
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_sehat_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_penyakit()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu pernyataan point 3");
			edit.getMedQuest_tambah2().setMsadm_penyakit(0);
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_penyakit_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu keterangan point 3");
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_medis()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu pernyataan point 4a");
			edit.getMedQuest_tambah2().setMsadm_medis(0);
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_medis_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu keterangan point 4a");
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_medis_alt()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu pernyataan point 4b");
			edit.getMedQuest_tambah2().setMsadm_medis_alt(0);
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_medis_alt_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu keterangan point 4b");
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_family_sick()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu pernyataan point 5");
		}
		
		if(edit.getMedQuest_tambah2().getMsadm_family_sick_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu keterangan point 5");
		}
		if(mspe_sex==0){
			if(edit.getMedQuest_tambah2().getMsadm_pregnant()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu pernyataan point 6a");
			}
			
			if(edit.getMedQuest_tambah2().getMsadm_pregnant_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu keterangan point 6a");
			}
			
			if(edit.getMedQuest_tambah2().getMsadm_abortion()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu pernyataan point 6b");
			}
			
			if(edit.getMedQuest_tambah2().getMsadm_abortion_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu keterangan point 6b");
			}
			
			if(edit.getMedQuest_tambah2().getMsadm_usg()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu pernyataan point 6c");
			}
			
			if(edit.getMedQuest_tambah2().getMsadm_usg_desc()==null){
				err.reject("","HALAMAN QUESTIONARE DATA KESEHATAN Tambahan II : Silakan diisi terlebih dahulu keterangan point 6c");
			}
		}
		
		if(edit.getMedQuest_tambah2().getMsadd_hobby()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan II : Silakan diisi terlebih dahulu pernyataan point 1");
		}
		
		if(edit.getMedQuest_tambah2().getMsadd_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan II : Silakan diisi terlebih dahulu pernyataan point 2");
		}
		
		if(edit.getMedQuest_tambah2().getMsadd_desc_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan II : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
		if(edit.getMedQuest_tambah2().getMsadd_smoke()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan II : Silakan diisi terlebih dahulu pernyataan point 3a");
		}
		
		/*if(edit.getMedQuest_tambah2().getNsadd_many_cig()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan II : Silakan diisi terlebih dahulu keterangan point 3a");
		}*/
		
		if(edit.getMedQuest_tambah2().getMsadd_drink_beer()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan II : Silakan diisi terlebih dahulu pernyataan point 3b");
		}
		
		if(edit.getMedQuest_tambah2().getMsadd_drink_beer_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan II : Silakan diisi terlebih dahulu keterangan point 3b");
		}
		
		if(edit.getMedQuest_tambah2().getMsadd_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan II : Silakan diisi terlebih dahulu pernyataan point 3c");
		}
		
		if(edit.getMedQuest_tambah2().getMsadd_reason_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan II : Silakan diisi terlebih dahulu keterangan point 3c");
		}
		
		if(edit.getMedQuest_tambah2().getMsadd_life_ins()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan II : Silakan diisi terlebih dahulu pernyataan point 4");
		}
		
		if(edit.getMedQuest_tambah2().getMsadd_life_ins_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan II : Silakan diisi terlebih dahulu keterangan point 4");
		}
		if(edit.getMedQuest_tambah2().getMsadm_tinggi()!=null && edit.getMedQuest_tambah2().getMsadm_berat()!=null){
			BigDecimal height =  new BigDecimal(edit.getMedQuest_tambah2().getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
			BigDecimal bmi = new BigDecimal(edit.getMedQuest_tambah2().getMsadm_berat()).divide(height,0,RoundingMode.HALF_UP);
			edit.getMedQuest_tambah2().setMsadm_bmi(bmi.doubleValue());
			BigDecimal bmi2 =bmi.divide(new BigDecimal(100));
			Map Em = uwManager.selectEmBmi(bmi.doubleValue(),age);
			edit.getMedQuest_tambah2().setMsadm_em(Em);
			edit.getMedQuest_tambah2().setMsadm_em_life( new Double(Em.get("LIFE").toString()) );
			
			//test
			edit.getMedQuest_tambah2().setMsadm_penyakit_std(0);
			edit.getMedQuest_tambah2().setMsadm_sehat_std(0);
			
			//pernah pengecekan medis maupun alternatif, hamil, abortion;
			//TT& BB ratio = <=50 % = kondisi standar, >50% = kondisi non standar
			//tidak sehat && sakit tidak standar
			//terkena penyakit && bukan penyakit standar
			//Jika wanita, pernah MCU, dan hasilnya tidak baik
			//Jika wanita, 
			
			if(mspe_sex==0){
				if(edit.getMedQuest_tambah2().getMsadm_medis()==1 || edit.getMedQuest_tambah2().getMsadm_medis_alt()==1 || 
						edit.getMedQuest_tambah2().getMsadm_pregnant()==1  || (edit.getMedQuest_tambah2().getMsadm_usg()==1 && edit.getMedQuest_tambah2().getMsadm_usg_mcu()==1 && edit.getMedQuest_tambah2().getMsadm_usg_mcu_std()==0 ) ||
						(edit.getMedQuest_tambah2().getMsadm_usg()==1 && edit.getMedQuest_tambah2().getMsadm_usg_mcu()==0) ||
						edit.getMedQuest_tambah2().getMsadm_abortion()==1 || edit.getMedQuest_tambah2().getMsadm_em_life()>50 || (edit.getMedQuest_tambah2().getMsadm_sehat()==0 && edit.getMedQuest_tambah2().getMsadm_sehat_std()==0) ||
						(edit.getMedQuest_tambah2().getMsadm_penyakit()==1 && edit.getMedQuest_tambah2().getMsadm_penyakit_std()==0)
						
					){
						edit.getMedQuest_tambah2().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
					}else{
						edit.getMedQuest_tambah2().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
					}
			}else{
				if( edit.getMedQuest_tambah2().getMsadm_medis()==1 || edit.getMedQuest_tambah2().getMsadm_medis_alt()==1 || 
					edit.getMedQuest_tambah2().getMsadm_em_life()>50 || (edit.getMedQuest_tambah2().getMsadm_sehat()==0 && edit.getMedQuest_tambah2().getMsadm_sehat_std()==0) ||
					(edit.getMedQuest_tambah2().getMsadm_penyakit()==1 && edit.getMedQuest_tambah2().getMsadm_penyakit_std()==0)
				){
					edit.getMedQuest_tambah2().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
				}else{
					edit.getMedQuest_tambah2().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
				}
			}
		}
	}
	
	public void validate_tambah3(Object cmd, Errors err,int age){
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Integer mspe_sex = 0;
		if(edit.getMedQuest_tambah3().getMsadm_pregnant()==null) mspe_sex = 1;
		
		if(edit.getMedQuest_tambah3().getMsadm_berat()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III: Silakan diisi terlebih dahulu pernyataan mengenai berat");
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_tinggi()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III: Silakan diisi terlebih dahulu pernyataan mengenai tinggi");
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_berat_berubah()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu pernyataan point 1b");
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_berubah_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III: Silakan diisi terlebih dahulu keterangan point 1b");
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_sehat()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu pernyataan point 2.");
			edit.getMedQuest_tambah3().setMsadm_sehat(0);
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_sehat_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_penyakit()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III: Silakan diisi terlebih dahulu pernyataan point 3");
			edit.getMedQuest_tambah3().setMsadm_penyakit(0);
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_penyakit_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu keterangan point 3");
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_medis()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III: Silakan diisi terlebih dahulu pernyataan point 4a");
			edit.getMedQuest_tambah3().setMsadm_medis(0);
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_medis_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu keterangan point 4a");
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_medis_alt()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu pernyataan point 4b");
			edit.getMedQuest_tambah3().setMsadm_medis_alt(0);
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_medis_alt_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu keterangan point 4b");
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_family_sick()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu pernyataan point 5");
		}
		
		if(edit.getMedQuest_tambah3().getMsadm_family_sick_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu keterangan point 5");
		}
		if(mspe_sex==0){
			if(edit.getMedQuest_tambah3().getMsadm_pregnant()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu pernyataan point 6a");
			}
			
			if(edit.getMedQuest_tambah3().getMsadm_pregnant_desc()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu keterangan point 6a");
			}
			
			if(edit.getMedQuest_tambah3().getMsadm_abortion()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu pernyataan point 6b");
			}
			
			if(edit.getMedQuest_tambah3().getMsadm_abortion_desc()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu keterangan point 6b");
			}
			
			if(edit.getMedQuest_tambah3().getMsadm_usg()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu pernyataan point 6c");
			}
			
			if(edit.getMedQuest_tambah3().getMsadm_usg_desc()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan III : Silakan diisi terlebih dahulu keterangan point 6c");
			}
		}
		
		if(edit.getMedQuest_tambah3().getMsadd_hobby()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu pernyataan point 1");
		}
		
		if(edit.getMedQuest_tambah3().getMsadd_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu pernyataan point 2");
		}
		
		if(edit.getMedQuest_tambah3().getMsadd_desc_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
		if(edit.getMedQuest_tambah3().getMsadd_smoke()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu pernyataan point 3a");
		}
		
		/*if(edit.getMedQuest_tambah3().getNsadd_many_cig()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu keterangan point 3a");
		}*/
		
		if(edit.getMedQuest_tambah3().getMsadd_drink_beer()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu pernyataan point 3b");
		}
		
		if(edit.getMedQuest_tambah3().getMsadd_drink_beer_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu keterangan point 3b");
		}
		
		if(edit.getMedQuest_tambah3().getMsadd_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu pernyataan point 3c");
		}
		
		if(edit.getMedQuest_tambah3().getMsadd_reason_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu keterangan point 3c");
		}
		
		if(edit.getMedQuest_tambah3().getMsadd_life_ins()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu pernyataan point 4");
		}
		
		if(edit.getMedQuest_tambah3().getMsadd_life_ins_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu keterangan point 4");
		}
		if(edit.getMedQuest_tambah3().getMsadm_tinggi()!=null && edit.getMedQuest_tambah3().getMsadm_berat()!=null){
			BigDecimal height =  new BigDecimal(edit.getMedQuest_tambah3().getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
			BigDecimal bmi = new BigDecimal(edit.getMedQuest_tambah3().getMsadm_berat()).divide(height,0,RoundingMode.HALF_UP);
			edit.getMedQuest_tambah3().setMsadm_bmi(bmi.doubleValue());
			BigDecimal bmi2 =bmi.divide(new BigDecimal(100));
			Map Em = uwManager.selectEmBmi(bmi.doubleValue(),age);
			edit.getMedQuest_tambah3().setMsadm_em(Em);
			edit.getMedQuest_tambah3().setMsadm_em_life( new Double(Em.get("LIFE").toString()) );
			
			//test
			edit.getMedQuest_tambah3().setMsadm_penyakit_std(0);
			edit.getMedQuest_tambah3().setMsadm_sehat_std(0);
			
			//pernah pengecekan medis maupun alternatif, hamil, abortion;
			//TT& BB ratio = <=50 % = kondisi standar, >50% = kondisi non standar
			//tidak sehat && sakit tidak standar
			//terkena penyakit && bukan penyakit standar
			//Jika wanita, pernah MCU, dan hasilnya tidak baik
			//Jika wanita, 
			
			/*Integer mspe_sex = 0;
			if(edit.getMedQuest_tambah3().getMsadm_pregnant()==null) mspe_sex = 1;*/
			if(mspe_sex==0){
				if(edit.getMedQuest_tambah3().getMsadm_medis()==1 || edit.getMedQuest_tambah3().getMsadm_medis_alt()==1 || 
						edit.getMedQuest_tambah3().getMsadm_pregnant()==1  || (edit.getMedQuest_tambah3().getMsadm_usg()==1 && edit.getMedQuest_tambah3().getMsadm_usg_mcu()==1 && edit.getMedQuest_tambah3().getMsadm_usg_mcu_std()==0 ) ||
						(edit.getMedQuest_tambah3().getMsadm_usg()==1 && edit.getMedQuest_tambah3().getMsadm_usg_mcu()==0) ||
						edit.getMedQuest_tambah3().getMsadm_abortion()==1 || edit.getMedQuest_tambah3().getMsadm_em_life()>50 || (edit.getMedQuest_tambah3().getMsadm_sehat()==0 && edit.getMedQuest_tambah3().getMsadm_sehat_std()==0) ||
						(edit.getMedQuest_tambah3().getMsadm_penyakit()==1 && edit.getMedQuest_tambah3().getMsadm_penyakit_std()==0)
						
					){
						edit.getMedQuest_tambah3().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
					}else{
						edit.getMedQuest_tambah3().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
					}
			}else{
				if( edit.getMedQuest_tambah3().getMsadm_medis()==1 || edit.getMedQuest_tambah3().getMsadm_medis_alt()==1 || 
					edit.getMedQuest_tambah3().getMsadm_em_life()>50 || (edit.getMedQuest_tambah3().getMsadm_sehat()==0 && edit.getMedQuest_tambah3().getMsadm_sehat_std()==0) ||
					(edit.getMedQuest_tambah3().getMsadm_penyakit()==1 && edit.getMedQuest_tambah3().getMsadm_penyakit_std()==0)
				){
					edit.getMedQuest_tambah3().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
				}else{
					edit.getMedQuest_tambah3().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
				}
			}
		}
	}
	public void validate_tambah4(Object cmd, Errors err,int age){
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Integer mspe_sex = 0;
		if(edit.getMedQuest_tambah4().getMsadm_pregnant()==null) mspe_sex = 1;
		
		if(edit.getMedQuest_tambah4().getMsadm_berat()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan mengenai berat");
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_tinggi()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan mengenai tinggi");
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_berat_berubah()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan point 1b");
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_berubah_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu keterangan point 1b");
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_sehat()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan point 2.");
			edit.getMedQuest_tambah4().setMsadm_sehat(0);
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_sehat_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_penyakit()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan point 3");
			edit.getMedQuest_tambah4().setMsadm_penyakit(0);
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_penyakit_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu keterangan point 3");
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_medis()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan point 4a");
			edit.getMedQuest_tambah4().setMsadm_medis(0);
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_medis_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu keterangan point 4a");
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_medis_alt()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan point 4b");
			edit.getMedQuest_tambah4().setMsadm_medis_alt(0);
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_medis_alt_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan point 4b");
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_family_sick()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan point 5");
		}
		
		if(edit.getMedQuest_tambah4().getMsadm_family_sick_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu keterangan point 5");
		}
		if(mspe_sex==0){
			if(edit.getMedQuest_tambah4().getMsadm_pregnant()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan point 6a");
			}
			
			if(edit.getMedQuest_tambah4().getMsadm_pregnant_desc()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu keterangan point 6a");
			}
			
			if(edit.getMedQuest_tambah4().getMsadm_abortion()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan point 6b");
			}
			
			if(edit.getMedQuest_tambah4().getMsadm_abortion_desc()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu keterangan point 6b");
			}
			
			if(edit.getMedQuest_tambah4().getMsadm_usg()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu pernyataan point 6c");
			}
			
			if(edit.getMedQuest_tambah4().getMsadm_usg_desc()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan IV : Silakan diisi terlebih dahulu keterangan point 6c");
			}
		}
		
		if(edit.getMedQuest_tambah4().getMsadd_hobby()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan IV  : Silakan diisi terlebih dahulu pernyataan point 1");
		}
		
		if(edit.getMedQuest_tambah4().getMsadd_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan IV  : Silakan diisi terlebih dahulu pernyataan point 2");
		}
		
		if(edit.getMedQuest_tambah4().getMsadd_desc_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan IV  : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
		if(edit.getMedQuest_tambah4().getMsadd_smoke()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan IV  : Silakan diisi terlebih dahulu pernyataan point 3a");
		}
		
		/*if(edit.getMedQuest_tambah3().getNsadd_many_cig()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu keterangan point 3a");
		}*/
		
		if(edit.getMedQuest_tambah4().getMsadd_drink_beer()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan IV  : Silakan diisi terlebih dahulu pernyataan point 3b");
		}
		
		if(edit.getMedQuest_tambah4().getMsadd_drink_beer_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan IV  : Silakan diisi terlebih dahulu keterangan point 3b");
		}
		
		if(edit.getMedQuest_tambah4().getMsadd_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan IV  : Silakan diisi terlebih dahulu pernyataan point 3c");
		}
		
		if(edit.getMedQuest_tambah4().getMsadd_reason_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan IV  : Silakan diisi terlebih dahulu keterangan point 3c");
		}
		
		if(edit.getMedQuest_tambah4().getMsadd_life_ins()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan IV  : Silakan diisi terlebih dahulu pernyataan point 4");
		}
		
		if(edit.getMedQuest_tambah4().getMsadd_life_ins_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan IV  : Silakan diisi terlebih dahulu keterangan point 4");
		}
		if(edit.getMedQuest_tambah4().getMsadm_tinggi()!=null && edit.getMedQuest_tambah4().getMsadm_berat()!=null){
			BigDecimal height =  new BigDecimal(edit.getMedQuest_tambah4().getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
			BigDecimal bmi = new BigDecimal(edit.getMedQuest_tambah4().getMsadm_berat()).divide(height,0,RoundingMode.HALF_UP);
			edit.getMedQuest_tambah4().setMsadm_bmi(bmi.doubleValue());
			BigDecimal bmi2 =bmi.divide(new BigDecimal(100));
			Map Em = uwManager.selectEmBmi(bmi.doubleValue(),age);
			edit.getMedQuest_tambah4().setMsadm_em(Em);
			edit.getMedQuest_tambah4().setMsadm_em_life( new Double(Em.get("LIFE").toString()) );
			
			//test
			edit.getMedQuest_tambah4().setMsadm_penyakit_std(0);
			edit.getMedQuest_tambah4().setMsadm_sehat_std(0);
			
			//pernah pengecekan medis maupun alternatif, hamil, abortion;
			//TT& BB ratio = <=50 % = kondisi standar, >50% = kondisi non standar
			//tidak sehat && sakit tidak standar
			//terkena penyakit && bukan penyakit standar
			//Jika wanita, pernah MCU, dan hasilnya tidak baik
			//Jika wanita, 
			
			/*Integer mspe_sex = 0;
			if(edit.getMedQuest_tambah3().getMsadm_pregnant()==null) mspe_sex = 1;*/
			if(mspe_sex==0){
				if(edit.getMedQuest_tambah4().getMsadm_medis()==1 || edit.getMedQuest_tambah4().getMsadm_medis_alt()==1 || 
						edit.getMedQuest_tambah4().getMsadm_pregnant()==1  || (edit.getMedQuest_tambah4().getMsadm_usg()==1 && edit.getMedQuest_tambah4().getMsadm_usg_mcu()==1 && edit.getMedQuest_tambah4().getMsadm_usg_mcu_std()==0 ) ||
						(edit.getMedQuest_tambah4().getMsadm_usg()==1 && edit.getMedQuest_tambah4().getMsadm_usg_mcu()==0) ||
						edit.getMedQuest_tambah4().getMsadm_abortion()==1 || edit.getMedQuest_tambah4().getMsadm_em_life()>50 || (edit.getMedQuest_tambah4().getMsadm_sehat()==0 && edit.getMedQuest_tambah4().getMsadm_sehat_std()==0) ||
						(edit.getMedQuest_tambah4().getMsadm_penyakit()==1 && edit.getMedQuest_tambah4().getMsadm_penyakit_std()==0)
						
					){
						edit.getMedQuest_tambah4().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
					}else{
						edit.getMedQuest_tambah4().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
					}
			}else{
				if( edit.getMedQuest_tambah4().getMsadm_medis()==1 || edit.getMedQuest_tambah4().getMsadm_medis_alt()==1 || 
					edit.getMedQuest_tambah4().getMsadm_em_life()>50 || (edit.getMedQuest_tambah4().getMsadm_sehat()==0 && edit.getMedQuest_tambah4().getMsadm_sehat_std()==0) ||
					(edit.getMedQuest_tambah4().getMsadm_penyakit()==1 && edit.getMedQuest_tambah4().getMsadm_penyakit_std()==0)
				){
					edit.getMedQuest_tambah4().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
				}else{
					edit.getMedQuest_tambah4().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
				}
			}
		}
	}
	
	public void validate_tambah5(Object cmd, Errors err,int age){
		Cmdeditbac edit= (Cmdeditbac) cmd;
		Integer mspe_sex = 0;
		if(edit.getMedQuest_tambah5().getMsadm_pregnant()==null) mspe_sex = 1;
		
		if(edit.getMedQuest_tambah5().getMsadm_berat()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu pernyataan mengenai berat");
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_tinggi()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu pernyataan mengenai tinggi");
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_berat_berubah()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu pernyataan point 1b");
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_berubah_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu keterangan point 1b");
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_sehat()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu pernyataan point 2.");
			edit.getMedQuest_tambah5().setMsadm_sehat(0);
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_sehat_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_penyakit()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu pernyataan point 3");
			edit.getMedQuest_tambah5().setMsadm_penyakit(0);
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_penyakit_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu keterangan point 3");
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_medis()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu pernyataan point 4a");
			edit.getMedQuest_tambah5().setMsadm_medis(0);
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_medis_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu keterangan point 4a");
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_medis_alt()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu pernyataan point 4b");
			edit.getMedQuest_tambah5().setMsadm_medis_alt(0);
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_medis_alt_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu keterangan point 4b");
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_family_sick()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu pernyataan point 5");
		}
		
		if(edit.getMedQuest_tambah5().getMsadm_family_sick_desc()==null){
			err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu keterangan point 5");
		}
		if(mspe_sex==0){
			if(edit.getMedQuest_tambah5().getMsadm_pregnant()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu pernyataan point 6a");
			}
			
			if(edit.getMedQuest_tambah5().getMsadm_pregnant_desc()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu keterangan point 6a");
			}
			
			if(edit.getMedQuest_tambah5().getMsadm_abortion()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu pernyataan point 6b");
			}
			
			if(edit.getMedQuest_tambah5().getMsadm_abortion_desc()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu keterangan point 6b");
			}
			
			if(edit.getMedQuest_tambah5().getMsadm_usg()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu pernyataan point 6c");
			}
			
			if(edit.getMedQuest_tambah5().getMsadm_usg_desc()==null){
				err.reject("","HALAMAN QUESTIONARE Tambahan V : Silakan diisi terlebih dahulu keterangan point 6c");
			}
		}
		
		if(edit.getMedQuest_tambah5().getMsadd_hobby()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan V  : Silakan diisi terlebih dahulu pernyataan point 1");
		}
		
		if(edit.getMedQuest_tambah5().getMsadd_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan V  : Silakan diisi terlebih dahulu pernyataan point 2");
		}
		
		if(edit.getMedQuest_tambah5().getMsadd_desc_flight()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan V  : Silakan diisi terlebih dahulu keterangan point 2");
		}
		
		if(edit.getMedQuest_tambah5().getMsadd_smoke()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan V  : Silakan diisi terlebih dahulu pernyataan point 3a");
		}
		
		/*if(edit.getMedQuest_tambah3().getNsadd_many_cig()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan III  : Silakan diisi terlebih dahulu keterangan point 3a");
		}*/
		
		if(edit.getMedQuest_tambah5().getMsadd_drink_beer()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan V  : Silakan diisi terlebih dahulu pernyataan point 3b");
		}
		
		if(edit.getMedQuest_tambah5().getMsadd_drink_beer_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan V  : Silakan diisi terlebih dahulu keterangan point 3b");
		}
		
		if(edit.getMedQuest_tambah5().getMsadd_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan V  : Silakan diisi terlebih dahulu pernyataan point 3c");
		}
		
		if(edit.getMedQuest_tambah5().getMsadd_reason_drugs()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan V  : Silakan diisi terlebih dahulu keterangan point 3c");
		}
		
		if(edit.getMedQuest_tambah5().getMsadd_life_ins()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan V  : Silakan diisi terlebih dahulu pernyataan point 4");
		}
		
		if(edit.getMedQuest_tambah5().getMsadd_life_ins_desc()==null){
			err.reject("","HALAMAN QUESTIONARE DATA PRIBADI Tambahan V  : Silakan diisi terlebih dahulu keterangan point 4");
		}
		if(edit.getMedQuest_tambah5().getMsadm_tinggi()!=null && edit.getMedQuest_tambah4().getMsadm_berat()!=null){
			BigDecimal height =  new BigDecimal(edit.getMedQuest_tambah5().getMsadm_tinggi()).divide(new BigDecimal(100)).pow(2);
			BigDecimal bmi = new BigDecimal(edit.getMedQuest_tambah5().getMsadm_berat()).divide(height,0,RoundingMode.HALF_UP);
			edit.getMedQuest_tambah5().setMsadm_bmi(bmi.doubleValue());
			BigDecimal bmi2 =bmi.divide(new BigDecimal(100));
			Map Em = uwManager.selectEmBmi(bmi.doubleValue(),age);
			edit.getMedQuest_tambah5().setMsadm_em(Em);
			edit.getMedQuest_tambah5().setMsadm_em_life( new Double(Em.get("LIFE").toString()) );
			//test
			edit.getMedQuest_tambah5().setMsadm_penyakit_std(0);
			edit.getMedQuest_tambah5().setMsadm_sehat_std(0);
			
			//pernah pengecekan medis maupun alternatif, hamil, abortion;
			//TT& BB ratio = <=50 % = kondisi standar, >50% = kondisi non standar
			//tidak sehat && sakit tidak standar
			//terkena penyakit && bukan penyakit standar
			//Jika wanita, pernah MCU, dan hasilnya tidak baik
			//Jika wanita, 
			
			/*Integer mspe_sex = 0;
			if(edit.getMedQuest_tambah3().getMsadm_pregnant()==null) mspe_sex = 1;*/
			if(mspe_sex==0){
				if(edit.getMedQuest_tambah5().getMsadm_medis()==1 || edit.getMedQuest_tambah5().getMsadm_medis_alt()==1 || 
						edit.getMedQuest_tambah5().getMsadm_pregnant()==1  || (edit.getMedQuest_tambah5().getMsadm_usg()==1 && edit.getMedQuest_tambah5().getMsadm_usg_mcu()==1 && edit.getMedQuest_tambah5().getMsadm_usg_mcu_std()==0 ) ||
						(edit.getMedQuest_tambah5().getMsadm_usg()==1 && edit.getMedQuest_tambah5().getMsadm_usg_mcu()==0) ||
						edit.getMedQuest_tambah5().getMsadm_abortion()==1 || edit.getMedQuest_tambah5().getMsadm_em_life()>50 || (edit.getMedQuest_tambah5().getMsadm_sehat()==0 && edit.getMedQuest_tambah5().getMsadm_sehat_std()==0) ||
						(edit.getMedQuest_tambah5().getMsadm_penyakit()==1 && edit.getMedQuest_tambah5().getMsadm_penyakit_std()==0)
						
					){
						edit.getMedQuest_tambah5().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
					}else{
						edit.getMedQuest_tambah5().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
					}
			}else{
				if( edit.getMedQuest_tambah5().getMsadm_medis()==1 || edit.getMedQuest_tambah5().getMsadm_medis_alt()==1 || 
					edit.getMedQuest_tambah5().getMsadm_em_life()>50 || (edit.getMedQuest_tambah5().getMsadm_sehat()==0 && edit.getMedQuest_tambah5().getMsadm_sehat_std()==0) ||
					(edit.getMedQuest_tambah5().getMsadm_penyakit()==1 && edit.getMedQuest_tambah5().getMsadm_penyakit_std()==0)
				){
					edit.getMedQuest_tambah5().setMsadm_clear_case(0);// 0 tidak dapat dapat diterima langsung, masuk ke U/W dulu(kondisi non standar)
				}else{
					edit.getMedQuest_tambah5
					().setMsadm_clear_case(1);// 1  dapat dapat diterima & transfer ke print polis langsung(kondisi standar)
				}
			}
		}
	}
}
