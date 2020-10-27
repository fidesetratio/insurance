/**
 * Scheduler untuk Generate Outsource
 * @author Mark Valentino
 * @since 13-02-2019
 */

package com.ekalife.utils.scheduler;

import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.dao.BasDao;
import com.ekalife.elions.dao.UwDao;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Simcard;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.elions.web.uw.PrintPolisAllPelengkap;
import com.ekalife.elions.web.uw.PrintPolisPrintingController;
import com.ekalife.elions.web.uw.PrintPolisMultiController;
import com.ekalife.utils.CekPelengkap;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.Print;
import com.ekalife.utils.Products;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.parent.ParentScheduler;
import com.google.gwt.http.client.RequestBuilder.Method;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GenerateOutsourceScheduler extends ParentScheduler {
	
	//protected PrintPolisPrintingController ppc;
	protected PrintPolisAllPelengkap ppap;
	private Properties props;
	private ElionsManager elionsManager;
	private UwManager uwManager;
	private BasDao basDao;
	//private PrintPolisMultiController ppmc;

	private BacManager bacManager;
	private Products products;
	private User user;
	
	private Log logger = LogFactory.getLog( getClass() );	
	
	public PrintPolisAllPelengkap getPpap() {
		return ppap;
	}

	public void setPpap(PrintPolisAllPelengkap ppap) {
		this.ppap = ppap;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public ElionsManager getElionsManager() {
		return elionsManager;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

	public UwManager getUwManager() {
		return uwManager;
	}

	public void setUwManager(UwManager uwManager) {
		this.uwManager = uwManager;
	}

	public BacManager getBacManager() {
		return bacManager;
	}

	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
	}	

	public Products getProducts() {
		return products;
	}

	public void setProducts(Products products) {
		this.products = products;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

//	public PrintPolisMultiController getPpmc() {
//		return ppmc;
//	}
//
//	public void setPpmc(PrintPolisMultiController ppmc) {
//		this.ppmc = ppmc;
//	}

	//main method
	public void main() throws Exception{
		User user = new User();
		user.setJn_bank(0);
		user.setEmail("ajsjava@sinarmasmsiglife.co.id");
		user.setLus_id("1");		
		
		//HOST INFORMATION
		String computerName = InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase();	
		InetAddress ip;
		String ipAddress = "";
		try {
			ip = InetAddress.getLocalHost();
			ipAddress = ip.getHostAddress().toString();
		}catch (UnknownHostException e){
			logger.error("ERROR :", e);
		}
		
		if(jdbcName.equals("eka8i") && 
				(
						InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("ODPIT03"))
						//InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI64"))
						//|| InetAddress.getLocalHost().getHostName().toString().trim().toUpperCase().equals("AJSJAVAI643"))
						//ipAddress.equals("128.21.30.47") || ipAddress.equals("192.168.9.99"))
		  ){
			try{
				System.out.println("--- GENERATE OUTSOURCE SCHEDULER STARTED!! --- " + new Date().toString() + " --- " + computerName + " / (" + ipAddress + ") ---");
				
				String spaj = "";
				String desc = "OK";
				String err = "";
				String msh_name = "SCHEDULER GENERATE OUTSOURCE (E-LIONS)";
				//String lsbs_id = "";
				boolean oke = true;	
				String resultGenerate = "";
				String errorValidasi = "";
				int jumlahSuksesGenerate = 0;
				int jumlahError = 0;
				int jumlahExclude = 0;
				int jumlahEmail = 0;
	
				String listSukses = "";			
				String listExcludeMsg = "";
				String listError = "";
				String listEmail = "";
				
				long waktuMulai = System.nanoTime();
				String waktuMulai2 = new Date().toString();
				
					//ambil semua spaj yang akan diprint
					List dataPrint = this.getUwManager().getUwDao().selectPendingPrintPolis();
					//Export list spaj ke file TXT
					String pattern2 = "dd-MM-yyyy-hh-mm-ss";
					SimpleDateFormat sdf2 = new SimpleDateFormat(pattern2);
					String tanggalNow2 = sdf2.format(new Date());
					String outputDir = props.getProperty("pdf.dir.export.temp");				
					File fileList = new File(outputDir + "\\" + "list_spaj_scheduler_go_" + tanggalNow2 + ".txt");
					String textList = "Total SPAJ = " + dataPrint.size() + "\r\n" + "\r\n";
					//String textList = "";
					int jumlahTerproses = 0;
					
					for(int i=0; i<dataPrint.size();i++){
						spaj = (String) ((Map) dataPrint.get(i)).get("SPAJ");
						textList = textList + spaj + "\r\n";
					}
					try{
						PrintWriter outputStream = new PrintWriter(fileList);
						outputStream.print(textList);
						outputStream.close();
					}catch(Exception e){
						logger.error("ERROR :", e);					
					}					
					
					for(int i=0; i<dataPrint.size();i++){
						try{
							oke = true;
							spaj = (String) ((Map) dataPrint.get(i)).get("SPAJ");
							if (spaj.equals("09190471491")){
								System.out.println("check!!");
							}								
							List detBisnis = elionsManager.selectDetailBisnis(spaj);
							String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
							String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
							String duaDigit = StringUtils.substring(spaj, 0, 2);							
							boolean isDmtm = duaDigit.equals("40");
							
							// cek mspo_date_print
							if(!(((Map) dataPrint.get(i)).get("DATE_PRINT") == null)) {
								errorValidasi = "Sudah pernah generate.";
								jumlahError++;
								listError = listError + spaj + "     " + ((String) ((Map) detBisnis.get(0)).get("LSDBS_NAME")) + "     " + errorValidasi + "\r\n";
								continue;
							}								
							
							boolean listExclude = 
									//Bancass
									(lsbs.equals("142") && ("002,004,006,007".indexOf(lsdbs)>-1)) //SIMAS PRIMA
									|| (lsbs.equals("175") && lsdbs.equals("002")) //POWER SAVE SYARIAH BSM
									|| (lsbs.equals("212") && lsdbs.equals("006")) //SMART LIFE PROTECTION (BUKOPIN)
									|| (lsbs.equals("223") && lsdbs.equals("001")) //Smile Life Syariah						
									//DMTM
									|| (lsbs.equals("187") && ("011,012,013,014".indexOf(lsdbs)>-1)) //SMART ACCIDENT CARE
									|| (lsbs.equals("205") && ("005,006,007,008".indexOf(lsdbs)>-1)) //PAS SYARIAH
									|| (lsbs.equals("197") && lsdbs.equals("002")) //CI CANCER
									|| (lsbs.equals("203")) //DEMAM BERDARAH
									|| (lsbs.equals("73") && ("014,015".indexOf(lsdbs)>-1)) //PA
									|| (lsbs.equals("212") && lsdbs.equals("008")) //SMILE LIFE PRO	
									//Semua Product Agency
									|| (duaDigit.equals("37"))
									|| (duaDigit.equals("53"))
									|| (duaDigit.equals("01"))
									|| (duaDigit.equals("02"));
							
							if(listExclude){
								jumlahExclude++;
								//listError + spaj + "     " + errorValidasi + "\n";
								listExcludeMsg = listExcludeMsg + spaj + "     " + ((String) ((Map) detBisnis.get(0)).get("LSDBS_NAME")) + "\r\n";
								continue;							
							}
							
							//Validasi menggunakan method dari class PrintPolisMultiController : validasi
//							List validasi = new ArrayList();
//							validasi = ppmc.publicValidasi(33, spaj, lsbs, null);
//							String reportUrl = "";
//							if(validasi.size() > 0){
//								jumlahError++;
//								listExcludeMsg = listExcludeMsg + spaj + "\r\n";
//								continue;								
//							}
							
//							//Exclude Khusus DMTM BTN Tele
//							int jnBank = bacManager.selectJnBankDetBisnis(Integer.parseInt(lsbs), Integer.parseInt(lsdbs));
//							HashMap m = Common.serializableMap(uwManager.selectInformasiEmailSoftcopy(spaj));
//							String email = (String) m.get("MSPE_EMAIL");
//							if ((email == null) || (email.length()<=3)) email = "";
//							if(isDmtm){
//								if ((lsbs.equals("195") || lsbs.equals("208") || lsbs.equals("169") || lsbs.equals("212")
//								|| lsbs.equals("183"))	&& (!email.equals("")) && (jnBank == 43)){
//									errorValidasi = "Polis ini harus diterbitkan dalam bentuk SOFTCOPY (E-MAIL).";									
//									jumlahEmail++;
//									listEmail = listEmail + spaj + "     " + ((String) ((Map) detBisnis.get(0)).get("LSDBS_NAME")) + "\r\n";
//									continue;
//								}
//							}
							
							Integer cekProsesBatal = bacManager.selectCountSpajMstRefund(spaj);
							if(cekProsesBatal>0){
								errorValidasi = "Polis tidak dapat dicetak karena dalam proses Pembatalan.";
								jumlahError++;
								listError = listError + spaj + "     " + ((String) ((Map) detBisnis.get(0)).get("LSDBS_NAME")) + "     " + errorValidasi + "\r\n";
								continue;								
							}
							if(this.elionsManager.validationPositionSPAJ(spaj)!=6) {
								errorValidasi = "Harap cek posisi SPAJ.";
								jumlahError++;
								listError = listError + spaj + "     " + ((String) ((Map) detBisnis.get(0)).get("LSDBS_NAME")) + "     " + errorValidasi + "\r\n";
								continue;										
							}
//							// Validasi Jenis Terbit : HARDCOPY / SOFTCOPY
//							if(uwManager.selectJenisTerbitPolis(spaj)==1) {
//								errorValidasi = "Polis ini harus diterbitkan dalam bentuk SOFTCOPY (E-MAIL).";
//								jumlahEmail++;
//								//listEmail = listEmail + spaj + "     " + "\r\n";
//								listEmail = listEmail + spaj + "     " + ((String) ((Map) detBisnis.get(0)).get("LSDBS_NAME")) + "\r\n";								
//								continue;
//							}							
							
							if(products.unitLink(lsbs)) {
								List<Date> asdf = uwManager.selectSudahProsesNab(spaj);
								for(Date d : asdf){
									if(d == null) {
										jumlahExclude++;
										listExcludeMsg = listExcludeMsg + spaj + "     " + ((String) ((Map) detBisnis.get(0)).get("LSDBS_NAME")) + "     " + "Belum Proses NAB" + "\r\n";									
										oke = false;
										continue;
									}else{
										oke = true;
									}
								}
								if(asdf.size()==0){
									jumlahExclude++;
									listExcludeMsg = listExcludeMsg + spaj + "     " + ((String) ((Map) detBisnis.get(0)).get("LSDBS_NAME")) + "     " + "Belum Proses NAB" + "\r\n";								
									oke = false;
									continue;
								}						
							}			
								
							//if ok -> generate Simascard & Generate Files
							if(oke){
								
								//Cek Stock Simascard
								int stockSimascard = Integer.parseInt(bacManager.getUwDao().selectCekStockSimCardRds(0, "GENERATE OUTSOURCE SCHEDULER"));
								if (stockSimascard <= 2000){ //ganti 5000
									//Generate Simascard
									uwManager.test("Generate Outsource Scheduler");
								}									
								
								//Input Simascard
								Map params = new HashMap();
								Simcard simascard = new Simcard();
								List lsError = new ArrayList();
								List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj);
								
								List daftarSebelumnya = uwManager.selectSimasCard(spaj); //daftar simas card sebelumnya yang masih aktif
								//daftarSebelumnya.removeAll(daftarSebelumnya);
								if(daftarSebelumnya.isEmpty()) {
									String mrc_no_kartu = uwManager.getUwDao().selectAmbilSatuNoKartuSimascard("GENERATE OUTSOURCE SCHEDULER");
									if(!StringUtil.isEmpty(mrc_no_kartu)){
										simascard = uwManager.selectSimasCardByNoKartu(mrc_no_kartu);
										if(simascard==null){
											simascard = uwManager.selectSimasCardBySpaj(spaj);
										}
										if(simascard!=null){
											lsError.add("No Kartu sudah pernah diinput pada no SPAJ : "+ simascard.getReg_spaj());
										}
										if(lsError.isEmpty()){
											Boolean prosesSimasCard = uwManager.prosesInsertSimasCardNew(spaj, mrc_no_kartu, user,0);									
											if(!prosesSimasCard){
												errorValidasi = "Proses Penginputan Simascard tidak berhasil.";
												jumlahError++;
												listError = listError + spaj + "     " + ((String) ((Map) detBisnis.get(0)).get("LSDBS_NAME")) + "     " + errorValidasi + "\r\n";
												continue;
											}
										}else{
											continue;
										}
									}else{
		//								lsError.add("Harap masukkan No Kartu terlebih dahulu");
										continue;
									}
								}
								params.put("lsError", lsError);
								
								//GENERATE FILES
								resultGenerate = ppap.generateOutsourceScheduler(spaj, 2, props, products, user);
														
//								//FILLING - Update LSPD_ID = 99 (Khusus DMTM)
//								if(isDmtm){
//									//cari tau menu "update LSPD ke 99" (Filing) di E-Lions di sebelah mana?
//								}
															
								if(resultGenerate.isEmpty()){
									//insert eka.mst_eksternal									
									String cabang = elionsManager.selectCabangFromSpaj(spaj);
								    String pathUploadZip = props.getProperty("pdf.dir.ftp")+"\\"+cabang+"\\"+spaj+"\\"+spaj+".zip";
							        Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
									String lcaId = dataUsulan.getLca_id();
									String lusId = "01";
								
									//handling duplicate
									bacManager.updateFlagKonfirmasiMstEksternal(spaj);
									//insert new data
									uwManager.getUwDao().insertMst_eksternal_print(spaj, pathUploadZip, lcaId, lusId);
									listSukses = listSukses + spaj + "     " + ((String) ((Map) detBisnis.get(0)).get("LSDBS_NAME")) + "\r\n";
									jumlahSuksesGenerate++;									
								}else{
									jumlahError++;
									listError = listError + spaj + "\r\n";													
									CekPelengkap cp = new CekPelengkap();
									String pathTemp = "";
									pathTemp = props.getProperty("pdf.dir.export.temp") + "\\" + spaj;
									File dirTemp = new File(pathTemp);
									if(dirTemp.exists()){
										cp.deleteDirectoryWithFiles(dirTemp);								
									}							
									continue;							
								}
							}
						}catch(Exception e){
							logger.error("ERROR :", e);
							continue;
						}
					}
					
					try {
						//insert history ke database
						bacManager.insertMstSchedulerHist(InetAddress.getLocalHost().getHostName(), msh_name, new Date(), new Date(), desc, 
								"Computer Name            = " + computerName + "\n"
										+ "IP Address               = " + ipAddress + "\n\n"
										+ "Jumlah List SPAJ         = " + dataPrint.size() + "\n"
										+ "Jumlah Sukses Generate   = " + jumlahSuksesGenerate + "\n" //+ listSukses + "\n" 
										+ "Jumlah Error             = " + jumlahError + "\n" //+ listError + "\n" 
										+ "Jumlah Exclude           = " + jumlahExclude + "\n" //+ listExcludeMsg + "\n"
										+ "Jumlah Softcopy (e-mail) = " + jumlahEmail); //+ listEmail);
						
						//Create TXT file untuk email attachment
						String pattern = "dd-MM-yyyy";
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						String tanggalNow = sdf.format(new Date());											
						File fileTxt = new File(outputDir + "\\" + "scheduler_go_attachment_" + tanggalNow + ".txt");
						jumlahTerproses = jumlahSuksesGenerate + jumlahEmail + jumlahError + jumlahExclude;
						String textMsg = "Jumlah List SPAJ = " + jumlahTerproses + "\r\n" + "\r\n"
								+ "Jumlah Sukses Generate = " + jumlahSuksesGenerate + "\r\n" + listSukses +  "\r\n"
								+ "Jumlah Error = " + jumlahError + "\r\n" + listError + "\r\n" 
								+ "Jumlah Exclude = " + jumlahExclude + "\r\n" + listExcludeMsg + "\r\n"
								+ "Jumlah Softcopy (e-mail) = " + jumlahEmail + "\r\n" + listEmail;
						try{
							PrintWriter outputStream = new PrintWriter(fileTxt);
							outputStream.print(textMsg);
							outputStream.close();
						}catch(Exception e){
							//e.printStackTrace();
							logger.error("ERROR :", e);					
							EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
									null, 0, 0, new Date(), null, false, props.getProperty("admin.ajsjava"), new String[]{"mark.valentino@sinarmasmsiglife.co.id"}, null, null,  
									"Error GENERATE OUTSOURCE SCHEDULER (E-LIONS)", 
									e+"", null, spaj);
						}						
						
						//kirim email Result Scheduler Generate Outsource
						long waktuSelesai = System.nanoTime();
						String waktuSelesai2 = new Date().toString();
						long lamaProses = (waktuSelesai - waktuMulai) / 1000000;  //divide by 1000000 to get milliseconds.						
						List<File> attachments = new ArrayList<File>();
						if(fileTxt.exists()){
							attachments.add(fileTxt);
						}
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0,
								null, 0, Integer.parseInt(user.getLus_id()), new Date(), null, true,
								props.getProperty("admin.ajsjava"),
								//to
								new String[]{"uwprinting@sinarmasmsiglife.co.id"},
								//new String[]{"mark.valentino@sinarmasmsiglife.co.id"},								
								//cc
								//helpdesk [145209] Permohonan Penghapusan email Pak Suryanto Lim
								//new String[]{"suryanto@sinarmasmsiglife.co.id;inge@sinarmasmsiglife.co.id;tities@sinarmasmsiglife.co.id;anna@sinarmasmsiglife.co.id;itweb@sinarmasmsiglife.co.id"},
								new String[]{"inge@sinarmasmsiglife.co.id;tities@sinarmasmsiglife.co.id;anna@sinarmasmsiglife.co.id;itweb@sinarmasmsiglife.co.id"},
								//null,								
								//bcc
								new String[]{"ibroy@sinarmasmsiglife.co.id;mark.valentino@sinarmasmsiglife.co.id;tohirin@sinarmasmsiglife.co.id;sultan@sinarmasmsiglife.co.id;maulana@sinarmasmsiglife.co.id"},
								//null,								
								//Judul Email
								"SCHEDULER GENERATE OUTSOURCE (E-LIONS) " + new Date(),
								//Body Email,	
								"Dear All," + "\n\n"
								+ "Berikut adalah hasil dari Scheduler Generate Outsource : " + "\n\n"
//										+ "Jumlah List SPAJ         = " + dataPrint.size() + "\n"
										+ "Jumlah List SPAJ         = " + jumlahTerproses + "\n"								
										+ "Jumlah Sukses Generate   = " + jumlahSuksesGenerate + "\n" //+ listSukses + "\n" 
										+ "Jumlah Error             = " + jumlahError + "\n" //+ listError + "\n" 
										+ "Jumlah Exclude           = " + jumlahExclude + "\n" //+ listExcludeMsg + "\n"
										+ "Jumlah Softcopy (e-mail) = " + jumlahEmail + "\n\n"
										+ "[Cek attachment untuk detail]" + "\n\n"
										+ "Waktu Mulai              = " + waktuMulai2 + "\n"
										+ "Waktu Selesai            = " + waktuSelesai2 + "\n"
										+ "Total Waktu Proses       = " + lamaProses + " ms.",
								attachments,
								null);
						
						fileTxt.delete();
					} catch (UnknownHostException e) {
						logger.error("ERROR :", e);
					}
					
					System.out.println("");
					System.out.println("----------------Scheduler Generate Outsource-------------------------");
					System.out.println("Jumlah SPAJ              = " + dataPrint.size());						
					System.out.println("Jumlah Sukses Generate   = " + jumlahSuksesGenerate);						
					System.out.println("Jumlah Error             = " + jumlahError);
					System.out.println("Jumlah Exclude           = " + jumlahExclude);
					System.out.println("Jumlah Softcopy (e-mail) = " + jumlahEmail);					
					System.out.println("---------------------------------------------------------------------");					
					
				}catch(Exception e){
					logger.error("ERROR :", e);					
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
							null, 0, Integer.parseInt(user.getLus_id()), new Date(), null, true, props.getProperty("admin.ajsjava"), new String[]{"mark.valentino@sinarmasmsiglife.co.id"}, null, null,  
							"Error GENERATE OUTSOURCE SCHEDULER (E-LIONS)", 
							e+"", null, null);			
				}
		
		}
	}

}
