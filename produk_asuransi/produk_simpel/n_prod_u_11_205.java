package produk_asuransi.produk_simpel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
/**
 * Produk Upload PAS Business Partner
 * @author : Adrian
 * @since : Jan 21, 2015
 */
public class n_prod_u_11_205 extends n_prod_u {
	
	protected final Log logger = LogFactory.getLog( getClass() );
	
	void main(){
		int x = getumur();
		//2
	}
		
	//override
	int getumur(){
		return 2;
	}
	
	public n_prod_u_11_205(){
		super();
	}
	
	public Pas set_prod_u_11_205(List rowAgencyExcelList ){
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
		agencyExcel.setProduct_code("205");
		agencyExcel.setProduk(Integer.parseInt(rowAgencyExcelList.get(29).toString().trim().replace(".00", "").replace(".0", "")));
		agencyExcel.setApplication_id(rowAgencyExcelList.get(31).toString().trim().replace(".00", "").replace(".0", ""));
		agencyExcel.setTm_id(rowAgencyExcelList.get(39).toString().trim().replace(".00", "").replace(".0", "")); // insert ke mst_poicy.mspo_no_kerjasama
		try{
			agencyExcel.setSpv_id(rowAgencyExcelList.get(40).toString().trim()); 
		}catch (Exception e) {
			logger.error("ERROR :", e);						
		}
		agencyExcel.setNo_va(rowAgencyExcelList.get(41).toString().trim()); 
		agencyExcel.setCif(rowAgencyExcelList.get(42).toString().trim()); 
		
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
		//No.kartu
		agencyExcel.setNo_kartu(rowAgencyExcelList.get(23).toString());
		
		//set Product_Code=Lsbs_id=205 
		//set Product=Lsdbs_number= (1->PAS Syariah Perdana (NEW)/2->PAS Syariah Single (NEW)/3->PAS Syariah Ceria (NEW)/4->PAS Syariah Ideal (NEW))
		//(5->PAS Syariah Perdana/6->PAS Syariah Single/7->PAS Syariah Ceria/8->PAS Syariah Ideal)
		
		double faktor_premi=1;
		double premi;
		if(agencyExcel.getLscb_id()==3){//tahunan
			faktor_premi = 1;
		}
		else if(agencyExcel.getLscb_id()==2){//semesteran
			faktor_premi = 0.525;
		}
		else if(agencyExcel.getLscb_id()==1){//triwulanan
			faktor_premi = 0.27;
		}
		else  if(agencyExcel.getLscb_id()==6){//bulanan //kolom T
			faktor_premi = 0.1;
		}
		if(agencyExcel.getProduk()==1 || agencyExcel.getProduk()==5){
			premi = faktor_premi*300000;							
			agencyExcel.setPremi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_premi(String.valueOf(new BigDecimal(premi)));	
			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(100000000)));	
		}
		else if(agencyExcel.getProduk()==2 || agencyExcel.getProduk()==6){
			premi = faktor_premi*500000;
			agencyExcel.setPremi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_premi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(50000000)));	
		}
		else if(agencyExcel.getProduk()==3 || agencyExcel.getProduk()==7){
			premi = faktor_premi*900000;
			BigDecimal rounded = new BigDecimal(premi).setScale(0, RoundingMode.HALF_UP);
			agencyExcel.setPremi(String.valueOf(rounded));
			agencyExcel.setMsp_premi(String.valueOf(rounded));
			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(100000000)));	
		}
		else if(agencyExcel.getProduk()==4 || agencyExcel.getProduk()==8){
			premi = faktor_premi*1600000;
			agencyExcel.setPremi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_premi(String.valueOf(new BigDecimal(premi)));
			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(200000000)));	
		}
//		else if(agencyExcel.getProduk()==5){
//			premi = faktor_premi*250000;							
//			agencyExcel.setPremi(String.valueOf(new BigDecimal(premi)));
//			agencyExcel.setMsp_premi(String.valueOf(new BigDecimal(premi)));	
//			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(100000000)));	
//		}
//		else if(agencyExcel.getProduk()==6){
//			premi = faktor_premi*450000;
//			agencyExcel.setPremi(String.valueOf(new BigDecimal(premi)));
//			agencyExcel.setMsp_premi(String.valueOf(new BigDecimal(premi)));
//			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(50000000)));	
//		}
//		else if(agencyExcel.getProduk()==7){
//			premi = faktor_premi*800000;
//			agencyExcel.setPremi(String.valueOf(new BigDecimal(premi)));
//			agencyExcel.setMsp_premi(String.valueOf(new BigDecimal(premi)));
//			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(100000000)));	
//		}
//		else if(agencyExcel.getProduk()==8){
//			premi = faktor_premi*1350000;
//			agencyExcel.setPremi(String.valueOf(new BigDecimal(premi)));
//			agencyExcel.setMsp_premi(String.valueOf(new BigDecimal(premi)));
//			agencyExcel.setMsp_up(String.valueOf(new BigDecimal(200000000)));	
//		}
		
		
		
		return agencyExcel;
	}
	
	
	 public String validate_prod_u_11_205(){
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
					err = err+ " Tanggal Expired CREDIT CARD harus diisi dlm format dd/MM/yyyy,";	
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
	
	 
	  public String validate_common(){
	    	String err="";
	    	//panggil parent krn extend : Andy
	    	err = err+ validate_prod_u();
	     return err;	
	    }
	 	 
	
}
