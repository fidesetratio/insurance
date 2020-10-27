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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SmartInvest {
	protected final Log logger = LogFactory.getLog( getClass() );
	private static SmartInvest instance;
	
	public static SmartInvest getInstance()
	{
		if( instance == null )
		{
			instance = new SmartInvest();
		}
		return instance;
	}
	
	public SmartInvest()
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
		String noBlanko= paramVO.getNoBlanko();
	
				int tambah = 60;
				
				over.showTextAligned(Element.ALIGN_LEFT, noBlanko , x, y-39, 0);
	
				over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-363, y-76, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-363, y-90, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_gelar(), x-3, y-76, 0);
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-358, y-103, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-210, y-103, 0);//x-286, y-103
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-163, y-103, 0);
	    			}
    			}
	    		String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-357+(i*13), y-115, 0);
	    			}
    			}
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-131, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-157, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-169, 0);//x-359, y-169
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "w", x-359, y-169, 0);//y-169
	    					break;
	    			}
    			}
    			String nama_istri= pemegang.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-225, y-142, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak1(), x-225, y-152, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x-225, y-162, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x-225, y-172, 0);
    			
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-141, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-141, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-141, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-151, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-151, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-151, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-161, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-161, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-161, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-171, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-171, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-171, 0);
	    			}
    			}
    			
		    	Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-183, 0);//x-359, y-183
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-293, y-183, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara,x-252, y-183, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-340+(i*10), y-194, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-283+(i*10), y-194, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-228+(i*13), y-194, 0);
	    			}
    			}  			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-150, y-194, 0);
    			
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-205, 0);//x-359, y-205
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-205, 0);
	    			}
    			}			
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-216, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-216, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-253, y-216, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-192, y-216, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-140, y-216, 0);//x-140, y-216
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-228, 0);// x-359, y-228
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-228, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-268, y-228, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-223, y-228, 0);
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-168, y-228, 0);
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-128, y-228, 0);
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x-44, y-228, 0);
	    					break;
	    			}
    			}
    			
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-362, y-243, 0);
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-359+(i*13),y-265, 0);// x-359+(i*13),y-265
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-357+(i*13),y-326, 0);//x-357+(i*13),y-278
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-293+(i*13),y-278, 0);
	    			}
    			}
    			String area_rumah2 = pemegang.getArea_code_rumah2();
    			if (area_rumah2 != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah2.charAt(i)), x-141+(i*13),y-278, 0);
	    			}
    			}
    			String telp_rumah2 = pemegang.getTelpon_rumah2();
    			if (telp_rumah2 != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah2.charAt(i)), x-75+(i*13),y-278, 0);
	    			}
    			}
    			
    			String alamat_kantor = pemegang.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-362, y-292, 0);
    			
    			String kode_pos_kantor = pemegang.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)),  x-357+(i*13),y-315, 0);
	    			}
    			}
    			
    			String area_kantor = pemegang.getArea_code_kantor();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-357+(i*13),y-326, 0);
	    			}
    			}
    			
    			String telp_kantor = pemegang.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-293+(i*13),y-326, 0);
	    			}
    			}
    			String code_kantor2 = pemegang.getArea_code_kantor2();
    			if (area_rumah2 != null)
    			{
	    			for (int i =0 ; i< code_kantor2.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(code_kantor2.charAt(i)), x-141+(i*13),y-326, 0);
	    			}
    			}
    			String telp_kantor2 = pemegang.getTelpon_kantor2();
    			if (telp_rumah2 != null)
    			{
	    			for (int i =0 ; i< telp_kantor2.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor2.charAt(i)), x-75+(i*13),y-326, 0);
	    			}
    			}
    			String alamat_tagihan = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_tagihan, x-362, y-341, 0);
    			
    			String kode_pos_tagihan = pemegang.getKd_pos_rumah();
    			if (kode_pos_tagihan != null)
    			{
    				for (int i =0 ; i< kode_pos_tagihan.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_tagihan.charAt(i)), x-359+(i*13),y-363, 0);// x-359+(i*13),y-265
		    			}
    			}
    			
    			String area_tagihan = pemegang.getArea_code_rumah();
    			if (area_tagihan != null)
    			{
	    			for (int i =0 ; i< area_tagihan.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_tagihan.charAt(i)), x-357+(i*13),y-376, 0);//x-357+(i*13),y-278
	    			}
    			}
    			
    			String telp_tagihan = pemegang.getTelpon_rumah();
    			if (telp_tagihan != null)
    			{
	    			for (int i =0 ; i< telp_tagihan.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_tagihan.charAt(i)), x-293+(i*13),y-376, 0);
	    			}
    			}
    			String area_tagihan2 = pemegang.getArea_code_rumah2();
    			if (area_tagihan2 != null)
    			{
	    			for (int i =0 ; i< area_tagihan2.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_tagihan2.charAt(i)), x-141+(i*13),y-376, 0);
	    			}
    			}
    			String telp_tagihan2 = pemegang.getTelpon_rumah2();
    			if (telp_tagihan2 != null)
    			{
	    			for (int i =0 ; i< telp_tagihan2.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_tagihan2.charAt(i)), x-75+(i*13),y-376, 0);
	    			}
    			}
    			String no_hp=pemegang.getNo_hp();
    			if (no_hp != null)
    			{
	    			for (int i =0 ; i< no_hp.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-358+(i*13),y-390, 0);
	    			}
    			}
    			String no_hp2=pemegang.getNo_hp();
    			if (no_hp != null)
    			{
	    			for (int i =0 ; i< no_hp.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-142+(i*12),y-390, 0);
	    			}
    			}
    			String imel= pemegang.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT, imel, x-362, y-403, 0);
    			
    			String tujuan = pemegang.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-463, y-428, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-320, y-428, 0);
	    				}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-188, y-428, 0);//x-188, y-428
	    					}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-463, y-441, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-320, y-441, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-188, y-441, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x-108, y-441, 0);
	    							}
    			}	
 		
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-464, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-478, 0);
	    				}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-321, y-464, 0);// x-321, y-464
	    					}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-321, y-478, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-188, y-464, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-108, y-466, 0);
	    						}
    			}
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-362, y-490, 0);
    			
//    			String uraian_kerja =pemegang.getMpn_job_desc();
//    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-350, y-490, 0);
    			
    			String jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-140, y-490, 0);
    			Integer masa_kerja= pemegang.getMspe_lama_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x+30, y-490, 0);
    			
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-516, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-528, 0);
	    				}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-321, y-516, 0);//x-321, y-516
	    					}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-321, y-528, 0);
		    					}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-188, y-516, 0);
		    						}else if (penghasilan.indexOf("> RP. 500 JUTA") >= 0)
		    							{
		    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-188, y-528, 0);
		    							}
    			}
    			String smbr_dana = pemegang.getMkl_pendanaan();
    			if (smbr_dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-556, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-568, 0);
	    				}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-322, y-556, 0);// x-322, y-556
	    					}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-322, y-568, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-188, y-556, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-108, y-557, 0);
	    						}
    			} 			
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x-247, y-581, 0);  			
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-355, y-623, 0);
    			String no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-357, y-593, 0);    			
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-355, y-607, 0);
    			String cabang = data_rek.getMrc_cabang();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang , x-355, y-635, 0);
    			String kota = data_rek.getMrc_kota();
    			over.showTextAligned(Element.ALIGN_LEFT,  kota , x-105, y-635, 0);
    			
    			//data tertanggung	
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-363, y-680, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-363, y-693, 0);
    			jns_identitas = tertanggung.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			if (jns_identitas != null)
    			{
    				if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-358, y-706, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-210, y-706, 0);//x-286, y-103
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-163, y-707, 0);
	    			}
    			}
    			
	    		no_identitas = tertanggung.getMspe_no_identity();
	    		if (no_identitas  !=null)
	    		{
	    			for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-357+(i*13), y-720, 0);
	    			}
	    		}
	    		sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital !=null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-734, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-761, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-772, 0);//x-359, y-772
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "w", x-359, y-772, 0);//y-169
	    					break;
	    			}
    			}
    			
    			nama_istri= pemegang.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-225, y-745, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak1(), x-225, y-757, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x-225, y-767, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x-225, y-777, 0);
    			tgllahirCouple = pemegang.getTgllhr_si();
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-745, 0);
	    			}
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-745, 0);
	    			}
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-745, 0);
	    			}
    			}
    			tgllahirAnk1 = pemegang.getTgllhr_anak1();
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-756, 0);
	    			}
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-756, 0);
	    			}
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-756, 0);
	    			}
    			}
    			tgllahirAnk2 = pemegang.getTgllhr_anak2();
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
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-766, 0);
	    			}
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-766, 0);
	    			}
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-766, 0);
	    			}
    			}
    			tgllahirAnk3 = pemegang.getTgllhr_anak3();
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-776, 0);
	    			}
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-776, 0);
	    			}
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-776, 0);
	    			}
    			}
	    		id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (id_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-786, 0);//x-359, y-786
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-293, y-786, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara,x-252, y-785, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-340+(i*10), y-797, 0);
	    			}
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-283+(i*10), y-797, 0);
	    			}
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-228+(i*13), y-797, 0);
	    			}
    			}
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_place_birth(),  x-150, y-798, 0);
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin != null)
    			{
    				if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-808, 0);//x-359, y-205
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-808, 0);
	    			}
    			}
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama !=null)
    			{
    				switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-820, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-819, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-253, y-819, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-192, y-819, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-140, y-819, 0);//x-140, y-216
	    					break;
	    			}
    			}
    			nama_agama = tertanggung.getLsag_name();
    			id_pendidikan = tertanggung.getLsed_id();
    			pendidikan = tertanggung.getLsed_name();
    			if (id_pendidikan != null)
    			{
    				switch (id_pendidikan)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-359, y-831, 0);// x-359, y-228
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-831, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-268, y-831, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-223, y-831, 0);
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-168, y-831, 0);
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-128, y-831, 0);
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x-44, y-830, 0);
	    					break;
	    			}
    			}
	    		alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-362, y-845, 0);
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-359+(i*13),y-870, 0);
		    			}
	    			}
    			}
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-357+(i*13),y-882, 0);
	    			}
    			}
    			area_rumah2 = tertanggung.getArea_code_rumah2();
    			if (area_rumah2 != null)
    			{
	    			for (int i =0 ; i< area_rumah2.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah2.charAt(i)), x-141+(i*13),y-882, 0);
	    			}
    			}
    			telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
    				for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-293+(i*13),y-882, 0);
	    			}
    			}
    			telp_rumah2 = tertanggung.getTelpon_rumah2();
    			if (telp_rumah2 != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah2.charAt(i)), x-75+(i*13),y-882, 0);
	    			}
    			}
    			Integer nOne = reader.getNumberOfPages();
				
				logger.info("jumlah page:"+nOne);
				
				over.endText();
				
				over = stamp.getOverContent(2);
		    	over.beginText();
		    	over.setFontAndSize(bf, f); //set ukuran font
    			
    			alamat_kantor = tertanggung.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-362, y-16, 0);
    			kode_pos_kantor = tertanggung.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)),  x-359+(i*13),y-39, 0);
	    			}
    			}
    			
    			area_kantor = tertanggung.getArea_code_kantor();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-357+(i*13),y-51, 0);
	    			}
    			}
    			
    			telp_kantor = tertanggung.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
    				for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-293+(i*13),y-51, 0);
	    			}
    			}
    			code_kantor2 = tertanggung.getArea_code_kantor2();
    			if (area_rumah2 != null)
    			{
	    			for (int i =0 ; i< code_kantor2.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(code_kantor2.charAt(i)), x-141+(i*13),y-51, 0);
	    			}
    			}
    			telp_kantor2 = tertanggung.getTelpon_kantor2();
    			if (telp_rumah2 != null)
    			{
	    			for (int i =0 ; i< telp_kantor2.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor2.charAt(i)), x-75+(i*13),y-51, 0);
	    			}
    			}
    			alamat_tagihan = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_tagihan, x-362, y-65, 0);
    			
    			kode_pos_tagihan = tertanggung.getKd_pos_rumah();
    			if (kode_pos_tagihan != null)
    			{
    				for (int i =0 ; i< kode_pos_tagihan.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_tagihan.charAt(i)), x-359+(i*13),y-88, 0);// x-359+(i*13),y-265
		    			}
    			}
    			
    			area_tagihan = tertanggung.getArea_code_rumah();
    			if (area_tagihan != null)
    			{
	    			for (int i =0 ; i< area_tagihan.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_tagihan.charAt(i)), x-357+(i*13),y-100, 0);//x-357+(i*13),y-278
	    			}
    			}
    			
    			telp_tagihan = tertanggung.getTelpon_rumah();
    			if (telp_tagihan != null)
    			{
	    			for (int i =0 ; i< telp_tagihan.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_tagihan.charAt(i)), x-293+(i*13),y-100, 0);
	    			}
    			}
    			area_tagihan2 = tertanggung.getArea_code_rumah2();
    			if (area_tagihan2 != null)
    			{
	    			for (int i =0 ; i< area_tagihan2.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_tagihan2.charAt(i)), x-141+(i*13),y-100, 0);
	    			}
    			}
    			telp_tagihan2 = tertanggung.getTelpon_rumah2();
    			if (telp_tagihan2 != null)
    			{
	    			for (int i =0 ; i< telp_tagihan2.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_tagihan2.charAt(i)), x-75+(i*13),y-100, 0);
	    			}
    			}
    			no_hp=tertanggung.getNo_hp();
    			if (no_hp != null)
    			{
	    			for (int i =0 ; i< no_hp.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-358+(i*13),y-112, 0);
	    			}
    			}
    			no_hp2=tertanggung.getNo_hp();
    			if (no_hp != null)
    			{
	    			for (int i =0 ; i< no_hp.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-142+(i*12),y-112, 0);
	    			}
    			}
    			imel= tertanggung.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT, imel, x-362, y-126, 0);
    			tujuan = tertanggung.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-463, y-152, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-320, y-152, 0);
	    				}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x-188, y-152, 0);//x-188, y-152
	    					}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-463, y-166, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-320, y-166, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-188, y-166, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x-108, y-166, 0);
	    							}
    			}
    			dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-189, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-201, 0);
	    				}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-321, y-189, 0);// x-321, y-189
	    					}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-321, y-201, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-188, y-189, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-108, y-191, 0);
	    						}
    			}
    			pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-362, y-215, 0);
    			
//    			String uraian_kerja =pemegang.getMpn_job_desc();
//    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-350, y-215, 0);
    			
    			jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-140, y-215, 0);
    			masa_kerja= pemegang.getMspe_lama_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x+30, y-215, 0);
    			
    			penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-240, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-253, 0);
	    				}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-321, y-240, 0);//x-321, y-240
	    					}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-321, y-253, 0);
		    					}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-188, y-240, 0);
		    						}else if (penghasilan.indexOf("> RP. 500 JUTA") >= 0)
		    							{
		    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-188, y-253, 0);
		    							}
    			}
    			smbr_dana = pemegang.getMkl_pendanaan();
    			if (smbr_dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-279, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-460, y-293, 0);
	    				}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-322, y-279, 0);// x-322, y-279
	    					}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-322, y-293, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-188, y-279, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-108, y-280, 0);
	    						}
    			}
    			//data datausulan
    			String kurs = datausulan.getLku_id();
    			String krs = "";
    			if (kurs != null)
    			{
	    			if (kurs.equalsIgnoreCase("01"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-386, y-343, 0);//x-386, y-343
	    				krs= "Rp ";
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-268, y-343, 0);
	    				krs = "U$ ";
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x-44+(i*13), y-353, 0);
	    			}
	    			
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x-9+(i*13), y-353, 0);
	    			}
	    			
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x+25+(i*13), y-353, 0);
	    			}
    			}
    			
    			Double premi = datausulan.getMspr_premium();
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-392, y-353, 0);
    			
    			
    			String nama_produk = datausulan.getLsdbs_name();
    			Integer idx = nama_produk.indexOf("BULAN");
    			
    			String mgi = data_pwr.getMps_jangka_inv();
    			if (mgi !=null)
    			{
    			
	    			switch (Integer.parseInt(mgi))
	    			{
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT,  "X" ,  x-304, y-363, 0);//303, y-363
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-251, y-363, 0);
	    					break;
	    				case 12:
	    					if (idx <0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-207, y-363, 0);
	    					}else{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-108, y-363, 0);
	    					}
	    					break;
	    				case 36:
	    					if (idx <0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-157, y-363, 0);
	    					}else{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-12, y-363, 0);
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
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-351, y-373, 0);
    						break;
    					case 2:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-351, y-383, 0);
    						break;
    					case 3:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-351, y-394, 0);//x-351, y-394
    						break;
    				}
    				
    			}
    			String kurs_rek = data_rek.getMrc_kurs();
    			if (kurs_rek != null)
    			{
    				if (kurs_rek.equalsIgnoreCase("01")){
    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-94, y-395, 0);//x-94, y-395
    				}else{
    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-57, y-395, 0);
    				}
    			}
    			
//    			String nama_bank = data_rek.getLsbp_nama();
//    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-305, y-532, 0);
    			no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-228, y-408, 0);
    			
    			atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-55, y-408, 0);
    			
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
    					
    					over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-460, y-442 + (i*8), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-150, y-442 + (i*8), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x-65,y-442 + (i*8), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x+37,y-442 + (i*8), 0);
    				}
    				
    				String no_spaj = pemegang.getReg_spaj();
    				Date tgl_spaj = pemegang.getMspo_spaj_date();
    				cabang = dataagen.getLsrg_nama();
//    				over.showTextAligned(Element.ALIGN_LEFT, FormatString.nomorSPAJ(no_spaj) , x-410, y-3, 0);
    			}
    			
	    		String nama_agen = dataagen.getMcl_first();	
	    		String kode_agen = dataagen.getMsag_id();
//	    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x+15,y-858, 0);
//	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x+15,y-848, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-327,y-830, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-465,y-830, 0);
    							   		
    			
		return over;
	}
	
}
