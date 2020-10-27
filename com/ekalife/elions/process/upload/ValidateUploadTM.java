package com.ekalife.elions.process.upload;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.web.bac.support.form_agen;
import com.ekalife.utils.parent.ParentDao;


public class ValidateUploadTM extends ParentDao{

	private BacManager bacManager;
	
	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
    }
	
	SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
	SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
	SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	SimpleDateFormat sdfr = new SimpleDateFormat("dd/MMyyyy");
	
	
	public String validateNilaiDefault(List<List> spajExcelList, int i) {		
	    String err="";
		//pemegang
	    if(spajExcelList.get(i).get(2).toString()==null || spajExcelList.get(i).get(2).toString().equals("") )err=err+"Nama Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(3).toString()==null ||spajExcelList.get(i).get(3).toString().equals(""))err=err+"Jenis Kelamin Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(4).toString().equals("") || spajExcelList.get(i).get(4).toString()==null )err=err+"Kota Lahir Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(6).toString()==null||spajExcelList.get(i).get(6).toString().equals(""))err=err+"Jenis Identitas Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(7).toString().equals("") ||spajExcelList.get(i).get(7).toString()==null )err=err+"Nomor Identitas Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(8).toString().equals("") || spajExcelList.get(i).get(8).toString()==null )err=err+"Status Pernikahan Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(9).toString()==null || spajExcelList.get(i).get(9).toString().equals(""))err=err+"Hubungan Pemegang Polis Dengan Tertanggung Masih kosong,";
	    if(spajExcelList.get(i).get(10).toString().equals("") ||spajExcelList.get(i).get(10).toString()==null )err=err+"Alamat Rumah Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(11).toString().equals("") || spajExcelList.get(i).get(11).toString()==null )err=err+"Kota Rumah Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(12).toString().equals("") || spajExcelList.get(i).get(12).toString()==null )err=err+"Kode Pos Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(21).toString().equals("") || spajExcelList.get(i).get(21).toString()==null )err=err+"Nomor Telpon Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(13).toString().equals("") || spajExcelList.get(i).get(13).toString()==null )err=err+"Alamat Kantor Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(14).toString().equals("") || spajExcelList.get(i).get(14).toString()==null )err=err+"Kota Kantor Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(22).toString().equals("") || spajExcelList.get(i).get(22).toString()==null )err=err+"Nomor Telpon Kantor Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(15).toString().equals("") || spajExcelList.get(i).get(15).toString()==null )err=err+"Kode Pos Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(23).toString().equals("") || spajExcelList.get(i).get(23).toString()==null )err=err+"Nomor Handphone Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(26).toString().equals("") || spajExcelList.get(i).get(26).toString()==null )err=err+"Email Pemegang Polis Masih kosong,";
	    if(spajExcelList.get(i).get(5).toString().equals("")||spajExcelList.get(i).get(5).toString()==null)err=err+"Tanggal Lahir Pemegang Polis Masih kosong,";
	    
	  //ttg
	    if(spajExcelList.get(i).get(27).toString().equals("") ||spajExcelList.get(i).get(27).toString()==null )err=err+"Nama Tertanggung Utama Masih kosong,";
	    if(spajExcelList.get(i).get(28).toString()==null || spajExcelList.get(i).get(28).toString().equals("") )err=err+"Jenis Kelamin Tertanggung Utama Masih kosong,";
	    if(spajExcelList.get(i).get(29).toString().equals("") || spajExcelList.get(i).get(29).toString()==null )err=err+"Kota Lahir Tertanggung Utama  Masih kosong,";
	    if(spajExcelList.get(i).get(31).toString()==null || spajExcelList.get(i).get(27).toString().equals(""))err=err+"Identitas Tertanggung Utama  Masih kosong,";
	    if(spajExcelList.get(i).get(32).toString().equals("") ||spajExcelList.get(i).get(32).toString()==null )err=err+"Nomor Identitas Tertanggung Utama  Masih kosong,";  
	    if(spajExcelList.get(i).get(34).toString().equals("") || spajExcelList.get(i).get(34).toString()==null )err=err+"Alamat Rumah Tertanggung Utama  Masih kosong,";
	    if(spajExcelList.get(i).get(35).toString().equals("") ||spajExcelList.get(i).get(35).toString()==null )err=err+"Kota Rumah Tertanggung Utama  Masih kosong,";
	    if(spajExcelList.get(i).get(36).toString().equals("") ||spajExcelList.get(i).get(36).toString()==null )err=err+"Kode Pos Tertanggung Utama  Masih kosong,";
	
	    //address billing
	    if(spajExcelList.get(i).get(16).toString().equals("") ||spajExcelList.get(i).get(16).toString()==null)err=err+"ALamat Penagihan Masih kosong,";
	    if(spajExcelList.get(i).get(17).toString().equals("") ||spajExcelList.get(i).get(17).toString()==null)err=err+"Kota Penagihan Masih kosong,";
	    if(spajExcelList.get(i).get(19).toString().equals("") || spajExcelList.get(i).get(19).toString()==null)err=err+"Nomor Telepon Penagihan Masih kosong,";				
	    if(spajExcelList.get(i).get(18).toString().equals("") || spajExcelList.get(i).get(18).toString()==null)err=err+"Kode Pos Penagihan Masih kosong,";
	    
	    //produk Utama
	    if(spajExcelList.get(i).get(136).toString()==null || spajExcelList.get(i).get(136).toString().equals(""))err=err+"Produk Utama Masih kosong,";
	    if(spajExcelList.get(i).get(139).toString()==null || spajExcelList.get(i).get(139).toString().equals(""))err=err+"Cara Bayar Produk Utama Masih kosong,";
	    
	    
	    //kode agen
	    if( spajExcelList.get(i).get(149).toString().equals("")|| spajExcelList.get(i).get(16).toString()==null)err=err+"Kode Agen Masih kosong,";
		
		return err;
	}


	public String validateAgenTutupan(String lca, Agen excelListagen) {
		
		 String err="";
		 if(lca.equals("04")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") ||
					 !excelListagen.getLsrg_id().toString().equals("00")  ){
				     err+="Kode Penutup Agen Salah.Untuk Produk ini Gunakan Kode agen(027018/027019/027020), ";
			 }
		 }else if(lca.equals("05")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") ||
					 !excelListagen.getLsrg_id().toString().equals("01")  ){
				     err+="Kode Penutup Agen Salah.Untuk Produk ini Gunakan Kode agen(027165/027166), ";
			 }
		 }else if(lca.equals("06")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("01") ||
					 !excelListagen.getLsrg_id().toString().equals("00")  ){
				     err+="Kode Penutup Agen Salah. ";
			 }
		 }else if(lca.equals("07")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") ||
					 !excelListagen.getLsrg_id().toString().equals("02")  ){
				     err+="Kode Penutup Agen Salah. ";
			 }
		 }else if(lca.equals("08")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
					!excelListagen.getLsrg_id().toString().equals("03")  ){
				     err+="Kode Penutup Agen Salah. ";
			 }
		 }else if(lca.equals("09")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("01") || 
					 !excelListagen.getLsrg_id().toString().equals("02")  ){
				     err+="Kode Penutup Agen Salah. ";
			 }
		 }else if(lca.equals("10")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
					 !excelListagen.getLsrg_id().toString().equals("04")  ){
				     err+="Kode Penutup Agen Salah. ";
			 }
		 }else if(lca.equals("11")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") ||
					 !excelListagen.getLsrg_id().toString().equals("05")  ){
				     err+="Kode Penutup Agen Salah. ";
			 }
		 }else if(lca.equals("12")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("01") || 
					 !excelListagen.getLsrg_id().toString().equals("00")  ){
				     err+="Kode Penutup Agen Salah. ";
			 }
		 }else if(lca.equals("13")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("01") || 
					 !excelListagen.getLsrg_id().toString().equals("02")  ){
				     err+="Kode Penutup Agen Salah. ";
			 }
		 }else if(lca.equals("14")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
					 !excelListagen.getLsrg_id().toString().equals("03")  ){
				     err+="Kode Penutup Agen Salah. ";
			 }
		 }else if(lca.equals("15")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
					 !excelListagen.getLsrg_id().toString().equals("06")  ){
				     err+="Kode Penutup Agen Salah. ";
			 }
		 }else if(lca.equals("20")){
			 
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("01")
					 ||!(excelListagen.getLsrg_id().toString().equals("04") || excelListagen.getLsrg_id().toString().equals("11"))
					 ){
				 		err+="Kode Penutup Agen Bukan Dari Site / Region BTN. ";	
			 }
			 
			 
		 }else if(lca.equals("21")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("01") || 
					 !excelListagen.getLsrg_id().toString().equals("07")  ){
				     err+="Kode Penutup Agen Bukan Dari Site / Region DKI. ";
			 }
		 }else if(lca.equals("25")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("01") || 
					 !excelListagen.getLsrg_id().toString().equals("08")  ){
			         err+="Kode Penutup Agen Bukan Dari Site / Region BUKOPIN. ";
		 	 		
//				 if(!excelListagen.getMsag_id().equals("901993")) err+="Kode Penutup Agen Bukan Dari Site / Region BUKOPIN. ";
			 
			 }
		 }else if(lca.equals("26")){
			 if(!excelListagen.getLca_id().toString().equals("40") || 
			   (!excelListagen.getLwk_id().toString().equals("02") && !excelListagen.getLwk_id().toString().equals("01")) || 
			   (!excelListagen.getLsrg_id().toString().equals("00") && !excelListagen.getLsrg_id().toString().equals("01") && !excelListagen.getLsrg_id().toString().equals("07") && !excelListagen.getLsrg_id().toString().equals("11") && !excelListagen.getLsrg_id().toString().equals("12"))){
				     err+="Kode Penutup Agen Salah. ";
			 }
		 }else if(lca.equals("27")){
			 // strictly untuk bank jatim
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("01") || 
					 !excelListagen.getLsrg_id().toString().equals("12")  ){
				     err+="Kode Penutup Agen Bukan Dari Site / Region Bank Jatim. ";
			 }
			 
		 }else if(lca.equals("28")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("01") || 
					 !excelListagen.getLsrg_id().toString().equals("13")  ){
				     err+="Kode Penutup Agen Bukan Dari Site / Region Bank BJB. ";
			 }
		 }else if(lca.equals("MARZ")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
					 !excelListagen.getLsrg_id().toString().equals("09")  ){
				     err+="Kode Penutup Agen Bukan Dari Site MARZ. ";
			 }
		 }else if(lca.equals("VALDO")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
					 !excelListagen.getLsrg_id().toString().equals("10")  ){
				     err+="Kode Penutup Agen Bukan Dari Site VALDO. ";
			 }
		 }else if(lca.equals("GOS")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
					 !excelListagen.getLsrg_id().toString().equals("11")  ){
				     err+="Kode Penutup Agen Bukan Dari Site GOS. ";
			 }
		 }else if(lca.equals("VASCO")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
					 !excelListagen.getLsrg_id().toString().equals("08")  ){
				     err+="Kode Penutup Agen Bukan Dari Site VASCO. ";
			 }
		 }else if(lca.equals("DENA")){
			 if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
					 !excelListagen.getLsrg_id().toString().equals("07")  ){
				     err+="Kode Penutup Agen Bukan Dari Site DENA. ";
			 }
		 }else if(lca.equals("NISSAN")){
            if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("01") || 
                    !excelListagen.getLsrg_id().toString().equals("09")){
                    err+="Kode Penutup Agen Bukan Dari Site NISSAN. ";
            }
		 }else if(lca.equals("SYNERGYS")){
	            if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
	                    !excelListagen.getLsrg_id().toString().equals("12")){
	                    err+="Kode Penutup Agen Bukan Dari Site SYNERGYS. ";
	            }
		}else if(lca.equals("SSI")){
            if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
                    !excelListagen.getLsrg_id().toString().equals("13")){
                    err+="Kode Penutup Agen Bukan Dari Site SSI. ";
            }
		}else if(lca.equals("APK")){
            if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
                    !excelListagen.getLsrg_id().toString().equals("14")){
                    err+="Kode Penutup Agen Bukan Dari Site AUSINDO PRATAMA KARYA. ";
            }
      }else if(lca.equals("ABH")){
            if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
                    !excelListagen.getLsrg_id().toString().equals("15")){
                    err+="Kode Penutup Agen Bukan Dari Site PT ABHIPRAYA. ";
            }
      }else if(lca.equals("KAY")){
          if(!excelListagen.getLca_id().toString().equals("40") || !excelListagen.getLwk_id().toString().equals("02") || 
                  !excelListagen.getLsrg_id().toString().equals("16")){
                  err+="Kode Penutup Agen Bukan Dari Site PT KAYZAN JAYA PERSADA. ";
          }
       }
		
		else if(lca.equals("NEWSIO")){
			  err+="Produk ini hanya bisa di ambil dengan Kode Penutup Agen Dari Extenal Site : Site MARZ/ Site GOS/ Site VALDO /Site VASCO / Site DENA/ Site SYNERGYS/ Site AUSINDO PRATAMA KARYA. ";
		 }
		
		return err;
	}

	public String validateDataAgen(Agen excelListagen, Date d_sysdate) {
		form_agen agn=new form_agen();
		String hasil=agn.sertifikasi_agen(excelListagen.getMsag_id(),excelListagen.getMsag_ulink(), excelListagen.getMsag_sertifikat(), excelListagen.getMsag_berlaku(), d_sysdate);
		if(!"".equalsIgnoreCase(hasil))hasil=hasil+", ";
		return hasil;
	}

	public String checkHubunganPP(Pemegang excelListPp, Tertanggung excelListTtg) {
		String s_pesan="";
		if(excelListPp.getLsre_id()==1){
			if(!excelListPp.getMcl_first().equalsIgnoreCase(excelListTtg.getMcl_first())){
				s_pesan="Data Tertanggung Salah. Perhatikan hubungan Pemegang Polis dengan Tertanggung";
			}
		}
		return s_pesan;
	}


	public String validateCerdasCare(Pemegang excelListPp, Tertanggung excelListTtg, PembayarPremi excelListpayer,
			Agen excelListagen, Datausulan excelListDatausulan, InvestasiUtama excelListInvestasi, Integer jnbankdetbisnis) {
		String s_pesan="";
		
		//validasi Data Usulan
//		if(excelListDatausulan.getLsbs_id()!=120 && "25,26,27".indexOf(excelListDatausulan.getLsdbs_number().toString())<0){
//			s_pesan +="Salah Pilih produk utama";
//		}
		
		//check Hubungan
		s_pesan += checkHubunganPP(excelListPp, excelListTtg);
		
		//check Agen
		String dist ="06";

		/**
		 * DMTM SIMAS KID BSIM 208 => 33,34,35,36
		 * Patar Timotius 
		 * 13/08/2018
		 * cek tertanggung nya ya
		 */
		boolean isSimasKidBSIM = excelListDatausulan.getLsbs_id() == 208 && (excelListDatausulan.getLsdbs_number() >= 33 && excelListDatausulan.getLsdbs_number() <= 36) ;

		if(excelListDatausulan.getLsbs_id()==163 || excelListDatausulan.getLsbs_id()==183 ||
				(excelListDatausulan.getLsbs_id()==195 && (excelListDatausulan.getLsdbs_number() >=37 && excelListDatausulan.getLsdbs_number() <=48))
		|| isSimasKidBSIM		
				){
			dist = "09";
		}
		
		if(jnbankdetbisnis==43){ //Produk BTN
			dist = "20";
		}else if(jnbankdetbisnis==46){ //Produk Bank DKI
			dist = "21";
		}else if(jnbankdetbisnis==50){ //Produk Bank Bukopin
			dist = "25";
		}else if(jnbankdetbisnis == 51 || jnbankdetbisnis == 61){// Product Bank Jatim
			dist = "27";
		}else if(jnbankdetbisnis== 62){ // Product Bank BJB
			dist="28";
		}
		
		// SMILE MEDICAL CARE (DKI)
		if ((excelListDatausulan.getLsbs_id()==183 && (excelListDatausulan.getLsdbs_number() >=106 && excelListDatausulan.getLsdbs_number() <=120 ))){
			dist = "21";
			if(jnbankdetbisnis == 51){// Product Bank Jatim
				dist = "27";
			}
		}
		
		// bank bukopin (HCP+NCB)
		if ((excelListDatausulan.getLsbs_id()==221 && (excelListDatausulan.getLsdbs_number() >=1 && excelListDatausulan.getLsdbs_number() <=12 ))){

			dist = "25"; // default dki only
			
			if(jnbankdetbisnis == 51){ // jika product bank jatim
			dist = "27";
			}
		}
		
		//SMiLe Term ROP DMTM
		if ((excelListDatausulan.getLsbs_id()==212 && excelListDatausulan.getLsdbs_number() == 4)
			|| (excelListDatausulan.getLsbs_id()==212 && excelListDatausulan.getLsdbs_number()== 12)){
			dist = "26";
		}
		
		//NISSAN
		if ( (excelListDatausulan.getLsbs_id()==197 && excelListDatausulan.getLsdbs_number()==2 )
				|| (excelListDatausulan.getLsbs_id()==212 && excelListDatausulan.getLsdbs_number()==8 )
				|| (excelListDatausulan.getLsbs_id()==73 && excelListDatausulan.getLsdbs_number()==15 )
				|| (excelListDatausulan.getLsbs_id()==203 && excelListDatausulan.getLsdbs_number()==4 )){
			dist = "NISSAN";
		}
		
		
	
		// Produk EXTERNAL untuk SIO PT MARZ , PT VALDO , PT GOS, PT VASCO, PT DENA , PT SYNERGYS , PT SSI
		if ((excelListDatausulan.getLsbs_id()==189 && (excelListDatausulan.getLsdbs_number() >= 33 && excelListDatausulan.getLsdbs_number() <= 47))
				||	(excelListDatausulan.getLsbs_id()==204 && (excelListDatausulan.getLsdbs_number() >= 37 && excelListDatausulan.getLsdbs_number() <= 48))
				||	(excelListDatausulan.getLsbs_id()==212 && excelListDatausulan.getLsdbs_number() == 7 )){
	
			if (excelListPp.getCampaign_id() == 14){
				dist = "MARZ";	
			}else if (excelListPp.getCampaign_id() == 15){
				dist = "VALDO";	
			}else if (excelListPp.getCampaign_id() == 16){
				dist = "GOS";	
			}else if (excelListPp.getCampaign_id() == 20){
				dist = "VASCO";	
			}else if (excelListPp.getCampaign_id() == 13){
				dist = "DENA";	
			}else if (excelListPp.getCampaign_id() == 22){
				dist = "SYNERGYS";	
			}else if (excelListPp.getCampaign_id() == 23){
				dist = "SSI";	
			}else if (excelListPp.getCampaign_id() == 24){
				dist = "APK";	
			}else if (excelListPp.getCampaign_id() == 25){
				dist = "ABH";	
			}else if (excelListPp.getCampaign_id() == 26){
				dist = "KAY";	
			}else {
				dist = "NEWSIO";
			}
		}	
		s_pesan += validateAgenTutupan(dist, excelListagen);
		return s_pesan;
	}

	public String checkDataAutodebet(Account_recur recur) {
			String s_pesan="";
			if(recur.getLbn_id()==null ||  recur.getLbn_id().equals("")){
				s_pesan = " Data Bank Autodebet Kosong, Harap Dicek Kembali Kelengkapan Data";
			}
			if(recur.getMar_holder().equals("")){
				s_pesan += " - Nama Pemegang Rekening Kosong, Harap Dicek Kembali Kelengkapan Data";
			}
			if(recur.getMar_acc_no().equals("")){
				s_pesan += " - No Rekening Kosong, Harap Dicek Kembali Kelengkapan Data";
			}
		return s_pesan;
	}
}