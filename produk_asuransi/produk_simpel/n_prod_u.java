package produk_asuransi.produk_simpel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;

import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.TmSales;
import com.ekalife.elions.model.Upload;
import com.ekalife.utils.Common;

/**
 * Produk yang menggunakan Upload, ex: PaPartnerUploadFormController
 * 
 * @author : Adrian
 * @since : Jan 21, 2015
 */
public class n_prod_u implements Serializable {
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	//inisialisasi pertama null
	Pas agencyExcel;
	TmSales tmSales;
	
	public void n_produk_u(){			
		
	}
	
	public n_prod_u(){
		agencyExcel= new Pas();
		tmSales= new TmSales();
	}
	
	public Pas set_prod_u(List rowAgencyExcelList ){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				
		agencyExcel.setMsp_fire_code_name("");
		agencyExcel.setMsp_fire_name("");
		agencyExcel.setMsp_fire_identity("");
		agencyExcel.setMsp_fire_date_of_birth2("");
		agencyExcel.setMsp_fire_date_of_birth(null);
		agencyExcel.setMsp_fire_occupation2("");
		agencyExcel.setMsp_fire_occupation("");
		agencyExcel.setMsp_fire_type_business2("");
		agencyExcel.setMsp_fire_type_business("");
		agencyExcel.setMsp_fire_source_fund2("");
		agencyExcel.setMsp_fire_source_fund("");
		agencyExcel.setMsp_fire_addr_code("");
		agencyExcel.setMsp_fire_address_1("");
		agencyExcel.setMsp_fire_postal_code("");
		agencyExcel.setMsp_fire_phone_number("");
		agencyExcel.setMsp_fire_mobile("");
		agencyExcel.setMsp_fire_email("");
		agencyExcel.setMsp_fire_insured_addr_code("");
		agencyExcel.setMsp_fire_insured_addr("");
		agencyExcel.setMsp_fire_insured_addr_no("");
		agencyExcel.setMsp_fire_insured_postal_code("");
		agencyExcel.setMsp_fire_insured_city("");
		agencyExcel.setMsp_fire_insured_phone_number("");
		agencyExcel.setMsp_fire_insured_addr_envir(null);
		agencyExcel.setMsp_fire_ins_addr_envir_else("");
		agencyExcel.setMsp_fire_okupasi("");
		agencyExcel.setMsp_fire_okupasi_else("");
		agencyExcel.setMsp_fire_beg_date(null);
		agencyExcel.setMsp_fire_end_date(null);
	//
		if(agencyExcel.getReg_spaj() == null)agencyExcel.setReg_spaj("");
		if(agencyExcel.getMsp_warga_else() == null)agencyExcel.setMsp_warga_else("");
		if(agencyExcel.getMsp_pendidikan_else() == null)agencyExcel.setMsp_pendidikan_else("");
		if(agencyExcel.getMsp_occupation_else() == null)agencyExcel.setMsp_occupation_else("");
		if(agencyExcel.getMsp_company_name() == null)agencyExcel.setMsp_company_name("");
		if(agencyExcel.getMsp_company_jabatan() == null)agencyExcel.setMsp_company_jabatan("");
		if(agencyExcel.getMsp_company_address() == null)agencyExcel.setMsp_company_address("");
		if(agencyExcel.getMsp_company_postal_code() == null)agencyExcel.setMsp_company_postal_code("");
		if(agencyExcel.getMsp_no_rekening_autodebet()== null)agencyExcel.setMsp_no_rekening_autodebet("");
		if(agencyExcel.getMsp_rek_nama_autodebet()== null)agencyExcel.setMsp_rek_nama_autodebet("");
		if(agencyExcel.getMsp_area_code_rumah()== null)agencyExcel.setMsp_area_code_rumah("");
		
		agencyExcel.setMsp_kode_sts_sms("00");
		agencyExcel.setDist("05");
		agencyExcel.setLspd_id(1);
		agencyExcel.setLssp_id(10);//POLICY IS BEING PROCESSED 
		agencyExcel.setMsp_flag_pas(1);
		agencyExcel.setMsp_flag_aksep(0);
		
		agencyExcel.setFlag_val_send(1);
		//Bukti Identitas - KTP
		agencyExcel.setLside_id(1);			
		agencyExcel.setMsp_mobile(rowAgencyExcelList.get(12).toString().trim().replace(".00", "").replace(".0", ""));	
		agencyExcel.setMsp_identity_no(rowAgencyExcelList.get(11).toString().trim().replace(".00", "").replace(".0", ""));					
				
		try{							
			Date msp_date_of_birth = df.parse(rowAgencyExcelList.get(16).toString());					
			agencyExcel.setMsp_date_of_birth(msp_date_of_birth);
		}
		catch (Exception e) {
			logger.error("ERROR :", e);						
		}						
						
		agencyExcel.setMsag_id(rowAgencyExcelList.get(22).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setMsp_identity_no_tt(rowAgencyExcelList.get(17).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setMsp_full_name(rowAgencyExcelList.get(14).toString());
		
		//agencyExcel.setMsp_date_of_birth(new Date(agencyExcelList.get(i).get(16).toString()));					
		agencyExcel.setMsp_address_1(rowAgencyExcelList.get(8).toString());
		agencyExcel.setMsp_city(rowAgencyExcelList.get(9).toString());	
		agencyExcel.setMsp_postal_code(rowAgencyExcelList.get(10).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setMsp_no_rekening(rowAgencyExcelList.get(21).toString().trim().replace(".00", "").replace(".0", ""));	
		agencyExcel.setMsp_pas_tmp_lhr_tt(rowAgencyExcelList.get(15).toString());	
		agencyExcel.setMsp_pas_nama_pp(rowAgencyExcelList.get(1).toString());
		agencyExcel.setMsp_pas_tmp_lhr_pp(rowAgencyExcelList.get(3).toString());
		agencyExcel.setMsp_pas_email(rowAgencyExcelList.get(13).toString());	
		try{							
				Date msp_pas_dob_pp = df.parse(rowAgencyExcelList.get(4).toString());					
				agencyExcel.setMsp_pas_dob_pp(msp_pas_dob_pp);
		}
		catch (Exception e) {
			logger.error("ERROR :", e);						
		}						
		//agencyExcel.setMsp_rek_cabang(agencyExcelList.get(i).get(  ).toString());		
		agencyExcel.setKode_ao(rowAgencyExcelList.get(22).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setMsag_id_pp(rowAgencyExcelList.get(22).toString().trim().replace(".00", "").replace(".0", ""));	
						
		String JK = rowAgencyExcelList.get(2).toString().trim();				
		if(rowAgencyExcelList.get(2).toString().trim().equalsIgnoreCase("Pria") || JK.equalsIgnoreCase("P"))
			{   agencyExcel.setMsp_sex_pp(1);	
		} 
		if(rowAgencyExcelList.get(2).toString().trim().equalsIgnoreCase("Wanita") || JK.equalsIgnoreCase("W"))
			{   agencyExcel.setMsp_sex_pp(0);	
		} 
				
		//Set Pendidikan & Pekerjaan
		//agencyExcel.setMsp_pendidikan(Integer.parseInt(agencyExcelList.get(i).get(  ).toString()));
		//agencyExcel.setMsp_occupation(agencyExcelList.get(i).get(  ).toString());
		if(agencyExcel.getPribadi() == null)agencyExcel.setPribadi(0);
		if(agencyExcel.getMsp_cek_ktp() == null)agencyExcel.setMsp_cek_ktp(0);
		if(agencyExcel.getMsp_cek_kk() == null)agencyExcel.setMsp_cek_kk(0);
		if(agencyExcel.getMsp_cek_npwp() == null)agencyExcel.setMsp_cek_npwp(0);						
		if(agencyExcel.getMsp_cek_bukti_bayar() == null)agencyExcel.setMsp_cek_bukti_bayar(0);						
		if(agencyExcel.getMsp_cek_rekening() == null)agencyExcel.setMsp_cek_rekening(0);						
		if(agencyExcel.getMsp_cek_ktp_uw() == null)agencyExcel.setMsp_cek_ktp_uw(0);						
		if(agencyExcel.getMsp_cek_kk_uw() == null)agencyExcel.setMsp_cek_kk_uw(0);						
		if(agencyExcel.getMsp_cek_npwp_uw() == null)agencyExcel.setMsp_cek_npwp_uw(0);
		
		if(agencyExcel.getMsp_cek_bukti_bayar_uw() == null)agencyExcel.setMsp_cek_bukti_bayar_uw(0);
		if(agencyExcel.getMsp_cek_rekening_uw() == null)agencyExcel.setMsp_cek_rekening_uw(0);
		if(agencyExcel.getMsp_cek_srt_keterangan() == null)agencyExcel.setMsp_cek_srt_keterangan(0);
		if(agencyExcel.getMsp_cek_srt_keterangan_uw() == null) agencyExcel.setMsp_cek_srt_keterangan_uw(0);
		if(agencyExcel.getMsp_cek_akte_kelahiran() == null)agencyExcel.setMsp_cek_akte_kelahiran(0);
		if(agencyExcel.getMsp_cek_akte_kelahiran_uw() == null)agencyExcel.setMsp_cek_akte_kelahiran_uw(0);		
		if(agencyExcel.getMsp_fire_jenis() == null)agencyExcel.setMsp_fire_jenis(0);
		if(agencyExcel.getMsp_no_rekening()== null || agencyExcel.getMsp_no_rekening().trim().equals(""))agencyExcel.setMsp_no_rekening("-");
		if(agencyExcel.getLsbp_id()==null)agencyExcel.setLsbp_id("0");
		if(agencyExcel.getMsp_pas_phone_number()== null)agencyExcel.setMsp_pas_phone_number("");
		if(agencyExcel.getMsp_exist_bp()== null)agencyExcel.setMsp_exist_bp(0);
		if(agencyExcel.getMsp_pendidikan()== null)agencyExcel.setMsp_pendidikan(0);
		if(agencyExcel.getMsp_occupation()== null)agencyExcel.setMsp_occupation("LAINNYA");
		
		agencyExcel.setCf_job_code("");	
		agencyExcel.setCf_customer_id("");	
		agencyExcel.setCf_campaign_code("");
		
		return agencyExcel;
	}
	
	
	public String validate_prod_u(){
    	String err="";
    	
    	if(agencyExcel.getMsp_pas_beg_date()==null){
		  	err = err+ " Tanggal BegDate harus diisi,";
		}
    	if(Common.isEmpty(agencyExcel.getMsp_mobile())){
			err = err+ " No.HP PP harus diisi,";
		}else if(agencyExcel.getMsp_mobile().contains("E") || !Common.validPhone(agencyExcel.getMsp_mobile().trim()) ) {
			err = err+ " No.HP PP harus diisi angka dlm format 'Text',";
		}else if(!agencyExcel.getMsp_mobile().substring(0, 1).equals("0")) {
			err = err+ " No.HP harus diawali angka 0,";
		}else if(!Common.isEmpty(agencyExcel.getMsp_mobile())){
			try{
				String x = agencyExcel.getMsp_mobile().trim().replace("0", "").replace("-", "");
				if(Common.isEmpty(x)){
					err = err+ " PAS : No. HP tidak boleh diisi '0' atau '-',";
				}
			}catch(Exception e){
				err = err+ " PAS : No. HP tidak boleh diisi '0' atau '-',";
			}
		}
						
		if(Common.isEmpty(agencyExcel.getMsp_identity_no())){
			err = err+ " No.Identitas PP harus diisi,";
		}else if(agencyExcel.getMsp_identity_no().contains("E")) {
			err = err+ " No.Identitas PP harus diisi angka dlm format 'Text',";
		}		
//		if(Common.isEmpty(agencyExcel.getMsp_no_rekening())){
//			err = err+ " No.Rekening PP harus diisi,";
//		}else if(agencyExcel.getMsp_no_rekening().contains("E")) {
//			err = err+ " No.Rekening PP harus diisi angka dlm format 'Text',";
//		}
    	
		if(Common.isEmpty(agencyExcel.getMsp_identity_no_tt())){
			err = err+ " No.Identitas TT harus diisi,";
		}else if(agencyExcel.getMsp_identity_no_tt().contains("E")) {
			err = err+ " No.Identitas TT harus diisi angka dlm format 'Text',";
		}		
		if(agencyExcel.getMsp_postal_code()==null || agencyExcel.getMsp_postal_code()=="") {
			err = err+ " Kode Pos PP harus diisi,";
		}else {
	//	try{
	//		int x = Integer.parseInt( agencyExcel.getMsp_postal_code());
	//	}catch(Exception e){
	//		err = err+ " PAS : Kode Pos PP harus diisi angka,";
	//	}
		}
		if(agencyExcel.getMsp_pas_nama_pp()== null || agencyExcel.getMsp_pas_nama_pp()== "") err = err+ " Nama PP harus diisi,";
		if(agencyExcel.getJenis_pas().equals("AP/BP") ){	
			if(agencyExcel.getMsp_full_name()==null || agencyExcel.getMsp_full_name()=="") err = err+ " Nama TT harus diisi,";
		}
		if(agencyExcel.getMsp_sex_pp()==null) err = err+ " P/W PP harus diisi Format: Pria/Wanita,";
		if(agencyExcel.getMsp_pas_tmp_lhr_pp()==null || agencyExcel.getMsp_pas_tmp_lhr_pp()=="") err = err+ " Tempat lahir PP harus diisi,";
		if(agencyExcel.getMsp_pas_dob_pp()==null) err = err+ " Tanggal lahir PP harus diisi dgn Format: DD/mm/yyyy,";
		if(agencyExcel.getMsp_warga()==null) err = err+ " WargaNegara PP harus diisi sesuai format,";
		if(agencyExcel.getMsp_status_nikah()==null)  err = err+ " Status nikah PP harus diisi sesuai format,";
		if(agencyExcel.getLsag_id()==null ) err = err+ " Agama PP harus diisi sesuai format,";
		if(agencyExcel.getMsp_address_1()==null || agencyExcel.getMsp_address_1()=="") err = err+ " Alamat PP harus diisi,";
		if(agencyExcel.getMsp_city()==null || agencyExcel.getMsp_city()=="") err = err+ " Kota PP harus diisi,";															
		if(agencyExcel.getMsp_pas_tmp_lhr_tt()==null || agencyExcel.getMsp_pas_tmp_lhr_tt()=="") err = err+ " Tempat lahir TT harus diisi,";
		if(agencyExcel.getMsp_date_of_birth()==null) err = err+ " Tanggal lahir TT harus diisi dgn Format: DD/mm/yyyy,";
		if(agencyExcel.getMsp_flag_cc()==null) err = err+ " Cara Pembayaran PAS harus diisi dgn Format:'TUNAI/TABUNGAN/KARTU KREDIT',";
		if(agencyExcel.getLscb_id()==null) err = err+ " Bentuk Pembayaran PAS harus diisi dgn Format:'TAHUNAN/BULANAN/SEMESTERAN/TRIWULANAN',";
//	    if(agencyExcel.getLsbp_id()==null || agencyExcel.getLsbp_id()=="") err = err+ " Nama Bank PP harus diisi sesuai format,";
		
		if(!Common.isEmpty(agencyExcel.getMsp_pas_email())){
			try {
				InternetAddress.parse(agencyExcel.getMsp_pas_email().trim());
			} catch (AddressException e) {
				err = err+ " PAS: email PP tidak valid,";
			} finally {
				if(!agencyExcel.getMsp_pas_email().trim().toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
					err = err+ " PAS: email PP tidak valid,";
				}
			}
		}
		
    	
    return err;	
    }
	
}
