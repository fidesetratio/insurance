/**Reinstatement.java
 * @author  : Ferry Harlim
 * @created : Feb 15, 2008 8:49:05 AM
 */
package com.ekalife.elions.process.uw;

import java.util.Date;
import org.springframework.dao.DataAccessException;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.reinstate.CommandReins;
import com.ekalife.elions.model.reinstate.Reinstatement;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentDao;

public class Reinstate extends ParentDao{

	public void prosesEditSurat(String spaj,String reinsNo,Integer aksep,Integer kondisi,String kondisiNote,
			Double totPremi,Date tglAccept, Double totBunga,Date tglPaid,String acceptNote)throws DataAccessException{
		
		reinstateDao.updateMstUwReinstateEdit(aksep,acceptNote,tglAccept,kondisi,
					kondisiNote,totPremi,totBunga,tglPaid,spaj,reinsNo);

	}	
	
	private boolean wf_check_reas(String spaj) {
		boolean lb_reas = true;
		Integer ll_row;
		ll_row=(Integer) reinstateDao.selectCountMstReins(spaj);
		
		if( ll_row.intValue() < 1)
			lb_reas = false;
		else if( ll_row.intValue() == 1)
			lb_reas = true;
		else
			lb_reas = false;
		
		return lb_reas;
	}
	
	public String prosesReinsReas(String spaj,String lusId,String noReins,Integer lspdId,Integer liBulan)throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		String pesan = "";
		if(!wf_check_reas(spaj)){
			pesan = "Polis Dengan Spaj "+spaj+" NON-REAS";
		}else{
			if(lspdId.intValue()==103) reinstateDao.updateMstReins1(spaj,106);
		}
		//
		reinstateDao.updateMstUwReinstate(spaj,lusId,new Integer(106),liBulan.intValue(),noReins);
		reinstateDao.updateMstReinstate(spaj,noReins,106);
		
		return pesan;
	}
	
	public void prosesBackToReinstate(String spaj,String noReins)throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		reinstateDao.deleteMstUwReinstate(spaj);
		reinstateDao.updateMstPolicyLspdId(spaj,99);
		reinstateDao.updateMstInsuredLspsId(spaj,17);
		reinstateDao.updateMstReinstate(spaj,null,17);
		
	}
	
	public void prosesTransferReinstate(String spaj,String noReins, String lus_id)throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Integer llRow;
		llRow=(Integer)reinstateDao.selectCountMstReins(spaj);
		
		if(llRow.intValue()==1)
			reinstateDao.updateMstReins2(spaj,99,1);
		
		reinstateDao.updateMstReinstate(spaj,noReins,107);
		reinstateDao.updateMstUwReinstateLspdId(spaj,noReins,107);
		
		//simpan tanggal transfernya, digunakan untuk report service level reinstate
		reinstateDao.updateMstTransHistory(spaj, "tgl_berkas_kirim_uw", "user_berkas_kirim_uw", lus_id);
	}
	
	public void prosesPrintSuratKonfirmasiPemulihanPolis(String spaj,String reins,String lusId,Integer msur_print,User currentUser)throws DataAccessException{
		String to=props.getProperty("uw.worksheet_to");
		String cc=props.getProperty("admin.yusuf");
		String pesan="Surat Konfirmasi Pemulihan Polis dengan Nospaj:"+FormatString.nomorSPAJ(spaj)+" telah dicetak.";
		//email.send(to.split(";"), cc.split(";"), "Cetak Surat Konfirmasi Pemulihan Polis", pesan, currentUser);
				
		reinstateDao.updateMstUwReinstateMsurPrint(spaj,reins,lusId,msur_print);
	}
	
	public void prosesCetakUlangSuratKonfirmasi(String spaj,String reins,Integer msurPrint,Date msurPrintDate,String desc ) throws DataAccessException{
		reinstateDao.updateMstUwReinstate(spaj,reins,msurPrint,msurPrintDate,desc);
	}	


	public void prosesReinstatementWorkSheet(CommandReins cmdReins,User currentUser)throws DataAccessException{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Reinstatement reins=(Reinstatement)cmdReins.getLsReinstatement().get(0);
//		reins.setLssp_id(1);
		uwDao.updateMstInsuredReinstatement(cmdReins.getReg_spaj(),reins.getMste_tgl_kirim_LB(), reins.getMste_tgl_terima_LB());
		if(reins.getMste_tgl_kirim_LB() != null && !reins.getMste_tgl_kirim_LB().equals(""))
			uwDao.saveMstTransHistory(cmdReins.getReg_spaj(), "tgl_kirim_lb", FormatDate.toStampDate(reins.getMste_tgl_kirim_LB()), null, null);
		if(reins.getMste_tgl_terima_LB() != null && !reins.getMste_tgl_terima_LB().equals(""))
			uwDao.saveMstTransHistory(cmdReins.getReg_spaj(), "tgl_terima_lb", FormatDate.toStampDate(reins.getMste_tgl_terima_LB()), null, null);
		uwDao.insertLst_ulangan(cmdReins.getReg_spaj(), commonDao.selectSysdate(), 
				"REINSTATEMENT", reins.getLssp_id(), currentUser.getLus_id(), reins.getPut_uw_new());
		uwDao.insertLst_ulangan(cmdReins.getReg_spaj(), commonDao.selectSysdateTruncated(3/86400) , 
				"REINSTATEMENT OK", reins.getLssp_id(), currentUser.getLus_id(), reins.getPut_uw_kep());
		uwDao.insertMstPositionSpaj(currentUser.getLus_id(), reins.getPut_uw_kep(),cmdReins.getReg_spaj(), 0);
		
	}
}
