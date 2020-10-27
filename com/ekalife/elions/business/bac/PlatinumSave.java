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

public class PlatinumSave {
	protected final Log logger = LogFactory.getLog( getClass() );
	private static PlatinumSave instance;
	
	public static PlatinumSave getInstance()
	{
		if( instance == null )
		{
			instance = new PlatinumSave();
		}
		return instance;
	}
	
	public PlatinumSave()
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
				over.showTextAligned(Element.ALIGN_LEFT, noBlanko , x-416, y+10, 0);
		
				over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-355, y-36, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-355, y-49, 0);
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-61, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-203, y-61, 0);// x-203, y-61
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-158, y-61, 0);
	    			}
    			}
	    		String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-352+(i*13), y-76, 0);
	    			}
    			}
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-90, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", 352, y-116, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-127, 0);//x-352, y-127
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-127, 0);
	    					break;
	    			}
    			}
    			String nama_istri= pemegang.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-218, y-101, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak1(), x-218, y-111, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x-218, y-121, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x-218, y-131, 0);
    			
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-33+(i*12), y-100, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-5+(i*12), y-100, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+24+(i*12), y-100, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-33+(i*12), y-110, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-5+(i*12), y-110, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+24+(i*12), y-110, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-120, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-120, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-120, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-130, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-130, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-130, 0);
	    			}
    			}
    			
		    	Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-142, 0); //x-352, y-142
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-287, y-142, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-245, y-142, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-335+(i*13), y-153, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-280+(i*13), y-153, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-220+(i*13), y-153, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-145, y-153, 0);
    			
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-307, y-164, 0);//x-352, y-164
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-164, 0);
	    			}
    			}
    			
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-175, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-306, y-175, 0); //protestan
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-245, y-175, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-184, y-175, 0);//hindu
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-133, y-175, 0);//budha x-133, y-175
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-187, 0);//SD x-352, y-187
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-306, y-187, 0);//SMP
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-187, 0);//SMU
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-216, y-187, 0);//D1/D3
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-162, y-187, 0); //s1
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-120, y-187, 0); //S2
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x-37, y-187, 0);
	    					break;
	    			}
    			}
    			
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-355, y-201, 0);
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-352+(i*13),y-224, 0);
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-349+(i*13),y-236, 0);
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-287+(i*13),y-236, 0);
	    			}
    			}
    			
    			String alamat_kantor = pemegang.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-355, y-250, 0);
    			
    			String kode_pos_kantor = pemegang.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)), x-352+(i*13),y-273, 0);
	    			}
    			}
    			
    			String area_kantor = pemegang.getArea_code_kantor();
    			if (area_kantor != null)
    			{
	    			for (int i =0 ; i< area_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kantor.charAt(i)),x-349+(i*13),y-285, 0);
	    			}
    			}
    			
    			String telp_kantor = pemegang.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-287+(i*13),y-285, 0);
	    			}
    			}
    			
    			String alamat_tagihan= pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_tagihan, x-355, y-301, 0);
    			
    			String kode_pos_tagihan = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-352+(i*13),y-323, 0);
		    			}
	    			}
    			}
    			
    			String area_tagihan = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-349+(i*13),y-334, 0);
	    			}
    			}
    			
    			String telp_tagihan = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-287+(i*13),y-334, 0);
	    			}
    			}
    			String no_hp=pemegang.getNo_hp();
    			if (no_hp != null)
    			{
	    			for (int i =0 ; i< no_hp.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-350+(i*13),y-346, 0);
	    			}
    			}
    			String imel= pemegang.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT, imel, x-355, y-361, 0);
    			String tujuan = pemegang.getMkl_tujuan();
    			
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-387, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-387, 0);
	    				}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-181, y-387, 0);//x-181, y-387
	    					}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-400, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-400, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X",x-181, y-400, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x-103, y-400, 0);
	    							}
    			}
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-424, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-436, 0);
	    				}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-424, 0);//x-314, y-424
	    					}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-436, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-181, y-424, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-103, y-424, 0);
	    						}
    			}
    			
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-355, y-448, 0);
    			
//    			String uraian_kerja =pemegang.getMpn_job_desc();
//    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-350, y-176, 0);
    			
    			String jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  "jabatan" , x-132, y-448, 0);
   			
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-474, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-489, 0);
	    				}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-474, 0);//x-314, y-474
	    					}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-489, 0);
		    					}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-181, y-474, 0);
		    						}else if (penghasilan.indexOf("> RP. 500 JUTA") >= 0)
		    							{
		    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-181, y-489, 0);
		    							}
    			}
    			String hasil = pemegang.getMkl_pendanaan();
    			if (hasil !=null)
    			{
	    			if (hasil.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-513, 0);
	    			}else if (hasil.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-528, 0);
	    				}else if (hasil.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-513, 0);//x-314, y-424
	    					}else if (hasil.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-528, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-181, y-513, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, hasil, x-103, y-513, 0);
	    						}
    			}
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x-240, y-541, 0);
    			String no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-349, y-551, 0);
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-349, y-566, 0);			
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-349, y-580, 0);   			
    			String cabang = data_rek.getMrc_cabang();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang , x-349, y-595, 0);
    			String kota = data_rek.getMrc_kota();
    			over.showTextAligned(Element.ALIGN_LEFT,  kota , x-98, y-595, 0);
    		
    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-355, y-639, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-355, y-652, 0);
    			jns_identitas = tertanggung.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			if (jns_identitas != null)
    			{
    				if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-664, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-203, y-664, 0);// x-203, y-61
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-158, y-664, 0);
	    			}
    			}
    			
	    		no_identitas = tertanggung.getMspe_no_identity();
	    		if (no_identitas  !=null)
	    		{
	    			for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-352+(i*13), y-677, 0);
	    			}
	    		}
	    		sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-694, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-719, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-719, 0);//x-352, y-731
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-731, 0);
	    					break;
	    			}
    			}
    			nama_istri= tertanggung.getNama_si();
    			over.showTextAligned(Element.ALIGN_LEFT, nama_istri, x-218, y-705, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getNama_anak1(), x-218, y-715, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak2(), x-218, y-725, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getNama_anak3(), x-218, y-735, 0);
    			
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-33+(i*12), y-704, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-5+(i*12), y-704, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+24+(i*12), y-704, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-33+(i*12), y-714, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-5+(i*12), y-714, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+24+(i*12), y-714, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-724, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-724, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-724, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-40+(i*10), y-734, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-12+(i*10), y-734, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  String.valueOf(thn_lhr.charAt(i)),  x+18+(i*10), y-734, 0);
	    			}
    			}
	    		id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (id_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-745, 0); //x-352, y-142
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-287, y-745, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-245, y-745, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-335+(i*13), y-756, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-280+(i*13), y-756, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-220+(i*13), y-756, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_place_birth(), x-145, y-756, 0);
    			
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin != null)
    			{
    				if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-307, y-768, 0);//x-352, y-164
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-768, 0);
	    			}
    			}
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama !=null)
    			{
    				switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-779, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-306, y-779, 0); //protestan
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-245, y-779, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-184, y-779, 0);//hindu
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-133, y-779, 0);//budha x-133, y-175
	    					break;
	    			}
    			}
    			id_pendidikan = tertanggung.getLsed_id();
    			pendidikan = tertanggung.getLsed_name();
    			if (id_pendidikan != null)
    			{
	    				
	    			switch (id_pendidikan)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-352, y-790, 0);//SD x-352, y-187
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-306, y-790, 0);//SMP
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-790, 0);//SMU
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-216, y-790, 0);//D1/D3
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-162, y-790, 0); //s1
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-120, y-790, 0); //S2
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x-37, y-790, 0);
	    					break;
	    			}
    			}
	    			
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-355, y-805, 0);
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-352+(i*13),y-827, 0);
		    			}
	    			}
    			}
    			
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-349+(i*13),y-840, 0);
	    			}
    			}
    			
    			telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-287+(i*13),y-840, 0);
	    			}
    			}
    			Integer nOne = reader.getNumberOfPages();
				
				logger.info("jumlah page:"+nOne);
				
				over.endText();
				
				over = stamp.getOverContent(2);
		    	over.beginText();
		    	over.setFontAndSize(bf, f); //set ukuran font
    			
    			alamat_kantor = tertanggung.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-355, y+24, 0);
    			
    			kode_pos_kantor = tertanggung.getKd_pos_kantor();
    			if ( kode_pos_kantor!=null)
    			{
	    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-352+(i*13),y, 0);
	    			}
    			}
    			
    			area_kantor = tertanggung.getArea_code_kantor();
    			if (area_kantor != null)
    			{
	    			for (int i =0 ; i< area_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kantor.charAt(i)),x-349+(i*13),y-11, 0);
	    			}
    			}
    			
    			telp_kantor = tertanggung.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-287+(i*13),y-11, 0);
	    			}
    			}	
    			alamat_tagihan= tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_tagihan, x-355, y-26, 0);	
    			kode_pos_tagihan = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-352+(i*13),y-49, 0);
		    			}
	    			}
    			}area_tagihan = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-349+(i*13),y-60, 0);
	    			}
    			}
    			telp_tagihan = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-287+(i*13),y-60, 0);
	    			}
    			}
    			no_hp=tertanggung.getNo_hp();
    			if (no_hp != null)
    			{
	    			for (int i =0 ; i< no_hp.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-350+(i*13),y-73, 0);
	    			}
    			}
    			imel= tertanggung.getEmail();
    			over.showTextAligned(Element.ALIGN_LEFT,imel, x-355, y-88, 0);
    			
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-113, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-113, 0);
	    				}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-181, y-113, 0);//x-181, y-113
	    					}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-127, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-127, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X",x-181, y-113, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x-103, y-113, 0);
	    							}
    			}
    			dana = tertanggung.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-149, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-163, 0);
	    				}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-149, 0);//x-314, y-424
	    					}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-163, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-181, y-149, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-103, y-149, 0);
	    						}
    			}
    			
    			pekerjaan =  tertanggung.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-355, y-175, 0);
//    			uraian_kerja =tertanggung.getMpn_job_desc();
//    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-350, y-424, 0);
    			
    			jabatan = tertanggung.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-132, y-175, 0);
    			
    			penghasilan = tertanggung.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-201, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-214, 0);
	    				}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-201, 0);//x-314, y-201
	    					}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-314, y-214, 0);
		    					}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-181, y-201, 0);
		    						}else if (penghasilan.indexOf("> RP. 500 JUTA") >= 0)
		    							{
		    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-181, y-214, 0);
		    							}
    			}
    			hasil = tertanggung.getMkl_pendanaan();
    			if (hasil !=null)
    			{
	    			if (hasil.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-241, 0);
	    			}else if (hasil.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-452, y-254, 0);
	    				}else if (hasil.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-316, y-241, 0);//x-316, y-241
	    					}else if (hasil.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-316, y-254, 0);
	    						}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-179, y-241, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, hasil, x-103, y-241, 0);
	    						}
    			}
    			//data datausulan
    			String kurs = datausulan.getLku_id();
    			String krs = "";
    			if (kurs != null)
    			{
	    			if (kurs.equalsIgnoreCase("01"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" ,x-380, y-305, 0);//x-380, y-305
	    				krs= "Rp ";
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "X" , x-262, y-305, 0);
	    				krs = "U$ ";
	    			}
    			}
    			
    			Double premi = datausulan.getMspr_premium();
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-383, y-314, 0);
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
    			
//    			nama_bank = data_rek.getLsbp_nama();
//    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-293, y-366, 0);
    			no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-223, y-369, 0);
    			atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-54, y-369, 0);
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
    					
    					over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-453, y-402 + (i*9), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-150, y-402 + (i*9), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x-47,y-402 + (i*9), 0);
    					over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x+45,y-402 + (i*9), 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x-170+(i*3), y-735, 0);
	    			}
	    			over.showTextAligned(Element.ALIGN_LEFT, "-", x-163, y-735, 0);
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x-159+(i*3), y-735, 0);
	    			}
	    			over.showTextAligned(Element.ALIGN_LEFT, "-", x-150, y-735, 0);
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x-147+(i*3), y-735, 0);
	    			}
    			}
    						
    			String no_spaj = pemegang.getReg_spaj();
    			Date tgl_spaj = pemegang.getMspo_spaj_date();
    			cabang = dataagen.getLsrg_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang , x-36,y-692, 0);
	    		String nama_agen = dataagen.getMcl_first();	
	    		String kode_agen = dataagen.getMsag_id();
//	    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x-27,y-850, 0);
//	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x-27,y-844, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-327,y-767, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-465,y-767, 0);
	    		
	    	
    			
		return over;
	}
	
}
