package com.ekalife.elions.process;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import javax.servlet.ServletException;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import com.ekalife.elions.model.Stamp;
import com.ekalife.elions.model.User;


import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentDao;

public class SavingBeaMeterai extends ParentDao{

	
	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.uw.";
	}

		// proses submit	
		public Stamp insertbeameterai( Stamp edit,User currentUser) throws ServletException,IOException
		{
			String bulan_indeks="";
			try{
				String yy1,mm1,dd1,yy2,mm2,dd2;
				String tanggal1 = edit.getTanggal1();
				String tanggal2 = edit.getTanggal2();
				String tgl1 = edit.getTgl1();
				String tgl2 = edit.getTgl2();
				
				if (tanggal1!=null)
				{
					tanggal1=tanggal1.replaceAll("/","");
					yy1=tanggal1.substring(4);
					mm1=tanggal1.substring(2,4);
					dd1=tanggal1.substring(0,2);
					tanggal1=yy1+mm1;
					bulan_indeks=mm1;
				}else{
					tanggal1 = "";
					tgl1 = "";
				}
				
				edit.setTanggal1(tanggal1);
				tanggal2 = tgl2.replaceAll("-","");
				yy2=tanggal2.substring(0,4);
				mm2=tanggal2.substring(4,6);
				dd2=tanggal2.substring(6);
				tanggal2=yy2+mm2;
				edit.setTanggal2(tanggal2);
				Double saldo_akhir= (Double) this.uwDao.bulan_stamp(tanggal2);
				if (saldo_akhir == null)
				{
					saldo_akhir= new Double(0);
				}
				Double saldo = edit.getSaldo();
				Double jumlah_saldo = new Double(saldo.doubleValue() + saldo_akhir.doubleValue());
				edit.setMstm_saldo_awal(jumlah_saldo);
				edit.setMstm_saldo_akhir(jumlah_saldo);
				edit.setMsth_jumlah(saldo);
				
				Integer count_bea_materai = (Integer)this.uwDao.count_kode_bea_meterai(tanggal1);
						
				if (count_bea_materai.intValue() ==0)
				{
					DecimalFormat f3 = new DecimalFormat("000");
					String noSurat=f3.format(Integer.parseInt(this.uwDao.selectGetCounter(72 , "01")));
					this.uwDao.updateMstCounter(Double.valueOf(noSurat), 72 , "01");
					Date sysdate=this.commonDao.selectSysdate();
					String mm=FormatDate.getMonth(sysdate);
					String yy=FormatDate.getYearFourDigit(sysdate);
					String indeks = "";

					switch (Integer.parseInt(bulan_indeks))
					{
						case 1:
							indeks = "I";
							break;
						case 2:
							indeks = "II";
							break;
						case 3:
							indeks="III";
							break;
						case 4:
							indeks="IV";
							break;
						case 5:
							indeks="V";
							break;
						case 6:
							indeks="VI";
							break;
						case 7:
							indeks="VII";
							break;
						case 8:
							indeks="VIII";
							break;
						case 9:
							indeks="IX";
							break;
						case 10:
							indeks="X";
							break;
						case 11:
							indeks="XI";
							break;
						case 12:
							indeks="XII";
							break;
					}
					noSurat=noSurat+"/SRT-UND-MT/"+indeks+"/"+yy;
					edit.setMstm_kode(noSurat);
					edit.setMstm_bulan(tanggal1);
					edit.setMstm_kode_dirjen(null);
					edit.setMstm_create_dt(sysdate);
					String lus_id = currentUser.getLus_id();
					edit.setMstm_lus_id(Integer.parseInt(lus_id));
					edit.setMsth_ref_no("-");
					edit.setMsth_jenis(new Integer(0));
					edit.setMsth_desc("SURAT PERMOHONAN BEA METERAI");
					edit.setStatus("insert");
					this.uwDao.insertmststamp(edit);
					this.uwDao.insertmststamp_hist(edit);
				}else{
					edit.setStatus("batal");
				}
			} catch (Exception e){
				edit.setStatus("gagal");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				logger.error("ERROR :", e);
			}
			 return edit;
		}
		
		
		// proses submit	
		public Stamp updatebeameterai( Stamp edit,User currentUser) throws ServletException,IOException
		{
			try{
				Double saldo_akhir= edit.getMstm_saldo_akhir();
				if (saldo_akhir == null)
				{
					saldo_akhir= new Double(0);
				}
				Double saldo = edit.getSaldo();
				Double jumlah_saldo = new Double(saldo.doubleValue() + saldo_akhir.doubleValue());
				edit.setMstm_saldo_akhir(jumlah_saldo);
				edit.setMsth_jumlah(saldo);
				String kode_dirjen = edit.getMstm_kode_dirjen();
				if (kode_dirjen == null)
				{
					kode_dirjen="-";
				}
				edit.setMsth_ref_no(kode_dirjen);
				edit.setMsth_jenis(new Integer(1));
				edit.setMsth_desc("SETORAN PEMBUBUHAN BEA METERAI");
				
				//if ( !kode_dirjen.equalsIgnoreCase(""))
				//{
					edit.setStatus("insert");
					this.uwDao.update_mst_stamp(edit);
					this.uwDao.insertmststamp_hist(edit);
				/*}else{
					edit.setStatus("batal");
				}*/
			} catch (Exception e){
				edit.setStatus("gagal");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				logger.error("ERROR :", e);
			}
			 return edit;
		}
		
}
