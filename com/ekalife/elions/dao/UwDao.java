package com.ekalife.elions.dao;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.DateUtil;
import id.co.sinarmaslife.std.util.PDFToImage;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;

import com.ekalife.elions.model.*;
import com.ekalife.elions.model.kyc.Hrc;
import com.ekalife.elions.model.kyc.NewBusinessCase;
import com.ekalife.elions.model.vo.JenisMedicalVO;
import com.ekalife.elions.model.vo.MedicalCheckupVO;
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
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.F_hit_premi_ke;
import com.ekalife.utils.F_hit_tahun_ke;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.PrintPolisPaBsm;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.parent.ParentDao;
import com.ibatis.sqlmap.client.event.RowHandler;

@SuppressWarnings("unchecked")
public class UwDao extends ParentDao {
	protected final Log logger = LogFactory.getLog( getClass() );

	DecimalFormat f1 = new DecimalFormat ("0");
	DecimalFormat f2 = new DecimalFormat ("00");
	DecimalFormat f3 = new DecimalFormat ("000");
	
	SimpleDateFormat sdfDd=new SimpleDateFormat("dd");
	SimpleDateFormat sdfMm=new SimpleDateFormat("MM");
	SimpleDateFormat sdfYy=new SimpleDateFormat("yyyy");
	SimpleDateFormat sdf_yyMM=new SimpleDateFormat("yyMM");
	SimpleDateFormat sdf_yearMonth=new SimpleDateFormat("yyyyMM");
	
	NumberFormat dec2 = new DecimalFormat("#.00;(#,##0.00)"); //
	NumberFormat dec3 = new DecimalFormat("#.000;(#,##0.000)"); //
	
	protected void initDao() throws Exception {
		this.statementNameSpace = "elions.uw.";
	}
	
	public List<DropDown> selectRekeningAjs(Integer lsbp_id, String rek) throws DataAccessException{
		Map map = new HashMap();
		map.put("lsbp_id", lsbp_id);
		map.put("rek", rek);
		
		return query("selectRekeningAjs", map);
	}
	
	public boolean selectAdaRider(String reg_spaj) throws DataAccessException{
		int ada = (Integer) querySingle("selectAdaRider", reg_spaj);
		if(ada == 0) return false; else return true;
	}
	
	public List<Scan> selectLstScan(String dept,String wajib) throws DataAccessException{
		Map map=new HashMap();
		map.put("dept",dept);
		map.put("wajib",wajib);
		return query("selectLstScan", map);
	}
	
	public String selectMaxMiIdMstInbox(String year) throws DataAccessException{
		return (String) querySingle("selectMaxMiIdMstInbox", year);
	}

	public Date selectKemarin() throws DataAccessException{
		return (Date) querySingle("selectKemarin", null);
	}
	
	public int selectSumProd(String reg_spaj, int prod_ke) throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("prod_ke", prod_ke);
		return (Integer) querySingle("selectSumProd", map);
	}
	
	public List<Map> selectInfoPosisiTerakhir(String reg_spaj) throws DataAccessException{
		return query("selectInfoPosisiTerakhir", reg_spaj);
	}
	
	public List<Map> selectInfoPosisiPasTerakhir(String msp_id) throws DataAccessException{
		return query("selectInfoPosisiPasTerakhir", msp_id);
	}
	
	public List<Map> selectDataPeserta(String reg_spaj) throws DataAccessException{
		return query("selectDataPeserta", reg_spaj);
	}
	
	public Double selectBonus(int lsco_jenis, int lev_comm, int lsco_year, int lsbs_id, int lsdbs_number) throws DataAccessException{
		Map m = new HashMap();
		m.put("lsco_jenis", lsco_jenis);
		m.put("lev_comm", lev_comm);
		m.put("lsco_year", lsco_year);
		m.put("lsbs_id", lsbs_id);
		m.put("lsdbs_number", lsdbs_number);
		return (Double) querySingle("selectBonus", m);
	}
	
	public List<Map> selectDaftarRiderEndors(String endors_no) throws DataAccessException{
		return query("selectDaftarRiderEndors", endors_no);
	}
	
	public List<Map> selectAutoRider(String endors_no) throws DataAccessException{
		return query("selectAutoRider", endors_no);
	}
	
	public Map selectRateYangDiberikan(String reg_spaj) throws DataAccessException{
		return (Map) querySingle("selectRateYangDiberikan", reg_spaj);
	}
  	
	public String selectBusinessName(String reg_spaj) throws DataAccessException {
		return (String) querySingle("selectBusinessName", reg_spaj);
	}
	
	public List<Map> selectPolisBelumDitransfer() throws DataAccessException{
		return getSqlMapClientTemplate().queryForList("elions.summary_akseptasi.selectPolisBelumDitransfer", null);
	}
	
	public Map selectEmailCabangBSM() throws DataAccessException{
		return (Map) getSqlMapClientTemplate().queryForObject("elions.summary_akseptasi.selectEmailCabangBSM", null);
	}
	
	public List<Map> selectPolisBelumDicetak() throws DataAccessException{
		return getSqlMapClientTemplate().queryForList("elions.summary_akseptasi.selectPolisBelumDicetak", null);
	}
	
	public List selectSpajMallBelumTrans() throws DataAccessException{
		return query("selectSpajMallBelumTrans", null);
	}
	
	public String selectEmailCabangBankSinarmas(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectEmailCabangBankSinarmas", reg_spaj);
	}
	
	public String selectEmailAoBankSinarmas(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectEmailAoBankSinarmas", reg_spaj);
	}
	
	public List<Date> selectSudahProsesNab(String reg_spaj) throws DataAccessException{
		return query("selectSudahProsesNab", reg_spaj);
	}
	
	public void updateBegDatePolis(String reg_spaj) throws DataAccessException{
		update("updateBegDatePolis", reg_spaj);
	}
	
	public String selectNpwpAgen(String msag_id) throws DataAccessException{
		return (String) querySingle("selectNpwpAgen", msag_id);
	}
	
	public String getClientNewId(String msag_id) throws DataAccessException{
		return (String) querySingle("selectSequenceClientID", null);
	}
	
	public int selectStatusAksep(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectStatusAksep", reg_spaj);
	}
	
	public Double selectCheckTotalUsedMstDrek(String noTrx, String payment_id, String no_spaj, Integer no_ke) throws DataAccessException{
		HashMap map = new HashMap();
		map.put("noTrx", noTrx);
		map.put("payment_id", payment_id);
		map.put("no_spaj", no_spaj);
		map.put("no_ke", no_ke);
		return (Double) querySingle("selectCheckTotalUsedMstDrek", map);
	}
	
	public List<Drek> selectMstDrek(int lsrek_id, int lsbp_id, String kode, Date startDate, Date endDate, Double startNominal, Double endNominal) throws DataAccessException{
		Map map = new HashMap();
		map.put("lsrek_id", lsrek_id);
		map.put("lsbp_id", lsbp_id);
		map.put("kodok", kode);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("startNominal", startNominal);
		map.put("endNominal", endNominal);
		return query("selectMstDrek", map);
	}
	
	public List<Company_ws> selectMstSummaryWsDet(String mcl_id, String jenis){
		Map map = new HashMap();
		map.put("mcl_id", mcl_id);
		map.put("jenis", jenis);
		return query("selectMstSummaryWsDet", map);
	}
	
	public List<ProSaveBayar> selectMstProSave(int mpb_flag_bs, String lcb_no, Date startDate, Date endDate)throws DataAccessException {
		Map map = new HashMap();
		map.put("mpb_flag_bs", mpb_flag_bs);
		map.put("lcb_no", lcb_no);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return query("selectMstProSave",map);
	}
	
	public int selectPrintCabangAtauPusat(String reg_spaj) throws DataAccessException{
		Integer result = (Integer) querySingle("selectPrintCabangAtauPusat", reg_spaj);
		if(result == null) result = -1;
		return result;
	}
	
	public void insertMstPositionSpajWithSubId(String lus_id, String msps_desc, String reg_spaj, Integer sub_id) throws DataAccessException{
		Map p = new HashMap();
		p.put("lus_id", lus_id);
		p.put("msps_desc", msps_desc);
		p.put("reg_spaj", reg_spaj);
		p.put("sub_id", sub_id);
		Boolean a = true;
		while(a){
			try{
				insert("insertMstPositionSpajWithSubId", p);
				a=false;
			}catch (Exception e) {
				a=true;
			}
		}
	}
	
	public void insertMstPositionSpaj(String lus_id, String msps_desc, String reg_spaj, int addSecond) throws DataAccessException{
		Map p = new HashMap();
		p.put("lus_id", lus_id);
		p.put("msps_desc", msps_desc);
		p.put("reg_spaj", reg_spaj);
		p.put("addSecond", addSecond);
		Boolean a = true;
		while(a){
			try{
				insert("insertMstPositionSpaj", p);
				a=false;
			}catch (Exception e) {
				a=true;
			}
		}
	}
	
	//spt direksi
	public void insertMstPositionSpajSpt(String lus_id, String msps_desc, String reg_spaj, int addSecond,String jenis) throws DataAccessException{
		Map p = new HashMap();
		p.put("lus_id", lus_id);
		p.put("msps_desc", msps_desc);
		p.put("reg_spaj", reg_spaj);
		p.put("jenis", jenis);		
		p.put("addSecond", addSecond);		
		Boolean a = true;
		while(a){
			try{
				insert("insertMstPositionSpajSpt", p);
				a=false;
			}catch (Exception e) {
				a=true;
			}
		}
		
	}
	
	public void insertMstPositionSpajRenewal(String lus_id, String reg_spaj, String msps_desc) throws DataAccessException{
		Map p = new HashMap();
		p.put("lus_id", lus_id);
		p.put("reg_spaj", reg_spaj);
		p.put("msps_desc", msps_desc);	
		insert("insertMstPositionSpajRenewal", p);		
	}
	
//	public String selectMstPositionSpajMspsDesc(String reg_spaj2, User currentUser){
//		return (String) querySingle("selectNpwpAgen", reg_spaj2, currentUser);
	
	public void insertMstPositionSpajRedFlag(String lus_id, String msps_desc, String reg_spaj, int addSecond,String jenis) throws DataAccessException{
		Map p = new HashMap();
		p.put("lus_id", lus_id);
		p.put("msps_desc", msps_desc);
		p.put("reg_spaj", reg_spaj);
		p.put("addSecond", addSecond);
		p.put("jenis", jenis);
		Boolean a = true;
		while(a){
			try{
				insert("insertMstPositionSpajRedFlag", p);
				a=false;
			}catch (Exception e) {
				a=true;
			}
		}
		
	}
	
	public void insertMstPositionSpajRedFlagTU(String lus_id, String msps_desc, String reg_spaj, int addSecond,String jenis) throws DataAccessException{
		Map p = new HashMap();
		p.put("lus_id", lus_id);
		p.put("msps_desc", msps_desc);
		p.put("reg_spaj", reg_spaj);
		p.put("addSecond", addSecond);
		p.put("jenis", jenis);
		Boolean a = true;
		while(a){
			try{
				insert("insertMstPositionSpajRedFlagTU", p);
				a=false;
			}catch (Exception e) {
				a=true;
			}
		}
	}
	
	public void insertMstPositionSpajPas(String lus_id, String msps_desc, String reg_id, int addSecond) throws DataAccessException{
		Map p = new HashMap();
		p.put("lus_id", lus_id);
		p.put("msps_desc", msps_desc);
		p.put("reg_id", reg_id);
		p.put("addSecond", addSecond);
		Boolean a = true;
		while(a){
			try{
				insert("insertMstPositionSpajPas", p);
				a=false;
			}catch (Exception e) {
				a=true;
			}
		}
	}
	
	public void insertMstPositionSpajPasBySpaj(String lus_id, String msps_desc, String reg_spaj, int addSecond) throws DataAccessException{
		Map p = new HashMap();
		p.put("lus_id", lus_id);
		p.put("msps_desc", msps_desc);
		p.put("reg_spaj", reg_spaj);
		p.put("addSecond", addSecond);
		Boolean a = true;
		while(a){
			try{
				insert("insertMstPositionSpajPasBySpaj", p);
				a=false;
			}catch (Exception e) {
				a=true;
			}
		}
	}
	
	public void updateSimpleBacSimultan(String msp_id, String lsClientTtNew, String lsClientPpNew){
		Map map = new HashMap();
		map.put("msp_id", msp_id);
		map.put("lsClientTtNew", lsClientTtNew);
		map.put("lsClientPpNew", lsClientPpNew);
		update("updateSimpleBacSimultan", map);
	}
	

	//public Integer selectCekDoubleSPH(String reg_spaj) throws DataAccessException{
	//	return (Integer) querySingle("selectCekDoubleSPH", reg_spaj);
	//}
	
	public List selectMstPolicyByCode(String nomor, Integer tipe){
		Map map = new HashMap();
		map.put("nomor", nomor);
		map.put("tipe", tipe);
		return query("selectMstPolicyByCode", map);
	}
	
	public int selectUsiaTertanggung(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectUsiaTertanggung", reg_spaj);
	}

	public int selectJumlahHariMGI(Date beg_date, Date end_date) throws DataAccessException{
		Map p = new HashMap();
		p.put("beg_date", new SimpleDateFormat("dd/MM/yyyy").format(beg_date));
		p.put("end_date", new SimpleDateFormat("dd/MM/yyyy").format(end_date));
		return (Integer) querySingle("selectJumlahHariMGI", p);
	}
	
	public Double selectTotalBunga(Date beg_date, Date end_date, Double rate, Double premi)throws DataAccessException{
		Map p = new HashMap();
		p.put("beg_date", new SimpleDateFormat("dd/MM/yyyy").format(beg_date));
		p.put("end_date", new SimpleDateFormat("dd/MM/yyyy").format(end_date));
		p.put("rate", rate);
		p.put("premi", premi);
		return (Double) querySingle("selectTotalBunga", p);
	}
	
	public int selectJumlahTransferPbp(String reg_spaj) throws DataAccessException{
		Integer result = (Integer) querySingle("selectJumlahTransferPbp", reg_spaj); 
		if(result == null) result = -1;
		return result;
	}
	
	public int selectValidasiTransferPbp(String reg_spaj) throws DataAccessException{
		Integer result = (Integer) querySingle("selectValidasiTransferPbp", reg_spaj); 
		if(result == null) result = -1;
		return result;
	}
	
	public int selectValidasiPbp(String reg_spaj) throws DataAccessException{
		Integer result = (Integer) querySingle("selectValidasiPbp", reg_spaj); 
		if(result == null) result = -1;
		return result;
	}
	
	public List<Pbp> selectPbp(String reg_spaj) throws DataAccessException{
		return (List<Pbp>) query("selectPbp", reg_spaj);
	}
	
	public Powersave selectInformasiPbp(String reg_spaj) throws DataAccessException{
		return (Powersave) querySingle("selectInformasiPbp", reg_spaj);
	}

	public String transferPbp(Command cmd, User user) throws DataAccessException{
		//update mst_pbp set flag_transfer = 1
		for(int i=0; i<cmd.getDaftarPbp().size(); i++) {
			Pbp p = cmd.getDaftarPbp().get(i);
			if(!p.getReg_spaj().equals("")) {
				Pbp update = new Pbp();
				update.setReg_spaj(p.getReg_spaj());
				update.setSpaj_bayar(p.getSpaj_bayar());
				update.setFlag_transfer(1);
				update("updateMstPbp", update);
			}
		}
		
		//insert history update
		uwDao.insertMstPositionSpaj(user.getLus_id(), "TRANSFER INFO SAVE BAYAR LINK", cmd.getPowersave().getReg_spaj(), 0);
		
		return "Data Save Bayar Link Ditransfer. Anda tidak dapat mengupdate informasi ini lagi. Terima kasih.";
	}
	
	public String savePbp(Command cmd, User user) throws DataAccessException{
		
		StringBuffer message = new StringBuffer();
		
		delete("delete.mst_pbp", cmd.getPowersave().getReg_spaj());
		
		for(int i=0; i<cmd.getDaftarPbp().size(); i++) {
			Pbp p = cmd.getDaftarPbp().get(i);
			if(!p.getReg_spaj().equals("")) {
				
				//bila begdate tidak sama, kasih warning
				if(p.getPowerSave().getMste_beg_date().compareTo(cmd.getPowersave().getMste_beg_date()) != 0) {
					message.append("\\nBegdate SPAJ SAVE Ke-" + p.getPremi_ke() + " " + p.getReg_spaj() + 
							" (" + defaultDateFormat.format(p.getPowerSave().getMste_beg_date()) + ") Tidak Sama Dengan Begdate SPAJ LINK " + cmd.getPowersave().getReg_spaj() +
							" (" + defaultDateFormat.format(cmd.getPowersave().getMste_beg_date()) + ")");
				}
				
				//insert mst_pbp
				p.setSpaj_bayar(cmd.getPowersave().getReg_spaj());
				p.setTgl_jttempo(FormatDate.add(p.getPowerSave().getMps_batas_date(), Calendar.DATE, -1));
				p.setFlag_cair(null);
				insert("insertPbp", p);
				
				//insert mst_powersave_ubah
				PowersaveUbah pu = new PowersaveUbah();
				pu.setReg_spaj(p.getReg_spaj());
				pu.setMpu_jenis(5);
				pu.setMpu_tgl_awal(FormatDate.add(p.getPowerSave().getMps_deposit_date(), Calendar.YEAR, i+1));
				pu.setLus_id(Integer.valueOf(user.getLus_id()));
				pu.setMpu_input(new Date());
				pu.setMpu_note("STOP ROLLOVER UNTUK POWERSAVE BAYAR LINK");
				pu.setMpu_print(0);
				pu.setMpu_print_reg(0);
				if(update("updatePowersaveUbah", pu) == 0) {
					insert("insertPowersaveUbah", pu);
				}
				
			}
		}
		
		//insert history update
		uwDao.insertMstPositionSpaj(user.getLus_id(), "UPDATE INFO SAVE BAYAR LINK", cmd.getPowersave().getReg_spaj(), 0);
		
		return message.toString();
	}
	
	public List<Map> selectSummaryAllNewBusiness() throws DataAccessException{
		return getSqlMapClientTemplate().queryForList("elions.summary_akseptasi.selectSummaryAllNewBusiness", null);
	}
	
	/* Summary Akseptasi */
	
	public List<Map> selectDaftarAkseptasiNyangkut(int lssa_id, boolean isEmailRequired, String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12, boolean isProductSekuritas) throws DataAccessException{
		Map params = new HashMap();
		String type = null;
		if(lssa_id == 1){
			type = "ES:";
		}else if(lssa_id == 2){
			type = "DC:";
		}else if(lssa_id == 3){
			type = "FR:";
		}else if(lssa_id == 4){
			type = "EP:";
		}else if(lssa_id == 5){
			type = "AC:";
		}else if(lssa_id == 8){
			type = "FA:";
		}else if(lssa_id == 9){
			type = "PP:";
		}else if(lssa_id == 10){
			type = "AK:";
		}
//		else if(lssa_id == 12){
//			type = "AS:";
//		}
		else{
			type = "";
		}
		
		params.put("lssa_id", lssa_id);
		params.put("type", type);
		params.put("yearbefore", yearbefore);
		params.put("month1", month1);
		params.put("month2", month2);
		params.put("month3", month3);
		params.put("month4", month4);
		params.put("month5", month5);
		params.put("month6", month6);
		params.put("month7", month7);
		params.put("month8", month8);
		params.put("month9", month9);
		params.put("month10", month10);
		params.put("month11", month11);
		params.put("month12", month12);
		if(isEmailRequired) params.put("isEmailRequired", "true");
		if(isProductSekuritas) params.put("isProductSekuritas", "true");
		return getSqlMapClientTemplate().queryForList("elions.summary_akseptasi.selectDaftarAkseptasiNyangkut", params);
	}
	
	public List<Map> selectDaftarAkseptasiNyangkutSiArco(int lssa_id, int lsbs_id ,boolean isEmailRequired, String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12) throws DataAccessException{
		Map params = new HashMap();
		String type = null;
		//String extend ="";
		if(lssa_id == 1){
			type = "ES:";
		}else if(lssa_id == 2){
			type = "DC:";
		}else if(lssa_id == 3){
			type = "FR:";
		}else if(lssa_id == 4){
			type = "EP:";
		}else if(lssa_id == 5){
			type = "AC:";
		}else if(lssa_id == 8){
			type = "FA:";
		}else if(lssa_id == 9){
			type = "PP:";
		}else if(lssa_id == 10){
			type = "AK:";
		}
		else{
			type = "";
		}
/*		
		if (lsbs_id ==183){
			extend="AND (e.lsbs_id = 183 and e.lsdbs_number  <![CDATA[>]]> 30)";
		}
		if(lsbs_id ==163){
			extend="AND e.lsbs_id = 163 ";
		}*/
		
		//params.put("lsbs_id",extend);
		params.put("lssa_id", lssa_id);
		params.put("type", type);
		params.put("yearbefore", yearbefore);
		params.put("month1", month1);
		params.put("month2", month2);
		params.put("month3", month3);
		params.put("month4", month4);
		params.put("month5", month5);
		params.put("month6", month6);
		params.put("month7", month7);
		params.put("month8", month8);
		params.put("month9", month9);
		params.put("month10", month10);
		params.put("month11", month11);
		params.put("month12", month12);
		if(isEmailRequired) params.put("isEmailRequired", "true");
		return getSqlMapClientTemplate().queryForList("elions.summary_akseptasi.selectDaftarAkseptasiNyangkutSiArco", params);
	}
	
	public List<Map> selectDaftarAkseptasiNyangkutBSMSyariah(int lssa_id, boolean isEmailRequired, String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12) throws DataAccessException{
		Map params = new HashMap();
		String type = null;
		if(lssa_id == 1){
			type = "ES:";
		}else if(lssa_id == 2){
			type = "DC:";
		}else if(lssa_id == 3){
			type = "FR:";
		}else if(lssa_id == 4){
			type = "EP:";
		}else if(lssa_id == 5){
			type = "AC:";
		}else if(lssa_id == 8){
			type = "FA:";
		}else if(lssa_id == 9){
			type = "PP:";
		}else if(lssa_id == 10){
			type = "AK:";
		}
//		else if(lssa_id == 12){
//			type = "AS:";
//		}
		else{
			type = "";
		}
		
		params.put("lssa_id", lssa_id);
		params.put("type", type);
		params.put("yearbefore", yearbefore);
		params.put("month1", month1);
		params.put("month2", month2);
		params.put("month3", month3);
		params.put("month4", month4);
		params.put("month5", month5);
		params.put("month6", month6);
		params.put("month7", month7);
		params.put("month8", month8);
		params.put("month9", month9);
		params.put("month10", month10);
		params.put("month11", month11);
		params.put("month12", month12);
		if(isEmailRequired) params.put("isEmailRequired", "true");
		return getSqlMapClientTemplate().queryForList("elions.summary_akseptasi.selectDaftarAkseptasiNyangkutBSMSyariah", params);
	}
	
	public List<Map> selectDaftarAkseptasiNyangkutTemp(int lssa_id, boolean isEmailRequired, String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12, String lastMonthLastDay) throws DataAccessException{
		Map params = new HashMap();
		String type = null;
		if(lssa_id == 1){
			type = "ES:";
		}else if(lssa_id == 2){
			type = "DC:";
		}else if(lssa_id == 3){
			type = "FR:";
		}else if(lssa_id == 4){
			type = "EP:";
		}else if(lssa_id == 5){
			type = "AC:";
		}else if(lssa_id == 8){
			type = "FA:";
		}else if(lssa_id == 9){
			type = "PP:";
		}else if(lssa_id == 10){
			type = "AK:";
		}
//		else if(lssa_id == 12){
//			type = "AS:";
//		}
		else{
			type = "";
		}
		
		params.put("lssa_id", lssa_id);
		params.put("type", type);
		params.put("yearbefore", yearbefore);
		params.put("month1", month1);
		params.put("month2", month2);
		params.put("month3", month3);
		params.put("month4", month4);
		params.put("month5", month5);
		params.put("month6", month6);
		params.put("month7", month7);
		params.put("month8", month8);
		params.put("month9", month9);
		params.put("month10", month10);
		params.put("month11", month11);
		params.put("month12", month12);
		params.put("lastMonthLastDay",lastMonthLastDay);
		if(isEmailRequired) params.put("isEmailRequired", "true");
		return getSqlMapClientTemplate().queryForList("elions.summary_akseptasi.selectDaftarAkseptasiNyangkutTemp", params);
	}	
	
	public List<Map> selectDaftarAkseptasiNyangkut3(int lssp_id,int lssa_id, boolean isEmailRequired, String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12) throws DataAccessException{
		Map params = new HashMap();
		String type = null;
		if(lssa_id == 1){
			type = "ES:";
		}else if(lssa_id == 2){
			type = "DC:";
		}else if(lssa_id == 3){
			type = "FR:";
		}else if(lssa_id == 4){
			type = "EP:";
		}else if(lssa_id == 5){
			type = "AC:";
		}else if(lssa_id == 8){
			type = "FA:";
		}else if(lssa_id == 9){
			type = "PP:";
		}else if(lssa_id == 10){
			type = "AK:";
		}else if(lssa_id == 12){
			type = "AS:";
		}else {
			type = "";
		}
		
		params.put("lssp_id", lssp_id);
		params.put("lssa_id", lssa_id);
		params.put("type", type);
		params.put("yearbefore", yearbefore);
		params.put("month1", month1);
		params.put("month2", month2);
		params.put("month3", month3);
		params.put("month4", month4);
		params.put("month5", month5);
		params.put("month6", month6);
		params.put("month7", month7);
		params.put("month8", month8);
		params.put("month9", month9);
		params.put("month10", month10);
		params.put("month11", month11);
		params.put("month12", month12);
		if(isEmailRequired) params.put("isEmailRequired", "true");
		return getSqlMapClientTemplate().queryForList("elions.summary_akseptasi.selectDaftarAkseptasiNyangkut3", params);
	}
	
	public List<Map> financeTopUp(boolean isEmailRequired, String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12) throws DataAccessException{
		Map params = new HashMap();
//		String type = null;
//		params.put("yearbefore", yearbefore);
//		params.put("month1", month1);
//		params.put("month2", month2);
//		params.put("month3", month3);
//		params.put("month4", month4);
//		params.put("month5", month5);
//		params.put("month6", month6);
//		params.put("month7", month7);
//		params.put("month8", month8);
//		params.put("month9", month9);
//		params.put("month10", month10);
//		params.put("month11", month11);
//		params.put("month12", month12);
		if(isEmailRequired) params.put("isEmailRequired", "true");
		return getSqlMapClientTemplate().queryForList("elions.summary_akseptasi.financeTopUp",params);
	}
	
	public List<Map> selectDaftarAkseptasiNyangkut2(int lssp_id, boolean isEmailRequired, String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12) throws DataAccessException{
		Map params = new HashMap();
		String type = "aaaa";
//		if(lssa_id == 1){
//			type = "ES:";
//		}else if(lssa_id == 2){
//			type = "DC:";
//		}else if(lssa_id == 3){
//			type = "FR:";
//		}else if(lssa_id == 4){
//			type = "EP:";
//		}else if(lssa_id == 5){
//			type = "AC:";
//		}else if(lssa_id == 8){
//			type = "FA:";
//		}else if(lssa_id == 9){
//			type = "PP:";
//		}else if(lssa_id == 10){
//			type = "AK:";
//		}else {
//			type = "";
//		}
		
		params.put("lssp_id", lssp_id);
		params.put("type", type);
		params.put("yearbefore", yearbefore);
		params.put("month1", month1);
		params.put("month2", month2);
		params.put("month3", month3);
		params.put("month4", month4);
		params.put("month5", month5);
		params.put("month6", month6);
		params.put("month7", month7);
		params.put("month8", month8);
		params.put("month9", month9);
		params.put("month10", month10);
		params.put("month11", month11);
		params.put("month12", month12);
		if(isEmailRequired) params.put("isEmailRequired", "true");
		return getSqlMapClientTemplate().queryForList("elions.summary_akseptasi.selectDaftarAkseptasiNyangkut2", params);
	}
	
	public String selectEmailVPFromSpaj(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectEmailVPFromSpaj", reg_spaj);
	}
	
	public Double selectTotalPremiStableLink(String reg_spaj) throws DataAccessException{
		return (Double) querySingle("selectTotalPremiStableLink", reg_spaj);
	}
	
	public Integer selectLastMscoPaid(String reg_spaj, Integer tahun_ke, Integer premi_ke) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("tahun_ke", tahun_ke);
		m.put("premi_ke", premi_ke);
		return (Integer) querySingle("selectLastMscoPaid", m);
	}
	
	public Integer selectcountProdLastAgent(String reg_spaj, Integer tahun_ke, Integer premi_ke, String msag_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("tahun_ke", tahun_ke);
		m.put("premi_ke", premi_ke);
		m.put("msag_id", msag_id);
		return (Integer) querySingle("selectcountProdLastAgent", m);
	}
	
	public Double selectMscoCommLastAgent(String reg_spaj, Integer msbi_tahun_ke, Integer msbi_premi_ke, String msag_id) throws DataAccessException{
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("msbi_tahun_ke", msbi_tahun_ke);
		params.put("msbi_premi_ke", msbi_premi_ke);
		params.put("msag_id", msag_id);
		return (Double) querySingle("selectMscoCommLastAgent", params);
		
	}

	public Double selectSumPremiMstDrekAndDet(String noTrx, String spaj) throws DataAccessException{
		Map params = new HashMap();
		params.put("noTrx", noTrx);
		params.put("spaj", spaj);
		return (Double) querySingle("selectSumPremiMstDrekAndDet", params);
	}

	public double selectKomisiPenutup(String reg_spaj, int tahun_ke, int premi_ke) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("tahun_ke", tahun_ke);
		m.put("premi_ke", premi_ke);
		Double hasil = (Double) querySingle("selectKomisiPenutup", m);
		if(hasil == null) hasil = 0.;
		return hasil;
	}
	
	public double selectKomisiPenutupRider(String reg_spaj, int tahun_ke, int premi_ke) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("tahun_ke", tahun_ke);
		m.put("premi_ke", premi_ke);
		Double hasil = (Double) querySingle("selectKomisiPenutupRider", m);
		if(hasil == null) hasil = 0.;
		return hasil;
	}
	
	public void updateTanggalTransferStableLink(String reg_spaj) {
		update("updateTanggalTransferStableLink", reg_spaj);
	}
	
	public Map selectPolisPowersaveDMTM(String reg_spaj) {
		return (Map) querySingle("selectPolisPowersaveDMTM", reg_spaj);
	}
	
	public List selectRiderPolisPas(String reg_spaj){
		return (List) query("selectRiderPolisPas", reg_spaj);
	}
	
	public Map selectPolisPAS(String reg_spaj) {
		return (Map) querySingle("selectPolisPas", reg_spaj);
	}
	
	public int selectCekAgenExpired(String msag_id, Date tanggal) {
		Map p = new HashMap();
		p.put("msag_id", msag_id);
		p.put("tanggal", tanggal);
		Integer hasil = (Integer) querySingle("selectCekAgenExpired", p);
		if(hasil == null) hasil = 0;
		return hasil;
	}
	
	public Double selectTotalPremiNewBusiness(String spaj) {
		return (Double) querySingle("selectTotalPremiNewBusiness", spaj);
	}
	
	public List selectDaftarKomisi(String dist, String prod) {
		
		String lsbs = prod.substring(0, prod.indexOf("~"));
		String lsdbs = prod.substring(prod.indexOf("~")+1);
		
		Map map = new HashMap();
		map.put("dist", dist);
		map.put("lsbs", lsbs);
		map.put("lsdbs", lsdbs);
		return query("selectDaftarKomisi", map);
	}
	
	public List selectDaftarEmailCabang() throws DataAccessException{
		return query("selectDaftarEmailCabang", null);
	}
	
	public void updatePengirimanPolis(Command cmd) {
		update("updatePengirimanPolis", cmd);
	}
	
	public List <String> selectMspsDescBasedSpaj(String spaj) throws DataAccessException{
		return (List<String>) query("selectMspsDescBasedSpaj", spaj);
	}
	
	public String selectMspsDescBasedSpajAndOtorisasi(String spaj) throws DataAccessException{
		return (String) querySingle("selectMspsDescBasedSpajAndOtorisasi", spaj);
	}
	
	public Date selectTanggalCetakSertifikatAwal(String spaj, String msps_desc) throws DataAccessException{
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("msps_desc", msps_desc);
		return (Date) querySingle("selectTanggalCetakSertifikatAwal", map);
	}
	
	public void updatePengirimanPolisTolak(String reg_spaj, Date sysdate, String kolom) throws DataAccessException {
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("sysdate", sysdate);
		params.put("kolom", kolom);
		update("updatePengirimanPolisTolak", params);
	}
	
	//public List<Map> selectListPengirimanPolis(String startDate, String endDate, Map param) throws DataAccessException{
	//	Map params = new HashMap();
	//	params.put("startDate", startDate);
	//	params.put("endDate", endDate);
	//	params.put("kondisi", param.get("kondisi"));
	//	params.put("lde_id", param.get("lde_id"));
	//	return query("selectListPengirimanPolis", params);
	//}
	
	public List<Map> selectListPengirimanPolis(String startDate, String endDate, int kondisi) throws DataAccessException{
		Map params = new HashMap();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("kondisi", kondisi);
		return query("selectListPengirimanPolis", params);
	}
	
	public List<ListPengirimanPolis> selectListPengirimanPolis() throws DataAccessException{
		return query("selectListPengirimanPolisLB", null);
	}
	public List<ListSpajTtp> selectTtpTransferAgency() throws DataAccessException{
		Map map = new HashMap();
		return query("selectTtpTransferAgency", map);
	}
	public List<ListPengirimanPolis> selectListPengirimanPolisUW() throws DataAccessException{
		return query("selectListPengirimanPolisUW", null);
	}
	
	public List<Payment> selectListPayment(String reg_spaj)throws DataAccessException{
		return query("selectListPayment", reg_spaj);
	}
	
	public List<DropDown> selectLstKurs(){
		return query("selectLstKurs", null);
	}
	
	public Payment selectMstPayment(String mspa_payment_id)throws DataAccessException{
		return (Payment) querySingle("selectMstPayment", mspa_payment_id);
	}
	
	public Payment selectMstDepositPremium(String reg_spaj)throws DataAccessException{
		return (Payment) querySingle("selectMstDepositPremium", reg_spaj);
	}
	
	public List<ListPengirimanPolis> selectListPengirimanPolisTotal() throws DataAccessException{
		return query("selectListPengirimanPolisTotal", null);
	}
	
	public List<ListPengirimanPolis> selectListPengirimanPolisBalik() throws DataAccessException{
		return query("selectListPengirimanPolisBalik", null);
	}
	
	public List<Map> selectListPengirimanPolis(String[] tambahan) throws DataAccessException{
		List<String> param = new ArrayList<String>();
		for(String s : tambahan) {
			param.add(s.trim().replace(".", ""));
		}
		return query("selectListPengirimanPolisTambahan", param);
	}
	
	public List<Map> selectListStatusMuamalat() throws DataAccessException{
		return query("selectListStatusMuamalat", null);	
	}
	
	/**
	 * Report service level dan report monitoring, semuanya bersumber ke satu query ini, agar mempermudah edit2
	 * 
	 * @author Yusuf
	 * @since May 13, 2008 (8:46:26 AM)
	 * @param jenis servicelevel, monitoring
	 * @param dist ALL, Agency, Agency (Unit-Link), Bancassurance, Bancassurance (Unit-Link), Worksite, Worksite (Unit-Link)
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map> selectReportServiceLevel(Map params) throws DataAccessException{
		return query("selectReportServiceLevel", params);
	}
	
	public List<Map> selectJenisPrintPolisUlang(Map params) throws DataAccessException{
		return query("selectJenisPrintPolisUlang", params);
	}
	
	public List<Map> selectFeeBasedPowersave(int lsbs_id, int lsdbs_number, String lku_id, int mgi) throws DataAccessException{
		Map params = new HashMap();
		params.put("lsbs_id", lsbs_id);
		params.put("lsdbs_number", lsdbs_number);
		params.put("lku_id", lku_id);
		params.put("mgi", mgi);
		return query("selectFeeBasedPowersave", params);
	}
	
	
	public Integer selectCerdasSalesOrOperation(String spaj) throws DataAccessException{
		return (Integer) querySingle("selectCerdasSalesOrOperation", spaj);
	}
	
	public Map selectInformasiCabangFromSpaj(String spaj) throws DataAccessException{
		return (Map) querySingle("selectInformasiCabangFromSpaj", spaj);
	}
	
	public List<DropDown> selectDropDown(String table_name, String key_column, String value_column, String desc_column, String order_column, String where_clause) throws DataAccessException{
		Map params = new HashMap();
		params.put("table_name", table_name);
		params.put("key_column", key_column);
		params.put("value_column", value_column);
		params.put("desc_column", (desc_column.equals("") ? value_column : desc_column));
		params.put("order_column", order_column);
		params.put("where_clause", where_clause);
		return query("selectDropDown", params);
	}
	
	public List<DropDown> selectDropDownUserUw(String table_name, String key_column, String value_column,String order_column,String where_clause) throws DataAccessException{
		Map params = new HashMap();
		params.put("table_name", table_name);
		params.put("key_column", key_column);
		params.put("value_column", value_column);
		params.put("order_column", order_column);
		params.put("where_clause", where_clause);
		return query("selectDropDownUserUw", params);
	}
	
	public List<DropDown> selectDropDownHashMap(String table_name, String key_column, String value_column, String order_column, String where_clause) throws DataAccessException{
		Map params = new HashMap();
		params.put("table_name", table_name);
		params.put("key_column", key_column);
		params.put("value_column", value_column);
		params.put("order_column", order_column);
		params.put("where_clause", where_clause);
		return query("selectDropDownHashMap", params);
	}
	
	public List<Map> selectMemorialUnitLink(String spaj) throws DataAccessException{
		return (List<Map>) query("selectMemorialUnitLink", spaj);
	}
	
	public Map selectHistoryAgen(String spaj, int lsle_id) throws DataAccessException{
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lsle_id", lsle_id);
		return (Map) querySingle("selectHistoryAgen", params);
	}
	
	public String selectRekruterAgenSys(String ls_lead_id) throws DataAccessException{
		return (String) querySingle("selectRekruterAgenSys", ls_lead_id);
	}
	
	public Map selectInfoAgenAAKM(String msag_id) throws DataAccessException{
		return (Map) querySingle("selectInfoAgenAAKM", msag_id);
	}

	public String selectLeadAAKM(String ls_lead_id) throws DataAccessException{
		String s = (String) querySingle("selectLeadAAKM", ls_lead_id);
		if(s == null) s = "";
		return s;
	}
	
	public Date selectTanggalBayar(String spaj) throws DataAccessException{
		return (Date) querySingle("selectTanggalBayar", spaj);
	}
	
	public Map selectAAKM(String spaj) throws DataAccessException{
		return (Map) querySingle("selectAAKM", spaj);
	}

	public Map selectFlagAAKMdanBPJ(String spaj, int tahun, int premi) throws DataAccessException{
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", tahun);
		params.put("premi", premi);
		return (Map) querySingle("selectFlagAAKMdanBPJ", params);
	}
	
	public Double select_biaya_akuisisi(int kode_produk,int number_produk,int cara_bayar,int ke,int period) throws SQLException {
		Map params = new HashMap();
		params.put("kode_produk", new Integer(kode_produk));
		params.put("number_produk", new Integer(number_produk));
		params.put("cara_bayar", new Integer(cara_bayar));
		params.put("ke", new Integer(ke));
		params.put("period", new Integer(period));
		Double result = (Double) getSqlMapClientTemplate().queryForObject("elions.n_prod.select_biaya_akuisisi", params);
		return result;
	}	
	
	public Double selectRateRider(String lku, int umurTertanggung, int umurPemegang, int lsbs, int jenis) throws SQLException {
		Map params = new HashMap();
		params.put("lku", lku);
		params.put("umurTertanggung", new Integer(umurTertanggung));
		params.put("umurPemegang", new Integer(umurPemegang));
		params.put("lsbs", new Integer(lsbs));
		params.put("jenis", new Integer(jenis));
		Double result = (Double) getSqlMapClientTemplate().queryForObject("elions.n_prod.selectRateRider", params);
		return result;
	}
	
	public Double selectRateUpScholarship(Integer lsbs_id, Integer umurTertanggung, Integer lsbs_number) {
		Map params = new HashMap();
		params.put("lsbs_id", lsbs_id);
		params.put("age", umurTertanggung);
		params.put("lsbs_number", lsbs_number);
		Double result = (Double) getSqlMapClientTemplate().queryForObject("elions.n_prod.selectRateUpScholarship", params);
		return result;
	}
	
	public List selectLaporanJamDua(List spaj, int jamdua) {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("cutoff", props.getProperty("time.cutoff.kustodian"));
		
		if(jamdua==0) {
			return query("selectLaporanSebelumJamDua", params);
		}else if(jamdua==1) {
			return query("selectLaporanSesudahJamDua", params);
		}
		return null;
	}
	
	public List selectLaporanKustodian(String dk, List spaj) {
		List<String> param = new ArrayList<String>();
		for (int i = 0; i < spaj.size(); i++) {
			Map sMap=(Map) spaj.get(i);
			String s=(String) sMap.get("REG_SPAJ");
			param.add(s.trim().replace(".", ""));			
		}
//			
//		}
		if(dk.equals("D")) {
			return query("selectLaporanKustodianDebet", param);
		}else if(dk.equals("K")) {
			return query("selectLaporanKustodianKredit", param);
		}
		return null;
	}
	
	public List selectLaporanKustodianFinance(String dk, List spaj) {
		List<String> param = new ArrayList<String>();
		for (int i = 0; i < spaj.size(); i++) {
			Map sMap=(Map) spaj.get(i);
			String s=(String) sMap.get("REG_SPAJ");
			param.add(s.trim().replace(".", ""));			
		}
		if(dk.equals("D")) {
			return query("selectLaporanKustodianDebetFinance", param);
		}else if(dk.equals("K")) {
			return query("selectLaporanKustodianKreditFinance", param);
		}
		return null;
	}
	
	public List selectKycNewBusiness(Date tgl_awal, Date tgl_akhir) throws DataAccessException {
		Map map = new HashMap();
		map.put("tgl_awal", tgl_awal);
		map.put("tgl_akhir", tgl_akhir);
		List hasil = query("selectKycNewBusiness", map);
		
		return hasil;
	}
	
	public Integer selectIsTopUp(String reg_spaj, Integer prod_ke) throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("prod_ke", prod_ke);
		Integer result = (Integer) querySingle("selectIsTopUp", map);
		if(result == null) result = new Integer(-1);
		return result;
	}

	public List selectReportPrintPolis(String produk, String cabang) throws DataAccessException{
		Map map = new HashMap();
		map.put("produk", produk);
		map.put("cabang", cabang);
		return query("selectReportPrintPolis", map);
	}
		
	public void updateMstDrekRecheck(String no_trx) throws DataAccessException{
		update("updateMstDrekRecheck", no_trx);
	}
	public void updateMstProSaveBayar(String mpb_bayar_id, int mpb_jenis) throws DataAccessException{
		Map map = new HashMap();
		map.put("mpb_bayar_id", mpb_bayar_id);
		map.put("mpb_jenis", mpb_jenis);
		update("updateMstProSaveBayar", map);
	}
		
	/**
	 * Dian: digunakan untuk mengosongkan spaj pada menu payment -list RK
	 * @param reg_spaj
	 * @param lus_id
	 * @param no_trx
	 * @throws DataAccessException
	 */	
	public void updateDrekKosongkanSpaj(String reg_spaj, String lus_id, String no_trx) throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("lus_id", lus_id);
		map.put("no_trx", no_trx);
		update("updateDrekKosongkanSpaj", map);
	}
	
	/**
	 *  Dian: digunakan untuk mengedit spaj pada menu payment -list RK
	 * @param reg_spaj
	 * @param lus_id
	 * @param no_trx
	 * @throws DataAccessException
	 */
	public void updateMstDrekEdit(String reg_spaj, String lus_id, String no_trx) throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("lus_id", lus_id);
		map.put("no_trx", no_trx);
		update("updateMstDrekEdit", map);
	}
	
	public void updateMscoTdkAdaKomisi(String spaj) throws DataAccessException{
		Map map = new HashMap();
		map.put("spaj", spaj);
		update("updateMscoTdkAdaKomisi", map);
	}
	
	public void updateMscoAktifKomisi(String spaj) throws DataAccessException{
		Map map = new HashMap();
		map.put("spaj", spaj);
		update("updateMscoAktifKomisi", map);
	}
	
	public void updateMscoAktifKomisiNewBusiness(String spaj, Integer msbi_tahun_ke, Integer msbi_premi_ke) throws DataAccessException{
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("msbi_tahun_ke", msbi_tahun_ke);
		map.put("msbi_premi_ke", msbi_premi_ke);
		update("updateMscoAktifKomisiNewBusiness", map);
	}
	
	public void updateMstDrek(String reg_spaj, String lus_id, String no_trx, Integer tipe, String jenis) throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("lus_id", lus_id);
		map.put("no_trx", no_trx);
		map.put("tipe", tipe);
		map.put("jenis", jenis);
		update("updateMstDrek", map);
	}
	
	public void updateMstDrekDet(String tahunKe, String premiKe, Double jumlah, String no_trx, 
			String noSpaj, Integer noKe, String paymentId, String updateId, Date updateDate, String jenis, String no_pre) throws DataAccessException{
		if( no_trx != null && !"".equals(no_trx) )
		{
			no_trx = no_trx.replace("\\", "");
		}
		Map map = new HashMap();
		map.put("tahunKe", tahunKe);
		map.put("premiKe", premiKe);
		map.put("jumlah", jumlah);
		map.put("no_trx", no_trx);
		map.put("noSpaj", noSpaj);
		map.put("noKe", noKe);
		map.put("paymentId", paymentId);	
		map.put("updateId", updateId);
		map.put("updateDate", updateDate);	
		map.put("jenis", jenis);
		map.put("no_pre", no_pre);
		update("updateMstDrekDet", map);
	}
	
	public void updateDeactiveMstDrekDet(String tahunKe, String premiKe, String no_trx, 
			String noSpaj, Integer noKe, String paymentId, String updateId, Date updateDate) throws DataAccessException{
		if( no_trx != null && !"".equals(no_trx) )
		{
			no_trx = no_trx.replace("\\", "");
		}
		Map map = new HashMap();
		map.put("tahunKe", tahunKe);
		map.put("premiKe", premiKe);
		map.put("no_trx", no_trx);
		map.put("noSpaj", noSpaj);
		map.put("noKe", noKe);
		map.put("paymentId", paymentId);	
		map.put("updateId", updateId);
		map.put("updateDate", updateDate);	
		update("updateDeactiveMstDrekDet", map);
	}
	
	
	public void updateMstDrekListRk(String reg_spaj, String lus_id, String no_trx, Integer tipe, String jenis) throws DataAccessException{
		
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("lus_id", lus_id);
		map.put("no_trx", no_trx);
		map.put("tipe", tipe);
		map.put("jenis", jenis);
		update("updateMstDrekListRk", map);
	}

	public void updateMst_productionUpp(Upp upp) throws DataAccessException{
		update("updateMst_productionUpp", upp);
	}
	
	public void updateMst_productionCommEva(Upp upp) throws DataAccessException{
		update("updateMst_productionCommEva", upp);
	}
	
	public Date selectTanggalLahirPemegang(String spaj) throws DataAccessException{
		return (Date) querySingle("selectTanggalLahirPemegang", spaj);
	}
	
	public String selectIzinMeteraiTerakhir() throws DataAccessException{
		return (String) querySingle("selectIzinMeteraiTerakhir", null);
	}
	
	public List<Position> selectAlasanPendingPrintPolis(String spaj) throws DataAccessException{
		return query("selectAlasanPendingPrintPolis", spaj);
	}
	
	public Date selectPrintDate(String spaj) {
		return (Date) querySingle("selectPrintDate", spaj);
	}
	
	public void insertMstAgencyBonus(String reg_spaj, int msbi_tahun_ke, int msbi_premi_ke, int flag_jenis, 
			int lsabt_id, String msag_id, String nama, String no_account, int lbn_id, double msco_comm, double msco_tax, 
			Date msco_pay_date, int msco_paid, int msco_active, String msco_no_pre, Integer msco_jurnal, 
			int lus_id, int msco_nilai_kurs, int flag_upload) throws DataAccessException{
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("msbi_tahun_ke", msbi_tahun_ke);
		params.put("msbi_premi_ke", msbi_premi_ke);
		params.put("flag_jenis", flag_jenis);
		params.put("lsabt_id", lsabt_id);
		params.put("msag_id", msag_id);
		params.put("nama", nama);
		params.put("no_account", no_account);
		params.put("lbn_id", lbn_id);
		params.put("msco_comm", msco_comm);
		params.put("msco_tax", msco_tax);
		params.put("msco_pay_date", msco_pay_date);
		params.put("msco_paid", msco_paid);
		params.put("msco_active", msco_active);
		params.put("msco_no_pre", msco_no_pre);
		params.put("msco_jurnal", msco_jurnal);
		params.put("lus_id", lus_id);
		params.put("msco_nilai_kurs", msco_nilai_kurs);
		params.put("flag_upload", flag_upload);
		insert("insertMstAgencyBonus", params);
	}
	
	/**
	 * Fungsi ini menyimpan history ke tabel EKA.MST_STAMP_HIST
	 * Dimana 0 = surat permohonan bea meterai
	 * 1 = surat setoran bea meterai
	 * 2 = print polis
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 15, 2007 (11:33:38 AM)
	 * @param meterai
	 */
	public void insertHistoryBeaMeterai(Meterai meterai) {
		insert("insertHistoryBeaMeterai", meterai);
	}
	
	/**
	 * Fungsi ini mengurangi saldo bea meterai bulan berjalan (atau bulan sebelumnya bila belum ada)
	 * Dimana pada object meterai, yang dibutuhkan adalah bea_meterai sebagai pengurang saldonya, 
	 * dan mstm_kode nya sebagai key  
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 15, 2007 (11:20:10 AM)
	 * @param meterai
	 * @return jumlah record yang diupdate
	 */
	public int updateSaldoBeaMeterai(Meterai meterai) {
		return update("updateSaldoBeaMeterai", meterai);
	}
	
	/**
	 * Fungsi ini hanya untuk menarik data meterai bulan berjalan (dan bulan sebelumnya)
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 15, 2007 (11:21:13 AM)
	 * @return
	 */
	public List selectMeterai() {
		return query("selectMeterai", null);
	}
	
	public int updateTanggalKirimPolis(String spaj) {
		return (Integer) update("updateTanggalKirimPolis", spaj);
	}
	
	public String validationOtorisasiSekuritas(int lus_id) {
		return (String) querySingle("validationOtorisasiSekuritas", lus_id);
	}
	
	public int validationCerdas(String spaj, int jn_nasabah) {
		//3 = gold link
		//4 = pro link
		Map param = new HashMap();
		param.put("spaj", spaj);
		param.put("jn_nasabah", jn_nasabah);
		return (Integer) querySingle("validationCerdas", param);
	}
	
	public int validationPrintPolisCabang(String spaj) {
		Date tgl_valid = (Date) querySingle("validationPrintPolisCabang", spaj);
		if(tgl_valid == null) return 0;
		else return 1;
	}
	
	public List selectEmailCabang(String cabang) {
		return query("selectEmailCabang", cabang);
	}
	
	public String selectEmailCabangFromKodeAgen(String msag_id) throws DataAccessException{
		return (String) querySingle("selectEmailCabangFromKodeAgen", msag_id);
	}
	
	public String selectEmailCabangFromSpaj(String spaj) {
		return (String) querySingle("selectEmailCabangFromSpaj", spaj);
	}
	
	public Integer selectJenisPenutupBII(String spaj) {
		return (Integer) querySingle("selectJenisPenutupBII", spaj);
	}
	
	public int updateValidForPrint(String spaj) {
		return update("updateValidForPrint", spaj);
	}
	
	public int selectIsBreakable(String spaj) {
		return (Integer) querySingle("selectIsBreakable", spaj);
	}
	
	public int selectPunyaEndors(String spaj) {
		return (Integer) querySingle("selectPunyaEndors", spaj);
	}
	
	public Map selectInformasiEmailSoftcopy(String spaj) {
		return (Map) querySingle("selectInformasiEmailSoftcopy", spaj);
	}
	
	public Map selectInformasiEmailSoftcopyPAS(String spaj) {
		return (Map) querySingle("selectInformasiEmailSoftcopyPAS", spaj);
	}
	
	public int selectJenisTerbitPolis(String spaj) {
		Map result = (Map) querySingle("selectJenisTerbitPolis", spaj);
		if(result != null) return ((BigDecimal) result.get("MSPO_JENIS_TERBIT")).intValue();
		else return -1;
	}
	
	public int selectCaraBayarFromSpaj(String spaj) {
		return (Integer) querySingle("selectCaraBayarFromSpaj", spaj);
	}
	
	public String selectLstPayMode(Integer lscbId){
		return(String)querySingle("selectLstPayMode", lscbId);
	}
	
	public int updateLeadReffBii(String kode, String spaj) {
		Map map = new HashMap();
		map.put("kode", kode);
		map.put("spaj", spaj);
		return update("updateLeadReffBii", map);
	}
	
	public int updateSelfIns(Integer mspo_self_ins, String spaj)throws DataAccessException {
		Map map = new HashMap();
		map.put("mspo_self_ins", mspo_self_ins);
		map.put("reg_spaj", spaj);
		return update("updateSelfIns", map);
	}
	
	
	public int updateTelemarketer(String tm_id, String spv_code, String spaj) {
		Map map = new HashMap();
		map.put("tm_id", tm_id);
		map.put("spv_code", spv_code);
		map.put("spaj", spaj);
		return update("updateTelemarketer", map);
	}
	
	public int updateSPVTelemarketer(String spv_code, String spaj) {
		Map map = new HashMap();
		map.put("spv_code", spv_code);
		map.put("spaj", spaj);
		return update("updateSPVTelemarketer", map);
	}
	
	public Map selectLeadNasabahFromSpaj(String spaj) {
		return (Map) querySingle("selectLeadNasabahFromSpaj", spaj);
	}

	public String selectMstPolicyMspoNoKerjasama(String reg_spaj){
		return (String) querySingle("selectMstPolicyMspoNoKerjasama", reg_spaj);
	}

	public String selectNoPB(String spaj) throws DataAccessException{
		return (String)querySingle("selectNoPB", spaj);
	}
	
	public Map selectTelemarketerFromSpaj(String spaj) {
		return (Map) querySingle("selectTelemarketerFromSpaj", spaj);
	}
	
	public Map selectLeadNasabah(String lead) {
		return (Map) querySingle("selectLeadNasabah", lead);
	}

	public List selectCariNasabah(String cari) {
		return query("selectCariNasabah", cari);
	}
	
	public List selectCariTelemarketer(String cari) {
		if(cari != null) cari = cari.toUpperCase();
		return query("selectCariTelemarketer", cari);
	}
	
	public List selectCariSPVTelemarketer(String cari) {
		if(cari != null) cari = cari.toUpperCase();
		return query("selectCariSPVTelemarketer", cari);
	}
	
	public Map selectBonusAgen(String msag_id) {
		return (Map) querySingle("selectBonusAgen", msag_id);
	}
	
	public Integer selectStatusPolisFromSpaj(String spaj) {
		return (Integer) querySingle("selectStatusPolisFromSpaj", spaj);
	}
	
	public Date selectNextBill(Date beg_date) {
		return (Date) querySingle("selectNextBill", beg_date);
	}
	
	public double selectTotalPremi(String spaj) {
		return ((Double) querySingle("selectTotalPremi", spaj)).doubleValue();
	}
	
	public Integer selectIsAgentCertified(String msag_id) {
		return (Integer) querySingle("selectIsAgentCertified", msag_id);
	}
	
	public List selectIsSimasCardClientAnAgent(String spaj) {
		return query("selectIsSimasCardClientAnAgent", spaj);
	}
	
	public Boolean prosesInsertSimasCardNew(String spaj, String mrc_no_kartu, User currentUser, Integer flag_insert){
		try{
			//flag_insert 0 = New, 1 = pengganti
			String polis = uwDao.selectNoPolisFromSpaj(spaj);
			polis = FormatString.nomorPolis(polis);
			
			int jumlahPolis = uwDao.selectJumlahPolis(spaj).size(); //ambil jumlah polis yang dia punya
			double totalPremi = uwDao.selectTotalPremi(spaj); //total semua premi nya
			
			String notes = "";
			int jenis=0; //apabila pemegang new business
			int flag_aktif=1;
			
			List isAgen = uwDao.selectIsSimasCardClientAnAgent(spaj); 
			if (!FormatString.rpad("0",(spaj.substring(0,2)),2).equalsIgnoreCase("09")){
				if(isAgen.size()>0) { //apabila pemegang seorang agent
					jenis=5; 
					String msag_id = (String) ((Map) isAgen.get(0)).get("MSAG_ID");
					
					if(uwDao.selectIsAgentCertified(msag_id)==0) { //apabila agen TIDAK certified
						notes = "[AGEN BELUM CERTIFIED] ";
						flag_aktif=0;
					}else{ //apabila agen certified
						notes = "[AGEN] ";
					}
				}
			}
			
			if(uwDao.selectJenisTerbitPolis(spaj)==1) //apabila softcopy, kasih notes aja
				uwDao.insertSimasCard(jenis, spaj, mrc_no_kartu, currentUser.getLus_id(), 
						jumlahPolis, totalPremi, 5, notes + "POLIS SOFTCOPY", flag_aktif);  
			else {
				uwDao.insertSimasCard(jenis, spaj, mrc_no_kartu, currentUser.getLus_id(), 
						jumlahPolis, totalPremi, 5, "POLIS HARDCOPY", flag_aktif); //belum print
			}
//			uwDao.updateKartuPas1(mrc_no_kartu, 1, spaj);
			uwDao.updateKartuPas2(mrc_no_kartu, 1, spaj);
			if(flag_insert==0){
				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "SIMAS CARD RECORD NO : "+ mrc_no_kartu, spaj, 0);
			}
//			//uwDao.updateKartuPas1(mrc_no_kartu, 1, spaj);
//			uwDao.updateKartuPas2(mrc_no_kartu, 1, spaj);
//			if(flag_insert==0){
//				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "INSERT MANUAL SIMAS CARD RECORD NO : "+ mrc_no_kartu, spaj, 0);
//			}
			
			return true;
		}catch (Exception e) {
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			logger.error("ERROR :", e);
			return false;
		}
	}
	
	public void deleteMstPositionSpajKirimLB(String spaj, String no_kartu) throws DataAccessException {
		Map param = new HashMap();
		param.put("spaj", spaj);
		param.put("msps_desc", no_kartu);
		delete("delete.MstPositionSpajKirimLB", param);
	}
	
	public Boolean prosesInsertSimasCardNewManual(String spaj, String mrc_no_kartu, User currentUser, Integer flag_insert, Date tgl_kirim){
		try{
//			deleteMstSimcard(mrc_no_kartu);
//			updateMstKartuPas(mrc_no_kartu);
//			deleteMstPositionSpajKirimLB(spaj, mrc_no_kartu);
			
			
			//flag_insert 0 = New, 1 = pengganti
			String polis = uwDao.selectNoPolisFromSpaj(spaj);
			polis = FormatString.nomorPolis(polis);
			
			int jumlahPolis = uwDao.selectJumlahPolis(spaj).size(); //ambil jumlah polis yang dia punya
			double totalPremi = uwDao.selectTotalPremi(spaj); //total semua premi nya
			
			String notes = "";
			int jenis=0; //apabila pemegang new business(jangan lupa insert mst_simcard datanya ambil dari pemegang polis)
//			int jenis=8; //apabila mri(jangan lupa insert mst_simcard datanya ambil dari tertanggung)
			int flag_aktif=1;
			
			List isAgen = uwDao.selectIsSimasCardClientAnAgent(spaj); 
			if (!FormatString.rpad("0",(spaj.substring(0,2)),2).equalsIgnoreCase("09")){
				if(isAgen.size()>0) { //apabila pemegang seorang agent
					jenis=5; 
					String msag_id = (String) ((Map) isAgen.get(0)).get("MSAG_ID");
					
					if(uwDao.selectIsAgentCertified(msag_id)==0) { //apabila agen TIDAK certified
						notes = "[AGEN BELUM CERTIFIED] ";
						flag_aktif=0;
					}else{ //apabila agen certified
						notes = "[AGEN] ";
					}
				}
			}
			
			if(uwDao.selectJenisTerbitPolis(spaj)==1) //apabila softcopy, kasih notes aja
				uwDao.insertSimasCardManual(jenis, spaj, mrc_no_kartu, currentUser.getLus_id(), 
						jumlahPolis, totalPremi, 5, notes + "POLIS SOFTCOPY", flag_aktif, tgl_kirim);  
			else {
				uwDao.insertSimasCardManual(jenis, spaj, mrc_no_kartu, currentUser.getLus_id(), 
						jumlahPolis, totalPremi, 5, "POLIS HARDCOPY", flag_aktif, tgl_kirim); //belum print
			}
			uwDao.updateKartuPas1(mrc_no_kartu, 1, spaj, tgl_kirim);
			uwDao.updateKartuPas2(mrc_no_kartu, 1, spaj);
			if(flag_insert==0){
				uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "INSERT MANUAL SIMAS CARD RECORD NO : "+ mrc_no_kartu, spaj, 0);
			}
			
			return true;
		}catch (Exception e) {
			logger.error("ERROR :", e);
			return false;
		}
	}
	
	/**@Fungsi:	Untuk Menginsert ke Tabel EKA.MST_SIMCARD
	 * @param 	int jenis, String spaj, String polis, String lus_id, int jumlahPolis, double totalPremi
	 * @author 	Ferry Harlim
	 * */
	public void insertSimasCard(int jenis, String spaj, String polis, String lus_id, int jumlahPolis, 
			double totalPremi, int flag_print, String notes, int flag_aktif) throws DataAccessException {
		Map params = new HashMap();
		params.put("jenis", jenis);
		params.put("spaj", spaj);
		params.put("polis", polis);
		params.put("lus_id", lus_id);
		params.put("jumlahPolis", new Integer(jumlahPolis));
		params.put("totalPremi", new Double(totalPremi));
		params.put("flag_print", flag_print);
		params.put("notes", notes);
		params.put("flag_aktif", flag_aktif);
		insert("insertSimasCard", params);
	}
	
	public void insertSimasCardManual(int jenis, String spaj, String polis, String lus_id, int jumlahPolis, 
			double totalPremi, int flag_print, String notes, int flag_aktif, Date tgl_kirim) throws DataAccessException {
		Map params = new HashMap();
		params.put("jenis", jenis);
		params.put("spaj", spaj);
		params.put("polis", polis);
		params.put("lus_id", lus_id);
		params.put("jumlahPolis", new Integer(jumlahPolis));
		params.put("totalPremi", new Double(totalPremi));
		params.put("flag_print", flag_print);
		params.put("notes", notes);
		params.put("flag_aktif", flag_aktif);
		params.put("tgl_kirim", tgl_kirim);
		insert("insertSimasCard", params);
	}
	
	/**@Fungsi:	Untuk Menampilkan jumlah polis
	 * @param 	int jenis, String spaj, String polis, String lus_id, int jumlahPolis, double totalPremi
	 * @return 	List
	 * @author 	Ferry Harlim
	 * */
	public List selectJumlahPolis(String spaj) throws DataAccessException{
		return query("selectJumlahPolis", spaj);
	}
	/**@Fungsi:	Untuk Menampilkan data pada tabel EKA.MST_SIMCARD
	 * @param 	String spaj
	 * @return 	List
	 * @author 	Ferry Harlim
	 * */
	public List selectSimasCard(String spaj) throws DataAccessException{
		return query("selectSimasCard", spaj);
	}
	
	public int updateAktifSimasCard(String spaj, int aktif) throws DataAccessException{
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("aktif", aktif);
		return update("update.mst_simcard.aktif", map);
	}	
	
	public int updateMstTransUlinkCancelPolis(String spaj, Integer lspd_id) throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj", spaj);
		map.put("lspd_id", lspd_id);
		return update("update.mst_trans_ulink_refund", map);
	}	
	
	public int updateMstDetUlinkCancelPolis(String spaj) throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj", spaj);
		return update("update.mst_det_ulink_refund", map);
	}	
	
	/**@Fungsi:	Untuk Menampilkan data pada tabel EKA.MST_NILAI
	 * @param 	String spaj
	 * @return 	List
	 * @author 	Ferry Harlim
	 * */
	public List<Nilai> selectNilai(String spaj) throws DataAccessException{
		return query("selectNilai", spaj);
	}
	
	public List<Nilai> selectNilaiMultiInvest(Map map) throws DataAccessException{
		return query("selectNilaiMultiInvest", map);
	}
	
	public Integer selectNTMI(String lsbs_id, String lsdbs_number, Integer lscb_id, Integer bayar_penuh, Integer nt_ke) throws DataAccessException{
		Map map = new HashMap();
		map.put("lsbs_id", lsbs_id);
		map.put("lsdbs_number", lsdbs_number);
		map.put("lscb_id", lscb_id);
		map.put("bayar_penuh", bayar_penuh);
		map.put("nt_ke", nt_ke);
		return (Integer) querySingle("selectNTMI", map);
	}
	
	public List<Nilai> selectNilaiMultiInvest2(Map map) throws DataAccessException{
		return query("selectNilaiMultiInvest2", map);
	}
	
	public List<Nilai> selectNilaiFromLstTable(Map map) throws DataAccessException{
		return query("selectNilaiFromLstTable", map);
	}
	
	public List<Nilai> selectNilaiFromLstTableNew(Map map) throws DataAccessException{
		return query("selectNilaiFromLstTableNew", map);
	}
	
	public List<Nilai> select208New(Map map) throws DataAccessException{
		return query("select208New", map);
	}
	
	public List selectTahapan208(String spaj) throws DataAccessException{
		return query("selectTahapan208", spaj);
	}
	
	public List selectNilaiTunai208(String spaj) throws DataAccessException{
		return query("selectNilaiTunai208", spaj);
	}
	
	public List selectNilaiTunaiTermRop(String spaj) throws DataAccessException{
		return query("selectNilaiTunaiTermRop", spaj);
	}
	
	public List<Nilai> selectNilaiFromLstNilai(Map map) throws DataAccessException{
		return query("selectNilaiFromLstNilai", map);
	}	
	
	public List<Nilai> selectNilaiFromLstNilai208(Map map) throws DataAccessException{
		return query("selectNilaiFromLstNilai208", map);
	}	
	
	public List<Nilai> selectNilaiFromLstPremiSS(Map map) throws DataAccessException{
		return query("selectNilaiFromLstPremiSS", map);
	}
	
	public List selectDaftarPremiPertama(String spaj) throws DataAccessException{
		return query("selectDaftarPremiPertama", spaj);
	}
	
	public List selectUnitLink(String spaj, int ke) throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("ke", new Integer(ke));
		return query("selectUnitLink", map);
	}
	
	public List<Map> selectInfoStableSave(String spaj) throws DataAccessException{
		return query("selectInfoStableSave", spaj);
	}
	
	public List<Map> selectInfoStableLink(String spaj) throws DataAccessException {
		return query("selectInfoStableLink", spaj);
	}
	
	public List<Map> selectInfoStableLinkAll(String spaj) throws DataAccessException {
		return query("selectInfoStableLinkAll", spaj);
	}
	
	public List<Map> selectInfoSlinkBayar(String reg_spaj, int msl_no) throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("msl_no", msl_no);
		return query("selectInfoSlinkBayar", map);
	}
	
	public List<Map> selectInfoStableLinkAllNew(String spaj) throws DataAccessException {
		return query("selectInfoStableLinkAllNew", spaj);
	}
	
	public List<Map> selectInfoStableLinkTopUp(String spaj,Integer tu_ke) throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("tu_ke", tu_ke);
		return query("selectInfoStableLinkTopUp", map);
	}
	
	public List<Map> selectInfoPSaveTopUp(String spaj,Integer tu_ke) throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("tu_ke", tu_ke);
		return query("selectInfoPSaveTopUp", map);
	}
	
	public List selectBiayaUnitLink(String spaj, int ke) throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("ke", new Integer(ke));
		return query("selectBiayaUnitLink", map);
	}
	
	public List selectDetailUnitLink(String spaj, int ke) throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("ke", new Integer(ke));
		return query("selectDetailUnitLink", map);
	}
	
	public Map selectKoefisienUpp(Upp upp) throws DataAccessException{
		return (Map) querySingle("selectKoefisienUpp", upp);
	}
	
	public Integer selectCountKoefisienUpp(String lsbs_id, String lsdbs_number) throws DataAccessException{
		Map map = new HashMap();
		map.put("lsbs_id", lsbs_id);
		map.put("lsdbs_number", lsdbs_number);
		return (Integer) querySingle("selectCountKoefisienUpp", map);
	}
	
	public Integer selectMasaGaransiInvestasi(String reg_spaj, Integer tahun_ke, Integer premi_ke) throws DataAccessException{
		Integer mgi = null;
		
		Map p = new HashMap();
		p.put("reg_spaj", reg_spaj);
		p.put("tahun_ke", tahun_ke);
		p.put("premi_ke", premi_ke);
		
		//stable link
		mgi = (Integer) querySingle("selectMGIStableLink", p);

		//stable save
		if(mgi == null) mgi = (Integer) querySingle("selectMGIStableSave", p);
		
		//powersave
		if(mgi == null) mgi = (Integer) querySingle("selectMasaGaransiInvestasi", reg_spaj);
		
		return mgi;
	}
	
	static class XmlRowHandler implements RowHandler {
		private Document domDocument;
		public XmlRowHandler(String xmlResultName) {
			domDocument = DocumentHelper.createDocument();
			getDomDocument().addElement(xmlResultName);
		}
		public void handleRow(Object object) {
			try {
				Document xmlFragment = DocumentHelper
						.parseText((String) object);
				Element xmlElement = xmlFragment.getRootElement();
				getDomDocument().getRootElement().add(xmlElement);
			} catch (DocumentException e) {
			}
		}
		public Document getDomDocument() {
			return domDocument;
		}
	}
	
	public Map selectInfoAgen2(String spaj) throws DataAccessException{
		return (Map) querySingle("selectInfoAgen2", spaj);
	}
	
	public Map selectReferalInput(String spaj) throws DataAccessException{
		return (Map) querySingle("selectReferalInput", spaj);
	}
	
	public Map selectValidBank(String spaj) throws DataAccessException{
		return (Map) querySingle("selectValidBank", spaj);
	}
	
	public List selectDaftarSPAJUnitLink(int lspd) throws DataAccessException {
		return query("selectDaftarSPAJ.UnitLink", new Integer(lspd));
	}
	
	public void updatePosisiTransUlink(int lspd, String spaj, Integer muke) throws DataAccessException {
		Map map = new HashMap();
		map.put("lspd", new Integer(lspd));
		map.put("spaj", spaj);
		map.put("muke", muke);
		update("update.mst_trans_ulink.posisi", map);
	}
	
	public void updateUploadScan( String spaj, Integer flag) throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("flag", flag);
		update("updateUploadScan", map);
	}
	
//	public List selectBiayaUnitLink(String spaj, User currenUser) throws DataAccessException {
//		Map map = new HashMap();
//		map.put("spaj", spaj);
//		map.put("currenUser", currenUser);
//		return query("selectBiayaUnitLink", map);
//	}
	
	public int flagUpdateMste_Upload_Scan( String spaj) throws DataAccessException {
		Integer result = (Integer) querySingle("flagUpdateMste_Upload_Scan", spaj);
		if(result == null) return -1; 
		else return result;	
	}
	
	public List selectDaftarSPAJUnitLink2(int lspd, String[] spaj) throws DataAccessException {
		ParameterClass pc = new ParameterClass();
		
		String a="";
		for(int i=0; i<spaj.length; i++) {
			a += (i==0?("'"+spaj[i]+"'"):(",'"+spaj[i]+"'"));
		}
		
		pc.setReg_spaj(a);
		pc.setLspd_id(new Integer(lspd));
		return query("selectDaftarSPAJ.UnitLink2", pc);
	}
	
	public List<Map<String, String>> selectMstReffBiiBySpaj(String spaj) throws DataAccessException {
		return query("selectMstReffBiiBySpaj", spaj);
	}
	
	public List selectSuratUnitLinkAlokasiInvestasi(String spaj) throws DataAccessException {
		return query("report.selectSuratUnitLink.AlokasiInvestasi", spaj);
	}
	
	public List selectSuratUnitLinkAlokasiBiaya(String spaj, Date tanggal) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tanggal", tanggal);
		return query("report.selectSuratUnitLink.AlokasiBiaya", params);
	}
	
	public List selectSuratInvestimaxAlokasiBiaya(String spaj, Date tanggal) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tanggal", tanggal);
		return query("report.selectSuratInvestimax.AlokasiBiaya", params);
	}
	
	public List selectSuratStableLinkAlokasiBiaya(String spaj, Date tanggal) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tanggal", tanggal);
		return query("report.selectSuratStableLink.AlokasiBiaya", params);
	}
		
	public List selectSuratUnitLinkAlokasiBiayaEkalink(String spaj, Date tanggal) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tanggal", tanggal);
		return query("report.selectSuratUnitLink.AlokasiBiaya.Ekalink", params);
	}
	public List selectSuratUnitLinkAlokasiBiayaSyariah(String spaj, Date tanggal) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tanggal", tanggal);
		return query("report.selectSuratUnitLink.AlokasiBiaya.Syariah", params);
	}
	public List selectSuratExcelLinkAlokasiBiayaSyariah(String spaj, Date tanggal) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tanggal", tanggal);
		return query("report.selectSuratExcelLink.AlokasiBiaya.Syariah", params);
	}
	
	public List selectSuratUnitLinkRider(String spaj) throws DataAccessException {
		return query("report.selectSuratStableLink.RincianRider", spaj);
	}
	
	public List selectSuratUnitLinkRincianTrans(String spaj, Date tanggal) throws DataAccessException {
		Map params = new HashMap();
		String lsbs = selectBusinessId(spaj);
		Integer lsdbs = selectBusinessNumber(spaj);
		params.put("spaj", spaj);
		params.put("tanggal", tanggal);
		if(products.stableLink(lsbs)) {
			if(commonDao.selectCountTotalRider(spaj)>0){
//				return query("report.selectSuratStableLink.RincianTransRider", params);
				return query("report.selectSuratStableLink.RincianTrans", params);
			}else{
				return query("report.selectSuratStableLink.RincianTrans", params);
			}
			
		}
		else if(lsbs.equals("165"))
			return query("report.selectSuratInvestimax.RincianTrans", params);
		else if(lsbs.equals("202"))
			return query("report.selectSuratExcelLink.RincianTrans", params);
		else
			return query("report.selectSuratUnitLink.RincianTrans", params);
	}
	
	public List selectDetailBisnis(String spaj) throws DataAccessException {
		return query("selectDetailBisnis", spaj);
	}
	
	public List selectswineflu(String spaj) throws DataAccessException {
		return query("selectswineflu", spaj);
	}
	/**Fungsi : Untuk Mengupdate Posisi di tabel eka.mst_billing dimana msbi_paid=1 and 
	 * 			premi_ke in(1,2,3) 
	 * 
	 * @param lspd
	 * @param spaj
	 * @param premi2
	 * @param premi3
	 * @throws DataAccessException
	 */
	public void updateBillingTtp(int lspd, String spaj) throws DataAccessException {
		//update billing ttp, hanya bila tidak 99
		Map params = new HashMap();
		params.put("lspd", new Integer(lspd));
		params.put("spaj", spaj);
		update("update.mst_billing.ttp", params);
	}
	
	public void updateBillingTtpStableLink(int lspd, String spaj) throws DataAccessException {
		//update billing ttp, hanya bila tidak 99
		Map params = new HashMap();
		params.put("lspd", new Integer(lspd));
		params.put("spaj", spaj);
		update("update.mst_billing.ttp.stablelink", params);
	}
	
	public int selectJumlahPolisAgen(String nama, String lahir) throws DataAccessException {
		Map params = new HashMap();
		params.put("nama", nama);
		params.put("lahir", lahir);
		return ((Integer) querySingle("selectJumlahPolisAgen", params));
	}
	
	public String selectAgenFromSpaj(String spaj) throws DataAccessException {
		return (String) querySingle("selectAgenFromSpaj", spaj);
	}
	public Integer selectpremiKe(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectpremiKe", spaj);
	}
	public String selectLsbsId(String spaj) throws DataAccessException{
		return(String) querySingle("selectLsbsId", spaj);
	}
	public String selectLsdbsNumber(String spaj) throws DataAccessException{
		return(String) querySingle("selectLsdbsNumber", spaj);
	}
	
	public Integer selectCountEkaSehatAndHCP(String spaj) throws DataAccessException{
		return(Integer) querySingle("selectCountEkaSehatAndHCP", spaj);
	}
	
	public Integer selectCountMstTempDMTM(String spaj) throws DataAccessException{
		return(Integer) querySingle("selectCountMstTempDMTM", spaj);
	}
	
	public Map selectRegSpajMstTempDMTM(String spaj) throws DataAccessException{
		return(Map) querySingle("selectRegSpajMstTempDMTM", spaj);
	}
	
	public String selectPolicyNoFromSpajManualMstTempDMTM(String spaj) throws DataAccessException{
		return(String) querySingle("selectPolicyNoFromSpajManualMstTempDMTM", spaj);
	}
	
	public String selectMspoPolicyNoFromRegSpajManualMstTempDMTM(String spaj) throws DataAccessException{
		return(String) querySingle("selectMspoPolicyNoFromRegSpajManualMstTempDMTM", spaj);
	}

	public Integer selectCountKirimLB(String spaj, String msps_desc) throws DataAccessException{
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("msps_desc", msps_desc);
		return(Integer) querySingle("selectCountKirimLB", map);
	}
	
	public Integer selectCountSwineFlu(String spaj) throws DataAccessException{
		return(Integer) querySingle("selectCountSwineFlu", spaj);
	}
	
	public List<HashMap> selectLsbsIdRiderHCPOrEkaSehat(String spaj) throws DataAccessException{
		return query("selectLsbsIdRiderHCPOrEkaSehat", spaj);
	}
	
	public String selectJenisReferral(String mns_kd_nasabah) throws DataAccessException {
		return (String) querySingle("selectJenisReferral", mns_kd_nasabah);
	}
	
	public List selectAgenCekKomisi(String msag) throws DataAccessException {
		return query("selectAgenCekKomisi", msag);
	}

	/*
	public void insertProductionFromCancel(int prodKe, Date prodDate, int jenisProd, int lstb_id, String spaj, int prodKe2) throws DataAccessException {
		Map param = new HashMap();
		param.put("prodKe", new Integer(prodKe));
		param.put("prodDate", prodDate);
		param.put("jenisProd", new Integer(jenisProd));
		param.put("lstb", new Integer(lstb_id));
		param.put("spaj", spaj);
		param.put("prodKe2", new Integer(prodKe2));
		insert("insertProductionFromCancel", param);
	}
	*/
	public void insertProductionFromCancel(Date prodDate, String spaj) throws DataAccessException {
		Map param = new HashMap();
		param.put("prodDate", prodDate);
		param.put("spaj", spaj);
		insert("insertProductionFromCancel", param);
	}
	/*
	public void insertDetProductionFromCancel(int prodKe, Date prodDate, int lstb_id, String spaj, int prodKe2) throws DataAccessException {
		Map param = new HashMap();
		param.put("prodKe", new Integer(prodKe));
		param.put("prodDate", prodDate);
		param.put("lstb", new Integer(lstb_id));
		param.put("spaj", spaj);
		param.put("prodKe2", new Integer(prodKe2));
		insert("insertDetProductionFromCancel", param);
	}
	*/
	public void insertDetProductionFromCancel(Date prodDate, String spaj) throws DataAccessException {
		Map param = new HashMap();
		param.put("prodDate", prodDate);
		param.put("spaj", spaj);
		insert("insertDetProductionFromCancel", param);
	}
	
	public Map selectInfoAgen(String spaj) throws DataAccessException {
		return (Map) querySingle("selectInfoAgen", spaj);
	}
	
	public int selectCekKomisi(String spaj) throws DataAccessException {
		return ((Integer) querySingle("selectCekKomisi", spaj));
	}
	
	public Integer selectTotalCsfCall(String spaj, String lus_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lus_id", lus_id);
		return (Integer) querySingle("selectTotalCsfCall", params);
	}
	
	public List selectCsfCallSummary(String spaj) throws DataAccessException {
		return query("selectCsfCallSummary", spaj);
	}
	
	public List selectCsfCallReminderList() throws DataAccessException {
		return query("selectCsfCallReminderList", null);
	}
	
	public int selectCsfCallReminder() throws DataAccessException {
		return ((Integer) querySingle("selectCsfCallReminder", null));
	}
	
	public List selectCsfCall(String spaj, String inout, String lus_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("inout", inout);
		params.put("lus_id", lus_id);
		return query("selectCsfCall", params);
	}
	
	public void updateCsfCall(String spaj, String inout, String lus_id, String s_dial, String s_jenis, String s_ket, String s_start, String s_end, String s_callback) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("inout", inout);
		params.put("lus_id", lus_id);
		params.put("s_dial", s_dial);
		params.put("s_jenis", s_jenis);
		params.put("s_ket", s_ket);
		params.put("s_start", s_start);
		params.put("s_end", s_end);
		params.put("s_callback", s_callback);
		update("updateCsfCall", params);
	}
	
	public void insertCsfCall(String spaj, String inout, String lus_id, String s_dial, String s_jenis, String s_ket, String s_start, String s_end, String s_callback, Integer mscfl_no_ref, Integer flag_finance) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("inout", inout);
		params.put("lus_id", lus_id);
		params.put("s_dial", s_dial);
		params.put("s_jenis", s_jenis);
		params.put("s_ket", s_ket);
		params.put("s_start", s_start);
		params.put("s_end", s_end);
		params.put("s_callback", s_callback);
		params.put("mscfl_no_ref", mscfl_no_ref);
		params.put("flag_finance", flag_finance);
		insert("insertCsfCall", params);
	}
	
	
	public Map selectbacCekAgen(String msag_id ) throws DataAccessException {
		Map params = new HashMap();
		params.put("msag_id", msag_id);
		return (HashMap) querySingle("cekAgen", params );
	}
	public List selectUwInfoHistoryBilling(String spaj) throws DataAccessException {
		return query("uwinfo.selectHistoryBilling", spaj);
	}
	
	public List selectUwInfoHistorySalah(String spaj) throws DataAccessException {
		return query("uwinfo.selectHistorySalah", spaj);
	}
	
	public List selectUwInfoBillingChange(String spaj) throws DataAccessException {
		return query("uwinfo.selectBillingChange", spaj);
	}
	
	public List selectUwInfoDetailBatal(String spaj) throws DataAccessException {
		return query("uwinfo.selectDetailBatal", spaj);
	}
	public List selectUwInfoTanggal(String spaj) throws DataAccessException {
		return query("uwinfo.selectTanggal", spaj);
	}
	
	public Integer selectGetMspoProvider(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectGetMspoProvider", spaj);
	}
	
	public List selectUwInfoStatusPolis(String spaj) throws DataAccessException {
		return query("uwinfo.selectStatusPolis", spaj);
	}
	
	public List selectUwInfoBeginDate(String spaj) throws DataAccessException {
		return query("uwinfo.selectBeginDate", spaj);
	}
	
	public List selectUwInfoListUlangan(String spaj) throws DataAccessException {
		return query("uwinfo.selectListUlangan", spaj);
	}
	
	public List selectUwInfoPositionSpaj(String spaj) throws DataAccessException {
		return query("uwinfo.selectPositionSpaj", spaj);
	}
	
	public List selectUwInfoPositionPas(String msp_id) throws DataAccessException {
		return query("uwinfo.selectPositionPas", msp_id);
	}
	
	public List selectUwInfoPositionFire(String msp_id) throws DataAccessException {
		return query("uwinfo.selectPositionFire", msp_id);
	}
	
	public List selectJenisTransaksi() throws DataAccessException {
		return query("selectJenisTransaksi", null);
	}
	
	public Integer selectJumlahNAB(int pos, String startDate, String endDate) throws DataAccessException {
		Map params = new HashMap();
		params.put("pos", new Integer(pos));
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return (Integer) querySingle("selectJumlahNAB", params);
	}
	
	public void insertRekeningNasabah(Rekening_client rekening) throws DataAccessException{
		insert("insertRekeningNasabah", rekening);
	}
	
	/**@Fungsi:	untuk insert stamp
	 * @param	model stamp
	 * @author 	Hemilda
	 * */
	public void insertmststamp(Stamp stamp) throws DataAccessException{
		insert("insertmststamp", stamp);
	}
	
	/**@Fungsi:	untuk insert history stamp
	 * @param	model stamp
	 * @author 	Hemilda
	 * */
	
	public void insertmststamp_hist(Stamp stamp) throws DataAccessException{
		insert("insertmststamp_hist", stamp);
	}	
	
	public int updateRekeningNasabah(Rekening_client rekening) throws DataAccessException {
		return update("update.mst_rek_client", rekening);
	}
	
	public List selectHistoryRekeningNasabah(String spaj, Integer type) throws DataAccessException{
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("type", type);
		return query("selectHistoryRekeningNasabah", params);
	}
	
	public Rekening_client selectRekeningNasabah(String spaj) throws DataAccessException {
		return (Rekening_client) querySingle("selectRekeningNasabah", spaj);
	}
	
	public Rekening_auto_debet selectRekeningAutoDebet(String spaj) throws DataAccessException {
		return (Rekening_auto_debet) querySingle("selectRekeningAutoDebet", spaj);
	}
	
	public List selectChartNav(String jenis) throws DataAccessException {
		return query("selectChartNav", jenis);
	}
	
	public List selectRincianInvestasi(String spaj) throws DataAccessException {
		return query("selectRincianInvestasi", spaj);
	}
	
	public List selectRincianInvestasiRegister(String spaj) throws DataAccessException {
		return query("selectRincianInvestasiRegister", spaj);
	}
	
	public List selectRincianInvestasiNilaiPolis(String spaj, int ke) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("ke", new Integer(ke));
		params.put("lnu", new Integer(0));
		return query("selectRincianInvestasiNilaiPolis", params);
	}
	
	public Map selectViewerDocPosition(String spaj) throws DataAccessException {
		return (HashMap) querySingle("select.viewerDocPosition", spaj);
	}
	
	public List selectViewerEndors(String noendors) throws DataAccessException {
		return query("select.viewerEndorse", noendors);
	}
	
	public List selectHistoryPengajuan(String mcl_first,Date birth) throws DataAccessException {
		Map params = new HashMap();
		params.put("mcl_first", mcl_first);
		params.put("birth", birth);
		return query("select.history_pengajuan", params);
	}
	
	public List selectBlacklist(String mcl, String type, Date tgl_lahir) throws DataAccessException {
		Map m = new HashMap();
		m.put("mcl", mcl);
		m.put("type", type);
		m.put("tgl_lahir", tgl_lahir);
		return query("select.blacklist", m);
	}
	
	public List selectViewerBilling(String spaj) throws DataAccessException {
		return query("select.viewerBilling", spaj);
	}
	
	public List selectViewerInsured(String spaj, Integer ins) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("ins", ins);
		return query("select.viewerInsured", params);
	}
	
	public List selectJenisAplikasi() throws DataAccessException {
		return query("selectJenisAplikasi", null);
	}
	
	public List selectAllDepartment() throws DataAccessException {
		return query("selectAllDepartment", "dummy"); 
	}	

	public List<SortedMap> selectAkumulasiPolisBySpaj(String spaj) throws DataAccessException {
		return query("selectAkumulasiPolisBySpaj", spaj);
	}
	
	public List selectViewerKontrolMedis(String spaj, int no, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		params.put("no", new Integer(no));
		return query("selectViewerKontrolMedis", params);
	}
	
	public String selectCountRowViewerKontrolNAV(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return (String) querySingle("selectCountRowViewerKontrolNAV", params);
	}
	
	public List selectGetYearViewerKontrolNAV(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectGetYearViewerKontrolNAV", params);
	}
	
	public List selectViewerKontrolNAV(String spaj,String bisnisId, String thnKe, String umur, int index, String ldecUp, double multiply, int varAdd, String ldecBonus, double multiplyNon, int varAddNon, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("value", spaj);
		params.put("bisnisId", bisnisId);
		params.put("thnKe", thnKe);
		params.put("umur", umur);
		params.put("index", index);
		params.put("ldecUp", ldecUp);
		params.put("multiply", multiply);
		params.put("varAdd", varAdd);
		params.put("ldecBonus", ldecBonus);
		params.put("multiplyNon", multiplyNon);
		params.put("varAddNon", varAddNon);
		params.put("lusId", lusId);
		return query("selectViewerKontrolNAV", params);
	}
	
	public List selectViewerKontrolRewards(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolRewards", params);
	}
	
	public List selectViewerKontrolKomisi(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolKomisi", params);
	}
	
	public Integer selectCountMstBillingNB(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectCountMstBillingNB", spaj);
	}
	
	public Integer selectCountMstBillingSucc(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectCountMstBillingSucc", spaj);
	}
	
	public List selectMstCommissionNewBusiness(String spaj, Integer msbi_tahun_ke, Integer msbi_premi_ke) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("msbi_tahun_ke", msbi_tahun_ke);
		params.put("msbi_premi_ke", msbi_premi_ke);
		return query("selectMstCommissionNewBusiness", params);
	}
	
	public List selectViewerKontrolReinstate(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolReinstate", params);
	}
	
	public List selectViewerKontrolBonus(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolBonus", params);
	}
	
	public List selectViewerKontrolPrivasi(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolPrivasi", params);
	}
	
	public List selectViewerKontrolMaturity(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolMaturity", params);
	}
	
	public List selectViewerKontrolReas(Map params) throws DataAccessException {
		return query("selectViewerKontrolReas", params);
	}
	
	public List selectViewerKontrolAgent(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolAgent", params);
	}
	
	public List selectViewerKontrolReferrer(String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		return query("selectViewerKontrolReferrer", params);
	}
	
	public List selectViewerKontrolDeduct(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolDeduct", params);
	}
	
	public List selectViewerKontrolPowersave(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolPowersave", params);
	}
	
	public List selectViewerKontrolClaimNilaiTunai(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolClaimNilaiTunai", params);
	}
	
	public List selectViewerKontrolPinjaman(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolPinjaman", params);
	}
	public List selectViewerKontrolSimpanan(List spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolSimpanan", params);
	}
	public List selectViewerKontrolBilling(List spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolBilling", params);
	}
	public List selectViewerKontrolTahapan(List spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolTahapan", params);
	}
	
	public List selectViewerKontrolStableLink(String spaj, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lusId", lusId);
		return query("selectViewerKontrolStableLink", params);
	}
	
	public Map selectGetInfoForRate(String regSpaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("regSpaj", regSpaj);
		return (HashMap) querySingle("selectGetInfoForRate", params);
	}
	
	public String selectGetLdecRate(String bisnisId, String thnKe, String umur) throws DataAccessException {
		Map params = new HashMap();
		params.put("bisnisId", bisnisId);
		params.put("thnKe", thnKe);
		params.put("umur", umur);
		return (String) querySingle("selectGetLdecRate", params);
	}
	
	public String selectGetLdecRateIfNoRow(String bisnisId, String thnKe, String umur) throws DataAccessException {
		Map params = new HashMap();
		params.put("bisnisId", bisnisId);
		params.put("thnKe", thnKe);
		params.put("umur", umur);
		return (String) querySingle("selectGetLdecRateIfNoRow", params);
	}
	
	public int selectCekSpajBayar(String spaj) {
		return (Integer) querySingle("selectCekSpajBayar", spaj);
	}
	
	public Double selectJumlahBayarTahapanUsingOldPolicy(String[] oldpol) throws DataAccessException{
		List<String> p = new ArrayList<String>();
		for(String s: oldpol){
			p.add(s);
		}
		return (Double) querySingle("selectJumlahBayarTahapanUsingOldPolicy", p);
	}
	
	public Double selectJumlahBayarTahapan(String spaj, Date begdate) throws DataAccessException {
		Map m = new HashMap();
		m.put("spaj", spaj);
		m.put("begdate", begdate);
		return (Double) querySingle("selectJumlahBayarTahapan", m);
	}
	
	public Map selectPolisSpajPemegang(String spaj) throws DataAccessException {
		return (HashMap) querySingle("selectPolisSpajPemegang", spaj);
	}

	public List selectNilaiSavingDevidentMaturity(String spaj,
			Integer lamaTanggung) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lamaTanggung", lamaTanggung);
		return query("selectNilaiSavingDevidentMaturity", params);
	}

	public List selectEndorseEdmNewFoot(String endorse) throws DataAccessException {
		return query("selectEndorseEdmNewFoot", endorse);
	}

	public List selectEndorseJadwalBayar(String endorse) throws DataAccessException {
		return query("selectEndorseJadwalBayar", endorse);
	}

	public List selectEndorseBebasPremi(String spaj, int lje) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lje", new Integer(lje));
		return query("selectEndorseBebasPremi", params);
	}

	public List selectEndorseBankClause(String endorse, String spaj,
			String insured, int msaw) throws DataAccessException {
		Map params = new HashMap();
		params.put("endorse", endorse);
		params.put("spaj", spaj);
		params.put("insured", insured);
		params.put("msaw", new Integer(msaw));
		return query("selectEndorseBankClause", params);
	}

	public List selectCetakWarisBaru(String endorse) throws DataAccessException {
		return query("selectCetakWarisBaru", endorse);
	}

	public List selectCetakWarisLama(String endorse) throws DataAccessException {
		return query("selectCetakWarisLama", endorse);
	}

	public Date kurang_tanggal(String tanggal, Integer hari)throws DataAccessException {
		Map m = new HashMap();
		m.put("tanggal",tanggal);
		m.put("hari",hari);
		return (Date)querySingle("kurang_tanggal",m);
	}
	
	public Integer selectCountCancel(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectCountCancel", spaj);
	}
	
	public Integer selectCountsimassehat(String tgl1 , String tgl2)throws DataAccessException {
		Map m = new HashMap();
		m.put("tgl1", tgl1);
		m.put("tgl2", tgl2);
		return (Integer) querySingle("selectCountsimassehat",m);
	}

	public List selectEndorseRubahPlanViewAll(String no, int lsje) throws DataAccessException {
		Map m = new HashMap();
		m.put("no", no);
		m.put("lsje", new Integer(lsje));
		return query("selectEndorseRubahPlanViewAll", m);
	}

	public Map selectNamaKota(String lska) throws DataAccessException {
		return (HashMap) querySingle("selectNamaKota", lska);
	}

	public String selectMclFirst(String mcl) throws DataAccessException {
		return (String) querySingle("selectMclFirst", mcl);
	}

	public Integer selectCountEndorse(String noEndorse) throws DataAccessException {
		return (Integer) querySingle("selectCountEndorse", noEndorse);
	}

	public Date selectBirthday(String str) throws DataAccessException {
		return (Date) querySingle("selectBirthday", str);
	}

	public String selectTertanggungFromEndorse(String no) throws DataAccessException {
		return (String) querySingle("selectTertanggungFromEndorse", no);
	}

	public List selectCetakHeaderEndorseNew(String no, int lsje) throws DataAccessException {
		Map m = new HashMap();
		m.put("no", no);
		m.put("lsje", new Integer(lsje));
		return query("selectCetakHeaderEndorse.new", m);
	}

	public List selectCetakHeaderEndorse(String endorse) throws DataAccessException {
		return query("selectCetakHeaderEndorse", endorse);
	}

	public List selectCetakHeaderEndorseSsh(String endorse) throws DataAccessException {
		return query("selectCetakHeaderEndorse.ssh", endorse);
	}

	public List selectCekDetailEndorse(String endorse) throws DataAccessException {
		return query("selectCekDetailEndorse", endorse);
	}

	public List selectInfoEndorse(String noEndorse) throws DataAccessException {
		return query("selectInfoEndorse", noEndorse);
	}

	public List selectAllEndorsements(String spaj) throws DataAccessException {
		return query("selectAllEndorsements", spaj);
	}
   
	public List selectAllEndorse(String spaj) throws DataAccessException {
		return query("selectAllEndorse", spaj);
	}
	
	public List selectAllSpaj(String spaj) throws DataAccessException {
		return query("selectAllSpaj", spaj);
	}

	public List selectWilayah() throws DataAccessException {
		return query("selectWilayah", null);
	}
	
	public List selectWilayah(String like) throws DataAccessException {
		return query("selectWilayah", like);
	}
	
	public List selectGelar(Integer jenis) throws DataAccessException {
		return query("selectGelar", jenis);
	}

	public List selectRegions(String query) throws DataAccessException {
		return query("selectRegions", query);
	}

	public List select_Icd(String query) throws DataAccessException {
		return query("selectIcdCode", query);
	}	
	
	public Map selectStatusPolis(int lssp) throws DataAccessException {
		return (HashMap) querySingle("selectStatusPolis", new Integer(lssp));
	}
	
	public String selectMclIDPemegangPolis(String spaj) throws DataAccessException{
		return (String) querySingle("selectMclIDPemegangPolis", spaj);
	}

	public void insertMst_batal(String spaj, String sekuens, String alasan,
			String lus_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("sekuens", sekuens);
		params.put("alasan", alasan);
		params.put("lus_id", lus_id);
		insert("insert.mst_batal", params);
	}

	public void insertMst_bvoucher(String ls_no_pre, int li_run, String ls_ket,
			Double ar_jumlah, Double ldec_kosong, String project, String budget, String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("ls_no_pre", ls_no_pre.trim());
		params.put("li_run", new Integer(li_run));
		params.put("ls_ket", ls_ket);
		params.put("ar_jumlah", ar_jumlah);
		params.put("ldec_kosong", ldec_kosong);
		params.put("project", project);
		params.put("budget", budget);
		params.put("spaj", spaj);
		insert("insert.mst_bvoucher", params);
	}

	public void insertMst_comm_reff_bii(SpajBill spajBill) throws DataAccessException {
		List cekSpajPromo = bacDao.selectCekSpajPromo(  null , spajBill.getNo_spaj(),  "1"); // cek spaj free (Promo Free Smile Medical) sudah terdaftar atau belum MST_FREE_SPAJ
		if(cekSpajPromo.isEmpty()){ // komisi tidak di input jika merupakan SPAJ free - Ridhaal 
			insert("insert.mst_comm_reff_bii", spajBill);
		}
	}
	
	public String selectBulanProduksi(String spaj) throws DataAccessException{
		String result = (String) querySingle("selectBulanProduksi", spaj); 
		return result==null?"0":result;
	}
	
	public void insertUploadRefund(SpajBill spajBill) throws DataAccessException{
		insert("insert.mst_upload_refund", spajBill);
	}

	public void insertMst_commission(Commission komisi, Date msco_date) throws DataAccessException {
		//logger.info("LEVEL " + komisi.getLev_kom() + " = " + dec2.format(komisi.getKomisi()));
		if((komisi.getKomisi()-komisi.getTax())>0) {
			komisi.msco_date = msco_date;
			insert("insert.mst_commission", komisi);
		}
	}
	
	public void insertMst_diskon_perusahaan(Commission komisi) throws DataAccessException {
			insert("insert.mst_diskon_perusahaan", komisi);
	}

	public void insertMst_dbank(String ls_no_pre, int li_run, String ls_ket,
			String ls_kas, Double ar_jumlah, Integer kode_cash_flow, String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("ls_no_pre", ls_no_pre.trim());
		params.put("li_run", new Integer(li_run));
		params.put("ls_ket", ls_ket);
		params.put("ls_kas", ls_kas);
		params.put("ar_jumlah", ar_jumlah);
		params.put("kode_cash_flow", kode_cash_flow);
		params.put("spaj", spaj);
		insert("insert.mst_dbank", params);
	}

	public void insertMst_deduct(String co_id, double ldec_sisa, String lus_id,
			int msdd, int lsjd, String desc) throws DataAccessException {
		Map params = new HashMap();
		params.put("co_id", co_id);
		params.put("ldec_sisa", new Double(ldec_sisa));
		params.put("lus_id", lus_id);
		params.put("msdd", new Integer(msdd));
		params.put("lsjd", new Integer(lsjd));
		params.put("desc", desc);
		insert("insert.mst_deduct", params);
	}

	public void insertMst_det_deposito(String spaj, int li_ke, int li_insert,
			double ldec_depo, double ldec_persen_dep, double ldec_saldo_depo) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("li_ke", new Integer(li_ke));
		params.put("li_insert", new Integer(li_insert));
		params.put("ldec_depo", new Double(ldec_depo));
		params.put("ldec_persen_dep", new Double(ldec_persen_dep));
		params.put("ldec_saldo_depo", new Double(ldec_saldo_depo));
		insert("insert.mst_det_deposito", params);
	}

	public void insertMst_det_privasi(String spaj, int li_ke, int urutan,
			double ldec_persen, double ldec_jlh_premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("li_ke", new Integer(li_ke));
		params.put("urutan", new Integer(urutan));
		params.put("ldec_persen", new Double(ldec_persen));
		params.put("ldec_jlh_premi", new Double(ldec_jlh_premi));
		insert("insert.mst_det_privasi", params);
	}

	public void insertMst_detail_production(String spaj, Date prodDate,
			String lsbs_id, String lsdbs_number, Integer prod_ke,
			Integer tahun, Integer premi, Double msdb_premium,
			Double msdpr_discount, Double mspr_tsi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("prodDate", prodDate);
		params.put("lsbs_id", lsbs_id);
		params.put("lsdbs_number", lsdbs_number);
		params.put("tahun", tahun);
		params.put("premi", premi);
		params.put("prod_ke", prod_ke);
		params.put("msdb_premium", msdb_premium);
		params.put("msdpr_discount", msdpr_discount);
		params.put("mspr_tsi", mspr_tsi);
		insert("insert.mst_detail_production", params);
	}
	
	public int selectValidasiProduksiDouble(String reg_spaj, int prod_ke) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("prod_ke", prod_ke);
		return (Integer) querySingle("selectValidasiProduksiDouble", m);
	}

	public void insertMst_payment(TopUp topup, String userId) throws DataAccessException {
		Map params = new HashMap();
		params.put("topup", topup);
		params.put("lus_id", userId);
		insert("insert.mst_payment", params);
	}

	public void insertMst_privasi(String spaj, int li_ke, double premi,
			double rider, int tahun_ke, int premi_ke, String lus_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("li_ke", new Integer(li_ke));
		params.put("premi", new Double(premi));
		params.put("rider", new Double(rider));
		params.put("tahun_ke", new Integer(tahun_ke));
		params.put("premi_ke", new Integer(premi_ke));
		params.put("lus_id", lus_id);
		params.put("dk", "D");
		params.put("lspd", new Integer(99));
		insert("insert.mst_privasi", params);
	}

	public Upp selectAgenBuatHitungUppEvaluasi(String spaj, int prod_ke) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("prod_ke", new Integer(prod_ke));
		return (Upp) querySingle("selectAgenBuatHitungUppEvaluasi", params);
	}
	
	public void insertUpp(Upp upp) throws DataAccessException{
		insert("insertUpp", upp);
	}
	
	public void insertMst_production(Date rkDate, Date prodDate, String spaj, int tahun,
			int premi, int prod_ke, double upp_eva, String s_agent_1, String s_agent_2, String s_agent_3, 
			String s_agent_4, String s_agent_5) throws DataAccessException {
		Map params = new HashMap();
		params.put("rkDate", rkDate);
		params.put("prodDate", prodDate);
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		params.put("prod_ke", new Integer(prod_ke));
		params.put("upp_eva", upp_eva);
		params.put("s_agent_1", s_agent_1);
		params.put("s_agent_2", s_agent_2);
		params.put("s_agent_3", s_agent_3);
		params.put("s_agent_4", s_agent_4);
		params.put("s_agent_5", s_agent_5);
		insert("insert.mst_production", params);
	}	

	public void insertMst_ptc_jm(String ls_no_jm, Integer li_run,
			String ls_ket, Double ar_ldec_bayar, String ls_accno1,
			String ls_accno2, String kode, String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("ls_no_jm", ls_no_jm);
		params.put("li_run", li_run);
		params.put("ls_ket", ls_ket);
		params.put("ar_ldec_bayar", ar_ldec_bayar);
		params.put("ls_accno1", ls_accno1);
		params.put("ls_accno2", ls_accno2);
		params.put("kode", kode);
		params.put("spaj", spaj);
		insert("insert.mst_ptc_jm", params);
	}

	public void insertMst_ptc_tm(String ls_no_jm, Integer li_pos,
			Date ldt_prod, String no_pre, String lus_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("ls_no_jm", ls_no_jm);
		params.put("li_pos", li_pos);
		params.put("ldt_prod", ldt_prod);
		params.put("no_pre", no_pre);
		params.put("user_input", lus_id);
		insert("insert.mst_ptc_tm", params);
	}

	public void insertMst_reward(String spaj, int tahun, int premi,
			String jenis_rekrut, String msrk_id, String msrk_name,
			String msrk_no_account, String lbn_id, Double reward, Double tax,
			Double kurs, String lus_id, int line_bus) throws DataAccessException {
		
		msrk_no_account = StringUtils.replace(msrk_no_account, "-","");
		msrk_no_account = StringUtils.replace(msrk_no_account, ".","");
		msrk_no_account = StringUtils.replace(msrk_no_account, "/","");
		
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		params.put("jenis_rekrut", jenis_rekrut);
		params.put("msrk_id", msrk_id);
		params.put("msrk_name", msrk_name);
		params.put("msrk_no_account", msrk_no_account);
		params.put("lbn_id", lbn_id);
		params.put("ldec_reward", reward);
		params.put("ldec_tax", tax);
		params.put("ldec_kurs", kurs);
		params.put("lus_id", lus_id);
		params.put("linebus", line_bus);
		insert("insert.mst_reward", params);
	}
	
	public void insertMst_rewardKetinggalan(String spaj, int tahun, int premi,
			String jenis_rekrut, String msrk_id, String msrk_name,
			String msrk_no_account, String lbn_id, Double reward, Double tax,
			Double kurs, String lus_id, int line_bus, Date msco_pay_date, Date msco_trf_date, String msco_no) throws DataAccessException {
		
		msrk_no_account = StringUtils.replace(msrk_no_account, "-","");
		msrk_no_account = StringUtils.replace(msrk_no_account, ".","");
		msrk_no_account = StringUtils.replace(msrk_no_account, "/","");
		
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		params.put("jenis_rekrut", jenis_rekrut);
		params.put("msrk_id", msrk_id);
		params.put("msrk_name", msrk_name);
		params.put("msrk_no_account", msrk_no_account);
		params.put("lbn_id", lbn_id);
		params.put("ldec_reward", reward);
		params.put("ldec_tax", tax);
		params.put("ldec_kurs", kurs);
		params.put("lus_id", lus_id);
		params.put("linebus", line_bus);
		params.put("msco_pay_date",msco_pay_date);
		params.put("msco_trf_date", msco_trf_date);
		params.put("msco_no", msco_no);
		insert("insert.mst_rewardKetinggalan", params);
	}

	public void insertMst_comm_bonus(String spaj, int tahun, int premi,
			int tipe, String msag_id, String nama,
			String no_account, String lbn_id, String msco_id,  
			Double bonus, Double tax,
			Double kurs, String lus_id, Date msco_date, int lsbs_linebus) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		params.put("tipe", tipe);
		params.put("msag_id", msag_id);
		params.put("nama", nama);
		params.put("no_account", no_account);
		params.put("lbn_id", lbn_id);
		params.put("msco_id", msco_id);
		params.put("bonus", bonus);
		params.put("tax", tax);
		params.put("kurs", kurs);
		params.put("lus_id", lus_id);
		params.put("msco_date", msco_date);
		params.put("linebus", lsbs_linebus);
		insert("insert.mst_comm_bonus", params);
	}

	public void insertMst_tag_payment(TopUp topup, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("topup", topup);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		params.put("tahun2", new Integer(1));
		params.put("premi2", new Integer(1));
		insert("insert.mst_tag_payment", params);
	}

	public void insertMst_tbank(String ls_no_pre, Integer li_pos,
			Date ldt_prod, Date ar_tglrk, Premi premi, String ls_kas,
			Double ar_jumlah, String lus_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("ls_no_pre", ls_no_pre.trim());
		params.put("li_pos", li_pos);
		params.put("ldt_prod", ldt_prod);
		params.put("ar_tglrk", ar_tglrk);
		params.put("premi", premi);
		params.put("ls_kas", ls_kas);
		params.put("ar_jumlah", ar_jumlah);
		params.put("lus_id", lus_id);
		insert("insert.mst_tbank", params);
	}

	public void insertMst_ulink_bill(String spaj, Integer tahun,
			Integer pot_ke, Date beginDate, Integer flag_pot, Date prodDate,
			Integer flag_jurnal, Integer flag_bill, Date nextDate,
			String lus_id, Integer muke, Double total_premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", tahun);
		params.put("pot_ke", pot_ke);
		params.put("beginDate", beginDate);
		params.put("flag_pot", flag_pot);
		params.put("prodDate", prodDate);
		params.put("flag_jurnal", flag_jurnal);
		params.put("flag_bill", flag_bill);
		params.put("nextDate", nextDate);
		params.put("lus_id", lus_id);
		params.put("muke", muke);
		params.put("total_premi", total_premi);
		insert("insert.mst_ulink_bill", params);
	}

	public void insertMst_ulink_det_billing(String spaj, Integer tahun,
			Integer pot_ke, String lsbs_id, String lsdbs_number, Double premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", tahun);
		params.put("pot_ke", pot_ke);
		params.put("lsbs_id", lsbs_id);
		params.put("lsdbs_number", lsdbs_number);
		params.put("premi", premi);
		insert("insert.mst_ulink_det_billing", params);
	}

	public void insertMst_ulink_det_billing(Map params) throws DataAccessException {
		insert("insert.mst_ulink_det_billing", params);
	}

	public List selectAdminRegion() throws DataAccessException {
		return query("selectAdminRegion", null);
	}

	public Integer selectAgeFromSPAJ(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectAgeFromSPAJ", spaj);
	}

	public Integer selectgetFlagTelemarketing(String spaj)throws DataAccessException{
		Integer result = (Integer) querySingle("selectgetFlagTelemarketing", spaj);
		return (result == null ? 0 : result);
	}
	
	public Date selectAgentActiveDate(String agentId) throws DataAccessException {
		return (Date) querySingle("selectAgentActiveDate", agentId);
	}

	public List selectAgentsFromSpaj(String spaj) throws DataAccessException {
		return query("selectAgentsFromSpaj", spaj);
	}

	public List selectAgentsFromSpajHybrid2009(String spaj) throws DataAccessException {
		return query("selectAgentsFromSpajHybrid2009", spaj);
	}
	
	public Map selectBancassCommisionAndBonus(Date ldt_temp, Commission astr_kom) throws DataAccessException {
		Map params = new HashMap();
		params.put("ldt_temp", ldt_temp);
		params.put("astr_kom", astr_kom);
		return (HashMap) querySingle("selectBancassCommisionAndBonus", params);
	}

	public List selectBankEkaLife(String dig4) throws DataAccessException {
		return query("selectBankEkaLife", dig4);
	}
	
	public List selectBankEkaLife2(String dig5) throws DataAccessException {
		return query("selectBankEkaLife2", dig5);
	}

	public List selectBankPusat() throws DataAccessException {
		return query("selectBankPusat", "ignoreMe");
	}

	public Double selectBiayaMaterai(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return (Double) querySingle("selectBiayaMaterai", params);
	}

	public Map selectBiayaMaterai2(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return (Map) querySingle("selectBiayaMaterai2", params);
	}

	public Double selectBiayaPolis(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return (Double) querySingle("selectBiayaPolis", params);
	}

	public Double selectBiayaUlink(String spaj, int muke, int ljb) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("muke", new Integer(muke));
		params.put("ljb", new Integer(ljb));
		return (Double) querySingle("selectBiayaUlink", params);
	}

	public Integer selectLjbIdFromBiayaUlink(String spaj, String businessID,
			String lsdbs_no) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lsbs_id", businessID);
		params.put("lsdbs_number", lsdbs_no);
		return (Integer) querySingle("selectLjbIdFromBiayaUlink", params);
	}
	
	public String selectBasicName(Integer kd_rd, Integer nm_rd) throws DataAccessException {
		Map params = new HashMap();
		params.put("lsbs_id", kd_rd);
		params.put("lsdbs_number", nm_rd);
		return (String) querySingle("selectBasicName", params);
	}
	
	public String selectJenisRider(Integer kd_rd, Integer nm_rd, String namaRider) throws DataAccessException {
		Map params = new HashMap();
		params.put("lsbs_id", kd_rd);
		params.put("lsdbs_number", nm_rd);
		params.put("riderName", namaRider);
		return (String) querySingle("selectJenisRider", params);
	}
	
	public Double selectBiayaUlinkRider(String spaj, String businessID,
			String lsdbs_no) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lsbs_id", businessID);
		params.put("lsdbs_number", lsdbs_no);
		return (Double) querySingle("selectBiayaUlinkRider", params);
	}

	public List selectBillingInfoForTransfer(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return query("selectBillingInfoForTransfer", params);
	}

	public List selectBillingInformation(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return query("selectBillingInformation", params);
	}

	public List<Billing> selectBillingInformationSucc(String spaj, int lspd_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lspd_id", new Integer(lspd_id));
		return query("selectBillingInformationSucc", params);
	}

	public Map selectBillingRemain(String spaj) throws DataAccessException {
		return (HashMap) querySingle("selectBillingRemain", spaj);
	}

	public String selectBusinessGroup(Long bisnisId) throws DataAccessException {
		return (String) querySingle("selectBusinessGroup", bisnisId);
	}
	
	public String selectBusinessId(String spaj) throws DataAccessException {
		String lsbs = (String) querySingle("selectBusinessId", spaj);
		return lsbs;
	}
	
	public Date selectBegDateInsured(String spaj) {
		return (Date) querySingle("selectBegDateInsured", spaj);
	}

	public Date selectBegDateProductInsured(String spaj) {
		return (Date) querySingle("selectBegDateProductInsured", spaj);
	}
	
	public Date selectBDateSLink(String spaj){
		return (Date) querySingle("selectBDateSLink", spaj);
	}

	public Integer selectBusinessNumber(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectBusinessNumber", spaj);
	}

	public Map selectCabangDanKursFromSpaj(String spaj) throws DataAccessException {
		Map hasil = (HashMap) querySingle("selectCabangFromSpaj", spaj);
		return hasil;
	}
	
	public String selectCabangFromSpaj_lar(String spaj) throws DataAccessException {
		Map hasil = (HashMap) querySingle("selectCabangFromSpaj_lar", spaj);
		return (String) hasil.get("region");
	}

	public String selectCabangFromSpaj(String spaj) throws DataAccessException {
		Map hasil = (HashMap) querySingle("selectCabangFromSpaj", spaj);
		if(hasil != null) return (String) hasil.get("LCA_ID");
		else return null;
	}
	public String selectWakilFromSpaj(String spaj) throws DataAccessException {
		Map hasil = (HashMap) querySingle("selectCabangFromSpaj", spaj);
		if(hasil != null) return (String) hasil.get("LWK_ID");
		else return null;
	}
	
	public Map selectrekNasabahBancass1(String spaj) throws DataAccessException {
		Map hasil = (HashMap) querySingle("selectrekNasabahBancass1", spaj);
		return hasil;
	}
	
	public List<Map> selectMstBillingKUD(String spaj) throws DataAccessException {
		return query("selectMstBillingKUD", spaj);
	}
	
	public Integer selectCountMstReffBii(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectCountMstReffBii", reg_spaj);
	}
	
	public String selectGetNoEndors(String spaj) throws DataAccessException {
		return (String) querySingle("selectGetNoEndors", spaj);
	}
	
	public String selectNmCabangFromSpaj(String spaj) throws DataAccessException {
		Map hasil = (HashMap) querySingle("selectNmCabangFromSpaj", spaj);
		if(hasil != null) return (String) hasil.get("LCA_NAMA");
		else return null;
	}

	public String selectEmailCabangTtp(String spaj) throws DataAccessException {
		return (String) querySingle("selectEmailCabangTtp", spaj);
	}
	public int selectCbFromSpaj(String spaj) throws DataAccessException {
		return Integer.parseInt(querySingle("selectCbFromSpaj", spaj)
				.toString());
	}

	public Map selectCommisionAndBonus(Date ldt_temp, Commission astr_kom) throws DataAccessException {
		Map params = new HashMap();
		params.put("ldt_temp", ldt_temp);
		params.put("astr_kom", astr_kom);
		return (HashMap) querySingle("selectCommisionAndBonus", params);
	}

	public Map selectCounterRekEkalife(Integer rek_id) throws DataAccessException {
		return (HashMap) querySingle("selectCounterRekEkalife", rek_id);
	}
	/**
	 * Fungsi :Untuk mencari polis,dimana ada dua model pencarian
	 * 			1. Mencari polis berdasarkan posisi dan type bisnis (lssaId=null)
	 * 			2. Mencari polis berdasarkan status aksep , posisi dan type bisnis (akseptasi khusus)
	 * @param posisi
	 * @param tipe
	 * @param lssaId
	 * @param jenisTerbit (
	 * @return List
	 */
	public List selectDaftarSPAJ(String posisi, int tipe,Integer lssaId,Integer jenisTerbit) throws DataAccessException {
		Map params = new HashMap();
		if(lssaId!=null){
			params.put("posisi", "in("+posisi+")");
		}else{
			params.put("posisi", posisi);
		}
		params.put("tipe", new Integer(tipe));
		params.put("lssaId", lssaId);
		params.put("jenisTerbit",jenisTerbit);
		return query("selectDaftarSPAJ", params);
	}
	
	public String selectSpajRecur(String spaj_r) throws DataAccessException {
		return(String) querySingle("selectSpajRecur", spaj_r);
	}
	
	public List selectUwRenewal(String spaj)throws DataAccessException{
		return query("selectUwRenewal",spaj);
	}
	
	public List selectProdRenewal(String spaj, String kode)throws DataAccessException{
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("kode", kode);
		return query("selectProdRenewal", params);
	}
	
	public List selectRenewalDU(String spaj, String tim)throws DataAccessException{
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tim", tim);
		return query("selectRenewalDU", params);
	}
	
	public String selectRedeemBlock(String rdBlock) throws DataAccessException {
		return(String) querySingle("selectRedeemBlock", rdBlock);
	}	
	
	public List selectDaftarSPAJPayment(String posisi, int tipe,Integer lssaId,Integer jenisTerbit) throws DataAccessException {
		Map params = new HashMap();
		if(lssaId!=null){
			params.put("posisi", "in("+posisi+")");
		}else{
			params.put("posisi", posisi);
		}
		params.put("tipe", new Integer(tipe));
		params.put("lssaId", lssaId);
		params.put("jenisTerbit",jenisTerbit);
		return query("selectDaftarSPAJPayment", params);
	}
	public List selectDaftarSPAJMall(String posisi, int tipe,Integer lssaId,Integer jenisTerbit) throws DataAccessException {
		Map params = new HashMap();
		if(lssaId!=null){
			params.put("posisi", "in("+posisi+")");
		}else{
			params.put("posisi", posisi);
		}
		params.put("tipe", new Integer(tipe));
		params.put("lssaId", lssaId);
		params.put("jenisTerbit",jenisTerbit);
		return query("selectDaftarSPAJMall", params);
	}
	
	public List selectDaftarSPAJOnline(String posisi, int tipe,Integer lssaId,String lus_id) throws DataAccessException {
		Map params = new HashMap();
		if(lssaId!=null){
			params.put("posisi", "in("+posisi+")");
		}else{
			params.put("posisi", posisi);
		}
		params.put("tipe", new Integer(tipe));
		params.put("lssaId", lssaId);
		params.put("lusId",lus_id);
		return query("selectDaftarSPAJOnline", params);
	}
	
	public List selectDaftarSPAJSimple(String posisi, int tipe,Integer lssaId,String lus_id,String jenis) throws DataAccessException {
		Map params = new HashMap();
		if(lssaId!=null){
			params.put("posisi", "in("+posisi+")");
		}else{
			params.put("posisi", posisi);
		}
		params.put("tipe", new Integer(tipe));
		params.put("lssaId", lssaId);
		params.put("lusId",lus_id);
		params.put("jenis",jenis);
		return query("selectDaftarSPAJSimple", params);
	}
	
	public List selectDaftarSPAJCrossSelling(String posisi, int tipe,Integer lssaId,Integer jenisTerbit, String lca_id) throws DataAccessException {
		Map params = new HashMap();
		if(lssaId!=null){
			params.put("posisi", "in("+posisi+")");
		}else{
			params.put("posisi", posisi);
		}
		params.put("tipe", new Integer(tipe));
		params.put("lssaId", lssaId);
		params.put("jenisTerbit",jenisTerbit);
		params.put("lca_id", lca_id);
		return query("selectDaftarSPAJCrossSelling", params);
	}


	public List selectDaftarSPAJ1(String posisi, int tipe,Integer lssaId,Integer jenisTerbit) throws DataAccessException {
		Map params = new HashMap();
		if(lssaId!=null){
			params.put("posisi", "in("+posisi+")");
		}else{
			params.put("posisi", posisi);
		}
		params.put("tipe", new Integer(tipe));
		params.put("lssaId", lssaId);
		params.put("jenisTerbit",jenisTerbit);
		return query("selectDaftarSPAJ2", params);
	}
	/**
	 * Fungsi :Untuk mencari polis,dimana ada dua model pencarian
	 * 			1. Mencari polis berdasarkan posisi dan type bisnis (lssaId=null)
	 * 			2. Mencari polis berdasarkan status aksep , posisi dan type bisnis (akseptasi khusus)
	 * @param posisi
	 * @param tipe
	 * @param lssaId
	 * @return List
	 */
	public List selectDaftarSPAJ_valid(String posisi, int tipe,Integer lssaId,Integer lus_id, String cab_bank) throws DataAccessException {
		Map params = new HashMap();
		if(lssaId!=null){
			params.put("posisi", "in("+posisi+")");
		}else{
			params.put("posisi", posisi);
		}
		params.put("tipe", new Integer(tipe));
		params.put("lssaId", lssaId);
		params.put("lus_id", lus_id);
		params.put("cab_bank", cab_bank);
		return query("selectDaftarSPAJ_valid", params);
	}
	
	/**
	 * Daftar spaj untuk uw simas prima
	 * @author Yusuf
	 * @since Feb 14, 2008 (4:12:25 PM)
	 * @param lspd_id
	 * @param jn_bank
	 * @return
	 * @throws DataAccessException
	 */
	public List selectDaftarSpajUwSimasPrima(int lspd_id, int jn_bank, int flag_approve, int lus_id, String cab_bank) throws DataAccessException{
		Map params = new HashMap();
		params.put("lspd_id", lspd_id);
		params.put("jn_bank", jn_bank);
		params.put("flag_approve", flag_approve);
		params.put("lus_id", lus_id);
		params.put("cab_bank", cab_bank);
		return query("selectDaftarSpajUwSimasPrima", params);
	}
	
	public List selectAgentUploadNewBusinessList(String jenis, String lus_id) throws DataAccessException{
		Map params = new HashMap();
		params.put("jenis", jenis);
		params.put("lus_id", lus_id);
		return query("selectAgentUploadNewBusinessList", params);
	}
	
	public List selectDaftarSpajInputanBank(Integer lus_id, Integer lstb_id, Integer lspd_id) throws DataAccessException{
		Map params = new HashMap();
		params.put("lus_id", lus_id);
		params.put("lstb_id", lstb_id);
		params.put("lspd_id", lspd_id);
		return query("selectDaftarSpajInputanBank", params);
	}
	
	public List selectDaftarSpajInputanASM(Integer lus_id, Integer lstb_id, Integer lspd_id) throws DataAccessException{
		Map params = new HashMap();
		params.put("lus_id", lus_id);
		params.put("lstb_id", lstb_id);
		params.put("lspd_id", lspd_id);
		return query("selectDaftarSpajInputanASM", params);
	}
	
	public List selectDaftarSpajInputanMall(Integer lus_id, Integer lstb_id, Integer lspd_id) throws DataAccessException{
		Map params = new HashMap();
		params.put("lus_id", lus_id);
		params.put("lstb_id", lstb_id);
		params.put("lspd_id", lspd_id);
		return query("selectDaftarSpajInputanMall", params);
	}
	
	/**@Fungsi: Untuk menampikan Data Usulan Asuransi secara detail, seprti premi,up,produk yang dipilih,dll
	 * @param	String spaj
	 * @return 	Map
	 * @author 	Ferry Harlim
	 * */
	public Map selectDataUsulan(String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		return (HashMap) querySingle("selectDataUsulan", params);
	}
	
	public Integer selectFlagSpecial(String spaj) throws DataAccessException{
		return (Integer) querySingle("selectFlagSpecial", spaj);
	}
	
	public List selectDataUsulanRider(String spaj){
		return query("selectDataUsulanRider",spaj);
	}
	
	public Map selectInfoAgenRider(String msag_id) throws DataAccessException{
		return (Map) querySingle("selectInfoAgenRider", msag_id);
	}
	
	public boolean selectCekDegradasiAgen(String msag_id, Date beg_date) throws DataAccessException{
		Map params = new HashMap();
		params.put("msag_id", msag_id);
		params.put("beg_date", beg_date);
		int result = (Integer) querySingle("selectCekDegradasiAgen", params);
		return (result <= 0);
	}
	
	public List selectDetBillingRider(String spaj, int premi_ke, int tahun_ke){
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("premi_ke", premi_ke);
		params.put("tahun_ke", tahun_ke);
		return query("selectDetBillingRider", params);
	}
	
	public void insertMstCommRider(CommRider cr) throws DataAccessException{
		insert("insertMstCommRider", cr);
	}
	
	//gak join paymode coz klo data lama gak ada paymodenya maka hasilnya gak ada
	public Map selectDataUsulan2(String spaj) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		return (HashMap) querySingle("select.Data_Usulan", param);
	}
	
	public Map selectDataUsulan3(String spaj) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		return (HashMap) querySingle("select.Data_Usulan3", param);
	}
	
	public Integer selectNoSuratAtDb() {
		return (Integer) querySingle("selectNoSuratAtDb", null);
	}
	
	public List<HashMap> selectNoSuratPenawaran() throws DataAccessException {
		return query("selectNoSuratPenawaran", null);
	}
	
	public void updateNoSuratPenawaran(List<Integer> counter) throws DataAccessException {
		Map param=new HashMap();
		for(int a=0;a<3;a++) {
			param.put("msco_number", 43+a);
			param.put("msco_value", counter.get(a));
			update("updateNoSuratPenawaran", param);
		}
	}
		
	public void addCounterNoSurat(String no) {
		update("addCounterNoSurat", no);
	}
	
	public List selectDetailManfaat(String spaj, Integer urut) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("urut", urut);
		return query("selectDetailManfaat", params);
	}

	public List selectDetailManfaatTambahan(String spaj) throws DataAccessException {
		return query("selectDetailManfaatTambahan", spaj);
	}

	public List selectDetailPayment(String spaj, int tahun, int premi,
			String jenis) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		params.put("jenis", jenis);
		return query("selectDetailPayment", params);
	}

	public String selectGetCounter(int aplikasi, String cabang) throws DataAccessException {
		Map params = new HashMap();
		params.put("aplikasi", new Integer(aplikasi));
		params.put("cabang", cabang);
		return (String) querySingle("selectGetCounter", params);
	}
	
	public String selectGetPacGl(String jenis) throws DataAccessException {
		String no = null;
		if(jenis.equals("nopre")){
			no = (String) querySingle("selectCounterNoPre", null);
		}else if(jenis.equals("nojm")){
			no =(String) querySingle("selectCounterNoJm", null);
		}
		return no;
	}

	public String selectGetCounter2(int aplikasi, String cabang) throws DataAccessException {
		Map params = new HashMap();
		params.put("aplikasi", new Integer(aplikasi));
		params.put("cabang", cabang);
		return (String) querySingle("selectGetCounter2", params);
	}

	public String selectGetCounterMonthYear(int aplikasi, String cabang) throws DataAccessException {
		Map params = new HashMap();
		params.put("aplikasi", new Integer(aplikasi));
		params.put("cabang", cabang);
		return (String) querySingle("selectGetCounterMonthYear", params);
	}
	
	public String selectGetCounterValueEb(int aplikasi, String cabang) throws DataAccessException {
		Map params = new HashMap();
		params.put("aplikasi", new Integer(aplikasi));
		params.put("cabang", cabang);
		return (String) querySingle("selectGetCounterValueEb", params);
	}

	public Map selectInfoBonusProduk(Integer lsbs) throws DataAccessException {
		return (HashMap) querySingle("selectInfoBonusProduk", lsbs);
	}

	public Map selectInfoCetakManfaat(String spaj) throws DataAccessException {
		return (HashMap) querySingle("selectInfoManfaat", spaj);
	}

	public Map selectInfoPribadi(String spaj) throws DataAccessException {
		return (HashMap) querySingle("selectInfoPribadi", spaj);
	}

	public Map selectInfoProductInsured(String spaj, String lsbs) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lsbs", Integer.valueOf(lsbs));
		return (HashMap) querySingle("selectInfoProductInsured", params);
	}

	public Map selectInsuredInfo(String spaj) throws DataAccessException {
		return (HashMap) querySingle("selectInsuredInfo", spaj);
	}

	public List selectKurs() throws DataAccessException {
		return query("selectKurs", null);
	}

	public List<DropDown> selectDaftarProdukKomisi(String dist) throws DataAccessException{
		return query("selectDaftarProdukKomisi", dist);
	}
	
	public Date selectLastBancassCommisionDate(Commission komisi) throws DataAccessException {
		return (Date) querySingle("selectLastBancassCommisionDate", komisi);
	}

	public Date selectLastComissionDate(Commission komisi) throws DataAccessException {
		return (Date) querySingle("selectLastComissionDate", komisi);
	}

	public Map selectKomisiPowersave(int lscp_jenis, int lsbs_id, int lsdbs_number, int lscp_mgi, String lku_id, int lev_comm, int lscp_year, double premi, int lscp_rollover, Date tgl_kom) throws DataAccessException{
		Map params = new HashMap();
		params.put("lscp_jenis", lscp_jenis);
		params.put("lsbs_id", lsbs_id);
		params.put("lsdbs_number", lsdbs_number);
		params.put("lscp_mgi", lscp_mgi);
		params.put("lku_id", lku_id);
		params.put("lev_comm", lev_comm);
		params.put("lscp_year", lscp_year);
		params.put("premi", premi);
		params.put("lscp_rollover", lscp_rollover);
		params.put("tgl_kom", tgl_kom);
		return (Map) querySingle("selectKomisiPowersave", params); 
	}
	
	public double selectLastCurrency(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		Double result = (Double) querySingle("selectLastCurrency", params);
		if(result==null) return -1;
		else return result.doubleValue();
	}

	public String selectManfaatPerLine(int lsbs, int lsdbs, int urut, int line) throws DataAccessException {
		Map params = new HashMap();
		params.put("lsbs", new Integer(lsbs));
		params.put("lsdbs", new Integer(lsdbs));
		params.put("urut", new Integer(urut));
		params.put("line", new Integer(line));
		return (String) querySingle("selectManfaatPerLine", params);
	}

	public String selectManfaatRider(int lsbs, int lsdbs) throws DataAccessException {
		Map params = new HashMap();
		params.put("lsbs", new Integer(lsbs));
		params.put("lsdbs", new Integer(lsdbs));
		List hasil = query("selectManfaatPerLine", params);
		StringBuffer result = new StringBuffer();
		for(int i=0; i<hasil.size(); i++) {
			result.append((String) hasil.get(i));
		}
		return result.toString();
	}

	public Date selectMaxRkDate(String spaj, int tahun, int premi, String jenis) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		params.put("jenis", jenis);
		return (Date) querySingle("selectMaxRkDate", params);
	}

	public Double selectMonthlyKurs(String lku, String tahun, String bulan) throws DataAccessException {
		Map params = new HashMap();
		params.put("lku", lku);
		params.put("tahun", tahun);
		params.put("bulan", bulan);
		return (Double) querySingle("selectMonthlyKurs", params);
	}

	public Date selectMst_default(int id) throws DataAccessException {
		return (Date) querySingle("selectMst_default", new Integer(id));
	}

	public Double selectMst_default_numeric(int id) throws DataAccessException {
		return (Double) querySingle("selectMst_default.numeric", id);
	}

	public Double selectMtuSaldoUnitLink(String spaj, int ke, int jn) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("ke", new Integer(ke));
		params.put("jn", new Integer(jn));
		Double result = (Double) querySingle("selectMtuSaldoUnitLink", params);
		if (result == null)
			result = new Double(0);
		return result;
	}

	public Date selectMuTglTrans(String spaj) throws DataAccessException {
		return (Date) querySingle("selectMuTglTrans", spaj);
	}

	public String selectNamaBusiness(Integer lsbs, Integer lsdbs) throws DataAccessException {
		Map params = new HashMap();
		params.put("lsbs_id", lsbs);
		params.put("lsdbs_number", lsdbs);
		return (String) querySingle("selectNamaBusiness", params);
	}

	public Map selectNilaiBonus(int lsbs_id, int umur, String kurs,
			Integer cbId, Integer lamaBayar, Integer lamaTanggung)
			throws DataAccessException {

		if (umur < 20)
			umur = 20;
		int il_cbid = cbId;
		if (il_cbid == 1 || il_cbid == 2)
			il_cbid = 3;
		cbId = new Integer(il_cbid);

		Map params = new HashMap();
		params.put("lsbs", new Integer(lsbs_id));
		params.put("umur", new Integer(umur));
		params.put("kurs", kurs);
		params.put("cbid", cbId);
		params.put("lamabayar", lamaBayar);
		params.put("lamatanggung", lamaTanggung);
		return queryMap(
				"selectNilaiBonus", params, "TAHUN_KE", "NILAI");
	}

	public Double selectNilaiUnderTable(String spaj) throws DataAccessException {
		Double result = (Double) querySingle("selectNilaiUnderTable", spaj); 
		return result!=null?result:new Double(0);
	}

	public Double selectNilaiMaturity(int jenis, int lsbs_id, int umur,
			String kurs, Integer cbId, Integer lamaBayar, Integer lamaTanggung,
			int tahun) throws DataAccessException {
		Map params = new HashMap();
		params.put("jenis", new Integer(jenis));
		params.put("lsbs", new Integer(lsbs_id));
		params.put("umur", new Integer(umur));
		params.put("kurs", kurs);
		params.put("cbid", cbId);
		params.put("lamabayar", lamaBayar);
		params.put("lamatanggung", lamaTanggung);
		params.put("tahun", new Integer(tahun));
		return (Double) querySingle("selectNilaiMaturity", params);
	}

	public Map selectNilaiTahapan(int lsbs_id, int umur, String kurs,
			Integer cbId, Integer lamaBayar, Integer lamaTanggung)
			throws DataAccessException {
		Map params = new HashMap();
		params.put("lsbs", new Integer(lsbs_id));
		params.put("umur", new Integer(umur));
		params.put("kurs", kurs);
		params.put("cbid", cbId);
		params.put("lamabayar", lamaBayar);
		params.put("lamatanggung", lamaTanggung);
		return queryMap(
				"selectNilaiTahapan", params, "TAHUN_KE", "NILAI");
	}

	public String selectNoPolisFromSpaj(String spaj) throws DataAccessException {
		return (String) querySingle("selectNoPolisFromSpaj", spaj);
	}
	// patar timotius 20181707 QR
	public String selectPolisBySpaj(String spaj) throws DataAccessException {
		return (String) querySingle("selectPolisBySpaj", spaj);
	}
	public void updateKartuPasBySpaj(Map<String, Object> params) throws DataAccessException {
		update("updateKartuPasBySpaj", params);
	}

	public List<Payment> selectPaymentCount(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return query("selectPaymentCount", params);
	}

	public Integer selectPayMode(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectPayMode", spaj);
	}

	public Integer selectPayPeriod(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectPayPeriod", spaj);
	}

	public Integer selectBiayaTU(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectBiayaTU", spaj);
	}
	
	public List selectPayType() throws DataAccessException {
		return query("selectPayType", null);
	}
	public List selectPayType0(String tipe) throws DataAccessException {
		return query("selectPayType0", tipe);
	}	
	/**Fungsi:	Untuk menampilkan data pemegang polis secara detail
	 * @param 	String spaj
	 * @return	Map
	 * @author	Ferry Harlim
	 */
	public Map selectPemegang(String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		return (HashMap) querySingle("selectPemegang", params);
	}
	
	public Map selectPemegangSimpleBac(String reg_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("reg_id", reg_id);
		return (HashMap) querySingle("selectPemegangSimpleBac", params);
	}

	public Map selectPeriodManfaat(String spaj, int lsbs, int lsdbs) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lsdbs", new Integer(lsdbs));
		params.put("lsbs", new Integer(lsbs));
		return (HashMap) querySingle("selectPeriodManfaat", params);
	}

	public Date selectPolicyPrintDate(String spaj) throws DataAccessException {
		return (Date) querySingle("selectPolicyPrintDate", spaj);
	}

	public Integer selectPolicyRelation(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectPolicyRelation", spaj);
	}

	public List selectPositionSpaj() throws DataAccessException {
		return query("selectPositionSpaj", null);
	}

	public Double selectPremiAndRider(String spaj, String flag) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("flag", flag);
		Double hasil = (Double) querySingle("selectPremiAndRider", params);
		if (hasil == null)
			return new Double(-1);
		else
			return hasil;
	}

	public Map selectPremiProdukUtama(String spaj) throws DataAccessException {
		return (HashMap) querySingle("selectPremiProdukUtama", spaj);
	}

	public Double selectPremiTahunan(String bisnis, Integer nomor, Integer umur) throws DataAccessException {
		Map params = new HashMap();
		params.put("bisnis", bisnis);
		params.put("nomor", nomor);
		params.put("umur", umur);
		return (Double) querySingle("selectPremiTahunan", params);
	}

	public Premi selectPremiTertanggung(String spaj) throws DataAccessException {
		return (Premi) querySingle("selectPremiTertanggung", spaj);
	}

	public Double selectPremiTopUpUnitLink(String spaj, Integer premi_ke) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("premi_ke", premi_ke);
		return (Double) querySingle("selectPremiTopUpUnitLink", params);
	}

	public Map selectPrestigeClub(String agentId, Date tgl_kom) throws DataAccessException {
		Map params = new HashMap();
		params.put("msag_id", agentId);
		params.put("tanggal", tgl_kom);
		return (HashMap) querySingle("selectPrestigeClub", params);
	}

	public int selectPrivasiKe(String spaj) throws DataAccessException {
		Integer hasil = (Integer) querySingle("selectPrivasiKe", spaj);
		if (hasil == null)
			return 0;
		else
			return hasil;
	}

	public List selectProductInsured(String spaj, int ins_no) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("ins_no", new Integer(ins_no));
		return query("selectProductInsured", params);
	}

	public Date selectProductionDate(String spaj, Integer tahun, Integer premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", tahun);
		params.put("premi", premi);
		return (Date) querySingle("selectProductionDate", params);
	}

	public Map selectProductionDateAndKurs(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return (HashMap) querySingle("selectProductionDateAndKurs", params);
	}

	public Integer selectProduksiPertamaAgent(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectProduksiPertamaAgent", spaj);
	}

	public Integer selectMgiNewBusiness(String reg_spaj) throws DataAccessException {
		String lsbs_id = FormatString.rpad("0", selectBusinessId(reg_spaj), 3);
		int lsdbs_number = selectBusinessNumber(reg_spaj);
		Map p = new HashMap();
		p.put("reg_spaj", reg_spaj);
		p.put("tahun_ke", 1);
		p.put("premi_ke", 1);
		if(products.powerSave(lsbs_id)){
			if(Integer.parseInt(lsbs_id)==188){
				return (Integer) querySingle("selectProSaveBaru", reg_spaj);
			}else{
				if(products.stableSave(Integer.parseInt(lsbs_id), lsdbs_number)) {
					return (Integer) querySingle("selectMGIStableSave", p);
				}else{
					if(lsbs_id.equals("177")){
						return (Integer) querySingle("selectMGIStableSave", p);
					}else{
						return (Integer) querySingle("selectProSave", reg_spaj);
					}
				}
			}
		}else if(products.stableLink(lsbs_id)){
			return (Integer) querySingle("selectMGIStableLink", p);
		}else if(products.stableSavePremiBulanan(lsbs_id) || products.stableSave(Integer.parseInt(lsbs_id), lsdbs_number)){
			return (Integer) querySingle("selectMGIStableSave", p);
		}else{
			return null;
		}
		
	}
	
	public Map selectReferrerBII(String spaj) throws DataAccessException {
		return (HashMap) querySingle("selectReferrerBII", spaj);
	}

	public Map selectReferrerBIINew(String spaj) throws DataAccessException {
		return (HashMap) querySingle("selectReferrerBII.new", spaj);
	}

	public Rekruter selectRekruterFromAgen(String msag_id) throws DataAccessException {
		return (Rekruter) querySingle("selectRekruterFromAgen", msag_id);
	}
	
	public Rekruter selectRekruterFromAgenSys(String msag_id) throws DataAccessException {
		return (Rekruter) querySingle("selectRekruterFromAgenSys", msag_id);
	}
	
	public String selectRekruterAgencySystem(String msag_id) throws DataAccessException {
		return (String) querySingle("selectRekruterAgencySystem", msag_id);
	}
	
	public String selectMclIdfromAgent(String msag_id) throws DataAccessException {
		return (String) querySingle("selectMclIdfromAgent", msag_id);
	}
	
	public Map<String, Object> selectRekruter(String agentId) throws DataAccessException {
		return (HashMap) querySingle("selectRekruter", agentId);
	}

	public Map<String, Object> selectRekruterDenganKomisi(String reg_spaj, String agentId) throws DataAccessException {
		Map p = new HashMap();
		p.put("reg_spaj", reg_spaj);
		p.put("msag_id", agentId);
		return (HashMap) querySingle("selectRekruterDenganKomisi", p);
	}

	public double selectSaldoDeposit(String spaj) throws DataAccessException {
		Double hasil = (Double) querySingle("selectSaldoDeposit", spaj);
		if (hasil == null)
			return 0;
		else
			return hasil.doubleValue();
	}

	public List selectSPAJ(int posisi, int tipe, String kategori, String kata) throws DataAccessException {
		Map params = new HashMap();
		params.put("posisi", new Integer(posisi));
		params.put("tipe", new Integer(tipe));
		params.put("kategori", kategori);
		params.put("kata", kata);
		return query("selectSPAJ", params);
	}

	public Integer selectSpajCancel(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectSpajCancel", spaj);
	}

	/*
	public Date selectSysdate() throws DataAccessException {
		return (Date) querySingle("selectSysdate", null);
	}
	
	public Date selectSysdate(String add, boolean trunc, int nilai) throws DataAccessException{
		Map param = new HashMap();
		param.put("add", add);
		param.put("trunc", trunc);
		param.put("nilai", nilai);
		return (Date) querySingle("selectSysdateMap", param);
	}
	*/
	
	public Map selectCounterTmms(Map params) throws DataAccessException{
		return (HashMap) querySingle("selectCounterTmms", params);
	}
	
	public Map selectTMProduct(String product_code) throws DataAccessException{
		return (HashMap) querySingle("selectTMProduct", product_code);
	}
	
	public void updateCounterTmms(Map params) throws DataAccessException{
		update("updateCounterTmms", params);
	}
	
	public void insertTmms(Tmms tmms) throws DataAccessException{
		insert("insertTmms", tmms);
	}
	
	public void insertTmmsDet(TmmsDet tmmsDet) throws DataAccessException{
		insert("insertTmmsDet", tmmsDet);
	}
	
	public void updateTmms(Tmms tmms) throws DataAccessException{
		insert("updateTmms", tmms);
	}
	
	public void updateTmmsDet(TmmsDet tmmsDet) throws DataAccessException{
		insert("updateTmmsDet", tmmsDet);
	}
	
	public void updateTmProduct(Map params) throws DataAccessException{
		update("updateTmProduct", params);
	}

	/*
	public Date selectSysdateTruncated(int daysAfter) throws DataAccessException {
		return (Date) querySingle("selectSysdateTruncated", new Integer(
				daysAfter));
	}
	*/

	public int selectTagPaymentCountStableLink(String spaj) throws DataAccessException{
		return (Integer) querySingle("selectTagPaymentCountStableLink", spaj);
	}
	
	public int selectTagPaymentCount(String spaj, int tahun, int premi,
			String jenis) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		params.put("jenis", jenis);
		Integer hasil = (Integer) querySingle("selectTagPaymentCount", params);
		if (hasil == null)
			return 0;
		else
			return hasil;
	}

	public Date selectTanggalJurnal(String voucher, String ls_tahun) throws DataAccessException {
		Map params = new HashMap();
		params.put("voucher", voucher);
		params.put("ls_tahun", ls_tahun);
		return (Date) querySingle("selectTanggalJurnal", params);
	}
	/**@Fungsi:	Untuk menampilkan data Tertanggung secara Detail
	 * @param	String spaj
	 * @return 	Map
	 * @author 	Ferry Harlim	
	 * */
	public Map selectTertanggung(String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		return (HashMap) querySingle("select.tt", params);
	}
	
	public Map selectTertanggungSimpleBac(String reg_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("reg_id", reg_id);
		return (HashMap) querySingle("select.tt.simplebac", params);
	}

	public Map selectTodayKurs(String lku_id, Date tanggal) throws DataAccessException {
		Map params = new HashMap();
		params.put("lku_id", lku_id);
		params.put("tanggal", tanggal);
		return (HashMap) querySingle("selectTodayKurs", params);
	}
	
	public List<Integer> selectMuKe(String spaj) throws DataAccessException{
		return query("selectMuKe", spaj);
	}
	
	public List selectTopUp(String spaj, int tahun, int premi, String urutan) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		params.put("urutan", urutan);
		return query("selectTopUp", params);
	}

	public TopUp selectTopUp(String spaj, String payment, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("payment", payment);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		TopUp hasil = (TopUp) querySingle("selectTopUp", params);
//		hasil.setTahun_ke(new Integer(tahun));
//		hasil.setPremi_ke(new Integer(premi));
		return hasil;
	}

//	public List selectViewKomisi(String spaj) throws DataAccessException {
//		return query("selectViewKomisi", spaj);
//	}

	public List selectViewKomisiAsli(String spaj) throws DataAccessException {
		return query("selectViewKomisiAsli", spaj);
	}
	
	public List selectViewKomisiHybrid2009(String spaj) throws DataAccessException {
		return query("selectViewKomisiHybrid2009", spaj);
	}

//	public List selectViewKomisiNewLg(String spaj) throws DataAccessException {
//		return query("selectViewKomisiNewLg", spaj);
//	}

	public List selectViewUlink(String spaj) throws DataAccessException {
		return query("report.selectViewUlink", spaj);
	}
	
	public List selectViewExcelink(String spaj) throws DataAccessException {
		return query("report.selectViewExcelink", spaj);
	}
	
	public List selectViewStableLink(String spaj) throws DataAccessException {
		return query("report.selectViewStableLink", spaj);
	}
	
	public Double selectPremiSuperSehat(int lsbs, int lsdbs, int umur, String lku) throws DataAccessException {
		Map params = new HashMap();
		params.put("lsbs", new Integer(lsbs));
		params.put("lsdbs", new Integer(lsdbs));
		params.put("umur", new Integer(umur));
		params.put("lku", lku);
		return (Double) querySingle("selectPremiSuperSehat", params);			
	}
	
	public List selectViewStableLinkTopUp(String spaj, Integer tu_ke) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tu_ke", tu_ke);
		return query("report.selectViewStableLinkTopUp", params);
	}
	
	public List selectViewPSaveTopUp(String spaj, Integer tu_ke) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tu_ke", tu_ke);
		return query("report.selectViewPSaveTopUp", params);
	}
	
	public Integer selectCheckSpajInMstSLink(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectCheckSpajInMstSLink", spaj);
	}
	
	public Integer selectCheckSpajInMstSLinkBasedFlagBulanan(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectCheckSpajInMstSLinkBasedFlagBulanan", spaj);
	}
	
	public Integer selectCheckSpajInMstPsave(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectCheckSpajInMstPsave", spaj);
	}
	
	public Integer selectCheckSpajInMstPsaveBasedFlagBulanan(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectCheckSpajInMstPsaveBasedFlagBulanan", spaj);
	}
	
	public Integer selectCheckSpajInLstUlangan(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectCheckSpajInLstUlangan", spaj);
	}
	
	public String selectLcaIdMstPolicyBasedSpaj( String spaj ) throws DataAccessException{
		return (String) querySingle( "selectLcaIdMstPolicyBasedSpaj", spaj );
	}
	
	public List selectForPrintEndorsemenLcaId9 ( String spaj, Integer tuKe ){
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tuKe", tuKe);
		return ( List ) query("selectForPrintEndorsemenLcaId9", params);
	}
	
	public List selectForPrintEndorsemenNotLcaId9 ( String spaj, Integer tuKe ){
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tuKe", tuKe);
		return ( List ) query("selectForPrintEndorsemenNotLcaId9", params);
	}
	
	public List selectMstDrekBasedNoTrx ( String noTrx ){
		Map params = new HashMap();
		params.put("noTrx", noTrx);
		return ( List ) query("selectMstDrekBasedNoTrx", params);
	}
	
	public List<DrekDet> selectMstDrekDet ( String noTrx, String regSpaj, String paymentId, String norek_ajs ){
		Map params = new HashMap();
		params.put("regSpaj", regSpaj);
		params.put("noTrx", noTrx);
		params.put("paymentId", paymentId);
		params.put("norek_ajs", norek_ajs);
		return ( List<DrekDet> ) query("selectMstDrekDet", params);
	}
	
	public List selectMstDrekDet2 ( String noTrx, String regSpaj, String paymentId, String norek_ajs ){
		Map params = new HashMap();
		params.put("regSpaj", regSpaj);
		params.put("noTrx", noTrx);
		params.put("paymentId", paymentId);
		params.put("norek_ajs", norek_ajs);
		return ( List<DrekDet> ) query("selectMstDrekDet2", params);
	}
	
	public Integer isUlinkBasedSpajNo ( String regSpaj ){
		return (Integer) querySingle("isUlinkBasedSpajNo", regSpaj);
	}
	
	public Integer isSlinkBasedSpajNo ( String regSpaj ){
		return (Integer) querySingle("isSlinkBasedSpajNo", regSpaj);
	}
	
	public List<Linkdetail> uLinkDescrAndDetail ( String spaj ){
		return ( List<Linkdetail> ) query("uLinkDescrAndDetail", spaj);
	}
	
	public List<Linkdetail> sLinkDescrAndDetail ( String spaj ){
		return ( List<Linkdetail> ) query("sLinkDescrAndDetail", spaj);
	}
	
	public String selectMaxNoUrutMstDrekDet ( String noTrx ){
		return ( String ) querySingle("selectMaxNoUrutMstDrekDet", noTrx);
	}
	
	public Integer selectCountMstDrekDet (String noTrx){
		return (Integer) querySingle("selectCountMstDrekDet", noTrx);
	}

	public int updateLst_rek_ekalife(Premi premi) throws DataAccessException {
		return update("update.lst_rek_ekalife", premi);
	}

	public int updateMst_address_billing(AddressBilling address) throws DataAccessException {
		return update("update.mst_address_billing", address);
	}

	public int updateMst_billing(String spaj, Integer flag_bayar,
			Integer flag_sisa, Double sisa, Integer tahun, Integer premi,
			Integer lspd_id, Date msbi_paid_date, Integer persen_paid) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("flag_bayar", flag_bayar);
		params.put("flag_sisa", flag_sisa);
		params.put("sisa", sisa);
		params.put("lspd_id", lspd_id);
		params.put("tahun", tahun);
		params.put("premi", premi);
		params.put("msbi_paid_date", msbi_paid_date);
		params.put("msbi_persen_paid", persen_paid);
		return update("update.mst_billing", params);
	}

	public int updateMst_billing_cancel(String spaj, Integer lspd_id) throws DataAccessException {
		HashMap params = new HashMap();
		params.put("spaj", spaj);
		params.put("lspd_id", lspd_id);
		return update("update.mst_billing.cancel", params);
	}

	public int updateMst_billingTopup(String spaj) throws DataAccessException {
		return update("update.mst_billing.topup", spaj);
	}

	public int updateMst_payment(TopUp topup, User currentUser) throws DataAccessException {
		topup.setLus_id(currentUser.getLus_id());
		return update("update.mst_payment", topup);
	}

	public int updateMst_paymentJurnal(Premi premi, String ls_pre, String ls_id, String no_jm, String no_jm_sa) throws DataAccessException {
		premi.setLs_pre(ls_pre.trim());
		premi.setMspa_payment_id(ls_id);
		premi.setNo_jm(no_jm);
		premi.setNo_jm_sa(no_jm_sa);
		return update("update.mst_payment.jurnal", premi);
	}
	
	public int updateMstPaymentJurnalFromSpaj(Premi premi, String ls_pre, String no_voucher, String reg_spaj, Long msdp_number, String no_jm, String no_jm_sa) throws DataAccessException {
		premi.setVoucher(no_voucher);
		premi.setLs_pre(ls_pre.trim());
		premi.setNo_spaj(reg_spaj);
		premi.setMsdp_number(msdp_number);
		premi.setNo_jm(no_jm);
		premi.setNo_jm_sa(no_jm_sa);
		return update("update.mst_payment.jurnal.spaj", premi);
	}

	public int updateMst_policy(String spaj, Double lspd, Integer proses, Date ttp) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lspd", lspd);
		params.put("proses", proses);
		params.put("ttp", ttp);
		return update("update.mst_policy", params);
	}

	public int updateMst_policyPrintDate(String spaj, String kolom) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("kolom", kolom);
		return update("update.mst_policy.printDate", params);
	}

	public int updateMst_policyEmptyPrintDate(String spaj) throws DataAccessException {
		return update("update.mst_policy.emptyPrintDate", spaj);
	}

	public int updateMst_reins_cancel(String spaj) throws DataAccessException {
		return update("update.mst_reins.cancel", spaj);
	}

	public int updateMst_ulink_cancel(String spaj, Integer lspd_id) throws DataAccessException{
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lspd_id", lspd_id);
		return update("update.mst_ulink.cancel", params);
	}
	
	public int updateMst_tag_payment(TopUp topup, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("topup", topup);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		return update("update.mst_tag_payment", params);
	}

	public int updateMst_trans_ulink(int flagPrint, String spaj, String tglTrans) throws DataAccessException {
		Map params = new HashMap();
		params.put("flagPrint", new Integer(flagPrint));
		params.put("spaj", spaj);
		params.put("tglTrans", tglTrans);
		return update("update.mst_trans_ulink.yusuf", params);
	}

	public int updateMst_ulink(int flagPrint, String spaj, String tglTrans) throws DataAccessException {
		Map params = new HashMap();
		params.put("flagPrint", new Integer(flagPrint));
		params.put("spaj", spaj);
		params.put("tglTrans", tglTrans);
		return update("update.mst_ulink.yusuf", params);
	}

	public int updateStatusPrintUlink(int flag, String spaj, BigDecimal muke) throws DataAccessException {
		Map params = new HashMap();
		params.put("flag", new Integer(flag));
		params.put("spaj", spaj);
		params.put("muke", muke);
		return update("update.statusPrintUlink", params);
	}

	public int updateStatusPrintTransUlink(int flag, String spaj, BigDecimal muke) throws DataAccessException {
		Map params = new HashMap();
		params.put("flag", new Integer(flag));
		params.put("spaj", spaj);
		params.put("muke", muke);
		return update("update.statusPrintTransUlink", params);
	}
	
	public int updateMstTransUlinkTransferTTP(String spaj, int ke) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("ke", new Integer(ke));
		return update("update.mst_trans_ulink.transferTTP", params);
	}

	public int updateMstUlinkTransferTTP(String spaj, int ke) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("ke", new Integer(ke));
		return update("update.mst_ulink.transferTTP", params);
	}

	public int updatePosisiMst_insured(String spaj, int posisi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("posisi", new Integer(posisi));
		return update("update.mst_insured.posisi", params);
	}
	public int updatePosisiMst_policy1(String spaj, int posisi,Date mspo_date_ttp ) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("posisi", new Integer(posisi));
		params.put("mspo_date_ttp", mspo_date_ttp);
		return update("update.mst_policy.posisi1", params);
	}

	public int updatePosisiMst_policy(String spaj, Integer lspd) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lspd", lspd);
		return update("update.mst_policy.posisi", params);
	}

	public int updateProduksi_pertama(Date prodDate, String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("prodDate", prodDate);
		params.put("spaj", spaj);
		return update("update.produksi_pertama", params);
	}

	public Integer validationAlreadyPaid(String spaj, int tahun, int premi) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", new Integer(tahun));
		params.put("premi", new Integer(premi));
		Integer result = (Integer) querySingle("validationAlreadyPaid", params);
		if (result == null)
			result = new Integer(-1);
		return result;
	}

	public boolean validationIsTitipanPremi(String payment_id) throws DataAccessException {
		Integer result = (Integer) querySingle("validationIsTitipanPremi", payment_id);
		if (result != null) {
			return true;
		}else {
			return false;
		}
	}

	public Integer validationDailyCurrency(String lku_id, Date rk_date) throws DataAccessException {
		Map params = new HashMap();
		params.put("lku_id", lku_id);
		params.put("rk_date", rk_date);
		return (Integer) querySingle("validationDailyCurrency", params);
	}

	public Integer validationNAB(String spaj) throws DataAccessException {
		return (Integer) querySingle("validationNAB", spaj);
	}

	public Integer validationPositionSPAJ(String spaj) throws DataAccessException {
		Integer result = (Integer) querySingle("validationPositionSPAJ", spaj);
		if (result == null)
			result = new Integer(-1);
		return result;
	}

	public Map validationPrintPolis(String spaj) throws DataAccessException {
		return (HashMap) querySingle("validationPrintPolis", spaj);
	}

	public Integer validationTopup(String spaj) throws DataAccessException {
		Integer result = (Integer) querySingle("validationTopup", spaj);
		if (result == null)
			result = new Integer(-1);
		return result;
	}

	public Integer validationStableLink(String spaj) throws DataAccessException {
		Integer result = (Integer) querySingle("validationStableLink", spaj);
		if (result == null)
			result = new Integer(-1);
		return result;
	}

	/**Fungsi:	Untuk mengecek apakah ada topup ke 3 dgn tgl transaksi 1 dan 3 sama
	 * @param 	String spaj
	 * @return 	Date
	 * @throws 	DataAccessException
	 * @auhtor	Ferry Harlim
	 */	
	public Date validationTopup3(String spaj) throws DataAccessException {
		return (Date) querySingle("validationTopup3", spaj);
	}
	/**
	 * Validasi tgl kirim pada print polis
	 * dian natalia
	 * @param spaj
	 * @return
	 * @throws DataAccessException
	 */
	public Date validationTglKirim(String spaj) throws DataAccessException {
		return (Date) querySingle("validationTglKirim", spaj);
	}

	public Map validationVerify(int lv_id) throws DataAccessException {
		return (HashMap) querySingle("validationVerify", new Integer(lv_id));
	}

	public List selectFilterSpaj(String tipe, String kata, String tglLahir) throws DataAccessException {
		Map params = new HashMap();
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("tglLahir", tglLahir);
		
		logger.info("a=======================================" + params);
		
		return query("selectFilterSpaj", params);
	}

	public List<Map> selectFilterSpajNew(String tipe, String kata, String tglLahir, Boolean flagLifeOnly) throws DataAccessException {
		Map params = new HashMap();
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("tglLahir", tglLahir);
		params.put("flagLifeOnly", flagLifeOnly);
		
		return query("selectFilterSpajNew", params);
	}

	public List selectFilterSpaj2(String posisi, String tipe, String kata, String pilter,String lssaId,String lsspId, String tgl_lahir, Map param) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		if(lssaId==null){
			params.put("posisi", posisi);
		}else{
			params.put("posisi", "in ("+posisi+")");
		}
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("lssaId",lssaId);
		params.put("lssp_id",lsspId);
		params.put("tgl_lahir", tgl_lahir);
		if (param!=null )
		{
			params.putAll(param);
		}
		
		return query("selectFilterSpaj2", params);
	}
	
	public String selectTotalFilterSpaj2(String posisi, String tipe, String kata, String pilter,String lssaId,String lsspId, String tgl_lahir) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		if(lssaId==null){
			params.put("posisi", posisi);
		}else{
			params.put("posisi", "in ("+posisi+")");
		}
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("lssaId",lssaId);
		params.put("lssp_id",lsspId);
		params.put("tgl_lahir", tgl_lahir);
		return (String) querySingle("selectTotalFilterSpaj2", params);
	}
	
	public List selectFilterSpaj2_valid(String posisi, String tipe, String kata, String pilter,String lssaId,Integer lus_id,String lsspId, String tgl_lahir, Map param) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		if(lssaId==null){
			params.put("posisi", posisi);
		}else{
			params.put("posisi", "in ("+posisi+")");
		}
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("lssaId",lssaId);
		params.put("lus_id",lus_id);
		params.put("lssp_id",lsspId);
		params.put("tgl_lahir", tgl_lahir);
		if (param!=null )
		{
			params.putAll(param);
		}
		
		return query("selectFilterSpaj2_valid", params);
	}
	
	public String selectTotalFilterSpaj2_valid(String posisi, String tipe, String kata, String pilter,String lssaId,Integer lus_id,String lsspId, String tgl_lahir) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		if(lssaId==null){
			params.put("posisi", posisi);
		}else{
			params.put("posisi", "in ("+posisi+")");
		}
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("lssaId",lssaId);
		params.put("lus_id",lus_id);
		params.put("lssp_id",lsspId);
		params.put("tgl_lahir", tgl_lahir);
		return (String) querySingle("selectTotalFilterSpaj2_valid", params);
	}
	
	public List selectFilterSpaj2SimasPrima(String posisi, String tipe, String kata, String pilter,Integer jn_bank, String tgl_lahir, Map param ) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		if(posisi.equals("-1")){
			params.put("posisi", posisi);
		}else{
			params.put("posisi", "in ("+posisi+")");
		}
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("jn_bank",jn_bank);
		params.put("tgl_lahir", tgl_lahir);
		
		if (param!=null )
		{
			params.putAll(param);
		}
		return query("selectFilterSpaj2SimasPrima", params);
	}
	
	public String selectTotalFilterSpaj2SimasPrima(String posisi, String tipe, String kata, String pilter,Integer jn_bank, String tgl_lahir) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		if(posisi.equals("-1")){
			params.put("posisi", posisi);
		}else{
			params.put("posisi", "in ("+posisi+")");
		}
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("jn_bank",jn_bank);
		params.put("tgl_lahir", tgl_lahir);
		return (String) querySingle("selectTotalFilterSpaj2SimasPrima", params);
	}
	
	
	public List selectFilterSpajSimple(String posisi, String tipe, String kata, String pilter,Integer jn_bank, String tgl_lahir, String jenis) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		params.put("posisi", posisi);
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("jn_bank",jn_bank);
		params.put("tgl_lahir", tgl_lahir);
		params.put("jenis", jenis);
		return query("selectFilterSpajSimple", params);
	}
	
	public List selectFilterOtorisasi(String tipe, String kata, String pilter, Integer jn_bank) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
//		params.put("pilter", tipe);
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("jn_bank",jn_bank);
		return query("selectFilterOtorisasi", params);
	}
	
	public List selectFilterOtorisasiHPlusSatu(String tipe, String kata, String pilter, Integer jn_bank) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
//		params.put("pilter", tipe);
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("jn_bank",jn_bank);
		return query("selectFilterHPlusSatu", params);
	}
	
	public List selectFilteragency(String tipe, String kata, String pilter) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		params.put("tipe", tipe);
		params.put("kata", kata);
		return query("selectFilteragency", params);
	}
	
	public List selectgutri(int posisi, String tipe, String kata, String pilter) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		params.put("posisi", new Integer(posisi));
		params.put("tipe", tipe);
		params.put("kata", kata);
		return query("selectgutri", params);
	}
	
	public List selectgutriSimple(int posisi, String tipe, String kata, String pilter, String jenis) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		params.put("posisi", new Integer(posisi));
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("jenis", jenis);
		return query("selectgutriSimple", params);
	}
	
	public List selectgutriblacklist(int posisi, String tipe, String kata, String cari, String pilter, String tgl_lahir, String telp, String sumber) {
		
//		if("LIKE%".equals(pilter))
//			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
//		else if("%LIKE".equals(pilter))
//			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
//		else if("%LIKE%".equals(pilter))
//			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
//		else if("LT".equals(pilter))
//			kata = " < (replace(upper(trim('"+kata+"')),'.') ";
//		else if("LE".equals(pilter))
//			kata = " <= replace(upper(trim('"+kata+"')),'.') ";
//		else if("EQ".equals(pilter))
//			kata = " = replace(upper(trim('"+kata+"')),'.') ";
//		else if("GE".equals(pilter))
//			kata = " >= replace(upper(trim('"+kata+"')),'.') ";
//		else if("GT".equals(pilter))
//			kata = " > replace(upper(trim('"+kata+"')),'.') ";
		
		if("LIKE%".equals(pilter))
			kata = " like upper(trim('"+kata+"')) || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||upper(trim('"+kata+"')) ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||upper(trim('"+kata+"')) || '%' ";
		else if("LT".equals(pilter))
			kata = " < upper(trim('"+kata+"')) ";
		else if("LE".equals(pilter))
			kata = " <= upper(trim('"+kata+"')) ";
		else if("EQ".equals(pilter))
			kata = " = upper(trim('"+kata+"')) ";
		else if("GE".equals(pilter))
			kata = " >= upper(trim('"+kata+"')) ";
		else if("GT".equals(pilter))
			kata = " > upper(trim('"+kata+"')) ";

		if("0".equals(sumber)){
			sumber = null;
		}
		if("".equals(telp)){
			telp = null;
		}

		Map params = new HashMap();
		params.put("posisi", new Integer(posisi));
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("cari", cari);
		params.put("telp", telp);
		params.put("sumber", sumber);
		params.put("tgl_lahir", tgl_lahir);
		return query("selectgutriblacklist", params);
	}
	
	public List selectgutripas(int posisi, String tipe, String kata, String cari, String pilter, String tgl_lahir, String telp, String sumber) {
		
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < (replace(upper(trim('"+kata+"')),'.') ";
		else if("LE".equals(pilter))
			kata = " <= replace(upper(trim('"+kata+"')),'.') ";
		else if("EQ".equals(pilter))
			kata = " = replace(upper(trim('"+kata+"')),'.') ";
		else if("GE".equals(pilter))
			kata = " >= replace(upper(trim('"+kata+"')),'.') ";
		else if("GT".equals(pilter))
			kata = " > replace(upper(trim('"+kata+"')),'.') ";

		if("0".equals(sumber)){
			sumber = null;
		}
		if("".equals(telp)){
			telp = null;
		}

		Map params = new HashMap();
		params.put("posisi", new Integer(posisi));
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("cari", cari);
		params.put("telp", telp);
		params.put("sumber", sumber);
		params.put("tgl_lahir", tgl_lahir);
		return query("selectgutripas", params);
	}
	
	public List selectgutri_valid(int posisi, String tipe, String kata, String pilter, Integer lus_id) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		params.put("posisi", new Integer(posisi));
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("lus_id", lus_id);
		return query("selectgutri_valid", params);
	}
	
	public List selectgutri_simple_valid(int posisi, String tipe, String kata, String pilter, Integer lus_id, String jenis) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		params.put("posisi", new Integer(posisi));
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("lus_id", lus_id);
		params.put("jenis", jenis);
		return query("selectgutri_simple_valid", params);
	}
	
	public List selectFilterSpaj3(int posisi, String tipe, String kata, String pilter) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		return selectFilterSpaj3(posisi, tipe, kata);
	}	
	
	public List selectFilterSpaj3(int posisi, String tipe, String kata) throws DataAccessException {
		Map params = new HashMap();
		params.put("posisi", new Integer(posisi));
		params.put("tipe", tipe);
		params.put("kata", kata);
		return query("selectFilterSpaj3", params);
	}

	public List selectBankForJurnal() throws DataAccessException {
		Map params = new HashMap();
		params.put("kata", "");
		return query("selectBankForJurnal", params);
	}
	
	
	public List selectFilterSpaj4( String tipe, String kata, String pilter) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		return selectFilterSpaj4(tipe, kata);
	}	
	
	public List selectFilterSpaj4( String tipe, String kata) throws DataAccessException {
		Map params = new HashMap();
		params.put("tipe", tipe);
		params.put("kata", kata);
		return query("selectFilterSpaj4", params);
	}
	
	public List selectDataNasabahSinarmasSekuritas(String lcb, String jenisReport, Date tglAwal, Date tglAkhir) throws DataAccessException {
		Map m = new HashMap();
		
		if(!lcb.trim().equals("M35")) m.put("lcb_no", lcb.trim());
		
		m.put("jenisReport", jenisReport);
		m.put("tglAwal", tglAwal);
		m.put("tglAkhir", tglAkhir);
		if(jenisReport.equals("tanggal_prod")) 
			return (List) query("selectDataNasabahSinarmasSekuritasProduksi", m);
		else
			return (List) query("selectDataNasabahSinarmasSekuritas", m);
	}

	public List selectJatuhTempoSekuritas(String lcb, Date startDate, Date endDate) throws DataAccessException{
		Map m = new HashMap();
		if(!lcb.trim().equals("M35")) m.put("lcb_no", lcb.trim());
		m.put("startDate", startDate);
		m.put("endDate", endDate);
		return (List) query("selectJatuhTempoSekuritas", m);
	}
	
	public List selectRolloverSekuritas(String lcb, Date startDate, Date endDate) throws DataAccessException{
		Map m = new HashMap();
		if(!lcb.trim().equals("M35")) m.put("lcb_no", lcb.trim());
		m.put("startDate", startDate);
		m.put("endDate", endDate);
		return (List) query("selectRolloverSekuritas", m);
	}
	
	public List selectSudahCairSekuritas(String lcb, Date startDate, Date endDate) throws DataAccessException{
		Map m = new HashMap();
		if(!lcb.trim().equals("M35")) m.put("lcb_no", lcb.trim());
		m.put("startDate", startDate);
		m.put("endDate", endDate);
		return (List) query("selectSudahCairSekuritas", m);
	}
	
	public List selectDataNasabah(List spaj) throws DataAccessException {
		List result = new ArrayList();
		for(int i=0; i<spaj.size(); i++) {
			result.add(querySingle("selectDataNasabah", spaj.get(i).toString().trim()));
		}
		return result;
	}

	public String selectPolicyNumberFromSpaj(String spaj) throws DataAccessException {
		return (String) querySingle("selectPolicyNumberFromSpaj", spaj);
	}
	
	public String selectNoKartuFromId(String msp_id) throws DataAccessException {
		return (String) querySingle("selectNoKartuFromId", msp_id);
	}
	
	public String selectNoKartuPasFromSpaj(String reg_spaj) throws DataAccessException {
		return (String) querySingle("selectNoKartuPasFromSpaj", reg_spaj);
	}
	
	public Map selectKartuPas(String no_kartu) throws DataAccessException {
		Map m = new HashMap();
		m.put("no_kartu", no_kartu);
		return (HashMap) querySingle("selectKartuPas", m);
	}

	public void insertLst_ulangan(String reg_spaj, Date tanggal, String jenis,
			Integer status_polis, String lus_id, String ket) throws DataAccessException {
		ParameterClass param = new ParameterClass();
		param.setReg_spaj(reg_spaj);
		param.setTanggal(tanggal);
		param.setJenis(jenis);
		param.setStatus(status_polis);
		param.setLus_id(new Integer(lus_id));
		param.setKet(ket);
		insert("insert.lst_ulangan", param);
	}

	public String selectInsuredNumber(String spaj) throws DataAccessException {
		return (String) querySingle("selectInsuredNumber", spaj);
	}
	
	public Integer selectCount_PowerSaveProses(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectCount.PowerSaveProses",spaj);
		
	}
	
	public Map selectPowerSaveProses_Kode(String spaj) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj	);
		param.put("mps_kode",new Integer(5));
		return (HashMap) querySingle("select.PowerSaveProses_Kode",param);
	}

	public Map selectDataBank(String spaj) throws DataAccessException {
		return (HashMap) querySingle("select.DataBank",spaj);
	}

	public Map selectRekClientAccount(String spaj) throws DataAccessException {
		return (HashMap) querySingle("select.rekClientAccount",spaj);
	}

	/*public Date selectFAddDate(Date beg_date,Integer i) throws DataAccessException {
		Map param=new HashMap();
		param.put("beg_date",beg_date);
		param.put("i",i);
		return (Date) querySingle("selectFAddMonths",param);
	}*/

	public void insertPwrSaveProses(String spaj,Date idt_begin,Date ldt_next,Integer li_auto,Integer ii_jamin,Date ldt_ps_int,
			Double ldec_bunga,Double ldec_inv,Double ldec_intrs,Integer li_employ,Integer gl_lus_id) throws DataAccessException {
		
	    Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("mps_kode",new Integer(5));
		param.put("idt_begin",idt_begin);
		param.put("ldt_next",ldt_next);
		
		param.put("li_auto",li_auto);
		param.put("ii_jamin",ii_jamin);
		param.put("ldt_ps_int",ldt_ps_int);
		param.put("ldec_bunga",ldec_bunga);
		
		param.put("ldec_inv",ldec_inv);
		param.put("ldec_intrs",ldec_intrs);
		param.put("mps_prm_insurance",new Integer(0));
		param.put("mps_prm_debet",new Integer(0));
		
		param.put("mps_tax",new Integer(0));
		param.put("li_employ",li_employ);
		param.put("gl_lus_id",gl_lus_id);
		
		insert("insert.mst_power_save_proses",param);
	}

	public void insertPwrSaveProsesRo(String spaj,Date idt_begin,Date ldt_last,Integer ii_jamin,
			Double ldec_bunga,Double ldec_inv,Double ldec_intrs,Integer gl_lus_id, Integer flag_breakable) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("mps_kode",new Integer(5));
		param.put("idt_begin",idt_begin);
		param.put("ldt_last",ldt_last);
		
		param.put("ii_jamin",ii_jamin);
		param.put("ldec_bunga",ldec_bunga);
		param.put("ldec_inv",ldec_inv);
		param.put("ldec_intrs",ldec_intrs);
		
		param.put("mpr_tax",new Integer(0));
		param.put("mpr_insurance",new Integer(0));
		param.put("mpr_debet",new Integer(0));
		param.put("mpr_print",new Integer(0));
		if(flag_breakable == null) flag_breakable = 0;
		param.put("mpr_breakable", flag_breakable);
	
		param.put("gl_lus_id",gl_lus_id);
		
		insert("insert.mst_power_save_proses_ro",param);
	}
	
	public void deleteNilai(String spaj, Integer jenis) {
		Nilai nilai = new Nilai();
		nilai.setReg_spaj(spaj);
		nilai.setJenis(jenis);
		delete("delete.mst_nilai", nilai);
	}
	
	public void deleteKirimLB(String spaj, String msps_desc){
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("msps_desc", msps_desc);
		delete("delete.MstPositionSpajKirimLB", map);
	}
	
	public void deletePwrSaveProses(String spaj) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("mps_kode",new Integer(5));
		delete("delete.mst_powersave_proses",param);
	}
	
	public void deletePwrSaveProsesRo(String spaj) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("mps_kode",new Integer(5));
		delete("delete.mst_powersave_proses_pro",param);
	}
	public void updatePowerSaveProses(String spaj,Date idt_begin,Date ldt_next,Integer li_auto,Integer ii_jamin,Date ldt_ps_int,
			Double ldec_bunga,Double ldec_inv,Double ldec_intrs,Integer li_employ,Integer gl_lus_id) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("mps_kode",new Integer(5));
		param.put("ii_jamin",ii_jamin);
		param.put("li_auto",li_auto);
		
		param.put("ldt_ps_int",ldt_ps_int);
		param.put("ldec_bunga",ldec_bunga);
		param.put("idt_begin",idt_begin);
		param.put("ldt_next",ldt_next);
		
		param.put("ldec_inv",ldec_inv);
		param.put("ldec_intrs",ldec_intrs);
		param.put("li_employ",li_employ);
		param.put("gl_lus_id",gl_lus_id);
		
		update("update.mst_power_save_proses",param);
	}
		
	public Double selectRekClientCount (String txtnospaj) throws DataAccessException {
		return (Double) querySingle("elions.bac.select.mst_rek_client.count",txtnospaj);
	}
	
	public List selectBisnisId(String spaj) throws DataAccessException {
		Map map=new HashMap();
		map.put("spaj",spaj);
		return query("select.Data_Usulan",map);
	}	
	/**@Fungsi:	untuk Mengetahui posisi spaj tersebut berasda
	 * @param	String spaj
	 * @return 	Map
	 * @author 	Ferry Harlim
	 * */
	public Map selectF_check_posisi(String spaj) throws DataAccessException {
		return (HashMap)querySingle("F_check_posisi",spaj);
	}
	
	public Map selectWf_get_status(String spaj,int insured) throws DataAccessException {
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mste_insured_no",new Integer(insured));
		
		return (HashMap)querySingle("validationStatusPolis",param);
	}
	
	public Integer selectCountMstReins(String spaj) throws DataAccessException {
		return (Integer)querySingle("select.countMstReins",spaj);
	}
	
	public Integer selectCountReasTempNew(String spaj) throws DataAccessException {
		return (Integer)querySingle("select.countReasTempNew",spaj);
	}
	
	public Integer selectCountMstSampleUw(String spaj) throws DataAccessException {
		return (Integer)querySingle("select.countMstSampleUw",spaj);
	}
	
	public void updateMstReins(String spaj) throws DataAccessException {
		Map param=new HashMap();
		param.put("lspd_id",new Integer(100));
		param.put("kd_print",new Integer(0));
		param.put("reg_spaj",spaj);
		update("update.mst_reins",param);
	}
	
	/**FUngsi:	Untuk update tanggal produksi di tabel EKA.MST_REINS.MSRE_NEXT_PRM_DATE
	 * 			(Hasil Meeting tanggal 14 sept 2007 ) ,sebelumnya pakai tanggal beg_date 
	 * 			terlewat proses reasnya. 
	 * 			filter msre_last_policy_age=0 untuk tidak ke proses >1x   
	 * @param spaj
	 * @param msreNextPrmDate
	 */
	public void updateMstReinsMsreNextPrmDate(String spaj, Date msreNextPrmDate,Integer insuredNo){
		Map param=new HashMap();
		param.put("reg_spaj", spaj);
		param.put("msre_next_prm_date", msreNextPrmDate);
		param.put("mste_insured_no", insuredNo);
		update("updateMstReinsMsreNextPrmDate",param);
	}
	
	public void updateMstPolicyCancelPolis(String spaj) throws DataAccessException {
		Map param=new HashMap();
		param.put("lssp_id",new Integer(13));
		param.put("lspd_id",new Integer(95));
		param.put("kd_bill",new Integer(0));
		param.put("reg_spaj",spaj);
		update("update.mst_policy.cancelpolis",param);
	}
	
	public void updateMstSampleuw(String spaj) throws DataAccessException {
		Map param=new HashMap();
		param.put("kode",new Integer(1));
		param.put("reg_spaj",spaj);
		update("update.mst_sample_uw",param);
	}
	
	public List selectProductInsured(String spaj,BigDecimal insuredNo,Integer active) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("insured_no",insuredNo);
		param.put("active",active);
		
		return query("select.mstProductInsured",param);
		
	}
	
	public BigDecimal selectLstRiderRate(BigDecimal bisnisId,BigDecimal jenis,String kurs,int usiaPPp,int usiaTt ) throws DataAccessException {
		Map param=new HashMap();
		param.put("bisnis_id",bisnisId);
		param.put("jenis",jenis);
		param.put("kurs",kurs);
		param.put("usiapp",new Integer(usiaPPp));
		param.put("usiatt",new Integer(usiaTt));
		return(BigDecimal) querySingle("select.lst_rider_rate2",param);
	}

	public Date selectFaddMonths(String s_tgl, int ai_month ) throws DataAccessException, ParseException {
		Map param=new HashMap();
		param.put("tgl",s_tgl); // dd/mm/yyyy
		param.put("ai_month",new Integer(ai_month));

		Date ldt_next 			= (Date)querySingle("transuw.select.f_add_months", param);
		Date adt_bdate 			= defaultDateFormat.parse(s_tgl);
		DateFormat day 			= new SimpleDateFormat("dd");
		DateFormat monthYear 	= new SimpleDateFormat("MM/yyyy");
		
		//Yusuf - 5 Jan 2010
		//bila 30/11/2009 ditambah satu bulan, hasilnya di oracle adalah 31/12/2009
		//seharusnya hasilnya yg benar adalah 30/12/2009, sehingga ditambahkan coding ini sedikit
		if(Integer.parseInt(day.format(ldt_next)) > Integer.parseInt(day.format(adt_bdate))){
			ldt_next = defaultDateFormat.parse(day.format(adt_bdate) + "/" + monthYear.format(ldt_next));
		}
		
		return ldt_next;
	}
	
	public List selectAllLstBisnis() throws DataAccessException {
		return query("selectAllBisnisIdnName",null);
	}
	
	public List listadmin(String id)throws DataAccessException {
		return query("listadmin",id);
	}
	
	public List selectAllLstBisnisRider(String ls_filter) throws DataAccessException {
		Map param=new HashMap();
		param.put("filter",ls_filter);
		return query("selectAllBisnisIdnNameRider",param);
	}
	
	public void insertRider(Rider rider) throws DataAccessException {
		insert("insert.mst_productInsured",rider);
	}
	
	public void updateRider(Rider rider) throws DataAccessException {
		update("update.mst_product_insured",rider);
	}	

	public String selectBisnisName(BigDecimal bisnisId,BigDecimal bisnisNo) throws DataAccessException {
		Map param=new HashMap();
		param.put("bisnisId",bisnisId);
		param.put("bisnisNo",bisnisNo);
		return (String) querySingle("selectBisnisName",param);
	}
	
	public List selectMstPositionSpaj(String spaj) throws DataAccessException {
		return query("select.mst_position_spaj2",spaj);
	}
	
	public List selectMstPositionSpajWithSubId(String spaj) throws DataAccessException {
		return query("selectMstPositionSpajWithSubId",spaj);
	}
	
	public List selectLstStatusAccept(Integer lssaId) throws DataAccessException {
		Map map=new HashMap();
		map.put("lssa_id", lssaId);
		return query("selectLstStatusAccept",map);
	}
	
	public List selectSubStatusAccept(Integer lssaId) throws DataAccessException {
		Map map=new HashMap();
		map.put("lssa_id", lssaId);
		return query("selectSubStatusAccept",map);
	}
	
	public List selectLstStatusAcceptAksepNFund() throws DataAccessException {
		return query("selectLstStatusAcceptAksepnFund",null);
	}
	
	public List selectLstUser() throws DataAccessException {
		return query("selectLstUser",null);
	}
	
	public List selectLstUser2() throws DataAccessException {
		return query("selectLstUser2",null);
	}

	public void updateMstPolicy(String spaj,int lspd) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("lspd",new Integer(lspd));
		update("update.backtoBac.mst_policy",param);
	}
	/**Fungsi:	Untuk mengupdate posisi (LSPD_ID) polis pada tabel EKA.MST_INSURED
	 * @param 	String spaj, int lspd
	 * @throws 	DataAccessException
	 * @author	Ferry Harlim
	 */
	public void updateMstInsured(String spaj,int lspd) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("lspd",new Integer(lspd));
		update("update.backtoBac.mst_insured",param);
	}
	/**@Fungsi: Untuk Mengupdate kolom LSPD_ID (posisi Polis klo null gak diupdate )dan LSSA_ID (Status Aksep)
	 * 			pada Table EKA.MST_INSURED 
	 * @param	String spaj,Integer lspdId,Integer lssaId,Integer insuredNo
	 * @author 	Ferry Harlim
	 * */
	public void updateMstInsured(String spaj,Integer lspdId,Integer lssaId,Integer insuredNo, Date mste_tgl_aksep){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("lspd_id",lspdId);
		param.put("lssa_id",lssaId);
		param.put("mste_insured_no",insuredNo);
		param.put("mste_tgl_aksep", mste_tgl_aksep);
		update("update.transUw.mst_insured_position", param);
	}
	
	/**Fungsi:	Untuk mengupdate posisi (LSPD_ID) dan status aksep (LSSA_ID) pada tabel EKA.MST_INSURED
	 * @param 	String spaj, int lspd
	 * @throws 	DataAccessException
	 * @author	M. Antasari
	 */
	public void updateMstInsuredBtl(String spaj,int lspd, int lssa) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("lspd",new Integer(lspd));
		param.put("lssa",new Integer(lssa));
		update("update.backtoBac.mst_insured_btl",param);
	}
	
	public List selectMedical(String spaj,Integer insuredNo) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("insuredNo",insuredNo);
		return query("selectLstMedical",param);
	}
	
	public List selectIcd(String spaj,Integer insuredNo) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("insuredNo",insuredNo);		
		return query("selectLstIcd",param);
	}	

	public List selectLsHslReas(String spaj,Integer insuredNo) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("insuredNo",insuredNo);		
		return query("selectLstHslReas",param);
	}	
	
	public List selectAllLstMedicalCheckUp() throws DataAccessException {
		return query("selectAllLstMedicalCheckUp",null);
	}

	public List selectAllLstMedicalCheckUpNew() throws DataAccessException {
		return query("selectAllLstMedicalCheckUpNew",null);
	}
	
	public List selectLsInsurer() throws DataAccessException {
		return query("selectLsInsurer",null);
	}	

	public Integer selectMaxMstDetMedical(String spaj,Integer insured) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("insuredNo",insured);
		
		return (Integer)querySingle("selectLstMedicalMax",param);
	}
	
	public void insertMstDetMedical(Medical medical) throws DataAccessException {
		insert("insert.mst_det_medical",medical);
	}

	public void updateMstDetMedical(Medical medical) throws DataAccessException {
		update("update.mst_det_medical",medical);
	}
	
	public void deleteMstDetMedical(Medical medical) throws DataAccessException {
		delete("delete.mst_det_medical",medical);
	}

	public void insertMstDetIcd(Icd icd) throws DataAccessException {
		insert("insert.mst_det_icd",icd);
	}

	public void insertMstDetHslReas(HslReas hslReas) throws DataAccessException {
		insert("insert.mst_det_hsl_reas",hslReas);
	}	
	
	public List selectAllLstBlackList() throws DataAccessException {
		return query("selectAllLstBlackList",null);
	}

	public void updateBilling(TopUp topup, int tahun, int premi){
		String lsbs = selectBusinessId(topup.getReg_spaj());
		List list=new ArrayList();
		if("183,204,189,193,163,96,182".indexOf(lsbs)>=0){
			list = selectBillingInformation2(topup.getReg_spaj(), topup.getTahun_ke(), topup.getPremi_ke());
		}else{
			list = selectBillingInformation(topup.getReg_spaj(), topup.getTahun_ke(), topup.getPremi_ke());
		}
		Map map = (HashMap) list.get(0);
		Double biaya = new Double(map.get("BIAYA").toString());
		Double bayar = new Double(map.get("BAYAR").toString());
		Double sisa = new Double(map.get("SISA").toString());
		Double totalpremi = selectMstProductInsuredPremiDiscount(topup.getReg_spaj(), 1, 1);
		Double persen;
		Integer flag_bayar = 1;
		Integer flag_sisa = -1;
		Integer hasMerchantFee = selectHasMerchantFee(topup.getReg_spaj(), topup.getTahun_ke(), topup.getPremi_ke());
		String lsbs_id = selectLsbsId(topup.getReg_spaj());
		String lsdbs_number = selectLsdbsNumber(topup.getReg_spaj());
		String bisnisId = f3.format(Integer.parseInt(lsbs_id));
		
		//MANTA - Cek Total Premi (Pokok + Topup)
		if(products.unitLink(bisnisId)) {
			List daftarTopUp = uwDao.selectMstUlinkTopupNewForDetBilling(topup.getReg_spaj());
			if(daftarTopUp.size()>0){
				for(int i=0; i<daftarTopUp.size(); i++){
					Map mapUlink = (HashMap) daftarTopUp.get(i);
					Double premi_tu = (Double) mapUlink.get("MU_JLH_PREMI");
					totalpremi += premi_tu;
				}
			}
		}
		
		if(sisa.doubleValue()>0){ //belum lunas
			persen = new Double((bayar.doubleValue()/biaya.doubleValue())*100);
			if(persen.doubleValue()==0){
				flag_sisa = 3;
			}else{
				flag_sisa = 4; //installment
				flag_bayar = 0;
			}
			
			//MANTA
			if(topup.getMspa_flag_merchant() != null || hasMerchantFee != null){
				Double merchant_fee = new Double(0);
				Integer flag_merchant = topup.getMspa_flag_merchant();
				if(flag_merchant == null) flag_merchant = hasMerchantFee;
				ArrayList<HashMap> listMerchant = Common.serializableList(selectLstMerchantFee(flag_merchant));
				BigDecimal fee = new BigDecimal(listMerchant.get(0).get("PERSENTASE").toString());
				BigDecimal premiawal = new BigDecimal(totalpremi);
				
				merchant_fee = new Double( ((premiawal.multiply(fee)).divide(new BigDecimal(100), RoundingMode.HALF_UP)).doubleValue() );
				if(sisa.doubleValue() == merchant_fee.doubleValue()){
					flag_sisa = 3;
					flag_bayar = 1;
				}
			}
		}else if(sisa.doubleValue()<0){ //bayar kelebihan
			flag_sisa=1;
		}else if(sisa.doubleValue()==0){ //lunas
			flag_sisa=3;
		}
		
		if(lsbs_id.equals("187") && lsdbs_number.equals("6")){
			if(sisa.doubleValue()<=49000){
				flag_sisa=3;
				flag_bayar=1;
			}else{
				flag_sisa=4; //installment
				flag_bayar=0;
			}
		}
		
		if(flag_bayar==1){
			List<Map> listRiderPSave = bacDao.selectRiderSave(topup.getReg_spaj());
			if(listRiderPSave.size()>0){
				for(int i=0;i<listRiderPSave.size();i++){
					Map riderPsave = listRiderPSave.get(i);
					BigDecimal lsbs_id_rider = (BigDecimal) riderPsave.get("LSBS_ID");
					BigDecimal lsdbs_number_rider = (BigDecimal) riderPsave.get("LSDBS_NUMBER");
					Double premi_kurang_bayar = bacDao.selectMrsKurangBayarRiderSave(topup.getReg_spaj(),lsbs_id_rider.toString(), lsdbs_number_rider.toString());
					bacDao.updateRiderSavePaidPremi(topup.getReg_spaj(),lsbs_id_rider.toString(), lsdbs_number_rider.toString(), premi_kurang_bayar, new Double(0) );
				}
			}
		}
		
		updateMst_billing(topup.getReg_spaj(), new Integer(flag_bayar), new Integer(flag_sisa), sisa, new Integer(tahun), new Integer(premi), null, null, null);
	}	

	public List selectUwInfo(String showTabel, String spaj) {
		if(showTabel.equals("0"))
			return selectUwInfoPositionSpaj(spaj);
		else if(showTabel.equals("1"))
			return selectUwInfoListUlangan(spaj);
		else if(showTabel.equals("2"))
			return selectUwInfoBeginDate(spaj);
		else if(showTabel.equals("3"))
			return selectUwInfoStatusPolis(spaj);
		else if(showTabel.equals("4"))
			return selectUwInfoDetailBatal(spaj);
		else if(showTabel.equals("5"))
			return selectUwInfoBillingChange(spaj);
		else if(showTabel.equals("6"))
			return selectUwInfoHistorySalah(spaj);
		else if(showTabel.equals("7"))
			return selectUwInfoHistoryBilling(spaj);
		else if(showTabel.equals("9"))
			return selectUwInfoTanggal(spaj);
		else return null;
	}
	
	public List selectUwPasInfo(String showTabel, String msp_id) {
		if(showTabel.equals("0"))
			return selectUwInfoPositionPas(msp_id);
		else if(showTabel.equals("1"))
			return selectUwInfoPositionFire(msp_id);
		else return null;
	}

	public List selectFilterSpaj(String tipe, String kata, String tglLahir, String pilter) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		return selectFilterSpaj(tipe, kata, tglLahir);
	}
		
	public List<Map> selectFilterSpajNew(String tipe, String kata, String tglLahir, String pilter, Boolean flagLifeOnly) {
		if("LIKE%".equals(pilter))
			kata = " like upper(trim('"+kata+"')) || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||upper(trim('"+kata+"')) ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||upper(trim('"+kata+"')) || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = upper(trim('"+kata+"')) ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		return selectFilterSpajNew(tipe, kata, tglLahir, flagLifeOnly);
	}
	
	public List selectLstPengecualian(){
		return query("selectLstPengecualian",null);
	}
	
	public void updateRider(List lsRider,int iIns){
		int brs=lsRider.size()-iIns;

		if(iIns==0){//jika tidak ada proses add hanya update saja	
			for(int i=0;i<lsRider.size();i++){
				Rider rider=(Rider) lsRider.get(i);
				updateRider(rider);
			}
		}else{//jika ada proses update dan insert
			for(int i=0;i<brs;i++){
				Rider rider=(Rider) lsRider.get(i);
				updateRider(rider);
			}
			for(int i=brs;i<lsRider.size();i++){
				Rider rider=(Rider) lsRider.get(i);
				insertRider(rider);
			}
		}
	}

	public int updateNabUlink(Map dw_1) {
		return update("update.mst_trans_ulink.nab", dw_1);
	}
	
	public void insertUlinkPlatinum(String spaj, int li_ke, int li_mtu, Date ldt_nab, Date ldt_elock, double ldec_jumlah, double ld_unit, double ld_saldo) {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("li_ke", new Integer(li_ke));
		params.put("li_mtu", new Integer(li_mtu));
		params.put("ldt_nab", ldt_nab);
		params.put("ldt_elock", ldt_elock);
		params.put("ldec_jumlah", new Double(ldec_jumlah));
		params.put("ld_unit", new Double(ld_unit));
		params.put("ld_saldo", new Double(ld_saldo));
		insert("insert.UlinkPlatinum", params);
	}
	
	public int updateSaldoUlink(double ld_saldo, double ld_saldo_pp, double ld_saldo_tu, String spaj, int li_ke, String jenis_invest) {
		Map params = new HashMap();
		params.put("ld_saldo", new Double(ld_saldo));
		params.put("ld_saldo_pp", new Double(ld_saldo_pp));
		params.put("ld_saldo_tu", new Double(ld_saldo_tu));
		params.put("spaj", spaj);
		params.put("li_ke", new Integer(li_ke));
		params.put("jenis_invest", jenis_invest);
		return update("update.saldoUlink", params);
	}

	
	public int updatePosisiUlink(int li_pos, String spaj, int li_ke) {
		Map params = new HashMap();
		params.put("li_pos", new Integer(li_pos));
		params.put("spaj", spaj);
		params.put("li_ke", new Integer(li_ke));
		return update("update.posisiUlink", params);
	}
	
	public Map selectSaldoUlink(String spaj, String jenis_invest) {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("jenis_invest", jenis_invest);
		Map result = (Map) querySingle("selectSaldoUlink", params);
		if(result.isEmpty()) return null; else return result;
		//if(result==null) return 0; else return result.doubleValue();
	}
	
	public int selectCountTransUlink(String spaj, int mu_ke){
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("mu_ke", mu_ke);
		return (Integer) querySingle("selectCountTransUlink", params);
	}
	
	public double selectNabUlink(List errors, String jenis_invest, int type_nab, String tgl_nab, String tgl_trans) {
		Map params = new HashMap();
		params.put("jenis_invest", jenis_invest);
		params.put("type_nab", new Integer(type_nab));
		params.put("tgl_nab", tgl_nab);
		Map result = (HashMap) querySingle("selectNabUlink", params);
		
		if(result==null) {
			errors.add("Terdapat kesalahan pada master data investasi. Harap hubungi EDP.");
			return 0;
		}else {
			Double nilai = (Double) result.get("LNU_NILAI");
			if(nilai==null) {
				errors.add("NAB untuk investasi "+result.get("LJI_INVEST")+" tanggal "+tgl_nab+" belum ada. Tanggal Transaksi: " + tgl_trans);
				return 0; 				
			}else {
				return nilai.doubleValue();
			}
		}
	}
	
	public List selectTransUlink(int pos, String startDate, String endDate, String filter) {
		Map params = new HashMap();
		params.put("pos", new Integer(pos));
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("filter", filter);
		return query("selectTransUlink", params);
	}

		
	public void insertNilai(List daftarNilai, String lus_id, int flag_proses, int flag_fix) {		
		for(int i=0; i<daftarNilai.size(); i++) {
			Nilai nilai = (Nilai) daftarNilai.get(i);		
						
			if(products.nilaiTunaiNotFixed(nilai.getLsbs_id()) && nilai.getTahun().intValue() == 8) {
				nilai.setFlag_fix(1);
			}else {
				nilai.setFlag_fix(flag_fix);		}
			
			nilai.setFlag_proses(flag_proses);
			try {
				nilai.setLus_id(Integer.valueOf(lus_id));
			} catch(NumberFormatException e) {
				nilai.setLus_id(0);
			}
			if(nilai.getNilai_tunai()!=null) {
				if(nilai.getLsbs_id() != null){
					if(nilai.getLsbs_id().equals("208")|| nilai.getLsbs_id().equals("219")){
						nilai.setNilai(nilai.getNilai_tunai());
						nilai.setJenis(new Integer(2));
						insert("insertNilai", nilai);
					}
				}else{
					if(nilai.getNilai_tunai().doubleValue()!=0) {
						nilai.setNilai(nilai.getNilai_tunai());
						nilai.setJenis(new Integer(2));
						insert("insertNilai", nilai);
					}
				}
				
			}
			if(nilai.getTahapan()!=null) {
				if(nilai.getTahapan().doubleValue()!=0) {
					nilai.setNilai(nilai.getTahapan());
					nilai.setJenis(new Integer(3));
					insert("insertNilai", nilai);
				}
			}
			if(nilai.getBonus()!=null) {
				if(nilai.getBonus().doubleValue()!=0) {
					nilai.setNilai(nilai.getBonus());
					nilai.setJenis(new Integer(4));
					insert("insertNilai", nilai);
				}
			}
			if(nilai.getSaving()!=null) {
				if(nilai.getSaving().doubleValue()!=0) {
					nilai.setNilai(nilai.getSaving());
					nilai.setJenis(new Integer(5));
					insert("insertNilai", nilai);
				}
			}
			if(nilai.getDeviden()!=null) {
				if(nilai.getDeviden().doubleValue()!=0) {
					nilai.setNilai(nilai.getDeviden());
					nilai.setJenis(new Integer(6));
					insert("insertNilai", nilai);
				}
			}
			if(nilai.getMaturity()!=null) {
				if(nilai.getMaturity().doubleValue()!=0) {
					nilai.setNilai(nilai.getMaturity());
					nilai.setJenis(new Integer(7));
					insert("insertNilai", nilai);
				}
			}					
		}
	}
	
	public String cekAgenTakBerpolis(String spaj) {
		String msag = selectAgenFromSpaj(spaj);
		List tmp = selectAgenCekKomisi(msag);
		Date ldt_birth = new Date();
		String ls_nama = "";
		Double ldec_kom = new Double(0);
		
		if( tmp.size() > 0 ) {
			ldt_birth = (Date) ((Map) tmp.get(tmp.size()-1)).get("MSPE_DATE_BIRTH");
			ls_nama = (String) ((Map) tmp.get(tmp.size()-1)).get("MCL_FIRST");
			ldec_kom = (Double) ((Map) tmp.get(tmp.size()-1)).get("KOMISI");
			int li_polis = selectJumlahPolisAgen(ls_nama, FormatDate.toString(ldt_birth));
			if(li_polis > 0) {
				for(int i=0; i<tmp.size(); i++) {
					Map m = (HashMap) tmp.get(i);
					m.put("CO_POLIS", new Integer(1));
				}
			}
			List dw_1 = new ArrayList();
			for(int i=0; i<tmp.size(); i++) {
				Map m = (HashMap) tmp.get(i);
				double komisi = ((Double) ((Map) tmp.get(tmp.size()-1)).get("KOMISI")).doubleValue();
				int co = ((Integer) ((Map) tmp.get(tmp.size()-1)).get("CO_POLIS"));
				if(komisi > 500000 && co==0) dw_1.add(m);
			}
			if(dw_1.size()<=0) {
				if(ldec_kom.doubleValue() > 500000 && li_polis > 0) return ("Info: Agent sudah mempunyai " + li_polis + " Polis.");
				else if(ldec_kom.doubleValue() <= 500000 && li_polis > 0) return ("Info: Agent sudah mempunyai " + li_polis + " Polis dengan komisi < Rp. 500.000,- "); 
			}
		}
		return null;
	}

	public List<String> saveCsfCall(String spaj, String inout, String lus_id, 
			String[] s_dial, String[] s_jenis, String[] s_ket, String[] s_start, String[] s_end, String[] s_callback) {
		List<String> errorMessage = new ArrayList<String>();
		for(int i=0; i<s_dial.length; i++) {
			if(s_jenis[i].equals("baru")) {
				insertCsfCall(spaj, inout, lus_id, s_dial[i], s_jenis[i], s_ket[i], s_start[i], s_end[i], s_callback[i],null,null);
			}else if(s_jenis[i].equals("lama")) {
				if(selectCekCsfCallSameDay(spaj, s_dial[i])) {
					updateCsfCall(spaj, inout, lus_id, s_dial[i], s_jenis[i], s_ket[i], s_start[i], s_end[i], s_callback[i]);
				}else {
					errorMessage.add("Call ke - " + s_dial[i] + " tidak boleh di-update. Hanya boleh update data pada hari yang sama.");
				}
			}
		}
		
		return errorMessage;
	}
	
	public boolean selectCekCsfCallSameDay(String spaj, String s_dial) {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("s_dial", s_dial);
		int hasil = (Integer) querySingle("selectCekCsfCallSameDay", map);
		if(hasil == 0) return false;
		else return true;
	}
	
	public TopUp insertTopUp(TopUp topup, int aplikasi, User currentUser){
		//ambil sekuens terakhir	
		topup.setMspa_payment_id(this.sequence.sequenceMst_payment(aplikasi, topup.getLca_polis()));
		
		//insert payment ke 2 tabel
		insertMst_payment(topup, currentUser.getLus_id() );
		insertMst_tag_payment(topup, topup.getTahun_ke(), topup.getPremi_ke());
		//update billing
		updateBilling(topup, topup.getTahun_ke(), topup.getPremi_ke());
		return topup;
	}

	public List selectProductInsured2(String spaj,BigDecimal insuredNo,Integer active){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("insured_no",insuredNo);
		param.put("active",active);
		
		return query("select.mstProductInsured2",param);
		
	}

	public List selectSimultanDetail(String value){
		Map param=new HashMap();
		param.put("nilai",value);
		param.put("kode",new Integer(1));//gak di pakai lsg di query pake (1,2) (individu/MRI)
		return query("simultan",param);
	}
	
	public List selectSimultan(Map map){
		return query("select.simultan",map);
	}
	public List selectSimultanNew(Map map){
		return query("select.simultan_new",map);
	}
	/**
	 * Fungsi:	Untuk Menampilkan Simultan Polis berdasarkan nama dan tanggal lahir tertanggung
	 * @param 	String nama,String tglLahir
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectViewSimultan(String nama,String tglLahir){
		Map map=new HashMap();
		map.put("nama",nama);
		map.put("tgl_lahir",tglLahir);
		return query("selectViewSimultan",map);
	}
	/**Fungsi:	Untuk menampilkan posisi Spaj dan kode posisi spaj tersebut
	 * @param 	String spaj
	 * @return	Map
	 * @author	Ferry Harlim
	 */
	public Map selectCheckPosisi(String spaj){
		return (HashMap) querySingle("select.f.check.posisi", spaj);
	}
	
	public List selectAllLstIdentity(){
		return query("selectAll.lstIdentity",null);
	}

	public void insertTestStable(String no_reg, Date tgl, int nomor, int flag_siapa) throws DataAccessException{
		Map p = new HashMap();
		p.put("no_reg", no_reg);
		p.put("tgl", tgl);
		p.put("nomor", nomor);
		p.put("flag_siapa", flag_siapa);
		insert("insertTestStable", p);
	}
	
	public int updateCounter(String nilai, int aplikasi, String cabang) throws DataAccessException {
		Map params = new HashMap();
		params.put("nilai", Long.valueOf(nilai));
		params.put("aplikasi", new Integer(aplikasi));
		params.put("cabang", cabang);
		if(Long.valueOf(nilai)<0){
			throw new RuntimeException("ERROR INSERT MST_COUNTER, NILAI : " + Long.valueOf(nilai));
		}
		return update("update.mst_counter", params);
		
	}
	
	public int updateCounterMonthYear(String nilai, int aplikasi, String cabang) throws DataAccessException {
		Map params = new HashMap();
		params.put("nilai", Long.valueOf(nilai));
		params.put("aplikasi", new Integer(aplikasi));
		params.put("cabang", cabang);
		if(Long.valueOf(nilai)<0){
			throw new RuntimeException("ERROR INSERT MST_COUNTER, NILAI : " + Long.valueOf(nilai));
		}
		return update("update.mst_counter.month_year", params);
	}
	
	public int updateCounterValueEb(String nilai, int aplikasi, String cabang) throws DataAccessException {
		Map params = new HashMap();
		params.put("nilai", Long.valueOf(nilai));
		params.put("aplikasi", new Integer(aplikasi));
		params.put("cabang", cabang);
		if(Long.valueOf(nilai)<0){
			throw new RuntimeException("ERROR INSERT MST_COUNTER, NILAI : " + Long.valueOf(nilai));
		}
		return update("update.mst_counter_eb.msco_value", params);
	}
	
	public void updateMstCounter(Double ld_cnt,int ai_id,String ls_cabang){
		Map param=new HashMap();
		param.put("msco_value",ld_cnt);
		param.put("msco_number",new Integer(ai_id));
		param.put("lca_id",ls_cabang);
		if(ld_cnt<0){
			throw new RuntimeException("ERROR INSERT MST_COUNTER, NILAI : " + ld_cnt);
		}
		update("update.mst_counters",param);
	}
	
	public void updateMstCounter2(String ld_cnt,int ai_id,String ls_cabang){
		Map param=new HashMap();
		param.put("msco_value",ld_cnt);
		param.put("msco_number",new Integer(ai_id));
		param.put("lca_id",ls_cabang);
		if(Long.valueOf(ld_cnt)<0){
			throw new RuntimeException("ERROR INSERT MST_COUNTER, NILAI : " + Long.valueOf(ld_cnt));
		}
		update("update.mst_counters",param);
	}

	public void updateMstCounter3(Integer ld_cnt,int ai_id,String ls_cabang){
		Map param=new HashMap();
		param.put("msco_value",Long.valueOf(ld_cnt));
		param.put("msco_number",new Integer(ai_id));
		param.put("lca_id",ls_cabang);
		if(Long.valueOf(ld_cnt)<0){
			throw new RuntimeException("ERROR INSERT MST_COUNTER, NILAI : " + Long.valueOf(ld_cnt));
		}
		update("update.mst_counters",param);
	}

	
	public Client selectMstClientNew(String mclIdnew,String mclIdOld){
		Map param=new HashMap();
		param.put("mcl_id_new",mclIdnew);
		param.put("mcl_id_old",mclIdOld);
		return (Client) querySingle("select.eka.mst_client_new",param);
	}
	
	public void insertMstClientNew(Client client){
		getSqlMapClientTemplate().insert("elions.uw.insert.mst_client_new",client);
	}
	
	public void updateMstClientNew(Client client){
		getSqlMapClientTemplate().update("elions.uw.update.mst_client_new",client);
	}
	
	public void updateClientToBlacklist(Client client){
		getSqlMapClientTemplate().update("elions.uw.update.client_to_blacklist",client);
	}
	
	public AddressNew selectMstAddressNew(String mclIdNew,String mclIdOld){
		Map param=new HashMap();
		param.put("mcl_id_new",mclIdNew);
		param.put("mcl_id_old",mclIdOld);
		return (AddressNew)querySingle("select.eka.mst_address_new",param);
	}
	
	public void insertMstAddressNew(AddressNew addressNew){
		getSqlMapClientTemplate().insert("elions.uw.insert.mst_address_new",addressNew);
	}
	
	public void insertLstBlacklist(BlackList blacklist){
		getSqlMapClientTemplate().insert("elions.uw.insert.lst_black_list",blacklist);
	}
	
	public void insertLstDetBlacklist(DetBlackList detBlacklist){
		getSqlMapClientTemplate().insert("elions.uw.insert.lst_det_black_list",detBlacklist);
	}

	
	public void deleteLstDetBlacklist(String lbl_id){
		Map param=new HashMap();
		param.put("lbl_id",lbl_id);
		getSqlMapClientTemplate().insert("elions.uw.deleteLstDetBlacklist",param);
	}
	
	public void updateLstBlacklist(BlackList blacklist){
		getSqlMapClientTemplate().update("elions.uw.update.lst_black_list",blacklist);
	}
	
	public void insertMstDrekDet(String noTrx, String tahunKe, String premiKe, 
			Double jumlah, Integer noUrut, String regSpaj, Integer transKe, 
			String paymentId, String createId, Date createDate, String no_pre, String norek_ajs, String jenis, Date tgl_trx){
		Map param=new HashMap();
		param.put("noTrx",noTrx);
		param.put("tahunKe",tahunKe);
		param.put("premiKe",premiKe);
		param.put("jumlah",jumlah);
		param.put("noUrut",noUrut);
		param.put("regSpaj",regSpaj);
		param.put("transKe",transKe);
		param.put("paymentId",paymentId);
		param.put("createId",createId);
		param.put("createDate",createDate);
		param.put("no_pre", no_pre);
		param.put("norek_ajs", norek_ajs);
		param.put("jenis", jenis);
		param.put("tgl_trx", tgl_trx);
		getSqlMapClientTemplate().insert("elions.uw.insert.insertMstDrekDet",param);
	}

	public void insertLstBlacklistFamily(BlackListFamily blacklistFamily){
		getSqlMapClientTemplate().insert("elions.uw.insert.lst_black_list_family",blacklistFamily);
	}
	
	public void updateLstBlacklistFamily(BlackListFamily blacklistFamily){
		getSqlMapClientTemplate().update("elions.uw.update.lst_black_list_family",blacklistFamily);
	}

	
	public void updateMstPolicyMspoPolicyHolder(String spaj,String mspoPolicyHolder){
		Map up_policy=new HashMap();
		up_policy.put("ls",mspoPolicyHolder);
		up_policy.put("nospaj",spaj);
		//logger.info(up_policy);
		update("simultan.update.mst_policy",up_policy);
	}
	
	public void updateMstInsuredMsteInsured(String spaj,String msteInsured){
		Map up_insured=new HashMap();
		up_insured.put("ls",msteInsured);
		up_insured.put("nospaj",spaj);
		update("simultan.update.mst_insured",up_insured);
	}
	
	public List selectMstStsClient(String MclId,Integer ljcId){
		Map param=new HashMap();
		param.put("mcl_id",MclId);
		param.put("ljc_id",ljcId);
		return query("select.eka.mst_sts_client.msc_active",param);
	}
	
	public void insertMstStsClient(String mclId,Integer ljcId,Integer mscActive){
		Map insParam=new HashMap();
		insParam.put("mcl_id",mclId);			
		insParam.put("ljc_id",ljcId);
		insParam.put("msc_active",mscActive);
		getSqlMapClientTemplate().insert("elions.uw.insert.mst_sts_client",insParam);
	}
	
	public void updateMstStsClient(String mclId,Integer ljcId,Integer mscActive){
		Map upParam=new HashMap();
		upParam.put("mcl_id",mclId);
		upParam.put("ljc_id",ljcId);
		upParam.put("msc_active",mscActive);
		update("simultan.update.mst_sts_client",upParam);
	}
	
	public Double selectLstMonthlyKurs(String kurs){
		return (Double)querySingle("select.eka.lst_monthly_kurs_lmk_nilai",kurs);
	}
	
	public Double selectCekPremi(Double ldec_kurs,String mclId,String lkuId,Integer value){
		Map param=new HashMap();
		param.put("ldec_kurs",ldec_kurs);
		param.put("mste_insured",mclId);
		param.put("lku_id",lkuId);
		param.put("value",value);
		return(Double) querySingle("select.cekPremi",param);
	}
	
	public Double selectTahunKe(String asTgl,String asToday){
		Map param=new HashMap();
		param.put("as_tgl",asTgl);
		param.put("as_today",asToday);
		return (Double)querySingle("transuw.select.f_check_tahun_ke",param);
	}
	
	public Integer selectLstReinsDesc1 (String as_kurs,int ai_bisnis_id,int ai_bisnis_no){
		Map param=new HashMap();
		param.put("ai_bisnis_id",new Integer(ai_bisnis_id));
		param.put("ai_bisnis_no",new Integer(ai_bisnis_no));
		param.put("as_kurs",as_kurs);
		return (Integer)querySingle("select.lst_reins_desc1",param);
	}
	
	public Double selectLstTableAwal(int ai_type,int ai_bisnis_id,String as_kurs,int ai_cbayar,int ai_lbayar,int ai_ltanggung,int ai_year,int ai_age){
		Map param=new HashMap();
		param.put("ai_type",new Integer(ai_type));
		param.put("ai_bisnis_id",new Integer(ai_bisnis_id));
		param.put("as_kurs",as_kurs);
		param.put("ai_cbayar",new Integer(ai_cbayar));
		param.put("ai_lbayar",new Integer(ai_lbayar));
		param.put("ai_ltanggung",new Integer(ai_ltanggung));
		param.put("ai_year",new Integer(ai_year));
		param.put("ai_age",new Integer(ai_age));
		return (Double)querySingle("select.lst_table_awal",param);
	}
	
	//investasi proses
	public List selectMstUlink(String spaj,Integer muKe){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("ar_ke",muKe);
		return query("select.mst_ulinks2",param);
	}
	
	public List selectMstDetUlink(String spaj,Integer muKe,Integer mduAktif){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("ar_ke",muKe);
		param.put("mdu_aktif",mduAktif);
		return query("select.mst_det_ulinks2",param);
	}
	
	public List selectMstBiayaUlink(String spaj,Integer muKe){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("ar_ke",muKe);
		return query("select.mst_biaya_ulinks2",param);
	}

	public Map selectMstCounter(Integer mscoNumber, String lcaId){
		Map param=new HashMap();
		param.put("msco_number",mscoNumber);
		param.put("lca_id",lcaId);
		return (HashMap) getSqlMapClientTemplate().queryForObject("elions.akseptasi_ssh.selectMstCounter",param);
	}

//	public Map selectMstCounter(Integer mscoNumber, String lcaId) throws DataAccessException{
//		Map param=new HashMap();
//		param.put("msco_number",mscoNumber);
//		param.put("lca_id",lcaId);
//		return (HashMap) querySingle("selectMstCounter",param);
//	}

	public Double f_get_counter(Integer id,String cab){
		Double hasil=null;
		Double max=null;
		Map a=selectMstCounter(id,cab);
		if(a.get("MSCO_VALUE")!=null)
			hasil=(Double) a.get("MSCO_VALUE");
		if(a.get("MSCO_MAX")!=null)
			max=(Double)a.get("MSCO_MAX");
		if(max==null)
			hasil=null;

		if(hasil==null)
			return null;
		else
			if (hasil.compareTo(max)>=0)
				hasil=new Double(0);
		
		return hasil;
	}
	
	
	public String wf_get_client_id(String lcaId)throws Exception {
		String lsClient=null;
/*		String cnt=selectGetCounter(3, lcaId);
		updateMstCounter2(cnt,3,lcaId);
		NumberFormat nf=new DecimalFormat("0000000000");
		String temp=nf.format(Integer.parseInt(cnt));
//		String temp = SimultanFormController.str(String.valueOf(Integer.parseInt(cnt)), "0000000000");
		lsClient=lcaId+temp;
		int pjg = lsClient.trim().length();
		
		if ((lsClient== null) || pjg!= 12) {
			return null;
		}
*/		
		lsClient = bacDao.selectSequenceClientID();		
		return lsClient;
	}
	
	public Integer wf_get_blacklist_id()throws Exception {
		return (Integer) querySingle("select_blacklist_id",null);
	}
	
	public Integer wf_get_blacklistFamily_id()throws Exception {
		return (Integer) querySingle("select_blacklistFamily_id",null);
	}
	
	public String selectProductName(String reg_spaj){
		return (String) querySingle("select_productName",reg_spaj);
	}

/*	private String wf_get_client_id(String lcaId)throws Exception {
		Double count;
		String lsClient=null;
		
		count=f_get_counter(new Integer(3),lcaId);
		if(count==0){
			count=new Double(1);
		}else{
			count=new Double(count+1);
		}
		//
		updateMstCounter(count,3,lcaId);
		String temp = SimultanFormController.str(String.valueOf(count), "0000000000");
		lsClient=lcaId+temp;
		
		int pjg = lsClient.trim().length();
		
		if ((lsClient== null) || pjg!= 12) {
			//MessageBox('Alert', 'Error Generate Kode Tertanggung.~nPesan : ' + ls_mess)
			return null;
		}
		return lsClient;
	}*/

	public void wf_sts_client(String mclId,Integer ljcId){
		List list=selectMstStsClient(mclId,ljcId);
		if(list.isEmpty()){
			insertMstStsClient(mclId,ljcId,new Integer(1));
		}else{
			updateMstStsClient(mclId,ljcId,new Integer(1));
		}
		
	}
		
	public Integer prosesSimultan(int proses,String spaj,String lcaId,String lsClientPpOld,String lsClientTtOld)throws Exception{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String lsClientPpNew = null,lsClientTtNew=null;
		String lsClientPp = null,lsClientTt=null;
		
		if(proses==1 || proses==3){
			if (lsClientPpOld.substring(0,2).equalsIgnoreCase("00") ){ // sudah mengunakan pac_conter dari awal proses BAC , jadi tidak perlu generate MCL_ID yang baru.
				lsClientPpNew = lsClientPpOld;
			}else{
				lsClientPpNew=wf_get_client_id(lcaId);
				if(lsClientPpNew==null){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return null;
				}
				//
				Client client=new Client();
				AddressNew addressNew=new AddressNew();
				client=selectMstClientNew(lsClientPpNew,lsClientPpOld);
				addressNew=selectMstAddressNew(lsClientPpNew,lsClientPpOld);
				insertMstClientNew(client);
				insertMstAddressNew(addressNew);
				updateMstPolicyMspoPolicyHolder(spaj,lsClientPpNew);
				updateMstInsuredMsteInsured(spaj,lsClientPpNew);
			}
			lsClientPp=lsClientPpNew;
			lsClientTt=lsClientPpNew;
		}else if(proses==2){
			//pemegang
			if (lsClientPpOld.substring(0,2).equalsIgnoreCase("00") ){ // sudah mengunakan pac_conter dari awal proses BAC , jadi tidak perlu generate MCL_ID yang baru.
				lsClientPpNew = lsClientPpOld;
				lsClientTtNew = lsClientTtOld;
			}else{
				lsClientPpNew=wf_get_client_id(lcaId);
				if(lsClientPpNew==null){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return null;
				}
				Client client=new Client();
				AddressNew addressNew=new AddressNew();
				client=selectMstClientNew(lsClientPpNew,lsClientPpOld);
				addressNew=selectMstAddressNew(lsClientPpNew,lsClientPpOld);
				insertMstClientNew(client);
				insertMstAddressNew(addressNew);
				updateMstPolicyMspoPolicyHolder(spaj,lsClientPpNew);
				//tertanggung
				lsClientTtNew=wf_get_client_id(lcaId);
				if(lsClientTtNew==null){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return null;
				}
				Client client2=new Client();
				AddressNew addressNew2=new AddressNew();
				client2=selectMstClientNew(lsClientTtNew,lsClientTtOld);
				addressNew2=selectMstAddressNew(lsClientTtNew,lsClientTtOld);
				insertMstClientNew(client2);
				insertMstAddressNew(addressNew2);
				updateMstInsuredMsteInsured(spaj,lsClientTtNew);
			}
			lsClientPp=lsClientPpNew;
			lsClientTt=lsClientTtNew;
		}else if(proses==4){
			updateMstPolicyMspoPolicyHolder(spaj,lsClientPpOld);
			updateMstInsuredMsteInsured(spaj,lsClientPpOld);
			lsClientPp=lsClientPpOld;
			lsClientTt=lsClientPpOld;
		}else if(proses==5){
			//proses pemegang
			if(lsClientPpOld.substring(0,2).equalsIgnoreCase("XX")|| lsClientPpOld.substring(0,2).equalsIgnoreCase("WW")){
				lsClientPpNew=wf_get_client_id(lcaId);
				if(lsClientPpNew==null){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return null;
				}
				Client client=new Client();
				AddressNew addressNew=new AddressNew();
				client=selectMstClientNew(lsClientPpNew,lsClientPpOld);
				addressNew=selectMstAddressNew(lsClientPpNew,lsClientPpOld);
				insertMstClientNew(client);
				insertMstAddressNew(addressNew);
				updateMstPolicyMspoPolicyHolder(spaj,lsClientPpNew);
				lsClientPp=lsClientPpNew;
			}else{
				updateMstPolicyMspoPolicyHolder(spaj,lsClientPpOld);
				lsClientPp=lsClientPpOld;
			}
			//proses tertanggung
			if(lsClientTtOld.substring(0,2).equalsIgnoreCase("XX")|| lsClientTtOld.substring(0,2).equalsIgnoreCase("WW")){
				lsClientTtNew=wf_get_client_id(lcaId);
				if(lsClientTtNew==null){
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return null;
				}
				
				Client client2=new Client();
				AddressNew addressNew2=new AddressNew();
				client2=selectMstClientNew(lsClientTtNew,lsClientTtOld);
				addressNew2=selectMstAddressNew(lsClientTtNew,lsClientTtOld);
				insertMstClientNew(client2);
				insertMstAddressNew(addressNew2);
				updateMstInsuredMsteInsured(spaj,lsClientTtNew);
				lsClientTt=lsClientTtNew;
			}else{
				updateMstInsuredMsteInsured(spaj,lsClientTtOld);
				lsClientTt=lsClientTtOld;
			}
		}
		wf_sts_client(lsClientPp,new Integer(1));
		wf_sts_client(lsClientTt,new Integer(1));
		return new Integer(0);
	}
	
	public void updateMstPolicyUnderTable(String spaj,double ldecBonus){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("ldecBonus",new Double(ldecBonus));
		
		update("update.mst_policy_under_table",param);
		
	}
	
	public Double selectMstPolicyUnderTable(String spaj){
		return (Double)querySingle("selectNilaiUnderTable",spaj);
	}
	
	public Date selectMaxLstBunga(String kurs,Integer jns){
		Map param=new HashMap();
		param.put("kurs",kurs);
		param.put("jns",jns);
		return (Date)querySingle("selectMaxLstBunga",param);
	}

	public Double selectLstBungaLsbunBunga(String kurs,Integer jns,Date tgl){
		Map param=new HashMap();
		param.put("jns",jns);
		param.put("kurs",kurs);
		param.put("tgl",tgl);
		return (Double)querySingle("selectLstBungaLsbunBunga",param);			
	}

	public Double selectMstDefaultMsdefNumeric(Integer li_id){
		return (Double) querySingle("select.mst_default",li_id);
	}
	
	public Double selectLstTableLstabValue(Integer jenis, Integer li_bisnis, String kurs, Integer pmode,
			Integer pperiod,Integer insPer, Integer tahunKe, Integer umurTt){
		Map param=new HashMap();
		param.put("jenis",jenis);
		param.put("li_bisnis",li_bisnis);
		param.put("txtkurs_id",kurs);
		param.put("li_pmode",pmode);
		param.put("txt_pperiod",pperiod);
		param.put("li_insper",insPer);
		param.put("tahun_ke",tahunKe);
		param.put("txtumur",umurTt);
		return (Double) querySingle("select.lst_table",param);
	}
	
	public List selectMstProductInsuredRider(String spaj,Integer insured,Integer active){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("insured",insured);
		param.put("active",active);
		return query("select.d_mst_prod_ins_rider2",param);
	}

	public Integer selectLstJenisBiayaLjbId(Integer lsbsId,Integer lsdbsNumber){
		Map param=new HashMap();
		param.put("li_bisnis",lsbsId);
		param.put("li_no",lsdbsNumber);
		return (Integer)querySingle("select.lst_jns_biaya.ljb_id",param);
	}
	
	public String selectLstJenisBiayaLjbBiaya(Integer ljbId){
		Map param=new HashMap();
		param.put("ljb_id",ljbId);
		return (String)querySingle("select.lst_jns_biaya.ljb_biaya",param);
	}
	
	public List selectLstJenisInvest(){
		return query("selectLstJenisInvestLjiId",null);
	}
	
	public Map selectWfGetStatus(String spaj,Integer insured){
		Map param = new HashMap();
		param.put("txtnospaj", spaj);
		param.put("li_insured_no", insured);
		return (HashMap)querySingle("select.wf_get_status",param);
	}
	
	public List selectd_mst_prod_ins(String spaj,Integer insNo){
		Map param =new HashMap();
		param.put("nospaj",spaj);
		param.put("ins_no",insNo);
		return query("select.d_mst_prod_ins",param);
	}

	/**@Fungsi:	Untuk Menampilkan data pemegang polis secara detail
	 * @param	String spaj,Integer lspdId,Integer lstbId
	 * @return 	com.ekalife.elions.web.Model.Policy
	 * @author 	Ferry Harlim
	 * */
	public Policy selectDw1Underwriting(String spaj,Integer lspdId,Integer lstbId){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("lspdId",lspdId);
		param.put("lstbId",lstbId);
		return (Policy) querySingle("selectDw1Underwriting",param);
	}
	
	public Integer selectMstInsuredMsteBackup(String spaj,Integer insuredNo){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("txtinsured_no",insuredNo);
		return (Integer)querySingle("select.mste_backup",param);
	}
	/**Fungsi	Untuk menampilkan jumlah persen investasi produk link pada tabel EKA.MST_DET_ULINK
	 * @param 	String spaj
	 * @return	Double
	 * @author	Ferry Harlim
	 */
	public Double selectSumPersenMstDetUlink(String spaj){
		return (Double)querySingle("select.mst_det_ulink",spaj);
	}
	/**Fungsi	Untuk Menampilkan jumlah premi pada tabel EKA.MST_ULINK
	 * @param 	String spaj
	 * @return	Double
	 * @author	Ferry Harlim
	 */
	public Double selectSumJlhPremiMstUlink(String spaj){
		return (Double)querySingle("select.mst_ulink",spaj);
			
	}
	/**@Fungsi:	Untuk Menampilkan Data pembayaran awal/titipan premi premi pada tabel EKA.MST_DEPOSIT_PREMIUM
	 * @param	String spaj,Integer flag	
	 * @return 	List
	 * @author 	Ferry Harlim
	 * */
	public List selectMstDepositPremium(String spaj, Integer flag){
		Map param = new HashMap();
		param.put("spaj",spaj);
		param.put("flag",flag);
		return query("transuw.select.mst_depositPremium",param);
	}
	
	public Integer selectMstSampleUwStatusBatal(String spaj,String mclId){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("mclId",mclId);
		return (Integer)querySingle("select.mst_sample_uw_status_batal",param);
	}
	
	public void updateMstInsuredMedical(String spaj,Integer medical){
		Map upParam=new HashMap();
		upParam.put("spaj",spaj);
		upParam.put("mste_medical",medical);
		update("update.mst_insured_medical",upParam);
	}
	
	public Date selectMstDefaultMsdefdate(Integer id){
		return (Date)querySingle("select.mst_default.msdef_date",id);
	}
	
	public void updateMstInsuredStatus(String spaj,Integer insuredNo,Integer liAksep,Integer liAktif,String lusId,Integer flagTgl){
		Map up=new HashMap();
		up.put("txtnospaj",spaj);
		up.put("txtinsured_no",insuredNo);
		up.put("li_aksep",liAksep);
		up.put("li_aktif",liAktif);
		up.put("lusId",lusId);
		up.put("flagTgl",flagTgl);
		update("update.mst_insured_status",up);
	}
	
	public void updateMstPasSmsStatus(String msp_id,Integer msp_status, Date msp_tgl_status, String msp_ket_status, String lusId){
		Map up=new HashMap();
		up.put("msp_id",msp_id);
		up.put("msp_tgl_status",msp_tgl_status);
		up.put("msp_ket_status",msp_ket_status);
		up.put("msp_status",msp_status);
		up.put("lusId",lusId);
		update("update.mst_pas_sms_status",up);
	}
	
	public void updateMstInsuredKirimAdmedika(String spaj,Integer insuredNo,String lusId){
		Map up=new HashMap();
		up.put("txtnospaj",spaj);
		up.put("txtinsured_no",insuredNo);
		up.put("lusId",lusId);
		update("update.mst_insured_kirim_admedika",up);
	}
	
	public void prosesCekSimultanStableLink(String reg_spaj) {
		List<String> daftar = query("selectSimultanStableLink", reg_spaj);
		String old_polis = null;
		if(!daftar.isEmpty()) old_polis = daftar.get(0);
		else old_polis = uwDao.selectPolicyNumberFromSpaj(reg_spaj);
		Map p = new HashMap();
		p.put("reg_spaj", reg_spaj);
		p.put("old_polis", old_polis);
		update("updateSimultanStableLink", p);
	}
	
	/**@Fungsi:	Untuk Melakukan Proses Akseptasi dan Fund Allocation (khusus untuk produk unit link)
	 * @param	Akseptasi akseptasi,int thn,int bln,String desc, BindException err
	 * @return 	int
	 * @author 	Ferry Harlim
	 * */
	public int prosesAkseptasi(Akseptasi akseptasi,int thn,int bln,String desc, BindException err)throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		int liAktif=0;
		int setNopol = 0;
		
		if(akseptasi.getProses().equals("1")){
			
			if(akseptasi.getLiAksep()==5 || akseptasi.getLiAksep()==10)
				liAktif=1;
			
			//
			if(akseptasi.getNoPolis()==null) {
				setNopol=wf_set_nopol(akseptasi, 2);
			}
			
			if(akseptasi.getLiAksep()==5 || akseptasi.getLiAksep()==10){//akseptasi
				if(setNopol>0){//rollback dan tampilkan message
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return setNopol;
				}
				
				updateMstInsuredStatus(akseptasi.getSpaj(),akseptasi.getInsuredNo(),
						akseptasi.getLiAksep(),liAktif,akseptasi.getCurrentUser().getLus_id(),new Integer(1));
				if(akseptasi.getLiAksep()==5) saveMstTransHistory(akseptasi.getSpaj(), "tgl_akseptasi_polis", null, null, null);
				if(akseptasi.getLiAksep()==10) saveMstTransHistory(akseptasi.getSpaj(), "tgl_special_accept", null, null, null);
				HashMap firstproses = new HashMap();
				firstproses = uwDao.selectFirstUserUwProses(akseptasi.getSpaj());
				if(firstproses != null)
					try {
						SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						saveMstTransHistory(akseptasi.getSpaj(), "tgl_uw_proses", df.parse(firstproses.get("MSPS_DATE").toString()), "user_uw_proses", firstproses.get("LUS_ID").toString());
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						//logger.error("ERROR :", e1);
					}
				//insertMstPositionSpaj(akseptasi.getCurrentUser().getLus_id(), desc.toUpperCase(), akseptasi.getSpaj());
				insertMstPositionSpajWithSubId(akseptasi.getCurrentUser().getLus_id(), desc.toUpperCase(), akseptasi.getSpaj(), akseptasi.getSubliAksep());
				//29/8/08 yusuf - untuk stable link, harus cek apakah dalam 1 id_simultan, pernah punya stable link sebelumnya
				//bila memang punya, maka update tabel mst_policy (kolom old polis)
				prosesCekSimultanStableLink(akseptasi.getSpaj());
				
				int lsbs_id = Integer.parseInt(uwDao.selectBusinessId(akseptasi.getSpaj()));
				int lsdbs_number = uwDao.selectBusinessNumber(akseptasi.getSpaj());
				//17/03/09 Yusuf - untuk produk muamalat, email
				if(akseptasi.getLiAksep() == 5){
					String nama = uwDao.selectNamaBusiness(lsbs_id, lsdbs_number);
					
					if(products.muamalat(akseptasi.getSpaj())) {
						String alamat = this.uwDao.selectEmailCabangFromSpaj(akseptasi.getSpaj());
						if(alamat != null){
							String pp = uwDao.selectPolicyHolderNameBySpaj(akseptasi.getSpaj());
							try {
								EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, false, 
										props.getProperty("admin.ajsjava"), 
										new String[]{alamat}, 
										new String[]{
											"shopiah@sinarmasmsiglife.co.id", "hanifah@sinarmasmsiglife.co.id",
											"Fouresta@sinarmasmsiglife.co.id", 
											"asriwulan@sinarmasmsiglife.co.id"},
										null, 
										"Akseptasi Fitrah Card SPAJ " + akseptasi.getSpaj() + " " + nama, 
										"SPAJ No " + akseptasi.getSpaj() + " a/n " + pp + " telah diaksep oleh Dept.Underwriting", null, akseptasi.getSpaj());
								
							} catch (MailException e) {
								logger.error("ERROR :", e);
							} 				
						}
					}
				}
				String nama_pp="";
				String nama_tt="";
				
				Pemegang pp = bacDao.selectpp(akseptasi.getSpaj());
				Tertanggung tt = bacDao.selectttg(akseptasi.getSpaj());
				nama_pp = pp.getMcl_first().toUpperCase();
				nama_tt = tt.getMcl_first().toUpperCase();
				String lca_id = uwDao.selectCabangFromSpaj(akseptasi.getSpaj());
				String lsdbs_name = uwDao.selectNamaBusiness(lsbs_id, lsdbs_number);
				
				//Yusuf - 25 May 09 - update virtual account
				if(lca_id.equals("58")){
					String va = financeDao.selectVirtualAccountSpaj(akseptasi.getSpaj());
					if (va==null || va.equals("")){
						bacDao.updateVirtualAccountBySpaj(akseptasi.getSpaj());
					}
				}
				
				//Map rekNasabahBancass1 = uwDao.selectrekNasabahBancass1(akseptasi.getSpaj());
				Map rekNasabahBancass1 = uwDao.selectDataBank(akseptasi.getSpaj());
				//Untuk email ke Team Hisar (bancass1) apabila rekening tidak ada dan status aksep sudah akseptasi/akseptasi khusus/further requirement
				if(lca_id.equals("09") && (akseptasi.getLiAksep()==3 || akseptasi.getLiAksep()==5 || akseptasi.getLiAksep()==10) && rekNasabahBancass1.size()==0){
					String mspo_policy_no = uwDao.selectNoPolisFromSpaj(akseptasi.getSpaj());
					String nama_akseptasi = "";
					if(akseptasi.getLiAksep()==3) nama_akseptasi="Further Requirement";
					else if(akseptasi.getLiAksep()==5) nama_akseptasi="Accepted";
					else if(akseptasi.getLiAksep()==10) nama_akseptasi="Akseptasi Khusus";
					
					Map jenis_bancass = uwDao.selectrekNasabahBancass1(akseptasi.getSpaj());
					Integer jn_lead = (Integer) jenis_bancass.get("JN_LEAD");
					
					String subject="[E-Lions] Rekening Polis Member Get Member a/n "+nama_pp+" (NO SPAJ "+FormatString.nomorSPAJ(akseptasi.getSpaj())+") Kosong";
					
					if(!Common.isEmpty(mspo_policy_no)){
						subject="[E-Lions] Rekening Polis Member Get Member a/n "+nama_pp+" (NO SPAJ "+FormatString.nomorSPAJ(akseptasi.getSpaj())+"/NO POLIS " +FormatString.nomorPolis(mspo_policy_no)+") Kosong";
					}
					HashMap mapEmail = uwDao.selectMstConfig(6, "prosesAkseptasi", "REKENING_POLIS_GET_MEMBER");
					String emailTo = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					String emailCc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					String bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
					String pesan =  
					"<table width=100% class=satu>"
						+ "<tr><td>Harap input rekening Bank Sinarmas </td></tr> <br><br>"
						+ "<tr><td>Rekening Polis Member Get Member  </td></tr><br>"
						+ "<tr><td>Pemegang Polis 	  </td><td>:</td><td>" + nama_pp+ "<td></tr>" 
						+ "<tr><td>Tertanggung		  </td><td>:</td><td>" + nama_tt + "<td></tr>"
						+ "<tr><td>No SPAJ"+(!Common.isEmpty(mspo_policy_no)?"No Polis":"")+"		  	  </td><td>:</td><td>"+FormatString.nomorSPAJ(akseptasi.getSpaj())+(!Common.isEmpty(mspo_policy_no)?"/"+FormatString.nomorPolis(mspo_policy_no):"")+"<td colspan=2></tr>" 
						+ "<tr><td>Nama Produk		  </td><td>:</td><td colspan=2>" +lsdbs_name+ "</td></tr>" 
						+ "<tr><td>Status Akseptasi	  </td><td>:</td><td>" +nama_akseptasi + "<td></tr>"
						+ "<tr><td>Program	  </td><td>:</td><td>" +(jn_lead==7?" MEMBER GET MEMBER":" REFERRAL") + "<td></tr>"
					+"</table>";
					
					try {
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, false, 
								props.getProperty("admin.ajsjava"), 
								new String[]{emailTo}, 
								new String[]{emailCc},
								new String[]{"deddy@sinarmasmsiglife.co.id"}, 
								subject, 
								"SPAJ No " + akseptasi.getSpaj() + " a/n " + pp + " telah diaksep oleh Dept.Underwriting", null, akseptasi.getSpaj());
						
					} catch (MailException e) {
						logger.error("ERROR :", e);
					}
				}
				
				//MANTA - SMILE LINK ULTIMATE
//				if(akseptasi.getLiAksep()==5 && lsbs_id==190 && lsdbs_number==9){
//					insertMstRefferal(akseptasi.getSpaj());
//				}
			}else if(akseptasi.getLiAksep().intValue()==8){//fund alocation
				int proUlink=0;
				if(akseptasi.isLbUlink()){
					updateMstInsuredStatus(akseptasi.getSpaj(),akseptasi.getInsuredNo(),akseptasi.getLiAksep(),null,null,null);
					insertMstPositionSpajWithSubId(akseptasi.getCurrentUser().getLus_id(), desc.toUpperCase(), akseptasi.getSpaj(), akseptasi.getSubliAksep());
					saveMstTransHistory(akseptasi.getSpaj(), "tgl_fund_allocation", null, null, null);
					if(!(akseptasi.getLsbsId()==164 && akseptasi.getLsdbsNumber()==11)){
//						insertMstPositionSpaj(akseptasi.getCurrentUser().getLus_id(), desc.toUpperCase(), akseptasi.getSpaj());
						//proses ulink khusus investimax
						if(akseptasi.getLsbsId()==165){
							proUlink=wf_proses_ulink_investimax(akseptasi.getSpaj(),akseptasi.getLsbsId(),akseptasi.getBegDate(),
									akseptasi.getPayPeriod(),akseptasi.getPremium(),akseptasi.getUmurPp(),akseptasi.getUmurTt(),
									akseptasi.getTsi(),akseptasi.getLkuId(),akseptasi.getCurrentUser().getLus_id());
						}else{
							proUlink=wf_proses_ulink(akseptasi.getSpaj(),akseptasi.getLsbsId(),akseptasi.getBegDate(),
									akseptasi.getPayPeriod(),akseptasi.getPremium(),akseptasi.getUmurPp(),akseptasi.getUmurTt(),
									akseptasi.getTsi(),akseptasi.getLkuId(),akseptasi.getCurrentUser().getLus_id());
						}
					}else{
						Date msl_trans_date=selectMinMstPositionSpajMspsDate(akseptasi.getSpaj());
						bacDao.updateTransDateSlink(akseptasi.getSpaj(), msl_trans_date);
					}
					if(proUlink>0){
						TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
						return proUlink;
					}
				}
			}
		}else if(akseptasi.getProses().equals("100")){//proses sample underwritting 1
			updateMstInsuredMedical(akseptasi.getSpaj(),akseptasi.getMedical());
		}else if(akseptasi.getProses().equals("101")){//proses sample underwritting 2
			updateLstSampleUw();
			updateMstInsuredMedical(akseptasi.getSpaj(),akseptasi.getMedical());
			insertMstSampleUw(akseptasi.getSpaj(),akseptasi.getMspoPolicyHolder(),
					akseptasi.getMsteInsured(),thn,bln);
		}
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	public int prosesAkseptasiPas(Akseptasi akseptasi,int thn,int bln,String desc, BindException err)throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		int liAktif=0;
		int setNopol = 0;
		
		if(akseptasi.getProses().equals("1")){
			
			if(akseptasi.getLiAksep()==5 || akseptasi.getLiAksep()==10)
				liAktif=1;
			
			//
			if(akseptasi.getNoPolis()==null) {
				setNopol=wf_set_nopol(akseptasi, 2);
			}
			
			if(akseptasi.getLiAksep()==5 || akseptasi.getLiAksep()==10){//akseptasi
				if(setNopol>0){//rollback dan tampilkan message
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					return setNopol;
				}
				
				updateMstInsuredStatus(akseptasi.getSpaj(),akseptasi.getInsuredNo(),
						akseptasi.getLiAksep(),liAktif,akseptasi.getCurrentUser().getLus_id(),new Integer(1));
				if(akseptasi.getLiAksep()==5) saveMstTransHistory(akseptasi.getSpaj(), "tgl_akseptasi_polis", null, null, null);
				if(akseptasi.getLiAksep()==10) saveMstTransHistory(akseptasi.getSpaj(), "tgl_special_accept", null, null, null);
				String va = financeDao.selectVirtualAccountSpaj(akseptasi.getSpaj());
				if (va==null){
				bacDao.updateVirtualAccountBySpaj(akseptasi.getSpaj());	}
			}
		}
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	/**@Fungsi:	Untuk melakukan proses akseptasi produk ASM 
	 * @param 	String spaj,String begDate,String endDate
	 * @author	Ferry Harlim
	 */
	public int prosesAkseptasiAsm(String spaj,String begDate,String endDate,User currentUser,String nopolis_asm){
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Akseptasi akseptasi=new Akseptasi();
		akseptasi.setSpaj(spaj);
		akseptasi.setCurrentUser(currentUser);
		Map data=selectPolicy_Agent_Product(spaj);
		akseptasi.setLcaId((String)data.get("LCA_ID"));
		akseptasi.setLsbsId((Integer)data.get("LSBS_ID"));
		akseptasi.setMsagId((String)data.get("MSAG_ID"));
		insertMstPositionSpaj(akseptasi.getCurrentUser().getLus_id(), "AKSEPTASI PRODUK ASM", akseptasi.getSpaj(), 0);

		int hasil=wf_set_nopol(akseptasi, 2);
		if(hasil!=0)
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		updateMstInsuredBegDateEndDate(spaj, begDate, endDate);
		updateMstProductInsuredBegDateEndDate(spaj, begDate, endDate);
		updateMstInsured(spaj, null, 5, 1, null);
		updateMstPolicyMspoNopolAsm(spaj, nopolis_asm);
		saveMstTransHistory(spaj, "tgl_akseptasi_polis", null, null, null);
		return hasil;
	}
	public void updateMstPolicyMspoNopolAsm(String spaj,String mspoNoPolAsm){
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("mspo_no_pol_asm", mspoNoPolAsm);
		update("updateMstPolicyMspoNopolAsm",map);
	}
	private int wf_proses_ulink(String spaj,Integer lsbsId,Date begDate,Integer payPeriod,
			Double premium,Integer umurPp,Integer UmurTt,Double tsi,String lkuId,String lusId) {
		int li_bisnis, i, li_trans = 5, li_lock = 0, k = 0;
		Integer liTopup;
		Double ld_det_jum, ld_persen_inv;
		Date ldtAksep=selectMinMstPositionSpajMspsDate(spaj);
//		ldtAksep = bacDao.cekHariKerjaMin(ldtAksep); 
		//tidak perlu dijaga kalau tgl transnya jatuh di jati libur. penjagaan ada di sistem billy
		
		Date tglTrans=ldtAksep;
		Integer muKe=new Integer(1);
		String ljiId;
		Double mduJumlah;
		//cek table ulink
		Double ldec_persen=selectSumMstDetUlinkPersen(spaj,muKe);
		if(ldec_persen==null)
			ldec_persen=new Double(0);
			
		if(ldec_persen.doubleValue()==0 || ldec_persen.doubleValue()!=100){
			//MessageBox('Alert', 'Produk Unit-Linked !!!~nAlokasi Investasi Belum Lengkap !!!')
			return 81;
		}
		ldec_persen=null;
		ldec_persen=selectCountMstBiayaUlink(spaj);
		if(ldec_persen==null)
			ldec_persen=new Double(0);
		if(ldec_persen==0){
			//MessageBox('Alert', 'Produk Unit-Linked !!!~nBiaya Alokasi Investasi Belum Lengkap !!!')
			return 82;
		}
		//
		if(lsbsId==87 || lsbsId==101)
			li_lock=1;
		List lsDetUlink=selectMstDetUlink(spaj,muKe,new Integer(1));
		
		for(int j=0;j<lsDetUlink.size();j++){
			DetUlink detUlink=(DetUlink)lsDetUlink.get(j);
			ld_persen_inv=detUlink.getMdu_persen();
			if(ld_persen_inv.doubleValue()>0){
				k++;
				ljiId=detUlink.getLji_id();
				mduJumlah=detUlink.getMdu_jumlah();
				insertMstTransUlink(null, spaj,new Integer(1),k,ldtAksep,new Integer(1),ljiId,"Alokasi Investasi",
						mduJumlah,lusId,li_lock,0.0,0.0,"D",0.0,new Integer(0),
						new Integer(42));
			}
		}
		
		//Cek ada top-up berkala?
		int l=0,n=0,o=0;
		List daftarTopUp= uwDao.selectMstUlinkTopupNewForDetBilling(spaj);
		updateMstUlink(spaj,ldtAksep,new Integer(42), 1);
		for(int index=0;index<daftarTopUp.size();index++){
			Map mapUlink =(HashMap)daftarTopUp.get(index);
			muKe=(Integer)mapUlink.get("MU_KE");
			li_trans=(Integer)mapUlink.get("LT_ID");
			lsDetUlink=selectMstDetUlink(spaj,muKe,new Integer(1));
			updateMstUlink(spaj,ldtAksep,new Integer(42), muKe);
							
			for(int j=0;j<lsDetUlink.size();j++){
				DetUlink detUlink=(DetUlink)lsDetUlink.get(j);
				ld_persen_inv=detUlink.getMdu_persen();
				if(ld_persen_inv>0){
					ljiId=detUlink.getLji_id();
					mduJumlah=detUlink.getMdu_jumlah();
					if(detUlink.getMu_ke()==2){
						l++;
						o=l;
					}else if(detUlink.getMu_ke()==3){
						n++;
						o=n;
					}	
					insertMstTransUlink(null, spaj,muKe,o,ldtAksep,new Integer(li_trans),ljiId,"Penambahan",
							mduJumlah,lusId,li_lock,0.0,0.0,"D",0.0,new Integer(0),
							new Integer(42));
				}
			}
		}	

		
		return 0;
	}
	
	/**Tidak ada proses NAB khusus investimax langsung update posisi ke
	 * lspd_id=46 PRINT KUSTODIAN TRANSAKSI 
	 * 
	 */
	private int wf_proses_ulink_investimax(String spaj,Integer lsbsId,Date begDate,Integer payPeriod,
			Double premium,Integer umurPp,Integer UmurTt,Double tsi,String lkuId,String lusId) {
		int li_bisnis, i, li_trans = 5, li_lock = 0, k = 0;
		Integer liTopup;
		Double ld_det_jum, ld_persen_inv;
		Date ldtAksep=selectMinMstPositionSpajMspsDate(spaj);
//		ldtAksep = bacDao.cekHariKerjaMin(ldtAksep);
		//tidak perlu dijaga kalau tgl transnya jatuh di jati libur. penjagaan ada di sistem billy

		
		Date tglTrans=ldtAksep;
		Integer muKe=new Integer(1);
		String ljiId;
		Integer lspdId=46;
		Double mtuNab=1000.0;
		Double mtuUnit=0.0,saldoUnit=0.0,mduJumlah;
		Double mduSaldoUnitPp=0.0;
		//cek table ulink
		Double ldec_persen=selectSumMstDetUlinkPersen(spaj,muKe);
		if(ldec_persen==null)
			ldec_persen=new Double(0);
			
		if(ldec_persen.doubleValue()==0 || ldec_persen.doubleValue()!=100){
			//MessageBox('Alert', 'Produk Unit-Linked !!!~nAlokasi Investasi Belum Lengkap !!!')
			return 81;
		}
		ldec_persen=null;
		ldec_persen=selectCountMstBiayaUlink(spaj);
		if(ldec_persen==null)
			ldec_persen=new Double(0);
		if(ldec_persen==0){
			//MessageBox('Alert', 'Produk Unit-Linked !!!~nBiaya Alokasi Investasi Belum Lengkap !!!')
			return 82;
		}
		//
		//if(lsbsId==87 || lsbsId==101)
		//	li_lock=1;
		
		//investimax cuma ada topup sekali
		Double saldoUnitTot=0.0;
		List lsDetUlink=selectMstDetUlink(spaj,muKe,new Integer(1));
		for(int j=0;j<lsDetUlink.size();j++){
			DetUlink detUlink=(DetUlink)lsDetUlink.get(j);
			//update eka.mst_det_ulink saldo unitnya
				saldoUnitTot+=(detUlink.getMdu_jumlah()/mtuNab);
		}
		lsDetUlink=selectMstDetUlink(spaj,2,new Integer(1));
		for(int j=0;j<lsDetUlink.size();j++){
			DetUlink detUlink=(DetUlink)lsDetUlink.get(j);
			//update eka.mst_det_ulink saldo unitnya
				saldoUnitTot+=(detUlink.getMdu_jumlah()/mtuNab);
		}
		
		lsDetUlink=selectMstDetUlink(spaj,muKe,new Integer(1));
		for(int j=0;j<lsDetUlink.size();j++){
			DetUlink detUlink=(DetUlink)lsDetUlink.get(j);
			ld_persen_inv=detUlink.getMdu_persen();
			//untuk mduSaldoUnitTu nol saja //rudi 03122007
			if(ld_persen_inv.doubleValue()>0){
				k++;
				ljiId=detUlink.getLji_id();
				mduJumlah=detUlink.getMdu_jumlah();
				mtuUnit=mduJumlah/mtuNab;
				saldoUnit=saldoUnit+mtuUnit;
				
				//DecimalFormat nf5=new DecimalFormat("0.00000");
				//premi pertama
				if(detUlink.getMu_ke()==1)
					mduSaldoUnitPp=mtuUnit;
				
				insertMstTransUlink(detUlink.getMdu_last_trans(), spaj,new Integer(1),k,ldtAksep,new Integer(1),ljiId,"Alokasi Investasi",
						mduJumlah,lusId,li_lock,mtuNab,mtuUnit,"D",saldoUnit,new Integer(0),
						lspdId);
				
			}
		}
		
		
		
		//Cek ada top-up berkala?
		int l=0,n=0,o=0;
		List daftarTopUp= uwDao.selectMstUlinkTopupNewForDetBilling(spaj);
		updateMstUlink(spaj,ldtAksep,lspdId, 1);
		for(int index=0;index<daftarTopUp.size();index++){
			Map mapUlink =(HashMap)daftarTopUp.get(index);
			muKe=(Integer)mapUlink.get("MU_KE");
			li_trans=(Integer)mapUlink.get("LT_ID");
			lsDetUlink=selectMstDetUlink(spaj,muKe,new Integer(1));
			updateMstUlink(spaj,ldtAksep,lspdId, muKe);
							
			for(int j=0;j<lsDetUlink.size();j++){
				DetUlink detUlink=(DetUlink)lsDetUlink.get(j);
				ld_persen_inv=detUlink.getMdu_persen();
				if(ld_persen_inv>0){
					ljiId=detUlink.getLji_id();
					mduJumlah=detUlink.getMdu_jumlah();
					mtuUnit=mduJumlah/mtuNab;
					saldoUnit=saldoUnit+mtuUnit;
					if(detUlink.getMu_ke()==2){
						l++;
						o=l;
					}else if(detUlink.getMu_ke()==3){
						n++;
						o=n;
					}	
					insertMstTransUlink(detUlink.getMdu_last_trans(), spaj,muKe,o,ldtAksep,new Integer(li_trans),ljiId,"Penambahan",
							mduJumlah,lusId,li_lock,mtuNab,mtuUnit,"D",saldoUnit,new Integer(0),
							lspdId);
				}
			}
		}	

		updateMstDetUlinkInvestimax(spaj, saldoUnitTot, mduSaldoUnitPp);
		
		return 0;
	}

	/**@Fungsi:	Untuk menggenerate Nopolis dengan format nopolis
	 * 			XX###YYYY@@@@@
	 * 			XX 		=Kode Cabang 	(09)
	 * 			###		=Kode Produk 	(159) 	
	 * 			YYYY	=Tahun 			(2006)
	 * 			@@@@@	=counter		(00001)
	 * fungsi ini bisa dijalankan di bac ataupun di akseptasi (Yusuf 14/12/2007) -> di BAC khusus polis inputan admin bank
	 * @param	Akseptasi akseptasi
	 * @return 	int
	 * @author 	Ferry Harlim
	 * */
	public int wf_set_nopol(Akseptasi akseptasi, int lspd_tujuan) {
		String lsNopol;
		String mspo_policy_no_manual = uwDao.selectPolicyNoFromSpajManualMstTempDMTM(akseptasi.getSpaj());
		if(mspo_policy_no_manual==null || mspo_policy_no_manual.equals("")){
			lsNopol = akseptasiDao.f_get_nopolis(akseptasi.getLcaId(),akseptasi.getLsbsId());
		}else{
			lsNopol = mspo_policy_no_manual;
		}

		if(lsNopol==null || lsNopol.length()<=0 ){
			return 51; //MESSAGEBOX('iNfo','No Polis is Null!!!')
		}
		
		String lsRegSpaj=selectMstPolicyRegSpaj(lsNopol);
		if(lsRegSpaj!=null){
			int i=lsNopol.length();
			int ldNo;
			
			if(akseptasi.getLcaId().equals("62")){
				ldNo=Integer.parseInt(lsNopol.substring(i-7,i));
			}else{
				ldNo=Integer.parseInt(lsNopol.substring(i-9,i));
			}
			
			String s_ld_no=f1.format(ldNo);
			
			if(ldNo>0){
				ldNo++;
				lsNopol = akseptasi.getLcaId()+ f3.format(akseptasi.getLsbsId())+ ldNo;
				
				lsRegSpaj = selectMstPolicyRegSpaj(lsNopol);
				if(lsRegSpaj!=null){
					return 52; //MessageBox( 'Pesan', 'Nomor Polis Kembar, Coba tranfer ulang...')
				}else{
					updateMstCntPolicy(akseptasi.getLsbsId(),akseptasi.getLcaId(),ldNo);
				}
			}
		}
		
		String lsNopolFormated=FormatString.nomorPolis(lsNopol);
		updateMstPolicyFormated(akseptasi.getSpaj(),lsNopol,lsNopolFormated);
		// update no blanko dan statusnya
		//update no blanko
		
		Integer liRekrutan=selectMstDetRekruter(akseptasi.getMsagId());
		if(liRekrutan!=null)
			if(liRekrutan>0)
				updateMstPolicyMspoPreExixting(akseptasi.getSpaj(),new Integer(1), lspd_tujuan,new Integer(1));
		
		akseptasi.setNoPolis(lsNopol);
		
		return 0;
	}

	public List selectLstSampleUwTglSample(){
		return query("select.lst_sample_uw.tgl_sample",null);
	}
	
	public Integer selectCountLstSampleUw(){
		return (Integer)querySingle("select.lst_sample_uw.count",null);
	}
	
	public Integer selectCountMstSampleUw(){
		return (Integer)querySingle("select.mst_sample_uw.count",null);
	}
	
	public Integer countsummaryinput(String id, String tgl1, String tgl2){
		Map param = new HashMap();
		param.put("id", id);
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		return (Integer)querySingle("countsummaryinput",param);
	}
	
	public Integer countsummaryinputguthrie( String tgl1, String tgl2){
		Map param = new HashMap();
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		return (Integer)querySingle("countsummaryinputguthrie",param);
	}
	
	public List selectDSampleUwRegion(int thn,int bln,String lcaId){
		Map param =new HashMap();
		param.put("li_thn",new Integer(thn));
		param.put("li_bln",new Integer(bln));
		param.put("txtlca_idtt",lcaId);
		return query("select.d_sample_uw_region",param);
	}
	
	public void updateLstSampleUw(){
		update("update.lst_sample_uw",null);
	}
	
	public void insertMstSampleUw(String spaj,String policyHolder,String msteInsured,int thn,int bln){
		Map param =new HashMap();
 		param.put("spaj",spaj);
 		param.put("thn",new Integer(thn));
 		param.put("bln",new Integer(bln));
 		param.put("msteInsured",msteInsured);
 		param.put("policyHolder",policyHolder);
 		getSqlMapClientTemplate().insert("elions.uw.insert.mst_sample_uw",param);
	}
	
	public String selectMstPolicyRegSpaj(String lsNopol){
		return (String) querySingle("select.mst_policy_reg_spaj",lsNopol);
	}
	
	public void updateMstCntPolicy(Integer lsbsId,String lcaId,int ldNo){
		Map up=new HashMap();
		up.put("as_bisnis",lsbsId);
		up.put("as_cab",lcaId);
		up.put("ld_no",new Integer(ldNo));
		update("update.mst_cnt_polis",up);
	}
	
	public void updateMstPolicyFormated(String spaj,String lsNopol,String lsNopolFormated){
		Map up=new HashMap();
		up.put("txtnospaj",spaj);
		up.put("ls_nopol",lsNopol);
		up.put("ls_nopol_formated",lsNopolFormated);
		update("update.mst_policy.nopolis",up);
	}
	
	public Integer selectMstDetRekruter(String msagId){
		return (Integer)querySingle("select.mst_det_rekruter",msagId);
	}
	
	public void updateMstPolicyMspoPreExixting(String spaj,Integer value,Integer lspdId,Integer lstbId){
		Map map=new HashMap();
		map.put("txtnospaj",spaj);
		map.put("value",value);
		map.put("lspdId",lspdId);
		map.put("lstbId",lstbId);
		update("update.mst_policy.mspo_preexisting",map);
	}
	
	public Date selectMinMstPositionSpajMspsDate(String spaj){
		return (Date) querySingle("select.mst_position_spaj.msps_date",spaj);
	}
	/*cek power save dan ulink*/
	/**Fungsi:	Untuk menampilkan jumlah data/record yang terdapat pada EKA.MST_POWERSAVE_PROSES
	 * @param 	String spaj
	 * @return	Long
	 * @author	Ferry Harlim
	 */
	public Long selectCountMstPowerSave(String spaj){
		return(Long)getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_powersave_proses.count",spaj);
	}
	
	public Long selectCountMstPowerSaveBaru(String spaj){
		return(Long)getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_powersavebaru.count",spaj);
	}
	
	public ParameterClass selectMstPowersaveDpDate(String spaj){
		return(ParameterClass) getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_powersave_proses_dp_date",spaj);
	}
	/**Fungsi:	Untuk Menampilkan jumlah data atau record pada table EKA.MST_POWERSAVE_RO
	 * @param 	String spaj
	 * @return	Long
	 * @author	Ferry Harlim
	 */
	public Long selectCountMstPowerSaveRo(String spaj){
		return(Long)getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_powersave_ro.count",spaj);

	}

	public Double selectSumMstDetUlinkPersen(String spaj,Integer muKe){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("ar_ke",muKe);
		return (Double) getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_det_ulink.persen",param);
	}
	/**Fungsi:	Untuk menampilkan jumlah biaya pada tabel EKA.MST_BIAYA_ULINK
	 * @param 	String spaj
	 * @return	Double
	 * @author	Ferry Harlim
	 */
	public Double selectCountMstBiayaUlink(String spaj){
		return(Double) getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_biaya_ulink.count",spaj);
	}

	public void insertMstTransUlink(Date mtu_tgl_nab, String spaj,Integer arKe,int k,Date ldtAksep,Integer ltId,String ljiId,
			String desc,Double ld_det_jum,String lusId,int li_lock,Double mtuNab,Double mtuUnit,String mtuDk,
			Double saldoUnit,Integer flagPrint,Integer lspdId){
		Map insert=new HashMap();
		insert.put("txtnospaj",spaj);
		insert.put("ar_ke",arKe);
		insert.put("k",new Integer(k));
		insert.put("ldt_aksep",ldtAksep);
		insert.put("lt_id",ltId);
		insert.put("lji_id",ljiId);
		insert.put("mtu_desc",desc);
		insert.put("ld_det_jum",ld_det_jum);
		insert.put("mtu_nab",mtuNab);
		insert.put("mtu_unit",mtuUnit);
		insert.put("mtu_dk",mtuDk);
		insert.put("saldo_unit",saldoUnit);
		insert.put("gl_lus_id",lusId);
		insert.put("mtu_flag_print",flagPrint);
		insert.put("lspd_id",lspdId);
		insert.put("li_lock",new Integer(li_lock));
		insert.put("mtu_tgl_nab", mtu_tgl_nab);
		getSqlMapClientTemplate().insert("elions.uw.insert.mst_trans_ulink",insert);
	}
	
	public Integer selectMstUlinksTopup(String spaj,Integer arKe){
		Map topup=new HashMap();
		topup.put("txtnospaj",spaj);
		topup.put("ar_ke",arKe);
		return (Integer) querySingle("select.mst_ulinks_topup",topup);
	}
	
	public void updateMstUlink(String spaj,Date ldtAksep,Integer lspdId, Integer muKe){
		Map update=new HashMap();
		update.put("reg_spaj",spaj);
		update.put("mu_tgl_trans",ldtAksep);
		update.put("lspd_id",lspdId);
		update.put("mu_ke",muKe);
	 	update("akseptasi.update.mst_ulink",update);
	}
	
	public Client selectAllClientNew(String spaj,int flag){
		if(flag==1)//tertanggung
			return (Client)querySingle("selectAllClientNewTertanggung",spaj);
		else //pemegang
			return (Client)querySingle("selectAllClientNewPemegang",spaj);
	}
	
	public Integer selectGetPasIdFromFireId(String msp_fire_id){
		return (Integer) querySingle("selectGetPasIdFromFireId",msp_fire_id);
	}
	
	public List<Map> selectStatusPasList(String msp_id, String reg_id){
		Map params=new HashMap();
		params.put("msp_id",msp_id);
		params.put("reg_id",reg_id);
		return query("selectStatusPasList",params);
	}
	
	public Integer countregbponline(){
		return (Integer) querySingle("countregbponline",null);
	}
	
	public List<Pas> selectAllPasList(String msp_id, String lspd_id, String tipe, String kata, String pilter, String jenis, String jenisDist, String jenisQuery, Integer lus_admin, String lus_id, Map param){
		
		if("LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < (replace(upper(trim('"+kata+"')),'.') ";
		else if("LE".equals(pilter))
			kata = " <= replace(upper(trim('"+kata+"')),'.') ";
		else if("EQ".equals(pilter))
			kata = " = replace(upper(trim('"+kata+"')),'.') ";
		else if("GE".equals(pilter))
			kata = " >= replace(upper(trim('"+kata+"')),'.') ";
		else if("GT".equals(pilter))
			kata = " > replace(upper(trim('"+kata+"')),'.') ";
		
		Map params=new HashMap();
		params.put("msp_id",msp_id);
		params.put("lspd_id",lspd_id);
		params.put("jenisDist",jenisDist);
		params.put("tipe",tipe);
		params.put("kata",kata);
		params.put("pilter",pilter);
		params.put("jenis",jenis);
		params.put("jenisQuery",jenisQuery);
		params.put("lus_admin",lus_admin);
		params.put("lus_id",lus_id);
		if (param!=null)
		{
			params.putAll(param);
		}
		return query("selectAllPasList",params);
	}
	
	public List<Pas> selectPasBySpaj(String reg_spaj, Map param){
		Map params=new HashMap();
		params.put("reg_spaj",reg_spaj);
		if (param!=null)
		{
			params.putAll(param);
		}
		return query("selectPasBySpaj", params);
	}	
	
	public String selectTotalAllPasList(String msp_id, String lspd_id, String tipe, String kata, String pilter, String jenis, String jenisDist, String jenisQuery, Integer lus_admin, String lus_id, Map param){
		
		if("LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < (replace(upper(trim('"+kata+"')),'.') ";
		else if("LE".equals(pilter))
			kata = " <= replace(upper(trim('"+kata+"')),'.') ";
		else if("EQ".equals(pilter))
			kata = " = replace(upper(trim('"+kata+"')),'.') ";
		else if("GE".equals(pilter))
			kata = " >= replace(upper(trim('"+kata+"')),'.') ";
		else if("GT".equals(pilter))
			kata = " > replace(upper(trim('"+kata+"')),'.') ";
		
		Map params=new HashMap();
		params.put("msp_id",msp_id);
		params.put("lspd_id",lspd_id);
		params.put("jenisDist",jenisDist);
		params.put("tipe",tipe);
		params.put("kata",kata);
		params.put("pilter",pilter);
		params.put("jenis",jenis);
		params.put("jenisQuery",jenisQuery);
		params.put("lus_admin",lus_admin);
		params.put("lus_id",lus_id);
		if (param!=null)
		{
			params.putAll(param);
		}
		return (String) querySingle("selectTotalAllPasList",params);
	}
	
	public String prosesofacscreening(String s_spaj,int type, int lus_id) {
		HashMap map = new HashMap();
		String result = "";
		map.put("lus_id", lus_id);
		map.put("spaj", s_spaj);
		map.put("type",type);
		map.put("result", result);
		
		getSqlMapClientTemplate().queryForObject("elions.uw.prosesofacscreening", map); 
		result = (String) map.get("result");
		
		return result;
		
		//return (String) querySingle("prosesAkseptasiSpeedy", map);
		//update("prosesAkseptasiSpeedy", map);
	}
	
	public Pemegang selectHolder(String spaj){

		return (Pemegang)querySingle("selectHolder", spaj);
	}
	
	public Pemegang selectHolderSertifikat(String spaj){

		return (Pemegang)querySingle("selectHolderSertifikat", spaj);
	}
	
	public Tertanggung selectTertanggungDetail(String spaj){

		return (Tertanggung)querySingle("selectTertanggungDetail", spaj);
	}
	
	public Tertanggung selectTertanggungDetailSertifikat(String spaj){

		return (Tertanggung)querySingle("selectTertanggungDetailSertifikat", spaj);
	}
	
	public Comment selectComment(String spaj){
		return (Comment)querySingle("selectComment", spaj);
	}
	
	public void insertComment(Integer add_num, Integer mofs_id, String spaj, String type, String comment, String nama) throws DataAccessException {		
		Map map = new HashMap();
		map.put("mofs_id", mofs_id);
		map.put("add_num", add_num);
		map.put("spaj", spaj);
		map.put("type", type);	
		map.put("comment", comment);
		map.put("nama", nama);
		insert("insertComment", map);	
	}
	
	public void ApproveMstPolicy(String reg_spaj, String lus_id){
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("lus_id",lus_id);
		update("approveMstPolicy",map);
	}
	
	public void ApproveRejectSertifikat(String reg_spaj, Integer mofs_status, String  mofs_status_message, String lus_id){
		Map map = new HashMap();
		map.put("mofs_status",mofs_status);
		map.put("mofs_status_message",mofs_status_message);
		map.put("reg_spaj",reg_spaj);
		map.put("lus_id",lus_id);
		update("ApproveRejectSertifikat",map);
	}
	
	public void ApproveRejectProcessed(String reg_spaj, String lus_id, Integer lspd_id){
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("lus_id",lus_id);
		map.put("lspd_id",lspd_id);
		update("ApproveRejectProcessed",map);
	}
	
	
	public void updateMstTransUlink(String reg_spaj, Integer mu_ke, Integer mtu_ke, String lus_id){
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("mu_ke",mu_ke);
		map.put("mtu_ke",mtu_ke);
		map.put("lus_id",lus_id);
		update("updateMstTransUlink",map);
	}
	
	public void updateMsUlink(String reg_spaj, String lus_id){
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("lus_id",lus_id);
		update("updateMsUlink",map);
	}
	
	public void RejectMstPolicy(String reg_spaj, String lus_id){
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("lus_id",lus_id);
		update("rejectMstPolicy",map);
	}
	
	public void ApproveMstInsured(String reg_spaj, String lus_id){
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("lus_id",lus_id);
		update("approveMstInsured",map);
	}
	
	public void RejectMstInsured(String reg_spaj, String lus_id){
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("lus_id",lus_id);
		update("rejectMstInsured",map);
	}
	
	public List<Benefeciary> selectBenefeciary(String spaj){

		Map params=new HashMap();
		params.put("spaj", spaj);
		
		return query("selectBenefeciary",params);
	}
	
	public List<Benefeciary> selectBenefeciarySertifikat(String spaj){

		Map params=new HashMap();
		params.put("spaj", spaj);
		
		return query("selectBenefeciarySertifikat",params);
	}
	
	public List<PesertaPlus> selectPeserta(String spaj){

		Map params=new HashMap();
		params.put("spaj", spaj);
		
		return query("selectPeserta",params);
	}
	
	public List<PesertaPlus> selectPesertaSertifikat(String spaj){

		Map params=new HashMap();
		params.put("spaj", spaj);
		
		return query("selectPesertaSertifikat",params);
	}
	
	public String selectKodeAgentFromMstKusioner(String fire_id){
		return (String) querySingle("selectKodeAgentFromMstKusioner", fire_id);
	}
	
	public List<Tmms> selectFreePaTmmsList(String tmms_id, String msag_id, String tipe, String kata, String pilter, Map param){
		
		if("LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < (replace(upper(trim('"+kata+"')),'.') ";
		else if("LE".equals(pilter))
			kata = " <= replace(upper(trim('"+kata+"')),'.') ";
		else if("EQ".equals(pilter))
			kata = " = replace(upper(trim('"+kata+"')),'.') ";
		else if("GE".equals(pilter))
			kata = " >= replace(upper(trim('"+kata+"')),'.') ";
		else if("GT".equals(pilter))
			kata = " > replace(upper(trim('"+kata+"')),'.') ";
		
		Map params=new HashMap();
		params.put("tmms_id",tmms_id);
		params.put("msag_id",msag_id);
		params.put("tipe",tipe);
		params.put("kata",kata);
		params.put("pilter",pilter);
		if (param!=null)
		{
			params.putAll(param);
		}
		return query("selectFreePaTmmsList",params);
	}
	
	public String selectTotalFreePaTmmsList(String tmms_id, String msag_id, String tipe, String kata, String pilter){
		
		if("LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < (replace(upper(trim('"+kata+"')),'.') ";
		else if("LE".equals(pilter))
			kata = " <= replace(upper(trim('"+kata+"')),'.') ";
		else if("EQ".equals(pilter))
			kata = " = replace(upper(trim('"+kata+"')),'.') ";
		else if("GE".equals(pilter))
			kata = " >= replace(upper(trim('"+kata+"')),'.') ";
		else if("GT".equals(pilter))
			kata = " > replace(upper(trim('"+kata+"')),'.') ";
		
		Map params=new HashMap();
		params.put("tmms_id",tmms_id);
		params.put("msag_id",msag_id);
		params.put("tipe",tipe);
		params.put("kata",kata);
		params.put("pilter",pilter);
		return (String) querySingle("selectTotalFreePaTmmsList",params);
	}
	
	public List<Pas> selectViewerPasList(String msp_id, String lspd_id, String tipe, String kata, String pilter, String jenis){
		
		if("LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < (replace(upper(trim('"+kata+"')),'.') ";
		else if("LE".equals(pilter))
			kata = " <= replace(upper(trim('"+kata+"')),'.') ";
		else if("EQ".equals(pilter))
			kata = " = replace(upper(trim('"+kata+"')),'.') ";
		else if("GE".equals(pilter))
			kata = " >= replace(upper(trim('"+kata+"')),'.') ";
		else if("GT".equals(pilter))
			kata = " > replace(upper(trim('"+kata+"')),'.') ";
		
		Map params=new HashMap();
		params.put("msp_id",msp_id);
		params.put("lspd_id",lspd_id);
		
		params.put("tipe",tipe);
		params.put("kata",kata);
		params.put("pilter",pilter);
		params.put("jenis",jenis);		
		return query("selectViewerPasList",params);
	}
	
	public List selectReportPas(){
		Map params=new HashMap();
		return query("selectReportPas",null);
	}
	
	public List selectReportPasList(){
		Map params=new HashMap();
		return query("selectReportPasList",null);
	}
	
	public List selectReportEmailStatusPabsm(){
		Map params=new HashMap();
		return query("selectReportEmailStatusPabsm",null);
	}
	
	public List selectReportBatalPasList(){
		Map params=new HashMap();
		return query("selectReportBatalPasList",null);
	}
	
	public List selectReportPenerimaanDanamasPrimaList(){
		Map params=new HashMap();
		return query("selectReportPenerimaanDanamasPrimaList",null);
	}
	
	public List selectReportKlaimDanamasPrimaList(){
		Map params=new HashMap();
		return query("selectReportKlaimDanamasPrimaList",null);
	}
	
	public List selectReportManfDanamasPrimaList(){
		Map params=new HashMap();
		return query("selectReportManfDanamasPrimaList",null);
	}
	
	public List selectFurtherRequirementPas(int lssa_id, boolean isEmailRequired, String yearbefore,String month1,String month2,String month3,String month4,String month5,String month6,String month7,String month8,String month9,String month10,String month11,String month12){
		Map params = new HashMap();
		String type = null;
		if(lssa_id == 1){
			type = "ES:";
		}else if(lssa_id == 2){
			type = "DC:";
		}else if(lssa_id == 3){
			type = "FR:";
		}else if(lssa_id == 4){
			type = "EP:";
		}else if(lssa_id == 5){
			type = "AC:";
		}else if(lssa_id == 8){
			type = "FA:";
		}else if(lssa_id == 9){
			type = "PP:";
		}else if(lssa_id == 10){
			type = "AK:";
		}
//		else if(lssa_id == 12){
//			type = "AS:";
//		}
		else{
			type = "";
		}
		
		params.put("lssa_id", lssa_id);
		params.put("type", type);
		params.put("yearbefore", yearbefore);
		params.put("month1", month1);
		params.put("month2", month2);
		params.put("month3", month3);
		params.put("month4", month4);
		params.put("month5", month5);
		params.put("month6", month6);
		params.put("month7", month7);
		params.put("month8", month8);
		params.put("month9", month9);
		params.put("month10", month10);
		params.put("month11", month11);
		params.put("month12", month12);
		if(isEmailRequired) params.put("isEmailRequired", "true");
		return query("selectFurtherRequirementPas",params);
	}
	
	public Pas selectPasFromMspId(String msp_id){
		Map params=new HashMap();
		params.put("msp_id", msp_id);
		return (Pas) querySingle("selectPasFromMspId",params);
	}
	
	public Pas selectBpFromMspId(String msp_id){
		Map params=new HashMap();
		params.put("msp_id", msp_id);
		return (Pas) querySingle("selectBpFromMspId",params);
	}
	
	public List selectReportPesertaPasList(){
		Map params=new HashMap();
		return query("selectReportPesertaPasList",null);
	}
	
	public List selectReportAksepPasList(){
		Map params=new HashMap();
		return query("selectReportAksepPasList",null);
	}
	
	public List selectReportPasEmailList(int lus_id){
		Map params=new HashMap();
		return query("selectReportPasEmailList",lus_id);
	}
	
	public String selectPasEmailAdminCabang(String no_kartu){
		Map params=new HashMap();
		return (String) querySingle("selectPasEmailAdminCabang",no_kartu);
	}
	
	public String selectLusFullName(BigDecimal lus_id){
		Map params=new HashMap();
		return (String) querySingle("selectLusFullName",lus_id);
	}
	
	public List selectReportFire(){
		Map params=new HashMap();
		return query("selectReportFire",null);
	}
	
	public List selectReportFireList(){
		Map params=new HashMap();
		return query("selectReportFireList",null);
	}
	
	public List selectReportBlackList(){
		Map params=new HashMap();
		return query("selectReportBlackList",null);
	}
	
	public List selectFrPasBp(){
		Map params=new HashMap();
		return query("selectFrPasBp",null);
	}
	
	public List selectFrDbd(String jenis_pas){
		Map params=new HashMap();
		params.put("jenis_pas", jenis_pas);
		return query("selectFrDbd",params);
	}
	
	public List selectFrMallAssurance(){
		//Map params=new HashMap();
		return query("selectFrMallAssurance",null);
		//return null;//query("selectFrMallAssurance",null);
	}
	
	public Map selectBackupReas(String spaj){
		return (Map) querySingle("selectBackupReas",spaj);
	}
	
	public String selectCekPin(String pin, Integer flag_active){
		Map map = new HashMap();
		map.put("pin", pin);
		map.put("flag_active", flag_active);
		return (String) querySingle("selectCekPin", map);
	}
	
	public String selectCekKartuPas(String no, Integer flag_active){
		Map map = new HashMap();
		map.put("no", no);
		map.put("flag_active", flag_active);
		return (String) querySingle("selectCekKartuPas", map);
	}
	
	public Double selectCekPremiHcp(String nomor, Integer umur){
		Map map = new HashMap();
		map.put("bisnis", 195);
		map.put("nomor", nomor);
		map.put("umur", umur);
		return (Double) querySingle("selectPremiTahunan", map);
	}
	
	public String selectCekNoKartu(String no_kartu, String produk, Integer flag_active){
		Map map = new HashMap();
		map.put("no_kartu", no_kartu);
		map.put("produk", produk);
		map.put("flag_active", flag_active);
		return (String) querySingle("selectCekNoKartu", map);
	}
	
	public Integer selectCountMstKuesionerPelayanan(){
		return (Integer) querySingle("selectCountMstKuesionerPelayanan", null);
	}
	
	public KuesionerPelayananByQuestion selectPercentageQuestionnaireByQuestion(){
		return (KuesionerPelayananByQuestion) querySingle("selectPercentageQuestionnaireByQuestion",null);
	}

	public KuesionerPelayananByGroup selectPercentageQuestionnaireByGroup(){
		return (KuesionerPelayananByGroup) querySingle("selectPercentageQuestionnaireByGroup",null);
	}
	
	public KuesionerPelayananAll selectPercentageQuestionnaireAll(){
		return (KuesionerPelayananAll) querySingle("selectPercentageQuestionnaireAll",null);
	}
	
	public List<Map> selectDetailAgen(String msag_id){
		return query("selectDetailAgen",msag_id);
	}
	
	public void updatePasNoRegister(Map params){
		update("updatePasNoRegister",params);
	}
	
	public void updateAgentCodePasFromRegister() throws DataAccessException{
		update("updateAgentCodePasFromRegister", null);
	}
	
	public void updatePas(Pas pas){
		update("updatePas",pas);
	}
	
	public void updateNoKartuMstPasSms( Map params ) throws DataAccessException {
		update("updateNoKartuMstPasSms",params);
	}
	
	public void updateNasabahAccMstPolicy( Map params ) throws DataAccessException {
		update("updateNasabahAccMstPolicy",params);
	}
	
	public void updateNoBlankoMstPolicy( Map params ) throws DataAccessException {
		update("updateNoBlankoMstPolicy",params);
	}
	
	public void insertPas(Pas pas){
		insert("insertPas",pas);
	}
	
	public void updateRegPas(Pas pas){
		update("updateRegPas",pas);
	}
	
	public void insertCplan(Cplan cplan){
		insert("insertCplan",cplan);
	}
	
	public void insertCplanDet(CplanDet cplanDet){
		insert("insertCplanDet",cplanDet);
	}
	
	public Map selectCplan(String reg_spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);		
		return (Map) querySingle("selectCplan", params);
	}
	
	public void updateCplan(Cplan cplan){
		update("updateCplan",cplan);
	}
	
	public void updateCplanDet(CplanDet cplanDet){
		update("updateCplanDet",cplanDet);
	}
	
	public boolean emailSoftcopyPaBsm(Cplan cplan, User user){
		return softcopy.softCopyOtomatisPaBsm(cplan.getReg_spaj(), user, cplan.getEmail(), cplan );
	}
	
	public List<String> selectLcChecklistWoMedis(){
		return query("selectLcChecklistWoMedis",null);
	}
	
	public void updateBukuPas(String pin, Integer flag_active){
		Map map = new HashMap();
		map.put("pin", pin);
		map.put("flag_active", flag_active);
		update("updateBukuPas",map);
	}
	
	public void updateKartuPas1(String no_kartu, Integer flag_active, String reg_spaj, Date tgl_kirim){
		Map map = new HashMap();
		map.put("no_kartu", no_kartu);
		map.put("flag_active", flag_active);
		map.put("reg_spaj", reg_spaj);
		map.put("tgl_kirim", tgl_kirim);
		update("updateKartuPas1",map);
	}
	
//	public void updateKartuPas1(String no_kartu, Integer flag_active, String reg_spaj, Date tgl_kirim){
//		Map map = new HashMap();
//		map.put("no_kartu", no_kartu);
//		map.put("flag_active", flag_active);
//		map.put("reg_spaj", reg_spaj);
//		map.put("tgl_kirim", tgl_kirim);
//		update("updateKartuPas1",map);
//	}
	
	public void updateKartuPas2(String no_kartu, Integer flag_active, String reg_spaj){
		Map map = new HashMap();
		map.put("no_kartu", no_kartu);
		map.put("flag_active", flag_active);
		map.put("reg_spaj", reg_spaj);
		update("updateKartuPas2",map);
	}
	
	public void insertFire(Pas pas){
		insert("insertFire",pas);
	}
	
    public void insertMstHistoryUT( Map params ) throws DataAccessException {
		insert("insertMstHistoryUT", params );
	}
    
    public void insertMstCompanyWs( String mcl_id, Date tgl_invoice, Date tgl_bayar, BigDecimal jumlah_invoice, BigDecimal jumlah_bayar,
    		String periode, BigDecimal nomor, String lku_id ) throws DataAccessException {
    	Map map = new HashMap();
    	map.put("mcl_id", mcl_id);
    	map.put("tgl_invoice", tgl_invoice);
    	map.put("tgl_bayar", tgl_bayar);
    	map.put("jumlah_invoice", jumlah_invoice);
    	map.put("jumlah_bayar", jumlah_bayar);
    	map.put("periode", periode);
    	map.put("nomor", nomor);
    	map.put("lku_id", lku_id);
		insert("insertMstCompanyWs", map );
	}
    
    public void insertMstInbox( MstInbox mstInbox ){
    	insert("insertMstInbox", mstInbox);
    }
    
    public void updateMstInbox (MstInbox mstinbox) {
    	update("updateMstInbox", mstinbox);
    }
    
    public void insertMstInboxHist( MstInboxHist mstInboxHist ){
    	String mi_id = mstInboxHist.getMi_id();
    	mstInboxHist.setCreate_date(selectSysdateByInboxHist(mi_id));
    	insert("insertMstInboxHist", mstInboxHist);
    }          
    
    public void insertMstJobList( MstInboxChecklist mstInboxChecklist ){
    	insert("insertMstJobList", mstInboxChecklist);
    }
    
    public void insertMstInboxChecklist( MstInboxChecklist mstInboxChecklist ){
    	insert("insertMstInboxChecklist", mstInboxChecklist);
    }
    
	public Client selectAllClientBlacklist(String mcl_id){
		return (Client)querySingle("selectAllClientBlacklist",mcl_id);
	}
	
	public String selectCekBlacklist(String reg_spaj){
		return (String) querySingle("selectCekBlacklist",reg_spaj);
	}
	
	public String selectCekSpajSebelumSurrender(String reg_spaj){
		return (String) querySingle("selectCekSpajSebelumSurrender", reg_spaj);
	}
	
	public String selectCekBlacklistDirect(String lbl_nama, String lbl_tgl_lahir){
		Map param=new HashMap();
		param.put("lbl_nama",lbl_nama);
		param.put("lbl_tgl_lahir",lbl_tgl_lahir);
		return (String) querySingle("selectCekBlacklistDirect",param);
	}
	
	public BlackList selectAllBlacklist(String lbl_nama, Date lbl_tgl_lahir, String mcl_id){
		Map param=new HashMap();
		if("".equals(mcl_id))mcl_id = null;
		param.put("lbl_nama",lbl_nama);
		param.put("lbl_tgl_lahir",lbl_tgl_lahir);
		param.put("mcl_id",mcl_id);
		return (BlackList)querySingle("selectAllBlacklist",param);
	}
	
	public List selectAllDetBlacklist(String lbl_id){
		return query("selectAllDetBlacklist",lbl_id);
	}
	
	public List selectAllBlacklistFamily(int lbl_id){
		return query("selectAllBlacklistFamily",lbl_id);
	}
	
	public AddressNew selectAllAddressNew(String mcl_id) {
		return (AddressNew) querySingle("selectAllAddressNew", mcl_id);
	}
	
	public AddressNew selectAllAddressNew(String spaj,int flag){
		if(flag==1)//tertanggung
			return (AddressNew)querySingle("selectAllAddressNewTertanggung",spaj);
		else //pemegang
			return (AddressNew)querySingle("selectAllAddressNewPemegang",spaj);
	}
	
	public List selectAllLstAgama(){
		return query("selectAllLstAgama",null);
	}
	
	public List selectAllLstTolak(){
		return query("selectAllLstTolak",null);
	}
	
	public List selectAllLstEducation(){
		return query("selectAllLstEducation",null);
	}
		
	public List selectAllLstGrpJob(){
		return query("selectAllLstGrpJob",null);
	}
		
	public List selectAllLstJabatan(){
		return query("selectAllLstJabatan",null);
	}
		
	public List selectAllLstNegara(){
		return query("selectAllLstNegara",null);
	}
	/**@Fungsi:	Untuk menampilkan jumlah data dari tabel EKA.MST_MEDICAL
	 * @param	String spaj,Integer insuredNo
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 * */
	public Integer selectCountMstMedical(String spaj,Integer insuredNo){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mste_insured_no",insuredNo);
		return (Integer)querySingle("transUw.select.Count.mst_det_medical",param);
	}
	/**@Fungsi:	Untuk menampilkan data dari tabel EKA.MST_PRODUCT_INSURED PA=800
	 * @param	String spaj,Integer insruedNo,Integer Pa
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 * */
	public Integer selectMstProductInsuredPa(String spaj,Integer insruedNo,Integer Pa){
		//pa==800
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("lsbs_id",Pa);
		return (Integer)querySingle("transUw.select.Count.mst_product_insured",param);
	
	}
	
	public Integer selectMstProductInsuredMsprClass(String spaj,Integer insured,Integer lsbsId){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mste_insured_no",insured);
		param.put("lsbs_id",lsbsId);
		return (Integer)querySingle("transuw.select.mst_product_insured.mspr_class",param);

	}
	
	public void prosesDecline(String spaj,String msteInsured,Integer insuredNo, String lusId, Integer lspdId,Integer lsspId,Integer lssaId,Integer lstbId,String desc){
		updateMstInsured(spaj,lspdId,lssaId,insuredNo, null);
		updateMstPolicyPosition(spaj,lspdId,lstbId);
		insertMstPositionSpaj(lusId, desc, spaj, 0);
		updateMstClientNewMclBlackList(msteInsured,new Integer(1));
		updateMstPolicyLspdId(spaj,new Integer(8),lstbId);
	}
	
	/**@Fungsi: Untuk Mengupdate kolom LSPD_ID (posisi Polis) 
	 * 			pada Table EKA.MST_POLICY
	 * @param	String spaj,Integer lspdId,Integer lstbId
	 * @author 	Ferry Harlim
	 **/
	public void updateMstPolicyPosition(String spaj,Integer lspdId,Integer lstbId){
		Map param=new HashMap();
		param.put("lspd_id",lspdId);
		param.put("lstb_id",lstbId);
		param.put("reg_spaj",spaj);
		update("update.transUw.mst_policy_position",param);
	}
	
	public void updateMstPolicyLspdId(String spaj,Integer lspdId,Integer lstbId){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("lstb_id",lstbId); //individu =1
		param.put("lspd_id",lspdId);
		update("update.transUw.mst_policy_status",param);

	}
	
	public void updateMstClientNewMclBlackList(String mclId,Integer mclBlacklist){
		Map param=new HashMap();
		param.put("mcl_blacklist",mclBlacklist);
		param.put("mcl_id",mclId);
		update("update.transuw.mst_client.mcl_blacklist",param);
	
	}
	
	public Integer selectCountMstProductInsuredCekStandard(String spaj){
		return (Integer)querySingle("transUw.select.Count.mst_product_insured2",spaj);
		
	}
	/**@Fungsi:	Untuk Menampilkan Jumlah data yang terdapat pada tabel EKA.MST_CANCEL
	 * @param	String spaj
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 * */
	public Integer selectCountMstCancel(String spaj){
		return (Integer)querySingle("transUw.select.Count.mstCancel",spaj);
		
	}
	/**@Fungsi:	Untuk menampilkan jumlah data yang terdapat pada tabel EKA.MST_TRANS_ULINK
	 * @param	String spaj, Integer ltId//jenis transaksi  klo null  maka gak di filter
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 * */
	public Integer selectCountMstTransUlink(String spaj,Integer ltId){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("lt_id",ltId);
		return (Integer)querySingle("select.count.mst_trans_ulink",param);
	}
	
	/**@Fungsi:	Untuk menampilkan jumlah data yang terdapat pada tabel EKA.MST_SLINK berdasarkan msl_posisi
	 * @param	String spaj, Integer lspd_id
	 * @return 	Integer
	 * @author 	Deddy
	 * */
	public Integer selectCountMstSlinkBasedPosition(String spaj, Integer lspd_id){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("lspd_id",lspd_id);
		return (Integer)querySingle("select.count.mst_slinkBasedPosition",param);
	}

	/**@Fungsi: Untuk menampilkan data untuk di insert ke database DPLK, tabel DPLK.DPLKWORKSITE
	 * @param	String spaj
	 * @return 	Map
	 * @author 	Ferry Harlim
	 * */
	public Map selectPersentaseDplk(String spaj) {
		return (Map) querySingle("selectPersentaseDplk", spaj);
	}
	/*Untuk Proses Insert ke table reins dan reins_product
	 * */
	public int prosesTransferPembayaranReasUlang(Transfer transfer,BindException err) throws NoTransactionException, DataAccessException, ParseException{
		String spaj,lusId,lkuId;
		Integer insuredNo,umurTt,liReas,medical,insPeriod;
		Integer lsbsId,lsdbsNumber,lstbId,lspdId,lssaId,liLama;
		Date begdate,endDate;
		List lsDp;
		int liPosisi;
		Policy policy;
		User currentUser;
		spaj=transfer.getSpaj();
		insuredNo=transfer.getInsuredNo();
		lusId=transfer.getCurrentUser().getLus_id();
		lsbsId=transfer.getLsbsId();
		lsdbsNumber=transfer.getLsdbsNumber();
		lstbId=transfer.getLstbId();
		lspdId=transfer.getLspdId();
		lssaId=transfer.getLiAksep();
		currentUser=transfer.getCurrentUser();
		liLama=transfer.getLiLama();
		lkuId=transfer.getLkuId();
		umurTt=transfer.getUmurTt();
		liReas=transfer.getLiReas();
		begdate=transfer.getBegDate();
		endDate=transfer.getEndDate();
		medical=transfer.getMedical();
		insPeriod=transfer.getInsPeriod();
		lsDp=transfer.getLsDp();
		liPosisi=transfer.getLiPosisi();
		policy=transfer.getPolicy();
		
		int proses=0;
		
		boolean lbLangsung=false;
		//wf_set_posisi
		//updateMstInsured(spaj,new Integer(liPosisi),lssaId,insuredNo);
		//insertMstPositionSpaj(spaj,lusId,liPosisi,10,lssaId,"TRANSFER DARI U/W");
		//updateMstPolicyPosition(spaj,new Integer(liPosisi),lstbId);
		//wf_save_reins()
		if(wf_save_reins(spaj,insuredNo,umurTt,liReas,lkuId,begdate,endDate,medical,lusId,insPeriod,err)){
			
		}else
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return proses;	
	}
	/**@Fungsi:	Untuk mengupdate kolom LSSP_ID (Status Polis) pada tabel EKA.MST_POLICY
	 * @param	String spaj, Integer lsspId
	 * @author 	Ferry Harlim
	 * */
	public void updateMstPolicyLsspdId(String spaj, Integer lsspId) {
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("lsspId",lsspId);
		update("update.mst_policy.lsspId",map);
	}
	/**@Fungsi:	Untuk mengalokasikan dana yang akan di reas, jika dalam hal ini polis tersebut mempunyai type reas treaty,
	 * 			dan jika tipe reasnya non reas, maka fungsi ini tidak dijalankan.
	 * @param	String spaj,Integer insured,Integer li_ins_age, Integer liReas,String lkuId,
	 * 			Date begDate,Date endDate,Integer medical,String lusId,Integer insPeriod,BindException err
	 * @return 	boolean
	 * @author 	Ferry Harlim 
	 * @throws ParseException 
	 * @throws DataAccessException 
	 * */
	public boolean wf_save_reins(String spaj,Integer insured,Integer li_ins_age, Integer liReas,String lkuId,Date begDate,Date endDate,Integer medical,String lusId,Integer insPeriod,BindException err) throws DataAccessException, ParseException{
	//	TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		List  list1, list2, list3,list4,list5;
		Date ldtProdDate, ldtBegDate, ldtEndDate, ldtLastPrmDate, ldtNextPrmDate,ldtBegDate1,ldtEndDate1,ldtLapse;
		Double ldec_ext_mort[] = {new Double(0),new Double(0),new Double(0)};
		Double ldec_ext_risk[] = {new Double(0),new Double(0),new Double(0)};
		Double ldecSimultan, ldecTsi, ldecSar, ldecReas;
		Integer li_bisnis[][]=new Integer[9][3];
		int li_hit_premi, li_last_policy_age, li_next_policy_age;
		Double li_th_ke;
		int li_cnt[]=new int[3];
		int li_bisnis_id,li_bisnis_NO;  
		Integer li_pos_id,li_lssp,li_contyear,li_pay,li_pemegang,li_pa_class,li_reas_client;
		String ls_prod[]=new String[9];
		String ls_kurs = null, ls_pa_risk;
		Integer li_type_reas;
		int ll_row[]=new int[5];
		int ll_hasil;
		int lsbs_id1,lsbs_id2;
		boolean lb_ret=true;
		
		Double totalReas = (Double) querySingle("selectTotalReas", spaj);
		if(totalReas == null) return false;
		else if(totalReas.doubleValue()==0) return true;		
		
		//
		if(liReas==0)
			return lb_ret;
		//
		Integer lsbsIdTemp;
		list2=selectd_ds_sarn(spaj,insured);
		for(int i=0;i<list2.size();i++){
			ParameterClass sarTemp=(ParameterClass) list2.get(i);
			lsbsIdTemp=sarTemp.getLsbs_id();
			if(lsbsIdTemp<600){//plan pokok
				li_bisnis[1][1]=sarTemp.getLsbs_id();
				li_bisnis[1][2]=sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==803){//term rider
				li_bisnis[2][1] = sarTemp.getLsbs_id();
				li_bisnis[2][2] = sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==800){// pa rider
				li_bisnis[4][1] = sarTemp.getLsbs_id();
				li_bisnis[4][2] = sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==801){//pk rider
				li_bisnis[6][1] = sarTemp.getLsbs_id();
				li_bisnis[6][2] = sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==806){//cash plan
				li_bisnis[7][1] = sarTemp.getLsbs_id();
				li_bisnis[7][2] = sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==807){//cash plan
				li_bisnis[8][1] = sarTemp.getLsbs_id();
				li_bisnis[8][2] = sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==900){//extra mortality
				ldec_ext_mort[1]=sarTemp.getMspr_extra();
			}else if(lsbsIdTemp==901){//extra job/risk
				ldec_ext_risk[1]=sarTemp.getMspr_extra();
			}
		}
		//
		lsbsIdTemp=null;
		list3=selectd_ds_rider_include(li_bisnis[1][1],li_bisnis[1][2],lkuId);
		for(int i=0;i<list3.size();i++){
			ParameterClass riderInclude=(ParameterClass) list3.get(i);
			lsbsIdTemp=riderInclude.getLst_lsbs_id();
			if(lsbsIdTemp==600){//pa include
				li_bisnis[3][1]=riderInclude.getLst_lsbs_id();
				li_bisnis[3][2]=riderInclude.getLst_lsdbs_number();
			}else if(lsbsIdTemp==601){			// pk include
				li_bisnis[5][1] = riderInclude.getLst_lsbs_id();
				li_bisnis[5][2] = riderInclude.getLst_lsdbs_number();
			}else if(lsbsIdTemp==806){			// cash plan
				li_bisnis[7][1] = riderInclude.getLst_lsbs_id();
				li_bisnis[7][2] = riderInclude.getLst_lsdbs_number();
			}else if(lsbsIdTemp==807 || lsbsIdTemp==808){			// TPD
				li_bisnis[8][1] = riderInclude.getLst_lsbs_id();
				li_bisnis[8][2] = riderInclude.getLst_lsdbs_number();
			}else {
				err.reject("","Please Contact EDP Dept !', 'Aplikasi belum dimodifikasi utk Plan ini !!!");
				return false;
			}
		}
		//
		if(lb_ret){
			if(liReas== 2)
				li_type_reas = liReas;
			else
				li_type_reas=selectLstBisnisLstrId(li_bisnis[1][1]);
			//
			if(li_type_reas==null){
				lb_ret=false;
				err.reject("","Empty Reas Type (Error)");
			}
			//
			ldtProdDate=selectMstDefaultMsdefdate(new Integer(1));
			if(ldtProdDate==null){
				err.reject("","Production Date Not Found (Error)");
				return false;
			}
			//
			//String today=defaultDateFormat.format(akseptasiDao.selectSysdate());
			String today=defaultDateFormat.format(commonDao.selectSysdateTrunc());
			li_th_ke=selectTahunKe(defaultDateFormat.format(begDate),today);
			li_last_policy_age = li_th_ke.intValue() - 1;
			li_next_policy_age = li_th_ke.intValue();
			//
			//ganti tanggal produksi meeting tanggal 14-09-2007 (pak herman) updatenya di proses produksi 'ucup'
			if (li_th_ke > 1){
				ldtLastPrmDate=null;
				ldtNextPrmDate=null;
				//
				ldtLastPrmDate=selectAddMonthsWfSaveReins(defaultDateFormat.format(begDate),li_last_policy_age);
				ldtNextPrmDate=selectAddMonthsWfSaveReins(defaultDateFormat.format(begDate),li_next_policy_age);
				if(ldtLastPrmDate==null || ldtNextPrmDate==null){
					err.reject("","Empty Last/Next Premium Date ");
					return false;
				}
			}else{
				ldtLastPrmDate  = ldtProdDate;
				ldtNextPrmDate  = ldtProdDate;
			}
			//checking batal
			
			Map policy=(HashMap)selectMstPolicy(spaj,new Integer(1));
			li_lssp=(Integer)policy.get("LSSP_ID");
			li_pos_id=(Integer)policy.get("LSPD_ID");
			li_contyear=(Integer)policy.get("MSPO_INS_PERIOD");
			li_pay=(Integer)policy.get("LSCB_ID");
			//
			if (li_lssp == 13 && li_pos_id == 95)
				li_pos_id = new Integer(100);
			else
				li_pos_id = new Integer(99);	
			//tambahan baru kolom jenis 28/09/2006 (Misbach)
			Integer jenis=selectMReasTempTipe(spaj);
						
			//insert mst_reins
			insertMstReins(spaj,insured,li_type_reas,li_bisnis[1][1],li_bisnis[1][2],li_last_policy_age,li_next_policy_age,
					ldtLastPrmDate,ldtNextPrmDate,medical,new Integer (1),new Integer(0),li_pos_id,lusId,jenis);
			//
			lsbsIdTemp=li_bisnis[1][1];
			if(lsbsIdTemp==43||lsbsIdTemp==53||lsbsIdTemp==54||lsbsIdTemp==67)
          		ls_prod[1] = "SSH";
			else if(lsbsIdTemp==45 ||lsbsIdTemp==130)
          		ls_prod[1] = "SSP";
          	else
          		ls_prod[1] = "LIFE";
          	
          	//
          	ls_prod[2] = "TR_RD";
          	ls_prod[3] = "PA_IN";
          	ls_prod[4] = "PA_RD";
          	ls_prod[5] = "PK_IN";
          	ls_prod[6] = "PK_RD";
          	ls_prod[7] = "CASH";
          	ls_prod[8] = "TPD";
/*        	ls_prod[9] = "PA_R";
          	ls_prod[10]= "HCP";*/
          	//
          	list1=selectMReasTemp(spaj);
          	for(li_cnt[1]=0;li_cnt[1]<list1.size();li_cnt[1]++){
          		Map reasTemp=(HashMap)list1.get(li_cnt[1]);
          		li_pemegang=(Integer)reasTemp.get("PEMEGANG");
				for(li_cnt[2]=1;li_cnt[2]<=8;li_cnt[2]++){////6  1=life, 2=tr_rider, 3=pa_include, 4=pa_rider, 5=pk_include, 6=pk_rider , 7=cash, 8=tpd
          			ldecSimultan = (Double) reasTemp.get("SIMULTAN_" + ls_prod[li_cnt[2]] );
					ldecTsi      = (Double) reasTemp.get("TSI_"      + ls_prod[li_cnt[2]] );
					ldecSar      = (Double) reasTemp.get("SAR_"      + ls_prod[li_cnt[2]] );
					ldecReas     = (Double) reasTemp.get("REAS_"     + ls_prod[li_cnt[2]] );
//					hitung contrak year
					if(lsbsIdTemp==51 || lsbsIdTemp==61){
						li_contyear = new Integer(59 - li_ins_age);
						if (li_bisnis[3][1]==600|| li_bisnis[5][1]==601 )
							li_contyear = new Integer(10);
					}else if(lsbsIdTemp==52 || lsbsIdTemp==62||lsbsIdTemp==78){
						li_contyear = new Integer(59 - li_ins_age);
						if(li_bisnis[3][1]==600|| li_bisnis[5][1]== 601) 
							li_contyear = new Integer(10);
						if (lkuId.equals("02")){
							if(li_bisnis[1][1]==new Integer(52))
								li_contyear = new Integer(60 - li_ins_age);
							else if( li_bisnis[3][1].compareTo(new Integer(600))==0)
								li_contyear = new Integer(65 - li_ins_age);
							
						}
					}else if(lsbsIdTemp==47 ||lsbsIdTemp==50||lsbsIdTemp==55||lsbsIdTemp==58){
						li_contyear=new Integer(5);
						if(li_bisnis[3][1]==600)
							li_contyear=new Integer(5);
					}else if(lsbsIdTemp==56 ||lsbsIdTemp==64||lsbsIdTemp==68||lsbsIdTemp==75){
						li_contyear=new Integer(4);
						if(li_bisnis[3][1]==600)
							li_contyear=new Integer(8);
					}else if(lsbsIdTemp==65){
						li_contyear=new Integer(79-li_ins_age);
						if(li_bisnis[3][1]==600 || li_bisnis[8][1]==807 || li_bisnis[8][1]==808)
							li_contyear = new Integer(59 - li_ins_age);
						else if (li_bisnis[5][1] == 601 )
							li_contyear = new Integer(59 - li_ins_age);
							if (li_contyear > 20)
								li_contyear = new Integer(0);
						else if (li_bisnis[7][1] == 806)
							li_contyear = new Integer(5); 
						
					}else if(lsbsIdTemp==66){
						li_contyear = new Integer(55 - li_ins_age);
						if (li_bisnis[3][1] == 600) 
							li_contyear = new Integer(55 - li_ins_age);
						else if(li_bisnis[7][1] == 806 ){
							li_contyear = new Integer(10);
							if (li_contyear + li_ins_age > 55 )
								li_contyear = new Integer(55 - li_ins_age);
						}
					}else if(lsbsIdTemp==69){
						if (li_bisnis[1][2] == 1){
							li_contyear = new Integer(5);
							if (li_bisnis[3][1] == 600)
								li_contyear = new Integer(8);
						}else if (li_bisnis[1][2] == 2){
							li_contyear = new Integer(7);
							if (li_bisnis[3][1] == 600)
								li_contyear = new Integer(10);
						}else{
							li_contyear = new Integer(9);
							if(li_bisnis[3][1] == 600)
								li_contyear = new Integer(12);
						}
					}else if(lsbsIdTemp== 74 || lsbsIdTemp==76){
						li_contyear = new Integer(5);
						if (li_bisnis[3][1] == 600 )
							li_contyear = new Integer(8);
					}else if(lsbsIdTemp==77 ||lsbsIdTemp== 84||
							lsbsIdTemp==100||lsbsIdTemp==102){
						li_contyear = new Integer(6);
						if (li_bisnis[3][1]== 600)
							li_contyear = new Integer(18);
					}else{
						li_contyear = insPeriod;
					} 
					//
					if(ldecReas.doubleValue() > 0){
						li_pa_class=null;
						li_pa_class=selectGetPaClass(spaj,insured,li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2]);
						if(li_pa_class==null){ 
								err.reject("","Empty PA Class, check uw Dao selectGetPaClass");
								return false;
						}
						if(li_pa_class!=null)
							if(li_pa_class==0)
								li_pa_class=null;
						ls_pa_risk=null;
						ls_pa_risk=selectGetPaRisk(spaj,insured,li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2]);
						if(ls_pa_risk==null){
							err.reject("","Empty PA Risk, CHECK Uw dao selectGetPaRisk");
							return false;
						}
						if(ls_pa_risk!=null)
						if(ls_pa_risk.equals(""))
							ls_pa_risk=null;
						//
						ldtBegDate=null;
						ldtEndDate=null;
						ldtBegDate1=null;
						ldtEndDate1=null;
						if(li_bisnis[li_cnt[2]][1] < 800 ){
							ls_kurs=lkuId;
							ldtBegDate=selectBegDateReinsProduct(defaultDateFormat.format(begDate),li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2],ls_kurs);
							if(ldtBegDate==null){
								err.reject("","Get End-Date Is Not Working Properly (a) ");
								return false;
							}
							ldtEndDate=selectEndDateReinsProduct(ldtBegDate,endDate,li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2],ls_kurs);
							if(ldtEndDate==null){
								err.reject("","Get End-Date Is Not Working Properly (b) ");
								return false;
							}
						}else{
							Map c=(HashMap)selectMstProductInsuredDateNKurs(spaj,insured,li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2]);
							if(c!=null){
								ls_kurs=(String)c.get("LKU_ID");
								ldtBegDate=(Date)c.get("MSPR_BEG_DATE");
								ldtEndDate=(Date)c.get("MSPR_END_DATE");
							}
							//
							if( li_bisnis[li_cnt[2]][1] >= 900 )
								ls_kurs = lkuId;
						}
						//
						if(ls_kurs==null){
							err.reject("","Empty Value of SPAJ Nospaj="+spaj+" lsbsId="+li_bisnis[li_cnt[2]][1]+" Kurs ="+ls_kurs+" Ins Beg Date= "+
									defaultDateFormat.format(ldtBegDate)+" Ins End Date="+defaultDateFormat.format(ldtEndDate)+" Check Eka.mst_product_insured");
							return false;
						}else{
							if(ldtBegDate==null || ldtEndDate==null){
								Map c=(HashMap)selectMstProductInsuredDateNKurs(spaj,insured,li_bisnis[1][1],li_bisnis[1][2]);
								ls_kurs=(String)c.get("LKU_ID");
								ldtBegDate1=(Date)c.get("MSPR_BEG_DATE");
								ldtEndDate1=(Date)c.get("MSPR_END_DATE");
								if(ldtBegDate1==null || ldtEndDate1==null){
									
									err.reject("","Empty Value of SPAJ Nospaj="+spaj+" lsbsId="+li_bisnis[li_cnt[2]][1]+" Kurs ="+ls_kurs+" Ins Beg Date= "+
											defaultDateFormat.format(ldtBegDate)+" Ins End Date="+defaultDateFormat.format(ldtEndDate)+" Check Eka.mst_product_insured");
									return false;
								}else{
									ldtBegDate= ldtBegDate1;
									ldtEndDate= ldtEndDate1;
								}
							}
						}
						//
						li_reas_client=selectLstDetBisnisLsdbsReasClient(li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2]);
						if(li_reas_client==null){
							err.reject("","Empty Jenis Client Reas (Error)");
							return false;
						}
						//
						ldec_ext_mort[2] = new Double(0);
						ldec_ext_risk[2] = new Double(0);
						//
						if(li_bisnis[li_cnt[2]][1]==69)
							li_pemegang = new Integer(1);
						//
						if( (li_reas_client==1 && li_pemegang==0 ) || ( li_reas_client >= 2 && li_pemegang==1)  ) {
							li_hit_premi = 1;
							if (li_cnt[2]== 1 || li_cnt[2] == 2){
								ldec_ext_mort[2] = ldec_ext_mort[1];
								ldec_ext_risk[2] = ldec_ext_risk[1];
							}
						}else if (li_bisnis[li_cnt[2]][1] < 900 ){
							li_hit_premi = 1;
						}else
							li_hit_premi = 0;
						//
						if(ldec_ext_mort[1]==null) 
							ldec_ext_mort[1] = new Double(0);
						//
						if(ldec_ext_mort[2]==null) 
							ldec_ext_mort[2] = new Double(0);
						//
						if(li_bisnis[li_cnt[2]][1] == 45 ||li_bisnis[li_cnt[2]][1] == 130)
							ls_pa_risk = "ABD";
						Double ldecRetensi=new Double(ldecSar.doubleValue() - ldecReas.doubleValue() );
//						if(logger.isDebugEnabled())logger.debug("ldet end_date ="+defaultDateFormat.format(ldtEndDate));
						insertMstReinsProduct(spaj,insured,li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2], (li_cnt[1]+1),
								ls_kurs,ldecSimultan,ldecTsi,ldecSar,ldecRetensi,ldecReas,li_pa_class,ls_pa_risk,ldec_ext_mort[2],
								ldec_ext_risk[2],ldtBegDate,ldtEndDate,li_pemegang,li_hit_premi,li_contyear);
				
					}	
			
				}
			}
		}
		return lb_ret;
	}
	
	public boolean wf_save_reinsNew(String spaj,Integer insured,Integer li_ins_age, Integer liReas,String lkuId,Date begDate,Date endDate,Integer medical,String lusId,Integer insPeriod,BindException err)throws DataAccessException, ParseException{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		List  list1, list2, list3,list4,list5;
		Date ldtProdDate, ldtBegDate, ldtEndDate, ldtLastPrmDate, ldtNextPrmDate,ldtBegDate1,ldtEndDate1,ldtLapse;
		Double ldec_ext_mort[] = {new Double(0),new Double(0),new Double(0)};
		Double ldec_ext_risk[] = {new Double(0),new Double(0),new Double(0)};
		Double ldecSimultan, ldecTsi, ldecSar, ldecReas;
		Integer li_bisnis[][]=new Integer[9][3];
		int li_hit_premi, li_last_policy_age, li_next_policy_age;
		int li_th_ke;
		int li_cnt[]=new int[3];
		int li_bisnis_id,li_bisnis_NO;  
		Integer li_pos_id,li_lssp,li_contyear,li_pay,li_pemegang,li_pa_class,li_reas_client;
		String ls_prod[]=new String[9];
		String ls_kurs = null, ls_pa_risk;
		Integer li_type_reas;
		int ll_row[]=new int[5];
		int ll_hasil;
		int lsbs_id1,lsbs_id2;
		boolean lb_ret=true;
		boolean typeReasBasic=false;
		//
		
		Double totalReas = (Double) querySingle("selectTotalReas", spaj);
//		totalReas=new Double(0);
		if(selectBusinessId(spaj).equals("161")) return true; //SSE mah gak perlu di reas kan
		else if(totalReas == null) return false;
		else if(totalReas.doubleValue()==0) return true;		
		
		if(liReas==0)
			return lb_ret;
		//
		Integer lsbsIdTemp;
		list2=selectd_ds_sarn(spaj,insured);
		for(int i=0;i<list2.size();i++){
			ParameterClass sarTemp=(ParameterClass) list2.get(i);
			lsbsIdTemp=sarTemp.getLsbs_id();
			if(lsbsIdTemp<600){//plan pokok
				li_bisnis[1][1]=sarTemp.getLsbs_id();
				li_bisnis[1][2]=sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==803){//term rider
				li_bisnis[2][1] = sarTemp.getLsbs_id();
				li_bisnis[2][2] = sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==800){// pa rider
				li_bisnis[4][1] = sarTemp.getLsbs_id();
				li_bisnis[4][2] = sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==801){//pk rider
				li_bisnis[6][1] = sarTemp.getLsbs_id();
				li_bisnis[6][2] = sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==806){//cash plan
				li_bisnis[7][1] = sarTemp.getLsbs_id();
				li_bisnis[7][2] = sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==807){//cash plan
				li_bisnis[8][1] = sarTemp.getLsbs_id();
				li_bisnis[8][2] = sarTemp.getLsdbs_number();
			}else if(lsbsIdTemp==900){//extra mortality
				ldec_ext_mort[1]=sarTemp.getMspr_extra();
			}else if(lsbsIdTemp==901){//extra job/risk
				ldec_ext_risk[1]=sarTemp.getMspr_extra();
			}
		}
		//
		lsbsIdTemp=null;
		list3=selectd_ds_rider_include(li_bisnis[1][1],li_bisnis[1][2],lkuId);
		for(int i=0;i<list3.size();i++){
			ParameterClass riderInclude=(ParameterClass) list3.get(i);
			lsbsIdTemp=riderInclude.getLst_lsbs_id();
			if(lsbsIdTemp==600){//pa include
				li_bisnis[3][1]=riderInclude.getLst_lsbs_id();
				li_bisnis[3][2]=riderInclude.getLst_lsdbs_number();
			}else if(lsbsIdTemp==601){			// pk include
				li_bisnis[5][1] = riderInclude.getLst_lsbs_id();
				li_bisnis[5][2] = riderInclude.getLst_lsdbs_number();
			}else if(lsbsIdTemp==806){			// cash plan
				li_bisnis[7][1] = riderInclude.getLst_lsbs_id();
				li_bisnis[7][2] = riderInclude.getLst_lsdbs_number();
			}else if(lsbsIdTemp==807 || lsbsIdTemp==808){			// TPD
				li_bisnis[8][1] = riderInclude.getLst_lsbs_id();
				li_bisnis[8][2] = riderInclude.getLst_lsdbs_number();
			}else {
				err.reject("","Please Contact EDP Dept !', 'Aplikasi belum dimodifikasi utk Plan ini !!!");
				return false;
			}
		}
		//
		if(lb_ret){
			if(liReas== 2)
				li_type_reas = liReas;
			else
				li_type_reas=selectLstBisnisLstrId(li_bisnis[1][1]);
			//
			if(li_type_reas==null){
				lb_ret=false;
				err.reject("","Empty Reas Type (Error)");
			}
			//
			ldtProdDate=selectMstDefaultMsdefdate(new Integer(1));
			if(ldtProdDate==null){
				err.reject("","Production Date Not Found (Error)");
				return false;
			}
			//
			//String today=defaultDateFormat.format(akseptasiDao.selectSysdate());
			String today=defaultDateFormat.format(commonDao.selectSysdateTrunc());
			li_th_ke=selectTahunKe(defaultDateFormat.format(begDate),today).intValue();
			li_last_policy_age = li_th_ke - 1;
			li_next_policy_age = li_th_ke;
			//
			if (li_th_ke > 1){
				ldtLastPrmDate=null;
				ldtNextPrmDate=null;
				//
				ldtLastPrmDate=selectAddMonthsWfSaveReins(defaultDateFormat.format(begDate),li_last_policy_age);
				ldtNextPrmDate=selectAddMonthsWfSaveReins(defaultDateFormat.format(begDate),li_next_policy_age);
				if(ldtLastPrmDate==null || ldtNextPrmDate==null){
					err.reject("","Empty Last/Next Premium Date ");
					return false;
				}
			}else{
				ldtLastPrmDate  = ldtProdDate;
				ldtNextPrmDate  = ldtProdDate;
			}
			//checking batal
			
			Map policy=(HashMap)selectMstPolicy(spaj,new Integer(1));
			li_lssp=(Integer)policy.get("LSSP_ID");
			li_pos_id=(Integer)policy.get("LSPD_ID");
			li_contyear=(Integer)policy.get("MSPO_INS_PERIOD");
			li_pay=(Integer)policy.get("LSCB_ID");
			//
			if (li_lssp == 13 && li_pos_id == 95)
				li_pos_id = new Integer(100);
			else
				li_pos_id = new Integer(99);	
			//tambahan baru kolom jenis 28/09/2006 (Misbach)
			Integer jenis=selectMReasTempTipe(spaj);
			//insert mst_reins yang pertama
			Integer countreins= selectMstReins(spaj);
			if(countreins>0){				
			}else{
			insertMstReins(spaj,insured,li_type_reas,li_bisnis[1][1],li_bisnis[1][2],li_last_policy_age,li_next_policy_age,
					ldtLastPrmDate,ldtNextPrmDate,medical,new Integer (1),new Integer(0),li_pos_id,lusId,jenis);
			
			}
			lsbsIdTemp=li_bisnis[1][1];
			Integer lsgrId=selectLstbisnisLsgrId(lsbsIdTemp);
			
			//if(lsbsIdTemp==43||lsbsIdTemp==53||lsbsIdTemp==54||lsbsIdTemp==67)
          	//	ls_prod[1] = "SSH";
			//else if(lsbsIdTemp==45)
          	//	ls_prod[1] = "SSP";
          	//else
          	//	ls_prod[1] = "LIFE";
			
			if(lsgrId==1)//life
				ls_prod[1] = "LIFE";
			else if(lsgrId==2)//PA
				ls_prod[1] = "SSP";
			else if(lsgrId==3)//health
				ls_prod[1] = "SSH";
          	//
          	ls_prod[2] = "TR_RD";
          	ls_prod[3] = "PA_IN";
          	ls_prod[4] = "PA_RD";
          	ls_prod[5] = "PK_IN";
          	ls_prod[6] = "PK_RD";
          	ls_prod[7] = "CASH";
          	ls_prod[8] = "TPD";
/*        	ls_prod[9] = "PA_R";
          	ls_prod[10]= "HCP";*/
          	//
          	list1=selectMReasTemp(spaj);
          	for(li_cnt[1]=0;li_cnt[1]<list1.size();li_cnt[1]++){
          		Map reasTemp=(HashMap)list1.get(li_cnt[1]);
          		li_pemegang=(Integer)reasTemp.get("PEMEGANG");
				for(li_cnt[2]=1;li_cnt[2]<=8;li_cnt[2]++){////6  1=life, 2=tr_rider, 3=pa_include, 4=pa_rider, 5=pk_include, 6=pk_rider , 7=cash, 8=tpd
          			ldecSimultan = (Double) reasTemp.get("SIMULTAN_" + ls_prod[li_cnt[2]] );
					ldecTsi      = (Double) reasTemp.get("TSI_"      + ls_prod[li_cnt[2]] );
					ldecSar      = (Double) reasTemp.get("SAR_"      + ls_prod[li_cnt[2]] );
					ldecReas     = (Double) reasTemp.get("REAS_"     + ls_prod[li_cnt[2]] );
//					hitung contrak year
					if(lsbsIdTemp==51 || lsbsIdTemp==61){
						li_contyear = new Integer(59 - li_ins_age);
						if (li_bisnis[3][1]==600|| li_bisnis[5][1]==601 )
							li_contyear = new Integer(10);
					}else if(lsbsIdTemp==52 || lsbsIdTemp==62||lsbsIdTemp==78){
						li_contyear = new Integer(59 - li_ins_age);
						if(li_bisnis[3][1]==600|| li_bisnis[5][1]== 601) 
							li_contyear = new Integer(10);
						if (lkuId.equals("02")){
							if(li_bisnis[1][1]==new Integer(52))
								li_contyear = new Integer(60 - li_ins_age);
							else if( li_bisnis[3][1].compareTo(new Integer(600))==0)
								li_contyear = new Integer(65 - li_ins_age);
							
						}
					}else if(lsbsIdTemp==47 ||lsbsIdTemp==50||lsbsIdTemp==55||lsbsIdTemp==58){
						li_contyear=new Integer(5);
						if(li_bisnis[3][1]==600)
							li_contyear=new Integer(5);
					}else if(lsbsIdTemp==56 ||lsbsIdTemp==64||lsbsIdTemp==68||lsbsIdTemp==75){
						li_contyear=new Integer(4);
						if(li_bisnis[3][1]==600)
							li_contyear=new Integer(8);
					}else if(lsbsIdTemp==65){
						li_contyear=new Integer(79-li_ins_age);
						if(li_bisnis[3][1]==600 || li_bisnis[8][1]==807 || li_bisnis[8][1]==808)
							li_contyear = new Integer(59 - li_ins_age);
						else if (li_bisnis[5][1] == 601 )
							li_contyear = new Integer(59 - li_ins_age);
							if (li_contyear > 20)
								li_contyear = new Integer(0);
						else if (li_bisnis[7][1] == 806)
							li_contyear = new Integer(5); 
						
					}else if(lsbsIdTemp==66){
						li_contyear = new Integer(55 - li_ins_age);
						if (li_bisnis[3][1] == 600) 
							li_contyear = new Integer(55 - li_ins_age);
						else if(li_bisnis[7][1] == 806 ){
							li_contyear = new Integer(10);
							if (li_contyear + li_ins_age > 55 )
								li_contyear = new Integer(55 - li_ins_age);
						}
					}else if(lsbsIdTemp==69){
						if (li_bisnis[1][2] == 1){
							li_contyear = new Integer(5);
							if (li_bisnis[3][1] == 600)
								li_contyear = new Integer(8);
						}else if (li_bisnis[1][2] == 2){
							li_contyear = new Integer(7);
							if (li_bisnis[3][1] == 600)
								li_contyear = new Integer(10);
						}else{
							li_contyear = new Integer(9);
							if(li_bisnis[3][1] == 600)
								li_contyear = new Integer(12);
						}
					}else if(lsbsIdTemp== 74 || lsbsIdTemp==76){
						li_contyear = new Integer(5);
						if (li_bisnis[3][1] == 600 )
							li_contyear = new Integer(8);
					}else if(lsbsIdTemp==77 ||lsbsIdTemp== 84||
							lsbsIdTemp==100||lsbsIdTemp==102){
						li_contyear = new Integer(6);
						if (li_bisnis[3][1]== 600)
							li_contyear = new Integer(18);
					}else{
						li_contyear = insPeriod;
					} 
					//
					if((li_bisnis[li_cnt[2]][1] != null) && (ldecReas.doubleValue() > 0 || li_bisnis[li_cnt[2]][1] < 300)){
						li_pa_class=null;
						li_pa_class=selectGetPaClass(spaj,insured,li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2]);
						if(li_pa_class==null){ 
								err.reject("","Empty PA Class, check uw Dao selectGetPaClass");
								return false;
						}
						if(li_pa_class!=null)
							if(li_pa_class==0)
								li_pa_class=null;
						ls_pa_risk=null;
						ls_pa_risk=selectGetPaRisk(spaj,insured,li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2]);
						if(ls_pa_risk==null){
							err.reject("","Empty PA Risk, CHECK Uw dao selectGetPaRisk");
							return false;
						}
						if(ls_pa_risk!=null)
						if(ls_pa_risk.equals(""))
							ls_pa_risk=null;
						//
						ldtBegDate=null;
						ldtEndDate=null;
						ldtBegDate1=null;
						ldtEndDate1=null;
						if(li_bisnis[li_cnt[2]][1] < 800 ){
							ls_kurs=lkuId;
							ldtBegDate=selectBegDateReinsProduct(defaultDateFormat.format(begDate),li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2],ls_kurs);
							if(ldtBegDate==null){
								err.reject("","Get End-Date Is Not Working Properly (c)");
								return false;
							}
							ldtEndDate=selectEndDateReinsProduct(ldtBegDate,endDate,li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2],ls_kurs);
							if(ldtEndDate==null){
								err.reject("","Get End-Date Is Not Working Properly (d)");
								return false;
							}
						}else{
							Map c=(HashMap)selectMstProductInsuredDateNKurs(spaj,insured,li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2]);
							if(c!=null){
								ls_kurs=(String)c.get("LKU_ID");
								ldtBegDate=(Date)c.get("MSPR_BEG_DATE");
								ldtEndDate=(Date)c.get("MSPR_END_DATE");
							}
							//
							if( li_bisnis[li_cnt[2]][1] >= 900 )
								ls_kurs = lkuId;
						}
						//
						if(ls_kurs==null){
							err.reject("","Empty Value of SPAJ Nospaj="+spaj+" lsbsId="+li_bisnis[li_cnt[2]][1]+" Kurs ="+ls_kurs+" Ins Beg Date= "+
									defaultDateFormat.format(ldtBegDate)+" Ins End Date="+defaultDateFormat.format(ldtEndDate)+" Check Eka.mst_product_insured");
							return false;
						}else{
							if(ldtBegDate==null || ldtEndDate==null){
								Map c=(HashMap)selectMstProductInsuredDateNKurs(spaj,insured,li_bisnis[1][1],li_bisnis[1][2]);
								ls_kurs=(String)c.get("LKU_ID");
								ldtBegDate1=(Date)c.get("MSPR_BEG_DATE");
								ldtEndDate1=(Date)c.get("MSPR_END_DATE");
								if(ldtBegDate1==null || ldtEndDate1==null){
									
									err.reject("","Empty Value of SPAJ Nospaj="+spaj+" lsbsId="+li_bisnis[li_cnt[2]][1]+" Kurs ="+ls_kurs+" Ins Beg Date= "+
											defaultDateFormat.format(ldtBegDate)+" Ins End Date="+defaultDateFormat.format(ldtEndDate)+" Check Eka.mst_product_insured");
									return false;
								}else{
									ldtBegDate= ldtBegDate1;
									ldtEndDate= ldtEndDate1;
								}
							}
						}
						//
						li_reas_client=selectLstDetBisnisLsdbsReasClient(li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2]);
						if(li_reas_client==null){
							err.reject("","Empty Jenis Client Reas (Error)");
							return false;
						}
						//
						ldec_ext_mort[2] = new Double(0);
						ldec_ext_risk[2] = new Double(0);
						//
						if(li_bisnis[li_cnt[2]][1]==69)
							li_pemegang = new Integer(1);
						//
						if( (li_reas_client==1 && li_pemegang==0 ) || ( li_reas_client >= 2 && li_pemegang==1)  ) {
							li_hit_premi = 1;
							if (li_cnt[2]== 1 || li_cnt[2] == 2){
								ldec_ext_mort[2] = ldec_ext_mort[1];
								ldec_ext_risk[2] = ldec_ext_risk[1];
							}
						}else if (li_bisnis[li_cnt[2]][1] < 900 ){
							li_hit_premi = 1;
						}else
							li_hit_premi = 0;
						//
						if(ldec_ext_mort[1]==null) 
							ldec_ext_mort[1] = new Double(0);
						//
						if(ldec_ext_mort[2]==null) 
							ldec_ext_mort[2] = new Double(0);
						//	
						if(li_bisnis[li_cnt[2]][1] == 45 || li_bisnis[li_cnt[2]][1] == 130){
							ls_pa_risk = "ABD";}
						Double ldecRetensi=new Double(ldecSar.doubleValue() - ldecReas.doubleValue() );
//						if(ls_pa_risk.equals("ABD")){
//							ls_pa_risk="LIFE";
//						}
//						if(logger.isDebugEnabled())logger.debug("ldet end_date ="+defaultDateFormat.format(ldtEndDate));
						insertMstReinsProduct(spaj,insured,li_bisnis[li_cnt[2]][1],li_bisnis[li_cnt[2]][2], (li_cnt[1]+1),
								ls_kurs,ldecSimultan,ldecTsi,ldecSar,ldecRetensi,ldecReas,li_pa_class,ls_pa_risk,ldec_ext_mort[2],
								ldec_ext_risk[2],ldtBegDate,ldtEndDate,li_pemegang,li_hit_premi,li_contyear);
						typeReasBasic=true;
					}
					
          		}
          	}
          	// untuk rider unit link yang baru
          	List lsRiderExcNew=selectMReasTempNew(spaj);
			for(int index=0;index<lsRiderExcNew.size();index++){
          		ReasTempNew reasNew=(ReasTempNew)lsRiderExcNew.get(index);
            	Map c=(HashMap)selectMstProductInsuredDateNKurs(spaj,insured,reasNew.getLsbs_id(),reasNew.getLsdbs_number());
    			ls_kurs=(String)c.get("LKU_ID");
    			ldtBegDate=(Date)c.get("MSPR_BEG_DATE");
    			ldtEndDate=(Date)c.get("MSPR_END_DATE");
    			li_pemegang=0;//tertanggung
    			
    			li_reas_client=selectLstDetBisnisLsdbsReasClient(reasNew.getLsbs_id(),reasNew.getLsdbs_number());
    			if(li_reas_client==null){
    				err.reject("","Empty Jenis Client Reas (Error)");
    				return false;
    			}
    			if( (li_reas_client==1)  ) {
    				li_hit_premi = 1;
    			}else if (reasNew.getLsbs_id() < 900 ){
    				li_hit_premi = 1;
    			}else
    				li_hit_premi = 0;
    			if(reasNew.getReas()>0){
    			Integer countReasProd= selectCountMstReinsProduct(spaj,reasNew.getLsbs_id(),reasNew.getLsdbs_number(),insured);//buat ngeliat apaka udah ada reins produk
    			if (countReasProd>0){
    				
    			}else
	          		insertMstReinsProduct(spaj,insured,reasNew.getLsbs_id(),reasNew.getLsdbs_number(), (li_cnt[1]+index),
							ls_kurs,0.0,reasNew.getTsi(),reasNew.getSar(),reasNew.getRetensi(),reasNew.getReas(),0,"",0.0,
							0.0,ldtBegDate,ldtEndDate,li_pemegang,li_hit_premi,li_contyear);
    			}
				Integer countReinsProdRider =selecCounttMstReinsProd(spaj);
				if (countReinsProdRider>0){ // insert ke mstReinsProd jika ridernya punya reas walaupun main product ga ada reas.
					Integer countReasProd= selectCountMstReinsProduct(spaj,li_bisnis[1][1],li_bisnis[1][2],insured);//cek udah ada di reins produk??
					if(countReasProd>0){
					}else{
						Integer countMainReas= selectCountMstMainReas(spaj,reasNew.getLsbs_id(),reasNew.getLsdbs_number(),insured);//cek udah ada di reins produk??
						if (countMainReas>0){	
						}else
						insertMstReinsProduct(spaj,insured,li_bisnis[1][1],li_bisnis[1][2], (li_cnt[1]+1),
						ls_kurs,0.0,reasNew.getTsi(),reasNew.getSar(),reasNew.getRetensi(),0.0,0,"",0.0,
						0.0,ldtBegDate,ldtEndDate,li_pemegang,li_hit_premi,li_contyear);				
				
					}
				
				}else{}
	      	}
			//jika basic gak di reas.
			//udpate lstr_id jadi 1 ato treaty karena tidak mengikuti main product
//			if(typeReasBasic==false){ //sesuai permintaan yosep 20080514
//				updateMstReinsLstrId(spaj, 1);
//			
//			}

		}
		return lb_ret;
	}
	
	private Integer selectLstbisnisLsgrId(Integer lsbsIdTemp) {
		return (Integer)querySingle("selectLstbisnisLsgrId",lsbsIdTemp);
	}
	
	public void updateMstPolicyPas(String regSpaj, Date mspo_beg_date, Date mspo_end_date) {
		if(regSpaj == null)regSpaj = "";
		if(!"".equals(regSpaj)){
			Map param = new HashMap();
			param.put("mspo_beg_date",mspo_beg_date);
			param.put("mspo_end_date",mspo_end_date);
			param.put("regSpaj",regSpaj);
			update("updateMstPolicyPas",param);
		}
	}
	
	/**@Fungsi:	Untuk Melakukan Proses insert billing, dimana dalam fungsi ini terdapat kriteria dalam pengisiian billing
	 * @param	String spaj,Integer insured,Integer active,Integer lsbsId,Integer lsdbsNumber,Integer lspdId,Integer lstbId,List lsDp,
	 *			String lusId,Policy policy,BindException err
	 * @return 	boolean
	 * @author 	Ferry Harlim 
	 **/
	public boolean wf_ins_bill(String spaj,Integer insured,Integer active,Integer lsbsId,Integer lsdbsNumber,Integer lspdId,Integer lstbId,List lsDp,
			String lusId,Policy policy,BindException err) throws DataAccessException{
		Integer liPmode, liPperiod, liMonth;
		Integer liPremiKe, liThKe, liPaid = 0, liPaidTU = 0;
		Integer liId = new Integer(0); //3 ~ biaya polis = Rp. 20.000,-
		Integer flagmerchant = 0;
		String lsCab, lsKursId, lsPayId, lsWakil, lsRegion, lsKursPolis;
		Double ldecPremi, ldecBpolis, ldecDp, ldecPremiAwal, ldecPremiTopUp, ldecTotalPremi;
		Date ldtBegDate, ldtEndDate, ldtDueDate;
		Boolean lbRet = true, lbTp = false;
		Double ldecKurs = new Double(1);
		Date ldt_tgl_book;
		Date ldt_tgl_debet = null;
	
		//Policy policy=(Policy)selectDw1Underwriting(spaj,lspdId,lstbId);
		
		lsKursId = policy.getLku_id();
		lsKursPolis = policy.getLku_id();
		liPmode = policy.getLscb_id();
		lsCab = policy.getLca_id();
		liPperiod = policy.getMspo_pay_period();
		lsWakil = policy.getLwk_id();
		lsRegion = policy.getLsrg_id();
		//
		Map map = selectMstInsuredBegDateEndDate(spaj,insured);
		ldtBegDate = (Date) map.get("MSTE_BEG_DATE");
		ldtEndDate = (Date) map.get("MSTE_END_DATE");
		
		//Deddy (11/6/2012) - Jika autodebet premi pertama dan posisi lspd_id !=1, maka tidak perlu proses billing lagi.
		Account_recur account_recur = bacDao.select_account_recur(spaj);
		if(account_recur!=null){
			if(account_recur.getFlag_autodebet_nb()!=null){
				if(account_recur.getFlag_autodebet_nb()==1 && lspdId !=56){
					ldt_tgl_debet=account_recur.getTgl_debet();
					return true;	
				}
			}
		}
		
		//Antisipasi jika di mst_account_recur tgl debet kosong , diset beg date atau kosong , biar aman. menandakan tgl debet di mst_billing = tgl beg date
//		if(account_recur==null){
//			ldt_tgl_debet=ldtBegDate;
//		}
		
		//Yusuf (16 Oct 2009) - Bila Stable Link, ambil begdate nya itu dari slink, bukan dari insured
		if(products.stableLink(uwDao.selectBusinessId(spaj))) {
			List<Map> daftarStableLink = uwDao.selectInfoStableLink(spaj);
			ldtBegDate = null;
			ldtEndDate = null;
			for(Map info : daftarStableLink) {
				int msl_no = ((BigDecimal) info.get("MSL_NO")).intValue(); 
				if(msl_no == 1) { //apabila 1, maka itu premi pokok
					ldtBegDate=(Date) info.get("MSL_BDATE");
					ldtEndDate=(Date) info.get("MSL_EDATE");
					break;
				}
			}
			if(ldtBegDate == null || ldtEndDate == null) throw new RuntimeException("BEGDATE SLINK ERROR, HARAP HUBUNGI ITwebandmobile@sinarmasmsiglife.co.id");
		}
		
		//
		if( lsbsId==142 && lsdbsNumber==11){
			ldecPremi = selectMstProductInsuredPremiSmartSave(spaj,insured,active);
		}else{
			ldecPremi = selectMstProductInsuredPremiDiscount(spaj,insured,active);
		}
		ldecPremiAwal = ldecPremi;
		ldecTotalPremi = ldecPremi;
		
		if(lsKursId.equals("02"))
			liId = new Integer(0); //4~biaya polis = $5
		//
		ldecBpolis = selectMstDefaultMsdefNumeric(liId); //tarik nilai default biaya polis
		//Kalo proteksiNARMAS, biaya polis tidak ada !!! juga pa pti, juga unit-linked
		Integer pos = 0;
		String kode = props.getProperty("product.plan_wf_insBill");
		String bisnisId = f3.format(lsbsId);
		pos = kode.indexOf(bisnisId);
		Map mapTt = selectTertanggung(spaj);
		Integer flagGuthrie = (Integer) mapTt.get("MSTE_FLAG_GUTHRIE");
		String lcaId = (String) mapTt.get("LCA_ID");
		Integer usia_tt = (Integer) mapTt.get("MSTE_AGE");
		Integer jenis_terbit = selectJenisTerbitPolis(spaj);
		
		if(pos>=0 || flagGuthrie.intValue()==1 || products.powerSave(FormatString.rpad("0", lsbsId.toString(), 3))){
			ldecBpolis = new Double(0);
		}
		
		//MANTA - Cek Total Premi (Pokok + Topup)
		if(products.unitLink(bisnisId)) {
			List daftarTopUp = uwDao.selectMstUlinkTopupNewForDetBilling(spaj);
			if(daftarTopUp.size()>0){
				for(int i=0; i<daftarTopUp.size(); i++){
					Map mapUlink = (HashMap) daftarTopUp.get(i);
					Double premi_tu = (Double) mapUlink.get("MU_JLH_PREMI");
					ldecTotalPremi += premi_tu;
				}
			}
		}
			
		//Multi Invest
		if("96,194".indexOf(lsbsId)>-1){
			ldecBpolis = new Double(0);
		}
		//Untuk cerdas new(121), bebas biaya polis
		else if(lsbsId==121){
			ldecBpolis = new Double(0);
		}
		//khusus produk simas sehat biaya polisnya 37,000
		else if(lsbsId==161){
			//ldecBpolis = new Double(37000);
			ldecBpolis = new Double(0);
		//Yusuf - Stable link, tidak ada biaya polis
		}else if(lsbsId==164) {
			ldecBpolis = (double) 0;
		}else if(products.muamalat(spaj)){
			//ldecBpolis = (double) 75000;
			ldecBpolis = new Double(0);
			//khusus mabrur, biayanya beda
			if(lsbsId==153) ldecBpolis = (double) 0;//(double) 40000;
		}else if(lsbsId==171){
			ldecBpolis = (double) 0;
		}else if(lsbsId==172){
			ldecBpolis = (double) 0;
		}else if (lsbsId==183 || lsbsId==189 || lsbsId==193) {//Eka Sehat
			if(lsdbsNumber<16){
				ldecBpolis=new Double(37000);
			}else{
				ldecBpolis = (double) 0;
			}
			if(lsbsId==189 || lsbsId==193){
				ldecBpolis = (double) 0;
			}
			//ldecBpolis = new Double(0);
		
		}else if (lsbsId==186) {//Progressive Link
			ldecBpolis = (double) 0;
		}
		
		//super prot, eka protection, biaya polis 20 rebu
		if(lsbsId.intValue() == 45 || lsbsId.intValue()==85|| lsbsId.intValue() == 130) {
			if(jenis_terbit==0){
				if(lsKursId.equals("02")){
					ldecBpolis = (double) 5;
				}else if(lsKursId.equals("01")){
					ldecBpolis = (double) 20000;
				}
			}else{
				ldecBpolis = (double) 0;
			}
			
		//super sejahtera dan (super sehat atau super sehat plus), biaya polis 20rb
		}else if(lsbsId.intValue() == 145 || lsbsId.intValue() == 53 || lsbsId.intValue() == 54 || lsbsId.intValue() == 131 || lsbsId.intValue() == 132){
			ldecBpolis = (double) 20000;
		}else if(products.SuperSejahtera(lsbsId.toString())){
				ldecBpolis = (double) 0;
		}else if(products.stableSave(lsbsId.intValue(), lsdbsNumber.intValue())){
			ldecBpolis = (double) 0;
		}
		
		//
		if(liPmode!=0){
			liMonth=selectLstPayModeLscbTtlMonth(liPmode);
//			if(logger.isDebugEnabled())logger.debug("ldt enddate ="+defaultDateFormat.format(ldtEndDate));
			Date dTemp=selectAddMonths(defaultDateFormat.format(ldtBegDate),liMonth);
//			if(logger.isDebugEnabled())logger.debug("dtemp ="+defaultDateFormat.format(dTemp));
			ldtEndDate=FormatDate.add(dTemp,Calendar.DATE,-1);
//			if(logger.isDebugEnabled())logger.debug("ldt enddate ="+defaultDateFormat.format(ldtEndDate));
			//Himmia 30/01/2001
			if(sdfDd.format(ldtEndDate).equals(sdfDd.format(ldtBegDate))) {
				ldtEndDate=FormatDate.add(ldtEndDate,Calendar.DATE,-1);
			}
		}
		//
		liThKe = 1;
		liPremiKe = 1;
		Calendar calTemp = new GregorianCalendar(2006,06-1,1);
		
		//yusuf 02062006 due date lebih besar dari 1 juni 2006 ditambah 1 minggu
		if(ldtBegDate.compareTo(calTemp.getTime()) >0){

			boolean smileHospital= lsbsId.intValue() == 195 && (lsdbsNumber >= 73 && lsdbsNumber <= 84);
			boolean smileMedical= lsbsId.intValue() == 183 && (lsdbsNumber >= 106 && lsdbsNumber <= 120);
			
			
			
			if(smileHospital || smileMedical){
				ldtDueDate=FormatDate.add(ldtBegDate,Calendar.DATE,45);
			}else{
				ldtDueDate=FormatDate.add(ldtBegDate,Calendar.DATE,7);
			}
			
			//custom grace period
			if((lsbsId.intValue() == 219 && (lsdbsNumber >= 5 && lsdbsNumber <= 8)) || //helpdesk [138638] produk baru SPP Syariah (219-5~8)
				(lsbsId.intValue() == 226 && (lsdbsNumber >= 1 && lsdbsNumber <= 5)) || //helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
				(lsbsId.intValue() == 134 && lsdbsNumber == 13) || //helpdesk [142003] produk baru Smart Platinum Link RPUL BEL (134-13)
				(lsbsId.intValue() == 120 && (lsdbsNumber >= 22 && lsdbsNumber <= 24)) ){ // Produk SIMPOL 120 (22,23,24)
				ldtDueDate=FormatDate.add(ldtBegDate, Calendar.DATE, 30);
			}
		}else{
			ldtDueDate = selectAddMonths(defaultDateFormat.format(ldtBegDate),new Integer(1));
		}
		
		if(ldtDueDate==null){
			err.reject("","Get End-Date Is Not Working Properly (e)");
			return false;
		}
		//
		if(!lsDp.isEmpty()){
			for(int i=0;i<lsDp.size();i++){
				DepositPremium depPremi = (DepositPremium) lsDp.get(i);
				ldecDp = new Double(0);
				liId = depPremi.getMsdp_jtp();
				if(liId==1 && depPremi.getMsdp_flag_topup() == null){
					lsKursId = depPremi.getLku_id();
					ldecDp = depPremi.getMsdp_payment();
					if( (!lsKursPolis.equals(lsKursId) ) || (lsKursId.equals("02")) ){
						ldt_tgl_book = depPremi.getMsdp_date_book();
						ldecKurs = selectGetKursJb(ldt_tgl_book,"J");
						if(ldecKurs==null){
							err.reject("","Kurs tgl "+defaultDateFormat.format(ldt_tgl_book)+" (dd/mm/yyyy) tidak ada");
							return false;
						}
					}
					
					if(!lsKursPolis.equals(lsKursId)) {
						if(lsKursPolis.equals("01"))
							ldecDp = new Double(ldecDp.doubleValue()* ldecKurs.doubleValue());
						else
							ldecDp = new Double(ldecDp.doubleValue()/ ldecKurs.doubleValue());
					}
					
					ldecPremi = new Double(ldecPremi.doubleValue()-ldecDp.doubleValue());
					lbTp = true;
					
					if(flagmerchant == 0){
						if(depPremi.getMsdp_flag_merchant() != null)
							flagmerchant = depPremi.getMsdp_flag_merchant();
					}
				}
			}
		}
		
		ldecPremi = new Double(ldecPremi.doubleValue()+ ldecBpolis.doubleValue());
		
		//(Deddy : penambahan apabila ada extra premi, biling ditambahkan)
		List<Product> listProductExtra = selectMstProductInsuredExtra(spaj);
		if(listProductExtra.size()>0){
			Double premiExtra = 0.0;
			for(int i=0; i<listProductExtra.size();i++){
				Product productExtra = (Product) listProductExtra.get(i);
				premiExtra += productExtra.getMspr_premium();
			}
//			if(lcaId.equals("42") && products.unitLink(uwDao.selectBusinessId(spaj))){
//				ldecPremi =new Double(ldecPremi.doubleValue());
//			}else{
				ldecPremi = new Double(ldecPremi.doubleValue() + premiExtra.doubleValue());
//			}
		}
		
		//MANTA
		BigDecimal premiawal = new BigDecimal(ldecTotalPremi);
		BigDecimal bd_limit = new BigDecimal(2.5);
		BigDecimal persen = new BigDecimal(100);
		if(ldecPremi.doubleValue() <= 0 && flagmerchant == 0){
			liPaid=1;
		}else if(flagmerchant != 0){
			Double selisih = new Double(0);
			Double merchant_fee = new Double(0);
			ArrayList<HashMap> listMerchant = Common.serializableList(selectLstMerchantFee(flagmerchant));
			
			BigDecimal fee = new BigDecimal(listMerchant.get(0).get("PERSENTASE").toString());
			
			merchant_fee = new Double( ((premiawal.multiply(fee)).divide(persen, RoundingMode.HALF_UP)).doubleValue() );
			selisih = ldecPremi.doubleValue() - merchant_fee.doubleValue();
			if(selisih == new Double(0) || selisih <= new Double(100)) liPaid=1;
			
//			if(ldecPremi.doubleValue() == merchant_fee.doubleValue()) liPaid=1;
		}else{
			Double d_limit = new Double( ((premiawal.multiply(bd_limit)).divide(persen, RoundingMode.HALF_UP)).doubleValue() );
			if(ldecPremi.doubleValue() <= d_limit.doubleValue()) liPaid=1;
		}
		
		//EKALINK FAMILY / ekalink famili syariah
		Integer msbiThBak=null,msbiPremiBak=null;
		Integer idxUlink=props.getProperty("product.premi_ulink").indexOf(bisnisId);
		if(idxUlink>=0){
			//
			Double ldecPremiAsli=selectMstProductInsuredPremiDiscount(spaj,insured,active);
			msbiThBak=1;
			msbiPremiBak=1;
			if (liPmode == 1 || liPmode == 2 || liPmode == 6 || liPmode == 3)
				liPmode= 3;
			
			Double persenAk = getBiaAkuisisi(lsbsId, lsdbsNumber,liPmode, liPperiod, new Integer(1));
			
			PremiUlink premiUlink=new PremiUlink();
			premiUlink.setReg_spaj(spaj);
			premiUlink.setMsbi_tahun_ke(new Integer(1));
			premiUlink.setMsbi_premi_ke(new Integer(1));
			premiUlink.setPremi_ke(new Integer(1));
			premiUlink.setLine_ak(new Integer(1));
			premiUlink.setTh_ak(new Integer(1));
			premiUlink.setPremi(ldecPremiAsli);
			premiUlink.setTotal_premi(ldecPremiAsli);
			premiUlink.setPersen_ak(persenAk);
			insertMstPremiUlink(premiUlink);

		}
		
		//himmia 06/03/2007 untuk produk multi invest msbi_persen_paid=100
		Integer msbiPersenPaid=null;
		if(bisnisId.indexOf(props.getProperty("product.multiInvest"))>0){
			msbiPersenPaid=100;
		}
		Integer countBill= selectCountMstBill(spaj, new Integer(liThKe),new Integer(liPremiKe));
		if(lspdId!=56){
			lspdId=4;
		}
		if(countBill<=0){
			if(!products.powerSave(bisnisId)){
				insertMstBilling(spaj,new Integer(liThKe),new Integer(liPremiKe),ldtBegDate,ldtEndDate,ldtDueDate,lsKursPolis,
						ldecBpolis,new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(liPaid),new Integer(1),
						new Integer(0),new Integer(0),lusId,lspdId,lsCab,lsWakil,lsRegion,null,msbiThBak,msbiPremiBak,msbiPersenPaid,ldt_tgl_debet);
//				if(lcaId.equals("42") && products.unitLink(uwDao.selectBusinessId(spaj))){
//					insertMstDetBillingNoExtra(spaj);
//				}else{
					insertMstDetBilling(spaj);
//				}
			
			} else if(products.powerSave(bisnisId)){
				List<Map> listRiderPSave = bacDao.selectRiderSave(spaj);
				if(listRiderPSave.size()>0){
					Powersave daftarPSave = (Powersave) this.bacDao.select_powersaver(spaj);
					if(Integer.parseInt(bisnisId)==188){
						daftarPSave = (Powersave) this.bacDao.select_powersaver_baru(spaj);
					}
					Map riderPsaveFirst = listRiderPSave.get(0);
					BigDecimal lscb_id_rider = (BigDecimal) riderPsaveFirst.get("LSCB_ID_RIDER");
					BigDecimal mrs_rider_cb = (BigDecimal) riderPsaveFirst.get("MRS_RIDER_CB");
					if(mrs_rider_cb.intValue()==1){
						insertMstBilling(spaj,new Integer(liThKe),new Integer(liPremiKe),ldtBegDate,ldtEndDate,ldtDueDate,lsKursPolis,
								ldecBpolis,new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(liPaid),new Integer(1),
								new Integer(0),new Integer(0),lusId,lspdId,lsCab,lsWakil,lsRegion,null,msbiThBak,msbiPremiBak,msbiPersenPaid,ldt_tgl_debet);
//						if(lcaId.equals("42") && products.unitLink(uwDao.selectBusinessId(spaj))){
//							insertMstDetBillingNoExtra(spaj);
//						}else{
							insertMstDetBilling(spaj);
//						}
					}else if(mrs_rider_cb.intValue()==0){
						int masa_bayar_rider=1;
						if(lscb_id_rider.intValue()==1){
							masa_bayar_rider=3;//triwulan
						}else if(lscb_id_rider.intValue()==2){
							masa_bayar_rider=2;//semester
						}else if(lscb_id_rider.intValue()==6){
							masa_bayar_rider=1;//bulanan
						}else if(lscb_id_rider.intValue()==3){
							masa_bayar_rider=12;//tahunan
						}
						
						if(("142").equals(bisnisId) && lsdbsNumber==11){
							masa_bayar_rider=12;
						}
						
						int periodBill = Integer.parseInt(daftarPSave.getMps_jangka_inv())/ masa_bayar_rider;
						Double  premiRider = bacDao.selectSumPremiRiderInMstRiderSave(spaj);
						//testing dulu bagian ini(10 Okt 2011)
						
						Double factor=0.0;
						Double diskon =1.0;
						if(lscb_id_rider.intValue()==1){
							factor = 0.270;
						}else if(lscb_id_rider.intValue()==2){
							factor = 0.525;
						}else if(lscb_id_rider.intValue()==6){
							factor = 0.1;
						}else factor = 1.0;
						Double total_premi =0.;
						Date beg_date_bill =ldtBegDate;
						for(int i=0;i<listRiderPSave.size();i++){
							Map riderPsave = listRiderPSave.get(i);
							BigDecimal lsbs_id_rider = (BigDecimal) riderPsave.get("LSBS_ID");
							BigDecimal lsdbs_number_rider = (BigDecimal) riderPsave.get("LSDBS_NUMBER");
							BigDecimal mrs_up =  (BigDecimal) riderPsave.get("MRS_UP");
							Date beg_date_next_bill = selectAddMonths(defaultDateFormat.format(ldtBegDate),masa_bayar_rider);
							for(int j=0;j<periodBill;j++){
								if(j>0){
									beg_date_bill =selectAddMonths(defaultDateFormat.format(beg_date_bill),masa_bayar_rider);
									beg_date_next_bill = selectAddMonths(defaultDateFormat.format(beg_date_next_bill),masa_bayar_rider);
								}
								int premi_ke = 0;
								int tahun_ke = 0;
								if(Integer.parseInt(daftarPSave.getMps_jangka_inv())>12){
									tahun_ke = F_hit_tahun_ke.hitTahunKe(FormatDate.getDateInFirstSecond(FormatDate.add(beg_date_next_bill, Calendar.DATE, -1)), FormatDate.getDateInFirstSecond(ldtBegDate), masa_bayar_rider);
									premi_ke =F_hit_premi_ke.hitPremiKe(FormatDate.getDateInFirstSecond(FormatDate.add(beg_date_next_bill, Calendar.DATE, -1)), FormatDate.getDateInFirstSecond(ldtBegDate), masa_bayar_rider);
								}else{
									tahun_ke = F_hit_tahun_ke.hitTahunKe(FormatDate.getDateInFirstSecond(FormatDate.add(beg_date_next_bill, Calendar.DATE, -1)), FormatDate.getDateInFirstSecond(ldtBegDate), masa_bayar_rider);
									premi_ke = F_hit_premi_ke.hitPremiKe(FormatDate.getDateInFirstSecond(FormatDate.add(beg_date_next_bill, Calendar.DATE, -1)), FormatDate.getDateInFirstSecond(ldtBegDate), masa_bayar_rider);
								}
								
								
								Double rate = bacDao.selectRateRider(lsKursId, usia_tt+(tahun_ke-1), 0, lsbs_id_rider.intValue(), lsdbs_number_rider.intValue());
								Double premi = new Double(0);
								if(lsbs_id_rider.intValue()==846 || lsbs_id_rider.intValue()==847){
									premi = new Double(0);
								}else{
									premi =(rate * mrs_up.doubleValue() / 1000) * factor; 
								}								
								if(lsbs_id_rider.intValue()==819 || lsbs_id_rider.intValue()==820 || lsbs_id_rider.intValue()==823 || lsbs_id_rider.intValue()==825 || lsbs_id_rider.intValue()==826){
									if(lsbs_id_rider.intValue()!=826){
										if(lsdbs_number_rider.intValue()>=16){
											diskon = 0.975;
										}
									}else{
										if(lsdbs_number_rider.intValue()>=11){
											diskon = 0.9;
										}
									}
									premi = rate * diskon;
								}
								if(Common.isEmpty(bacDao.selectStatusPaidBilling(spaj, premi_ke, tahun_ke)) ){
									if(j==0 ){
										total_premi = ldecPremi.doubleValue()+premi;
										if(total_premi <= 0){
											liPaid=1;
										}else{
											liPaid=0;
										}
										Double premi_kurang_bayar = bacDao.selectMrsKurangBayarRiderSave(spaj,lsbs_id_rider.toString(), lsdbs_number_rider.toString());
										if(liPaid==1){
											bacDao.updateRiderSavePaidPremi(spaj,lsbs_id_rider.toString(), lsdbs_number_rider.toString(), premi_kurang_bayar, new Double(0) );
										}
										insertMstBilling(spaj,new Integer(liThKe),new Integer(liPremiKe),ldtBegDate,ldtEndDate,ldtDueDate,lsKursPolis,
												ldecBpolis,new Double(0),new Double(0),new Double(0),ldecPremi+premi,new Integer(liPaid),new Integer(1),
												new Integer(0),new Integer(0),lusId,lspdId,lsCab,lsWakil,lsRegion,null,msbiThBak,msbiPremiBak,msbiPersenPaid,ldt_tgl_debet);
									}else{
										insertMstBilling(spaj, tahun_ke, premi_ke,
												beg_date_bill,FormatDate.add(beg_date_next_bill, Calendar.DATE, -1),FormatDate.add(beg_date_bill, Calendar.DATE, 7),
												lsKursPolis, new Double(0), new Double(0), new Double(0), new Double(0),
												premi, 0, 1, 0, 0, lusId, 12, 
												lsCab, lsWakil, lsRegion, 1, null, null, msbiPersenPaid,account_recur.getTgl_debet());
									}
								}else{
									total_premi += premi;
									if(total_premi <= 0){
										liPaid=1;
									}else{
										liPaid=0;
									}
									Double premi_kurang_bayar = bacDao.selectMrsKurangBayarRiderSave(spaj,lsbs_id_rider.toString(), lsdbs_number_rider.toString());
									if(liPaid==1){
										bacDao.updateRiderSavePaidPremi(spaj,lsbs_id_rider.toString(), lsdbs_number_rider.toString(), premi_kurang_bayar, new Double(0) );
									}
									bacDao.updateMstBillingRemain(spaj, tahun_ke, premi_ke, premi,liPaid);
									if(i==1 && j==0){
										insertMstDetBilling(spaj, tahun_ke, premi_ke, 1, lsbsId, lsdbsNumber, ldecPremiAwal, new Double(0));
									}
									
								}
								if(i==0 ){
									insertMstDetBilling(spaj, tahun_ke, premi_ke, i+2, lsbs_id_rider.intValue(), lsdbs_number_rider.intValue(), premi, new Double(0));
								}else{
									insertMstDetBilling(spaj, tahun_ke, premi_ke, i+2, lsbs_id_rider.intValue(), lsdbs_number_rider.intValue(), premi, new Double(0));
								}
							}
						}
					}
				}else{
					insertMstBilling(spaj,new Integer(liThKe),new Integer(liPremiKe),ldtBegDate,ldtEndDate,ldtDueDate,lsKursPolis,
							ldecBpolis,new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(liPaid),new Integer(1),
							new Integer(0),new Integer(0),lusId,lspdId,lsCab,lsWakil,lsRegion,null,msbiThBak,msbiPremiBak,msbiPersenPaid,ldt_tgl_debet);
						
//					if(lcaId.equals("42") && products.unitLink(uwDao.selectBusinessId(spaj))){
//						insertMstDetBillingNoExtra(spaj);
//					}else{
						insertMstDetBilling(spaj);
//					}
				}
			}
		}
		

		//Yusuf (1/5/08) stable link, insert billing dan det billingnya, diambil dari MST_SLINK
		if(products.stableLink(bisnisId)) {
			List<Map> daftarStableLink = uwDao.selectInfoStableLink(spaj);

			for(Map info : daftarStableLink) {
				int msl_no = ((BigDecimal) info.get("MSL_NO")).intValue(); 
				if(msl_no>1) { //apabila 1, maka itu premi pokok
					double premi = ((BigDecimal) info.get("MSL_PREMI")).doubleValue();
					insertMstBilling(spaj, 1, msl_no,
							(Date) info.get("MSL_BDATE"),
							(Date) info.get("MSL_EDATE"),
							(Date) info.get("MSL_NEXT_DATE"),
							lsKursPolis, new Double(0), new Double(0), new Double(0), new Double(0),
							premi, 0, 1, 0, 0, lusId, lspdId, 
							lsCab, lsWakil, lsRegion, 1, null, null, msbiPersenPaid,ldt_tgl_debet);
					insertMstDetBilling(spaj, 1, msl_no, 1, lsbsId, lsdbsNumber, premi, new Double(0));
				}
			}					
		//jika unit link
		}else if(products.unitLink(bisnisId)) {
			
//			Integer li_tu=new Integer(0);
//			Map h=selectMstUlinkTopup(spaj,new Integer(1));
//			li_tu=(Integer)h.get("MU_PERIODIC_TU");
//			ldecPremi=(Double)h.get("MU_JLH_TU");
//			if(li_tu==null)
//				li_tu=new Integer(0);
//			if(ldecPremi==null)
//				ldecPremi=new Double(0);
//			
//			if(li_tu >= 1 && ldecPremi.doubleValue() > 0) {
//				insertMstBilling(spaj,new Integer(liThKe), new Integer(2),ldtBegDate,ldtEndDate,ldtDueDate,
//						lsKursPolis,new Double(0),new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(0),
//						new Integer(1),new Integer(0),new Integer(0),lusId,new Integer(4),lsCab,lsWakil,lsRegion,new Integer(1),null,null,msbiPersenPaid);
//				insertMstDetBillingTopup(spaj,ldecPremi);
//			}
			
			//List<Integer> daftarMuKe = uwDao.selectMuKe(spaj);
			List daftarTopUp = uwDao.selectMstUlinkTopupNewForDetBilling(spaj);
			Integer topupBerkala = this.uwDao.validationTopup(spaj);
			
			if(topupBerkala>=1){
				Integer x = selectMaxMstDetBillingDetKe(spaj); 
				for(int d=0; d<daftarTopUp.size(); d++) {
					x++;
					Map mapUlink = (HashMap) daftarTopUp.get(d);
					Double premi = (Double) mapUlink.get("MU_JLH_PREMI");
					ldecPremiTopUp = premi;
					Integer premiKe = (Integer) mapUlink.get("MU_PREMI_KE");
					Integer topup = (Integer) mapUlink.get("LT_ID");//topup 2=tunggal 5=berkala
					Integer flagTopup = null, msdpTopup = null;
					Integer flagmerchant_tu = 0;
					if(topup==2){
						flagTopup = 1;
					} else if(topup==5){
						flagTopup = 2;
					}
					
					//MANTA - Cek apakah ada DP untuk Billing Top Up
					if(!lsDp.isEmpty()){
						for(int i=0;i<lsDp.size();i++){
							DepositPremium depPremi = (DepositPremium) lsDp.get(i);
							ldecDp = new Double(0);
							liId = depPremi.getMsdp_jtp();
							msdpTopup = depPremi.getMsdp_flag_topup();
							if(liId==1 && flagTopup.equals(msdpTopup)){
								lsKursId = depPremi.getLku_id();
								ldecDp = depPremi.getMsdp_payment();
								if( (!lsKursPolis.equals(lsKursId) ) || (lsKursId.equals("02")) ){
									ldt_tgl_book = depPremi.getMsdp_date_book();
									ldecKurs = selectGetKursJb(ldt_tgl_book,"J");
									if(ldecKurs==null){
										err.reject("","Kurs tgl "+defaultDateFormat.format(ldt_tgl_book)+" (dd/mm/yyyy) tidak ada");
										return false;
									}
								}
								
								if(!lsKursPolis.equals(lsKursId)) {
									if(lsKursPolis.equals("01")){
										ldecDp = new Double(ldecDp.doubleValue() * ldecKurs.doubleValue());
									}else{
										ldecDp = new Double(ldecDp.doubleValue() / ldecKurs.doubleValue());
									}
								}
								
								ldecPremiTopUp = new Double(ldecPremiTopUp.doubleValue() - ldecDp.doubleValue());
							
								if(flagmerchant_tu == 0){
									if(depPremi.getMsdp_flag_merchant() != null)
										flagmerchant_tu = depPremi.getMsdp_flag_merchant();
								}
							}
						}
					}
					
					if(ldecPremiTopUp.doubleValue() <= 0 && flagmerchant_tu == 0){
						liPaidTU = 1;
					}else if(flagmerchant_tu != 0){
						Double selisih = new Double(0);
						Double merchant_fee = new Double(0);
						ArrayList<HashMap> listMerchant = Common.serializableList(selectLstMerchantFee(flagmerchant_tu));
						BigDecimal fee = new BigDecimal(listMerchant.get(0).get("PERSENTASE").toString());
						
						merchant_fee = new Double( ((premiawal.multiply(fee)).divide(persen, RoundingMode.HALF_UP)).doubleValue() );
						selisih = ldecPremiTopUp.doubleValue() - merchant_fee.doubleValue();
						if(selisih == new Double(0) || selisih <= new Double(1)) liPaidTU=1;

//						if(ldecPremiTopUp.doubleValue() == merchant_fee.doubleValue()) liPaidTU=1;
					}
					
					if(premiKe!=0 && topup!=10){
						insertMstBilling(spaj,new Integer(liThKe),d+2,ldtBegDate,ldtEndDate,ldtDueDate,
								lsKursPolis,new Double(0),new Double(0),new Double(0),new Double(0),ldecPremiTopUp,new Integer(liPaidTU),
								new Integer(1),new Integer(0),new Integer(0),lusId,lspdId,lsCab,lsWakil,lsRegion,flagTopup,null,null,msbiPersenPaid,ldt_tgl_debet);
						
						if(premiKe!=1) x = 1;
						insertMstDetBilling(spaj, 1, premiKe, x, lsbsId, lsdbsNumber, premi, new Double(0));
					}
				}
			}	
		}
		
		//Jika ada Deposit Premium
		if(lbTp){
			List daftarTopUp = uwDao.selectMstUlinkTopupNewForDetBilling(spaj);
			for(int i=0;i<lsDp.size();i++){
				DepositPremium depPremi = (DepositPremium) lsDp.get(i);
				Integer i_premike = 1;
				if(depPremi.getMsdp_jtp()==1){
					lsKursId = depPremi.getLku_id();
					ldecKurs = new Double(1);
					if( (!lsKursPolis.equals(lsKursId) ) || (lsKursId.equals("02")) ){
						ldt_tgl_book = depPremi.getMsdp_date_book();
						ldecKurs = selectGetKursJb(ldt_tgl_book,"J");
						if(ldecKurs==null){
							err.reject("","Kurs tgl "+defaultDateFormat.format(ldt_tgl_book)+" (dd/mm/yyyy) tidak ada");
							return false;
						}
					}
					//(12 May 2014) Deddy - Ditutup karena memakai seq dari Oracle lgsg.
//					String sequence=selectGetCounter(9,lsCab);
//					updateCounter(sequence,9,lsCab);
//					DecimalFormat f10=new DecimalFormat("0000000000");
//					lsPayId=lsCab+f10.format(Integer.parseInt(sequence));
					if(depPremi.getMsdp_flag_topup()==null){
						lsPayId = selectSeqPaymentId();
						insertMstPayment(lsPayId, ldecKurs, depPremi);
						insertMstTagPayment(spaj, lsPayId, new Integer(1), i_premike, depPremi);
					}else{
						for(int j=0;j<daftarTopUp.size();j++) {
							Map mapUlink = (HashMap) daftarTopUp.get(j);
							Integer premiKe = (Integer) mapUlink.get("MU_PREMI_KE");
							Integer topup = (Integer) mapUlink.get("LT_ID");//topup tunggal=2 berkala=5
							Integer flagTopup = null;
							if(topup==2){
								flagTopup = 1;
							}else if(topup==5){
								flagTopup = 2;
							}
							if(depPremi.getMsdp_flag_topup().equals(flagTopup)){
								i_premike = premiKe;
								lsPayId = selectSeqPaymentId();
								insertMstPayment(lsPayId, ldecKurs, depPremi);
								insertMstTagPayment(spaj, lsPayId, new Integer(1), i_premike, depPremi);
							}
						}
					}
				}
			}
		}
		//
		if(lbRet){
			if(liPmode!=0){
				if(logger.isDebugEnabled())logger.debug("ldtEnddate= "+defaultDateFormat.format(ldtEndDate));
				Date nextBill=FormatDate.add(ldtEndDate,Calendar.DATE,1);
				if(logger.isDebugEnabled())logger.debug("ldtnext bill= "+defaultDateFormat.format(nextBill));
				updateMstPolicyMspoNextBill(spaj,new Integer(1),nextBill);
			}	
		}
		
		return lbRet;
	}
	
	/**@Fungsi:	Untuk Melakukan Proses insert billing, dimana dalam fungsi ini terdapat kriteria dalam pengisiian billing
	 * @param	String spaj,Integer insured,Integer active,Integer lsbsId,Integer lsdbsNumber,Integer lspdId,Integer lstbId,List lsDp,
	 *			String lusId,Policy policy,BindException err
	 * @return 	boolean
	 * @author 	Ferry Harlim 
	 **/
	public boolean wf_ins_bill_pas(String spaj,Integer insured,Integer active,Integer lsbsId,Integer lsdbsNumber,Integer lspdId,Integer lstbId,List lsDp,
			String lusId,Policy policy,BindException err)throws DataAccessException{
		Integer liPmode,liPperiod,liMonth;
		int liPremiKe, liThKe, liPaid = 0;
		Integer liId = new Integer(0); //3 ~ biaya polis = Rp. 20.000,-
		String lsCab, lsKursId, lsPayId, lsWakil, lsRegion, lsKursPolis;
		Double ldecPremi, ldecBpolis, ldecDp;
		Date ldtBegDate, ldtEndDate, ldtDueDate;
		boolean lbRet = true, lbTp = false;
		Double ldecKurs = new Double(1);
		Date ldt_tgl_book;
	
		//Policy policy=(Policy)selectDw1Underwriting(spaj,lspdId,lstbId);
		
		lsKursId=policy.getLku_id();
		lsKursPolis=policy.getLku_id();
		liPmode=policy.getLscb_id();
		lsCab=policy.getLca_id();
		liPperiod=policy.getMspo_pay_period();
		lsWakil=policy.getLwk_id();
		lsRegion=policy.getLsrg_id();
		//
		Map map=selectMstInsuredBegDateEndDate(spaj,insured);
		ldtBegDate=(Date)map.get("MSTE_BEG_DATE");
		ldtEndDate=(Date)map.get("MSTE_END_DATE");
		
		//Yusuf (16 Oct 2009) - Bila Stable Link, ambil begdate nya itu dari slink, bukan dari insured
		if(products.stableLink(uwDao.selectBusinessId(spaj))) {
			List<Map> daftarStableLink = uwDao.selectInfoStableLink(spaj);
			ldtBegDate = null;
			ldtEndDate = null;
			for(Map info : daftarStableLink) {
				int msl_no = ((BigDecimal) info.get("MSL_NO")).intValue(); 
				if(msl_no == 1) { //apabila 1, maka itu premi pokok
					ldtBegDate=(Date) info.get("MSL_BDATE");
					ldtEndDate=(Date) info.get("MSL_EDATE");
					break;
				}
			}
			if(ldtBegDate == null || ldtEndDate == null) throw new RuntimeException("BEGDATE SLINK ERROR, HARAP HUBUNGI ITwebandmobile@sinarmasmsiglife.co.id");
		}
		
		//
		ldecPremi = selectMstProductInsuredPremiDiscount(spaj,insured,active);
		if(lsKursId.equals("02"))
			liId = new Integer(0); //4~biaya polis = $5
		//
		ldecBpolis=selectMstDefaultMsdefNumeric(liId); //tarik nilai default biaya polis
		//Kalo proteksiNARMAS, biaya polis tidak ada !!! juga pa pti, juga unit-linked
		int pos=0;
		String kode=props.getProperty("product.plan_wf_insBill");
		String bisnisId=f3.format(lsbsId);
		pos=kode.indexOf(bisnisId);
		Map mapTt=selectTertanggung(spaj);
		Integer flagGuthrie=(Integer)mapTt.get("MSTE_FLAG_GUTHRIE");
		
		if(pos>=0 || flagGuthrie.intValue()==1 || products.powerSave(FormatString.rpad("0", lsbsId.toString(), 3))){
			ldecBpolis = new Double(0);
		}
			
		//Multi Invest
		if(lsbsId==96){
			ldecBpolis = new Double(0);
		}
		//Untuk cerdas new(121), bebas biaya polis
		else if(lsbsId==121){
			ldecBpolis = new Double(0);
		}
		//khusus produk simas sehat biaya polisnya 37,000
		else if(lsbsId==161){
			//ldecBpolis = new Double(37000);
			ldecBpolis = new Double(0);
		//Yusuf - Stable link, tidak ada biaya polis
		}else if(lsbsId==164) {
			ldecBpolis = (double) 0;
		}else if(products.muamalat(spaj)){
			//ldecBpolis = (double) 75000;
			ldecBpolis = new Double(0);
			//khusus mabrur, biayanya beda
			if(lsbsId==153) ldecBpolis = (double) 0;//(double) 40000;
		}else if(lsbsId==171){
			ldecBpolis = (double) 0;
		}else if(lsbsId==172){
			ldecBpolis = (double) 0;
		}else if (lsbsId==183 || lsbsId==189 || lsbsId == 193) {//Eka Sehat
			if(lsdbsNumber<16){
				ldecBpolis=new Double(37000);
			}else{
				ldecBpolis = (double) 0;
			}
			if(lsbsId==189){
				ldecBpolis = (double) 0;
			}
			//ldecBpolis = new Double(0);
		
		}else if (lsbsId==186) {//Progressive Link
			ldecBpolis = (double) 0;
		}
		
		//super prot, eka protection, biaya polis 20 rebu
		if(lsbsId.intValue() == 45 || lsbsId.intValue()==85|| lsbsId.intValue() == 130) {
			if(lsKursId.equals("02")){
				ldecBpolis = (double) 5;
			}else if(lsKursId.equals("01")){
				ldecBpolis = (double) 20000;
			}
		//super sejahtera dan (super sehat atau super sehat plus), biaya polis 20rb
		}else if(lsbsId.intValue() == 145 || lsbsId.intValue() == 53 || lsbsId.intValue() == 54 || lsbsId.intValue() == 131 || lsbsId.intValue() == 132){
			ldecBpolis = (double) 20000;
		}else if(products.SuperSejahtera(lsbsId.toString())){
				ldecBpolis = (double) 0;
		}else if(products.stableSave(lsbsId.intValue(), lsdbsNumber.intValue())){
			ldecBpolis = (double) 0;
		}
		
		//
		if(liPmode!=0){
			liMonth=selectLstPayModeLscbTtlMonth(liPmode);
			if(logger.isDebugEnabled())logger.debug("ldt enddate ="+defaultDateFormat.format(ldtEndDate));
			Date dTemp=selectAddMonths(defaultDateFormat.format(ldtBegDate),liMonth);
			if(logger.isDebugEnabled())logger.debug("dtemp ="+defaultDateFormat.format(dTemp));
			ldtEndDate=FormatDate.add(dTemp,Calendar.DATE,-1);
			if(logger.isDebugEnabled())logger.debug("ldt enddate ="+defaultDateFormat.format(ldtEndDate));
			//Himmia 30/01/2001
			if(sdfDd.format(ldtEndDate).equals(sdfDd.format(ldtBegDate))) {
				ldtEndDate=FormatDate.add(ldtEndDate,Calendar.DATE,-1);
			}
		}
		//
		liThKe = 1;
		liPremiKe = 1;
		Calendar calTemp=new GregorianCalendar(2006,06-1,1);
		
		//yusuf 02062006 due date lebih besar dari 1 juni 2006 ditambah 1 minggu
		// direvisi jadi ditambah 30 hari
		if(ldtBegDate.compareTo(calTemp.getTime()) >0){
			//ldtDueDate=FormatDate.add(ldtBegDate,Calendar.DATE,7);
			ldtDueDate=FormatDate.add(ldtBegDate,Calendar.DATE,30);
		}else
		ldtDueDate = selectAddMonths(defaultDateFormat.format(ldtBegDate),new Integer(1));
		
		if(ldtDueDate==null){
			err.reject("","Get End-Date Is Not Working Properly (e)");
			return false;
		}
		//
		if(!lsDp.isEmpty()){
			for(int i=0;i<lsDp.size();i++){
				DepositPremium depPremi=(DepositPremium) lsDp.get(i);
				ldecDp=new Double(0);
				liId=depPremi.getMsdp_jtp();
				if(liId==1){
					lsKursId=depPremi.getLku_id();
					ldecDp=depPremi.getMsdp_payment();
					if( (!lsKursPolis.equals(lsKursId) ) || (lsKursId.equals("02")) ){
						ldt_tgl_book = depPremi.getMsdp_date_book();
						ldecKurs = selectGetKursJb(ldt_tgl_book,"J");
						if(ldecKurs==null){
							err.reject("","Kurs tgl "+defaultDateFormat.format(ldt_tgl_book)+" (dd/mm/yyyy) tidak ada");
							return false;
						}
					}
					
					if(!lsKursPolis.equals(lsKursId)) {
						if(lsKursPolis.equals("01"))
							ldecDp =new Double(ldecDp.doubleValue()* ldecKurs.doubleValue());
						else
							ldecDp =new Double(ldecDp.doubleValue()/ ldecKurs.doubleValue());
					}
					
					ldecPremi = new Double(ldecPremi.doubleValue()-ldecDp.doubleValue());
					lbTp = true;
				}
			}
		}
		//
		ldecPremi = new Double(ldecPremi.doubleValue() + ldecBpolis.doubleValue());
		if(ldecPremi.doubleValue() <= 0)
			liPaid=1;
		//EKALINK FAMILY / ekalink famili syariah
		Integer msbiThBak=null,msbiPremiBak=null;
		Integer idxUlink=props.getProperty("product.premi_ulink").indexOf(bisnisId);
		if(idxUlink>=0){
			//
			Double ldecPremiAsli=selectMstProductInsuredPremiDiscount(spaj,insured,active);
			msbiThBak=1;
			msbiPremiBak=1;
			if (liPmode == 1 || liPmode == 2 || liPmode == 6 || liPmode == 3)
				liPmode= 3;
			
			Double persenAk = getBiaAkuisisi(lsbsId, lsdbsNumber,liPmode, liPperiod, new Integer(1));
			
			PremiUlink premiUlink=new PremiUlink();
			premiUlink.setReg_spaj(spaj);
			premiUlink.setMsbi_tahun_ke(new Integer(1));
			premiUlink.setMsbi_premi_ke(new Integer(1));
			premiUlink.setPremi_ke(new Integer(1));
			premiUlink.setLine_ak(new Integer(1));
			premiUlink.setTh_ak(new Integer(1));
			premiUlink.setPremi(ldecPremiAsli);
			premiUlink.setTotal_premi(ldecPremiAsli);
			premiUlink.setPersen_ak(persenAk);
			insertMstPremiUlink(premiUlink);

		}
		
		//himmia 06/03/2007 untuk produk multi invest msbi_persen_paid=100
		Integer msbiPersenPaid=null;
		if(bisnisId.indexOf(props.getProperty("product.multiInvest"))>0){
			msbiPersenPaid=100;
		}
		Integer countBill= selectCountMstBill(spaj, new Integer(liThKe),new Integer(liPremiKe));
		if(countBill>0){
			
		}else{
			if(lsbsId==187 && (lsdbsNumber>=11 && lsdbsNumber<=14)){
				Map dataRecur = bacDao.selectDataVirtualAccount(spaj);
				int flag_cc	= ((BigDecimal) dataRecur.get("MSTE_FLAG_CC")).intValue();
				if(flag_cc==1 || flag_cc ==2){
					insertMstBilling(spaj,new Integer(liThKe),new Integer(liPremiKe),ldtBegDate,ldtEndDate,ldtDueDate,lsKursPolis,
							ldecBpolis,new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(0),new Integer(1),
							new Integer(0),new Integer(0),lusId,new Integer(56),lsCab,lsWakil,lsRegion,null,msbiThBak,msbiPremiBak,msbiPersenPaid,null);
				}else{
					insertMstBilling(spaj,new Integer(liThKe),new Integer(liPremiKe),ldtBegDate,ldtEndDate,ldtDueDate,lsKursPolis,
							ldecBpolis,new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(0),new Integer(1),
							new Integer(0),new Integer(0),lusId,new Integer(4),lsCab,lsWakil,lsRegion,null,msbiThBak,msbiPremiBak,msbiPersenPaid,null);
				}
				insertMstDetBilling(spaj);
			}else{
				insertMstBilling(spaj,new Integer(liThKe),new Integer(liPremiKe),ldtBegDate,ldtEndDate,ldtDueDate,lsKursPolis,
						ldecBpolis,new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(0),new Integer(1),
						new Integer(0),new Integer(0),lusId,new Integer(4),lsCab,lsWakil,lsRegion,null,msbiThBak,msbiPremiBak,msbiPersenPaid,null);
				insertMstDetBilling(spaj);
			}
		
		}

		//Yusuf (1/5/08) stable link, insert billing dan det billingnya, diambil dari MST_SLINK
		if(products.stableLink(bisnisId)) {
			List<Map> daftarStableLink = uwDao.selectInfoStableLink(spaj);

			for(Map info : daftarStableLink) {
				int msl_no = ((BigDecimal) info.get("MSL_NO")).intValue(); 
				if(msl_no>1) { //apabila 1, maka itu premi pokok
					double premi = ((BigDecimal) info.get("MSL_PREMI")).doubleValue();
					insertMstBilling(spaj, 1, msl_no,
							(Date) info.get("MSL_BDATE"),
							(Date) info.get("MSL_EDATE"),
							(Date) info.get("MSL_NEXT_DATE"),
							lsKursPolis, new Double(0), new Double(0), new Double(0), new Double(0),
							premi, 0, 1, 0, 0, lusId, 4, 
							lsCab, lsWakil, lsRegion, 1, null, null, msbiPersenPaid,null);
					insertMstDetBilling(spaj, 1, msl_no, 1, lsbsId, lsdbsNumber, premi, new Double(0));
				}
			}					
		//jika unit link
		}else if(products.unitLink(bisnisId)) {
			
//			Integer li_tu=new Integer(0);
//			Map h=selectMstUlinkTopup(spaj,new Integer(1));
//			li_tu=(Integer)h.get("MU_PERIODIC_TU");
//			ldecPremi=(Double)h.get("MU_JLH_TU");
//			if(li_tu==null)
//				li_tu=new Integer(0);
//			if(ldecPremi==null)
//				ldecPremi=new Double(0);
//			
//			if(li_tu >= 1 && ldecPremi.doubleValue() > 0) {
//				insertMstBilling(spaj,new Integer(liThKe), new Integer(2),ldtBegDate,ldtEndDate,ldtDueDate,
//						lsKursPolis,new Double(0),new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(0),
//						new Integer(1),new Integer(0),new Integer(0),lusId,new Integer(4),lsCab,lsWakil,lsRegion,new Integer(1),null,null,msbiPersenPaid);
//				insertMstDetBillingTopup(spaj,ldecPremi);
//			}
			int topupBerkala=this.uwDao.validationTopup(spaj);
			//List<Integer> daftarMuKe = uwDao.selectMuKe(spaj);
			List daftarTopUp= uwDao.selectMstUlinkTopupNewForDetBilling(spaj);
			
			if(topupBerkala>=1){
				Integer x=selectMaxMstDetBillingDetKe(spaj); 
				for(int d=0;d<daftarTopUp.size();d++) {
					x++;
					Map mapUlink=(HashMap)daftarTopUp.get(d);
					Double premi=(Double)mapUlink.get("MU_JLH_PREMI");
					Integer premiKe=(Integer)mapUlink.get("MU_PREMI_KE");
					Integer topup=(Integer)mapUlink.get("LT_ID");//topup 2=tunggal 5=berkala
					Integer flagTopup=null;
					if(topup==2)
						flagTopup=1;
					else if(topup==5)
						flagTopup=2;
						
					insertMstBilling(spaj,new Integer(liThKe),d+2,ldtBegDate,ldtEndDate,ldtDueDate,
					lsKursPolis,new Double(0),new Double(0),new Double(0),new Double(0),ldecPremi,new Integer(0),
					new Integer(1),new Integer(0),new Integer(0),lusId,new Integer(4),lsCab,lsWakil,lsRegion,flagTopup,null,null,msbiPersenPaid,null);
					if(premiKe!=1)
						x=1;
					
					insertMstDetBilling(spaj, 1, premiKe, x, lsbsId, lsdbsNumber, premi, new Double(0));
				}
			}	
		}
		
		//
		if(lbTp){//kalo ada DP
			for(int i=0;i<lsDp.size();i++){
				DepositPremium depPremi=(DepositPremium) lsDp.get(i);
				if(depPremi.getMsdp_jtp()==1){
					lsKursId=depPremi.getLku_id();
					ldecKurs=new Double(1);
					if( (!lsKursPolis.equals(lsKursId) ) || (lsKursId.equals("02")) ){
						ldt_tgl_book = depPremi.getMsdp_date_book();
						ldecKurs = selectGetKursJb(ldt_tgl_book,"J");
						if(ldecKurs==null){
							err.reject("","Kurs tgl "+defaultDateFormat.format(ldt_tgl_book)+" (dd/mm/yyyy) tidak ada");
							return false;
						}
					}
					//(12 May 2014) Deddy - Ditutup karena memakai seq dari Oracle lgsg.
//					String sequence=selectGetCounter(9,lsCab);
//					updateCounter(sequence,9,lsCab);
//					DecimalFormat f10=new DecimalFormat("0000000000");
//					lsPayId=lsCab+f10.format(Integer.parseInt(sequence));
					lsPayId= selectSeqPaymentId();
					insertMstPayment(lsPayId,ldecKurs,depPremi);
					insertMstTagPayment(spaj,lsPayId,new Integer(1),new Integer(1),depPremi);
				}
			}
		}
		//
		if(lbRet){
			if(liPmode!=0){
				if(logger.isDebugEnabled())logger.debug("ldtEnddate= "+defaultDateFormat.format(ldtEndDate));
				Date nextBill=FormatDate.add(ldtEndDate,Calendar.DATE,1);
				if(logger.isDebugEnabled())logger.debug("ldtnext bill= "+defaultDateFormat.format(nextBill));
				updateMstPolicyMspoNextBill(spaj,new Integer(1),nextBill);
			}	
		}
		
		return lbRet;
	}
	
	public List selectSpajPremiUlinkNotInserted(Integer lsbsId){
		return query("selectSpajPremiUlinkNotInserted",lsbsId);
	}

	public List selectSpajPremiUlinkNotInsertedNew(){
		return query("selectSpajPremiUlinkNotInsertedNew",null);
	}
	
	public void prosesInsertPremiUlinkManual(User currentUser)throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		//String spaj,Integer insured,Integer active,Integer lsbsId,Integer lsdbsNumber,Policy policy
		Integer lsbsId=162;
		Integer insured=1;
		Integer active=1;
		List lsInsert=new ArrayList();
		if(currentUser.getLus_id().equals("574")){
			//List lsSpaj=selectSpajPremiUlinkNotInserted(lsbsId);
			List lsSpaj=selectSpajPremiUlinkNotInsertedNew();//query dari ronaldy.
			for(int i=0;i<lsSpaj.size();i++){
				Map mapProduk=(HashMap)lsSpaj.get(i);
				String spaj=(String)mapProduk.get("REG_SPAJ");
				BigDecimal lspdId=(BigDecimal)mapProduk.get("LSPD_ID");
				Policy policy=selectDw1Underwriting(spaj,lspdId.intValue(),1);
				
				Integer liPmode,liPperiod,liMonth;
				int liPremiKe, liPaid = 0;
				Integer liId = new Integer(3);
				String lsCab, lsKursId, lsPayId, lsWakil, lsRegion, lsKursPolis;
				Double ldecPremi, ldecBpolis, ldecDp;
				Date ldtBegDate, ldtEndDate, ldtDueDate;
				boolean lbRet = true, lbTp = false;
				Double ldecKurs = new Double(1);
				Date ldt_tgl_book;
			
				//Policy policy=(Policy)selectDw1Underwriting(spaj,lspdId,lstbId);
				lsKursId=policy.getLku_id();
				lsKursPolis=policy.getLku_id();
				liPmode=policy.getLscb_id();
				lsCab=policy.getLca_id();
				liPperiod=policy.getMspo_pay_period();
				lsWakil=policy.getLwk_id();
				lsRegion=policy.getLsrg_id();
				Map mDataUsulan=selectDataUsulan(spaj	);
				Integer lsdbsNumber=(Integer)mDataUsulan.get("LSDBS_NUMBER");
				//
				Map map=selectMstInsuredBegDateEndDate(spaj,insured);
				ldtBegDate=(Date)map.get("MSTE_BEG_DATE");
				ldtEndDate=(Date)map.get("MSTE_END_DATE");
				//
				if(liPmode!=0){
					liMonth=selectLstPayModeLscbTtlMonth(liPmode);
					if(logger.isDebugEnabled())logger.debug("ldt enddate ="+defaultDateFormat.format(ldtEndDate));
					Date dTemp=selectAddMonths(defaultDateFormat.format(ldtBegDate),liMonth);
					if(logger.isDebugEnabled())logger.debug("dtemp ="+defaultDateFormat.format(dTemp));
					ldtEndDate=FormatDate.add(dTemp,Calendar.DATE,-1);
					if(logger.isDebugEnabled())logger.debug("ldt enddate ="+defaultDateFormat.format(ldtEndDate));
					//Himmia 30/01/2001
					if(sdfDd.format(ldtEndDate).equals(sdfDd.format(ldtBegDate))) {
						ldtEndDate=FormatDate.add(ldtEndDate,Calendar.DATE,-1);
					}
				}
				//
		
				Double ldecPremiAsli=selectMstProductInsuredPremiDiscount(spaj,insured,active);
				if (liPmode == 1 || liPmode == 2 || liPmode == 6 || liPmode == 3)
					liPmode= 3;
				
				Double persenAk=getBiaAkuisisi(lsbsId, lsdbsNumber,liPmode, liPperiod, new Integer(1));
				PremiUlink premiUlink=new PremiUlink();
				premiUlink.setReg_spaj(spaj);
				premiUlink.setMsbi_tahun_ke(new Integer(1));
				premiUlink.setMsbi_premi_ke(new Integer(1));
				premiUlink.setPremi_ke(new Integer(1));
				premiUlink.setLine_ak(new Integer(1));
				premiUlink.setTh_ak(new Integer(1));
				premiUlink.setPremi(ldecPremiAsli);
				premiUlink.setTotal_premi(ldecPremiAsli);
				premiUlink.setPersen_ak(persenAk);
				insertMstPremiUlink(premiUlink);
				lsInsert.add(premiUlink.getReg_spaj());
			}
			for(int i=0;i<lsInsert.size();i++){
				System.err.println(lsInsert.get(i));
			}
		}
	}
	private void updateMstPolicyMspoNextBill(String spaj, Integer lstbId, Date nextBill) {
		Map dw_1=new HashMap();
		dw_1.put("mspo_next_bill",nextBill);
		dw_1.put("lstb_id",lstbId);
		dw_1.put("reg_spaj",spaj);
		update("update.transuw.mst_policy.mspo_next_bill",dw_1);

		
	}
	public void insertMstDetBilling(String spaj,Integer tahunKe,Integer premiKe,Integer detKe,
			Integer lsbsId,Integer lsdbsNumber, Double premium, Double disc){
		Map param=new HashMap();
		param.put("reg_spaj", spaj);
		param.put("msbi_tahun_ke", tahunKe);
		param.put("msbi_premi_ke", premiKe);
		param.put("msdb_det_ke", detKe);
		param.put("lsbs_id", lsbsId);
		param.put("lsdbs_number", lsdbsNumber);
		param.put("msdb_premium", premium);
		param.put("msdb_discount", disc);
		insert("insert.transuw.mstDetBilling",param);
	}

	public List selectMstProductInsuredRiderTambahan(String spaj,Integer insured,Integer active){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("ins_no",insured);
		param.put("active",active);
		return query("select.mst_product_insured_rider_tambahan",param);
	}
	
	public Product selectMstProductInsuredUtamaFromSpaj(String reg_spaj){
		return (Product)querySingle("selectMstProductInsuredUtama",reg_spaj);
	}
	
	public List selectMstProductInsuredRiderTambahanSar(String spaj,Integer insured,Integer active){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("ins_no",insured);
		param.put("active",active);
		return query("selectMstProductInsuredRiderTambahanSar",param);
	}
	
	public List selectMstProductInsuredRiderWPD(String spaj,Integer insured,Integer active){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("ins_no",insured);
		param.put("active",active);
		return query("selectMstProductInsuredRiderWPD",param);
	}
	public List selectMstProductInsuredRiderHealth(String spaj,Integer insured,Integer active){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("ins_no",insured);
		param.put("active",active);
		return query("selectMstProductInsuredRiderHealth",param);
	}
	
	public List selectd_ds_sarn(String spaj,Integer insured){
		Map param=new HashMap();
		 param.put("reg_spaj",spaj);
		 param.put("mste_insured_no",insured);
			
		return query("transuw.select.d_ds_sarn2",param);
	
	}
	
	public List selectd_ds_rider_include(Integer lsbsId,Integer lsdbsNumber,String lkuId){
        
		Map param=new HashMap();
	    param.put("lsbs_id",lsbsId);
	    param.put("lsdbs_number",lsdbsNumber);
	    param.put("lku_id",lkuId);
		return query("transuw.select.d_ds_rider_include2",param);
		
	}
	
	public Integer selectLstBisnisLstrId(Integer lsbsId){
		return (Integer)querySingle("transuw.select.lst_bisnis_lstr_id",lsbsId);
	}
	
	public Date selectAddMonthsWfSaveReins(String tanggal,int policyAge){
		Date date = null;
		try{
			Date tanggal2 = (Date) defaultDateFormat.parse(tanggal);
			int nilai = ((policyAge-1)*12);
			date = DateUtil.selectAddDate(tanggal2, "mm", true, nilai);
		}catch(Exception e) {
			logger.error("ERROR :", e);
		}
		return date;
	}
	public Date selectAddMonths(String tanggal,Integer month){
		Date date = null;
		try{
			Date tanggal2 = (Date) defaultDateFormat.parse(tanggal);
			date = DateUtil.selectAddDate(tanggal2, "mm", true, month.intValue());
		}catch(Exception e) {
			logger.error("ERROR :", e);
		}
		return date;
	}
	
	public Map selectMstPolicy(String spaj,Integer lstbId){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("lstb_id",lstbId);
		return (HashMap)querySingle("transuw.select.mst_policy",param);
		
	}	
	/**@Fungsi: untuk menginsert ke dalam tabel EKA.MST_REINS
	 * @param	String spaj,Integer insured, Integer li_type_reas, Integer bisnisId, Integer bisnisNo, 
	 *			int li_last_policy_age, int li_next_policy_age, Date ldtLastPrmDate, Date ldtNextPrmDate, Integer medical, 
	 *			Integer lstbId, Integer printCertificate,Integer lspdId,String lusId,Integer jenis
	 *@author 	Ferry Harlim
	 **/
	public void insertMstReins(String spaj,Integer insured, Integer li_type_reas, Integer bisnisId, Integer bisnisNo, 
			int li_last_policy_age, int li_next_policy_age, Date ldtLastPrmDate, Date ldtNextPrmDate, Integer medical, 
			Integer lstbId, Integer printCertificate,Integer lspdId,String lusId,Integer jenis){
		Map paramIns=new HashMap();
		paramIns.put("reg_spaj",spaj);
		paramIns.put("mste_insured_no",insured);
		paramIns.put("msre_certificate",spaj);
		paramIns.put("lstb_id",lstbId);
		paramIns.put("lstr_id",li_type_reas);
		paramIns.put("lsbs_id",bisnisId);
		paramIns.put("lsdbs_number",bisnisNo);
		paramIns.put("msre_last_policy_age",new Integer(li_last_policy_age));
		paramIns.put("msre_next_policy_age",new Integer(li_next_policy_age));
		paramIns.put("msre_last_prm_date",ldtLastPrmDate);
		paramIns.put("msre_next_prm_date",ldtNextPrmDate);
		paramIns.put("msre_medical",medical);
		paramIns.put("lspd_id",lspdId);
		paramIns.put("msre_print_certificate",printCertificate);
		paramIns.put("lus_id",lusId);
		paramIns.put("msre_jenis", jenis);
//		paramIns.put("flag", flag);
		getSqlMapClientTemplate().insert("elions.uw.insert.transuw.mst_reins",paramIns);
		
	}
	
	public List selectMReasTemp(String spaj){
		return query("transuw.select.m_reas_temp",spaj);
	}
	
	/**Fungsi : Untuk Menampilkan data retensi khusus main product
	 * 
	 * @param spaj
	 * @return Double
	 */
	public Double selectRetensiMReasTemp(String spaj){
		return (Double) querySingle("transuw.selectMReasTempMainProduct",spaj);
	}
	
	public Integer selectGetPaClass(String spaj,Integer insured,Integer lsbsId,Integer lsdbsNumber){
		Integer li_pa_class = new Integer(0);

		if(lsbsId< 600 || lsbsId>= 800 ){

			Map param=new HashMap();
			param.put("reg_spaj",spaj);
			param.put("mste_insured_no",insured);
			param.put("lsbs_id",lsbsId);
			param.put("lsdbs_number",lsdbsNumber);
			li_pa_class=(Integer)querySingle("transuw.select.mst_product_insured.mspr_class",param);
		//
			if(li_pa_class==null) 
					li_pa_class = new Integer(0);
		}else if(lsbsId== 600){
			li_pa_class = new Integer(1);
		}
//
		if(li_pa_class == 0){
			
			if(lsbsId==45||lsbsId==130||lsbsId==600||lsbsId==800){
				//MessageBox('Error PA Class', 'Empty PA Class, check F_GET_PA_CLASS')
				li_pa_class=null;
			}else if(lsbsId==52||lsbsId==62||lsbsId==78){
				li_pa_class = new Integer(1);
			}
		}

		return li_pa_class;
	}
	
	public String selectGetPaRisk(String spaj,Integer insured,Integer lsbsId,Integer lsdbsNumber){
		String ls_pa_risk = "";
		String ls_risk_item[] = { "A", "B", "C", "D", "M"};
		Double ldec_pa_risk[]=new Double[6];
		int li_cnt;
		List list;
		if(lsbsId<600 || lsbsId>=800){
			Map param=new HashMap();
			param.put("reg_spaj",spaj);
			param.put("mste_insured_no",insured);
			param.put("lsbs_id",lsbsId);
			param.put("lsdbs_number",lsdbsNumber);
			Map a=(HashMap)querySingle("transuw.select.mst_product_insured.mspr_tsi",param);
			if(a!=null){
				ldec_pa_risk[0]=(Double)a.get("MSPR_TSI_PA_A");
				ldec_pa_risk[1]=(Double)a.get("MSPR_TSI_PA_B");
				ldec_pa_risk[2]=(Double)a.get("MSPR_TSI_PA_C");
				ldec_pa_risk[3]=(Double)a.get("MSPR_TSI_PA_D");
				ldec_pa_risk[4]=(Double)a.get("MSPR_TSI_PA_M");
			}
			
			for(li_cnt=0;li_cnt<ls_risk_item.length;li_cnt++){
				if(ldec_pa_risk[li_cnt]!=null){
					if(ldec_pa_risk[li_cnt].doubleValue() > 0) 
						ls_pa_risk += ls_risk_item[li_cnt];
				}
			}
		}else if(lsbsId ==600){
			ls_pa_risk += ls_risk_item[1];
		}
		//
		if(ls_pa_risk.equals("")){
			if(lsbsId==45 || lsbsId==600 || lsbsId==800 || lsbsId==130){
				//MessageBox('Error PA Risk', 'Empty PA Risk, CHECK F_GET_PA_RISK')
				ls_pa_risk=null;
			}else if(lsbsId==52 || lsbsId==62 || lsbsId==78){
				ls_pa_risk = "A";
			}
		}
//		if(ls_pa_risk.equals(""))ls_pa_risk=null;
		return ls_pa_risk;
	}
	
	public Date selectBegDateReinsProduct(String tanggal,Integer lsbsId,Integer lsdbsNumber,String kursId ) throws DataAccessException, ParseException{
		Date ldt_beg_date_reas;
		Integer li_add_month=null;
		
		Map param=new HashMap();
		param.put("lsbs_id",lsbsId);
		param.put("lsdbs_number",lsdbsNumber);
		param.put("lku_id",kursId);
		li_add_month=(Integer)querySingle("transuw.select.lst_reins_desc.lsrp_add_beg_date",param);

		if(li_add_month==null) 
			ldt_beg_date_reas=null;
		else
			ldt_beg_date_reas = selectFaddMonths(tanggal,li_add_month);
			
		
		
		return ldt_beg_date_reas;
	}
	
	public Date selectEndDateReinsProduct(Date begDate,Date endDate,Integer lsbsId,Integer lsdbsNumber, String kursId){
		Integer li_flag_insper, li_add_month;
		Date ldt_end_date_reas;

		li_flag_insper=null;
		li_add_month=null;
		//
		Map param=new HashMap();
		param.put("lsbs_id",lsbsId);
		param.put("lsdbs_number",lsdbsNumber);
		param.put("lku_id",kursId);
		Map a=(HashMap) querySingle("transuw.select.lst_reins_desc",param);
		li_flag_insper=(Integer)a.get("LSRP_FLAG_INSPER");
		li_add_month=(Integer)a.get("LSRP_ADD_END_DATE");
		//
		if(li_flag_insper==null || li_add_month==null )
			ldt_end_date_reas=null;
		else if(li_flag_insper==0)
			ldt_end_date_reas = selectAddMonths(defaultDateFormat.format(begDate),li_add_month);
		else if(li_flag_insper==1)
			ldt_end_date_reas = selectAddMonths(defaultDateFormat.format(endDate),li_add_month);
		else
			ldt_end_date_reas=null;
		
		return ldt_end_date_reas;
	}
	
	
	public Map selectMstProductInsuredDateNKurs(String spaj,Integer insured,Integer lsbsId,Integer lsdbsNumber){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mste_insured_no",insured);
		param.put("lsbs_id",lsbsId);
		param.put("lsdbs_number",lsdbsNumber);
		//
		return (HashMap)querySingle("transuw.select.mst_product_insured",param);
		 
	}
	
	public Integer selectLstDetBisnisLsdbsReasClient(Integer lsbsId,Integer lsdbsNumber){
		Map param=new HashMap();
		param.put("lsbs_id",lsbsId);
		param.put("lsdbs_number",lsdbsNumber);
		return (Integer)querySingle("transuw.select.lst_det_bisnis.lsdbs_reas_client",param);
		
	}
	/**@Fungsi:	Untuk Menginsert tabel EKA.MST_REINS_PRODUCT
	 * @param	String spaj,Integer insured,Integer lsbsId,Integer lsdbsNumber,int msrprNumber,
	 *			String lkuId,Double ldecSimultan,Double ldecTsi,Double ldecSar,Double ldecRetensi,Double ldecReas,Integer liPaClass,
	 *			String lsPaRisk,Double ldecExtMort,Double ldecExtRisk,Date begDate,Date endDate,Integer liPemegang,int hitpremi,
	 *			Integer liCountYear
	 * @author 	Ferry Harlim
	 * */
	public void insertMstReinsProduct(String spaj,Integer insured,Integer lsbsId,Integer lsdbsNumber,int msrprNumber,
			String lkuId,Double ldecSimultan,Double ldecTsi,Double ldecSar,Double ldecRetensi,Double ldecReas,Integer liPaClass,
			String lsPaRisk,Double ldecExtMort,Double ldecExtRisk,Date begDate,Date endDate,Integer liPemegang,int hitpremi,
			Integer liCountYear){
//		if(lsbsId.intValue() < 300) {
			Map param_insert=new HashMap();
			param_insert.put("reg_spaj",spaj);
			param_insert.put("mste_insured_no",insured);
			param_insert.put("lsbs_id",lsbsId);
			param_insert.put("lsdbs_number",lsdbsNumber);
			param_insert.put("msrpr_number",new Integer(msrprNumber));
			param_insert.put("lku_id",lkuId);
			param_insert.put("msrpr_simultan",ldecSimultan);
			param_insert.put("msrpr_tsi",ldecTsi);
			param_insert.put("msrpr_resiko_awal",ldecSar);
			param_insert.put("msrpr_retensi",ldecRetensi);
			param_insert.put("msrpr_tsi_reas",ldecReas);
			param_insert.put("msrpr_pa_class",liPaClass);
			param_insert.put("msrpr_pa_risk",lsPaRisk);
			param_insert.put("msrpr_extra_mort",ldecExtMort);
			param_insert.put("msrpr_extra_risk",ldecExtRisk);
			param_insert.put("msrpr_beg_date",begDate);
			param_insert.put("msrpr_end_date",endDate);
			param_insert.put("msrpr_pemegang",liPemegang);
			param_insert.put("msrpr_flag_premi",new Integer(hitpremi));
			param_insert.put("msrpr_sar",new Integer(0));
			param_insert.put("msrpr_premium",new Integer(0));
			param_insert.put("msrpr_commision",new Integer(0));
			param_insert.put("msrpr_prm_ext_mort",new Integer(0));
			param_insert.put("msrpr_comm_ext_mort",new Integer(0));
			param_insert.put("msrpr_prm_ext_risk",new Integer(0));
			param_insert.put("msrpr_comm_ext_risk",new Integer(0));
			param_insert.put("msrpr_contract_year",liCountYear);
	//		param_insert.put("flag",flag);
			getSqlMapClientTemplate().insert("elions.uw.insert.transuw.mst_reins_product",param_insert);
//		}
	}

	public int validasiSpajAsli(String spaj) {
		return (Integer) querySingle("validasiSpajAsli", spaj);		
	}
	
	public Map selectMstInsuredBegDateEndDate(String spaj,Integer insured){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mste_insured_no",insured);
		return (HashMap)querySingle("transuw.select.eka.mst_insured.begenddate",param);
		
	}
	
	public Double selectMstProductInsuredPremiDiscount(String spaj, Integer insured, Integer active){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mste_insured_no",insured);
		param.put("mspr_active",active);
		return (Double)querySingle("transuw.select.eka.mst_insured.PremiDiscount",param);
	}
	
	public Double selectMstProductInsuredPremiSmartSave(String spaj, Integer insured, Integer active){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mste_insured_no",insured);
		param.put("mspr_active",active);
		return (Double)querySingle("transuw.select.eka.mst_insured.PremiSmartSave",param);
	}
	
	public Integer selectLstPayModeLscbTtlMonth(Integer payMode){
		return(Integer)querySingle("transuw.select.eka.lst_pay_mode.lscb_ttl_month",payMode);
	}
	
	public Double selectGetKursJb(Date tglKurs,String asJb){
		Double ldec_kurs = new Double(1);

		Map param= new HashMap();
		param.put("lku_id","02");
		param.put("lkh_date",tglKurs);
		//
		if(asJb.equalsIgnoreCase("J")){ 		
			ldec_kurs=(Double)querySingle("transuw.select.eka.lst_daily_currency.lkh_kurs_jual",param);
		}else if(asJb.equalsIgnoreCase("B")){
			ldec_kurs=(Double)querySingle("transuw.select.eka.lst_daily_currency.lkh_kurs_beli",param);
		}else{
			ldec_kurs=(Double)querySingle("transuw.select.eka.lst_daily_currency.lkh_currency",param);
		}
		//MessageBox('Information','Kurs tgl '+String(adt_tgl_kurs,'dd/mm/yyyy')+ ' tidak ada, '+&
		//			'harap hubungi CSF')
		return ldec_kurs;
	}
	
	public void insertMstBilling(String spaj,Integer liThKe,Integer liPremiKe,Date ldtBegDate,Date ldtEndDate,
			Date ldtDueDate,String lkuId,Double ldecBPolis,Double hcrPolicyCost, Double ttlCardCost,Double stamp,
			Double ldecPremi,Integer liPaid,Integer msbiActive,Integer msbiPrint,Integer addBill,String lusId,Integer lspdId,
			String lcaId,String lwkId,String lsrgId,Integer flagTopup,Integer msbiThBak,Integer msbiPremiBak,Integer msbiPersenPaid,Date tglDebet)throws DataAccessException{
		Map insBill=new HashMap();
		insBill.put("reg_spaj",spaj);
		insBill.put("msbi_tahun_ke",liThKe);
		insBill.put("msbi_premi_ke",liPremiKe);
		insBill.put("msbi_beg_date",ldtBegDate);
		insBill.put("msbi_end_date",ldtEndDate);
		
		insBill.put("msbi_due_date",ldtDueDate);
		insBill.put("msbi_aktif_date",ldtBegDate);
		insBill.put("lku_id",lkuId);
		insBill.put("msbi_policy_cost",ldecBPolis);
		insBill.put("msbi_hcr_policy_cost",hcrPolicyCost);
		
		insBill.put("msbi_ttl_card_cost",ttlCardCost);
		insBill.put("msbi_stamp",stamp);
		insBill.put("msbi_remain",ldecPremi);
		insBill.put("msbi_paid",liPaid);
		insBill.put("msbi_active",msbiActive);
		
		insBill.put("msbi_print",msbiPrint);
		insBill.put("msbi_add_bill",addBill);
		insBill.put("lus_id",lusId);
		insBill.put("lspd_id",lspdId);
		insBill.put("lca_id",lcaId);
		insBill.put("lwk_id",lwkId);
		insBill.put("lsrg_id",lsrgId);
		insBill.put("msbi_flag_topup",flagTopup);
		insBill.put("msbi_th_bak", msbiThBak);
		insBill.put("msbi_premi_bak", msbiPremiBak);
		insBill.put("msbi_persen_paid", msbiPersenPaid);
		insBill.put("tgl_debet",tglDebet);
		getSqlMapClientTemplate().insert("elions.uw.insert.transuw.mst_billing",insBill);
	}

	public void insertMstDetBilling(String spaj){
		Map insDetBill=new HashMap();
		insDetBill.put("reg_spaj",spaj);
		getSqlMapClientTemplate().insert("elions.uw.insert.transuw.mst_det_billing",insDetBill);
		
	}
	
	public void insertMstDetBillingNoExtra(String spaj){
		Map insDetBill=new HashMap();
		insDetBill.put("reg_spaj",spaj);
		getSqlMapClientTemplate().insert("elions.uw.insert.transuw.mst_det_billing_noextra",insDetBill);
		
	}
	
	/*public void insertMstDetBillingTopup(String spaj,Double ldecPremi){
		Map insDetBillTopup=new HashMap();
		insDetBillTopup.put("reg_spaj",spaj);
		insDetBillTopup.put("mspr_premium",ldecPremi);
		getSqlMapClientTemplate().insert("elions.uw.insert.transuw.mst_det_billing_topup",insDetBillTopup);
		
	}*/
	
	public void insertMstPayment(String lsPayId, Double ldecKurs,DepositPremium u){
		Payment insPayment=new Payment();
		insPayment.setMspa_payment_id(lsPayId);
		insPayment.setLku_id(u.getLku_id());
		insPayment.setLsjb_id(u.getLsjb_id());
		insPayment.setClient_bank(u.getClient_bank());
		insPayment.setReg_spaj(u.getReg_spaj());
		insPayment.setMsdp_number(u.getMsdp_number());
		insPayment.setMspa_no_rek(u.getMsdp_no_rek());
		insPayment.setMspa_pay_date(u.getMsdp_pay_date());
		insPayment.setMspa_due_date(u.getMsdp_due_date());
		insPayment.setMspa_date_book(u.getMsdp_date_book());
		insPayment.setMspa_payment(u.getMsdp_payment());
		insPayment.setMspa_input_date(u.getMsdp_input_date());
		insPayment.setMspa_old_policy(u.getMsdp_old_policy());
		insPayment.setMspa_desc(u.getMsdp_desc());
		insPayment.setLus_id(u.getLus_id());
		insPayment.setMspa_active(u.getMsdp_active());
		insPayment.setLsrek_id(u.getLsrek_id());
		insPayment.setMspa_no_pre(u.getMsdp_no_pre());
		insPayment.setMspa_jurnal(u.getMsdp_jurnal());
		insPayment.setMspa_nilai_kurs(ldecKurs);
		insPayment.setMspa_no_voucher(u.getMsdp_no_voucher());
		insPayment.setMspa_flag_merchant(u.getMsdp_flag_merchant());
		getSqlMapClientTemplate().insert("elions.uw.insert.transuw.mst_payment",insPayment);
	}
	
	public void insertMstTagPayment(String spaj,String lsPayId,Integer tahunKe,Integer premiKe,DepositPremium u){
		Map insTagPayment=new HashMap();
		insTagPayment.put("reg_spaj",spaj);
		insTagPayment.put("msbi_tahun_ke",tahunKe);
		insTagPayment.put("msbi_premi_ke",premiKe);
		insTagPayment.put("mstp_value",u.getMsdp_payment());
		insTagPayment.put("mspa_payment_id",lsPayId);
		getSqlMapClientTemplate().insert("elions.uw.insert.transuw.mst_tag_payment",insTagPayment);
	}
	
	public Map selectMstUlinkTopup(String spaj,Integer ke){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mu_ke",ke);
		
		return (HashMap)querySingle("transuw.select.eka.mst_ulink_topup",param);
		
	}
	/**@Fungsi:	Untuk Mengecek data pada kolom MSBI_PAID pada tabel EKA.MST_BILLING
	 * 			apakah untuk pembayaran premi_ke dan tahun_ke itu sudah lunas atau belum
	 * @param	String spaj,Integer tahunKe,Integer premiKe
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 * */
	public Integer selectMstBillingMsbiPaid(String spaj,Integer tahunKe,Integer premiKe){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("msbi_tahun_ke",tahunKe);
		param.put("msbi_premi_ke",premiKe);
		return (Integer)querySingle("transuw.select.eka.mst_billing.msbi_paid",param);
		
	}
	/**@Fungsi: Untuk menampilkan data pada kolom MU_PERIODIC_TU pada tabel EKA.MST_ULINK 
	 * @param	String spaj,Integer muKe
	 * @return 	Integer
	 * @author 	Ferry Harlim
	 **/
	public Integer selectMstUlinkMuPeriodicTu(String spaj,Integer muKe){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mu_ke",muKe);
		return (Integer)querySingle("transuw.select.eka.mst_ulink.mu_periodic_tu",param);
		
	}

	public List selectMSarTemp(String spaj){
		return query("select.eka.m_sar_temp",spaj);
	}
	
	public List selectMSarTemp2(String spaj){
		return query("select.eka.m_sar_temp_new",spaj);
	}

	public List selectMSarTempMedis(String spaj,Integer flag){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("flag", flag);
		return query("select.eka.m_sar_temp",param);
	}
	
	public List selectProductUtama(String spaj){
		return query("select.product_utama2",spaj);
	}
	
	public List selectSpajSimultan(String id_simultan){
		return query("selectSpajSimultan",id_simultan);
	}
	
	public List selectSpajSimultanLife(String id_simultan){
		return query("selectSpajSimultanLife",id_simultan);
	}
	
	public List listOldSar(String id_simultan){
		return query("listOldSar",id_simultan);
	}
	
	public List listOldProdSave(String id_simultan){
		return query("listOldProdSave",id_simultan);
	}
	
	public List listOldSarTPD(String id_simultan){
		return query("listOldSarTPD",id_simultan);
	}
	public Double selectOldSar(String id_simultan, Integer rowke)throws DataAccessException {
		Map map=new HashMap();
		map.put("id_simultan", id_simultan);
		map.put("rowke", rowke);
		return (Double) querySingle("selectOldSar",map);
	}
	
	public Double selectOldSarTPD(String id_simultan, Integer rowke)throws DataAccessException {
		Map map=new HashMap();
		map.put("id_simultan", id_simultan);
		map.put("rowke", rowke);
		return (Double) querySingle("selectOldSarTPD",map);
	}
	public Double selectOldSar1(String id_simultan, Integer rowke)throws DataAccessException {
		Map map=new HashMap();
		map.put("id_simultan", id_simultan);
		map.put("rowke", rowke);
		return (Double) querySingle("selectOldSar1",map);
	}
	
	public Integer prosesReas(String spaj,String msteInsured,String mspoPolicyHolder,Integer insured,String lusId,BindException err)throws Exception{
		Integer liReas=null;
		Integer liBackup=null;
		liReas=getReas(spaj,true,err);
		if(liReas==null){
			err.reject("","Please ContacT ITwebandmobile@sinarmasmsiglife.co.id, Gagal Proses Reas");
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			return null;
		}
		//
		wfInsSimultan(spaj,msteInsured,mspoPolicyHolder,insured);
		//update mst_insured
		updateMstInsuredReasnBackup(spaj,insured,liReas,liBackup,null,null);
		wf_ins_cash_tpd(spaj,insured);
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		if(liReas==0){
			liBackup=new Integer(3);
			insertMstPositionSpaj(lusId, "PROSES REAS (NON REAS)", spaj, 0);
		}else if(liReas==1){
			liBackup=new Integer(2);
			insertMstPositionSpaj(lusId, "PROSES REAS (TREATY)", spaj, 0);
		}else if(liReas==2){
			liBackup=new Integer(0);
			insertMstPositionSpaj(lusId, "PROSES TRANSFER DARI U/W)", spaj, 0);
		}
		
		return liReas;
	}

	public Map prosesReasNew(String spaj,String msteInsured,String mspoPolicyHolder,Integer insured, User currentUser,BindException err)throws Exception{
		Map map=new HashMap();
		Integer liReas=null;
		Integer liBackup=null;
		liReas=getReas(spaj,true,err);
		if(liReas==null){
			err.reject("","Please ContacT ITwebandmobile@sinarmasmsiglife.co.id, Gagal Proses Reas");
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			return null;
		}
		//
		wfInsSimultan(spaj,msteInsured,mspoPolicyHolder,insured);
		//update mst_insured
		updateMstInsuredReasnBackup(spaj,insured,liReas,liBackup,null,null);
		wf_ins_cash_tpd(spaj,insured);
		
		//proses simultan medis.. june 2007
		//cek 2 tahun saja yang simultannya. lalu bandingkan ke tabel medis
//		Policy policy=selectDw1Underwriting(spaj,2,1);
//		List lsSar=selectMSarTemp(spaj);
//		Date begDateCom=null,begDateNext;
//		Double totalSar=new Double(0);
//		for(int i=0;i<lsSar.size();i++){
//			Map mSar=(HashMap)lsSar.get(i);
//			String regSpaj=(String)mSar.get("REG_SPAJ");
//			String regSpajRef=(String)mSar.get("REG_SPAJ_REF");
//			Integer lsbsId=(Integer)mSar.get("BISNIS_ID");
//			Integer lsdbsNumber=(Integer)mSar.get("BISNIS_NO");
//			Double sar=(Double)mSar.get("SAR");
//			Map product=selectMstProductInsuredDateNKurs(regSpajRef, 1, lsbsId,lsdbsNumber);
//			if(i==0){
//				begDateCom=(Date)product.get("MSPR_BEG_DATE");
//				begDateNext=(Date)product.get("MSPR_BEG_DATE");
//			}else{
//				begDateNext=(Date)product.get("MSPR_BEG_DATE");
//			}
//			if(FormatDate.dateDifferenceInYears(begDateCom, begDateNext)<=2){
//				totalSar+=sar;
//			}
//		}	
//		//cari range age 
//		Integer age=selectMstInsuredMsteAge(spaj, insured);
//		Integer rangeAge=selectLstMedicalRangeAge(age);
//		//cari range sar
//		Integer rangeSar=selectLstMedicalRangeSar(selectMstProductInsuredKurs(spaj, insured), totalSar);
		Integer medis=0;
//		List lsMedis=selectDaftarTabelMedis(rangeAge, rangeSar, 1, policy.getLku_id());
//		for(int j=0;j<lsMedis.size();j++){
//			TabelMedis tabelMedis=(TabelMedis)lsMedis.get(j);
//			if(tabelMedis.getJns_medis()!=1){
//				Medical medical=new Medical();
//				medical.setReg_spaj(spaj);
//				medical.setLsmc_id(tabelMedis.getLsmc_id());
//				medical.setMste_insured_no(1);
//				medical.setMpa_number(j+1);
//				medical.setMsdm_status(1);
//				insertMstDetMedical(medical);
//				medis=1;
//			}
//		}
//		updateMstInsuredMedical(spaj, medis);
		//email ke cabang bersangkutan.
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		map.put("liReas", liReas);
		map.put("medis", medis);
		
		if(liReas==0){
			liBackup=new Integer(3);
			insertMstPositionSpaj(currentUser.getLus_id(), "PROSES REAS (NON REAS)", spaj, 0);
		}else if(liReas==1){
			liBackup=new Integer(2);
			insertMstPositionSpaj(currentUser.getLus_id(), "PROSES REAS (TREATY)", spaj, 0);
		}else if(liReas==2){
			liBackup=new Integer(0);
			insertMstPositionSpaj(currentUser.getLus_id(), "PROSES TRANSFER DARI U/W)", spaj, 0);
		}
		
		return map;
	}

	/**@Fungsi:	Untuk Menentukan tipe reas dari suatu produk.
	 * @param	String spaj,boolean abAdd,BindException err
	 * @return 	Integer (0=NonReas 1=Treaty 2=facultative
	 * @author 	Ferry Harlim
	 * */
	public Integer getReas(String spaj,boolean abAdd,BindException err)throws DataAccessException{
		int aiCnt=0;
		Integer liReas=new Integer(0);
		Integer liBisnisId[] = new Integer[5];
		Integer liBisnisNo[] = new Integer[5];
		Integer liReasClient[] = new Integer[5];
		Double ldecRateSar[] = new Double[5];
		Double ldecLimit[] = new Double[5];
		Double ldecTotalReas[]=new Double[5];
		Integer liThKe,liMedis = null,liInsuredNo,liStsPolis = null,liCbayar,liLbayar,liLTanggung,liInsMonth,liFlat;
		Integer liTypeReas = null;
		String lsRegSpaj,lsKursId = null;
		Double ldecUp = null,ldecBunga,ldecReasComp;
		Double ldecQsReas[]=new Double[5];
		int liCnt[] = new int[10], liAge[] = new int[10];
		Double[][][] ldecReasLf=new Double[3][6][3]; // dec{2} pada pb
		Double[][][] ldecReasPa=new Double[3][6][4]; // dec{2} pada pb
		Double[][][] ldecReasPk=new Double[3][6][3]; // dec{2} pada pb
		Double[][][] ldecReasSs=new Double[3][6][3]; // dec{2} pada pb
		Double[][][] ldecReasCp=new Double[3][6][3]; // dec{2} pada pb
		Double ldecExtMort[] = new Double[10]; //dec{3}
		Double ldecKurs,ldecTsi = null,ldecSar,ldecReasSar,ldecOwnSar = null;
		//Double ldecReasHcp[]=new Double [5]; //simultan,tsi,sar,retensi,reas

		Date begDate;
		Integer liTypeBisnis;
		Integer paramTypeBisnis=new Integer(1);
		int posDoc[] = new int[3];
		Integer lspdId=new Integer(2);
		Integer lstbId=new Integer(1);
		Integer umurPp,umurTt;
		String lcaId;
		String lsOldSpaj,lsClient,lkuId,noPolis;
		Integer liSimultanNo;
		Integer liPHolder = null,liStsAksep;
		int llRow[] = new int[10];
		List ldsProd;
		
		posDoc[1]=2;
		posDoc[2]=10;
		String msteInsured,mspoPolicyHolder;
		Integer insuredNo;
		
		//data usulan asuransi
		Map mDataUsulan=selectDataUsulan(spaj);
		begDate=(Date)mDataUsulan.get("MSTE_BEG_DATE");
		//tertanggung
		Map mTertanggung=selectTertanggung(spaj);
		insuredNo=(Integer)mTertanggung.get("MSTE_INSURED_NO");
		msteInsured=(String)mTertanggung.get("MCL_ID");
		umurTt=(Integer)mTertanggung.get("MSTE_AGE");
		//pemegang
		Policy policy=selectDw1Underwriting(spaj,lspdId,lstbId);
		mspoPolicyHolder=policy.getMspo_policy_holder();
		noPolis=policy.getMspo_policy_no();
		//insPeriod=policy.getMspo_ins_period();
		//payPeriod=policy.getMspo_pay_period();
		lkuId=policy.getLku_id();
		umurPp=policy.getMspo_age();
		//lcaId=policy.getLca_id();
		
		//Deklarasi variabel Array
		for(liCnt[1]=1;liCnt[1]<=2;liCnt[1]++){	// 1=inssured ; 2=policy-holder
			for(liCnt[2]=1;liCnt[2]<=5;liCnt[2]++) {// 1=total-sar-simultan ; 2=tsi ; 3=resiko-awal ; 4=retensi ; 5=sum-reas
				ldecReasLf [liCnt[1]][liCnt[2]][1]=new Double(0);  //term
				ldecReasLf [liCnt[1]] [liCnt[2]] [2]=new Double(0);  //life
				ldecReasPa [liCnt[1]] [liCnt[2]] [1]=new Double(0);  //ssp
				ldecReasPa [liCnt[1]] [liCnt[2]] [2]=new Double(0);  //pa include (risk-a)
				ldecReasPa [liCnt[1]] [liCnt[2]] [3]=new Double(0);  //pa rider
				ldecReasPk [liCnt[1]] [liCnt[2]] [1]=new Double(0);  //pk include
				ldecReasPk [liCnt[1]] [liCnt[2]] [2]=new Double(0);  //pk rider
				ldecReasSs [liCnt[1]] [liCnt[2]] [1]=new Double(0);  //ssh,ss,ss+
				ldecReasCp [liCnt[1]] [liCnt[2]] [1]=new Double(0);  //cash plan
			}
		}
		liCnt[4]=0;
		ldecExtMort[1]=new Double(0);
		ldecKurs=selectGetKursReins("02",begDate);
		//
		Integer jum=selectCountMReasTemp(spaj);
		// delete table m_reas_temp dan sar temp
		if(jum>0){
			deleteMReasTemp(spaj);
			deleteMSarTemp(spaj);
		}
		//
		for(liCnt[1]=1;liCnt[1]<=2;liCnt[1]++){// 1=inssured ; 2=policy-holder 	
			for(liCnt[2]=1;liCnt[2]<=2;liCnt[2]++) {	// 1=simultan ; 2=now				
 				if(liCnt[2]==1){
					lsOldSpaj=null;
					liSimultanNo=null;
					if (liCnt[1] == 1)
						lsClient = msteInsured;
					else	
						lsClient = mspoPolicyHolder;
					//
					lsOldSpaj=selectMstCancelRegSpaj(spaj);
					if(lsOldSpaj!=null)
						liSimultanNo=selectMinMstSimultaneous(lsOldSpaj,lsClient);
					if(liSimultanNo==null)
						liSimultanNo=new Integer(0);
					//
					ldsProd=selectDdsSar(lsClient,liSimultanNo);
					llRow[1]=ldsProd.size();
				}else{
					ldsProd=selectDdsSarn(spaj,insuredNo);
					llRow[1]=ldsProd.size();
				}
				//
				for (liCnt[3] = 1;liCnt[3]<=llRow[1];liCnt[3]++){ // per-detil product
					D_DS_Sar dataProd=(D_DS_Sar)ldsProd.get(liCnt[3]-1);
					liBisnisId[1]=dataProd.getLsbs_id();
					liBisnisNo[1]=dataProd.getLsdbs_number();
					liReasClient[1]=dataProd.getLsdbs_reas_client();
					if(liBisnisId[1]<600){//pokok
						liThKe= new Integer(selectTahunKe(defaultDateFormat.format(dataProd.getMste_beg_date()),defaultDateFormat.format(begDate)).intValue());
						liMedis=dataProd.getMste_medical();
						lsRegSpaj=dataProd.getReg_spaj();
						liInsuredNo=dataProd.getMste_insured_no();
						noPolis=dataProd.getMspo_policy_no();
						liStsPolis=dataProd.getLssp_id();
						liCbayar=dataProd.getLscb_id();
						liLbayar=dataProd.getMspo_pay_period();
						liLTanggung=dataProd.getMspo_ins_period();
						liInsMonth=dataProd.getMspo_ins_bulan();
						liFlat=dataProd.getMspo_flat();
						ldecBunga=dataProd.getMspr_bunga_kpr();
						liTypeBisnis=dataProd.getLstb_id();
						liTypeReas=dataProd.getLstr_id();
						ldecQsReas[1]=dataProd.getLstr_quota_reas();
						lsKursId=dataProd.getLku_id();
						ldecUp=dataProd.getMspr_tsi();
						liBisnisId[2]=liBisnisId[1];// assign main_bisnis_id
						liBisnisNo[2]=liBisnisNo[1];// assign main_bisnis_no
						//
						if(liCnt[2] == 1){
							liPHolder= dataProd.getMssm_pemegang();
							liStsAksep = dataProd.getLssa_id();
							if (liStsPolis == 10 && liStsAksep != 5){
								if(liStsAksep!= 8){
									err.reject("","Pending Reins Proccess until Simultan Policy Accepted, Polis Simultan (SPAJ= "+lsRegSpaj+") Belum Ada Backup Facultative-Nya");
									return null;
								}
							}
						}else{
							ldecQsReas[2]   = ldecQsReas[1];
							liReasClient[2] = liReasClient[1];
							liAge[1] = umurTt;
							liAge[2] = umurTt;
							if (liReasClient[2] == 2 || liReasClient[2] == 3)
								liAge[2] = umurPp;
							if (liCnt[1] == 1 )
								liPHolder = new Integer(0) ;
							else 
								liPHolder = new Integer(1);
						}
						//
						ldecTsi=selectGetTsiReas(spaj,insuredNo,liPHolder,liBisnisId[2],liBisnisNo[2],liBisnisId[1],
								liBisnisNo[1],lsKursId,ldecUp,err);
						if(ldecTsi==null)
							return null;
						if(ldecTsi.doubleValue() == 0)
							continue;
						//
						if(liReasClient[1]== 1 && liPHolder ==0  || liReasClient[1]== 2 && 
								liPHolder ==1 || liReasClient[1] > 2){
							if(liTypeBisnis==1){//individu
								ldecRateSar[1] = selectGetSar(new Integer(1), liBisnisId[1], liBisnisNo[1], lsKursId, liCbayar, liLbayar, liLTanggung, liThKe, umurTt);
								ldecRateSar[2] = selectGetSar(new Integer(2), liBisnisId[1], liBisnisNo[1], lsKursId, liCbayar, liLbayar, liLTanggung, liThKe, umurTt);
							}else if(liTypeBisnis==2){//MRI
								if(liInsMonth==null) 
									liInsMonth =new Integer(0);
								//
								ldecRateSar[1] = selectGetMriSar(new Integer(1), liBisnisId[1], liFlat, ldecBunga, 
															new Integer( (liLTanggung * 12) + liInsMonth), liThKe,err);
								ldecRateSar[2] = selectGetMriSar(new Integer(2), liBisnisId[1], liFlat, ldecBunga, 
															new Integer( (liLTanggung * 12) + liInsMonth), liThKe,err);
							}
							//
							if (ldecRateSar[1]==null || ldecRateSar[2]==null )
								return null;
							//
							ldecSar      = new Double(ldecTsi.doubleValue() * ldecRateSar[1].doubleValue() / 1000);
							if (liCnt[2] == 1)
								ldecSar = new Double(ldecSar.doubleValue() * ldecRateSar[2].doubleValue() / 1000);
							if (liBisnisId[1] == 66 || liBisnisId[1] == 79 || 
									liBisnisId[1] == 80)
								ldecSar = new Double(ldecSar.doubleValue() * 1.5); //procare,SEHAT,PRIVACY
							//
							ldecReasSar = new Double(ldecSar.doubleValue() * ldecQsReas[1].doubleValue() / 100);
							ldecOwnSar  = new Double(ldecSar.doubleValue() - ldecReasSar.doubleValue());
							//
							if( liBisnisId[1]==43 || liBisnisId[1]==53 ||// simas sehat harian
								liBisnisId[1]==54 || liBisnisId[1]==67){
								if (liCnt[2]== 1)
									ldecReasSs[liCnt[1]][1][1] =new Double( ldecReasSs[liCnt[1]][1][1].doubleValue() +  
																			selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2)
									ldecReasSs[liCnt[1]][2][1] =new Double( ldecReasSs[liCnt[1]][2][1].doubleValue() + 
																			ldecTsi.doubleValue() );
								if (liCnt[2] == 2)
									ldecReasSs[liCnt[1]][3][1] =new Double( ldecReasSs[liCnt[1]][3][1].doubleValue()+ 
																			ldecSar.doubleValue() / 100 );//bc
							}else if(liBisnisId[1]==45 ||liBisnisId[1]==130){	 // simas super protection
								if (liCnt[2]== 1)
									ldecReasPa[liCnt[1]][1][1] =new Double( ldecReasPa[liCnt[1]][1][1].doubleValue() + 
																			selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2)
									ldecReasPa[liCnt[1]][2][1] =new Double( ldecReasPa[liCnt[1]][2][1].doubleValue() + 
																			ldecTsi.doubleValue() );
								if (liCnt[2] == 2)
									ldecReasPa[liCnt[1]][3][1] =new Double( ldecReasPa[liCnt[1]][3][1].doubleValue()+ 
																			ldecSar.doubleValue() );
							}else{//others
								if (liCnt[2]== 1)
									ldecReasLf[liCnt[1]][1][2] =new Double( ldecReasLf[liCnt[1]][1][2].doubleValue() + 
																			selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2)
									ldecReasLf[liCnt[1]][2][2] =new Double( ldecReasLf[liCnt[1]][2][2].doubleValue() + 
																			ldecTsi.doubleValue() );
								if (liCnt[2] == 2)
									ldecReasLf[liCnt[1]][3][2] =new Double( ldecReasLf[liCnt[1]][3][2].doubleValue()+ 
																			ldecSar.doubleValue() );
							}
							//
							if (abAdd)
								aiCnt=insertMSarTemp(spaj,noPolis,liBisnisId[1],liBisnisNo[1],lsKursId,ldecSar,
										liStsPolis,liMedis,aiCnt,dataProd.getReg_spaj_ref());
						}
						//rider include
						List ldsRiderInclude=selectd_ds_rider_include(liBisnisId[2],liBisnisNo[2],lsKursId);
						for(int t=0;t<ldsRiderInclude.size();t++){
							ParameterClass rider=(ParameterClass)ldsRiderInclude.get(t);
							liBisnisId[1]    = rider.getLst_lsbs_id();
							liBisnisNo[1]   = rider.getLst_lsdbs_number();
							liReasClient[1] = rider.getLsdbs_reas_client();
							if(liReasClient[1]==null)
								liReasClient[1]=new Integer(0);
							
							if(liReasClient[1] == 1 && liPHolder == 0  || 
								liReasClient[1]== 2 && liPHolder == 1 || liReasClient[1] > 2 ){
								ldecTsi =selectGetTsiReas(spaj,insuredNo,liPHolder,liBisnisId[2],liBisnisNo[2],
											liBisnisId[1],liBisnisNo[1],lsKursId,ldecUp,err);
								if(ldecTsi==null)
									return null;
								//
								//procare
								if(liBisnisId[1] == 601 && liBisnisId[2] == 54)
									ldecSar = new Double(100 * ldecTsi.doubleValue());
								else 
									ldecSar = ldecTsi;
								//
								ldecReasSar = new Double(ldecSar.doubleValue() * ldecQsReas[1].doubleValue()/100);	
								ldecOwnSar  = new Double(ldecSar.doubleValue() - ldecReasSar.doubleValue());
								if(liBisnisId[1]==600){// PA Include Resiko A
									if (liCnt[2]== 1)
										ldecReasPa[liCnt[1]][1][2] =new Double( ldecReasPa[liCnt[1]][1][2].doubleValue() + 
																				selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
									if (liCnt[2] == 2)
										ldecReasPa[liCnt[1]][2][2] =new Double( ldecReasPa[liCnt[1]][2][2].doubleValue() + 
																				ldecTsi.doubleValue() );
									if (liCnt[2] == 2)
										ldecReasPa[liCnt[1]][3][2] =new Double( ldecReasPa[liCnt[1]][3][2].doubleValue()+ 
																				ldecSar.doubleValue() );
								}else if(liBisnisId[1]==601){// PK Include
									if (liCnt[2]== 1)
										ldecReasPk[liCnt[1]][1][1] =new Double( ldecReasPk[liCnt[1]][1][1].doubleValue() + 
																				selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
									if (liCnt[2] == 2)
										ldecReasPk[liCnt[1]][2][1] =new Double( ldecReasPk[liCnt[1]][2][1].doubleValue() + 
																				ldecTsi.doubleValue() );
									if (liCnt[2] == 2)
										ldecReasPk[liCnt[1]][3][1] =new Double( ldecReasPk[liCnt[1]][3][1].doubleValue()+ 
																				ldecSar.doubleValue() );
								}else if(liBisnisId[1]>=806 && liBisnisId[1]<=808){/// CASH PLAN, tpd
									if (liCnt[2]== 1)
										ldecReasCp[liCnt[1]][1][1] =new Double( ldecReasCp[liCnt[1]][1][1].doubleValue() + 
																				selectKursAdjust(ldecOwnSar,ldecKurs,lkuId,lsKursId));
									if (liCnt[2] == 2)
										ldecReasCp[liCnt[1]][2][1] =new Double( ldecReasCp[liCnt[1]][2][1].doubleValue() + 
																				ldecTsi.doubleValue() );
									if (liCnt[2] == 2)
										ldecReasCp[liCnt[1]][3][1] =new Double( ldecReasCp[liCnt[1]][3][1].doubleValue()+ 
																				ldecSar.doubleValue() );
									
								}else{
									err.reject("","Please Contact ITwebandmobile@sinarmasmsiglife.co.id. Aplikasi belum dimodifikasi utk rider include (Bisnis Id="+liBisnisId[1]+" )");
									return null;
								}
								if (abAdd)
									aiCnt=insertMSarTemp(spaj,noPolis,liBisnisId[1],liBisnisNo[1],lsKursId,ldecSar,
											liStsPolis,liMedis,aiCnt,dataProd.getReg_spaj_ref());
							}
						}
					//rider	
					}else if(liBisnisId[1]==800 || liBisnisId[1]==801 || liBisnisId[1]==803){
						if (liReasClient[1] == 1 && liPHolder == 0  || liReasClient[1] == 2 && 
								liPHolder == 1 || liReasClient[1] > 2){
							ldecTsi =selectGetTsiReas(spaj,insuredNo,liPHolder,liBisnisId[2],liBisnisNo[2],
									liBisnisId[1],liBisnisNo[1],lsKursId,ldecUp,err);
							if(ldecTsi==null)
								return null;
							ldecSar=ldecTsi;
							//
							if (abAdd)
								aiCnt=insertMSarTemp(spaj,noPolis,liBisnisId[1],liBisnisNo[1],lsKursId,ldecSar,
										liStsPolis,liMedis,aiCnt,dataProd.getReg_spaj_ref());
							//
							if(liBisnisId[1]==800){// PA Rider
								if (liCnt[2]== 1)
									ldecReasPa[liCnt[1]][1][3] =new Double( ldecReasPa[liCnt[1]][1][3].doubleValue() + 
																			selectKursAdjust(ldecSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2)
									ldecReasPa[liCnt[1]][2][3] =new Double( ldecReasPa[liCnt[1]][2][3].doubleValue() + 
																			ldecTsi.doubleValue() );
								if (liCnt[2] == 2)
									ldecReasPa[liCnt[1]][3][3] =new Double( ldecReasPa[liCnt[1]][3][3].doubleValue()+ 
																			ldecSar.doubleValue() );
								
							}else if(liBisnisId[1]==801){// PK Rider
								if (liCnt[2]== 1)
									ldecReasPk[liCnt[1]][1][2] =new Double( ldecReasPk[liCnt[1]][1][2].doubleValue() + 
																			selectKursAdjust(ldecSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2)
									ldecReasPk[liCnt[1]][2][2] =new Double( ldecReasPk[liCnt[1]][2][2].doubleValue() + 
																			ldecTsi.doubleValue() );
								if (liCnt[2] == 2)
									ldecReasPk[liCnt[1]][3][2] =new Double( ldecReasPk[liCnt[1]][3][2].doubleValue()+ 
																			ldecSar.doubleValue() );
							}else if(liBisnisId[1]==803){// Term Rider
								if (liCnt[2]== 1)
									ldecReasLf[liCnt[1]][1][1] =new Double( ldecReasLf[liCnt[1]][1][1].doubleValue() + 
																			selectKursAdjust(ldecSar,ldecKurs,lkuId,lsKursId));
								if (liCnt[2] == 2)
									ldecReasLf[liCnt[1]][2][1] =new Double( ldecReasLf[liCnt[1]][2][1].doubleValue() + 
																			ldecTsi.doubleValue() );
								if (liCnt[2] == 2)
									ldecReasLf[liCnt[1]][3][1] =new Double( ldecReasLf[liCnt[1]][3][1].doubleValue()+ 
																			ldecSar.doubleValue() );
							}
						}
					}else if(liCnt[2]==2 && liBisnisId[1]==900 ){//Extra Mortalita
						ldecExtMort[1]=dataProd.getMspr_extra();
						if(ldecExtMort[1]==null)
							ldecExtMort[1]=new Double(0);
						ldecExtMort[2]=selectLstEmMaxLsemMaxLsemValue(paramTypeBisnis,liTypeReas,begDate);
						if(ldecExtMort[2]==null){
							err.reject("","EM Max not found Bisnis="+paramTypeBisnis+" reins="+liTypeReas+" begdate="+defaultDateFormat.format(begDate));
							return null;
						}
						if (ldecExtMort[1].doubleValue()> ldecExtMort[2].doubleValue()){
							ldecQsReas[2] = new Double(Math.max( 50, ldecQsReas[1].doubleValue() ) );
							liReas = new Integer(2);
						}
					}
				/*	if(liBisnisId[1]==811){//HCP
						ldecReasHcp[1]=dataProd.getMspr_tsi();
						ldecReasHcp[2]=dataProd.getMspr_tsi()/2;
						ldecReasHcp[4]=dataProd.getMspr_tsi()/2;
						ldecReasHcp[3] = selectGetRetensi(liBisnisId[2],new Integer(1),new Integer(1),liMedis,lkuId,begDate,new Integer(liAge[liCnt[1]]),err); //Retensi limit
						
					}*/
						
						
				}	
			}
			/* Hitung Retensi & Sum-Reas */
			// reas_life
			if(ldecReasLf[liCnt[1]][3][1].doubleValue() + ldecReasLf[liCnt[1]][3][2].doubleValue() > 0){
				ldecLimit[1] = selectGetRetensi(liBisnisId[2], liBisnisNo[2],new Integer(1),new Integer(1),liMedis,lkuId,begDate,new Integer(liAge[liCnt[1]]),spaj,err); //Retensi limit
				ldecLimit[2] = selectGetRetensi(liBisnisId[2], liBisnisNo[2],new Integer(1),new Integer(2),liMedis,lkuId,begDate,new Integer(liAge[liCnt[1]]),spaj,err); //UW limit
				//
				if(ldecLimit[1]==null || ldecLimit[2]==null )
					return null;
				// untuk reas cth kasus.
				//mis ada 3 polis power save maka
				//polis 1 tsi=700 jt retensi=500 maka reas=350jt ekalife=350jt
				//polis 2 tsi 200 jt retensi=500 maka reas=100 jt ekalife 100 jt
				//polis 3 tsi 600 jt retensi=500-350+100(ekalife)
				//khsusus power save itu <500 jt non reas ato qouta reas=0
				String prodSave=" 86,94,105,106,123,124,142,155,158,175";
				if(prodSave.indexOf(""+liBisnisId[1])>0 && ldecTsi >ldecLimit[1])
					ldecQsReas[2]=new Double(50);
				//kalo lapse ,maturiy dan claim cash value tidak hitung 
				ldecReasLf[liCnt[1]][4][1] = new Double( Math.max( 0, ldecLimit[1].doubleValue() - ldecReasLf[liCnt[1]][1][1].doubleValue() - 
																      ldecReasLf[liCnt[1]][1][2].doubleValue() ) );
				ldecReasLf[liCnt[1]][5][1] = new Double(Math.max( ldecReasLf[liCnt[1]][3][1].doubleValue() * ldecQsReas[2].doubleValue() / 100, 
																   ldecReasLf[liCnt[1]][3][1].doubleValue() - ldecReasLf[liCnt[1]][4][1].doubleValue() )) ;
				//retensi=_life=simultan_life-sar_life-reas_life
				ldecReasLf[liCnt[1]][4][2] = new Double(Math.max( 0, ldecReasLf[liCnt[1]][4][1].doubleValue() - ( ldecReasLf[liCnt[1]][3][1].doubleValue() -
																	 ldecReasLf[liCnt[1]][5][1].doubleValue() ) ));		
				ldecReasLf[liCnt[1]][5][2] = new Double(Math.max( ldecReasLf[liCnt[1]][3][2].doubleValue() * ldecQsReas[2].doubleValue() / 100, 
																  ldecReasLf[liCnt[1]][3][2].doubleValue() - ldecReasLf[liCnt[1]][4][2].doubleValue() ));
				//
				if (ldecReasLf[liCnt[1]][3][1].doubleValue() == 0)
					ldecReasLf[liCnt[1]][4][1] = new Double(0);
				if (ldecReasLf[liCnt[1]][3][2].doubleValue() == 0) 
					ldecReasLf[liCnt[1]][4][2] = new Double(0);
				if ( (ldecReasLf[liCnt[1]][1][1].doubleValue()  + ldecReasLf[liCnt[1]][1][2].doubleValue() + 
						ldecReasLf[liCnt[1]][3][1].doubleValue() + ldecReasLf[liCnt[1]][3][2].doubleValue()) >ldecLimit[2].doubleValue()) 
						liReas = new Integer(2);
			}
			// reas_pa
			if (ldecReasPa[liCnt[1]][3][1].doubleValue() + ldecReasPa[liCnt[1]][3][2].doubleValue() + 
					ldecReasPa[liCnt[1]][3][3].doubleValue() > 0){
				if ( (ldecQsReas[1].doubleValue() > 0) && (ldecReasPa[liCnt[1]][3][1].doubleValue() == 0) ){
					ldecLimit[1] = selectGetRetensi(liBisnisId[2], liBisnisNo[2], new Integer(3),new Integer(1),liMedis,lkuId,begDate,new Integer(liAge[liCnt[1]]),spaj,err); 
					ldecLimit[2] = selectGetRetensi(liBisnisId[2], liBisnisNo[2], new Integer(3),new Integer(2),liMedis,lkuId,begDate,new Integer(liAge[liCnt[1]]),spaj,err); 
				}else{
					ldecLimit[1] = selectGetRetensi(liBisnisId[2], liBisnisNo[2], new Integer(1),new Integer(1),liMedis,lkuId,begDate,new Integer(liAge[liCnt[1]]),spaj,err); 
					ldecLimit[2] = selectGetRetensi(liBisnisId[2], liBisnisNo[2], new Integer(1),new Integer(2),liMedis,lkuId,begDate,new Integer(liAge[liCnt[1]]),spaj,err); 
				}
				if(ldecLimit[1]==null || ldecLimit[2]==null )
					return null;
				//
				ldecReasPa[liCnt[1]][4][1] = new Double(Math.max( 0, ldecLimit[1].doubleValue() - ldecReasPa[liCnt[1]][1][1].doubleValue() - 
																	 ldecReasPa[liCnt[1]][1][2].doubleValue() - ldecReasPa[liCnt[1]][1][3].doubleValue() ));
				ldecReasPa[liCnt[1]][5][1] = new Double(Math.max( ldecReasPa[liCnt[1]][3][1].doubleValue() * ldecQsReas[2].doubleValue() / 100, 
																  ldecReasPa[liCnt[1]][3][1].doubleValue() - ldecReasPa[liCnt[1]][4][1].doubleValue() ));
				ldecReasPa[liCnt[1]][4][2] = new Double(Math.max( 0, ldecReasPa[liCnt[1]][4][1].doubleValue() - ( ldecReasPa[liCnt[1]][3][1].doubleValue() - 
																	 ldecReasPa[liCnt[1]][5][1].doubleValue() ) ));
				ldecReasPa[liCnt[1]][5][2] = new Double(Math.max( ldecReasPa[liCnt[1]][3][2].doubleValue() * ldecQsReas[2].doubleValue() / 100, 
																  ldecReasPa[liCnt[1]][3][2].doubleValue() - ldecReasPa[liCnt[1]][4][2].doubleValue() ));
				ldecReasPa[liCnt[1]][4][3] = new Double(Math.max( 0, ldecReasPa[liCnt[1]][4][2].doubleValue() - 
																   ( ldecReasPa[liCnt[1]][3][2].doubleValue() - ldecReasPa[liCnt[1]][5][2].doubleValue() ) ));
				ldecReasPa[liCnt[1]][5][3] = new Double(Math.max( ldecReasPa[liCnt[1]][3][3].doubleValue() * ldecQsReas[2].doubleValue() / 100, 
																  ldecReasPa[liCnt[1]][3][3].doubleValue() - ldecReasPa[liCnt[1]][4][3].doubleValue() ));
				//
				if (ldecReasPa[liCnt[1]][3][1].doubleValue() == 0)
					ldecReasPa[liCnt[1]][4][1] = new Double(0);
				if (ldecReasPa[liCnt[1]][3][2].doubleValue() == 0)
					ldecReasPa[liCnt[1]][4][2] = new Double(0);
				if (ldecReasPa[liCnt[1]][3][3].doubleValue() == 0)
					ldecReasPa[liCnt[1]][4][3] = new Double(0);
				if ( (ldecReasPa[liCnt[1]][1][1].doubleValue() + ldecReasPa[liCnt[1]][1][2].doubleValue() 
						+ ldecReasPa[liCnt[1]][1][3].doubleValue() + ldecReasPa[liCnt[1]][3][1].doubleValue()
						+ ldecReasPa[liCnt[1]][3][2].doubleValue() + ldecReasPa[liCnt[1]][3][3].doubleValue())
						> ldecLimit[2].doubleValue() )
					liReas = new Integer(2);
			}
			// reas_pk
			if( ldecReasPk[liCnt[1]][3][1].doubleValue() + ldecReasPk[liCnt[1]][3][2].doubleValue() > 0){
				if (ldecQsReas[1].doubleValue() > 0){
					ldecLimit[1] = selectGetRetensi(liBisnisId[2], liBisnisNo[2], new Integer(4),new Integer(1),liMedis,lkuId,begDate,new Integer(liAge[liCnt[1]]),spaj,err); 
					ldecLimit[2] = selectGetRetensi(liBisnisId[2], liBisnisNo[2], new Integer(4),new Integer(2),liMedis,lkuId,begDate,new Integer(liAge[liCnt[1]]),spaj,err); 
				}else{
					ldecLimit[1] = selectGetRetensi(liBisnisId[2], liBisnisNo[2], new Integer(2),new Integer(1),liMedis,lkuId,begDate,new Integer(liAge[liCnt[1]]),spaj,err); 
					ldecLimit[2] = selectGetRetensi(liBisnisId[2], liBisnisNo[2], new Integer(2),new Integer(2),liMedis,lkuId,begDate,new Integer(liAge[liCnt[1]]),spaj,err); 
				}
				if(ldecLimit[1]==null || ldecLimit[2]==null )
					return null;
				//
				ldecReasPk[liCnt[1]][4][1] = new Double(Math.max( 0, ldecLimit[1].doubleValue() - ldecReasPk[liCnt[1]][1][1].doubleValue() 
																	 - ldecReasPk[liCnt[1]][1][2].doubleValue() ));
				ldecReasPk[liCnt[1]][5][1] = new Double(Math.max( (ldecReasPk[liCnt[1]][3][1].doubleValue() * ldecQsReas[2].doubleValue() / 100), 
																   (ldecReasPk[liCnt[1]][3][1].doubleValue() - ldecReasPk[liCnt[1]][4][1].doubleValue()) ));
				ldecReasPk[liCnt[1]][4][2] = new Double(Math.max( 0, ldecReasPk[liCnt[1]][4][1].doubleValue() - 
																	( ldecReasPk[liCnt[1]][3][1].doubleValue() - ldecReasPk[liCnt[1]][5][1].doubleValue() ) ));
				ldecReasPk[liCnt[1]][5][2] = new Double(Math.max( (ldecReasPk[liCnt[1]][3][2].doubleValue() * ldecQsReas[2].doubleValue() / 100),
																   (ldecReasPk[liCnt[1]][3][2].doubleValue() - ldecReasPk[liCnt[1]][4][2].doubleValue()) ));
				//
				if (ldecReasPk[liCnt[1]][3][1].doubleValue() == 0)
					ldecReasPk[liCnt[1]][4][1] = new Double(0);
				if (ldecReasPk[liCnt[1]][3][2].doubleValue() == 0)
					ldecReasPk[liCnt[1]][4][2] = new Double(0);
				if ( (ldecReasPk[liCnt[1]][1][1].doubleValue() + ldecReasPk[liCnt[1]][1][2].doubleValue()
						+ ldecReasPk[liCnt[1]][3][1].doubleValue() + ldecReasPk[liCnt[1]][3][2].doubleValue()) 
						> ldecLimit[2].doubleValue())
						liReas = new Integer(2);
			}
			//806, 807 = cash plan, tpd
			if (ldecReasCp[liCnt[1]][2][1].doubleValue() > 0){
				ldecReasCp[liCnt[1]][5][1] = new Double( ldecReasCp[liCnt[1]][3][1].doubleValue() * ldecQsReas[2].doubleValue() / 100);
				ldecReasCp[liCnt[1]][4][1] = ldecReasCp[liCnt[1]][5][1];
			}
			// simas sehat harian
			if (ldecReasSs[liCnt[1]][3][1].doubleValue() > 0){
				ldecReasSs[liCnt[1]][5][1] = new Double(ldecReasSs[liCnt[1]][3][1].doubleValue() * ldecQsReas[2].doubleValue() / 100);
				ldecReasSs[liCnt[1]][4][1] = new Double(ldecReasSs[liCnt[1]][3][1].doubleValue() - ldecReasSs[liCnt[1]][5][1].doubleValue() );
			}
			ldecTotalReas[liCnt[1]] = new Double(ldecReasLf[liCnt[1]][5][1].doubleValue() + ldecReasLf[liCnt[1]][5][2].doubleValue() 
										+ ldecReasPa[liCnt[1]][5][1].doubleValue() + ldecReasPa[liCnt[1]][5][2].doubleValue()
										+ ldecReasPa[liCnt[1]][5][3].doubleValue() + ldecReasPk[liCnt[1]][5][1].doubleValue() 
										+ ldecReasPk[liCnt[1]][5][2].doubleValue() + ldecReasSs[liCnt[1]][5][1].doubleValue() 
										+ ldecReasCp[liCnt[1]][5][1].doubleValue()); 
			if(ldecTotalReas[liCnt[1]].doubleValue() > 0 )
				liReas= new Integer(Math.max( 1, liReas));
		}
		//
		if(liReasClient[2].doubleValue() == 3){
			if(ldecTsi==null)
				return null;
			Double tempTsi1=selectGetTsiReas(spaj,insuredNo,new Integer(0),liBisnisId[2],liBisnisNo[2],
							liBisnisId[2],liBisnisNo[2],lsKursId,ldecUp,err);
			Double tempTsi2=selectGetTsiReas(spaj,insuredNo,new Integer(1),liBisnisId[2],liBisnisNo[2],
							liBisnisId[2],liBisnisNo[2],lsKursId,ldecUp,err);
			if(tempTsi1==null || tempTsi2==null)
				return null;
			ldecReasComp=new Double(tempTsi1.doubleValue()/tempTsi2.doubleValue());
			ldecReasLf[1][5][2] = new Double(Math.max( ldecReasLf[1][5][2].doubleValue(), ldecReasLf[2][5][2].doubleValue() * ldecReasComp.doubleValue() ));
			ldecReasLf[2][5][2] = new Double(ldecReasLf[1][5][2].doubleValue() / ldecReasComp.doubleValue());
		}
		//
		if(abAdd){
			for(liCnt[1]=1;liCnt[1]<=2;liCnt[1]++){
				if( (liReasClient[2] == liCnt[1]) || (liReasClient[2].doubleValue() > 2 ) ){
					if ( (liReasClient[2].doubleValue() > 2 ) && (liCnt[1] == 2 ) &&
							(msteInsured.equals(mspoPolicyHolder))) 
						return null;
					ReasTemp insReas=new ReasTemp();
					//
					insReas.setReg_spaj(spaj);
					insReas.setPemegang(liCnt[1]-1);
					insReas.setMste_reas(liReas);
					insReas.setExtra_mortality(ldecExtMort[1]);
					insReas.setNil_kurs(ldecKurs.intValue());
					insReas.setLku_id(lkuId);
					//
					insReas.setSimultan_tr_rd(ldecReasLf[liCnt[1]][1][1]);
					insReas.setTsi_tr_rd(ldecReasLf[liCnt[1]][2][1]);
					insReas.setSar_tr_rd(ldecReasLf[liCnt[1]][3][1]);
					insReas.setRetensi_tr_rd(ldecReasLf[liCnt[1]][4][1]);
					insReas.setReas_tr_rd(ldecReasLf[liCnt[1]][5][1] );
					//
					insReas.setSimultan_life(ldecReasLf[liCnt[1]][1][2]);
					insReas.setTsi_life(ldecReasLf[liCnt[1]][2][2]);
					insReas.setSar_life(ldecReasLf[liCnt[1]][3][2]);
					insReas.setRetensi_life(ldecReasLf[liCnt[1]][4][2]);
					insReas.setReas_life(ldecReasLf[liCnt[1]][5][2]);
					//
					insReas.setSimultan_ssp(ldecReasPa[liCnt[1]][1][1]);
					insReas.setTsi_ssp(ldecReasPa[liCnt[1]][2][1]);
					insReas.setSar_ssp(ldecReasPa[liCnt[1]][3][1]);
					insReas.setRetensi_ssp(ldecReasPa[liCnt[1]][4][1]);
					insReas.setReas_ssp(ldecReasPa[liCnt[1]][5][1]);
					//
					insReas.setSimultan_pa_in(ldecReasPa[liCnt[1]][1][2]);
					insReas.setTsi_pa_in(ldecReasPa[liCnt[1]][2][2]);
					insReas.setSar_pa_in(ldecReasPa[liCnt[1]][3][2]);
					insReas.setRetensi_pa_in(ldecReasPa[liCnt[1]][4][2]);
					insReas.setReas_pa_in(ldecReasPa[liCnt[1]][5][2]);
					//
					insReas.setSimultan_pa_rd(ldecReasPa[liCnt[1]][1][3]);
					insReas.setTsi_pa_rd(ldecReasPa[liCnt[1]][2][3]);
					insReas.setSar_pa_rd(ldecReasPa[liCnt[1]][3][3]);
					insReas.setRetensi_pa_rd(ldecReasPa[liCnt[1]][4][3]);
					insReas.setReas_pa_rd(ldecReasPa[liCnt[1]][5][3]);
					//
					insReas.setSimultan_pk_in(ldecReasPk[liCnt[1]][1][1]);
					insReas.setTsi_pk_in(ldecReasPk[liCnt[1]][2][1]);
					insReas.setSar_pk_in(ldecReasPk[liCnt[1]][3][1]);
					insReas.setRetensi_pk_in(ldecReasPk[liCnt[1]][4][1]);
					insReas.setReas_pk_in(ldecReasPk[liCnt[1]][5][1]);
					//
					insReas.setSimultan_pk_rd(ldecReasPk[liCnt[1]][1][2]);
					insReas.setTsi_pk_rd(ldecReasPk[liCnt[1]][2][2]);
					insReas.setSar_pk_rd(ldecReasPk[liCnt[1]][3][2]);
					insReas.setRetensi_pk_rd(ldecReasPk[liCnt[1]][4][2]);
					insReas.setReas_pk_rd(ldecReasPk[liCnt[1]][5][2]);
					//
					insReas.setSimultan_ssh(ldecReasSs[liCnt[1]][1][1]);
					insReas.setTsi_ssh(ldecReasSs[liCnt[1]][2][1]);
					insReas.setSar_ssh(ldecReasSs[liCnt[1]][3][1]);
					insReas.setRetensi_ssh(ldecReasSs[liCnt[1]][4][1]);
					insReas.setReas_ssh(ldecReasSs[liCnt[1]][5][1]);
					insReas.setTipe(0);
/*					insReas.setSimultan_hcp(ldecReasHcp[0]);
					insReas.setTsi_hcp(ldecReasHcp[1]);
					insReas.setSar_hcp(ldecReasHcp[2]);
					insReas.setRetensi_hcp(ldecReasHcp[3]);
					insReas.setReas_hcp(ldecReasHcp[4]);*/
					insertMReasTemp(insReas);
				}
			}
		}


		return liReas;
	}


	public Double selectGetKursReins(String kurs,Date begDate){
		Map param=new HashMap();
		param.put("lkuId",kurs);
		param.put("begDate",defaultDateFormat.format(begDate));
		return (Double)querySingle("select.lst_reins_currency2",param);
	}
	
	public String selectMstCancelRegSpaj(String spaj){
		return (String) querySingle("select.ls_old_spaj",spaj);
	}
	
	public Integer selectMinMstSimultaneous(String spaj,String mclId){
		Map param = new HashMap();
		param.put("ls_old_spaj", spaj);
		param.put("ls_client", mclId);
		return (Integer)querySingle("select.no_simultan",param);
	
	}

	/**
	 * @deprecated 
	 */
	public List selectDdsSar(String mclId,Integer liSimultanNo){
		Map param=new HashMap();
		param.put("mclId",mclId);
		param.put("simultanNo",liSimultanNo);
		return query("reas.select.d_ds_sar",param);
	}
	
	public List selectDdsSarNew(String idSimultan, String spaj, Integer liSimultanNo){
		Map param=new HashMap();
		param.put("id_simultan",idSimultan);
		param.put("simultanNo",liSimultanNo);
		param.put("spaj", spaj);
		return query("reas.select.d_ds_sarNew",param);
	}
	
	public List selectDdsSarNewDian(String idSimultan, Integer liSimultanNo){
		Map param=new HashMap();
		param.put("id_simultan",idSimultan);
		param.put("simultanNo",liSimultanNo);
		return query("reas.select.d_ds_sarNewDian",param);
	}
	/**
	 * 
	 * Dian natalia
	 * digunakan untuk perbaikan menu report reas
	 * @param idSimultan
	 * @param spaj
	 * @param liSimultanNo
	 * @return
	 */
//	public List selectDdsSarNew1(String idSimultan, String spaj, Integer liSimultanNo){
//		Map param=new HashMap();
//		param.put("id_simultan",idSimultan);
//		param.put("simultanNo",liSimultanNo);
//		param.put("spaj", spaj);
//		return query("select.eka.m_sar_temp_new1",param);
//	}
	/**
	 * @deprecated 
	 */
	public List selectDdsSarn(String spaj,Integer insured){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("insured",insured);
		return query("reas.select.d_ds_sarn",param);
	}
	
	public List selectDdsSarnNew(String spaj,Integer insured){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("insured",insured);
		return query("reas.select.d_ds_sarnNew",param);
	}
	
	public void wfInsSimultan(String spaj,String msteInsured,String mspoPolicyHolder,Integer insured){
		 Integer l_simultan_no = null;
		    String s_client;
			for(int j=1;j<=2;j++){
				if(j== 1) 
					s_client = msteInsured; 
				else 
					s_client = mspoPolicyHolder;
				
				if((j== 1) || (!msteInsured.equals(mspoPolicyHolder))){
					l_simultan_no=selectMaxMstSimultaneousMssmNumber(s_client);
				}
				if(l_simultan_no==null)
					l_simultan_no=new Integer(0);
				
				l_simultan_no=new Integer(l_simultan_no+1);
				//
				insertMstSimultaneous(spaj,s_client,l_simultan_no,(j-1),insured);
			}
	}
	
	public void wfInsSimultanNew(String spaj,String msteInsured,String mspoPolicyHolder,Integer insured,String idSimultanPp,String idSimultanTt){
		 Integer l_simultan_no = null;
		    String s_client,idSimultan;
			for(int j=1;j<=2;j++){
				if(j== 1) {
					idSimultan=idSimultanTt;
					s_client = msteInsured; 
				}else {
					idSimultan=idSimultanPp;
					s_client = mspoPolicyHolder;
				}	
				if((j== 1) || (!msteInsured.equals(mspoPolicyHolder))){
					l_simultan_no=selectMaxMstSimultaneousMssmNumber(s_client);
				}
				if(l_simultan_no==null)
					l_simultan_no=new Integer(0);
				
				l_simultan_no=new Integer(l_simultan_no+1);
				//
				insertMstSimultaneousNew(spaj,s_client,l_simultan_no,(j-1),insured,idSimultan);
			}
	}
	
	public void insertMstSimultaneous(String spaj,String mclId,Integer SimultanNo,int value,Integer insured){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("s_client",mclId);
		param.put("l_simultan_no",SimultanNo);
		param.put("j",new Integer(value));
		param.put("mste_insured_no",insured);
		getSqlMapClientTemplate().insert("elions.uw.insert.mst_simultaneous",param);
	}
	
	public void insertMstSimultaneousNew(String spaj,String mclId,Integer SimultanNo,int value,Integer insured,String idSimultan){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("s_client",mclId);
		param.put("l_simultan_no",SimultanNo);
		param.put("j",new Integer(value));
		param.put("mste_insured_no",insured);
		param.put("id_simultan",idSimultan);
		getSqlMapClientTemplate().insert("elions.uw.insert.mst_simultaneous_new",param);
	}
	
	public Integer selectMaxMstSimultaneousMssmNumber(String mclId){
		return (Integer)querySingle("select.mst_simultaneous",mclId);
	}
	
	public void updateMstInsuredReasnBackup(String spaj,Integer insured,Integer liReas,Integer liBackup,
			Integer lssaId,String medis){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("txtli_insured_no",insured);
		param.put("li_reas",liReas);
		param.put("li_backup",liBackup);
		param.put("lssaId", lssaId);
		param.put("medis", medis);
		update("update.mst_insured",param);
	}
	
	public void wf_ins_cash_tpd(String spaj,Integer insured){;
		Integer liMbisnisId, liMbisnisNo;
		Double ldecMtsi;
		double ldecTsiCash,ldecSarCash,ldecRtsCash,ldecReaCash,ldecTsiTpd;
		double ldecSarTpd,ldecRtsTpd,ldecReaTpd; 
		String lsKursId;
		List list=selectProductUtama(spaj);
		Product product=(Product)list.get(0);
		liMbisnisId=product.getLsbs_id();
		liMbisnisNo=product.getLsdbs_number();
		ldecMtsi=product.getMspr_tsi();
		lsKursId=product.getLku_id();
		//
		if(liMbisnisId == 65){
			ldecTsiCash = 400000;
			ldecSarCash = 400000;
			ldecRtsCash = 200000;
			ldecReaCash = 200000;
			ldecTsiTpd = ldecMtsi.doubleValue();
			ldecSarTpd = ldecMtsi.doubleValue();
			ldecRtsTpd = 25000000;
			ldecReaTpd = ldecMtsi.doubleValue() - 25000000;
			
		}else if(liMbisnisId == 66 || liMbisnisId == 79 || liMbisnisId == 91 ){
			if (liMbisnisId == 79 || liMbisnisId == 91 ){
				if (liMbisnisNo <= 3 )
					liMbisnisNo = new Integer(1);
				else if(liMbisnisNo > 3 && liMbisnisNo <= 6)
					liMbisnisNo = new Integer(2);
				else if (liMbisnisNo > 6 && liMbisnisNo <= 9)
					liMbisnisNo = new Integer(3);
				else
					liMbisnisNo = new Integer(4);
			}
			
			if (liMbisnisNo == 1){
				ldecTsiCash = 250000;
				ldecSarCash = 250000;
				ldecRtsCash = 125000;
				ldecReaCash = 125000;
			}else if (liMbisnisNo == 2){
				ldecTsiCash = 500000;
				ldecSarCash = 500000;
				ldecRtsCash = 250000;
				ldecReaCash = 250000;
			}else if (liMbisnisNo == 3){
				ldecTsiCash = 750000;
				ldecSarCash = 750000;
				ldecRtsCash = 375000;
				ldecReaCash = 375000;
			}else{
				ldecTsiCash = 1000000;
				ldecSarCash = 1000000;
				ldecRtsCash = 500000;
				ldecReaCash = 500000;
			}
			ldecTsiTpd = 0;
			ldecSarTpd = 0;
			ldecRtsTpd = 0;
			ldecReaTpd = 0;
		}else{
			ldecTsiCash = 0;
			ldecSarCash = 0;
			ldecRtsCash = 0;
			ldecReaCash = 0;
			ldecTsiTpd = 0;
			ldecSarTpd = 0;
			ldecRtsTpd = 0;
			ldecReaTpd = 0;	
		}
		//
		updateMReasTemp(spaj,0,ldecTsiCash,ldecSarCash,ldecRtsCash,ldecReaCash,
				0,ldecTsiTpd,ldecSarTpd,ldecRtsTpd,ldecReaTpd);
	}
	
	public void updateMReasTemp(String spaj,double simCash,double tsiCash,double sarCash,double rtsCash,
			double reaCash,double simTpd,double tsiTpd,double sarTpd,double rtsTpd,double reaTpd){
		Map param=new HashMap();
		param.put("txtnospaj",spaj);
		param.put("sim_cash",new Double(simCash));
		param.put("ldec_tsi_cash",new Double(tsiCash));
		param.put("ldec_sar_cash",new Double(sarCash));
		param.put("ldec_rts_cash",new Double(rtsCash));
		param.put("ldec_rea_cash",new Double(reaCash));
		param.put("sim_tpd",new Double(simTpd));
		param.put("ldec_tsi_tpd",new Double(tsiTpd));
		param.put("ldec_sar_tpd",new Double(sarTpd));
		param.put("ldec_rts_tpd",new Double(rtsTpd));
		param.put("ldec_rea_tpd",new Double(reaTpd));
		update("update.m_reas_temp",param);
	}
	
	/**@Fungsi:	Mengambil Nilai perhitungan TSI reas
	 * @param	String spaj,Integer insured,Integer pemegang,Integer mainBisnisId,Integer mainBisnisNo,
	 *			Integer bisnisId,Integer bisnisNo,String kurs,Double ldecUp,BindException err
	 * @return 	Double
	 * @author 	Ferry Harlim
	 **/
	public Double selectGetTsiReas(String spaj,Integer insured,Integer pemegang,Integer mainBisnisId,Integer mainBisnisNo,
			Integer bisnisId,Integer bisnisNo,String kurs,Double ldecUp,BindException err)throws DataAccessException{
		Double ldecTsi=null,ldecKaliUp=null;
		Integer liAge=null, liUnit=null;
		
		liAge=selectMstInsuredMsteAge(spaj,insured);
		if(liAge==null){
			err.reject("","TSI Reinsurance..Can't Get Age item from MST_INSURED table ");
			return ldecTsi;
		}
		//
		if(mainBisnisId == 79){
			if(mainBisnisNo <= 3)
				bisnisNo = new Integer(1);
			else if (mainBisnisNo > 3 && mainBisnisNo <= 6)
				bisnisNo = new Integer(2);
			else if (mainBisnisNo > 6 && mainBisnisNo <= 9)
				bisnisNo = new Integer(3);
			else
				bisnisNo = new Integer(4);
					
		}
		//Ekasiswa
		if(bisnisId==24 || bisnisId==31 || bisnisId==33 || bisnisId==172){
			if (pemegang== 0 )
				ldecTsi = new Double(0.5 * ldecUp.doubleValue());
			else 
				ldecTsi = ldecUp;
			//Dana Sejahtera, End Care
		}else if(bisnisId==163||bisnisId==168||bisnisId==164||bisnisId==40||bisnisId==174||bisnisId==186){
			ldecTsi = ldecUp;
		
		//Eka Simponi Dollar
		}else if(bisnisId==56 || bisnisId==64){//BC ASALNYA CUMA 56 DOANG 050401
			if(liAge < 17 )
				ldecTsi = new Double(Math.min( 30000, ldecUp.doubleValue()));
			else if(liAge <= 55 )
				ldecTsi = new Double(Math.min( 75000, ldecUp.doubleValue()));
			else
				ldecTsi = new Double(Math.min( 30000, ldecUp.doubleValue()* 20/100 ));
		//Eka Simponi		
		}else if(bisnisId==58 || bisnisId==83){
			if(liAge < 17 )
				ldecTsi = new Double(Math.min( 200000000, ldecUp.doubleValue()));
			else if(liAge <= 55 )
				ldecTsi = new Double(Math.min( 500000000, ldecUp.doubleValue()));
			else
				ldecTsi = new Double(Math.min( 200000000, ldecUp.doubleValue()* 20/100 ));
		//PA Rider & Term Rider
		}else if(bisnisId==800 || bisnisId==803){
			liUnit=selectMstProductInsuredMsprUnit(spaj,insured,bisnisId,bisnisNo);
			if(liUnit!=null)
				ldecTsi=new Double(liUnit*ldecUp.doubleValue());
		//PK Rider
		}else if(bisnisId==801){
			ldecTsi = new Double(0.5 * ldecUp.doubleValue());
		//PA Include & PK Include, CASH PLAN, tpd
		}else if(bisnisId==600 || bisnisId==601 || bisnisId==806 ||bisnisId==807){
			ldecKaliUp=selectLstRiderIncludeLsridKaliUp(mainBisnisId,mainBisnisNo,bisnisId,bisnisNo,kurs);
			if(ldecKaliUp!=null)
				ldecTsi=new Double(ldecKaliUp.doubleValue()*ldecUp.doubleValue());
			//
			if(ldecKaliUp ==null){
				ldecTsi=null;
			}else if(bisnisId==600){
				if (mainBisnisId == 56){//PA Include pd plan Eka Simponi Dollar
					if (liAge < 17)
						ldecTsi = new Double(Math.min( 30000, ldecTsi.doubleValue()));
					else if (liAge <= 55)
						ldecTsi = new Double(Math.min( 75000, ldecTsi.doubleValue()));
					else
						ldecTsi = new Double(Math.min( 30000, ldecTsi.doubleValue() * 20/100 ));
				}else if (mainBisnisId== 58 || mainBisnisId == 83){//PA Include pd plan Eka Simponi
					if(liAge < 17 )
						ldecTsi = new Double(Math.min( 200000000, ldecTsi.doubleValue()));
					else if(liAge <= 55)
//						ldecTsi = new Double(Math.min( 500000000, ldecTsi.doubleValue()));
						ldecTsi = new Double(Math.min( 750000000, ldecTsi.doubleValue())); // per 1 apri 2008
					else
						ldecTsi = new Double(Math.min( 200000000, (ldecTsi.doubleValue() * 20/100 ) ));
				}else if (mainBisnisId == 65){ 
					if (ldecTsi.doubleValue() + ldecUp.doubleValue()> 600000000 )
						ldecTsi = new Double(600000000 - ldecUp.doubleValue());
				}

			}else if(bisnisId==601){
				if (mainBisnisId == 56) {//PK Include pd plan Eka Simponi Dollar
					if(liAge < 17)
						ldecTsi =new Double(Math.min( 15000, ldecTsi.doubleValue()));
					else if(liAge <= 55)
						ldecTsi =new Double(Math.min( 37500, ldecTsi.doubleValue()));
					else
						ldecTsi =new Double(Math.min( 15000, (ldecTsi.doubleValue() * 20/100 ) ));
				}else if(mainBisnisId == 58 || mainBisnisId == 83) {//PK Include pd plan Eka Simponi
					if (liAge < 17 )
						ldecTsi = new Double(Math.min( 100000000, ldecTsi.doubleValue()));
					else if (liAge <= 55)
						ldecTsi = new Double(Math.min( 250000000, ldecTsi.doubleValue() ));
					else
						ldecTsi = new Double(Math.min( 100000000, ldecTsi.doubleValue() * 20/100 ));
				}else if(mainBisnisId == 65){ 
					if (ldecTsi.doubleValue() > 300000000 )
						ldecTsi =new Double(300000000);
				}
			}else if(bisnisId==806){
				ldecTsi = ldecKaliUp;			
			}else if(bisnisId==807){
				if (ldecTsi.doubleValue() > 300000000 ) 
					ldecTsi = new Double(300000000);
			}
				
		}else{
			ldecKaliUp=selectLstReinsDescLsrpKaliUp(bisnisId,bisnisNo,kurs);
			if(ldecKaliUp==null)
				ldecTsi=null;
			else 
				ldecTsi = new Double(ldecKaliUp.doubleValue() * ldecUp.doubleValue());
		}

		if(ldecTsi==null){
			err.reject("","F_get_tsi_reas Value of TSI Reins. is Null Bisnis ID="+
					bisnisId+" Bisnis No="+bisnisNo+"Kurs ="+kurs+"Check LST_REINS_DESC");
		}
		return ldecTsi;
	}
	
	public Integer selectMstInsuredMsteAge(String spaj,Integer insured){
		Map param=new HashMap();
		param.put("as_spaj",spaj);
		param.put("ai_insured",insured);
		return (Integer)querySingle("select.mst_insured_age",param);
		
	}
	
	public Integer selectMstProductInsuredMsprUnit(String spaj,Integer insured,Integer lsbsId,Integer lsdbsNumber){
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("mste_insured_no",insured);
		param.put("lsbs_id",lsbsId);
		param.put("lsdbs_number",lsdbsNumber);
		return (Integer)querySingle("select.mst_product_insured_mspr_unit",param);
	}
	
	public Double selectLstRiderIncludeLsridKaliUp(Integer mainBisnisId,Integer mainBisnisNo,Integer bisnisId,Integer bisnisNo,String kurs){
		Map param=new HashMap();
		param.put("ai_main_bisnis_id",mainBisnisId);
		param.put("ai_main_bisnis_no",mainBisnisNo);
		param.put("ai_bisnis_id",bisnisId);
		param.put("ai_bisnis_no",bisnisNo);
		param.put("as_kurs",kurs);
		return (Double) querySingle("select.lst_rider_include2",param);
	}
	
	public Double selectLstReinsDescLsrpKaliUp(Integer bisnisId,Integer bisnisNo,String kurs){
		Map param=new HashMap();
		param.put("ai_bisnis_id",bisnisId);
		param.put("ai_bisnis_no",bisnisNo);
		param.put("as_kurs",kurs);
		return (Double) querySingle("select.lst_reins_desc2",param);
		
	}
	
	public Double selectGetSar(Integer aiType,Integer aiBisnisId,Integer aiBisnisNo, String asKurs, Integer aiCbayar,
			Integer aiLbayar, Integer aiLTanggung, Integer aiYear, Integer aiAge){
		Double ldecValue;
		Integer liLTanggungMax;
		if(aiBisnisId<300){
			ldecValue=null;
			liLTanggungMax=null;
			if (aiBisnisId == 70 || aiBisnisId == 71 || aiBisnisId == 72 || aiBisnisId == 172)
				aiBisnisId = new Integer(31);
			else if (aiBisnisId == 78) 
				aiBisnisId = new Integer(52); 
			else if (aiBisnisId == 82) 
				aiBisnisId = new Integer(69);
			else if (aiBisnisId == 83) 
				aiBisnisId = new Integer(58);
			//
			if (aiCbayar == 1 || aiCbayar == 2 || aiCbayar == 6)
				aiCbayar = new Integer(3);
			if (aiCbayar == 4 || aiCbayar == 5 )
				aiCbayar = new Integer(3);
			//
			liLTanggungMax=selectLstReinsDesc1(asKurs,aiBisnisId,aiBisnisNo);
			if(liLTanggungMax!=null)
				if(liLTanggungMax>0)
					aiLTanggung=new Integer(liLTanggungMax-aiAge);
			//
			/*if (aiBisnisId == 182 && aiBisnisNo == 12)
					if(aiLTanggung==null)
						aiLTanggung=new Integer(8);
						aiLbayar = new Integer(5);
			*/
			if(aiType == 1)
				aiYear = new Integer(1);
			//
			if (aiBisnisId == 62 )
				aiBisnisId = new Integer(52); //040401BC
			if (aiBisnisId == 56 || aiBisnisId == 64 )
				aiLTanggung = new Integer(8); //ASAL LSBS=56 050401BC
			if (aiBisnisId == 64)
				aiBisnisId = new Integer(56);
			if (aiBisnisId != 65 )
				aiAge = new Integer(1); 		
			if (aiCbayar == 0)
				aiLbayar = new Integer(1);
			//
			ldecValue=selectLstTableAwal(aiType,aiBisnisId,asKurs,aiCbayar,
					aiLbayar,aiLTanggung,aiYear,aiAge);
			//
		}else if(aiBisnisId < 600 )
			ldecValue=null;
		else
			ldecValue = new Double(1000);

		if(ldecValue==null)
			ldecValue = new Double(1000);
			
		return ldecValue;
	}
	
	public Double selectGetSarNew(Integer aiType,Integer aiBisnisId,Integer aiBisnisNo, String asKurs, Integer aiCbayar, Integer aiLbayar, 
								  Integer aiLTanggung, Integer aiYear, Integer aiAge, Integer lstbId, String polis, Integer bulanKe, String begDate, String now){	
		Double ldecValue = 0.0;
		Integer liLTanggungMax;
		//polis = "08560200800556";
		//lstbId = 2;
		
		if(lstbId == 1) {			
			if(aiBisnisId<300 || aiBisnisId == 800 || aiBisnisId == 801 || aiBisnisId == 802 || aiBisnisId == 803 ||
			   aiBisnisId == 804 || aiBisnisId == 807 || aiBisnisId == 808 || aiBisnisId == 810 || aiBisnisId == 813 ||
			   aiBisnisId == 811 || aiBisnisId == 812 ||  aiBisnisId == 816 || aiBisnisId == 817 ||
			   aiBisnisId == 819 || aiBisnisId == 820 || aiBisnisId == 823 || aiBisnisId == 825 || aiBisnisId == 826 ||
			   aiBisnisId == 831 || aiBisnisId == 832 || aiBisnisId == 833){
				if (aiBisnisId == 172) aiBisnisId = new Integer(31);
				if (aiBisnisId != 65 ) aiAge = new Integer(1);
				
				if (aiCbayar == 1 || aiCbayar == 2 || aiCbayar == 4 || aiCbayar == 5 || aiCbayar == 6) aiCbayar = new Integer(3);
				if (aiCbayar == 0) aiLbayar = new Integer(1);			

				liLTanggungMax=selectLstReinsDesc1(asKurs,aiBisnisId,aiBisnisNo);
				if(liLTanggungMax!=null && liLTanggungMax > 0)
					aiLTanggung=new Integer(liLTanggungMax-aiAge);	
				
				ldecValue = selectLstTableAwal(9,aiBisnisId,"01",9,0,0,0,0);
				if(ldecValue == null) 
					ldecValue=selectLstTableAwal(aiType,aiBisnisId,asKurs,aiCbayar,aiLbayar,aiLTanggung,aiYear,aiAge); 
			}else if(aiBisnisId >= 900) {
				ldecValue = new Double(0);				
			}else if(aiBisnisId==815){
				  aiType = 2;
				  aiCbayar = 0;
				  aiLbayar=aiBisnisNo!=6?1:6;
				  aiLTanggung= 1;
				  aiYear = 1;	  
				  ldecValue=selectLstTableAwal(aiType,aiBisnisId,asKurs,aiCbayar,aiLbayar,aiLTanggung,aiYear,aiAge); 				  
			}else if(aiBisnisId==814){
				  aiType = 2;
				  aiCbayar = 0;
				  aiLbayar=1;
				  aiLTanggung= 1;
				  aiYear = 1;	  
				  ldecValue=selectLstTableAwal(aiType,aiBisnisId,asKurs,aiCbayar,aiLbayar,aiLTanggung,aiYear,aiAge); 
			}else if(aiBisnisId == 827){
				  aiType = 2;
				  aiCbayar = 0;
				  if(aiBisnisNo==1)aiLbayar=55;
				  else if(aiBisnisNo==2)aiLbayar=60;
				  else if(aiBisnisNo==3)aiLbayar=65;
				  else aiLbayar=55;
				  if(aiBisnisNo==4 || aiBisnisNo == 5)aiAge=45;
				  aiLTanggung= 1;
				  aiYear = 1;	  
				  ldecValue=selectLstTableAwal(aiType,aiBisnisId,asKurs,aiCbayar,aiLbayar,aiLTanggung,aiYear,aiAge); 				
			}else if(aiBisnisId == 828){
				  aiType = 2;
				  aiCbayar = 0;
				  aiLbayar=1;
				  if(aiBisnisNo==2){aiLTanggung=5;aiType=1;}
				  else if(aiBisnisNo==3){aiLTanggung=10;aiType=1;}
				  else aiLTanggung=1;
				  aiYear = 1;	  
				  ldecValue=selectLstTableAwal(aiType,aiBisnisId,asKurs,aiCbayar,aiLbayar,aiLTanggung,aiYear,aiAge); 
			}else if(aiBisnisId == 835){
				  aiType = 2;
				  aiCbayar = 0;
				  aiLbayar=1;
				  aiLTanggung=aiBisnisNo!=1?25:22;
				  aiYear = 1;	  
				  ldecValue=selectLstTableAwal(aiType,aiBisnisId,asKurs,aiCbayar,aiLbayar,aiLTanggung,aiYear,aiAge); 
			}else ldecValue = new Double(1000);			
		}else if(lstbId == 2) { // SAR Menurun MRI
			if(aiBisnisId >= 900) ldecValue = new Double(0);
			else {
				if(polis.equals("")) return 0.0;
				if(!now.equals("00/00/0000")) {
					f_hit_umur x = new f_hit_umur();
					bulanKe = x.bulan(Integer.parseInt(begDate.substring(6)), Integer.parseInt(begDate.substring(3,5)), Integer.parseInt(begDate.substring(0,2)), Integer.parseInt(now.substring(6)), Integer.parseInt(now.substring(3,5)), Integer.parseInt(now.substring(0,2)));
				}
				Integer ldec_total_bulan = 0;
				Double ldec_nilai = 0.0;
				
				Map temp = new HashMap();
				temp = selectLoanTemp(polis);
				Integer ldec_tahun = new BigDecimal(temp.get("LDEC_TAHUN").toString()).intValue();
				Integer ldec_bulan = new BigDecimal(temp.get("LDEC_BULAN").toString()).intValue();
				String ls_kurs = temp.get("LS_KURS").toString();
				Double ldc_bunga = new BigDecimal(temp.get("LDC_BUNGA").toString()).doubleValue();
				Double ldec_up = new BigDecimal(temp.get("LDEC_UP").toString()).doubleValue();
				Integer li_bisnis = new BigDecimal(temp.get("LI_BISNIS").toString()).intValue();
				
				if(ldec_bulan > 0) ldec_total_bulan = (ldec_tahun * 12) + ldec_bulan;
				else {
					if(li_bisnis == 312) ldec_total_bulan = ldec_tahun * 4;
					else ldec_total_bulan = ldec_tahun * 12;
				}
				
				Integer li_bulan_ke = 0;
				Integer li_bulan_ke_tmp = 0;
				while (li_bulan_ke <= bulanKe) {
					if(ldc_bunga == 0) {
						if(li_bulan_ke == 0) ldec_nilai = ldec_up;
						else ldec_nilai = ldec_up;
						if(ls_kurs.equals( "01")) ldec_nilai = new BigDecimal(Math.round(ldec_nilai)).doubleValue();
					}
					else {
						if(li_bisnis == 315) {
							Integer ldec_rate = selectLstTblSpdn(li_bisnis,ldec_tahun,li_bulan_ke);
							ldec_nilai  = (ldec_rate * ldec_up) /1000;
						}
						else if(li_bisnis == 321) {
							if(li_bulan_ke == 0) ldec_nilai = ldec_up;
							else ldec_nilai = ldec_up * (( ldec_total_bulan - (li_bulan_ke - 1 ))/ ldec_total_bulan );
							if(ls_kurs.equals( "01")) ldec_nilai = new BigDecimal(Math.round(ldec_nilai)).doubleValue();
						}
						else if(li_bisnis == 312) {
							if(li_bulan_ke == 0) ldec_nilai = ldec_up;
							else ldec_nilai = ldec_up * (  (Math.pow((1 + (ldc_bunga/100)/4 ), li_bulan_ke))  -  (Math.pow(( (ldc_bunga/100)/4+1), li_bulan_ke) - 1) /  (1 - ( 1/ Math.pow(( 1+(ldc_bunga/100)/4), ldec_total_bulan) )) );
							if(ls_kurs.equals( "01")) ldec_nilai = new BigDecimal(Math.round(ldec_nilai)).doubleValue();						
						}
						else {
							if(li_bulan_ke == 0) ldec_nilai = ldec_up;
							else ldec_nilai = ldec_up * (  (Math.pow((1 + (ldc_bunga/100)/12 ), li_bulan_ke))  -  (Math.pow(( (ldc_bunga/100)/12+1), li_bulan_ke) - 1) /  (1 - ( 1/ Math.pow(( 1+(ldc_bunga/100)/12), ldec_total_bulan) )) );
							if(ls_kurs.equals( "01")) ldec_nilai = new BigDecimal(Math.round(ldec_nilai)).doubleValue();
						}
					}
					ldecValue = ldec_nilai;
					//logger.info(li_bulan_ke + ": " + ldec_nilai);
					if(li_bisnis == 312) li_bulan_ke_tmp += 3;
					li_bulan_ke +=  1;
				}				
			}
		}
		
		return ldecValue;
	}
	
	public HashMap selectLoanTemp(String noPolis) {
		return (HashMap)querySingle("selectLoanTemp", noPolis);
	}
	
	public Integer selectLstTblSpdn(Integer detBisnis, Integer tahun, Integer bulan) {
		Map params = new HashMap();
		params.put("detBisnis", detBisnis);
		params.put("tahun", tahun);
		params.put("bulan", bulan);	
		
		return (Integer) querySingle("selectLstTblSpdn", params);
	}

	public Double selectGetMriSar(Integer aiType,Integer aiBisnisId,Integer aiFlat, Double aiBunga, Integer aiMonth, Integer aiYear,BindException err){
		Integer liPenurunanSar=null;
		Double ldecValue=null;
			
		if( (aiBisnisId == 331 || aiBisnisId == 336 ||aiBisnisId == 557) 
				&& (aiFlat == 1) ) {//19072001
			if (aiType == 1) 
				aiYear = new Integer(1);
			//
			liPenurunanSar=selectLstBisnisLsbsMonthSar(aiBisnisId);
			if(liPenurunanSar==null)
				err.reject("","F_Get_MRI_SAR Bisnis Id="+aiBisnisId+" Month Sar="+liPenurunanSar);
			//
			aiBunga = new Double(90); //dummy
		}else if( (( aiBisnisId >= 300 && aiBisnisId < 400 ) || ( aiBisnisId >= 500 && aiBisnisId < 600 ))  
			&& (aiFlat == 0) ){
			if (aiType == 1)
				aiYear = new Integer(1);
			//
			liPenurunanSar=selectLstBisnisLsbsMonthSar(aiBisnisId);
			if(liPenurunanSar==null)
				err.reject("","F_Get_MRI_SAR Bisnis Id="+aiBisnisId+" Month Sar="+liPenurunanSar);
			//
			ldecValue=selectLstTableMriAwalLstMawValue(aiType,liPenurunanSar,aiBunga,aiMonth,aiYear);

		}else if(aiBisnisId < 300 )
			ldecValue=null;
		else if( (aiBisnisId >= 400 && aiBisnisId < 500 ) || ( aiBisnisId >= 600 ) )
			ldecValue=null;
		else
			ldecValue = new Double(1000);
		
		if(ldecValue==null){
			ldecValue = new Double(1000);
//			MessageBox( 'F_Get_Mri_Sar', &
//					'type      = ' + string(ai_type) + '~r~n' + &
//	  	         'bisnis_id = ' + string(ai_bisnis) + '~r~n' + &
//					'Pen. SAR  = ' + string(li_penurunan_sar) + '~r~n' + &
//	  	         'bunga     = ' + string(ai_bunga) + '~r~n' + &
//	     	      'bulan     = ' + string(ai_month) + '~r~n' + &
//           	   'tahun ke  = ' + string(ai_year) )
		}
		return ldecValue;
	}
	
	public Integer selectLstBisnisLsbsMonthSar(Integer aiBisnisId){
		return (Integer) querySingle("select.lst_bisnis",aiBisnisId);
	}
	
	public Double selectLstTableMriAwalLstMawValue(Integer aiType,Integer liPenurunanSar,Double aiBunga,Integer aiMonth,Integer aiYear){
		Map param=new HashMap();
		param.put("ai_type",aiType);
		param.put("li_penurunan_sar",liPenurunanSar);
		param.put("ai_bunga",aiBunga);
		param.put("ai_month",aiBunga);
		param.put("ai_year",aiYear);
		return (Double)querySingle("select.lst_table_mri_awal",param);
		
	}
	
	public double selectKursAdjust(Double ldecUp, Double ldecKurs, String asKurs1,String asKurs2){
		double ldecReturn=0;
		//
		if(asKurs1.equals(asKurs2))
			ldecReturn = ldecUp.doubleValue();
		else if(asKurs1.equals("01"))
			ldecReturn = ldecUp.doubleValue()* ldecKurs.doubleValue();
		else if(asKurs1.equals("02"))
			ldecReturn = ldecUp.doubleValue()/ ldecKurs.doubleValue();
		
		return ldecReturn;	
	}
	
	public int insertMSarTemp(String spaj,String asPolis,Integer aiBisnisId, Integer aiBisnisNo,
			String aiKursId, Double ldecSar, Integer aiSts, Integer aiMedical, int aiCnt,String reg_spaj_ref){
		aiCnt++;
		try{
			ldecSar=new Double(dec2.format(ldecSar.doubleValue()));
		}catch (Exception e){
			logger.info(e);
		}
		
		
		Map insParam =new HashMap();
		insParam.put("txtnospaj",spaj);
		//insParam.put("reg_spaj_ref",reg_spaj_ref);
		insParam.put("li_cnt",new Integer(aiCnt));
		insParam.put("ls_polis",asPolis);
		insParam.put("li_bisnis_id",aiBisnisId);
		insParam.put("li_bisnis_no",aiBisnisNo);
		insParam.put("ls_kurs_id",aiKursId);
		insParam.put("ldec_sar",ldecSar);
		insParam.put("li_sts_polis",aiSts);
		insParam.put("li_medis",aiMedical);
		getSqlMapClientTemplate().insert("elions.uw.insert.m_sar_temp",insParam);
		return aiCnt;
	}
	
	public int insertMSarTempNew(String spaj,String asPolis,Integer aiBisnisId, Integer aiBisnisNo,Integer lsgb_id,
			String aiKursId, Double ldecSar, Integer aiSts, Integer aiMedical, int aiCnt,Double simultan,
			Double tsi){
		aiCnt++;
		ldecSar=new Double(dec2.format(ldecSar.doubleValue()));
		
		Map insParam =new HashMap();
		insParam.put("txtnospaj",spaj);
		insParam.put("li_cnt",new Integer(aiCnt));
		insParam.put("ls_polis",asPolis);
		insParam.put("li_bisnis_id",aiBisnisId);
		insParam.put("li_bisnis_no",aiBisnisNo);
		insParam.put("lsgb_id", lsgb_id);
		insParam.put("ls_kurs_id",aiKursId);
		insParam.put("ldec_sar",ldecSar);
		insParam.put("li_sts_polis",aiSts);
		insParam.put("li_medis",aiMedical);
		insParam.put("simultan", simultan);
		insParam.put("tsi", tsi);
		getSqlMapClientTemplate().insert("elions.uw.insert.m_sar_temp_new",insParam);
		return aiCnt;
	}
	public Double selectLstEmMaxLsemMaxLsemValue(Integer typeBisnis, Integer typeReas,Date begdate){
		Map param =new HashMap();
		param.put("type_bisnis",typeBisnis);
		param.put("li_type_reas",typeReas);
		param.put("beg_date",begdate);
		return (Double)querySingle("select.lst_em_max2",param);
	}
	
	public Double selectGetRetensi(Integer aiMainBisnisId,Integer aiMainBisnisNo, Integer aiRider, Integer aiType, Integer aiMedis,
			String asKurs, Date begDate,Integer aiAge,String spaj ,BindException err){
		Integer liTbisnis=null, liTreins = null;
		Double ldecValue=null;
		if (aiMainBisnisId == 331){
			if(aiType == 1){//Retensi Limit
				if (asKurs.equals("01")){
					if(aiMedis == 0 )
						ldecValue = new Double(15000000);
					else
						ldecValue = new Double(30000000);
				}
			}else if(aiType == 2){//UW Limit
				if (asKurs.equals("01")){
					if( aiMedis == 0){
						if (aiAge >= 1 && aiAge <= 50 )
							ldecValue = new Double(200000000);
						else if (aiAge >= 51 && aiAge <= 55)
							ldecValue = new Double(150000000);
						else if (aiAge >= 56 && aiAge <= 60 )
							ldecValue = new Double(100000000);
					}else if (aiMedis == 1){
//						ldecValue = new Double(500000000);
						if((products.syariah(aiMainBisnisId.toString(), aiMainBisnisNo.toString()))){	
							ldecValue = new Double(500000000); //per 1 april 2008
						}else
							ldecValue = new Double(750000000); //per 1 april 2008
					}
				}
			}
			
		}else if (aiMainBisnisId == 539){// doel020305
			if (aiMedis == 0)
				ldecValue = new Double(20000);
			else
				ldecValue = new Double(32000);
		}
		else{
			ParameterClass param=selectLstBisnis(aiMainBisnisId);
			liTbisnis=param.getLstb_id();
			liTreins=param.getLstr_id();
			//ldecValue=new Double(500000000); //special case 42200800143(novie)
//			List ldecValueList=selectLstLimitReinsuranceLsliValue(liTbisnis,liTreins,aiRider,aiType,
//					asKurs,aiMedis,aiAge,begDate,spaj);
//			for (int j = 0; j < ldecValueList.size(); j++) {
//				Map ldecValueMap= (Map) ldecValueList.get(j);
//				ldecValue= ((BigDecimal)ldecValueMap.get("LSLI_VALUE")).doubleValue();
//				}
			if((products.syariah(aiMainBisnisId.toString(), aiMainBisnisNo.toString()))){	
				ldecValue = new Double(500000000); //per 1 april 2008
			}else
			ldecValue=selectLstLimitReinsuranceLsliValue(liTbisnis,liTreins,aiRider,aiType,
					asKurs,aiMedis,aiAge,begDate,spaj);
		}
		
		// Untuk Special Case Tidak Mendapatkan Retensi
		//default value retensi untuk tahun 2007 medis dan nonmedis sama (untuk special case
//		if(asKurs.equals("01")){
//			ldecValue=new Double(750000000); //RP
//		}else{
//			ldecValue=new Double(75000); //US$
//		}
		if(ldecValue==null){
			err.reject("","Retensi Not Found Bisnis Id="+aiMainBisnisId+" BisnisTYpe="+liTbisnis+" reins Type="+liTreins+
					"Rider Type="+aiRider+" table type="+aiType+" Medical Status="+aiMedis+" kurs="+asKurs+
					"Date="+defaultDateFormat.format(begDate)+" age="+aiAge+" Check selectGetRetensi or lst_limit_reinsurance");
		}
		return ldecValue;
	}
	
	public ParameterClass selectLstBisnis(Integer bisnisId){
		return (ParameterClass)querySingle("select.lst_bisnis_retensi2",bisnisId);
	}
	
	public Double selectLstLimitReinsuranceLsliValue(Integer liTbisnis,Integer liTreins,Integer aiRider,
			Integer aiType, String asKurs, Integer aiMedis, Integer aiAge,Date begDate,String spaj){
		Map param=new HashMap();
		param.put("li_tbisnis",liTbisnis);
		param.put("li_treins",liTreins);
		param.put("ai_rider",aiRider);
		param.put("ai_type",aiType);
		param.put("as_kurs",asKurs);
		param.put("ai_medis",aiMedis);
		param.put("ai_age",aiAge);
		param.put("adt_bdate",begDate);
		param.put("spaj",spaj);
		return  (Double)querySingle("select.lst_limit_reinsureance3",param);	
//		return (Double)querySingle("select.lst_limit_reinsureance2",param);	
	}
	
	public Integer selectCountMReasTemp(String spaj){
		return (Integer) querySingle("select.count_m_reas_temp",spaj);
	}
	
	public Integer selectMstReins(String spaj){
		return (Integer) querySingle("selectMstReins",spaj);
	}
	
	public Integer selecCounttMstReinsProd(String spaj){
		return (Integer) querySingle("selecCounttMstReinsProd",spaj);
	}
	
	public void deleteMReasTemp(String spaj){
		getSqlMapClientTemplate().delete("elions.uw.delete.m_reas_temp",spaj);
	}

	public void deleteMReasTempNew(String spaj){
		getSqlMapClientTemplate().delete("elions.uw.delete.m_reas_temp_new",spaj);
	}
	
	public void deleteMSarTemp(String spaj){
		getSqlMapClientTemplate().delete("elions.uw.delete.m_sar_temp",spaj);
	}
	
	public void deleteMSarTempNew(String spaj){
		getSqlMapClientTemplate().delete("elions.uw.delete.m_sar_temp_new",spaj);
	}
	
	public void insertMReasTemp(ReasTemp insReas){
		getSqlMapClientTemplate().insert("elions.uw.insert.m_reas_temp2",insReas);
	}

	public void insertMReasTempNew(ReasTempNew insReas){
		getSqlMapClientTemplate().insert("elions.uw.insert.m_reas_temp_new",insReas);

	}
	
	public void updateMReasTempMsteReas(String spaj,Integer msteReas, Integer mste_backup){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("mste_reas",msteReas);
		param.put("mste_backup", mste_backup);
		update("update.m_reas_temp.mste_reas",param);
	}
	
	public void updateMstInsuredMsteStandard(String spaj,Integer insured,Integer msteStandard){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("insured",insured);
		param.put("mste_standard",msteStandard);
		update("update.mst_insured.mste_standard",param);
	}

	public void deleteDataKomisi(String spaj) throws DataAccessException{
//		delete("delete.upload.non", spaj);
//		delete("delete.upload", spaj);
//		delete("delete.refund", spaj);
		delete("delete.kom_bonus", spaj);
		delete("delete.deduct", spaj);
		delete("delete.komisi", spaj);
		delete("delete.reward", spaj);
		delete("delete.kom_reff_bii", spaj);
		delete("delete.diskon_perusahaan",spaj);
	}
	
	public void deleteDataProduksi(String spaj) throws DataAccessException{
		delete("delete.mst_det_production", spaj);
		delete("delete.mst_production", spaj);
	}
	
	public void deleteMstSimcard(String no_kartu)throws DataAccessException{
		delete("delete.mst_simcard", no_kartu);
	}
	
	public void updateMstKartuPas(String no_kartu)throws DataAccessException{
		update("updateKartuPas", no_kartu);
	}
	
	public String selectEmailAdmin(String spaj){
		return (String) querySingle("selectemailAdmin",spaj);
	}
	
	public Map selectEmailAgen(String spaj){
		return (HashMap) querySingle("selectemailAgen",spaj);
	}
	
	public Map selectEmailAgen2(String msag_id){
		return (HashMap) querySingle("selectemailAgen2",msag_id);
	}
	
	public Map selectInfoPemegang(String spaj){
		return (HashMap) querySingle("selectInfoPemegang",spaj);
	}
	
	public void prosesFurtherRequirement(String spaj,Integer insuredNo,Integer liAksep,int liAktif,
			String lusId,int lspdId,int lsspId,String desc){	
		insertMstPositionSpaj(lusId, desc, spaj, 0);
		updateMstInsuredStatus(spaj,insuredNo,liAksep,liAktif,null,null);
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
	}
	
	public List selectlstCabang(){
		return query("selectAllLstCabang",null);
	}

//	public List selectAllAgen(){
//		return query("selectAllAgen",null);
//	}
	
	public List selectAllLstRegion(){
		return query("selectAllLstRegion",null);
	}
	public List selectProductCombined() {
		return query("selectProductCombined", null);
	}
	
	public List selectlstCabang2(){
		return query("selectAllLstCabang2",null);
	}
	
	public List selectReportPrintPolis(Map map) {
		return query("selectReportPrintPolis", map);
	}
	
	public List selectlstlevel(){
		return query("selectlstlevel",null);
	}
	/**Fungsi:	Untuk Menampilkan Data Karyawan yang mempunyai polis.
	 * @param 	String spaj
	 * @return	String
	 * @author	Ferry Harlim
	 */
	public String selectMstEmp(String spaj){
		return (String)getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_emp",spaj);
	}

	/**Fungsi:	Untuk Membuat Jurnal produk link Pada proses Transfer BAC ke UW
	 * @param 	String spaj,String lusId,Integer lsbsId,String namaPemegang,BindException err
	 * @return	boolean
	 * @author	Ferry Harlim
	 * sampe sini
	 * @throws ParseException 
	 */
	
    public boolean getJurnalBacUlink(String spaj,String lusId,Integer lsbsId,String namaPemegang,BindException err,HttpServletRequest request) throws ParseException{
		Date ld_tgl_rk;
		Boolean hasil = true;
		Boolean lJurnal = false;
		long ll_bill;
		Integer li_bayar = null;
		Double ldec_bayar = null, ldec_jumlah;
		String ls_year, ls_month, ls_kurs, ls_no_voucher, ls_cb, ls_no_pre;
		S_Premi stru_premi = new S_Premi();
		List lds_bill;
		lds_bill = selectMstDepositPremium(spaj, null);
		if(lds_bill.isEmpty()){
			ll_bill = 0;
		} else{
			ll_bill = lds_bill.size();
		}
		
		ls_no_voucher = null;
		stru_premi.setNama_pemegang(namaPemegang);
		stru_premi.setNo_spaj(FormatString.nomorSPAJ(spaj));
		if(stru_premi.getNo_spaj()==null){
			err.reject("","Nomor Spaj Kosong");
			return false;
		}
		
		stru_premi.setPremike(new Integer(1));
		stru_premi.setTahunke (new Integer(1));
		stru_premi.setNo_pre(null);
		stru_premi.setMri(false);
		if(ll_bill == 0){
			Account_recur account_recur = bacDao.select_account_recur(spaj);
			if(account_recur!=null){
				if(account_recur.getFlag_autodebet_nb()!=null){
					if(account_recur.getFlag_autodebet_nb()==1){
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
			
		//Buat jurnal sesuai dengan banyaknya titipan premi yang diinput
		for(long i=0;i<lds_bill.size();i++){
			DepositPremium depPre = (DepositPremium) lds_bill.get((int) i);
			stru_premi.setRek_id(depPre.getLsrek_id());
			li_bayar = depPre.getLsjb_id();
			ls_kurs = depPre.getLku_id();
			ldec_bayar = depPre.getMsdp_payment();
			ld_tgl_rk = depPre.getMsdp_date_book();
			ls_no_voucher = depPre.getMsdp_no_voucher();
			ls_no_pre = depPre.getMsdp_no_pre();
			if(ld_tgl_rk==null){
				err.reject("","Tanggal R/K Kosong");
				return false;
			}
			
			if(ls_kurs.equals("02")){
				ls_year = defaultDateFormatStripes.format(ld_tgl_rk).substring(6,defaultDateFormatStripes.format(ld_tgl_rk).length());
				ls_month = defaultDateFormatStripes.format(ld_tgl_rk).substring(3,5);
				
				Double kursBulanan = selectLstMonthlyKursLmkNilai(ls_year, ls_month, ls_kurs);
				if(kursBulanan==null){
					err.reject("","Open Lst Monthly kurs Gagal");
					return false;
				}
				stru_premi.setKurs_bulanan(kursBulanan);
			}else{
				stru_premi.setKurs_bulanan(new Double(1));
			}
			
			if(stru_premi.getKurs_bulanan()==null){
				stru_premi.setKurs_bulanan(new Double(0));
			}
			if(ldec_bayar==null) ldec_bayar = new Double(0);
			ldec_jumlah = new Double(stru_premi.getKurs_bulanan().doubleValue() * ldec_bayar.doubleValue());
			
			//Validasi cara bayar yang harus di jurnal
			ls_cb = props.getProperty("product.CB_jurnal");
			Integer pos;
			String ls_bayar = f2.format(li_bayar);
			pos = ls_cb.indexOf(ls_bayar);
			if(pos>=0){
				String param = "ulink";
				Premi premi_payment = new Premi();
				List payment = this.uwDao.selectDetailPayment(spaj, 1, 1, param);
				Integer li_row = this.uwDao.selectTagPaymentCount(spaj, 1, 1, param);
				if(ls_no_voucher==null){
					if(getBuatVoucherPremiIndividu(stru_premi, ls_no_pre, ls_no_voucher, li_bayar)){
						ls_no_voucher = stru_premi.getVoucher(); //pass by reference
						//Deddy (17 Feb 2015) - Jurnal TTD : Apabila no_pre dari mst_drek exists, pakai jurnal baru (Jurnal TTD).Apabila tidak ada, pakai jurnal bank yg lama.
						if(!StringUtil.isEmpty(ls_no_pre)){	
							lJurnal = getUlinkJm(stru_premi,ldec_jumlah,ld_tgl_rk,li_bayar,i,lusId,ls_no_pre,request);
						}else{
							//Kondisi sebelum 1 maret 2015, masih di jurnal bank.
							lJurnal = getPreUlink(stru_premi,ldec_jumlah,ld_tgl_rk,li_bayar,i,lusId,request);
						}
						
						if(lJurnal){	
							Premi premi = new Premi();
							Long rk_nocr = null;
							rk_nocr = this.accountingDao.selectNewCrNo("");		// counterAvnel
							//premi.setNo_cr(stru_premi.getNo_cr());
							premi.setNo_cr(rk_nocr);
							premi.setRek_id(stru_premi.getRek_id());
							//updateLst_rek_ekalife(premi);
							//
							bacDao.updateMstDepositPremium(stru_premi, new Integer(1), new Long(i+1));
							Long l_counterID = i;
								premi_payment.setVoucher(stru_premi.getVoucher());
								Map m = (HashMap) payment.get(l_counterID.intValue());
								String ls_id = (String) m.get("MSPA_PAYMENT_ID");
								//Deddy (17 Feb 2015) - Jurnal TTD : di payment diupdate ke kolom mspa_no_jm_sa utk no_jm
								this.uwDao.updateMst_paymentJurnal(premi_payment, stru_premi.getDepo_pre(), ls_id, null,stru_premi.getNo_jm());
								if(!StringUtil.isEmpty(ls_no_pre)){	
									uwDao.updateNoPreMstDrekDetFromDepositPremium(spaj, ls_id , stru_premi.getDepo_pre(), stru_premi.getNo_jm(),new Long(i+1));
								}else {
									uwDao.updateNoPreMstDrekDetFromDepositPremium(spaj, ls_id , stru_premi.getDepo_pre(), null, new Long(i+1));
								}
							
						}else{
							err.reject("","Terjadi Kesalahan Pada Proses Jurnal!");
							return false;
						}
						
					}
				}else{//bagian ini untuk set ulang no pre dan voucher ke mst_payment
					for(int j=0; j<li_row; j++){
						premi_payment.setVoucher(ls_no_voucher);
						Map m = (HashMap) payment.get(j);
						String ls_id = (String) m.get("MSPA_PAYMENT_ID");
						this.uwDao.updateMst_paymentJurnal(premi_payment, depPre.getMsdp_no_pre(), ls_id, null, stru_premi.getNo_jm());
						uwDao.updateNoPreMstDrekDetFromDepositPremium(spaj, ls_id , stru_premi.getDepo_pre(), null, new Long(i+1));
					}
				}
			}else{
				if(li_bayar!=null) lJurnal=true;
			}
		}
		return hasil;
}
	
    public boolean getJurnalBacIndividu(String spaj, String lusId, Integer lsbsId, String namaPemegang, BindException err, HttpServletRequest request) throws ParseException{
		Date ld_tgl_rk;
		Boolean lJurnal = true;
		long ll_bill;
		Integer li_bayar = null;
		Double ldec_bayar = null, ldec_jumlah;
		String ls_year, ls_month, ls_kurs, ls_no_pre, ls_no_voucher, ls_cb;
		S_Premi stru_premi = new S_Premi();
		List lds_bill;
		lds_bill = selectMstDepositPremium(spaj, null);
		if(lds_bill.isEmpty()){
			ll_bill = 0;
		} else{
			ll_bill = lds_bill.size();
		}
		ls_no_voucher = null;
		
		stru_premi.setNama_pemegang(namaPemegang);
		stru_premi.setNo_spaj(FormatString.nomorSPAJ(spaj));
		if(stru_premi.getNo_spaj()==null){
			err.reject("","No SPaj Kosong..");
			return false;
		}
		
		stru_premi.setPremike(new Integer(1));
		stru_premi.setTahunke (new Integer(1));
		stru_premi.setNo_pre(null);
		stru_premi.setMri(false);
		if(ll_bill == 0){
			Account_recur account_recur = bacDao.select_account_recur(spaj);
			if(account_recur!=null){
				if(account_recur.getFlag_autodebet_nb()!=null){
					if(account_recur.getFlag_autodebet_nb()==1){
						return true;
					}else{
						return false;
					}
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
		
		//Buat jurnal sesuai dengan banyaknya titipan premi yang diinput
		for(long i=0;i<lds_bill.size();i++){
			DepositPremium depPre = (DepositPremium) lds_bill.get((int) i);
			stru_premi.setRek_id(depPre.getLsrek_id());
			li_bayar = depPre.getLsjb_id();
			ls_kurs = depPre.getLku_id();
			ldec_bayar = depPre.getMsdp_payment();
			ld_tgl_rk = depPre.getMsdp_date_book();
			ls_no_voucher = depPre.getMsdp_no_voucher();
			ls_no_pre = depPre.getMsdp_no_pre();
			
			if(ld_tgl_rk==null){
				err.reject("","Tanggal R/K Kosong");
				return false;
			}
			
			if(ls_kurs.equals("02")){
				ls_year = defaultDateFormatStripes.format(ld_tgl_rk).substring(6,defaultDateFormatStripes.format(ld_tgl_rk).length());
				ls_month = defaultDateFormatStripes.format(ld_tgl_rk).substring(3,5);
				
				Double kursBulanan = selectLstMonthlyKursLmkNilai(ls_year,ls_month,ls_kurs);
				if(kursBulanan==null){
					err.reject("","Open Lst Monthly Kurs Gagal");
					return false;
				}
				stru_premi.setKurs_bulanan(kursBulanan);
			}else{
				stru_premi.setKurs_bulanan(new Double(1));
			}
			
			if(stru_premi.getKurs_bulanan()==null){
				stru_premi.setKurs_bulanan(new Double(0));
			}
			if(ldec_bayar==null) ldec_bayar=new Double(0);
			ldec_jumlah = new Double(stru_premi.getKurs_bulanan().doubleValue() * ldec_bayar.doubleValue());
			
			//Validasi cara bayar jurnal
			ls_cb = props.getProperty("product.CB_jurnal");
			Integer pos;
			String ls_bayar = f2.format(li_bayar);
			pos = ls_cb.indexOf(ls_bayar);
			if(pos>=0){
				String param = "individu";
				String lsbs = uwDao.selectBusinessId(spaj);
				if(products.stableLink(lsbs)) param = "ulink";
				Premi premi_payment = new Premi();
				List payment = this.uwDao.selectDetailPayment(spaj, 1, 1, param);
				Integer li_row = this.uwDao.selectTagPaymentCount(spaj, 1, 1, param);
				Map mappingPayment = (HashMap) payment.get((int) i);
				if(ls_no_voucher==null){
					if(getBuatVoucherPremiIndividu(stru_premi, ls_no_pre, ls_no_voucher, li_bayar)){
						ls_no_voucher = stru_premi.getVoucher(); //pass by reference
//						//Deddy (17 Feb 2015) - Jurnal TTD : Apabila no_pre dari mst_drek exists, pakai jurnal baru (Jurnal TTD).Apabila tidak ada, pakai jurnal bank yg lama.
						if(!StringUtil.isEmpty(ls_no_pre)){
							lJurnal = getIndividuJm(stru_premi,ldec_jumlah,ld_tgl_rk,li_bayar,i,lusId,ls_no_pre, err,request);
						}else{
							lJurnal = getPreIndividu(stru_premi,ldec_jumlah,ld_tgl_rk,li_bayar,i,lusId,err,request);
						}
						
						if(lJurnal){	
							Premi premi = new Premi();
							Long rk_nocr = null;
							rk_nocr = this.accountingDao.selectNewCrNo("");		// counterAvnel
							premi.setNo_cr(rk_nocr);
							//premi.setNo_cr(stru_premi.getNo_cr());
							premi.setRek_id(stru_premi.getRek_id());
							//updateLst_rek_ekalife(premi);
							
							bacDao.updateMstDepositPremium(stru_premi,new Integer(1),new Long(i+1));
							uwDao.updateMstPaymentJurnalFromSpaj(premi_payment, stru_premi.getDepo_pre(), ls_no_voucher, spaj, new Long(i+1), null, stru_premi.getNo_jm() );
							if(!StringUtil.isEmpty(ls_no_pre)){	
								uwDao.updateNoPreMstDrekDetFromDepositPremium(spaj, (String) mappingPayment.get("MSPA_PAYMENT_ID")  , stru_premi.getDepo_pre(), stru_premi.getNo_jm(), new Long(i+1));
							}else {
								uwDao.updateNoPreMstDrekDetFromDepositPremium(spaj, (String) mappingPayment.get("MSPA_PAYMENT_ID")  , stru_premi.getDepo_pre(), null, new Long(i+1));
							}
							
						}else{
							err.reject("","Terjadi Kesalahan Pada Proses Jurnal!");
							return false;
						}
					}
				}else{//bagian ini untuk set ulang no pre dan voucher ke mst_payment
					uwDao.updateMstPaymentJurnalFromSpaj(premi_payment, depPre.getMsdp_no_pre(), ls_no_voucher, spaj, new Long(i+1), null, stru_premi.getNo_jm() );
					uwDao.updateNoPreMstDrekDetFromDepositPremium(spaj, (String) mappingPayment.get("MSPA_PAYMENT_ID")  , depPre.getMsdp_no_pre(), null, new Long(i+1));
				}
			}else{
				if(li_bayar!=null) lJurnal = true;
			}
		}
		
		return lJurnal;
	}
    
	public Double selectLstMonthlyKursLmkNilai(String year,String month,String lkuId){
		Map param=new HashMap();
		param.put("lmk_year",year);
		param.put("lmk_month",month);
		param.put("lku_id",lkuId);
		return (Double)getSqlMapClientTemplate().queryForObject("elions.bac.select.lst_monthly_kurs.lmk_nilai",param);
	}
	
	public boolean getBuatVoucherPremiIndividu(S_Premi struPremi,String lsNoPre,String lsNoVoucher,Integer liBayar){
		 DecimalFormat fno_Cr = new DecimalFormat ("00000");
		 Boolean lSelect = false;
		 String ls_simbol = "";
		 String ls_cr_bayar;
		 Long ll_cr = null;
		 List list;
		 
		 ls_cr_bayar = f2.format(liBayar);
		 struPremi.setSaldo_akhir(new Double(0));
		 if(!getCekString(ls_cr_bayar,"17,99")){
			 list = selectLstRekEkalife(struPremi.getRek_id());
			 for(int i=0; i<list.size(); i++){
				 Map a = (HashMap)list.get(i);
				 if(a.get("LSREK_SYMBOL")!=null) ls_simbol = (String)a.get("LSREK_SYMBOL");
				 if(a.get("LSREK_NO_CR")!=null) ll_cr = Long.valueOf(a.get("LSREK_NO_CR").toString());
				 struPremi.setNo_cr(ll_cr);
				 struPremi.setAccno((String)a.get("LSREK_GL_NO"));
			 }
			 lSelect = true;
		 }else{
			 struPremi.setAccno("01400203");
		 }
		 struPremi.setAccno(struPremi.getAccno().trim());
		 
		 if(!ls_simbol.equals("")) ls_simbol = ls_simbol.trim();
		 if(lSelect){
			if(ll_cr == null) ll_cr = new Long(0);
			struPremi.setNo_cr(new Long(ll_cr.longValue()+1));
			
			if(ls_simbol.equals("")){
				struPremi.setVoucher(null);
			}else{
				struPremi.setVoucher(ls_simbol+fno_Cr.format(struPremi.getNo_cr())+"R");
				struPremi.setVoucher(struPremi.getVoucher().trim());
		 	}
			if(!StringUtil.isEmpty(lsNoPre)){
				lsNoVoucher = refundDao.selectNoVoucher(lsNoPre);
			}else{
				lsNoVoucher = struPremi.getVoucher();
			}
			struPremi.setVoucher(lsNoVoucher);
		 }
		 return lSelect;		
	}
	
	/*Fungsi : Untuk Membuat Jurnal Produk UnitLink
	 * */
	public boolean getPreUlink(S_Premi stru_premi,Double ar_jumlah,Date ar_tglrk,Integer ar_cr_bayar,long i,String lusId,HttpServletRequest request){

		//Yusuf - 11/06/08 - untuk produk unit link syariah, profit center nya beda
		//Yusuf - 30/03/10 - untuk product stable link efektif 1 apr 2010, profit center nya beda
		//Yusuf - 29/02/12 - Req Gesti Akunting - untuk produk unit link syariah, profit center untuk sisi Debet diambil dari lst_rek_ekalife, untuk sisi kredit 801, bukan 850 lagi
		String lsbs_id = this.selectLsbsId(stru_premi.getNo_spaj());
		String lsdbs_number = this.selectLsdbsNumber(stru_premi.getNo_spaj());
		String profitCenterUlink = "501";
		if(products.unitLinkSyariah(lsbs_id) || products.muamalat(stru_premi.getNo_spaj())) { //syariah
			profitCenterUlink = "801";
		}else if(products.stableLink(lsbs_id)){ //stable link
			profitCenterUlink = "701";
		}

		String ll_no_pre;
		String ls_no_pre, ls_kas = "M", ls_bayar;
		String ls_ket[]=new String[5];
		int li_run = 0;
		Integer liPos=new Integer(2);
		Date ldt_now, ldt_prod;
		Double ldec_saldo, ldec_kosong = new Double(0);
		Double ldec_bayar;//{2}
		DecimalFormat fno_Pre = new DecimalFormat ("00000");
		NumberFormat NumFor2 = new DecimalFormat("#.00;(#,##0.00)"); 
		String awProject[]=new String[3];
		String awBudget[]=new String[3];
		String akProject[]=new String[3];
		String akBudget[]=new String[3];
		
		String lca_id = selectCabangFromSpaj(stru_premi.getNo_spaj());
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(stru_premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
		if((lsbs_id.equals("190") && lsdbs_number.equals("9")) || (lsbs_id.equals("200") && lsdbs_number.equals("7"))) jenis = 3;
		//ldt_prod 	= getTglProduksiNew(ar_tglrk);
		ldt_prod 	= jurnal.getTanggalJurnal(ar_tglrk, lca_id, jenis);
		ldt_now   	= commonDao.selectSysdate();
		//ll_no_pre=selectGetCounter(32, "01");
		//updateMstCounter2(ll_no_pre,32,"01");
		//ls_no_pre 	= sdf_yyMM.format(ldt_now)+ fno_Pre.format(Integer.parseInt(ll_no_pre));
		ls_no_pre   = this.uwDao.selectGetPacGl("nopre");
		
		ldec_saldo 	= new Double(ar_jumlah.doubleValue() + stru_premi.getSaldo_akhir().doubleValue());
		//
		//set session no pre untuk error message 21 june 2007 Himmia
		request.getSession().setAttribute("no_pre", ls_no_pre);
		bacDao.insertMstTBank(ls_no_pre,liPos,ldt_prod,ar_tglrk,stru_premi.getVoucher(),ls_kas,ar_jumlah,lusId,stru_premi.getAccno());
		//
		if(stru_premi.getKurs_bulanan().doubleValue()>1){
			ldec_bayar = new Double(ar_jumlah.doubleValue()/stru_premi.getKurs_bulanan().doubleValue());
			ls_bayar   = "$ " + NumFor2.format(ldec_bayar);
		}else
			ls_bayar="";
		//
		if (stru_premi.getPremike().compareTo(new Integer(1))==0){
			ls_ket[1] = "BANK " + stru_premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ar_tglrk)+ "  " + ls_bayar;
			ls_ket[2] = "TITIPAN PREMI NO. SPAJ " + stru_premi.getNo_spaj() + "  " + ls_bayar;
			ls_ket[3] = "BK " + stru_premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ar_tglrk) + "  " + ls_bayar;
			ls_ket[4] = "TP " + stru_premi.getNo_spaj() + " " + defaultDateFormatStripes.format(ar_tglrk) + "  " + ls_bayar; 	
			ls_ket[1] = ls_ket[1] + " BOOKING";
			ls_ket[3] = ls_ket[3] + " BOOKING";
		}else{
			ls_ket[1] = "BANK " + stru_premi.getNama_pemegang() + "   " + defaultDateFormatStripes.format(ar_tglrk) + "  " + ls_bayar;
			ls_ket[2] = "TITIPAN PREMI SUCC NO. POLIS " + stru_premi.getNo_polis() + "  " + ls_bayar;
			ls_ket[3] = "BK " + stru_premi.getNama_pemegang() + "   " + defaultDateFormatStripes.format(ar_tglrk) + "  " + ls_bayar;
			ls_ket[4] = "TP " + stru_premi.getNo_polis() + " " + defaultDateFormatStripes.format(ar_tglrk) + "  " + ls_bayar;	
		}
		//
		if(stru_premi.getAccno().equals("0"))
			awProject[1]=stru_premi.getAccno();
		else	
			awProject[1]=stru_premi.getAccno().substring(0,3);
		if(stru_premi.getAccno().equals("0"))
			awBudget[1]  = "";
		else
			awBudget[1]  = stru_premi.getAccno().substring(3);
		//
		if(stru_premi.getKurs_bulanan().doubleValue()>1){
			awProject[2] = profitCenterUlink;
			awBudget[2] = "41112";
		}else{
			awProject[2] = profitCenterUlink;
			awBudget[2] = "41111";
		}
		stru_premi.setProject(awProject);
		stru_premi.setBudget(awBudget);
		//
		li_run ++;
		ls_kas = "M";
		//
		bacDao.insertMstDBank(ls_no_pre,li_run,ls_ket[1],ls_kas,ar_jumlah,null, stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		akProject=stru_premi.getProject();
		akBudget=stru_premi.getBudget();
		bacDao.insertMstBVoucher(ls_no_pre,li_run,ls_ket[3],ar_jumlah,ldec_kosong,akProject[1],akBudget[1], stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		//
		li_run++;
		ls_kas="B";
		bacDao.insertMstDBank(ls_no_pre,li_run,ls_ket[2],ls_kas,ar_jumlah,new Integer(1), stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		bacDao.insertMstBVoucher(ls_no_pre,li_run,ls_ket[4],ldec_kosong, ar_jumlah,akProject[2],akBudget[2], stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		//
		stru_premi.setDepo_pre(ls_no_pre);
		stru_premi.setSaldo_akhir(ldec_saldo);
		
		return true;
	}
	
	/*Fungsi : Untuk Membuat Jurnal Produk Individu Non Link(tidak ke jurnal dbank lagi, tapi lgsg masuk ke jm)
	 * Created by : Deddy
	 * Create Date : 16 feb 2015
	 * */
	public boolean getIndividuJm(S_Premi stru_premi, Double ar_jumlah, Date ar_tglrk, Integer ar_cr_bayar, long i, String lusId, String no_pre, BindException err, HttpServletRequest request){

		//Yusuf (10/4/2012) req Derry Laksana via helpdesk #24552: Tolong diperbaiki harusnya profit center ulink bukan 001 tapi 801
		//Yusuf (10/4/2012) req Derry Laksana via helpdesk #24526: Tolong diperbaiki harusnya profit center dmtm bukan 001 tapi 040
		String profitCenter = "001";
		Map detbisnis = (Map) uwDao.selectDetailBisnis(stru_premi.getNo_spaj()).get(0);
		String lsbs_id = (String) detbisnis.get("BISNIS");
		String lsdbs_number = (String) detbisnis.get("DETBISNIS");
		if(products.syariah(lsbs_id, lsdbs_number)) { //syariah
			profitCenter = "801";
		}

		long ll_arr;
		String ls_no_jm, ls_kas = "M", ls_bayar, ls_thn, no_trx = "";
		String ls_ket[]=new String[5];
		String ls_banding[]=new String[3];
		String ls_cr_bayar;
		int li_run = 0;
		Integer li_pos = new Integer(2);
		Date ldt_now, ldt_prod, ldt_date_input, ldt_msdef_date;
		List lsLdt_voucher;
		Double ldec_saldo, ldec_kosong = new Double(0);
		Double ldec_bayar;
		boolean lb_ar;
		
		DecimalFormat fno_Pre = new DecimalFormat ("00000");
		NumberFormat numFor2 = new DecimalFormat("#.00;(#,##0.00)"); 
		String awProject[]=new String[3];
		String awBudget[]=new String[3];
		String akProject[]=new String[3];
		String akBudget[]=new String[3];
		
		String lca_id = selectCabangFromSpaj(stru_premi.getNo_spaj());
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(stru_premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
		if((lsbs_id.equals("190") && lsdbs_number.equals("009")) || (lsbs_id.equals("200") && lsdbs_number.equals("007"))) jenis = 3;
		lb_ar = false;
		ldt_prod=null;
		if (stru_premi.getPremike().compareTo(new Integer(1))==0) {
			ldt_prod = jurnal.getTanggalJurnal(ar_tglrk, lca_id, jenis);
			//Atas permintaan Timotius 23/04/2002
			if (ar_tglrk.getTime() > ldt_prod.getTime() ){
				ldt_prod = ar_tglrk;
			}
		}else{
			ldt_prod=bacDao.selectMstProductionMsproProdDate(stru_premi);
			if(ldt_prod==null){
				ldt_msdef_date=bacDao.selectMstDefaultMsDefDate(new Integer(1));
				//Ambil tgl RK Terakhir
				ar_tglrk=bacDao.selectMstPaymentMspaDateBook(stru_premi);
				//ldt_date_input =akseptasiDao.selectSysdate(); 
				ldt_date_input =commonDao.selectSysdateTrunc(); 
				//
				ldt_prod = ldt_date_input;
				if(ar_tglrk.getTime() <= ldt_msdef_date.getTime()){
					if(ldt_date_input.getTime() >= ldt_msdef_date.getTime())
						ldt_prod = ldt_msdef_date;
				}
			}
		}
		//
		ls_thn = sdfYy.format(ldt_prod);
		if(stru_premi.getVoucher()!=null)
			stru_premi.setVoucher(stru_premi.getVoucher().trim());
		
		ldt_now   	= commonDao.selectSysdate();
		ls_no_jm   = this.uwDao.selectGetPacGl("nojm");
		//
		String jum_pre[];
		if(stru_premi.getJum_pre()!=null){
			jum_pre=new String[stru_premi.getJum_pre().length+1];
			ll_arr=stru_premi.getJum_pre().length;
		}else{
			jum_pre=new String[2];
			ll_arr=0;
		}
		
		if (ll_arr == 0){
			jum_pre[0]=no_pre;
			stru_premi.setJum_pre(jum_pre);
		
		}else{
			ll_arr = ll_arr ;
			jum_pre[(int) ll_arr]=no_pre;
			stru_premi.setJum_pre(jum_pre);
		}
		//
		stru_premi.setDepo_pre(no_pre);
		if (stru_premi.getNo_pre()==null ) 
			stru_premi.setNo_pre(no_pre);
		else
			stru_premi.setNo_pre(stru_premi.getNo_pre() + ", " + no_pre); 
		
		ldec_saldo = new Double(ar_jumlah.doubleValue() + stru_premi.getSaldo_akhir().doubleValue());
		//
		//set session no pre untuk error message 21 june 2007 Himmia
		request.getSession().setAttribute("no_jm", ls_no_jm);
		uwDao.insertMst_ptc_tm(ls_no_jm, 1, ldt_prod, no_pre, lusId);
		//
		if(stru_premi.getKurs_bulanan().doubleValue()>1){
			ldec_bayar = new Double(ar_jumlah.doubleValue()/stru_premi.getKurs_bulanan().doubleValue());
			ls_bayar = " US $ " + numFor2.format(ldec_bayar) + " " + stru_premi.getKurs_bulanan().doubleValue() + " ";
		}else{
			ldec_bayar = ar_jumlah;
			ls_bayar = " ";
		}

		List<DrekDet> listdrekdet = uwDao.selectMstDrekDet(null, stru_premi.getNo_spaj(), null, null);
		for(int j=0; j<listdrekdet.size(); j++){
			DrekDet drekdet = listdrekdet.get(j);
			if(drekdet.getNo_pre().equals(no_pre) && drekdet.getJumlah()==ldec_bayar.doubleValue())
				no_trx = drekdet.getNo_trx();
		}
		
		ArrayList mstbvoucher = Common.serializableList(refundDao.selectMstBvoucher(no_pre, 2));
		HashMap bvoucher = (HashMap) mstbvoucher.get(0);
		String profitCenter2 = (String) bvoucher.get("PROJECT_NO");
		
		ls_cr_bayar=f2.format(ar_cr_bayar);
		if(stru_premi.getPremike().compareTo(new Integer(1))==0){
			if(getCekString(ls_cr_bayar,"17,99")){
				ls_ket[1] = "KOMISI AGEN " + stru_premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ar_tglrk);
				ls_ket[2] = "TITIPAN PREMI NO. SPAJ " + stru_premi.getNo_spaj();
				ls_ket[3] = "KA " + stru_premi.getNama_pemegang().trim()  + "   " + sdf_yyMM.format(ar_tglrk);
				ls_ket[4] = "TP " + stru_premi.getNo_spaj().trim()  + " " + sdf_yyMM.format(ar_tglrk);
				awProject[1]=profitCenter;
				awBudget[1]="400203";
				awProject[2]=profitCenter;
				awBudget[2]="400201";
				stru_premi.setProject(awProject);
				stru_premi.setBudget(awBudget);
			}else{
				ls_banding[1] = sdf_yearMonth.format(ar_tglrk);
				ls_banding[2] = sdf_yearMonth.format(ldt_prod);
				if (lb_ar ){
					ls_ket[1] = "BANK " + stru_premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ar_tglrk)+ "  " + ls_bayar;
					ls_ket[2] = "PIUTANG PREMI NO. SPAJ " + stru_premi.getNo_spaj() + "  " + ls_bayar;
					ls_ket[3] = "BK " + stru_premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ar_tglrk)+ "  " + ls_bayar;
					ls_ket[4] = "AR " + stru_premi.getNo_spaj().trim()  + " " + sdf_yyMM.format(ar_tglrk) + "  " + ls_bayar;

					if(stru_premi.getAccno().equals("0")){
						awProject[1]=stru_premi.getAccno();
						awBudget[1]  = "";
					}else{	
						awProject[1]=stru_premi.getAccno().substring(0,3);
						awBudget[1]  = stru_premi.getAccno().substring(3);
					}	

					awProject[2]=profitCenter;
					awBudget[2]="021002";
					stru_premi.setProject(awProject);
					stru_premi.setBudget(awBudget);
					
				}else{

					ls_ket[1] = "TITIPAN TIDAK DIKETAHUI " + defaultDateFormatStripes.format(ar_tglrk) + ls_bayar + no_trx + " " + stru_premi.getVoucher();
					ls_ket[2] = "TITIPAN PREMI NO. SPAJ " + stru_premi.getNo_spaj();
					ls_ket[3] = "BK " + stru_premi.getNama_pemegang().trim()  + "   " + defaultDateFormatStripes.format(ar_tglrk) + "  " + ls_bayar;
					ls_ket[4] = "TP " + stru_premi.getNo_spaj().trim() + " " + defaultDateFormatStripes.format(ar_tglrk) + "  " + ls_bayar;
					
					if(stru_premi.getKurs_bulanan().doubleValue()>1){
						awProject[1] = profitCenter2;
						awBudget[1] = "48215";
						awProject[2] = profitCenter;
						awBudget[2] = "41112";
					}else{
						awProject[1] = profitCenter2;
						awBudget[1] = "48214";
						awProject[2] = profitCenter;
						awBudget[2] = "41111";
					}
					
					stru_premi.setProject(awProject);
					stru_premi.setBudget(awBudget);
				}
			}
		}else{
			ls_ket[1] = "TITIPAN TIDAK DIKETAHUI " + defaultDateFormatStripes.format(ar_tglrk) + ls_bayar + no_trx + " " + stru_premi.getVoucher();
			ls_ket[2] = "TITIPAN PREMI SUCC NO. POLIS " + stru_premi.getNo_polis();
			ls_ket[3] = "BK " + stru_premi.getNama_pemegang().trim() + " " + sdf_yyMM.format(ar_tglrk);
			ls_ket[4] = "TP " + stru_premi.getNo_polis().trim()  + " " + sdf_yyMM.format(ar_tglrk) + "  " + ls_bayar;

			if(stru_premi.getKurs_bulanan().doubleValue()>1){
				awProject[1] = profitCenter2;
				awBudget[1] = "48215";
				awProject[2] = profitCenter;
				awBudget[2] = "41112";
			}else{
				awProject[1] = profitCenter2;
				awBudget[1] = "48214";
				awProject[2] = profitCenter;
				awBudget[2] = "41111";
			}
			stru_premi.setProject(awProject);
			stru_premi.setBudget(awBudget);
		}
		
		if(StringUtil.isEmpty(awProject[1])){
			return false;
		}
		
		//
		li_run ++;
		ls_kas = "D";
		//
		insertMst_ptc_jm(ls_no_jm, li_run, ls_ket[1], ar_jumlah, awProject[1], awBudget[1], ls_kas, stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		//
		li_run++;
		ls_kas="C";
		insertMst_ptc_jm(ls_no_jm, li_run, ls_ket[2], ar_jumlah, awProject[2], awBudget[2], ls_kas, stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		//
		stru_premi.setSaldo_akhir(ldec_saldo);
		stru_premi.setNo_pre(no_pre);
		stru_premi.setNo_jm(ls_no_jm);
		return true;
	}
	
	/*Fungsi : Untuk Membuat Jurnal Produk UnitLink(tidak ke jurnal dbank lagi, tapi lgsg masuk ke jm)
	 * Created by : Deddy
	 * Create Date : 16 feb 2015
	 * */
	public boolean getUlinkJm(S_Premi stru_premi,Double ar_jumlah,Date ar_tglrk,Integer ar_cr_bayar,long i,String lusId,String no_pre, HttpServletRequest request){
		DecimalFormat fno_Pre = new DecimalFormat ("00000");
		NumberFormat NumFor2 = new DecimalFormat("#.00;(#,##0.00)");
		
		Date ldt_now, ldt_prod;
		String ls_no_jm, ls_kas = "M", ls_bayar, no_trx = "";
		String ls_ket[] = new String[5];
		String awProject[] = new String[3];
		String awBudget[] = new String[3];
		String akProject[] = new String[3];
		String akBudget[] = new String[3];
		Integer li_run = 0;
		Integer liPos = new Integer(2);
		Double ldec_saldo, ldec_kosong = new Double(0);
		Double ldec_bayar;//{2}
		
		String lsbs_id = this.selectLsbsId(stru_premi.getNo_spaj());
		String lsdbs_number = this.selectLsdbsNumber(stru_premi.getNo_spaj());
		String profitCenterUlink = "501";
		if(products.unitLinkSyariah(lsbs_id) || products.muamalat(stru_premi.getNo_spaj())) { //syariah
			profitCenterUlink = "801";
		}else if(products.stableLink(lsbs_id)){ //stable link
			profitCenterUlink = "701";
		}
		
		String lca_id = selectCabangFromSpaj(stru_premi.getNo_spaj());
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(stru_premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
		if((lsbs_id.equals("190") && lsdbs_number.equals("9")) || (lsbs_id.equals("200") && lsdbs_number.equals("7"))) jenis = 3;
		ldt_prod = jurnal.getTanggalJurnal(ar_tglrk, lca_id, jenis);
		ldt_now = commonDao.selectSysdate();
		ls_no_jm = this.uwDao.selectGetPacGl("nojm");
		
		ldec_saldo 	= new Double(ar_jumlah.doubleValue() + stru_premi.getSaldo_akhir().doubleValue());

		//set session no pre untuk error message 21 june 2007 Himmia
		request.getSession().setAttribute("no_jm", ls_no_jm);
		uwDao.insertMst_ptc_tm(ls_no_jm, 1, ldt_prod, no_pre, lusId);

		if(stru_premi.getKurs_bulanan().doubleValue()>1){
			ldec_bayar = new Double(ar_jumlah.doubleValue()/stru_premi.getKurs_bulanan().doubleValue());
			ls_bayar = " US $ " + NumFor2.format(ldec_bayar) + " " + stru_premi.getKurs_bulanan().doubleValue() + " ";
		}else{
			ldec_bayar = ar_jumlah;
			ls_bayar = " ";
		}

		List<DrekDet> listdrekdet = uwDao.selectMstDrekDet(null, stru_premi.getNo_spaj(), null, null);
		for(int j=0; j<listdrekdet.size(); j++){
			DrekDet drekdet = listdrekdet.get(j);
			if(drekdet.getNo_pre().equals(no_pre) && drekdet.getJumlah()==ldec_bayar.doubleValue())
				no_trx = drekdet.getNo_trx();
		}
		
		ArrayList mstbvoucher = Common.serializableList(refundDao.selectMstBvoucher(no_pre, 2));
		HashMap bvoucher = (HashMap) mstbvoucher.get(0);
		String profitCenterUlink2 = (String) bvoucher.get("PROJECT_NO");
		
		if (stru_premi.getPremike().compareTo(new Integer(1))==0){
			ls_ket[1] = "TITIPAN TIDAK DIKETAHUI " + defaultDateFormatStripes.format(ar_tglrk) + ls_bayar + no_trx + " " + stru_premi.getVoucher();
			ls_ket[2] = "TITIPAN PREMI NO. SPAJ " + stru_premi.getNo_spaj();
		}else{
			ls_ket[1] = "TITIPAN TIDAK DIKETAHUI " + defaultDateFormatStripes.format(ar_tglrk) + ls_bayar + no_trx + " " + stru_premi.getVoucher();
			ls_ket[2] = "TITIPAN PREMI SUCC NO. POLIS " + stru_premi.getNo_polis();
		}
		
		if(stru_premi.getKurs_bulanan().doubleValue()>1){
			awProject[1] = profitCenterUlink2;
			awBudget[1] = "48215";
			awProject[2] = profitCenterUlink;
			awBudget[2] = "41112";
		}else{
			awProject[1] = profitCenterUlink2;
			awBudget[1] = "48214";
			awProject[2] = profitCenterUlink;
			awBudget[2] = "41111";
		}
		stru_premi.setProject(awProject);
		stru_premi.setBudget(awBudget);
		
		if(StringUtil.isEmpty(awProject[1])){
			return false;
		}
		
		//
		li_run ++;
		ls_kas = "D";
		//
		insertMst_ptc_jm(ls_no_jm, li_run, ls_ket[1], ar_jumlah, awProject[1], awBudget[1], ls_kas, stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		//
		li_run++;
		ls_kas = "C";
		insertMst_ptc_jm(ls_no_jm, li_run, ls_ket[2], ar_jumlah, awProject[2], awBudget[2], ls_kas, stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		//
		stru_premi.setDepo_pre(no_pre);
		stru_premi.setSaldo_akhir(ldec_saldo);
		stru_premi.setNo_jm(ls_no_jm);
		
		return true;
	}
	
	/*Fungsi : Untuk Membuat Jurnal Produk Individu (Not unitLink)
	 * */
	public boolean getPreIndividu(S_Premi stru_premi,Double ar_jumlah,Date ar_tglrk,Integer ar_cr_bayar,long i,String lusId,BindException err,HttpServletRequest request){

		//Yusuf (10/4/2012) req Derry Laksana via helpdesk #24552: Tolong diperbaiki harusnya profit center ulink bukan 001 tapi 801
		//Yusuf (10/4/2012) req Derry Laksana via helpdesk #24526: Tolong diperbaiki harusnya profit center dmtm bukan 001 tapi 040
		String profitCenter = "001";
		Map detbisnis = (Map) uwDao.selectDetailBisnis(stru_premi.getNo_spaj()).get(0);
		String lsbs_id = (String) detbisnis.get("BISNIS");
		String lsdbs_number = (String) detbisnis.get("DETBISNIS");
		if(products.syariah(lsbs_id, lsdbs_number)) { //syariah
			profitCenter  = "801";
		}else if(stru_premi.getNo_spaj().startsWith("40")){ //dmtm
			profitCenter = "040";
		}

		String ll_no_pre;
		long ll_arr;
		String ls_no_pre, ls_kas = "M", ls_bayar, ls_thn;
		String ls_ket[]=new String[5];
		String ls_banding[]=new String[3];
		String ls_cr_bayar;
		int li_run = 0;
		Integer li_pos = new Integer(2);
		Date ldt_now, ldt_prod, ldt_date_input, ldt_msdef_date;
		List lsLdt_voucher;
		Double ldec_saldo, ldec_kosong = new Double(0);
		Double ldec_bayar;
		boolean lb_ar;
		
		DecimalFormat fno_Pre = new DecimalFormat ("00000");
		NumberFormat numFor2 = new DecimalFormat("#.00;(#,##0.00)"); 
		String awProject[]=new String[3];
		String awBudget[]=new String[3];
		String akProject[]=new String[3];
		String akBudget[]=new String[3];
		
		String lca_id = selectCabangFromSpaj(stru_premi.getNo_spaj());
		Integer jenis = 1;
		
		/** full autosales project start **/
		String flagAutoSalesCek = bacDao.selectFullAutoSalesFromMstPolicy(stru_premi.getNo_spaj());
		if (flagAutoSalesCek != null && flagAutoSalesCek.equals("4")){
			jenis = 4;
		}
		/** full autosales project end **/
		
		if((lsbs_id.equals("190") && lsdbs_number.equals("009"))|| (lsbs_id.equals("200") && lsdbs_number.equals("007"))) jenis = 3;
		lb_ar = false;
		ldt_prod=null;
		if (stru_premi.getPremike().compareTo(new Integer(1))==0) {
			//ldt_prod=getTglProduksiNew(ar_tglrk);
			ldt_prod = jurnal.getTanggalJurnal(ar_tglrk, lca_id, jenis);
			//Atas permintaan Timotius 23/04/2002
			if (ar_tglrk.getTime() > ldt_prod.getTime() ){
				ldt_prod = ar_tglrk;
				//lb_ar 	= True // Atas permintaan Kiki jurnal AR ditutup
			}
		}else{
			ldt_prod=bacDao.selectMstProductionMsproProdDate(stru_premi);
			if(ldt_prod==null){
				ldt_msdef_date=bacDao.selectMstDefaultMsDefDate(new Integer(1));
				//Ambil tgl RK Terakhir
				ar_tglrk=bacDao.selectMstPaymentMspaDateBook(stru_premi);
				//ldt_date_input =akseptasiDao.selectSysdate(); 
				ldt_date_input =commonDao.selectSysdateTrunc(); 
				//
				ldt_prod = ldt_date_input;
				if(ar_tglrk.getTime() <= ldt_msdef_date.getTime()){
					if(ldt_date_input.getTime() >= ldt_msdef_date.getTime())
						ldt_prod = ldt_msdef_date;
				}
			}
		}
		//
		ls_thn = sdfYy.format(ldt_prod);
		if(stru_premi.getVoucher()!=null)
			stru_premi.setVoucher(stru_premi.getVoucher().trim()); 
		
		lsLdt_voucher=bacDao.selectMstTBankTglJurnal(stru_premi.getVoucher(),ls_thn);
		
		if(lsLdt_voucher.isEmpty()==false){
			err.reject("","No Voucher Kembar .. Transfer Ulang");
			return false;
		}
		ldt_now = commonDao.selectSysdate();
		
		//ll_no_pre=selectGetCounter(32, "01");
		//updateMstCounter2(ll_no_pre,32,"01");
		//ls_no_pre 	= sdf_yyMM.format(ldt_now)+ fno_Pre.format(Integer.parseInt(ll_no_pre));
		ls_no_pre   = this.uwDao.selectGetPacGl("nopre");
		//
		String jum_pre[];
		if(stru_premi.getJum_pre()!=null){
			jum_pre=new String[stru_premi.getJum_pre().length+1];
			ll_arr=stru_premi.getJum_pre().length;
		}else{
			jum_pre=new String[2];
			ll_arr=0;
		}
		
		if (ll_arr == 0){
			jum_pre[0]=ls_no_pre;
			stru_premi.setJum_pre(jum_pre);
		
		}else{
			ll_arr = ll_arr ;
			jum_pre[(int) ll_arr]=ls_no_pre;
			stru_premi.setJum_pre(jum_pre);
		}
		//
		stru_premi.setDepo_pre(ls_no_pre);
		if (stru_premi.getNo_pre()==null ) 
			stru_premi.setNo_pre(ls_no_pre);
		else
			stru_premi.setNo_pre(stru_premi.getNo_pre() + ", " + ls_no_pre); 
		
		ldec_saldo = new Double(ar_jumlah.doubleValue() + stru_premi.getSaldo_akhir().doubleValue());
		//
		//set session no pre untuk error message 21 june 2007 Himmia
		request.getSession().setAttribute("no_pre", ls_no_pre);
		bacDao.insertMstTBank(ls_no_pre,li_pos,ldt_prod,ar_tglrk,stru_premi.getVoucher(),ls_kas,ar_jumlah,lusId,stru_premi.getAccno());
		
		//
		if(stru_premi.getKurs_bulanan().doubleValue()>1){
			ldec_bayar = new Double(ar_jumlah.doubleValue()/stru_premi.getKurs_bulanan().doubleValue());
			ls_bayar   = "$ " + numFor2.format(ldec_bayar);
		}else
			ls_bayar="";
		//
		ls_cr_bayar=f2.format(ar_cr_bayar);
		if(stru_premi.getPremike().compareTo(new Integer(1))==0){
			if(getCekString(ls_cr_bayar,"17,99")){
				ls_ket[1] = "KOMISI AGEN " + stru_premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ar_tglrk);
				ls_ket[2] = "TITIPAN PREMI NO. SPAJ " + stru_premi.getNo_spaj();
				ls_ket[3] = "KA " + stru_premi.getNama_pemegang().trim()  + "   " + sdf_yyMM.format(ar_tglrk);
				ls_ket[4] = "TP " + stru_premi.getNo_spaj().trim()  + " " + sdf_yyMM.format(ar_tglrk);
				awProject[1]=profitCenter;
				awBudget[1]="400203";
				awProject[2]=profitCenter;
				awBudget[2]="400201";
				stru_premi.setProject(awProject);
				stru_premi.setBudget(awBudget);
			}else{
				ls_banding[1] = sdf_yearMonth.format(ar_tglrk);
				ls_banding[2] = sdf_yearMonth.format(ldt_prod);
				if (lb_ar ){
					ls_ket[1] = "BANK " + stru_premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ar_tglrk)+ "  " + ls_bayar;
					ls_ket[2] = "PIUTANG PREMI NO. SPAJ " + stru_premi.getNo_spaj() + "  " + ls_bayar;
					ls_ket[3] = "BK " + stru_premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ar_tglrk)+ "  " + ls_bayar;
					ls_ket[4] = "AR " + stru_premi.getNo_spaj().trim()  + " " + sdf_yyMM.format(ar_tglrk) + "  " + ls_bayar;

					if(stru_premi.getAccno().equals("0")){
						awProject[1]=stru_premi.getAccno();
						awBudget[1]  = "";
					}else{	
						awProject[1]=stru_premi.getAccno().substring(0,3);
						awBudget[1]  = stru_premi.getAccno().substring(3);
					}	

					awProject[2]=profitCenter;
					awBudget[2]="021002";
					stru_premi.setProject(awProject);
					stru_premi.setBudget(awBudget);
					
				}else{
					ls_ket[1] = "BANK " + stru_premi.getNama_pemegang().trim() + "   " + defaultDateFormatStripes.format(ar_tglrk) + "  " + ls_bayar;
					ls_ket[2] = "TITIPAN PREMI NO. SPAJ " + stru_premi.getNo_spaj() + "  " + ls_bayar;
					ls_ket[3] = "BK " + stru_premi.getNama_pemegang().trim()  + "   " + defaultDateFormatStripes.format(ar_tglrk) + "  " + ls_bayar;
					ls_ket[4] = "TP " + stru_premi.getNo_spaj().trim() + " " + defaultDateFormatStripes.format(ar_tglrk) + "  " + ls_bayar;
					
					if(stru_premi.getKurs_bulanan().doubleValue()>1){
						if(stru_premi.getAccno().equals("0")){
							awProject[1]=stru_premi.getAccno();
							awBudget[1]  = "";
						}else{	
							awProject[1]=stru_premi.getAccno().substring(0,3);
							awBudget[1]  = stru_premi.getAccno().substring(3);
						}	
						awProject[2]=profitCenter;
						awBudget[2]="41112";
					}else{
						if(stru_premi.getAccno().equals("0")){
							awProject[1]=stru_premi.getAccno();
							awBudget[1]  = "";
						}else{	
							awProject[1]=stru_premi.getAccno().substring(0,3);
							awBudget[1]  = stru_premi.getAccno().substring(3);
						}	
						awProject[2]=profitCenter;
						awBudget[2]="41111";
					}
					stru_premi.setProject(awProject);
					stru_premi.setBudget(awBudget);
				}
				
				ls_ket[1] = ls_ket[1] + " BOOKING";
				ls_ket[3] = ls_ket[3] + " BOOKING";
			}
		}else{
			ls_ket[1] = "BANK " + stru_premi.getNama_pemegang().trim() + "   " + sdf_yyMM.format(ar_tglrk);
			ls_ket[2] = "TITIPAN PREMI SUCC NO. POLIS " + stru_premi.getNo_polis() + "  " + ls_bayar;
			ls_ket[3] = "BK " + stru_premi.getNama_pemegang().trim() + " " + sdf_yyMM.format(ar_tglrk);
			ls_ket[4] = "TP " + stru_premi.getNo_polis().trim()  + " " + sdf_yyMM.format(ar_tglrk) + "  " + ls_bayar;
			//
			if(stru_premi.getKurs_bulanan().doubleValue()>1){
				if(stru_premi.getAccno().equals("0")){
					awProject[1]=stru_premi.getAccno();
					awBudget[1]  = "";
				}else{	
					awProject[1]=stru_premi.getAccno().substring(0,3);
					awBudget[1]  = stru_premi.getAccno().substring(3);
				}	
				awProject[2]=profitCenter;
				awBudget[2]="41122";
			}else{
				if(stru_premi.getAccno().equals("0")){
					awProject[1]=stru_premi.getAccno();
					awBudget[1]  = "";
				}else{	
					awProject[1]=stru_premi.getAccno().substring(0,3);
					awBudget[1]  = stru_premi.getAccno().substring(3);
				}	
				awProject[2]=profitCenter;
				awBudget[2]="41121";
			}
			stru_premi.setProject(awProject);
			stru_premi.setBudget(awBudget);
		}
		//
		li_run ++;
		ls_kas = "M";
		//
		bacDao.insertMstDBank(ls_no_pre,li_run,ls_ket[1],ls_kas,ar_jumlah,null, stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		akProject=stru_premi.getProject();
		akBudget=stru_premi.getBudget();
		bacDao.insertMstBVoucher(ls_no_pre,li_run,ls_ket[3],ar_jumlah,ldec_kosong,akProject[1],akBudget[1], stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		//
		li_run++;
		ls_kas="B";
		bacDao.insertMstDBank(ls_no_pre,li_run,ls_ket[2],ls_kas,ar_jumlah,new Integer(1), stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		bacDao.insertMstBVoucher(ls_no_pre,li_run,ls_ket[4],ldec_kosong, ar_jumlah,akProject[2],akBudget[2], stru_premi.getNo_spaj().replaceAll("\\.","").trim());
		//
		stru_premi.setSaldo_akhir(ldec_saldo);
		stru_premi.setNo_pre(ls_no_pre);
		return true;
	}
	
	public boolean getCekString(String ar_kode,String ar_kal){
		for (int i=0;i<ar_kal.length();i++){
			if(ar_kal.indexOf(ar_kode)>-1)
				return true;
		}
		 return false;
	}
	
	public List selectLstRekEkalife(Integer rekId){
		return getSqlMapClientTemplate().queryForList("elions.bac.select.lst_rek_ekalife",rekId);
	}
	
	public Date getTglProduksiNew(Date tglRk)throws DataAccessException{
		Date ldt_prod_for; Date ldt_tgl_input; Date ldt_close;
		ldt_prod_for = selectMst_default(1);
		ldt_tgl_input = commonDao.selectSysdateTruncated(0);
		if(FormatDate.dateDifference(tglRk, ldt_prod_for, false)>0){
			if(FormatDate.dateDifference(ldt_tgl_input, ldt_prod_for, false)<0){
				ldt_close = selectMst_default(2);
				if(FormatDate.dateDifference(ldt_tgl_input, ldt_close, false)<0){
					ldt_prod_for=null;
				}
				return ldt_prod_for;
			}
		}else{
			return ldt_prod_for;
		}
		
		ldt_prod_for = ldt_tgl_input;
		
		return ldt_prod_for;
	}

	public void updateMstPositionSpajDesc(String spaj,Integer lssaId,String mspsDate,String desc){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("lssaId",lssaId);
		param.put("mspsDate",mspsDate);
		param.put("desc",desc);
		update("update.mst_position_spaj.desc",param);
	}

	public void insertHistoryRekeningNasabah(Rekening_client rekening) {
		insert("insert.mst_rek_hist", rekening);
	}
	
	public void updateMstPolicyMspoNoBlangko(String spaj,String noBlangko){
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("noBlangko",noBlangko);
		update("update.mst_policy.mspo_no_blangko",map);
	}
	
	public void updateMstClientNewKtp(String mcl_id ,String noKtp, String lside_id){
		Map map=new HashMap();
		map.put("mcl_id",mcl_id);
		map.put("noKtp",noKtp);
		map.put("lside_id",lside_id);
		update("update.mst_client_newktp",map);
	}
	
	public void updateMstClientNewSatuKolom(String mcl_id , String nama_kolom, String nilaiUpdate){
		Map map=new HashMap();
		map.put("mcl_id",mcl_id);
		map.put("nama_kolom",nama_kolom);
		map.put("nilai",nilaiUpdate);
		update("update.mst_client_newsatukolom",map);
	}
	
	public void updateMstPolicyRelation(String spaj , String lsre_id){
		Map map=new HashMap();
		map.put("reg_spaj",spaj);
		map.put("lsre_id",lsre_id);
		update("update.mst_policyrelation",map);
	}
	
	public void updateMstClientNewNamaPemegangTertanggung(String mcl_id,String mcl_first){
		Map map=new HashMap();
		map.put("mcl_id",mcl_id);
		map.put("mcl_first",mcl_first);
		update("update.mst_client_new.mcl_firstPemegangTertanggung",map);
	}
	
	public void updateMstClientTgl(String mcl_id ,String tgl, String tempat){
		Map map=new HashMap();
		map.put("mcl_id",mcl_id);
		map.put("tgl",tgl);
		map.put("mspe_place_birth",tempat);
		update("update.mst_client_new.mspe_date_birth",map);
	}
	
	public void updateMstPolicyUmurPemegang(String spaj ,Integer umurP){
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("umurP",umurP);
		update("update.mst_policy.mspo_age",map);
	}
	
	public void updateMstInsuredUmurTertanggung(String spaj ,Integer umurT){
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("umurT",umurT);
		update("update.mst_insured.mste_age",map);
	}
	
	public void updateMstPolicyMspoSpajDate(String spaj,String tglSpaj){
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("tglSpaj",tglSpaj);
		update("update.mst_policy.mspo_spaj_date",map);
	}
	
	public void updateMstAddressNew(AddressNew addNew){
		update("update.mst_address_new",addNew);
	}
	
	public void deleteMstBenefeciary(String spaj){
		delete("delete.mst_benefeciary",spaj);
	}
	
	public void insertMstBenefeciary(Benefeciary benefeciary){
		insert("insert.mst_benefeciary",benefeciary);
	}
	
	//cek lagi masih di pake apa gak?
	public Position selectMstPositionSpajAccepted(String regSpaj,Integer lspdId,Integer lssaId,Integer lsspId){
		Map param=new HashMap();
		param.put("reg_spaj", regSpaj);
		param.put("lspd_id", lspdId);
		param.put("lssa_id", lssaId);
		param.put("lssp_id", lsspId);
		return (Position)querySingle("select.mst_position_spaj_accepted", param);
	}
	public List<Map> selectMstPositionSpajCekStatus(String reg_spaj){
		Map param= new HashMap();
		param.put("reg_spaj",reg_spaj);			
		return  query("selectMstPositionSpajCekStatus", param);
	}
	public List <Map> selectMstPositionSpajCekAksep(String spaj, String flag){
		Map param= new HashMap();
		param.put("reg_spaj",spaj);	
		param.put("flag",flag);		
		return  query("selectMstPositionSpajCekAksep", param);
	}
	
	public Integer selectMstPositionSpajCekAksepCS(String spaj){
			
		return (Integer)querySingle("selectMstPositionSpajCekAksepCS", spaj);
	}	
	
	
	public List<Map> selectDataPemegangPolisSpt(String reg_spaj){
		Map param=new HashMap();
		param.put("reg_spaj", reg_spaj);		
		return query("selectDataPemegangPolisSpt",param);
	}
	public String selectSptUpload(String reg_spaj){
		
		return (String)querySingle("selectSptUpload",reg_spaj);
	}
	
	public String selectDireksi(String reg_spaj){
		
		return (String)querySingle("selectDireksi",reg_spaj);
	}
	
	
	public List selectMstPositionSpajMspsDate(String mspsDate,String lcaId){
		Map param=new HashMap();
		param.put("msps_date", mspsDate);
		param.put("lca_id", lcaId);
		return query("selectAllMstPositionSpajMspsDate", param);
	}

	public Map selectAllSummaryStatus(String spaj,Date mspsDate){
		Map param=new HashMap();
		//param.put("reg_spaj", spaj);
		//param.put("msps_date", mspsDate);
		return (HashMap) querySingle("selectAllSummaryStatus", mspsDate);
	}

	public void insertMstPremiUlink(PremiUlink premiUlink){
		insert("insert.transuw.mst_premi_ulink", premiUlink);
	}
	
	public Double getBiaAkuisisi(Integer lsbsid,Integer lsdbsNumber,Integer lscbId,Integer liPperiod,Integer tahunKe){
		Map param=new HashMap();
		String kode="116, 118, 159, 140, 160, 138, 153, 217,218";
		String bisnisId=f3.format(lsbsid);
		if(kode.indexOf(bisnisId)>0 && lscbId!=0)
			liPperiod=80;
		if(bisnisId.equals("162") && lscbId!=0)
			liPperiod=88;
		
		param.put("lsbs_id", lsbsid);
		param.put("lsdbs_number", lsdbsNumber);
		param.put("lscb_id", lscbId);
		param.put("lbu_lbayar", liPperiod);
		param.put("tahun_ke", tahunKe);
		
		return (Double)querySingle("selectLstBakUlinkAkuisisi", param);	 
	}
	
	public Integer selectMReasTempTipe(String spaj){
		return (Integer)querySingle("transuw.select.eka.m_reas_temp.tipe", spaj);
	}

	public List selectLstUlangan(String spaj){
		return query("selectLstUlangan",spaj);
	}

	public String selectLstPolicyStatus(Integer lsspId){
		return (String)querySingle("selectLstPolicyStatus",lsspId);
	}
	
	public void insertLstUlangan(Ulangan ulangan){
		insert("insert.lst_ulangan2", ulangan);
	}
	
	public void updateMstSimcardAlamat(Simcard simcard){
		update("update.mst_simcard.alamat",simcard);
	}
	
	public Map selectPerusahaanWorksite(String spaj){
		return (HashMap) querySingle("selectPerusahaanWorksite", spaj);
	}
	
	public Integer countkomisiperagen(String kode_agen,String id){
		Map param = new HashMap();
		param.put("kode_agen",kode_agen);
		param.put("id",id);
		return (Integer)querySingle("countkomisiperagen", param);
	}
	
	public Integer countkomisipertglbayar(String kode_agen, String tgl1,String tgl2,String id){
		Map param = new HashMap();
		param.put("kode_agen",kode_agen);
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		param.put("id", id);
		return (Integer)querySingle("countkomisipertglbayar", param);
	}
	
	public Integer countkomisiperttp(String kode_agen, String tgl1,String tgl2,String id){
		Map param = new HashMap();
		param.put("kode_agen",kode_agen);
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		param.put("id", id);
		return (Integer)querySingle("countkomisiperttp", param);
	}
	
	public Integer countkomisipertglproduksi(String kode_agen, String tgl1,String tgl2,String id){
		Map param = new HashMap();
		param.put("kode_agen",kode_agen);
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		param.put("id", id);
		return (Integer)querySingle("countkomisipertglproduksi", param);
	}
	
	public Integer countkomisipercabang(String cabang, String tgl1,String tgl2,String id){
		Map param = new HashMap();
		param.put("cabang",cabang);
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		param.put("id", id);
		return (Integer)querySingle("countkomisipercabang", param);
	}
	
	public Integer countkomisipercabang_all(String tgl1,String tgl2,String id){
		Map param = new HashMap();
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		param.put("id", id);
		return (Integer)querySingle("countkomisipercabang_all", param);
	}
	
	public Integer countproduksiperkodeagen(String kode_agen , String tgl1 ,String tgl2, String id)
	{
		Map param = new HashMap();
		param.put("kode_agen", kode_agen);
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		param.put("id", id);
		return (Integer) querySingle("countproduksiperkodeagen",param);
	}
	
	public Integer countproduksiperlevel(String level ,String tgl1 ,String tgl2, String id)
	{
		Map param = new HashMap();
		param.put("level", level);
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		param.put("id", id);
		return (Integer) querySingle("countproduksiperlevel",param);
	}
	
	public Integer countproduksipercabang(String cabang , String tgl1 ,String tgl2, String id)
	{
		Map param = new HashMap();
		param.put("cabang", cabang);
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		param.put("id", id);
		return (Integer) querySingle("countproduksipercabang",param);
	}	
	
	public Integer countproduksipercabang_all(String tgl1 ,String tgl2, String id)
	{
		Map param = new HashMap(); 
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		param.put("id", id);
		return (Integer) querySingle("countproduksipercabang_all",param);
	}
	
	public Integer countagenbonus( String kode_agen, String tgl1,String tgl2 , String id)
	{
		Map param = new HashMap(); 
		param.put("kode_agen", kode_agen);
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		param.put("id", id);
		return (Integer) querySingle("countagenbonus",param);
	}
	
	public Integer countsoftcopy( String tgl1,String tgl2)
	{
		Map param = new HashMap(); 
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		return (Integer) querySingle("countsoftcopy",param);
	}
	
	public Integer countmonitorpolis( String tgl1,String tgl2)
	{
		Map param = new HashMap(); 
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		return (Integer) querySingle("countmonitorpolis",param);
	}
	
	public Integer countservicepolis( String tgl1,String tgl2)
	{
		Map param = new HashMap(); 
		param.put("tgl1",tgl1);
		param.put("tgl2",tgl2);
		return (Integer) querySingle("countservicepolis",param);
	}
	
	public List selectDaftarkodestamp() throws DataAccessException {
		return query("kode_bea_meterai",null);
	}	
	
	public Map stamp_sekarang()
	{
		return (HashMap) querySingle("stamp_sekarang",null);
	}
	
	/**@Fungsi:	Untuk cari apakah sudah pernah diinput untuk bulan tersebut pada tabel EKA.MST_STAMP
	 * @param 	Stamp stamp
	 * @author  Hemilda Sari Dewi
	 * */
	public Integer count_kode_bea_meterai( String bulan )
	{
		return (Integer) querySingle("count_kode_bea_meterai",bulan);
	}	
	
	/**@Fungsi:	Untuk cek apakah dalam periode tersebut ada pemakaian bea meterai
	 * @param 	String tgl1,String tgl2
	 * @author  Hemilda Sari Dewi
	 * */
	public Integer countbea_perproduk( String tgl1, String tgl2)
	{
		Map m = new HashMap();
		m.put("tgl1",tgl1);
		m.put("tgl2", tgl2);
		return (Integer) querySingle("countbea_perproduk",m);
	}	
	
	/**@Fungsi:	Untuk cari saldo akhir berdasarkan bulan pada tabel EKA.MST_STAMP
	 * @param 	Stamp stamp
	 * @author  Hemilda Sari Dewi
	 * */
	public Double bulan_stamp( String bulan )
	{
		return (Double) querySingle("bulan_stamp",bulan);
	}
	
	/**@Fungsi:	Untuk update mstm_saldo_akhir pada tabel EKA.MST_STAMP
	 * @param 	Stamp stamp
	 * @author  Hemilda Sari Dewi
	 * */
	public void update_mst_stamp (Stamp stamp) throws DataAccessException{
		update("update_mst_stamp",stamp);
	}
	
	/**@Fungsi:	Untuk update kode dirjen pada tabel EKA.MST_STAMP
	 * @param 	Stamp stamp
	 * @author  Hemilda Sari Dewi
	 * */
	public void update_mst_kode_dirjen(Stamp stamp) throws DataAccessException{
		update("update_mst_kode_dirjen",stamp);
	}	
	
	/**@Fungsi:	Untuk cari bulan berdasarkan kode pada tabel EKA.MST_STAMP
	 * @param 	Stamp stamp
	 * @author  Hemilda Sari Dewi
	 * */
	public String kode_stamp( String kode )
	{
		return (String) querySingle("kode_stamp",kode);
	}

	/**@Fungsi:	Untuk isi row berdasarkan kode pada tabel EKA.MST_STAMP
	 * @param 	Stamp stamp
	 * @author  Hemilda Sari Dewi
	 * */
	public Stamp detil_kode_stamp( String kode )throws DataAccessException
	{
		return  (Stamp) querySingle("detil_kode_stamp",kode);
	}
	
	public Integer ceklevelagen(String kode_agen){
		return (Integer)querySingle("ceklevelagen", kode_agen);
	}
	
	/**@Fungsi:	Untuk Mengupdate Kode Pos rumah pada tabel EKA.MST_ADDRESS_NEW
	 * @param 	String spaj, String kdPosRumah
	 * @author	Ferry Harlim
	 * */
	public void updateMstAddressNewKdPosRumah(String mclId,String kdPosRumah){
		Map param=new HashMap();
		param.put("mcl_id", mclId);
		param.put("kd_pos_rumah", kdPosRumah);
		update("update.mst_address_new_kd_pos_rumah", param);
	}
	/**@Fungsi:	Untuk Menampilkan semua data Pada Tabel EKA.MST_INSURED
	 * @param 	String spaj
	 * @return Map
	 * @author	Ferry Harlim
	 * */
	public Map selectAllMstInsured(String spaj){
		return (HashMap)querySingle("selectAllMstInsured", spaj);
	}
	
	/**@Fungsi:	Untuk Menampilkan semua data Pada Tabel EKA.MST_SIMULTANEOUS
	 * @param 	String spaj
	 * @return 	List
	 * @author	Ferry Harlim
	 * */
	public List selectAllMstSimultaneous(String spaj){
		return query("selectAllMstSimultaneous", spaj);
	}
	
	/**@Fungsi:	Untuk Menghapus data pada tabel EKA.MST_SIMULTANEOUS
	 * @param String spaj
	 * @author	Ferry Harlim
	 * */
	public void deleteMstSimultaneous(String spaj){
		delete("delete.mst_simultaneous", spaj);
	}
	
	/**@Fungsi:	Untuk Menampilkan semua data Pada Tabel EKA.MST_DET_ULINK
	 * @param 	String spaj
	 * @author	Ferry Harlim
	 * @return List
	 * */
	public List selectAllMstDetUlink(String spaj,String ljiId){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("lji_id", ljiId);
		return query("selectAllMstDetUlink",param);
	}

	/**@Fungsi:	Untuk Menampilkan semua data Pada Tabel EKA.MST_TRANS_ULINK
	 * @param 	String spaj
	 * @return 	List
	 * @author	Ferry Harlim
	 * */
	public List selectAllMstTransUlink(String spaj){
		return query("selectAllMstTransUlink",spaj);
	}
	/**@Fungsi:	Untuk Mengupdate Tanggal pada tabel EKA.MST_INSURED
	 * 			show(0)=untuk update tgl Terima spaj.
	 * 			show(1)=untuk update tgl Kirim polis.
	 * 			show(2)=untuk update tgl SPAJ DOC
	 * 			show(3)=untuk tgl terima admedika
	 * 			show(4)=untuk tgl terima admin
	 * @param	String spaj,Integer insuredNo, Date tgl,Integer show
	 * @author	Ferry Harlim
	 */
	public void updateMstInsuredTgl(String spaj,Integer insuredNo, Date tgl,Integer show){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("insuredNo", insuredNo);
		param.put("show", show);
		param.put("tgl", tgl);
		update("update.mst_insured.tgl", param);
	}
	
	public void updateMstInsuredTglAdmin(String spaj,Integer insuredNo, Date tanggal,Integer show){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("insuredNo", insuredNo);
		param.put("show", show);
		param.put("tanggal", tanggal);
		update("update.mst_insured.tglAdmin", param);
	}
	
	public void updateMstPolicyTtp(String spaj,Date tanggal){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("tanggal", tanggal);
		update("update.mst_policy.tglTtp", param);
	}
	
	/**@Fungsi:	Untuk Menampilkan nilai NAB dari suatu polis
	 * @param 	String spaj,Integer muKe
	 * @return	List
	 * @author	Ferry Harlim	
	 */
	public List selectJnsLinkAndNab(String spaj,Integer muKe){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("mu_ke", muKe);
		return query("selectJnsLinkAndNab", param);
	}
	/**@Fungsi 	Untuk mengupdate tanggal dan user id pada tabel
	 * 			EKA.MST_POSITION_SPAJ
	 * @param 	String spaj,String userId,String tgl (dd/mm/yyyy) String desc
	 * @author	Ferry Harlim
	 */
	public int updateMstpositionSpajTgl(String spaj,String userId,String tglkirim ,String desc,String keterangan){
		Map param=new HashMap();
		param.put("lus_id", userId);
		param.put("spaj", spaj);
		param.put("tglkirim",tglkirim);
		param.put("msps_desc", desc);
		param.put("keterangan", keterangan);
		return update("update.mst_position_spaj.tgl", param);
	}
	
	public List selectMstProductInsuredExtra(String spaj){
		return query("selectMstProductInsuredExtra", spaj);
	}
	/**Fungsi:	Untuk Menampilkan data rider-rider 810-818 yang tidak di reas oleh sistem.
	 * @param 	Date begDate, Integer lsbsId1, Integer lsbsId2
	 * @return 	List
	 * @author	Ferry Harlim
	 */
	public List selectAllRiderCheckReas(String begDate,Integer lsbsId1, Integer lsbsId2){
		Map param=new HashMap();
		param.put("begDate", begDate);
		param.put("lsbsId1", lsbsId1);
		param.put("lsbsId2", lsbsId2);
		return query("selectAllRiderCheckReas", param);
	}
	/**Fungsi:	Untuk menampilkan data rider-rider secara detail (Nama Tertanggung, No Polis dan Nama Produk)
	 * @param	String reg_spaj, Integer lsbs_id, Integer lsdbs_number
	 * @return	java.util.HashMap
	 * @author 	Ferry Harlim
	 */
	public Temp selectAllRiderCheckReasDetail(Temp upData){
		return (Temp)querySingle("selectAllRiderCheckReasDetail", upData);
	}
	
	public List selectAllRiderCheckReasNew(String begDate,String endDate,String begDateTime,String endDateTime,String unitlink){
		Map param=new HashMap();
		param.put("beg_date", begDate);
		param.put("end_date", endDate);
		param.put("beg_date_time", begDateTime);
		param.put("end_date_time", endDateTime);
		param.put("unitlink", "in("+unitlink+")");
		return query("selectAllRiderCheckReasNew", param);
	}
	
	public List selectDistinctMSarTempNewBisnisId(String idSar){
		return query("select.distinct.m_sar_temp_new.bisnis_id", idSar);
	}
	
	public List selectDistinctMSarTempNewLsgbId(String idSar){
		return query("select.distinct.m_sar_temp_new.lsgb_id", idSar);
	}
	
	public List selectMSarTempNew(String idSar){
		return query("select.m_sar_temp_new",idSar);
	}
	
	public List selectLstGroupBisnis(){
		return query("select.lst_group_bisnis", null);
	}
	/**Fungsi:	Untuk menampilkan data keterangan pada tabel EKA.MST_POSITION_SPAJ
	 * 			berdasarkan nospaj dan flag terdiri dari 3 buat keterangan
	 * 			flag(0)=untuk update tgl Terima spaj.
	 * 			flag(1)=untuk update tgl Kirim polis.
	 * 			flag(2)=untuk update tgl SPAJ DOC
	 * 
	 * @param	String spaj,Integer flag
	 * @return	String
	 * @author 	Ferry Harlim
	 */
	public String selectMstPositionSpajMspsDesc(String spaj, Integer flag){
		Map param=new HashMap();
		param.put("spaj", spaj);
		if(flag==0)
			param.put("ket", "LIKE '%TGL TERIMA SPAJ%'");
		else if(flag==1){
			param.put("ket", "LIKE '%TGL KIRIM POLIS%'");
		}else if(flag==2){
			param.put("ket", "LIKE '%TGL SPAJ DOC%'");
		}else if(flag==3){
			param.put("ket", "LIKE '%TGL TERIMA ADMEDIKA%'");
		}else if(flag==4){
			param.put("ket", "LIKE '%TGL INPUT TTP%'");
		}else if(flag==5){
			param.put("ket", "LIKE '%SUDAH PERNAH DAPAT SIMAS CARD%'");
		}else if(flag==6){
			param.put("ket", "LIKE '%BEA METERAI UNTUK SOFTCOPY POLIS NOMOR%'");
		}else if(flag==7){
			param.put("ket", "LIKE '%PRINT KWITANSI%'");
		}else if(flag==8){
			param.put("ket", "LIKE '%TGL KIRIM SK DEBET%'");
		}
		return (String)querySingle("selectMstPositionSPajMspsDesc", param);
	}
	
	public Integer selectMstPositionSpajOtorisasi(String spaj, Integer msl_tu_ke){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("ket", "LIKE '%OTORISASI INPUT TOPUP KE-"+msl_tu_ke+"%'");
		return (Integer)querySingle("selectMstPositionSpajOtorisasi", param);
	}

	
	/**Fungsi:	Untuk Mengupdate flag komisi dan tanggal komisi pada tabel EKA.MST_INSURED,
	 * 			msteTglKomisi adalah flag untuk update tanggal komisi 
	 * @param 	String spaj, Integer insuredNo, Integer flagKomisi dan tanggal komisi=sysdate
	 * 			Integer msteTglKomisi 
	 * @author	Ferry Harlim	
	 */
	public void updateMstInsuredKomisi(String spaj, Integer insuredNo,Integer flagKomisi,Integer msteTglKomisi, String lus_id, boolean updatePositionSpaj){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("insuredNo", insuredNo);
		param.put("flagKomisi", flagKomisi);
		param.put("msteTglKomisi", msteTglKomisi);
		update("updateMstInsuredKomisi",param);
		
		if(updatePositionSpaj) uwDao.insertMstPositionSpaj((lus_id == null ? "0" : lus_id), "UPDATE TANGGAL KOMISI", spaj, 0);
	}
	/**Fungsi:	Untuk Menginsured tabel eka.mst_deduct
	 * @param	Deduct deduct
	 * @author 	Ferry Harlim
	 */
	public void insertMstDeduct(Deduct deduct){
		insert("insertMstDeduct", deduct);
	}
	/**Fungsi:	Untuk menampilkan data pada tabel eka.lst_jn_deduct
	 * @author	Ferry Harlim
	 */
	public List selectLstJnDeduct(){
		return query("selectLstJnDeduct",null);
	}
	/**Fungsi:	Untuk Menampilkan data pada tabel eka.mst_deduct
	 * @param	String msco_id
	 * @author 	Ferry Harlim
	 */
	public Deduct selectMstDeduct(String mscoId,Integer msddNumber){
		Map param=new HashMap();
		param.put("msco_id", mscoId);
		param.put("msdd_number", msddNumber);
		return (Deduct)querySingle("selectMstDeduct", param);
	}
	
	/**Fungsi:	Untuk Mengupdate data pada tabel EKA.MST_DEDUCT
	 * @param	Deduct deduct
	 * @author 	Ferry Harlim
	 */
	public void updateMstDeduct(Deduct deduct){
		update("updateMstDeduct", deduct);
	}
	/**Fungsi:	Untuk Menampilkan data produk utama pada tabel eka.lst_bisnis dalam bentuk list
	 * 			Sehingga dapat ditaruh di form select
	 * @return	List 
	 * @author	Ferry Harlim
	 */
	public List selectLstBisnis(){
		return query("selectLstBisnis",null);
	}
	/**Fungsi:	Untuk Menampilkan detail nama product berdasarkan lsbs_id dan lsdbs_number nya
	 * @param	Integer lsbs_id	
	 * @return	List 
	 * @author	Ferry Harlim
	 */
	public List selectLstDetBisnis(Integer lsbs_id){
		return query("selectLstDetBisnis",lsbs_id);
	}
	/**Fungsi:	Untuk Menampilkan nama produk berdasarkan lsbs_id dan lsbs_number
	 * @param 	Integer lsbsId, Integer lsdbsNumber
	 * @return	String
	 * @author	Ferry Harlim
	 */
	public String selectLstDetBisnisNamaProduk(Integer lsbsId, Integer lsdbsNumber){
		Map param=new HashMap();
		param.put("lsbs_id",lsbsId);
		param.put("lsdbs_number",lsdbsNumber);
		return (String) querySingle("selectLstDetBisnisNamaProduk", param);
	}
	/**Fungsi:	Untuk Menampilkan msco_id berdasarkan spaj
	 * @param 	String spaj,Integer tahunKe,Integer premiKe,Integer level
	 * @return	String
	 * @author	Ferry Harlim
	 */
	public String selectMstCommissionMscoId(String spaj,Integer tahunKe,Integer premiKe,Integer level){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("premi_ke",premiKe);
		param.put("tahun_ke",tahunKe);
		param.put("lev_comm",level);
		return (String) querySingle("selectMstCommissionMscoId", param);
	}

	/**Fungsi:	Untuk Menampilkan flag tanggal komisi pada tabel eka.mst_insured 
	 * 			(0)=polis baru masuk ke ttp.
	 * 			(1)=polis balikan dari agency support.
	 * 			(2)=ttp fax telah diterima.
	 * @param 	String spaj
	 * @return	Integer
	 * @author	Ferry Harlim
	 */
	public Integer selectMstInsuredMsteFlagKomisi(String spaj){
		return (Integer) querySingle("selectMstInsuredMsteFlagKomisi", spaj);
	}
	
	/**Fungsi:	Untuk menampilkan data kode cabang, kode bisnis, kode agent
	 * @param	String spaj
	 * @return 	Map
	 * @author	Ferry Harlim
	 */
	public Map selectPolicy_Agent_Product(String spaj){
		return (HashMap)querySingle("selectPolicy_Agent_Product", spaj);
	}
	
	/**Fungsi:	Untuk mengupdate tanggal begdate dan enddate dari tabel eka.mst_insured
	 * @param	String spaj,String begDate, String endDate
	 * @author	Ferry Harlim
	 */
	public void updateMstInsuredBegDateEndDate(String spaj,String begDate, String endDate){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("mste_beg_date", begDate);
		param.put("mste_end_date", endDate);
		update("updateMstInsuredBegDateEndDate", param);
	}
	
	/**Fungsi:	Untuk mengupdate tanggal begdate dan enddate dari tabel eka.mst_product_insured
	 * @param	String spaj,String begDate, String endDate
	 * @author	Ferry Harlim
	 */
	public void updateMstProductInsuredBegDateEndDate(String spaj,String begDate, String endDate){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("mspr_beg_date", begDate);
		param.put("mspr_end_date", endDate);
		update("updateMstProductInsuredBegDateEndDate", param);
	}
	
	public List selectMstUlinkTopupNewForDetBilling(String spaj){
		return query("selectMstUlinkTopupNewForDetBilling",spaj);
	}
	
	public Integer selectMaxMstDetBillingDetKe(String spaj){
		return (Integer)querySingle("selectMaxMstDetBillingDetKe", spaj);
	}
	
	public Agen selectdetilagen(String msag_id)
	{
		return (Agen)querySingle("selectdetilagen", msag_id);
	}		

	public Integer jumlahfollowup(String tgl1,String tgl2,String admin)
	{
		Map param = new HashMap();
		param.put("tgl1",tgl1);
		param.put("tgl2", tgl2);
		param.put("admin", admin);
		return (Integer)querySingle("jumlahfollowup", param);
	}		
	
	public Integer jumlahkematianbayar(String tgl1,String tgl2,String lstb_id, String lus_id)
	{
		Map param = new HashMap();
		param.put("tgl1",tgl1);
		param.put("tgl2", tgl2);
		param.put("lstb_id", lstb_id);
		param.put("lus_id", lus_id);
		return (Integer)querySingle("jumlahkematianbayar", param);
	}		
	
	public Integer jumlahkematianpending(String tgl1,String tgl2,String lstb_id, String lus_id)
	{
		Map param = new HashMap();
		param.put("tgl1",tgl1);
		param.put("tgl2", tgl2);
		param.put("lstb_id", lstb_id);
		param.put("lus_id", lus_id);
		return (Integer)querySingle("jumlahkematianpending", param);
	}	
	
	public Integer jumlahkesehatan(String tgl1,String tgl2,String lstb_id, String lus_id)
	{
		Map param = new HashMap();
		param.put("tgl1",tgl1);
		param.put("tgl2", tgl2);
		param.put("lstb_id", lstb_id);
		param.put("lus_id", lus_id);
		return (Integer)querySingle("jumlahkesehatan", param);
	}
	
	public Integer f_prod_topup(String spaj, Integer prod_ke)
	{
		Map param = new HashMap();
		param.put("spaj", spaj);
		param.put("prod_ke",prod_ke);
		return (Integer) querySingle("f_prod_topup",param);
	}
	
	public Integer selectCountMstPeserta(String spaj)throws DataAccessException{
		return (Integer)querySingle("selectCountMstPeserta", spaj);
	}
	
	public Integer selectCountMstProductInsuredRider(String spaj, Integer lsbsId)throws DataAccessException{
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("lsbs_id", lsbsId);
		return (Integer)querySingle("selectCountMstProductInsuredRider", param);
	}
	
	public Integer selectCountTotalRider(String spaj)throws DataAccessException{
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		return (Integer)querySingle("selectCountTotalRider", param);
	}
	
	/**Fungsi:	Untuk Menampilkan id_range_sar dari tabel eka.lst_medical_range_sar
	 * 			termasuk pada range sar yang ke berapa dari total_sar berdasarkan kursnya
	 * @param 	String lku_id,Double total_sar
	 * @return 	Integer
	 * @author	Ferry Harlim
	 */
	public Integer selectLstMedicalRangeSar(String lku_id,Double total_sar){
		Map param=new HashMap();
		param.put("lku_id", lku_id);
		param.put("total_sar", total_sar);
		return (Integer)querySingle("selectLstMedicalRangeSar", param);
	}
	
	/**Fungsi:	Untuk Menampilkan id_range_age dari tabel eka.lst_medical_range_age
	 * 			termasuk pada range age yang ke berapa dari umur tertanggung
	 * @param 	Integer age
	 * @return 	Integer
	 * @author	Ferry Harlim
	 */
	public Integer selectLstMedicalRangeAge(Integer age){
		return (Integer)querySingle("selectLstMedicalRangeAge",age);
	}
	
	/**Fungsi:	Untuk Menampilkan Kurs dari spaj yang bersangkutan
	 * @param 	String spaj, Integer insuredNo
	 * @return 	String
	 * @author	Ferry Harlim
	 */
	public String selectMstProductInsuredKurs(String spaj, Integer insuredNo){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("insured_no", insuredNo);
		return (String) querySingle("selectMstProductInsuredKurs", param);
	}
	
	public List jenis_topproducer()
	{
		return query("jenis_topproducer",null);
	}
	
	public List list_topproducer(Integer tahun , Integer id)
	{
		Map param = new HashMap();
		param.put("tahun",tahun);
		param.put("id",id);
		return query("list_topproducer",param);
	}
	
	public Integer count_topproducer(Integer tahun , Integer id)
	{
		Map param = new HashMap();
		param.put("tahun",tahun);
		param.put("id",id);
		return (Integer) querySingle("count_topproducer",param);
	}
	
	public List selectDaftarTabelMedis(Integer id_range_age,Integer id_range_sar, Integer active, String lku_id){
		Map param=new HashMap();
		param.put("id_range_age",id_range_age);
		param.put("id_range_sar", id_range_sar);
		param.put("active", active);
		param.put("lku_id", lku_id);
		return query("selectDaftarTabelMedis",param);
	}
	
	public Integer count_softcopy_perhari(String tgl1,String tgl2)
	{
		Map param = new HashMap();
		param.put("tgl1", tgl1);
		param.put("tgl2",tgl2);
		return (Integer)querySingle("count_softcopy_perhari",param);
	}
	/**
	 * 
	 * Dian natalia
	 * @param reg_spaj
	 * @return
	 * @throws DataAccessException
	 * digunakan untuk mencek apakah sudah ada rekening agen
	 */
	public List cekRekAgen(String reg_spaj) throws DataAccessException{
		return query("cekRekAgen",reg_spaj);
	}
	
	public List cekAgenDariAgenNonKomisi(String reg_spaj) throws DataAccessException{
		return query("cekAgenDariAgenNonKomisi",reg_spaj);
	}
	
	public Map namaAgen(String msag_id,String spaj) throws DataAccessException{
		Map param = new HashMap();
		param.put("msag_id", msag_id);
		param.put("spaj", spaj);
		return (HashMap) querySingle("namaAgen",param);
	}
	public List cekSertifikat(String spaj) throws DataAccessException{
		return query("cekSertifikat",spaj);
	}
	
	public List cekBegDateInsured(String msag_id, String spaj) throws DataAccessException{
		Map param = new HashMap();
		param.put("msag_id", msag_id);
		param.put("spaj", spaj);
		return query("cekBegDateInsured",param);
	}
	
	public List cekJnSertifikat(String msag_id, String spaj) throws DataAccessException{
		Map param = new HashMap();
		param.put("msag_id", msag_id);
		param.put("spaj", spaj);
		return query("cekJnSertifikat",param);
	}
	
	public Map cekKdBankRekAgen(String msag_id) throws DataAccessException{
		return (HashMap) querySingle("cekKdBankRekAgen",msag_id);
	}
	
	public List cekRekAgenD(String reg_spaj) throws DataAccessException{
		return query("cekRekAgenD",reg_spaj);
	}
	public String cekKdBankRekAgen2(String spaj)throws DataAccessException{
		List lCekRekAgen=uwDao.cekRekAgen(spaj);
		List lCekAgenNonKomisi=uwDao.cekAgenDariAgenNonKomisi(spaj);
		String agen="";
		Integer jmlY=lCekRekAgen.size();
			agen="";
			for(int i = 0; i<lCekRekAgen.size(); i++){
				Map map=(HashMap)lCekRekAgen.get(i);
				String msag_tabungan=(String)map.get("MSAG_TABUNGAN");
				String lca_id=(String)map.get("LCA_ID");
				String lev_comm=(String)map.get("LEV_COMM");
				Integer lbn_id= (Integer)map.get("LBN_ID");
				String nm_agen=(String)map.get("AGEN");
				String msag_id= (String)map.get("MSAG_ID");

			Map cekkdRek= uwDao.cekKdBankRekAgen(msag_id);
			Integer msag_flag = ((BigDecimal) cekkdRek.get("MSAG_FLAG")).intValue();
			Integer lsbp_id = ((BigDecimal) cekkdRek.get("LSBP_ID")).intValue();
			if ((msag_flag==0)){ 
				if(lsbp_id ==156 || lsbp_id==224)//hrs rekening bank sinarmas
					{agen+=(String)map.get("AGEN")+", ";}
			}else{
				logger.info("boleh lahhhh......");
			}
		}
			//cek dari struktur agen no komisi, langsung pass/lewat - ryan
			if(!lCekAgenNonKomisi.isEmpty() || !lCekAgenNonKomisi.equals("")){
				for(int i = 0; i<lCekAgenNonKomisi.size(); i++){
					Map map=(HashMap)lCekAgenNonKomisi.get(i);
					String msag_id= (String)map.get("MSAG_ID");
					agen+=(String)map.get("AGEN")+", ";
				}
			}
		return agen;
	}
	
	public String cekSetifikatAgen(String spaj)throws DataAccessException{
		String agen="";
		//validasi ini sudah tidak berlaku lagi, karena akan dicek di system LIONS
		/*List cekSer=uwDao.cekSertifikat(spaj);
		List lCekAgenNonKomisi=uwDao.cekAgenDariAgenNonKomisi(spaj);
		Date sysdate=commonDao.selectSysdateTrunc();
		for(int y=0; y<cekSer.size();y++){
			Map map1=(HashMap)cekSer.get(y);
			int msag_komisi = ((BigDecimal) map1.get("MSAG_KOMISI")).intValue();
			String msag_id = (String) map1.get("MSAG_ID");
			int msag_sertifikat = ((BigDecimal) map1.get("MSAG_SERTIFIKAT")).intValue();
			Date msag_berlaku = ((Date) map1.get("MSAG_BERLAKU"));
			Map mAgen= uwDao.namaAgen(msag_id,spaj);
			if (msag_komisi==1){ // cek flag komisi harus 1--> kondis1 yg mau di cek
				List begdatepolis= uwDao.cekBegDateInsured(msag_id,spaj);// masa berlaku sertifikat > begdate insured 
				List cekJnSertifikat= uwDao.cekJnSertifikat(msag_id,spaj);
				// *revisi -> cek tgl sertifikasi, apakah > dari sydate  - ryan
				if(sysdate.after(msag_berlaku)){
					agen="";
				}else{
					agen+=(msag_id)+", ";
				}
			}
		}
		//cek dari struktur agen no komisi, langsung pass/lewat - ryan
		if(!lCekAgenNonKomisi.isEmpty() || !lCekAgenNonKomisi.equals("")){
			for(int i = 0; i<lCekAgenNonKomisi.size(); i++){
				Map map=(HashMap)lCekAgenNonKomisi.get(i);
				String msag_id= (String)map.get("MSAG_ID");
				agen+=(String)map.get("AGEN")+", ";
			}
		}*/
		return agen;
	}
	
	
	public String cekRekAgen2(String spaj)throws DataAccessException{
		List lCekRekAgen=uwDao.cekRekAgen(spaj);
		List lCekAgenNonKomisi=uwDao.cekAgenDariAgenNonKomisi(spaj);
		String agen="";
		Integer jmlY=lCekRekAgen.size();
//		if(jmlY!=null){
		
		//disabled by Yusuf - 24/12/08 - kalo ada agen yg dobel, jumlahnya jadi lebih sedikit yg dicek, bahaya!
//			List lCekRekAgenD=uwDao.cekRekAgenD(spaj); // untuk menghidari agent yang sama muncul lebih dari 1 kali
//			Integer jmlD=lCekRekAgenD.size();
			agen="";
		for(int i = 0; i<lCekRekAgen.size(); i++){
			Map map=(HashMap)lCekRekAgen.get(i);
			String msag_tabungan=(String)map.get("MSAG_TABUNGAN");
			String lca_id=(String)map.get("LCA_ID");
			String lev_comm=(String)map.get("LEV_COMM");
			Integer lbn_id= (Integer)map.get("LBN_ID");
			String nm_agen=(String)map.get("AGEN");
			String msag_id= (String)map.get("MSAG_ID");
			

			if (lca_id.equals("09")||(lca_id.equals(" "))|| lev_comm == null||lev_comm.equals("")){
				agen+=(String)map.get("AGEN")+", ";		
			}else{			
				if (lev_comm.equals("")||lev_comm==null){
//					logger.info("syah.. syahhhh.. saja.....");
				}else{
					if(msag_tabungan == null)msag_tabungan = "";
					if(!msag_tabungan.trim().equals("")){
						agen+=(String)map.get("AGEN")+", ";
					}
					if(lev_comm!=null){
						agen+=(String)map.get("AGEN")+", ";//Update Ryan
					}
					if (lbn_id!=null){
						agen+=(String)map.get("AGEN")+", ";
					}
					
				}
			}
			
//		for(int y = 0; y<jmlD; y++){
//			String agencek= (String)map.get("AGEN");
//			
		}
		
		//cek dari struktur agen no komisi, langsung pass/lewat - ryan
		if(!lCekAgenNonKomisi.isEmpty() || !lCekAgenNonKomisi.equals("")){
			for(int i = 0; i<lCekAgenNonKomisi.size(); i++){
				Map map=(HashMap)lCekAgenNonKomisi.get(i);
				String msag_id= (String)map.get("MSAG_ID");
				agen+=(String)map.get("AGEN")+", ";
			}
		}
		return agen;
	
	}
	
//	public List lCekRekAgen(String reg_spaj) throws DataAccessException{
//		return query("cekRekAgen",reg_spaj);
//	}
	/***
	 * Dian natalia
	 * @param reg_spaj
	 * @return
	 * @throws DataAccessException
	 * digunakan untuk mencek apakahtanggal RK dan tanggal  begdate sama  tau tidak
	 */
	public List cekTglRk(String reg_spaj)throws DataAccessException{
		return (List)query("cekTglRk",reg_spaj);
	}
	public List selectLstGroupBisnis(Integer lstbId){
		return query("selectLstGroupBisnis",lstbId);
	}
	
	/**Fungsi :	Untuk Menampilkan semua group reas 
	 * @author	Ferry Harlim
	 */
	public List selectLstGroupReas(){
		return query("selectLstGroupReas",null);
	}
	
	public List selectMReasTempNew(String spaj)throws DataAccessException{
		Map map=new HashMap();
		map.put("spaj", spaj);
		return query("selectMReasTempNew",map);
	}
	/**Fungsi : Untuk Menampilkan data dari tabel eka.m_sar_Temp_new 
	 * 			yang belum diinsert k reins dan reins product
	 * 
	 * @param spaj
	 * @return
	 * @throws DataAccessException
	 */
	public List selectMReasTempNewNotInsertToReins()throws DataAccessException{
		return query("selectMReasTempNewNotInsertToReins",null);
	}
	
	public Integer selectCountMstReinsProduct(String spaj,Integer lsbsId,Integer lsdbsNumber,Integer insuredNo)throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj",spaj);
		map.put("lsbs_id",lsbsId);
		map.put("lsdbs_number",lsdbsNumber);
		map.put("mste_insured_no",insuredNo);
		return (Integer)querySingle("selectCountMstReinsProduct",map);
	}
	
	public Integer selectCountMstMainReas(String spaj,Integer lsbsId,Integer lsdbsNumber,Integer insuredNo)throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj",spaj);
		map.put("lsbs_id",lsbsId);
		map.put("lsdbs_number",lsdbsNumber);
		map.put("mste_insured_no",insuredNo);
		return (Integer)querySingle("selectCountMstMainReas",map);
	}
	
	public void updateMstReinsLstrId(String spaj, Integer lstrId){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("lstr_id", lstrId);
		update("updateMstReinsLstrId",param);
	}

	public void deleteMstReins(String regSpaj) {
		delete("deleteMstReins",regSpaj);
		
	}

	/**Fungsi: 	Untuk Menampilkan jumlah history dari polis sesuai dengan status accept yang diinginkan
	 * 
	 * @param	spaj
	 * @param 	lssaId
	 * @return 	Integer
	 */
	public Integer selectCountMstPositionSpaj(String spaj, String lssaId) {
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		param.put("lssaId", "in("+lssaId+")");
		return (Integer) querySingle("selectCountMstPositionSpaj", param);
	}
	
	public Integer selectCountTerimaTtp(String spaj) {
		Map param=new HashMap();
		param.put("reg_spaj",spaj);
		return (Integer) querySingle("selectCountTerimaTtp", param);
	}
	
	public List selectHighRiskCustm(){
		return  query("selectHighRiskCustm",null);
	}
	
	public void deleteLstHighRiskCust(Integer lshc_id){
		Map map=new HashMap();
		map.put("lshc_id",lshc_id);
		delete("deleteLstHighRiskCust",map);
	}
	
	public void insertLstHighRiskCust(Hrc hrc){
		insert("insertLstHighRiskCust",hrc);
	}
	
	public void inserthighrisk_cust(String Desc){
		  insert("inserthighrisk_cust",Desc);
	}
	
	public List selectKYCnewBis_utama(String dariTanggal,String sampaiTanggal){
		Map param=new HashMap();
		param.put("dariTanggal", dariTanggal);
		param.put("sampaiTanggal", sampaiTanggal);
		return query("selectKYCnewBis_utama", param);
	}
	
	public List selectReportPep(String dariTanggal,String sampaiTanggal){
		Map param=new HashMap();
		param.put("dariTanggal", dariTanggal);
		param.put("sampaiTanggal", sampaiTanggal);
		return query("selectReportPep", param);
	}
	
	public List selectKYCnewBis_utama_individu(String dariTanggal,String sampaiTanggal){
		Map param=new HashMap();
		param.put("dariTanggal", dariTanggal);
		param.put("sampaiTanggal", sampaiTanggal);
		return query("selectKYCnewBis_utama_individu", param);
	}
	
	public Map selectKYC(String spaj){
		Map param=new HashMap();
		param.put("spaj", spaj);
		return(HashMap) querySingle("selectKYC", param);
	}
	
	public List selectProsesKyc(String reg_spaj){
		Map param=new HashMap();
		param.put("reg_spaj", reg_spaj);
		return query("selectProsesKyc", param);
	}
	
	public List selectKYCnewBis_utamaPK(String dariTanggal,String sampaiTanggal){
		Map param=new HashMap();
		param.put("dariTanggal", dariTanggal);
		param.put("sampaiTanggal", sampaiTanggal);
		return query("selectKYCnewBis_utamaPK", param);
	}
	
	public String selectKYCnewBisJnsTopUp(String spaj){
		return (String)querySingle("selectKYCnewBisJnsTopUp", spaj);
	}
	/**Fungsi	: Utnuk menecek apakah suatu polis yang pekerjaannya termasuk dalam daftar
	 * 				High Risk Customer(HCR)
	 * @param mpnJobDesc
	 * @param mklKerja
	 * @param mklIndustri
	 * @return Integer
	 * @author Ferry Harlim
	 */
	public Integer selectCountLstHighRiskCust(String mpnJobDesc, String mklKerja, String mklIndustri){
		if(mpnJobDesc==null)
			mpnJobDesc="";
		if(mklKerja==null)
			mklKerja="";
		if(mklIndustri==null)
			mklIndustri="";
		
		Map map=new HashMap();
			map.put("arg1", mpnJobDesc.trim());
			map.put("arg2", mklKerja.trim());
			map.put("arg3", mklIndustri.trim());
		return (Integer)querySingle("selectCountLstHighRiskCust", map);
	}
	
	public Double selectJumTop_x(String reg_spaj){
		return (Double) querySingle("selectJumTop_x",reg_spaj);
	}
	
	public User selectMstpositionSpajUserAccept(String spaj){
		return (User)querySingle("selectMstpositionSpajUserAccept",spaj);
	}	
	
	public void updateMstInsuredKycResult(String spaj, Integer insured, String desc,String lusId,
			Integer mste_flag_Yuw, Integer mste_flag_Yukm, Integer mste_flag_YDirec) {
		Map param=new HashMap();
		param.put("reg_spaj", spaj);
		param.put("mste_insured_no", insured);
		param.put("mste_kyc_result", desc);
		param.put("mste_kyc_lus_id", lusId);
		param.put("mste_flag_uw", mste_flag_Yuw);
		param.put("mste_flag_ukm", mste_flag_Yukm);
		param.put("mste_flag_Direc", mste_flag_YDirec);
		update("updateMstInsuredKycResult",param);		
	}

	public void updateProsesKyc(String spaj, Integer insured,String lusId,
			Integer mste_flag_Yuw, Date mste_kyc_date) {
		Map param=new HashMap();
		param.put("reg_spaj", spaj);
		param.put("mste_insured_no", insured);
		param.put("mste_flag_uw", mste_flag_Yuw);
		param.put("mste_kyc_date", mste_kyc_date);
		param.put("lusId", lusId);
		update("updateProsesKyc",param);		
	}
	
	public void updateProsesKycResultKyc(String spaj, Integer insured,String lusId,
			String desc, Date mste_kyc_date) {
		Map param=new HashMap();
		param.put("reg_spaj", spaj);
		param.put("mste_insured_no", insured);
		param.put("desc", desc);
		param.put("mste_kyc_date", mste_kyc_date);
		param.put("lusId", lusId);
		update("updateProsesKycResultKyc",param);		
	}
	
	public void updateMstClientNewKycResult(String insured,String mpn_job_desc, String mkl_kerja, String mkl_industri){
		Map param=new HashMap();
		param.put("mspo_policy_holder", insured);
		param.put("mpn_job_desc", mpn_job_desc);
		param.put("mkl_kerja", mkl_kerja);
		param.put("mkl_industri", mkl_industri);
		update("updateMstClientNewKycResult", param);
	}
	
	public Integer selectCountMstSurrender(String spaj,Integer lstbId){
		Map param=new HashMap();
		param.put("reg_Spaj",spaj);
		param.put("lstb_id",lstbId);
		return (Integer)querySingle("selectCountMstSurrender",param);
	}

	public List selectlstCabangForAkseptasiKhusus() {
		return query("selectlstCabangForAkseptasiKhusus",null);
	}
	
	public List selectlstCabangForBancass() {
		return query("selectlstCabangForBancass",null);
	}

	public List selectlstCabangForAkseptasiKhususToday() {
		return query("selectlstCabangForAkseptasiKhususToday",null);
	}
	
	public Pemegang2 selectPemegangPolisUpdateNasabah(String spaj){
		return (Pemegang2) querySingle("selectPemegangPolisUpdateNasabah", spaj);
	}
	public Tertanggung selectTertanggungUpdateNasabah(String spaj){
		return (Tertanggung)querySingle("selectTertanggungUpdateNasabah", spaj);
	}
	
	public Double selectMstCommissionKomisiAgen(String spaj,Integer levCom,Integer tahunKe, Integer premiKe)throws DataAccessException{
		Map param=new HashMap();
		param.put("reg_spaj", spaj);
		param.put("lev_comm",levCom );
		param.put("msbi_tahun_ke", tahunKe);
		param.put("msbi_premi_ke", premiKe);
		return (Double)querySingle("selectMstCommissionKomisiAgen",param);
		
	}
	
	public void deleteMstDetMedicalNew(String spaj, Integer mpaNumber){
		Map param=new HashMap();
		param.put("reg_spaj", spaj);
		param.put("mpa_number", mpaNumber);
		delete("deleteMstDetMedicalNew",param);
	}
	
	public void deleteMstDetIcdNew(String spaj, Integer mpaNumber){
		Map param=new HashMap();
		param.put("reg_spaj", spaj);
		param.put("mpa_number", mpaNumber);
		delete("deleteMstDetIcdNew",param);
	}	

	public void deleteMstDetHslReasNew(String spaj, Integer mpaNumber){
		Map param=new HashMap();
		param.put("reg_spaj", spaj);
		param.put("mpa_number", mpaNumber);
		delete("deleteMstDetHslReasNew",param);
	}	
	
	public Map selectCekSimultanCounter(String lcaId,Integer mscoNumber)throws DataAccessException{
		Map param=new HashMap();
		param.put("lca_id",lcaId);
		param.put("msco_number",mscoNumber);
		return (HashMap)querySingle("selectCekSimultanCounter",param);
	}
	public String selectIdSimultan(String spaj,Integer pemegang){
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("pemegang",pemegang);
		return (String)querySingle("selectIdSimultan",param);
	}
	public List selectAllLstRegion1(){
		return query("selectAllLstRegion1",null);
	}
	public List selectAllLstCab(){
		return query("selectAllLstCab",null);
	} 
	//lufi-khusus bancass 1
	public List selectAllLstCab3(){
		return query("selectAllLstCab3",null);
	}


	public String selectEmailAddr(String lca_id) throws DataAccessException{
		return (String) querySingle("selectEmailAddr", lca_id);
	}
	
	public String selectEmailCabangPenutup(String nilai, Integer tipe) throws DataAccessException{
		Map map = new HashMap();
		map.put("nilai", nilai);
		map.put("tipe", tipe);//1 berdasarkan spaj, 2 berdasarkan kode lar_id
		return (String) querySingle("selectEmailCabangPenutup", map);
	}
	
	public List selectStampHist(String reg_spaj) throws DataAccessException{
		return query("selectStampHist", reg_spaj);
	}
	public void deletemstStampHist(String spaj) throws DataAccessException{
		delete("delete.mstStampHist", spaj);
	}
	public void mstStampMaterai(String  mstm_bulan) throws DataAccessException{
		update("mstStampMaterai", mstm_bulan);
	}
	public String mstm_bulan() throws DataAccessException{
		return (String) querySingle("mstm_bulan", null);
	}

	public void updateMstDetUlinkInvestimax(String reg_spaj,Double mduSaldoUnit,Double mduSaldoUnitPp)throws DataAccessException{
		Map map=new HashMap();
		map.put("mdu_saldo_unit",mduSaldoUnit);
		map.put("mdu_saldo_unit_pp",mduSaldoUnitPp);
		map.put("reg_spaj",reg_spaj);
		update("updateMstDetUlinkInvestimax",map);
	}
	// belom di pakai 
	public List selectMSarTempGroup(String spaj, Integer groupReas) {
	//select *from eka.m_sar_temp where lsbs_id<300 and group_reas=..
		Map map=new HashMap();
		map.put("reg_spaj",spaj);
		map.put("",groupReas);
		return query("selectMSarTempGroup",map);
	}
	
	/**Fungsi : untuk menampilkan rate rider link (>810)
	 * 
	 * @param lsbsId
	 * @param lsrrAge
	 * @param lkuId
	 * @return
	 */
	public Double selectLstRateRider(Integer lsbsId, Integer lsdbsNumber, Integer lsrrAge, String lkuId){
		Map map=new HashMap();
		map.put("lsbs_id",lsbsId);
		map.put("lsdbs_number",lsdbsNumber);
		map.put("lsrr_age",lsrrAge);
		map.put("lku_id",lkuId);
		return (Double) querySingle("selectLstRateRider", map);
	}
	
	public Integer selectCountMstSimultaneous(String spaj){
		return (Integer)querySingle("selectCountMstSimultaneous", spaj);
	}
	
	public String selectMstSimultaneousIdSimultan(String mclId){
		return (String)querySingle("selectMstSimultaneousIdSimultan", mclId);
	}
	public  String createSimultanId() {
		String idSimultan;
//		//insert ke tabel eka.mst_simultaneous
//		//cekin apakah ada simultan polis. setelah itu
//		// idSimultan
//		DecimalFormat f6=new DecimalFormat("000000");
//		String id="01";
//		Integer mscoNumber=78;//counter 
//		//counter (format : yyyy+counter(000000))
//		Map cekSim=(HashMap)selectCekSimultanCounter(id, mscoNumber);
//		String counterNew="000001";
//		BigDecimal flag=(BigDecimal)cekSim.get("FLAG");
//		String counterOld=(String)cekSim.get("COUNTER_OLD");
//		String yyyy=(String)cekSim.get("TAHUN");
//		String mscoValue=(String)cekSim.get("MSCO_VALUE");
//		if(flag.intValue()==1){//cek tahun id_simultan ,reset menjadi 0 
//			idSimultan=yyyy+counterNew;
//		}else{
//			idSimultan=mscoValue;
//		}
//		updateCounter(idSimultan, mscoNumber, id);
		idSimultan = bacDao.selectSequenceSimultan();
		return idSimultan;
			
	}
	
	public List selectSpajLostInsertMstReinsProduct()throws DataAccessException{ 
		return query("selectSpajLostInsertMstReinsProduct",null);
	}

	public Date selectMaxRkDateFromTitipanPremi(String spaj) {
		return (Date) querySingle("selectMaxRkDateFromTitipanPremi", spaj);
		
	}
	
	public Integer selectCountIdSimultan(String idSimultan){
		return (Integer)querySingle("selectCountIdSimultan",idSimultan);
		
	}

	public List selectMstSimultaneousRegSpaj(String idSimultan){
		return query("selectMstSimultaneousRegSpaj",idSimultan);
		
	}
	
	public void updateMstSimultaneousIdSimultan(String idSimultan,String filterId){
		Map map=new HashMap();
		map.put("idSimultan", idSimultan);
		map.put("filterId","in ("+filterId+")");
		update("updateMstSimultaneousIdSimultan",map);
	}

	public void updateMstSimultaneousIdSimultanSendEmail(List lsIdUpdate,String userName){
		String pesan="";
		
		for(int i=0;i<lsIdUpdate.size();i++){
			String spaj="<tr><th>NO SPAJ </th><td>";
			String idSimultan="<tr><th>ID SIMULTAN</th><td>";
			Map map=(HashMap)lsIdUpdate.get(i);
			idSimultan+=(String)map.get("ID_SIMULTAN")+"</td></tr>";
			List lsData=(List)map.get("LS_DATA");
			for(int j=0;j<lsData.size();j++){
				spaj+=(String)lsData.get(j)+",";
			}
			spaj+="</td>";
			pesan+=idSimultan+"</td></tr>"+spaj+"<td></tr>";
		}
		pesan="<table>"+
				pesan+"</table>";
		
//		try {
//			email.send(true, props.getProperty("admin.ajsjava"), new String[] {props.getProperty("admin.yusuf")}, null, null, 
//					"Update Id Simultan (Update by User: "+userName+")", pesan, null);
//		} catch (MailException e) {
//			logger.error("ERROR :", e);
//		} catch (MessagingException e) {
//			logger.error("ERROR :", e);
//		}
		
	}
	
	/**Fungsi : Untuk Menampilkan data reinstatement Policy yang ada di UW
	 * 			request dari dr ingrid
	 * 
	 * @param spaj
	 * @return
	 * @throws DataAccessException
	 * @Date 05/02/2008
	 */
	public List selectReinstatementWorkSheet(String spaj)throws DataAccessException{
		return query("selectReinstatementWorkSheet",spaj);
	}
	
	public List selectReinstatementWorkSheet2(String spaj)throws DataAccessException{
		return query("selectReinstatementWorkSheet2",spaj);
	}

	/**Fungsi : Untuk Mengupdate Tanggal kirim dan tanggal terima dari life benefit
	 * 			
	 * 
	 * @param spaj
	 * @return
	 * @throws DataAccessException
	 * @Date 15/02/2008
	 */
	public void updateMstInsuredReinstatement(String spaj,String tglKirim,String tglTerima)throws DataAccessException{
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("tglKirim",tglKirim);
		map.put("tglTerima",tglTerima);
		update("updateMstInsuredReinstatement",map);
	}
	
	/**Fungsi : Untuk Mencek polis produk simas prima yang terakseptasi khusus
	 * 			jika count >0 maka true
	 * 			else false
	 * @param spaj
	 * @return 
	 * @throws DataAccessException
	 */
	public Integer selectCountProductSimasPrimaAkseptasiKhusus(String spaj,Integer insuredNo,Integer lssaId, Integer jnBank)throws DataAccessException{
		Map map=new HashMap();
		map.put("spaj", spaj);
		map.put("insured_no", insuredNo);
		map.put("lssa_id", lssaId);
		map.put("jn_bank", jnBank);
		return (Integer)querySingle("selectCountProductSimasPrimaAkseptasiKhusus", map);
	}
	
	public Integer selectCountMstBill(String reg_spaj,Integer msbi_tahun_ke , Integer msbi_premi_ke)throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("msbi_tahun_ke", msbi_tahun_ke);
		map.put("msbi_premi_ke", msbi_premi_ke);
		
		return (Integer)querySingle("selectCountMstBill",map);
	}

	/**Fungsi : Untuk menampilkan daftar jenis prefix medical seperti jenis A, B, D, E, F dst
    *
    * @return list
    * @throws DataAccessException
    */
   public List<JenisMedicalVO> selectLstJenisPrefix() throws DataAccessException {
		return (List<JenisMedicalVO>) query("selectLstJenisPrefix",null);
	}

   /**Fungsi : Untuk menampilkan daftar jenis pemeriksaan spt: LPK, Urine Rutin, Rontgen dst
    *
    * @return list
    * @throws DataAccessException
    */
   public List<MedicalCheckupVO> selectMedicalCheckupList() throws DataAccessException {
		return (List<MedicalCheckupVO>) query("selectMedicalCheckupList",null);
	}

   /**Fungsi : Untuk menampilkan daftar jenis pemeriksaan berdasarkan jenis medis
    *          mis: jenis A akan return permeriksaan 1,2,4
    *
    * @return list
    * @throws DataAccessException
    */
   public List<Integer> selectMedicalCheckupListByJenisMedis( Integer jnsMedis ) throws DataAccessException {
		return (List<Integer>) query("selectMedicalCheckupListByJenisMedis", jnsMedis );
	}

   /**Fungsi : Untuk menampilkan nama pemegang polis berdasarkan nomor spaj
    *
    * @return String
    * @throws DataAccessException
    */
   public String selectPolicyHolderNameBySpaj( String spaj ) throws DataAccessException {
		Map person = ( HashMap ) querySingle("selectPersonBySpaj", spaj );
       return ( String ) person.get( "PEMEGANG" );
   }

   /**Fungsi : Untuk menampilkan nama tertanggung berdasarkan nomor spaj
    *
    * @return String
    * @throws DataAccessException
    */
   public String selectInsuredNameBySpaj( String spaj ) throws DataAccessException {
		Map person = ( HashMap ) querySingle("selectPersonBySpaj", spaj );
       return ( String ) person.get( "TERTANGGUNG" );
   }
   
   
   /**
    * untuk mengetahui group reas
    * dian natalia
    * @param bisnisId
    * @return
    * @throws DataAccessException
    */
   public List selectGroupReas(String bisnisId){
		return query("selectGroupReas", bisnisId);
	}
  
   public void insertLstCekValid(CekValidPrintPolis cvpp) throws DataAccessException{
		insert("insertLstCekValid",cvpp);
	}
   
   public void insertMstVoidPayment(VoidPayment voidPayment) throws DataAccessException{
	   insert("insertMstVoidPayment", voidPayment);
   }
   
   public Integer selectCountCekValid(String spaj) throws DataAccessException {
		return (Integer)querySingle("selectCountCekValid",spaj);
	}
   
   public List selectCabangEmail(String cabang) {
		return query("selectEmailCabang", cabang);
	}
   
   public Integer selectIntMonth(String spaj) throws DataAccessException {
		return (Integer)querySingle("selectIntMonth",spaj);
	}
   public Integer selectFlagReas(String spaj) throws DataAccessException {
		return (Integer)querySingle("selectFlagReas",spaj);
	}
   public List selectReasSimultanPowerSave(String id_simultan){
		return query("selectReasSimultanPowerSave",id_simultan);
	}
   
   public void deleteMstDrek(String no_trx) throws DataAccessException {
		Map param=new HashMap();
		param.put("no_trx",no_trx);
		delete("deleteMstDrek",param);
	}
   
   public void deleteCekValid(String reg_spaj) throws DataAccessException {
	   delete("deleteCekValid",reg_spaj);
   }
   
   public String selectIcdDesc(String licId) throws DataAccessException {
	   return (String) querySingle("selectIcdDesc", licId);
	   
   }
   
	public List selectPincab() throws DataAccessException {
		return query("selectPincab",null);
	}  
	
	public String selectEmailPincab_Ao(String kode) throws DataAccessException {
		return (String) querySingle("selectEmailPincab_Ao",kode);
	}    
	
	public List<Map> selectEmailPincab(String kode) throws DataAccessException {
		return query("selectEmailPincab",kode);
	}	
	
	public Integer getRowSs() throws DataAccessException {
		return (Integer) querySingle("getRowSs", null);
	}
	
	public Integer selectVersionViewMedis(String spaj) {
		return (Integer) querySingle("selectVersionViewMedis", spaj);
	}
	
	public List<Map> selectSpajExpire() throws DataAccessException {
		return query("selectSpajExpire", null);
	}
	
	public List<Map> selectTahunFiling() throws DataAccessException {
		return query("selectTahunFiling",null);
	}
	
	public List<Map> selectBulanFiling(String tahun) throws DataAccessException {
		return query("selectBulanFiling",tahun);
		
	}
	
	public void updateMstCompanyWs(Date tgl_bayar, String mcl_id, BigDecimal jumlah_bayar, String periode, BigDecimal nomor)throws DataAccessException{
		Map param=new HashMap();
		param.put("tgl_bayar", tgl_bayar);
		param.put("mcl_id", mcl_id);
		param.put("jumlah_bayar", jumlah_bayar);
		param.put("periode", periode);
		param.put("nomor", nomor);
		update("updateMstCompanyWs", param);
	}
	
	public void updatePrintStableLinkTopup(String spaj, Integer tu_ke)throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("tu_ke",tu_ke);
		update("updatePrintStableLinkTopup", param);
	}
	
	public void updatePrintPSaveTopup(String spaj, Integer tu_ke)throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("tu_ke",tu_ke);
		update("updatePrintPSaveTopup", param);
	}
	
	public List<Map> selectBiiNonSelfGen(String spaj) throws DataAccessException{
		return query("selectBiiNonSelfGen", spaj);
	}
	public Integer selectNoTranSLink(String no_trx, Integer tu_ke) throws DataAccessException{
		Map param=new HashMap();
		param.put("no_trx",no_trx);
		param.put("tu_ke",tu_ke);
		return (Integer) querySingle("selectNoTranSLink", param);
	}
	
	public Integer selectNoTranPsave(String no_trx, Integer tu_ke) throws DataAccessException{
		Map param=new HashMap();
		param.put("no_trx",no_trx);
		param.put("tu_ke",tu_ke);
		return (Integer) querySingle("selectNoTranPsave", param);
	}
	
	public Integer selectMaxSlink(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectNoKeSlink", reg_spaj);
	}
	
	public HashMap selectCabBsm(String kode)throws DataAccessException{
		return (HashMap) querySingle("selectCabBsm", kode);
	}	
	
	public Integer selectExistBlacklist(String tgl_lahir, String nama, String noIdentity)throws DataAccessException{
		
		if("".equals(tgl_lahir))tgl_lahir = null;
		if("".equals(noIdentity))noIdentity = null;
		
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("mspe_no_identity", noIdentity);
		params.put("mcl_first", nama);
		params.put("mspe_date_birth", tgl_lahir);
		return (Integer) querySingle("selectExistBlacklist", params);
		}
	public Integer updateAutodebetNasabah(Account_recur recur) {
		return update("updateAutodebetNasabah", recur);
	}

	public List<Map> selectBank() throws DataAccessException {
		return query("selectBank",null);
	}
	
	public List<Map> selectBankData(Integer jenis) throws DataAccessException {
		return query("selectBankData",jenis);
	}
	public List<Map> selectBankDataSub(Integer jenis) throws DataAccessException {
		return query("selectBankDataSub",jenis);
	}
	public String getBankPusat(String lbn_id) throws DataAccessException {
		return (String) querySingle("getBankPusat", lbn_id);
	}
	
	public Integer getFlagSpecial(String reg_spaj){
		return (Integer) querySingle("getFlagSpecial", reg_spaj);
	}
	
	public Integer selectMstEndorsAutoRider(String reg_spaj){
		return (Integer) querySingle("selectMstEndorsAutoRider", reg_spaj);
	}

	public Map selectExtraMortalita(String reg_spaj) throws DataAccessException {
		return (Map) querySingle("selectExtraMortalita", reg_spaj);
	} 
	
	public Map selectDataAdmedika(String reg_spaj) throws DataAccessException {
		return (Map) querySingle("selectDataAdmedika", reg_spaj);
	}	
	
	public List<Map> selectHealthClaim(String reg_claim, String klaim)throws DataAccessException{
		Map param=new HashMap();
		param.put("klaim",klaim);
		param.put("reg_claim",reg_claim);
		return query("selectHealthClaim", param);
	}
	
	public List<Map> selectTrackingClaimHealth(String req_claim)throws DataAccessException{
		return query("selectTrackingClaimHealth", req_claim);
	}
	
	public List<Map> selectHealthClaimTM(String spaj)throws DataAccessException{
		return query("selectHealthClaimTM", spaj);
	}
	
	public List<Map> selectDaftarIcd(String id, String desc, Integer type) throws DataAccessException {
		Map param=new HashMap();
		param.put("id",id);
		param.put("desc",desc);
		param.put("type",type);
		
		return query("selectDaftarIcd",param);
	}
	
	public List<Map> selectIcdByClassy(String data1, String data2) throws DataAccessException {
		Map param=new HashMap();
		param.put("data1",data1);
		param.put("data2",data2);
		
		return query("selectIcdByClassy",param);
	}
	
	public List<Map> selectDaftarProvider(String name, String addr) throws DataAccessException {
		Map param=new HashMap();
		param.put("name",name);
		param.put("addr",addr);
		
		return query("selectDaftarProvider",param);		
	}
	
	public List<Map> selectHealthClaimEBTolak(String spaj, String insured_no)throws DataAccessException{
		Map<String, Object> params = new HashMap<String, Object>(); 
		/*params.put("spaj", spaj);*/
		params.put("klaim", spaj);
		params.put("insured_no", insured_no);
		return query("selectHealthClaimEBTolak", params);
	}
	
	public List<Map> selectHealthClaimEBAccept(String spaj, String insured_no)throws DataAccessException{
		Map<String, Object> params = new HashMap<String, Object>(); 
		/*params.put("spaj", spaj);*/
		params.put("klaim", spaj);
		params.put("insured_no", insured_no);
		return query("selectHealthClaimEBAccept", params);
	}
	
	public List<Map> selectHealthClaimEBPREAccept(String spaj, String insured_no)throws DataAccessException{
		Map<String, Object> params = new HashMap<String, Object>(); 
		/*params.put("spaj", spaj);*/
		params.put("klaim", spaj);
		params.put("insured_no", insured_no);
		return query("selectHealthClaimEBPREAccept", params);
	}	
	
	public List<Map> selectHealthClaimEBPU(String spaj, String insured_no)throws DataAccessException{
		Map<String, Object> params = new HashMap<String, Object>(); 
		/*params.put("spaj", spaj);*/
		params.put("klaim", spaj);
		params.put("insured_no", insured_no);
		return query("selectHealthClaimEBPU", params);
	}

	public List<Map> selectCekHealthProduct(String nama, Date bod)throws DataAccessException{
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("nama", nama);
		params.put("bod", bod);
		return query("selectCekHealthProduct", params);
	}
	
	public List<Map> selectBlomFlagPrintTopup()throws DataAccessException{
		return query("selectBlomFlagPrintTopup", null);
	}
	
	public List selectTotalTagih(String spaj) throws DataAccessException {
		Map param=new HashMap();
		param.put("spaj",spaj);
//		param.put("tahun_ke",tahun_ke);
//		param.put("premi_ke",premi_ke);
		return  query("selectTotalTagih", param);
	}
	
	public Double selectCariSukuBunga(String lku_id, Date tgl_bayar)throws DataAccessException{
		Map params= new HashMap();
		params.put("lku_id", lku_id);
		params.put("tgl_bayar", tgl_bayar);
		return (Double) querySingle("selectCariSukuBunga", params);
	}
	
	public Double selectCekTahapan(String reg_spaj)throws DataAccessException{
		return (Double) querySingle("selectCekTahapan", reg_spaj);
	}
	
	public Integer selectIsEkaLink(String reg_spaj)throws DataAccessException{
		return (Integer) querySingle("selectIsEkaLink", reg_spaj);
	}
	
	public Integer selectIsUlink(String reg_spaj)throws DataAccessException{
		return (Integer) querySingle("selectIsUlink", reg_spaj);
	}
	
	public List selectBillOSBunga(String reg_spaj, Integer tahun_ke, Integer premi_ke)throws DataAccessException{
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("tahun_ke", tahun_ke);
		params.put("premi_ke", premi_ke);
		return query("selectBillOSBunga", params);
	}
	
	public List selectProductInsured1(String reg_spaj, Integer insured_no)throws DataAccessException{
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		return query("selectProductInsured1", params);
	}
	
	public HashMap selectDataBilling(String reg_spaj)throws DataAccessException{
		return (HashMap) querySingle("selectDataBilling", reg_spaj);
	}
	
	public Double selectDiskPlan(Integer lsbs_id, Integer lsdbs_number, Integer lstht_tahun_ke)throws DataAccessException{
		Map params= new HashMap();
		params.put("lsdbs_number", lsdbs_number);
		params.put("lstht_tahun_ke", lstht_tahun_ke);
		params.put("lsbs_id", lsbs_id);
		return (Double) querySingle("selectDiskPlan", params);
	}

	public Integer selectMonthBetween(Date nextdate, Date begDate)throws  DataAccessException{
		Map params= new HashMap();
		params.put("nextdate", nextdate);
		params.put("begDate", begDate);
		return (Integer) querySingle("selectMonthBetween", params);
	}
	
	public Map getExtraPremi(String spaj, String lsdbs_name) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lsdbs_name", lsdbs_name);
		
		return (Map) querySingle("getExtraPremi", params);
	}

	public String getKetMedis(String spaj) throws DataAccessException {
		return (String) querySingle("getKetMedis", spaj); 
	}
	
	public String getTipeMedis(Integer lstb_id, String currency, String birthDate, String sysDate, BigDecimal sar) throws DataAccessException {
		f_hit_umur hitUmur = new f_hit_umur();
		Integer umur = hitUmur.umur(Integer.parseInt(birthDate.substring(6)),Integer.parseInt(birthDate.substring(3,5)) , Integer.parseInt(birthDate.substring(0,2)), Integer.parseInt(sysDate.substring(6)),Integer.parseInt(sysDate.substring(3,5)) , Integer.parseInt(sysDate.substring(0,2)));
		String kata = "";
		
		if(lstb_id == 1) {
			kata = currency;
			if(umur < 19) {
				kata += " WHERE umur_atas = 19";
			}
			else if(umur >= 20 || umur <= 70) {
				kata += " WHERE umur_bawah <= "+umur+" AND umur_atas >= "+umur;
			}
			
			if(sar.compareTo(new BigDecimal("25000000")) < 0) {
				kata += " AND resiko_atas = 25000000";
			}
			else if(sar.compareTo(new BigDecimal("10000000001")) >= 0) {
				kata += " AND resiko_bawah = 10000000001";
			}
			else {
				kata += "AND resiko_bawah <= "+sar+" AND resiko_atas >= "+sar;
			}
			
			kata = (String) querySingle("selectLstTableMedisIndividu", kata);
		}
		return kata;
	}

	public List<HashMap> selectDaftarPeserta(String spaj) throws DataAccessException {
		return query("selectDaftarPeserta", spaj);
	}
	
	public Map selectEmBmi(Double bmi, Integer umur) throws DataAccessException {
		String kata = "";

		if(umur <= 34) kata = " umur_atas = 34";
		else if(umur >= 70) kata = " umur_bawah = 70";
		else if(umur >= 34 || umur <= 70) kata = "umur_bawah <= "+umur+" AND umur_atas >= "+umur;
		if(bmi <= 14.0) kata += " AND bmi_atas = 34";
		else if(bmi >= 44.0) kata += " AND bmi_bawah = 70";
		else if(bmi >= 14.0 || bmi <= 44.0) kata += " AND bmi_bawah <= "+bmi+" AND bmi_atas >= "+bmi;
		
		return (Map) querySingle("selectEmBmi",kata);
	}
	
	public List<DropDown> selectLstWorksheet(Integer jenis, Integer id) throws DataAccessException {
		Map params= new HashMap();
		params.put("jenis", jenis);
		params.put("id", id);
		
		return query("selectLstWorksheet", params);
	}
	
	public Map selectEmWorksheet(Integer lw_id) throws DataAccessException {
		return (Map) querySingle("selectEmWorksheet",lw_id);
	}
	
	public String selectEmBloodPresure(Integer systolic, Integer diastolic, Integer umur) throws DataAccessException {
		String kata = "";
		
		if(umur < 40) kata = " umur_atas = 40";
		else if(umur > 79) kata = " umur_bawah = 79";
		else if(umur >= 40 || umur <= 79) kata = "umur_bawah <= "+umur+" AND umur_atas >= "+umur;
		if(systolic > 210) kata += " AND systolic_bawah = 210";
		else kata += " AND systolic_bawah <= "+systolic+" AND systolic_atas >= "+systolic;
		if(diastolic < 60) kata += " AND diastolic_atas = 34";
		else if(diastolic > 122) kata += " AND diastolic_bawah = 70";
		else if(diastolic >= 60 || diastolic <= 122) kata += " AND diastolic_bawah <= "+diastolic+" AND diastolic_atas >= "+diastolic;
		
		return (String) querySingle("selectEmBloodPresure", kata);
	}
	
	public String getRatioChol(Integer satuan, Double total, Double hdl, Integer umur) throws DataAccessException {
		String kata = "";
		
		if(satuan == 1) {
			kata = "mgdl";
			
			if(umur < 35) kata += " WHERE umur_atas = 35";
			else if(umur > 74) kata += " WHERE umur_bawah = 74";
			else if(umur > 35 || umur < 74) kata += " WHERE umur_bawah < "+umur+" AND umur_atas > "+umur;
			if(total <= 125.0) kata += " AND total_atas = 125";
			else if(total >= 550.0) kata += " AND total_bawah = 550";
			else if(total > 125.0 || total < 550.0) kata += " AND total_bawah <= "+total+" AND total_atas >= "+total;
			if(hdl < 35.0) kata += " AND hdl_atas = 35";
			else if(hdl > 90.0) kata += " AND hdl_bawah = 90";
			else if(hdl > 35.0 || hdl < 90.0) kata += " AND hdl_bawah <= "+hdl+" AND hdl_atas >= "+hdl;
		}
		else {
			kata = "mmol";
			
			if(umur < 35) kata += " WHERE umur_atas = 35";
			else if(umur > 74) kata += " WHERE umur_bawah = 74";
			else if(umur > 35 || umur < 74) kata += " WHERE umur_bawah < "+umur+" AND umur_atas > "+umur;
			if(total <= 3.2) kata += " AND total_atas = 3.2";
			else if(total >= 14.3) kata += " AND total_bawah = 14.3";
			else if(total > 3.2 || total < 14.3) kata += " AND total_bawah <= "+total+" AND total_atas >= "+total;
			if(hdl < 0.9) kata += " AND hdl_atas = 0.9";
			else if(hdl > 2.34) kata += " AND hdl_bawah = 2.34";
			else if(hdl > 0.9 || hdl < 2.34) kata += " AND hdl_bawah <= "+hdl+" AND hdl_atas >= "+hdl;			
		}
			
		return (String) querySingle("getRatioChol", kata);
	}
	
	public Integer cekMstWorksheet(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return (Integer) querySingle("cekMstWorksheet", params);
	}
	
	public void insertMstWorksheet(UwWorkSheet ws) throws DataAccessException {
		insert("insertMstWorksheet", ws);
	}
	
	public void updateMstWorksheet(UwWorkSheet ws) throws DataAccessException {
		update("updateMstWorksheet", ws);
	}
	
	public Integer cekMstDetNonMedis(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return (Integer) querySingle("cekMstDetNonMedis", params);		
	}
	
	public void insertMstDetNonMedis(UwWorkSheet ws) throws DataAccessException {
		insert("insertMstDetNonMedis",ws);
	}
	
	public void updateMstDetNonMedis(UwWorkSheet ws) throws DataAccessException {
		update("updateMstDetNonMedis", ws);
	}
	
	public Integer cekListQuest(String reg_spaj, Integer insured_no, Integer urut) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urut", urut);
		
		return (Integer) querySingle("cekListQuest", params);		
	}	
	
	public void insertLstWorksheetQuestionnaire(UwQuestionnaire uq) throws DataAccessException {
		insert("insertLstWorksheetQuestionnaire",uq);
	}
	
	public void updateListQuest(UwQuestionnaire uq) throws DataAccessException {
		update("updateListQuest", uq);
	}	
	
	public UwWorkSheet getUwWorksheet(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		if(insured_no == null) return (UwWorkSheet) querySingle("isWorksheetExis", reg_spaj);
		return (UwWorkSheet) querySingle("getUwWorksheet", params);
	}
	
	public UwWorkSheet getWoksheetNonMed(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return (UwWorkSheet) querySingle("getWoksheetNonMed", params);
	}
	
	public List<UwQuestionnaire> getListQuestionnaire(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListQuestionnaire", params);		
	}
	
	public List<HashMap> getListUwDec(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListUwDec", params);			
	}
	
	public List<UwDecisionRider> getListWorkDecRider(String reg_spaj, Integer insured_no, Integer mwd_urut) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("mwd_urut", mwd_urut);
		
		return query("getListWorkDecRider", params);			
	}
	
	public UwDecisionRider getListWorkDecRiderById(String reg_spaj, Integer insured_no, Integer mwd_urut, Integer lsbs_id, Integer lsdbs_number) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("mwd_urut", mwd_urut);
		params.put("lsbs_id", lsbs_id);
		params.put("lsdbs_number", lsdbs_number);
		
		return (UwDecisionRider) querySingle("getListWorkDecRiderById", params);		
	}
	
	public Integer cekMstWorkDec(String reg_spaj, Integer insured_no, Integer mwd_urut) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("mwd_urut", mwd_urut);
		
		return (Integer) querySingle("cekMstWorkDec", params);
	}
	
	public void insertMstWorkDec(UwDecision ud) throws DataAccessException {
		insert("insertMstWorkDec",ud);
	}
	
	public void updateMstWorkDec(UwDecision ud) throws DataAccessException {
		update("updateMstWorkDec", ud);
	}
	
	public void deleteWorkDec(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);		
		delete("deleteWorkDec", params);
	}
	
	public Integer cekMstWorkDecRider(String reg_spaj, Integer insured_no, Integer mwd_urut, Integer mwdr_urut) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("mwd_urut", mwd_urut);
		params.put("mwdr_urut", mwdr_urut);
		
		return (Integer) querySingle("cekMstWorkDecRider", params);
	}
	
	public void insertMstWorkDecRider(UwDecisionRider udr) throws DataAccessException {
		insert("insertMstWorkDecRider", udr);
	}
	
	public void updateMstWorkDecRider(UwDecisionRider udr) throws DataAccessException {
		update("updateMstWorkDecRider", udr);
	}
	
	public void deleteWorkDecRider(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);		
		delete("deleteWorkDecRider", params);
	}
	
	public UwWorkSheet getFinancialStat(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return (UwWorkSheet) querySingle("getFinancialStat", params);
	}
	
	public Integer cekMstFinancialStat(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return (Integer) querySingle("cekMstFinancialStat", params);
	}
	
	public void insertMstFinancialStat(UwWorkSheet ws) throws DataAccessException {
		insert("insertMstFinancialStat",ws);
	}
	
	public void updateMstFinancialStat(UwWorkSheet ws) throws DataAccessException {
		update("updateMstFinancialStat", ws);
	}
	
	public List<UwLpk> getListLpk(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListLpk", params);
	}
	
	public Integer cekListLpk(String reg_spaj, Integer insured_no, Integer urutan) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutan", urutan);
		
		return (Integer) querySingle("cekListLpk", params);
	}
	
	public void insertMedLpk(UwLpk uk) throws DataAccessException {
		insert("insertMedLpk", uk);
	}
	
	public void updateListLpk(UwLpk uk) throws DataAccessException {
		update("updateListLpk", uk);
	}
	
	public List<HashMap> getListRiwPenyakit(String reg_spaj, Integer insured_no, Integer urutanLpk, Integer rp_type) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutanLpk", urutanLpk);
		params.put("rp_type", rp_type);
		
		return query("getListRiwPenyakit",params);
	}
	
	public Integer cekLstRiwPenyakit(String reg_spaj, Integer insured_no, Integer urutan_lpk, Integer rp_urutan, Integer rp_type) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutan_lpk", urutan_lpk);
		params.put("rp_urutan", rp_urutan);
		params.put("rp_type", rp_type);
		
		return (Integer) querySingle("cekLstRiwPenyakit", params);		
	}
	
	public void insertLstRiwPenyakit(UwRiwPenyakit ur) throws DataAccessException {
		insert("insertLstRiwPenyakit",ur);
	}
	
	public void updateLstRiwPenyakit(UwRiwPenyakit ur) throws DataAccessException {
		update("updateLstRiwPenyakit", ur);
	}
	
	public List<UwHiv> getListHiv(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListHiv", params);	
	}
	
	public Integer cekListHiv(String reg_spaj, Integer insured_no, Integer urutan) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutan", urutan);
		
		return (Integer) querySingle("cekListHiv", params);
	}
	
	public void insertMedHiv(UwHiv uh) throws DataAccessException {
		insert("insertMedHiv",uh);
	}
	
	public void updateListHiv(UwHiv uh) throws DataAccessException {
		update("updateListHiv", uh);
	}
	
	public List<UwUrin> getListUrin(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListUrin", params);	
	}
	
	public List<UwAda> getListAda(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListAda", params);	
	}
	
	public Integer cekListUrin(String reg_spaj, Integer insured_no, Integer urutan) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutan", urutan);
		
		return (Integer) querySingle("cekListUrin", params);
	}
	
	public void insertMedUrin(UwUrin ur) throws DataAccessException {
		insert("insertMedUrin",ur);
	}
	
	public void updateListUrin(UwUrin ur) throws DataAccessException {
		update("updateListUrin", ur);
	}	
	
	public Integer cekListAda(String reg_spaj, Integer insured_no, Integer urutan) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutan", urutan);
		
		return (Integer) querySingle("cekListAda", params);
	}
	
	public void insertMedAda(UwAda ua) throws DataAccessException {
		insert("insertMedAda",ua);
	}
	
	public void updateListAda(UwAda ua) throws DataAccessException {
		update("updateListAda", ua);
	}
	
	public List<UwTumor> getListTumor(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListTumor", params);	
	}
	
	public Integer cekListTumor(String reg_spaj, Integer insured_no, Integer urutan) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutan", urutan);
		
		return (Integer) querySingle("cekListTumor", params);
	}
	
	public void insertMedTumor(UwTumor ut) throws DataAccessException {
		insert("insertMedTumor",ut);
	}
	
	public void updateListTumor(UwTumor ut) throws DataAccessException {
		update("updateListTumor", ut);
	}	
	
	public List<UwAbdomen> getListAbdomen(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListAbdomen", params);	
	}
	
	public Integer cekListAbdomen(String reg_spaj, Integer insured_no, Integer urutan) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutan", urutan);
		
		return (Integer) querySingle("cekListAbdomen", params);
	}
	
	public void insertMedAbdomen(UwAbdomen ub) throws DataAccessException {
		insert("insertMedAbdomen",ub);
	}
	
	public void updateListAbdomen(UwAbdomen ub) throws DataAccessException {
		update("updateListAbdomen", ub);
	}	
	
	public List<UwDadaPa> getListDadaPa(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListDadaPa", params);	
	}
	
	public Integer cekListDadaPa(String reg_spaj, Integer insured_no, Integer urutan) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutan", urutan);
		
		return (Integer) querySingle("cekListDadaPa", params);
	}
	
	public void insertMedDadaPa(UwDadaPa udp) throws DataAccessException {
		insert("insertMedDadaPa",udp);
	}
	
	public void updateListDadaPa(UwDadaPa udp) throws DataAccessException {
		update("updateListDadaPa", udp);
	}
	
	public List<UwEkg> getListEkg(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListEkg", params);	
	}
	
	public Integer cekListEkg(String reg_spaj, Integer insured_no, Integer urutan) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutan", urutan);
		
		return (Integer) querySingle("cekListEkg", params);
	}
	
	public void insertMedEkg(UwEkg ue) throws DataAccessException {
		insert("insertMedEkg",ue);
	}
	
	public void updateListEkg(UwEkg ue) throws DataAccessException {
		update("updateListEkg", ue);
	}
	
	public List<UwTreadmill> getListTreadmill(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListTreadmill", params);	
	}
	
	public Integer cekListTreadmill(String reg_spaj, Integer insured_no, Integer urutan) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutan", urutan);
		
		return (Integer) querySingle("cekListTreadmill", params);
	}
	
	public void insertMedTreadmill(UwTreadmill ut) throws DataAccessException {
		insert("insertMedTreadmill",ut);
	}
	
	public void updateListTreadmill(UwTreadmill ut) throws DataAccessException {
		update("updateListTreadmill", ut);
	}
	
	public List<UwMedisLain> getListMedLain(String reg_spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		
		return query("getListMedLain", params);	
	}
	
	public Integer cekListMedLain(String reg_spaj, Integer insured_no, Integer urutan) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("insured_no", insured_no);
		params.put("urutan", urutan);
		
		return (Integer) querySingle("cekListMedLain", params);
	}
	
	public void insertMedLain(UwMedisLain uml) throws DataAccessException {
		insert("insertMedLain",uml);
	}
	
	public void updateListMedLain(UwMedisLain uml) throws DataAccessException {
		update("updateListMedLain", uml);
	}
	
	public String selectExpiredDate(String inputDate, Integer dayAdd) throws DataAccessException {
		Map params= new HashMap();
		params.put("inputDate", inputDate);
		params.put("dayAdd", dayAdd);
		
		return (String) querySingle("selectExpiredDate", params);
	}
	
	public Map getHasilReas(String spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("reg_spaj", spaj);
		params.put("insured_no", insured_no);
		
		return (Map) querySingle("getHasilReas", params);		
	}
		
	public Map selectRujukanMedis(String rsnama, String nama_medis, Integer msw_sex, Integer usia, String jenis) throws DataAccessException {
		Map params= new HashMap();
		params.put("rsnama", rsnama);
		params.put("nama_medis", nama_medis);
		params.put("msw_sex", msw_sex);
		params.put("usia", usia);
		params.put("jenis", jenis);
		
		return (Map) querySingle("selectRujukanMedis", params);
	}

	public Integer getLstbId(String spaj) throws DataAccessException {
		return (Integer) querySingle("getLstbId", spaj);
	}
	
	public Reinsurer getDataReinsurer(Integer lsre_id) throws DataAccessException {
		return (Reinsurer) querySingle("getDataReinsurer", lsre_id);
	}
	
	public void insertNewReinsurer(Reinsurer reinsurer) throws DataAccessException {
		insert("insertNewReinsurer", reinsurer);
	}
	
	public void updateReinsurer(Reinsurer reinsurer) throws DataAccessException {
		update("updateReinsurer",reinsurer);
	}
	
	public String insertMedQuest(MedQuest medQuest) throws DataAccessException {
		insert("insertMedQuest", medQuest);
		
		return "Pernyataan Medis telah disimpan";
	}
	
	public String updateMedQuest(MedQuest medQuest) throws DataAccessException {
		update("updateMedQuest", medQuest);
		
		return "Pernyataan Medis telah diperbarui";
	}
	
	public List<MedQuest> selectMedQuest(String spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("spaj", spaj);
		params.put("insured_no", insured_no);
		
		return query("selectMedQuest",params);
	}
	
	public List<Map> selectListHealth() throws DataAccessException {
		return query("selectListHealth",null);
	}
	
	public List<Map> selectHealthChecklist(String spaj, Integer insured_no) throws DataAccessException {
		Map params= new HashMap();
		params.put("spaj", spaj);
		params.put("insured_no", insured_no);
		
		return query("selectHealthChecklist",params);
	}
	
	public Double selectSumPremiExtra(String spaj) throws DataAccessException {
		return (Double) querySingle("selectAkumulasiPolisBySpaj.sumPremiExtra",spaj);
	}
	
	public Double selectSumPremiRider(String spaj) throws DataAccessException {
		return (Double) querySingle("selectAkumulasiPolisBySpaj.sumPremiRider",spaj);
	}

	public Map select_cektglkirimpolis(String spaj) throws DataAccessException {
		return (Map) querySingle("select_cektglkirimpolis", spaj);
	}
	
	public List<Map> selectCetakPolisMaKemarin() throws DataAccessException {
		return query("selectCetakPolisMaKemarin",null);
	}
	
	public List<Map> selectPolisMaBlmAcp() throws DataAccessException {
		return query("selectPolisMaBlmAcp",null);		
	}
	
	public List<Map> selectAksepUserUW(String spaj) throws DataAccessException {
		return query("selectAksepUserUW", spaj);
	}
	
	public String selectAksepAgen(String spaj) {
		return (String) querySingle("selectAksepAgen", spaj);
	}
	
	public String selectAksepAdmin(String spaj) {
		return (String) querySingle("selectAksepAdmin", spaj);
	}
	
	public List<Map> selectAksepSmsEmail() throws DataAccessException {
		return query("selectAksepSmsEmail",null);
	}
	
	public List<Map> selectPendingAdmedika(String html) throws DataAccessException {
		return query("selectPendingAdmedika",html);
	}	
	
	public List<Map> selectQuotRm() throws DataAccessException {
		return query("selectQuotRm",null);
	}
	
	public List<Map> selectHistQuotRm() throws DataAccessException {
		return query("selectHistQuotRm",null);
	}
	
	public List<Map> selectJpnQuotRm() throws DataAccessException {
		return query("selectJpnQuotRm",null);
	}
	
	public List<Map> selectAgentQuotRm() throws DataAccessException {
		return query("selectAgentQuotRm",null);
	}
	
	public List<Map> selectWsQuotRm() throws DataAccessException {
		return query("selectWsQuotRm",null);
	}
	
	public List<Map> selectTargetList() throws DataAccessException {
		return query("selectTargetList",null);
	}
	
	public ParameterClass selectMstPowersaveBaruDpDate(String spaj){
		return(ParameterClass) getSqlMapClientTemplate().queryForObject("elions.bac.select.mst_powersave_baru_dp_date",spaj);
	}
	
	public Map selectPolisBiasa(String reg_spaj) {
		return (Map) querySingle("selectPolisBiasa", reg_spaj);
	}
	
	public String kodeProductUtama(String spaj) {
		return (String) querySingle("selectKodeProductUtama", spaj);
	}
	public List <Map> selectTitle() throws DataAccessException{
		return query("select_title",null);
	}
	
	public void updateAddressBillingEndors(String address,String kota,String kdpos, String spaj){
		Map map=new HashMap();
		map.put("address",address);
		map.put("kota",kota);
		map.put("kdpos",kdpos);
		map.put("spaj",spaj);
		update("update.addressbillingendors",map);
	}
	public Double selectResultPremi(int ljb_id, String reg_spaj) throws SQLException {
		Map params = new HashMap();
		params.put("ljb_id", new Integer(ljb_id));
		params.put("reg_spaj", reg_spaj);
		Double resultPremi = (Double) getSqlMapClientTemplate().queryForObject("elions.n_prod.selectResultPremi", params);
		return resultPremi;
	}
	
	//questionare DMTM
	public String insertMedQuestDMTM(MedQuest medQuest,MedQuest_tertanggung medQuest_tertanggung,MedQuest_tambah medQuest_tambah,MedQuest_tambah2 medQuest_tambah2,MedQuest_tambah3 medQuest_tambah3,MedQuest_tambah4 medQuest_tambah4,MedQuest_tambah5 medQuest_tambah5,Integer relasi) throws DataAccessException {
		
		if(relasi==1){
			if(medQuest!=null){
				for(int i=0;i<2;i++){
					medQuest.setMste_insured_no(i);
					int update = update("updateMedQuestDMTM", medQuest);
					
					if(update>0){
					}else{
						insert("insertMedQuestDMTM", medQuest);
					}
				}
			}
			
			if(medQuest_tambah!=null){
				medQuest_tambah.setMste_insured_no(2);
				int update = update("updateMedQuestDMTM_tambah", medQuest_tambah);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM_tambah", medQuest_tambah);
				}
			}
			
			if(medQuest_tambah2!=null){
				medQuest_tambah2.setMste_insured_no(3);
				int update = update("updateMedQuestDMTM_tambah2", medQuest_tambah2);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM_tambah2", medQuest_tambah2);
				}
			}
			
			if(medQuest_tambah3!=null){
				medQuest_tambah3.setMste_insured_no(4);
				int update = update("updateMedQuestDMTM_tambah3", medQuest_tambah3);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM_tambah3", medQuest_tambah3);
				}
			}
			
			if(medQuest_tambah4!=null){
				medQuest_tambah4.setMste_insured_no(4);
				int update = update("updateMedQuestDMTM_tambah4", medQuest_tambah4);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM_tambah4", medQuest_tambah4);
				}
			}
			
			if(medQuest_tambah5!=null){
				medQuest_tambah5.setMste_insured_no(5);
				int update = update("updateMedQuestDMTM_tambah5", medQuest_tambah5);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM_tambah5", medQuest_tambah5);
				}
			}
			
		}else{
			if(medQuest!=null){
				medQuest.setMste_insured_no(0);
				int update = update("updateMedQuestDMTM", medQuest);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM", medQuest);
				}
			}
			
			if(medQuest_tertanggung!=null){
				medQuest_tertanggung.setMste_insured_no(1);
				int update = update("updateMedQuestDMTM_tertanggung", medQuest_tertanggung);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM_tertanggung", medQuest_tertanggung);
				}
			}
			
			if(medQuest_tambah!=null){
				medQuest_tambah.setMste_insured_no(2);
				int update = update("updateMedQuestDMTM_tambah", medQuest_tambah);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM_tambah", medQuest_tambah);
				}
			}
			
			if(medQuest_tambah2!=null){
				medQuest_tambah2.setMste_insured_no(3);
				int update = update("updateMedQuestDMTM_tambah2", medQuest_tambah2);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM_tambah2", medQuest_tambah2);
				}
			}
			
			if(medQuest_tambah3!=null){
				medQuest_tambah3.setMste_insured_no(4);
				int update = update("updateMedQuestDMTM_tambah3", medQuest_tambah3);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM_tambah3", medQuest_tambah3);
				}
			}
		
			if(medQuest_tambah4!=null){
				medQuest_tambah4.setMste_insured_no(4);
				int update = update("updateMedQuestDMTM_tambah3", medQuest_tambah4);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM_tambah4", medQuest_tambah4);
				}
			}
			
			if(medQuest_tambah3!=null){
				medQuest_tambah5.setMste_insured_no(4);
				int update = update("updateMedQuestDMTM_tambah3", medQuest_tambah5);
				
				if(update>0){
				}else{
					insert("insertMedQuestDMTM_tambah3", medQuest_tambah5);
				}
			}
		}
		
		return "Pernyataan Medis telah disimpan";
	}
	
	public void insertMstScan(Scan scan){
		insert("insertMstScan", scan);
	}
	
	public Integer selectCountKdScan(String kd_scan){
		return (Integer) querySingle("selectCountKdScan", kd_scan);
	}
	
	public Integer selectNonClearCaseAllMedQuest(String reg_spaj){
		return (Integer) querySingle("selectNonClearCaseAllMedQuest", reg_spaj);
	}
	
	public Integer selectRedFlag(String mcl_id) throws DataAccessException {
		Integer result = (Integer) querySingle("selectRedFlag", mcl_id);
		if(result == null) return -1; 
		else return result;	
	}
	public String selectNamaRiderNya(Integer kd_rd, Integer nm_rd) throws DataAccessException {
		Map params = new HashMap();
		params.put("lsbs_id", kd_rd);
		params.put("lsdbs_number", nm_rd);
		return (String) querySingle("selectNamaRiderNya", params);
	}
	
	//report UW Individu
	//report UW Individu sekarang digabung menjadi 1 kecuali Payroll(Req Ningrum helpdesk 55335)
	public List selectreportUWIndividu(Map params) throws DataAccessException{		
		return query("selectreportUWIndividu", params);
	}
	
	public List selectreportUWIndividu_ws_payroll(String bdate, String edate, String lus_id, String status, String produk) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("status", status);
		m.put("produk", produk);
		return query("selectreportUWIndividu_ws_payroll", m);
	}
//	public List selectreportUWIndividu_Agency(String bdate, String edate, String lus_id, String status, String produk) throws DataAccessException{
//		Map m = new HashMap();
//		m.put("bdate", bdate);
//		m.put("edate", edate);
//		m.put("lus_id", lus_id);
//		m.put("status", status);
//		m.put("produk", produk);
//		return query("selectreportUWIndividu", m);
//	}
//	
//	public List selectreportUWIndividu_Bancass1(String bdate, String edate, String lus_id, String status, String produk) throws DataAccessException{
//		Map m = new HashMap();
//		m.put("bdate", bdate);
//		m.put("edate", edate);
//		m.put("lus_id", lus_id);
//		m.put("status", status);
//		m.put("produk", produk);
//		return query("selectreportUWIndividu_Bancass1", m);
//	}
//	
//	public List selectreportUWIndividu_Bancass2(String bdate, String edate, String lus_id, String status, String produk) throws DataAccessException{
//		Map m = new HashMap();
//		m.put("bdate", bdate);
//		m.put("edate", edate);
//		m.put("lus_id", lus_id);
//		m.put("status", status);
//		m.put("produk", produk);
//		return query("selectreportUWIndividu_Bancass2", m);
//	}
//	
//	public List selectreportUWIndividu_Sekuritas(String bdate, String edate, String lus_id, String status, String produk) throws DataAccessException{
//		Map m = new HashMap();
//		m.put("bdate", bdate);
//		m.put("edate", edate);
//		m.put("lus_id", lus_id);
//		m.put("status", status);
//		m.put("produk", produk);
//		return query("selectreportUWIndividu_Sekuritas", m);
//	}
	
//	public List selectreportUWIndividu_WS_MNC_FCD(String bdate, String edate, String lus_id, String status, String produk) throws DataAccessException{
//		Map m = new HashMap();
//		m.put("bdate", bdate);
//		m.put("edate", edate);
//		m.put("lus_id", lus_id);
//		m.put("status", status);
//		m.put("produk", produk);
//		return query("selectreportUWIndividu_WS_MNC_FCD", m);
//	}
//	
	public Integer selectMst_reas(String spaj) throws DataAccessException {
		Integer result = (Integer) querySingle("selectMst_reas", spaj);
		if(result == null) return -1; 
		else return result;	
	}
	
	public void updateflagMedis(String spaj ,String medis){
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("medis",medis);
		update("update.mst_medical",map);
	}
	public void updatePenghasilan(String mcl_id ,String penghasilan, String mamah){
		Map map=new HashMap();
		map.put("mcl_id",mcl_id);
		map.put("penghasilan",penghasilan);
		map.put("mamah",mamah);
		update("updatePenghasilan",map);
	}
	
	public void updateMspoCallDate(String spaj ,Date tglCall){
		Map map=new HashMap();
		map.put("spaj",spaj);
		map.put("tglCall",tglCall);
		update("updateMspoCallDate",map);
	}
	
	public Integer selectFlagQuestionare(String spaj) throws DataAccessException {
	Integer result = (Integer) querySingle("selectFlagQuestionare", spaj);
	if(result == null) return -1; 
	else return result;	
	}
	
	public Integer selectCallDate(String spaj) throws DataAccessException {
	Integer result = (Integer) querySingle("selectCallDate", spaj);
	if(result == null) return -1; 
	else return result;	
	}
	
	public List<Followup> selectquestionareDMTM(String spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", spaj);
		return query("selectquestionareDMTM", m);
	}
	
	public List<Followup> selectreportDetailSPAJRefundBatal(String bdate, String lus_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("lus_id", lus_id);
		return query("selectreportDetailSPAJRefundBatal", m);
	}
	
	public List<Followup> selectreportSummarySPAJRefundBatal(String bdate, String lus_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("lus_id", lus_id);
		return query("selectreportSummarySPAJRefundBatal", m);
	}
	
	public List<Integer> lc_id_value(Integer id_scan){
		return (List<Integer>) query("lc_id_value", id_scan);
	}
	
	public List<Map> selectMstInbox (String reg_spaj, String tipe){
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("tipe", tipe);
		return query("selectMstInbox", map);
	}
	
	public void updateMstInboxLspdId (String mi_id, Integer lspd_id, Integer lspd_id_from, Integer flag_mi_pos, Integer flag_pending, String reg_spaj, String old_reg_spaj, String mi_desc, Integer flag_validasi){
		Map map = new HashMap();
		map.put("mi_id", mi_id);
		map.put("lspd_id", lspd_id);
		map.put("lspd_id_from", lspd_id_from);
		map.put("flag_mi_pos", flag_mi_pos);
		map.put("flag_pending", flag_pending);
		map.put("reg_spaj", reg_spaj);
		map.put("old_reg_spaj", old_reg_spaj);
		map.put("mi_desc", mi_desc);
		map.put("flag_validasi", flag_validasi);
		update("updateMstInboxLspdId", map);
	}
	
	public Map selectMstJobList(Integer ljj_id, Integer lc_id){
		Map map = new HashMap();
		map.put("ljj_id", ljj_id);
		map.put("lc_id", lc_id);
		return (Map) querySingle("selectMstJobList", map);
	}

	public List<Map> selectHealthClaimEBTolakSum(String spaj, String insured_no)throws DataAccessException{
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("spaj", spaj);
		params.put("insured_no", insured_no);
		return query("selectHealthClaimEBTolakSum", params);
	}
	
	public List<Map> selectHealthClaimEBAcceptSum(String spaj, String insured_no)throws DataAccessException{
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("spaj", spaj);
		params.put("insured_no", insured_no);
		return query("selectHealthClaimEBAcceptSum", params);
	}
	
	public List<Map> selectHealthClaimEBPREAcceptSum(String spaj, String insured_no)throws DataAccessException{
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("spaj", spaj);
		params.put("insured_no", insured_no);
		return query("selectHealthClaimEBPREAcceptSum", params);
	}
	
	public List<Map> selectHealthClaimEBPUSum(String spaj, String insured_no)throws DataAccessException{
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("spaj", spaj);
		params.put("insured_no", insured_no);
		return query("selectHealthClaimEBPUSum", params);
	}
	
	public List<Map> selectHealthClaimSum(String spaj)throws DataAccessException{
		return query("selectHealthClaimSum", spaj);
	}
	
	public List<Map> selectHealthClaimTMSum(String spaj)throws DataAccessException{
		return query("selectHealthClaimTMSum", spaj);
	}
	
	public List selectSimpolExpired() throws DataAccessException {
		return query("selectSimpolExpired",null);
	}
	public String selectStatusScheduler() {
		return (String) querySingle("selectStatusScheduler", null);
	}
	
	public Integer selectPolicy_pribadi(String msag_id) throws DataAccessException {
		Integer result = (Integer) querySingle("selectPolicy_pribadi", msag_id);
		if(result == null) return -1; 
		else return result;	
	}
	public List namaBank(String lbn_id){
		
		return query("selectNamaBank",lbn_id);
	}
	
	public Integer selectInboxChecklistExist(String mi_id, Integer ljj_id,Integer lc_id){
		Map param = new HashMap();
		param.put("mi_id", mi_id);
		param.put("ljj_id", ljj_id);
		param.put("lc_id", lc_id);
		return (Integer) querySingle("selectInboxChecklistExist", param);
	}
	
	public Integer selectInboxCheckingLspdId(String reg_spaj, Integer lspd_id){
		Map param = new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("lspd_id", lspd_id);
		return (Integer) querySingle("selectInboxCheckingLspdId", param);
	}
	
	public Map selectMstInboxExisting(String reg_spaj){
		return(Map) query("selectMstInboxExisting", reg_spaj);
	}
	
	public List selectMstInboxExistingNew(String reg_spaj){
		return query("selectMstInboxExisting", reg_spaj);
	}
	
	//referensi(tambang emas)
	public Integer seleckCekRef(String reg_spaj, String jenis){
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("spaj", reg_spaj);
		params.put("jenis", jenis);
		return (Integer) querySingle("seleckCekRef", params);
	}

	public Integer selectLtId(String reg_spaj, Double top_up) {
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("spaj", reg_spaj);
		params.put("top_up", top_up);
		return (Integer) querySingle("selectLtId", params);
	}
	
	public double selectRatePoint(String lsbs_id) {
		return (Double) querySingle("selectRatePoint", lsbs_id);
	}
	
	public List selectProdukTE() {
		return query("selectProdukTE",null);
	}
	
	public List selectDaftarCabangBsm(){
		return  query("selectDaftarCabangBsm", null);
	}
	public List selectListReportOutstandingBsm(String jenis_produk,String lcb_no, Date process_date){
		Map params = new HashMap();
		params.put("jenis_produk", jenis_produk);
		params.put("lcb_no", lcb_no);
		params.put("process_date", process_date);
		return query("selectListReportOutstandingBsm",params);
	}
	
	public void insertMstEmail(Email email){
		insert("insertMstEmail", email);
	}

	public Integer cekRef(String nama, String tgl_lahir) {
		Map params = new HashMap();
		params.put("nama", nama);
		params.put("tgl_lahir", tgl_lahir);
		return (Integer) querySingle("cekRef", params);
	}

	public List selectReportRef(String bdate, String edate) {
		Map params = new HashMap();
		params.put("bdate", bdate);
		params.put("edate", edate);
		return query("selectReportRef",params);
	}
	
	public String selectMspIdFromMspFireId(String msp_fire_id) throws DataAccessException{
		return (String) querySingle("selectMspIdFromMspFireId", msp_fire_id);
	}
	
	public void updateNoPreMstDrekDet(String no_trx, String noSpaj, String paymentId, String no_pre, String no_jm) throws DataAccessException{
		if(no_trx != null && !"".equals(no_trx)) no_trx = no_trx.replace("\\", "");
		if(no_pre != null && !"".equals(no_pre)) no_pre = no_pre.trim();
		Map map = new HashMap();
		map.put("no_trx", no_trx);
		map.put("noSpaj", noSpaj);
		map.put("paymentId", paymentId);
		map.put("no_pre", no_pre);
		map.put("no_jm", no_jm);
		update("updateNoPreMstDrekDet", map);
	}
	
	public void updateNoPreMstDrekDetFromDepositPremium(String noSpaj, String paymentId, String no_pre, String no_jm, Long no_ke) throws DataAccessException{
		Map map = new HashMap();
		map.put("no_ke", no_ke);
		map.put("no_spaj", noSpaj);
		map.put("paymentId", paymentId);
		map.put("no_pre", no_pre.trim());
		map.put("no_jm",no_jm);
		update("updateNoPreMstDrekDetFromDepositPremium", map);
	}
	
	public Map selectLifeRating_ggtsgptsgot(Map params) throws DataAccessException{
		return (Map) querySingle("selectLifeRating_ggtsgptsgot", params);
	}
	
	public List selectLifeRating(String life_rating){
		return (List) query("selectLifeRating", life_rating);
	}
	
	//FIX===================
	
	public Map selectDataPasForFix(String reg_id){
		return (Map) querySingle("selectDataPasForFix", reg_id);
	}
	
	public Map selectDataPolicyForFix(String reg_spaj){
		return (Map) querySingle("selectDataPolicyForFix", reg_spaj);
	}
	
	//MST_PAS_SMS 
	public void fixBegDatePas(Map param){
		update("fixBegDatePas", param);
	}
	
	//MST_POLICY
	public void fixBegDatePolicy(Map param){
		update("fixBegDatePolicy", param);
	}
	
	//MST_INSURED
	public void fixBegDateInsured(Map param){
		update("fixBegDateInsured", param);
	}
	
	//MST_PRODUCT_INSURED
	public void fixBegDateProductInsured(Map param){
		update("fixBegDateProductInsured", param);
	}
	
	//MST_BILLING
	public void fixBegDateBilling(Map param){
		update("fixBegDateBilling", param);
	}
	
	//=======================
	
	/*public String selectSymbol(String lku_id) throws DataAccessException{
		return (String) querySingle("selectSymbol", lku_id);
	}*/
	
	public List selectKYCtopup_main(String dariTanggal,String sampaiTanggal){
		Map param=new HashMap();
		param.put("dariTanggal", dariTanggal);
		param.put("sampaiTanggal", sampaiTanggal);
		return query("selectKYCtopup_main", param);
	}
	
	public List selectKe(String spaj) throws DataAccessException{
		return query("selectKe", spaj);
		
	}
	
	public Map selectPremiTop (String reg_spaj, Integer kee){
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("kee", kee);
		return (Map) querySingle("selectPremiTop", map);
	}
	
//	public List selectPremiTop (String reg_spaj) throws DataAccessException{
//		return query("selectPremiTop", reg_spaj);
//	}
//	public Map selectMstJobList(Integer ljj_id, Integer lc_id){
//		Map map = new HashMap();
//		map.put("ljj_id", ljj_id);
//		map.put("lc_id", lc_id);
//		return (Map) querySingle("selectMstJobList", map);
//	}
//	public List selectPremiTop (String reg_spaj) throws DataAccessException{
//		return query("selectPremiTop", reg_spaj);
//	}
//	public Map selectMstJobList(Integer ljj_id, Integer lc_id){
//		Map map = new HashMap();
//		map.put("ljj_id", ljj_id);
//		map.put("lc_id", lc_id);
//		return (Map) querySingle("selectMstJobList", map);
//	}
	
	// *VIPcard
	public List selectVipCard(String spaj) throws DataAccessException{
		return query("selectVipCard", spaj);
	}
	public Double selectPaymentMode(String spaj) throws DataAccessException{
		return(Double) querySingle("selectPaymentMode", spaj);
	}
	
	public String selectNoPolisFormat(String spaj) throws DataAccessException {
		return (String) querySingle("selectNoPolisFormat", spaj);
	}
	
//	public String selectFlagcc(String spaj) throws DataAccessException {
//		String cc = (String) querySingle("selectFlagcc", spaj);
//		return cc;
//	}
	
	public void insertVipCard(int jenis, String spaj, String polis, String lus_id, int jumlahPolis, 
			double totalPremi, int flag_print, String notes, int flag_aktif, Date tgl_naktif) throws DataAccessException {
		Map params = new HashMap();
		params.put("jenis", jenis);
		params.put("spaj", spaj);
		params.put("polis", polis);
		params.put("lus_id", lus_id);
		params.put("jumlahPolis", new Integer(jumlahPolis));
		params.put("totalPremi", new Double(totalPremi));
		params.put("flag_print", flag_print);
		params.put("notes", notes);
		params.put("flag_aktif", flag_aktif);
		params.put("tgl_naktif", tgl_naktif);
		insert("insertVipCard", params);
	}
	
	public String selectBancassTeam(String spaj) throws DataAccessException {
		return (String) querySingle("selectBancassTeam", spaj);
	}	
	
	public Date selectBegDate(String spaj) throws DataAccessException {
		return (Date) querySingle("selectBegDate", spaj);
	}
	public String selectNamaBusinessSmile(Integer lsbs, Integer lsdbs) throws DataAccessException {
		Map params = new HashMap();
		params.put("lsbs_id", lsbs);
		params.put("lsdbs_number", lsdbs);
		return (String) querySingle("selectNamaBusinessSmile", params);
	}
	
	public Integer selectCountNotSentMstEmail(){
		return (Integer) querySingle("selectCountNotSentMstEmail", null);
	}
	
	public Integer updateBPSMS(Pas pas){
		Integer cekUpd = 0;
		cekUpd = update("updateBPSMS",pas);
		return cekUpd;
	}
	
	public void updateNextBayar(String spaj, Date begdate){
		Map params=new HashMap();
		params.put("spaj", spaj);
		params.put("tgl", begdate);
		update("updateNextbayar", params);
	}

	/**
	 * Select pengiriman hadiah pada program hadiah hari H
	 * @author Canpri
	 * @since 22 Oct 2012
	 * @param date
	 */
	public List<Hadiah> selectAppointmentProgramHadiah(String bdate) {
		return query("selectAppointmentProgramHadiah", bdate);
	}
	
    public List select_detilprodukutama_viewer(Integer kode, String spaj)
    {
    	Map param = new HashMap();
    	param.put("spaj", spaj);
    	param.put("kode", kode);
    return query("select_detilprodukutama_viewer",param);
    }
	
	public List<Map> selectTransPolToUw() throws DataAccessException {
		return query("selectTransPolToUw",null);
	}
	
	public int selectjumlahAdmedika(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectjumlahAdmedika", reg_spaj);
	}

	public Integer selectSyariah(String spaj) {
		return (Integer) querySingle("selectSyariah", spaj);
	}

	public List selectInfoTabaru(String spaj, Integer tahun, Integer pot_ke) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", tahun);
		params.put("pot_ke", pot_ke);
		return query("selectInfoTabaru", params);
	}
	public int updateMst_Ulink_Bill(String spaj, Integer tahun, Integer pot_ke, Double tabarru, Double ujrah) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tahun", tahun);
		params.put("pot_ke", pot_ke);
		params.put("tabarru", tabarru);
		params.put("ujrah", ujrah);
		return update("update.mst_ulink_bill", params);
	}
	
	public List selectMst_Ulink_Bill(String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		return query("selectMst_Ulink_Bill", params);
	}
	
	public String selectCabangForUserInput(String spaj) throws DataAccessException {
		Map hasil = (HashMap) querySingle("selectCabangForUserInput", spaj);
		if(hasil != null) return (String) hasil.get("LCA_ID");
		else return null;
	}
	
	public List selectUserCSsms(String user) {
		String userCS = "in("+user+")";
		return query("selectUserCSsms", userCS);
	}
	
	public List selectReportSMSHarian(String user_id,String bdate, String edate){
	    Map param = new HashMap();
	    param.put("lus_id", user_id);
	    param.put("bdate", bdate);
	    param.put("edate", edate);
	    return query("selectReportSMSHarian",param);
	}
	
	public List selectReportSMSBulanan(String user_id,String bdate, String edate){
		Map param = new HashMap();
	    param.put("lus_id", user_id);
		param.put("bdate", bdate);
		param.put("edate", edate);
		return query("selectReportSMSBulanan",param);
	}
	 
	public List selectValidasiInputPasBp(String msag_id_pp, String msp_pas_nama_pp, String msp_full_name, Date msp_pas_beg_date){
		Map params = new HashMap();
		params.put("msag_id_pp", msag_id_pp);
		params.put("msp_pas_nama_pp", msp_pas_nama_pp);
		params.put("msp_full_name", msp_full_name);
		params.put("msp_pas_beg_date", msp_pas_beg_date);
		
		return query("selectValidasiInputPasBp", params);
	}
	
	public List selectValidasiInputDbd(String msp_pas_nama_pp, String msp_full_name, Date msp_pas_beg_date){
		Map params = new HashMap();
		params.put("msp_pas_nama_pp", msp_pas_nama_pp);
		params.put("msp_full_name", msp_full_name);
		params.put("msp_pas_beg_date", msp_pas_beg_date);
		
		return query("selectValidasiInputDbd", params);
	}
	
	public String selectNamaCabang(String spaj) throws DataAccessException {
		return (String) querySingle("selectNamaCabang", spaj);
	}	
	
	public List<CoverLetter> selectPolisCoverLetter(String bdate, String edate, Integer stpolis, Integer larid,
			String lusid, Integer jalur_dist) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("stpolis", stpolis);
		m.put("larid", larid);
		m.put("lusid", lusid);
		m.put("jalur_dist", jalur_dist);
		return query("selectPolisCoverLetter", m);
	}
	
	public List selectReportCoverLetter(String tanggalAwal, String tanggalAkhir, Integer lar_id, Integer jalur_dist)
			throws DataAccessException{
		Map m = new HashMap();
		m.put("tanggalAwal", tanggalAwal);
		m.put("tanggalAkhir", tanggalAkhir);
		m.put("lar_id", lar_id);
		m.put("jalur_dist", jalur_dist);
		return query("selectReportCoverLetter", m);
	}
	
	public List<Map> selectCabCoverLetter(String spaj) throws DataAccessException{
		return query("selectCabCoverLetter", spaj);
	}
	
	public HashMap selectAddrRegion(String lar_id) throws DataAccessException{
		return (HashMap) querySingle("selectAddrRegion", lar_id);
	}
	
	public void updateCoverLetter(String nopol, String stpolis){
		Map m=new HashMap();
		m.put("nopol", nopol);
		m.put("stpolis", stpolis);
		update("updateCoverLetter", m);
	}
	
	public String selectTglCoverLetter(String nopol, String stpolis) {
		Map m = new HashMap();
		m.put("nopol", nopol);
		m.put("stpolis", stpolis);
		return (String) querySingle("selectTglCoverLetter", m);
	}
	
	public List selectDetailDTH(String spaj) throws DataAccessException {
		return query("selectDetailDTH", spaj);
	}
	
	public Integer selectFlagDTH(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectFlagDTH", spaj);
	}
	
	public List selectListJabodetabekBSM() throws DataAccessException {
		return query("selectListJabodetabekBSM", null);
	}

	public List select_hadiah_ps(Double mspr_premium) {
		return query("select_hadiah_ps", mspr_premium);
	}
	
	public List select_hadiah_ps_spesial(Double mspr_premium) {
		return query("select_hadiah_ps_spesial", mspr_premium);
	}
	
	public List selectReportSuratReins(String spaj, String noreins) {
		Map m=new HashMap();
		m.put("nospaj", spaj);
		m.put("reinsno", noreins);
		return query("selectReportSuratReins", m);
	}
	
	public List selectReportRekapSP(Date awal, Date akhir) {
		Map m=new HashMap();
		m.put("tglAwal", awal);
		m.put("tglAkhir", akhir);
		return query("selectReportProsesAksepPendingSP", m);
	}
	
	public List selectReportRekapSL(Date awal, Date akhir) {
		Map m=new HashMap();
		m.put("tglAwal", awal);
		m.put("tglAkhir", akhir);
		return query("selectReportProsesAksepPendingSL", m);
	}
	
	public List selectKYCpencairan_main(String dariTanggal,String sampaiTanggal){
		Map param=new HashMap();
		param.put("dariTanggal", dariTanggal);
		param.put("sampaiTanggal", sampaiTanggal);
		return query("selectKYCpencairan_main", param);
	}
	
	public String selectIDinbox(String spaj) throws DataAccessException {
		return (String) querySingle("selectIDinbox", spaj);
	}
	
	public List<Map> selectFollowUpPolis() throws DataAccessException {
		return query("selectFollowUpPolis",null);
	}
	
	public List<Map> selectSpajFurther() throws DataAccessException {
		return query("selectSpajFurther", null);
	}
	public List<Map> selectFurther(String spajFurther) throws DataAccessException {
		return query("selectFurther",spajFurther);
	}
	
	public List<Map> selectNpwIndividu(Integer status) throws DataAccessException {
		Map y = new HashMap();
		y.put("status", status);
		return query("selectNpwIndividu",y);
	}

	public List selectPolisKlaimKesehatan(String tipe, String operator, String nama, String tglLahir) {
		if("LIKE%".equals(operator))
			nama = " like upper(trim('"+nama+"')) || '%' ";
		else if("%LIKE".equals(operator))
			nama = " like '%' ||upper(trim('"+nama+"')) ";
		else if("%LIKE%".equals(operator))
			nama = " like '%' ||upper(trim('"+nama+"')) || '%' ";
		else if("LT".equals(operator))
			nama = " < rpad(replace(upper(trim('"+nama+"')),'.'),11,' ') ";
		else if("LE".equals(operator))
			nama = " <= rpad(replace(upper(trim('"+nama+"')),'.'),11,' ') ";
		else if("EQ".equals(operator))
			nama = " = upper(trim('"+nama+"')) ";
		else if("GE".equals(operator))
			nama = " >= rpad(replace(upper(trim('"+nama+"')),'.'),11,' ') ";
		else if("GT".equals(operator))
			nama = " > rpad(replace(upper(trim('"+nama+"')),'.'),11,' ') ";
		else if("=".equals(operator))
			nama = " = upper(trim('"+nama+"')) ";
		
		Map params = new HashMap();
		params.put("tipe", tipe);
		params.put("nama", nama);
		params.put("tglLahir", tglLahir);
		
		return query("selectPolisKlaimKesehatan", params);
	}

	public List selectAttentionList(String tipe, String operator, String nama, String tglLahir) {
		if("LIKE%".equals(operator))
			nama = " like upper(trim('"+nama+"')) || '%' ";
		else if("%LIKE".equals(operator))
			nama = " like '%' ||upper(trim('"+nama+"')) ";
		else if("%LIKE%".equals(operator))
			nama = " like '%' ||upper(trim('"+nama+"')) || '%' ";
		else if("LT".equals(operator))
			nama = " < rpad(replace(upper(trim('"+nama+"')),'.'),11,' ') ";
		else if("LE".equals(operator))
			nama = " <= rpad(replace(upper(trim('"+nama+"')),'.'),11,' ') ";
		else if("EQ".equals(operator))
			nama = " = upper(trim('"+nama+"')) ";
		else if("GE".equals(operator))
			nama = " >= rpad(replace(upper(trim('"+nama+"')),'.'),11,' ') ";
		else if("GT".equals(operator))
			nama = " > rpad(replace(upper(trim('"+nama+"')),'.'),11,' ') ";
		else if("=".equals(operator))
			nama = " = upper(trim('"+nama+"')) ";
		
		Map params = new HashMap();
		params.put("tipe", tipe);
		params.put("nama", nama);
		params.put("tglLahir", tglLahir);
		
		return query("selectAttentionList", params);
	}
	
	public String selectMstInboxNoReff (String blanko){
		return (String) querySingle("selectMstInboxNoReff", blanko);
	}
	
	public String selectMstInboxMiUrl (String mi_id){
		return (String) querySingle("selectMstInboxMiUrl", mi_id);
	}

	public List selectDaftarSPAJ_aksepEndors(String posisi, int tipe, Integer lssaId, Integer jenisTerbit) throws DataAccessException {
		Map params = new HashMap();
		if(lssaId!=null){
			params.put("posisi", "in("+posisi+")");
		}else{
			params.put("posisi", posisi);
		}
		params.put("tipe", new Integer(tipe));
		params.put("lssaId", lssaId);
		params.put("jenisTerbit",jenisTerbit);
		return query("selectDaftarSPAJ_aksepEndors", params);
	}

	public void updateAksepEndors(Integer aksep_uw, String spaj, String lus_id){
		Map map=new HashMap();
		map.put("aksep_uw",aksep_uw);
		map.put("spaj",spaj);
		map.put("lus_id", lus_id);
		update("updateAksepEndors",map);
	}
	
	public Integer selectMsen_aksep_uw(String spaj) throws DataAccessException {
		Integer result = (Integer) querySingle("selectMsen_aksep_uw", spaj);
		if(result == null) return -1; 
		else return result;	
	}
	
	public Map selectMsen_aksep_uw_new(String spaj) throws DataAccessException{
		return (HashMap) querySingle("selectMsen_aksep_uw_new", spaj);
	}
	
	public String selectKete(String spaj) throws DataAccessException {
		return (String) querySingle("selectKete", spaj);
	}
	
	public Endors selectEndorsNew(String spaj) throws DataAccessException {
		return (Endors) querySingle("selectEndorsNew", spaj);
	}
	
	public List selectFilterSpajEndorsment(String posisi, String tipe, String kata, String pilter,String lssaId,String lsspId) throws DataAccessException {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		if(lssaId==null){
			params.put("posisi", posisi);
		}else{
			params.put("posisi", "in ("+posisi+")");
		}
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("lssaId",lssaId);
		params.put("lssp_id",lsspId);
		return query("selectFilterSpajEndorsment", params);
	}

	public List selectreportDetailHealthClaim(String bdate, String edate, String lus_id, String jenis_periode) throws DataAccessException {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("jenis_periode", jenis_periode);
		return query("selectreportDetailHealthClaim",map);
	}

	public List selectreportHealthClaimBasedEntryAge(String bdate, String edate, String lus_id, String jenis_periode) throws DataAccessException {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("jenis_periode", jenis_periode);
		return query("selectreportHealthClaimBasedEntryAge",map);
	}

	public List selectreportHealthClaimByMedis(String bdate, String edate, String lus_id, String jenis_periode) throws DataAccessException {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("jenis_periode", jenis_periode);
		return query("selectreportHealthClaimByMedis",map);
	}

	public List selectGetTotalHealthClaimByMedis(String bdate, String edate, String lus_id, String medis, String jenis_periode) throws DataAccessException {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("medis", medis);
		map.put("jenis_periode", jenis_periode);
		return query("selectGetTotalHealthClaimByMedis",map);
	}

	public List selectreportHealthClaimBasedDurationPolicy(String bdate, String edate, String lus_id, String jenis_periode) throws DataAccessException {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("jenis_periode", jenis_periode);
		return query("selectreportHealthClaimBasedDurationPolicy",map);
	}

	public List selectreportHealthClaimByBranch(String bdate, String edate, String lus_id, String jenis_periode) {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("jenis_periode", jenis_periode);
		return query("selectreportHealthClaimByBranch",map);
	}

	public List selectreportHealthClaimByCOD(String bdate, String edate,	String lus_id, String jenis_periode) {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("jenis_periode", jenis_periode);
		return query("selectreportHealthClaimByCOD",map);
	}

	public Integer selectreportHealthClaimByCOD_TotalCase(String bdate, String edate, String lus_id, String jenis_periode) {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("jenis_periode", jenis_periode);
		return (Integer) querySingle("selectreportHealthClaimByCOD_TotalCase", map);
	}

	public List selectreportHealthClaimBasedAge(String bdate, String edate, String lus_id, String jenis_periode) {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("jenis_periode", jenis_periode);
		return query("selectreportHealthClaimBasedAge",map);
	}

	public List selectreportExGratiaHealthClaim(String bdate, String edate, String lus_id, String jenis_periode) {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("jenis_periode", jenis_periode);
		return query("selectreportExGratiaHealthClaim",map);
	}

	public List selectreportClaimBySAKesehatan(String bdate, String edate, String lus_id, String jenis_periode) {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("jenis_periode", jenis_periode);
		return query("selectreportClaimBySAKesehatan",map);
	}

	public List selectreportHealthClaimByProduct(String bdate, String edate,	String lus_id, String jenis_periode) {
		Map map=new HashMap();
		map.put("bdate",bdate);
		map.put("edate",edate);
		map.put("lus_id", lus_id);
		map.put("jenis_periode", jenis_periode);
		return query("selectreportHealthClaimByProduct",map);
	}
	
	public List selectReportSummaryInputDanaSejahtera(String bdate, String edate){
	    Map param = new HashMap();	 
	    param.put("bdate", bdate);
	    param.put("edate", edate);
	    return query("selectReportSummaryInputDanaSejahtera",param);
	}
	
	public List selectReportFurtherDanaSejahteraBSM(String bdate, String edate){
	    Map param = new HashMap();	 
	    param.put("bdate", bdate);
	    param.put("edate", edate);
	    return query("selectReportFurtherDanaSejahteraBSM",param);
	}
	
	public List selectReportSummaryInfoDanaSejahteraBSM(String bdate, String edate){
	    Map param = new HashMap();	 
	    param.put("bdate", bdate);
	    param.put("edate", edate);
	    return query("selectReportSummaryInfoDanaSejahteraBSM",param);
	}
	
	public void insertMstCancel(String kopi_spaj, String reg_spaj, String lus_id){
		Map param = new HashMap();
		param.put("kopi_spaj", kopi_spaj);
		param.put("reg_spaj", reg_spaj);
		param.put("lus_id", lus_id);
		insert("insertMstCancel", param);
	}
	
	public Integer selectCekKonfirmasiSyariah(String reg_spaj) throws DataAccessException{
			return (Integer) querySingle("selectCekKonfirmasiSyariah", reg_spaj);
	}
	
	public Integer selectPositionPolicy(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectPositionPolicy", reg_spaj);
	}
	
	public Integer selectSertifikatYN(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectSertifikatYN", reg_spaj);
	}
	

	public List<OfacSertifikat> selectSertifikat(String spaj){
		
		Map params=new HashMap();
		params.put("spaj", spaj);
		
		return query("selectSertifikat",params);
	}
	
	public List<MstTransUlink> selectCheckMstTransUlink(String spaj){
		
		Map params=new HashMap();
		params.put("spaj", spaj);
		
		return query("selectCheckMstTransUlink",params);
	}
	
	public Integer selectPositionInsured(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectPositionInsured", reg_spaj);
	}
	
	public List<Map> selectDetailInvestasi(String reg_spaj) throws DataAccessException{
		return query("selectDetailInvestasi", reg_spaj);
	}
	
	public Integer selectTotBiayaMuKe(String reg_spaj, Integer mu_ke) throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("mu_ke",mu_ke);
		return (Integer) querySingle("selectTotBiayaMuKe", map);
	}
	
	public void updateMduJumlah(String reg_spaj, String lji_id, String mu_ke, Double mdu_jumlah){
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("lji_id",lji_id);
		map.put("mu_ke",mu_ke);
		map.put("mdu_jumlah", mdu_jumlah);
		update("updateMduJumlah",map);
	}
	
	public void updatePrmExt(String reg_spaj, Integer lsbs_id, Integer lsdbs_number){
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("lsbs_id",lsbs_id);
		map.put("lsdbs_number", lsdbs_number);
		update("updatePrmExt",map);
	}
	
	public Integer updateKekuranganPrm(String reg_spaj, Integer prmbaru){
		Map map = new HashMap();
		map.put("reg_spaj",reg_spaj);
		map.put("prmbaru",prmbaru);
		return update("updateKekuranganPrm",map);
	}
	
	public Integer selectLusSpecial(String lusid) throws DataAccessException {
		return (Integer) querySingle("selectLusSpecial", lusid);
	}	
	
	public List selectCabBII() throws DataAccessException {
		return query("selectCabBII", null);
	}
	
	public List selectPolisCoverLetterBsm(String bdate, String edate, String stpolis, String lcb_no) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("stpolis", stpolis);
		m.put("lcb_no", lcb_no);
		return query("selectPolisCoverLetterBsm", m);
	}
	
	public List selectDataCoverBsmAll(String tanggalAwal, String tanggalAkhir) throws DataAccessException{
		Map m = new HashMap();
		m.put("tanggalAwal", tanggalAwal);
		m.put("tanggalAkhir", tanggalAkhir);
		return query("selectDataCoverBsmAll", m);
	}
	
	public List selectDataCoverBsmCab(String tanggalAwal, String tanggalAkhir, String lcb_no) throws DataAccessException{
		Map m = new HashMap();
		m.put("tanggalAwal", tanggalAwal);
		m.put("tanggalAkhir", tanggalAkhir);
		m.put("lcb_no", lcb_no);
		return query("selectDataCoverBsmCab", m);
	}
	
	public void insertMstBlankoPolis(String reg_spaj, String no_blanko, String lus_id, String jenis, String keterangan, int addSecond) throws DataAccessException{
		Map p = new HashMap();
		p.put("reg_spaj", reg_spaj);
		p.put("no_blanko", no_blanko);
		p.put("lus_id", lus_id);
		p.put("jenis", jenis);
		p.put("keterangan", keterangan);
		p.put("addSecond", addSecond);
		insert("insertMstBlankoPolis", p);	
	}
	
	public Integer selectNoBlankoPrintBSM(String blanko) throws DataAccessException{
		return(Integer) querySingle("selectNoBlankoPrintBSM", blanko);
	}	
	
	public List selectreportProduksiCair(Date bdate, Date edate, Integer jn_report){
		Map map = new HashMap();
		map.put("bdate", bdate);
		map.put("edate", edate);
		if(jn_report==0){
			return query("selectreportProduksiCair",map);
		}else{
			return query("selectreportProduksiCairMGI",map);
		}
	}
	
	public Map selectInformasiSpajExpired(String spaj) {
		return (Map) querySingle("selectInformasiSpajExpired", spaj);
	}

	public void update_billing(Cmdeditbac edit, int flag_proses, Integer countBilling) {
		Integer liPmode,liPperiod,liMonth;
		int liPremiKe, liThKe, liPaid = 0;
		Integer liId = new Integer(0); //3 ~ biaya polis = Rp. 20.000,-
		String lsCab, lsKursId, lsPayId, lsWakil, lsRegion, lsKursPolis;
		Double ldecPremi, ldecBpolis, ldecDp;
		Date ldtBegDate, ldtEndDate, ldtDueDate;
		boolean lbRet = true, lbTp = false;
		Double ldecKurs = new Double(1);
		Date ldt_tgl_book;
		
		liPmode=edit.getDatausulan().getLscb_id();
		ldtBegDate=edit.getDatausulan().getMste_beg_date();
		ldtEndDate=edit.getDatausulan().getMste_end_date();
		
		if(liPmode!=0){
			liMonth=selectLstPayModeLscbTtlMonth(liPmode);
			if(logger.isDebugEnabled())logger.debug("ldt enddate ="+defaultDateFormat.format(ldtEndDate));
			Date dTemp=selectAddMonths(defaultDateFormat.format(ldtBegDate),liMonth);
			if(logger.isDebugEnabled())logger.debug("dtemp ="+defaultDateFormat.format(dTemp));
			ldtEndDate=FormatDate.add(dTemp,Calendar.DATE,-1);
			if(logger.isDebugEnabled())logger.debug("ldt enddate ="+defaultDateFormat.format(ldtEndDate));
			//Himmia 30/01/2001
			if(sdfDd.format(ldtEndDate).equals(sdfDd.format(ldtBegDate))) {
				ldtEndDate=FormatDate.add(ldtEndDate,Calendar.DATE,-1);
			}
		}
		liThKe = 1;
		liPremiKe = 1;
		Calendar calTemp=new GregorianCalendar(2006,06-1,1);
		
		//yusuf 02062006 due date lebih besar dari 1 juni 2006 ditambah 1 minggu
		// direvisi jadi ditambah 30 hari
		if(ldtBegDate.compareTo(calTemp.getTime()) >0){
			ldtDueDate=FormatDate.add(ldtBegDate,Calendar.DATE,7);
			//ldtDueDate=FormatDate.add(ldtBegDate,Calendar.DATE,30);
		}else{
			ldtDueDate = selectAddMonths(defaultDateFormat.format(ldtBegDate),new Integer(1));
		}
		if(ldtDueDate==null){
			logger.info("gagal get ldtDueDate");
		}

		Map map = new HashMap();
		map.put("reg_spaj",edit.getDatausulan().getReg_spaj());
		map.put("bdate",ldtBegDate);
		map.put("edate",ldtEndDate);
		map.put("ddate",ldtDueDate);
		map.put("flag_proses",flag_proses);
		update("updateMstBillingNB",map);
		
		if(liPmode!=0){
			if(logger.isDebugEnabled())logger.debug("ldtEnddate= "+defaultDateFormat.format(ldtEndDate));
			Date nextBill=FormatDate.add(ldtEndDate,Calendar.DATE,1);
			if(logger.isDebugEnabled())logger.debug("ldtnext bill= "+defaultDateFormat.format(nextBill));
			updateMstPolicyMspoNextBill(edit.getDatausulan().getReg_spaj(),new Integer(1),nextBill);
		}	
		
	}
	
	public Map selectEmailHybrid(String msag_id){
		return (HashMap) querySingle("selectEmailHybrid",msag_id);
	}

	public List selectCatatanPolis(String pemegang, String bdate) {
		Map map = new HashMap();
		map.put("pemegang", pemegang);
		map.put("bdate", bdate);
		return query("selectCatatanPolis",map);
	}
	
	public void updatePosisiSkDebetKredit(String reg_spaj, Integer posisi, String lus_id, String keterangan) {
		Map params = new HashMap();
		String msps_desc = "";
		if(posisi.equals(0)) {
			msps_desc = "U/W";
		} else {
			msps_desc = "Finance";
		}
		
		if(keterangan != null && !keterangan.trim().equals("")) {
			msps_desc += " - Ket: " + keterangan;
		}
		
		params.put("reg_spaj", reg_spaj);
		params.put("lus_id", lus_id);
		params.put("msps_desc", "SK Debet/Kredit sudah diterima di bagian " + msps_desc);
		
		Boolean a = true;
		while(a){
			try{
				insert("insertPosisiSkDebetKredit", params);
				a=false;
			}catch (Exception e) {
				a=true;
			}
		}
	}
	
	
//	public List selectLusIdAkseptasiDoc() throws DataAccessException {
//		return query("selectLusIdAkseptasiDoc",null);
//	}
//	
//	public List<Map> selectTanggalFillingScan(String lus_id) throws DataAccessException {
//		return query("selectTanggalFillingScan",lus_id);
//	}
	
	public Map selectEmailAoHrd(String spaj){
		return (HashMap) querySingle("selectEmailAoHrd",spaj);
	}
	
	public Map selectReffNonLisensi(String spaj){
		return(HashMap) querySingle("selectReffNonLisensi", spaj);
	}

	public Map selectRegion(String spaj) {		
		return (HashMap) querySingle("selectRegionSpaj",spaj);
	}	
	
	//MANTA - BegDate Editor
	public Integer selectNewBussiness(String reg_spaj) throws DataAccessException {
		return (Integer) querySingle("selectNewBussiness", reg_spaj);
	}
	
	public Policy selectMstPolicyAll(String spaj) {		
		return (Policy) querySingle("selectMstPolicyAll", spaj);
	}
	
	public Insured selectMstInsuredAll(String spaj) {		
		return (Insured) querySingle("selectMstInsuredAll", spaj);
	}
	
	public List<Product> selectMstProdInsAll(String spaj) {		
		return query("selectMstProdInsAll", spaj);
	}
	
	public List<Production> selectMstProductionAll(String spaj) {		
		return query("selectMstProductionAll", spaj);
	}
	
	public List<Billing> selectMstBillingAll(String spaj) {		
		return query("selectMstBillingAll", spaj);
	}
	
	public HashMap selectMstPasSMSAll(String spaj) {		
		return (HashMap) querySingle("selectMstPasSMSAll", spaj);
	}
	
	public List<Ulink> selectMstUlinkAll(String spaj) {		
		return query("selectMstUlinkAll", spaj);
	}
	
	public List<TransUlink> selectMstTransUlinkAll(String spaj) {		
		return query("selectMstTransUlinkAll", spaj);
	}
	
	public List<DetUlink> selectMstDetUlinkAll(String spaj) {		
		return query("selectMstDetUlinkAll", spaj);
	}
	
	public List<BiayaUlink> selectMstBiayaUlinkAll(String spaj) {		
		return query("selectMstBiayaUlinkAll", spaj);
	}
	
	public List<UlinkBill> selectMstUlinkBillAll(String spaj) {		
		return query("selectMstUlinkBillAll", spaj);
	}
	
	public Map selectNoKartuPas(String spaj) {		
		return (HashMap) querySingle("selectNoKartuPas",spaj);
	}
	
	public void updateMstPolicyBegDate(Policy policy){
		update("updateMstPolicyBegDate", policy);
	}
	
	public void updateMstInsuredBegDate(Insured insured){
		update("updateMstInsuredBegDate", insured);
	}
	
	public void updateMstProdInsBegDate(Product prodins){
		update("updateMstProdInsBegDate", prodins);
	}
	
	public void updateMstProductionBegDate(Production prod){
		update("updateMstProductionBegDate", prod);
	}
	
	public void updateMstBillingBegDate(Billing bill){
		update("updateMstBillingBegDate", bill);
	}
	
	public void updateMstPasSMSBegDate(String reg_spaj, Date msp_pas_beg_date, Date msp_pas_end_date){
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("msp_pas_beg_date", msp_pas_beg_date);
		map.put("msp_pas_end_date", msp_pas_end_date);
		update("updateMstPasSMSBegDate", map);
	}
	
	public void updateMstPesertaBegDate(String reg_spaj, Date next_send){
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("next_send", next_send);
		update("updateMstPesertaBegDate", map);
	}
	
	public void updateMstUlinkBegDate(Ulink ulink){
		update("updateMstUlinkBegDate", ulink);
	}
	
	public void updateMstDetUlinkBegDate(DetUlink detulink){
		update("updateMstDetUlinkBegDate", detulink);
	}
	
	public void updateMstBiayaUlinkBegDate(BiayaUlink biayaulink){
		update("updateMstBiayaUlinkBegDate", biayaulink);
	}
	
	public void updateMstUlinkBillBegDate(UlinkBill ulinkbill){
		update("updateMstUlinkBillBegDate", ulinkbill);
	}
	
	//lufi
	public List<DropDown> selectDropDownDaftarPeserta(String spaj) {		
		return query("selectDropDownDaftarPeserta",spaj);
	}

	public List selectJawabanMedical(String spaj, int flag_jenis_peserta) {
		Map map = new HashMap();
		map.put("spa", spaj);
		map.put("fjp", flag_jenis_peserta);
		return query("selectJawabanMedicals",map);
	}	
	
	public List selectJawabanMedicalsSIOtambahan(String spaj, int flag_jenis_peserta) {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("fjp", flag_jenis_peserta);
		return query("selectJawabanMedicalsSIOtambahan",map);
	}
	
	public List selectJawabanMedicalALL(String spaj, int flag_jenis_peserta) {
		Map map = new HashMap();
		map.put("spa", spaj);
		map.put("fjp", flag_jenis_peserta);
		return query("selectJawabanMedicalALL",map);
	}	
	
	public List selectSuratUnitLinkAdditionalUnit(String spaj, Date tanggal) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("tanggal", tanggal);
		return query("report.selectSuratUnitLink.AdditionalUnit", params);
	}
	
	public String prosesPermintaanVA(VirtualAccount va, User currentUser) throws DataAccessException, ParseException, MailException, MessagingException {
		
		SimpleDateFormat yy = new SimpleDateFormat("yy");
		SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
		SimpleDateFormat mm = new SimpleDateFormat("MM");
		SimpleDateFormat dt = new SimpleDateFormat("yyyyMM");
		StringBuffer pesan = new StringBuffer();
		
		va.setUser_create(Integer.parseInt(currentUser.getLus_id()));
		
		//Get msv_id (Kode Permintaan VA)
		Map id = (Map)querySingle ("selectLastIdPermintaanVA", dt.format(commonDao.selectSysdate()));
		Integer cnt = 0;
		
		if(id==null){
			cnt = 1;
		}else{
			cnt = ((BigDecimal)id.get("COUNTER")).intValue() + 1;
		}
		
		String msv_id = "VA"+dt.format(commonDao.selectSysdate())+org.apache.commons.lang.StringUtils.leftPad(cnt.toString(), 4, "0");
		//end 
		
		//Insert to eka.mst_form_va
		va.setMsv_id(msv_id);
		insert("insertMstVA", va);
		//end
		
		//generate no va kalau insert ke mst_form_va berhasil
		String kode = "";
		String prefix = "";//konvensional=8006, syariah=8076
		
		String tahun = yy.format(va.getTgl());
		String bulan = mm.format(va.getTgl());
		
		
		//jenis va
		/* 0 = paper konvensional
		 * 1 = paper syariah
		 * 2 = gadget konvensional
		 * 3 = gadget syariah
		 * 4 = lain-lain konven
		 * 5 = lainnya
		 * 
		 * va.getJenis_va awalnya cuma 0-3 ini harus di konversi sesuai ketentuan diatas jd harus di set ulang
		 * */
		
		//tentukan kode awal
		if(va.getJenis_syariah()==1){//syariah
			if(va.getJenis_va()==2){
				prefix = "8093";
				va.setJenis_va(4);
			}else if(va.getJenis_va()==3){
				prefix = "";
				va.setJenis_va(5);
			}else{
				if(va.getJenis_spaj().equals("20")){
					prefix = "7721";
				}else{
					//prefix = "8076";
					prefix = "87111"; //generate format baru untuk nomor virtual account
				}
				if(va.getJenis_va()==0){
					va.setJenis_va(1);
				}else{
					va.setJenis_va(3);
				}
				if(va.getJenis_spaj().equals("22")){
					prefix = "71404223"; 
				}else if(va.getJenis_spaj().equals("23")){
					prefix = "71404224"; 
				}
				
				if(va.getJenis_spaj().equals("01"))va.setJenis_spaj("09");
				else if(va.getJenis_spaj().equals("02"))va.setJenis_spaj("10");
				else if(va.getJenis_spaj().equals("03"))va.setJenis_spaj("11");
				else if(va.getJenis_spaj().equals("05"))va.setJenis_spaj("13");
				else if(va.getJenis_spaj().equals("07"))va.setJenis_spaj("14");
				else if(va.getJenis_spaj().equals("20"))va.setJenis_spaj("14");
				else if(va.getJenis_spaj().equals("21"))va.setJenis_spaj("16");
			}
		}else{
			 if(va.getJenis_va()==2){
				prefix = "8093";
				va.setJenis_va(4);
			}else if(va.getJenis_va()==3){
				prefix = "";
				va.setJenis_va(5);
			}else{
				if(va.getJenis_spaj().equals("20")){
					prefix = "7720";
				}else if(va.getJenis_va()==1 && va.getJenis_spaj().equals("17")){
					prefix = "8387";
				}else{prefix = "8006";}
				if(va.getJenis_va()==0){
					va.setJenis_va(0);
				}else{
					va.setJenis_va(2);
				}
				
				if(va.getJenis_spaj().equals("21"))va.setJenis_spaj("16");
				else if(va.getJenis_spaj().equals("20"))va.setJenis_spaj("14");
			}
		}
		
		kode = prefix+va.getJenis_link()+va.getJenis_va()+tahun+va.getJenis_spaj();
		
		if(va.getJenis_spaj().equals("22") || va.getJenis_spaj().equals("23")){ //BTN Syariah
			kode = prefix + yyyy.format(va.getTgl());
		}
			
		Integer awal = 0;
		Integer akhir = 0;
		
		if(va.getJenis_va()==1){
			awal = Integer.parseInt(va.getStart_no_va_req());
			akhir = Integer.parseInt(va.getEnd_no_va_req());
		}else{
			awal = Integer.parseInt(va.getStart_no_va_cetak());
			akhir = Integer.parseInt(va.getEnd_no_va_cetak());
		}
		
		Integer jml = akhir-awal;
		
		//insert no virtual account
		for(int j=0;j<=jml;j++){
			String no = kode+org.apache.commons.lang.StringUtils.leftPad(awal.toString(), 6, "0");
			if(prefix.trim().equalsIgnoreCase("87111") || prefix.trim().equalsIgnoreCase("87222")){
				String run_number = (awal.toString().length() > 5 ? awal.toString().substring(awal.toString().length() - 5) : awal.toString());
				no = kode+org.apache.commons.lang.StringUtils.leftPad(run_number, 5, "0"); //generate format baru untuk nomor virtual account
			}
//			String no = kode+org.apache.commons.lang.StringUtils.leftPad(awal.toString(), 4, "0");
			
			if(va.getJenis_spaj().equals("22") || va.getJenis_spaj().equals("23")){//BTN Syariah
				no = kode+org.apache.commons.lang.StringUtils.leftPad(awal.toString(), 7, "0");
			}
			
			Integer cek = (Integer)querySingle("selectCekVA",no);
			if(cek>0){
				pesan.append("<br>- Nomor virtual account "+no+" sudah ada.");
			}else{
				va.setNo_va(no);
				va.setFlag_active(0);
				insert("insertMstDetVA", va);
			}
			awal++;
		}
		//end
		//Balikan Posisi Jenis SPAJ nya Khusus Harda dan Magnaprime
		if(va.getJenis_spaj().equals("16"))va.setJenis_spaj("21");
		else if(va.getJenis_spaj().equals("14"))va.setJenis_spaj("20");
		
		List l_va = selectPermintaanVA(msv_id);
		Map mva = (Map) l_va.get(0);
		String no_va = (String)mva.get("MIN_MAX");		
		HashMap mapEmail = uwDao.selectMstConfig(6, "emailPengaktifanVa", "EMAIL_PENGAKTIFAN_VA");
		String[] to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
		String[] cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
		String[] bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
		EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
				"ajsjava@sinarmasmsiglife.co.id",
				to, 
				cc, 
				bcc, 
				"Pembuatan Virtual Account", "Telah proses virtual account pada tanggal "+defaultDateFormat.format(commonDao.selectSysdate())+" dengan no "+no_va+".\nHarap untuk ditindaklanjuti.\n\n", null, null);
		
		return "Berhasil!"+pesan.toString();
	}

	public List selectPermintaanVA(String msv_id) {
		return query("selectPermintaanVA",msv_id);
	}
	
	public List selectJenisBank(String j_bank) {
		return query("selectJenisBank",j_bank);
	}
	
	public void insertBiayaFee(Date tgl_proses, String no_polis,
			String lsdbs_name, Integer jml, String status, String noKLoter,Double tax) throws DataAccessException {
		Map params = new HashMap();
		params.put("tgl_proses", tgl_proses);
		params.put("no_polis", no_polis);
		params.put("lsdbs_name", lsdbs_name);
		params.put("jml", jml);
		params.put("status", status);
		params.put("noKLoter", noKLoter);
		params.put("tax", tax);
		insert("insert.mst_upload_admedika", params);
	}
	
	public List selectDataUploadAdmedika(String bdate, String edate, Integer cmp_id,
			String company1, String stpolis) {
		Map map = new HashMap();
		map.put("bdate", bdate);
		map.put("edate", edate);
		map.put("dist", cmp_id);
		map.put("company", company1);
		map.put("stpolis", stpolis);
		return query("selectDataUploadAdmedika", map);
	}
	
	public Integer procKomNewAgency(String reg_spaj, Integer tahun_ke, Integer premi_ke) throws DataAccessException, SQLException{
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("result", null);
		map.put("tahun_ke", tahun_ke);
		map.put("premi_ke", premi_ke);
		Integer hasil = 1;
		List daftarKomisi = this.financeDao.selectKomisiAgen(reg_spaj,tahun_ke, premi_ke);
		if(daftarKomisi.size()==0){
			 querySingle("procKomNewAgency1", map);
		}else{
			hasil=0;
		}
//		Object test = getSqlMapClient().queryForObject("elions.uw.procKomNewAgency1", map);
//		Object o = map.get("result");
//		if(o instanceof ResultSet)
//		{
//		    ResultSet res = (ResultSet) o;
//		    while(res.next())
//		        logger.info(res.getInt("ROLE_NR"));
//		    res.close();
//		}
//		Integer hasil =(Integer) querySingle("procKomNewAgency", map);//1 berhasil, 0 ada error
//		Integer hasil2 = (Integer) querySingle("procKomNewAgency", map);
//		if(hasil==0)TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//		return "";
		return hasil;
	}
	
	public Integer selectCountCleanCaseInsured(String no_reg) {
		return (Integer) querySingle("selectCountCleanCaseInsured", no_reg);
	}
	
	//select untuk cek polis2 yg extra premi dan postphone/decline untuk warning di proses simultan autoaccept uw
	public List selectExtraPremiSimultan(Map paramEp) throws DataAccessException {
		return query("selectExtraPremiSimultan",paramEp);
	}
	
	public List selectPostponeDeclineSimultan(Map paramEp) throws DataAccessException {
		return query("selectPostponeDeclineSimultan",paramEp);
	}
	
	public void updateEmailFlagBooster( String spaj, Integer flag) throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("flag", flag);
		update("updateEmailFlagBooster", map);
	}
	

	public Map selectPemegangExclude(String reg_spaj, String dp) {
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("dp", dp);
		return (HashMap) querySingle("selectPemegangExclude",map);
	}

	public String selectIcdCategory(String lic_id) {
		return (String) querySingle("selectIcdCategory", lic_id);
	}

	public Integer selectValidForInput(String lus_id) {
		return (Integer) querySingle("selectValidForInput", lus_id);
	}

//	public Map selectValidDoc(String spaj) {
//		return (Map) querySingle("selectValidDoc", spaj);
//	}
	
	public String selectNamaBankData(String id_bank){
		return (String) querySingle("selectNamaBankData",id_bank);
	}

	public List selectBillingInformation2(String reg_spaj, Integer tahun_ke,
			Integer premi_ke) {
		Map params = new HashMap();
		params.put("spaj", reg_spaj);
		params.put("tahun", new Integer(tahun_ke));
		params.put("premi", new Integer(premi_ke));
		return query("selectBillingInformation2", params);
	}
	
	public void updateKloterKePeserta( String spaj, String noKloter) throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("noKloter", noKloter);
		update("updateKloterKePeserta", map);
	}
	
	public void updateKloterKePeserta2( String polis, String noKloter) throws DataAccessException {
		Map map = new HashMap();
		map.put("polis", polis);
		map.put("noKloter", noKloter);
		update("updateKloterKePeserta2", map);
	}
	
	public void updateUmurMstPeserta( String spaj, Integer umur, String noreg) throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("umur", umur);
		map.put("noreg", noreg);
		update("updateUmurMstPeserta", map);
	}
	
	public void insertMstVisaCamp(String spaj,Integer nominal, String lus_id, Integer flag_med, Integer flag_nb){
		Map param =new HashMap();
 		param.put("spaj",spaj);
 		param.put("nominal",nominal);
 		param.put("lus_id",lus_id);
 		param.put("flag_med",flag_med);
 		param.put("flag_nb",flag_nb);
 		insert("insert.mst_visa_camp", param);
	}
	
	public String selectSeqPaymentId(){
		return(String) querySingle("selectSeqPaymentId", null);
	}
	
	public String selectSeqCommId(){
		return(String) querySingle("selectSeqCommId", null);
	}
	
	public String selectSeqEmailId(){
		return(String) querySingle("selectSeqEmailId",null);
	}
	
	public String selectSeqScanId(){
		return(String) querySingle("selectSeqScanId",null);
	}
	
	public String selectSeqInboxId(){
		return(String) querySingle("selectSeqInboxId",null);
	}
	
	public String selectSeqUrlSecureId(){
		return(String) querySingle("selectSeqUrlSecureId",null);
	}
	
	public HashMap selectDataRKCC(String reg_spaj) throws DataAccessException{
		return (HashMap) querySingle("selectDataRKCC", reg_spaj);
	}
	
	public HashMap selectDataRKVA(String reg_spaj) throws DataAccessException{
		return (HashMap) querySingle("selectDataRKVA", reg_spaj);
	}
	
	public String selectNoJmBySpajNB(String reg_spaj){
		return(String) querySingle("selectNoJmBySpajNB", reg_spaj);
	}
	
	public void updateMstPtcTm(HashMap map){
		update("updateMstPtcTm",map);
	}
	
	public List<DropDown> selectProdKes(String spaj) {
		return query("selectDropDownProdkes",spaj);
	}

	public List selectDataKes(String kode_prod, String number_prod, String spaj) {
		Map map = new HashMap();
		map.put("kode_prod", kode_prod);
		map.put("number_prod", number_prod);
		map.put("spaj", spaj);		
		return query("selectDataKes", map);
	}

	public List selectNasabahPernahSubstandart(Map paramNS) {
		return query("selectNasabahPernahSubstandart",paramNS);
	}	
	
	public void updateMstInsuredStatusBas(String spaj, Integer insuredNo,
			Integer liAksep, Integer liAktif,String lusId,Integer flagTgl) {
			Map up=new HashMap();
			up.put("spaj",spaj);
			up.put("txtinsured_no",insuredNo);
			up.put("li_aksep",liAksep);
			up.put("li_aktif",liAktif);		
			update("update.mst_insured_status_bas",up);
	}

	public void insertMstPositionSpajWithSubIdBas(String lus_id,
		String deskripsi, String spaj, Integer subLiAksep, Integer lssa_id_bas) throws DataAccessException {		
		Map p = new HashMap();
		p.put("lus_id", lus_id);
		p.put("msps_desc", deskripsi);
		p.put("reg_spaj", spaj);
		p.put("sub_id", subLiAksep);	
		p.put("lssa_id_bas", lssa_id_bas);	
		insert("insertMstPositionSpajWithSubIdBas", p);	
	}

	public String selectDetailStatus(Integer liAksep, Integer subLiAksep) {
		Map map = new HashMap();
		map.put("liAksep", liAksep);
		map.put("subLiAksep", subLiAksep);
		return(String) querySingle("selectDetailStatusBas", map);
	}
		
	public List<HashMap> selectDataFollowUpFR(Integer warning, String leader) {
		HashMap map = new HashMap();
		map.put("warning", warning);
		map.put("leader", leader);
		return query("selectDataFollowUpFR", map);
	}
	
	public Map selectMstPaymentVisa(String spaj)throws DataAccessException{
		return (Map) querySingle("selectMstPaymentVisa", spaj);
	}
	
	public List<Map> selectPaymentVisaTertinggal() throws DataAccessException {
		return query("selectMstPaymentVisaTertinggal",null);
	}
	
	public Integer selectValidAdminKantorPemasaran(String lus_id, String lar_id) {
		HashMap map = new HashMap();
		map.put("lus_id", lus_id);
		map.put("lar_id", lar_id);
		return (Integer) querySingle("selectValidAdminKantorPemasaran", map);
	}
	
//	public List selectDataTmms(String s_kata, String s_tipe,
//			String s_pilter) {
//		Map map = new HashMap();
//		map.put("s_kata", s_kata);
//		map.put("s_tipe", s_tipe);
//		map.put("s_pilter", s_pilter);		
//		return query("selectDataTmms", map);
//	}

	public Integer selectMedPlusRiderAddon(String spaj) {		
		return (Integer) querySingle("selectMedPlusRiderAddon", spaj);
	}
	
	public ArrayList selectDataSpajDmtmdiPayment() {
		return (ArrayList) query("selectDataSpajDmtmdiPayment", null);
	}

	public Map selectDataFromDrekCC(String reg_spaj) {
		return (Map) querySingle("selectDataFromDrekCC", reg_spaj);
	}

	public String prosesSimultanMedis(String s_spaj, String lus_id) {
		HashMap map = new HashMap();
		String result = "";
		map.put("lus_id", lus_id);
		map.put("spaj", s_spaj);
		map.put("result", result);
		
		getSqlMapClientTemplate().queryForObject("elions.uw.prosesSimultanMedis", map); 
		result = (String) map.get("result");
		
		return result;
		
		//return (String) querySingle("prosesSimultanMedis", map);
		//update("prosesSimultanMedis", map);
	}
	
	public String prosesValQuest(String s_spaj, String lus_id) {
		HashMap map = new HashMap();
		String result = "";
		map.put("lus_id", lus_id);
		map.put("spaj", s_spaj);
		map.put("result", result);
		
		getSqlMapClientTemplate().queryForObject("elions.uw.prosesValQuest", map); 
		result = (String) map.get("result");
		
		return result;
		
		//return (String) querySingle("prosesValQuest", map);
		//update("prosesValQuest", map);
	}	

	public String selectMedisDesc(Integer jn_medis) {
		return (String) querySingle("selectMedisDesc",jn_medis);
	}

	public List selectHistorySpeedy(String s_spaj) {
		return query("selectHistorySpeedy", s_spaj);
	}

	public Integer cekFund(String spaj) {
		return (Integer) querySingle("cekFund",spaj);
	}
	
//	public List selectDataLQG(Integer type, Date question_valid_date) {
//		Map map = new HashMap();
//		map.put("type", type);
//		map.put("question_valid_date", question_valid_date);
//		return query("selectLQG", map);
//	}
	
	public List selectDataLQG(Integer type, Date question_valid_date , Integer index, Integer index2) {
		Map map = new HashMap();
		map.put("type", type);
		map.put("question_valid_date", question_valid_date);
		map.put("index", index);
		map.put("index2", index2);
		return query("selectLQG", map);
	}
	
	public List selectDataLQL(Integer type , Date question_valid_date, Integer index, Integer index2) {
		Map map = new HashMap();
		map.put("type", type);
		map.put("question_valid_date", question_valid_date);
		map.put("index", index);
		map.put("index2", index2);
		return query("selectLQL", map);
	}
	
	public List selectDataLQLTambahan(Integer type , Date question_valid_date, Integer index, Integer index2, int peserta) {
		Map map = new HashMap();
		map.put("type", type);
		map.put("question_valid_date", question_valid_date);
		map.put("index", index);
		map.put("index2", index2);
		if(peserta == 1){
			map.put("tambahan", "3,4,5,6,7");
		}else if(peserta == 2){
			map.put("tambahan", "4,5,6,7");
		}else if(peserta == 3){
			map.put("tambahan", "5,6,7");
		}else if(peserta == 4){
			map.put("tambahan", "6,7");
		}else if(peserta == 5){
			map.put("tambahan", "7");
		}else if(peserta == 6){
			map.put("tambahan", "");
		}
		return query("selectDataLQLTambahan", map);
	}

	public MedQuest selectHasilQuest(String spaj, int flag_jenis_peserta) {
		Map map = new HashMap();
		map.put("spa", spaj);
		map.put("fjp", flag_jenis_peserta);
		return ( MedQuest )  querySingle("selectHasilQuest",map);
	}	
	
	public void insertMstPrintHistory(String spaj,String deskripsi, Integer pageCount, String lus_id) throws DataAccessException {		
		Map map = new HashMap();
		map.put("reg_spaj", spaj);
		map.put("print_desc", deskripsi);
		map.put("page_count", pageCount);	
		map.put("lus_id", lus_id);
		insert("insertMstPrintHistory", map);	
	}	
	
	public String selectMscoNoByMonth(String msag_id, String month){
		HashMap map = new HashMap();
		map.put("msag_id", msag_id);
		map.put("month", month);
		return (String) querySingle("selectMscoNoByMonth", map); 
	}
	
	public Integer selectPengaliCaraBayar(String reg_spaj) {	
		return (Integer) querySingle("selectPengaliCaraBayar", reg_spaj);
	}
	
	public void saveMstTransHistory(String reg_spaj, String kolom_tgl, Date tgl, String kolom_user, String lus_id){
		if(tgl == null) tgl = commonDao.selectSysdate();
		
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("kolom_tgl", kolom_tgl);
		map.put("tgl", tgl);
		map.put("kolom_user", kolom_user);
		map.put("lus_id", lus_id);
		
		Integer exist = (Integer) querySingle("selectMstTransHist",reg_spaj);
		
		if(exist > 0){
			update("updateMstTransHistory", map);
		}else{
			insert("insertMstTransHistory", map);
		}
	}
	
	public void updateTransHistoryDelPolisRetur(String reg_spaj){
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		update("updateTransHistoryDelPolisRetur", map);
	}
	
	public HashMap selectFirstUserUwProses(String reg_spaj){
		return (HashMap) querySingle("selectFirstUserUwProses", reg_spaj);
	}
	
	public void updateMspoProvider(String spaj, Integer mspo_provider)
	{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("mspo_provider", mspo_provider);
		update("updateMspoProvider",param);
	}
	
	public String prosesAkseptasiSpeedy(String s_spaj, String lus_id) {
		HashMap map = new HashMap();
		String result = "";
		map.put("lus_id", lus_id);
		map.put("spaj", s_spaj);
		map.put("result", result);
		
		getSqlMapClientTemplate().queryForObject("elions.uw.prosesAkseptasiSpeedy", map); 
		result = (String) map.get("result");
		
		try {
			this.batchingSimasPrimelink(s_spaj); //Chandra A - 20180702: batching simas prime link (rider save) 
			//this.blacklistChecking(s_spaj); //chandra 20181008, email compliance. helpdesk 127356
		} catch (Exception ex) {
			ex.printStackTrace();
		}
				
		return result;
		
		//return (String) querySingle("prosesAkseptasiSpeedy", map);
		//update("prosesAkseptasiSpeedy", map);
	}
	
	/**
	 * method untuk update batching simas primelink
	 * @param spaj
	 * @throws Exception
	 * @author Chandra 20180702
	 */
	public void batchingSimasPrimelink(String spaj) throws Exception{
		Map spajData = new HashMap();
		spajData = selectMstProductInsured(spaj);
		
		if(spajData != null){
			BigDecimal kode_produk = (BigDecimal)spajData.get("LSBS_ID");
			BigDecimal kode_sub_produk = (BigDecimal)spajData.get("LSDBS_NUMBER");
			
			String with_batch = "";
			if(spajData.get("FLAG_BATCH") != null){
				with_batch = (spajData.get("FLAG_BATCH")).toString().trim();
			}
			
			if(kode_produk.toString().equalsIgnoreCase("134") && kode_sub_produk.toString().equalsIgnoreCase("10") && with_batch.equalsIgnoreCase("BATCH")){
				try {
					if(spajData.get("NO_BATCH") == null) {
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						HashMap hsmp = selectMstUlink(spaj);
						Date mu_tgl_input = sdf.parse(sdf.format((Date)hsmp.get("MU_TGL_INPUT")));
						
						hsmp = selectMstBatchSwitching();
						String batch_no = (String)hsmp.get("NO_BATCH");
						Date batch_date_end = sdf.parse(sdf.format((Date)hsmp.get("END_DATE_PERIODE")));
						
						if(mu_tgl_input.compareTo(batch_date_end) < 0 || mu_tgl_input.compareTo(batch_date_end) == 0){
							UpdateMstPolicyNoBatch(batch_no, spaj);
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}	
	
	/**
	 * @param spaj
	 * helpdesk [133187], tambah kegiatan sms pada saat spaj inforce //chandra
	 */
	
	public void prosesSMSAkseptasi(String spaj){
		try{
			Pemegang pemegang = new Pemegang();
			pemegang = bacDao.selectpp(spaj);
			if(pemegang.getLca_id().equals("40") && pemegang.getLwk_id().equals("01") && (pemegang.getLsrg_id().equals("04") || pemegang.getLsrg_id().equals("11"))){				
				String polis_no = pemegang.getMspo_policy_no();
				String pesan = "Nasabah Yth, Polis No " + FormatString.nomorPolis(polis_no) + " sudah aktif. Premi lanjutan mendebet rekening tabungan anda. Info 021-50163977 atau kunjungi epolicy.sinarmasmsiglife.co.id.";
								
				com.ekalife.elions.model.sms.Smsserver_out sms_out = new com.ekalife.elions.model.sms.Smsserver_out();
				sms_out.setReg_spaj(spaj);
				sms_out.setMspo_policy_no(polis_no);
				sms_out.setJenis(21);
				sms_out.setLjs_id(21);
				sms_out.setRecipient(pemegang.getNo_hp() != null ? pemegang.getNo_hp() : pemegang.getNo_hp2());
				sms_out.setText(pesan);
				sms_out.setLus_id(44892); //user testing
	
				basDao.insertSmsServerOutWithGateway(sms_out, true);
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("kirim sms error [prosesSMSAkseptasi]");
		}
	}
	
	/**Fungsi:	Untuk repot data Polis Retur 
	 * @param	Date bdate, Date edate, String proses
	 * @author 	Lufi
	 */
	public List selectReportPolisDataRetur(String bdate, String edate,
			String proses) {
		Map map = new HashMap();
		map.put("bdate", bdate);
		map.put("edate", edate);
		map.put("proses", proses);		
		return query("selectReportPolisDataRetur", map);
	}
	
	/**Fungsi:	Untuk update data no.Resi pengiriman di mst_policy
	 * @param	String spaj, String no_resi
	 * @author 	Lufi
	 */
	public void updateMstPolicyNoResi(String spaj, String s_noresi) {
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("no_resi",new String(s_noresi));
		update("update.mst_policyNoResi",param);
		
	}
	/**Fungsi:	Untuk select Data report Uw & Collection
	 * @param	String bdate, String edate
	 * @author 	Lufi
	 * @param tipe 
	 */
	public List selectReportUwCollection(Map params) {		
		return query("selectReportUwCollection", params);
	}
	
	public Integer cekSpajTransferToFilling(String reg_spaj){
		Integer result = 0;
		result = (Integer) querySingle("cekSpajTransferToFilling", reg_spaj);
		return result;
	}
	
	public HashMap selectMstConfig(Integer app_id, String section, String sub_section){
		HashMap result;
		HashMap param = new HashMap();
		param.put("app_id", app_id);
		param.put("section", section);
		param.put("sub_section", sub_section);
		result =(HashMap) querySingle("selectMstConfig", param);
		return result;
	}
	
	public HashMap selectEmailBancass(String lcb_no){
		HashMap result;
		HashMap param = new HashMap();
		param.put("lcb_no", lcb_no);
		result =(HashMap) querySingle("selectEmailBancass", param);
		return result;
	}
	
	public HashMap<String, Object> selectFilterSpajLockIdInbox(String posisi, String tipe, String kata, String pilter,String lssaId,String lsspId, String tgl_lahir) {
		if("LIKE%".equals(pilter))
			kata = " like replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("%LIKE".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') ";
		else if("%LIKE%".equals(pilter))
			kata = " like '%' ||replace(upper(trim('"+kata+"')),'.') || '%' ";
		else if("LT".equals(pilter))
			kata = " < rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("LE".equals(pilter))
			kata = " <= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("EQ".equals(pilter))
			kata = " = rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GE".equals(pilter))
			kata = " >= rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";
		else if("GT".equals(pilter))
			kata = " > rpad(replace(upper(trim('"+kata+"')),'.'),11,' ') ";

		Map params = new HashMap();
		if(lssaId==null){
			params.put("posisi", posisi);
		}else{
			params.put("posisi", "in ("+posisi+")");
		}
		params.put("tipe", tipe);
		params.put("kata", kata);
		params.put("lssaId",lssaId);
		params.put("lssp_id",lsspId);
		params.put("tgl_lahir", tgl_lahir);
		return (HashMap)querySingle ("selectLockIdInbox", params);
	}
	public void updateMstInboxLockId(String lus_id, String mi_id){
		Map map = new HashMap();
		map.put("lus_id", lus_id);
		map.put("mi_id", mi_id);
		update("updateMstInboxLockId", map);
	}
	
	/**Fungsi:	Untuk insert lst_ulangan dengan spaj dan tanggal yang sama tapi dengan perbedaan detik
	 * @param	String bdate, String edate
	 * @author 	Rahmayanti
	 */
	public void insertLstUlangan3(String reg_spaj, int addSecond, String jenis, Integer lus_id, String keterangan) throws DataAccessException{
		Map p = new HashMap();
		p.put("reg_spaj", reg_spaj);
		p.put("addSecond", addSecond);
		p.put("jenis", jenis);
		p.put("lus_id", lus_id);
		p.put("keterangan", keterangan);
		Boolean a = true;
		while(a){
			try{
				insert("insertLstUlangan3", p);
				a=false;
			}catch (Exception e) {
				a=true;
			}
		}
	}
	
	public List<Map> schedulerNotProceedWith() throws DataAccessException {
		return query("schedulerNotProceedWith",null);
	}
	
		
/*	public List<Map> selectUserUW(Integer tipe){
		return query("selectUserUW",tipe);
	}*/
	
//	public Integer selectCountUserUW(String lde_id, Integer lus_id, Integer lspd_id) {
//		HashMap map = new HashMap();
//		map.put("lde_id", lde_id);
//		map.put("lus_id", lus_id);
//		map.put("lspd_id", lspd_id);
//		return (Integer) querySingle("selectCountUserUW", map);
//	}
	
	public List<Map> selectCountUserUW(Integer lspd_id, Integer product) {
		HashMap map = new HashMap();
		map.put("lspd_id", lspd_id);
		map.put("product", product);
		return query("selectCountUserUW", map);
	}
	
	public List<Map> selectPemegangPolis2(String tipe, String reg_spaj, String mcl_first, Date mspe_date_birth, String mspe_no_identity, String mspe_mother) {
		HashMap map = new HashMap();
		map.put("tipe", tipe);
		map.put("reg_spaj", reg_spaj);
		map.put("mcl_first", mcl_first);
		map.put("mspe_date_birth", mspe_date_birth);
		map.put("mspe_no_identity", mspe_no_identity);
		map.put("mspe_mother", mspe_mother);
		return query("selectPemegangPolis2", map);
	}
	
	public Integer selectLockID(String reg_spaj, Integer lspd_id){
		HashMap map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("lspd_id", lspd_id);
		return (Integer) querySingle("selectLockID",map);
	}
	
	
	public List<Map> schedulerNotProceedWithNonBsim() throws DataAccessException {
		return query("schedulerNotProceedWithNonBsim",null);
	}
	
	public List<Map> selectLspin(String productCode) {		
		return query("selectLspin",productCode);
	}
	
	public void insertTmSales(Map params) throws DataAccessException {		
		insert("insertTmSales", params);
	}
	
	public void insertMstUrlSecure(Map params) throws DataAccessException {		
		insert("insertMstUrlSecure", params);
	}
	
	public HashMap<String, Object> selectDetailKartuPas(String no_kartu) throws DataAccessException {
		return (HashMap<String, Object>) querySingle("selectDetailKartuPas", no_kartu);
	}
	
	public String selectLcbNo(Integer lrb_id) throws DataAccessException {
		return (String) querySingle("selectLcbNo", lrb_id);
	}
	
	public HashMap<String, Object> selectKotaBank(String lbn_id) throws DataAccessException {
		return (HashMap<String, Object>) querySingle("selectKotaBank", lbn_id);
	}
	
	public String selectMedisDescNew(String s_spaj) throws DataAccessException {
		return (String) querySingle ("selectMedisDescNew", s_spaj);
	}

	public void updateFlagSpeedy(String spaj, Integer speedy) throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("speedy", speedy);
		update("updateFlagSpeedy", map);
	}

	public void insertMstSpeedyHistory(String spaj, String keterangan, String lus_id) throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("keterangan", keterangan);
		map.put("lus_id", lus_id);
		insert("insertMstSpeedyHistory", map);
	}
	
//	public ArrayList selectKomisiForPtcjm(String no_spaj, String flag) {
//		Map param = new HashMap();
//		param.put("no_spaj", no_spaj);
//		param.put("flag", flag);
//		ArrayList listComm=(ArrayList) query("selectKomisiForPtcjm", param);
//		if(listComm==null)listComm=new ArrayList();
//		return listComm;
//	}
//	
//	public ArrayList selectCommBonusForPtcjm(String no_spaj, String flag) {
//		Map param = new HashMap();
//		param.put("no_spaj", no_spaj);
//		param.put("flag", flag);
//		ArrayList listCommBonus=(ArrayList) query("selectCommBonusForPtcjm", param);
//		if(listCommBonus==null)listCommBonus=new ArrayList();
//		return listCommBonus;
//	}
//	
//	public ArrayList selectCommRewardForPtcjm(String no_spaj, String flag) {
//		Map param = new HashMap();
//		param.put("no_spaj", no_spaj);
//		param.put("flag", flag);
//		ArrayList listCommReward=(ArrayList) query("selectCommRewardForPtcjm", param);
//		if(listCommReward==null)listCommReward=new ArrayList();
//		return listCommReward;
//	}

	public List selectDataPendingSmilePrioritas() {		
		return query("selectDataPendingSmilePrioritas",null);
	}
	
	public List selectReportCoverLetterJne(String spaj, String dist) {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("dist", dist);
		return query("selectReportCoverLetterJne", map);
	}
	
	public List selectReportFollowUpCsfCall(String tanggalAwal, String tanggalAkhir) {
		Map map = new HashMap();
		map.put("tanggalAwal", tanggalAwal);
		map.put("tanggalAkhir", tanggalAkhir);
		return query("selectReportFollowUpCsfCall", map);
	}

    public String savePayment(TopUp topup, int flag, User currentUser, Integer i_flagCC) {
		Date dateNow = this.commonDao.selectSysdate("dd", false, 0);
		
		if(topup.getMspa_payment_id() == null) topup.setMspa_payment_id("");
		List kurs = this.bacDao.selectnilaikurs("02", defaultDateFormat.format(topup.getMspa_date_book()));
		
		if(topup.getBill_lku_id().equals(topup.getLku_id())){
			topup.setMspa_nilai_kurs(new Double(1));
		}else{
			topup.setMspa_nilai_kurs((Double) ((Map) kurs.get(0)).get("LKH_KURS_JUAL"));
		}
		
		String tempTahunPremiKe = topup.getJenis();
		if(tempTahunPremiKe!= null && !"".equals( tempTahunPremiKe )){
			
		}else{
			tempTahunPremiKe = "1@1";
		}
		String tahunKe = tempTahunPremiKe.substring(0,tempTahunPremiKe.indexOf("@"));
		String premiKe = tempTahunPremiKe.substring(tempTahunPremiKe.indexOf("@")+1, tempTahunPremiKe.length());

		if(topup.getMspa_payment_id().equals("")){
			topup.setMspa_active(new Integer(1));
			topup = this.uwDao.insertTopUp(topup, flag, currentUser);
		}else{
			this.uwDao.updateMst_payment(topup, currentUser);
			this.uwDao.updateMst_tag_payment(topup, topup.getTahun_ke().intValue(), topup.getPremi_ke().intValue());
			this.uwDao.updateBilling(topup, topup.getTahun_ke().intValue(), topup.getPremi_ke().intValue());
		}
		
		String norek_ajs = null, jenis = null, no_pre = null;
		Date tgl_trx = new Date();
		List mstDrekBasedNoTrx = uwDao.selectMstDrekBasedNoTrx(topup.getNo_trx());
		if(mstDrekBasedNoTrx.size()>0){
			Map viewMstDrekDetail = (Map) mstDrekBasedNoTrx.get(0);
			norek_ajs = (String) viewMstDrekDetail.get("NOREK_AJS");
			jenis = (String) viewMstDrekDetail.get("JENIS");
			tgl_trx = (Date) viewMstDrekDetail.get("TGL_TRX");
			if(viewMstDrekDetail.get("NO_PRE") != null){
				no_pre = (String) viewMstDrekDetail.get("NO_PRE");
				topup.setNo_pre(no_pre);
			}
		}
		if("insert".equals(topup.getActionTypeForDrekDet())){
			if(topup.getNo_trx()!= null && !"".equals(topup.getNo_trx())){
				String noUrut = uwDao.selectMaxNoUrutMstDrekDet( topup.getNo_trx() ) ;
				if(noUrut != null && !"".equals( noUrut )){
					
				}else{  
					noUrut = "0";
				}
				this.uwDao.updateMst_payment(topup, currentUser);
				uwDao.insertMstDrekDet(topup.getNo_trx(), tahunKe, premiKe, topup.getMspa_payment(), LazyConverter.toInt(noUrut) + 1, topup.getReg_spaj(), null, topup.getMspa_payment_id(),currentUser.getLus_id(), dateNow, topup.getNo_pre(), norek_ajs, jenis, tgl_trx);
			}
			
		}else if("update".equals(topup.getActionTypeForDrekDet())){
			List<DrekDet> mstDrekDetBasedSpaj = uwDao.selectMstDrekDet( null, topup.getReg_spaj(), null, null );
			List<Payment> payment = uwDao.selectPaymentCount(topup.getReg_spaj(), 1, 1);
			if(payment.get(0).getLsrek_id()!=0){
				if(mstDrekDetBasedSpaj.size()==0){
					String noUrut = uwDao.selectMaxNoUrutMstDrekDet( topup.getNo_trx() ) ;
					if(noUrut != null && !"".equals( noUrut )){
						
					}else{  
						noUrut = "0";
					}
					uwDao.insertMstDrekDet(topup.getNo_trx(), tahunKe, premiKe, topup.getMspa_payment(), LazyConverter.toInt(noUrut) + 1, topup.getReg_spaj(), null, topup.getMspa_payment_id(),currentUser.getLus_id(), dateNow, topup.getNo_pre(), norek_ajs, jenis, tgl_trx);
				
				}else{
					uwDao.updateMstDrekDet(tahunKe, premiKe, topup.getMspa_payment(), topup.getNo_trx(), 
							topup.getReg_spaj(), null, topup.getMspa_payment_id(), currentUser.getLus_id(),  dateNow, jenis, topup.getNo_pre());
				}
			}
		}
		
		//Tarik data dari tabel MST_DREK, trus diupdate juga datanya
		if(topup.getNo_trx() != null) {
			if(!topup.getNo_trx().trim().equals("")){
				Integer flagTunggalGabungan = null;
				if("Tunggal".equals( topup.getTipe() )){
					flagTunggalGabungan = 0;
				}else if( "Gabungan".equals( topup.getTipe() )){
					flagTunggalGabungan = 1;
				}
				uwDao.updateMstDrek(topup.getReg_spaj(), currentUser.getLus_id(), topup.getNo_trx(),flagTunggalGabungan, jenis);
			}
		}
		
		//MANTA
        if(i_flagCC == 1) bacDao.updateMstDrekCc(topup.getMspa_no_rek(), topup.getMspa_payment().intValue());
		return "Data pembayaran berhasil disimpan.";
	}
	
	public Map selectTransferImaging(String mi_id) throws DataAccessException {
		return (HashMap) querySingle("selectTransferImaging", mi_id);
	}

	
	/**Fungsi:	Untuk Kirim Email Permohonan Akseptasi SPT
	 * 
	 * @param reg_Spaj 	     : No SPAJ
	 * @param currentUser    : User proses 
	 * @param fileName		 : Nama file pdf SPT yang akan di comfort
	 * @param destSPT        : Direktori file SPT
	 * @param fileName2		 : Nama file ouput hasil convert
	 * @param flag_proses    : Departemen Proses(1=UW 2=CSFL)
	 * @param jenisEmail     : Email baru atau Reminder
	 * @return statusProses  : 1=berhasil 0=Gagal
	 * @author 	Lufi	
	 */
	public Integer kirimEmailPermohonanSPT(String reg_spaj, User currentUser, String fileName, String destSPT, String fileName2, Integer flag_proses,String jenisEmail)  {
		String keterangan=new String() ,jenis =new String(), lus_director=new String(), name=new String();
		Integer statusProses = 1;
		HashMap mapEmail = new HashMap();
		Date nowDate = commonDao.selectSysdate();
		Integer flagDepartemen = 1;		
 		Integer months = nowDate.getMonth()+1;
 		Integer years = nowDate.getYear()+1900;
		List<DropDown> ListImage = new ArrayList<DropDown>();								
		String imageSPT=fileName2+"1"+".jpg";
		ListImage.add(new DropDown("spt", destSPT+imageSPT));
		String lca_id=selectCabangFromSpaj(reg_spaj);		
		if( flag_proses == 1){
			mapEmail = selectMstConfig(6, "kirimEmailPermohonanSPT", "SPT_UW");
//			keterangan = "SPT  - WAITING APPROVAL";
//			jenis = "WAITING APPROVAL SPT";
			flagDepartemen = 1;
		}else if(flag_proses == 2){
			mapEmail = selectMstConfig(6, "kirimEmailPermohonanSPT", "SPT_CS");
//			keterangan = "SPT DIKIRIM KE CSFL";
//			jenis = "WAITING APPROVAL SPT";
//			fileName="SPT_"+reg_spaj.trim()+"_001.pdf";
			flagDepartemen = 2;
		}
		String emailTo = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():"";
		String emailCc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():"";
		String bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():"";
		String from="ajsjava@sinarmasmsiglife.co.id";		
		List <Map> dataPolis=selectDataPemegangPolisSpt(reg_spaj);
		HashMap mapDataPolis=(HashMap)dataPolis.get(0);
		String namaPemegang=(String)mapDataPolis.get("PEMEGANG");
		String namaTertanggung=(String)mapDataPolis.get("TERTANGGUNG");
		String prod=(String)mapDataPolis.get("LSDBS_NAME");
		BigDecimal up1 = (BigDecimal) mapDataPolis.get("UP");
         String up = FormatString.formatCurrency("", up1);
		BigDecimal pr=(BigDecimal)mapDataPolis.get("PREMI_RUPIAH");
		BigDecimal pd=(BigDecimal)mapDataPolis.get("PREMI_DOLLAR");	
		String premi_rupiah=null;
		String premi_dollar=null;
		String mgi=(String)mapDataPolis.get("MGI");
		String no_rek=(String)mapDataPolis.get("NO_REK");
		if (pr!=null){							

			premi_rupiah=FormatString.formatCurrency(" ", pr);
		}

		if (pd!=null){
			premi_dollar=FormatString.formatCurrency(" ", pd);
		}
		if(mgi==null){
			mgi="-";
		}
		if(no_rek==null){
			no_rek="-";
		}
		
		String tanggal=(String)mapDataPolis.get("TANGGAL_MULAI");
		if(tanggal==null)tanggal="-";
		String mrc_nama=(String)mapDataPolis.get("MRC_NAMA");
		if(mrc_nama==null)mrc_nama="-";
		String cabang=(String)mapDataPolis.get("CABANG");
		if(cabang==null)cabang="-";
		String bayar=(String)mapDataPolis.get("CARA_BAYAR");
		if(bayar==null)bayar="-";   
    	String subject= "[URGENT!] PERMOHONAN AKSEPTASI SPT untuk SPAJ NO: "+FormatString.nomorSPAJ(reg_spaj)+" "+"Pemegang Polis :"+" "+namaPemegang;	
    	StringBuffer pesan = new StringBuffer();	
		String [] to =emailTo.split(";");
		try{
				for(int i=0;i<to.length;i++){
					String [] sendingTo = new String[] {to[i]};	
					String me_id = sequence.sequenceMeIdEmail();
					String destTo=props.getProperty("embedded.mailpool.dir")+"\\" +years+"\\"+FormatString.rpad("0", months.toString(), 2)+"\\"+me_id+"\\";
			   	    File destDir2 = new File(destTo);
					if(!destDir2.exists()) destDir2.mkdirs();
					PDFToImage.convertPdf2Image(destSPT,  destTo, fileName, fileName2,null,null);					
			   		if(to[i].equals("noor@sinarmasmsiglife.co.id")){
			   			lus_director="1653";
			   			name="Noor";
			   			
			   		}
			   		else if(to[i].equals("eny@sinarmasmsiglife.co.id")){
			   			lus_director="812";
			   			name="Eny";
			   		}		
			   		else if(to[i].equals("achmad_r@sinarmasmsiglife.co.id")){
			   			lus_director="2140";
			   			name="Achmad R";
			   		}
			   		else if(to[i].equals("yayuk@sinarmasmsiglife.co.id")){
			   			lus_director="2293";
			   			name="Yayuk T";
			   		}
			   		else if(to[i].equals("kristina@sinarmasmsiglife.co.id")){
			   			lus_director="1654";
			   			name="Kristina";
			   		}
			   		else if(to[i].equals("jajat@sinarmasmsiglife.co.id")){
			   			lus_director="1550";
			   			name="Jajat";
			   		}
			   		else if(to[i].equals("bunga@sinarmasmsiglife.co.id")){
			   			lus_director="2141";
			   			name="Bunga";
			   		}//				   		
			   		else if(to[i].equals("Maria_I@sinarmasmsiglife.co.id")){
			   			lus_director="1493";
			   			name="Maria";
			   		}
			   		else if(to[i].equals("rosiana@sinarmasmsiglife.co.id")){
			   			lus_director="781";
			   			name="Rosiana";
			   		}
			   		else if(to[i].equals("Inge@sinarmasmsiglife.co.id")){
			   			lus_director="687";
			   			name="Inge";
			   		}
			   		else if(to[i].equals("Tities@sinarmasmsiglife.co.id")){
			   			lus_director="1161";
			   			name="Tities";
			   		}
			   		else{//buat tes aja
			   			lus_director="2998";
			   			name="Lufi";
			   		}
			   		
			   		pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: yellow;} table td{border: 1px solid black;}</style></head>");
			   		pesan.append("<body bgcolor='#ffffc9'>Email ini terkirim otomatis oleh System E-Lions, mohon jangan lakukan Reply/Forward terhadap email yang dikirim ini.<br><br>");
			   		pesan.append("<br>"+jenisEmail+"<br>");
				 	pesan.append("Telah dilakukan Validasi oleh&nbsp"+currentUser.getName()+"untuk SPT dari :<br><br>");
				 	pesan.append("<ul><li>Nama Pemegang Polis\t\t:\t"+namaPemegang+"</li>");
				 	pesan.append("<li>Nama Tertanggung\t\t:\t"+namaTertanggung+"</li>");
				 	pesan.append("<li>Produk\t\t\t:\t"+prod+"</li>");
				 		
					if (premi_rupiah!=null){
						
						 pesan.append("<li>Uang Pertanggungan\t\t:\t"+"Rp"+" "+up+"</li>");
						 pesan.append("<li>Premi/Cara Bayar\t\t:\t"+"Rp"+" "+premi_rupiah+"/"+bayar+"</li>");
				    }
					if (premi_dollar!=null){			 	
					    pesan.append("<li>Uang Pertanggungan\t\t:\t"+"$"+" "+up+"</li>");
					 	pesan.append("<li>Premi\t\t\t:\t"+"$"+" "+premi_dollar+"/"+bayar+"</li>");
		
					}
			    	pesan.append("<li>MGI\t\t\t:\t"+mgi+"</li>");
			    	pesan.append("<li>Mulai Asuransi\t\t:\t"+tanggal+"</li>");
			    	pesan.append("<li>No.Rek Pembayaran Manfaat\t:\t"+no_rek+" "+"a/n"+" "+mrc_nama+"</li>");
			    	pesan.append("<li>Cabang\t\t\t:\t"+cabang+"</li></ul>");
			   
			    	pesan.append("<center><img border='0' src='cid:"+imageSPT+"'></center><br><br>");
				 	pesan.append("Untuk proses selanjutnya, silahkan klik link dibawah:<br><br>");
		//   		 	pesan2.append("<a href='http://lufi-lap:8080/E-Lions/uw/uw.htm?window=aksepcs&id="+lus_director+"&spaj="+reg_spaj+"&pp="+namaPemegang+"&status="+"1"+"&email="+currentUser.getEmail()+"&flag="+"2"+"&name="+name+"'><font size='10'>Setuju</font></a>\t");						   		 		
		//		 		pesan2.append("<a href='http://lufi-lap:8080/E-Lions/uw/uw.htm?window=aksepcs&id="+lus_director+"&spaj="+reg_spaj+"&pp="+namaPemegang+"&status="+"2"+"&email="+currentUser.getEmail()+"&flag="+"2"+"&name="+name+"'><font color='#fd020e' size='10'>Tidak Setuju</font></a><br><br>");
				 	
				 	String link1 = "http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=aksepsp&id="+lus_director+"&spaj="+reg_spaj+"&pp="+namaPemegang+"&status="+"1"+"&email="+currentUser.getEmail()+"&flag="+flagDepartemen+"&name="+name;
				 	String link2 = "http://elions.sinarmasmsiglife.co.id/uw/uw.htm?window=aksepsp&id="+lus_director+"&spaj="+reg_spaj+"&pp="+namaPemegang+"&status="+"2"+"&email="+currentUser.getEmail()+"&flag="+flagDepartemen+"&name="+name;
				 	
				 	String tkey1 = "";
				 	String tkey2 = "";
			   		try {
			   			tkey1 = commonDao.encryptUrlKey("aksepsp", reg_spaj, App.ID, link1);
			   			tkey2 = commonDao.encryptUrlKey("aksepsp", reg_spaj, App.ID, link2);
			   		}catch (Exception e) {
						logger.error("ERROR", e);
					}
			   		
			   		link1 = link1 +"&tkey="+tkey1;
			   		link2 = link2 +"&tkey="+tkey2;
				 	
				 	pesan.append("<a href='"+link1+"'><font size='10'>Setuju</font></a>\t");						   		 		
				 	pesan.append("<a href='"+link2+"'><font color='#fd020e' size='10'>Tidak Setuju</font></a><br><br>");
				 	
				 	
				 	pesan.append("Jika anda mengalami kesulitan dengan link diatas, gunakan link dibawah:<br><br><br>");                 	            		
			        pesan.append("<a href='http://apache/E-Lions/uw/uw.htm?window=aksepsp&id="+lus_director+"&spaj="+reg_spaj+"&pp="+namaPemegang+"&status="+"1"+"&email="+currentUser.getEmail()+"&flag="+flagDepartemen+"&name="+name+"&tkey="+tkey1+"'><font size='10'>Setuju</font></a>\t");
					pesan.append("<a href='http://apache/E-Lions/uw/uw.htm?window=aksepsp&id="+lus_director+"&spaj="+reg_spaj+"&pp="+namaPemegang+"&status="+"2"+"&email="+currentUser.getEmail()+"&flag="+flagDepartemen+"&name="+name+"&tkey="+tkey2+"'><font color='#fd020e'size='10'>Tidak Setuju</font></a><br><br>");
					pesan.append("<br>Pengirim: " + currentUser.getName() + "&nbsp" + "</body></html>");			   		 		
				 	
					EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, commonDao.selectSysdate(), null, 
							true, from,												
							sendingTo, 
							new String[]{emailCc}, 
							new String[]{bcc}, 
							subject+" "+namaPemegang, 
							pesan.toString(), 
							null,7);
		   		 	pesan.delete(0, pesan.length());
			   		
				}
		}catch (Exception e) {
			statusProses = 0;
			return statusProses;
					
		}
		return statusProses;
	}
	
	public List<Payment> selectMstPaymentAll(String spaj) {		
		return query("selectMstPaymentAll", spaj);
	}
	
	public HashMap selectMstTransHistoryNewBussines(String reg_spaj) throws DataAccessException{
		return (HashMap) querySingle("selectMstTransHistoryNewBussines", reg_spaj);
	}
	
	public void deleteMstProductInsured(String reg_spaj, Integer lsbs_id, Integer lsdbs_number) throws DataAccessException {
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("lsbs_id", lsbs_id);
		map.put("lsdbs_number", lsdbs_number);
		delete("deleteMstProductInsured", map);
	}
	
	public void deleteMstBiayaUlink(String reg_spaj, Integer lsbs_id, Integer lsdbs_number, Integer ljb_id) throws DataAccessException {
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("lsbs_id", lsbs_id);
		map.put("lsdbs_number", lsdbs_number);
		map.put("ljb_id", ljb_id);
		delete("deleteMstBiayaUlink", map);
	}

	//Rahmayanti - Proses Snows
	public void prosesSnows(String spaj, String lusId, Integer lspd_id, Integer lspd_id_coll){
		/* lst_dokumen_position
		 * admin = 207
		 * uw new business = 202
		 * uw proses = 210
		 * uw helpdesk = 209
		 * uw printing = 211
		 * imaging 201
		 * collection = 212
		 * filiing = 99
		 * bas qa1 = 218
		 * bas qa2 = 219
		 * bas = 208
		 */	
		
		Policy policy = this.selectMstPolicyAll(spaj);
		String no_blanko = policy.getMspo_no_blanko();	
		Integer lspd_id_polis = policy.getLspd_id();
		
		Integer lspd_id_from = 207; //lspd_id_from uw awal
		Integer lspd_id_from_coll =207; //lspd_id_from collection awal 
		Integer mi_pos = 1; //unclean case
		Integer flag_pending = 0;
		Integer lspd_id_from2 = 0;
		Integer age = 0;
		Double up, kurs;
		String lku_id;
		Date nowDate = commonDao.selectSysdate();
		
		String mi_id = uwDao.selectSeqInboxId();
		String mi_id_collection;		
		
		List <Map> mapInbox = uwDao.selectMstInbox(spaj, null);
		
		List <Map> mapInboxUw = uwDao.selectMstInbox(spaj, "1");
		List <Map> mapInboxColl = uwDao.selectMstInbox(spaj, "2");
		List <Map> mapInboxUwProdNew = uwDao.selectMstInboxNewProduct(no_blanko);
		List <Map> mapInboxUwProd = uwDao.selectMstInbox(spaj, "3");
		
		Integer countInbox = mapInbox.size(); //mengecek inbox
		
		Integer flag_validasi = selectSPBTTD(spaj);
		
		long tanggal = 0;
		
		//jika lspd_id!=null untuk collection imaging
		if(lspd_id!=null&&lspd_id_coll!=201){
			//jika lspd_id 201 sudah imaging maka clean case bagi uw
			if(lspd_id==201){
				mi_pos = 2;  //clean case
			}
			if(lspd_id==209){
				flag_pending = 1;
			}
			if(lspd_id==202){
				Insured insured = this.selectMstInsuredAll(spaj);
				Product produk = this.selectMstProductInsuredUtamaFromSpaj(spaj);
				age = insured.getMste_age().intValue();
				up = produk.getMspr_tsi();			
				/*Map MapKurs=this.selectTodayKurs("02", nowDate);
				if(produk.getLku_id().equals("02")){
					if(MapKurs!=null){
						up= ((Double)MapKurs.get("LKH_CURRENCY"))*up; 
					}
				}*/
//				if(produk.getLku_id().equals("02")){
//					if((up>25000 || age>50) ){
//						lspd_id = 210;
//					}else{
//						lspd_id = 202;
//					}
//				}else{
//					if(up>250000000 || age>50){
//						lspd_id = 210;
//					}else{
//						lspd_id = 202;
//					}
//				}
			}
			
			//data di inbox ada
			if(countInbox>0){
				//data di inbox hanya ada 1
				if(!mapInboxUw.isEmpty()&&mapInboxColl.isEmpty()&&lspd_id_polis==27){
					Integer lock_id_hist = 0;
					Integer product = 0;
					HashMap inbox =(HashMap) mapInboxUw.get(0);
					mi_id = (String) inbox.get("MI_ID");
					BigDecimal lspd_id_before = (BigDecimal) inbox.get("LSPD_ID");
					lspd_id_from = lspd_id_before.intValue();
					BigDecimal lspd_before = (BigDecimal) inbox.get("LSPD_ID_FROM");
					lspd_id_from2 = lspd_before.intValue();
					Integer checkCollection = uwDao.selectInboxCheckingLspdId(spaj, 212);	
					
					//set lock_id berdasarkan total_spaj, lus_full_name, dan pemegang polis yang sama
					List <Map> total_spaj = uwDao.selectCountUserUW(lspd_id, product);
					HashMap mapCekUser =(HashMap) total_spaj.get(0);				
					List <Map> cekPemegang = uwDao.selectPemegangPolis2("0", spaj, null, null, null, null);	
					HashMap mapCekPemegang =(HashMap) cekPemegang.get(0);
					String mcl_first =(String) mapCekPemegang.get("MCL_FIRST");
					Date mspe_date_birth = (Date) mapCekPemegang.get("MSPE_DATE_BIRTH");
					String mspe_no_identity = (String) mapCekPemegang.get("MSPE_NO_IDENTITY"); 
					String mspe_mother = (String) mapCekPemegang.get("MSPE_MOTHER");
					List <Map> cekPemegangLain = uwDao.selectPemegangPolis2("1", spaj, mcl_first, mspe_date_birth, mspe_no_identity, mspe_mother);
					Integer temp = 0;
					if(mapCekUser!=null && !lspd_id.equals(lspd_id_from)&&(!(lspd_id.equals(lspd_id_before)&&lspd_id==210))){
						List <Map> user_lock_uw = uwDao.selectMstInboxHist(mi_id, 0, null);
						List <Map> user_lock_qa = uwDao.selectMstInboxHist(mi_id, 1, lspd_id);	
						HashMap user = new HashMap();
						if(lspd_id.equals(lspd_id_from2)&&!user_lock_uw.isEmpty()){	
							user = (HashMap)user_lock_uw.get(0);
							lock_id_hist = ((BigDecimal) user.get("LOCK_ID")).intValue();
							uwDao.updateMstInboxLockId(""+lock_id_hist+"", mi_id);	
						}
						else if(lspd_id==218&&!user_lock_qa.isEmpty()){
							user = (HashMap)user_lock_qa.get(0);
							lock_id_hist = ((BigDecimal) user.get("LOCK_ID")).intValue();
							uwDao.updateMstInboxLockId(""+lock_id_hist+"", mi_id);	
						}
						else{
							BigDecimal lock_id= (BigDecimal) mapCekUser.get("LUS_ID");
							Integer lus_id = lock_id.intValue();
								if(cekPemegangLain.size()>=1){
									for(int j=0; j<cekPemegangLain.size(); j++){
										HashMap mapCekPemegangLain =(HashMap) cekPemegangLain.get(j);
										String reg_spaj =(String) mapCekPemegangLain.get("REG_SPAJ");
										Integer select_lock_id = uwDao.selectLockID(reg_spaj,lspd_id);
										if(select_lock_id!=null){
											uwDao.updateMstInboxLockId(""+select_lock_id+"", mi_id);	
											lock_id_hist = select_lock_id;
										}
										if((j==(cekPemegangLain.size()-1)) && select_lock_id ==null){
											uwDao.updateMstInboxLockId(""+lus_id+"", mi_id);	
											lock_id_hist = lus_id;
										}						
									}															
								}
								else{
										uwDao.updateMstInboxLockId(""+lus_id+"", mi_id);	
										lock_id_hist = lus_id;
								}
						}				
					}						
											
					//mengecek di inbox apakah lspd_id sesudah dan lspd_id sebelum sama
					//jika sama maka set lspd_id_after dan lspd_id_before di inbox_histnya sama dengan inbox_hist sebelum. perbedaannya cuma create_id
					//jika sama maka set lspd_id dan lspd_id_from di inbox sama dengan inbox sebelum. perbedaan cuma lock_id dan tg_berkas_masuk		
					if(!lspd_id.equals(lspd_id_from)){
						if(lspd_id==209){
							MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id, null, lspd_id_from, null, Integer.parseInt(lusId), nowDate, lock_id_hist,0, flag_validasi);
							uwDao.updateMstInboxLspdId(mi_id, lspd_id, lspd_id_from, mi_pos, flag_pending, spaj, null, null, flag_validasi);
							uwDao.insertMstInboxHist(mstInboxHist);	
						}
						else{
							MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id, null, null, null, Integer.parseInt(lusId), nowDate, lock_id_hist,0,flag_validasi);
							uwDao.updateMstInboxLspdId(mi_id, lspd_id, lspd_id_from, mi_pos, flag_pending, null, null, null, flag_validasi);
							uwDao.insertMstInboxHist(mstInboxHist);	
						}		
					}					
					
					//mengecek data collection diinbox tidak ada
					if(checkCollection==0){
//						nowDate = commonDao.selectSysdate(); //qwer
						tanggal = nowDate.getTime();
						tanggal = tanggal + 1000;
						nowDate=new Date(tanggal);
						
						mi_id_collection = uwDao.selectSeqInboxId();
						MstInbox mstInbox_collection = new MstInbox(mi_id_collection, 29, lspd_id_coll, null, lspd_id_from_coll, 
								null, null, spaj, null, 
								null, 1, nowDate, null, 
								null, null, Integer.parseInt(lusId), nowDate, null, null, 1, null, null, null, null, null, null, null,null,0,null);
						uwDao.insertMstInbox(mstInbox_collection);
						
												
						//req dari Ferra - Ridhaal
						//1. untuk spaj DMTM set lock_id ke Alif_BAM (4852)
						//2. untuk spaj NON DMTM set lock_id dengan bagi sama rata antara ANDREAS_L (4990) dan FERRA (4867)
						
						String lca_id=selectCabangFromSpaj(spaj);
						Integer lock_id_col = 0;
						
						if (lca_id.equalsIgnoreCase("40")){
							lock_id_col = 4852; //Alief_BAM
						}else{//pembagian user antara ANDREAS dengan FERRA
							Integer userCol1 = uwDao.cekTotalSpajLockUserCollection(4990);//Andreas
							Integer userCol2 = uwDao.cekTotalSpajLockUserCollection(4867);//Ferra
							
							if (userCol1 <= userCol2 ){
								lock_id_col = 4990; //Andreas
							}else{
								lock_id_col = 4867; //Ferra
							}
							
						}						
						uwDao.updateMstInboxLockId(""+lock_id_col+"", mi_id_collection);	
						
						MstInboxHist mstInboxHist_collection = new MstInboxHist(mi_id_collection, lspd_id_from_coll, lspd_id_coll, null, null, null, Integer.parseInt(lusId), nowDate, lock_id_col,0,0);	
						uwDao.insertMstInboxHist(mstInboxHist_collection);
					}
				
				}
				//data di inbox ada 2
				if((!mapInboxUw.isEmpty()&&!mapInboxColl.isEmpty())||(!mapInboxUw.isEmpty()&&lspd_id_polis!=27)){
					Integer lock_id_hist = 0;
					Integer product = 0;
					HashMap inbox_uw =(HashMap) mapInboxUw.get(0);
					mi_id = (String) inbox_uw.get("MI_ID");
					BigDecimal lspd_id_before = (BigDecimal) inbox_uw.get("LSPD_ID");
					lspd_id_from = lspd_id_before.intValue();
					BigDecimal lspd_before = (BigDecimal) inbox_uw.get("LSPD_ID_FROM");
					lspd_id_from2 = lspd_before.intValue();
//					nowDate = commonDao.selectSysdate(); //qwer
					tanggal = nowDate.getTime();
					tanggal = tanggal + 1000;
					nowDate=new Date(tanggal);
					
					//back to bas				
					if(lspd_id==207){
						uwDao.updateMstInboxLspdId(mi_id, lspd_id, lspd_id_from, mi_pos, flag_pending, null, null, null, flag_validasi);
						uwDao.updateMstInboxLockId(null, mi_id);	
						MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id, null, null, null, Integer.parseInt(lusId), nowDate, null,0, flag_validasi);	
						uwDao.insertMstInboxHist(mstInboxHist);
					}
					else{
						//set lock_id berdasarkan total_spaj, lus_full_name, dan pemegang polis yang sama
						List <Map> total_spaj = uwDao.selectCountUserUW(lspd_id, product);
						HashMap mapCekUser =(HashMap) total_spaj.get(0);				
						List <Map> cekPemegang = uwDao.selectPemegangPolis2("0", spaj, null, null, null, null);	
						HashMap mapCekPemegang =(HashMap) cekPemegang.get(0);
						String mcl_first =(String) mapCekPemegang.get("MCL_FIRST");
						Date mspe_date_birth = (Date) mapCekPemegang.get("MSPE_DATE_BIRTH");
						String mspe_no_identity = (String) mapCekPemegang.get("MSPE_NO_IDENTITY"); 
						String mspe_mother = (String) mapCekPemegang.get("MSPE_MOTHER");
						List <Map> cekPemegangLain = uwDao.selectPemegangPolis2("1", spaj, mcl_first, mspe_date_birth, mspe_no_identity, mspe_mother);
						Integer temp = 0;
						if(mapCekUser!=null && !lspd_id.equals(lspd_id_from)&&(!(lspd_id.equals(lspd_id_before)&&lspd_id==210))){
							List <Map> user_lock_uw = uwDao.selectMstInboxHist(mi_id, 0, null);
							List <Map> user_lock_qa = uwDao.selectMstInboxHist(mi_id, 1, lspd_id);	
							HashMap user = new HashMap();
							if(lspd_id.equals(lspd_id_from2)&&!user_lock_uw.isEmpty()){	
								user = (HashMap)user_lock_uw.get(0);
								lock_id_hist = ((BigDecimal) user.get("LOCK_ID")).intValue();
								uwDao.updateMstInboxLockId(""+lock_id_hist+"", mi_id);	
							}
							else if(lspd_id==218&&!user_lock_qa.isEmpty()){
								user = (HashMap)user_lock_qa.get(0);
								lock_id_hist = ((BigDecimal) user.get("LOCK_ID")).intValue();
								uwDao.updateMstInboxLockId(""+lock_id_hist+"", mi_id);	
							}
							else{
								BigDecimal lock_id= (BigDecimal) mapCekUser.get("LUS_ID");
								Integer lus_id = lock_id.intValue();
									if(cekPemegangLain.size()>=1){
										for(int j=0; j<cekPemegangLain.size(); j++){
											HashMap mapCekPemegangLain =(HashMap) cekPemegangLain.get(j);
											String reg_spaj =(String) mapCekPemegangLain.get("REG_SPAJ");
											Integer select_lock_id = uwDao.selectLockID(reg_spaj,lspd_id);
											if(select_lock_id!=null){
												uwDao.updateMstInboxLockId(""+select_lock_id+"", mi_id);	
												lock_id_hist = select_lock_id;
											}
											if((j==(cekPemegangLain.size()-1)) && select_lock_id ==null){
												uwDao.updateMstInboxLockId(""+lus_id+"", mi_id);	
												lock_id_hist = lus_id;
											}						
										}															
									}
									else{
											uwDao.updateMstInboxLockId(""+lus_id+"", mi_id);	
											lock_id_hist = lus_id;
									}
							}				
						}
						//set lock id collection jika lock id = null ketika ada data collection diinbox - ridhaal
						Integer checkCollection = uwDao.selectInboxCheckingLspdId(spaj, 212);						
						if(!mapInboxColl.isEmpty() && checkCollection > 0){ 
							HashMap inbox =(HashMap) mapInboxColl.get(0);
							mi_id_collection = (String) inbox.get("MI_ID");		
							BigDecimal lock_id_coll_exist = (BigDecimal) inbox.get("LOCK_ID");
//							nowDate = commonDao.selectSysdate(); //qwer
							tanggal = nowDate.getTime();
							tanggal = tanggal + 1000;
							nowDate=new Date(tanggal);
																												
							if (lock_id_coll_exist == null ){
								
								//req dari Ferra - Ridhaal
								//1. untuk spaj DMTM set lock_id ke Alif_BAM (4852)
								//2. untuk spaj NON DMTM set lock_id dengan bagi sama rata antara ANDREAS_L (4990) dan FERRA (4867)
								
								String lca_id=selectCabangFromSpaj(spaj);
								Integer lock_id_col = null;
								
								
								if (lca_id.equalsIgnoreCase("40")){
									lock_id_col = 4852; //Alief_BAM
								}else{//pembagian user antara ANDREAS dengan FERRA
									Integer userCol1 = uwDao.cekTotalSpajLockUserCollection(4990);//Andreas
									Integer userCol2 = uwDao.cekTotalSpajLockUserCollection(4867);//Ferra
									
									if (userCol1 <= userCol2 ){
										lock_id_col = 4990; //Andreas
									}else{
										lock_id_col = 4867; //Ferra
									}
									
								}						
								uwDao.updateMstInboxLockId(""+lock_id_col+"", mi_id_collection);				
								
								MstInboxHist mstInboxHist_collection = new MstInboxHist(mi_id_collection, 212, lspd_id_coll, null, null, null, Integer.parseInt(lusId), nowDate, lock_id_col,0,0);	
								uwDao.insertMstInboxHist(mstInboxHist_collection);
							}
						}
						
						if((!lspd_id.equals(lspd_id_from))){
							List <Map> selectUserQa1 = this.selectUserQa1(spaj, 218);
//							nowDate = commonDao.selectSysdate(); //qwer
							tanggal = nowDate.getTime();
							tanggal = tanggal + 1000;
							nowDate=new Date(tanggal);
							
							if(lspd_id==209){
								MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id, null, lspd_id_from, null, Integer.parseInt(lusId), nowDate, lock_id_hist,0, flag_validasi);	
								uwDao.updateMstInboxLspdId(mi_id, lspd_id, lspd_id_from, mi_pos, flag_pending, spaj, null, null, flag_validasi);
								uwDao.insertMstInboxHist(mstInboxHist);						
							}else if(lspd_id==218 && !selectUserQa1.isEmpty()){
								HashMap dataFurther =(HashMap) selectUserQa1.get(0);
								String mi_desc = (String) dataFurther.get("MI_DESC");
								Integer flag_kategoris = ((BigDecimal) dataFurther.get("FLAG_KATEGORI")).intValue();
								
								MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id, null, null, mi_desc, Integer.parseInt(lusId), nowDate, lock_id_hist, flag_kategoris, flag_validasi );	
								uwDao.updateMstInboxLspdId(mi_id, lspd_id, lspd_id_from, mi_pos, flag_pending, null, null, mi_desc, flag_validasi);
								uwDao.insertMstInboxHist(mstInboxHist);	
								
							}else{	
	//							Ulangan u = new Ulangan();
	//							u.setReg_spaj(spaj);
	//							u.setTanggal(new Date());
	//							u.setJenis("EDIT DATA");
	//							u.setLus_id(4620);
	//							u.setKeterangan("EDIT INBOX SNOWS DARI LSPD_ID 202 JADI 211 DAN LSPD_ID_FROM  DARI 207 JADI 210 KRN TIDAK BISA FILLING SAAT TTP");
	//							insertLstUlangan(u);
								MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id, null, null, null, Integer.parseInt(lusId), nowDate, lock_id_hist,0, flag_validasi);	
								uwDao.updateMstInboxLspdId(mi_id, lspd_id, lspd_id_from, mi_pos, flag_pending, null, null, null, flag_validasi);
								uwDao.insertMstInboxHist(mstInboxHist);								
							}
							
						}					
					}
							
				}
				
				if(!mapInboxUwProdNew.isEmpty()){
					Integer lock_id_hist = 0;
					Integer product = 1;
					
					HashMap inbox_uw =(HashMap) mapInboxUwProd.get(0);
					String old_reg_spaj = (String) inbox_uw.get("REG_SPAJ");
					mi_id = (String) inbox_uw.get("MI_ID");
					BigDecimal lspd_id_before = (BigDecimal) inbox_uw.get("LSPD_ID");
					lspd_id_from = lspd_id_before.intValue();
					BigDecimal lspd_before = (BigDecimal) inbox_uw.get("LSPD_ID_FROM");
					lspd_id_from2 = lspd_before.intValue();
					Integer checkCollection = uwDao.selectInboxCheckingLspdId(spaj, 212);
//					nowDate = commonDao.selectSysdate(); //qwer
					tanggal = nowDate.getTime();
					tanggal = tanggal + 1000;
					nowDate=new Date(tanggal);
					
					//back to bas				
					if(lspd_id==207){
						lspd_id = 208;
						uwDao.updateMstInboxLspdId(mi_id, lspd_id, lspd_id_from, mi_pos, flag_pending, null, null, null, flag_validasi);
						uwDao.updateMstInboxLockId(null, mi_id);	
						MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id, null, null, null, Integer.parseInt(lusId), nowDate, null,0, flag_validasi);	
						uwDao.insertMstInboxHist(mstInboxHist);
					}
					else{
						//set lock_id berdasarkan total_spaj, lus_full_name, dan pemegang polis yang sama
						List <Map> total_spaj = uwDao.selectCountUserUW(lspd_id, product);
						if(!total_spaj.isEmpty()&&(!(lspd_id.equals(lspd_id_before)&&lspd_id==210))){
							List <Map> user_lock_uw = uwDao.selectMstInboxHist(mi_id, 0, null);
							List <Map> user_lock_qa = uwDao.selectMstInboxHist(mi_id, 1, lspd_id);	
							HashMap user = new HashMap();
							if(lspd_id.equals(lspd_id_from2)&&!user_lock_uw.isEmpty()){	
								user = (HashMap)user_lock_uw.get(0);
								lock_id_hist = ((BigDecimal) user.get("LOCK_ID")).intValue();
								uwDao.updateMstInboxLockId(""+lock_id_hist+"", mi_id);	
							}
							else if(lspd_id==218&&!user_lock_qa.isEmpty()){
								user = (HashMap)user_lock_qa.get(0);
								lock_id_hist =  ((BigDecimal) user.get("LOCK_ID")).intValue();
								uwDao.updateMstInboxLockId(""+lock_id_hist+"", mi_id);	
							}
							else{
								HashMap mapCekUser =(HashMap) total_spaj.get(0);				
								List <Map> cekPemegang = uwDao.selectPemegangPolis2("0", spaj, null, null, null, null);	
								HashMap mapCekPemegang =(HashMap) cekPemegang.get(0);
								String mcl_first =(String) mapCekPemegang.get("MCL_FIRST");
								Date mspe_date_birth = (Date) mapCekPemegang.get("MSPE_DATE_BIRTH");
								String mspe_no_identity = (String) mapCekPemegang.get("MSPE_NO_IDENTITY"); 
								String mspe_mother = (String) mapCekPemegang.get("MSPE_MOTHER");
								List <Map> cekPemegangLain = uwDao.selectPemegangPolis2("1", spaj, mcl_first, mspe_date_birth, mspe_no_identity, mspe_mother);
								Integer temp = 0;
								if(mapCekUser!=null && !lspd_id.equals(lspd_id_from)){
									BigDecimal lock_id= (BigDecimal) mapCekUser.get("LUS_ID");
									Integer lus_id = lock_id.intValue();
										if(cekPemegangLain.size()>=1){
											for(int j=0; j<cekPemegangLain.size(); j++){
												HashMap mapCekPemegangLain =(HashMap) cekPemegangLain.get(j);
												String reg_spaj =(String) mapCekPemegangLain.get("REG_SPAJ");
												Integer select_lock_id = uwDao.selectLockID(reg_spaj,lspd_id);
												if(select_lock_id!=null){
													uwDao.updateMstInboxLockId(""+select_lock_id+"", mi_id);	
													lock_id_hist = select_lock_id;
												}
												if((j==(cekPemegangLain.size()-1)) && select_lock_id ==null){
													uwDao.updateMstInboxLockId(""+lus_id+"", mi_id);	
													lock_id_hist = lus_id;
												}						
											}															
										}
										else{
												uwDao.updateMstInboxLockId(""+lus_id+"", mi_id);	
												lock_id_hist = lus_id;
										}								
								}
								
							}
							
							if(!lspd_id.equals(lspd_id_from)){
								if(lspd_id==209){
									MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id, null, lspd_id_from, null, Integer.parseInt(lusId), nowDate, lock_id_hist,0, flag_validasi);	
									uwDao.updateMstInboxLspdId(mi_id, lspd_id, lspd_id_from, mi_pos, flag_pending, spaj, null, null, flag_validasi);
									uwDao.insertMstInboxHist(mstInboxHist);						
								}else{
									MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id, null, null, null, Integer.parseInt(lusId), nowDate, lock_id_hist,0, flag_validasi);	
									uwDao.updateMstInboxLspdId(mi_id, lspd_id, lspd_id_from, mi_pos, flag_pending, null, null, null, flag_validasi);
									uwDao.insertMstInboxHist(mstInboxHist);		
								}							
							}			
						}					
					}
					//mengecek data collection diinbox tidak ada
					if(checkCollection==0&&(lspd_id==202||lspd_id==210)){
//						nowDate = commonDao.selectSysdate(); //qwer
						tanggal = nowDate.getTime();
						tanggal = tanggal + 1000;
						nowDate=new Date(tanggal);
						
						mi_id_collection = uwDao.selectSeqInboxId();
						MstInbox mstInbox_collection = new MstInbox(mi_id_collection, 29, lspd_id_coll, null, lspd_id_from_coll, 
								null, null, spaj, null, 
								null, 1, nowDate, null, 
								null, null, Integer.parseInt(lusId), nowDate, null, null, 1, null, null, null, null, null, null, null,null, 0,null);
						uwDao.insertMstInbox(mstInbox_collection);
						
						//req dari Ferra - Ridhaal
						//1. untuk spaj DMTM set lock_id ke Alif_BAM (4852)
						//2. untuk spaj NON DMTM set lock_id dengan bagi sama rata antara ANDREAS_L (4990) dan FERRA (4867)
						
						String lca_id=selectCabangFromSpaj(spaj);
						Integer lock_id_col = 0;
						
						if (lca_id.equalsIgnoreCase("40")){
							lock_id_col = 4852; //Alief_BAM
						}else{//pembagian user antara ANDREAS dengan FERRA
							Integer userCol1 = uwDao.cekTotalSpajLockUserCollection(4990);//Andreas
							Integer userCol2 = uwDao.cekTotalSpajLockUserCollection(4867);//Ferra
							
							if (userCol1 <= userCol2 ){
								lock_id_col = 4990; //Andreas
							}else{
								lock_id_col = 4867; //Ferra
							}
							
						}						
						uwDao.updateMstInboxLockId(""+lock_id_col+"", mi_id_collection);	
						
						MstInboxHist mstInboxHist_collection = new MstInboxHist(mi_id_collection, lspd_id_from_coll, lspd_id_coll, null, null, null, Integer.parseInt(lusId), nowDate, lock_id_col,0,0);	
						uwDao.insertMstInboxHist(mstInboxHist_collection);
					}	
					else if(!mapInboxColl.isEmpty() && checkCollection > 0){ //set lock id collection jika lock id = null ketika ada data collection diinbox - ridhaal
						HashMap inbox =(HashMap) mapInboxColl.get(0);
						mi_id_collection = (String) inbox.get("MI_ID");								
						BigDecimal lock_id_coll_exist = (BigDecimal) inbox.get("LOCK_ID");	
//						nowDate = commonDao.selectSysdate(); //qwer
						tanggal = nowDate.getTime();
						tanggal = tanggal + 1000;
						nowDate=new Date(tanggal);
						
						if (lock_id_coll_exist == null){
							
							//req dari Ferra - Ridhaal
							//1. untuk spaj DMTM set lock_id ke Alif_BAM (4852)
							//2. untuk spaj NON DMTM set lock_id dengan bagi sama rata antara ANDREAS_L (4990) dan FERRA (4867)
							
							String lca_id=selectCabangFromSpaj(spaj);
							Integer lock_id_col = null;
							
							if (lca_id.equalsIgnoreCase("40")){
								lock_id_col = 4852; //Alief_BAM
							}else{//pembagian user antara ANDREAS dengan FERRA
								Integer userCol1 = uwDao.cekTotalSpajLockUserCollection(4990);//Andreas
								Integer userCol2 = uwDao.cekTotalSpajLockUserCollection(4867);//Ferra
								
								if (userCol1 <= userCol2 ){
									lock_id_col = 4990; //Andreas
								}else{
									lock_id_col = 4867; //Ferra
								}
								
							}						
							uwDao.updateMstInboxLockId(""+lock_id_col+"", mi_id_collection);				
							
							MstInboxHist mstInboxHist_collection = new MstInboxHist(mi_id_collection, 212, lspd_id_coll, null, null, null, Integer.parseInt(lusId), nowDate, lock_id_col,0,0);	
							uwDao.insertMstInboxHist(mstInboxHist_collection);
						}							
					}
				}
			
			//data diibox tidak ada
			//insert inbox beserta lock_id, inbox_hist, inbox_checklist
			}else{
				if(!mapInboxUwProdNew.isEmpty()){
//					nowDate = commonDao.selectSysdate(); //qwer
					tanggal = nowDate.getTime();
					tanggal = tanggal + 1000;
					nowDate=new Date(tanggal);
					
					Integer lock_id_hist = 0;
					Integer product = 1;
					flag_pending = 3;
					
					HashMap inbox_uw =(HashMap) mapInboxUwProdNew.get(0);
					String old_reg_spaj = (String) inbox_uw.get("REG_SPAJ");
					mi_id = (String) inbox_uw.get("MI_ID");
					BigDecimal lspd_id_before = (BigDecimal) inbox_uw.get("LSPD_ID");
					lspd_id_from = lspd_id_before.intValue();
					
					//set lock_id berdasarkan total_spaj, lus_full_name, dan pemegang polis yang sama
					List <Map> total_spaj = uwDao.selectCountUserUW(lspd_id, product);
					HashMap mapCekUser =(HashMap) total_spaj.get(0);				
					List <Map> cekPemegang = uwDao.selectPemegangPolis2("0", spaj, null, null, null, null);	
					HashMap mapCekPemegang =(HashMap) cekPemegang.get(0);
					String mcl_first =(String) mapCekPemegang.get("MCL_FIRST");
					Date mspe_date_birth = (Date) mapCekPemegang.get("MSPE_DATE_BIRTH");
					String mspe_no_identity = (String) mapCekPemegang.get("MSPE_NO_IDENTITY"); 
					String mspe_mother = (String) mapCekPemegang.get("MSPE_MOTHER");
					List <Map> cekPemegangLain = uwDao.selectPemegangPolis2("1", spaj, mcl_first, mspe_date_birth, mspe_no_identity, mspe_mother);
					Integer temp = 0;
					if(mapCekUser!=null){
						BigDecimal lock_id= (BigDecimal) mapCekUser.get("LUS_ID");
						Integer lus_id = lock_id.intValue();
							if(cekPemegangLain.size()>=1){
								for(int j=0; j<cekPemegangLain.size(); j++){
									HashMap mapCekPemegangLain =(HashMap) cekPemegangLain.get(j);
									String reg_spaj =(String) mapCekPemegangLain.get("REG_SPAJ");
									Integer select_lock_id = uwDao.selectLockID(reg_spaj,lspd_id);
									if(select_lock_id!=null){
										uwDao.updateMstInboxLockId(""+select_lock_id+"", mi_id);	
										lock_id_hist = select_lock_id;
									}
									if((j==(cekPemegangLain.size()-1)) && select_lock_id ==null){
										uwDao.updateMstInboxLockId(""+lus_id+"", mi_id);	
										lock_id_hist = lus_id;
									}						
								}															
							}
							else{
									uwDao.updateMstInboxLockId(""+lus_id+"", mi_id);	
									lock_id_hist = lus_id;
							}							
											
						MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id, null, null, null, Integer.parseInt(lusId), nowDate, lock_id_hist,0, flag_validasi);	
						uwDao.updateMstInboxLspdId(mi_id, lspd_id, lspd_id_from, mi_pos, flag_pending, spaj, old_reg_spaj, null, flag_validasi);
						uwDao.insertMstInboxHist(mstInboxHist);			
					}
				}
				else{
					if(lspd_id==202||lspd_id==210){
//						nowDate = commonDao.selectSysdate();//qwer
						tanggal = nowDate.getTime();
						tanggal = tanggal + 1000;
						nowDate=new Date(tanggal);
						
						mi_id_collection = uwDao.selectSeqInboxId();
						lspd_id_coll = 212;
						MstInbox mstInbox_collection = new MstInbox(mi_id_collection, 29, lspd_id_coll, null, lspd_id_from_coll, 
								null, null, spaj, null, 
								null, 1, nowDate, null, 
								null, null, Integer.parseInt(lusId), nowDate, null, null, 1, null, null, null, null, null, null, null,null,0,null);
						uwDao.insertMstInbox(mstInbox_collection);
						
						//req dari Ferra - Ridhaal
						//1. untuk spaj DMTM set lock_id ke Alif_BAM (4852)
						//2. untuk spaj NON DMTM set lock_id dengan bagi sama rata antara ANDREAS_L (4990) dan FERRA (4867)
						
						String lca_id=selectCabangFromSpaj(spaj);
						Integer lock_id_col = 0;
						
						if (lca_id.equalsIgnoreCase("40")){
							lock_id_col = 4852; //Alief_BAM
						}else{//pembagian user antara ANDREAS dengan FERRA
							Integer userCol1 = uwDao.cekTotalSpajLockUserCollection(4990);//Andreas
							Integer userCol2 = uwDao.cekTotalSpajLockUserCollection(4867);//Ferra
							
							if (userCol1 <= userCol2 ){
								lock_id_col = 4990; //Andreas
							}else{
								lock_id_col = 4867; //Ferra
							}
							
						}						
						uwDao.updateMstInboxLockId(""+lock_id_col+"", mi_id_collection);							
						
						MstInboxHist mstInboxHist_collection = new MstInboxHist(mi_id_collection, lspd_id_from_coll, lspd_id_coll, null, null, null, Integer.parseInt(lusId), nowDate, null,0,0);
						uwDao.insertMstInboxHist(mstInboxHist_collection);
					}
					Integer product = 0;
					
					MstInbox mstInbox = new MstInbox(mi_id, 21, lspd_id, null, lspd_id_from, 
							null, null, spaj, null, 
							null, 1, nowDate, null, 
							null, null, Integer.parseInt(lusId), nowDate, null, null, 1, null, null, null, null, null, null, null,null, flag_validasi,null);			
							
					uwDao.insertMstInbox(mstInbox);
					Integer lock_id_hist = 0;
					//set lock_id berdasarkan total_spaj, lus_full_name, dan pemegang polis yang sama
					List <Map> total_spaj = uwDao.selectCountUserUW(lspd_id, product);
					HashMap mapCekUser =(HashMap) total_spaj.get(0);				
					List <Map> cekPemegang = uwDao.selectPemegangPolis2("0", spaj, null, null, null, null);	
					HashMap mapCekPemegang =(HashMap) cekPemegang.get(0);
					String mcl_first =(String) mapCekPemegang.get("MCL_FIRST");
					Date mspe_date_birth = (Date) mapCekPemegang.get("MSPE_DATE_BIRTH");
					String mspe_no_identity = (String) mapCekPemegang.get("MSPE_NO_IDENTITY"); 
					String mspe_mother = (String) mapCekPemegang.get("MSPE_MOTHER");
					List <Map> cekPemegangLain = uwDao.selectPemegangPolis2("1", spaj, mcl_first, mspe_date_birth, mspe_no_identity, mspe_mother);
					Integer temp = 0;
					if(mapCekUser!=null){
						BigDecimal lock_id= (BigDecimal) mapCekUser.get("LUS_ID");
						Integer lus_id = lock_id.intValue();
							if(cekPemegangLain.size()>=1){
								for(int j=0; j<cekPemegangLain.size(); j++){
									HashMap mapCekPemegangLain =(HashMap) cekPemegangLain.get(j);
									String reg_spaj =(String) mapCekPemegangLain.get("REG_SPAJ");
									Integer select_lock_id = uwDao.selectLockID(reg_spaj,lspd_id);
									if(select_lock_id!=null){
										uwDao.updateMstInboxLockId(""+select_lock_id+"", mi_id);
										lock_id_hist = select_lock_id;
									}
									if((j==(cekPemegangLain.size()-1)) && select_lock_id ==null && (uwDao.selectLockID(spaj, null)==null)){
										uwDao.updateMstInboxLockId(""+lus_id+"", mi_id);	
										lock_id_hist = lus_id;
									}						
								}															
							}
							else{
									uwDao.updateMstInboxLockId(""+lus_id+"", mi_id);
									lock_id_hist = lus_id;
							}			
					}

//					nowDate = commonDao.selectSysdate(); //qwer
					tanggal = nowDate.getTime();
					tanggal = tanggal + 1000;
					nowDate=new Date(tanggal);
					
					MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_from, lspd_id, null, null, null, Integer.parseInt(lusId), nowDate, lock_id_hist,0, flag_validasi);	
					uwDao.insertMstInboxHist(mstInboxHist);
					
					File directory = new File(
							props.getProperty("pdf.dir.export") + "\\" +
							uwDao.selectCabangFromSpaj(spaj) + "\\" +
							spaj);
					List<File> daftarFile = FileUtils.listFilesInDirectory2(directory.toString());
					if(daftarFile.size()>0){
						for(int i=0;i<daftarFile.size();i++){
							String fileName =daftarFile.get(i).getName();
							List<Scan> daftarScan = uwDao.selectLstScan("UW",null);
							for(Scan scan : daftarScan){
								if(fileName.contains(scan.getNmfile())){
									List<Integer> lc_id = uwDao.lc_id_value(scan.getId());
									for(int j=0;j<lc_id.size();j++){
										if(uwDao.selectInboxChecklistExist(mi_id, 21, lc_id.get(j))<=0){
											MstInboxChecklist mstInboxChecklist = new MstInboxChecklist(mi_id, 21, (Integer) lc_id.get(j), 1, null, Integer.parseInt(lusId), nowDate);
											uwDao.insertMstInboxChecklist(mstInboxChecklist);
										}
									}
								}
							}
						}
					}
				}
			}
		}else{
			if(!mapInboxColl.isEmpty()){
				HashMap inbox =(HashMap) mapInboxColl.get(0);
				mi_id_collection = (String) inbox.get("MI_ID");				
				uwDao.updateMstInboxLspdId(mi_id_collection, lspd_id_coll, 212, 2, null, null, null, null,0);
//				nowDate = commonDao.selectSysdate(); //qwer
				tanggal = nowDate.getTime();
				tanggal = tanggal + 1000;
				nowDate=new Date(tanggal);
				
				BigDecimal lock_id_coll_exist = (BigDecimal) inbox.get("LOCK_ID");				
				Integer lock_id_col = null;
				
				if (lock_id_coll_exist == null){
					
					//req dari Ferra - Ridhaal
					//1. untuk spaj DMTM set lock_id ke Alif_BAM (4852)
					//2. untuk spaj NON DMTM set lock_id dengan bagi sama rata antara ANDREAS_L (4990) dan FERRA (4867)
					
					String lca_id=selectCabangFromSpaj(spaj);
					
					if (lca_id.equalsIgnoreCase("40")){
						lock_id_col = 4852; //Alief_BAM
					}else{//pembagian user antara ANDREAS dengan FERRA
						Integer userCol1 = uwDao.cekTotalSpajLockUserCollection(4990);//Andreas
						Integer userCol2 = uwDao.cekTotalSpajLockUserCollection(4867);//Ferra
						
						if (userCol1 <= userCol2 ){
							lock_id_col = 4990; //Andreas
						}else{
							lock_id_col = 4867; //Ferra
						}
						
					}						
					uwDao.updateMstInboxLockId(""+lock_id_col+"", mi_id_collection);				
					
				}else{
					lock_id_col = ((BigDecimal) inbox.get("LOCK_ID")).intValue();
				}
				
				SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				String test = dt.format(nowDate);
				int cektgl = uwDao.selectTglInbox(test);
				while (cektgl>0){
					tanggal = nowDate.getTime();
					tanggal = tanggal + 1000;
					nowDate=new Date(tanggal);
					test = dt.format(nowDate);
					cektgl = uwDao.selectTglInbox(test);
				}

				
				MstInboxHist mstInboxHist_collection = new MstInboxHist(mi_id_collection, 212, lspd_id_coll, null, null, null, Integer.parseInt(lusId), nowDate , lock_id_col,0,0);	
				uwDao.insertMstInboxHist(mstInboxHist_collection);
			}
		}
		
		//welcome call cs - ulink total premi >50 juta
//		if(lspd_id_polis==27){
//			Integer jml_premi = this.selectTotalPremiUlink(spaj);
//			if(jml_premi>50000000){
//				mi_id = uwDao.selectSeqInboxId();
//				MstInbox mstInbox_welcall = new MstInbox(mi_id, 63, 204, null, 202, 
//						null, null, spaj, null, 
//						null, 1, nowDate, null, 
//						null, null, Integer.parseInt(lusId), nowDate, null, null, 1, null, null, null, null, null, null, null,null,0,1);
//				MstInboxHist mstInboxHist_welcall = new MstInboxHist(mi_id, 202, 204, null, null, null, Integer.parseInt(lusId), nowDate, null,0,0);
//				uwDao.insertMstInbox(mstInbox_welcall);
//				uwDao.insertMstInboxHist(mstInboxHist_welcall);
//			}
//		}
	}

	public int selectTglInbox(String nowDate) throws DataAccessException {
		return (Integer) querySingle("selectTglInbox", nowDate);
	}
	
	public List<Map> selectKartuPasbyTglInput(String tgl_input) throws DataAccessException {
		HashMap m = new HashMap();
		m.put("tgl_input", tgl_input);
		return query("selectKartuPasbyTglInput", m);
	} 

	public void updateMstInsuredHealthQuest(String spaj, Integer health_quest)  throws DataAccessException {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("health_quest", health_quest);
		update("updateMstInsuredHealthQuest", map);
	}

	public Integer selectProdukNonKuesioner(String reg_spaj) {
		return (Integer) querySingle("selectProdukNonKuesioner", reg_spaj);
	}
	
	public boolean cekDanaInvestasiMencukupi(String reg_spaj) throws DataAccessException {
		boolean cukup = false;
		BigDecimal balance = (BigDecimal) querySingle("validationDanaInvestasiMencukupi", reg_spaj);
		if(balance.doubleValue() >= 0) cukup = true;
		
		return cukup;
	}
	
	public void updateFlagAprove(String reg_spaj,Integer flag_approve, String kolom){
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("kolom", kolom);
		map.put("flag_approve", flag_approve);
		update("updateFlagAprove", map);
	}
	
	public List selectMstPositionQc(String reg_spaj, Integer jenis){
		Map map=new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("jenis", jenis);
		return query("selectMstPositionQc", map);		
	}
	
	public List <Map> selectMstBenefeciary(String reg_spaj) throws DataAccessException {
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		return  query("selectMstBenefeciary", map);
	} 
	
	public List reportQc(String bdate, String edate){
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		return query("selectReportQc",m);
	}	

	public ArrayList selectDataIcdExclude(String spaj) {
		Map param = new HashMap();
		param.put("no_spaj", spaj);		
		ArrayList listICD=(ArrayList) query("selectDataIcdExclude", param);
		if(listICD==null)listICD=new ArrayList();
		return listICD;
	}

	public void prosesEndorsKetinggalanNew(String spaj, Integer lsbs) {
		Map map = new HashMap();
		map.put("spaj", spaj);
		map.put("lsbs", lsbs);
		getSqlMapClientTemplate().queryForObject("elions.uw.prosesEndorsmentKetinggalanNew", map); 
	}

	public Integer selectPunyaEndorsEkaSehatAdmedika(String spaj) {
		return (Integer) querySingle("selectPunyaEndorsEkaSehatAdmedika", spaj);
	}
	
	public List<Map> selectSnowsError(Integer lus_id){
		Map map = new HashMap();
		map.put("lus_id", lus_id);
		return query("selectSnowsError", map);
	}	
	
	public String selectLusId(String reg_spaj){
		return (String) querySingle ("selectLusId", reg_spaj);		
	}
	
	public void updateCancelInbox(String reg_spaj, String lus_id){
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("lus_id", lus_id);
		update("updateCancelInbox", map);
	}	
		
	public List<Map> selectMstInboxNewProduct(String no_blanko){
		Map map = new HashMap();
		map.put("no_blanko", no_blanko);
		return query("selectMstInboxNewProduct", map);
	}		
	
	public List<Map> selectMstInboxHist(String mi_id, Integer flag, Integer lspd_after){
		Map map = new HashMap();
		map.put("mi_id", mi_id);
		map.put("flag", flag);
		map.put("lspd_after", lspd_after);
		return query("selectMstInboxHist", map);
	}	
	
	public List reportAutomatedUw(String bdate, String edate){
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		return query("selectAutomatedUw",m);
	}
	
	public ArrayList<HashMap> selectSpajPAPosisiPayment() {
		return (ArrayList<HashMap>) query("selectSpajPAPosisiPayment", null);
	}

	public List selectDataPolisNpw(String spaj) {
		return query("selectDataPolisNpw",spaj);
	}

	public Map selectDataKuisionerCerdas(String reg_spaj) {		
		return (HashMap) querySingle("selectDataKuisionerCerdas", reg_spaj);
	}
	
	public List<Map> selectTransToFillingBSM() throws DataAccessException {
		return query("selectTransToFillingBSM", null);
	}
	
	public List selectReportSLPolicyIssued(Map params) throws DataAccessException {
		return query("selectReportSLPolicyIssued", params);
	}

	public List selectReportSnowsNB(Map params) {
		return query("selectReportSnowsNB", params);
	}
	
	public String selectPeriodQHealth(String reg_spaj) throws DataAccessException {
		return (String) querySingle("selectPeriodQHealth", reg_spaj);
	}
	
	public List<File> selectListAttFileFurtherSpeedy(String reg_spaj) throws DataAccessException {
		List<File> result = new ArrayList<File>();
		List<String> listAttachmentPath = query("selectListAttFileFurtherSpeedy", reg_spaj);
		
		if(listAttachmentPath == null)
			return null;
		
		for(String path : listAttachmentPath) {
			if(path!= null){
				File file = new File(path);
				if(file.exists()) {
					result.add(file);
				}
			}
			
		}
		
		return result;
	}

	public List selectLsKycNewBus(List KYCnewBis) {
		List lsKycNewBus=new ArrayList();		
		Double batasan=new Double(100000000);
		int  row=0;
		for(int i=0;i<KYCnewBis.size();i++){
			NewBusinessCase newBus=(NewBusinessCase)KYCnewBis.get(i);
			if(newBus.getLsbs_id().intValue() == 164){
				newBus.setNama_topup("TUNGGAL");
			}else if(newBus.getJmlh_tu()==2){
				newBus.setNama_topup("TUNGGAL DAN BERKALA");
			}else if(newBus.getJmlh_tu()==1){
				newBus.setNama_topup(selectKYCnewBisJnsTopUp(newBus.getReg_spaj()));
			}

			double total=newBus.getTotal_tu().doubleValue()+newBus.getPremi_pokok().doubleValue();
			//cek daftar pekerjaan,, apakah termasuk dalam daftar High Risk Customer (HCR)
			//int hrcFilter=selectCountLstHighRiskCust(newBus.getMpn_job_desc(),newBus.getMkl_kerja(), newBus.getMkl_industri());
			ArrayList viewer = Common.serializableList(selectHistoryPengajuan(newBus.getPemegang(),newBus.getMspe_date_birth()));
			if (viewer.size() > 1){
				newBus.setStatusPol("*");
			}
			if(total>=batasan.doubleValue()){//pengecekan apakah total(premi + top up lebih besar dari 100jt ato masuk daftar HRC)
				newBus.setFlagKyc(0);
				newBus.setRow(row);
				User user= uwDao.selectMstpositionSpajUserAccept(newBus.getReg_spaj());
				if(user!=null)
					newBus.setLus_full_name(user.getLus_full_name());
				row++;
				lsKycNewBus.add(newBus);
			}	
		}
		return lsKycNewBus;
	}
	
	public List selectCabangSinarmasAll(String jenis ,String lcb_no){
		Map p = new HashMap();
		p.put("jenis", jenis);
		p.put("lcb_no", lcb_no);
		return query("selectCabangSinarmasAll", p);
	}
	
	public List selectReportSlaUwIndividu(Map params) {
		return query("selectReportSlaUwIndividu", params);
	}

	public void updateMstPolicySatuKolom(String spaj, String namaKolom,
			String nilaiUpdate) {
		Map map = new HashMap();
		map.put("reg_spaj", spaj);
		map.put("nama_kolom", namaKolom);
		map.put("nilai", nilaiUpdate);
		update("updateMstPolicySatuKolom", map);	
	}

	public List selectDataNasabahBerdasarkanNoHp(String hp, String bdate, String nama_pp) {
		Map map = new HashMap();
		map.put("no_hp", hp);
		map.put("bdate", bdate);
		map.put("namaPP", nama_pp);
		return query("selectDataNasabahBerdasarkanNoHp", map);
	}
	
	public List selectDataAgenBerdasarkanNoHp(String hp) {
		return query("selectDataAgenBerdasarkanNoHp", hp);
	}
	
	public List selectUserQa(String reg_spaj, Integer lspd_after) {
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("lspd_after", lspd_after);
		return query("selectUserQa", map);
	}
	
	public List selectUserQa1(String reg_spaj, Integer lspd_after) {
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("lspd_after", lspd_after);
		return query("selectUserQa1", map);
	}

	public Map selectDetailPod(String reg_spaj, String mspo_no_pengiriman) {
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("mspo_no_pengiriman", mspo_no_pengiriman);
		return (Map) querySingle("selectDetailPod", params);
	}

	public Integer selectCountSpajMstRefund(String spaj) {
		return (Integer) querySingle("selectCountSpajMstRefund", spaj);
	}

	public Integer selectCountDataCekPhone(String hp, String nama_pp, String namaPpSebelumnya, String tgl_lahir_pp_sebelumnya, String bdate, String flag) {	
		Map map = new HashMap();
		hp="'"+hp+"'";
		map.put("nama_pp", nama_pp);
		map.put("namaPpSebelumnya", namaPpSebelumnya);
		map.put("tgl_lahir_pp_sebelumnya", tgl_lahir_pp_sebelumnya);
		map.put("hp", hp);		
		map.put("bdate", bdate);
		map.put("flag", flag);
		return (Integer) querySingle("selectCountDataCekPhone", map);
	}

	public void insertMstCheckPhone(String nama_pp, String namaPpSebelumnya,
			String tgl_lahir_pp_sebelumnya, String hp, String bdate, String no_blanko, String flag, String no_polis) {
		Map map = new HashMap();		
		map.put("nama_pp", nama_pp);
		map.put("namaPpSebelumnya", namaPpSebelumnya);
		map.put("tgl_lahir_pp_sebelumnya", tgl_lahir_pp_sebelumnya);
		map.put("hp", hp);
		map.put("no_blanko", no_blanko);
		map.put("bdate", bdate);
		map.put("flag", flag);
		map.put("no_polis", no_polis);		
		insert("insertMstCheckPhone", map);
	}
	
	public List<Map> selectLstMerchantFee(Integer flag_merchant) throws DataAccessException {
		Map map = new HashMap();
		map.put("flag_merchant", flag_merchant);
		return query("selectLstMerchantFee", map);
	}
	
	public List<Map> schedulerNotProceedWithDMTMProcesRecuiring(Integer status_FR, Integer stat_Rec) throws DataAccessException {
		Map map = new HashMap();
		map.put("status_FR", status_FR);
		map.put("stat_Rec", stat_Rec);
		return query("schedulerNotProceedWithDMTMProcesRecuiring",map);
	}
	
	public Pas insertPaTemp(Pas pas, User user) {
		pas.setStatus(0);
		try{
			//mst_counter
				//cplan tambah kolom spaj dummy buat konek ke mst_reff_bii
				
				//waktu transfer mst_comm_reff_bii (20% dr premi (sk))
			//pindahin ke mst_pas_sms buat tempat penampungan sementara
				//insert pas
				// update counter	
				// 130 = NO REFF SPAJ PA BSM
				if("".equals(pas.getReg_spaj()))pas.setReg_spaj(null);
				if(pas.getReg_spaj() == null && !("73".equals(pas.getProduct_code())||"205".equals(pas.getProduct_code()))){
					Long intIDCounter = (Long) this.bacDao.select_counter(130, "01");
						int intID = intIDCounter.intValue();
						String inttgl2Str = "";
						Calendar tgl_sekarang = Calendar.getInstance(); 
						Integer bulan = new Integer(tgl_sekarang.get(Calendar.MONTH)) + 1;
						if(bulan < 10){
							inttgl2Str = new Integer(tgl_sekarang.get(Calendar.YEAR)).toString() + "0" + bulan.toString();
						}else{
							inttgl2Str = new Integer(tgl_sekarang.get(Calendar.YEAR)).toString() + bulan.toString();
						}
						
						Integer inttgl2 = new Integer(inttgl2Str);
						if (intIDCounter.longValue() == 0)
						{
							intIDCounter = new Long ((long)(inttgl2)* 100000);
						}else{
							Integer inttgl1=new Integer(selectGetCounterMonthYear(130, "01"));
		
							if (inttgl1.intValue() != inttgl2.intValue())
							{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("00000")));
								//ganti dengan tahun skarang
								updateCounterMonthYear(inttgl2.toString(), 130, "01");
								//reset nilai counter dengan 0
								intID = 0;
								updateMstCounter2("0", 130, "01");
								//logger.info("update mst counter start di bulan dan tahun baru");
							} else{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("00000"))+intIDCounter);
							}
						}
						
						//--------------------------------------------
						Long intRegSpajPaBsm = new Long(intIDCounter.longValue() + 1);
						intID = intID + 1;
						updateMstCounter3(intID, 130, "01");
						String regSpajPaBsm = intRegSpajPaBsm.toString();
						pas.setReg_spaj(regSpajPaBsm);
					//====================
				}
				
				// Generate Fire Id jika PA BSM - Daru 10 April 2015
				if(pas.getMsp_fire_id() == null && "73".equals(pas.getProduct_code())){
					Long intIDCounter = (Long) this.bacDao.select_counter(113, "01");
						int intID = intIDCounter.intValue();
						Calendar tgl_sekarang = Calendar.getInstance(); 
						Integer inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
						if (intIDCounter.longValue() == 0)
						{
							intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 1000000));
						}else{
							Integer inttgl1=new Integer(selectGetCounterMonthYear(113, "01"));
		
							if (inttgl1.intValue() != inttgl2.intValue())
							{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
								//ganti dengan tahun skarang
								updateCounterMonthYear(inttgl2.toString(), 113, "01");
								//reset nilai counter dengan 0
								intID = 0;
								updateMstCounter2("0", 113, "01");
								//logger.info("update mst counter start di tahun baru");
							}else{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000"))+intIDCounter);
							}
						}
						
						//--------------------------------------------
						Long intFireId = new Long(intIDCounter.longValue() + 1);
						intID = intID + 1;
						updateMstCounter3(intID, 113, "01");
						String mspFireId = intFireId.toString();
						pas.setMsp_fire_id(mspFireId);
					//====================
				}
				
				// Generate Fire Id jika PA BSM - Daru 10 April 2015
				if(pas.getMsp_fire_id() == null && "205".equals(pas.getProduct_code())){
					Long intIDCounter = (Long) this.bacDao.select_counter(237, "01");
						int intID = intIDCounter.intValue();
						Calendar tgl_sekarang = Calendar.getInstance(); 
						Integer inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
						if (intIDCounter.longValue() == 0)
						{
							intIDCounter = new Long ((long)((tgl_sekarang.get(Calendar.YEAR))* 1000000));
						}else{
							
							String counterMonthYear = selectGetCounterMonthYear(237, "01");
							Integer inttgl1=0;
							if(counterMonthYear != null){
								if(!counterMonthYear.trim().equals("")){
									inttgl1 = new Integer(counterMonthYear);
								}
							}
							
							if (inttgl1.intValue() != inttgl2.intValue())
							{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
								//ganti dengan tahun skarang
								updateCounterMonthYear(inttgl2.toString(), 237, "01");
								//reset nilai counter dengan 0
								intID = 0;
								updateMstCounter2("0", 237, "01");
								//logger.info("update mst counter start di tahun baru");
							}else{
								intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000"))+intIDCounter);
							}
						}
						
						//--------------------------------------------
						Long intFireId = new Long(intIDCounter.longValue() + 1);
						intID = intID + 1;
						updateMstCounter3(intID, 237, "01");
						String mspFireId = intFireId.toString();
						pas.setMsp_fire_id(mspFireId);
					//====================
				}
				
				pas.setLspd_id(1);
				pas.setLssp_id(10);
				pas.setDist("05");
				pas.setMsp_kode_sts_sms("00");
				pas.setMsp_pas_create_date(commonDao.selectSysdate());
				
				// Set lcb_no & lcb_reff pada pa bsm
				if("73".equals(pas.getProduct_code())) {
					pas.setLcb_no(selectLcbNo(pas.getLrb_id()));
					pas.setLcb_reff(selectLcbNo(pas.getReff_id()));
				}
				
				if("205".equals(pas.getProduct_code())) {
					pas.setLcb_no(selectLcbNo(pas.getLrb_id()));
					pas.setLcb_reff(selectLcbNo(pas.getReff_id()));
				}
				
				insertPas(pas);
				insertMstPositionSpajPas(pas.getLus_id().toString(), "NEW ENTRY DATA", pas.getMsp_fire_id(), 5);
				
				if("73".equals(pas.getProduct_code()) && pas.getNo_kartu() != null && !pas.getNo_kartu().trim().equals("")) {
					updateKartuPas1(pas.getNo_kartu(), 1, pas.getReg_spaj(), null);
				}
				
				//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				//TRANSFER===================================
				if(!("73".equals(pas.getProduct_code()) ||  "205".equals(pas.getProduct_code())))
					pas = transferCplan(pas, user);//request, pas, errors,"input",user,errors);
				//============================================
		}catch(Exception e){
			
			logger.info(e);
			pas.setStatus(1);
			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		return pas;
	}

	private Pas transferCplan(Pas pas, User user) {
		pas.setStatus(0);
		try{
			Date sysdate = commonDao.selectSysdate();
			int produk = pas.getProduk();
			Double up = new Double(pas.getMsp_up());
//			double rate = 0.00;
//			if(produk == 1){
//				rate = 0.65;
//			}else if(produk == 2){
//				rate = 1.45;
//			}else if(produk == 3){
//				rate = 3.05;
//			}
//			Double premi = (up * rate) / new Double(1000);
			Double premi = new Double(pas.getMsp_premi());
			//================================
			Integer umurPp = 0;
			Integer umurTt = 0;
			try{
				f_hit_umur umr = new f_hit_umur();
				
				SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
				SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
				SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
				
				int tahun2 = Integer.parseInt(sdfYear.format(sysdate));
				int bulan2 = Integer.parseInt(sdfMonth.format(sysdate));
				int tanggal2 = Integer.parseInt(sdfDay.format(sysdate));
				
				if(pas.getMsp_pas_dob_pp() != null){
					
					int tahun1 = Integer.parseInt(sdfYear.format(pas.getMsp_pas_dob_pp()));
					int bulan1 = Integer.parseInt(sdfMonth.format(pas.getMsp_pas_dob_pp()));
					int tanggal1 = Integer.parseInt(sdfDay.format(pas.getMsp_pas_dob_pp()));
					
					umurPp=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
				}
				if(pas.getMsp_date_of_birth() != null){
					int tahun1 = Integer.parseInt(sdfYear.format(pas.getMsp_date_of_birth()));
					int bulan1 = Integer.parseInt(sdfMonth.format(pas.getMsp_date_of_birth()));
					int tanggal1 = Integer.parseInt(sdfDay.format(pas.getMsp_date_of_birth()));
					umurTt=umr.umur(tahun1, bulan1, tanggal1, tahun2, bulan2, tanggal2);
				}
			}catch(Exception e){
				
			}
			//==================================
			//generate id di cplan
			Long intIDCounter = (Long) this.bacDao.select_counter_eb(246, "01");
			int intID = Integer.parseInt(intIDCounter.toString().substring(4));
			Calendar tgl_sekarang = Calendar.getInstance(); 
			Integer inttgl2=new Integer(tgl_sekarang.get(Calendar.YEAR));
			String subIdYear = "25" + inttgl2.toString().substring(2);
			if (intID == 0)
			{
				intIDCounter =  new Long (Integer.parseInt(subIdYear) * new Long(1000000));
			}else{
				Long inttgl1=new Long(uwDao.selectGetCounterValueEb(246, "01"));
				String subtgl1 = inttgl1.toString().substring(0,4);

				if (!subIdYear.equals(subtgl1))
				{
					//intIDCounter=new Long(Long.parseLong(inttgl2.toString().concat("000000")));
					intIDCounter=new Long(Long.parseLong(subIdYear.concat("000000")));
					//ganti dengan tahun skarang
					uwDao.updateCounterValueEb(intIDCounter.toString(), 246, "01");
					//logger.info("update mst counter start di tahun baru");
				}else{
					intIDCounter=new Long(Long.parseLong(subIdYear.concat("000000"))+intID);
				}
			}
			
			//--------------------------------------------
			Long no_id = new Long(intIDCounter.longValue() + 1);
			intID = intID + 1;
			uwDao.updateCounterValueEb(no_id.toString(), 246, "01");
			String noId = no_id.toString();
			//==========================
			//=========================
			//pas.setLus_id(Integer.parseInt(user.getLus_id()));
			//================================================
			//CPLAN
			Cplan cplan = new Cplan();
			cplan.setNo_id(noId);
			cplan.setJenis_cp(25);//pa bsm
			cplan.setNo_rek(pas.getMsp_no_rekening());
			if(cplan.getNo_rek() == null || cplan.getNo_rek().equals(""))cplan.setNo_rek("0");
			//cplan.setKanwill_id("");	
			cplan.setLsbp_id(156);
			cplan.setFull_name(pas.getMsp_pas_nama_pp());
			cplan.setLku_id("01");
			//cplan.setSet_bulanan(null);
			//cplan.setSet_cplan(null);
			cplan.setTotal_premi(new Long(premi.toString().replace(".0", "")));
			cplan.setLump_sum(new Long(pas.getMsp_up().toString().replace(".0", "")));
			cplan.setMcp_insper(1);// 1 tahun
			//cplan.setMcp_sex(totalPremi);
			cplan.setMcp_umur(umurPp);
			cplan.setMcp_tgl_lahir(pas.getMsp_pas_dob_pp());
			cplan.setMcp_tgl_lahir_pp(pas.getMsp_pas_dob_pp());
			cplan.setMcp_beg_date(pas.getMsp_pas_beg_date());
			cplan.setMcp_end_date(pas.getMsp_pas_end_date());
			cplan.setLssp_id(10);
			cplan.setMcp_sts_aksep(5);
			cplan.setMcp_tgl_aksep(sysdate);
			//cplan.setMcp_next_bill(null);// ga ada nextbill
			cplan.setLspd_id(99);
			cplan.setMsag_id(pas.getMsag_id());
			//cplan.setMcp_flag_sim(null);
			//cplan.setLc_id(null);
			//cplan.setMcp_flag_bill(null);
			cplan.setLscb_id(pas.getLscb_id());
			cplan.setNo_sertifikat(sertifikatPaCplan(cplan));
			cplan.setAddress1(pas.getMsp_address_1());
			cplan.setCity(pas.getMsp_city());
			cplan.setPostal_code(pas.getMsp_postal_code());
			cplan.setReg_spaj(pas.getReg_spaj());
			cplan.setEmail(pas.getMsp_pas_email());
			uwDao.insertCplan(cplan);
			//=================================================
			//CPLAN_DET
			CplanDet cplanDet = new CplanDet();
			cplanDet.setNo_id(noId);
			cplanDet.setUrut(1);
			cplanDet.setNama_peserta(pas.getMsp_full_name());
			//cplanDet.setSex();
			cplanDet.setTmp_lahir_peserta(pas.getMsp_pas_tmp_lhr_tt());
			cplanDet.setBod_peserta(pas.getMsp_date_of_birth());
			cplanDet.setUmur(umurTt);
			//cplanDet.setRelasi(relasi);
			cplanDet.setProduct_code("046");
			cplanDet.setPlan(pas.getProduk());
			cplanDet.setBeg_aktif(pas.getMsp_pas_beg_date());
			cplanDet.setEnd_aktif(pas.getMsp_pas_end_date());
			cplanDet.setBeg_date(pas.getMsp_pas_beg_date());
			cplanDet.setFlag_aksep(5);
			cplanDet.setFlag_aktif(1);
			cplanDet.setFlag_batal(0);
			cplanDet.setUser_aksep(Integer.parseInt(user.getLus_id()));
			cplanDet.setPremi(Integer.parseInt(premi.toString().replace(".0", "")));
			cplanDet.setTgl_aksep(sysdate);
			uwDao.insertCplanDet(cplanDet);
			
			//GENERATE POLIS
			PrintPolisPaBsm printPolis = new PrintPolisPaBsm();
			File userDir = new File(props.getProperty("pdf.dir.export") + "\\cplan\\25\\" + cplan.getNo_sertifikat());
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
			String outputName = props.getProperty("pdf.dir.export") + "\\cplan\\25\\" + cplan.getNo_sertifikat() + "\\" + cplan.getNo_sertifikat() + ".pdf";
			String hamid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
			Date nowDate = commonDao.selectSysdate();
			if(cplanDet.getPremi() == 0){
				printPolis.generatePaBsm_free(outputName, cplan, cplanDet, nowDate, hamid);
			}else{
				printPolis.generatePaBsm(outputName, cplan, cplanDet, nowDate, hamid);
			}
			//=================================================
			//transfer email otomatis
			Boolean EmailOtomatis = uwDao.emailSoftcopyPaBsm(cplan, user);
			//==================================================
			uwDao.insertMstPositionSpajPas(pas.getLus_id().toString(), "TRANSFER DATA", pas.getMsp_fire_id(), 5);
			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}catch(Exception e){
			
			logger.info(e);
			pas.setStatus(2);//transfer gagal
			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		
		return pas;
		
	}

	private String sertifikatPaCplan(Cplan cplan) {		
			String id="";
		
			HashMap tm=(HashMap) selectTMProduct("046");
			//querySingle("selectTMProduct", tmms.getProd().getProduct_code());
			BigDecimal nomor=(BigDecimal) tm.get("NOMOR");
			BigDecimal nomor2=(BigDecimal) tm.get("NOMOR2");
			String no_polis=(String) tm.get("NO_POLIS");
			
			nomor=nomor.add(new BigDecimal(1));
			id=no_polis+"-"+FormatString.rpad("0", nomor.toString(), 6);
			Map param=new HashMap<String, Object>();
			param.put("nomor", nomor);
			param.put("nomor2", nomor2);
			param.put("product_code", "046");
			updateTmProduct(param);
			//update("updateTmProduct", param);
		
			return id;
	}
	
	public List<Map> selectSpajProses() throws DataAccessException {
		return query("selectSpajProses", null);
	}
	
	public Integer selectHasMerchantFee(String reg_spaj, Integer tahun_ke, Integer premi_ke) throws DataAccessException {
		HashMap map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("tahun_ke", tahun_ke);
		map.put("premi_ke", premi_ke);
		return (Integer) querySingle("selectHasMerchantFee", map);
	}
	
	public List<AutoPaymentVA> selectListPaymentVA() throws DataAccessException {
		return query("selectListPaymentVA", null);
	}
	
	public ArrayList<HashMap<String, Object>> selectDetailPermintaanVa(String no_va, String begDate, String endDate) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("no_va", no_va);
		params.put("begDate", begDate);
		params.put("endDate", endDate);
		
		return (ArrayList<HashMap<String, Object>>) query("selectDetailPermintaanVa", params);
	}
	
	/*public Map selectDataFromDrekCC(String reg_spaj , String mar_jenis) {
		HashMap map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("mar_jenis", mar_jenis);
		return (Map) querySingle("selectDataFromDrekCC", map);
	} 
	
	public List selectDataLQG2(Integer type) {
		Map map = new HashMap();
		map.put("type", type);
		return query("selectLQG2", map);
	}
	
	public List selectDataLQL2(Integer type) {
		Map map = new HashMap();
		map.put("type", type);
		return query("selectLQL2", map);
	}*/ 
	
	// Rahmayanti - validasi welcome call
	public Integer selectSPBTTD(String spaj){
		Policy policy = this.selectMstPolicyAll(spaj);
		String cabang = policy.getLca_id();
		Integer lspd_id_polis = policy.getLspd_id();
		String path = props.getProperty("pdf.dir.export").trim() + "\\" + 
				cabang.trim() + "\\" + 
				spaj.trim() + "\\" + 
				spaj.trim() + "SPBTTD 001.pdf";
		File file = new File(path);
		Integer flag_validasi = 0, selectFlagValidasi= this.selectFlagValidasi(spaj);
		if(lspd_id_polis==27&&file.exists()){
			if(selectFlagValidasi!=null) flag_validasi = selectFlagValidasi;
			else flag_validasi = 1;
		}
		return flag_validasi;
	}
	
	public Integer selectFlagValidasi(String spaj){
		return (Integer) querySingle("selectFlagValidasi", spaj);
	}
	
	// Ridhaal - cek status further Colection (lssa_id_bas = 18 - Further Collection/ BSB)
	public Integer selectStatusFurtherColection(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectStatusFurtherColection", reg_spaj);
	}
	
	public Double selectTotalPremiUlink(String reg_spaj){
		return (Double) querySingle("selectTotalPremiUlink", reg_spaj);
	}
	
	//Ridhaal - untuk mendapatkan email BC dan AM / agen penutup dan leader penutup
	public Map selectEmailBCAM(String spaj){
		return(HashMap) querySingle("selectEmailBCAM", spaj);
	}
	
	public List<Map> selectUsiaTT (String reg_spaj, Integer umur1 , Integer umur2){
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("umur1", umur1);
		map.put("umur2", umur2);
		
		return query("selectUsiaTT", map);
	}
	
	public List<Map> selectReminderSmileBaby() throws DataAccessException {
		return query("selectReminderSmileBaby", null);
	}
	
	public List  selectPlanProvestara(String spaj, int lsbs_id) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lsbs_id", new Integer(lsbs_id));		
		return query("selectPlanProvestara", params);
	}
	
	public List<Map> selectInforceToWelcomeCall() throws DataAccessException {
		return query("selectInforceToWelcomeCall", null);
	}
	
	public List<Map> selectInforceToWelcomeCallBankAs() throws DataAccessException {
		return query("selectInforceToWelcomeCallBankAs", null);
	}
	
	public List<Map> selectInforceToWelcomeCallCorona() throws DataAccessException {
		return query("selectInforceToWelcomeCallCorona", null);
	}
	
	public List<Map> selectDuplicateSnows() throws DataAccessException {
		return query("selectDuplicateSnows", null);
	}
	
	public List<Map> selectCountMstInboxSnows(String spaj) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		return query("selectCountMstInboxSnows", params);
	}
	
	public List<Map> selectListSnowsDuplicate(String spaj,int ljj_id,int lspd_id,int lspd_id_from,int mi_pos) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lspd_id", lspd_id);
		params.put("lspd_id_from", lspd_id_from);
		params.put("ljj_id", ljj_id);
		params.put("mi_pos", mi_pos);
		return query("selectListSnowsDuplicate", params);
	}
	
	public List<DropDown> selectDropDownFU(String spaj, String lsfuId) throws DataAccessException{
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("lsfuId", lsfuId);		
		return query("selectDropDownFU", params);
	}
	
	public List<Map> selectProductionBSIMInBranch(Map params) throws DataAccessException{
		return query("selectProductionBSIMInBranch", params);
	}
	
	public int cekTotalSpajLockUserCollection(int lus_id) throws DataAccessException{
		return (Integer) querySingle("cekTotalSpajLockUserCollection", lus_id);
	}
	
	public Map selectEmailBCAM2(String spaj){
		return (Map) querySingle("selectEmailBCAM2", spaj);
	}
	
	public List<AutoPaymentVA> selectListPaymentBSM() throws DataAccessException {
		return query("selectListPaymentBSM", null);
	}
	
	public BigDecimal selectMstSpajFree(String reg_spaj) throws DataAccessException{
		return (BigDecimal) querySingle("selectMstSpajFree", reg_spaj);
	}
	
	public Date selectSysdateByInboxHist(String mi_id) throws DataAccessException {
		return (Date) querySingle("selectSysdateByInboxHist", null);
	}
	
	public void insertMstRefferal(String reg_spaj) throws DataAccessException {
		insert("insertMstRefferal", reg_spaj);
	}
	
	public String selectSeqNoEndors(){
		return (String) querySingle("selectSeqNoEndors", null);
	}
	
	public void closeConn(Connection conn){
        this.closeConnection(conn);
	}
	
	public List<AutoPaymentVA> selectListPaymentBancass() throws DataAccessException {
		return query("selectListPaymentBancass", null);
	}
	
	public HashMap selectMstUlink(String reg_spaj){ //Chandra A - 20180521: batching simas prime link (rider save)
		return (HashMap)querySingle("selectMstUlink", reg_spaj);
	}
	
	public HashMap selectMstBatchSwitching() { //Chandra A - 20180521: batching simas prime link (rider save)
		return (HashMap)querySingle("selectMstBatchSwitching", null);
	}
	
	public void UpdateMstPolicyNoBatch(String no_batch, String reg_spaj){ //Chandra A - 20180521: batching simas prime link (rider save)
		Map param = new HashMap();
		param.put("NO_BATCH", no_batch);
		param.put("REG_SPAJ", reg_spaj);
		
		insert("UpdateMstPolicyNoBatch", param);		
	}
	
	public HashMap selectMstProductInsured(String reg_spaj){ //Chandra A - 20180702: batching simas prime link (rider save)
		return (HashMap)querySingle("selectMstProductInsured", reg_spaj);
	}

	//Mark Valentino 30-08-2018 MST_EKSTERNAL_PRINT
	public void insertMst_eksternal_print(String spaj, String path, String lcaId, String lusId) throws DataAccessException {
		Map params = new HashMap();
		params.put("reg_spaj", spaj);
		params.put("path", path);
		params.put("lcaId", lcaId);
		params.put("lusId", lusId);
		
		insert("insert.mst_eksternal_print", params);
	}

	public void updateFlagKonfirmasiMstEksternal(String spaj) throws DataAccessException {
		update("updateFlagKonfirmasiMstEksternal", spaj);
	}	
	
	public List<Map> selectPendingPrintPolis(){
		HashMap result;
//		HashMap param = new HashMap();
//		param.put("tglAwal", tglAwal);
//		param.put("tglKemarin", tglKemarin);
//		return (HashMap) querySingle("selectPendingPrintPolis", null);
//		return (List<Map>) query("selectPendingPrintPolisDmtm", null);
//		return (List<Map>) query("selectPendingPrintPolisCombine", null);
//		return (List<Map>) query("selectPendingPrintPolisMuat", null); 	
		return (List<Map>) query("selectPendingPrintPolis2", null);
	}
	
	public String selectCekStockSimCardRds(Integer flag_active, String keterangan){
		Map map = new HashMap();
		map.put("flag_active", flag_active);
		map.put("keterangan", keterangan);
		return (String) querySingle("selectCekStockSimCardRds", map);
	}
	
	public String selectAmbilSatuNoKartuSimascard(String keterangan){
		Map map = new HashMap();
		map.put("keterangan", keterangan);
		return (String) querySingle("selectAmbilSatuNoKartuSimascard", map);
	}
	
	public void deleteMstPositionSpajGO(String spaj, String msps_desc) throws DataAccessException {
		Map param = new HashMap();
		param.put("spaj", spaj);
		param.put("msps_desc", msps_desc);
		delete("delete.MstPositionSpajGO", param);
	}
	
	public void updateMstTransHistory(String reg_spaj, String kolom_tgl, Date tgl, String kolom_user, String lus_id){
		//if(tgl == null) tgl = commonDao.selectSysdate();
		
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("kolom_tgl", kolom_tgl);
		map.put("tgl", null);
		map.put("kolom_user", kolom_user);
		map.put("lus_id", lus_id);
		
		Integer exist = (Integer) querySingle("selectMstTransHist",reg_spaj);
		
		if(exist > 0){
			update("updateMstTransHistory", map);		
		}
	}
	
	public String selectMspIdFromSpaj(String spaj){
		return (String) querySingle("selectMspIdFromSpaj",spaj);
	}	
	
	public HashMap selectMstPasSmsFromNoKartu(String no_kartu) throws DataAccessException{		
		return (HashMap) querySingle("selectMstPasSmsFromNoKartu", no_kartu);		
	}
	
	public List selectSIPNT(Double nt1, Double nt2, Double nt3){ //helpdesk [133346] produk baru 142-13 Smart Investment Protection
		Map m = new HashMap();
		m.put("nt1", nt1);
		m.put("nt2", nt2);
		m.put("nt3", nt3);
		return query("selectSIPNT", m);
	}
	
	
	/* added by nana 20191127 */
	public String selectNoSenderSpaj(String spaj) throws DataAccessException {
		return (String) querySingle("selectNoSenderSpaj", spaj);
	} 
	
	/* added by nana 20191127 */
	public String selectWsId() throws DataAccessException {
		return (String) querySingle("selectWsId",null);
	} 

	//Added By Nana 20191202 insertLstHistActvWsOut
	public void insertLstHistActvWsOut(String wsid, Integer clientId, Integer jenisId,
			String processApi, String processDesc,String httpStatus, 
			String result, Integer method) throws DataAccessException {
		
		Map params = new HashMap();	
		params.put("ID", wsid);	
		params.put("CLIENT_ID", clientId);
		params.put("JENIS_ID", jenisId);
		params.put("PROCESS_API", processApi); 
		params.put("PROCESS_DESC", processDesc);
		params.put("HTTP_STATUS", httpStatus);
		params.put("RESULT", result);
		params.put("METHOD", method);
		params.put("INPUT_DATE", new Date());
		
		insert("insert.insertLstHistActvWsOut", params);
	}
	
	//helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
	public List<Nilai> selectLstTableNewForSimasLegacyPlan(Map map) throws DataAccessException{
		return query("selectLstTableNewForSimasLegacyPlan", map);
	}
	//iga 02/04/2020 pending print siap2u
	public List<Map> selectPendingPrintSIAP2U(String tgl){
		return (List<Map>) query("selectPendingPrintSIAP2U", tgl);
	}
	
	public String selectTotalListSPAJ(String nopol, String spaj, String noRow, String sertifikat, String possible, String matched){
		
		Map params=new HashMap();
		params.put("nopol",nopol);
		params.put("spaj", spaj);
		params.put("noRow", noRow);
		params.put("sertifikat", sertifikat);
		params.put("possible", possible);
		params.put("matched", matched);
		
		return (String) querySingle("selectTotalListSPAJ",params);
	}
	
	public Map selectExtraMortalitaNew(String reg_spaj) throws DataAccessException {
		return (Map) querySingle("selectExtraMortalitaNew", reg_spaj);
	}
	
	//select spaj date
	public Date selectSpajDate(String reg_spaj) throws DataAccessException {
		return (Date) querySingle("selectSpajDate", reg_spaj);
	}
	
	// SPAJ DATE EM
	public Date selectSpajDateRateMortalitaFromConfig() throws DataAccessException{
		return (Date) querySingle("selectSpajDateRateMortalitaFromConfig", null);
	}
}