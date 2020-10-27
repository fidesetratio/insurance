package com.ekalife.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.client.rpc.SerializableException;

public class F_CopyObjectToFile extends SerializableException{
	protected static final Log logger = LogFactory.getLog( F_CopyObjectToFile.class );

	/**
	 *@author Deddy
	 *@since Aug 7, 2015
	 *@description TODO 
	 */
	private static final long serialVersionUID = -6655682715774838268L;

	/**
	 * Class ini digunakan untuk Serializeable dari sebuah object ke dalam File atau sebaliknya.
	 * serializeable untuk menyimpan object dan ditampung dalam sebuah file mis : txt.
	 * unserializeable untuk membaca file yang pernah ditampung menjadi sebuah object kembali. 
	 * 
	 * @author Deddy
	 * @since Nov 23, 2011 4:34:14 PM
	 * @param args 
	 */
	public static void serializable(Object cmd, String pathFile)throws IOException {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			fout = new FileOutputStream(pathFile);
			oos = new ObjectOutputStream(fout);
			oos.writeUnshared(cmd);
		}
		catch (Exception e) { logger.error("ERROR :", e); }
		finally{
			if(oos!=null){
				oos.flush();
				oos.close();
			}
			if(fout!=null){
				fout.flush();
				fout.close();
			}
		}
	}
	
	public static Object unserializeable(Object cmd, String pathFile) throws IOException{
		FileInputStream fin = null;
		ObjectInputStream ois = null;
//		BufferedReader tes= new BufferedReader(new InputStreamReader(fin));
		try{
//			Integer i = tes.read();
//			String j = tes.readLine();
//			logger.info(j);
			fin = new FileInputStream(pathFile);
			ois = new ObjectInputStream(fin);
			cmd = (Object) ois.readUnshared();
		}catch(Exception e){
			logger.error("ERROR :", e);
		}finally{
			if(fin!=null){
				fin.close();
			}
			if(ois!=null){
				ois.close();
			}
		}
	    
		return cmd;     
	}
	
	
}
