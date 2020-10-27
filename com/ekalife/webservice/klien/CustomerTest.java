package com.ekalife.webservice.klien;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import javax.xml.ws.Holder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * kelas untuk testing webservice
 * @author Bertho Rafitya Iwasurya
 * @since Oct 24, 2011 3:16:56 PM
 *
 */
public class CustomerTest {
	protected final static Log logger = LogFactory.getLog( CustomerTest.class );
	/**
	 * @param args
	 * @author Bertho Rafitya Iwasurya
	 * @throws CreateAccountFault_Exception 
	 * @since Oct 20, 2011 1:18:48 PM
	 */
	public static void main(String[] args)  {
		// TODO Auto-generated method stub
		 CustomerAgencyService memberService=new CustomerAgencyService();
       	 CustomerAgencyPortType ports=memberService.getCustomerAgencyPort();
       	 
       	 CustomerDetail customer=new CustomerDetail();
       	 customer.address="adadad";
       	 customer.atmCardNo="123456781772";
       	 customer.birthDate="1984-01-04 00:00:00";//format : yyyy-mm-dd hh:mm:ss
       	 customer.birthPlace="Jakarta";
       	 customer.city="Jakarta";
       	 customer.custcode="471125";
       	 customer.gender="M";
       	 customer.homePhone="124112424";
       	 customer.idExpiryDate="2015-01-04 00:00:00";//format : yyyy-mm-dd hh:mm:ss
       	 customer.idNumber="124124";
       	 customer.mobilePhone="124124";
       	 customer.mothersMaiden="IBU";//min char : 3
       	 customer.name="BAPAK RAFITYA";//min char : 3
       	 customer.postalCode="10650";
       	 customer.workPhone="124214124";
       	 
       	 ChannelDetail channel=new ChannelDetail();
       	 channel.agentId="asf";
       	 channel.branchCode="01";
       	 channel.company="AJSM";
       	 channel.txReff="12123";
       	 
       	Holder<String> responseCode=new Holder<String>("");
       	Holder<String> message=new Holder<String>("");
       	Holder<String> parameter=new Holder<String>("");	 
       	setProxy();
       	 try {
       		 /**
       		  * create account
       		  */
		 ports.createAccount(channel, customer, responseCode, message, parameter);
		 logger.info("Response Code ="+responseCode.value);
         logger.info("Message ="+message.value);
         logger.info("Parameter ="+parameter.value);
       		 
       		 /**
       		  * cek inquiry berdasarkan cust code
       		  */
//			List<AccountbyCustcode> aa= ports.enquiryAccountbyCustcode("AJSM", "471125");
//			for(AccountbyCustcode a:aa){
//				logger.info("account no : "+a.getAccountNo());
//				logger.info("atm no : "+a.getAtmCardNo());
//				logger.info("cust code   : "+a.getCustCode());
//				logger.info("cust name : "+a.getCustName());
//				logger.info("message : "+a.getMessage());
//				
//			}
//       	
       		 /**
       		  * cek inquiry approve berdasarkan tanggal 
       		  */
//       	List<ApprovedAccount> bb= ports.enquiryApprovedAccount("AJSM", "2011-01-04 00:00:00", "2011-11-04 00:00:00");
//			for(ApprovedAccount b:bb){
//				logger.info("account no : "+b.getAccountNo());
//				logger.info("atm no : "+b.getAtmCardNo());
//				logger.info("cust code   : "+b.getCustCode());
//				logger.info("cust name : "+b.getCustName());
//			}
       		
       		/**
       		  * cek inquiry reject berdasarkan tanggal 
       		  */
//       		List<RejectedAccount> cc= ports.enquiryRejectedAccount("AJSM", "2011-10-24 14:00:00", "2011-10-24 17:00:00");
//			for(RejectedAccount c:cc){				
//				logger.info("cust code   : "+c.getCustCode());
//				logger.info("cust name : "+c.getCustName());
//			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		}
    
      
       
      
	}
	
	public static void setProxy() {

		final String AUTHUSER = null;
		final String AUTHPASSWORD = null;
		final String HOST = "ajsjava";
		final String PORT = "808";
		final int DEFAULT_MAX_REDIRECTS=1000;
		Authenticator.setDefault(
		   new Authenticator() {
		      public PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication(
		        		 AUTHUSER, AUTHPASSWORD.toCharArray());
		      }
		   }
		);
		System.setProperty( "http.maxRedirects",
                Integer.toString( DEFAULT_MAX_REDIRECTS ) );
		System.setProperty("http.proxyHost", HOST);
		System.setProperty("http.proxyPort", PORT);
//		System.setProperty("http.proxyUser", authUser);
//		System.setProperty("http.proxyPassword", authPassword);

	}

}
