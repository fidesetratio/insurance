/**
 * 
 */
package com.ekalife.elions.web.worksite;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentController;
import com.lowagie.text.pdf.PdfWriter;

public class InputJurnalController extends ParentController{

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map map = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String perusahaan=request.getParameter("perusahaan");
		String nominal = request.getParameter("nominal");
		String bank_descr = request.getParameter("bank_descr");  
		String bankId = request.getParameter("bankId");
		String giro = request.getParameter("giro");
		String periode = request.getParameter("periode");
		String mcl_id = request.getParameter("mcl_id");
		String no_pre = request.getParameter("no_pre");
		String position = request.getParameter("position");
		String bank_id = request.getParameter("bank_id");
		String tax_no = request.getParameter("tax_no");
		String nama_rek = request.getParameter("nama_rek");
		String no_rek = request.getParameter("no_rek");
		String nettParameter = request.getParameter("nett");
		String full_periode = request.getParameter("full_periode");
		String jenisNominalCd = request.getParameter("jenisNominalJurnal");
		String editNominalCd = request.getParameter("editNominalJurnal");
		String periodeCd = request.getParameter("periodeCd");
		String event = request.getParameter("event");
		String jenisBdn = null;
		String npwp = null;
		int positionInt = 0;
		Date nowDate2 = elionsManager.selectSysdate();
		List bankList = this.uwManager.selectBankForJurnal();
		List<HashMap<String, Object>> jenisBdnDanNpwp = this.uwManager.selectJenisBdnDanNpwp(mcl_id);
		
		List<DropDown> periodeList = this.uwManager.selectPeriodeList(mcl_id);
		if( jenisBdnDanNpwp != null && jenisBdnDanNpwp.size() > 0 )
		{
			if( jenisBdnDanNpwp.get(0).get("JENIS") != null){ jenisBdn = jenisBdnDanNpwp.get(0).get("JENIS").toString();}
			if( jenisBdnDanNpwp.get(0).get("NPWP") != null ){ npwp = jenisBdnDanNpwp.get(0).get("NPWP").toString(); }
		}
		List<HashMap<String, Object>> listMstDBank = this.uwManager.selectInfoDBankForJurnal(no_pre);
		List<HashMap<String, Object>> detailUpdateJurnal = this.uwManager.selectDetailUpdateJurnalDiskonPerusahaan(mcl_id);
		String showDetail = "false";
		if( detailUpdateJurnal != null && detailUpdateJurnal.size() > 0 ) showDetail = "true";
		for( int i = 0 ; i < listMstDBank.size() ; i ++ )
		{
			if(listMstDBank.get(i).get("GIRO") != null && !"".equals( listMstDBank.get(i).get("GIRO") ) ){
				giro = listMstDBank.get(i).get("GIRO").toString();
			}
		}
		if( bank_id != null && !"".equals(bank_id) ){bankId = bank_id;}
		if( position == null || position != null && "".equals( position ) ){ positionInt = 0; }
		else{ positionInt = LazyConverter.toInt(position); }
		
		List<DropDown> jenisNominalList = new ArrayList<DropDown>();
		
		jenisNominalList.add( new DropDown("1", "Kelebihan Bayar") );
		jenisNominalList.add( new DropDown("2", "Kekurangan Bayar") );
		map.put("showDetail", showDetail);
		map.put("detailUpdateJurnal", detailUpdateJurnal);
		map.put("periodeList", periodeList);
		map.put("jenisNominalList", jenisNominalList);
		map.put("bankList", bankList);
		map.put("perusahaan", perusahaan);
		map.put("nominal", nominal);
		map.put("bank_descr", bank_descr);
		map.put("bankId", bankId);
		map.put("giro", giro);
		map.put("periode", periode);
		map.put("mcl_id", mcl_id);
		map.put("no_pre", no_pre);
		map.put("position", position);
		map.put("positionInt", positionInt);
		map.put("jenisBdn", jenisBdn);
		map.put("npwp", npwp);
		map.put("tax_no", tax_no);
		map.put("listMstDBank", listMstDBank);
		map.put("nama_rek", nama_rek);
		map.put("no_rek", no_rek);
		map.put("full_periode", full_periode);
		map.put("jenisNominalJurnal", jenisNominalCd);
		map.put("editNominalJurnal", editNominalCd);
		map.put("periodeCd", periodeCd);
		String sukses = null;
		String periodeForFile = full_periode.substring(7, full_periode.length() );
		if(event!=null && event.equals("update")){
			try{
				BigDecimal nominalEditResult =  new BigDecimal(nominal);
				if( jenisNominalCd != null && jenisNominalCd.equals("1") ){//kelebihan bayar
					nominalEditResult = nominalEditResult.subtract( new BigDecimal( editNominalCd ) );
				}else if( jenisNominalCd != null && jenisNominalCd.equals("2") ){// kekurangan bayar
					nominalEditResult = nominalEditResult.add( new BigDecimal( editNominalCd ) );
				}
				uwManager.update_mst_diskon_perusahaan(mcl_id, periodeCd,  nominalEditResult, new BigDecimal(editNominalCd), jenisNominalCd, currentUser.getLus_id() );
				sukses = "sukses";
			}catch (Exception e) {
				logger.error("ERROR :", e);
				sukses = "gagal";
			// TODO: handle exception
			}
		}
	   if(request.getParameter("transfer")!=null){
		   Connection conn = null;
			try{
				conn = this.elionsManager.getUwDao().getDataSource().getConnection();
				Map infoDiskonPerusahaan = uwManager.selectInfoDiskonPerusahaan("1", mcl_id);
				
        		String outputDirClient = props.getProperty("pdf.dir.report")+"\\undertable\\client\\";
        		String outputDirLampiranClient = props.getProperty("pdf.dir.report")+"\\undertable\\";
        		// buka comment bawah utk test
//        		outputDirClient = "c:\\ebserver\\pdfind\\Report\\undertable\\client\\";
//        		outputDirLampiranClient = "c:\\ebserver\\pdfind\\Report\\undertable\\";
        		
                String newDicClient = outputDirClient + periodeForFile;
                File dirFileClient = new File( newDicClient );
                if(!dirFileClient.exists()) dirFileClient.mkdirs();
                
                List<File> attachmentsClient = new ArrayList<File>();
                File diskonFileClient;
                String diskonFilenamePrefixCorp = "pembayaran_pic_ws_"+ mcl_id + "_";
                String diskonFilenameCorp = diskonFilenamePrefixCorp + periodeForFile + ".pdf";
                Map<String, Comparable> reportaClientParams = new HashMap<String, Comparable>();
                String nowDate =  FormatDate.toIndonesian(elionsManager.selectSysdateSimple());
                DateFormat df 		= new SimpleDateFormat("yyyyMM");
                Date tempDate = elionsManager.selectSysdateSimple();
                tempDate.setMonth( tempDate.getMonth() - 1 );
                String periodeDate = df.format(tempDate);
                String content = null;
                String note = null;
                BigDecimal nett = uwManager.selectNettDiskonPerusahaan(new BigDecimal(nominal), periodeForFile, mcl_id, no_pre);
                BigDecimal pajak = uwManager.selectPajakDiskonPerusahaan(new BigDecimal(nominal), periodeForFile, mcl_id, no_pre);
                
                if( infoDiskonPerusahaan.get("REK_NO") == null ){
                	content = "Bersama ini kami informasikan bahwa pada tanggal "+nowDate 
            		+ " telah dilakukan pembayaran komisi "+infoDiskonPerusahaan.get("PERUSAHAAN") + " untuk produksi bulan "
            		+ periodeFormatDate(periodeDate) + " sebesar " + "Rp." + FormatString.formatCurrency( null, nett )
            		+ " Dengan detail sebagai berikut :";
                	note = "Pembayaran melalui Giro a/n " + infoDiskonPerusahaan.get("NAMA_LENGKAP");
                }else if( infoDiskonPerusahaan.get("REK_NO") != null){
                	content = "Bersama ini kami informasikan bahwa pada tanggal "+nowDate 
            		+ " telah dilakukan pembayaran komisi "+infoDiskonPerusahaan.get("PERUSAHAAN") + " untuk produksi bulan "
            		+ periodeFormatDate(periodeDate) + " sebesar " + "Rp." + FormatString.formatCurrency( null, nett )
            		+ " Dengan detail sebagai berikut :";
                	note = "Pembayaran ditujukan ke rekening no. " + infoDiskonPerusahaan.get("REK_NO") + " a/n " + infoDiskonPerusahaan.get("REK_NAMA");
                }
                reportaClientParams.put("namaPic", infoDiskonPerusahaan.get("NAMA_LENGKAP").toString());
                reportaClientParams.put("nominal", "Rp." +FormatString.formatCurrency( null, new BigDecimal(nominal) ));
                reportaClientParams.put("pajak", "Rp." +FormatString.formatCurrency( null, pajak ));
                reportaClientParams.put("nett", "Rp." +FormatString.formatCurrency( null, nett ));
                reportaClientParams.put("note", note );
                reportaClientParams.put("content", content);
                
                // parameter utk insert ke history ut ==========================================================================================================================================================
                Map<String, Object> paramsHistoryUt = new HashMap<String, Object>();
                paramsHistoryUt.put( "MCL_ID", mcl_id ); // primary key
                paramsHistoryUt.put( "JENIS", 1 ); // primary key
                paramsHistoryUt.put( "REG_SPAJ", "00000000000" );    // dummy utk primary key
                paramsHistoryUt.put( "TAHUN_KE", 0 );   // dummy utk primary key
                paramsHistoryUt.put( "PREMI_KE", 0 );   // dummy utk primary key
                
                String periode2 = uwManager.selectMaxPeriodeMstDiskonPerusahaan( paramsHistoryUt );
                if( periode2 != null )
                {
                	Integer maxNo = uwManager.selectMaxNoMstHistoryUT( paramsHistoryUt );
                	if( maxNo == null ) { maxNo = 1; }
                	else { maxNo = maxNo + 1; }
                	Date createDate = elionsManager.selectNowDate();
                	
                	paramsHistoryUt.put( "PERIODE", periode2 ); // primary key
                	paramsHistoryUt.put( "NO", maxNo ); // primary key
                	paramsHistoryUt.put( "LUS_ID", null );
                	paramsHistoryUt.put( "DESCR", "Telah ditransfer ke CR/CD komisi "+ perusahaan +" sebesar Rp."+ FormatString.formatCurrency( null, nett ) +" kepada "+ nama_rek +" pemilik rekening "+ no_rek +", "+ bank_descr +", no giro: " + giro + " " );
                	paramsHistoryUt.put( "CREATE_DATE", createDate );
                	paramsHistoryUt.put( "FILE_NAME", null );
                }	
			    // =====================================================================================================================================================================
			    
			    String periode_awal = full_periode.substring(0, 6);
			    String periode_akhir = full_periode.substring(7, full_periode.length() );
                Map<String, Object> params = new HashMap<String, Object>();
                params.put( "MCL_ID", mcl_id );
                params.put( "JENIS", 1 );
                params.put( "REG_SPAJ", "00000000000" );
                params.put( "TAHUN_KE", 0 );
                params.put( "PREMI_KE", 0 );
                params.put( "PERIODE_FROM", periode_awal );
                params.put( "PERIODE_TO", periode_akhir );

//                List<Map<String, Object>> historyList = uwManager.selectHistoryUT( params );
//			        
//                Integer tahun_periode_awal = LazyConverter.toInt(periode_awal.substring(0,4));
//                Integer tahun_periode_akhir = LazyConverter.toInt(periode_akhir.substring(0,4));
//                Integer bulan_periode_awal = LazyConverter.toInt(periode_awal.substring(4, periode_awal.length()));
//                Integer bulan_periode_akhir = LazyConverter.toInt(periode_akhir.substring(4,periode_akhir.length()));
//            	List fileLampiranClientAll = new ArrayList();
//            	String fileNameLampiran = "";
//            	if( !periode_awal.equals(periode_akhir) )
//            	{
//            		bulan_periode_awal = bulan_periode_awal + 1; 
//	                while( !periode_awal.equals(periode_akhir) ){
//	                	if( !tahun_periode_awal.equals(tahun_periode_akhir)){
//	                		if( !bulan_periode_awal.equals(12) ){// jika bulan nya january - november
//	                			if( bulan_periode_awal.toString().length() == 1 ){// jika bulan ny adalah january - september(1-9)
//	                				fileNameLampiran = "diskon_"+mcl_id+"_"+tahun_periode_awal+"0"+bulan_periode_awal;
//	                			}else{// jika bulan ny adalah oktober dan november(10 dan 11)
//	                				fileNameLampiran = "diskon_"+mcl_id+"_"+tahun_periode_awal+bulan_periode_awal;
//	                			}
//	                			fileLampiranClientAll.add(fileNameLampiran+".pdf");
//	                			bulan_periode_awal += 1 ;
//	                			if( bulan_periode_awal.toString().length() == 1 ){// jika bulan ny adalah january - september(1-9)
//	                				periode_awal = tahun_periode_awal + "0" + bulan_periode_awal;
//	                			}else{// jika bulan ny adalah oktober dan november(10 dan 11)
//	                				periode_awal = tahun_periode_awal.toString() + bulan_periode_awal.toString();
//	                			}
//	                		}else{ // jika bulan ny adalah desember
//	                			fileNameLampiran = "diskon_"+mcl_id+"_"+tahun_periode_awal+bulan_periode_awal;
//	                			fileLampiranClientAll.add(fileNameLampiran+".pdf");
//	                			tahun_periode_awal += 1;
//	                			bulan_periode_awal = 1;
//	                		}
//	                	}
//	                	
//	                	if( tahun_periode_awal.equals(tahun_periode_akhir)){
//                			if( bulan_periode_awal.toString().length() == 1 ){// jika bulan ny adalah january - september(1-9)
//                				fileNameLampiran = "diskon_"+mcl_id+"_"+tahun_periode_awal+"0"+bulan_periode_awal;
//                			}else{// jika bulan ny adalah oktober dan november(10 dan 11)
//                				fileNameLampiran = "diskon_"+mcl_id+"_"+tahun_periode_awal+bulan_periode_awal;
//                			}
//                			fileLampiranClientAll.add(fileNameLampiran+".pdf");
//                			bulan_periode_awal += 1 ;
//                			if( bulan_periode_awal.toString().length() == 1 ){// jika bulan ny adalah january - september(1-9)
//                				periode_awal = tahun_periode_awal + "0" +bulan_periode_awal;
//                			}else{// jika bulan ny adalah oktober dan november(10 dan 11)
//                				periode_awal = tahun_periode_awal.toString() + bulan_periode_awal.toString();
//                			}
//	                	}
//	                }
//            	}
//            	if( periode_awal.equals(periode_akhir) ){
//        			if( bulan_periode_awal.toString().length() == 1 ){// jika bulan ny adalah january - september(1-9)
//        				fileNameLampiran = "diskon_"+mcl_id+"_"+tahun_periode_awal+"0"+bulan_periode_awal;
//        			}else{// jika bulan ny adalah oktober dan november(10 dan 11)
//        				fileNameLampiran = "diskon_"+mcl_id+"_"+tahun_periode_awal+bulan_periode_awal;
//        			}
//        			fileLampiranClientAll.add(fileNameLampiran+".pdf");
//            	}
//            	List fileLampiranClientSelected = new ArrayList();
//            	for( int m = 0 ; m < historyList.size() ; m ++ ){
//            		if( historyList.get(m).get("FILE_NAME") != null ){
//            			for( int n = 0 ; n < fileLampiranClientAll.size() ; n ++ ){
//        					String compare_1 = historyList.get(m).get("FILE_NAME").toString().replace(";", "").trim();
//        					String compare_2 = fileLampiranClientAll.get(n).toString();
//            				if( compare_1.equals(compare_2)){
//            					fileLampiranClientSelected.add(fileLampiranClientAll.get(n));
//            				}
//            			}
//            		}
//            	}
            	
            	// fadly
//            	for( int j = 0 ; j < fileLampiranClientSelected.size() ; j++ ){
//            		Integer fileLength = fileLampiranClientSelected.get(j).toString().length();
//            		String periodeLampiran = fileLampiranClientSelected.get(j).toString().substring(fileLength - 10,fileLength -4);
//                    diskonFileClient = new File(outputDirLampiranClient + periodeLampiran + "\\" + fileLampiranClientSelected.get(j) );
//                    attachmentsClient.add( diskonFileClient );
//            	}
                
                //Req Weinie (14 Apr 2014) - untuk tgl periode, dari tanggal RK
                if(!Common.isEmpty(no_pre)){
                	periode = df.format(bacManager.selectTglRkFromTbank(no_pre)) ;
                }
                
			    // PROSES TRANSFER==========================================================================================================================================================
				String noPre = uwManager.allJournalProcess(mcl_id, periode, bankId, giro, paramsHistoryUt);
				// =================================================================================================================================================================
				String noPre2=bacManager.selectNoPreMstPerusahaan(mcl_id, periode);
				map.put("noPre","No Pre : "+ noPre2);
				
				
				// export ke pdf ==========================================================================================================================================================
                JasperUtils.exportReportToPdf(
                		props.getProperty("report.worksite.komisi_client") + ".jasper",
                        newDicClient,
                        diskonFilenameCorp,
                        reportaClientParams,
                        conn,
                        PdfWriter.AllowPrinting, null, null);
                diskonFileClient = new File(outputDirClient + periodeForFile + "\\" + diskonFilenamePrefixCorp + periodeForFile + ".pdf" );
                attachmentsClient.add( diskonFileClient );
                //============================================================================================================================================================================
                
                
                
                // email =================================================================================================================================================================================================================================
	              if( attachmentsClient.size() > 0 )
	              {
	            	String to;
	                Object emailClient = infoDiskonPerusahaan.get("EMAIL");
	                if( emailClient != null && !"".equals(emailClient)){
	                	to = emailClient.toString();
	                }else{
	                	to = "evie@sinarmasmsiglife.co.id";
	                }
	                
	                EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, nowDate2, null, 
							true,  props.getProperty("admin.ajsjava"), 
						    new String[]{to},
		                       new String[]{"evie@sinarmasmsiglife.co.id"},
		                        new String[]{"ryan@sinarmasmsiglife.co.id"},
		                        "Pembayaran Pengembalian diskon " + infoDiskonPerusahaan.get("PERUSAHAAN"),
		                        "Kepada : "+ infoDiskonPerusahaan.get("PERUSAHAAN") +
		                        "<br> INFORMASI : pada tanggal " + nowDate +" telah dilakukan pembayaran pengembalian diskon " + infoDiskonPerusahaan.get("PERUSAHAAN"),
		                                  attachmentsClient, null);
	         /*       
	               email.send(true,
	                        props.getProperty("admin.ajsjava"),
	                        new String[]{to},
	                       new String[]{"novie@sinarmasmsiglife.co.id"},
	                        new String[]{"ryan@sinarmasmsiglife.co.id"},
	                        "Pembayaran Pengembalian diskon " + infoDiskonPerusahaan.get("PERUSAHAAN"),
	                        "Kepada : "+ infoDiskonPerusahaan.get("PERUSAHAAN") +
	                        "<br> INFORMASI : pada tanggal " + nowDate +" telah dilakukan pembayaran pengembalian diskon " + infoDiskonPerusahaan.get("PERUSAHAAN"),
	                                  attachmentsClient);*/
	
	              }
              // =======================================================================================================================================================================================================================================
              
              sukses = "sukses";
			}catch (Exception e) {
				logger.error("ERROR :", e);
				sukses = "gagal";
				// TODO: handle exception
			}finally{
				this.elionsManager.getUwDao().closeConn(conn);
			}
		}
		map.put("sukses", sukses);
		return new ModelAndView("worksite/input_jurnal", "cmd", map);
	}
	
    public String periodeFormatDate( String periode ){
    	String tahun = periode.substring(0,4);
    	String bulan = periode.substring(4,6);
    	String bulanIndo;
    	String result;
    	if("01".equals(bulan)) bulanIndo = "januari";
    	else if("02".equals(bulan)) bulanIndo = "febuari";
    	else if("03".equals(bulan)) bulanIndo = "maret";
    	else if("04".equals(bulan)) bulanIndo = "april";
    	else if("05".equals(bulan)) bulanIndo = "mei";
    	else if("06".equals(bulan)) bulanIndo = "juni";
    	else if("07".equals(bulan)) bulanIndo = "juli";
    	else if("08".equals(bulan)) bulanIndo = "agustus";
    	else if("09".equals(bulan)) bulanIndo = "september";
    	else if("10".equals(bulan)) bulanIndo = "oktober";
    	else if("11".equals(bulan)) bulanIndo = "november";
    	else bulanIndo = "desember";
    	result = bulanIndo+ " " + tahun;
    	return  result;
    }

}
