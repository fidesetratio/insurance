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

public class SuperProtection {
	protected final Log logger = LogFactory.getLog( getClass() );
	private static SuperProtection instance;
	
	public static SuperProtection getInstance()
	{
		if( instance == null )
		{
			instance = new SuperProtection();
		}
		return instance;
	}
	
	public SuperProtection()
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
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-265, y-77, 0);  
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-265, y-92, 0);
    			
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-108, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-217, y-108, 0);//x-217, y-113
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-177, y-108, 0);
	    				}
    			}
    			
    			String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-95+(i*5), y-108, 0);
	    			}
    			}
    			
    			Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-122, 0);//x-262, y-122
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-217, y-122, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-177, y-123, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-262+(i*10), y-139, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-217+(i*10), y-139, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-174+(i*12), y-139, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-107, y-139, 0);
    			
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-217, y-153, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-153, 0);
	    				}
    			}
    			
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-104, y-153, 0);//blum kawin
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "S", x-61, y-153, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "s", x-21, y-153, 0);//x-23, y-159
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-21, y-153, 0);
	    					break;
	    			}
    			}
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-166, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-217, y-166, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-172, y-166, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-105, y-166, 0);//hindu
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-61, y-166, 0);// x-61, y-172
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-180, 0);//SD
	    					break;	    				
    					case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-172, y-180, 0);//smu
	    					break;
	    				case 9:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-217, y-180, 0);//smp x-217, y-186
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-138, y-180, 0);//S1
	    						break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-61, y-180, 0);//S2
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-100, y-180, 0);//D1-3
	    						break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-23, y-180, 0);//lainnya
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan,  x+18, y-180, 0);
	    					break;
	    			}
    			}
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-265, y-200, 0);
    			String kota_rumah = pemegang.getKota_rumah();
    			if (kota_rumah != null)
    			{
	    			for (int i =0 ; i< kota_rumah.length() ; i++)
	    			{
//	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_rumah.charAt(i)), x-247+(i*5),y-221, 0);
	    			}
    			}
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)),x-96+(i*5),y-216, 0); // x-286+(i*10),y-160
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-22+(i*5),y-216, 0);
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x+(i*5),y-216, 0);
	    			}
    			}
    			String alamat_kores = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kores, x-265, y-233, 0);
    			String kota_kores = pemegang.getKota_rumah();
    			if (kota_kores != null)
    			{
	    			for (int i =0 ; i< kota_kores.length() ; i++)
	    			{
//	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_kores.charAt(i)), x-247+(i*5),y-245, 0);
	    			}
    			}
    			
    			String kode_pos_kores = pemegang.getKd_pos_rumah();
    			if (kode_pos_kores != null)
    			{
	    			if ( kode_pos_kores!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_kores.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kores.charAt(i)),x-96+(i*5),y-248, 0); // x-286+(i*10),y-160
		    			}
	    			}
    			}
    			
    			String area_kores = pemegang.getArea_code_rumah();
    			if (area_kores != null)
    			{
	    			for (int i =0 ; i< area_kores.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kores.charAt(i)), x-22+(i*5),y-248, 0);
	    			}
    			}
    			
    			String telp_kores = pemegang.getTelpon_rumah();
    			if (telp_kores != null)
    			{
	    			for (int i =0 ; i< telp_kores.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kores.charAt(i)), x+(i*5),y-248, 0);
	    			}
    			}
    			String hp= pemegang.getNo_hp();
    			if (hp != null)
    			{
    				for (int i =0 ; i< hp.length() ; i++)
    	    		{
    	    			over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(hp.charAt(i)), x-265+(i*5),y-266, 0);
    	    		}
    			}
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getEmail(), x-106, y-266, 0);
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-265, y-282, 0);
    			
    			String uraian_kerja =pemegang.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-265, y-297, 0);
    			String jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,jabatan , x-104, y-297, 0);
    			String tujuan = pemegang.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "x", x-350, y-322, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "x", x-261, y-322, 0);
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-173, y-322, 0);//  x-173, y-322
	    							}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "x", x-350, y-335, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "x", x-261, y-335, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "x", x-173, y-335, 0);//lain2
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan,x-107, y-336, 0);
	    								}
    			}
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-350, y-361, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-350, y-374, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "x", x-216, y-361, 0);   //  x-216, y-361x-216, y-367
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-216, y-374, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-75, y-361, 0);
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-75, y-374, 0);
		    							}
    			}
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-350, y-400, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-350, y-413, 0);
	    					}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-413, 0);//x-262, y-400
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-413, 0);
	    							}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-205, y-400, 0);//lain2
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-147, y-399, 0);
				    				}
    			}
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x-185, y-426, 0);
    			String kurs = datausulan.getLku_id();
    			String krs ="";
    			if (kurs != null)
    			{
	    			if (kurs.equalsIgnoreCase("01"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "x" ,  x-283, y-440, 0); // x-283, y-446
	    				krs="Rp ";
	    			}else{
	    				krs= "U$ ";
	    				over.showTextAligned(Element.ALIGN_LEFT,  "x" ,x-237, y-440, 0);
	    			}
    			}
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-285, y-470, 0);
//    			String no_rek = data_rek.getMrc_no_ac();
//    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-332, y-482, 0);
    			
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-285, y-456, 0);
    			
    			String cabang_bank = data_rek.getMrc_cabang();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang_bank, x-285, y-483, 0);
    			
//    			String kota_bank = data_rek.getMrc_kota();
//    			over.showTextAligned(Element.ALIGN_LEFT,  kota_bank, x-21, y-475, 0);

    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-264, y-555, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-264, y-568, 0);
    			
    			jns_identitas = tertanggung.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-581, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-217, y-581, 0);//x-217, y-113
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-177, y-581, 0);
	    				}
    			}
    			
    			no_identitas = tertanggung.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-95+(i*5), y-581, 0);
	    			}
    			}
    			
    			id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-594, 0);//x-262, y-130
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-217, y-594, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-177, y-594, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-262+(i*10), y-611, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-2107+(i*10), y-611, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-174+(i*12), y-611, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_place_birth(), x-107, y-611, 0);
    			
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-217, y-625, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-625, 0);
	    				}
    			}
    			sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-104, y-625, 0);//blum kawin
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-61, y-625, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-23, y-625, 0);//x-23, y-159
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-23, y-625, 0);
	    					break;
	    			}
    			}
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-638, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-217, y-638, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-172, y-638, 0);//katolik
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-105, y-638, 0);//hindu
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-61, y-638, 0);// x-61, y-172
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-262, y-652, 0);//SD
	    					break;	    				
    					case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-172, y-652, 0);//smu
	    					break;
	    				case 9:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",x-217, y-652, 0);//smp x-217, y-186
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X",  x-138, y-652, 0);//S1
	    						break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-61, y-652, 0);//S2
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-100, y-652, 0);//D1-3
	    						break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-23, y-652, 0);//lainnya
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan,  x+18, y-652, 0);
	    					break;
	    			}
    			}
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-265, y-667, 0);
    			kota_rumah = tertanggung.getKota_rumah();
    			if (kota_rumah != null)
    			{
	    			for (int i =0 ; i< kota_rumah.length() ; i++)
	    			{
//	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_rumah.charAt(i)), x-247+(i*5),y-682, 0);
	    			}
    			}
    			
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)),x-96+(i*5),y-680, 0); // x-286+(i*10),y-160
		    			}
	    			}
    			}
    			
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-22+(i*5),y-680, 0);
	    			}
    			}
    			
    			telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-5+(i*5),y-680, 0);
	    			}
    			}
    			alamat_kores = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kores, x-265, y-692, 0);
    			kota_kores = tertanggung.getKota_rumah();
    			if (kota_kores != null)
    			{
	    			for (int i =0 ; i< kota_kores.length() ; i++)
	    			{
//	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_kores.charAt(i)), x-247+(i*5),y-718, 0);
	    			}
    			}
    			
    			kode_pos_kores = tertanggung.getKd_pos_rumah();
    			if (kode_pos_kores != null)
    			{
	    			if ( kode_pos_kores!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_kores.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kores.charAt(i)),x-96+(i*5),y-707, 0); // x-286+(i*10),y-160
		    			}
	    			}
    			}
    			
    			area_kores = tertanggung.getArea_code_rumah();
    			if (area_kores != null)
    			{
	    			for (int i =0 ; i< area_kores.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kores.charAt(i)), x-22+(i*5),y-707, 0);
	    			}
    			}
    			
    			telp_kores = tertanggung.getTelpon_rumah();
    			if (telp_kores != null)
    			{
	    			for (int i =0 ; i< telp_kores.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kores.charAt(i)), x-5+(i*5),y-707, 0);
	    			}
    			}
    			hp= tertanggung.getNo_hp();
    			if (hp != null)
    			{
    				for (int i =0 ; i< hp.length() ; i++)
    	    		{
    	    			over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(hp.charAt(i)), x-265+(i*5),y-719, 0);
    	    		}
    			}
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getEmail(), x-106, y-719, 0);
    			pekerjaan =  tertanggung.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-265, y-733, 0);
    			
    			uraian_kerja =tertanggung.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-265, y-746, 0);
    			jabatan = tertanggung.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,jabatan , x-104, y-746, 0);
    			
    			Integer nOne = reader.getNumberOfPages();
				
				logger.info("jumlah page:"+nOne);
				
				over.endText();
				
				over = stamp.getOverContent(2);
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
	    					
	    					over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-353,y-160+ (i*12), 0);	    					
	    					over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-110, y-160+ (i*12), 0);
	    					over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x-25,y-160+ (i*12), 0);
	    					over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x+100,y-160+ (i*12), 0);
	    				}
    			}
	    					    			
	    		//Penutup
	    		String nama_agen = dataagen.getMcl_first();	
	    		String kode_agen = dataagen.getMsag_id();
	    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x-2,y-561, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x-2,y-582, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-315,y-462, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-10,y-462, 0);/*
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
