package com.ekalife.elions.web.uw.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.view.PDFViewer;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class WordingPdfViewer extends AbstractPdfView {

	private ElionsManager elionsManager;
	private UwManager uwManager;
	private Properties props;
	
	public static boolean checkFileProduct(ElionsManager elionsManager, UwManager uwManager, Properties props, String spaj) {
		List detBisnis = elionsManager.selectDetailBisnis(spaj);
		Date begdate=uwManager.selectBegDateInsured(spaj);
		String produkUtama = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String detProdukUtama = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");		
		String dir=new String();
		//String dir = props.getProperty("pdf.dir.syaratpolis");
		for(int i=0; i<detBisnis.size(); i++) {
			Map m = (HashMap) detBisnis.get(i);
			File file = PDFViewer.productFile(elionsManager, uwManager, dir, spaj, m, props);
			if(file!=null) if(file.exists()) return true;
			file = PDFViewer.riderFile(elionsManager,uwManager, dir, produkUtama, detProdukUtama, m,spaj, props);
			if(file!=null) if(file.exists()) return true;
		}
		return false;
	}
	
	public static List<File> listFileProduct(ElionsManager elionsManager, UwManager uwManager, Properties props, String spaj) {
		List detBisnis = elionsManager.selectDetailBisnis(spaj);
		String produkUtama = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String detProdukUtama = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		String dir = props.getProperty("pdf.dir.syaratpolis");
		List<File> hasil = new ArrayList<File>();
		for(int i=0; i<detBisnis.size(); i++) {
			Map m = (HashMap) detBisnis.get(i);
			File file = PDFViewer.productFile(elionsManager, uwManager, dir, spaj, m, props);
			if(file!=null) if(file.exists()) hasil.add(file);
			file = PDFViewer.riderFile(elionsManager,uwManager, dir, produkUtama, detProdukUtama, m,spaj, props);
			if(file!=null) if(file.exists()) hasil.add(file);
		}
		return hasil;
	}
	
	@Override
	protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
		ServletContext servletContext = getServletContext();
		this.elionsManager = (ElionsManager) Common.getBean(servletContext, "elionsManager");
		this.uwManager = (UwManager) Common.getBean(servletContext, "uwManager");
		this.props = (Properties) Common.getBean(servletContext, "ekaLifeConfigurations");	
		
		return PdfWriter.getInstance(document, os);
	}
	
	@Override
	protected void prepareWriter(Map model, PdfWriter writer, HttpServletRequest request) throws DocumentException {
		Integer pdfPermissions = (Integer) model.get("pdfPermissions");
		if(pdfPermissions != null)
			writer.setEncryption(true, null, null, pdfPermissions);
		else
			writer.setEncryption(true, null, null, 0);
	}
	
	@Override
	protected void buildPdfDocument(Map map, Document document, PdfWriter writer, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String spaj = map.get("spaj").toString();
		List list = listFileProduct(elionsManager, uwManager, props, spaj);
		
		if(list.size()==0) {
			ServletOutputStream out = response.getOutputStream();
			out.println("<script>alert('Tidak ada file Syarat-syarat Umum / Khusus.');</script>");
			out.flush();
			return;
		}else {
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline;filename=file.pdf");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			showPdf(spaj, list, map, response.getOutputStream(), document, writer);
		}
		
	}
	
	private void showPdf(String spaj, List pdfFiles, Map model, ServletOutputStream os, Document document, PdfWriter writer) throws Exception{

		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		
		boolean saveToPdf = (Boolean) model.get("saveToPdf");
		
		PdfCopy pdfCopy = null;
		if(saveToPdf) {
	        File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
			FileOutputStream fileOutput = new FileOutputStream(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+props.getProperty("pdf.ssu")+".pdf");
			pdfCopy = new PdfCopy(document, fileOutput);
		}
		
		document.open();
		//writer.addJavaScript("this.print(true);", false);

		//pdfFiles berisi semua SSU/SSK dari suatu polis (produk utama maupun rider)
		for(int i=0; i<pdfFiles.size(); i++) {
			String pdfFile = pdfFiles.get(i).toString();
			PdfReader reader = new PdfReader(pdfFile);
	        PdfContentByte cb = writer.getDirectContent();
	        if(saveToPdf) {
		        for (int j=1; j <= reader.getNumberOfPages(); j++){
		        	//2. simpan ke file
	                pdfCopy.addPage(pdfCopy.getImportedPage(reader, j));
		        }
	        }else {
		        for (int j=1; j <= reader.getNumberOfPages(); j++){
		        	//1. gabungkan PDF nya
		        	document.newPage();
		        	cb.addTemplate(writer.getImportedPage(reader, j), 0,0);
		        }
	        }
		}

        if(saveToPdf) {
        	pdfCopy.close();
        }
        
	}
	
	/**
	 * @author Yusuf
	 * @deprecated
	 * Contoh untuk menggabung semua PDF, tambahkan password, 
	 * bahkan bisa sekaligus tambahkan permission untuk save/print pada PDF yg di hasilkan
	 */
/*	private void combineAllPdfInDirectory(String pdfDir, ServletOutputStream os, Document document, PdfWriter writer) throws Exception{
		document = new Document();
        writer = PdfWriter.getInstance(document, os);
		writer.setCloseStream(false);
//		writer.setEncryption(true, "yusuf", "sutarko", PdfWriter.AllowScreenReaders);
		document.open();
//		writer.addJavaScript("this.print(true);", false);
        PdfContentByte cb = writer.getDirectContent();

		File inputDir = new File(pdfDir);
		//inputDir.listFiles().length
		for(int k=0; k<5; k++) { 
			File file = inputDir.listFiles()[k];
			String namaFile = file.getAbsolutePath();
			if(file.getName().toLowerCase().endsWith(".pdf")) {
				//logger.info(file.getName());
				PdfReader reader = new PdfReader(namaFile);
		        for (int i=1; i <= reader.getNumberOfPages(); i++){
		        	document.newPage();
		        	cb.addTemplate(writer.getImportedPage(reader, i), 0,0);
		        }
	        }
		}
        document.close();
	}*/
	//	combineAllPdfInDirectory(
	//	props.getProperty("pdf.dir.syaratpolis"), 
	//	response.getOutputStream(), 
	//	document, 
	//	writer);
	
}