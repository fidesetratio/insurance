package com.ekalife.elions.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.App;
import com.ekalife.elions.model.BlackList;
import com.ekalife.elions.model.Bmi;
import com.ekalife.elions.model.Company_ws;
import com.ekalife.elions.model.DanamasPrima;
import com.ekalife.elions.model.Hadiah;
import com.ekalife.elions.model.MstInbox;
import com.ekalife.elions.model.MstInboxHist;
import com.ekalife.elions.model.OutstandingBSM;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.WelcomeCallStatus;
import com.ekalife.elions.model.sms.Smsserver_out;
import com.ekalife.elions.process.CancelPolis;
import com.ekalife.elions.web.refund.RefundConst;
import com.ekalife.elions.web.refund.vo.RekapInfoVO;
import com.ekalife.elions.web.refund.vo.SetoranPremiDbVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentDao;
import com.ekalife.utils.scheduler.CurrencyScheduler;
import com.ekalife.utils.scheduler.IHSGScheduler;
import com.ekalife.utils.view.XLSCreatorBlacklist;
import com.ekalife.utils.view.XLSCreatorFrMallAssurance;
import com.ekalife.utils.view.XLSCreatorFrPasBp;
import com.ekalife.utils.view.XLSCreatorFreeSimasRumah;
import com.ekalife.utils.view.XLSCreatorPas;
import com.ekalife.utils.view.XLSCreatorReportDanamasPrima;
import com.ekalife.utils.view.XLSCreatorReportOutstandingBSM;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;
import net.sf.jasperreports.engine.JRException;

@SuppressWarnings("unchecked")
public class SchedulerDao extends ParentDao{
	protected final Log logger = LogFactory.getLog( getClass() );

	DateFormat dateFormat;
	private CancelPolis cancelPolis;
	public void setCancelPolis(CancelPolis cancelPolis) {this.cancelPolis = cancelPolis;}
	
	public SchedulerDao() {
		this.dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
	}
	
	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.scheduler.";
	}	

	public void insertMstSchedulerHist(String machine, String name, Date bdate, Date edate, String desc, String full_desc) throws DataAccessException{
		Map m = new HashMap();
		m.put("msh_machine", machine);
		m.put("msh_name", name);
		m.put("msh_bdate", bdate);
		m.put("msh_edate", edate);
		m.put("msh_desc", desc);
		m.put("msh_full_desc", full_desc);
		insert("insertMstSchedulerHist", m);
	}

	
	public void resetMstCounter(Integer msco_number, String lca_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("msco_number", msco_number);
		m.put("lca_id", lca_id);
		update("resetMstCounter", m);
	}

	/**
	 * Inner Class ini, untuk memproses secara paralel (threading) data hasil outputstream dan errorstream, agar runtime.exec tidak ngehang pas di execute  
	 * 
	 * @author yusuf
	 */
	class StreamGobbler extends Thread {
		InputStream is;

		String type;

		StreamGobbler(InputStream is, String type) {
			this.is = is;
			this.type = type;
		}

		public void run() {
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null)
					logger.info(type + ">" + line);
			} catch (IOException ioe) {
				logger.error(ioe);
			}finally{
				try {
					if(isr!=null){
						isr.close();
					}
					if(br!=null){
						br.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e);
				}
				
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
    	
    	List < RekapInfoVO > tempInfoForRekap = refundDao.selectInfoForRekap( params );
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
    			List<SetoranPremiDbVO> setoranPremiDb = refundDao.selectSetoranPremiBySpaj( tempInfoForRekap.get(i).getSpajNo() );
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
    
    /**
	 * Report Penerimaan dan Klaim Danamas Prima dikirim
	 */
	public void schedulerPenerimaanDanKlaimDanamasPrima(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err	= "";
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
		NumberFormat nf = NumberFormat.getInstance();
		
		Date nowDate = commonDao.selectSysdate();
		Date startDate = commonDao.selectSysdate();
		Date endDate = commonDao.selectSysdate();
		Date startDateAll = commonDao.selectSysdate();
		startDate.setDate(1);
		startDate.setMonth(startDate.getMonth()-1);
		//startDateAll.setMonth(startDateAll.getMonth()-1);
		endDate.setDate(1);
		endDate.setDate(endDate.getDate()-1);
		
		//scheduler untuk pas
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				String outputDir = props.getProperty("pdf.dir.report") + "\\danamas_prima\\";
				//String outputDir = "D:" + "\\danamas_prima\\";
				String outputFilename1 = "lap_penerimaan_danamas_prima_" + sdf.format(commonDao.selectSysdate()) + ".xls";
				String sheetFilename1 = "lap penerimaan danamas prima";
				String outputFilename2 = "lap_klaim_danamas_prima_" + sdf.format(commonDao.selectSysdate()) + ".xls";
				String sheetFilename2 = "lap klaim danamas prima";
				String outputFilename3 = "lap_manf_danamas_prima_" + sdf.format(commonDao.selectSysdate()) + ".xls";
				String sheetFilename3 = "lap manf danamas prima";
				
				List<DanamasPrima> reportPenerimaanDanamasPrimaList = uwDao.selectReportPenerimaanDanamasPrimaList();
				List<DanamasPrima> reportKlaimDanamasPrimaList = uwDao.selectReportKlaimDanamasPrimaList();
				List<DanamasPrima> reportManfDanamasPrimaList = uwDao.selectReportManfDanamasPrimaList();
				
				if(reportPenerimaanDanamasPrimaList.size() > 0 || reportKlaimDanamasPrimaList.size() > 0 || reportManfDanamasPrimaList.size() > 0){
					//Date sysdate = commonDao.selectSysdate();
						outputFilename1 = "lap_penerimaan_danamas_prima_" + sdf.format(commonDao.selectSysdate()) + ".xls";
						outputFilename2 = "lap_klaim_danamas_prima_" + sdf.format(commonDao.selectSysdate()) + ".xls";
						outputFilename3 = "lap_manf_danamas_prima_" + sdf.format(commonDao.selectSysdate()) + ".xls";
						
						XLSCreatorReportDanamasPrima xlsCreatorReportDanamasPrima = new XLSCreatorReportDanamasPrima();
						xlsCreatorReportDanamasPrima.buildExcelPenerimaanDanamasPrimaDocument(sheetFilename1, outputDir+"\\"+outputFilename1, reportPenerimaanDanamasPrimaList, df.format(startDateAll), df.format(nowDate));
						
						//XLSCreatorReportDanamasPrima xlsCreatorReportDanamasPrima = new XLSCreatorReportDanamasPrima();
						xlsCreatorReportDanamasPrima.buildExcelKlaimDanamasPrimaDocument(sheetFilename2, outputDir+"\\"+outputFilename2, reportKlaimDanamasPrimaList, df.format(startDateAll), df.format(nowDate));

						xlsCreatorReportDanamasPrima.buildExcelManfDanamasPrimaDocument(sheetFilename3, outputDir+"\\"+outputFilename3, reportManfDanamasPrimaList, df.format(startDateAll), df.format(nowDate));
						
						// email file sheet1.xls
						List<File> attachments = new ArrayList<File>();
						File sourceFile1 = new File(outputDir+"\\"+outputFilename1);
						
						// email file sheet1.xls
						//List<File> attachments = new ArrayList<File>();
						File sourceFile2 = new File(outputDir+"\\"+outputFilename2);

						File sourceFile3 = new File(outputDir+"\\"+outputFilename3);
						
						attachments.add(sourceFile1);
						attachments.add(sourceFile2);
						attachments.add(sourceFile3);
						
						EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
								"ajsjava@sinarmasmsiglife.co.id",
								new String[]{"julina.hasan@sinarmasmsiglife.co.id", "leonardo@sinarmasmsiglife.co.id"},
								//new String[]{"yusuf@sinarmasmsiglife.co.id"},
								null,
								null,
								"Data Penerimaan, Klaim, dan Manfaat/Bunga Danamas Prima Periode "+df2.format(startDate)+" s/d "+df2.format(endDate),//outputFilename,
								"Terlampir Data Penerimaan, Klaim, dan Manfaat/Bunga Danamas Prima Periode "+df2.format(startDate)+" s/d "+df2.format(endDate) +
								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
								attachments, null);
						
//						email.send(true, 
//								"ajsjava@sinarmasmsiglife.co.id",
//								new String[]{"julina.hasan@sinarmasmsiglife.co.id", "leonardo@sinarmasmsiglife.co.id"},
//								//new String[]{"yusuf@sinarmasmsiglife.co.id"},
//								null,
//								null,
//								"Data Penerimaan, Klaim, dan Manfaat/Bunga Danamas Prima Periode "+df2.format(startDate)+" s/d "+df2.format(endDate),//outputFilename,
//								"Terlampir Data Penerimaan, Klaim, dan Manfaat/Bunga Danamas Prima Periode "+df2.format(startDate)+" s/d "+df2.format(endDate) +
//								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//								attachments);
						
//						email.send(true, 
//								"test",
//								new String[]{"andy@sinarmasmsiglife.co.id", "yusuf@sinarmasmsiglife.co.id"},
//								null,
//								null,
//								"Data Penerimaan dan Klaim Danamas Prima Periode "+df2.format(startDate)+" s/d "+df2.format(endDate),//outputFilename,
//								"Terlampir Data Penerimaan dan Klaim Danamas Prima Periode "+df2.format(startDate)+" s/d "+df2.format(endDate) +
//								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//								attachments);
					
				}
				
	//			insertMstSchedulerHist(
	//				InetAddress.getLocalHost().getHostName(),
	//				"SCHEDULER PENERIMAAN DAN KLAIM DANAMAS PRIMA", bdate, new Date(), desc);
				
			} catch (Exception e) {
				desc = "ERROR";
				err=e.getLocalizedMessage();
				logger.error("ERROR :", e);
			}
			
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						msh_name, bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		//================================================================================================
		
	}
    
    /**
	 * Report Pas dikirim
	 */
	public void schedulerPas(int followUpPas, int followUpPasAll, int aksepPas, int aksepPasAll, int pesertaPas, int fire, int batalPas, int batalPasAll, String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("HH:mm");
		NumberFormat nf = NumberFormat.getInstance();
		
		Date nowDate = commonDao.selectSysdate();
		Date startDate = commonDao.selectSysdate();
		Date startDateAll = commonDao.selectSysdate();
		String isiTable = "";
		startDate.setDate(1);
		startDateAll.setDate(1);
		startDateAll.setMonth(7);//agustus
		
		//scheduler untuk pas
		if(followUpPas == 1){
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				String outputDir = props.getProperty("pdf.dir.report") + "\\pas\\";
				String outputFilename = "pas_follow_up_pembayaran" + sdf.format(commonDao.selectSysdate()) + ".xls";
				//String sheetFilename = "pas_follow_up_pembayaran" + sdf.format(commonDao.selectSysdate());
				String sheetFilename = "Follow Up PAS";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				
				//List<Map> reportPas = uwDao.selectReportPas();
				List<Pas> reportPasList = uwDao.selectReportPasList();
				
				List<List<Pas>> reportPasListx= new ArrayList<List<Pas>>(); 
				
				int lusIdNode = 0, idx = 0;
				if(reportPasList.size() > 0){
					lusIdNode = reportPasList.get(0).getLus_id();
				}
				for(int i = 0 ; i < reportPasList.size() ; i++){
					if(i == 0){//(i = 0)
						reportPasListx.add(new ArrayList<Pas>());
						reportPasListx.get(idx).add(reportPasList.get(i));
					}else{//(i = 1) sampai (i < reportPasList.size())
						if(lusIdNode == reportPasList.get(i).getLus_id()){// kalau lus_id sebelumnya sama dengan lus_id saat ini -> tambah ke list
							reportPasListx.get(idx).add(reportPasList.get(i));
						}else if(lusIdNode != reportPasList.get(i).getLus_id()){// kalau lus_id sebelumnya beda dengan lus_id saat ini -> buat list baru dan tambahkan
							idx++;
							lusIdNode = reportPasList.get(i).getLus_id();
							reportPasListx.add(new ArrayList<Pas>());
							reportPasListx.get(idx).add(reportPasList.get(i));
						}
					}
				}
				
				if(reportPasListx.size() > 0){
					Date sysdate = commonDao.selectSysdate();
					int BranchEmptyEmailFlag = 0;
					int BranchEmptyEmailCounter = 1;
					String BranchEmptyEmail = "Dear Team BAS,<br>Mohon bantuannya untuk data  alamat email dari admin cabang berikut :";
					for(int i = 0 ; i < reportPasListx.size() ; i++){
						List<String> reportPasEmailList = new ArrayList<String>();
						reportPasEmailList = uwDao.selectReportPasEmailList(reportPasListx.get(i).get(0).getLus_id());
						String[] emailList = new String[reportPasEmailList.size()];
						for(int j = 0 ; j < reportPasEmailList.size() ; j++){
							emailList[j] = reportPasEmailList.get(j);
						}
						outputFilename = "pas_follow_up_pembayaran" + sdf.format(commonDao.selectSysdate()) + "("+reportPasListx.get(i).get(0).getCabang()+")"+i+".xls";
						
						XLSCreatorPas xlsCreatorPas = new XLSCreatorPas();
						xlsCreatorPas.buildExcelFollowUpDocument(sheetFilename, outputDir+"\\"+outputFilename, reportPasListx.get(i), df.format(startDate), df.format(nowDate));
						SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						isiTable = "<tr><td>No.</td><td>No. HP</td><td>Nama Tertanggung</td><td>Nama Paket</td><td>Premi</td><td>Nomor Kartu</td><td>Tanggal Aktivasi</td></tr>";
						for(int x = 0 ; x < reportPasListx.get(i).size() ; x++){
							isiTable = isiTable + "<tr>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + (x+1);
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportPasListx.get(i).get(x).getMsp_mobile();
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportPasListx.get(i).get(x).getMsp_full_name();
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportPasListx.get(i).get(x).getNama_produk().replace("PAS ", "");
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportPasListx.get(i).get(x).getPremi();
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportPasListx.get(i).get(x).getNo_kartu();
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + sdf2.format(reportPasListx.get(i).get(x).getMsp_message_date());
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "</tr>";
						}
						
						// email file sheet1.xls
						List<File> attachments = new ArrayList<File>();
						File sourceFile = new File(outputDir+"\\"+outputFilename);
						
						attachments.add(sourceFile);
						
						if(emailList[0] == null){
							
							EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
									"info@sinarmasmsiglife.co.id",
									//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
									//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
									new String[]{"mariadf@sinarmasmsiglife.co.id", "siti_p@sinarmasmsiglife.co.id"}, null,//new String[]{"policy_service@sinarmasmsiglife.co.id"},
									new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
									"List data PAS yang sudah diaktivasi klien , tapi belum diinput di Cabang " +reportPasListx.get(i).get(0).getCabang(),//outputFilename,
									"Mohon dilakukan follow up kelengkapan dokumen dan pembayaran premi kepada agen penjual PAS ini, <br>" +
									"karena nasabah sudah melakukan aktivasi. Apabila dokumen sudah dilengkapi mohon agar segera di <br>" +
									"proses data-data nasabah tersebut.<br><br>" +
									"<b>DATA-DATA INI TIDAK TERKIRIM KE CABANG, DIKARENAKAN E-MAIL CABANG TIDAK TERCATAT DI SYSTEM.</b><br><br>"
									+ "<table border=1>"
									+ isiTable
									+ "</table>"
									+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
									attachments, null);
							
//							email.send(true, 
//									"info@sinarmasmsiglife.co.id",
//									//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//									//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//									new String[]{"mariadf@sinarmasmsiglife.co.id", "ani_chrys@sinarmasmsiglife.co.id", "siti_p@sinarmasmsiglife.co.id"}, null,//new String[]{"policy_service@sinarmasmsiglife.co.id"},
//									new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
//									"List data PAS yang sudah diaktivasi klien , tapi belum diinput di Cabang " +reportPasListx.get(i).get(0).getCabang(),//outputFilename,
//									"Mohon dilakukan follow up kelengkapan dokumen dan pembayaran premi kepada agen penjual PAS ini, <br>" +
//									"karena nasabah sudah melakukan aktivasi. Apabila dokumen sudah dilengkapi mohon agar segera di <br>" +
//									"proses data-data nasabah tersebut.<br><br>" +
//									"<b>DATA-DATA INI TIDAK TERKIRIM KE CABANG, DIKARENAKAN E-MAIL CABANG TIDAK TERCATAT DI SYSTEM.</b><br><br>"
//									+ "<table border=1>"
//									+ isiTable
//									+ "</table>"
//									+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//									attachments);
							BranchEmptyEmailFlag = 1;
							String lus_full_name = uwDao.selectLusFullName(new BigDecimal(reportPasListx.get(i).get(0).getLus_id()));
							BranchEmptyEmail = BranchEmptyEmail + "<br>" + BranchEmptyEmailCounter + ". " + "Nama : " + lus_full_name.toUpperCase() + "<br>&nbsp;&nbsp;&nbsp;&nbsp;Cabang : " + reportPasListx.get(i).get(0).getCabang();
							BranchEmptyEmailCounter++;
						}else{
							
							EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
									"info@sinarmasmsiglife.co.id",
									//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
									//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
									emailList, null,//new String[]{"ingrid@sinarmasmsiglife.co.id", "inge@sinarmasmsiglife.co.id"},//new String[]{"policy_service@sinarmasmsiglife.co.id"},
									new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
									"List data PAS yang sudah diaktivasi klien , tapi belum diinput di Cabang " +reportPasListx.get(i).get(0).getCabang(),//outputFilename,
									"Mohon dilakukan follow up kelengkapan dokumen dan pembayaran premi kepada agen penjual PAS ini, <br>" +
									"karena nasabah sudah melakukan aktivasi. Apabila dokumen sudah dilengkapi mohon agar segera di <br>" +
									"proses data- data nasabah tersebut.<br><br>"
									+ "<table border=1>"
									+ isiTable
									+ "</table>"
									+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
									attachments, null);
							
//							email.send(true, 
//									"info@sinarmasmsiglife.co.id",
//									//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//									//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//									emailList, null,//new String[]{"ingrid@sinarmasmsiglife.co.id", "inge@sinarmasmsiglife.co.id"},//new String[]{"policy_service@sinarmasmsiglife.co.id"},
//									new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
//									"List data PAS yang sudah diaktivasi klien , tapi belum diinput di Cabang " +reportPasListx.get(i).get(0).getCabang(),//outputFilename,
//									"Mohon dilakukan follow up kelengkapan dokumen dan pembayaran premi kepada agen penjual PAS ini, <br>" +
//									"karena nasabah sudah melakukan aktivasi. Apabila dokumen sudah dilengkapi mohon agar segera di <br>" +
//									"proses data- data nasabah tersebut.<br><br>"
//									+ "<table border=1>"
//									+ isiTable
//									+ "</table>"
//									+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//									attachments);
						}
					}
//					if(BranchEmptyEmailFlag == 1){
//						BranchEmptyEmail = BranchEmptyEmail + "<br>Tolong datanya dikirimkan ke Berto@sinarmasmsiglife.co.id dan Andy@sinarmasmsiglife.co.id<br>Atas bantuannya kami ucapkan terima kasih.";
//						email.send(true, 
//								"Team_Java",
//								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//								new String[]{"bas@sinarmasmsiglife.co.id"}, new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},//new String[]{"policy_service@sinarmasmsiglife.co.id"},
//								null,
//								"Update Email User Admin",//outputFilename,
//								BranchEmptyEmail, 
//								null);
//					}
					if(BranchEmptyEmailFlag == 1){
						BranchEmptyEmail = BranchEmptyEmail + "<br>Tolong datanya dikirimkan ke Berto@sinarmasmsiglife.co.id dan Andy@sinarmasmsiglife.co.id<br>Atas bantuannya kami ucapkan terima kasih.";
						
						EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
								"Team_Java",
								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
								new String[]{"andy@sinarmasmsiglife.co.id"}, null,//new String[]{"policy_service@sinarmasmsiglife.co.id"},
								null,
								"Update Email User Admin",//outputFilename,
								BranchEmptyEmail, 
								null, null);
						
//						email.send(true, 
//								"Team_Java",
//								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//								new String[]{"andy@sinarmasmsiglife.co.id"}, null,//new String[]{"policy_service@sinarmasmsiglife.co.id"},
//								null,
//								"Update Email User Admin",//outputFilename,
//								BranchEmptyEmail, 
//								null);
					}
				}
				
	//			insertMstSchedulerHist(
	//				InetAddress.getLocalHost().getHostName(),
	//				"SCHEDULER PAS FOLLOW UP PEMBAYARAN", bdate, new Date(), desc);
				
			} catch (Exception e) {
				desc = "ERROR";
				err=e.getLocalizedMessage();
				logger.error("ERROR :", e);
			}
			
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						msh_name, bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
		//================================================================================================
		
		//scheduler untuk pas all
		desc = "OK";
		isiTable = "";
		if(followUpPasAll == 1){
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				String outputDir = props.getProperty("pdf.dir.report") + "\\pas\\";
				String outputFilename = "pas_follow_up_pembayaran_all" + sdf.format(commonDao.selectSysdate()) + ".xls";
				//String sheetFilename = "pas_follow_up_pembayaran" + sdf.format(commonDao.selectSysdate());
				String sheetFilename = "Follow Up PAS ALL";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				
				//List<Map> reportPas = uwDao.selectReportPas();
				List<Pas> reportPasList = uwDao.selectReportPasList();
				
				List<List<Pas>> reportPasListx= new ArrayList<List<Pas>>(); 
				
				int lusIdNode = 0, idx = 0;
				if(reportPasList.size() > 0){
					lusIdNode = reportPasList.get(0).getLus_id();
				}
				for(int i = 0 ; i < reportPasList.size() ; i++){
					if(i == 0){//(i = 0)
						reportPasListx.add(new ArrayList<Pas>());
						reportPasListx.get(idx).add(reportPasList.get(i));
					}else{//(i = 1) sampai (i < reportPasList.size())
						if(lusIdNode == reportPasList.get(i).getLus_id()){// kalau lus_id sebelumnya sama dengan lus_id saat ini -> tambah ke list
							reportPasListx.get(idx).add(reportPasList.get(i));
						}else if(lusIdNode != reportPasList.get(i).getLus_id()){// kalau lus_id sebelumnya beda dengan lus_id saat ini -> buat list baru dan tambahkan
							idx++;
							lusIdNode = reportPasList.get(i).getLus_id();
							reportPasListx.add(new ArrayList<Pas>());
							reportPasListx.get(idx).add(reportPasList.get(i));
						}
					}
				}
				
				if(reportPasListx.size() > 0){
					Date sysdate = commonDao.selectSysdate();
						outputFilename = "pas_follow_up_pembayaran_all" + sdf.format(commonDao.selectSysdate()) + ".xls";
						
						XLSCreatorPas xlsCreatorPas = new XLSCreatorPas();
						xlsCreatorPas.buildExcelFollowUpAllDocument(sheetFilename, outputDir+"\\"+outputFilename, reportPasListx, df.format(startDateAll), df.format(nowDate));
						SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
						
						// email file sheet1.xls
						List<File> attachments = new ArrayList<File>();
						File sourceFile = new File(outputDir+"\\"+outputFilename);
						
						attachments.add(sourceFile);
						
						EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
								"info@sinarmasmsiglife.co.id",
								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
								new String[]{"siti_p@sinarmasmsiglife.co.id"},
								new String[]{"fouresta@sinarmasmsiglife.co.id", "mariadf@sinarmasmsiglife.co.id", 
								"asriwulan@sinarmasmsiglife.co.id"
								, "onna@sinarmasmsiglife.co.id", "andri@sinarmasmsiglife.co.id"
								, "callcentrepas@sinarmasmsiglife.co.id", "claim@sinarmasmsiglife.co.id"},
								new String[]{"andy@sinarmasmsiglife.co.id", "hendra@sinarmasmsiglife.co.id","yune@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id","martino@sinarmasmsiglife.co.id","huluk@sinarmasmsiglife.co.id"},
								"Terlampir data PAS yang telah diaktivasi oleh seluruh Cabang.",//outputFilename,
								"Mohon dilakukan follow up kelengkapan dokumen dan pembayaran premi kepada agen penjual PAS ini, karena nasabah sudah melakukan aktivasi. <br>" +
								"Data aktivasi PAS  per  Cabang  telah kami email ke masing-masing Cabang.<br>" +
								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
								attachments, null);
						
//						email.send(true, 
//								"info@sinarmasmsiglife.co.id",
//								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//								new String[]{"ani_chrys@sinarmasmsiglife.co.id", "siti_p@sinarmasmsiglife.co.id"},
//								new String[]{"fouresta@sinarmasmsiglife.co.id", "mariadf@sinarmasmsiglife.co.id", 
//								"ingrid@sinarmasmsiglife.co.id", "asriwulan@sinarmasmsiglife.co.id", "novie@sinarmasmsiglife.co.id"
//								, "onna@sinarmasmsiglife.co.id", "andri@sinarmasmsiglife.co.id"
//								, "callcentrepas@sinarmasmsiglife.co.id", "claim@sinarmasmsiglife.co.id"},
//								new String[]{"andy@sinarmasmsiglife.co.id", "hendra@sinarmasmsiglife.co.id","yune@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id","martino@sinarmasmsiglife.co.id","huluk@sinarmasmsiglife.co.id"},
//								"Terlampir data PAS yang telah diaktivasi oleh seluruh Cabang.",//outputFilename,
//								"Mohon dilakukan follow up kelengkapan dokumen dan pembayaran premi kepada agen penjual PAS ini, karena nasabah sudah melakukan aktivasi. <br>" +
//								"Data aktivasi PAS  per  Cabang  telah kami email ke masing-masing Cabang.<br>" +
//								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//								attachments);
						
//						email.send(true, 
//								"info@sinarmasmsiglife.co.id",
//								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//								new String[]{"ani_chrys@sinarmasmsiglife.co.id", "siti_p@sinarmasmsiglife.co.id"},
//								new String[]{"fouresta@sinarmasmsiglife.co.id", "mariadf@sinarmasmsiglife.co.id", 
//								"ingrid@sinarmasmsiglife.co.id", "asriwulan@sinarmasmsiglife.co.id", "novie@sinarmasmsiglife.co.id"
//								, "onna@sinarmasmsiglife.co.id", "andri@sinarmasmsiglife.co.id"
//								, "callcentrepas@sinarmasmsiglife.co.id", "claim@sinarmasmsiglife.co.id"},
//								new String[]{"andy@sinarmasmsiglife.co.id", "hendra@sinarmasmsiglife.co.id","yune@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id","martino@sinarmasmsiglife.co.id","huluk@sinarmasmsiglife.co.id"},
//								"Terlampir data PAS yang telah diaktivasi oleh seluruh Cabang.",//outputFilename,
//								"Mohon dilakukan follow up kelengkapan dokumen dan pembayaran premi kepada agen penjual PAS ini, karena nasabah sudah melakukan aktivasi. <br>" +
//								"Data aktivasi PAS  per  Cabang  telah kami email ke masing-masing Cabang.<br>" +
//								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//								attachments);
					
				}
				
	//			insertMstSchedulerHist(
	//				InetAddress.getLocalHost().getHostName(),
	//				"SCHEDULER PAS FOLLOW UP PEMBAYARAN", bdate, new Date(), desc);
				
			} catch (Exception e) {
				desc = "ERROR";
				err=e.getLocalizedMessage();
				logger.error("ERROR :", e);
			}
			
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						"SCHEDULER PAS FOLLOW UP PEMBAYARAN ALL", bdate, new Date(), desc, err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
		//================================================================================================
		
		//scheduler untuk akseptasi pas
		desc = "OK";
		isiTable = "";
		if(aksepPas == 1){
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				String outputDir = props.getProperty("pdf.dir.report") + "\\pas\\";
				String outputFilename = "pas_laporan_akseptasi" + sdf.format(commonDao.selectSysdate()) + ".xls";
				//String sheetFilename = "pas_follow_up_pembayaran" + sdf.format(commonDao.selectSysdate());
				String sheetFilename = "Laporan Akseptasi PAS";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				
				//List<Map> reportPas = uwDao.selectReportPas();
				List<Pas> reportAksepPasList = uwDao.selectReportAksepPasList();
				
				List<List<Pas>> reportAksepPasListx= new ArrayList<List<Pas>>(); 
				
				int lusIdNode = 0, idx = 0;
				if(reportAksepPasList.size() > 0){
					lusIdNode = reportAksepPasList.get(0).getLus_id();
				}
				for(int i = 0 ; i < reportAksepPasList.size() ; i++){
					if(i == 0){//(i = 0)
						reportAksepPasListx.add(new ArrayList<Pas>());
						reportAksepPasListx.get(idx).add(reportAksepPasList.get(i));
					}else{//(i = 1) sampai (i < reportPasList.size())
						if(lusIdNode == reportAksepPasList.get(i).getLus_id()){// kalau lus_id sebelumnya sama dengan lus_id saat ini -> tambah ke list
							reportAksepPasListx.get(idx).add(reportAksepPasList.get(i));
						}else if(lusIdNode != reportAksepPasList.get(i).getLus_id()){// kalau lus_id sebelumnya beda dengan lus_id saat ini -> buat list baru dan tambahkan
							idx++;
							lusIdNode = reportAksepPasList.get(i).getLus_id();
							reportAksepPasListx.add(new ArrayList<Pas>());
							reportAksepPasListx.get(idx).add(reportAksepPasList.get(i));
						}
					}
				}
				
				if(reportAksepPasListx.size() > 0){
					for(int i = 0 ; i < reportAksepPasListx.size() ; i++){
						List<String> reportPasAksepEmailList = new ArrayList<String>();
						reportPasAksepEmailList = uwDao.selectReportPasEmailList(reportAksepPasListx.get(i).get(0).getLus_id());
						String[] emailList = new String[reportPasAksepEmailList.size()];
						for(int j = 0 ; j < reportPasAksepEmailList.size() ; j++){
							emailList[j] = reportPasAksepEmailList.get(j);
						}
						outputFilename = "pas_laporan_akseptasi" + sdf.format(commonDao.selectSysdate()) + "("+reportAksepPasListx.get(i).get(0).getCabang()+")"+i+".xls";
						
						XLSCreatorPas xlsCreatorPas = new XLSCreatorPas();
						xlsCreatorPas.buildExcelAkseptasiDocument(sheetFilename, outputDir+"\\"+outputFilename, reportAksepPasListx.get(i), df.format(startDate), df.format(nowDate));
						SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
						isiTable = "<tr><td>No.</td><td>No. HP</td><td>Nama Pemegang</td><td>Nama Paket</td><td>Tanggal Akseptasi</td><td>Nama Agen</td></tr>";
						for(int x = 0 ; x < reportAksepPasListx.get(i).size() ; x++){
							isiTable = isiTable + "<tr>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + (x+1);
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportAksepPasListx.get(i).get(x).getMsp_mobile();
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportAksepPasListx.get(i).get(x).getMsp_pas_nama_pp();
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportAksepPasListx.get(i).get(x).getNama_produk().replace("PAS ", "");
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + sdf2.format(reportAksepPasListx.get(i).get(x).getMsp_pas_accept_date());
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportAksepPasListx.get(i).get(x).getNama_agent();
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "</tr>";
						}
						
						// email file sheet1.xls
						List<File> attachments = new ArrayList<File>();
						File sourceFile = new File(outputDir+"\\"+outputFilename);
						
						attachments.add(sourceFile);
						
						if(emailList[0] == null){
							EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
									"info@sinarmasmsiglife.co.id",
									//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
									//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
									new String[]{"mariadf@sinarmasmsiglife.co.id", "siti_p@sinarmasmsiglife.co.id"}, null,//new String[]{"ingrid@sinarmasmsiglife.co.id", "inge@sinarmasmsiglife.co.id"},//new String[]{"policy_service@sinarmasmsiglife.co.id"},
									new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
									"Report Akseptasi PAS",//outputFilename,
									"Berikut adalah Laporan Akseptasi PAS.<br><br>" +
									"<b>DATA-DATA INI TIDAK TERKIRIM KE CABANG, DIKARENAKAN E-MAIL CABANG TIDAK TERCATAT DI SYSTEM.</b><br><br>"
									+ "<table border=1>"
									+ isiTable
									+ "</table>"
									+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
									attachments, null);
							
//							email.send(true, 
//									"info@sinarmasmsiglife.co.id",
//									//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//									//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//									new String[]{"mariadf@sinarmasmsiglife.co.id", "ani_chrys@sinarmasmsiglife.co.id", "siti_p@sinarmasmsiglife.co.id"}, null,//new String[]{"ingrid@sinarmasmsiglife.co.id", "inge@sinarmasmsiglife.co.id"},//new String[]{"policy_service@sinarmasmsiglife.co.id"},
//									new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
//									"Report Akseptasi PAS",//outputFilename,
//									"Berikut adalah Laporan Akseptasi PAS.<br><br>" +
//									"<b>DATA-DATA INI TIDAK TERKIRIM KE CABANG, DIKARENAKAN E-MAIL CABANG TIDAK TERCATAT DI SYSTEM.</b><br><br>"
//									+ "<table border=1>"
//									+ isiTable
//									+ "</table>"
//									+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//									attachments);
						}else{
							EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
									"info@sinarmasmsiglife.co.id",
									//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
									//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
									new String[]{"inge@sinarmasmsiglife.co.id"}, null,//emailList, new String[]{"ingrid@sinarmasmsiglife.co.id", "inge@sinarmasmsiglife.co.id"},//new String[]{"policy_service@sinarmasmsiglife.co.id"},
									new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
									"Report Akseptasi PAS",//outputFilename,
									"Berikut adalah Laporan Akseptasi PAS.<br><br>"
									+ "<table border=1>"
									+ isiTable
									+ "</table>"
									+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
									attachments, null);
							
//							email.send(true, 
//									"info@sinarmasmsiglife.co.id",
//									//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//									//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//									new String[]{"ingrid@sinarmasmsiglife.co.id", "inge@sinarmasmsiglife.co.id"}, null,//emailList, new String[]{"ingrid@sinarmasmsiglife.co.id", "inge@sinarmasmsiglife.co.id"},//new String[]{"policy_service@sinarmasmsiglife.co.id"},
//									new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
//									"Report Akseptasi PAS",//outputFilename,
//									"Berikut adalah Laporan Akseptasi PAS.<br><br>"
//									+ "<table border=1>"
//									+ isiTable
//									+ "</table>"
//									+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//									attachments);
						}
						
						
					}
				}
				
			} catch (Exception e) {
				desc = "ERROR";
				err=e.getLocalizedMessage();
				logger.error("ERROR :", e);
			}
			
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						"SCHEDULER PAS LAPORAN AKSEPTASI", bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
//================================================================================================
		
		//scheduler untuk akseptasi pas all
		desc = "OK";
		isiTable = "";
		if(aksepPasAll == 1){
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				String outputDir = props.getProperty("pdf.dir.report") + "\\pas\\";
				String outputFilename = "pas_laporan_akseptasi_all" + sdf.format(commonDao.selectSysdate()) + ".xls";
				//String sheetFilename = "pas_follow_up_pembayaran" + sdf.format(commonDao.selectSysdate());
				String sheetFilename = "Laporan Akseptasi PAS ALL";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				
				//List<Map> reportPas = uwDao.selectReportPas();
				List<Pas> reportAksepPasList = uwDao.selectReportAksepPasList();
				
				List<List<Pas>> reportAksepPasListx= new ArrayList<List<Pas>>();
				int lusIdNode = 0, idx = 0;
				if(reportAksepPasList.size() > 0){
					lusIdNode = reportAksepPasList.get(0).getLus_id();
				}
				for(int i = 0 ; i < reportAksepPasList.size() ; i++){
					if(i == 0){//(i = 0)
						reportAksepPasListx.add(new ArrayList<Pas>());
						reportAksepPasListx.get(idx).add(reportAksepPasList.get(i));
					}else{//(i = 1) sampai (i < reportPasList.size())
						if(lusIdNode == reportAksepPasList.get(i).getLus_id()){// kalau lus_id sebelumnya sama dengan lus_id saat ini -> tambah ke list
							reportAksepPasListx.get(idx).add(reportAksepPasList.get(i));
						}else if(lusIdNode != reportAksepPasList.get(i).getLus_id()){// kalau lus_id sebelumnya beda dengan lus_id saat ini -> buat list baru dan tambahkan
							idx++;
							lusIdNode = reportAksepPasList.get(i).getLus_id();
							reportAksepPasListx.add(new ArrayList<Pas>());
							reportAksepPasListx.get(idx).add(reportAksepPasList.get(i));
						}
					}
				}
				
				if(reportAksepPasListx.size() > 0){
						outputFilename = "pas_laporan_akseptasi_all" + sdf.format(commonDao.selectSysdate()) + ".xls";
						
						XLSCreatorPas xlsCreatorPas = new XLSCreatorPas();
						xlsCreatorPas.buildExcelAkseptasiAllDocument(sheetFilename, outputDir+"\\"+outputFilename, reportAksepPasListx, df.format(startDateAll), df.format(nowDate));
						SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
						
						// email file sheet1.xls
						List<File> attachments = new ArrayList<File>();
						File sourceFile = new File(outputDir+"\\"+outputFilename);
						
						attachments.add(sourceFile);
						
						EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
								"info@sinarmasmsiglife.co.id",
								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
								new String[]{"hayatin@sinarmasmsiglife.co.id", "fouresta@sinarmasmsiglife.co.id", 
										"ingrid@sinarmasmsiglife.co.id", "asriwulan@sinarmasmsiglife.co.id", "inge@sinarmasmsiglife.co.id"}
								, null,
								new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
								"Report Akseptasi PAS ALL",//outputFilename,
								"Berikut adalah Laporan Akseptasi PAS ALL.<br>"
								+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
								attachments, null);
						
//						email.send(true, 
//								"info@sinarmasmsiglife.co.id",
//								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//								new String[]{"hayatin@sinarmasmsiglife.co.id", "fouresta@sinarmasmsiglife.co.id", "hadi@sinarmasmsiglife.co.id", 
//										"ingrid@sinarmasmsiglife.co.id", "asriwulan@sinarmasmsiglife.co.id", "inge@sinarmasmsiglife.co.id"}
//								, null,
//								new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
//								"Report Akseptasi PAS ALL",//outputFilename,
//								"Berikut adalah Laporan Akseptasi PAS ALL.<br>"
//								+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//								attachments);
						
				}
				
			} catch (Exception e) {
				desc = "ERROR";
				logger.error("ERROR :", e);
				err=e.getLocalizedMessage();
			}
			
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						"SCHEDULER PAS LAPORAN AKSEPTASI ALL", bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
		
		//================================================================================================
		
		//scheduler untuk peserta pas
		desc = "OK";
		isiTable = "";
		if(pesertaPas == 1){
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				String outputDir = props.getProperty("pdf.dir.report") + "\\pas\\";
				String outputFilename = "pas_data_peserta" + sdf.format(commonDao.selectSysdate()) + ".xls";
				//String sheetFilename = "pas_follow_up_pembayaran" + sdf.format(commonDao.selectSysdate());
				String sheetFilename = "Data Peserta Pas";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				
				//List<Map> reportPas = uwDao.selectReportPas();
				List<Pas> reportPasList = uwDao.selectReportPesertaPasList();
				
				if(reportPasList.size() > 0){
					Date sysdate = commonDao.selectSysdate();
						outputFilename = "pas_data_peserta" + sdf.format(commonDao.selectSysdate()) + ".xls";
						
						XLSCreatorPas xlsCreatorPas = new XLSCreatorPas();
						xlsCreatorPas.buildExcelDataPesertaPasDocument(sheetFilename, outputDir+"\\"+outputFilename, reportPasList, df.format(startDateAll), df.format(nowDate));
						SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
						
						// email file sheet1.xls
						List<File> attachments = new ArrayList<File>();
						File sourceFile = new File(outputDir+"\\"+outputFilename);
						
						attachments.add(sourceFile);
						
						EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
								"info@sinarmasmsiglife.co.id", 
//								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},					
								new String[]{"kpr@sinarmasmsiglife.co.id", "onna@sinarmasmsiglife.co.id", "andri@sinarmasmsiglife.co.id"
								, "callcentrepas@sinarmasmsiglife.co.id", "sylvia@sinarmasmsiglife.co.id"}
								, null,
								new String[]{"andy@sinarmasmsiglife.co.id"},
								"Report Data Peserta PAS",//outputFilename,
								"Berikut adalah Laporan Data Peserta PAS." +
								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
								attachments, null);
						
//						email.send(true, 
//								"info@sinarmasmsiglife.co.id",
//								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//								new String[]{"kpr@sinarmasmsiglife.co.id", "onna@sinarmasmsiglife.co.id", "andri@sinarmasmsiglife.co.id"
//								, "callcentrepas@sinarmasmsiglife.co.id", "sylvia@sinarmasmsiglife.co.id"}
//								, null,
//								new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
//								"Report Data Peserta PAS",//outputFilename,
//								"Berikut adalah Laporan Data Peserta PAS." +
//								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//								attachments);
					
				}
				
	//			insertMstSchedulerHist(
	//				InetAddress.getLocalHost().getHostName(),
	//				"SCHEDULER PAS FOLLOW UP PEMBAYARAN", bdate, new Date(), desc);
				
			} catch (Exception e) {
				desc = "ERROR";
				err=e.getLocalizedMessage();
				logger.error("ERROR :", e);
			}
			
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						"SCHEDULER PAS DATA PESERTA", bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
		//================================================================================================
		
		//scheduler untuk fire
		desc = "OK";
		if(fire == 1){
			try {
				
				List<File> attachments = new ArrayList<File>();
				
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				String outputDir = props.getProperty("pdf.dir.report") + "\\pas\\";
				String outputFilename = "pas" + sdf.format(commonDao.selectSysdate()) + ".xls";
				//String sheetFilename = "pas" + sdf.format(commonDao.selectSysdate());
				String sheetFilename = "sheet1";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				
				//List<Map> reportFire = uwDao.selectReportFire();
				List<Pas> reportFireList = uwDao.selectReportFireList();
				
				// generate & save outputFilename
	//			JasperUtils.exportReportToXls(props.getProperty("report.fire") + ".jasper", 
	//					outputDir, outputFilename, params, reportPas, null);
				//==========================================
				
				// generate & save outputFilenameSheet
	//			JasperUtils.exportReportToXls(props.getProperty("report.fire") + ".jasper", 
	//					outputDir, outputFilenameSheet, params, reportPas, null);
				//==========================================
				
				if(reportFireList.size() > 0){
					XLSCreatorFreeSimasRumah xlsCreatorFreeSimasRumah = new XLSCreatorFreeSimasRumah();
					xlsCreatorFreeSimasRumah.buildExcelDocument(sheetFilename, outputDir+"\\"+outputFilename, reportFireList);
					
					// email file sheet1.xls
					File sourceFile = new File(outputDir+"\\"+outputFilename);
					
					attachments.add(sourceFile);
					
					EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
							"info@sinarmasmsiglife.co.id",
							//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
							//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
							new String[]{"simasrumahfree@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id",
											"fouresta@sinarmasmsiglife.co.id", 
											"inge@sinarmasmsiglife.co.id", 
											"apriyani@sinarmasmsiglife.co.id", 
											"sutini@sinarmasmsiglife.co.id",	
											"ingrid@sinarmasmsiglife.co.id", 
											"ningrum@sinarmasmsiglife.co.id", 
											"asriwulan@sinarmasmsiglife.co.id"},
							new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
							"Free Simas Rumah AJS",//outputFilename,
							"Berikut adalah Laporan Free Simas Rumah AJS."
							+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
							attachments, null);
					
//					email.send(true, 
//							"info@sinarmasmsiglife.co.id",
//							//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//							//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//							new String[]{"simasrumahfree@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id",
//											"fouresta@sinarmasmsiglife.co.id", 
//											"inge@sinarmasmsiglife.co.id", 
//											"apriyani@sinarmasmsiglife.co.id", 
//											"sutini@sinarmasmsiglife.co.id",	
//											"ingrid@sinarmasmsiglife.co.id", 
//											"ningrum@sinarmasmsiglife.co.id", 
//											"asriwulan@sinarmasmsiglife.co.id"},
//							new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
//							"Free Simas Rumah AJS",//outputFilename,
//							"Berikut adalah Laporan Free Simas Rumah AJS."
//							+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//							attachments);
					
					for(int i = 0 ; i < reportFireList.size() ; i++){
						Pas pas = reportFireList.get(i);
						if(pas.getMsp_fire_export_flag() == 0){
							pas.setMsp_fire_export_flag(1);
						}else if(pas.getMsp_fire_export_flag() == 2){
							pas.setMsp_fire_export_flag(3);
						}
						pas.setMsp_fire_export_date(commonDao.selectSysdate());
						uwDao.updatePas(pas);
					}
					
					//FileUtil.copyfile(outputDir+"\\"+outputFilenameSheet, outputDir+"\\"+outputFilename);
					// delete file sheet1.xls
					//FileUtil.deleteFile(outputDir, outputFilenameSheet, null);
				}
				
	//			insertMstSchedulerHist(
	//				InetAddress.getLocalHost().getHostName(),
	//				"SCHEDULER PAS SIMAS RUMAH FREE", bdate, new Date(), desc);
				
			} catch (Exception e) {
				desc = "ERROR";
				err=e.getLocalizedMessage();
				logger.error("ERROR :", e);
			}
			
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						"SCHEDULER PAS SIMAS RUMAH FREE", bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
		
		//scheduler untuk batal pas
		List<Pas> reportBatalPasList = new ArrayList<Pas>();
		if(batalPas == 1){
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				String outputDir = props.getProperty("pdf.dir.report") + "\\pas\\";
				String outputFilename = "pas_batal" + sdf.format(commonDao.selectSysdate()) + ".xls";
				//String sheetFilename = "pas_follow_up_pembayaran" + sdf.format(commonDao.selectSysdate());
				String sheetFilename = "Batal PAS";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				
				//List<Map> reportPas = uwDao.selectReportPas();
				reportBatalPasList = uwDao.selectReportBatalPasList();
				
				List<List<Pas>> reportBatalPasListx= new ArrayList<List<Pas>>(); 
				
				int lusIdNode = 0, idx = 0;
				if(reportBatalPasList.size() > 0){
					lusIdNode = reportBatalPasList.get(0).getLus_id();
				}
				for(int i = 0 ; i < reportBatalPasList.size() ; i++){
					Pas p = new Pas();
					p.setMsp_id(reportBatalPasList.get(i).getMsp_id());
					p.setLspd_id(95);
					p.setLus_id_uw_batal(0);
					p.setMsp_ket_batal("PENGAKTIFANNYA SUDAH 3 BULAN KE ATAS DAN BELUM DIINPUT OLEH ADMIN");
					p.setMsp_tgl_batal(commonDao.selectSysdate());
					//p.setMsp_fire_beg_date(elionsManager.selectSysdate());
					//p.setMsp_fire_end_date(end_date);
					uwDao.updatePas(p);
					uwDao.updateKartuPas1(p.getNo_kartu(), 2, null, null);
					if(i == 0){//(i = 0)
						reportBatalPasListx.add(new ArrayList<Pas>());
						reportBatalPasListx.get(idx).add(reportBatalPasList.get(i));
					}else{//(i = 1) sampai (i < reportPasList.size())
						if(lusIdNode == reportBatalPasList.get(i).getLus_id()){// kalau lus_id sebelumnya sama dengan lus_id saat ini -> tambah ke list
							reportBatalPasListx.get(idx).add(reportBatalPasList.get(i));
						}else if(lusIdNode != reportBatalPasList.get(i).getLus_id()){// kalau lus_id sebelumnya beda dengan lus_id saat ini -> buat list baru dan tambahkan
							idx++;
							lusIdNode = reportBatalPasList.get(i).getLus_id();
							reportBatalPasListx.add(new ArrayList<Pas>());
							reportBatalPasListx.get(idx).add(reportBatalPasList.get(i));
						}
					}
				}
				
				if(reportBatalPasListx.size() > 0){
					//String BranchEmptyEmail = "Dear Team BAS,<br>Mohon bantuannya untuk data  alamat email dari admin cabang berikut :";
					for(int i = 0 ; i < reportBatalPasListx.size() ; i++){
						List<String> reportPasEmailList = new ArrayList<String>();
						reportPasEmailList = uwDao.selectReportPasEmailList(reportBatalPasListx.get(i).get(0).getLus_id());
						String[] emailList = new String[reportPasEmailList.size()];
						for(int j = 0 ; j < reportPasEmailList.size() ; j++){
							emailList[j] = reportPasEmailList.get(j);
						}
						outputFilename = "pas_batal" + sdf.format(commonDao.selectSysdate()) + "("+reportBatalPasListx.get(i).get(0).getCabang()+")"+i+".xls";
						
						XLSCreatorPas xlsCreatorPas = new XLSCreatorPas();
						xlsCreatorPas.buildExcelBatalDocument(sheetFilename, outputDir+"\\"+outputFilename, reportBatalPasListx.get(i), df.format(startDate), df.format(nowDate));
						SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						isiTable = "<tr><td>No.</td><td>No. HP</td><td>Nama Tertanggung</td><td>Nama Paket</td><td>Premi</td><td>Nomor Kartu</td><td>Tanggal Aktivasi</td></tr>";
						for(int x = 0 ; x < reportBatalPasListx.get(i).size() ; x++){
							isiTable = isiTable + "<tr>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + (x+1);
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportBatalPasListx.get(i).get(x).getMsp_mobile();
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportBatalPasListx.get(i).get(x).getMsp_full_name();
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportBatalPasListx.get(i).get(x).getNama_produk().replace("PAS ", "");
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportBatalPasListx.get(i).get(x).getPremi();
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + reportBatalPasListx.get(i).get(x).getNo_kartu();
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "<td>";
							isiTable = isiTable + sdf2.format(reportBatalPasListx.get(i).get(x).getMsp_message_date());
							isiTable = isiTable + "</td>";
							isiTable = isiTable + "</tr>";
						}
						
						// email file sheet1.xls
						List<File> attachments = new ArrayList<File>();
						File sourceFile = new File(outputDir+"\\"+outputFilename);
						
						attachments.add(sourceFile);
						
						EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
								"info@sinarmasmsiglife.co.id",
								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
								emailList, null,//new String[]{"ingrid@sinarmasmsiglife.co.id", "inge@sinarmasmsiglife.co.id"},//new String[]{"policy_service@sinarmasmsiglife.co.id"},
								new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
								"List data PAS yang sudah dibatalkan di Cabang " +reportBatalPasListx.get(i).get(0).getCabang(),//outputFilename,
								"List data PAS yang dibatalkan karena sudah lebih dari 3 bulan sejak diaktivasi klien. <br>" +
								"Proses data-data nasabah tersebut<br><br>"
								+ "<table border=1>"
								+ isiTable
								+ "</table>"
								+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
								attachments, null);
						
//							email.send(true, 
//									"info@sinarmasmsiglife.co.id",
//									//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//									//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//									emailList, null,//new String[]{"ingrid@sinarmasmsiglife.co.id", "inge@sinarmasmsiglife.co.id"},//new String[]{"policy_service@sinarmasmsiglife.co.id"},
//									new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
//									"List data PAS yang sudah dibatalkan di Cabang " +reportBatalPasListx.get(i).get(0).getCabang(),//outputFilename,
//									"List data PAS yang dibatalkan karena sudah lebih dari 3 bulan sejak diaktivasi klien. <br>" +
//									"Proses data-data nasabah tersebut<br><br>"
//									+ "<table border=1>"
//									+ isiTable
//									+ "</table>"
//									+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//									attachments);
					}
				}
				
			} catch (Exception e) {
				desc = "ERROR";
				logger.error("ERROR :", e);
				err=e.getLocalizedMessage();
			}
			
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						"SCHEDULER PAS BATAL", bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
//================================================================================================
		
		//scheduler untuk batal pas all
		desc = "OK";
		isiTable = "";
		if(batalPasAll == 1){
			try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
				String outputDir = props.getProperty("pdf.dir.report") + "\\pas\\";
				String outputFilename = "pas_batal_all" + sdf.format(commonDao.selectSysdate()) + ".xls";
				//String sheetFilename = "pas_follow_up_pembayaran" + sdf.format(commonDao.selectSysdate());
				String sheetFilename = "Batal PAS ALL";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				
				//List<Map> reportPas = uwDao.selectReportPas();
				//List<Pas> reportPasList = uwDao.selectReportBatalPasList();
				List<Pas> reportPasList = reportBatalPasList;
				
				List<List<Pas>> reportPasListx= new ArrayList<List<Pas>>(); 
				
				int lusIdNode = 0, idx = 0;
				if(reportPasList.size() > 0){
					lusIdNode = reportPasList.get(0).getLus_id();
				}
				for(int i = 0 ; i < reportPasList.size() ; i++){
					if(i == 0){//(i = 0)
						reportPasListx.add(new ArrayList<Pas>());
						reportPasListx.get(idx).add(reportPasList.get(i));
					}else{//(i = 1) sampai (i < reportPasList.size())
						if(lusIdNode == reportPasList.get(i).getLus_id()){// kalau lus_id sebelumnya sama dengan lus_id saat ini -> tambah ke list
							reportPasListx.get(idx).add(reportPasList.get(i));
						}else if(lusIdNode != reportPasList.get(i).getLus_id()){// kalau lus_id sebelumnya beda dengan lus_id saat ini -> buat list baru dan tambahkan
							idx++;
							lusIdNode = reportPasList.get(i).getLus_id();
							reportPasListx.add(new ArrayList<Pas>());
							reportPasListx.get(idx).add(reportPasList.get(i));
						}
					}
				}
				
				if(reportPasListx.size() > 0){
					Date sysdate = commonDao.selectSysdate();
						outputFilename = "pas_batal_all" + sdf.format(commonDao.selectSysdate()) + ".xls";
						
						XLSCreatorPas xlsCreatorPas = new XLSCreatorPas();
						xlsCreatorPas.buildExcelBatalAllDocument(sheetFilename, outputDir+"\\"+outputFilename, reportPasListx, df.format(startDateAll), df.format(nowDate));
						SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
						
						// email file sheet1.xls
						List<File> attachments = new ArrayList<File>();
						File sourceFile = new File(outputDir+"\\"+outputFilename);
						
						attachments.add(sourceFile);
						
						EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
								"info@sinarmasmsiglife.co.id",
								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
								new String[]{"siti_p@sinarmasmsiglife.co.id"},
								new String[]{"fouresta@sinarmasmsiglife.co.id", "mariadf@sinarmasmsiglife.co.id", 
								"ingrid@sinarmasmsiglife.co.id", "asriwulan@sinarmasmsiglife.co.id", "novie@sinarmasmsiglife.co.id"
								, "onna@sinarmasmsiglife.co.id", "andri@sinarmasmsiglife.co.id"
								, "callcentrepas@sinarmasmsiglife.co.id", "claim@sinarmasmsiglife.co.id"},
								new String[]{"andy@sinarmasmsiglife.co.id", "hendra@sinarmasmsiglife.co.id","yune@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id","martino@sinarmasmsiglife.co.id","huluk@sinarmasmsiglife.co.id"},
								"Terlampir data PAS yang telah dibatalkan dari seluruh Cabang.",//outputFilename,
								"List data PAS yang dibatalkan karena sudah lebih dari 3 bulan sejak diaktivasi klien. <br>" +
								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
								attachments, null);
						
//						email.send(true, 
//								"info@sinarmasmsiglife.co.id",
//								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//								new String[]{"ani_chrys@sinarmasmsiglife.co.id", "siti_p@sinarmasmsiglife.co.id"},
//								new String[]{"fouresta@sinarmasmsiglife.co.id", "mariadf@sinarmasmsiglife.co.id", 
//								"ingrid@sinarmasmsiglife.co.id", "asriwulan@sinarmasmsiglife.co.id", "novie@sinarmasmsiglife.co.id"
//								, "onna@sinarmasmsiglife.co.id", "andri@sinarmasmsiglife.co.id"
//								, "callcentrepas@sinarmasmsiglife.co.id", "claim@sinarmasmsiglife.co.id"},
//								new String[]{"andy@sinarmasmsiglife.co.id", "hendra@sinarmasmsiglife.co.id","yune@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id","martino@sinarmasmsiglife.co.id","huluk@sinarmasmsiglife.co.id"},
//								"Terlampir data PAS yang telah dibatalkan dari seluruh Cabang.",//outputFilename,
//								"List data PAS yang dibatalkan karena sudah lebih dari 3 bulan sejak diaktivasi klien. <br>" +
//								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//								attachments);
						
//						email.send(true, 
//								"info@sinarmasmsiglife.co.id",
//								//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//								//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//								new String[]{"ani_chrys@sinarmasmsiglife.co.id", "siti_p@sinarmasmsiglife.co.id"},
//								new String[]{"fouresta@sinarmasmsiglife.co.id", "mariadf@sinarmasmsiglife.co.id", 
//								"ingrid@sinarmasmsiglife.co.id", "asriwulan@sinarmasmsiglife.co.id", "novie@sinarmasmsiglife.co.id"
//								, "onna@sinarmasmsiglife.co.id", "andri@sinarmasmsiglife.co.id"
//								, "callcentrepas@sinarmasmsiglife.co.id", "claim@sinarmasmsiglife.co.id"},
//								new String[]{"andy@sinarmasmsiglife.co.id", "hendra@sinarmasmsiglife.co.id","yune@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id","martino@sinarmasmsiglife.co.id","huluk@sinarmasmsiglife.co.id"},
//								"Terlampir data PAS yang telah dibatalkan dari seluruh Cabang.",//outputFilename,
//								"List data PAS yang dibatalkan karena sudah lebih dari 3 bulan sejak diaktivasi klien. <br>" +
//								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//								attachments);
					
				}
				
	//			insertMstSchedulerHist(
	//				InetAddress.getLocalHost().getHostName(),
	//				"SCHEDULER PAS FOLLOW UP PEMBAYARAN", bdate, new Date(), desc);
				
			} catch (Exception e) {
				desc = "ERROR";
				err=e.getLocalizedMessage();
				logger.error("ERROR :", e);
			}
			
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						"SCHEDULER PAS BATAL ALL", bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
		//================================================================================================

	}
	
	/**
	 * Report Summary RK kemarin, baik pembayaran cash maupun non cash dikirimkan ke dept accounting 
	 */
	public void schedulerSummaryRK(Date tanggal, String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		Date nowDate = commonDao.selectSysdate();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("hh:mm");
		NumberFormat nf = NumberFormat.getInstance();
		Connection conn = null;
		
		Date kemarin = tanggal;
		if(kemarin == null) kemarin = uwDao.selectKemarin(); 

		try {
			conn = this.getDataSource().getConnection();
			List<File> attachments = new ArrayList<File>();
			
			//1. Report 1 : Summary RK (ENTRY > BAC > SUMMARY > BIASA > ALL)
			//String outputDir = "C:\\";
			String outputDir = props.getProperty("pdf.dir.report") + "\\summary_rk\\";
			String outputFilename = "summary_rk_" + dateFormat.format(kemarin) + ".xls";
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			params.put("tglAwal", kemarin);
			params.put("tglAkhir", kemarin);
			params.put("user_print", "SYSTEM");
			params.put("lus_id", "All");
			
			List<Map> reportSummary = bacDao.selectReportSummaryBiasa(params);
			//JasperUtils.exportReportToPdf(props.getProperty("report.summary.biasa")+".jasper", outputDir, outputFilename, params, reportSummary, PdfWriter.AllowPrinting, null, null);
			JasperUtils.exportReportToXls(props.getProperty("report.summary.biasa") + ".jasper", 
					outputDir, outputFilename, params, reportSummary, null);
			
			File sourceFile = new File(outputDir+"\\"+outputFilename);
			attachments.add(sourceFile);
			
			//2. Report 2 : Summary Premi Non Cash (REPORT > UNDERWRITING > REPORT PREMI NON CASH)
			outputFilename = "summary_rk_noncash_"+dateFormat.format(kemarin)+".xls";
			
			params = new HashMap<String, Comparable>();
			params.put("tanggalAwal", df.format(kemarin));
			params.put("tanggalAkhir", df.format(kemarin));
			
			JasperUtils.exportReportToXls(props.getProperty("report.uw.report_PremiNonCash") + ".jasper", 
					outputDir, outputFilename, params, conn);
			
			sourceFile = new File(outputDir+"\\"+outputFilename);
			attachments.add(sourceFile);
			
			//3. Email the reports
			List<String> daftarEmailUnderwriter = new ArrayList<String>();
			for(Map m : reportSummary){
				String email = (String) m.get("LUS_EMAIL");
				if(email !=null){
					if(!daftarEmailUnderwriter.contains(email)) daftarEmailUnderwriter.add(email);
				}
			}
			
			HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryRK", "SCHEDULER_SUMMARY_RK");
			String[] email_to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
			String[] email_cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
			String[] email_bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;

			String[] underwriters = new String[daftarEmailUnderwriter.size() + 1];
			for(int i=0; i<daftarEmailUnderwriter.size(); i++){
				underwriters[i] = daftarEmailUnderwriter.get(i);
			}
			underwriters[underwriters.length-1] = "inge@sinarmasmsiglife.co.id";
			
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, nowDate, null, 
					true, "ajsjava@sinarmasmsiglife.co.id", 
					email_to, 
					underwriters, 
					email_bcc, 
					"[E-Lions] Summary RK New Bussiness dari Dept. Collection", 
					"Summary RK New Bussiness " + df.format(kemarin) +
					"<br>Berikut adalah Laporan Summary RK New Bussiness dari Dept. Collection.", 
					attachments, null);
			
			for(Map m : reportSummary) {
				int add = 0;
				boolean sukses = false;
				
				while(!sukses){
					try {
						sukses = false;
						uwDao.insertMstPositionSpaj("0", "Kirim Summary RK ke Accounting(" + df2.format((Date) m.get("TGL_INPUT")) + "|" + m.get("NO_PRE") + "|" + m.get("NO_VOUCHER") + "|" + nf.format(((BigDecimal)m.get("JUMLAH")).doubleValue()) + ")", (String) m.get("KEY_JURNAL"), add);
						add = 0; sukses = true;
					} catch (Exception e) {
						add++; sukses = false;
					}
				}
			}
			
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, nowDate, null, 
					true, "ajsjava@sinarmasmsiglife.co.id", 
					null,
					null,
//					new String[]{"derry@sinarmasmsiglife.co.id", "jamaludin@sinarmasmsiglife.co.id", "windy@sinarmasmsiglife.co.id", "gesti@sinarmasmsiglife.co.id", "ray.nova@sinarmasmsiglife.co.id", "arnold@sinarmasmsiglife.co.id", "julina.hasan@sinarmasmsiglife.co.id"}, 
//					underwriters,  
					new String[]{"deddy@sinarmasmsiglife.co.id","ryan@sinarmasmsiglife.co.id"}, 
					"[E-Lions] Error Summary RK New Bussiness dari Dept. Collection", 
					"Ada Error saat menjalankan scheduler Summary RK, Terlampir Pesannya : <br><br>"+Common.getRootCause(e).getMessage(),
					null, null);
		}finally{
			closeConnection(conn);
		}

		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Report Scheduler Prata (Generate data untuk Jatis)
	 */
	public void schedulerPrata(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		
		// Query Jatis ada 9, query ke 2,3,4,5 hanya ada yg FULL saja, sedangkan
		// ke 1,6,7,8,9 ada FULL dan INCREMENTAL
		List<DropDown> daftarTabel = new ArrayList<DropDown>();
		daftarTabel.add(new DropDown("masterdata_simas", 		"jat1_master1", 	"i"));
		daftarTabel.add(new DropDown("balancereport_simas", 	"jat2_balanc2", 	null));
		daftarTabel.add(new DropDown("nilaitunai_simas", 		"jat3_ntunai2", 	null));
		daftarTabel.add(new DropDown("detailtrx_simas", 		"jat4_dtrx2",		null));
		daftarTabel.add(new DropDown("detailfund_simas", 		"jat5_dfund2", 		null));
		daftarTabel.add(new DropDown("detailrider_simas", 		"jat6_drider1", 	"i"));
		daftarTabel.add(new DropDown("policyprofile_simas", 	"jat7_pprofile1", 	"i"));
		daftarTabel.add(new DropDown("policyinsurable_simas", 	"jat8_pinsured1", 	"i"));
		daftarTabel.add(new DropDown("policybeneficiary_simas", "jat9_pbenef1", 	"i"));
		
		//supporting vars
		File dirFile 		= null;
		Writer outputFULL	= null;
		Writer outputINCR	= null;
		StringBuffer pesan 	= new StringBuffer();
		BufferedReader input= null;
		Date now 			= commonDao.selectSysdate();
		String nowString 	= (new SimpleDateFormat("yyyyMMddHHmmss")).format(now);
		DateFormat df 		= new SimpleDateFormat("yyyyMMdd");
		String nowString2 	= df.format(now); 
		String separator 	= System.getProperty("line.separator");
		String dir 			= props.getProperty("pdf.dir.report") + "\\prata\\" + nowString;
		StringBuffer test	= new StringBuffer();
		
		try{
			//create directories, satu untuk yg full, satu untuk yg incremental, satu lagi untuk hasil split
			dirFile = new File(dir + "\\full");
			if(!dirFile.exists()) dirFile.mkdirs();
			dirFile = new File(dir + "\\incremental");
			if(!dirFile.exists()) dirFile.mkdirs();
			dirFile = new File(dir + "\\split");
			if(!dirFile.exists()) dirFile.mkdirs();
			
			for(DropDown d : daftarTabel){
				//create text file
				outputFULL = new BufferedWriter(new FileWriter(new File(dir + "\\full\\" + d.getKey() + ".txt")));
				test.append(dir + "\\full\\" + d.getKey() + ".txt");
				outputINCR = new BufferedWriter(new FileWriter(new File(dir + "\\incremental\\" + d.getKey() + ".txt")));
				test.append(dir + "\\incremental\\" + d.getKey() + ".txt");
					
				//column headers
				List<String> kol = crossSellingDao.selectDaftarKolomTabel(d.getValue());
				for(int i=0; i<kol.size(); i++) {
					if(i>0) {
						outputFULL.write(",");
						outputINCR.write(",");
					}
					outputFULL.write(kol.get(i)); 
					outputINCR.write(kol.get(i)); 
				}
				outputFULL.write(separator);
				outputINCR.write(separator);
				
				//column data untuk yg FULL
				List<Map> data = crossSellingDao.selectDataJatis(d.getKey(), null);
				for(int i=0; i<data.size(); i++) {
					for(int j=0; j<kol.size(); j++) {
						if(j>0) outputFULL.write(",");
						Object o = data.get(i).get(kol.get(j));
						if(o != null) outputFULL.write("\"" + o.toString() + "\""); 
					}
					outputFULL.write(separator);
				}
				//column data untuk yg INCR (bila tdk ada, tetap dicreate filenya, yg FULL)
				if(d.getDesc() != null) data = crossSellingDao.selectDataJatis(d.getKey(), d.getDesc());
				for(int i=0; i<data.size(); i++) {
					for(int j=0; j<kol.size(); j++) {
						if(j>0) outputINCR.write(",");
						Object o = data.get(i).get(kol.get(j));
						if(o != null) outputINCR.write("\"" + o.toString() + "\""); 
					}
					outputINCR.write(separator);
				}
				
				outputFULL.close();
				outputINCR.close();
			}
			
			//1. zip yg FULL
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec("C:\\EkaWeb\\WinZip\\wzzip -a -s12345678 -ycAES256 "+dir+"\\bii_"+nowString2+".zip "+dir+"\\full\\*.txt");
			test.append("C:\\EkaWeb\\WinZip\\wzzip -a -s12345678 -ycAES256 "+dir+"\\bii_"+nowString2+".zip "+dir+"\\full\\*.txt");
			//output pesan dari WinZip Console
            StreamGobbler errorGobbler = new StreamGobbler(pr.getErrorStream(), "ERROR");            
            StreamGobbler outputGobbler = new StreamGobbler(pr.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            //exit value
            int exitVal = pr.waitFor();
            if(exitVal != 0){
            	throw new RuntimeException("Gagal Proses Zip File FULL ("+exitVal+")");
            }
            
            //2. zip yg INCR
			pr = rt.exec("C:\\EkaWeb\\WinZip\\wzzip -a -s12345678 -ycAES256 "+dir+"\\bii_"+nowString2+"i.zip "+dir+"\\incremental\\*.txt");
			test.append("C:\\EkaWeb\\WinZip\\wzzip -a -s12345678 -ycAES256 "+dir+"\\bii_"+nowString2+"i.zip "+dir+"\\incremental\\*.txt");
			
			//output pesan dari WinZip Console
            errorGobbler = new StreamGobbler(pr.getErrorStream(), "ERROR");            
            outputGobbler = new StreamGobbler(pr.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            //exit value
            exitVal = pr.waitFor();
            if(exitVal != 0){
            	throw new RuntimeException("Gagal Proses Zip File INCR ("+exitVal+")");
            }
			
            //3. File zip FULL tadi kemudian di-split menjadi 1000kb per file
			pr = rt.exec("C:\\EkaWeb\\WinZip\\wzzip -ys1000k "+dir+"\\bii_"+nowString2+".zip "+dir+"\\split\\bii_"+nowString2+"");
			test.append("C:\\EkaWeb\\WinZip\\wzzip -ys1000k "+dir+"\\bii_"+nowString2+".zip "+dir+"\\split\\bii_"+nowString2+"");
			//output pesan dari WinZip Console
            errorGobbler = new StreamGobbler(pr.getErrorStream(), "ERROR");            
            outputGobbler = new StreamGobbler(pr.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            //exit value
            exitVal = pr.waitFor();
            if(exitVal != 0){
            	throw new RuntimeException("Gagal Proses Split Zip File FULL ("+exitVal+")");
            }
            
            //4. File zip INCR tadi kemudian di-split menjadi 1000kb per file
			pr = rt.exec("C:\\EkaWeb\\WinZip\\wzzip -ys1000k "+dir+"\\bii_"+nowString2+"i.zip "+dir+"\\split\\bii_"+nowString2+"i");
			test.append("C:\\EkaWeb\\WinZip\\wzzip -ys1000k "+dir+"\\bii_"+nowString2+"i.zip "+dir+"\\split\\bii_"+nowString2+"i");
			//output pesan dari WinZip Console
            errorGobbler = new StreamGobbler(pr.getErrorStream(), "ERROR");            
            outputGobbler = new StreamGobbler(pr.getInputStream(), "OUTPUT");
            errorGobbler.start();
            outputGobbler.start();
            //exit value
            exitVal = pr.waitFor();
            if(exitVal != 0){
            	throw new RuntimeException("Gagal Proses Split Zip File INCR ("+exitVal+")");
            }
            
			/* INFO : BAGIAN ZIP DIGANTI WINZIP, BUKAN 7ZIP LAGI
			//start zipping files
    		//7z -aoa -psecret -mem=AES256 a 20090928.zip .\test\* -v1000k
			//
            //-p 			= password
            //-m 			= metode enkripsi
            //-aoa			= overwrite files w/o prompt
            //a 			= add to archive
            //-v1024k		= split file per 1 mb
            //*.zip 		= nama file zip hasilnya
            //.\test\*.txt= semua yg ada di subfolder test
			
			//1. zip yg FULL
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec("C:\\EkaWeb\\Setup\\7z -aoa -p123 -mem=AES256 a "+dir+"\\bii_"+nowString2+".zip "+dir+"\\full\\*.txt -v1000k");

			//output pesan dari 7zip console
			pesan.append("GENERATING ZIP (FULL)" + separator);
			input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line=null;
            while((line=input.readLine()) != null) {
            	pesan.append(line + separator);
            }
			
            //0 No error 
            //1 Warning (Non fatal error(s)). For example, one or more files were locked by some other application, so they were not compressed. 
            //2 Fatal error 
            //7 Command line error 
            //8 Not enough memory for operation 
            //255 User stopped the process 
            int exitVal = pr.waitFor();
            if(exitVal != 0){
            	throw new RuntimeException("Gagal Proses Zip File FULL ("+exitVal+")");
            }
			
            //2. zip yg INCR
			pr = rt.exec("C:\\EkaWeb\\Setup\\7z -aoa -p123 -mem=AES256 a "+dir+"\\bii_"+nowString2+"i.zip "+dir+"\\incremental\\*.txt -v1000k");

			//output pesan dari 7zip console
			pesan.append("GENERATING ZIP (INCR)" + separator);
			input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            line = null;
            while((line=input.readLine()) != null) {
            	pesan.append(line + separator);
            }

            exitVal = pr.waitFor();
            if(exitVal != 0){
            	throw new RuntimeException("Gagal Proses Zip File INCR ("+exitVal+")");
            }
			*/

            //email hasilnya
			dirFile = new File(dir + "\\split");
            String[] zipList = dirFile.list();
            List<File> attachments = null;
            int count = 0;
            
            for(int i=1; i<=zipList.length; i++) {
            	File attachment = new File(dir + "\\split\\" + zipList[i-1]);
            	if(attachment.isFile()) count++;
            }
            
            //untuk setiap file, di email sekali
			for(int i=1; i<=zipList.length; i++) {
				File attachment = new File(dir + "\\split\\" + zipList[i-1]);
				if(attachment.isFile()){ //yg diemail file nya saja, yg directory jgn
					attachments = new ArrayList<File>();
					attachments.add(attachment);
					
					//DISABLE THIS!
					//logger.info("[Sinarmas MSIG Life] Data BII-Jatis ("+i+"/"+count+")");
					//logger.info(attachment.toString());
					
					//ENABLE THIS!
					//disabled, req Himmia 24/11/09
					//enabled again since 10 feb 2010, req Himmia
					//disabled, req Himmia 26/11/10
//					email.send(false, 
//							props.getProperty("admin.ajsjava"),
//							new String[] {"SYusuf@bankbii.com", "swahyuni@bankbii.com", "nsetiawan@bankbii.com"},
//							null,
//							null,
//							"[Sinarmas MSIG Life] Data BII-Jatis ("+i+"/"+count+")", 
//							"Dear All,\n" +
//							"Berikut data AJS.", 
//							attachments);

					//ini untuk cek apakah sampe aja
//					email.send(false, 
//							props.getProperty("admin.ajsjava"),
//							new String[] {"yusufsutarko@yahoo.com", "himmia@yahoo.com"},
//							null,
//							null,
//							"[Sinarmas MSIG Life] Data BII-Jatis ("+i+"/"+count+")", 
//							"Dear All,\n" +
//							"Berikut data AJS.", 
//							null);
					
				}
			}
            
		}catch(Exception e){
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
			try {
				email.send(false, 
						props.getProperty("admin.ajsjava"),
						new String[] {props.getProperty("admin.yusuf")}, null, null,
						"Error Saat Generate Data BII-Jatis", 
						e.toString() + separator + pesan.toString(), null);
			} catch (Exception e1) {}
			
			desc = "ERROR";
			
		}finally{

			try {
				if(outputFULL != null) {
					outputFULL.close();
				}
				if(outputINCR != null) {
					outputINCR.close();
				}
				if(input != null) {
					input.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("cannot closed reader or writer", e);
			}
			
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						msh_name, bdate, new Date(), desc,err);
				
				// delete file2 yg gak perlu sesudah selesai kirim email
				
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
		
	}
	
	/**
	 * Proses untuk generate text file dari EKA.MST_BMI, dijalankan secara schedule tiap jam 5 sore, lalu diemail ke uw dan muamalat
	 * 
	 * @author Yusuf
	 * @since Nov 28, 2008 (10:55:20 AM)
	 * @throws DataAccessException
	 * @throws IOException 
	 */
	public void schedulerBmi(String msh_name) throws DataAccessException, IOException{
//		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		
		List<Bmi> daftar = muamalatDao.selectDataUntukGenerateTextFileBmi();
		
		if(!daftar.isEmpty()) {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			Date now = commonDao.selectSysdate();

			File textFile = null;
			Writer output = null;
			List<String> daftarBmi = new ArrayList<String>();
			
			try {
				String dir = props.getProperty("pdf.dir.report") + "\\muamalat\\";
				String path = dir + df.format(now) + ".txt";
				textFile = new File(path);
				output = new BufferedWriter(new FileWriter(textFile));
				
				//1. Generate Text File
				
				try {
					for(Bmi bmi : daftar) {
						output.write(
								bmi.no_seri + "|" + 	bmi.kd_bank + "|" +		bmi.no_kartu + "|" + 
								bmi.nama + "|" + 		bmi.sex + "|" +			bmi.mspe_place_birth + "|" + 
								bmi.tglahir + "|" + 	bmi.agama + "|" +		bmi.alamat + "|" + 
								bmi.atagih + "|" + 		bmi.telp_rmh + "|" +	bmi.telp_ktr + "|" + 
								bmi.no_hp + "|" + 		bmi.ktp + "|" +			bmi.mspe_no_identity + "|" + 
								bmi.mspe_mother + "|" + bmi.sumber + "|" +		bmi.ptambah + "|" + 
								bmi.kerja + "|" + 		bmi.ptetap + "|" +		bmi.ptdk + "|" + 
								bmi.agen + "|" + 		bmi.rekagen + "|" +		bmi.bdate + "|" + 
								bmi.edate + "|" + 		bmi.akhir + "|" +		bmi.premi.intValue());
						output.write("\n"); //spasi
						
						Bmi updateBmi = new Bmi();
						updateBmi.mdb_id = bmi.mdb_id;
						updateBmi.reg_spaj = bmi.reg_spaj;
						daftarBmi.add(updateBmi.mdb_id);
					}
				} finally {
					if(output!=null){
						output.close();
					}
				}
				
				//2. Email 1 TextFile + 2 PDF (aksep dan tolak) ke Muamalat, CC ke U/W
				
				List<File> attachments = new ArrayList<File>();
				attachments.add(textFile);
				
				String outputFilename = df.format(now);
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();

				//report aksep
				List listAksep = muamalatDao.selectDataUntukReportBmi(daftarBmi, 5);
				if(!listAksep.isEmpty()) {
					JasperUtils.exportReportToPdf(
							props.getProperty("report.muamalat.bank_muamalat")+".jasper", 
							dir, 
							outputFilename + "_aksep.pdf", 
							params, 
							listAksep, 
							PdfWriter.AllowPrinting, null, null);

					File sourceFile = new File(dir+"\\"+outputFilename + "_aksep.pdf");
					attachments.add(sourceFile);
				}
				
				//report tolak
				List listTolak = muamalatDao.selectDataUntukReportBmi(daftarBmi, 2);
				if(!listTolak.isEmpty()) {
					JasperUtils.exportReportToPdf(
							props.getProperty("report.muamalat.tolak_bank_muamalat")+".jasper", 
							dir, 
							outputFilename + "_tolak.pdf", 
							params, 
							listTolak, 
							PdfWriter.AllowPrinting, null, null);
					
					File sourceFile2 = new File(dir+"\\"+outputFilename + "_tolak.pdf");
					attachments.add(sourceFile2);
				}
				
				//(Deddy)Tambahan- Tiap proses SPAJ, dimasukkan SSU ke masing2 produk.
				for(int i=0;i<listAksep.size();i++){
					Map map = (Map) listAksep.get(i);
					String lsbs_id = "";
					String lsdbs_number = "";
					String reg_spaj = (String) map.get("REG_SPAJ");
					File directory = new File(
							props.getProperty("pdf.dir.export") + "\\" +
							uwDao.selectCabangFromSpaj(reg_spaj) + "\\" +
							reg_spaj);
					if(!directory.exists()) directory.mkdirs();
					List detail = uwDao.selectDetailBisnis(reg_spaj);
					Map det = (HashMap) detail.get(0);
					if(!detail.isEmpty()) {
						lsbs_id = (String) det.get("BISNIS");
						lsdbs_number = (String) det.get("DETBISNIS");
					}
					if((lsbs_id.equals("170")&& lsdbs_number.equals("001")) || 
							(lsbs_id.equals("153")&& lsdbs_number.equals("005")) || 
							(lsbs_id.equals("171")&& lsdbs_number.equals("001"))){
						File from = null;
						from = new File(props.getProperty("pdf.dir.syaratpolis") + "\\"+lsbs_id+"-"+lsdbs_number+"-AUTOSS.pdf");
						File to = new File(
								props.getProperty("pdf.dir.export") + "\\" +
								uwDao.selectCabangFromSpaj(reg_spaj) + "\\" +
								reg_spaj + "\\" +
								props.getProperty("pdf.ssu") + ".pdf");
						FileUtils.copy(from, to);
					}
				}
				
				if(!listAksep.isEmpty() || !listTolak.isEmpty()){
					email.send(true, 
							props.getProperty("admin.ajsjava"),
							new String[]{"jgunawan@muamalatbank.com", "ayu.sofiyana@muamalatbank.com"}, //"dessy.saptiani@muamalatbank.com" 
							new String[]{
								"Fouresta@sinarmasmsiglife.co.id",
								"Saputra@sinarmasmsiglife.co.id",
								"Ingrid@sinarmasmsiglife.co.id",
								"Novie@sinarmasmsiglife.co.id",
								"asriwulan@sinarmasmsiglife.co.id",
								"Shopiah@sinarmasmsiglife.co.id",
								"ingrid@sinarmasmsiglife.co.id",
								"asriwulan@sinarmasmsiglife.co.id",
								"sisca.r@sinarmasmsiglife.co.id", 
								"aprina@sinarmasmsiglife.co.id", 
								"vonny_t@sinarmasmsiglife.co.id", 
								"sally@sinarmasmsiglife.co.id", 
								"mariadf@sinarmasmsiglife.co.id", 
								"himmia@sinarmasmsiglife.co.id", 
								"yusufsutarko@gmail.com", 
								"anggi@sinarmasmsiglife.co.id",
								"novi_a@sinarmasmsiglife.co.id",
								"hafri@sinarmasmsiglife.co.id",
								"ayu_g@sinarmasmsiglife.co.id"},
							//new String[]{"yusuf@sinarmasmsiglife.co.id"},new String[]{"yusuf@sinarmasmsiglife.co.id"},
							//new String[]{props.getProperty("admin.yusuf")},
							null,
							"Laporan Harian Produk Bank Muamalat Per " + completeDateFormat.format(now),
							"Berikut adalah Laporan Harian Produk Bank Muamalat Per " + completeDateFormat.format(now), 
							attachments);
				}
				
				//3. Bila semua berhasil, baru update Database 
				for(Bmi bmi : daftar) {
					Bmi updateBmi = new Bmi();
					updateBmi.mdb_id = bmi.mdb_id;
					updateBmi.reg_spaj = bmi.reg_spaj;
					updateBmi.user_proses = 0;
					updateBmi.tgl_proses = now;
					muamalatDao.updateMstDataBmi(updateBmi);
				}
				
				
			} catch (Exception e) {
				logger.error("ERROR :", e);

				StringBuffer stackTrace = new StringBuffer();
				stackTrace.append("\n- Exception : \n");
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				stackTrace.append(sw);
				try {
					
				}finally{
					if(sw!=null){
						sw.close();
					}
					if(pw!=null){
						pw.close();
					}
				}
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					false,
					props.getProperty("admin.ajsjava"),
					new String[] {props.getProperty("admin.yusuf"), "yusufsutarko@gmail.com"}, 
					null, null, 
					"ERROR Laporan Harian Muamalat", 
					stackTrace.toString(), 
					null, null);
				desc = "ERROR";err=e.getLocalizedMessage();
			}
		}

		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}

	}
	
	/**
	 * Proses Transfer Polis2 yang nyangkut di TTP > 30 hari
	 */
	public void schedulerKomisi30Hari(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

		User currentUser = new User();
        currentUser.setLus_id("0");
        currentUser.setName("E-Lions");
        currentUser.setDept("IT");
        Map map = new HashMap();
        ServletRequestDataBinder a = new ServletRequestDataBinder(map, "cmd");
        String hasil = "";
        try {
    		hasil = this.transferPolis.transferTandaTerimaPolisToKomisiOrFilling30Hari(currentUser, a.getErrors());
        }catch(Exception e) {
        	try {
				email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[] {props.getProperty("admin.yusuf")}, null, null, "ERROR PROSES KOMISI 30 HARI", e.toString(), null);
			} catch (MailException e1) {
				logger.error("ERROR :", e1);
			} catch (MessagingException e1) {
				logger.error("ERROR :", e1);
			}
			desc = "ERROR";
			err=e.getLocalizedMessage();
        }finally {
    		try {
    			insertMstSchedulerHist(
    					InetAddress.getLocalHost().getHostName(),
    					msh_name, bdate, new Date(), desc,err);
    		} catch (UnknownHostException e) {
    			logger.error("ERROR :", e);
    		}
        }
	}
	
	/**
	 * Proses scheduler email status pa bsm
	 */
	public void schedulerEmailStatusPabsm(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("HH:mm");
		
		Date nowDate = commonDao.selectSysdate();
		Date yesterday = commonDao.selectSysdateTruncated(-1);
		String isiTable = "";

        try {
			
        	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String outputDir = props.getProperty("pdf.dir.report") + "\\pabsm\\";
			String outputFilename = "email_status_pa_bsm" + sdf.format(commonDao.selectSysdate()) + ".xls";
			String sheetFilename = "Email Status PA BSM";
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			
			List<Map> reportPasList = uwDao.selectReportEmailStatusPabsm();
			
			if(reportPasList.size() > 0){
				XLSCreatorPas xlsCreatorPas = new XLSCreatorPas();
				xlsCreatorPas.buildExcelEmailStatusPaBsm(sheetFilename, outputDir+"\\"+outputFilename, reportPasList, df.format(yesterday));
				
				// email file sheet1.xls
				List<File> attachments = new ArrayList<File>();
				File sourceFile = new File(outputDir+"\\"+outputFilename);
				
				attachments.add(sourceFile);
				
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, nowDate, null, 
						true, "ajsjava@sinarmasmsiglife.co.id", 
						new String[]{"inge@sinarmasmsiglife.co.id"}, 
						null, 
						new String[]{"andy@sinarmasmsiglife.co.id"}, 
						"Data Email Status PA BSM untuk tanggal "+yesterday, 
						"Terlampir Data Email Status untuk Produk PA BSM. <br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachments, null);
			}
        	
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Proses scheduler autoupdate pas column
	 */
	public void schedulerAutoUpdatePasColumn(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

        try {
			
        	//AUTO UPDATE KOLOM MSAG_ID_PP DI MST_PAS_SMS UNTUK JENIS_PAS = AP/BP ONLINE===
        	uwDao.updateAgentCodePasFromRegister();
			//===============================================================================
        	
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Proses scheduler further requirement BP
	 */
	public void schedulerFurtherRequirementBp(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

        try {
			
			List<File> attachments = new ArrayList<File>();
			
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String outputDir = props.getProperty("pdf.dir.report") + "\\further_requirement_pas_bp\\";
			String outputFilename = "fr_pas_bp" + sdf.format(commonDao.selectSysdate()) + ".xls";
			//String sheetFilename = "pas" + sdf.format(commonDao.selectSysdate());
			String sheetFilename = "sheet1";
			
			Date yesterday = commonDao.selectSysdateTruncated(-1);
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			
			List<Pas> reportFrPasBp = uwDao.selectFrPasBp();
			
			// generate & save outputFilename
//			JasperUtils.exportReportToXls(props.getProperty("report.fire") + ".jasper", 
//					outputDir, outputFilename, params, reportPas, null);
			//==========================================
			
			// generate & save outputFilenameSheet
//			JasperUtils.exportReportToXls(props.getProperty("report.fire") + ".jasper", 
//					outputDir, outputFilenameSheet, params, reportPas, null);
			//==========================================
			
			if(reportFrPasBp.size() > 0){
				XLSCreatorFrPasBp xlsCreatorFrPasBp = new XLSCreatorFrPasBp();
				xlsCreatorFrPasBp.buildExcelDocument(sheetFilename, outputDir+"\\"+outputFilename, reportFrPasBp, yesterday);
				
				// email file sheet1.xls
				File sourceFile = new File(outputDir+"\\"+outputFilename);
				
				attachments.add(sourceFile);
				
				HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerFurtherRequirementBp", "FURTHER_REQUIREMENT_BP");
				String[] email_to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
				String[] email_cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
				String[] email_bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;

				
				//sub_section mst_config: Further_Requirement_BP
				EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, new Date(), null, false,
						"info@sinarmasmsiglife.co.id",
						email_to,
						email_cc,
						email_bcc,
						"Data Further Requirement PAS BP",//outputFilename,
						"Berikut adalah Laporan Further Requirement PAS BP."
						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachments, null);
				
			}
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
		}
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Proses scheduler further requirement DBD-(BP/Agency)
	 */
	public void schedulerFurtherRequirementDbd(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

        try {
			
        	List<File> attachmentBp = new ArrayList<File>();
			List<File> attachmentAgen = new ArrayList<File>();
			
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String outputDir = props.getProperty("pdf.dir.report") + "\\further_requirement_dbd\\";
			String outputFilenameDbdBp = "fr_dbd_bp" + sdf.format(commonDao.selectSysdate()) + ".xls";
			String outputFilenameDbdAgen = "fr_dbd_agen" + sdf.format(commonDao.selectSysdate())+".xls";
			//String sheetFilename = "pas" + sdf.format(commonDao.selectSysdate());
			String sheetFilename = "sheet1";
			
			Date yesterday = commonDao.selectSysdateTruncated(-1);
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			
			List<Pas> reportFrDbdBp = uwDao.selectFrDbd("DBD BP");
			List<Pas> reportFrDbdAgen = uwDao.selectFrDbd("DBD AGENCY");
			// generate & save outputFilename
//			JasperUtils.exportReportToXls(props.getProperty("report.fire") + ".jasper", 
//					outputDir, outputFilename, params, reportPas, null);
			//==========================================
			
			// generate & save outputFilenameSheet
//			JasperUtils.exportReportToXls(props.getProperty("report.fire") + ".jasper", 
//					outputDir, outputFilenameSheet, params, reportPas, null);
			//==========================================
			
			if(reportFrDbdBp.size() > 0){
				XLSCreatorFrPasBp xlsCreatorFrDbdBp = new XLSCreatorFrPasBp();
				xlsCreatorFrDbdBp.buildExcelDocument(sheetFilename, outputDir+"\\"+outputFilenameDbdBp, reportFrDbdBp, yesterday);
				
				// email file sheet1.xls
				File sourceFile = new File(outputDir+"\\"+outputFilenameDbdBp);				
				attachmentBp.add(sourceFile);	
				
				HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerFurtherRequirementDbd", "FURTHER_REQUIREMENT_DBD_BP");
				String[] email_to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
				String[] email_cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
				String[] email_bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
				
				//keterangan di sub_section mst_config :Further_Requirement_DBD_BP
				EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, new Date(), null, true,
						"info@sinarmasmsiglife.co.id",					
						email_to,
						email_cc,
						email_bcc,
						"Data Further Requirement DBD BP",//outputFilename,
						"Berikut adalah Laporan Further Requirement DBD BP."
						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachmentBp, null);	
				
//				 email.send(true, 
//						"info@sinarmasmsiglife.co.id",					
//						new String[]{"srirahayu@sinarmasmsiglife.co.id","widia_a@sinarmasmsiglife.co.id","selvi@sinarmasmsiglife.co.id"}, 
//						new String[]{"stephanus@sinarmasmsiglife.co.id","underwritingagencyworksite@sinarmasmsiglife.co.id","timmy@sinarmasmsiglife.co.id","eko_b@sinarmasmsiglife.co.id","dewi_andriyati@sinarmasmsiglife.co.id"}, 
//						new String[]{"andy@sinarmasmsiglife.co.id","adrian_n@sinarmasmsiglife.co.id"}, 
//						"Data Further Requirement DBD BP",//outputFilename,
//						"Berikut adalah Laporan Further Requirement DBD BP."
//						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//						attachmentBp);	
				}
			if(reportFrDbdAgen.size() > 0){
				XLSCreatorFrPasBp xlsCreatorFrDbdAgen = new XLSCreatorFrPasBp();
				xlsCreatorFrDbdAgen.buildExcelDocument(sheetFilename, outputDir+"\\"+outputFilenameDbdAgen, reportFrDbdAgen, yesterday);
				
				// email file sheet1.xls
				File sourceFile = new File(outputDir+"\\"+outputFilenameDbdAgen);				
				attachmentAgen.add(sourceFile);	
				
				HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerFurtherRequirementDbd", "FURTHER_REQUIREMENT_DBD_AGENCY");
				String[] email_to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
				String[] email_cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
				String[] email_bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
				
				//sub_section mst_config :Further_Requirement_DBD_Agency
				EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, new Date(), null, true,
						"info@sinarmasmsiglife.co.id",					
						email_to,
						email_cc,
						email_bcc,
						"Data Further Requirement DBD Agency",//outputFilename,
						"Berikut adalah Laporan Further Requirement DBD Agency."
						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachmentAgen, null); 	
							
//	     	      email.send(true, 
//						"info@sinarmasmsiglife.co.id",					
//						new String[]{"siti_p@sinarmasmsiglife.co.id","selvi@sinarmasmsiglife.co.id"}, 
//						new String[]{"keke@sinarmasmsiglife.co.id","aderohman@sinarmasmsiglife.co.id","yusy@sinarmasmsiglife.co.id","arief_dw@sinarmasmsiglife.co.id","underwritingagencyworksite@sinarmasmsiglife.co.id","timmy@sinarmasmsiglife.co.id","eko_b@sinarmasmsiglife.co.id","dewi_andriyati@sinarmasmsiglife.co.id"}, 
//						new String[]{"andy@sinarmasmsiglife.co.id","adrian_n@sinarmasmsiglife.co.id"}, 
//						"Data Further Requirement DBD Agency",//outputFilename,
//						"Berikut adalah Laporan Further Requirement DBD Agency."
//						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//						attachmentAgen); 	
				}
//			insertMstSchedulerHist(
//				InetAddress.getLocalHost().getHostName(),
//				"SCHEDULER PAS SIMAS RUMAH FREE", bdate, new Date(), desc);
			
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}	
	
	public void schedulerFurtherRequirementMallAssurance(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

        try {
			
        	List<File> attachmentMallAssurance = new ArrayList<File>();
				
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String outputDir = props.getProperty("pdf.dir.report") + "\\further_requirement_mall\\";
			String outputFilenameMallAssurance = "fr_MallAssurance" + sdf.format(commonDao.selectSysdate()) + ".xls";
			
			//String sheetFilename = "pas" + sdf.format(commonDao.selectSysdate());
			String sheetFilename = "sheet1";
			
			Date yesterday = commonDao.selectSysdateTruncated(-1);
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			
			List<Pas> reportFrMallAssurance = uwDao.selectFrMallAssurance();
			
			if(reportFrMallAssurance.size() > 0){
				XLSCreatorFrMallAssurance xlsCreatorFrMallAssurance = new XLSCreatorFrMallAssurance();
				xlsCreatorFrMallAssurance.buildExcelDocument(sheetFilename, outputDir+"\\"+outputFilenameMallAssurance, reportFrMallAssurance, yesterday);
				
				// email file sheet1.xls
				File sourceFile = new File(outputDir+"\\"+outputFilenameMallAssurance);				
				attachmentMallAssurance.add(sourceFile);	
				
				EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, new Date(), null, false,
						"info@sinarmasmsiglife.co.id",					
						new String[]{"mallassurance@sinarmasmsiglife.co.id"}, 
						new String[]{"apriyani@sinarmasmsiglife.co.id","inge@sinarmasmsiglife.co.id","ningrum@sinarmasmsiglife.co.id","sutini@sinarmasmsiglife.co.id","inge@sinarmasmsiglife.co.id"}, 
						new String[]{"andy@sinarmasmsiglife.co.id","adrian_n@sinarmasmsiglife.co.id"}, 
						"Data Further Requirement PAS MallAssurance",//outputFilename,
						"Berikut adalah Laporan Further Requirement PAS MallAssurance."
						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachmentMallAssurance, null);
			
//				 email.send(true, 
//						"info@sinarmasmsiglife.co.id",					
//						new String[]{"mallassurance@sinarmasmsiglife.co.id"}, 
//						new String[]{"apriyani@sinarmasmsiglife.co.id","inge@sinarmasmsiglife.co.id","ningrum@sinarmasmsiglife.co.id","sutini@sinarmasmsiglife.co.id","inge@sinarmasmsiglife.co.id"}, 
//						new String[]{"andy@sinarmasmsiglife.co.id","adrian_n@sinarmasmsiglife.co.id"}, 
//						"Data Further Requirement PAS MallAssurance",//outputFilename,
//						"Berikut adalah Laporan Further Requirement PAS MallAssurance."
//						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//						attachmentMallAssurance);
				 			
				}			

	}	catch (Exception e) {
		desc = "ERROR";
		logger.error("ERROR :", e);
		err=e.getLocalizedMessage();
	}        
    	try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	/**
	 * Proses scheduler Attention List
	 */
	public void schedulerBlacklist(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

        try {
			
			List<File> attachments = new ArrayList<File>();
			
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			String outputDir = props.getProperty("pdf.dir.report") + "\\blacklist\\"; //folder Attention List
			String outputFilename = "attention_list" + sdf.format(commonDao.selectSysdate()) + ".xls";
			//String sheetFilename = "pas" + sdf.format(commonDao.selectSysdate());
			String sheetFilename = "sheet1";
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			
			//List<Map> reportFire = uwDao.selectReportFire();
			List<BlackList> reportBlackList = uwDao.selectReportBlackList();
			
			// generate & save outputFilename
//			JasperUtils.exportReportToXls(props.getProperty("report.fire") + ".jasper", 
//					outputDir, outputFilename, params, reportPas, null);
			//==========================================
			
			// generate & save outputFilenameSheet
//			JasperUtils.exportReportToXls(props.getProperty("report.fire") + ".jasper", 
//					outputDir, outputFilenameSheet, params, reportPas, null);
			//==========================================
			
			if(reportBlackList.size() > 0){
				XLSCreatorBlacklist xlsCreatorBlacklist = new XLSCreatorBlacklist();
				xlsCreatorBlacklist.buildExcelDocument(sheetFilename, outputDir+"\\"+outputFilename, reportBlackList);
				
				// email file sheet1.xls
				File sourceFile = new File(outputDir+"\\"+outputFilename);
				
				attachments.add(sourceFile);
				
				HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerBlacklist", "schedulerBlacklist");
				String from = props.getProperty("admin.ajsjava");
				String emailto = mapEmail.get("NAME")!=null? mapEmail.get("NAME").toString():null;
				String emailbcc = mapEmail.get("NAME3")!=null? mapEmail.get("NAME3").toString():null;
				
				String[] to = emailto.split(";");
				String[] bcc = emailbcc.split(";");
				
				EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, new Date(), null, false,
						"info@sinarmasmsiglife.co.id",
						//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
						//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
						to, 
						null,
						bcc,
						"Data Attention List",//outputFilename,
						"Berikut adalah Laporan Attention List."
						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachments, null);
				
//				email.send(true, 
//						"info@sinarmasmsiglife.co.id",
//						//new String[]{"yusuf@sinarmasmsiglife.co.id"}, null,
//						//new String[]{"mkt.pld@sinarmas.co.id"}, new String[]{"christin@sinarmas.co.id"},
//						new String[]{"yurika@sinarmasmsiglife.co.id","andreas@sinarmasmsiglife.co.id"}, 
//						null,
//						new String[]{"andy@sinarmasmsiglife.co.id"},
//						"Data Attention List",//outputFilename,
//						"Berikut adalah Laporan Attention List."
//						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//						attachments);
				
				//FileUtil.copyfile(outputDir+"\\"+outputFilenameSheet, outputDir+"\\"+outputFilename);
				// delete file sheet1.xls
				//FileUtil.deleteFile(outputDir, outputFilenameSheet, null);
			}
			
//			insertMstSchedulerHist(
//				InetAddress.getLocalHost().getHostName(),
//				"SCHEDULER PAS SIMAS RUMAH FREE", bdate, new Date(), desc);
			
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Proses Email Rekap Pembatalan SPAJ oleh UW
	 */
	public void schedulerRekapPembatalanUw(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

		try{
		  	Date nowDate = refundDao.selectNowDate();
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
			err=e.getLocalizedMessage();
			desc = "ERROR";
		}
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Scheduler untuk summary expired
	 */
	public void schedulerSummaryExpired(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

		try {
			Date yesterday = commonDao.selectSysdateTruncated(-1);
			Date today = commonDao.selectSysdateTruncated(0);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			//Deddy (13/3/2009) tambahan untuk menentukan tahun -1 dari sekarang 
			DateFormat year = new SimpleDateFormat("yyyy");
			String yearbefore = year.format(FormatDate.add(today, Calendar.YEAR, -1) );
			
			//Deddy(16/3/2009) tambahan untuk menentukan bulan1-12 dari tahun sekarang
			DateFormat monthyear = new SimpleDateFormat("mmyyyy");
			String month1 = "01"+ year.format(today);
			String month2 = "02"+ year.format(today);
			String month3 = "03"+ year.format(today);
			String month4 = "04"+ year.format(today);
			String month5 = "05"+ year.format(today);
			String month6 = "06"+ year.format(today);
			String month7 = "07"+ year.format(today);
			String month8 = "08"+ year.format(today);
			String month9 = "09"+ year.format(today);
			String month10 = "10"+ year.format(today);
			String month11 = "11"+ year.format(today);
			String month12 = "12"+ year.format(today);
			
			
			logger.info("UW SCHEDULER AT " + new Date());
			long start = System.currentTimeMillis();
			
			//Report Summary (Menu dapat diakses di Entry > UW > Akseptasi Khusus > Summary)
			
			String outputDir = props.getProperty("pdf.dir.report") + "\\summary_akseptasi\\" + dateFormat.format(today) + "\\";
//			String outputDir = "D:\\Test\\";
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			params.put("tanggal", df.format(today));
			params.put("user", "SYSTEM");
			
			//passing parameter ke reportnya
			params.put("yearbefore", yearbefore);
			params.put("month1", month1);
			params.put("month2", month2);
			params.put("month3", month3);
			params.put("month4", month4);
			params.put("month5", month5);
			params.put("month6", month6);
			params.put("month7", month7);
			params.put("month8", month8);
			params.put("month9", month9);
			params.put("month10", month10);
			params.put("month11", month11);
			params.put("month12", month12);

			Map<String, List<Map>> distribusi 			= new HashMap<String, List<Map>>();
			Map<String, List<Map>> daftarReport 		= new HashMap<String, List<Map>>();
			Map<String, List<File>> daftarAttachment1 	= new HashMap<String, List<File>>();
			Map<String, List<File>> daftarAttachment2 	= new HashMap<String, List<File>>();
			
			daftarAttachment1.put("Agency", 	new ArrayList<File>());
			daftarAttachment1.put("Hybrid", 	new ArrayList<File>());
			daftarAttachment1.put("Regional", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass1", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass2", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass3", 	new ArrayList<File>());
			daftarAttachment1.put("Worksite", 	new ArrayList<File>());
			daftarAttachment1.put("DMTM", 		new ArrayList<File>());
			
			daftarAttachment2.put("Agency", 	new ArrayList<File>());
			daftarAttachment2.put("Hybrid", 	new ArrayList<File>());
			daftarAttachment2.put("Regional", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass1", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass2", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass3", 	new ArrayList<File>());
			daftarAttachment2.put("Worksite", 	new ArrayList<File>());
			daftarAttachment2.put("DMTM", 		new ArrayList<File>());
			
			String b;
			//Daftar Semua Report
			daftarReport.put("Expired", uwDao.selectDaftarAkseptasiNyangkut3(10,3, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 				//5. polis expired
			
			//Looping Utama dari daftar semua report
			for(String r : daftarReport.keySet()) {
				
				params.put("banyakMaunya", r);
				
				//Parameter Tambahan
				if(r.equals("Expired")) {
					params.put("judul", "Follow-Up Polis Expired");
				}
		
				if(r.equals("Expired")) {
					params.put("note",
						"Note:\n" +
						"Data yang masuk disini adalah data :\n" +
						"* Polis yang expired > 90 hari ( Polis Expired > 90 Hari )");
				}else {
					params.put("note", "");
				}
				
				//Daftar pembagian distribusi, direset setiap loop
				distribusi.put("Agency", 	new ArrayList<Map>());
				distribusi.put("Hybrid", 	new ArrayList<Map>());
				distribusi.put("Regional", 	new ArrayList<Map>());
				distribusi.put("Bancass1", 	new ArrayList<Map>());
				distribusi.put("Bancass2", 	new ArrayList<Map>());
				distribusi.put("Bancass3", 	new ArrayList<Map>());
				distribusi.put("Worksite", 	new ArrayList<Map>());
				distribusi.put("DMTM", 		new ArrayList<Map>());
				
				List<Map> report = daftarReport.get(r);
				
				//Looping untuk membagi2 hasil query ke masing2 distribusi
				for(Map m : report) {
					String lca_id = (String) m.get("LCA_ID");
					String team_name = (String) m.get("TEAM_NAME");
					BigDecimal jenis = (BigDecimal) m.get("JN_BANK");
					int jn_bank = (jenis == null ? 0 : jenis.intValue());
					
					if(team_name== null){
						team_name= "";
					}
					
					if("37,52".indexOf(lca_id)>=-1) { //Agency
						((List<Map>) distribusi.get("Agency")).add(m);
					}else if(lca_id.equals("46")) { //Hybrid
						((List<Map>) distribusi.get("Hybrid")).add(m);
					}else if(lca_id.equals("09")) { //Bancassurance
						if(team_name.toUpperCase().equals("TEAM JAN ROSADI")) { //Bancassurance2
							((List<Map>) distribusi.get("Bancass2")).add(m);
						}else if(team_name.toUpperCase().equals("TEAM DEWI")) { //Bancassurance3
							((List<Map>) distribusi.get("Bancass3")).add(m);
						}else { //Bancassurance1
							((List<Map>) distribusi.get("Bancass1")).add(m);
						}
					}else if(lca_id.equals("08") || lca_id.equals("42")) { //Worksite
						((List<Map>) distribusi.get("Worksite")).add(m);
					}else if(lca_id.equals("55")) { //DM/TM
						((List<Map>) distribusi.get("DMTM")).add(m);
					}else { //Regional
						((List<Map>) distribusi.get("Regional")).add(m);
					}
				}
				
				
				//Looping untuk menyimpan file PDF berdasarkan masing2 distribusi
				for(String s : distribusi.keySet()) {
					List<Map> daftar = distribusi.get(s);
					
					String cab_bank = "";
					if(s.equals("Bancass1")) cab_bank = "1";
					else if(s.equals("Bancass2")) cab_bank = "2";
					
					
					params.put("cab_bank", cab_bank);
					if(!daftar.isEmpty()) {

						//Bagian ini untuk menghasilkan file dalam bentuk pdf
//						String outputFilename = r + "_" + s + ".pdf";
//						JasperUtils.exportReportToPdf(
//								props.getProperty("report.uw.summary." + r) + ".jasper", 
//								outputDir, outputFilename, params, daftar, PdfWriter.AllowPrinting, null, null);
						
//						Bagian ini untuk menghasilkan file dalam bentuk Xcel
						String outputFilename = r + "_" + s + ".xls";
						JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
								outputDir, outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");

//						buat testing						
//						JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
//								props.getProperty("upload.dir"), outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");
						
						if(r.equals("Akseptasi_Khusus") || r.equals("Further_Requirements")) {
							List<File> attachments = daftarAttachment1.get(s);
							attachments.add(new File(outputDir + "\\" + outputFilename));
						}else {
							List<File> attachments = daftarAttachment2.get(s);
							attachments.add(new File(outputDir + "\\" + outputFilename));
						}						
					}
				}
			}
			Integer flag =0;
			//Looping untuk send email per masing2 distribusi
			for(String a : daftarAttachment1.keySet()) {
				List<File> attachments1 = daftarAttachment1.get(a);
				List<File> attachments2 = daftarAttachment2.get(a);
				String to = "";
				String cc = "";
				
				if(a.equals("Agency")||a.equals("Hybrid")||a.equals("Regional")){
					flag=flag+1;
					if (a.equals("Agency")){
						attachments2.add(new File(outputDir + "\\" + "Expired_Hybrid.xls"));
						attachments2.add(new File(outputDir + "\\" + "Expired_Regional.xls"));
					}else if (a.equals("Hybrid")){
						attachments2.add(new File(outputDir + "\\" + "Expired_Regional.xls"));
						attachments2.add(new File(outputDir + "\\" + "Expired_Agency.xls"));
					}else if (a.equals("Regional")){
						attachments2.add(new File(outputDir + "\\" + "Expired_Agency.xls"));
						attachments2.add(new File(outputDir + "\\" + "Expired_Hybrid.xls"));
					}
				}
				
				if(a.equals("Agency")||a.equals("Hybrid")||a.equals("Regional")) {
					to = "timmy"; 
					cc = "hayatin";
//					to = "fouresta;saputra;shopiah;"; 
//					cc = "ingrid;asriwulan";	
				}else if(a.equals("Bancass1")) { 
					to = "andhika"; 
					cc = "lufi";
//					to = "ariani;dinni;ery;ria;chizni";
//					cc = "ingrid;asriwulan;novie;ariani";
					
				}else if(a.equals("Bancass2")) { 
					to = "andhika"; 
					cc = "lufi";
//					to = "fikKi;novie;timmy;tities"; 
//					cc = "ingrid;asriwulan;novie;ariani";	
					
				}else if(a.equals("Bancass3")) { 
					to = "andhika"; 
					cc = "lufi";
//					to = "fikKi;novie;timmy;tities;ariani;dinni;ery;ria;chizni"; 
//					cc = "ingrid;asriwulan;novie;ariani";	
				}else if(a.equals("Worksite")) {
					to = "timmy"; 
					cc = "hayatin";
//					to = "fouresta;hadi;hanifah;hayatin;saputra;shopiah;tities;novie;"; 
//					cc = "ingrid;asriwulan;novie;ariani";	 			 
				}else if(a.equals("DMTM")) {
					to = "andhika"; 
					cc = "lufi";
//					to = "inge;apriyani;"; 
//					cc = "yusuf;ingrid;asriwulan;novie;ariani";	
						 
				}
				
				//to dan cc nya @sinarmasmsiglife.co.id
				String[] emailTo = to.split(";");
				String[] emailCc = cc.split(";");
				
				for(int y=0; y<emailTo.length; y++){
					emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
				}
				for(int y=0; y<emailCc.length; y++){
					emailCc[y] = emailCc[y].concat("@sinarmasmsiglife.co.id");
				}
				
				//E-mail 1 : Akseptasi_Khusus & Further_Requirements
				if(!attachments2.isEmpty()) {
					Integer file=attachments2.size();
					String me_id = sequence.sequenceMeIdEmail();
					if (a.equals("Agency")||a.equals("Hybrid")||a.equals("Regional")){
						if (flag==1){
							email.send(true, "ajsjava@sinarmasmsiglife.co.id",
								emailTo, 
								emailCc, 
								new String[]{"andhika@sinarmasmsiglife.co.id"},
								"List SPAJ Expired Agency/Hybrid/Regional  " + a + " s/d " + df.format(yesterday), 
								"List SPAJ Expired " + a + " s/d " + df.format(yesterday) +"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
								attachments2);							
							
//						EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
//								null, 0, 0, today, null, 
//								true, "ajsjava@sinarmasmsiglife.co.id", 
//								emailTo, 
//								emailCc, 
//								new String[]{"andhika@sinarmasmsiglife.co.id"}, 
////								new String[]{"Deddy@sinarmasmsiglife.co.id"},null,null,
//								"List SPAJ Expired Agency/Hybrid/Regional  " + a + " s/d " + df.format(yesterday), 
//								"List SPAJ Expired " + a + " s/d " + df.format(yesterday) +"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
//								attachments2);
						}else{
							
						}
					
					}else
					/*EmailPool.send(me_id,1, 1, 0, 0, 
							null, 0, 0, today, null, 
							true, "ajsjava@sinarmasmsiglife.co.id", 
							emailTo, 
							emailCc, 
							new String[]{"Deddy@sinarmasmsiglife.co.id"}, 
							"List SPAJ Expired  " + a + " s/d " + df.format(yesterday), 
						"List SPAJ Expired " + a + " s/d " + df.format(yesterday) + "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
							attachments2,null);*/
					
						email.send(true, "ajsjava@sinarmasmsiglife.co.id",
							emailTo, 
							emailCc, 
							new String[]{"andhika@sinarmasmsiglife.co.id"},
							"List SPAJ Expired Agency/Hybrid/Regional  " + a + " s/d " + df.format(yesterday), 
							"List SPAJ Expired " + a + " s/d " + df.format(yesterday) +"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
							attachments2);
				}
				
			}
		} catch (Exception e) {
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
			desc = "ERROR";
		}
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
		
	}
	
	/**
	 * Summary untuk status2 aksep yg masih terpending, seperti further dkk 
	 */
	public void schedulerSummaryAkseptasi(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		Date today = commonDao.selectSysdateTruncated(0);

		try {
			Date yesterday = commonDao.selectSysdateTruncated(-1);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			//Deddy (13/3/2009) tambahan untuk menentukan tahun -1 dari sekarang 
			DateFormat year = new SimpleDateFormat("yyyy");
			String yearbefore = year.format(FormatDate.add(today, Calendar.YEAR, -1) );
			
			//Deddy(16/3/2009) tambahan untuk menentukan bulan1-12 dari tahun sekarang
			DateFormat monthyear = new SimpleDateFormat("mmyyyy");
			String month1 = "01"+ year.format(today);
			String month2 = "02"+ year.format(today);
			String month3 = "03"+ year.format(today);
			String month4 = "04"+ year.format(today);
			String month5 = "05"+ year.format(today);
			String month6 = "06"+ year.format(today);
			String month7 = "07"+ year.format(today);
			String month8 = "08"+ year.format(today);
			String month9 = "09"+ year.format(today);
			String month10 = "10"+ year.format(today);
			String month11 = "11"+ year.format(today);
			String month12 = "12"+ year.format(today);
			
			
			logger.info("UW SCHEDULER AT " + new Date());
			long start = System.currentTimeMillis();
			
			//Report Summary (Menu dapat diakses di Entry > UW > Akseptasi Khusus > Summary)
			
			String outputDir = props.getProperty("pdf.dir.report") + "\\summary_akseptasi\\" + dateFormat.format(today) + "\\";
			//String outputDir = "D:\\Test\\";
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			params.put("tanggal", df.format(today));
			params.put("user", "SYSTEM");
			
			//passing parameter ke reportnya
			params.put("yearbefore", yearbefore);
			params.put("month1", month1);
			params.put("month2", month2);
			params.put("month3", month3);
			params.put("month4", month4);
			params.put("month5", month5);
			params.put("month6", month6);
			params.put("month7", month7);
			params.put("month8", month8);
			params.put("month9", month9);
			params.put("month10", month10);
			params.put("month11", month11);
			params.put("month12", month12);

			Map<String, List<Map>> distribusi 			= new HashMap<String, List<Map>>();
			Map<String, List<Map>> daftarReport 		= new HashMap<String, List<Map>>();
			Map<String, List<File>> daftarAttachment1 	= new HashMap<String, List<File>>();
			Map<String, List<File>> daftarAttachment2 	= new HashMap<String, List<File>>();
			
			daftarAttachment1.put("Agency", 	new ArrayList<File>());
			daftarAttachment1.put("Hybrid", 	new ArrayList<File>());
			daftarAttachment1.put("Regional", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass1", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass2", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass3", 	new ArrayList<File>());
			daftarAttachment1.put("BancassSimpol", 	new ArrayList<File>()); //Yusuf (24/10/2011) req Edy Kohar, simpol dipisah
			daftarAttachment1.put("Worksite", 	new ArrayList<File>());
			daftarAttachment1.put("DMTM", 		new ArrayList<File>());
			daftarAttachment1.put("MallAssurance", new ArrayList<File>());
			daftarAttachment1.put("MNC", new ArrayList<File>()); // Deddy(21/2/2013) req Novie via helpdesk
			daftarAttachment1.put("FCD", new ArrayList<File>()); // Deddy(21/2/2013) req Novie via helpdesk
			daftarAttachment1.put("sms1", new ArrayList<File>());
			
			daftarAttachment2.put("Agency", 	new ArrayList<File>());
			daftarAttachment2.put("Hybrid", 	new ArrayList<File>());
			daftarAttachment2.put("Regional", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass1", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass2", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass3", 	new ArrayList<File>());
			daftarAttachment2.put("BancassSimpol", 	new ArrayList<File>()); //Yusuf (24/10/2011) req Edy Kohar, simpol dipisah
			daftarAttachment2.put("Worksite", 	new ArrayList<File>());
			daftarAttachment2.put("DMTM", 		new ArrayList<File>());
			daftarAttachment2.put("MallAssurance", new ArrayList<File>());
			daftarAttachment2.put("MNC", new ArrayList<File>()); // Deddy(21/2/2013) req Novie via helpdesk
			daftarAttachment2.put("FCD", new ArrayList<File>()); // Deddy(21/2/2013) req Novie via helpdesk
			daftarAttachment2.put("sms1", new ArrayList<File>());
			
			String b;
			
			//Daftar Semua Report
			daftarReport.put("Akseptasi_Khusus", uwDao.selectDaftarAkseptasiNyangkut(10, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12, false)); 		//1. akseptasi khusus
			daftarReport.put("Further_Requirements", uwDao.selectDaftarAkseptasiNyangkut(3, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,false)); 	//2. further requirements -> lssa_id in (3,4,8)
			daftarReport.put("Akseptasi_Khusus_Sms", uwDao.selectDaftarAkseptasiNyangkut(10, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,true)); 		//1. akseptasi khusus
			daftarReport.put("Further_Requirements_Sms", uwDao.selectDaftarAkseptasiNyangkut(3, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,true)); 	//2. further requirements -> lssa_id in (3,4,8)
			daftarReport.put("Postpone", uwDao.selectDaftarAkseptasiNyangkut(9, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,false)); 				//3. postpone
			daftarReport.put("Decline", uwDao.selectDaftarAkseptasiNyangkut(2, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,false)); 				//4. decline
			//Deddy (26 Feb 2014) Req Novie via Helpdesk 48028 : Ditambahkan summary akseptasi
			daftarReport.put("Akseptasi", uwDao.selectDaftarAkseptasiNyangkut(5, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12, false)); 		//1. akseptasi
			daftarReport.put("Akseptasi_Sms", uwDao.selectDaftarAkseptasiNyangkut(5, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,true)); 		//1. akseptasi
			
			//Looping Utama dari daftar semua report
			for(String r : daftarReport.keySet()) {
				
				params.put("banyakMaunya", r);
				
				//Parameter Tambahan
				if(r.equals("Expired")) {
					params.put("judul", "Follow-Up Polis Expired");
				}else if(r.equals("Akseptasi") || r.equals("Akseptasi_Sms")) {
					params.put("judul", "Follow-Up Policy Accepted");
				}
				else if(r.equals("Akseptasi_Khusus") || r.equals("Akseptasi_Khusus_Sms")) {
					params.put("judul", "Follow-Up Akseptasi Khusus");
				}else if(r.equals("Further_Requirements") || r.equals("Further_Requirements_Sms")) {
					params.put("judul", "Follow-Up Further Requirement");
				}else if(r.equals("Postpone")) {
					params.put("judul", "Follow-Up Surat Konfirmasi Case Postpone");
				}else if(r.equals("Decline")) {
					params.put("judul", "Follow-Up Surat Konfirmasi Case Decline");
				}
				
				if(r.equals("Akseptasi_Khusus") || r.equals("Akseptasi_Khusus_Sms")) {
					params.put("note",
						"Data yang masuk disini adalah data:\n" +
						"* Polis diaksep dengan kondisi khusus (polis yang sudah diaksep tetapi masih diperlukan data tambahan)");
//						"Note:\n" +
//						"Data yang masuk disini adalah data :\n" +
//						"* Polis yang sudah dicetak, tetapi masih ada Further requirement. Polis diaksep dengan kondisi khusus.\n" + 
//						"Polis yang di 'Aksep dengan kondisi khusus' adalah polis yang diaksep ( dapat sudah langsung dicetak polis atau masih pending cetak polis )\n" +
//						"namun sebenarnya masih diperlukan data tambahan.");
				}else if(r.equals("Further_Requirements") || r.equals("Further_Requirements_Sms")) {
					params.put("note",
						"Note:\n" +
						"Data yang masuk disini adalah data :\n" +
						"* Polis yang belum diaksep, masih perlu data tambahan ( Further requirement )");
				}else if(r.equals("Akseptasi") || r.equals("Akseptasi_Sms")) {
					params.put("note",
							"Note:\n" +
							"Data yang masuk disini adalah data :\n" +
							"* Polis yang sudah diaksep.");
				}else {
					params.put("note", "");
				}
				
				//Daftar pembagian distribusi, direset setiap loop
				distribusi.put("Agency", 	new ArrayList<Map>());
				distribusi.put("Hybrid", 	new ArrayList<Map>());
				distribusi.put("Regional", 	new ArrayList<Map>());
				//distribusi.put("Bancass1", 	new ArrayList<Map>()); Lufi(03/12/2015) req Inge via helpdesk
				//distribusi.put("Bancass2", 	new ArrayList<Map>()); Lufi(03/12/2015) req Inge via helpdesk
				distribusi.put("Bancass3", 	new ArrayList<Map>());
				distribusi.put("BancassSimpol", 	new ArrayList<Map>()); //Yusuf (24/10/2011) req Edy Kohar, simpol dipisah
				distribusi.put("Worksite", 	new ArrayList<Map>());
				distribusi.put("DMTM", 		new ArrayList<Map>());
				//distribusi.put("MallAssurance", new ArrayList<Map>());Lufi(03/12/2015) req Inge via helpdesk
				distribusi.put("MNC", new ArrayList<Map>()); // Deddy(21/2/2013) req Novie via helpdesk
				distribusi.put("FCD", new ArrayList<Map>()); // Deddy(21/2/2013) req Novie via helpdesk
				distribusi.put("sms1", 	new ArrayList<Map>());
				
				List<Map> report = daftarReport.get(r);
				
				//Looping untuk membagi2 hasil query ke masing2 distribusi
				for(Map m : report) {
					String lca_id = (String) m.get("LCA_ID");
					String team_name = (String) m.get("TEAM_NAME");
					BigDecimal jenis = (BigDecimal) m.get("JN_BANK");
					
					BigDecimal templsbs = (BigDecimal) m.get("LSBS_ID");
					int lsbs_id = 0;
					if(templsbs != null) lsbs_id = templsbs.intValue();

					BigDecimal templsdbs = (BigDecimal) m.get("LSDBS_NUMBER");
					int lsdbs_number = 0;
					if(templsdbs != null) lsdbs_number = templsdbs.intValue();

					int jn_bank = (jenis == null ? 0 : jenis.intValue());
					
					if(team_name== null){
						team_name= "";
					}
					
					if("37,52".indexOf(lca_id)>-1) { //Agency
						((List<Map>) distribusi.get("Agency")).add(m);
					}else if(lca_id.equals("46")) { //Hybrid
						((List<Map>) distribusi.get("Hybrid")).add(m);
					}else if(lca_id.equals("09")) { //Bancassurance
						
//						Yusuf (24/10/2011) req Edy Kohar, simpol dipisah
//						Andhika (20/9/2013) update tambah sub bisnis (22,23,24)
						if(lsbs_id == 120 && (lsdbs_number==10 || lsdbs_number==11 || lsdbs_number==12 || lsdbs_number==22 || lsdbs_number==23 || lsdbs_number==24)){
							((List<Map>) distribusi.get("BancassSimpol")).add(m);
						}
						
						//Andhika (17/05/2013) SMiLe Link Satu
						if(lsbs_id == 120 && (lsdbs_number == 19 || lsdbs_number == 20 || lsdbs_number == 21)){
							((List<Map>) distribusi.get("sms1")).add(m);
						}else if(team_name.toUpperCase().equals("TEAM JAN ROSADI")) { //Bancassurance2
//							((List<Map>) distribusi.get("Bancass2")).add(m);
						}else if(team_name.toUpperCase().equals("TEAM DEWI")) { //Bancassurance3
							((List<Map>) distribusi.get("Bancass3")).add(m);
						}else if(team_name.toUpperCase().equals("TEAM YANTI SUMIRKAN")) { //Bancassurance1
//							((List<Map>) distribusi.get("Bancass1")).add(m);
						}
						
					}else if(lca_id.equals("08") || lca_id.equals("42")) { //Worksite
						((List<Map>) distribusi.get("Worksite")).add(m);
					}else if(lca_id.equals("55")) { //DM/TM
						((List<Map>) distribusi.get("DMTM")).add(m);
					}else if(lca_id.equals("58")) { //MallAssurance
//						((List<Map>) distribusi.get("MallAssurance")).add(m);
					}else if(lca_id.equals("62")) { //MallAssurance
						((List<Map>) distribusi.get("MNC")).add(m);
					}else if(lca_id.equals("65")) { //MallAssurance
						((List<Map>) distribusi.get("FCD")).add(m);
					}else { //Regional
						((List<Map>) distribusi.get("Regional")).add(m);
					}
				}
				
				
				//Looping untuk menyimpan file PDF berdasarkan masing2 distribusi
				for(String s : distribusi.keySet()) {
					List<Map> daftar = distribusi.get(s);
					
					String cab_bank = "";
					if(s.equals("Bancass1")) cab_bank = "1";
					else if(s.equals("Bancass2")) cab_bank = "2";
					
					
					params.put("cab_bank", cab_bank);
					if(!daftar.isEmpty()) {

						//Bagian ini untuk menghasilkan file dalam bentuk pdf
//						String outputFilename = r + "_" + s + ".pdf";
//						JasperUtils.exportReportToPdf(
//								props.getProperty("report.uw.summary." + r) + ".jasper", 
//								outputDir, outputFilename, params, daftar, PdfWriter.AllowPrinting, null, null);
						
//						Bagian ini untuk menghasilkan file dalam bentuk Xcel
						String outputFilename = r + "_" + s + ".xls";
						JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
								outputDir, outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");

//						buat testing						
//						JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
//								props.getProperty("upload.dir"), outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");
						
						if(r.equals("Akseptasi_Khusus") || r.equals("Further_Requirements") || r.equals("Akseptasi_Khusus_Sms") || r.equals("Further_Requirements_Sms") || r.equals("Akseptasi") || r.equals("Akseptasi_Sms")) {
							List<File> attachments = daftarAttachment1.get(s);
							attachments.add(new File(outputDir + "\\" + outputFilename));
						}else {
							List<File> attachments = daftarAttachment2.get(s);
							attachments.add(new File(outputDir + "\\" + outputFilename));
						}						
					}
				}
			}
			
			//Looping untuk send email per masing2 distribusi
			for(String a : daftarAttachment1.keySet()) {
				List<File> attachments1 = daftarAttachment1.get(a);
				List<File> attachments2 = daftarAttachment2.get(a);
				String to = "";
				String cc = "";
				String bcc= "";
				
				//Yusuf (28/02/2011) - Request Ariani via Email
				//Agency, Regional, Worksite = ariani;novie;dinni;shopiah
				//Bancass 1 & 2 = hayatin;tities

				if(a.equals("Agency")) {
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_AGENCY");
					//sub_section mst_config : SUMMARY_AKSEPTASI_AGENCY
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//					to = "siti_p;henny;yudi;yusy;iway;tedi"; //ADMIN AGENCY
//					cc = "ingrid;hanifah;fouresta;asriwulan;shopiah;" + //IT + UNDERWRITER 
//						 "keke;Yusup;martino;yudi;pingkan;pratidina"; //BAS
					
				}else if(a.equals("Hybrid")) {
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_HYBRID");
					//sub_section mst_config : SUMMARY_AKSEPTASI_HYBRID
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//					to = "yani;nofriani"; //ADMIN HYBRID
//					cc = "ingrid;asriwulan;hanifah;fouresta;shopiah;" + //IT + UNDERWRITER
//						 "keke;Yusup;martino"; //BAS
					
				}else if(a.equals("Regional")) {
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_REGIONAL");
					//sub_section mst_config : SUMMARY_AKSEPTASI_REGIONAL
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//					to = "henny;yudi;yusy;iway;tedi"; //ADMIN REGIONAL
//					cc = "ingrid;asriwulan;hanifah;fouresta;shopiah;" + //IT + UNDERWRITER
//						 "keke;Yusup;martino"; //BAS					
				}else if(a.equals("Bancass1")) { //Bu Yanty
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_BANCASS1");
					//sub_section mst_config : SUMMARY_AKSEPTASI_BANCASS1
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
////					to = "jelita;shima;iriana;" + //ADMIN BANCASS 1
////						 "yunita;" + //SALES SUPPORT BANCASS 1
////						 "yantisumirkan;nugroho.wonoadi"; //BOS BANCASS 1
////					cc = "UnderwritingBancass;" + //IT + UNDERWRITER
////						 "keke;Yusup;erwin_k;martino";//BAS
//				
				}				else if(a.equals("Bancass2")) { //Pak Jan
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_BANCASS2");
					//sub_section mst_config : SUMMARY_AKSEPTASI_BANCASS2
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//					to = "jelita;iriana;bancass_bandung;banc_semarang;" + //ADMIN BANCASS 2
//					 	 "bancass_medan;bancass_lampung;bancass_yogya;hayatin;tities;Edy;Yuliasari;Dwi.Pangestuti;nugroho.wonoadi;bancass_bsk01;Natalia"; //SALES SUPPORT BANCASS 2
//					cc = "underwritingbancass";
					
				}else if(a.equals("Bancass3")) { //Bu Dewi
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_BANCASS3");
					//sub_section mst_config : SUMMARY_AKSEPTASI_BANCASS3
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//					to = "jelita;iriana;adminbamdo;tien;Bancass_Bali;bancass_makassar;" + //ADMIN BANCASS 3
//						 "hendry;iriana;Edy;patricia;bancass_balikpapan;bancass_malang;nugroho.wonoadi;Natalia"; //SALES SUPPORT BANCASS 3
//					cc = "Underwritingbancass;dewiwijaya;hayatin;tities";
				}else if(a.equals("sms1")) {  // simas sekuritas
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_SMS");
					//sub_section mst_config : SUMMARY_AKSEPTASI_SMS
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//					to = "Jelita;Iriana;Natalia;bancass_jaktim02;Dwi.Pangestuti;";
//					cc = "Underwritingbancass;hayatin;tities";
				//Yusuf (24/10/2011) req Edy Kohar, simpol dipisah
				}else if(a.equals("BancassSimpol")) { //Simpol (bancass 2 / jan)
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_SIMPOL");
					//sub_section mst_config : SUMMARY_AKSEPTASI_SIMPOL
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//				  String bancassPrima="natalia;hendry;bancass_medan;" +
//							"tasya;iriana;bancass_bsk02;Bancass_surabaya;tien;" +
//							"adminbamdo;Bancass_bandung;Bancass_yogya;" +
//							"bancass_semarang;bancass_lampung;Bancass_bali;bancass_makassar;" +
//							"patricia;bancass_balikpapan;bancass_malang;Yuliasari";
//					to = "jelita;iriana;bancass_bsk01;" +bancassPrima;//ADMIN BANCASS 2				         	
//					cc = "underwritingbancass;jan;edy;";
				}else if(a.equals("Worksite")) {
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_WORKSITE");
					//sub_section mst_config : SUMMARY_AKSEPTASI_WORKSITE
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//					to = "asti;setyo"; //ADMIN WORKSITE
//					cc = "ingrid;novie;asriwulan;tities;shopiah;inge;devy;" + //IT + UNDERWRITER
//						 "keke;Yusup;martino"; //BAS
				}else if(a.equals("DMTM")) {
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_DMTM");
					//sub_section mst_config : SUMMARY_AKSEPTASI_DMTM
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//					to = "telemarketing"; //ADMIN DM/TM
//					cc = "UnderwritingDMTM;" + //IT + UNDERWRITER
//						 "keke;Yusup;grisye;martino"; //BAS
				}else if(a.equals("MallAssurance")) {
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_MALLASS");
					//sub_section mst_config : SUMMARY_AKSEPTASI_MALLASS
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//					to = "mallassurance"; //ADMIN MallAssurance
//					cc = "UnderwritingDMTM;" + //IT + UNDERWRITER
//						 "kartika_s;agustina"; //BAS & MallAss
				}else if(a.equals("MNC")) {
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_MNC");
					//sub_section mst_config : SUMMARY_AKSEPTASI_MNC
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//					to = "bimo;iway;yusy"; //BAS
//					cc = "novie;inge;tities;devy;ingrid"; //U/W
				}else if(a.equals("FCD")) {
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerSummaryAkseptasi", "SUMMARY_AKSEPTASI_FCD");
					//sub_section mst_config : SUMMARY_AKSEPTASI_FCD
					to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString():null;
					cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString():null;
					bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString():null;
//					to = "hernawan;suryani;yune;frans_s"; //BAS
//					cc = "novie;inge;tities;devy;ingrid"; // U/W
				}
				
				//to dan cc nya @sinarmasmsiglife.co.id
				String[] emailTo = to!=null?to.split(";"):null;
				String[] emailCc = cc!=null?cc.split(";"):null;
				String[] emailBcc = bcc!=null?bcc.split(";"):null;
				
				//E-mail 1 : Akseptasi_Khusus & Further_Requirements
				if(!attachments1.isEmpty()) {
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, today, null, 
							true, "ajsjava@sinarmasmsiglife.co.id", 
							emailTo, emailCc, emailBcc, 
//							new String[]{"Deddy@sinarmasmsiglife.co.id"},null,null,
							"Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS / POLICY ACCEPTED " + a + " s/d " + df.format(yesterday), 
							"Berikut adalah Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS / POLICY ACCEPTED " + a + " s/d " + df.format(yesterday) + 
							"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", attachments1, null);
				}

				//E-mail 2 : Postpone & Decline
				if(!attachments2.isEmpty()) {
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, today, null, 
							true, "ajsjava@sinarmasmsiglife.co.id", 
							emailTo, emailCc, emailBcc, 
//							new String[]{"Deddy@sinarmasmsiglife.co.id"},null,null,
							"Summary Follow-Up Surat Konfirmasi Case DECLINED / POSTPONED " + a + " s/d " + df.format(yesterday), 
							"Berikut adalah Summary Follow-Up Surat Konfirmasi Case DECLINED / POSTPONED " + a + " s/d " + df.format(yesterday) + 
							"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", attachments2, null);
				}
					
			}
			
			//Bila yang diatas meng-email summary nya, maka yang dibawah ini meng-email satu per satu ke admin bersangkutan
			
			//Daftar Semua Report
			daftarReport = new HashMap<String, List<Map>>();
			daftarReport.put("Akseptasi_Khusus", uwDao.selectDaftarAkseptasiNyangkut(10, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12, false)); 		//1. akseptasi khusus
			daftarReport.put("Further_Requirements", uwDao.selectDaftarAkseptasiNyangkut(3, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,false)); 	//2. further requirements -> lssa_id in (3,4,8)
			daftarReport.put("Akseptasi_Khusus_Sms", uwDao.selectDaftarAkseptasiNyangkut(10, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,true)); 		//1. akseptasi khusus
			daftarReport.put("Further_Requirements_Sms", uwDao.selectDaftarAkseptasiNyangkut(3, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,true)); 	//2. further requirements -> lssa_id in (3,4,8)
			daftarReport.put("Postpone", uwDao.selectDaftarAkseptasiNyangkut(9, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,false)); 				//3. postpone
			daftarReport.put("Decline", uwDao.selectDaftarAkseptasiNyangkut(2, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,false)); 				//4. decline
			//Deddy (26 Feb 2014) Req Novie via Helpdesk 48028 : Ditambahkan summary akseptasi
			daftarReport.put("Akseptasi", uwDao.selectDaftarAkseptasiNyangkut(5, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12, false)); 		//1. akseptasi
			daftarReport.put("Akseptasi_Sms", uwDao.selectDaftarAkseptasiNyangkut(5, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,true)); 		//1. akseptasi
			
			
			for(String r : daftarReport.keySet()) {
				
				params.put("banyakMaunya", r);
				
				List<Map> report = daftarReport.get(r); //akseptasi, further, postpone, decline
				Map<String, List<Map>> daftarEmail = new HashMap<String, List<Map>>(); 
				String tmp = "";
				List<Map> reportTmp = new ArrayList<Map>();
				
				//pecah2 dari masing2 report ke masing2 email
				for(Map row : report) {
					String lar_email = (String) row.get("LAR_EMAIL");
					String lca_id = (String) row.get("LCA_ID");
					
					if(lar_email == null)lar_email = "";
					if(lca_id == null)lca_id = "";
					
					BigDecimal templsbs = (BigDecimal) row.get("LSBS_ID");
					int lsbs_id = 0;
					if(templsbs != null) lsbs_id = templsbs.intValue();
					
					BigDecimal templsdbs = (BigDecimal) row.get("LSDBS_NUMBER");
					int lsdbs_number = 0;
					if(templsdbs != null) lsdbs_number = templsdbs.intValue();

					//Yusuf (24/10/11) Req Edy Kohar, Simpol dipisah, diemail ke pincab
					if(lca_id.equals("09") && lsbs_id == 120 && (lsdbs_number==10 || lsdbs_number==11 || lsdbs_number==12 || lsdbs_number==22 || lsdbs_number==23 || lsdbs_number==24)){ 
						lar_email = "jelita@sinarmasmsiglife.co.id;edy@sinarmasmsiglife.co.id;jan@sinarmasmsiglife.co.id;iriana@sinarmasmsiglife.co.id;Bancass_bsk01@sinarmasmsiglife.co.id";
						Map emailcab = uwDao.selectEmailCabangBSM();
						if(emailcab != null){
							String emailCab = (String) emailcab.get("EMAIL_CAB");
							if(emailCab != null) lar_email += ";" + emailCab;
							String emailHead = (String) emailcab.get("EMAIL_HEAD");
							if(emailHead != null) lar_email += ";" + emailHead;
						}
					}
					
				/*	if(lca_id.equals("40")){
						lar_email=uwDao.selectEmailCabangPenutup("449",2);//Default ambil lar_id 449
					}*/
					
					if(!tmp.equals(lar_email)) {
						if(!tmp.equals("")) daftarEmail.put(tmp, reportTmp);
						reportTmp = new ArrayList<Map>();
						tmp = lar_email;
					}
					reportTmp.add(row);
				}
				if(!reportTmp.isEmpty()) {
					daftarEmail.put(tmp, reportTmp);
				}

				//untuk setiap email, generate report, lalu kirim email
				for(String e : daftarEmail.keySet()) {
//					String outputFilename = r + ".pdf";
					String outputFilename = r + ".xls";
					
					//Parameter Tambahan
					if(!r.equals("Akseptasi_Khusus") && !r.equals("Further_Requirements") && !r.equals("Akseptasi_Khusus_Sms") && !r.equals("Further_Requirements_Sms") && !r.equals("Akseptasi") && !r.equals("Akseptasi_Sms")) {
						params.put("judul", "Follow-Up Surat Konfirmasi Case " + r);
					}else if(r.equals("Akseptasi_Khusus") || r.equals("Akseptasi_Khusus_Sms")) {
						params.put("judul", "Follow-Up Akseptasi Khusus");
					}else if(r.equals("Further_Requirements") || r.equals("Further_Requirements_Sms")) {
						params.put("judul", "Follow-Up Further Requirement");
					}else if(r.equals("Akseptasi") || r.equals("Akseptasi_Sms")){
						params.put("judul", "Follow-Up Policy Accepted");
					}else {
						params.put("judul", "Follow-Up " + r);
					}
					
					if(r.equals("Akseptasi_Khusus") || r.equals("Akseptasi_Khusus_Sms")) {
						params.put("note",
							"Data yang masuk disini adalah data:\n" +
							"* Polis diaksep dengan kondisi khusus (polis yang sudah diaksep tetapi masih diperlukan data tambahan)");
//							"Note:\n" +
//							"Data yang masuk disini adalah data :\n" +
//							"* Polis yang sudah dicetak, tetapi masih ada Further requirement. Polis diaksep dengan kondisi khusus.\n" + 
//							"Polis yang di 'Aksep dengan kondisi khusus' adalah polis yang diaksep ( dapat sudah langsung dicetak polis atau masih pending cetak polis )\n" +
//							"namun sebenarnya masih diperlukan data tambahan.");
					}else if(r.equals("Further_Requirements") || r.equals("Further_Requirements_Sms")) {
						params.put("note",
							"Note:\n" +
							"Data yang masuk disini adalah data :\n" +
							"* Polis yang belum diaksep, masih perlu data tambahan ( Further requirement )");
					}else if(r.equals("Akseptasi") || r.equals("Akseptasi_Sms")) {
						params.put("note",
								"Note:\n" +
								"Data yang masuk disini adalah data :\n" +
								"* Polis yang sudah diaksep.");
					}else {
						params.put("note", "");
					}
					
					//generate report
//					JasperUtils.exportReportToPdf(
//							props.getProperty("report.uw.summary." + r) + ".jasper", 
//							outputDir + e + "\\", outputFilename, params, daftarEmail.get(e), PdfWriter.AllowPrinting, null, null);
					
					String[] emailTo = e!=null?e.split(";"):null;
					if(emailTo!=null){
						for(String to : emailTo){
							JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
									outputDir + to.trim() + "\\", outputFilename, params, daftarEmail.get(e), props.getProperty("report.uw.summary.sub.total")+ ".jasper");
						}
					}
					
//					buat testing
//					JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
//					props.getProperty("upload.dir"), outputFilename, params, daftarEmail.get(e), props.getProperty("report.uw.summary.sub.total")+ ".jasper");
					
					List<File> attachments = new ArrayList<File>();
					attachments.add(new File(outputDir + "\\" + e + "\\" + outputFilename));

					//special case, buat bos-bos
					//1. ibu widya, bosnya jatim1 (LCA_ID = 05)
					if(e.toUpperCase().contains("JATIM") || 
							e.toUpperCase().contains("BANJARMASIN") ||
							e.toUpperCase().contains("JEMBER") ||
							e.toUpperCase().contains("MALANG") ||
							e.toUpperCase().contains("MATARAM") ||
							e.toUpperCase().contains("KEDIRI") ||
							e.toUpperCase().contains("BALIKPAPAN") ||
							e.toUpperCase().contains("SAMARINDA") ||
							e.toUpperCase().contains("JAWA TENGAH") ||
							e.toUpperCase().contains("DUTA MANDIRI AGENCY") ||
							e.toUpperCase().contains("CHAMPION II AGENCY") ||
							e.toUpperCase().contains("INFOBEST SETIA AGENCY") ||
							e.toUpperCase().contains("AIFA") ||
							e.toUpperCase().contains("LIBRA") ||
							e.toUpperCase().contains("BORNEO") ||
							e.toUpperCase().contains("JAVA AGENCY")){
						e.concat(";widyawati@sinarmasmsiglife.co.id");
					}

					if(!attachments.isEmpty()) {
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
								null, 0, 0, today, null, 
								true, "ajsjava@sinarmasmsiglife.co.id", 
								e.split(";"), null, null,//new String[] {props.getProperty("admin.yusuf")},  
//								new String[]{"Deddy@sinarmasmsiglife.co.id"},null,null,
								"Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQ / POLICY ACCEPTED / DECLINED / POSTPONED s/d " + df.format(yesterday), 
								"Berikut adalah Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQ / DECLINED / POSTPONED s/d " + df.format(yesterday) + 
								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", attachments, null);
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR";
			err=e.getLocalizedMessage();
			StringBuffer stackTrace = new StringBuffer();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			stackTrace.append(sw);
			try {
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, today, null, 
						false, "ajsjava@sinarmasmsiglife.co.id", 
						new String[] {"deddy@sinarmasmsiglife.co.id","ryan@sinarmasmsiglife.co.id"}, null, null,//new String[] {props.getProperty("admin.yusuf")},   
						"ERROR pada Scheduler Akseptasi E-Lions " , 
						"Pesan Error : "+ stackTrace.toString(), null, null);
			} catch (MailException e1) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e1);
			} 
		}		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}		
	}
	
	/**
	 * Summary untuk status2 aksep PAS yg masih terpending, seperti further dkk 
	 */
	public void schedulerSummaryFurtherRequirementPas(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

		try {
			Date yesterday = commonDao.selectSysdateTruncated(-1);
			Date today = commonDao.selectSysdateTruncated(0);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			
			//Deddy (13/3/2009) tambahan untuk menentukan tahun -1 dari sekarang 
			DateFormat year = new SimpleDateFormat("yyyy");
			String yearbefore = year.format(FormatDate.add(today, Calendar.YEAR, -1) );
			
			//Deddy(16/3/2009) tambahan untuk menentukan bulan1-12 dari tahun sekarang
			DateFormat monthyear = new SimpleDateFormat("mmyyyy");
			String month1 = "01"+ year.format(today);
			String month2 = "02"+ year.format(today);
			String month3 = "03"+ year.format(today);
			String month4 = "04"+ year.format(today);
			String month5 = "05"+ year.format(today);
			String month6 = "06"+ year.format(today);
			String month7 = "07"+ year.format(today);
			String month8 = "08"+ year.format(today);
			String month9 = "09"+ year.format(today);
			String month10 = "10"+ year.format(today);
			String month11 = "11"+ year.format(today);
			String month12 = "12"+ year.format(today);
			
			
			//logger.info("UW SCHEDULER AT " + new Date());
			//long start = System.currentTimeMillis();
			
			//Report Summary (Menu dapat diakses di Entry > UW > Akseptasi Khusus > Summary)
			
			String outputDir = props.getProperty("pdf.dir.report") + "\\pas\\";
			//String outputDir = "D:\\Test\\";
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			params.put("tanggal", df.format(today));
			params.put("user", "SYSTEM");
			
			//passing parameter ke reportnya
			params.put("yearbefore", yearbefore);
			params.put("month1", month1);
			params.put("month2", month2);
			params.put("month3", month3);
			params.put("month4", month4);
			params.put("month5", month5);
			params.put("month6", month6);
			params.put("month7", month7);
			params.put("month8", month8);
			params.put("month9", month9);
			params.put("month10", month10);
			params.put("month11", month11);
			params.put("month12", month12);

			Map<String, List<Map>> distribusi 			= new HashMap<String, List<Map>>();
			Map<String, List<Map>> daftarReportTemp 		= new HashMap<String, List<Map>>();
			Map<String, List<File>> daftarAttachment1 	= new HashMap<String, List<File>>();
			Map<String, List<File>> daftarAttachment2 	= new HashMap<String, List<File>>();
			Map<String, List<Map>> daftarReport;
			
			daftarAttachment1.put("Agency", 	new ArrayList<File>());
			daftarAttachment1.put("Hybrid", 	new ArrayList<File>());
			daftarAttachment1.put("Regional", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass1", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass2", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass3", 	new ArrayList<File>());
			daftarAttachment1.put("Worksite", 	new ArrayList<File>());
			daftarAttachment1.put("DMTM", 		new ArrayList<File>());
			daftarAttachment1.put("MallAssurance", new ArrayList<File>());
			
			daftarAttachment2.put("Agency", 	new ArrayList<File>());
			daftarAttachment2.put("Hybrid", 	new ArrayList<File>());
			daftarAttachment2.put("Regional", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass1", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass2", 	new ArrayList<File>());
			daftarAttachment2.put("Bancass3", 	new ArrayList<File>());
			daftarAttachment2.put("Worksite", 	new ArrayList<File>());
			daftarAttachment2.put("DMTM", 		new ArrayList<File>());
			daftarAttachment2.put("MallAssurance", new ArrayList<File>());
			
			String b;
			//Daftar Semua Report
			//daftarReport.put("Akseptasi_Khusus", uwDao.selectDaftarAkseptasiNyangkut(10, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 		//1. akseptasi khusus
//			daftarReport.put("Further_Requirements", uwDao.selectDaftarAkseptasiNyangkut(3, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 	//2. further requirements -> lssa_id in (3,4,8)
			//daftarReport.put("Postpone", uwDao.selectDaftarAkseptasiNyangkut(9, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 				//3. postpone
			//daftarReport.put("Decline", uwDao.selectDaftarAkseptasiNyangkut(2, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 				//4. decline
			
			daftarReportTemp.put("Further_Requirements_Pas", uwDao.selectFurtherRequirementPas(3, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 	//2. further requirements -> lssa_id in (3,4,8)
			
			List<Map> reportPasList = uwDao.selectFurtherRequirementPas(3, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12);
			List<List<Map>> reportPasListx = new ArrayList<List<Map>>();
			
			int lusIdNode = 0, idx = 0;
			if(reportPasList.size() > 0){
				lusIdNode = ((BigDecimal) reportPasList.get(0).get("LUS_ID")).intValue();
			}
			
			for(int i = 0 ; i < reportPasList.size() ; i++){
				if(i == 0){//(i = 0)
					reportPasListx.add(new ArrayList<Map>());
					reportPasListx.get(idx).add(reportPasList.get(i));
				}else{//(i = 1) sampai (i < reportPasList.size())
					int lus_id = ((BigDecimal) reportPasList.get(i).get("LUS_ID")).intValue();
					if(lusIdNode == lus_id){// kalau lus_id sebelumnya sama dengan lus_id saat ini -> tambah ke list
						reportPasListx.get(idx).add(reportPasList.get(i));
					}else if(lusIdNode != lus_id){// kalau lus_id sebelumnya beda dengan lus_id saat ini -> buat list baru dan tambahkan
						idx++;
						lusIdNode = lus_id;
						reportPasListx.add(new ArrayList<Map>());
						reportPasListx.get(idx).add(reportPasList.get(i));
					}
				}
			}
			
			for(int i = 0 ; i < reportPasListx.size() ; i++){
			daftarReport 		= new HashMap<String, List<Map>>();
			daftarAttachment1 	= new HashMap<String, List<File>>();
			daftarAttachment1.put("Agency", 	new ArrayList<File>());
			daftarAttachment1.put("Hybrid", 	new ArrayList<File>());
			daftarAttachment1.put("Regional", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass1", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass2", 	new ArrayList<File>());
			daftarAttachment1.put("Bancass3", 	new ArrayList<File>());
			daftarAttachment1.put("Worksite", 	new ArrayList<File>());
			daftarAttachment1.put("DMTM", 		new ArrayList<File>());
			daftarAttachment1.put("MallAssurance", new ArrayList<File>());
			daftarReport.put("Further_Requirements_Pas", reportPasListx.get(i));
			
			//Looping Utama dari daftar semua report
			for(String r : daftarReport.keySet()) {
				
				params.put("banyakMaunya", r);
				
				//Parameter Tambahan
				if(r.equals("Further_Requirements_Pas")) {
					params.put("judul", "Follow-Up Further Requirement");
				}
				
				if(r.equals("Further_Requirements_Pas")) {
					params.put("note",
						"Note:\n" +
						"Data yang masuk disini adalah data :\n" +
						"* Pas yang belum diaksep, masih perlu data tambahan ( Further requirement )");
				}else {
					params.put("note", "");
				}
				
				//Daftar pembagian distribusi, direset setiap loop
				distribusi.put("Agency", 	new ArrayList<Map>());
				distribusi.put("Hybrid", 	new ArrayList<Map>());
				distribusi.put("Regional", 	new ArrayList<Map>());
				distribusi.put("Bancass1", 	new ArrayList<Map>());
				distribusi.put("Bancass2", 	new ArrayList<Map>());
				distribusi.put("Bancass3", 	new ArrayList<Map>());
				distribusi.put("Worksite", 	new ArrayList<Map>());
				distribusi.put("DMTM", 		new ArrayList<Map>());
				distribusi.put("MallAssurance", new ArrayList<Map>());
				
				List<Map> report = daftarReport.get(r);
				
				//Looping untuk membagi2 hasil query ke masing2 distribusi
				for(Map m : report) {
					
						((List<Map>) distribusi.get("Regional")).add(m);
				}
				
				
				//Looping untuk menyimpan file PDF berdasarkan masing2 distribusi
				for(String s : distribusi.keySet()) {
					List<Map> daftar = distribusi.get(s);
					
					String cab_bank = "";
					if(s.equals("Bancass1")) cab_bank = "1";
					else if(s.equals("Bancass2")) cab_bank = "2";
					
					
					params.put("cab_bank", cab_bank);
					if(!daftar.isEmpty()) {

						//Bagian ini untuk menghasilkan file dalam bentuk pdf
//						String outputFilename = r + "_" + s + ".pdf";
//						JasperUtils.exportReportToPdf(
//								props.getProperty("report.uw.summary." + r) + ".jasper", 
//								outputDir, outputFilename, params, daftar, PdfWriter.AllowPrinting, null, null);
						
//						Bagian ini untuk menghasilkan file dalam bentuk Xcel
						String outputFilename = r + "_" + s + sdf.format(today) + "(" + reportPasListx.get(i).get(0).get("LCA_NAMA") + ")" + i+".xls";
						JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
								outputDir, outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");

//						buat testing						
//						JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
//								props.getProperty("upload.dir"), outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");
						
						if(r.equals("Akseptasi_Khusus") || r.equals("Further_Requirements_Pas")) {
							List<File> attachments = daftarAttachment1.get(s);
							attachments.add(new File(outputDir + "\\" + outputFilename));
						}else {
							List<File> attachments = daftarAttachment2.get(s);
							attachments.add(new File(outputDir + "\\" + outputFilename));
						}						
					}
					
					// send email
					List<File> attachments1 = new ArrayList<File>();
					attachments1 = daftarAttachment1.get(s);
					//List<File> attachments2 = daftarAttachment2.get(s);
					String to = "";
					String cc = "";
						
					List<String> reportPasEmailList = new ArrayList<String>();
					reportPasEmailList = uwDao.selectReportPasEmailList(((BigDecimal) reportPasListx.get(i).get(0).get("LUS_ID")).intValue());
					String[] emailList = new String[reportPasEmailList.size()];
					for(int j = 0 ; j < reportPasEmailList.size() ; j++){
						emailList[j] = reportPasEmailList.get(j);
					}

					
					//E-mail 1 : Akseptasi_Khusus & Further_Requirements_Pas
//					if(emailList[0] == null){
//						if(!attachments1.isEmpty()) {
//							email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
//									emailList, null, new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
//									//new String[]{"Deddy@sinarmasmsiglife.co.id,Yusup_a@sinarmasmsiglife.co.id"},null,null,
//									"Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS " + s + " s/d " + df.format(yesterday), 
//									"Berikut adalah Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS " + s + " s/d " + df.format(yesterday) + 
//									"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", attachments1);
//						}
//					}
						if(!attachments1.isEmpty()) {
							EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, new Date(), null, false,
									"ajsjava@sinarmasmsiglife.co.id",emailList, new String[]{"hayatin@sinarmasmsiglife.co.id", "dinni@sinarmasmsiglife.co.id", "fouresta@sinarmasmsiglife.co.id", "ingrid@sinarmasmsiglife.co.id", "asriwulan@sinarmasmsiglife.co.id"},
									new String[]{"andy@sinarmasmsiglife.co.id", "berto@sinarmasmsiglife.co.id"},
									"Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS " + s + " s/d " + df.format(yesterday), 
									"Berikut adalah Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS " + s + " s/d " + df.format(yesterday) + 
									"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", attachments1, null);
						}
				}
			}
			
			
			}
			
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
		}	
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
		
	}
	
	/**
	 * Request Hendra (BAS) 4/3/09 - terkait pencetakan polis di cabang.
	 */
	public void schedulerSummaryTerlambatCetakPolis(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

		Date sysdate = commonDao.selectSysdateTruncated(0);
		
		//cc ke orang2 BAS
		String cc = "Yune@sinarmasmsiglife.co.id;Martino@sinarmasmsiglife.co.id;Desy@sinarmasmsiglife.co.id";
		//String cc = "ryan@sinarmasmsiglife.co.id;";
		
		///Yusuf (13 Aug 09) - Request Hendra via email, untuk cabang2 tertentu ada cabang induknya, misalnya Jatim I
		List<DropDown> cabangInduk = new ArrayList<DropDown>();
		//(10 May 2011)sementara email jatim1 diganti ke lusia@sinarmasmsiglife.co.id karena cabang sedang direnovasi.
		//cabangInduk.add(new DropDown("05", "jatim1@sinarmasmsiglife.co.id")); //Regional JATIM I : Malang, Kediri, Jember, Mataram, Banjarmasin
		cabangInduk.add(new DropDown("05", "lusia@sinarmasmsiglife.co.id"));
		//1. Polis2 yang H+1 dari tgl valid belum dicetak juga oleh cabang
		/*
		 * Kondisi2nya adalah : 
		 * - LSPD_ID < 8
		 * - LCA_ID <> '09'
		 * - MSPO_DATE_PRINT IS NULL
		 * - MSTE_TGL_VALID_PRINT IS NOT NULL
		 * - FLAG_PRINT_CABANG = 1
		 * - Selisih antara TGL VALID dgn SYSDATE > 1 
		 */
		List<Map> summary = uwDao.selectPolisBelumDicetak();
		for(Map m : summary){
			Date mste_tgl_valid_print = (Date) m.get("MSTE_TGL_VALID_PRINT");
			
			String bcc = "Hendra@sinarmasmsiglife.co.id";

			String lca_id = ((String) m.get("LCA_ID")).trim();
			for(DropDown c : cabangInduk){
				if(c.getKey().equals(lca_id)){
					bcc += ";" + c.getValue();
				}
			}
			
			int selisih = (int) FormatDate.dateDifference(mste_tgl_valid_print, sysdate, true);
			if(selisih > 1){
				String mspo_policy_no_format = (String) m.get("MSPO_POLICY_NO_FORMAT");
				//String reg_spaj = (String) m.get("REG_SPAJ");
				String emailCabang = (String) m.get("LAR_EMAIL");
				String emailCabangPrint="";
				if(emailCabang.contains("Jatim1") || emailCabang.contains("malang")||emailCabang.contains("mataram")||emailCabang.contains("kediri")){
					emailCabangPrint="Jatim1@branch.sinarmasmsiglife.co.id";
				}
				if(emailCabang.contains("sukabumi") || emailCabang.contains("garut")||emailCabang.contains("tasikmalaya")){
					emailCabangPrint="bandung@branch.sinarmasmsiglife.co.id";
				}
				if(emailCabang != null){
					try {
						email.send(false, 
								props.getProperty("admin.ajsjava"), new String[]{emailCabang,emailCabangPrint}, cc.split(";"), 
								bcc.split(";"), 
								"Harap segera lakukan pencetakan Polis " + mspo_policy_no_format, 
								"Harap segera lakukan pencetakan Polis " + mspo_policy_no_format + "! Pencetakan Polis ini sudah terlambat " + selisih + " hari sejak di-VALID oleh UNDERWRITING.", 
								null);
					} catch (MailException e) {
						logger.error("ERROR :", e);
					} catch (MessagingException e) {
						logger.error("ERROR :", e);
					}
				}
			}
		}
		
		//2. Polis2 yang H+1 dari tgl cetak polis belum ditransfer juga oleh cabang
		/*
		 * Kondisi2nya adalah : 
		 * - LSPD_ID = 6
		 * - LCA_ID <> '09'
		 * - MSPO_DATE_PRINT IS NOT NULL
		 * - MSTE_TGL_VALID_PRINT IS NOT NULL
		 * - FLAG_PRINT_CABANG = 1
		 * - Selisih antara TGL CETAK dgn SYSDATE > 1 
		 */
		List<Map> summary2 = uwDao.selectPolisBelumDitransfer();
		for(Map m : summary2){
			Date mspo_date_print = (Date) m.get("MSPO_DATE_PRINT");

			String lca_id = (String) m.get("LCA_ID");
			String bcc = "Hendra@sinarmasmsiglife.co.id";
			for(DropDown c : cabangInduk){
				if(c.getKey().equals(lca_id)){
					bcc += ";" + c.getValue();
				}
			}

			int selisih = (int) FormatDate.dateDifference(mspo_date_print, sysdate, true);
			if(selisih > 1){
				String mspo_policy_no_format = (String) m.get("MSPO_POLICY_NO_FORMAT");
				//String reg_spaj = (String) m.get("REG_SPAJ");
				String emailCabang = (String) m.get("LAR_EMAIL");
				if(emailCabang != null){
					try {
						email.send(false, 
								props.getProperty("admin.ajsjava"), new String[]{emailCabang}, cc.split(";"),  
								bcc.split(";"), 
								"Harap segera lakukan transfer Polis " + mspo_policy_no_format, 
								"Harap segera lakukan transfer Polis " + mspo_policy_no_format + "! Transfer Polis ini sudah terlambat " + selisih + " hari sejak dilakukan pencetakan Polis.", 
								null);
					} catch (MailException e) {
						logger.error("ERROR :", e);
						err=e.getLocalizedMessage();
					} catch (MessagingException e) {
						logger.error("ERROR :", e);
						err=e.getLocalizedMessage();
					}
				}
			}
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
		
	}
	
	/**
	 * Scheduler Kirim report ke BSM pertanggal 15 atau tgl 17 jika tgl 14 hari sabtu, 
	 * & tgl 1 bln berikut nya
	 * 
	 * @throws Exception
	 *
	 * @author Yusup_A
	 * @since Jul 31, 2009 (2:25:02 PM)
	 */
	public void summaryAkseptasiBSM(String msh_name) throws DataAccessException {
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		
		Calendar curr = Calendar.getInstance();
		curr.setTime(new Date());
		//int lastDay = curr.getActualMaximum(Calendar.DAY_OF_MONTH);
		int halfMonth = 15;
		String lastMonthLastDay = "";
		Calendar curr2 = Calendar.getInstance();
		curr2.set(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH), halfMonth);
		
		if(curr2.get(Calendar.DAY_OF_WEEK) == 1) halfMonth = 17;
		//else if (curr2.get(Calendar.DAY_OF_WEEK) == 7) halfMonth = 17;
		if(halfMonth == curr.get(Calendar.DATE)) {
			curr2.set(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH)-1, 1);
			lastMonthLastDay = (new Integer(curr2.getActualMaximum(Calendar.DAY_OF_MONTH))).toString()
								+"/"+
								(new Integer(curr2.get(Calendar.MONTH)+1)).toString()
								+"/"+
								(new Integer(curr2.get(Calendar.YEAR))).toString();
			//lastMonthLastDay = "30/7/2009";
		}
		//if(halfMonth == curr.get(Calendar.DATE) || 1 == curr.get(Calendar.DATE)) {
			try {
				Date yesterday = commonDao.selectSysdateTruncated(-1);
				Date today = commonDao.selectSysdateTruncated(0);
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				
				//Deddy (13/3/2009) tambahan untuk menentukan tahun -1 dari sekarang 
				DateFormat year = new SimpleDateFormat("yyyy");
				String yearbefore = year.format(FormatDate.add(today, Calendar.YEAR, -1) );
				
				//Deddy(16/3/2009) tambahan untuk menentukan bulan1-12 dari tahun sekarang
				DateFormat monthyear = new SimpleDateFormat("mmyyyy");
				String month1 = "01"+ year.format(today);
				String month2 = "02"+ year.format(today);
				String month3 = "03"+ year.format(today);
				String month4 = "04"+ year.format(today);
				String month5 = "05"+ year.format(today);
				String month6 = "06"+ year.format(today);
				String month7 = "07"+ year.format(today);
				String month8 = "08"+ year.format(today);
				String month9 = "09"+ year.format(today);
				String month10 = "10"+ year.format(today);
				String month11 = "11"+ year.format(today);
				String month12 = "12"+ year.format(today);
				
				logger.info("UW SCHEDULER KHUSUS BSM AT " + new Date());
				long start = System.currentTimeMillis();
				
				String outputDir = props.getProperty("pdf.dir.report") + "\\summary_akseptasi\\" + dateFormat.format(today) + "\\";
				
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				params.put("tanggal", df.format(today));
				params.put("user", "SYSTEM");
				
				//passing parameter ke reportnya
				params.put("yearbefore", yearbefore);
				params.put("month1", month1);
				params.put("month2", month2);
				params.put("month3", month3);
				params.put("month4", month4);
				params.put("month5", month5);
				params.put("month6", month6);
				params.put("month7", month7);
				params.put("month8", month8);
				params.put("month9", month9);
				params.put("month10", month10);
				params.put("month11", month11);
				params.put("month12", month12);			

				Map<String, List<Map>> distribusi 			= new HashMap<String, List<Map>>();
				Map<String, List<Map>> daftarReport 		= new HashMap<String, List<Map>>();
				Map<String, List<File>> daftarAttachment  = new HashMap<String, List<File>>();
				
				//Daftar Semua Report
				daftarReport.put("Akseptasi_Khusus", uwDao.selectDaftarAkseptasiNyangkutTemp(10, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,lastMonthLastDay)); 		//1. akseptasi khusus
				daftarReport.put("Further_Requirements", uwDao.selectDaftarAkseptasiNyangkutTemp(3, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12,lastMonthLastDay)); 	//2. further requirements -> lssa_id in (3,4,8)
				
				//Looping Utama dari daftar semua report
				for(String r : daftarReport.keySet()) {
					
					params.put("banyakMaunya", r);
					
					//Parameter Tambahan
					if(r.equals("Expired")) {
						params.put("judul", "Follow-Up Polis Expired");
					}
					else if(r.equals("Akseptasi_Khusus")) {
						params.put("judul", "Follow-Up Akseptasi Khusus");
					}else if(r.equals("Further_Requirements")) {
						params.put("judul", "Follow-Up Further Requirement");
					}else if(r.equals("Postpone")) {
						params.put("judul", "Follow-Up Surat Konfirmasi Case Postpone");
					}else if(r.equals("Decline")) {
						params.put("judul", "Follow-Up Surat Konfirmasi Case Decline");
					}
					
					if(r.equals("Akseptasi_Khusus")) {
						params.put("note",
							"Data yang masuk disini adalah data:\n" +
							"* Polis diaksep dengan kondisi khusus (polis yang sudah diaksep tetapi masih diperlukan data tambahan)");
//							"Note:\n" +
//							"Data yang masuk disini adalah data :\n" +
//							"* Polis yang sudah dicetak, tetapi masih ada Further requirement. Polis diaksep dengan kondisi khusus.\n" + 
//							"Polis yang di 'Aksep dengan kondisi khusus' adalah polis yang diaksep ( dapat sudah langsung dicetak polis atau masih pending cetak polis )\n" +
//							"namun sebenarnya masih diperlukan data tambahan.");
					}else if(r.equals("Further_Requirements")) {
						params.put("note",
							"Note:\n" +
							"Data yang masuk disini adalah data :\n" +
							"* Polis yang belum diaksep, masih perlu data tambahan ( Further requirement )");
					}else {
						params.put("note", "");
					}
					
					//Daftar pembagian distribusi, direset setiap loop
					distribusi.put("Bancass",   new ArrayList<Map>());
					
					List<Map> report = daftarReport.get(r);
					
					//Looping untuk membagi2 hasil query ke masing2 distribusi
					for(Map m : report) {
						String lca_id = (String) m.get("LCA_ID");
						String team_name = (String) m.get("TEAM_NAME");
						BigDecimal jenis = (BigDecimal) m.get("JN_BANK");
						int jn_bank = (jenis == null ? 0 : jenis.intValue());
						
						if(team_name== null){
							team_name= "";
						}
						else if(lca_id.equals("09")) { //Bancassurance
							((List<Map>) distribusi.get("Bancass")).add(m);
						}
					}
					
					List<Map> temp = new ArrayList<Map>();
					Map<String, List<Map>> cabang = new HashMap<String, List<Map>>();
					String key = "";
					for(int a=0;a< distribusi.get("Bancass").size();a++) {
						if(distribusi.get("Bancass").get(a).get("KODE_BSM") != null) {
							if(!key.equals(distribusi.get("Bancass").get(a).get("KODE_BSM"))) {
								if(key.equals("")) {
									temp.add(distribusi.get("Bancass").get(a));
									key = distribusi.get("Bancass").get(a).get("KODE_BSM").toString();
								}
								else {
									cabang.put(key, temp);
									temp = new ArrayList<Map>();
									temp.add(distribusi.get("Bancass").get(a));
									key = distribusi.get("Bancass").get(a).get("KODE_BSM").toString();
								}
							}
							else {
								temp.add(distribusi.get("Bancass").get(a));
							}						
						}
					}
					
					//Looping untuk menyimpan file PDF berdasarkan masing2 distribusi
					for(String s : cabang.keySet()) {
						List<Map> daftar = cabang.get(s);
						
						String cab_bank = "";
						if(s.equals("Bancass1")) cab_bank = "1";
						else if(s.equals("Bancass2")) cab_bank = "2";
						
						
						params.put("cab_bank", cab_bank);
						if(!daftar.isEmpty()) {

							//Bagian ini untuk menghasilkan file dalam bentuk pdf
//							String outputFilename = r + "_" + s + ".pdf";
//							JasperUtils.exportReportToPdf(
//									props.getProperty("report.uw.summary." + r) + ".jasper", 
//									outputDir, outputFilename, params, daftar, PdfWriter.AllowPrinting, null, null);
							
//							Bagian ini untuk menghasilkan file dalam bentuk Xcel
							String outputFilename = r + "_" + s + ".xls";
							JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
									outputDir, outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");

//							buat testing						
//							JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
//									props.getProperty("upload.dir"), outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");
							
							if(daftarAttachment.get(s) == null) {
								List<File> x = new ArrayList<File>();
								x.add(new File(outputDir + "\\" + outputFilename));
								daftarAttachment.put(s,x);
							}
							else {
								List<File> y   = daftarAttachment.get(s);
								y.add(new File(outputDir + "\\" + outputFilename));
								daftarAttachment.put(s,y);
							}
						}
					}
				}
				
				//email semua report
				for (String s : daftarAttachment.keySet()) {
					List<File> attachments = daftarAttachment.get(s);
					List<Map> daftarEmail = uwDao.selectEmailPincab(s);
					
					String to = "";
					String cc = "";
					String[] emailto = new String[] {};
					String[] emailcc = new String[] {};
					
					//String pincab = daftarEmail.get(0).get("EM_PINCAB").toString();
					//if(daftarEmail.get(0).get("KODE").toString().trim().equals("BSD")) pincab = "astrid.tambunan@banksinarmas.com";
					//else if(daftarEmail.get(0).get("KODE").toString().trim().equals("FTM")) pincab = "hans.felix@banksinarmas.com";
					//else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SRG")) pincab = "deddy.t.wiharja@banksinarmas.com";
					//else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SKB")) pincab = "richard@banksinarmas.com";
					
					to = daftarEmail.get(0).get("EM_PINCAB").toString();
					emailto = to.split(";");
					if(halfMonth == curr.get(Calendar.DATE) || 1 == curr.get(Calendar.DATE)) {
//						to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"miranti.d.putri@banksinarmas.com"};
//						if(daftarEmail.get(0).get("KODE").toString().trim().equals("BSD")) to = new String[]{"ming.aswaty@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("FTM")) to = new String[]{"niken.wulan@banksinarmas.com","cs059@banksinarmas.com","ro059@banksinarmas.com","niken.wulan@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SRG")) to = new String[]{"deddy.t.wiharja@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SKB")) to = new String[]{"enung.zarkasih@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("TNA")) to = new String[]{"antonius.gustaman@banksinarmas.com","Jackson@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("LPG")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"Bancass_Lampung@sinarmasmsiglife.co.id","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PLU")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"maya.natalia@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("KUD")) to = new String[]{"koengfoe15@yahoo.com","kanthi.nalarantini@banksinarmas.com",daftarEmail.get(0).get("EM_PINCAB").toString(),"miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PLM")) to = new String[]{"sudrajat@banksinarmas.com","cs041@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MDN")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"cs015@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PWT")) to = new String[]{"andreas.s.andrianto@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("HAS")) to = new String[]{"linawati.sudarmo@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SKJ")) to = new String[]{"laurencia.y.gunawan@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("JTT")) to = new String[]{"yanti@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("RXM")) to = new String[]{"cs009@banksinarmas.com","cindy.chandra@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SER")) to = new String[]{"hans.felix@banksinarmas.com","cs033@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("DIP")) to = new String[]{"subiantoro@banksinarmas.com","widha.s.wijaya@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MND")) to = new String[]{"stella.sumual@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("BTM")) to = new String[]{"hotma.situmeang@banksinarmas","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("KRW")) to = new String[]{"cs036@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SPG")) to = new String[]{"cs070@banksinarmas.co","evelin.mailoa@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("RPT")) to = new String[]{"jony@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("JYP")) to = new String[]{"hendra.purwanto@banksinarmas.com","Raymond.pancadarma@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("BKS")) to = new String[]{"hendry.lee@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SEN")) to = new String[]{"handoko@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("ZAF")) to = new String[]{"irawati.rusli@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MGD")) to = new String[]{"marcellina.nelly@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MLG")) to = new String[]{"nina.s.rejeki@banksinarmas.com ","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PTK")) to = new String[]{"yenni@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("GBG")) to = new String[]{"cs046@banksinarmas.com","eni.maratus@banksinarmas.com","bambang.w.tanojo@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SMR")) to = new String[]{"CS077@banksinarmas.com","alberth.tanner@banksinarmas.com","cornelius.noviady@banksinarmas.com","harman.darsono@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("JMD")) to = new String[]{"devi.c.wongso@banksinarmas.com","cs058@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MAD")) to = new String[]{"rinie.suzana@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("GTS")) to = new String[]{"nengah.citayasa@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("JBR")) to = new String[]{"laurencia.y.gunawan@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("KRK")) to = new String[]{"miranti.d.putri@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MSP")) to = new String[]{"aji.alim@banksinarmas.com","miranti.d.putri@banksinarmas.com"};
						
						emailcc = new String[] {"hayatin@sinarmasmsiglife.co.id","inge@sinarmasmsiglife.co.id"};

						StringBuffer pesan = new StringBuffer();
						pesan.append("Berikut adalah Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS Bank Sinarmas "+ daftarEmail.get(0).get("NAMA_CAB").toString().trim() +" s/d "+df.format(yesterday));
						pesan.append("<br/><br/><br/><br/>");
						pesan.append("Salam,");
						pesan.append("<br/>");
						pesan.append("PT Asuransi Jiwa Sinarmas MSIG Tbk.");
						pesan.append("<br/>");
						pesan.append("Underwriting Departement");
						pesan.append("<br/>");
						pesan.append("[ (021)6257808 / ext. 8711 ]");
					
						
						email.send(true, "hayatin@sinarmasmsiglife.co.id", 
								emailto,
								emailcc,
								new String[]{"yusup_a@sinarmasmsiglife.co.id"},
								//new String[] {"yusup_a@sinarmasmsiglife.co.id"},	
								//new String[]{},new String[]{},new String[]{"yusup_a@sinarmasmsiglife.co.id"},
								"Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS Bank Sinarmas "+ daftarEmail.get(0).get("NAMA_CAB") +" s/d " + df.format(yesterday), 
								pesan.toString(), attachments);	
					}
					else if(2 == curr.get(Calendar.DAY_OF_WEEK)){
//						to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString()};
//						if(daftarEmail.get(0).get("KODE").toString().trim().equals("BSD")) to = new String[]{"ming.aswaty@banksinarmas.com","cs011@banksinarmas.com","RO011@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("FTM")) to = new String[]{"niken.wulan@banksinarmas.com","cs059@banksinarmas.com","ro059@banksinarmas.com","niken.wulan@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SRG")) to = new String[]{"deddy.t.wiharja@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SKB")) to = new String[]{"enung.zarkasih@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("TNA")) to = new String[]{"antonius.gustaman@banksinarmas.com","Jackson@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("LPG")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"Bancass_Lampung@sinarmasmsiglife.co.id"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PLU")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"maya.natalia@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("KUD")) to = new String[]{"koengfoe15@yahoo.com","kanthi.nalarantini@banksinarmas.com",daftarEmail.get(0).get("EM_PINCAB").toString()};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PLM")) to = new String[]{"sudrajat@banksinarmas.com","cs041@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MDN")) to = new String[]{daftarEmail.get(0).get("EM_PINCAB").toString(),"cs015@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PWT")) to = new String[]{"andreas.s.andrianto@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("HAS")) to = new String[]{"linawati.sudarmo@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SKJ")) to = new String[]{"laurencia.y.gunawan@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("JTT")) to = new String[]{"yanti@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("RXM")) to = new String[]{"cs009@banksinarmas.com","cindy.chandra@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SER")) to = new String[]{"hans.felix@banksinarmas.com","cs033@banksinarmas.com "};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("DIP")) to = new String[]{"subiantoro@banksinarmas.com","widha.s.wijaya@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MND")) to = new String[]{"stella.sumual@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("BTM")) to = new String[]{"hotma.situmeang@banksinarmas"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("KRW")) to = new String[]{"cs036@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SPG")) to = new String[]{"cs070@banksinarmas.co","evelin.mailoa@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("RPT")) to = new String[]{"jony@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("JYP")) to = new String[]{"hendra.purwanto@banksinarmas.com","Raymond.pancadarma@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("BKS")) to = new String[]{"hendry.lee@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SEN")) to = new String[]{"handoko@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("ZAF")) to = new String[]{"irawati.rusli@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MGD")) to = new String[]{"marcellina.nelly@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MLG")) to = new String[]{"nina.s.rejeki@banksinarmas.com "};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("PTK")) to = new String[]{"yenni@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("GBG")) to = new String[]{"cs046@banksinarmas.com","eni.maratus@banksinarmas.com","bambang.w.tanojo@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("SMR")) to = new String[]{"CS077@banksinarmas.com","alberth.tanner@banksinarmas.com","cornelius.noviady@banksinarmas.com","harman.darsono@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("JMD")) to = new String[]{"devi.c.wongso@banksinarmas.com","cs058@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MAD")) to = new String[]{"rinie.suzana@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("GTS")) to = new String[]{"nengah.citayasa@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("JBR")) to = new String[]{"laurencia.y.gunawan@banksinarmas.com"};
//						else if(daftarEmail.get(0).get("KODE").toString().trim().equals("MSP")) to = new String[]{"aji.alim@banksinarmas.com"};
						
						emailcc = new String[] {"hayatin@sinarmasmsiglife.co.id","inge@sinarmasmsiglife.co.id"};

						StringBuffer pesan = new StringBuffer();
						pesan.append("Berikut adalah Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS Bank Sinarmas "+ daftarEmail.get(0).get("NAMA_CAB").toString().trim() +" s/d "+df.format(yesterday));
						pesan.append("<br/><br/><br/><br/>");
						pesan.append("Salam,");
						pesan.append("<br/>");
						pesan.append("PT Asuransi Jiwa Sinarmas MSIG Tbk.");
						pesan.append("<br/>");
						pesan.append("Underwriting Departement");
						pesan.append("<br/>");
						pesan.append("[ (021)6257807 / ext. 8711 ]");
					
						
						email.send(true, "hayatin@sinarmasmsiglife.co.id", 
								emailto,
								emailcc,
								new String[]{"yusup_a@sinarmasmsiglife.co.id"},
								//new String[] {"yusup_a@sinarmasmsiglife.co.id"},	
								//new String[]{},new String[]{},new String[]{"yusup_a@sinarmasmsiglife.co.id"},
								"Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS Bank Sinarmas "+ daftarEmail.get(0).get("NAMA_CAB") +" s/d " + df.format(yesterday), 
								pesan.toString(), attachments);	
					}
					
				}
				
				long end = System.currentTimeMillis();
				logger.info("UW SCHEDULER BSM FINISHED AT " + new Date() + " in " + ( (float) (end-start) / 1000) + " SECONDS.");
			}catch (Exception e) {
				logger.error("ERROR :", e);
				err=e.getLocalizedMessage();
				desc = "ERROR";
			}			
		//}
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
			
	}
	
	public void schedulerCekPermintaanSpaj(String msh_name) throws DataAccessException {
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		
		List<Map> data = new ArrayList<Map>();
		data = basDao.selectPermintaanSpajBlomAccept();
		
		StringBuffer hasil = new StringBuffer();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		HashMap mapEmail = uwDao.selectMstConfig(6, "emailNotifPermintaanSPAJ", "emailNotifPermintaanSPAJ");
		String[] to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
		String[] cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
		String[] bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
		
		hasil.append("Permintaan spaj yang belum diproses sampai dengan kemaren\n");
		hasil.append("----------------------------------------------------------\n\n");
		for(int a=0;a<data.size();a++) {
			hasil.append("Nama : "+data.get(a).get("LUS_FULL_NAME")+"\n");
			hasil.append("Cabang : "+data.get(a).get("LCA_NAMA")+"\n");
			hasil.append("No Permintaan : "+data.get(a).get("MSF_ID")+"\n");
			hasil.append("Tgl Permintaan : "+df.format(data.get(a).get("MSFH_DT"))+"\n\n");
		}
		
		try {
			
			EmailPool.send("Konfirmasi Permintaan Spaj", 1, 1, 0, 0, null, 0, 0, new Date(), null, true, 
					props.getProperty("admin.ajsjava"), 
					to, 
					cc, 
					bcc, 
					"Konfirmasi Permintaan Spaj Tanggal " + df.format(new Date()), 
					hasil.toString() + "NB: Informasi permintaan spaj ini di-email secara otomatis melalui sistem E-Lions.", 
					null, 
					null);
			
		}catch(Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);err=e.getLocalizedMessage();
			try {
				EmailPool.send("Konfirmasi Permintaan Spaj", 1, 1, 0, 0, null, 0, 0, new Date(), null, true, 
						props.getProperty("admin.ajsjava"), 
						bcc, 
						null, 
						null, 
						"ERROR Konfirmasi Permintaan Spaj Tanggal " + df.format(new Date()), 
						sw.toString(), 
						null, 
						null);
				
				 desc	= "ERROR";
			} catch (MailException e1) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e1);
			}
		}	
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}		
	}
	
	public void schedulerBlmPertgjwbnSpaj(String msh_name) throws DataAccessException {
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		
		List<Map> data = new ArrayList<Map>();
		data = basDao.selectPermintaanSpajBlomPertgjwbn();
		String namaAdmin = "";
		String mail = "";
		String[] to = new String[] {};
		String[] cc = new String[] {};
 		
		StringBuffer hasil = new StringBuffer();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");		
		
		for(int a=0;a<data.size();a++) {
			if(!namaAdmin.equals(data.get(a).get("LUS_FULL_NAME").toString())) {
				if(!namaAdmin.equals("") && !namaAdmin.equals(data.get(a).get("LUS_FULL_NAME").toString())) {
					if(mail.equals("")) {
						to = cc;
						cc = new String[] {};
					}
					/*else if(data.get(a).get("LUS_FULL_NAME").toString().equals("JOSEPHINE")) {
						to = new String[] {"iway@sinarmasmsiglife.co.id"};
						cc = new String[] {"pratidina@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id",
								"martino@sinarmasmsiglife.co.id"};
					}*/
					else if(data.get(a).get("LUS_FULL_NAME").toString().equals("NOVI AGUSTINA")) {
						to = new String[] {"yudi@sinarmasmsiglife.co.id"};
						cc = new String[] {"pratidina@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id",
								"martino@sinarmasmsiglife.co.id"};
					}
					else if(data.get(a).get("LUS_FULL_NAME").toString().equals("Denni Nofriani Saragih")) {
						to = new String[] {"redy@sinarmasmsiglife.co.id"};
						cc = new String[] {"pratidina@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id",
								"martino@sinarmasmsiglife.co.id"};
					}
					else if(data.get(a).get("LUS_FULL_NAME").toString().equals("SRI SURYANI")) {
						to = new String[] {"redy@sinarmasmsiglife.co.id"};
						cc = new String[] {"pratidina@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id",
								"martino@sinarmasmsiglife.co.id"};
					}
					else if(data.get(a).get("LUS_FULL_NAME").toString().equals("LING KWAN")) {
						to = new String[] {"redy@sinarmasmsiglife.co.id"};
						cc = new String[] {"pratidina@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id",
								"martino@sinarmasmsiglife.co.id"};
					}
					else {
						to = new String[] {mail};
						cc = new String[] {"pratidina@sinarmasmsiglife.co.id","desy@sinarmasmsiglife.co.id",
								"martino@sinarmasmsiglife.co.id"};
					}
					
					try {
						email.send(false, 
								props.getProperty("admin.ajsjava"), 
								to,
								//new String[] {"yusup_a@sinarmasmsiglife.co.id"},
								cc, 
								new String[] {},
								//null,null,
								"Konfirmasi Belum Pertanggungjawaban Spaj Tanggal " + df.format(new Date()), 
								hasil.toString() + "NB: Informasi pertanggungjawaban spaj ini di-email secara otomatis melalui sistem E-Lions.", 
								null);
						logger.info("Email");
						
					}catch(Exception e) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						err=e.getLocalizedMessage();
						/*try {
							email.send(false, 
									props.getProperty("admin.ajsjava"), 
									new String[] {"yusup_a@sinarmasmsiglife.co.id"}, 
									new String[] {}, new String[] {}, 
									"ERROR Konfirmasi Pertanggungjawaban Spaj Tanggal " + df.format(new Date()), 
									sw.toString(),
									null);
							 desc	= "ERROR";
						} catch (MailException e1) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e1);
						} catch (MessagingException e1) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e1);
						}*/
						logger.info("Error");
					}					
					hasil = new StringBuffer();
				}
				hasil.append("Permintaan spaj yang belum dipertanggungjawabkan sampai dengan hari ini\n");
				hasil.append("--------------------------------------------------------------------\n\n");
				hasil.append("Admin : "+data.get(a).get("LUS_FULL_NAME")+"\n");
				hasil.append("Cabang : "+data.get(a).get("LCA_NAMA")+"\n");
				namaAdmin = data.get(a).get("LUS_FULL_NAME").toString();
				if(data.get(a).get("LUS_EMAIL") != null) mail = data.get(a).get("LUS_EMAIL").toString();
				else mail = "";
			}
			hasil.append("     Agent : "+data.get(a).get("MSAB_NAMA")+" ["+data.get(a).get("MSAG_ID")+"]\n");
			hasil.append("     No Permintaan              : "+data.get(a).get("MSF_ID")+"\n");
			hasil.append("     Jenis Permintaan           : "+data.get(a).get("LSJS_DESC")+"\n");
			hasil.append("     No BLanko                  : "+data.get(a).get("LSJS_PREFIX")+" "+data.get(a).get("NO_BLANKO_REQ")+"\n");
			hasil.append("     Tgl Permintaan             : "+df.format(data.get(a).get("MSSD_DT"))+"\n");
			hasil.append("     Tgl Max Pertanggungjawaban : "+df.format(data.get(a).get("MAX_DATE"))+"\n\n");
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}		
	}
	
	public void schedulercetakPolisMa(String msh_name) throws DataAccessException {
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		Connection conn = null;

		try {
			conn = this.getDataSource().getConnection();
			Calendar today = Calendar.getInstance();
			today.setTime(bdate);
			today.add(Calendar.DATE, -1);
			
			List<Map> data = new ArrayList<Map>(), data2 = new ArrayList<Map>();
			data = uwDao.selectCetakPolisMaKemarin();
			data2 = uwDao.selectPolisMaBlmAcp();	
			StringBuffer mssg = new StringBuffer();
			List<File> attachments = new ArrayList<File>();
			
			if(data.size() != 0 || data2.size() != 0) {
				String outputDir = props.getProperty("pdf.dir.report") + "\\ctk_polis_ma\\"; //+ dateFormat.format(today)+ "\\";
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				if(data.size() != 0) {
					String outputFilename = "cp_ma_"+today.get(Calendar.YEAR)+
											 FormatString.rpad("0",String.valueOf((today.get(Calendar.MONTH)+1)),2)+
											 FormatString.rpad("0",String.valueOf((today.get(Calendar.DATE))),2) + ".xls";
					JasperUtils.exportReportToXls(
					props.getProperty("report.uw.ctk_polis_ma") + ".jasper",
					outputDir, outputFilename, null, conn);
					attachments.add(new File(outputDir + "\\" + outputFilename));
					mssg.append("Laporan Polis issue Mallassurance sd. "+(today.get(Calendar.DATE))+"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR)+"<br/>");						
				}
				else {
					mssg.append("Tidak ada Laporan Polis issue Mallassurance sd. "+(today.get(Calendar.DATE))+"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR)+"<br/>");
				}
				if(data2.size() != 0) {
					String outputFilename = "cp_ma_blm_tnd_trm_"+today.get(Calendar.YEAR)+
											FormatString.rpad("0",String.valueOf((today.get(Calendar.MONTH)+1)),2)+
											FormatString.rpad("0",String.valueOf((today.get(Calendar.DATE))),2) + ".xls";
					JasperUtils.exportReportToXls(
					props.getProperty("report.uw.ctk_polis_ma_bef_acp") + ".jasper",
					outputDir, outputFilename, null, conn);
					attachments.add(new File(outputDir + "\\" + outputFilename));
					mssg.append("Laporan Polis pending sd "+(today.get(Calendar.DATE))+"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR)+"<br/>");						
				}
				else {
					mssg.append("Tidak ada Laporan Polis pending sd "+(today.get(Calendar.DATE))+"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR)+"<br/>");
				}

			}
			else {
				mssg.append("Tidak ada Laporan Polis issue Mallassurance sd. "+(today.get(Calendar.DATE)-1)+"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR)+"<br/>");
				mssg.append("Tidak ada Laporan Polis pending sd "+(today.get(Calendar.DATE)-1)+"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR)+"<br/>");
			}
			mssg.append("<br/><br/><br/><br/>*Pesan ini dikirim secara otomatis dari sistem E-Lions");
			
			email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[] {"Mallassurance@sinarmasmsiglife.co.id"},
					new String[] {"UnderwritingDMTM@sinarmasmsiglife.co.id"},
					new String[] {"deddy@sinarmasmsiglife.co.id"},
//					new String[]{},new String[]{},new String[]{"deddy@sinarmasmsiglife.co.id"},
					"Cetak Polis MallAssurance untuk tanggal "+(today.get(Calendar.DATE))+"/"+(today.get(Calendar.MONTH)+1)+"/"+today.get(Calendar.YEAR), 
					mssg.toString(), attachments);	
		
		}catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR";
			err=e.getLocalizedMessage();
		}finally{
			closeConnection(conn);
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
			
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/*
	 * direct link : http://elions.sinarmasmsiglife.co.id/common/util.htm?window=kurs
	 */
	public void schedulerDailyCurr(String msh_name)  throws DataAccessException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate 	= new Date();
		String err="";
		CurrencyScheduler c = new CurrencyScheduler(commonDao, false);
		
		//apabila tidak berhasil, email error
		if(c.getEmailMessage().trim().equals("")) {
			try {
				Date nowDate = commonDao.selectSysdate();
				String pesan = "Harap hubungi IT untuk mengkonfirmasi hal ini. Terima kasih.";
				EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, true, "ajsjava@sinarmasmsiglife.co.id", new String[]{"lilyana@sinarmasmsiglife.co.id","rika@sinarmasmsiglife.co.id","aprina@sinarmasmsiglife.co.id","pawestri@sinarmasmsiglife.co.id","suryandari@sinarmasmsiglife.co.id","sisca.r@sinarmasmsiglife.co.id","Saphry@sinarmasmsiglife.co.id","FebiP@sinarmasmsiglife.co.id","Risky@sinarmasmsiglife.co.id","virgin@sinarmasmsiglife.co.id","liana@sinarmasmsiglife.co.id"},
						new String[]{"himmia@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id", "vonny_t@sinarmasmsiglife.co.id","kuseno@sinarmasmsiglife.co.id","auditya@sinarmasmsiglife.co.id","ryan@sinarmasmsiglife.co.id","andy@sinarmasmsiglife.co.id"}, null, 
						"PERHATIAN! ERROR saat penarikan kurs dari website BI per tanggal " + df.format(new Date()), pesan, null, null);
				
			} catch (MailException e1) {
				// TODO Auto-generated catch block
				err=e1.getLocalizedMessage();
				logger.error("ERROR :", e1);
			}
		
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						"SCHEDULER KURS BI", bdate, new Date(), "ERROR",err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
			
		//apabila berhasil, email
		}else {
			try {
				Date nowDate = commonDao.selectSysdate();
				EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
						"ajsjava@sinarmasmsiglife.co.id", 
//						new String[] {"deddy@sinarmasmsiglife.co.id"},
//						new String[] {"deddy@sinarmasmsiglife.co.id"}, new String[]{},							
						new String[]{"lilyana@sinarmasmsiglife.co.id","rika@sinarmasmsiglife.co.id","aprina@sinarmasmsiglife.co.id","pawestri@sinarmasmsiglife.co.id","suryandari@sinarmasmsiglife.co.id","sisca.r@sinarmasmsiglife.co.id","Saphry@sinarmasmsiglife.co.id","FebiP@sinarmasmsiglife.co.id","Risky@sinarmasmsiglife.co.id","virgin@sinarmasmsiglife.co.id","liana@sinarmasmsiglife.co.id"}, 
						new String[]{"himmia@sinarmasmsiglife.co.id","vonny_t@sinarmasmsiglife.co.id","ridhaal@sinarmasmsiglife.co.id","auditya@sinarmasmsiglife.co.id","ryan@sinarmasmsiglife.co.id"}, 
						new String[]{}, 
						"Konfirmasi Kurs Tanggal " + df.format(new Date()), 
						c.getEmailMessage().trim() + "\nNB: Informasi kurs ini di-email secara otomatis melalui sistem E-Lions.", 
						null, null);
				
			}catch(Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				err=e.getLocalizedMessage();
				try {
					Date nowDate = commonDao.selectSysdate();
					EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, nowDate, null, false,
							"ajsjava@sinarmasmsiglife.co.id", 
							new String[]{"lilyana@sinarmasmsiglife.co.id","rika@sinarmasmsiglife.co.id","aprina@sinarmasmsiglife.co.id","pawestri@sinarmasmsiglife.co.id","suryandari@sinarmasmsiglife.co.id","sisca.r@sinarmasmsiglife.co.id","Saphry@sinarmasmsiglife.co.id","FebiP@sinarmasmsiglife.co.id","Risky@sinarmasmsiglife.co.id","margareth@sinarmasmsiglife.co.id","virgin@sinarmasmsiglife.co.id","edmond@sinarmasmsiglife.co.id","liana@sinarmasmsiglife.co.id"}, 
							new String[]{"himmia@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id", "vonny_t@sinarmasmsiglife.co.id","kuseno@sinarmasmsiglife.co.id","auditya@sinarmasmsiglife.co.id","ryan@sinarmasmsiglife.co.id","andy@sinarmasmsiglife.co.id"}, 
							new String[]{}, 
							"ERROR Konfirmasi Kurs Tanggal " + df.format(new Date()), 
							sw.toString(), 
							null, null);
				} catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
				}
			}
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						msh_name, bdate, new Date(), "OK",err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}				
		}
	}
	
	public void schedulerDailyTTP(String msh_name) throws DataAccessException{

		Date bdate = new Date();
		String desc = "OK";
		String err="";
		Connection conn = null;		

		try {
			Date yesterday = commonDao.selectSysdateTruncated(-1);
			Date today = commonDao.selectSysdateTruncated(0);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			conn = this.getDataSource().getConnection();

			String outputDir = props.getProperty("pdf.dir.report")
					+ "\\lap_ttp_harian\\" + dateFormat.format(today)
					+ "\\";

			Map<String, Comparable> params = new HashMap<String, Comparable>();
			List<String> daftarReport = new ArrayList<String>();
			List<File> daftarAttachment = new ArrayList<File>();

			// Daftar Semua Report
			daftarReport.add("lap_ttp_harian");
			daftarReport.add("lap_ttp_bulanan");
			daftarReport.add("lap_ttp_tahunan");
			
			// Looping Utama dari daftar semua report, export dalam bentuk XLS, masukkan dalam daftar attachment untuk di email
			for (int i=0; i<daftarReport.size(); i++) {
				String reportName = daftarReport.get(i);
				JasperUtils.exportReportToXls(
						props.getProperty("report.uw.summary." + reportName) + ".jasper",
						outputDir,  reportName + ".xls", params, conn);
				
				daftarAttachment.add(new File(outputDir + "\\" + reportName + ".xls"));
			}
			
			// email semua report dalam 1 email
			StringBuffer pesan = new StringBuffer();
			pesan.append("Berikut Adalah Summary Proses Input TTP " + df.format(yesterday));
			pesan.append("<br/><br/><br/><br/>");

			email.send(true, "admin.ajsjava",
					new String[] {
						"ingrid@sinarmasmsiglife.co.id",
						"asriwulan@sinarmasmsiglife.co.id" },
					null, null, 
					"Summary Proses Input TTP " + " s/d " + df.format(yesterday), pesan.toString(), daftarAttachment);
		} catch (Exception e) {
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
			desc = "ERROR";
		} finally{
			closeConnection(conn);
		}
		
		try {
			insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(), msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	public void schedulerIHSG(String msh_name) throws DataAccessException{

		Date bdate = new Date();
		String desc = "OK";
		String err="";

		try {
			IHSGScheduler i = new IHSGScheduler(this.financeDao);
		} catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR";
			err=e.getLocalizedMessage();
		}
		try {
			insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(), msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	public void scheduler() throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

	
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					"SCHEDULER SUMMARY EXPIRED", bdate, new Date(), desc,"");
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	  /**
	 * Daily Report - Pengiriman SPH/SPT ke LB 
	 */
	public void schedulerPengirimanSPHSPTKeLB() throws DataAccessException{
		String desc	= "OK";
		
		Date dateEarly = commonDao.selectSysdateTrunc();
		dateEarly.setDate(1);
		Date nowDate = commonDao.selectSysdateTrunc();
		String tanggalReport = FormatDate.toIndonesian(nowDate);
		//scheduler untuk Report Pengiriman SPH SPT Ke LB
			
			try{
				DateFormat df = new SimpleDateFormat("ddMMyyyy");
				DateFormat day = new SimpleDateFormat("dd");
				DateFormat bulan = new SimpleDateFormat("MM");
				DateFormat dfParams = new SimpleDateFormat("dd/MM/yyyy");
				DateFormat dfAwalTahun = new SimpleDateFormat("01/01/yyyy");
				String tanggalAwal = dfParams.format(dateEarly);
				String tanggal = dfParams.format(nowDate);
				String formatDate = df.format(nowDate);
				String bulanAwal = dfAwalTahun.format(nowDate);
				BigDecimal perHari = new BigDecimal(day.format(nowDate));
				BigDecimal perBulan = new BigDecimal(bulan.format(nowDate));
				List<Map> selectUserDailyKirimSphSptKeLbMonthlyScheduler = selectUserKirimSphSptKeLbScheduler(tanggalAwal, tanggal);
				List<Map> selectUserDailyKirimSphSptKeLbYearlyScheduler = selectUserKirimSphSptKeLbScheduler(bulanAwal, tanggal);
				//String outputDir = props.getProperty("pdf.dir.report") + "\\danamas_prima\\";
				List<File> attachments = new ArrayList<File>();
				for( int i = 0  ; i < selectUserDailyKirimSphSptKeLbMonthlyScheduler.size() ; i ++ ){
					String user = selectUserDailyKirimSphSptKeLbMonthlyScheduler.get(i).get("LUS_LOGIN_NAME").toString();
					String outputDir = props.getProperty("pdf.dir.report")+"\\summary_sph_spt\\"+ formatDate;
					String outputFilenameDaily = user+"_daily_summary_SPHSPT_" + formatDate + ".pdf";
					BigDecimal jmlHr = selectJmlHrKirimSphSptKeLb(tanggalAwal, tanggal, user);
					List selectUserKirimSphSptKeLbListScheduler = selectUserKirimSphSptKeLbListScheduler(tanggalAwal, tanggal, user);
			        Map<String, Object> params = new HashMap<String, Object>();
			        params.put("bulan", tanggalReport.substring(3, tanggalReport.length()));
			        params.put("tanggalAwal", tanggalAwal);
			        params.put("tanggal", tanggal);
			        params.put("tanggalReport", tanggalReport); 
			        params.put("user", user);
			        params.put("perHari", perHari);
			        params.put("perBulan", perBulan);
			        params.put("bulanAwal", bulanAwal);
			        params.put("jmlHr", jmlHr);
					JasperUtils.exportReportToPdf(
							props.getProperty("report.uw.pengiriman_sph_spt") + ".jasper", 
							outputDir + "\\", outputFilenameDaily, params, selectUserKirimSphSptKeLbListScheduler, PdfWriter.AllowPrinting, null, null);
					File sourceFileDaily = new File(outputDir+"\\"+outputFilenameDaily);
					attachments.add(sourceFileDaily);
				}
				for( int i = 0  ; i < selectUserDailyKirimSphSptKeLbYearlyScheduler.size() ; i ++ ){
					String user = selectUserDailyKirimSphSptKeLbYearlyScheduler.get(i).get("LUS_LOGIN_NAME").toString();
					String outputDir = props.getProperty("pdf.dir.report")+"\\summary_sph_spt\\"+ formatDate;
					String outputFilenameYearly = user+"_yearly_summary_SPHSPT_" + formatDate + ".pdf";
					BigDecimal jml_bln = selectJmlBlnKirimSphSptKeLbYearly(bulanAwal, tanggal, user);
					List selectUserKirimSphSptKeLbListYearlyScheduler = selectUserKirimSphSptKeLbListYearlyScheduler(bulanAwal, tanggal, user);
			        Map<String, Object> params = new HashMap<String, Object>();
			        params.put("bulan", tanggalReport.substring(3, tanggalReport.length()));
			        params.put("tanggalAwal", tanggalAwal);
			        params.put("tanggal", tanggal);
			        params.put("tanggalReport", tanggalReport); 
			        params.put("user", user);
			        params.put("perHari", perHari);
			        params.put("perBulan", perBulan);
			        params.put("bulanAwal", bulanAwal);
			        params.put("jml_bln", jml_bln);
					
					JasperUtils.exportReportToPdf(
							props.getProperty("report.uw.yearly_pengiriman_sph_spt") + ".jasper", 
							outputDir + "\\", outputFilenameYearly, params, selectUserKirimSphSptKeLbListYearlyScheduler, PdfWriter.AllowPrinting, null, null);
					File sourceFileYearly = new File(outputDir+"\\"+outputFilenameYearly);
					attachments.add(sourceFileYearly);
				}

				String to = props.getProperty("email_kirim_sph_spt");
//				String bcc = "Fadly_M@sinarmasmsiglife.co.id";
				String bcc = "andy@sinarmasmsiglife.co.id";
				String[] emailTo = to.split(";"); 
				String[] emailBcc = bcc.split(";"); 
		        String content =
	                "Informasi : Report Harian Untuk Laporan Pengiriman SPH dan SPT ke LB ";
		        if( attachments.size() > 0 )
		        {
					email.send(true, 
							props.getProperty("admin.ajsjava"), 
							emailTo, 
							null,
							emailBcc, 
							"[INFO] Report Harian Pengiriman SPH dan SPT ke LB",
							content, 
							attachments);
		        }
			} catch (Exception e) {
				logger.error("ERROR :", e);
				desc = "ERROR";
			}
		//================================================================================================
		
	}
	
	
	public void schedulerSummaryCompanyWs() throws DataAccessException{
		//scheduler untuk Report company ws yg blm bayar tagihan
			
			try{
				Date nowDate = commonDao.selectSysdateTrunc();
				DateFormat df = new SimpleDateFormat("ddMMyyyy");
				String formatDate = df.format(nowDate);
				String tanggalReport = FormatDate.toIndonesian(nowDate);
				List<Company_ws> selectCompanyWsList = selectCompanyWsScheduler();
				//String outputDir = props.getProperty("pdf.dir.report") + "\\danamas_prima\\";
				List<File> attachments = new ArrayList<File>();
				if( selectCompanyWsList != null && selectCompanyWsList.size() > 0 )
				{
					for( int i = 0  ; i < selectCompanyWsList.size() ; i ++ ){
						selectCompanyWsList.get(i).setJumlah_invoice_conv(FormatNumber.convertToTwoDigit(selectCompanyWsList.get(i).getJumlah_invoice()));
						selectCompanyWsList.get(i).setTgl_invoice_indo( FormatDate.toIndonesian( selectCompanyWsList.get(i).getTgl_invoice() ) );
					}
					String outputDir = props.getProperty("pdf.dir.report")+"\\summary_company_ws\\";
//					String outputDir = "C:"+"\\summary_company_ws\\";
					String outputFilename = "summary_company_ws_"+formatDate+".pdf";
			        Map<String, Object> params = new HashMap<String, Object>();
			        params.put("companyWsList", JasperReportsUtils.convertReportData( selectCompanyWsList) );
			        List temp = new ArrayList();
					JasperUtils.exportReportToPdf(
							props.getProperty("report.worksite.company_ws") + ".jasper", 
							outputDir + "\\", outputFilename, params, temp, PdfWriter.AllowPrinting, null, null);
					File sourceFile = new File(outputDir+"\\"+outputFilename);
					attachments.add(sourceFile);
				}

//				String bcc = "Fadly_M@sinarmasmsiglife.co.id";
				String bcc = "andy@sinarmasmsiglife.co.id";
				String[] emailTo = {"Asti@sinarmasmsiglife.co.id","Fabry@sinarmasmsiglife.co.id","setyo@sinarmasmsiglife.co.id","apri_d@sinarmasmsiglife.co.id"};
//				String[] emailCc = {"Fadly_M@sinarmasmsiglife.co.id","novie@sinarmasmsiglife.co.id"};
				String[] emailCc = {"andy@sinarmasmsiglife.co.id","novie@sinarmasmsiglife.co.id"};
				String[] emailBcc = bcc.split(";"); 
		        String content =
	                "Informasi : Report Data Perusahaan yang Premi Lanjutannya sudah Jatuh Tempo dan belum ada data Pembayaran pd tgl "+ tanggalReport;
		        if( attachments.size() > 0 )
		        {
					email.send(true, 
							props.getProperty("admin.ajsjava"), 
							emailTo, 
							emailCc,
							null, 
							"[INFO] Report Data Perusahaan yang Premi Lanjutannya sudah Jatuh Tempo dan belum ada data Pembayaran",
							content, 
							attachments);
		        }
			} catch (Exception e) {
				logger.error("ERROR :", e);
				try {
//					email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[]{"fadly_m@sinarmasmsiglife.co.id"},null,null, "[ ERROR ]Report invoice company ws "+new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()), e.getMessage(),null);
					email.send(false, "ajsjava@sinarmasmsiglife.co.id", new String[]{"andy@sinarmasmsiglife.co.id"},null,null, "[ ERROR ]Report invoice company ws "+new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()), e.getMessage(),null);
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
				}
			}
		//================================================================================================
		
	}
	
	
	/**
	 * Fungsi scheduller untuk mengirim email dan SMS secara otomatis ke Pemegang Polis dengan BCC ke agen penutup dan admin
	 * untuk memberitahukan keterangan mengenai proses polis.
	 * @author Anta
	 * @throws DataAccessException
	 */
	public void schedulerAksepSmsEmail(String msh_name) throws DataAccessException {
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("ddMMyyyy");
		Date bdate 	= new Date();
	
		List<Map> data = new ArrayList<Map>();
		data = uwDao.selectAksepSmsEmail();

		String desc	= "OK";
		String err = "";
		
		for(int i=0; i<data.size();i++){
			try{
				Map x = data.get(i);
				String spaj = x.get("REG_SPAJ").toString();
				String aksepst = x.get("LSSA_ID").toString();
				
				//Mendapatkan Email dari User U/W yg login pada saat memberikan Akseptasi/Akseptasi Khusus
				List<Map> useruw = new ArrayList<Map>();
				useruw = uwDao.selectAksepUserUW(spaj);
				String userbcc = "";
				for(int j=0; j<useruw.size();j++){
					Map y = (HashMap) useruw.get(j);
					if (useruw.size() > 1){
						String email = (String) y.get("LUS_EMAIL");
						if (email==null){
							userbcc = userbcc;
						}
						else{
							if (j == (useruw.size()-1)){
								userbcc = userbcc + email;
							}
							else{
								userbcc = userbcc + email + ";";
							}
						}
						
					}
				}
				String adminbcc = uwDao.selectAksepAdmin(spaj);
				String agenbcc = uwDao.selectAksepAgen(spaj);
				String emailbcc = agenbcc + ";" + adminbcc + ";" + userbcc;
				String[] bcc = emailbcc.split(";");

				Pemegang pemegang = new Pemegang();
				pemegang = bacDao.selectpp(spaj);
				Smsserver_out sms_out = new Smsserver_out();
				String pesan = "Diberitahukan bahwa Polis Anda dengan Nomor "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+" saat ini telah selesai dan akan disampaikan oleh Team Sales kami.</br>Terima kasih atas kepercayaan Anda untuk bergabung bersama PT Asuransi Jiwa Sinarmas MSIG Tbk. Silakan mengakses ke https://epolicy.sinarmasmsiglife.co.id/ untuk melihat data Polis Anda.";
				sms_out.setRecipient(pemegang.getNo_hp()!=null?pemegang.getNo_hp():pemegang.getNo_hp2());
				sms_out.setJenis(21);
				sms_out.setLjs_id(21);
				Integer countAkseptasiKhusus = bacDao.selectCountAkseptasiKhusus(spaj);
			
				if(aksepst.equals("5")){//hardcopy
					sms_out.setText("Nasabah Yth, PENGAJUAN ASURANSI SDH KAMI SETUJUI dgn No. Polis "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+". Dokumen polis akan dikirim oleh Team Sales kami. Silahkan mengakses HTTPS://EPOLICY.SINARMASMSIGLIFE.CO.ID untuk melihat data polis, User Name : "+pemegang.getMspe_no_identity()+", Password : "+df2.format(pemegang.getMspe_date_birth())+". Utk info hub CS di 021-26508300. Apabila ada perubahan No. Telepon, HP dan alamat koresponden / penagihan, silahkan SMS ke nomor 0812 1111 880 - 0855 8079 403.");
				}	
				//permintaan Andy untuk di nonaktifkan terlebih dahulu
//					else if(aksepst.equals("10")){//hardcopy
//						sms_out.setText("Yth."+(pemegang.getMspe_sex()==1?"bpk. ":"ibu. ")+ pemegang.getMcl_first()+", Polis No "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+" telah selesai di akseptasi.Tetapi masih ada kekurangan data dan akan disampaikan oleh Team Sales kami.Silahkan mengakses HTTPS://EPOLICY.SINARMASMSIGLIFE.CO.ID untuk melihat data polis,User Name : "+pemegang.getMspe_no_identity()+",Password : "+df2.format(pemegang.getMspe_date_birth())+".Utk info hub CS di 021-26508300. Apabila ada perubahan data,SMS ke 0812 1111 880 - 0855 8079 403");
//						sms_out.setLjs_id(24);
//						pesan = "Diberitahukan bahwa Polis Anda dengan Nomor "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+" saat ini telah selesai dan kekurangan data akan disampaikan oleh Team Sales kami.</br>Terima kasih atas kepercayaan Anda untuk bergabung bersama PT Asuransi Jiwa Sinarmas MSIG Tbk. Silakan mengakses ke https://www.sinarmasmsiglife.co.id/E-Policy untuk melihat data Polis Anda.";
//					}
			
				Boolean accessGranted = true;
				if(aksepst.equals("5") && countAkseptasiKhusus>=1){
					accessGranted=false;
				}
			
				if(accessGranted){
					if(pemegang.getMspe_email()!=null){
						if(aksepst.equals("5")){//sementara untuk lssa = 10 di nonaktifkan
							String subject = "Pemberitahuan Proses Pengajuan Polis";
							pesan = "Kepada Yth.</br>"+ (pemegang.getMspe_sex()==1?"Bpk. ":"Ibu. ") +
									pemegang.getMcl_first()+"</br></br>" + pesan;
							
							EmailPool.send("E-Lions", 1, 1, 0, 0, 
									null, 0, 0, commonDao.selectSysdate(), null, 
									true,
									"policy_service@sinarmasmsiglife.co.id", 
									new String[] {pemegang.getMspe_email()}, 
									null, 
									bcc, 
									subject, 
									pesan.toString(), 
									null, null);
							
							basDao.insertSmsServerOutWithGateway(sms_out, true);
						}
					}else{
						if(aksepst.equals("5")){//sementara untuk lssa = 10 di nonaktifkan
							basDao.insertSmsServerOutWithGateway(sms_out, true);
						}
					}
				}
			}
			catch (Exception e) {
				desc = "ERROR";
				logger.error("ERROR :", e);
				err=e.getLocalizedMessage();
			}
			
			try {
				insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
				msh_name, bdate, new Date(), desc, err);
			}
			catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
	}
	
	//================================================================================================

	/**
	 * Fungsi scheduller untuk mengirim email otomatis ke Jap PIC dan Agen untuk memberitahukan
	 * event yg akan berlangsung berdasarkan E-MNC.
	 * @author Anta
	 * @throws DataAccessException
	 */
	public void schedulerQuotation(String msh_name) throws DataAccessException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dm = new SimpleDateFormat("dd-MMM-yyyy");
		Date bdate 	= new Date();
		
		List<Map> data = new ArrayList<Map>();
		data = uwDao.selectQuotRm();
		
		String desc	= "OK";
		String err="";
		
		for(int i=0; i<data.size();i++){
			try{
				Map x = data.get(i);
				
				Date tglSys = (Date) commonDao.selectSysdate();
				Date tglMulai = (Date) x.get("start date");
				Date tglSelesai = (Date) x.get("end date");
				if(tglSelesai==null){
					tglSelesai = tglMulai;
				}
				
				String AAA = df.format(tglSys);
				Date AA = new Date();
				try{
					AA = df.parse(AAA);
				}catch(Exception e){
					System.err.println(e);
				}
				long tgl1 = AA.getTime();
				long tgl2 = tglMulai.getTime();
				long diff = tgl2 - tgl1;
				long diff_day = diff/(1000*60*60*24);
				
				Integer actint = null;
				if(x.get("activity reminder interval")==null){
					actint = 0;
				}else{
					actint = ((BigDecimal) x.get("activity reminder interval")).intValue();
				}
				
				String from = props.getProperty("admin.ajsjava");
				String emailto = x.get("japanese pic email").toString();
				String emailcc = x.get("agent email").toString();
				String emailbcc = "antasari@sinarmasmsiglife.co.id;akhyar@sinarmasmsiglife.co.id;yusuf@sinarmasmsiglife.co.id";
				
				StringBuffer pesan = new StringBuffer();
				pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: grey;} table td{border: 1px solid black;}</style></head>");
				pesan.append("<body>Dear " + x.get("japanese pic") + ", <br><br>");
				pesan.append("Regarding to our schedule for activity \"" + x.get("activity name").toString().toUpperCase() + "\" with ");
				pesan.append(x.get("account name") + " on E-MNC, ");
				pesan.append("we would like to announce you that the activity will be held on <b>" +  dm.format(tglMulai) + "</b> to <b>" + dm.format(tglSelesai) + "</b>. ");
				pesan.append("Here are the details:<br><br>");
				pesan.append("Activity Remark \t\t" + ": " + x.get("activity remark") + "<br>");
				pesan.append("Activity Type \t\t" + ": " + x.get("activity type") + "<br>");
				pesan.append("Company Remark \t\t" + ": " + x.get("company remark") + "<br>");
				pesan.append("Current Insurance \t\t" + ": " + x.get("current insurance") + "<br>");
				pesan.append("Insurance Expiry Date \t" + ": " + x.get("expired insurance date") + "<br>");
				pesan.append("Agent \t\t\t" + ": " + x.get("agent") + "<br><br><br>");
				pesan.append("Regards, <br>");
				pesan.append("<b>ADMIN</b>");

				if(x.get("activity reminder status").toString().equals("1")){
					if(diff_day >= 0){
						if(!emailto.trim().equals("")){	
							String[] to = emailto.split(";");
							String[] cc = emailcc.split(";");
							String[] bcc = emailbcc.split(";");
							Boolean accessGranted = false;
							if(x.get("activity repeat status").toString().equals("1")){	
								if(actint == 0) {
									accessGranted = false;
								}else {
									if(diff_day % actint == 0){	
										accessGranted = true;
									}
								}
							}
							else{
								if(diff_day == actint || diff_day == 0){
									accessGranted = true;
								}
							}
							if(accessGranted){
								try {
									email.send(true, from, to, cc, bcc,
										"Auto Reminder For Activity " + x.get("activity name"),
										pesan.toString(), null);
								}
								catch (MailException e1) {
									// TODO Auto-generated catch block
									logger.error("ERROR :", e1);
								}
								catch (MessagingException e1) {
									// TODO Auto-generated catch block
									logger.error("ERROR :", e1);
								}
							}
						}	
					}
				}
			}
			catch (Exception e) {
				desc = "ERROR";
				logger.error("ERROR :", e);
				err=e.getLocalizedMessage();
			}
			
			try {
				insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
				msh_name, bdate, new Date(), desc,err);
			}
			catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
	}
	//================================================================================================
	
	/**
	 * Fungsi scheduller untuk mengirim email otomatis ke Jap PIC dan Agen untuk memberitahukan
	 * event yg telah berlangsung selama 1 Minggu berdasarkan E-MNC.
	 * @author Anta
	 * @throws DataAccessException
	 */
	public void schedulerHistoryQuot(String msh_name) throws DataAccessException {
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		DateFormat dm = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate 	= new Date();
		Date endDate = commonDao.selectSysdate();
		Date startDate = commonDao.selectSysdateTruncated(-7);
		Connection conn = null;	
		String desc	= "OK";
		String err="";
		
		try{		
			conn = this.getDataSource().getConnection();
			List<Map> data = new ArrayList<Map>();
			List<Map> jpnhist = new ArrayList<Map>(); 
			List<Map> agnhist = new ArrayList<Map>();
			List<Map> wshist = new ArrayList<Map>();
			data = uwDao.selectHistQuotRm();
			jpnhist = uwDao.selectJpnQuotRm();
			agnhist = uwDao.selectAgentQuotRm();
			wshist = uwDao.selectWsQuotRm();
			
			String me_id = sequence.sequenceMeIdEmail();
			String dir = props.getProperty("pdf.dir.report") + "\\e_mnc\\";
			
			List<File> attachments = new ArrayList<File>();
			
			String outputFilename = "recent_activity_" + df.format(bdate) + ".xls";
			
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("tglMulai", dm.format(startDate));
	        params.put("tglAkhir", dm.format(endDate));
			
			if(!data.isEmpty()) {
				JasperUtils.exportReportToXls(props.getProperty("report.emnc_recent_hist") + ".jasper", 
						dir, outputFilename, params, conn);
				
				File sourceFile = new File(dir+"\\"+outputFilename);
				attachments.add(sourceFile);
			}
			
			StringBuffer pesan = new StringBuffer();
			//Format isi email
			pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
			pesan.append("<body>Dear Mr / Mrs,<br><br>");
			pesan.append("This is an auto-generated report from E-MNC System.<br>");
			pesan.append("Regarding to our schedule on E-MNC, ");
			pesan.append("we would like to announce you about company recent activity on <b>" + dm.format(startDate) + "</b> to <b>" + dm.format(endDate) + "</b>.<br><br><br>");
			pesan.append("Regards, <br>");
			pesan.append("<b>ADMIN</b>");

			
			//Untuk mengambil email Japanese PIC
			String jpnmail = "";
			for(int j=0; j<jpnhist.size();j++){
				Map y = jpnhist.get(j);
				String jpnpic = y.get("jap_email").toString();
				if (!jpnpic.equals("-")){
					jpnmail = jpnmail + jpnpic + ";";
				}
			}
			
			//Untuk mengambil email Agent
			String agnmail = "";
			for(int k=0; k<agnhist.size();k++){
				Map z = agnhist.get(k);
				String agn = z.get("loc_email").toString();
				if (!agn.equals("-")){
					agnmail = agnmail + agn + ";";
				}
			}
			
			//Untuk mengambil email Worksite Agent
			String wsmail = "";
			for(int l=0; l<wshist.size();l++){
				Map xyz = wshist.get(l);
				String ws = xyz.get("ws_email").toString();
				if (!ws.equals("-")){
					wsmail = wsmail + ws + ";";
				}
			}
			
			
			String from = props.getProperty("admin.ajsjava");
			String emailto = "ken_sukasah@sinarmasmsiglife.co.id";
			String emailcc = "naoki.takeda@sinarmasmsiglife.co.id";
			String emailbcc = "antasari@sinarmasmsiglife.co.id;akhyar@sinarmasmsiglife.co.id;yusuf@sinarmasmsiglife.co.id";
			//String emailto = "antasari@sinarmasmsiglife.co.id";
			//String emailto = jpnmail + "naoki.takeda@sinarmasmsiglife.co.id";
			//String emailcc = agnmail+";"+wsmail;
			
			if(!emailto.trim().equals("")){	
				String[] to = emailto.split(";");
				String[] cc = emailcc.split(";");
				String[] bcc = emailbcc.split(";");
				
				try {
					email.send(true, from, to, cc, bcc,
						"[E-MNC] Announcement For Recent Activity",
						pesan.toString(), attachments);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
				}
				catch (MessagingException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
				}
			}	
		}
		catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
		}finally{
			closeConnection(conn);
		}
		
		try {
			insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
			msh_name, bdate, new Date(), desc,err);
		}
		catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	//================================================================================================
	
	/**
	 * Fungsi scheduller untuk mengirim email otomatis ke Mr.Takeda dan Mr.Tsukada untuk mengirimkan
	 * target list report selama 1 Minggu berdasarkan E-MNC.
	 * @author Anta
	 * @throws DataAccessException
	 */
	public void schedulerTargetListReport(String msh_name) throws DataAccessException {
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		DateFormat dm = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate 	= new Date();
		Date endDate = commonDao.selectSysdate();
		Date startDate = commonDao.selectSysdateTruncated(-7);
		String desc	= "OK";
		String err="";
		
		try{		
			List<Map> data = new ArrayList<Map>();
			data = uwDao.selectTargetList();
			
			String me_id = sequence.sequenceMeIdEmail();
			String dir = props.getProperty("pdf.dir.report") + "\\e_mnc\\";
			
			List<File> attachments = new ArrayList<File>();
			
			String outputFilename = "company_activity_report_" + df.format(bdate) + ".pdf";
			
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("tglMulai", dm.format(startDate));
	        params.put("tglAkhir", dm.format(endDate));
			
			if(!data.isEmpty()) {
				JasperUtils.exportReportToPdf(
						props.getProperty("report.emnc_target_list")+".jasper", 
						dir, 
						outputFilename, 
						params, 
						data, 
						PdfWriter.AllowPrinting, null, null);

				File sourceFile = new File(dir+"\\"+outputFilename);
				attachments.add(sourceFile);
			}
			
			StringBuffer pesan = new StringBuffer();
			//Format isi email
			pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
			pesan.append("<body>Dear Mr / Mrs,<br><br>");
			pesan.append("This is an auto-generated report from E-MNC System, ");
			pesan.append("regarding company activity from <b>" + dm.format(startDate) + "</b> to <b>" + dm.format(endDate) + "</b>.<br><br><br>");
			pesan.append("Regards, <br>");
			pesan.append("<b>ADMIN</b>");
			
			String from = props.getProperty("admin.ajsjava");
			String emailto = "ken_sukasah@sinarmasmsiglife.co.id";
			String emailcc = "naoki.takeda@sinarmasmsiglife.co.id";
			String emailbcc = "antasari@sinarmasmsiglife.co.id;akhyar@sinarmasmsiglife.co.id;yusuf@sinarmasmsiglife.co.id";
			//String emailto = "naoki.takeda@sinarmasmsiglife.co.id;yasuhisa.tsukada@sinarmasmsiglife.co.id";
			//String emailto = "antasari@sinarmasmsiglife.co.id";
			
			if(!emailto.trim().equals("")){	
				String[] to = emailto.split(";");
				String[] cc = emailcc.split(";");
				String[] bcc = emailbcc.split(";");
				
				try {
//					EmailPool.send(me_id,"E-MNC", 1, 1, 0, 0, 
//							null, 0, 0, bdate, null, 
//							true, from, 
//							to, 
//							null, 
//							bcc, 
//							"Report For This Week's Activity", 
//							"Aloha.", 
//							attachments);
					email.send(true, from, to, cc, bcc,
							"[E-MNC] Report For This Week's Activity",
							pesan.toString(),
							attachments);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
				}
				catch (MessagingException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
				}
			}	
		}
		catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
		}
		
		try {
			insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
			msh_name, bdate, new Date(), desc,err);
		}
		catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	//================================================================================================
	
	public void schedulerPengirimanPolisKeDelta() throws DataAccessException{
		String desc	= "OK";
		
		Date dateEarly = commonDao.selectSysdateTrunc();
		dateEarly.setDate(1);
		Date nowDate = commonDao.selectSysdateTrunc();
		String tanggalReport = FormatDate.toIndonesian(nowDate);
		//scheduler untuk Report Pengiriman SPH SPT Ke LB
			
			try{
				DateFormat df = new SimpleDateFormat("ddMMyyyy");
				DateFormat day = new SimpleDateFormat("dd");
				DateFormat bulan = new SimpleDateFormat("MM");
				DateFormat dfParams = new SimpleDateFormat("dd/MM/yyyy");
				DateFormat dfAwalTahun = new SimpleDateFormat("01/01/yyyy");
				String tanggalAwal = dfParams.format(dateEarly);
				String tanggal = dfParams.format(nowDate);
				String formatDate = df.format(nowDate);
				String bulanAwal = dfAwalTahun.format(nowDate);
				BigDecimal perHari = new BigDecimal(day.format(nowDate));
				BigDecimal perBulan = new BigDecimal(bulan.format(nowDate));
				List<Map> selectUserKirimPolisKeDeltaMonthlyScheduler = selectUserKirimPolisKeDeltaScheduler(tanggalAwal, tanggal);
				List<Map> selectUserKirimPolisKeDeltaYearlyScheduler = selectUserKirimPolisKeDeltaScheduler(bulanAwal, tanggal);
				//String outputDir = props.getProperty("pdf.dir.report") + "\\danamas_prima\\";
				List<File> attachments = new ArrayList<File>();
				for( int i = 0  ; i < selectUserKirimPolisKeDeltaMonthlyScheduler.size() ; i ++ ){
					String user = selectUserKirimPolisKeDeltaMonthlyScheduler.get(i).get("LUS_LOGIN_NAME").toString();
					String outputDir = props.getProperty("pdf.dir.report")+"\\summary_polis_deltamas\\"+ formatDate;
					String outputFilenameDaily = user+"_daily_summary_polis_" + formatDate + ".pdf";
					BigDecimal jmlHr = selectJmlHrKirimPolisKeDelta(tanggalAwal, tanggal, user);
					List selectUserKirimPolisKeDeltaListScheduler = selectUserKirimPolisKeDeltaListScheduler(tanggalAwal, tanggal, user);
			        Map<String, Object> params = new HashMap<String, Object>();
			        params.put("bulan", tanggalReport.substring(3, tanggalReport.length()));
			        params.put("tanggalAwal", tanggalAwal);
			        params.put("tanggal", tanggal);
			        params.put("tanggalReport", tanggalReport); 
			        params.put("user", user);
			        params.put("perHari", perHari);
			        params.put("perBulan", perBulan);
			        params.put("bulanAwal", bulanAwal);
			        params.put("jmlHr", jmlHr);
					JasperUtils.exportReportToPdf(
							props.getProperty("report.uw.pengiriman_polis_delta_monthly") + ".jasper", 
							outputDir + "\\", outputFilenameDaily, params, selectUserKirimPolisKeDeltaListScheduler, PdfWriter.AllowPrinting, null, null);
					File sourceFileDaily = new File(outputDir+"\\"+outputFilenameDaily);
					attachments.add(sourceFileDaily);
					
				}

				for( int i = 0  ; i < selectUserKirimPolisKeDeltaYearlyScheduler.size() ; i ++ ){
					String user = selectUserKirimPolisKeDeltaYearlyScheduler.get(i).get("LUS_LOGIN_NAME").toString();
					String outputDir = props.getProperty("pdf.dir.report")+"\\summary_polis_deltamas\\"+ formatDate;
					String outputFilenameYearly = user+"_yearly_summary_polis_" + formatDate + ".pdf";
					BigDecimal jmlBln = selectJmlBlnKirimPolisKeDeltaYearly(bulanAwal, tanggal, user);
					List selectUserKirimPolisKeDeltaListYearlyScheduler = selectUserKirimPolisKeDeltaListYearlyScheduler(bulanAwal, tanggal, user);
			        Map<String, Object> params = new HashMap<String, Object>();
			        params.put("bulan", tanggalReport.substring(3, tanggalReport.length()));
			        params.put("tanggalAwal", tanggalAwal);
			        params.put("tanggal", tanggal);
			        params.put("tanggalReport", tanggalReport); 
			        params.put("user", user);
			        params.put("perHari", perHari);
			        params.put("perBulan", perBulan);
			        params.put("bulanAwal", bulanAwal);
			        params.put("jmlBln", jmlBln);
					
					JasperUtils.exportReportToPdf(
							props.getProperty("report.uw.pengiriman_polis_delta_yearly") + ".jasper", 
							outputDir + "\\", outputFilenameYearly, params, selectUserKirimPolisKeDeltaListYearlyScheduler, PdfWriter.AllowPrinting, null, null);
					File sourceFileYearly = new File(outputDir+"\\"+outputFilenameYearly);
					attachments.add(sourceFileYearly);
				}
				
				String to = props.getProperty("email_kirim_delta");
//				String bcc = "Fadly_M@sinarmasmsiglife.co.id";
				String bcc = "andy@sinarmasmsiglife.co.id";
				String[] emailTo = to.split(";"); 
				String[] emailBcc = bcc.split(";");
		        String content =
	                "Informasi : Report Harian Untuk Laporan Pengiriman Polis ke Delta Mas ";
		        if( attachments.size() > 0 )
		        {
					email.send(true, 
							props.getProperty("admin.ajsjava"), 
							emailTo, 
							null,
							emailBcc, 
							"[INFO] Report Harian Pengiriman Polis ke Delta Mas",
							content, 
							attachments);
		        }
			} catch (Exception e) {
				logger.error("ERROR :", e);
				desc = "ERROR";
			}
		//================================================================================================
		
	}
	
	public List selectUserKirimPolisKeDeltaListScheduler( String tanggalAwal, String tanggal, String user) throws DataAccessException {
		Map params = new HashMap();
		params.put("tanggalAwal", tanggalAwal);
		params.put("tanggal", tanggal);
		params.put("user", user);
		return query("selectUserKirimPolisKeDeltaListScheduler", params);
	}
	
	public BigDecimal selectJmlHrKirimPolisKeDelta( String tanggalAwal, String tanggal, String user) throws DataAccessException {
		Map params = new HashMap();
		params.put("tanggalAwal", tanggalAwal);
		params.put("tanggal", tanggal);
		params.put("user", user);
		return (BigDecimal) querySingle("selectJmlHrKirimPolisKeDelta", params);
	}
	
	public BigDecimal selectJmlBlnKirimPolisKeDeltaYearly( String bulanAwal, String tanggal, String user) throws DataAccessException {
		Map params = new HashMap();
		params.put("bulanAwal", bulanAwal);
		params.put("tanggal", tanggal);
		params.put("user", user);
		return (BigDecimal) querySingle("selectJmlBlnKirimPolisKeDeltaYearly", params);
	}
	
	public List selectUserKirimSphSptKeLbListScheduler( String tanggalAwal, String tanggal, String user) throws DataAccessException {
		Map params = new HashMap();
		params.put("tanggalAwal", tanggalAwal);
		params.put("tanggal", tanggal);
		params.put("user", user);
		return query("selectUserKirimSphSptKeLbListScheduler", params);
	}

	public BigDecimal selectJmlHrKirimSphSptKeLb( String tanggalAwal, String tanggal, String user) throws DataAccessException {
		Map params = new HashMap();
		params.put("tanggalAwal", tanggalAwal);
		params.put("tanggal", tanggal);
		params.put("user", user);
		return (BigDecimal)querySingle("selectJmlHrKirimSphSptKeLb", params);
	}
	
	public List selectUserKirimSphSptKeLbListYearlyScheduler( String bulanAwal, String tanggal, String user) throws DataAccessException {
		Map params = new HashMap();
		params.put("bulanAwal", bulanAwal);
		params.put("tanggal", tanggal);
		params.put("user", user);
		return query("selectUserKirimSphSptKeLbListYearlyScheduler", params);
	}
	
	public BigDecimal selectJmlBlnKirimSphSptKeLbYearly( String bulanAwal, String tanggal, String user) throws DataAccessException {
		Map params = new HashMap();
		params.put("bulanAwal", bulanAwal);
		params.put("tanggal", tanggal);
		params.put("user", user);
		return (BigDecimal) querySingle("selectJmlBlnKirimSphSptKeLbYearly", params);
	}
	
	public List selectUserKirimPolisKeDeltaScheduler( String tanggalAwal, String tanggal) throws DataAccessException {
		Map params = new HashMap();
		params.put("tanggalAwal", tanggalAwal);
		params.put("tanggal", tanggal);
		return query("selectUserKirimPolisKeDeltaScheduler", params);
	}
	
	public List selectUserKirimSphSptKeLbScheduler( String tanggalAwal, String tanggal) throws DataAccessException {
		Map params = new HashMap();
		params.put("tanggalAwal", tanggalAwal);
		params.put("tanggal", tanggal);
		return query("selectUserKirimSphSptKeLbScheduler", params);
	}
	
	public List<Company_ws> selectCompanyWsScheduler(){
		Map map = new HashMap();
		return query("selectCompanyWsScheduler", map);
	}
	
	public List selectUserKirimPolisKeDeltaListYearlyScheduler( String bulanAwal, String tanggal, String user) throws DataAccessException {
		Map params = new HashMap();
		params.put("bulanAwal", bulanAwal);
		params.put("tanggal", tanggal);
		params.put("user", user);
		return query("selectUserKirimPolisKeDeltaListYearlyScheduler", params);
	}
	
	public void schedulerAutoCancelPolisMallAssurance(String msh_name)  throws DataAccessException {
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		
		try{
			//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			List<String> daftarSpaj = query("selectAutoCancelPolisMallAssurance", null);
			
			for(String spaj : daftarSpaj){
				cancelPolis.cancelPolisFromUw(spaj, "Pembatalan otomatis H+14 dari tanggal input (MALLASSURANCE)", "0");
			}
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
		}

		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
			
	}
	
	public void schedulerAdmissionBridge() throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		
		Connection conn = null;
		int iErr = 0;

        try {
			
			List<File> attachments = new ArrayList<File>();
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
			String outputDir = props.getProperty("pdf.dir.report") + "\\bridge_admission\\";
			String outputFilename = "bridge" + sdf2.format(commonDao.selectSysdate()) + ".xls";
			//String sheetFilename = "pas" + sdf.format(commonDao.selectSysdate());
			String sheetFilename = "sheet1";
			
			Date yesterday = commonDao.selectSysdateTruncated(-1);
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			params.put("tanggalAwal", sdf.format(yesterday));
			params.put("tanggalAkhir", sdf.format(commonDao.selectSysdate()));
//			params.put("tanggalAkhir", sdf.format(yesterday));
			
			Integer CountTotalAdmissionBridge = bacDao.CountTotalAdmissionBridge(params);
			email.send(true, 
					"info@sinarmasmsiglife.co.id",
//					new String[]{"deddy@sinarmasmsiglife.co.id"}, null,null,
					new String[]{"bridgeagency.office@yahoo.com","yanuar.thomas@gmail.com"}, 
					new String[]{"Siti_P@sinarmasmsiglife.co.id","anthonius@sinarmasmsiglife.co.id"}, 
					new String[]{"deddy@sinarmasmsiglife.co.id","tri.handini@sinarmasmsiglife.co.id"}, 
					"Scheduler Admission Bridge Agency",//outputFilename,
					"Ada " +CountTotalAdmissionBridge+" produk Bridge yang masuk scheduler, Coba cek schedulernya ya."
					+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
					null);
			if(CountTotalAdmissionBridge>0){
			
				conn = this.getDataSource().getConnection();
				
				JasperUtils.exportReportToXls(props.getProperty("report.bridge.bridge_admission") + ".jasper", 
					outputDir, outputFilename, params, conn);
			
				
				File sourceFile = new File(outputDir+"\\"+outputFilename);
				
				attachments.add(sourceFile);
				
				email.send(true, 
						"info@sinarmasmsiglife.co.id",
//						new String[]{"deddy@sinarmasmsiglife.co.id"}, null,null,
						new String[]{"bridgeagency.office@yahoo.com","yanuar.thomas@gmail.com"}, 
						new String[]{"Siti_P@sinarmasmsiglife.co.id","anthonius@sinarmasmsiglife.co.id"}, 
						new String[]{"deddy@sinarmasmsiglife.co.id","tri.handini@sinarmasmsiglife.co.id"}, 
						"Scheduler Admission Bridge Agency",//outputFilename,
						"Berikut adalah Laporan SPAJ Admission Bridge Agency."
						+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
						attachments);
				
				try {
					insertMstSchedulerHist(
							InetAddress.getLocalHost().getHostName(),
							"SCHEDULER ADMISSION BRIDGE AGENCY", bdate, new Date(), desc,err);
				} catch (UnknownHostException e) {
					logger.error("ERROR :", e);
				}
			}
			
		} catch (Exception e) {
			iErr = 1;
			desc = "ERROR";
			logger.error("ERROR :", e);
		}finally{
			closeConnection(conn);
		}
		
	}
	/**
	 * fungsi untuk mengirim email otomatis ke cabang bancass yang terkait, ber-isikan file attachment spaj SIMPOL yang sudah Expired (Status di further req
	 * dan tidak diproses lanjut selama 90 dari tgl mspo_spaj_date/mste_tgl_terima_spaj).
	 * @author ryan
	 * @throws DataAccessException
	 */
	public void schedulerSimpolExpired() throws DataAccessException {
		
		Date bdate 	= new Date();
		String desc	= "OK";
		Connection conn = null;
        try {
        	conn = this.getDataSource().getConnection();
			List<File> attachments = new ArrayList<File>();
			List<Map> data = new ArrayList<Map>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
			String outputDir = props.getProperty("pdf.dir.report") + "\\simpol_expired\\";
			String outputFilename = "simpol_expired" + sdf2.format(commonDao.selectSysdate()) + ".xls";
			//String sheetFilename = "pas" + sdf.format(commonDao.selectSysdate());
			String sheetFilename = "sheet1";
			data = uwDao.selectSimpolExpired();
			if (data.size()>0){
				//pengalamatan email
				String to = "";
				String cc = "";
				String[] emailto = new String[] {};
				String[] emailcc = new String[] {};
				Map<String, Comparable> params = new HashMap<String, Comparable>();
			
					JasperUtils.exportReportToXls(props.getProperty("report.uw.report_polis_expired_simpol") + ".jasper", 
							outputDir, outputFilename, params, conn);
			
					
					File sourceFile = new File(outputDir+"\\"+outputFilename);
				
					attachments.add(sourceFile);
					String me_id = sequence.sequenceMeIdEmail();
					EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, new Date(), null, 
							true, "info@sinarmasmsiglife.co.id",
		//					new String[]{"ryan@sinarmasmsiglife.co.id"}, null,null,
							new String[]{"jelita@sinarmasmsiglife.co.id","iriana@sinarmasmsiglife.co.id","siti_n@sinarmasmsiglife.co.id"}, 
							new String[]{props.getProperty("uw.bancass2"),"jan@sinarmasmsiglife.co.id","edy@sinarmasmsiglife.co.id"}, 
							new String[]{"ryan@sinarmasmsiglife.co.id"}, 
							"Scheduler Simpol Expired",//outputFilename,
							"Berikut adalah Laporan SPAJ Simpol yang sudah Expired."
							+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions", 
							attachments,4);
					logger.info("SCHEDULER SIMPOL EXPIRED");
//				try {
//					logger.info("KIRIM   KIRIM");
//				} catch (Exception e) {
//					logger.error("ERROR :", e);
//				}
			}	
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
		}finally{
			closeConnection(conn);
		}
	}
	
	/**
	 * @author Deddy
	 * @since 16 Jul 2012
	 * @param msh_name
	 * @return Integer : untuk pencegahan agar scheduler tidak jalan >1x per hari
	 */
	public Integer selectAlreadyExistScheduler(String msh_name){
		return (Integer) querySingle("selectAlreadyExistScheduler", msh_name);
	}
	
	public void schedulerResetCounter(String msh_name) {
		String desc	= "OK";
		String err="";
		
		DateFormat df = new SimpleDateFormat("ddMMyy");
		Date nowDate = commonDao.selectSysdate();
		Map params = new HashMap();
		try{
			resetMstCounter(144, "01");
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						msh_name, new Date(), new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
		}
	}
	
	public void schedulerOutstandingBSM(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";
		
		DateFormat month = new SimpleDateFormat("MMMM");
		DateFormat year = new SimpleDateFormat("yyyy");
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
		NumberFormat nf = NumberFormat.getInstance();
		
		Date nowDate = commonDao.selectSysdate();
		Date startDateAll = commonDao.selectSysdate();
		Date processDate = commonDao.selectSysdateTruncated(-1);
//		startDate.setDate(1);
//		startDate.setMonth(startDate.getMonth()-1);
//		//startDateAll.setMonth(startDateAll.getMonth()-1);
//		endDate.setDate(1);
//		endDate.setDate(endDate.getDate()-1);
		Boolean successEmail = true;
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			
			String outputFilename1 = "lap_outstanding_sp_bsm_" + sdf.format(commonDao.selectSysdate()) + ".xls";
			String sheetFilename1 = "lap_outstanding_sp_bsm";
			String outputFilename2 = "lap_outstanding_ssl_bsm_" + sdf.format(commonDao.selectSysdate()) + ".xls";
			String sheetFilename2 = "lap_outstanding_ssl_bsm";
			String outputFilename3 = "lap_outstanding_simpol_bsm_" + sdf.format(commonDao.selectSysdate()) + ".xls";
			String sheetFilename3 = "lap_outstanding_simpol_bsm";
			
			List listCabangBsm = uwDao.selectDaftarCabangBsm();
			String[] emailList = new String[listCabangBsm.size()];
			for(int i=0; i<listCabangBsm.size();i++){
//				String date = FormatDate.getDay2(nowDate);
//				String months =FormatDate.getMonth(nowDate);
//				String years = FormatDate.getYearFourDigit(nowDate);
				String me_id = sequence.sequenceMeIdEmail();
				Integer months = nowDate.getMonth()+1;
				Integer years = nowDate.getYear()+1900;
				String outputDir = props.getProperty("attachment.mailpool.dir") +"\\" +years+"\\"+FormatString.rpad("0", months.toString(), 2)+"\\"+me_id;
				File dirFile = new File(outputDir);
				if(!dirFile.exists()) dirFile.mkdirs();
				Map daftarCabangBsm = (HashMap) listCabangBsm.get(i);
				emailList[i] = (String) daftarCabangBsm.get("EMAIL_CAB");
				String lcb_no = (String) daftarCabangBsm.get("LCB_NO");
				List<OutstandingBSM> reportOutstandingBsmSP = uwDao.selectListReportOutstandingBsm("SP",lcb_no,processDate);
				List<OutstandingBSM> reportOutstandingBsmSSL = uwDao.selectListReportOutstandingBsm("SSL",lcb_no,processDate);
				List<OutstandingBSM> reportOutstandingBsmSPL = uwDao.selectListReportOutstandingBsm("SPL",lcb_no,processDate);
				
				if(reportOutstandingBsmSP.size() > 0 || reportOutstandingBsmSSL.size() > 0 || reportOutstandingBsmSPL.size() > 0){
					//Date sysdate = commonDao.selectSysdate();
						outputFilename1 = "lap_outstanding_sp_bsm_" + sdf.format(commonDao.selectSysdate()) + ".xls";
						outputFilename2 = "lap_outstanding_ssl_bsm_" + sdf.format(commonDao.selectSysdate()) + ".xls";
						outputFilename3 = "lap_outstanding_simpol_bsm_" + sdf.format(commonDao.selectSysdate()) + ".xls";
						
						XLSCreatorReportOutstandingBSM xlsCreatorReportOutstandingBSM = new XLSCreatorReportOutstandingBSM();
						if(reportOutstandingBsmSP.size()>0){
							xlsCreatorReportOutstandingBSM.buildExcelOutstandingBsmSP(sheetFilename1, outputDir+"\\"+outputFilename1, reportOutstandingBsmSP, df.format(startDateAll), df.format(nowDate));
						}
						//XLSCreatorReportDanamasPrima xlsCreatorReportDanamasPrima = new XLSCreatorReportDanamasPrima();
						if(reportOutstandingBsmSSL.size()>0){
							xlsCreatorReportOutstandingBSM.buildExcelOutstandingBsmSSL(sheetFilename2, outputDir+"\\"+outputFilename2, reportOutstandingBsmSSL, df.format(startDateAll), df.format(nowDate));
						}
						if(reportOutstandingBsmSPL.size()>0){
							xlsCreatorReportOutstandingBSM.buildExcelOutstandingBsmSPL(sheetFilename3, outputDir+"\\"+outputFilename3, reportOutstandingBsmSPL, df.format(startDateAll), df.format(nowDate));
						}
						
						// email file sheet1.xls
						List<File> attachments = new ArrayList<File>();
						File sourceFile1 = new File(outputDir+"\\"+outputFilename1);
						
						// email file sheet1.xls
						File sourceFile2 = new File(outputDir+"\\"+outputFilename2);

						File sourceFile3 = new File(outputDir+"\\"+outputFilename3);
						
						if(reportOutstandingBsmSP.size()>0)attachments.add(sourceFile1);
						if(reportOutstandingBsmSSL.size()>0)attachments.add(sourceFile2);
						if(reportOutstandingBsmSPL.size()>0)attachments.add(sourceFile3);
						
						EmailPool.send(me_id,"SMiLe E-Lions", 1, 1, 0, 0, 
										null, 0, 0, nowDate, null, 
										true, "ajsjava@sinarmasmsiglife.co.id", 
//										new String[]{"deddy@sinarmasmsiglife.co.id"}, 
//										new String[]{"deddy@sinarmasmsiglife.co.id"},  
										new String[]{(String) daftarCabangBsm.get("EMAIL_CAB")}, 
										new String[]{"deddy@sinarmasmsiglife.co.id"}, 
										null, 
										"Data OutStanding Bank Sinarmas cabang "+(String) daftarCabangBsm.get("NAMA_CABANG")+" sampai dengan "+month.format(processDate)+" tahun " +year.format(processDate), 
										"Terlampir Data Outstanding Bank Sinarmas untuk Produk Simas Prima, Simas Stabil Link, dan Simas Power Link. <br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
										null,5);
				}
			}
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						msh_name, bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
		}
		
		
	}
	
	/**
	 * fungsi untuk mengirim email otomatis ke UW(AGENCY&WORKSITE) tentang pending admedika yg lebih dari 3 hari kerja setelah proses kirim
	 * @author lufi
	 * @throws DataAccessException
	 */
	public void pendingKartuAdmedika(String msh_name) throws DataAccessException {
				
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat dm = new SimpleDateFormat("dd-MMM-yyyy");	
			Date bdate 	= new Date();
			String desc	= "OK";
			String err ="";
					//Calendar curr=Calendar.getInstance();
					//int tigaharisebelumnya;
			List<Map> data = new ArrayList<Map>();
			List<Map> excel = new ArrayList<Map>();
			List tampungspaj = new ArrayList<String>();
			List sortir=new ArrayList();
			List tampungnopol = new ArrayList<String>();
			List tampungpp = new ArrayList<String>();
			List tampungnp = new ArrayList<String>();
			List tampungtt = new ArrayList<String>();
			List tampungstat = new ArrayList<String>();
			StringBuffer pesan = new StringBuffer();
					
					//Format isi email
			pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
			pesan.append("<body>Dear All,<br><br>");
			pesan.append("<b>NB:E-MAIL INI ADALAH E-MAIL AUTOMATIS JANGAN REPLY E-MAIL INI</b><br><br>");
			pesan.append("<b>Laporan Pending Admedika tanggal:  "+dm.format(bdate)+"</b><br><br>");
			pesan.append("<table cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1000px'>");
			pesan.append("<tr bgcolor='#A8B8EE'><th> No </th>");
			pesan.append("<th>NO.POLIS</th>");
			pesan.append("<th>Mulai Berlaku</th>");
			pesan.append("<th>Nama Tertanggung</th>");
			pesan.append("<th>Status</th>");
			pesan.append("<th>Nama Plan</th>");
			pesan.append("<th>Tanggal Kirim</th>");
			pesan.append("<th>Tanggal Akseptasi</th>");
			pesan.append("<th>Cabang</th></tr>");
			data=uwDao.selectPendingAdmedika("yes");
			excel = uwDao.selectPendingAdmedika("no");
		    Date tglSys = (Date) commonDao.selectSysdate();
		    int no=0;
			if (data!=null){					
			    for (int i=0;i<data.size();i++){					
					 Map medika=data.get(i);
					 Map medikaex = excel.get(i);			 
					 Date tglKirim=(Date) medika.get("TGL_KIRIM");
					 String harIni=(String)medika.get("NOW");
					 String hariKirim=(String)medika.get("HARI");						 
					 String AAA = df.format(tglSys);						 
					 Date AA = new Date();
				     try {
				    	 
						 	AA = df.parse(AAA);
						 	
					}
					catch(Exception e){
						
						 	System.err.println(e);
						 	
					}
					long tgl1 = AA.getTime();
					long tgl2 = tglKirim.getTime();
					long diff = tgl1 - tgl2;				
					long diff_day = diff/(1000*60*60*24);
					String status = (String)medika.get("STATUS");
					status = status.replace(",", "<br>");
				    String ttg = (String)medika.get("TERTANGGUNG");
					ttg = ttg.replace(",", "<br>");
				    if (harIni.trim().equals("Monday")){
						if (diff_day>=3 && diff_day<=4 && ((hariKirim.trim().equals("Friday")||hariKirim.trim().equals("Thursday")))){
							
								 logger.info("GAK BOLEH DIKIRIM");
						}else{
								 no=no+1;
								 pesan.append("<tr bgcolor='#E0EEFE'><td align='center' nowrap>"+ no + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("MSPO_POLICY_NO") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("BEGDATE") + "</td>");
								 pesan.append("<td nowrap>"+ttg + "</td>");
								 pesan.append("<td nowrap>"+ status + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("NAMA_PLAN") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("HARI_H") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("TGL_AKSEPTASI") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("CABANG") + "</td></tr>");
								 
								 Map m=new HashMap();
								 m.put("MSPO_POLICY_NO", medikaex.get("MSPO_POLICY_NO"));
								 m.put("BEGDATE", medikaex.get("BEGDATE"));
								 m.put("TERTANGGUNG",medikaex.get("TERTANGGUNG"));
								 m.put("STATUS", medikaex.get("STATUS"));
								 m.put("NAMA_PLAN", medikaex.get("NAMA_PLAN"));
								 m.put("HARI_H", medikaex.get("HARI_H"));
								 m.put("TGL_AKSEPTASI",medikaex.get("TGL_AKSEPTASI"));
								 m.put("CABANG",medikaex.get("CABANG"));
								 sortir.add(m);
								 
								 
							 }
						 }else if (harIni.trim().equals("Tuesday")){
							 if (diff_day<5 ){
								 logger.info("GAK BOLEH DIKIRIM");
							 }else{
								 no=no+1;
								 pesan.append("<tr bgcolor='#E0EEFE'><td align='center' nowrap>"+ no + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("MSPO_POLICY_NO") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("BEGDATE") + "</td>");
								 pesan.append("<td nowrap>"+ttg + "</td>");
								 pesan.append("<td nowrap>"+ status + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("NAMA_PLAN") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("HARI_H") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("TGL_AKSEPTASI") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("CABANG") + "</td></tr>");
								 
								 Map m=new HashMap();
								 m.put("MSPO_POLICY_NO", medikaex.get("MSPO_POLICY_NO"));
								 m.put("BEGDATE", medikaex.get("BEGDATE"));
								 m.put("TERTANGGUNG",medikaex.get("TERTANGGUNG"));
								 m.put("STATUS", medikaex.get("STATUS"));
								 m.put("NAMA_PLAN", medikaex.get("NAMA_PLAN"));
								 m.put("HARI_H", medikaex.get("HARI_H"));
								 m.put("TGL_AKSEPTASI",medikaex.get("TGL_AKSEPTASI"));
								 m.put("CABANG",medikaex.get("CABANG"));
								 sortir.add(m);
							 }
						 }else if (harIni.trim().equals("Wednesday")){
							 if (diff_day<5 ){
								 
								 logger.info("GAK BOLEH DIKIRIM");
								 
							 }else{
								 no=no+1;
								 pesan.append("<tr bgcolor='#E0EEFE'><td align='center' nowrap>"+ no + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("MSPO_POLICY_NO") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("BEGDATE") + "</td>");
								 pesan.append("<td nowrap>"+ttg + "</td>");
								 pesan.append("<td nowrap>"+ status + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("NAMA_PLAN") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("HARI_H") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("TGL_AKSEPTASI") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("CABANG") + "</td></tr>");
								 
								 Map m=new HashMap();
								 m.put("MSPO_POLICY_NO", medikaex.get("MSPO_POLICY_NO"));
								 m.put("BEGDATE", medikaex.get("BEGDATE"));
								 m.put("TERTANGGUNG",medikaex.get("TERTANGGUNG"));
								 m.put("STATUS", medikaex.get("STATUS"));
								 m.put("NAMA_PLAN", medikaex.get("NAMA_PLAN"));
								 m.put("HARI_H", medikaex.get("HARI_H"));
								 m.put("TGL_AKSEPTASI",medikaex.get("TGL_AKSEPTASI"));
								 m.put("CABANG",medikaex.get("CABANG"));
								 sortir.add(m);
							 }
						 }else if (harIni.trim().equals("Thursday")){
							 if (diff_day<3 ){
								 
								logger.info("GAK BOLEH DIKIRIM");
								
							 }else{
								 no=no+1;
								 pesan.append("<tr bgcolor='#E0EEFE'><td align='center' nowrap>"+ no + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("MSPO_POLICY_NO") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("BEGDATE") + "</td>");
								 pesan.append("<td nowrap>"+ttg + "</td>");
								 pesan.append("<td nowrap>"+ status + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("NAMA_PLAN") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("HARI_H") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("TGL_AKSEPTASI") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("CABANG") + "</td></tr>");
								 
								 Map m=new HashMap();
								 m.put("MSPO_POLICY_NO", medikaex.get("MSPO_POLICY_NO"));
								 m.put("BEGDATE", medikaex.get("BEGDATE"));
								 m.put("TERTANGGUNG",medikaex.get("TERTANGGUNG"));
								 m.put("STATUS", medikaex.get("STATUS"));
								 m.put("NAMA_PLAN", medikaex.get("NAMA_PLAN"));
								 m.put("HARI_H", medikaex.get("HARI_H"));
								 m.put("TGL_AKSEPTASI",medikaex.get("TGL_AKSEPTASI"));
								 logger.info(medikaex.get("TGL_AKSEPTASI"));
								 m.put("CABANG",medikaex.get("CABANG"));
								 sortir.add(m);
							 }
						 }else if (harIni.trim().equals("Friday")){
							 if (diff_day<3 ){
								 
								 logger.info("GAK BOLEH DIKIRIM");
								 
							 }else{
								
								 no=no+1;
								 pesan.append("<tr bgcolor='#E0EEFE'><td align='center' nowrap>"+ no + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("MSPO_POLICY_NO") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("BEGDATE") + "</td>");
								 pesan.append("<td nowrap>"+ttg + "</td>");
								 pesan.append("<td nowrap>"+ status + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("NAMA_PLAN") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("HARI_H") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("TGL_AKSEPTASI") + "</td>");
								 pesan.append("<td nowrap>"+ medika.get("CABANG") + "</td></tr>");
								 
								 Map m=new HashMap();
								 m.put("MSPO_POLICY_NO", medikaex.get("MSPO_POLICY_NO"));
								 m.put("BEGDATE", medikaex.get("BEGDATE"));
								 m.put("TERTANGGUNG",medikaex.get("TERTANGGUNG"));
								 m.put("STATUS", medikaex.get("STATUS"));
								 m.put("NAMA_PLAN", medikaex.get("NAMA_PLAN"));
								 m.put("HARI_H", medikaex.get("HARI_H"));
								 m.put("TGL_AKSEPTASI",medikaex.get("TGL_AKSEPTASI"));
								 m.put("CABANG",medikaex.get("CABANG"));
								 sortir.add(m);
							  }
						 }	
						 
					 }
					
					pesan.append("</table> <br><br><br></body>");	
					String dir = props.getProperty("pdf.dir.export") + "\\admedika\\";		
					List<File> attachments = new ArrayList<File>();			
					String outputFilename = "report_pending_admedika" + dm.format(bdate) + ".xls";			
			        Map<String, Object> params = new HashMap<String, Object>();
					if(!sortir.isEmpty()) {
						try {
							
							JasperUtils.exportReportToXls(props.getProperty("report.pending_admedika")+".jasper",dir,outputFilename,params, sortir,null);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e);
							err=e.getLocalizedMessage();
						} catch (JRException e) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e);
							err=e.getLocalizedMessage();
						}

						File sourceFile = new File(dir+"\\"+outputFilename);
						attachments.add(sourceFile);
							   	    
					String from = props.getProperty("admin.ajsjava");
					//String emailto = "lufi@sinarmasmsiglife.co.id;";
					HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerpendingKartuAdmedika", "SCHEDULER_PENDING_KARTU_ADMEDIKA");
					String[] to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
					String[] cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
					String[] bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
					//String emailto="Timmy@sinarmasmsiglife.co.id;Riyadi@sinarmasmsiglife.co.id;Inge@sinarmasmsiglife.co.id;novie@sinarmasmsiglife.co.id;ningrum@sinarmasmsiglife.co.id;tities@sinarmasmsiglife.co.id";
					//String[] to=emailto.split(";");
					try {
						EmailPool.send("E-Lions", 1, 1, 0, 0, 
								null, 0, 0, commonDao.selectSysdate(), null, 
								true, "info@sinarmasmsiglife.co.id", 
								to, 
								cc, 
								bcc, 
								"Laporan Pending Admedika", 
								pesan.toString(), 
								attachments, null);
					}
					catch (MailException e1) {
							// TODO Auto-generated catch block
							//logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
					}
					
				}
				try {
						insertMstSchedulerHist(
								InetAddress.getLocalHost().getHostName(),
								msh_name, bdate, new Date(), desc,err);
					} catch (UnknownHostException e) {
							logger.error("ERROR :", e);
					}
				     catch (Exception e) {
				    	 desc = "ERROR";
				    	 logger.error("ERROR :", e);
				    }
					
		 }
				
	}
	
	/**
	 * fungsi untuk mengirim email report Status SPT
	 * @author lufi
	 * @throws DataAccessException
	 */
	public void schedulerAksepSPT(String msh_name) throws DataAccessException {
		Date bdate 	= new Date();
		String desc	= "OK";
		DateFormat dm = new SimpleDateFormat("dd-MMM-yyyy");
		
		try{
			Date tglSys = (Date) commonDao.selectSysdate();
			String dir = props.getProperty("pdf.dir.export") + "\\admedika\\";		
			List<File> attachments = new ArrayList<File>();			
			String outputFilename = "report_aksep_spt_harian" + dm.format(bdate) + ".pdf";			
	        Map<String, Object> params = new HashMap<String, Object>();
	        params.put("tanggalAwal", tglSys);
	        params.put("tanggalAkhir", tglSys);
	        
	        logger.info("GAK BOLEH DIKIRIM");
			
	        Connection conn = null;
			try {
					conn = this.getDataSource().getConnection();
					JasperUtils.exportReportToPdf(
							props.getProperty("report.uw.aksep_spt_byEmail_harian")+".jasper", 
							dir, 
							outputFilename, 
							params, 
							conn,
							PdfWriter.AllowPrinting, null, null);
			}catch(Exception e){
				  logger.error("ERROR :", e);
	              throw e;
			}finally{
				  closeConnection(conn);
			}	
				logger.info("GAK BOLEH DIKIRIM");
				File sourceFile = new File(dir+"\\"+outputFilename);
				attachments.add(sourceFile);					   	    
				String from = props.getProperty("admin.ajsjava");
				StringBuffer pesan = new StringBuffer();
				String emailto="ingrid@sinarmasmsiglife.co.id;ningrum@sinarmasmsiglife.co.id;inge@sinarmasmsiglife.co.id;Tities@sinarmasmsiglife.co.id;Hayatin@sinarmasmsiglife.co.id;novie@sinarmasmsiglife.co.id";
				//String emailto="lufi@sinarmasmsiglife.co.id";
				//String[] cc=new String[] {"lufi@sinarmasmsiglife.co.id"};
				String[] to=emailto.split(";");
				
				//isi email
				pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
				pesan.append("<body>Dear All,<br><br>");
				pesan.append("<b>NB:E-MAIL INI ADALAH E-MAIL OTOMATIS JANGAN REPLY E-MAIL INI</b><br><br>");
				pesan.append("<b>REPORT STATUS AKSEPTASI  SPT BY EMAIL tanggal:  "+dm.format(bdate)+"</b><br><br>");
				pesan.append("Terlampir Report SPT <br><br>");				
				pesan.append("<b>Note :  dalam report ini termasuk  semua SPT  yang belum diaksep sampai dengan tanggal report ini dikeluarkan.</b><br><br>");
				pesan.append(" <br><br><br></body></html>");
						
				
				
				email.send(true, from, to, null, null,
						"Report SPT by Email",
						pesan.toString(), attachments);	
				
				try {
					insertMstSchedulerHist(
							InetAddress.getLocalHost().getHostName(),
							msh_name, bdate, new Date(), desc,"");
				} catch (UnknownHostException e) {
						logger.error("ERROR :", e);
				}
			     catch (Exception e) {
			    	 desc = "ERROR";
			    	 logger.error("ERROR :", e);
			    }
				
				
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("ERROR :", e);
		}
		
	}
	
	/**
	 * Fungsi untuk Email Rekap Referensi ke Agency Support
	 * @author Canpri
	 * @since 4 Sep 2012
	 * @param msh_name
	 */
	public void schedulerRekapReferensi(String msh_name)  throws DataAccessException {
		Date bdate 	= new Date();
		String desc	= "OK";
		
		try{
			List rekap = query("selectRekapReferensi", null);
			String ref = "";
			
			ref += "<p>Rekap Tambang Emas per Tanggal : "+defaultDateFormat.format(bdate)+"</p>";
			ref += "<table width='100%' border='1' cellpadding='1' cellspacing='0' style='font-size:10px;'>";
			ref += "<tr>";
			ref += "<td rowspan='2' align='center' valign='middle'><strong>No</strong></td>";
			ref += "<td rowspan='2' align='center' valign='middle'><strong>Tgl Pembuatan</strong></td>";
			ref += "<td colspan='4' align='center' valign='middle'><strong>Referral</strong></td>";
			ref += "<td colspan='4' align='center' valign='middle'><strong>Referensi</strong></td>";
			ref += "<td colspan='3' align='center' valign='middle'><strong>Agent</strong></td>";
			ref += "</tr>";
			ref += "<tr>";
			ref += "<td align='center' valign='middle'><strong>Kode</strong></td>";
			ref += "<td align='center' valign='middle'><strong>Nama</strong></td>";
			ref += "<td align='center' valign='middle'><strong>Tgl Lahir</strong></td>";
			ref += "<td align='center' valign='middle'><strong>Email</strong></td>";
			ref += "<td align='center' valign='middle'><strong>Nama</strong></td>";
			ref += "<td align='center' valign='middle'><strong>Tgl Lahir</strong></td>";
			ref += "<td align='center' valign='middle'><strong>No Telp.</strong></td>";
			ref += "<td align='center' valign='middle'><strong>Jenis Referensi</strong></td>";
			ref += "<td align='center' valign='middle'><strong>Kode</strong></td>";
			ref += "<td align='center' valign='middle'><strong>Nama</strong></td>";
			ref += "<td align='center' valign='middle'><strong>Email</strong></td>";
			ref += "</tr>";
			  
			
			
			for(int i=0;i<rekap.size();i++){
				Map m = (HashMap)rekap.get(i);
				
				Integer no = i+1;
				
				ref += "<tr>";
				ref += "<td align='center'>"+no+"</td>";
			    ref += "<td align='center'>"+defaultDateFormat.format((Date)m.get("TGL_BUAT"))+"</td>";
			    ref += "<td align='center'>"+(String)m.get("KD_REF")+"</td>";
			    ref += "<td align='center'>"+(String)m.get("NAMA_REF")+"</td>";
			    ref += "<td align='center'>"+defaultDateFormat.format((Date)m.get("TGL_LAHIR_REF"))+"</td>";
			    ref += "<td align='center'>"+(String)m.get("EMAIL_REF")+"</td>";
			    ref += "<td align='center'>"+(String)m.get("NAMA")+"</td>";
			    ref += "<td align='center'>"+defaultDateFormat.format((Date)m.get("TGL_LAHIR"))+"</td>";
			    ref += "<td align='center'>"+(BigDecimal)m.get("NO_TELP")+"</td>";
			    ref += "<td align='center'>"+(String)m.get("JENIS")+"</td>";
			    ref += "<td align='center'>"+(String)m.get("KD_AGEN")+"</td>";
			    ref += "<td align='center'>"+(String)m.get("NAMA_AGEN")+"</td>";
			    ref += "<td align='center'>"+(String)m.get("EMAIL_AGEN")+"</td>";
			  	ref += "</tr>";
			}
			
			ref += "</table>";
			
			if(!rekap.isEmpty()){
				/*for(int i=0;i<rekap.size();i++){
					Map m = (HashMap)rekap.get(i);
					
					Map params = new HashMap();
					params.put("kd_ref", (String)m.get("KD_REF"));
					params.put("nama", (String)m.get("NAMA"));
					params.put("tgl_lahir", defaultDateFormat.format((Date)m.get("TGL_LAHIR")));
					
					update("updateRekapReferensi", params);
				}*/
				update("updateRekapReferensi", null);
				
				email.send(true, 
						props.getProperty("admin.java"), 
						new String[]{"canpri@sinarmasmsiglife.co.id"}, 
						null, null, 
						"Rekap Tambang Emas", ref, null);
				logger.info("SCHEDULER REKAP REFERENSI");
			}
			
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
		}

		try {
			String a = InetAddress.getLocalHost().getHostName();
			//insertMstSchedulerHist(
					//InetAddress.getLocalHost().getHostName(),
					//msh_name, bdate, new Date(), desc);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
			
	}
	
	/**
	 * Summary untuk status2 aksep yg masih terpending, seperti further dkk untuk Power Save Syariah BSM dan Multi Invest Syariah BSM
	 * @author Canpri
	 * @since 11 Oct 2012
	 * @param msh_name
	 */
	public void schedulerSummaryAkseptasiBancassSyariah(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

		try {
			Date yesterday = commonDao.selectSysdateTruncated(-1);
			Date today = commonDao.selectSysdateTruncated(0);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			//Deddy (13/3/2009) tambahan untuk menentukan tahun -1 dari sekarang 
			DateFormat year = new SimpleDateFormat("yyyy");
			String yearbefore = year.format(FormatDate.add(today, Calendar.YEAR, -1) );
			
			//Deddy(16/3/2009) tambahan untuk menentukan bulan1-12 dari tahun sekarang
			DateFormat monthyear = new SimpleDateFormat("mmyyyy");
			String month1 = "01"+ year.format(today);
			String month2 = "02"+ year.format(today);
			String month3 = "03"+ year.format(today);
			String month4 = "04"+ year.format(today);
			String month5 = "05"+ year.format(today);
			String month6 = "06"+ year.format(today);
			String month7 = "07"+ year.format(today);
			String month8 = "08"+ year.format(today);
			String month9 = "09"+ year.format(today);
			String month10 = "10"+ year.format(today);
			String month11 = "11"+ year.format(today);
			String month12 = "12"+ year.format(today);
			
			
			logger.info("UW SCHEDULER AT " + new Date());
			long start = System.currentTimeMillis();
			
			//Report Summary (Menu dapat diakses di Entry > UW > Akseptasi Khusus > Summary)
			
			String outputDir = props.getProperty("pdf.dir.report") + "\\summary_akseptasi_bancass_syariah\\" + dateFormat.format(today) + "\\";
			//String outputDir = "D:\\Test\\";
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			params.put("tanggal", df.format(today));
			params.put("user", "SYSTEM");
			
			//passing parameter ke reportnya
			params.put("yearbefore", yearbefore);
			params.put("month1", month1);
			params.put("month2", month2);
			params.put("month3", month3);
			params.put("month4", month4);
			params.put("month5", month5);
			params.put("month6", month6);
			params.put("month7", month7);
			params.put("month8", month8);
			params.put("month9", month9);
			params.put("month10", month10);
			params.put("month11", month11);
			params.put("month12", month12);

			Map<String, List<Map>> distribusi 			= new HashMap<String, List<Map>>();
			Map<String, List<Map>> daftarReport 		= new HashMap<String, List<Map>>();
			Map<String, List<File>> daftarAttachment1 	= new HashMap<String, List<File>>();
			Map<String, List<File>> daftarAttachment2 	= new HashMap<String, List<File>>();
			
			daftarAttachment1.put("BSM", 	new ArrayList<File>());
			
			daftarAttachment2.put("BSM", 	new ArrayList<File>());
			
			String b;
			//Daftar Semua Report
			daftarReport.put("Akseptasi_Khusus_Bancass_Syariah", uwDao.selectDaftarAkseptasiNyangkutBSMSyariah(10, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 		//1. akseptasi khusus
			daftarReport.put("Further_Requirements_Bancass_Syariah", uwDao.selectDaftarAkseptasiNyangkutBSMSyariah(3, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 	//2. further requirements -> lssa_id in (3,4,8)
			
			//Looping Utama dari daftar semua report
			for(String r : daftarReport.keySet()) {
				
				params.put("banyakMaunya", r);
				
				//Parameter Tambahan
				if(r.equals("Expired")) {
					params.put("judul", "Follow-Up Polis Expired");
				}
				else if(r.equals("Akseptasi_Khusus_Bancass_Syariah")) {
					params.put("judul", "Follow-Up Akseptasi Khusus Bancass Syariah");
				}else if(r.equals("Further_Requirements_Bancass_Syariah")) {
					params.put("judul", "Follow-Up Further Requirement Bancass Syariah");
				}
				
				if(r.equals("Akseptasi_Khusus_Bancass_Syariah")) {
					params.put("note",
						"Data yang masuk disini adalah data:\n" +
						"* Polis diaksep dengan kondisi khusus (polis yang sudah diaksep tetapi masih diperlukan data tambahan)");
//						"Note:\n" +
//						"Data yang masuk disini adalah data :\n" +
//						"* Polis yang sudah dicetak, tetapi masih ada Further requirement. Polis diaksep dengan kondisi khusus.\n" + 
//						"Polis yang di 'Aksep dengan kondisi khusus' adalah polis yang diaksep ( dapat sudah langsung dicetak polis atau masih pending cetak polis )\n" +
//						"namun sebenarnya masih diperlukan data tambahan.");
				}else if(r.equals("Further_Requirements_Bancass_Syariah")) {
					params.put("note",
						"Note:\n" +
						"Data yang masuk disini adalah data :\n" +
						"* Polis yang belum diaksep, masih perlu data tambahan ( Further requirement )");
				}else {
					params.put("note", "");
				}
				
				//Daftar pembagian distribusi, direset setiap loop
				distribusi.put("BSM", 	new ArrayList<Map>());
				
				List<Map> report = daftarReport.get(r);
				
				//Looping untuk membagi2 hasil query ke masing2 distribusi
				for(Map m : report) {
					String lca_id = (String) m.get("LCA_ID");
					String team_name = (String) m.get("TEAM_NAME");
					BigDecimal jenis = (BigDecimal) m.get("JN_BANK");
					
					BigDecimal templsbs = (BigDecimal) m.get("LSBS_ID");
					int lsbs_id = 0;
					if(templsbs != null) lsbs_id = templsbs.intValue();

					BigDecimal templsdbs = (BigDecimal) m.get("LSDBS_NUMBER");
					int lsdbs_number = 0;
					if(templsdbs != null) lsdbs_number = templsdbs.intValue();

					int jn_bank = (jenis == null ? 0 : jenis.intValue());
					
					if(team_name== null){
						team_name= "";
					}
					
					((List<Map>) distribusi.get("BSM")).add(m);
				
				}
				
				
				//Looping untuk menyimpan file PDF berdasarkan masing2 distribusi
				for(String s : distribusi.keySet()) {
					List<Map> daftar = distribusi.get(s);
					
					String cab_bank = "";
					if(s.equals("BSM")) cab_bank = "1";
					else if(s.equals("Bancass2")) cab_bank = "2";
					
					
					params.put("cab_bank", cab_bank);
					if(!daftar.isEmpty()) {

						//Bagian ini untuk menghasilkan file dalam bentuk pdf
//						String outputFilename = r + "_" + s + ".pdf";
//						JasperUtils.exportReportToPdf(
//								props.getProperty("report.uw.summary." + r) + ".jasper", 
//								outputDir, outputFilename, params, daftar, PdfWriter.AllowPrinting, null, null);
						
//						Bagian ini untuk menghasilkan file dalam bentuk Xcel
						String outputFilename = r + "_" + s + ".xls";
						JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
								outputDir, outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");

//						buat testing						
//						JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
//								props.getProperty("upload.dir"), outputFilename, params, daftar, props.getProperty("report.uw.summary.sub.total")+ ".jasper");
						
						if(r.equals("Akseptasi_Khusus_Bancass_Syariah") || r.equals("Further_Requirements_Bancass_Syariah")) {
							List<File> attachments = daftarAttachment1.get(s);
							attachments.add(new File(outputDir + "\\" + outputFilename));
						}else {
							List<File> attachments = daftarAttachment2.get(s);
							attachments.add(new File(outputDir + "\\" + outputFilename));
						}						
					}
				}
			}
			
			//Looping untuk send email per masing2 distribusi
			for(String a : daftarAttachment1.keySet()) {
				List<File> attachments1 = daftarAttachment1.get(a);
				List<File> attachments2 = daftarAttachment2.get(a);
				
				//to = siti nihayatun; jelita; hisar; edy khohar; sadewo; 
				//cc = Underwriting (Bancass 1)
				
				// Andhika(13/12/2013) : Update tambah email mulyadiana@banksinarmas.com req : Asima
				String[] emailTo = {"siti_n@sinarmasmsiglife.co.id","Jelita@sinarmasmsiglife.co.id","Iriana@sinarmasmsiglife.co.id","Edy@sinarmasmsiglife.co.id","sadewo@sinarmasmsiglife.co.id", "mulyadiana@banksinarmas.com"};
				String[] emailCc = {"UnderwritingBancass@sinarmasmsiglife.co.id"};
				String[] emailBcc = {"canpri@sinarmasmsiglife.co.id"};
				
				//E-mail 1 : Akseptasi_Khusus & Further_Requirements
				if(!attachments1.isEmpty()) {
					logger.info("send mail...!");
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, new Date(), null, 
							true, "ajsjava@sinarmasmsiglife.co.id", 
							emailTo, emailCc, emailBcc,
							//new String[]{"canpri@sinarmasmsiglife.co.id"},null,null,
							"Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS Bancass Syariah s/d " + df.format(yesterday), 
							"Berikut adalah Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQUIREMENTS Bancass Syariah s/d " + df.format(yesterday) + 
							"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", attachments1, null);
					//email.send(true, "akhyar@sinarmasmsiglife.co.id", new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, "TES", "Tes Email", null);
					logger.info("send mail succes...!");
				}

				//E-mail 2 : Postpone & Decline
				/*if(!attachments2.isEmpty()) {
					email.send(true, "ajsjava@sinarmasmsiglife.co.id", 
							emailTo, emailCc, emailBcc,
//							new String[]{"Deddy@sinarmasmsiglife.co.id"},null,null,
							"Summary Follow-Up Surat Konfirmasi Case DECLINED / POSTPONED Bancass Syariah s/d " + df.format(yesterday), 
							"Berikut adalah Summary Follow-Up Surat Konfirmasi Case DECLINED / POSTPONED " + a + " s/d " + df.format(yesterday) + 
							"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", attachments2);
				}*/
					
			}
			
		} catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR" + e.getMessage();
			err=e.getLocalizedMessage();
			StringBuffer stackTrace = new StringBuffer();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			stackTrace.append(sw);
			try {
				email.send(
						false,
						props.getProperty("admin.ajsjava"),
						new String[] {"canpri@sinarmasmsiglife.co.id"}, 
						null, null, 
						"ERROR pada Scheduler Akseptasi BSM Syariah E-Lions " , 
						"Pesan Error : "+ stackTrace.toString(), 
						null);
			} catch (MailException e1) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e1);
				err=e1.getLocalizedMessage();
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e1);
				err=e1.getLocalizedMessage();
			}
		}		
		try {
			//String a = InetAddress.getLocalHost().getHostName();
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}		
	}
	
	public void schedulerCekMailPool() throws DataAccessException{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date nowDate = commonDao.selectSysdate();
		Integer countMailNotSent = uwDao.selectCountNotSentMstEmail();
		String pesan ="";
		
		
		if(countMailNotSent==0){
			pesan="Lapor!!! Hari ini Mail Pooling berhasil kekirim semua gan!";
		}else{
			pesan="Lapor!!! Hari ini Mail Pooling ada "+countMailNotSent+" biji yang kagak ke sent gan!";
		}
		EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
				null, 0, 0, nowDate, null, 
				true, "ajsjava@sinarmasmsiglife.co.id", 
//				new String[]{"deddy@sinarmasmsiglife.co.id"}, 
//				new String[]{"deddy@sinarmasmsiglife.co.id"},  
				new String[]{"berto@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id"}, 
				null, 
				null, 
				"Checking Mail Pooling tanggal "+df.format(nowDate), 
				pesan, 
				null, null);
	}
	
	/**
	 * Fungsi scheduller untuk auto transfer dari PRINT POLIS to UW untuk semua product bank sinarmas
	 * @author Andhika
	 * @throws DataAccessException
	 */
	public void schedulerTransPolToUw(String msh_name) throws DataAccessException{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate 	= new Date();
		List<Map> data = new ArrayList<Map>();
		data = uwDao.selectTransPolToUw();
		String desc	= "OK";
		String err="";
		
		for(int i=0; i<data.size();i++){
			try{
				Map x = data.get(i);
				String spaj = x.get("REG_SPAJ").toString();
				String lsbs_id = x.get("LSBS_ID").toString();
				Date begdate = (Date) x.get("MSTE_BEG_DATE");
				Date mspo_print_date = (Date) x.get("MSPO_DATE_PRINT");
				String lusid =  "0";
				
				Date Sys = (Date) commonDao.selectSysdate();
				String Sysdat = df.format(Sys);
				
				int li_lspd;
				li_lspd = 27;// UW - SPEEDY : RYAN
				String ls_ket;
				
				String lcaid = uwDao.selectCabangFromSpaj(spaj);
				Integer noInsured = 1;
				Integer lssaId = 10;
				Integer isInputanBank = -1;
					    isInputanBank = bacDao.selectIsInputanBank(spaj);
				
				Integer count = uwDao.selectCountProductSimasPrimaAkseptasiKhusus(spaj, 1,10, isInputanBank);
				boolean xx =  selectValidasiCheckListBySpaj(spaj);
				
				if(count <= 0){
					if(!xx){
						
						// *Update mst_insured
						uwDao.updateMstInsured(spaj, li_lspd, 17, 1, null);
						// *Insert ke mst_position_spaj
						uwDao.insertMstPositionSpaj("0", "AUTOTRANSFER KE UW SPEEDY", spaj, 0);
						uwDao.saveMstTransHistory(spaj, "tgl_transfer_uw_speedy", null, null, null);
						
						// *Update mst_billink
						this.uwDao.updateMst_billing(spaj, null, null, null, 1, 1, li_lspd, null, null);

						if(products.stableLink(lsbs_id)) { //stable
							Integer li_topup = this.uwDao.validationStableLink(spaj);
							if(li_topup != null) if(li_topup==2) {
								this.uwDao.updateMst_billing(spaj, null, null, null, 1, 2, li_lspd, null, null);
							}
							
						}else if(products.unitLink(lsbs_id)) {
							int li_ke = 0;
							Integer li_topup = this.uwDao.validationTopup(spaj);
							if(li_topup!=null)if(li_topup>=1) {
								li_ke=2;
								this.uwDao.updateMst_billing(spaj, null, null, null, 1, 2, li_lspd, null, null);
							}
							if(this.uwDao.validationTopup3(spaj) != null) {
								this.uwDao.updateMst_billing(spaj, null, null, null, 1, 3, li_lspd, null, null);
							}
							
							this.uwDao.updateMstUlinkTransferTTP(spaj, li_ke);
							this.uwDao.updateMstTransUlinkTransferTTP(spaj, li_ke);
							
							
						}
						
						uwDao.updateMstInsuredTgl(spaj, 1, FormatDate.add(mspo_print_date,Calendar.DATE, 1), 1);
						uwDao.saveMstTransHistory(spaj, "tgl_kirim_polis", FormatDate.add(mspo_print_date,Calendar.DATE, 1), null, null);
						
						// *Update mst_policy
						uwDao.updatePosisiMst_policy(spaj, new Integer(li_lspd));
						// *Insert snows
						uwDao.prosesSnows(spaj, lusid, 202, 212);
					}
				}
				
			}
			catch (Exception e) {
				desc = "ERROR";
				logger.error("ERROR :", e);
				err=e.getLocalizedMessage();
			}
		}
		try {
			insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
			msh_name, bdate, new Date(), desc,err);
		}
		catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	public boolean selectValidasiCheckListBySpaj(String reg_spaj) throws DataAccessException{
		Map m = (HashMap) querySingle("selectValidasiCheckListBySpaj", reg_spaj);
		if(m == null) return false;
		
		int adm 	= ((BigDecimal) m.get("ADM")).intValue();
		int uw 		= ((BigDecimal) m.get("UW")).intValue();
		int print 	= ((BigDecimal) m.get("PRINT")).intValue();
		int filling	= ((BigDecimal) m.get("FILLING")).intValue();
		
		Integer lspd_id = (Integer) querySingle("selectPosisiDokumenBySpaj", reg_spaj);
		Integer lssa_id = (Integer) querySingle("selectStsAksepBySpaj", reg_spaj);
		
		if(lspd_id == 1 && adm == 0 ) return false;
		else if(lspd_id == 2 && uw == 0 ) return false;
		else if(lspd_id == 6 && print == 0) return false;
		else if(lspd_id == 7 && filling == 0 ) return false;
		else return true;
	}
	
	/**
	 * Reminder pengiriman hadiah pada program hadiah hari H
	 * @author Canpri
	 * @since 22 Oct 2012
	 * @param msh_name
	 */
	public void schedulerAppointment(String msh_name) throws DataAccessException{
		Date bdate 	= commonDao.selectSysdate();
		String desc	= "OK";
		String err="";
		Integer no = 0;
		Integer b1 = 0;
		Integer dm = 0;
		
		SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
		StringBuffer pesan = new StringBuffer();
		
		try{
			List data = new ArrayList();
			List<Hadiah> hadiah = uwDao.selectAppointmentProgramHadiah(defaultDateFormat.format(bdate));
			
			if(!hadiah.isEmpty()){
				pesan.append("<p><strong>SCHEDULER APPOINTMENT</strong> <br><br>");
				pesan.append("Mohon agar dapat dikirimkan hadiah nasabah<br><br>");
				
				for(int i=0;i<hadiah.size();i++){
					HashMap m = new HashMap();
	                Hadiah n = hadiah.get(i);
	                no += 1;
	                
	                Pemegang pemegang = bacDao.selectpp(n.reg_spaj);
					Agen agen = bacDao.select_detilagen(n.reg_spaj);
					Hadiah jn_hadiah= basDao.selectLstHadiah(n.getLhc_id(),n.getLh_id());
					String lca_id = uwDao.selectCabangFromSpaj(n.reg_spaj);
					Product produk = uwDao.selectMstProductInsuredUtamaFromSpaj(n.reg_spaj);
					
					double pr = produk.getMspr_premium();
					
					String team_name = uwDao.selectBancassTeam(n.reg_spaj);
					if(team_name==null){
						team_name= "";
					}
					
					String cb = "";
					if("37,52".indexOf(lca_id)>-1) { //Agency
					       cb = "Agency";
					}else if(lca_id.equals("46")) { //Hybrid
					       cb = "Hybrid";
					}else if(lca_id.equals("09")) { //Bancassurance
					       if(team_name.toUpperCase().equals("TEAM JAN ROSADI")) { //Bancassurance2
					    	   cb = "Bancassurance 2";
					       }else if(team_name.toUpperCase().equals("TEAM DEWI")) { //Bancassurance3
					    	   cb = "Bancassurance 3";
					       }else if(team_name.toUpperCase().equals("TEAM YANTI SUMIRKAN")) { //Bancassurance1
					    	   cb = "Bancassurance 1";
					    	   b1 += 1;
					       }else{
					    	   cb = "Bancassurance";
					       }
					}else if(lca_id.equals("08") || lca_id.equals("42")) { //Worksite
					       cb = "Worksite";
					}else if(lca_id.equals("55")) { //DM/TM
					       cb = "DMTM";
					       dm += 1;
					}else if(lca_id.equals("58")) { //MallAssurance
					       cb = "MallAssurance";
					       dm += 1;
					}else { //Regional
					       cb = "Regional";
					}
					
					//Format isi email
					
					pesan.append(no+". - Nama\t : "+pemegang.getMcl_first()+"</strong><br>");
					pesan.append("   - No polis\t : "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+"<br>");
					pesan.append("   - Premi\t : Rp. "+FormatString.formatCurrency("", new BigDecimal(pr))+"<br>");
					pesan.append("   - Penutup\t : "+agen.getMcl_first()+" ("+cb+")<br>");
					pesan.append("   - Appointment dgn nasabah\t : "+FormatDate.getDayInWeekIndonesia(n.mh_tgl_kirim_hadiah)+", "+FormatDate.toIndonesian(n.mh_tgl_kirim_hadiah)+" (jam "+dt.format(n.mh_tgl_kirim_hadiah)+")<br><br>");
				}
				
				/*Email ke : 	Ferry (GA), 
				Sanga (GA), 
				Saphry (Purchasing), 
				Lucianna (Purchasing), 
				Meidy (Purchasing)
				cc : User input reschedule, 
					GM user input reschedule
					Jelita  dan Hisar*/
				
				String from = props.getProperty("admin.ajsjava");
				//String[] to = new String[]{"canpri@sinarmasmsiglife.co.id","Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id"};
				String[] cc = null;
				//String[] bcc = null;
				
				String[] to = new String[]{"Ferryanto@sinarmasmsiglife.co.id","Sanga@sinarmasmsiglife.co.id", "Saphry@sinarmasmsiglife.co.id","lucianna@sinarmasmsiglife.co.id",
								"meidytumewu@sinarmasmsiglife.co.id"};
				if(b1>0 && dm>0){
					cc = new String[]{"Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id", "yantisumirkan@sinarmasmsiglife.co.id", "Gideon@sinarmasmsiglife.co.id"};
				}else if(b1>0 && dm==0){
					cc = new String[]{"Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id", "yantisumirkan@sinarmasmsiglife.co.id"};
				}else if(b1==0 && dm>0){
					cc = new String[]{"Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id", "Gideon@sinarmasmsiglife.co.id"};
				}else{
					cc = new String[]{"Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id"};
				}
				//String[] cc = new String[]{"Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id", email_cc};
				String[] bcc = new String[]{"canpri@sinarmasmsiglife.co.id", "antasari@sinarmasmsiglife.co.id", "deddy@sinarmasmsiglife.co.id"};
				String message = pesan.toString();
				String subject = "Program Hadiah Stable Save (SCHEDULER APPOINTMENT "+defaultDateFormat.format(bdate)+")";
				
				EmailPool.send("Program Hadiah E-Lions", 1, 1, 0, 0, null, 0, 0, bdate, null, true, from, to, cc, bcc, subject, message, null, null);
			}
		} catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR" + e.getMessage();
			err=e.getLocalizedMessage();
			StringBuffer stackTrace = new StringBuffer();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			stackTrace.append(sw);
			try {
				EmailPool.send("[ERROR]Program Hadiah E-Lions", 1, 1, 0, 0, null, 0, 0, bdate, null, 
						false,
						props.getProperty("admin.ajsjava"),
						new String[] {"canpri@sinarmasmsiglife.co.id"}, 
						null, null, 
						"ERROR pada reminder pengiriman hadiah Program Hadiah" , 
						"Pesan Error : "+ stackTrace.toString(), 
						null, null);
			} catch (MailException e1) {
				logger.error("ERROR :", e1);
				err=e1.getLocalizedMessage();
			}
		}		
		try {
			//String a = InetAddress.getLocalHost().getHostName();
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}		
	}
	
	/**
	 * Randy (01/03/2016)
	 * Req : Titis
	 */	
	public void schedulerAutomailSummaryFurther(String msh_name) throws DataAccessException{
		String desc = "OK";
        String err = "";    
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date bdate = new Date();
        List<Map> data = new ArrayList<Map>();
        List<Map> data2 = new ArrayList<Map>();
        data = uwDao.selectNpwIndividu(1);
        data2 = uwDao.selectNpwIndividu(2);
        
        HashMap mapEmail1 = uwDao.selectEmailBancass("S78");
        HashMap mapEmail2 = uwDao.selectEmailBancass("S77");
        HashMap mapEmail3 = uwDao.selectEmailBancass("S81");
        HashMap mapEmail4 = uwDao.selectEmailBancass("S123");
        HashMap mapEmail5 = uwDao.selectEmailBancass("S79");
        HashMap mapEmail6 = uwDao.selectEmailBancass("S80");
        HashMap mapEmail7 = uwDao.selectEmailBancass("S161");
        HashMap mapEmail8 = uwDao.selectEmailBancass("S162");
        HashMap mapEmail9 = uwDao.selectEmailBancass("S163");
        HashMap mapEmail10 = uwDao.selectEmailBancass("S164");
        HashMap mapEmail11 = uwDao.selectEmailBancass("S279");
        HashMap mapEmail12 = uwDao.selectEmailBancass("S280");
        HashMap mapEmail13 = uwDao.selectEmailBancass("S443");
        HashMap mapEmail14 = uwDao.selectEmailBancass("S441");
//      HashMap mapEmail15 = uwDao.selectMstConfig(6, "UWHelpdesk", "UWHelpdesk"); //tidak ada kanwilnya, dikirim ke uwHelpdesk
        HashMap mapEmailUW = uwDao.selectMstConfig(6, "KANWIL_UW", "KANWIL_UW"); // email ke uw helpdesk

        String from = props.getProperty("admin.ajsjava");
        String[] to1 = null, to2 = null, to3 = null, to4 = null, to5 = null, to6 = null, to7 = null, to8 = null, to9 = null, to10 = null, to11 = null, to12 = null,  to13 = null, to14 = null,  to15 = null, 
        		 cc1 = null, cc2=null, cc3=null, cc4=null, cc5=null, cc6=null, cc7=null, cc8=null, cc9=null, cc10=null, cc11=null, cc12=null, cc13=null, cc14=null, cc15=null, touw = null, ccuw = null,
        		 bcc1=null,bcc2=null,bcc3=null,bcc4=null,bcc5=null,bcc6=null,bcc7=null,bcc8=null,bcc9=null,bcc10=null,bcc11=null,bcc12=null,bcc13=null,bcc14=null;
            
    	to1 = mapEmail1.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc1 = mapEmail1.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc1 = mapEmail1.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to2 = mapEmail2.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc2 = mapEmail2.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc2 = mapEmail2.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to3 = mapEmail3.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc3 = mapEmail3.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc3 = mapEmail3.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to4 = mapEmail4.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc4 = mapEmail4.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc4 = mapEmail4.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to5 = mapEmail5.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc5 = mapEmail5.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc5 = mapEmail5.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to6 = mapEmail6.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc6 = mapEmail6.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc6 = mapEmail6.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to7 = mapEmail7.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc7 = mapEmail7.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc7 = mapEmail7.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to8 = mapEmail8.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc8 = mapEmail8.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc8 = mapEmail8.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to9 = mapEmail9.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc9 = mapEmail9.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc9 = mapEmail9.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to10 = mapEmail10.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc10 = mapEmail10.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc10 = mapEmail10.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to11 = mapEmail11.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc11 = mapEmail11.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc11 = mapEmail11.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to12 = mapEmail12.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc12 = mapEmail12.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc12 = mapEmail12.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to13 = mapEmail13.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc13 = mapEmail13.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc13 = mapEmail13.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
    	to14 = mapEmail14.get("EMAIL_CAB")!=null?mapEmail1.get("EMAIL_CAB").toString().split(";"):null;
    	cc14 = mapEmail14.get("EMAIL_CAB_CC")!=null?mapEmail1.get("EMAIL_CAB_CC").toString().split(";"):null;
    	bcc14 = mapEmail14.get("EMAIL_CAB_BCC")!=null?mapEmail1.get("EMAIL_CAB_BCC").toString().split(";"):null;
    	
//    	to15 = mapEmail15.get("NAME")!=null?mapEmail15.get("NAME").toString().split(";"):null; // tanpa wil_no, tujuannya ke uwHelpdesk
//    	cc15 = mapEmail15.get("NAME2")!=null?mapEmail15.get("NAME2").toString().split(";"):null;
           
        touw = mapEmailUW.get("NAME")!=null?mapEmailUW.get("NAME").toString().split(";"):null;
        ccuw = mapEmailUW.get("NAME2")!=null?mapEmailUW.get("NAME2").toString().split(";"):null;
        
        String[] bcc = mapEmailUW.get("NAME3")!=null?mapEmailUW.get("NAME3").toString().split(";"):null;
                    
        try{                  
                     StringBuffer pesan = new StringBuffer();
                     pesan.append("<body><b>Dear Team </b><br><br>Mohon bantuannya untuk melakukan follow up ke masing-masing nasabah berikut ini supaya polis dapat segera diterbitkan. Terlampir data-data  SPAJ yang apabila tidak dilengkapi akan kami Bekukan dan  akam kami proses Refund ke Rekening yang tercantum dalam  SPAJ: <br><br>");
                     pesan.append("<table border ='1' cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1200px'>");
                     pesan.append("<tr bgcolor='#A8B8EE'>");
                     pesan.append("<th align='center'>No.</th>");
                     pesan.append("<th align='center'>Cabang Penutup</th>");
                     pesan.append("<th align='center'>Nama Penutup/AO/BAC</th>");
                     pesan.append("<th align='center'>Cabang Refferal</th>");
                     pesan.append("<th align='center'>Nama Refferal</th>");
                     pesan.append("<th align='center'>Nama PP</th>");
                     pesan.append("<th align='center'>No. SPAJ</th>");
                     pesan.append("<th align='center'>Nama Produk</th>");
                     pesan.append("<th align='center'>Premi</th>");
                     pesan.append("<th align='center'>Tgl Further</th>");
                     pesan.append("<th align='center'>Date Line</th>");
                     pesan.append("<th align='center'>Keterangan</th></tr>");
                     
                     StringBuffer pesanh2 = new StringBuffer();
                     pesanh2.append("<body><b>Dear Team </b><br><br>Terlampir data-data  SPAJ NPW Individu untuk dicek dan dibatalkan.");
                     pesanh2.append("<br><br>");
                     pesanh2.append("<table border ='1' cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1200px'>");
                     pesanh2.append("<tr bgcolor='#A8B8EE'>");
                     pesanh2.append("<th align='center'>No.</th>");
                     pesanh2.append("<th align='center'>Channel</th>");
 					 pesanh2.append("<th align='center'>Reg. Spaj</th>");
 					 pesanh2.append("<th align='center'>Produk</th>");
 					 pesanh2.append("<th align='center'>Plan</th>");
 					 pesanh2.append("<th align='center'>Tgl. NPW</th>");
 					 pesanh2.append("<th align='center'>Tgl. Input</th>");
 					 pesanh2.append("<th align='center'>Pemegang</th>");
 					 pesanh2.append("<th align='center'>Posisi</th>");
 					 pesanh2.append("<th align='center'>Cabang</th>");					
 					 pesanh2.append("<th align='center'>Region/Site</th>");
 					 pesanh2.append("<th align='center'>Keterangan</th></tr>");
              
                     int no1=1, no2=1, no3=1, no4=1, no5=1, no6=1, no7=1, no8=1, no9=1, no10=1, no11=1, no12=1, no13=1, no14=1, no15=1;
                     boolean pesan0 = false, 
                    		 pesan1 = false, pesan2 = false, pesan3 = false, pesan4 = false, pesan5 = false, pesan6 = false, pesan7 = false, 
                    		 pesan8 = false, pesan9 = false, pesan10 = false, pesan11 = false, pesan12 = false, pesan13 = false, pesan14 = false, 
                    		 pesan15 = false, pesan30 = false;
                                        
                     StringBuffer pesana1 = new StringBuffer(), pesana2 = new StringBuffer(), pesana3 = new StringBuffer(), pesana4 = new StringBuffer(), 
	                    		  pesana5 = new StringBuffer(), pesana6 = new StringBuffer(), pesana7 = new StringBuffer(), pesana8 = new StringBuffer(), 
	                    		  pesana9 = new StringBuffer(), pesana10 = new StringBuffer(), pesana11 = new StringBuffer(), pesana12 = new StringBuffer(), 
	                    		  pesana13 = new StringBuffer(), pesana14 = new StringBuffer(), pesana15 = new StringBuffer(), footer = new StringBuffer(), 
	                    		  pesanUW = new StringBuffer();  
                     
                     footer.append("</table> <br><br>");
                     footer.append("<b>Terima Kasih</b><br><br>");
                           
                     for(int i=0; i<data.size();i++){
                    	 	String wil = "";
                            Map x = data.get(i);
                            Integer telat =((BigDecimal) x.get("HARI_TELAT")).intValue();
                            if(x.get("WIL_NO")!= null){
                            	wil = x.get("WIL_NO").toString();
                            }
                            String spaj = x.get("NO_SPAJ").toString();
                            String bagian = x.get("BAGIAN").toString();
                                                      
                            if(telat >= 20 && telat <= 30){
                            	if(x.get("BAGIAN").toString().equals("a")){
                            		if(wil.equals("S78")){
	                                   pesana1.append("<tr><td nowrap>"+ no1++  + "</td>");
	                                   pesana1.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana1.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana1.append("<td>"+ " " + "</td>");}
	                                   pesana1.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana1.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana1.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana1.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana1.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana1.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana1.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana1.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana1.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan1 = true; 
                            		}else if (wil.equals("S77")){
	                                   pesana2.append("<tr><td nowrap>"+ no2++  + "</td>");
	                                   pesana2.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana2.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana2.append("<td>"+ " " + "</td>");}
	                                   pesana2.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana2.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana2.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana2.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana2.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana2.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana2.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana2.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana2.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan2 = true;
                            		}else if (wil.equals("S81")){
	                                   pesana3.append("<tr><td nowrap>"+ no3++  + "</td>");
	                                   pesana3.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana3.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana3.append("<td>"+ " " + "</td>");}
	                                   pesana3.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana3.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana3.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana3.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana3.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana3.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana3.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana3.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana3.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan3 = true; 
                            		}else if (wil.equals("S123")){
	                                   pesana4.append("<tr><td nowrap>"+ no4++  + "</td>");
	                                   pesana4.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana4.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana4.append("<td>"+ " " + "</td>");}
	                                   pesana4.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana4.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana4.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana4.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana4.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana4.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana4.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana4.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana4.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan4 = true; 
                            		}else if (wil.equals("S79")){
	                                   pesana5.append("<tr><td nowrap>"+ no5++  + "</td>");
	                                   pesana5.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana5.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana5.append("<td>"+ " " + "</td>");}
	                                   pesana5.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana5.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana5.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana5.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana5.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana5.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana5.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana5.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana5.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan5 = true; 
                            		}else if (wil.equals("S80")){
	                                   pesana6.append("<tr><td nowrap>"+ no6++  + "</td>");
	                                   pesana6.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana6.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana6.append("<td>"+ " " + "</td>");}
	                                   pesana6.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana6.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana6.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana6.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana6.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana6.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana6.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana6.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana6.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan6 = true; 
                            		}else if (wil.equals("S161")){
	                                   pesana7.append("<tr><td nowrap>"+ no7++  + "</td>");
	                                   pesana7.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana7.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana7.append("<td>"+ " " + "</td>");}
	                                   pesana7.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana7.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana7.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana7.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana7.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana7.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana7.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana7.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana7.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan7 = true; 
                            		}else if (wil.equals("S162")){
	                                   pesana8.append("<tr><td nowrap>"+ no8++  + "</td>");
	                                   pesana8.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana8.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana8.append("<td>"+ " " + "</td>");}
	                                   pesana8.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana8.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana8.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana8.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana8.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana8.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana8.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana8.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana8.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan8 = true; 
                            		}else if (wil.equals("S163")){
	                                   pesana9.append("<tr><td nowrap>"+ no9++  + "</td>");
	                                   pesana9.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana9.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana9.append("<td>"+ " " + "</td>");}
	                                   pesana9.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana9.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana9.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana9.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana9.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana9.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana9.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana9.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana9.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan9 = true; 
                            		}else if (wil.equals("S164")){
	                                   pesana10.append("<tr><td nowrap>"+ no10++  + "</td>");
	                                   pesana10.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana10.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana10.append("<td>"+ " " + "</td>");}
	                                   pesana10.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana10.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana10.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana10.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana10.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana10.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana10.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana10.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana10.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan10 = true; 
                            		}else if (wil.equals("S279")){
	                                   pesana11.append("<tr><td nowrap>"+ no11++  + "</td>");
	                                   pesana11.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana11.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana11.append("<td>"+ " " + "</td>");}
	                                   pesana11.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana11.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana11.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana11.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana11.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana11.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana11.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana11.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana11.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan11 = true; 
                            		}else if (wil.equals("S280")){
	                                   pesana12.append("<tr><td nowrap>"+ no12++  + "</td>");
	                                   pesana12.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana12.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana12.append("<td>"+ " " + "</td>");}
	                                   pesana12.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana12.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana12.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana12.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana12.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana12.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana12.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana12.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana12.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan12 = true; 
                            		}else if (wil.equals("S443")){
	                                   pesana13.append("<tr><td nowrap>"+ no13++  + "</td>");
	                                   pesana13.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana13.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana13.append("<td>"+ " " + "</td>");}
	                                   pesana13.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana13.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana13.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana13.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana13.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana13.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana13.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana13.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana13.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan13 = true; 
                            		}else if (wil.equals("S441")){
	                                   pesana14.append("<tr><td nowrap>"+ no14++  + "</td>");
	                                   pesana14.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	                                   if(x.get("NAMA_PENUTUP")!=null) {pesana14.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana14.append("<td>"+ " " + "</td>");}
	                                   pesana14.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	                                   pesana14.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	                                   pesana14.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	                                   pesana14.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	                                   pesana14.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	                                   pesana14.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	                                   pesana14.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	                                   pesana14.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	                                   pesana14.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
	                                   pesan14 = true; 
                            		}else {    pesana15.append("<tr><td nowrap>"+ no15++  + "</td>");
				                               pesana15.append("<td>"+ x.get("CABANG_1") + "</td>"); 
				                               if(x.get("NAMA_PENUTUP")!=null) {pesana15.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana15.append("<td>"+ " " + "</td>");}
				                               pesana15.append("<td>"+ x.get("CABANG_REFF") + "</td>");
				                               pesana15.append("<td>"+ x.get("NAMA_REFF") + "</td>");
				                               pesana15.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
				                               pesana15.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
				                               pesana15.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
				                               pesana15.append("<td nowrap>"+ x.get("PREMI") + "</td>");
				                               pesana15.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
				                               pesana15.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
				                               pesana15.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
				                               pesan15 = true; 
                            		}
                            	}// end of bagian a
                            	
                            		if(x.get("BAGIAN").toString().equals("b")){
                            			 StringBuffer pesana0 = new StringBuffer();
                            			 int no0=1;
                            			 String[] to0 = null, cc0 = null;
                            			 
//                            		     HashMap mapEmail0 = uwDao.selectMstConfig(6, "KANWIL_1", "KANWIL_1");
                            			 to0 = uwDao.selectEmailCabangPenutup(spaj,1).split(";");
                            			 
//                            			 to0 = mapEmail0.get("NAME")!=null?mapEmail0.get("NAME").toString().split(";"):null;
//                            		     cc0 = mapEmail0.get("NAME2")!=null?mapEmail0.get("NAME2").toString().split(";"):null;
                            		     
	   	                            	 pesana0.append("<tr><td nowrap>"+ no0++  + "</td>");
	   	                                 pesana0.append("<td>"+ x.get("CABANG_1") + "</td>"); 
	   	                                 if(x.get("NAMA_PENUTUP")!=null) {pesana0.append("<td>"+ x.get("NAMA_PENUTUP") + "</td>");}else{pesana0.append("<td>"+ " " + "</td>");}
	   	                                 pesana0.append("<td>"+ x.get("CABANG_REFF") + "</td>");
	   	                                 pesana0.append("<td>"+ x.get("NAMA_REFF") + "</td>");
	   	                                 pesana0.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
	   	                                 pesana0.append("<td nowrap>"+ x.get("NO_SPAJ") + "</td>");
	   	                                 pesana0.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
	   	                                 pesana0.append("<td nowrap>"+ x.get("PREMI") + "</td>");
	   	                                 pesana0.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
	   	                                 pesana0.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
	   	                                 pesana0.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");

	   	                              try {EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
	   	                                   from, to0, cc0, bcc,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana0.toString()+footer.toString(),  null, null);
	   	     						       }
	   	     						catch (MailException e1) {
	   	     						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
	   	     						      }
	   	                              
		   	                              pesana0 = null;
		   	                              no0 = 0;
                            		} //end of bagian b                       	
                            } //end of telat 20-30        
                     } //end of for1  
                     
                     for(int i=0; i<data2.size();i++){
                         Map x = data2.get(i);
                         Integer telat =((BigDecimal) x.get("HARI_TELAT")).intValue();

                         String spaj = x.get("NO_SPAJ").toString();
                         String bagian = x.get("BAGIAN").toString();
                     if(telat > 30){                    
                            pesanUW.append("<tr><td nowrap>"+ no2++  + "</td>");
                            pesanUW.append("<td>"+ x.get("CHANNEL") + "</td>"); 
                            pesanUW.append("<td>"+ x.get("NO_SPAJ") + "</td>");
                            pesanUW.append("<td>"+ x.get("NAMA_PRODUK") + "</td>");
                            pesanUW.append("<td>"+ x.get("PLAN") + "</td>"); 
                            pesanUW.append("<td>"+ x.get("TGL_NPW") + "</td>"); 
                            pesanUW.append("<td>"+ x.get("TGL_INPUT") + "</td>");
                            pesanUW.append("<td>"+ x.get("NAMA_PEMEGANG") + "</td>");
                            pesanUW.append("<td>"+ x.get("POSISI") + "</td>");
                            pesanUW.append("<td>"+ x.get("CABANG_1") + "</td>");
                            pesanUW.append("<td>"+ x.get("REGION") + "</td>");
                            pesanUW.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
                            pesan30 = true;
                            
                          uwDao.updateMstPolicy(spaj, 999);
                          uwDao.updateMstPolicyLsspdId(spaj, 29);
                          uwDao.insertMstPositionSpaj("0", "AUTOPROSES UPDATE KE NOT PROCEED WITH POLIS-POLIS NON-DMTM > 30 HARI (KALENDER)", spaj, 0);
                          uwDao.prosesSnows(spaj, "0", 999, 212);
                     	}
                     } //end of for2
                     
                     if(pesan1){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to1, cc1, bcc1,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana1.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan2){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to2, cc2, bcc2,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana2.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan3){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to3, cc3, bcc3,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana3.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan4){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to4, cc4, bcc4,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana4.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan5){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to5, cc5, bcc5,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana5.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan6){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to6, cc6, bcc6,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana6.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan7){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to7, cc7, bcc7,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana7.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan8){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to8, cc8, bcc8,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana8.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan9){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to9, cc9, bcc9,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana9.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan10){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to10, cc10, bcc10,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana10.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan11){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to11, cc11, bcc11,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana11.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan12){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to12, cc12, bcc12,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana12.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan13){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to13, cc13, bcc13,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana13.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan14){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to14, cc14, bcc14,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana14.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan15){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, touw, ccuw, bcc,"WARNING !! Follow up SPAJ NON-DMTM yang akan kadaluarsa dan proses refund", pesan.toString()+pesana15.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}
                     if(pesan30){                        
						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
	                         from, touw, ccuw, bcc,"SPAJ NPW NON-DMTM", pesanh2.toString()+pesanUW.toString()+footer.toString(),  null, null);
						       }
						catch (MailException e1) {
						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
						      }
					    }  
              }// end of try
                            	      
        catch (Exception e) {
            desc = "ERROR";
            logger.error("ERROR :", e);
            err=e.getLocalizedMessage();
                        EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                      props.getProperty("admin.ajsjava"), new String[]{"randy@sinarmasmsiglife.co.id"}, new String[]{"ryan@sinarmasmsiglife.co.id"}, bcc, 
                                      "ERROR SCHEDULER PROSES NPW (NOT PROCEED WITH) DMTM", 
                                      err, null, null);
              }
      try {
            insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
            msh_name, bdate, new Date(), desc,err);
      }
      catch (UnknownHostException e) {
            logger.error("ERROR :", e);
      }
	}			
	
	/**
	 * Andhika (01/06/2013)
	 * Req : Novie
	 * Update tambah Email (03/12/2013)
	 */	
	public void schedulerFollowUpValidPolis(String msh_name) throws DataAccessException{
		Date bdate 	= commonDao.selectSysdate();	
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		User currentUser = new User();
		String desc	= "OK";
		String err="";
		
		List<Map> dat = new ArrayList<Map>();
		dat = uwDao.selectFollowUpPolis();
		
			try{			
				StringBuffer pesan = new StringBuffer();
				//Format isi email

				pesan.append("<body>Berikut Polis New Simas Stabil link yang sudah diaksep namun belum di cetak di cabang");
				pesan.append("<br>");
				pesan.append("<table cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1000px'>");
				pesan.append("<tr bgcolor='#A8B8EE'><th align='left'>No.</th>");
				pesan.append("<th align='left'>No. Spaj</th>");
				pesan.append("<th align='left'>No. Polis</th>");
				pesan.append("<th  align='left'>Nama Agen</th>");
				pesan.append("<th align='left'>Pemegang Polis</th>");
				pesan.append("<th align='left'>Tanggal Valid</th>"); // g.nama_cabang
				pesan.append("<th align='left'>Cabang bank</th></tr>");
				pesan.append("<th align='left'>Email</th></tr>");
				
				int no = 0;
				for(int i=0; i<dat.size();i++){
					Map x = dat.get(i);
						pesan.append("<td nowrap>"+ x.get("NO") + "</td>");
						pesan.append("<td>"+ x.get("REG_SPAJ") + "</td>");
						pesan.append("<td>"+ x.get("MSPO_POLICY_NO_FORMAT") + "</td>");
						pesan.append("<td nowrap>"+ x.get("NAMA_AGENT") + "</td>");
						pesan.append("<td nowrap>"+ x.get("NAMA_PP") + "</td>");
						pesan.append("<td nowrap>"+ x.get("TANGGAL_VALID") + "</td>");
						pesan.append("<td nowrap>"+ x.get("NAMA_CABANG") + "</td></tr>");
						pesan.append("<td nowrap>"+ x.get("EMAIL") + "</td></tr>");
				}
				pesan.append("</table> <br><br>");
				pesan.append("Terima Kasih");
				
				String from = props.getProperty("admin.ajsjava");
				String emailto = "Bancass_Medan@sinarmasmsiglife.co.id;Bancass_Bandung@sinarmasmsiglife.co.id;banc_semarang@sinarmasmsiglife.co.id;bancass_lampung@sinarmasmsiglife.co.id;Bancass_Yogya@sinarmasmsiglife.co.id;Edy@sinarmasmsiglife.co.id;Iriana@sinarmasmsiglife.co.id;Jelita@sinarmasmsiglife.co.id;adminbamdo@sinarmasmsiglife.co.id;Bancass_Bali@sinarmasmsiglife.co.id;bancass_makassar@sinarmasmsiglife.co.id;Hendry@sinarmasmsiglife.co.id;patricia@sinarmasmsiglife.co.id;bancass_balikpapan@sinarmasmsiglife.co.id;bancass_malang@sinarmasmsiglife.co.id;";
				String emailcc = "UnderwritingBancass@sinarmasmsiglife.co.id;";
				String emailbcc = "andhika@sinarmasmsiglife.co.id;";
				
				if(dat.size()!=0){
					if(!emailto.trim().equals("")){	
						String[] to = emailto.split(";");
						String[] cc = emailcc.split(";");
						String[] bcc = emailbcc.split(";");

						try {
							email.send(true, from, to, cc, bcc,
							"Follow up New Simas Stabil link yang sudah diaksep namun belum di cetak di cabang",
							pesan.toString(), null);
						}
						catch (MailException e1) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
						}
						catch (MessagingException e1) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
						}
					}
				}
			}catch (Exception e) {
				logger.error("ERROR :", e);err=e.getLocalizedMessage();
			}
			try {
				insertMstSchedulerHist(
						InetAddress.getLocalHost().getHostName(),
						msh_name, bdate, new Date(), desc,err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
	}

	public void schedulerExpiredLisensiAgent(String msh_name) {
		Date bdate 	= commonDao.selectSysdate();	
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String desc	= "OK";
		String err="";
		
		List l_email = basDao.selectEmailCabangLisensiAgent();
		
		for(int i=0;i<l_email.size();i++){
			Map m = new HashMap();
			m = (Map) l_email.get(i);
			Integer no = ((BigDecimal)m.get("NO")).intValue();
			String email_cab = (String)m.get("LAR_EMAIL");
			
			List data = basDao.selectReport_LisensiAgent(null, null, "2", null, email_cab, null, null, null);
			
			try{
				Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("jenis", "2");
	    		params.put("pdate", df.format(bdate));
	    		
	    		String em = email_cab.substring(0, email_cab.indexOf("@"));
	    		
				if(data.size()!=0){
					if(!email_cab.trim().equals("")){	
						
						String outputDir = props.getProperty("pdf.dir.report") + "\\expired_lisensi_agent\\";
						//String outputDir = "D:\\test\\expired_lisensi\\";
						String outputFilename = no+"_"+em+"_expired_lisensi_agent.xls";
			    		
						JasperUtils.exportReportToXls(props.getProperty("report.bas.report_lisensi_agen") + ".jasper", 
								outputDir, outputFilename, params, data, null);
						
						List<File> attachments = new ArrayList<File>();
						attachments.add(new File(outputDir + "\\" + outputFilename));
						
						StringBuffer pesan = new StringBuffer();
						//Format isi email
			
						pesan.append("Berikut adalah List Lisensi Agent bulan "+new SimpleDateFormat("MMMM yyyy").format(bdate));
						//pesan.append("\n\nnb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.");
						//pesan.append("\n\nHarap tidak me-reply email ini.");
						
						String from = props.getProperty("admin.ajsjava");
						String emailto = email_cab;
						String emailcc = null;
						String emailbcc = null;
						
						String[] to = emailto.split(";");
						//String[] cc = emailcc.split(";");
						//String[] bcc = emailbcc.split(";");
						
						try {
							//use email pool
							EmailPool.send("E-Lions", 1, 1, 0, 0, 
									null, 0, 0, bdate, null, 
									true, from, 
									to, 
									null, 
									null, 
									"List Lisensi Agent yang akan expired", 
									pesan.toString(), 
									attachments, null);
							
						}
						catch (MailException e1) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e1);
							err=e1.getLocalizedMessage();
						}
						
						
						/*String[] to = emailto.split(";");
						//String[] cc = emailcc.split(";");
						//String[] bcc = emailbcc.split(";");
	 
						try {
							email.send(true, from, to, null, null,
							"List Lisensi Agent yang akan expired",
							pesan.toString(), attachments);
						}
						catch (MailException e1) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e1);
						}
						catch (MessagingException e1) {
							// TODO Auto-generated catch block
							logger.error("ERROR :", e1);
						}*/
					}
				}
			}catch (Exception e) {
				logger.error("ERROR :", e);
				err=e.getLocalizedMessage();
			}
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}

	/* Canpri
	 * Warning upload dan transfer Spaj 
	 */
	public void schedulerWarningUploadTransferSpaj(String msh_name) {
		Date bdate 	= commonDao.selectSysdate();	
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String desc	= "OK";
		String err="";
		
		Map params = new HashMap();
		params.put("bdate", defaultDateFormat.format(commonDao.selectSysdateTruncated(-8)));
		params.put("edate", defaultDateFormat.format(commonDao.selectSysdateTruncated(0)));
		List data = query("selectWarningUploadTransferSpaj", params);
		
		if(data.size()>0){
			String subject = "";
			for(int i=0; i<data.size();i++){
				try{
					Map m = (Map) data.get(i);
					
					if(m.get("NO_INDEK")==null){
						subject = "Mohon segera lakukan scan upload dan transfer untuk spaj "+(String)m.get("REG_SPAJ");
					}else{
						subject = "Mohon segera lakukan scan upload untuk spaj "+(String)m.get("REG_SPAJ");
					}
					
					String link = "http://elions.sinarmasmsiglife.co.id/bas/spaj.htm?window=ket_upload_transfer&id="+((BigDecimal)m.get("LUS_ID")).intValue()+"&spaj="+(String)m.get("REG_SPAJ");
					String tkey = "";
			   		try {
			   			tkey = commonDao.encryptUrlKey("ket_upload_transfer", (String)m.get("REG_SPAJ"), App.ID, link);
			   		}catch (Exception e) {
						logger.error("ERROR", e);
					}
			   		link = link + "&tkey="+tkey;
					
					StringBuffer pesan = new StringBuffer();
					//Format isi email

					pesan.append("Mohon lakukan proses selanjutnya untuk :");
					pesan.append("\nReg Spaj\t : "+(String)m.get("REG_SPAJ"));
					pesan.append("\nProduk\t\t : "+(String)m.get("PRODUK"));
					pesan.append("\nPemegang\t : "+(String)m.get("PEMEGANG"));
					pesan.append("\nTertanggung\t : "+(String)m.get("TERTANGGUNG"));
					pesan.append("\nTanggal Input\t : "+(String)m.get("TGL_INPUT"));
					pesan.append("\n\nHarap isi keterangan untuk belum upload scan atau transfer di <a href='"+link+"'>Input Keterangan</a>");
					
					String from = props.getProperty("admin.ajsjava");
					String[] to = new String[]{(String)m.get("EMAIL")};
					//String[] cc = emailcc.split(";");
					//String[] bcc = emailbcc.split(";");

					try {
						//use email pool
						EmailPool.send("E-Lions", 1, 1, 0, 0, 
								null, 0, 0, commonDao.selectSysdate(), null, 
								true, from, 
								to, 
//								new String[]{"yudi@sinarmasmsiglife.co.id","iway@sinarmasmsiglife.co.id","yusy@sinarmasmsiglife.co.id","pratidina@sinarmasmsiglife.co.id",
//									"desy@sinarmasmsiglife.co.id","yune@sinarmasmsiglife.co.id","martino@sinarmasmsiglife.co.id"}, CC email dihilangkan (99065)
								null, 
								new String[]{"canpri@sinarmasmsiglife.co.id"}, 
								subject, 
								pesan.toString(), 
								null, null);
						
						//email.send(true, from, to, null, null,subject,pesan.toString(), null);
//						email.send(true, from, new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null,subject,pesan.toString(), null);
					}
					catch (MailException e1) {
						// TODO Auto-generated catch block
						//logger.error("ERROR :", e1);
						logger.error("ERROR :", e1);
						err=e1.getLocalizedMessage();
					}
				}catch (Exception e) {
					logger.error("ERROR :", e);
					err=e.getLocalizedMessage();
				}
			}
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Summary untuk ARCO 
	 */
	public void schedulerSummaryBuatArco(String msh_name) throws DataAccessException{
		Date bdate 	= new Date();
		String desc	= "OK";
		String err="";

		try {
			Date yesterday = commonDao.selectSysdateTruncated(-1);
			Date today = commonDao.selectSysdateTruncated(0);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			DateFormat year = new SimpleDateFormat("yyyy");
			String yearbefore = year.format(FormatDate.add(today, Calendar.YEAR, -1) );
			
			DateFormat monthyear = new SimpleDateFormat("mmyyyy");
			String month1 = "01"+ year.format(today);
			String month2 = "02"+ year.format(today);
			String month3 = "03"+ year.format(today);
			String month4 = "04"+ year.format(today);
			String month5 = "05"+ year.format(today);
			String month6 = "06"+ year.format(today);
			String month7 = "07"+ year.format(today);
			String month8 = "08"+ year.format(today);
			String month9 = "09"+ year.format(today);
			String month10 = "10"+ year.format(today);
			String month11 = "11"+ year.format(today);
			String month12 = "12"+ year.format(today);
			
			
			logger.info("DMTM ARCO SCHEDULER AT " + new Date());
			long start = System.currentTimeMillis();
			
			//Report Summary (Menu dapat diakses di Entry > UW > Akseptasi Khusus > Summary)
			
			String outputDir = props.getProperty("pdf.dir.report") + "\\summary_akseptasi_arco\\" + dateFormat.format(today) + "\\";
			
			Map<String, Comparable> params = new HashMap<String, Comparable>();
			params.put("tanggal", df.format(today));
			params.put("user", "SYSTEM");
			
			//passing parameter ke reportnya
			params.put("yearbefore", yearbefore);
			params.put("month1", month1);
			params.put("month2", month2);
			params.put("month3", month3);
			params.put("month4", month4);
			params.put("month5", month5);
			params.put("month6", month6);
			params.put("month7", month7);
			params.put("month8", month8);
			params.put("month9", month9);
			params.put("month10", month10);
			params.put("month11", month11);
			params.put("month12", month12);

			Map<String, List<Map>> distribusi 			= new HashMap<String, List<Map>>();
			Map<String, List<Map>> daftarReport 		= new HashMap<String, List<Map>>();
			Map<String, List<File>> daftarAttachment1 	= new HashMap<String, List<File>>();
			Map<String, List<File>> daftarAttachment2 	= new HashMap<String, List<File>>();
			daftarAttachment1.put("DMTM - SMILE MEDICAL ARCO", 		new ArrayList<File>());
			daftarAttachment2.put("DMTM - SMILE MEDICAL ARCO", 		new ArrayList<File>());
			
			String b;
			
			//Daftar Semua Report
			daftarReport.put("Akseptasi_Khusus", uwDao.selectDaftarAkseptasiNyangkutSiArco(5, 183,false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 		//1. akseptasi khusus
			daftarReport.put("Further_Requirements", uwDao.selectDaftarAkseptasiNyangkutSiArco(3,183, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 	//2. further requirements -> lssa_id in (3,4,8)
			
			//Looping Utama dari daftar semua report
			for(String r : daftarReport.keySet()) {
				
				params.put("banyakMaunya", r);
				
				//Parameter Tambahan
				if(r.equals("Expired")) {
					params.put("judul", "Follow-Up Polis Expired");
				}
				else if(r.equals("Akseptasi_Khusus") || r.equals("Akseptasi_Khusus_Sms")) {
					params.put("judul", "Follow-Up Akseptasi Khusus");
				}else if(r.equals("Further_Requirements") || r.equals("Further_Requirements_Sms")) {
					params.put("judul", "Follow-Up Further Requirement");
				}else if(r.equals("Postpone")) {
					params.put("judul", "Follow-Up Surat Konfirmasi Case Postpone");
				}else if(r.equals("Decline")) {
					params.put("judul", "Follow-Up Surat Konfirmasi Case Decline");
				}
				
				if(r.equals("Akseptasi_Khusus") || r.equals("Akseptasi_Khusus_Sms")) {
					params.put("note",
						"Data yang masuk disini adalah data:\n" +
						"* Polis diaksep dengan kondisi khusus (polis yang sudah diaksep tetapi masih diperlukan data tambahan)");
//						"Note:\n" +
//						"Data yang masuk disini adalah data :\n" +
//						"* Polis yang sudah dicetak, tetapi masih ada Further requirement. Polis diaksep dengan kondisi khusus.\n" + 
//						"Polis yang di 'Aksep dengan kondisi khusus' adalah polis yang diaksep ( dapat sudah langsung dicetak polis atau masih pending cetak polis )\n" +
//						"namun sebenarnya masih diperlukan data tambahan.");
				}else if(r.equals("Further_Requirements") || r.equals("Further_Requirements_Sms")) {
					params.put("note",
						"Note:\n" +
						"Data yang masuk disini adalah data :\n" +
						"* Polis yang belum diaksep, masih perlu data tambahan ( Further requirement )");
				}else {
					params.put("note", "");
				}
				
				List<Map> report = daftarReport.get(r);
				
			}
			
			
			//Daftar Semua Report
			daftarReport = new HashMap<String, List<Map>>();
			daftarReport.put("Akseptasi_Khusus", uwDao.selectDaftarAkseptasiNyangkutSiArco(5,183, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 		//1. akseptasi khusus
			daftarReport.put("Further_Requirements", uwDao.selectDaftarAkseptasiNyangkutSiArco(3,183, false, yearbefore, month1, month2, month3, month4, month5, month6, month7, month8, month9, month10, month11, month12)); 	//2. further requirements -> lssa_id in (3,4,8)
			
			for(String r : daftarReport.keySet()) {
				
				params.put("banyakMaunya", r);
				
				List<Map> report = daftarReport.get(r); //akseptasi, further, postpone, decline
				Map<String, List<Map>> daftarEmail = new HashMap<String, List<Map>>(); 
				String tmp = "";
				List<Map> reportTmp = new ArrayList<Map>();
				
				//pecah2 dari masing2 report ke masing2 email
				for(Map row : report) {
					String lar_email = (String) row.get("LAR_EMAIL");
					String lca_id = (String) row.get("LCA_ID");
					
					if(lar_email == null)lar_email = "";
					if(lca_id == null)lca_id = "";
					
					BigDecimal templsbs = (BigDecimal) row.get("LSBS_ID");
					int lsbs_id = 0;
					if(templsbs != null) lsbs_id = templsbs.intValue();
					
					BigDecimal templsdbs = (BigDecimal) row.get("LSDBS_NUMBER");
					int lsdbs_number = 0;
					if(templsdbs != null) lsdbs_number = templsdbs.intValue();
					if(!tmp.equals(lar_email)) {
						if(!tmp.equals("")) daftarEmail.put(tmp, reportTmp);
						reportTmp = new ArrayList<Map>();
						tmp = lar_email;
					}
					reportTmp.add(row);
				}
				if(!reportTmp.isEmpty()) {
					daftarEmail.put(tmp, reportTmp);
				}

				//untuk setiap email, generate report, lalu kirim email
				for(String e : daftarEmail.keySet()) {
//					String outputFilename = r + ".pdf";
					String outputFilename = r + ".xls";
					
					//Parameter Tambahan
					if(!r.equals("Akseptasi_Khusus") && !r.equals("Further_Requirements") && !r.equals("Akseptasi_Khusus_Sms") && !r.equals("Further_Requirements_Sms")) {
						params.put("judul", "Follow-Up Surat Konfirmasi Case " + r);
					}else if(r.equals("Akseptasi_Khusus") || r.equals("Akseptasi_Khusus_Sms")) {
						params.put("judul", "Follow-Up Akseptasi Khusus");
					}else if(r.equals("Further_Requirements") || r.equals("Further_Requirements_Sms")) {
						params.put("judul", "Follow-Up Further Requirement");
					}else {
						params.put("judul", "Follow-Up " + r);
					}
					
					if(r.equals("Akseptasi_Khusus") || r.equals("Akseptasi_Khusus_Sms")) {
						params.put("note",
							"Data yang masuk disini adalah data:\n" +
							"* Polis diaksep dengan kondisi khusus (polis yang sudah diaksep tetapi masih diperlukan data tambahan)");
//							"Note:\n" +
//							"Data yang masuk disini adalah data :\n" +
//							"* Polis yang sudah dicetak, tetapi masih ada Further requirement. Polis diaksep dengan kondisi khusus.\n" + 
//							"Polis yang di 'Aksep dengan kondisi khusus' adalah polis yang diaksep ( dapat sudah langsung dicetak polis atau masih pending cetak polis )\n" +
//							"namun sebenarnya masih diperlukan data tambahan.");
					}else if(r.equals("Further_Requirements") || r.equals("Further_Requirements_Sms")) {
						params.put("note",
							"Note:\n" +
							"Data yang masuk disini adalah data :\n" +
							"* Polis yang belum diaksep, masih perlu data tambahan ( Further requirement )");
					}else {
						params.put("note", "");
					}
					
					JasperUtils.exportReportToXls(props.getProperty("report.uw.summary." + r) + ".jasper", 
							outputDir + e + "\\", outputFilename, params, daftarEmail.get(e), props.getProperty("report.uw.summary.sub.total")+ ".jasper");
					List<File> attachments = new ArrayList<File>();
					attachments.add(new File(outputDir + "\\" + e + "\\" + outputFilename));


					if(!attachments.isEmpty()) {
				/*	email.send(true,"ajsjava@sinarmasmsiglife.co.id", 
							new String[] {"ryan@sinarmasmsiglife.co.id"}, new String[] {"ningrum@sinarmasmsiglife.co.id","Saphry@sinarmasmsiglife.co.id","Saphry@sinarmasmsiglife.co.id"},   
							null,"[SMiLe E-Lions] Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQ / DECLINED / POSTPONED DMTM - ARCO " + df.format(yesterday), "Berikut adalah Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQ / DECLINED / POSTPONED s/d " + df.format(yesterday) + 
							"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.",
							attachments);*/
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
								null, 0, 0, today, null, 
								true, "ajsjava@sinarmasmsiglife.co.id", 
								new String[] {"ningrum@sinarmasmsiglife.co.id"},new String[] {"ryan@sinarmasmsiglife.co.id"},null,//e.split(";"), null, null,//new String[] {props.getProperty("admin.yusuf")},   
								"Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQ / DECLINED / POSTPONED DMTM - ARCO " + df.format(yesterday), 
								"Berikut adalah Summary Follow-Up AKSEPTASI KHUSUS / FURTHER REQ / DECLINED / POSTPONED DMTM - ARCO  s/d " + df.format(yesterday) + 
								"<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", attachments, null);
					}
					

				}
				
			}
			
		} catch (Exception e) {
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
			desc = "ERROR";
			StringBuffer stackTrace = new StringBuffer();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			stackTrace.append(sw);
			try {
				email.send(
						false,
						props.getProperty("admin.ajsjava"),
//						"deddy@sinarmasmsiglife.co.id", 
						new String[] {"deddy@sinarmasmsiglife.co.id"}, 
						null, null, 
						"ERROR pada Scheduler Akseptasi E-Lions " , 
						"Pesan Error : "+ stackTrace.toString(), 
						null);
			} catch (MailException e1) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e1);
			} catch (MessagingException e1) {
				// TODO Auto-generated catch block
				logger.error("ERROR :", e1);
			}
		}		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}		
	}
	
	/**
	 * Fungsi scheduller untuk mengirim email otomatis dari UW ke Finance perihal Refund Premi
	 * @author MANTA
	 * @throws DataAccessException
	 */
	public void schedulerRefundPremiAuto(String msh_name) {
		Date bdate = commonDao.selectSysdate();
		Date yesterday = commonDao.selectSysdateTruncated(-1);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		String desc	= "OK";
		String err = "";
		
		ArrayList<HashMap> data1 = new ArrayList<HashMap>();
		ArrayList<HashMap> data2 = new ArrayList<HashMap>();
		ArrayList<HashMap> data3 = new ArrayList<HashMap>();
		ArrayList<HashMap> data4 = new ArrayList<HashMap>();
		ArrayList<HashMap> data5 = new ArrayList<HashMap>();
		ArrayList<HashMap> data6 = new ArrayList<HashMap>();
		ArrayList<HashMap> data7 = new ArrayList<HashMap>();
		ArrayList<HashMap> data8 = new ArrayList<HashMap>();
		ArrayList<HashMap> data9 = new ArrayList<HashMap>();
		ArrayList<HashMap> data10 = new ArrayList<HashMap>();
		ArrayList<HashMap> data11 = new ArrayList<HashMap>();
		ArrayList<HashMap> data12 = new ArrayList<HashMap>();
		
		ArrayList<HashMap> data13 = new ArrayList<HashMap>();
		ArrayList<HashMap> data14 = new ArrayList<HashMap>();
		ArrayList<HashMap> data15 = new ArrayList<HashMap>();
		ArrayList<HashMap> data16 = new ArrayList<HashMap>();
		ArrayList<HashMap> data17 = new ArrayList<HashMap>();
		ArrayList<HashMap> data18 = new ArrayList<HashMap>();
		ArrayList<HashMap> data19 = new ArrayList<HashMap>();
		ArrayList<HashMap> data20 = new ArrayList<HashMap>();
		ArrayList<HashMap> data21 = new ArrayList<HashMap>();
		ArrayList<HashMap> data22 = new ArrayList<HashMap>();
		ArrayList<HashMap> data23 = new ArrayList<HashMap>();
		ArrayList<HashMap> data24 = new ArrayList<HashMap>();
		
		// pega only
		ArrayList<HashMap> data25 = new ArrayList<HashMap>();
		ArrayList<HashMap> data26 = new ArrayList<HashMap>();
		
		
		data1 = Common.serializableList(refundDao.selectSumRefAksep("01"));
		if(data1.size()>0){
			data2 = Common.serializableList(refundDao.selectDetRefAksep("01"));
			data25 = Common.serializableList(refundDao.selectDetRefPegaAksep("01")); //pega
			data3 = Common.serializableList(refundDao.selectUploadRefund("0", "01")); //Untuk Rek Bank Lain-lain
			data4 = Common.serializableList(refundDao.selectUploadRefund("1", "01")); //Untuk Rek Bank Sinarmas
			data5 = Common.serializableList(refundDao.selectUploadRefund("2", "01")); //Untuk Rek Bank BCA
			data6 = Common.serializableList(refundDao.selectUploadRefund("3", "01")); //Untuk Rek Bank BII
		}
		
		data7 = Common.serializableList(refundDao.selectSumRefAksepNonSpaj("01"));//Untuk Summary Refund Non SPAJ
		if(data7.size()>0){
			data8 = Common.serializableList(refundDao.selectDetRefAksepNonSpaj("01"));//Untuk Detail Refund Non SPAJ
			data9 = Common.serializableList(refundDao.selectUploadRefundNonSpaj("0", "01"));//Untuk Upload Refund Non SPAJ
			data10 = Common.serializableList(refundDao.selectUploadRefundNonSpaj("1", "01"));//Untuk Upload Refund Non SPAJ Sinarmas
			data11 = Common.serializableList(refundDao.selectUploadRefundNonSpaj("2", "01"));//Untuk Upload Refund Non SPAJ BCA
			data12 = Common.serializableList(refundDao.selectUploadRefundNonSpaj("3", "01"));//Untuk Upload Refund Non SPAJ BII
		}
		
		data13 = Common.serializableList(refundDao.selectSumRefAksep("02"));
		if(data13.size()>0){
			data14 = Common.serializableList(refundDao.selectDetRefAksep("02"));
			data26 = Common.serializableList(refundDao.selectDetRefPegaAksep("02")); // pega
			data15 = Common.serializableList(refundDao.selectUploadRefund("0", "02")); //Untuk Rek Bank Lain-lain
			data16 = Common.serializableList(refundDao.selectUploadRefund("1", "02")); //Untuk Rek Bank Sinarmas
			data17 = Common.serializableList(refundDao.selectUploadRefund("2", "02")); //Untuk Rek Bank BCA
			data18 = Common.serializableList(refundDao.selectUploadRefund("3", "02")); //Untuk Rek Bank BII
		}
		
		data19 = Common.serializableList(refundDao.selectSumRefAksepNonSpaj("02"));//Untuk Summary Refund Non SPAJ
		if(data19.size()>0){
			data20 = Common.serializableList(refundDao.selectDetRefAksepNonSpaj("02"));//Untuk Detail Refund Non SPAJ
			data21 = Common.serializableList(refundDao.selectUploadRefundNonSpaj("0", "02"));//Untuk Upload Refund Non SPAJ
			data22 = Common.serializableList(refundDao.selectUploadRefundNonSpaj("1", "02"));//Untuk Upload Refund Non SPAJ Sinarmas
			data23 = Common.serializableList(refundDao.selectUploadRefundNonSpaj("2", "02"));//Untuk Upload Refund Non SPAJ BCA
			data24 = Common.serializableList(refundDao.selectUploadRefundNonSpaj("3", "02"));//Untuk Upload Refund Non SPAJ BII
		}
		
		Map params = new HashMap();
		params.put("tgl_report", df.format(yesterday));

		try{
			if(data1.size()>0 || data7.size()>0 || data13.size()>0 || data19.size()>0){
				String subject = "[INFO] PERMINTAAN REFUND PREMI OLEH DEPT. NEW BUSSINESS DAN DEPT. COLLECTION";
				String outputDir = props.getProperty("pdf.dir.report") + "\\refund\\"+df2.format(bdate)+"\\";
				String outputFilename1 = "SumRefundPremi(Rp).pdf";
				String outputFilename2 = "DetRefundPremi(Rp).xls";
				String outputFilename3 = "UplRefundPremi(Rp).xls";
				String outputFilename4 = "UplRefundPremiSM(Rp).xls";
				String outputFilename5 = "UplRefundPremiBCA(Rp).xls";
				String outputFilename6 = "UplRefundPremiBII(Rp).xls";
				String outputFilename7 = "SumRefundPremiNonSpaj(Rp).pdf";
				String outputFilename8 = "DetRefundPremiNonSpaj(Rp).xls";
				String outputFilename9 = "UplRefundPremiNonSpaj(Rp).xls";
				String outputFilename10 = "UplRefundPremiNonSpajSM(Rp).xls";
				String outputFilename11 = "UplRefundPremiNonSpajBCA(Rp).xls";
				String outputFilename12 = "UplRefundPremiNonSpajBII(Rp).xls";
				
				String outputFilename13 = "SumRefundPremi(USD).pdf";
				String outputFilename14 = "DetRefundPremi(USD).xls";
				String outputFilename15 = "UplRefundPremi(USD).xls";
				String outputFilename16 = "UplRefundPremiSM(USD).xls";
				String outputFilename17 = "UplRefundPremiBCA(USD).xls";
				String outputFilename18 = "UplRefundPremiBII(USD).xls";
				String outputFilename19 = "SumRefundPremiNonSpaj(USD).pdf";
				String outputFilename20 = "DetRefundPremiNonSpaj(USD).xls";
				String outputFilename21 = "UplRefundPremiNonSpaj(USD).xls";
				String outputFilename22 = "UplRefundPremiNonSpajSM(USD).xls";
				String outputFilename23 = "UplRefundPremiNonSpajBCA(USD).xls";
				String outputFilename24 = "UplRefundPremiNonSpajBII(USD).xls";
				
				// pega
				String outputFilename25 = "DetRefundPremiPega(Rp).xls";
				String outputFilename26 = "DetRefundPremiPega(USD).xls";
				
				if(data1.size()>0){
					params.put("jenis_report", "0");
					JasperUtils.exportReportToPdf(
							props.getProperty("report.refund.sum_refund")+".jasper", 
							outputDir, 
							outputFilename1, 
							params, 
							data1, 
							PdfWriter.AllowPrinting, null, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.det_refund")+".jasper", 
							outputDir,
							outputFilename2,
							params,
							data2, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.det_refund")+".jasper", 
							outputDir,
							outputFilename25,
							params,
							data25, null); //pega
					
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename3,
							params,
							data3, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund_bsm")+".jasper", 
							outputDir,
							outputFilename4,
							params,
							data4, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename5,
							params,
							data5, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename6,
							params,
							data6, null);
				}
				
				if(data7.size()>0){
					params.put("jenis_report", "1");
					JasperUtils.exportReportToPdf(
							props.getProperty("report.refund.sum_refund")+".jasper", 
							outputDir, 
							outputFilename7, 
							params, 
							data7, 
							PdfWriter.AllowPrinting, null, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.det_refund")+".jasper", 
							outputDir,
							outputFilename8,
							params,
							data8, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename9,
							params,
							data9, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund_bsm")+".jasper", 
							outputDir,
							outputFilename10,
							params,
							data10, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename11,
							params,
							data11, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename12,
							params,
							data12, null);
				}
				
				if(data13.size()>0){
					params.put("jenis_report", "0");
					JasperUtils.exportReportToPdf(
							props.getProperty("report.refund.sum_refund")+".jasper", 
							outputDir, 
							outputFilename13, 
							params, 
							data13, 
							PdfWriter.AllowPrinting, null, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.det_refund")+".jasper", 
							outputDir,
							outputFilename14,
							params,
							data14, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.det_refund")+".jasper", 
							outputDir,
							outputFilename26,
							params,
							data26, null); //pega
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename15,
							params,
							data15, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund_bsm")+".jasper", 
							outputDir,
							outputFilename16,
							params,
							data16, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename17,
							params,
							data17, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename18,
							params,
							data18, null);
				}
				
				if(data19.size()>0){
					params.put("jenis_report", "1");
					JasperUtils.exportReportToPdf(
							props.getProperty("report.refund.sum_refund")+".jasper", 
							outputDir, 
							outputFilename19, 
							params, 
							data19, 
							PdfWriter.AllowPrinting, null, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.det_refund")+".jasper", 
							outputDir,
							outputFilename20,
							params,
							data20, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename21,
							params,
							data21, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund_bsm")+".jasper", 
							outputDir,
							outputFilename22,
							params,
							data22, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename23,
							params,
							data23, null);
					
					JasperUtils.exportReportToXls(
							props.getProperty("report.refund.upload_refund")+".jasper", 
							outputDir,
							outputFilename24,
							params,
							data24, null);
				}
				
				List<File> attachments = new ArrayList<File>();
				if(data1.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename1));
				if(data2.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename2));
				if(data3.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename3));
				if(data4.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename4));
				if(data5.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename5));
				if(data6.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename6));
				if(data7.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename7));
				if(data8.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename8));
				if(data9.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename9));
				if(data10.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename10));
				if(data11.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename11));
				if(data12.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename12));
				if(data13.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename13));
				if(data14.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename14));
				if(data15.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename15));
				if(data16.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename16));
				if(data17.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename17));
				if(data18.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename18));
				if(data19.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename19));
				if(data20.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename20));
				if(data21.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename21));
				if(data22.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename22));
				if(data23.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename23));
				if(data24.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename24));
				if(data25.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename25));
				if(data26.size() > 0) attachments.add(new File(outputDir + "\\" + outputFilename26));
				
				//Format isi email
				StringBuffer pesan = new StringBuffer();
				
				pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
				pesan.append("<body>Telah dilakukan permintaan refund premi pada tanggal <b>" + df.format(yesterday) + "</b> untuk Polis-polis yang tertera pada lampiran.<br>");
				pesan.append("Harap segera dilakukan proses pembayaran terhadap Polis-polis tersebut.");
				
				HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerRefundPremiAuto", "SCHEDULER_REFUND_PREMI");
				String from = props.getProperty("admin.ajsjava");
				String emailto = mapEmail.get("NAME")!=null? mapEmail.get("NAME").toString():null;
				String emailcc = mapEmail.get("NAME2")!=null? mapEmail.get("NAME2").toString():null;
				String emailbcc = mapEmail.get("NAME3")!=null? mapEmail.get("NAME3").toString():null;
				
				String[] to = emailto.split(";");
				String[] cc = emailcc.split(";");
				String[] bcc = emailbcc.split(";");
					
				try {
					//use email pool
					EmailPool.send("E-Lions", 1, 1, 0, 0, 
							null, 0, 0, commonDao.selectSysdate(), null, 
							true,
							from, 
							to, 
							cc, 
							bcc, 
							subject, 
							pesan.toString(), 
							attachments, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
					err = e1.getLocalizedMessage();
					desc = "ERROR";
				}
			}else{
				desc = "DATA TIDAK ADA";
			}
		}catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR";
			err = e.getLocalizedMessage();
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc, err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	public void schedulerStatusAAJICalonKaryawan(String msh_name) throws DataAccessException{
		Date bdate 	= commonDao.selectSysdate();
		String desc = "OK";
		String err="";
		List data = rekruitmentDao.selectProsesAAJICalonKaryawan();
		
		if(data.size()>0){
			String subject = "[REMINDER] CEK STATUS AAJI CALON KARYAWAN";
			try{
				//Format isi email
				StringBuffer pesan = new StringBuffer();
				
				pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
				pesan.append("<body>Mohon untuk dilakukan pengecekan status AAJI calon karyawan berikut ini <br><br>");
				pesan.append("<table>");
				pesan.append("<tr>");
				pesan.append("<td>No. KTP</td>");
				pesan.append("<td>Nama</td>");
				pesan.append("</tr>");
				for(int i=0; i<data.size();i++){
					Map m = (HashMap) data.get(i);
					
					String ktp = (String) m.get("KTP");
					String nama = (String) m.get("NAMA");

					pesan.append("<tr>");
					pesan.append("<td>"+ktp+"</td>");
					pesan.append("<td>"+nama+"</td>");
					pesan.append("</tr>");
				}
				pesan.append("</table>");
				pesan.append("<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.");
				pesan.append("</body>");
				
				String from = props.getProperty("admin.ajsjava");
				String to[] = props.getProperty("rekruitment.pic.aaji").split(";");
				String cc[] = props.getProperty("rekruitment.aaji.cc.fcd").split(";");
				
				try {
					//use email pool
					String me_id = sequence.sequenceMeIdEmail();
					EmailPool.send(me_id,"E-Lions", 1, 1, 0, 0, 
							null, 0, 0, commonDao.selectSysdate(), null, 
							true,
							from, // from
							new String[]{"alfian_h@sinarmasmsiglife.co.id","csbranchfcd@sinarmasmsiglife.co.id"}, // to 
							null, // cc
							null, 
							subject, 
							pesan.toString(), 
							null,6);
					
				}catch (MailException e1) {
					logger.error("ERROR :", e1);
					err=e1.getLocalizedMessage();
				}
			}catch(Exception e){
				logger.error("ERROR :", e);
				err=e.getLocalizedMessage();
			}
		}
		
		try{
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		}catch(UnknownHostException e){
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Summary surat history untuk Retur KPL yang diinput untuk notifikasi ke cabang.
	 * @author canpri
	 * @since 27/03/2014
	 */
	public void schedulerReturKpl(String msh_name) throws DataAccessException{
		Date bdate 	= commonDao.selectSysdate();	
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String desc	= "OK";
		String err="";
		
		List l_cabang = basDao.selectReturKpl(1, null);
		
		for(int i=0;i<l_cabang.size();i++){
			Map m = new HashMap();
			m = (Map) l_cabang.get(i);
			String lca_id = (String)m.get("LCA_ID");
			
			List data = basDao.selectReturKpl(2, lca_id);
			
			try{
				List l_email = uwDao.selectEmailCabang(lca_id);
	    		
				for(int j=0; j<l_email.size();j++){
					
					String email_cab = (String) l_email.get(j);
					
					StringBuffer pesan = new StringBuffer();
					//Format isi email
		
					pesan.append("Berikut adalah List Retur KPL tgl "+defaultDateFormat.format(commonDao.selectSysdateTruncated(-1)));
					pesan.append("\n\n");
					pesan.append("<table width='100%' border='1' cellpadding='0' cellspacing='0' style='font-size:10px'>");
					pesan.append("<tr><td align='center' bgcolor='#FFFF00'><strong>No</strong></td>");
					pesan.append("<td align='center' bgcolor='#FFFF00'><strong>Cabang </strong></td>");
					pesan.append("<td align='center' bgcolor='#FFFF00'><strong>No Polis</strong></td>");
					pesan.append("<td align='center' bgcolor='#FFFF00'><strong>Pemegang</strong></td>");
					pesan.append("<td align='center' bgcolor='#FFFF00'><strong>Alamat</strong></td>");
					pesan.append("<td align='center' bgcolor='#FFFF00'><strong>No. Telp</strong></td>");
					pesan.append("<td align='center' bgcolor='#FFFF00'><strong>Keterangan Retur</strong></td></tr>");
					
					for(int z=0;z<data.size();z++){
						Map n = new HashMap();
						n = (Map) data.get(z);
						Integer no = z+1;
						
						pesan.append("<tr><td>"+no+"</td>");
						pesan.append("<td>"+(String)n.get("CABANG")+"</td>");
						pesan.append("<td>"+(String)n.get("POLIS")+"</td>");
						pesan.append("<td>"+(String)n.get("PEMEGANG")+"</td>");
						pesan.append("<td>"+(String)n.get("ALAMAT")+"</td>");
						pesan.append("<td>"+(String)n.get("TELP")+"</td>");
						pesan.append("<td>"+(String)n.get("KETERANGAN")+"</td></tr>");
					}
					pesan.append("</table>");
					
					String from = props.getProperty("admin.ajsjava");
					String emailto = email_cab;
					String emailcc = null;
					String emailbcc = null;
					
					String[] to = emailto.split(";");
					//String[] cc = emailcc.split(";");
					//String[] bcc = emailbcc.split(";");
				
					
					if(email_cab!=null){
						try {
							//use email pool
							EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, bdate, null, true, 
									from, to, null, new String[]{"canpri@sinarmasmsiglife.co.id"}, "List Retur KPL tgl "+defaultDateFormat.format(commonDao.selectSysdateTruncated(-1)), pesan.toString(), null, null);
							
						}
						catch (MailException e1) {
							//logger.error("ERROR :", e1);
							err=e1.getLocalizedMessage();
						}
					}
				}
			}catch (Exception e) {
				err=e.getLocalizedMessage();
				logger.error("ERROR :", e);
			}
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Email notifikasi berakhirnya tanggal sewa dan surat domisili kantor cabang
	 * @author canpri
	 * @since 29/04/2014
	 */
	public void schedulerSuratDomisili(String msh_name) throws DataAccessException{
		Date bdate 	= commonDao.selectSysdate();	
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String desc	= "OK";
		String err="";
		
		try{
			//email notifikasi 6 bulan sebelum masa sewa gedung berakhir
			Map param = new HashMap();
			param.put("jenis", 1);
			List notif_6_bln = query("selectEmailSuratDomisili",param);
			
			for(int i=0; i<notif_6_bln.size();i++){
				Map map = (Map) notif_6_bln.get(i);
				String email_cab = (String) map.get("LAR_EMAIL");
				Date end_date = (Date) map.get("LAR_END_DATE_SEWA");
				
				StringBuffer pesan = new StringBuffer();
				
				//Format isi email
				pesan.append("Dear All,");
				pesan.append("\n\nSewa gedung akan berakhir dalam waktu 6 bulan, pada tanggal "+defaultDateFormat.format(end_date)+". Mohon agar dapat diinformasikan kepada leader.");
				pesan.append("\n\nTerima kasih.");
				
				String from = props.getProperty("admin.ajsjava");
				String emailto = email_cab;
				String emailcc = "bas@sinarmasmsiglife.co.id;rismawati@sinarmasmsiglife.co.id";
				String emailbcc = null;
				
				String[] to = emailto.split(";");
				String[] cc = emailcc.split(";");
				//String[] bcc = emailbcc.split(";");
			
				if(email_cab!=null){
					try {
						//use email pool
						EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, bdate, null, true, 
								from, to, cc, null, "Masa berakhir sewa gedung", pesan.toString(), null, null);
						
					}
					catch (MailException e1) {
						err=e1.getLocalizedMessage();
					}
				}
			}
			
			//email notifikasi 3 bulan sebelum masa sewa gedung berakhir
			Map param2 = new HashMap();
			param2.put("jenis", 2);
			List notif_3_bln = query("selectEmailSuratDomisili",param2);
			
			for(int i=0; i<notif_3_bln.size();i++){
				Map map = (Map) notif_3_bln.get(i);
				String email_cab = (String) map.get("LAR_EMAIL");
				Date end_date = (Date) map.get("LAR_END_DATE_SEWA");
				
				StringBuffer pesan = new StringBuffer();
				
				//Format isi email
				pesan.append("Dear All,");
				pesan.append("\n\nSewa gedung akan berakhir dalam waktu 3 bulan, pada tanggal "+defaultDateFormat.format(end_date)+". Mohon agar dapat diinformasikan kepada leader.");
				pesan.append("\n\nTerima kasih.");
				
				String from = props.getProperty("admin.ajsjava");
				String emailto = email_cab;
				String emailcc = "bas@sinarmasmsiglife.co.id;rismawati@sinarmasmsiglife.co.id";
				String emailbcc = null;
				
				String[] to = emailto.split(";");
				String[] cc = emailcc.split(";");
				//String[] bcc = emailbcc.split(";");
			
				if(email_cab!=null){
					try {
						//use email pool
						EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, bdate, null, true, 
								from, to, cc, null, "Masa berakhir sewa gedung", pesan.toString(), null, null);
						
					}
					catch (MailException e1) {
						err=e1.getLocalizedMessage();
					}
				}
			}
			
			//email notifikasi 1 bulan sebelum masa sewa gedung berakhir dan akan terus muncul sampai CS branch mengupdate pada sistem
			Map param3 = new HashMap();
			param3.put("jenis", 3);
			List notif_1_bln = query("selectEmailSuratDomisili",param3);
			
			for(int i=0; i<notif_1_bln.size();i++){
				Map map = (Map) notif_1_bln.get(i);
				String email_cab = (String) map.get("LAR_EMAIL");
				Date end_date = (Date) map.get("LAR_END_DATE_SEWA");
				
				StringBuffer pesan = new StringBuffer();
				
				//Format isi email
				pesan.append("Dear All,");
				pesan.append("\n\nSewa gedung akan berakhir dalam waktu 1 bulan, pada tanggal "+defaultDateFormat.format(end_date)+". Mohon lakukan update sebelum masa tanggal berakhir.");
				pesan.append("\n\nTerima kasih.");
				
				String from = props.getProperty("admin.ajsjava");
				String emailto = email_cab;
				String emailcc = "bas@sinarmasmsiglife.co.id;rismawati@sinarmasmsiglife.co.id";
				String emailbcc = null;
				
				String[] to = emailto.split(";");
				String[] cc = emailcc.split(";");
				//String[] bcc = emailbcc.split(";");
			
				if(email_cab!=null){
					try {
						//use email pool
						EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, bdate, null, true, 
								from, to, cc, null, "Masa berakhir sewa gedung", pesan.toString(), null, null);
						
					}
					catch (MailException e1) {
						err=e1.getLocalizedMessage();
					}
				}
			}
			
			//email notifikasi 2 bulan sebelum tanggal berakhir surat domisili	
			Map param4 = new HashMap();
			param4.put("jenis", 4);
			List notif_2_bln = query("selectEmailSuratDomisili",param4);
			
			for(int i=0; i<notif_2_bln.size();i++){
				Map map = (Map) notif_2_bln.get(i);
				String email_cab = (String) map.get("LAR_EMAIL");
				Date end_date = (Date) map.get("LAR_END_DATE_SEWA");
				
				StringBuffer pesan = new StringBuffer();
				
				//Format isi email
				pesan.append("Dear All,");
				pesan.append("\n\nSurat domisili akan berakhir pada tanggal "+defaultDateFormat.format(end_date)+". Mohon agar dapat lakukan update dan upload berkas yang diperlukan.");
				pesan.append("\n\nTerima kasih.");
				
				String from = props.getProperty("admin.ajsjava");
				String emailto = email_cab;
				String emailcc = "bas@sinarmasmsiglife.co.id;rismawati@sinarmasmsiglife.co.id";
				String emailbcc = null;
				
				String[] to = emailto.split(";");
				String[] cc = emailcc.split(";");
				//String[] bcc = emailbcc.split(";");
			
				if(email_cab!=null){
					try {
						//use email pool
						EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, bdate, null, true, 
								from, to, cc, null, "Masa berakhir surat domisili", pesan.toString(), null, null);
						
					}
					catch (MailException e1) {
						//logger.error("ERROR :", e1);
						err=e1.getLocalizedMessage();
					}
				}
			
			}
			
		}catch (Exception e) {
			err=e.getLocalizedMessage();
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc,err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Fungsi scheduller untuk mengirim email otomatis
	 * @author MANTA
	 * @throws DataAccessException
	 */
	public void schedulerFollowUpFR(String msh_name) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate 	= commonDao.selectSysdate();
		String desc	= "DATA TIDAK ADA";
		String err = "";
		ArrayList<HashMap> data = new ArrayList<HashMap>();
		ArrayList<HashMap> data2 = new ArrayList<HashMap>();
		HashMap params = new HashMap();
		params.put("tgl_report", df.format(bdate));
		
		try{
			
			//Perulangan untuk filter tanggal 
			for(int i=0; i<3; i++){
				data = Common.serializableList(uwDao.selectDataFollowUpFR(i+1, null));
				
				if(data!=null && data.size()>0){
					desc = "ERROR";
					
					String[] email_leader = new String[data.size()];
					for(int j=0; j<data.size(); j++){
						email_leader[j] = data.get(j).get("EMAIL_LEADER").toString();
					}
						
					//Untuk mendapatkan email
					String[] distArray = new String[data.size()];
					Integer arr = 0;
					distArray[arr] = email_leader[0];
					Boolean duplicate = false;
					Boolean duplicate2 = false;
					for(int j=0;j<data.size();j++){
						String temp = email_leader[j];
						//Looping untuk membandingkan array yg diambil dengan array pada source
						for(int k=0;k<data.size();k++){
							duplicate = false; duplicate2 = false;
							if(j != k && temp == email_leader[k]){
								duplicate = true;
							}
							if(!duplicate){
								//Looping untuk membandingkan array yg diambil dengan array yg telah disimpan
								for(int l=0;l<arr+1;l++){
									if(distArray[l].equals(email_leader[k])){
										duplicate2 = true;
									}
								}
								if(!duplicate2){
									arr = arr + 1;
									distArray[arr] = email_leader[k];
								}
							}
						}
					}
					
					for(int j=0;j<arr+1;j++){
						String leader = distArray[j];
						data2 = Common.serializableList(uwDao.selectDataFollowUpFR(i+1, leader));
						
						//Format isi email
						StringBuffer pesan = new StringBuffer();
						String subject = "";
						Integer subno = i + 1;
						
						if(subno == 3){
							subject = "WARNING " + subno + "! Follow Up SPAJ yang akan kadaluarsa dan proses refund";
							pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
							pesan.append("<body>Terlampir data-data SPAJ yang batas waktu untuk memenuhi requirement-nya sudah habis. SPAJ tersebut akan kami bekukan dan saat ini dalam proses refund ke rekening yang tercantum dalam SPAJ.<br><br>");
						}else{
							subject = "WARNING " + subno + "! Follow Up Further Requirements";
							pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
							pesan.append("<body>Mohon bantuannya untuk melakukan follow up ke masing-masing nasabah yang terdaftar pada attachment berikut ini, supaya Polis dapat segera diterbitkan.<br><br>");
						}
						
						//Table
						pesan.append("<table cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1000px'>");
						pesan.append("<tr bgcolor='#A8B8EE'><th>No</th>");
						pesan.append("<th>Cabang Penutup</th>");
						pesan.append("<th>Nama Penutup/AO/BAC</th>");
						pesan.append("<th>Cabang Refferal/Region</th>");
						pesan.append("<th>Nama Refferal/Agen Penutup</th>");
						pesan.append("<th>Nama PP</th></th>");
						pesan.append("<th>No. SPAJ</th>");
						pesan.append("<th>Nama Produk</th>");
						pesan.append("<th>Premi</th>");
						pesan.append("<th>Tgl. Further</th>");
						pesan.append("<th>Date Line</th>");
						pesan.append("<th>Keterangan</th></tr>");
						
						for(int k=0;k<data2.size();k++){
							HashMap x = data2.get(k);
							
							Integer no = k + 1;
							
							pesan.append("<td nowrap>"+ no + "</td>");
							pesan.append("<td nowrap>"+ x.get("CABANG_PENUTUP") + "</td>");
							pesan.append("<td nowrap>"+ x.get("NAMA_PENUTUP") + "</td>");
							pesan.append("<td nowrap>"+ x.get("CABANG_REFF") + "</td>");
							pesan.append("<td nowrap>"+ x.get("NAMA_REFF") + "</td>");
							pesan.append("<td nowrap>"+ x.get("NAMA_PP") + "</td>");
							pesan.append("<td nowrap>"+ x.get("SPAJ") + "</td>");
							pesan.append("<td nowrap>"+ x.get("PRODUK") + "</td>");
							pesan.append("<td nowrap>"+ x.get("PREMI") + "</td>");
							pesan.append("<td nowrap>"+ df.format(x.get("TGL_FURTHER")) + "</td>");
							pesan.append("<td nowrap>"+ df.format(x.get("DATE_LINE")) + "</td>");
							pesan.append("<td>"+ x.get("KETERANGAN") + "</td></tr>");
						}
						pesan.append("</table><br><br>");
						pesan.append("Email Leader: "+leader);
						
						String outputDir = props.getProperty("pdf.dir.report") + "\\follow_up_fr\\";
						String outputFilename = "SPAJFurtherRequirement.pdf";
						
						JasperUtils.exportReportToPdf(
								props.getProperty("report.uw.follow_up_fr")+".jasper", 
								outputDir, 
								outputFilename, 
								params, 
								data2, 
								PdfWriter.AllowPrinting, null, null);
						
						List<File> attachments = new ArrayList<File>();
						attachments.add(new File(outputDir + "\\" + outputFilename));
						
						HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerFollowUpFR", "EMAIL_FOLLOW_UP_FR");
						String from = props.getProperty("admin.ajsjava");
						String emailto = mapEmail.get("NAME")!=null? mapEmail.get("NAME").toString():null;
						String emailcc = leader;
						String emailbcc = mapEmail.get("NAME3")!=null? mapEmail.get("NAME3").toString():null;
						
						String[] to = emailto.split(";");
						String[] cc = emailcc.split(";");
						String[] bcc = emailbcc.split(";");
							
						try {
							EmailPool.send("E-Lions", 1, 1, 0, 0, 
									null, 0, 0, commonDao.selectSysdate(), null, 
									true,
									from, 
									to, 
									null, 
									bcc, 
									subject, 
									pesan.toString(),
									null, null);
						}
						catch (MailException e1) {
							// TODO Auto-generated catch block
							//logger.error("ERROR :", e1);
							desc = "ERROR";
							err = e1.getLocalizedMessage();
						}
					}
				}
			}
					
		} catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err = e.getLocalizedMessage();
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc, err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}

	}
	
	/**
	 * Fungsi scheduller untuk Virtual Account yang akan habis
	 * @author Canpri , Ryan
	 * @throws DataAccessException
	 */
	public void schedulerVA(String msh_name) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate 	= commonDao.selectSysdate();
		String desc	= "OK";
		String err = "";
		HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerStockVA", "W_STOCK_VA");
		String emailto = mapEmail.get("NAME")!=null? mapEmail.get("NAME").toString():null;
		String emailbcc = mapEmail.get("NAME3")!=null? mapEmail.get("NAME3").toString():null;
		
		String[] to = emailto.split(";");
		String[] bcc = emailbcc.split(";");
		try{
			ArrayList l_va = Common.serializableList(query("selectMinVA", null));
			Integer i_kirimEmail = 0;
			
			StringBuffer sb_mssg = new StringBuffer();
			sb_mssg.append("Virtual Account saat ini yg tersisa dibawah 200 :");
			for(int i=0; i<l_va.size();i++){
				HashMap m = (HashMap) l_va.get(i);
				
				String type = (String) m.get("TYPE");
				Integer ul = ((BigDecimal)m.get("UL")).intValue();
				Integer nul = ((BigDecimal)m.get("NUL")).intValue();
				Integer simpol = ((BigDecimal)m.get("SIMPOL")).intValue();
				Integer sprima = ((BigDecimal)m.get("SPRIMA")).intValue();
				Integer psave = ((BigDecimal)m.get("PSAVE")).intValue();
				Integer slsatu = ((BigDecimal)m.get("SLSATU")).intValue();
				Integer kcl = ((BigDecimal)m.get("KCL")).intValue();
				Integer family = ((BigDecimal)m.get("FAMILY")).intValue();
				Integer psavesyh = ((BigDecimal)m.get("PSAVESYH")).intValue();
				Integer dnmasprm = ((BigDecimal)m.get("DNMASPRM")).intValue();
				
				sb_mssg.append("\n\nTipe : "+type);
				if(ul<=200 && ul!=0){
					sb_mssg.append("\nUnit Link : "+ul);
					i_kirimEmail = 1;
				}
				if(nul<=200 && nul!=0){
					sb_mssg.append("\nNon Unit Link : "+nul);
					i_kirimEmail = 1;
				}
				if(simpol<=200 && simpol!=0){
					sb_mssg.append("\nSimpol : "+simpol);
					i_kirimEmail = 1;
				}
				if(sprima<=200 && sprima!=0){
					sb_mssg.append("\nSimas Prima : "+sprima);
					i_kirimEmail = 1;
				}
				if(psave<=200 && psave!=0){
					sb_mssg.append("\nProduk Save : "+psave);
					i_kirimEmail = 1;
				}
				if(slsatu<=200 && slsatu!=0){
					sb_mssg.append("\nSMile Link Satu : "+slsatu);
					i_kirimEmail = 1;
				}
				if(kcl<=200 && kcl!=0){
					sb_mssg.append("\nKecelakaan : "+kcl);
					i_kirimEmail = 1;
				}
				if(family<=200 && family!=0){
					sb_mssg.append("\nVIP Family Plan : "+family);
					i_kirimEmail = 1;
				}
				if(psavesyh<=200 && psavesyh!=0){
					sb_mssg.append("\nVIP Family Plan : "+family);
					i_kirimEmail = 1;
				}
				if(dnmasprm<=200 && dnmasprm!=0){
					sb_mssg.append("\nVIP Family Plan : "+family);
					i_kirimEmail = 1;
				}
				if(ul>200 && nul>200 && simpol>200 && sprima>200 && psave>200 && slsatu>200 && kcl>200 && family>200 && psavesyh>200 && dnmasprm>200){
					sb_mssg.append("\n-");
				}
			}
			
			if(i_kirimEmail>0){
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
						props.getProperty("admin.ajsjava"), to, null, bcc, 
						"Notifikasi virtual account yang akan habis", 
						sb_mssg.toString(), 
						null, null);
			}
		} catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR";
			err = "ERROR" + e.getMessage();
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), bcc, null, null, 
					"ERROR SCHEDULER VIRTUAL ACCOUNT", 
					err, null, null);
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc, err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Fungsi scheduller REMINDER PENGECEKAN NEON BOX/BILLBOARD/BACKDROP DAN UPDATE LIST ASSET 
	 * @author Canpri
	 * @throws ParseException 
	 * @throws DataAccessException
	 */
	public void schedulerNeonBox(String msh_name) throws ParseException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dy = new SimpleDateFormat("yyyy");
		Date bdate 	= commonDao.selectSysdate();
		String desc	= "OK";
		String err = "";
		
		try{
			String yr = dy.format(commonDao.selectSysdate());
			Integer i_yr = Integer.parseInt(yr)-1;
			String s_tgl1 = "31/12/"+i_yr;
			String s_tgl2 = "01/07/"+yr;
			
			Date d_tgl1 = df.parse(s_tgl1);
			Date d_tgl2 = df.parse(s_tgl2);
			
			String s_beg_date = "";
			String s_end_date = "";
			
			Date d_now = commonDao.selectSysdate();
			if(d_now.after(d_tgl1) && d_now.before(d_tgl2)){
				s_beg_date = "01/01/"+yr;
				s_end_date = "30/06/"+yr;
			}else{
				s_beg_date = "01/07/"+yr;
				s_end_date = "31/12/"+yr;
			}
			
			HashMap hm_map = new HashMap();
			hm_map.put("bdate", s_beg_date);
			hm_map.put("edate", s_end_date);
			
			ArrayList l_data = Common.serializableList(query("selectJtNeonBox",hm_map));
			if(l_data.size()>0){
				for(int i=0;i<l_data.size();i++){
					HashMap m = (HashMap) l_data.get(i);
					String mail = (String)m.get("LAR_EMAIL");
					String[] email= mail.split(";");
					
					StringBuffer mssg = new StringBuffer();
					mssg.append("Mohon untuk dilakukan :\n");
					mssg.append("<ul><li>Pengecekan dan Upload foto Neon Box/Billboard/Backdrop pada system E-Lions " +
							"(Update surat domisili dan status gedung)•	Pengecekan dan Upload foto Neon Box/Billboard/Backdrop " +
							"pada system  E-Lions (Update surat domisili dan status gedung)</li>");
					mssg.append("<li>Pengecekan list asset dan upload  pada system  E-Lions " +
							"(Update surat domisili dan status gedung)</li>");
					mssg.append("<li>Pembayaran pajak Reklame/Neon box yang akan jatuh tempo</li><ul>");
					mssg.append("\n\nTerima Kasih");
					
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							props.getProperty("admin.ajsjava"), email, new String[]{"bas@sinarmasmsiglife.co.id"}, new String[]{"canpri@sinarmasmsiglife.co.id"}, 
							"PENGECEKAN NEON BOX/BILLBOARD/BACKDROP DAN UPDATE LIST ASSET", 
							mssg.toString(), null, null);
				}
			}
			
		}catch(Exception e){
			logger.error("ERROR :", e);
			desc = "ERROR";
			err = "ERROR" + e.getMessage();
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER NEONBOX", 
					err, null, null);
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc, err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Fungsi scheduller Further H+1 ke cabang 
	 * @author Canpri
	 * @throws ParseException 
	 * @throws DataAccessException
	 */
	public void schedulerFRkeCabang(String msh_name) throws ParseException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate = commonDao.selectSysdate();
		Date fdate = commonDao.selectSysdateTruncated(-1);
		String desc	= "OK";
		String err = "";
		
		try{
			ArrayList l_cabang = Common.serializableList(basDao.selectLstAddrRegion(""));
			
			for(int i=0; i<l_cabang.size(); i++){
				HashMap hm_cabang = (HashMap) l_cabang.get(i);
				String email = (String)hm_cabang.get("LAR_EMAIL");
				Integer lar_id = ((BigDecimal)hm_cabang.get("LAR_ID")).intValue();
				String lar_admin = (String)hm_cabang.get("LAR_ADMIN");
				
				ArrayList data = Common.serializableList(query("selectFRkeCabang", lar_id));
				
				if(data.size()>0){
					if(!email.trim().equals("")){	
						
						String outputDir = props.getProperty("pdf.dir.report") + "\\further_requirement_cabang\\";
						//String outputDir = "D:\\test\\further_requirment_cabang\\";
						String outputFilename = lar_id+"_"+lar_admin+"_FR.xls";
			    		
						Map<String, Object> params = new HashMap<String, Object>();
			    		params.put("pdate", df.format(bdate));
			    		params.put("fdate", df.format(fdate));
			    		params.put("admin", lar_admin.toUpperCase());
						
						JasperUtils.exportReportToXls(props.getProperty("report.bas.report_further_requirement_cabang") + ".jasper", 
								outputDir, outputFilename, params, data, null);
						
						List<File> attachments = new ArrayList<File>();
						attachments.add(new File(outputDir + "\\" + outputFilename));
						
						StringBuffer pesan = new StringBuffer();
						//Format isi email
			
						pesan.append("Berikut adalah List Further Requirement tanggal "+df.format(fdate));
						
						String from = props.getProperty("admin.ajsjava");
						String emailto = email;
						String emailcc = null;
						String emailbcc = null;
						
						String[] to = emailto.split(";");
						//String[] cc = emailcc.split(";");
						//String[] bcc = emailbcc.split(";");
						
						try {
							//use email pool
							EmailPool.send("E-Lions", 1, 1, 0, 0, 
									null, 0, 0, bdate, null, 
									true, from, 
									to, 
									null, 
									null, 
									"List Further Requirement", 
									pesan.toString(), 
									attachments, null);
							
						}
						catch (MailException e1) {
							err = e1.getLocalizedMessage();
						}
					}
				}
				
			}
		}catch(Exception e){
			logger.error("ERROR :", e);
			desc = "ERROR";
			err = "ERROR" + e.getMessage();
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER FURTHER H+1 KE CABANG", 
					err, null, null);
		}
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc, err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	
	/**
	 * Fungsi scheduller Update ke posisi 999 (NOT PROCEED WITH) polis gantung 14 hari dari penginputan di BSIM
	 * @author Ryan
	 * @since 5 des 2014
	 */
	public void schedulerNotProceedWith(String msh_name) throws DataAccessException{
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date bdate 	= new Date();
			List<Map> data = new ArrayList<Map>();
			List<Map> data2 = new ArrayList<Map>();
			data = uwDao.schedulerNotProceedWith();
			data2 = uwDao.schedulerNotProceedWithNonBsim();
			String desc	= "OK";
			String err="";
			HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerNotProceedWith", "SCHEDULER_NPW");
			String[] to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
			String[] cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
			String[] bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
				try{
					// khusus BSIM
					for(int i=0; i<data.size();i++){
						Map dataSize = data.get(i);
						Integer telat =((BigDecimal) dataSize.get("TELAT")).intValue();
						String spaj = dataSize.get("REG_SPAJ").toString();
						
						//tambahan kalau udah masuk produksi ga usah di npw lagi						
						int count =  uwDao.selectValidasiProduksiDouble(spaj, 1);
						
						if(telat > 14 && count==0){
							//dikarenakan system ada yang kurang(saat cetak tidak insert date_print), maka dibuat alternatif.. dicek juga filenya
							String cabang =uwDao.selectCabangFromSpaj(spaj);
							String path = props.getProperty("pdf.dir.export").trim() + "\\" + 
									cabang.trim() + "\\" + 
									spaj + "\\sertifikat_powersave.pdf";
							File file = new File(path);
							if(!file.exists()){
								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", "AUTOPROSES UPDATE POSISI KE NOT PROCEED WITH POLIS PENDING BSIM > 14 KERJA ", spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);
							}

						}
					}
					// Req Mba tities Permohonan Ticket No. 72641 , Polis2 yang FR >30 hari , akan di ubah Ke NPW
					for(int i=0; i<data2.size();i++){
						Map dataSize2 = data2.get(i);
						Integer telat =((BigDecimal) dataSize2.get("TELAT")).intValue();
						String spaj = dataSize2.get("REG_SPAJ").toString();
						if(telat > 30){
							uwDao.updateMstPolicy(spaj, 999);
							uwDao.updateMstPolicyLsspdId(spaj, 29);
							uwDao.insertMstPositionSpaj("0", "AUTOPROSES UPDATE POSISI KE NOT PROCEED WITH POLIS-POLIS FR > 30 HARI KERJA ", spaj, 0);
							uwDao.prosesSnows(spaj, "0", 999, 212);
						}
					}
				}
				catch (Exception e) {
					desc = "ERROR";
					logger.error("ERROR :", e);
					err=e.getLocalizedMessage();
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							props.getProperty("admin.ajsjava"), to, cc, bcc, 
							"ERROR SCHEDULER PROSES NPW (NOT PROCEED WITH)", 
							err, null, null);
					
				}
			try {
				insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
				msh_name, bdate, new Date(), desc,err);
			}
			catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
	}
	
	
	/**
	 * 
	 * @author Lufi
	 * @since 9 Jan 2015
	 */
	public void schedulerPendingPrioritas(String msh_name) throws IOException, JRException {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate 	=  commonDao.selectSysdate();
		String desc	= "OK";
		String err="";
		ArrayList data=new ArrayList();		
		data = Common.serializableList(uwDao.selectDataPendingSmilePrioritas());		
		if(data!=null){
			try{
				
			
			Map<String, Object> params = new HashMap<String, Object>();
			HashMap mapReport = uwDao.selectMstConfig(6, "Report Pending Smile Prioritas", "report.pending.sp");
			HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerPendingPrioritas", "SCHEDULER_PENDING_PRIORITAS");
			String[] to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
			String[] cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
			String[] bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
			StringBuffer pesan = new StringBuffer();
			String outputDir = props.getProperty("pdf.dir.report") + "\\pending_sp\\";			
			String outputFilename = "pending_sp.xls";
			
			JasperUtils.exportReportToXls(mapReport.get("NAME") + ".jasper", 
					outputDir, outputFilename, params, data, null);
			
			List<File> attachments = new ArrayList<File>();
			attachments.add(new File(outputDir + "\\" + outputFilename));
			
			
			pesan.append("Dear All,");
			pesan.append("\n\nBerikut Dilampirkan untuk SMiLe Prioritas Pending Process Report.");
			pesan.append("\n\nRegards,");
			pesan.append("\n\nPT Asuransi Jiwa Sinarmas MSIG Tbk.");
			
			//use email pool
			EmailPool.send("E-Lions", 1, 1, 0, 0, 
					null, 0, 0, bdate, null, 
					true, "info@sinarmasmsiglife.co.id", 
					to, 
					cc, 
					bcc, 
					"Pending Proses SMiLe Prioritas", 
					pesan.toString(), 
					attachments, null);
			}catch (Exception e) {
				logger.error("ERROR :", e);
				err = e.getLocalizedMessage(); 
			}
			
			try {
				insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
				msh_name, bdate, new Date(), desc,err);
			}
			catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
	}
	
	/**
	 * Fungsi scheduller untuk auto transfer dari PRINT POLIS to FILLING untuk semua product BSM
	 * @author MANTA
	 * @throws DataAccessException
	 */
	public void schedulerTransferToFillingBSM(String msh_name) throws DataAccessException{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate 	= new Date();
		ArrayList<HashMap> data = new ArrayList<HashMap>();
		data = Common.serializableList(uwDao.selectTransToFillingBSM());
		String desc	= "OK";
		String err="";
		
		try{
			for(int i=0; i<data.size(); i++){
				Map x = data.get(i);
				String spaj = x.get("REG_SPAJ").toString();
				Integer lspd_id = 99;
				
				uwDao.insertMstPositionSpaj("0", "AUTOTRANSFER KE FILLING", spaj, 0);
				uwDao.updateMst_billing(spaj, null, null, null, 1, 1, lspd_id, null, null);
				uwDao.updatePosisiMst_policy(spaj, lspd_id);
				uwDao.updatePosisiMst_insured(spaj, lspd_id);
				
				//Proses SNOWS
				List<Map> inbox = null, inbox_uw = null;
				HashMap m = null;
				String mi_id = null;
				BigDecimal lspd_id_inbox = null;
				Date nowDate = commonDao.selectSysdate();

				inbox = uwDao.selectMstInbox(spaj, null);
				inbox_uw = uwDao.selectMstInbox(spaj, "1");
				if(inbox.size()>0){
					if(!inbox_uw.isEmpty()){
						m = (HashMap) inbox_uw.get(0);
						mi_id = (String) m.get("MI_ID");
						lspd_id_inbox = (BigDecimal) m.get("LSPD_ID");
					}
					uwDao.updateMstInboxLspdId(mi_id, 201, lspd_id_inbox.intValue(), 2, null, null, null, null, 0);
					MstInboxHist mstInboxHist = new MstInboxHist(mi_id, lspd_id_inbox.intValue(), 201, null, null, null, Integer.parseInt("0"), nowDate, null,0,0);
					uwDao.insertMstInboxHist(mstInboxHist);
					uwDao.insertMstPositionSpaj("0", "Transfer Dokumen dari U/W ke IMAGING", spaj, 0);
				}
			}
		}catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR";
			err = e.getLocalizedMessage();
		}
	}
	
	/**
	 * Scheduler Email Notifikasi Dashboard New Business
	 * req June (HD #73804)
	 * 
	 * @author Daru
	 * @since Jul 30, 2015
	 */
	public void schedulerNotifDashboard(String msh_name) throws DataAccessException {
		Date bDate = new Date();
		String desc = "OK";
		String err = "";
		
		try {
			// Utk keperluan testing, jika di production pastikan valuenya true
			Boolean kirim = true;
			
			/** Input SPAJ */
			
			// Warning
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("tipe", "warning");
			params.put("jenis", "input spaj");
			
			ArrayList<HashMap<String, Object>> list = (ArrayList<HashMap<String, Object>>) query("selectListDashboardInput", params);
			
			if(kirim && !Common.isEmpty(list))
				sendDashboardNotifEmail((String) params.get("tipe"), (String) params.get("jenis"), list, null);
			
			// Crisis
			params.put("tipe", "crisis");
			params.put("jenis", "input spaj");
			
			list = (ArrayList<HashMap<String, Object>>) query("selectListDashboardInput", params);
			
			if(kirim && !Common.isEmpty(list))
				sendDashboardNotifEmail((String) params.get("tipe"), (String) params.get("jenis"), list, null);
			
			/** Further
			 * 
			 * Karena further emailnya beda sendiri dibanding yg laen
			 * (kirimnya ke email cabang msg2 spaj), maka data dipecah 
			 * berdasarkan alamat emailnya
			 */
			
			// Warning
			params.put("tipe", "warning");
			params.put("jenis", "further requirement");
			
			list = (ArrayList<HashMap<String, Object>>) query("selectListDashboardFurther", params);
			
			if(kirim && !Common.isEmpty(list)) {
				ArrayList<String> listEmail = new ArrayList<String>();
				for(HashMap<String, Object>map : list) {
					String email2 = (String) map.get("EMAIL");
					if(!listEmail.contains(email2)) {
						listEmail.add(email2);
					}
				}
				
				for(String email : listEmail) {
					ArrayList<HashMap<String, Object>> sendList = new ArrayList<HashMap<String, Object>>();
					for(HashMap<String, Object> map : list) {
						if(email.equals((String) map.get("EMAIL"))) {
							sendList.add(map);
						}
					}
					
					sendDashboardNotifEmail((String) params.get("tipe"), (String) params.get("jenis"), sendList, email);
				}
			}
			
			// Crisis
			params.put("tipe", "crisis");
			params.put("jenis", "further requirement");
			
			list = (ArrayList<HashMap<String, Object>>) query("selectListDashboardFurther", params);
			
			if(kirim && !Common.isEmpty(list)) {
				ArrayList<String> listEmail = new ArrayList<String>();
				for(HashMap<String, Object>map : list) {
					String email2 = (String) map.get("EMAIL");
					if(!listEmail.contains(email2)) {
						listEmail.add(email2);
					}
				}
				
				for(String email : listEmail) {
					ArrayList<HashMap<String, Object>> sendList = new ArrayList<HashMap<String, Object>>();
					for(HashMap<String, Object> map : list) {
						if(email.equals((String) map.get("EMAIL"))) {
							sendList.add(map);
						}
					}
					
					sendDashboardNotifEmail((String) params.get("tipe"), (String) params.get("jenis"), sendList, email);
				}
			}
			
			/** Aksep */
			
			// Warning
			params.put("tipe", "warning");
			params.put("jenis", "akseptasi");
			
			list = (ArrayList<HashMap<String, Object>>) query("selectListDashboardAksep", params);
			
			if(kirim && !Common.isEmpty(list))
				sendDashboardNotifEmail((String) params.get("tipe"), (String) params.get("jenis"), list, null);
			
			// Crisis
			params.put("tipe", "crisis");
			params.put("jenis", "akseptasi");
			
			list = (ArrayList<HashMap<String, Object>>) query("selectListDashboardAksep", params);
			
			if(kirim && !Common.isEmpty(list))
				sendDashboardNotifEmail((String) params.get("tipe"), (String) params.get("jenis"), list, null);
			
			/** Print */
			
			// Warning
			params.put("tipe", "warning");
			params.put("jenis", "cetak polis");
			
			list = (ArrayList<HashMap<String, Object>>) query("selectListDashboardPrint", params);
			
			if(kirim && !Common.isEmpty(list))
				sendDashboardNotifEmail((String) params.get("tipe"), (String) params.get("jenis"), list, null);
			
			//Crisis
			params.put("tipe", "crisis");
			params.put("jenis", "cetak polis");
			
			list = (ArrayList<HashMap<String, Object>>) query("selectListDashboardPrint", params);
			
			if(kirim && !Common.isEmpty(list))
				sendDashboardNotifEmail((String) params.get("tipe"), (String) params.get("jenis"), list, null);
		} catch(Exception e) {
			desc = "ERROR";
			err = e.getLocalizedMessage();
			
			if(Common.isEmpty(err)) {
				err = "- Exception : \n";
				err += ExceptionUtils.getStackTrace(e);
			}
			
			EmailPool.send("E-Lions", 1, 0, 0, 0, null, 0, 0, new Date(), null, true, 
					props.getProperty("admin.ajsjava"), 
					new String[] { "daru@sinarmasmsiglife.co.id" }, 
					null, 
					null, 
					"Error Pada Scheduler Notifikasi Dashboard", 
					err, 
					null, 
					null);
		} finally {
			try {
				insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(), msh_name, bDate, new Date(), desc, err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
	}
	
	/**
	 * Method untuk kirim email notifikasi dashboard sesuai format
	 * @param tipe (warning / crisis)
	 * @param jenisData (Input SPAJ / Further / Akseptasi / Cetak Polis)
	 * @param list Data
	 * @param email Email Tujuan
	 * 
	 * @author Daru
	 * @since Jul 31, 2015
	 */
	private void sendDashboardNotifEmail(String tipe, String jenisData, ArrayList<HashMap<String, Object>> list, String email) throws Exception {
		// Daftar email tujuan
		String[] emailBas = new String[] {
				"desy@sinarmasmsiglife.co.id",
				"iway@sinarmasmsiglife.co.id",
				"Martino@sinarmasmsiglife.co.id",
				"pratidina@sinarmasmsiglife.co.id",
				"tedi@sinarmasmsiglife.co.id",
				"Yudi@sinarmasmsiglife.co.id",
				"Yune@sinarmasmsiglife.co.id"
//				"daru@sinarmasmsiglife.co.id"
		};
		
		String[] emailNbIdv = new String[] {
				"newbusiness@sinarmasmsiglife.co.id"
//				"daru@sinarmasmsiglife.co.id"
		};
		
		String[] emailDeptHead = new String[] {
				"inge@sinarmasmsiglife.co.id",
				"hendra@sinarmasmsiglife.co.id",
				"sylvia@sinarmasmsiglife.co.id"
//				"daru@sinarmasmsiglife.co.id"
		};
		
		// Initialize default variable
		String[] emailTo = new String[] {};
		String labelJenis = "Tgl Input";
		String tindakan = "Discuss";
		
		// Set variable berdasarkan jenisData & tipe
		if("input spaj".equals(jenisData)) {
			if("warning".equals(tipe)) {
				emailTo = emailBas;
			} else {
				emailTo = (String[]) ArrayUtils.addAll(emailBas, emailDeptHead);
				tindakan = "1. Discuss/Meeting mengenai keterlambatan input SPAJ<br>" +
						"2. Identifikasi alasan keterlambatan input SPAJ<br>" +
						"3. Mencari Solusi";
			}
		} else if("further requirement".equals(jenisData)) {
			labelJenis = "Tgl Further";
//			email = "daru@sinarmasmsiglife.co.id";
			
			if(!Common.isEmpty(email))
				emailTo = email.split(";");
			
			if("warning".equals(tipe)) {
				tindakan = "Infokan report further ke leader";
			} else {
				tindakan = "Lengkapi kekurangan berkas / further";
			}
		} else if("akseptasi".equals(jenisData)) {
			labelJenis = "Tgl Selesai Proses SPAJ";
			
			if("warning".equals(tipe)) {
				emailTo = emailNbIdv;
				tindakan = "Overtime / share job";
			} else {
				emailTo = (String[]) ArrayUtils.addAll(emailNbIdv, emailDeptHead);
				tindakan = "1. Discuss/Meeting mengenai keterlambatan aksep<br>" +
						"2. Identifikasi alasan keterlambatan aksep<br>" +
						"3. Mencari solusi agar aksep dapat diselesaikan";
			}
		} else if("cetak polis".equals(jenisData)) {
			labelJenis = "Tgl Akseptasi";
			
			if("warning".equals(tipe)) {
				emailTo = emailNbIdv;
				tindakan = "Overtime / share job";
			} else {
				emailTo = (String[]) ArrayUtils.addAll(emailNbIdv, emailDeptHead);
				tindakan = "1. Discuss/Meeting mengenai keterlambatan print<br>" +
						"2. Identifikasi alasan keterlambatan print<br>" +
						"3. Mencari solusi<br>" +
						"4. Sharing Pekerjaan";
			}
		}
		
		// Susun message dalam table
		String msg = "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" style=\"font-family: Courier New, Arial, sans-serif; font-size: 14px;\">";
		for(HashMap<String, Object> map : list) {
			msg += 
					"<tr>" +
						"<td width=\"200\">SPAJ</td>" +
						"<td width=\"20\">:</td>" +
						"<td>" + (String) map.get("REG_SPAJ") + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>" + labelJenis + "</td>" +
						"<td>:</td>" +
						"<td>" + (String) map.get("TGL_INPUT") + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>Keterangan Proses</td>" +
						"<td>:</td>" +
						"<td>" + WordUtils.capitalize(jenisData) + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>Keterangan Barometer</td>" +
						"<td>:</td>" +
						"<td>" + WordUtils.capitalize(tipe) + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td>Keterlambatan Proses</td>" +
						"<td>:</td>" +
						"<td>" + (map.get("LAMA_PROSES") == null ? "-" : map.get("LAMA_PROSES").toString() + " hari") + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td style=\"vertical-align: top\">Tindakan</td>" +
						"<td style=\"vertical-align: top\">:</td>" +
						"<td>" + tindakan + "</td>" +
					"</tr>" +
					"<tr>" +
						"<td colspan=\"3\">&nbsp;</td>" +
					"</tr>";
		}
		msg += "</table>";
		
		EmailPool.send("Dashboard Notification", 1, 1, 0, 0, null, 0, 0, new Date(), null, true, 
				props.getProperty("admin.ajsjava"), 
				emailTo, 
				null, 
				new String[] { "daru@sinarmasmsiglife.co.id" }, 
				tipe.toUpperCase() + " PADA PROSES " + jenisData.toUpperCase(), 
				msg, 
				null, 
				null,
				19);
	}
	
	/**
	 * Scheduler untuk menghitung lama proses polis terbit dalam jam
	 * (dari print polis sampai polis dikirimkan).
	 * 
	 * @author Daru
	 * @since Aug 5, 2015
	 */
	public void schedulerCountPolicyIssueHours(String msh_name) {
		Date bDate = new Date();
		String desc = "OK";
		String err = "";
		
		try {
			update("updateCountPolicyIssueHours", null);
		} catch(Exception e) {
			desc = "ERROR";
			err = e.getLocalizedMessage();
			
			if(Common.isEmpty(err)) {
				err = "- Exception : \n";
				err += ExceptionUtils.getStackTrace(e);
			}
			
			EmailPool.send("E-Lions", 1, 0, 0, 0, null, 0, 0, new Date(), null, true, 
					props.getProperty("admin.ajsjava"), 
					new String[] { "daru@sinarmasmsiglife.co.id" }, 
					null, 
					null, 
					"Error Pada Scheduler Count Policy Issue Hours", 
					err, 
					null, 
					null);
		} finally {
			try {
				insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(), msh_name, bDate, new Date(), desc, err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
	}

	public void schedulerReportKycNewBussiness(String msh_name) {
		Date bDate = new Date();
		String desc = "OK";
		String err = "";
		
		try {			
			Map<String, Object> params = new HashMap<String, Object>();
			SimpleDateFormat sdf= new SimpleDateFormat("EEE");
			Date today = commonDao.selectSysdateTruncated(0);
			//String temp = sdf.format(today).toUpperCase();
			Date yesterday = commonDao.selectSysdateTruncated(-1);			
			//if(temp.equals("MON"))yesterday=commonDao.selectSysdateTruncated(-2);
			
			ArrayList listKycNB = Common.serializableList(uwDao.selectKYCnewBis_utama(FormatDate.toString(yesterday), FormatDate.toString(yesterday)));
			ArrayList data = Common.serializableList(uwDao.selectLsKycNewBus(listKycNB));
			if(!data.isEmpty()){
				params.put("tanggalAwal", yesterday);
				params.put("tanggalAkhir", yesterday);
				
				HashMap mapReport = uwDao.selectMstConfig(6, "Report Daily Monitoring KYC", "report.report_new_business_case");
				HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerDailyReportKycNewBussiness", "SCHEDULER_DAILY_REPORT_KYC_NB");
				String[] to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
				String[] cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
				String[] bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
				StringBuffer pesan = new StringBuffer();
				String outputDir = props.getProperty("pdf.dir.report") + "\\daily_kyc\\";			
				String outputFilename = "report_kyc.xls";
					
				JasperUtils.exportReportToXls(mapReport.get("NAME") + ".jasper", 
						outputDir, outputFilename, params, data, null);
					
				List<File> attachments = new ArrayList<File>();
				attachments.add(new File(outputDir + "\\" + outputFilename));
					
					
				pesan.append("Dear All,");
				pesan.append("\n\n<font color='#FF0000'>Terlampir</font> Report KYC periode"+ " "+ FormatDate.toIndonesian(today));
				pesan.append("\n\nTerima Kasih,");			
				pesan.append("\n\nUnderwriting Dept.");
				
				
				EmailPool.send("E-Lions", 1, 1, 0, 0, 
						null, 0, 0, bDate, null, 
						true, "info@sinarmasmsiglife.co.id", 
						to, 
						cc, 
						bcc, 
						"Report KYC periode"+ " "+ FormatDate.toIndonesian(today), 
						pesan.toString(), 
						attachments, null);
			}
			
		} catch(Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR";
			err = e.getLocalizedMessage();
			
			if(Common.isEmpty(err)) {
				err = "- Exception : \n";
				err += ExceptionUtils.getStackTrace(e);
			}
			
			EmailPool.send("E-Lions", 1, 0, 0, 0, null, 0, 0, new Date(), null, true, 
					props.getProperty("admin.ajsjava"), 
					new String[] { "antasari@sinarmasmsiglife.co.id" }, 
					null, 
					null, 
					"Error Pada Scheduler Report KYC New Bussiness", 
					err, 
					null, 
					null);
		} finally {
			try {
				insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(), msh_name, bDate, new Date(), desc, err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}		
	}
	
	/**
	 * Fungsi scheduller untuk Report PEP
	 * @author Iga
	 * @time 04/11/2019
	 */
	public void schedulerReportPep(String msh_name) {
		Date bDate = new Date();
		String desc = "OK";
		String err = "";
		DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		
		try {			
			Map<String, Object> params = new HashMap<String, Object>();
			SimpleDateFormat sdf= new SimpleDateFormat("EEE");
			Date today = commonDao.selectSysdateTruncated(0);
			Date yesterday = commonDao.selectSysdateTruncated(-1);			
			//if(temp.equals("MON"))yesterday=commonDao.selectSysdateTruncated(-2);
			
			ArrayList listReportPep= Common.serializableList(uwDao.selectReportPep(FormatDate.toString(yesterday), FormatDate.toString(yesterday)));
//			if(!listReportPep.isEmpty()){
				params.put("tanggalAwal", yesterday);
				params.put("tanggalAkhir", yesterday);
				
				HashMap mapReport = uwDao.selectMstConfig(6, "Report Daily Monitoring PEP", "report.report_pep");
				HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerDailyReportPep", "SCHEDULER_DAILY_REPORT_PEP");
				String[] to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
				String[] cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
				String[] bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
				StringBuffer pesan = new StringBuffer();
				String outputDir = props.getProperty("pdf.dir.report") +  "\\Daily Report PEP\\";			
				String outputFilename = "report_pep"+df2.format(today)+".xls" ;
					
				JasperUtils.exportReportToXls(mapReport.get("NAME") + ".jasper", 
						outputDir, outputFilename, params, listReportPep, null);
					
				List<File> attachments = new ArrayList<File>();
				attachments.add(new File(outputDir + "\\" + outputFilename));
					
				pesan.append("Dear Tim UKPN,");
				pesan.append("\n\n<font color='#FF0000'>Terlampir list nasabah dengan kategori Politically Exposed Person (PEP).</font>");
				pesan.append("\n\nDemikian kami sampaikan atas perhatian dan kerjasamanya kami ucapkan terimakasih.");			
				pesan.append("\n\nNew Business.");		
				
				EmailPool.send("E-Lions", 1, 1, 0, 0, 
						null, 0, 0, bDate, null, 
						true, "info@sinarmasmsiglife.co.id", 
						to, 
						cc, 
						bcc, 
						"[INFO PEP] Report Politically Exposed Person "+ " "+ FormatDate.toIndonesian(today), 
						pesan.toString(), 
						attachments, null);
//			}
			
		} catch(Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR";
			err = e.getLocalizedMessage();
			
			HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerDailyReportPep", "SCHEDULER_DAILY_REPORT_PEP");
			String[] bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
			
			if(Common.isEmpty(err)) {
				err = "- Exception : \n";
				err += ExceptionUtils.getStackTrace(e);
			}
			
			EmailPool.send("E-Lions", 1, 0, 0, 0, null, 0, 0, new Date(), null, true, 
					props.getProperty("admin.ajsjava"), 
					bcc, 
					null, 
					null, 
					"ERROR SCHEDULER REPORT PEP", 
					err, 
					null, 
					null);
		} finally {
			try {
				insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(), msh_name, bDate, new Date(), desc, err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
		
	}
	
	/**
	 * Fungsi scheduller untuk ALert STOCK SPAJ
	 * @author Ryan
	 * @throws DataAccessException
	 */
	public void schedulerStockSPAJ(String msh_name) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate 	= commonDao.selectSysdate();
		String desc	= "OK";
		String err = "";
		StringBuffer pesan = new StringBuffer();
		Integer i_kirimEmail=0;
		try{
			//setting TO, FROM, CC 
			HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerStockSPAJ", "W_STOCK_SPAJ");
			String from = props.getProperty("admin.ajsjava");
			String emailto = mapEmail.get("NAME")!=null? mapEmail.get("NAME").toString():null;
			String emailcc = mapEmail.get("NAME2")!=null? mapEmail.get("NAME2").toString():null;
			String emailbcc = mapEmail.get("NAME3")!=null? mapEmail.get("NAME3").toString():null;
			
			String[] to = emailto.split(";");
			String[] cc = emailcc.split(";");
			String[] bcc = emailbcc.split(";");
			ArrayList<HashMap> stock1 = Common.serializableList(basDao.selectStokSpaj("0",null));
			//Header Table
			pesan.append("<table cellpadding='4' cellspacing='0' style='table-layout: fixed; width:100%'>");
			pesan.append("<tr bgcolor='#A8B8EE'><th>Prefix</th>");
			pesan.append("<th>Deskripsi</th>");
			pesan.append("<th>Stock</th></tr>");
			
			for(int k=0;k<stock1.size();k++){
				HashMap x = stock1.get(k);
				Integer amount=((BigDecimal) x.get("LSJS_STOCK")).intValue();
					if (amount < 500 && amount !=0){
						pesan.append("<tr bgcolor='#368BC1' align='center'><td nowrap>"+ x.get("LSJS_PREFIX") + "</td>");
						pesan.append("<td nowrap>"+ x.get("LSJS_DESC") + "</td>");
						pesan.append("<td nowrap>"+ amount + "</td></tr>");
						i_kirimEmail=1;
						}
			}
			
			pesan.append("</table><br><br>");
			if(i_kirimEmail>0){	
				EmailPool.send("E-Lions", 1, 1, 0, 0, 
						null, 0, 0, commonDao.selectSysdate(), null, 
						true,
						from, 
						to, 
						null, 
						bcc, 
						"Email scheduler otomatis Warning Stock SPAJ (Testing)", 
						pesan.toString(), 
						null, null);
			}
			
		} catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR";
			err = "ERROR" + e.getMessage();
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), new String[]{"ryan@sinarmasmsiglife.co.id"}, null, null, 
					"ERROR SCHEDULER CEK STOCK SPAJ", 
					err, null, null);
		}
		
		try {
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc, err);
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	public void schedulerJatuhTempoVisaCampBas(String msh_name) {
		Date bDate = new Date();
		String desc = "OK";
		String err = "";
		
		try {			
			Map<String, Object> params = new HashMap<String, Object>();
			SimpleDateFormat sdf= new SimpleDateFormat("EEE");
			Date today = commonDao.selectSysdateTruncated(0);			
			
			ArrayList data = Common.serializableList(basDao.selectDataJatuhTempoVisa());
			if(!data.isEmpty()){
				params.put("tanggalAwal", today);				
				HashMap mapReport = uwDao.selectMstConfig(6, "Report Follow Up Jatuh Tempo Visa", "report.report_jt_visacamp_bas");
				HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerJatuhTempoVisaCampBas", "SCHEDULER_FU_JT_VISA");
				String[] to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
				String[] cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
				String[] bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
				StringBuffer pesan = new StringBuffer();
				String outputDir = props.getProperty("pdf.dir.report") + "\\daily_jtvisa\\";			
				String outputFilename = "report_jtvisa.xls";
					
				JasperUtils.exportReportToXls(mapReport.get("NAME") + ".jasper", 
						outputDir, outputFilename, params, data, null);
					
				List<File> attachments = new ArrayList<File>();
				attachments.add(new File(outputDir + "\\" + outputFilename));
					
					
				pesan.append("Dear All,");
				pesan.append("\n\n<font color='#FF0000'>Terlampir</font> Report Follow Up Jatuh Tempo Visa  periode"+ " "+ FormatDate.toIndonesian(today));
				pesan.append("\n\nTerima Kasih,");		
				
				
				EmailPool.send("E-Lions", 1, 1, 0, 0, 
						null, 0, 0, bDate, null, 
						true, "info@sinarmasmsiglife.co.id", 
						to, 
						cc, 
						bcc, 
						"Report Jatuh Tempo Visa periode"+ " "+ FormatDate.toIndonesian(today), 
						pesan.toString(), 
						attachments, null);
			}
			
		} catch(Exception e) {
			desc = "ERROR";
			err = e.getLocalizedMessage();
			
			if(Common.isEmpty(err)) {
				err = "- Exception : \n";
				err += ExceptionUtils.getStackTrace(e);
			}
			
			EmailPool.send("E-Lions", 1, 0, 0, 0, null, 0, 0, new Date(), null, true, 
					props.getProperty("admin.ajsjava"), 
					new String[] { "antasari@sinarmasmsiglife.co.id" }, 
					null, 
					null, 
					"Error Pada Scheduler Report KYC New Bussiness", 
					err, 
					null, 
					null);
		} finally {
			try {
				insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(), msh_name, bDate, new Date(), desc, err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
		
	}
	
	/**
	 * (*RDL) Fungsi scheduller Update ke posisi 999 (NOT PROCEED WITH) DMTM 
	 * dengan kondisi 30 hari SPAJ tidak berubah status menjadi accepted sejak SPAJ transfer ke speedy atau 45 hari SPAJ gagal recurring
	 * @author Ridhaal
	 * @since 4 Feb 2016
	 */
	public void schedulerNotProceedWithRecurring(String msh_name) throws DataAccessException{

		String desc	= "OK";
		String err="";

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate 	= new Date();

		List<Map> data = new ArrayList<Map>();
		List<Map> data2 = new ArrayList<Map>();

		data = uwDao.schedulerNotProceedWithDMTMProcesRecuiring(3,0); //proses DMTM Status FR and gagal recuiring ---- (*RDL)
		data2 = uwDao.schedulerNotProceedWithDMTMProcesRecuiring(5,2); //proses DMTM Status belum accept setelah sukses recuiring ---- (*RDL)

		//Info : untuk uwDao.selectMstConfig(app_id, section, sub_section); dapat dilihat nilainya pada database eka.mst_config ---- (*RDL)
		// mapEmail digunakan untuk mengirim email ke list user yang menerima email schedulerNotProceedWithDMTM ---- (*RDL)
		HashMap mapEmail10 = new HashMap(), mapEmail12 = new HashMap(),mapEmail20 = new HashMap(),mapEmail21 = new HashMap(),mapEmail22 = new HashMap(),mapEmail23 = new HashMap(),mapEmail24 = new HashMap(),mapEmail25 = new HashMap(),mapEmail26 = new HashMap(),mapEmail27 = new HashMap();
		
		String from = props.getProperty("admin.ajsjava");
		String[] to10 = null,to12 = null,to20 = null,to21 = null,to22 = null,to23 = null, to24 = null, to25 = null,to26 = null, to27 = null;		
		String[] cc10 = null,cc12 = null,cc20 = null,cc21 = null,cc22 = null,cc23 = null, cc24 = null, cc25 = null,cc26 = null, cc27 = null;
		
		int no1_10 = 1,no1_12 = 1,no1_20 = 1,no1_21 = 1,no1_22 = 1,no1_23 = 1,no1_24 = 1,no1_25 = 1,no1_26 = 1,no1_27 = 1;
		int no2_10 = 1,no2_12 = 1,no2_20 = 1,no2_21 = 1,no2_22 = 1,no2_23 = 1,no2_24 = 1,no2_25 = 1,no2_26 = 1,no2_27 = 1;
		int no3_10 = 1,no3_12 = 1,no3_20 = 1,no3_21 = 1,no3_22 = 1,no3_23 = 1,no3_24 = 1,no3_25 = 1,no3_26 = 1,no3_27 = 1;
		int no4_10 = 1,no4_12 = 1,no4_20 = 1,no4_21 = 1,no4_22 = 1,no4_23 = 1,no4_24 = 1,no4_25 = 1,no4_26 = 1,no4_27 = 1;
	
		boolean pesan30FRgagalRec10 = false, pesan30FRgagalRec12 = false, pesan30FRgagalRec20 = false, pesan30FRgagalRec21 = false, pesan30FRgagalRec22 = false, pesan30FRgagalRec23 = false, pesan30FRgagalRec24 = false, pesan30FRgagalRec25 = false, pesan30FRgagalRec26 = false, pesan30FRgagalRec27 = false ; 
		boolean pesan45FRgagalRec10 = false, pesan45FRgagalRec12 = false, pesan45FRgagalRec20 = false, pesan45FRgagalRec21 = false, pesan45FRgagalRec22 = false, pesan45FRgagalRec23 = false, pesan45FRgagalRec24 = false, pesan45FRgagalRec25 = false, pesan45FRgagalRec26 = false, pesan45FRgagalRec27 = false ; 
		boolean pesan20FRsuksesRec10 = false, pesan20FRsuksesRec12 = false, pesan20FRsuksesRec20 = false, pesan20FRsuksesRec21 = false, pesan20FRsuksesRec22 = false, pesan20FRsuksesRec23 = false, pesan20FRsuksesRec24 = false, pesan20FRsuksesRec25 = false, pesan20FRsuksesRec26 = false, pesan20FRsuksesRec27 = false ; 
		boolean pesan30FRsuksesRec10 = false, pesan30FRsuksesRec12 = false, pesan30FRsuksesRec20 = false, pesan30FRsuksesRec21 = false, pesan30FRsuksesRec22 = false, pesan30FRsuksesRec23 = false, pesan30FRsuksesRec24 = false, pesan30FRsuksesRec25 = false, pesan30FRsuksesRec26 = false, pesan30FRsuksesRec27 = false ; 
		
		String pesanFull = null;
		String pesanGagal3010 = null, pesanGagal3012 = null, pesanGagal3020 = null,pesanGagal3021 = null,pesanGagal3022 = null, pesanGagal3023 = null, pesanGagal3024 = null, pesanGagal3025 = null, pesanGagal3026 = null,  pesanGagal3027 = null;
		String pesanGagal4510 = null, pesanGagal4512 = null, pesanGagal4520 = null,pesanGagal4521 = null,pesanGagal4522 = null, pesanGagal4523 = null, pesanGagal4524 = null, pesanGagal4525 = null, pesanGagal4526 = null,  pesanGagal4527 = null;
		String pesanSukses2010 = null, pesanSukses2012 = null, pesanSukses2020 = null,pesanSukses2021 = null,pesanSukses2022 = null, pesanSukses2023 = null, pesanSukses2024 = null, pesanSukses2025 = null, pesanSukses2026 = null,  pesanSukses2027 = null;
		String pesanSukses3010 = null, pesanSukses3012 = null, pesanSukses3020 = null,pesanSukses3021 = null,pesanSukses3022 = null, pesanSukses3023 = null, pesanSukses3024 = null, pesanSukses3025 = null, pesanSukses3026 = null,  pesanSukses3027 = null;
		
		StringBuffer pesanHeader = new StringBuffer(), pesanHeaderT = new StringBuffer(), pesanHeader2 = new StringBuffer(), pesanHeaderT2 = new StringBuffer() ;					
		
		StringBuffer pesanFRgagalRec10 = new StringBuffer(), pesanFRgagalRec12 = new StringBuffer(), pesanFRgagalRec20 = new StringBuffer(), pesanFRgagalRec21 = new StringBuffer(), pesanFRgagalRec22 = new StringBuffer(), pesanFRgagalRec23 = new StringBuffer(), pesanFRgagalRec24 = new StringBuffer(), pesanFRgagalRec25 = new StringBuffer(), pesanFRgagalRec26 = new StringBuffer(), pesanFRgagalRec27 = new StringBuffer() ;
		StringBuffer pesanNPWgagalRec10 = new StringBuffer(), pesanNPWgagalRec12 = new StringBuffer(), pesanNPWgagalRec20 = new StringBuffer(), pesanNPWgagalRec21 = new StringBuffer(), pesanNPWgagalRec22 = new StringBuffer(), pesanNPWgagalRec23 = new StringBuffer(), pesanNPWgagalRec24 = new StringBuffer(), pesanNPWgagalRec25 = new StringBuffer(), pesanNPWgagalRec26 = new StringBuffer(), pesanNPWgagalRec27 = new StringBuffer() ;
		StringBuffer pesanFRsuksesRec10 = new StringBuffer(), pesanFRsuksesRec12 = new StringBuffer(), pesanFRsuksesRec20 = new StringBuffer(), pesanFRsuksesRec21 = new StringBuffer(), pesanFRsuksesRec22 = new StringBuffer(), pesanFRsuksesRec23 = new StringBuffer(), pesanFRsuksesRec24 = new StringBuffer(), pesanFRsuksesRec25 = new StringBuffer(), pesanFRsuksesRec26 = new StringBuffer(), pesanFRsuksesRec27 = new StringBuffer() ;
		StringBuffer pesanNPWsuksesRec10 = new StringBuffer(), pesanNPWsuksesRec12 = new StringBuffer(), pesanNPWsuksesRec20 = new StringBuffer(), pesanNPWsuksesRec21 = new StringBuffer(), pesanNPWsuksesRec22 = new StringBuffer(), pesanNPWsuksesRec23 = new StringBuffer(), pesanNPWsuksesRec24 = new StringBuffer(), pesanNPWsuksesRec25 = new StringBuffer(), pesanNPWsuksesRec26 = new StringBuffer(), pesanNPWsuksesRec27 = new StringBuffer() ;
		
		StringBuffer pesanFooterT = new StringBuffer(), pesanFooter = new StringBuffer();
		
		mapEmail10 = uwDao.selectMstConfig(6, "schedulerNotProceedWithDMTM", "DMTM_DMTM");
		to10 = mapEmail10.get("NAME")!=null?mapEmail10.get("NAME").toString().split(";"):null;
		cc10 = mapEmail10.get("NAME2")!=null?mapEmail10.get("NAME2").toString().split(";"):null;
		String[] bcc = mapEmail10.get("NAME3")!=null?mapEmail10.get("NAME3").toString().split(";"):null;

		mapEmail12 = uwDao.selectMstConfig(6, "schedulerNotProceedWithDMTM", "DMTM_BSIM");
		to12 = mapEmail12.get("NAME")!=null?mapEmail12.get("NAME").toString().split(";"):null;
		cc12 = mapEmail12.get("NAME2")!=null?mapEmail12.get("NAME2").toString().split(";"):null;

		mapEmail20 = uwDao.selectMstConfig(6, "schedulerNotProceedWithDMTM", "DMTM_ARCO");
		to20 = mapEmail20.get("NAME")!=null?mapEmail20.get("NAME").toString().split(";"):null;
		cc20 = mapEmail20.get("NAME2")!=null?mapEmail20.get("NAME2").toString().split(";"):null;

		mapEmail21 = uwDao.selectMstConfig(6, "schedulerNotProceedWithDMTM", "DMTM_SMP");
		to21 = mapEmail21.get("NAME")!=null?mapEmail21.get("NAME").toString().split(";"):null;
		cc21 = mapEmail21.get("NAME2")!=null?mapEmail21.get("NAME2").toString().split(";"):null;

		mapEmail22 = uwDao.selectMstConfig(6, "schedulerNotProceedWithDMTM", "DMTM_SSS");
		to22 = mapEmail22.get("NAME")!=null?mapEmail22.get("NAME").toString().split(";"):null;
		cc22 = mapEmail22.get("NAME2")!=null?mapEmail22.get("NAME2").toString().split(";"):null;

		mapEmail23 = uwDao.selectMstConfig(6, "schedulerNotProceedWithDMTM", "DMTM_SIP");
		to23 = mapEmail23.get("NAME")!=null?mapEmail23.get("NAME").toString().split(";"):null;
		cc23 = mapEmail23.get("NAME2")!=null?mapEmail23.get("NAME2").toString().split(";"):null;

		mapEmail24 = uwDao.selectMstConfig(6, "schedulerNotProceedWithDMTM", "DMTM_REDBERRY");
		to24 = mapEmail24.get("NAME")!=null?mapEmail24.get("NAME").toString().split(";"):null;
		cc24 = mapEmail24.get("NAME2")!=null?mapEmail24.get("NAME2").toString().split(";"):null;

		mapEmail25 = uwDao.selectMstConfig(6, "schedulerNotProceedWithDMTM", "DMTM_PSJS");
		to25 = mapEmail25.get("NAME")!=null?mapEmail25.get("NAME").toString().split(";"):null;
		cc25 = mapEmail25.get("NAME2")!=null?mapEmail25.get("NAME2").toString().split(";"):null;

		mapEmail26 = uwDao.selectMstConfig(6, "schedulerNotProceedWithDMTM", "DMTM_SAPTA_SARANA_SEJAHTERA");
		to26 = mapEmail26.get("NAME")!=null?mapEmail26.get("NAME").toString().split(";"):null;
		cc26 = mapEmail26.get("NAME2")!=null?mapEmail26.get("NAME2").toString().split(";"):null;

		mapEmail27 = uwDao.selectMstConfig(6, "schedulerNotProceedWithDMTM", "DMTM_DENA_AKHYARO_NUSANTARA");
		to27 = mapEmail27.get("NAME")!=null?mapEmail27.get("NAME").toString().split(";"):null;
		cc27 = mapEmail27.get("NAME2")!=null?mapEmail27.get("NAME2").toString().split(";"):null;


		try{
			//---- (*RDL)-------------------------------------------------------------------------start

			pesanHeader.append("<body><b>Dear Team </b><br><br>Mohon bantuannya untuk melakukan follow up ke masing-masing nasabah berikut ini supaya polis dapat segera diterbitkan. Terlampir data-data SPAJ yang apabila tidak dilengkapi akan kami Bekukan dan akan kami proses Refund ke Rekening yang tercantum dalam SPAJ.");
			pesanHeaderT.append("<table border ='1' cellpadding='4' cellspacing='0' >");
			pesanHeaderT.append("<tr bgcolor='#A8B8EE'><FONT SIZE=2>");
			pesanHeaderT.append("<th align='center'>No.</th>");
			pesanHeaderT.append("<th align='center'>Site</th>");
			pesanHeaderT.append("<th align='center'>Nama Pemegang Polis</th>");
			pesanHeaderT.append("<th align='center'>No. SPAJ</th>");
			pesanHeaderT.append("<th align='center'>Nama Produk</th>");
			pesanHeaderT.append("<th align='center'>Premi</th>");
			pesanHeaderT.append("<th align='center'>Tgl. Further</th>");
			pesanHeaderT.append("<th align='center'>Date Line</th>");
			pesanHeaderT.append("<th align='center'>Keterangan</th></font></tr>");

			pesanHeader2.append("<body><b>Dear Team </b><br><br>Terlampir data-data SPAJ NPW DMTM untuk dicek dan dibatalkan : ");
			pesanHeaderT2.append("<table border ='1' cellpadding='4' cellspacing='0' >");
			pesanHeaderT2.append("<tr bgcolor='#A8B8EE'><FONT SIZE=2>");
			pesanHeaderT2.append("<th align='center'>No.</th>");
			pesanHeaderT2.append("<th align='center'>Channel</th>");
			pesanHeaderT2.append("<th align='center'>Site</th>");
			pesanHeaderT2.append("<th align='center'>Reg. SPAJ</th>");
			pesanHeaderT2.append("<th align='center'>Produk Utama</th>");
			pesanHeaderT2.append("<th align='center'>Plan</th>");
			pesanHeaderT2.append("<th align='center'>Tgl. NPW</th>");
			pesanHeaderT2.append("<th align='center'>Tgl. Input</th>");
			pesanHeaderT2.append("<th align='center'>Pemegang Polis</th>");
			pesanHeaderT2.append("<th align='center'>Posisi Documen Terakhir Sebelum NPW</th>");					
			pesanHeaderT2.append("<th align='center'>Keterangan</th></font></tr>");

			// untuk proses gagal recuring	- Start 

			for(int i=0; i<data.size();i++){
				Map x = data.get(i);
				Integer telat =((BigDecimal) x.get("HARI_TELAT")).intValue();
				Integer status = ((BigDecimal) x.get("STAT_RECUR")).intValue();
				String spaj = x.get("REG_SPAJ").toString();
				String lca_id = x.get("LCA_ID").toString();
				String lwk_id = x.get("LWK_ID").toString();
				String lsrg_id = x.get("LSRG_ID").toString();

				if(lca_id.equalsIgnoreCase("40")){
					if(lwk_id.equalsIgnoreCase("01")){ 
						if(lsrg_id.equals("00")){//Roxy
						//DMTM - GENERAL
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 0 && telat > 45) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 45 HARI KERJA GAGAL RECURRING";

								pesanNPWgagalRec10.append("<tr><FONT SIZE=2><td nowrap>"+ no1_10++  + "</td>");
								pesanNPWgagalRec10.append("<td nowrap>"+ x.get("CHANNEL") + "</td>");
								pesanNPWgagalRec10.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanNPWgagalRec10.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanNPWgagalRec10.append("<td nowrap>"+ x.get("PRODUK_UTAMA") + "</td>");
								pesanNPWgagalRec10.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanNPWgagalRec10.append("<td nowrap>"+ x.get("DATE_NPW") + "</td>");
								pesanNPWgagalRec10.append("<td nowrap>"+ x.get("TGL_INPUT") + "</td>");
								pesanNPWgagalRec10.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWgagalRec10.append("<td nowrap>"+ x.get("LSPD_POSITION") + "</td>");
								pesanNPWgagalRec10.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan45FRgagalRec10 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal4510 = "<br><br><b>Report Policy NPW - Gagal Recurring selama 45 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 0 && telat > 30  && telat <= 45 ){

								pesanFRgagalRec10.append("<tr><FONT SIZE=2><td nowrap>"+ no2_10++  + "</td>");
								pesanFRgagalRec10.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanFRgagalRec10.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanFRgagalRec10.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanFRgagalRec10.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanFRgagalRec10.append("<td nowrap>"+ x.get("PREMI") + "</td>");
								pesanFRgagalRec10.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
								pesanFRgagalRec10.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
								pesanFRgagalRec10.append("<td >"+ x.get("MSPS_DESC") + "</td></font></tr>");

								pesan30FRgagalRec10 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal3010 = "<br><br><b>Report SPAJ - Gagal Recurring selama 30 Hari</b><br>";
							}
							
						}else if(lsrg_id.equals("02")){
						//DMTM - BSIM
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 0 && telat > 45) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 45 HARI KERJA GAGAL RECURRING";

								pesanNPWgagalRec12.append("<tr><FONT SIZE=2><td nowrap>"+ no1_12++  + "</td>");
								pesanNPWgagalRec12.append("<td nowrap>"+ x.get("CHANNEL") + "</td>");
								pesanNPWgagalRec12.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanNPWgagalRec12.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanNPWgagalRec12.append("<td nowrap>"+ x.get("PRODUK_UTAMA") + "</td>");
								pesanNPWgagalRec12.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanNPWgagalRec12.append("<td nowrap>"+ x.get("DATE_NPW") + "</td>");
								pesanNPWgagalRec12.append("<td nowrap>"+ x.get("TGL_INPUT") + "</td>");
								pesanNPWgagalRec12.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWgagalRec12.append("<td nowrap>"+ x.get("LSPD_POSITION") + "</td>");
								pesanNPWgagalRec12.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan45FRgagalRec12 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal4512 = "<br><br><b>Report Policy NPW - Gagal Recurring selama 45 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 0 && telat > 30 && telat <= 45 ){

								pesanFRgagalRec12.append("<tr><FONT SIZE=2><td nowrap>"+ no2_12++  + "</td>");
								pesanFRgagalRec12.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanFRgagalRec12.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanFRgagalRec12.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanFRgagalRec12.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanFRgagalRec12.append("<td nowrap>"+ x.get("PREMI") + "</td>");
								pesanFRgagalRec12.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
								pesanFRgagalRec12.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
								pesanFRgagalRec12.append("<td >"+ x.get("MSPS_DESC") + "</td></font></tr>");

								pesan30FRgagalRec12 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal3012 = "<br><br><b>Report SPAJ - Gagal Recurring selama 30 Hari</b><br>";
							}											
						}
					}else if(lwk_id.equalsIgnoreCase("02")){
						if(lsrg_id.equals("00")){
						//DMTM - ARCO
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 0 && telat > 45) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 45 HARI KERJA GAGAL RECURRING";

								pesanNPWgagalRec20.append("<tr><FONT SIZE=2><td nowrap>"+ no1_20++  + "</td>");
								pesanNPWgagalRec20.append("<td nowrap>"+ x.get("CHANNEL") + "</td>");
								pesanNPWgagalRec20.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanNPWgagalRec20.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanNPWgagalRec20.append("<td nowrap>"+ x.get("PRODUK_UTAMA") + "</td>");
								pesanNPWgagalRec20.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanNPWgagalRec20.append("<td nowrap>"+ x.get("DATE_NPW") + "</td>");
								pesanNPWgagalRec20.append("<td nowrap>"+ x.get("TGL_INPUT") + "</td>");
								pesanNPWgagalRec20.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWgagalRec20.append("<td nowrap>"+ x.get("LSPD_POSITION") + "</td>");
								pesanNPWgagalRec20.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan45FRgagalRec20 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal4520 = "<br><br><b>Report Policy NPW - Gagal Recurring selama 45 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 0 && telat > 30 && telat <= 45 ){

								pesanFRgagalRec20.append("<tr><FONT SIZE=2><td nowrap>"+ no2_20++  + "</td>");
								pesanFRgagalRec20.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanFRgagalRec20.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanFRgagalRec20.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanFRgagalRec20.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanFRgagalRec20.append("<td nowrap>"+ x.get("PREMI") + "</td>");
								pesanFRgagalRec20.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
								pesanFRgagalRec20.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
								pesanFRgagalRec20.append("<td >"+ x.get("MSPS_DESC") + "</td></font></tr>");

								pesan30FRgagalRec20 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal3020 = "<br><br><b>Report SPAJ - Gagal Recurring selama 30 Hari</b><br>";
							}

						}else if(lsrg_id.equals("01")){
						//DMTM - SMP
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 0 && telat > 45) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 45 HARI KERJA GAGAL RECURRING";

								pesanNPWgagalRec21.append("<tr><FONT SIZE=2><td nowrap>"+ no1_21++  + "</td>");
								pesanNPWgagalRec21.append("<td nowrap>"+ x.get("CHANNEL") + "</td>");
								pesanNPWgagalRec21.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanNPWgagalRec21.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanNPWgagalRec21.append("<td nowrap>"+ x.get("PRODUK_UTAMA") + "</td>");
								pesanNPWgagalRec21.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanNPWgagalRec21.append("<td nowrap>"+ x.get("DATE_NPW") + "</td>");
								pesanNPWgagalRec21.append("<td nowrap>"+ x.get("TGL_INPUT") + "</td>");
								pesanNPWgagalRec21.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWgagalRec21.append("<td nowrap>"+ x.get("LSPD_POSITION") + "</td>");
								pesanNPWgagalRec21.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);
																		
								pesan45FRgagalRec21 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal4521 = "<br><br><b>Report Policy NPW - Gagal Recurring selama 45 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 0 && telat > 30 && telat <= 45 ){

								pesanFRgagalRec21.append("<tr><FONT SIZE=2><td nowrap>"+ no2_21++  + "</td>");
								pesanFRgagalRec21.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanFRgagalRec21.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanFRgagalRec21.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanFRgagalRec21.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanFRgagalRec21.append("<td nowrap>"+ x.get("PREMI") + "</td>");
								pesanFRgagalRec21.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
								pesanFRgagalRec21.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
								pesanFRgagalRec21.append("<td >"+ x.get("MSPS_DESC") + "</td></font></tr>");

								pesan30FRgagalRec21 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal3021 = "<br><br><b>Report SPAJ - Gagal Recurring selama 30 Hari</b><br>";
							}
							
						}else if(lsrg_id.equals("02")){
							//DMTM - SSS
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 0 && telat > 45) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 45 HARI KERJA GAGAL RECURRING";

								pesanNPWgagalRec22.append("<tr><FONT SIZE=2><td nowrap>"+ no1_22++  + "</td>");
								pesanNPWgagalRec22.append("<td nowrap>"+ x.get("CHANNEL") + "</td>");
								pesanNPWgagalRec22.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanNPWgagalRec22.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanNPWgagalRec22.append("<td nowrap>"+ x.get("PRODUK_UTAMA") + "</td>");
								pesanNPWgagalRec22.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanNPWgagalRec22.append("<td nowrap>"+ x.get("DATE_NPW") + "</td>");
								pesanNPWgagalRec22.append("<td nowrap>"+ x.get("TGL_INPUT") + "</td>");
								pesanNPWgagalRec22.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWgagalRec22.append("<td nowrap>"+ x.get("LSPD_POSITION") + "</td>");
								pesanNPWgagalRec22.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan45FRgagalRec22 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal4522 = "<br><br><b>Report Policy NPW - Gagal Recurring selama 45 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 0 && telat > 30 && telat <= 45 ){

								pesanFRgagalRec22.append("<tr><FONT SIZE=2><td nowrap>"+ no2_22++  + "</td>");
								pesanFRgagalRec22.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanFRgagalRec22.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanFRgagalRec22.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanFRgagalRec22.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanFRgagalRec22.append("<td nowrap>"+ x.get("PREMI") + "</td>");
								pesanFRgagalRec22.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
								pesanFRgagalRec22.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
								pesanFRgagalRec22.append("<td >"+ x.get("MSPS_DESC") + "</td></font></tr>");

								pesan30FRgagalRec22 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal3022 = "<br><br><b>Report SPAJ - Gagal Recurring selama 30 Hari</b><br>";
							}

						}else if(lsrg_id.equals("03")){
						//DMTM - SIP
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 0 && telat > 45) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 45 HARI KERJA GAGAL RECURRING";

								pesanNPWgagalRec23.append("<tr><FONT SIZE=2><td nowrap>"+ no1_23++  + "</td>");
								pesanNPWgagalRec23.append("<td nowrap>"+ x.get("CHANNEL") + "</td>");
								pesanNPWgagalRec23.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanNPWgagalRec23.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanNPWgagalRec23.append("<td nowrap>"+ x.get("PRODUK_UTAMA") + "</td>");
								pesanNPWgagalRec23.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanNPWgagalRec23.append("<td nowrap>"+ x.get("DATE_NPW") + "</td>");
								pesanNPWgagalRec23.append("<td nowrap>"+ x.get("TGL_INPUT") + "</td>");
								pesanNPWgagalRec23.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWgagalRec23.append("<td nowrap>"+ x.get("LSPD_POSITION") + "</td>");
								pesanNPWgagalRec23.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan45FRgagalRec23 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal4523 = "<br><br><b>Report Policy NPW - Gagal Recurring selama 45 Hari</b><br>";
							}
							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 0 && telat > 30 && telat <= 45 ){

								pesanFRgagalRec23.append("<tr><FONT SIZE=2><td nowrap>"+ no2_23++  + "</td>");
								pesanFRgagalRec23.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanFRgagalRec23.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanFRgagalRec23.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanFRgagalRec23.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanFRgagalRec23.append("<td nowrap>"+ x.get("PREMI") + "</td>");
								pesanFRgagalRec23.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
								pesanFRgagalRec23.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
								pesanFRgagalRec23.append("<td >"+ x.get("MSPS_DESC") + "</td></font></tr>");

								pesan30FRgagalRec23 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal3023 = "<br><br><b>Report SPAJ - Gagal Recurring selama 30 Hari</b><br>";
							}

						}else if(lsrg_id.equals("04")){
						//DMTM - REDBERRY
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 0 && telat > 45) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 45 HARI KERJA GAGAL RECURRING";

								pesanNPWgagalRec24.append("<tr><FONT SIZE=2><td nowrap>"+ no1_24++  + "</td>");
								pesanNPWgagalRec24.append("<td nowrap>"+ x.get("CHANNEL") + "</td>");
								pesanNPWgagalRec24.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanNPWgagalRec24.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanNPWgagalRec24.append("<td nowrap>"+ x.get("PRODUK_UTAMA") + "</td>");
								pesanNPWgagalRec24.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanNPWgagalRec24.append("<td nowrap>"+ x.get("DATE_NPW") + "</td>");
								pesanNPWgagalRec24.append("<td nowrap>"+ x.get("TGL_INPUT") + "</td>");
								pesanNPWgagalRec24.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWgagalRec24.append("<td nowrap>"+ x.get("LSPD_POSITION") + "</td>");
								pesanNPWgagalRec24.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan45FRgagalRec24 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal4524 = "<br><br><b>Report Policy NPW - Gagal Recurring selama 45 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 0 && telat > 30 && telat <= 45 ){

								pesanFRgagalRec24.append("<tr><FONT SIZE=2><td nowrap>"+ no2_24++  + "</td>");
								pesanFRgagalRec24.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanFRgagalRec24.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanFRgagalRec24.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanFRgagalRec24.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanFRgagalRec24.append("<td nowrap>"+ x.get("PREMI") + "</td>");
								pesanFRgagalRec24.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
								pesanFRgagalRec24.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
								pesanFRgagalRec24.append("<td >"+ x.get("MSPS_DESC") + "</td></font></tr>");

								pesan30FRgagalRec24 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal3024 = "<br><br><b>Report SPAJ - Gagal Recurring selama 30 Hari</b><br>";
							}

						}else if(lsrg_id.equals("05")){
						//DMTM - PSJS
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 0 && telat > 45) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 45 HARI KERJA GAGAL RECURRING";

								pesanNPWgagalRec25.append("<tr><FONT SIZE=2><td nowrap>"+ no1_25++  + "</td>");
								pesanNPWgagalRec25.append("<td nowrap>"+ x.get("CHANNEL") + "</td>");
								pesanNPWgagalRec25.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanNPWgagalRec25.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanNPWgagalRec25.append("<td nowrap>"+ x.get("PRODUK_UTAMA") + "</td>");
								pesanNPWgagalRec25.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanNPWgagalRec25.append("<td nowrap>"+ x.get("DATE_NPW") + "</td>");
								pesanNPWgagalRec25.append("<td nowrap>"+ x.get("TGL_INPUT") + "</td>");
								pesanNPWgagalRec25.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWgagalRec25.append("<td nowrap>"+ x.get("LSPD_POSITION") + "</td>");
								pesanNPWgagalRec25.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan45FRgagalRec25 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal4525 = "<br><br><b>Report Policy NPW - Gagal Recurring selama 45 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 0 && telat > 30 && telat <= 45 ){

								pesanFRgagalRec25.append("<tr><FONT SIZE=2><td nowrap>"+ no2_25++  + "</td>");
								pesanFRgagalRec25.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanFRgagalRec25.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanFRgagalRec25.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanFRgagalRec25.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanFRgagalRec25.append("<td nowrap>"+ x.get("PREMI") + "</td>");
								pesanFRgagalRec25.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
								pesanFRgagalRec25.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
								pesanFRgagalRec25.append("<td >"+ x.get("MSPS_DESC") + "</td></font></tr>");

								pesan30FRgagalRec25 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal3025 = "<br><br><b>Report SPAJ - Gagal Recurring selama 30 Hari</b><br>";
							}

						}else if(lsrg_id.equals("06")){
						//DMTM - SAPTA SARANA SEJAHTERA
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 0 && telat > 45) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 45 HARI KERJA GAGAL RECURRING";

								pesanNPWgagalRec26.append("<tr><FONT SIZE=2><td nowrap>"+ no1_26++  + "</td>");
								pesanNPWgagalRec26.append("<td nowrap>"+ x.get("CHANNEL") + "</td>");
								pesanNPWgagalRec26.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanNPWgagalRec26.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanNPWgagalRec26.append("<td nowrap>"+ x.get("PRODUK_UTAMA") + "</td>");
								pesanNPWgagalRec26.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanNPWgagalRec26.append("<td nowrap>"+ x.get("DATE_NPW") + "</td>");
								pesanNPWgagalRec26.append("<td nowrap>"+ x.get("TGL_INPUT") + "</td>");
								pesanNPWgagalRec26.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWgagalRec26.append("<td nowrap>"+ x.get("LSPD_POSITION") + "</td>");
								pesanNPWgagalRec26.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan45FRgagalRec26 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal4526 = "<br><br><b>Report Policy NPW - Gagal Recurring selama 45 Hari</b><br>";
							}
							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 0 && telat > 30 && telat <= 45 ){

								pesanFRgagalRec26.append("<tr><FONT SIZE=2><td nowrap>"+ no2_26++  + "</td>");
								pesanFRgagalRec26.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanFRgagalRec26.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanFRgagalRec26.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanFRgagalRec26.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanFRgagalRec26.append("<td nowrap>"+ x.get("PREMI") + "</td>");
								pesanFRgagalRec26.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
								pesanFRgagalRec26.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
								pesanFRgagalRec26.append("<td >"+ x.get("MSPS_DESC") + "</td></font></tr>");

								pesan30FRgagalRec26 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal3026 = "<br><br><b>Report SPAJ - Gagal Recurring selama 30 Hari</b><br>";
							}

						}else if(lsrg_id.equals("07")){
						//DMTM - DENA AKHYARO NUSANTARA
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 0 && telat > 45) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 45 HARI KERJA GAGAL RECURRING";

								pesanNPWgagalRec27.append("<tr><FONT SIZE=2><td nowrap>"+ no1_27++  + "</td>");
								pesanNPWgagalRec27.append("<td nowrap>"+ x.get("CHANNEL") + "</td>");
								pesanNPWgagalRec27.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanNPWgagalRec27.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanNPWgagalRec27.append("<td nowrap>"+ x.get("PRODUK_UTAMA") + "</td>");
								pesanNPWgagalRec27.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanNPWgagalRec27.append("<td nowrap>"+ x.get("DATE_NPW") + "</td>");
								pesanNPWgagalRec27.append("<td nowrap>"+ x.get("TGL_INPUT") + "</td>");
								pesanNPWgagalRec27.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWgagalRec27.append("<td nowrap>"+ x.get("LSPD_POSITION") + "</td>");
								pesanNPWgagalRec27.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan45FRgagalRec27 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal4527 = "<br><br><b>Report Policy NPW - Gagal Recurring selama 45 Hari</b><br>";
							}
							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 0 && telat > 30 && telat <= 45 ){

								pesanFRgagalRec27.append("<tr><FONT SIZE=2><td nowrap>"+ no2_26++  + "</td>");
								pesanFRgagalRec27.append("<td nowrap>"+ x.get("SITE") + "</td>");
								pesanFRgagalRec27.append("<td nowrap>"+ x.get("PEMEGANG_POLIS") + "</td>");
								pesanFRgagalRec27.append("<td nowrap>"+ x.get("REG_SPAJ") + "</td>");
								pesanFRgagalRec27.append("<td nowrap>"+ x.get("NAMA_PRODUK") + "</td>");
								pesanFRgagalRec27.append("<td nowrap>"+ x.get("PREMI") + "</td>");
								pesanFRgagalRec27.append("<td nowrap>"+ x.get("TGL_FURTHER") + "</td>");
								pesanFRgagalRec27.append("<td nowrap>"+ x.get("DATE_LINE") + "</td>");
								pesanFRgagalRec27.append("<td >"+ x.get("MSPS_DESC") + "</td></font></tr>");

								pesan30FRgagalRec27 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanGagal3027 = "<br><br><b>Report SPAJ - Gagal Recurring selama 30 Hari</b><br>";
							}
						}
					}//end if LWK = 2 data

				} //end if LCA = 40 data

			}// end For : Data

			// untuk proses gagal recuring	- END 

			////////////////////////////////////////////////////////////////////////////////////////////////

			// untuk process sukses recurring - START

			for(int i=0; i<data2.size();i++){
				Map y = data2.get(i);
				Integer telat =((BigDecimal) y.get("HARI_TELAT")).intValue();
				Integer status = ((BigDecimal) y.get("STAT_RECUR")).intValue();
				String spaj = y.get("REG_SPAJ").toString();
				String lca_id = y.get("LCA_ID").toString();
				String lwk_id = y.get("LWK_ID").toString();
				String lsrg_id = y.get("LSRG_ID").toString();


				if(lca_id.equalsIgnoreCase("40")){
					if(lwk_id.equalsIgnoreCase("01")){
						if(lsrg_id.equals("00")){//Roxy
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 2 && telat > 30) {
							//DMTM - General
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 30 HARI KERJA SUKSES RECURRING";

								pesanNPWsuksesRec10.append("<tr><FONT SIZE=2><td nowrap>"+ no3_10++  + "</td>");
								pesanNPWsuksesRec10.append("<td nowrap>"+ y.get("CHANNEL") + "</td>");
								pesanNPWsuksesRec10.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanNPWsuksesRec10.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanNPWsuksesRec10.append("<td nowrap>"+ y.get("PRODUK_UTAMA") + "</td>");
								pesanNPWsuksesRec10.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanNPWsuksesRec10.append("<td nowrap>"+ y.get("DATE_NPW") + "</td>");
								pesanNPWsuksesRec10.append("<td nowrap>"+ y.get("TGL_INPUT") + "</td>");
								pesanNPWsuksesRec10.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWsuksesRec10.append("<td nowrap>"+ y.get("LSPD_POSITION") + "</td>");
								pesanNPWsuksesRec10.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);
															
								pesan30FRsuksesRec10 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses3010 = "<br><br><b>Report Policy NPW - Sukses Recurring selama 30 Hari</b><br>";
							}
							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 2 && telat > 20 && telat <= 30 ){

								pesanFRsuksesRec10.append("<tr><FONT SIZE=2><td nowrap>"+ no4_10++  + "</td>");
								pesanFRsuksesRec10.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanFRsuksesRec10.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanFRsuksesRec10.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanFRsuksesRec10.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanFRsuksesRec10.append("<td nowrap>"+ y.get("PREMI") + "</td>");
								pesanFRsuksesRec10.append("<td nowrap>"+ y.get("TGL_FURTHER") + "</td>");
								pesanFRsuksesRec10.append("<td nowrap>"+ y.get("DATE_LINE") + "</td>");
								pesanFRsuksesRec10.append("<td >"+ y.get("MSPS_DESC") + "</td></font></tr>");

								pesan20FRsuksesRec10 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses2010 = "<br><br><b>Report SPAJ - Sukses Recurring selama 20 Hari</b><br>";
							}
							
						}else if(lsrg_id.equals("02")){
						//DMTM - BSIM
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 2 && telat > 30) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 30 HARI KERJA SUKSES RECURRING";

								pesanNPWsuksesRec12.append("<tr><FONT SIZE=2><td nowrap>"+ no3_12++  + "</td>");
								pesanNPWsuksesRec12.append("<td nowrap>"+ y.get("CHANNEL") + "</td>");
								pesanNPWsuksesRec12.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanNPWsuksesRec12.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanNPWsuksesRec12.append("<td nowrap>"+ y.get("PRODUK_UTAMA") + "</td>");
								pesanNPWsuksesRec12.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanNPWsuksesRec12.append("<td nowrap>"+ y.get("DATE_NPW") + "</td>");
								pesanNPWsuksesRec12.append("<td nowrap>"+ y.get("TGL_INPUT") + "</td>");
								pesanNPWsuksesRec12.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWsuksesRec12.append("<td nowrap>"+ y.get("LSPD_POSITION") + "</td>");
								pesanNPWsuksesRec12.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan30FRsuksesRec12 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses3012 = "<br><br><b>Report Policy NPW - Sukses Recurring selama 30 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 2 && telat > 20 && telat <= 30 ){

								pesanFRsuksesRec12.append("<tr><FONT SIZE=2><td nowrap>"+ no4_12++  + "</td>");
								pesanFRsuksesRec12.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanFRsuksesRec12.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanFRsuksesRec12.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanFRsuksesRec12.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanFRsuksesRec12.append("<td nowrap>"+ y.get("PREMI") + "</td>");
								pesanFRsuksesRec12.append("<td nowrap>"+ y.get("TGL_FURTHER") + "</td>");
								pesanFRsuksesRec12.append("<td nowrap>"+ y.get("DATE_LINE") + "</td>");
								pesanFRsuksesRec12.append("<td >"+ y.get("MSPS_DESC") + "</td></font></tr>");

								pesan20FRsuksesRec12 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses2012 = "<br><br><b>Report SPAJ - Sukses Recurring selama 20 Hari</b><br>";
							}
							
						}
					}else if(lwk_id.equalsIgnoreCase("02")){
						if(lsrg_id.equals("00")){
						//DMTM - ARCO
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 2 && telat > 30) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 30 HARI KERJA SUKSES RECURRING";

								pesanNPWsuksesRec20.append("<tr><FONT SIZE=2><td nowrap>"+ no3_20++  + "</td>");
								pesanNPWsuksesRec20.append("<td nowrap>"+ y.get("CHANNEL") + "</td>");
								pesanNPWsuksesRec20.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanNPWsuksesRec20.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanNPWsuksesRec20.append("<td nowrap>"+ y.get("PRODUK_UTAMA") + "</td>");
								pesanNPWsuksesRec20.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanNPWsuksesRec20.append("<td nowrap>"+ y.get("DATE_NPW") + "</td>");
								pesanNPWsuksesRec20.append("<td nowrap>"+ y.get("TGL_INPUT") + "</td>");
								pesanNPWsuksesRec20.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWsuksesRec20.append("<td nowrap>"+ y.get("LSPD_POSITION") + "</td>");
								pesanNPWsuksesRec20.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan30FRsuksesRec20 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses3020 = "<br><br><b>Report Policy NPW - Sukses Recurring selama 30 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 2 && telat > 20 && telat <= 30 ){

								pesanFRsuksesRec20.append("<tr><FONT SIZE=2><td nowrap>"+ no4_20++  + "</td>");
								pesanFRsuksesRec20.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanFRsuksesRec20.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanFRsuksesRec20.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanFRsuksesRec20.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanFRsuksesRec20.append("<td nowrap>"+ y.get("PREMI") + "</td>");
								pesanFRsuksesRec20.append("<td nowrap>"+ y.get("TGL_FURTHER") + "</td>");
								pesanFRsuksesRec20.append("<td nowrap>"+ y.get("DATE_LINE") + "</td>");
								pesanFRsuksesRec20.append("<td >"+ y.get("MSPS_DESC") + "</td></font></tr>");

								pesan20FRsuksesRec20 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses2020 = "<br><br><b>Report SPAJ - Sukses Recurring selama 20 Hari</b><br>";
							}
							
						}else if(lsrg_id.equals("01")){
						//DMTM - SMP
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 2 && telat > 30) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 30 HARI KERJA SUKSES RECURRING";

								pesanNPWsuksesRec21.append("<tr><FONT SIZE=2><td nowrap>"+ no3_21++  + "</td>");
								pesanNPWsuksesRec21.append("<td nowrap>"+ y.get("CHANNEL") + "</td>");
								pesanNPWsuksesRec21.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanNPWsuksesRec21.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanNPWsuksesRec21.append("<td nowrap>"+ y.get("PRODUK_UTAMA") + "</td>");
								pesanNPWsuksesRec21.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanNPWsuksesRec21.append("<td nowrap>"+ y.get("DATE_NPW") + "</td>");
								pesanNPWsuksesRec21.append("<td nowrap>"+ y.get("TGL_INPUT") + "</td>");
								pesanNPWsuksesRec21.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWsuksesRec21.append("<td nowrap>"+ y.get("LSPD_POSITION") + "</td>");
								pesanNPWsuksesRec21.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan30FRsuksesRec21 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses3021 = "<br><br><b>Report Policy NPW - Sukses Recurring selama 30 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 2 && telat > 20 && telat <= 30 ){

								pesanFRsuksesRec21.append("<tr><FONT SIZE=2><td nowrap>"+ no4_21++  + "</td>");
								pesanFRsuksesRec21.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanFRsuksesRec21.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanFRsuksesRec21.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanFRsuksesRec21.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanFRsuksesRec21.append("<td nowrap>"+ y.get("PREMI") + "</td>");
								pesanFRsuksesRec21.append("<td nowrap>"+ y.get("TGL_FURTHER") + "</td>");
								pesanFRsuksesRec21.append("<td nowrap>"+ y.get("DATE_LINE") + "</td>");
								pesanFRsuksesRec21.append("<td >"+ y.get("MSPS_DESC") + "</td></font></tr>");

								pesan20FRsuksesRec21 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses2021 = "<br><br><b>Report SPAJ - Sukses Recurring selama 20 Hari</b><br>";
							}
							
						}else if(lsrg_id.equals("02")){
						//DMTM - SSS
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 2 && telat > 30) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 30 HARI KERJA SUKSES RECURRING";

								pesanNPWsuksesRec22.append("<tr><FONT SIZE=2><td nowrap>"+ no3_22++  + "</td>");
								pesanNPWsuksesRec22.append("<td nowrap>"+ y.get("CHANNEL") + "</td>");
								pesanNPWsuksesRec22.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanNPWsuksesRec22.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanNPWsuksesRec22.append("<td nowrap>"+ y.get("PRODUK_UTAMA") + "</td>");
								pesanNPWsuksesRec22.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanNPWsuksesRec22.append("<td nowrap>"+ y.get("DATE_NPW") + "</td>");
								pesanNPWsuksesRec22.append("<td nowrap>"+ y.get("TGL_INPUT") + "</td>");
								pesanNPWsuksesRec22.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWsuksesRec22.append("<td nowrap>"+ y.get("LSPD_POSITION") + "</td>");
								pesanNPWsuksesRec22.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan30FRsuksesRec22 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses3022 = "<br><br><b>Report Policy NPW - Sukses Recurring selama 30 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 2 && telat > 20 && telat <= 30 ){

								pesanFRsuksesRec22.append("<tr><FONT SIZE=2><td nowrap>"+ no4_22++  + "</td>");
								pesanFRsuksesRec22.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanFRsuksesRec22.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanFRsuksesRec22.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanFRsuksesRec22.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanFRsuksesRec22.append("<td nowrap>"+ y.get("PREMI") + "</td>");
								pesanFRsuksesRec22.append("<td nowrap>"+ y.get("TGL_FURTHER") + "</td>");
								pesanFRsuksesRec22.append("<td nowrap>"+ y.get("DATE_LINE") + "</td>");
								pesanFRsuksesRec22.append("<td >"+ y.get("MSPS_DESC") + "</td></font></tr>");

								pesan20FRsuksesRec22 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses2022 = "<br><br><b>Report SPAJ - Sukses Recurring selama 20 Hari</b><br>";
							}
							
						}else if(lsrg_id.equals("03")){
						//DMTM - SIP
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 2 && telat > 30) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 30 HARI KERJA SUKSES RECURRING";

								pesanNPWsuksesRec23.append("<tr><FONT SIZE=2><td nowrap>"+ no3_23++  + "</td>");
								pesanNPWsuksesRec23.append("<td nowrap>"+ y.get("CHANNEL") + "</td>");
								pesanNPWsuksesRec23.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanNPWsuksesRec23.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanNPWsuksesRec23.append("<td nowrap>"+ y.get("PRODUK_UTAMA") + "</td>");
								pesanNPWsuksesRec23.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanNPWsuksesRec23.append("<td nowrap>"+ y.get("DATE_NPW") + "</td>");
								pesanNPWsuksesRec23.append("<td nowrap>"+ y.get("TGL_INPUT") + "</td>");
								pesanNPWsuksesRec23.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWsuksesRec23.append("<td nowrap>"+ y.get("LSPD_POSITION") + "</td>");
								pesanNPWsuksesRec23.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan30FRsuksesRec23 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses3023 = "<br><br><b>Report Policy NPW - Sukses Recurring selama 30 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 2 && telat > 20 && telat <= 30 ){

								pesanFRsuksesRec23.append("<tr><FONT SIZE=2><td nowrap>"+ no4_23++  + "</td>");
								pesanFRsuksesRec23.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanFRsuksesRec23.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanFRsuksesRec23.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanFRsuksesRec23.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanFRsuksesRec23.append("<td nowrap>"+ y.get("PREMI") + "</td>");
								pesanFRsuksesRec23.append("<td nowrap>"+ y.get("TGL_FURTHER") + "</td>");
								pesanFRsuksesRec23.append("<td nowrap>"+ y.get("DATE_LINE") + "</td>");
								pesanFRsuksesRec23.append("<td >"+ y.get("MSPS_DESC") + "</td></font></tr>");

								pesan20FRsuksesRec23 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses2023 = "<br><br><b>Report SPAJ - Sukses Recurring selama 20 Hari</b><br>";
							}
							
						}else if(lsrg_id.equals("04")){
						//DMTM - REDBERRY
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 2 && telat > 30) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 30 HARI KERJA SUKSES RECURRING";

								pesanNPWsuksesRec24.append("<tr><FONT SIZE=2><td nowrap>"+ no3_24++  + "</td>");
								pesanNPWsuksesRec24.append("<td nowrap>"+ y.get("CHANNEL") + "</td>");
								pesanNPWsuksesRec24.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanNPWsuksesRec24.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanNPWsuksesRec24.append("<td nowrap>"+ y.get("PRODUK_UTAMA") + "</td>");
								pesanNPWsuksesRec24.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanNPWsuksesRec24.append("<td nowrap>"+ y.get("DATE_NPW") + "</td>");
								pesanNPWsuksesRec24.append("<td nowrap>"+ y.get("TGL_INPUT") + "</td>");
								pesanNPWsuksesRec24.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWsuksesRec24.append("<td nowrap>"+ y.get("LSPD_POSITION") + "</td>");
								pesanNPWsuksesRec24.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan30FRsuksesRec24 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses3024 = "<br><br><b>Report Policy NPW - Sukses Recurring selama 30 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 2 && telat > 20 && telat <= 30 ){

								pesanFRsuksesRec24.append("<tr><FONT SIZE=2><td nowrap>"+ no4_24++  + "</td>");
								pesanFRsuksesRec24.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanFRsuksesRec24.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanFRsuksesRec24.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanFRsuksesRec24.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanFRsuksesRec24.append("<td nowrap>"+ y.get("PREMI") + "</td>");
								pesanFRsuksesRec24.append("<td nowrap>"+ y.get("TGL_FURTHER") + "</td>");
								pesanFRsuksesRec24.append("<td nowrap>"+ y.get("DATE_LINE") + "</td>");
								pesanFRsuksesRec24.append("<td >"+ y.get("MSPS_DESC") + "</td></font></tr>");

								pesan20FRsuksesRec24 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses2024 = "<br><br><b>Report SPAJ - Sukses Recurring selama 20 Hari</b><br>";
							}
							
						}else if(lsrg_id.equals("05")){
						//DMTM - PSJS
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 2 && telat > 30) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 30 HARI KERJA SUKSES RECURRING";

								pesanNPWsuksesRec25.append("<tr><FONT SIZE=2><td nowrap>"+ no3_25++  + "</td>");
								pesanNPWsuksesRec25.append("<td nowrap>"+ y.get("CHANNEL") + "</td>");
								pesanNPWsuksesRec25.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanNPWsuksesRec25.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanNPWsuksesRec25.append("<td nowrap>"+ y.get("PRODUK_UTAMA") + "</td>");
								pesanNPWsuksesRec25.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanNPWsuksesRec25.append("<td nowrap>"+ y.get("DATE_NPW") + "</td>");
								pesanNPWsuksesRec25.append("<td nowrap>"+ y.get("TGL_INPUT") + "</td>");
								pesanNPWsuksesRec25.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWsuksesRec25.append("<td nowrap>"+ y.get("LSPD_POSITION") + "</td>");
								pesanNPWsuksesRec25.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan30FRsuksesRec25 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses3025 = "<br><br><b>Report Policy NPW - Sukses Recurring selama 30 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 2 && telat > 20 && telat <= 30 ){

								pesanFRsuksesRec25.append("<tr><FONT SIZE=2><td nowrap>"+ no4_25++  + "</td>");
								pesanFRsuksesRec25.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanFRsuksesRec25.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanFRsuksesRec25.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanFRsuksesRec25.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanFRsuksesRec25.append("<td nowrap>"+ y.get("PREMI") + "</td>");
								pesanFRsuksesRec25.append("<td nowrap>"+ y.get("TGL_FURTHER") + "</td>");
								pesanFRsuksesRec25.append("<td nowrap>"+ y.get("DATE_LINE") + "</td>");
								pesanFRsuksesRec25.append("<td >"+ y.get("MSPS_DESC") + "</td></font></tr>");

								pesan20FRsuksesRec25 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses2025 = "<br><br><b>Report SPAJ - Sukses Recurring selama 20 Hari</b><br>";
							}
							
						}else if(lsrg_id.equals("06")){
						//DMTM -  SAPTA SARANA SEJAHTERA
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 2 && telat > 30) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 30 HARI KERJA SUKSES RECURRING";

								pesanNPWsuksesRec26.append("<tr><FONT SIZE=2><td nowrap>"+ no3_26++  + "</td>");
								pesanNPWsuksesRec26.append("<td nowrap>"+ y.get("CHANNEL") + "</td>");
								pesanNPWsuksesRec26.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanNPWsuksesRec26.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanNPWsuksesRec26.append("<td nowrap>"+ y.get("PRODUK_UTAMA") + "</td>");
								pesanNPWsuksesRec26.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanNPWsuksesRec26.append("<td nowrap>"+ y.get("DATE_NPW") + "</td>");
								pesanNPWsuksesRec26.append("<td nowrap>"+ y.get("TGL_INPUT") + "</td>");
								pesanNPWsuksesRec26.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWsuksesRec26.append("<td nowrap>"+ y.get("LSPD_POSITION") + "</td>");
								pesanNPWsuksesRec26.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan30FRsuksesRec26 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses3026 = "<br><br><b>Report Policy NPW - Sukses Recurring selama 30 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 2 && telat > 20 && telat <= 30 ){

								pesanFRsuksesRec26.append("<tr><FONT SIZE=2><td nowrap>"+ no4_26++  + "</td>");
								pesanFRsuksesRec26.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanFRsuksesRec26.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanFRsuksesRec26.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanFRsuksesRec26.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanFRsuksesRec26.append("<td nowrap>"+ y.get("PREMI") + "</td>");
								pesanFRsuksesRec26.append("<td nowrap>"+ y.get("TGL_FURTHER") + "</td>");
								pesanFRsuksesRec26.append("<td nowrap>"+ y.get("DATE_LINE") + "</td>");
								pesanFRsuksesRec26.append("<td >"+ y.get("MSPS_DESC") + "</td></font></tr>");

								pesan20FRsuksesRec26 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses2026 = "<br><br><b>Report SPAJ - Sukses Recurring selama 20 Hari</b><br>";
							}
							
						}else if(lsrg_id.equals("07")){
						//DMTM -  DENA AKHYARO NUSANTARA
							// merubah status spaj menjadi NPW setelah SPAJ gagal recur selama 45 hari (kalender) ---- (*RDL)
							if(status == 2 && telat > 30) {
								String ket = "AUTOPROSES UPDATE POSISI (FR) KE NOT PROCEED WITH POLIS-POLIS DMTM > 30 HARI KERJA SUKSES RECURRING";

								pesanNPWsuksesRec27.append("<tr><FONT SIZE=2><td nowrap>"+ no3_27++  + "</td>");
								pesanNPWsuksesRec27.append("<td nowrap>"+ y.get("CHANNEL") + "</td>");
								pesanNPWsuksesRec27.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanNPWsuksesRec27.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanNPWsuksesRec27.append("<td nowrap>"+ y.get("PRODUK_UTAMA") + "</td>");
								pesanNPWsuksesRec27.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanNPWsuksesRec27.append("<td nowrap>"+ y.get("DATE_NPW") + "</td>");
								pesanNPWsuksesRec27.append("<td nowrap>"+ y.get("TGL_INPUT") + "</td>");
								pesanNPWsuksesRec27.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanNPWsuksesRec27.append("<td nowrap>"+ y.get("LSPD_POSITION") + "</td>");
								pesanNPWsuksesRec27.append("<td >"+ket+"</td></font></tr>");

								uwDao.updateMstPolicy(spaj, 999);
								uwDao.updateMstPolicyLsspdId(spaj, 29);
								uwDao.insertMstPositionSpaj("0", ket, spaj, 0);
								uwDao.prosesSnows(spaj, "0", 999, 212);

								pesan30FRsuksesRec27 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses3027 = "<br><br><b>Report Policy NPW - Sukses Recurring selama 30 Hari</b><br>";
							}

							//melakukan pengecekan dan pencatatan data apabila ada SPAJ yang gagal recur selama 30 hari (kalender) untuk dikirimkan isi email (pesan30) ke user dengan status lspd_id not in (999 "NPW", 95"Cancel From Begining") ---- (*RDL)
							if(status == 2 && telat > 20 && telat <= 30 ){

								pesanFRsuksesRec27.append("<tr><FONT SIZE=2><td nowrap>"+ no4_27++  + "</td>");
								pesanFRsuksesRec27.append("<td nowrap>"+ y.get("SITE") + "</td>");
								pesanFRsuksesRec27.append("<td nowrap>"+ y.get("PEMEGANG_POLIS") + "</td>");
								pesanFRsuksesRec27.append("<td nowrap>"+ y.get("REG_SPAJ") + "</td>");
								pesanFRsuksesRec27.append("<td nowrap>"+ y.get("NAMA_PRODUK") + "</td>");
								pesanFRsuksesRec27.append("<td nowrap>"+ y.get("PREMI") + "</td>");
								pesanFRsuksesRec27.append("<td nowrap>"+ y.get("TGL_FURTHER") + "</td>");
								pesanFRsuksesRec27.append("<td nowrap>"+ y.get("DATE_LINE") + "</td>");
								pesanFRsuksesRec27.append("<td >"+ y.get("MSPS_DESC") + "</td></font></tr>");

								pesan20FRsuksesRec27 = true; //untuk mengetahui ada data yang tercatat atau tidak ---- (*RDL)
								pesanSukses2027 = "<br><br><b>Report SPAJ - Sukses Recurring selama 20 Hari</b><br>";
							}
						}
					}//end if LWK = 2 Data2
				}//end if LCA = 40 Data2
			} //end FOR Each Data2
			///////////////////////////////////////////////////////////////////////////////////////////////

			pesanFooterT.append("</table> <br><br>");
			pesanFooter.append("<b>Terima Kasih</b><br><br>");
			pesanFooter.append("<i>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.</i><br><br>");

			//Proses pengiriman email -- START ------------------------------------------------------------------------					
			//DMTM
			// kirim email khusus DMTM gagal recuring selama 30 hari dari tgl Further/ 20 hari sukses recur ---- (*RDL)
			if(pesan30FRgagalRec10 || pesan20FRsuksesRec10 ){

				try {
					if(pesan30FRgagalRec10 && pesan20FRsuksesRec10 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3010+pesanHeaderT.toString()+pesanFRgagalRec10.toString()+pesanFooterT.toString()
								+pesanSukses2010+pesanHeaderT.toString()+pesanFRsuksesRec10.toString()+pesanFooterT.toString()+pesanFooter.toString();
				
					}else if(pesan30FRgagalRec10 && !pesan20FRsuksesRec10 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3010+pesanHeaderT.toString()+pesanFRgagalRec10.toString()+pesanFooterT.toString()+pesanFooter.toString();					
												
					}else if(!pesan30FRgagalRec10 && pesan20FRsuksesRec10 )
					{
						pesanFull = pesanHeader.toString()+pesanSukses2010+pesanHeaderT.toString()+pesanFRsuksesRec10.toString()+pesanFooterT.toString()+pesanFooter.toString();				
					}
					
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to10, cc10, bcc,"WARNING !! Follow up SPAJ DMTM yang akan kadarluasa dan proses refund", pesanFull,	null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}

			// kirim email NPW khusus DMTM gagal recuring selama 45 hari dari tgl Further/ 30 hari sukses recur ---- (*RDL)
			if(pesan45FRgagalRec10 || pesan30FRsuksesRec10){

				try {
					if(pesan45FRgagalRec10 && pesan30FRsuksesRec10)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4510+pesanHeaderT2.toString()+pesanNPWgagalRec10.toString()+pesanFooterT.toString()
								+pesanSukses3010+pesanHeaderT2.toString()+pesanNPWsuksesRec10.toString()+pesanFooterT.toString()+pesanFooter.toString();
												
					}else if(pesan45FRgagalRec10 && !pesan30FRsuksesRec10)
					{
						pesanFull =	pesanHeader2.toString()+pesanGagal4510+pesanHeaderT2.toString()+pesanNPWgagalRec10.toString()+pesanFooterT.toString()+pesanFooter.toString();		
												
					}else if(!pesan45FRgagalRec10 && pesan30FRsuksesRec10)
					{
						pesanFull = pesanHeader2.toString()+pesanSukses3010+pesanHeaderT2.toString()+pesanNPWsuksesRec10.toString()+pesanFooterT.toString()+pesanFooter.toString();					
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to10, cc10, bcc,"SPAJ NPW DMTM",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}

			//BSIM
			// kirim email khusus DMTM gagal recuring selama 30 hari dari tgl Further/ 20 hari sukses recur ---- (*RDL)
			if(pesan30FRgagalRec12 || pesan20FRsuksesRec12 ){

				try {
					if(pesan30FRgagalRec12 && pesan20FRsuksesRec12 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3012+pesanHeaderT.toString()+pesanFRgagalRec12.toString()+pesanFooterT.toString()
								+pesanSukses2012+pesanHeaderT.toString()+pesanFRsuksesRec12.toString()+pesanFooterT.toString()+pesanFooter.toString();
						
						
					}else if(pesan30FRgagalRec12 && !pesan20FRsuksesRec12 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3012+pesanHeaderT.toString()+pesanFRgagalRec12.toString()+pesanFooterT.toString()+pesanFooter.toString();
												
					}else if(!pesan30FRgagalRec12 && pesan20FRsuksesRec12 )
					{
						pesanFull = pesanHeader.toString()+pesanSukses2012+pesanHeaderT.toString()+pesanFRsuksesRec12.toString()+pesanFooterT.toString()+pesanFooter.toString();						
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to12, cc12, bcc,
							"WARNING !! Follow up SPAJ DMTM yang akan kadarluasa dan proses refund",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}

			// kirim email NPW khusus DMTM gagal recuring selama 45 hari dari tgl Further/ 30 hari sukses recur ---- (*RDL)
			if(pesan45FRgagalRec12 || pesan30FRsuksesRec12){

				try {
					if(pesan45FRgagalRec12 && pesan30FRsuksesRec12)
					{
						pesanFull =	pesanHeader2.toString()+pesanGagal4512+pesanHeaderT2.toString()+pesanNPWgagalRec12.toString()+pesanFooterT.toString()
								+pesanSukses3012+pesanHeaderT2.toString()+pesanNPWsuksesRec12.toString()+pesanFooterT.toString()+pesanFooter.toString();				
												
					}else if(pesan45FRgagalRec12 && !pesan30FRsuksesRec12)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4512+pesanHeaderT2.toString()+pesanNPWgagalRec12.toString()+pesanFooterT.toString()+pesanFooter.toString();
												
					}else if(!pesan45FRgagalRec12 && pesan30FRsuksesRec12)
					{
						pesanFull = pesanHeader2.toString()+pesanSukses3012+pesanHeaderT2.toString()+pesanNPWsuksesRec12.toString()+pesanFooterT.toString()+pesanFooter.toString();
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to12, cc12, bcc,"SPAJ NPW DMTM",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}
			//ARCO
			// kirim email khusus DMTM gagal recuring selama 30 hari dari tgl Further/ 20 hari sukses recur ---- (*RDL)
			if(pesan30FRgagalRec20 || pesan20FRsuksesRec20 ){

				try {
					if(pesan30FRgagalRec20 && pesan20FRsuksesRec20 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3020+pesanHeaderT.toString()+pesanFRgagalRec20.toString()+pesanFooterT.toString()
								+pesanSukses2020+pesanHeaderT.toString()+pesanFRsuksesRec20.toString()+pesanFooterT.toString()+pesanFooter.toString();
					
					}else if(pesan30FRgagalRec20 && !pesan20FRsuksesRec20 )
					{
						pesanFull =pesanHeader.toString()+pesanGagal3020+pesanHeaderT.toString()+pesanFRgagalRec20.toString()+pesanFooterT.toString()+pesanFooter.toString();
												
					}else if(!pesan30FRgagalRec20 && pesan20FRsuksesRec20 )
					{
						pesanFull =	pesanHeader.toString()+pesanSukses2020+pesanHeaderT.toString()+pesanFRsuksesRec20.toString()+pesanFooterT.toString()+pesanFooter.toString();					
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to20, cc20, bcc,
							"WARNING !! Follow up SPAJ DMTM yang akan kadarluasa dan proses refund",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}

			// kirim email NPW khusus DMTM gagal recuring selama 45 hari dari tgl Further/ 30 hari sukses recur ---- (*RDL)
			if(pesan45FRgagalRec20 || pesan30FRsuksesRec20){

				try {
					if(pesan45FRgagalRec20 && pesan30FRsuksesRec20)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4520+pesanHeaderT2.toString()+pesanNPWgagalRec20.toString()+pesanFooterT.toString()
								+pesanSukses3020+pesanHeaderT2.toString()+pesanNPWsuksesRec20.toString()+pesanFooterT.toString()+pesanFooter.toString();
						
					}else if(pesan45FRgagalRec20 && !pesan30FRsuksesRec20)
					{
						pesanFull =	pesanHeader2.toString()+pesanGagal4520+pesanHeaderT2.toString()+pesanNPWgagalRec20.toString()+pesanFooterT.toString()+pesanFooter.toString();				
						
					}else if(!pesan45FRgagalRec20 && pesan30FRsuksesRec20)
					{
						pesanFull = pesanHeader2.toString()+pesanSukses3020+pesanHeaderT2.toString()+pesanNPWsuksesRec20.toString()+pesanFooterT.toString()+pesanFooter.toString();					
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to20, cc20, bcc,"SPAJ NPW DMTM",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}
			//SMP
			// kirim email khusus DMTM gagal recuring selama 30 hari dari tgl Further/ 20 hari sukses recur ---- (*RDL)
			if(pesan30FRgagalRec21 || pesan20FRsuksesRec21 ){

				try {
					if(pesan30FRgagalRec21 && pesan20FRsuksesRec21 )
					{
						pesanFull =	pesanHeader.toString()+pesanGagal3021+pesanHeaderT.toString()+pesanFRgagalRec21.toString()+pesanFooterT.toString()
								+pesanSukses2021+pesanHeaderT.toString()+pesanFRsuksesRec21.toString()+pesanFooterT.toString()+pesanFooter.toString();					
						
					}else if(pesan30FRgagalRec21 && !pesan20FRsuksesRec21 )
					{
						pesanFull =	pesanHeader.toString()+pesanGagal3021+pesanHeaderT.toString()+pesanFRgagalRec21.toString()+pesanFooterT.toString()+pesanFooter.toString();				
												
					}else if(!pesan30FRgagalRec21 && pesan20FRsuksesRec21 )
					{
						pesanFull = pesanHeader.toString()+pesanSukses2021+pesanHeaderT.toString()+pesanFRsuksesRec21.toString()+pesanFooterT.toString()+pesanFooter.toString();
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to21, cc21, bcc,"WARNING !! Follow up SPAJ DMTM yang akan kadarluasa dan proses refund",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}

			// kirim email NPW khusus DMTM gagal recuring selama 45 hari dari tgl Further/ 30 hari sukses recur ---- (*RDL)
			if(pesan45FRgagalRec21 || pesan30FRsuksesRec21){

				try {
					if(pesan45FRgagalRec21 && pesan30FRsuksesRec21)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4521+pesanHeaderT2.toString()+pesanNPWgagalRec21.toString()+pesanFooterT.toString()
								+pesanSukses3021+pesanHeaderT2.toString()+pesanNPWsuksesRec21.toString()+pesanFooterT.toString()+pesanFooter.toString();
						
					}else if(pesan45FRgagalRec21 && !pesan30FRsuksesRec21)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4521+pesanHeaderT2.toString()+pesanNPWgagalRec21.toString()+pesanFooterT.toString()+pesanFooter.toString();
						
					}else if(!pesan45FRgagalRec21 && pesan30FRsuksesRec21)
					{
						pesanFull = pesanHeader2.toString()+pesanSukses3021+pesanHeaderT2.toString()+pesanNPWsuksesRec21.toString()+pesanFooterT.toString()+pesanFooter.toString();
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to21, cc21, bcc,"SPAJ NPW DMTM",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}
			//SSS

			// kirim email khusus DMTM gagal recuring selama 30 hari dari tgl Further/ 20 hari sukses recur ---- (*RDL)
			if(pesan30FRgagalRec22 || pesan20FRsuksesRec22 ){

				try {
					if(pesan30FRgagalRec22 && pesan20FRsuksesRec22 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3022+pesanHeaderT.toString()+pesanFRgagalRec22.toString()+pesanFooterT.toString()
								+pesanSukses2022+pesanHeaderT.toString()+pesanFRsuksesRec22.toString()+pesanFooterT.toString()+pesanFooter.toString();											
						
					}else if(pesan30FRgagalRec22 && !pesan20FRsuksesRec22 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3022+pesanHeaderT.toString()+pesanFRgagalRec22.toString()+pesanFooterT.toString()+pesanFooter.toString();					
						
					}else if(!pesan30FRgagalRec22 && pesan20FRsuksesRec22 )
					{
						pesanFull = pesanHeader.toString()+pesanSukses2022+pesanHeaderT.toString()+pesanFRsuksesRec22.toString()+pesanFooterT.toString()+pesanFooter.toString();
					
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to22, cc22, bcc,"WARNING !! Follow up SPAJ DMTM yang akan kadarluasa dan proses refund",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}

			// kirim email NPW khusus DMTM gagal recuring selama 45 hari dari tgl Further/ 30 hari sukses recur ---- (*RDL)
			if(pesan45FRgagalRec22 || pesan30FRsuksesRec22){

				try {
					if(pesan45FRgagalRec22 && pesan30FRsuksesRec22)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4522+pesanHeaderT2.toString()+pesanNPWgagalRec22.toString()+pesanFooterT.toString()
								+pesanSukses3022+pesanHeaderT2.toString()+pesanNPWsuksesRec22.toString()+pesanFooterT.toString()+pesanFooter.toString();					
						
						
					}else if(pesan45FRgagalRec22 && !pesan30FRsuksesRec22)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4522+pesanHeaderT2.toString()+pesanNPWgagalRec22.toString()+pesanFooterT.toString()
								+pesanFooter.toString(); 					
												
					}else if(!pesan45FRgagalRec22 && pesan30FRsuksesRec22)
					{
						pesanFull = pesanHeader2.toString()+pesanSukses3022+pesanHeaderT2.toString()+pesanNPWsuksesRec22.toString()+pesanFooterT.toString()
								+pesanFooter.toString(); 					
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to22, cc22, bcc,"SPAJ NPW DMTM",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}
			//SIP

			// kirim email khusus DMTM gagal recuring selama 30 hari dari tgl Further/ 20 hari sukses recur ---- (*RDL)
			if(pesan30FRgagalRec23 || pesan20FRsuksesRec23 ){

				try {
					if(pesan30FRgagalRec23 && pesan20FRsuksesRec23 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3023+pesanHeaderT.toString()+pesanFRgagalRec23.toString()+pesanFooterT.toString()
								+pesanSukses2023+pesanHeaderT.toString()+pesanFRsuksesRec23.toString()+pesanFooterT.toString()+pesanFooter.toString();					
												
					}else if(pesan30FRgagalRec23 && !pesan20FRsuksesRec23 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3023+pesanHeaderT.toString()+pesanFRgagalRec23.toString()+pesanFooterT.toString()+pesanFooter.toString();
												
					}else if(!pesan30FRgagalRec23 && pesan20FRsuksesRec23 )
					{
						pesanFull = pesanHeader.toString()+pesanSukses2023+pesanHeaderT.toString()+pesanFRsuksesRec23.toString()+pesanFooterT.toString()+pesanFooter.toString();
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to23, cc23, bcc,"WARNING !! Follow up SPAJ DMTM yang akan kadarluasa dan proses refund",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}

			// kirim email NPW khusus DMTM gagal recuring selama 45 hari dari tgl Further/ 30 hari sukses recur ---- (*RDL)
			if(pesan45FRgagalRec23 || pesan30FRsuksesRec23){

				try {
					if(pesan45FRgagalRec23 && pesan30FRsuksesRec23)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4523+pesanHeaderT2.toString()+pesanNPWgagalRec23.toString()+pesanFooterT.toString()
								+pesanSukses3023+pesanHeaderT2.toString()+pesanNPWsuksesRec23.toString()+pesanFooterT.toString()+pesanFooter.toString();
												
					}else if(pesan45FRgagalRec23 && !pesan30FRsuksesRec23)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4523+pesanHeaderT2.toString()+pesanNPWgagalRec23.toString()+pesanFooterT.toString()+pesanFooter.toString();
												
					}else if(!pesan45FRgagalRec23 && pesan30FRsuksesRec23)
					{
						pesanFull = pesanHeader2.toString()+pesanSukses3023+pesanHeaderT2.toString()+pesanNPWsuksesRec23.toString()+pesanFooterT.toString()+pesanFooter.toString();
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to23, cc23, bcc,"SPAJ NPW DMTM",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}
			//REDBERRY

			// kirim email khusus DMTM gagal recuring selama 30 hari dari tgl Further/ 20 hari sukses recur ---- (*RDL)
			if(pesan30FRgagalRec24 || pesan20FRsuksesRec24 ){

				try {
					if(pesan30FRgagalRec24 && pesan20FRsuksesRec24 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3024+pesanHeaderT.toString()+pesanFRgagalRec24.toString()+pesanFooterT.toString()
								+pesanSukses2024+pesanHeaderT.toString()+pesanFRsuksesRec24.toString()+pesanFooterT.toString()+pesanFooter.toString();
												
					}else if(pesan30FRgagalRec24 && !pesan20FRsuksesRec24 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3024+pesanHeaderT.toString()+pesanFRgagalRec24.toString()+pesanFooterT.toString()+pesanFooter.toString(); 						
												
					}else if(!pesan30FRgagalRec24 && pesan20FRsuksesRec24 )
					{
						pesanFull = pesanHeader.toString()+pesanSukses2024+pesanHeaderT.toString()+pesanFRsuksesRec24.toString()+pesanFooterT.toString()+pesanFooter.toString(); 						
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to24, cc24, bcc,"WARNING !! Follow up SPAJ DMTM yang akan kadarluasa dan proses refund",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}

			// kirim email NPW khusus DMTM gagal recuring selama 45 hari dari tgl Further/ 30 hari sukses recur ---- (*RDL)
			if(pesan45FRgagalRec24 || pesan30FRsuksesRec24){

				try {
					if(pesan45FRgagalRec24 && pesan30FRsuksesRec24)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4524+pesanHeaderT2.toString()+pesanNPWgagalRec24.toString()+pesanFooterT.toString()
								+pesanSukses3024+pesanHeaderT2.toString()+pesanNPWsuksesRec24.toString()+pesanFooterT.toString()+pesanFooter.toString();						
						
						
					}else if(pesan45FRgagalRec24 && !pesan30FRsuksesRec24)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4524+pesanHeaderT2.toString()+pesanNPWgagalRec24.toString()+pesanFooterT.toString()+pesanFooter.toString();						
						
					}else if(!pesan45FRgagalRec24 && pesan30FRsuksesRec24)
					{
						pesanFull = pesanHeader2.toString()+pesanSukses3024+pesanHeaderT2.toString()+pesanNPWsuksesRec24.toString()+pesanFooterT.toString()+pesanFooter.toString();
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to24, cc24, bcc,"SPAJ NPW DMTM",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}
			//PSJS

			// kirim email khusus DMTM gagal recuring selama 30 hari dari tgl Further/ 20 hari sukses recur ---- (*RDL)
			if(pesan30FRgagalRec25 || pesan20FRsuksesRec25 ){

				try {
					if(pesan30FRgagalRec25 && pesan20FRsuksesRec25 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3025+pesanHeaderT.toString()+pesanFRgagalRec25.toString()+pesanFooterT.toString()
								+pesanSukses2025+pesanHeaderT.toString()+pesanFRsuksesRec25.toString()+pesanFooterT.toString()+pesanFooter.toString();					
						
					}else if(pesan30FRgagalRec25 && !pesan20FRsuksesRec25 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3025+pesanHeaderT.toString()+pesanFRgagalRec25.toString()+pesanFooterT.toString()+pesanFooter.toString();
						
					}else if(!pesan30FRgagalRec25 && pesan20FRsuksesRec25 )
					{
						pesanFull = pesanHeader.toString()+pesanSukses2025+pesanHeaderT.toString()+pesanFRsuksesRec25.toString()+pesanFooterT.toString()+pesanFooter.toString();
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to25, cc25, bcc,"WARNING !! Follow up SPAJ DMTM yang akan kadarluasa dan proses refund",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}

			// kirim email NPW khusus DMTM gagal recuring selama 45 hari dari tgl Further/ 30 hari sukses recur ---- (*RDL)
			if(pesan45FRgagalRec25 || pesan30FRsuksesRec25){

				try {
					if(pesan45FRgagalRec25 && pesan30FRsuksesRec25)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4525+pesanHeaderT2.toString()+pesanNPWgagalRec25.toString()+pesanFooterT.toString()
								+pesanSukses3025+pesanHeaderT2.toString()+pesanNPWsuksesRec25.toString()+pesanFooterT.toString()+pesanFooter.toString();						
						
					}else if(pesan45FRgagalRec25 && !pesan30FRsuksesRec25)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4525+pesanHeaderT2.toString()+pesanNPWgagalRec25.toString()+pesanFooterT.toString()+pesanFooter.toString(); 						
												
					}else if(!pesan45FRgagalRec25 && pesan30FRsuksesRec25)
					{
						pesanFull = pesanHeader2.toString()+pesanSukses3025+pesanHeaderT2.toString()+pesanNPWsuksesRec25.toString()+pesanFooterT.toString()+pesanFooter.toString();						
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to25, cc25, bcc,"SPAJ NPW DMTM",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}
			//SAPTA SARANA SEJAHTERA

			// kirim email khusus DMTM gagal recuring selama 30 hari dari tgl Further/ 20 hari sukses recur ---- (*RDL)
			if(pesan30FRgagalRec26 || pesan20FRsuksesRec26 ){

				try {
					if(pesan30FRgagalRec26 && pesan20FRsuksesRec26 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3026+pesanHeaderT.toString()+pesanFRgagalRec26.toString()+pesanFooterT.toString()
								+pesanSukses2026+pesanHeaderT.toString()+pesanFRsuksesRec26.toString()+pesanFooterT.toString()+pesanFooter.toString();					
						
					}else if(pesan30FRgagalRec26 && !pesan20FRsuksesRec26 )
					{
						pesanFull =	pesanHeader.toString()+pesanGagal3026+pesanHeaderT.toString()+pesanFRgagalRec26.toString()+pesanFooterT.toString()+pesanFooter.toString();				
												
					}else if(!pesan30FRgagalRec26 && pesan20FRsuksesRec26 )
					{
						pesanFull = pesanHeader.toString()+pesanSukses2026+pesanHeaderT.toString()+pesanFRsuksesRec26.toString()+pesanFooterT.toString()+pesanFooter.toString();						
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to26, cc26, bcc,"WARNING !! Follow up SPAJ DMTM yang akan kadarluasa dan proses refund",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}

			// kirim email NPW khusus DMTM gagal recuring selama 45 hari dari tgl Further/ 30 hari sukses recur ---- (*RDL)
			if(pesan45FRgagalRec26 || pesan30FRsuksesRec26){

				try {
					if(pesan45FRgagalRec26 && pesan30FRsuksesRec26)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4526+pesanHeaderT2.toString()+pesanNPWgagalRec26.toString()+pesanFooterT.toString()
								+pesanSukses3026+pesanHeaderT2.toString()+pesanNPWsuksesRec26.toString()+pesanFooterT.toString()+pesanFooter.toString();
												
					}else if(pesan45FRgagalRec26 && !pesan30FRsuksesRec26)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4526+pesanHeaderT2.toString()+pesanNPWgagalRec26.toString()+pesanFooterT.toString()
								+pesanFooter.toString(); 						
												
					}else if(!pesan45FRgagalRec26 && pesan30FRsuksesRec26)
					{
						pesanFull = pesanHeader2.toString()+pesanSukses3026+pesanHeaderT2.toString()+pesanNPWsuksesRec26.toString()+pesanFooterT.toString()
								+pesanFooter.toString();						
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to26, cc26, bcc,"SPAJ NPW DMTM",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}
			//DENA AKHYARO NUSANTARA

			// kirim email khusus DMTM gagal recuring selama 30 hari dari tgl Further/ 20 hari sukses recur ---- (*RDL)
			if(pesan30FRgagalRec27 || pesan20FRsuksesRec27 ){

				try {
					if(pesan30FRgagalRec27 && pesan20FRsuksesRec27 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3027+pesanHeaderT.toString()+pesanFRgagalRec27.toString()+pesanFooterT.toString()
								+pesanSukses2027+pesanHeaderT.toString()+pesanFRsuksesRec27.toString()+pesanFooterT.toString()+pesanFooter.toString();
												
					}else if(pesan30FRgagalRec27 && !pesan20FRsuksesRec27 )
					{
						pesanFull = pesanHeader.toString()+pesanGagal3027+pesanHeaderT.toString()+pesanFRgagalRec27.toString()+pesanFooterT.toString()+pesanFooter.toString();					
												
					}else if(!pesan30FRgagalRec27 && pesan20FRsuksesRec27 )
					{
						pesanFull = pesanHeader.toString()+pesanSukses2027+pesanHeaderT.toString()+pesanFRsuksesRec27.toString()+pesanFooterT.toString()+pesanFooter.toString(); 						
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to27, cc27, bcc,"WARNING !! Follow up SPAJ DMTM yang akan kadarluasa dan proses refund",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}

			// kirim email NPW khusus DMTM gagal recuring selama 45 hari dari tgl Further/ 30 hari sukses recur ---- (*RDL)
			if(pesan45FRgagalRec27 || pesan30FRsuksesRec27){

				try {
					if(pesan45FRgagalRec27 && pesan30FRsuksesRec27)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4527+pesanHeaderT2.toString()+pesanNPWgagalRec27.toString()+pesanFooterT.toString()
								+pesanSukses3027+pesanHeaderT2.toString()+pesanNPWsuksesRec27.toString()+pesanFooterT.toString()+pesanFooter.toString();					
												
					}else if(pesan45FRgagalRec27 && !pesan30FRsuksesRec27)
					{
						pesanFull = pesanHeader2.toString()+pesanGagal4527+pesanHeaderT2.toString()+pesanNPWgagalRec27.toString()+pesanFooterT.toString()
								+pesanFooter.toString();						
												
					}else if(!pesan45FRgagalRec27 && pesan30FRsuksesRec27)
					{
						pesanFull =	pesanHeader2.toString()+pesanSukses3027+pesanHeaderT2.toString()+pesanNPWsuksesRec27.toString()+pesanFooterT.toString()
								+pesanFooter.toString();					
						
					}
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to27, cc27, bcc,"SPAJ NPW DMTM",pesanFull,null, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
				}
			}
			//Proses pengiriman email -- START---- (*RDL)---------------------------------------------------------------------------end


		}
		catch (Exception e) {
			desc = "ERROR";
			logger.error("ERROR :", e);
			err=e.getLocalizedMessage();
								EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
										props.getProperty("admin.ajsjava"), new String[]{"ridhaal@sinarmasmsiglife.co.id"}, new String[]{"ryan@sinarmasmsiglife.co.id"}, bcc, 
										"ERROR SCHEDULER PROSES NPW (NOT PROCEED WITH) DMTM", 
										err, null, null);

		}
					try {
						insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
						msh_name, bdate, new Date(), desc,err);
					}
					catch (UnknownHostException e) {
						logger.error("ERROR :", e);
					}
	}
	
	/**
	 * Auto Reminder Smile Baby
	 * @author 	Randy (29-08-2016)
	 */	
	public void schedulerReminderSmileBaby(String msh_name) throws DataAccessException{
		String desc = "OK";
        String err = "";    
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        Date bdate = new Date();
        boolean aktif1 = false, aktif2 = false;
        List<Map> data = new ArrayList<Map>();
        data = uwDao.selectReminderSmileBaby();

        HashMap mapEmail1 = uwDao.selectMstConfig(6, "reminderSmileBaby", "reminderSmileBaby");

        String from = props.getProperty("admin.ajsjava");
            
    	String[] to = mapEmail1.get("NAME")!=null?mapEmail1.get("NAME").toString().split(";"):null;
    	String[] cc = mapEmail1.get("NAME2")!=null?mapEmail1.get("NAME2").toString().split(";"):null;
        String[] bcc = mapEmail1.get("NAME3")!=null?mapEmail1.get("NAME3").toString().split(";"):null;
                    
        try{                  
             StringBuffer pesan = new StringBuffer();
             String subject = "";
             aktif1 = false;
             if(data.size()>0){
	             for(int i=0; i<data.size(); i++){
	            	 aktif1 = false;
	            	 Map y = data.get(i);
	            	 String hitung = y.get("HITUNG").toString();
	            	 
	            	 if(hitung.equals("0") || hitung.equals("-1")){
	            		 aktif1 = true; aktif2 = true;
	                	 String spaj = y.get("REG_SPAJ").toString();
	                	 String polis = y.get("MSPO_POLICY_NO").toString();
	                	 String pemegang = y.get("PEMEGANG").toString();
	                	 String tertanggung = y.get("TERTANGGUNG").toString();
	                	 String hpl = y.get("HPL").toString();
	            		 subject = "Follow UP Nasabah Smile Baby "+pemegang+" "+spaj+" / "+polis;
	 					 pesan.append("Mohon dilakukan follow up nasabah Smile Baby agar melakukan endors data tertanggung : <br/><br/>");
	 					 pesan.append("Nama Pemeganng Polis          : "+pemegang+"<br/>");
	 					 pesan.append("Nama Tertanggung              : "+tertanggung+"<br/>");
	 					 pesan.append("Tanggal Perkiraan Lahir (HPL) : "+hpl+"<br/>");
	 					 pesan.append("Nomor SPAJ/Polis              : "+spaj+" / "+polis);
	            	 }
	            	 
	            	  if(aktif1){                                
	      				try { 	
	      					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
	                              from, to, cc, bcc, subject, pesan.toString(),  null, null);
//	      				email.send(true, from, new String[]{"randy@sinarmasmsiglife.co.id"}, 
//	      						new String[]{"randy@sinarmasmsiglife.co.id"}, 
//	      						new String[]{"randy@sinarmasmsiglife.co.id"}, subject, pesan.toString(), null);
	      				
	      				}
	      				catch (MailException e1) {
	      				       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
	      				      }
	      			 }
	          }
          }
      }// end of try
                            	      
        catch (Exception e) {
            desc = "ERROR";
            logger.error("ERROR :", e);
            err=e.getLocalizedMessage();
                        EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                      props.getProperty("admin.ajsjava"), new String[]{"randy@sinarmasmsiglife.co.id"}, new String[]{"ryan@sinarmasmsiglife.co.id"}, bcc, 
                                      "ERROR REMINDER SMILE BABY", 
                                      err, null, null);
              }
        
      if(aktif2){
	      try {
	            insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
	            msh_name, bdate, new Date(), desc,err);
	      }
	      catch (UnknownHostException e) {
	            logger.error("ERROR :", e);
	      }
      }
	}
	
	/**
	 * Autoreport List SPAJ Posisi Waiting Process NB dan SPAJ Gagal Proses
	 * Randy (16/03/2016)
	 * Req : Titis
	 */	
	public void schedulerWaitingProses(String msh_name) throws DataAccessException{
		String desc = "OK";
        String err = "";    
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date bdate = new Date();
        Integer waktu = 0;
        
        List<Map> data = new ArrayList<Map>();
        data = uwDao.selectSpajProses();

//      HashMap mapEmail1 = uwDao.selectMstConfig(6, "schedulerNpw", "schedulerNpw");
        HashMap mapEmail1 = uwDao.selectMstConfig(6, "schedulerWaitingProses", "schedulerWaitingProses");

        String from = props.getProperty("admin.ajsjava");
            
    	String[] to = mapEmail1.get("NAME")!=null?mapEmail1.get("NAME").toString().split(";"):null;
    	String[] cc = mapEmail1.get("NAME2")!=null?mapEmail1.get("NAME2").toString().split(";"):null;
        String[] bcc = mapEmail1.get("NAME3")!=null?mapEmail1.get("NAME3").toString().split(";"):null;
                    
        try{                  
                     StringBuffer pesan = new StringBuffer(), pesan1 = new StringBuffer(), footer = new StringBuffer();
                     int no=1;
                     boolean aktif1 = false;
                     Map y = data.get(0); 
                     String jam = y.get("JAM_CUTOFF").toString();
                     
                     pesan.append("<body><b>Dear Team </b><br><br>Terlampir List SPAJ Posisi Waiting Process NB dan SPAJ Gagal Proses. Mohon dapat segera di follow up.");
                     pesan.append("<br><br>");
                     if(jam.equals("04")||jam.equals("05")||jam.equals("06")||jam.equals("07")||jam.equals("08")||jam.equals("09")||jam.equals("10")||jam.equals("11")){
                    	 pesan.append("<font color='#FF0000'>Cut Off: " + y.get("TGL_CUTOFF") + " 05.00/13.00</font>");
                    	 waktu = 1;
                     } else { 
                    	 pesan.append("<font color='#FF0000'>Cut Off: " + y.get("TGL_CUTOFF") + " 13.00/05.00</font>");
                    	 waktu = 2;} 
                     pesan.append("<br><br>");
                     pesan.append("<table border ='1' cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1200px'>");
                     pesan.append("<tr bgcolor='#A8B8EE'>");
                     pesan.append("<th align='center'>NO.</th>");
                     pesan.append("<th align='center'>REG SPAJ</th>");
 					 pesan.append("<th align='center'>TGL TRANSFER KE SPEEDY</th>");
 					 pesan.append("<th align='center'>NAMA CABANG</th>");
 					 pesan.append("<th align='center'>USER AKSEPTASI</th>");
 					 pesan.append("<th align='center'>PEMEGANG</th>");
 					 pesan.append("<th align='center'>TERTANGGUNG</th>");
 					 pesan.append("<th align='center'>PRODUK</th>");
 					 pesan.append("<th align='center'>TGL APPROVE NB/UW</th>");
 					 pesan.append("<th align='center'>TGL APPROVE COLLECTION</th>");					
 					 pesan.append("<th align='center'>STATUS</th>");
 					 pesan.append("<th align='center'>KETERANGAN</th></tr>");
 					  
                     footer.append("</table> <br><br>");
                     footer.append("<b>Terima Kasih</b><br><br>");
                     
                     for(int i=0; i<data.size();i++){
                         Map x = data.get(i);     
                         pesan1.append("<tr><td nowrap>"+ no++  + "</td>");
                         pesan1.append("<td>"+ x.get("REG_SPAJ") + "</td>"); 
                         pesan1.append("<td>"+ x.get("TGL_TRANSFER_KE_SPEEDY") + "</td>");
                         pesan1.append("<td>"+ x.get("NAMA_CABANG") + "</td>");
                         pesan1.append("<td>"+ x.get("USER_AKSEPTASI") + "</td>"); 
                         pesan1.append("<td>"+ x.get("PEMEGANG") + "</td>"); 
                         pesan1.append("<td>"+ x.get("TERTANGGUNG") + "</td>");
                         pesan1.append("<td>"+ x.get("PRODUK") + "</td>");
                         if(x.get("TGL_APPROVE_NB")!=null) {pesan1.append("<td>"+ x.get("TGL_APPROVE_NB") + "</td>");}else{pesan1.append("<td>"+ " " + "</td>");}
                         if(x.get("TGL_APPROVE_COLLECTION")!=null) {pesan1.append("<td>"+ x.get("TGL_APPROVE_COLLECTION") + "</td>");}else{pesan1.append("<td>"+ " " + "</td>");}
                         pesan1.append("<td>"+ x.get("STATUS") + "</td>");
                         if(x.get("KETERANGAN")!=null) {pesan1.append("<td>"+ x.get("KETERANGAN") + "</td>");}else{pesan1.append("<td>"+ " " + "</td>");}
                         aktif1 = true;
                     } //end of for

                     if(aktif1){                                
  						try { 	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                from, to, cc, bcc,"List SPAJ Posisi Waiting Process NB dan SPAJ Gagal Proses", pesan.toString()+pesan1.toString()+footer.toString(),  null, null);
  						       }
  						catch (MailException e1) {
  						       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
  						      }
  						}

              }// end of try
                            	      
        catch (Exception e) {
            desc = "ERROR";
            logger.error("ERROR :", e);
            err=e.getLocalizedMessage();
                        EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                                      props.getProperty("admin.ajsjava"), new String[]{"randy@sinarmasmsiglife.co.id"}, new String[]{"ryan@sinarmasmsiglife.co.id"}, bcc, 
                                      "ERROR SCHEDULER WAITING PROSES NB, DAN GAGAL PROSES", 
                                      err, null, null);
              }
        try {
    	  	if(waktu == 1){
    	  		msh_name = "AUTOMAIL REPORT SPAJ WAITING PROSES CUTOFF PAGI";
                insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
                        msh_name, bdate, new Date(), desc,err);
    	  	}else if(waktu == 2){
    	  		msh_name = "AUTOMAIL REPORT SPAJ WAITING PROSES CUTOFF SIANG";
    	  		insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
                        msh_name, bdate, new Date(), desc,err);
    	  	}else{
    	  		insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
                        msh_name, bdate, new Date(), desc,err);
    	  	}

      	}
      	catch (UnknownHostException e) {
            logger.error("ERROR :", e);
      	}
	}	
	
	/**
	 * Fungsi scheduller untuk WELCOME CALL 
	 * 1.Data SPAJ yang masuk ke CS untuk dilakukan Welcoma Call 
	 * (Polis inforce + 1) dengan  premi awal min (premi pokok + top up)  Rp 50.000.000 unit link semua chanell (bukan APE))
	 * lspd_id = 6
	 * 2. Pengiriman Email yang menginformasikan SPAJ sudah berhasil di Welcome call oleh CS
	 * Flag_validasi = 2 dan lspd_id = 7
	 * @author Ridhaal (21/09/2016)
	 * @throws DataAccessException
	 */
	public void schedulerWelcomeCall(String msh_name) throws DataAccessException{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date bdate 	= new Date();
		ArrayList<HashMap> data = new ArrayList<HashMap>();
		ArrayList<HashMap> data2 = new ArrayList<HashMap>();
		data = Common.serializableList(uwDao.selectInforceToWelcomeCall()); // pengambilan data hari kemarin karena (kriteria : Polis inforce + 1) untuk scheduller today
		data2 = Common.serializableList(basDao.selectWelcomeCallSuccess(null, 0)); // pengambilan data yang berhasil di validasi oleh CS (flag_validasi = 2 ) dengan lspd_id = 6 dan 7 untuk scheduller today
		String desc	= "OK";
		String err="";
		
		 try {
	            insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
	            msh_name, bdate, new Date(), desc,err);
	      }
	     catch (UnknownHostException e) {
	            logger.error("ERROR :", e);
	      }
			
        //melakukan proses wellcome call dimana status policy sudah inforce (lspd_id 6) - Ridhaal     
/*		try{*/
			for(int i=0; i<data.size(); i++){
				Map x = data.get(i);
				String spaj = x.get("REG_SPAJ").toString();
				String lspd_id = x.get("LSPD_ID").toString();
				String lsbs_id = x.get("LSBS_ID").toString();
				String lsdbs_number = x.get("LSDBS_NUMBER").toString();
				Double ape = ((BigDecimal) x.get("APE")).doubleValue();
				String mi_id = uwDao.selectSeqInboxId();
				Date nowDate = commonDao.selectSysdate();
				
				ArrayList<HashMap> cekdatainbox = new ArrayList<HashMap>();
				cekdatainbox = Common.serializableList(basDao.selectWelcomeCallSuccess(spaj, 1)); // cek data sudah terinput di mst inbox untuk ljj_id = 63 (WC) dengan lspd_id = 6, 7 
				String message = "Success To Welcome Call";
				int status = 8;
				if(cekdatainbox.isEmpty()){
				
						//welcome call cs - ulink total premi >50 juta 
						//posisi awal 27 di ganti ke 6 (edit by Ridhaal)
						//Flag_validasiset ke 1 ( 0 = tanpa validasi; 1 = need validasi; 2 = approve validasi; 3 = gagal validasi)
						if(lspd_id.equalsIgnoreCase("6") || lspd_id.equalsIgnoreCase("7")){
							Double jml_premi = uwDao.selectTotalPremiUlink(spaj);
							
							//khusus produk Magna dan Prime Link, APE di atas 20 juta ; selain itu total premi 50000000
							if(( (lsbs_id.equalsIgnoreCase("213") && (lsdbs_number.equalsIgnoreCase("1") || lsdbs_number.equalsIgnoreCase("2"))) 
							      || (lsbs_id.equalsIgnoreCase("216") && lsdbs_number.equalsIgnoreCase("1")) 
							      || (lsbs_id.equalsIgnoreCase("134") && lsdbs_number.equalsIgnoreCase("5")) 	
							      || (lsbs_id.equalsIgnoreCase("215") && lsdbs_number.equalsIgnoreCase("1")) ))
							{						
									if (ape >= 20000000){
										mi_id = uwDao.selectSeqInboxId();
										MstInbox mstInbox_welcall = new MstInbox(mi_id, 63, 204, null, 202, 
												null, null, spaj, null, 
												null, 1, nowDate, null, 
												null, null, 0, nowDate, null, null, 1, null, null, null, 0, null, null, null,null,1,1);
										MstInboxHist mstInboxHist_welcall = new MstInboxHist(mi_id, 202, 204, null, null, null, 0 , nowDate, null,0,1);
										snowsDao.insertMstInbox(mstInbox_welcall);
										snowsDao.insertMstInboxHist(mstInboxHist_welcall);
									}else{
										status = 9;
									    message = "Not Success To Welcome Call Since Not Part of Rule kurang 20000000";
									}
								
							}else{					
									if(jml_premi>=50000000){
										mi_id = uwDao.selectSeqInboxId();
										MstInbox mstInbox_welcall = new MstInbox(mi_id, 63, 204, null, 202, 
												null, null, spaj, null, 
												null, 1, nowDate, null, 
												null, null, 0, nowDate, null, null, 1, null, null, null, 0, null, null, null,null,1,1);
										MstInboxHist mstInboxHist_welcall = new MstInboxHist(mi_id, 202, 204, null, null, null, 0 , nowDate, null,0,1);
										snowsDao.insertMstInbox(mstInbox_welcall);
										snowsDao.insertMstInboxHist(mstInboxHist_welcall);
									}else{
										status = 9;
									    message = "Not Success To Welcome Call Since Not Part of Rule premi kurang 50000000 ";
									}
							}
						}
						
					
						
						
				}else{
					status = 9;
				    message = "Not Success To Welcome Call Since Not Part of Rule already in welcome call";
				}
				WelcomeCallStatus welcomeCallStatus = new WelcomeCallStatus();
				welcomeCallStatus.setReg_spaj(spaj);
				welcomeCallStatus.setStatus_insert(status);
				welcomeCallStatus.setStatus_message(message);
				welcomeCallStatus.setLsbs_id(Integer.parseInt(lsbs_id));
				welcomeCallStatus.setLsdbs_number(Integer.parseInt(lsdbs_number));
				snowsDao.insertWelcomeCallStatus(welcomeCallStatus);
		 		
			}
	/*	}catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR pengambilan data hari kemarin karena (kriteria : Polis inforce + 1) Wellcome Call ";
			err = e.getLocalizedMessage();
		}
		*/
		//pengiriman email scheduller yang berhasil di validasi oleh CS (Wellcome Call)
		String from = props.getProperty("admin.ajsjava");
            
		String to = "timmy@sinarmasmsiglife.co.id;rizky_a@sinarmasmsiglife.co.id;";
		String cc = null;
		String bcc = "ridhaal@sinarmasmsiglife.co.id;";
		
		String[] emailto = to!=null?to.split(";"):null;
		String[] emailcc = cc!=null?cc.split(";"):null;
		String[] emailbcc = bcc!=null?bcc.split(";"):null;
			
		 try{                  
             StringBuffer pesan = new StringBuffer(), pesan1 = new StringBuffer(), footer = new StringBuffer();
             int no=1;
             boolean aktif1 = false;
             
             pesan.append("<body><b>Dear UW Printing </b><br><br>Berikut data polis yang berhasil validasi welcome call dimana sebelumnya gagal valdasi. Mohon diproses transfer.");
             pesan.append("<br><br>");
             
             pesan.append("<table border ='1' cellpadding='4' cellspacing='0' style='table-layout: fixed; width:1200px'>");
             pesan.append("<tr bgcolor='#A8B8EE'>");
             pesan.append("<th align='center'>NO.</th>");
             pesan.append("<th align='center'>NAMA CABANG</th>");
				 pesan.append("<th align='center'>REG SPAJ</th>");
				 pesan.append("<th align='center'>NO POLIS</th>");
				 pesan.append("<th align='center'>NAMA PEMEGANG POLIS</th>");		
				  
             footer.append("</table> <br><br>");
             footer.append("<b>Terima Kasih</b><br><br>");
             
             for(int i=0; i<data2.size();i++){
                 Map x = data2.get(i);     
                 pesan1.append("<tr><td nowrap>"+ no++  + "</td>");
                 pesan1.append("<td>"+ x.get("LCA_NAMA") + "</td>");
                 pesan1.append("<td>"+ x.get("REG_SPAJ") + "</td>");                  
                 pesan1.append("<td>"+ x.get("MSPO_POLICY_NO") + "</td>");
                 pesan1.append("<td>"+ x.get("MCL_FIRST") + "</td>"); 
               aktif1 = true;
             } //end of for

             if(aktif1){                                
					try { 
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
                        from, emailto, emailcc, emailbcc,"List Data Polis yang berhasil Validasi Welcome Call", pesan.toString()+pesan1.toString()+footer.toString(),  null, null);
					
//						email.send(true, 
//								"ridhaal@sinarmasmsiglife.co.id",
//								new String[]{"ridhaal@sinarmasmsiglife.co.id"},
//								null,
//								null,
//								"List Data Polis yang berhasil Validasi Welcome Call",
//								pesan.toString()+pesan1.toString()+footer.toString(), 
//								null);
					
					}
					catch (MailException e1) {
					       logger.error("ERROR :", e1);err=e1.getLocalizedMessage();
					      }
					}

		 }// end of try                    	      
		catch (Exception e) {
		    desc = "ERROR";
		    logger.error("ERROR :", e);
		    err=e.getLocalizedMessage();
		                EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
		                              props.getProperty("admin.ajsjava"), new String[]{"ridhaal@sinarmasmsiglife.co.id"}, new String[]{"ryan@sinarmasmsiglife.co.id"}, emailbcc, 
		                              "ERROR SCHEDULER List Data Polis yang berhasil Validasi Welcome Call", 
		                              err, null, null);
		      }
				

	}
	public void schedulerWelcomeCallCorona(String msh_name) throws DataAccessException{
		String desc	= "OK";
		String err="";
		Date bdate 	= new Date();
		 try {
	            insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
	            msh_name, bdate, new Date(), desc,err);
	      }
	     catch (UnknownHostException e) {
	            logger.error("ERROR :", e);
	      };
	      
	      ArrayList<HashMap> data = new ArrayList<HashMap>();
	   	  data = Common.serializableList(uwDao.selectInforceToWelcomeCallCorona());
	   	for(int i=0; i<data.size(); i++){
	 		String message = "Success To Welcome Call";
	 		int status = 8;
	 		Map x = data.get(i);
	 		String spaj = x.get("REG_SPAJ").toString();
	 		String lsbs_id = x.get("LSBS_ID").toString();
			String lsdbs_number = x.get("LSDBS_NUMBER").toString();
			String mi_id = uwDao.selectSeqInboxId();
	 		Date nowDate = commonDao.selectSysdate();
	 		MstInbox mstInbox_welcall = new MstInbox(mi_id, 63, 204, null, 202, 
					null, null, spaj, null, 
					null, 1, nowDate, null, 
					null, null, 0, nowDate, null, null, 1, null, null, null, 0, null, null, null,null,1,1);
			MstInboxHist mstInboxHist_welcall = new MstInboxHist(mi_id, 202, 204, null, null, null, 0 , nowDate, null,0,1);
			snowsDao.insertMstInbox(mstInbox_welcall);
			snowsDao.insertMstInboxHist(mstInboxHist_welcall);
			WelcomeCallStatus welcomeCallStatus = new WelcomeCallStatus();
			welcomeCallStatus.setReg_spaj(spaj);
			welcomeCallStatus.setStatus_insert(status);
			welcomeCallStatus.setStatus_message(message);
			welcomeCallStatus.setLsbs_id(Integer.parseInt(lsbs_id));
			welcomeCallStatus.setLsdbs_number(Integer.parseInt(lsdbs_number));
			snowsDao.insertWelcomeCallStatus(welcomeCallStatus);
	 		
	   	}
	   	  
	}
	public void schedulerWelcomeCallBankAs(String msh_name) throws DataAccessException{
		String desc	= "OK";
		String err="";
		Date bdate 	= new Date();
		 try {
	            insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(),
	            msh_name, bdate, new Date(), desc,err);
	      }
	     catch (UnknownHostException e) {
	            logger.error("ERROR :", e);
	      };
	      ArrayList<HashMap> data = new ArrayList<HashMap>();
	  	 data = Common.serializableList(uwDao.selectInforceToWelcomeCallBankAs()); // pengambilan data hari kemarin karena (kriteria : Polis inforce + 1) untuk scheduller today
		
	 	for(int i=0; i<data.size(); i++){
	 		String message = "Success To Welcome Call";
			int status = 0;
	 		Map x = data.get(i);
	 		String spaj = x.get("REG_SPAJ").toString();
	 		String lscb_id = x.get("LSCB_ID").toString();
	 		String lsbs_id = x.get("LSBS_ID").toString();
			String lsdbs_number = x.get("LSDBS_NUMBER").toString();
			ArrayList<HashMap> cekdatainbox = new ArrayList<HashMap>(); 
			Date nowDate = commonDao.selectSysdate();
			cekdatainbox = Common.serializableList(basDao.selectWelcomeCallSuccess(spaj, 1)); // cek data sudah terinput di mst inbox untuk ljj_id = 63 (WC) dengan lspd_id = 6, 7 
			String lspd_id = x.get("LSPD_ID").toString();
			if(cekdatainbox.isEmpty()){
				if(lspd_id.equalsIgnoreCase("6") || lspd_id.equalsIgnoreCase("7")){
					String mi_id = uwDao.selectSeqInboxId();
					Double jml_premi = uwDao.selectTotalPremiUlink(spaj);
					boolean allowedInsert = false;
					if(lscb_id.trim().equals("3") && jml_premi>=5000000){
						allowedInsert = true;
					}
					if(lscb_id.trim().equals("6") && jml_premi>=1000000){
						allowedInsert = true;
					}
					if(lscb_id.trim().equals("2") && jml_premi>=2500000){
						allowedInsert = true;
					}
					if(lscb_id.trim().equals("1") && jml_premi>=1250000){
						allowedInsert = true;
					}
					if(lscb_id.trim().equals("0") && jml_premi>=10000000){
						allowedInsert = true;
					}
					
					if(allowedInsert){
						
						mi_id = uwDao.selectSeqInboxId();
						MstInbox mstInbox_welcall = new MstInbox(mi_id, 63, 204, null, 202, 
								null, null, spaj, null, 
								null, 1, nowDate, null, 
								null, null, 0, nowDate, null, null, 1, null, null, null, 0, null, null, null,null,1,1);
						MstInboxHist mstInboxHist_welcall = new MstInboxHist(mi_id, 202, 204, null, null, null, 0 , nowDate, null,0,1);
						snowsDao.insertMstInbox(mstInbox_welcall);
						snowsDao.insertMstInboxHist(mstInboxHist_welcall);
						
					}else{
						status = 1;
					    message = "Not Success To Welcome Call Since Not Part of Rule jml_premi kurang "+jml_premi ;
					}
					
				}
			}else{
				status = 1;
			    message = "Not Success To Welcome Call Since Already exist in welcome call";
			};
			
			WelcomeCallStatus welcomeCallStatus = new WelcomeCallStatus();
			welcomeCallStatus.setReg_spaj(spaj);
			welcomeCallStatus.setStatus_insert(status);
			welcomeCallStatus.setStatus_message(message);
			welcomeCallStatus.setLsbs_id(Integer.parseInt(lsbs_id));
			welcomeCallStatus.setLsdbs_number(Integer.parseInt(lsdbs_number));
			snowsDao.insertWelcomeCallStatus(welcomeCallStatus);
	 		
	 		
	 	}
	 	
	    ArrayList<HashMap> dataDuplicate = new ArrayList<HashMap>();
	    dataDuplicate = Common.serializableList(uwDao.selectDuplicateSnows()); // data duplicate
		for(int i=0; i<dataDuplicate.size(); i++){
			Map x = dataDuplicate.get(i);
	 		String spaj = x.get("REG_SPAJ").toString();
	 		List<Map> total = uwDao.selectCountMstInboxSnows(spaj);
	 		if(total.size()>0){
	 			int tot = ((BigDecimal) total.get(0).get("TOTAL")).intValue();
	 			int lspd_id = ((BigDecimal) total.get(0).get("LSPD_ID")).intValue();
	 			int lspd_id_from = ((BigDecimal)  total.get(0).get("LSPD_ID_FROM")).intValue();
	 			int ljj_id = ((BigDecimal)  total.get(0).get("LJJ_ID")).intValue();
	 			int mi_pos = ((BigDecimal)  total.get(0).get("MI_POS")).intValue();
	 			if(tot >= 2){
	 				List<Map> dups =  uwDao.selectListSnowsDuplicate(spaj,ljj_id,lspd_id,lspd_id_from,mi_pos);
	 				System.out.println("-----"+dups.size()+"-----");
	 				Map m = dups.get(0);
	 				String mi_id = (String)m.get("MI_ID");
	 				WelcomeCallStatus welcomecallStatus = new WelcomeCallStatus();
	 				welcomecallStatus.setReg_spaj(spaj);
	 				welcomecallStatus.setStatus_insert(-3);
	 				snowsDao.deleteMstInboxHist(mi_id);
	 				snowsDao.deleteMstInbox(mi_id);	 				
	 				snowsDao.updateWelcomeCallStatus(welcomecallStatus);
	 				
	 				
	 			}else{
	 				WelcomeCallStatus welcomecallStatus = new WelcomeCallStatus();
	 				welcomecallStatus.setReg_spaj(spaj);
	 				welcomecallStatus.setStatus_insert(-3);
	 				snowsDao.updateWelcomeCallStatus(welcomecallStatus);
	 			}
	 			
	 			
	 			
	 		}
	 		
		}
	  	 
	  	 
	};
	
	
	/**
	 * Fungsi scheduller untuk mengirim email otomatis dari UW ke Finance perihal Refund Premi
	 * @author MANTA
	 * @throws DataAccessException
	 */
	public void schedulerRefundPremiSuccessive(String msh_name) {
		Date bdate = commonDao.selectSysdate();
		Date yesterday = commonDao.selectSysdateTruncated(-1);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		String desc	= "OK";
		String err = "";
		
		try{
			ArrayList<HashMap> data = new ArrayList<HashMap>();
			data = Common.serializableList(refundDao.selectRefundPremiLanjutan());
			
			Map params = new HashMap();
			params.put("tgl_report", df.format(yesterday));

			String subject = "[INFO] PERMINTAAN REFUND PREMI LANJUTAN";
			String outputDir = props.getProperty("pdf.dir.report") + "\\refund_lanjutan\\"+df2.format(bdate)+"\\";
			String outputFilename = "DetRefundPremiLanjutan.xls";
			
			JasperUtils.exportReportToXls(
						props.getProperty("report.refund.det_refund_lanjutan")+".jasper", 
						outputDir,
						outputFilename,
						params,
						data, null);
			
			List<File> attachments = new ArrayList<File>();
			if(data.size() > 0){
				attachments.add(new File(outputDir + "\\" + outputFilename));
				
				//Format isi email
				StringBuffer pesan = new StringBuffer();
				
				pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 2px solid black; align: center; font-size: 10pt} table tr{border: 1px solid black} table th{border: 1px solid black} table td{border: 1px solid black}</style></head>");
				pesan.append("<body>Telah dilakukan permintaan refund premi lanjutan pada tanggal <b>" + df.format(yesterday) + "</b> untuk Polis-polis yang tertera pada lampiran.<br>");
				pesan.append("Harap segera dilakukan proses pembayaran terhadap Polis-polis tersebut.");
				
				HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerRefundPremiSuccessive", "SCHEDULER_REFUND_PREMI_SUCC");
				String from = props.getProperty("admin.ajsjava");
				String emailto = mapEmail.get("NAME")!=null? mapEmail.get("NAME").toString():null;
				String emailcc = mapEmail.get("NAME2")!=null? mapEmail.get("NAME2").toString():null;
				String emailbcc = mapEmail.get("NAME3")!=null? mapEmail.get("NAME3").toString():null;
				
				String[] to = emailto.split(";");
				String[] cc = emailcc.split(";");
				String[] bcc = emailbcc.split(";");
					
				try {
					//use email pool
					EmailPool.send("E-Lions", 1, 1, 0, 0, 
							null, 0, 0, commonDao.selectSysdate(), null, 
							true,
							from, 
							new String[] {"septa@sinarmasmsiglife.co.id"}, 
							null, 
							bcc, 
							subject, 
							pesan.toString(), 
							attachments, null);
				}
				catch (MailException e1) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e1);
					err = e1.getLocalizedMessage();
					desc = "ERROR";
				}
			}
		}catch (Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR";
			err = e.getLocalizedMessage();
		}
		
		try{
			insertMstSchedulerHist(
					InetAddress.getLocalHost().getHostName(),
					msh_name, bdate, new Date(), desc, err);
		}catch (UnknownHostException e) {
			logger.error("ERROR :", e);
		}
	}
	
	/**
	 * Fungsi scheduller untuk Report  Pending Print SIAP2U
	 * @author Iga
	 * @time 01/04/2020
	 */
	public void schedulerPendingPrintSIAP2U(String msh_name) {
		Date bDate = new Date();
		String desc = "OK";
		String err = "";
		DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
		
		try {			
			Map<String, Object> params = new HashMap<String, Object>();
			SimpleDateFormat sdf= new SimpleDateFormat("EEE");
			Date today = commonDao.selectSysdateTruncated(0);
			String file="";
			File destDir = new File(file);
			
			List dataPrint = uwDao.selectPendingPrintSIAP2U(FormatDate.toString(today));
			if(dataPrint.size() > 0){
				for(int x=0; x<dataPrint.size();x++){	
					String idgadget = (String) ((Map) dataPrint.get(x)).get("NO_SPAJ_TEMP");
					String spaj = (String) ((Map) dataPrint.get(x)).get("NO_SPAJ");
					String cabang = uwDao.selectCabangFromSpaj(spaj);
					String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
							+ cabang + "\\" + spaj;
					file = exportDirectory + "\\" + idgadget + "_FT_SP_AUTOSALES"+ ".pdf";
					destDir = new File(file);
				}
					if (!destDir.exists()){
						
						
						HashMap mapReport = uwDao.selectMstConfig(6, "Report Pending Print SIAP2U", "report.pending_print_siap2u");
						HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerPendingPrintSiap2u", "SCHEDULER_PENDING_PRINT_SIAP2U");
						String[] to = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
						String[] cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
						String[] bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
						StringBuffer pesan = new StringBuffer();
						String outputDir = props.getProperty("pdf.dir.report") +  "\\Report_Pending_Print_SIAP2U\\";			
						String outputFilename = "pending_print_siap2u_"+df2.format(today)+".xls" ;
							
						JasperUtils.exportReportToXls(mapReport.get("NAME") + ".jasper", 
								outputDir, outputFilename, params, dataPrint, null);
							
						List<File> attachments = new ArrayList<File>();
						attachments.add(new File(outputDir + "\\" + outputFilename));
							
						pesan.append("Dear Rekan-rekan SIAP2U,");
						pesan.append("\n\nTerlampir list Polis Smile Link Ultimate dan Smile Link Ultimate Syariah yang belum melampirkan form Customer Declaration.");
						pesan.append("\nDokumen dapat dikirimkan ke Krisna@sinarmasmsiglife.co.id.");
						pesan.append("\n\n<b>Mohon dapat segera melengkapi agar tidak menghambat proses cetak polis.</b>");
						pesan.append("\n\nDemikian kami sampaikan atas perhatian dan kerjasamanya kami ucapkan terimakasih.");
						pesan.append("\n\nNew Business.");		
						
						EmailPool.send("E-Lions", 1, 1, 0, 0, 
								null, 0, 0, bDate, null, 
								true, "info@sinarmasmsiglife.co.id", 
								to, 
								cc, 
								bcc, 
								"[PENDING PRINT] Informasi Polis Smile Link Ultimate dan Smile Link Ultimate Syariah "+ " "+ FormatDate.toIndonesian(today), 
								pesan.toString(), 
								attachments, null);
//					}
					
				}
			}
		
			
		} catch(Exception e) {
			logger.error("ERROR :", e);
			desc = "ERROR";
			err = e.getLocalizedMessage();
			
			HashMap mapEmail = uwDao.selectMstConfig(6, "schedulerPendingPrintSiap2u", "SCHEDULER_PENDING_PRINT_SIAP2U");
			String[] bcc = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
			
			if(Common.isEmpty(err)) {
				err = "- Exception : \n";
				err += ExceptionUtils.getStackTrace(e);
			}
			
			EmailPool.send("E-Lions", 1, 0, 0, 0, null, 0, 0, new Date(), null, true, 
					props.getProperty("admin.ajsjava"), 
					bcc, 
					null, 
					null, 
					"ERROR SCHEDULER PENDING PRINT SIAP2U", 
					err, 
					null, 
					null);
		} finally {
			try {
				insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(), msh_name, bDate, new Date(), desc, err);
			} catch (UnknownHostException e) {
				logger.error("ERROR :", e);
			}
		}
		
	}
}