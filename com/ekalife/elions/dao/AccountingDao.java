package com.ekalife.elions.dao;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.omg.CORBA.NO_IMPLEMENT;
import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.BVoucher;
import com.ekalife.elions.model.DBank;
import com.ekalife.elions.model.RekEkalife;
import com.ekalife.elions.model.TBank;
import com.ekalife.utils.parent.ParentDao;

public class AccountingDao extends ParentDao{
	
	protected void initDao() throws DataAccessException{
		this.statementNameSpace ="elions.accounting.";
	}

	public List selectallMstTBank (Integer position){
		return query("selectAllMstTbank", position);
	}
	
	public List selectMstTBankByCode (String nomor){
		return query("selectMstTBankByCode", nomor);
	}
	
	public String selectJenisKas (String nomor) throws DataAccessException{
		return (String) querySingle("selectJenisKas",nomor);
	}
	
	public List<DBank> selectMstDBank (String nomor){
		return query("selectMstDBank", nomor);
	}
	
	public List<DropDown> selectKodeCashFlow(){
		return query("selectKodeCashFlow", null);
	}
	
	public List<DropDown> selectLstBank2(){
		return query("selectLstBank2", null);
	}
	
	public List<DropDown> selectLstBank(){
		return query("selectLstBank", null);
	}
	
	public String selectRekEkalife(String lsrek_gl_no){
		return (String) querySingle("selectRekEkalife", lsrek_gl_no);
	}
	
	public List selectVoucher(Integer rek_id){
		return (List) query("selectVoucher", rek_id);
	}
	
	public String selectNoVoucherCr(String ls_simbol) throws DataAccessException{
		return (String) querySingle("selectNoVoucherCr", ls_simbol);
	}
	
	public String selectNoVoucherCd(String ls_simbol) throws DataAccessException{
		return (String) querySingle("selectNoVoucherCd", ls_simbol);
	}
	
	public Long selectNewCrNo(String ls_simbol) throws DataAccessException{
		return (Long) querySingle("selectNewCrNo", ls_simbol);
	}
	
	public Long selectNewCdNo(String ls_simbol) throws DataAccessException{
		return (Long) querySingle("selectNewCdNo", ls_simbol);
	}	
	
	public Date selectMstDefault()throws DataAccessException {
		return (Date) querySingle("selectMstDefault", null);
	}
	
	public Date selectMstDefault2()throws DataAccessException {
		return (Date) querySingle("selectMstDefault2", null);
	}
	
	public String selectMstTBankVoucher(String no_voucher,String ls_thn){
		Map map = new HashMap();
		map.put("no_voucher",no_voucher);
		map.put("ls_thn", ls_thn);
		return (String) querySingle("selectMstTBankVoucher", map);
	}
	
	public List<DBank> selectMstDBankTrans(String nomor){
		return query("selectMstDBankTrans", nomor);
	}
	
	public void updateRekEkalifeCD(int Lsrek_no_cd, String rek_id)throws DataAccessException{
		Map map = new HashMap();
		map.put("Lsrek_no_cd",Lsrek_no_cd);
		map.put("rek_id", rek_id);
		update("updateRekEkalifeCD", map);
	}
	
	public void updateRekEkalifeCR(int Lsrek_no_cr, String rek_id)throws DataAccessException{
		Map map = new HashMap();
		map.put("Lsrek_no_cr",Lsrek_no_cr);
		map.put("rek_id", rek_id);
		update("updateRekEkalifeCR", map);
	}
	
	public void updateMstDBank(DBank dbank)throws DataAccessException{
		update("updateMstDBank",dbank);
	}
	
	public void updatePreGantung(TBank tbank)throws DataAccessException{
		update("updatePreGantung",tbank);
	}
	
	public void updateMstTBank(TBank tbank)throws DataAccessException{
		update("updateMstTBank",tbank);
	}
	
	public void updateMstTBankBook(TBank tbank)throws DataAccessException{
		update("updateMstTBankBook",tbank);
	}
	
	public void updateMstTBankPosition(String nomor)throws DataAccessException{
		update("updateMstTBankPosition", nomor);
	}
	
	public void updateMstDBankRow(String nomor,int no_jurnalafter,int no_jurnalbefore)throws DataAccessException{
		Map map=new HashMap();
		map.put("nomor", nomor);
		map.put("no_jurnalafter", no_jurnalafter);
		map.put("no_jurnalbefore", no_jurnalbefore);
		update("updateMstDBankRow", map);
	}
	
	public void insertMstDBank(DBank dbank)throws DataAccessException{
		insert("insertMstDBank", dbank);
	}
	
	public void insertMstTBank(TBank tbank) throws DataAccessException{
		insert("insertMstTBank", tbank);
	}
	
	public void insertMstBVoucher(BVoucher bvoucher)throws DataAccessException{
		insert("insertMstBVoucher", bvoucher);
	}
	
	public void deleteMstDBank(String nomor, int no_jurnal) throws DataAccessException{
		Map map=new HashMap();
		map.put("nomor", nomor);
		map.put("no_jurnal", no_jurnal);
		delete("deleteMstDBank", map);
	}
	
	public String selectRekEkalifeByRekAJS(String rek_ajs){
		return (String) querySingle("selectRekEkalifeByRekAJS", rek_ajs);
	}
}