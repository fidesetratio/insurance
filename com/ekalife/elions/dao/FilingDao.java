package com.ekalife.elions.dao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.AksesHist;
import com.ekalife.elions.model.Filing;
import com.ekalife.utils.parent.ParentDao;

	public class FilingDao extends ParentDao{
	
		DecimalFormat f1 = new DecimalFormat ("0");
		DecimalFormat f2 = new DecimalFormat ("00");
		DecimalFormat f3 = new DecimalFormat ("000");
		
		SimpleDateFormat sdfDd=new SimpleDateFormat("dd");
		SimpleDateFormat sdfMm=new SimpleDateFormat("MM");
		SimpleDateFormat sdfYy=new SimpleDateFormat("yyyy");
		SimpleDateFormat sdf_yyMM=new SimpleDateFormat("yyMM");
		SimpleDateFormat sdf_yearMonth=new SimpleDateFormat("yyyyMM");
		
		NumberFormat dec2= new DecimalFormat("#.00;(#,##0.00)"); //
		NumberFormat dec3 = new DecimalFormat("#.000;(#,##0.000)"); //
		
	protected void initDao() throws DataAccessException{
		this.statementNameSpace ="elions.filing.";
	}
	
//	Proses Select
	
	public Filing selectSpajOrNoPolis(String reg_spaj) throws DataAccessException{
		return (Filing) querySingle("selectSpajOrNoPolis", reg_spaj);
	}
	
	public List selectNoBoxFromMBox(int lde_id)throws DataAccessException{
		return query("selectNoBoxFromMBox", lde_id);
	} 
	
	public List<Filing> selectFilterSPAJInFilling(String reg_spaj)throws DataAccessException{
		return  query("selectFilterSPAJInFilling", reg_spaj);
	}
	
	public Integer selectCountSpajAfterAcceptInFilling(String reg_spaj)throws DataAccessException{
		return (Integer) querySingle("selectCountSpajAfterAcceptInFilling", reg_spaj);
	}
	
	public Integer selectCountBundleFilling(String kd_box)throws DataAccessException{
		return (Integer) querySingle("selectCountBundleFilling", kd_box);
	}

	//////////////////////////////////////////////
	
//	Proses insert
	
	public void insertMBundle(Filing filing) throws DataAccessException{
		insert("insertMBundle", filing);
	}
	
	public void insertMFile(Filing filing)throws DataAccessException{
		insert("insertMFile", filing);
	}
	
	//////////////////////////////////////////////
	
}