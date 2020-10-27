package com.ekalife.elions.dao;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.Simcard;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentDao;

@SuppressWarnings("unchecked")
public class SimasCardDao extends ParentDao{

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.simas_card.";
	}	
	
	public void updateSuratSimasCard(Integer flag_print, Integer msc_jenis, String no_kartu) throws DataAccessException{
		Map map = new HashMap();
		map.put("flag_print", flag_print);
		map.put("msc_jenis", msc_jenis);
		map.put("no_kartu", no_kartu);
		update("updateSuratSimasCard", map);
	}
	
	public void updateKartuSimasCard(Integer flag_print, Date tgl_print, Integer msc_jenis, String no_kartu) throws DataAccessException{
		Map map = new HashMap();
		map.put("flag_print", flag_print);
		map.put("tgl_print", tgl_print);
		map.put("msc_jenis", msc_jenis);
		map.put("no_kartu", no_kartu);
		update("updateKartuSimasCard", map);
		
	}
	
	public void updateFlagActiveSimasCard(String reg_spaj,  String mrc_no_kartu_lama) throws DataAccessException{
		Map map = new HashMap();
		map.put("reg_spaj", reg_spaj);
		map.put("mrc_no_kartu_lama", mrc_no_kartu_lama);
		update("updateFlagActiveSimCard", map);
		update("updateFlagActiveKartuPas", map);
	}
	
	public List<Simcard> selectCetakSimasCard(int msc_jenis, String lca_id, int flag_print, String cabangBII, String cabangBankSinarmas, int jumlah_print) throws DataAccessException{
		Map m = new HashMap();
		m.put("msc_jenis", msc_jenis);
		m.put("lca_id", lca_id);
		m.put("flag_print", flag_print);
		m.put("cabangBII", cabangBII);
		m.put("cabangBankSinarmas", cabangBankSinarmas);
		m.put("jumlah_print", jumlah_print);
		return query("selectCetakSimasCard", m);
		
	}
	
	public List<DropDown> selectJenisSimasCard() throws DataAccessException{
		return query("selectJenisSimasCard", null);
	}
	
	public List selectCabangSimasCard() throws DataAccessException{
		return query("selectCabangSimasCard", null);
	}
	
	public List<DropDown> selectCabangBankSimasCard(int bank) throws DataAccessException{
		return query("selectCabangBankSimasCard", bank);
	}
	
	public Simcard selectSimasCardBySpaj(String reg_spaj) throws DataAccessException {
		return (Simcard) querySingle("selectSimasCardBySpaj", reg_spaj);
	}
	
	public Simcard selectSimasCardByNoKartu(String no_kartu) throws DataAccessException {
		return (Simcard) querySingle("selectSimasCardByNoKartu", no_kartu);
	}
	
	public List<DropDown> selectCariSimasCard(int jenis, String kata) throws DataAccessException{
		Map m = new HashMap();
		m.put("jenis", jenis);
		m.put("kata", kata);
		return query("selectCariSimasCard", m);
	}
	
	public Simcard selectSimcard(String msc_jenis, String no_kartu) throws DataAccessException{
		Map m = new HashMap();
		m.put("msc_jenis", msc_jenis);
		m.put("no_kartu", no_kartu);
		return (Simcard) querySingle("selectSimcard", m);
	}
	
	public int selectCountSimcardByJenis(int msc_jenis) throws DataAccessException{
		return (Integer) querySingle("selectCountSimcardByJenis", msc_jenis);
	}
	
	public void saveSimcard(Simcard s, User u) throws DataAccessException{
		//1. INSERT
		if(s.getLus_id() == null) {

			//PROSEDUR KHUSUS DIREKSI & REKANAN DIREKSI
			if(s.getMsc_jenis().intValue() == 2 || s.getMsc_jenis().intValue() == 3) {
				int ll_no = selectCountSimcardByJenis(s.getMsc_jenis());
				Date now = commonDao.selectSysdate();
				DateFormat mm = new SimpleDateFormat("MM");
				DateFormat yyyy = new SimpleDateFormat("yyyy");
				
				ll_no++;
	
				if(s.getMsc_jenis().intValue() == 2) {
					s.setNo_kartu("01." + mm.format(now) + ".512." + yyyy.format(now) + ".1" + FormatString.rpad("0", String.valueOf(ll_no), 4));
				}else if(s.getMsc_jenis().intValue() == 3) {
					s.setNo_kartu("01." + mm.format(now) + ".512." + yyyy.format(now) + ".2" + FormatString.rpad("0", String.valueOf(ll_no), 4));
				}
			}
			//END OF PROSEDUR KHUSUS DIREKSI & REKANAN DIREKSI
			
			s.setLus_id(Integer.valueOf(u.getLus_id()));
			insert("insertSimasCard", s);
			
		//2. UPDATE
		}else {
			update("updateSimasCard", s);			
		}
	}
	
	public void prosesSavePenggantianSimasCard(String spaj, String mrc_no_kartu, String ket, String mrc_no_kartu_lama, User user)throws DataAccessException{
		//update flag_active lama jadi 0
		updateFlagActiveSimasCard(spaj, mrc_no_kartu_lama);
		uwDao.prosesInsertSimasCardNew(spaj, mrc_no_kartu, user, 1);
		uwDao.insertMstPositionSpaj(user.getLus_id(), "SIMAS CARD PENGGANTI NO : "+ mrc_no_kartu + "KETERANGAN :" + ket, spaj, 0);
	}
	// *VIPcard
	public List<Simcard> selectCetakVipCard(int msc_jenis, String lca_id, int flag_print, int jumlah_print) throws DataAccessException{
		Map m = new HashMap();
		m.put("msc_jenis", msc_jenis);
		m.put("lca_id", lca_id);
		m.put("flag_print", flag_print);
		m.put("jumlah_print", jumlah_print);
		return query("selectCetakVipCard", m);
	}
	
}