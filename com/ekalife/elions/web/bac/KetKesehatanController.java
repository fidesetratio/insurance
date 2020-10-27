package com.ekalife.elions.web.bac;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Kesehatan;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author HEMILDA
 * Controller ketkesehatan khusus produk platinum save
 */
public class KetKesehatanController extends ParentFormController{
	
	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}	
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception 
	{
		Kesehatan detiledit = new Kesehatan();
		String spaj = request.getParameter("spaj");
		/**
		 * @author HEMILDA
		 * spaj nya sudah pernah diinput baru bisa insert keterangan kesehatan tambahan 
		 */
		if(spaj!=null)
		{
			detiledit = (Kesehatan)this.elionsManager.selectkesehatan(spaj);
			if (detiledit == null)
			{
				detiledit = new Kesehatan();
				detiledit.setMsnm_keterangan("");
			}else{
				detiledit.setMsnm_keterangan("");
				if (detiledit.getMsnm_turun_naik() == null)
				{
					detiledit.setKeadaan("0");
				}else{
					if (detiledit.getMsnm_turun_naik().intValue() == 1)
					{
						detiledit.setKeadaan("1");
					}else{
						if (detiledit.getMsnm_turun_naik().intValue()==2)
						{
							detiledit.setKeadaan("2");
						}
					}
				}
			}
			detiledit.setReg_spaj(spaj);
		}
		return detiledit;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception 
	{
		
		Kesehatan detiledit = (Kesehatan) cmd;
		User currentUser = (User)request.getSession().getAttribute("currentUser");
		String spaj = request.getParameter("spaj");
		String keterangan="";
		if(spaj!=null)
		{
			detiledit = (Kesehatan)this.elionsManager.selectkesehatan(spaj);
			if (detiledit == null)
			{
				keterangan="input";
			}else{
				keterangan="edit";
			}
		}
		
		Kesehatan edit = (Kesehatan) cmd;
		edit.setMsnm_keterangan(keterangan);
		edit.setReg_spaj(spaj);
		edit = this.elionsManager.savingkesehatan(cmd, err, "", currentUser);
		
		return new ModelAndView("bac/keterangan_kesehatan","cmd", edit).addAllObjects(this.referenceData(request,cmd,err));
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception{
		return new ModelAndView("common/duplicate");
	}	

}
