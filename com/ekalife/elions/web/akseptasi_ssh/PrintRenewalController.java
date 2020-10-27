package com.ekalife.elions.web.akseptasi_ssh;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.worksheet.UwRenewal;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author  : Randy
 * @created : May 30, 2016
 * 
 */
public class PrintRenewalController extends ParentFormController {
		
		@Override
		protected Object formBackingObject(HttpServletRequest request) throws Exception {
			UwRenewal cmdr = new UwRenewal();
			HttpSession session = request.getSession();
			User currentUser = (User)session.getAttribute("currentUser");
			
			cmdr.setReg_spaj(ServletRequestUtils.getStringParameter(request, "nospaj"));
			cmdr.setArea1(ServletRequestUtils.getStringParameter(request, "a1"));
			cmdr.setArea2(ServletRequestUtils.getStringParameter(request, "a2",""));
			cmdr.setArea4(ServletRequestUtils.getStringParameter(request, "a4",""));
			cmdr.setProd1(ServletRequestUtils.getStringParameter(request, "p1"));
			cmdr.setCrbyr(ServletRequestUtils.getStringParameter(request, "c1",""));
			cmdr.setArea7(ServletRequestUtils.getStringParameter(request, "a7",""));
			
			cmdr.setLsRenewal(uwManager.selectUwRenewal(cmdr.getReg_spaj()));
			cmdr.setAksep(DroplistManager.getInstance().get("aksep_renewal.xml","",request));
			cmdr.setLus_id(currentUser.getLus_id());
			cmdr.setLus_login(currentUser.getName());
			cmdr.setLus_full_name(currentUser.getLus_full_name());
			cmdr.setLus_dept(currentUser.getDept());
			
			
			return cmdr;
		}
		
		@Override
		protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
			UwRenewal cmdRenewal =(UwRenewal) cmd;
			if(cmdRenewal.getArea1()=="")
				err.reject("","Jenis Akseptasi Belum Dipilih. Harap Ke Halaman Akesptasi Renewal.");	
			if(cmdRenewal.getArea3()=="")
				err.reject("","Nomor Surat Masih Kosong.");
		}
		
		@Override
		protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
			UwRenewal cmdRenewal =(UwRenewal) cmd;
			User currentUser=(User)request.getSession().getAttribute("currentUser");
						
			return new ModelAndView("akseptasi_ssh/print_surat_renewal", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request, cmd, err));
		}
	}
	