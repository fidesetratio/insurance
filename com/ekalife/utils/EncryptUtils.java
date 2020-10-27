package com.ekalife.utils;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Fungsi2 tambahan untuk encryption
 * 
 * @author Yusuf Sutarko
 * @since Jun 27, 2007 (1:51:33 PM)
 */
public class EncryptUtils {
	protected final static Log logger = LogFactory.getLog( EncryptUtils.class );
	/**
	 * Encrypt dengan SHA-1, lalu Encode dengan Base64
	 * 
	 * @author Yusuf Sutarko
	 * @since Jun 27, 2007 (1:51:18 PM)
	 * @param bytes
	 * @return
	 */
	public static String encode(byte[] bytes) {
		return DatatypeConverter.printBase64Binary(DigestUtils.sha(bytes)).toString();
		//return new BASE64Encoder().encode(DigestUtils.sha(bytes));
	}
	
	//testing only 
	public static void main(String[] args) {
		
		String spaj[] = {
				"31200800008","31200800014","31200800016","31200800009","31200800007","31200800015","31200800017","31200800006","31200800010","31200800005"
		};
		
		for(String s : spaj) {
			logger.info("http://www.sinarmasmsiglife.co.id/E-Policy/common/confirm.htm?spaj="+s+"&auth=" + EncryptUtils.encode(("yusufsutarko"+s).getBytes()));
		}
		
		
	}

}