package com.ekalife.elions.dao;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.MstInbox;
import com.ekalife.elions.model.MstInboxChecklist;
import com.ekalife.elions.model.MstInboxDet;
import com.ekalife.elions.model.MstInboxHist;
import com.ekalife.elions.model.Stamp;
import com.ekalife.elions.model.WelcomeCallStatus;
import com.ekalife.utils.parent.ParentDao;

/**
 * Proses2 yang berhubungan dengan Program Snows
 * @author alfian_h
 * @since 03/06/2015
 *
 */
@SuppressWarnings("unchecked")
public class SnowsDao extends ParentDao {

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.snows.";
	}
	
	public List<DropDown> selectLstJnJob() throws DataAccessException{
		Map m = new HashMap();
		return query("selectLstJnJob", m);
	}
	
	public List selectMstPolicy(String polis, String spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("polis", polis);
		m.put("spaj", spaj);
		return query("selectMstPolicy",m);
	}
	
	public List selectMstInbox(String mi_id, String polis, Integer lus_id, String lde_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("mi_id", mi_id);
		m.put("polis", polis);
		m.put("lus_id", lus_id);
		m.put("lde_id", lde_id);
		return query("selectMstInbox", m);
	}
	
	public List selectMstInboxChecklist(String mi_id, String mi_flag) throws DataAccessException{
		Map m = new HashMap();
		m.put("mi_id", mi_id);
		m.put("mi_flag", mi_flag);
		return query("selectMstInboxChecklist", m);
	}
	
	public String selectCheckPending(String spaj, String polis, String lstb_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", spaj);
		m.put("nopol", polis);
		m.put("lstb_id", lstb_id);
		return (String) querySingle("selectCheckPending", m);
	}
	
	public List selectNmFileChecklist(String mi_id, String lc_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("mi_id", mi_id);
		m.put("lc_id", lc_id);
		return query("selectNmFileChecklist",m);
	}
	
	public String selectCountChecklist(String mi_id, String mi_flag) throws DataAccessException{
		Map m = new HashMap();
		m.put("mi_id", mi_id);
		m.put("mi_flag", mi_flag);
		return (String) querySingle("selectCountChecklist", m);
	}
	
	public List selectListLeft(Integer lus_id, String lde_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("lus_id", lus_id);
		m.put("lde_id", lde_id);
		return query("selectListLeft",m);
	}
	
	public List selectMstTmms(String polis, String spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("polis", polis);
		m.put("spaj", spaj);
		return query("selectMstTmms",m);
	}
	
	public List selectLstChecklist(String ljj_id, String mi_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("ljj_id", ljj_id);
		m.put("mi_id", mi_id);
		return query("selectLstChecklist",m);
	}
	
	public List<DropDown> selectLstDocumentPosition()throws DataAccessException{
		return query("selectLstDocumentPosition",null);
	}
	
	public void insertMstInbox( MstInbox mstInbox ){
    	insert("insertMstInbox", mstInbox);
    }
    
    public void insertMstInboxHist( MstInboxHist mstInboxHist ){
    	String mi_id = mstInboxHist.getMi_id();
    	mstInboxHist.setCreate_date(selectSysdateByInboxHist(mi_id));
    	insert("insertMstInboxHist", mstInboxHist);
    }
    
    
    public void insertWelcomeCallStatus( WelcomeCallStatus welcomecallStatus ){
    	welcomecallStatus.setCreated_date(selectSysdateByInboxHist(null));
    	insert("insertWelcomeCallStatus", welcomecallStatus);
    }
    
    public void updateWelcomeCallStatus( WelcomeCallStatus welcomecallStatus ){
    	update("updateWelcomeCallStatus", welcomecallStatus);
    }
    
    
    
    public void insertMstInboxChecklist( MstInboxChecklist mstInboxChecklist ){
    	insert("insertMstInboxChecklist", mstInboxChecklist);
    }
    
    public String selectGenMIID() throws DataAccessException{
    	return (String) querySingle("selectGenMIID", null);
    }
    
    public void updateMstInbox(MstInbox mstInbox){
    	update("updateMstInbox", mstInbox);
    }
    
    public void updateMstInboxChecklist(MstInboxChecklist mstInbox){
    	update("updateMstInboxChecklist", mstInbox);
    }
    
    public List selectWithdrawStableLink(String spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", spaj);
		return query("selectWithdrawStableLink",m);
	}
    
    public List selectWithdrawUnitLink(String spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", spaj);
		return query("selectWithdrawUnitLink",m);
	}
    
    public List selectselectSwitchingUnitLink(String spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", spaj);
		return query("selectSwitchingUnitLink",m);
	}
    
    public List selectReturSLink(String spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", spaj);
		return query("selectReturSLink",m);
	}
    
    public List selectEndorsmentRollover(String spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", spaj);
		return query("selectEndorsmentRollover",m);
	}
    
    public String selectJmlhTahapan(String spaj, String ljj_id) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
    	return (String) querySingle("selectJmlhTahapan", m);
    }
    
    public List selectJtTempoTahapan(String spaj, String ljj_id, String tggl) throws DataAccessException{
		Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
    	m.put("tggl", tggl);
		return query("selectJtTempoTahapan",m);
	}
    
    public List selectJtTempoTahapanMulti(String spaj, String ljj_id) throws DataAccessException{
		Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
		return query("selectJtTempoTahapanMulti",m);
	}
    
    public List selectJtTempoSimpananMulti(String spaj, String ljj_id) throws DataAccessException{
		Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
		return query("selectJtTempoSimpananMulti",m);
	}
    
    public List selectMutliInvest(String spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", spaj);
		return query("selectMutliInvest",m);
	} 
    
    public Date selectBegDateInsured(String spaj, String ljj_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
		return (Date) query("selectBegDateInsured",m);
	}
    
    public String selectTmmsTHP(String spaj, String ljj_id) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
    	return (String) querySingle("selectTmmsTHP", m);
    }
    
    public List selectJtTempoTmmsTHP(String spaj, String ljj_id) throws DataAccessException{
		Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
		return query("selectJtTempoTmmsTHP",m);
	}
    
    public List selectTmmsDet(String spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", spaj);
		return query("selectTmmsDet",m);
	} 
    
    public String selectJmlhSimpanan(String spaj, String ljj_id) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
    	return (String) querySingle("selectJmlhSimpanan", m);
    }
    
    public List selectWithdrawULSnows(String spaj) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("spaj", spaj);
    	return query("selectWithdrawULSnows", m);
    }
    
    public List selectWithdrawULSnowsMuKe(String spaj, String ulke) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("mu_ke", ulke);
    	return query("selectWithdrawULSnowsMuKe", m);
    }
    
    public List selectJtTempoSimpanan(String spaj, String ljj_id, String tggl) throws DataAccessException{
		Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
     	m.put("tggl", tggl);
		return query("selectJtTempoSimpanan",m);
	}
    
    public String selectJmlhMaturity(String spaj, String ljj_id) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
    	return (String) querySingle("selectJmlhMaturity", m);
    }
    
    public List selectJtTempoMaturity(String spaj, String ljj_id) throws DataAccessException{
		Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
		return query("selectJtTempoMaturity",m);
	}
    
    public String selectTmmsMaturity(String spaj, String ljj_id) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
    	return (String) querySingle("selectTmmsMaturity", m);
    }
    
    public List selectJtTempoTmmsMaturity(String spaj, String ljj_id) throws DataAccessException{
		Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
		return query("selectJtTempoTmmsMaturity",m);
	}
    
    public String selectJmlPencairanAllStableLink(String spaj) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("spaj", spaj);
    	return (String) querySingle("selectJmlPencairanAllStableLink", m);
    }
    
    public String selectJmlNilaiTunaiMRI(String spaj, String ljj_id) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
    	return (String) querySingle("selectJmlNilaiTunaiMRI", m);
    }
    
    public List selectJtTempoMstSurrender(String spaj, String ljj_id) throws DataAccessException{
		Map m = new HashMap();
    	m.put("spaj", spaj);
    	m.put("ljj_id", ljj_id);
		return query("selectJtTempoMstSurrender",m);
	}
    
	public void updateDataPicRekening(String lcb_no , String no_rekening, String nama_pic, String email_pic){
		Map map = new HashMap();
		map.put("lcb_no",lcb_no);
		map.put("no_rek_pic",no_rekening);
		map.put("nama_rek_pic",nama_pic);
		map.put("email_pic",email_pic);
		update("updateDataPicRekening",map);
	}
	
	public void insertMstStampFee( Stamp stamp ){
    	insert("insertMstStampFee", stamp);
    }
	
	public void insertMstStampDet( Stamp stamp){
    	insert("insertMstStampDet", stamp);
    }
	
	public List selectDataBmaterai(String periode , String periode2 ,String status, String jenis, String lcb_no, String filter){
		Map p = new HashMap();
		p.put("periode", periode);
		p.put("periode2", periode2);
		p.put("status", status);
		p.put("jenis", jenis);
		p.put("lcb_no", lcb_no);
		p.put("filter", filter);
		return query("selectDataBmaterai", p);
	}
	public List selectDataBmateraiDet(String no ,String lcb_no){
		Map p = new HashMap();
		p.put("no", no);
		p.put("lcb_no", lcb_no);
		return query("selectDataBmateraiDet", p);
	}
	
	public void updatePosisiMstSFee(String msf_no , String msf_posisi , String kolom, Date sysdate){
		Map map = new HashMap();
		map.put("msf_no",msf_no);
		map.put("msf_posisi",msf_posisi);
		map.put("kolom",kolom);
		map.put("sysdate",sysdate);
		update("updatePosisiMstSFee",map);
	}
	
    public List selectMstInboxHist(String mi_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("mi_id", mi_id);
		return query("selectMstInboxHist",m);
	}
	
	public String selectLcaIdAB(String noBerkas, String kota, Integer jnlcaid)  throws DataAccessException{
    	Map m = new HashMap();
    	m.put("noBerkas", noBerkas);
    	m.put("kota", kota);
    	m.put("jnlcaid", jnlcaid);
    	return (String) querySingle("selectLcaIdAB", m);
    }
	
	public List selectListAjuanBiaya(String lca_id, String lcaName, Integer detail, String mi_id)  throws DataAccessException{
		Map p = new HashMap();
		p.put("lca_id", lca_id);
		p.put("lcaName", lcaName);
		p.put("detail", detail);
		p.put("mi_id", mi_id);		
		return query("selectListAjuanBiaya", p);
	}
	
	public List selectDataNoBerkasDet(String mi_id)  throws DataAccessException{
		Map p = new HashMap();
		p.put("mi_id", mi_id);		
		return query("selectDataNoBerkasDet", p);
	}
	
	/*
	public String selectSysdateddmmyyy() throws DataAccessException {
		return (String) querySingle("selectSysdateddmmyyy", null);
	}
	*/
	
	public List selectHistoryBerkas(Integer jnsearch, String noBerkas, String cabang)  throws DataAccessException{
		Map p = new HashMap();
		p.put("jnsearch", jnsearch);
		p.put("noBerkas", noBerkas);
		p.put("cabang", cabang);
		return query("selectHistoryBerkas", p);
	}
	
	public List selectListInboxCheck(String noBerkas, Integer jnupl, Integer jnc)  throws DataAccessException{
		Map p = new HashMap();
		p.put("noBerkas", noBerkas);
		p.put("jnupl", jnupl);
		p.put("jnc", jnc);
		return query("selectListInboxCheck", p);
	}
	
	public List selectCekRolAB(String jenis, String tgl_pemakaian, String kota, Integer role, Integer jnUpd, String no_berkas)  throws DataAccessException{
		Map p = new HashMap();
		p.put("jenis", jenis);
		p.put("tgl_pemakaian", tgl_pemakaian);
		p.put("kota", kota);
		p.put("role", role);
		p.put("jnUpd", jnUpd);
		p.put("no_berkas", no_berkas);
		return query("selectCekRolAB", p);
	}
	
	public String selectKotaInboxDet(String noBerkas) throws DataAccessException {
		return (String) querySingle("selectKotaInboxDet", noBerkas);
	}
	
	public List selectAllLstCabangAB(String lca_id, Integer jnuselcaid) throws DataAccessException{
		Map p = new HashMap();
		p.put("lca_id", lca_id);
		p.put("jnuselcaid", jnuselcaid);
		return query("selectAllLstCabangAB", p);
	}
	
	public List selectLstScanAB(String NoBerkas) throws DataAccessException{
		Map p = new HashMap();
		p.put("NoBerkas", NoBerkas);
		return query("selectLstScanAB", p);
	}
	    
	public void insertMstInboxDet(MstInboxDet mstInboxDet){
    	insert("insertMstInboxDet", mstInboxDet);
    }
    
	public void deleteMstInboxDet(String mi_id){
    	delete("deleteMstInboxDet", mi_id);
    }
	
	
	public void deleteMstInboxHist(String mi_id){
    	delete("deleteMstInboxHist", mi_id);
    }

	public void deleteMstInbox(String mi_id){
    	delete("deleteMstInbox", mi_id);
    }
	
	
	public void updateTglPengirimanBerkas(String mi_id, String tgl_berkas_masuk){
		Map p = new HashMap();
		p.put("mi_id", mi_id);
		p.put("tgl_berkas_masuk", tgl_berkas_masuk);
    	update("updateTglPengirimanBerkas", p);
    }
	
	public void updateDataMstStampFee(String lcb_no , String no_rekening, String nama_pic){
		Map map = new HashMap();
		map.put("msf_lcb_no",lcb_no);
		map.put("msf_norek",no_rekening);
		map.put("msf_pic",nama_pic);
		update("updateDataMstStampFee",map);
	}

	public void insertMstCsfDial(
			String mscsf_ket, String reg_spaj, String mscsf_dial_ke, String flag_inout,
			String lus_id, String mscfl_no_ref, String flag_finance, String lscsf_jenis, String mscsf_ocr,
			String mscsf_dir, String mscsf_tujuan){
		Map m = new HashMap();
		m.put("mscsf_ket", mscsf_ket);
		m.put("reg_spaj", reg_spaj);
		m.put("mscsf_dial_ke", mscsf_dial_ke);
		m.put("flag_inout", flag_inout);
		m.put("lus_id", lus_id);
		m.put("mscfl_no_ref", mscfl_no_ref);
		m.put("flag_finance", flag_finance);
		m.put("lscsf_jenis", lscsf_jenis);
		m.put("mscsf_ocr", mscsf_ocr);
		m.put("mscsf_dir", mscsf_dir);
		m.put("mscsf_tujuan", mscsf_tujuan);
		insert("insertMstCsfDial", m);
	}
	
	public String selectMaxMscsfDialKe(String reg_spaj){
		Map m = new HashMap();
		m.put("spaj", reg_spaj);
		return (String) querySingle("selectMaxMscsfDialKe", m);
	}
	
	public String selectLstDocumentPositionByLSPDID(String lspd_id){
		Map m = new HashMap();
		m.put("lspd_id", lspd_id);
		return (String) querySingle("selectLstDocumentPositionByLSPDID", m);
	}
	
	public Date selectSysdateByInboxHist(String mi_id) throws DataAccessException {
		return (Date) querySingle("selectSysdateByInboxHist", null);
	}
	
}
