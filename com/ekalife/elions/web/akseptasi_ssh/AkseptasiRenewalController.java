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
 * @created : May 16, 2016
 * 
 */
public class AkseptasiRenewalController extends ParentFormController {		
		@Override
		protected Object formBackingObject(HttpServletRequest request) throws Exception {
			UwRenewal cmdr = new UwRenewal();
			HttpSession session = request.getSession();
			User currentUser = (User)session.getAttribute("currentUser");
			
			String spaj = ServletRequestUtils.getStringParameter(request, "reg_spaj");
			cmdr.setReg_spaj(ServletRequestUtils.getStringParameter(request, "reg_spaj"));
			cmdr.setLsRenewal(uwManager.selectUwRenewal(cmdr.getReg_spaj()));
			cmdr.setAksep(DroplistManager.getInstance().get("aksep_renewal.xml","",request));
			cmdr.setLus_id(currentUser.getLus_id());
			cmdr.setLus_login(currentUser.getName());
			cmdr.setLus_full_name(currentUser.getLus_full_name());
			cmdr.setLus_dept(currentUser.getDept());
			cmdr.setLsDataUsulan1(uwManager.selectRenewalDU(spaj,"1"));
			cmdr.setLsDataUsulan2(uwManager.selectRenewalDU(spaj,"2"));
			
			String nmr = bacManager.getNoSuratUwRenewal(5,"01",0);
			cmdr.setNomorsrt(nmr);
			
			return cmdr;
		}
		
		@Override
		protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
			UwRenewal cmdr =(UwRenewal) cmd;
			String prod1 = (ServletRequestUtils.getStringParameter(request, "prod1","")); // prod
			String area1 = (ServletRequestUtils.getStringParameter(request, "area1","")); // 2:borderline 4:xtrapremi 5:accept
			String area7 = (ServletRequestUtils.getStringParameter(request, "area7","")); // extra premi persenan
			String area6 = (ServletRequestUtils.getStringParameter(request, "area6","")); // ket tambahan
			String crbyr = (ServletRequestUtils.getStringParameter(request, "crbyr","")); // cara bayar
			String area3 = (ServletRequestUtils.getStringParameter(request, "area3","")); // nmr surat
			String area4 = (ServletRequestUtils.getStringParameter(request, "area4","")); // premi
			String area2 = (ServletRequestUtils.getStringParameter(request, "area2","")); // premi topup
			
			cmdr.setProd1(prod1);
			cmdr.setArea1(area1); 
			cmdr.setArea7(area7);
			cmdr.setArea6(area6);
			cmdr.setCrbyr(crbyr);
			cmdr.setArea3(area3);
			cmdr.setArea4(area4);
			cmdr.setArea2(area2);

			if(prod1.equals("")) err.reject("","Produk Belum Dipilih.");
			
			if(area1.equals("4")){
				
				if(area3.equals("")) err.reject("","Nomor Surat Belum Diisi.");
				if(area3.contains("\\") || area3.contains("'")) err.reject("","Jangan menggunakan tanda ' dan \\.");
				if(area6.contains("\\") || area6.contains("'")) err.reject("","Jangan menggunakan tanda ' dan \\.");

				if(!area6.equals("")){
					if(area7.equals("")) err.reject("","Harap Diisi Besaran Persentase Extra Premi.");
				}
				
				if(area4.equals("")) err.reject("","Premi Extra Belum Diisi.");
			}
		}
		
		@Override
		protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
			UwRenewal cmdRenewal =(UwRenewal) cmd;
			User currentUser=(User)request.getSession().getAttribute("currentUser");
			cmdRenewal.setArea2(ServletRequestUtils.getStringParameter(request, "area2",""));
			
			return new ModelAndView("akseptasi_ssh/akseptasi_renewal", err.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request, cmd, err));
		}
	}
	