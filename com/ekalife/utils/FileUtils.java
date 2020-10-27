package com.ekalife.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.ekalife.elions.model.FileItem;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;
/**
 * @author Yusuf
 * @since 01/12/2006
 * Fungsi2 berhubungan dengan file, seperti download..
 */
public class FileUtils {
	protected static Properties props;
	protected static final Log logger = LogFactory.getLog( FileUtils.class );
	
    public static void copy(File src, File dst) throws IOException {
    	InputStream in = null;
    	OutputStream out = null;
    	try {
	        in = new FileInputStream(src);
	        out = new FileOutputStream(dst);
	    
	        // Transfer bytes from in to out
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
    	}finally {
    		try {
    			if(in != null) {
    				in.close();
    			}
    			if(out != null) {
    				out.close();
    			}
    		}catch (Exception e) {
				logger.error("ERROR :", e);
			}
    	}
        
    }

	//baca isi dari file dan return dalam bentuk string
	public static String readFileAsString(String filePath) throws java.io.IOException{
		StringBuilder fileData = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
			        new FileReader(filePath));
			char[] buf = new char[1024];
			int numRead=0;
			while((numRead=reader.read(buf)) != -1){
			    fileData.append(buf, 0, numRead);
			}
		}finally {
			try {
				if(reader != null) {
					reader.close();
				}
			}catch (Exception e) {
				logger.error("ERROR :", e);
			}
		}
		
		
		return fileData.toString();
	}		
	
	//delete file oleh user dari server
	public static boolean deleteFile(String destDir, String fileName) 
			throws FileNotFoundException, IOException{
		File file = new File(destDir+"/"+fileName);
		File file2=new File(destDir+"//"+fileName);
		if(file.exists()) return file.delete();
		if(file2.exists()) return file2.delete();
		return false;
	}	
	
	//download file oleh user dari server
	public static void downloadFile(String attachOrInline, String destDir, String fileName, HttpServletResponse response) 
			throws FileNotFoundException, IOException{
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(destDir+"/"+fileName);
			if (in != null) {
				out = new BufferedOutputStream(response.getOutputStream());
				in = new BufferedInputStream(in);
				//String contentType = "application/unknown";
//				response.setHeader("Content-Disposition", attachOrInline+"; filename=\"" + fileName + "\"");
				if(fileName.toLowerCase().indexOf(".pdf")>-1) {
					response.setContentType("application/pdf");
				}else if(fileName.toLowerCase().indexOf(".xls")>-1) {
					response.setContentType("application/vnd.ms-excel");
				}
			
				response.setHeader("Content-Disposition", attachOrInline+"; filename=" + fileName);
				int c;
				while ((c = in.read()) != -1) out.write(c);
				
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
			}
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}finally {
			if (in != null) in.close();  
			if (out != null) out.close();
		}				
	}
	
	//listing file di dalam suatu directory
	public static List<DropDown> listFilesInDirectory(String dir) {
		File destDir = new File(dir);
		List<DropDown> daftar = new ArrayList<DropDown>();
		if(destDir.exists()) {
			String[] children = destDir.list();
			daftar = new ArrayList<DropDown>();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			if(children !=null ){
				for(int i=0; i<children.length; i++) {
					File file = new File(destDir+"/"+children[i]);
					if(!children[i].contains("REINS_"))
						daftar.add(new DropDown(children[i], df.format(new Date(file.lastModified())), dir));
				}
			}
		}
		return daftar;
	}
	
	public static List<DropDown> listFilesInDirectorySSU(String dir) {
		File destDir = new File(dir);
		List<DropDown> daftar = new ArrayList<DropDown>();
		if(destDir.exists()) {
			String[] children = destDir.list();
			daftar = new ArrayList<DropDown>();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			for(int i=0; i<children.length; i++) {
				File file = new File(destDir+"/"+children[i]);
				if(children[i].contains(".pdf") || children[i].contains(".PDF"))
					daftar.add(new DropDown(children[i], df.format(new Date(file.lastModified())), dir));
			}
		}
		return daftar;
	}

	//listing directory dalam suatu directory
	public static List<DropDown> listDirectories(String dir) {
		File destDir = new File(dir);
		List<DropDown> daftar = new ArrayList<DropDown>();
		if(destDir.exists()) {
			File[] children = destDir.listFiles();
			for(File tmp : children){
				if(tmp.isDirectory()){
					daftar.add(new DropDown(tmp.getAbsolutePath(), tmp.getName()));
				}
			}
		}
		return daftar;
	}
	
	//listing file di dalam suatu directory, berdasarkan nama file
	public static List<DropDown> listFilesInDirectoryStartsWith(String dir, String startsWith) {
		File destDir = new File(dir);
		List<DropDown> daftar = new ArrayList<DropDown>();
		if(destDir.exists()) {
			String[] children = destDir.list();
			daftar = new ArrayList<DropDown>();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			for(int i=0; i<children.length; i++) {
				if(children[i].startsWith(startsWith)) {
					File file = new File(destDir+"/"+children[i]);
					daftar.add(new DropDown(children[i], df.format(new Date(file.lastModified())), dir));
				}
			}
		}
		return daftar;
	}
	
	//listing file dalam directory berdasarkan startdate sampai enddate
	public static List<DropDown> listFilesInDirectoryHistory(String dir,String hsStartDate,String hsEndDate) throws ParseException{
		File destDir = new File(dir);
		List<DropDown> daftar = new ArrayList<DropDown>();
		int flag = 0;
		if(destDir.exists()) {
			SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			DateFormat df2 = new SimpleDateFormat("yyyyMMdd");
			String[] children = destDir.list();
			daftar = new ArrayList<DropDown>();
			Date HsStartDate = df1.parse(hsStartDate);
			Date HsEndDate = df1.parse(hsEndDate);
			
			long a = FormatDate.dateDifference(HsStartDate, HsEndDate, true);
			
			for(int i=0; i<=a; i++) {
				//logger.info(FormatDate.toIndonesian(HsStartDate));
				String tmp = df2.format(FormatDate.add(HsStartDate, Calendar.DATE, i));

				for(int b=0; b<children.length;b++){
					if(children[b].contains(tmp)){
						File file = new File(destDir+"/"+children[b]);
						daftar.add(new DropDown(children[b], df.format(new Date(file.lastModified())), dir));
					}
				}
			}
		}
		return daftar;
	}
	
	//listing file di dalam suatu directory
	public static List<File> listFilesInDirectory2(String dir) {
		File destDir = new File(dir);
		List<File> daftar = new ArrayList<File>();
		if(destDir.exists()) {
			String[] children = destDir.list();
			daftar = new ArrayList<File>();
			for(int i=0; i<children.length; i++) {
				daftar.add(new File(destDir+"/"+children[i]));
			}
		}
		return daftar;
	}
	
	//listing file di dalam suatu directory
	public static List<FileItem> listFilesInDirectoryRecursive(File destDir) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	    List<FileItem> result = new ArrayList<FileItem>();

	    File[] filesAndDirs = destDir.listFiles();
	    if(filesAndDirs !=null) {
	    	List filesDirs = Arrays.asList(filesAndDirs);
	    	Iterator filesIter = filesDirs.iterator();
	    	File file = null;

	    	while ( filesIter.hasNext() ) {
	    		file = (File) filesIter.next();

	    		if (!file.isFile()) {
	    			//recursive call!
	    			List deeperList = listFilesInDirectoryRecursive(file);
	    			result.addAll(deeperList);
	    		}else{
	    			String desc = StringUtils.replace(destDir.getAbsolutePath(), "\\\\ebserver\\download\\", "");
	    			desc = StringUtils.replace(desc, "download_agency\\", "");
	    			desc = StringUtils.replace(desc, "download_akm\\", "");

	    			String donlot = StringUtils.replace(destDir.getAbsolutePath(), "\\", "\\\\");
	    			result.add(new FileItem(file.getName(), df.format(new Date(file.lastModified())), donlot, desc));
	    		}

	    	}
	    }
	    return result;		
	}
	
	//listing file di dalam suatu directory, khusus admin cabang.
	public static List<DropDown> listFilesInDirectoryForAdmin(String dir, String reg_spaj) {
		File destDir = new File(dir);
		List<DropDown> daftar = new ArrayList<DropDown>();
		if(destDir.exists()) {
			String[] children = destDir.list();
			daftar = new ArrayList<DropDown>();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			List kategori = new ArrayList();
			//Deddy -10-10-2012 Helpdesk No 27816, hanya boleh akses untuk doc SPAJ, Form perubahan SPAJ, dan Proposal
			String jenis0 = reg_spaj+"SPAJ";
			String jenis1 = reg_spaj+"PROPOSAL";
			String jenis2 = reg_spaj+"FUBAH";
			//Deddy -08-04-2013 Helpdesk No 32848 dan 33333, admin cabang bisa akses ke Perpanjangan Polis/Renewal dan Pemulihan Polis/Reinstate
			String jenis3 = reg_spaj+"PREN";
			String jenis4 = reg_spaj+"REINS";
			for(int i=0; i<children.length; i++) {
				if(children[i].startsWith(jenis0) || children[i].startsWith(jenis1) || children[i].startsWith(jenis2) || children[i].startsWith(jenis3) || children[i].startsWith(jenis4)) {
					File file = new File(destDir+"/"+children[i]);
					daftar.add(new DropDown(children[i], df.format(new Date(file.lastModified())), dir));
				}
			}
		}
		return daftar;
	}
	
	  private static String validateDirectory(File aDirectory) {
		if (aDirectory == null) {
			return ("Directory should not be null.");
		}
		if (!aDirectory.exists()) {
			return ("Directory does not exist: " + aDirectory);
		}
		if (!aDirectory.isDirectory()) {
			return ("Is not a directory: " + aDirectory);
		}
		if (!aDirectory.canRead()) {
			return ("Directory cannot be read: " + aDirectory);
		}
		return "";
	}
	  
	  public long GetFileSizeInBytes(String filename) { 
		  	
		    File file = new File(filename);
		    
		    if (!file.exists() || !file.isFile()) {
		      logger.info("File doesn\'t exist");
		      return -1;
		    }
		    
		    //Here we get the actual size
		    return file.length();
	  }

	  /**
	   * @author Deddy
	   * @since 01/31/2013
	   * Fungsi untuk melakukan proses Print Out ke Printer secara otomatis dari webpage langsung.
	   * 
	   * @parameter directoryFile 	: untuk menentukan pathFile directory.
	   * @parameter fileName 		: untuk nama file yang akan diprint otomatis (termasuk type file tersebut misalkan abc.pdf).
	   * @parameter url 			: untuk mengambil file secara link.
	   * @parameter typeSilent 		: untuk mengetahui jenis silentPrint yang akan dijalankan. 1 apabila ambil file langsung dari pathFile server. 2 apabila ambil file secara URL/link.
	   */
	public static void silentPrint(String directoryFile, String fileName, String url, Integer typeSilent, HttpServletResponse response){
//		response.setContentType("application/pdf");  
		try{
			PdfReader reader=null;
			if(typeSilent==1){
				reader = new PdfReader(directoryFile+"\\"+fileName);
			}else if(typeSilent==2){
				URL readURL = new URL(url); 
				reader = new PdfReader(readURL);
			}
			PdfStamper stamper = new PdfStamper(reader,response.getOutputStream());  
            PdfWriter writer = stamper.getWriter();  
            
            StringBuffer javascript = new StringBuffer();  
            javascript.append("var params = this.getPrintParams();");  
   
            javascript.append("params.interactive = params.constants.interactionLevel.silent;");  
            javascript.append("params.pageHandling = params.constants.handling.shrink;");  
   
            javascript.append("this.print(params);");  
              
            PdfAction pdfAction= PdfAction.javaScript(javascript.toString(), writer);  
            writer.addJavaScript(pdfAction);  
              
            stamper.close();  
		}catch (DocumentException de) {  
            logger.error(de); 
//            System.err.println("document: " + de.getMessage());  
        } catch (Exception e) {  
            logger.error("ERROR :", e);  
        } 
		
		 
	}
	
	  /**
	   * @author Lufi
	   * @since 18/06/2013
	   * Fungsi untuk melakukan proses list file di dalam suatu directory melalui FTP
	   * 
	   * @parameter dir			 	: untuk menentukan pathFile directory.
	   * @parameter username 		: untuk username ftp
	   * @parameter pass 			: password ftp
	   * @parameter host	 		: alamat  server FTP
	   */
	public static List<DropDown> listFilesInDirectorywithFtp(String dir,String username,String pass,String host, int port)  {
		List <DropDown> daftar = new ArrayList<DropDown>();
		DateFormat df = new SimpleDateFormat("yyyyMMDD");
		
		//FTPClient client = new FTPHTTPClient("ekaproxy",8080);
		FTPClient client = new FTPClient();
		
		try {
			client.connect(host, port);
			client.enterLocalPassiveMode();
		    client.login(username, pass); 
		    FTPFile[] files = client.listFiles(dir);
	        for (FTPFile file : files) {
	        	if(file.isFile()){
	        		logger.info(file.getName()); 
	        		
	        		daftar.add(new DropDown(file.getName(),  df.format(new Date(file.getTimestamp().getTime().getTime()))));       		
	        	}
	        }     
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		}finally{
			try {
				if (client.isConnected()) {
					client.logout();  
					client.disconnect();
			     }            
			} catch (IOException e) { 
				logger.error("ERROR :", e);        
			}
		}
       
      
		return daftar;
	}
	
	/**
	   * @author Lufi
	   * @since 18/06/2013
	   * Fungsi untuk melakukan proses download dan delete file melalui FTP
	   * 
	   * @parameter destDir			: untuk menentukan pathFile yg akan di download.
	   * @parameter username 		: untuk pathfile menyimpan hasil file download
	   * @parameter Filename 		: untuk nama file
	   * @parameter host	 		: alamat  server FTP
	   */
	public static boolean downloadFilewithFtp(String destDir,String dir, String fileName,String username,String pass,String host, int port, HttpServletResponse response){	
		//FTPClient client = new FTPHTTPClient("ekaproxy",8080);
		FTPClient client = new FTPClient();
		try {
			client.connect(host, port);
			client.enterLocalPassiveMode();
	        client.login(username, pass); 
	        client.setFileType(FTP.BINARY_FILE_TYPE);
	        File dirc = new File(dir);
	        if(!dirc.exists()){
	        	dirc.mkdir();
	        }
	        String remoteFile1 = destDir+"/"+fileName;           
	        File downloadFile1 = new File(dir+"/"+fileName);     
	        downloadFile1.getParentFile().mkdirs();
	        if(!downloadFile1.exists()) downloadFile1.createNewFile();	       
	        OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));            
	        boolean success = client.retrieveFile(remoteFile1, outputStream1);
	        outputStream1.close();            
	        if (success) {               
	        	logger.info("File #1 has been downloaded successfully."); 
	        	downloadFile("Attachment", dir, fileName, response);
//	        	boolean delete=client.deleteFile(destDir+"/"+fileName);
//	        	if(delete)logger.info("File Deleted");	        	
	        	return true;
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		}
        return false;
	}	
	
	//Ryan - Fuction Untuk Meng - Kopy file dalam Folder ke folder
	//masih Tahap Pengembangan
	public static void copyDirectory(String source, String destination) throws IOException{
		File src = new File(source);
		File dst = new File(destination);
		copyDirectory(src, dst);
	}
	
	public static void copyDirectory(File srcPath, File dstPath) throws IOException{
		if (srcPath.isDirectory())
		{
			if (!dstPath.exists())
			{
				dstPath.mkdir();
			}

			String files[] = srcPath.list();
			for(int i = 0; i < files.length; i++)
			{
				if(files[i].contains(".pdf") || files[i].contains("TTD"))
				copyDirectory(new File(srcPath, files[i]), new File(dstPath, files[i]));
			}
		}
		else
		{
			if(!srcPath.exists())
			{
				logger.info("File or directory does not exist.");
			}
			else
			{
				InputStream in = null;
				OutputStream out = null;
				try {
					in = new FileInputStream(srcPath);
			        out = new FileOutputStream(dstPath);
	    
					// Transfer bytes from in to out
			        byte[] buf = new byte[1024];
					int len;
			        while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
				}finally {
					try {
						if(in != null) {
							in.close();
						}
						if(out != null) {
							out.close();
						}
					}catch (Exception e) {
						logger.error("ERROR :", e);
					}
				}
			}
		}
		logger.info("Directory copied.");
	}
	
	//Write file oleh user ke server -- ryan
		public static boolean writeFile(String lus_id, String sysdate) {
			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				/*String pathFileTemp = props.getProperty("temp.fileinput.user")+"\\2258";*/
				String content = sysdate;
				
				File file = new File("C:\\EkaWeb\\temp\\"+lus_id+"\\filename.txt");
				
				File pathFile = new File("C:\\EkaWeb\\temp\\"+lus_id);
				if(!pathFile.exists()) {
					pathFile.mkdirs();
		        }
				
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				fw = new FileWriter(file.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write(content);
				return true;

			} catch (IOException e) {
				logger.error("ERROR :", e);
				return false;
			}finally {
				try {
					if(fw != null) {
						fw.close();
					}
					if(bw != null) {
						bw.close();
					}
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		}	
	  
		//Read file oleh user ke server -- ryan
		public static boolean readFile(String lus_id, String sysdate) {
			BufferedReader in = null;
			try {
				File file = new File("C:\\EkaWeb\\temp\\"+lus_id+"\\filename.txt");
				in = new BufferedReader(new FileReader(file));
				String line;Integer jam = null;Integer menit = null;Integer detik = null; Integer lusId;
				Integer jam2 = null;Integer menit2 = null;Integer detik2 = null; Integer lusId2;
				String isiText="";
				if((line = in.readLine()) != null)
				{
					isiText=line.substring(line.lastIndexOf("~")+1, line.length());
					sysdate=sysdate.substring(line.lastIndexOf("~")+1, line.length());
					logger.info(jam.parseInt(isiText.substring(0,2)));
					logger.info(menit.parseInt(isiText.substring(3,isiText.lastIndexOf(":"))));
					logger.info(detik.parseInt(isiText.substring(6,8)));
					logger.info(jam2.parseInt(sysdate.substring(0,2)));
					logger.info(menit2.parseInt(sysdate.substring(3,isiText.lastIndexOf(":"))));
					logger.info(detik2.parseInt(sysdate.substring(6,8)));
					
					in.close();
					return false;
				}else{
					return true;
				}

			} catch (IOException e) {
				logger.error("ERROR :", e);
				return false;
			}finally {
				try {
					if(in != null) {
						in.close();
					}
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		}	
}
