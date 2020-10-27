package com.ekalife.utils;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Date;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.Sides;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFMergerUtility;

import com.lowagie.text.pdf.PdfReader;

public class Print implements Serializable {

	protected static final Log logger = LogFactory.getLog( Print.class );

	//Class ini digunakan untuk membantu Direct Print
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8933154953320281711L;

	//Fungsi ini digunakan untuk menghapus semua file yang digenerate
	public static Boolean deleteAllFile(File pFile){
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
	
	//Fungsi ini digunakan untuk mengetahui jumlah kertas yang dicetak untuk diinsert ke mst_print_history
	public static Integer getCountPrint(String pdfFile) throws IOException{
		Integer count = 0;
		try{
			PdfReader file = new PdfReader(pdfFile);
			if (file.getNumberOfPages() % 2 == 0){
				count = file.getNumberOfPages()/2 ;
			}else{
				count = (file.getNumberOfPages()+1)/2 ;
			}
		}catch(Exception e){
			logger.error("ERROR :", e);
		}
		return count;
	}
	
	
	/*untuk mengetahui jumlah document dipdf*/
	public static Boolean checkCountDocument(String pdfFile) throws IOException{
		Boolean flag = false ;
		PdfReader file = new PdfReader(pdfFile);
	    if(file.getNumberOfPages() % 2 == 0){
	    	flag = true;
	    }else{
	    	flag = false;
	    }
		return flag;
	}
	
	
	//Fungsi ini digunakan untuk mengenerate file pdf untuk digabungkan
	public static String generateReportMergeAndDelete(String cabang,String path,String pathTemplate,String lsbs,Integer mspoProvider,String flagLink, Integer punyaSimascard, int flagPrePrinted) throws Exception{	
		
			PDFMergerUtility before = new PDFMergerUtility();
			PDFMergerUtility after = new PDFMergerUtility();
			
			before.addSource(path+"\\"+"pathSurat.pdf");
			before.addSource(path+"\\"+"pathPolis.pdf");
			before.addSource(path+"\\"+"pathManfaat.pdf");	
			
			if (flagLink.equals("1")){
				before.addSource(path+"\\"+"pathAlokasiDana.pdf");
			}
			if(flagPrePrinted==1){				
				before.addSource(path+"\\"+"pathTandaTerimaPolis.pdf");
				before.addSource(pathTemplate);
				File userDir = new File(path+"\\"+"pathSS.pdf");
				if (userDir.exists())before.addSource(path+"\\"+"pathSS.pdf");
				if(mspoProvider==2){
					File admedika = new File(path+"\\"+"pathAdmedika.pdf");
			        if(admedika.exists()){
						before.addSource(path+"\\"+"pathAdmedika.pdf");
			        }
				}
				before.setDestinationFileName(path+"\\"+"pathTemp.pdf");
				before.mergeDocuments();
				if(checkCountDocument(path+"\\"+"pathTemp.pdf")){
					after.addSource(path+"\\"+"pathTemp.pdf");						
				}else if(!checkCountDocument(path+"\\"+"pathTemp.pdf")){
					after.addSource(path+"\\"+"pathTemp.pdf");
					after.addSource(pathTemplate);
				}
			}else if(flagPrePrinted==2){
				before.addSource(path+"\\"+"pathTandaTerimaPolis.pdf");
				before.addSource(pathTemplate);
				File userDir = new File(path+"\\"+"pathSS.pdf");
				if (userDir.exists())before.addSource(path+"\\"+"pathSS.pdf");
				if(mspoProvider==2){
					File admedika = new File(path+"\\"+"pathAdmedika.pdf");
			        if(admedika.exists()){
						before.addSource(path+"\\"+"pathAdmedika.pdf");
			        }
				}
				before.setDestinationFileName(path+"\\"+"pathTemp.pdf");
				before.mergeDocuments();
				
				if(punyaSimascard==1){
					if(checkCountDocument(path+"\\"+"pathTemp.pdf")){
						after.addSource(path+"\\"+"pathTemp.pdf");
						after.addSource(path+"\\"+"pathSuratSimasCard.pdf");
					}else if(!checkCountDocument(path+"\\"+"pathTemp.pdf")){
						after.addSource(path+"\\"+"pathTemp.pdf");
						after.addSource(pathTemplate);
						after.addSource(path+"\\"+"pathSuratSimasCard.pdf");
					}
				}else{
					if(checkCountDocument(path+"\\"+"pathTemp.pdf")){
						after.addSource(path+"\\"+"pathTemp.pdf");						
					}else if(!checkCountDocument(path+"\\"+"pathTemp.pdf")){
						after.addSource(path+"\\"+"pathTemp.pdf");
						after.addSource(pathTemplate);
					}	
				}
				
			}else if(flagPrePrinted==3){//untuk Produk BTN 				
				before.addSource(path+"\\"+"pathTandaTerimaPolis.pdf");
				before.addSource(pathTemplate);
				File userDir = new File(path+"\\"+"pathSSU_SSK.pdf");
				if (userDir.exists())before.addSource(path+"\\"+"pathSSU_SSK.pdf");
				File userDir2 = new File(path+"\\"+"pathSPP.pdf");
				if (userDir2.exists())before.addSource(path+"\\"+"pathSPP.pdf");				
				before.setDestinationFileName(path+"\\"+"pathTemp.pdf");
				before.mergeDocuments();
				if(checkCountDocument(path+"\\"+"pathTemp.pdf")){
					after.addSource(path+"\\"+"pathTemp.pdf");						
				}else if(!checkCountDocument(path+"\\"+"pathTemp.pdf")){
					after.addSource(path+"\\"+"pathTemp.pdf");
					after.addSource(pathTemplate);
				}
			}
			after.setDestinationFileName(path+"\\"+"PolisAll.pdf");
			after.mergeDocuments();			
			deleteAllFile(new File(path));
	
		return null;
	}
	
	//Fungsi ini digunakan untuk memeriksa nama printer
	public static Boolean checkPrinterName (String printerName) throws Exception{
		   Boolean printerExists = false ;
		   PrintService[] printService = PrinterJob.lookupPrintServices();
        for(int i = 0; !printerExists && i < printService.length; i++)
            {
                if(printService[i].getName().toUpperCase().contains(printerName) && printService[i].isAttributeValueSupported(Sides.DUPLEX, null, null))
                {
                    printerExists = true;
                    printerName = printService[i].getName();
                }else{
             	   printerExists = false;
                }
            }
        return printerExists;
	}
	
	//Fungsi ini digunakan untuk Direct Print
	public static Boolean directPrint(String pdfFile,String printerName,String allowPrint)  {
			Boolean print = false ;
			if (allowPrint.equals("1")){
		        PDDocument document = null;
		        try
		        {
		            document = PDDocument.load( pdfFile );
		            PrinterJob printJob = PrinterJob.getPrinterJob();
		            printJob.setJobName(pdfFile);
		            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
		            printJob.setPrintService(printService);
		            printerName = printService.getName();
		            if(!printerName.equals(null)){
		            	 HashPrintRequestAttributeSet rat = new HashPrintRequestAttributeSet();
		            	 	Media[] supportedMedia = (Media[]) printJob.getPrintService().getSupportedAttributeValues(Media.class, null, null);
				            for (Media m : supportedMedia) {
				                if (m.toString().equals("Tray2")) {
				                	rat.add(m);
				                    break;
				                }
				            }
				            rat.add(Sides.DUPLEX);
				            printJob.print(rat);
				            document.silentPrint(printJob);
						    print = true;
				            
		            }
		        }catch(Exception e){
		        	EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, 0, new Date(), null, false, "ajsjava@sinarmasmsiglife.co.id", new String[]{"natanael@sinarmasmsiglife.co.id"}, null, null,  
							"Error Print.Java", 
							e+"", null, null);
		        }
		        finally
		        {
		            if( document != null )
		            {
		                try {
							document.close();
						} catch (IOException e) {
							EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
									null, 0, 0, new Date(), null, false, "ajsjava@sinarmasmsiglife.co.id", new String[]{"natanael@sinarmasmsiglife.co.id"}, null, null,  
									"Error Closing Dokumen", 
									e+"", null, null);
						}
		            }
		        }
		
			}
		
	        return print;
	    }
	
	/*Fungsi ini digunakan untuk mengping ke komputer tertentu
	return true jika connect
	*/
	public static Boolean testConnection(String ipAddress) throws IOException{
		Boolean connect = false;
		InetAddress inet = InetAddress.getByName(ipAddress);
		connect = inet.isReachable(3000);
		return connect;
		
	}
	
	

}
