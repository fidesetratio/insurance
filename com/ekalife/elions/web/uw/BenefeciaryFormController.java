package com.ekalife.elions.web.uw;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.ViewPolis;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.f_validasi;
import com.ekalife.utils.parent.ParentFormController;

public class BenefeciaryFormController extends ParentFormController{
	protected final Log logger = LogFactory.getLog( getClass() );

		@Override
		protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
			binder.registerCustomEditor(Double.class, null, doubleEditor); 
			binder.registerCustomEditor(Integer.class, null, integerEditor); 
			binder.registerCustomEditor(Date.class, null, dateEditor); 
		}
		
		@Override
		protected Map referenceData(HttpServletRequest request, Object comand, Errors err) throws Exception {
			User user = (User) request.getSession().getAttribute("currentUser");
			Map map = new HashMap();
			map.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
			return map;
		}
		
		@Override
		protected Object formBackingObject(HttpServletRequest request) throws Exception {
			ViewPolis viewPolis = new ViewPolis();
			viewPolis.setCurrentTimeMillis(System.currentTimeMillis());
			User user = (User) request.getSession().getAttribute("currentUser");
				String spaj = request.getParameter("reg_spaj");
				Integer lstbId = uwManager.getLstbId(spaj);
				if(lstbId == 1) {
					String businessID = uwManager.selectBusinessId(spaj);
					
					//Data utama yang ditampilkan
					Pemegang pemegang = elionsManager.selectpp(spaj);
					//tambahan untuk badan usaha
					Personal personal = new Personal();
					ContactPerson contactPerson = new ContactPerson();
					if(pemegang != null){
						if(pemegang.getMcl_jenis() == 1){
							//mst_company//mst_company_contact//mst_company_contact_address//mst_company_contact_family
							personal = elionsManager.selectProfilePerusahaan(pemegang.getMcl_id());
							contactPerson = elionsManager.selectpic(pemegang.getMcl_id());
							if(pemegang.getMkl_industri() != null){
								if(pemegang.getMkl_industri().equalsIgnoreCase("LAIN - LAIN") || pemegang.getMkl_industri().equalsIgnoreCase("LAINNYA")){
									//pemegang.setIndustria("");
								}else{
									pemegang.setIndustria("");
								}
							}
						}
					}
					Tertanggung tertanggung = elionsManager.selectttg(spaj);
					Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
					if(dataUsulan == null) dataUsulan = new Datausulan();
					dataUsulan.setWorksite(elionsManager.selectPerusahaanWorksite(spaj));
					viewPolis.setPersonal(personal);
					viewPolis.setContactPerson(contactPerson);
					viewPolis.setPolicyNo(elionsManager.selectPolicyNumberFromSpaj(spaj));
					viewPolis.setPemegang(pemegang);
					viewPolis.setTertanggung(tertanggung);
					viewPolis.setDataUsulan(dataUsulan);
					viewPolis.setUser(user);
					viewPolis.setAddressbilling(elionsManager.selectAddressBilling(spaj));
					viewPolis.setInsured(elionsManager.selectInsuredNumber(spaj));
					//map.put("productInsured", elionsManager.selectViewerInsured(spaj, new Integer(1)));
					Map map=uwManager.selectInfoAgen2(spaj);
					viewPolis.setAgen(map);
					Map map1=uwManager.selectReferalInput(spaj);
					viewPolis.setReferal(map1);
					viewPolis.setBlangko((String)map.get("MSPO_NO_BLANKO"));
					viewPolis.setTempBlangko((String)map.get("MSPO_NO_BLANKO"));
					viewPolis.setTglSpaj(defaultDateFormat.format((Date)map.get("MSPO_SPAJ_DATE")));
					viewPolis.setTempTglSpaj(defaultDateFormat.format((Date)map.get("MSPO_SPAJ_DATE")));
					viewPolis.setBilling(elionsManager.selectViewerBilling(spaj));
					
					int lsbs = Integer.valueOf(businessID);
					int lsdbs = dataUsulan.getLsdbs_number();
					
					//Yusuf (1/5/08) untuk produk stable link

			logger.info(viewPolis.getCurrentTimeMillis());
			logger.info(System.currentTimeMillis());
			double selisih =  new Double((System.currentTimeMillis() - viewPolis.getCurrentTimeMillis())) / 1000.;
			logger.info(selisih);
			request.setAttribute("selisih", selisih);
			
	}
			return viewPolis;
}
	
	
		protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
			User currentUser=(User)request.getSession().getAttribute("currentUser");
			f_validasi data =new f_validasi();
			ViewPolis viewPolis = (ViewPolis) cmd;
			String spaj = request.getParameter("reg_spaj");
			List lsBeneficiary=viewPolis.getDataUsulan().getDaftabenef();
			Datausulan dataUsulan=viewPolis.getDataUsulan();
			int jmlh_penerima = Integer.parseInt(request.getParameter("jmlpenerima"));
			Integer no=new Integer(1);
			//
			if(jmlh_penerima==0){///add beneficiary
				String relasi[]=new String[lsBeneficiary.size()];
				relasi=request.getParameterValues("relasi");
				for(int i=0;i<lsBeneficiary.size();i++){
					Benefeciary upBene=(Benefeciary)lsBeneficiary.get(i);
					upBene.setLsre_id(Integer.valueOf(relasi[i]));
					lsBeneficiary.set(i,upBene);
				}
				
				Benefeciary insBene=new Benefeciary();
				if(! lsBeneficiary.isEmpty()){
					no=new Integer(lsBeneficiary.size()+1);
				}
				insBene.setMsaw_number(no);
				insBene.setLsre_id(new Integer(0));
//				insBene.setMsaw_birth(cal.getTime());
				insBene.setSmsaw_birth("01/01/1900");
				insBene.setMste_insured_no(new Integer(1));
				insBene.setMsaw_persen(new Double(0));
				insBene.setReg_spaj(spaj);
				insBene.setMsaw_sex(1);
				lsBeneficiary.add(insBene);
				err.reject("","Telah Dilakuakan Penambahan Penerima Manfaat");
			/*}else if(flag.equals("2")){//delete benefeciary
				if(lsBeneficiary.isEmpty())
					err.reject("","Tidak Berhasil di hapus, Data Penerima Manfaat Tidak ada");
				else{
					lsBeneficiary.remove(lsBeneficiary.size()-1);
					err.reject("","Telah Dilakukan Penghapusan Penerima Manfaat");
				}*/
			
			dataUsulan.setDaftabenef(lsBeneficiary);
			viewPolis.setDataUsulan(dataUsulan);}
		
	}
		
		@Override
		protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException err) throws Exception {
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			Map map = new HashMap();
			ViewPolis viewPolis=(ViewPolis)command;
			String spaj = request.getParameter("reg_spaj");
			//ajaxManager.prosesBenef(viewPolis.getPemegang().getReg_spaj(), viewPolis, currentUser, request, err,viewPolis.getPemegang().getLssp_id());
			map.put("reg_spaj", viewPolis.getPemegang().getReg_spaj());
			map.put("pesan", "Data Berhasil Diupdate");
			return new ModelAndView(new RedirectView(request.getContextPath()+"/uw/benefeciary.htm")).addAllObjects(map);
		}

}
