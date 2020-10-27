package com.ekalife.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.common.resources.Resources;
//test
/* f_hit_umur
 * Created on Jul 18, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class f_hit_umur implements Serializable{
	
	protected static final Log logger = LogFactory.getLog( f_hit_umur.class );
	
	private static final long serialVersionUID = 1L;
	
	private Properties props;
	
	public f_hit_umur() {
		try {
			props = new Properties();
			props.load(new FileInputStream("C:\\EkaWeb\\jdbc_properties\\jdbc.properties"));
			
		} catch (IOException e) {
			logger.error("ERROR :", e);
		}		
	}
	
	public int umur(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2) 
	{
		int li_month = 0;
		int li_Umur = 0;
		int li_add = 0;
		int li_curr_month = 0;
		int li_curr_year = 0;
		int li_curr_day = 0;
		//Calendar tgl_sekarang = Calendar.getInstance(); 
		/*li_curr_day   = tgl_sekarang.get(Calendar.DAY_OF_MONTH);
		li_curr_month = tgl_sekarang.get(Calendar.MONTH)+1;
		li_curr_year  = tgl_sekarang.get(Calendar.YEAR);*/
		li_curr_day =tanggal2;
		li_curr_month=bulan2;
		li_curr_year=tahun2;

		if (tahun1 != li_curr_year)
		{
			if (li_curr_month >= bulan1)
			{
				li_Umur = li_curr_year - tahun1;
			}else{
				li_Umur = (li_curr_year - tahun1) - 1;
				li_add = 12;
			}
			li_month = li_curr_month + li_add - bulan1;
			if (li_month >= 6)
			{
				if (li_month==6)
				{
					if ((li_curr_day  - tanggal1) >= 0)
					{
						li_Umur = li_Umur+1;
					}
				}else{
					li_Umur = li_Umur+1;
				}
				
			}
		}
		if (li_Umur<0){
			li_Umur=0;
		}
		return li_Umur;
	}
	
	public int hari(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2) 
	{
		Date dt=null;
		int li_curr_month = 0;
		int li_curr_year = 0;
		int li_curr_day = 0;
		li_curr_day =tanggal2;
		li_curr_month=bulan2;
		li_curr_year=tahun2;
		String date1=li_curr_month+"/"+li_curr_day+"/"+li_curr_year;
		String date2=bulan1+"/"+tanggal1+"/"+tahun1;
		long j=java.util.Date.parse(date1); //converts date into milliseconds
		long k=java.util.Date.parse(date2);		
		int q=(int)Math.abs((j-k)/24/60/60/1000);	
		return q;
	}	
	
	
	public int hari_powersave(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2) 
	{
		Date dt=null;
		int li_curr_month = 0;
		int li_curr_year = 0;
		int li_curr_day = 0;
		li_curr_day =tanggal2;
		li_curr_month=bulan2;
		li_curr_year=tahun2;
		String date1=li_curr_month+"/"+li_curr_day+"/"+li_curr_year;
		String date2=bulan1+"/"+tanggal1+"/"+tahun1;
		long j=java.util.Date.parse(date1); //converts date into milliseconds
		long k=java.util.Date.parse(date2);		
		int q=(int)((j-k)/24/60/60/1000);	
		if (q<0)
		{
			q=0;
		}
		return q;
	}	
	
	public int hari1(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2) 
	{
		Date dt=null;
		int li_curr_month = 0;
		int li_curr_year = 0;
		int li_curr_day = 0;
		li_curr_day =tanggal2;
		li_curr_month=bulan2;
		li_curr_year=tahun2;
		String date1=li_curr_month+"/"+li_curr_day+"/"+li_curr_year;
		String date2=bulan1+"/"+tanggal1+"/"+tahun1;
		long j=java.util.Date.parse(date1); //converts date into milliseconds
		long k=java.util.Date.parse(date2);		
		int q=(int)((j-k)/24/60/60/1000);	
		return q;
	}		
	
	public int bulan(int tahun1, int bulan1, int tanggal1,int tahun2, int bulan2, int tanggal2) 
	{
		String err="";
		Date dt=null;
		int q=0;
		Double temp=0.0;
		int li_curr_month = 0;
		int li_curr_year = 0;
		int li_curr_day = 0;
		li_curr_day =tanggal2;
		li_curr_month=bulan2;
		li_curr_year=tahun2;
		String date1=li_curr_day+"/"+li_curr_month+"/"+li_curr_year;
		String date2=tanggal1+"/"+bulan1+"/"+tahun1;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
		  Class.forName(props.getProperty("oracle.driver"));
		  con =  DriverManager.getConnection(props.getProperty("ajsdb.jdbc.url"),props.getProperty("ajsdb.jdbc.user"),props.getProperty("ajsdb.jdbc.password"));
		  ps = con.prepareStatement("SELECT MONTHS_BETWEEN (TO_DATE(?,'DD/MM/YYYY'),TO_DATE(?,'DD/MM/YYYY')) tgl FROM DUAL");
		  ps.setString(1, date1);
		  ps.setString(2, date2);
		  rs = ps.executeQuery();
		  if(rs.next()) {
		  	temp= rs.getDouble("tgl");
		  	q=(int)Math.round(temp);
		  }
		  
		}
		  catch (Exception e) {
			err=e.toString();
		  }finally {
			  try {
				  if(rs != null) {
					  rs.close();
				  }
				  if(ps != null) {
					  ps.close();
				  }
				  if(con != null) {
					  con.close();
				  }
			  }catch (Exception e) {
				logger.error("ERROR :", e);
			}
		  }

		return q;
	}	
	
	public Date f_add_months(int tahun,int bulan, int tanggal,int li_month)
	{
		String err="";
		String date1=tanggal+"/"+bulan+"/"+tahun;
		Date date2=null;

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			  Class.forName(props.getProperty("oracle.driver"));
			  con =  DriverManager.getConnection(props.getProperty("ajsdb.jdbc.url"),props.getProperty("ajsdb.jdbc.user"),props.getProperty("ajsdb.jdbc.password"));
			  ps = con.prepareStatement("SELECT ADD_MONTHS( to_date(?,'DD/MM/YYYY'), ?) tgl  FROM DUAL");
			  ps.setString(1, date1);
			  ps.setInt(2, li_month);
			  rs = ps.executeQuery();
			  if(rs.next()) {
				date2= rs.getDate("tgl");
			  }
		}
		  catch (Exception e) {
			err=e.toString();
		  }finally {
			  try {
				  if(rs != null) {
					  rs.close();
				  }
				  if(ps != null) {
					  ps.close();
				  }
				  if(con != null) {
					  con.close();
				  }
			  }catch (Exception e) {
				logger.error("ERROR :", e);
			}
		  }

		  return date2;
	}

	public static void main(String[] args) {
		f_hit_umur a = new f_hit_umur();
		int jml = a.bulan(1957, 9, 4,2006, 3, 22) ;

	}
}
