package com.ekalife.elions.process;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.ekalife.elions.dao.RekruitmentDao;
import com.ekalife.elions.model.Kuesioner;
import com.ekalife.elions.model.KuesionerTanggungan;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentDao;

import id.co.sinarmaslife.std.spring.util.Email;

@SuppressWarnings({ "unchecked", "deprecation" })
public class SavingRekruitment extends ParentDao{
	private Email email;
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private long accessTime;
	private RekruitmentDao rekruitmentDao;
	public long getAccessTime() { return accessTime;}
	public void setAccessTime(long accessTime) {this.accessTime = accessTime;}
	public RekruitmentDao getRekruitmentDao() { return rekruitmentDao;  }
	public void setRekruitmentDao(RekruitmentDao rekruitmentDao) { this.rekruitmentDao = rekruitmentDao;  }	
	
	public void setEmail(Email email) {
		this.email = email;
	}
	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.rekruitment.";
	}

		
		// proses submit	
		public Kuesioner insertkuesionerbaru(Object cmd, User currentUser) throws ServletException,IOException
		{
			Kuesioner edit= (Kuesioner) cmd;
			Date nowDate = commonDao.selectSysdate();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String mku_no_reg=null;
			try {
				Calendar tgl_sekarang = Calendar.getInstance();
				Integer bln1 = tgl_sekarang.getTime().getMonth()+1;
				Integer thn1 = tgl_sekarang.getTime().getYear()+1900;
				String tahun = Integer.toString(thn1.intValue());
				tahun = tahun.substring(2, 4);
				String waktu = FormatString.rpad("0",Integer.toString(bln1.intValue()),2)+FormatString.rpad("0",tahun ,2);
				String region = edit.getMku_regional();
				String lca_id = region.substring(0,2);
				String kunci = lca_id + waktu; // lca_id | bulan | tahun
				String no_reg = null;

				Integer juml_x = edit.getJuml_daftarTanggungan();
				if(juml_x==null){
					juml_x=0;
				}

				no_reg = (String) querySingle("no_reg",lca_id); //ubah pembuat nomor register agen ismile //chandra
				/*String max = (String) querySingle("no_reg",kunci);
				
				if (max == null)
				{
					max = "";
				}
				if (max.equalsIgnoreCase(""))
				{
					Integer no = new Integer(1);
					no_reg = kunci+FormatString.rpad("0",Integer.toString(no.intValue()),4);
				}else{
					
					//Integer nilai =Integer.parseInt(max);
					String register = max.substring(6,10);
					Integer count1 = Integer.parseInt(register);
					count1 = new Integer(count1.intValue() + 1);
					register = FormatString.rpad("0",Integer.toString(count1.intValue()),4);
					no_reg = FormatString.rpad("0",kunci+register,10);
					//no_reg = FormatString.rpad("0",Integer.toString(nilai.intValue()+1),10);
				}*/
				edit.setMku_no_reg(no_reg);
				edit.setMsag_id(edit.getCurrentUser().getMsag_id_ao());
				edit.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				proc_save_kuesioner (edit);
				edit.setKeterangan("Data Rekruitment telah berhasil disimpan");
				
				if (juml_x.intValue()>=0)
				{
					proc_save_tanggungan_kuesioner(edit);
				}
				
			} catch (Exception e){
				mku_no_reg="";
				edit.setMku_no_reg(mku_no_reg);
				edit.setKeterangan("Data Rekruitment gagal disimpan");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				logger.error("ERROR :", e);
				
				String desc = "ERROR";
				logger.error("ERROR :", e);
				String err=e.getLocalizedMessage();
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, nowDate, null, 
						true, "ajsjava@sinarmasmsiglife.co.id", 
						null,
						null,
						new String[]{"ryan@sinarmasmsiglife.co.id","alfian_h@sinarmasmsiglife.co.id"}, 
						"[E-Lions] Error Saving Rekruitment", 
						"Ada Error saat menjalankan simpan data rekruitment, Terlampir Pesannya : <br><br>"+Common.getRootCause(e).getMessage(),
						null, null);
				
				/*try {
					email.send(true, "ajsjava@sinarmasmsiglife.co.id", new String[]{"alfian_h@sinarmasmsiglife.co.id"}, null, null, "Error", "Error : <br><br>"+Common.getRootCause(e).getMessage(), null);
				} catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
				}*/
			}
			return edit;
		}
		
		public Kuesioner editkuesionerbaru(Object cmd, User currentUser) throws ServletException,IOException
		{
			Kuesioner edit= (Kuesioner) cmd;
			Date nowDate = commonDao.selectSysdate();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//			String mku_no_reg=null;
			try {
				Calendar tgl_sekarang = Calendar.getInstance();
				Integer bln1 = tgl_sekarang.getTime().getMonth()+1;
				Integer thn1 = tgl_sekarang.getTime().getYear()+1900;
				String waktu = FormatString.rpad("0",Integer.toString(bln1.intValue()),2)+FormatString.rpad("0",Integer.toString(thn1.intValue()),2);
				String region = edit.getMku_regional();
				String lca_id = region.substring(0,2);
				String kunci = lca_id + waktu;
				String no_reg = null;
				no_reg = (String) querySingle("no_reg",lca_id); //ubah pembuat nomor register agen ismile //chandra
				/*Double max = (Double) querySingle("no_reg",kunci);
				if (max ==null)
				{
					Integer no = new Integer(1);
					no_reg = kunci+FormatString.rpad("0",Integer.toString(no.intValue()),4);
				}else{
					no_reg = FormatString.rpad("0",Double.toString(max.doubleValue()+1),10);
				}*/
				edit.setMku_no_reg(no_reg);
				edit.setMsag_id(edit.getCurrentUser().getMsag_id_ao());
				edit.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				edit.setKeterangan("Data Rekruitment telah berhasil disimpan");
				
				Integer juml_x = edit.getJuml_daftarTanggungan();
				if (juml_x.intValue()>=0)
				{
					proc_save_tanggungan_kuesioner(edit);
				}
				
			} catch (Exception e){
				//mku_no_reg="";
				///edit.setMku_no_reg(mku_no_reg);
				edit.setKeterangan("Data Rekruitment gagal disimpan");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
				logger.error("ERROR :", e);
				
				String desc = "ERROR";
				logger.error("ERROR :", e);
				String err=e.getLocalizedMessage();
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, nowDate, null, 
						true, "ajsjava@sinarmasmsiglife.co.id", 
						null,
						null,
						new String[]{"ryan@sinarmasmsiglife.co.id","alfian_h@sinarmasmsiglife.co.id"}, 
						"[E-Lions] Error Saving Rekruitment", 
						"Ada Error saat menjalankan simpan data rekruitment, Terlampir Pesannya : <br><br>"+Common.getRootCause(e).getMessage(),
						null, null);
			}
//			edit.setMku_no_reg(mku_no_reg);
			return edit;
		}
			
		private void proc_save_kuesioner (Kuesioner edit)throws ServletException,IOException
		{	
			insert("insert_mst_kuesioner",edit);
		}
		
		private void proc_save_tanggungan_kuesioner (Kuesioner edit)throws ServletException,IOException
		{	
			List listTanggungan = edit.getDaftarTanggungan();
			
			Integer listTanggunga_x = new Integer (0);
			Integer NoUrut = new Integer (0);
			
			if(listTanggungan == null){
				listTanggunga_x = 0;
			}else{
				listTanggunga_x = listTanggungan.size();
			}
			
			Integer juml_x = edit.getJuml_daftarTanggungan();
			
			if(listTanggunga_x > 0){
				for (int i=0; i<listTanggunga_x.intValue(); i++){
					NoUrut+=1;
//					PesertaPlus_mix plus2 = (PesertaPlus_mix)ptx_mix.get(i);
					KuesionerTanggungan kt = (KuesionerTanggungan)listTanggungan.get(i);
					
					kt.setMku_no_reg(edit.getMku_no_reg());
					kt.setUrut(NoUrut);
					kt.setNama(kt.getMkt_nama());
//					kt.setMkt_jenis(kt.getMkt_jenis());
					kt.setTgl_lhr(kt.getMkt_tgl_lahir());
					kt.setTempat_lhr(kt.getMkt_tempat_lahir());
					kt.setPendidikan(kt.getMkt_pendidikan());
					kt.setUmur(kt.getMkt_umur());
					kt.setRelasi(kt.getMkt_hubungan());
					
//					logger.info(kt.getMkt_tgl_lahir());
//					logger.info(kt.getMkt_pendidikan());
//					logger.info(kt.getMkt_umur());
					
//					rekruitmentDao.insert_mst_tanggungan_ku(kt);
					insert("insert_mst_tanggungan_ku",kt);
			}
			
//				if (listTanggunga_x.intValue() >0)
//				{
//					for (int i=0; i<listTanggunga_x.intValue();i++)
//					{
//						KuesionerTanggungan kt = (KuesionerTanggungan)listTanggungan.get(i);
////						bacDao.insert_mst_peserta_plus_mix(kt2);
//						rekruitmentDao.insert_mst_tanggungan_ku(kt);
//					}
//				}
		}
		}
	
//		Calendar tgl_sekarang = Calendar.getInstance();
//		Integer bln1 = tgl_sekarang.getTime().getMonth()+1;
//		Integer thn1 = tgl_sekarang.getTime().getYear()+1900;
//		String tahun = Integer.toString(thn1.intValue());
//		tahun = tahun.substring(2, 4);
//		String waktu = FormatString.rpad("0",Integer.toString(bln1.intValue()),2)+FormatString.rpad("0",tahun ,2);
//		String region = edit.getMku_regional();
//		String lca_id = region.substring(0,2);
//		String kunci = lca_id + waktu; // lca_id | bulan | tahun
//		String max = (String) querySingle("no_reg",kunci);
//		if (max == null)
//		{
//			max = "";
//		}
//		String no_reg = null;
//		if (max.equalsIgnoreCase(""))
//		{
//			Integer no = new Integer(1);
//			no_reg = kunci+FormatString.rpad("0",Integer.toString(no.intValue()),4);
//		}else{
//			
//			//Integer nilai =Integer.parseInt(max);
//			String register = max.substring(6,10);
//			Integer count1 = Integer.parseInt(register);
//			count1 = new Integer(count1.intValue() + 1);
//			register = FormatString.rpad("0",Integer.toString(count1.intValue()),4);
//			no_reg = FormatString.rpad("0",kunci+register,10);
//			//no_reg = FormatString.rpad("0",Integer.toString(nilai.intValue()+1),10);
//		}

	
	
		
}
