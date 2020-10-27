package com.ekalife.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Class berisi fungsi-fungsi untuk angka, misalnya pembulatan
 * 
 * @author Yusuf
 * @since 02/20/2005
 */
public class FormatNumber implements Serializable {

	private static final long serialVersionUID = 8171862015966401783L;

	/**
	 * Fungsi untuk pembulatan per desimal angka (xx angka dibelakang koma) (contoh: <b>round(3.1235, 2) = 3.12</b>)
	 * @param number nilai yang ingin dibulatkan
	 * @param decimalPlace jumlah desimal dibelakang koma
	 * @return Hasil setelah dibulatkan
	 */	
	public static double round(double number, int decimalPlace) {
		BigDecimal bd = new BigDecimal(number);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	
	public static Double round2(Double number, Integer decimalPlace) {
		BigDecimal bd = new BigDecimal(number);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	
    public  static String convertToTwoDigit( BigDecimal value )
    {
        DecimalFormat decimalFormat = new DecimalFormat( "###,###,###,###,##0.00" );
        return decimalFormat.format( value );
    }

	/**
	 * Fungsi untuk pembulatan per angka (contohnya, 25) berdasarkan satuan yang dimasukkan 
	 * (contoh: <b>rounding(1378, true, 25) = 1400</b>)
	 * @param ad_nilai Nilai yang ingin dibulatkan
	 * @param up True apabila dibulatkan keatas, False apabila dibulatkan kebawah
	 * @param satuan pembulatan
	 * @return Hasil setelah dibulatkan
	 * @see Double
	 */	
	public static Double rounding(Double ad_nilai, boolean up, double satuan){
		double ad_sisa;
	
		ad_sisa = ad_nilai.doubleValue() % satuan;
	
		if(ad_sisa != 0 ){
			if(up) ad_nilai = new Double(ad_nilai.doubleValue() - ad_sisa + satuan);
			else ad_nilai = new Double(ad_nilai.doubleValue() - ad_sisa);
		}
		
		return ad_nilai;		
	}

//	public static void main(String[] args) {
//		NumberFormat nf = NumberFormat.getNumberInstance();
//		logger.info(nf.format(rounding((double) 34000000, false, 5000000)));
//		logger.info(nf.format(rounding((double) 29000000, false, 5000000)));
//		logger.info(nf.format(rounding((double) 24000000, false, 5000000)));
//		logger.info(nf.format(rounding((double) 31000000, false, 5000000)));
//	}
	
	
	public static String angkaRomawi(String nilai) {
		if(Integer.parseInt(nilai.replaceFirst("0", "")) == 1) return "I";
		else if(Integer.parseInt(nilai.replaceFirst("0", "")) == 2) return "II";
		else if(Integer.parseInt(nilai.replaceFirst("0", "")) == 3) return "III";
		else if(Integer.parseInt(nilai.replaceFirst("0", "")) == 4) return "IV";
		else if(Integer.parseInt(nilai.replaceFirst("0", "")) == 5) return "V";
		else if(Integer.parseInt(nilai.replaceFirst("0", "")) == 6) return "VI";
		else if(Integer.parseInt(nilai.replaceFirst("0", "")) == 7) return "VII";
		else if(Integer.parseInt(nilai.replaceFirst("0", "")) == 8) return "VIII";
		else if(Integer.parseInt(nilai.replaceFirst("0", "")) == 9) return "IX";
		else if(Integer.parseInt(nilai.replaceFirst("0", "")) == 10) return "X";
		else if(Integer.parseInt(nilai.replaceFirst("0", "")) == 11) return "XI";
		else if(Integer.parseInt(nilai.replaceFirst("0", "")) == 12) return "XII";
		
		return null;
	}
}
