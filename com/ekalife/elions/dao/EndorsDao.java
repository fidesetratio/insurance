package com.ekalife.elions.dao;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.Bmi;
import com.ekalife.elions.model.DetEndors;
import com.ekalife.elions.model.Endors;
import com.ekalife.utils.parent.ParentDao;

/**
 * Proses Endors New Business (Untuk UW dan hanya non-material saja)
 * 
 * @author yusuf
 * @since Oct 2, 2009 (10:38:28 AM)
 */
@SuppressWarnings("unchecked") 
public class EndorsDao extends ParentDao{

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.endors.";
	}	
	
	/**
	 * list jenis endors yang ingin ditampilkan
	 * @return
	 * @throws DataAccessException
	 * Filename : EndorsDao.java
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : Oct 14, 2009 11:09:36 AM
	 */
	public List<DropDown> selectListJnEndors() throws DataAccessException{
		return query("selectListJnEndors", null);
	}
	
	/**
	 * saat ini khusus untuk endors - endors dari uw aja
	 * @param lstb_id
	 * @param lspd_id
	 * @return
	 * @throws DataAccessException
	 * Filename : EndorsDao.java
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : Oct 14, 2009 11:20:04 AM
	 */
	public List selectListNoEndors(Integer lstb_id, Integer lspd_id,String reg_spaj) throws DataAccessException{
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("lstb_id", lstb_id);
		params.put("lspd_id", lspd_id);
		params.put("reg_spaj", reg_spaj);
		return query("selectListNoEndors", params);
	}
	
	public Endors selectMstEndors(String no_endors) throws DataAccessException{
		return (Endors) querySingle("selectMstEndors", no_endors);
	}
	
	public List<DetEndors> selectMstDetEndors(String no_endors)throws DataAccessException{
		return query("selectMstDetEndors", no_endors);
	}
	
}