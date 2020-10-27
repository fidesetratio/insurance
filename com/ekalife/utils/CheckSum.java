package com.ekalife.utils;

import java.util.Date;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 
 * @author Yusup Andri
 * @since 6 Juli 2010
 *
 */
public class CheckSum {
	protected final static Log logger = LogFactory.getLog( CheckSum.class );

	/*
	 * Info Tambahan (Yusuf S)
	 * Checksum1 = untuk PIN produk Perdana (8 digit)
	 * Checksum2 = untuk PIN produk Single (8 digit)
	 * Checksum3 = untuk PIN produk Ceria (8 digit)
	 * Checksum4 = untuk PIN produk Ideal (8 digit)
	 * Checksum5 = untuk No Kartu distribusi Agency (12 digit)
	 * Checksum6 = untuk No Kartu distribusi DM/TM (12 digit)
	 * */
	
	/* param :
	 * return : String
	 * 
	 * Checksum1 :
	 * 1. juml 4 digit pertama di mod 3 sisa nya 1  
	 * 2. juml 4 digit terakhir merupakan kelipatan 3
	 * 3. juml 4 digit pertama dikurang juml dari digit 1 & 7 > digit ke 5 kali digit ke 6 tamba digit ke 7 
	 * 4. juml ke 8 digit jika di mod 8 sisa nya max 5 
	 * 5. juml angka 0 max 3
	 * 
	 * Checksum2 :
	 * 1. juml 4 digit pertama di mod 2 sisa nya 1  
	 * 2. juml 4 digit terakhir merupakan kelipatan 3
	 * 3. juml 4 digit pertama ditambah juml dari digit 1 & 8 > digit ke 5 kali digit ke 6 dikurang digit ke 3 
	 * 4. juml ke 8 digit jika di mod 7 sisa nya max 5 
	 * 5. juml angka 3 max 4
	 * 
	 * Checksum3 :
	 * 1. juml digit genap pertama di mod 2 sisa nya 1  
	 * 2. juml digit ganjil merupakan kelipatan 3
	 * 3. juml digit 1,2,7,8 ditambah juml dari digit 5 & 6 > digit ke 2 kali digit ke 6 dikurang digit ke 5 
	 * 4. juml ke 8 digit jika di mod 6 sisa nya max 4 
	 * 5. juml angka 2 max 3 
	 * 
	 * Checksum4 :
	 * 1. juml digit genap pertama di mod 3 sisa nya 1  
	 * 2. juml digit ganjil merupakan kelipatan 3
	 * 3. juml digit 1,2,5,6 ditambah juml dari digit 3 & 7 > digit ke 4 kali digit ke 8 dikurang digit ke 6 
	 * 4. juml ke 8 digit jika di mod 9 sisa nya max 6 
	 * 5. juml angka 4 max 3
	 *   
	 * Checksum5 :
	 * 1. juml 6 digit pertama di mod 3 sisa nya 1  
	 * 2. juml 6 digit terakhir merupakan kelipatan 3
	 * 3. juml 6 digit pertama dikurang juml dari digit 1,2,10 & 11 > digit ke 5 kali digit ke 6 tamba digit ke 7 
	 * 4. juml ke 12 digit jika di mod 8 sisa nya max 5 
	 * 5. juml angka 0 max 5 
	 * 
	 * Checksum6 :
	 * 1. juml digit 4,5,6,7,8,9 di mod 5 sisa nya 1  
	 * 2. juml digit 1,2,3,10,11,12 merupakan kelipatan 3
	 * 3. juml 6 digit pertama dikurang juml dari digit 7,8 & 9 > digit ke 10 kali digit ke 11 tambah digit ke 12 
	 * 4. juml ke 12 digit jika di mod 6 sisa nya max 4 
	 * 5. juml angka 3 max 5  
	 * 
	 * Checksum7 :
	 * 1. juml digit 4,5,6,7,8,9 di mod 5 sisa nya 1  
	 * 2. juml digit 1,2,3,8,9,10 merupakan kelipatan 3
	 * 3. juml digit 4,5,6,7,8,9 dikurang juml dari digit 7,8 & 9 > digit ke 8 kali digit ke 9 tambah digit ke 10 
	 * 4. juml ke 10 digit jika di mod 6 sisa nya max 4 
	 * 5. juml angka 3 max 5
	 */

	public static void main(String args[]) {
		long start = System.currentTimeMillis();
		
		//Contoh untuk generate pin, sekaligus langsung dicek
//		for (int i = 0; i < 10; i++) {
//			String pin = generatePin12Digit(5);
//			logger.info("Pin: " + pin + ", hasil cek: " + cekKartu(pin));
//		}
		
		String pin="86593952";
		logger.info("Pin: " + pin + ", hasil cek: " + cekPin2(generatePin8Digit(5),"05"));
//		 pin="87972736";
		logger.info("Pin: " + pin + ", hasil cek: " + generatePin8Digit(5));
//		logger.info("Jumlah kemungkinan dari rumus CheckSum1 : " + jumlKemungkinan());
				
		long end = System.currentTimeMillis();
//		logger.info("FINISHED AT " + new Date() + " in " + ( (float) (end-start) / 1000) + " SECONDS.");
	}
	
	public static String generatePin8Digit(int jenis) {
		int[] pin = new int[8];
		String tampung= "";
		Boolean counter = false;
		
		while(counter == false) {
			for(int a=0;a<pin.length;a++) {
				pin[a] = (int)(Math.random()*10);
			}
			tampung = Integer.toString(pin[0])+Integer.toString(pin[1])+Integer.toString(pin[2])+Integer.toString(pin[3])+
					  Integer.toString(pin[4])+Integer.toString(pin[5])+Integer.toString(pin[6])+Integer.toString(pin[7]);
			
			if(1 == jenis) counter = rumusCheckSum1(tampung);
			else if(2 == jenis) counter = rumusCheckSum2(tampung);
			else if(3 == jenis)	counter = rumusCheckSum3(tampung);
			else if(4 == jenis || 5 == jenis) counter = rumusCheckSum4(tampung);
			else throw new RuntimeException("Jenis harus 1-4");
		}
		return tampung;
	}
	
	public static String generateSimasCard6Digit(int jenis) {
		int[] pin = new int[6];
		String tampung= "";
		Boolean counter = false;
		
		while(counter == false) {
			for(int a=0;a<pin.length;a++) {
				pin[a] = (int)(Math.random()*10);
			}
			tampung = Integer.toString(pin[0])+Integer.toString(pin[1])+Integer.toString(pin[2])+Integer.toString(pin[3])+
					  Integer.toString(pin[4])+Integer.toString(pin[5]);
			
			if(7 == jenis) counter = rumusCheckSum1(tampung);
			else throw new RuntimeException("Jenis harus 7");
		}
		return tampung;
	}
	
	public static String generatePin6Digit(int jenis) {
		int[] pin = new int[6];
		String tampung= "";
		Boolean counter = false;
		
		while(counter == false) {
			for(int a=0;a<pin.length;a++) {
				pin[a] = (int)(Math.random()*10);
			}
			tampung = Integer.toString(pin[0])+Integer.toString(pin[1])+Integer.toString(pin[2])+Integer.toString(pin[3])+
					  Integer.toString(pin[4])+Integer.toString(pin[5]);
			
			if(7 == jenis) counter = rumusCheckSum1(tampung);
			else throw new RuntimeException("Jenis harus 7");
		}
		return tampung;
	}

	public static String generatePin12Digit(int jenis) {
		int[] pin = new int[12];
		String tampung= "";
		Boolean counter = false;
		
		while(counter == false) {
			for(int a=0;a<pin.length;a++) {
				pin[a] = (int)(Math.random()*10);
			}
			tampung = Integer.toString(pin[0])+Integer.toString(pin[1])+Integer.toString(pin[2])+Integer.toString(pin[3])+
					  Integer.toString(pin[4])+Integer.toString(pin[5])+Integer.toString(pin[6])+Integer.toString(pin[7])+
					  Integer.toString(pin[8])+Integer.toString(pin[9])+Integer.toString(pin[10])+Integer.toString(pin[11]);

			if(5 == jenis) counter = rumusCheckSum5(tampung);
			else if(6 == jenis) counter = rumusCheckSum6(tampung);
			else throw new RuntimeException("Jenis harus 5-6");
		}
		return tampung;
	}
	
	//Generate Kode Untuk Tambang Emas
	public static String generateKode10Digit(int jenis) {
		String tampung= "";
		Boolean counter = false;
		
		while(counter == false) {
			String uuid = UUID.randomUUID().toString();
			uuid = uuid.substring(0,10);
			tampung = uuid;
			
			if(1 == jenis) counter = rumusCheckSum7(tampung);
			else throw new RuntimeException("Jenis harus 1");
		}
		return tampung;
	}

	public static String cekPin(String pin) {
		//Boolean cek = false;
		String mssg = "";
		//tambahan
		if(pin.length()>2){
			pin=pin.substring(2);
		}
		//=======
		
		if(rumusCheckSum1(pin)) {
			mssg = "01";
		}
		else if(rumusCheckSum2(pin)) {
			mssg = "02";
		}
		else if(rumusCheckSum3(pin)) {
			mssg = "03";
		}
		else if(rumusCheckSum4(pin)) {
			mssg = "04";
		}
		else {
			mssg = "x";
		}
		//cek = rumusCheckSum1(pin);
		//if(cek == false) return "Pin tidak sesuai dengan rumus checksum";
		
		//return "Pin yang dimasukkan sesuai dengan rumus checksum";
		return mssg;
	}
	
	
	public static Boolean cekPin2(String pin,String produk) {
		//Boolean cek = false;
		String mssg = "";
		
		if(produk.equals("01")){
			return rumusCheckSum1(pin);
		}else if(produk.equals("02")){
			return rumusCheckSum2(pin);
		}else if(produk.equals("03")){
			return rumusCheckSum3(pin);
		}else if(produk.equals("04") || produk.equals("05")){
			return rumusCheckSum4(pin);
		}
		
		return false;
	}
	
	public static Boolean cekKartu2(String pin,String dist) {
		//Boolean cek = false;
		String mssg = "";
		
		if(dist.equals("01")){
			return rumusCheckSum6(pin);
		}else if(dist.equals("05")){
			return rumusCheckSum5(pin);
		}
		
		return false;
	}
	
	public static String cekKartu(String pin) {
		//Boolean cek = false;
		String mssg = "";
		
		if(rumusCheckSum5(pin)) {
			mssg = "05";
		}
		else if(rumusCheckSum6(pin)) {
			mssg = "01";
		}
		else {
			mssg = "0";
		}
		//cek = rumusCheckSum1(pin);
		//if(cek == false) return "Pin tidak sesuai dengan rumus checksum";
		
		//return "Pin yang dimasukkan sesuai dengan rumus checksum";
		return mssg;
	}
	
	private static Boolean rumusCheckSum1(String pin) {
		int ctt = 0;
		int[] cek = new int[8];

		for (int i = 0; i < pin.length(); i++) { cek[i] = (int) pin.charAt(i)-48; }
		
		int pertama = cek[0]+cek[1]+cek[2]+cek[3];
		int kedua = cek[4]+cek[5]+cek[6]+cek[7];
		int ketiga = cek[4]*cek[5]+cek[6];
		int keempat = cek[0] + cek[6];
		if(pertama % 3 == 1) {
			if(kedua % 3 == 0) {
				if(pertama - keempat > ketiga) {
					if((pertama+kedua) % 8 <= 5) {
						for (int cekNol : cek) {
							if(cekNol == 0) ctt++;
						}	
						if(ctt <= 3) {
							return true;
						}
					}
				}
			}
		}		
		return false;
	}
	
	private static Boolean rumusCheckSum2(String pin) {
		int ctt = 0;
		int[] cek = new int[8];

		for (int i = 0; i < pin.length(); i++) { cek[i] = (int) pin.charAt(i)-48; }
		
		int pertama = cek[0]+cek[1]+cek[2]+cek[3];
		int kedua = cek[4]+cek[5]+cek[6]+cek[7];
		int ketiga = cek[5]*cek[6]+cek[7];
		int keempat = cek[0] + cek[7];
		if(pertama % 2 == 1) {
			if(kedua % 3 == 0) {
				if(pertama - keempat > ketiga) {
					if((pertama+kedua) % 7 <= 5) {
						for (int cekNol : cek) {
							if(cekNol == 0) ctt++;
						}	
						if(ctt <= 3) {
							return true;
						}
					}
				}
			}
		}		
		return false;
	}
	
	private static Boolean rumusCheckSum3(String pin) {
		int ctt = 0;
		int[] cek = new int[8];

		for (int i = 0; i < pin.length(); i++) { cek[i] = (int) pin.charAt(i)-48; }
		
		int pertama = cek[0]+cek[2]+cek[4]+cek[6];
		int kedua = cek[0]+cek[1]+cek[6]+cek[7];
		int ketiga = cek[1]*cek[5]-cek[4];
		int keempat = cek[4] + cek[5];
		int kelima = cek[0]+cek[1]+cek[6]+cek[7];
		if(pertama % 2 == 1) {
			if(kedua % 3 == 0) {
				if(kelima + keempat > ketiga) {
					if((pertama+kedua) % 6 <= 4) {
						for (int cekNol : cek) {
							if(cekNol == 2) ctt++;
						}	
						if(ctt <= 3) {
							return true;
						}
					}
				}
			}
		}		
		return false;
	}	
	
	private static Boolean rumusCheckSum4(String pin) {
		int ctt = 0;
		int[] cek = new int[8];

		for (int i = 0; i < pin.length(); i++) { cek[i] = (int) pin.charAt(i)-48; }
		
		int pertama = cek[0]+cek[2]+cek[4]+cek[6];
		int kedua = cek[0]+cek[1]+cek[6]+cek[7];
		int ketiga = cek[3]*cek[7]-cek[5];
		int keempat = cek[2] + cek[6];
		int kelima = cek[0]+cek[1]+cek[4]+cek[5];
		if(pertama % 2 == 1) {
			if(kedua % 3 == 0) {
				if(kelima + keempat > ketiga) {
					if((pertama+kedua) % 9 <= 6) {
						for (int cekNol : cek) {
							if(cekNol == 4) ctt++;
						}	
						if(ctt <= 3) {
							return true;
						}
					}
				}
			}
		}		
		return false;
	}	
	
	private static Boolean rumusCheckSum5(String pin) {
		int ctt = 0;
		int[] cek = new int[12];

		for (int i = 0; i < pin.length(); i++) { cek[i] = (int) pin.charAt(i)-48; }
		
		int pertama = cek[0]+cek[1]+cek[2]+cek[3]+cek[4]+cek[5];
		int kedua = cek[6]+cek[7]+cek[8]+cek[9]+cek[10]+cek[11];
		int ketiga = cek[4]*cek[5]+cek[6];
		int keempat = cek[0]+cek[1]+cek[10]+cek[11];
		if(pertama % 3 == 1) {
			if(kedua % 3 == 0) {
				if(pertama - keempat > ketiga) {
					if((pertama+kedua) % 8 <= 5) {
						for (int cekNol : cek) {
							if(cekNol == 0) ctt++;
						}	
						if(ctt <= 5) {
							return true;
						}
					}
				}
			}
		}		
		return false;
	}
	
	private static Boolean rumusCheckSum6(String pin) {
		int ctt = 0;
		int[] cek = new int[12];

		for (int i = 0; i < pin.length(); i++) { cek[i] = (int) pin.charAt(i)-48; }
		
		int pertama = cek[3]+cek[4]+cek[5]+cek[6]+cek[7]+cek[8];
		int kedua = cek[0]+cek[1]+cek[2]+cek[9]+cek[10]+cek[11];
		int ketiga = cek[9]*cek[10]+cek[11];
		int keempat = cek[6]+cek[7]+cek[8];
		int kelima = cek[0]+cek[1]+cek[2]+cek[3]+cek[4]+cek[5]+cek[6]+cek[7]+cek[8]+cek[9]+cek[10]+cek[11];
		if(pertama % 5 == 1) {
			if(kedua % 3 == 0) {
				if(pertama - keempat > ketiga) {
					if((kelima) % 6 <= 4) {
						for (int cekNol : cek) {
							if(cekNol == 3) ctt++;
						}	
						if(ctt <= 5) {
							return true;
						}
					}
				}
			}
		}		
		return false;
	}
	
	private static Boolean rumusCheckSum7(String pin) {
		int ctt = 0;
		int[] cek = new int[10];

		for (int i = 0; i < pin.length(); i++) { cek[i] = (int) pin.charAt(i)-48; }
		
		int pertama = cek[3]+cek[4]+cek[5]+cek[6]+cek[7]+cek[8];
		int kedua = cek[0]+cek[1]+cek[2]+cek[7]+cek[8]+cek[9];
		int ketiga = cek[7]*cek[8]+cek[9];
		int keempat = cek[6]+cek[7]+cek[8];
		int kelima = cek[0]+cek[1]+cek[2]+cek[3]+cek[4]+cek[5]+cek[6]+cek[7]+cek[8]+cek[9];
		if(pertama % 5 == 1) {
			if(kedua % 3 == 0) {
				if(pertama - keempat > ketiga) {
					if((kelima) % 6 <= 4) {
						for (int cekNol : cek) {
							if(cekNol == 3) ctt++;
						}	
						if(ctt <= 5) {
							return true;
						}
					}
				}
			}
		}		
		return false;
	}
	
	private static int jumlKemungkinan() {
		int[] nilai = new int[8];
		String kirim = "";
		int jml = 0;
		
		for (int a = 0; a < 10; a++) {
			nilai[7] = a;
			for (int b = 0; b < 10; b++) {
				nilai[6] = b;
				for (int c = 0; c < 10; c++) {
					nilai[5] = c;
					for (int d = 0; d < 10; d++) {
						nilai[4] = d;
						for (int e = 0; e < 10; e++) {
							nilai[3] = e;
							for (int f = 0; f < 10; f++) {
								nilai[2] = f;
								for (int g = 0; g < 10; g++) {
									nilai[1] = g;
									for (int h = 0; h < 10; h++) {
										nilai[0] = h;
										
										kirim = Integer.toString(nilai[0])+Integer.toString(nilai[1])+Integer.toString(nilai[2])+Integer.toString(nilai[3])+
										  		Integer.toString(nilai[4])+Integer.toString(nilai[5])+Integer.toString(nilai[6])+Integer.toString(nilai[7]);
										
										logger.info(kirim + "\n");
										if(rumusCheckSum1(kirim) == true) jml++;
										if(rumusCheckSum2(kirim) == true) jml++;
										if(rumusCheckSum3(kirim) == true) jml++;
										if(rumusCheckSum4(kirim) == true) jml++;
									}
								}
							}
						}
					}
				}
			}
		}
		
		return jml;
	}	
}