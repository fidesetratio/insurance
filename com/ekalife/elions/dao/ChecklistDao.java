package com.ekalife.elions.dao;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.Checklist;
import com.ekalife.elions.model.CommandChecklist;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentDao;

@SuppressWarnings("unchecked")
public class ChecklistDao extends ParentDao{

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.checklist.";
	}	
	
	public int selectPosisiDokumenBySpaj(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectPosisiDokumenBySpaj", reg_spaj);
	}
	
	public int selectStsAksepBySpaj(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectStsAksepBySpaj", reg_spaj);
	}
	
	public String selectLcaIdBySpaj(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectLcaIdBySpaj", reg_spaj);
	}
	
	public List<Checklist> selectCheckListBySpaj(String reg_spaj) throws DataAccessException{
		return query("selectCheckListBySpaj", reg_spaj);
	}

	public boolean selectValidasiCheckListBySpaj(String reg_spaj) throws DataAccessException{
		Map m = (HashMap) querySingle("selectValidasiCheckListBySpaj", reg_spaj);
		if(m == null) return false;
		int lspd_id = (Integer) querySingle("selectPosisiDokumenBySpaj", reg_spaj);
		Integer lssa_id = (Integer) querySingle("selectStsAksepBySpaj", reg_spaj);
		
		
		int adm 	= ((BigDecimal) m.get("ADM")).intValue();
		int uw 		= ((BigDecimal) m.get("UW")).intValue();
		int print 	= ((BigDecimal) m.get("PRINT")).intValue();
		int filling	= ((BigDecimal) m.get("FILLING")).intValue();
		
		if(lspd_id == 1 && adm == 0 ) return false;
		else if(lspd_id == 2 && uw == 0 ) return false;
		else if(lspd_id == 6 && print == 0) return false;
		else if(lspd_id == 7 && filling == 0 ) return false;
		else return true;
	}
	
	public void saveChecklist(CommandChecklist cmd, User currentUser) throws DataAccessException {
		
		Date now = commonDao.selectSysdate();
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Integer lssa_id = (Integer) querySingle("selectStsAksepBySpaj", (cmd.reg_spaj));
		String lca_id=(String)querySingle("selectLcaIdBySpaj", (cmd.reg_spaj));
		Integer cekflag=0;
	
		cmd.setLssa_id(lssa_id);
		cmd.setLca_id(lca_id);
		for(Checklist c : cmd.listChecklist) {

			c.setReg_spaj(cmd.reg_spaj);
			if(c.flag_adm == null) c.flag_adm = 0;
			if(c.flag_bancass == null) c.flag_bancass = 0;
			if(c.flag_uw == null) c.flag_uw = 0;
			if(c.flag_print == null) c.flag_print = 0;
			if(c.flag_filling == null) c.flag_filling = 0;
			
			if(cmd.lspd_id == 1) { //adm
				c.lus_id_adm 		= Integer.valueOf(currentUser.getLus_id());
				c.tgl_adm 			= now;
				cmd.setCekflag(-1);
				if(c.flag_adm == null) c.flag_adm = 0;
			}else if(cmd.lspd_id == 2) { //uw
				c.lus_id_uw 		= Integer.valueOf(currentUser.getLus_id());
				c.tgl_uw 			= now;
				cmd.setCekflag(-1);
				if(c.flag_uw == null) c.flag_uw = 0;
			}else if(cmd.lspd_id == 6) { //print
				c.lus_id_print 		= Integer.valueOf(currentUser.getLus_id());
				c.tgl_print 		= now;
				cmd.setCekflag(-1);
				if(c.flag_print == null) c.flag_print = 0;
			}else if(cmd.lspd_id == 7) { //tanda terima -> filling
				c.lus_id_filling 	= Integer.valueOf(currentUser.getLus_id());
				c.tgl_filling 		= now;
				cmd.setCekflag(-1);
				if(c.flag_filling == null) c.flag_filling = 0;
			}else if(cmd.lspd_id == -1) { //menu terpisah 
				c.lus_id_filling 	= Integer.valueOf(currentUser.getLus_id());
				c.tgl_filling 		= now;
//				cmd.setCekflag(0);
//				c.tgl_print 		= now;
//				c.lus_id_print 		= Integer.valueOf(currentUser.getLus_id());
				if(lssa_id==10 && cmd.centang==1&&cmd.lca_id.equals("09")){//request uw jika akseptasi khusus checklist print& filling ter-checklist
					c.tgl_print 		= now;
					c.lus_id_print 		= Integer.valueOf(currentUser.getLus_id());
					if(c.flag_filling!=0){
						cekflag=cekflag+1;
					}
					cmd.setCekflag(cekflag);
				}else{
					cmd.setCekflag(-1);
				}
				
			}
			
			//update atau insert
			if(update("updateMstChecklist", c) == 0) {
				insert("insertMstChecklist", c);
			}
			
			if(c.lc_id==33 && (c.flag_adm==1 || c.flag_bancass==1 || c.flag_uw==1 || c.flag_print == 1 || c.flag_filling == 1)){//Req Hadi (22 OCt 2012) : Jika Ceklist SPH & File upload SPH memang ada, Otomatis akan ditandai exist(flag = 1)
				String directory = props.getProperty("pdf.dir.export")+"\\"+cmd.lca_id+"\\"+cmd.reg_spaj;
				File destDir = new File(directory);
				if(destDir.exists()) {
					String[] children = destDir.list();
					for(int i=0; i<children.length; i++) {
						if(children[i].startsWith(cmd.reg_spaj+"SPH") ){
							bacDao.updateFlagSPH(cmd.reg_spaj, 1);
						}
					}
				}
		    }
			
		}
		
	}
	
	public void saveChecklistBancass(CommandChecklist cmd, User currentUser) throws DataAccessException {
		
		Date now = commonDao.selectSysdate();
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		
		for(Checklist c : cmd.listChecklist) {

			c.setReg_spaj(cmd.reg_spaj);
			
			//bancass
			c.lus_id_bancass 	= Integer.valueOf(currentUser.getLus_id());
			c.tgl_bancass 		= now;
			if(c.flag_bancass == null) c.flag_bancass = 0;
			
			//update atau insert
//			if(update("updateMstChecklistBancass", c) == 0) {
//				if((query("selectMstChecklistExist", c)).size() == 0) {
//					insert("insertMstChecklist", c);
//				}
//			}
			
			if((query("selectMstChecklistExist", c)).size() == 0) {
				insert("insertMstChecklist", c);
			}else{
				if(c.flag_bancass != 0){
					update("updateMstChecklistBancass", c);
				}
			}
		}
		
	}
	
	public void saveChecklistPas(Checklist c) throws DataAccessException {
		//update atau insert
		if(update("updateMstChecklist", c) == 0) {
			insert("insertMstChecklist", c);
		}
	}
	
}