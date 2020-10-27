package produk_asuransi.produk_simpel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.Pas;
import com.ekalife.utils.Common;
/**
 * Produk Upload PAS Business Partner
 * @author : Adrian
 * @since : Jan 21, 2015
 */
public class n_prod_u_15_073 extends n_prod_u {
	
	protected final Log logger = LogFactory.getLog( getClass() );
		
	void main(){
		int x = getumur();
		//2
	}
		
	//override
	int getumur(){
		return 2;
	}
	public n_prod_u_15_073(){
		super();
	}
	
  public Pas set_prod_u_15_073(List rowAgencyExcelList ){
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
		agencyExcel.setProduct_code("073");
		agencyExcel.setProduk(15);
		//agencyExcel.setProduk(Integer.parseInt(rowAgencyExcelList.get(29).toString().trim().replace(".00", "").replace(".0", "")));
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
    
    public String validate_common(){
    	String err="";
    	//panggil parent krn extend : Andy
    	err = err+ validate_prod_u();
     return err;	
    }
    
	 public String validate_prod_u_15_073(){
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
