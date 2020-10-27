package com.ekalife.elions.web.bac;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.ekalife.elions.model.Policy;
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

public class QuestionareNew extends ParentFormController{
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
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		String pesan = ServletRequestUtils.getStringParameter(request, "pesan","");
		String flag = ServletRequestUtils.getStringParameter(request, "flag","");
		HashMap<String, Object> cmd = new HashMap<String, Object>();
		String today = defaultDateFormat.format(elionsManager.selectSysdate());
		ArrayList data_LQGPP=new ArrayList();
		ArrayList data_LQLPP=new ArrayList();
		ArrayList data_LQGTT=new ArrayList();
		ArrayList data_LQLTT=new ArrayList();
		ArrayList data_LQGQS=new ArrayList();
		ArrayList data_LQLQS=new ArrayList();

		//buat SPAJ SIO
		ArrayList data_LQGSIO=new ArrayList();
		ArrayList data_LQLSIO=new ArrayList();
		
		Integer index=null;
		Integer index2=null;

		String mspo_flag = bacManager.selectMspoFLagSpaj(spaj);

		Integer lspd_id = 0;
		Policy pol = bacManager.selectMstPolicyAll(spaj);
		lspd_id = pol.getLspd_id();

		Integer jml_ttg = 0;
		Datausulan du = elionsManager.selectDataUsulanutama(spaj);
		jml_ttg = du.getJml_peserta();
		Date question_valid_date = null;
		Date sept2015 = null;
		try {
			sept2015 = defaultDateFormat.parse("01/09/2015");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		}
		//Set Data Questionare , apakah > AUG2015 || < AUG2015
		if(du.getMste_beg_date().before(sept2015)){
			question_valid_date = defaultDateFormat.parse("01/08/2014");
		}else{
			question_valid_date = null;
			if(flag.equals("3")){
				index=105;
				index2=130;
			}else if(flag.equals("4")){
				index=131;
				index2=156;
			}
		}

		//mspo_flag = "4";
		//jml_ttg = 4;
		if(flag.equals("2")){
			data_LQLPP=Common.serializableList(bacManager.selectQuestionareNew(spaj, 1,index,index2));
			if(data_LQLPP.isEmpty() || data_LQLPP==null){
				data_LQLPP =Common.serializableList(bacManager.selectDataLQL(1,question_valid_date,index,index2));
			}else{
				data_LQLPP=Common.serializableList(bacManager.selectQuestionareNew(spaj, 1,index,index2));
			}
			data_LQGPP =Common.serializableList(bacManager.selectDataLQG(1, question_valid_date,index,index2));
			cmd.put("data_LQGPP", data_LQGPP);
			cmd.put("data_LQLPP", data_LQLPP);
		}else if(flag.equals("1")){
			if(mspo_flag.equals("4")){
				data_LQLSIO=Common.serializableList(bacManager.selectQuestionareNew(spaj, 12,index,index2));

				if(data_LQLSIO.isEmpty() || data_LQLSIO==null){
					data_LQLSIO =Common.serializableList(bacManager.selectDataLQL(12,question_valid_date,index,index2));
				}else{
					data_LQLSIO=Common.serializableList(bacManager.selectQuestionareNew(spaj, 12,index,index2));
				}

				data_LQGSIO =Common.serializableList(bacManager.selectDataLQG(12, question_valid_date,index,index2));

				cmd.put("data_LQGSIO", data_LQGSIO);
				cmd.put("data_LQLSIO", data_LQLSIO);
			}else{
				data_LQLTT=Common.serializableList(bacManager.selectQuestionareNew(spaj, 2,index,index2));
				if(data_LQLTT.isEmpty() || data_LQLTT==null){
					data_LQLTT =Common.serializableList(bacManager.selectDataLQL(2,question_valid_date,index,index2));
				}else{
					data_LQLTT=Common.serializableList(bacManager.selectQuestionareNew(spaj, 2,index,index2));
				}
				data_LQGTT =Common.serializableList(bacManager.selectDataLQG(2, question_valid_date,index,index2));
				cmd.put("data_LQGTT", data_LQGTT);
				cmd.put("data_LQLTT", data_LQLTT);
			}
		}else if(flag.equals("3")){
			data_LQLQS=Common.serializableList(bacManager.selectQuestionareNew(spaj,3,index,index2));
			if(data_LQLQS.isEmpty() || data_LQLQS==null){
				data_LQLQS=Common.serializableList(bacManager.selectDataLQL(3,question_valid_date,index,index2));
			}else{
				data_LQLQS=Common.serializableList(bacManager.selectQuestionareNew(spaj,3,index,index2));
			}
			HashMap LQLQS = (HashMap) data_LQLQS.get(0);

			if(LQLQS.get("QUESTION_VALID_DATE") != null){
				question_valid_date = (Date) LQLQS.get("QUESTION_VALID_DATE");
				Date aug31 = null;
				try {
					aug31 = defaultDateFormat.parse("31/08/2015");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e);
				}
				if(question_valid_date.before(aug31)){
					question_valid_date = defaultDateFormat.parse("01/08/2014");
				}else{
					question_valid_date = null;
				}
			}
			data_LQGQS =Common.serializableList(bacManager.selectDataLQG(3, question_valid_date,index,index2));
			cmd.put("data_LQGQS", data_LQGQS);
			cmd.put("data_LQLQS", data_LQLQS);
		}else{
			data_LQLQS =Common.serializableList(bacManager.selectQuestionareNew(spaj, 3,index,index2));
			if(data_LQLQS.isEmpty() || data_LQLQS==null){
				data_LQLQS =Common.serializableList(bacManager.selectDataLQL(3,question_valid_date,index,index2));
			}else{
				data_LQLQS=Common.serializableList(bacManager.selectQuestionareNew(spaj, 3,index,index2));
			}
			HashMap LQLQS = (HashMap) data_LQLQS.get(0);

			if(LQLQS.get("QUESTION_VALID_DATE") != null){
				question_valid_date = (Date) LQLQS.get("QUESTION_VALID_DATE");
				Date aug31 = null;
				try {
					aug31 = defaultDateFormat.parse("31/08/2015");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e);
				}
				if(question_valid_date.before(aug31)){
					question_valid_date = defaultDateFormat.parse("01/08/2014");
				}else{
					question_valid_date = null;
				}
			}
			data_LQGQS =Common.serializableList(bacManager.selectDataLQG(3, question_valid_date,index,index2));
			cmd.put("data_LQGQS", data_LQGQS);
			cmd.put("data_LQLQS", data_LQLQS);
		}

		cmd.put("spaj", spaj);
		cmd.put("flag", flag);
		cmd.put("mspo_flag", mspo_flag);
		cmd.put("today", today);
		cmd.put("pesan", pesan);
		cmd.put("lspd_id", lspd_id);
		cmd.put("jml_ttg", jml_ttg);

		return cmd;
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		/*HttpSession session = request.getSession();
		ArrayList data_LQLTT=Common.serializableList(bacManager.selectDataLQL(2));
		HashMap map = new HashMap();
		for(int i=0;i<data_LQLTT.size();i++){		
			HashMap m = (HashMap) data_LQLTT.get(i);
			Integer question_id = ((BigDecimal)m.get("QUESTION_ID")).intValue();
			Integer option_type = ((BigDecimal)m.get("OPTION_TYPE")).intValue();
			Integer option_group = ((BigDecimal)m.get("OPTION_GROUP")).intValue();
			Date valid_date= (Date)m.get("QUESTION_VALID_DATE");
			Integer option_order = ((BigDecimal)m.get("OPTION_ORDER")).intValue();
			String dataForSubstring =ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group+"_"+option_order,"");
			logger.info( question_id+"_"+option_type+"_"+option_group+"_"+option_order);
			logger.info(dataForSubstring);
		 *//**
		 * krn ga bisa diset value, sehingga value untuk radio butt diset coding
		 * contoh : jika opt_ord =1 (ya), dicentang, maka valuenya 1 , jika tidak dicentang diset 0
		 *//*
			if (option_type==1 ){
				if(option_order==1){
					dataForSubstring=ServletRequestUtils.getStringParameter(request, question_id+"_"+option_type+"_"+option_group,"");
					if(dataForSubstring.equals("0")){
						dataForSubstring="0";
					}
				}

			}
		}
		err.reject("err","HALAMAN QUESTIONARE DATA KESEHATAN Pemegang Polis : Silakan diisi terlebih dahulu pernyataan mengenai berat");*/
		
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		HashMap map = new HashMap();
		//		Cmdeditbac edit= (Cmdeditbac) cmd;
		String pesan ="";
		HashMap map2 = (HashMap)cmd;

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj");
		String flag = ServletRequestUtils.getStringParameter(request, "flag");
		String flag2 = ServletRequestUtils.getStringParameter(request, "flag2","");
		Integer customFields = ServletRequestUtils.getIntParameter(request, "jml");
		Boolean proses =bacManager.prosesSaveQuestionare(request, spaj, customFields, currentUser,Integer.parseInt(flag));
		if(proses==true){
			if(flag2.equals("5")){
			map.put("pesan","Data Sudah Tersimpan.");
			pesan = "Data Sudah Tersimpan.";
			flag2="1";//balikin ke Tertanggung kalau SUKSES
			}
		}else{
			if(flag2.equals("5")){
			map.put("pesan","Data Tidak Berhasil Disimpan.");
			pesan = "Data Tidak Berhasil Disimpan.";
			}else{
				map.put("pesan","Terjadi Error Saat Me-Loading Data. . . Harap Hubungi IT");
				pesan = "Terjadi Error Saat Me-Loading Data. . . Harap Hubungi IT.";
			}
		}
		if (proses==true)return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/questionarenew.htm?spaj="+spaj+"&pesan="+pesan+"&flag="+flag2));
		else return new ModelAndView("bac/questionareNew","cmd", map2).addAllObjects(map);
	}
	
}
