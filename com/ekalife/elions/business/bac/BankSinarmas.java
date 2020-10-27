package com.ekalife.elions.business.bac;

import java.math.BigDecimal;
import java.util.Date;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.InvestasiUtama;
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

public class BankSinarmas {

	private static BankSinarmas instance;
	
	public static BankSinarmas getInstance()
	{
		if( instance == null )
		{
			instance = new BankSinarmas();
		}
		return instance;
	}
	
	public BankSinarmas()
	{
	}
	
	public PdfContentByte createPdf( BacSpajParamVO paramVO )
	{
		PdfContentByte over = paramVO.getOver(); 
		Pemegang pemegang = paramVO.getPemegang(); 
		Tertanggung tertanggung = paramVO.getTertanggung(); 
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
		String noBlanko= paramVO.getNoBlanko();
	
				int tambah = 60;
//				data pemegang
				
				over.showTextAligned(Element.ALIGN_LEFT, noBlanko, x-5, y-1, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-340, y-32, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-340, y-51, 0);
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-63, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-147, y-63, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-112, y-63, 0);
	    			}
    			}
	    		String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-330+(i*12), y-73, 0);
	    			}
    			}
    			
    			Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    		if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-84, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-257, y-84, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-223, y-83, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-320+(i*13), y-94, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-245+(i*13), y-94, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-173+(i*13), y-94, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-80, y-93, 0);
    			
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-267, y-104, 0);// pria
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-104, 0);// wanita
	    			}
    			}
    			
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-142, y-105, 0);//single
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-81, y-104, 0);//menikah
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-18, y-103, 0);//janda
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-18, y-103, 0);//duda
	    					break;
	    			}
    			}				    			
    			
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-115, 0);//islam
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-268, y-115, 0);//protestan
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-203,y-115, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-80, y-115, 0);//budha
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-142, y-115, 0);//hindu
	    					break;
	    			}
    			}
    			
    			 String nama_agama = pemegang.getLsag_name();
    			
    			Integer id_pendidikan = pemegang.getLsed_id();
    			String pendidikan = pemegang.getLsed_name();
    			if (pendidikan != null)
    			{
	    			switch (id_pendidikan)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-125, 0);//smu
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-270, y-125, 0);//d1-d3
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-203, y-125, 0);//s1
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-140, y-125, 0);//s2
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-80, y-125, 0);//s3
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-18, y-125, 0);//lainnya
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x +20, y-123, 0);
	    					break;
	    			}
    			}
    			
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-335, y-133, 0);
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-330+(i*12),y-148, 0);
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-203+(i*10),y-148, 0);
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-144+(i*12),y-148, 0);
	    			}
    			}

    			
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-335, y-157, 0);
    			
    			String uraian_kerja =pemegang.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-335, y-166, 0);
    			
    			String jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-86, y-157, 0);
    			
    			String tujuan = pemegang.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-178, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-215, y-178, 0);
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-104, y-178, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-188, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-215, y-188, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-100, y-188, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x-50, y-188, 0);
	    								}
    			}				    								
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-198, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-206, 0);
	    					}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-215, y-198, 0);
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-215, y-208, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-103, y-198, 0);
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-103, y-209, 0);
		    							}
    			}
    			
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-220, 0);
	    		}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-268, y-220, 0);
	    					}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-205, y-220, 0);
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-133, y-220, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-78, y-220, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-3, y-218, 0);
				    				}
    			}
    			
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x-275, y-235, 0);
    			
    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-340, y-273, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-340, y-293, 0);
    			jns_identitas = tertanggung.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-305, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-150, y-305, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-112,y-305, 0);
	    			}
    			}
    			no_identitas = tertanggung.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-330+(i*12), y-315, 0);
	    			}
    			}
    			
    			id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-325, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-257, y-325, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-223, y-325, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-315+(i*10), y-335, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-245+(i*10), y-335, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-170+(i*12), y-335, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_place_birth(), x-80, y-335, 0);
    			
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-267, y-346 ,0);//wanita
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-346, 0);//pria
	    			}
    			}
    			
    			sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-143, y-346, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-81, y-346, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-22, y-346, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-22, y-346, 0);
	    					break;
	    			}
    			}				    			
    			
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-356, 0);
	    				break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-268, y-356, 0);
	    				break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-200,y-356, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-81, y-356, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-143, y-356, 0);
	    					break;
	    			}
    			}
    			
    				nama_agama = tertanggung.getLsag_name();
    			
    			id_pendidikan = tertanggung.getLsed_id();
    			pendidikan = tertanggung.getLsed_name();
    			if (pendidikan != null)
    			{
	    			switch (id_pendidikan)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-366, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-267, y-366, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-205, y-366, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-145, y-366, 0);
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-80, y-366, 0);
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-20, y-365, 0);
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x+20 , y-365, 0);
	    					break;
	    			}
    			}
    			
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-335, y-374, 0);
    			
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-330+(i*12),y-395, 0);
		    			}
	    			}
    			}
    			
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-225+(i*13),y-395, 0);
	    			}
    			}
    			
    				telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-166+(i*12),y-395, 0);
	    			}
    			}
    			
    			pekerjaan =  tertanggung.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-335, y-405, 0);
    			
    			uraian_kerja =tertanggung.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-335, y-414, 0);
    			
    			jabatan = tertanggung.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-86, y-405, 0);
    			
    			
//    			data datausulan

    			String kurs = datausulan.getLku_id();
    			String krs= "";
    			if (kurs != null)
    			{
	    			if (kurs.equalsIgnoreCase("01"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-365 , y-563, 0);
	    				krs= "Rp ";
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-310, y-563, 0);
	    				krs = "U$ ";
	    			}
    			}

    			Double premi = datausulan.getMspr_premium();
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-260, y-563, 0);
    			
    			String nama_produk = datausulan.getLsdbs_name();
    			if (nama_produk.contains("SIMPONI"))
    			{
    				if (kurs.equalsIgnoreCase("01"))
    				{
    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-365, y-543, 0);
    				}else{
    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-300, y-543, 0);
    				}
    			}else{
    				if (kurs.equalsIgnoreCase("01"))
    				{
    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-235, y-543, 0);
    				}else{
    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-155, y-543, 0);
    				}
    			}
    			
    			Date mulai_tanggung = datausulan.getMste_beg_date();
    			if (mulai_tanggung != null)
    			{
	    			Integer tgl_beg_date = mulai_tanggung.getDay();
	    			String tanggal_beg_date = FormatString.rpad("0",(Integer.toString(tgl_beg_date)),2);
	    			Integer bln_beg_date = mulai_tanggung.getMonth()+1;
	    			String bulan_beg_date = FormatString.rpad("0",(Integer.toString(bln_beg_date)),2);
	    			Integer thn_beg_date = mulai_tanggung.getYear()+1900;
	    			String tahun_beg_date = Integer.toString(thn_beg_date);
	    			
	    			for (int i =0 ; i< tanggal_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x-23+(i*10), y-564, 0);
	    			}
	    			
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x+15+(i*10), y-564, 0);
	    			}
	    			
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x+50+(i*10), y-564, 0);
	    			}
    			}
    			
    			
    			
    			String mgi = data_pwr.getMps_jangka_inv();
    			if (mgi !=null)
    			{
    			
	    			switch (Integer.parseInt(mgi))
	    			{
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-16, y-553, 0);
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+25, y-553, 0);
	    					break;
	    				case 12:
	    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+60, y-553, 0);
	    				break;
	    				
	    			}
    			}				    			
    				Integer rollover = data_pwr.getMps_roll_over();
    			if (rollover != null)
    			{
    				switch (rollover)
    				{
    					case 1:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-345, y-657, 0);
    						break;
    					case 2:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-107, y-657, 0);
    						break;
    				}
    				
    			}
    			
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-90, y-665, 0);
    			
    			String no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-230, y-665, 0);
    			
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-230, y-673, 0);
    			
    			String cabang_bank = data_rek.getMrc_cabang();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang_bank, x-90, y-673, 0);
    			
    			String kota_bank = data_rek.getMrc_kota();
    			over.showTextAligned(Element.ALIGN_LEFT,  kota_bank, x+40, y-665, 0);
    		
    			Integer jumlah_benef = datausulan.getDaftabenef().size();
    			if(jumlah_benef > 3) jumlah_benef=3;
	    		if (jumlah_benef.intValue() > 0)
	    		{
	    			for (int i = 0 ; i < jumlah_benef; i++)
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
	    				
	    				over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-447,y- 600-(i*8), 0);
	    				
	    				over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-140, y- (600+ (i*8)), 0);
	    				over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x-50,y- (600+ (i*8)), 0);
	    				over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x+55,y-(600+ (i*8)), 0);
	    			}
	    		}
	    			
	    		String nama_agen = dataagen.getMcl_first();	
	    		String kode_agen = dataagen.getMsag_id();
	    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x-15,y-833, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x-15,y-823, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-452,y-820, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-318,y-820, 0);
				Date tgl_spaj = pemegang.getMspo_spaj_date();
				
				String cabang = dataagen.getLsrg_nama();
				//over.showTextAligned(Element.ALIGN_LEFT, cabang , x-365,y-610, 0);
				String no_spaj = pemegang.getReg_spaj();
				over.showTextAligned(Element.ALIGN_LEFT, FormatString.nomorSPAJ(no_spaj) , x-400, y, 0);
				
    			
		return over;
	}
	
}
