package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.ReasTempNew;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.parent.ParentController;

public class SimultanControllerNew extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		String spaj=request.getParameter("spaj");
		List list=elionsManager.selectMReasTemp(spaj);
		Map reasTemp=(HashMap)list.get(0);
		Double reas[]=new Double[10];
		double total = 0;
		reas[0]=(Double) reasTemp.get("REAS_TR_RD");
		reas[1]=(Double) reasTemp.get("REAS_LIFE");
		reas[2]=(Double) reasTemp.get("REAS_SSP");
		reas[3]=(Double) reasTemp.get("REAS_PA_IN");
		reas[4]=(Double) reasTemp.get("REAS_PA_RD");
		reas[5]=(Double) reasTemp.get("REAS_PK_IN");
		reas[6]=(Double) reasTemp.get("REAS_PK_RD");
		reas[7]=(Double) reasTemp.get("REAS_SSH");
		reas[8]=(Double) reasTemp.get("REAS_CASH");
		reas[9]=(Double) reasTemp.get("REAS_TPD");
		List lsRiderExcNew=elionsManager.selectMReasTempNew(spaj);
		reasTemp.put("lsRiderExcNew", lsRiderExcNew);
		for(int i=0;i<reas.length;i++){
			if(reas[i]==null)
				reas[i]=new Double(0);
			total=total+reas[i].doubleValue();
		}
		for(int i=0;i<lsRiderExcNew.size();i++){
			ReasTempNew riderTemp=(ReasTempNew)lsRiderExcNew.get(i);
			total+=riderTemp.getReas();
		}
		
		map.put("sarTemp",elionsManager.selectMSarTemp2(spaj));
		map.put("reasTemp",reasTemp);
		map.put("total",new Double(total));
		
		//tambahan permintaan backup reas (facultative)
		try{
			String backupreas=request.getParameter("backupreas");
			if("true".equals(backupreas)){
				backupReasRequest(request);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		//==============================
		
		return new ModelAndView("uw/simultanpolis_new", map);
	}
	
	public void backupReasRequest(HttpServletRequest request) throws Exception {
			
	//		Dear,
	//
	//		Mhn bantuannya utk tambah tombol 'permintaan back up reas' 
	//		yg nantinya secara otomatis ada pengiriman email ke aktuaria (
	//				Fitriyana Maya, Noor Ayu, dan Taqbir Haryadi) dan cc ke user UW. 
	//				Email permohonan back up reas tersebut memuat : 1. Jenis Produk : MRI/Individu, 
	//				2. Nama P.Polis/ Tertanggung, 3. No SPAJ, 4. UP , 5. Simultan Polis, 6. Medis/ Non Medis, 7. EM
	//		Utk tombolnya mungkin dapat berupa quesion (terlampir), atau ada usul lain?
	//
	//		terima kasih
		
//		Utk pengiriman email permintaannya ke aktuaria ditujukan ke
//
//		Taqbir
//		Joko Berlianto
//		Ardibian Krismanti
//		Kamma PP
//		Fitriyana Maya D
//
//		Kemaren ak revisi utk pic di aktuaria (email terlampir).
//
//		Selain itu, emailnya juga cc ke user uw yg mengajukan back up reasnya ya...
//
//		Jadi kurang lebih pegiriman emailnya spt ini :
//
//		To : Taqbir ; Joko Berlianto ; Ardibian Krismanto ; Kamma PP ; Fitriyana Maya D
//		CC : user UW yg mengajukan (misal Titis)

			
	//		select 
	//		(select a.LSTB_NAME from eka.lst_type_business a where a.lstb_id=x.lstb_id) lini_bisnis,
	//		(select b.mcl_first from eka.mst_client_new b where b.mcl_id=x.mspo_policy_holder ) nama_pemegang,
	//		(select c.mcl_first from eka.mst_client_new c, eka.mst_insured d where d.reg_spaj=x.reg_spaj and 
	//		c.mcl_id=d.mste_insured ) nama_tertanggung,
	//		x.reg_spaj,
	//		y.lku_id,
	//		y.mspr_tsi,
	//		(select count(e.reg_spaj) from eka.mst_simultaneous e where e.reg_spaj=x.reg_spaj) simultan_polis,
	//		(select f.mste_medical from eka.mst_insured f where f.reg_spaj=x.reg_spaj) medis,
	//		y.mspr_extra
	//		from eka.mst_policy x, eka.mst_product_insured y where x.reg_spaj = #value#
	//		and x.reg_spaj=y.reg_spaj
			
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String spaj=request.getParameter("spaj");
			
			Map backupReas=uwManager.selectBackupReas(spaj);
			
			String lini_bisnis = (String) backupReas.get("LINI_BISNIS");
			String nama_pemegang = (String) backupReas.get("NAMA_PEMEGANG");
			String nama_tertanggung = (String) backupReas.get("NAMA_TERTANGGUNG");
			String reg_spaj = (String) backupReas.get("REG_SPAJ");
			String lku_id = (String) backupReas.get("LKU_ID");
			BigDecimal mspr_tsi = (BigDecimal) backupReas.get("MSPR_TSI");
			String simultan_polis = (String) backupReas.get("SIMULTAN_POLIS");
			BigDecimal medis = (BigDecimal) backupReas.get("MEDIS");
			BigDecimal mspr_extra = (BigDecimal) backupReas.get("MSPR_EXTRA");
			
			String up = "";
			String med = "";
			if(lku_id.equals("01")){
				up = "Rp. " + FormatNumber.convertToTwoDigit(mspr_tsi);
			}else if(lku_id.equals("02")){
				up = "$ " + FormatNumber.convertToTwoDigit(mspr_tsi);
			}else{
				up = FormatNumber.convertToTwoDigit(mspr_tsi);
			}
			if(medis.intValue() == 1){
				med = "Medis";
			}else{
				med = "Non Medis";
			}
			//1. Jenis Produk : MRI/Individu, 
	//		2. Nama P.Polis/ Tertanggung, 3. No SPAJ, 4. UP , 5. Simultan Polis, 6. Medis/ Non Medis, 7. EM
			// Hayu Puspita SNJ, Joko Berlianto, Fitriyana Maya D 
			//permintaan tities & joko : email ditujukan ke Joko Berlianto & Fitriyana Maya Damayanti
		email.send(
				false, props.getProperty("admin.ajsjava"), 
				//new String[]{"joko@sinarmasmsiglife.co.id","fitriyana@sinarmasmsiglife.co.id","ardibian@sinarmasmsiglife.co.id","taqbir@sinarmasmsiglife.co.id","pratikna@sinarmasmsiglife.co.id"},
				new String[]{"joko@sinarmasmsiglife.co.id","fitriyana@sinarmasmsiglife.co.id"},
				new String[]{currentUser.getEmail()}, 
				new String[]{"andy@sinarmasmsiglife.co.id"}, 
				"Permintaan Back Up Reas (SPAJ : " + spaj +" )", 
				"Jenis Produk              : " + lini_bisnis + "\n" +
				"Nama P.Polis/ Tertanggung : " + nama_pemegang +"/"+ nama_tertanggung + "\n" +
				"No SPAJ                   : " + spaj + "\n" +
				"UP                        : " + up + "\n" +
				"Simultan Polis            : " + simultan_polis + "\n" +
				"Medis/ Non Medis          : " + med + "\n" +
				"EM                        : " + mspr_extra + "\n"
				, null
				);
		
	}

}