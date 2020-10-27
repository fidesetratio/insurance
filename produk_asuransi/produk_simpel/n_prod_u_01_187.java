package produk_asuransi.produk_simpel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.StringUtil;
/**
 * Produk Upload PAS Business Partner
 * @author : Adrian
 * @since : Jan 21, 2015
 */
public class n_prod_u_01_187 extends n_prod_u {
	
	protected final Log logger = LogFactory.getLog( getClass() );
		
	void main(){
		int x = getumur();
		//2
	}
		
	//override
	int getumur(){
		return 2;
	}
	public n_prod_u_01_187(){
		super();
	}

	
    public Pas set_prod_u_01_187_SMS(List rowAgencyExcelList ){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		
		agencyExcel.setLsre_id(0);
		agencyExcel.setMsp_pas_phone_number(rowAgencyExcelList.get(12).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setMsag_id(rowAgencyExcelList.get(23).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setMsp_identity_no_tt(rowAgencyExcelList.get(11).toString().trim().replace(".00", "").replace(".0", ""));
		
		try{							
			Date beg_date = df.parse(rowAgencyExcelList.get(26).toString());					
			agencyExcel.setMsp_pas_beg_date(beg_date);
		}
		catch (Exception e) {
			logger.error("ERROR :", e);						
		}
		
		//agencyExcel.setMsp_pas_beg_date(new Date(agencyExcelList.get(i).get(26).toString()));		
		agencyExcel.setNo_kartu(rowAgencyExcelList.get(24).toString());	
		agencyExcel.setMsp_fire_id(rowAgencyExcelList.get(25).toString());	
		agencyExcel.setMsp_kode_sts_sms("00");
		
		agencyExcel.setProduk(5);
		agencyExcel.setPremi("74000");	
		agencyExcel.setMsp_premi("74000");
		agencyExcel.setDist("05");
		agencyExcel.setLspd_id(1);
		agencyExcel.setLssp_id(10);//POLICY IS BEING PROCESSED 
		agencyExcel.setMsp_flag_pas(1);
		agencyExcel.setMsp_flag_aksep(0);
		agencyExcel.setMsp_admin_date(agencyExcel.getMsp_pas_beg_date());	
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
		Date debetDate = (Date) agencyExcel.getMsp_pas_beg_date().clone();
		debetDate.setMonth(debetDate.getMonth() + 6);
		debetDate.setDate(debetDate.getDate() - 1);
		agencyExcel.setMsp_tgl_debet(debetDate);
		
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
		//if(!jenis_pas.equals("PAS SYARIAH") ){
		if(agencyExcel.getMsp_rek_kota() == null)agencyExcel.setMsp_rek_kota("-");
		if(agencyExcel.getMsp_rek_cabang() == null)agencyExcel.setMsp_rek_cabang("-");							
		if(agencyExcel.getLsbp_id()==null){
			agencyExcel.setMsp_rek_nama("-");
		}else{
			agencyExcel.setMsp_rek_nama(agencyExcel.getMsp_pas_nama_pp());
		}
		if(agencyExcel.getLsbp_id_autodebet()== null)agencyExcel.setLsbp_id_autodebet("0");				
		//}
		if(agencyExcel.getLsbp_id()==null)agencyExcel.setLsbp_id("0");
		if(agencyExcel.getMsp_pas_phone_number()== null)agencyExcel.setMsp_pas_phone_number("");
		if(agencyExcel.getMsp_exist_bp()== null)agencyExcel.setMsp_exist_bp(0);
		if(agencyExcel.getMsp_pendidikan()== null)agencyExcel.setMsp_pendidikan(0);
		if(agencyExcel.getMsp_occupation()== null)agencyExcel.setMsp_occupation("LAINNYA");
		
		return agencyExcel;
	}
    
    
    public Pas set_prod_u_01_187_Card(List rowAgencyExcelList ){
    	//No.Kartu BP-Card
    	agencyExcel.setNo_kartu(rowAgencyExcelList.get(23).toString());
    	return agencyExcel;
    }
	
    
    
    public Pas set_prod_u_01_187(List rowAgencyExcelList ){
		
    	//panggil parent krn extend : Andy
		agencyExcel = set_prod_u(rowAgencyExcelList);
				
		agencyExcel.setLsre_id(1);
		agencyExcel.setLscb_id(3);
    	
		agencyExcel.setMsag_id(rowAgencyExcelList.get(22).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setMsp_identity_no_tt(rowAgencyExcelList.get(17).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setMsp_full_name(rowAgencyExcelList.get(14).toString());
    	
		agencyExcel.setProduk(5);
		agencyExcel.setPremi("74000");	
		agencyExcel.setMsp_premi("74000");
		
		//if(!jenis_pas.equals("PAS SYARIAH") ){
		if(agencyExcel.getMsp_rek_kota() == null)agencyExcel.setMsp_rek_kota("-");
		if(agencyExcel.getMsp_rek_cabang() == null)agencyExcel.setMsp_rek_cabang("-");							
		if(agencyExcel.getLsbp_id()==null){
			agencyExcel.setMsp_rek_nama("-");
		}else{
			agencyExcel.setMsp_rek_nama(agencyExcel.getMsp_pas_nama_pp());
		}
		if(agencyExcel.getLsbp_id_autodebet()== null)agencyExcel.setLsbp_id_autodebet("0");	
		//}
		
		return agencyExcel;
	}
	
  public Pas set_prod_u_01_187_SAC(List rowAgencyExcelList ){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		//panggil parent krn extend : Andy
		agencyExcel = set_prod_u(rowAgencyExcelList);
		
		try{									
			Date msp_pas_beg_date = df.parse(rowAgencyExcelList.get(30).toString());					
			agencyExcel.setMsp_pas_beg_date(msp_pas_beg_date);
		}
		catch (Exception e) {
			logger.error("ERROR :", e);						
		}
		Date end_date = (Date) agencyExcel.getMsp_pas_beg_date().clone();
		end_date.setYear(end_date.getYear()+1);
		end_date.setDate(end_date.getDate()-1);
		agencyExcel.setMsp_pas_end_date(end_date);
		
		agencyExcel.setMsp_admin_date(agencyExcel.getMsp_pas_beg_date());
		
		Date debetDate = (Date) agencyExcel.getMsp_pas_beg_date().clone();
		debetDate.setMonth(debetDate.getMonth() + 6);
		debetDate.setDate(debetDate.getDate() - 1);
		agencyExcel.setMsp_tgl_debet(debetDate);
			
		agencyExcel.setMsp_flag_cc(Integer.parseInt(rowAgencyExcelList.get(18).toString().trim().replace(".00", "").replace(".0", "")));
		agencyExcel.setLscb_id(Integer.parseInt(rowAgencyExcelList.get(19).toString().trim().replace(".00", "").replace(".0", "")));
		agencyExcel.setLsre_id(Integer.parseInt(rowAgencyExcelList.get(25).toString().trim().replace(".00", "").replace(".0", "")));	
		agencyExcel.setMsp_pas_phone_number(rowAgencyExcelList.get(24).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setMsp_rek_cabang(rowAgencyExcelList.get(26).toString().trim());
		agencyExcel.setMsp_rek_kota(rowAgencyExcelList.get(27).toString().trim());
		agencyExcel.setMsp_rek_nama(rowAgencyExcelList.get(28).toString().trim());
		agencyExcel.setLsbp_id(rowAgencyExcelList.get(20).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setProduct_code("187");
		agencyExcel.setProduk(Integer.parseInt(rowAgencyExcelList.get(29).toString().trim().replace(".00", "").replace(".0", "")));
		agencyExcel.setApplication_id(rowAgencyExcelList.get(31).toString().trim().replace(".00", "").replace(".0", ""));
		
		//Set CREDIT-CARD/ REK-AUTODEBET 
		agencyExcel.setLsbp_id_autodebet(rowAgencyExcelList.get(32).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setMsp_no_rekening_autodebet(rowAgencyExcelList.get(33).toString().trim().replace(".00", "").replace(".0", ""));
		Date cc_valid_date=null;
		try{
			cc_valid_date = df.parse(rowAgencyExcelList.get(34).toString());
		} catch (Exception e) {
			logger.error("ERROR :", e);						
		}
		agencyExcel.setMsp_tgl_valid(cc_valid_date);
		agencyExcel.setMsp_rek_nama_autodebet(rowAgencyExcelList.get(35).toString().trim());

		agencyExcel.setCf_job_code(rowAgencyExcelList.get(36).toString().trim());	
		agencyExcel.setCf_customer_id(rowAgencyExcelList.get(37).toString().trim());	
		agencyExcel.setCf_campaign_code(rowAgencyExcelList.get(38).toString().trim());	
		agencyExcel.setCif(rowAgencyExcelList.get(39).toString().trim());
		
		try{
			agencyExcel.setTm_id(rowAgencyExcelList.get(40).toString().trim()); // insert ke mst_poicy.mspo_no_kerjasama
			agencyExcel.setSpv_id(rowAgencyExcelList.get(41).toString().trim()); 
			int flagKpr = 0;
			if(rowAgencyExcelList.size() > 42){
				if(rowAgencyExcelList.get(42) != null)
					flagKpr = (rowAgencyExcelList.get(42) == "" ? 0 : Integer.parseInt(rowAgencyExcelList.get(42).toString()));
			}
			agencyExcel.setFlag_kpr(flagKpr); //tambah kolom baru. helpdesk[132502] //chandra
		}catch (Exception e) {
			logger.error("ERROR :", e);						
		}
		
		//No.kartu
		agencyExcel.setNo_kartu(rowAgencyExcelList.get(23).toString());
		
		//set Product_Code=Lsbs_id = 187 
		//set Product = Lsdbs_number (11=SMART PROTECTION CARE PERDANA, 12=SMART PROTECTION CARE SINGLE, 13=SMART PROTECTION CARE CERIA, 14=SMART PROTECTION CARE IDEAL)
		
		double faktor_premi=1;
		double premi;
		if(agencyExcel.getLscb_id()==3){ //TAHUNAN
			faktor_premi = 1;
		}
		else if(agencyExcel.getLscb_id()==2){ // SEMESTERAN
			faktor_premi = 0.525;
		}
		else if(agencyExcel.getLscb_id()==1){ // 3WULAN
			faktor_premi = 0.27;
		}
		else  if(agencyExcel.getLscb_id()==6){ //BULANAN
			faktor_premi = 0.1;
		}
		if(agencyExcel.getProduk()==11){ //perdana
			premi = faktor_premi*300000;							
			agencyExcel.setPremi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_premi(String.valueOf(new BigDecimal(premi)));	
			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(100000000)));	
		}
		else if(agencyExcel.getProduk()==12){ //single
			premi = faktor_premi*500000;
			agencyExcel.setPremi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_premi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(50000000)));	
		}
		else if(agencyExcel.getProduk()==13){ //ceria
			premi = faktor_premi*900000;
			agencyExcel.setPremi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_premi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(100000000)));	
		}
		else if(agencyExcel.getProduk()==14){ //ideal
			premi = faktor_premi*1600000;
			agencyExcel.setPremi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_premi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(200000000)));	
		}

		return agencyExcel;
	}
    
    
    public  String validate_prod_u_01_187_SMS(){
    	String err="";
    	
    	if(Common.isEmpty(agencyExcel.getMsag_id()) || agencyExcel.getMsag_id() == null || agencyExcel.getMsag_id() == ""){
    		err = err+ " Update PAS BP SMS: Kode Agen BP tidak boleh kosong!,";
    	}    	
    	if(Common.isEmpty(agencyExcel.getMsp_fire_id()) || agencyExcel.getMsp_fire_id() == null || agencyExcel.getMsp_fire_id() == ""){
    		err = err+ " Update PAS BP SMS: Fire.ID tidak boleh kosong!,";
    	}
    	if(Common.isEmpty(agencyExcel.getMsp_pas_email())){
			err = err+ " PAS: email PP harus diisi,";
		}
    	
    return err;	
    }
    
    public String validate_common(){
    	String err="";
    	//panggil parent krn extend : Andy
    	err = err+ validate_prod_u();
     return err;	
    }
    
	 public String validate_prod_u_01_187_SAC(){
	    	String err="";
	    	
	    	// VALIDASI CREDIT-CARD/ REK-AUTODEBET= PAS-SYARIAH
			if(agencyExcel.getMsp_flag_cc() == 1 || agencyExcel.getMsp_flag_cc() == 2){
				if(Common.isEmpty(agencyExcel.getLsbp_id_autodebet()) || agencyExcel.getLsbp_id_autodebet() == null || agencyExcel.getLsbp_id_autodebet() == ""){
					err = err+ " CREDIT CARD BANK/KODE BANK AUTODEBET harus diisi,";
				}
				if(Common.isEmpty(agencyExcel.getMsp_no_rekening_autodebet()) || agencyExcel.getMsp_no_rekening_autodebet() == null || agencyExcel.getMsp_no_rekening_autodebet() == ""){
					err = err+ " CREDIT CARD NO/NO REKENING AUTODEBET harus diisi,";
				}
				if(agencyExcel.getMsp_flag_cc() == 1 && agencyExcel.getMsp_tgl_valid()==null){
//					err = err+ " Tanggal Expired CREDIT CARD harus diisi dlm format dd/MM/yyyy,";	
					//bank BTN tidak menggunakan CC, tidak ada validasi exp ini.
				}
				if(Common.isEmpty(agencyExcel.getMsp_rek_nama_autodebet()) || agencyExcel.getMsp_rek_nama_autodebet() == null || agencyExcel.getMsp_rek_nama_autodebet() == ""){
					err = err+ " Nama Pemegang CREDIT CARD / Nama Pemilik REKENING AUTODEBET harus diisi,";
				}	
			}
			/*
			if(agencyExcel.getLsre_id()==null) err = err+ " Relasi_TTG PAS SYARIAH DMTM kosong/ tidak sesuai Format!',";
 		if(Common.isEmpty(agencyExcel.getMsp_pas_phone_number())){
				err = err+ " No. Telepon harus diisi,";
			}else if(agencyExcel.getMsp_pas_phone_number().contains("E") || !Common.validPhone(agencyExcel.getMsp_pas_phone_number().trim()) ) {
				err = err+ " No. Telepon harus diisi angka dlm format 'Text',";
			}
 		if(Common.isEmpty(agencyExcel.getMsp_rek_cabang())){
 			err = err+ " Cabang Bank harus diisi,";
 		}
 		if(Common.isEmpty(agencyExcel.getMsp_rek_kota())){
 			err = err+ " Kota Bank harus diisi,";
 		}
 		if(Common.isEmpty(agencyExcel.getMsp_rek_nama())){
 			err = err+ " Atas Nama Bank harus diisi,";
 		}*/
		    	
	    return err;	
	    }
    
}
