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

public class Horison {
	protected final Log logger = LogFactory.getLog( getClass() );
	private static Horison instance;
	
	public static Horison getInstance()
	{
		if( instance == null )
		{
			instance = new Horison();
		}
		return instance;
	}
	
	public Horison()
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
		PdfStamper stamp =paramVO.getStamp();
		BaseFont bf = paramVO.getBf();
		PdfReader reader =paramVO.getReader();
		String noBlanko= paramVO.getNoBlanko();
	
		int tambah = 60;
		
		over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-367, y-107, 0);
		Integer jns_identitas = pemegang.getLside_id();
    	String jenis_identitas = pemegang.getLside_name();
    	if (jns_identitas != null)
    	{
	    	if (jns_identitas.intValue() == 1)
	    	{
	    		over.showTextAligned(Element.ALIGN_LEFT, "X", x-363, y-128, 0);
	    	}else{
	    		over.showTextAligned(Element.ALIGN_LEFT, "X",  x-320, y-128,0);//x-320, y-128
	    		over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-276, y-128, 0);
	    		}
    		}
    	String no_identitas = pemegang.getMspe_no_identity();
    	if (no_identitas != null)
    	{
    		for (int i =0 ; i< no_identitas.length() ; i++)
	    	{
    		over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-363+(i*13), y-147, 0);
	    	}
    	}
		
//    	over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-265, y-93, 0);    	
//    	over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_gelar(), x-45, y-100, 0);
    	
	    
    		
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
	    		over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-342+(i*10), y-168, 0);
	    	}
	    			
	    	for (int i =0 ; i< bln_lhr.length() ; i++)
	    	{
	    		over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-288+(i*12),  y-168, 0);
	    	}
	    			
	    	for (int i =0 ; i< thn_lhr.length() ; i++)
	    	{
	    		over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-228+(i*13),  y-168, 0);
	    	}
    	}
    		
    		over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-153,  y-168, 0);    				
    	Integer jenis_kelamin = pemegang.getMspe_sex();
    	if (jenis_kelamin !=null)
    	{
	    	if (jenis_kelamin.intValue() == 0)
	    	{
	    		over.showTextAligned(Element.ALIGN_LEFT, "X", x-318, y-188, 0);// pria 363
	    	}else{
	    		over.showTextAligned(Element.ALIGN_LEFT, "X", x-363, y-188, 0);// wanita
	    	}
    	}
    				
    	String sts_marital = pemegang.getMspe_sts_mrt();
    	if (sts_marital != null)
    	{
    		switch (Integer.parseInt(sts_marital))
	    	{
	    		case 1:
	    			over.showTextAligned(Element.ALIGN_LEFT, "X", x-218, y-188, 0);//single //218
	    			break;
	    		case 2:
	    			over.showTextAligned(Element.ALIGN_LEFT, "X", x-146, y-188, 0);//menikah
	    			break;
	    		case 3:
	    			over.showTextAligned(Element.ALIGN_LEFT, "X", x-94, y-188, 0);//janda
	    			break;
	    		case 4:
	    			over.showTextAligned(Element.ALIGN_LEFT, "X", x-94, y-188, 0);//duda
	    			break;
	    			}
    			}				    			
    			
    	Integer id_agama = pemegang.getLsag_id();
    	if (id_agama != null)
    	{
	    	switch (id_agama)
	    		{
	    			case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-364, y-208, 0);//islam
	    					break;
	    			case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-319, y-208, 0);//protestan
	    					break;
	    			case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-258, y-208, 0);//katolik
	    					break;
	    			case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-198, y-208, 0);//hindu
	    						break;
	    			case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-146, y-208, 0);//budha x-146, y-208
	    					break;
	    			}
    			}
    	 	
    	Integer id_negara = pemegang.getLsne_id();
    	String nama_negara = pemegang.getLsne_note();
    	if (nama_negara != null)
    	{
    		if (id_negara.intValue() == 1)
	    	{
	    		over.showTextAligned(Element.ALIGN_LEFT, "X",x-364, y-228, 0);//x-364, y-228
	    	}else{
	    		over.showTextAligned(Element.ALIGN_LEFT, "X", x-298, y-228, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-175, y-228, 0);
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
	    			over.showTextAligned(Element.ALIGN_LEFT, "S",x-93, y-251, 0);//sd
	    			break;
	    		case 9:
	    			over.showTextAligned(Element.ALIGN_LEFT, "R", x-318, y-251, 0);//smp
	    			break;
	    		case 1:
	    			over.showTextAligned(Element.ALIGN_LEFT, "X", x-364, y-251, 0);//SD
	    			break;
	    		case 3:
	    			over.showTextAligned(Element.ALIGN_LEFT, "E", x-273, y-251, 0);//smu
	    			break;
	    		case 4:
	    			over.showTextAligned(Element.ALIGN_LEFT, "D", x-173, y-251, 0);//s1  x-173, y-243
	    			break;
	    		case 5:
	    			over.showTextAligned(Element.ALIGN_LEFT, "X", x-226, y-251, 0);//d1/d3
	    			break; 
	    			default:
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-93, y-251, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x-47, y-251, 0);
	    			break;
	    			}
	    
    	}
    			
    	String alamat_rumah = pemegang.getAlamat_rumah();
    	over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-366, y-269, 0);
    			
    	String kode_pos_rumah = pemegang.getKd_pos_rumah();
    	if ( kode_pos_rumah!=null)
			{
    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
    			{
    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-363+(i*13),y-298, 0);
    			}
			}
    			
    	String area_rumah = pemegang.getArea_code_rumah();
    	if (area_rumah != null)
    	{
    		for(int i=0;i<area_rumah.length();i++){
    			over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-266+(i*13),y-298, 0);
      		}
    	}
    			
    	String telp_rumah = pemegang.getTelpon_rumah();
    	if (telp_rumah != null)
    	{
    		for(int i=0;i<telp_rumah.length();i++){
    			over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-201+(i*13),y-298, 0);
      		}
    	}	
    	String no_hp=tertanggung.getNo_hp();
    	if (no_hp != null)
    	{
	    	for (int i =0 ; i< no_hp.length() ; i++)
	    	{
	    		over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_hp.charAt(i)), x-363+(i*13),y-318, 0);
	    	}
    	}
    	String imel= tertanggung.getEmail();
    	over.showTextAligned(Element.ALIGN_LEFT,"imel", x-142, y-321, 0);
    	String penghasilan = pemegang.getMkl_penghasilan();
    	if (penghasilan != null)
    	{
	    if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    	{
	    	over.showTextAligned(Element.ALIGN_LEFT, "X", x-464, y-346, 0);
	    }else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    	{
	    	over.showTextAligned(Element.ALIGN_LEFT, "X", x-463, y-359, 0);
	    }else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    	{
    		over.showTextAligned(Element.ALIGN_LEFT, "X", x-326, y-346, 0);//x-326, y-346
	    }else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    {
	    	over.showTextAligned(Element.ALIGN_LEFT, "X", x-325, y-359, 0);
	    }else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    {
		    over.showTextAligned(Element.ALIGN_LEFT, "X", x-192, y-346, 0);
		}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    {
		    over.showTextAligned(Element.ALIGN_LEFT, "X", x-192, y-359, 0); //x-76, y-375
		    }
    	}
    		
//    	String pekerjaan =  pemegang.getMkl_kerja();
//    	over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-366, y-365, 0);
    			
    	String uraian_kerja =pemegang.getMpn_job_desc();
    	over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-366, y-378, 0);
    	Integer hubungan = pemegang.getLsre_id();
//    	String hub = pemegang.getLsre_relation();
//    	over.showTextAligned(Element.ALIGN_LEFT, hub, x-237, y-385, 0);
    	
    	String nama_bank = data_rek.getLsbp_nama();
    	over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-151, y-407, 0);
    	String cabang = dataagen.getLsrg_nama();
		over.showTextAligned(Element.ALIGN_LEFT, cabang , x-151,y-419, 0);
//		String kota= dataagen.getLsle_name();
//		over.showTextAligned(Element.ALIGN_LEFT, kota , x-151,y-423, 0);
		String no_rek = data_rek.getMrc_no_ac();
    	over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-366, y-407, 0);
    	String atas_nama = data_rek.getMrc_nama();
    	over.showTextAligned(Element.ALIGN_LEFT,  atas_nama ,x-366, y-419, 0);
    	String kurs = datausulan.getLku_id();
		String krs= "";	
		Double premi = datausulan.getMspr_premium();
		over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-305, y-484, 0);
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
				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x-302+(i*13), y-534, 0);
			}
			
			for (int i =0 ; i< bulan_beg_date.length() ; i++)
			{
				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x-248+(i*13), y-534, 0);
			}
			
			for (int i =0 ; i<tahun_beg_date.length() ; i++)
			{
				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x-190+(i*13), y-534, 0);
			}
		}
		
		Integer jumlah_benef = datausulan.getDaftabenef().size();
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

				String jBene= (Integer.toString(i+1));
//				over.showTextAligned(Element.ALIGN_LEFT,  jBene, x-365,y-678-(i*10), 0);		
				over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-460,y-658-(i*15), 0);		
				over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-250, y-(658+ (i*15)), 0);
				over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x-140,y-(658+ (i*15)), 0);
				over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x-20,y-(658+ (i*15)), 0);
			}
		}
		Integer nOne = reader.getNumberOfPages();
		
		logger.info("jumlah page:"+nOne);
		
		over.endText();
		
		over = stamp.getOverContent(2);
    	over.beginText();
    	over.setFontAndSize(bf, f); //set ukuran font
  
    	String nama_agen = dataagen.getMcl_first();	
	    String kode_agen = dataagen.getMsag_id();
	    over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x-428,y-672, 0);
	    over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x-428,y-684, 0);
//	    over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-170,y-677, 0);
	    over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-210,y-567, 0);
	    	
	   
    			
		return over;
	}
	
}
