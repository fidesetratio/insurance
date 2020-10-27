package com.ekalife.elions.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.ekalife.elions.model.Commission;
import com.ekalife.elions.model.DepositPremium;
import com.ekalife.elions.model.Payment;
import com.ekalife.elions.model.Ulangan;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("unchecked")
public class EditDataDao extends ParentDao{
	protected final Log logger = LogFactory.getLog( getClass() );

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.edit_data.";
	}	
	
	/** SELECT */
	
	public List<String> selectDataAgenUntukTesting() throws DataAccessException{
		return query("selectDataAgenUntukTesting", null);
	}
	
	public void updateMstAgentTax(Double komisi, Double pajak, Date tanggal, String agen) throws DataAccessException{
		Map m = new HashMap();
		m.put("komisi", komisi);
		m.put("pajak", pajak);
		m.put("tanggal", tanggal);
		m.put("agen", agen);
		update("updateMstAgentTax", m);
	}
	
	public void insertMstUploadNon(Commission c) throws DataAccessException{
		insert("insertMstUploadNon", c);
	}
	
	public List<Commission> selectDataBonusStableLinkYangKetinggalan() throws DataAccessException{
		return query("selectDataBonusStableLinkYangKetinggalan", null);
	}
	
	public List<Map> selectCommissionWSLanjutanYgTertinggalper2010(String msag){
		return query("selectCommissionWSLanjutanYgTertinggalper2010", msag);
	}
	
	public List<Map> selectSerbaGuna(String query) throws DataAccessException{
		return query("selectSerbaGuna", query);
	}
	
	public List<DepositPremium> selectTitipanPremiBySpaj(String reg_spaj) throws DataAccessException{
		return query("selectTitipanPremiBySpaj", reg_spaj);
	}
	
	public List<Payment> selectPaymentBySpaj(String reg_spaj) throws DataAccessException{
		return query("selectPaymentBySpaj", reg_spaj);
	}
	
	/** UPDATE */
	
	public int updateMstDepositPremium(DepositPremium titipan) throws DataAccessException{
		return update("updateMstDepositPremium", titipan);
	}
	
	public int updateMstPayment(Payment payment) throws DataAccessException{
		return update("updateMstPayment", payment);
	}
	
	public int updateMstPaymentFromTitipan(Payment payment) throws DataAccessException{
		return update("updateMstPaymentFromTitipan", payment);
	}
	
	public int updateMstPaymentbySpajAndPre(Payment payment) throws DataAccessException{
		return update("updateMstPaymentbySpajAndPre", payment);
	}
	
	public int updateMstPaymentNonBank(String spaj, String nojm) throws DataAccessException{
		HashMap<Object, String> map = new HashMap<Object, String>();
		map.put("spaj", spaj);
		map.put("nojm", nojm);
		return update("updateMstPaymentNonBank", map);
	}
	
	/** INSERT */
	
	public void insertLstUlangan(Ulangan ulangan) throws DataAccessException{
		insert("insertLstUlangan", ulangan);
	}
	
	/** FUNGSI2 */
	
	//fungsi edit data
	/*
	public String editTitipanPremi(CommandEditData cmd, User currentUser) throws DataAccessException{
		
		NumberFormat nf = NumberFormat.getNumberInstance();
		
		List<DepositPremium> listLama = editDataDao.selectTitipanPremiBySpaj(cmd.getReg_spaj());
		for(DepositPremium d : cmd.getListTitipanPremi()) {
			if(d.isChecked()) {
				logger.info(d.getMsdp_number());

				for(DepositPremium lama : listLama) {
					if(d.getReg_spaj().equals(lama.getReg_spaj()) && d.getMsdp_number().equals(lama.getMsdp_number())) {
						StringBuffer ket = new StringBuffer();
						
						//var untuk update titipan
						DepositPremium updateDp = new DepositPremium();
						updateDp.setReg_spaj(d.getReg_spaj());
						updateDp.setMsdp_number(d.getMsdp_number());
						
						//var untuk update payment
						Payment updatePayment = new Payment();
						updatePayment.setReg_spaj(d.getReg_spaj());
						updatePayment.setMsdp_number(d.getMsdp_number());
						
						ket.append("DATA LAMA:");
						
						//kurs
						if(!d.getLku_id().equals(lama.getLku_id())) {
							ket.append(",lku=" + lama.getLku_id());
							updateDp.setLku_id(d.getLku_id());
							updatePayment.setLku_id(d.getLku_id());
							
						}
						
						//jml bayar
						if(!d.getMsdp_payment().equals(lama.getMsdp_payment())) {
							ket.append(",payment=" + nf.format(lama.getMsdp_payment()));
							updateDp.setMsdp_payment(d.getMsdp_payment());
							updatePayment.setMspa_payment(d.getMsdp_payment());
						}
						
						//tgl rk
						if(!d.getMsdp_date_book().equals(lama.getMsdp_date_book())) {
							ket.append(",rk=" + defaultDateFormat.format(lama.getMsdp_date_book()));
							updateDp.setMsdp_date_book(d.getMsdp_date_book());
							updatePayment.setMspa_date_book(d.getMsdp_date_book());
						}
						
						//tgl bayar
						if(!d.getMsdp_pay_date().equals(lama.getMsdp_pay_date())) {
							ket.append(",tglbyr=" + defaultDateFormat.format(lama.getMsdp_pay_date()));
							updateDp.setMsdp_pay_date(d.getMsdp_pay_date());
							updatePayment.setMspa_pay_date(d.getMsdp_pay_date());
						}
						
						//tgl jatuh tempo
						if(d.getMsdp_due_date() == null) {
							if(lama.getMsdp_due_date() != null) {
								ket.append(",tgldue=" + defaultDateFormat.format(lama.getMsdp_due_date()));
								updateDp.setMsdp_due_date(d.getMsdp_due_date());
								updatePayment.setMspa_due_date(d.getMsdp_due_date());
							}
						}else if(!d.getMsdp_due_date().equals(lama.getMsdp_due_date())) {
							if(lama.getMsdp_due_date() == null) ket.append(",tgldue=null");
							else {
								ket.append(",tgldue=" + defaultDateFormat.format(lama.getMsdp_due_date()));
								updateDp.setMsdp_due_date(d.getMsdp_due_date());
								updatePayment.setMspa_due_date(d.getMsdp_due_date());
							}
						}

						//rek ekalife
						if(d.getLsrek_id() == null) {
							if(lama.getLsrek_id() != null) {
								ket.append(",rek=" + lama.getLsrek_id());
								updateDp.setLsrek_id(d.getLsrek_id());
								updatePayment.setLsrek_id(d.getLsrek_id());
							}
						}else if(!d.getLsrek_id().equals(lama.getLsrek_id())) {
							ket.append(",rek=" + lama.getLsrek_id());
							updateDp.setLsrek_id(d.getLsrek_id());
							updatePayment.setLsrek_id(d.getLsrek_id());
						}

						//no pre
						if(!d.getMsdp_no_pre().equals(lama.getMsdp_no_pre())) {
							ket.append(",pre=" + lama.getMsdp_no_pre());
							updateDp.setMsdp_no_pre(d.getMsdp_no_pre());
							updatePayment.setMspa_no_pre(d.getMsdp_no_pre());
						}
						
						//no voucher
						if(!d.getMsdp_no_voucher().equals(lama.getMsdp_no_voucher())) {
							ket.append(",vcr=" + lama.getMsdp_no_voucher());
							updateDp.setMsdp_no_voucher(d.getMsdp_no_voucher());
							updatePayment.setMspa_no_voucher(d.getMsdp_no_voucher());
						}

						//jenis bayar
						if(d.getLsjb_id() == null) {
							if(lama.getLsjb_id() != null) {
								ket.append(",lsjb=" + lama.getLsjb_id());
								updateDp.setLsjb_id(d.getLsjb_id());
								updatePayment.setLsjb_id(d.getLsjb_id());
							}
						}else if(!d.getLsjb_id().equals(lama.getLsjb_id())) {
							ket.append(",lsjb=" + lama.getLsjb_id());
							updateDp.setLsjb_id(d.getLsjb_id());
							updatePayment.setLsjb_id(d.getLsjb_id());
						}

						//no polis lama (u/ tahapan)
						if(!d.getMsdp_old_policy().equals(lama.getMsdp_old_policy())) {
							ket.append(",oldpol=" + lama.getMsdp_old_policy());
							updateDp.setMsdp_old_policy(d.getMsdp_old_policy());
							updatePayment.setMspa_old_policy(d.getMsdp_old_policy());
						}
						
						//nilai kurs
						if(d.getMsdp_selisih_kurs() == null) {
							if(lama.getMsdp_selisih_kurs() != null) {
								ket.append(",kurs=" + nf.format(lama.getMsdp_selisih_kurs()));
								updateDp.setMsdp_selisih_kurs(d.getMsdp_selisih_kurs());
								updatePayment.setMspa_nilai_kurs(d.getMsdp_selisih_kurs());
							}
						}else if(!d.getMsdp_selisih_kurs().equals(lama.getMsdp_selisih_kurs())) {
							ket.append(",kurs=" + nf.format(lama.getMsdp_selisih_kurs()));
							updateDp.setMsdp_selisih_kurs(d.getMsdp_selisih_kurs());
							updatePayment.setMspa_nilai_kurs(d.getMsdp_selisih_kurs());
						}

						//logger.info(ket);
						
//						if(!d.getNo_kttp().equals(lama.getNo_kttp())) logger.info("kttp = " + d.getNo_kttp());
//						if(d.getClient_bank() == null) {
//							if(lama.getClient_bank() != null) logger.info("client bank = " + d.getClient_bank());
//						}else if(!d.getClient_bank().equals(lama.getClient_bank())) logger.info("client bank = " + d.getClient_bank());
//						if(!d.getMsdp_no_rek().equals(lama.getMsdp_no_rek())) logger.info("norek = " + d.getMsdp_no_rek());
//						if(!d.getMsdp_desc().equals(lama.getMsdp_desc())) logger.info("desc = " + d.getMsdp_desc());
						
						updateMstDepositPremium(updateDp);
						updateMstPaymentFromTitipan(updatePayment);
						
						Ulangan u = new Ulangan();
						u.setReg_spaj(d.getReg_spaj());
						u.setTanggal(new Date());
						u.setJenis("EDIT TITIPAN PREMI");
						u.setLus_id(Integer.valueOf(currentUser.getLus_id()));
						u.setKeterangan(ket.toString());
						insertLstUlangan(u);
						
					}
				}
				
			}
		}
		return "Data Titipan Premi Berhasil Diupdate";
	}
	*/
	
	public String editDataPowersave(Map params, User currentUser) {
		String vJenis = (String) params.get("jenis");
		String vMgi = (String) params.get("mgi");
		String vRollover = (String) params.get("rollover");
		String vRate = (String) params.get("rate");
		String vBegdate = (String) params.get("begdate");
		String vPremi = (String) params.get("premi");
		String vDesc = (String) params.get("desc");
		
		logger.info(vJenis);
		logger.info(vMgi);
		logger.info(vRollover);
		logger.info(vRate);
		logger.info(vBegdate);
		logger.info(vPremi);
		logger.info(vDesc);
		
		if("Ubah MGI".equals(vJenis)) {
			
			return "Fungsi ini belum selesai dibuat. Harap hubungi IT";
			
		}else if("Ubah Rollover".equals(vJenis)) {
			
			return "Fungsi ini belum selesai dibuat. Harap hubungi IT";
			
		}else if("Ubah Rate".equals(vJenis)) {
			
			return "Fungsi ini belum selesai dibuat. Harap hubungi IT";
			
		}else if("Ubah Beg Date".equals(vJenis)) {
			
			return "Fungsi ini belum selesai dibuat. Harap hubungi IT";
			
		}else if("Ubah Premi".equals(vJenis)) {
			
			return "Fungsi ini belum selesai dibuat. Harap hubungi IT";
			
		}else {
			return "Harap pilih salah satu jenis edit data!";
		}
	}
	
}