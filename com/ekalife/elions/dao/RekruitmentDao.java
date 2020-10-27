package com.ekalife.elions.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.CalonKaryawan;
import com.ekalife.elions.model.Kuesioner;
import com.ekalife.elions.model.KuesionerTanggungan;
import com.ekalife.utils.parent.ParentDao;

@SuppressWarnings("unchecked")
public class RekruitmentDao extends ParentDao{

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.rekruitment.";
	}	
	
	public void insertmst_kuesioner(Kuesioner kuesioner) {
		insert("insert_mst_kuesioner", kuesioner);
	}
	
	public void updateKuesioner(Kuesioner kuesioner) {
		update("updateKuesioner", kuesioner);
	}
	
	public Integer cek_kuesioner(String nama, String tgllhr)
	{
		Map m = new HashMap();
		m.put("nama", nama);
		m.put("tgllhr", tgllhr);
		return (Integer) querySingle("cek_rekrut", m);	
	}
	
	public Double no_reg(String kunci)
	{
		return (Double) querySingle("no_reg", kunci);	
	}
	
	public String no_reg_s(String kunci)
	{
		return (String) querySingle("no_reg", kunci);	
	}
	
	public Kuesioner selectkuesioner ( String no_rekrut)
	{
		return (Kuesioner) querySingle("selectkuesioner",no_rekrut);
	}
	// *Kuesioner
	public List select_tertanggung_rekrut(String kode_rekrut){
		return query("select_tertanggung_rekrut", kode_rekrut);
	}
	
	public void insert_mst_tanggungan_ku(KuesionerTanggungan kuesionerTanggungan){
		insert("insert_mst_tanggungan_ku", kuesionerTanggungan);
	}
	
	public List<HashMap> selectdaftarRegKuesioner(String msag_id)throws DataAccessException
	{
		return  query("selectdaftarRegKuesioner", msag_id);	
	}
	
	public String selectEmailAdmin(String msag_id)throws DataAccessException
	{
		return  (String) querySingle("selectEmailAdmin", msag_id);	
	}
	
	public void insert_aaji_calon_karyawan(Map m){
		insert("insert_aaji_calon_karyawan", m);
	}
	
	public void  updateAAJICalonKaryawan(int blacklist, int perusahaan_lama, int status_join, String ket, String ktp){
		Map m = new HashMap();
		m.put("blacklist", blacklist);
		m.put("perusahaan_lama", perusahaan_lama);
		m.put("status_join", status_join);
		m.put("keterangan", ket);
		m.put("ktp", ktp);
		update("updateAAJICalonKaryawan", m);
	}
	
	public List selectAAJICalonKaryawan(Date tglA, Date tglB, String ktp) throws DataAccessException{
		Map m = new HashMap();
		m.put("tglA", tglA);
		m.put("tglB", tglB);
		m.put("ktp", ktp);
		return query("selectAAJICalonKaryawan",m);
	}
	
	public List selectProsesAAJICalonKaryawan() throws DataAccessException{
		return query("selectProsesAAJICalonKaryawan",null);
	}

	public ArrayList selectDataTsr(String bdate, String edate, String dist1, String flag, String nip) {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("dist1", dist1);
		m.put("flag", flag);
		m.put("nip", nip);
		return (ArrayList) query("selectDataTsr",m);
	}
	
	public HashMap selectLstLevelDistRek(String dist, String inisial) throws DataAccessException
	{
		Map m = new HashMap();
		m.put("dist", dist);
		m.put("inisial", inisial);
		return (HashMap) querySingle("selectLstLevelDistRek", m);	
	}
	
	public void insert_mstkuesioner_hist(Map m){
		insert("insert_mstkuesioner_hist", m);
	}
	
	public List selectMstKuesioner_hist(String mku_no_reg) throws DataAccessException{
		Map m = new HashMap();
		m.put("mku_no_reg",mku_no_reg);
		return query("selectMstKuesioner_hist",m);
	}
	
	public List selectkuesionerBy(String lus_id, String posisi, String no_reg) throws DataAccessException{
		Map m = new HashMap();
		m.put("lus_id", lus_id);
		m.put("posisi", posisi);
		m.put("no_reg", no_reg);
		return query("selectkuesionerBy",m);
	}
	
	public List selectLstScan(String dept, String ID) throws DataAccessException{
		Map m = new HashMap();
		m.put("dept", dept);
		m.put("id", ID);
		return query ("selectLstScan",m);
	}
}