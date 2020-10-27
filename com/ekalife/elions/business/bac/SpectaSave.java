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

public class SpectaSave {
	protected final Log logger = LogFactory.getLog( getClass() );
	private static SpectaSave instance;
	
	public static SpectaSave getInstance()
	{
		if( instance == null )
		{
			instance = new SpectaSave();
		}
		return instance;
	}
	
	public SpectaSave()
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
				
				over.showTextAligned(Element.ALIGN_LEFT, noBlanko , x-28, y+133, 0);
				String no_spaj = pemegang.getReg_spaj();
				over.showTextAligned(Element.ALIGN_LEFT, FormatString.nomorSPAJ(no_spaj) , x-42, y+145, 0);
				
    			//data pemegang
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x+18, y+100, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x+18, y+87, 0);
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y+74, 0);
	    			}else{//x+95, y+74
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x+172, y+74, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-250, y+74, 0);
	    			}
    			}
	    		String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x+22+(i*13), y+61, 0);
	    			}
    			}
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y+20, 0);//Menikah
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y+45, 0);//single
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y+7, 0);//janda
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y+7, 0);//duda
	    					break;
	    			}
    			}				    			
    			
    			
    			Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    		if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-7, 0);//x+22, y-7
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X",x+88, y-7, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x+127, y-7, 0);
	    			}
    			}
    			
    			String nama_istri= pemegang.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x+155, y+34, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak1(), x+155, y+24, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x+155, y+14, 0);//x+155, y+14
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x+155, y+4, 0);
    			
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+340+(i*10), y+35, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+370+(i*10), y+35, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+400+(i*10), y+35, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+340+(i*10), y+24, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+370+(i*10), y+24, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+400+(i*10), y+24, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(tgl_lhr.charAt(i)), x+340+(i*10), y+14, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,String.valueOf(bln_lhr.charAt(i)), x+370+(i*10), y+14, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+400+(i*10), y+14, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+340+(i*10), y+4, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+370+(i*10), y+4, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(thn_lhr.charAt(i)),  x+400+(i*10), y+4, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+42+(i*10), y-17, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+95+(i*13),y-17, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x+153+(i*13), y-17, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x+230, y-17, 0);
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-29, 0);// pria  x+22, y-29
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x+67, y-29, 0);// wanita
	    			}
    			}
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-40, 0);//islam
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+67, y-40, 0);//protestan
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+129, y-40, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x+189, y-40, 0);//hindu
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+240, y-40, 0);//budha 
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-52, 0);//SD x+22, y-52
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+67, y-52, 0);//smp
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+112, y-52, 0);//smu
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+158, y-52, 0);//d1/d3
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+213, y-52, 0);//s1
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x+291, y-52, 0);//lainnya
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x+337, y-52, 0);
	    					break;
	    			}
    			}
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x+18, y-67, 0);
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x+22+(i*13),y-89, 0);
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x+24+(i*13),y-101, 0);
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x+86+(i*13),y-101, 0);
	    			}
    			}
    			
    			String alamat_kantor = pemegang.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x+18, y-115, 0);
    			
    			String kode_pos_kantor = pemegang.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)), x+22+(i*13),y-138, 0);
	    			}
    			}
    			
    			String area_kantor = pemegang.getArea_code_kantor();
    			if (area_kantor != null)
    			{
	    			for (int i =0 ; i< area_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kantor.charAt(i)), x+24+(i*13),y-150, 0);
	    			}
    			}
    			
    			String telp_kantor = pemegang.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x+86+(i*13),y-150, 0);
	    			}
    			}
    			
    			String alamat_tagihan = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x+18, y-164, 0);
    			
    			String kode_pos_tagihan = pemegang.getKd_pos_rumah();
    			if (kode_pos_tagihan != null)
    			{
	    			if ( kode_pos_tagihan!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_tagihan.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_tagihan.charAt(i)), x+22+(i*13),y-188, 0);
		    			}
	    			}
    			}
    			
    			String area_tagihan = pemegang.getArea_code_rumah();
    			if (area_tagihan != null)
    			{
	    			for (int i =0 ; i< area_tagihan.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_tagihan.charAt(i)), x+24+(i*13),y-200, 0);
	    			}
    			}
    			String telp_tagihan = pemegang.getTelpon_rumah();
    			if (telp_tagihan != null)
    			{
	    			for (int i =0 ; i< telp_tagihan.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_tagihan.charAt(i)), x+86+(i*13),y-200, 0);
	    			}
    			}
    			String no_hp=pemegang.getNo_hp();
    			if (no_hp != null)
    			{
	    			for (int i =0 ; i< no_hp.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x+22+(i*13),y-213, 0);
	    			}
    			}
    			String imel= pemegang.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT, imel, x+18, y-226, 0);
    			
    			String tujuan = pemegang.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X",x-79, y-252, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+60, y-252, 0);
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x+193, y-252, 0);//x+193, y-252
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-79, y-265, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x+60, y-265, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x+193, y-265, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x+269, y-265, 0);
	    								}
    			}
    			
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-79, y-287, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-79, y-302, 0);
	    					}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x+60, y-287, 0);//x+60, y-287
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X",x+60, y-302, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x+193, y-287, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, "dana",x+269, y-287, 0);
				    				}
    			}
    			
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x+18, y-314, 0);
    			
//    			String uraian_kerja =pemegang.getMpn_job_desc();
//    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x+60, y-156, 0);
    			
    			String jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x+245, y-314, 0);
    			
    			Integer masa_kerja= pemegang.getMspe_lama_kerja();
    			if (masa_kerja!=null){
    				over.showTextAligned(Element.ALIGN_LEFT,  masa_kerja.toString() , x+410, y-314, 0);
    			}else 
    				over.showTextAligned(Element.ALIGN_LEFT, "-", x+410, y-314, 0);
    							    								
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-79,  y-340, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-79,  y-353, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "X", x+60,  y-340, 0);//x+60, y-340
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x+60,  y-353, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x+193,  y-340, 0);
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x+193,  y-353, 0);
		    							}
    			}
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x+135, y-405, 0);
    			String smbr_hasil = pemegang.getMkl_pendanaan();
    			if (smbr_hasil !=null)
    			{
	    			if (smbr_hasil.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-79, y-379, 0);
	    			}else if (smbr_hasil.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-79, y-393, 0);
	    					}else if (smbr_hasil.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x+60, y-379, 0);//x+60, y-379
	    						}else if (smbr_hasil.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X",x+60, y-393, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x+193, y-379, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana,x+269, y-379, 0);
				    				}
    			}
    			
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x+25, y-447, 0);
    			String no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x+25, y-416, 0);    			
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x+25, y-432, 0);
    			String cabang = data_rek.getMrc_cabang();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang , x+25, y-459, 0);
    			String kota = data_rek.getMrc_kota();
    			over.showTextAligned(Element.ALIGN_LEFT,  kota , x+278, y-459, 0);
    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x+18, y-503, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x+18, y-517, 0);
    			jns_identitas = tertanggung.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			if (jns_identitas != null)
    			{
    				if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "R", x+22, y-529, 0);
	    			}else{//x+95, y+74
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x+172, y-529, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-250, y-529, 0);
	    			}
    			}
    			no_identitas = tertanggung.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x+22+(i*13), y-544, 0);
	    			}
    			}
    			
    			sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-584, 0);//Menikah
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-558, 0);//single
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-596, 0);//janda x+22, y-596
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-596, 0);//duda
	    					break;
	    			}
    			}				    			
    			
    			
    			id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (nama_negara != null)
    			{
	    		if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-610, 0);//x+22, y-7
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X",x+88, y-610, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x+127, y-610, 0);
	    			}
    			}
    			
    			nama_istri= tertanggung.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x+155, y-570, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak1(), x+155, y-580, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x+155, y-590, 0);//x+155, y+14
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x+155, y-600, 0);
    			
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+340+(i*10), y-568, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+370+(i*10), y-568, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+400+(i*10), y-568, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+340+(i*10), y-579, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+370+(i*10), y-579, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+400+(i*10), y-579, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(tgl_lhr.charAt(i)), x+340+(i*10), y-589, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,String.valueOf(bln_lhr.charAt(i)), x+370+(i*10), y-589, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+400+(i*10), y-589, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+340+(i*10), y-599, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+370+(i*10), y-599, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(thn_lhr.charAt(i)),  x+400+(i*10),y-599, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x+42+(i*10), y-621, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x+95+(i*13),y-621, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x+153+(i*13), y-621, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x+230, y-621, 0);
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-633, 0);// pria  x+22, y-29
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x+67, y-633, 0);// wanita
	    			}
    			}
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-644, 0);//islam
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+67, y-644, 0);//protestan
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+129, y-644, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x+189, y-644, 0);//hindu
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+240, y-644, 0);//budha 
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+22, y-655, 0);//SD x+22, y-52
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+67, y-655, 0);//smp
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+112, y-655, 0);//smu
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+158, y-655, 0);//d1/d3
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+213, y-655, 0);//s1
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x+291, y-655, 0);//lainnya
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x+337, y-655, 0);
	    					break;
	    			}
    			}
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x+18, y-670, 0);
    			
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x+22+(i*13),y-692, 0);
		    			}
	    			}
    			}
    			
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x+24+(i*13),y-704, 0);
	    			}
    			}
    			
    			telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x+86+(i*13),y-704, 0);
	    			}
    			}
    			//Ganti halaman
    			Integer nOne = reader.getNumberOfPages();
				
				logger.info("jumlah page:"+nOne);
				
				over.endText();
				
				over = stamp.getOverContent(2);
		    	over.beginText();
		    	over.setFontAndSize(bf, f); //set ukuran font
    			
    			alamat_kantor = tertanggung.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x+18, y+157, 0);
    			
    			kode_pos_kantor = tertanggung.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)), x+22+(i*13),y+135, 0);
	    			}
    			}
    			
    			area_kantor = tertanggung.getArea_code_kantor();
    			if (area_kantor != null)
    			{
	    			for (int i =0 ; i< area_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kantor.charAt(i)), x+24+(i*13),y+124, 0);
	    			}
    			}
    			
    			telp_kantor = tertanggung.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x+86+(i*13),y+124, 0);
	    			}
    			}
    			
    			alamat_tagihan = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x+18, y+109, 0);
    			
    			kode_pos_tagihan = tertanggung.getKd_pos_rumah();
    			if (kode_pos_tagihan != null)
    			{
	    			if ( kode_pos_tagihan!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_tagihan.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_tagihan.charAt(i)), x+22+(i*13),y+87, 0);
		    			}
	    			}
    			}
    			
    			area_tagihan = tertanggung.getArea_code_rumah();
    			if (area_tagihan != null)
    			{
	    			for (int i =0 ; i< area_tagihan.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_tagihan.charAt(i)), x+24+(i*13),y+74, 0);
	    			}
    			}
    			telp_tagihan = tertanggung.getTelpon_rumah();
    			if (telp_tagihan != null)
    			{
	    			for (int i =0 ; i< telp_tagihan.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_tagihan.charAt(i)), x+86+(i*13),y+74, 0);
	    			}
    			}
    			no_hp=tertanggung.getNo_hp();
    			if (no_hp != null)
    			{
	    			for (int i =0 ; i< no_hp.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x+22+(i*13),y+62, 0);
	    			}
    			}
    			imel= tertanggung.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT, "imel", x+18, y+48, 0);
    			
    			tujuan = tertanggung.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X",x-79, y+22, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x+60, y+22, 0);
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x+193, y+22, 0);//"X", x+193, y+22
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-79, y+8, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x+60, y+8, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x+193,y+8, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x+269, y+8, 0);
	    								}
    			}
    			dana = tertanggung.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-79, y-15, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-79, y-28, 0);
	    					}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x+60, y-15, 0);//x+60, y-287
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X",x+60, y-28, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x+193, y-15, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana,x+269, y-15, 0);
				    				}
    			}
    			
    			pekerjaan =  tertanggung.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x+18, y-41, 0);
    			  			
//    			String uraian_kerja =pemegang.getMpn_job_desc();
//    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x+60, y-156, 0);
    			
    			jabatan = tertanggung.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x+245, y-41, 0);
    			
    			masa_kerja= tertanggung.getMspe_lama_kerja();
    			if (masa_kerja!=null){
    				over.showTextAligned(Element.ALIGN_LEFT,  masa_kerja.toString() , x+410, y-41, 0);
    			}else 
    				over.showTextAligned(Element.ALIGN_LEFT, "-", x+410, y-41, 0);
    			 
    			penghasilan = tertanggung.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-79, y-66, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-79,  y-78, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "X", x+60,  y-66, 0);//x+60,  y-66
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x+60,  y-78, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x+193,  y-66, 0);
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x+193,  y-78, 0);
		    							}
    			}
    			smbr_hasil = tertanggung.getMkl_pendanaan();
    			if (smbr_hasil !=null)
    			{
	    			if (smbr_hasil.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-79, y-105, 0);
	    			}else if (smbr_hasil.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-79, y-118, 0);
	    					}else if (smbr_hasil.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X",x+58, y-105, 0);//x+62, y-105
	    						}else if (smbr_hasil.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X",x+58, y-118, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x+193, y-105, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana,x+269, y-105, 0);
				    				}
    			}
//    			data datausulan
    			//String nama_produk = datausulan.getLsdbs_name();
    			//over.showTextAligned(Element.ALIGN_LEFT,  nama_produk , x-920, y-400, 0);
    			
    			String kurs = datausulan.getLku_id();
    			String krs= "";
    			if (kurs != null)
    			{
	    			if (kurs.equalsIgnoreCase("01"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x-6, y-157, 0); //x-6, y-157
	    				krs= "Rp ";
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+113, y-157, 0);
	    				krs = "U$ ";
	    			}
    			}
    			
    			Double premi = datausulan.getMspr_premium();
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-10, y-166, 0);
    			
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x+335+(i*12), y-166, 0);
	    			}
	    			
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x+370+(i*12), y-166, 0);
	    			}
	    			
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x+403+(i*12), y-166, 0);
	    			}
    			}
    			String nama_produk = datausulan.getLsdbs_name();
    			Integer idx = nama_produk.indexOf("BULAN");
    			String mgi = data_pwr.getMps_jangka_inv();
    			if (mgi !=null)
    			{
    			
	    			switch (Integer.parseInt(mgi))
	    			{
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x+77, y-177, 0);// x+77, y-177
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x+130, y-177, 0);
	    					break;
	    				case 12:
	    					if (idx <0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x+173, y-177, 0);
	    					}else{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x+271, y-177, 0);
	    					}
	    					break;
	    				case 36:
	    					if (idx <0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+225, y-177, 0);
	    					}else{
	    						over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x+369, y-177, 0);
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
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+28, y-187, 0);
    						break;
    					case 2:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x+28, y-198, 0);
    						break;
    					case 3:
    						over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+28, y-209, 0);//x+28, y-209
    						break;
    				}
    				
    			}
    			
//    			String nama_bank = data_rek.getLsbp_nama();
//    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x+90, y-449, 0);
    			
    			no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x+153, y-222, 0);
    			
    			atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x+320, y-222, 0);
    					
    			String kurs_rek = data_rek.getMrc_kurs();
    			if (kurs_rek != null)
    			{
    				if (kurs_rek.equalsIgnoreCase("01")){
    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+286, y-209, 0);//x+286, y-209
    				}else{
    					over.showTextAligned(Element.ALIGN_LEFT,  "X" , x+324, y-209, 0);
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
	    				
	    				over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-80,y-273-(i*7), 0);
	    				
	    				over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x+225, y-(273+ (i*7)), 0);
	    				over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x+325,y-(273+ (i*7)), 0);
	    				over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x+417,y-(273+ (i*7)), 0);
	    			}
	    		}
	    			
	    		String nama_agen = dataagen.getMcl_first();	
	    		String kode_agen = dataagen.getMsag_id();
//	    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x+365,y-688, 0);
//	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x+365,y-680, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x+60,y-683, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-80,y-683, 0);
				Date tgl_spaj = pemegang.getMspo_spaj_date();
				
				mulai_tanggung = datausulan.getMste_beg_date();
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x+205+(i*3), y-654, 0);
	    			}
	    			over.showTextAligned(Element.ALIGN_LEFT, "-", x+212, y-654, 0);
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x+215+(i*3), y-654, 0);
	    			}
	    			over.showTextAligned(Element.ALIGN_LEFT, "-", x+223, y-654, 0);
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x+226+(i*3), y-654, 0);
	    			}
    			}
				
				cabang = dataagen.getLsrg_nama();
				over.showTextAligned(Element.ALIGN_LEFT, cabang , x+340,y-617, 0);
    							   
    			
		return over;
	}
	
}
