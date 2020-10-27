package com.ekalife.elions.business.bac;

import java.math.BigDecimal;
import java.util.Date;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.vo.BacSpajParamVO;
import com.ekalife.utils.FormatString;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfContentByte;

public class Maxi {

	private static Maxi instance;
	
	public static Maxi getInstance()
	{
		if( instance == null )
		{
			instance = new Maxi();
		}
		return instance;
	}
	
	public Maxi()
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
		String noBlanko= paramVO.getNoBlanko();
	
				int tambah = 60;
				
				over.showTextAligned(Element.ALIGN_LEFT, noBlanko , x-3, y, 0);
//				data pemegang
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-297, y-27, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-297, y-44, 0);
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-294, y-55, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-185, y-56, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-140, y-56, 0);
	    			}
    			}
	    		String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-295+(i*10), y-68, 0);
	    			}
    			}
    			
		    	Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-293, y-80, 0);//indonesia
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-185, y-81, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-156, y-81, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-275+(i*11), y-91, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-225+(i*10), y-91, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-185+(i*10), y-91, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-120, y-91, 0);
    			
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-255, y-101, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-293, y-101, 0);
	    			}
    			}
    			
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-107, y-101, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-31, y-101, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+23, y-101, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+23, y-101, 0);
	    					break;
	    			}
    			}				    			
    			
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-295, y-111, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-255,y-111, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-205, y-111, 0);
	    						break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-157, y-111, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-107, y-111, 0);
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
	    				case 8:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-295, y-121, 0);//sd
	    					break;
	    				case 9:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-255, y-121, 0);//smp
	    					break;
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-205, y-121, 0);//smu
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-157, y-121, 0);//s1
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-107, y-121, 0);//s2
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-73, y-121, 0);//s3
	    					break; 
	    				 case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-32, y-121, 0);
	    					break; 
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+23, y-121, 0);
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x + 60, y-121, 0);
	    					break;
	    			}
    			}
    			
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-295, y-133, 0);
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-295+(i*10),y-158, 0);
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-207+(i*10),y-158, 0);
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-160+(i*10),y-158, 0);
	    			}
    			}
    			
    			
    			
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-295, y-171, 0);
    			
    			String uraian_kerja =pemegang.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-295, y-182, 0);
    			
    			String jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x+25, y-172, 0);
    			
    			String tujuan = pemegang.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-257, y-193, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-139, y-193, 0);
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-30, y-193, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-259, y-203, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-142, y-203, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-31, y-203, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x+35, y-203, 0);
	    								}
    			}				    								
    			
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-257, y-213, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-257, y-222, 0);
	    				}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-139, y-212, 0);
	    					}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-142, y-223, 0);
		    					}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-32, y-213, 0);
		    						}else if (penghasilan.indexOf("> RP. 500 JUTA") >= 0)
		    							
		    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-31, y-222, 0);
		    							}
    			
    			
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-257, y-234, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-224, y-235, 0);
	    				}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-163, y-234, 0);
	    					}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-94, y-235, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-31, y-235, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x+40, y-235, 0);
	    						}
    			}
    			
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x-205, y-248, 0);
    			
    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-295, y-285,  0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-295, y-305, 0);
    			jns_identitas = tertanggung.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-293, y-315, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-182, y-315, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-135, y-315, 0);
	    			}
    			}
    			
	    		no_identitas = tertanggung.getMspe_no_identity();
	    		if (no_identitas  !=null)
	    		{
	    			for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)),  x-295+(i*10), y-328,0);
	    			}
	    		}
	    		
	    		id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (id_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-293, y-340, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-182, y-340, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-135, y-340, 0);
	    			}
    			}
    			
    			tanggal_lahir = tertanggung.getMspe_date_birth();
    			if (tanggal_lahir!=null)
    			{
	    			Integer tgl_lahir = tanggal_lahir.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tanggal_lahir.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tanggal_lahir.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-275+(i*10), y-351, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-222+(i*10), y-351, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-185+(i*10), y-351, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_place_birth(), x-120, y-350, 0);
    			
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin != null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-253, y-360, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-293, y-360, 0);
	    			}
    			}
    			
    			sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital !=null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-108, y-360, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-31, y-360, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+23, y-360, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+23, y-360, 0);
	    					break;
	    			}
    			}
    			
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama !=null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-295, y-371, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-253, y-371, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-204, y-371, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-107, y-371, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-154, y-371, 0);
	    					break;
	    			}
    			}
    			
    			nama_agama = tertanggung.getLsag_name();
    			
    			id_pendidikan = tertanggung.getLsed_id();
    			pendidikan = tertanggung.getLsed_name();
    			
    			switch (id_pendidikan)
    			{
    				case 8:
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-295, y-381, 0);
    					break;
    				case 9:
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-253, y-381, 0);
    					break;
    				case 1:
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-204, y-381, 0);
    					break;
    				case 3:
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-155, y-381, 0);
    					break;
    				case 4:
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-107, y-381, 0);
    					break;
    				case 5:
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-72, y-381, 0);
    					break; 
    				 case 6:
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-34, y-381, 0);
    					break; 
    				default:
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+23, y-381, 0);
    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x + 65, y-381, 0);
    					break;
    			}
    			
    			
	    			
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-294, y-392, 0);
    			
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-295+(i*10),y-416, 0);
		    			}
	    			}
    			}
    			
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-209+(i*10),y-416, 0);
	    			}
    			}
    			
    			telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-156+(i*10),y-416, 0);
	    			}
    			}
    			
    			pekerjaan =  tertanggung.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-295, y-428, 0);
    			
    			uraian_kerja =tertanggung.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-295, y-438, 0);
    			
    			jabatan = tertanggung.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x+40, y-428, 0);
    			
    			//data datausulan
    			String kurs = datausulan.getLku_id();
    			String krs="";
    	
    			Integer kode_bisnis  = datausulan.getLsbs_id();
    			if (kode_bisnis == 93)
    			{
    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-300, y-578, 0);
    			}
    			if ((kode_bisnis == 103) || (kode_bisnis == 137))
    			{
    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-185, y-578, 0);
    			}
    			if (kode_bisnis == 114)
    			{
    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-40, y-578, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x+30+(i*10), y-590, 0);
	    			}
	    			
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x+60+(i*10), y-590, 0);
	    			}
	    			
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x+90+(i*10), y-590, 0);
	    			}
    			}
    			
    			Double premi = datausulan.getMspr_premium();
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-275, y-588, 0);
    			
    			
    			
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
    					
    					over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-376, y-631 + (i*12), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-90, y-631+ (i*12), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x+6,y-631+ (i*12), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x+100,y-631+ (i*12), 0);
    				}
    				
    				String no_spaj = pemegang.getReg_spaj();
    				Date tgl_spaj = pemegang.getMspo_spaj_date();
    				String cabang = dataagen.getLsrg_nama();
    				over.showTextAligned(Element.ALIGN_LEFT, FormatString.nomorSPAJ(no_spaj) , x-340, y, 0);
		    		String nama_agen = dataagen.getMcl_first();	
		    		String kode_agen = dataagen.getMsag_id();
		    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x+7,y-833, 0);
		    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x+7,y-840, 0);
		    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-277,y-831, 0);
		    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-385,y-831, 0);
    		}		    		
    			
		return over;
	}
	
}
