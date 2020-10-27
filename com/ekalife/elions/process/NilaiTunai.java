package com.ekalife.elions.process;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.dao.DataAccessException;
import org.springframework.util.StringUtils;

import com.ekalife.elions.model.Nilai;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class yang digunakan untuk menarik nilai tunai / tahapan / bonus / saving / deviden / maturity pada semua jenis produk
 * - Nilai2 tersebut disimpan di EKA.MST_NILAI dengan JENIS :
 * 	0 - nilai tunai
 * 	1 - tahapan
 * 	2 - bonus
 * 	3 - saving
 * 	4 - deviden
 * 	5 - maturity
 * @author Yusuf
 * @since 14 Juli 2006
 */
public class NilaiTunai extends ParentDao {
	protected static final Log logger = LogFactory.getLog( NilaiTunai.class );
	/**
	 * Proses Utama Nilai Tunai, akses ini saja untuk perhitungan / akses nilai tunai 
	 */
	public List<Nilai> proses(String spaj, String lus_id, String jenis, int flag_proses, int flag_fix) throws DataAccessException {
		Map params = prepareParameter(spaj, lus_id);
		
		if("cetak".equalsIgnoreCase(jenis)) { //cetak nilai tunai, kalo belom dihitung, hitung dulu, insert, baru cetak (manfaat polis)

			List<Nilai> daftarNilai = uwDao.selectNilai(spaj);
			if(daftarNilai.isEmpty()) {
				daftarNilai = hitungNilai(params);
				uwDao.insertNilai(daftarNilai, lus_id, flag_proses, flag_fix);
				daftarNilai = uwDao.selectNilai(spaj);
			}
			return cetakNilai(params, daftarNilai);
			
		}else if("simpan".equalsIgnoreCase(jenis)) { //hapus hasil perhitungan lama, hitung lagi, insert ulang
			
			uwDao.deleteNilai(spaj, null);
			List<Nilai> daftarNilai = hitungNilai(params);
			uwDao.insertNilai(daftarNilai, lus_id, flag_proses, flag_fix);
			daftarNilai = uwDao.selectNilai(spaj);
			return daftarNilai;
			
		}else if("tampil".equalsIgnoreCase(jenis)) { //tampilkan nilai tunai, kalo belom dihitung, hitung dulu, insert, baru tampilkan
			
			List<Nilai> daftarNilai = uwDao.selectNilai(spaj);
			if(daftarNilai.isEmpty()) {
				daftarNilai = hitungNilai(params);
				uwDao.insertNilai(daftarNilai, lus_id, flag_proses, flag_fix);
				daftarNilai = uwDao.selectNilai(spaj);
			}
			return daftarNilai;
			
		}
		
		return null;
	}
	
	/**
	 * Proses untuk insert semua nuilai tunai yang lama2, disini flag_proses = 1
	 * @author Yusuf Sutarko
	 * @since Mar 13, 2008 (7:07:11 PM)
	 * @param daftarEspeaje
	 * @param lus_id
	 * @param flag_proses
	 * @param flag_fix
	 * @return
	 * @throws DataAccessException
	 */
	public boolean prosesNilaiLama(List<String> daftarEspeaje, String lus_id, int flag_fix) throws DataAccessException{
		
		for(String spaj : daftarEspeaje) {
			// delete dulu, terserah mau pake apa gak
			commonDao.delete("DELETE FROM EKA.MST_NILAI WHERE REG_SPAJ = RPAD ('"+spaj+"', 11, ' ')");
			
			//int flag = commonDao.selectApakahSudahProsesNilaiTunai(spaj);
			//if(flag == 0) { //apabila belum pernah proses
			Map params = prepareParameter(spaj, lus_id);
			List daftarNilai = hitungNilai(params);
			uwDao.insertNilai(daftarNilai, lus_id, 1, flag_fix);
			//}
		}
		return true;
	}
	
	/**
	 * Proses ini mem-format nilai-nilai menjadi bentuk untuk di Print Manfaat
	 */
	private List<Nilai> cetakNilai(Map params, List<Nilai> daftar) throws DataAccessException {
		
		String lsbs_id 		= (String) params.get("lsbs_id");
		int lsdbs_number 	= ((Integer) params.get("lsdbs_number"));
		int ins_period 		= ((Integer) params.get("mspo_ins_period"));
		int umur 			= ((Integer) params.get("umur"));
		double mspr_tsi 	= ((Double) params.get("mspr_tsi"));

		int baris = -1, kolom=-1;
		String[] judul = null;
		
		if("020,026,027".indexOf(lsbs_id)>-1) {
			if(lsdbs_number==1) {baris = 2; kolom = 10;
			}else if(lsdbs_number==2) {baris = 3; kolom = 15;
			}else if(lsdbs_number==3) {baris = 4; kolom = 20;}
			judul = new String[] {"Akhir Tahun", "Nilai Tunai", "Tahapan"};
		}else if("023".indexOf(lsbs_id)>-1) {
			baris = 4; kolom = 18;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai", "Tahapan"};
		}else if("021,022,028,032,034,036,039,040,060,088,089,090,145,146,147,149,163, 185".indexOf(lsbs_id)>-1) {
			baris = 4; kolom = 20;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai"};
		}else if("029, 035, 048, 057".indexOf(lsbs_id)>-1) {
			baris = 4; kolom = 20;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai", "Tahapan"};
		}else if("031,033,070,071,072,172".indexOf(lsbs_id)>-1) {
			Double brs = new Double(ins_period-1)/new Double(6);
			Double brs2 = FormatNumber.round(brs,0);
			//baris = (ins_period - 1)/6;
			baris = ((Double) brs2).intValue();	
			if(baris%6>0) baris++;
			kolom = ins_period - 1;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai", "Tahapan"};
		}else if("046".equals(lsbs_id)) {
			baris = 3; kolom = 15;
			String apaSihApaDong = "Akhir Tahun";
			Map m = this.uwDao.selectInfoBonusProduk(Integer.valueOf(lsbs_id));
			BigDecimal tunai = (BigDecimal) m.get("LSBS_TUNAI");
			BigDecimal tahapan = (BigDecimal) m.get("LSBS_TAHAPAN");
			BigDecimal bonus = (BigDecimal) m.get("LSBS_BONUS");
			if(tunai==null || tahapan==null || bonus==null) throw new RuntimeException("Informasi Tunai, Tahapan, dan Bonus tidak lengkap");
			if(tunai.intValue()==1) apaSihApaDong+=",Nilai Tunai";
			if(tahapan.intValue()==1) apaSihApaDong+=",Tahapan";
			if(bonus.intValue()==1) apaSihApaDong+=",Bonus";
			judul = StringUtils.commaDelimitedListToStringArray(apaSihApaDong);
		}else if("051,052,061,062,078,085,180,181".indexOf(lsbs_id)>-1) {
			baris = 2; 
			kolom = 10;
			if("085".equals(lsbs_id)) {
				baris = 3;
				kolom = 12;
			}else if("181".equals(lsbs_id)){
				baris = 3;
				kolom = 12;
			}
			judul = new String[] {"Akhir Tahun", "Nilai Tunai", "Tahapan"};			
		}else if("047,049,055,058,083,093,104,108,110,125".indexOf(lsbs_id)>-1) {
			baris = 1; kolom = 5;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai"};
		}else if("112,126".indexOf(lsbs_id)>-1) {
			baris=2; kolom=8;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai", "Bonus Tahapan *)"};
		}else if("095,098".indexOf(lsbs_id)>-1) {
			baris=2; kolom=8;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai"};
		}else if("056,064,068,075".indexOf(lsbs_id)>-1) {
			baris=1; kolom=3;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai"};
		}else if("063,173".indexOf(lsbs_id)>-1) {
			if("063".indexOf(lsbs_id)>-1){
				baris = ins_period/5;
				if(baris>5)baris=5;
				kolom = ins_period;
				if(kolom>24) kolom=24;
			}else if("173".indexOf(lsbs_id)>-1){
				if(ins_period%5>0){
					baris = (ins_period/5) ;
					kolom = ins_period-1;
					if(kolom>25) kolom=25;
				}
			}
			
			//if(ins_period%5>0) baris++;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai", "Tahapan"};
		}else if("065".equals(lsbs_id)) {
			baris=2; kolom=10;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai"};
		}else if("066".equals(lsbs_id)) {
			baris = ins_period / 6;
			if(ins_period % 6 > 0) baris++;
			if(baris>4) baris=4;
			kolom = ins_period;
			if(kolom>20) kolom=20;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai"};
		}else if("069,082".indexOf(lsbs_id)>-1) {
			int li_i=0;
			if(lsdbs_number==1) {li_i=5;
			}else if(lsdbs_number==2) {li_i=7;
			}else if(lsdbs_number==3) {li_i=9;}
			baris = li_i/6;
			if(li_i%6>0)baris++;
			kolom=li_i;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai", "Tahapan"};
		}else if("103,114,137".indexOf(lsbs_id)>-1) {
			//maxi invest yg baru
			if((lsbs_id.equals("114") && (lsdbs_number == 2 || lsdbs_number == 3 || lsdbs_number == 4)) || 
					(lsbs_id.equals("137") && (lsdbs_number == 3 || lsdbs_number == 5))){
				baris=1; kolom=5;
				judul = new String[] {"Akhir Tahun", "Nilai Tunai", "Bonus Tahapan"};				
			}else{
				baris=1; kolom=5;
				judul = new String[] {"Akhir Tahun", "Nilai Tunai"};
			}
		}else if("111".equals(lsbs_id) && lsdbs_number==1) {
			baris=1; kolom=5;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai", "Tahapan"};
		}else if("133".equals(lsbs_id)) {
			baris=1; kolom=4;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai"};
		}else if(products.multiInvest(lsbs_id) || "111".equals(lsbs_id) && (lsdbs_number==2 || lsdbs_number==3)) {
			baris=2; kolom=8;
			judul = new String[] {"Akhir Tahun Polis Ke :", "Nilai Tunai", "Tahapan"};
		}else if("079,091".indexOf(lsbs_id)>-1) {
			baris = ins_period / 6;
			if(ins_period % 6>0)baris++;
			if(baris > 5) baris =5;
			kolom = ins_period;
			if(kolom>24) kolom=24;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai"};
		}else if("150, 151".indexOf(lsbs_id)>-1) {
			baris=1; kolom=4;
			judul = new String[] {"Akhir Tahun", "Nilai Tunai *)"};
		}else if("157".equals(lsbs_id)) {
			baris=4; kolom=20;
			judul = new String[] {"Usia Peserta", "Akhir Tahun", "Nilai Tunai"};
		}else if("167".equals(lsbs_id)) {
			baris=4; kolom=20;
			judul = new String[] {"Usia", "Nilai Tunai"};
		}else if("168".equals(lsbs_id)) {
			if(ins_period%5>0){
				baris = (ins_period/5)+1;
			}else {
				baris = (ins_period/5);
			}
			
			kolom = ins_period+1;
			if(kolom>65) kolom=65;
//			baris=5; kolom=25;
			judul = new String[] {"Usia", "Nilai Tunai Paket Dana Kehidupan", "Nilai Tunai Paket HCP"};

			//khusus end-care, berikut cara penampilannya
			//14 tahun pertama, diprint out nilai tunainya
			//tahun ke 15, di print out nilai tunai saat umur nasabah umur 55 (100%)
			//tahun ke 16 - 25, di print out nilai tunai bercabang yang Paket Dana Kehidupan dan Paket HCP
//			List<Nilai> tmp = daftar.subList(0, 14);
//			tmp.add(daftar.get(daftar.size()-1));
//			daftar = tmp;
		}else if("169,210".indexOf(lsbs_id)>=0){
			if(lsdbs_number==1 || lsdbs_number==5 || lsdbs_number==23  || lsdbs_number==34) {baris=1; kolom=5;}
			else if(lsdbs_number==2 || lsdbs_number==6 || lsdbs_number==24 || lsdbs_number==35){baris=2; kolom=10;}
			else if(lsdbs_number==3 || lsdbs_number==7){baris=3; kolom=15;}
			else {baris=4; kolom=20;}
			judul = new String[] {"Usia", "Nilai Tunai"};
			if(lsbs_id.equals("210"))judul = new String[] {"Akhir Tahun Polis ke-","Usia", "Nilai Tunai"};
		}else if("178".indexOf(lsbs_id)>-1){
			if((lsdbs_number>=1 && lsdbs_number<=8) || (lsdbs_number>=17 && lsdbs_number<=24) || (lsdbs_number>=33 && lsdbs_number<=40)|| (lsdbs_number>=49 && lsdbs_number<=65)) {baris=1; kolom=5;}
			else if((lsdbs_number>=9 && lsdbs_number<=16) || (lsdbs_number>=25 && lsdbs_number<=32) || (lsdbs_number>=41 && lsdbs_number<=48)) {baris=2; kolom=10;}
			judul = new String[] {"Akhir Tahun Polis ke-", "Nilai Tunai","Tahapan"};
		}else if("208".indexOf(lsbs_id)>-1){
			if (lsdbs_number>=5 && lsdbs_number<=28){
				baris=1; 
				kolom=5;
				judul = new String[] {"Usia ke-", "Dana Belajar","Tahapan"};
			}else{
				baris=1; 
				kolom=5;
				judul = new String[] {"Usia ke-", "Dana Belajar","Manfaat Perlindungan"};
			}
		}else if("219".indexOf(lsbs_id)>-1){
				baris=1; 
				kolom=5;
				judul = new String[] {"Usia ke-", "Dana Belajar","Tahapan"};
		}
		else if("179".indexOf(lsbs_id)>-1) {
			baris = 4; kolom = 20;
			judul = new String[] {"Akhir Tahun Polis ke-", "Nilai Tunai", "Tahapan dan Bonus"};
		}else if("180".indexOf(lsbs_id)>-1) {
			if(lsdbs_number==1 ) {baris=2; kolom=19;}
			else if(lsdbs_number==2){baris=3; kolom=14;}
			else if(lsdbs_number==3){baris=4; kolom=19;}
			judul = new String[] {"Akhir Tahun Polis ke-", "Nilai Tunai", "Tahapan"};
		}
		
		List result = null;
		SortedMap m = null;

		if(judul!=null && baris!=-1 && kolom!=-1) {
			NumberFormat df = new DecimalFormat("#,##0;(#,##0)"); //format tanpa koma
			result = new ArrayList();
			Map<Integer, Double>[] nilai = listToMap(daftar);
			
			/* UNTUK NGECEK ISI DATANYA
			for(int i=0; i<nilai.length; i++){
				Map<Integer, Double> asdf = nilai[i];
				for(Iterator<Integer> iter = asdf.keySet().iterator(); iter.hasNext();){
					Integer tahun = iter.next();
					logger.info(tahun + " = " + twoDecimalNumberFormat.format(asdf.get(tahun)));
				}
			}*/
			
			int hitung=0;
			int tahun[] = new int[] {1,1,1,1,1};
			int umir=1;

			//Yusuf 30/04/09
			//nilai tunai ini, khusus produk end-care (168), dimana mulai umur 56-65 thn, nilai tunai-nya bisa bercabang
			//dalam arti nasabah boleh pilih pake dana kehidupan, atau pilih paket HCP, setiap jalur nilai tunai nya beda
//			double ntDK[] = {971.58, 869.31, 766.03, 661.58, 555.77, 448.51, 339.70, 229.06, 115.95, 0}; //Nilai Tunai Dana Kehidupan
//			double ntHCP[] = {71.58, 69.31, 66.03, 61.58, 55.77, 48.51, 39.70, 29.06, 15.95, 0}; //Nilai Tunai HCP
			double ntDK[] = {71.58, 69.31, 66.03, 61.58, 55.77, 48.51, 39.70, 29.06, 15.95, 0}; //Nilai Tunai Dana Kehidupan
			double ntHCP[] = {971.58, 869.31, 766.03, 661.58, 555.77, 448.51, 339.70, 229.06, 115.95, 0}; //Nilai Tunai HCP
			int konterDK = 0, konterHCP = 0;
			
			logger.info(judul.length);
			logger.info(baris);
			logger.info(hitung);
			
			for(int i=0; i<(baris*judul.length); i++) {
				m = new TreeMap();
				if(hitung==judul.length)hitung=0;
				
				if(judul[hitung].startsWith("Akhir Tahun")) {
					for(int j=1; j<=5; j++) {
						if(tahun[0]<=kolom) {
							m.put("KOL"+j, String.valueOf(tahun[0]++));
						}else m.put("KOL"+j, "");
					}
				}if(judul[hitung].startsWith("Usia ke-")) {
					for(int j=1; j<=5; j++) {
						if(tahun[0]<=kolom) {
							if(tahun[0]==1){
								m.put("KOL"+j, String.valueOf(6));
							}else if(tahun[0]==2){
								m.put("KOL"+j, String.valueOf(12));
							}else if(tahun[0]==3){
								m.put("KOL"+j, String.valueOf(15));
							}else if(tahun[0]==4){
								m.put("KOL"+j, String.valueOf(18));
							}else if(tahun[0]==5){
								m.put("KOL"+j, String.valueOf(22));
							}
							tahun[0]++;
						}else m.put("KOL"+j, "");
					}
				}else if(judul[hitung].startsWith("Dana Belajar")){
					for(int j=1; j<=5; j++) {
						if(tahun[1]<=kolom) {
							String bintang = "";
							Double tmp = (Double)nilai[0].get(new Integer(tahun[1]++));
							logger.info(tmp);
							if(tahun[1]==6 || tahun[1]==11) {
								m.put("KOL"+j, df.format((tmp!=null?tmp:0.)) + bintang.toString());
							}else {
								m.put("KOL"+j, df.format((tmp!=null?tmp:0.)));
							}
						}
					}
				}else if(judul[hitung].startsWith("Manfaat Perlindungan")){
					for(int j=1; j<=5; j++) {
						if(tahun[2]<=kolom) {
							Double tmp = mspr_tsi;
							tahun[2]++;
							m.put("KOL"+j, df.format((tmp!=null?tmp:0.)));
							
						}else m.put("KOL"+j, "");
					}
				}
				else if(judul[hitung].startsWith("Nilai Tunai")) {
					//apabila umur 49 kebawah, baru bisa, kalo gak dobel2
					for(int j=1; j<=5; j++) {
						if(tahun[1]<=kolom) {
							
							if("167".equals(lsbs_id) && umur <= 49) {
								if(i > 1) {
									String bintang = "";
									int umir2 = 49 + umir++;
									Double tmp = (Double) nilai[0].get(new Integer(umir2-umur));
									if(umir2 >= 55) bintang = " *)";
									else if(umir2 == 70) bintang = " **)";
									//logger.info("Umir2 = " + umir2 + ", Nilai = " + df.format((tmp!=null?tmp:0.)) + bintang);
									m.put("KOL"+j, df.format((tmp!=null?tmp:0.)) + bintang);
								}else {
									String bintang = "";
									int umir2 = umur + umir++;
									Double tmp = (Double)nilai[0].get(new Integer(umir2-umur));
									if(umir2 >= 55) bintang = " *)";
									else if(umir2 == 70) bintang = " **)";
									//logger.info("Umir2 = " + umir2 + ", Nilai = " + df.format((tmp!=null?tmp:0.)) + bintang);
									m.put("KOL"+j, df.format((tmp!=null?tmp:0.)) + bintang);
								}
							}else if("168".equals(lsbs_id)) {
								if((umur + tahun[4] - 1) > 56){ //diatas umur 54 tahun, hardcoding (55 100%, seterusnya rate hardcoding diatas)
//								if(umur  > 55){
									Double tmp = null;
									if(umur+tahun[1]==55){
										tmp = (Double)nilai[0].get(new Integer(tahun[1]++));
										m.put("KOL"+j, df.format((tmp!=null?tmp.doubleValue():new Double(0).doubleValue())));
									}else{
										if(tahun[1]<65){
											if(konterDK==10 && konterHCP==10){
												if(j==5){
													return result;
												}else{
													tahun[1]++;
													m.put("KOL"+j, "");
												}
											}else {
												if(judul[hitung].contains("Paket Dana Kehidupan")){
													if(konterDK!=10){
														tmp = ntDK[konterDK++]/1000 * mspr_tsi;
														tahun[1]++;
														m.put("KOL"+j, df.format((tmp!=null?tmp.doubleValue():new Double(0).doubleValue())));
													}else{
														for(int a=j;a<=5;a++){
															tahun[1]++;
															m.put("KOL"+a, "");
														}
														j=5;
													}
													
												}else if(judul[hitung].contains("Paket HCP")){
													if(konterHCP!=10){
														tmp = ntHCP[konterHCP++]/1000 * mspr_tsi;
														tahun[1]++;
														m.put("KOL"+j, df.format((tmp!=null?tmp.doubleValue():new Double(0).doubleValue())));
													}
												}
											}
											
										}else {
											tahun[1]++;
											m.put("KOL"+j, "");
										}
									}
									
								}else{
									Double tmp = (Double)nilai[0].get(new Integer(tahun[1]++));
									m.put("KOL"+j, df.format((tmp!=null?tmp.doubleValue():new Double(0).doubleValue())));
//									if((umur + tahun[4] - 5 + j - 1) == 55){ //usia ke 55, ambil yg ke 55
//									
//										tmp = ntDK[konterDK++]/1000 * mspr_tsi;
//										m.put("KOL"+j, df.format((tmp!=null?tmp.doubleValue():new Double(0).doubleValue())) + " *)");
//									}
									if((umur + tahun[1] - 5 + j - 1) == 56){
										if(judul[hitung].contains("Paket Dana Kehidupan")){
											tmp = ntDK[konterDK++]/1000 * mspr_tsi;
										}else if(judul[hitung].contains("Paket HCP")){
											tmp = ntHCP[konterHCP++]/1000 * mspr_tsi;
										}
										m.put("KOL"+j, df.format((tmp!=null?tmp.doubleValue():new Double(0).doubleValue())));
									}
								}
							}
							else if("178".equals(lsbs_id)){
								String bintang = "";
								Double tmp = (Double)nilai[0].get(new Integer(tahun[1]++));
								if( tahun[1]==11) {
									bintang = " *)";
									m.put("KOL"+j, df.format((tmp!=null?tmp:0.)) + bintang.toString());
									//m.put("KOL"+j, twoDecimalNumberFormat.format((tmp!=null?tmp.doubleValue():new Double(0).doubleValue())+ bintang.toString()));
								}else {
									m.put("KOL"+j, df.format((tmp!=null?tmp:0.)));
								}
							}
							else {
								Double tmp = (Double)nilai[0].get(new Integer(tahun[1]++));
								m.put("KOL"+j, twoDecimalNumberFormat.format((tmp!=null?tmp.doubleValue():new Double(0).doubleValue())));
							}
						}else m.put("KOL"+j, "");
						
						//khusus End-Care, Nilai Tunai Paket Dana Kehidupan dan Nilai Tunai Paket HCP 
						//adalah sama semua, sampai umur 55 thn
						//makanya ini di "undo" 5 tahun loopingnya
						//untuk yg umur 55 - 65, di hardcode di atas (cari kode 168)
						if("168".equals(lsbs_id) && judul[hitung].contains("Dana Kehidupan") && j==5) {
							tahun[1] -= 5;
						}
					}
				}else if(judul[hitung].startsWith("Tahapan")) {
					for(int j=1; j<=5; j++) {
						if(tahun[2]<=kolom) {
							Double tmp = (Double)nilai[1].get(new Integer(tahun[2]++));
							//randy
							if (lsbs_id.equals("208") && ((lsdbs_number>=5 && lsdbs_number<=12) || (lsdbs_number>=17 && lsdbs_number<=28))){
								int ctr[] = {0, 16, 10, 7, 4, 0};
								tmp = (Double)nilai[1].get(ins_period-ctr[j]);
							}else if (lsbs_id.equals("219")){
								int ctr[] = {0, 16, 10, 7, 4, 0};
								tmp = (Double)nilai[1].get(ins_period-ctr[j]);
							}
							logger.info(tmp);
							m.put("KOL"+j, twoDecimalNumberFormat.format((tmp!=null?tmp.doubleValue():new Double(0).doubleValue())));
						}else m.put("KOL"+j, "");
					}
				}else if("Bonus".equals(judul[hitung]) || judul[hitung].startsWith("Bonus Tahapan")) {
					for(int j=1; j<=5; j++) {
						if(tahun[3]<=kolom) {
							Double tmp = (Double)nilai[2].get(new Integer(tahun[3]++));
							m.put("KOL"+j, twoDecimalNumberFormat.format((tmp!=null?tmp.doubleValue():new Double(0).doubleValue())));
						}else m.put("KOL"+j, "");
					}
				}else if("Usia Peserta".equals(judul[hitung]) || "Usia".equals(judul[hitung])) {
					if(i > 0 && "167".equals(lsbs_id) && umur <= 49) { //apabila hidup bahagia, pada baris kedua, ada perubahan
						for(int j=1; j<=5; j++) {
							if(tahun[4]<=kolom) {
								Double tmp = new Double(49 + tahun[4]++);
								m.put("KOL"+j, df.format((tmp!=null?tmp:0)));
							}else m.put("KOL"+j, "");
						}
					}else {
						for(int j=1; j<=5; j++) {
							if("168".equals(lsbs_id)){
								Double tmp = new Double(umur + tahun[4]++);
								if(tmp.intValue()>65){
									m.put("KOL"+j, "");
								}else m.put("KOL"+j, df.format((tmp!=null?tmp:0)));
								
//								if(Integer.parseInt((String) m.get("KOL" + j)) == (55-umur)){
//									m.put("KOL"+j, "55");
//								}
								
								//if((tmp - umur) == 15) tahun[4] = (56-umur); //line ini sgt penting, untuk melongkap bila usia muda, karena 10 row terakhir harus usia 56-65
								
							}else if(tahun[4]<=kolom) {
								Double tmp = new Double(umur + tahun[4]++);
								if("167".equals(lsbs_id) && tmp.intValue() > 70) m.put("KOL"+j, "");
								else m.put("KOL"+j, df.format((tmp!=null?tmp:0)));

								//
							}else m.put("KOL"+j, "");
						}
					}
				}
				m.put("KOL0", judul[hitung++]);
				
				//Yusuf (5 Aug 09) - Tambahan persentase untuk 
				for(int j=1; j<=5; j++) {
					Double tmp = (Double)nilai[6].get(new Integer(j));
					m.put("PERSEN"+j, twoDecimalNumberFormat.format((tmp!=null?tmp.doubleValue():new Double(0).doubleValue())));
				}
				
				
				result.add(m);
				
			}
		}
		
		return result;
	}

	/**
	 * Proses ini menyiapkan perubahan2 parameter, seperti lscb_id, lsbs_id, etc... 
	 */
	private Map prepareParameter(String spaj, String lus_id) throws DataAccessException {
		
		Map info = uwDao.selectInfoCetakManfaat(spaj);
		
		String lsbs_id = FormatString.rpad("0", (String) info.get("lsbs_id"), 3);
		int lscb_id = ((Integer) info.get("lscb_id"));
		int pay_period = ((Integer) info.get("mspo_pay_period"));
		int ins_period = ((Integer) info.get("mspo_ins_period"));
		int lsdbs_number = ((Integer) info.get("lsdbs_number"));
		String lku_id = (String) info.get("lku_id");
		int umur;

		if(lsbs_id.equals("064")) lsbs_id = "056";
		else if(lsbs_id.equals("062")) lsbs_id = "052";
		else if(lsbs_id.equals("089")) lsbs_id = "060"; //Ultra Sejahtera PLB
		else if(lsbs_id.equals("091")) lsbs_id = "079";
		else if(("099, 135, 136").indexOf(lsbs_id) > -1) lsbs_id = "096";
		
		if(lsbs_id.equalsIgnoreCase("226") && (lsdbs_number >= 1 && lsdbs_number <= 5)){ //helpdesk [139867] produk baru Simas Legacy Plan (226-1~5)
			ins_period = 0;
		}
		
		if("1, 2, 4, 5, 6".indexOf(String.valueOf(lscb_id))>-1) lscb_id = 3;
		
		if("016, 031, 033, 070, 071, 072, 172".indexOf(lsbs_id)>-1) {
			umur = ((Integer) info.get("mspo_age"));
			if(lsbs_id.equals("031") && umur > 55) umur = 34;
		}else if("011".equals(lsbs_id)) {
			umur = ((Integer) info.get("mspo_umur_beasiswa"));
		}else {
			umur = ((Integer) info.get("mste_age"));
		}
		
		if("038".equals(lsbs_id) && lscb_id==0) {
			lscb_id = 3;
		}else if("027".equals(lsbs_id)) {
			if(lsdbs_number == 1) pay_period = 10;
			else if(lsdbs_number == 2) pay_period = 15;
			else if(lsdbs_number == 3) pay_period = 20;
		}
		
		//Ikuti Ketentuan nilai Tunai yg diinsert Pak Himmia, Maka cara bayar sekaligus untuk masa bayar diset per masing2 MPP nya.
		if("169".equals(lsbs_id) && lscb_id == 0){
			if(lsdbs_number == 5) pay_period =5;
			else if(lsdbs_number == 6) pay_period = 10;
			else if(lsdbs_number == 7) pay_period = 15;
			else if(lsdbs_number == 8) pay_period = 20;
			else if(lsdbs_number == 23 || lsdbs_number == 34) pay_period = 5;
			else if(lsdbs_number == 24 ||  lsdbs_number == 35) pay_period = 10;
		}
			
		
		ins_period = getLamaTanggung(ins_period, lsbs_id, lku_id);
		
		info.put("lsbs_id", lsbs_id);
		info.put("lsdbs_number", lsdbs_number);
		info.put("lscb_id", new Integer(lscb_id));
		info.put("mspo_pay_period", new Integer(pay_period));
		info.put("mspo_ins_period", new Integer(ins_period));
		info.put("umur", new Integer(umur));
		info.put("lus_id", lus_id);
	
		return info;
	}
	
	/**
	 * Proses ini menghitung semua nilai TUNAI, BONUS, TAHAPAN, SAVING, DEVIDEN, MATURITY
	 */
	private List<Nilai> hitungNilai(Map params) throws DataAccessException{
				
		//-------------- Tarik nilai yang sudah dihitung beserta rate dari tabel nilai tunai --------------//
		String lsbs_id = (String)params.get("lsbs_id");
		String spaj = (String) params.get("reg_spaj");
		int lsdbs_number = ((Integer) params.get("lsdbs_number")); 
		int umur1 = ((Integer) params.get("umur")); 
		
		List<Nilai> daftarNilai;		
		List<Nilai> daftarNilaiNew;
		if(products.multiInvest(lsbs_id) || (lsbs_id.equals("111") && (lsdbs_number==2 || lsdbs_number==3))) {					
				daftarNilai = uwDao.selectNilaiMultiInvest(params); //yang baru
			
		}else if(products.newNilaiTunai(lsbs_id)) {
			//yusuf - LST_NILAI, yang pake umur baru endowment 20, yang lain belum
			if(!lsbs_id.equals("157") && !lsbs_id.equals("178") && !lsbs_id.equals("212")) params.put("umur", 0);
				
			daftarNilai = uwDao.selectNilaiFromLstNilai(params); //yang baru
			if(lsbs_id.equals("208")){
				if ((lsdbs_number>=5 && lsdbs_number<= 28) || (lsdbs_number>=37 && lsdbs_number<= 44)){
					params.put("lscb_id", 0);
					params.put("umur", umur1);
					daftarNilai = uwDao.select208New(params);// SIMAS KID & SMILE KID & SMART PLAN PROTECTION
				}else if(lsdbs_number>=29 && lsdbs_number<= 32){
					params.put("lscb_id", 0);
					params.put("umur", umur1);
					daftarNilai = uwDao.select208New(params);// SMART KID DMTM
				}else if(lsdbs_number>=45 && lsdbs_number<= 48){
					params.put("lscb_id", 0);
					params.put("umur", umur1);
					daftarNilai = uwDao.select208New(params);// SMILE KID BJB
				}else{
					params.put("umur", 99);
					daftarNilai = uwDao.selectNilaiFromLstNilai208(params);//SMART KID
				}
			}else if(lsbs_id.equals("219")){
				params.put("lscb_id", 0);
				params.put("umur", umur1);
				daftarNilai = uwDao.select208New(params);// SIMAS KID SYARIAH
			}else if("223".equals(lsbs_id) || ("212".equals(lsbs_id) && (lsdbs_number==1 ||lsdbs_number==3 || lsdbs_number==6 || lsdbs_number==8 || lsdbs_number==9 || lsdbs_number==10 || lsdbs_number==12 || lsdbs_number==13 || lsdbs_number==14)) ){ // Nana Add 212-14
				params.put("lscb_id", 6);
				params.put("umur", umur1);
				if(("212".equals(lsbs_id) && (lsdbs_number==6 || lsdbs_number==9 || lsdbs_number==10 || lsdbs_number==14))){// Nana Add 212-14
					params.put("lsdbs_number", 8);
				}
				daftarNilai = uwDao.selectNilaiFromLstTableNew(params);
			}
		}else {
			if(lsbs_id.equals("167")) params.put("mspo_ins_period", 70); //yusuf - khusus hidup bahagia, tarik ratenya pukul rata pd umur 70
			if(lsbs_id.equals("168")) params.put("mspo_ins_period", 65);
			//if(lsbs_id.equals("169")) params.put("mspo_pay_period", 1); //Yusuf (23/12/2010) - pukul rata pay period = 1

			 // Andhika(28-06-2013) - lst_table_new
			if("163,167,169,172,173,179,180,181,185,210,212".indexOf(lsbs_id)>-1){// update sub 163 untuk yg aktif
				if( (lsbs_id.equalsIgnoreCase("163") && (lsdbs_number>=21 && lsdbs_number<=25)) ||
					(lsbs_id.equalsIgnoreCase("169")) || (lsbs_id.equalsIgnoreCase("180")) ||	(lsbs_id.equalsIgnoreCase("210")) || (lsbs_id.equalsIgnoreCase("212"))
					||(lsbs_id.equalsIgnoreCase("172")) || (lsbs_id.equalsIgnoreCase("173"))
					 	){
					daftarNilai = uwDao.selectNilaiFromLstTableNew(params);
				}else{
					params.put("mspo_ins_period", 0);
					daftarNilai = uwDao.selectNilaiFromLstTableNew(params);
				}
			}else{
				daftarNilai = uwDao.selectNilaiFromLstTable(params);
			}
		}
		
		//-------------- Special Cases --------------//
		
		//1. Simponi8 ada declare bonus under table di tahun ke 8
			if("112, 126".indexOf(lsbs_id)>-1) {
				double underTable = uwDao.selectNilaiUnderTable(spaj).doubleValue() * 10;
				setValue(daftarNilai, 7, "Bonus", underTable);
			}
		//2. Kalau Pro Investor (69,82), perhitungan nilai tunai dan bonus dibagi 3 lagi 
			//(lihat query elions.uw.selectNilaiFromLstTable)
		//3. Kalau Pro-Invest (42), UP nya diambil dari tabel eka.mst_investasi_plan_42 
			//(lihat query elions.uw.selectNilaiFromLstTable)
		//4. Untuk Simponi Dollar Rupiah (150,151) TAPI BUKAN SECURED INVEST DELUXE 
			//rate tahun ke-3 dan ke-4 bunganya disimpan di kolom mst_policy.mspo_under_table
			else if("150, 151".indexOf(lsbs_id)>-1 && lsdbs_number == 1) { //hanya simponi, bukan secured invest
				double persen=-1;
				if("150".equals(lsbs_id)) {
					persen = 0.08;
				}else if("151".equals(lsbs_id)) {
					persen = 0.02;
				}
				double rate3 = Math.pow((1 + (uwDao.selectNilaiUnderTable(spaj).doubleValue()/100)), 3) * 100;
				rate3 = FormatNumber.round(rate3, 2); //round 2
				double tsi3 = getValue(daftarNilai, 2, "Tsi").doubleValue();
				setValue(daftarNilai, 2, "Nilai_tunai", FormatNumber.round((tsi3 * rate3)/100, 2)); //round 2 
				
				double rate4 = rate3 * (1+persen);
				rate4 = FormatNumber.round(rate4, 2);
				double tsi4 = getValue(daftarNilai, 3, "Tsi").doubleValue();
				setValue(daftarNilai, 3, "Nilai_tunai", FormatNumber.round((tsi4 * rate4)/100,2)); 
			}
		//5. Untuk Investa (111), nilai tunai tahun ke-5 menjadi tahapan tahun ke-5, nilai tunai nya di set 0
			else if("111".equals(lsbs_id) && lsdbs_number==1) {
				exchangeValue(daftarNilai, 4, "Tahapan", "Nilai_tunai");
			}
		//6. Untuk Super Prolife 20, nilai tunai tahun ke-20 = 0
			else if("035".equals(lsbs_id)) {
				setValue(daftarNilai, 19, "Nilai_tunai", 0);
			}
		//7. Untuk Eka Simponi US $ (55), nilai tunai tahun pertama = 0
			else if("055".equals(lsbs_id)) {
				setValue(daftarNilai, 0, "Nilai_tunai", 0);
			}
		//8. Untuk Eka Simponi (83), nilai maturity di tahun ke-5 dihitung dari premi
			else if("083".equals(lsbs_id)) {
				setValue(daftarNilai, 4, "Maturity", getValue(daftarNilai, 4, "Mspr_premium").doubleValue() * 1.4);
			}
		//9. Untuk Warisan Sejahtera (88, 90), nilai tunai di akhir tahun polis dituker ke maturity
			else if("088, 090".indexOf(lsbs_id)>-1) {
				exchangeValue(daftarNilai, daftarNilai.size()-1, "Maturity", "Nilai_tunai");
		//10. Untuk Multi Invest (074,076,096,099,135,136), nilai tahapan tahun ke 5 = maturity tahun ke 5
			}else if(products.multiInvest(lsbs_id) ||( "111".equals(lsbs_id) && (lsdbs_number==2 || lsdbs_number==3)) ) {
				exchangeValue(daftarNilai, daftarNilai.size()-1, "Tahapan", "Maturity");
			}
		//10. Untuk Multi Invest (074,076,096,099,135,136), nilai maturity tahun ke 5 = tahapan tahun ke 5
//			else if(products.multiInvest(lsbs_id)) {
//				double manfaat = 0;
//				lsbs_id = uwDao.selectBusinessId(spaj); 
//				if(lsbs_id.equals("074")) {manfaat = 1.16;
//				}else if(lsbs_id.equals("076")) {manfaat = 1.12;
//				}else if("096,099".indexOf(lsbs_id)>-1) {manfaat = 2.8;
//				}else if("135,136".indexOf(lsbs_id)>-1) {manfaat = 2.9;}
//				
//				double tsi = getValue(daftarNilai, daftarNilai.size()-1, "Tsi").doubleValue();
//				double nilai = manfaat * tsi;
//				
//				if(daftarNilai.size()<5) {
//					Nilai temp = new Nilai();
//					temp.setReg_spaj(spaj);
//					temp.setTahun(new Integer(5));
//					temp.setTahapan(new Double(FormatNumber.round(nilai, 2)));
//					daftarNilai.add(temp);
//				}else {
//					setValue(daftarNilai, daftarNilai.size()-1, "Tahapan", FormatNumber.round(nilai, 2));
//				}
//				
//			}

		return daftarNilai;

	}

	/**
	 * Fungsi dibawah untuk merubah ins_period 
	 */
	public int getLamaTanggung(int ins_period, String lsbs_id, String lku_id) {
		lsbs_id = FormatString.rpad("0", lsbs_id, 3);
		if("027".equals(lsbs_id)) {
			ins_period = 99;
		}else if("012, 021, 022, 028, 029, 032, 034, 035, 036, 048".indexOf(lsbs_id)>-1) {
			ins_period = 99;
		}else if("039, 040, 057, 060, 145, 146, 147, 185".indexOf(lsbs_id)>-1) {
			ins_period = 79;
		}else if("168".equals(lsbs_id)) {
			//ins_period = 65;
			ins_period = ins_period;
		}else if("046".equals(lsbs_id)) {
			ins_period = 75;
		}else if("047, 049, 055, 058".indexOf(lsbs_id)>-1) {
			ins_period = 5;
		}else if("051, 052, 061, 062, 078".indexOf(lsbs_id)>-1) {
			if("01".equals(lku_id)) {ins_period = 59;}
			else {ins_period = 65;}
		}else if("056, 068, 075".indexOf(lsbs_id)>-1) {
			ins_period = 8;
		}
		return ins_period;
	}
	
	/**
	 * Fungsi untuk ambil nilai maturity
	 */
	public Double getNilaiMaturity(Integer businessId, String kurs, Integer cbId, Integer lamaBayar, Integer lamaTanggung, int umur, int tahun, int jenis)
	throws Exception {
		
		Double nilaiMatur = null;
		
		int lsbs = businessId;
		int il_cbid = cbId;
		if(il_cbid==1 || il_cbid==2) il_cbid=3;
		cbId = new Integer(il_cbid);		
		
		if(jenis==4 && umur<20) umur=20;
		
		nilaiMatur = this.uwDao.selectNilaiMaturity(jenis, lsbs, umur, kurs, cbId, lamaBayar, lamaTanggung, tahun);
		
		if(jenis==7) {
			if(lsbs==83) {
				return new Double(933.333);
			}
			else if(lsbs==88 || lsbs==90) {
				return new Double(1000);
			}
		}
		
		return nilaiMatur;
		
	}
	
	//fungsi2 dibawah tidak ada hubungannya dengan nilai-nilai, hanya untuk membantu saja
	
	private static Map<Integer, Double>[] listToMap(List daftar) {
		Map<Integer, Double>[] result = new Map[] { new HashMap(), new HashMap(), new HashMap(), new HashMap(), new HashMap(), new HashMap(), new HashMap()};
		for(int i=0; i<daftar.size(); i++) {
			Nilai nilai = (Nilai) daftar.get(i);
			if(nilai.getNilai_tunai().doubleValue() > 0) result[0].put(nilai.getTahun(), nilai.getNilai_tunai());
			if(nilai.getTahapan().doubleValue() > 0) result[1].put(nilai.getTahun(), nilai.getTahapan());
			if(nilai.getBonus().doubleValue() > 0) result[2].put(nilai.getTahun(), nilai.getBonus());
			if(nilai.getSaving().doubleValue() > 0) result[3].put(nilai.getTahun(), nilai.getSaving());
			if(nilai.getDeviden().doubleValue() > 0) result[4].put(nilai.getTahun(), nilai.getDeviden());
			if(nilai.getMaturity().doubleValue() > 0) result[5].put(nilai.getTahun(), nilai.getMaturity());
			if(nilai.getPersen().doubleValue() > 0) result[6].put(nilai.getTahun(), nilai.getPersen());
		}
		return result;
	}
	private static void setValue(List daftarNilai, int baris, String target, double jml) {
		Nilai nilai = (Nilai) daftarNilai.get(baris);
		try {
			Class[] parameterTypes = new Class[] {Double.class};
			Object[] arguments = new Object[] {new Double(jml)};
	        Method setMethod = nilai.getClass().getMethod("set"+target, parameterTypes);
	        setMethod.invoke(nilai, arguments);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		} catch (NoSuchMethodException e) {
			logger.error("ERROR :", e);
		} catch (InvocationTargetException e) {
			logger.error("ERROR :", e);
		}
	}
	private static Double getValue(List daftarNilai, int baris, String source) {
		Nilai nilai = (Nilai) daftarNilai.get(baris);
		Double hasil = null;
		try {
	        Method getMethod = nilai.getClass().getMethod("get"+source, (Class[]) null);
	        hasil = (Double) getMethod.invoke(nilai, (Object[]) null);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		} catch (NoSuchMethodException e) {
			logger.error("ERROR :", e);
		} catch (InvocationTargetException e) {
			logger.error("ERROR :", e);
		}
		return hasil!=null?hasil:new Double(0);
	}
	private static void exchangeValue(List daftarNilai, int baris, String target, String source) {
		Nilai nilai = (Nilai) daftarNilai.get(baris);
		try {
			Class[] parameterTypes = new Class[] {Double.class};
			Method getMethod = nilai.getClass().getMethod("get"+source, (Class[]) null);
			//tarik dulu nilai-nya dari source
			Object[] arguments = new Object[] {getMethod.invoke(nilai, (Object[]) null)};
			//masukkan nilai dari source ke target
	        Method setMethod = nilai.getClass().getMethod("set"+target, parameterTypes);
	        setMethod.invoke(nilai, arguments);
	        //reset nilai dari source jadi 0
	        Object[] arguments2 = new Object[] {new Double(0)};
	        Method setMethod2 = nilai.getClass().getMethod("set"+source, parameterTypes);
	        setMethod2.invoke(nilai, arguments2);
		} catch (IllegalAccessException e) {
			logger.error("ERROR :", e);
		} catch (NoSuchMethodException e) {
			logger.error("ERROR :", e);
		} catch (InvocationTargetException e) {
			logger.error("ERROR :", e);
		}
	}
	
}