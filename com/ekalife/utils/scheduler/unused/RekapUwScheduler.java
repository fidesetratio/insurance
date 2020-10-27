package com.ekalife.utils.scheduler.unused;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.mail.MailException;

import com.ekalife.elions.web.refund.RefundConst;
import com.ekalife.elions.web.refund.vo.RekapInfoVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentScheduler;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @spring.bean Class ini untuk scheduling e-mail sender otomatis dept UW
 * 
 * @author Yusuf
 * @since Sep 23, 2008 (1:38:40 PM)
 */
public class RekapUwScheduler extends ParentScheduler{

	public void send(boolean isHtml, String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments) 
			throws MailException, MessagingException{
		//enable untuk debugging saja
		//to = new String[] {props.getProperty("admin.yusuf")};
		//cc = new String[] {props.getProperty("admin.yusuf")};
		//bcc = new String[] {props.getProperty("admin.yusuf")};
		
		this.email.send(isHtml, from, to, cc, bcc, subject, message, attachments);
	}
	
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i")) {
			try{
				
			  	Date nowDate = elionsManager.selectNowDate();
		    	Date tglBatalAwal = new Date( nowDate.getYear(), nowDate.getMonth(), nowDate.getDate() - 1, 8, 00,00 );
		    	Date tglBatalAkhir = new Date( nowDate.getYear(), nowDate.getMonth(), nowDate.getDate() - 1 , 23, 59,00 );
		    	Date tglCetakLaporan = new Date( nowDate.getYear(), nowDate.getMonth(), nowDate.getDate() , 1, 00,00 );
		    	
		        SimpleDateFormat tgl = new SimpleDateFormat("dd/MM/yyyy");
		        SimpleDateFormat jam = new SimpleDateFormat("HH:mm");
		        
		        String awalTglKirim = tgl.format( tglBatalAwal );
		        String awalJamKirim = jam.format( tglBatalAwal );
		        String akhirTglKirim = tgl.format( tglBatalAkhir );
		        String akhirJamKirim = jam.format( tglBatalAkhir );
		        String cetakLaporanTgl = tgl.format( tglCetakLaporan );
		        String cetakLaporanJam = jam.format( tglCetakLaporan );
		        
				Map<String, Object> params = genParamsRekapBatal( awalTglKirim, awalJamKirim, akhirTglKirim, akhirJamKirim, cetakLaporanTgl,  cetakLaporanJam);
				
				String outputFilename = "surat_rekap_pembatalan_otomatis_" + dateFormat.format( new Date() ) + ".pdf";
				String outputDir = props.getProperty("upload.dir.refund");
				String to = props.getProperty("email_rekap_otomatis.email_tujuan");
				String[] emailTo = to.split(";"); 
				
		        String content =
	                "Informasi : Telah dibuat rekap pembatalan otomatis dr tanggal "
		        	+ awalTglKirim + " (pukul"+awalJamKirim+")" +" s/d "+ akhirTglKirim + " (pukul"+akhirJamKirim+")";
		        
		        
		        List<Map<String, String>>  rekapInfoVO = rekapInfoVO( tglBatalAwal, tglBatalAkhir );
		        
				JasperUtils.exportReportToPdf(
						props.getProperty("report.refund.lamp_1_rekap_batal_refund") + ".jasper", 
						outputDir + "\\", outputFilename, params, rekapInfoVO, PdfWriter.AllowPrinting, null, null);
				
				List<File> attachments = new ArrayList<File>();
				File sourceFile = new File(outputDir+"\\"+outputFilename);
				attachments.add(sourceFile);
				
				email.send(true, 
						props.getProperty("admin.ajsjava"), 
						emailTo, 
						null,
						null, 
						"[INFO] Telah dibuat rekap pembatalan otomatis dr tanggal "+ awalTglKirim + " (pukul"+awalJamKirim+")" +" s/d "+ akhirTglKirim + " (pukul"+akhirJamKirim+")",
						content, 
						attachments);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			  }
		}
	}
	
    
    public List<Map<String, String>>   rekapInfoVO( Date tglBatalAwal, Date tglBatalAkhir )
    {
    	Map<String, Object> params = new HashMap<String, Object>();
    	List<Map<String, String>>   result = new ArrayList<Map<String,String>>();
    	String tglKirimDokFisik = "";
    	Map<String, String>  map;
    	String noPolis = "";

    	params.put( "tglBatalAwal", tglBatalAwal );
    	params.put( "tglBatalAkhir", tglBatalAkhir );
    	
    	List < RekapInfoVO > tempInfoForRekap = elionsManager.selectInfoForRekap(params);
    	if( tempInfoForRekap != null && tempInfoForRekap.size() > 0 )
    	{
    		for(int i = 0 ; i < tempInfoForRekap.size() ; i ++)
    		{
    			if( tempInfoForRekap.get(i).getTglKirimDokFisik() != null && !"".equals( tempInfoForRekap.get(i).getTglKirimDokFisik() ) )
    			{
    				tglKirimDokFisik = FormatDate.toIndonesian( tempInfoForRekap.get(i).getTglKirimDokFisik() );
    			}
    			else 
    			{
    				tglKirimDokFisik = "";
    			}
    			if( tempInfoForRekap.get( i ).getTindakanCd() != null && RefundConst.TINDAKAN_TIDAK_ADA.equals( tempInfoForRekap.get( i ).getTindakanCd() ) )
    			{
    				tglKirimDokFisik = "tidak ada surat";
    			}
    			if( tempInfoForRekap.get(i).getPolicyNo() != null && !"".equals( tempInfoForRekap.get(i).getPolicyNo() ) )
    			{
    				noPolis = tempInfoForRekap.get(i).getPolicyNo();
    			}
    			else 
    			{
    				noPolis = "";
    			}
    			if( tempInfoForRekap.get(i).getAlasan() != null && tempInfoForRekap.get(i).getAlasan().length() > 18 )
    			{
	    			String alasanPembatalanPolisStr = tempInfoForRekap.get(i).getAlasan().toLowerCase().substring( 0, 18 );
	    			if("pembatalan polis :".equals( alasanPembatalanPolisStr ) )
	    			{
	    				tempInfoForRekap.get(i).setAlasan( tempInfoForRekap.get(i).getAlasan().substring( 19, tempInfoForRekap.get(i).getAlasan().length()) );
	    			}
    			}
    			List<SetoranPremiDbVO> setoranPremiDb = elionsManager.selectSetoranPremiBySpaj( tempInfoForRekap.get(i).getSpajNo() );
    			String noPre = null;
    			String voucher = null;
    			if( setoranPremiDb == null )
    			{
    				noPre = "-";
        			voucher = "-";
    			}
    			else if( setoranPremiDb != null && setoranPremiDb.size() > 0 )
    			{
    				for( int j = 0 ; j < setoranPremiDb.size() ; j ++ )
    				{
    					if( setoranPremiDb.get(j).getNoPre() != null && !"".equals(setoranPremiDb.get(j).getNoPre()))
    					{
    						if( noPre == null)
        					{
        						noPre = setoranPremiDb.get(j).getNoPre();
        					}
        					else
        					{
        						noPre = noPre + "," + setoranPremiDb.get(j).getNoPre();
        					}
    					}
    					if( setoranPremiDb.get(j).getNoVoucher() != null && !"".equals(setoranPremiDb.get(j).getNoVoucher()))
    					{
	    					if( voucher == null )
	    					{
	    						voucher = setoranPremiDb.get(j).getNoVoucher();
	    					}
	    					else
	    					{
	    						voucher = voucher + "," + setoranPremiDb.get(j).getNoVoucher();
	    					}
    					}
    				
    				}
    			}
    			
    			String alasanBatal = tempInfoForRekap.get(i).getAlasan();
    			map = new HashMap<String, String>();
    			map.put( "no", i + 1 + "" );
    			map.put( "noSpaj", tempInfoForRekap.get(i).getSpajNo() );
    			map.put( "noPolis", noPolis );
    			map.put( "namaPemegangPolis", tempInfoForRekap.get(i).getNamaPp() );
    			map.put( "produk", tempInfoForRekap.get(i).getNamaProduk() );
    			map.put( "voucher", voucher );
    			map.put( "noPre", noPre );
    			map.put( "alasanBatal", alasanBatal );
    			map.put( "userUw", tempInfoForRekap.get(i).getUserUw() );
    			map.put( "tglKirim", tglKirimDokFisik );
    			result.add( map );
    		}
    		
    	}
    	
    	return result;
    }
    
    private Map<String, Object> genParamsRekapBatal( String awalTglKirim, String awalJamKirim, String akhirTglKirim, String akhirJamKirim, String cetakLaporanTgl, String cetakLaporanJam )
    {
        Map< String, Object > params = new HashMap< String, Object >();
        // default for report
        params.put( "awalTglKirim", awalTglKirim );
        params.put( "awalJamKirim", "( pukul " + awalJamKirim + " )" );
        params.put( "akhirTglKirim", akhirTglKirim );
        params.put( "akhirJamKirim", "( pukul " + akhirJamKirim + " )" );
        params.put( "tglCetakLaporan", cetakLaporanTgl );
        params.put( "jamCetakLaporan", "( pukul " + cetakLaporanJam + " )" );
        params.put( "format", "pdf" );
        params.put( "logoPath", "com/ekalife/elions/reports/refund/images/logo_ajs.gif" );

    	return params;
    }
	
}