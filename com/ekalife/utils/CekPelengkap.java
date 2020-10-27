package com.ekalife.utils;

import id.co.sinarmaslife.std.util.FileUtil;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.Sides;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.springframework.web.bind.ServletRequestUtils;

import bsh.commands.dir;

import com.ekalife.elions.service.ElionsManager;
import com.google.common.io.Files;
import com.lowagie.text.pdf.PdfReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CekPelengkap implements Serializable {

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
	
	//Mark Valentino 20180906
	//Fungsi ini digunakan untuk hapus file Temp dan memisahkan per TRAY
	public static String deleteTempAndMoveFiles(String cabang,String path,String pathTemplate,String lsbs,Integer mspoProvider,String flagLink, Integer punyaSimascard, int flagPrePrinted, HttpServletRequest request) throws Exception{	
		PDFMergerUtility before = new PDFMergerUtility();
		PDFMergerUtility after = new PDFMergerUtility();
		
		before.addSource(path+"\\"+"Surat.pdf");
		before.addSource(path+"\\"+"Polis.pdf");
		before.addSource(path+"\\"+"Manfaat.pdf");	
		
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");		
		
		if (flagLink.equals("1")){
			before.addSource(path+"\\"+"AlokasiDana.pdf");
		}
		if(flagPrePrinted==1){				
			before.addSource(path+"\\"+"TandaTerimaPolis.pdf");
			before.addSource(pathTemplate);
			File userDir = new File(path+"\\"+"SS.pdf");
			if (userDir.exists())before.addSource(path+"\\"+"SS.pdf");
			if(mspoProvider==2){
				File admedika = new File(path+"\\"+"Admedika.pdf");
		        if(admedika.exists()){
					before.addSource(path+"\\"+"Admedika.pdf");
		        }
			}
			before.setDestinationFileName(path+"\\"+"Temp.pdf");
			before.mergeDocuments();
			if(checkCountDocument(path+"\\"+"Temp.pdf")){
				after.addSource(path+"\\"+"Temp.pdf");						
			}else if(!checkCountDocument(path+"\\"+"Temp.pdf")){
				after.addSource(path+"\\"+"Temp.pdf");
				after.addSource(pathTemplate);
			}
		}else if(flagPrePrinted==2){
			before.addSource(path+"\\"+"TandaTerimaPolis.pdf");
			before.addSource(pathTemplate);
			File userDir = new File(path+"\\"+"SS.pdf");
			if (userDir.exists())before.addSource(path+"\\"+"SS.pdf");
			if(mspoProvider==2){
				File admedika = new File(path+"\\"+"Admedika.pdf");
		        if(admedika.exists()){
					before.addSource(path+"\\"+"Admedika.pdf");
		        }
			}
			before.setDestinationFileName(path+"\\"+"Temp.pdf");
			before.mergeDocuments();
			
			if(punyaSimascard==1){
				if(checkCountDocument(path+"\\"+"Temp.pdf")){
					after.addSource(path+"\\"+"Temp.pdf");
					after.addSource(path+"\\"+"SuratSimasCard.pdf");
				}else if(!checkCountDocument(path+"\\"+"Temp.pdf")){
					after.addSource(path+"\\"+"Temp.pdf");
					after.addSource(pathTemplate);
					after.addSource(path+"\\"+"SuratSimasCard.pdf");
				}
			}else{
				if(checkCountDocument(path+"\\"+"Temp.pdf")){
					after.addSource(path+"\\"+"Temp.pdf");						
				}else if(!checkCountDocument(path+"\\"+"Temp.pdf")){
					after.addSource(path+"\\"+"Temp.pdf");
					after.addSource(pathTemplate);
				}	
			}
			
		}else if(flagPrePrinted==3){//untuk Produk BTN 				
			before.addSource(path+"\\"+"TandaTerimaPolis.pdf");
			before.addSource(pathTemplate);
			File userDir = new File(path+"\\"+"SSU_SSK.pdf");
			if (userDir.exists())before.addSource(path+"\\"+"SSU_SSK.pdf");
			File userDir2 = new File(path+"\\"+"SPP.pdf");
			if (userDir2.exists())before.addSource(path+"\\"+"SPP.pdf");				
			before.setDestinationFileName(path+"\\"+"Temp.pdf");
			before.mergeDocuments();
			if(checkCountDocument(path+"\\"+"Temp.pdf")){
				after.addSource(path+"\\"+"Temp.pdf");						
			}else if(!checkCountDocument(path+"\\"+"Temp.pdf")){
				after.addSource(path+"\\"+"Temp.pdf");
				after.addSource(pathTemplate);
			}
		}
		after.setDestinationFileName(path+"\\"+"PolisAll.pdf");
		after.mergeDocuments();
		
		deleteAllFile(new File(path));		
		
		//create dir TRAY 2 & 3
        File trayDir2 = new File(path+"\\TRAY2");
        File trayDir3 = new File(path+"\\TRAY3");		        
        if(!trayDir2.exists()) {
        	trayDir2.mkdirs();
        }		
        if(!trayDir3.exists()) {
        	trayDir3.mkdirs();
        }	
        
        File filePolis = new File(path + "\\Polis.pdf");
        File fileAdmedika = new File(path + "\\Admedika.pdf");
        File fileManfaat = new File(path + "\\Manfaat.pdf");        
        File filePolisAll = new File(path + "\\PolisAll.pdf");        
        File fileSSU_SSK = new File(path + "\\SSU_SSK.pdf");        
        File fileSurat = new File(path + "\\Surat.pdf");
        File fileSuratSimasCard = new File(path + "\\SuratSimasCard.pdf");
        File fileTandaTerimaPolis = new File(path + "\\TandaTerimaPolis.pdf");        
        File fileEspaj = new File(path + "\\espajDMTM2_" + spaj + ".pdf");               
        File fileTemp = new File(path + "\\Temp.pdf");
        
        //Copy ke folder TRAY masing-masing
		FileUtils.copyFileToDirectory(filePolis, trayDir2);
		FileUtils.copyFileToDirectory(fileAdmedika, trayDir2);
		FileUtils.copyFileToDirectory(fileManfaat, trayDir2);
		FileUtils.copyFileToDirectory(filePolisAll, trayDir2);
		FileUtils.copyFileToDirectory(fileSSU_SSK, trayDir2);
		FileUtils.copyFileToDirectory(fileSurat, trayDir2);
		FileUtils.copyFileToDirectory(fileSuratSimasCard, trayDir2);
		FileUtils.copyFileToDirectory(fileTandaTerimaPolis, trayDir2);
		FileUtils.copyFileToDirectory(fileEspaj, trayDir3);
		
		//Delete original files
		filePolis.delete();
		fileAdmedika.delete();
		fileManfaat.delete();
		filePolisAll.delete();
		fileSSU_SSK.delete();
		fileSurat.delete();
		fileSuratSimasCard.delete();
		fileTandaTerimaPolis.delete();
		fileEspaj.delete();
		fileTemp.delete();		
        
	return null;
}

	public static String generate2File(String cabang,String pathTemp,String path,String pathTemplate1, String pathTemplate2,String lsbs,Integer mspoProvider,String flagLink, Integer punyaSimascard, int flagPrePrinted, HttpServletRequest request, Properties props) throws Exception{

		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");		
		
		PDFMergerUtility before1 = new PDFMergerUtility();
		PDFMergerUtility before2 = new PDFMergerUtility();		
		PDFMergerUtility after1 = new PDFMergerUtility();
		//PDFMergerUtility after2 = new PDFMergerUtility();
		
		File destDirTemp = new File(pathTemp);
		File destDir = new File(path);
		
		if(destDir.exists()) {
			
		    File[] files = destDir.listFiles();
		    if (files == null || files.length == 0) {
		        return null;
		    }
		    File fileFubah = null;
		    int jumlahFileFubah = 0;
		    for (int i = 0; i < files.length; i++) {
		       if ((files[i].toString().toUpperCase().contains("FUBAH"))) {
		    	   fileFubah = files[i];
		    	   jumlahFileFubah++;
		       }
		    }
		    if ((fileFubah != null) && (jumlahFileFubah > 1)){
 	    	   for (int j = 0; j < files.length; j++){
	    		   if (files[j].toString().toUpperCase().contains("FUBAH")){
		    		   if (fileFubah.lastModified() < files[j].lastModified()){
		    			   fileFubah = files[j];	   
		    		   }   
	    		   }
	    	   }		    	
		    }
		    if(fileFubah != null){
		    	before2.addSource(fileFubah);		    	
		    }

		    File fileProposal = null;
		    int jumlahFileProposal = 0;		    
		    for (int i = 0; i < files.length; i++) {
		       if ((files[i].toString().toUpperCase().contains("PROPOSAL"))) {
		    	   fileProposal = files[i];
		    	   jumlahFileProposal++;
		       }
		    }
		    if ((fileProposal != null) && (jumlahFileProposal > 1)){
	    	   for (int j = 0; j < files.length; j++){
	    		   if (files[j].toString().toUpperCase().contains("PROPOSAL")){
		    		   if (fileProposal.lastModified() < files[j].lastModified()){
		    			   fileProposal = files[j];	   
		    		   }   
	    		   }
	    	   }		    	
		    }
		    if(fileProposal != null){
		    	before2.addSource(fileProposal);		    	
		    }		    	
		    
		    //20180924 Mark SPAJ menggunakan file hasil generate E-Spaj-Online-Gadget
		    File fileESpajGadget = null;
		    int jumlahFileESpajGadget = 0;		    
		    for (int i = 0; i < files.length; i++) {
		       if ((files[i].toString().toUpperCase().contains("ESPAJONLINEGADGET"))) {
		    	   fileESpajGadget = files[i];
		    	   jumlahFileESpajGadget++;
		       }
		    }
		    if ((fileESpajGadget != null) && (jumlahFileESpajGadget > 1)){
	    	   for (int j = 0; j < files.length; j++){
	    		   if (files[j].toString().toUpperCase().contains("ESPAJONLINEGADGET")){
		    		   if (fileESpajGadget.lastModified() < files[j].lastModified()){
		    			   fileESpajGadget = files[j];	   
		    		   }   
	    		   }
	    	   }
		    }
		    if(fileESpajGadget != null){
		    	before2.addSource(fileESpajGadget);    	
		    }
		    
		    //20180924 Mark Jika file E-SPAJ-Gadget tidak ada maka menggunakan file SPAJ existing
		    if (fileESpajGadget == null){
			    File fileSpaj = null;
			    int jumlahfileSpaj = 0;		    
			    for (int i = 0; i < files.length; i++) {
			       if ((files[i].toString().toUpperCase().contains("SPAJ "))) {
			    	   fileSpaj = files[i];
			    	   jumlahfileSpaj++;
			       }
			    }
			    if ((fileSpaj != null) && (jumlahfileSpaj > 1)){
		    	   for (int j = 0; j < files.length; j++){
		    		   if (files[j].toString().toUpperCase().contains("SPAJ ")){
			    		   if (fileSpaj.lastModified() < files[j].lastModified()){
			    			   fileSpaj = files[j];	   
			    		   }   
		    		   }
		    	   }
			    }
			    if(fileSpaj != null){
			    	before2.addSource(fileSpaj);    	
			    }
		    }			    
		    
		    before2.setDestinationFileName(path+"\\"+"pelengkap.pdf");
			before2.mergeDocuments();
		}
					
		before1.addSource(pathTemp+"\\"+"pathSurat.pdf");
		before1.addSource(pathTemp+"\\"+"pathPolis.pdf");
		before1.addSource(pathTemp+"\\"+"pathManfaat.pdf");				
		
		if (flagLink.equals("1")){
			before1.addSource(pathTemp+"\\"+"pathAlokasiDana.pdf");
		}
		if(flagPrePrinted==1){				
			before1.addSource(pathTemp+"\\"+"pathTandaTerimaPolis.pdf");
			before1.addSource(pathTemplate1);
			File userDir = new File(pathTemp+"\\"+"pathSS.pdf");
			if (userDir.exists())before1.addSource(pathTemp+"\\"+"pathSS.pdf");
			if(mspoProvider==2){
				File admedika = new File(pathTemp+"\\"+"pathAdmedika.pdf");
		        if(admedika.exists()){
					before1.addSource(pathTemp+"\\"+"pathAdmedika.pdf");
					before1.addSource(props.getProperty("pdf.template.admedika")+"\\"+"daftar rumah sakit.pdf");					
		        }
			}
			before1.setDestinationFileName(pathTemp+"\\"+"pathTemp.pdf");
			before1.mergeDocuments();
			if(checkCountDocument(pathTemp+"\\"+"pathTemp.pdf")){
				after1.addSource(pathTemp+"\\"+"pathTemp.pdf");						
			}else if(!checkCountDocument(pathTemp+"\\"+"pathTemp.pdf")){
				after1.addSource(pathTemp+"\\"+"pathTemp.pdf");
				after1.addSource(pathTemplate1);
			}
		}else if(flagPrePrinted==2){
			before1.addSource(pathTemp+"\\"+"pathTandaTerimaPolis.pdf");
			before1.addSource(pathTemplate1);		
			File userDir = new File(pathTemp+"\\"+"pathSS.pdf");
			if (userDir.exists())before1.addSource(pathTemp+"\\"+"pathSS.pdf");
			if(mspoProvider==2){
				File admedika = new File(pathTemp+"\\"+"pathAdmedika.pdf");
		        if(admedika.exists()){
					before1.addSource(pathTemp+"\\"+"pathAdmedika.pdf");
					before1.addSource(props.getProperty("pdf.template.admedika")+"\\"+"daftar rumah sakit.pdf");
		        }
			}
			before1.setDestinationFileName(pathTemp+"\\"+"pathTemp.pdf");
			before1.mergeDocuments();				
			
			if(punyaSimascard==1){
				if(checkCountDocument(pathTemp+"\\"+"pathTemp.pdf")){
					after1.addSource(pathTemp+"\\"+"pathTemp.pdf");
					after1.addSource(pathTemp+"\\"+"pathSuratSimasCard.pdf");
				}else if(!checkCountDocument(pathTemp+"\\"+"pathTemp.pdf")){
					after1.addSource(pathTemp+"\\"+"pathTemp.pdf");
					after1.addSource(pathTemplate1);
					after1.addSource(pathTemp+"\\"+"pathSuratSimasCard.pdf");
				}
			}else{
				if(checkCountDocument(pathTemp+"\\"+"pathTemp.pdf")){
					after1.addSource(pathTemp+"\\"+"pathTemp.pdf");						
				}else if(!checkCountDocument(pathTemp+"\\"+"pathTemp.pdf")){
					after1.addSource(pathTemp+"\\"+"pathTemp.pdf");
					after1.addSource(pathTemplate1);
				}	
			}
			
		}else if(flagPrePrinted==3){//untuk Produk BTN 				
			before1.addSource(pathTemp+"\\"+"pathTandaTerimaPolis.pdf");
			before1.addSource(pathTemplate1);
			File userDir = new File(pathTemp+"\\"+"pathSSU_SSK.pdf");
			if (userDir.exists())before1.addSource(pathTemp+"\\"+"pathSSU_SSK.pdf");
			File userDir2 = new File(pathTemp+"\\"+"pathSPP.pdf");
			if (userDir2.exists())before1.addSource(pathTemp+"\\"+"pathSPP.pdf");				
			before1.setDestinationFileName(pathTemp+"\\"+"pathTemp1.pdf");
			before1.mergeDocuments();
			if(checkCountDocument(pathTemp+"\\"+"pathTemp1.pdf")){
				after1.addSource(pathTemp+"\\"+"pathTemp1.pdf");						
			}else if(!checkCountDocument(pathTemp+"\\"+"pathTemp.pdf")){
				after1.addSource(pathTemp+"\\"+"pathTemp1.pdf");
				after1.addSource(pathTemplate1);
			}
		}
		//after1.setDestinationFileName(path+"\\"+"PolisAll.pdf");
		after1.setDestinationFileName(pathTemp+"\\"+"polis_all.pdf");
		after1.mergeDocuments();	
		//deleteAllFile(new File(pathTemp));
		
		File dirTemp = new File(pathTemp);
		
		FileUtil.copyfile(pathTemp + "\\polis_all.pdf", path + "\\polis_all.pdf");
		deleteDirectoryWithFiles(dirTemp);

	return null;
	
}	
	
	public static String generateFileRDS(String cabang,String pathTemp,String path,String pathTemplate1, String pathTemplate2,String lsbs,Integer mspoProvider,String flagLink, Integer punyaSimascard, int flagPrePrinted, HttpServletRequest request, Properties props, ElionsManager elionsManager) throws Exception{

		String spaj;
		if(request == null){
			spaj = path.substring(path.lastIndexOf("\\")+1);
		}else{
			spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");	
		}	
		
		PDFMergerUtility before1 = new PDFMergerUtility();	
		PDFMergerUtility after1 = new PDFMergerUtility();
		
		File fileDirTemp = new File(pathTemp);
		File fileDir = new File(path);
		String pathDirTemp = pathTemp;
		String pathDirDest = path;
		boolean isDmtm = (StringUtils.substring(spaj, 0, 2).equals("40"));	
		
		if(fileDir.exists() && (!isDmtm)) {
			
		    File[] files = fileDir.listFiles();
		    if (files == null || files.length == 0) {
		        return null;
		    }
		    File fileFubah = null;
		    int jumlahFileFubah = 0;
		    for (int i = 0; i < files.length; i++) {
		       if ((files[i].toString().toUpperCase().contains("FUBAH"))) {
		    	   fileFubah = files[i];
		    	   jumlahFileFubah++;
		       }
		    }
		    if ((fileFubah != null) && (jumlahFileFubah > 1)){
 	    	   for (int j = 0; j < files.length; j++){
	    		   if (files[j].toString().toUpperCase().contains("FUBAH")){
		    		   if (fileFubah.lastModified() < files[j].lastModified()){
		    			   fileFubah = files[j];	   
		    		   }   
	    		   }
	    	   }
		    }
		    if(fileFubah != null){
				//FileUtil.copyfile(fileFubah.toString(), pathDirTemp + "\\" + spaj + "FUBAH.pdf");
				FileUtil.copyfile(fileFubah.toString(), pathDirTemp + "\\" + fileFubah.getName());				
		    }

		    File fileProposal = null;
		    int jumlahFileProposal = 0;		    
		    for (int i = 0; i < files.length; i++) {
		       if ((files[i].toString().toUpperCase().contains("PROPOSAL"))) {
		    	   fileProposal = files[i];
		    	   jumlahFileProposal++;
		       }
		    }
		    if ((fileProposal != null) && (jumlahFileProposal > 1)){
	    	   for (int j = 0; j < files.length; j++){
	    		   if (files[j].toString().toUpperCase().contains("PROPOSAL")){
		    		   if (fileProposal.lastModified() < files[j].lastModified()){
		    			   fileProposal = files[j];	   
		    		   }   
	    		   }
	    	   }		    	
		    }
		    if(fileProposal != null){
				//FileUtil.copyfile(fileProposal.toString(), pathDirTemp + "\\" + spaj + "PROPOSAL.pdf");    	
				FileUtil.copyfile(fileProposal.toString(), pathDirTemp + "\\" + fileProposal.getName());				
		    }	
		}
					
		if(flagPrePrinted==1){
			if(mspoProvider==2){
				File admedika = new File(pathTemp+"\\"+"pathAdmedika.pdf");
		        if(admedika.exists()){
					FileUtil.copyfile(admedika.toString(), fileDirTemp + "\\" + spaj + "\\" + "10. surat_penjaminan_provider.pdf");		        	
					//FileUtil.copyfile(props.getProperty("pdf.template.admedika")+"\\"+"daftar rumah sakit.pdf", fileDirTemp + "\\" + spaj + "\\" + "14. Daftar Rumah Sakit.pdf");
		        }
			}
		}else if(flagPrePrinted==2){	
//			File userDir = new File(pathTemp+"\\"+"pathSS.pdf");

			if(mspoProvider==2){
				File admedika = new File(pathTemp+"\\"+"pathAdmedika.pdf");
		        if(admedika.exists()){
					FileUtil.copyfile(admedika.toString(), fileDirTemp + "\\" + spaj + "\\" + "10. surat_penjaminan_provider.pdf");		        	
					//FileUtil.copyfile(props.getProperty("pdf.template.admedika")+"\\"+"daftar rumah sakit.pdf", fileDirTemp + "\\" + spaj + "\\" + "14. Daftar Rumah Sakit.pdf");
		        }
			}
			
		}
		
	    //Mark Valentino 20180928 copy files dari Local Server ke EBSERVER
//	    File[] files = fileDirTemp.listFiles();
//	    for (int i = 0; i < files.length; i++) {
//	    	if ((!files[i].isDirectory()) && (!files[i].toString().toUpperCase().contains("PROPOSAL")) && (!files[i].toString().toUpperCase().contains("FUBAH"))){
//	    		String newName = files[i].getName();
//		    	FileUtil.copyfile(files[i].toString(), pathDirDest + "\\" + newName);
//	    	}
//		}

	    renameFilesRDS(pathTemp, path,request, props);
	    
	    String pathUploadZip = props.getProperty("pdf.dir.ftp")+"\\"+cabang+"\\"+spaj;
	    File dirUploadZip = new File(pathUploadZip);
        if(!dirUploadZip.exists()) {
        	dirUploadZip.mkdirs();
        }
        
        //20190201 Mark Valentino - Copy all generated files to EBSERVER without overwrite - Aktifkan untuk production
	    File[] files = fileDirTemp.listFiles();
	    for (int i = 0; i < files.length; i++) {
		       if ((files[i].toString().toUpperCase().contains(".PDF"))) {
		    	   File targetFile = new File(pathDirDest + "\\" + files[i].getName());
		    	    if((fileDir.exists())){
		    	    	if((!targetFile.exists())){
		    	    		FileUtil.copyfile(pathDirTemp + "\\" + files[i].getName(), pathDirDest + "\\" + files[i].getName());
		    	    	}
//		    	    	//Revisi alokasi dana - Non aktifkan jika tidak digunakan
//		    	    	if((files[i].toString().toUpperCase().contains("PATHALOKASIDANA"))){
//		    	    		FileUtil.copyfile(pathDirTemp + "\\" + files[i].getName(), pathDirDest + "\\" + "pathAlokasiDana_rev.pdf");
//		    	    	}		    	    	
		    	    }else{
		    	    	fileDir.mkdirs();
		    	    	FileUtil.copyfile(pathDirTemp + "\\" + files[i].getName(), pathDirDest + "\\" + files[i].getName());			    	       	
		    	    }
		       }
		}
        
//	    String pathTemp2 = props.getProperty("pdf.dir.export.temp");	    
//		zipFolder(pathTemp + "\\" + spaj, pathTemp2 + "\\" + spaj + ".zip");	    
		zipFolder(pathTemp + "\\" + spaj, pathUploadZip + "\\" + spaj + ".zip");	    
		File dirTemp = new File(pathTemp);
		if(dirTemp.exists()){
			deleteDirectoryWithFiles(dirTemp);			
		}

	return null;		
}	

	public static String renameFilesRDS(String pathTemp, String path, HttpServletRequest request, Properties props) throws Exception{
		
		String spaj;
		if(request == null){
			spaj = path.substring(path.lastIndexOf("\\")+1);
		}else{
			spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");	
		}		
		
		File fileDirTemp = new File(pathTemp);
		File fileDir = new File(path);
		File fileDirRar = new File(pathTemp + "\\" + spaj);
		String pathDirTemp = pathTemp;
		String pathDirDest = path;
		
		boolean isDmtm = (StringUtils.substring(spaj, 0, 2).equals("40"));	
		
        if(!fileDirRar.exists()) {
        	fileDirRar.mkdirs();
        }				
		
		if(fileDirTemp.exists()) {
			
		    File[] files = fileDirTemp.listFiles();
		    File[] filesEbserver = fileDir.listFiles();		    
		    if (files == null || files.length == 0) {
		        return null;
		    }
		    
		    if ((!isDmtm) && (fileDir.exists())){
			    File fileFubah = null;
			    for (int i = 0; i < files.length; i++) {
			       if ((files[i].toString().toUpperCase().contains("FUBAH"))) {
			    	   fileFubah = files[i];
			    	   if ((fileFubah!=null) && (!isDmtm)) if(fileFubah.exists()){
				    	   FileUtil.copyfile(fileFubah.toString(), pathDirTemp + "\\" + spaj + "\\" + "12. " + spaj + "FUBAH.pdf");		    		   
			    	   }
			       }
			    }

			    File fileSpaj = null;
			    for (int i = 0; i < files.length; i++) {
			       if ((files[i].toString().toUpperCase().contains("ESPAJONLINEGADGET")) || (files[i].toString().toUpperCase().contains("ESPAJDMTM"))) {
			    	   fileSpaj = files[i];
			       }
			    }
			    if (fileSpaj == null){
				    int jumlahfileSpaj = 0;				    	
				    for (int i = 0; i < filesEbserver.length; i++) {
					       if ((filesEbserver[i].toString().toUpperCase().contains("SPAJ"))) {
					    	   fileSpaj = filesEbserver[i];
					    	   jumlahfileSpaj++;
					       }
				    }
				    if ((fileSpaj != null) && (jumlahfileSpaj > 1)){
				    	   for (int j = 0; j < filesEbserver.length; j++){
				    		   if (filesEbserver[j].toString().toUpperCase().contains("SPAJ ")){
					    		   if (fileSpaj.lastModified() < filesEbserver[j].lastModified()){
					    			   fileSpaj = filesEbserver[j];	   
					    		   }   
				    		   }
				    	   }
				    }			    
			    }	    
			    if ((fileSpaj!=null) && (!isDmtm)) if(fileSpaj.exists()){
		    	   FileUtil.copyfile(fileSpaj.toString(), pathDirTemp + "\\" + spaj + "\\" + "11. " + fileSpaj.getName());		    
			    }

			    File fileProposal = null;
			    for (int i = 0; i < files.length; i++) {
			       if ((files[i].toString().toUpperCase().contains("PROPOSAL"))) {
			    	   fileProposal = files[i];
			    	   if ((fileProposal!=null) && (!isDmtm)) if(fileProposal.exists()){
				    	   FileUtil.copyfile(fileProposal.toString(), pathDirTemp + "\\" + spaj + "\\" + "13. " + fileProposal.getName());		    		   
			    	   }
			       }
			    }
			    
			    File fileAlokasiDana = null;
			    for (int i = 0; i < files.length; i++) {
			       if ((files[i].toString().toUpperCase().contains("PATHALOKASIDANA.PDF"))) {
			    	   fileAlokasiDana = files[i];
			    	   if (fileAlokasiDana!=null) if(fileAlokasiDana.exists()){
				    	   FileUtil.copyfile(fileAlokasiDana.toString(), pathDirTemp + "\\" + spaj + "\\" + "5. alokasi_dana.pdf");
			    	   }
			       }
			    }			    
		    }
		    
		    File fileTandaTerima = null;
		    for (int i = 0; i < files.length; i++) {
		       if ((files[i].toString().toUpperCase().contains("TANDATERIMA"))) {
		    	   fileTandaTerima = files[i];
		    	   if (fileTandaTerima!=null) if(fileTandaTerima.exists()){
			    	   FileUtil.copyfile(fileTandaTerima.toString(), pathDirTemp + "\\" + spaj + "\\" + "1. tanda_terima.pdf");		    		   
		    	   }
		       }
		    }		    
		    
		    File fileSurat = null;
		    for (int i = 0; i < files.length; i++) {
		       if ((files[i].toString().toUpperCase().contains("PATHSURAT.PDF"))) {
		    	   fileSurat = files[i];
		    	   if (fileSurat!=null) if(fileSurat.exists()){
			    	   FileUtil.copyfile(fileSurat.toString(), pathDirTemp + "\\" + spaj + "\\" + "2. surat_polis.pdf");
		    	   }
		       }
		    }		    
		    
		    File filePolis = null;
		    for (int i = 0; i < files.length; i++) {
		       if ((files[i].toString().toUpperCase().contains("PATHPOLIS.PDF"))) {
		    	   filePolis = files[i];
		    	   if (filePolis!=null) if(filePolis.exists()){
			    	   FileUtil.copyfile(filePolis.toString(), pathDirTemp + "\\" + spaj + "\\" + "3. polis_utama.pdf");
			    	   FileUtil.copyfile(props.getProperty("pdf.template")+"\\"+"General penerapan aplikasi QR Code.pdf", fileDirTemp + "\\" + spaj + "\\" + "15. General penerapan aplikasi QR Code.pdf");
			    	   //
		    	   }
		       }		    	   
		    }		    
		    
		    File fileManfaat = null;
		    for (int i = 0; i < files.length; i++) {
		       if ((files[i].toString().toUpperCase().contains("PATHMANFAAT.PDF"))) {
		    	   fileManfaat = files[i];
		    	   if (fileManfaat!=null) if(fileManfaat.exists()){
		    		   long size = fileManfaat. length();
		    		   double ukuranFile = (size / 1024);
		    		   if (ukuranFile > 2){ // 1 KB
				    	   FileUtil.copyfile(fileManfaat.toString(), pathDirTemp + "\\" + spaj + "\\" + "4. manfaat.pdf");		    			   
		    		   }
		    	   }
		       }
		    }		    
		    
//		    File fileSsu = null;
//		    for (int i = 0; i < files.length; i++) {
//		       if ((files[i].toString().toUpperCase().contains("SSU.PDF"))) {
//		    	   fileSsu = files[i];
//		    	   if (fileSsu!=null) if(fileSsu.exists()){
//			    	   FileUtil.copyfile(fileSsu.toString(), pathDirTemp + "\\" + spaj + "\\" + "6. ssu.pdf");
//		    	   }
//		       }
//		    }		    
//
//		    File fileSsk = null;
//		    for (int i = 0; i < files.length; i++) {
//		       if ((files[i].toString().toUpperCase().contains("SSU.PDF"))) {
//		    	   fileSsk = files[i];
//		    	   if (fileSsk!=null) if(fileSsk.exists()){
//			    	   FileUtil.copyfile(fileSsk.toString(), pathDirTemp + "\\" + spaj + "\\" + "7. ssk.pdf");
//		    	   }
//		       }
//		    }
		    
		    File fileSuratSimascard = null;
		    for (int i = 0; i < files.length; i++) {
		       if ((files[i].toString().toUpperCase().contains("PATHSURATSIMASCARD.PDF"))) {
		    	   fileSuratSimascard = files[i];
		    	   if (fileSuratSimascard!=null) if(fileSuratSimascard.exists()){
		    		   long size = fileSuratSimascard. length();
		    		   double ukuranFile = (size / 1024);
		    		   if (ukuranFile > 1){ // 1 KB
				    	   FileUtil.copyfile(fileSuratSimascard.toString(), pathDirTemp + "\\" + spaj + "\\" + "8. surat_simcard.pdf");		    			   
		    		   }
		    	   }
		       }
		    }
		    
		}
		
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
	
	//Mark Valentino 20180907 Zip Files
	public static String zipFilesOld(String path, HttpServletRequest request) throws Exception{	

		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		
        String sourceFile = spaj;
        FileOutputStream fos = new FileOutputStream(spaj+".zip");
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        File fileToZip = new File(path);
 
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(spaj);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
        zipOut.close();
        fos.close();		
		
	return null;	
	}
	
	//Mark Valentino 20180907 Zip Folder Temp
	static public void zipFolder(String srcFolder, String destZipFile) throws Exception {
	    ZipOutputStream zip = null;
	    FileOutputStream fileWriter = null;

	    fileWriter = new FileOutputStream(destZipFile);
	    zip = new ZipOutputStream(fileWriter);

	    addFolderToZip("", srcFolder, zip);
	    zip.flush();
	    zip.close();
	}
	
	static private void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {
	
		    File folder = new File(srcFile);
		    if (folder.isDirectory()) {
		      addFolderToZip(path, srcFile, zip);
		    }else {
		      byte[] buf = new byte[1024];
		      int len;
		      FileInputStream in = new FileInputStream(srcFile);
		      zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
		      while ((len = in.read(buf)) > 0) {
		        zip.write(buf, 0, len);
		      }
		      in.close();
		    }
	}
	  
	static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		    File folder = new File(srcFolder);

		    for (String fileName : folder.list()) {
		      if (path.equals("") && fileName.toUpperCase().contains(".PDF")) {
		        addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
		      }else if (fileName.toUpperCase().contains(".PDF")){
		        addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
		      }
		    }
		    zip.flush();
		    zip.close();
	}
	
	//Mark Valentino 20180909
	public static List cekKelengkapan(String path, HttpServletRequest request, List errors) throws Exception{

		boolean lengkap = false;
		
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		File destDir = new File(path);	
		int hitLengkap = 0;
		errors.clear();
		
		if(destDir.exists()) {
			String[] children = destDir.list();
			if(children !=null ){
//				for(int i=0; i<children.length; i++) {
//					if(children[i].contains("FUBAH")) {
//						lengkap = true;
//					}
//				}
//				if (lengkap == false) errors.add("File FUBAH belum ada.");
//				lengkap = false;
//				for(int i=0; i<children.length; i++) {
//					if(children[i].toUpperCase().contains("SPAJ")) {
//						lengkap = true;
//					}
//				}
//				if (lengkap == false) errors.add("File SPAJ belum ada.");				
				lengkap = false;				
				for(int i=0; i<children.length; i++) {
					if(children[i].toUpperCase().contains("PROPOSAL")) {
						lengkap = true;
					}
				}
				if (lengkap == false) errors.add("File PROPOSAL belum ada.");						
//				if (hitLengkap == 3){
//					return errors;
//				}else return errors;
			}	
		}	
	return errors;
	}

	public static String deleteDirectoryWithFiles(File file) throws IOException{
		if (file.isDirectory()){
			File[] entries = file.listFiles();
			if (entries != null){
				for (File entry : entries){
					deleteDirectoryWithFiles(entry);
				}
			}
		}
		if (!file.delete()){
			throw new IOException("Gagal menghapus direktori " + file);
		}
	return null;	
	}
	
	public static String deleteRandomAccessFiles(File file) throws IOException{
		if (file.isDirectory()){
			File[] entries = file.listFiles();
			if (entries != null){
			    for (int i = 0; i < entries.length; i++) {
			    	RandomAccessFile raf = new RandomAccessFile(entries[i], "rw");
			    	raf.close();
			    	entries[i].delete();
			    }	
			}
		}
		if (!file.delete()){
			throw new IOException("Gagal menghapus direktori " + file);
		}
	return null;	
	}
	
}	