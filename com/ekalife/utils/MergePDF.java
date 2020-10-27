package com.ekalife.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Gabungin pdf
 * @author Yusup_A
 * @since Oct 14, 2008 (4:55:08 PM)
 * 
 * @param : List dari pdf, String nama output pdf, Boolean no halaman 
 * @return : pdf sesuai output			
 */
public class MergePDF {
	
	protected static final Log logger = LogFactory.getLog( MergePDF.class );

		  public static boolean concatPDFs(List<String> streamOfPDFFiles, OutputStream outputStream, boolean paging) {

		    Document document = new Document();
		    try {
		      List<String> pdfs = streamOfPDFFiles;
		      List<PdfReader> readers = new ArrayList<PdfReader>();
		      int totalPages = 0;
		      Iterator<String> iteratorPDFs = pdfs.iterator();

		      // Create Readers for the pdfs.
		      while (iteratorPDFs.hasNext()) {
		        String pdf = iteratorPDFs.next();
		        PdfReader pdfReader = new PdfReader(pdf);
		        readers.add(pdfReader);
		        totalPages += pdfReader.getNumberOfPages();
		      }
		      // Create a writer for the outputstream
		      PdfWriter writer = PdfWriter.getInstance(document, outputStream);

		      document.open();
		      BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		      PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
		      // data

		      PdfImportedPage page;
		      int currentPageNumber = 0;
		      int pageOfCurrentReaderPDF = 0;
		      Iterator<PdfReader> iteratorPDFReader = readers.iterator();

		      // Loop through the PDF files and add to the output.
		      while (iteratorPDFReader.hasNext()) {
		        PdfReader pdfReader = iteratorPDFReader.next();

		        // Create a new page in the target for each source page.
		        while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
		          document.newPage();
		          pageOfCurrentReaderPDF++;
		          currentPageNumber++;
		          page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
		          cb.addTemplate(page, 0, 0);

		          // Code for pagination.
		          if (paging) {
		            cb.beginText();
		            cb.setFontAndSize(bf, 9);
		            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 7, 0);
		            cb.endText();
		          }
		        }
		        pageOfCurrentReaderPDF = 0;
		      }
		      outputStream.flush();
		      document.close();
		      outputStream.close();
		    } catch (Exception e) {
		      logger.error("ERROR :", e);
		    } finally {
		      if (document.isOpen())
		        document.close();
		      try {
		        if (outputStream != null)
		          outputStream.close();
		      } catch (IOException ioe) {
		        logger.error(ioe);
		      }
		    }
		    return true;
		  }
}
