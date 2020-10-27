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

public class Buana {

	private static Buana instance;
	
	public static Buana getInstance()
	{
		if( instance == null )
		{
			instance = new Buana();
		}
		return instance;
	}
	
	public Buana()
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
	
				int tambah = 60;
//				over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspo_no_blanko() , x-433, y+2, 0);
				String no_spaj = pemegang.getReg_spaj();
//				over.showTextAligned(Element.ALIGN_LEFT, FormatString.nomorSPAJ(no_spaj) , x-42, y+5, 0);
				
    			//data pemegang
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-346, y-75, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-346, y-87, 0);
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-97, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-97, 0);//x-237, y-97
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-202, y-96, 0);
	    			}
    			}
	    		String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-344+(i*12), y-108, 0);
	    			}
    			}
    			Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    		if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-130, 0);//x-344, y-130
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-294, y-130, 0);//302
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-258, y-130, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-271+(i*10), y-142, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-230+(i*10), y-142, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-184+(i*12), y-142, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-346, y-139, 0);
    			
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-149, 0);// pria x-344, y-149
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X",x-294, y-149, 0);// wanita
	    			}
    			}
    			
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-158, 0);//single
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-294, y-158, 0);//menikah
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-158, 0);//janda x-230, y-309
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-158, 0);//duda
	    					break;
	    			}
    			}				    			
    			
    			String nama_istri= pemegang.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-307, y-178, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak1(), x-307, y-189, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x-307, y-201, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x-307, y-211, 0);
    			
    			Date tgllahirCouple = pemegang.getTgllhr_si();
    			if (tgllahirCouple != null)
    			{
	    			Integer tgl_lahir = tgllahirCouple.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirCouple.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirCouple.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-178+(i*3), y-177, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-167+(i*3), y-177, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x-158+(i*3), y-177, 0);
	    			}
    			}
    			Date tgllahirAnk1 = pemegang.getTgllhr_anak1();
    			if (tgllahirAnk1 != null)
    			{
	    			Integer tgl_lahir = tgllahirAnk1.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirAnk1.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirAnk1.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-178+(i*3), y-188, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-167+(i*3), y-188, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x-158+(i*3), y-188, 0);
	    			}
    			}
    			Date tgllahirAnk2 = pemegang.getTgllhr_anak2();
    			if (tgllahirAnk2 != null)
    			{
	    			Integer tgl_lahir = tgllahirAnk2.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirAnk2.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirAnk2.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(tgl_lhr.charAt(i)), x-178+(i*3), y-200, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,String.valueOf(bln_lhr.charAt(i)), x-167+(i*3), y-200, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x-158+(i*3), y-200, 0);
	    			}
    			}
    			Date tgllahirAnk3 = pemegang.getTgllhr_anak3();
    			if (tgllahirAnk3 != null)
    			{
	    			Integer tgl_lahir = tgllahirAnk3.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirAnk3.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirAnk3.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-178+(i*3), y-210, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)),  x-167+(i*3), y-210, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(thn_lhr.charAt(i)), x-158+(i*3), y-210, 0);
	    			}
    			}
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-220, 0);//islam
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-294, y-220, 0);//protestan
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-237,y-220, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-178, y-220, 0);//hindu //x-184 //y-203
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-344, y-230, 0);//budha x-344, y-230
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-239, 0);//smu //x-338, y-406
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-294, y-239, 0);//d1-d3
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-239, 0);//s1
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-177, y-239, 0);//s2
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-249, 0);//s3
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-294, y-249, 0);//lainnya  x-294, y-251
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan,x-252, y-249, 0);
	    					break;
	    			}
    			}
    			
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-346, y-259, 0);
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-199+(i*12),y-276, 0);
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-344+(i*12),y-287, 0);
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-291+(i*12),y-287, 0);
	    			}
    			}
    			String alamat_kantor = pemegang.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kantor, x-346, y-297, 0);
    			
    			String kode_pos_kantor = pemegang.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)), x-199+(i*12),y-315, 0);
	    			}
    			}
    			String area_kantor = pemegang.getArea_code_kantor();
    			if (area_kantor == null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-344+(i*12),y-325, 0);
	    			}
    			}
    			String telp_kantor = pemegang.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-291+(i*12),y-325, 0);
	    			}
    			}
    			String alamat_korespondensi = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-346, y-335, 0);
    			
    			String kode_pos_korespondensi = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-199+(i*12),y-354, 0);
		    			}
	    			}
    			}
    			String area_korespondensi = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-344+(i*12),y-364, 0);
	    			}
    			}
    			String telp_korespondensi = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-291+(i*12),y-364, 0);
	    			}
    			}
    			String no_hp=pemegang.getNo_hp();
    			if (no_hp != null)
	    		{
	    			for (int i =0 ; i< 4 ; i++)
	    	    	{
	    	    		over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-344+(i*12),y-376, 0);
	    	    	}

	    			for (int i =4 ; i< no_hp.length() ; i++)
	    	    	{
	    	    		over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-338+(i*12),y-376, 0);
	    	    	}
	    		}
    			String imel= pemegang.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT, imel, x-346, y-386, 0);
    			
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-346, y-395, 0);
    			
    			String uraian_kerja =pemegang.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-248, y-395, 0);
    			
    			String jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-346, y-405, 0);
//    			String nm_company= pemegang.getMkl_industri();
//    			over.showTextAligned(Element.ALIGN_LEFT,  nm_company , x-340, y-620, 0);
//    			String bid_usaha= pemegang.getIndustria();
//    			over.showTextAligned(Element.ALIGN_LEFT,  bid_usaha , x-340, y-632, 0);
    			
    			String tujuan = pemegang.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-415, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-415, 0);//244
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-344, y-425, 0); //x-344, y-425
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-425, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-435, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-435, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x-197, y-435, 0);
	    								}
    			}
    			String Sumberdana = pemegang.getMkl_pendanaan();
    			if (Sumberdana !=null)
    			{
	    			if (Sumberdana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-447, 0);
	    			}else if (Sumberdana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-294, y-447, 0);
	    					}else if (Sumberdana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-447, 0);//x-237, y-447
	    						}else if (Sumberdana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-457, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-294, y-457, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, Sumberdana, x-256, y-453, 0);
				    				}
    			}				    								
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-467, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-478, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-467, 0);//x-237, y-467
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-487, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-478, 0); //x-244, y-468
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-487, 0);
		    							}
    			}
    			
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
    	    		if (dana.equalsIgnoreCase("Gaji"))
    	    		{
    	    			over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-499, 0);
    	    		}else if (dana.equalsIgnoreCase("Hasil Usaha"))
    	    			{
    	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-294, y-499, 0);
    	    			}else if (dana.equalsIgnoreCase("Hasil Investasi"))
    	    				{
    	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-499, 0);//x-237, y-499
    	    				}else if (dana.equalsIgnoreCase("Warisan"))
    	    					{
    	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-509, 0);
    	    					}else {
    	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-294, y-509, 0);
    				    			over.showTextAligned(Element.ALIGN_LEFT, dana, x-256, y-507, 0);
    				    		}
        			}
    			
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x-346, y-523, 0);//x-277, y-793
    			
    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-126, y-75, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-126, y-87, 0);
    			jns_identitas = tertanggung.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-98, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X",x-17, y-98, 0);//x-17, y-98
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x+17, y-98, 0);
	    			}
    			}
	    		no_identitas = tertanggung.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-125+(i*12), y-108, 0);
	    			}
    			}
    			id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (nama_negara != null)
    			{
	    		if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-131, 0);//x-125, y-130
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-75, y-131, 0);//302
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-258, y-130, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-55+(i*10), y-142, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-10+(i*10), y-142, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x+34+(i*12), y-142, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_place_birth(), x-126, y-139, 0);
    			
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-75, y-149, 0);// pria x-125, y-149
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X",x-75, y-149, 0);// wanita
	    			}
    			}
    			
    			sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-158, 0);//single
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-75, y-158, 0);//menikah
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-17, y-158, 0);//janda x-230, y-309
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-17, y-158, 0);//duda
	    					break;
	    			}
    			}				    			
    			
    			nama_istri= tertanggung.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-88, y-178, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getNama_anak1(), x-88, y-189, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getNama_anak2(), x-88, y-201, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getNama_anak3(), x-88, y-211, 0);
    			
    			tgllahirCouple = tertanggung.getTgllhr_si();
    			if (tgllahirCouple != null)
    			{
	    			Integer tgl_lahir = tgllahirCouple.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirCouple.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirCouple.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+40+(i*3), y-177, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+52+(i*3), y-177, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+63+(i*3), y-177, 0);
	    			}
    			}
    			tgllahirAnk1 = tertanggung.getTgllhr_anak1();
    			if (tgllahirAnk1 != null)
    			{
	    			Integer tgl_lahir = tgllahirAnk1.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirAnk1.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirAnk1.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+40+(i*3), y-188, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+52+(i*3), y-188, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+63+(i*3), y-188, 0);
	    			}
    			}
    			tgllahirAnk2 = tertanggung.getTgllhr_anak2();
    			if (tgllahirAnk2 != null)
    			{
	    			Integer tgl_lahir = tgllahirAnk2.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirAnk2.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirAnk2.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(tgl_lhr.charAt(i)), x+40+(i*3), y-200, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,String.valueOf(bln_lhr.charAt(i)), x+52+(i*3), y-200, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+63+(i*3), y-200, 0);
	    			}
    			}
    			tgllahirAnk3 = tertanggung.getTgllhr_anak3();
    			if (tgllahirAnk3 != null)
    			{
	    			Integer tgl_lahir = tgllahirAnk3.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirAnk3.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirAnk3.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)),x+40+(i*3), y-210, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)),  x+52+(i*3), y-210, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(thn_lhr.charAt(i)), x+63+(i*3), y-210, 0);
	    			}
    			}
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-220, 0);//islam
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-75, y-220, 0);//protestan
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-17, y-220, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x+44, y-220, 0);//hindu //x-184 //y-203
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-230, 0);//budha x-125, y-230
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-239, 0);//smu //x-338, y-406
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-75, y-239, 0);//d1-d3
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-237, y-239, 0);//s1
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-177, y-239, 0);//s2
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-344, y-249, 0);//s3
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-75, y-249, 0);//lainnya  x-294, y-251
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan,x-35, y-249, 0);
	    					break;
	    			}
    			}
    			
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-125, y-259, 0);
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x+19+(i*12),y-277, 0);
		    			}
	    			}
    			}
    			
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-125+(i*12),y-287, 0);
	    			}
    			}
    			
    			telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-70+(i*12),y-287, 0);
	    			}
    			}
    			alamat_kantor = tertanggung.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kantor, x-125, y-297, 0);
    			
    			kode_pos_kantor = tertanggung.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)), x+19+(i*12),y-315, 0);
	    			}
    			}
    			area_kantor = tertanggung.getArea_code_kantor();
    			if (area_kantor == null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-125+(i*12),y-325, 0);
	    			}
    			}
    			telp_kantor = tertanggung.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-70+(i*12),y-325, 0);
	    			}
    			}
    			alamat_korespondensi = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-125, y-335, 0);
    			
    			kode_pos_korespondensi = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x+19+(i*12),y-354, 0);
		    			}
	    			}
    			}
    			area_korespondensi = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-125+(i*12),y-364, 0);
	    			}
    			}
    			telp_korespondensi = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-70+(i*12),y-364, 0);
	    			}
    			}
    			no_hp=tertanggung.getNo_hp();
    			if (no_hp != null)
	    		{
	    			for (int i =0 ; i< 4 ; i++)
	    	    	{
	    	    		over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-125+(i*12),y-376, 0);
	    	    	}

	    			for (int i =4 ; i< no_hp.length() ; i++)
	    	    	{
	    	    		over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-120+(i*12),y-376, 0);
	    	    	}
	    		}
    			imel= tertanggung.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT, imel, x-125, y-386, 0);
    			
    			pekerjaan =  tertanggung.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-125, y-395, 0);
    			
    			uraian_kerja =tertanggung.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-35, y-395, 0);
    			
    			jabatan = tertanggung.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-346, y-405, 0);
//    			String nm_company= pemegang.getMkl_industri();
//    			over.showTextAligned(Element.ALIGN_LEFT,  nm_company , x-340, y-620, 0);
//    			String bid_usaha= pemegang.getIndustria();
//    			over.showTextAligned(Element.ALIGN_LEFT,  bid_usaha , x-340, y-632, 0);
    			
    			tujuan = tertanggung.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-415, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-17, y-415, 0);//244
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-125, y-425, 0); //x-125, y-425
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-17, y-425, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-435, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-17, y-435, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x+20, y-435, 0);
	    								}
    			}
    			Sumberdana = tertanggung.getMkl_pendanaan();
    			if (Sumberdana !=null)
    			{
	    			if (Sumberdana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-447, 0);
	    			}else if (Sumberdana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-294, y-447, 0);
	    					}else if (Sumberdana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-17, y-447, 0);//x-17, y-447
	    						}else if (Sumberdana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-457, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-75, y-457, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, Sumberdana, x-34, y-453, 0);
				    				}
    			}				    								
    			penghasilan = tertanggung.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-467, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-17, y-478, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-17, y-467, 0);//x-237, y-467
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-487, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-478, 0); //x-244, y-468
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-1, y-487, 0);
		    							}
    			}
    			dana = tertanggung.getMkl_pendanaan();
    			if (dana !=null)
    			{
    	    		if (dana.equalsIgnoreCase("Gaji"))
    	    		{
    	    			
    	    			over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-499, 0);
    	    		}else if (dana.equalsIgnoreCase("Hasil Usaha"))
    	    			{
    	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-75, y-499, 0);
    	    			}else if (dana.equalsIgnoreCase("Hasil Investasi"))
    	    				{
    	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-17, y-499, 0);//x-237, y-499
    	    				}else if (dana.equalsIgnoreCase("Warisan"))
    	    					{
    	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-125, y-509, 0);
    	    					}else {
    	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-75, y-509, 0);
    				    			over.showTextAligned(Element.ALIGN_LEFT, dana, x+20, y-507, 0);
    				    		}
        			}
    			String kurs = datausulan.getLku_id();
    			String krs= "";
    			if (kurs != null)
    			{
	    			if (kurs.equalsIgnoreCase("01"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-354, y-554, 0);//x-354, y-553
	    				krs= "Rp ";
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x-303, y-554, 0);
	    				krs = "U$ ";
	    			}
    			}
    			Double premi = datausulan.getMspr_premium();
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-268, y-553, 0);
    			
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
	    				
	    				over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-360,y-572-(i*10), 0);//x-356,y-587
	    				over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-114, y- (572+ (i*10)), 0);
	    				over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x-38,y- (572+ (i*10)), 0);
	    				over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x+40,y-(572+ (i*10)), 0);
	    			}
	    		}
    			
    			String no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-239, y-645, 0);
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-100, y-645, 0);
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-239, y-652, 0);
    			String cabang = dataagen.getLsrg_nama();
				over.showTextAligned(Element.ALIGN_LEFT, cabang , x-100,y-652, 0);
				over.showTextAligned(Element.ALIGN_LEFT, data_rek.getMrc_kota() , x+30, y-645, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x-58+(i*10), y-610, 0);
	    			}
	    			
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x-23+(i*12), y-610, 0);
	    			}
	    			
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x+10+(i*12), y-610, 0);
	    			}
    			}
    			
    			
    			String nama_agen = dataagen.getMcl_first();	
	    		String kode_agen = dataagen.getMsag_id();
	    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x+2,y-824, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x+2,y-830, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-445,y-881, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-310,y-881, 0);
	    		/*Date tgl_spaj = pemegang.getMspo_spaj_date();x+5
   		
    			String nama_produk = datausulan.getLsdbs_name();
    			Integer idx = nama_produk.indexOf("BULAN");
   			
    			String mgi = data_pwr.getMps_jangka_inv();
    			if (mgi !=null)
    			{
    			
	    			switch (Integer.parseInt(mgi))
	    			{
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-362, y-624, 0);//x-362, y-624
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-325, y-624, 0);
	    					break;
	    				case 12:
	    					if (idx <0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-290, y-624, 0);
	    					}else{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-251, y-624, 0);
	    					}
	    					break;
//	    				case 36:
//	    					if (idx <0)
//	    					{
//	    						over.showTextAligned(Element.ALIGN_LEFT,  "F" , x+225, y-624, 0);
//	    					}else{
//	    						over.showTextAligned(Element.ALIGN_LEFT,  "W" , x+348, y-624, 0);
//	    					}
//	    					break;
	    			}
    			}				    			
    			Integer rollover = data_pwr.getMps_roll_over();
    			if (rollover != null)
    			{
    				switch (rollover)
    				{
    					case 1:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+59, y-418, 0);
    						break;
    					case 2:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+59, y-428, 0);
    						break;
    					case 3:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-110, y-665, 0);
    						break;
    				}
    				
    			}
    			
    			String kurs_rek = data_rek.getMrc_kurs();
    			if (kurs_rek != null)
    			{
    				if (kurs_rek.equalsIgnoreCase("01")){
//    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+275, y-438, 0);
    				}else{
//    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+309, y-438, 0);
    				}
    			}

    			*/
		return over;
	}
	
}
