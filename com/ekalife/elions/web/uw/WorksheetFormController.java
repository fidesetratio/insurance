
package com.ekalife.elions.web.uw;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.TopUp;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.worksheet.UwAbdomen;
import com.ekalife.elions.model.worksheet.UwAda;
import com.ekalife.elions.model.worksheet.UwDadaPa;
import com.ekalife.elions.model.worksheet.UwDecision;
import com.ekalife.elions.model.worksheet.UwDecisionRider;
import com.ekalife.elions.model.worksheet.UwEkg;
import com.ekalife.elions.model.worksheet.UwHiv;
import com.ekalife.elions.model.worksheet.UwLpk;
import com.ekalife.elions.model.worksheet.UwMedisLain;
import com.ekalife.elions.model.worksheet.UwQuestionnaire;
import com.ekalife.elions.model.worksheet.UwRiwPenyakit;
import com.ekalife.elions.model.worksheet.UwTreadmill;
import com.ekalife.elions.model.worksheet.UwTumor;
import com.ekalife.elions.model.worksheet.UwUrin;
import com.ekalife.elions.model.worksheet.UwWorkSheet;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentFormController;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;

public class WorksheetFormController extends ParentFormController {
	
	public static String medis_LPK = "LPK";//MEDICAL
	public static String medis_LPK2 = "LPK2";//2 X Medical
	public static String medis_URIN = "URIN";//CMU
	public static String medis_ADA = "ADA";//BDTS 2 or 3//Analisa Darah Rutin 2 (Pria) atau 3 (Wanita)
	public static String medis_ADAL = "ADAL";//BDTS 1//Analisa Darah Lengkap
	public static String medis_HIV = "HIV";
	public static String medis_TUMOR = "TUMOR";//CEA//AFP test//AFP ( Alfa Feto Protein)
	public static String medis_ABDOMEN = "ABDOMEN";
	public static String medis_DADAPA = "DADAPA";//Rontgen Dada (Chest X-Ray)//CXR//Rontgen Thorax
	public static String medis_EKG = "EKG";//ECG
	public static String medis_TREADMILL = "TREADMILL";
	public static String medis_MEDLAIN = "MEDLAIN";//APS (hasil check up ke dokter/hasil lab)
	
	
	public static Map<String, DropDown> map_medical_2014 = new HashMap<String, DropDown>();
	static{
		map_medical_2014.put("A", new DropDown("[A] Medical", "A"));
		map_medical_2014.put("A+", new DropDown("[A+] Medical + APS", "A+"));
		map_medical_2014.put("B", new DropDown("[B] Medical + CMU", "B"));
		map_medical_2014.put("C", new DropDown("[C] Medical + CMU + ECG", "C"));
		map_medical_2014.put("D", new DropDown("[D] Medical + CMU + BDTS 1", "D"));
		map_medical_2014.put("E", new DropDown("[E] Medical + CMU + ECG + BDTS 1", "E"));
		map_medical_2014.put("F", new DropDown("[F] Medical + CMU + TREADMILL + BDTS 1", "F"));
		map_medical_2014.put("G", new DropDown("[G] 2 X Medical + CMU + CXR + TREADMILL + BDTS 1 + APS", "G"));
		map_medical_2014.put("H", new DropDown("[H] Medical + CMU + TREADMILL + BDTS1 + CXR + BDTS 2 or 3", "H"));
		map_medical_2014.put("A2", new DropDown("[A2] 2 X Medical + APS", "A2"));
		map_medical_2014.put("E2", new DropDown("[E2] 2 X Medical + CMU + ECG + BDTS 1 + APS", "E2"));
		map_medical_2014.put("F2", new DropDown("[F2] 2 X Medical + TREADMILL + ECG + BDTS 1 + APS", "F2"));
		map_medical_2014.put("H2", new DropDown("[H2] 2 X Medical + TREADMILL + ECG + BDTS 1 + CXR + BDTS 2 or 3 + APS", "H2"));
	}
	
	public static Map<String, DropDown> map_medical_2008 = new HashMap<String, DropDown>();
	static{
		map_medical_2008.put("A", new DropDown("[A] LPK dari Dokter Spesialis Anak", "A"));
		map_medical_2008.put("B", new DropDown("[B] LPK dari Dokter Spesialis Anak + Urin lengkap", "B"));
		map_medical_2008.put("C", new DropDown("[C] LPK + Urin lengkap + ADA ( Darah Rutin ) + EKG", "C"));
		map_medical_2008.put("D", new DropDown("[D] LPK + Urin lengkap + ADAL + EKG", "D"));
		map_medical_2008.put("E", new DropDown("[E] LPK + Urin lengkap + ADAL + Rontgen Dada (Chest X-Ray) + EKG", "E"));
		map_medical_2008.put("F", new DropDown("[F] LPK + Urin lengkap + ADAL + Rontgen Dada (Chest X-Ray) + Treadmill test", "F"));
		map_medical_2008.put("G", new DropDown("[G] LPK + Urin lengkap + ADAL + Anti HIV test + CEA + Rontgen Dada (Chest X-Ray) + Treadmill test", "G"));
		map_medical_2008.put("H", new DropDown("[H] LPK 2 dokter yang berbeda + Urin lengkap + ADAL + Anti HIV test + CEA + Rontgen Dada (Chest X-Ray) + Treadmill test", "H"));
	}
	
	/*
	protected Connection connection = null;
	protected Connection getConnection() {
		if(this.connection==null) {
			try {
				this.connection = this.elionsManager.getUwDao().getDataSource().getConnection();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		}
		return this.connection;
	}
	*/
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		//Yusuf (11/1/12) - bila dari EAS, maka akan bypass login, sehingga currentUser tidak ada isinya, default ke "Underwriting" saja deptnya
		String lde_id = currentUser != null ? currentUser.getLde_id() : "11";
		
		UwWorkSheet ws = (UwWorkSheet) cmd;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		f_hit_umur hitUmur = new f_hit_umur();
		String sysDate = df.format(elionsManager.selectSysdate());
		
		Map<String, List> map = new HashMap<String, List>();
		List<DropDown> pptt = new ArrayList<DropDown>();
		pptt.add(new DropDown("Pemegang","2"));
		pptt.add(new DropDown("Tertanggung","3"));
		List<DropDown> satChol = new ArrayList<DropDown>();
		satChol.add(new DropDown("mg/dl","1"));
		satChol.add(new DropDown("mmol/l","2"));
		List<DropDown> lsSex = new ArrayList<DropDown>();
		lsSex.add(new DropDown("Wanita","0"));
		lsSex.add(new DropDown("Pria","1"));
		lsSex.add(new DropDown("Anak-anak","2"));
		
		List<HashMap> dataPeserta = uwManager.selectDaftarPeserta(ws.getReg_spaj());
		int a = 0;
		String mcl_id = "";
		for (HashMap map2 : dataPeserta) {
			if(Integer.parseInt(map2.get("INSURED_NO").toString()) == 1) mcl_id = map2.get("MCL_ID").toString();
			else if(map2.get("MCL_ID") == null) map2.put("MCL_ID", mcl_id);
			String birthDate = df.format(map2.get("MSPE_DATE_BIRTH"));
			map2.put("UMUR", hitUmur.umur(Integer.parseInt(birthDate.substring(6)),Integer.parseInt(birthDate.substring(3,5)) , Integer.parseInt(birthDate.substring(0,2)), Integer.parseInt(sysDate.substring(6)),Integer.parseInt(sysDate.substring(3,5)) , Integer.parseInt(sysDate.substring(0,2))));
			map2.put("MSPE_DATE_BIRTH", birthDate);
			dataPeserta.set(a++, map2);
		}
		
		String reg_spaj = ws.getReg_spaj();
		
		Integer lstb_id = uwManager.getLstbId(reg_spaj);
		
		List<DropDown> jenisMedis = new ArrayList<DropDown>();
		if(lde_id.equals("11")) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			List lRkCek= elionsManager.cekTglRk(reg_spaj);
			Date mspa_date_book = null;
			if(lRkCek.size() > 0){mspa_date_book = ((TopUp)lRkCek.get(0)).getMspa_date_book();}
			Date period_date_medis_2014 = sdf.parse("01/03/2014");
			
			if(lstb_id == 2){//MRI - (dipakai sama dr.selina)
				jenisMedis.add(map_medical_2008.get("A"));
				jenisMedis.add(map_medical_2008.get("B"));
				jenisMedis.add(map_medical_2008.get("C"));
				jenisMedis.add(map_medical_2008.get("D"));
				jenisMedis.add(map_medical_2008.get("E"));
				jenisMedis.add(map_medical_2008.get("F"));
				jenisMedis.add(map_medical_2008.get("G"));
				jenisMedis.add(map_medical_2008.get("H"));
			}else{//LAINNYA KE INDIVIDU - (dipakai sama novie & chizni)
				if(mspa_date_book != null && mspa_date_book.before(period_date_medis_2014)){
					jenisMedis.add(map_medical_2008.get("A"));
					jenisMedis.add(map_medical_2008.get("B"));
					jenisMedis.add(map_medical_2008.get("C"));
					jenisMedis.add(map_medical_2008.get("D"));
					jenisMedis.add(map_medical_2008.get("E"));
					jenisMedis.add(map_medical_2008.get("F"));
					jenisMedis.add(map_medical_2008.get("G"));
					jenisMedis.add(map_medical_2008.get("H"));
				}else{
					jenisMedis.add(map_medical_2014.get("A"));
					jenisMedis.add(map_medical_2014.get("A+"));
					jenisMedis.add(map_medical_2014.get("B"));
					jenisMedis.add(map_medical_2014.get("C"));
					jenisMedis.add(map_medical_2014.get("D"));
					jenisMedis.add(map_medical_2014.get("E"));
					jenisMedis.add(map_medical_2014.get("F"));
					jenisMedis.add(map_medical_2014.get("G"));
					jenisMedis.add(map_medical_2014.get("H"));
					jenisMedis.add(map_medical_2014.get("A2"));
					jenisMedis.add(map_medical_2014.get("E2"));
					jenisMedis.add(map_medical_2014.get("F2"));
					jenisMedis.add(map_medical_2014.get("H2"));
				}
			}
			
		}
		else if(lde_id.equals("07")) {
			jenisMedis.add(new DropDown("[A1] LPK + EKG + Urin lengkap +  ADAL","A1"));
			jenisMedis.add(new DropDown("[A2] LPK + EKG + Urine + ADAL","A2"));
			jenisMedis.add(new DropDown("[B1] LPK + Urin lengkap + ADAL + Treadmill test","B1"));
			jenisMedis.add(new DropDown("[B2] LPK + Urin lengkap + EKG","B2"));
			jenisMedis.add(new DropDown("[B3] LPK + Urine + ADAL + Treadmill","B3"));
			jenisMedis.add(new DropDown("[C1] LPK + Urin lengkap + Rontgen Dada (Chest X-Ray) + ADAL + Treadmill test + Anti HIV test","C1"));
			jenisMedis.add(new DropDown("[C2] LPK + Urin lengkap +  EKG + ADAL","C2"));
			jenisMedis.add(new DropDown("[C3] LPK + Urine + X-Ray + ADAL + Treadmill + HIV Test","C3"));
			jenisMedis.add(new DropDown("[C4] LPK + Urin lengkap  + ADA ( Darah Rutin ) + EKG","C4"));
			jenisMedis.add(new DropDown("[D1] LPK + Urin lengkap + Rontgen Dada (Chest X-Ray) + ADAL + Treadmill test + Anti HIV test +  AFP ( Alfa Feto Protein)","D1"));
			jenisMedis.add(new DropDown("[D2] LPK + Urin lengkap + ADAL + Treadmill test","D2"));
			jenisMedis.add(new DropDown("[D3] LPK + Urine + X-Ray + ADAL + Treadmill + HIV Test + AFP Test","D3"));
			jenisMedis.add(new DropDown("[D4] LPK + Urin lengkap + ADAL + EKG","D4"));
			jenisMedis.add(new DropDown("[E1] LPK + Urin lengkap + Rontgen Dada (Chest X-Ray) + ADAL + Treadmill test + Anti HIV test   + AFP ( Alfa Feto Protein) test + USG Abdomen + Ca 15-3 test (untuk Wanita) atau PSA Test (untuk Pria)","E1"));
			jenisMedis.add(new DropDown("[E2] LPK + Urin lengkap + Rontgen Dada (Chest X-Ray) + ADAL + Treadmill test + Anti HIV test","E2"));
			jenisMedis.add(new DropDown("[E3] LPK + Urine + X-Ray + ADAL + Treadmill + HIV Test + AFP Test + USG Abdomen + Ca 15-3 test (untuk Wanita) atau PSA test (untuk Pria)","E3"));
			jenisMedis.add(new DropDown("[E4] LPK + Urin lengkap + ADAL + EKG + Rontgen Dada (Chest X-Ray)","E4"));
			jenisMedis.add(new DropDown("[F2] LPK 2 dokter yang berbeda + Urin lengkap + Rontgen Dada (Chest X-Ray) + ADAL + Treadmill test + Anti HIV test","F2"));
			jenisMedis.add(new DropDown("[F3] LPK + Urin lengkap + ADAL + Rontgen Dada (Chest X-Ray) + Treadmill test","F3"));
			jenisMedis.add(new DropDown("[G2] LPK + Urin lengkap + ADAL + Rontgen Dada (Chest X-Ray) + Treadmill test +  Anti HIV test + CEA","G2"));
			jenisMedis.add(new DropDown("[H2] LPK 2 dokter yang berbeda + Urin lengkap + ADAL + Rontgen Dada (Chest X-Ray) + Treadmill test + Anti HIV test + CEA","H2"));
		}
		else if(lde_id.equals("01")) {
			jenisMedis.add(new DropDown("[A] LPK dari Dokter Spesialis Anak","A"));
			jenisMedis.add(new DropDown("[A1] LPK + EKG + Urin lengkap +  ADAL","A1"));
			jenisMedis.add(new DropDown("[A2] LPK + EKG + Urine + ADAL","A2"));
			jenisMedis.add(new DropDown("[B] LPK dari Dokter Spesialis Anak + Urin lengkap","B"));
			jenisMedis.add(new DropDown("[B1] LPK + Urin lengkap + ADAL + Treadmill test","B1"));
			jenisMedis.add(new DropDown("[B2] LPK + Urin lengkap + EKG","B2"));
			jenisMedis.add(new DropDown("[B3] LPK + Urine + ADAL + Treadmill","B3"));
			jenisMedis.add(new DropDown("[C] LPK + Urin lengkap + ADA ( Darah Rutin ) + EKG","C"));
			jenisMedis.add(new DropDown("[C1] LPK + Urin lengkap + Rontgen Dada (Chest X-Ray) + ADAL + Treadmill test + Anti HIV test","C1"));
			jenisMedis.add(new DropDown("[C2] LPK + Urin lengkap +  EKG + ADAL","C2"));
			jenisMedis.add(new DropDown("[C3] LPK + Urine + X-Ray + ADAL + Treadmill + HIV Test","C3"));
			jenisMedis.add(new DropDown("[C4] LPK + Urin lengkap  + ADA ( Darah Rutin ) + EKG","C4"));
			jenisMedis.add(new DropDown("[D] LPK + Urin lengkap + ADAL + EKG","D"));
			jenisMedis.add(new DropDown("[D1] LPK + Urin lengkap + Rontgen Dada (Chest X-Ray) + ADAL + Treadmill test + Anti HIV test +  AFP ( Alfa Feto Protein)","D1"));
			jenisMedis.add(new DropDown("[D2] LPK + Urin lengkap + ADAL + Treadmill test","D2"));
			jenisMedis.add(new DropDown("[D3] LPK + Urine + X-Ray + ADAL + Treadmill + HIV Test + AFP Test","D3"));
			jenisMedis.add(new DropDown("[D4] LPK + Urin lengkap + ADAL + EKG","D4"));
			jenisMedis.add(new DropDown("[E] LPK + Urin lengkap + ADAL + Rontgen Dada (Chest X-Ray) + EKG","E"));
			jenisMedis.add(new DropDown("[E1] LPK + Urin lengkap + Rontgen Dada (Chest X-Ray) + ADAL + Treadmill test + Anti HIV test   + AFP ( Alfa Feto Protein) test + USG Abdomen + Ca 15-3 test (untuk Wanita) atau PSA Test (untuk Pria)","E1"));
			jenisMedis.add(new DropDown("[E2] LPK + Urin lengkap + Rontgen Dada (Chest X-Ray) + ADAL + Treadmill test + Anti HIV test","E2"));
			jenisMedis.add(new DropDown("[E3] LPK + Urine + X-Ray + ADAL + Treadmill + HIV Test + AFP Test + USG Abdomen + Ca 15-3 test (untuk Wanita) atau PSA test (untuk Pria)","E3"));
			jenisMedis.add(new DropDown("[E4] LPK + Urin lengkap + ADAL + EKG + Rontgen Dada (Chest X-Ray)","E4"));
			jenisMedis.add(new DropDown("[F] LPK + Urin lengkap + ADAL + Rontgen Dada (Chest X-Ray) + Treadmill test","F"));
			jenisMedis.add(new DropDown("[F2] LPK 2 dokter yang berbeda + Urin lengkap + Rontgen Dada (Chest X-Ray) + ADAL + Treadmill test + Anti HIV test","F2"));
			jenisMedis.add(new DropDown("[F3] LPK + Urin lengkap + ADAL + Rontgen Dada (Chest X-Ray) + Treadmill test","F3"));
			jenisMedis.add(new DropDown("[G] LPK + Urin lengkap + ADAL + Anti HIV test + CEA + Rontgen Dada (Chest X-Ray) + Treadmill test","G"));
			jenisMedis.add(new DropDown("[G2] LPK + Urin lengkap + ADAL + Rontgen Dada (Chest X-Ray) + Treadmill test +  Anti HIV test + CEA","G2"));
			jenisMedis.add(new DropDown("[H] LPK 2 dokter yang berbeda + Urin lengkap + ADAL + Anti HIV test + CEA + Rontgen Dada (Chest X-Ray) + Treadmill test","H"));
			jenisMedis.add(new DropDown("[H2] LPK 2 dokter yang berbeda + Urin lengkap + ADAL + Rontgen Dada (Chest X-Ray) + Treadmill test + Anti HIV test + CEA","H2"));
		}

		
		map.put("lsPeserta", dataPeserta);
		map.put("lsPptt", pptt);
		map.put("lsSex",lsSex);
		map.put("lsOccupation", uwManager.selectLstWorksheet(2,null));
		map.put("lsHobby", uwManager.selectLstWorksheet(3,null));
		map.put("lsQuest", uwManager.selectLstWorksheet(4,null));
		map.put("lsPlan", sortingPlanUtama(sortingPlan(uwManager.selectAkumulasiPolisBySpaj(ws.getReg_spaj()))));
		map.put("lsSatChol", satChol);
		map.put("lsJnsMds", jenisMedis);
		
		return map;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		UwWorkSheet ws = new UwWorkSheet();
		f_hit_umur hitUmur = new f_hit_umur();
		Boolean isWorksheet = true;
		String sysDate = df.format(elionsManager.selectSysdate());
		ws.setInsured_no(ServletRequestUtils.getIntParameter(request, "no",-1));
		ws.setReg_spaj(ServletRequestUtils.getStringParameter(request, "spaj",""));
		ws.setSuccessMsg(ServletRequestUtils.getStringParameter(request, "success"));
		ws.setIsViewer(ServletRequestUtils.getIntParameter(request, "view",0));
		
		//logic copy data dari spaj lain
		
		for(int a=0;a<3;a++) { ws.getListQuest().add(new UwQuestionnaire()); }
		
		List<SortedMap> jumlProd = sortingPlanUtama(sortingPlan(uwManager.selectAkumulasiPolisBySpaj(ws.getReg_spaj())));
		UwDecision ud = new UwDecision(sysDate);
		ud.setRiderContent(jumlProd);
		ud.setUrutan_penyakit(1);
		ws.getListUwDec().add(ud);
		ud = new UwDecision(sysDate);
		ud.setRiderContent(jumlProd);
		ud.setUrutan_decision(0);
		ws.getListUwDec().add(ud);
		ud = new UwDecision(sysDate);
		ud.setRiderContent(jumlProd);
		ud.setUrutan_decision(1);
		ws.getListUwDec().add(ud);
		ws.setTotalPenyakitUwDesc(1);
		
		Map<String, Object> newPaket = new HashMap<String, Object>();
		newPaket.put(medis_LPK, true);
		newPaket.put(medis_URIN, true);
		newPaket.put(medis_ADA, true);
		newPaket.put(medis_HIV, true);
		newPaket.put(medis_TUMOR, true);
		newPaket.put(medis_ABDOMEN, true);
		newPaket.put(medis_DADAPA, true);
		newPaket.put(medis_EKG, true);
		newPaket.put(medis_TREADMILL, true);
		newPaket.put(medis_MEDLAIN, true);
		
		ws = addMedis(ws, 0, true, newPaket);
		
		// baru masuk halaman
		if(ws.getInsured_no() == -1) {
			UwWorkSheet isWorksheetExist = uwManager.getUwWorksheet(ws.getReg_spaj(),null);
			isWorksheetExist = uwManager.getUwWorksheet(ws.getReg_spaj(),null);
			if(isWorksheetExist != null) {
				ws.setInsured_no(isWorksheetExist.getInsured_no());
			}
			else {
				isWorksheet = false;
				ws.setInsured_no(1);
				if(ws.getInsured_no() == 0) ws.setPptt(2);
				else ws.setPptt(3);
				List<HashMap> dataPeserta = uwManager.selectDaftarPeserta(ws.getReg_spaj());
				int a = 0;
				String mcl_id = "";
				for (HashMap map2 : dataPeserta) {
					if(Integer.parseInt(map2.get("INSURED_NO").toString()) == 1) mcl_id = map2.get("MCL_ID").toString();
					else if(map2.get("MCL_ID") == null) map2.put("MCL_ID", mcl_id);
					String birthDate = df.format(map2.get("MSPE_DATE_BIRTH"));
					if(Integer.parseInt(map2.get("INSURED_NO").toString()) == ws.getInsured_no()) {
						ws.setMsw_sex(Integer.parseInt(map2.get("MSPE_SEX").toString()));
						ws.setBirthDate(birthDate);
						ws.setUmur(hitUmur.umur(Integer.parseInt(birthDate.substring(6)),Integer.parseInt(birthDate.substring(3,5)) , Integer.parseInt(birthDate.substring(0,2)), Integer.parseInt(sysDate.substring(6)),Integer.parseInt(sysDate.substring(3,5)) , Integer.parseInt(sysDate.substring(0,2))));
						ws.setNamaPeserta(map2.get("MCL_FIRST").toString());
						ws.setMcl_id(map2.get("MCL_ID").toString());
						break;
					}
				}
				
				if(ws.getIsViewer() == 1) {
					ws.setOkSave(0);
					ws.setOkPrint(0);
					ws.setOkReverse1(0);
					ws.setOkReverse2(0);
					ws.setOkTransfer1(0);
					ws.setOkTransfer2(0);
				}
			}
		}
		
		if(isWorksheet) {
			//ws = inputAllDataWorksheet(ws, reg_spaj, ws.getInsured_no(), df,hitUmur,sysDate,jumlProd);
			ws = inputAllDataWorksheet(ws, df,hitUmur,sysDate,jumlProd);
			configPosition(ws,currentUser);
			chekAllWarning(ws);
			
			// hitung Penambahan rating SGOT, SGPT, GGT di medis adal
			for(int a=0;a<ws.getListAda().size();a++) {
				if(ws.getListAda().get(a).getSgot() != null && ws.getListAda().get(a).getSgpt() != null && ws.getListAda().get(a).getGgt() != null) {
					if(!ws.getListAda().get(a).getSgot().isEmpty() && !ws.getListAda().get(a).getSgpt().isEmpty() && !ws.getListAda().get(a).getGgt().isEmpty()) {
						BigDecimal sgot = new BigDecimal(ws.getListAda().get(a).getSgot());
						BigDecimal sgpt = new BigDecimal(ws.getListAda().get(a).getSgpt());
						BigDecimal ggt = new BigDecimal(ws.getListAda().get(a).getGgt());
						
						String sgotMaxStr = ws.getListAda().get(a).getNv_fungsi_hati_sgot_ast();
						String sgptMaxStr = ws.getListAda().get(a).getNv_fungsi_hati_sgpt_alt();
						String ggtMaxStr = ws.getListAda().get(a).getNv_fungsi_hati_gamma_gt_ggtp();
						
						BigDecimal sgotMax = new BigDecimal(0);
						BigDecimal sgptMax = new BigDecimal(0);
						BigDecimal ggtMax = new BigDecimal(0);
						String x = sgotMaxStr.replace(sgotMaxStr.replace("<", "").replace("=", "").replace("-", "").replace("U/L", ""),"");
						if(sgotMaxStr.indexOf("<") != -1){
							sgotMax = new BigDecimal(sgotMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", "")).subtract(new BigDecimal(1));
						}else if(sgotMaxStr.indexOf("<=") != -1){
							sgotMax = new BigDecimal(sgotMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", ""));
						}else{
							sgotMax = new BigDecimal(sgotMaxStr.substring(sgotMaxStr.indexOf("-")).replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", ""));
						}
						
						if(sgptMaxStr.indexOf("<") != -1){
							sgptMax = new BigDecimal(sgptMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", "")).subtract(new BigDecimal(1));
						}else if(sgptMaxStr.indexOf("<=") != -1){
							sgptMax = new BigDecimal(sgptMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", "")).subtract(new BigDecimal(1));
						}else{
							sgptMax = new BigDecimal(sgptMaxStr.substring(sgptMaxStr.indexOf("-")).replace("<", "").replace("-", "").replace("=", "").replace("U/L", "").replace(" ", ""));
						}
						
						if(ggtMaxStr.indexOf("<") != -1){
							ggtMax = new BigDecimal(ggtMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", "")).subtract(new BigDecimal(1));
						}else if(ggtMaxStr.indexOf("<=") != -1){
							ggtMax = new BigDecimal(ggtMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", ""));
						}else{
							ggtMax = new BigDecimal(ggtMaxStr.substring(ggtMaxStr.indexOf("-")).replace("<", "").replace("-", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", ""));
						}
						
						BigDecimal SGOTx = sgot.divide(sgotMax,2,RoundingMode.FLOOR);
						BigDecimal SGPTx = sgpt.divide(sgptMax,2,RoundingMode.FLOOR);
						BigDecimal GGTx = ggt.divide(ggtMax,2,RoundingMode.FLOOR);
						
						BigDecimal SGOTx_SGPTx =  new BigDecimal(Math.max(SGOTx.doubleValue(), SGPTx.doubleValue())).setScale(2,RoundingMode.HALF_UP);
						
						//cari life_rating
						Map life_rating = uwManager.selectLifeRating_ggtsgptsgot(GGTx, SGOTx_SGPTx);
						
						//cari rating
						List<Map> rating = uwManager.selectLifeRating(life_rating);
		//				String adb = (String) rating.get("ADB");
		//				String tpd = (String) rating.get("TPD");
		//				String wop = (String) rating.get("WOP");
						
		//				request.setAttribute("life_rating", life_rating);
						request.setAttribute("sgot_rate", SGOTx);
						request.setAttribute("sgpt_rate", SGPTx);
						request.setAttribute("ggt_rate", GGTx);
						request.setAttribute("rating", rating);
		//				request.setAttribute("adb_rate", adb);
		//				request.setAttribute("tpd_rate", tpd);
		//				request.setAttribute("wop_rate", wop);
					}
				}
			}
		}
		
		return ws;
	}
	
	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if(submitMode.equals("viewSimultan") || submitMode.equals("hitBMI") || 
		   submitMode.equals("occ") || submitMode.equals("hobby") || submitMode.equals("addQuest") ||
		   submitMode.equals("addKelainan") || submitMode.equals("addFinRat") || 
		   submitMode.equals("sumRating") || submitMode.equals("changeForm") || 
		   submitMode.equals("hitBloodPres") || submitMode.equals("addLpk") || submitMode.equals("addUrin") ||
		   submitMode.equals("addAda") || submitMode.equals("changeFormADA/L") || submitMode.equals("countChol") ||
		   submitMode.equals("peserta") || submitMode.equals("addToUwDesc") || 
		   submitMode.equals("addPenyDahLpk") || submitMode.equals("addPenySekLpk") || submitMode.equals("addPenyKelLpk") ||
		   submitMode.equals("addKelLpk") || submitMode.equals("addHiv") || submitMode.equals("addTumor") ||
		   submitMode.equals("addAbdomen") || submitMode.equals("addDadaPa") || submitMode.equals("addEkg") ||
		   submitMode.equals("addTreadmill") || submitMode.equals("addMedisLain") || submitMode.equals("changeMedis") ||
		   submitMode.equals("setDef") || submitMode.equals("delKelainan") || submitMode.equals("printWorksheet") || submitMode.equals("copyWorksheet")) {
			return true; 
		} else {
			return false;
		}
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object cmd) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		UwWorkSheet ws = (UwWorkSheet) cmd;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		f_hit_umur hitUmur = new f_hit_umur();
		String sysDate = df.format(elionsManager.selectSysdate());
		BigDecimal height =  new BigDecimal(0);
		BigDecimal bmi = new BigDecimal(0);
		
		ws.setSuccessMsg("");
		if(ws.getInsured_no() == 0) ws.setPptt(2);
		else ws.setPptt(3);
		
		// tampilkan polis simultan
		if(ws.getNamaPeserta() != null && ws.getBirthDate() != null) {
			List<SortedMap> temp, daftar1 = new ArrayList<SortedMap>();
			List<SortedMap> daftar2 = new ArrayList<SortedMap>();
			Boolean _2th = ws.getModeSim() == 0 ? false : true;
			List<Map> filters = uwManager.selectFilterSpajNew(ws.getPptt().toString(), ws.getNamaPeserta(), new SimpleDateFormat("yyyyMMdd").format(df.parse(ws.getBirthDate())), "LIKE%", _2th);
			
			for(int i=0; i<filters.size(); i++) {
				temp = sortingPlanUtama(sortingPlan(uwManager.selectAkumulasiPolisBySpaj((String) filters.get(i).get("REG_SPAJ"))));
				for(SortedMap map : temp) {
					String uw_info = uwManager.getKetMedis(map.get("M").toString());
					if(uw_info != null) map.put("D1", uw_info);
					if(map.get("L").toString().equals("1")) daftar1.add(map);
					else if(map.get("L").toString().equals("10")) daftar2.add(map);
				}
			}
			ws.setListSimultan(daftar1);
			ws.setListSimultanProc(daftar2);
		}
		
		// hitung bmi
		if(ws.getNonMedisTb() != null && ws.getNonMedisBb() != null) {
			height =  new BigDecimal(ws.getNonMedisTb()).divide(new BigDecimal(100)).pow(2);
			bmi = new BigDecimal(ws.getNonMedisBb()).divide(height,0,RoundingMode.HALF_UP);
			ws.setNonMedisBmi(bmi.doubleValue());
			ws.setEmBmi(uwManager.selectEmBmi(ws.getNonMedisBmi(),ws.getUmur()));
		}
		
		
		for(int a=0;a<ws.getListLpk().size();a++) {
			// hitung bmi yg ada di medis lpk			
			if(ws.getListLpk().get(a).getLpkTb() != null && ws.getListLpk().get(a).getLpkBb() != null) {
				height =  new BigDecimal(ws.getListLpk().get(a).getLpkTb()).divide(new BigDecimal(100)).pow(2);
				bmi = new BigDecimal(ws.getListLpk().get(a).getLpkBb()).divide(height,0,RoundingMode.HALF_UP);
				ws.getListLpk().get(a).setLpkBmi(bmi.doubleValue());
				ws.getListLpk().get(a).setEmBmiLpk(uwManager.selectEmBmi(ws.getListLpk().get(a).getLpkBmi(),ws.getUmur()));
			}
			
			// hitung tekanan darah di medis lpk
			if(ws.getListLpk().get(a).getLpkSystolic() != null && ws.getListLpk().get(a).getLpkDiastolic() != null) {
				ws.getListLpk().get(a).setEmBloodPresure(uwManager.selectEmBloodPresure(ws.getListLpk().get(a).getLpkSystolic(),ws.getListLpk().get(a).getLpkDiastolic(),ws.getUmur()));
			}			
		}
		
		// hitung cholsterol di medis adal
		for(int a=0;a<ws.getListAda().size();a++) {
			if(ws.getListAda().get(a).getTotal_cholesterol() != null && ws.getListAda().get(a).getHdl_cholesterol() != null) {
				BigDecimal totChol = new BigDecimal(ws.getListAda().get(a).getTotal_cholesterol());
				totChol = totChol.divide(new BigDecimal(ws.getListAda().get(a).getHdl_cholesterol()),2,RoundingMode.HALF_UP);
				ws.getListAda().get(a).setChol_hdl(totChol.doubleValue());
				ws.getListAda().get(a).setRatio_cholesterol(uwManager.getRatioChol(ws.getListAda().get(a).getSatuanChol(),ws.getListAda().get(a).getTotal_cholesterol(),ws.getListAda().get(a).getHdl_cholesterol(),ws.getUmur()));
			}
			if(ws.getListAda().get(a).getHdl_cholesterol() != null && ws.getListAda().get(a).getLdl_cholesterol() != null) {
				BigDecimal ldl = new BigDecimal(ws.getListAda().get(a).getLdl_cholesterol());
				ldl = ldl.divide(new BigDecimal(ws.getListAda().get(a).getHdl_cholesterol()),2,BigDecimal.ROUND_HALF_UP);
				ws.getListAda().get(a).setLdl_hdl(ldl.doubleValue());
			}
		}
		
		// hitung Penambahan rating SGOT, SGPT, GGT di medis adal
		for(int a=0;a<ws.getListAda().size();a++) {
			if(ws.getListAda().get(a).getSgot() != null && ws.getListAda().get(a).getSgpt() != null && ws.getListAda().get(a).getGgt() != null) {
				if(!ws.getListAda().get(a).getSgot().isEmpty() && !ws.getListAda().get(a).getSgpt().isEmpty() && !ws.getListAda().get(a).getGgt().isEmpty()) {
					BigDecimal sgot = new BigDecimal(ws.getListAda().get(a).getSgot());
					BigDecimal sgpt = new BigDecimal(ws.getListAda().get(a).getSgpt());
					BigDecimal ggt = new BigDecimal(ws.getListAda().get(a).getGgt());
					
					String sgotMaxStr = ws.getListAda().get(a).getNv_fungsi_hati_sgot_ast();
					String sgptMaxStr = ws.getListAda().get(a).getNv_fungsi_hati_sgpt_alt();
					String ggtMaxStr = ws.getListAda().get(a).getNv_fungsi_hati_gamma_gt_ggtp();
					
					BigDecimal sgotMax = new BigDecimal(0);
					BigDecimal sgptMax = new BigDecimal(0);
					BigDecimal ggtMax = new BigDecimal(0);
					String x = sgotMaxStr.replace(sgotMaxStr.replace("<", "").replace("=", "").replace("U/L", ""),"");
					if(sgotMaxStr.indexOf("<") != -1){
						sgotMax = new BigDecimal(sgotMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", "")).subtract(new BigDecimal(1));
					}else if(sgotMaxStr.indexOf("<=") != -1){
						sgotMax = new BigDecimal(sgotMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", ""));
					}else{
						sgotMax = new BigDecimal(sgotMaxStr.substring(sgotMaxStr.indexOf("-")).replace("<", "").replace("-", "").replace("=", "").replace("U/L", "").replace(" ", ""));
					}
					
					if(sgptMaxStr.indexOf("<") != -1){
						sgptMax = new BigDecimal(sgptMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", "")).subtract(new BigDecimal(1));
					}else if(sgptMaxStr.indexOf("<=") != -1){
						sgptMax = new BigDecimal(sgptMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", "")).subtract(new BigDecimal(1));
					}else{
						sgptMax = new BigDecimal(sgptMaxStr.substring(sgptMaxStr.indexOf("-")).replace("<", "").replace("-", "").replace("=", "").replace("U/L", "").replace(" ", ""));
					}
					
					if(ggtMaxStr.indexOf("<") != -1){
						ggtMax = new BigDecimal(ggtMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", "")).subtract(new BigDecimal(1));
					}else if(ggtMaxStr.indexOf("<=") != -1){
						ggtMax = new BigDecimal(ggtMaxStr.replace("<", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", ""));
					}else{
						ggtMax = new BigDecimal(ggtMaxStr.substring(ggtMaxStr.indexOf("-")).replace("<", "").replace("-", "").replace("=", "").replace("U/L", "").replace("-", "").replace(" ", ""));
					}
					
					BigDecimal SGOTx = sgot.divide(sgotMax,2,RoundingMode.FLOOR);
					BigDecimal SGPTx = sgpt.divide(sgptMax,2,RoundingMode.FLOOR);
					BigDecimal GGTx = ggt.divide(ggtMax,2,RoundingMode.FLOOR);
					
					BigDecimal SGOTx_SGPTx =  new BigDecimal(Math.max(SGOTx.doubleValue(), SGPTx.doubleValue())).setScale(2,RoundingMode.HALF_UP);
					
					//cari life_rating
					Map life_rating = uwManager.selectLifeRating_ggtsgptsgot(GGTx, SGOTx_SGPTx);
					
					//cari rating
					List<Map> rating = uwManager.selectLifeRating(life_rating);
	//				String adb = (String) rating.get("ADB");
	//				String tpd = (String) rating.get("TPD");
	//				String wop = (String) rating.get("WOP");
					
	//				request.setAttribute("life_rating", life_rating);
					request.setAttribute("sgot_rate", SGOTx);
					request.setAttribute("sgpt_rate", SGPTx);
					request.setAttribute("ggt_rate", GGTx);
					request.setAttribute("rating", rating);
	//				request.setAttribute("adb_rate", adb);
	//				request.setAttribute("tpd_rate", tpd);
	//				request.setAttribute("wop_rate", wop);
				}
			}
		}
		
		// tampilkan EM untuk occupation
		if(ws.getNonMedisOccupation() != null) {
			ws.setEmOcc(uwManager.selectEmWorksheet(ws.getNonMedisOccupation()));
		}
		// tampilkan EM untuk hobby
		if(ws.getNonMedisHobby() != null) {
			ws.setEmHobby(uwManager.selectEmWorksheet(ws.getNonMedisHobby()));
		}

		// tampilkan data peserta yg dipilih
		if(ws.getSubmitMode().equals("peserta")) {
			ws.setNamaPeserta(ServletRequestUtils.getStringParameter(request, "namaPeserta",""));
			ws.setInsured_no(ServletRequestUtils.getIntParameter(request, "insured_no",-1));
			ws.setUmur(ServletRequestUtils.getIntParameter(request, "umur",0));
			ws.setBirthDate(ServletRequestUtils.getStringParameter(request, "birthDate",""));
			ws.setMcl_id(ServletRequestUtils.getStringParameter(request, "mcl_id",""));
			
			ws.setSuccessMsg(null);
			ws.setListSimultan(null);
			ws.setListSimultanProc(null);
			ws.setPptt(null);
			ws.setNonMedisBb(null);
			ws.setNonMedisTb(null);
			ws.setNonMedisBmi(null);
			ws.setNonMedisBmiKelainan(null);
			ws.setEmBmi(null);
			ws.setNonMedisOccupation(null);
			ws.setNonMedisHabits(null);
			ws.setNonMedisHabitsKelainan(null);
			ws.setNonMedisHobby(null);
			ws.setNonMedisKelainan(null);
			ws.setNonMedisKelainanKelainan(null);
			ws.setEmOcc(null);
			ws.setEmHobby(null);
			ws.setFlag_fs(0);
			ws.setFs_date(null);
			ws.setFs_copy_rek_bank(null);
			ws.setFs_copy_npwp(null);
			ws.setFs_spt_pribadi(null);
			ws.setFs_copy_neraca_persh(null);
			ws.setFs_lain(null);
			ws.setFs_lain_desc(null);
			
			ws.setListQuest(new ArrayList<UwQuestionnaire>());
			for(int a=0;a<3;a++) { ws.getListQuest().add(new UwQuestionnaire()); }
			
			List<SortedMap> jumlProd = sortingPlan(uwManager.selectAkumulasiPolisBySpaj(ws.getReg_spaj()));
			ws.setListUwDec(new ArrayList<UwDecision>());
			UwDecision ud = new UwDecision(sysDate);
			ud.setRiderContent(jumlProd);
			ud.setUrutan_penyakit(1);
			ws.getListUwDec().add(ud);
			ud = new UwDecision(sysDate);
			ud.setRiderContent(jumlProd);
			ud.setUrutan_decision(0);
			ws.getListUwDec().add(ud);
			ud = new UwDecision(sysDate);
			ud.setRiderContent(jumlProd);
			ud.setUrutan_decision(1);
			ws.getListUwDec().add(ud);
			ws.setTotalPenyakitUwDesc(1);
			
			ws.setListLpk(new ArrayList<UwLpk>());
			ws.setListUrin(new ArrayList<UwUrin>());
			ws.setListAda(new ArrayList<UwAda>());
			ws.setListHiv(new ArrayList<UwHiv>());
			ws.setListTumor(new ArrayList<UwTumor>());
			ws.setListAbdomen(new ArrayList<UwAbdomen>());
			ws.setListDadaPa(new ArrayList<UwDadaPa>());
			ws.setListEkg(new ArrayList<UwEkg>());
			ws.setListTreadmill(new ArrayList<UwTreadmill>());
			ws.setListMedLain(new ArrayList<UwMedisLain>());
			
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADA, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_TUMOR, true);
			newPaket.put(medis_ABDOMEN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_EKG, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_MEDLAIN, true);
			
			ws = addMedis(ws, 0, true, newPaket);

			UwWorkSheet temp = uwManager.getUwWorksheet(ws.getReg_spaj(),ws.getInsured_no());
			if(temp != null) {
				//logic copy data dari spaj lain
				//String reg_spaj = ServletRequestUtils.getStringParameter(request, "copy_reg_spaj","");
//				if("".equals(reg_spaj)){
//					reg_spaj = ws.getReg_spaj();
//				}
//				UwWorkSheet isWorksheetExistCek = uwManager.getUwWorksheet(reg_spaj,null);
//				if(isWorksheetExistCek == null) {
//					reg_spaj = ws.getReg_spaj();
//				}
//				request.setAttribute("ref_reg_spaj", reg_spaj);
//				ws = inputAllDataWorksheet(ws, ws.getReg_spaj(), ws.getInsured_no(), df, hitUmur, sysDate, jumlProd);
				ws = inputAllDataWorksheet(ws, df, hitUmur, sysDate, jumlProd);
				configPosition(ws,currentUser);
			}
		}// tambah kolom questionaire
		else if(ws.getSubmitMode().equals("addQuest")) {
			ws.getListQuest().add(new UwQuestionnaire());
		} // tambah kolom kelainan/penyakit di table uw decision
		else if(ws.getSubmitMode().equals("addKelainan")) {
			List<SortedMap> jumlProd = sortingPlan(uwManager.selectAkumulasiPolisBySpaj(ws.getReg_spaj()));
			Integer urutan_dec = ws.getListUwDec().get(ws.getListUwDec().size()-1).getUrutan_decision();
			UwDecision ud = new UwDecision(df.format(elionsManager.selectSysdate()));
			ud.setRiderContent(jumlProd);
			
			ws.getListUwDec().add(ud);
			for(int a=0;a<=urutan_dec;a++) {
				ws.getListUwDec().set(ws.getListUwDec().size()-(1+a), ws.getListUwDec().get(ws.getListUwDec().size()-(2+a)));
			}
			ud.setUrutan_penyakit(ws.getListUwDec().size()-(urutan_dec+1));
			ws.getListUwDec().set(ud.getUrutan_penyakit()-1, ud);
			ws.setTotalPenyakitUwDesc(ud.getUrutan_penyakit());
		} // tamhah kolom final rating di table uw decision
		else if(ws.getSubmitMode().equals("addFinRat")) {
			List<SortedMap> jumlProd = sortingPlan(uwManager.selectAkumulasiPolisBySpaj(ws.getReg_spaj()));
			UwDecision ud = new UwDecision(df.format(elionsManager.selectSysdate()));
			ud.setRiderContent(jumlProd);
			ud.setUrutan_decision(ws.getListUwDec().get(ws.getListUwDec().size()-1).getUrutan_decision()+1);
			ws.getListUwDec().add(ud);
		} // tambah medis lpk baru
		else if(ws.getSubmitMode().equals("addLpk")) {
			ws.getListLpk().add(new UwLpk());
			ws.getListLpk().get(ws.getListLpk().size()-1).setUrutanLpk(ws.getListLpk().size());
		} // tambah medis urin baru
		else if(ws.getSubmitMode().equals("addUrin")) {
			ws.getListUrin().add(new UwUrin());
			ws.getListUrin().get(ws.getListUrin().size()-1).setUrutanUrin(ws.getListUrin().size());
		} // tambah medis ada/adal baru
		else if(ws.getSubmitMode().equals("addAda")) {
			ws.getListAda().add(new UwAda());
			ws.getListAda().get(ws.getListAda().size()-1).setUrutanAda(ws.getListAda().size());
		} // tambah medis hiv baru
		else if(ws.getSubmitMode().equals("addHiv")) {
			ws.getListHiv().add(new UwHiv());
			ws.getListHiv().get(ws.getListHiv().size()-1).setUrutanHiv(ws.getListHiv().size());
		} // tambah medis tumor baru
		else if(ws.getSubmitMode().equals("addTumor")) {
			ws.getListTumor().add(new UwTumor());
			ws.getListTumor().get(ws.getListTumor().size()-1).setUrutanTumor(ws.getListTumor().size());
		} // tambah medis abdomen baru
		else if(ws.getSubmitMode().equals("addAbdomen")) {
			ws.getListAbdomen().add(new UwAbdomen());
			ws.getListAbdomen().get(ws.getListAbdomen().size()-1).setUrutanAbdomen(ws.getListAbdomen().size());
		} // tambah medis ekg baru
		else if(ws.getSubmitMode().equals("addEkg")) {
			ws.getListEkg().add(new UwEkg());
			ws.getListEkg().get(ws.getListEkg().size()-1).setUrutanEkg(ws.getListEkg().size());
		} // tambah medis treadmill baru
		else if(ws.getSubmitMode().equals("addTreadmill")) {
			ws.getListTreadmill().add(new UwTreadmill());
			ws.getListTreadmill().get(ws.getListEkg().size()-1).setUrutanTreadmill(ws.getListTreadmill().size());
		} // tambah medis lain baru
		else if(ws.getSubmitMode().equals("addMedisLain")) {
			ws.getListMedLain().add(new UwMedisLain());
			ws.getListMedLain().get(ws.getListMedLain().size()-1).setUrutanMedisLain(ws.getListMedLain().size());
		} // tambah kolom riwayat penyakit dahulu di medis lpk
		else if(ws.getSubmitMode().equals("addPenyDahLpk")) {
			Integer index = ServletRequestUtils.getIntParameter(request, "index",-1);
			ws.getListLpk().get(index).getListRpd().add(new UwRiwPenyakit(1));
		} // tambah kolom riwayat penyakit sekarang di medis lpk
		else if(ws.getSubmitMode().equals("addPenySekLpk")) {
			Integer index = ServletRequestUtils.getIntParameter(request, "index",-1);
			ws.getListLpk().get(index).getListRps().add(new UwRiwPenyakit(2));
		} // tambah kolom riwayat penyakit keluarga di medis lpk
		else if(ws.getSubmitMode().equals("addPenyKelLpk")) {
			Integer index = ServletRequestUtils.getIntParameter(request, "index",-1);
			ws.getListLpk().get(index).getListRpk().add(new UwRiwPenyakit(3));
		} // tambah kolom riwayat kelainan di medis lpk
		else if(ws.getSubmitMode().equals("addKelLpk")) {
			Integer index = ServletRequestUtils.getIntParameter(request, "index",-1);
			ws.getListLpk().get(index).getListKelainan().add(new UwRiwPenyakit(4));
		} // hitung rating uw decision
		else if(ws.getSubmitMode().equals("sumRating")) {
			Integer urutan_dec = ws.getListUwDec().get(ws.getListUwDec().size()-1).getUrutan_decision();
			List<SortedMap> jumlProd = sortingPlan(uwManager.selectAkumulasiPolisBySpaj(ws.getReg_spaj()));
			
			for(int a=0;a<jumlProd.size()+1;a++) {
				BigDecimal valuePersen = new BigDecimal(0);
				BigDecimal valuePermil = new BigDecimal(0);
				Boolean number = true;
				
				for(int b=0;b<(ws.getListUwDec().size()-(urutan_dec+1));b++) {
					if(a == 0) {
						if(ws.getListUwDec().get(b).getProd_utama_persen().toLowerCase().contains("postpone") ||
						   ws.getListUwDec().get(b).getProd_utama_persen().toLowerCase().contains("decline") ||
						   ws.getListUwDec().get(b).getProd_utama_persen().toLowerCase().contains("exclusion") ||
						   ws.getListUwDec().get(b).getProd_utama_persen().toLowerCase().contains("borderline std")) {
							ws.getListUwDec().get(ws.getListUwDec().size()-(urutan_dec+1)).setProd_utama_persen(ws.getListUwDec().get(b).getProd_utama_persen());
							number = false;
							break;
						}
						else {
							valuePersen = valuePersen.add(new BigDecimal(ws.getListUwDec().get(b).getProd_utama_persen()));
						}
						if(number) {
							ws.getListUwDec().get(ws.getListUwDec().size()-(urutan_dec+1)).setProd_utama_persen(valuePersen.toString());
						}						
					}
					else if(a == 1) {
						if(ws.getListUwDec().get(b).getProd_utama_permil().toLowerCase().contains("postpone") ||
						   ws.getListUwDec().get(b).getProd_utama_permil().toLowerCase().contains("decline") ||
						   ws.getListUwDec().get(b).getProd_utama_permil().toLowerCase().contains("exclusion") ||
						   ws.getListUwDec().get(b).getProd_utama_persen().toLowerCase().contains("borderline std")) {
							ws.getListUwDec().get(ws.getListUwDec().size()-(urutan_dec+1)).setProd_utama_permil(ws.getListUwDec().get(b).getProd_utama_persen());
							number = false;
							break;
						}
						else {
							valuePermil = valuePermil.add(new BigDecimal(ws.getListUwDec().get(b).getProd_utama_permil()));
						}
						if(number) {
							ws.getListUwDec().get(ws.getListUwDec().size()-(urutan_dec+1)).setProd_utama_permil(valuePermil.toString());
						}						
					}
					else {
						if(ws.getListUwDec().get(b).getRider().get(a-2).getRate_persen().toLowerCase().contains("postpone") ||
						   ws.getListUwDec().get(b).getRider().get(a-2).getRate_persen().toLowerCase().contains("decline") ||
						   ws.getListUwDec().get(b).getRider().get(a-2).getRate_persen().toLowerCase().contains("exclusion") ||
						   ws.getListUwDec().get(b).getProd_utama_persen().toLowerCase().contains("borderline std")) {
							ws.getListUwDec().get(ws.getListUwDec().size()-(urutan_dec+1)).getRider().set(a-2, ws.getListUwDec().get(b).getRider().get(a-2));
							number = false;
							break;
						}
						else {
							valuePersen = valuePersen.add(new BigDecimal(ws.getListUwDec().get(b).getRider().get(a-2).getRate_persen()));
						}
						if(number) {
							ws.getListUwDec().get(ws.getListUwDec().size()-(urutan_dec+1)).getRider().get(a-2).setRate_persen(valuePersen.toString());
						}
						
						number = true;
						if(ws.getListUwDec().get(b).getRider().get(a-2).getRate_permil().toLowerCase().contains("postpone") ||
						   ws.getListUwDec().get(b).getRider().get(a-2).getRate_permil().toLowerCase().contains("decline") ||
						   ws.getListUwDec().get(b).getRider().get(a-2).getRate_permil().toLowerCase().contains("exclusion") ||
						   ws.getListUwDec().get(b).getProd_utama_persen().toLowerCase().contains("borderline std")) {
							ws.getListUwDec().get(ws.getListUwDec().size()-(urutan_dec+1)).getRider().set(a-2, ws.getListUwDec().get(b).getRider().get(a-2));
							number = false;
							break;
						}
						else {
							valuePermil = valuePermil.add(new BigDecimal(ws.getListUwDec().get(b).getRider().get(a-2).getRate_permil()));
						}
						if(number) {
							ws.getListUwDec().get(ws.getListUwDec().size()-(urutan_dec+1)).getRider().get(a-2).setRate_permil(valuePermil.toString());
						}
					}
				}				
			}
			
		} // pindahkan inputan ke kolom penyakit/kelainan di table uw decision
		else if(ws.getSubmitMode().equals("addToUwDesc")) {
			String mode = ServletRequestUtils.getStringParameter(request, "mode","");
			Integer index = ServletRequestUtils.getIntParameter(request, "index",-1);
			Integer sub = ServletRequestUtils.getIntParameter(request, "sub",-1);
			
			List<SortedMap> jumlProd = sortingPlan(uwManager.selectAkumulasiPolisBySpaj(ws.getReg_spaj()));
			Integer urutan_dec = ws.getListUwDec().get(ws.getListUwDec().size()-1).getUrutan_decision();
			UwDecision ud = new UwDecision(df.format(elionsManager.selectSysdate()));
			ud.setRiderContent(jumlProd);
			
			if(!ws.getListUwDec().get(0).getPenyakit().equals("")) {
				ws.getListUwDec().add(ud);
				for(int a=0;a<=urutan_dec;a++) {
					ws.getListUwDec().set(ws.getListUwDec().size()-(1+a), ws.getListUwDec().get(ws.getListUwDec().size()-(2+a)));
				}
				ud.setUrutan_penyakit(ws.getListUwDec().size()-(urutan_dec+1));
				ws.getListUwDec().set(ud.getUrutan_penyakit()-1, ud);
				//ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setUrutan_decision(null);
				ws.setTotalPenyakitUwDesc(ud.getUrutan_penyakit());	
			}
			else ud.setUrutan_penyakit(1);
			
			if(mode.equals("nonMedBmi")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getNonMedisBmiKelainan());
			}
			else if(mode.equals("nonMedOcc")) {
				//List<DropDown> x = uwManager.selectLstWorksheet(2,ws.getNonMedisOccupation());
				//ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(x.get(0).getKey());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getNonMedisOccDesc());
			}
			else if(mode.equals("nonMedHob")) {
				//List<DropDown> x = uwManager.selectLstWorksheet(3,ws.getNonMedisHobby());
				//ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(x.get(0).getKey());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getNonMedisHobbyDesc());
			}
			else if(mode.equals("nonMedQuest")) {
				List<DropDown> x = uwManager.selectLstWorksheet(4,ws.getListQuest().get(index).getLw_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(x.get(0).getKey());
			}
			else if(mode.equals("lpkBmi")) {
				if(ws.getListUwDec().get(0).getPenyakit().equals("")) ws.getListUwDec().get(0).setPenyakit(ws.getListLpk().get(index).getLpkBmiKelainan());
				else ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListLpk().get(index).getLpkBmiKelainan());
			}
			else if(mode.equals("nonMedHabits")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getNonMedisHabitsKelainan());
			}
			else if(mode.equals("nonMedKelainan")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getNonMedisKelainanKelainan());
			}
			else if(mode.equals("rpd")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListLpk().get(index).getListRpd().get(sub).getRp_desc());
			}
			else if(mode.equals("rps")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListLpk().get(index).getListRps().get(sub).getRp_desc());
			}
			else if(mode.equals("rpk")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListLpk().get(index).getListRpk().get(sub).getRp_desc());
			}
			else if(mode.equals("keln")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListLpk().get(index).getListKelainan().get(sub).getRp_desc());
			}
			else if(mode.equals("warna")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getWarna_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getWarna_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getWarna_lic_desc());
			}
			else if(mode.equals("kerjenihan")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getKejernihan_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getKejernihan_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getKejernihan_lic_desc());
			}
			else if(mode.equals("bj")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getBj_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getBj_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getBj_lic_desc());
			}
			else if(mode.equals("ph")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getPh_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getPh_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getPh_lic_desc());
			}
			else if(mode.equals("protein")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getProtein_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getProtein_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getProtein_lic_desc());
			}
			else if(mode.equals("glukosa")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getGlukosa_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getGlukosa_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getGlukosa_lic_desc());
			}
			else if(mode.equals("keton")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getKeton_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getKeton_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getKeton_lic_desc());
			}
			else if(mode.equals("bilirubin")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getBilirubin_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getBilirubin_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getBilirubin_lic_desc());
			}
			else if(mode.equals("urobilinogen")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getUrobilinogen_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getUrobilinogen_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getUrobilinogen_lic_desc());
			}
			else if(mode.equals("nitrit")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getNitrit_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getNitrit_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getNitrit_lic_desc());
			}
			else if(mode.equals("dar_sam")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getDarah_samar_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getDarah_samar_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getDarah_samar_lic_desc());
			}
			else if(mode.equals("lek_est")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getLekosit_esterase_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getLekosit_esterase_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getLekosit_esterase_lic_desc());
			}
			else if(mode.equals("sed_eri")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getSedimen_eritrosit_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getSedimen_eritrosit_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getSedimen_eritrosit_lic_desc());
			}
			else if(mode.equals("sed_leu")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getSedimen_leukosit_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getSedimen_leukosit_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getSedimen_leukosit_lic_desc());
			}
			else if(mode.equals("sed_epi")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getSedimen_epitel_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getSedimen_epitel_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getSedimen_epitel_lic_desc());
			}
			else if(mode.equals("sed_sil")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getSedimen_silinder_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getSedimen_silinder_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getSedimen_silinder_lic_desc());
			}
			else if(mode.equals("sed_kri")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListUrin().get(index).getSedimen_kristal_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListUrin().get(index).getSedimen_kristal_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListUrin().get(index).getSedimen_kristal_lic_desc());
			}
			else if(mode.equals("hemoglobin")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getHemoglobin_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getHemoglobin_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getHemoglobin_lic_desc());
			}
			else if(mode.equals("leukosit")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getLeukosit_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getLeukosit_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getLeukosit_lic_desc());
			}
			else if(mode.equals("eosinofil")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getEosinofil_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getEosinofil_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getEosinofil_lic_desc());
			}
			else if(mode.equals("basofil")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getBasofil_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getBasofil_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getBasofil_lic_desc());
			}
			else if(mode.equals("neu_bat")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getNeutrofil_batang_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getNeutrofil_batang_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getNeutrofil_batang_lic_desc());
			}
			else if(mode.equals("neu_seg")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getNeutrofil_segmen_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getNeutrofil_segmen_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getNeutrofil_segmen_lic_desc());
			}
			else if(mode.equals("limfosit")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getLimfosit_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getLimfosit_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getLimfosit_lic_desc());
			}
			else if(mode.equals("monosit")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getMonosit_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getMonosit_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getMonosit_lic_desc());
			}
			else if(mode.equals("trombosit")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getTrombosit_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getTrombosit_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getTrombosit_lic_desc());
			}
			else if(mode.equals("eritrosit")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getEritrosit_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getEritrosit_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getEritrosit_lic_desc());
			}
			else if(mode.equals("hematokrit")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getHematokrit_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getHematokrit_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getHematokrit_lic_desc());
			}
			else if(mode.equals("led")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getLed_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getLed_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getLed_lic_desc());
			}
			else if(mode.equals("mcv")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getMcv_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getMcv_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getMcv_lic_desc());
			}
			else if(mode.equals("mch")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getMch_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getMch_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getMch_lic_desc());
			}
			else if(mode.equals("mchc")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getMchc_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getMchc_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getMchc_lic_desc());
			}
			else if(mode.equals("rdw")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getRdw_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getRdw_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getRdw_lic_desc());
			}
			else if(mode.equals("glu_dar")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getGula_darah_puasa_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getGula_darah_puasa_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getGula_darah_puasa_lic_desc());
			}
			else if(mode.equals("glu_2")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getGula_darah_pp_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getGula_darah_pp_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getGula_darah_pp_lic_desc());
			}
			else if(mode.equals("hba1c")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getHba1c_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getHba1c_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getHba1c_lic_desc());
			}
			else if(mode.equals("sgot")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getSgot_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getSgot_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getSgot_lic_desc());
			}
			else if(mode.equals("sgpt")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getSgpt_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getSgpt_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getSgpt_lic_desc());
			}
			else if(mode.equals("ggt")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getGgt_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getGgt_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getGgt_lic_desc());
			}
			else if(mode.equals("fos_alk")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getFosfatase_alkali_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getFosfatase_alkali_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getFosfatase_alkali_lic_desc());
			}
			else if(mode.equals("bil_dir")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getBilirubin_direk_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getBilirubin_direk_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getBilirubin_direk_lic_desc());
			}
			else if(mode.equals("bil_ind")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getBilirubin_indirek_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getBilirubin_indirek_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getBilirubin_indirek_lic_desc());
			}
			else if(mode.equals("bil_tot")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getBilirubin_total_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getBilirubin_total_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getBilirubin_total_lic_desc());
			}
			else if(mode.equals("albumin")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getAlbumin_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getAlbumin_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getAlbumin_lic_desc());
			}
			else if(mode.equals("globulin")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getGlobulin_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getGlobulin_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getGlobulin_lic_desc());
			}
			else if(mode.equals("tot_pro")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getTotal_protein_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getTotal_protein_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getTotal_protein_lic_desc());
			}
			else if(mode.equals("hbs_ag")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getHbs_ag_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getHbs_ag_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getHbs_ag_lic_desc());
			}
			else if(mode.equals("hbe_ag")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getHbe_ag_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getHbe_ag_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getHbe_ag_lic_desc());
			}
			else if(mode.equals("creatinin")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getCreatinin_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getCreatinin_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getCreatinin_lic_desc());
			}
			else if(mode.equals("ureum")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getUreum_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getUreum_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getUreum_lic_desc());
			}
			else if(mode.equals("asm_ura")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAda().get(index).getAsam_urat_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAda().get(index).getAsam_urat_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAda().get(index).getAsam_urat_lic_desc());
			}
			else if(mode.equals("abdomen")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListAbdomen().get(index).getAbdomen_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListAbdomen().get(index).getAbdomen_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListAbdomen().get(index).getAbdomen_lic_desc());
			}
			else if(mode.equals("dada_pa")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListDadaPa().get(index).getDada_pa_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListDadaPa().get(index).getDada_pa_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListDadaPa().get(index).getDada_pa_lic_desc());
			}
			else if(mode.equals("ekg")) {
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setPenyakit(ws.getListEkg().get(index).getEkg_kelainan());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_id(ws.getListEkg().get(index).getEkg_lic_id());
				ws.getListUwDec().get(ud.getUrutan_penyakit()-1).setLic_desc(ws.getListEkg().get(index).getEkg_lic_desc());
			}
		} // ubah jenis medis
		else if(ws.getSubmitMode().equals("changeMedis")) {
			ws = changeMedis(ws,0,true);
		} // set nilai default untuk semua jenis medis seperti yg ada di lpk
		else if(ws.getSubmitMode().equals("setDef")) {
			Integer index = ServletRequestUtils.getIntParameter(request, "index",-1);
			if(!ws.getListLpk().get(index).getTglMcuLpk().equals("") && !ws.getListLpk().get(index).getTmpMcuLpk().equals("")) {
				if(ws.getListUrin().size() != 0 && ws.getListUrin().get(index) != null) {
					ws.getListUrin().get(index).setTglMcuUrin(ws.getListLpk().get(index).getTglMcuLpk());
					ws.getListUrin().get(index).setTmpMcuUrin(ws.getListLpk().get(index).getTmpMcuLpk());					
				}
				if(ws.getListAda().size() != 0 && ws.getListAda().get(index) != null) {
					ws.getListAda().get(index).setTglMcuAda(ws.getListLpk().get(index).getTglMcuLpk());
					ws.getListAda().get(index).setTmpMcuAda(ws.getListLpk().get(index).getTmpMcuLpk());					
				}
				if(ws.getListHiv().size() != 0 && ws.getListHiv().get(index) != null) {
					ws.getListHiv().get(index).setTglMcuHiv(ws.getListLpk().get(index).getTglMcuLpk());
					ws.getListHiv().get(index).setTmpMcuHiv(ws.getListLpk().get(index).getTmpMcuLpk());
				}
				if(ws.getListTumor().size() != 0 && ws.getListTumor().get(index) != null) {
					ws.getListTumor().get(index).setTglMcuTumor(ws.getListLpk().get(index).getTglMcuLpk());
					ws.getListTumor().get(index).setTmpMcuTumor(ws.getListLpk().get(index).getTmpMcuLpk());
				}
				if(ws.getListAbdomen().size() != 0 && ws.getListAbdomen().get(index) != null) {
					ws.getListAbdomen().get(index).setTglMcuAbdomen(ws.getListLpk().get(index).getTglMcuLpk());
					ws.getListAbdomen().get(index).setTmpMcuAbdomen(ws.getListLpk().get(index).getTmpMcuLpk());
				}
				if(ws.getListDadaPa().size() != 0 && ws.getListDadaPa().get(index) != null) {
					ws.getListDadaPa().get(index).setTglMcuDadaPA(ws.getListLpk().get(index).getTglMcuLpk());
					ws.getListDadaPa().get(index).setTmpMcuDadaPA(ws.getListLpk().get(index).getTmpMcuLpk());
				}
				if(ws.getListEkg().size() != 0 && ws.getListEkg().get(index) != null) {
					ws.getListEkg().get(index).setTglMcuEkg(ws.getListLpk().get(index).getTglMcuLpk());
					ws.getListEkg().get(index).setTmpMcuEkg(ws.getListLpk().get(index).getTmpMcuLpk());
				}
				if(ws.getListTreadmill().size() != 0 && ws.getListTreadmill().get(index) != null) {
					ws.getListTreadmill().get(index).setTglMcuTreadmill(ws.getListLpk().get(index).getTglMcuLpk());
					ws.getListTreadmill().get(index).setTmpMcuTreadmill(ws.getListLpk().get(index).getTmpMcuLpk());
				}
				if(ws.getListMedLain().size() != 0 && ws.getListMedLain().get(index) != null) {
					ws.getListMedLain().get(index).setTglMcuMedisLain(ws.getListLpk().get(index).getTglMcuLpk());
					ws.getListMedLain().get(index).setTmpMcuMedisLain(ws.getListLpk().get(index).getTmpMcuLpk());
				}	
			}
		} // delete kolom kelainan di table uw decision 
		else if(ws.getSubmitMode().equals("delKelainan")) {
			Integer totalRate = 0;
			List<UwDecision> tempDec = ws.getListUwDec();
			ws.setListUwDec(new ArrayList<UwDecision>());
			for(int a=tempDec.size()-1;a>=0;a--) {
				String isDel = ServletRequestUtils.getStringParameter(request, "delPenyakit"+a,"");
				if(!isDel.equals("")) {
					tempDec.remove(a);
				}
			}
			for(int a=0;a<tempDec.size();a++) {
				if(tempDec.get(a).getUrutan_decision() == null) {
					tempDec.get(a).setUrutan_penyakit(a+1);
					for(int b=0;b< tempDec.get(a).getRider().size();b++) {
						tempDec.get(a).getRider().get(b).setUrutan_penyakit(a+1);
						tempDec.get(a).getRider().get(b).setUrutan_decision(null);
					}
					ws.setTotalPenyakitUwDesc(tempDec.get(a).getUrutan_penyakit());
				}
				tempDec.get(a).setMwd_urut(a+1);
				for(int b=0;b< tempDec.get(a).getRider().size();b++) {
					tempDec.get(a).getRider().get(b).setMwd_urut(a+1);
				}
				ws.getListUwDec().add(tempDec.get(a));
			}
		} // print uw worsheet
		else if(ws.getSubmitMode().equals("printWorksheet")) {
			printUwWorksheet(ws, request, response);
			elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "Print UW Worksheet" , ws.getReg_spaj(), 0);
		}// copy uw worsheet
		else if(ws.getSubmitMode().equals("copyWorksheet")) {
			String copy_reg_spaj = ServletRequestUtils.getStringParameter(request, "copy_reg_spaj","");
			String copy_peserta = ServletRequestUtils.getStringParameter(request, "copy_peserta","");
			
			UwWorkSheet copyTemp = uwManager.getUwWorksheet(copy_reg_spaj, new Integer(copy_peserta));
			//list peserta
			List<HashMap> copyDataPeserta = uwManager.selectDaftarPeserta(copy_reg_spaj);
			int a = 0;
			String mcl_id = "";
			for (HashMap map2 : copyDataPeserta) {
				if(Integer.parseInt(map2.get("INSURED_NO").toString()) == 1) mcl_id = map2.get("MCL_ID").toString();
				else if(map2.get("MCL_ID") == null) map2.put("MCL_ID", mcl_id);
				String birthDate = df.format(map2.get("MSPE_DATE_BIRTH"));
				map2.put("UMUR", hitUmur.umur(Integer.parseInt(birthDate.substring(6)),Integer.parseInt(birthDate.substring(3,5)) , Integer.parseInt(birthDate.substring(0,2)), Integer.parseInt(sysDate.substring(6)),Integer.parseInt(sysDate.substring(3,5)) , Integer.parseInt(sysDate.substring(0,2))));
				map2.put("MSPE_DATE_BIRTH", birthDate);
				copyDataPeserta.set(a++, map2);
			}
			//=========
			request.setAttribute("copyDataPeserta", copyDataPeserta);
			request.setAttribute("ref_reg_spaj", copy_reg_spaj);
			request.setAttribute("ref_peserta", copy_peserta);
			if(copyTemp != null){
				List<SortedMap> jumlProd = sortingPlan(uwManager.selectAkumulasiPolisBySpaj(ws.getReg_spaj()));
				ws.setListUwDec(new ArrayList<UwDecision>());
				UwDecision ud = new UwDecision(sysDate);
				ud.setRiderContent(jumlProd);
				ud.setUrutan_penyakit(1);
				ws.getListUwDec().add(ud);
				ud = new UwDecision(sysDate);
				ud.setRiderContent(jumlProd);
				ud.setUrutan_decision(0);
				ws.getListUwDec().add(ud);
				ud = new UwDecision(sysDate);
				ud.setRiderContent(jumlProd);
				ud.setUrutan_decision(1);
				ws.getListUwDec().add(ud);
				ws.setTotalPenyakitUwDesc(1);
				ws = copyDataWorksheet(ws, copy_reg_spaj.trim(), new Integer(copy_peserta),df, hitUmur, sysDate, jumlProd);
				configPosition(ws,currentUser);
				request.setAttribute("copyMessage", "worksheet ditemukan");
				request.setAttribute("copyMessageColor", "green");
			}else{
				request.setAttribute("copyMessage", "worksheet tidak ditemukan");
				request.setAttribute("copyMessageColor", "red");
			}
		}
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		UwWorkSheet ws = (UwWorkSheet) cmd;
		
		ws.setSuccessMsg("");
		if((ws.getSubmitMode().equals("saveWorksheet") && ws.getPosition() == 1) || (ws.getSubmitMode().equals("saveWorksheet") && ws.getPosition() == 2)) {
			ws.setListWarning(new ArrayList<String>());
			for(int a=0;a<ws.getListUrin().size();a++) {
				if(ws.getListUrin().get(a).getFlagWarna() == 1 && (ws.getListUrin().get(a).getWarna_lic_id().equals("") || ws.getListUrin().get(a).getWarna_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin warna");
				if(ws.getListUrin().get(a).getFlagkejernihan() == 1 && (ws.getListUrin().get(a).getKejernihan_lic_id().equals("") || ws.getListUrin().get(a).getKejernihan_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin kejernihan");
				if(ws.getListUrin().get(a).getFlagBj() == 1 && (ws.getListUrin().get(a).getBj_lic_id().equals("") || ws.getListUrin().get(a).getBj_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin berat jenis");
				if(ws.getListUrin().get(a).getFlagPh() == 1 && (ws.getListUrin().get(a).getPh_lic_id().equals("") || ws.getListUrin().get(a).getPh_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin pH");
				if(ws.getListUrin().get(a).getFlagProtein() == 1 && (ws.getListUrin().get(a).getProtein_lic_id().equals("") || ws.getListUrin().get(a).getProtein_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin protein");
				if(ws.getListUrin().get(a).getFlagGlukosa() == 1 && (ws.getListUrin().get(a).getGlukosa_lic_id().equals("") || ws.getListUrin().get(a).getGlukosa_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin glukosa");
				if(ws.getListUrin().get(a).getFlagKeton() == 1 && (ws.getListUrin().get(a).getKeton_lic_id().equals("") || ws.getListUrin().get(a).getKeton_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin keton");
				if(ws.getListUrin().get(a).getFlagBilirubin() == 1 && (ws.getListUrin().get(a).getBilirubin_lic_id().equals("") || ws.getListUrin().get(a).getBilirubin_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin bilirubin");
				if(ws.getListUrin().get(a).getFlagUrobilinogen() == 1 && (ws.getListUrin().get(a).getUrobilinogen_lic_id().equals("") || ws.getListUrin().get(a).getUrobilinogen_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin urobilinogen");
				if(ws.getListUrin().get(a).getFlagNitrit() == 1 && (ws.getListUrin().get(a).getNitrit_lic_id().equals("") || ws.getListUrin().get(a).getNitrit_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin nitrit");
				if(ws.getListUrin().get(a).getFlagDarah_samar() == 1 && (ws.getListUrin().get(a).getDarah_samar_lic_id().equals("") || ws.getListUrin().get(a).getDarah_samar_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin darah samar");
				if(ws.getListUrin().get(a).getFlagLeukosit_esterase() == 1 && (ws.getListUrin().get(a).getLekosit_esterase_lic_id().equals("") || ws.getListUrin().get(a).getLekosit_esterase_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin lekosit esterase");
				if(ws.getListUrin().get(a).getFlagLeukosit_esterase() == 1 && (ws.getListUrin().get(a).getLekosit_esterase_lic_id().equals("") || ws.getListUrin().get(a).getLekosit_esterase_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin lekosit esterase");
				if(ws.getListUrin().get(a).getFlagSedimen_eritrosit() == 1 && (ws.getListUrin().get(a).getSedimen_eritrosit_lic_id().equals("") || ws.getListUrin().get(a).getSedimen_eritrosit_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin sedimen eritrosit");
				if(ws.getListUrin().get(a).getFlagSedimen_eritrosit() == 1 && (ws.getListUrin().get(a).getSedimen_eritrosit_lic_id().equals("") || ws.getListUrin().get(a).getSedimen_eritrosit_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin sedimen eritrosit");
				if(ws.getListUrin().get(a).getFlagSedimen_leukosit() == 1 && (ws.getListUrin().get(a).getSedimen_leukosit_lic_id().equals("") || ws.getListUrin().get(a).getSedimen_leukosit_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin sedimen leukosit");
				if(ws.getListUrin().get(a).getFlagSedimen_epitel() == 1 && (ws.getListUrin().get(a).getSedimen_epitel_lic_id().equals("") || ws.getListUrin().get(a).getSedimen_epitel_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin sedimen epitel");
				if(ws.getListUrin().get(a).getFlagSedimen_silinder() == 1 && (ws.getListUrin().get(a).getSedimen_silinder_lic_id().equals("") || ws.getListUrin().get(a).getSedimen_silinder_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin sedimen silinder");
				if(ws.getListUrin().get(a).getFlagSedimen_kristal() == 1 && (ws.getListUrin().get(a).getSedimen_kristal_lic_id().equals("") || ws.getListUrin().get(a).getSedimen_kristal_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada urin sedimen kristal");
			}
			for(int a=0;a<ws.getListAda().size();a++) {
				if(ws.getListAda().get(a).getFlagHemoglobin() == 1 && (ws.getListAda().get(a).getHemoglobin_lic_id().equals("") || ws.getListAda().get(a).getHemoglobin_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada hemoglobin");
				if(ws.getListAda().get(a).getFlagLeukosit() == 1 && (ws.getListAda().get(a).getLeukosit_lic_id().equals("") || ws.getListAda().get(a).getLeukosit_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada leukosit");
				if(ws.getListAda().get(a).getFlagEosinofil() == 1 && (ws.getListAda().get(a).getEosinofil_lic_id().equals("") || ws.getListAda().get(a).getEosinofil_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada eosinofil");
				if(ws.getListAda().get(a).getFlagBasofil() == 1 && (ws.getListAda().get(a).getBasofil_lic_id().equals("") || ws.getListAda().get(a).getBasofil_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada basofil");
				if(ws.getListAda().get(a).getFlagNeutrofil_batang() == 1 && (ws.getListAda().get(a).getNeutrofil_batang_lic_id().equals("") || ws.getListAda().get(a).getNeutrofil_batang_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada neutrofil batang");
				if(ws.getListAda().get(a).getFlagNeutrofil_segmen() == 1 && (ws.getListAda().get(a).getNeutrofil_segmen_lic_id().equals("") || ws.getListAda().get(a).getNeutrofil_segmen_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada neutrofil segmen");
				if(ws.getListAda().get(a).getFlagLimfosit() == 1 && (ws.getListAda().get(a).getLimfosit_lic_id().equals("") || ws.getListAda().get(a).getLimfosit_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada limfosit");
				if(ws.getListAda().get(a).getFlagMonosit() == 1 && (ws.getListAda().get(a).getMonosit_lic_id().equals("") || ws.getListAda().get(a).getMonosit_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada monosit");
				if(ws.getListAda().get(a).getFlagTrombosit() == 1 && (ws.getListAda().get(a).getTrombosit_lic_id().equals("") || ws.getListAda().get(a).getTrombosit_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada trombosit");
				if(ws.getListAda().get(a).getFlagEritrosit() == 1 && (ws.getListAda().get(a).getEritrosit_lic_id().equals("") || ws.getListAda().get(a).getEritrosit_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada eritrosit");
				if(ws.getListAda().get(a).getFlagHematokrit() == 1 && (ws.getListAda().get(a).getHematokrit_lic_id().equals("") || ws.getListAda().get(a).getHematokrit_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada hematokrit");
				if(ws.getListAda().get(a).getFlagLed() == 1 && (ws.getListAda().get(a).getLed_lic_id().equals("") || ws.getListAda().get(a).getLed_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada led");
				if(ws.getListAda().get(a).getFlagMcv() == 1 && (ws.getListAda().get(a).getMcv_lic_id().equals("") || ws.getListAda().get(a).getMcv_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada mcv");
				if(ws.getListAda().get(a).getFlagMchc() == 1 && (ws.getListAda().get(a).getMchc_lic_id().equals("") || ws.getListAda().get(a).getMch_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada mch");
				if(ws.getListAda().get(a).getFlagMchc() == 1 && (ws.getListAda().get(a).getMchc_lic_id().equals("") || ws.getListAda().get(a).getMchc_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada mchc");
				if(ws.getListAda().get(a).getFlagRdw() == 1 && (ws.getListAda().get(a).getRdw_lic_id().equals("") || ws.getListAda().get(a).getRdw_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ada rdw");
				if(ws.getListAda().get(a).getFlagGulaDarahPuasa() == 1 && (ws.getListAda().get(a).getGula_darah_puasa_lic_id().equals("") || ws.getListAda().get(a).getGula_darah_puasa_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal gula darah puasa");
				if(ws.getListAda().get(a).getFlagGulaDarahPp() == 1 && (ws.getListAda().get(a).getGula_darah_pp_lic_id().equals("") || ws.getListAda().get(a).getGula_darah_pp_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal gula darah pp");
				if(ws.getListAda().get(a).getFlagHb1c() == 1 && (ws.getListAda().get(a).getHba1c_lic_id().equals("") || ws.getListAda().get(a).getHba1c_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal HbA1c");
				if(ws.getListAda().get(a).getFlagSgot() == 1 && (ws.getListAda().get(a).getSgot_lic_id().equals("") || ws.getListAda().get(a).getSgot_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal sgot");
				if(ws.getListAda().get(a).getFlagSgpt() == 1 && (ws.getListAda().get(a).getSgpt_lic_id().equals("") || ws.getListAda().get(a).getSgpt_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal sgpt");
				if(ws.getListAda().get(a).getFlagGgt() == 1 && (ws.getListAda().get(a).getGgt_lic_id().equals("") || ws.getListAda().get(a).getGgt_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal ggt");
				if(ws.getListAda().get(a).getFlagFosfatase_alkali() == 1 && (ws.getListAda().get(a).getFosfatase_alkali_lic_id().equals("") || ws.getListAda().get(a).getFosfatase_alkali_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal fosfatase alkali");
				if(ws.getListAda().get(a).getFlagBilirubin_direk() == 1 && (ws.getListAda().get(a).getBilirubin_direk_lic_id().equals("") || ws.getListAda().get(a).getBilirubin_direk_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal bilirubin direk");
				if(ws.getListAda().get(a).getFlagBilirubin_direk() == 1 && (ws.getListAda().get(a).getBilirubin_direk_lic_id().equals("") || ws.getListAda().get(a).getBilirubin_direk_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal bilirubin direk");
				if(ws.getListAda().get(a).getFlagBilirubin_indirek() == 1 && (ws.getListAda().get(a).getBilirubin_indirek_lic_id().equals("") || ws.getListAda().get(a).getBilirubin_indirek_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal bilirubin indirek");
				if(ws.getListAda().get(a).getFlagBilirubin_total() == 1 && (ws.getListAda().get(a).getBilirubin_total_lic_id().equals("") || ws.getListAda().get(a).getBilirubin_total_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal bilirubin total");
				if(ws.getListAda().get(a).getFlagAlbumin() == 1 && (ws.getListAda().get(a).getAlbumin_lic_id().equals("") || ws.getListAda().get(a).getAlbumin_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal albumin");
				if(ws.getListAda().get(a).getFlagGlobulin() == 1 && (ws.getListAda().get(a).getGlobulin_lic_id().equals("") || ws.getListAda().get(a).getGlobulin_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal globulin");
				if(ws.getListAda().get(a).getFlagTotal_protein() == 1 && (ws.getListAda().get(a).getTotal_protein_lic_id().equals("") || ws.getListAda().get(a).getTotal_protein_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal total protein");
				if(ws.getListAda().get(a).getFlagHbs_ag() == 1 && (ws.getListAda().get(a).getHbs_ag_lic_id().equals("") || ws.getListAda().get(a).getHbs_ag_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal HBs Ag");
				if(ws.getListAda().get(a).getFlagHbe_ag() == 1 && (ws.getListAda().get(a).getHbe_ag_lic_id().equals("") || ws.getListAda().get(a).getHbe_ag_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal HBe Ag");
				if(ws.getListAda().get(a).getFlagCreatinin() == 1 && (ws.getListAda().get(a).getCreatinin_lic_id().equals("") || ws.getListAda().get(a).getCreatinin_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal creatinin");
				if(ws.getListAda().get(a).getFlagUreum() == 1 && (ws.getListAda().get(a).getUreum_lic_id().equals("") || ws.getListAda().get(a).getUreum_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal ureum");
				if(ws.getListAda().get(a).getFlagAsam_urat() == 1 && (ws.getListAda().get(a).getAsam_urat_lic_id().equals("") || ws.getListAda().get(a).getAsam_urat_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada adal asam urat");
			}
			for(int a=0;a<ws.getListAbdomen().size();a++) {
				if(ws.getListAbdomen().get(a).getFlagAbdomen() == 1 && (ws.getListAbdomen().get(a).getAbdomen_lic_id().equals("") || ws.getListAbdomen().get(a).getAbdomen_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada usg abdomen");
			}
			for(int a=0;a<ws.getListDadaPa().size();a++) {
				if(ws.getListDadaPa().get(a).getFlagDadaPa() == 1 && (ws.getListDadaPa().get(a).getDada_pa_lic_id().equals("") || ws.getListDadaPa().get(a).getDada_pa_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada rontgen dada pa");
			}
			for(int a=0;a<ws.getListEkg().size();a++) {
				if(ws.getListEkg().get(a).getFlagEkg() == 1 && (ws.getListEkg().get(a).getEkg_lic_id().equals("") || ws.getListEkg().get(a).getEkg_lic_desc().equals("")))
					errors.reject("", "icd code & keterangannya pada ekg");
			}
		}
		if(ws.getSubmitMode().equals("saveWorksheet") && ws.getPosition() == 0) {
			ws.setListWarning(new ArrayList<String>());
			if("A".equals(ws.getJenisMedis())){//LPK dari dokter spesialis anak
				for(int a=0;a<ws.getListLpk().size();a++) {
					if("".equals(ws.getListLpk().get(a).getTmpMcuLpk())){errors.reject("", "tempat medis pada LPK");}
					if("".equals(ws.getListLpk().get(a).getTglMcuLpk())){errors.reject("", "tanggal medis pada LPK");}
				}
			}else if("B".equals(ws.getJenisMedis())){//LPK dari dokter spesialis anak + urin lengkap
				for(int a=0;a<ws.getListLpk().size();a++) {
					if("".equals(ws.getListLpk().get(a).getTmpMcuLpk())){errors.reject("", "tempat medis pada LPK");}
					if("".equals(ws.getListLpk().get(a).getTglMcuLpk())){errors.reject("", "tanggal medis pada LPK");}
				}
				for(int a=0;a<ws.getListUrin().size();a++) {
					if("".equals(ws.getListUrin().get(a).getTmpMcuUrin())){errors.reject("", "tempat medis pada urin warna");}
					if("".equals(ws.getListUrin().get(a).getTglMcuUrin())){errors.reject("", "tanggal medis pada urin warna");}
				}
			}else if("C".equals(ws.getJenisMedis())){//LPK + urin lengkap + ADA (darah rutin) + EKG
				for(int a=0;a<ws.getListLpk().size();a++) {
					if("".equals(ws.getListLpk().get(a).getTmpMcuLpk())){errors.reject("", "tempat medis pada LPK");}
					if("".equals(ws.getListLpk().get(a).getTglMcuLpk())){errors.reject("", "tanggal medis pada LPK");}
				}
				for(int a=0;a<ws.getListUrin().size();a++) {
					if("".equals(ws.getListUrin().get(a).getTmpMcuUrin())){errors.reject("", "tempat medis pada urin warna");}
					if("".equals(ws.getListUrin().get(a).getTglMcuUrin())){errors.reject("", "tanggal medis pada urin warna");}
				}
				for(int a=0;a<ws.getListAda().size();a++) {
					if("".equals(ws.getListAda().get(a).getTmpMcuAda())){errors.reject("", "tempat medis pada ADA");}
					if("".equals(ws.getListAda().get(a).getTglMcuAda())){errors.reject("", "tanggal medis pada ADA");}
				}
				for(int a=0;a<ws.getListEkg().size();a++) {
					if("".equals(ws.getListEkg().get(a).getTmpMcuEkg())){errors.reject("", "tempat medis pada EKG");}
					if("".equals(ws.getListEkg().get(a).getTglMcuEkg())){errors.reject("", "tanggal medis pada EKG");}
				}
			}else if("D".equals(ws.getJenisMedis())){//LPK + urin lengkap + ADAL + EKG
				for(int a=0;a<ws.getListLpk().size();a++) {
					if("".equals(ws.getListLpk().get(a).getTmpMcuLpk())){errors.reject("", "tempat medis pada LPK");}
					if("".equals(ws.getListLpk().get(a).getTglMcuLpk())){errors.reject("", "tanggal medis pada LPK");}
				}
				for(int a=0;a<ws.getListUrin().size();a++) {
					if("".equals(ws.getListUrin().get(a).getTmpMcuUrin())){errors.reject("", "tempat medis pada urin warna");}
					if("".equals(ws.getListUrin().get(a).getTglMcuUrin())){errors.reject("", "tanggal medis pada urin warna");}
				}
				for(int a=0;a<ws.getListAda().size();a++) {
					if("".equals(ws.getListAda().get(a).getTmpMcuAda())){errors.reject("", "tempat medis pada ADAL");}
					if("".equals(ws.getListAda().get(a).getTglMcuAda())){errors.reject("", "tanggal medis pada ADAL");}
				}
				for(int a=0;a<ws.getListEkg().size();a++) {
					if("".equals(ws.getListEkg().get(a).getTmpMcuEkg())){errors.reject("", "tempat medis pada EKG");}
					if("".equals(ws.getListEkg().get(a).getTglMcuEkg())){errors.reject("", "tanggal medis pada EKG");}
				}
			}else if("E".equals(ws.getJenisMedis())){//LPK + urin lengkap + ADAL + rontgen dada (chest x-ray) + EKG
				for(int a=0;a<ws.getListLpk().size();a++) {
					if("".equals(ws.getListLpk().get(a).getTmpMcuLpk())){errors.reject("", "tempat medis pada LPK");}
					if("".equals(ws.getListLpk().get(a).getTglMcuLpk())){errors.reject("", "tanggal medis pada LPK");}
				}
				for(int a=0;a<ws.getListUrin().size();a++) {
					if("".equals(ws.getListUrin().get(a).getTmpMcuUrin())){errors.reject("", "tempat medis pada urin warna");}
					if("".equals(ws.getListUrin().get(a).getTglMcuUrin())){errors.reject("", "tanggal medis pada urin warna");}
				}
				for(int a=0;a<ws.getListAda().size();a++) {
					if("".equals(ws.getListAda().get(a).getTmpMcuAda())){errors.reject("", "tempat medis pada ADAL");}
					if("".equals(ws.getListAda().get(a).getTglMcuAda())){errors.reject("", "tanggal medis pada ADAL");}
				}
				for(int a=0;a<ws.getListDadaPa().size();a++) {
					if("".equals(ws.getListDadaPa().get(a).getTmpMcuDadaPA())){errors.reject("", "tempat medis pada rontgen dada");}
					if("".equals(ws.getListDadaPa().get(a).getTglMcuDadaPA())){errors.reject("", "tanggal medis pada rontgen dada");}
				}
				for(int a=0;a<ws.getListEkg().size();a++) {
					if("".equals(ws.getListEkg().get(a).getTmpMcuEkg())){errors.reject("", "tempat medis pada EKG");}
					if("".equals(ws.getListEkg().get(a).getTglMcuEkg())){errors.reject("", "tanggal medis pada EKG");}
				}
			}else if("F".equals(ws.getJenisMedis())){//LPK + urin lengkap + ADAL + rontgen dada (chest x-ray) + treadmill test
				for(int a=0;a<ws.getListLpk().size();a++) {
					if("".equals(ws.getListLpk().get(a).getTmpMcuLpk())){errors.reject("", "tempat medis pada LPK");}
					if("".equals(ws.getListLpk().get(a).getTglMcuLpk())){errors.reject("", "tanggal medis pada LPK");}
				}
				for(int a=0;a<ws.getListUrin().size();a++) {
					if("".equals(ws.getListUrin().get(a).getTmpMcuUrin())){errors.reject("", "tempat medis pada urin warna");}
					if("".equals(ws.getListUrin().get(a).getTglMcuUrin())){errors.reject("", "tanggal medis pada urin warna");}
				}
				for(int a=0;a<ws.getListAda().size();a++) {
					if("".equals(ws.getListAda().get(a).getTmpMcuAda())){errors.reject("", "tempat medis pada ADAL");}
					if("".equals(ws.getListAda().get(a).getTglMcuAda())){errors.reject("", "tanggal medis pada ADAL");}
				}
				for(int a=0;a<ws.getListDadaPa().size();a++) {
					if("".equals(ws.getListDadaPa().get(a).getTmpMcuDadaPA())){errors.reject("", "tempat medis pada rontgen dada");}
					if("".equals(ws.getListDadaPa().get(a).getTglMcuDadaPA())){errors.reject("", "tanggal medis pada rontgen dada");}
				}
				for(int a=0;a<ws.getListTreadmill().size();a++) {
					if("".equals(ws.getListTreadmill().get(a).getTmpMcuTreadmill())){errors.reject("", "tempat medis pada treadmill");}
					if("".equals(ws.getListTreadmill().get(a).getTglMcuTreadmill())){errors.reject("", "tanggal medis pada treadmill");}
				}
			}else if("G".equals(ws.getJenisMedis())){//LPK + urin lengkap + ADAL + anti hiv test + cea + rontgen dada (chest x-ray) + treadmill test
				for(int a=0;a<ws.getListLpk().size();a++) {
					if("".equals(ws.getListLpk().get(a).getTmpMcuLpk())){errors.reject("", "tempat medis pada LPK");}
					if("".equals(ws.getListLpk().get(a).getTglMcuLpk())){errors.reject("", "tanggal medis pada LPK");}
				}
				for(int a=0;a<ws.getListUrin().size();a++) {
					if("".equals(ws.getListUrin().get(a).getTmpMcuUrin())){errors.reject("", "tempat medis pada urin warna");}
					if("".equals(ws.getListUrin().get(a).getTglMcuUrin())){errors.reject("", "tanggal medis pada urin warna");}
				}
				for(int a=0;a<ws.getListAda().size();a++) {
					if("".equals(ws.getListAda().get(a).getTmpMcuAda())){errors.reject("", "tempat medis pada ADAL");}
					if("".equals(ws.getListAda().get(a).getTglMcuAda())){errors.reject("", "tanggal medis pada ADAL");}
				}
				for(int a=0;a<ws.getListHiv().size();a++) {
					if("".equals(ws.getListHiv().get(a).getTmpMcuHiv())){errors.reject("", "tempat medis pada anti HIV test");}
					if("".equals(ws.getListHiv().get(a).getTglMcuHiv())){errors.reject("", "tanggal medis pada anti HIV test");}
				}
//				for(int a=0;a<ws.getListCea().size();a++) {
//				if("".equals(ws.getListHiv().get(a).getTmpMcuHiv())){errors.reject("", "tempat medis pada anti HIV test");}
//				if("".equals(ws.getListHiv().get(a).getTglMcuHiv())){errors.reject("", "tanggal medis pada anti HIV test");}
//			}
				for(int a=0;a<ws.getListDadaPa().size();a++) {
					if("".equals(ws.getListDadaPa().get(a).getTmpMcuDadaPA())){errors.reject("", "tempat medis pada rontgen dada");}
					if("".equals(ws.getListDadaPa().get(a).getTglMcuDadaPA())){errors.reject("", "tanggal medis pada rontgen dada");}
				}
				for(int a=0;a<ws.getListTreadmill().size();a++) {
					if("".equals(ws.getListTreadmill().get(a).getTmpMcuTreadmill())){errors.reject("", "tempat medis pada treadmill");}
					if("".equals(ws.getListTreadmill().get(a).getTglMcuTreadmill())){errors.reject("", "tanggal medis pada treadmill");}
				}
			}else if("H".equals(ws.getJenisMedis())){//LPK 2 dokter yang berbeda + urin lengkap + adal + anti hiv test + cea + rontgen dada(Chest X-Ray) + Treadmill test
				for(int a=0;a<ws.getListLpk().size();a++) {
					if("".equals(ws.getListLpk().get(a).getTmpMcuLpk())){errors.reject("", "tempat medis pada LPK");}
					if("".equals(ws.getListLpk().get(a).getTglMcuLpk())){errors.reject("", "tanggal medis pada LPK");}
				}
				for(int a=0;a<ws.getListUrin().size();a++) {
					if("".equals(ws.getListUrin().get(a).getTmpMcuUrin())){errors.reject("", "tempat medis pada urin warna");}
					if("".equals(ws.getListUrin().get(a).getTglMcuUrin())){errors.reject("", "tanggal medis pada urin warna");}
				}
				for(int a=0;a<ws.getListAda().size();a++) {
					if("".equals(ws.getListAda().get(a).getTmpMcuAda())){errors.reject("", "tempat medis pada ADAL");}
					if("".equals(ws.getListAda().get(a).getTglMcuAda())){errors.reject("", "tanggal medis pada ADAL");}
				}
				for(int a=0;a<ws.getListHiv().size();a++) {
					if("".equals(ws.getListHiv().get(a).getTmpMcuHiv())){errors.reject("", "tempat medis pada anti HIV test");}
					if("".equals(ws.getListHiv().get(a).getTglMcuHiv())){errors.reject("", "tanggal medis pada anti HIV test");}
				}
//				for(int a=0;a<ws.getListCea().size();a++) {
//					if("".equals(ws.getListHiv().get(a).getTmpMcuHiv())){errors.reject("", "tempat medis pada anti HIV test");}
//					if("".equals(ws.getListHiv().get(a).getTglMcuHiv())){errors.reject("", "tanggal medis pada anti HIV test");}
//				}
				for(int a=0;a<ws.getListDadaPa().size();a++) {
					if("".equals(ws.getListDadaPa().get(a).getTmpMcuDadaPA())){errors.reject("", "tempat medis pada rontgen dada");}
					if("".equals(ws.getListDadaPa().get(a).getTglMcuDadaPA())){errors.reject("", "tanggal medis pada rontgen dada");}
				}
				for(int a=0;a<ws.getListTreadmill().size();a++) {
					if("".equals(ws.getListTreadmill().get(a).getTmpMcuTreadmill())){errors.reject("", "tempat medis pada treadmill");}
					if("".equals(ws.getListTreadmill().get(a).getTglMcuTreadmill())){errors.reject("", "tanggal medis pada treadmill");}
				}
			}
		}
		/*else if(ws.getSubmitMode().equals("medicalType")) {
			String[] medisType = ServletRequestUtils.getStringParameter(request, "medicalType","").split("_");
			//logger.info("medis type : "+medisType[0]+" - "+medisType[1]);
			if(medisType[0].equals("lpk")) {
				Map nvDenyutNadi = uwManager.selectRujukanMedis(ws.getListLpk().get(Integer.parseInt(medisType[1])).getTmpMcuLpk(),"denyut nadi",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvDenyutNadi != null) {
					String satuan = nvDenyutNadi.get("SATUAN") == null ? "" : " "+nvDenyutNadi.get("SATUAN").toString();
					ws.getListLpk().get(Integer.parseInt(medisType[1])).setNv_lpk_deyut_nadi(nvDenyutNadi.get("NILAI").toString()+satuan);
				}
			}
			else if(medisType[0].equals("urin")) {
				Map nvWarna = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"warna",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvWarna != null) {
					String satuan = nvWarna.get("SATUAN") == null ? "" : " "+nvWarna.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_warna(nvWarna.get("NILAI").toString()+satuan);
				}
				Map nvKejernihan = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"kejernihan",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvKejernihan != null) {
					String satuan = nvKejernihan.get("SATUAN") == null ? "" : " "+nvKejernihan.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_kejernihan(nvKejernihan.get("NILAI").toString()+satuan);
				}
				Map nvBrtJns = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"berat jenis",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvBrtJns != null) {
					String satuan = nvBrtJns.get("SATUAN") == null ? "" : " "+nvBrtJns.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_berat_jenis(nvBrtJns.get("NILAI").toString()+satuan);
				}
				Map nvPH = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"ph",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvPH != null) {
					String satuan = nvPH.get("SATUAN") == null ? "" : " "+nvPH.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_ph(nvPH.get("NILAI").toString()+satuan);
				}
				Map nvProtein = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"protein",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvProtein != null) {
					String satuan = nvProtein.get("SATUAN") == null ? "" : " "+nvProtein.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_protein(nvProtein.get("NILAI").toString()+satuan);
				}
				Map nvGlukosa = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"glukosa",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvGlukosa != null) {
					String satuan = nvGlukosa.get("SATUAN") == null ? "" : " "+nvGlukosa.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_glukosa(nvGlukosa.get("NILAI").toString()+satuan);
				}
				Map nvKeton = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"keton",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvKeton != null) {
					String satuan = nvKeton.get("SATUAN") == null ? "" : " "+nvKeton.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_keton(nvKeton.get("NILAI").toString()+satuan);
				}
				Map nvBilirubin = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"bilirubin",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvBilirubin != null) {
					String satuan = nvBilirubin.get("SATUAN") == null ? "" : " "+nvBilirubin.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_bilirubin(nvBilirubin.get("NILAI").toString()+satuan);
				}
				Map nvUrobilinogen = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"urobilinogen",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvUrobilinogen != null) {
					String satuan = nvUrobilinogen.get("SATUAN") == null ? "" : " "+nvUrobilinogen.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_urobilinogen(nvUrobilinogen.get("NILAI").toString()+satuan);
				}
				Map nvNitrit = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"nitrit",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvNitrit != null) {
					String satuan = nvNitrit.get("SATUAN") == null ? "" : " "+nvNitrit.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_nitrit(nvNitrit.get("NILAI").toString()+satuan);
				}
				Map nvDrhSmr = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"darah samar",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvDrhSmr != null) {
					String satuan = nvDrhSmr.get("SATUAN") == null ? "" : " "+nvDrhSmr.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_darah_samar(nvDrhSmr.get("NILAI").toString()+satuan);
				}
				Map nvLeuEsterase = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"leukosit esterase",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvLeuEsterase != null) {
					String satuan = nvLeuEsterase.get("SATUAN") == null ? "" : " "+nvLeuEsterase.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_lekosit_esterase(nvLeuEsterase.get("NILAI").toString()+satuan);
				}
				Map nvSedEri = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"sedimen eritrosit",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvSedEri != null) {
					String satuan = nvSedEri.get("SATUAN") == null ? "" : " "+nvSedEri.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_kristal(nvSedEri.get("NILAI").toString()+satuan);
				}
				Map nvSedLeu = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"sedimen leukosit",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvSedLeu != null) {
					String satuan = nvSedLeu.get("SATUAN") == null ? "" : " "+nvSedLeu.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_lekosit(nvSedLeu.get("NILAI").toString()+satuan);
				}
				Map nvSedEpi = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"sedimen epitel",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvSedEpi != null) {
					String satuan = nvSedEpi.get("SATUAN") == null ? "" : " "+nvSedEpi.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_epitel(nvSedEpi.get("NILAI").toString()+satuan);
				}
				Map nvSedSil = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"sedeimen silinder",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvSedSil != null) {
					String satuan = nvSedSil.get("SATUAN") == null ? "" : " "+nvSedSil.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_silinder(nvSedSil.get("NILAI").toString()+satuan);
				}
				Map nvSedKris = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"sedimen kristal",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvSedKris != null) {
					String satuan = nvSedKris.get("SATUAN") == null ? "" : " "+nvSedKris.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_kristal(nvSedKris.get("NILAI").toString()+satuan);
				}
				Map nvBakteri = uwManager.selectRujukanMedis(ws.getListUrin().get(Integer.parseInt(medisType[1])).getTmpMcuUrin(),"bakteri",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvBakteri != null) {
					String satuan = nvBakteri.get("SATUAN") == null ? "" : " "+nvBakteri.get("SATUAN").toString();
					ws.getListUrin().get(Integer.parseInt(medisType[1])).setNv_urin_bakteri(nvBakteri.get("NILAI").toString()+satuan);
				}
			}
			else if(medisType[0].equals("ada")) {
				Map nvHemo = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"anti hiv",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvHemo != null) {
					String satuan = nvHemo.get("SATUAN") == null ? "" : " "+nvHemo.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_hemoglobin(nvHemo.get("NILAI").toString()+satuan);
				}
				Map nvLeu = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"leukosit",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvLeu != null) {
					String satuan = nvLeu.get("SATUAN") == null ? "" : " "+nvLeu.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_leukosit(nvLeu.get("NILAI").toString()+satuan);
				}
				Map nvEos = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"eosinofil",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvEos != null) {
					String satuan = nvEos.get("SATUAN") == null ? "" : " "+nvEos.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_eosinofil(nvEos.get("NILAI").toString()+satuan);
				}
				Map nvBas = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"basofil",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvBas != null) {
					String satuan = nvBas.get("SATUAN") == null ? "" : " "+nvBas.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_basofil(nvBas.get("NILAI").toString()+satuan);
				}
				Map nvNeuBtg = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"neutrofil batang",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvNeuBtg != null) {
					String satuan = nvNeuBtg.get("SATUAN") == null ? "" : " "+nvNeuBtg.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_neutrofil_batang(nvNeuBtg.get("NILAI").toString()+satuan);
				}
				Map nvNeuSeg = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"neutrofil segmen",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvNeuSeg != null) {
					String satuan = nvNeuSeg.get("SATUAN") == null ? "" : " "+nvNeuSeg.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_neutrofil_segmen(nvNeuSeg.get("NILAI").toString()+satuan);
				}
				Map nvLimfo = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"limfosit",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvLimfo != null) {
					String satuan = nvLimfo.get("SATUAN") == null ? "" : " "+nvLimfo.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_limfosit(nvLimfo.get("NILAI").toString()+satuan);
				}
				Map nvMono = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"monosit",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvMono != null) {
					String satuan = nvMono.get("SATUAN") == null ? "" : " "+nvMono.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_monosit(nvMono.get("NILAI").toString()+satuan);
				}
				Map nvTrombo = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"trombosit",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvTrombo != null) {
					String satuan = nvTrombo.get("SATUAN") == null ? "" : " "+nvTrombo.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_trombosit(nvTrombo.get("NILAI").toString()+satuan);
				}
				Map nvEritro = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"eritrosit",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvEritro != null) {
					String satuan = nvEritro.get("SATUAN") == null ? "" : " "+nvEritro.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_eritrosit(nvEritro.get("NILAI").toString()+satuan);
				}
				Map nvHema = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"hematokrit",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvHema != null) {
					String satuan = nvHema.get("SATUAN") == null ? "" : " "+nvHema.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_hematokrit(nvHema.get("NILAI").toString()+satuan);
				}
				Map nvLed = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"led",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvLed != null) {
					String satuan = nvLed.get("SATUAN") == null ? "" : " "+nvLed.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_led(nvLed.get("NILAI").toString()+satuan);
				}
				Map nvMcv = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"mcv",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvMcv != null) {
					String satuan = nvMcv.get("SATUAN") == null ? "" : " "+nvMcv.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_mcv(nvMcv.get("NILAI").toString()+satuan);
				}
				Map nvMch = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"mch",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvMch != null) {
					String satuan = nvMch.get("SATUAN") == null ? "" : " "+nvMch.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_mch(nvMch.get("NILAI").toString()+satuan);
				}
				Map nvMchc = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"mchc",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvMchc != null) {
					String satuan = nvMchc.get("SATUAN") == null ? "" : " "+nvMchc.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_mchc(nvMchc.get("NILAI").toString()+satuan);
				}
				Map nvRdw = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"rdw",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvRdw != null) {
					String satuan = nvRdw.get("SATUAN") == null ? "" : " "+nvRdw.get("SATUAN").toString();
					ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_darah_rutin_rdw(nvRdw.get("NILAI").toString()+satuan);
				}
				if(ws.getMsw_medis() == 1) {
					Map nvGdp = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"gula darah puasa",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvGdp != null) {
						String satuan = nvGdp.get("SATUAN") == null ? "" : " "+nvGdp.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_gula_darah_glukosa_darah_puasa(nvGdp.get("NILAI").toString()+satuan);
					}
					Map nvGlukosa = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"glukosa 2 jam pp",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvGlukosa != null) {
						String satuan = nvGlukosa.get("SATUAN") == null ? "" : " "+nvGlukosa.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_gula_darah_glukosa_pp(nvGlukosa.get("NILAI").toString()+satuan);
					}
					Map nvHba1c = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"hba1c",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvHba1c != null) {
						String satuan = nvHba1c.get("SATUAN") == null ? "" : " "+nvHba1c.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_gula_darah_glukosa_hba1c(nvHba1c.get("NILAI").toString()+satuan);
					}
					Map nvTtlChole = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"total cholesterol",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvTtlChole != null) {
						String satuan = nvTtlChole.get("SATUAN") == null ? "" : " "+nvTtlChole.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_lemak_darah_total_cholesterol(nvTtlChole.get("NILAI").toString()+satuan);
					}
					Map nvHdlChole = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"hdl cholesterol",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvHdlChole != null) {
						String satuan = nvHdlChole.get("SATUAN") == null ? "" : " "+nvHdlChole.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_lemak_darah_hdl_cholesterol(nvHdlChole.get("NILAI").toString()+satuan);
					}
					Map nvLdlChole = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"ldl cholesterol",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvLdlChole != null) {
						String satuan = nvLdlChole.get("SATUAN") == null ? "" : " "+nvLdlChole.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_lemak_darah_ldl_cholesterol(nvLdlChole.get("NILAI").toString()+satuan);
					}
					Map nvTrigli = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"trigliserida",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvTrigli != null) {
						String satuan = nvTrigli.get("SATUAN") == null ? "" : " "+nvTrigli.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_lemak_darah_trigliserida(nvTrigli.get("NILAI").toString()+satuan);
					}
					Map nvSgot = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"sgot",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvSgot != null) {
						String satuan = nvSgot.get("SATUAN") == null ? "" : " "+nvSgot.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_hati_sgot_ast(nvSgot.get("NILAI").toString()+satuan);
					}
					Map nvSgpt = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"sgpt",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvSgpt != null) {
						String satuan = nvSgpt.get("SATUAN") == null ? "" : " "+nvSgpt.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_hati_sgpt_alt(nvSgpt.get("NILAI").toString()+satuan);
					}
					Map nvGgt = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"ggt",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvGgt != null) {
						String satuan = nvGgt.get("SATUAN") == null ? "" : " "+nvGgt.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_hati_gamma_gt_ggtp(nvGgt.get("NILAI").toString()+satuan);
					}
					Map nvFosAlkali = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"fosfatase alkali",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvFosAlkali != null) {
						String satuan = nvFosAlkali.get("SATUAN") == null ? "" : " "+nvFosAlkali.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_hati_alkali_fosfatase(nvFosAlkali.get("NILAI").toString()+satuan);
					}
					Map nvBilDirek = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"bilirubin direk",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvBilDirek != null) {
						String satuan = nvBilDirek.get("SATUAN") == null ? "" : " "+nvBilDirek.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_hati_bilirubin_direk(nvBilDirek.get("NILAI").toString()+satuan);
					}
					Map nvBilIndirek = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"bilirubin indirek",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvBilIndirek != null) {
						String satuan = nvBilIndirek.get("SATUAN") == null ? "" : " "+nvBilIndirek.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_hati_bilirubin_indirek(nvBilIndirek.get("NILAI").toString()+satuan);
					}
					Map nvBilTotal = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"bilirubin total",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvBilTotal != null) {
						String satuan = nvBilTotal.get("SATUAN") == null ? "" : " "+nvBilTotal.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_hati_bilirubin_total(nvBilTotal.get("NILAI").toString()+satuan);
					}
					Map nvAlbumin = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"albumin",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvAlbumin != null) {
						String satuan = nvAlbumin.get("SATUAN") == null ? "" : " "+nvAlbumin.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_hati_albumin(nvAlbumin.get("NILAI").toString()+satuan);
					}
					Map nvGlobulin = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"globulin",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvGlobulin != null) {
						String satuan = nvGlobulin.get("SATUAN") == null ? "" : " "+nvGlobulin.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_hati_globulin(nvGlobulin.get("NILAI").toString()+satuan);
					}
					Map nvTtlProt = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"total protein",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvTtlProt != null) {
						String satuan = nvTtlProt.get("SATUAN") == null ? "" : " "+nvTtlProt.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_hati_total_protein(nvTtlProt.get("NILAI").toString()+satuan);
					}
					Map nvHbsAg = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"hbsag",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvHbsAg != null) {
						String satuan = nvHbsAg.get("SATUAN") == null ? "" : " "+nvHbsAg.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_hepatitis_hbsag(nvHbsAg.get("NILAI").toString()+satuan);
					}
					Map nvHbeAg = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"hbeag",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvHbeAg != null) {
						String satuan = nvHbeAg.get("SATUAN") == null ? "" : " "+nvHbeAg.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_hepatitis_hbeag(nvHbeAg.get("NILAI").toString()+satuan);
					}
					Map nvCreatinin = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"creatinine",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvCreatinin != null) {
						String satuan = nvCreatinin.get("SATUAN") == null ? "" : " "+nvCreatinin.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_ginjal_creatinin(nvCreatinin.get("NILAI").toString()+satuan);
					}
					Map nvUreum = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"ureum",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvUreum != null) {
						String satuan = nvUreum.get("SATUAN") == null ? "" : " "+nvUreum.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_ginjal_ureum(nvUreum.get("NILAI").toString()+satuan);
					}
					Map nvAsamUrat = uwManager.selectRujukanMedis(ws.getListAda().get(Integer.parseInt(medisType[1])).getTmpMcuAda(),"asam urat",ws.getMsw_sex(),ws.getUmur(),"tahun");
					if(nvAsamUrat != null) {
						String satuan = nvAsamUrat.get("SATUAN") == null ? "" : " "+nvAsamUrat.get("SATUAN").toString();
						ws.getListAda().get(Integer.parseInt(medisType[1])).setNv_fungsi_ginjal_asam_urat(nvAsamUrat.get("NILAI").toString()+satuan);
					}
				}
			}
			else if(medisType[0].equals("hiv")) {
				Map nvHiv = uwManager.selectRujukanMedis(ws.getListHiv().get(Integer.parseInt(medisType[1])).getTmpMcuHiv(),"anti hiv",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvHiv != null) {
					String satuan = nvHiv.get("SATUAN") == null ? "" : " "+nvHiv.get("SATUAN").toString();
					ws.getListHiv().get(Integer.parseInt(medisType[1])).setNv_anti_hiv_anti_hiv(nvHiv.get("NILAI").toString()+satuan);
				}
			}
			else if(medisType[0].equals("tumor")) {
				Map nvCea = uwManager.selectRujukanMedis(ws.getListTumor().get(Integer.parseInt(medisType[1])).getTmpMcuTumor(),"cea",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvCea != null) {
					String satuan = nvCea.get("SATUAN") == null ? "" : " "+nvCea.get("SATUAN").toString();
					ws.getListTumor().get(Integer.parseInt(medisType[1])).setNv_tumor_marker_cea(nvCea.get("NILAI").toString()+satuan);
				}
				Map nvAfp = uwManager.selectRujukanMedis(ws.getListTumor().get(Integer.parseInt(medisType[1])).getTmpMcuTumor(),"afp",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvAfp != null) {
					String satuan = nvAfp.get("SATUAN") == null ? "" : " "+nvAfp.get("SATUAN").toString();
					ws.getListTumor().get(Integer.parseInt(medisType[1])).setNv_tumor_marker_afp(nvAfp.get("NILAI").toString()+satuan);
				}
				Map nvPsa = uwManager.selectRujukanMedis(ws.getListTumor().get(Integer.parseInt(medisType[1])).getTmpMcuTumor(),"psa (untuk pria)",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvPsa != null) {
					String satuan = nvPsa.get("SATUAN") == null ? "" : " "+nvPsa.get("SATUAN").toString();
					ws.getListTumor().get(Integer.parseInt(medisType[1])).setNv_tumor_marker_psa(nvPsa.get("NILAI").toString()+satuan);
				}
				Map nvCa15 = uwManager.selectRujukanMedis(ws.getListTumor().get(Integer.parseInt(medisType[1])).getTmpMcuTumor(),"ca 15-3 (untuk wanita)",ws.getMsw_sex(),ws.getUmur(),"tahun");
				if(nvCa15 != null) {
					String satuan = nvCa15.get("SATUAN") == null ? "" : " "+nvCa15.get("SATUAN").toString();
					ws.getListTumor().get(Integer.parseInt(medisType[1])).setNv_tumor_marker_ca_15_3(nvCa15.get("NILAI").toString()+satuan);
				}
			}
			errors.reject("", "perubahan normal value untuk medis "+medisType[0]);
		}*/
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException errors) throws Exception {
		//logger.info("msk onSubmit");
		UwWorkSheet ws = (UwWorkSheet) cmd;
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		ws.setMsw_lus_id(Integer.parseInt(currentUser.getLus_id()));
		for(int a=0;a<ws.getListQuest().size();a++) {
			ws.getListQuest().get(a).setReg_spaj(ws.getReg_spaj());
			ws.getListQuest().get(a).setInsured_no(ws.getInsured_no());
			ws.getListQuest().get(a).setUrut(a+1);
		}
		for(int a=0;a<ws.getListUwDec().size();a++) {
			ws.getListUwDec().get(a).setReg_spaj(ws.getReg_spaj());
			ws.getListUwDec().get(a).setInsured_no(ws.getInsured_no());
			ws.getListUwDec().get(a).setMwd_urut(a+1);
			if(ws.getListUwDec().get(a).getLus_id() == null) ws.getListUwDec().get(a).setLus_id(currentUser.getLus_id());
			
			//tambahan untuk menyesuaikan/mengisi spaj
			for(int i=0 ; i < ws.getListUwDec().get(a).getRider().size() ; i++) {
				ws.getListUwDec().get(a).getRider().get(i).setReg_spaj(ws.getReg_spaj());
			}
		}
		
		if(ws.getSubmitMode().equals("transfer_uw1")) ws.setPosition(1);
		else if(ws.getSubmitMode().equals("transfer_uw2")) ws.setPosition(2);
		else if(ws.getSubmitMode().equals("reverse_staff")) ws.setPosition(0);
		else if(ws.getSubmitMode().equals("reverse_uw1")) ws.setPosition(1);
		
		map.put("success", uwManager.prosesUwWorksheet(ws));
		map.put("spaj", ws.getReg_spaj());
		map.put("no", ws.getInsured_no());
		
		if(ws.getSubmitMode().equals("saveWorksheet"))
			elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "Save UW Worksheet" , ws.getReg_spaj(), 0);
		else if(ws.getSubmitMode().equals("transfer_uw1"))
			elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "Transfer UW 1" , ws.getReg_spaj(), 0);
		else if(ws.getSubmitMode().equals("transfer_uw2"))
			elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "Transfer UW 2" , ws.getReg_spaj(), 0);
		else if(ws.getSubmitMode().equals("reverse_staff"))
			elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "Reverse Staff" , ws.getReg_spaj(), 0);
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/uw/worksheet.htm")).addAllObjects(map);
	}
	
	/**
	 * Fungsi untuk buang rider2 yg tidak diperlukan
	 * @param data
	 * @return
	 *
	 * @author Yusup_A
	 * @since May 26, 2010 (11:17:19 AM)
	 */
	private List<SortedMap> sortingPlan(List<SortedMap> data) {
		Integer size = data.size();
		for(int a=size-1;a>=0;a--) {
			if(data.get(a).get("mste_insured_no").equals("1")) {
				if((Integer) data.get(a).get("O") == 900 || (Integer) data.get(a).get("O") == 907) data.remove(a);
			}
			else if(data.get(a).get("mste_insured_no").equals("2")) data.remove(a);
		}
		
		return data;
	}
	
	/**
	 * Fungsi untuk memposisikan plan utama di urutan pertama
	 * @param data
	 * @return
	 *
	 * @author Andy
	 * @since February 09, 2012 (11:17:19 AM)
	 */
	private List<SortedMap> sortingPlanUtama(List<SortedMap> data) {
		Integer size = data.size();
		List<SortedMap> dataTemp = null;
		for(int a=0;a<size;a++) {
			if(a==0){
				dataTemp = new ArrayList<SortedMap>(data);
			}
			else{ 
				if(Integer.parseInt(data.get(a).get("O").toString()) <= 203) {
					data.set(a, dataTemp.get(0));
					data.set(0, dataTemp.get(a));
				}
			}
		}
		
		return data;
	}
	
	/**
	 * Fungsi untuk menampilkan semua hasil inputan worksheet yg tersimpan di DB
	 * @param ws
	 * @param df
	 * @param hitUmur
	 * @param sysDate
	 * @param jumlProd
	 * @throws java.text.ParseException
	 *
	 * @author Yusup_A
	 * @since May 26, 2010 (11:18:04 AM)
	 */
	private UwWorkSheet inputAllDataWorksheet(UwWorkSheet wsTemp, SimpleDateFormat df, f_hit_umur hitUmur, String sysDate, List<SortedMap> jumlProd) throws java.text.ParseException {
		UwWorkSheet temp = uwManager.getUwWorksheet(wsTemp.getReg_spaj(), wsTemp.getInsured_no());
		if(temp != null) {
			wsTemp.setMsw_medis(temp.getMsw_medis());
			wsTemp.setModeSim(temp.getModeSim());
			wsTemp.setMsw_lus_id(temp.getMsw_lus_id());
			wsTemp.setMcl_id(temp.getMcl_id());
			wsTemp.setNonMedisTb(temp.getNonMedisTb());
			wsTemp.setNonMedisBb(temp.getNonMedisBb());
			wsTemp.setNonMedisBmiKelainan(temp.getNonMedisBmiKelainan());
			wsTemp.setNonMedisOccupation(temp.getNonMedisOccupation());
			wsTemp.setNonMedisOccDesc(temp.getNonMedisOccDesc());
			wsTemp.setNonMedisHabits(temp.getNonMedisHabits());
			wsTemp.setNonMedisHabitsKelainan(temp.getNonMedisHabitsKelainan());
			wsTemp.setNonMedisHobby(temp.getNonMedisHobby());
			wsTemp.setNonMedisHobbyDesc(temp.getNonMedisHobbyDesc());
			wsTemp.setNonMedisKelainan(temp.getNonMedisKelainan());
			wsTemp.setNonMedisKelainanKelainan(temp.getNonMedisKelainanKelainan());
			wsTemp.setEmOcc(uwManager.selectEmWorksheet(wsTemp.getNonMedisOccupation()));
			wsTemp.setEmHobby(uwManager.selectEmWorksheet(wsTemp.getNonMedisHobby()));
			wsTemp.setSumMCU(temp.getSumMCU());
			wsTemp.setJenisMedis(temp.getJenisMedis());
			wsTemp.setPosition(temp.getPosition());
		}
		
		List<HashMap> dataPeserta = uwManager.selectDaftarPeserta(wsTemp.getReg_spaj());
		Integer index = 0;
		if(dataPeserta.size() != 1){
			//index = wsTemp.getInsured_no(); 
			for(int p = 0 ; p < dataPeserta.size() ; p++){
				if(Integer.parseInt(dataPeserta.get(p).get("INSURED_NO").toString()) == wsTemp.getInsured_no()){
					index = p;
				}
				
			}
			
		}
		String birthDate = df.format(dataPeserta.get(index).get("MSPE_DATE_BIRTH"));
		wsTemp.setBirthDate(birthDate);
		wsTemp.setNamaPeserta(dataPeserta.get(index).get("MCL_FIRST").toString());
		wsTemp.setSex(dataPeserta.get(index).get("KELAMIN").toString());
		wsTemp.setUmur(hitUmur.umur(Integer.parseInt(birthDate.substring(6)),Integer.parseInt(birthDate.substring(3,5)) , Integer.parseInt(birthDate.substring(0,2)), Integer.parseInt(sysDate.substring(6)),Integer.parseInt(sysDate.substring(3,5)) , Integer.parseInt(sysDate.substring(0,2))));
		if(wsTemp.getNonMedisTb() != null && wsTemp.getNonMedisBb() != null) {
			BigDecimal height =  new BigDecimal(wsTemp.getNonMedisTb()).divide(new BigDecimal(100)).pow(2);
			BigDecimal bmi = new BigDecimal(wsTemp.getNonMedisBb()).divide(height,0,RoundingMode.HALF_UP);
			wsTemp.setNonMedisBmi(bmi.doubleValue());
			wsTemp.setEmBmi(uwManager.selectEmBmi(wsTemp.getNonMedisBmi(),wsTemp.getUmur()));				
		}

		if(wsTemp.getMsw_medis() == 0) {
			List<UwQuestionnaire> uq = uwManager.getListQuestionnaire(wsTemp.getReg_spaj(), wsTemp.getInsured_no());
			if(uq.size() != 0) wsTemp.setListQuest(uq);
		}
		else if(wsTemp.getMsw_medis() == 1) {
			List<UwLpk> uk = uwManager.getListLpk(wsTemp.getReg_spaj(), wsTemp.getInsured_no());
			if(uk.size() != 0) {
				for(int a=0;a<uk.size();a++) {
					if(uk.get(a).getLpkTb() != null && uk.get(a).getLpkBb() != null) {
						BigDecimal height =  new BigDecimal(uk.get(a).getLpkTb()).divide(new BigDecimal(100)).pow(2);
						BigDecimal bmi = new BigDecimal(uk.get(a).getLpkBb()).divide(height,0,RoundingMode.HALF_UP);
						uk.get(a).setLpkBmi(bmi.doubleValue());
						uk.get(a).setEmBmiLpk(uwManager.selectEmBmi(uk.get(a).getLpkBmi(),wsTemp.getUmur()));
					}
					if(uk.get(a).getLpkSystolic() != null && uk.get(a).getLpkDiastolic() != null) {
						uk.get(a).setEmBloodPresure(uwManager.selectEmBloodPresure(uk.get(a).getLpkSystolic(),uk.get(a).getLpkDiastolic(),wsTemp.getUmur()));
					}
					
					List<HashMap> ur = uwManager.getListRiwPenyakit(wsTemp.getReg_spaj(), wsTemp.getInsured_no(),a+1,1);
					List<UwRiwPenyakit> urp = new ArrayList<UwRiwPenyakit>();
					for(int b=0;b<ur.size();b++) {
						UwRiwPenyakit urk = new UwRiwPenyakit(1);
						urk.setReg_spaj(ur.get(b).get("REG_SPAJ").toString());
						urk.setInsured_no(Integer.parseInt(ur.get(b).get("INSURED_NO").toString()));
						urk.setUrutanLpk(Integer.parseInt(ur.get(b).get("URUTAN_LPK").toString()));
						urk.setRp_desc(ur.get(b).get("RP_DESC").toString());
						urk.setRp_type(Integer.parseInt(ur.get(b).get("RP_TYPE").toString()));
						urk.setRp_urutan(Integer.parseInt(ur.get(b).get("RP_URUTAN").toString()));
						
						urp.add(urk);
					}
					uk.get(a).setListRpd(urp);  
					
					ur = uwManager.getListRiwPenyakit(wsTemp.getReg_spaj(), wsTemp.getInsured_no(),a+1,2);
					urp = new ArrayList<UwRiwPenyakit>();
					for(int b=0;b<ur.size();b++) {
						UwRiwPenyakit urk = new UwRiwPenyakit(2);
						urk.setReg_spaj(ur.get(b).get("REG_SPAJ").toString());
						urk.setInsured_no(Integer.parseInt(ur.get(b).get("INSURED_NO").toString()));
						urk.setUrutanLpk(Integer.parseInt(ur.get(b).get("URUTAN_LPK").toString()));
						urk.setRp_desc(ur.get(b).get("RP_DESC").toString());
						urk.setRp_type(Integer.parseInt(ur.get(b).get("RP_TYPE").toString()));
						urk.setRp_urutan(Integer.parseInt(ur.get(b).get("RP_URUTAN").toString()));
						
						urp.add(urk);
					}
					uk.get(a).setListRps(urp); 
					
					ur = uwManager.getListRiwPenyakit(wsTemp.getReg_spaj(), wsTemp.getInsured_no(),a+1,3);
					urp = new ArrayList<UwRiwPenyakit>();
					for(int b=0;b<ur.size();b++) {
						UwRiwPenyakit urk = new UwRiwPenyakit(3);
						urk.setReg_spaj(ur.get(b).get("REG_SPAJ").toString());
						urk.setInsured_no(Integer.parseInt(ur.get(b).get("INSURED_NO").toString()));
						urk.setUrutanLpk(Integer.parseInt(ur.get(b).get("URUTAN_LPK").toString()));
						urk.setRp_desc(ur.get(b).get("RP_DESC").toString());
						urk.setRp_type(Integer.parseInt(ur.get(b).get("RP_TYPE").toString()));
						urk.setRp_urutan(Integer.parseInt(ur.get(b).get("RP_URUTAN").toString()));
						
						urp.add(urk);
					}
					uk.get(a).setListRpk(urp);
					
					ur = uwManager.getListRiwPenyakit(wsTemp.getReg_spaj(), wsTemp.getInsured_no(),a+1,4);
					urp = new ArrayList<UwRiwPenyakit>();
					for(int b=0;b<ur.size();b++) {
						UwRiwPenyakit urk = new UwRiwPenyakit(4);
						urk.setReg_spaj(ur.get(b).get("REG_SPAJ").toString());
						urk.setInsured_no(Integer.parseInt(ur.get(b).get("INSURED_NO").toString()));
						urk.setUrutanLpk(Integer.parseInt(ur.get(b).get("URUTAN_LPK").toString()));
						urk.setRp_desc(ur.get(b).get("RP_DESC").toString());
						urk.setRp_type(Integer.parseInt(ur.get(b).get("RP_TYPE").toString()));
						urk.setRp_urutan(Integer.parseInt(ur.get(b).get("RP_URUTAN").toString()));
						
						urp.add(urk);
					}
					uk.get(a).setListKelainan(urp);
				}
				wsTemp.setListLpk(uk);	
			}
			List<UwUrin> ur = uwManager.getListUrin(wsTemp.getReg_spaj(), wsTemp.getInsured_no());
			wsTemp.setListUrin(uwManager.getListUrin(wsTemp.getReg_spaj(), wsTemp.getInsured_no()));
			wsTemp.setListAda(uwManager.getListAda(wsTemp.getReg_spaj(), wsTemp.getInsured_no()));
			for(int a=0;a<wsTemp.getListAda().size();a++) {
				if(wsTemp.getListAda().get(a).getTotal_cholesterol() != null && wsTemp.getListAda().get(a).getHdl_cholesterol() != null) {
					BigDecimal totChol = new BigDecimal(wsTemp.getListAda().get(a).getTotal_cholesterol());
					totChol = totChol.divide(new BigDecimal(wsTemp.getListAda().get(a).getHdl_cholesterol()),2,RoundingMode.HALF_UP);
					wsTemp.getListAda().get(a).setChol_hdl(totChol.doubleValue());
					wsTemp.getListAda().get(a).setRatio_cholesterol(uwManager.getRatioChol(wsTemp.getListAda().get(a).getSatuanChol(),wsTemp.getListAda().get(a).getTotal_cholesterol(),wsTemp.getListAda().get(a).getHdl_cholesterol(),wsTemp.getUmur()));
				}
				if(wsTemp.getListAda().get(a).getHdl_cholesterol() != null && wsTemp.getListAda().get(a).getLdl_cholesterol() != null) {
					BigDecimal ldl = new BigDecimal(wsTemp.getListAda().get(a).getLdl_cholesterol());
					ldl = ldl.divide(new BigDecimal(wsTemp.getListAda().get(a).getHdl_cholesterol()),2,RoundingMode.HALF_UP);
					wsTemp.getListAda().get(a).setLdl_hdl(ldl.doubleValue());
				}
			}
			wsTemp.setListHiv(uwManager.getListHiv(wsTemp.getReg_spaj(), wsTemp.getInsured_no()));
			wsTemp.setListTumor(uwManager.getListTumor(wsTemp.getReg_spaj(), wsTemp.getInsured_no()));
			wsTemp.setListAbdomen(uwManager.getListAbdomen(wsTemp.getReg_spaj(), wsTemp.getInsured_no()));
			wsTemp.setListDadaPa(uwManager.getListDadaPa(wsTemp.getReg_spaj(), wsTemp.getInsured_no()));
			wsTemp.setListEkg(uwManager.getListEkg(wsTemp.getReg_spaj(), wsTemp.getInsured_no()));
			wsTemp.setListTreadmill(uwManager.getListTreadmill(wsTemp.getReg_spaj(), wsTemp.getInsured_no()));
			wsTemp.setListMedLain(uwManager.getListMedLain(wsTemp.getReg_spaj(), wsTemp.getInsured_no()));
		}

		List<HashMap> dec = uwManager.getListUwDec(wsTemp.getReg_spaj(), wsTemp.getInsured_no());
		if(dec.size() != 0) {
			wsTemp.setListUwDec(new ArrayList<UwDecision>());
			for(int a=0;a<dec.size();a++) {
				UwDecision ud = new UwDecision(sysDate);
				ud.setRiderContent(jumlProd);
				wsTemp.getListUwDec().add(ud);
				
				wsTemp.getListUwDec().get(a).setReg_spaj(dec.get(a).get("REG_SPAJ").toString());
				wsTemp.getListUwDec().get(a).setInsured_no(Integer.parseInt(dec.get(a).get("INSURED_NO").toString()));
				wsTemp.getListUwDec().get(a).setMwd_urut(Integer.parseInt(dec.get(a).get("MWD_URUT").toString()));
				if(dec.get(a).get("PENYAKIT") != null) wsTemp.getListUwDec().get(a).setPenyakit(dec.get(a).get("PENYAKIT").toString());
				wsTemp.getListUwDec().get(a).setLsbs_id(Integer.parseInt(dec.get(a).get("LSBS_ID").toString()));
				wsTemp.getListUwDec().get(a).setLsdbs_number(Integer.parseInt(dec.get(a).get("LSDBS_NUMBER").toString()));
				if(dec.get(a).get("PROD_UTAMA_PERSEN") != null)wsTemp.getListUwDec().get(a).setProd_utama_persen(dec.get(a).get("PROD_UTAMA_PERSEN").toString());
				if(dec.get(a).get("PROD_UTAMA_PERMIL") != null)wsTemp.getListUwDec().get(a).setProd_utama_permil(dec.get(a).get("PROD_UTAMA_PERMIL").toString());
				if(dec.get(a).get("LIC_ID") != null) wsTemp.getListUwDec().get(a).setLic_id(dec.get(a).get("LIC_ID").toString());
				if(dec.get(a).get("LIC_DESC") != null) wsTemp.getListUwDec().get(a).setLic_desc(dec.get(a).get("LIC_DESC").toString());
				if(dec.get(a).get("URUTAN_PENYAKIT") != null) wsTemp.getListUwDec().get(a).setUrutan_penyakit(Integer.parseInt(dec.get(a).get("URUTAN_PENYAKIT").toString()));
				else wsTemp.getListUwDec().get(a).setUrutan_penyakit(null);
				if(dec.get(a).get("URUTAN_DECISION") != null) wsTemp.getListUwDec().get(a).setUrutan_decision(Integer.parseInt(dec.get(a).get("URUTAN_DECISION").toString()));
				else wsTemp.getListUwDec().get(a).setUrutan_decision(null);
				if(dec.get(a).get("CATATAN") != null) wsTemp.getListUwDec().get(a).setCatatan(dec.get(a).get("CATATAN").toString());
				wsTemp.getListUwDec().get(a).setLus_id(dec.get(a).get("LUS_ID").toString());
				wsTemp.getListUwDec().get(a).setInput_date(dec.get(a).get("INPUT_DATE").toString());
				// untuk menghandle rider nya berubah setelah worksheet disimpan
				for(int b=0;b<wsTemp.getListUwDec().get(a).getRider().size();b++) {
					UwDecisionRider udr = uwManager.getListWorkDecRiderById(dec.get(a).get("REG_SPAJ").toString(),Integer.parseInt(dec.get(a).get("INSURED_NO").toString()),Integer.parseInt(dec.get(a).get("MWD_URUT").toString()),wsTemp.getListUwDec().get(a).getRider().get(b).getLsbs_id(),wsTemp.getListUwDec().get(a).getRider().get(b).getLsdbs_number());
					if(udr != null) wsTemp.getListUwDec().get(a).getRider().set(b, udr);
				}
				//wsTemp.getListUwDec().get(a).setRider(uwManager.getListWorkDecRider(dec.get(a).get("REG_SPAJ").toString(),Integer.parseInt(dec.get(a).get("INSURED_NO").toString()),Integer.parseInt(dec.get(a).get("MWD_URUT").toString())));
				if(dec.get(a).get("URUTAN_PENYAKIT") != null) wsTemp.setTotalPenyakitUwDesc(Integer.parseInt(dec.get(a).get("URUTAN_PENYAKIT").toString()));
			}
		}			
		
		temp = uwManager.getFinancialStat(wsTemp.getReg_spaj(), wsTemp.getInsured_no());
		if(temp != null) {
			if(temp.getFlag_fs() != 0) wsTemp.setFlag_fs(temp.getFlag_fs());
			if(temp.getFs_date() != null) wsTemp.setFs_date(temp.getFs_date());
			if(temp.getFs_copy_rek_bank() != null) wsTemp.setFs_copy_rek_bank(temp.getFs_copy_rek_bank());
			if(temp.getFs_copy_npwp() != null) wsTemp.setFs_copy_npwp(temp.getFs_copy_npwp());
			if(temp.getFs_spt_pribadi() != null) wsTemp.setFs_spt_pribadi(temp.getFs_spt_pribadi());
			if(temp.getFs_copy_neraca_persh() != null) wsTemp.setFs_copy_neraca_persh(temp.getFs_copy_neraca_persh());
			if(temp.getFs_lain() != null) wsTemp.setFs_lain(temp.getFs_lain());
			wsTemp.setFs_lain_desc(temp.getFs_lain_desc());				
		}
		

		List<SortedMap> x, daftar1 = new ArrayList<SortedMap>();
		List<SortedMap> daftar2 = new ArrayList<SortedMap>();
		Boolean _2th = wsTemp.getModeSim() == 0 ? false : true;
		if(wsTemp.getInsured_no() == 0) wsTemp.setPptt(2);
		else wsTemp.setPptt(3);
		List<Map> filters = uwManager.selectFilterSpajNew(wsTemp.getPptt().toString(), wsTemp.getNamaPeserta(), new SimpleDateFormat("yyyyMMdd").format(df.parse(wsTemp.getBirthDate())), "LIKE%", _2th);

		for(int i=0; i<filters.size(); i++) {
			x = sortingPlanUtama(sortingPlan(uwManager.selectAkumulasiPolisBySpaj((String) filters.get(i).get("REG_SPAJ"))));
			for(SortedMap map : x) {
				String uw_info = uwManager.getKetMedis(map.get("M").toString());
				if(uw_info != null) map.put("D1", uw_info);
				if(map.get("L").toString().equals("1")) daftar1.add(map);
				else if(map.get("L").toString().equals("10")) daftar2.add(map);
			}
		}
		wsTemp.setListSimultan(daftar1);
		wsTemp.setListSimultanProc(daftar2);
		if(wsTemp.getJenisMedis() != null) wsTemp = changeMedis(wsTemp,1,false);	
		
		return wsTemp;
	}
	
	/**
	 * Fungsi untuk menampilkan copy worksheet yg tersimpan di DB (modified)
	 * @param ws
	 * @param df
	 * @param hitUmur
	 * @param sysDate
	 * @param jumlProd
	 * @throws java.text.ParseException
	 *
	 * @author Andy
	 * @since March 20, 2012 (11:18:04 AM)
	 */
	private UwWorkSheet copyDataWorksheet(UwWorkSheet wsTemp, String reg_spaj, Integer insuredNo, SimpleDateFormat df, f_hit_umur hitUmur, String sysDate, List<SortedMap> jumlProd) throws java.text.ParseException {
		UwWorkSheet temp = uwManager.getUwWorksheet(wsTemp.getReg_spaj(), wsTemp.getInsured_no());
		UwWorkSheet copyTemp = uwManager.getUwWorksheet(reg_spaj, insuredNo);//copy
		if(temp != null) {//worksheet exist
			wsTemp.setMsw_medis(copyTemp.getMsw_medis());//copy
			wsTemp.setModeSim(copyTemp.getModeSim());//copy
			wsTemp.setMsw_lus_id(temp.getMsw_lus_id());
			wsTemp.setMcl_id(temp.getMcl_id());
			wsTemp.setNonMedisTb(copyTemp.getNonMedisTb());//copy
			wsTemp.setNonMedisBb(copyTemp.getNonMedisBb());//copy
			wsTemp.setNonMedisBmiKelainan(copyTemp.getNonMedisBmiKelainan());//copy
			wsTemp.setNonMedisOccupation(copyTemp.getNonMedisOccupation());//copy
			wsTemp.setNonMedisOccDesc(copyTemp.getNonMedisOccDesc());//copy
			wsTemp.setNonMedisHabits(copyTemp.getNonMedisHabits());//copy
			wsTemp.setNonMedisHabitsKelainan(copyTemp.getNonMedisHabitsKelainan());//copy
			wsTemp.setNonMedisHobby(copyTemp.getNonMedisHobby());//copy
			wsTemp.setNonMedisHobbyDesc(copyTemp.getNonMedisHobbyDesc());//copy
			wsTemp.setNonMedisKelainan(copyTemp.getNonMedisKelainan());//copy
			wsTemp.setNonMedisKelainanKelainan(copyTemp.getNonMedisKelainanKelainan());//copy
			wsTemp.setEmOcc(uwManager.selectEmWorksheet(copyTemp.getNonMedisOccupation()));//wsTemp//copy
			wsTemp.setEmHobby(uwManager.selectEmWorksheet(copyTemp.getNonMedisHobby()));//wsTemp//copy
			wsTemp.setSumMCU(copyTemp.getSumMCU());//copy
			wsTemp.setJenisMedis(copyTemp.getJenisMedis());//copy
			wsTemp.setPosition(temp.getPosition());
		}else{//worksheet baru
			wsTemp.setMsw_medis(copyTemp.getMsw_medis());//copy
			wsTemp.setModeSim(copyTemp.getModeSim());//copy
			//wsTemp.setMsw_lus_id(temp.getMsw_lus_id());
			//wsTemp.setMcl_id(temp.getMcl_id());
			wsTemp.setNonMedisTb(copyTemp.getNonMedisTb());//copy
			wsTemp.setNonMedisBb(copyTemp.getNonMedisBb());//copy
			wsTemp.setNonMedisBmiKelainan(copyTemp.getNonMedisBmiKelainan());//copy
			wsTemp.setNonMedisOccupation(copyTemp.getNonMedisOccupation());//copy
			wsTemp.setNonMedisOccDesc(copyTemp.getNonMedisOccDesc());//copy
			wsTemp.setNonMedisHabits(copyTemp.getNonMedisHabits());//copy
			wsTemp.setNonMedisHabitsKelainan(copyTemp.getNonMedisHabitsKelainan());//copy
			wsTemp.setNonMedisHobby(copyTemp.getNonMedisHobby());//copy
			wsTemp.setNonMedisHobbyDesc(copyTemp.getNonMedisHobbyDesc());//copy
			wsTemp.setNonMedisKelainan(copyTemp.getNonMedisKelainan());//copy
			wsTemp.setNonMedisKelainanKelainan(copyTemp.getNonMedisKelainanKelainan());//copy
			wsTemp.setEmOcc(uwManager.selectEmWorksheet(copyTemp.getNonMedisOccupation()));//wsTemp//copy
			wsTemp.setEmHobby(uwManager.selectEmWorksheet(copyTemp.getNonMedisHobby()));//wsTemp//copy
			wsTemp.setSumMCU(copyTemp.getSumMCU());//copy
			wsTemp.setJenisMedis(copyTemp.getJenisMedis());//copy
			wsTemp.setPosition(0);
		}
		
		List<HashMap> dataPeserta = uwManager.selectDaftarPeserta(wsTemp.getReg_spaj());
		//List<HashMap> copyDataPeserta = uwManager.selectDaftarPeserta(reg_spaj);//copy
		Integer index = 0;
		if(dataPeserta.size() != 1){
			//index = wsTemp.getInsured_no(); 
			for(int p = 0 ; p < dataPeserta.size() ; p++){
				if(Integer.parseInt(dataPeserta.get(p).get("INSURED_NO").toString()) == wsTemp.getInsured_no()){
					index = p;
				}
				
			}
			
		}
		String birthDate = df.format(dataPeserta.get(index).get("MSPE_DATE_BIRTH"));
		wsTemp.setBirthDate(birthDate);
		wsTemp.setNamaPeserta(dataPeserta.get(index).get("MCL_FIRST").toString());
		wsTemp.setSex(dataPeserta.get(index).get("KELAMIN").toString());
		wsTemp.setUmur(hitUmur.umur(Integer.parseInt(birthDate.substring(6)),Integer.parseInt(birthDate.substring(3,5)) , Integer.parseInt(birthDate.substring(0,2)), Integer.parseInt(sysDate.substring(6)),Integer.parseInt(sysDate.substring(3,5)) , Integer.parseInt(sysDate.substring(0,2))));
		if(wsTemp.getNonMedisTb() != null && wsTemp.getNonMedisBb() != null) {
			BigDecimal height =  new BigDecimal(wsTemp.getNonMedisTb()).divide(new BigDecimal(100)).pow(2);
			BigDecimal bmi = new BigDecimal(wsTemp.getNonMedisBb()).divide(height,0,RoundingMode.HALF_UP);
			wsTemp.setNonMedisBmi(bmi.doubleValue());
			wsTemp.setEmBmi(uwManager.selectEmBmi(wsTemp.getNonMedisBmi(),wsTemp.getUmur()));				
		}

		if(wsTemp.getMsw_medis() == 0) {
			List<UwQuestionnaire> uq = uwManager.getListQuestionnaire(wsTemp.getReg_spaj(), wsTemp.getInsured_no());
			List<UwQuestionnaire> copyUq = uwManager.getListQuestionnaire(reg_spaj, insuredNo);//copy
			if(copyUq.size() != 0) wsTemp.setListQuest(copyUq);//copy
		}
		else if(wsTemp.getMsw_medis() == 1) {
			//List<UwLpk> uk = uwManager.getListLpk(wsTemp.getReg_spaj(), wsTemp.getInsured_no());
			List<UwLpk> copyUk = uwManager.getListLpk(reg_spaj, insuredNo);//copy
			if(copyUk.size() != 0) {//copy all
				for(int a=0;a<copyUk.size();a++) {
					copyUk.get(a).setReg_spaj(wsTemp.getReg_spaj());//copy ori
					if(copyUk.get(a).getLpkTb() != null && copyUk.get(a).getLpkBb() != null) {
						BigDecimal height =  new BigDecimal(copyUk.get(a).getLpkTb()).divide(new BigDecimal(100)).pow(2);
						BigDecimal bmi = new BigDecimal(copyUk.get(a).getLpkBb()).divide(height,0,RoundingMode.HALF_UP);
						copyUk.get(a).setLpkBmi(bmi.doubleValue());
						copyUk.get(a).setEmBmiLpk(uwManager.selectEmBmi(copyUk.get(a).getLpkBmi(),wsTemp.getUmur()));
					}
					if(copyUk.get(a).getLpkSystolic() != null && copyUk.get(a).getLpkDiastolic() != null) {
						copyUk.get(a).setEmBloodPresure(uwManager.selectEmBloodPresure(copyUk.get(a).getLpkSystolic(),copyUk.get(a).getLpkDiastolic(),wsTemp.getUmur()));
					}
					
					//List<HashMap> ur = uwManager.getListRiwPenyakit(wsTemp.getReg_spaj(), wsTemp.getInsured_no(),a+1,1);
					List<HashMap> copyUr = uwManager.getListRiwPenyakit(reg_spaj, insuredNo,a+1,1);//copy
					List<UwRiwPenyakit> urp = new ArrayList<UwRiwPenyakit>();
					for(int b=0;b<copyUr.size();b++) {// copy all
						UwRiwPenyakit urk = new UwRiwPenyakit(1);
						urk.setReg_spaj(wsTemp.getReg_spaj());//copy ori
						urk.setInsured_no(Integer.parseInt(copyUr.get(b).get("INSURED_NO").toString()));
						urk.setUrutanLpk(Integer.parseInt(copyUr.get(b).get("URUTAN_LPK").toString()));
						urk.setRp_desc(copyUr.get(b).get("RP_DESC").toString());
						urk.setRp_type(Integer.parseInt(copyUr.get(b).get("RP_TYPE").toString()));
						urk.setRp_urutan(Integer.parseInt(copyUr.get(b).get("RP_URUTAN").toString()));
						
						urp.add(urk);
					}
					copyUk.get(a).setListRpd(urp);  
					
					copyUr = uwManager.getListRiwPenyakit(wsTemp.getReg_spaj(), wsTemp.getInsured_no(),a+1,2);//copy
					urp = new ArrayList<UwRiwPenyakit>();
					for(int b=0;b<copyUr.size();b++) {// copy all
						UwRiwPenyakit urk = new UwRiwPenyakit(2);
						urk.setReg_spaj(wsTemp.getReg_spaj());//copy ori
						urk.setInsured_no(Integer.parseInt(copyUr.get(b).get("INSURED_NO").toString()));
						urk.setUrutanLpk(Integer.parseInt(copyUr.get(b).get("URUTAN_LPK").toString()));
						urk.setRp_desc(copyUr.get(b).get("RP_DESC").toString());
						urk.setRp_type(Integer.parseInt(copyUr.get(b).get("RP_TYPE").toString()));
						urk.setRp_urutan(Integer.parseInt(copyUr.get(b).get("RP_URUTAN").toString()));
						
						urp.add(urk);
					}
					copyUk.get(a).setListRps(urp); 
					
					copyUr = uwManager.getListRiwPenyakit(wsTemp.getReg_spaj(), wsTemp.getInsured_no(),a+1,3);//copy
					urp = new ArrayList<UwRiwPenyakit>();
					for(int b=0;b<copyUr.size();b++) {// copy all
						UwRiwPenyakit urk = new UwRiwPenyakit(3);
						urk.setReg_spaj(wsTemp.getReg_spaj());//copy ori
						urk.setInsured_no(Integer.parseInt(copyUr.get(b).get("INSURED_NO").toString()));
						urk.setUrutanLpk(Integer.parseInt(copyUr.get(b).get("URUTAN_LPK").toString()));
						urk.setRp_desc(copyUr.get(b).get("RP_DESC").toString());
						urk.setRp_type(Integer.parseInt(copyUr.get(b).get("RP_TYPE").toString()));
						urk.setRp_urutan(Integer.parseInt(copyUr.get(b).get("RP_URUTAN").toString()));
						
						urp.add(urk);
					}
					copyUk.get(a).setListRpk(urp);
					
					copyUr = uwManager.getListRiwPenyakit(wsTemp.getReg_spaj(), wsTemp.getInsured_no(),a+1,4);//copy
					urp = new ArrayList<UwRiwPenyakit>();
					for(int b=0;b<copyUr.size();b++) {// copy all
						UwRiwPenyakit urk = new UwRiwPenyakit(4);
						urk.setReg_spaj(wsTemp.getReg_spaj());//copy ori
						urk.setInsured_no(Integer.parseInt(copyUr.get(b).get("INSURED_NO").toString()));
						urk.setUrutanLpk(Integer.parseInt(copyUr.get(b).get("URUTAN_LPK").toString()));
						urk.setRp_desc(copyUr.get(b).get("RP_DESC").toString());
						urk.setRp_type(Integer.parseInt(copyUr.get(b).get("RP_TYPE").toString()));
						urk.setRp_urutan(Integer.parseInt(copyUr.get(b).get("RP_URUTAN").toString()));
						
						urp.add(urk);
					}
					copyUk.get(a).setListKelainan(urp);
				}
				wsTemp.setListLpk(copyUk);//copy
			}
			//wsTemp.setListUrin(uwManager.getListUrin(wsTemp.getReg_spaj(),  wsTemp.getInsured_no()));
			wsTemp.setListUrin(uwManager.getListUrin(reg_spaj,  insuredNo));//copy
			//wsTemp.setListAda(uwManager.getListAda(wsTemp.getReg_spaj(),  wsTemp.getInsured_no()));
			wsTemp.setListAda(uwManager.getListAda(reg_spaj,  insuredNo));//copy
			for(int a=0;a<wsTemp.getListAda().size();a++) {
				if(wsTemp.getListAda().get(a).getTotal_cholesterol() != null && wsTemp.getListAda().get(a).getHdl_cholesterol() != null) {
					BigDecimal totChol = new BigDecimal(wsTemp.getListAda().get(a).getTotal_cholesterol());
					totChol = totChol.divide(new BigDecimal(wsTemp.getListAda().get(a).getHdl_cholesterol()),2,RoundingMode.HALF_UP);
					wsTemp.getListAda().get(a).setChol_hdl(totChol.doubleValue());
					wsTemp.getListAda().get(a).setRatio_cholesterol(uwManager.getRatioChol(wsTemp.getListAda().get(a).getSatuanChol(),wsTemp.getListAda().get(a).getTotal_cholesterol(),wsTemp.getListAda().get(a).getHdl_cholesterol(),wsTemp.getUmur()));
				}
				if(wsTemp.getListAda().get(a).getHdl_cholesterol() != null && wsTemp.getListAda().get(a).getLdl_cholesterol() != null) {
					BigDecimal ldl = new BigDecimal(wsTemp.getListAda().get(a).getLdl_cholesterol());
					ldl = ldl.divide(new BigDecimal(wsTemp.getListAda().get(a).getHdl_cholesterol()),2,RoundingMode.HALF_UP);
					wsTemp.getListAda().get(a).setLdl_hdl(ldl.doubleValue());
				}
			}
//			wsTemp.setListHiv(uwManager.getListHiv(wsTemp.getReg_spaj(),  wsTemp.getInsured_no()));
//			wsTemp.setListTumor(uwManager.getListTumor(wsTemp.getReg_spaj(),  wsTemp.getInsured_no()));
//			wsTemp.setListAbdomen(uwManager.getListAbdomen(wsTemp.getReg_spaj(),  wsTemp.getInsured_no()));
//			wsTemp.setListDadaPa(uwManager.getListDadaPa(wsTemp.getReg_spaj(),  wsTemp.getInsured_no()));
//			wsTemp.setListEkg(uwManager.getListEkg(wsTemp.getReg_spaj(),  wsTemp.getInsured_no()));
//			wsTemp.setListTreadmill(uwManager.getListTreadmill(wsTemp.getReg_spaj(),  wsTemp.getInsured_no()));
//			wsTemp.setListMedLain(uwManager.getListMedLain(wsTemp.getReg_spaj(),  wsTemp.getInsured_no()));
			
			//copy all
			wsTemp.setListHiv(uwManager.getListHiv(reg_spaj,  insuredNo));
			wsTemp.setListTumor(uwManager.getListTumor(reg_spaj,  insuredNo));
			wsTemp.setListAbdomen(uwManager.getListAbdomen(reg_spaj,  insuredNo));
			wsTemp.setListDadaPa(uwManager.getListDadaPa(reg_spaj,  insuredNo));
			wsTemp.setListEkg(uwManager.getListEkg(reg_spaj,  insuredNo));
			wsTemp.setListTreadmill(uwManager.getListTreadmill(reg_spaj,  insuredNo));
			wsTemp.setListMedLain(uwManager.getListMedLain(reg_spaj,  insuredNo));
			//
		}

		//List<HashMap> dec = uwManager.getListUwDec(wsTemp.getReg_spaj(), wsTemp.getInsured_no());
		List<HashMap> copyDec = uwManager.getListUwDec(reg_spaj,  insuredNo);//copy
		if(copyDec.size() != 0) {//copy all
			wsTemp.setListUwDec(new ArrayList<UwDecision>());
			for(int a=0;a<copyDec.size();a++) {
				UwDecision ud = new UwDecision(sysDate);
				ud.setRiderContent(jumlProd);
				wsTemp.getListUwDec().add(ud);
				
				wsTemp.getListUwDec().get(a).setReg_spaj(wsTemp.getReg_spaj());//copy
				wsTemp.getListUwDec().get(a).setInsured_no(wsTemp.getInsured_no());//copy
				wsTemp.getListUwDec().get(a).setMwd_urut(Integer.parseInt(copyDec.get(a).get("MWD_URUT").toString()));
				if(copyDec.get(a).get("PENYAKIT") != null) wsTemp.getListUwDec().get(a).setPenyakit(copyDec.get(a).get("PENYAKIT").toString());
				wsTemp.getListUwDec().get(a).setLsbs_id(Integer.parseInt(copyDec.get(a).get("LSBS_ID").toString()));
				wsTemp.getListUwDec().get(a).setLsdbs_number(Integer.parseInt(copyDec.get(a).get("LSDBS_NUMBER").toString()));
				if(copyDec.get(a).get("PROD_UTAMA_PERSEN") != null)wsTemp.getListUwDec().get(a).setProd_utama_persen(copyDec.get(a).get("PROD_UTAMA_PERSEN").toString());
				if(copyDec.get(a).get("PROD_UTAMA_PERMIL") != null)wsTemp.getListUwDec().get(a).setProd_utama_permil(copyDec.get(a).get("PROD_UTAMA_PERMIL").toString());
				if(copyDec.get(a).get("LIC_ID") != null) wsTemp.getListUwDec().get(a).setLic_id(copyDec.get(a).get("LIC_ID").toString());
				if(copyDec.get(a).get("LIC_DESC") != null) wsTemp.getListUwDec().get(a).setLic_desc(copyDec.get(a).get("LIC_DESC").toString());
				if(copyDec.get(a).get("URUTAN_PENYAKIT") != null) wsTemp.getListUwDec().get(a).setUrutan_penyakit(Integer.parseInt(copyDec.get(a).get("URUTAN_PENYAKIT").toString()));
				else wsTemp.getListUwDec().get(a).setUrutan_penyakit(null);
				if(copyDec.get(a).get("URUTAN_DECISION") != null) wsTemp.getListUwDec().get(a).setUrutan_decision(Integer.parseInt(copyDec.get(a).get("URUTAN_DECISION").toString()));
				else wsTemp.getListUwDec().get(a).setUrutan_decision(null);
				if(copyDec.get(a).get("CATATAN") != null) wsTemp.getListUwDec().get(a).setCatatan(copyDec.get(a).get("CATATAN").toString());
				wsTemp.getListUwDec().get(a).setLus_id(copyDec.get(a).get("LUS_ID").toString());
				wsTemp.getListUwDec().get(a).setInput_date(copyDec.get(a).get("INPUT_DATE").toString());
				// untuk menghandle rider nya berubah setelah worksheet disimpan
				for(int b=0;b<wsTemp.getListUwDec().get(a).getRider().size();b++) {
					UwDecisionRider udr = uwManager.getListWorkDecRiderById(copyDec.get(a).get("REG_SPAJ").toString(),Integer.parseInt(copyDec.get(a).get("INSURED_NO").toString()),Integer.parseInt(copyDec.get(a).get("MWD_URUT").toString()),wsTemp.getListUwDec().get(a).getRider().get(b).getLsbs_id(),wsTemp.getListUwDec().get(a).getRider().get(b).getLsdbs_number());
					if(udr != null) wsTemp.getListUwDec().get(a).getRider().set(b, udr);
				}
				//wsTemp.getListUwDec().get(a).setRider(uwManager.getListWorkDecRider(dec.get(a).get("REG_SPAJ").toString(),Integer.parseInt(dec.get(a).get("INSURED_NO").toString()),Integer.parseInt(dec.get(a).get("MWD_URUT").toString())));
				if(copyDec.get(a).get("URUTAN_PENYAKIT") != null) wsTemp.setTotalPenyakitUwDesc(Integer.parseInt(copyDec.get(a).get("URUTAN_PENYAKIT").toString()));
			}
		}			
		
//		temp = uwManager.getFinancialStat(wsTemp.getReg_spaj(), wsTemp.getInsured_no());
		copyTemp = uwManager.getFinancialStat(reg_spaj,  insuredNo);//copy
		if(copyTemp != null) {// copy all
			if(copyTemp.getFlag_fs() != 0) wsTemp.setFlag_fs(copyTemp.getFlag_fs());
			if(copyTemp.getFs_date() != null) wsTemp.setFs_date(copyTemp.getFs_date());
			if(copyTemp.getFs_copy_rek_bank() != null) wsTemp.setFs_copy_rek_bank(copyTemp.getFs_copy_rek_bank());
			if(copyTemp.getFs_copy_npwp() != null) wsTemp.setFs_copy_npwp(copyTemp.getFs_copy_npwp());
			if(copyTemp.getFs_spt_pribadi() != null) wsTemp.setFs_spt_pribadi(copyTemp.getFs_spt_pribadi());
			if(copyTemp.getFs_copy_neraca_persh() != null) wsTemp.setFs_copy_neraca_persh(copyTemp.getFs_copy_neraca_persh());
			if(copyTemp.getFs_lain() != null) wsTemp.setFs_lain(copyTemp.getFs_lain());
			wsTemp.setFs_lain_desc(copyTemp.getFs_lain_desc());				
		}
		

		List<SortedMap> x, daftar1 = new ArrayList<SortedMap>();
		List<SortedMap> daftar2 = new ArrayList<SortedMap>();
		Boolean _2th = wsTemp.getModeSim() == 0 ? false : true;
		if(wsTemp.getInsured_no() == 0) wsTemp.setPptt(2);
		else wsTemp.setPptt(3);
		List<Map> filters = uwManager.selectFilterSpajNew(wsTemp.getPptt().toString(), wsTemp.getNamaPeserta(), new SimpleDateFormat("yyyyMMdd").format(df.parse(wsTemp.getBirthDate())), "LIKE%", _2th);

		for(int i=0; i<filters.size(); i++) {
			x = sortingPlanUtama(sortingPlan(uwManager.selectAkumulasiPolisBySpaj((String) filters.get(i).get("REG_SPAJ"))));
			for(SortedMap map : x) {
				String uw_info = uwManager.getKetMedis(map.get("M").toString());
				if(uw_info != null) map.put("D1", uw_info);
				if(map.get("L").toString().equals("1")) daftar1.add(map);
				else if(map.get("L").toString().equals("10")) daftar2.add(map);
			}
		}
		wsTemp.setListSimultan(daftar1);
		wsTemp.setListSimultanProc(daftar2);
		if(wsTemp.getJenisMedis() != null) wsTemp = changeMedis(wsTemp,1,false);	
		
		return wsTemp;
	}
	
	/**
	 * Fungsi untuk menampilkan jenis-jenis medis tertentu saja
	 * @param ws
	 * @param flag
	 *
	 * @author Yusup_A
	 * @since May 26, 2010 (11:18:52 AM)
	 */
	private UwWorkSheet changeMedis(UwWorkSheet ws, Integer flag, Boolean isNew) {
		String reg_spaj = ws.getReg_spaj();
		
		Integer lstb_id = uwManager.getLstbId(reg_spaj);
		
		if(lstb_id == 2){//MRI
			ws = changeMedis_mri(ws, flag, isNew);
		}else{//LAINNYA KE INDIVIDU
			ws = changeMedis_individu(ws, flag, isNew);
		}
		
		return ws;
	}
	
	private UwWorkSheet changeMedis_mri(UwWorkSheet ws, Integer flag, Boolean isNew) {
		
		if(ws.getJenisMedis().equals("ALL")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADA, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_TUMOR, true);
			newPaket.put(medis_ABDOMEN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_EKG, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_MEDLAIN, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("A")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("A+")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_MEDLAIN, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("B")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("C")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADA, true);
			newPaket.put(medis_EKG, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("D")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_EKG, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("E")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_EKG, true);
			newPaket.put(medis_DADAPA, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("F")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_TREADMILL, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("G")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_TUMOR, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("H")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK2, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_TUMOR, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("A1")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_EKG, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("B1")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_TREADMILL, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("C1")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("D1")) { 
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_TUMOR, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("E1")) { 
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_ABDOMEN, true);
			newPaket.put(medis_TUMOR, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("B2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_EKG, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("C2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_EKG, true);
			newPaket.put(medis_ADAL, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("D2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_TREADMILL, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("E2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("F2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK2, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("A2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_EKG, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("B3")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_TREADMILL, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("C3")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("D3")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_TUMOR, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}	
		else if(ws.getJenisMedis().equals("E3")) { 
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_ABDOMEN, true);
			newPaket.put(medis_TUMOR, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("C4")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADA, true);
			newPaket.put(medis_EKG, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("D4")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_EKG, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("E4")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_EKG, true);
			newPaket.put(medis_DADAPA, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("F3")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_TREADMILL, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("G2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_TUMOR, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}	
		else if(ws.getJenisMedis().equals("H2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK2, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_TUMOR, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}	
		
		return ws;
	}
	
	private UwWorkSheet changeMedis_individu(UwWorkSheet ws, Integer flag, Boolean isNew) {
		
		if(ws.getJenisMedis().equals("ALL")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADA, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_TUMOR, true);
			newPaket.put(medis_ABDOMEN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_EKG, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_MEDLAIN, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("A")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("A+")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_MEDLAIN, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("B")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("C")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_EKG, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("D")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("E")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_EKG, true);
			newPaket.put(medis_ADAL, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("F")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_ADAL, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("G")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_HIV, true);
			newPaket.put(medis_TUMOR, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("H")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK2, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_MEDLAIN, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("A2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK2, true);
			newPaket.put(medis_MEDLAIN, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("E2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK2, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_EKG, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_MEDLAIN, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("F2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK2, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_MEDLAIN, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}
		else if(ws.getJenisMedis().equals("H2")) {
			Map<String, Object> newPaket = new HashMap<String, Object>();
			newPaket.put(medis_LPK2, true);
			newPaket.put(medis_URIN, true);
			newPaket.put(medis_TREADMILL, true);
			newPaket.put(medis_ADAL, true);
			newPaket.put(medis_DADAPA, true);
			newPaket.put(medis_ADA, true);
			newPaket.put(medis_MEDLAIN, true);
			
			ws = addMedis(ws, flag, isNew, newPaket);
		}	
		
		return ws;
	}
	
	/**
	 * Fungsi untuk print inputan uw worksheet
	 * @param ws
	 * @param response
	 * @throws Exception
	 *
	 * @author Yusup_A
	 * @since May 26, 2010 (11:19:34 AM)
	 */
	private void printUwWorksheet(UwWorkSheet ws, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String outputDir = props.getProperty("pdf.dir.report") + "\\uw_worksheet\\" + ws.getReg_spaj().substring(0,2) + "\\" + ws.getReg_spaj() + "\\";
		String outputFilename = "";
		
		// isi di halaman ke 1
		params.put("REG_SPAJ", ws.getReg_spaj());
		params.put("MCL_FIRST", ws.getNamaPeserta());
		params.put("SEX", ws.getSex());
		params.put("BIRTH_DATE", ws.getBirthDate());
		params.put("AGE", ws.getUmur());
		if(ws.getInsured_no() == 0) {
			params.put("PESERTA", "PP");
			params.put("PESERTA2", "Pemegang Polis");
		}
		else {
			params.put("PESERTA", "TTG");
			params.put("PESERTA2", "Tertanggung");
		}
		List<SortedMap> plan = sortingPlanUtama(sortingPlan(uwManager.selectAkumulasiPolisBySpaj(ws.getReg_spaj())));
		String rider = "";
		for(int a=0;a<plan.size();a++) {
			if(a == 0) {
				params.put("PROD", plan.get(a).get("V").toString());
				params.put("UP", new BigDecimal(plan.get(a).get("E").toString()));
				params.put("INSURED_DATE", plan.get(a).get("J").toString() + " s/d " + plan.get(a).get("K").toString());
			}
			else if(a == 1) rider = plan.get(a).get("V").toString().trim();
			else if(a > 1) rider = rider + ", " + plan.get(a).get("V").toString().trim();
		}
		params.put("RIDER", rider);
		params.put("TIPE_MEDIS", ws.getJenisMedis());
		params.put("FIN_STAT", ws.getFlag_fs() == 0 ? "No" : "Yes");
		if(ws.getNonMedisOccupation() == null) params.put("OCC", "");
		else {
			List<DropDown> x = uwManager.selectLstWorksheet(2,ws.getNonMedisOccupation());
			params.put("OCC", x.get(0).getKey());
		}
		if(ws.getNonMedisHobby() == null) params.put("HOBBY", "");
		else {
			List<DropDown> x = uwManager.selectLstWorksheet(3,ws.getNonMedisHobby());
			params.put("HOBBY", x.get(0).getKey());
		}
		params.put("KEBIASAAN", ws.getNonMedisHabits());
		String penyakit = "Tidak Ada";
		for(int a=0;a<ws.getListLpk().get(ws.getListLpk().size()-1).getListRpd().size();a++) {
			if(a ==0) penyakit = ws.getListLpk().get(0).getListRpd().get(a).getRp_desc();
			else penyakit = penyakit + ", " + ws.getListLpk().get(0).getListRpd().get(a).getRp_desc();
		}
		if(ws.getListLpk().get(0).getLpkRPD() == 0){penyakit = "Tidak Ada";}
		params.put("RPD", penyakit);
		penyakit = "Tidak Ada";
		for(int a=0;a<ws.getListLpk().get(ws.getListLpk().size()-1).getListRps().size();a++) {
			if(a ==0) penyakit = ws.getListLpk().get(0).getListRps().get(a).getRp_desc();
			else penyakit = penyakit + ", " + ws.getListLpk().get(0).getListRps().get(a).getRp_desc();
		}
		if(ws.getListLpk().get(0).getLpkRPS() == 0){penyakit = "Tidak Ada";}
		params.put("RPS", penyakit);
		penyakit = "Tidak Ada";
		for(int a=0;a<ws.getListLpk().get(ws.getListLpk().size()-1).getListRpk().size();a++) {
			if(a ==0) penyakit = ws.getListLpk().get(0).getListRpk().get(a).getRp_desc();
			else penyakit = penyakit + ", " + ws.getListLpk().get(0).getListRpk().get(a).getRp_desc();;
		}
		if(ws.getListLpk().get(0).getLpkRPK() == 0){penyakit = "Tidak Ada";}
		params.put("RPK", penyakit);
		params.put("nonMedBB", ws.getNonMedisBb());
		params.put("nonMedTB", ws.getNonMedisTb());
		params.put("nonMedBmi", ws.getNonMedisBmi());
		params.put("nonMedKelainan",ws.getNonMedisKelainan());
		if(ws.getListLpk().size() != 0) {
			if(ws.getListLpk().get(ws.getListLpk().size()-1).getLpkBb() != null) params.put("BB", ws.getListLpk().get(ws.getListLpk().size()-1).getLpkBb());
			if(ws.getListLpk().get(ws.getListLpk().size()-1).getLpkTb() != null) params.put("TB", ws.getListLpk().get(ws.getListLpk().size()-1).getLpkTb());
			if(ws.getListLpk().get(ws.getListLpk().size()-1).getLpkBmi() != null) params.put("BMI_LPK", ws.getListLpk().get(ws.getListLpk().size()-1).getLpkBmi());
			params.put("BLOOD_PRES", ws.getListLpk().get(ws.getListLpk().size()-1).getLpkSystolic() + "/" + ws.getListLpk().get(ws.getListLpk().size()-1).getLpkDiastolic());
			params.put("EM_BLOOD_PRES", ws.getListLpk().get(ws.getListLpk().size()-1).getEmBloodPresure());
			params.put("PULSE", ws.getListLpk().get(ws.getListLpk().size()-1).getLpkDeyutNadi());			
		}
		List<SortedMap> dataList = new ArrayList<SortedMap>();
		if(ws.getListUrin().size() != 0) {
			params.put("WARNA", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagWarna(),ws.getListUrin().get(ws.getListUrin().size()-1).getWarna()));
			params.put("KEJERNIHAN", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagkejernihan(),ws.getListUrin().get(ws.getListUrin().size()-1).getKejernihan()));
			params.put("BJ", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagBj(),ws.getListUrin().get(ws.getListUrin().size()-1).getBj()));
			params.put("PH", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagPh(),ws.getListUrin().get(ws.getListUrin().size()-1).getPh()));
			params.put("PROTEIN", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagProtein(),ws.getListUrin().get(ws.getListUrin().size()-1).getProtein()));
			params.put("GLUKOSA", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagGlukosa(),ws.getListUrin().get(ws.getListUrin().size()-1).getGlukosa()));
			params.put("KETON", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagKeton(),ws.getListUrin().get(ws.getListUrin().size()-1).getKeton()));
			params.put("BILIRUBIN", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagBilirubin(),ws.getListUrin().get(ws.getListUrin().size()-1).getBilirubin()));
			params.put("UROBILINOGEN", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagUrobilinogen(),ws.getListUrin().get(ws.getListUrin().size()-1).getUrobilinogen()));
			params.put("NITRIT", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagNitrit(),ws.getListUrin().get(ws.getListUrin().size()-1).getNitrit()));
			params.put("DARAH_SAMAR", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagDarah_samar(),ws.getListUrin().get(ws.getListUrin().size()-1).getDarah_samar()));
			params.put("LEUKOSIT_ESTERASE", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagLeukosit_esterase(),ws.getListUrin().get(ws.getListUrin().size()-1).getLekosit_esterase()));
			params.put("SEDIMEN_ERITROSIT", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagSedimen_eritrosit(),ws.getListUrin().get(ws.getListUrin().size()-1).getSedimen_eritrosit()));
			params.put("SEDIMEN_LEUKOSIT", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagSedimen_leukosit(),ws.getListUrin().get(ws.getListUrin().size()-1).getSedimen_leukosit()));
			params.put("SEDIMEN_EPITEL", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagSedimen_epitel(),ws.getListUrin().get(ws.getListUrin().size()-1).getSedimen_epitel()));
			params.put("SEDIMEN_SILINDER", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagSedimen_silinder(),ws.getListUrin().get(ws.getListUrin().size()-1).getSedimen_silinder()));
			params.put("SEDIMEN_KRISTAL", assignValueToReport(ws.getListUrin().get(ws.getListUrin().size()-1).getFlagSedimen_kristal(),ws.getListUrin().get(ws.getListUrin().size()-1).getSedimen_kristal()));
			params.put("NV_WARNA", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_warna());
			params.put("NV_KEJERNIHAN", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_kejernihan());
			params.put("NV_BJ", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_berat_jenis());
			params.put("NV_PH", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_ph());
			params.put("NV_PROTEIN", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_protein());
			params.put("NV_GLUKOSA", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_glukosa());
			params.put("NV_KETON", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_keton());
			params.put("NV_BILIRUBIN", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_bilirubin());
			params.put("NV_UROBILINOGEN", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_urobilinogen());
			params.put("NV_NITRIT", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_nitrit());
			params.put("NV_DARAH_SAMAR", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_darah_samar());
			params.put("NV_LEUKOSIT_ESTERASE", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_lekosit_esterase());
			params.put("NV_SEDIMEN_ERITROSIT", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_eritrosit());
			params.put("NV_SEDIMEN_LEUKOSIT", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_lekosit());
			params.put("NV_SEDIMEN_EPITEL", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_epitel());
			params.put("NV_SEDIMEN_SILINDER", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_silinder());
			params.put("NV_SEDIMEN_KRISTAL", ws.getListUrin().get(ws.getListUrin().size()-1).getNv_urin_kristal());				
		}
		
		if(ws.getMsw_medis() == 1) {
			//List<SortedMap> dataList = new ArrayList<SortedMap>();
			for(int a=0;a<ws.getListSimultan().size();a++) {
				dataList.add(ws.getListSimultan().get(a));
			}
			for(int a=0;a<ws.getListSimultanProc().size();a++) {
				dataList.add(ws.getListSimultanProc().get(a));
			}
			outputFilename = "uw_worksheet_p1_"+ws.getReg_spaj()+".pdf";		
			JasperUtils.exportReportToPdf(props.getProperty("report.uw.worksheet_peserta_p1") + ".jasper", outputDir, outputFilename, params, dataList, PdfWriter.AllowPrinting, null, null);
		}else if(ws.getMsw_medis() == 0) {
			for(int a=0;a<ws.getListSimultanProc().size();a++) {
				dataList.add(ws.getListSimultanProc().get(a));
			}
		}
	
		
		// isi di halaman ke 2
		if(ws.getListAda().size() != 0) {
			params.put("HEMOGLOBIN", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagHemoglobin(),ws.getListAda().get(ws.getListAda().size()-1).getHemoglobin()));
			params.put("LEUKOSIT", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagLeukosit(),ws.getListAda().get(ws.getListAda().size()-1).getLeukosit()));
			params.put("EOSINOFIL", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagEosinofil(),ws.getListAda().get(ws.getListAda().size()-1).getEosinofil()));
			params.put("BASOFIL", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagBasofil(),ws.getListAda().get(ws.getListAda().size()-1).getBasofil()));
			params.put("NEUTROFIL_BATANG", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagNeutrofil_batang(),ws.getListAda().get(ws.getListAda().size()-1).getNeutrofil_batang()));
			params.put("NEUTROFIL_SEGMEN", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagNeutrofil_segmen(),ws.getListAda().get(ws.getListAda().size()-1).getNeutrofil_segmen()));
			params.put("LIMFOSIT", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagLimfosit(),ws.getListAda().get(ws.getListAda().size()-1).getLimfosit()));
			params.put("MONOSIT", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagMonosit(),ws.getListAda().get(ws.getListAda().size()-1).getMonosit()));
			params.put("TROMBOSIT", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagTrombosit(),ws.getListAda().get(ws.getListAda().size()-1).getTrombosit()));
			params.put("ERITROSIT", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagEritrosit(),ws.getListAda().get(ws.getListAda().size()-1).getEritrosit()));
			params.put("HEMATOKRIT", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagHematokrit(),ws.getListAda().get(ws.getListAda().size()-1).getHematokrit()));
			params.put("LED", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagLed(),ws.getListAda().get(ws.getListAda().size()-1).getLed()));
			params.put("MCV", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagMcv(),ws.getListAda().get(ws.getListAda().size()-1).getMcv()));
			params.put("MCH", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagMch(),ws.getListAda().get(ws.getListAda().size()-1).getMch()));
			params.put("MCHC", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagMchc(),ws.getListAda().get(ws.getListAda().size()-1).getMch()));
			params.put("RDW", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagRdw(),ws.getListAda().get(ws.getListAda().size()-1).getRdw()));
			params.put("NV_HEMOGLOBIN", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_hemoglobin());
			params.put("NV_LEUKOSIT", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_leukosit());
			params.put("NV_EOSINOFIL", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_eosinofil());
			params.put("NV_BASOFIL", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_basofil());
			params.put("NV_NEUTROFIL_BATANG", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_neutrofil_batang());
			params.put("NV_NEUTROFIL_SEGMEN", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_neutrofil_segmen());
			params.put("NV_LIMFOSIT", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_limfosit());
			params.put("NV_MONOSIT", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_monosit());
			params.put("NV_TROMBOSIT", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_trombosit());
			params.put("NV_ERITROSIT", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_eritrosit());
			params.put("NV_HEMATOKRIT", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_hematokrit());
			params.put("NV_LED", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_led());
			params.put("NV_MCV", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_mcv());
			params.put("NV_MCH", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_mch());
			params.put("NV_MCHC", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_mchc());
			params.put("NV_RDW", ws.getListAda().get(ws.getListAda().size()-1).getNv_darah_rutin_rdw());
			params.put("GLUKOSA_F", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagGulaDarahPuasa(),ws.getListAda().get(ws.getListAda().size()-1).getGula_darah_puasa()));
			params.put("GLUKOSA_PP", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagGulaDarahPp(),ws.getListAda().get(ws.getListAda().size()-1).getGula_darah_pp()));
			params.put("HBA1C", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagHb1c(),ws.getListAda().get(ws.getListAda().size()-1).getHba1c()));
			params.put("CHOL_TOTAL", ws.getListAda().get(ws.getListAda().size()-1).getTotal_cholesterol());
			params.put("CHOL_HDL", ws.getListAda().get(ws.getListAda().size()-1).getHdl_cholesterol());
			params.put("CHOL_LDL", ws.getListAda().get(ws.getListAda().size()-1).getLdl_cholesterol());
			params.put("RATIO_CHOL_HDL", ws.getListAda().get(ws.getListAda().size()-1).getChol_hdl());
			params.put("RATIO_LDL_HDL", ws.getListAda().get(ws.getListAda().size()-1).getLdl_hdl());
			params.put("TRIGLISERIDA", ws.getListAda().get(ws.getListAda().size()-1).getTrigiliserida());
			params.put("SGOT", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagSgot(),ws.getListAda().get(ws.getListAda().size()-1).getSgot()));
			params.put("SGPT", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagSgpt(),ws.getListAda().get(ws.getListAda().size()-1).getSgpt()));
			params.put("GGT", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagGgt(),ws.getListAda().get(ws.getListAda().size()-1).getGgt()));
			params.put("FOSFATASE_ALKALI", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagFosfatase_alkali(),ws.getListAda().get(ws.getListAda().size()-1).getFosfatase_alkali()));
			params.put("HBSAG", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagHbs_ag(),ws.getListAda().get(ws.getListAda().size()-1).getHbs_ag()));
			params.put("HBEAG", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagHbe_ag(),ws.getListAda().get(ws.getListAda().size()-1).getHbe_ag()));
			params.put("BILIRUBIN_TOTAL", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagBilirubin_total(),ws.getListAda().get(ws.getListAda().size()-1).getBilirubin_total()));
			params.put("BILIRUBIN_DIREK", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagBilirubin_direk(),ws.getListAda().get(ws.getListAda().size()-1).getBilirubin_direk()));
			params.put("BILIRUBIN_INDIREK", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagBilirubin_indirek(),ws.getListAda().get(ws.getListAda().size()-1).getBilirubin_indirek()));
			params.put("PROTEIN_TOTAL", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagTotal_protein(),ws.getListAda().get(ws.getListAda().size()-1).getTotal_protein()));
			params.put("ALBUMIN", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagAlbumin(),ws.getListAda().get(ws.getListAda().size()-1).getAlbumin()));
			params.put("GLOBULIN", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagGlobulin(),ws.getListAda().get(ws.getListAda().size()-1).getGlobulin()));
			params.put("UREUM", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagUreum(),ws.getListAda().get(ws.getListAda().size()-1).getUreum()));
			params.put("CREATININ", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagCreatinin(),ws.getListAda().get(ws.getListAda().size()-1).getCreatinin()));
			params.put("ASAM_URAT", assignValueToReport(ws.getListAda().get(ws.getListAda().size()-1).getFlagAsam_urat(),ws.getListAda().get(ws.getListAda().size()-1).getAsam_urat()));
			params.put("NV_GLUKOSA_F", ws.getListAda().get(ws.getListAda().size()-1).getNv_gula_darah_glukosa_darah_puasa());
			params.put("NV_GLUKOSA_PP", ws.getListAda().get(ws.getListAda().size()-1).getNv_gula_darah_glukosa_pp());
			params.put("NV_HBA1C", ws.getListAda().get(ws.getListAda().size()-1).getNv_gula_darah_glukosa_hba1c());
			params.put("NV_CHOL_TOTAL", ws.getListAda().get(ws.getListAda().size()-1).getNv_lemak_darah_total_cholesterol());
			params.put("NV_CHOL_HDL", ws.getListAda().get(ws.getListAda().size()-1).getNv_lemak_darah_hdl_cholesterol());
			params.put("NV_CHOL_LDL", ws.getListAda().get(ws.getListAda().size()-1).getNv_lemak_darah_ldl_cholesterol());
			params.put("NV_RATIO_CHOL_HDL", ws.getListAda().get(ws.getListAda().size()-1).getNv_lemak_darah_ratio_total_chol_hdl());
			params.put("NV_RATIO_LDL_HDL", ws.getListAda().get(ws.getListAda().size()-1).getNv_lemak_darah_ratio_ldl_hdl());
			params.put("NV_TRIGLISERIDA", ws.getListAda().get(ws.getListAda().size()-1).getNv_lemak_darah_trigliserida());
			params.put("NV_SGOT", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_hati_sgot_ast());
			params.put("NV_SGPT", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_hati_sgpt_alt());
			params.put("NV_GGT", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_hati_gamma_gt_ggtp());
			params.put("NV_FOSFATASE_ALKALI", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_hati_alkali_fosfatase());
			params.put("NV_HBSAG", ws.getListAda().get(ws.getListAda().size()-1).getNv_hepatitis_hbsag());
			params.put("NV_HBEAG", ws.getListAda().get(ws.getListAda().size()-1).getNv_hepatitis_hbeag());
			params.put("NV_BILIRUBIN_TOTAL", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_hati_bilirubin_total());
			params.put("NV_BILIRUBIN_DIREK", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_hati_bilirubin_direk());
			params.put("NV_BILIRUBIN_INDIREK", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_hati_bilirubin_indirek());
			params.put("NV_PROTEIN_TOTAL", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_hati_total_protein());
			params.put("NV_ALBUMIN", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_hati_albumin());
			params.put("NV_GLOBULIN", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_hati_globulin());
			params.put("NV_UREUM", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_ginjal_ureum());
			params.put("NV_CREATININ", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_ginjal_creatinin());
			params.put("NV_ASAM_URAT", ws.getListAda().get(ws.getListAda().size()-1).getNv_fungsi_ginjal_asam_urat());
		}
		
		if(ws.getMsw_medis() == 1) {
			outputFilename = "uw_worksheet_p2_"+ws.getReg_spaj()+".pdf";		
			
			Connection conn = null;
			try {
				conn = this.elionsManager.getUwDao().getDataSource().getConnection();
				JasperUtils.exportReportToPdf(props.getProperty("report.uw.worksheet_peserta_p2") + ".jasper", outputDir, outputFilename, params, conn, PdfWriter.AllowPrinting, null, null);
			}catch(Exception e){
				this.elionsManager.getUwDao().closeConn(conn);
	            throw e;
			}finally{
				this.elionsManager.getUwDao().closeConn(conn);
			}
			
		}
		
		
		// isi di halaman ke 3
		if(ws.getListHiv().size() != 0) {
			params.put("HIV", ws.getListHiv().get(ws.getListHiv().size()-1).getAntiHiv());
			params.put("NV_HIV", ws.getListHiv().get(ws.getListHiv().size()-1).getNv_anti_hiv_anti_hiv());
		}
		if(ws.getListTumor().size() != 0) {
			params.put("AFP", ws.getListTumor().get(ws.getListTumor().size()-1).getAfp());
			params.put("CEA", ws.getListTumor().get(ws.getListTumor().size()-1).getCea());
			params.put("PSA", ws.getListTumor().get(ws.getListTumor().size()-1).getPsa());
			params.put("CA_15-3", ws.getListTumor().get(ws.getListTumor().size()-1).getCa_15_3());
			params.put("NV_AFP", ws.getListTumor().get(ws.getListTumor().size()-1).getNv_tumor_marker_afp());
			params.put("NV_CEA", ws.getListTumor().get(ws.getListTumor().size()-1).getNv_tumor_marker_cea());
			params.put("NV_PSA", ws.getListTumor().get(ws.getListTumor().size()-1).getNv_tumor_marker_psa());
			params.put("NV_CA_15-3", ws.getListTumor().get(ws.getListTumor().size()-1).getNv_tumor_marker_ca_15_3());
		}
		if(ws.getListAbdomen().size() != 0) {
			params.put("ABDOMEN", assignValueToReport(ws.getListAbdomen().get(ws.getListAbdomen().size()-1).getFlagAbdomen(),ws.getListAbdomen().get(ws.getListAbdomen().size()-1).getAbdomen_kelainan()));
		}
		if(ws.getListDadaPa().size() != 0) {
			params.put("DADA_PA", assignValueToReport(ws.getListDadaPa().get(ws.getListDadaPa().size()-1).getFlagDadaPa(),ws.getListDadaPa().get(ws.getListDadaPa().size()-1).getDada_pa_kelainan()));
		}
		if(ws.getListEkg().size() != 0) {
			params.put("EKG", assignValueToReport(ws.getListEkg().get(ws.getListEkg().size()-1).getFlagEkg(),ws.getListEkg().get(ws.getListEkg().size()-1).getEkg_kelainan()));
		}
		if(ws.getListTreadmill().size() != 0) {
			params.put("DURATION", ws.getListTreadmill().get(ws.getListTreadmill().size()-1).getDuration_men() + " mnt " + ws.getListTreadmill().get(ws.getListTreadmill().size()-1).getDuration_det() + " det");
			params.put("METS", ws.getListTreadmill().get(ws.getListTreadmill().size()-1).getMets());
			params.put("ROT", ws.getListTreadmill().get(ws.getListTreadmill().size()-1).getReason_of_termination());
			params.put("MAX_ST", ws.getListTreadmill().get(ws.getListTreadmill().size()-1).getMax_st_depresi());
			params.put("RESTING_BP", ws.getListTreadmill().get(ws.getListTreadmill().size()-1).getResting_bp());
			params.put("MAX_BP", ws.getListTreadmill().get(ws.getListTreadmill().size()-1).getMax_bp());
			params.put("MAX_HEART_RATE", ws.getListTreadmill().get(ws.getListTreadmill().size()-1).getMax_heart_rate());
			params.put("INTERPRETASI", ws.getListTreadmill().get(ws.getListTreadmill().size()-1).getInterpretasi());
		}
		if(ws.getListMedLain().size() != 0) {
			params.put("MED_LAIN", ws.getListMedLain().get(ws.getListMedLain().size()-1).getKetMedisLain());
		}
		
		List<SortedMap> uwDesc = new ArrayList();
		List<String> uwDescx = new ArrayList();
		SortedMap data = new TreeMap();
		data.put("INPUT", "Ditemukan kelainan : ");
		uwDesc.add(data);
		uwDescx.add("Ditemukan kelainan : ");
		for(int a=0;a<ws.getListUwDec().size();a++) {
			if(ws.getListUwDec().get(a).getPenyakit() != null) {
				data = new TreeMap();
				String rating = "";
				String icd = "";
				String keterangan = "";
				String nilai = !ws.getListUwDec().get(a).getProd_utama_persen().equals("0") ? ws.getListUwDec().get(a).getProd_utama_persen()+ " (%)" : ws.getListUwDec().get(a).getProd_utama_permil()+ " (‰)";
				nilai = " [" + nilai + "]";
				if(!"".equals(ws.getListUwDec().get(a).getLic_id())) icd = " (" +  ws.getListUwDec().get(a).getLic_id() + ")";
				if(!"".equals(ws.getListUwDec().get(a).getLic_desc())) keterangan = " (" +  ws.getListUwDec().get(a).getLic_desc() + ")";
				data.put("INPUT", "  - "+ws.getListUwDec().get(a).getPenyakit() + nilai.toUpperCase() + icd + keterangan);
				uwDesc.add(data);
				uwDescx.add("  - "+ws.getListUwDec().get(a).getPenyakit());
			}
		}
		data = new TreeMap();
		data.put("INPUT", "  - UW Decision :");
		uwDesc.add(data);
		uwDescx.add("  - UW Decision :");
		for(int a=0;a<ws.getListUwDec().size();a++) {
			if(ws.getListUwDec().get(a).getUrutan_decision() != null && ws.getListUwDec().get(a).getUrutan_decision() > 0) {
				if(currentUser.getName().toUpperCase().equals("INGRID") || currentUser.getName().toUpperCase().equals("ASRIWULAN") || currentUser.getName().toUpperCase().equals("SELINA") || currentUser.getName().equals("GRACE_BEATRIX") || currentUser.getName().toUpperCase().equals("INGE") || currentUser.getName().toUpperCase().equals("CHANDRA_DW") || currentUser.getLus_id().equals(ws.getListUwDec().get(a).getLus_id()) || currentUser.getLde_id().equals("01")) {
					User user = elionsManager.selectUser(ws.getListUwDec().get(a).getLus_id());
					if(ws.getListUwDec().get(a).getUrutan_decision() > 1) {
						data = new TreeMap();
						data.put("INPUT", "");
						uwDesc.add(data);
						uwDescx.add("");
					}
					data = new TreeMap();
					String nilai = !ws.getListUwDec().get(a).getProd_utama_persen().equals("0") ? ws.getListUwDec().get(a).getProd_utama_persen()+ " (%)" : ws.getListUwDec().get(a).getProd_utama_permil()+ " (‰)";
					data.put("INPUT", "    [ "+ws.getListUwDec().get(a).getInput_date()+ " - " + user.getName()+ " ]"+" : "+ nilai.toUpperCase());
					uwDesc.add(data);
					uwDescx.add("    [ "+ws.getListUwDec().get(a).getInput_date()+ " - " + user.getName()+ " ]"+" : "+ nilai.toUpperCase());
//					data = new TreeMap();
//					String nilai = !ws.getListUwDec().get(a).getProd_utama_persen().equals("0") ? ws.getListUwDec().get(a).getProd_utama_persen()+ " (%)" : ws.getListUwDec().get(a).getProd_utama_permil()+ " (‰)";
//					data.put("INPUT", "    "+ uwManager.selectLstDetBisnisNamaProduk(ws.getListUwDec().get(a).getLsbs_id(),ws.getListUwDec().get(a).getLsdbs_number())+" : "+ nilai);
//					uwDesc.add(data);
//					uwDescx.add("    "+ uwManager.selectLstDetBisnisNamaProduk(ws.getListUwDec().get(a).getLsbs_id(),ws.getListUwDec().get(a).getLsdbs_number())+" : "+ nilai);
					for(int b=0;b<ws.getListUwDec().get(a).getRider().size();b++) {
						data = new TreeMap();
						nilai = !ws.getListUwDec().get(a).getRider().get(b).getRate_persen().equals("0") ? ws.getListUwDec().get(a).getRider().get(b).getRate_persen()+ " (%)" : ws.getListUwDec().get(a).getRider().get(b).getRate_permil()+ " (‰)";
						data.put("INPUT", "    "+uwManager.selectLstDetBisnisNamaProduk(ws.getListUwDec().get(a).getRider().get(b).getLsbs_id(),+ws.getListUwDec().get(a).getRider().get(b).getLsdbs_number())+" : "+ nilai.toUpperCase());
						uwDesc.add(data);
						uwDescx.add("    "+uwManager.selectLstDetBisnisNamaProduk(ws.getListUwDec().get(a).getRider().get(b).getLsbs_id(),+ws.getListUwDec().get(a).getRider().get(b).getLsdbs_number())+" : "+ nilai.toUpperCase());
					}
					data = new TreeMap();
					data.put("INPUT", "  Catatan : ");
					uwDesc.add(data);
					uwDescx.add("  Catatan : ");	
					//logger.info("String length : "+ws.getListUwDec().get(a).getCatatan().length());
					//logger.info("dibagi 130 : "+ws.getListUwDec().get(a).getCatatan().length()/130);
					//logger.info("ceil : "+Math.ceil(ws.getListUwDec().get(a).getCatatan().length()/130));
					//logger.info("max divide : "+new BigDecimal(ws.getListUwDec().get(a).getCatatan().length()).divide(new BigDecimal(93),BigDecimal.ROUND_CEILING).intValue());
					//logger.info("before : "+ws.getListUwDec().get(a).getCatatan());
			        Pattern p = Pattern.compile("(\n)*\r*");
			        Matcher m = p.matcher(ws.getListUwDec().get(a).getCatatan());
			        StringBuffer sb = new StringBuffer();
			        boolean result = m.find();
			        while(result) {
			            m.appendReplacement(sb, "");
			            result = m.find();
			        }
			        m.appendTail(sb);
			        ws.getListUwDec().get(a).setCatatan(sb.toString());
			        //logger.info("after : "+ws.getListUwDec().get(a).getCatatan());
					//logger.info("String length : "+ws.getListUwDec().get(a).getCatatan().length());
					Integer maxDivide = new BigDecimal(ws.getListUwDec().get(a).getCatatan().length()).divide(new BigDecimal(93),BigDecimal.ROUND_CEILING).intValue();
					for (int i = 0; i < maxDivide; i++) {
						if(i == 0) {
							data = new TreeMap();
							if(ws.getListUwDec().get(a).getCatatan().length() < 93) {uwDescx.add("    " + ws.getListUwDec().get(a).getCatatan());	data.put("INPUT","    " + ws.getListUwDec().get(a).getCatatan());}
							else {uwDescx.add("    " + ws.getListUwDec().get(a).getCatatan().substring(0,93));	data.put("INPUT","    " + ws.getListUwDec().get(a).getCatatan().substring(0,93));}
							uwDesc.add(data);							
							//logger.info(ws.getListUwDec().get(a).getCatatan().substring(0,93));
						}
						else if(i == maxDivide-1) {
							data = new TreeMap();
							data.put("INPUT","    " + ws.getListUwDec().get(a).getCatatan().substring(i*93));
							uwDesc.add(data);
							uwDescx.add("    " + ws.getListUwDec().get(a).getCatatan().substring(i*93));
							//logger.info(ws.getListUwDec().get(a).getCatatan().substring(i*93));
						}
						else {
							data = new TreeMap();
							data.put("INPUT","    " + ws.getListUwDec().get(a).getCatatan().substring(i*93,(i+1)*93));
							uwDesc.add(data);
							uwDescx.add("    " + ws.getListUwDec().get(a).getCatatan().substring(i*93,(i+1)*93));
						}
					}
				}
			}
		}
		Map hslReas = uwManager.getHasilReas(ws.getReg_spaj(),ws.getInsured_no());
		if(hslReas != null) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			data = new TreeMap();
			data.put("INPUT", " ");
			uwDesc.add(data);
			uwDescx.add(" ");
			data = new TreeMap();
			data.put("INPUT", "Hasil Reas");
			uwDesc.add(data);
			uwDescx.add("Hasil Reas");
			data = new TreeMap();
			data.put("INPUT", "  - Nama Co. Reas : "+ hslReas.get("LSRE_NAMA"));
			uwDesc.add(data);
			uwDescx.add("  - Nama Co. Reas : "+ hslReas.get("LSRE_NAMA"));
			data = new TreeMap();
			data.put("INPUT", "  - Tgl Akseptasi Reas : "+ df.format(hslReas.get("MSDHR_INPUT_DATE")));
			uwDesc.add(data);
			uwDescx.add("  - Tgl Akseptasi Reas : "+ df.format(hslReas.get("MSDHR_INPUT_DATE")));
			if(hslReas.get("MSDHR_EXPIRED_DATE") != null) {
				data = new TreeMap();
				data.put("INPUT", "  - Expired Date : "+ df.format(hslReas.get("MSDHR_EXPIRED_DATE")));				
				uwDesc.add(data);
				uwDescx.add("  - Expired Date : "+ df.format(hslReas.get("MSDHR_EXPIRED_DATE")));
			}
			data = new TreeMap();
			data.put("INPUT", "  - Rating : "+ hslReas.get("MSDHR_EM")+ " %");
			uwDesc.add(data);
			uwDescx.add("  - Rating : "+ hslReas.get("MSDHR_EM")+ " %");
			data = new TreeMap();
			data.put("INPUT", "  - Kelainan : "+ hslReas.get("MSDHR_ALASAN"));
			uwDesc.add(data);
			uwDescx.add("  - Kelainan : "+ hslReas.get("MSDHR_ALASAN"));	
			data = new TreeMap();
			data.put("INPUT", "  - Catatan : "+ hslReas.get("MSDHR_CATATAN"));
			uwDesc.add(data);
			uwDescx.add("  - Catatan : "+ hslReas.get("MSDHR_CATATAN"));
		}
		
		if(ws.getMsw_medis() == 1) {
			outputFilename = "uw_worksheet_p3_"+ws.getReg_spaj()+".pdf";		
			JasperUtils.exportReportToPdf(props.getProperty("report.uw.worksheet_peserta_p3") + ".jasper", outputDir, outputFilename, params, uwDesc, PdfWriter.AllowPrinting, null, null);
	
			// gabungin semua pdf jadi 1
//			List<String> pdfs = new ArrayList<String>();
//			pdfs.add(outputDir+"uw_worksheet_p1_"+ws.getReg_spaj()+".pdf");
//			pdfs.add(outputDir+"uw_worksheet_p2_"+ws.getReg_spaj()+".pdf");
//			pdfs.add(outputDir+"uw_worksheet_p3_"+ws.getReg_spaj()+".pdf");
//			OutputStream output = new FileOutputStream(outputDir+"uw_worksheet_"+ws.getReg_spaj()+".pdf");
//			MergePDF.concatPDFs(pdfs, output, false);	
			// pakai report gabungan
			params.put("INPUT", JasperReportsUtils.convertReportData(uwDesc));
			params.put("dataList", JasperReportsUtils.convertReportData(dataList));
			outputFilename = "uw_worksheet_"+ws.getReg_spaj()+".pdf";
			JasperUtils.exportReportToPdf(props.getProperty("report.uw.worksheet_peserta") + ".jasper", outputDir, outputFilename, params, dataList, PdfWriter.AllowPrinting, null, null);
		}	
		else if(ws.getMsw_medis() == 0) {
			outputFilename = "uw_worksheet_p1_"+ws.getReg_spaj()+".pdf";		
			//JasperUtils.exportReportToPdf(props.getProperty("report.uw.worksheet_non_med_p1") + ".jasper", outputDir, outputFilename, params, ws.getListSimultan(), PdfWriter.AllowPrinting, null, null);
			JasperUtils.exportReportToPdf(props.getProperty("report.uw.worksheet_non_med_p1") + ".jasper", outputDir, outputFilename, params, dataList, PdfWriter.AllowPrinting, null, null);
			outputFilename = "uw_worksheet_p2_"+ws.getReg_spaj()+".pdf";		
			JasperUtils.exportReportToPdf(props.getProperty("report.uw.worksheet_non_med_p2") + ".jasper", outputDir, outputFilename, params, uwDesc, PdfWriter.AllowPrinting, null, null);
			
			// gabungin semua pdf jadi 1
//			List<String> pdfs = new ArrayList<String>();
//			pdfs.add(outputDir+"uw_worksheet_p1_"+ws.getReg_spaj()+".pdf");
//			pdfs.add(outputDir+"uw_worksheet_p2_"+ws.getReg_spaj()+".pdf");
//			OutputStream output = new FileOutputStream(outputDir+"uw_worksheet_"+ws.getReg_spaj()+".pdf");
//			MergePDF.concatPDFs(pdfs, output, false);	
			
			// pakai report gabungan
			params.put("INPUT", JasperReportsUtils.convertReportData(uwDesc));
			//params.put("dataList", JasperReportsUtils.convertReportData(dataList));
			outputFilename = "uw_worksheet_"+ws.getReg_spaj()+".pdf";
			//JasperUtils.exportReportToPdf(props.getProperty("report.uw.worksheet_non_med") + ".jasper", outputDir, outputFilename, params, ws.getListSimultan(), PdfWriter.AllowPrinting, null, null);
			JasperUtils.exportReportToPdf(props.getProperty("report.uw.worksheet_non_med") + ".jasper", outputDir, outputFilename, params, dataList, PdfWriter.AllowPrinting, null, null);

		}
		

		FileInputStream in = null;
		ServletOutputStream ouputStream = null;
	    try{
	    	
	    	response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "Attachment;filename=uw_worksheet_"+ws.getReg_spaj()+".pdf");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
	    	
			in = new FileInputStream(outputDir+"uw_worksheet_"+ws.getReg_spaj()+".pdf");
		    ouputStream = response.getOutputStream();
		    
		    IOUtils.copy(in, ouputStream);
		}finally {
            try {
                if(in != null) {
                     in.close();
                }
                if(ouputStream != null) {
                       ouputStream.flush();
                       ouputStream.close();
                }
              }catch (Exception e) {
                    logger.error("ERROR :", e);
              }
		}
		
	}
	
	/**
	 * Fungsi untuk hak save data berdasarkan posisi nya <br/>
	 * 0 : posisi di staf, uw-med1 & uw-med2 hanya bisa view & print <br/>
	 * 1 : posisi di uw-med1, staf & uw-med2 hanya bisa view & print <br/>
	 * 2 : posisi di uw-med2, staf & uw-med1 hanya bisa view & print <br/>
	 * IT tidak terpengaruh dengan hak save
	 * @param ws
	 * @param currentUser
	 *
	 * @author Yusup_A
	 * @since May 26, 2010 (11:19:57 AM)
	 */
	private void configPosition(UwWorkSheet ws, User currentUser) {
		
		//default
//		ws.setOkSave(0);
//		ws.setOkPrint(0);
//		ws.setOkReverse1(0);
//		ws.setOkReverse2(0);
//		ws.setOkTransfer1(0);
//		ws.setOkTransfer2(0);
		
		if(ws.getIsViewer() == 0) {
			if(currentUser.getLde_id().equals("01")) {
				ws.setOkPrint(1);
				ws.setOkSave(1);
			}
			else if(currentUser.getLde_id().equals("11") || currentUser.getLde_id().equals("07")) {
				if(ws.getPosition() == 0) {
					if(currentUser.getName().equals("INGRID") || currentUser.getName().equals("ASRIWULAN") || currentUser.getName().equals("SELINA") || currentUser.getName().equals("GRACE_BEATRIX") || currentUser.getName().equals("INGE")) { 
						ws.setOkSave(0); 
						ws.setOkTransfer1(0);
						ws.setOkTransfer2(0);
					}
					else ws.setOkSave(1);
				}
				else if(ws.getPosition() == 1) {
					if(currentUser.getName().equals("ASRIWULAN") || currentUser.getName().equals("SELINA") || currentUser.getName().equals("GRACE_BEATRIX") || currentUser.getName().equals("CHANDRA_DW") || currentUser.getName().equals("INGE")) { 
						ws.setOkSave(1); 
						ws.setOkTransfer2(1);
						ws.setOkReverse1(1);
					}
					else {
						ws.setOkSave(0);
						ws.setOkTransfer2(0);
						ws.setOkReverse1(0);
					}
				}
				else if(ws.getPosition() == 2) {
					if(currentUser.getName().equals("INGRID") || currentUser.getName().equals("ASRIWULAN")) { 
						ws.setOkSave(1); 
						ws.setOkReverse1(1);
						ws.setOkReverse2(1);
					}
					else {
						ws.setOkSave(0);
						ws.setOkReverse1(0);
						ws.setOkReverse2(0);
					}
				}
				ws.setOkPrint(1);
			}
			else {
				ws.setOkPrint(0);
				ws.setOkSave(0);
			}			
		}
		else if(ws.getIsViewer() == 1) {
			ws.setOkSave(0);
			ws.setOkPrint(1);
			ws.setOkReverse1(0);
			ws.setOkReverse2(0);
			ws.setOkTransfer1(0);
			ws.setOkTransfer2(0);
		}

	}
	
	/**
	 * Fungsi untuk menset nilai "normal" & "tidak ada" di print uw worksheet jika flag medis nya bukan abnormal
	 * @param flag
	 * @param value
	 * @return 
	 * flag 0 : "normal" <br/>
	 * flag 2 : "tidak ada"  <br/> 
	 * flag 1 : value  <br/> 
	 *
	 * @author Yusup_A
	 * @since May 26, 2010 (11:22:12 AM)
	 */
	private String assignValueToReport(Integer flag, String value) {
		if(flag == 0) {
			return "normal";
		}
		else if(flag == 2) {
			return "tidak ada";
		}
		//if(flag == 0 || flag == 2) return "";
		return value;
	}
	
	/**
	 * Fungsi untuk menampilkan semua inputan medis ubnormal yg icd nya blom diisi
	 * @param ws
	 *
	 * @author yusup_a
	 * @since Jul 19, 2010 (4:10:22 PM)
	 */
	private void chekAllWarning(UwWorkSheet ws) {
		for(int a=0;a<ws.getListUrin().size();a++) {
			if(ws.getListUrin().get(a).getFlagWarna() == 1 && (ws.getListUrin().get(a).getWarna_lic_id() == null || ws.getListUrin().get(a).getWarna_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin warna");
			if(ws.getListUrin().get(a).getFlagkejernihan() == 1 && (ws.getListUrin().get(a).getKejernihan_lic_id() == null || ws.getListUrin().get(a).getKejernihan_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin kejernihan");
			if(ws.getListUrin().get(a).getFlagBj() == 1 && (ws.getListUrin().get(a).getBj_lic_id() == null || ws.getListUrin().get(a).getBj_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin berat jenis");
			if(ws.getListUrin().get(a).getFlagPh() == 1 && (ws.getListUrin().get(a).getPh_lic_id() == null || ws.getListUrin().get(a).getPh_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin pH");
			if(ws.getListUrin().get(a).getFlagProtein() == 1 && (ws.getListUrin().get(a).getProtein_lic_id() == null || ws.getListUrin().get(a).getProtein_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin protein");
			if(ws.getListUrin().get(a).getFlagGlukosa() == 1 && (ws.getListUrin().get(a).getGlukosa_lic_id() == null || ws.getListUrin().get(a).getGlukosa_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin glukosa");
			if(ws.getListUrin().get(a).getFlagKeton() == 1 && (ws.getListUrin().get(a).getKeton_lic_id() == null || ws.getListUrin().get(a).getKeton_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin keton");
			if(ws.getListUrin().get(a).getFlagBilirubin() == 1 && (ws.getListUrin().get(a).getBilirubin_lic_id() == null || ws.getListUrin().get(a).getBilirubin_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin bilirubin");
			if(ws.getListUrin().get(a).getFlagUrobilinogen() == 1 && (ws.getListUrin().get(a).getUrobilinogen_lic_id() == null || ws.getListUrin().get(a).getUrobilinogen_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin urobilinogen");
			if(ws.getListUrin().get(a).getFlagNitrit() == 1 && (ws.getListUrin().get(a).getNitrit_lic_id() == null || ws.getListUrin().get(a).getNitrit_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin nitrit");
			if(ws.getListUrin().get(a).getFlagDarah_samar() == 1 && (ws.getListUrin().get(a).getDarah_samar_lic_id() == null || ws.getListUrin().get(a).getDarah_samar_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin darah samar");
			if(ws.getListUrin().get(a).getFlagLeukosit_esterase() == 1 && (ws.getListUrin().get(a).getLekosit_esterase_lic_id() == null || ws.getListUrin().get(a).getLekosit_esterase_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin lekosit esterase");
			if(ws.getListUrin().get(a).getFlagLeukosit_esterase() == 1 && (ws.getListUrin().get(a).getLekosit_esterase_lic_id() == null || ws.getListUrin().get(a).getLekosit_esterase_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin lekosit esterase");
			if(ws.getListUrin().get(a).getFlagSedimen_eritrosit() == 1 && (ws.getListUrin().get(a).getSedimen_eritrosit_lic_id() == null || ws.getListUrin().get(a).getSedimen_eritrosit_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin sedimen eritrosit");
			if(ws.getListUrin().get(a).getFlagSedimen_eritrosit() == 1 && (ws.getListUrin().get(a).getSedimen_eritrosit_lic_id() == null || ws.getListUrin().get(a).getSedimen_eritrosit_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin sedimen eritrosit");
			if(ws.getListUrin().get(a).getFlagSedimen_leukosit() == 1 && (ws.getListUrin().get(a).getSedimen_leukosit_lic_id() == null || ws.getListUrin().get(a).getSedimen_leukosit_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin sedimen leukosit");
			if(ws.getListUrin().get(a).getFlagSedimen_epitel() == 1 && (ws.getListUrin().get(a).getSedimen_epitel_lic_id() == null || ws.getListUrin().get(a).getSedimen_epitel_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin sedimen epitel");
			if(ws.getListUrin().get(a).getFlagSedimen_silinder() == 1 && (ws.getListUrin().get(a).getSedimen_silinder_lic_id() == null || ws.getListUrin().get(a).getSedimen_silinder_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin sedimen silinder");
			if(ws.getListUrin().get(a).getFlagSedimen_kristal() == 1 && (ws.getListUrin().get(a).getSedimen_kristal_lic_id() == null || ws.getListUrin().get(a).getSedimen_kristal_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada urin sedimen kristal");
		}
		for(int a=0;a<ws.getListAda().size();a++) {
			if(ws.getListAda().get(a).getFlagHemoglobin() == 1 && (ws.getListAda().get(a).getHemoglobin_lic_id() == null || ws.getListAda().get(a).getHemoglobin_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada hemoglobin");
			if(ws.getListAda().get(a).getFlagLeukosit() == 1 && (ws.getListAda().get(a).getLeukosit_lic_id() == null || ws.getListAda().get(a).getLeukosit_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada leukosit");
			if(ws.getListAda().get(a).getFlagEosinofil() == 1 && (ws.getListAda().get(a).getEosinofil_lic_id() == null || ws.getListAda().get(a).getEosinofil_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada eosinofil");
			if(ws.getListAda().get(a).getFlagBasofil() == 1 && (ws.getListAda().get(a).getBasofil_lic_id() == null || ws.getListAda().get(a).getBasofil_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada basofil");
			if(ws.getListAda().get(a).getFlagNeutrofil_batang() == 1 && (ws.getListAda().get(a).getNeutrofil_batang_lic_id() == null || ws.getListAda().get(a).getNeutrofil_batang_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada neutrofil batang");
			if(ws.getListAda().get(a).getFlagNeutrofil_segmen() == 1 && (ws.getListAda().get(a).getNeutrofil_segmen_lic_id() == null || ws.getListAda().get(a).getNeutrofil_segmen_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada neutrofil segmen");
			if(ws.getListAda().get(a).getFlagLimfosit() == 1 && (ws.getListAda().get(a).getLimfosit_lic_id() == null || ws.getListAda().get(a).getLimfosit_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada limfosit");
			if(ws.getListAda().get(a).getFlagMonosit() == 1 && (ws.getListAda().get(a).getMonosit_lic_id() == null || ws.getListAda().get(a).getMonosit_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada monosit");
			if(ws.getListAda().get(a).getFlagTrombosit() == 1 && (ws.getListAda().get(a).getTrombosit_lic_id() == null || ws.getListAda().get(a).getTrombosit_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada trombosit");
			if(ws.getListAda().get(a).getFlagEritrosit() == 1 && (ws.getListAda().get(a).getEritrosit_lic_id() == null || ws.getListAda().get(a).getEritrosit_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada eritrosit");
			if(ws.getListAda().get(a).getFlagHematokrit() == 1 && (ws.getListAda().get(a).getHematokrit_lic_id() == null || ws.getListAda().get(a).getHematokrit_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada hematokrit");
			if(ws.getListAda().get(a).getFlagLed() == 1 && (ws.getListAda().get(a).getLed_lic_id() == null || ws.getListAda().get(a).getLed_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada led");
			if(ws.getListAda().get(a).getFlagMcv() == 1 && (ws.getListAda().get(a).getMcv_lic_id() == null || ws.getListAda().get(a).getMcv_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada mcv");
			if(ws.getListAda().get(a).getFlagMchc() == 1 && (ws.getListAda().get(a).getMchc_lic_id() == null || ws.getListAda().get(a).getMch_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada mch");
			if(ws.getListAda().get(a).getFlagMchc() == 1 && (ws.getListAda().get(a).getMchc_lic_id() == null || ws.getListAda().get(a).getMchc_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada mchc");
			if(ws.getListAda().get(a).getFlagRdw() == 1 && (ws.getListAda().get(a).getRdw_lic_id() == null || ws.getListAda().get(a).getRdw_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ada rdw");
			if(ws.getListAda().get(a).getFlagGulaDarahPuasa() == 1 && (ws.getListAda().get(a).getGula_darah_puasa_lic_id() == null || ws.getListAda().get(a).getGula_darah_puasa_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal gula darah puasa");
			if(ws.getListAda().get(a).getFlagGulaDarahPp() == 1 && (ws.getListAda().get(a).getGula_darah_pp_lic_id() == null || ws.getListAda().get(a).getGula_darah_pp_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal gula darah pp");
			if(ws.getListAda().get(a).getFlagHb1c() == 1 && (ws.getListAda().get(a).getHba1c_lic_id() == null || ws.getListAda().get(a).getHba1c_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal HbA1c");
			if(ws.getListAda().get(a).getFlagSgot() == 1 && (ws.getListAda().get(a).getSgot_lic_id() == null || ws.getListAda().get(a).getSgot_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal sgot");
			if(ws.getListAda().get(a).getFlagSgpt() == 1 && (ws.getListAda().get(a).getSgpt_lic_id() == null || ws.getListAda().get(a).getSgpt_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal sgpt");
			if(ws.getListAda().get(a).getFlagGgt() == 1 && (ws.getListAda().get(a).getGgt_lic_id() == null || ws.getListAda().get(a).getGgt_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal ggt");
			if(ws.getListAda().get(a).getFlagFosfatase_alkali() == 1 && (ws.getListAda().get(a).getFosfatase_alkali_lic_id() == null || ws.getListAda().get(a).getFosfatase_alkali_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal fosfatase alkali");
			if(ws.getListAda().get(a).getFlagBilirubin_direk() == 1 && (ws.getListAda().get(a).getBilirubin_direk_lic_id() == null || ws.getListAda().get(a).getBilirubin_direk_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal bilirubin direk");
			if(ws.getListAda().get(a).getFlagBilirubin_direk() == 1 && (ws.getListAda().get(a).getBilirubin_direk_lic_id() == null || ws.getListAda().get(a).getBilirubin_direk_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal bilirubin direk");
			if(ws.getListAda().get(a).getFlagBilirubin_indirek() == 1 && (ws.getListAda().get(a).getBilirubin_indirek_lic_id() == null || ws.getListAda().get(a).getBilirubin_indirek_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal bilirubin indirek");
			if(ws.getListAda().get(a).getFlagBilirubin_total() == 1 && (ws.getListAda().get(a).getBilirubin_total_lic_id() == null || ws.getListAda().get(a).getBilirubin_total_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal bilirubin total");
			if(ws.getListAda().get(a).getFlagAlbumin() == 1 && (ws.getListAda().get(a).getAlbumin_lic_id() == null || ws.getListAda().get(a).getAlbumin_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal albumin");
			if(ws.getListAda().get(a).getFlagGlobulin() == 1 && (ws.getListAda().get(a).getGlobulin_lic_id() == null || ws.getListAda().get(a).getGlobulin_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal globulin");
			if(ws.getListAda().get(a).getFlagTotal_protein() == 1 && (ws.getListAda().get(a).getTotal_protein_lic_id() == null || ws.getListAda().get(a).getTotal_protein_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal total protein");
			if(ws.getListAda().get(a).getFlagHbs_ag() == 1 && (ws.getListAda().get(a).getHbs_ag_lic_id() == null || ws.getListAda().get(a).getHbs_ag_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal HBs Ag");
			if(ws.getListAda().get(a).getFlagHbe_ag() == 1 && (ws.getListAda().get(a).getHbe_ag_lic_id() == null || ws.getListAda().get(a).getHbe_ag_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal HBe Ag");
			if(ws.getListAda().get(a).getFlagCreatinin() == 1 && (ws.getListAda().get(a).getCreatinin_lic_id() == null || ws.getListAda().get(a).getCreatinin_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal creatinin");
			if(ws.getListAda().get(a).getFlagUreum() == 1 && (ws.getListAda().get(a).getUreum_lic_id() == null || ws.getListAda().get(a).getUreum_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal ureum");
			if(ws.getListAda().get(a).getFlagAsam_urat() == 1 && (ws.getListAda().get(a).getAsam_urat_lic_id() == null || ws.getListAda().get(a).getAsam_urat_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada adal asam urat");
		}
		for(int a=0;a<ws.getListAbdomen().size();a++) {
			if(ws.getListAbdomen().get(a).getFlagAbdomen() == 1 && (ws.getListAbdomen().get(a).getAbdomen_lic_id() == null || ws.getListAbdomen().get(a).getAbdomen_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada usg abdomen");
		}
		for(int a=0;a<ws.getListDadaPa().size();a++) {
			if(ws.getListDadaPa().get(a).getFlagDadaPa() == 1 && (ws.getListDadaPa().get(a).getDada_pa_lic_id() == null || ws.getListDadaPa().get(a).getDada_pa_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada rontgen dada pa");
		}
		for(int a=0;a<ws.getListEkg().size();a++) {
			if(ws.getListEkg().get(a).getFlagEkg() == 1 && (ws.getListEkg().get(a).getEkg_lic_id() == null || ws.getListEkg().get(a).getEkg_lic_desc() == null))
				ws.getListWarning().add("icd code & keterangannya pada ekg");
		}
	}
	
	private UwWorkSheet addMedis(UwWorkSheet ws, Integer flag, Boolean isNew, Map<String, Object> paket) {
		
//		HashMap<String, Object> paket = new HashMap<String, Object>();
//		paket.put(medis_LPK, true);
//		paket.put(medis_URIN, true);
//		paket.put(medis_ADA, true);
//		paket.put(medis_HIV, true);
//		paket.put(medis_TUMOR, true);
//		paket.put(medis_ABDOMEN, true);
//		paket.put(medis_DADAPA, true);
//		paket.put(medis_EKG, true);
//		paket.put(medis_TREADMILL, true);
//		paket.put(medis_MEDLAIN, true);
		
		//NEW=====
		if(isNew) {
			//LPK================================
			if(paket.get(medis_LPK)!= null && ((Boolean) paket.get(medis_LPK))){
				if (ws.getListLpk().size() > 1) {
					for(int a=ws.getListLpk().size()-1;a>0;a--) ws.getListLpk().remove(a);
				}
				else if(ws.getListLpk().size() == 0) {
					ws.getListLpk().add(new UwLpk());
					ws.getListLpk().get(0).setUrutanLpk(1);
				}
			}
			//LPK2================================
			if(paket.get(medis_LPK2)!= null && ((Boolean) paket.get(medis_LPK2))){
				if (ws.getListLpk().size() > 2) {
					for(int a=ws.getListLpk().size()-1;a>1;a--) ws.getListLpk().remove(a);
				}
				else if(ws.getListLpk().size() == 0) {
					ws.getListLpk().add(new UwLpk());
					ws.getListLpk().get(0).setUrutanLpk(1);
					ws.getListLpk().add(new UwLpk());
					ws.getListLpk().get(1).setUrutanLpk(2);
				}
				else if(ws.getListLpk().size() == 1) {
					ws.getListLpk().add(new UwLpk());
					ws.getListLpk().get(1).setUrutanLpk(2);
				}
			}
			//URIN==================================
			if(paket.get(medis_URIN)!= null && ((Boolean) paket.get(medis_URIN))){
				if (ws.getListUrin().size() > 1) {
					for(int a=ws.getListUrin().size()-1;a>0;a--) ws.getListUrin().remove(a);
				}
				else if(ws.getListUrin().size() == 0) {
					ws.getListUrin().add(new UwUrin());
					ws.getListUrin().get(0).setUrutanUrin(1);
				}
			}
			//ADA======================================
			if(paket.get(medis_ADA)!= null && ((Boolean) paket.get(medis_ADA))){
				if (ws.getListAda().size() > 1) {
					for(int a=ws.getListAda().size()-1;a>0;a--) ws.getListAda().remove(a);
				}
				else if(ws.getListAda().size() == 0) {
					ws.getListAda().add(new UwAda());
					ws.getListAda().get(0).setUrutanAda(1);
				}
				if(flag == 0) ws.getListAda().get(0).setFlagAda(0);
			}
			//ADAL======================================
			if(paket.get(medis_ADAL)!= null && ((Boolean) paket.get(medis_ADAL))){
				if (ws.getListAda().size() > 1) {
					for(int a=ws.getListAda().size()-1;a>0;a--) ws.getListAda().remove(a);
				}
				else if(ws.getListAda().size() == 0) {
					ws.getListAda().add(new UwAda());
					ws.getListAda().get(0).setUrutanAda(1);
				}
				if(flag == 0) ws.getListAda().get(0).setFlagAda(1);
			}
			//HIV=======================================
			if(paket.get(medis_HIV)!= null && ((Boolean) paket.get(medis_HIV))){
				if (ws.getListHiv().size() > 1) {
					for(int a=ws.getListHiv().size()-1;a>0;a--) ws.getListHiv().remove(a);
				}
				else if(ws.getListHiv().size() == 0) {
					ws.getListHiv().add(new UwHiv());
					ws.getListHiv().get(0).setUrutanHiv(1);
				}
			}
			//TUMOR===================================
			if(paket.get(medis_TUMOR)!= null && ((Boolean) paket.get(medis_TUMOR))){
				if (ws.getListTumor().size() > 1) {
					for(int a=ws.getListTumor().size()-1;a>0;a--) ws.getListTumor().remove(a);
				}
				else if(ws.getListTumor().size() == 0) {
					ws.getListTumor().add(new UwTumor());
					ws.getListTumor().get(0).setUrutanTumor(1);
				}
			}
			//ABDOMEN=============================================
			if(paket.get(medis_ABDOMEN)!= null && ((Boolean) paket.get(medis_ABDOMEN))){
				if (ws.getListAbdomen().size() > 1) {
					for(int a=ws.getListAbdomen().size()-1;a>0;a--) ws.getListAbdomen().remove(a);
				}
				else if(ws.getListAbdomen().size() == 0) {
					ws.getListAbdomen().add(new UwAbdomen());
					ws.getListAbdomen().get(0).setUrutanAbdomen(1);
				}
			}
			//DADAPA=================================================
			if(paket.get(medis_DADAPA)!= null && ((Boolean) paket.get(medis_DADAPA))){
				if (ws.getListDadaPa().size() > 1) {
					for(int a=ws.getListDadaPa().size()-1;a>0;a--) ws.getListDadaPa().remove(a);
				}
				else if(ws.getListDadaPa().size() == 0) {
					ws.getListDadaPa().add(new UwDadaPa());
					ws.getListDadaPa().get(0).setUrutanDadaPA(1);
				}
			}
			//EKG===================================
			if(paket.get(medis_EKG)!= null && ((Boolean) paket.get(medis_EKG))){
				if (ws.getListEkg().size() > 1) {
					for(int a=ws.getListEkg().size()-1;a>0;a--) ws.getListEkg().remove(a);
				}
				else if(ws.getListEkg().size() == 0) {
					ws.getListEkg().add(new UwEkg());
					ws.getListEkg().get(0).setUrutanEkg(1);
				}
			}
			//TREADMILL===============================================
			if(paket.get(medis_TREADMILL)!= null && ((Boolean) paket.get(medis_TREADMILL))){
				if (ws.getListTreadmill().size() > 1) {
					for(int a=ws.getListTreadmill().size()-1;a>0;a--) ws.getListTreadmill().remove(a);
				}
				else if(ws.getListTreadmill().size() == 0) {
					ws.getListTreadmill().add(new UwTreadmill());
					ws.getListTreadmill().get(0).setUrutanTreadmill(1);
				}
			}
			//MEDLAIN=================================================
			if(paket.get(medis_MEDLAIN)!= null && ((Boolean) paket.get(medis_MEDLAIN))){
				if (ws.getListMedLain().size() > 1) {
					for(int a=ws.getListMedLain().size()-1;a>0;a--) ws.getListMedLain().remove(a);
				}
				else if(ws.getListMedLain().size() == 0) {
					ws.getListMedLain().add(new UwMedisLain());
					ws.getListMedLain().get(0).setUrutanMedisLain(1);
				}	
			}
			//========================================================
		}
		
		//OTHER
		//LPK & LPK2================================
		if(paket.get(medis_LPK)== null && paket.get(medis_LPK2)== null){//satu kesatuan jadinya pakai &&
			for(int a=ws.getListLpk().size()-1;a>=0;a--) ws.getListLpk().remove(a);
		}
		//URIN==================================
		if(paket.get(medis_URIN)== null){
			for(int a=ws.getListUrin().size()-1;a>=0;a--) ws.getListUrin().remove(a);
		}
		//ADA & ADAL======================================
		if(paket.get(medis_ADA)== null && paket.get(medis_ADAL)== null){//satu kesatuan jadinya pakai &&
			for(int a=ws.getListAda().size()-1;a>=0;a--) ws.getListAda().remove(a);
		}
		//HIV=======================================
		if(paket.get(medis_HIV)== null){
			for(int a=ws.getListHiv().size()-1;a>=0;a--) ws.getListHiv().remove(a);
		}
		//TUMOR===================================
		if(paket.get(medis_TUMOR)== null){
			for(int a=ws.getListTumor().size()-1;a>=0;a--) ws.getListTumor().remove(a);
		}
		//ABDOMEN=============================================
		if(paket.get(medis_ABDOMEN)== null){
			for(int a=ws.getListAbdomen().size()-1;a>=0;a--) ws.getListAbdomen().remove(a);
		}
		//DADAPA=================================================
		if(paket.get(medis_DADAPA)== null){
			for(int a=ws.getListDadaPa().size()-1;a>=0;a--) ws.getListDadaPa().remove(a);
		}
		//EKG===================================
		if(paket.get(medis_EKG)== null){
			for(int a=ws.getListEkg().size()-1;a>=0;a--) ws.getListEkg().remove(a);
		}
		//TREADMILL===============================================
		if(paket.get(medis_TREADMILL)== null){
			for(int a=ws.getListTreadmill().size()-1;a>=0;a--) ws.getListTreadmill().remove(a);
		}
		//MEDLAIN=================================================
		if(paket.get(medis_MEDLAIN)== null){
			for(int a=ws.getListMedLain().size()-1;a>=0;a--) ws.getListMedLain().remove(a);	
		}
		//========================================================
		
		return ws;
	}
}
