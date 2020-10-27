package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.spring.util.Email;
import id.co.sinarmaslife.std.spring.util.EmailSender;
import id.co.sinarmaslife.std.util.FileUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.security.Principal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.CommandUploadBac;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilInvestasi;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.AjaxManager;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.elions.web.uw.PrintPolisMultiController;
import com.ekalife.elions.web.uw.PrintPolisPrintingController;
import com.ekalife.elions.web.uw.support.WordingPdfViewer;
import com.ekalife.utils.CekPelengkap;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.ITextPdf;
import com.ekalife.utils.MergePDF;
import com.ekalife.utils.PdfUtils;
import com.ekalife.utils.Print;
import com.ekalife.utils.PrintPolisPerjanjianAgent;
import com.ekalife.utils.Products;
import com.ekalife.utils.QrUtils;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentJasperReportingController;
import com.ekalife.utils.view.PDFViewer;
import com.ibatis.common.resources.Resources;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.ekalife.utils.view.PDFViewer;
import com.lowagie.text.pdf.PdfCopy;
import com.sun.pdfview.PDFFile;
/**
 * @author Mark Valentino
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class PrintPolisAllPelengkap extends ParentJasperReportingController{
	protected final Log logger = LogFactory.getLog( getClass() );
	Errors errors;
	DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
	
	public  PrintPolisAllPelengkap (ServletContext servletContext){
		this.setServletContext(servletContext);
	}
	
	public PrintPolisAllPelengkap(){
		
	}

	
	//Mark Valentino 20180906
	public ModelAndView generatePolisAllPelengkap(HttpServletRequest request, HttpServletResponse response, ElionsManager elionsManager, UwManager uwManager, BacManager bacManager, Properties props, Products products) throws Exception {
		String success  = ""; 
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List errors = new ArrayList();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj");	
		Integer flagFrom = ServletRequestUtils.getIntParameter(request, "flag", 2);//1 print dan kirim email(khusus produk dari click for life) 2: print all tanpa mengirim email
		Integer mspo_provider=uwManager.selectGetMspoProvider(spaj);
		ServletOutputStream out = response.getOutputStream();		
	    Document document = new Document();	
		List pdfFiles = new ArrayList();
		try{
			//Mark Valentino 2018090908 jangan lupa aktifkan lagi
			elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT POLIS ALL(E-LIONS)", true, currentUser);
			
			//(HttpServletRequest request,Integer mspoProvider, int flagPrePrinted, ElionsManager elionsManager, UwManager uwManager, BacManager bacManager, Properties props, Products products)
			generateReport3(request, mspo_provider, 2, elionsManager, uwManager, bacManager, props, products);
				
		}catch (Exception e) {			
			 errors.add("Terjadi kesalahan dalam generate Polis");
			 logger.error("ERROR", e);
			 bacManager.updateMst_policyEmptyPrintDate(spaj);
		}
	    if(errors.isEmpty()){
	    	Map detBisnis = (Map) elionsManager.selectDetailBisnis(spaj).get(0);
			String businessId = (String) detBisnis.get("BISNIS");
			String lsbs_name = (String) detBisnis.get("LSBS_NAME");
			String lsdbs_name = (String) detBisnis.get("LSDBS_NAME");
			String lsdbs = (String) detBisnis.get("DETBISNIS");
	    	if(products.unitLink(businessId) && !products.stableLink(businessId)) {
				elionsManager.updateUlink(3, spaj, df2.format(elionsManager.selectMuTglTrans(spaj)));
			}
	    	
		    String cabang = elionsManager.selectCabangFromSpaj(spaj);
		    String dir = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;	    

		    String file = "polis_all.pdf";	    
		    if(flagFrom==1){
		    	//kirim email dari click for life
		    }else if(flagFrom==2){
			    Document documentPolisAll = new Document();
				try {
					
					//baca source pdf nya
					Boolean itextReaderOld = true;
					PdfReader reader = null;
					com.itextpdf.text.pdf.PdfReader readerNew = null;
					
					try{
						reader = new PdfReader(dir + "\\" + file, "ekalife".getBytes());
					} catch(IOException ioe1){
						try{
							reader = new PdfReader(dir + "\\" + file);
						}catch(IOException ioe2){
							itextReaderOld=false;
							try {
								readerNew = new com.itextpdf.text.pdf.PdfReader(dir + "\\" + file, "ekalife".getBytes());
							} catch (IOException ioe) {
								try {
									readerNew = new com.itextpdf.text.pdf.PdfReader(dir + "\\" + file);
								} catch (IOException e) {		
									response.setContentType("text/html");					
									ServletOutputStream out2 = response.getOutputStream();
						    		out2.println("<script>alert('File tidak ada. Harap cek kembali data Anda masukkan.');</script>");
						    		out2.flush();
						    		document.close();
						    		return null;	
								}
							}
						}
					}					
		//			response header
					response.setHeader("Content-Disposition", "inline;filename=file.pdf");
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					response.setContentType("application/pdf");		
					//"stamp" source pdf nya ke output stream tanpa menambah apa2
		            ServletOutputStream sos = response.getOutputStream();
		            if(itextReaderOld){
		            	PdfStamper stamper = new PdfStamper(reader, sos);
		            	//disable printing apabila ada flag
		    			if(ServletRequestUtils.getIntParameter(request, "readonly", 0) == 1){
		    				stamper.setEncryption(false, null, null, PdfWriter.ALLOW_MODIFY_ANNOTATIONS);
		    			}
		    			stamper.close();	
		            }else{
		            	com.itextpdf.text.pdf.PdfStamper stamperNew = new com.itextpdf.text.pdf.PdfStamper(readerNew, sos);
		            	//disable printing apabila ada flag
		    			if(ServletRequestUtils.getIntParameter(request, "readonly", 0) == 1){
		    				stamperNew.setEncryption(false, null, null, PdfWriter.ALLOW_MODIFY_ANNOTATIONS);
		    			}
		    			stamperNew.close();
		            }
					
		            if(sos!=null){
						sos.flush();
						sos.close();
					}
		            if(reader!=null)reader.close();
		            if(readerNew!=null)readerNew.close();			
				}catch(Exception de) {
					logger.error(de);
		    		ServletOutputStream out2 = response.getOutputStream();
		    		out2.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
		    		out2.flush();
				}
				document.close();
		    }
		    
			return null;
	    }else{
	    	return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
	    }
	}	
	
	//Mark Valentino 20180905 RDS
	public void generateFileRDS(HttpServletRequest request, HttpServletResponse response, Integer mspoProvider, int flagPrePrinted, ElionsManager elionsManager, UwManager uwManager, BacManager bacManager, Properties props, Products products) throws Exception{
		
		Map paramsPolis = null;
		
		//Mark Valentino 20180920 - Generate File SPAJ
		String reg_spaj = request.getParameter("spaj");

		//20181114 Mark Valentino Non aktifkan utk Test - Aktifkan utk Production
		List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
		if (listSpajTemp.size() > 0){
			String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);			
			if (cabang.equals("40")){
				espajdmtm3(this.getServletContext(), reg_spaj, request, 0, elionsManager, uwManager, bacManager, props, products);
			}else{
				espajonlinegadget(request, response, elionsManager, uwManager,bacManager, props, products, reg_spaj);				
			}
		}
		
		Connection conn = null;
		try {
			conn = uwManager.getUwDao().getDataSource().getConnection();			
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String jpu = ServletRequestUtils.getStringParameter(request, "seq", "");
			Integer punyaSimascard = 0;
			String flagUlink ="0";
			
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String detbisnis = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");			
			String lus_id = currentUser.getLus_id();
		
			Boolean listExclude = false;
			
//			Boolean listExclude = null;
//					
//			listExclude = ( ((lsbs.equals("212")) &&  ("002,003,005,006,007,008,010".indexOf(detbisnis) > 0))
//					|| ((lsbs.equals("175")) && ("001,002".indexOf(detbisnis) > 0))
//					|| ((lsbs.equals("142")) && (detbisnis.equals("002")))
//					|| ((lsbs.equals("223")) && (detbisnis.equals("001"))) );
//
//			if (listExclude){
//				paramsPolis = printPolisExclude(servletContext, spaj, request, PrintPolisMultiController.POLIS_QUADRUPLEX, elionsManager, uwManager, bacManager, props, products);
//			}else{
//				//1. Polis
//				paramsPolis = printPolis(servletContext, spaj, request, PrintPolisMultiController.POLIS_QUADRUPLEX, elionsManager, uwManager, bacManager, props, products);
//			}

			//1. Polis
			paramsPolis = printPolis(spaj, request, PrintPolisMultiController.POLIS_QUADRUPLEX, elionsManager, uwManager, bacManager, props, products);
			
			//2. Manfaat
			Integer businessNumber = uwManager.selectBusinessNumber(spaj);
			Map paramsManfaat = elionsManager.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
			String pathManfaat = (String) paramsManfaat.get("reportPath");
			pathManfaat = pathManfaat.substring(17);
			paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
			paramsManfaat.remove("reportPath");
			paramsManfaat.put("lsdbs", businessNumber);
			paramsPolis.putAll(paramsManfaat);
			
			paramsPolis.put("koneksi", conn);
			List temp = new ArrayList(); Map map = new HashMap();
			map.put("halaman", "1"); temp.add(map);//polis
			map.put("halaman", "2"); temp.add(map);//manfaat
			map.put("halaman", "3"); temp.add(map);//surat
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {
				map.put("halaman", "4"); temp.add(map);//kosong
				map.put("halaman", "5"); temp.add(map);//alokasi dana (bisa 1 / 2 halaman)
			}
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {//surat simas card
				map.put("halaman", "7"); temp.add(map);
			}else{
				map.put("halaman", "4"); temp.add(map);
			}
			map.put("halaman", "6"); temp.add(map);
			map.put("halaman", "8"); temp.add(map);
			map.put("halaman", "9"); temp.add(map);
			map.put("halaman", "10"); temp.add(map);
			paramsPolis.put("dsManfaat", JasperReportsUtils.convertReportData(temp));
			
			String businessId = FormatString.rpad("0", uwManager.selectBusinessId(spaj), 3);
			
			//3. Surat Polis
//			if("001,045,053,054,130,131,132".indexOf(businessId)==-1) {

				String va = uwManager.selectVirtualAccountSpaj(spaj);
				if(cabang.equals("58")){
					if(elionsManager.selectValidasiTransferPbp(spaj)==0){//ini mste_flag_cc
						if(va==null){
							uwManager.updateVirtualAccountBySpaj(spaj);
						}
					}
				}
				
				Map data 		= uwManager.selectDataVirtualAccount(spaj);
				int lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
				String lku_id 	= (String) data.get("LKU_ID");
				
				Integer flag_cc = elionsManager.select_flag_cc(spaj);
				
//				if(va != null){
				if(lku_id.equals("01") && !products.syariah(businessId,businessNumber.toString()) && lscb_id != 0 && flag_cc ==0 && !businessId.equals("196") && !(businessId.equals("217") && businessNumber ==2)&& !(businessId.equals("190") && "5,6".indexOf(businessNumber.toString())>=0)){
//				if(flag_cc ==0){
					
					List cekSpajPromo = bacManager.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
					
					if(!cekSpajPromo.isEmpty()){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va_promo") + ".jasper");
					}else if(businessId.equals("217")&& businessNumber==2){ //untuk ERbe di set menggunakan surat_polis
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
					}
					
				//	paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
				}else if(products.syariah(businessId, businessNumber.toString())){
					if(flag_cc==0 && !businessId.equals("175")){
						if(businessId.equals("215")&& businessNumber==003){
							paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
						}else{
							paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah_va") + ".jasper");
						}
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
					}
				}else if(cabang.equals("58")){
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.mall") + ".jasper");
				}
				else{
					if(businessId.equals("196")&& businessNumber==002){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.term") + ".jasper");
					}else{
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}
				}
				
				paramsPolis.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid (Yusuf - 04/05/2006)
//			}
			
			//4. Alokasi Dana
			List viewUlink = elionsManager.selectViewUlink(spaj);
			if(viewUlink.size()!=0){
				Map paramsAlokasi = elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 1, 1,0);
				paramsAlokasi.put("elionsManager", elionsManager);
				
				String pathAlokasi = (String) paramsAlokasi.get("reportPath");
				pathAlokasi = pathAlokasi.substring(17);
				paramsAlokasi.put("pathAlokasiDana", pathAlokasi + ".jasper");
				paramsAlokasi.remove("reportPath");
				paramsAlokasi.put("dsAlokasiDana", JasperReportsUtils.convertReportData(viewUlink));
				
				paramsPolis.putAll(paramsAlokasi);
			}
			
		
			//5 Tanda Terima Polis
			Integer referal = ServletRequestUtils.getIntParameter(request, "referal", 0);
			
//			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs_id = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
//			String lsbs_id = FormatString.rpad("0", uwManager.selectBusinessId(spaj), 3);
			String reportPath;
			String namaFile = props.getProperty("pdf.tanda_terima_polis");
			int isInputanBank = elionsManager.selectIsInputanBank(spaj); 
			if(((isInputanBank == 2 || isInputanBank == 3) && referal == 0 && !products.productBsmFlowStandardIndividu(Integer.parseInt(lsbs_id), Integer.parseInt(lsdbs))) || (lsbs_id.equals("175")&&lsdbs.equals("002"))) {
				reportPath = props.getProperty("report.tandaterimasertifikat");
				namaFile = props.getProperty("pdf.tanda_terima_sertifikat");
			}else if(products.unitLink(lsbs_id) && !products.stableLink(lsbs_id)) {
				if(products.syariah(lsbs_id, lsdbs)){
					reportPath = props.getProperty("report.tandaterimapolis.syariah");
				}else{
					reportPath = props.getProperty("report.tandaterimapolis.link");
				}
			}else if(lsbs_id.equals("187")){
				reportPath = props.getProperty("report.tandaterimapolis.pas");
			}else {
				reportPath = props.getProperty("report.tandaterimapolis"); //ini udah include biasa + syariah
			}
			
			paramsPolis.put("referal", referal);
			paramsPolis.put("pathTandaTerimaPolis", reportPath + ".jasper");
			
			 	
			if(flagPrePrinted==2){
				
				List daftarSebelumnya = uwManager.selectSimasCard(spaj);
				List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj);
				
				if(!daftarSebelumnya.isEmpty() && isAgen.isEmpty()){
					Map SimasCardSebelumnya = (Map) daftarSebelumnya.get(0);
					if(!Common.isEmpty(SimasCardSebelumnya.get("REG_SPAJ"))){
						if(SimasCardSebelumnya.get("REG_SPAJ").equals(spaj)){
							punyaSimascard=1;
						}else{
							elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO SPAJ"+ SimasCardSebelumnya.get("REG_SPAJ"), spaj, 0);
						}
					}
				}	
					
			}
			String reportPathSimasCard = props.get("report.surat_simcard").toString();
			paramsPolis.put("pathSuratSimasCard", reportPathSimasCard + ".jasper");
			
			for(Iterator iter = paramsPolis.keySet().iterator(); iter.hasNext();){
				String nama = (String) iter.next();
				logger.info(nama + " = " + paramsPolis.get(nama));
			}
			
			//SSU-SSK	
			//update 7 September 2018 Mark Valentino SSU-SSK diikutsertakan
			if(	PDFViewer.checkFileProduct(elionsManager, uwManager, props, spaj));{
				List<File> pdfFiles = new ArrayList<File>();
				
//				String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
				String exportDirTemp = props.getProperty("pdf.dir.export.temp")+"\\"+spaj;
				
		        File exportDirTemp1 = new File(props.getProperty("pdf.dir.export.temp")+"\\"+spaj);					
		        if(!exportDirTemp1.exists()) {
		        	exportDirTemp1.mkdirs();
		        }
		        File fileDirZip = new File(exportDirTemp1 + "\\" + spaj);		        
		        if(!fileDirZip.exists()) {
		        	fileDirZip.mkdirs();
		        }
		        
			    String dirSsk = props.getProperty("pdf.dir.syaratpolis");
				PDFMergerUtility SskBefore = new PDFMergerUtility();
				File file = null;
		    	File fileSsk1 = null;				
			    for(int i=0; i<detBisnis.size(); i++) {
					Map m = (HashMap) detBisnis.get(i);
					//Mark Valentino 20180906
					//SSU
					Integer lsbsId = Integer.parseInt(m.get("BISNIS").toString());
					if(lsbsId <= 300){					
						File fileSSU = PDFViewer.productFile(elionsManager, uwManager, dirSsk, spaj, m, props);
						if(fileSSU!=null) if(fileSSU.exists()) {
							pdfFiles.add(fileSSU);
							String pathSsu = fileSSU.toString();
							FileUtil.copyfile(pathSsu, fileDirZip.toString() + "\\" + "6. ssu.pdf");
						}
					}	
					//SSK					
					if ((lsbsId > 800) && (lsbsId < 900)) {
						if (i == 1) {
							fileSsk1 = PDFViewer.riderFile(elionsManager,uwManager, dirSsk, lsbs_id, lsdbs, m, spaj,props);
							if (fileSsk1 != null){
								if (fileSsk1.exists()){
									SskBefore.addSource(fileSsk1);
									pdfFiles.add(fileSsk1);
									System.out.println("File SSK Rider pertama telah ditambahkan.");											
								}
							}	

						}else if (i >= 2) {
							File fileSskN = PDFViewer.riderFile(elionsManager,uwManager, dirSsk, lsbs_id, lsdbs, m, spaj,props);

							// Jika file name SSK ke-2 berbeda dgn filename SSK pertama, maka diambil juga									
							if (!fileSsk1.getName().toString().equals(fileSskN.getName().toString())){									
								if (fileSskN != null){
									if (fileSskN.exists()){
											SskBefore.addSource(fileSskN);
											pdfFiles.add(fileSskN);
											System.out.println("File SSK Rider ke-"+ i + " telah ditambahkan.");												
										}
									}
							}
						}else {
							file = null;
						}
						// if(file!=null) if(file.exists()) {
						// String pathSsk = file.toString();
						// SskBefore.addSource(file);
						// FileUtil.copyfile(pathSsk, fileDirZip.toString() +
						// "\\" + "7. ssk.pdf");

						// }
					}

				}
				SskBefore.setDestinationFileName(fileDirZip.toString() + "\\"
						+ "7. ssk.pdf");

				SskBefore.mergeDocuments();
				//pdfFiles.add(SskBefore);
				// PdfUtils.combinePdf(pdfFiles, exportDirectory, "pathSS.pdf");
				PdfUtils.combinePdf(pdfFiles, exportDirTemp, "pathSS.pdf");

			}
				
			//ENDORS SMILE MEDICAL
			
			if(mspoProvider==2){
				PrintPolisPerjanjianAgent printPolis = new PrintPolisPerjanjianAgent();
				List<String> pdfs = new ArrayList<String>();
				Boolean suksesMerge = false;
				Boolean scFile=false;
				String endorsPolis = "";
				String Kartuadmedika = "";
				String Pesertaadmedika = "";
				String provider = "";
				String filename = "pathAdmedika";
				Date sysdate = elionsManager.selectSysdate();
				Boolean syariah = products.syariah(lsbs_id, lsdbs);
				
				Integer ekaSehatBaru = uwManager.selectCountEkaSehatAdmedikaNew(spaj,0);
				Integer ekaSehatHCP = uwManager.selectCountEkaSehatAdmedikaHCP(spaj);
				Integer ekaSehatPlus = uwManager.selectCountEkaSehatAdmedikaNew(spaj,1);
				Integer s=Integer.parseInt(lsdbs.substring(1));
				Integer punyaEndorsAdmedika = bacManager.selectPunyaEndorsEkaSehatAdmedika(spaj);
				
				if(punyaEndorsAdmedika == 0)bacManager.prosesEndorsKetinggalanNew(spaj, Integer.parseInt(lsbs_id));
				
				if(ekaSehatBaru>=1){
					if(lsbs_id.equals("189")|| products.syariah(lsbs_id, lsdbs)){
						if(lsbs_id.equals("189") && Integer.parseInt(lsdbs.substring(1))>15){
							endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariahSMP.pdf";
						}else{
							endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariah.pdf";
						}
					}else{
						endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedical.pdf"; // EndorsemenPolisBaru
					}
				}else if(ekaSehatPlus>=1){				
					endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedicalPlus.pdf";
				}else{
					endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolis.pdf";
				}
				if(ekaSehatHCP>=1 && ekaSehatBaru<1 && ekaSehatPlus<1){
					Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuHCP.pdf";
					provider = props.getProperty("pdf.template.admedika")+"\\ProviderHCP.pdf";
				}else{
					Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuAdmedika.pdf";
					provider = props.getProperty("pdf.template.admedika")+"\\Provider.pdf";
				}
				Pesertaadmedika = props.getProperty("pdf.template.admedika")+"\\PesertaAdmedika.pdf";
				
				if (ekaSehatBaru>=1 || ekaSehatPlus>=1){
					pdfs.add(endorsPolis);
				}
				if(flagPrePrinted==1){
					//pdfs.add(Kartuadmedika);
					//pdfs.add(provider);
				}
				Integer total_ekasehat = uwManager.getJumlahEkaSehat(spaj);
				List<Map> datapeserta = uwManager.selectDataPeserta(spaj);
				
				
				if(ekaSehatHCP>=1){
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaHCP.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}else if(lsbs_id.equals("189") || products.syariah(lsbs_id, lsdbs)){
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatSyariah.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}else if(ekaSehatPlus>=1){
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaMedicalPlus.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}else{
					// Andhika
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatNew.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);					
				}
				
				//File userDirTemp = new File(props.getProperty("pdf.dir.export.temp")+"\\"+cabang+"\\"+spaj);
				File userDirTemp = new File(props.getProperty("pdf.dir.export.temp")+"\\"+spaj);				
		        if(!userDirTemp.exists()) {
		        	userDirTemp.mkdirs();
		        }
				
				//String outputName = props.getProperty("pdf.dir.export.temp")+"\\"+cabang+"\\"+spaj+"\\"+filename+".pdf";
		        //outputName = null;
			    String outputName = props.getProperty("pdf.dir.export.temp")+"\\"+spaj+"\\"+filename+".pdf";
				
				if(!scFile) {
					Map dataAdmedika = uwManager.selectDataAdmedika(spaj);
					String ingrid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
					printPolis.generateEndorseAdmedikaEkaSehat(spaj,total_ekasehat, outputName, dataAdmedika, datapeserta, sysdate, ingrid,lsbs_id,lsdbs,syariah,ekaSehatHCP,ekaSehatBaru,ekaSehatPlus);
				}
			}
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {
				flagUlink="1";
			}
			
			//randy - direct print e-spaj dmtm (req. timmy) - 07/09/2016
			//Mark Valentino 20181009 direct print e-spaj ditaruh di atas
//			if (cabang.equals("40")){
//				espajdmtm3(servletContext, spaj, request, 0, elionsManager, uwManager, bacManager, props, products);
//			}
			
				String path ="";
				String pathTemp = "";
				path = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
				pathTemp = props.getProperty("pdf.dir.export.temp")+"\\"+spaj;
				String pathTemplate1 = props.getProperty("pdf.template")+"\\BlankPaper.pdf";
				String pathTemplate2 = props.getProperty("pdf.template")+"\\BlankPaper2.pdf";				
				try {
					JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathSurat").toString(), pathTemp , "pathSurat.pdf", paramsPolis, conn);
					JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathManfaat").toString(),pathTemp , "pathManfaat.pdf",paramsPolis, conn);
					if (!listExclude){
						JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathPolis").toString(),pathTemp , "pathPolis.pdf", paramsPolis, conn);
					}
					JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathTandaTerimaPolis").toString(),pathTemp, "pathTandaTerimaPolis.pdf", paramsPolis, conn);
					if(viewUlink.size()!=0)JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathAlokasiDana").toString(),pathTemp, "pathAlokasiDana.pdf", paramsPolis, conn);
					if(flagPrePrinted==2){
						List daftarSebelumnya = uwManager.selectSimasCard(spaj);
						List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj);
						
						if((!daftarSebelumnya.isEmpty()) && isAgen.isEmpty()){
							String spaj2 = (String) ((Map) daftarSebelumnya.get(0)).get("REG_SPAJ");
							if(spaj2.equals(spaj)){
								JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathSuratSimasCard").toString(),pathTemp, "pathSuratSimasCard.pdf", paramsPolis, conn);
							}
						}
					}
					
					CekPelengkap.generateFileRDS(cabang,pathTemp,path,pathTemplate1,pathTemplate2,lsbs_id,mspoProvider,flagUlink,punyaSimascard,flagPrePrinted,request,props,elionsManager);
				}catch(Exception e){
			        throw e;
				}
	}finally{
				closeConnection(conn);
		}
	}
	
	// Menyesuaikan request Tim UW Printing
	public void generateReport3(HttpServletRequest request,Integer mspoProvider, int flagPrePrinted, ElionsManager elionsManager, UwManager uwManager, BacManager bacManager, Properties props, Products products) throws Exception{
		
		Connection conn = null;
		try {
			//conn = getDataSource().getConnection();
			conn = uwManager.getUwDao().getDataSource().getConnection();
			//conn = getUwManager().getUwDao().getDataSource().getConnection();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String jpu = ServletRequestUtils.getStringParameter(request, "seq", "");
			Integer punyaSimascard = 0;
			String flagUlink ="0";

			//1. Polis
			Map paramsPolis = printPolis(spaj, request, PrintPolisMultiController.POLIS_QUADRUPLEX, elionsManager, uwManager, bacManager, props, products);
			
			//2. Manfaat
			Integer businessNumber = uwManager.selectBusinessNumber(spaj);
			Map paramsManfaat = elionsManager.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
			String pathManfaat = (String) paramsManfaat.get("reportPath");
			pathManfaat = pathManfaat.substring(17);
			paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
			paramsManfaat.remove("reportPath");
			paramsManfaat.put("lsdbs", businessNumber);
			paramsPolis.putAll(paramsManfaat);
			
			paramsPolis.put("koneksi", conn);
			List temp = new ArrayList(); Map map = new HashMap();
			map.put("halaman", "1"); temp.add(map);//polis
			map.put("halaman", "2"); temp.add(map);//manfaat
			map.put("halaman", "3"); temp.add(map);//surat
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {
				map.put("halaman", "4"); temp.add(map);//kosong
				map.put("halaman", "5"); temp.add(map);//alokasi dana (bisa 1 / 2 halaman)
			}
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {//surat simas card
				map.put("halaman", "7"); temp.add(map);
			}else{
				map.put("halaman", "4"); temp.add(map);
			}
			map.put("halaman", "6"); temp.add(map);
			map.put("halaman", "8"); temp.add(map);
			map.put("halaman", "9"); temp.add(map);
			map.put("halaman", "10"); temp.add(map);
			paramsPolis.put("dsManfaat", JasperReportsUtils.convertReportData(temp));
			
			String businessId = FormatString.rpad("0", uwManager.selectBusinessId(spaj), 3);
			
			//3. Surat Polis
				String va = uwManager.selectVirtualAccountSpaj(spaj);
				if(cabang.equals("58")){
					if(elionsManager.selectValidasiTransferPbp(spaj)==0){//ini mste_flag_cc
						if(va==null){
							uwManager.updateVirtualAccountBySpaj(spaj);
						}
					}
				}
				
				Map data 		= uwManager.selectDataVirtualAccount(spaj);
				Integer lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
				String lku_id 	= (String) data.get("LKU_ID");
				
				Integer flag_cc = elionsManager.select_flag_cc(spaj);
				
				if(lku_id.equals("01") && !products.syariah(businessId,businessNumber.toString()) && lscb_id != 0 && flag_cc ==0 && !businessId.equals("196")&& !(businessId.equals("190") && "5,6".indexOf(businessNumber.toString())>=0) ){
					
					List cekSpajPromo = bacManager.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
					
					if(!cekSpajPromo.isEmpty()){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va_promo") + ".jasper");
					}else if(businessId.equals("217")&& businessNumber== 2){ //untuk ERbe di set menggunakan surat_polis
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
					}		
					
					//paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
				}else if(products.syariah(businessId, businessNumber.toString())){
					if(flag_cc==0 && !businessId.equals("175")){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah_va") + ".jasper");
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
					}
				}else if(cabang.equals("58")){
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.mall") + ".jasper");
				}
				else{
					if(businessId.equals("196")&& businessNumber==002){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.term") + ".jasper");
					}else{
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}
				}
				
				paramsPolis.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid (Yusuf - 04/05/2006)

			
			//4. Alokasi Dana
			List viewUlink = elionsManager.selectViewUlink(spaj);
			if(viewUlink.size()!=0){
				Map paramsAlokasi = elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 1, 1,0);
				paramsAlokasi.put("elionsManager", elionsManager);
				
				String pathAlokasi = (String) paramsAlokasi.get("reportPath");
				pathAlokasi = pathAlokasi.substring(17);
				paramsAlokasi.put("pathAlokasiDana", pathAlokasi + ".jasper");
				paramsAlokasi.remove("reportPath");
				paramsAlokasi.put("dsAlokasiDana", JasperReportsUtils.convertReportData(viewUlink));
				
				paramsPolis.putAll(paramsAlokasi);
			}
			
		
			//5 Tanda Terima Polis
			Integer referal = ServletRequestUtils.getIntParameter(request, "referal", 0);
			
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs_id = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
//			String lsbs_id = FormatString.rpad("0", uwManager.selectBusinessId(spaj), 3);
			String reportPath;
			String namaFile = props.getProperty("pdf.tanda_terima_polis");
			int isInputanBank = elionsManager.selectIsInputanBank(spaj); 
			if(((isInputanBank == 2 || isInputanBank == 3) && referal == 0 && !products.productBsmFlowStandardIndividu(Integer.parseInt(lsbs_id), Integer.parseInt(lsdbs))) || (lsbs_id.equals("175")&&lsdbs.equals("002"))) {
				reportPath = props.getProperty("report.tandaterimasertifikat");
				namaFile = props.getProperty("pdf.tanda_terima_sertifikat");
			}else if(products.unitLink(lsbs_id) && !products.stableLink(lsbs_id)) {
				if(products.syariah(lsbs_id, lsdbs)){
					reportPath = props.getProperty("report.tandaterimapolis.syariah");
				}else{
					reportPath = props.getProperty("report.tandaterimapolis.link");
				}
			}else if(lsbs_id.equals("187")){
				reportPath = props.getProperty("report.tandaterimapolis.pas");
			}else {
				reportPath = props.getProperty("report.tandaterimapolis"); //ini udah include biasa + syariah
			}
			
			paramsPolis.put("referal", referal);
			paramsPolis.put("pathTandaTerimaPolis", reportPath + ".jasper");
			
			 	
			if(flagPrePrinted==2){
				
				List daftarSebelumnya = uwManager.selectSimasCard(spaj);
				List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj);
				
				if(!daftarSebelumnya.isEmpty() && isAgen.isEmpty()){
					Map SimasCardSebelumnya = (Map) daftarSebelumnya.get(0);
					if(!Common.isEmpty(SimasCardSebelumnya.get("REG_SPAJ"))){
						if(SimasCardSebelumnya.get("REG_SPAJ").equals(spaj)){
							punyaSimascard=1;
						}else{
							elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO SPAJ"+ SimasCardSebelumnya.get("REG_SPAJ"), spaj, 0);
						}
					}
				}	
					
			}
			String reportPathSimasCard = props.get("report.surat_simcard").toString();
			paramsPolis.put("pathSuratSimasCard", reportPathSimasCard + ".jasper");
			
			for(Iterator iter = paramsPolis.keySet().iterator(); iter.hasNext();){
				String nama = (String) iter.next();
				logger.info(nama + " = " + paramsPolis.get(nama));
			}
			
			//SSU-SSK	
			//update 7 September 2018 Mark Valentino SSU-SSK diikutsertakan
			if(	PDFViewer.checkFileProduct(elionsManager, uwManager, props, spaj));{
				List<File> pdfFiles = new ArrayList<File>();
				
				String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
				String exportDirTemp = props.getProperty("pdf.dir.export.temp")+"\\"+spaj;
				
		        File exportDirTemp1 = new File(props.getProperty("pdf.dir.export.temp")+"\\"+spaj);					
		        if(!exportDirTemp1.exists()) {
		        	exportDirTemp1.mkdirs();
		        }					
				
			    String dirSsk = props.getProperty("pdf.dir.syaratpolis");					
			    for(int i=0; i<detBisnis.size(); i++) {
					Map m = (HashMap) detBisnis.get(i);
					//Mark Valentino 20180906
					File fileSSU = PDFViewer.productFile(elionsManager, uwManager, dirSsk, spaj, m, props);
					File file = PDFViewer.riderFile(elionsManager,uwManager, dirSsk, lsbs_id, lsdbs, m,spaj, props);
					if(fileSSU!=null) if(fileSSU.exists()) pdfFiles.add(fileSSU);						
					if(file!=null) if(file.exists()) pdfFiles.add(file);
				}
				//PdfUtils.combinePdf(pdfFiles, exportDirectory, "pathSS.pdf");
				PdfUtils.combinePdf(pdfFiles, exportDirTemp, "pathSS.pdf");
			}	
				
			//ENDORS SMILE MEDICAL
			
			if(mspoProvider==2){
				PrintPolisPerjanjianAgent printPolis = new PrintPolisPerjanjianAgent();
				List<String> pdfs = new ArrayList<String>();
				Boolean suksesMerge = false;
				Boolean scFile=false;
				String endorsPolis = "";
				String Kartuadmedika = "";
				String Pesertaadmedika = "";
				String provider = "";
				String filename = "pathAdmedika";
				Date sysdate = elionsManager.selectSysdate();
				Boolean syariah = products.syariah(lsbs_id, lsdbs);
				
				Integer ekaSehatBaru = uwManager.selectCountEkaSehatAdmedikaNew(spaj,0);
				Integer ekaSehatHCP = uwManager.selectCountEkaSehatAdmedikaHCP(spaj);
				Integer ekaSehatPlus = uwManager.selectCountEkaSehatAdmedikaNew(spaj,1);
				Integer s=Integer.parseInt(lsdbs.substring(1));
				Integer punyaEndorsAdmedika = bacManager.selectPunyaEndorsEkaSehatAdmedika(spaj);
				
				if(punyaEndorsAdmedika == 0)bacManager.prosesEndorsKetinggalanNew(spaj, Integer.parseInt(lsbs_id));
				
				if(ekaSehatBaru>=1){
					if(lsbs_id.equals("189")|| products.syariah(lsbs_id, lsdbs)){
						if(lsbs_id.equals("189") && Integer.parseInt(lsdbs.substring(1))>15){
							endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariahSMP.pdf";
						}else{
							endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariah.pdf";
						}
					}else{
						endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedical.pdf"; // EndorsemenPolisBaru
					}
				}else if(ekaSehatPlus>=1){				
					endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedicalPlus.pdf";
				}else{
					endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolis.pdf";
				}
				if(ekaSehatHCP>=1 && ekaSehatBaru<1 && ekaSehatPlus<1){
					Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuHCP.pdf";
					provider = props.getProperty("pdf.template.admedika")+"\\ProviderHCP.pdf";
				}else{
					Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuAdmedika.pdf";
					provider = props.getProperty("pdf.template.admedika")+"\\Provider.pdf";
				}
				Pesertaadmedika = props.getProperty("pdf.template.admedika")+"\\PesertaAdmedika.pdf";
				
				if (ekaSehatBaru>=1 || ekaSehatPlus>=1){
					pdfs.add(endorsPolis);
				}
				if(flagPrePrinted==1){
					//pdfs.add(Kartuadmedika);
					//pdfs.add(provider);
				}
				Integer total_ekasehat = uwManager.getJumlahEkaSehat(spaj);
				List<Map> datapeserta = uwManager.selectDataPeserta(spaj);
				
				
				if(ekaSehatHCP>=1){
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaHCP.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}else if(lsbs_id.equals("189") || products.syariah(lsbs_id, lsdbs)){
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatSyariah.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}else if(ekaSehatPlus>=1){
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaMedicalPlus.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}else{
					// Andhika
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatNew.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}
				
				//File userDirTemp = new File(props.getProperty("pdf.dir.export.temp")+"\\"+cabang+"\\"+spaj);
				File userDirTemp = new File(props.getProperty("pdf.dir.export.temp")+"\\"+spaj);				
		        if(!userDirTemp.exists()) {
		        	userDirTemp.mkdirs();
		        }
				
				//String outputName = props.getProperty("pdf.dir.export.temp")+"\\"+cabang+"\\"+spaj+"\\"+filename+".pdf";
				String outputName = props.getProperty("pdf.dir.export.temp")+"\\"+spaj+"\\"+filename+".pdf";				
				
				if(!scFile) {
					Map dataAdmedika = uwManager.selectDataAdmedika(spaj);
					String ingrid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
					printPolis.generateEndorseAdmedikaEkaSehat(spaj,total_ekasehat, outputName, dataAdmedika, datapeserta, sysdate, ingrid,lsbs_id,lsdbs,syariah,ekaSehatHCP,ekaSehatBaru,ekaSehatPlus);
				}
			}
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {
				flagUlink="1";
			}
			
//			//randy - direct print e-spaj dmtm (req. timmy) - 07/09/2016
//			if (cabang.equals("40")){
//				espajdmtm3(spaj);
//			}
			
				String path ="";
				String pathTemp = "";
				path = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
				pathTemp = props.getProperty("pdf.dir.export.temp")+"\\"+spaj;
				String pathTemplate1 = props.getProperty("pdf.template")+"\\BlankPaper.pdf";
				String pathTemplate2 = props.getProperty("pdf.template")+"\\BlankPaper2.pdf";				
				try {
					JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathSurat").toString(), pathTemp , "pathSurat.pdf", paramsPolis, conn);
					JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathManfaat").toString(),pathTemp , "pathManfaat.pdf",paramsPolis, conn);
					JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathPolis").toString(),pathTemp , "pathPolis.pdf", paramsPolis, conn);
					JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathTandaTerimaPolis").toString(),pathTemp, "pathTandaTerimaPolis.pdf", paramsPolis, conn);
					if(viewUlink.size()!=0)JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathAlokasiDana").toString(),pathTemp, "pathAlokasiDana.pdf", paramsPolis, conn);
					if(flagPrePrinted==2)JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathSuratSimasCard").toString(),pathTemp, "pathSuratSimasCard.pdf", paramsPolis, conn);
					//Mark Valentino 20180908 Buat 2 file : PolisAll & Pelengkap
					CekPelengkap.generate2File(cabang,pathTemp,path,pathTemplate1,pathTemplate2,lsbs_id,mspoProvider,flagUlink,punyaSimascard,flagPrePrinted,request,props);
				}catch(Exception e){
			        throw e;
				}
		}finally{
				closeConnection(conn);
		}
	}
	
	
	/** PRINT POLIS **/
	private Map printPolis(String spaj, HttpServletRequest request, int singleDuplexQuadruplex, ElionsManager elionsManager, UwManager uwManager, BacManager bacManager, Properties props, Products products) throws Exception {
		Map params = new HashMap();
		User currentUser = new User();
		//20190216 Mark Valentino - request == null artinya dari Scheduler
		if(request == null){
			currentUser.setJn_bank(0);
		}else{
			currentUser = (User) request.getSession().getAttribute("currentUser");
		}
		params.put("spaj", spaj);
		params.put("props", props);
		String validasiMeterai = uwManager.validasiBeaMeterai(currentUser.getJn_bank());
		if(validasiMeterai != null){
			params.put("meterai", null);
			params.put("izin", "");
		}else{
			params.put("meterai", "Rp. 6.000,-");
			params.put("izin", elionsManager.selectIzinMeteraiTerakhir());
		}
		
		//jenis print ulang
		int jpu;
		if(request == null){
			jpu = -1;
		}else{
			jpu = ServletRequestUtils.getIntParameter(request, "jpu", -1);
		}	
		
		if(jpu == 1) { //PRINT ULANG POLIS
			params.put("tipePolis", "O R I G I N A L");
		}else if(jpu == 2) { //PRINT DUPLIKAT POLIS
			String seq = ServletRequestUtils.getStringParameter(request, "seq", "");
			params.put("tipePolis", "D U P L I C A T E " + "("+seq+")");
		}else if(jpu == 3) { //PRINT ULANG POLIS
			params.put("tipePolis", "O R I G I N A L");
		}
		params.put("ingrid", props.get("images.ttd.direksi")); //ttd dr. ingrid (Yusuf - 04/05/2006)
		
		String kategori = products.kategoriPrintPolis(spaj);
		String policyNumber = "09208201800001";
		 	   policyNumber = uwManager.getUwDao().selectNoPolisFromSpaj(spaj);
		PrintPolisPrintingController pppc = new PrintPolisPrintingController();
		String logoQr = pppc.getQrBasedOnPolicy(this.getServletContext(),props,policyNumber, spaj, bacManager);
		
		if( !logoQr.contains("09208201800001") ){
			params.put("Print", "cetak");
		}			
		params.put("logoQr", logoQr);			
		
		String context = this.getServletContext().getRealPath("/");
		params.put("context", context);
		
		//String kategori = "guthrie";
		logger.info("JENIS POLIS : " + kategori);	
		if(PrintPolisMultiController.POLIS == singleDuplexQuadruplex) { //single
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis."+kategori));
		}else if(PrintPolisMultiController.POLIS_DUPLEX == singleDuplexQuadruplex) { //duplex(cetak BOLAK-BALIK)
			params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.duplex"));
		}else if(PrintPolisMultiController.POLIS_QUADRUPLEX == singleDuplexQuadruplex) { //quadruplex
			params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex"));
		}else if(PrintPolisMultiController.POLIS_QUADRUPLEX_PLUS_HADIAH == singleDuplexQuadruplex) { //quadruplex
			params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex_plus_hadiah"));
		}else if(PrintPolisMultiController.POLIS_QUADRUPLEX_MEDICAL_PLUS== singleDuplexQuadruplex) { //quadruplex				
			params.put("pathPolis", props.getProperty("report.polis."+kategori+".medplus")+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex_medical_plus"));
		}
		return params;
	}
			
	//Mark Valentino 20181030 : dicopy dari class 'PrintPolisPrintingController.java'
	//Jika ada perubahan di 'PrintPolisPrintingController.java' mohon diupdate jg disini.
	public void espajdmtm3(ServletContext servletContext, String reg_spaj, HttpServletRequest request, int singleDuplexQuadruplex, ElionsManager elionsManager, UwManager uwManager, BacManager bacManager, Properties props, Products products) throws Exception {
		
		Date sysdate = elionsManager.selectSysdate();

		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
//		String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
		String exportDirectory = props.getProperty("pdf.dir.export.temp")+"\\"+reg_spaj;		
		Integer data2 = uwManager.selectFlagQuestionare(reg_spaj);
		String dir = props.getProperty("pdf.template.espajdmtm2");
//		File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
		File userDir = new File(props.getProperty("pdf.dir.export.temp")+"\\"+reg_spaj);		
        if(!userDir.exists()) {
            userDir.mkdirs();
        }
        String espajFile = dir + "\\espaj_"+reg_spaj+".pdf";
        
        HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "DMTM");
		moreInfo.put("Subject", "E-SPAJ DMTM DANA SEJAHTERA");
		
		PdfContentByte over;
		BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		
        PdfReader reader = new PdfReader(props.getProperty("pdf.template.espajdmtm2")+"\\espajdmtm2.pdf");
//      String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espajDMTM2_"+reg_spaj+".pdf";
        String outputName = props.getProperty("pdf.dir.export.temp")+"\\"+reg_spaj+"\\"+"espajDMTM2_"+reg_spaj+".pdf"; 
        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
        
        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
        AddressBilling addrBill = elionsManager.selectAddressBilling(reg_spaj);
        Datausulan dataUsulan = elionsManager.selectDataUsulanutama(reg_spaj);
        InvestasiUtama inv  = elionsManager.selectinvestasiutama(reg_spaj);
        Rekening_client rekClient = elionsManager.select_rek_client(reg_spaj);
        Account_recur accRecur = elionsManager.select_account_recur(reg_spaj);//ada isinya
        List detInv = uwManager.selectdetilinvestasimallspaj(reg_spaj);
        List benef = elionsManager.select_benef(reg_spaj);
        List medQuest=uwManager.selectquestionareDMTM(reg_spaj);
        List peserta=uwManager.select_all_mst_peserta(reg_spaj);
        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
        List<MedQuest> mq = uwManager.selectMedQuest(reg_spaj,null);
        Agen agen =elionsManager.select_detilagen(reg_spaj);
        dataUsulan.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
        List namaBank =uwManager.namaBank(accRecur.getLbn_id());
		Map premiProdukUtama = elionsManager.selectPremiProdukUtama(reg_spaj);
		String kurs = (String) premiProdukUtama.get("LKU_ID");
        
        if(!dataUsulan.getLca_id().equals("40")){
//				ServletOutputStream sos = response.getOutputStream();
//    			sos.println("<script>alert('E-SPAJ DMTM , Hanya Untuk jalur distribusi DMTM');window.close();</script>");
//    			sos.close();
		}
        
        try {
        stamp.setMoreInfo(moreInfo);
		
		over = stamp.getOverContent(1);
		over.beginText();
		over.setFontAndSize(times_new_roman,7);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 90, 646, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getMspo_no_blanko())?"-":dataPP.getMspo_no_blanko().toUpperCase(), 400, 646, 0);
	    
	    over.setFontAndSize(times_new_roman,5);
	  //Data PP
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 160, 619, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_mother().toUpperCase(), 160, 606, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLside_name(), 160, 592, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_no_identity(), 246, 592, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getLsne_note())?"-":dataPP.getLsne_note().toUpperCase(), 160, 578, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_place_birth().toUpperCase() + ", " + FormatDate.toIndonesian(dataPP.getMspe_date_birth()), 160, 565, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sex2().toUpperCase(), 160, 551, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataPP.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataPP.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 160, 537, 0);
	   
	    int monyong = 0;
    	String[] alamat = StringUtil.pecahParagraf(dataPP.getAlamat_rumah().toUpperCase(), 55);
    	for(int i=0; i<alamat.length; i++) {
    		monyong = 7 * i;
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							160, 527-monyong, 0);
    	}
    	
    	monyong = 0;
		if(!Common.isEmpty(dataPP.getAlamat_kantor())){
			String[] alamat_kantor =  StringUtil.pecahParagraf(dataPP.getAlamat_kantor().toUpperCase(), 55);
        	for(int i=0; i<alamat_kantor.length; i++) {
        		monyong = 7 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							160, 514-monyong, 0);
        	}
		}
    	
    	monyong = 0;
    	String[] alamat_billing = StringUtil.pecahParagraf(addrBill.getMsap_address(), 55);
    	for(int i=0; i<alamat_billing.length; i++) {
    		monyong = 7 * i;;
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							160, 500-monyong, 0);
    	}
    	
    	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getNo_hp())?"":dataPP.getNo_hp()) , 160, 482, 0);
    	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getEmail())?"":dataPP.getEmail()) , 160, 468, 0);
    	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getMkl_penghasilan())?"":dataPP.getMkl_penghasilan().toUpperCase()), 160, 455, 0);
    	over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_pendanaan().toUpperCase(), 160, 441, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_kerja().toUpperCase(), 160, 427, 0);
//		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_kerja(), 160, 427, 0);
		
		over.setFontAndSize(times_new_roman,6);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getLsre_relation().toUpperCase(), 290, 414, 0);
		
	if(dataPP.getLsre_id()!=1){
			over.setFontAndSize(times_new_roman,5);
    		//Data tertanggung
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first().toUpperCase(), 350, 619, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_mother().toUpperCase(), 350, 606, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLside_name(), 350, 592, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_no_identity(), 436, 592, 0);
		    if(dataTT.getLsne_note() == null){
		    	dataTT.setLsne_note("-");
		    }else{
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsne_note().toUpperCase(), 350, 578, 0);		    	
		    }
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_place_birth().toUpperCase() + ", " + FormatDate.toIndonesian(dataTT.getMspe_date_birth()), 350, 565, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sex2().toUpperCase(), 350, 551, 0);
		    if(dataTT.getMspe_sts_mrt() == null){
		    	dataTT.setMspe_sts_mrt("1");
		    }else{
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataTT.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataTT.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 350, 537, 0);
		    }

			 
		    monyong = 0;
	        	String[] alamat2 = StringUtil.pecahParagraf(dataTT.getAlamat_rumah().toUpperCase(), 55);
	        	for(int i=0; i<alamat.length; i++) {
	        		monyong = 7 * i;
	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							350, 527-monyong, 0);
	        	}
	        	monyong = 0;
	 			if(!Common.isEmpty(dataPP.getAlamat_kantor())){
	 				String[] alamat_kantor =  StringUtil.pecahParagraf(dataTT.getAlamat_rumah().toUpperCase()/*getAlamat_kantor().toUpperCase()*/, 55);
	 	        	for(int i=0; i<alamat_kantor.length; i++) {
	 	        		monyong = 7 * i;
	 	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							350, 514-monyong, 0);
	 	        	}
	 			}
	        	
	        	monyong = 0;
	        	String[] alamat_billing2 = StringUtil.pecahParagraf(addrBill.getMsap_address(), 55);
	        	for(int i=0; i<alamat_billing.length; i++) {
	        		monyong = 7 * i;
	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							350, 500-monyong, 0);
	        	}
	        	
	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getNo_hp())?"":dataTT.getNo_hp()) , 350, 482, 0);
	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getEmail())?"":dataTT.getEmail()) , 350, 468, 0);
	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getMkl_penghasilan())?"":dataTT.getMkl_penghasilan().toUpperCase()), 350, 455, 0);
	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_pendanaan().toUpperCase(), 350, 441, 0);
//	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_kerja().toUpperCase(), 350, 427, 0);
	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,StringUtils.defaultString(dataTT.getMkl_kerja()).toUpperCase(), 350, 427, 0);
    	}
	    		
	    		over.setFontAndSize(times_new_roman,6);
	    	//	if (dataTT.getMste_flag_cc()==1){
		    if (accRecur!=null)
		    	{
		    			String bank_pusat = "";
		    			String bank_cabang= "";
		    			
		    			if(!namaBank.isEmpty()){
		        			HashMap m = (HashMap)namaBank.get(0);
		        			bank_pusat = (String)m.get("LSBP_NAMA");
		        			bank_cabang = (String)m.get("LBN_NAMA");
		    			}
		    			
					over.setFontAndSize(times_new_roman,5);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(bank_pusat)?"":bank_pusat.toUpperCase()+" - "+(Common.isEmpty(bank_cabang)?"":bank_cabang.toUpperCase()), 110, 392, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(bank_cabang)?"":bank_cabang.toUpperCase()/*accRecur.getLbn_nama().toUpperCase()*/, 110, 381, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_acc_no())?"":accRecur.getMar_acc_no().toUpperCase(), 396, 392, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_holder())?"":accRecur.getMar_holder().toUpperCase(), 396, 381, 0);
		    		}
			//	}
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLsdbs_name(),155, 338, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())),379, 338, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())),520, 338, 0);//dataUsulan.getMspr_ins_period().toString()
		    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,dataUsulan.getMspr_ins_period().toString()+" Tahun",180, 324, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLscb_pay_mode(),162, 310, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),222, 299, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(dataPP.getMste_tgl_recur()), 162, 288, 0);

			//data manfaat yang ditunjuk
    		if(benef.size()>0){
			int j=0;
			for(int i=0;i<benef.size();i++){
			Benefeciary benefi = (Benefeciary) benef.get(i);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_first(),86, 236-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(benefi.getMsaw_sex()==1?"L":"P"),325, 236-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getSmsaw_birth(),375, 236-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getLsre_relation().toUpperCase(),463, 236-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_persen().toString(),532, 236-j, 0);
			j+=11;}
		}
    		int x=0;
			int y=0;
			int y1=0;
    	//Pertanyaan MEDIS 
			if (!mq.isEmpty()){
			if(mq.size()>2) {
//					for(int i=0;i<mq.size();i++){
		    		for(int i=0;i<2;i++){
						Integer kelamin = null;
						if(mq.get(i).getMste_insured_no()==0){
							kelamin = dataPP.getMspe_sex();
						}else if (mq.get(i).getMste_insured_no()==1){
							kelamin = dataTT.getMspe_sex();
						}
						
					// Questionare SPAJ ONLINE SYARIAH
						//Berat dan Tinggi, 1a, 1b
						over.setFontAndSize(times_new_roman,6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_berat().toString() +" Kg", 447+x, 198, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_tinggi().toString()+" Cm",447+x, 189, 0);
						//Pertanyaan 2
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_berat_berubah()==1?"YA" : "TIDAK"), 447+x, 180, 0);
						over.setFontAndSize(times_new_roman,5);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TT : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
						(Common.isEmpty(mq.get(i).getMsadm_berubah_desc())?"":mq.get(i).getMsadm_berubah_desc()), 76, 176-y, 0);
						//Pertanyaan 3
					    over.setFontAndSize(times_new_roman,6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,(mq.get(i).getMsadm_penyakit()==1?"YA" : "TIDAK"),447+x, 154, 0);
						over.setFontAndSize(times_new_roman,5);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
						(Common.isEmpty(mq.get(i).getMsadm_penyakit_desc())?"getMsadm_penyakit_desc":mq.get(i).getMsadm_penyakit_desc()), 76, 143-y, 0);
						//pertanyaan 4
						over.setFontAndSize(times_new_roman,6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_medis()==1?"YA" : "TIDAK"), 447+x, 122, 0);
						over.setFontAndSize(times_new_roman,5);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
						(Common.isEmpty(mq.get(i).getMsadm_medis_desc())?"getMsadm_medis_desc":mq.get(i).getMsadm_medis_desc()), 76, 97-y, 0);
						//Pertanyaan 5 Khusus Wanita
						if(kelamin==0){
							over.setFontAndSize(times_new_roman,6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMsadm_pregnant())?"TIDAK":(mq.get(i).getMsadm_pregnant()==1?"YA" : "TIDAK")),447+x, 76, 0);
							over.setFontAndSize(times_new_roman,5);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
							(Common.isEmpty(mq.get(i).getMsadm_pregnant_desc())?"":mq.get(i).getMsadm_pregnant_desc()), 76, 71-y1, 0);
							y1+=6;
						}
						x+=68;
						y+=9;
					}
			}else{
				for(int i=0;i<mq.size();i++){
//			    		for(int i=0;i<2;i++){
						Integer kelamin = null;
						if(mq.get(i).getMste_insured_no()==0){
							kelamin = dataPP.getMspe_sex();
						}else if (mq.get(i).getMste_insured_no()==1){
							kelamin = dataTT.getMspe_sex();
						}
						
					// Questionare SPAJ ONLINE SYARIAH
						//Berat dan Tinggi, 1a, 1b
						over.setFontAndSize(times_new_roman,6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_berat().toString() +" Kg", 447+x, 198, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_tinggi().toString()+" Cm",447+x, 189, 0);
						//Pertanyaan 2
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_berat_berubah()==1?"YA" : "TIDAK"), 447+x, 180, 0);
						over.setFontAndSize(times_new_roman,5);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TT : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
						(Common.isEmpty(mq.get(i).getMsadm_berubah_desc())?"":mq.get(i).getMsadm_berubah_desc()), 76, 176-y, 0);
						//Pertanyaan 3
					    over.setFontAndSize(times_new_roman,6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,(mq.get(i).getMsadm_penyakit()==1?"YA" : "TIDAK"),447+x, 154, 0);
						over.setFontAndSize(times_new_roman,5);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
						(Common.isEmpty(mq.get(i).getMsadm_penyakit_desc())?"getMsadm_penyakit_desc":mq.get(i).getMsadm_penyakit_desc()), 76, 143-y, 0);
						//pertanyaan 4
						over.setFontAndSize(times_new_roman,6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_medis()==1?"YA" : "TIDAK"), 447+x, 122, 0);
						over.setFontAndSize(times_new_roman,5);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
						(Common.isEmpty(mq.get(i).getMsadm_medis_desc())?"getMsadm_medis_desc":mq.get(i).getMsadm_medis_desc()), 76, 97-y, 0);
						//Pertanyaan 5 Khusus Wanita
						if(kelamin==0){
							over.setFontAndSize(times_new_roman,6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMsadm_pregnant())?"TIDAK":(mq.get(i).getMsadm_pregnant()==1?"YA" : "TIDAK")),447+x, 76, 0);
							over.setFontAndSize(times_new_roman,5);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
							(Common.isEmpty(mq.get(i).getMsadm_pregnant_desc())?"":mq.get(i).getMsadm_pregnant_desc()), 76, 71-y1, 0);
							y1+=6;
						}
						x+=68;
						y+=9;
					}
			}
			}
    		
    		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMcl_first(),140, 47, 0);
    		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMsag_id(),108, 47, 0);
	    over.endText();
	    reader.close();
		stamp.close();

//		in = new FileInputStream(l_file);
//	    ouputStream = response.getOutputStream();		
//		if(in != null) {
//			    in.close();
//		}
//		if(ouputStream != null) {
//			ouputStream.flush();
//		    ouputStream.close();
//		}		
		
        }catch (Exception e) {
			stamp.close();
//				logger.error("ERROR :", e);
			FileUtils.deleteFile(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj, "espajDMTM2_"+reg_spaj+".pdf");
			return;
		}
        
        
        //Method baca file
//		File l_file = new File(outputName);
//		try{
//			FileInputStream in = new FileInputStream(l_file);
//		    int length = in.available();
//		    byte[] pdfbytes = new byte[length];
//		    in.read(pdfbytes);
//		    in.close();
//
//		}catch (Exception e) {
//			logger.error("ERROR :", e);
//		}
        
	}	

	// SPAJ ONLINE UNTUK GADGET
	// 20181015 Mark Valentino : dicopy dari class
	// 'PrintPolisPrintingController.java'
	// Jika ada perubahan di 'PrintPolisPrintingController.java' mohon diupdate
	// jg disini.
	public void espajonlinegadget(HttpServletRequest request,
			HttpServletResponse response, ElionsManager elionsManager,
			UwManager uwManager, BacManager bacManager, Properties props,
			Products products, String spaj) throws Exception {
		// if(request == null){
		// spaj = uwManager.getNilaiTunai();
		// }else{
		// spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		// }
		String reg_spaj = spaj;
		Date sysdate = elionsManager.selectSysdate();
		List<String> pdfs = new ArrayList<String>();
		Boolean suksesMerge = false;
		spaj = "";

		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj;

		String dir = props.getProperty("pdf.template.espajonlinegadget");

		File userDir = new File(props.getProperty("pdf.dir.export.temp") + "\\"
				+ reg_spaj);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "GADGET");
		moreInfo.put("Subject", "E-SPAJ ONLINE");

		PdfContentByte over;
		BaseFont times_new_roman = BaseFont.createFont(
				"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);

		PdfReader reader = new PdfReader(
				props.getProperty("pdf.template.espajonlinegadget")
						+ "\\spajonlinegadget.pdf");
		OutputStream output = new FileOutputStream(userDir + "\\"
				+ "espajonlinegadget_" + reg_spaj + ".pdf");

		spaj = dir + "\\spajonlinegadget.pdf";
		pdfs.add(spaj);
		suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
		String outputName = props.getProperty("pdf.dir.export.temp") + "\\"
				+ reg_spaj + "\\" + "espajonlinegadget_" + reg_spaj + ".pdf";
		PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
				outputName));

		Pemegang dataPP = elionsManager.selectpp(reg_spaj);
		Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
		PembayarPremi pembPremi = bacManager.selectP_premi(reg_spaj);
		if (pembPremi == null)
			pembPremi = new PembayarPremi();
		AddressBilling addrBill = elionsManager.selectAddressBilling(reg_spaj);
		Datausulan dataUsulan = elionsManager.selectDataUsulanutama(reg_spaj);
		dataUsulan
				.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
		InvestasiUtama inv = elionsManager.selectinvestasiutama(reg_spaj);
		Rekening_client rekClient = elionsManager.select_rek_client(reg_spaj);
		Account_recur accRecur = elionsManager.select_account_recur(reg_spaj);
		List detInv = bacManager.selectdetilinvestasi2(reg_spaj);
		List benef = elionsManager.select_benef(reg_spaj);
		List peserta = uwManager.select_all_mst_peserta(reg_spaj);
		List dist = elionsManager.select_tipeproduk();
		List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
		HashMap spajTemp = (HashMap) listSpajTemp.get(0);
		String idgadget = (String) spajTemp.get("NO_TEMP");
		Map agen = bacManager.selectAgenESPAJSimasPrima(reg_spaj);

		String s_channel = "";
		for (int i = 0; i < dist.size(); i++) {
			HashMap dist2 = (HashMap) dist.get(i);
			Integer i_lstp_id = (Integer) dist2.get("lstp_id");
			if (i_lstp_id.intValue() == dataUsulan.getTipeproduk().intValue()) {
				s_channel = (String) dist2.get("lstp_produk");
			}
		}

		Double d_premiRider = 0.;
		if (dataUsulan.getDaftaRider().size() > 0) {
			for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
				Datarider rider = (Datarider) dataUsulan.getDaftaRider().get(i);
				d_premiRider = rider.getMspr_premium();
			}
		}

		Double d_topUpBerkala = new Double(0);
		Double d_topUpTunggal = new Double(0);
		Double d_totalTopup = new Double(0);
		if (inv != null) {
			DetilTopUp daftarTopup = inv.getDaftartopup();
			d_topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala()) ? new Double(
					0) : daftarTopup.getPremi_berkala();
			d_topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal()) ? new Double(
					0) : daftarTopup.getPremi_tunggal();
			d_totalTopup = d_topUpBerkala + d_topUpTunggal;
		}

		Double d_premiExtra = (Common.isEmpty(uwManager
				.selectSumPremiExtra(reg_spaj)) ? 0. : uwManager
				.selectSumPremiExtra(reg_spaj));
		Double d_totalPremi = dataUsulan.getMspr_premium() + d_totalTopup
				+ d_premiRider + d_premiExtra;

		stamp.setMoreInfo(moreInfo);

		// ---------- Data Halaman Pertama ----------
		over = stamp.getOverContent(1);
		over.beginText();
		over.setFontAndSize(times_new_roman, 8);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatString.nomorSPAJ(reg_spaj), 370, 706, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toString(sysdate), 80, 693, 0);

		over.setFontAndSize(times_new_roman, 6);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first()
				.toUpperCase(), 200, 601, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first()
				.toUpperCase(), 200, 588, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_depkeu(), 200, 575, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_tsi())), 200, 562, 0);

		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_premium())), 280, 551, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_totalTopup.doubleValue() == new Double(0) ? "-" : (dataUsulan
						.getLku_symbol() + " " + FormatNumber
						.convertToTwoDigit(new BigDecimal(d_totalTopup))), 280,
				541, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_premiRider.doubleValue() == new Double(0) ? "-" : (dataUsulan
						.getLku_symbol() + " " + FormatNumber
						.convertToTwoDigit(new BigDecimal(d_premiRider))), 280,
				531, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								d_totalPremi)), 280, 521, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT, agen.get("NM_PENUTUP")
				.toString().toUpperCase(), 200, 492, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, agen.get("KD_PENUTUP")
				.toString(), 200, 479, 0);

		over.endText();

		// ---------- Data Halaman Kedua ----------
		over = stamp.getOverContent(2);
		over.beginText();

		// String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" +
		// (dataPP.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
		String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" + ".jpg";
		try {
			Image img = Image.getInstance(ttdPp);
			img.scaleAbsolute(40, 40);
			over.addImage(img, img.getScaledWidth(), 0, 0,
					img.getScaledHeight(), 458, 705);
			over.stroke();

			// String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_" +
			// (dataTT.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
			String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
					+ ".jpg";
			if (dataTT.getMste_age() < 17 || dataPP.getLsre_id() == 1)
				ttdTu = ttdPp;
			Image img2 = Image.getInstance(ttdTu);
			img2.scaleAbsolute(40, 40);
			over.addImage(img2, img2.getScaledWidth(), 0, 0,
					img2.getScaledHeight(), 458, 658);
			over.stroke();
		} catch (FileNotFoundException e) {
			logger.error("ERROR :", e);
			// ServletOutputStream sos = response.getOutputStream();
			// sos.println("<script>alert('TTD Pemegang Polis / Tertanggung Utama Tidak Ditemukan');window.close();</script>");
			// sos.close();
		}

		over.setFontAndSize(times_new_roman, 8);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toString(sysdate), 280, 790, 0);

		over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataPP.getMcl_first()
				.toUpperCase(), 300, 723, 0);

		over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataTT.getMcl_first()
				.toUpperCase(), 300, 676, 0);

		if (peserta.size() > 0) {
			Integer vertikal = 676;
			for (int i = 0; i < peserta.size(); i++) {
				vertikal = vertikal - 47;
				PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER, pesertaPlus
						.getNama().toUpperCase(), 300, vertikal, 0);

				vertikal = vertikal + 2;
			}
		}

		try {
			// String ttdAgen = exportDirectory + "\\" + idgadget + "_TTD_AGEN_"
			// + (agen.get("NM_PENUTUP").toString().toUpperCase()).replace(" ",
			// "") + ".jpg";
			String ttdAgen = exportDirectory + "\\" + idgadget + "_TTD_AGEN_"
					+ ".jpg";
			Image img3 = Image.getInstance(ttdAgen);
			img3.scaleAbsolute(40, 40);
			over.addImage(img3, img3.getScaledWidth(), 0, 0,
					img3.getScaledHeight(), 100, 420);
			over.stroke();
		} catch (FileNotFoundException e) {
			logger.error("ERROR :", e);
			// ServletOutputStream sos = response.getOutputStream();
			// sos.println("<script>alert('TTD Agen Penutup Tidak Ditemukan');window.close();</script>");
			// sos.close();
		}

		try {
			// String ttdReff = exportDirectory + "\\" + idgadget + "_TTD_REF_"
			// + (agen.get("NM_REFFERAL").toString().toUpperCase()).replace(" ",
			// "") + ".jpg";
			String ttdReff = exportDirectory + "\\" + idgadget + "_TTD_REF_"
					+ ".jpg";
			File cekFileEksis = new File(ttdReff);
			if(cekFileEksis.exists()){
				Image img4 = Image.getInstance(ttdReff);
				img4.scaleAbsolute(40, 40);
				over.addImage(img4, img4.getScaledWidth(), 0, 0,
						img4.getScaledHeight(), 280, 420);
				over.stroke();				
			}
		} catch (FileNotFoundException e) {
			logger.error("ERROR :", e);
			// ServletOutputStream sos = response.getOutputStream();
			// sos.println("<script>alert('TTD Agen Refferal Tidak Ditemukan');window.close();</script>");
			// sos.close();
		}

		over.setFontAndSize(times_new_roman, 6);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, agen.get("NM_PENUTUP")
				.toString().toUpperCase(), 110, 415, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 61, 405, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT, agen.get("KD_PENUTUP")
				.toString(), 81, 395, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, agen.get("NM_REFFERAL")
				.toString().toUpperCase(), 290, 415, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, agen.get("KD_REFFERAL")
				.toString(), 261, 405, 0);

		over.endText();

		// ---------- Data Halaman Ketiga ----------
		over = stamp.getOverContent(3);
		over.beginText();
		over.setFontAndSize(times_new_roman, 6);
		String flag_spaj = "";
		if (dataPP.getMspo_flag_spaj() == 0) {
			flag_spaj = "SPAJ OLD";
		} else if (dataPP.getMspo_flag_spaj() == 1) {
			flag_spaj = "SPAJ NEW VA";
		} else if (dataPP.getMspo_flag_spaj() == 2) {
			flag_spaj = "SPAJ NEW VA & PEMBAYAR PREMI";
		} else if (dataPP.getMspo_flag_spaj() == 3) {
			flag_spaj = "SPAJ FULL (MARET 2015)";
		} else if (dataPP.getMspo_flag_spaj() == 4) {
			flag_spaj = "SPAJ SIO (MARET 2015)";
		} else {
			flag_spaj = "SPAJ GIO (MARET 2015)";
		}
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, flag_spaj, 190, 788, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				s_channel.toUpperCase(), 190, 778, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_depkeu(), 190, 768, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_name(), 190, 758, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first()
				.toUpperCase(), 190, 748, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getMcl_gelar()) ? "-" : dataPP.getMcl_gelar(), 190, 739, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getMspe_mother(), 190, 730, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsne_note(),
				190, 720, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getMcl_green_card()) ? "TIDAK" : dataPP.getMcl_green_card(),
				190, 710, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLside_name(),
				190, 700, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getMspe_no_identity(), 190, 690, 0);

		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMspe_no_identity_expired()) ? "-"
						: FormatDate.toString(dataPP
								.getMspe_no_identity_expired()), 190, 680, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataPP.getMspe_place_birth() + ", "
						+ FormatDate.toString(dataPP.getMspe_date_birth()),
				190, 671, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspo_age()
				+ " Tahun", 190, 661, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sex2()
				.toUpperCase(), 190, 651, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
				.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataPP
				.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataPP
				.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 190, 642,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsag_name(),
				190, 632, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsed_name(),
				190, 622, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getMcl_company_name()) ? "-" : dataPP.getMcl_company_name(),
				246, 613, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMkl_kerja(),
				190, 603, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKerjab(),
				198, 593, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getMkl_kerja_ket()) ? "-" : dataPP.getMkl_kerja_ket(), 190,
				583, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getAlamat_kantor()) ? "-" : dataPP.getAlamat_kantor(), 250,
				573, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getKota_kantor()) ? "-" : dataPP.getKota_kantor(), 153, 563, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getKd_pos_kantor()) ? "-" : dataPP.getKd_pos_kantor(), 153,
				553, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getTelpon_kantor()) ? "-" : dataPP.getTelpon_kantor(), 153,
				544, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getTelpon_kantor2()) ? "-" : dataPP.getTelpon_kantor2(), 153,
				535, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
				153, 525, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getAlamat_rumah()) ? "-" : dataPP.getAlamat_rumah(), 153, 515,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getKota_rumah()) ? "-" : dataPP.getKota_rumah(), 153, 505, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getKd_pos_rumah()) ? "-" : dataPP.getKd_pos_rumah(), 153, 495,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getTelpon_rumah()) ? "-" : dataPP.getTelpon_rumah(), 153, 486,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getTelpon_rumah2()) ? "-" : dataPP.getTelpon_rumah2(), 153,
				476, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getAlamat_tpt_tinggal()) ? "-" : dataPP
						.getAlamat_tpt_tinggal(), 177, 466, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getKota_tpt_tinggal()) ? "-" : dataPP.getKota_tpt_tinggal(),
				153, 456, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKd_pos_tpt_tinggal()) ? "-" : dataPP
						.getKd_pos_tpt_tinggal(), 153, 446, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getTelpon_rumah()) ? "-" : dataPP.getTelpon_rumah(), 153, 437,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getTelpon_rumah2()) ? "-" : dataPP.getTelpon_rumah2(), 153,
				427, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
				153, 417, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(addrBill
				.getMsap_address()) ? "-" : addrBill.getMsap_address(), 208,
				407, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getNo_hp()) ? "-" : dataPP.getNo_hp(),
				153, 397, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getEmail()) ? "-" : dataPP.getEmail(),
				153, 387, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getMcl_npwp()) ? "-" : dataPP.getMcl_npwp(), 153, 378, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getMkl_penghasilan()) ? "-" : dataPP.getMkl_penghasilan(),
				226, 368, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getMkl_pendanaan()) ? "-" : dataPP.getMkl_pendanaan(), 153,
				359, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP
				.getMkl_tujuan()) ? "-" : dataPP.getMkl_tujuan(), 190, 349, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first()
				.toUpperCase(), 190, 319, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getMcl_gelar()) ? "-" : dataTT.getMcl_gelar(), 190, 309, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getMspe_mother(), 190, 300, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsne_note(),
				190, 290, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getMcl_green_card()) ? "TIDAK" : dataTT.getMcl_green_card(),
				190, 281, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLside_name(),
				190, 271, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getMspe_no_identity(), 190, 261, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMspe_no_identity_expired()) ? "-"
						: FormatDate.toString(dataTT
								.getMspe_no_identity_expired()), 190, 251, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataTT.getMspe_place_birth() + ", "
						+ FormatDate.toString(dataTT.getMspe_date_birth()),
				190, 241, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMste_age()
				+ " Tahun", 190, 231, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sex2()
				.toUpperCase(), 190, 221, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
				.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataTT
				.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataTT
				.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 190, 212,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsag_name(),
				190, 202, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsed_name(),
				190, 192, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getMcl_company_name()) ? "-" : dataTT.getMcl_company_name(),
				246, 183, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMkl_kerja(),
				190, 173, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getKerjab(),
				198, 163, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getMkl_kerja_ket()) ? "-" : dataTT.getMkl_kerja_ket(), 190,
				154, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getAlamat_kantor()) ? "-" : dataTT.getAlamat_kantor(), 250,
				144, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getKota_kantor()) ? "-" : dataTT.getKota_kantor(), 153, 134, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getKd_pos_kantor()) ? "-" : dataTT.getKd_pos_kantor(), 153,
				124, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getTelpon_kantor()) ? "-" : dataTT.getTelpon_kantor(), 153,
				115, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getTelpon_kantor2()) ? "-" : dataTT.getTelpon_kantor2(), 153,
				105, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
				153, 95, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getAlamat_rumah()) ? "-" : dataTT.getAlamat_rumah(), 153, 85,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getKota_rumah()) ? "-" : dataTT.getKota_rumah(), 153, 75, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getKd_pos_rumah()) ? "-" : dataTT.getKd_pos_rumah(), 153, 65,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getTelpon_rumah()) ? "-" : dataTT.getTelpon_rumah(), 153, 56,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getTelpon_rumah2()) ? "-" : dataTT.getTelpon_rumah2(), 153,
				46, 0);
		over.endText();

		// ---------- Data Halaman Keempat ----------
		over = stamp.getOverContent(4);

		over.beginText();
		over.setFontAndSize(times_new_roman, 6);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getAlamat_tpt_tinggal()) ? "-" : dataTT
						.getAlamat_tpt_tinggal(), 177, 798, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getKota_tpt_tinggal()) ? "-" : dataTT.getKota_tpt_tinggal(),
				153, 788, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKd_pos_tpt_tinggal()) ? "-" : dataTT
						.getKd_pos_tpt_tinggal(), 153, 779, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getTelpon_rumah()) ? "-" : dataTT.getTelpon_rumah(), 153, 769,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getTelpon_rumah2()) ? "-" : dataTT.getTelpon_rumah2(), 153,
				759, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
				153, 749, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(addrBill
				.getMsap_address()) ? "-" : addrBill.getMsap_address(), 208,
				739, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getNo_hp()) ? "-" : dataTT.getNo_hp(),
				153, 729, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getEmail()) ? "-" : dataTT.getEmail(),
				153, 719, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getMcl_npwp()) ? "-" : dataTT.getMcl_npwp(), 153, 710, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getMkl_penghasilan()) ? "-" : dataTT.getMkl_penghasilan(),
				226, 700, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getMkl_pendanaan()) ? "-" : dataTT.getMkl_pendanaan(), 153,
				690, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT
				.getMkl_tujuan()) ? "-" : dataTT.getMkl_tujuan(), 190, 681, 0);

		// Data Pembayar Premi
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getNama_pihak_ketiga()) ? "-"
						: pembPremi.getNama_pihak_ketiga(), 190, 652, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getKewarganegaraan()) ? "-"
						: pembPremi.getKewarganegaraan(), 190, 642, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getAlamat_lengkap()) ? "-" : pembPremi
						.getAlamat_lengkap(), 190, 632, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTelp_rumah()) ? "-" : pembPremi
						.getTelp_rumah(), 190, 622, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTelp_kantor()) ? "-" : pembPremi
						.getTelp_kantor(), 190, 613, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
				.isEmpty(pembPremi.getEmail()) ? "-" : pembPremi.getEmail(),
				190, 603, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTempat_lahir()) ? "-"
						: (pembPremi.getTempat_lahir() + ", " + FormatDate
								.toString(pembPremi
										.getMspe_date_birth_3rd_pendirian())),
				190, 593, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getMkl_kerja()) ? "-" : pembPremi
						.getMkl_kerja(), 190, 584, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 574, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 564, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 554, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getNo_npwp()) ? "-" : pembPremi
						.getNo_npwp(), 190, 544, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getLsre_relation()) ? "-" : pembPremi
						.getLsre_relation(), 215, 535, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getSumber_dana()) ? "-" : pembPremi
						.getSumber_dana(), 190, 525, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTujuan_dana_3rd()) ? "-"
						: pembPremi.getTujuan_dana_3rd(), 190, 515, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 505, 0);

		// Data Usulan
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				s_channel.toUpperCase(), 190, 476, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_name(), 190, 466, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_name(), 190, 456, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getMspr_ins_period() + " Tahun", 190, 446, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLscb_pay_mode(), 190, 437, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_premium())), 190, 427, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_tsi())), 190, 417, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
						.getMste_flag_cc() == 2 ? "TABUNGAN" : "KARTU KREDIT"),
				190, 407, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toIndonesian(dataUsulan.getMste_beg_date()), 190,
				397, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toIndonesian(dataUsulan.getMste_end_date()), 190,
				387, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 378, 0);

		// Data Investasi
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_premium())), 190, 349, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_topUpBerkala.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_topUpBerkala))), 190, 339, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_topUpTunggal.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_topUpTunggal))), 190, 329, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_premiExtra.doubleValue() == new Double(0) ? "-" : (dataUsulan
						.getLku_symbol() + " " + FormatNumber
						.convertToTwoDigit(new BigDecimal(d_premiExtra))), 190,
				319, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								d_totalPremi)), 190, 309, 0);

		// Data Rekening
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
						.getMste_flag_cc() == 2 ? "TABUNGAN" : "KARTU KREDIT"),
				190, 280, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getLsbp_nama(), 190, 270, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getMrc_cabang(), 190, 260, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 252, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 242, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getMrc_no_ac(), 190, 232, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getMrc_nama(), 190, 222, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, rekClient.getKuasa()
				.toUpperCase(), 190, 212, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toIndonesian(rekClient.getTgl_surat()), 190, 202, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 193, 0);

		Double d_jmlinves = new Double(0);
		String s_jnsinves = "";
		for (int i = 0; i < detInv.size(); i++) {
			DetilInvestasi detInves = (DetilInvestasi) detInv.get(i);
			d_jmlinves = d_jmlinves + detInves.getMdu_jumlah1();
			s_jnsinves = s_jnsinves + detInves.getLji_invest1().toUpperCase()
					+ " " + detInves.getMdu_persen1() + "%";
			if (i != (detInv.size() - 1))
				s_jnsinves = s_jnsinves + ", ";
		}
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								d_jmlinves)), 208, 183, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, s_jnsinves, 190, 173, 0);

		if (benef.size() > 0) {
			Integer j = 0;
			for (int i = 0; i < benef.size(); i++) {
				Benefeciary benefit = (Benefeciary) benef.get(i);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						benefit.getMsaw_first(), 60, 123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						benefit.getSmsaw_birth(), 208, 123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						benefit.getMsaw_sex() == 1 ? "PRIA" : "WANITA", 255,
						123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						dataPP.getLsne_note(), 310, 123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, benefit
						.getLsre_relation().toUpperCase(), 400, 123 - j, 0);
				j += 10;
			}
		}
		over.endText();
		stamp.close();
		reader.close();		
		output.flush();
		output.close();

		// File l_file = new File(outputName);
		// FileInputStream in = null;
		// ServletOutputStream ouputStream = null;
		// try{
		//
		// response.setContentType("application/pdf");
		// response.setHeader("Content-Disposition", "Inline");
		// response.setHeader("Expires", "0");
		// response.setHeader("Cache-Control",
		// "must-revalidate, post-check=0, pre-check=0");
		// response.setHeader("Pragma", "public");
		//
		// in = new FileInputStream(l_file);
		// ouputStream = response.getOutputStream();
		//
		// IOUtils.copy(in, ouputStream);
		// }catch (Exception e) {
		// logger.error("ERROR :", e);
		// }finally {
		// try {
		// if(in != null) {
		// in.close();
		// }
		// if(ouputStream != null) {
		// ouputStream.flush();
		// ouputStream.close();
		// }
		// }catch (Exception e) {
		// logger.error("ERROR :", e);
		// }
		// }

		// return null;
	}
			
		    private Boolean deleteAllFile(File pFile){
		        if(pFile.exists()){
		            if(pFile.isDirectory()){
		                if(pFile.list().length > 0){
		                    String[] strFiles = pFile.list();
		                    for(String strFileName : strFiles){
		                        if(strFileName.contains("path") || strFileName.contains("TP.pdf")){
		                             File fileToDelete = new File(pFile, strFileName);
		                             deleteAllFile(fileToDelete);
		                        }
		                    }
		                }
		            }
		        }
		        return pFile.delete();
		    }			

		    //20181030 Mark Valentino : copy dari class 'PDFViewer.java'
		    //Jika ada perubahan di 'PDFViewer.java' mohon diupdate jg disini.
			public static Map genItextTemplate1(ElionsManager elionsManager, BacManager bacManager, Properties props, String fileName, boolean sertificateFlag, int benefitType, String spaj, String lsbs_id, String lsdb_number) throws DocumentException, IOException {
				HashMap m = new HashMap();
				String reg_spaj = spaj;
				String lsbs = lsbs_id;
				String detbisnis = lsdb_number;
				
				String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
				String wakil = bacManager.selectWakilFromSpaj(reg_spaj);
				//String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
				
				File userDir = new File(props.getProperty("pdf.dir.export.temp")+"\\"+reg_spaj);
		        if(!userDir.exists()) {
		            userDir.mkdirs();
		        }
		        
				
				PdfContentByte over;
				BaseFont fonts = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
//				Font font = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 0.8f, Font.NORMAL, BaseColor.BLACK);
				
				if(StringUtil.isEmpty(fileName)) {
					fileName = "SERTIFIKAT.pdf";
				}
				
				String pathFile = props.getProperty("pdf.dir.syaratpolis")+"\\"+lsbs+"-"+FormatString.rpad("0", String.valueOf(detbisnis), 3)+"-NT.pdf";
//		        PdfReader reader = new PdfReader(props.getProperty("pdf.dir")+"\\ss_smile\\"+lsbs+"-"+FormatString.rpad("0", detbisnis, 3)+".pdf");
		        PdfReader reader = new PdfReader(pathFile);
		        int pages = reader.getNumberOfPages();
		        String hal = "1-" + (pages-1);
				
		        reader.selectPages(hal);
		        
//		        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+fileName;
		        String outputName = props.getProperty("pdf.dir.export.temp")+"\\"+reg_spaj+"\\"+fileName;		        
		        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
		        
		        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
		        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
		        Datausulan dataUsulan = elionsManager.selectDataUsulanutama(reg_spaj);
		        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		        
		        String alamat_1 = "";
		        String alamat_2 = "";
		        String no_sertifikat = "";
		        try {
		        	if(sertificateFlag) {
				        no_sertifikat = bacManager.selectNoSertifikat(reg_spaj);
				        
				        if("".equals(no_sertifikat) || null == (no_sertifikat)){
				        	String lsbsId_3dg = FormatString.rpad("0", lsbs.toString(), 3);
				        	no_sertifikat = bacManager.selectSertifikatTemp(cabang, wakil, lsbsId_3dg);
				        	
				        	HashMap param = new HashMap();
				        	param.put("name_pp", dataPP.getMcl_first());
				        	param.put("bod_pp", sdf.format(dataPP.getMspe_date_birth()));
				        	param.put("age_pp", dataPP.getMspo_age());
				        	param.put("begdate", sdf.format(dataUsulan.getMste_beg_date()));
				        	param.put("insperiod", dataUsulan.getMspo_ins_period());
				        	param.put("address", dataPP.getKota_rumah());
				        	param.put("name_tt", dataTT.getMcl_first());
				        	param.put("bod_tt", sdf.format(dataTT.getMspe_date_birth()));
				        	param.put("age_tt", dataTT.getMste_age());
				        	param.put("lsbs_id", lsbs);
				        	param.put("lsdbs_number", detbisnis);
				        	param.put("premi", dataUsulan.getMspr_premium());
				        	param.put("up", dataUsulan.getMspr_tsi());
				        	param.put("msag_id", dataPP.getMsag_id());
				        	param.put("no_policy", no_sertifikat);
				        	param.put("reg_spaj", reg_spaj);
				        	
				        	bacManager.insertMstSpajCrt(param);
				        }
			        }else {
			        	no_sertifikat = elionsManager.selectPolicyNumberFromSpaj(reg_spaj);
			        }
		        	
		        	JasperScriptlet jasper = new JasperScriptlet();
		        	
		            if (dataPP.getAlamat_rumah().length() > 46) {
		                alamat_1 = dataPP.getAlamat_rumah().substring(0, 45);
		                alamat_2 = dataPP.getAlamat_rumah().substring(46, dataPP.getAlamat_rumah().length());
		            } else {
		                alamat_1 = dataPP.getAlamat_rumah();
		            }
					over = stamp.getOverContent(1);
					over.beginText();
					over.setFontAndSize(fonts,8f);
					
					String tglbdate = sdf.format(dataUsulan.getMste_beg_date());
					String tgledate = sdf.format(dataUsulan.getMste_end_date());
					
		            over.showTextAligned(PdfContentByte.ALIGN_LEFT, no_sertifikat, 115, 744, 0);
		            over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toString(), 115, 730, 0);
		            over.showTextAligned(PdfContentByte.ALIGN_LEFT, sdf.format(dataPP.getMspe_date_birth())  + " / " + dataPP.getMspo_age() + " Tahun", 115, 715, 0);
		            over.showTextAligned(PdfContentByte.ALIGN_LEFT, tglbdate, 115, 700, 0);
		            over.showTextAligned(PdfContentByte.ALIGN_LEFT, tglbdate + " s/d " + tgledate, 115, 683, 0);
		            over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_1, 115, 670, 0);
		            if (alamat_2.equals("")) {
		            	if(dataPP.getKd_pos_rumah()==null || "".equals(dataPP.getKd_pos_rumah()) ){
		            		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKota_rumah(), 115, 660, 0);
		            	}else{
		            		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKota_rumah() + ", " + dataPP.getKd_pos_rumah(), 115, 660, 0);
		            	}
		            } else {
		            	if(dataPP.getKd_pos_rumah()==null || "".equals(dataPP.getKd_pos_rumah()) ){
		            		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_2, 115, 660, 0);
			            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKota_rumah(), 115, 650, 0);
		            	}else{
			            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_2, 115, 660, 0);
			            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKota_rumah() + ", " + dataPP.getKd_pos_rumah(), 115, 650, 0);
		            	}

		            }

		            over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first(), 398, 744, 0);
		            over.showTextAligned(PdfContentByte.ALIGN_LEFT, sdf.format(dataTT.getMspe_date_birth())  + " / " + dataTT.getMste_age() + " Tahun", 398, 730, 0);
		            over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLsdbs_depkeu(), 398, 715, 0);
		            
		            String cbyr = "";
		            int faktor = 1;
		            if(dataUsulan.getLscb_id()==1){
		            	cbyr = "triwulan";
		            	faktor = 3;
		            }else if(dataUsulan.getLscb_id()==2){
		            	cbyr = "semester";
		            	faktor = 6;
		            }else if(dataUsulan.getLscb_id()==3){
		            	cbyr = "tahun";
		            	faktor = 12;
		            }else if(dataUsulan.getLscb_id()==6){
		            	cbyr = "bulan";
		            }
		            
		            over.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency("Rp ",BigDecimal.valueOf(dataUsulan.getMspr_premium())) + ",- per "+cbyr, 398, 700, 0);
		            if(benefitType == 0) {
		            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.formatCurrency("Rp ",BigDecimal.valueOf(dataUsulan.getMspr_tsi())), 398, 685, 0);
		            }else {
		            	over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dibayarkan sekaligus " + jasper.formatCurrency("Rp ",BigDecimal.valueOf(dataUsulan.getMspr_tsi())) + "; Atau", 398, 685, 0);
		                over.showTextAligned(PdfContentByte.ALIGN_LEFT, "Dibayarkan bulanan " + jasper.formatCurrency("Rp ",BigDecimal.valueOf((dataUsulan.getMspr_premium()*10)/faktor)) + " selama 5 tahun", 398, 673, 0);
		            }
		//
		            over.showTextAligned(PdfContentByte.ALIGN_LEFT, tglbdate, 433, 659, 0);
		            
				    String ttd = Resources.getResourceURL(props.getProperty("images.ttd.direksi")).getPath();
				    Image img = Image.getInstance(ttd);					
					img.setAbsolutePosition(380, 300);		
//					img.scaleAbsolute(90, 34);
					img.scalePercent(27);
					if("223".equals(lsbs) &&  "001".equals(detbisnis)){
						over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 400, 616);
					}else{
						over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 400, 624);
					}
		            
		            over.endText();
		            stamp.close();
		            reader.close();

		        } catch (Exception e) {
		            e.printStackTrace();
					FileUtils.deleteFile(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj,fileName);
		        } 
		        
		        return m;
			}
			
			private Map printPolisExclude(ServletContext servletContext, String spaj, HttpServletRequest request, int singleDuplexQuadruplex, ElionsManager elionsManager, UwManager uwManager, BacManager bacManager, Properties props, Products products) {
				Map params = new HashMap();
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				params.put("spaj", spaj);
				params.put("props", props);
				String validasiMeterai = uwManager.validasiBeaMeterai(currentUser.getJn_bank());
				if(validasiMeterai != null){
					params.put("meterai", null);
					params.put("izin", "");
				}else{
					params.put("meterai", "Rp. 6.000,-");
					params.put("izin", elionsManager.selectIzinMeteraiTerakhir());
				}
				
				//jenis print ulang
				int jpu = ServletRequestUtils.getIntParameter(request, "jpu", -1);
				
				if(jpu == 1) { //PRINT ULANG POLIS
					params.put("tipePolis", "O R I G I N A L");
				}else if(jpu == 2) { //PRINT DUPLIKAT POLIS
					String seq = ServletRequestUtils.getStringParameter(request, "seq", "");
					params.put("tipePolis", "D U P L I C A T E " + "("+seq+")");
				}else if(jpu == 3) { //PRINT ULANG POLIS
					params.put("tipePolis", "O R I G I N A L");
				}
				params.put("ingrid", props.get("images.ttd.direksi")); //ttd dr. ingrid (Yusuf - 04/05/2006)
				
				String kategori = products.kategoriPrintPolis(spaj);
//				String policyNumber = "09208201800001";
//				 	   policyNumber = uwManager.getUwDao().selectNoPolisFromSpaj(spaj);
//				PrintPolisPrintingController ppc = new PrintPolisPrintingController();
//				params.put("logoQr", ppc.getQrBasedOnPolicy(servletContext,props,policyNumber));
//				params.put("Print", "cetak");
				
				//String kategori = "guthrie";
				logger.info("JENIS POLIS : " + kategori);	
				if(PrintPolisMultiController.POLIS == singleDuplexQuadruplex) { //single
					params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis."+kategori));
				}else if(PrintPolisMultiController.POLIS_DUPLEX == singleDuplexQuadruplex) { //duplex(cetak BOLAK-BALIK)
					params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
					params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.duplex"));
				}else if(PrintPolisMultiController.POLIS_QUADRUPLEX == singleDuplexQuadruplex) { //quadruplex
					params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
					params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex"));
				}else if(PrintPolisMultiController.POLIS_QUADRUPLEX_PLUS_HADIAH == singleDuplexQuadruplex) { //quadruplex
					params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
					params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex_plus_hadiah"));
				}else if(PrintPolisMultiController.POLIS_QUADRUPLEX_MEDICAL_PLUS== singleDuplexQuadruplex) { //quadruplex				
					params.put("pathPolis", props.getProperty("report.polis."+kategori+".medplus")+".jasper");
					params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex_medical_plus"));
				}
				return params;
			}
			
			//Menggunakan class EmailPool
			public String emailPaKonven(HttpServletRequest request,HttpServletResponse response, ElionsManager elionsManager, UwManager uwManager, Properties props, String spaj) throws Exception {
				
				Date tanggal = elionsManager.selectSysdateSimple();
				//String sysdate = defaultDateFormat.format(new Date());
				Map params = new HashMap();
				User currentUser=(User)request.getSession().getAttribute("currentUser");
				//String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
				if (spaj.equals(null)){
					spaj = request.getAttribute("spaj").toString();
				}
				//Integer show= ServletRequestUtils.getIntParameter(request, "show",0);
				//String flag= ServletRequestUtils.getStringParameter(request, "flag","");
				Pemegang pmg = elionsManager.selectpp(spaj);
				List<File> attachments = new ArrayList<File>();
				List<String> error = new ArrayList<String>();
				String hasilEmail = "";
				
				//Uploader
				CommandUploadBac uploader = new CommandUploadBac();
				ServletRequestDataBinder binder = new ServletRequestDataBinder(uploader);
				binder.bind(request);
				uploader.setErrorMessages(new ArrayList<String>());				
				
				//Cari File
				String msp_id = uwManager.getUwDao().selectMspIdFromSpaj(spaj);
				String businessId = uwManager.selectBusinessId(spaj);
				String product_sub_code = uwManager.selectLsdbsNumber(spaj);
				List<Pas> pas = new ArrayList<Pas>();
				//pas = uwManager.selectAllPasList(msp_id, null, null, null, null, "pabsm", null, "pabsm", null, null, null);
				pas = uwManager.getUwDao().selectPasBySpaj(spaj, null);
				Pas p = pas.get(0);
				
				HashMap<String, Object> kartu = uwManager.selectDetailKartuPas(p.getNo_kartu());
				String no_polis_induk = kartu.get("NO_POLIS_INDUK").toString();
				//String outputName = pdfPolisPath + "\\bsm\\73\\" + no_polis_induk + "-" + kode_plan + "-" + no_sertifikat + ".pdf";
				String fileName = no_polis_induk + "-" + "073" + "-" + p.getNo_kartu() + ".pdf";		
				File sourceFile = new File("\\\\ebserver\\pdfind\\Polis\\bsm\\73\\" + fileName);
//				if(sourceFile.exists()){
//					attachments.add(sourceFile);
//				}else{
					try {
						String nama_plan = kartu.get("NAMA_PLAN").toString();
						String product_code = "73";						
						product_sub_code = "073";
						ITextPdf.generateSertifikatPaBsmV2(p.getNo_kartu(), no_polis_induk, product_code, product_sub_code, nama_plan, p.getMsp_up(), p.getMsp_premi(), elionsManager);
					}catch(Exception e){
						
						FileInputStream in = null;
						ServletOutputStream ouputStream = null;
						in = new FileInputStream(fileName);
						ouputStream = response.getOutputStream();
						if(ouputStream != null) {
							in.close();
							ouputStream.flush();
						    ouputStream.close();							
						}							
//						PdfReader pdfReader = new PdfReader("123");						
//						PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(sourceFile));						
						
						logger.error("ERROR :", e);
						hasilEmail = "Error pada saat generate file Sertifikat.";
						return hasilEmail;						
					}					
//				}
//				if(!currentUser.getLde_id().equals("11")){
//				    params.put("main", "true");
//			     }

				if(!sourceFile.exists()){
					error.add("Data SOFTCOPY Kosong, Pengiriman GAGAL");
				}else{
					attachments.add(sourceFile);
					try{
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0,
								null, 0, Integer.parseInt(currentUser.getLus_id()), new Date(), null, true,
								props.getProperty("admin.ajsjava"),
								new String[]{pmg.getMspe_email()},
								//cc
								(currentUser.getEmail().trim().equals("")? null : currentUser.getEmail().replaceAll(" ", "").split(";")),						
								//bcc
								//new String[]{"mark.valentino@sinarmasmsiglife.co.id;titis@sinarmasmsiglife.co.id"},
								null,
								"Softcopy Sertifikat Polis Asuransi Personal Accident atas nama " + pmg.getMcl_first() + " no. " + pmg.getMspo_policy_no(),
								"Kepada Yth." + "\n" +
								"Bapak/Ibu " + pmg.getMcl_first() + "\n" +
								"di tempat" + 
								"\n" +
								"\n" +
								"Nasabah Terhormat," + "\n" +
								"Selamat, Anda telah terdaftar sebagai peserta Personal Accident Risiko ABD dari Sinarmas MSIG Life dengan manfaat :" + 
								"\n" +
								"    1.    Manfaat Asuransi risiko meninggal dunia akibat Kecelakaan/Risiko A." + "\n" +
								"    2.    Manfaat Asuransi risiko cacat tetap total atau sebagian akibat Kecelakaan/Risiko B." + "\n" +								
								"    3.    Manfaat Asuransi risiko biaya pengobatan dan perawatan di Rumah Sakit akibat Kecelakaan/Risiko D." +
								"\n" + 
								"\n" +								
								"Terlampir adalah sertifikat polis sebagai panduan Anda dalam memahami ketentuan produk secara ringkas." + "\n" +
								"\n" +						
								"Terima kasih" + 
								"\n" +
								"\n" +							
								"Salam Hangat," + "\n" +
								"Sinarmas MSIG Life",
								attachments,
								spaj);
						error.add("Kirim Sertifikat Ke Nasabah (Email) Berhasil!");
						hasilEmail = error.toString();				
					}catch(Exception e){
						logger.error("ERROR :", e);
						hasilEmail = "Proses Pengiriman Email Gagal.";
						return hasilEmail;
					}
					
				if(!error.equals("")){
					params.put("pesanError", error);
				}
				params.put("pmg", pmg);
			}	
				return hasilEmail;
		}
			
			public String generateOutsourceScheduler(String spaj, int flagPrePrinted, Properties props, Products products, User user)
					throws Exception {

				Map paramsPolis = null;
				Integer mspoProvider = uwManager.selectGetMspoProvider(spaj);		

				boolean isDmtm = (StringUtils.substring(spaj, 0, 2).equals("40"));

				// 20181114 Mark Valentino Non aktifkan utk Test - Aktifkan utk Production
				List listSpajTemp = this.bacManager.selectReferensiTempSpaj(spaj);
				if ((listSpajTemp.size() > 0) && (!isDmtm)) {
						espajonlinegadget(null, null, elionsManager, uwManager, bacManager, props, products, spaj);

				}

				String hasilGenerate = "";
				Connection conn = null;
				try {

					conn = uwManager.getUwDao().getDataSource().getConnection();
					// String spaj = ServletRequestUtils.getStringParameter(request,
					// "spaj", "");
					
					elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", "01", 6, 1, "GENERATE OUTSOURCE SCHEDULER (E-LIONS)", true, user);

					String cabang = elionsManager.selectCabangFromSpaj(spaj);	

					String jpu = "0";
					Integer punyaSimascard = 0;
					String flagUlink = "0";

					List detBisnis = elionsManager.selectDetailBisnis(spaj);
					String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
					String detbisnis = (String) ((Map) detBisnis.get(0))
							.get("DETBISNIS");

					String lus_id = "01";

					Boolean listExclude = false;
					//String test1 = StringUtils.substring(spaj, 0, 2);
					//boolean isDmtm = (StringUtils.substring(spaj, 0, 2).equals("40"));

					// 1. Polis
					paramsPolis = printPolis(spaj, null,
							PrintPolisMultiController.POLIS_QUADRUPLEX, elionsManager,
							uwManager, bacManager, props, products);

					// 2. Manfaat
					Integer businessNumber = uwManager.selectBusinessNumber(spaj);
					Map paramsManfaat = elionsManager.prosesCetakManfaat(spaj, "01",
							null);

					String pathManfaat = (String) paramsManfaat.get("reportPath");
					pathManfaat = pathManfaat.substring(17);
					paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
					paramsManfaat.remove("reportPath");
					paramsManfaat.put("lsdbs", businessNumber);
					paramsPolis.putAll(paramsManfaat);

					paramsPolis.put("koneksi", conn);
					List temp = new ArrayList();
					Map map = new HashMap();

					map.put("halaman", "1");
					temp.add(map);// polis

					map.put("halaman", "2");
					temp.add(map);// manfaat

					map.put("halaman", "3");
					temp.add(map);// surat

					if (products.unitLink(uwManager.selectBusinessId(spaj))) {
						map.put("halaman", "4");
						temp.add(map);// kosong

						map.put("halaman", "5");
						temp.add(map);// alokasi dana (bisa 1 / 2 halaman)

					}
					if (products.unitLink(uwManager.selectBusinessId(spaj))) {// surat
																				// simas
																				// card

						map.put("halaman", "7");
						temp.add(map);

					} else {
						map.put("halaman", "4");
						temp.add(map);

					}
					map.put("halaman", "6");
					temp.add(map);

					map.put("halaman", "8");
					temp.add(map);

					map.put("halaman", "9");
					temp.add(map);

					map.put("halaman", "10");
					temp.add(map);

					paramsPolis.put("dsManfaat",
							JasperReportsUtils.convertReportData(temp));

					String businessId = FormatString.rpad("0",
							uwManager.selectBusinessId(spaj), 3);

					//3. Surat Polis
//					if("001,045,053,054,130,131,132".indexOf(businessId)==-1) {

						String va = uwManager.selectVirtualAccountSpaj(spaj);
						if(cabang.equals("58")){
							if(elionsManager.selectValidasiTransferPbp(spaj)==0){//ini mste_flag_cc
								if(va==null){
									uwManager.updateVirtualAccountBySpaj(spaj);
								}
							}
						}
						
						Map data 		= uwManager.selectDataVirtualAccount(spaj);
						int lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
						String lku_id 	= (String) data.get("LKU_ID");
						
						Integer flag_cc = elionsManager.select_flag_cc(spaj);
						
//						if(va != null){
						if(lku_id.equals("01") && !products.syariah(businessId,businessNumber.toString()) && lscb_id != 0 && flag_cc ==0 && !businessId.equals("196") && !(businessId.equals("217") && businessNumber ==2)&& !(businessId.equals("190") && "5,6".indexOf(businessNumber.toString())>=0)){
//						if(flag_cc ==0){
							
							List cekSpajPromo = bacManager.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
							
							if(!cekSpajPromo.isEmpty()){
								paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va_promo") + ".jasper");
							}else if(businessId.equals("217")&& businessNumber==2){ //untuk ERbe di set menggunakan surat_polis
								paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
							}else{
								paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
							}
							
						//	paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
						}else if(products.syariah(businessId, businessNumber.toString())){
							if(flag_cc==0 && !businessId.equals("175")){
								if(businessId.equals("215")&& businessNumber==003){
									paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
								}else{
									paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah_va") + ".jasper");
								}
							}else{
								paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
							}
						}else if(cabang.equals("58")){
							paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.mall") + ".jasper");
						}
						else{
							if(businessId.equals("196")&& businessNumber==002){
								paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.term") + ".jasper");
							}else{
							paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
							}
						}
						
						paramsPolis.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid (Yusuf - 04/05/2006)
//					}

					// 4. Alokasi Dana
					List viewUlink = elionsManager.selectViewUlink(spaj);
					if (viewUlink.size() != 0) {
						Map paramsAlokasi = elionsManager.cetakSuratUnitLink(viewUlink,
								spaj, true, 1, 1, 0);

						paramsAlokasi.put("elionsManager", elionsManager);

						String pathAlokasi = (String) paramsAlokasi.get("reportPath");
						pathAlokasi = pathAlokasi.substring(17);
						paramsAlokasi.put("pathAlokasiDana", pathAlokasi + ".jasper");
						paramsAlokasi.remove("reportPath");
						paramsAlokasi.put("dsAlokasiDana",
								JasperReportsUtils.convertReportData(viewUlink));

						paramsPolis.putAll(paramsAlokasi);
					}

					// 5 Tanda Terima Polis
					// Integer referal = ServletRequestUtils.getIntParameter(request,
					// "referal", 0);

					Integer referal = 0;

					// List detBisnis = elionsManager.selectDetailBisnis(spaj);
					String lsbs_id = (String) ((Map) detBisnis.get(0)).get("BISNIS");
					String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
					// String lsbs_id = FormatString.rpad("0",
					// uwManager.selectBusinessId(spaj), 3);

					String reportPath;
					String namaFile = props.getProperty("pdf.tanda_terima_polis");
					int isInputanBank = elionsManager.selectIsInputanBank(spaj);
					if (((isInputanBank == 2 || isInputanBank == 3) && referal == 0 && !products
							.productBsmFlowStandardIndividu(Integer.parseInt(lsbs_id),
									Integer.parseInt(lsdbs)))
							|| (lsbs_id.equals("175") && lsdbs.equals("002"))) {

						reportPath = props.getProperty("report.tandaterimasertifikat");
						namaFile = props.getProperty("pdf.tanda_terima_sertifikat");
					} else if (products.unitLink(lsbs_id)
							&& !products.stableLink(lsbs_id)) {

						if (products.syariah(lsbs_id, lsdbs)) {

							reportPath = props
									.getProperty("report.tandaterimapolis.syariah");
						} else {

							reportPath = props
									.getProperty("report.tandaterimapolis.link");
						}
					} else if (lsbs_id.equals("187")) {
						reportPath = props.getProperty("report.tandaterimapolis.pas");
					} else {
						reportPath = props.getProperty("report.tandaterimapolis"); // ini
																					// udah
																					// include
																					// biasa
																					// +
																					// syariah

					}

					paramsPolis.put("referal", referal);
					paramsPolis.put("pathTandaTerimaPolis", reportPath + ".jasper");

					// flagPrePrinted : 1 = direct print, 2 = Polis All
					if (flagPrePrinted == 2) {

						List daftarSebelumnya = uwManager.selectSimasCard(spaj);
						List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj);

						if (!daftarSebelumnya.isEmpty() && isAgen.isEmpty()) {
							Map SimasCardSebelumnya = (Map) daftarSebelumnya.get(0);
							if (!Common.isEmpty(SimasCardSebelumnya.get("REG_SPAJ"))) {
								if (SimasCardSebelumnya.get("REG_SPAJ").equals(spaj)) {
									punyaSimascard = 1;
								} else {
									elionsManager.insertMstPositionSpaj(
											"01",
											"SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO SPAJ"
													+ SimasCardSebelumnya
															.get("REG_SPAJ"), spaj, 0);

								}
							}

						}

					}
					String reportPathSimasCard = props.get("report.surat_simcard")
							.toString();

					paramsPolis.put("pathSuratSimasCard", reportPathSimasCard
							+ ".jasper");

					for (Iterator iter = paramsPolis.keySet().iterator(); iter
							.hasNext();) {

						String nama = (String) iter.next();
						logger.info(nama + " = " + paramsPolis.get(nama));
					}

					// SSU-SSK
					// update 7 September 2018 Mark Valentino SSU-SSK diikutsertakan
					if (PDFViewer.checkFileProduct(elionsManager, uwManager, props,
							spaj))
						;
					{

						List<File> pdfFiles = new ArrayList<File>();

						// String exportDirectory =
						// props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;

						String exportDirTemp = props.getProperty("pdf.dir.export.temp")
								+ "\\" + spaj;

						File exportDirTemp1 = new File(
								props.getProperty("pdf.dir.export.temp") + "\\" + spaj);

						if (!exportDirTemp1.exists()) {
							exportDirTemp1.mkdirs();

						}
						File fileDirZip = new File(exportDirTemp1 + "\\" + spaj);
						if (!fileDirZip.exists()) {
							fileDirZip.mkdirs();

						}

						String dirSsk = props.getProperty("pdf.dir.syaratpolis");
						PDFMergerUtility SskBefore = new PDFMergerUtility();
						File file = null;
						File fileSsk1 = null;
						for (int i = 0; i < detBisnis.size(); i++) {
							Map m = (HashMap) detBisnis.get(i);
							// Mark Valentino 20180906
							// SSU
							Integer lsbsId = Integer.parseInt(m.get("BISNIS")
									.toString());

							if (lsbsId <= 300) {
								File fileSSU = PDFViewer.productFile(elionsManager,
										uwManager, dirSsk, spaj, m, props);

								if (fileSSU != null)
									if (fileSSU.exists()) {
										pdfFiles.add(fileSSU);
										String pathSsu = fileSSU.toString();
										FileUtil.copyfile(pathSsu,fileDirZip.toString() + "\\" + "6. ssu.pdf");
									}							
							}
							// SSK
							if ((lsbsId > 800) && (lsbsId < 900)) {
								if (i == 1) {
									fileSsk1 = PDFViewer.riderFile(elionsManager,uwManager, dirSsk, lsbs_id, lsdbs, m, spaj,props);
									if (fileSsk1 != null){
										if (fileSsk1.exists()){
											SskBefore.addSource(fileSsk1);
											pdfFiles.add(fileSsk1);
											System.out.println("File SSK Rider pertama telah ditambahkan.");											
										}
									}	

								}else if (i >= 2) {
									File fileSskN = PDFViewer.riderFile(elionsManager,uwManager, dirSsk, lsbs_id, lsdbs, m, spaj,props);

									// Jika file name SSK ke-2 berbeda dgn filename SSK pertama, maka diambil juga									
									if (!fileSsk1.getName().toString().equals(fileSskN.getName().toString())){									
										if (fileSskN != null){
											if (fileSskN.exists()){
													SskBefore.addSource(fileSskN);
													pdfFiles.add(fileSskN);
													System.out.println("File SSK Rider ke-"+ i + " telah ditambahkan.");												
												}
											}
									}
								}else {
									file = null;
								}
								// if(file!=null) if(file.exists()) {
								// String pathSsk = file.toString();
								// SskBefore.addSource(file);
								// FileUtil.copyfile(pathSsk, fileDirZip.toString() +
								// "\\" + "7. ssk.pdf");

								// }
							}

						}
						SskBefore.setDestinationFileName(fileDirZip.toString() + "\\"
								+ "7. ssk.pdf");

						SskBefore.mergeDocuments();
						//pdfFiles.add(SskBefore);
						// PdfUtils.combinePdf(pdfFiles, exportDirectory, "pathSS.pdf");
						PdfUtils.combinePdf(pdfFiles, exportDirTemp, "pathSS.pdf");

					}

					// ENDORS SMILE MEDICAL

					if (mspoProvider == 2) {
						PrintPolisPerjanjianAgent printPolis = new PrintPolisPerjanjianAgent();
						List<String> pdfs = new ArrayList<String>();
						Boolean suksesMerge = false;
						Boolean scFile = false;
						String endorsPolis = "";
						String Kartuadmedika = "";
						String Pesertaadmedika = "";
						String provider = "";
						String filename = "pathAdmedika";
						Date sysdate = elionsManager.selectSysdate();
						Boolean syariah = products.syariah(lsbs_id, lsdbs);

						Integer ekaSehatBaru = uwManager.selectCountEkaSehatAdmedikaNew(spaj, 0);
						Integer ekaSehatHCP = uwManager.selectCountEkaSehatAdmedikaHCP(spaj);
						Integer ekaSehatPlus = uwManager.selectCountEkaSehatAdmedikaNew(spaj, 1);
						Integer s = Integer.parseInt(lsdbs.substring(1));
						Integer punyaEndorsAdmedika = bacManager.selectPunyaEndorsEkaSehatAdmedika(spaj);

						if (punyaEndorsAdmedika == 0)
							bacManager.prosesEndorsKetinggalanNew(spaj,Integer.parseInt(lsbs_id));
						
						if(syariah){
							Kartuadmedika = props.getProperty("pdf.template.admedika")+ "\\PETUNJUK_PENGGUNAAN_KARTU_PESERTA_syariah.pdf";					
						}else{
							Kartuadmedika = props.getProperty("pdf.template.admedika")+ "\\PETUNJUK_PENGGUNAAN_KARTU_PESERTA_konven.pdf";
						}
						OutputStream output = new FileOutputStream(props.getProperty("pdf.dir.export.temp") + "\\"
								+ spaj + "\\" + filename + ".pdf");
						
						pdfs.add(0,Kartuadmedika);
						
						if (pdfs.size() > 0){
							suksesMerge = MergePDF.concatPDFs(pdfs, output, false);					
						}
						
//						if (ekaSehatBaru >= 1) {
//							if (lsbs_id.equals("189")
//									|| products.syariah(lsbs_id, lsdbs)) {
		//
//								if (lsbs_id.equals("189")
//										&& Integer.parseInt(lsdbs.substring(1)) > 15) {
//									// endorsPolis =
//									// props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariahSMP.pdf";
		//
//									endorsPolis = props.getProperty("pdf.template.admedika")
//											+ "\\PETUNJUK_PENGGUNAAN_KARTU_PESERTA_syariah.pdf";
		//
//								} else {
//									// endorsPolis =
//									// props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariah.pdf";
//									endorsPolis = props.getProperty("pdf.template.admedika")
//											+ "\\PETUNJUK_PENGGUNAAN_KARTU_PESERTA_syariah.pdf";
		//
//								}
//							} else {
//								// endorsPolis =
//								// props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedical.pdf";
//								// // EndorsemenPolisBaru
//								endorsPolis = props.getProperty("pdf.template.admedika")
//										+ "\\PETUNJUK_PENGGUNAAN_KARTU_PESERTA_konven.pdf"; // EndorsemenPolisBaru
		//
//							}
//						} else if (ekaSehatPlus >= 1) {
//							// endorsPolis =
//							// props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedicalPlus.pdf";
//							endorsPolis = props.getProperty("pdf.template.admedika")
//									+ "\\PETUNJUK_PENGGUNAAN_KARTU_PESERTA_konven.pdf";
		//
//						} else {
//							// endorsPolis =
//							// props.getProperty("pdf.template.admedika")+"\\EndorsemenPolis.pdf";
//							endorsPolis = props.getProperty("pdf.template.admedika")
//									+ "\\PETUNJUK_PENGGUNAAN_KARTU_PESERTA_konven.pdf";
		//
//						}
//						
//						if (ekaSehatHCP >= 1 && ekaSehatBaru < 1 && ekaSehatPlus < 1) {
//							// Kartuadmedika =
//							// props.getProperty("pdf.template.admedika")+"\\KartuHCP.pdf";
//							Kartuadmedika = props.getProperty("pdf.template.admedika")
//									+ "\\PETUNJUK_PENGGUNAAN_KARTU_PESERTA_konven.pdf";
		//
//							provider = props.getProperty("pdf.template.admedika")
//									+ "\\ProviderHCP.pdf";
		//
//						} else {
//							// Kartuadmedika =
//							// props.getProperty("pdf.template.admedika")+"\\KartuAdmedika.pdf";
//							Kartuadmedika = props.getProperty("pdf.template.admedika")
//									+ "\\PETUNJUK_PENGGUNAAN_KARTU_PESERTA_konven.pdf";
		//
//							provider = props.getProperty("pdf.template.admedika")
//									+ "\\Provider.pdf";
		//
//						}
//						
//						Pesertaadmedika = props.getProperty("pdf.template.admedika")
//								+ "\\PesertaAdmedika.pdf";
		//
//						if (ekaSehatBaru >= 1 || ekaSehatPlus >= 1) {
//							pdfs.add(endorsPolis);
//						}
//						if (flagPrePrinted == 1 || flagPrePrinted == 2) {
//							// pdfs.add(Kartuadmedika);
//							// pdfs.add(provider);
//							// 20190304 Mark Valentino - Request Tim Printing : Jika
//							// SPAJ centang provider, maka exclude file Endorse
//							if ((mspoProvider == 2) && (pdfs.size() > 0)) {
//								pdfs.remove(0);
//								pdfs.add(0, Kartuadmedika);
//							} else {
//								pdfs.add(0, Kartuadmedika);
//							}
		//
//						}
//						Integer total_ekasehat = uwManager.getJumlahEkaSehat(spaj);
//						List<Map> datapeserta = uwManager.selectDataPeserta(spaj);
		//
//						if (ekaSehatHCP >= 1) {
		//
//							OutputStream output;
//							// OutputStream output = new
//							// FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaHCP.pdf");
//							if (mspoProvider == 2) {
//								output = new FileOutputStream(
//										props.getProperty("pdf.dir.export.temp") + "\\"
//												+ spaj + "\\" + filename + ".pdf");
//							} else {
//								output = new FileOutputStream(
//										props.getProperty("pdf.template.admedika")
//												+ "\\KartuHCP.pdf");
//							}
		//
//							suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
//						} else if (lsbs_id.equals("189")
//								|| products.syariah(lsbs_id, lsdbs)) {
		//
//							OutputStream output;
//							// OutputStream output = new
//							// FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatSyariah.pdf");
//							if (mspoProvider == 2) {
//								output = new FileOutputStream(
//										props.getProperty("pdf.dir.export.temp") + "\\"
//												+ spaj + "\\" + filename + ".pdf");
//							} else {
//								output = new FileOutputStream(
//										props.getProperty("pdf.template.admedika")
//												+ "\\KartuAdmedikaSyariah.pdf");
//							}
		//
//							suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
//						} else if (ekaSehatPlus >= 1) {
//							OutputStream output = new FileOutputStream(
//									props.getProperty("pdf.template.admedika")
//											+ "\\MergeAdmedikaMedicalPlus.pdf");
		//
//							suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
//						} else {
//							// 20190304 Mark Valentino - Request Tim Printing : Jika
//							// SPAJ centang provider, maka exclude file Endorse
		//
//							// OutputStream output = new
//							// FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatNew.pdf");
//							OutputStream output;
//							if (mspoProvider == 2) {
//								output = new FileOutputStream(
//										props.getProperty("pdf.dir.export.temp") + "\\"
//												+ spaj + "\\" + filename + ".pdf");
//							} else {
//								output = new FileOutputStream(
//										props.getProperty("pdf.template.admedika")
//												+ "\\MergeAdmedikaEkaSehatNew.pdf");
//							}
		//
//							suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
//						}

//						// File userDirTemp = new
//						// File(props.getProperty("pdf.dir.export.temp")+"\\"+cabang+"\\"+spaj);
//						File userDirTemp = new File(
//								props.getProperty("pdf.dir.export.temp") + "\\" + spaj);
//						if (!userDirTemp.exists()) {
//							userDirTemp.mkdirs();
//						}
		//
//						// String outputName =
//						// props.getProperty("pdf.dir.export.temp")+"\\"+cabang+"\\"+spaj+"\\"+filename+".pdf";
//						String outputName = props.getProperty("pdf.dir.export.temp")
//								+ "\\" + spaj + "\\" + filename + ".pdf";

//						if (!scFile) {
//							Map dataAdmedika = uwManager.selectDataAdmedika(spaj);
//							String ingrid = props.getProperty("pdf.template.admedika2")
//									+ "\\hamid.bmp";
//							if (mspoProvider != 2) {
		//
//								printPolis.generateEndorseAdmedikaEkaSehat(spaj,
//										total_ekasehat, outputName, dataAdmedika,
//										datapeserta, sysdate, ingrid, lsbs_id, lsdbs,
//										syariah, ekaSehatHCP, ekaSehatBaru,
//										ekaSehatPlus);
//							}
		//
//						}
					}
					if (products.unitLink(uwManager.selectBusinessId(spaj))) {
						flagUlink = "1";
					}

					// randy - direct print e-spaj dmtm (req. timmy) - 07/09/2016
					// Mark Valentino 20181009 direct print e-spaj ditaruh di atas
					// if (cabang.equals("40")){
					// espajdmtm3(servletContext, spaj, request, 0, elionsManager,
					// uwManager, bacManager, props, products);

					// }

					String path = "";
					String pathTemp = "";
					path = props.getProperty("pdf.dir.export") + "\\" + cabang + "\\"
							+ spaj;

					pathTemp = props.getProperty("pdf.dir.export.temp") + "\\" + spaj;
					String pathTemplate1 = props.getProperty("pdf.template")
							+ "\\BlankPaper.pdf";

					String pathTemplate2 = props.getProperty("pdf.template")
							+ "\\BlankPaper2.pdf";

					JasperUtils.exportReportToPdfNoLock(paramsPolis
							.get("pathSurat").toString(), pathTemp,
							"pathSurat.pdf", paramsPolis, conn);
			
					JasperUtils.exportReportToPdfNoLock(
							paramsPolis.get("pathManfaat").toString(), pathTemp,
							"pathManfaat.pdf", paramsPolis, conn);
			
					if (!listExclude) {
						JasperUtils.exportReportToPdfNoLock(
								paramsPolis.get("pathPolis").toString(), pathTemp,
								"pathPolis.pdf", paramsPolis, conn);
			
					}
					JasperUtils
							.exportReportToPdfNoLock(
									paramsPolis.get("pathTandaTerimaPolis")
											.toString(), pathTemp,
									"pathTandaTerimaPolis.pdf", paramsPolis, conn);
			
					if (!isDmtm) {
						if (viewUlink.size() != 0)
							JasperUtils.exportReportToPdfNoLock(
									paramsPolis.get("pathAlokasiDana").toString(),
									pathTemp, "pathAlokasiDana.pdf", paramsPolis,
									conn);
			
					}
					if (flagPrePrinted == 2){
						List daftarSebelumnya = uwManager.selectSimasCard(spaj);
						List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj);

						if((!daftarSebelumnya.isEmpty()) && isAgen.isEmpty()){
							String spaj2 = (String) ((Map) daftarSebelumnya.get(0)).get("REG_SPAJ");
							if(spaj2.equals(spaj)){
								JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathSuratSimasCard").toString(),pathTemp, "pathSuratSimasCard.pdf", paramsPolis, conn);
							}
						}
					}
					CekPelengkap.generateFileRDS(cabang, pathTemp, path,
							pathTemplate1, pathTemplate2, lsbs_id, mspoProvider,
							flagUlink, punyaSimascard, flagPrePrinted, null, props,
							elionsManager);

					return hasilGenerate;
				}catch (Exception e) {
					String msps_desc = "GENERATE OUTSOURCE SCHEDULER (E-LIONS)";
					logger.error("ERROR :", e);					
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, Integer.parseInt(user.getLus_id()), new Date(), null, true, props.getProperty("admin.ajsjava"), new String[]{"mark.valentino@sinarmasmsiglife.co.id"}, null, null,  
							"Error GENERATE OUTSOURCE SCHEDULER (E-LIONS)", 
							e+"", null, spaj);								
//					hasilGenerate = "error";
//					throw e;		
					hasilGenerate = "Terjadi kesalahan dalam generate Polis";
//					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					bacManager.updateMst_policyEmptyPrintDate(spaj);
					uwManager.getUwDao().deleteMstPositionSpajGO(spaj, msps_desc);
					uwManager.getUwDao().updateMstTransHistory(spaj, "tgl_print_polis", null, null, null);			
				}finally {
					closeConnection(conn);
					return hasilGenerate;
				}

			}			
}