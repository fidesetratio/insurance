package com.ekalife.elions.dao;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;

import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.AddressRegion;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.AgentTax;
import com.ekalife.elions.model.App;
import com.ekalife.elions.model.BVoucher;
import com.ekalife.elions.model.Billing;
import com.ekalife.elions.model.ClientHistory;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.DBank;
import com.ekalife.elions.model.DataNasabah;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Followup;
import com.ekalife.elions.model.FormHist;
import com.ekalife.elions.model.FormSpaj;
import com.ekalife.elions.model.Hadiah;
import com.ekalife.elions.model.KartuNama;
import com.ekalife.elions.model.Keluarga;
import com.ekalife.elions.model.Mia;
import com.ekalife.elions.model.Pemegang2;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Premi;
import com.ekalife.elions.model.Questionnaire;
import com.ekalife.elions.model.Region;
import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.model.SpajDet;
import com.ekalife.elions.model.Surat_history;
import com.ekalife.elions.model.TBank;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.TravelInsurance;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.btpp.Btpp;
import com.ekalife.elions.model.sms.Smsserver_in;
import com.ekalife.elions.model.sms.Smsserver_out;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.ExcelRead;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentDao;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;


/**
 * Data Access Object untuk modul2 di menu BAS
 * 
 * @author Yusuf Sutarko
 * @since Feb 23, 2007 (4:56:03 PM)
 */
@SuppressWarnings("unchecked")
public class BasDao extends ParentDao{
	protected final Log logger = LogFactory.getLog( getClass() );

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);	

	protected void initDao() throws DataAccessException{
		this.statementNameSpace = "elions.bas.";
	}	
	
	public Boolean selectCekPinExist(String Pin)throws DataAccessException {
    	Integer exist = (Integer) querySingle("selectCekPinExist", Pin);
    	if(exist == 1) return true;
    	return false;
    }
	
	public Boolean selectCekKartuExist(String Pin)throws DataAccessException {
    	Integer exist = (Integer) querySingle("selectCekKartuExist", Pin);
    	if(exist == 1) return true;
    	return false;
    }
	
	public void insertManualSimasCardOld(User currentUser) throws Exception {
		String directory = "C:\\Documents and Settings\\deddy\\Desktop\\";
		String fileName = "INDIVIDU.xls";
		ExcelRead excelRead = new ExcelRead();
		List<List> excelList = excelRead.readExcelFile(directory, fileName);
		String simcardnull="";
		for(int i = 1 ; i < excelList.size() ; i++){
			String spaj=excelList.get(i).get(0).toString();
			String mrc_no_kartu =excelList.get(i).get(1).toString();
			Date tgl_kirim = new Date(excelList.get(i).get(2).toString());
			
//Apabila proses sesuai prosedur, pakai prosesinsertsimascardnew, apabila proses manual krn tertinggal/terlewat, pakai prosesinsertsimascardnewmanual
			//uwDao.prosesInsertSimasCardNew(spaj, mrc_no_kartu.toString(), currentUser,0);
//			Simcard simcard = simasCardDao.selectSimasCardByNoKartu(mrc_no_kartu);
			String simcard = basDao.selectExistNoKartuSimasCard(mrc_no_kartu);
			if(Common.isEmpty(simcard)){
				simcardnull +=mrc_no_kartu+"\n";
			}else{
				uwDao.prosesInsertSimasCardNewManual(spaj, mrc_no_kartu.toString(), currentUser,0,tgl_kirim);
			}
			
		}
		logger.info(simcardnull);
		
//		uwDao.prosesInsertSimasCardNew(spaj, mrc_no_kartu, currentUser,0);
	}

	public void generateKartuSimasCard() throws DataAccessException{
		Calendar tgl_sekarang = Calendar.getInstance();
//		Integer generateYear=new Integer(tgl_sekarang.get(Calendar.YEAR));
		Integer generateYear=new Integer(2110); // Utk U/W sebelumnya 2010
//		Integer generateYear=new Integer(2011); // Utk Agency
//		Integer generateYear=new Integer(2012); // Utk Marketing
		String nomorKartu = "";
		String generateKartu = "";
		//115 counter untuk U/W
		//117 counter untuk Agency
		//133 counter untuk marketing
		Integer counter_id = 115;
		int totalGenerate	= 50000;
		for(int i=0; i<totalGenerate; i++){
			Map a = uwDao.selectMstCounter(counter_id,"01");
			Double hasil=(Double) a.get("MSCO_VALUE");
			Boolean ok = false;
			do{
				try{
					Integer counter=new Integer(hasil.intValue());
					String generateCounter=FormatString.rpad("0", counter.toString(), 6);
					generateKartu = CheckSum.generateSimasCard6Digit(7);
					nomorKartu = generateYear.toString()+ generateKartu+ generateCounter;
					String jumlahKartu = selectExistNoKartuSimasCard(nomorKartu);
					if(selectExistNoKartuSimasCard(nomorKartu) != null){
						ok=false;
					}else{
						Map m = new HashMap();
						m.put("dist", 		"05");//05=UW / agency, 08=marketing
						m.put("no_kartu", 			nomorKartu); 
						m.put("produk", 		"05");
						m.put("premi", 			(double) 0);
						m.put("lus_id_input", 	(int) 1255);
						m.put("reg_spaj", 		null);
						insert("insertMstKartuPas", m);
						counter+=1;
						//115 counter untuk U/W
						//117 counter untuk Agency
						//133 counter untuk marketing
						bacDao.update_counter(counter.toString(), counter_id, "01");
						ok=true;
					}
				}catch(Exception e){};
			}while (!ok);
		}
		
		
//		String to[]="inge@sinarmasmsiglife.co.id;ingrid@sinarmasmsiglife.co.id".split(";");
//		String subject="Generate Nomor kartu Simas Card";
//		String message="Berikut disampaikan Hasil Generate sebanyak "+totalGenerate+" nomor yang diminta :\n"+
//					   "";
//		try {
//			email.send(
//					false, props.getProperty("admin.ajsjava"),
//					to, null, null, subject, message, null);
//		} catch (MailException e) {
//			logger.error("ERROR :", e);
//		} catch (MessagingException e) {
//			logger.error("ERROR :", e);
//		}
	}
	
	public void generateNoKartu() throws DataAccessException{
		Calendar tgl_sekarang = Calendar.getInstance();
		String generateIdNumber="0544";// diubah sesuai request
		String nomorKartu = "";
		String generateKartu = "";
		int totalGenerate	= 3000;// total sesuai request +1
		int start = 7001;
		//start counter dari no 1
		for(int i=start; i<(start+totalGenerate); i++){
			//115 counter untuk U/W
			//117 counter untuk Agency
			//133 counter untuk marketing
			Double hasil=new Double(i);
			Boolean ok = false;
			do{
				try{
					Integer counter=new Integer(hasil.intValue());
					String generateCounter=FormatString.rpad("0", counter.toString(), 6);
					generateKartu = CheckSum.generateSimasCard6Digit(7);
					nomorKartu = generateIdNumber+ generateKartu+ generateCounter;
					String jumlahKartu = selectExistNoKartuSimasCard(nomorKartu);
					if(selectExistNoKartuSimasCard(nomorKartu) != null){
						ok=false;
					}else{
						Map m = new HashMap();
						m.put("dist", 		"05");//05 agency, 08 marketing
						m.put("no_kartu", 			nomorKartu); 
						m.put("produk", 		"15");
						m.put("premi", 			(double) 75000);
						m.put("up", 			(double) 25000000);
						m.put("lus_id_input", 	(int) 1436);
						m.put("reg_spaj", 		null);
						m.put("product_code", 		73);
						m.put("product_sub_code", 		14);
						m.put("msag_id", 		"027789");
						m.put("keterangan", 	"BANK SINARMAS");
						insert("insertMstKartuPas", m);
						ok=true;
					}
				}catch(Exception e){
					logger.error("ERROR :", e);
				};
			}while (!ok);
		}
		
	}
	
	/**
	 * 
	 * @throws DataAccessException
	 * product_code = 73 & 187
	 * sub_code = 73 (14); 187 (1,2,3,4)
	 * generateIdNumber/totalGenerate/premi/up/lus_id = dinamis
	 * start (counter) dari db
	 * 
	 * 
	 */
	public void generateNoKartuPAS(HashMap map) throws DataAccessException{
		Calendar tgl_sekarang = Calendar.getInstance();
		String generateIdNumber= (String) map.get("generateIdNumber");// diubah sesuai request (default = 0544)
		String nomorKartu = "";
		String generateKartu = "";
		Integer totalGenerate	= Integer.valueOf((String) map.get("totalGenerate"));// total sesuai request +1
		Map count = uwDao.selectMstCounter(197, "01");
		Double cc = (Double) count.get("MSCO_VALUE");
		Integer start = cc.intValue()+1;// ((Integer) count.get("MSCO_VALUE"))+1;
		Double hasil = null;
		//start counter dari no 1
		for(int i=start; i<(start+totalGenerate); i++){
			//115 counter untuk U/W
			//117 counter untuk Agency
			//133 counter untuk marketing
			hasil=new Double(i);
			Boolean ok = false;
			do{
				try{
					Integer counter=new Integer(hasil.intValue());
					String generateCounter=FormatString.rpad("0", counter.toString(), 6);
					generateKartu = CheckSum.generateSimasCard6Digit(7);
					nomorKartu = generateIdNumber+ generateKartu+ generateCounter;
					String jumlahKartu = selectExistNoKartuSimasCard(nomorKartu);
					if(selectExistNoKartuSimasCard(nomorKartu) != null){
						ok=false;
					}else{
						Double premi = Double.valueOf((String) map.get("premi"));
						Double up = Double.valueOf((String) map.get("up"));
						Integer lus_id = Integer.valueOf((String) map.get("lus_id"));
						
						Map m = new HashMap();
						m.put("dist", 		"05");//05 agency, 08 marketing
						m.put("no_kartu", 			nomorKartu); 
						m.put("produk", 		"15");
						m.put("premi", 			premi);
						m.put("up", 			up);
						m.put("lus_id_input", 	lus_id);
						m.put("reg_spaj", 		null);
						m.put("product_code",	(String) map.get("product_code"));
						m.put("product_sub_code", (String) map.get("sub_code"));
						m.put("msag_id", 		"027789");
						m.put("keterangan", 	"BANK SINARMAS");
						insert("insertMstKartuPas", m);
						ok=true;
					}
				}catch(Exception e){
					logger.error("ERROR :", e);
				};
			}while (!ok);
		}
		if(hasil!=null) {
			uwDao.updateMstCounter(hasil, 197, "01");
		}
	}
	
	public void generateKartuHcp() throws DataAccessException{
		Integer generateKode=new Integer(1195); // 1:individu ; 195:plan HCP
		String nomorKartu = "";
		String generateKartu = "";
		int totalGenerate	= 10000;
		for(int i=0; i<totalGenerate; i++){
			//132 counter untuk U/W (NO HCP)
			Map a = uwDao.selectMstCounter(132,"01");
			Double hasil=(Double) a.get("MSCO_VALUE");
			Boolean ok = false;
			do{
				try{
					Integer counter=new Integer(hasil.intValue());
					String generateCounter=FormatString.rpad("0", counter.toString(), 6);
					generateKartu = CheckSum.generatePin6Digit(7);
					nomorKartu = generateKode.toString()+ generateKartu+ generateCounter;
					if(selectExistNoKartuHcp(nomorKartu) != null){
						ok=false;
					}else{
						Map m = new HashMap();
						m.put("dist", 		"01"); //INDIVIDU
						m.put("no_kartu", 			nomorKartu); 
						m.put("produk", 		"07"); //produk HCP di table mst_kartu_pas
						m.put("premi", 			(double) 0);
						m.put("lus_id_input", 	(int) 1436);// ANDY_IT
						m.put("reg_spaj", 		null);
						insert("insertMstKartuPas", m);
						counter+=1;
						bacDao.update_counter(counter.toString(), 132, "01");
						ok=true;
					}
				}catch(Exception e){};
			}while (!ok);
		}
	}
	
	public void generateKartuDbd() throws DataAccessException{
		Calendar tgl_sekarang = Calendar.getInstance();
//		Integer generateYear=new Integer(tgl_sekarang.get(Calendar.YEAR));
//		Integer generateYear=new Integer(2110); // Utk U/W sebelumnya 2010
//		Integer generateYear=new Integer(2011); // Utk Agency
		Integer generateYear=new Integer(2012); // Utk Marketing
		String nomorKartu = "";
		String generateKartu = "";
		int totalGenerate	= 10000;
		for(int i=0; i<totalGenerate; i++){
			Map a = uwDao.selectMstCounter(139,"01");
			Double hasil=(Double) a.get("MSCO_VALUE");
			Boolean ok = false;
			do{
				try{
					Integer counter=new Integer(hasil.intValue());
					String generateCounter=FormatString.rpad("0", counter.toString(), 6);;
					generateKartu = CheckSum.generateSimasCard6Digit(7);
					nomorKartu = generateYear.toString()+ generateKartu+ generateCounter;
					String jumlahKartu = selectExistNoKartuDbd(nomorKartu);
					if(selectExistNoKartuDbd(nomorKartu) != null){
						ok=false;
					}else{
						Map m = new HashMap();
						m.put("dist", 		"05");//05 agency, 08 marketing
						m.put("no_kartu", 			nomorKartu); 
						m.put("produk", 		"09");//09 DBD
						m.put("premi", 			(double) 0);
						m.put("lus_id_input", 	(int) 1436);
						m.put("reg_spaj", 		null);
						insert("insertMstKartuPas", m);
						counter+=1;
						bacDao.update_counter(counter.toString(), 139, "01");
						ok=true;
					}
				}catch(Exception e){};
			}while (!ok);
		}
	}
	
	public void generateKartuPasBp() throws DataAccessException{
		Calendar tgl_sekarang = Calendar.getInstance();
//		Integer generateYear=new Integer(tgl_sekarang.get(Calendar.YEAR));
//		Integer generateYear=new Integer(2110); // Utk U/W sebelumnya 2010
//		Integer generateYear=new Integer(2011); // Utk Agency
		Integer generateYear=new Integer(0112); // permintaan Mba Arin
		String nomorKartu = "";
		String generateKartu = "";
		int totalGenerate	= 3000;
		for(int i=0; i<totalGenerate; i++){
			Map a = uwDao.selectMstCounter(141,"01");
			Double hasil=(Double) a.get("MSCO_VALUE");
			Boolean ok = false;
			do{
				try{
					Integer counter=new Integer(hasil.intValue());
					String generateCounter=FormatString.rpad("0", counter.toString(), 6);;
					generateKartu = CheckSum.generateSimasCard6Digit(7);
					nomorKartu = generateYear.toString()+ generateKartu+ generateCounter;
					String jumlahKartu = selectExistNoKartuPasBp(nomorKartu);
					if(selectExistNoKartuPasBp(nomorKartu) != null){
						ok=false;
					}else{
						Map m = new HashMap();
						m.put("dist", 		"05");//05 agency, 08 marketing
						m.put("no_kartu", 			nomorKartu); 
						m.put("produk", 		"10");//10 PAS BP
						m.put("premi", 			(double) 0);
						m.put("lus_id_input", 	(int) 1436);
						m.put("reg_spaj", 		null);
						insert("insertMstKartuPas", m);
						counter+=1;
						bacDao.update_counter(counter.toString(), 141, "01");
						ok=true;
					}
				}catch(Exception e){};
			}while (!ok);
		}
	}
	
	public Boolean sendManualSoftcopyPas(User currentUser) throws DataAccessException{
		List<Map> list_spaj = polispasKetinggalanSoftcopy();
		for(int i=0;i<list_spaj.size();i++){
			Map map_spaj = list_spaj.get(i);
			String reg_spaj =(String) map_spaj.get("REG_SPAJ");
			Map m = uwDao.selectInformasiEmailSoftcopy(reg_spaj);
			String email = (String) m.get("MSPE_EMAIL");
					//uwDao.insertMstPositionSpaj(currentUser.getLus_id(), "Kirim Softcopy Otomatis", reg_spaj, 1000);
			Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);			
				if(email!=null){
					if(matcher.find()){
						Boolean EmailOtomatis =softcopy.softCopyOtomatis(reg_spaj, currentUser, email);
						if(EmailOtomatis==false){
							return false;
						}						
					}
				}
		}
		return true;
	}
	
	public List polispasKetinggalanSoftcopy() throws DataAccessException{
		return query("polispasKetinggalanSoftcopy", null);
	}
	
	//Yusuf (6 Jul 2010) - Fungsi untuk generate nomor polis, nomor kartu, nomor buku PAS
	public void generateNomorPAS() throws DataAccessException{
		
		List<String> pinPerdana	= new ArrayList<String>();
		List<String> pinSingle	= new ArrayList<String>();
		List<String> pinCeria 	= new ArrayList<String>();
		List<String> pinIdeal 	= new ArrayList<String>();
		
		List<String> kartuAgency	= new ArrayList<String>();
		List<String> kartuDMTM		= new ArrayList<String>();

		int totalPerdana	= 500;
		int totalSingle 	= 2000;
		int totalCeria 		= 2000;
		int totalIdeal 		= 500;
		
		/**
		 * Update counter klo habis generate lagi cek di
		 * select max(no_buku) from eka.mst_buku_pas  where produk=04 order by no_buku
		 * lihat 5 digit terakhir 
		 */
		
		int countBukuPerdana=1000;
		int countBukuSingle =1500;
		int countBukuCeria  =1500;
		int countBukuIdeal  =1000;
		
		int totalAgency	= 5000;
		int totalDMTM	= 1000;
		
		//1. generate PIN untuk 4 produk
		for(int i=0; i<totalPerdana; i++){
			String pin = "";
			
			do{
				pin = CheckSum.generatePin8Digit(1);
			}while (pinPerdana.contains(pin) || pinSingle.contains(pin) || pinCeria.contains(pin) || pinIdeal.contains(pin)||selectCekPinExist("01"+pin)||!CheckSum.cekPin2(pin,"01"));
			
			logger.info("PERDANA: " + FormatString.rpad("0", String.valueOf(i), 4) + " = " + pin);
			pinPerdana.add(pin);
		}
		for(int i=0; i<totalSingle; i++){
			String pin = "";
			do{
				pin = CheckSum.generatePin8Digit(2);
			}while (pinPerdana.contains(pin) || pinSingle.contains(pin) || pinCeria.contains(pin) || pinIdeal.contains(pin)||selectCekPinExist("02"+pin)||!CheckSum.cekPin2(pin,"02"));
			
			logger.info("SINGLE: " + FormatString.rpad("0", String.valueOf(i), 4) + " = " + pin);
			pinSingle.add(pin);
		}
		for(int i=0; i<totalCeria; i++){
			String pin = "";
			do{
				pin = CheckSum.generatePin8Digit(3);
			}while (pinPerdana.contains(pin) || pinSingle.contains(pin) || pinCeria.contains(pin) || pinIdeal.contains(pin)||selectCekPinExist("03"+pin)||!CheckSum.cekPin2(pin,"03"));
			
			logger.info("CERIA: " + FormatString.rpad("0", String.valueOf(i), 4) + " = " + pin);
			pinCeria.add(pin);
		}
		for(int i=0; i<totalIdeal; i++){
			String pin = "";
			do{
				pin = CheckSum.generatePin8Digit(4);
			}while (pinPerdana.contains(pin) || pinSingle.contains(pin) || pinCeria.contains(pin) || pinIdeal.contains(pin)||selectCekPinExist("04"+pin)||!CheckSum.cekPin2(pin,"04"));
			
			logger.info("IDEAL: " + FormatString.rpad("0", String.valueOf(i), 4) + " = " + pin);
			pinIdeal.add(pin);
		}
		
		
		//2. insert ke MST_BUKU_PAS (including nomor buku + nomor PIN)
		for(int i=0; i<totalPerdana; i++){
			Map m = new HashMap();
			m.put("no_buku", 		"01" + FormatString.rpad("0", String.valueOf(i+countBukuPerdana+1), 5)); //contoh: 0100001
			m.put("pin", 			"01" + pinPerdana.get(i)); //contoh: 0100000001
			m.put("produk", 		"01");
			m.put("premi", 			(double) 15000);
			m.put("lus_id_input", 	(int) 0);
			m.put("reg_spaj", 		null);
			insert("insertMstBukuPas", m);
		}
		for(int i=0; i<totalSingle; i++){
			Map m = new HashMap();
			m.put("no_buku", 		"02" + FormatString.rpad("0", String.valueOf(i+countBukuSingle+1), 5)); //contoh: 0100001
			m.put("pin", 			"02" + pinSingle.get(i)); //contoh: 0100000001
			m.put("produk", 		"02");
			m.put("premi", 			(double) 30000);
			m.put("lus_id_input", 	(int) 0);
			m.put("reg_spaj", 		null);
			insert("insertMstBukuPas", m);
		}
		for(int i=0; i<totalCeria; i++){
			Map m = new HashMap();
			m.put("no_buku", 		"03" + FormatString.rpad("0", String.valueOf(i+countBukuCeria+1), 5)); //contoh: 0100001
			m.put("pin", 			"03" + pinCeria.get(i)); //contoh: 0100000001
			m.put("produk", 		"03");
			m.put("premi", 			(double) 50000);
			m.put("lus_id_input", 	(int) 0);
			m.put("reg_spaj", 		null);
			insert("insertMstBukuPas", m);
		}
		for(int i=0; i<totalIdeal; i++){
			Map m = new HashMap();
			m.put("no_buku", 		"04" + FormatString.rpad("0", String.valueOf(i+countBukuIdeal+1), 5)); //contoh: 0100001
			m.put("pin", 			"04" + pinIdeal.get(i)); //contoh: 0100000001
			m.put("produk", 		"04");
			m.put("premi", 			(double) 90000);
			m.put("lus_id_input", 	(int) 0);
			m.put("reg_spaj", 		null);
			insert("insertMstBukuPas", m);
		}
		
		
		//3. Generate No Kartu untuk Agency
		for(int i=0; i<totalAgency; i++){
			String noKartu = "";
			do{
				noKartu = CheckSum.generatePin12Digit(5);
			}while (kartuAgency.contains(noKartu) || kartuDMTM.contains(noKartu)||selectCekKartuExist("0510"+noKartu)||!CheckSum.cekKartu2(noKartu, "05"));
			logger.info("No Kartu Agency: " + FormatString.rpad("0", String.valueOf(i), 4) + " = " + noKartu);
			kartuAgency.add(noKartu);
		}
		
		
		//4. Generate No Kartu untuk DMTM
		for(int i=0; i<totalDMTM; i++){
			String noKartu = "";
			do{
				noKartu = CheckSum.generatePin12Digit(6);
			}while (kartuAgency.contains(noKartu) || kartuDMTM.contains(noKartu)||selectCekKartuExist("0110"+noKartu)||!CheckSum.cekKartu2(noKartu, "01"));
			logger.info("No Kartu DMTM: " + FormatString.rpad("0", String.valueOf(i), 4) + " = " + noKartu);
			kartuDMTM.add(noKartu);
		}
		
		
		//5. Insert ke MST_KARTU_PAS untuk Agency
		int count = 0;
		for(int i=0; i<totalAgency; i++){
			Map m = new HashMap();
			m.put("dist", 			"05");
			m.put("no_kartu", 		"0510" + kartuAgency.get(i));
			m.put("lus_id_input", 	0);
			m.put("reg_spaj", 		null);

			count++;
			
			if(i < totalPerdana){
				m.put("produk",	"01");
				m.put("premi", 	(double) 15000);
			}else if(i >= totalPerdana && i < totalPerdana+totalSingle){
				m.put("produk",	"02");
				m.put("premi", 	(double) 30000);
			}else if(i >= totalPerdana+totalSingle && i < totalPerdana+totalSingle+totalCeria){
				m.put("produk",	"03");
				m.put("premi", 	(double) 50000);
			}else{
				m.put("produk",	"04");
				m.put("premi", 	(double) 90000);
			}
			
			insert("insertMstKartuPas", m);
		}
		//6. Insert ke MST_KARTU_PAS untuk DMTM
		for(int i=0; i<totalDMTM; i++){
			Map m = new HashMap();
			m.put("dist", 			"01");
			m.put("no_kartu", 		"0110" + kartuDMTM.get(i));
			m.put("lus_id_input", 	0);
			m.put("reg_spaj", 		null);
			
			//ALL PRODUCT
//			if(i < 1000){
//				m.put("produk",	"01");
//				m.put("premi", 	(double) 15000);
//			}else if(i >= 1000 && i < 2500){
//				m.put("produk",	"02");
//				m.put("premi", 	(double) 30000);
//			}else if(i >= 2500 && i < 4000){
//				m.put("produk",	"03");
//				m.put("premi", 	(double) 50000);
//			}else{
//				m.put("produk",	"04");
//				m.put("premi", 	(double) 90000);
//			}
			
			//ONLY CERIA
			m.put("produk",	"03");
			m.put("premi", 	(double) 50000);
			
			insert("insertMstKartuPas", m);
		}
	}
	
	//ANDY (18 Mar 2014) - Fungsi untuk generate nomor polis, nomor kartu PAS SYARIAH
		public void generateNomorPASSyariah() throws DataAccessException{
			
			List<String> kartuAgency	= new ArrayList<String>();
			List<String> kartuDMTM		= new ArrayList<String>();

			int totalPerdana	= 1500;
			int totalSingle 	= 200;
			int totalCeria 		= 200;
			int totalIdeal 		= 100;
			
			int totalAgency	= 0;
			int totalDMTM	= 2000;
			
			//3. Generate No Kartu untuk Agency
			for(int i=0; i<totalAgency; i++){
				String noKartu = "";
				do{
					noKartu = CheckSum.generatePin12Digit(5);
				}while (kartuAgency.contains(noKartu) || kartuDMTM.contains(noKartu)||selectCekKartuExist("0510"+noKartu)||!CheckSum.cekKartu2(noKartu, "05"));
				logger.info("No Kartu Agency: " + FormatString.rpad("0", String.valueOf(i), 4) + " = " + noKartu);
				kartuAgency.add(noKartu);
			}
			
			
			//4. Generate No Kartu untuk DMTM
			for(int i=0; i<totalDMTM; i++){
				String noKartu = "";
				do{
					noKartu = CheckSum.generatePin12Digit(6);
				}while (kartuAgency.contains(noKartu) || kartuDMTM.contains(noKartu)||selectCekKartuExist("0110"+noKartu)||!CheckSum.cekKartu2(noKartu, "01"));
				logger.info("No Kartu DMTM: " + FormatString.rpad("0", String.valueOf(i), 4) + " = " + noKartu);
				kartuDMTM.add(noKartu);
			}
			
			
			//5. Insert ke MST_KARTU_PAS untuk Agency
			int count = 0;
			for(int i=0; i<totalAgency; i++){
				Map m = new HashMap();
				m.put("dist", 			"05");
				m.put("no_kartu", 		"0510" + kartuAgency.get(i));
				m.put("lus_id_input", 	0);
				m.put("reg_spaj", 		null);

				count++;
				
				if(i < totalPerdana){
					m.put("produk",	"01");
					m.put("premi", 	(double) 15000);
				}else if(i >= totalPerdana && i < totalPerdana+totalSingle){
					m.put("produk",	"02");
					m.put("premi", 	(double) 30000);
				}else if(i >= totalPerdana+totalSingle && i < totalPerdana+totalSingle+totalCeria){
					m.put("produk",	"03");
					m.put("premi", 	(double) 50000);
				}else{
					m.put("produk",	"04");
					m.put("premi", 	(double) 90000);
				}
				
				insert("insertMstKartuPas", m);
			}
			//6. Insert ke MST_KARTU_PAS untuk DMTM
			for(int i=0; i<totalDMTM; i++){
				Map m = new HashMap();
				m.put("dist", 			"01");
				m.put("no_kartu", 		"0110" + kartuDMTM.get(i));
				m.put("lus_id_input", 	1436);
				m.put("reg_spaj", 		null);
				
				
				if(i < totalPerdana){
					m.put("produk",	"11");
					m.put("premi", 	(double) 0);
				}else if(i >= totalPerdana && i < totalPerdana+totalSingle){
					m.put("produk",	"12");
					m.put("premi", 	(double) 0);
				}else if(i >= totalPerdana+totalSingle && i < totalPerdana+totalSingle+totalCeria){
					m.put("produk",	"13");
					m.put("premi", 	(double) 0);
				}else{
					m.put("produk",	"14");
					m.put("premi", 	(double) 0);
				}
				
				insert("insertMstKartuPas", m);
			}
		}
	
	/* Yusuf (08/03/2010) - Start of Travel Insurance */
	public TravelInsurance selectTravelInsuranceDet(int msti_id, int msti_jenis, int msid_no) throws DataAccessException{
		Map m = new HashMap();
		m.put("msti_id", msti_id);
		m.put("msti_jenis", msti_jenis);
		m.put("msid_no", msid_no);
		return (TravelInsurance) querySingle("selectTravelInsuranceDet", m);
	}
	
	public TravelInsurance selectValidasiTravelInsurance(int msti_id, int msti_jenis) throws DataAccessException{
		Map m = new HashMap();
		m.put("msti_id", msti_id);
		m.put("msti_jenis", msti_jenis);
		return (TravelInsurance) querySingle("selectValidasiTravelInsurance", m);
	}
	
	public List<TravelInsurance> selectTravelInsuranceDet(int msti_id, int msti_jenis) throws DataAccessException{
		Map m = new HashMap();
		m.put("msti_id", msti_id);
		m.put("msti_jenis", msti_jenis);
		return query("selectTravelInsuranceDet", m);
	}
	public double selectValidasiMaxUpTravelInsurance(TravelInsurance peserta) throws DataAccessException{
		Double hasil = (Double) querySingle("selectValidasiMaxUpTravelInsurance", peserta);
		return (hasil != null) ? hasil : 0;
	}
	public List<TravelInsurance> selectBandara() throws DataAccessException{
		return query("selectBandara", null);
	}
	public List<TravelInsurance> selectTravelInsurance(String lca_id, Integer posisi, Date tglAwal, Date tglAkhir) throws DataAccessException{
		Map m = new HashMap();
		m.put("lca_id", lca_id);
		m.put("posisi", posisi);
		m.put("tglAwal", tglAwal);
		m.put("tglAkhir", tglAkhir);
		return query("selectTravelInsurance", m);
	}
	public TravelInsurance selectTravelInsurance(int msti_id, int msti_jenis) throws DataAccessException{
		Map m = new HashMap();
		m.put("msti_id", msti_id);
		m.put("msti_jenis", msti_jenis);
		return (TravelInsurance) querySingle("selectTravelInsurance", m);
	}
	public String saveTravelInsurance(Command command, User currentUser) throws DataAccessException{
		List<TravelInsurance> listTravelIns = command.getDaftarTravelIns();
		String hasil = "Tidak ada data yang diupdate. Harap konfirmasi dengan IT.";
		for(int i=0; i<listTravelIns.size(); i++){
			TravelInsurance t = listTravelIns.get(i);
			
			//SAVE 
			if(t.flag.intValue() == 1){
				//MST_TRAVEL_INS
				t.msti_jenis 		= 1;
				t.lca_id 			= currentUser.getLca_id();
				t.msti_posisi 		= 1;
				t.msti_premi 		= (double) t.msti_premi_setor * 2;
				t.msti_up			= (double) t.msti_premi_setor * 10000;
				t.msti_msag_id 		= "502895"; //JOHAN (MBA ANNA YULIA 23/03/2010) 
				t.lus_id_input 		= Integer.valueOf(currentUser.getLus_id());
				t.mspt_lus_id 		= Integer.valueOf(currentUser.getLus_id());
				
				if(t.msti_id == null){ 
					t.msti_id = (Integer) querySingle("selectIdTravelInsurance", t.msti_jenis);
					insert("insertMstTravelIns", t);
					t.mspt_desc = "INPUT DATA GLOBAL";
					insert("insertMstPositionTravelIns", t);
				}else{
					update("updateTravelIns", t);
					t.mspt_desc = "EDIT DATA GLOBAL";
					insert("insertMstPositionTravelIns", t);
				}
				
				hasil = "Data berhasil disimpan";
				break;
				
			//TRANSFER
			}else if(t.flag.intValue() == 2){
				t.msti_posisi = 2;
				update("updateTravelIns", t);
				t.mspt_lus_id = Integer.valueOf(currentUser.getLus_id());
				t.mspt_desc = "TRANSFER DATA GLOBAL KE FILLING";
				insert("insertMstPositionTravelIns", t);
				hasil = "Data berhasil ditransfer ke Filling";
				break;
				
			//AKSEPTASI
			}else if(t.flag.intValue() == 3){
				Date sysdate = commonDao.selectSysdate();
				//TODO penjurnalannya gimana?
				//t.no_pre
				//t.no_voucher
				t.msti_tgl_prod 	= t.msti_tgl;
				t.msti_tgl_aksep 	= sysdate;
				t.lus_id_aksep 		= Integer.valueOf(currentUser.getLus_id());
				t.msti_komisi		= 0.2 * t.msti_premi_setor; //20% dari premi yg disetor, atau 10% dari total premi;
				t.msti_kom_tax		= komisi.f_load_tax(t.msti_komisi, t.msti_tgl_prod, t.msti_msag_id);
				update("updateTravelIns", t);
				t.mspt_lus_id = Integer.valueOf(currentUser.getLus_id());
				t.mspt_desc = "AKSEPTASI DATA GLOBAL";
				insert("insertMstPositionTravelIns", t);
				hasil = "Data berhasil diakseptasi";
				break;
			}
		}
		return hasil;
	}
	
	private void jurnalNewBusinessTravelInsurance(TravelInsurance travelIns) throws DataAccessException{
		
		//variable2 yang dibutuhkan
		String polis_induk = "01-0410-039"; //format : ket <KODE BANDARA> <POLIS INDUK>
		int counter = 0;
		String tanggalRK = defaultDateFormatStripes.format(travelIns.msti_tgl);
		
		//proses generate nomor voucher
		Premi premi = new Premi();
		premi.setRek_id(0); //TODO : bank ekalife nya yang mana?
		try {
			premi = sequence.sequenceVoucherPremiIndividu(premi, null, null, 1, null);
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
		
		//MST_TBANK
		
		TBank tbank = new TBank();
		tbank.no_pre 			= this.uwDao.selectGetPacGl("nopre");//sequence.sequenceNo_pre(32);
		tbank.position 			= 2;
		tbank.tgl_input 		= commonDao.selectSysdate();
		tbank.tgl_jurnal 		= travelIns.msti_tgl_prod;
		tbank.tgl_rk 			= travelIns.msti_tgl;
		tbank.no_voucher 		= premi.getLs_no_voucher();
		tbank.kas 				= "M";
		tbank.jumlah 			= travelIns.msti_premi;
		tbank.lus_id 			= String.valueOf(travelIns.lus_id_aksep);
		tbank.mtb_gl_no 		= premi.getAccno(); //NO GL DARI LST_REK_EKALIFE
		accountingDao.insertMstTBank(tbank);

		//SISI DEBET (MST_DBANK dan MST_BVOUCHER) - 1 ROW
		
		counter = 1;
		
		DBank dbank = new DBank();
		dbank.no_pre 			= tbank.no_pre;
		dbank.no_jurnal 		= counter;
		dbank.keterangan 		= "BANK " + tanggalRK;
		dbank.kas 				= "M";
		dbank.jumlah 			= travelIns.msti_premi;
		//dbank.kode_cash_flow 	= null; //TODO : DIISI GAK?
		accountingDao.insertMstDBank(dbank);

		BVoucher bvoucher = new BVoucher();
		bvoucher.no_pre 		= tbank.no_pre;
		bvoucher.no_jurnal 		= counter;
		bvoucher.keterangan 	= "BK " + tanggalRK;
		bvoucher.debet 			= travelIns.msti_premi;
		bvoucher.kredit 		= (double) 0;
		bvoucher.project_no 	= tbank.mtb_gl_no.substring(0, 3);
		bvoucher.budget_no 		= tbank.mtb_gl_no.substring(3);
		bvoucher.type_trx 		= 1;
		//bvoucher.key_jurnal 	= null; //TODO : DIISI GAK?
		accountingDao.insertMstBVoucher(bvoucher);

		//SISI KREDIT (MST_DBANK dan MST_BVOUCHER) - row 1 dari 2
		
		counter = 2;
		
		dbank = new DBank();
		dbank.no_pre 			= tbank.no_pre;
		dbank.no_jurnal 		= counter;
		dbank.keterangan 		= "DISC PA-RISIKO A " + travelIns.lsb_code + " " + tanggalRK;
		dbank.kas 				= "B";
		dbank.jumlah 			= travelIns.msti_premi - travelIns.msti_premi_setor;
		//dbank.kode_cash_flow 	= null; //TODO : DIISI GAK?
		accountingDao.insertMstDBank(dbank);

		bvoucher = new BVoucher();
		bvoucher.no_pre 		= tbank.no_pre;
		bvoucher.no_jurnal 		= counter;
		bvoucher.keterangan 	= "DISC PA-RISIKO A " + travelIns.lsb_code + " " + tanggalRK;
		bvoucher.debet 			= (double) 0;
		bvoucher.kredit 		= dbank.jumlah;
		bvoucher.project_no 	= tbank.mtb_gl_no.substring(0, 3);
		bvoucher.budget_no 		= tbank.mtb_gl_no.substring(3);
		bvoucher.type_trx 		= 1;
		//bvoucher.key_jurnal 	= null; //TODO : DIISI GAK?
		accountingDao.insertMstBVoucher(bvoucher);
		
		//SISI KREDIT (MST_DBANK dan MST_BVOUCHER) - row 2 dari 2

		counter = 3;
		
		dbank = new DBank();
		dbank.no_pre 			= tbank.no_pre;
		dbank.no_jurnal 		= counter;
		dbank.keterangan 		= "PP PA-RISIKO A " + travelIns.lsb_code + " " + tanggalRK;
		dbank.kas 				= "B";
		dbank.jumlah 			= travelIns.msti_premi_setor;
		//dbank.kode_cash_flow 	= null; //TODO : DIISI GAK?
		accountingDao.insertMstDBank(dbank);

		bvoucher = new BVoucher();
		bvoucher.no_pre 		= tbank.no_pre;
		bvoucher.no_jurnal 		= counter;
		bvoucher.keterangan 	= "PP PA-RISIKO A " + travelIns.lsb_code + " " + tanggalRK;
		bvoucher.debet 			= (double) 0;
		bvoucher.kredit 		= dbank.jumlah;
		bvoucher.project_no 	= tbank.mtb_gl_no.substring(0, 3);
		bvoucher.budget_no 		= tbank.mtb_gl_no.substring(3);
		bvoucher.type_trx 		= 1;
		//bvoucher.key_jurnal 	= null; //TODO : DIISI GAK?
		accountingDao.insertMstBVoucher(bvoucher);
		
	}
	
	public String saveTravelInsuranceDet(Command command, User currentUser) throws DataAccessException{
		String hasil = "Data tidak berhasil disimpan. Harap konfirmasi dengan IT.";
		TravelInsurance peserta = command.getPeserta();
		
		if("save".equals(command.getSubmitMode())){
			peserta.lus_id 			= Integer.valueOf(currentUser.getLus_id());
			peserta.msid_premi 		= (double) peserta.msid_premi_setor * 2;
			peserta.msid_up			= (double) peserta.msid_premi_setor * 10000;
			peserta.msid_end_date 	= FormatDate.add(peserta.msid_beg_date, Calendar.DATE, 3);
			
			//INSERT BARU
			if(peserta.getMsid_no().intValue() == -1){
				insert("insertMstTravelInsDet", peserta);
			//UPDATE DATA
			}else{
				update("updateTravelInsDet", peserta);
			}
			
			peserta.lsp_id_lama = 2;
			peserta.lsp_id_baru = 3;
			peserta.msf_id = (String) querySingle("selectFormBlankoTravelIns", peserta);
			update("updateBlankoTravelIns", peserta);
			
			hasil = "Data berhasil disimpan";
			
		}else if("delete".equals(command.getSubmitMode())){
			peserta = new TravelInsurance();
			peserta.setMsti_id(command.getMsti_id());
			peserta.setMsti_jenis(command.getMsti_jenis());
			peserta.setMsid_no(command.getMsid_no());
			delete("deleteTravelInsDet", peserta);
			
			peserta.lsp_id_lama = 3;
			peserta.lsp_id_baru = 2;
			peserta.msf_id = (String) querySingle("selectFormBlankoTravelIns", peserta);			
			update("updateBlankoTravelIns", peserta);
			
			hasil = "Data berhasil dihapus";
		}
		
		return hasil;
	}
	/* Yusuf (08/03/2010) - End of Travel Insurance */

	/* Yusuf (11/02/2010) - Start of Validasi Random Sample BII */
	
	public void insertMstQuestionnaire(Questionnaire q) throws DataAccessException{
		insert("insertMstQuestionnaire", q);
	}
	
	public void deleteMstQuestionnaire(String reg_spaj) throws DataAccessException{
		delete("delete.mst_questionnaire", reg_spaj);
	}
	
	public List<Questionnaire> selectQuestionnaireFromSpaj(int lsqu_jenis, String reg_spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("lsqu_jenis", lsqu_jenis);
		m.put("reg_spaj", reg_spaj);
		return query("selectQuestionnaireFromSpaj", m);
	}
	
	public String selectReferrerIdBiiFromSpaj(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectReferrerIdBiiFromSpaj", reg_spaj);
	}
	
	public List<Map> selectJumlahTutupanBiiPerReferrer(String referrer) throws DataAccessException{
		return query("selectJumlahTutupanBiiPerReferrer", referrer);
	}
	
	public Date selectBegDateInsuredFromSpaj(String reg_spaj) throws DataAccessException{
		return (Date) querySingle("selectBegDateInsuredFromSpaj", reg_spaj);
	}
	
	/* Yusuf (11/02/2010) - End of Validasi Random Sample BII */
	
	public double selectSumPtkp(String as_msag, Date ldt_beg_mon, Date ldt_beg_year) throws DataAccessException{
		Map m = new HashMap();
		m.put("as_msag", as_msag);
		m.put("ldt_beg_mon", ldt_beg_mon);
		m.put("ldt_beg_year", ldt_beg_year);
		return (Double) querySingle("selectSumPtkp", m);
	}
	
	public AgentTax selectTotalAgentTax(AgentTax agentTax, Date ldt_beg_mon, Date ldt_beg_year, Date ldt_end_mon) throws DataAccessException{
		Map m = new HashMap();
		m.put("as_msag", agentTax.as_msag);
		m.put("ldt_beg_mon", ldt_beg_mon);
		m.put("ldt_beg_year", ldt_beg_year);
		m.put("ldt_end_mon", ldt_end_mon);
		Map result = (Map) querySingle("selectTotalAgentTax", m);
		if(result != null){
			agentTax.adec_comm_month 	= ((BigDecimal) result.get("ADEC_COMM_MONTH")).doubleValue();
			agentTax.adec_comm_year 	= ((BigDecimal) result.get("ADEC_COMM_YEAR")).doubleValue();
			agentTax.adec_pkp_month 	= ((BigDecimal) result.get("ADEC_PKP_MONTH")).doubleValue();
			agentTax.adec_pkp_year 		= ((BigDecimal) result.get("ADEC_PKP_YEAR")).doubleValue();
			agentTax.adec_tax_paid 		= ((BigDecimal) result.get("ADEC_TAX_PAID")).doubleValue();
			agentTax.adec_ptkp			= 0.;
		}else{
			agentTax.adec_comm_month 	= 0.;
			agentTax.adec_comm_year 	= 0.;
			agentTax.adec_pkp_month 	= 0.;
			agentTax.adec_pkp_year 		= 0.;
			agentTax.adec_tax_paid 		= 0.;
			agentTax.adec_ptkp			= 0.;
		}
		return agentTax;
	}
	
	public double selectPtkp(AgentTax agentTax) throws DataAccessException{
		Double result = (Double) querySingle("selectPtkp", agentTax);
		return result==null?0.:result;
	}
	
	public int selectCountAgentTax(AgentTax agentTax) throws DataAccessException{
		return (Integer) querySingle("selectCountAgentTax", agentTax);
	}
	
	public AgentTax selectAgentTax(AgentTax agentTax) throws DataAccessException{
		return (AgentTax) querySingle("selectAgentTax", agentTax);
	}
	
	public boolean selectDapatSwineFlu(String spaj) throws DataAccessException{
		int result = (Integer) querySingle("selectDapatSwineFlu", spaj);
		if(result == 0) return false; else return true;
	}
	
	public Map selectPolisLamaSurrenderEndorsement(String reg_spaj) throws DataAccessException{
		return (Map) querySingle("selectPolisLamaSurrenderEndorsement",reg_spaj);
	}

	public int selectAksesAdminTerhadapAgen(String lus_id, String msag_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("lus_id", lus_id);
		m.put("msag_id", msag_id);
		return (Integer) querySingle("selectAksesAdminTerhadapAgen", m);
	}
	
	public List<Map> selectSlipPajakPerAgen(String msag_id, String yyyymm) throws DataAccessException{
		Map map = new HashMap();
		map.put("msag_id", msag_id);
		map.put("yyyymm", yyyymm);
		return query("selectSlipPajakPerAgen", map);
	}
	
	public List<Map> selectSlipPajakPerAgenTaxRev(String msag_id, String yyyymm) throws DataAccessException{
		Map map = new HashMap();
		map.put("msag_id", msag_id);
		map.put("yyyymm", yyyymm);
		return query("selectSlipPajakPerAgenTaxRev", map);
	}
	
	public List<Map> selectSlipPajakPerAdminCabangTaxRev(int lus_id, String yyyymm) throws DataAccessException{
		Map map = new HashMap();
		map.put("lus_id", lus_id);
		map.put("yyyymm", yyyymm);
		return query("selectSlipPajakPerAdminCabangTaxRev", map);
	}
	
	public List<Map> selectSlipPajakPerAdminCabang(int lus_id, String yyyymm) throws DataAccessException{
		Map map = new HashMap();
		map.put("lus_id", lus_id);
		map.put("yyyymm", yyyymm);
		return query("selectSlipPajakPerAdminCabang", map);
	}
	
	/**
	 * Proses untuk insert permohonan pengiriman spaj dari pusat ke cabang
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 23, 2007 (4:56:17 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processNewFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		FormSpaj f = cmd.getDaftarFormSpaj().get(0);
		if("".equals(f.getMsf_id())) { //INSERT BARU
			
			/** 1. Insert stok spaj di cabang tersebut walaupun masih 0, dan insert form spaj */
			String seq = sequence.sequenceMst_form(0, currentUser.getLca_id());
			for(FormSpaj s : cmd.getDaftarFormSpaj()) {
				s.setMsf_id(seq);
				s.setMss_jenis(0);
				s.setLca_id(currentUser.getLca_id());
				s.setMsab_id(0); //AGENT 000000
				s.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				//no_blanko tidak boleh diisi saat baru minta
				s.setStart_no_blanko(null);
				s.setEnd_no_blanko(null);
				if(cmd.getBmi_id() != null)
					s.setBmi_id(cmd.getBmi_id());
				if(cmd.getAdmTravIns() != null) 
					s.setTrav_ins_type(cmd.getAdmTravIns());
				if(((Integer) querySingle("selectCekSpaj", s))==0) {
					Spaj spaj = new Spaj();
					spaj.newSpaj(0, s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 0, s.getLus_id(),s.getLus_id());
					insert("insertNewSpaj", spaj);
				}
				insert("insertNewFormSpaj", s);
			}
			cmd.setMsf_id(seq);

			/** 2. Insert history form spaj */
			FormHist form = new FormHist();
			form.setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
			form.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			form.setPosisi(0);
			form.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			form.setMsfh_desc("Permintaan oleh Branch Admin");			
			insert("insertFormHistory", form);
			
			//permintaan yusy email ke bas
			String to[] = new String[]{"bas.sinarmasmsiglife.co.id"};
			String subject="Permintaan SPAJ from Cabang";
			String message="Form Permintaan SPAJ oleh Cabang "+cmd.getDaftarFormSpaj().get(0).getLca_nama()+ 
							" dengan no permintaan " + cmd.getDaftarFormSpaj().get(0).getMsf_id() +
							"\nDiharapkan untuk segera ditindaklanjuti";
			try {
//				email.send(
//						true, (currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava")),
//						to, null, null, subject, message, null);
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
						currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava"), to, null, null, 
						subject, message, null, null);
			} catch (MailException e) {
				logger.error("ERROR :", e);
			}
			
			return 
				"Permintaan anda sudah tersimpan dengan nomor " + cmd.getMsf_id() 
				+ ". \\nApabila sudah di-approve oleh Agency Support / BAS, maka SPAJ akan dikirimkan oleh General Affairs";
		} else { //UPDATE DATA LAMA
			
			/** 1. Update jumlah pada form spaj */
			for(FormSpaj s : cmd.getDaftarFormSpaj()) {
				update("updateFormSpaj", s);
			}
			
			/** 2. Insert history form */
			FormHist form = new FormHist();
			form.setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
			form.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			form.setPosisi(0);
			form.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			form.setMsfh_desc("Perubahan Permintaan oleh Branch Admin");			
			insert("insertFormHistory", form);
			
			return "Data Permintaan anda sudah di-update.";
		}
				
	}
	
	/**
	 * Proses untuk pembatalan permohonan pengiriman spaj dari pusat ke cabang
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 23, 2007 (4:56:42 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processCancelFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		
		/** 1. update form spaj */
		for(FormSpaj s : cmd.getDaftarFormSpaj()) {
			s.setMsf_amount(0);
			s.setPosisi(2); //cancel
			update("updateFormSpaj", s);
		}
		
		/** 2. insert history form */
		if(!cmd.getDaftarFormSpaj().isEmpty()) {
			FormHist f = new FormHist();
			f.setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
			f.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			f.setPosisi(2);
			f.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			f.setMsfh_desc("Dibatalkan oleh Branch Admin");			
			insert("insertFormHistory", f);
		}
		
		return "Data Permintaan anda sudah dibatalkan.";
	}
	
	/**
	 * Proses untuk persetujuan permohonan pengiriman spaj dari pusat ke cabang,
	 * oleh agency support / BAS
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 23, 2007 (4:56:42 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 * @throws MessagingException 
	 * @throws MailException 
	 */
	public String processApprovalFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException{
		Boolean isSimCard = false;
		
		for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
			FormSpaj s = cmd.getDaftarFormSpaj().get(i);
			s.setPosisi(1); //approve
			
			if(s.getLsjs_id() == 21 && s.getMsf_amount_req() > 0) isSimCard = true;
			
			/** 1. update form spaj */
			update("updateFormSpaj", s);
		}
		
		/** 2. insert form history */
		cmd.getFormHist().setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
		cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getFormHist().getMsf_id()));
		cmd.getFormHist().setPosisi(1);
		cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		insert("insertFormHistory", cmd.getFormHist());
		
		//email ke ga
		User userCabang=selectLstUser(cmd.getDaftarFormSpaj().get(0).getLus_id());
		String to[]=props.getProperty("bas.email.approve.to").split(";");
		String cc[]=new String[]{userCabang.getEmail()};
		String subject="Approved SPAJ from BAS";
		String message="Form Permintaan SPAJ oleh Cabang "+cmd.getDaftarFormSpaj().get(0).getLca_nama()+ 
						" dengan no permintaan " + cmd.getDaftarFormSpaj().get(0).getMsf_id() +
						" telah disetujui.\nDiharapkan GA untuk mengirimkan SPAJ tersebut secepatnya";
		try {
//			email.send(
//					true, (currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava")),
//					to, cc, null, subject, message, null);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava"), to, cc, null, 
					subject, message, null, null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		}
		
		if(isSimCard) {
			to = new String[]{"Timmy@sinarmasmsiglife.co.id","Dewi_Andriyati@sinarmasmsiglife.co.id","Sutini@sinarmasmsiglife.co.id"};
			message="Form Permintaan SPAJ oleh Cabang "+cmd.getDaftarFormSpaj().get(0).getLca_nama()+ 
			" dengan no permintaan " + cmd.getDaftarFormSpaj().get(0).getMsf_id() +
			" telah disetujui.\nSilahkan UW melakukan proses selanjutnya";
			try {
//				email.send(
//						false, (currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava")),
//						to, null, null, subject, message, null);
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
						currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava"), to, null, null, 
						subject, message, null, null);
			} catch (MailException e) {
				logger.error("ERROR :", e);
			}			
		}
		
		return 
			"Data permintaan cabang sudah disetujui. "
			+ ". \\nPermintaan akan selanjutnya diproses oleh General Affairs"
			+	"\\nSetelah General Affairs melakukan pengiriman, harap lengkapi detail pengiriman di menu [BAS] PENGIRIMAN SPAJ.";
	}
	
	/**
	 * Proses untuk penolakan permohonan pengiriman spaj dari pusat ke cabang,
	 * oleh agency support / BAS
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 23, 2007 (4:56:42 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processRejectFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
			FormSpaj s = cmd.getDaftarFormSpaj().get(i);
			s.setPosisi(3); //reject
			
			/** 1. update form spaj */
			update("updateFormSpaj", s);
		}

		/** 2. insert form history */
		cmd.getFormHist().setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
		cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getFormHist().getMsf_id()));
		cmd.getFormHist().setPosisi(3);
		cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		insert("insertFormHistory", cmd.getFormHist());
		
		//permintaan dari yusy
		User userCabang=selectLstUser(cmd.getDaftarFormSpaj().get(0).getLus_id());
		String to[]=new String[]{userCabang.getEmail()};
		String subject="Reject SPAJ from BAS";
		String message="Form Permintaan SPAJ oleh Cabang "+cmd.getDaftarFormSpaj().get(0).getLca_nama()+ 
						" dengan no permintaan " + cmd.getDaftarFormSpaj().get(0).getMsf_id() +
						" telah ditolak.";
		try {
//			email.send(
//					true, props.getProperty("admin.ajsjava"),
//					to, null, null, subject, message, null);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), to, null, null, 
					subject, message, null, null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		}

		return 
			"Data permintaan cabang sudah ditolak. "
			+ ". \\nAlasan penolakan akan diteruskan ke cabang bersangkutan.";
	}
	
	/**
	 * Proses yang menandakan bahwa permohonan pengiriman spaj dari pusat ke cabang
	 * sudah dilakukan oleh GA
	 * 
	 * @author Yusuf Sutarko, Rahmayanti, Ryan
	 * @since Feb 23, 2007 (4:56:42 PM), Nov 18 11,2014
	 * @param cmd
	 * @param currentUser
	 * @return
	 * @throws MessagingException 
	 * @throws MailException 
	 */
	public String processSendFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
			FormSpaj s = cmd.getDaftarFormSpaj().get(i);
			s.setStart_no_blanko(FormatString.rpad("0", s.getStart_no_blanko(), 6));
			s.setEnd_no_blanko(FormatString.rpad("0", s.getEnd_no_blanko(), 6));
			s.setPosisi(4); //sent
			String noBlanko1[]=s.getNo_blanko_req().split(";");
			String noBlanko2[]=new String[noBlanko1.length];
			String hasilNoBlanko="";
			for(int k=0;k<noBlanko1.length;k++){
				if(noBlanko1[k].contains("-")) {
					noBlanko2=noBlanko1[k].split("-");
					for(int j = 0; j <noBlanko2.length; j++) {
						logger.info(noBlanko2.length);
						if(j+1==noBlanko2.length)
							hasilNoBlanko=hasilNoBlanko+FormatString.rpad("0", noBlanko2[j], 6);
						else
							hasilNoBlanko=hasilNoBlanko+FormatString.rpad("0", noBlanko2[j], 6)+"-";

					}					
				}
				else if(!noBlanko1[k].equals("")) {
					logger.info(noBlanko1.length);
					logger.info(noBlanko1[k]);
					hasilNoBlanko=hasilNoBlanko+FormatString.rpad("0", noBlanko1[k], 6);
					logger.info(hasilNoBlanko);
				}
				else hasilNoBlanko = "0";
				if(k+1!=noBlanko1.length)
					hasilNoBlanko+=";";
			}
			if(noBlanko2.length==1 && noBlanko2[0] != null)
				hasilNoBlanko=hasilNoBlanko+FormatString.rpad("0", noBlanko2[0], 6);
			s.setNo_blanko_req(hasilNoBlanko);
			/** 1. update form spaj */
			update("updateFormSpaj", s);
			
			/** 2. insert detail setiap blanko ke mst_spaj_det */
			if(s.getMsf_amount_req()>0) {
//				int start = Integer.valueOf(s.getStart_no_blanko());
//				int end = Integer.valueOf(s.getEnd_no_blanko());
//				for(int j = start; j <=end; j++) {
//					SpajDet spajDet = new SpajDet(s.getMsf_id(), s.getMsf_id(),s.getMsf_id(), s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 
//							0, // = dikirim 
//							FormatString.rpad("0", String.valueOf(j), 6), null, Integer.valueOf(currentUser.getLus_id()),null);
//					insert("insertSpajDet", spajDet);
//				}
				
				//** 2a. update lst_jenis_spaj_hist *//*
				insertSpajHist(s);
				updatePenguranganSpajUw(s);
				
				logger.info("iseng");
				String noBlankoReq1[]=s.getNo_blanko_req().split(";");
				String noBlankoReq2[]=new String[noBlankoReq1.length];
				for(int k=0;k<noBlankoReq1.length;k++){
					if(noBlankoReq1[k].contains("-")) {
						noBlankoReq2=noBlankoReq1[k].split("-");
						Long start=Long.parseLong(noBlankoReq2[0]);
						Long end=Long.parseLong(noBlankoReq2[1]);
						
						logger.info(start.intValue());						
						for(Long j = start; j <=end; j++) {
							//insert spaj baru dengan no blanko 12 digit
							if(s.getLsjs_id()==94||s.getLsjs_id()==95||s.getLsjs_id()==96||s.getLsjs_id()==97||s.getLsjs_id()==98||s.getLsjs_id()==99||s.getLsjs_id()==100||s.getLsjs_id()==101||s.getLsjs_id()==102||s.getLsjs_id()==103) {
								SpajDet spajDet = new SpajDet(s.getMsf_id(), s.getMsf_id(),s.getMsf_id(), s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 
										0, // = dikirim 
										FormatString.rpad("0", String.valueOf(j), 12), null, Integer.valueOf(currentUser.getLus_id()),null);
								insert("insertSpajDet", spajDet);	
							}
							//insert spaj lama dengan no blanko 6 digit
							else
							{
								SpajDet spajDet = new SpajDet(s.getMsf_id(), s.getMsf_id(),s.getMsf_id(), s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 
										0, // = dikirim 
										FormatString.rpad("0", String.valueOf(j), 6), null, Integer.valueOf(currentUser.getLus_id()),null);
								insert("insertSpajDet", spajDet);
							}

						}							

					}
					else {
						//insert spaj baru dengan no blanko 12 digit
						if(s.getLsjs_id()==94||s.getLsjs_id()==95||s.getLsjs_id()==96||s.getLsjs_id()==97||s.getLsjs_id()==98||s.getLsjs_id()==99||s.getLsjs_id()==100||s.getLsjs_id()==101||s.getLsjs_id()==102||s.getLsjs_id()==103) {
							//insert spaj umum(new) dan tambahan(new) sebanyak spaj yang disetujui
							if(s.getLsjs_id()==94 || s.getLsjs_id()==96){
								for(int a = 1; a <= s.getMsf_amount_req(); a++)
								{
									SpajDet spajDet = new SpajDet(s.getMsf_id(), s.getMsf_id(),s.getMsf_id(), s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 
											0, // = dikirim 
											FormatString.rpad("0", String.valueOf(noBlankoReq1[k]), 12), null, Integer.valueOf(currentUser.getLus_id()),null);
									insert("insertSpajDet", spajDet);
								}
							}
							else{
								SpajDet spajDet = new SpajDet(s.getMsf_id(), s.getMsf_id(),s.getMsf_id(), s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 
										0, // = dikirim 
										FormatString.rpad("0", String.valueOf(noBlankoReq1[k]), 12), null, Integer.valueOf(currentUser.getLus_id()),null);
								insert("insertSpajDet", spajDet);
							}
						}
						//insert spaj lama dengan no blanko 6 digit
						else{
							SpajDet spajDet = new SpajDet(s.getMsf_id(), s.getMsf_id(),s.getMsf_id(), s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 
									0, // = dikirim 
									FormatString.rpad("0", String.valueOf(noBlankoReq1[k]), 6), null, Integer.valueOf(currentUser.getLus_id()),null);
							insert("insertSpajDet", spajDet);	
						}					
					}
				}
				
			}
			
		}

			/** 3. insert form history */
			cmd.getFormHist().setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
			cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getFormHist().getMsf_id()));
			cmd.getFormHist().setPosisi(4);
			cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			insert("insertFormHistory", cmd.getFormHist());
			//email ke cabang
			User userCabang = selectLstUser(cmd.getDaftarFormSpaj().get(0).getLus_id());
			String to[] = {userCabang.getEmail()};
			String cc[] = new String[]{"desy@sinarmasmsiglife.co.id","pratidina@sinarmasmsiglife.co.id","Yusy@sinarmasmsiglife.co.id","syaidi@sinarmasmsiglife.co.id","yulin@sinarmasmsiglife.co.id","tedi@sinarmasmsiglife.co.id"};
			String sukses = "";
			if(userCabang.getEmail()==null){
				to = new String[]{"desy@sinarmasmsiglife.co.id","pratidina@sinarmasmsiglife.co.id","Yusy@sinarmasmsiglife.co.id","syaidi@sinarmasmsiglife.co.id","yulin@sinarmasmsiglife.co.id","tedi@sinarmasmsiglife.co.id"};
				cc = new String[]{""};
				//sukses="Email Cabang Tidak ada,informasi tidak dapat dikirim\n";
			}
			
			//String subject="Pengiriman SPAJ oleh GA";
			//String message="SPAJ dengan no form "+cmd.getDaftarFormSpaj().get(0).getMsf_id()+
			//				" Telah dikirim oleh GA.\nHarap update tombol telah diterima jikalau sudah mendapatkan SPAJ tersebut.";
			
			//Permintaan perubahan email oleh Desy [canpri 26/11/2013]
			Map tgl = (Map) querySingle("selectTglFormHist", cmd.getDaftarFormSpaj().get(0).getMsf_id());
			
			String subject = "KONFIRMASI TELAH DIKIRIM PERMINTAAN SPAJ DENGAN NOMOR "+cmd.getDaftarFormSpaj().get(0).getMsf_id();
			StringBuffer mssg = new StringBuffer();
			mssg.append("Admin\t\t : "+userCabang.getLus_full_name());
			//mssg.append("\nCabang\t\t : "+userCabang.getCabang());
			mssg.append("\nCabang\t\t : "+cmd.getDaftarFormSpaj().get(0).getLca_nama());
			mssg.append("\nNo. Permintaan\t : "+cmd.getDaftarFormSpaj().get(0).getMsf_id());
			//mssg.append("Jenis Permintaan : ");
			//mssg.append("No. Blanko : ");
			mssg.append("\nTgl. Permintaan\t : "+(String)tgl.get("TGL_MINTA"));
			mssg.append("\nTgl. Approve\t : "+(String)tgl.get("TGL_APPROVE"));
			mssg.append("\nTgl. Kirim\t : "+(String)tgl.get("TGL_KIRIM"));
			mssg.append("\n\n<table width='50%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
			mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>Jenis Permintaan</strong></td><td align='center'><strong>No. Blanko</strong></td></tr>");
			for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++){
				FormSpaj s = cmd.getDaftarFormSpaj().get(i);
				if(s.getMsf_amount_req()>0){
					mssg.append("<tr>");
					mssg.append("<td align='center'>"+s.getLsjs_desc()+"</td>");
					mssg.append("<td align='center'>"+s.getNo_blanko_req()+"</td>");
					mssg.append("</tr>");
				}
			}
			mssg.append("</table>");
			mssg.append("\n\nHarap update tombol telah diterima jika sudah menerima SPAJ tersebut.");
			
			//if(userCabang.getEmail()!=null) {
				try {
//					email.send(true, props.getProperty("admin.ajsjava"), to, cc, null, subject, mssg.toString(), null);
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							props.getProperty("admin.ajsjava"), to, cc, null, 
							subject, mssg.toString(), null, null);
				} catch (MailException e) {
					logger.error("ERROR :", e);
				}
	
			return 
				sukses+" Data pengiriman SPAJ sudah diupdate.";
	}

	/**
	 * Proses yang menandakan bahwa spaj hasil permintaan dari cabang ke pusat sudah dikirimkan
	 * ke cabang dan sudah diterima oleh cabang, sehingga otomatis menambah jumlah spaj yang ada di cabang
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 26, 2007 (2:26:05 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processReceiveFormSpaj(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(int i=0; i<cmd.getDaftarFormSpaj().size(); i++) {
			FormSpaj s = cmd.getDaftarFormSpaj().get(i);
			s.setPosisi(5); //received
			
			/** 1. update form spaj dan stok spaj */
			update("updateFormSpaj", s);
			Spaj spaj = new Spaj();
			spaj.newSpaj(s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), s.getMsf_amount(), Integer.valueOf(currentUser.getLus_id()),Integer.valueOf(currentUser.getLus_id()));
			update("updateStokSpaj", spaj);
			
			/** 2. update status di mst_spaj_det */
			if(s.getMsf_amount()>0) {
//				int start = Integer.valueOf(s.getStart_no_blanko());
//				int end = Integer.valueOf(s.getEnd_no_blanko());
//				for(int j = start; j <=end; j++) {
//					SpajDet spajDet = new SpajDet(null, null, null,null, null, s.getLsjs_id(), null, 
//							1, // = di cabang 
//							FormatString.rpad("0", String.valueOf(j), 6), null, Integer.valueOf(currentUser.getLus_id()),null);
//					update("updateSpajDet", spajDet);
//				}
				String noBlankoReq1[]=s.getNo_blanko_req().split(";");
				String noBlankoReq2[]=new String[noBlankoReq1.length];
				for(int k=0;k<noBlankoReq1.length;k++) {
					if(noBlankoReq1[k].contains("-")) {
						noBlankoReq2=noBlankoReq1[k].split("-");
						Long start=Long.parseLong(noBlankoReq2[0]);
						Long end=Long.parseLong(noBlankoReq2[1]);
						for(Long j = start; j <=end; j++) {
							SpajDet spajDet = new SpajDet(null, null, null,null, null, s.getLsjs_id(), null, 
							1, // = di cabang 
							FormatString.rpad("0", String.valueOf(j), 6), null, Integer.valueOf(currentUser.getLus_id()),null);
							spajDet.setMssd_dt(new Date());
							update("updateSpajDet", spajDet);
							
							// insert history fitrah card (Yusup A)
							if(s.getLsjs_id() == 9) {
								Map params = new HashMap();
								params.put("lca_id", s.getLca_id());
								params.put("msab_id", s.getMsab_id());
								params.put("lus_id", s.getLus_id());
								params.put("lsjs_id", s.getLsjs_id());
								params.put("msfh_date", new Date());
								params.put("no_blanko", FormatString.rpad("0", String.valueOf(j), 6));
								params.put("lsp_id", 1);
								params.put("msfh_desc","Sudah diterima oleh Branch Admin");
								params.put("msf_urut", selectNoUrutFormFitrah(FormatString.rpad("0", String.valueOf(j), 6),s.getLsjs_id()));
								params.put("flag_urut", 0);
								insert("insertFitrahHist",params);
							}
						}						
					}
					else {
						SpajDet spajDet = new SpajDet(null, null, null,null, null, s.getLsjs_id(), null, 
						1, // = di cabang 
						FormatString.rpad("0", String.valueOf(noBlankoReq1[k]), 6), null, Integer.valueOf(currentUser.getLus_id()),null);
						spajDet.setMssd_dt(new Date());
						update("updateSpajDet", spajDet);
						
						// insert history fitrah card (Yusup A)
						if(s.getLsjs_id() == 9) {
							Map params = new HashMap();
							params.put("lca_id", s.getLca_id());
							params.put("msab_id", s.getMsab_id());
							params.put("lus_id", s.getLus_id());
							params.put("lsjs_id", s.getLsjs_id());
							params.put("msfh_date", new Date());
							params.put("no_blanko", FormatString.rpad("0", String.valueOf(noBlankoReq1[k]), 6));
							params.put("lsp_id", 1);
							params.put("msfh_desc","Sudah diterima oleh Branch Admin");
							params.put("msf_urut", selectNoUrutFormFitrah(FormatString.rpad("0", String.valueOf(noBlankoReq1[k]), 6),s.getLsjs_id()));
							params.put("flag_urut", 0);
							insert("insertFitrahHist",params);
						}						
					}
				}
			}
			
		}

		/** 3. insert form history */
		if(!cmd.getDaftarFormSpaj().isEmpty()) {
			FormHist f = new FormHist();
			f.setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
			f.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			f.setPosisi(5);
			f.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			f.setMsfh_desc("Sudah diterima oleh Branch Admin");			
			insert("insertFormHistory", f);
			
			//permintaan yusy email ke bas
			String to[] = new String[]{"bas@sinarmasmsiglife.co.id"};
			String subject="Permintaan SPAJ from Cabang Sudah Diterima";
			String message="Form Permintaan SPAJ oleh Cabang "+cmd.getDaftarFormSpaj().get(0).getLca_nama()+ 
							" dengan no permintaan " + cmd.getDaftarFormSpaj().get(0).getMsf_id() +
							" sudah diterima.";
			try {
				EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, new Date(), null, true,
						(currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava")),
						to, null, null, subject, message, null, null);
			} catch (MailException e) {
				logger.error("ERROR :", e);
			}
		}

		return "Data penerimaan SPAJ sudah diupdate. Jumlah SPAJ di sistem sudah ditambahkan.";
	}
	
	/**
	 * Proses untuk insert permohonan spaj dari agen ke cabang
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 23, 2007 (4:56:17 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processNewFormSpajAgen(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		/** 1. insert kode agen semetnara cabang */
		if(cmd.getAgen().getMsab_id()==null) {
			Integer msab_id = (Integer) querySingle("selectLastAgentBranchId", null);
			if(msab_id == null) msab_id = 0;
			cmd.getAgen().setMsab_id(msab_id);
			insert("insertAgentBranch", cmd.getAgen());
		}
		
		FormSpaj f = cmd.getDaftarFormSpaj().get(0);
		String seq = sequence.sequenceMst_form(1, currentUser.getLca_id());
		for(FormSpaj s : cmd.getDaftarFormSpaj()) {
			s.setMsf_id(seq);
			s.setMss_jenis(1);
			s.setLca_id(currentUser.getLca_id());
			s.setMsab_id(cmd.getAgen().getMsab_id());
			s.setMsf_amount(s.getMsf_amount_req());
			if (cmd.getDaftarFormSpaj().get(0) != null) {
				s.setBukti_pembayaran(cmd.getDaftarFormSpaj().get(0).getBukti_pembayaran());
			}

			/** 2. update stoknya agen */
			Spaj spaj = new Spaj();
			spaj.newSpaj(s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), s.getMsf_amount(), Integer.valueOf(currentUser.getLus_id()),Integer.valueOf(currentUser.getLus_id()));
			if(((Integer) querySingle("selectCekSpaj", s))==0) {
				insert("insertNewSpaj", spaj); //insert stok di agen
			}else {
				update("updateStokSpaj", spaj); //tambah stok di agen
			}

			/** 3. insert form agen */
			insert("insertNewFormSpaj", s); //insert form-nya

			/** 4. kurangi stok cabang */
			Spaj spajCabang = new Spaj();
			spajCabang.newSpaj(0, s.getLca_id(), s.getLsjs_id(), 0, 0-s.getMsf_amount(), Integer.valueOf(currentUser.getLus_id()),Integer.valueOf(currentUser.getLus_id()));
			update("updateStokSpaj", spajCabang); //kurangi stok di cabang

			/** 5. update status di mst_spaj_det, dipindahkan dari cabang ke tangan agen */
			if(s.getMsf_amount()>0) {
				String lsBlankoReq[]=s.getNo_blanko_req().split(";");
				for(int j=0;j<lsBlankoReq.length;j++){
					SpajDet spajDet2 = new SpajDet(s.getMsf_id(), null,s.getMsf_id(), s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 
							2, // = di agen 
							lsBlankoReq[j], null, Integer.valueOf(currentUser.getLus_id()),1);
					if(cmd.getTanggal().equals("")) spajDet2.setMssd_dt(new Date());
					else {
						SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
						try {
							spajDet2.setMssd_dt(formatDate.parse(cmd.getTanggal()));
						} catch (java.text.ParseException e) {logger.error("ERROR :", e);}							
					}
					//spajDet2.setMssd_dt(mssd_dt);
					update("updateSpajDet", spajDet2);
					
					// insert history per no blanko (Yusup A)
					//if(s.getLsjs_id() == 9) {
						Map params = new HashMap();
						params.put("lca_id", s.getLca_id());
						params.put("msab_id", s.getMsab_id());
						params.put("lus_id", s.getLus_id());
						params.put("lsjs_id", s.getLsjs_id());
						params.put("msfh_date", new Date());
						params.put("no_blanko", lsBlankoReq[j]);
						params.put("lsp_id", 2);
						params.put("msfh_desc","Pemberian SPAJ kepada agen [MSAB_ID:"+cmd.getAgen().getMsab_id()+"]");
						params.put("msf_urut", selectNoUrutFormFitrah(lsBlankoReq[j],s.getLsjs_id()));
						params.put("flag_urut", selectNoUrutFlagFitrah(lsBlankoReq[j],s.getMsab_id(),new Integer(2),s.getLsjs_id()));
						insert("insertFitrahHist",params);
					//}					
				}
				/*
				int start = Integer.valueOf(s.getStart_no_blanko());
				int end = Integer.valueOf(s.getEnd_no_blanko());
				for(int j = start; j <=end; j++) {
					SpajDet spajDet = new SpajDet(s.getMsf_id(), null, s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 
							2, // = di agen 
							FormatString.rpad("0", String.valueOf(j), 6), null, Integer.valueOf(currentUser.getLus_id()));
					update("updateSpajDet", spajDet);
				}*/
			}
		}
		/** 6. update blanko2 lama jadinya masuk ke blanko sini */
		//SpajDet spajDet = new SpajDet(s.getMsf_id(),null, null,null, null, null, cmd.getAgen().getMsab_id(), null, null, null, Integer.valueOf(currentUser.getLus_id()),null);
		//update("updateSpajDetTgjwb", spajDet);
		updateSpajDetAll(cmd.getMsf_id_bef(), seq);
		
		cmd.setMsf_id(seq);
		
		
		/** 7. insert history form */
		FormHist form = new FormHist();
		form.setMsf_id(cmd.getDaftarFormSpaj().get(0).getMsf_id());
		form.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
		form.setPosisi(1);
		form.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		form.setMsfh_desc("Pemberian SPAJ kepada agen [MSAB_ID:"+cmd.getAgen().getMsab_id()+"]");			
		if(cmd.getTanggal().equals("")) insert("insertFormHistory", form);
		else {
			SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
			try {
				form.setMsfh_dt(formatDate.parse(cmd.getTanggal()));
			} catch (java.text.ParseException e) {logger.error("ERROR :", e);}	
			insert("insertFormHistory2", form);
		}
		
		return 
			"Permintaan agen sudah tersimpan dengan nomor " + cmd.getMsf_id() 
			+ ". \\nPermintaan ini harus dipertanggungjawabkan dalam kurun waktu < 30 hari sebelum agen dapat meminta kembali.";
	}
	
	/**
	 * Proses untuk insert pertanggungjawaban SPAJ oleh branch admin
	 * @author Yusuf Sutarko
	 * @since Mar 14, 2007 (10:39:30 AM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 * @throws DataAccessException
	 */
	public String processPertanggungJawabanFormSpajAgen(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		
		/**1. Tandai form sudah dipertanggungjawabkan */
		if(cmd.getDaftarFormSpaj() != null) {
			for(FormSpaj s : cmd.getDaftarFormSpaj()) {
				s.setMsf_id(cmd.getMsf_id());
				s.setMss_jenis(1);

				s.setPosisi(6); //acknowledged
				update("updateFormSpaj", s);
				
			}			
		}

		/**2. Update keterangan per nomor blanko */
		Map<Integer, Integer> pengurangAgen = new HashMap<Integer, Integer>();
		Map<Integer, Integer> penambahAgen = new HashMap<Integer, Integer>();
		
		for(SpajDet s : cmd.getDaftarPertanggungjawaban()) {
			Map params = new HashMap();
			if(2 == s.getLsp_id()) { //di agen
				//...
			} 
			else if(3 == s.getLsp_id() || 4 == s.getLsp_id() || 
					5 == s.getLsp_id() || 6 == s.getLsp_id() || 8 == s.getLsp_id()) { //jadi spaj / rusak / hilang / salah / pemutihan
				Integer pengurang = pengurangAgen.get(s.getLsjs_prefix());
				if(pengurang == null) pengurang = 0;
				pengurang += 1;
				pengurangAgen.put(s.getLsjs_id(), pengurang);
				
				if(3 == s.getLsp_id()) { //jadi spaj
					//Map infoBlanko = selectSpajFromBlanko(s.getLsjs_prefix()+s.getNo_blanko());
					//Map infoBlanko = selectSpajFromBlanko(s.getNo_blanko());
					Map infoBlanko = selectSpajFromBlanko(s.getLsjs_prefix()+s.getNo_blanko());
					if(infoBlanko == null) throw new RuntimeException("Nomor blanko ini tidak tercatat di sistem sebagai SPAJ.");
					else {
						s.setReg_spaj((String) infoBlanko.get("REG_SPAJ"));
						s.setMspo_policy_no((String) infoBlanko.get("MSPO_POLICY_NO"));
					}
				}	
				if(s.getMssd_lus_id().equals("") || s.getMssd_lus_id() == null)
					s.setMssd_lus_id(Integer.valueOf(currentUser.getLus_id()));
				s.setFlag_bef(1);
				s.setMsf_id_bef(s.getMsf_id());		
				
				params.put("msab_id", s.getMsab_id());
				params.put("lsp_id", s.getLsp_id());
				if(s.getLsjs_id() == 9)
					params.put("flag_urut", selectNoUrutFlagFitrah(s.getNo_blanko(),s.getMsab_id(),s.getLsp_id(),s.getLsjs_id()));
			} 
			else if(7 == s.getLsp_id() && 9 == s.getLsjs_id()) {
				// jika fitrah card dikembalikan, stok cabang bertambah, stok agen berkurang
				Integer pengurang = pengurangAgen.get(s.getLsjs_id()); // stok agen
				if(pengurang == null) pengurang = 0;
				pengurang += 1;
				pengurangAgen.put(s.getLsjs_id(), pengurang);
				
				Integer penambahan = penambahAgen.get(s.getLsjs_id()); // stok cabang
				if(penambahan == null) penambahan = 0;
				penambahan += 1;
				penambahAgen.put(s.getLsjs_id(), penambahan);
				
				params.put("msab_id", s.getMsab_id());
				params.put("lsp_id", s.getLsp_id());
				if(s.getLsjs_id() == 9)
					params.put("flag_urut", selectNoUrutFlagFitrah(s.getNo_blanko(),s.getMsab_id(),s.getLsp_id(),s.getLsjs_id()));
				
				s.setMss_jenis(0);
				s.setMsab_id(0);
				s.setMssd_dt(new Date());
				s.setMsf_id(s.getMsf_id_asli());
				s.setMsf_id_bef("");
				s.setFlag_bef(1);
				s.setLsp_id(1); // biar bs kluar di stok lg
				s.setMssd_dt(new Date());

			}
			update("updateSpajDet", s);
			
			// insert history fitrah card (Yusup A)
			//if(s.getLsjs_id() == 9 && s.getLsp_id() != 2) {
				params.put("lca_id", s.getLca_id());
				params.put("lus_id", s.getMssd_lus_id());
				params.put("lsjs_id", s.getLsjs_id());
				params.put("msfh_date", new Date());
				params.put("lsp_id", s.getLsp_id());
				params.put("msab_id", s.getMsab_id());
				params.put("no_blanko", s.getNo_blanko());
				if(s.getMssd_desc().equals("")) s.setMssd_desc("Pertanggungjawaban form agen");
				params.put("msfh_desc",s.getMssd_desc());
				params.put("msf_urut", selectNoUrutFormFitrah(s.getNo_blanko(),s.getLsjs_id()));
				insert("insertFitrahHist",params);
			//}				
		}

		/**3. Kurangi stok spaj yang ada di agen apabila rusak / hilang / salah / dikembalikan */
		Integer mss_jenis,msab_id;
		String lca_id;
		if(cmd.getDaftarFormSpaj() != null) {
			mss_jenis = cmd.getDaftarFormSpaj().get(0).getMss_jenis();
			lca_id = cmd.getDaftarFormSpaj().get(0).getLca_id();
			msab_id = cmd.getDaftarFormSpaj().get(0).getMsab_id();
			
			for(Integer lsjs_id : pengurangAgen.keySet()) {
				Integer pengurang = pengurangAgen.get(lsjs_id);
				if(pengurang != null) {
					if(pengurang > 0) {
						Spaj spaj = new Spaj();
						spaj.newSpaj(mss_jenis, lca_id,lsjs_id, msab_id, 0-pengurang,
								 Integer.valueOf(currentUser.getLus_id()),Integer.valueOf(currentUser.getLus_id()));
						update("updateStokSpaj", spaj);
						if(lsjs_id == 9) {// tambah stok di cabang
							Integer penambahan = penambahAgen.get(lsjs_id);
							if(penambahan != null) {
								if(penambahan > 0) {
									spaj.newSpaj(0, lca_id,lsjs_id, 0, 0+penambahan,
											 Integer.valueOf(currentUser.getLus_id()),Integer.valueOf(currentUser.getLus_id()));	
									update("updateStokSpaj", spaj);									
								}
							}
						}						
					}
				}
			}			
		}
		else {
			mss_jenis = cmd.getDaftarPertanggungjawaban().get(0).getMss_jenis();
			lca_id = cmd.getDaftarPertanggungjawaban().get(0).getLca_id();
			msab_id = cmd.getDaftarPertanggungjawaban().get(0).getMsab_id();
			
			for(Integer lsjs_id : pengurangAgen.keySet()) {
				Integer pengurang = pengurangAgen.get(lsjs_id);
				if(pengurang != null) {
					if(pengurang > 0) {
						Spaj spaj = new Spaj(); 
						if(lsjs_id == 9) {// tambah stok di cabang
							spaj.newSpaj(0, lca_id,lsjs_id, 0, 0+pengurang,
									 Integer.valueOf(currentUser.getLus_id()),Integer.valueOf(currentUser.getLus_id()));	
							update("updateStokSpaj", spaj);
						}
						spaj.newSpaj(mss_jenis, lca_id,lsjs_id, msab_id, 0-pengurang, 
								Integer.valueOf(currentUser.getLus_id()),null);
						update("updateStokSpaj", spaj);
					}
				}
			}			
		}
		
		/**4. Masukkan history perubahan */
		if(!cmd.getMsf_id().equals("")) {
			cmd.getFormHist().setMsf_id(cmd.getMsf_id());
			cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getMsf_id()));
			cmd.getFormHist().setPosisi(6);
			cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			cmd.getFormHist().setMsfh_desc("Pertanggungjawaban form agen");
			insert("insertFormHistory", cmd.getFormHist());			
		}
		else {
			String temp = "";
			for(SpajDet s : cmd.getDaftarPertanggungjawaban()) {
				if(!temp.equals(s.getMsf_id())) {
					cmd.getFormHist().setMsf_id(s.getMsf_id());
					cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(s.getMsf_id()));
					cmd.getFormHist().setPosisi(6);
					cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
					cmd.getFormHist().setMsfh_desc("Update Pertanggungjawaban form agen");
					insert("insertFormHistory", cmd.getFormHist());						
				}
				temp = s.getMsf_id();
			}
		}
		if(!cmd.getMsf_id().equals("")) return "Form dengan nomor " + cmd.getMsf_id() + " sudah dipertanggungjawabkan.";
		return "Form pertanggungjawaban sudah di update";
	}
	
	/**
	 * Query untuk menarik daftar permintaan pengiriman spaj yang ada di suatu cabang
	 * @author Yusuf Sutarko
	 * @since Feb 23, 2007 (4:58:03 PM)
	 * @param formSpaj
	 * @return
	 */
	public List<FormSpaj> selectDaftarFormSpaj(FormSpaj formSpaj) throws DataAccessException {
		return query("selectDaftarFormSpaj", formSpaj);
	}
	
	/**
	 * Query untuk menarik data yang sudah ada, atau untuk penyimpanan data baru 
	 * dari permintaan pengiriman spaj
	 * @author Yusuf Sutarko
	 * @since Feb 23, 2007 (4:58:53 PM)
	 * @param msf_id
	 * @param lca_id
	 * @param lus_id
	 * @return
	 */
	public List<FormSpaj> selectFormSpaj(String msf_id, String lca_id, String lus_id) throws DataAccessException {
		FormSpaj formSpaj = new FormSpaj();
		formSpaj.setMss_jenis(0);
		formSpaj.setMsf_id(msf_id);
		formSpaj.setLca_id(lca_id);
		formSpaj.setLus_id(Integer.parseInt(lus_id));
		if(msf_id==null)
			return query("selectNewFormSpaj", formSpaj);
		else
			return query("selectFormSpaj", formSpaj);
	}
	
	public List<DropDown> selectLokasiAdmin(String lus_id) {
		return query("selectLokasiAdmin", lus_id);
	}	
	
	/**
	 * Query untuk menarik penyimpanan data baru 
	 * dari permintaan spaj dari agen ke branch admin
	 * @author Yusuf Sutarko
	 * @since Feb 23, 2007 (4:58:53 PM)
	 * @param msf_id
	 * @param lca_id
	 * @param lus_id
	 * @return
	 */
	public List<FormSpaj> selectFormSpajAgen(String msf_id, String lca_id) throws DataAccessException {
		FormSpaj formSpaj = new FormSpaj();
		formSpaj.setMss_jenis(1);
		formSpaj.setMsf_id(msf_id);
		formSpaj.setLca_id(lca_id);
		return query("selectNewFormSpaj", formSpaj);
	}
	
	
	
	/**
	 * Query untuk menarik jumlah stok spaj yang tersedia di suatu cabang / agen
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 23, 2007 (5:00:58 PM)
	 * @param spaj
	 * @return
	 */
	public List<Spaj> selectStokSpaj(Spaj spaj) throws DataAccessException {
		return query("selectStokSpaj", spaj);
	}
	public List<Spaj> selectStokSpajAgen(Spaj spaj) throws DataAccessException {
		return query("selectStokSpajAgen", spaj);
	}
	
	/**
	 * Query untuk menarik jumlah stok kertas polis yang tersedia di suatu cabang / agen
	 * 
	 * @author Hemilda
	 * @since Feb 23, 2007 (5:00:58 PM)
	 * @param spaj
	 * @return
	 */
	public List<Spaj> selectStokBlanko(Spaj spaj) throws DataAccessException {
		return query("selectStokBlanko", spaj);
	}
	
	/**
	 * Query untuk menarik nomor urut terakhir dari mst_form_hist
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 26, 2007 (9:00:58 AM)
	 * @param spaj
	 * @return
	 */
	public Integer selectNoUrutFormHistory(String msf_id) throws DataAccessException {
		Integer result = (Integer) querySingle("selectNoUrutFormHistory", msf_id);
		if(result == null) return 1;
		else return result + 1;
	}
	
	public String selectExistNoKartuSimasCard(String nokartu) throws DataAccessException {
		return (String) querySingle("selectExistNoKartuSimasCard", nokartu);
	}
	
	public String selectExistNoKartuDbd(String nokartu) throws DataAccessException {
		return (String) querySingle("selectExistNoKartuDbd", nokartu);
	}
	
	public String selectExistNoKartuPasBp(String nokartu) throws DataAccessException {
		return (String) querySingle("selectExistNoKartuPasBp", nokartu);
	}
	
	public String selectExistNoKartuHcp(String nokartu) throws DataAccessException {
		return (String) querySingle("selectExistNoKartuHcp", nokartu);
	}
	
	public Integer selectNoUrutFormFitrah(String no_blanko, Integer lsjs_id) throws DataAccessException {
		Map param = new HashMap();
		param.put("no_blanko", no_blanko);
		param.put("lsjs_id", lsjs_id);		
		Integer result = (Integer) querySingle("selectNoUrutFormFitrah", param);
		if(result == null) return 1;
		else return result + 1;
	}
	
	public Integer selectNoUrutFlagFitrah(String no_blanko, Integer msab_id, Integer lsp_id, Integer lsjs_id) throws DataAccessException {
		Map param = new HashMap();
		param.put("no_blanko", no_blanko);
		param.put("msab_id", msab_id);
		param.put("lsjs_id", lsjs_id);		
		Integer result = (Integer) querySingle("selectNoUrutFlagFitrah", param);
		
		if(result == null) return 1;
		else if(lsp_id == 2) {
			return result + 1;
		}
		return result;
	}
	
	/**
	 * Query untuk menarik history dari suatu permintaan spaj
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 26, 2007 (9:00:58 AM)
	 * @param spaj
	 * @return
	 */
	public List<FormHist> selectFormHistory(String msf_id) throws DataAccessException {
		FormHist formHist = new FormHist();
		formHist.setMsf_id(msf_id);
		return query("selectFormHistory", formHist);
	}
	
	/**
	 * Query untuk menarik history yang specific dari suatu permintaan spaj
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 26, 2007 (9:00:58 AM)
	 * @param spaj
	 * @return
	 */
	public FormHist selectFormHistory(FormHist formHist) throws DataAccessException {
		return (FormHist) querySingle("selectFormHistory", formHist);
	}
	
	/**
	 * Query untuk menarik hak akses seorang user terhadap address region,
	 * dimana dari address region tersebut (LAR_ID) dapat diakses tabel LST_REGION
	 * yang akan membuka akses terhadap cabang, wakil, region (LCA, LWK, LSRG)
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 27, 2007 (8:38:07 AM)
	 * @param lus_id
	 * @return
	 * @throws DataAccessException
	 */
	public List<Region> selectUserAdmin(int lus_id) throws DataAccessException {
		return query("selectUserAdmin", lus_id);
	}
	
	/**
	 * Query untuk menarik daftar agen dari MST_AGENT yang ada pada suatu LAR tertentu (LCA, LWK, LSRG)
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 27, 2007 (10:44:40 AM)
	 * @param lca_lwk_lsrg
	 * @return
	 * @throws DataAccessException
	 */
	public List<Agen> selectAgentFromRegion(String lca_lwk_lsrg) throws DataAccessException {
		return query("selectAgentFromRegion", lca_lwk_lsrg);
	}

	/**
	 * Query untuk menarik seluruh daftar agen dari MST_AGENT yang dapat diakses seorang branch admin
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 27, 2007 (10:44:40 AM)
	 * @param lus_id
	 * @return
	 * @throws DataAccessException
	 */
	public List<Agen> selectAllAgentFromRegion(int lus_id) throws DataAccessException {
		return query("selectAllAgentFromRegion", lus_id);
	}
	
	/**
	 * Query untuk menarik daftar agen dari MST_AGENT_BRANCH yang ada pada suatu LAR tertentu (LCA, LWK, LSRG)
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 27, 2007 (10:44:40 AM)
	 * @param lca_lwk_lsrg
	 * @return
	 * @throws DataAccessException
	 */
	public List<Agen> selectAgentBranchFromRegion(String lca_lwk_lsrg) throws DataAccessException {
		return query("selectAgentBranchFromRegion", lca_lwk_lsrg);
	}
	
	/**
	 * Query untuk menarik daftar agen dari MST_AGENT_BRANCH yang dapat diakses seorang branch admin
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 27, 2007 (10:44:40 AM)
	 * @param lca_lwk_lsrg
	 * @return
	 * @throws DataAccessException
	 */
	public List<Agen> selectAllAgentBranchFromRegion(int lus_id) throws DataAccessException {
		return query("selectAllAgentBranchFromRegion", lus_id);
	}
	
	/**
	 * Query untuk mengecek apakah saldo SPAJ di suatu cabang / agen ada 
	 * @author Yusuf Sutarko
	 * @since Feb 28, 2007 (3:22:46 PM)
	 * @param s
	 * @return
	 * @throws DataAccessException
	 */
	public Integer selectCekSpaj(FormSpaj s) throws DataAccessException{
		return (Integer) querySingle("selectCekSpaj", s);
	}
	
	/**
	 * Query untuk mengetahui jumlah sisa suatu spaj jenis tertentu 
	 * @author Yusuf Sutarko
	 * @since Mar 22, 2007 (3:22:46 PM)
	 * @param s
	 * @return
	 * @throws DataAccessException
	 */
	public Integer selectSisaSpaj(FormSpaj s) throws DataAccessException{
		return (Integer) querySingle("selectSisaSpaj", s);
	}
	
	/**
	 * Query untuk menarik history permintaan semua agen dalam region cakupan user tersebut
	 * 
	 * @author Yusuf Sutarko
	 * @since Mar 7, 2007 (2:51:48 PM)
	 * @param lus_id
	 * @return
	 * @throws DataAccessException
	 */
	public List<FormSpaj> selectHistoryAgentAllRegion(int lus_id) throws DataAccessException{
		return query("selectHistoryAgentAllRegion", lus_id);
	}
	
	/**
	 * Query untuk menarik history permintaan semua agen dalam suatu region
	 * 
	 * @author Yusuf Sutarko
	 * @since Mar 7, 2007 (2:51:48 PM)
	 * @param lus_id
	 * @return
	 * @throws DataAccessException
	 */
	public List<FormSpaj> selectHistoryAgent(int lus_id, String lca_lwk_lsrg) throws DataAccessException{
		Map param = new HashMap();
		param.put("lus_id", lus_id);
		param.put("lca_lwk_lsrg", lca_lwk_lsrg);
		return query("selectHistoryAgent", param);
	}
	
	/**
	 * Query untuk menarik data2 form agen yang harus dipertanggungjawabkan (lebih dari 30 hari)
	 * 
	 * @author Yusuf Sutarko
	 * @since Mar 15, 2007 (11:33:14 AM)
	 * @param lus_id
	 * @return
	 * @throws DataAccessException
	 */
	public List<FormSpaj> selectWarning30Hari(int lus_id) throws DataAccessException{
		return query("selectWarning30Hari", lus_id);
	}
	
	/**
	 * Query untuk menarik data form hasil pencarian di viewer form spaj
	 * 
	 * @author Yusuf Sutarko
	 * @since Mar 31, 2007 (9:39:35 AM)
	 * @param msf_id
	 * @return
	 * @throws DataAccessException
	 */
	public List<FormSpaj> selectDaftarFormCari(String msf_id) throws DataAccessException{
		return query("selectDaftarFormCari", msf_id);
	}
	
	/**
	 * Query untuk menarik data no blanko
	 * 
	 * @author Yusuf Sutarko
	 * @since Mar 31, 2007 (5:01:40 PM)
	 * @param msf_id
	 * @return
	 * @throws DataAccessException
	 */
	public List<SpajDet> selectPertanggungjawaban(String msf_id) throws DataAccessException{
		return query("selectPertanggungjawaban", msf_id);
	}
	
	public List<SpajDet> selectUpdatePertanggungjawaban(String lusId, String msagId, String msabId) throws DataAccessException {
		Map data = new HashMap();
		data.put("lus_id", lusId);
		data.put("msag_id", msagId);
		data.put("msab_id", msabId);
		
		return query("selectUpdatePertanggungjawaban", data);
	}
	
	/**
	 * Query untuk menarik jenis pertanggungjawaban (misalnya rusak hilang salah)
	 * @author Yusuf Sutarko
	 * @since Apr 1, 2007 (10:31:46 AM)
	 * @return
	 * @throws DataAccessException
	 */
	public List<DropDown> selectJenisPertanggungjawaban() throws DataAccessException{
		return query("selectJenisPertanggungjawaban", null);
	}
	
	/**
	 * Query untuk menarik detail spaj dan polis terakhir atas suatu nomor blanko
	 * Bisa juga dijadikan untuk validasi apakah benar suatu nomor blanko sudah diinput
	 * 
	 * @author Yusuf Sutarko
	 * @since Apr 1, 2007 (10:32:32 AM)
	 * @param no_blanko
	 * @return
	 * @throws DataAccessException
	 */
	public HashMap selectSpajFromBlanko(String no_blanko) throws DataAccessException{
		return (HashMap) querySingle("selectSpajFromBlanko", no_blanko);
	}
	
	/**
	 * Query untuk mengecek posisi suatu form
	 * @author Yusuf Sutarko
	 * @since Apr 1, 2007 (11:15:16 AM)
	 * @param msf_id
	 * @return
	 * @throws DataAccessException
	 */
	public Integer selectCekPosisiFormSpaj(String msf_id) throws DataAccessException{
		return (Integer) querySingle("selectCekPosisiFormSpaj", msf_id);
	}
	
	/**
	 * Query untuk menarik jenis spaj
	 * 
	 * @author Yusuf Sutarko
	 * @since Apr 1, 2007 (12:14:11 PM)
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map> selectJenisSpaj() throws DataAccessException{
		return query("selectJenisSpaj", null);
	}
	
	/**
	 * Query untuk mengecek blanko di agen
	 * 
	 * @author Administrator
	 * @since Apr 2, 2007
	 * @param lsjs_id
	 * @param start_no_blanko
	 * @param end_no_blanko
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map> selectCekBlankoDiAgen(Integer lsjs_id, String start_no_blanko, String end_no_blanko) throws DataAccessException{
		Map params = new HashMap();
		params.put("lsjs_id", lsjs_id);
		params.put("start_no_blanko", start_no_blanko);
		params.put("end_no_blanko", end_no_blanko);
		return query("selectCekBlankoDiAgen", params);
	}
	
	public List<FormSpaj> selectDetailBlankoCabang(String lca_id) throws DataAccessException {
		return query("selectDetailBlankoCabang", lca_id);
	}
	/**Fungsi: Untuk menampilkan bahwa spaj yang ada di cabang telah di pertanggungjawabkan
	 * @param 	Integer msab_id , String lca_id
	 * @return	Integer
	 * @author	Ferry Harlim
	 */
	public Integer selectJumlahSpajPerTgJbwn(Integer msab_id,String lca_id)throws DataAccessException{
		Map param=new HashMap();
		param.put("msab_id",msab_id);
		param.put("lca_id",lca_id);
		return (Integer)querySingle("selectJumlahSpajPerTgJbwn", param);
	}
	/**Fungsi:	Untuk Menampilkan kode cabang berdasarkan no form
	 * @param 	String msfId
	 * @return	String
	 * @author	Ferry Harlim
	 */
	public String selectMstFormLcaId(String msfId){
		return (String)querySingle("select_mst_form_lca_id", msfId);
	}
	
	/**Fungsi: 	Untuk Menampilkan detail sisa stok (spaj) di cabang
	 * @param	String lcaId, Integer mssJenis, Integer lsjsId , Integer lspId
	 * @return 	List
	 * @author	Ferry Harlim
	 */
	public List selectDetailStokCabang(String lcaId, Integer mssJenis, Integer lsjsId, Integer lspId,Integer lusId, String no_blanko, Integer trav_ins_type){
		Map param=new HashMap();
		param.put("lca_id", lcaId);
		param.put("mss_jenis", mssJenis);
		param.put("lsjs_id", lsjsId);
		param.put("lsp_id", lspId);
		param.put("mssd_lus_id", lusId);
		param.put("no_blanko", no_blanko);
		param.put("trav_ins_type", trav_ins_type);
		return query("selectDetailStokCabang", param);
	}
	
	public List selectDetailStockSimasCard() {
		return query("selectDetailStockSimasCard", null);
	}
	
	/**Fungsi:	Untuk Menampilkan jenis spaj
	 * @param	Integer lsjsId
	 * @return	java.util.HashMap
	 * @author 	Ferry Harlim
	 */
	public Map selectLstJenisSpaj(Integer lsjsId){
		return (HashMap) querySingle("selectLstJenisSpaj", lsjsId);
	}
	
	/**Fungsi:	Untuk Menampilkan msf_id yang akan dipertanggungjawabkan berdasarkan kode cabang 
	 * 			dan kode agen.
	 * @param	String msagId, String lcaId
	 * @return	String
	 * @author 	Ferry Harlim
	 */
	public String selectMsfIdPertanggungjawaban(String msagId, String lcaId){
		Map param=new HashMap();
		param.put("msag_id",msagId);
		param.put("lca_id",lcaId);
		return (String) querySingle("selectMsfIdPertanggungjawaban", param);
	}

	/**Fungsi:	Untuk Menampilkan msf_id sebelumnya. 
	 * 			
	 * @param	Integer msab_id
	 * @return	String
	 * @author 	Ferry Harlim
	 */
	public String selectMsfIdBef(Integer msab_id){
		return (String) querySingle("selectMsfIdBef", msab_id);
	}
	/**Fungsi:	Untuk mengupdate msf_id dari tabel EKA.MST_SPAJ_DET 
	 * @param	String msagId, String lcaId
	 * @return	String
	 * @author 	Ferry Harlim
	 */
	public void updateSpajDetAll(String msfIdOld,String msfIdNew){
		Map param=new HashMap();
		param.put("msf_id_old", msfIdOld);
		param.put("msf_id_new", msfIdNew);
		update("updateSpajDetAll", param);
	}
	/**Fungsi:	Untuk Menampilkan data form spaj 
	 * @param	String msfId
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectSearchFormSpajExpress(String msfId){
		return query("selectSearchFormSpajExpress",msfId);
	}
	/**Fungsi:	Untuk Menampilkan data form spaj 
	 * @param	FormSpaj formSpaj
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectSearchFormSpajDetail(FormSpaj formSpaj){
		return query("selectSearchFormSpajDetail",formSpaj);
	}
	
	/**Fungsi:	Untuk Menampilkan data agent per cabang 
	 * @param	Agen agen
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectAllAgentFromBranch(Agen agen){
		return query("selectAllAgentFromBranch",agen);
	}
	/**Fungsi:	Untuk Mengupdate status agen 
	 * 			0 = Ok
	 * 			1 = SP-1
	 * 			2 = SP-2
	 * 			3 = SP-3 
	 * @param	Agen agen
	 * @author 	Ferry Harlim
	 */
	public void updateMstAgentStatus(Agen agen){
		update("updateMstAgentStatus", agen);
	}
	/**Fungsi:	Untuk Menampilkan Daftar user dari masing2 cabang 
	 * @param	String lcaid
	 * @author 	Ferry Harlim
	 */
	public List selectAllUserInCabang(String lcaId){
		return query("selectAllUserInCabang",lcaId);
	}
	
	public List<DropDown> selectJenisForm() throws DataAccessException {
		return query("selectJenisForm",null);
	}
	
	/*
	 * untuk menampilkan list user agency
	 * dian natalia
	 */
	public List selectAllUserAgency(){
		return query("selectAllUserAgency", null);
	}
	
	public List selectAllUserRegional(String lcaId){
		return query("selectAllUserRegional",lcaId);
	}
	
	public List selectAllUserWorksite(String lcaId){
		return query("selectAllUserWorksite",lcaId);
	}
	
	/**Fungsi:	Untuk Menampilkan Email User cabang
	 * @param 	Integer lusId
	 * @return 	User
	 * @author 	Ferry Harlim
	 */
	public User selectLstUser(Integer lusId){
		return (User)querySingle("selectLstUser", lusId);
	}

    /**
     *
     * @param tglAwal            not filled yet
     * @param tglAkhir           not filled yet
     * @param codeToPrintList    not filled yet
     * @return                   not filled yet
     */
    public List selectEkpedisiSpaj( String tglAwal, String tglAkhir, String[] codeToPrintList )
    {
        Map param = new HashMap();
        param.put( "tanggalAwal", tglAwal );
		param.put( "tanggalAkhir", tglAkhir );
		param.put( "codeToPrintList", codeToPrintList );
        return query( "selectEkpedisiSpaj", param );
    }
    /**
     * 
     * Dian natalia
     * untuk menampilkan data
     */

    public List selectAllMstBtpp(String value,String tipe,String filter,String lcaId){
		Map param=new HashMap();
			param.put("tipe",tipe);
			if(value!=null)
				value=value.toUpperCase();
			if(filter==null)
				param.put("kata"," like '%' ||'"+value+"'|| '%'");
			else if(filter.equalsIgnoreCase("LIKE%"))
				param.put("kata"," like '"+value+"'|| '%'");
			else if(filter.equalsIgnoreCase("%LIKE"))
				param.put("kata"," like '%'||'"+value+"' ");
			else
				param.put("kata"," like '%' ||'"+value+"'|| '%'");
			param.put("lca_id", lcaId);
			//logger.info(param);
			return query("cariNomor",param);
	}
    
    /**
     * dian Natalia
     * untuk emngamnil history mencetak BTPP
     */
    
    public List selectLstHistoryPrintBtpp(String mstNo){
		return query("selectLstHistoryPrintBtpp",mstNo);
	}

    /**
     * dian Natalia
     */
//    public List selectMstHistoryBtpp(String mstNo){
//		return query("selectMstHistoryBtpp", mstNo);		
//	}
    
    public List selectMstPolicyBtppNopolis(String mstNo){
		return query("selectMstPolicyBtppNopolis",mstNo );
    }
    
    /**
     * untuk ambil nilai premi
     * @param reg_spaj
     * digunakan pada saat show btpp
     * 
     * @return
     */
    public List selectMstProduct_Insured(String reg_spaj){
		return query("selectMstProduct_Insured",reg_spaj );
    }
    
    public List selectMstCaraByrBtpp(String nomor){
		return query("selectMstCaraByrBtpp(",nomor);
	}
    
    public List selectMstPolicyBtpp(String nomor,String kurs){
		Map map=new HashMap();
		map.put("mstNo",nomor);
		map.put("kurs", kurs);
		return query("selectMstPolicyBtpp",map);
	}
    
//    public List selectMstBtpp(String mst_no){
//    	return query("selectMstBtpp",mst_no);
//    }
    public List selectMstBtpp(String value,String tipe,String filter,String lcaId){
		Map param=new HashMap();
			param.put("tipe",tipe);
			if(value!=null)
				value=value.toUpperCase();
			if(filter==null)
					param.put("kata", " = '"+value+"' ");
			else if(filter.equalsIgnoreCase("%LIKE%"))
				param.put("kata"," like '%' ||'"+value+"'|| '%'");
			else if(filter.equalsIgnoreCase("LIKE%"))
				param.put("kata"," like '"+value+"'|| '%'");
			else if(filter.equalsIgnoreCase("%LIKE"))
				param.put("kata"," like '%'||'"+value+"' ");
			else
				param.put("kata"," like '%' ||'"+value+"'|| '%'");
			param.put("lca_id", lcaId);
			//logger.info(param);
			return query("cariNomor",param);
	}

    
    /**
     * digunakan untuk me-insert mst_BTPP
     */
//    public String processNewFormSpajAgen(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
//		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
//		/** 1. insert kode agen semetnara cabang */
//		if(cmd.getAgen().getMsab_id()==null) {
//			Integer msab_id = (Integer) querySingle("selectLastAgentBranchId", null);
//			if(msab_id == null) msab_id = 0;
//			cmd.getAgen().setMsab_id(msab_id);
//			insert("insertAgentBranch", cmd.getAgen());
//		}
    
    
    public String prosesInputMstBtpp(Btpp btpp, String lusId)throws DataAccessException{
    	SimpleDateFormat sdf = new SimpleDateFormat("20MMyyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
		Date sysdate = commonDao.selectSysdate("dd", false, 0);
		Date begdate = commonDao.selectSysdate("dd", true, 0);
		//Date enddate = add(selectSysdate("yy", true, plan.getMcp_insper()), Calendar.DATE, -1); //lama tanggung dalam tahun, dikurang 1 hari
		btpp.setMcl_id(btpp.getKd_agen());
		btpp.setMcl_first(btpp.getMcl_id());
		btpp.setMst_tgl_input(sysdate);
		btpp.setBiaya_polis(btpp.getBiaya_polis());
		btpp.setLst_kd_cab(btpp.getLst_kd_cab());
		btpp.setBiaya_polis(btpp.getBiaya_polis());
		btpp.setExtra_premi(btpp.getExtra_premi());
		return (String) querySingle("insertMstBtpp", null);
    }
    
    public String selectMstformGetNoBlanko(String msf_id) throws DataAccessException{
    	return (String) querySingle("selectMstformGetNoBlanko", msf_id);
    }
    
    /*
	public Date selectSysdate(String add, boolean trunc, int nilai) throws DataAccessException{
		Map param = new HashMap();
		param.put("add", add);
		param.put("trunc", trunc);
		param.put("nilai", nilai);
		return (Date) querySingle("selectSysdate", param);
	}
	*/
	
	public HashMap  selectGetCounterBtpp(int aplikasi, String cabang) throws DataAccessException {
		Map params = new HashMap();
		params.put("aplikasi", new Integer(aplikasi));
		params.put("cabang", cabang);
		return (HashMap) querySingle("selectGetCounterBtpp", params);
	}
	
	public void updateResetMstCounterBtpp(Integer aplikasi,String cabang){
		Map map=new HashMap();
		map.put("cabang", cabang);
		map.put("aplikasi", aplikasi);
		update("updateResetMstCounterBtpp", map);
	}
	
	
	public Integer selectCekCounterBtppMonthAndYear(Integer aplikasi, String cabang) throws DataAccessException {
		Map params = new HashMap();
		params.put("aplikasi", aplikasi);
		params.put("cabang", cabang);
		return (Integer) querySingle("selectCekCounterBtppMonthAndYear", params);
	}
	 
	public void updateMstCounterBtpp(Double ld_cnt,int ai_id,String ls_cabang){
		Map param=new HashMap();
		param.put("nilai",ld_cnt);
		param.put("aplikasi",new Integer(ai_id));
		param.put("cabang",ls_cabang);
		update("updateMstCounterBtpp",param);
	}
	
	public void insertMstBtpp(Btpp btpp){
		  insert("insertMstBtpp",btpp);
	}

	public List lsSelectAgent(String kd_agen){
		return query("selectAgent", kd_agen);
	}
	
	public String selectAgenTemp() {
		return (String) querySingle("select_agent_temp", null);
	}
	
	public List lsSelectAgenTemp() {
		return (List) query("selectAgentTemp", null);
	}
	
	public void updateMstBtppTglSetor(String mstNo,Date tgl_setor){
		Map map=new HashMap();
		map.put("mst_no",mstNo );
		map.put("tgl_setor",tgl_setor );
		update("update.updateMstBtppTglSetor", map);
	}
	public Date selectMstBtppTglSetor(String mstNo){
		return (Date)querySingle("selectMstBtppTglSetor", mstNo);
	}

	public void deleteMstBtpp(String mstNo){
		delete("deleteMst_btpp",mstNo);
	}
	
	public void updateMstBtppBatal(String mstNo,String mstNoBtl,String mstNoNew,Integer flag){
		Map map=new HashMap();
		map.put("mst_no", mstNo);
		map.put("mst_no_batal", mstNoBtl );
		map.put("mst_no_new", mstNoNew );
		map.put("flag", flag);
		update("updateMstBtppBatal", map);
	}
	
	public void updateMstBtppFlagPrint(String mstNo,Integer flag){
		Map map=new HashMap();
		map.put("mst_no",mstNo);
		map.put("flag",flag);
		update("updateMstBtppFlagPrint",map);
	}
	
	public void updateMstBtppFlagbatal(String mstNo,Integer flag){
		Map map=new HashMap();
		map.put("mst_no",mstNo);
		map.put("flag",flag);
		update("updateMstBtppFlagbatal",map);
	}
	
	public void insertLstHistoryPrint(String mst_no,String mst_tgl,String kode_cabang,String alasan_batal, String mst_no_new, Integer flg_btl){
		Map map=new HashMap();
		map.put("mst_no",mst_no);
		map.put("mst_tgl",mst_tgl);
		map.put("kode_cabang",kode_cabang);
		map.put("alasanbatal",alasan_batal);
		map.put("mst_no_new", mst_no_new);
		map.put("flg_btl", flg_btl);
		insert("insertMstHistoryBtpp",map);
	}
	

	public void insertSpajASM(String lsjs_id,String no_blanko,String lsp_id,String lca_id, String mssd_lus_id, String mssd_desc){
		Map map=new HashMap();
		map.put("lsjs_id",lsjs_id);
		map.put("no_blanko",no_blanko);
		map.put("lsp_id",lsp_id);
		map.put("lca_id",lca_id);
		map.put("mssd_lus_id", mssd_lus_id);
		map.put("mssd_desc", mssd_desc);
		insert("insertSpajASM",map);
	}
	public void insertHstMstBtpp(String mst_no,String mst_tgl,String kode_cabang,String alasan_batal, String mst_no_new){
		Map map=new HashMap();
		map.put("mst_no",mst_no);
		map.put("mst_tgl",mst_tgl);
		map.put("kode_cabang",kode_cabang);
		map.put("alasanbatal",alasan_batal);
		map.put("mst_no_new", mst_no_new);
		insert("insertMstHistoryBtpp",map);

	}
	
	public Integer selectMaxLstHistoryPrintBtpp(String mstNo){
		return (Integer)querySingle("selectMaxLstHistoryPrintBtpp",mstNo);
	}
	
	public List selectViewerBtpp(String lusId,String lcaId,String flagPrint){
		Map map=new HashMap();
		map.put("lusId", lusId);
		map.put("lcaId", lcaId);
		map.put("flagPrint", flagPrint);
		return query("selectViewerBtpp",map);
	}
	
	public Integer selectflagBatal(String mst_no){
		return (Integer) querySingle("selectflagBatal", mst_no);
	}
	
	public void  updateMstBtppFlagbatal(String mst_no){
		update ("updateMstBtppFlagbatal",(mst_no));
	} 
	/**
	 * untuk input setelah batal
	 * @param mst_no
	 * dian
	 */
	public void  updateMstBtppFlagbatal1(String mst_no){
		update ("updateMstBtppFlagbatal1",(mst_no));
	}
	public String selectTglRk(String mst_no){
		return(String) querySingle("selectTglRk", mst_no);
	}
	
	public String selectperiodeAwal(String mst_no){
		return(String) querySingle("selectperiodeAwal", mst_no);
	}
	
	public String selectperiodeAkhr(String mst_no){
		return(String) querySingle("selectperiodeAkhr", mst_no);
	}
	
	public Integer selectCountHist(String mst_no){
		return(Integer) querySingle("selectCountHist", mst_no);
	}
	
	public String selectMstNoNew(String mst_no){
		return (String)querySingle("selectMstNoNew", mst_no);
	}	
	/**
	 * @param spaj
	 * @param insured
	 * @return
	 * @author Ferry Harlim
	 */
	public List selectMstKeluarga(String spaj, Integer insured)
	{
		Map m = new HashMap();
		m.put("spaj",spaj);
		m.put("insured", insured);
		return query("selectMstKeluarga",m);
	}
	
	public List selectPeriode(String periodeAwal, String periodeAkhr){
		Map map = new HashMap();
		map. put("periodeAwal", periodeAwal);
		map.put("periodeAkhr", periodeAkhr);
		return query("selectPeriode", map);
	}

	/**Fungsi : Untuk Update Pengkinian data..
	 * @param cmd
	 * @param err
	 * @return
	 * @author Ferry Harlim
	 */
	
	public Integer select_mst_client_old(String mcl_id)
	{
		return (Integer) querySingle("select_mst_client_old", mcl_id);
	}
	
	public Datausulan selectDataUsulanutama(String spaj)
	{
		return (Datausulan) querySingle("selectDataUsulanutama",spaj);
	}
	
	public Agen select_detilagen(String spaj)
	{
		return (Agen) querySingle("select_detilagen",spaj);
	}
	
	public Agen selectAgentByMsagId(String msag_id){
		return (Agen) querySingle("selectAgentByMsagId", msag_id);
	}
	
	public List selectReportFitrahCard(Map map) {
		return query("selectReportFitrahCard", map);
	}
	
	public List list_mst_keluarga(String spaj, Integer insured)
	{
		Map m = new HashMap();
		m.put("spaj",spaj);
		m.put("insured", insured);
		return query("list_mst_keluarga",m);
	}
	
	public Integer select_count_client(String nopolis, String nama, String tgl)
	{
		Map m = new HashMap();
		m.put("nopolis", nopolis);
		m.put("nama",nama);
		m.put("tgl",tgl);
		return (Integer) querySingle ("select_count_client",m);
	}
	
	public String select_count_client(String nopolis)
	{
		return (String) querySingle("select_count_client",nopolis);
	}
	
	public Map cari_mcl_id(String nopolis)
	{
		return (HashMap) querySingle("cari_mcl_id",nopolis);
	}
	
	public List cari_polis_lain(String mcl_id)
	{
		return query("cari_polis_lain",mcl_id);
	}
	
	public Integer jml_diubah (String reg_spaj)
	{
		return (Integer) querySingle("jml_diubah",reg_spaj);
	}
	
	public List selectkota (String kota)
	{
		return  query("selectkota", kota);
	}
	
	public Map select_groupjob (String kerja)
	{
		return (HashMap)querySingle("select_groupjob",kerja);
	}
	
	public Map select_jabatan (String jabatan)
	{
		return (HashMap)querySingle("select_jabatan",jabatan);
	}
	
	private DataNasabah proc_nasabah1(DataNasabah nasabah1,DataNasabah nasabah,String nopolis,User currentUser) throws ServletException, IOException,Exception
	{
		String reg_spaj = uwDao.selectMstPolicyRegSpaj(nopolis);
		if (reg_spaj == null)
		{
			nasabah1.setProses(null);	
			return nasabah1;
		}else
		{	
			DataNasabah nasabah2 = new DataNasabah();
			nasabah2.setPemegang((Pemegang2)this.selectpemegangpolis(reg_spaj));
			nasabah1.setPemegang((Pemegang2)this.selectpemegangpolis(nasabah.getPemegang().getReg_spaj()));
			nasabah1.getPemegang().setReg_spaj(reg_spaj);
			nasabah1.getPemegang().setMcl_id(nasabah2.getPemegang().getMcl_id());
			
			nasabah2.setAddressBilling((AddressBilling)this.bacDao.selectAddressBilling(reg_spaj));
			nasabah1.setAddressBilling((AddressBilling)this.bacDao.selectAddressBilling(nasabah.getAddressBilling().getReg_spaj()));
			nasabah1.getAddressBilling().setReg_spaj(reg_spaj);
			
			nasabah2.setTertanggung((Tertanggung)this.bacDao.selectttg(reg_spaj));
			nasabah1.setTertanggung((Tertanggung)this.bacDao.selectttg(nasabah.getTertanggung().getReg_spaj()));
			nasabah1.getTertanggung().setReg_spaj(reg_spaj);
			nasabah1.getTertanggung().setMcl_id(nasabah2.getTertanggung().getMcl_id());
			
			nasabah1.setDatausulan((Datausulan) this.selectDataUsulanutama(reg_spaj));
			nasabah2.setDatausulan((Datausulan) this.selectDataUsulanutama(reg_spaj));
			
			nasabah1.setAgen((Agen)this.select_detilagen(reg_spaj));


			
			nasabah.getDatausulan().setStatus_submit("");	
			
//			data keluarga yang sudah diganti
			List keluarga_pp = this.list_mst_keluarga(nasabah.getPemegang().getReg_spaj(), new Integer(1));
			if (keluarga_pp.size() == 0)
			{
				keluarga_pp= new ArrayList();
				if (keluarga_pp.size() == 0)
				{
					Keluarga data = new Keluarga();
					data.setNo(new Integer(1));
					data.setLsre_id(new Integer(5));
					data.setLsre_relation("SUAMI/ISTRI");
					data.setInsured(new Integer(1));
					data.setReg_spaj(reg_spaj);
					keluarga_pp.add(data);
					
					for (int i = 0 ; i <3 ; i++)
					{
						Keluarga data1 = new Keluarga();
						data1.setNo(new Integer(i + 2));
						data1.setLsre_id(new Integer(4));
						data1.setLsre_relation("Anak" + (i+1));
						data.setInsured(new Integer(1));
						data.setReg_spaj(reg_spaj);
						keluarga_pp.add(data1);
					}
				}
			}
			
			
			List keluarga_ttg = this.list_mst_keluarga(nasabah.getTertanggung().getReg_spaj(), new Integer(0));
			if (keluarga_ttg.size() == 0)
			{
				keluarga_ttg=  new ArrayList();
				if (keluarga_ttg.size() == 0)
				{
					Keluarga data = new Keluarga();
					data.setNo(new Integer(1));
					data.setLsre_id(new Integer(5));
					data.setLsre_relation("SUAMI/ISTRI");
					data.setInsured(new Integer(0));
					data.setReg_spaj(reg_spaj);
					keluarga_ttg.add(data);
					
					for (int i = 0 ; i <3 ; i++)
					{
						Keluarga data1 = new Keluarga();
						data1.setNo(new Integer(i + 2));
						data1.setLsre_id(new Integer(4));
						data1.setLsre_relation("Anak" + (i+1));
						data.setInsured(new Integer(0));
						data.setReg_spaj(reg_spaj);
						keluarga_ttg.add(data1);
					}
				}
			}
			nasabah1.getPemegang().setListKeluarga(keluarga_pp);
			nasabah1.getTertanggung().setListKeluarga(keluarga_ttg);
	
			
			String mcl_id_pp = nasabah1.getPemegang().getMcl_id();
			String mcl_id_ttg = nasabah1.getTertanggung().getMcl_id();
			proc_nasabah(nasabah1,nasabah,currentUser);
			nasabah1.setProses(reg_spaj);
			return nasabah1;
		}
		
	}
	
	private void proc_nasabah(DataNasabah nasabah1,DataNasabah edit,User currentUser)throws ServletException,IOException,Exception
	{
		//sumber dana
		String sumber_dana=null;//--
		if (nasabah1.getPemegang().getDanaa()=="")
		{
			sumber_dana = nasabah1.getPemegang().getMkl_pendanaan().toUpperCase();
		}else{
			sumber_dana = nasabah1.getPemegang().getDanaa().toUpperCase();
		}
		nasabah1.getPemegang().setMkl_pendanaan(sumber_dana);
		
		
		String sumber_hasil=null;//--
		if (nasabah1.getPemegang().getDanaa()=="")
		{
			sumber_dana = nasabah1.getPemegang().getMkl_pendanaan().toUpperCase();
		}else{
			sumber_dana = nasabah1.getPemegang().getDanaa().toUpperCase();
		}
		nasabah1.getPemegang().setMkl_pendanaan(sumber_dana);
		
		//pekerjaan
		String pekerjaan =null;//--
		if (nasabah1.getPemegang().getKerjaa()=="")
		{
			pekerjaan = nasabah1.getPemegang().getMkl_kerja().toUpperCase();
		}else{
			pekerjaan = nasabah1.getPemegang().getKerjaa().toUpperCase();
		}
		nasabah1.getPemegang().setMkl_kerja(pekerjaan);

		//group job
		String groupjob_pp = null;
		Map data_3= (HashMap) this.select_groupjob(pekerjaan);
		if (data_3!=null)
		{		
			groupjob_pp = (String)data_3.get("LGJ_ID");
		}else{
			groupjob_pp="";
		}
		nasabah1.getPemegang().setLgj_id(groupjob_pp);
		
		//jabatan
		String jabatan = nasabah1.getPemegang().getKerjab();
		String jbtn_pp ="";
		if(jabatan!=null)
		if (jabatan.trim().length()!=0)
		{
			pekerjaan = jabatan;
			Map data_4= (HashMap) this.select_jabatan(jabatan);
			if (data_4!=null)
			{		
				jbtn_pp = (String)data_4.get("LJB_ID");
			}else{
				jbtn_pp = "";
			}
		}
		nasabah1.getPemegang().setLjb_id(jbtn_pp);
		
		//bidang industri
		String bidang = null;//--
		if (nasabah1.getPemegang().getIndustria()=="")
		{
			bidang=nasabah1.getPemegang().getMkl_industri().toUpperCase();
		}else{
			bidang=nasabah1.getPemegang().getIndustria().toUpperCase();
		}	
		nasabah1.getPemegang().setMkl_industri(bidang);
		if (nasabah1.getPemegang().getMkl_kerja().equalsIgnoreCase("KARYAWAN SWASTA"))
		{
			nasabah1.getPemegang().setMpn_job_desc(jabatan);
		}else{
			nasabah1.getPemegang().setMpn_job_desc(bidang);
		}

		
		Integer jumlah = this.select_mst_client_old(nasabah1.getPemegang().getMcl_id());
		if (jumlah == null)
		{
			jumlah = new Integer(0);
		}
		
//		if (jumlah.intValue() == 0)
//		{
			insert("insert_client_old", nasabah1.getPemegang().getMcl_id());
			insert("insert_address_old",nasabah1.getPemegang().getMcl_id());
//		}
			update("update_mst_client_new_pp", nasabah1.getPemegang());
			update("update_mst_address_new_pp", nasabah1.getPemegang());
			
		Integer jumlah_old = this.jml_diubah(edit.getAddressBilling().getReg_spaj());
		if (jumlah_old == null)
		{
			jumlah_old = new Integer(0);
		}
//		if (jumlah_old.intValue() == 0)
//		{
			insert("insert_address_billing_old", nasabah1.getAddressBilling().getReg_spaj());
//		}
		update("update_mst_address_billing", nasabah1.getAddressBilling());
		
		//pengecekan realasi antara pp dan tt
		//jika pp dan tt null maka di cekin apakah mcl_id sama. jika sama lsre_id=1
		//kalo beda maka untuk sementara di kasih lsre_id=20 (Sementara lsre_id sebelumnya NULL)
		if(nasabah1.getPemegang().getLsre_id()==null){
			if(nasabah1.getPemegang().getMcl_id()==nasabah1.getTertanggung().getMcl_id()){
				nasabah1.getPemegang().setLsre_id(1);//diri sendiri karena id pp dan tt sama
				//update tabel mst_policy lsre_id=1(diri sendiri) lstbId=1(Individu)
				updateMstPolicyLsreId(nasabah1.getSpajAwal(),1,1);
			}else{
				nasabah1.getPemegang().setLsre_id(20);
				//update tabel mst_policy lsre_id=20(Relasi Sementara) lstbId=1(Individu)
				updateMstPolicyLsreId(nasabah1.getSpajAwal(),20,1);
			}
		}
		if (nasabah1.getPemegang().getLsre_id() != 1)
		{
			

			//sumber dana
			String sumber_dana_ttg=null;//--
			if (nasabah1.getTertanggung().getDanaa()=="")
			{
				sumber_dana_ttg = nasabah1.getTertanggung().getMkl_pendanaan().toUpperCase();
			}else{
				sumber_dana_ttg = nasabah1.getTertanggung().getDanaa().toUpperCase();
			}
			nasabah1.getTertanggung().setMkl_pendanaan(sumber_dana_ttg);
			
			//pekerjaan
			String pekerjaan_ttg =null;//--
			if (nasabah1.getTertanggung().getKerjaa()=="")
			{
				pekerjaan_ttg = nasabah1.getTertanggung().getMkl_kerja().toUpperCase();
			}else{
				pekerjaan_ttg = nasabah1.getTertanggung().getKerjaa().toUpperCase();
			}
			nasabah1.getTertanggung().setMkl_kerja(pekerjaan_ttg);
			
			//group job
			String groupjob_ttg = null;
			Map data_1= (HashMap) this.select_groupjob(pekerjaan_ttg);
			if (data_1!=null)
			{		
				groupjob_ttg = (String)data_1.get("LGJ_ID");
			}else{
				groupjob_ttg="";
			}
			nasabah1.getTertanggung().setLgj_id(groupjob_ttg);
			
			//jabatan
			String jabatan_ttg = (nasabah1.getTertanggung().getKerjab()==null?"":nasabah1.getTertanggung().getKerjab()).toUpperCase();
			String jbtn_ttg ="";
			if (jabatan_ttg.trim().length()!=0)
			{
				pekerjaan_ttg = jabatan_ttg;
				
				Map data_2= (HashMap) this.select_jabatan(jabatan_ttg);
				if (data_2!=null)
				{		
					jbtn_ttg = (String)data_2.get("LJB_ID");
				}else{
					jbtn_ttg="";
				}
			}
			nasabah1.getTertanggung().setLjb_id(jbtn_ttg);
			
			//bidang industri
			String bidang_ttg = null;//--
			if (nasabah1.getTertanggung().getIndustria()=="")
			{
				bidang_ttg=nasabah1.getTertanggung().getMkl_industri().toUpperCase();
			}else{
				bidang_ttg=nasabah1.getTertanggung().getIndustria().toUpperCase();
			}
			nasabah1.getTertanggung().setMkl_industri(bidang_ttg);
			if (nasabah1.getTertanggung().getMkl_kerja().equalsIgnoreCase("KARYAWAN SWASTA"))
			{
				nasabah1.getTertanggung().setMpn_job_desc(jabatan_ttg);
			}else{
				nasabah1.getTertanggung().setMpn_job_desc(bidang_ttg);
			}
			
			Integer jumlah1 = this.select_mst_client_old(nasabah1.getTertanggung().getMcl_id());
			if (jumlah1 == null)
			{
				jumlah1 = new Integer(0);
			}
			
//			if (jumlah1.intValue() == 0)
//			{
				insert("insert_client_old", nasabah1.getTertanggung().getMcl_id());
				insert("insert_address_old", nasabah1.getTertanggung().getMcl_id());
//			}
				update("update_mst_client_new_ttg", nasabah1.getTertanggung());
				update("update_mst_address_new_ttg", nasabah1.getTertanggung());
				
		}
		
//		if (jumlah_old.intValue() == 0)
//		{
			insert("insert_mst_keluarga_old",nasabah1.getPemegang().getReg_spaj());
//		}
		delete("delete_mst_keluarga",nasabah1.getPemegang().getReg_spaj());

		List data_keluarga_pp = edit.getPemegang().getListKeluarga();
		for (int count = 0 ; count < data_keluarga_pp.size() ; count++)
		{
			Keluarga keluarga = (Keluarga) data_keluarga_pp.get(count);
			if (keluarga.getNama() == null)
			{
				keluarga.setNama("");
			}
			if (!keluarga.getNama().equalsIgnoreCase(""))
			{
				keluarga.setReg_spaj(nasabah1.getPemegang().getReg_spaj());
				keluarga.setInsured(new Integer(1));
				insert("insert_mst_keluarga",keluarga);
			}
		}
		
		List data_keluarga_ttg = edit.getTertanggung().getListKeluarga();
		for (int count = 0 ; count < data_keluarga_ttg.size() ; count++)
		{
			Keluarga keluarga = (Keluarga) data_keluarga_ttg.get(count);
			if (keluarga.getNama() == null)
			{
				keluarga.setNama("");
			}
			if (!keluarga.getNama().equalsIgnoreCase(""))
			{
				keluarga.setReg_spaj(nasabah1.getTertanggung().getReg_spaj());
				keluarga.setInsured(new Integer(0));
				insert("insert_mst_keluarga",keluarga);
			}
		}
		
		//insert history
		Map param = new HashMap();
		param.put("reg_spaj", nasabah1.getPemegang().getReg_spaj());
		param.put("jenis", "UPDATING DATA");
		param.put("status_polis",nasabah1.getPemegang().getLssp_id());
		param.put("lus_id", null);
		param.put("keterangan", nasabah1.getPemegang().getId());
		insert("insert_lst_ulangan",param);
	}
	
	/**Fungsi : Untuk mengupdate relasi dari mst_policy
	 * @param	String spaj, Integer lsreId, Integer lstbId
	 * @author 	Ferry Harlim
	 **/
	public void updateMstPolicyLsreId(String spaj, Integer lsreId,Integer lstbId) {
		Map map=new HashMap();
		map.put("reg_spaj",spaj);
		map.put("lsre_id",lsreId);
		map.put("lstb_id", lstbId);
		update("updateMstPolicyLsreId",map);
		
	}

	private void insertMstHistoryClient(DataNasabah nasabah1,String regSpaj,User currentUser) throws Exception{
		Pemegang2 pemegang=selectpemegangpolis(regSpaj);
		Tertanggung tertanggung =bacDao.selectttg(regSpaj);
		Datausulan dataUsulan=selectDataUsulanutama(regSpaj);
		ClientHistory clientHistory=new ClientHistory();
		clientHistory.setMspo_policy_no(nasabah1.getNo_polis());
		clientHistory.setLssh_id(nasabah1.getLssh_id());//eka.lst_status_history
		clientHistory.setMsch_status_input(1);//Update Nasabah Oleh BAS
		clientHistory.setMsch_bas_tgl_terima(FormatDate.toDateWithSlash(nasabah1.getMsch_bas_tgl_terima()));
		clientHistory.setLus_id(Integer.valueOf(currentUser.getLus_id()));
		clientHistory.setMsch_alamat(nasabah1.getPemegang().getAlamat_rumah());
		clientHistory.setMsch_cabang(nasabah1.getPemegang().getLca_nama());
		clientHistory.setMsch_kota(nasabah1.getPemegang().getKota_rumah());
		clientHistory.setMsch_nama_pp(pemegang.getMcl_first());
		clientHistory.setMsch_nama_produk(dataUsulan.getLsdbs_name());
		clientHistory.setMsch_nama_tt(tertanggung.getMcl_first());
		clientHistory.setMsch_no_seri(pemegang.getMspo_no_blanko());
		Integer noUrut=commonDao.selectMaxUrutMstClientHistory(nasabah1.getNo_polis());
		if(noUrut==null)
			noUrut=0;
		clientHistory.setMsch_no_urut(noUrut+1);
		clientHistory.setMsch_penerima(null);
		clientHistory.setMsch_zip_code(nasabah1.getPemegang().getKd_pos_rumah());
		clientHistory.setMsch_history(nasabah1.getAlasan());
		commonDao.insertMstClientHistory(clientHistory);
		
	}
	
//	private void insertMstHistoryClient(DataNasabah nasabah1,User currentUser) throws Exception{
//		ClientHistory clientHistory=new ClientHistory();
//		clientHistory.setMsch_bas_tgl_terima(nasabah1.getMsch_bas_tgl_terima());
//		clientHistory.setMspo_policy_no(nasabah1.getNo_polis());
//		clientHistory.setLssh_id(7);//Update Nasabah Oleh BAS
//		clientHistory.setLus_id(Integer.valueOf(currentUser.getLus_id()));
//		clientHistory.setMsch_alamat(nasabah1.getPemegang().getAlamat_rumah().trim());
//		clientHistory.setMsch_cabang(nasabah1.getPemegang().getLca_nama());
//		clientHistory.setMsch_kota(nasabah1.getPemegang().getKota_rumah());
//		clientHistory.setMsch_nama_pp(nasabah1.getPemegang().getMcl_first().trim());
//		clientHistory.setMsch_nama_produk(nasabah1.getDatausulan().getLsdbs_name().trim());
//		clientHistory.setMsch_nama_tt(nasabah1.getTertanggung().getMcl_first().trim());
//		clientHistory.setMsch_no_seri(nasabah1.getPemegang().getMspo_no_blanko());
//		Integer noUrut=commonDao.selectMaxUrutMstClientHistory(nasabah1.getNo_polis());
//		if(noUrut==null)
//			noUrut=0;
//		clientHistory.setMsch_no_urut(noUrut+1);
//		clientHistory.setMsch_penerima(null);
//		clientHistory.setMsch_zip_code(nasabah1.getPemegang().getKd_pos_rumah());
//		commonDao.insertMstClientHistory(clientHistory);
//		
//	}

	public DataNasabah prosesUpdateNasabah(Object cmd, User currentUser,BindException err) throws DataAccessException
	{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		//data dari win utama
		DataNasabah edit = (DataNasabah) cmd;
		//data polis lain2 yang ditimpan
 		DataNasabah nasabah1 = (DataNasabah) cmd;
		//data lama dari polis lain2 yang akan ditimpa
 		//data untuk insert ke tabel client History
 		DataNasabah nasabahClient= (DataNasabah) cmd;
 		nasabahClient.setMsch_bas_tgl_terima(edit.getMsch_bas_tgl_terima());
		try {
			//insert ke tabel mst history client
	 		nasabah1.setMsch_bas_tgl_terima(edit.getMsch_bas_tgl_terima());
	 		if(nasabah1.getLssh_id()==1)
	 			updateMstInsuredMsteFlagNasabah(nasabah1.getPemegang().getReg_spaj(),nasabah1.getLssh_id());
	 		insertMstHistoryClient(nasabah1,nasabah1.getPemegang().getReg_spaj(), currentUser);
			nasabah1.setProses("");
			proc_nasabah(nasabah1,edit,currentUser);
			//save data utama
			String nopolis="";
			nasabah1.getDatausulan().setNo_polis(nopolis);
			List a1 = edit.getListPolisLain1();
			Integer count_a1 = a1.size();
			Integer lsshId=nasabah1.getLssh_id();
			for (int j=0;j<count_a1.intValue() ;j++)
			{
				Policy polis = (Policy) a1.get(j);
				nopolis = polis.getMspo_policy_no();
				
				if (nopolis == null)
				{
					nopolis ="";
				}
				Integer cek = polis.getCek();
				if (!nopolis.equalsIgnoreCase(""))
				{	
					if (cek != null)
					{
						if (cek.intValue() == 1)
						{
							nasabahClient.setNo_polis(nopolis);
							insertMstHistoryClient(nasabahClient,uwDao.selectMstPolicyRegSpaj(nopolis),currentUser);
							if(lsshId==1)
					 			updateMstInsuredMsteFlagNasabah(uwDao.selectMstPolicyRegSpaj(nopolis),nasabah1.getLssh_id());
					 		
							nasabah1 = new DataNasabah();
							//logger.info(nopolis);
					 		nasabah1=proc_nasabah1(nasabah1,edit,nopolis,currentUser);
						}
					}
				}
			}
			
		/*	List a2 = edit.getListPolisLain2();
			Integer count_a2 = a2.size();
			for (int j=0;j<count_a2.intValue() ;j++)
			{
				nopolis = (String) a2.get(j);
				if (nopolis == null)
				{
					nopolis ="";
				}
				if (!nopolis.equalsIgnoreCase(""))
				{	
					nasabah1 = new DataNasabah();
					nasabah1.setNo_polis(nopolis);
					proc_nasabah1(nasabah1,edit,nopolis);
				}
			}
			
			List a3 = edit.getListPolisLain3();
			Integer count_a3 = a3.size();
			for (int j=0;j<count_a3.intValue() ;j++)
			{
				nopolis = (String) a3.get(j);
				if (nopolis == null)
				{
					nopolis ="";
				}
				if (!nopolis.equalsIgnoreCase(""))
				{	
					nasabah1 = new DataNasabah();
					nasabah1.setNo_polis(nopolis);
					proc_nasabah1(nasabah1,edit,nopolis);
				}
			}*/
			edit.setProses(nasabah1.getProses());
			if(nasabah1.getProses()!=null)
				edit.getDatausulan().setStatus_submit("berhasil");
			else{
				edit.getDatausulan().setStatus_submit("gagal");
				edit.getDatausulan().setKeterangan("Nomor polis "+nasabah1.getNo_polis()+" tidak bisa diupdate, silahkan hilangkan dari daftar polis.");
				TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			}

		} catch (Exception e) {
			edit.setProses(null);
			edit.getDatausulan().setStatus_submit("gagal");
			edit.getDatausulan().setKeterangan("Nomor polis "+nasabah1.getNo_polis()+" tidak bisa diupdate, silahkan hilangkan dari daftar polis.");
			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
			logger.error("ERROR :", e);
			StringBuffer stackTrace = new StringBuffer();
			stackTrace.append("<table>");
			stackTrace.append("<tr><td>ID  User  : "+currentUser.getLus_id()+"</td></tr>");
			stackTrace.append("<tr><td>Nama User : "+currentUser.getLus_full_name()+"</td></tr>");
			stackTrace.append("<tr><td>Reg SPAJ  : "+edit.getSpajAwal()+"</td></tr>");
			stackTrace.append("<tr><td>" + e.toString()+"</td></tr>");
			for(int i=0; i< e.getStackTrace().length; i++) {
				stackTrace.append("<tr><td>    at " + e.getStackTrace()[i]+"</td></tr>");
			}
			stackTrace.append("<table>");
			try {
				email.send(true, 
						props.getProperty("admin.ajsjava"), new String[] {props.getProperty("admin.yusuf")}, null, null,
						"Error Pengkinian Data Oleh BAS", stackTrace.toString(), null);
			} catch (MailException e1) {
				logger.error("ERROR :", e1);
			} catch (MessagingException e1) {
				logger.error("ERROR :", e1);
			}
		}
		return edit;
	}
	
	private void updateMstInsuredMsteFlagNasabah(String reg_spaj, Integer lssh_id) {
		Map param=new HashMap();
		param.put("reg_spaj", reg_spaj);
		param.put("mste_flag_update_nasabah", lssh_id);
		update("updateMstInsuredMsteFlagNasabah",param);
	}

	public Pemegang2 selectpemegangpolis(String spaj)
	{
		return (Pemegang2) querySingle("selectpemegangpolis",spaj);
	}
		/**
	 * @param noIdentity
	 * @param nopolis
	 * @return
	 * @throws DataAccessException
	 * @author Ferry Harlim
	 */
	public List selectAllPolicy(String mclId,String nopolis) throws DataAccessException{
		Map<String, String> param=new HashMap<String, String>();
		param.put("mcl_id", mclId);
		param.put("nopolis", nopolis);
	//	param.put("date_birth", dateBirth);
		return query("selectAllPolis", param);
	}
	
	public String selectMstPolicyRegSpaj(String nopolis)throws DataAccessException{
		return (String)querySingle("selectMstPolicyRegSpaj", nopolis);
	}
	
	/**
	 * @param flag  1 = Pencarian berdasarkan id agen (msag_id).
	 * 				2 = Pencarian berdasarkan nama agen (mcl_first).
	 * 				3 = Pencarian Berdasarkan No. Identitas	(mspe_no_identity).1
	 * @param query
	 * @return
	 */
	public List selectAllAgent(Integer flag,String lcaId,String query)throws DataAccessException{
		Map param=new HashMap();
		param.put("flag",flag);
		param.put("lca_id",lcaId);
		param.put("query",query);
		return query("selectAllAgent",param);
	}
	/**Fungsi : Untuk Mencek apakah suatu agen termasuk dalam akses dari admin
	 * 
	 * @param lusId
	 * @param msfId
	 * @return
	 * @throws DataAccessException
	 */
	public Integer selectCekAgenInUserAdmin(String lusId,String msfId)throws DataAccessException{
	   Map param=new HashMap();
	   param.put("lus_id",lusId);
	   param.put("msf_id",msfId);
	   return (Integer)querySingle("selectCekAgenInUserAdmin",param);
	}
	
	/**Fungsi : Untuk mengupdate lus_id dari tabel eka.mst_form (proses endorse form agen)  
	 * 
	 * @param lusId
	 * @param msfId
	 * @author Ferry Harlim
	 */
	public void updateMstFormLusId(String lusId,String msfId)throws DataAccessException{
		   Map param=new HashMap();
		   param.put("lus_id",lusId);
		   param.put("msf_id",msfId);
			update("updateMstFormLusId",param);   
	}

	/**Fungsi : Untuk mengupdate lus_id dari tabel eka.mst_spaj(proses endorse form agen)  
	 * 
	 * @param lusId
	 * @param msfId
	 * @author Ferry Harlim
	 */
	public void updateMstSpajLusId(String lusId,String msfId)throws DataAccessException{
		   Map param=new HashMap();
		   param.put("lus_id",lusId);
		   param.put("msf_id",msfId);
			update("updateMstSpajLusId",param);   
	}
	/**Fungsi : Untuk mengupdate lus_id dari tabel eka.mst_spaj_det (proses endorse form agen)  
	 * 
	 * @param lusId
	 * @param msfId
	 * @author Ferry Harlim
	 */

	public void updateMstSpajDetLusId(String lusId,String msfId)throws DataAccessException{
		   Map param=new HashMap();
		   param.put("lus_id",lusId);
		   param.put("msf_id",msfId);
			update("updateMstSpajDetLusId",param);   
	}
	public void insertFormHistory(String msfId,String lusId,Integer posisi,String desc)throws DataAccessException{
		FormHist form = new FormHist();
		form.setMsf_id(msfId);
		form.setMsf_urut(selectNoUrutFormHistory(msfId));
		form.setPosisi(posisi);
		form.setMsfh_lus_id(Integer.valueOf(lusId));
		form.setMsfh_desc(desc);			
		insert("insertFormHistory", form);
	}	
	
	/**Fungsi : untuk menampilkan data polis yang sudah Terupdate (lengkap) polisnya oleh nasabah/bas 
	 * @return
	 * @throws DataAccessException
	 */
	public List selectbasCekDataInputData(String tanggalAwal,String tanggalAkhir,String lcaId)throws DataAccessException{
		Map param=new HashMap();
		param.put("tanggalAwal", tanggalAwal);
		param.put("tanggalAkhir", tanggalAkhir);
		param.put("lcaId", lcaId);
		return query("selectbasCekDataInputData",param);
	}

	/**Fungsi : untuk menampilkan data polis yang belum terupdate polisnya oleh nasabah/bas 
	 * @return
	 * @throws DataAccessException
	 */
	public List selectbasCekDataBalik(String tanggalAwal,String tanggalAkhir,String lcaId)throws DataAccessException{
		Map param=new HashMap();
		param.put("tanggalAwal", tanggalAwal);
		param.put("tanggalAkhir", tanggalAkhir);
		param.put("lcaId", lcaId);
		return query("selectbasCekDataBalik",param);
	}
	
	/**Fungsi : untuk menampilkan data (VENDOR) polis yang belum terupdate polisnya oleh nasabah/bas 
	 * @return
	 * @throws DataAccessException
	 */
	public ClientHistory selectDataVendor(String mspoPolicyNo)throws DataAccessException{
		return (ClientHistory)querySingle("selectDataVendor",mspoPolicyNo);
	}
	
	/**Fungsi : untuk menampilkan data (NASABAH) polis yang belum terupdate polisnya oleh nasabah/bas 
	 * @return
	 * @throws DataAccessException
	 */
	public ClientHistory selectDataNasabahUpdate(String mspoPolicyNo)throws DataAccessException{
		return (ClientHistory) querySingle("selectDataNasabahUpdate",mspoPolicyNo);
	}
	
	public String updateTanggalPertanggungjawaban(String kodeAgent, String noBlanko, String tanggal) {
		Map param = new HashMap();
		param.put("kodeAgent", kodeAgent);
		param.put("noBlanko", noBlanko);
		param.put("tanggal", tanggal);
		
		//List<HashMap> param2 = query("getMsfIdMssdDt",param);
		update("updateTanggalFormHist",param);
		update("updateTanggalSpajDet", param);
		return "sukses update";
	}
	
	public List<Map> selectFitrah21Hari(FormSpaj formSpaj) throws DataAccessException {
		return query("selectFitrah21Hari",formSpaj);
	}
	
	public void prosesGenerate21HariFitrah() throws DataAccessException {
		List<Map> lsCabang = uwDao.selectlstCabang();
		String dir = props.getProperty("pdf.dir.report") + "\\fitrah\\";
		Connection conn = null;
		for(Map content : lsCabang) {
			FormSpaj formSpaj = new FormSpaj();
			formSpaj.setMss_jenis(0);
			formSpaj.setLca_id(content.get("KEY").toString());
			formSpaj.setMsab_id(0);
			
			List<Map> Fitrah21Hari = selectFitrah21Hari(formSpaj);
			List<File> attachments = new ArrayList<File>();
			if(!Fitrah21Hari.isEmpty()) {
				try {
					conn = this.getDataSource().getConnection();
					// buat attachment	
					for(int a=0;a<Fitrah21Hari.size();a++) {
						Map params = new HashMap();
						params.put("msf_id", Fitrah21Hari.get(a).get("MSF_ID_ASLI").toString());
						params.put("prefix", "F");
						JasperUtils.exportReportToPdf(
								props.getProperty("report.bas.prtgjwbFormSpaj")+".jasper", 
								dir, 
								"permintaan no " + Fitrah21Hari.get(a).get("MSF_ID_ASLI").toString() +".pdf", 
								params, 
								conn, 
								PdfWriter.AllowPrinting, null, null);
						
						File sourceFile = new File(dir+"\\permintaan no " + Fitrah21Hari.get(a).get("MSF_ID_ASLI").toString() +".pdf");
						attachments.add(sourceFile);
					}
				
					// kirim ke cabang
					List daftarEmail = uwDao.selectCabangEmail(content.get("KEY").toString());
					String [] emailAdmin = new String[daftarEmail.size()];
					for(int a=0;a<daftarEmail.size();a++) {
						if(daftarEmail.get(a) != null) {
							emailAdmin[a] = new String(daftarEmail.get(a).toString());
							logger.info("============================= email cabang : " + emailAdmin[a] + " =============================");
						}	
						else 
							emailAdmin[a] = new String();
					}
					email.send(true, 
							props.getProperty("admin.ajsjava"), 
							//emailAdmin, 
							//new String[]{"yune@sinarmasmsiglife.co.id","hendra@sinarmasmsiglife.co.id"},
							new String[]{},new String[]{},
							new String[]{"deddy@sinarmasmsiglife.co.id"}, 
							"Laporan Fitrah Card Outstanding Per " + completeDateFormat.format(new Date()),
							"Berikut adalah Laporan Harian Fitrah Card Outstanding Per " + completeDateFormat.format(new Date())+
							" yang dikirim secara otomatis oleh sistem E-Lions", 
							attachments);
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}finally{
					closeConnection(conn);
				}				
			}
		}
		//logger.info("finish " + new Date());	
	}

    public List<Map<String, Object>> selectStableRO( Map<String, Object> params ) throws DataAccessException
    {
		return query( "selectStableRO", params );
	}
    
    public List<DropDown> selectCabBmi() {
    	return query("selectCabBmi",null);
    }
    
    public List<DropDown> selectCabBmiForReport() {
    	return query("selectCabBmiForReport",null);
    }
    
    public List<DropDown> selectPermintaan(String lus_id) {
    	return query("selectPermintaanBandara",lus_id);
    } 
    
    public List<DropDown> selectAdminBandara() {
    	return query("selectAdminBandara",null);
    }    
    
    public List<DropDown> selectNoBlankoBandara(String lus_id, String lca_id) {
    	Map param=new HashMap<String, Object>();
    	param.put("lus_id", lus_id);
    	param.put("lca_id", lca_id);  
    	
    	return query("selectNoBlankoBandara", param);
    }
    
    public List<Smsserver_in> selectSmsserver_in(String beg_date, String end_date, Integer update,Integer process,Integer id,Integer lus_id,Integer followup, Map params)throws DataAccessException{
    	Map param=new HashMap<String, Object>();
    	param.put("beg_date", beg_date);
    	param.put("end_date", end_date);
    	param.put("update", update);
    	param.put("process", process);
    	param.put("id",id);
    	param.put("lus_id",lus_id);
    	param.put("followup", followup);
    	if (params!=null)
		{
			param.putAll(params);
		}    	
    	return query("selectSmsserver_in", param);
    }
    
    public String selectTotalSmsserver_in(String beg_date, String end_date, Integer update,Integer process,Integer id,Integer lus_id,Integer followup, Map params)throws DataAccessException{
    	Map param=new HashMap<String, Object>();
    	param.put("beg_date", beg_date);
    	param.put("end_date", end_date);
    	param.put("update", update);
    	param.put("process", process);
    	param.put("id",id);
    	param.put("lus_id",lus_id);
    	param.put("followup", followup);
    	if (params!=null)
		{
			param.putAll(params);
		}
    	return (String) querySingle("selectTotalSmsserver_in", param);
    }
    
    public List<Smsserver_out> selectSmsserver_out(Integer id,Integer lus_id)throws DataAccessException{
    	Map param=new HashMap<String, Object>();    	
    	param.put("id",id);
    	param.put("lus_id",lus_id);    	
    	return query("selectSmsserver_out", param);
    }
    
    public List<Smsserver_in> selectSmsserver_in_by_originator(String originator)throws DataAccessException{
      	return query("selectSmsserver_in_by_originator", originator);
    }
    
    public List<Smsserver_out> selectSmsserver_out_by_recipient(String recipient)throws DataAccessException{
  		return query("selectSmsserver_out_by_recipient", recipient);
    }
    
    public void updateSmsserver_in(Smsserver_in sms_in)throws DataAccessException{
    	update("updateSmsserver_in", sms_in);
    }
    
    public void insertSmsserver_out(Smsserver_out sms_out,boolean default_param)throws DataAccessException{
    	if(default_param){
    		sms_out.setType("O");
    		sms_out.setOriginator("");
    		sms_out.setErrors(0);
    		sms_out.setEncoding("7");
    		sms_out.setStatus_report(1);
    		sms_out.setFlash_sms(0);
    		sms_out.setSrc_port(-1);
    		sms_out.setDst_port(-1);
    		sms_out.setGateway_id("*");
    	}
    	
    	insert("insertSmsserver_out", sms_out);
    }
    
    public void insertSmsServerOutWithGateway(Smsserver_out sms_out,boolean default_param)throws DataAccessException{
    	if(default_param){
    		sms_out.setType("O");
    		sms_out.setOriginator("");
    		sms_out.setErrors(0);
    		sms_out.setEncoding("7");
    		sms_out.setStatus_report(1);
    		sms_out.setFlash_sms(0);
    		sms_out.setSrc_port(-1);
    		sms_out.setDst_port(-1);
    		sms_out.setGateway_id(sms_out.getRecipient());
    	}
    	insert("insertSmsServerOutWithGateway", sms_out);
    }
    
    public String getMaxPermintaanId(String lus_id) throws DataAccessException {
    	return (String) querySingle("getMaxPermintaanId", lus_id);
    }
    
    public String getKodeBandara(String lus_id) throws DataAccessException {
    	return (String) querySingle("getKodeBandara", lus_id);
    }
    
    public HashMap selectFormPermintaanBandara(String id) throws DataAccessException {
    	return (HashMap) querySingle("selectFormPermintaanBandara", id);
    }  
    
    public Integer cekAdmTravIns(String lus_id) {
    	return (Integer) querySingle("cekAdmTravIns", lus_id);
    }
    
    
    
    public Boolean cekNoBlankoIsExist(String lus_id,String lca_id, Integer lsjs_id, String no_blanko,Integer msab_id, Integer mss_jenis, Integer jnsTravIns) throws DataAccessException {
    	Map param = new HashMap();    	
    	param.put("lus_id",lus_id);
    	param.put("lca_id",lca_id);
    	param.put("lsjs_id",lsjs_id);
    	param.put("no_blanko",no_blanko);
    	param.put("msab_id",msab_id); 
    	param.put("mss_jenis",mss_jenis); 
    	param.put("jnsTravIns",jnsTravIns);
    	
    	Integer exist = (Integer) querySingle("cekNoBlankoIsExist", param);
    	if(exist == 1) return true;
    	return false;
    }
    
    public void insertFormBandara(String id,String date,String nama,String jumlh,String noBlanko,String lus_id, Integer jenis) throws DataAccessException {
    	Map param = new HashMap();    	
    	param.put("id",id);
    	param.put("date",date);
    	param.put("nama",nama);
    	param.put("jumlh",jumlh); 
    	param.put("noBlanko",noBlanko); 
    	param.put("lus_id",lus_id); 
    	param.put("jenis",jenis);
    	
    	insert("insertFormBandara", param);
    }
    
    public void updateFormBandara(String id,String date,String nama,String jumlh,String noBlanko,String lus_id) throws DataAccessException {
    	Map param = new HashMap();    	
    	param.put("id",id);
    	param.put("date",date);
    	param.put("nama",nama);
    	param.put("jumlh",jumlh); 
    	param.put("noBlanko",noBlanko); 
    	param.put("lus_id",lus_id); 
    	
    	update("updateFormBandara", param);
    }
    
    public void updateStockCabang(Spaj spajCabang) throws DataAccessException {
    	update("updateStokSpaj", spajCabang);
    }
    
    public void updateSpajDet(SpajDet spajDet) throws DataAccessException {
    	update("updateSpajDetV2", spajDet);
    }
    
    public List<Map> selectPermintaanSpajBlomAccept() throws DataAccessException {
    	return query("selectPermintaanSpajBlomAccept", null);
    }
    
    public List<Map> selectPermintaanSpajBlomPertgjwbn() throws DataAccessException {
    	return query("selectPermintaanSpajBlomPertgjwbn", null);
    }
    
    public List<DropDown> getLstJnsAlasan() throws DataAccessException {
    	return query("getLstJnsAlasan", null);
    }
    
    public List<Surat_history> selectSuratHistByDate(String begDate, String endDate, Integer allAccess, String lus_id, Integer jenis, String polis, String searchbypolis) throws DataAccessException {
    	Map param = new HashMap();    	
    	param.put("begDate",begDate);
    	param.put("endDate",endDate);
    	param.put("allAccess",allAccess);
    	param.put("lus_id",lus_id);
    	param.put("jenis", jenis);
    	param.put("polis", polis);
    	param.put("bypolis", searchbypolis);
    	
    	return query("selectSuratHistByDate",param);
    }
    
    public void updateSuratHist(Surat_history sh) {
    	update("updateSuratHist", sh);
    }
    
    public List<DropDown> selectMiaMsagId() throws DataAccessException {
    	return query("selectMiaMsagId",null);
    }
    
    public List<DropDown> selectlsRegion(String lca_id) throws DataAccessException {
    	return query("selectlsRegion", lca_id);
    }
    
    public List<DropDown> selectlsBank() throws DataAccessException {
    	return query("selectlsBank",null);
    }
    
    public String isAgentExist(String name, String birth_date) throws DataAccessException {
    	Map param = new HashMap();    	
    	param.put("name",name);
    	param.put("birth_date",birth_date);
    	
    	List<String> result = query("isAgentExist", param);
    	StringBuffer sb = new StringBuffer();
    	if(result.size() > 0) {
    		sb.append(result.get(0));
    		
    		for (int i=1;i<result.size();i++) {
    			sb.append(",");
    			sb.append(result.get(i));
    		}
    	}
    	
    	return sb.toString();   	
    }
    
    public Integer selectCountMstRecuiter(String ls_recruiter) throws DataAccessException {
    	return (Integer) querySingle("selectCountMstRecuiter", ls_recruiter);
    }
    
    public void updateMstDetRecruiter(String ls_recruiter, String ls_recruit) throws DataAccessException {
    	Map param = new HashMap();    	
    	param.put("ls_recruiter",ls_recruiter);
    	param.put("ls_recruit",ls_recruit);   
    	
    	update("updateMstDetRecruiter", param);
    }
    
    public Integer getAgenBlackList(String name, String birth_date) throws DataAccessException {
    	Map param = new HashMap();    	
    	param.put("name",name);
    	param.put("birth_date",birth_date);
    	
    	return (Integer) querySingle("getAgenBlackList", param);
    }
    
    public Integer wf_cek_rekruter(String as_agent) throws DataAccessException {
    	return (Integer) querySingle("wf_cek_rekruter", as_agent);
    }
    
    public Integer getLevelRecruiter(String ls_agent, Integer ls_lca) throws DataAccessException {
    	Map param = new HashMap();    	
    	param.put("ls_agent",ls_agent);
    	param.put("ls_lca",ls_lca);
    	
    	return (Integer) querySingle("getLevelRecruiter", param);   	
    }
    
    public void insertMstInputAgensys(Mia mia) throws DataAccessException {
    	insert("insertMstInputAgensys", mia);
    }
    
    public void updateMasterInputAgen(Mia mia) throws DataAccessException {
    	update("updateMasterInputAgen", mia);
    }
    
    public void deleteMasterInputAgen(String agensys_id) throws DataAccessException {
    	delete("deleteMasterInputAgen", agensys_id);
    }
    
    public Mia getMstInputAgensys(String agensys_id) throws DataAccessException {
    	return (Mia) querySingle("getMstInputAgensys", agensys_id);
    }
    
    public List getMisFromName(String nama) throws DataAccessException {
    	return query("getMisFromName", nama);
    }
    
    public List getMisFromSpaj(String spaj) throws DataAccessException {
    	return query("getMisFromSpaj", spaj);
    }
    
	/* Yusuf (19/08/2011) - Start of Followup Billing Premi Lanjutan */    
    public List<Followup> selectFollowupBilling(String jenis, Integer aging, String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String jn, String spaj) throws DataAccessException{
    	String lca_id = "";
    	
    	if(jenis.equals("gagaldebet_efc")){
    		jenis = "gagaldebet";
    		lca_id = "65";
    	}
    	
    	Map m = new HashMap();
    	m.put("jenis", jenis); //gagaldebet, jatuhtempo, all
    	m.put("aging", aging);
    	m.put("begdate", begdate);
    	m.put("enddate", enddate);
    	m.put("lus_id", lus_id);
    	m.put("stfu", stfu);
    	m.put("cabang", cabang);
    	m.put("admin", admin);
    	m.put("jn", jn);
    	m.put("lca_id", lca_id);
    	m.put("spaj", spaj);
    	
    	if(jenis.equalsIgnoreCase("visa_camp")) return query("selectFollowupBillingVC", m);
    	else return query("selectFollowupBilling", m);
    }
    /*
    public List<Followup> selectFollowupBilling(int aging, String lus_id, String stfu) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("aging", aging);
    	m.put("lus_id", lus_id);
    	m.put("stfu", stfu);
    	return query("selectFollowupBilling", m);
    }
    
    public List<Followup> selectFollowupBilling(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("begdate", begdate);
    	m.put("enddate", enddate);
    	m.put("lus_id", lus_id);
    	m.put("stfu", stfu);
    	m.put("cabang", cabang);
    	m.put("admin", admin);
    	return query("selectFollowupBilling", m);
    }
    */
    
    public List<Followup> selectDetailPolisFollowupBilling(String tahun_ke, String premi_ke, String reg_spaj) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("tahun_ke", tahun_ke);
    	m.put("premi_ke", premi_ke);
    	m.put("reg_spaj", reg_spaj);
    	return query("selectDetailPolisFollowupBilling", m);
    }
    
    public List<Followup> selectDetailPaymentFollowupBilling(String reg_spaj) throws DataAccessException{
    	return query("selectDetailPaymentFollowupBilling", reg_spaj);
    }
    
    public List<Followup> selectHistoryFollowupBilling(String tahun_ke, String premi_ke, String reg_spaj) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("tahun_ke", tahun_ke);
    	m.put("premi_ke", premi_ke);
    	m.put("reg_spaj", reg_spaj);
    	return query("selectHistoryFollowupBilling", m);
    }
    
    public List<String> selectEmailAdminCabangFollowupBilling(String cabang, String admin) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("cabang", cabang);
    	m.put("admin", admin);
    	return query("selectEmailAdminCabangFollowupBilling", m);
    }

    public List<Followup> selectReportFollowupBilling(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String kat, String jn_tgl) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("begdate", begdate);
    	m.put("enddate", enddate);
    	m.put("lus_id", lus_id);
    	m.put("stfu", stfu);
    	m.put("cabang", cabang);
    	m.put("admin", admin);
    	m.put("kat", kat);
    	m.put("jn_tgl", jn_tgl);
    	return query("selectReportFollowupBilling", m);
    }
    
    public List<Followup> selectReportFollowupBillingPerUser(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String kat, String rep, String jn_tgl) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("begdate", begdate);
    	m.put("enddate", enddate);
    	m.put("lus_id", lus_id);
    	m.put("stfu", stfu);
    	m.put("cabang", cabang);
    	m.put("admin", admin);
    	m.put("kat", kat);
    	m.put("rep", rep);
    	m.put("jn_tgl", jn_tgl);
    	return query("selectReportFollowupBillingPerUser", m);
    }
    
    public String selectAttachmentFollowupBilling(String tahun_ke, String premi_ke, String reg_spaj) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("msbi_tahun_ke", tahun_ke);
    	m.put("msbi_premi_ke", premi_ke);
    	m.put("reg_spaj", reg_spaj);
    	return (String) querySingle("selectAttachmentFollowupBilling", m);
    }

    public boolean insertFollowupBilling(Followup fu, User currentUser, MultipartFile file, boolean kirimEmail, String emailto) throws DataAccessException{
    	boolean sukses = true;
    	List<File> attachments = null;
    	
    	//simpan filenya ke server (bila ada)
		try {
			if(file != null){
				if(file.isEmpty()==false){
					//create folder
			    	String dir = props.getProperty("pdf.dir.followupbilling") + "\\" + fu.reg_spaj.trim() + "\\" + fu.msbi_tahun_ke + "\\" + fu.msbi_premi_ke;
					//String dir = "D:\\test\\" + fu.reg_spaj.trim() + "\\" + fu.msbi_tahun_ke + "\\" + fu.msbi_premi_ke;
					File destDir = new File(dir);
					if(!destDir.exists()) destDir.mkdirs();
					//save file
					String dest = destDir.getPath() +"/"+ file.getOriginalFilename();
					File outputFile = new File(dest);
				    FileCopyUtils.copy(file.getBytes(), outputFile);
				    
				    attachments = new ArrayList<File>();
				    attachments.add(outputFile);
				    
				    //tambahkan path lengkap attachmentnya
				    fu.msfu_attachment = outputFile.getAbsolutePath();
				}
			}
		} catch (IOException e) {
			logger.error("ERROR :", e);
			sukses = false;
		}
    	
    	//email beserta attachment ke audit dept cc usernya
		if(kirimEmail){
	    	try {
	    		String from = props.getProperty("admin.ajsjava");
	    		if(!emailto.trim().equals("")){
		    		String[] to = emailto.split(";");
		    		String[] cc = null;
		    		
		    		//untuk kategori Menitipkan pembayaran premi ke agen dan Tidak bisa dihubungi
		    		//kirim ke legal dan cc ke audit
		    		//07/10/2013 canpri
		    		if(fu.getLsfu_id()==6 || fu.getLsfu_id()==7){
		    			if(currentUser.getEmail() != null){
			    			if(!currentUser.getEmail().trim().equals("")){
//			    				cc = new String[]{currentUser.getEmail(),"Audit@sinarmasmsiglife.co.id"};
			    				cc = new String[]{currentUser.getEmail(),"compliance@sinarmasmsiglife.co.id","yulikusuma@sinarmasmsiglife.co.id"};
			    			}else{
//			    				cc = new String[]{"Audit@sinarmasmsiglife.co.id"};
			    				cc = new String[]{"compliance@sinarmasmsiglife.co.id","yulikusuma@sinarmasmsiglife.co.id"};
			    			}
			    		}else{
//			    			cc = new String[]{"Audit@sinarmasmsiglife.co.id"};
			    			cc = new String[]{"compliance@sinarmasmsiglife.co.id","yulikusuma@sinarmasmsiglife.co.id"};
			    		}
		    		}else if(fu.getLsfu_id()==32){
		    			if(currentUser.getEmail() != null){
			    			if(!currentUser.getEmail().trim().equals("")){
//			    				cc = new String[]{currentUser.getEmail(),"yulin@sinarmasmsiglife.co.id"};
			    				cc = new String[]{currentUser.getEmail()};
			    			}else{
//			    				cc = new String[]{"yulin@sinarmasmsiglife.co.id"};
			    			}
			    		}else{
//			    			cc = new String[]{"yulin@sinarmasmsiglife.co.id"};
			    		}
		    		}else{
		    			if(currentUser.getEmail() != null){
			    			if(!currentUser.getEmail().trim().equals("")){
//			    				cc = new String[]{currentUser.getEmail()};
			    				cc = new String[]{currentUser.getEmail(),
			    						"compliance@sinarmasmsiglife.co.id","yulikusuma@sinarmasmsiglife.co.id"};
			    			}
			    		}
		    		}
		    		
		    		// disable this, for demo/testing only
//		    		to = new String[]{"edi@sinarmasmsiglife.co.id"};
		    		if(selectCountJnFollowUp(fu.reg_spaj,7)>=3){//(21 Feb 2012) - Hasil meeting, apabila tidak bisa dihubungi 3x, maka akan email ke Pak Edy
		    			cc = new String[]{"edi@sinarmasmsiglife.co.id"};
		    		}
		    		//Testing Email Only
//		    		to = new String[]{"deddy@sinarmasmsiglife.co.id"};
//		    		cc = new String[]{"deddy@sinarmasmsiglife.co.id"};
		    		// disable this, for demo/testing only
		    		Followup hasilFollowUp = selectLstFollowUp(fu.lsfu_id); 
		    		
		    		StringBuffer pesan = new StringBuffer();
					pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: yellow;} table td{border: 1px solid black;}</style></head>");
					pesan.append("<body>Harap untuk menindaklanjuti follow up billing premi lanjutan polis dibawah ini:<br><br>");
					pesan.append("<table cellspacing='0'><thead><tr><th>Polis</th><th>Pemegang</th><th>Tanggal Follow Up</th><th>Kategori Follow Up</th><th>Hasil Follow Up</th><th>Status Case</th>"+(fu.getReminder_date()!=null?"<th>Tanggal Reminder Date</th>":"")+"</tr></thead><tbody>");
					pesan.append(
							"<tr><td>" + fu.mspo_policy_no_format + "</td>" +
							"<td>" + fu.pemegang + "</td>" +
							"<td>" + fu.tgl_proses + "</td>" +
							"<td>" + hasilFollowUp.getLsfu_desc() + "</td>" +
							"<td>" + fu.msfu_ket + "</td>" +
							"<td>" + (fu.msfu_status == 0 ? "Follow Up" : "Closed") + "</td>" +
							(fu.getReminder_date()!=null?"<td>"+ fu.reminder_date+"</td>":"") );
					pesan.append("</tbody></table>");
					pesan.append("<br>Pengirim: " + currentUser.getName() + "<br>" + "NB: Email ini dikirim secara otomatis oleh sistem E-Lions. Harap tidak mereply email ini.</body></html>");		    		
		    		
//					this.email.send(true, from, to, cc, null, "[E-Lions] Followup Billing untuk Polis " + fu.mspo_policy_no_format, pesan.toString(), attachments);
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							from, to, cc, null, 
							"Followup Billing untuk Polis " + fu.mspo_policy_no_format, pesan.toString(), attachments, fu.getReg_spaj());
	    		}
				
			} catch (MailException e) {
				logger.error("ERROR :", e);
				sukses = false;
			}
		}
    	
    	//insert followup paling terakhir, bila gak error
		if(sukses) {
			insert("insertFollowupBilling", fu);
			uwDao.insertCsfCall(fu.reg_spaj, "O", currentUser.getLus_id(), null, null, fu.getMsfu_ket(), null, null, null, 1,2);
			// enable this
//			insert("insertFollowupCSF", fu);
		}
    	
		return sukses;
    }

	public boolean insertKontrolFollowupBilling(User currentUser, String cabang, String admin, String[] polis, String[] pp, String[] spaj, int[] thnke, int[] premike, String[] ket) throws DataAccessException{
		boolean sukses = true;
		List<Followup> daftar = new ArrayList<Followup>();
		StringBuffer pesan = new StringBuffer();

		if(spaj.length > 0){
			pesan.append("<html><head><style>body{font-family: Tahoma, Arial, Helvetica, sans-serif;}table{border: 1px solid black;}table th{border: 1px solid black; background-color: yellow;} table td{border: 1px solid black;}</style></head>");
			pesan.append("<body>Harap untuk menindaklanjuti follow up billing premi lanjutan polis dibawah ini:<br><br>");
			pesan.append("<table cellspacing='0'><thead><tr><th>Polis</th><th>Pemegang</th><th>Spaj</th><th>Tahun Ke</th><th>Premi Ke</th><th>Keterangan</th></tr></thead><tbody>");
			for(int i=0; i<spaj.length; i++){
				daftar.add(new Followup(spaj[i], polis[i], thnke[i], premike[i], 99, currentUser.getLus_id(), ket[i], 0));
				pesan.append(
						"<tr><td>" + polis[i] + "</td>" +
						"<td>" + pp[i] + "</td>" +
						"<td>" + spaj[i] + "</td>" +
						"<td>" + thnke[i] + "</td>" +
						"<td>" + premike[i] + "</td>" +
						"<td>" + ket[i] + "</td></tr>");
			}
			pesan.append("</tbody></table>");
			pesan.append("<br>Pengirim: " + currentUser.getName() + "<br>" + "NB: Email ini dikirim secara otomatis oleh sistem E-Lions. Harap tidak mereply email ini.</body></html>");
			
	    	try {
	    		List<String> daftarEmail = selectEmailAdminCabangFollowupBilling(cabang, admin);
	    		
	    		String from = props.getProperty("admin.ajsjava");
	    		String[] to = StringUtils.toStringArray(daftarEmail);
	    		
	    		// disable this, for demo/testing only
//	    		to = new String[]{"edi@sinarmasmsiglife.co.id"};
	    		// disable this, for demo/testing only
	    		
//				this.email.send(true, from, to, null, null, "[E-Lions] Kontrol Followup Billing", pesan.toString(), null);
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
						from, to, null, null, 
						"[E-Lions] Kontrol Followup Billing", pesan.toString(), null, null);
				
			} catch (MailException e) {
				logger.error("ERROR :", e);
				sukses = false;
			}
			
	    	//insert followup paling terakhir, bila gak error
			if(sukses) {
				for(Followup fu : daftar){
					insert("insertFollowupBilling", fu);
					// enable this
					//insert("insertFollowupCSF", fu);
				}
			}
		}else{
			sukses = false;
		}
		
		return sukses;
	}
    
	/* Yusuf (19/08/2011) - End of Followup Billing Premi Lanjutan */

	public List<Followup> selectReportJTPowersave(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id, String kategori) throws DataAccessException{
		Map m = new HashMap();
		m.put("lca", lca);
		m.put("lwk", lwk);
		m.put("lsrg", lsrg);
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("kategori", kategori);
		return query("selectReportJTPowersave", m);
	}
	
	public List<Followup> selectreportPrintPolisCabang(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("lca", lca);
		m.put("lwk", lwk);
		m.put("lsrg", lsrg);
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		return query("selectreportPrintPolisCabang", m);
	}
	
	public List<Followup> selectreportSimascardCabang(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("lca", lca);
		m.put("lwk", lwk);
		m.put("lsrg", lsrg);
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		return query("selectreportSimascardCabang", m);
	}
	
	public List<Followup> selectreportSummaryInput(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("lca", lca);
		m.put("lwk", lwk);
		m.put("lsrg", lsrg);
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		return query("selectreportSummaryInput", m);
	}
	
	public List selectreportSummaryInputUserBas() throws DataAccessException{
		return query("selectreportSummaryInputUserBas",null);
	}
	
	public List selectReportUserDailyAksep() throws DataAccessException{
		return query("selectReportUserDailyAksep",null);
	}
	
	public List selectReportDailyAksep(String bdate, String edate, String lus_id) {
		Map m=new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		return query("selectReportDailyAksep", m);
	}
	
	public List selectReportFollowUpBillingCol(String bdate, String edate, String prod, String tipe, String status) {
		Map m=new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("prod", prod);
		m.put("tipe", tipe);
		m.put("status", status);
		return query("selectReportFollowUpBillingCol", m);
	}
	
	public List selectReportHasilFollowUpBilling(String bdate, String edate, String prod, String status) {
		Map m=new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("prod", prod);
		m.put("status", status);
		return query("selectReportHasilFollowUpBilling", m);
	}
	
	public List selectReportFollowUpBillingSum(String bdate, String edate, String prod, String tipe, String status) {
		Map m=new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("prod", prod);
		m.put("tipe", tipe);
		m.put("status", status);
		return query("selectReportFollowUpBillingSum", m);
	}
	
	public List selectLsbsSsu() throws DataAccessException{
		return query("selectLsbsSsu",null);
	}
	
	public List selectLsbsSrc() throws DataAccessException{
		return query("selectLsbsSrc",null);
	}
	
	public List selectLsdbsSsu(Integer lsbsId) {
		return query("selectLsdbsSsu",lsbsId);
	}
	
	public List selectLsdbsSrc(Integer lsbsId) {
		return query("selectLsdbsSrc",lsbsId);
	}
	
	public List selectLsdbsSrc2(String nama) {
		return query("selectLsdbsSrc2",nama);
	}
	
	public String selectNamaProduk(String lsbs, String lsdbs) {
		Map m = new HashMap();
		m.put("lsbs", lsbs);
		m.put("lsdbs", lsdbs);
		return (String) querySingle("selectNamaProduk",m);
	}
	
	public List selectHistoryUploadSsu() throws DataAccessException{
		return query("selectHistoryUploadSsu",null);
	}
	
	public List<Followup> selectreportSummaryInputUser(  String idbas2,String bdate, String edate, String prods, String preview) throws DataAccessException{
		Map m = new HashMap();
		m.put("lus_id", idbas2);
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("prods", prods);
		m.put("preview", preview);
		return query("selectreportSummaryInputUser", m);
	}
	
	public List<Followup> selectreportSummaryInputTotal(  String lus_id,String bdate, String edate) throws DataAccessException{
		Map m = new HashMap();
		m.put("lus_id", lus_id);
		m.put("bdate", bdate);
		m.put("edate", edate);		
		return query("selectreportSummaryInputTotal", m);
	}
	
	public List<Followup> selectReportSummaryInputRegisterAgen(String cabang, String userinput, String bdate, String edate, String userBas, String jn_report) throws DataAccessException{
		Map m = new HashMap();
		m.put("cabang", cabang);
		m.put("userinput", userinput);
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("userBas", userBas);
		m.put("jn_report", jn_report);
		return query("selectReportSummaryInputRegisterAgen", m);
	}
	
	public Map selectHitungBungaPinjaman(String spaj) throws DataAccessException{
		return (Map) querySingle("selectHitungBungaPinjaman", spaj);
	}
	
	public Map selectMstPinjaman(String reg_spaj) throws DataAccessException{
		return (Map) querySingle("selectMstPinjaman", reg_spaj);
	}
	
	public List selectReportSummaryAgentContract(String bdate, String edate, String userBas, String jn_report) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("userBas", userBas);
		m.put("jn_report", jn_report);
		return query("selectReportSummaryAgentContract", m);
	}
	
	public void saveAgentContractHist(User currentUser, Map<String, Object> content) throws DataAccessException{
//		logger.info("-------------------------------------------------------------");
//		for(String s : content.keySet()){
//			logger.info(s + " = " + content.get(s));
//		}
//		logger.info("-------------------------------------------------------------");
		
		//set counter
		Date now = commonDao.selectSysdate();
		DateFormat df = new SimpleDateFormat("yyyyMM");
		String max = (String) querySingle("selectMaxNoAgentContract", null);
		String bulan = df.format(now);
		String id = null;
		int counter = 1;
		
		if(max != null) {
			String bulanCounter = max.substring(0, 6);
			if(bulanCounter.equals(bulan)){
				counter = Integer.parseInt(max.substring(6));
				counter++;
			}
		}
		id = bulan + FormatString.rpad("0", String.valueOf(counter), 5);
		
		content.put("MAC_ID", id);
		content.put("lus_id", currentUser.getLus_id());
		insert("insertAgentContract", content);
	}
	
	public Map selectAgentContract(String mac_id) throws DataAccessException{
		return (Map) querySingle("selectAgentContract", mac_id);
		
	}

	public List selectReportDemografiBSM(Map<String, Object> m, Integer jn_bank) {
		Map<String, StringBuffer> p = new HashMap<String, StringBuffer>();
		p.put("umur", new StringBuffer());
		p.put("sex", new StringBuffer());
		p.put("marital", new StringBuffer());
		p.put("agama", new StringBuffer());
		p.put("penghasilan", new StringBuffer());
		p.put("wilayah", new StringBuffer());
		p.put("outstanding", new StringBuffer());
		p.put("mgi", new StringBuffer());
		p.put("product", new StringBuffer());
		p.put("kurs", new StringBuffer());

		for (Map.Entry<String, Object> entry : m.entrySet()){
			String key = entry.getKey();
			String value =  ((String[]) entry.getValue())[0];
			for (Map.Entry<String, StringBuffer> e : p.entrySet()){
				if(key.startsWith(e.getKey())){
					if("umur".equals(e.getKey())){
						StringBuffer tmp = p.get(e.getKey());
						if(!tmp.toString().equals("")) p.get(e.getKey()).append(" or ");
						p.get(e.getKey()).append("(a.mspo_age between " + value + ")");
					}else if("sex".equals(e.getKey())){
						StringBuffer tmp = p.get(e.getKey());
						if(!tmp.toString().equals("")) p.get(e.getKey()).append(" or ");
						p.get(e.getKey()).append("(f.mspe_sex = " + value + ")");
					}else if("marital".equals(e.getKey())){
						StringBuffer tmp = p.get(e.getKey());
						if(!tmp.toString().equals("")) p.get(e.getKey()).append(" or ");
						p.get(e.getKey()).append("(f.mspe_sts_mrt = " + value + ")");
					}else if("agama".equals(e.getKey())){
						StringBuffer tmp = p.get(e.getKey());
						if(!tmp.toString().equals("")) p.get(e.getKey()).append(" or ");
						p.get(e.getKey()).append("(f.lsag_id = " + value + ")");
					}else if("penghasilan".equals(e.getKey())){
						StringBuffer tmp = p.get(e.getKey());
						if(!tmp.toString().equals("")) p.get(e.getKey()).append(" or ");
						p.get(e.getKey()).append("(f.mkl_penghasilan = '" + value + "')");
					}else if("wilayah".equals(e.getKey())){
						StringBuffer tmp = p.get(e.getKey());
						if(!tmp.toString().equals("")) p.get(e.getKey()).append(" or ");
						p.get(e.getKey()).append("(c.wil_no = rpad('" + value + "', 4, ' '))");
					}else if("outstanding".equals(e.getKey())){
						StringBuffer tmp = p.get(e.getKey());
						if(!tmp.toString().equals("")) p.get(e.getKey()).append(" or ");
						p.get(e.getKey()).append("(sum(i.premi) between " + value + ")");
					}else if("mgi".equals(e.getKey())){
						StringBuffer tmp = p.get(e.getKey());
						if(!tmp.toString().equals("")) p.get(e.getKey()).append(" or ");
						p.get(e.getKey()).append("(i.mgi = " + value + ")");
					}else if("product".equals(e.getKey())){
						StringBuffer tmp = p.get(e.getKey());
						if(!tmp.toString().equals("")) p.get(e.getKey()).append(" or ");
						p.get(e.getKey()).append("((h.lsbs_id || '-' || h.lsdbs_number) = '" + value + "')");
					}else if("kurs".equals(e.getKey())){
						StringBuffer tmp = p.get(e.getKey());
						if(!tmp.toString().equals("")) p.get(e.getKey()).append(" or ");
						p.get(e.getKey()).append("(a.lku_id = '" + value + "')");
					}
				}
			}
		}
		
		//khusus untuk OUTSTANDING, karena merupakan SUM(), maka tidak bisa dikondisi WHERE, melainkan di kondisi HAVING
		StringBuffer where = new StringBuffer();
		StringBuffer having = new StringBuffer();
		for (Map.Entry<String, StringBuffer> e : p.entrySet()){
			if(!"".equals(e.getValue().toString())){
				if(!e.getKey().equals("outstanding")){
					where.append(" AND (" + e.getValue().toString() + ") ");
				}else{
					having.append(" HAVING (" + e.getValue().toString() + ") ");
				}
			}
		}
		
		Map params = new HashMap();
		params.put("where", where.toString());
		params.put("having", having.toString());
		params.put("jn_bank", jn_bank);
		
		return query("selectReportDemografiBSM", params);
	}

	public List selectReportSummaryJapaneseDesk(String bdate, String edate) {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		return query("selectReportSummaryJapaneseDesk", m);
	}

	public List <Map> selectCabangBridge() throws DataAccessException{
		return query("select_cabangbridge",null);
	}
	
	public List getNamaBridge(String nama) throws DataAccessException {
    	return query("getNamaBridge", nama);
    }
    
    public List getKodeBridge(String kode) throws DataAccessException {
    	return query("getKodeBridge", kode);
    }
	
	//SHOW DATA
    public List<Hadiah> selectDataPolisUntukInputHadiah(String spajPolis) throws DataAccessException{
		return query("selectDataPolisUntukInputHadiah", spajPolis);
	}
	
    public List<Hadiah> selectDataPolisUntukInputHadiah2(String spajPolis) throws DataAccessException{
    	return query("selectDataPolisUntukInputHadiah2", spajPolis);
	}
    
    public List<Hadiah> selectDataPolisUntukInputHadiah3(String spajPolis) throws DataAccessException{
    	return query("selectDataPolisUntukInputHadiah3", spajPolis);
	}
    
    //CEK
    public List<Hadiah> selectHadiahCek(String spajPolis) throws DataAccessException{
    	return query("selectHadiahCek", spajPolis);
	}
    
	public List<Map> selectMasterSupplier(String v) throws DataAccessException{
		return query("selectMasterSupplier", v);
	}

	public String saveHadiah(Hadiah hadiah, User currentUser) throws DataAccessException{
		Date sysdate = commonDao.selectSysdate();
		String pesan = null;

		int update = update("updateHadiah", hadiah);
		
		if(update > 0){ //bila update data sebelumnya
			hadiah.keterangan = "Update Data Hadiah";
			hadiah.create_id = Integer.valueOf(currentUser.getLus_id());
			hadiah.create_dt = sysdate;
			insert("insertHadiahHist", hadiah);
			pesan = "Data berhasil diupdate";
			
		}else{ //bila baru pertama kali input
			hadiah.mh_no = 1;
			hadiah.create_id = Integer.valueOf(currentUser.getLus_id());
			if(hadiah.program_hadiah==null)hadiah.program_hadiah = 0;
			if(hadiah.program_hadiah==1){
				hadiah.lspd_id = 91; 
				hadiah.keterangan = "Input Data Hadiah dari UW";
				hadiah.mh_tgl_input = sysdate;
				hadiah.create_dt = sysdate;
				hadiah.mh_tgl_aksep = sysdate;
				hadiah.mh_tgl_kirim_uw = sysdate;
				insert("insertHadiah", hadiah);
				
				hadiah.lspd_id = 84;
				hadiah.keterangan = "Input Data Hadiah dari UW";
				insert("insertHadiahHist", hadiah);
				
				hadiah.lspd_id = 91;
				hadiah.keterangan = "Transfer Ke Purchasing";
				insert("insertHadiahHist", hadiah);
			}else{
				hadiah.lspd_id = 84; //INPUT IDENTIFIKASI BARANG
				hadiah.keterangan = "Input Data Hadiah"; //keterangan MST_HADIAH_HIST
				hadiah.mh_tgl_input = sysdate;
				hadiah.create_dt = sysdate;
				insert("insertHadiah", hadiah);
				insert("insertHadiahHist", hadiah);
			}
			pesan = "Data berhasil disimpan";
		}
		return pesan;
	}
	
	protected Connection getConnection() {
		Connection conn = null;
		try {
			conn = this.getDataSource().getConnection();
		} catch (SQLException e) {
			logger.error("ERROR :", e);
		}
		return conn;
	}
	
	//Transfer Doc Input Hadiah ke 85
	public String transferDocInputHadiah(Hadiah hadiah, User currentUser) throws DataAccessException{
		Date sysdate = commonDao.selectSysdate();
		String pesan = null;
		
		hadiah.keterangan = "Transfer dokumen ke 85";
		hadiah.create_id = Integer.valueOf(currentUser.getLus_id());
		hadiah.create_dt = sysdate;
		/*update("updatedokumenHadiahInput", hadiah);
		insert("insertHadiahHist", hadiah);
		
		return pesan;*/
		
		try {
			//send email
			String to = "";
			String cc = "";
			
			//attachment
			List<File> attachments = new ArrayList<File>();
			
			to = props.getProperty("hadiah.purchasing.memo1.to");
			cc = props.getProperty("hadiah.purchasing.memo1.cc");
			
			
			//to dan cc nya @sinarmasmsiglife.co.id
			String[] emailTo = to.split(";");
			String[] emailCc = cc.split(";");
			
			for(int y=0; y<emailTo.length; y++){
				emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
			}
			for(int y=0; y<emailCc.length; y++){
				emailCc[y] = emailCc[y].concat("@sinarmasmsiglife.co.id");
			}
			
			String file = props.getProperty("pdf.dir.hadiah.memo")+hadiah.reg_spaj+"\\Memo1_" + hadiah.reg_spaj +".pdf";
			File destDir = new File(file);
			
			if(!destDir.exists()){
				
				pesan ="Mohon print memo 1 dahulu";
				
			}else{

				hadiah.lspd_id = 85;
				
				File sourceFile = new File(file);
				attachments.add(sourceFile);
				
				int upt = update("updatedokumenHadiahInput", hadiah);
				
				if(upt>0){
					
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							props.getProperty("admin.ajsjava"), emailTo, emailCc, null, 
							"Memo 1 "+hadiah.reg_spaj, "Permohonan hadiah" +
									"<br>Tanggal : "+sysdate+
									"<br>No SPAJ : "+hadiah.reg_spaj+
									"<br>Nama : "+hadiah.pemegang+
									"<br>Hadiah : "+hadiah.lh_nama, attachments,hadiah.reg_spaj);
					
					insert("insertHadiahHist", hadiah);
				
					pesan = "Transfer dokumen berhasil";
				}else{
					pesan = "Transfer dokumen gagal";
				}
				
			}
			
		} catch (Exception e) {
			logger.error("ERROR :", e);
			pesan = "ERROR";
		}
		
		return pesan;
	}
	
	public Integer selectPositionHadiah(String spaj) {
		return (Integer) querySingle("selectPositionHadiah", spaj);
	}
	
	//Cancel hadiah
	public String cancelHadiah(Hadiah hadiah, User currentUser) throws DataAccessException{
		Date sysdate = commonDao.selectSysdate();
		String pesan = null;
		
		hadiah.keterangan = "Cancel";
		hadiah.create_id = Integer.valueOf(currentUser.getLus_id());
		hadiah.create_dt = sysdate;
		
		try{
			hadiah.lspd_id = 95;
			
			String to = "";
			String cc = "";
			
			to = props.getProperty("hadiah.purchasing.to");
			cc = props.getProperty("hadiah.purchasing.cc");
			
			
			//to dan cc nya @sinarmasmsiglife.co.id
			String[] emailTo = to.split(";");
			String[] emailCc = cc.split(";");
			
			for(int y=0; y<emailTo.length; y++){
				emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
			}
			for(int y=0; y<emailCc.length; y++){
				emailCc[y] = emailCc[y].concat("@sinarmasmsiglife.co.id");
			}
			
			int upt = update("updatecancelHadiah", hadiah);
			
			if(upt>0){
				
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
						props.getProperty("admin.ajsjava"), emailTo, emailCc, null, 
						"Cancel hadiah "+hadiah.reg_spaj, "Cancel hadiah" +
								"<br>Tanggal : "+sysdate+
								"<br>No SPAJ : "+hadiah.reg_spaj+
								"<br>Nama : "+hadiah.pemegang+
								"<br>Hadiah : "+hadiah.lh_nama, null, hadiah.reg_spaj);
				
				insert("insertHadiahHist", hadiah);
				
				pesan = "Hadiah telah di Cancel";
			}else{
				pesan = "No SPAJ tidak ada/belum terdaftar";
			}
		}catch(Exception e){
			logger.error("ERROR :", e);
			pesan = "ERROR";
		}
		
		return pesan;
	}
	
	//Print memo 1 hadiah
	public String printMemoHadiah(Hadiah hadiah, User currentUser) throws DataAccessException{
		Date sysdate = commonDao.selectSysdate();
		String pesan = null;

		hadiah.create_id = Integer.valueOf(currentUser.getLus_id());
		hadiah.create_dt = sysdate;
		
		logger.info(hadiah.reg_spaj);
		Connection conn = null;
		try{
			conn = this.getDataSource().getConnection();
			String to = "";
			String cc = "";
			
			List<File> attachments = new ArrayList<File>();
			
			to = props.getProperty("hadiah.purchasing.memo1.to");
			cc = props.getProperty("hadiah.purchasing.memo1.cc");
			
			
			//to dan cc nya @sinarmasmsiglife.co.id
			String[] emailTo = to.split(";");
			String[] emailCc = cc.split(";");
			
			for(int y=0; y<emailTo.length; y++){
				emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
			}
			for(int y=0; y<emailCc.length; y++){
				emailCc[y] = emailCc[y].concat("@sinarmasmsiglife.co.id");
			}
			
			String file = props.getProperty("pdf.dir.hadiah.memo")+hadiah.reg_spaj+"\\Memo1_" + hadiah.reg_spaj +".pdf";
			File destDir = new File(file);
			
			if(!destDir.exists()){
				//Jasper Report
				String dir = props.getProperty("pdf.dir.hadiah.memo")+hadiah.reg_spaj+"\\";
				
				Map params = new HashMap();
				params.put("reg_spaj", hadiah.reg_spaj);
				
				JasperUtils.exportReportToPdf(
						props.getProperty("report.bas.hadiah_memo1")+".jasper", 
						dir, 
						"Memo1_" + hadiah.reg_spaj +".pdf", 
						params, 
						conn,
						PdfWriter.AllowPrinting, null, null);
				
				File sourceFile = new File(file);
				attachments.add(sourceFile);
				
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
						props.getProperty("admin.ajsjava"), emailTo, emailCc, null, 
						"Print Memo 1 "+hadiah.reg_spaj, "Memo 1" +
								"<br>Tanggal : "+sysdate+
								"<br>No SPAJ : "+hadiah.reg_spaj+
								"<br>Nama : "+hadiah.pemegang+
								"<br>Hadiah : "+hadiah.lh_nama, attachments, hadiah.reg_spaj);

				pesan = "Print Memo 1 dengan SPAJ "+hadiah.reg_spaj+" Sukses";
			}else{
				pesan = "Memo 1 dengan SPAJ "+hadiah.reg_spaj+" sudah ada";
			}
			
		}catch(Exception e){
			logger.error("ERROR :", e);
			pesan = "ERROR";
		}finally{
			closeConnection(conn);
		}
		
		return pesan;
	}

	public Hadiah selectLstHadiah(int lhc_id, int lh_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("lhc_id", lhc_id);
		m.put("lh_id", lh_id);
		return (Hadiah) querySingle("selectLstHadiah", m);
	}

	public List<Map> selectHadiahHist(String reg_spaj, Integer mh_no) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("mh_no", mh_no);
		return query("selectHadiahHist", m);
	}
	
	//show tanggal history
	public List<Map> selecttglHistory(String reg_spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		return query("selecttglHistory", m);
	}
	
	public Hadiah selectMstHadiah(String reg_spaj, Integer mh_no) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("mh_no", mh_no);
		return (Hadiah) querySingle("selectMstHadiah", m);
	} 
	
	public Hadiah selectMstHadiahRE(String reg_spaj, Integer mh_no) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("mh_no", mh_no);
		return (Hadiah) querySingle("selectMstHadiahRE", m);
	} 
	
    public Map selectDataAgenBridge(String msag_id) {
		return (Map) querySingle("selectDataAgenBridge", msag_id);
	}
    
    public void updateKodeBridge(String msag_id ,String lsrg_id){
		Map map = new HashMap();
		map.put("msag_id", msag_id);
		map.put("lsrg_id",lsrg_id);
		update("updateKodeBridge",map);
	}
    
    public List<Map> selectPosisiProsesHadiah(String begdate, String enddate, int lspd_id, String jenis, String program_hadiah) throws DataAccessException{
  	  Map m = new HashMap();
  	  m.put("lspd_id", lspd_id);
  	  m.put("begdate", begdate);
  	  m.put("enddate", enddate);
  	  m.put("jenis", jenis);
  	  m.put("program_hadiah", program_hadiah);
  	  return query("selectPosisiProsesHadiah",m);
  	}
  	
  	public List<Map> selectInfoMstHadiah(String spaj){
  		Map m = new HashMap();
  		m.put("reg_spaj", spaj);
  		return query("selectInfoMstHadiah",m);
  	}
  	
  	public List<Map> selectBlanko(String no_blanko){
  		Map m = new HashMap();
  		m.put("no_blanko", no_blanko);
  		return query("selectBlanko2",m);
  	}
  	
  	public Map selectMstSpajAoFurtherOrNonFurther(String no_blanko, String status){
  		Map m = new HashMap();
  		m.put("no_blanko", no_blanko);
  		m.put("status", status);
  		return (HashMap) querySingle("selectMstSpajAoFurtherOrNonFurther",m);
  	}
  	
  	public String updateProses(Hadiah hadiah, User currentUser) throws DataAccessException{
  		Date sysdate = commonDao.selectSysdate();
  		String pesan = null;
  		
  		//hadiah.keterangan = "Transfer Data Hadiah ke-" + hadiah.lspd_id;
  		hadiah.create_id = Integer.valueOf(currentUser.getLus_id());
  		hadiah.create_dt = sysdate;
  		
  		int update = update("updateProsesTransfer", hadiah);
  		if(update>0){
  			pesan = "Berhasil di update";
  			insert("insertHadiahHist", hadiah);
  		}else{
  			pesan = "gagal";
  		}
  		return pesan;
  	}

  	public String cancelProses(Hadiah hadiah, User currentUser) throws DataAccessException {
  		Date sysdate = commonDao.selectSysdate();
  		String pesan = null;
  		
  		hadiah.keterangan = "Cancel proses hadiah";
  		hadiah.create_id = Integer.valueOf(currentUser.getLus_id());
  		hadiah.create_dt = sysdate;
  		
  		int update = update("cancelProsesTransfer", hadiah);
  		if(update>0){
  			pesan = "Proses sudah di cancel";
  			cancelHadiah(hadiah, currentUser);
  			insert("insertHadiahHist",hadiah);
  		}else{
  			pesan = "gagal";
  		}
  		return pesan;
  	}
  	
  	public List<Map> viewDetailHadiah(String spaj, Integer mh, String program_hadiah) throws DataAccessException {
		Map m = new HashMap();
		m.put("reg_spaj", spaj);
		m.put("mh_no",mh);
		m.put("program_hadiah", program_hadiah);
		return query("viewDetailHadiah", m);
	}
  	
  	public String prosesHadiahMemo2(Hadiah hadiah, User currentUser) throws DataAccessException {
		Date sysdate = commonDao.selectSysdate();
		String pesan = null;

		hadiah.create_id = Integer.valueOf(currentUser.getLus_id());
		hadiah.create_dt = sysdate;
		
		BigDecimal a = BigDecimal.valueOf(hadiah.mh_harga);
		String spajF = FormatString.nomorSPAJ(hadiah.reg_spaj);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String tgl = df.format(sysdate);
		Connection conn = null;
		try{	
			conn = this.getDataSource().getConnection();
			String to = "";
			String cc = "";
			
			List<File> attachments = new ArrayList<File>();
			
			to = props.getProperty("hadiah.purchasing.memo2.to");
			cc = props.getProperty("hadiah.purchasing.memo2.cc");
			
			
			//to dan cc nya @sinarmasmsiglife.co.id
			String[] emailTo = to.split(";");
			String[] emailCc = cc.split(";");
			
			for(int y=0; y<emailTo.length; y++){
				emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
			}
			for(int y=0; y<emailCc.length; y++){
				emailCc[y] = emailCc[y].concat("@sinarmasmsiglife.co.id");
			}
			
			String dir = props.getProperty("pdf.dir.hadiah.memo")+"\\"+hadiah.reg_spaj+"\\";
			File destDir = new File(dir);
			
			if(!destDir.exists()) destDir.mkdirs();
			
			String file = props.getProperty("pdf.dir.hadiah.memo")+"\\"+hadiah.reg_spaj+"\\Memo2_" + hadiah.reg_spaj+".pdf";
			File ff = new File(file);
			
			if(!ff.exists()){
				
				Map params = new HashMap();
				String u = String.valueOf(currentUser.getLus_full_name());
				params.put("currentUser", u);
				params.put("reg_spaj", hadiah.reg_spaj);
				
				JasperUtils.exportReportToPdf(
						props.getProperty("report.bas.hadiah_memo2")+".jasper", 
						dir, 
						"Memo2_" + hadiah.reg_spaj +".pdf",
						params, 
						conn,
						PdfWriter.AllowPrinting, null, null);

				// Export Memo 3 to pdf
				
				params.put("currentUser", u);
				params.put("reg_spaj", hadiah.reg_spaj);
				params.put("mh_no", hadiah.mh_no);
				params.put("spajF", spajF);
				params.put("tgl_bayar",hadiah.mh_tgl_deadline_bayar);
				
				JasperUtils.exportReportToPdf(
						props.getProperty("report.bas.hadiah_memo3")+".jasper", 
						dir, 
						"Memo3_" + hadiah.reg_spaj +".pdf",
						params, 
						this.getDataSource().getConnection(),
						PdfWriter.AllowPrinting, null, null);
				
				// Send Email with attachment Memo 2
				
				File sourceFile = new File(file);
				attachments.add(sourceFile);
				
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
						props.getProperty("admin.ajsjava"), emailTo, emailCc, null, 
						"Internal Memorandum "+spajF, "Permohonan Pembelian Unit " +
								"<br>Tanggal : "+tgl+
								"<br>No SPAJ : "+spajF+
								"<br>Nama : "+hadiah.pemegang+
								"<br>Kategori : "+hadiah.lhc_nama+
								"<br>Hadiah : "+hadiah.lh_nama+
								"<br>Quantity : "+hadiah.mh_quantity+
								"<br>Harga : "+FormatNumber.convertToTwoDigit(a), attachments, spajF);

				pesan = "Print Memo Permohonan Pembelian Unit dengan SPAJ "+spajF+" Sukses";
			}else{
				
				// Re-send email of Memo 2
				 
				File sourceFile = new File(file);
				attachments.add(sourceFile);
				
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
						props.getProperty("admin.ajsjava"), emailTo, emailCc, null, 
						"Internal Memorandum "+spajF, "Permohonan Pembelian Unit " +
								"<br>Tanggal : "+tgl+
								"<br>No SPAJ : "+spajF+
								"<br>Nama : "+hadiah.pemegang+
								"<br>Kategori : "+hadiah.lhc_nama+
								"<br>Hadiah : "+hadiah.lh_nama+
								"<br>Quantity : "+hadiah.mh_quantity+
								"<br>Harga : "+FormatNumber.convertToTwoDigit(a), attachments, spajF);

				pesan = "Memo Permohonan Pembelian Unit dengan SPAJ "+spajF+" sudah di Email kembali";
			}			
		}catch(Exception e){
			logger.error("ERROR :", e);
			pesan = "ERROR";
		}finally{
			closeConnection(conn);
		}		
		return pesan;
	}

	public String prosesHadiahMemo3(Hadiah hadiah, User currentUser) {
		Date sysdate = commonDao.selectSysdate();
		String pesan = null;
		
		hadiah.keterangan = "Email memo 2 dan 3 dan transfer 86";
		hadiah.create_id = Integer.valueOf(currentUser.getLus_id());
		hadiah.create_dt = sysdate;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String tgl = df.format(sysdate);
		String spajF = FormatString.nomorSPAJ(hadiah.reg_spaj);
		
		try {
			//send email
			String to = "";
			String cc = "";
			
			//attachment
			List<File> attachments = new ArrayList<File>();
			
			to = props.getProperty("hadiah.purchasing.memo3.to");
			cc = props.getProperty("hadiah.purchasing.memo3.cc");
			
			
			//to dan cc nya @sinarmasmsiglife.co.id
			String[] emailTo = to.split(";");
			String[] emailCc = cc.split(";");
			
			for(int y=0; y<emailTo.length; y++){
				emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
			}
			for(int y=0; y<emailCc.length; y++){
				emailCc[y] = emailCc[y].concat("@sinarmasmsiglife.co.id");
			}
			
			String file = props.getProperty("pdf.dir.hadiah.memo")+"\\"+hadiah.reg_spaj+"\\Memo2_" + hadiah.reg_spaj +".pdf";
			String file1 = props.getProperty("pdf.dir.hadiah.memo")+"\\"+hadiah.reg_spaj+"\\Memo3_" + hadiah.reg_spaj +".pdf";
			File destDir = new File(file1);
			
			if(!destDir.exists()){
				
				pesan ="Mohon print memo terlebih dahulu";
				
			}else{
				
				File sourceFile = new File(file);
				File sourceFile1 = new File(file1);
				attachments.add(sourceFile);
				attachments.add(sourceFile1);
				
				int upt = update("updateProsesTransfer", hadiah);
				
				if(upt>0){
					String link = "http://www.sinarmasmsiglife.co.id/E-Lions/bas/hadiah.htm?window=upload&t=upPath&_spaj="+hadiah.reg_spaj+"&lspdID=86&mh_no="+hadiah.mh_no;
					String tkey = "";
			   		try {
			   			tkey = commonDao.encryptUrlKey("hadiah_upload", hadiah.reg_spaj + "_" + hadiah.mh_no, App.ID, link);
			   		}catch (Exception e) {
						logger.error("ERROR", e);
					}
			   		link = link + "&tkey="+tkey;
					
					//direct link email ke ajsjavai64, untuk contoh ke localhost
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							props.getProperty("admin.ajsjava"), emailTo, emailCc, null, 
							"MEMO PERINTAH BAYAR "+spajF, "MEMO PERINTAH BAYAR " +
									"<br>Tanggal : "+tgl+
									"<br>No SPAJ : "+spajF+
									"<br>Nama : "+hadiah.pemegang+
									"<br>"+
									"<br>"+
									"<br>Click link dibawah ini jika sudah dilakukan pembayaran"+
									"<br><a href='"+link+"'>click</a>"+
									"<br>jika tidak bisa, copy dan paste link berikut ini ke browser anda."+
									"<br>http://canprilaptop/E-Lions/bas/hadiah.htm?window=upload&t=upPath&_spaj="+hadiah.reg_spaj+"&lspdID=86&mh_no="+hadiah.mh_no+"&tkey="+tkey+
									"<br>"+
									"<br>", attachments, spajF);
					
					pesan = "Transfer dokumen berhasil";
				}else{
					pesan = "Transfer dokumen gagal";
				}
			}
			
		} catch (Exception e) {
			logger.error("ERROR :", e);
			pesan = "ERROR";
		}
		
		return pesan;
	}
	
	//report claim
	public List<Followup> selectreportClaimBasedAgeDeath(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectreportClaimBasedAgeDeath", m);
	}
	
	public List<Followup> selectreportClaimBasedPolicyDuration(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectreportClaimBasedPolicyDuration", m);
	}
	
	public List<Followup> selectreportDetailClaim(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectreportDetailClaim", m);
	}
	
	public List<Followup> selectreportCODByBranch(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectreportCODByBranch", m);
	}
	
	public List<Followup> selectreportClaimByCOD(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectreportClaimByCOD", m);
	}
	
	public Integer selectreportClaimByCOD_TotalCase(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return (Integer) querySingle("selectreportClaimByCOD_TotalCase", m);
	}
	
	public List<Followup> selectreportClaimByMedis(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectreportClaimByMedis", m);
	}
	
	public List<Followup> selectGetTotalClaimByMedis(String bdate, String edate, String lus_id, String polis, String medis, String grup_bank, String nama_bank, String jenis_periode) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("medis", medis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectGetTotalClaimByMedis", m);
	}
	
	public List<Followup> selectreportClaimBasedEntryAge(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectreportClaimBasedEntryAge", m);
	}
	
	public List<Followup> selectreportExGratiaClaim(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank, String jenis_periode) throws DataAccessException{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectreportExGratiaClaim", m);
	}
	
	public List selectreportClaimByProduct(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank,	String jenis_periode) {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectreportClaimByProduct", m);
	}
	
	public List selectreportClaimBySA(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank,	String jenis_periode) {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectreportClaimBySA", m);
	}
	
	public List selectreportClaimByPaid(String bdate, String edate, String lus_id, String polis, String grup_bank, String nama_bank,	String jenis_periode) {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("polis", polis);
		m.put("grup_bank", grup_bank);
		m.put("nama_bank", nama_bank);
		m.put("jenis_periode", jenis_periode);
		return query("selectreportClaimByPaid", m);
	}
	
	public List <Map> selectPolicyRelationDMTM(String spaj) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", spaj);
		return query("selectPolicyRelationDMTM",m);
	}
	
	public Integer selectCountJnFollowUp(String reg_spaj, Integer lsfu_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("spaj", reg_spaj);
		m.put("lsfu_id", lsfu_id);
		return (Integer) querySingle("selectCountJnFollowUp", m);
	}
	
	public Followup selectLstFollowUp (Integer lsfu_id) throws DataAccessException {
		return (Followup) querySingle("selectLstFollowUp", lsfu_id);
	}

	/**
	 * Proses untuk Select Auto Debet Follow Up Premi Lanjutan
	 * 
	 * @author Canpri 
	 * @since Jun 22, 2012
	 * @param reg_spaj
	 * @return List
	 */
	public List selectAutoDebetFollowupBilling(String reg_spaj) {
		return query("selectAutoDebetFollowupBilling", reg_spaj);
	}
	
	/**
	 * Proses untuk Select Billing Follow Up Premi Lanjutan
	 * 
	 * @author Canpri 
	 * @since Jul 25, 2012
	 * @param reg_spaj
	 * @return List
	 */
	public List selectViewBillingFollowup(String reg_spaj) {
		return query("selectViewBillingFollowup", reg_spaj);
	}
	
	/**
	 * Proses untuk Select Tahapan Follow Up Premi Lanjutan
	 * 
	 * @author Canpri 
	 * @since Jul 25, 2012
	 * @param reg_spaj
	 * @return List
	 */
	public List selectViewTahapanFollowup(String reg_spaj) {
		return query("selectViewTahapanFollowup", reg_spaj);
	}
	
	/**
	 * Proses untuk Select Simpanan Follow Up Premi Lanjutan
	 * 
	 * @author Canpri 
	 * @since Jul 25, 2012
	 * @param reg_spaj
	 * @return List
	 */
	public List selectViewSimpananFollowup(String reg_spaj) {
		return query("selectViewSimpananFollowup", reg_spaj);
	}
	
	/**
	 * Proses untuk Select Call Summary Follow Up Premi Lanjutan
	 * 
	 * @author Canpri 
	 * @since Aug 01, 2012
	 * @param reg_spaj
	 * @return List
	 */
	public List selectViewCallSummary(String reg_spaj) {
		return query("selectViewCallSummary", reg_spaj);
	}

	/**
	 * Cek apakah ada topup premi
	 * 
	 * @author Canpri 
	 * @since Sep 24, 2012
	 * @param followup
	 * @return premi_ke
	 */
	public Integer selectFollowupPremiKeTopup(Followup followup) {
		return (Integer) querySingle("selectFollowupPremiKeTopup",followup);
	}

	public Integer selectLibur(String time) {
		return (Integer) querySingle("selectLibur",time);
	}

	public List selectReportDataUpload(String bdate, String edate, String jenis, Integer jn_transfer) {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("jenis", jenis);
		m.put("jn_transfer", jn_transfer);
		return query("selectReportDataUpload", m);
	}
	
	public List selectReportDataBackToBas(String bdate, String edate, String jenis, Integer jn_transfer, String lus_id) {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("jenis", jenis);
		m.put("lus_id", lus_id);
		m.put("jn_transfer", jn_transfer);
		return query("selectReportDataBackToBas", m);
	}
	
	public List selectReportDataPenerimaanBSPAJ(String bdate, String edate, String jenis) {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("jenis", jenis);
		return query("selectReportDataPenerimaanBSPAJ", m);
	}
	
	public List reportFollowupBillingPL(String bdate, String edate, String lca, Integer jn_report) {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lca", lca);
		if(jn_report==0) return query("reportFollowupBillingPL", m);
		else return query("reportFollowupBillingPLKategori", m);
	}
	
	public List selectEmailLeader(String reg_spaj) {
		return query("selectEmailLeader", reg_spaj);
	}

	public List<DropDown> selectHadiahStableSave(String reg_spaj) {
		return query("selectHadiahStableSave", reg_spaj);
	}

	public Integer updateHadiahStableSave(String reg_spaj, String lh_id) {
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("lh_id", lh_id);
		return update("updateHadiahStableSave", m);
	}

	public String printHadiahStableSave(Hadiah hadiah, User currentUser) {
		Date sysdate = commonDao.selectSysdate();
		String pesan = null;

		hadiah.create_id = Integer.valueOf(currentUser.getLus_id());
		hadiah.create_dt = sysdate;
		
		logger.info(hadiah.reg_spaj);
		Connection conn = null;
		try{
			conn = this.getDataSource().getConnection();
			//String file = props.getProperty("pdf.dir.tt.hadiah")+hadiah.reg_spaj+"\\tanda_terima_hadiah.pdf";
			String file = props.getProperty("pdf.dir.export")+"\\"+uwDao.selectCabangFromSpaj(hadiah.reg_spaj)+"\\"+hadiah.reg_spaj+"\\TANDA_TERIMA_HADIAH.pdf";
			File destDir = new File(file);
			
			if(!destDir.exists()){
				//Jasper Report
				//String dir = props.getProperty("pdf.dir.tt.hadiah")+hadiah.reg_spaj+"\\";
				String dir = props.getProperty("pdf.dir.export")+"\\"+uwDao.selectCabangFromSpaj(hadiah.reg_spaj)+"\\"+hadiah.reg_spaj+"\\";
				
				Map params = new HashMap();
				params.put("reg_spaj", hadiah.reg_spaj);
				
				JasperUtils.exportReportToPdf(
						props.getProperty("report.bas.tt_hadiah")+".jasper", 
						dir, 
						"TANDA_TERIMA_HADIAH.pdf", 
						params, 
						conn,
						PdfWriter.AllowPrinting, null, null);
				
				//pesan = "Print Memo 1 dengan SPAJ "+hadiah.reg_spaj+" Sukses";
			}else{
				//pesan = "Memo 1 dengan SPAJ "+hadiah.reg_spaj+" sudah ada";
			}
			
		}catch(Exception e){
			logger.error("ERROR :", e);
			pesan = "ERROR";
		}finally{
			closeConnection(conn);
		}
		
		return pesan;
	}

	public List<DropDown> selectUserBasSummaryInput(String user) throws DataAccessException {
		String userBas = "in("+user+")";
		return query("selectUserBasSummaryInput", userBas);
	}
	
	public List<DropDown> selectUserBasSummaryInputNew(int pusat) throws DataAccessException {
		Map m = new HashMap();
		m.put("pusat", pusat);
		return query("selectUserBasSummaryInputNew", m);
	}

	public List<DropDown> selectCabangBSM() {
		return query("selectCabangBSM", null);
	}
	
	public List<Map> selectListSpajPremiLanjutan(String lus_id, String lde_id){
		Map m = new HashMap();
		m.put("lus_id", lus_id);
		m.put("lde_id", lde_id);
		return query("selectListSpajPremiLanjutan", m);
	}
	
	public List<Billing> selectMstBillingFlagKuitansi(String spaj, String msbi_bill_no){
		Map m = new HashMap();
		m.put("spaj", spaj);
		m.put("msbi_bill_no", msbi_bill_no);
		return query("selectMstBillingFlagKuitansi", m);
	}
	
	public void updateMstBillingBillNo(String spaj, String msbi_bill_no,Integer msbi_flag_kuitansi){
		Map param = new HashMap();
		param.put("spaj", spaj);
		param.put("msbi_bill_no", msbi_bill_no);
		param.put("msbi_flag_kuitansi", msbi_flag_kuitansi);
		update("updateMstBillingBillNo", param);
	}

	public List<Map> selectJenisBrosur() throws DataAccessException{
		return query("selectJenisBrosur", null);
	}

	public List<FormSpaj> selectFormBrosur(String msf_id, String lca_id, String lus_id, Integer jenis) throws DataAccessException {
		FormSpaj formSpaj = new FormSpaj();
		formSpaj.setMss_jenis(0);
		formSpaj.setMsf_id(msf_id);
		formSpaj.setLca_id(lca_id);
		formSpaj.setLus_id(Integer.parseInt(lus_id));
		if(jenis == null)formSpaj.setJn_brosur(1);
		else formSpaj.setJn_brosur(jenis);
		if(msf_id==null)
			return query("selectNewFormBrosur", formSpaj);
		else
			return query("selectFormBrosur", formSpaj);
	}

	/**
	 * Query untuk menarik jumlah stok brosur yang tersedia di suatu cabang / agen
	 * 
	 * @author Canpri
	 * @since Feb 04, 2013 (1:53:58 PM)
	 * @param brosur
	 * @return
	 */
	public List<Spaj> selectStokBrosur(Spaj brosur) throws DataAccessException {
		return query("selectStokBrosur", brosur);
	}
	
	/**
	 * Proses untuk insert permohonan pengiriman brosur
	 * 
	 * @author Canpri
	 * @since Feb 05, 2013 (8:32:17 AM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processNewFormBrosur(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		FormSpaj f = cmd.getDaftarFormBrosur().get(0);
		String pesan = "";
		if("".equals(f.getMsf_id())) { //INSERT BARU
			
			/** 1. Insert stok brosur di cabang tersebut walaupun masih 0, dan insert form brosur(query sama dengan permintaan spaj) */
			String seq = sequence.sequenceMst_form(0, currentUser.getLca_id());
			for(FormSpaj s : cmd.getDaftarFormBrosur()) {
				s.setBusdev(cmd.getBusdev());
				s.setMsf_id(seq);
				s.setMss_jenis(2);
				s.setLca_id(currentUser.getLca_id());
				s.setMsab_id(0); //AGENT 000000
				s.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				//no_blanko tidak boleh diisi saat baru minta
				s.setStart_no_blanko(null);
				s.setEnd_no_blanko(null);
				if(cmd.getBmi_id() != null)
					s.setBmi_id(cmd.getBmi_id());
				if(cmd.getAdmTravIns() != null) 
					s.setTrav_ins_type(cmd.getAdmTravIns());
				if(((Integer) querySingle("selectCekSpaj", s))==0) {
					Spaj spaj = new Spaj();
					spaj.newSpaj(2, s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 0, s.getLus_id(),s.getLus_id());
					insert("insertNewSpaj", spaj);
				}
				insert("insertNewFormSpaj", s);
			}
			cmd.setMsf_id(seq);

			/** 2. Insert history form brosur */
			FormHist form = new FormHist();
			form.setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
			form.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			form.setPosisi(0);
			form.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			form.setMsfh_desc("Permintaan oleh Branch Admin");			
			insert("insertFormHistory", form);
			
			pesan = "Permintaan anda sudah tersimpan dengan nomor " + cmd.getMsf_id();
		} else { //UPDATE DATA LAMA
			
			/** 1. Update jumlah pada form brosur(query sama dengan permintaan spaj) */
			for(FormSpaj s : cmd.getDaftarFormBrosur()) {
				update("updateFormSpaj", s);
			}
			
			/** 2. Insert history form */
			FormHist form = new FormHist();
			form.setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
			form.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			form.setPosisi(0);
			form.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			form.setMsfh_desc("Perubahan Permintaan oleh Branch Admin");			
			insert("insertFormHistory", form);
			
			pesan = "Data Permintaan anda sudah di-update.";
		}
		
		//email ke cabang
		HashMap mapEmail = uwDao.selectMstConfig(6, "processNewFormBrosur", "NEW_FORM_BROSUR");
		String[] cc = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
		User userCabang=selectLstUser(cmd.getDaftarFormBrosur().get(0).getLus_id());
//		String to[]={userCabang.getEmail(),"pratidina@sinarmasmsiglife.co.id"};
		String to[]={userCabang.getEmail(),"Rakhel.Apriliana@sinarmasmsiglife.co.id"}; //chandra a - 20180305
		String sukses="";
		if(userCabang.getEmail()==null){
//			to = new String[]{"pratidina@sinarmasmsiglife.co.id"};
			to = new String[]{"Rakhel.Apriliana@sinarmasmsiglife.co.id"}; //chandra a - 20180305
		}
		
		Map tgl = (Map) querySingle("selectTglFormHist", cmd.getDaftarFormBrosur().get(0).getMsf_id());
		
		String subject="KONFIRMASI PERMINTAAN MARKETING TOOLS DENGAN NOMOR "+cmd.getDaftarFormBrosur().get(0).getMsf_id();
		StringBuffer mssg = new StringBuffer();
		mssg.append("Admin\t\t : "+userCabang.getLus_full_name());
		mssg.append("\nCabang\t\t : "+userCabang.getCabang());
		mssg.append("\nNo. Permintaan\t : "+cmd.getDaftarFormBrosur().get(0).getMsf_id());
		mssg.append("\nTgl. Permintaan\t : "+(String)tgl.get("TGL_MINTA"));
		mssg.append("\n\n<table width='50%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
		mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>Jenis Permintaan</strong></td><td align='center'><strong>Jumlah</strong></td></tr>");
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++){
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);
			if(s.getMsf_amount_req()>0){
				mssg.append("<tr>");
				mssg.append("<td align='center'>"+s.getLsjs_desc()+"</td>");
				mssg.append("<td align='center'>"+s.getMsf_amount_req()+"</td>");
				mssg.append("</tr>");
			}
		}
		mssg.append("</table>");
		
		try {
//			email.send(true, props.getProperty("admin.ajsjava"), to, cc, null, subject, mssg.toString(), null);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), to, cc, null, 
					subject, mssg.toString(), null, null);
			
			String link1 = "http://elions.sinarmasmsiglife.co.id/bas/cabang.htm?window=approveBrosurByEmail&app=1&msf_id="+cmd.getMsf_id();
			String link2 = "http://elions.sinarmasmsiglife.co.id/bas/cabang.htm?window=approveBrosurByEmail&app=0&msf_id="+cmd.getMsf_id();
			String tkey1 = "";
			String tkey2 = "";
	   		try {
	   			tkey1 = commonDao.encryptUrlKey("approveBrosurByEmail", cmd.getMsf_id(), App.ID, link1);
	   			tkey2 = commonDao.encryptUrlKey("approveBrosurByEmail", cmd.getMsf_id(), App.ID, link2);
	   		}catch (Exception e) {
				logger.error("ERROR", e);
			}
	   		link1 = link1 + "&tkey="+tkey1;
	   		link2 = link2 + "&tkey="+tkey2;
			
			//email  approval
			mssg.append("\n\n<a href='"+link1+"'>Approve</a>");
			mssg.append("\t\t<a href='"+link2+"'>Reject</a>");
			String subject2 = "APPROVAL KONFIRMASI PERMINTAAN MARKETING TOOLS DENGAN NOMOR "+cmd.getDaftarFormBrosur().get(0).getMsf_id();
			
			//chandra a - 20180305
//			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
//					props.getProperty("admin.ajsjava"), new String[]{"pratidina@sinarmasmsiglife.co.id"}, new String[]{"yulin@sinarmasmsiglife.co.id"}, null, 
//					subject2, mssg.toString(), null, null);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), new String[]{"Rakhel.Apriliana@sinarmasmsiglife.co.id"}, new String[]{"yenchilia@sinarmasmsiglife.co.id"}, null, 
					subject2, mssg.toString(), null, null);
			//chandra a - 20180305
			
//				email.send(
//				true, props.getProperty("admin.ajsjava"),
//				new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, subject, mssg.toString(), null);
			
		} catch (MailException e) {
			logger.error("ERROR :", e);
		}
		
		return pesan;
	}
	
	/**
	 * Proses untuk pembatalan permohonan pengiriman brosur
	 * 
	 * @author Canpri
	 * @since Feb 05, 2013 (1:48:42 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processCancelFormBrosur(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		
		/** 1. update form brosur(query sama dengan permintaan spaj) */
			for(FormSpaj s : cmd.getDaftarFormBrosur()) {
				s.setMsf_amount(0);
				s.setPosisi(2); //cancel
				update("updateFormSpaj", s);
			}
		
		/** 2. insert history form */
		if(!cmd.getDaftarFormBrosur().isEmpty()) {
			FormHist f = new FormHist();
			f.setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
			f.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			f.setPosisi(2);
			f.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			f.setMsfh_desc("Dibatalkan oleh Branch Admin");			
			insert("insertFormHistory", f);
		}
		
		return "Data Permintaan anda sudah dibatalkan.";
	}
	
	/**
	 * Proses untuk persetujuan permohonan pengiriman brosur dari pusat ke cabang,
	 * oleh agency support / BAS
	 * 
	 * @author Canpri
	 * @since Feb 6, 2013 (9:09:42 AM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 * @throws MessagingException 
	 * @throws MailException 
	 */
	public String processApprovalFormBrosur(CommandControlSpaj cmd, User currentUser) throws DataAccessException{
		Boolean isSimCard = false;
		
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);
			s.setPosisi(1); //approve
			
			if(s.getLsjs_id() == 21 && s.getMsf_amount_req() > 0) isSimCard = true;
			
			/** 1. update form brosur(query sama dengan permintaan spaj) */
			update("updateFormSpaj", s);

		}
		
		/** 2. insert form history */
		cmd.getFormHist().setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
		cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getFormHist().getMsf_id()));
		cmd.getFormHist().setPosisi(1);
		cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		insert("insertFormHistory", cmd.getFormHist());
		//email ke pic masing2
		/*String to[]=props.getProperty("bas.email.approve.to").split(";");*/
		
		//get email pic masing2
		String email_to = "";
		if(cmd.getDaftarFormBrosur().get(0).getBusdev().equals("1"))email_to = "Ani_chrys@sinarmasmsiglife.co.id";
		if(cmd.getDaftarFormBrosur().get(0).getBusdev().equals("2"))email_to = "nixon@sinarmasmsiglife.co.id";
		if(cmd.getDaftarFormBrosur().get(0).getBusdev().equals("3"))email_to = "grisye@sinarmasmsiglife.co.id";
		if(cmd.getDaftarFormBrosur().get(0).getBusdev().equals("4"))email_to = "jelita@sinarmasmsiglife.co.id";
		
		String to[]=new String[]{email_to};
		String subject="Approved Brosur from BAS";
		String message="Form Permintaan Brosur oleh Cabang "+cmd.getDaftarFormBrosur().get(0).getLca_nama()+ 
						" dengan no permintaan " + cmd.getDaftarFormBrosur().get(0).getMsf_id() +
						" telah disetujui.\nDiharapkan untuk mengirimkan Brosur tersebut secepatnya";
		try {
//			email.send(
//					true, (currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava")),
//					to, null, null, subject, message, null);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					(currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava")), to, null, null, 
					subject, message, null, null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		}
		
		/*if(isSimCard) {
			to = new String[]{"Dewi_Andriyati@sinarmasmsiglife.co.id"};
			message="Form Permintaan Brosur oleh Cabang "+cmd.getDaftarFormBrosur().get(0).getLca_nama()+ 
			" dengan no permintaan " + cmd.getDaftarFormBrosur().get(0).getMsf_id() +
			" telah disetujui.\nSilahkan UW melakukan proses selanjutnya";
			try {
				email.send(
						false, (currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava")),
						to, null, null, subject, message, null);
				email.send(
						false, (currentUser.getEmail()!=null?currentUser.getEmail():props.getProperty("admin.ajsjava")),
						new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, subject, message, null);
			} catch (MailException e) {
				logger.error("ERROR :", e);
			} catch (MessagingException e) {
				logger.error("ERROR :", e);
			}			
		}*/
		
		return 
			"Data permintaan cabang sudah disetujui. "
			+ ". \\nPermintaan akan selanjutnya diproses"
			+	"\\nSetelah dilakukan pengiriman, harap lengkapi detail pengiriman di menu [BAS] PENGIRIMAN BROSUR.";
	}
	
	/**
	 * Proses untuk penolakan permohonan pengiriman brosur dari pusat ke cabang,
	 * oleh agency support / BAS
	 * 
	 * @author Canpri
	 * @since Feb 06, 2007 (9:09:42 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processRejectFormBrosur(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);
			s.setPosisi(3); //reject
			
			/** 1. update form brosur(query sama dengan permintaan spaj) */
			update("updateFormSpaj", s);
		}

		/** 2. insert form history */
		cmd.getFormHist().setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
		cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getFormHist().getMsf_id()));
		cmd.getFormHist().setPosisi(3);
		cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		insert("insertFormHistory", cmd.getFormHist());
		
		//email ke cabang
		User userCabang=selectLstUser(cmd.getDaftarFormBrosur().get(0).getLus_id());
		String to[]={userCabang.getEmail()};
		String cc[] = new String[]{"pratidina@sinarmasmsiglife.co.id;yulin@sinarmasmsiglife.co.id"};
		String sukses="";
		if(userCabang.getEmail()==null){
			//chandra a - 20180305
//			to = new String[]{"pratidina@sinarmasmsiglife.co.id"};
//			cc = new String[]{"yulin@sinarmasmsiglife.co.id"};
			to = new String[]{"Rakhel.Apriliana@sinarmasmsiglife.co.id"};
			cc = new String[]{"yenchilia@sinarmasmsiglife.co.id"};
			//chandra a - 20180305
		}
		
		Map tgl = (Map) querySingle("selectTglFormHist", cmd.getDaftarFormBrosur().get(0).getMsf_id());
		
		String subject="KONFIRMASI PENOLAKAN PERMINTAAN MARKETING TOOLS DENGAN NOMOR "+cmd.getDaftarFormBrosur().get(0).getMsf_id();
		StringBuffer mssg = new StringBuffer();
		mssg.append("Admin\t\t : "+userCabang.getLus_full_name());
		mssg.append("\nCabang\t\t : "+userCabang.getCabang());
		mssg.append("\nNo. Permintaan\t : "+cmd.getDaftarFormBrosur().get(0).getMsf_id());
		mssg.append("\nTgl. Permintaan\t : "+(String)tgl.get("TGL_MINTA"));
		mssg.append("\nTgl. Penolakan\t : "+(String)tgl.get("TGL_REJECT"));
		mssg.append("\n\n<table width='50%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
		mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>Jenis Permintaan</strong></td><td align='center'><strong>Jumlah</strong></td></tr>");
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++){
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);
			if(s.getMsf_amount_req()>0){
				mssg.append("<tr>");
				mssg.append("<td align='center'>"+s.getLsjs_desc()+"</td>");
				mssg.append("<td align='center'>"+s.getMsf_amount()+"</td>");
				mssg.append("</tr>");
			}
		}
		mssg.append("</table>");
		
		try {
//			email.send(true, props.getProperty("admin.ajsjava"), to, cc, null, subject, mssg.toString(), null);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), to, cc, null, 
					subject, mssg.toString(), null, null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		}

		return 
			"Data permintaan cabang sudah ditolak. "
			+ ". \\nAlasan penolakan akan diteruskan ke cabang bersangkutan.";
	}
	
	/**
	 * Proses yang menandakan bahwa permohonan pengiriman brosur dari pusat ke cabang
	 * sudah dilakukan oleh GA
	 * 
	 * @author Canpri
	 * @since Feb 06, 2013 (10:28:42 AM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 * @throws MessagingException 
	 * @throws MailException 
	 */
	public String processSendFormBrosur(CommandControlSpaj cmd, User currentUser) throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);

			s.setPosisi(4); //sent
						
			/** 1. update form brosur(query sama dengan permintaan spaj) */
			update("updateFormSpaj", s);
			
			/** 2. update stok brosur di stok.stock_brosur */
			Integer stok = -1 * s.getMsf_amount();
			s.setMsf_amount(stok);
			update("updateStokBrosur", s);
			s.setMsf_amount(-1 * stok);
		}

		/** 2. insert form history */
		cmd.getFormHist().setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
		cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getFormHist().getMsf_id()));
		cmd.getFormHist().setPosisi(4);
		cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		insert("insertFormHistory", cmd.getFormHist());
		
		//email ke cabang
		User userCabang=selectLstUser(cmd.getDaftarFormBrosur().get(0).getLus_id());
		String to[]={userCabang.getEmail()};
		String cc[] = new String[]{"pratidina@sinarmasmsiglife.co.id"};
		String sukses="";
		if(userCabang.getEmail()==null){
			to = new String[]{"pratidina@sinarmasmsiglife.co.id"};
			cc = new String[]{""};
		}
		
		Map tgl = (Map) querySingle("selectTglFormHist", cmd.getDaftarFormBrosur().get(0).getMsf_id());
		
		String subject="KONFIRMASI PENGIRIMAN PERMINTAAN MARKETING TOOLS DENGAN NOMOR "+cmd.getDaftarFormBrosur().get(0).getMsf_id();
		StringBuffer mssg = new StringBuffer();
		mssg.append("Admin\t\t : "+userCabang.getLus_full_name());
		mssg.append("\nCabang\t\t : "+userCabang.getCabang());
		mssg.append("\nNo. Permintaan\t : "+cmd.getDaftarFormBrosur().get(0).getMsf_id());
		mssg.append("\nTgl. Permintaan\t : "+(String)tgl.get("TGL_MINTA"));
		mssg.append("\nTgl. Pengiriman\t : "+(String)tgl.get("TGL_KIRIM"));
		mssg.append("\n\n<table width='50%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
		mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>Jenis Permintaan</strong></td><td align='center'><strong>Jumlah</strong></td></tr>");
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++){
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);
			if(s.getMsf_amount_req()>0){
				mssg.append("<tr>");
				mssg.append("<td align='center'>"+s.getLsjs_desc()+"</td>");
				mssg.append("<td align='center'>"+s.getMsf_amount()+"</td>");
				mssg.append("</tr>");
			}
		}
		mssg.append("</table>");
		mssg.append("\n\nHarap update tombol telah diterima jika sudah menerima item tersebut.");
		
		try {
//			email.send(true, props.getProperty("admin.ajsjava"), to, cc, null, subject, mssg.toString(), null);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), to, cc, null, 
					subject, mssg.toString(), null, null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		}
		
		return 
			sukses+"Data pengiriman Marketing Tools sudah diupdate.";
	}
	
	/**
	 * Proses yang menandakan bahwa brosur hasil permintaan dari cabang ke pusat sudah dikirimkan
	 * ke cabang dan sudah diterima oleh cabang, sehingga otomatis menambah jumlah brosur yang ada di cabang
	 * 
	 * @author Canpri
	 * @since Feb 06, 2013 (1:26:05 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processReceiveFormBrosur(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);
			s.setPosisi(5); //received
			
			/** 1. update form brosur dan stok brosur(query sama dengan permintaan spaj) */
			update("updateFormSpaj", s);
			Spaj spaj = new Spaj();
			spaj.newSpaj(s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), s.getMsf_amount(), Integer.valueOf(currentUser.getLus_id()),Integer.valueOf(currentUser.getLus_id()));
			update("updateStokSpaj", spaj);
			
		}

		/** 2. insert form history */
		if(!cmd.getDaftarFormBrosur().isEmpty()) {
			FormHist f = new FormHist();
			f.setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
			f.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			f.setPosisi(5);
			f.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			f.setMsfh_desc("Sudah diterima oleh Branch Admin");			
			insert("insertFormHistory", f);
		}

		return "Data penerimaan Brosur sudah diupdate. Jumlah Brosur di sistem sudah ditambahkan.";
	}

	/**
	 * Query untuk menarik daftar permintaan pengiriman brosur
	 * @author Canpri
	 * @since Feb 05, 2013 (1:28:03 PM)
	 * @param formBrosur
	 * @return
	 */
	public List<FormSpaj> selectDaftarFormBrosur(FormSpaj formBrosur) throws DataAccessException {
		return query("selectDaftarFormBrosur", formBrosur);
	}
	
	/**Fungsi:	Untuk Menampilkan data form brosur
	 * @param	String msfId
	 * @return	List
	 * @author 	Canpri
	 * @since Feb 07, 2013 (1:28:03 PM)
	 */
	public List selectSearchFormBrosurExpress(String msfId){
		return query("selectSearchFormBrosurExpress",msfId);
	}
	
	/**Fungsi:	Untuk Menampilkan data form brosur
	 * @param	FormSpaj brosur
	 * @return	List
	 * @author 	Canpri
	 * @since Feb 07, 2013 (1:28:03 PM)
	 */
	public List selectSearchFormBrosurDetail(FormSpaj brosur){
		return query("selectSearchFormBrosurDetail",brosur);
	}
	
	/**Fungsi:	Insert Hadiah Power Save from BAC
	 * @param	Hadiah
	 * @return	String
	 * @author 	Canpri
	 * @since mar 25, 2013 (9:25:03 AM)
	 */
	public String saveHadiahBAC(Hadiah hadiah) throws DataAccessException{
		String pesan = null;

		int update = update("updateHadiah", hadiah);
		
		hadiah.mh_tgl_input = commonDao.selectSysdate();
		hadiah.create_dt = commonDao.selectSysdate();
		
		if(update > 0){ //bila update data sebelumnya
			
			hadiah.keterangan = "Transfer Ke Purchasing";
			insert("insertHadiahHist", hadiah);
			pesan = "Data berhasil diupdate";
			
		}else{ //bila baru pertama kali input
			hadiah.keterangan = "Input Data Hadiah"; //keterangan MST_HADIAH_HIST
			
			insert("insertHadiah", hadiah);
			insert("insertHadiahHist", hadiah);
			
			pesan = "Data berhasil disimpan";
		}
		return pesan;
	}
	
	/**Fungsi:	Delete Hadiah Power Save from BAC
	 * @param	Hadiah
	 * @return	String
	 * @author 	Canpri
	 * @since mar 25, 2013 (9:25:03 AM)
	 */
	public String deleteHadiahBAC(Hadiah hadiah) throws DataAccessException{
		String pesan = null;
		
		delete("deleteHadiahHist", hadiah);
		delete("deleteHadiah", hadiah);
		
		hadiah.keterangan = "Delete Data Hadiah";
		pesan = "Data berhasil dihapus";
			
		return pesan;
	}

	//select from mst_hadiah
	public List<Hadiah> selectHadiah(String reg_spaj) {
		return query("selectHadiah",reg_spaj);
	}

	public List selectHadiahPS(Double premi) {
		return query("selectHadiahPS",premi);
	}

	public Hadiah selectLh_id(Double premi) {
		return (Hadiah) querySingle("selectLh_id", premi);
	}

	public String saveJenisHadiahPS(String nama_hadiah, String premi, String standard, String aktif, String lus_id) {
		String pesan = null;
		
		Map map=new HashMap();
		map.put("lh_nama",nama_hadiah );
		map.put("lh_harga",premi );
		map.put("flag_standard",standard );
		map.put("flag_active",aktif );
		map.put("create_id",lus_id );
		map.put("lhc_id","8" );
		map.put("lh_hari","7" );

		try{
			insert("insertJenisHadiahPS", map);
			pesan = "Data berhasil disimpan";
		}catch(Exception e){
			e.getLocalizedMessage();
			pesan = "Data gagal disimpan";
		}
		return pesan;
	}

	public String updateJenisHadiah(String lh_id, String lhc_id, String mode) {
		String pesan = null;
		
		Map map=new HashMap();
		map.put("lh_id",lh_id );
		map.put("lhc_id",lhc_id );
		map.put("mode",mode );
		
		int update = update("updateJenisHadiah", map);
		
		if(update > 0){ 
			pesan = "Data berhasil diupdate";
		}else{
			pesan = "Data gagal diupdate";
		}
		
		return pesan;
	}

	public List selectReportJTPowersave4thn(String bdate, String edate) {
		Map map=new HashMap();
		map.put("bdate",bdate );
		map.put("edate",edate );
		
		return query("selectReportJTPowersave4thn",map);
	}
	
	/**
	 * Untuk pencarian Simas Saving Plan
	 * @return List
	 * @author Daru
	 * @since Apr 15, 2013
	 */
	public List selectSearchSsp(String id, String nama, String no_kanwill, String no_rek){
		Map params = new HashMap();
		if(id != null && id != ""){
			params.put("id", id);
		}
		if(nama != null && nama != ""){
			params.put("nama", nama);
		}
		if(no_kanwill != null && no_kanwill != ""){
			params.put("no_kanwill", no_kanwill);
		}
		if(no_rek != null && no_rek != ""){
			params.put("no_rek", no_rek);
		}
		
		return query("selectSearchSsp", params);
	}
	
	/**
	 * Untuk menampilkan data Simas Saving Plan
	 * @param id
	 * @return Map
	 * @author Daru
	 * @since Apr 15, 2013
	 */
	public Map selectViewSsp(String id){
		Map res = (Map) querySingle("selectViewSsp", id);
		String tipe_reas = "";
		if(res.get("MCP_FLAG_REAS") == null || res.get("MCP_FLAG_REAS") == ""){
			tipe_reas = "Belum";
		}else{
			Integer flag = (Integer) res.get("MCP_FLAG_REAS");
			switch(flag){
				case 0:
					tipe_reas = "Non-reas";
					break;
				case 1:
					tipe_reas = "Treaty";
					break;
				case 2:
					tipe_reas = "Facultative";
					break;
				default:
					tipe_reas = "Belum";
			}
		}
		res.put("MCP_FLAG_REAS", tipe_reas);
		
		return res;
	}
	
	/**
	 * Untuk menampilkan data billing Simas Saving Plan
	 * @param id
	 * @return List
	 * @author Daru
	 * @since Apr 16, 2013
	 */
	public List selectViewSspBill(String id){
		return query("selectViewSspBill", id);
	}
	
	/**
	 * Untuk menampilkan data Simas Saving Plan yg ingin dibatalkan
	 * @param id
	 * @return Map
	 * @author Daru
	 * @since Apr 16, 2013
	 */
	public Map selectViewBatalSsp(String id){
		return (Map) querySingle("selectViewBatalSsp", id);
	}
	
	/**
	 * Untuk update proses batal Simas Saving Plan
	 * @param id
	 * @param lssp_id
	 * @param lspd_id
	 * @param mcp_flag_bill
	 * @param mcp_tgl_batal
	 * @param mcp_alasan
	 * @return boolean
	 * @author Daru
	 * @since Apr 17, 2013
	 */
	public boolean updateBatalSsp(String id, Integer lssp_id, Integer lspd_id, Integer mcp_flag_bill, Date mcp_tgl_batal, String mcp_alasan, String mcp_note, Integer lus_id){
		Map params = new HashMap();
		params.put("id", id);
		params.put("lssp_id", lssp_id);
		params.put("lspd_id", lspd_id);
		params.put("mcp_flag_bill", mcp_flag_bill);
		params.put("mcp_tgl_batal", mcp_tgl_batal);
		params.put("mcp_alasan", mcp_alasan);
		params.put("mcp_note", mcp_note);
		params.put("lus_id", lus_id);
		
		int update = update("updateBatalSsp", params);
		if(update > 0){
			insert("insertBatalSspHist", params);
			return true;
		}else{
			return false;
		}
	}
	
	//Canpri select jatuh tempo power save per cabang
	public List selectReport_Powersave(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id, String msag_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("lca", lca);
		m.put("lwk", lwk);
		m.put("lsrg", lsrg);
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("msag_id", msag_id);
		return query("selectReport_Powersave", m);
	}

	//Canpri select jatuh tempo stable link per cabang
	public List selectReport_Stablelink(String lca, String lwk, String lsrg, String bdate, String edate, String lus_id, String msag_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("lca", lca);
		m.put("lwk", lwk);
		m.put("lsrg", lsrg);
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		m.put("msag_id", msag_id);
		return query("selectReport_Stablelink", m);
	}
	
	public Double selectTotalBayarKlaim(String spaj)throws DataAccessException{
		return (Double) querySingle("selectTotalBayarKlaim", spaj);
	}
	
	public List selectMstClaim(String spaj) throws DataAccessException {
		return query("selectMstClaim", spaj);
	}

	public List selectReport_Produksi_Mingguan(String bdate, String edate, String lus_id) {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("lus_id", lus_id);
		return query("selectReport_Produksi_Mingguan", m);
	}

	public List selectDetailPowerSaveFU(String reg_spaj)throws DataAccessException {
		return query("selectDetailPowerSaveFU", reg_spaj);
	}

	public Object selectDetailStableLinkFU(String reg_spaj) throws DataAccessException{
		return query("selectDetailStableLinkFU", reg_spaj);
	}

	public List selectStokBrosurBusDev(String busdev, String jenis) {
		Map m = new HashMap();
		m.put("busdev", busdev);
		m.put("jenis", jenis);
		return query("selectStokBrosurBusDev", m);
	}

	public void updateStokBrosur(FormSpaj formbrosur) {
		update("updateStokBrosur", formbrosur);
	}

	public void updateStokSpajUw(FormSpaj formspaj) {
		update("updateStokSpajUw", formspaj);
	}
	
	public void updatePenguranganSpajUw(FormSpaj formspaj) {
		update("updatePenguranganSpajUw", formspaj);
	}
	
	public String TambahBrosur(String prefix, String nm_brosur, String busdev, User currentUser, String jenis)throws DataAccessException {
		String pesan = "";
		//cek ke lst_jenis yg jenis = 1 sudah ada apa belum(prefix tidak boleh sama)
		Integer pref = (Integer) querySingle("selectPrefixBrosur", prefix);
		
		if(pref>0){
			pesan = "Prefix sudah digunakan. Tambah brosur gagal.";
		}else{
			Map m = new HashMap();
			m.put("prefix", prefix);
			m.put("nm_brosur", nm_brosur);
			m.put("lus_id", currentUser.getLus_id());
			m.put("jenis", jenis);
			
			//insert ke lst_jenis
			insert("insertBrosur", m);
			
			//get stok_id dari STOK.STOCK_BROSUR
			Integer id = (Integer) querySingle("selectStokIdBrosur", null);
			
			//insert ke STOK.STOCK_BROSUR
			//for(int i=0;i<lsBusdev.size();i++){
				//get stok_id dari STOK.STOCK_BROSUR
				Integer no = id;
				String stok_id = "BR"+FormatString.rpad("0", no.toString(), 3);
				m.put("stok_id", stok_id);
				
				//DropDown dd = lsBusdev.get(i);
				//m.put("busdev", dd.getKey());
				m.put("busdev", busdev);
				insert("insertStokBrosur",m);
			//}
			
			pesan = "Tambah item berhasil.";
		}
		
		return pesan;
	}
	
	public Integer selectJenisFormBrosur(String msf_id)throws DataAccessException {
		return (Integer) querySingle("selectJenisFormBrosur", msf_id);
	}

	public List selectReport_LisensiAgent(String bdate, String edate, String jenis, String lus_id, String lar_email,
			String lca, String lwk, String lsrg)throws DataAccessException {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("jenis", jenis);//1 report, 2 scheduler
		m.put("lus_id", lus_id);
		m.put("email", lar_email);
		m.put("lca", lca);
		m.put("lwk", lwk);
		m.put("lsrg", lsrg);
		return query("selectReport_LisensiAgent", m);
	}

	public List<KartuNama> selectDaftarKartuNama(KartuNama cmd)throws DataAccessException {
		return query("selectDaftarKartuNama", cmd);
	}
	
	public List<KartuNama> selectDaftarNoKartuNama(KartuNama cmd)throws DataAccessException {
		return query("selectDaftarNoKartuNama", cmd);
	}

	public String processKartuNama(KartuNama cmd, User currentUser, HttpSession session)throws DataAccessException {
		List<Map> d_agent = (List<Map>) session.getAttribute("resultDaftarAgent");
		Integer no = 1;
		
		Map m = (Map) querySingle("selectIDKartuNama", null);
		if(m.get("ID")!=null)no = ((BigDecimal)m.get("ID")).intValue() + 1;
		String id = org.apache.commons.lang.StringUtils.leftPad(no.toString(), 4, "0");
		id = new SimpleDateFormat("yyyyMM").format(new Date())+id;
		
		for(int i=0;i<d_agent.size();i++){
			Map agent = d_agent.get(i);
			cmd.setMsag_id((String)agent.get("MSAG_ID"));
			cmd.setMkn_lus_id(currentUser.getLus_id());
			cmd.setMkn_approve(0);
			cmd.setMkn_id(id);
			cmd.setMkn_position(2);
			cmd.setMkn_document(cmd.getMkn_id()+".pdf");
			cmd.setTelp((String)agent.get("TELP"));
			
			insert("insertKartuNama", cmd);
		}
		session.removeAttribute("resultDaftarAgent");
		
		/** Insert history*/
		FormHist form = new FormHist();
		form.setMsf_id(cmd.getMkn_id());
		form.setMsf_urut(selectNoUrutFormHistory(cmd.getMkn_id()));
		form.setPosisi(cmd.getMkn_position());
		form.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		form.setMsfh_desc("Permintaan kartu nama Branch Admin");			
		insert("insertFormHistory", form);
		
		cmd.setMkn_position(0);
		
		//email ke cabang
		User userCabang = selectLstUser(Integer.parseInt(cmd.getMkn_lus_id()));
		
		HashMap mapEmail = uwDao.selectMstConfig(6, "processKartuNama", "KONFIRMASI_PERMINTAAN_KARTU_NAMA");
		String from = props.getProperty("admin.ajsjava");
		String emailto = mapEmail.get("NAME")!=null? mapEmail.get("NAME").toString():null;
		String emailcc = mapEmail.get("NAME2")!=null? mapEmail.get("NAME2").toString():null;
		
		if(userCabang.getEmail() != null) emailto = emailto + ";" + userCabang.getEmail();
		
		String[] to = emailto.split(";");
		String[] cc = emailcc.split(";");
		
		String sukses = "";
		/*if(userCabang.getEmail()==null){
			to = new String[]{""};
			bcc = new String[]{""};
		}*/
		
		List dataKartuNama = query("selectDataKartuNama", cmd.getMkn_id());
		
		String subject = "KONFIRMASI PERMINTAAN KARTU NAMA DENGAN NOMOR "+cmd.getMkn_id();
		StringBuffer mssg = new StringBuffer();
		mssg.append("Harap untuk menindaklanjuti follow up Permintaan Kartu Nama dengan no. "+cmd.getMkn_id()+" dibawah :");
		mssg.append("\n\n<table width='100%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
		mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>No.</strong></td><td align='center'><strong>Nama</strong></td>" +
				"<td align='center'><strong>Kode Agen</strong></td><td align='center'><strong>Posisi</strong></td><td align='center'><strong>No. Telp</strong></td>" +
				"<td align='center'><strong>Email</strong></td><td align='center'><strong>Keterangan</strong></td></tr>");
		for(int i=0; i<dataKartuNama.size() ; i++){
			Map s = (Map) dataKartuNama.get(i);
			Integer nomor = i+1;
			mssg.append("<tr>");
			mssg.append("<td align='center'>"+nomor+"</td>");
			mssg.append("<td align='center'>"+(String)s.get("NAMA")+"</td>");
			mssg.append("<td align='center'>"+(String)s.get("MSAG_ID")+"</td>");
			mssg.append("<td align='center'>"+(String)s.get("POSISI")+"</td>");
			mssg.append("<td align='center'>"+(String)s.get("TELP")+"</td>");
			mssg.append("<td align='center'>"+(String)s.get("EMAIL")+"</td>");
			mssg.append("<td align='center'>"+(String)s.get("KETERANGAN")+"</td>");
			mssg.append("</tr>");
		}
		mssg.append("</table>");
		mssg.append("\nPengirim: "+currentUser.getLus_full_name());
		
		if(userCabang.getEmail()!=null){
			try {
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
						from, to, cc, null, 
						subject, mssg.toString(), null, null);
			} catch (MailException e) {
				logger.error("ERROR :", e);
			}
		}
		
		return "Permintaan kartu nama telah disimpan";
	}

	public String processKartuNamaNext(KartuNama cmd, User currentUser)throws DataAccessException {
		KartuNama kn = new KartuNama();
		String sukses = "";
		Integer send_mail = 0;
		kn = cmd;
		
		User userCabang=selectLstUser(Integer.parseInt(cmd.getMkn_lus_id()));
		StringBuffer mssg = new StringBuffer();
		
		String from = props.getProperty("admin.ajsjava");
		String emailto = "";
		String emailcc = "";
		
		String to[] = new String[]{};
		String cc[] = new String[]{};
		String subject = "";
		String message = "";
		
		if(kn.getSubmitMode().equalsIgnoreCase("approve_ds")){
			kn.setMkn_position(3);
			kn.setMkn_approve(1);
			send_mail = 1;
			
			HashMap mapEmail = uwDao.selectMstConfig(6, "processKartuNamaNext", "APPROVE_DS");
			emailto = mapEmail.get("NAME")!=null? mapEmail.get("NAME").toString():null;
			emailcc = mapEmail.get("NAME2")!=null? mapEmail.get("NAME2").toString():null;
			
			//Email ke cabang
			if(userCabang.getEmail()!=null){
				emailcc = emailto + ";" + emailcc;
				emailto = userCabang.getEmail();
			}
			
			to = emailto.split(";");
			cc = emailcc.split(";");
			
			List dataKartuNama = query("selectDataKartuNama", cmd.getMkn_id());
			
			subject="KONFIRMASI APPROVE KARTU NAMA DENGAN NOMOR "+cmd.getMkn_id()+" OLEH AGENCY DISTRIBUTION";
			
			mssg.append("Harap untuk menindaklanjuti follow up Permintaan Kartu Nama dengan no. "+cmd.getMkn_id()+" dibawah :");
			mssg.append("\n\n<table width='100%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
			mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>No.</strong></td><td align='center'><strong>Nama</strong></td>" +
					"<td align='center'><strong>Kode Agen</strong></td><td align='center'><strong>Posisi</strong></td><td align='center'><strong>No. Telp</strong></td>" +
					"<td align='center'><strong>Email</strong></td><td align='center'><strong>Keterangan</strong></td></tr>");
			for(int i=0; i<dataKartuNama.size() ; i++){
				Map s = (Map) dataKartuNama.get(i);
				Integer nomor = i+1;
				mssg.append("<tr>");
				mssg.append("<td align='center'>"+nomor+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("NAMA")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("MSAG_ID")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("POSISI")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("TELP")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("EMAIL")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("KETERANGAN")+"</td>");
				mssg.append("</tr>");
			}
			mssg.append("</table>");
			mssg.append("\nPengirim: "+currentUser.getLus_full_name());
			mssg.append("\nNB: Email ini dikirim secara otomatis oleh sistem E-Lions. Harap tidak mereply email ini.");
			
			sukses = "permintaan kartu nama telah disetujui";
		}else if(kn.getSubmitMode().equalsIgnoreCase("approve_bd")){
			kn.setMkn_position(2);
			kn.setMkn_approve(1);
			send_mail = 1;
			
			HashMap mapEmail = uwDao.selectMstConfig(6, "processKartuNamaNext", "APPROVE_BD");
			emailto = mapEmail.get("NAME")!=null? mapEmail.get("NAME").toString():null;
			emailcc = mapEmail.get("NAME2")!=null? mapEmail.get("NAME2").toString():null;
			
			//Email ke cabang
			emailcc = emailcc + ";" + userCabang.getEmail();
			
			to = emailto.split(";");
			cc = emailcc.split(";");
			
			List dataKartuNama = query("selectDataKartuNama", cmd.getMkn_id());
			
			subject = "KONFIRMASI APPROVE KARTU NAMA DENGAN NOMOR "+cmd.getMkn_id()+" OLEH BUSSINESS DEVELOPMENT";

			mssg.append("Harap untuk menindaklanjuti follow up Permintaan Kartu Nama dengan no. "+cmd.getMkn_id()+" dibawah :");
			mssg.append("\n\n<table width='100%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
			mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>No.</strong></td><td align='center'><strong>Nama</strong></td>" +
					"<td align='center'><strong>Kode Agen</strong></td><td align='center'><strong>Posisi</strong></td><td align='center'><strong>No. Telp</strong></td>" +
					"<td align='center'><strong>Email</strong></td><td align='center'><strong>Keterangan</strong></td></tr>");
			for(int i=0; i<dataKartuNama.size() ; i++){
				Map s = (Map) dataKartuNama.get(i);
				Integer nomor = i+1;
				mssg.append("<tr>");
				mssg.append("<td align='center'>"+nomor+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("NAMA")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("MSAG_ID")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("POSISI")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("TELP")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("EMAIL")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("KETERANGAN")+"</td>");
				mssg.append("</tr>");
			}
			mssg.append("</table>");
			mssg.append("\nPengirim: "+currentUser.getLus_full_name());
			mssg.append("\nNB: Email ini dikirim secara otomatis oleh sistem E-Lions. Harap tidak mereply email ini.");
			
			
			sukses = "permintaan kartu nama telah dikirim ke bagian Agency Distribution";
		}else if(kn.getSubmitMode().equalsIgnoreCase("reject")){
			kn.setMkn_position(5);
			kn.setMkn_approve(2);
			send_mail = 1;
			sukses = "permintaan kartu nama telah ditolak";
			
			//email ke cabang
			to = new String[]{userCabang.getEmail()};
			cc = new String[]{"tri.handini@sinarmasmsiglife.co.id", "bas@sinarmasmsiglife.co.id"};
			if(userCabang.getEmail()==null){
				to = new String[]{"desy@sinarmasmsiglife.co.id"};
				//bcc = new String[]{""};
			}
			
			List dataKartuNama = query("selectDataKartuNama", cmd.getMkn_id());
			
			subject="KONFIRMASI PENOLAKAN KARTU NAMA DENGAN NOMOR "+cmd.getMkn_id();
			
			mssg.append("Permintaan Kartu Nama dengan no. "+cmd.getMkn_id()+" ditolak karena "+cmd.getKeterangan());
			mssg.append("\n\n<table width='100%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
			mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>No.</strong></td><td align='center'><strong>Nama</strong></td>" +
					"<td align='center'><strong>Kode Agen</strong></td><td align='center'><strong>Posisi</strong></td><td align='center'><strong>Sertifikat</strong></td>" +
					"<td align='center'><strong>Masa Berlaku</strong></td><td align='center'><strong>Total Permintaan</strong></td></tr>");
			for(int i=0; i<dataKartuNama.size() ; i++){
				Map s = (Map) dataKartuNama.get(i);
				Integer nomor = i+1;
				mssg.append("<tr>");
				mssg.append("<td align='center'>"+nomor+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("NAMA")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("MSAG_ID")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("POSISI")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("SERTIFIKAT")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("LISENSI")+"</td>");
				mssg.append("<td align='center'>"+((BigDecimal)s.get("TOTAL_PERMINTAAN")).intValue()+"</td>");
				mssg.append("</tr>");
			}
			mssg.append("</table>");
			mssg.append("\nPengirim: "+currentUser.getLus_full_name());
			mssg.append("\nNB: Email ini dikirim secara otomatis oleh sistem E-Lions. Harap tidak mereply email ini.");
			
		}else if(kn.getSubmitMode().equalsIgnoreCase("send_purchasing")){
			kn.setMkn_position(4);
			kn.setMkn_approve(1);
			send_mail = 1;
			sukses = "permintaan kartu nama telah dikirim ke GA";
			
			//email ke cabang
			to = new String[]{"Ferdiansyah@sinarmasmsiglife.co.id"};
			cc = new String[]{"tri.handini@sinarmasmsiglife.co.id", "BAS@sinarmasmsiglife.co.id", userCabang.getEmail()};
			/*if(userCabang.getEmail()==null){
				to = new String[]{""};
				bcc = new String[]{""};
			}*/
			
			List dataKartuNama = query("selectDataKartuNama", cmd.getMkn_id());
			
			subject="KONFIRMASI KARTU NAMA DENGAN NOMOR "+cmd.getMkn_id();

			mssg.append("Permintaan Kartu Nama dengan no. "+cmd.getMkn_id()+". Telah selesai di cetak atas nama berikut :");
			mssg.append("\n\n<table width='100%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
			mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>No.</strong></td><td align='center'><strong>Nama</strong></td>" +
					"<td align='center'><strong>Kode Agen</strong></td><td align='center'><strong>Posisi</strong></td><td align='center'><strong>No. Telp</strong></td>" +
					"<td align='center'><strong>Email</strong></td><td align='center'><strong>Keterangan</strong></td></tr>");
			for(int i=0; i<dataKartuNama.size() ; i++){
				Map s = (Map) dataKartuNama.get(i);
				Integer nomor = i+1;
				mssg.append("<tr>");
				mssg.append("<td align='center'>"+nomor+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("NAMA")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("MSAG_ID")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("POSISI")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("TELP")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("EMAIL")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("KETERANGAN")+"</td>");
				mssg.append("</tr>");
			}
			mssg.append("</table>");
			mssg.append("\nPengirim: "+currentUser.getLus_full_name());
			mssg.append("\nNB: Email ini dikirim secara otomatis oleh sistem E-Lions. Harap tidak mereply email ini.");
			
		}else if(kn.getSubmitMode().equalsIgnoreCase("send_ga")){
			kn.setMkn_position(5);
			kn.setMkn_approve(1);
			send_mail = 1;
			sukses = "permintaan kartu nama telah dikirim";
			
			//email ke cabang
			to = new String[]{userCabang.getEmail()};
			cc = new String[]{"febip@sinarmasmsiglife.co.id", "BAS@sinarmasmsiglife.co.id"};
			if(userCabang.getEmail()==null){
				to = new String[]{"desy@sinarmasmsiglife.co.id"};
				//bcc = new String[]{""};
			}
			
			List dataKartuNama = query("selectDataKartuNama", cmd.getMkn_id());
			
			subject="KONFIRMASI PENGIRIMAN KARTU NAMA DENGAN NOMOR "+cmd.getMkn_id();

			mssg.append("Permintaan Kartu Nama dengan no. "+cmd.getMkn_id()+" akan segera dikirim atas nama berikut :");
			mssg.append("\n\n<table width='100%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
			mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>No.</strong></td><td align='center'><strong>Nama</strong></td>" +
					"<td align='center'><strong>Kode Agen</strong></td><td align='center'><strong>Posisi</strong></td><td align='center'><strong>No. Telp</strong></td>" +
					"<td align='center'><strong>Email</strong></td><td align='center'><strong>Keterangan</strong></td></tr>");
			for(int i=0; i<dataKartuNama.size() ; i++){
				Map s = (Map) dataKartuNama.get(i);
				Integer nomor = i+1;
				mssg.append("<tr>");
				mssg.append("<td align='center'>"+nomor+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("NAMA")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("MSAG_ID")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("POSISI")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("TELP")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("EMAIL")+"</td>");
				mssg.append("<td align='center'>"+(String)s.get("KETERANGAN")+"</td>");
				mssg.append("</tr>");
			}
			mssg.append("</table>");
			mssg.append("\nPengirim: "+currentUser.getLus_full_name());
			mssg.append("\nNB: Email ini dikirim secara otomatis oleh sistem E-Lions. Harap tidak mereply email ini.");
		}
		
		insert("updateKartuNama", kn);
		
		/** Insert history*/
		FormHist form = new FormHist();
		form.setMsf_id(kn.getMkn_id());
		form.setMsf_urut(selectNoUrutFormHistory(kn.getMkn_id()));
		form.setPosisi(kn.getMkn_position());
		form.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		form.setMsfh_desc(kn.getKeterangan());			
		insert("insertFormHistory", form);
		
		if(send_mail>0){
			if(userCabang.getEmail()!=null){
				try {
//					email.send(true, props.getProperty("admin.ajsjava"), to, cc, null, subject, mssg.toString(), null);
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
							props.getProperty("admin.ajsjava"), to, cc, null, 
							subject, mssg.toString(), null, null);
				} catch (MailException e) {
					logger.error("ERROR :", e);
				}
			}
		}
		
		return sukses;
	}

	public List<KartuNama> selectHistoryKartuNama(KartuNama cmd)throws DataAccessException {
		return query("selectHistoryKartuNama", cmd);
	}

	public List selectEmailCabangLisensiAgent()throws DataAccessException {
		return query("selectEmailCabangLisensiAgent", null);
	}
	
	public List selectAddressRegionLcaId(String lca)throws DataAccessException {
		Map param = new HashMap();
		param.put("lca", lca);
		return query("selectAddressRegionLcaId", param);
	}

	public List selectReportInputHarian(String bdate, String edate, String lsbs_id)throws DataAccessException  {
		Map param = new HashMap();
		param.put("bdate", bdate);
		param.put("edate", edate);
		param.put("lsbs_id", lsbs_id);
		
		if(lsbs_id.equalsIgnoreCase("simpol"))return query("selectReportInputHarianNew", param);
		else if(lsbs_id.equalsIgnoreCase("agency"))return query("selectReportInputHarianNew", param);
		else return query("selectReportInputHarian", param);
	}
	
	public List monitorPolisIssued(String dist, String lsgb,String provider,Date tanggalAwal,Date tanggalAkhir){
		Map map=new HashMap();
		map.put("dist", dist);
		map.put("lsgb", lsgb);
		map.put("provider", provider);
		map.put("tanggalAwal", tanggalAwal);
		map.put("tanggalAkhir", tanggalAkhir);
		return query("monitorPolisIssued",map);
	}

	public List selectReturKpl(int jenis, String lca) {
		if(jenis==1)return query("selectCabangReturKpl", null);
		else return query("selectReturKpl", lca);
	}

	public List selectReportGagalDebetdanAging(String bdate, String edate, String lca_id, String jenis) {
		Map map=new HashMap();
		map.put("bdate", bdate);
		map.put("edate", edate);
		map.put("lca_id", lca_id);
		
		if(jenis.equals("1"))return query("selectReportGagalDebet", map);
		else return query("selectReportAging", map);
	}
	
	/**
	 * Proses untuk insert permintaan Promo Item
	 * 
	 * @author Canpri
	 * @since Apr 10, 2014 (8:32:17 AM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 * @throws IOException 
	 */
	public String processNewFormPromo(CommandControlSpaj cmd, User currentUser) throws DataAccessException, IOException {
		FormSpaj f = cmd.getDaftarFormBrosur().get(0);
		String pesan = "";
		if("".equals(f.getMsf_id())) { //INSERT BARU
			
			/** 1. Insert stok brosur di cabang tersebut walaupun masih 0, dan insert form brosur(query sama dengan permintaan spaj) */
			String seq = sequence.sequenceMst_form(0, currentUser.getLca_id());
			for(FormSpaj s : cmd.getDaftarFormBrosur()) {
				s.setPosisi(4);
				s.setBusdev(cmd.getBusdev());
				s.setMsf_id(seq);
				s.setMss_jenis(3);
				s.setLca_id(currentUser.getLca_id());
				s.setMsab_id(0); //AGENT 000000
				s.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				//no_blanko tidak boleh diisi saat baru minta
				s.setStart_no_blanko(null);
				s.setEnd_no_blanko(null);
				if(cmd.getBmi_id() != null)
					s.setBmi_id(cmd.getBmi_id());
				if(cmd.getAdmTravIns() != null) 
					s.setTrav_ins_type(cmd.getAdmTravIns());
				if(((Integer) querySingle("selectCekSpaj", s))==0) {
					Spaj spaj = new Spaj();
					spaj.newSpaj(3, s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), 0, s.getLus_id(),s.getLus_id());
					insert("insertNewSpaj", spaj);
				}
				insert("insertNewFormSpaj", s);
			}
			cmd.setMsf_id(seq);

			/** 2. Insert history form brosur */
			FormHist form = new FormHist();
			form.setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
			form.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			form.setPosisi(4);
			form.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			form.setMsfh_desc("Permintaan oleh Branch Admin");			
			insert("insertFormHistory", form);
			
			pesan = "Permintaan anda sudah tersimpan dengan nomor " + cmd.getMsf_id();
		} else { //UPDATE DATA LAMA
			
			/** 1. Update jumlah pada form brosur(query sama dengan permintaan spaj) */
			for(FormSpaj s : cmd.getDaftarFormBrosur()) {
				update("updateFormSpaj", s);
			}
			
			/** 2. Insert history form */
			FormHist form = new FormHist();
			form.setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
			form.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			form.setPosisi(4);
			form.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			form.setMsfh_desc("Perubahan Permintaan oleh Branch Admin");			
			insert("insertFormHistory", form);
			
			pesan = "Data Permintaan anda sudah di-update.";
		}
		
		//upload attachment
		//destiny upload file
		List<File> attachments = new ArrayList<File>();
		String tDest = props.getProperty("pdf.dir.upload.kpl.promo")+"\\Promo\\"+cmd.getMsf_id()+"\\";
		File destDir = new File(tDest);
		
		if(!destDir.exists()) destDir.mkdirs();
		
		String filename = cmd.getFile1().getOriginalFilename();
		String dest = tDest +"\\"+filename;
		File outputFile = new File(dest);
		FileCopyUtils.copy(cmd.getFile1().getBytes(), outputFile);
		
		attachments.add(outputFile);
		
		//email ke cabang
		User userCabang=selectLstUser(cmd.getDaftarFormBrosur().get(0).getLus_id());
		String to[]={userCabang.getEmail(),"pratidina@sinarmasmsiglife.co.id"};
		String cc[] = new String[]{""};
		String sukses="";
		if(userCabang.getEmail()==null){
			to = new String[]{"pratidina@sinarmasmsiglife.co.id"};
			cc = new String[]{""};
		}
		
		Map tgl = (Map) querySingle("selectTglFormHist", cmd.getDaftarFormBrosur().get(0).getMsf_id());
		
		String subject="KONFIRMASI PERMINTAAN PROMO ITEM DENGAN NOMOR "+cmd.getDaftarFormBrosur().get(0).getMsf_id();
		StringBuffer mssg = new StringBuffer();
		mssg.append("Admin\t\t : "+userCabang.getLus_full_name());
		mssg.append("\nCabang\t\t : "+userCabang.getCabang());
		mssg.append("\nNo. Permintaan\t : "+cmd.getDaftarFormBrosur().get(0).getMsf_id());
		mssg.append("\nTgl. Permintaan\t : "+(String)tgl.get("TGL_MINTA"));
		mssg.append("\n\n<table width='50%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
		mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>Jenis Permintaan</strong></td><td align='center'><strong>Jumlah</strong></td></tr>");
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++){
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);
			if(s.getMsf_amount_req()>0){
				mssg.append("<tr>");
				mssg.append("<td align='center'>"+s.getLsjs_desc()+"</td>");
				mssg.append("<td align='center'>"+s.getMsf_amount_req()+"</td>");
				mssg.append("</tr>");
			}
		}
		mssg.append("</table>");
		
		try {
//			email.send(true, props.getProperty("admin.ajsjava"), to, cc, null, subject, mssg.toString(), attachments);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), to, cc, null, 
					subject, mssg.toString(), null, null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		}
		
		return pesan;
	}
	
	/**
	 * Proses untuk pembatalan permohonan permintaan Promo item
	 * 
	 * @author Canpri
	 * @since Apr 10, 2014 (8:32:17 AM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processCancelFormPromo(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		
		/** 1. update form brosur(query sama dengan permintaan spaj) */
			for(FormSpaj s : cmd.getDaftarFormBrosur()) {
				s.setMsf_amount(0);
				s.setPosisi(2); //cancel
				update("updateFormSpaj", s);
			}
		
		/** 2. insert history form */
		if(!cmd.getDaftarFormBrosur().isEmpty()) {
			FormHist f = new FormHist();
			f.setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
			f.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			f.setPosisi(2);
			f.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			f.setMsfh_desc("Dibatalkan oleh Branch Admin");			
			insert("insertFormHistory", f);
		}
		
		return "Data Permintaan anda sudah dibatalkan.";
	}
	
	/**
	 * Proses yang menandakan bahwa permohonan pengiriman promo item dari pusat ke cabang
	 * 
	 * @author Canpri
	 * @since Apr 11, 2014 (10:28:42 AM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 * @throws MessagingException 
	 * @throws MailException 
	 */
	public String processSendFormPromo(CommandControlSpaj cmd, User currentUser) throws DataAccessException{
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);

			s.setPosisi(4); //sent
						
			/** 1. update form brosur(query sama dengan permintaan spaj) */
			update("updateFormSpaj", s);
		}

		/** 2. insert form history */
		cmd.getFormHist().setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
		cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getFormHist().getMsf_id()));
		cmd.getFormHist().setPosisi(4);
		cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		insert("insertFormHistory", cmd.getFormHist());
		
		//email ke cabang
		User userCabang=selectLstUser(cmd.getDaftarFormBrosur().get(0).getLus_id());
		String to[]={userCabang.getEmail()};
		String cc[] = new String[]{"pratidina@sinarmasmsiglife.co.id"};
		String sukses="";
		if(userCabang.getEmail()==null){
			to = new String[]{"pratidina@sinarmasmsiglife.co.id"};
			cc = new String[]{""};
		}
		
		Map tgl = (Map) querySingle("selectTglFormHist", cmd.getDaftarFormBrosur().get(0).getMsf_id());
		
		String subject="KONFIRMASI PENGIRIMAN PERMINTAAN PROMO ITEM DENGAN NOMOR "+cmd.getDaftarFormBrosur().get(0).getMsf_id();
		StringBuffer mssg = new StringBuffer();
		mssg.append("Admin\t\t : "+userCabang.getLus_full_name());
		mssg.append("\nCabang\t\t : "+userCabang.getCabang());
		mssg.append("\nNo. Permintaan\t : "+cmd.getDaftarFormBrosur().get(0).getMsf_id());
		mssg.append("\nTgl. Permintaan\t : "+(String)tgl.get("TGL_MINTA"));
		mssg.append("\nTgl. Pengiriman\t : "+(String)tgl.get("TGL_KIRIM"));
		mssg.append("\n\n<table width='50%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
		mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>Jenis Permintaan</strong></td><td align='center'><strong>Jumlah</strong></td></tr>");
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++){
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);
			if(s.getMsf_amount_req()>0){
				mssg.append("<tr>");
				mssg.append("<td align='center'>"+s.getLsjs_desc()+"</td>");
				mssg.append("<td align='center'>"+s.getMsf_amount()+"</td>");
				mssg.append("</tr>");
			}
		}
		mssg.append("</table>");
		mssg.append("\n\nHarap update tombol telah diterima jika sudah menerima item tersebut.");
		
		try {
//			email.send(true, props.getProperty("admin.ajsjava"), to, cc, null, subject, mssg.toString(), null);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), to, cc, null, 
					subject, mssg.toString(), null, null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		}
		
		return 
			sukses+"Data pengiriman Promo Item sudah diupdate.";
	}

	/**
	 * Proses untuk penolakan permohonan pengiriman promo dari pusat ke cabang,
	 * 
	 * @author Canpri
	 * @since Apr 11, 2014 (9:09:42 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processRejectFormPromo(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);
			s.setPosisi(3); //reject
			
			/** 1. update form brosur(query sama dengan permintaan spaj) */
			update("updateFormSpaj", s);
		}

		/** 2. insert form history */
		cmd.getFormHist().setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
		cmd.getFormHist().setMsf_urut(selectNoUrutFormHistory(cmd.getFormHist().getMsf_id()));
		cmd.getFormHist().setPosisi(3);
		cmd.getFormHist().setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
		insert("insertFormHistory", cmd.getFormHist());
		
		//email ke cabang
		User userCabang=selectLstUser(cmd.getDaftarFormBrosur().get(0).getLus_id());
		String to[]={userCabang.getEmail()};
		String cc[] = new String[]{"pratidina@sinarmasmsiglife.co.id"};
		String sukses="";
		if(userCabang.getEmail()==null){
			to = new String[]{"pratidina@sinarmasmsiglife.co.id"};
			cc = new String[]{""};
		}
		
		Map tgl = (Map) querySingle("selectTglFormHist", cmd.getDaftarFormBrosur().get(0).getMsf_id());
		
		String subject="KONFIRMASI PENOLAKAN PERMINTAAN PROMO ITEM DENGAN NOMOR "+cmd.getDaftarFormBrosur().get(0).getMsf_id();
		StringBuffer mssg = new StringBuffer();
		mssg.append("Admin\t\t : "+userCabang.getLus_full_name());
		mssg.append("\nCabang\t\t : "+userCabang.getCabang());
		mssg.append("\nNo. Permintaan\t : "+cmd.getDaftarFormBrosur().get(0).getMsf_id());
		mssg.append("\nTgl. Permintaan\t : "+(String)tgl.get("TGL_MINTA"));
		mssg.append("\nTgl. Penolakan\t : "+(String)tgl.get("TGL_REJECT"));
		mssg.append("\n\n<table width='50%' border='1' cellpadding='0' cellspacing='0' style='font-size:12px;'>");
		mssg.append("<tr bgcolor='#FFFF00'><td align='center'><strong>Jenis Permintaan</strong></td><td align='center'><strong>Jumlah</strong></td></tr>");
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++){
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);
			if(s.getMsf_amount_req()>0){
				mssg.append("<tr>");
				mssg.append("<td align='center'>"+s.getLsjs_desc()+"</td>");
				mssg.append("<td align='center'>"+s.getMsf_amount()+"</td>");
				mssg.append("</tr>");
			}
		}
		mssg.append("</table>");
		
		try {
//			email.send(true, props.getProperty("admin.ajsjava"), to, cc, null, subject, mssg.toString(), null);
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, commonDao.selectSysdate(), null, true, 
					props.getProperty("admin.ajsjava"), to, cc, null, 
					subject, mssg.toString(), null, null);
		} catch (MailException e) {
			logger.error("ERROR :", e);
		}

		return 
			"Data permintaan cabang sudah ditolak. "
			+ ". \\nAlasan penolakan akan diteruskan ke cabang bersangkutan.";
	}
	
	/**
	 * Proses yang menandakan bahwa promo hasil permintaan dari cabang ke pusat sudah dikirimkan
	 * ke cabang dan sudah diterima oleh cabang
	 * 
	 * @author Canpri
	 * @since Apr 11, 2014 (1:26:05 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public String processReceiveFormPromo(CommandControlSpaj cmd, User currentUser) throws DataAccessException {
		for(int i=0; i<cmd.getDaftarFormBrosur().size(); i++) {
			FormSpaj s = cmd.getDaftarFormBrosur().get(i);
			s.setPosisi(5); //received
			
			/** 1. update form brosur dan stok brosur(query sama dengan permintaan spaj) */
			update("updateFormSpaj", s);
			Spaj spaj = new Spaj();
			spaj.newSpaj(s.getMss_jenis(), s.getLca_id(), s.getLsjs_id(), s.getMsab_id(), s.getMsf_amount(), Integer.valueOf(currentUser.getLus_id()),Integer.valueOf(currentUser.getLus_id()));
			update("updateStokSpaj", spaj);
			
		}

		/** 2. insert form history */
		if(!cmd.getDaftarFormBrosur().isEmpty()) {
			FormHist f = new FormHist();
			f.setMsf_id(cmd.getDaftarFormBrosur().get(0).getMsf_id());
			f.setMsf_urut(selectNoUrutFormHistory(f.getMsf_id()));
			f.setPosisi(5);
			f.setMsfh_lus_id(Integer.valueOf(currentUser.getLus_id()));
			f.setMsfh_desc("Sudah diterima oleh Branch Admin");			
			insert("insertFormHistory", f);
		}

		return "Data penerimaan Promo Item sudah diupdate. Jumlah Promo Item di sistem sudah ditambahkan.";
	}

	public String updateKantorPemasaran(AddressRegion cmd, User currentUser) {
		cmd.setLus_id(Integer.parseInt(currentUser.getLus_id()));
		//update eka.lst_addr_region
		
		//insert kea.lst_addr_region_hist
		String update = "Update : ";
		if(!cmd.getLar_alamat().equals("") && cmd.getLar_alamat()!=null)update+="alamat, ";
		if(!cmd.getLar_luas().equals("") && cmd.getLar_luas()!=null)update+="luas, ";
		//if(!cmd.getLar_admin().equals("") && cmd.getLar_admin()!=null)update+="admin, ";
		if(!cmd.getLar_telpon().equals("") && cmd.getLar_telpon()!=null)update+="telpon, ";
		if(!cmd.getLar_fax().equals("") && cmd.getLar_fax()!=null)update+="fax, ";
		if(!cmd.getLar_status_gedung().equals("") && cmd.getLar_status_gedung()!=null)update+="status gedung, ";
		if(cmd.getLar_beg_date_sewa()!=null)update+="tgl mulai sewa, ";
		if(cmd.getLar_end_date_sewa()!=null)update+="tgl akhir sewa, ";
		if(!cmd.getLar_no().equals("") && cmd.getLar_no()!=null)update+="no surat, ";
		if(cmd.getLar_beg_date_domisili()!=null)update+="tgl mulai domisili, ";
		if(cmd.getLar_end_date_domisili()!=null)update+="tgl akhir domisili, ";
		//if(cmd.getLar_non_active_date()!=null)update+="tgl non active kantor pemasaran, ";
		if(cmd.getFile1()!=null)if(!cmd.getFile1().isEmpty())update+="file domisili, ";
		if(cmd.getLar_end_date_reklame()!=null)update+="tgl jatuh tempo reklame, ";
		if(cmd.getFile2()!=null)if(!cmd.getFile2().isEmpty())update+="foto neonbox/billboard, ";
		if(cmd.getFile3()!=null)if(!cmd.getFile3().isEmpty())update+="foto backdrop, ";
		if(cmd.getFile4()!=null)if(!cmd.getFile4().isEmpty())update+="list asset ";
		
		if (update.length() > 0 && update.charAt(update.length()-2)==',') {
			update = update.substring(0, update.length()-2);
		}
		
		cmd.setKeterangan(update);
		
		//upload attachment
		if(cmd.getFile1()!=null){
			if(cmd.getFile1().isEmpty()==false){
				//destiny upload file
				String tDest = props.getProperty("pdf.dir.surat_domisili")+"\\"+cmd.getLar_id()+"\\";
				File destDir = new File(tDest);
				
				if(!destDir.exists()) destDir.mkdirs();
				
				String filename = "surat_domisili.pdf";
				String dest = tDest +"\\"+filename;
				File outputFile = new File(dest);
				try {
					FileCopyUtils.copy(cmd.getFile1().getBytes(), outputFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e);
				}
			}
		}
		
		//upload foto neonbox
		if(cmd.getFile2()!=null){
			if(cmd.getFile2().isEmpty()==false){
				//destiny upload file
				String tDest = props.getProperty("pdf.dir.surat_domisili")+"\\"+cmd.getLar_id()+"\\";
				File destDir = new File(tDest);
				
				if(!destDir.exists()) destDir.mkdirs();
				
				String filename = "foto_neonbox.pdf";
				String dest = tDest +"\\"+filename;
				File outputFile = new File(dest);
				try {
					FileCopyUtils.copy(cmd.getFile2().getBytes(), outputFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e);
				}
			}
		}
				
		//upload foto backdrop
		if(cmd.getFile3()!=null){
			if(cmd.getFile3().isEmpty()==false){
				//destiny upload file
				String tDest = props.getProperty("pdf.dir.surat_domisili")+"\\"+cmd.getLar_id()+"\\";
				File destDir = new File(tDest);
				
				if(!destDir.exists()) destDir.mkdirs();
				
				String filename = "foto_backdrop.pdf";
				String dest = tDest +"\\"+filename;
				File outputFile = new File(dest);
				try {
					FileCopyUtils.copy(cmd.getFile3().getBytes(), outputFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e);
				}
			}
		}
				
		//upload attachment
		if(cmd.getFile4()!=null){
			if(cmd.getFile4().isEmpty()==false){
				//destiny upload file
				String tDest = props.getProperty("pdf.dir.surat_domisili")+"\\"+cmd.getLar_id()+"\\";
				File destDir = new File(tDest);
				
				if(!destDir.exists()) destDir.mkdirs();
				
				String filename = "list_asset.pdf";
				String dest = tDest +"\\"+filename;
				File outputFile = new File(dest);
				try {
					FileCopyUtils.copy(cmd.getFile4().getBytes(), outputFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("ERROR :", e);
				}
			}
		}
		
		update("updateAddressRegion", cmd);
		insertLstAddrRegionHist(cmd.getLar_id().toString(), cmd.getLus_id().toString(), cmd.getKeterangan());
		
		StringBuffer message = new StringBuffer();
		message.append("Dear All,");
		message.append("\n\nAdmin "+cmd.getLar_admin().trim()+" telah mengupdate informasi surat domisili dan status gedung.");
		message.append("\n\nTerima kasih");
		
		EmailPool.send("E-Lions", 1, 1, 0, 0, null, 0, 0, new Date(), null, true,
				props.getProperty("admin.ajsjava"),	new String[]{"rismawati@sinarmasmsiglife.co.id","bas@sinarmasmsiglife.co.id"}, null, null, 
				"Update Surat Domisili dan Status Gedung", message.toString(), null, null);
		
		return "Data berhasil diupdate.";
	}
	
	public List selectLstAddrRegion(String nm_kota)throws DataAccessException {
		Map param = new HashMap();
		param.put("nm_kota", nm_kota);
		return query("selectLstAddrRegion", param);
	}
	
	public void updateLstAddrRegion(String lar_id, String nama, String alamat, String admin,
			String telp, String email, String no, String aktif, String lar_speedy) throws DataAccessException{
		Map param = new HashMap();
		
		param.put("lar_id", lar_id);
		param.put("nama", nama);
		param.put("alamat", alamat);
		param.put("admin", admin);
		param.put("telp", telp);
		param.put("email", email);
		param.put("no", no);
		param.put("aktif", aktif);
		param.put("lar_speedy", lar_speedy);
		
		update("updateLstAddrRegion", param);
	}
	
	public void insertLstAddrRegionHist(String lar_id, String lus_id, String keterangan) throws DataAccessException{
		Map param = new HashMap();
		param.put("lar_id", lar_id);
		param.put("lus_id", lus_id);
		param.put("keterangan", keterangan);
		
		insert("insertLstAddrRegionHist", param);
	}
	
	public void insertLstAutobetSpeedyHist(String lar_id, String lar_telpon, String lar_speedy, String tgl_autodebet, String status, String keterangan) throws DataAccessException{
		Map param = new HashMap();
		param.put("lar_id", lar_id);
		param.put("lar_telpon", lar_telpon);
		param.put("lar_speedy", lar_speedy);
		param.put("tgl_autodebet", tgl_autodebet);
		param.put("status", status);
		param.put("keterangan", keterangan);
		
		insert("insertLstAutobetSpeedyHist", param);
	}

	public List selectAutodebetSpeedy(String tglAwal, String tglAkhir, String status)throws DataAccessException {
		Map param = new HashMap();
		param.put("tglAwal", tglAwal);
		param.put("tglAkhir", tglAkhir);
		param.put("status", status);
		return query("selectAutodebetSpeedy", param);
	}
	
	public String insertMonitoringSpaj(String no_blanko, String msag_id, String pemegang, String informasi, String fdm, User currentUser, Integer jenis, String note, String jenis_further, 
			Date tgl_kembali_ke_agen, Date tgl_terima_agen, String keterangan_further, Date tgl_further, Integer flag_further, String emailcc) throws Exception {
		String pesan = "";
		
		HashMap blanko = (HashMap) selectBlankoMonitoringSpaj(no_blanko, null);		
		
		if(blanko!=null){
			pesan = "No blanko sudah ada";
		}
		else{
			Map param = new HashMap();
			param.put("no_blanko", no_blanko);
			param.put("msag_id", msag_id);
			param.put("holder_name", pemegang);
			param.put("informasi", informasi);
			param.put("fdm", fdm);
			param.put("lus_id", currentUser.getLus_id());
			param.put("jenis", jenis);
			param.put("note", note);
			param.put("jenis_further", jenis_further);
			param.put("tgl_kembali_ke_agen", tgl_kembali_ke_agen);
			param.put("tgl_terima_agen", tgl_terima_agen);	
			param.put("keterangan_further", keterangan_further);
			param.put("tgl_further", tgl_further);
			param.put("flag_further", flag_further);	
			param.put("email_cc", emailcc);
					
			if(tgl_further != null){
				try{
					HashMap agent = (HashMap) selectMstAgent(null, msag_id, 1);
					if(agent==null){
						pesan = "Kode Agen Tidak Terdaftar";
					}
					else{
						insert("insertMonitoringSpaj", param);						
						pesan = "Data Berhasil di Input";	
						
						Map agen = uwDao.selectEmailAgen2(msag_id);
						String email_agen = (String) agen.get("MSPE_EMAIL");
						String email_user = commonDao.selectEmailUser(currentUser.getLus_id());
						if(email_agen == null) email_agen = "";
						if(email_user == null) email_user = "";
						String emailto = email_agen+";"+email_user;
						String[] to = emailto.split(";");
						String[] cc = emailcc.split(";");
						
						HashMap jd = (HashMap) querySingle("selectJalurDistByMsagId", msag_id);
						
						StringBuffer mssg = new StringBuffer();
						mssg.append("Tgl dikembalikan berkas\t : "+defaultDateFormat.format(tgl_kembali_ke_agen));
						mssg.append("\njalur distribusi\t\t : "+(String)jd.get("GRUP_REPORT"));
						mssg.append("\nAgen\t\t\t\t : "+(String)jd.get("MCL_FIRST"));
						mssg.append("\nKode Agen\t\t\t : "+msag_id);
						mssg.append("\nNo blanko\t\t\t : "+no_blanko);
						mssg.append("\nPemegang polis\t\t\t : "+pemegang);
						mssg.append("\nJenis further\t\t\t : "+jenis_further);
						mssg.append("\nKeterangan further\t\t : "+keterangan_further);
						mssg.append("\nUser BAS\t\t\t : "+currentUser.getLus_full_name());
						
						EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
								null, 0, 0, new Date(), null, 
								true, "ajsjava@sinarmasmsiglife.co.id", 
								to, 
								cc,
								new String[] {"ridhaal@sinarmasmsiglife.co.id"}, 
								"FURTHER BAS "+no_blanko,
								mssg.toString(),null, null);
						
						//email.send(true, "ajsjava@sinarmasmsiglife.co.id", new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null, "FURTHER BAS "+no_blanko , mssg.toString(), null);
						
					}					
				}catch (Exception e) {
					logger.error("ERROR :", e);
					throw e;
				}
				
			}
			else{
				insert("insertMonitoringSpaj", param);						
				pesan = "Data Berhasil di Input";	
			}			
		}
		
		return pesan;
	}
	
	public void updateMonitoringSpaj(String no_blanko, Integer jenis, String type, Date tgl_kembali_ke_agen, Date tgl_terima_dari_agen, String jenis_further, String keterangan_further,String msag_id,String nama_pemegang, String emailcc) {
		Map param = new HashMap();
		param.put("value", no_blanko);
		param.put("jenis", jenis);		
		param.put("type", type);
		param.put("tgl_kembali_ke_agen", tgl_kembali_ke_agen);
		param.put("tgl_terima_dari_agen", tgl_terima_dari_agen);
		param.put("jenis_further", jenis_further);
		param.put("keterangan_further", keterangan_further);
		
		param.put("msag_id", msag_id);
		param.put("nama_pemegang", nama_pemegang);
		param.put("emailcc", emailcc);
		
		
		update("updateMonitoringSpaj", param);
	}

	public List selectReportMonitoringSPAJ(String bdate, String edate, String lus_id, Integer jn_report, Integer admin,  String searchList, String searchText) throws DataAccessException {
		//jn_report = 1, berdasarkan tanggal, 0 = data yang belum diinput(belum jadi spaj)
		
		Map param = new HashMap();
		param.put("bdate", bdate);
		param.put("edate", edate);
		param.put("lus_id", lus_id);
		param.put("jn_report", jn_report);
		param.put("admin", admin);
		param.put("searchList", searchList);
		param.put("searchText", searchText);
		return query("selectReportMonitoringSPAJ", param);
	}
	
	public List selectReportMonitoringSPAJFurther(String bdate, String edate, String lus_id, Integer admin, Integer jn_report) throws DataAccessException {
		//jn_report = 1, berdasarkan tanggal, 0 = data yang belum diinput(belum jadi spaj)
		
		Map param = new HashMap();
		param.put("bdate", bdate);
		param.put("edate", edate);
		param.put("lus_id", lus_id);
		param.put("admin", admin);
		param.put("jn_report", jn_report);
		return query("selectReportMonitoringSPAJFurther", param);
	}

	public List<Map> selectHistoryUpdateRegion(String lar_id, String order) {
		Map m = new HashMap();
		m.put("lar_id", lar_id);
		m.put("order", order);
		return query("selectHistoryUpdateRegion", m);
	}

	public List selectDataListPendingBas(String cabang, String bdate,
			String edate) {
		Map param = new HashMap();
		param.put("cabang", cabang);
		param.put("bdate", bdate);
		param.put("edate", edate);
		return  query("selectDataListPendingBas", param);		
	}

	public List selectDataPersentasePendingBas(String cabang, String bdate,
			String edate) {
		Map param = new HashMap();
		param.put("cabang", cabang);
		param.put("bdate", bdate);
		param.put("edate", edate);
		return  query("selectDataPersentasePendingBas", param);	
	}

	public Integer selectStatusSpajBas(String spaj) {		
		return (Integer) querySingle("selectStatusSpajBas", spaj);
	}

	public List selectFollowupKategori(String s_bdate, String s_edate, String s_jenis, String s_jn_tgl) {
		Map param = new HashMap();
		param.put("bdate", s_bdate);
		param.put("edate", s_edate);
		param.put("jenis", s_jenis);
		param.put("jn_tgl", s_jn_tgl);
		return  query("selectFollowupKategori", param);	
	}
	
	/**
	 * Proses untuk insert permohonan pengiriman spaj dari pusat ke cabang
	 * 
	 * @author Rahmayanti
	 * @since Feb 23, 2007 (4:56:17 PM)
	 * @param cmd
	 * @param currentUser
	 * @return
	 */
	public List reportFollowupTempo(String bdate, String edate, String jenis, String jn_tgl)
	{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("jenis", jenis);
		m.put("jn_tgl", jn_tgl);
		return query("selectreportFollowupTempo",m);
	}
	
	public List reportSimasSpt(String bdate, String edate)
	{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		return query("selectreportSimasSpt",m);
	}	
	
	public List reportSimasNonSpt(String bdate, String edate)
	{
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		return query("selectreportSimasNonSpt",m);
	}	


	public List selectKomisiPendingAgent(String s_msag_id, String s_polis) {
		Map m = new HashMap();
		m.put("msag_id", s_msag_id);
		m.put("polis", s_polis);
		return query("selectKomisiPendingAgent",m);
	}

	public String getUrlDatabase(String url) {
		Connection con=getConnection();
		DatabaseMetaData dmd ;
		try {
			dmd = con.getMetaData();
			url=dmd.getURL();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR :", e);
		}
	    
		return url;
	}
	
	public List selectDataSmilePrioritas(String bdate, String edate,
			String jn_report, int flag) {
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		m.put("jn_report", jn_report);
		m.put("flag", flag);
		return query("selectDataSmilePrioritas",m);
	}
	
	public List reportError(String bdate, String edate){
		Map m = new HashMap();
		m.put("bdate", bdate);
		m.put("edate", edate);
		return query("selectReportError",m);
	}	
	
	public Map selectBlankoMonitoringSpaj(String no_blanko, String type){
		Map m = new HashMap();
		m.put("value", no_blanko);
		m.put("type", type);
		return (HashMap) querySingle("selectBlankoMonitoringSpaj", m);
	}
	
	public Map selectMstAgent(String no_blanko, String msag_id, Integer flag){
		Map m = new HashMap();
		m.put("no_blanko", no_blanko);
		m.put("msag_id", msag_id);
		m.put("flag", flag);
		return (HashMap) querySingle("selectMstAgent", m);
	}
	
	public List selectLstDetBisnisPAS() throws DataAccessException {
		return query("selectLstDetBisnisPAS", null);
	}

	public void insertAgingFollowup(String polis, String pemegang,
			String beg_date, String end_date) {
		Map m = new HashMap();
		m.put("polis", polis);
		m.put("pemegang", pemegang);
		m.put("beg_date", beg_date);
		m.put("end_date", end_date);
		
		insert("insertAgingFollowup", m);
	}

	public Integer selectAgingFollowup(String polis, String beg_date, String end_date) {
		Map m = new HashMap();
		m.put("polis", polis);
		m.put("beg_date", beg_date);
		m.put("end_date", end_date);
		return (Integer) querySingle("selectAgingFollowup", m);
	}

	public List selectReportAgingFollowup(String a_beg_date, String a_end_date) {
		Map m = new HashMap();
		m.put("a_beg_date", a_beg_date);
		m.put("a_end_date", a_end_date);
		return query("selectReportAgingFollowupKategori", m);
	}
	
	public List selectStokSpaj(String jenis, Integer lsjs_id) {
		Map m = new HashMap();
		m.put("jenis", jenis);
		m.put("lsjs_id", lsjs_id);
		return query("selectStokSpaj2", m);
	}
	
	public List selectStokSPAJSummary(String b_date, String e_date, Integer filter) {
		Map m = new HashMap();
		m.put("b_date", b_date);
		m.put("e_date", e_date);
		m.put("filter", filter);
		return query("selectStokSPAJSummary", m);
	}
	
	public void insertSpajHist(FormSpaj formspaj) {
		insert("insertSpajHist", formspaj);
	}

	public List selectDataJatuhTempoVisa() {
		return query("selectDataJatuhTempoVisa", null);
	}
	
	public void updateListAgingFollowup(String a_beg_date, String a_end_date) {
		Map m = new HashMap();
		m.put("a_beg_date", a_beg_date);
		m.put("a_end_date", a_end_date);
		update("updateListAgingFollowup", m);
	}
	
	public List selectReportCekNoHp(Map params) {
		return query("selectReportCekNoHp", params);
	}
	
	public Map selectNoGadgetESPAJ(String reg_spaj) throws DataAccessException{
		return (Map) querySingle("selectNoGadgetESPAJ",reg_spaj);
	}
	
	public List<Followup> selectReportFollowupBilling2(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String kat, String jn_tgl) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("begdate", begdate);
    	m.put("enddate", enddate);
    	m.put("lus_id", lus_id);
    	m.put("stfu", stfu);
    	m.put("cabang", cabang);
    	m.put("admin", admin);
    	m.put("kat", kat);
    	m.put("jn_tgl", jn_tgl);
    	return query("selectReportFollowupBilling2", m);
	 }
	
	public List<Followup> selectReportFollowupBillingPerUser2(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String kat, String rep, String jn_tgl) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("begdate", begdate);
    	m.put("enddate", enddate);
    	m.put("lus_id", lus_id);
    	m.put("stfu", stfu);
    	m.put("cabang", cabang);
    	m.put("admin", admin);
    	m.put("kat", kat);
    	m.put("rep", rep);
    	m.put("jn_tgl", jn_tgl);
    	return query("selectReportFollowupBillingPerUser2", m);
	}
	
	public List<Map> selectWelcomeCallSuccess (String reg_spaj, Integer jn_cek){
		Map m = new HashMap();
    	m.put("reg_spaj", reg_spaj);
    	m.put("jn_cek", jn_cek);
		return query("selectWelcomeCallSuccess", m);
	}
	
	public List<Followup> selectFollowupBilling2(String jenis, Integer aging, String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String jn, String spaj) throws DataAccessException{
		String lca_id = "";
		if(jenis.equals("gagaldebet_efc")){
			jenis = "gagaldebet";
    		lca_id = "65";
    	}
	    	
    	Map m = new HashMap();
    	m.put("jenis", jenis); //gagaldebet, jatuhtempo, all
    	m.put("aging", aging);
    	m.put("begdate", begdate);
    	m.put("enddate", enddate);
    	m.put("lus_id", lus_id);
    	m.put("stfu", stfu);
    	m.put("cabang", cabang);
    	m.put("admin", admin);
    	m.put("jn", jn);
    	m.put("lca_id", lca_id);
    	m.put("spaj", spaj);
    	
    	if(jenis.equalsIgnoreCase("visa_camp")) return query("selectFollowupBillingVC", m);
    	else return query("selectFollowupBilling2", m);
	}
	 
	public List<Followup> selectReportFollowupBillingPerUserCount(String begdate, String enddate, String lus_id, String stfu, String cabang, String admin, String kat, String rep, String jn_tgl) throws DataAccessException{
    	Map m = new HashMap();
    	m.put("begdate", begdate);
    	m.put("enddate", enddate);
    	m.put("lus_id", lus_id);
    	m.put("stfu", stfu);
    	m.put("cabang", cabang);
    	m.put("admin", admin);
    	m.put("kat", kat);
    	m.put("rep", rep);
    	m.put("jn_tgl", jn_tgl);
    	return query("selectReportFollowupBillingPerUserCount", m);
	}
	
	public List selectDataEspajBas(Map params) {
		return query("selectDataEspajBas", params);
	}

	public void generateKartuSimasCardOutsource() throws DataAccessException{
		Calendar tgl_sekarang = Calendar.getInstance();
//		Integer generateYear=new Integer(tgl_sekarang.get(Calendar.YEAR));
		Integer generateYear=new Integer(2110); // Utk U/W sebelumnya 2010
//		Integer generateYear=new Integer(2011); // Utk Agency
//		Integer generateYear=new Integer(2012); // Utk Marketing
		String nomorKartu = "";
		String generateKartu = "";
		//115 counter untuk U/W
		//117 counter untuk Agency
		//133 counter untuk marketing
		Integer counter_id = 115;
		int totalGenerate	= 1000;
		for(int i=0; i<totalGenerate; i++){
			Map a = uwDao.selectMstCounter(counter_id,"01");
			Double hasil=(Double) a.get("MSCO_VALUE");
			Boolean ok = false;
			do{
				try{
					Integer counter=new Integer(hasil.intValue());
					String generateCounter=FormatString.rpad("0", counter.toString(), 6);
					generateKartu = CheckSum.generateSimasCard6Digit(7);
					nomorKartu = generateYear.toString()+ generateKartu+ generateCounter;
					String jumlahKartu = selectExistNoKartuSimasCard(nomorKartu);
					String keterangan = "Generate Outsource Scheduler";
					if(selectExistNoKartuSimasCard(nomorKartu) != null){
						ok=false;
					}else{
						Map m = new HashMap();
						m.put("dist", 		"05");//05=UW / agency, 08=marketing
						m.put("no_kartu", 			nomorKartu); 
						m.put("produk", 		"05");
						m.put("premi", 			(double) 0);
						m.put("lus_id_input", 	(int) 1);
						m.put("reg_spaj", 		null);
						m.put("keterangan", keterangan);
						insert("insertMstKartuPas", m);
						counter+=1;
						//115 counter untuk U/W
						//117 counter untuk Agency
						//133 counter untuk marketing
						bacDao.update_counter(counter.toString(), counter_id, "01");
						ok=true;
					}
				}catch(Exception e){};
			}while (!ok);
		}
		
		
//		String to[]="inge@sinarmasmsiglife.co.id;ingrid@sinarmasmsiglife.co.id".split(";");
//		String subject="Generate Nomor kartu Simas Card";
//		String message="Berikut disampaikan Hasil Generate sebanyak "+totalGenerate+" nomor yang diminta :\n"+
//					   "";
//		try {
//			email.send(
//					false, props.getProperty("admin.ajsjava"),
//					to, null, null, subject, message, null);
//		} catch (MailException e) {
//			logger.error("ERROR :", e);
//		} catch (MessagingException e) {
//			logger.error("ERROR :", e);
//		}
	}	
	
}