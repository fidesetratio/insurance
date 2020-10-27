package com.ekalife.elions.dao;


import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.ekalife.utils.parent.ParentDao;

@SuppressWarnings("unchecked")
public class WorksiteDao extends ParentDao{
	
	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.worksite.";
	}
	public List select_nama_perusahaan() throws DataAccessException {
		return query("select_nama_perusahaan", null);
	}
	public List select_nama_perusahaan_by_mcl_id(String mcl_id) throws DataAccessException {
		Map m = new HashMap();
		m.put("mcl_id",mcl_id);
		return query("select_nama_perusahaan_by_mcl_id", m);
	}	
	
	public List select_nama_perusahaan_by_filter(String kode) throws DataAccessException {
		Map m = new HashMap();
		m.put("kode",kode);
		return query("select_nama_perusahaan_by_filter", m);
	}	
	
	public List selectCompanyWsList(String mcl_id, String jenis) throws DataAccessException {
		Map m = new HashMap();
		m.put("mcl_id",mcl_id);
		m.put("jenis", jenis);
		return query("selectCompanyWsList", m);
	}
	
	public List select_nama_perusahaan2() throws DataAccessException {
		return query("select_nama_perusahaan2", null);
	}	

	public List select_invoice(String id)  {
		Map m = new HashMap();
		m.put("id",id);
		return query("select_invoice",m);
	}
	
	public List select_invoice_lanjutan(String id)  {
		Map m = new HashMap();
		m.put("id",id);
		return query("select_invoice_lanjutan",m);
	}
	
	public List select_invoice_endow(String id, String tgl1, String tgl2)  {
		Map m = new HashMap();
		m.put("id",id);
		m.put("tgl1",tgl1);
		m.put("tgl2",tgl2);
		return query("select_invoice_endow",m);
	}	
	
	public List select_invoice_endow_perspaj(String id)  {
		Map m = new HashMap();
		m.put("id",id);
		return query("select_invoice_endow_perspaj",m);
	}
	
	public Map select_max_invoice(String id)  {
		Map m = new HashMap();
		m.put("id",id);
		return (HashMap) querySingle("select_max_invoice",m);
	}
	
	public Map select_max_invoice_lanjutan(String id)  {
		Map m = new HashMap();
		m.put("id",id);
		return (HashMap) querySingle("select_max_invoice_lanjutan",m);
	}

	public Map select_max_invoice_endow(String id, String tgl1, String tgl2)  {
		Map m = new HashMap();
		m.put("id",id);
		m.put("tgl1",tgl1);
		m.put("tgl2",tgl2);
		return (HashMap) querySingle("select_max_invoice_endow",m);
	}	
	
	public Map select_max_invoice_endow_perspaj(String id)  {
		Map m = new HashMap();
		m.put("id",id);
		return (HashMap) querySingle("select_max_invoice_endow_perspaj",m);
	}
	
	public Long select_counter_invoice(Map param)
	{
		return (Long) querySingle("select_counter_invoice",param);
	}
	
	public void update_mst_counter(Map param) throws DataAccessException {
		update("update_mst_counter", param);
	}
	
	public void update_mst_diskon_perusahaan(Map param) throws DataAccessException {
		update("update_mst_diskon_perusahaan", param);
	}
	
	public void update_counter_mst_insured(Map param) throws DataAccessException {
		update("update_counter_mst_insured", param);
	}
	
	public void update_nobilling(Map param) throws DataAccessException {
		update("update_nobilling", param);
	}	

	public Map select_billing(String spaj, Integer ke)  {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("ke",ke);
		return (HashMap) querySingle("select_billing",params);
	}
	
	public Map select_billing_endow_spaj(String spaj, Integer ke)  {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("ke",ke);
		return (HashMap) querySingle("select_billing_endow_spaj",params);
	}
	
	public List select_kwitansi_endow(String id, String tgl1, String tgl2)  {
		Map m = new HashMap();
		m.put("id",id);
		m.put("tgl1",tgl1);
		m.put("tgl2",tgl2);
		return query("select_kwitansi_endow",m);
	}		
	
	public List select_billing_endow(Integer ke, String kode, String bisnis, String tgl1, String tgl2)  {
		Map m = new HashMap();
		m.put("ke",ke);
		m.put("kode",kode);
		m.put("bisnis",bisnis);
		m.put("tgl1",tgl1);
		m.put("tgl2",tgl2);
		return query("select_billing_endow",m);
	}	
	
	public Map select_max_billing_endow(Integer ke, String kode, String bisnis, String tgl1, String tgl2)  {
		Map m = new HashMap();
		m.put("ke",ke);
		m.put("kode",kode);
		m.put("bisnis",bisnis);
		m.put("tgl1",tgl1);
		m.put("tgl2",tgl2);
		return (HashMap) querySingle("select_max_billing_endow",m);
	}	

    public List<Map<String, Object>> selectHistoryUT( Map<String, Object> params )
    {
        return ( List<Map<String, Object>> ) query( "selectHistoryUT", params );
	}
    
//    public List<Map<String, Object>> selectListSummaryKomisi( Map<String, Object> params )
//    {
//        return ( List<Map<String, Object>> ) query( "selectListSummaryKomisi", params );
//	}
    
	public List selectListSummaryKomisi(String periode, String bank, String transfer,String nama)  {
		Map m = new HashMap();
		m.put("periode",periode);
		m.put("bank",bank);
		m.put("transfer", transfer);
		m.put("nama", nama);
		return query("selectListSummaryKomisi",m);
	}
	
	public List<Map<String, String>> selectListSummaryKomisiForPrint(String periode, String bank)  {
		Map m = new HashMap();
		m.put("periode",periode);
		m.put("bank",bank);
		return query("selectListSummaryKomisiForPrint",m);
	}
	
	public List selectBankForJurnal()  {
		Map m = new HashMap();
		m.put("periode","");
		return query("selectBankForJurnal",m);
	}
	
	public List selectJenisBdnDanNpwp(String mcl_id)  {
		Map m = new HashMap();
		m.put("mcl_id",mcl_id);
		return query("selectJenisBdnDanNpwp",m);
	}
	
	
	public List selectInfoDBankForJurnal(String no_pre)  {
		Map m = new HashMap();
		m.put("no_pre",no_pre);
		return query("selectInfoDBankForJurnal",m);
	}
	
    public String inputJournalProcess( String mclId, String periode, String bankId, String noGiro ) throws DataAccessException
    {  	
    	Map params = new HashMap();
    	params.put("mclId", mclId);
    	params.put("periode", periode);
    	params.put("bankId", bankId);
    	params.put("noGiro", noGiro);
    	return (String) querySingle( "inputJournalProcess", params );
    }
    
    public String selectMaxPeriodeMstDiskonPerusahaan( Map params ) throws DataAccessException
    {
		return ( String ) querySingle( "selectMaxPeriodeMstDiskonPerusahaan", params );
	}
    
    public Integer selectMaxNoMstHistoryUT( Map params ) throws DataAccessException
    {
		return ( Integer ) querySingle( "selectMaxNoMstHistoryUT", params );
	}
    
    public BigDecimal selectNettDiskonPerusahaan( BigDecimal nominal, String periode , String mcl_id, String no_pre ) throws DataAccessException
    {
    	Map map = new HashMap();
    	map.put("nominal", nominal);
    	map.put("periode", periode);
    	map.put("mcl_id", mcl_id);
    	map.put("no_pre", no_pre);
		return ( BigDecimal ) querySingle( "selectNettDiskonPerusahaan", map );
	}
    
    public BigDecimal selectMaxNomorCompanyWs( String periode, String mcl_id ) throws DataAccessException
    {
    	Map map = new HashMap();
    	map.put("mcl_id", mcl_id);
    	map.put("periode", periode);
		return ( BigDecimal ) querySingle( "selectMaxNomorCompanyWs", map );
	}
    
    public BigDecimal selectPajakDiskonPerusahaan( BigDecimal nominal, String periode , String mcl_id, String no_pre ) throws DataAccessException
    {
    	Map map = new HashMap();
    	map.put("nominal", nominal);
    	map.put("periode", periode);
    	map.put("mcl_id", mcl_id);
    	map.put("no_pre", no_pre);
		return ( BigDecimal ) querySingle( "selectPajakDiskonPerusahaan", map );
	}
    
	public Map selectInfoDiskonPerusahaan(String jenis, String mcl_id)  {
		Map m = new HashMap();
		m.put("jenis",jenis);
		m.put("mcl_id",mcl_id);
		return (HashMap) querySingle("selectInfoDiskonPerusahaan",m);
	}	
	
	public List selectDetailUpdateJurnalDiskonPerusahaan(String mcl_id)  {
		Map m = new HashMap();
		m.put("mcl_id",mcl_id);
		return query("selectDetailUpdateJurnalDiskonPerusahaan",m);
	}
	
	public List<DropDown> selectPeriodeList(String mcl_id){
		Map params = new HashMap();
		params.put("mcl_id", mcl_id);
		return (List<DropDown>) query("selectPeriodeList", params);
	}

	public List<Map> selectInvoicePayroll(String customer, String tgl1, String tgl2)  {
		Map m = new HashMap();
		m.put("customer",customer);
		m.put("tgl1",tgl1);
		m.put("tgl2",tgl2);
		return query("selectInvoicePayroll", m);
	}	
	
	 public String selectNoPreMstPerusahaan( String mclId, String periode) throws DataAccessException
	    {
			Map m = new HashMap();
			m.put("mclId",mclId);
			m.put("periode",periode);
		 	return (String) querySingle( "selectNoPreMstPerusahaan", m );
		}
	 
	 public Date selectTglRkFromTbank(String no_pre){
		 return (Date) querySingle("selectTglRkFromTbank", no_pre);
	 }
	    
}
