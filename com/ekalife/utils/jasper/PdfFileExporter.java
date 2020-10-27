package com.ekalife.utils.jasper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRExportProgressMonitor;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.utils.FormatString;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Class pembantu untuk jasperreports dalam export ke PDF, sebenarnya class ini sudah disediakan
 * oleh jasper, yaitu class JRPdfExporter, namun ada beberapa hal yang harus di customize ulang
 * sehingga terpaksa dibuat sebuah class baru, contohnya, saat export PDF ke layar, sekaligus simpan
 * ke dalam file
 * 
 * @author Yusuf Sutarko
 * @since Feb 1, 2007 (11:24:52 AM)
 */
public class PdfFileExporter extends JRPdfExporter implements Serializable{

	private Map fontMap;
	private Map expParams;
	private String spaj;
	private String cabang;
	private String fileName;
	private Properties props;
	private boolean isSilentPrint;
	private HttpServletResponse response;
	private String va;
	private boolean isViewer;
	
	public void setViewer(boolean isViewer) {this.isViewer = isViewer;}
	public void setResponse(HttpServletResponse response) {this.response = response;}
	public void setSilentPrint(boolean isSilentPrint) {this.isSilentPrint = isSilentPrint;}
	public void setCabang(String cabang) {this.cabang = cabang;}
	public void setProps(Properties props) {this.props = props;}
	public void setExpParams(Map expParams) {this.expParams = expParams;}
	public void setFileName(String fileName) {this.fileName = fileName;}
	public void setSpaj(String spaj) {this.spaj = spaj;}
	public void setVa(String va) {this.va = va;}
	
	public PdfFileExporter(HttpServletResponse response, Properties props, Map expParams, String spaj, String cabang, 
			String fileName, boolean isSilentPrint, String va, boolean isViewer) {
		this.response = response;
		this.props = props;
		this.expParams = expParams;
		this.spaj = spaj;
		this.cabang = cabang;
		this.fileName = fileName;
		this.va = va;
		this.isViewer = isViewer;
		setSilentPrint(isSilentPrint);
	}
	
	private void responseHeader(HttpServletResponse response, int contentLength) {
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");				
		response.setContentType("application/pdf");
		response.setContentLength(contentLength);
	}
	
	public void exportReport() throws JRException
	{
		registerFonts();
		
		progressMonitor = (JRExportProgressMonitor)parameters.get(JRExporterParameter.PROGRESS_MONITOR);
		
		/*   */
		setOffset();

		try
		{
			/*   */
			setExportContext();
	
			/*   */
			setInput();
	
			/*   */
			if (!isModeBatch)
			{
				setPageRange();
			}
			
			Boolean isCreatingBatchModeBookmarksParameter = (Boolean)parameters.get(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS);
			if(isCreatingBatchModeBookmarksParameter != null){
				isCreatingBatchModeBookmarks = isCreatingBatchModeBookmarksParameter.booleanValue();
			}
	
			Boolean isCompressedParameter = (Boolean)parameters.get(JRPdfExporterParameter.IS_COMPRESSED);
			if (isCompressedParameter != null)
			{
				isCompressed = isCompressedParameter.booleanValue();
			}
			
			Boolean isEncryptedParameter = (Boolean)parameters.get(JRPdfExporterParameter.IS_ENCRYPTED);
			if (isEncryptedParameter != null)
			{
				isEncrypted = isEncryptedParameter.booleanValue();
			}
			
			Boolean is128BitKeyParameter = (Boolean)parameters.get(JRPdfExporterParameter.IS_128_BIT_KEY);
			if (is128BitKeyParameter != null)
			{
				is128BitKey = is128BitKeyParameter.booleanValue();
			}
			
			userPassword = (String)parameters.get(JRPdfExporterParameter.USER_PASSWORD);
			ownerPassword = (String)parameters.get(JRPdfExporterParameter.OWNER_PASSWORD);
	
			Integer permissionsParameter = (Integer)parameters.get(JRPdfExporterParameter.PERMISSIONS);
			if (permissionsParameter != null)
			{
				permissions = permissionsParameter.intValue();
			}
	
			pdfVersion = (Character) parameters.get(JRPdfExporterParameter.PDF_VERSION);
	
			fontMap = (Map) parameters.get(JRExporterParameter.FONT_MAP);
			
			setSplitCharacter();
	
			OutputStream os = (OutputStream)parameters.get(JRExporterParameter.OUTPUT_STREAM);
			if (os != null)
			{
				exportReportToStream(os);
				responseHeader(response, ((ByteArrayOutputStream) os).size());
			}
			else
			{
				File destFile = (File)parameters.get(JRExporterParameter.OUTPUT_FILE);
				if (destFile == null)
				{
					String fileName = (String)parameters.get(JRExporterParameter.OUTPUT_FILE_NAME);
					if (fileName != null)
					{
						destFile = new File(fileName);
					}
					else
					{
						throw new JRException("No output specified for the exporter.");
					}
				}
	
				try
				{
					os = new FileOutputStream(destFile);
					exportReportToStream(os);
					responseHeader(response, ((ByteArrayOutputStream) os).size());
					os.flush();
				}
				catch (IOException e)
				{
					throw new JRException("Error trying to export to file : " + destFile, e);
				}
				finally
				{
					if (os != null)
					{
						try
						{
							os.close();
						}
						catch(IOException e)
						{
						}
					}
				}
			}
		}
		finally
		{
			resetExportContext();
		}
	}
	
	//bagian ini lah intinya
	protected void exportReportToStream(OutputStream os) throws JRException {

		if(this.spaj!=null) {
			if(!this.spaj.equals("")) {
				//Save file ke server
				File userDir =null;
				this.fileName=this.fileName==null?"":this.fileName;
				if(this.fileName.length()>6){
					if(this.fileName.substring(0, 6).equals("TOP_UP")){
						userDir = new File(props.getProperty("pdf.dir.topup.stabil")+"\\"+cabang+"\\"+spaj);
					}else if(this.fileName.substring(0, 6).equals("POWERS")){
						userDir = new File(props.getProperty("pdf.dir.topup.psave")+"\\"+cabang+"\\"+spaj);
					}else if(this.fileName.substring(0, 6).equals("ENDORS")){
						userDir = new File(props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj);
					}else{
						userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
					}
				}else{
					userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
				}
		        if(!userDir.exists()) {
		            userDir.mkdirs();
		        }else { //if(!isSilentPrint)
		        	try {
		        		//Kalau sudah ada filenya (pernah di generate sebelumnya) dan bukan seritifikat, buka file tersebut
		        		if(!this.fileName.equals(props.getProperty("pdf.sertifikat_powersave")) && !this.fileName.equals(props.getProperty("pdf.alokasi_dana")) && !this.fileName.equals(props.getProperty("pdf.endorsemen"))) {
		        			PdfReader reader = new PdfReader(userDir.getAbsolutePath()+"\\"+fileName+".pdf");
		                    Document document = new Document(reader.getPageSizeWithRotation(1)); //Penting!!! untuk menyamakan portrait/landscape nya
		                    PdfWriter writer = PdfWriter.getInstance(document, os);
		                    //writer.setViewerPreferences(PdfWriter.HideMenubar | PdfWriter.HideToolbar);
		        			if (pdfVersion != null)
		        				writer.setPdfVersion(pdfVersion.charValue());
		        			if (isEncrypted) {
		        				writer.setEncryption(is128BitKey, userPassword,
		        						ownerPassword, permissions);
		        			}
		                    document.open();
							//Rectangle psize = reader.getPageSize(1);
							//float width = psize.width();
							//float height = psize.height();	                    
		                    PdfContentByte cb = writer.getDirectContent();
		                    for (int i=1; i <= reader.getNumberOfPages(); i++){
		                    	document.newPage();
//		                    	cb.addTemplate(writer.getImportedPage(reader, i), width*(i-1), height*(i-1));
		                    	cb.addTemplate(writer.getImportedPage(reader, i), 0, 0);
		                    }
		                    
		                    //khusus surat polis, bila ada virtual accountnya, tambahkan file panduan va disini
		                    //PENTING : bagian ini ada di 2 tempat di class ini, bila dirubah2, rubah ke satunya juga
//		                    if((this.fileName.equals(props.getProperty("pdf.surat_polis")) || this.fileName.equals(props.getProperty("pdf.polis_quadruplex"))) && va != null) {
//	                    		if(!va.trim().equals("")){
//		                    		PdfReader reader2 = new PdfReader(props.getProperty("pdf.dir.other")+"/PANDUAN_VIRTUAL_ACCOUNT.pdf");
//				                    for (int i=1; i <= reader2.getNumberOfPages(); i++){
//				                    	document.newPage();
//	//				                    	cb.addTemplate(writer.getImportedPage(reader, i), width*(i-1), height*(i-1));
//				                    	cb.addTemplate(writer.getImportedPage(reader2, i), 0, 0);
//				                    }
//	                    		}
//	                    	}
		                    
		                    document.close();
		                    return;
		        		}
		        	}catch(Exception e) {
		        		//logger.error("ERROR :", e);
		        	}
		        }
		        if(!isViewer){
					JRPdfExporter jrpdfexporter = new JRPdfExporter();
					jrpdfexporter.setParameters(expParams);
					jrpdfexporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					jrpdfexporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, userDir.getAbsolutePath()+"\\"+fileName+".pdf");
					jrpdfexporter.exportReport();
		        }
			}
		}

		document = new Document(new Rectangle(jasperPrint.getPageWidth(), jasperPrint.getPageHeight()));

		imageTesterDocument = new Document(new Rectangle(10, 10));
		
		try {
			PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
            //pdfWriter.setViewerPreferences(PdfWriter.HideMenubar | PdfWriter.HideToolbar);
			pdfWriter.setCloseStream(false);

			if (pdfVersion != null)	 pdfWriter.setPdfVersion(pdfVersion.charValue());

			if (isEncrypted) {
				pdfWriter.setEncryption(is128BitKey, userPassword, ownerPassword, permissions);
			}

			document.open();
			if(isSilentPrint) {
				pdfWriter.addJavaScript("this.print(true);", false);
			}

			pdfContentByte = pdfWriter.getDirectContent();

			initBookmarks();

			PdfWriter imageTesterPdfWriter = PdfWriter.getInstance(imageTesterDocument, new ByteArrayOutputStream());
			imageTesterDocument.open();
			imageTesterDocument.newPage();
			imageTesterPdfContentByte = imageTesterPdfWriter.getDirectContent();
			imageTesterPdfContentByte.setLiteral("\n");

			for (reportIndex = 0; reportIndex < jasperPrintList.size(); reportIndex++) {
				jasperPrint = (JasperPrint) jasperPrintList.get(reportIndex);
				loadedImagesMap = new HashMap();
				document.setPageSize(new Rectangle(jasperPrint.getPageWidth(), jasperPrint.getPageHeight()));

				List pages = jasperPrint.getPages();
				if (pages != null && pages.size() > 0) {
					if (isModeBatch) {
						document.newPage();

						if (isCreatingBatchModeBookmarks) {
							addBookmark(0, jasperPrint.getName(), 0, 0);
						}

						startPageIndex = 0;
						endPageIndex = pages.size() - 1;
					}

					Chunk chunk = null;
					ColumnText colText = null;
					JRPrintPage page = null;
					for (int pageIndex = startPageIndex; pageIndex <= endPageIndex; pageIndex++) {
						if (Thread.currentThread().isInterrupted()) {
							throw new JRException("Current thread interrupted.");
						}

						page = (JRPrintPage) pages.get(pageIndex);

						document.newPage();

						pdfContentByte = pdfWriter.getDirectContent();

						pdfContentByte.setLineCap(2);

						chunk = new Chunk(" ");
						chunk.setLocalDestination(JR_PAGE_ANCHOR_PREFIX
								+ reportIndex + "_" + (pageIndex + 1));

						colText = new ColumnText(pdfContentByte);
						colText.setSimpleColumn(new Phrase(chunk), 0,
								jasperPrint.getPageHeight(), 1, 1, 0,
								Element.ALIGN_LEFT);

						colText.go();

						exportPage(page);
					}
				} else {
					document.newPage();
					pdfContentByte = pdfWriter.getDirectContent();
					pdfContentByte.setLiteral("\n");
				}
			}
			
            //khusus surat polis, bila ada virtual accountnya, tambahkan file panduan va disini
            //PENTING : bagian ini ada di 2 tempat di class ini, bila dirubah2, rubah ke satunya juga
//			if(this.fileName != null){
//	            if((this.fileName.equals(props.getProperty("pdf.surat_polis")) || this.fileName.equals(props.getProperty("pdf.polis_quadruplex"))) && va != null) {
//	        		if(!va.trim().equals("")){
//	                    try {
//							PdfContentByte cb = pdfWriter.getDirectContent();
//							PdfReader reader = new PdfReader(props.getProperty("pdf.dir.other")+"/PANDUAN_VIRTUAL_ACCOUNT.pdf");
//							for (int i=1; i <= reader.getNumberOfPages(); i++){
//								document.newPage();
//								cb.addTemplate(pdfWriter.getImportedPage(reader, i), 0, 0);
//							}
//						} catch (IOException e) {
//							logger.error("ERROR :", e);
//						} finally{
//							document.close();
//						}
//	        		}
//	        	}
//			}
			
		} catch (DocumentException e) {
			throw new JRException("PDF Document error : " + jasperPrint.getName(), e);
		} catch (IOException e) {
			throw new JRException("Error generating PDF report : " + jasperPrint.getName(), e);
		} finally {
		
			document.close();
			imageTesterDocument.close();			
		}

		// return os.toByteArray();
	}

}
