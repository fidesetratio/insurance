package com.ekalife.elions.web.uw;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Questionnaire;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

public class QuestionnaireFormController extends ParentFormController  {

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command cmd = new Command();
		cmd.setReg_spaj(ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj"));
		cmd.setPesan(ServletRequestUtils.getStringParameter(request, "pesan", ""));
		cmd.setDaftarQuestionnaire(uwManager.selectQuestionnaireFromSpaj(1, cmd.getReg_spaj()));
		
		//bila belum pernah diisi, set beberapa nilai default
		if(!cmd.getDaftarQuestionnaire().isEmpty()){
			if(cmd.getDaftarQuestionnaire().get(0).getReg_spaj() == null){
				for(Questionnaire q : cmd.getDaftarQuestionnaire()){
					q.setReg_spaj(cmd.getReg_spaj());
					q.setMsqu_jawab(1);
				}
			}
		}
		
		return cmd;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		Command cmd = (Command) command;
		for(int i=0; i<cmd.getDaftarQuestionnaire().size(); i++){
			errors.setNestedPath("daftarQuestionnaire["+i+"]");
			Questionnaire q = (Questionnaire) cmd.getDaftarQuestionnaire().get(i);
			if(q.msqu_jawab.intValue() == 0 && q.msqu_desc.trim().equals("")){
				errors.reject("msqu_desc", "Silahkan lengkapi kolom keterangan bila jawaban TIDAK");
			}
		}
		errors.setNestedPath("");
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException err) throws Exception {
		Command cmd = (Command) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String pesan = uwManager.saveMstQuestionnaire(cmd.getReg_spaj(), currentUser.getLus_id(), cmd.getDaftarQuestionnaire());
		return new ModelAndView(new RedirectView(request.getContextPath()+"/uw/questionnaire.htm?reg_spaj=" + cmd.getReg_spaj())).addObject("pesan", pesan);
	}
	
}
