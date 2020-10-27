package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Simcard;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * 
 * @author Yusuf
 * @since Sep 11, 2008 (1:26:12 PM)
 */
public class SimasCardFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}	

	@Override
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors errors) throws Exception {
		
		List<DropDown> daftarJenis = new ArrayList<DropDown>();
		daftarJenis.add(new DropDown("0", "Individu (NB)"));
		daftarJenis.add(new DropDown("1", "Individu (Existing)"));
		daftarJenis.add(new DropDown("2", "Direksi"));
		daftarJenis.add(new DropDown("3", "Rekanan Direksi"));
		daftarJenis.add(new DropDown("4", "Merchant"));
		daftarJenis.add(new DropDown("5", "Marketing"));
		daftarJenis.add(new DropDown("6", "Karyawan MKL"));
		daftarJenis.add(new DropDown("7", "Karyawan BO"));
		
		List<DropDown> daftarCabang = elionsManager.selectDropDown("EKA.LST_CABANG", "LCA_ID", "LCA_NAMA", "", "LCA_NAMA", null);
		
		List<DropDown> daftarFlagPrint = new ArrayList<DropDown>();
		daftarFlagPrint.add(new DropDown("0", "Kartu"));
		daftarFlagPrint.add(new DropDown("1", "Surat"));
		daftarFlagPrint.add(new DropDown("2", "List"));
		daftarFlagPrint.add(new DropDown("3", "File"));
		daftarFlagPrint.add(new DropDown("4", "Tidak Print"));
		
		List<DropDown> daftarYesNo = new ArrayList<DropDown>();
		daftarYesNo.add(new DropDown("0", "No"));
		daftarYesNo.add(new DropDown("1", "Yes"));
		
		Map m = new HashMap();
		m.put("daftarJenis", daftarJenis);
		m.put("daftarCabang", daftarCabang);
		m.put("daftarFlagPrint", daftarFlagPrint);
		m.put("daftarYesNo", daftarYesNo);
		return m;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String mj = ServletRequestUtils.getStringParameter(request, "mj", "");
		String nk = ServletRequestUtils.getStringParameter(request, "nk", "");
		
		Simcard s;

		//EDIT
		if(!mj.equals("") && !nk.equals("")) {
			s = elionsManager.selectSimcard(mj, nk);
			
		//NEW
		}else {
			s = new Simcard();
			s.setFlag_aktif(1); //YES
			s.setFlag_roll(1); //YES
			s.setKartu_ke(1);
		}
		
		return s;
	}

	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Simcard s = (Simcard) cmd;
		if(s.getMsc_jenis().intValue() != 2 && s.getMsc_jenis().intValue() != 3) {
			ValidationUtils.rejectIfEmptyOrWhitespace(err, "no_kartu", "", "Harap isi Nomor Kartu");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(err, "nama", "", "Harap isi Nama");
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Date sysdate = elionsManager.selectSysdateSimple();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
       Simcard s = (Simcard) command;
       s.setLca_cabang(elionsManager.selectLstCabangNamaCabang(s.getLca_id()));
       s.setTgl_input(sysdate);
       elionsManager.saveSimcard((Simcard) command, currentUser);
       return new ModelAndView(new RedirectView(request.getContextPath() + "/uw/simas/input.htm?a=1&mj=" + s.getMsc_jenis() + "&nk=" + s.getNo_kartu()));
	}
	
}