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
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AmanahLinkSyariah {
	protected final Log logger = LogFactory.getLog( getClass() );

	private static AmanahLinkSyariah instance;
	
	public static AmanahLinkSyariah getInstance()
	{
		if( instance == null )
		{
			instance = new AmanahLinkSyariah();
		}
		return instance;
	}
	
	public AmanahLinkSyariah()
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
		PdfReader reader =paramVO.getReader();
		PdfStamper stamp =paramVO.getStamp();
		BaseFont bf = paramVO.getBf();
	
				int tambah = 60;
		
				over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-334, y-181, 0);
				over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_gelar(), x-20, y-181, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-334, y-197, 0);
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-332, y-210, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-332, y-210, 0);// x-182, y-210
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-137, y-210, 0);
	    			}
    			}
	    		String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-331+(i*13), y-230, 0);
	    			}
    			}
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-332, y-248, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-332, y-274, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-332, y-248, 0);//x-332, y-285
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-332, y-285, 0);
	    					break;
	    			}
    			}
    			String nama_istri= pemegang.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-199, y-260, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak1(), x-199, y-270, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x-199, y-280, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x-199, y-290, 0);
    			
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-53+(i*12), y-258, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-24+(i*12), y-258, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+4+(i*12), y-258, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-53+(i*12), y-268, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-24+(i*12), y-268, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+4+(i*12), y-268, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(tgl_lhr.charAt(i)), x-53+(i*10), y-278, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,String.valueOf(bln_lhr.charAt(i)), x-24+(i*10), y-278, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+4+(i*10), y-278, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-53+(i*10), y-288, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-24+(i*10), y-288, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(thn_lhr.charAt(i)),  x+4+(i*10), y-288, 0);
	    			}
    			}
    			
		    	Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-332, y-303, 0); //x-332, y-303
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-265, y-303, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-225, y-304, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-310+(i*13), y-318, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-253+(i*13), y-318, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-198+(i*13), y-318, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-122, y-318, 0);
    			
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-331, y-331, 0);//x-331, y-331
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-285, y-331, 0);
	    			}
    			}
    			
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-331, y-175, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-285, y-175, 0); //protestan
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-223, y-344, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-163, y-344, 0);//hindu
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-112, y-344, 0);//budha x-112, y-344
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-240, y-358, 0);//SMU x-352, y-187
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-193, y-358, 0);//D1/D3
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-139, y-358, 0);//S1
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-99, y-358, 0);//S2
	    					break;
	    				case 8:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-331, y-358, 0); //SD
	    					break;
	    				case 9:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-285, y-358, 0); //SMP  x-285, y-358
	    					break;	
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-60, y-358, 0); //S2
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x-15, y-358, 0);
	    					break;
	    			}
    			}
    			
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-334, y-375, 0);
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-331+(i*13),y-403, 0);
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-326+(i*13),y-418, 0);
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-265+(i*13),y-418, 0);
	    			}
    			}
    			
    			String alamat_kantor = pemegang.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kantor, x-334, y-434, 0);
    			
    			String kode_pos_kantor = pemegang.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)), x-331+(i*13),y-460, 0);
	    			}
    			}
    			
    			String area_kantor = pemegang.getArea_code_kantor();
    			if (area_kantor != null)
    			{
	    			for (int i =0 ; i< area_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kantor.charAt(i)),x-326+(i*13),y-475, 0);
	    			}
    			}
    			
    			String telp_kantor = pemegang.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-265+(i*13),y-475, 0);
	    			}
    			}
    			
    			String alamat_tagihan= pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-334, y-492, 0);
    			
    			String kode_pos_tagihan = pemegang.getKd_pos_rumah();
    			if (kode_pos_tagihan != null)
    			{
	    			if ( kode_pos_tagihan!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_tagihan.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_tagihan.charAt(i)), x-331+(i*13),y-519, 0);
		    			}
	    			}
    			}
    			
    			String area_tagihan = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-326+(i*13),y-534, 0);
	    			}
    			}
    			
    			String telp_tagihan = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-265+(i*13),y-534, 0);
	    			}
    			}
    			String no_hp=pemegang.getNo_hp();
    			if (no_hp != null)
    			{
	    			for (int i =0 ; i< no_hp.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-331+(i*13),y-549, 0);
	    			}
    			}
    			String imel= pemegang.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT, imel, x-334, y-565, 0);
    			String tujuan = pemegang.getMkl_tujuan();
    			
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-593, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-291, y-593, 0);
	    				}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-158, y-593, 0);//x-158, y-593
	    					}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-606, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-291, y-606, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X",x-158, y-606, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan,x-81, y-606, 0);
	    							}
    			}
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-632, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-644, 0);
	    				}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-291, y-632, 0);//x-291, y-632
	    					}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-291, y-644, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-158, y-632, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-81, y-632, 0);
	    						}
    			}
    			
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-334, y-661, 0);
    			
//    			String uraian_kerja =pemegang.getMpn_job_desc();
//    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-50, y-661, 0);
    			
    			String jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  "jabatan" , x-142, y-661, 0);
   			
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-689, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-703, 0);
	    				}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-291, y-689, 0);// x-291, y-689
	    					}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-291, y-703, 0);
		    					}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-158, y-689, 0);
		    						}else if (penghasilan.indexOf("> RP. 500 JUTA") >= 0)
		    							{
		    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-158, y-703, 0);
		    							}
    			}
    			String hasil = pemegang.getMkl_pendanaan();
    			if (hasil !=null)
    			{
	    			if (hasil.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-732, 0);
	    			}else if (hasil.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-744, 0);
	    				}else if (hasil.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-293, y-732, 0);//x-293, y-732
	    					}else if (hasil.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-291, y-744, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-158, y-732, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, hasil, x-81, y-732, 0);
	    						}
    			}
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x-236, y-760, 0);
    		
    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-335, y-810, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-335, y-826, 0);
    			jns_identitas = pemegang.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-332, y-840, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-332, y-840, 0);// x-182, y-210
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-180, y-840, 0);
	    			}
    			}
	    		no_identitas = tertanggung.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-331+(i*13), y-857, 0);
	    			}
    			}
    			Integer nOne = reader.getNumberOfPages();
				
				logger.info("jumlah page:"+nOne);
				
				over.endText();
				
				over = stamp.getOverContent(2);
		    	over.beginText();
		    	over.setFontAndSize(bf, f); //set ukuran font
    			sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-329, y-25, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-329, y-52, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-329, y-63, 0);//x-329, y-63
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-329, y-63, 0);
	    					break;
	    			}
    			}
    			nama_istri= tertanggung.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-196, y-36, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak1(), x-196, y-46, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x-196, y-56, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x-196, y-66, 0);
    			
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-50+(i*12), y-35, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-21+(i*12), y-35, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+6+(i*12), y-35, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-50+(i*12), y-45, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-21+(i*12), y-45, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+6+(i*12), y-45, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(tgl_lhr.charAt(i)), x-50+(i*10), y-55, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,String.valueOf(bln_lhr.charAt(i)), x-21+(i*10), y-55, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+6+(i*10), y-55, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-50+(i*10), y-65, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-21+(i*10), y-65, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(thn_lhr.charAt(i)),  x+6+(i*10), y-65, 0);
	    			}
    			}
    			
		    	id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-83, 0); //x-332, y-303
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-265, y-85, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-225, y-85, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-310+(i*13), y-101, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-253+(i*13), y-101, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-198+(i*13), y-101, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_place_birth(), x-122, y-101, 0);
    			
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-330, y-115, 0);//x-331, y-331
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-285, y-115, 0);
	    			}
    			}
    			
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-331, y-128, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-285, y-128, 0); //protestan
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-223, y-128, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-163, y-128, 0);//hindu
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-112, y-128, 0);//budha x-112, y-128
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-240, y-142, 0);//SMU x-352, y-187
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-193, y-142, 0);//D1/D3
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-139, y-142, 0);//S1
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-99, y-142, 0);//S2
	    					break;
	    				case 8:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-331, y-142, 0); //SD
	    					break;
	    				case 9:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-285, y-142, 0); //SMP  x-285, y-358
	    					break;	
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-60, y-142, 0); //S2
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x-15, y-142, 0);
	    					break;
	    			}
    			}
    			
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-334, y-157, 0);
    			
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-331+(i*13),y-184, 0);
		    			}
	    			}
    			}
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-326+(i*13),y-198, 0);
	    			}
    			}
    			telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-265+(i*13),y-198, 0);
	    			}
    			}
    			
    			String alamat_korespondensi= tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-334, y-213, 0);
    			
    			String kode_pos_korespondensi = tertanggung.getKd_pos_rumah();
    			if (kode_pos_tagihan != null)
    			{
	    			if ( kode_pos_tagihan!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_tagihan.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_tagihan.charAt(i)), x-331+(i*13),y-239, 0);
		    			}
	    			}
    			}
    			
    			String area_korespondensi = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-326+(i*13),y-253, 0);
	    			}
    			}
    			
    			String telp_korespondensi = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-265+(i*13),y-253, 0);
	    			}
    			}
    			no_hp=tertanggung.getNo_hp();
    			if (no_hp != null)
    			{
	    			for (int i =0 ; i< no_hp.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-331+(i*13),y-268, 0);
	    			}
    			}
    			imel= tertanggung.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT, "imel", x-334, y-284, 0);
    			tujuan = tertanggung.getMkl_tujuan();
    			
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-313, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-290, y-313, 0);
	    				}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-158, y-325, 0);//x-158, y-593
	    					}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-325, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-290, y-325, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X",x-158, y-325, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan,x-81, y-325, 0);
	    							}
    			}
    			dana = tertanggung.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-351, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-364, 0);
	    				}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-290, y-351, 0);//x-291, y-351
	    					}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-290, y-364, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-158, y-351, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-81, y-351, 0);
	    						}
    			}
    			pekerjaan =  tertanggung.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-334, y-378, 0);
    			
//    			String uraian_kerja =pemegang.getMpn_job_desc();
//    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-50, y-661, 0);
    			
    			jabatan = tertanggung.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-142, y-378, 0);
   			
    			penghasilan = tertanggung.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-403, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-415, 0);
	    				}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-290, y-403, 0);// x-291, y-403
	    					}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-290, y-415, 0);
		    					}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-158, y-403, 0);
		    						}else if (penghasilan.indexOf("> RP. 500 JUTA") >= 0)
		    							{
		    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-158, y-415, 0);
		    							}
    			}
    			hasil = tertanggung.getMkl_pendanaan();
    			if (hasil !=null)
    			{
	    			if (hasil.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-443, 0);
	    			}else if (hasil.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-430, y-455, 0);
	    				}else if (hasil.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-292, y-443, 0);//x-292, y-443
	    					}else if (hasil.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-291, y-455, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-158, y-443, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, hasil, x-81, y-443, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x-316+(i*12), y-501, 0);
	    			}
	    			over.showTextAligned(Element.ALIGN_LEFT, "-", x-163, y-735, 0);
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x-241+(i*12), y-501, 0);
	    			}
	    			over.showTextAligned(Element.ALIGN_LEFT, "-", x-150, y-735, 0);
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x-170+(i*12), y-501, 0);
	    			}
    			}
    			String no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-393, y-600, 0);
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-393, y-612, 0);
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-199, y-600, 0);
    			String cabang = dataagen.getLsrg_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang , x-199,y-612, 0);
    			String kota_bank = data_rek.getMrc_kota();
    			over.showTextAligned(Element.ALIGN_LEFT,  kota_bank, x-199, y-624, 0);
    			String kurs = datausulan.getLku_id();
    			String krs = "";
    			if (kurs != null)
    			{
	    			if (kurs.equalsIgnoreCase("01"))
	    			{
//	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x-380, y-305, 0);//x-380, y-305
	    				krs= "Rp ";
	    			}else{
//	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-262, y-305, 0);
	    				krs = "U$ ";
	    			}
    			}
    			Double premi = datausulan.getMspr_premium();
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-248, y-651, 0);
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
    					
    					over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-447, y-813 + (i*12), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-200, y-813 + (i*12), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x-107,y-813 + (i*12), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x+5,y-813 + (i*12), 0);
    				}	
    			}
    			nOne = reader.getNumberOfPages();
				
				logger.info("jumlah page:"+nOne);
				
				over.endText();
				
				over = stamp.getOverContent(3);
		    	over.beginText();
		    	over.setFontAndSize(bf, f); //set ukuran font
		    	
    			
    			String no_spaj = pemegang.getReg_spaj();
    			Date tgl_spaj = pemegang.getMspo_spaj_date();
    			
	    		String nama_agen = dataagen.getMcl_first();	
	    		String kode_agen = dataagen.getMsag_id();
	    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x-130,y-864, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x-170,y-843, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-280,y-850, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-423,y-850, 0);/*
	    		
	    		
    			//data datausulan   			
    			String nama_produk = datausulan.getLsdbs_name();
    			Integer idx = nama_produk.indexOf("BULAN");
    			String mgi = data_pwr.getMps_jangka_inv();
    			if (mgi !=null)
    			{
    			
	    			switch (Integer.parseInt(mgi))
	    			{
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-297, y-324, 0);//x-297, y-324
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT,  "X" ,  x-245, y-324, 0);
	    					break;
	    				case 12:
	    					if (idx <0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-200, y-324, 0);
	    					}else{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-102, y-324, 0);
	    					}
	    					break;
	    				case 36:
	    					if (idx <0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-150, y-324, 0);
	    					}else{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x-5, y-324, 0);
	    					}
	    					break;
	    			}
    			}
    			Integer rollover = data_pwr.getMps_roll_over();
    			if (rollover != null)
    			{
    				switch (rollover)
    				{
    					case 1:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-345, y-334, 0);
    						break;
    					case 2:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-345, y-345, 0);
    						break;
    					case 3:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-345, y-356, 0);//x-345, y-356
    						break;
    				}
    				
    			}
    			
    			String kurs_rek = data_rek.getMrc_kurs();
    			if (kurs_rek != null)
    			{
    				if (kurs_rek.equalsIgnoreCase("01")){
    					over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x-88, y-356, 0);//x-88, y-356
    				}else{
    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-50, y-356, 0);
    				}
    			}
    			
    			
    						

	    		*/
	    	
    			
		return over;
	}
	
}
