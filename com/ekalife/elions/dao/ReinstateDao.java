package com.ekalife.elions.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Edit;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentDao;

@SuppressWarnings("unchecked")
public class ReinstateDao extends ParentDao {

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.uw_reinstate.";
	}
	
	public void updateMstTransHistory(String reg_spaj, String kolom_tanggal, String user_tanggal, String lus_id) throws DataAccessException{
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("kolom_tanggal", kolom_tanggal);
		params.put("tanggal", new Date());
		params.put("user_tanggal", user_tanggal);
		params.put("lus_id", lus_id);
		params.put("jenis", 4);
		//params.put("trans_ke", 0);
		//params.put("trans_ke2", 0);
		int update = update("updateMstTransHistory", params);
	}
	
	public String saveMstTransHistory(String reg_spaj, String trans_no, String kolom_tanggal, String tanggal, String hh, String mm, String user_tanggal, String lus_id) throws DataAccessException{
		
		Date tgl = null;
		try {
			tgl = completeDateFormat.parse(tanggal + " " + hh + ":" + mm);
		} catch (ParseException e) {
			logger.error("ERROR :", e);
			return "Data tidak berhasil disimpan. Harap hubungi ITwebandmobile@sinarmasmsiglife.co.id";
		}
		
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		params.put("jenis", 4);
		params.put("trans_ke", 0);
		params.put("trans_ke2", 0);
		params.put("trans_no", trans_no);
		
		params.put("kolom_tanggal", kolom_tanggal);
		params.put("tanggal", tgl);
		params.put("user_tanggal", user_tanggal);
		params.put("lus_id", lus_id);
		int update = update("updateMstTransHistory", params);
		
		if(update == 0) {
			insert("insertMstTransHistory", params);
		}
		
		uwDao.insertMstPositionSpaj(lus_id, "UPDATE TANGGAL SPAJ DOC REINSTATE (" + completeDateFormat.format(tgl) + ")", reg_spaj, 0);
		
		return "Data berhasil disimpan.";
	}
	
	public List selectDaftarReinstate(int lspd_id1,int lspd_id2,String kata,String kategori) throws DataAccessException{
		Map  param=new HashMap();
		param.put("lspd_id1",new Integer(lspd_id1));
		param.put("lspd_id2",new Integer(lspd_id2));
		param.put("kata",kata);
		param.put("kategori",kategori);
		return query("selectDaftarReinstate",param);
	}
	
	public Integer selectCountMstReins(String spaj) throws DataAccessException{
		return (Integer)querySingle("count.mst_reins",spaj);
	}
	
	public Map selectDocumentPosition(String spaj) throws DataAccessException{
		return (HashMap)querySingle("lst_document_position",spaj);
	}
	
	public Date selectMsre_next_prm_date(String spaj) throws DataAccessException{
		return (Date)querySingle("mst_reins.msre_next_prm_date",spaj);
	}
	
	public Date selectDate() throws DataAccessException{
		return (Date)getSqlMapClientTemplate().queryForObject("elions.uw.select.date",null);
	}
	
	public void updateMstReins1(String spaj,int lspdId) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("lapse_date",null);
		param.put("lspd_id",new Integer(lspdId));
		update("update.mst_reins1",param);
	}

	public void updateMstReins2(String spaj,int lspdId,int insured) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("no_insured",new Integer(insured));
		param.put("lspd_id",new Integer(lspdId));
		update("update.mst_reins2",param);
	}
	
	public void updateMstUwReinstate(String spaj,String lusId,Integer lspdId,int liBln,String noReins) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("lspd_id",lspdId);
		param.put("lus_id",lusId);
		param.put("bln_lapse",new Integer(liBln));
		param.put("no_reins",noReins);
		update("update.mst_uw_reinstate",param);
	}
	
	public void updateMstUwReinstateLspdId(String spaj,String noReins,int lspdId) throws DataAccessException{
		Map param =new HashMap();
		param.put("spaj",spaj);
		param.put("lspd_id",new Integer(lspdId));
		param.put("no_reins",noReins);
		update("update.mst_uw_reinstate.lspd_id",param);
	}
	
	public List selectMstReinstateLspdId(String spaj) throws DataAccessException{
		return query("mst_reinstate.lspd_id",spaj);
	}
	
	public void deleteMstUwReinstate(String spaj) throws DataAccessException{
		getSqlMapClientTemplate().delete("elions.uw_reinstate.delete.mst_uw_reinstate",spaj);
	}
	
	public void updateMstPolicyLspdId(String spaj,int lspd_id) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("lspd_id",new Integer(lspd_id));
		update("update.mst_policy.lspd_id",param);
	}
	
	public void updateMstInsuredLspsId(String spaj,int lspd_id) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("lspd_id",new Integer(lspd_id));
		update("update.mst_insured.lspd_id",param);
	}
	
	public void updateMstReinstate(String spaj,String noReins,int lspdId) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("lspd_id",new Integer(lspdId));
		param.put("no_reins",noReins);
		update("update.mst_reinstate",param);
	}
	
	public Map selectMstUwReinstate(String spaj,String noReins) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("no_reins",noReins);
		return (HashMap)querySingle("mst_uw_reinstate",param);
	}
	
	/*
	public Date selectSysdate() throws DataAccessException{
		return (Date)querySingle("selecetDate",null);
	}
	*/
	
	public Integer selectCountMstReins(String spaj,int noInsured) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("no_insured",new Integer(noInsured));
		return (Integer)querySingle("countMstReinsRegSpaj",param);
		
	}
	
	public Edit selectSuratKonfirmasi(String spaj,String reinsNo) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("reins",reinsNo);
		return (Edit)querySingle("suratKonfirmasi",param);
	}

	public void updateMstUwReinstateEdit(Integer accept,String accept_note,Date tgl_accept,
			Integer kondisi_polis,String kondisi_note,Double tot_premi,Double tot_bunga,
			Date tgl_paid,String spaj,String reinsNo) throws DataAccessException{
		Map param=new HashMap();
		param.put("accept",accept);
		param.put("accept_note",accept_note);
		param.put("tgl_accept",tgl_accept);
		param.put("kondisi_polis",kondisi_polis);
		param.put("kondisi_note",kondisi_note);
		param.put("tot_premi",tot_premi);
		param.put("tot_bunga",tot_bunga);
		param.put("tgl_paid",tgl_paid);
		param.put("spaj",spaj);
		param.put("reinsNo",reinsNo);
		update("update.mst_uw_reinstate.accept",param);
	}

	public Integer selectMstUwReinstateMsurPrint(String spaj,String reins) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("reins",reins);
		return (Integer) querySingle("selecetMstUwReinstateMsurPrint",param);
	}

	public void updateMstUwReinstateMsurPrint(String spaj,String reins,String lusId,Integer msur_print) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("reins",reins);
		param.put("msur_print",msur_print);
		param.put("lus_id",lusId);
		update("update.mst_uw_reinstate.msur_print",param);
	}
	
	public void updateMstUwReinstate(String spaj,String reins,Integer msurPrint,Date msurPrintDate,String desc ) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("reins",reins);
		param.put("msur_print",msurPrint);
		param.put("msur_print_date",msurPrintDate);
		param.put("desc",desc);
		update("update.mst_uw_reinstate.reedit",param);
	}
	
	
	
	/*public void updateMstUwReinstateKondisiNote(String spaj,String kondisiNote) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("kondisi_note",kondisiNote);
		update("update.mst_uw_reinstate.kondisi_note",param);
	}*/
	
/*	public void updateMstUwReinstateAcceptNote(String spaj,String acceptNote) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("accept_note",acceptNote);
		update("update.mst_uw_reinstate.accept_note",param);
	}
	
	public void updateMstUwReinstateKondisiNotenPolis(String spaj,Integer kondisiPolis,String kondisiNote) throws DataAccessException{
		Map param=new HashMap();
		param.put("spaj",spaj);
		param.put("kondisi_polis",kondisiPolis);
		param.put("kondisi_note",kondisiNote);
		update("update.mst_uw_reinstate.kondisi_polis",param);
	}*/
	
	public List selectTopTenProduct(){
		return query("toptenproduct",null);
	}
	
	public List selectNonDeclarationProduct(String lsbsId){
		return query("nodeclarationproduk",lsbsId);
	}
	
	public int selectIsPolisUnitlink(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectIsPolisUnitlink", reg_spaj);
	}
	
	public Double selectBiayaMateraiTertunggak(String reinsNo)throws DataAccessException{
		return (Double)querySingle("selectBiayaMateraiTertunggak", reinsNo);
	}
	
	public Double selectBiayaMateraiFromBilling(String spaj)throws DataAccessException{
		return (Double)querySingle("selectBiayaMateraiFromBilling", spaj);
	}
	
	public Date selectmaxMsbiEndDateMstBilling(String spaj)throws DataAccessException{
		return (Date)querySingle("selectmaxMsbiEndDateMstBilling",spaj);
	}
	
	public String selectLstReinsUwNilai(Double up,Integer tahunKe,Integer umur,Integer lamaLapse,String lkuId)throws DataAccessException{
		Map param=new HashMap();
		param.put("up", up);
		param.put("tahun_ke", tahunKe);
		param.put("umur", umur);
		param.put("lama_lapse", lamaLapse);
		param.put("lku_id", lkuId);
		return (String)querySingle("selectLstReinsUwNilai", param);
	}
	
	public Integer selectLstMedicalRangeSar(Double sar,String lkuId)throws DataAccessException{
		Map param=new HashMap();
		param.put("sar", sar);
		param.put("lku_id", lkuId);
		return (Integer)querySingle("selectLstMedicalRangeSar", param);
	}

	public Integer selectLstMedicalRangeAge(Integer age)throws DataAccessException{
		Map param=new HashMap();
		param.put("age", age);
		return (Integer)querySingle("selectLstMedicalRangeAge", param);
	}
	
	public List<String> selectLstJenisMedical(Integer age,Integer sar,String lkuId)throws DataAccessException{
		Map param=new HashMap();
		param.put("age", age);
		param.put("sar", sar);
		param.put("lku_id", lkuId);
		return query("selectLstJenisMedical", param);
	}
	
	public String getPeriodeAkhirPremiPokokTertunggak(String reg_spaj) throws DataAccessException{
		Date tglReins = (Date) querySingle("selectTanggalReinstate", reg_spaj); 
		Date hasil = null;
		
		Map params = new HashMap();
		params.put("reg_spaj", reg_spaj);
		int pengurang = 0;
		
		do{
			params.put("pengurang", pengurang++);
			hasil = (Date) querySingle("selectPeriodeAkhirPremiPokokTertunggak2", params);
			if (hasil==null)hasil = (Date) querySingle("selectPeriodeAkhirPremiPokokTertunggak", params);
		}while(hasil.compareTo(tglReins) > 0);
		
		Date begdate = (Date) querySingle("selectPeriodeAwalPremiPokokTertunggak2", params);
		if (begdate==null)begdate = (Date) querySingle("selectPeriodeAwalPremiPokokTertunggak", params);
		return FormatDate.toIndonesian(begdate) + " s/d " + FormatDate.toIndonesian(hasil);
	}

	/*
	 * Canpri
	 */
	public Double selectHitungSAR(String spaj, Integer lsbs_id, Integer lsdbs_number, Integer lama_bayar,
			Integer lama_tanggung, Integer tahun_ke, Integer umur, Integer tipe)throws DataAccessException{
		Map map = new HashMap();
        map.put("spaj", spaj);
        map.put("lsbs", lsbs_id);
        map.put("lsdbs", lsdbs_number);
        map.put("lama_bayar", lama_bayar);
        map.put("lama_tanggung", lama_tanggung);
        map.put("tahun_ke", tahun_ke);
        map.put("umur", umur);
        map.put("tipe", tipe);
		
		return (Double)querySingle("selectHitungSAR", map);
	}
	
	public String selectMedisSimultan(String spaj, Integer mste_age, Datausulan du) {
		//select SAR
		Double sar = selectHitungSAR(spaj, du.getLsbs_id(), du.getLsdbs_number(), 1, 1, 1, mste_age, 2);
		
		//select range sar
		Integer range_sar = selectLstMedicalRangeSar(sar, du.getLku_id());
		
		//select range age
		Integer range_age = selectLstMedicalRangeAge(mste_age);
		
		//select jenis medis
		List<String> medis = selectLstJenisMedical(range_age, range_sar, du.getLku_id());
		String med = medis.get(0);
		
		return med;
	}
	
}
