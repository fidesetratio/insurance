package com.ekalife.elions.web.common;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.CommandEditData;
import com.ekalife.elions.model.DepositPremium;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Controller Untuk Edit2 Data Polis New Business
 * 
 * @author Yusuf
 * @since Sep 26, 2008 (2:06:25 PM)
 */
public class EditDataFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, null, dateEditor);
		binder.registerCustomEditor(Double.class, null, doubleEditor);
		binder.registerCustomEditor(Integer.class, null, integerEditor);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		CommandEditData cmd = new CommandEditData();
		cmd.setReg_spaj(ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj")); //mandatory
		cmd.setListTitipanPremi(uwManager.selectTitipanPremiBySpaj(cmd.getReg_spaj()));
		cmd.setListPayment(uwManager.selectPaymentBySpaj(cmd.getReg_spaj()));
		return cmd;
	}

	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();
		map.put("now", elionsManager.selectSysdateSimple());
		map.put("listRekEkalife", 
			elionsManager.selectDropDown(
				"eka.lst_rek_ekalife a, eka.lst_bank b, eka.lst_bank_pusat c, eka.lst_kurs d",
				"a.lsrek_id", 
				//"c.lsbp_nama || ' ' || b.lbn_nama || ' [' || a.lre_acc_no || '|' || d.lku_symbol || '|' || a.lsrek_symbol || ']'", 
				"c.lsbp_nama || ' ' || b.lbn_nama || ' [' || a.lre_acc_no || ']'", "",  
				"2", "a.lbn_id = b.lbn_id AND b.lsbp_id = c.lsbp_id AND a.lku_id = d.lku_id"));
		map.put("listJenisBayar", 
			elionsManager.selectDropDown(
				"eka.lst_payment_type", "lsjb_id", "lsjb_type || ' (' || decode(lsjb_type_bank, 1, 'BANK', 'NON') || ')'", "", "2", null));
		map.put("listKurs",
			elionsManager.selectDropDown(
				"eka.lst_kurs", "lku_id", "lku_symbol", "", "2", null));
		map.put("listBankPusat",
			elionsManager.selectDropDown(
				"eka.lst_bank_pusat", "lsbp_id", "lsbp_nama", "", "2", null));
		
		List<DropDown> listEdit = new ArrayList<DropDown>();
		listEdit.add(new DropDown("TitipanPremi", 	"Titipan Premi"));
		listEdit.add(new DropDown("Payment", 		"Payment"));
		listEdit.add(new DropDown("AhliWaris", 		"Ahli Waris"));
		listEdit.add(new DropDown("Rekening", 		"Rekening"));
		map.put("listEdit", listEdit);
		
		return map;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException err) throws Exception {
		CommandEditData cmd = (CommandEditData) command;
		String edit = ServletRequestUtils.getRequiredStringParameter(request, "listEdit");
		
		if("TitipanPremi".equals(edit)) {
			int jml = 0;
			for(DepositPremium d : cmd.getListTitipanPremi()) {
				if(d.isChecked()) jml++;
			}
			if(jml==0) err.reject("", "Harap check/centang minimal 1 data yang akan diedit");
		}else if("Payment".equals(edit)) {
			
		}else if("AhliWaris".equals(edit)) {
			
		}
		
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandEditData cmd = (CommandEditData) command;
		Map m = new HashMap();
		m.put("reg_spaj", cmd.getReg_spaj());
		
		//user menekan tombol save
//		if(request.getParameter("save") != null) {
//			String edit = ServletRequestUtils.getRequiredStringParameter(request, "listEdit");
//			User currentUser = (User) request.getSession().getAttribute("currentUser");
//			
//			m.put("e", edit);
//			m.put("pesan", uwManager.editData(edit, cmd, currentUser));
//		}
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/common/edit.htm")).addAllObjects(m);
	}
	
}