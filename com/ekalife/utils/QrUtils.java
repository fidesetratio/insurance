package com.ekalife.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * 
 * @author Patar.Tambunan
 * QrUtils works as utils for receive, save and give
 * path image properly to jasper.
 * Need to instance it properly
 */
public class QrUtils {
		private String policy;
		private Properties props;
		private String ip;
		private String pathTemp;
		private ServletContext context;
		public QrUtils(Properties props, String policy,ServletContext context){
			this.props = props;
			this.policy = policy;
			this.context = context;
			this.init();
		}
		
		private void init(){
			this.ip = props.getProperty("qr.webservice").toString();
			this.pathTemp = props.get("qr.internal.path").toString();
		}
		private String process(){
			//String path = "";
			String path = props.get("qr.internal.path").toString();
			Integer mode =  Integer.parseInt(props.get("qr.mode").toString());
			switch(mode){
			case 1: // check and save temp
				path = processAndCheckSaveTemp();
			break;
			}
			
			
			return path;
	
		}
		
		//https://stackoverflow.com/questions/23979842/convert-base64-string-to-image
		
		private String processAndCheckSaveTemp(){
			String path = "";
			String policy = this.policy;
            //GetMethod  method = new GetMethod("http://sourcecode:8080/Generator/api/qr/genqrc?value="+this.policy+"&tipe=2&flag=1");
           String url = props.get("qr.webservice").toString();
		//	GetMethod  method = new GetMethod("http://payment.sinarmasmsiglife.co.id:8880/Generator/api/qr/genqrc?value="+this.policy+"&tipe=2&flag=1");
       		GetMethod  method = new GetMethod(url+"/Generator/api/qr/genqrc?value="+this.policy+"&tipe=2&flag=1");
          
            HttpClient client = new HttpClient();
			// method 3 kali kalo bisa
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
			    		new DefaultHttpMethodRetryHandler(3, false));
			try {
			      // Execute the method.
			      int statusCode = client.executeMethod(method);
			      if (statusCode != HttpStatus.SC_OK) {
			        System.err.println("Method failed: " + method.getStatusLine());
			      }

			      // Read the response body.
			      byte[] responseBody = method.getResponseBody();
			      String base64 = new String(responseBody);
			      byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64);
			      System.out.println("Method responseBody:"+new String(responseBody));
			      BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
			  /*    String p = 	      this.getClass().getResource("images/Dinari.gif").getPath();
			      p = p.substring(0,p.length()-"/Dinari.gif".length());
			  */  String fileName = "";
			      fileName = this.context.getRealPath("/")+"images/"+this.policy+".png";
			      System.out.println("FileName::: use context"+fileName);
			      File outputfile = new File(fileName);
			      ImageIO.write(image, "png", outputfile);
			      path = fileName;
//			      path = "http://localhost:8080/E-Lions/images/09208201800001.png";
			    //  path = "http://localhost:8801/E-Lions/images/"+this.policy+".png";
			  //    path = ""
			      /* String base64EnCode = new String(responseBody);*/
			      // Deal with the response.
			      // Use caution: ensure correct character encoding and is not binary data
			    //  System.out.println("base64Encode="+base64EnCode);
			    } catch (HttpException e) {
			      System.err.println("Fatal protocol violation: " + e.getMessage());
			      e.printStackTrace();
			    } catch (IOException e) {
			      System.err.println("Fatal transport error: " + e.getMessage());
			      e.printStackTrace();
			    } finally {
			      // Release the connection.
			      method.releaseConnection();
			    }  
			
			
				
				
    		
			return path;
		}
		
		public String resolveQr(){
			String path = "";
			path = process();
			return path;
		}

}
