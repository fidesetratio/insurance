package com.ekalife.elions.business.bac;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilInvestasi;
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

public class Link {
	protected final Log logger = LogFactory.getLog( getClass() );
	private static Link instance;
	
	public static Link getInstance()
	{
		if( instance == null )
		{
			instance = new Link();
		}
		return instance;
	}
	
	public Link()
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
		

    			//data pemegang
				over.showTextAligned(Element.ALIGN_LEFT, noBlanko , x-6, y-7, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMcl_first(), x-945, y-86, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_mother(), x-945, y-152, 0);
    			Integer jns_identitas = pemegang.getLside_id();
    			String jenis_identitas = pemegang.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-190, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-411, y-190, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-300, y-190, 0);
	    				}
    			}
	    		String no_identitas = pemegang.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-920+(i*34), y-225, 0);
	    			}
    			}
    			
    			Integer id_negara = pemegang.getLsne_id();
    			String nama_negara = pemegang.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-258, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-718, y-258, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-620, y-258, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-880+(i*25), y-290, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-675+(i*25), y-290, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-470+(i*30), y-290, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, pemegang.getMspe_place_birth(), x-220, y-285, 0);
    			
    			Integer jenis_kelamin = pemegang.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-745, y-322, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-322, 0);
	    				}
    			}
    			
    			String sts_marital = pemegang.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-398, y-322, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-220, y-322, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-45, y-322, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-45, y-322, 0);
	    					break;
	    			}
    			}				    			
    			
    			Integer id_agama = pemegang.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-362, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-740, y-362, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-570,y-362, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-398, y-362, 0);
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-220, y-362, 0);
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-392, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-740, y-392, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-570, y-392, 0);
	    						break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-400, y-392, 0);
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-220, y-392, 0);
	    						break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-45, y-392, 0);
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x +50, y-392, 0);
	    					break;
	    			}
    			}
    			
    			String alamat_rumah = pemegang.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-920, y-420, 0);
    			
    			String kode_pos_rumah = pemegang.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-920+(i*35),y-475, 0);
		    			}
	    			}
    			}
    			
    			String area_rumah = pemegang.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-620+(i*35),y-475, 0);
	    			}
    			}
    			
    				String telp_rumah = pemegang.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-450+(i*33),y-475, 0);
	    			}
    			}
    			
    			String pekerjaan =  pemegang.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-920, y-506, 0);
    			
    			String uraian_kerja =pemegang.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-920, y-538, 0);
    			
    			String jabatan = pemegang.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-250, y-510, 0);
    			
    			String tujuan = pemegang.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-578, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-600, y-578, 0);
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-284, y-574, 0);
	    							}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    						{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-610, 0);
	    						}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-600, y-610, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-285, y-607, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x-100, y-607, 0);
	    								}
    			}				    								
    			String penghasilan = pemegang.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.indexOf("<= RP. 10 JUTA") >= 0 )
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-641, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-670, 0);
	    						}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-600, y-645, 0);
	    						}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-600, y-670, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-282, y-645, 0);
		    							}else if (penghasilan.indexOf("> RP. 500 JUTA") >=0)
		    							{
		    			    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-284, y-672, 0);
		    							}
    			}
    			
    			String dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-705, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-742, y-708, 0);
	    					}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-565, y-705, 0);
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-370, y-705, 0);
	    							}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-221, y-708, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-40, y-705, 0);
				    				}
    			}
    			
    			Integer hubungan = pemegang.getLsre_id();
    			String hub = pemegang.getLsre_relation();
    			over.showTextAligned(Element.ALIGN_LEFT, hub, x-762, y-765, 0);
    			
    			//data tertanggung
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMcl_first(), x-944, y-878, 0);
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_mother(), x-944, y-950, 0);
    			jns_identitas = tertanggung.getLside_id();
    			jenis_identitas = tertanggung.getLside_name();
    			if (jns_identitas != null)
    			{
	    			if (jns_identitas.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-988, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-411, y-988, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, jenis_identitas, x-300, y-985, 0);
	    			}
    			}
    			no_identitas = tertanggung.getMspe_no_identity();
    			if (no_identitas != null)
    			{
		    		for (int i = 0 ; i < no_identitas.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(no_identitas.charAt(i)), x-920+(i*34), y-1020, 0);
	    			}
    			}
    			
    			id_negara = tertanggung.getLsne_id();
    			nama_negara = tertanggung.getLsne_note();
    			if (nama_negara != null)
    			{
	    			if (id_negara.intValue() == 1)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-1055, 0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-720, y-1055, 0);
	    				over.showTextAligned(Element.ALIGN_LEFT, nama_negara, x-620, y-1058, 0);
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tgl_lhr.charAt(i)), x-880+(i*25), y-1090, 0);
	    			}
	    			
	    			for (int i =0 ; i< bln_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bln_lhr.charAt(i)), x-675+(i*25), y-1090, 0);
	    			}
	    			
	    			for (int i =0 ; i< thn_lhr.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(thn_lhr.charAt(i)), x-470+(i*30), y-1090, 0);
	    			}
    			}
    			
    			over.showTextAligned(Element.ALIGN_LEFT, tertanggung.getMspe_place_birth(), x-220, y-1083, 0);
    			
    			jenis_kelamin = tertanggung.getMspe_sex();
    			if (jenis_kelamin !=null)
    			{
	    			if (jenis_kelamin.intValue() == 0)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-745, y-1120 ,0);
	    			}else{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-1120, 0);
	    				}
    			}
    			
    			sts_marital = tertanggung.getMspe_sts_mrt();
    			if (sts_marital != null)
    			{
	    			switch (Integer.parseInt(sts_marital))
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-400, y-1120, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-220, y-1120, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-45, y-1120, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-45, y-1120, 0);
	    						break;
	    			}
    			}				    			
    			
    			id_agama = tertanggung.getLsag_id();
    			if (id_agama != null)
    			{
	    			switch (id_agama)
	    			{
	    				case 1:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-1160, 0);
	    					break;
	    				case 2:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-745, y-1160, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-570,y-1160, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-398, y-1160, 0);
	    						break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-220, y-1160, 0);
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
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-1190, 0);
	    					break;
	    				case 3:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-740, y-1190, 0);
	    					break;
	    				case 4:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-570, y-1190, 0);
	    					break;
	    				case 5:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-400, y-1190, 0);
	    					break;
	    				case 6:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-220, y-1190, 0);
	    					break;
	    				default:
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-45, y-1190, 0);
	    					over.showTextAligned(Element.ALIGN_LEFT, pendidikan, x +50, y-1185, 0);
	    					break;
	    			}
    			}
    			
    			alamat_rumah = tertanggung.getAlamat_rumah();
    			over.showTextAligned(Element.ALIGN_LEFT, alamat_rumah, x-920, y-1220, 0);
    			
    			kode_pos_rumah = tertanggung.getKd_pos_rumah();
    			if (kode_pos_rumah != null)
    			{
	    			if ( kode_pos_rumah!=null)
	    			{
		    			for (int i =0 ; i< kode_pos_rumah.length() ; i++)
		    			{
		    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(kode_pos_rumah.charAt(i)), x-920+(i*35),y-1270, 0);
		    			}
	    			}
    			}
    			
    			area_rumah = tertanggung.getArea_code_rumah();
    			if (area_rumah != null)
    			{
	    			for (int i =0 ; i< area_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(area_rumah.charAt(i)), x-620+(i*35),y-1270, 0);
	    			}
    			}
    			
    			telp_rumah = tertanggung.getTelpon_rumah();
    			if (telp_rumah != null)
    			{
	    			for (int i =0 ; i< telp_rumah.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(telp_rumah.charAt(i)), x-450+(i*33),y-1270, 0);
	    			}
    			}
    			
    			pekerjaan =  tertanggung.getMkl_kerja();
    			over.showTextAligned(Element.ALIGN_LEFT, pekerjaan, x-920, y-1300, 0);
    			
    			uraian_kerja =tertanggung.getMpn_job_desc();
    			over.showTextAligned(Element.ALIGN_LEFT, uraian_kerja, x-920, y-1330, 0);
    			
    			jabatan = tertanggung.getKerjab();
    			over.showTextAligned(Element.ALIGN_LEFT,  jabatan , x-250, y-1300, 0);
    			
    			tujuan = tertanggung.getMkl_tujuan();
    			if (tujuan != null)
    			{
	    			if (tujuan.equalsIgnoreCase("Perlindungan Keluarga"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-1370, 0);
	    			}else if (tujuan.equalsIgnoreCase("Perlindungan Hari Tua"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-600, y-1370, 0);
	    					}else if (tujuan.equalsIgnoreCase("Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-282, y-1370, 0);
	    					}else if (tujuan.equalsIgnoreCase("Perlindungan Pendidikan"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-1400, 0);
	    					}else if (tujuan.equalsIgnoreCase("Perlindungan Kesehatan"))
	    							{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-600, y-1400, 0);
	    							}else{
	    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-285, y-1405, 0);
	    								over.showTextAligned(Element.ALIGN_LEFT, tujuan, x-100, y-1395, 0);
	    								}
    			}				    								
    			
    			penghasilan = tertanggung.getMkl_penghasilan();
    			if (penghasilan != null)
    			{
	    			if (penghasilan.equalsIgnoreCase(" <= RP. 10 JUTA"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-1435, 0);
	    			}else if (penghasilan.indexOf("> RP. 10 JUTA - RP. 50 JUTA") >= 0)
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-1470, 0);
	    							}else if (penghasilan.indexOf("> RP. 50 JUTA - RP. 100 JUTA") >= 0)
	    					{
    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-600, y-1435, 0);
    							}else if (penghasilan.indexOf("> RP. 100 JUTA - RP. 300 JUTA") >= 0)
		    					{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-600, y-1470, 0);
	    							}else if (penghasilan.indexOf("> RP. 300 JUTA - RP. 500 JUTA") >= 0)
		    						{
		    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-282, y-1435, 0);
		    								}else if (penghasilan.indexOf("> RP. 500 JUTA") >= 0)
		    							{
		    								over.showTextAligned(Element.ALIGN_LEFT, "X", x-282, y-1470, 0);
		    								}
    			}
    			
    			dana = pemegang.getMkl_pendanaan();
    			if (dana !=null)
    			{
	    			if (dana.equalsIgnoreCase("Gaji"))
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, "X", x-920, y-1505, 0);
	    			}else if (dana.equalsIgnoreCase("Hasil Usaha"))
	    				{
	    					over.showTextAligned(Element.ALIGN_LEFT, "X", x-750, y-1505, 0);
	    						}else if (dana.equalsIgnoreCase("Hasil Investasi"))
	    					{
	    						over.showTextAligned(Element.ALIGN_LEFT, "X", x-565, y-1505, 0);
	    						}else if (dana.equalsIgnoreCase("Warisan"))
	    						{
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-370, y-1505, 0);
	    							}else {
	    							over.showTextAligned(Element.ALIGN_LEFT, "X", x-218, y-1505, 0);
				    				over.showTextAligned(Element.ALIGN_LEFT, dana, x-40, y-1505, 0);
				    				}
    			}
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
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tanggal_beg_date.charAt(i)), x-902+(i*30), y-1915, 0);
	    			}
	    			
	    			for (int i =0 ; i< bulan_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(bulan_beg_date.charAt(i)), x-700+(i*30), y-1915, 0);
	    			}
	    			
	    			for (int i =0 ; i<tahun_beg_date.length() ; i++)
	    			{
	    				over.showTextAligned(Element.ALIGN_LEFT, String.valueOf(tahun_beg_date.charAt(i)), x-490+(i*30), y-1915, 0);
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
    				over.showTextAligned(Element.ALIGN_LEFT, FormatString.nomorSPAJ(no_spaj) , x-1105, y-3, 0);
    						    				
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
	    			
	    		String nama_agen = dataagen.getMcl_first();	
	    		String kode_agen = dataagen.getMsag_id();
	    		over.showTextAligned(Element.ALIGN_LEFT,  kode_agen , x-360,y-2330, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  nama_agen , x-360,y-2310, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  pemegang.getMcl_first() , x-850,y-2300, 0);
	    		over.showTextAligned(Element.ALIGN_LEFT,  tertanggung.getMcl_first() , x-1220,y-2300, 0);
				Date tgl_spaj = pemegang.getMspo_spaj_date();
				
				String cabang = dataagen.getLsrg_nama();
				over.showTextAligned(Element.ALIGN_LEFT,  cabang , x-360,y-2105, 0);
		
    			
		return over;
	}
	
}
