package com.ekalife.elions.web.uw;
	/**
	 * @author Ryan F
	 * Ini merupakan Controller untuk meng-edit alamat rumah pemegang dan tertanggung
	 * 
	 */
	import java.util.ArrayList;
import java.util.Date;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;

	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import javax.servlet.http.HttpSession;

	import org.springframework.validation.BindException;
	import org.springframework.validation.Errors;
	import org.springframework.web.bind.ServletRequestDataBinder;
	import org.springframework.web.bind.ServletRequestUtils;
	import org.springframework.web.servlet.ModelAndView;
	import org.springframework.web.servlet.view.RedirectView;

	import com.ekalife.elions.model.AddressNew;
	import com.ekalife.elions.model.Benefeciary;
	import com.ekalife.elions.model.Datausulan;
	import com.ekalife.elions.model.Pemegang;
	import com.ekalife.elions.model.Tertanggung;
	import com.ekalife.elions.model.User;
	import com.ekalife.elions.model.ViewPolis;
	import com.ekalife.elions.service.ElionsManager;
	import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentFormController;

	public class AddressNewFormController extends ParentFormController {

		@Override
		protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
			binder.registerCustomEditor(Double.class, null, 	doubleEditor); 
			binder.registerCustomEditor(Integer.class, null, 	integerEditor); 
		}	

		@Override
		protected Map referenceData(HttpServletRequest request, Object cmd, Errors errors) throws Exception {
			User user = (User) request.getSession().getAttribute("currentUser");
			Map map = new HashMap();
			map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(user.getLus_id())));
			//String reg_spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
			//Pemegang pemegang = elionsManager.selectpp(reg_spaj);
			
			//map.put("pemegang", pemegang);
			return map;
		}
		
		@Override
		protected Object formBackingObject(HttpServletRequest request) throws Exception {
			Map map = new HashMap();
			HttpSession session = request.getSession();
			String reg_spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
			ViewPolis viewPolis = new ViewPolis();
			User currentUser=(User)request.getSession().getAttribute("currentUser");
			Tertanggung tertanggung = elionsManager.selectttg(reg_spaj);
			//Map currentUser = (Map)elionsManager.selectInfoDetailUser(Integer.valueOf(user.getLus_id()));
			Pemegang pemegang = elionsManager.selectpp(reg_spaj);
			viewPolis.setPemegang(pemegang);
			viewPolis.setTertanggung(tertanggung);
			viewPolis.setUser(currentUser);
		
			return viewPolis ;
		}
		
		@Override
		protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
			ViewPolis viewPolis=(ViewPolis)command;
			HashMap map = new HashMap();
			User currentUser = (User) request.getSession().getAttribute("currentUser");
	        String submitMode = ServletRequestUtils.getStringParameter(request, "submit","");
	        String alamatP= ServletRequestUtils.getStringParameter(request, "alamatPemegang","").toUpperCase();
	        String kotaP= ServletRequestUtils.getStringParameter(request, "kotaP","").toUpperCase();
	        String KdPosP= ServletRequestUtils.getStringParameter(request, "KdPosP","").toUpperCase();
	        String alasan= ServletRequestUtils.getStringParameter(request, "keterangan","").toUpperCase();
	        String spaj = viewPolis.getPemegang().getReg_spaj();
	        String aplikasi = alamatP+" , "+kotaP+" "+ KdPosP;
	        //Pemegang pemegang = elionsManager.selectpp(spaj);
	        List<String> error = new ArrayList<String>();
	        	if(alamatP.equals("")||kotaP.equals("")||KdPosP.equals("")||alasan.equals("")){
	        		error.add("Harap Isi Field Yang Kosong Terlebih Dahulu");
	        	}else{
	        		//String no =uwManager.noEndor(viewPolis.getPemegang().getLca_id());
	        		String no = uwManager.endorseNonMaterial(spaj, Integer.parseInt(currentUser.getLus_id()), alasan, aplikasi,3,currentUser,alamatP,kotaP,KdPosP,null);
					viewPolis.getPemegang().setAlamat_rumah(alamatP);
					viewPolis.getPemegang().setKota_rumah(kotaP);
					viewPolis.getPemegang().setKd_pos_rumah(KdPosP);
	        		error.add("Berhasil Disimpan Dengan No : " + no);
	        	}
	        		
	       if(!error.equals("")){
	        	map.put("pesanError", error);
	       }
	        	//uwManager.prosesEditAlamat(spaj,viewPolis,currentUser,request,errors);
	        map.put("reg_spaj", spaj);
	        map.put("pesan",error);
	        return new ModelAndView(new RedirectView(request.getContextPath()+"/uw/addressnew.htm")).addAllObjects(map);
		}
}
