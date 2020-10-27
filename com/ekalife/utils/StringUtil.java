package com.ekalife.utils;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: StringUtil
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Apr 4, 2008 3:41:13 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtil
{
    protected final static Log logger = LogFactory.getLog( StringUtil.class );

    public StringUtil()
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* StringUtil constructor is called ..." );
    }

    /**
     *
     * @param words: input String
     * @return String yang berformat camel and hump dan di trim
     * ex: "  saMueL baKtiAr " menjadi "Samuel Baktiar"
     */
    public static String camelHumpAndTrim( String words )
    {
        StringBuffer result = new StringBuffer( "" );
        if( words != null )
        {
            String ch;
            boolean isCap;
            words = words.trim();
            int size = words.length();

            for( int i = 0; i < size; i++ )
            {
                ch = words.substring( i, i + 1 );
                if( i == 0 )
                {
                    isCap = true;
                }
                else if( ch.equals( " " ) )
                {
                    result = result.append( " " );
                    isCap = true;
                    i++;
                    ch = words.substring( i, i + 1 );
                }
                else
                {
                    isCap = false;
                }

                ch = isCap? ch.toUpperCase() : ch.toLowerCase();
                result = result.append( ch );
            }
        }

        return result.toString();
    }
    
    public static String nomorPAS(String kata) {
		if (kata == null) {
			return "";
		}  else
			return kata.substring(0, 4) + " " + kata.substring(4, 8) + " "
			+ kata.substring(8, 12) + " "+ kata.substring(12) ;
	}
    
    /**
	 * Fungsi untuk memecah suatu string yang panjang menjadi beberapa baris
	 * tanpa memecah bagian suatu kata dan bisa diset panjang karakternya
	 * 
	 * @author Yusuf
	 * @since Aug 28, 2008 (8:26:12 PM)
	 * @param string
	 * @param max
	 * @return
	 */
	public static String[] pecahParagraf(String string, int max) {
		StringBuffer tmp = new StringBuffer();
		StringBuffer hasil = new StringBuffer();
		String[] katakata = string.split(" ");
		
		for(String kata : katakata) {			
			if(tmp.length()+kata.length() > max) {
				hasil.append(tmp);
				hasil.append("~");
				tmp = new StringBuffer();
			}
			tmp.append(kata + " ");
		}
		
		if(tmp.toString().length() > 0) hasil.append(tmp);
		
		return hasil.toString().trim().split("~");
	}
	
	/**
	 * Fungsi untuk memecah suatu string yang panjang menjadi beberapa baris
	 * tanpa memecah bagian suatu kata dan bisa diset panjang karakternya
	 * Jika Line breaks maka akan mengikuti 
	 * @param string
	 * @param max
	 * @return
	 * Filename : StringUtil.java
	 * Create By : Bertho Rafitya Iwasurya
	 * Date Created : Feb 16, 2010 9:28:12 AM
	 */
	public static String[] pecahParagrafLineBreaksInclude(String string, int max) {
		StringBuffer tmp = new StringBuffer();
		StringBuffer hasil = new StringBuffer();
		
		String []splitLines=string.split("\n");
		int item=0;
		for (int i = 0; i < splitLines.length; i++) {
			String splitLine=splitLines[i];
			
			String[] katakata = splitLine.split(" ");
			for(String kata : katakata) {			
				if(tmp.length()+kata.length()>max || i!=item) {
					hasil.append(tmp.toString().trim());
					hasil.append("~");
					tmp = new StringBuffer();
					item=i;
				}
				tmp.append(kata + " ");
			}
			
		}
		
		
		if(tmp.toString().length() > 0) hasil.append(tmp);
		
		return hasil.toString().trim().split("~");
	}

    public static boolean isEmpty( String content )
    {
        return content == null || content.trim().equals( "" );
    }
    public static void main( String[] args )
    {
        logger.info( "*-*-*-*  = " + camelHumpAndTrim( "samUel baKtiar" ));
    }
}
