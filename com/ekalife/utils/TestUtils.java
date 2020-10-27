package com.ekalife.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.List;
import common.Logger;

import com.ekalife.elions.model.DetilInvestasi;

/**
 * Class buat testing2
 * 
 * @author Yusuf
 * @since May 12, 2008 (8:09:15 AM)
 */
public class TestUtils {
	static Logger logger = Logger.getLogger(TestUtils.class);
	
	public static void main(String[] args) {
        try {
            Runtime rt = Runtime.getRuntime();
            /*
    		7z -psecret -mem=AES256 a 20090928.zip .\test\*
            -p 			= password
            -m 			= metode enkripsi
             a 			= add to archive
             *.zip 		= nama file zip hasilnya
            .\test\* 	= semua yg ada di subfolder test
            */
            Process pr = rt.exec("D:\\20090928\\7z -psecret -mem=AES256 a D:\\20090928\\20090928.zip D:\\20090928\\test\\*");

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;
            while((line=input.readLine()) != null) {
                logger.info(line);
            }

            /*
            0 No error 
            1 Warning (Non fatal error(s)). For example, one or more files were locked by some other application, so they were not compressed. 
            2 Fatal error 
            7 Command line error 
            8 Not enough memory for operation 
            255 User stopped the process 
            */
            int exitVal = pr.waitFor();
            logger.info("Exited with error code "+exitVal);

        } catch(Exception e) {
            logger.info(e.toString());
            logger.error("ERROR :", e);
        }
    }
	
	private static void cetak(NumberFormat nf, String deskripsi, Double value) {
		logger.info(deskripsi + " = " + (value == null ? "" : nf.format(value)));
	}
	
	/**
	 * Untuk testing perhitungan reas di ReasIndividu
	 * 
	 * @author Yusuf
	 * @since May 15, 2008 (9:09:26 AM)
	 * @param liCnt
	 * @param ldecReasLf
	 * @param ldecReasPa
	 * @param ldecReasPk
	 * @param ldecReasSs
	 * @param ldecReasCp
	 */
	public static void printDetailReas(Double[][][] ldecReasLf, Double[][][] ldecReasPa, Double[][][] ldecReasPk, Double[][][] ldecReasSs, Double[][][] ldecReasCp) {
		//Deklarasi variabel Array
		//x = liCnt[1] -> 1=inssured ; 2=policy-holder
		//y = liCnt[2] -> 1=total-sar-simultan ; 2=tsi ; 3=resiko-awal ; 4=retensi ; 5=sum-reas
		//ldecReasLf [x][y][1] = term
		//ldecReasLf [x][y][1] = life
		//ldecReasPa [x][y][1] = ssp
		//ldecReasPa [x][y][2] = pa include (risk-a)
		//ldecReasPa [x][y][3] = pa rider
		//ldecReasPk [x][y][1] = pk include
		//ldecReasPk [x][y][2] = pk rider
		//ldecReasSs [x][y][1] = ssh,ss,ss+
		//ldecReasCp [x][y][1] = cash plan
		
		logger.info("");
		
		NumberFormat nf = NumberFormat.getInstance();
		String[] pptt = {"", "TERTANGGUNG", "PEMEGANG"};
		String[] jenis = {"", 
				"TOTAL-SAR-SIMULTAN", 
				"TSI               ", 
				"RESIKO-AWAL       ", 
				"RETENSI           ", 
				"SUM-REAS          "};
		
		//for(int i=1;i<=2;i++){	// 1=inssured ; 2=policy-holder
		for(int i=1;i<=1;i++){	// 1=inssured ; 2=policy-holder
			//logger.info(pptt[i]);
			for(int j=1; j<=5; j++) {// 1=total-sar-simultan ; 2=tsi ; 3=resiko-awal ; 4=retensi ; 5=sum-reas
				//cetak(nf, jenis[liCnt[2]], ldecReasLf [liCnt[1]] [liCnt[2]] [1]); //term
				//cetak(nf, jenis[liCnt[2]], ldecReasLf [liCnt[1]] [liCnt[2]] [2]);  //life
				cetak(nf, "SSP    " + jenis[j] , ldecReasPa [i] [j] [1]);  //ssp
				//cetak(nf, jenis[liCnt[2]], ldecReasPk [liCnt[1]] [liCnt[2]] [1]);  //pk include
				//cetak(nf, jenis[liCnt[2]], ldecReasPk [liCnt[1]] [liCnt[2]] [2]);  //pk rider
				//cetak(nf, jenis[liCnt[2]], ldecReasSs [liCnt[1]] [liCnt[2]] [1]);  //ssh,ss,ss+
				//cetak(nf, jenis[liCnt[2]], ldecReasCp [liCnt[1]] [liCnt[2]] [1]);  //cash plan
			}
			for(int j=1; j<=5; j++) {
				cetak(nf, "PA INC " + jenis[j], ldecReasPa [i] [j] [2]);  //pa include (risk-a)
			}
			for(int j=1; j<=5; j++) {
				cetak(nf, "PA RDR " + jenis[j], ldecReasPa [i] [j] [3]);  //pa rider
			}
		}		
	}
	
	/**
	 * Buat testing di Halaman Input Spaj (BAC)
	 * 
	 * @author Yusuf
	 * @since May 15, 2008 (9:02:42 AM)
	 * @param daftar
	 */
	public static void printDetailInvestasi(List daftar) {
		logger.info("================================================================");
		for(int y=0; y<daftar.size(); y++) {
			DetilInvestasi di = (DetilInvestasi) daftar.get(y);
			if(di.getMdu_persen1() != null) {
				if(di.getMdu_persen1() > 0) {
					logger.info("REG_SPAJ = " + di.getReg_spaj1());
					logger.info("MU_KE1 = " + di.getMu_ke1());
					logger.info("LJI_ID = " + di.getLji_id1());
					logger.info("LJI_INVEST = " + di.getLji_invest1());
					logger.info("MDU_JUMLAH1 = " + di.getMdu_jumlah1());
					logger.info("MDU_JUMLAH_TOP = " + di.getMdu_jumlah_top());
					logger.info("MDU_JUMLAH_TOP_TUNGGAL = " + di.getMdu_jumlah_top_tunggal());
					logger.info("MDU_PERSEN1 = " + di.getMdu_persen1());
					logger.info("================================================================");
				}
			}
		}
	}
	
}