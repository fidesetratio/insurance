/**
 * @author  : Ryan F
 * @created : May 03, 2011, 8:37:46 AM
 * 
 * Ini merupakan Controller buat Endorse Non Material Bancass II req Novie.
 */
package com.ekalife.elions.web.uw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.MstOfacScreeningResult;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;

public class HasilScreeningController extends ParentMultiController {
	
	// Halaman Depan dari Endorse (Tampilan Awal).
	/*public ModelAndView lihat (HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.debug("EditBacController : formBackingObject");
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String lus_id =null;
		String lde_id = currentUser.getLde_id();
		String view_spaj = ServletRequestUtils.getStringParameter(request, "view_spaj", "");
		
		String pilihan = ServletRequestUtils.getStringParameter(request, "pilihan", "0");
		
		Pemegang pemegangDetail = new Pemegang();
		Tertanggung TertanggungDetail = new Tertanggung();
		
		pemegangDetail = bacManager.selectHolder(view_spaj);
		
		TertanggungDetail = bacManager.selectTertanggungDetail(view_spaj);
		
		List<Benefeciary> benefeciaryDetail = new ArrayList<Benefeciary>();
		
		benefeciaryDetail = bacManager.selectBenefeciary(view_spaj);
		
		request.setAttribute("pemegangDetail", pemegangDetail);
		
		request.setAttribute("tertanggungDetail", TertanggungDetail);
		
		List<PemegangResult> pemegangResult = new ArrayList<PemegangResult>();
		
		pemegangResult = bacManager.selectPemegangResult(view_spaj);
		
		List pemegangResultList = new ArrayList();
		pemegangResultList = bacManager.selectPemegangResultList(view_spaj);
		
		List<TertanggungResult> tertanggungResult = new ArrayList<TertanggungResult>();
		
		tertanggungResult = bacManager.selectTertanggungResult(view_spaj);
		
		List<BenefeciaryResult> benefeciaryResult = new ArrayList<BenefeciaryResult>();
		
		benefeciaryResult = bacManager.selectBenefeciaryResult(view_spaj);
		
		List<PesertaPlus> PesertaDetail = new ArrayList<PesertaPlus>();
		
		PesertaDetail = bacManager.selectPeserta(view_spaj);
		
		List<PesertaResult> pesertaResult = new ArrayList<PesertaResult>();
		
		pesertaResult = bacManager.selectPesertaResult(view_spaj);
		
		request.setAttribute("benefeciaryResultSize", benefeciaryResult.size());
		request.setAttribute("benefeciaryDetail", benefeciaryDetail);
		request.setAttribute("pesertaResult", pesertaResult);
		request.setAttribute("pemegangResult", pemegangResult);
		request.setAttribute("benefeciaryResult", benefeciaryResult);
		request.setAttribute("tertanggungResult", tertanggungResult);
		request.setAttribute("spaj", view_spaj);
		request.setAttribute("pesertaDetail", PesertaDetail);
		
		request.setAttribute("pilihan", pilihan);	
		request.setAttribute("lde_id", lde_id);
		return new ModelAndView("uw/hasil_screening");
	}
*/	

	public ModelAndView lihat (HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.debug("EditBacController : formBackingObject");
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String lus_id =null;
		String lde_id = currentUser.getLde_id();
		String view_spaj = ServletRequestUtils.getStringParameter(request, "view_spaj", "");
		String pilihan = ServletRequestUtils.getStringParameter(request, "pilihan", "0");
		
		Integer type_data = bacManager.selectSertifikatYN(view_spaj);
		
		if(type_data==1){
				//Pemegang pemegang = elionsManager.selectpp(view_spaj);
				Pemegang pemegang = bacManager.selectHolder(view_spaj);
				//Tertanggung tertanggung = elionsManager.selectttg(view_spaj);
				Tertanggung tertanggung = bacManager.selectTertanggungDetail(view_spaj);
				//Datausulan dataUsulan = elionsManager.selectDataUsulanutama(view_spaj);
				//List peserta_tambahan = bacManager.select_semua_mst_peserta2(view_spaj);
				List peserta_tambahan = bacManager.selectPeserta(view_spaj);
				
				List<MstOfacScreeningResult> results = new ArrayList<MstOfacScreeningResult>();
				if(pemegang!=null){
					results = bacManager.selectMstoFacresultScreening(view_spaj, "PEMEGANG", pemegang.getMcl_first().trim().toUpperCase());
				}
				
				List<MstOfacScreeningResult> resultstt = new ArrayList<MstOfacScreeningResult>();
				if(tertanggung!=null){
					resultstt = bacManager.selectMstoFacresultScreening(view_spaj, "TERTANGGUNG", tertanggung.getMcl_first().trim().toUpperCase());
				}
				
				//List benef = dataUsulan.getDaftabenef();
				//List daftarplus = dataUsulan.getDaftaExtra();
				List benef = bacManager.selectBenefeciary(view_spaj);
			
				logger.info("resultsize:"+results.size());
				logger.info("benef:"+benef.size());
				
				Map tempBenef = new HashMap();
				if(benef != null){
				
					for(int i = 0;i<benef.size();i++){
						Benefeciary b = (Benefeciary)benef.get(i);
						String name = b.getMsaw_first().toUpperCase();
						List<MstOfacScreeningResult> ofacResults = new ArrayList<MstOfacScreeningResult>();
						ofacResults = bacManager.selectMstoFacresultScreening(view_spaj, "BENEFICIARY", name.trim().toUpperCase());
						tempBenef.put(name, ofacResults);
					}
				}
				
				Map tempPesertaTambahan = new HashMap();
				
				if(peserta_tambahan != null){
				/*for(int i = 0;i<peserta_tambahan.size();i++){
					Map p = (Map)peserta_tambahan.get(i);
					String name = (String)p.get("NAMA");
					List<MstOfacScreeningResult> ofacResults = new ArrayList<MstOfacScreeningResult>();
					ofacResults = bacManager.selectMstoFacresultScreening(view_spaj, "PESERTA", name.trim().toUpperCase());
					tempPesertaTambahan.put(name, ofacResults);
				}*/
					for(int i = 0;i<peserta_tambahan.size();i++){
						PesertaPlus p = (PesertaPlus)peserta_tambahan.get(i);
						String name = p.getNama().toUpperCase();
						List<MstOfacScreeningResult> ofacResults = new ArrayList<MstOfacScreeningResult>();
						ofacResults = bacManager.selectMstoFacresultScreening(view_spaj, "PESERTA", name.trim().toUpperCase());
						tempPesertaTambahan.put(name, ofacResults);
					}
				};
		
		
				Tertanggung TertanggungDetail = new Tertanggung();
				
				TertanggungDetail = bacManager.selectTertanggungDetail(view_spaj);
				
				request.setAttribute("pemegangDetail", pemegang);
				
				request.setAttribute("tertanggungDetail", tertanggung);
				
				request.setAttribute("pemegangOfacResults", results);
				request.setAttribute("tertanggungOfacResults", resultstt);
				request.setAttribute("benefResults", tempBenef);
				request.setAttribute("pesertaTambahanResults", tempPesertaTambahan);
				
		}else if(type_data==2){
			//Pemegang pemegang = elionsManager.selectpp(view_spaj);
			Pemegang pemegang = bacManager.selectHolderSertifikat(view_spaj);
			//Tertanggung tertanggung = elionsManager.selectttg(view_spaj);
			Tertanggung tertanggung = bacManager.selectTertanggungDetailSertifikat(view_spaj);
			//Datausulan dataUsulan = elionsManager.selectDataUsulanutama(view_spaj);
			//List peserta_tambahan = bacManager.select_semua_mst_peserta2(view_spaj);
			List peserta_tambahan = bacManager.selectPesertaSertifikat(view_spaj);
			
			List<MstOfacScreeningResult> results = new ArrayList<MstOfacScreeningResult>();
			if(pemegang!=null){
				results = bacManager.selectMstoFacresultScreening(view_spaj, "PEMEGANG", pemegang.getMcl_first().trim().toUpperCase());
			}
			
			List<MstOfacScreeningResult> resultstt = new ArrayList<MstOfacScreeningResult>();
			if(tertanggung!=null){
				resultstt = bacManager.selectMstoFacresultScreening(view_spaj, "TERTANGGUNG", tertanggung.getMcl_first().trim().toUpperCase());
			}
			
			//List benef = dataUsulan.getDaftabenef();
			//List daftarplus = dataUsulan.getDaftaExtra();
			List benef = bacManager.selectBenefeciarySertifikat(view_spaj);
		
			logger.info("resultsize:"+results.size());
			logger.info("benef:"+benef.size());
			
			Map tempBenef = new HashMap();
			if(benef != null){
			
				for(int i = 0;i<benef.size();i++){
					Benefeciary b = (Benefeciary)benef.get(i);
					String name = b.getMsaw_first().toUpperCase();
					List<MstOfacScreeningResult> ofacResults = new ArrayList<MstOfacScreeningResult>();
					ofacResults = bacManager.selectMstoFacresultScreening(view_spaj, "BENEFICIARY", name.trim().toUpperCase());
					tempBenef.put(name, ofacResults);
				}
			}
			
			Map tempPesertaTambahan = new HashMap();
			
			if(peserta_tambahan != null){
			/*for(int i = 0;i<peserta_tambahan.size();i++){
				Map p = (Map)peserta_tambahan.get(i);
				String name = (String)p.get("NAMA");
				List<MstOfacScreeningResult> ofacResults = new ArrayList<MstOfacScreeningResult>();
				ofacResults = bacManager.selectMstoFacresultScreening(view_spaj, "PESERTA", name.trim().toUpperCase());
				tempPesertaTambahan.put(name, ofacResults);
			}*/
				for(int i = 0;i<peserta_tambahan.size();i++){
					PesertaPlus p = (PesertaPlus)peserta_tambahan.get(i);
					String name = p.getNama().toUpperCase();
					List<MstOfacScreeningResult> ofacResults = new ArrayList<MstOfacScreeningResult>();
					ofacResults = bacManager.selectMstoFacresultScreening(view_spaj, "PESERTA", name.trim().toUpperCase());
					tempPesertaTambahan.put(name, ofacResults);
				}
			};
	
	
			Tertanggung TertanggungDetail = new Tertanggung();
			
			TertanggungDetail = bacManager.selectTertanggungDetail(view_spaj);
			
			request.setAttribute("pemegangDetail", pemegang);
			
			request.setAttribute("tertanggungDetail", tertanggung);
			
			request.setAttribute("pemegangOfacResults", results);
			request.setAttribute("tertanggungOfacResults", resultstt);
			request.setAttribute("benefResults", tempBenef);
			request.setAttribute("pesertaTambahanResults", tempPesertaTambahan);
		}				
		request.setAttribute("spaj", view_spaj);
		
		request.setAttribute("pilihan", pilihan);	
		request.setAttribute("lde_id", lde_id);
		return new ModelAndView("uw/hasil_screening");
	}

	public ModelAndView first (HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.debug("EditBacController : formBackingObject");
        HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String lus_id = null;
		String lde_id = currentUser.getLde_id();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		
		String pilihan = ServletRequestUtils.getStringParameter(request, "pilihan", "0");
		
		request.setAttribute("spaj", spaj);
			
		request.setAttribute("lde_id", lde_id);
		return new ModelAndView("uw/hasil_screening");
	}
}