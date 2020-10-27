package com.ekalife.elions.business.bac;

import java.util.Date;

import com.ekalife.elions.model.Agen;
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

public class mediplan {
	protected final Log logger = LogFactory.getLog( getClass() );
	private static mediplan instance;
	
	public static mediplan getInstance()
	{
		if( instance == null )
		{
			instance = new mediplan();
		}
		return instance;
	}
	
	public mediplan()
	{
	}
	
	public PdfContentByte createPdf( BacSpajParamVO paramVO )
	{
		PdfContentByte over = paramVO.getOver(); 
		Pemegang pemegang = paramVO.getPemegang(); 
		Tertanggung tertanggung = paramVO.getTertanggung(); 
		Integer x =paramVO.getX();
		Integer y= paramVO.getY();
		Integer f =paramVO.getF();
		Datausulan datausulan=paramVO.getDatausulan();
		Powersave data_pwr =paramVO.getData_pwr();
		Rekening_client data_rek= paramVO.getData_rek();
		Agen dataagen = paramVO.getDataagen();
		InvestasiUtama inv = paramVO.getInv();
		PdfReader reader =paramVO.getReader();
		String dir = paramVO.getDir();
		String file = paramVO.getFile();
		PdfStamper stamp =paramVO.getStamp();
		BaseFont bf = paramVO.getBf();
		String noBlanko= paramVO.getNoBlanko();
		

				int tambah = 60;
				
//				over.showTextAligned(Element.ALIGN_LEFT,noBlanko, x-14, y+3, 0);
    			//data pemegang
//				over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMkl_industri(), x-367, y-28, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-367, y-84, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_gelar(), x-50, y-84, 0);
    			String alamat_kantor = pemegang.getAlamat_kantor();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_kantor, x-367, y-40, 0);
    			String kode_pos_kantor = pemegang.getKd_pos_kantor();
    			if (kode_pos_kantor != null)
    			{
	    			if ( kode_pos_kantor!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_kantor.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_kantor.charAt(i)), x-364+(i*13),y-70, 0);
		    			}
	    			}
    			}
    			String area_kantor = pemegang.getArea_code_kantor();
    			if (area_kantor != null)
    			{
	    			for (int i =0 ; i< area_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_kantor.charAt(i)), x-270+(i*5),y-70, 0);
	    			}
    			}
    			
    			String telp_kantor = pemegang.getTelpon_kantor();
    			if (telp_kantor != null)
    			{
	    			for (int i =0 ; i< telp_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_kantor.charAt(i)), x-205+(i*5),y-70, 0);
	    			}
    			}
    			String kota_kantor = pemegang.getKota_kantor();
    			if (kota_kantor != null)
    			{
	    			for (int i =0 ; i< kota_kantor.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_kantor.charAt(i)), x-76+(i*5),y-70, 0);
	    			}
    			}
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-364, y-105, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-322, y-105, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-275, y-105, 0);
	    				}
    			}
    			String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-364+(i*13), y-122, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-343+(i*12), y-140, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-287+(i*12), y-140, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-231+(i*13), y-140, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-157, y-140, 0);
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-320, y-155, 0);//x-364, y-155
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-364, y-155, 0);
	    				}
    			}
    			
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-218, y-155, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-146, y-155, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-95, y-155, 0); //x-95, y-155
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-95, y-155, 0);
	    					break;
	    			}
    			}
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-364, y-171, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-321, y-171, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-260, y-171, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-197, y-171, 0);
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-146, y-171, 0);//x-146, y-171
	    					break;
	    			}
    			}
    			Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-364, y-189, 0);//x-364, y-189
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-300, y-189, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-260, y-190, 0);
	    				}
    			}
    			Integer id_pendidikan = pemegang.getLsed_id();
    			logger.info(id_pendidikan);
    			String pendidikan = pemegang.getLsed_name();
    			if (pendidikan != null)
    			{
	    			switch (id_pendidikan)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-275, y-206, 0);// SMU x-275, y-206
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-229, y-206, 0);//D1-D3
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-174, y-206, 0);//S1
	    						break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-133, y-206, 0);//s2
	    					break;
	    				case 8:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-364, y-206, 0);//SD
	    					break;
	    				case 9:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-319, y-206, 0);//SMP
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-94, y-206, 0);
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x-50, y-206, 0);
	    					break;
	    			}
    			}
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-367, y-221, 0);
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-364+(i*13),y-248, 0);
		    			}
	    			}
    			}
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-269+(i*13),y-248, 0);
	    			}
    			}
    			
    			String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-203+(i*13),y-248, 0);
	    			}
    			}
    			String kota_rumah = pemegang.getKota_rumah();
    			if (kota_rumah != null)
    			{
	    			for (int i =0 ; i< kota_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kota_rumah.charAt(i)), x-76+(i*5),y-248, 0);
	    			}
    			}
    			String hp= pemegang.getNo_hp();
    			if (hp != null)
    			{
    				for (int i =0 ; i< hp.length() ; i++)
    	    		{
    	    			over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(hp.charAt(i)), x-364+(i*13),y-264, 0);
    	    		}
    			}
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getEmail(), x-144, y-264, 0);
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-465, y-288, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-465, y-301, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "X",x-327, y-286, 0);//x-327, y-286
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-327, y-300, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X",  x-193, y-288, 0);
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-193, y-300, 0);
		    							}
    			}
//    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-367, y-40, 0);
//    			
//    			String nama_agama = pemegang.getLsag_name();
    			
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-367, y-319, 0);
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x-239, y-334, 0);
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-154, y-348, 0);
    			
    			String no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-367, y-359, 0);
    			
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-367, y-372, 0);
    			
    			String cabang_bank = data_rek.getMrc_cabang();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang_bank, x-154, y-359, 0);
    			
    			String kota_bank = data_rek.getMrc_kota();
    			over.showTextAligned(Element.ALIGN_LEFT,  kota_bank, x-154, y-372, 0);
    			
    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-367, y-405, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-367, y-419, 0);
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-18, y-405 ,0);//x-64, y-405
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-64, y-405, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-343+(i*12), y-435, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-287+(i*12), y-435, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-231+(i*13), y-435, 0);
	    			}
    			}
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_place_birth(), x-157, y-435, 0);
    			
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-367, y-450, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x-353+(i*12), y-524, 0);
	    			}
	    			
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x-297+(i*12), y-524, 0);
	    			}
	    			
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x-240+(i*13), y-524, 0);
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
	    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x-432,y-480, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x-432,y-490, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-225,y-364, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-70,y-364, 0);/*
				Date tgl_spaj = pemegang.getMspo_spaj_date();
		    	
//    			data datausulan
    			String nama_produk = datausulan.getLsdbs_name();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_produk , x-926, y-1680, 0);
    			
    			String kurs = datausulan.getLku_id();
    			String krs ="";
    			if (kurs != null)
    			{
	    			if (kurs.equalsIgnoreCase("01"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT,  "Rp " , x-330, y-1680, 0);
	    				krs="Rp ";
	    			}else{
	    				krs= "U$ ";
	    				over.showTextAligned(Element.ALIGN_LEFT,  "USD" , x-203, y-1680, 0);
	    			}
    			}

    			Integer lama_tanggung = datausulan.getMspr_ins_period();
    			over.showTextAligned(Element.ALIGN_LEFT,  Integer.toString(lama_tanggung) , x-605, y-1680, 0);

    			
    			Integer lama_bayar = datausulan.getMspo_pay_period();
    			over.showTextAligned(Element.ALIGN_LEFT,  Integer.toString(lama_bayar) , x-470, y-1680, 0);

    			Double premi = datausulan.getMspr_premium();
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-85, y-1680, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi.doubleValue())) , x-650, y-1945, 0);

    			Integer paymode = datausulan.getLscb_id();
    			if (paymode != null)
    			{
    				switch (paymode )
	    			{
	    				case 0: // sekaligus
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-933, y-1860, 0);
		    				break;
	    				case 1: //triwulanan
    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-420, y-1860, 0);
	    				break;
	    				case 2: // semesteran
    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-590, y-1860, 0);
    						break;
	    				case 3:
    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-760, y-1860, 0);
    					break;
	    				case 6:
							over.showTextAligned(Element.ALIGN_LEFT, "X", x-240, y-1860, 0);
	    					break;
	    			}
    			}
    			
    			Integer bentuk_pembayaran = datausulan.getMste_flag_cc();
    			if (bentuk_pembayaran !=null)
    			{
    				switch (bentuk_pembayaran)
	    			{
	    				case 0: // cekgiro/tunai
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-934, y-1887, 0);
		    				break;
	    				case 1: //triwulanan
    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-760, y-1885, 0);
    						break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-590, y-1885, 0);
	    				
	    					if (bentuk_pembayaran.intValue() == 2) // tabungan
	    					{
		    					over.showTextAligned(Element.ALIGN_LEFT, "TABUNGAN", x-480, y-1885, 0);
		    				}else{
    							over.showTextAligned(Element.ALIGN_LEFT, "PAYROLL", x-480, y-1885, 0);
		    				}
	    					break;
	    			}
    			}
    			
    			
    				
    			
    			String nama_bank = data_rek.getLsbp_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  nama_bank , x-555, y-2165, 0);
    			
    			String no_rek = data_rek.getMrc_no_ac();
    			over.showTextAligned(Element.ALIGN_LEFT,  no_rek , x-1102, y-2165, 0);
    			
    			String atas_nama = data_rek.getMrc_nama();
    			over.showTextAligned(Element.ALIGN_LEFT,  atas_nama , x-1102, y-2190, 0);
    			
    			String cabang_bank = data_rek.getMrc_cabang();
    			over.showTextAligned(Element.ALIGN_LEFT,  cabang_bank, x-555, y-2190, 0);
    			
    			String kota_bank = data_rek.getMrc_kota();
    			over.showTextAligned(Element.ALIGN_LEFT,  kota_bank, x-555, y-2210, 0);
    			
    			Double total_topup = new Double(0);
    			
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
    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-395, y-1965, 0);
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
    				over.showTextAligned(Element.ALIGN_LEFT,  "X", x-215, y-1965, 0);
    			}

    			total_topup = new Double(topup_tunggal.doubleValue() + topup_berkala.doubleValue());
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(total_topup.doubleValue())) , x-650, y-1965, 0);
    			
    			List datarider = datausulan.getDaftaRider();
    			Double total_premirider = new Double(0);
    			
    			for (int i = 0 ; i < datarider.size(); i++)
    			{
    				Datarider datard = (Datarider)datarider.get(i);
    				String nama_rider = datard.getLsdbs_name();
    				Integer pertanggungan_rider = datard.getMspr_ins_period();
    				Double premi_rider = datard.getMspr_premium();
    				over.showTextAligned(Element.ALIGN_LEFT,  nama_rider , x-920, y-1710 - (i*30), 0);
    				if (kurs != null)
	    			{
		    			if (kurs.equalsIgnoreCase("01"))
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT,  "Rp " , x-330, y-1710 -(i*30), 0);
		    			}else{
		    				over.showTextAligned(Element.ALIGN_LEFT,  "USD" , x-203, y-1710 - (i*30), 0);
		    			}
	    			}
    				over.showTextAligned(Element.ALIGN_LEFT,  Integer.toString(pertanggungan_rider) , x-605, y-1710 - (i*30), 0);
    				over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(premi_rider.doubleValue())) , x-85,y-1710 - (i*30), 0);
    				total_premirider = new Double(premi_rider.doubleValue() + total_premirider.doubleValue());
    			}
    		
    			Double total_pokok_rider = new Double(total_premirider.doubleValue() + premi.doubleValue());
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(total_pokok_rider.doubleValue()))  , x-85,y-1825, 0);
    			
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(total_premirider.doubleValue())) , x-650,y-1985, 0);
    			
    			Double total_pokok_rider_top = new Double( total_pokok_rider.doubleValue() + total_topup.doubleValue());
    			over.showTextAligned(Element.ALIGN_LEFT, FormatString.formatCurrency(krs, new BigDecimal(total_pokok_rider_top.doubleValue())) , x-650,y-2025, 0);
    				
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
//	    						over.showTextAligned(Element.ALIGN_LEFT,  Integer.toString(fixed)  , x-930,y-2275 , 0);
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
    				over.showTextAligned(Element.ALIGN_LEFT, FormatString.nomorSPAJ(no_spaj) , x-1105, y, 0);
    						    				
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
	    					
	    					over.showTextAligned(Element.ALIGN_LEFT,  nama_benef , x-1205,y- (i*30), 0);
	    					
	    					over.showTextAligned(Element.ALIGN_LEFT,  tanggal_lahir_benef , x-650, y- (i*30), 0);
	    					over.showTextAligned(Element.ALIGN_LEFT,  hub_benef , x-335,y- (i*30), 0);
	    					over.showTextAligned(Element.ALIGN_LEFT,  Double.toString(persen_benef) , x-25,y- (i*30), 0);
	    				}
    			}
	    			
	    		
				
				String cabang = dataagen.getLsrg_nama();
				over.showTextAligned(Element.ALIGN_LEFT,  cabang , x-360,y-2105, 0);
	*/	
    			
		return over;
	}
	
}
