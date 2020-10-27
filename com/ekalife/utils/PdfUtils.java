package com.ekalife.utils;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.common.resources.Resources;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageLabels;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author Yusuf
 * @since 14/11/2006
 */
public class PdfUtils {
	
	protected static final Log logger = LogFactory.getLog( PdfUtils.class );

	public static PdfReader getReader(String path, String ownerPassword) throws IOException{
		if(ownerPassword!=null) return new PdfReader(path, ownerPassword.getBytes());
		else return new PdfReader(path);
	}
	
	public static boolean isExist(String path) {
		File file = new File(path);
		return file.exists();
	}
	
	public static void showPDF(String path, String ownerPassword, PdfWriter writer, Document document, HttpServletResponse response) throws Exception{
		try {
			PdfReader reader = getReader(path, ownerPassword);
	        PdfContentByte cb = writer.getDirectContent();
	        for (int j=1; j <= reader.getNumberOfPages(); j++){
	        	document.newPage();
	        	cb.addTemplate(writer.getImportedPage(reader, j), 0,0);
	        }
		}catch(IOException ioe) {
			//logger.info("ERROR IN showPDF");
			logger.error(ioe);
//    		ServletOutputStream out = response.getOutputStream();
//    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
//    		out.flush();
		}
	}
	
	public static void buildPdfHeaders(HttpServletResponse response) {
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline;filename=file.pdf");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");		
	}
	
	public static void buildPdfMetadata(Document document, Properties props, String keywords) {
		document.addTitle(props.getProperty("pdf.title.polis"));
		document.addSubject(props.getProperty("pdf.title.polis"));
		document.addAuthor(props.getProperty("company.name"));
		document.addKeywords(keywords);
		//document.addCreator("http://yusufsutarko.googlepages.com/resume"); // ^_^ \\
		document.addCreationDate();
	}
	
	public static void combinePdf(List<File> fileList, String destDirectory, String destFileName) throws Exception{
		if(!fileList.isEmpty()) {
			File destDir = new File(destDirectory);
	        if(!destDir.exists()) destDir.mkdirs();			
			
			Document document = new Document(PageSize.A4);
			FileOutputStream fileOutput = new FileOutputStream(destDirectory+"\\"+destFileName);
			PdfCopy pdfCopy = new PdfCopy(document, fileOutput);
			try{
				document.open();
				
				for(int i=0; i<fileList.size(); i++) {
					String pdfFile = fileList.get(i).toString();
					PdfReader reader = new PdfReader(pdfFile);
			        for (int j=1; j <= reader.getNumberOfPages(); j++){
			        	//simpan ke file
		                pdfCopy.addPage(pdfCopy.getImportedPage(reader, j));
			        }
			        reader.close();
				}
			}finally{
				pdfCopy.flush();
				pdfCopy.close();
				fileOutput.flush();
				fileOutput.close();
				document.close();
			}
			
			
		}
	}
	
	public static File addBarcodeAndLogo(List<DropDown> fileList, File file, Properties props, String userPassword) throws Exception{
		// BARCODE NYA BELOM, LOGO DULU
		
		//cek dulu, yang mesti dikasih watermark logo sinarmaslife yang mana aja
		//ALL = semua kasih logo
		//NONE = semua tidak logo
		//FIRST_PAGE_ONLY = hanya halaman pertama saja
		List<Integer> noLogo = new ArrayList<Integer>();
		int totalHalaman = 0;
		for(DropDown d : fileList) {
			File testExist = new File(d.getKey());
			if(testExist.exists()) { //cek dulu ada gak filenya
				PdfReader reader = null;
				try {
					reader = new PdfReader(d.getKey(), props.getProperty("pdf.userPassword").getBytes());
				} catch (IOException ioe) {
					try {
						reader = new PdfReader(d.getKey());
					} catch (IOException ioe2) {
					}
				}
				if(d.getDesc().equals("ALL")) {
					
				} else if(d.getDesc().equals("NONE")) {
					for(int i=0; i<reader.getNumberOfPages(); i++) {
						noLogo.add(totalHalaman + (i+1));
					}
				} else if(d.getDesc().equals("FIRST_PAGE_ONLY")) {
					for(int i=0; i<reader.getNumberOfPages(); i++) {
						if(i!=0) noLogo.add(totalHalaman + (i+1));
					}
				}
				totalHalaman += reader.getNumberOfPages();
			}
		}
		
        // we create a reader for a certain document
		FileInputStream is = new FileInputStream(file);
        PdfReader reader = new PdfReader(is, (userPassword==null)?null:userPassword.getBytes());
        int n = reader.getNumberOfPages();
        // we create a stamper that will copy the document to a new file
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(file));
        if(userPassword!=null){
			stamp.setEncryption(
					props.getProperty("pdf.ownerPassword").getBytes(), 
					userPassword.getBytes(),
					//"yusuf".getBytes(),
					PdfWriter.AllowPrinting, false);
        }
        // adding some metadata
        HashMap moreInfo = new HashMap();
        moreInfo.put("Author", "PT Asuransi Jiwa Sinarmas MSIG Tbk.");
        stamp.setMoreInfo(moreInfo);
        // adding content to each page
        int i = 0;
        PdfContentByte under;
        Image img = Image.getInstance(Resources.getResourceAsFile(props.getProperty("images.ttd.ekalife.logo.konven")).getAbsolutePath());
        img.setAbsolutePosition(35, 785);
        img.scaleToFit(210, 30);
        while (i < n) {
        	boolean cetak = true;
        	i++;
        	if(i>1) {
        		for(Integer j : noLogo) {
        			if(i==j) cetak = false;
        		}
        		if(cetak) {
		        	// watermark OVER / UNDER the existing page
		        	under = stamp.getOverContent(i);
		        	under.addImage(img);
        		}
        	}
        }
        // closing PdfStamper will generate the new PDF file
        stamp.close();
        is.close();
        
        return file;
	}
	
	//08082019 iga ukiarwan penambahan file images syariah 
	public static File addBarcodeAndLogoSyariah(List<DropDown> fileList, File file, Properties props, String userPassword) throws Exception{

		List<Integer> noLogo = new ArrayList<Integer>();
		int totalHalaman = 0;
		for(DropDown d : fileList) {
			File testExist = new File(d.getKey());
			if(testExist.exists()) { //cek dulu ada gak filenya
				PdfReader reader = null;
				try {
					reader = new PdfReader(d.getKey(), props.getProperty("pdf.userPassword").getBytes());
				} catch (IOException ioe) {
					try {
						reader = new PdfReader(d.getKey());
					} catch (IOException ioe2) {
					}
				}
				if(d.getDesc().equals("ALL")) {
					
				} else if(d.getDesc().equals("NONE")) {
					for(int i=0; i<reader.getNumberOfPages(); i++) {
						noLogo.add(totalHalaman + (i+1));
					}
				} else if(d.getDesc().equals("FIRST_PAGE_ONLY")) {
					for(int i=0; i<reader.getNumberOfPages(); i++) {
						if(i!=0) noLogo.add(totalHalaman + (i+1));
					}
				}
				totalHalaman += reader.getNumberOfPages();
			}
		}
		
        // we create a reader for a certain document
		FileInputStream is = new FileInputStream(file);
        PdfReader reader = new PdfReader(is, (userPassword==null)?null:userPassword.getBytes());
        int n = reader.getNumberOfPages();
        // we create a stamper that will copy the document to a new file
        PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(file));
        if(userPassword!=null){
			stamp.setEncryption(
					props.getProperty("pdf.ownerPassword").getBytes(), 
					userPassword.getBytes(),
					//"yusuf".getBytes(),
					PdfWriter.AllowPrinting, false);
        }
        // adding some metadata
        HashMap moreInfo = new HashMap();
        moreInfo.put("Author", "PT Asuransi Jiwa Sinarmas MSIG Tbk.");
        stamp.setMoreInfo(moreInfo);
        // adding content to each page
        int i = 0;
        PdfContentByte under;
        Image img = Image.getInstance(Resources.getResourceAsFile(props.getProperty("images.ttd.ekalife.logo.syariah")).getAbsolutePath());
        img.setAbsolutePosition(35, 785);
        img.scaleToFit(210, 30);
        while (i < n) {
        	boolean cetak = true;
        	i++;
        	if(i>1) {
        		for(Integer j : noLogo) {
        			if(i==j) cetak = false;
        		}
        		if(cetak) {
		        	// watermark OVER / UNDER the existing page
		        	under = stamp.getOverContent(i);
		        	under.addImage(img);
        		}
        	}
        }
        // closing PdfStamper will generate the new PDF file
        stamp.close();
        is.close();
        
        return file;
	}

	/*
	        //insert barcode
            BarcodePDF417 pdf417 = new BarcodePDF417();
            String text = "It was the best of times, it was the worst of times, " + 
                "it was the age of wisdom, it was the age of foolishness, " +
                "it was the epoch of belief, it was the epoch of incredulity, " +
                "it was the season of Light, it was the season of Darkness, " +
                "it was the spring of hope, it was the winter of despair, " +
                "we had everything before us, we had nothing before us, " +
                "we were all going direct to Heaven, we were all going direct " +
                "the other way - in short, the period was so far like the present " +
                "period, that some of its noisiest authorities insisted on its " +
                "being received, for good or for evil, in the superlative degree " +
                "of comparison only.";
            pdf417.setText(text);
//            Image img = pdf417.getImage();
//            img.scaleAbsolute(200, 22);
//	        img.setAlignment(Image.MIDDLE);
//	        document.add(img);

            Image img2 = pdf417.getImage();
            img2.scaleAbsolute(220, 22);
            img2.setAbsolutePosition(180, 250);
	        document.add(img2);
	 */
	public static File combinePdfWithOutline(List<DropDown> fileList, String destDirectory, String destFileName, Properties props, String userPassword) throws Exception{
		File result = null;
		
		if(!fileList.isEmpty()) {
			
			//1. cek dulu, directory tujuan udah ada belum
			File destDir = new File(destDirectory);
	        if(!destDir.exists()) destDir.mkdirs();			
			
			Document document = new Document(PageSize.A4);
			PdfCopy pdfCopy = null;
			FileOutputStream fileOutput = null;
			
			try {
				fileOutput = new FileOutputStream(destDirectory+"\\"+destFileName);
				pdfCopy = new PdfCopy(document, fileOutput);
	
				pdfCopy.setViewerPreferences(PdfWriter.PageModeUseOutlines);
				if(userPassword!=null){
					pdfCopy.setEncryption(
							props.getProperty("pdf.ownerPassword").getBytes(), 
							userPassword.getBytes(),
							//"yusuf".getBytes(),
							PdfWriter.AllowPrinting, false);
				}

				document.addTitle("PT Asuransi Jiwa Sinarmas MSIG Tbk.");
				document.addSubject("PT Asuransi Jiwa Sinarmas MSIG Tbk.");
				document.addAuthor("PT Asuransi Jiwa Sinarmas MSIG Tbk.");
				document.addCreationDate();
				
				document.open();
				
				int page=1;
	            PdfPageLabels pageLabels = new PdfPageLabels();
	            String currentOutlineCaption = "";
	            PdfOutline parentOutline = pdfCopy.getRootOutline();
	            
				for(int i=0; i<fileList.size(); i++) { //looping sebanyak file2
					DropDown file = fileList.get(i);
					String pdfFile = file.getKey();
					File testExist = new File(pdfFile);
					if(testExist.exists()) { //cek dulu ada gak filenya
						PdfReader reader = null;
						try {
							reader = new PdfReader(pdfFile, props.getProperty("pdf.userPassword").getBytes());
						} catch (IOException ioe) {
							try {
								reader = new PdfReader(pdfFile);
							} catch (IOException ioe2) {}
						}
				        for (int j=1; j <= reader.getNumberOfPages(); j++){ //looping sebanyak jumlah halaman dalam 1 file
				        	//gabungin file2 tadi, per halaman
			                pdfCopy.addPage(pdfCopy.getImportedPage(reader, j));
			                if(!currentOutlineCaption.equals(file.getValue())) { //kasih caption, hanya kalo judul halamannya sama
				                //tambah Outline (list halaman disebelah kiri)
				                currentOutlineCaption = file.getValue();
				                PdfAction bookmark = PdfAction.gotoLocalPage(page, new PdfDestination(PdfDestination.XYZ, -1, -1, 0), pdfCopy);
				                if(i==0) {
				                	parentOutline = new PdfOutline(parentOutline, bookmark, file.getValue());
				                } else new PdfOutline(parentOutline, bookmark, file.getValue());
				                
				    			//tambah PageLabel (caption dari thumbnail2 halaman) 
				                pageLabels.addPageLabel(page, PdfPageLabels.EMPTY, file.getValue());
			                }
			                //
			                page++;
				        }
			        }
				}

	            pdfCopy.setPageLabels(pageLabels);
				document.close();
				pdfCopy.close();
				fileOutput.close();
			} catch(Exception e) {
				logger.error("ERROR :", e);
				document.close();
				pdfCopy.close();
				fileOutput.close();
				throw e;
			}
			
			result = new File(destDirectory+"\\"+destFileName);
		}
		
		return result;
	}

//	public static void addBarcode(String barcodeText, String fileName) {
//
//	        
//	}
	//08082019 iga ukiarwan penambahan file images syariah 
	public static void addTableOfContentsSyariah(List<DropDown> fileList, Properties props, String dest) {

		String fileName="firstpage.pdf";
		fileList.add(0, new DropDown(dest+fileName, "Daftar Isi", "ALL"));		
		Map<String, Integer> halaman = new HashMap<String, Integer>();
		
		//ambilin jumlah halaman setiap file
        for(int i=0; i<fileList.size(); i++) {
        	String pdfFile = fileList.get(i).getKey();
        	PdfReader reader;
			try {
				reader = new PdfReader(pdfFile, props.getProperty("pdf.userPassword").getBytes());
	        	halaman.put(fileList.get(i).getValue(), reader.getNumberOfPages());
			} catch (IOException e) {
				try {
					reader = new PdfReader(pdfFile);
		        	halaman.put(fileList.get(i).getValue(), reader.getNumberOfPages());
				} catch (IOException e2) {
				} //sengaja di catch untuk menghindari exception apabila tidak ada filenya
			} //sengaja di catch untuk menghindari exception apabila tidak ada filenya
        }
        
		Document document = new Document();
		PdfWriter writer = null;
		 
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(dest+fileName));
	        document.open();

	        //tabel utama
	        PdfPTable tabel = new PdfPTable(2);
	        
	        //insert image ekalife diatas
	        Image jpg = Image.getInstance(Resources.getResourceURL(props.getProperty("images.ttd.ekalife.logo.syariah")));
//	        jpg.scaleToFit(400, 400);
	        jpg.scaleToFit(210, 30);
//	        jpg.setAbsolutePosition(800, 600);
	        jpg.setAbsolutePosition(40, 785);
	        jpg.setAlignment(Image.LEFT);
//	        logger.info(jpg.getAbsoluteX());
//	        logger.info(jpg.getAbsoluteY());
	        PdfPCell cell = new PdfPCell(jpg);
	        cell.setColspan(2);
	        cell.setBorder(PdfPCell.NO_BORDER);
	        tabel.addCell(cell);
	        
	        //insert judul "Daftar Isi"
	        cell = new PdfPCell(new Phrase("\nDaftar Isi", FontFactory.getFont(FontFactory.HELVETICA, 36)));
	        cell.setColspan(2);
	        cell.setBorder(PdfPCell.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        tabel.addCell(cell);

	        int page=2;
	        
	        for(int i=0; i<fileList.size(); i++) {
	        	String pdfFile = fileList.get(i).getKey();
	        	File testExist = new File(pdfFile);
//	        	logger.info(i);
//	        	logger.info(pdfFile);
//	        	logger.info(testExist.exists());
	        	if(testExist.exists() && !fileList.get(i).getValue().equals("Daftar Isi")) {
		        	//judul halaman
		        	cell = new PdfPCell(new Phrase("\n- " + fileList.get(i).getValue(), FontFactory.getFont(FontFactory.HELVETICA, 16)));
		        	cell.setNoWrap(true);
			        cell.setBorder(PdfPCell.NO_BORDER);
		        	tabel.addCell(cell);
		        	//halaman
		        	cell = new PdfPCell(new Phrase("\n"+page, FontFactory.getFont(FontFactory.HELVETICA, 16)));
			        cell.setBorder(PdfPCell.NO_BORDER);
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        	tabel.addCell(cell);
		        	page+=halaman.get(fileList.get(i).getValue()); 
	        	}
	        }

        	document.add(tabel);

		}catch(Exception e) {
			logger.error("ERROR :", e);
		}finally {
	        document.close();
	        writer.close();
		}
	}
	
	public static void addTableOfContents(List<DropDown> fileList, Properties props, String dest) {

		String fileName="firstpage.pdf";
		fileList.add(0, new DropDown(dest+fileName, "Daftar Isi", "ALL"));		
		Map<String, Integer> halaman = new HashMap<String, Integer>();
		
		//ambilin jumlah halaman setiap file
        for(int i=0; i<fileList.size(); i++) {
        	String pdfFile = fileList.get(i).getKey();
        	PdfReader reader;
			try {
				reader = new PdfReader(pdfFile, props.getProperty("pdf.userPassword").getBytes());
	        	halaman.put(fileList.get(i).getValue(), reader.getNumberOfPages());
			} catch (IOException e) {
				try {
					reader = new PdfReader(pdfFile);
		        	halaman.put(fileList.get(i).getValue(), reader.getNumberOfPages());
				} catch (IOException e2) {
				} //sengaja di catch untuk menghindari exception apabila tidak ada filenya
			} //sengaja di catch untuk menghindari exception apabila tidak ada filenya
        }
        
		Document document = new Document();
		PdfWriter writer = null;
		 
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(dest+fileName));
	        document.open();

	        //tabel utama
	        PdfPTable tabel = new PdfPTable(2);
	        
	        //insert image ekalife diatas
	        Image jpg = Image.getInstance(Resources.getResourceURL(props.getProperty("images.ttd.ekalife.logo.konven")));
//	        jpg.scaleToFit(400, 400);
	        jpg.scaleToFit(210, 30);
//	        jpg.setAbsolutePosition(800, 600);
	        jpg.setAbsolutePosition(40, 785);
	        jpg.setAlignment(Image.LEFT);
//	        logger.info(jpg.getAbsoluteX());
//	        logger.info(jpg.getAbsoluteY());
	        PdfPCell cell = new PdfPCell(jpg);
	        cell.setColspan(2);
	        cell.setBorder(PdfPCell.NO_BORDER);
	        tabel.addCell(cell);
	        
	        //insert judul "Daftar Isi"
	        cell = new PdfPCell(new Phrase("\nDaftar Isi", FontFactory.getFont(FontFactory.HELVETICA, 36)));
	        cell.setColspan(2);
	        cell.setBorder(PdfPCell.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        tabel.addCell(cell);

	        int page=2;
	        
	        for(int i=0; i<fileList.size(); i++) {
	        	String pdfFile = fileList.get(i).getKey();
	        	File testExist = new File(pdfFile);
//	        	logger.info(i);
//	        	logger.info(pdfFile);
//	        	logger.info(testExist.exists());
	        	if(testExist.exists() && !fileList.get(i).getValue().equals("Daftar Isi")) {
		        	//judul halaman
		        	cell = new PdfPCell(new Phrase("\n- " + fileList.get(i).getValue(), FontFactory.getFont(FontFactory.HELVETICA, 16)));
		        	cell.setNoWrap(true);
			        cell.setBorder(PdfPCell.NO_BORDER);
		        	tabel.addCell(cell);
		        	//halaman
		        	cell = new PdfPCell(new Phrase("\n"+page, FontFactory.getFont(FontFactory.HELVETICA, 16)));
			        cell.setBorder(PdfPCell.NO_BORDER);
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        	tabel.addCell(cell);
		        	page+=halaman.get(fileList.get(i).getValue()); 
	        	}
	        }

        	document.add(tabel);

		}catch(Exception e) {
			logger.error("ERROR :", e);
		}finally {
	        document.close();
	        writer.close();
		}
	}
	
	public static String createFirstPage(Properties props, String dest) {
		String fileName="firstpage.pdf";
		Document document = new Document();
		PdfWriter writer = null;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(dest+fileName));
	        document.open();
	        Image jpg = Image.getInstance(Resources.getResourceURL(props.getProperty("images.ttd.ekalife.large")));
	        jpg.scaleToFit(400, 400);
	        jpg.setAlignment(Image.MIDDLE);
	        document.add(jpg);
		}catch(Exception e) {
			logger.error("ERROR :", e);
		}finally {
			writer.close();
	        document.close();
		}
        return dest+fileName;
	}

}