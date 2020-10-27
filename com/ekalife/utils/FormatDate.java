/**
 * 
 */
package com.ekalife.utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Class yang digunakan untuk fungsi yang berhubungan dengan Date dan Time
 * 
 * @author Yusuf
 * @since 01/11/2005
 */
public class FormatDate implements Serializable {
	protected static final Log logger = LogFactory.getLog( FormatDate.class );
	private static final long serialVersionUID = -783144883698637352L;

	private static final int MILLISECS_PER_DAY = 1000 * 60 * 60 * 24;

	public static Date stringToDate(String tanggalan) {
		String[] nguk = tanggalan.split(" ");
		try {
			nguk[1] = nguk[1].trim().toLowerCase();
			return (new SimpleDateFormat("dd/MM/yyyy")).parse(	
			nguk[0] + "/" +		
				(
						(nguk[1].equals("jan") || nguk[1].equals("januari") || nguk[1].equals("january")) ? "01" : 
						(nguk[1].equals("feb") || nguk[1].equals("februari") || nguk[1].equals("february")) ? "02" : 
						(nguk[1].equals("mar") || nguk[1].equals("maret") || nguk[1].equals("march")) ? "03" : 
						(nguk[1].equals("apr") || nguk[1].equals("april")) ? "04" : 
						(nguk[1].equals("may") || nguk[1].equals("mei")) ? "05" : 
						(nguk[1].equals("jun") || nguk[1].equals("juni") || nguk[1].equals("june")) ? "06" : 
						(nguk[1].equals("jul") || nguk[1].equals("juli") || nguk[1].equals("july")) ? "07" : 
						(nguk[1].equals("aug") || nguk[1].equals("agustus") || nguk[1].equals("august") || nguk[1].equals("ags")) ? "08" : 
						(nguk[1].equals("sep") || nguk[1].equals("september")) ? "09" : 
						(nguk[1].equals("okt") || nguk[1].equals("oktober") || nguk[1].equals("october") || nguk[1].equals("oct")) ? "10" : 
						(nguk[1].equals("nov") || nguk[1].equals("november")) ? "11" : 
						(nguk[1].equals("dec") || nguk[1].equals("december") || nguk[1].equals("desember") || nguk[1].equals("des")) ? "12" : ""
				)+"/"+nguk[2]);
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
		return null;
	}
	
	public static String toIndonesian(String yyyymm) {
		return (yyyymm.substring(4).equals("01") ? " Januari " : yyyymm
				.substring(4).equals("02") ? " Februari " : yyyymm
				.substring(4).equals("03") ? " Maret " : yyyymm
				.substring(4).equals("04") ? " April " : yyyymm
				.substring(4).equals("05") ? " Mei " : yyyymm
				.substring(4).equals("06") ? " Juni " : yyyymm
				.substring(4).equals("07") ? " Juli " : yyyymm
				.substring(4).equals("08") ? " Agustus " : yyyymm
				.substring(4).equals("09") ? " September " : yyyymm
				.substring(4).equals("10") ? " Oktober " : yyyymm
				.substring(4).equals("11") ? " November " : yyyymm
				.substring(4).equals("12") ? " Desember " : "")+yyyymm.substring(0,4);
	}
	
	/**
	 * Fungsi untuk menampilkan tanggal dalam bahasa indonesia
	 * 
	 * @param tgl
	 *            Tanggal yang ingin ditambahkan
	 * @return String hasilnya
	 * @see Date, Calendar
	 */
	public static String toIndonesian(Date tgl) {
		if (tgl == null) return "";
		DateFormat df = new SimpleDateFormat("ddMMyyyy");
		String temp = df.format(tgl);
		return Integer.valueOf(temp.substring(0, 2))
				+ (temp.substring(2, 4).equals("01") ? " Januari " : temp
						.substring(2, 4).equals("02") ? " Februari " : temp
						.substring(2, 4).equals("03") ? " Maret " : temp
						.substring(2, 4).equals("04") ? " April " : temp
						.substring(2, 4).equals("05") ? " Mei " : temp
						.substring(2, 4).equals("06") ? " Juni " : temp
						.substring(2, 4).equals("07") ? " Juli " : temp
						.substring(2, 4).equals("08") ? " Agustus " : temp
						.substring(2, 4).equals("09") ? " September " : temp
						.substring(2, 4).equals("10") ? " Oktober " : temp
						.substring(2, 4).equals("11") ? " November " : temp
						.substring(2, 4).equals("12") ? " Desember " : "")
				+ temp.substring(4);
	}
	

	/**
	 * Fungsi untuk Tambah Tanggal (contoh: FormatDate.add(tanggal, Calendar.DATE, 30) atau add(new Date(), Calendar.MONTH, 1)),
	 * bisa juga menggunakan negatif (untuk mengurangi)
	 * 
	 * @param tanggal
	 *            Tanggal yang ingin ditambahkan
	 * @param kalendar
	 *            Konstanta penambah, sesuai dengan konstanta yang ada di class
	 *            Calendar
	 * @param angka
	 *            Jumlah angka yang ingin ditambahkan ke tanggal bersangkutan
	 * @return Date hasil setelah ditambahkan (atau dikurangi)
	 * @see Date, Calendar
	 */
	public static Date add(Date tanggal, int kalendar, int angka) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(tanggal);
		cal.add(kalendar, angka);
		return cal.getTime();
	}

	/**
	 * Fungsi untuk mengecek apakah tanggal berada diantara awal dan akhir
	 * 
	 * @param tanggal
	 *            Tanggal yang ingin di-cek
	 * @param awal
	 *            Tanggal batas awal
	 * @param akhir
	 *            Tanggal batas akhir
	 * @return boolean
	 * @see Date
	 */
	public static boolean isDateBetween(Date tanggal, Date awal, Date akhir) {
		if (tanggal.compareTo(awal) < 0)
			return false;
		else if (tanggal.compareTo(akhir) > 0)
			return false;
		else
			return true;
	}

	/**
	 * Fungsi untuk menghitung selisih antara 2 tanggal (dalam bentuk angka
	 * absolut / tidak ada negatif)
	 * 
	 * @param before
	 *            Tanggal awal
	 * @param after
	 *            Tanggal akhir
	 * @param flagAbsolut
	 *            True: return value selalu positif, False: tergantung selisih antara after-before
	 * @return long yang berisi selisihnya
	 * @see Date, Calendar
	 */
	public static long dateDifference(Date before, Date after, boolean flagAbsolut) {
		Calendar calAfter = Calendar.getInstance();
		Calendar calBefore = Calendar.getInstance();
		calAfter.setTime(after);
		calBefore.setTime(before);
		long endL = calAfter.getTimeInMillis()
				+ calAfter.getTimeZone().getOffset(calAfter.getTimeInMillis());
		long startL = calBefore.getTimeInMillis()
				+ calBefore.getTimeZone()
						.getOffset(calBefore.getTimeInMillis());
		if(flagAbsolut)
			return Math.abs((endL - startL) / MILLISECS_PER_DAY);
		else 
			return (endL - startL) / MILLISECS_PER_DAY;
	}

	/**
	 * Fungsi untuk menghitung selisih antara 2 tanggal dalam tahun
	 * 
	 * @param before
	 *            Tanggal awal
	 * @param after
	 *            Tanggal akhir
	 * @return int yang berisi selisihnya
	 * @see Date, Calendar
	 */
	public static int dateDifferenceInYears(Date before, Date after) {
		return after.getYear()-before.getYear();
	}

	/**
	 * Fungsi untuk mem-format tanggal dalam bentuk dd/mm/yyyy
	 * 
	 * @param tanggal
	 *            Tanggal yang ingin di-format
	 * @return string 
	 * 			hasilnya
	 */
	public static String toString(Date tanggal) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(tanggal);
	}
	/**
	 * Fungsi untuk mem-format tanggal dalam bentuk ddmmyyyy
	 * 
	 * @param tanggal
	 *            Tanggal yang ingin di-format
	 * @return string 
	 * 			hasilnya
	 */
	public static String toStringWithOutSlash(Date tanggal) {
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
		return df.format(tanggal);
	}
	public static Date toDate(String tanggal) {
		int yy,mm,dd;
		dd=Integer.parseInt(tanggal.substring(0,2));
		mm=Integer.parseInt(tanggal.substring(2,4));
		yy=Integer.parseInt(tanggal.substring(4,8));
		
		Calendar cal= new GregorianCalendar(yy,mm,dd);
		
		return cal.getTime();
	}
	
	public static Date toFixDate(String tanggal) {
		int yy,mm,dd;
		dd=Integer.parseInt(tanggal.substring(0,2));
		mm=Integer.parseInt(tanggal.substring(2,4));
		yy=Integer.parseInt(tanggal.substring(4,8));
		
		Calendar cal= new GregorianCalendar(yy,mm-1,dd);
	    
		return cal.getTime();
	}

	public static Date toDateWithSlash(String tanggal) {
		int yy,mm,dd;
		dd=Integer.parseInt(tanggal.substring(0,2));
		mm=Integer.parseInt(tanggal.substring(3,5));
		yy=Integer.parseInt(tanggal.substring(6,10));
		Calendar cal= new GregorianCalendar(yy,mm-1,dd);
	    
		return cal.getTime();
	}
	/**
	 * Fungsi untuk menghitung umur seseorang, yang merupakan selisih dari 
	 * tanggal input dengan tanggal lahir
	 * 
	 * @param awal
	 *            Tanggal lahir
	 * @param akhir
	 * 			  Tanggal saat ini 
	 * @return hasil
	 * Misalnya :	awal="18/10/1972"
	 * 				akhir="11/09/1997"
	 * 				umur="241023"  ==> (24 bulan tahun, 10 bulan, 23 hari)
	 * 		Umur seseorang lengkap dengan tahun,bulan, dan hari
	 * 			
	 */
	public static String getUmur(Date tglInput,Date tglLahir) {
		String umur="000000";
		int liHari1, liHari2, liBulan1, liBulan2, liTahun1, liTahun2;
		int liHari3 = 0, liBulan3, liTahun3, liBulanTemp;
		NumberFormat nf=new DecimalFormat("00");
		
		if(Math.abs(tglInput.getTime()) <= Math.abs(tglLahir.getTime())){
			return umur;
		}	
		//
		Calendar calInput = new GregorianCalendar();
        Calendar calLahir = new GregorianCalendar();
        
//        tglInput=new Date(2006,3,2);
//		tglLahir=new Date(2005,12,19);
//		tglInput=new Date(1997,9,11);
//		tglLahir=new Date(1972,10,18);
//		calInput.set(2006+1900,3,3);
//        calLahir.set( 2003+1900,3,31);
              calInput.setTime(tglInput);
              calLahir.setTime(tglLahir);
		liHari1=calInput.get(Calendar.DATE);
		liBulan1=calInput.get(Calendar.MONTH);
		liTahun1=calInput.get(Calendar.YEAR);
		
//		logger.info(tglInput);
//		logger.info(tglLahir);
//		logger.info("++++++++++++++++++++");

//		liHari1=tglInput.getDate();
//		liBulan1=tglInput.getMonth();
//		liTahun1=tglInput.getYear();
		
//		logger.info(liHari1);
//		logger.info(liBulan1);
//		logger.info(liTahun1);
		
		
		liHari2=calLahir.get(Calendar.DATE);
		liBulan2=calLahir.get(Calendar.MONTH);
		liTahun2=calLahir.get(Calendar.YEAR);
//		liHari2=tglLahir.getDate();
//		liBulan2=tglLahir.getMonth();
//		liTahun2=tglLahir.getYear();
		
//		logger.info("*************");
//		logger.info(liHari2);
//		logger.info(liBulan2);
//		logger.info(liTahun2);
		
		liTahun3=liTahun1-liTahun2;
		liBulan3=liBulan1-liBulan2;
		liHari3=liHari1-liHari2;
//		logger.info("=========");
//		logger.info(liBulan3);
//		logger.info(liTahun3);
		
		if(liBulan1==1)
			liBulanTemp=12;
		else
			liBulanTemp=liBulan1-1;
		
		if(liBulan3 < 0){
			liBulan3 = 12 + liBulan3;
			liTahun3 = liTahun3 - 1;
			if(liHari3 < 0 ){
				liBulan3 = liBulan3 - 1;
				liHari3  = getJumlahHariPerBulan(liBulanTemp,liTahun1) + liHari3;
			}
		}else if( liBulan3 > 0){
			if(liHari3 < 0 ){
				liBulan3 = liBulan3 - 1;
				liHari3  = getJumlahHariPerBulan(liBulanTemp,liTahun1) + liHari3;
			}
		}else if(liBulan3 == 0){
			if(liHari3 < 0){
				liTahun3 = liTahun3 - 1;
				liBulan3 = 11;
				liHari3  = getJumlahHariPerBulan(liBulanTemp,liTahun1) + liHari3;
			}
		}
		umur=nf.format(liTahun3)+nf.format(liBulan3)+nf.format(liHari3);

		return umur;
		
	}
	/* Fungsi untuk menghitung jumlah hari pada bulan dan tahun tertentu
	 * 
	 * @param awal
	 *            int bulan
	 * @param akhir
	 * 			  int tahun
	 * @return hasil
	 * Misalnya :	bulan=2
	 * 				akhir=2006
	 * 				jumlah hari dalam bulan  Februari tahun 2006 adalah 28		
	 * 		
     *
	 */
	public static int getJumlahHariPerBulan(int bulan,int tahun){
		int hari=0;
		int jumHariperBulan[]={31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		
		if(bulan > 0 && bulan <= 12){
			hari= jumHariperBulan[bulan-1];
		}
		if(bulan == 2){
			if(tahun%4 ==0 )
				if(tahun%100 ==0){
					if(tahun%400 == 0)
						hari=29;
				}else
					hari=29;
		}
				
		return hari;
	}

	
	public static void main(String args[]){
//		Calendar a = new GregorianCalendar(2004, 10, 10, 5, 23, 12);
//		Calendar b = new GregorianCalendar(2004, 10, 10, 1, 1, 1);
		
		//logger.info(dateDifference(a.getTime(), b.getTime(), true));
		
		
		Date tglInput ;
//		Date tglLahir;
		
//		tglInput.setYear(2006);
//		tglInput.setMonth(3);
//		tglInput.setDate(2);
//		tglLahir.setYear(2005);
//		tglLahir.setMonth(12);
//		tglLahir.setDate(19);
		tglInput=new Date(2009,4,29);
//		tglLahir=new Date(2005,12,19);
//		tglInput=new Date(1997,9,11);
//		tglLahir=new Date(1972,10,18);
//		tglInput=new Date(2006,3,3);
//		tglLahir=new Date(2003,3,31);
		
		logger.info(getDayInWeekIndonesia(tglInput));
//		logger.info(getUmur(tglInput,tglLahir));
//		String lsLlapse="123456";
//		int liTahun,liBulan,liHari;
//		liTahun = Integer.parseInt(lsLlapse.substring(0,2));
//		liBulan= Integer.parseInt(lsLlapse.substring(2,4));
//		liHari= Integer.parseInt(lsLlapse.substring(4,6));
//		logger.info(liTahun+ " " +liBulan+" "+liHari);

//		int i=2;
//		if(i<3)
//			logger.info("ok");
//		else if(i>3)
//			logger.info("ok");
//		else if(i==2)
//			logger.info("ok");
			
//		Map map=getDay(496);
	}
	
	public static String getDayInWeekIndonesia(Date tgl){
		if (tgl == null) return "";
		SimpleDateFormat sdf= new SimpleDateFormat("EEE");
		String temp = sdf.format(tgl).toUpperCase();
//		logger.info(temp);
		return 	temp.equals("SUN")?"Minggu":
				temp.equals("MON")?"Senin":
				temp.equals("TUE")?"Selasa":
				temp.equals("WED")?"Rabu":
				temp.equals("THU")?"Kamis":
				temp.equals("FRI")?"Jumat":
				temp.equals("SAT")?"Sabtu":"";
				
	}
	public static String getDay(Date tgl){
		SimpleDateFormat sdf=new SimpleDateFormat("DD");
		return sdf.format(tgl);
	}
	
	public static String getDay2(Date tgl){
		SimpleDateFormat sdf=new SimpleDateFormat("dd");
		return sdf.format(tgl);
	}

	public static String getMonth(Date tgl){
		SimpleDateFormat sdf=new SimpleDateFormat("MM");
		return sdf.format(tgl);
	}

	public static String getYearTwoDigit(Date tgl){
		SimpleDateFormat sdf=new SimpleDateFormat("yy");
		return sdf.format(tgl);
	}

	public static String getYearFourDigit(Date tgl){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
		return sdf.format(tgl);
	}
	
	/**Fungsi : Untuk Menampilkan banyaknya tahun,bulan,hari dalam hari yang diinginkan.
	 * 			Mis: 496 hari menjadi 1 tahun, 4 bulan, 16 hari
	 * 			dimana pembulan 1 bulan=30 hari, 1 tahun=12 bulan.
	 * 
	 * @param count
	 * @return
	 * @created by : Ferry Harlim (08/02/2008)
	 */
	public static Map getDay(Integer count){
		Map<String, Integer> map=new HashMap();
		double oneMonth=30;
		Double  month,year = 0.0,day;
		double temp;
		temp=count/oneMonth;
		String result=String.valueOf(temp);
		String arr[]=new String[2];
		result=result.replace(".", ";");
		arr=result.split(";");
		month=new Double(Integer.parseInt(arr[0]));
		day=Double.parseDouble("0."+arr[1]);
		day=day*oneMonth;
		/////
		if(month>=12){
			temp=month/12;
			result=String.valueOf(temp);
			result=result.replace(".", ";");
			arr=result.split(";");
			year=new Double(Integer.parseInt(arr[0]));
			month=month-(year*12);
		}
		map.put("year", year.intValue());
		map.put("month", month.intValue());
		map.put("day", (int)Math.round(day));
		return map;
	}

    /**
     * author: Samuel
     * @param date: tanggal yang diinput
     * @return date, yaitu tanggal hari pertama dari bulan
     */
    public static Date getFirstDayOfMonth( Date date )
    {
        SimpleDateFormat awalBulanFormat = new SimpleDateFormat( "01/" + "MM/yyyy" );
        String awalBulanStr = awalBulanFormat.format( date );
        return FormatDate.toDateWithSlash( awalBulanStr );
    }

    /**
     * author: Samuel
     * @param date: tanggal yang diinput
     * @return tanggal dari date, hanya saja waktunya di set ke 23:59:59
     */
    public static Date getDateInLastSecond( Date date )
    {
        SimpleDateFormat dayFormat = new SimpleDateFormat( "dd/MM/yyyy" );
        String dayStr = dayFormat.format( date );

        int yy,mm,dd;
		dd = Integer.parseInt( dayStr.substring( 0, 2 ) );
		mm = Integer.parseInt( dayStr.substring( 3, 5 ) );
		yy = Integer.parseInt( dayStr.substring( 6, 10 ) );
		Calendar cal = new GregorianCalendar( yy, mm - 1, dd, 23, 59, 59 );

		return cal.getTime();
    }

    /**
     * author: Samuel
     * @param date: tanggal yang diinput
     * @return tanggal dari date, hanya saja waktunya di set ke 00:00:00
     */
    public static Date getDateInFirstSecond( Date date )
    {
        SimpleDateFormat dayFormat = new SimpleDateFormat( "dd/MM/yyyy" );
        String dayStr = dayFormat.format( date );

        int yy,mm,dd;
		dd = Integer.parseInt( dayStr.substring( 0, 2 ) );
		mm = Integer.parseInt( dayStr.substring( 3, 5 ) );
		yy = Integer.parseInt( dayStr.substring( 6, 10 ) );
		Calendar cal = new GregorianCalendar( yy, mm - 1, dd, 0, 0, 0 );

		return cal.getTime();
    }

    /**
     *
     * @param tanggal
     * @return  String dengan format dd/MM/yyyy HH:mm:ss
     */
    public static String toStampString(Date tanggal) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return df.format(tanggal);
	}

    /**
     *
     * @param tanggal
     * @return Date dimana input string tangal berformat dd/MM/yyyy HH:mm:ss
     */
    public static Date toStampDate( String tanggal ) {
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date result = null;

        try
        {
            result = df.parse( tanggal );
        }
        catch( ParseException e )
        {
            logger.error("ERROR :", e);
        }

        return result;
	}
    
    public static String periodeFormatDate( String periode ){
    	String tahun = periode.substring(0,4);
    	String bulan = periode.substring(4,6);
    	String bulanIndo;
    	String result;
    	if("01".equals(bulan)) bulanIndo = "januari";
    	else if("02".equals(bulan)) bulanIndo = "febuari";
    	else if("03".equals(bulan)) bulanIndo = "maret";
    	else if("04".equals(bulan)) bulanIndo = "april";
    	else if("05".equals(bulan)) bulanIndo = "mei";
    	else if("06".equals(bulan)) bulanIndo = "juni";
    	else if("07".equals(bulan)) bulanIndo = "juli";
    	else if("08".equals(bulan)) bulanIndo = "agustus";
    	else if("09".equals(bulan)) bulanIndo = "september";
    	else if("10".equals(bulan)) bulanIndo = "oktober";
    	else if("11".equals(bulan)) bulanIndo = "november";
    	else bulanIndo = "desember";
    	result = bulanIndo+ " " + tahun;
    	return  result;
    }
    
    public static String periodeFormatDate2( String periode ){
    	String tahun = periode.substring(0,4);
    	String bulan = periode.substring(4,6);
    	String bulanIndo;
    	String result;
    	if("01".equals(bulan)) bulanIndo = "januari";
    	else if("02".equals(bulan)) bulanIndo = "febuari";
    	else if("03".equals(bulan)) bulanIndo = "maret";
    	else if("04".equals(bulan)) bulanIndo = "april";
    	else if("05".equals(bulan)) bulanIndo = "mei";
    	else if("06".equals(bulan)) bulanIndo = "juni";
    	else if("07".equals(bulan)) bulanIndo = "juli";
    	else if("08".equals(bulan)) bulanIndo = "agustus";
    	else if("09".equals(bulan)) bulanIndo = "september";
    	else if("10".equals(bulan)) bulanIndo = "oktober";
    	else if("11".equals(bulan)) bulanIndo = "november";
    	else bulanIndo = "desember";
    	result = bulanIndo;
    	return  result;
    }
}
