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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Investimax {
	protected final Log logger = LogFactory.getLog( getClass() );
	private static Investimax instance;
	
	public static Investimax getInstance()
	{
		if( instance == null )
		{
			instance = new Investimax();
		}
		return instance;
	}
	
	public Investimax()
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
				over.showTextAligned(Element.ALIGN_LEFT,noBlanko, x-14, y-143, 0);
				String no_spaj = pemegang.getReg_spaj();
//				over.showTextAligned(Element.ALIGN_LEFT, FormatString.nomorSPAJ(no_spaj) , x-42, y+5, 0);
				
    			//data pemegang
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-340, y-211, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-340, y-226, 0);
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-242, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-230, y-242, 0);//x-230, y-242
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-196, y-240, 0);
	    			}
    			}
	    		String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-338+(i*12), y-254, 0);
	    			}
    			}
    			
    			Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    		if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-276, 0);//x-338, y-276
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-288, y-276, 0);//302
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-252, y-276, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-265+(i*10), y-288, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-223+(i*10), y-288, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-181+(i*12), y-288, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-340, y-286, 0);
    			
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-288, y-299, 0);// pria x-338, y-299
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-299, 0);// wanita
	    			}
    			}
    			
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-309, 0);//single
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-288, y-309, 0);//menikah
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-230, y-309, 0);//janda x-230, y-309
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-230, y-309, 0);//duda
	    					break;
	    			}
    			}				    			
    			
    			String nama_istri= pemegang.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-300, y-330, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak1(), x-300, y-342, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x-300, y-352, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x-300, y-362, 0);
    			
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-172+(i*3), y-330, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-162+(i*3), y-330, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x-150+(i*3), y-330, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-172+(i*3), y-340, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-162+(i*3), y-340, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x-150+(i*3), y-340, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(tgl_lhr.charAt(i)), x-172+(i*3), y-352, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,String.valueOf(bln_lhr.charAt(i)), x-162+(i*3), y-352, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x-150+(i*3), y-352, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-172+(i*3), y-362, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)),  x-162+(i*3), y-362, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(thn_lhr.charAt(i)), x-150+(i*3), y-362, 0);
	    			}
    			}
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-385, 0);//islam
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-288, y-385, 0);//protestan
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-230,y-385, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-171, y-385, 0);//hindu //x-184 //y-203
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-396, 0);//budha  x-338, y-396
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-406, 0);//smu //x-338, y-406
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-288, y-406, 0);//d1-d3
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-230, y-406, 0);//s1
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-171, y-406, 0);//s2
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-417, 0);//s3
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-288, y-417, 0);//lainnya
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan,x-249, y-417, 0);
	    					break;
	    			}
    			}
    			
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-340, y-429, 0);
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-194+(i*12),y-451, 0);
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-338+(i*12),y-463, 0);
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-282+(i*12),y-463, 0);
	    			}
    			}
    			String alamat_kantor = pemegang.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kantor, x-340, y-475, 0);
    			
    			String kode_pos_kantor = pemegang.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)), x-194+(i*12),y-497, 0);
	    			}
    			}
    			String area_kantor = pemegang.getArea_code_kantor();
    			if (area_kantor != null)
    			{
	    			for (int i =0 ; i< area_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kantor.charAt(i)), x-338+(i*12),y-509, 0);
	    			}
    			}
    			String telp_kantor = pemegang.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-282+(i*12),y-509, 0);
	    			}
    			}
    			String alamat_korespondensi = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-340, y-522, 0);
    			
    			String kode_pos_korespondensi = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-194+(i*12),y-546, 0);
		    			}
	    			}
    			}
    			
    			String area_korespondensi = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-338+(i*12),y-559, 0);
	    			}
    			}
    			
    			String telp_korespondensi = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-282+(i*12),y-559, 0);
	    			}
    			}
    			String no_hp=pemegang.getNo_hp();
    			if (no_hp != null)
	    		{
	    			for (int i =0 ; i< 4 ; i++)
	    	    	{
	    	    		over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-338+(i*12),y-571, 0);
	    	    	}

	    			for (int i =4 ; i< no_hp.length() ; i++)
	    	    	{
	    	    		over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-331+(i*12),y-571, 0);
	    	    	}
	    		}
    			String imel= pemegang.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT, imel, x-340, y-583, 0);
    			
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-340, y-595, 0);
    			
    			String uraian_kerja =pemegang.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-244, y-595, 0);
    			
    			String jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-340, y-605, 0);
    			String nm_company= pemegang.getMkl_industri();
    			over.showTextAligned(Element.ALIGN_LEFT,  nm_company , x-340, y-620, 0);
    			String bid_usaha= pemegang.getIndustria();
    			over.showTextAligned(Element.ALIGN_LEFT,  bid_usaha , x-340, y-632, 0);
    			
    			String tujuan = pemegang.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-645, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-230, y-645, 0);//244
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-338, y-657, 0); //x-338, y-657
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-230, y-657, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-668, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-230, y-668, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x-210, y-668, 0);
	    								}
    			}
    			String Sumberdana = pemegang.getMkl_pendanaan();
    			if (Sumberdana !=null)
    			{
	    			if (Sumberdana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-336, y-680, 0);
	    			}else if (Sumberdana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-287, y-680, 0);
	    					}else if (Sumberdana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-230, y-680, 0);//x-230, y-680
	    						}else if (Sumberdana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-336, y-692, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-287, y-692, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, Sumberdana, x-248, y-690, 0);
				    				}
    			}				    								
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-336, y-703, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-715, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-727, 0);//x-334, y-727
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-230, y-703, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-230, y-715, 0); //x-244, y-468
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-230, y-727, 0);
		    							}
    			}
    			
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-741, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-287, y-741, 0);
	    					}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-230, y-741, 0);//x-230, y-741
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-755, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-287, y-755, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-248, y-753, 0);
				    				}
    			}
    			
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			if (hub!=null){
    				if (hub.equalsIgnoreCase("Suami")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-338, y-769, 0);
    				}else if (hub.equalsIgnoreCase("Saudara Kandung")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-272, y-769, 0);
    				}else if (hub.equalsIgnoreCase("Istri")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-780, 0);
    				}else if (hub.equalsIgnoreCase("Orang Tua")){
    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-338, y-79, 0);
    				}else{
    					over.showTextAligned(Element.ALIGN_LEFT, hub, x-277, y-793, 0);//x-277, y-793
    				}
    			}
    			
    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-122, y-211, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-122, y-226, 0);
    			jns_identitas = tertanggung.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-117, y-242, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-10, y-242, 0);//x-10, y-242
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x+25, y-240, 0);
	    			}
    			}
	    		no_identitas = tertanggung.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-117+(i*12), y-254, 0);
	    			}
    			}
    			
    			id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (nama_negara != null)
    			{
	    		if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X",x-117, y-276, 0);//x-117, y-276
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-67, y-276, 0);//302
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-34, y-276, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-48+(i*10), y-288, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-4+(i*10), y-288, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x+38+(i*12), y-288, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_place_birth(), x-122, y-286, 0);
    			
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-67, y-299, 0);// x-118, y-299
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X",  x-118, y-299, 0);// wanita
	    			}
    			}
    			
    			sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-309, 0);//single
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-67, y-308, 0);//menikah
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-10, y-309, 0);//janda x-10, y-309
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-10, y-309, 0);//duda
	    					break;
	    			}
    			}
    			nama_istri= tertanggung.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-82, y-330, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak1(), x-82, y-342, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x-82, y-352, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x-82, y-362, 0);
    			
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+47+(i*3), y-330, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+59+(i*3), y-330, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+68+(i*3), y-330, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+47+(i*3), y-340, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+59+(i*3), y-340, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+68+(i*3), y-340, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+47+(i*3), y-350, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+59+(i*3), y-350, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+68+(i*3), y-350, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+47+(i*3), y-360, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+59+(i*3), y-360, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+68+(i*3), y-360, 0);
	    			}
    			}
    			
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-385, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-67, y-385, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-10,y-385, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+49, y-385, 0);//+34
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-396, 0);//x-118, y-396
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-407, 0);//x-118, y-407
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-67, y-407, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-10,y-407, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+49, y-407, 0);
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-118, y-418, 0);//
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-67, y-418, 0);
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x-27, y-418, 0);
	    					break;
	    			}
    			}
    			
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-122, y-429, 0);
    			
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x+26+(i*12),y-451, 0);
		    			}
	    			}
    			}
    			
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-118+(i*12),y-464, 0);
	    			}
    			}
    			
    				telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-65+(i*12),y-464, 0);
	    			}
    			}
    			
    			alamat_kantor = tertanggung.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kantor, x-122, y-475, 0);
    			
    			kode_pos_kantor = tertanggung.getKd_pos_kantor();
    			if (kode_pos_kantor != null)
    			{
	    			if ( kode_pos_kantor!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)), x+26+(i*12),y-498, 0);
		    			}
	    			}
    			}
    			
    			area_kantor = tertanggung.getArea_code_kantor();
    			if (area_kantor != null)
    			{
	    			for (int i =0 ; i< area_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kantor.charAt(i)), x-118+(i*12),y-510, 0);
	    			}
    			}
    			
    			telp_kantor = tertanggung.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-65+(i*12),y-510, 0);
	    			}
    			}
    			alamat_korespondensi = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_korespondensi, x-122, y-522, 0);
    			
    			kode_pos_korespondensi = tertanggung.getKd_pos_rumah();
    			if (kode_pos_korespondensi != null)
    			{
	    			if ( kode_pos_korespondensi!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_korespondensi.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_korespondensi.charAt(i)),x+26+(i*12),y-546, 0);
		    			}
	    			}
    			}
    			
    			area_korespondensi = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-118+(i*12),y-559, 0);
	    			}
    			}
    			
    			telp_korespondensi = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-65+(i*12),y-559, 0);
	    			}
    			}
    			
    			imel= tertanggung.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT, imel, x-122, y-583, 0);
    			
    			pekerjaan =  tertanggung.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-122, y-595, 0);
    			
    			uraian_kerja =tertanggung.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-23, y-595, 0);
    			
    			jabatan = tertanggung.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-122, y-605, 0);
    			nm_company= tertanggung.getMkl_industri();
    			over.showTextAligned(Element.ALIGN_LEFT,  nm_company , x-122, y-618, 0);
    			bid_usaha= tertanggung.getIndustria();
    			over.showTextAligned(Element.ALIGN_LEFT,  bid_usaha , x-122, y-632, 0);
    			
    			
    			tujuan = tertanggung.getMkl_tujuan();
    			if (tujuan != null)
    				if (tujuan != null)
        			{
    	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
    	    			{
    	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-645, 0);
    	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
    	    				{
    	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-10, y-645, 0);//244
    	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
    	    					{
    	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-118, y-657, 0); //x-118, y-657
    	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
    	    						{
    	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-10, y-657, 0);
    	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
    	    							{
    	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-668, 0);
    	    							}else{
    	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-10, y-668, 0);
    	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x+30, y-668, 0);
    	    								}
        			}				    								
    			penghasilan = tertanggung.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-703, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-715, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-727, 0);//x-334, y-727
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-10, y-703, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-10, y-715, 0); //x-244, y-468
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-10, y-727, 0);
		    							}
    			}
    			
    			dana = tertanggung.getMkl_pendanaan();
    			if (Sumberdana !=null)
    			{
	    			if (Sumberdana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-680, 0);
	    			}else if (Sumberdana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-67, y-680, 0);
	    					}else if (Sumberdana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-10, y-680, 0);//x-10, y-680
	    						}else if (Sumberdana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-692, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-67, y-692, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, Sumberdana, x-30, y-690, 0);
				    				}
    			}
    			Sumberdana = tertanggung.getMkl_pendanaan();
    			if (Sumberdana !=null)
    			{
	    			if (Sumberdana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-118, y-741, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-67, y-741, 0);
	    					}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-10, y-741, 0);//x-230, y-741
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X",x-118, y-755, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X",  x-67, y-755, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-30, y-753, 0);
				    				}
    			}
    			Integer nOne = reader.getNumberOfPages();
				
				logger.info("jumlah page:"+nOne);
				
				over.endText();
				
				over = stamp.getOverContent(2);
		    	over.beginText();
		    	over.setFontAndSize(bf, f);
//    			data datausulan
    			//String nama_produk = datausulan.getLsdbs_name();
    			//over.showTextAligned(Element.ALIGN_LEFT,  nama_produk , x-920, y-400, 0);
    			
    			String kurs = datausulan.getLku_id();
    			String krs= "";
    			if (kurs != null)
    			{
//	    			if (kurs.equalsIgnoreCase("01"))
//	    			{
//	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-362, y-563, 0);//362
//	    				krs= "Rp ";
//	    			}else{
//	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-310, y-563, 0);
//	    				krs = "U$ ";
//	    			}
    			}

    			Double premi = datausulan.getMspr_premium();
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-230, y-278, 0);
    			Double premi_top_up = datausulan.getMspr_premium();
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-230, y-278, 0);
    			
    			String no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-263, y-348, 0);
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-263, y-373, 0);
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-263, y-360, 0);
    			String cabang = dataagen.getLsrg_nama();
				over.showTextAligned(Element.ALIGN_LEFT, cabang , x-263,y-384, 0);
				over.showTextAligned(Element.ALIGN_LEFT, data_rek.getMrc_kota() , x-263, y-395, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x-322+(i*10), y-335, 0);
	    			}
	    			
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x-290+(i*12), y-335, 0);
	    			}
	    			
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x-253+(i*12), y-335, 0);
	    			}
    			}
    			
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
	    				
	    				over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-410,y-484-(i*14), 0);//x-356,y-587
	    				over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-205, y- (484+ (i*14)), 0);
	    				over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x-85,y- (484+ (i*14)), 0);
	    				over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x+40,y-(484+ (i*14)), 0);
	    			}
	    		}
    			
	    		logger.info("jumlah page:"+nOne);
				
				over.endText();
				
				over = stamp.getOverContent(3);
		    	over.beginText();
		    	over.setFontAndSize(bf, f);
		    	
    			String nama_agen = dataagen.getMcl_first();	
	    		String kode_agen = dataagen.getMsag_id();
//	    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x-30,y-837, 0);
//	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x-30,y-827, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-430,y-673, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-256,y-673, 0);/*
				Date tgl_spaj = pemegang.getMspo_spaj_date();
   		
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
