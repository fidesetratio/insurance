package com.ekalife.elions.business.bac;

import java.util.Date;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.Kesehatan;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.vo.BacSpajParamVO;
import com.ekalife.utils.FormatString;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Syariah {
	protected final Log logger = LogFactory.getLog( getClass() );
	private static Syariah instance;
	
	public static Syariah getInstance()
	{
		if( instance == null )
		{
			instance = new Syariah();
		}
		return instance;
	}
	
	public Syariah()
	{
	}
	
	public PdfContentByte createPdf( BacSpajParamVO paramVO )
	{
		PdfContentByte over = paramVO.getOver(); 
		Pemegang pemegang = paramVO.getPemegang(); 
		Tertanggung tertanggung = paramVO.getTertanggung(); 
		Kesehatan medical=paramVO.getMedical();
		Integer x = paramVO.getX();
		Integer y = paramVO.getY();
		Integer f = paramVO.getF();
		Datausulan datausulan = paramVO.getDatausulan();
		Powersave data_pwr = paramVO.getData_pwr();
		Rekening_client data_rek = paramVO.getData_rek();
		Agen dataagen =paramVO.getDataagen();
		InvestasiUtama inv = paramVO.getInv();
		PdfStamper stamp =paramVO.getStamp();
		BaseFont bf = paramVO.getBf();
		PdfReader reader =paramVO.getReader();
	
				int tambah = 60;
		
//				data pemegang
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-279, y-38, 0);  
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-279, y-51, 0);
    			
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-277, y-64, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-240, y-64, 0);//x-240, y-64
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-200, y-64, 0);
	    				}
    			}
    			
    			String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-277+(i*11), y-77, 0);
	    			}
    			}
    			Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-279, y-91, 0);//  x-279, y-91
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X",x-219, y-92, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-182, y-92, 0);
	    				}
    			}
    			
    			Date tanggal_lahir = pemegang.getMspe_date_birth();
    			if (tanggal_lahir != null)
    			{
	    			Integer tgl_lahir = tanggal_lahir.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tanggal_lahir.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tanggal_lahir.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-262+(i*10), y-104, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-212+(i*10), y-104, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-161+(i*12), y-104, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-94, y-104, 0);
    			
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-118, 0);// x-279, y-118
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-279, y-118, 0);
	    				}
    			}
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-148, y-117, 0);//blum kawin
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-84, y-117, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-40, y-117, 0);//x-40, y-118
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-40, y-117, 0);
	    					break;
	    			}
    			}	
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-279, y-130, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-130, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-185, y-130, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-130, y-130, 0);//hindu
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-84, y-130, 0);//x-84, y-130 
	    					break;
	    			}
    			}
    			Integer id_pendidikan = pemegang.getLsed_id();
    			String pendidikan = pemegang.getLsed_name();
    			logger.info(pendidikan);
    			if (pendidikan != null)
    			{
	    			switch (id_pendidikan)
	    			{
	    				case 8:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-279, y-143, 0);//SD
	    					break;	    				
    					case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-197, y-143, 0);//smu
	    					break;
	    				case 9:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-237, y-143, 0);//x-237, y-143
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-107, y-143, 0);//S1
	    						break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-74, y-143, 0);//S2
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-158, y-143, 0);//D1-3
	    						break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-23, y-143, 0);//lainnya
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan,  x+18, y-143, 0);
	    					break;
	    			}
    			}
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-281, y-160, 0);
    			String kota_rumah = pemegang.getKota_rumah();
    			if (kota_rumah != null)
    			{
	    			for (int i =0 ; i< kota_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_rumah.charAt(i)), x-22+(i*5),y-185, 0);
	    			}
    			}
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)),x-278+(i*12),y-184, 0); // x-286+(i*10),y-160
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-193+(i*12),y-184, 0);
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-137+(i*12),y-184, 0);
	    			}
    			}
    			String alamat_kantor = pemegang.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kantor, x-279, y-200, 0);
    			String kota_kantor = pemegang.getKota_kantor();
    			if (kota_kantor != null)
    			{
	    			for (int i =0 ; i< kota_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_kantor.charAt(i)), x-22+(i*5),y-225, 0);
	    			}
    			}
    			
    			String kode_pos_kantor  = pemegang.getKd_pos_kantor();
    			if (kode_pos_kantor != null)
    			{
	    			if ( kode_pos_kantor!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)),x-278+(i*12),y-226, 0); // x-286+(i*10),y-160
		    			}
	    			}
    			}
    			
    			String area_kantor = pemegang.getArea_code_kantor();
    			if (area_kantor != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kantor.charAt(i)), x-193+(i*12),y-226, 0);
	    			}
    			}
    			
    			String telp_kantor = pemegang.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-137+(i*12),y-226, 0);
	    			}
    			}
    			String alamat_kores = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kores, x-279, y-241, 0);
    			String kota_kores = pemegang.getKota_rumah();
    			if (kota_rumah != null)
    			{
	    			for (int i =0 ; i< kota_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_rumah.charAt(i)), x-22+(i*5),y-267, 0);
	    			}
    			}
    			
    			String kode_pos_kores  = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)),x-278+(i*12),y-267, 0); // x-286+(i*10),y-160
		    			}
	    			}
    			}
    			
    			String area_kores = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-193+(i*12),y-267, 0);
	    			}
    			}
    			
    			String telp_kores = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-137+(i*12),y-267, 0);
	    			}
    			}
    			String hp= pemegang.getNo_hp();
    			if (hp != null)
    			{
    				for (int i =0 ; i< hp.length() ; i++)
    	    		{
    	    			over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(hp.charAt(i)), x-278+(i*12),y-280, 0);
    	    		}
    			}
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getEmail(), x-82, y-282, 0);
    			String tujuan = pemegang.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "x", x-367, y-308, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "x", x-244, y-308, 0);
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-126, y-308, 0);//  x-126, y-308
	    							}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-367, y-320, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "x", x-244, y-320, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "x", x-126, y-320, 0);//lain2
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan,x-57, y-321, 0);
	    								}
    			}
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-346, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-358, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "x", x-244, y-346, 0);   //  x-244, y-346
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-358, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-358, 0);
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-346, 0);
		    							}
    			}
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-384, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-396, 0);
	    					}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-244, y-384, 0);//x-244, y-384
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-396, 0);
	    							}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-384, 0);//lain2
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-57, y-385, 0);
				    				}
    			}
    			String pekerjaan =  pemegang.getMkl_kerja();
    			if(pekerjaan!= null){
    				if (pekerjaan.equals("PROFESIONAL")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-324, 0);
    				}else if(pekerjaan.equals("PEMILIK/PENGUSAHA")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-435, 0);
    				}else if (pekerjaan.equals("PEMASARAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-446, 0);
    				}else if (pekerjaan.equals("KARYAWAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-435, 0);
    				}else if (pekerjaan.equals("JASA")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-446, 0);
    				}else if (pekerjaan.equals("BURUH")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-435, 0);
    				}else if (pekerjaan.equals("PELAJAR")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-446, 0);
    				}else if (pekerjaan.equals("IBU RUMAH TANGGA")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-47, y-435, 0);//47, y-435
    				}else {
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-47, y-445, 0);
    				}
    			}
    			
    			String industri = pemegang.getIndustria();
    			logger.info(industri);
    			if(industri!= null){
    				if (industri.equals("PERTANIAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-472, 0);
    				}else if(industri.equals("PERTAMBANGAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-485, 0);
    				}else if (industri.equals("KONSTRUKSI")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-472, 0);
    				}else if (industri.equals("MANUFAKTUR")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-485, 0);
    				}else if (industri.equals("PERDAGANGAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-472, 0);
    				}else if (industri.equals("JASA KEUANGAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-485, 0);//x-126, y-485
    				}else if (industri.equals("PEMERINTAHAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-47, y-472, 0);
    				}else {
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-47, y-485, 0);
    				}
    			}
//    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-265, y-282, 0);
    			  			
//    			String uraian_kerja =pemegang.getMpn_job_desc();
//    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-265, y-297, 0);
//    			String jabatan = pemegang.getKerjab();
//    			over.showTextAligned(Element.ALIGN_LEFT,jabatan , x-104, y-297, 0);
    			
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x-198, y-507, 0);
    			
    			Integer nOne = reader.getNumberOfPages();
				
				logger.info("jumlah page:"+nOne);
				
				over.endText();
				
				over = stamp.getOverContent(2);
		    	over.beginText();
		    	over.setFontAndSize(bf, f); //set ukuran font
		    	
    			String kurs = datausulan.getLku_id();
    			String krs ="";
    			if (kurs != null)
    			{
	    			if (kurs.equalsIgnoreCase("01"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" ,  x-272, y+221, 0); // x-272, y+221
	    				krs="Rp";
	    			}else{
	    				krs= "U$ ";
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x-242, y+221, 0);
	    			}
    			}
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-276, y+208, 0);
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-276, y+195, 0);
    			String cabang_bank = data_rek.getMrc_cabang();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang_bank, x-276, y+183, 0);
    			String kota_bank = data_rek.getMrc_kota();
    			over.showTextAligned(Element.ALIGN_LEFT,  kota_bank, x-51, y+183, 0);

//    			String no_rek = data_rek.getMrc_no_ac();
//    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-276, y+182, 0);

    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-279, y+118, 0);  
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-279, y+105, 0);
    			
    			jns_identitas = tertanggung.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-277,y+92, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-240, y+92, 0);//x-240, y-64
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-200, y+92, 0);
	    				}
    			}
    			
    			no_identitas = tertanggung.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-276+(i*11), y+79, 0);
	    			}
    			}
    			id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-279, y+66, 0);//  x-279, y-91
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X",x-219, y+66, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-182, y+66, 0);
	    				}
    			}
    			
    			tanggal_lahir = tertanggung.getMspe_date_birth();
    			if (tanggal_lahir != null)
    			{
	    			Integer tgl_lahir = tanggal_lahir.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tanggal_lahir.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tanggal_lahir.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-262+(i*10), y+53, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-212+(i*10), y+53, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-161+(i*12), y+53, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-94, y+53, 0);
    			
    			jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y+41, 0);// x-279, y-118
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-279, y+41, 0);
	    				}
    			}
    			sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-148,y+41, 0);//blum kawin
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-84, y+41, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-40, y+41, 0);//x-40, y-118
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-40,y+41, 0);
	    					break;
	    			}
    			}	
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-279, y+28, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y+28, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-185, y+28, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-130, y+28, 0);//hindu
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-84, y+28, 0);//x-84, y-130 
	    					break;
	    			}
    			}
    			id_pendidikan = tertanggung.getLsed_id();
    			pendidikan = tertanggung.getLsed_name();
    			logger.info(pendidikan);
    			if (pendidikan != null)
    			{
	    			switch (id_pendidikan)
	    			{
	    				case 8:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-279, y+16, 0);//SD
	    					break;	    				
    					case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-197, y+16, 0);//smu
	    					break;
	    				case 9:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-237, y+16, 0);//x-237, y-143
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-107, y+16, 0);//S1
	    						break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-74, y+16, 0);//S2
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-158, y+16, 0);//D1-3
	    						break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-23, y+16, 0);//lainnya
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan,  x+18, y+16, 0);
	    					break;
	    			}
    			}
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-281, y+4, 0);
    			kota_rumah = tertanggung.getKota_rumah();
    			if (kota_rumah != null)
    			{
	    			for (int i =0 ; i< kota_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_rumah.charAt(i)), x-22+(i*5),y-22, 0);
	    			}
    			}
    			
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)),x-278+(i*12),y-21, 0); // x-286+(i*10),y-160
		    			}
	    			}
    			}
    			
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-193+(i*12),y-21, 0);
	    			}
    			}
    			
    			telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-137+(i*12),y-21, 0);
	    			}
    			}
    			alamat_kantor = tertanggung.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kantor, x-279, y-35, 0);
    			kota_kantor = tertanggung.getKota_kantor();
    			if (kota_kantor != null)
    			{
	    			for (int i =0 ; i< kota_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_kantor.charAt(i)), x-22+(i*5),y-59, 0);
	    			}
    			}
    			
    			kode_pos_kantor  = tertanggung.getKd_pos_kantor();
    			if (kode_pos_kantor != null)
    			{
	    			if ( kode_pos_kantor!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)),x-278+(i*12),y-59, 0); // x-286+(i*10),y-160
		    			}
	    			}
    			}
    			
    			area_kantor = tertanggung.getArea_code_kantor();
    			if (area_kantor != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kantor.charAt(i)), x-193+(i*12),y-59, 0);
	    			}
    			}
    			
    			telp_kantor = tertanggung.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-137+(i*12),y-59, 0);
	    			}
    			}
    			alamat_kores = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kores, x-279, y-73, 0);
    			kota_kores = tertanggung.getKota_rumah();
    			if (kota_rumah != null)
    			{
	    			for (int i =0 ; i< kota_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_rumah.charAt(i)), x-22+(i*5),y-99, 0);
	    			}
    			}
    			
    			kode_pos_kores  = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)),x-278+(i*12),y-96, 0); // x-286+(i*10),y-160
		    			}
	    			}
    			}
    			
    			area_kores = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-193+(i*12),y-96, 0);
	    			}
    			}
    			
    			telp_kores = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-137+(i*12),y-96, 0);
	    			}
    			}
    			hp= tertanggung.getNo_hp();
    			if (hp != null)
    			{
    				for (int i =0 ; i< hp.length() ; i++)
    	    		{
    	    			over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(hp.charAt(i)), x-278+(i*12),y-109, 0);
    	    		}
    			}
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getEmail(), x-82, y-109, 0);
    			tujuan = tertanggung.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "x", x-367, y-127, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "x", x-242, y-127, 0);
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-124, y-127, 0);//  x-124, y-127
	    							}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-367, y-138, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "x", x-242, y-138, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "x", x-124, y-138, 0);//lain2
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan,x-57, y-139, 0);
	    								}
    			}
    			penghasilan = tertanggung.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-157, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-167, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "x", x-242, y-157, 0);   //  x-242, y-157
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-167, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-167, 0);
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-157, 0);
		    							}
    			}
    			dana = tertanggung.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-187, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-198, 0);
	    					}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-243, y-187, 0);//x-244, y-384
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-198, 0);
	    							}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-187, 0);//lain2
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-57, y-187, 0);
				    				}
    			}
    			pekerjaan =  tertanggung.getMkl_kerja();
    			if(pekerjaan!= null){
    				if (pekerjaan.equals("PROFESIONAL")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-219, 0);
    				}else if(pekerjaan.equals("PEMILIK/PENGUSAHA")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-229, 0);
    				}else if (pekerjaan.equals("PEMASARAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-240, 0);
    				}else if (pekerjaan.equals("KARYAWAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-229, 0);
    				}else if (pekerjaan.equals("JASA")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-240, 0);
    				}else if (pekerjaan.equals("BURUH")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-229, 0);
    				}else if (pekerjaan.equals("PELAJAR")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-126, y-240, 0);
    				}else if (pekerjaan.equals("IBU RUMAH TANGGA")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-45, y-229, 0);//x-45, y-229
    				}else {
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-45, y-240, 0);
    				}
    			}
    			industri = tertanggung.getIndustria();
    			logger.info(industri);
    			if(industri!= null){
    				if (industri.equals("PERTANIAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-260, 0);
    				}else if(industri.equals("PERTAMBANGAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-367, y-270, 0);
    				}else if (industri.equals("KONSTRUKSI")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-260, 0);
    				}else if (industri.equals("MANUFAKTUR")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-244, y-270, 0);
    				}else if (industri.equals("PERDAGANGAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-260, 0);
    				}else if (industri.equals("JASA KEUANGAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-270, 0);//x-125, y-270
    				}else if (industri.equals("PEMERINTAHAN")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-45, y-260, 0);
    				}else {
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-45, y-270, 0);
    				}
    			}
    			Integer paymode = datausulan.getLscb_id();
    			if (paymode != null)
    			{
    				switch (paymode )
	    			{
	    				case 0: // sekaligus
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+15, y-416, 0);//x-242, y-416
		    				break;
	    				case 1: //triwulanan
    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-39, y-416, 0);
	    				break;
	    				case 2: // semesteran
    						over.showTextAligned(Element.ALIGN_LEFT, "X",  x-99, y-416, 0);
    						break;
	    				case 3://tahunan
    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-160, y-416, 0);
    					break;
	    				case 6://bulanan
							over.showTextAligned(Element.ALIGN_LEFT, "X", x-240, y-416, 0);
	    					break;
	    			}
    			}
    			
    			Integer bentuk_pembayaran = datausulan.getMste_flag_cc();
    			logger.info(bentuk_pembayaran);
    			if (bentuk_pembayaran !=null)
    			{
    				switch (bentuk_pembayaran)
	    			{
	    				case 0: // cekgiro/tunai
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-934, y-1887, 0);
		    				break;
	    				case 1: //triwulanan
    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-760, y-1885, 0);
    						break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-590, y-1885, 0);
	    				
	    					if (bentuk_pembayaran.intValue() == 2) // tabungan
	    					{
		    					over.showTextAligned(Element.ALIGN_LEFT, "TABUNGAN", x-480, y-1885, 0);
		    				}else{
    							over.showTextAligned(Element.ALIGN_LEFT, "PAYROLL", x-480, y-1885, 0);
		    				}
	    					break;
	    			}
    			}
    			
    			nOne = reader.getNumberOfPages();
				
				logger.info("jumlah page:"+nOne);
				
				over.endText();
				
				over = stamp.getOverContent(3);
		    	over.beginText();
		    	over.setFontAndSize(bf, f); //set ukuran font
		    	
		
	    		Integer jumlah_benef = datausulan.getDaftabenef().size();
	    		if(jumlah_benef > 3) jumlah_benef=3;
	    		if (jumlah_benef.intValue() > 0)
	    		{
	    			for (int i = 0 ; i < jumlah_benef.intValue(); i++)
	    			{
	    				Benefeciary benef = (Benefeciary) datausulan.getDaftabenef().get(i);
	    				String nama_benef = benef.getMsaw_first();
	    				Date tgl_lhr_benef = benef.getMsaw_birth();
	    				Integer tgl_benef = tgl_lhr_benef.getDate();
	    				Integer bln_benef = tgl_lhr_benef.getMonth()+1;
	    				Integer thn_benef = tgl_lhr_benef.getYear()+1900;
	    					
	    				String tanggal_lahir_benef = FormatString.rpad("0",(Integer.toString(tgl_benef)),2)+"/"+FormatString.rpad("0",(Integer.toString(bln_benef)),2)+"/"+thn_benef;
	    				String hub_benef = benef.getLsre_relation();
	    				Integer hubungan_benef = benef.getLsre_id();
	    				Double persen_benef = benef.getMsaw_persen();
	    				if (persen_benef == null)
	    				{
	    					persen_benef = new Double(0);
	    				}
	    				over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-353,y+17+ (i*12), 0);	    					
	    				over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-160,y+17+ (i*12), 0);
	    				over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x-82,y+17+ (i*12), 0);
	    				over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x+30,y+17+ (i*12), 0);
	    			}
    			}
	    		nOne = reader.getNumberOfPages();
				logger.info("jumlah page:"+nOne);
				over.endText();
				over = stamp.getOverContent(4);
		    	over.beginText();
		    	over.setFontAndSize(bf, f); //set ukuran font

		    	nOne = reader.getNumberOfPages();
		    	logger.info("jumlah page:"+nOne);
				over.endText();
				over = stamp.getOverContent(5);
		    	over.beginText();
		    	over.setFontAndSize(bf, f);
		    	
		    	nOne = reader.getNumberOfPages();
		    	logger.info("jumlah page:"+nOne);
				over.endText();
				over = stamp.getOverContent(6);
		    	over.beginText();
		    	over.setFontAndSize(bf, f);
	    		
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-290,y-7, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-80,y-7, 0);		    			
	    		//Penutup
	    		String nama_agen = dataagen.getMcl_first();	
	    		String kode_agen = dataagen.getMsag_id();
	    		for (int i =0 ; i<kode_agen.length() ; i++)
    			{
    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_agen.charAt(i)), x-304+(i*12), y-272, 0);
    			}	
	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x-307,y-290, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x-337,y-375, 0);
	    		/*
				Date tgl_buat = pemegang.getMspo_input_date();
				if (tgl_buat != null)
    			{
	    			Integer tgl_beg_date = tgl_buat.getDay();
	    			String tanggal_beg_date = FormatString.rpad("0",(Integer.toString(tgl_beg_date)),2);
	    			Integer bln_beg_date = tgl_buat.getMonth()+1;
	    			String bulan_beg_date = FormatString.rpad("0",(Integer.toString(bln_beg_date)),2);
	    			Integer thn_beg_date = tgl_buat.getYear()+1900;
	    			String tahun_beg_date = Integer.toString(thn_beg_date);
	    			
	    			for (int i =0 ; i< tanggal_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x-350+(i*5), y-555, 0);
	    				
	    			}
	    			over.showTextAligned(Element.ALIGN_LEFT, "-", x-340, y-555, 0);
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x-335+(i*5), y-555, 0);
	    				
	    			}
	    			over.showTextAligned(Element.ALIGN_LEFT, "-", x-325, y-555, 0);
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x-320+(i*5), y-555, 0);
	    			}
    			}
				kota_bank = data_rek.getMrc_kota();
    			over.showTextAligned(Element.ALIGN_LEFT,  kota_bank, x-350, y-540, 0);
				String nama_region= dataagen.getLsrg_nama() ;
				over.showTextAligned(Element.ALIGN_LEFT,  nama_region , x-125,y-642, 0);
				cabang_bank = data_rek.getMrc_cabang();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang_bank, x+25, y-642, 0);
    				*/		    		
    			
		return over;
	}
	
}
