package com.ekalife.elions.process;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.parent.ParentDao;

public class Nab extends ParentDao {

	public List prosesHitungUnit(int pos, String startDate, String endDate, String nabDate, User user) throws DataAccessException {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		List errors = new ArrayList();
		String filter=null;
		Date ldt_nab=null;
		Integer li_belum=0;
		Integer li_belum2=0;

		try {
			ldt_nab = defaultDateFormatReversed.parse(nabDate);
		}catch(Exception e) {
			errors.add("Harap masukkan tanggal yang benar.");
		}
		
		if(pos==43)filter= "( mtu_tgl_trans = to_date("+startDate+",'dd/mm/yyyy') and to_char(mtu_tgl_input,'hh') > '14:00' ) or ( mtu_tgl_trans > to_date("+startDate+",'dd/mm/yyyy') and mtu_tgl_trans < to_date("+nabDate+",'dd/mm/yyyy') ) or ( mtu_tgl_trans = to_date("+nabDate+",'dd/mm/yyyy') and to_char(mtu_tgl_input,'hh') <= '14:00' )";
		else if(pos==47) {
			if(user.getLde_id().equals("15")){//Finance proses topup
				filter = "lt_id in (2,6) and ( mtu_tgl_trans = to_date("+startDate+",'dd/mm/yyyy') and to_char(mtu_tgl_input,'hh') > '14:00' ) or ( mtu_tgl_trans > to_date("+startDate+",'dd/mm/yyyy') and mtu_tgl_trans < to_date("+nabDate+",'dd/mm/yyyy') ) or ( mtu_tgl_trans = to_date("+nabDate+",'dd/mm/yyyy') and to_char(mtu_tgl_input,'hh') <= '14:00' )";
			}else if(user.getLde_id().equals("12")){// CS proses w-draw
				filter = "lt_id in (3,7)";
			}else{
				filter="lt_id <> 4";
			}
		}
		
		List transUlink = this.uwDao.selectTransUlink(pos, startDate, endDate, filter);
		
		Date ldt_elock = FormatDate.add(FormatDate.add(ldt_nab, Calendar.YEAR, 1), Calendar.DATE, -1);
		
		for(int i=0; i<transUlink.size(); i++) {
			Map dw_1 = (HashMap) transUlink.get(i);
			int li_transaksi = ((Integer) dw_1.get("LT_ID"));
			String reg_spaj = (String) dw_1.get("REG_SPAJ");
			String jenis_invest = (String) dw_1.get("LJI_ID");
			Date tgl_trans = (Date) dw_1.get("MTU_TGL_TRANS");
			if(Common.isEmpty(tgl_trans)){
				tgl_trans= FormatDate.stringToDate(nabDate);
			}
			int type_nab = 0;
			Date tgl_nab = ldt_nab;
//			boolean get_tgl = false;
			double harga = this.uwDao.selectNabUlink(errors, jenis_invest, type_nab, defaultDateFormatReversed.format(tgl_nab), defaultDateFormatReversed.format(tgl_trans));
			if(harga==0) {
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				return errors;
			}
			int li_ke = ((Integer) dw_1.get("MU_KE"));
			String ls_dk = (String) dw_1.get("MTU_DK");
			int th_ke = ((Integer) dw_1.get("MTU_TAHUN_KE"));
			
			Map dataTertanggung = uwDao.selectTertanggung(reg_spaj);
			Integer lspd_id = (Integer) dataTertanggung.get("LSPD_ID");
			Date mste_tgl_kirim_polis = (Date) dataTertanggung.get("MSTE_TGL_KIRIM_POLIS");
			
			if(pos==52){
				if(lspd_id < 7 && Common.isEmpty(mste_tgl_kirim_polis)){
					li_belum2++;
				}
			}
			
			int counttransulink = uwDao.selectCountTransUlink(reg_spaj, li_ke);
			if(counttransulink>0){
				li_belum ++;
			}
			
			Map ld_saldo = this.uwDao.selectSaldoUlink(reg_spaj, jenis_invest);
			Double mdu_saldo_unit = (Double)  ld_saldo.get("MDU_SALDO_UNIT");
			Double mdu_saldo_unit_pp = (Double) ld_saldo.get("MDU_SALDO_UNIT_PP");
			Double mdu_saldo_unit_tu = (Double) ld_saldo.get("MDU_SALDO_UNIT_TU");
			
			if(Common.isEmpty(mdu_saldo_unit))mdu_saldo_unit=0.;
			if(Common.isEmpty(mdu_saldo_unit_pp))mdu_saldo_unit_pp=0.;
			if(Common.isEmpty(mdu_saldo_unit_tu))mdu_saldo_unit_tu=0.;	
				
			int li_pos=pos;
			if(pos==43) li_pos = 44;
			else if(pos==47) {
				li_pos=48;
				if(li_transaksi==3) li_pos = 54;
				if(li_transaksi==7) li_pos = 49;
			}else if(pos==52) li_pos = 53;
			else if(pos==62) li_pos = 63; //potongan
			double ldec_jumlah = ((Double) dw_1.get("MTU_JUMLAH")).doubleValue();
			boolean lb_input_unit = false;
			double ld_unit;
			if(ldec_jumlah==0) { //
				//Jika transaksi input unit, hitung jumlahnya
				ld_unit = ((Double) dw_1.get("MTU_UNIT")).doubleValue();
				if(li_transaksi==3){
					if(Math.abs(ld_unit)> Math.abs(mdu_saldo_unit)){
						
					}
				}
				
				ldec_jumlah = Math.abs(FormatNumber.round(ld_unit * harga, 2));
				dw_1.put("MTU_JUMLAH", new Double(ldec_jumlah));
				lb_input_unit = true;
			}else {
				//ganti per 15/10/03 (hm)
				ld_unit = FormatNumber.round( ldec_jumlah / harga, 4);
			}
			if(ls_dk.equals("K") && !lb_input_unit) ld_unit *=-1;
			mdu_saldo_unit += ld_unit;
			if(li_transaksi==2 || li_transaksi==5){
				mdu_saldo_unit_tu += ld_unit;
			}else if(li_transaksi==7){
				mdu_saldo_unit_tu += ld_unit;
				if(mdu_saldo_unit_tu< 0){
					mdu_saldo_unit_pp += mdu_saldo_unit_tu;
					mdu_saldo_unit_tu=0.;
				}
			}else{
				mdu_saldo_unit_pp += ld_unit;
			}
			dw_1.put("MTU_SALDO_UNIT", new Double(mdu_saldo_unit));
			dw_1.put("MTU_UNIT", new Double(ld_unit));
			dw_1.put("LSPD_ID", new Integer(li_pos));
			dw_1.put("MTU_TGL_NAB", tgl_nab);
			dw_1.put("MTU_NAB", new Double(harga));
			dw_1.put("MTU_TGL_PROSES", "sysdate");
			dw_1.put("MTU_SALDO_UNIT_PP", new Double(mdu_saldo_unit_pp));
			dw_1.put("MTU_SALDO_UNIT_TU", new Double(mdu_saldo_unit_tu));
			
			if(this.uwDao.updatePosisiUlink(li_pos, reg_spaj, li_ke)==0) {
				errors.add("Gagal dalam pengubahan posisi unit-link. Harap hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				return errors;
			}
			if(this.uwDao.updateSaldoUlink(mdu_saldo_unit,mdu_saldo_unit_pp, mdu_saldo_unit_tu, reg_spaj, li_ke, jenis_invest)==0) {
				errors.add("Gagal dalam pengubahan saldo unit-link. Harap hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				return errors;
			}
			if(this.uwDao.updateNabUlink(dw_1)==0) {
				errors.add("Gagal dalam pengubahan status unit-link. Harap hubungi ITwebandmobile@sinarmasmsiglife.co.id.");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				return errors;
			}
			
			int li_lock = ((Integer) dw_1.get("MTU_FLAG_LOCK"));
			//top-up platinum
			if(li_transaksi == 6 || li_lock==1) {
				int li_mtu = ((Integer) dw_1.get("MTU_KE"));
				if(ls_dk.equals("D")) {
					this.uwDao.insertUlinkPlatinum(reg_spaj, li_ke, li_mtu, ldt_nab, ldt_elock, ldec_jumlah, ld_unit, mdu_saldo_unit);
				}
			}
		}
		
		return errors;
	}
	
}
