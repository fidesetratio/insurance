package com.ekalife.elions.web.uw;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.Pbp;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.User;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Window Input MST_PBP
 * 
 * @author Yusuf
 * @since Oct 10, 2008 (6:21:20 PM)
 */
public class InputSaveBayarLinkFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}	

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Command cmd = new Command();
		String reg_spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");

		cmd.setPowersave(elionsManager.selectInformasiPbp(reg_spaj));
		cmd.setDaftarPbp(elionsManager.selectPbp(reg_spaj));
		
		for(Pbp p : cmd.getDaftarPbp()) {
			p.setPowerSave(elionsManager.selectInformasiPbp(p.reg_spaj));
		}
		
		if(cmd.getDaftarPbp().size() < 4) {
			for(int i = cmd.getDaftarPbp().size(); i < 4; i ++) {
				Pbp p = new Pbp();
				p.setPowerSave(new Powersave());
				cmd.getDaftarPbp().add(p);
			}
		}
		
		return cmd;
	}

	@Override
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors errors) throws Exception {
		Map refData = new HashMap();
		refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER.xml","ID",request));
		return refData;
	}

	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {

		Command cmd = (Command) command;
		String lsbs_id = uwManager.selectBusinessId(cmd.getPowersave().getReg_spaj());
		int flag_transfer = this.elionsManager.selectValidasiPbp(cmd.getPowersave().getReg_spaj());

		if(request.getParameter("save") != null) {
			
			//bila sudah transfer, tidak boleh edit
			if(flag_transfer > 0) errors.reject("", "SPAJ ini sudah ditransfer. Tidak bisa edit informasi save bayar link!");
			
//			if(!lsbs_id.equals("134") && !products.cerdas(lsbs_id) && !lsbs_id.equals("191")) errors.reject("", "Produk Utama Bukan Platinum Link / Cerdas! ");
			
			for(int i = 0; i < cmd.getDaftarPbp().size(); i++) {
				Pbp p = (Pbp) cmd.getDaftarPbp().get(i);
				if(!p.getReg_spaj().equals("")) {
					Powersave pow = elionsManager.selectInformasiPbp(p.getReg_spaj()); 
					if(pow == null) {
						errors.rejectValue("daftarPbp["+i+"].reg_spaj", "", "Produk Save Ke-"+(i+2)+" (" + p.getReg_spaj() + ") bukan Produk Powersave! ");
					}else if(!(pow.getLsbs_id().intValue() == 158 && (pow.getLsdbs_number().intValue()==10 || pow.getLsdbs_number().intValue()==11 || pow.getLsdbs_number().intValue()==12))) {
//						errors.rejectValue("daftarPbp["+i+"].reg_spaj", "", "Produk Save Ke-"+(i+2)+" (" + p.getReg_spaj() + ") bukan Produk Save Bayar Link!");
					}else { 
						
						for(int j = cmd.getDaftarPbp().size()-1; j > i; j--) {
							Pbp tmp = cmd.getDaftarPbp().get(j);
							if(p.getReg_spaj().equals(tmp.getReg_spaj())) {
								errors.rejectValue("daftarPbp["+i+"].reg_spaj", "", "Tidak boleh memasukkan nomor yang sama dua kali!");
							}
						}
						
						//validasi MGI, gak usah ditambahkan (himmia : 13 okt)
//						p.setPremi_ke(i+2);
//						int mgi = Integer.valueOf(p.getPowerSave().getMps_jangka_inv());
//						if(p.getPremi_ke().intValue() == 2) {
//							if(mgi != 12)  errors.rejectValue("daftarPbp["+i+"].reg_spaj", "", "Produk Save Ke-"+(i+2)+" (" + p.getReg_spaj() + ") MGI-nya harus 12 Bulan!");
//						}else if(p.getPremi_ke().intValue() == 3) {
//							if(mgi != 12 && mgi != 24)  errors.rejectValue("daftarPbp["+i+"].reg_spaj", "", "Produk Save Ke-"+(i+2)+" (" + p.getReg_spaj() + ") MGI-nya harus 12/24 Bulan!");
//						}else if(p.getPremi_ke().intValue() == 4) {
//							
//						}else if(p.getPremi_ke().intValue() == 5) {
//							
//						}
					}
					p.setPowerSave(pow);
				}
			}

		}else if(request.getParameter("transfer") != null) {
			//bila sudah transfer, tidak boleh edit
			if(flag_transfer > 0) errors.reject("", "SPAJ ini sudah ditransfer. Tidak bisa edit informasi save bayar link!");
			//harus 4 row
			int counter = 0;
			for(Pbp p : cmd.getDaftarPbp()) {
				if(!p.getReg_spaj().equals("")) counter++;
			}
			if(counter != 4) errors.reject("", "Untuk Transfer, harus ada 4 SPAJ Save Untuk Pembayaran Link. Terima kasih");
		}
		
	}

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		Command cmd = (Command) command;
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        String messageAfterSave = "";

        Map m = new HashMap();

        if(request.getParameter("save") != null) {
        	messageAfterSave = elionsManager.savePbp(cmd, currentUser);
            m.put("s", 1);
        }else if(request.getParameter("transfer") != null) {
        	messageAfterSave = elionsManager.transferPbp(cmd, currentUser);
            m.put("s", 2);
        }
        
        m.put("m", messageAfterSave);
        m.put("reg_spaj", cmd.getPowersave().getReg_spaj());

        return new ModelAndView(new RedirectView(request.getContextPath() + "/uw/inputsblink.htm")).addAllObjects(m);
	}
	
}