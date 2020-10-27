package com.ekalife.utils.iso8583;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import com.ekalife.utils.FormatString;

public class Iso8583Client implements Runnable{
	protected final static Log logger = LogFactory.getLog( Iso8583Client.class );
	private InputStream in;
	private OutputStream out;
	private Socket sock;
	private MessageHandler messageHandler;
	private String ip;
	private int port;
	private Thread th;
	private Boolean signOn;
	private Boolean echoTestSuccess;
	public String[] data = new String[] {};
	public String mssg;
	
	public Iso8583Client() {
		ip = "10.39.7.5";
		port = 31540;
		signOn = false;
		echoTestSuccess = false;
		int count = 1;
		
		//logger.info("================ Start Iso 8583  ================");
		//logger.info("set ip to " + getIp());
		//logger.info("set port to " + getPort());
		//logger.info("\n================ Connect to Giro Billing Server  ================");
		String con = createConnection();
		while(!con.equals("true") && count < 3) {
			logger.info(con);
			con = createConnection();
			count++;
		}
		//logger.info("-=* connection establish *=-");
		mssg = con;
		if(con.equals("true")) {
			logger.info("\n-=* send SignOn Mssg *=-");
			sendMssgToServer(isoMssgSignOn());			
		}
	}
	
	protected void reconnectServer() {
		CloseConnection();	
		//logger.info("\n================ Reconnect to Giro Billing Server  ================");
		String con = createConnection();
		while(!con.equals("true")) {
			logger.info(con);
			con = createConnection();	
		}
		//logger.info("-=* connection establish *=-");
		//logger.info("-=* send SignOn Mssg *=-");
		sendMssgToServer(isoMssgSignOn());		
	}
	
	public String getIp() { return ip; }
	public int getPort() { return port; }
	protected void setEchoTestSuccess(Boolean echoTestSuccess) { this.echoTestSuccess = echoTestSuccess; }
	protected Boolean getEchoTestSuccess() { return echoTestSuccess; }
	public Boolean getSignOn() { return signOn; }	

	/**
	 * Buat koneksi dengan server bank sinarmas
	 * @return String status koneksi
	 *
	 * @author Yusup_A
	 * @since Aug 26, 2009 (7:18:30 PM)
	 */
	private String createConnection() {
		try {
			sock = new Socket(getIp(),getPort());
			in = sock.getInputStream();
			out = sock.getOutputStream();
			th = new Thread(this);
			th.start();
			messageHandler = new MessageHandler();
			
		} catch (UnknownHostException e) {
			logger.error("ERROR :", e);
			return "!! can't connect server !!";
		} catch (IOException e) {
			logger.error("ERROR :", e);
			return "!! can't transfer data !!";
		}
		
		return "true";
	}
	
	private String constructMssg(String partMssg) {
		String fullMssg = partMssg.substring(4);
		int len = partMssg.length();
		fullMssg = FormatString.rpad("0",Integer.toString(len),4)+partMssg;
		
		return fullMssg; 
	}
	
	public void sendMssgToServer(String mssg) {
		try {
			out.write(mssg.getBytes());
			logger.info("To Server : " + mssg);
		} catch (IOException e) {
			logger.error("ERROR :", e);
		}finally {
			CloseConnection();
		}
	}
	
	/**
	 * Buat pesan iso message untuk proses Sign On<br><br>
	 * 
	 * field 1  : bitmap<br>
	 * field 7  : TRANSMISSION DATE AND TIME [MMDDhhmmss | n10]<br>
	 * field 11 : SYSTEM TRACE AUDIT NUMBER [n6]<br>
	 * field 33 : FORWARDING INSTITUTION IDENT CODE [LLVar | n ..11]<br>
	 * field 70 : NETWORK MANAGEMENT INFORMATION CODE [001 | n3]
	 * 
	 * @return String iso_mssg_SignOn
	 *
	 * @author Yusup_A
	 * @since Aug 26, 2009 (7:19:17 PM)
	 */
	private String isoMssgSignOn() {
		SimpleDateFormat df = new SimpleDateFormat("MMddHHmmss");
		
		String isoType  = "0800";
		String field1   = "82200000800000000400000000000000";
		String field7   = df.format(new Date());
		String field11  = "028309";
		String field33  = "03153";
		String field70  = "001";
		String partMssg = isoType+field1+field7+field11+field33+field70;

		return constructMssg(partMssg);
	}

	/**
	 * Buat pesan iso message untuk proses Echo Test<br><br>
	 * 
	 * field 1  : bitmap<br>
	 * field 7  : TRANSMISSION DATE AND TIME [MMDDhhmmss | n10]<br>
	 * field 11 : SYSTEM TRACE AUDIT NUMBER [n6]<br>
	 * field 33 : FORWARDING INSTITUTION IDENT CODE [LLVar | n ..11]<br>
	 * field 70 : NETWORK MANAGEMENT INFORMATION CODE [301 | n3]
	 * 
	 * @return String iso_mssg_EchoTest
	 *
	 * @author Yusup_A
	 * @since Aug 26, 2009 (7:19:17 PM)
	 */		
	protected String isoMssgEchoTest() {
		SimpleDateFormat df = new SimpleDateFormat("MMddHHmmss");
		
		String isoType  = "0800";
		String field1   = "82200000800000000400000000000000";
		String field7   = df.format(new Date());
		String field11  = "028309";
		String field33  = "03153";
		String field70  = "301";
		String partMssg = isoType+field1+field7+field11+field33+field70;
		
		return constructMssg(partMssg);		
	}
	
	/**
	 * Buat pesan iso message untuk proses Sign Off<br><br>
	 * 
	 * field 1  : bitmap<br>
	 * field 7  : TRANSMISSION DATE AND TIME [MMDDhhmmss | n10]<br>
	 * field 11 : SYSTEM TRACE AUDIT NUMBER [n6]<br>
	 * field 33 : FORWARDING INSTITUTION IDENT CODE [LLVar | n ..11]<br>
	 * field 70 : NETWORK MANAGEMENT INFORMATION CODE [002 | n3]
	 * 
	 * @return String iso_mssg_SignOff
	 *
	 * @author Yusup_A
	 * @since Aug 26, 2009 (7:19:17 PM)
	 */
	public String isoMssgSignOff() {
		SimpleDateFormat df = new SimpleDateFormat("MMddHHmmss");
		
		String isoType  = "0800";
		String field1   = "82200000800000000400000000000000";
		String field7   = df.format(new Date());
		String field11  = "028309";
		String field33  = "03153";
		String field70  = "002";
		String partMssg = isoType+field1+field7+field11+field33+field70;
	
		return constructMssg(partMssg);
	}
	
	/**
	 * Buat pesan iso message untuk proses Transfer Inquiry<br><br>
	 * 
	 * field 1   : bitmap<br>
	 * field 2   : PAN - PRIMARY ACCOUNT NUMBER [LLVar | n ..19]<br>
	 * field 3   : PROCESSING CODE [37xxxx | n6]<br>
	 * field 4   : AMOUNT, TRANSACTION [n18(16,2)]<br>
	 * field 7   : TRANSMISSION DATE AND TIME [MMDDhhmmss | n10]<br>
	 * field 11  : SYSTEM TRACE AUDIT NUMBER [n6]<br>
	 * field 12  : TIME, LOCAL TRANSACTION [hhmmss | n6]<br>
	 * field 13  : DATE, LOCAL TRANSACTION [MMDD | n4]<br>
	 * field 15  : DATE, SETTLEMENT [MMDD | n4]<br>
	 * field 18  : MERCHANTS TYPE [n4]<br>
	 * field 32  : ACQUIRING INSTITUTION IDENT CODE [LLVar | n ..11]<br> 
	 * field 33  : FORWARDING INSTITUTION IDENT CODE [LLVar | n ..11]<br>
	 * field 37  : RETRIEVAL REFERENCE NUMBER [an12]<br>
	 * field 41  : CARD ACCEPTOR TERMINAL IDENTIFICACION [ans8]<br>
	 * field 49  : CURRENCY CODE, TRANSACTION [a or n3]<br>
	 * field 100 : RECEIVING INSTITUTION IDENT CODE [LLVar | n ..11]<br>
	 * field 102 : ACCOUNT IDENTIFICATION 1 (from) [LLVar | ans ..28]<br>
	 * field 103 : ACCOUNT IDENTIFICATION 2 (to) [LLVar | ans ..28]<br>
	 * field 127 : 
	 * 
	 * @param String noRek
	 * @return String iso_mssg_Transfer_Inquiry
	 *
	 * @author Yusup_A
	 * @since Aug 27, 2009 (9:40:48 AM)
	 */
	public String isoMssgTransferInquiry(String noRek) {
		SimpleDateFormat df1 = new SimpleDateFormat("MMddHHmmss");
		SimpleDateFormat df2 = new SimpleDateFormat("MMdd");
		SimpleDateFormat df3 = new SimpleDateFormat("HHmmss");
		Date now = new Date();
		
		String isoType  = "0200";
		String field1   = "F23A4001888080000000000016000002";
		String field2   = "161234567890123456";
		String field3   = "370000";
		String field4   = "000000000000000000";
		String field7   = df1.format(now);
		String field11  = "000001";
		String field12  = df3.format(now);
		String field13  = df2.format(now);
		String field15  = df2.format(now);
		String field18  = "6011";
		String field32  = "03153";
		String field33  = "03153";
		String field37  = "112233445566";
		String field41  = "00000001";
		String field49  = "360";
		String field100 = "03153";
		String field102 = FormatString.rpad("0", Integer.toString(noRek.length()), 2)+noRek;
		String field103 = FormatString.rpad("0", Integer.toString(noRek.length()), 2)+noRek;
		String field127 = "003153";
		String partMssg = isoType+field1+field2+field3+field4+field7+
						  field11+field12+field13+field15+field18+field32+
						  field33+field37+field41+field49+field100+field102+
						  field103+field127;

		return constructMssg(partMssg);
	}
	
	/**
	 *  Menangkap semua iso message yg dikirim oleh server BSM
	 * 
	 *
	 * @author Yusup_A
	 * @since Aug 27, 2009 (2:52:33 PM)
	 */
	public void run() {
		while(th != null) {
			byte b[] = new byte[1024];
			int len;
			StringBuffer response = new StringBuffer();
			
			//logger.info("-=* Start Input Stream Thread *=-");
			try {
				len = in.read(b);
				String subResponse=new String(b,0,len);
				response.append(subResponse);
				
				String[] mssg = parseIsoMssg(response.toString());
				if(mssg[0].equals("0810")) {
					if(mssg[70].equals("001")) {
						signOn = true;
						data = mssg;
						//logger.info("-=* SignOn Mssg accepted *=-");
						
						//ManageConnection mc = new ManageConnection(this);						
					}
					else if(mssg[70].equals("301")) {
						setEchoTestSuccess(true);
						data = mssg;
						//logger.info("-=* EchoTest Mssg accepted *=-");
					}
					/*else if(mssg[70].equals("002")) {
						data = mssg;
						//logger.info("-=* EchoTest Mssg accepted *=-");
						CloseConnection();
					}*/					
				}
				else if(mssg[0].equals("0210")) {
					data = mssg;
					//logger.info("-=* Transfer Inquiry Reply *=-");
				}
				
			} catch (IOException e) {
				logger.error("ERROR :", e);
				
			}finally {
				CloseConnection();
			}
			
		}
	}
	
	/**
	 * Menutup stream, menghapus thread, & menutup socket
	 * 
	 *
	 * @author Yusup_A
	 * @since Aug 27, 2009 (2:53:56 PM)
	 */
	public void CloseConnection() {
		try {
			in.close();
			out.close();
			th.stop();
			//th.destroy();
			sock.close();
		} catch (IOException e) {
			logger.error("ERROR :", e);
		}
	}

	/**
	 * memparse iso message menjadi data yg dapat di baca
	 * 
	 * @param isoMssg
	 *
	 * @author Yusup_A
	 * @since Aug 27, 2009 (2:54:39 PM)
	 */
	private String[] parseIsoMssg(String isoMssg) {
		String dataFromServer = isoMssg.substring(4);
		logger.info("From Server : " + isoMssg);

		try {
			ISOMsg isoMsg = messageHandler.unpackRequest(dataFromServer);

			return messageHandler.process(isoMsg);
		} catch (ISOException e) { logger.error("ERROR :", e); } 
		catch (Exception e) { logger.error("ERROR :", e); }
		
		return null;
	}
	
	public String checkNoRek(String rek) {
		if(rek.contains("-")) { rek = rek.replace("-", ""); }
		else if(rek.contains(".")) { rek = rek.replace(".", ""); }

		return FormatString.rpad("0",rek,10);
	}

	/**
	 * Term of Use :<br/><br/>
	 * 
	 * 1.  Buat object dari class Iso8583Client<br/>
	 * 2.  Masukkin semua no rekening kedlm 1 buah arrray<br/>
	 * 3.  Looping semua no yg  mau di cek<br/>
	 * 4.  Kirim pesan SignOff<br/>
	 * 5.  Untuk perbandingan :<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;- cek arr[103]	dgn no rek di db<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;- cek arr[48] dgn nama pemilik di db<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* kalau arr[48] = null atau arr[48] = kosong, brarti tidak ada no rek nya<br/>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;* 30 digit pertama dari arr[48] adalah yg dibuat perbandingan nama nya dengan db<br/> 	
	 * 
	 * @param args
	 *
	 * @author Yusup_A
	 * @since Sep 4, 2009 (4:55:36 PM)
	 */
	public static void main(String args[]) {
		//1
		Iso8583Client c =  new Iso8583Client();

		while(c.data.length == 0) {;}
		logger.info("SignOn mssg");
		for(int b=0;b<c.data.length;b++) {
			if(c.data[b] != null) {
				logger.info("field : " + b + " : " + c.data[b]);
			}
		}		
		
		//2
		String[] test = new String[] {"0001830357","0001742655"};
		
		//3
		for(int a=0;a<test.length;a++) {
			c.data = new String[] {};
			logger.info("\n-=* send Transfer Inquiry Mssg *=-");
			c.sendMssgToServer(c.isoMssgTransferInquiry(c.checkNoRek(test[a])));
			while(c.data.length == 0) {;}
			logger.info("Transfer Inquiry mssg");
			for(int b=0;b<c.data.length;b++) {
				if(c.data[b] != null && !c.data[b].trim().equals("")) {
					logger.info("field : " + b + " : " + c.data[b]);
				}
			}
			logger.info("");
		}
		
		/*c.data = new String[] {};
		logger.info("\n-=* send EchoTest Mssg *=-");
		c.sendMssgToServer(c.isoMssgEchoTest());
		while(c.data.length == 0) {;}
		logger.info("EchoTest mssg");			
		for(int b=0;b<c.data.length;b++) {
			if(c.data[b] != null) {
				logger.info("field : " + b + " : " + c.data[b]);
			}
		}*/

		//4
		/*c.data = new String[] {};
		logger.info("\n-=* send SignOff Mssg *=-");
		c.sendMssgToServer(c.isoMssgSignOff());
		while(c.data.length == 0) {;}
		logger.info("SignOff mssg");
		for(int b=0;b<c.data.length;b++) {
			if(c.data[b] != null) {
				logger.info("field : " + b + " : " + c.data[b]);
			}
		}*/	
		logger.info("\n-=* send SignOff Mssg *=-");
		c.sendMssgToServer(c.isoMssgSignOff());
		c.CloseConnection();
	}
	
}
