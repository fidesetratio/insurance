package com.ekalife.elions.business.bac;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Datarider;
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

public class Cerdas {
	protected final Log logger = LogFactory.getLog( getClass() );
	private static Cerdas instance;
	
	public static Cerdas getInstance()
	{
		if( instance == null )
		{
			instance = new Cerdas();
		}
		return instance;
	}
	
	public Cerdas()
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
		String noBlanko= paramVO.getNoBlanko();
	
				int tambah = 60;
		
//				data pemegang
				over.showTextAligned(Element.ALIGN_LEFT, noBlanko , x+10, y-117, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-288, y-145, 0);  
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-288, y-162, 0);
    			
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-285, y-173, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-177, y-173, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-137, y-173, 0);
	    				}
    			}
    			String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-285+(i*10), y-187, 0);
	    			}
    			}
    			
    			Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-285, y-201, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-176, y-201, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-145, y-201, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-266+(i*10), y-215, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-216+(i*10), y-215, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-176+(i*10), y-215, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-115, y-213, 0);
    			
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-245, y-227, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-227, 0);
	    				}
    			}
    			
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-240, 0);//blum kawin
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-245, y-240, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-177, y-240, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-177, y-240, 0);
	    					break;
	    			}
    			}
    			String nama_istri= pemegang.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-219, y-268, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak1(), x-219, y-280, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x-219, y-292, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x-219, y-303, 0);
    			
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
	    				over.showTextAligned(Element.ALIGN_LEFT, tgl_lhr, x+50, y-268, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, bln_lhr, x+80, y-268, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, thn_lhr, x+100, y-268, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, tgl_lhr, x+50, y-280, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, bln_lhr, x+80, y-280, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, thn_lhr, x+100, y-280, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, tgl_lhr, x+50, y-292, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, bln_lhr, x+80, y-292, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, thn_lhr, x+100, y-292, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, tgl_lhr, x+50, y-303, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, bln_lhr, x+80, y-303, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, thn_lhr, x+100, y-303, 0);
	    			}
    			}
    			
    			
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-317, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-245, y-317, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-196,y-317, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-147, y-317, 0);//hindu
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-100, y-317, 0);// budha "T", x-100, y-114
	    					break;
	    			}
    			}
    			
    			String nama_agama = pemegang.getLsag_name();
//    			over.showTextAligned(Element.ALIGN_LEFT, nama_agama, x+75, y-316, 0);
    			
    			Integer id_pendidikan = pemegang.getLsed_id();
    			String pendidikan = pemegang.getLsed_name();
    			if (pendidikan != null)
    			{
	    			switch (id_pendidikan)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-330, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-245, y-330, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-198, y-330, 0);
	    						break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-163, y-330, 0);
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-100, y-330, 0);
	    						break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-71, y-330, 0);
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan,  x+75, y-330, 0);
	    					break;
	    			}
    			}
    			
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-288, y-349, 0);
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)),x-286+(i*10),y-375, 0); // x-286+(i*10),y-160
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-200+(i*10),y-375, 0);
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-151+(i*10),y-375, 0);
	    			}
    			}
    			
    			String alamat_kantor = pemegang.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-286, y-392, 0);
    			
    			String kode_pos_kantor = pemegang.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)),  x-286+(i*10),y-415
	    						, 0);
	    			}
    			}
    			
    			String area_kantor = pemegang.getArea_code_kantor();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-200+(i*10),y-415, 0);
	    			}
    			}
    			
    			String telp_kantor = pemegang.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-151+(i*10),y-415, 0);
	    			}
    			}
    		
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getAlamat_rumah(), x-288, y-434, 0);
    			
    			String area_kores = pemegang.getArea_code_rumah();
    			if (area_kores != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kores.charAt(i)), x-200+(i*10),y-459, 0);
	    			}
    			}
    		
    			String telp_kores = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kores.charAt(i)), x-151+(i*10),y-459, 0);
	    			}
    			}
    			
    			String hp= pemegang.getNo_hp();
    			if (hp != null)
    			{
    				for (int i =0 ; i< 4 ; i++)
    	    		{
    	    			over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(hp.charAt(i)), x-286+(i*10),y-475, 0);
    	    		}

    				for (int i =4 ; i< hp.length() ; i++)
    	    		{
    	    			over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(hp.charAt(i)), x-276+(i*10),y-475, 0);
    	    		}
    			}
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getEmail(), x-151, y-475, 0);
    			
    			
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-288, y-488, 0);
    			
//    			String uraian_kerja =pemegang.getMpn_job_desc();
//    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-920, y-488, 0);
    			
    			Integer masa_bakti =pemegang.getMspe_lama_kerja();
    			if (masa_bakti== null){
    				over.showTextAligned(Element.ALIGN_LEFT, "-", x+107, y-488, 0);
    			}else
    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(masa_bakti), x+107, y-488, 0);
    			
    			String jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,jabatan , x-60, y-488, 0);
    			
    			String tujuan = pemegang.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "x", x-286, y-501, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "x", x-169, y-501, 0);
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-59, y-501, 0);// x-59, y-501
	    							}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-286, y-510, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "x", x-169, y-510, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "x", x-59, y-510, 0);//lain2
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan,x+7, y-510, 0);
	    								}
    			}	
    		
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-541, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-541, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "x", x-169, y-541, 0);   // x-169, y-542
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-169, y-551, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-59, y-551, 0);
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-59, y-551, 0);
		    							}
    			}
    			
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-525, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-251, y-525, 0);
	    					}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-192, y-525, 0);
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-225, y-525, 0);
	    							}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-59, y-525, 0);//lain2
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-40, y-525, 0);
				    				}
    			}
    			String Smbr_penghasilan = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-565, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-251, y-565, 0);
	    					}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-192, y-565, 0);
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-225, y-565, 0);
	    							}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-59, y-565, 0);//lain2
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-40, y-565, 0);
				    				}
    			}
		
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x-263, y-580, 0);
    			
    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-288, y-620, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-288, y-637, 0);
    			
    			jns_identitas = tertanggung.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-648, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-246, y-648, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-137, y-648, 0);
	    			}
    			}
    			
    			no_identitas = tertanggung.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-286+(i*10), y-661, 0);
	    			}
    			}
    			
    			id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-677, 0);//x-286, y-677
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-177, y-677, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-145, y-677, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-266+(i*10), y-689, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-217+(i*10), y-689, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-177+(i*10), y-689, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_place_birth(), x-115, y-689, 0);
    			
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-246, y-703 ,0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-703, 0);
	    				}
    			}
    			
    			sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
    				switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-716, 0);//blum kawin
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-246, y-716, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-177, y-716, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-177, y-716, 0);
	    					break;
	    			}
    			}	
    			
    			String couple= tertanggung.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-219, y-744, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getNama_anak1(), x-219, y-754, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getNama_anak2(), x-219, y-765, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getNama_anak3(), x-219, y-775, 0);
    			
    			Date tgllahirCoupleT = tertanggung.getTgllhr_si();
    			if (tgllahirCouple != null)
    			{
	    			Integer tgl_lahir = tgllahirCoupleT.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirCoupleT.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirCoupleT.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, tgl_lhr, x+50, y-744, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, bln_lhr, x+80, y-744, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, thn_lhr, x+100, y-744, 0);
	    			}
    			}
    			Date tgllahirAnkT1 = tertanggung.getTgllhr_anak1();
    			if (tgllahirAnk1 != null)
    			{
	    			Integer tgl_lahir = tgllahirAnkT1.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirAnkT1.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirAnkT1.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, tgl_lhr, x+50, y-754, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, bln_lhr, x+80, y-754, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, thn_lhr, x+100, y-754, 0);
	    			}
    			}
    			Date tgllahirAnkT2 = tertanggung.getTgllhr_anak2();
    			if (tgllahirAnk2 != null)
    			{
	    			Integer tgl_lahir = tgllahirAnkT2.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirAnkT2.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirAnkT2.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, tgl_lhr, x+50, y-765, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, bln_lhr, x+80, y-765, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, thn_lhr, x+100, y-765, 0);
	    			}
    			}
    			Date tgllahirAnkT3 = tertanggung.getTgllhr_anak3();
    			if (tgllahirAnk3 != null)
    			{
	    			Integer tgl_lahir = tgllahirAnkT3.getDate();
	    			String tgl_lhr = FormatString.rpad("0",(Integer.toString(tgl_lahir)),2);
	    			Integer bln_lahir = tgllahirAnkT3.getMonth()+1;
	    			String bln_lhr = FormatString.rpad("0",(Integer.toString(bln_lahir)),2);
	    			Integer thn_lahir = tgllahirAnkT3.getYear()+1900;
	    			String thn_lhr = Integer.toString(thn_lahir);
	    			
	    			for (int i =0 ; i< tgl_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, tgl_lhr, x+50, y-775, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, bln_lhr, x+80, y-775, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, thn_lhr, x+100, y-775, 0);
	    			}
    			}
    			
    		
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-792, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-247, y-792, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-198,y-792, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-148, y-792, 0);
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-102, y-792, 0);//x-102, y-792
	    					break;
	    			}
    			}
    			id_pendidikan = tertanggung.getLsed_id();
    			pendidikan = tertanggung.getLsed_name();
    			if (pendidikan != null)
    			{
	    			switch (id_pendidikan)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-805, 0);//x-286, y-805
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-247, y-805, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-198, y-805, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-148, y-805, 0);
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-102, y-807, 0);
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-64, y-805, 0);
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x+74, y-805, 0);
	    					break;
	    			}
    			}
    			
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-288, y-818, 0);
    			
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-286+(i*10),y-845, 0);
		    			}
	    			}
    			}
    			
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-200+(i*10),y-845, 0);//x-200+(i*10),y-845
	    			}
    			}
    			
    			telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-150+(i*10),y-845, 0);
	    			}
    			}
    			Integer nOne = reader.getNumberOfPages();
				
				logger.info("jumlah page:"+nOne);
				
				over.endText();
				
				over = stamp.getOverContent(2);
		    	over.beginText();
		    	over.setFontAndSize(bf, f); //set ukuran font

    			alamat_kantor = tertanggung.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-288, y-90, 0);
    			
    			kode_pos_kantor = tertanggung.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)), x-286+(i*10),y-115, 0);
	    			}
    			}		
    			area_kantor = tertanggung.getArea_code_kantor();
    			if (area_kantor != null)
    			{
	    			for (int i =0 ; i< area_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kantor.charAt(i)), x-200+(i*10),y-115, 0);
	    			}
    			}		
    			telp_kantor = tertanggung.getTelpon_kantor();
//    			telp_kantor = pemegang.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-151+(i*10),y-115, 0);
	    			}
    			}
    			
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-288, y-137, 0);
    			
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-286+(i*10),y-163, 0);
		    			}
	    			}
    			}
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-200+(i*10),y-163, 0);//x-200+(i*10),y-163
	    			}
    			}
    			telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-150+(i*10),y-163, 0);
	    			}
    			}
    			String no_hp = tertanggung.getNo_hp();		
    			if (no_hp != null)
    			{
    				for (int i =0 ; i< 4 ; i++)
    	    		{
    	    			over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-286+(i*10),y-178, 0);
    	    		}

    				for (int i =4 ; i< no_hp.length() ; i++)
    	    		{
    	    			over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-276+(i*10),y-178, 0);
    	    		}
    			}
    			pekerjaan =  tertanggung.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-290, y-192, 0);
//    			String uraian_kerja1 =tertanggung.getMpn_job_desc();
//    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja1, x-57, y-515, 0);
    			
    			masa_bakti =tertanggung.getMspe_lama_kerja();
    			if (masa_bakti== null){
    				over.showTextAligned(Element.ALIGN_LEFT, "-", x+115, y-515, 0);
    			}else
    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(masa_bakti), x+115, y-192, 0);
    			
    			jabatan = tertanggung.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-57, y-192, 0);
    			
    			tujuan = tertanggung.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-204, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-169, y-204, 0);
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-60, y-204, 0);// x-60, y-204
	    					}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-286, y-214, 0);
	    					}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-140, y-214, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-59, y-214, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x+8, y-214, 0);
	    								}
    			}			
    			dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "x", x-286, y-226, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "x", x-251, y-226, 0);
	    						}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-192, y-226, 0);//192, y-226
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "x", x-119, y-226, 0);
	    							}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "x", x-59, y-226, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x+7, y-226, 0);
				    				}
    			}
  			
    			
    			penghasilan = tertanggung.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.equalsIgnoreCase(" <= RP. 10 JUTA"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "x", x-286, y-240, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "x", x-286, y-250, 0);
	    							}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "x", x-169, y-240, 0);//x-169, y-240
    							}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "x", x-168, y-250, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "x", x-59, y-240, 0);
		    								}else if (penghasilan.indexOf("> RP. 500 JUTA") >= 0)
		    							{
		    								over.showTextAligned(Element.ALIGN_LEFT, "x", x-59, y-250, 0);
		    								}
    			}
    			String smbr_dana = pemegang.getMkl_pendanaan();
    			if (smbr_dana !=null)
    			{
	    			if (smbr_dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "x", x-286, y-264, 0);
	    			}else if (smbr_dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "x", x-251, y-264, 0);
	    						}else if (smbr_dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-192, y-264, 0);//192, y-226
	    						}else if (smbr_dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "x", x-119, y-264, 0);
	    							}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "x", x-59, y-264, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, smbr_dana, x+7, y-264, 0);
				    				}
    			}
    			
    			//data datausulan
    			String nama_produk = datausulan.getLsdbs_name();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_produk , x-373, y-334, 0);
    			
    			String kurs = datausulan.getLku_id();
    			String krs ="";
    			if (kurs != null)
    			{
	    			if (kurs.equalsIgnoreCase("01"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "x" , x-264, y-351, 0);
	    				krs="Rp ";
	    			}else{
	    				krs= "U$ ";
	    				over.showTextAligned(Element.ALIGN_LEFT,  "x" , x-200, y-351, 0);
	    			}
    			}
    			Integer lama_tanggung = datausulan.getMspr_ins_period();
//    			over.showTextAligned(Element.ALIGN_LEFT,  Integer.toString(lama_tanggung) , x-180, y-630, 0);

    			
    			Integer lama_bayar = datausulan.getMspo_pay_period();
    			
    			Double premi = datausulan.getMspr_premium();
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x+55, y-334, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-145, y-334, 0);//690
    			
    			Integer paymode = datausulan.getLscb_id();
    			if (paymode != null)
    			{
    				switch (paymode )
	    			{
	    				case 0: // sekaligus
	    					over.showTextAligned(Element.ALIGN_LEFT, "x", x-263, y-360, 0); // x-263, y-360
		    				break;
	    				case 1: //triwulanan
    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-198, y-360, 0); //
	    				break;
	    				case 2: // semesteran
    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-133, y-360, 0);
    						break;
	    				case 3:
    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-68, y-360, 0);
    					break;
	    				case 6:
							over.showTextAligned(Element.ALIGN_LEFT, "x", x-3, y-360, 0);
	    					break;
	    			}
    			}
    			
    			Integer bentuk_pembayaran = datausulan.getMste_flag_cc();
    			if (bentuk_pembayaran !=null)
    			{
    				switch (bentuk_pembayaran)
	    			{
	    				case 0: 
	    					over.showTextAligned(Element.ALIGN_LEFT, "x", x-263, y-371, 0);//263//transfer bank
		    				break;
	    				case 1: 
    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-199, y-371, 0); //atm
    						break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "x", x-133, y-371, 0); //
	    				
	    					if (bentuk_pembayaran.intValue() == 2) 
	    					{
		    					over.showTextAligned(Element.ALIGN_LEFT, "TABUNGAN", x-263, y-371, 0);
		    				}else{
    							over.showTextAligned(Element.ALIGN_LEFT, "PAYROLL", x-133, y-371, 0);
		    				}
	    					break;
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x-242+(i*10), y-392, 0);
	    			}
	    			
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x-188+(i*10), y-392, 0);
	    			}
	    			
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x-118+(i*10), y-392, 0);
	    			}
    			}
    			
    			
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-87, y-482, 0);
    			
    			String no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-332, y-482, 0);
    			
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-332, y-493, 0);
    			
    			String cabang_bank = data_rek.getMrc_cabang();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang_bank, x-87, y-493, 0);
    			
    			String kota_bank = data_rek.getMrc_kota();
    			over.showTextAligned(Element.ALIGN_LEFT,  kota_bank, x+57, y-493, 0);
    			
    			
    			   			
    			Integer id_top_up_tunggal = inv.getDaftartopup().getId_tunggal();
    			Double topup_tunggal = inv.getDaftartopup().getPremi_tunggal();
    			if (topup_tunggal == null)
    			{
    				topup_tunggal = new Double(0);
    			}
    			
    			if (id_top_up_tunggal == null)
    			{
    				id_top_up_tunggal = new Integer(0);
    			}
    			if (id_top_up_tunggal.intValue() == 2)
    			{
    				over.showTextAligned(Element.ALIGN_LEFT,  "x", x-91,  y-409, 0);;
    			}
    			if (topup_tunggal==null){
    				topup_tunggal=0.0;
    			}

    			Integer id_top_up_berkala = inv.getDaftartopup().getId_berkala();
    			Double topup_berkala = inv.getDaftartopup().getPremi_berkala();
    			if (topup_berkala == null)
    			{
    				topup_berkala = new Double(0);
    			}
    			if (id_top_up_berkala == null)
    			{
    				id_top_up_berkala = new Integer(0);
    				
    			}
    			if (id_top_up_berkala.intValue() == 1)
    			{
    				over.showTextAligned(Element.ALIGN_LEFT,  "x", x-26,  y-409, 0);
    			}
    			
    			Double total_topup = new Double(topup_tunggal.doubleValue() + topup_berkala.doubleValue());
    			if (total_topup==null){
    				total_topup=0.0;
    			}
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-210, y-402, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(total_topup.doubleValue())) , x-210, y-412, 0);
    			
    		 			
    			List datarider = datausulan.getDaftaRider();
    			Double total_premirider = new Double(0);
    			
    			for (int i = 0 ; i < datarider.size(); i++)
    			{
    				Datarider datard = (Datarider)datarider.get(i);
    				String nama_rider = datard.getLsdbs_name();
    				Integer pertanggungan_rider = datard.getMspr_ins_period();
    				Double premi_rider = datard.getMspr_premium();
//    				over.showTextAligned(Element.ALIGN_LEFT,  nama_rider , x-920, y-1710 - (i*30), 0);
    				if (kurs != null)
	    			{
		    			if (kurs.equalsIgnoreCase("01"))
		    			{
//		    				over.showTextAligned(Element.ALIGN_LEFT,  "Rp " , x-330, y-1710 -(i*30), 0);
		    			}else{
//		    				over.showTextAligned(Element.ALIGN_LEFT,  "USD" , x-203, y-1710 - (i*30), 0);
		    			}
	    			}
//    				over.showTextAligned(Element.ALIGN_LEFT,  Integer.toString(pertanggungan_rider) , x-605, y-1710 - (i*10), 0);
    				if (premi_rider==null){
    					premi_rider=0.0;
    				}
    				over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi_rider.doubleValue())) ,  x-210, y-421, 0);
    				total_premirider = new Double(premi_rider.doubleValue() + total_premirider.doubleValue());
    			}
    			Double jumlah = new Double(total_premirider.doubleValue() + premi.doubleValue()+ total_topup.doubleValue());
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(jumlah.doubleValue())) ,  x-210, y-431, 0);
    			/*
    			Double total_pokok_rider = new Double(total_premirider.doubleValue() + premi.doubleValue());
//    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(total_pokok_rider.doubleValue()))  , x-200,y-814, 0);
//    			
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(total_premirider.doubleValue())) , x-200,y-708, 0);
    			
    			Double total_pokok_rider_top = new Double( total_pokok_rider.doubleValue() + total_topup.doubleValue());
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(total_pokok_rider_top.doubleValue())) , x-200,y-718, 0);
    				
    			List datainvestasi = inv.getDaftarinvestasi();
    			
    			Integer jumlah_DataInvestasi = datainvestasi.size();
//    			if(jumlah_DataInvestasi > 1) jumlah_DataInvestasi=1;
    			for (int i = 0 ; i < jumlah_DataInvestasi; i++)
    			{
    				DetilInvestasi datainv = (DetilInvestasi) datainvestasi.get(i);
    				
    			}
    			
    				List daftarinvestasi = inv.getDaftarinvestasi();
    				Integer jumlah_DetilInvestasi = daftarinvestasi.size();
    				Integer fixed = new Integer(0);
    				Integer dynamic = new Integer(0);
    				Integer aggresive = new Integer(0);
    				Integer secured = new Integer(0);
    				Integer dyna = new Integer(0);
//    				if(jumlah_DetilInvestasi > 3) jumlah_DataInvestasi=3;
    				for (int i = 0 ; i< jumlah_DetilInvestasi ; i++)
    				{
    					DetilInvestasi daftarinv = (DetilInvestasi)daftarinvestasi.get(i);
    					String kode = daftarinv.getLji_id1();
    					if (kurs.equalsIgnoreCase("01"))
    					{
	    					if (kode.equalsIgnoreCase("01"))
	    					{
	    						fixed = daftarinv.getMdu_persen1();
//	    						over.showTextAligned(Element.ALIGN_LEFT,  Integer.toString(fixed)  , x-288,y-700 , 0);
	    					}else if (kode.equalsIgnoreCase("02"))
		    					{
		    						dynamic = daftarinv.getMdu_persen1();
//		    						over.showTextAligned(Element.ALIGN_LEFT,  Integer.toString(dynamic)  , x-930,y-2315 , 0);
		    					}else if (kode.equalsIgnoreCase("03"))
			    					{
			    						aggresive = daftarinv.getMdu_persen1();
//			    						over.showTextAligned(Element.ALIGN_LEFT,  Integer.toString(aggresive)  , x-930,y-2355 , 0);
			    					}
    					}else{
    						if (kode.equalsIgnoreCase("05"))
    						{
			    				secured = daftarinv.getMdu_persen1();
//			    				over.showTextAligned(Element.ALIGN_LEFT,  Integer.toString(secured)  , x-395,y-2275 , 0);
			    			}else if (kode.equalsIgnoreCase("05"))
				    			{
			    					dyna = daftarinv.getMdu_persen1();
//				    				over.showTextAligned(Element.ALIGN_LEFT,  Integer.toString(dyna )  , x-395,y-2315, 0);
					    		}
    					}
    				}
    				String no_spaj = pemegang.getReg_spaj();
//    				over.showTextAligned(Element.ALIGN_LEFT, FormatString.nomorSPAJ(no_spaj) , x-286, y+70, 0);
*/
    			
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
	    					
	    					over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-350,y-635+ (i*12), 0);
	    					
	    					over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-150, y-635+ (i*12), 0);
	    					over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x-25,y-635+ (i*12), 0);
	    					over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x+70,y-635+ (i*12), 0);
	    				}
    			}
	    				    			
	    		//Untuk menampilkan data medical 
//	    		over.showTextAligned(Element.ALIGN_LEFT, medical.getKeadaan(), x-290, y-56, 0); 	
//	    		over.showTextAligned(Element.ALIGN_LEFT, "TESTING", x-290, y-56, 0); 
	    		nOne = reader.getNumberOfPages();
					
				logger.info("jumlah page:"+nOne);
					
				over.endText();
					
				over = stamp.getOverContent(3);
			    over.beginText();
			    over.setFontAndSize(bf, f); //set ukuran font

	    		//Penutup
	    		String nama_agen = dataagen.getMcl_first();	
	    		String kode_agen = dataagen.getMsag_id();
//	    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x-200,y-297, 0);
//	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x-1,y-689, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-205,y-610, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-10,y-610, 0);
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
    						    		
    			
		return over;
	}
	
}
