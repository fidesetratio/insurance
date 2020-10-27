package com.ekalife.elions.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.FormHist;
import com.ekalife.elions.model.FormSpaj;
import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentDao;

/**
 * Data Access Object untuk modul2 di menu kertas polis
 * 
 * @author Hemilda
 * @since Feb 23, 2007 (4:56:03 PM)
 */
@SuppressWarnings("unchecked")
public class BlankoDao extends ParentDao{

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.bas.";
	}	

	/**
	 * sequence EKA.MST_FORM (MSCO_NUMBER = 74)
	 * @return (2 digit cabang + 4 digit tahun + 4 digit sequence)
	 */
	public String sequenceMst_form(int mss_jenis, String cabang) {
		String sekuens = this.uwDao.selectGetCounter(74, "00");
		this.uwDao.updateCounter(sekuens, 74, "00");

		String hasil = FormatString.rpad("0", sekuens,9);
		
		if(logger.isDebugEnabled())logger.debug("SEQUENCE: untuk EKA.MST_FORM terakhir adalah: " + hasil);
		
		return hasil;
	}
	
	
	/**
	 * Proses untuk insert permohonan pengiriman kertas polis dari pusat ke cabang
	 * 
	 * @author Hemilda Sari Dewi
	 * @since Feb 23, 2007 (4:56:17 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processNewFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		FormSpaj f = cmd.getDaftarFormSpaj().get(0);
		if("".equals(f.getMsf_id())) { //INSERT BARU
			String seq = this.sequenceMst_form(2, currentUser.getLca_id());
			for(FormSpaj s : cmd.getDaftarFormSpaj()) {
				s.setMsf_amount(0);
				s.setMsf_id(seq);
				s.setMss_jenis(2);
				s.setLca_id(currentUser.getLca_id());
				s.setMsab_id(0); //AGENT 000000
				if(((Integer) querySingle("selectCekSpaj", s))==0) {
					Spaj spaj = new Spaj();
					spaj.newSpaj(2, s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 0, Integer.valueOf(currentUser.getLus_id()),null);
					insert("insertNewSpaj", spaj);
				}
				insert("insertNewFormSpaj", s);
			}
			cmd.setMsf_id(seq);

			FormHist form = new FormHist();
			form.setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
			form.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			form.setPosisi(0);
			form.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			form.setMsfh_desc("Permintaan oleh Branch Admin");			
			insert("insertFormHistory", form);
			
			return 
				"Permintaan anda sudah tersimpan dengan nomor " + cmd.getMsf_id() 
				+ ". \\nApabila sudah di-approve oleh Agency Support / BAS, maka KERTAS POLIS akan dikirimkan oleh General Affairs";
		} else { //UPDATE DATA LAMA
			for(FormSpaj s : cmd.getDaftarFormSpaj()) {
				update("updateFormSpaj", s);
			}
			FormHist form = new FormHist();
			form.setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
			form.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			form.setPosisi(0);
			form.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			form.setMsfh_desc("Perubahan Permintaan oleh Branch Admin");			
			insert("insertFormHistory", form);
			
			return "Data Permintaan anda sudah di-update.";
		}
				
	}
	
	/**
	 * Proses untuk pembatalan permohonan pengiriman KERTAS POLIS dari pusat ke cabang
	 * 
	 * @author Hemilda Sari Dewi
	 * @since Feb 23, 2007 (4:56:42 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processCancelFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(FormSpaj s : cmd.getDaftarFormSpaj()) {
			s.setMsf_amount(0);
			s.setPosisi(2); //cancel
			update("updateFormSpaj", s);
		}
		if(!cmd.getDaftarFormSpaj().isEmpty()) {
			FormHist f = new FormHist();
			f.setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
			f.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			f.setPosisi(2);
			f.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			f.setMsfh_desc("Dibatalkan oleh Branch Admin");			
			insert("insertFormHistory", f);
		}
		
		return "Data Permintaan anda sudah dibatalkan.";
	}
	
	/**
	 * Proses untuk pembatalan pertanggungjawaban KERTAS POLIS dari pusat ke cabang
	 * 
	 * @author Hemilda Sari Dewi
	 * @since Feb 23, 2007 (4:56:42 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processBatalFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(Spaj s : cmd.getDaftarStokSpaj()) {
			s.setDamage(0);
			s.setLca_id(currentUser.getLca_id()); //cancel
			s.setMss_jenis(2);
			s.setMsab_id(0);
			update("updateStokSpajnol", s);
		}
		return "Data Pertanggungjawaban anda sudah dibatalkan.";
	}
	
	/**
	 * Proses untuk persetujuan permohonan pengiriman KERTAS POLIS dari pusat ke cabang,
	 * oleh agency support / BAS
	 * 
	 * @author Hemilda Sari Dewi
	 * @since Feb 23, 2007 (4:56:42 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processApprovalFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
			FormSpaj s = cmd.getDaftarFormSpaj().get(i);
			s.setPosisi(1); //approve
			update("updateFormSpaj", s);
		}
		
		cmd.getFormHist().setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
		cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getFormHist().getMsf_id()));
		cmd.getFormHist().setPosisi(1);
		cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		insert("insertFormHistory", cmd.getFormHist());
		
		return 
			"Data permintaan cabang sudah disetujui. "
			+ ". \\nPermintaan akan selanjutnya diproses oleh General Affairs";
	}
	
	/**
	 * Proses untuk penolakan permohonan pengiriman KERTAS POLIS dari pusat ke cabang,
	 * oleh agency support / BAS
	 * 
	 * @author Hemilda Sari Dewi
	 * @since Feb 23, 2007 (4:56:42 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processRejectFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
			FormSpaj s = cmd.getDaftarFormSpaj().get(i);
			s.setPosisi(3); //reject
			update("updateFormSpaj", s);
		}

		cmd.getFormHist().setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
		cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getFormHist().getMsf_id()));
		cmd.getFormHist().setPosisi(3);
		cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		insert("insertFormHistory", cmd.getFormHist());

		return 
			"Data permintaan cabang sudah ditolak. "
			+ ". \\nAlasan penolakan akan diteruskan ke cabang bersangkutan.";
	}
	
	/**
	 * Proses yang menandakan bahwa permohonan pengiriman KERTAS POLIS dari pusat ke cabang
	 * sudah dilakukan oleh GA
	 * 
	 * @author Hemilda Sari Dewi
	 * @since Feb 23, 2007 (4:56:42 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processSendFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
			FormSpaj s = cmd.getDaftarFormSpaj().get(i);
			s.setPosisi(4); //sent
			update("updateFormSpaj", s);
		}

		cmd.getFormHist().setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
		cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getFormHist().getMsf_id()));
		cmd.getFormHist().setPosisi(4);
		cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		insert("insertFormHistory", cmd.getFormHist());

		return 
			"Data pengiriman KERTAS POLIS sudah diupdate. ";
	}

	/**
	 * Proses yang menandakan bahwa KERTAS POLIS hasil permintaan dari cabang ke pusat sudah dikirimkan
	 * ke cabang dan sudah diterima oleh cabang, sehingga otomatis menambah jumlah KERTAS POLIS yang ada di cabang
	 * 
	 * @author Hemilda Sari Dewi
	 * @since Feb 26, 2007 (2:26:05 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processReceiveFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
			FormSpaj s = cmd.getDaftarFormSpaj().get(i);
			s.setPosisi(5); //received
			update("updateFormSpaj", s);
			Spaj spaj = new Spaj();
			spaj.newSpaj(s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), s.getMsf_amount(), Integer.valueOf(currentUser.getLus_id()),null);
			update("updateStokSpaj", spaj);
		}

		if(!cmd.getDaftarFormSpaj().isEmpty()) {
			FormHist f = new FormHist();
			f.setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
			f.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			f.setPosisi(5);
			f.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			f.setMsfh_desc("Sudah diterima oleh Branch Admin");			
			insert("insertFormHistory", f);
		}

		return 
			"Data pengiriman KERTAS POLIS sudah diupdate. Jumlah KERTAS POLIS di sistem sudah ditambahkan.";
	}
	
	
	/**
	 * Query untuk menarik nomor urut terakhir dari mst_form_hist
	 * 
	 * @author Hemilda Sari Dewi
	 * @since Feb 26, 2007 (9:00:58 AM)
	 * @param spaj
	 * @return
	 */
	public Integer selectNoUrutFormHistory(String msf_id) throws DataAccessException {
		Integer result = (Integer) querySingle("selectNoUrutFormHistory", msf_id);
		if(result == null) return 1;
		else return result + 1;
	}
	
	
	/**
	 * Query untuk menarik data yang sudah ada, atau untuk penyimpanan data baru 
	 * dari permintaan pengiriman spaj
	 * @author Hemilda Sari Dewi
	 * @since Feb 23, 2007 (4:58:53 PM)
	 * @param msf_id
	 * @param lca_id
	 * @param lus_id
	 * @return
	 */
	public List<FormSpaj> selectFormBlanko(String msf_id, String lca_id) throws DataAccessException {
		FormSpaj formSpaj = new FormSpaj();
		formSpaj.setMss_jenis(0);
		formSpaj.setMsf_id(msf_id);
		formSpaj.setLca_id(lca_id);
		if(msf_id==null)
			return query("selectNewFormBlanko", formSpaj);
		else
			return query("selectFormSpaj", formSpaj);
	}
	
	
	/**
	 * Proses untuk insert pertanggungjawaban kertas polis dari pusat ke cabang
	 * 
	 * @author Hemilda Sari Dewi
	 * @since Feb 23, 2007 (4:56:17 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processSimpanFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
			for(Spaj s : cmd.getDaftarStokSpaj()) {
				update("updateStokSpajnol", s);
			}
			return "Data Pertanggungjawaban anda sudah di-update.";
	}	
	
	
	public Map select_mst_form(String msf_id)
	{
		return (HashMap) querySingle("select_mst_form", msf_id);
	}

}