package com.ekalife.elions.web.common;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Currency;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.parent.ParentFormController;

public class CurrencyFormController extends ParentFormController {

	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command cmd = new Command();
		cmd.setTglAwal(defaultDateFormat.format(elionsManager.selectSysdate(-7)));
		cmd.setTglAkhir(defaultDateFormat.format(elionsManager.selectSysdate()));
		List daftarKurs = elionsManager.selectCurrency(cmd);
		cmd.setDaftarKurs(daftarKurs);
		
		String kursBelomAda = "";
		Currency kurs = new Currency();
		kurs.setLkh_date(elionsManager.selectSysdate());
		kurs.setLku_id("02");
		if(elionsManager.validationCurrency(kurs)==0) kursBelomAda += "\\n- DOLLAR (US$)"; 
		kurs.setLku_id("03");
		if(elionsManager.validationCurrency(kurs)==0) kursBelomAda += "\\n- SINGAPORE (SIN$)"; 
		
		if(!kursBelomAda.equals("")) request.setAttribute("kursBelomAda", "Kurs hari ini belum ada untuk : " + kursBelomAda + "\\nSilahkan masukkan kurs secara manual.");
		
		return cmd;
	}

	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, null, dateEditor);
		binder.registerCustomEditor(Double.class, null, doubleEditor);
	}
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();
		map.put("select_kurs",DroplistManager.getInstance().get("KURS.xml","ID",request));
		return map;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException err) throws Exception {
		Command cmd = (Command) command;
		String show;
		if((show = request.getParameter("show")) != null) {
			if("show".equals(show)) {
				List daftarKurs = elionsManager.selectCurrency(cmd);
				cmd.setDaftarKurs(daftarKurs);
				err.reject("","");
			}else if("add".equals(show)) {
				Currency c = new Currency();
				c.setFlag_insert("1");
				c.setLku_id("02");
				c.setLkh_date(elionsManager.selectSysdate());
				cmd.getDaftarKurs().add(c);
				err.reject("","");
			}else if("save".equals(show)) {
				for(int i=0; i<cmd.getDaftarKurs().size(); i++) {
					err.setNestedPath("daftarKurs["+i+"]");
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "lkh_date", "", "Silahkan masukkan tanggal kurs");
					ValidationUtils.rejectIfEmptyOrWhitespace(err, "lkh_currency", "", "Silahkan masukkan Kurs Tengah BI");
					if( !(err.hasFieldErrors("lkh_date") || err.hasFieldErrors("lkh_currency")) ) {
						Currency kurs = (Currency) cmd.getDaftarKurs().get(i);
						if(kurs.getLkh_date().after(elionsManager.selectSysdate(0))) {
							err.rejectValue("lkh_date", "", "Maaf, tetapi anda tidak diperbolehkan menyimpan kurs lebih besar dari hari ini");
						}else if(kurs.getFlag_insert().equals("1") && elionsManager.validationCurrency(kurs)>0) {
							err.rejectValue("lkh_date", "", "Maaf, tetapi kurs hari ini sudah ada. Silahkan meng-update yang sudah tersimpan");
						}
					}
				}
				err.setNestedPath("");
			}else if("delete".equals(show)) {
				cmd.getDaftarKurs().remove(ServletRequestUtils.getRequiredIntParameter(request, "del"));
			}
		}
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		elionsManager.saveCurrency(((Command) command).getDaftarKurs());
		return new ModelAndView(new RedirectView(getSuccessView())).addObject("sukses", "sukses");
	}

	
}
