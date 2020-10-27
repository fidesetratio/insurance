package com.ekalife.elions.dao;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ekalife.elions.model.Absen;
import com.ekalife.elions.model.AddReffBii;
import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.AksesHist;
import com.ekalife.elions.model.Aktivitas;
import com.ekalife.elions.model.Aspirasi;
import com.ekalife.elions.model.Bfa;
import com.ekalife.elions.model.Children;
import com.ekalife.elions.model.ClientHistory;
import com.ekalife.elions.model.Command;
import com.ekalife.elions.model.ContactPerson;
import com.ekalife.elions.model.Currency;
import com.ekalife.elions.model.HistoryUpload;
import com.ekalife.elions.model.Icd;
import com.ekalife.elions.model.IncomeCalc;
import com.ekalife.elions.model.Jiffy;
import com.ekalife.elions.model.Kebutuhan;
import com.ekalife.elions.model.KuesionerBrand;
import com.ekalife.elions.model.Matrix;
import com.ekalife.elions.model.Menu;
import com.ekalife.elions.model.Nasabah;
import com.ekalife.elions.model.Pendapatan;
import com.ekalife.elions.model.Personal;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.ProdBank;
import com.ekalife.elions.model.ProtectCalc;
import com.ekalife.elions.model.Rekomendasi;
import com.ekalife.elions.model.RelasiNasabah;
import com.ekalife.elions.model.SpajDet;
import com.ekalife.elions.model.Surat;
import com.ekalife.elions.model.SurplusCalc;
import com.ekalife.elions.model.TandaTangan;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.VoucherTaxi;
import com.ekalife.elions.model.reas.DataRiderLink;
import com.ekalife.elions.model.saveseries.Result;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.parent.ParentDao;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.stevesoft.pat.Regex;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.DateUtil;

public class CommonDao extends ParentDao {

	protected void initDao() throws Exception {
		this.statementNameSpace = "elions.common.";
		super.initDao();
	}
	
	/**Fungsi: 	Untuk mendapatkan NTP Server Date
	 * @return	Date
	 * @author	Andy
	 */
	public static Date selectSysdate(){
		return DateUtil.selectSysdate();
	}
	
	/**Fungsi: 	Untuk mendapatkan NTP Server Date (dd/MM/yyyy)
	 * @return	Date
	 * @author	Andy
	 */
	public static Date selectSysdateTrunc() {
		return DateUtil.selectSysdateTrunc();
	}
	
	/**Fungsi: 	Untuk mendapatkan NTP Server Date (dd/MM/yyyy)
	 * @return	String
	 * @author	Andy
	 */
	public static String selectSysdateTruncToString() {
		return DateUtil.selectSysdateTruncToString();
	}
	
	/**Fungsi: 	Untuk mendapatkan NTP Server Date (dd/MM/yyyy)
	 * @param	int (+/- day)
	 * @return	Date
	 * @author	Andy
	 */
	public static Date selectSysdateTruncated(int daysAfter) {
		return DateUtil.selectSysdateTruncated(daysAfter);
	}
	
	/**Fungsi: 	Untuk mendapatkan NTP Server Date
	 * @param	("dd"/"mm"/"yy"), (true/false), (+/-)
	 * @return	Date
	 * @author	Andy
	 */
	public static Date selectSysdate(String add, boolean trunc, int nilai) {
		return DateUtil.selectSysdate(add, trunc, nilai);
	}
	
	/**Fungsi: 	Untuk mendapatkan hari terakhir dari bulan dengan NTP Server Date (dd/MM/yyyy)
	 * @param	int (+/- day)
	 * @return	Date
	 * @author	Andy
	 */
	public static int selectLastDay() {
		return DateUtil.selectLastDay();
	}
	
	public Date selectTanggalLahirClient(String mcl_id) throws DataAccessException{
		return (Date) querySingle("selectTanggalLahirClient", mcl_id);
	}
	
	public String prosesUploadCabang(String reg_spaj, HttpServletRequest request) throws DataAccessException, IOException, MailException, MessagingException{
		StringBuffer result = new StringBuffer();
		
		Upload upload = new Upload();
		upload.setDaftarFile(new ArrayList<MultipartFile>(10));
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		
		for(int i=0; i<10; i++){
			String jns = ServletRequestUtils.getStringParameter(request, "jns" + i, "");
			MultipartFile mf = upload.getDaftarFile().get(i);
			String filename = mf.getOriginalFilename();
			Integer q=0;
			
			if(mf.isEmpty()){
				
			}else if(jns.equals("")){
				result.append("File " + (i+1) + " (" + filename + ") GAGAL diupload karena TIPE DOKUMEN tidak dipilih.\\n");
			}else if(mf.getSize() > 512000){
				result.append("File " + (i+1) + " (" + filename + ") GAGAL diupload karena ukuran file > 500kb.\\n");
			}else if(!filename.toLowerCase().trim().endsWith(".pdf")){
				result.append("File " + (i+1) + " (" + filename + ") GAGAL diupload karena bukan file PDF.\\n");
			}else{
				//PROSES UPLOAD FILE
				File directory = new File(
						props.getProperty("pdf.dir.export") + "\\" +
						uwDao.selectCabangFromSpaj(reg_spaj) + "\\" +
						reg_spaj);
				if(!directory.exists()) directory.mkdirs();
				List<File> daftarFile = FileUtils.listFilesInDirectory2(directory.toString());
				String ori= upload.getDaftarFile().get(i).getOriginalFilename();
				ori=ori.substring(0, ori.length() -4); //nama file original
				int iori= ori.length();// pjg file original
//				List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
				if (daftarFile.size()!=0){
					for(int x=0; x<daftarFile.size(); x++){
						String nfile= daftarFile.get(x).toString();
						String cuih= daftarFile.get(x).toString();
						nfile= nfile.substring(0, nfile.length() -4);
						cuih= cuih.substring(0, cuih.length() -8);
						String tfile=nfile.substring(49, cuih.length());// tipe file
						int p= tfile.length();
						if(ori!=null){
						
						String tipe= daftarFile.get(x).toString();
						tipe=tipe.substring(49, tipe.length());
						tipe= tipe.substring(0, tipe.length() -4);
						
						tipe= tipe.substring(tfile.length(),tipe.length());//ambil digit angkanya
						String tipe1= tipe.substring(2,tipe.length());
						tipe=tipe.substring(1,tipe.length());//nilai satuan
						
//						logger.info(tfile);
//						logger.info(jns);
						
						if(tfile.equals(jns)){
							q=tipe.length();
							Integer d=0;
							d=(Integer)d.parseInt(tipe);

							if(d.equals(0)||d.equals(null)){
								q=0;
								
								ori=reg_spaj+jns+" "+"00"+1+".pdf";
								String dest=directory+"/"+ori;
								File outputFile = new File(dest);
							    FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile);
							}else{
								d=(Integer)q.parseInt(tipe);
								d=d+1;
								if(d>9){
									ori=reg_spaj+jns+" "+"0"+d+".pdf";
									String dest=directory+"/"+ori;
									File outputFile = new File(dest);
								    FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile);
								}else{
									ori=reg_spaj+jns+" "+"00"+d+".pdf";
									String dest=directory+"/"+ori;
									File outputFile = new File(dest);
									FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile);
								}
							}
						}else{
							ori=reg_spaj+jns+" "+"00"+1+".pdf";
							String dest=directory+"/"+ori;
							File outputFile = new File(dest);
						    FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile);
						}
					}
					
					}
				}else{
					ori=reg_spaj+jns+" "+"00"+1+".pdf";
					String dest=directory+"/"+ori;
					File outputFile = new File(dest);
				    FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile);
				}
				
				result.append("File " + (i+1) + " (" + filename + ") BERHASIL diupload.\\n");
			}
		}
		
		String alamat="chizni@sinarmasmsiglife.co.id;tities@sinarmasmsiglife.co.id;timmy@sinarmasmsiglife.co.id;novie@sinarmasmsiglife.co.id";
		
		email.send(true, props.getProperty("admin.ajsjava"), alamat.split(";"), 
				null, null, "Ada File Scan ", "Ada File Scan untuk SPAJ " + reg_spaj+ "", null);

		return result.toString();
	}
	
	
	public String prosesUploadDokumenAsm(String no_blanko, HttpServletRequest request) throws DataAccessException, IOException, MailException, MessagingException{		StringBuffer result = new StringBuffer();
		
		Upload upload = new Upload();
		upload.setDaftarFile(new ArrayList<MultipartFile>(10));
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		
		for(int i=0; i<10; i++){
			String jns = ServletRequestUtils.getStringParameter(request, "jns" + i, "");
			MultipartFile mf = upload.getDaftarFile().get(i);
			String filename = mf.getOriginalFilename();
			Integer q=0;
			
			if(mf.isEmpty()){
				
			}else if(jns.equals("")){
				result.append("File " + (i+1) + " (" + filename + ") GAGAL diupload karena TIPE DOKUMEN tidak dipilih.\\n");
			}else if(mf.getSize() > 512000){
				result.append("File " + (i+1) + " (" + filename + ") GAGAL diupload karena ukuran file > 500kb.\\n");
			}else if(!filename.toLowerCase().trim().endsWith(".pdf")){
				result.append("File " + (i+1) + " (" + filename + ") GAGAL diupload karena bukan file PDF.\\n");
			}else{
				//PROSES UPLOAD FILE
				File directory = new File(
						props.getProperty("pdf.dir.uploadASM") + "\\" +
						no_blanko);
				if(!directory.exists()) directory.mkdirs();
				List<File> daftarFile = FileUtils.listFilesInDirectory2(directory.toString());
				String ori= upload.getDaftarFile().get(i).getOriginalFilename();
				ori=ori.substring(0, ori.length() -4); //nama file original
				int iori= ori.length();// pjg file original
//				List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
				int flag = 0;
				if (daftarFile.size()!=0){
					for(int x=0; x<daftarFile.size(); x++){
						String nfile= daftarFile.get(x).toString();
						String cuih= daftarFile.get(x).toString();
						nfile= nfile.substring(0, nfile.length() -4);
						cuih= cuih.substring(0, cuih.length() -8);
						String tfile=nfile.substring(44, cuih.length());// tipe file
						if(ori!=null){
						
						String tipe= daftarFile.get(x).toString();
						tipe=tipe.substring(44, tipe.length());
						tipe= tipe.substring(0, tipe.length() -4);
						
						tipe= tipe.substring(tfile.length(),tipe.length());//ambil digit angkanya
						tipe=tipe.substring(1,tipe.length());//nilai satuan
						
//						logger.info(tfile);
//						logger.info(jns);
						if(tfile.equals(jns)){
							flag = 1;
							q=tipe.length();
							Integer d=0;
							d=(Integer)d.parseInt(tipe);

							if(d.equals(0)||d.equals(null)){
								q=0;
								
								ori=no_blanko+jns+" "+"00"+1+".pdf";
								String dest=directory+"/"+ori;
								File outputFile = new File(dest);
							    FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile);
							}else{
								d=(Integer)q.parseInt(tipe);
								d=d+1;
								if(d>9){
									ori=no_blanko+jns+" "+"0"+d+".pdf";
//									String dest=directory+"/"+ori;
//									File outputFile = new File(dest);
//								    FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile);
								}else{
									ori=no_blanko+jns+" "+"00"+d+".pdf";
//									String dest=directory+"/"+ori;
//									File outputFile = new File(dest);
//									FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile);
								}
							}
						}else if( flag == 0 ){
							ori=no_blanko+jns+" "+"00"+1+".pdf";
//							String dest=directory+"/"+ori;
//							File outputFile = new File(dest);
//						    FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile);
						}
					}
					}
					String dest=directory+"/"+ori;
					File outputFile = new File(dest);
				    FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile);
				}else{
					ori=no_blanko+jns+" "+"00"+1+".pdf";
					String dest=directory+"/"+ori;
					File outputFile = new File(dest);
				    FileCopyUtils.copy(upload.getDaftarFile().get(i).getBytes(), outputFile);
				}
				
				result.append("File " + (i+1) + " (" + filename + ") BERHASIL diupload.\\n");
			}
		}
		
//		String alamat="Fadly_M@sinarmasmsiglife.co.id";
//		
//		email.send(true, "Fadly_M@sinarmasmsiglife.co.id", alamat.split(";"), 
//				null, null, "Ada File Scan ", "Ada File Scan untuk SPAJ " + no_blanko+ "", null);

		return result.toString();
	}
	
	public String prosesMoveDokumenAsm(String no_blanko, HttpServletRequest request, List<File> daftarFileFromParent, String tempCheckBoxString, String spaj) throws DataAccessException, IOException, MailException, MessagingException{
		StringBuffer result = new StringBuffer();
		
//		Upload upload = new Upload();
//		upload.setDaftarFile(new ArrayList<MultipartFile>(10));
//		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
//		binder.bind(request);
		String[] checkBoxStringArr = tempCheckBoxString.split(",");
		for(int i=0; i<daftarFileFromParent.size(); i++){
			if( checkBoxStringArr[i] != null && !checkBoxStringArr[i].equals("0") ){
				String jns = ServletRequestUtils.getStringParameter(request, "jns" + i, "");
	//			MultipartFile mf = upload.getDaftarFile().get(i);
	//			String filename = mf.getOriginalFilename();
				Integer q=0;
				
	//			if(mf.isEmpty()){
	//				
	//			}else{
					//PROSES UPLOAD FILE
					String lca_id = uwDao.selectCabangFromSpaj(spaj);
					File directory = new File(
							props.getProperty("pdf.dir.export") + "\\" +
							lca_id+ "\\" + spaj);
					if(!directory.exists()) directory.mkdirs();
					List<File> daftarFile = FileUtils.listFilesInDirectory2(directory.toString());
					String ori= daftarFileFromParent.get(i).getName();
	//				List<DropDown> daftarFile = FileUtils.listFilesInDirectory(directory);
					if (daftarFile.size()!=0){
						for(int x=0; x<daftarFile.size(); x++){
							String nfile= daftarFile.get(x).toString();
							String tfile=nfile.substring(39, nfile.length());
							if(ori!=null){
								if( ori.equals(tfile ))
								{
									String tipe= daftarFile.get(x).toString();
									tipe= tipe.substring(tipe.length() -7, tipe.length() -4);
									
									tipe=tipe.substring(1,tipe.length());//nilai satuan
									q=tipe.length();
									Integer d=0;
									d=(Integer)d.parseInt(tipe);
									
										ori= spaj + ori.substring(6,ori.length());
										String dest=directory+"/"+ori;
										File directory2 = new File(
												props.getProperty("pdf.dir.export") + "\\" +
												lca_id+ "\\" + spaj);
										List<File> daftarFile2 = FileUtils.listFilesInDirectory2(directory.toString());
										for( int j = 0 ; j < daftarFile2.size() ; j ++ )
										{
											String nfile2= daftarFile2.get(j).toString();
											String tfile2=nfile2.substring(39, nfile2.length());
											if( ori.equals(tfile2 ))
											{
												String tipe2= ori.substring(ori.length() -7, ori.length() -4);
												tipe2=tipe2.substring(1,tipe2.length());//nilai satuan
												q=tipe2.length();
												d=(Integer)q.parseInt(tipe2);
												d=d+1;
												if(d>9){
													ori=ori.substring(0,ori.length() -8)+" "+"0"+d+".pdf";
													dest=directory+"/"+ori;
												}else{
													ori=ori.substring(0,ori.length() -8)+" "+"00"+d+".pdf";
													dest=directory+"/"+ori;
												}
											}
										}
											try{
												File outputFile = new File(dest);
											    FileCopyUtils.copy(daftarFileFromParent.get(i), outputFile);
											    daftarFileFromParent.get(i).delete();
											}catch (Exception e) {
												logger.error("ERROR :", e);
												result.append("File " + (i+1) + " (" + daftarFileFromParent.get(i).getName() + ") GAGAL diupload.\\n");
											}
								}else if( x == daftarFile.size() - 1 ){
									try{
										String dest=directory+"/"+ori;
										File outputFile = new File(dest);
									    FileCopyUtils.copy(daftarFileFromParent.get(i), outputFile);
									    daftarFileFromParent.get(i).delete();
									}catch (Exception e) {
										logger.error("ERROR :", e);
										result.append("File " + (i+1) + " (" + daftarFileFromParent.get(i).getName() + ") GAGAL diupload.\\n");
									}
								}
							}
						}
					}else{
						try{
							String dest=directory+"/"+ori;
							File outputFile = new File(dest);
						    FileCopyUtils.copy(daftarFileFromParent.get(i), outputFile);
						    daftarFileFromParent.get(i).delete();
						}catch (Exception e) {
							logger.error("ERROR :", e);
							result.append("File " + (i+1) + " (" + daftarFileFromParent.get(i).getName() + ") GAGAL diupload.\\n");
						}
					}
					
					result.append("File " + (i+1) + " (" + daftarFileFromParent.get(i).getName() + ") BERHASIL diupload.\\n");
	//			}
			}
		}
		
//		String alamat="Fadly_M@sinarmasmsiglife.co.id";
//		
//		email.send(true, "Fadly_M@sinarmasmsiglife.co.id", alamat.split(";"), 
//				null, null, "Ada File Scan ", "Ada File Scan untuk SPAJ " + no_blanko+ "", null);

		return result.toString();
	}
	
	public List<Map> selectLast10MessageOfTheDay() throws DataAccessException{
		return query("selectLast10MessageOfTheDay", null);
	}
	
	public Map selectMessageOfTheDay() throws DataAccessException{ 
		return (Map) querySingle("selectMessageOfTheDay", null);
	}
	
	public void insertMstMessageDaily(Date msmd_id, String msmd_message, Date msmd_create_dt, int msmd_lus_id, int msmd_active) throws DataAccessException{
		Map m = new HashMap();
		m.put("msmd_id", msmd_id);
		m.put("msmd_message", msmd_message);
		m.put("msmd_create_dt", msmd_create_dt);
		m.put("msmd_lus_id", msmd_lus_id);
		m.put("msmd_active", msmd_active);
		insert("insertMstMessageDaily", m);
	}
	
	public int updateMstMessageDailyDeleteMessage(String msmd_id) throws DataAccessException{
		return (Integer) update("updateMstMessageDailyDeleteMessage", msmd_id);
	}
	
	public int selectAksesUserTerhadapSpaj(String reg_spaj, String lus_id) throws DataAccessException{
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("lus_id", lus_id);
		return (Integer) querySingle("selectAksesUserTerhadapSpaj", m);
	}
	
	public List<Map> selectPendingBSM(String lcb_no) throws DataAccessException{
		return query("selectPendingBSM",lcb_no);
	}
	
	public Date selectBeginOfYear(Date date) throws DataAccessException{
		return (Date) querySingle("selectBeginOfYear", date);
	}
	
	public Date selectBeginOfMonth(Date date) throws DataAccessException{
		return (Date) querySingle("selectBeginOfMonth", date);
	}
	
	public List<String> selectStableLinkRevisiSSU() throws DataAccessException{
		return query("selectStableLinkRevisiSSU", null);
	}
	
	public List<String> selectAllStableLink() throws DataAccessException{
		return query("selectAllStableLink", null);
	}
	
	public List<String> selectStabilLinkAfterSept2009() throws DataAccessException{
		return query("selectStabilLinkAfterSept2009", null);
	}
	
	public Date selectTanggalProduksiUntukProsesProduksi(Date tgl_rk, String lca_id, Integer jenis) throws DataAccessException{
		Map m = new HashMap();
		m.put("tgl_rk", tgl_rk);
		m.put("lca_id", lca_id);
		m.put("jenis", jenis); //MANTA (28/7/2017) - 1 = NB, 3 = Khusus SIAP2U Agency
		return (Date) querySingle("selectTanggalProduksiUntukProsesProduksi", m);
	}
	
	public int selectJumlahDuplikatPolis(String reg_spaj) throws DataAccessException{
		return (Integer) querySingle("selectJumlahDuplikatPolis", reg_spaj);
	}
	
	public List<Powersave> selectDaftarFileRolloverPowersave(String reg_spaj) throws DataAccessException{
		return query("selectDaftarFileRolloverPowersave", reg_spaj);
	}
	
	public List<Powersave> selectDaftarFileRolloverStablelink(String reg_spaj) throws DataAccessException{
		return query("selectDaftarFileRolloverStablelink", reg_spaj);
	}
	
	public String selectMsagIdLeader(String msag_id) throws DataAccessException{
		return (String)querySingle("selectMsagIdLeader", msag_id);
	}
	
	public List<Powersave> selectDaftarFileTopupStablelink(String reg_spaj) throws DataAccessException{
		return query("selectDaftarFileTopupStablelink", reg_spaj);
	}
	
	public void updateEmail(String lus_id, String email) throws DataAccessException{
		Map m = new HashMap();
		m.put("lus_id", lus_id);
		m.put("email", email);
		update("updateEmail", m);
	}
	
	public String encryptUrlKey(String idSecure2, String keyId, int jenisId, String link) {
		Map params = new HashMap();
		params.put("id_secure_2", idSecure2);
		params.put("key_id", keyId);
		params.put("jenis_id", jenisId);
		params.put("link", link.toLowerCase().replaceAll("&", "~"));
		return (String) querySingle("encryptUrlKey", params);
	}
	
	public int validateUrlKey(String idSecure, String link) {
		Map params = new HashMap();
		params.put("id_secure", idSecure);
		params.put("link", link.toLowerCase().replaceAll("&", "~"));
		return (Integer) querySingle("validateUrlKey", params);
	}
	
	public void delete(String query) {
		delete("deleteCanggih", query);
	}
	
	public String selectStatusAcceptFromSpaj(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectStatusAcceptFromSpaj", reg_spaj);
	}

	public String selectEmailUserInputFromSpaj(String reg_spaj) throws DataAccessException{
		return (String) querySingle("selectEmailUserInputFromSpaj", reg_spaj);
	}
	
	public String selectEmailUser(String lus_id) throws DataAccessException{
		return (String) querySingle("selectEmailUser", lus_id);
	}
	
	public Map selectInfoDetailUser(int lus_id) throws DataAccessException{
		return (HashMap) querySingle("selectInfoDetailUser", String.valueOf(lus_id));
	}
	
	public Date selectAddWorkdays(Date tanggal, int tambah) {
		Map params = new HashMap();
		params.put("tanggal", tanggal);
		params.put("tambah", tambah);
		return (Date) querySingle("selectAddWorkdays", params);
	}
	
	public String selectTanggalLahirPemegangPolis(String spaj) {
		return (String) querySingle("selectTanggalLahirPemegangPolis", spaj);
	}
	
	public void updateUserOnlineStatus(Date luss_online, String lus_id) {
		Map p = new HashMap();
		p.put("luss_online", luss_online);
		p.put("lus_id", lus_id);
		update("updateUserOnlineStatus", p);
	}
	
	public Map selectAksesHist(String lus_id) {
		Map result = new HashMap();
		result.put("detil", query("selectAksesHistDetail", lus_id));
		result.put("daftarSpaj", query("selectAksesHistSpaj", lus_id));
		result.put("jumlahSpaj", ((List) result.get("daftarSpaj")).size());
		return result;
	}
	
	public int selectJumlahSpajAksesHist(String lus_id) {
		Map result = new HashMap();
		result.put("daftarSpaj", query("selectAksesHistSpaj", lus_id));
		result.put("jumlahSpaj", ((List) result.get("daftarSpaj")).size());
		return ((List) result.get("daftarSpaj")).size();
	}
	
	public void insertAksesHist(AksesHist a) throws DataAccessException{
		insert("insertAksesHist", a);
	}
	
	static class HierarchicalXmlRowHandler implements RowHandler {
		//khusus untuk pake dengan dhtmlx treeview
		private Document domDocument;
		private Element tempParent;
		public HierarchicalXmlRowHandler(String aplikasi) throws DataAccessException {
			domDocument = DocumentHelper.createDocument();
			domDocument.setXMLEncoding("iso-8859-1");
			Element rootElement = getDomDocument().addElement("tree").addAttribute("id","0");
			rootElement.addElement("item")
				.addAttribute("text", aplikasi)
				.addAttribute("id", "menus")
				.addAttribute("open", "1");
		}
	    private void treeWalk(Element element, Integer parent) throws DataAccessException {
	        for ( int i = 0, size = element.nodeCount(); i < size; i++ ) {
	            Node node = element.node(i);
	            if ( node instanceof Element ) {
	            	Element t = (Element) node;
		            if(t.attribute("id").getValue().equals(parent.toString())) {
		            	this.tempParent = t;
		            	return;
		            }
            		treeWalk(t, parent);
	            }
	        }
	    }		
		public void handleRow(Object object) throws DataAccessException {
			Map row = (HashMap) object;
			Element parentElement=null;
			Integer parentMenuId = (Integer) row.get("PARENT_MENU_ID");
			if(parentMenuId==null) {
				parentElement = getDomDocument().getRootElement();
			}else if(parentMenuId==0) {
				parentElement = (Element) getDomDocument().getRootElement().node(0);
			}else {
				treeWalk(getDomDocument().getRootElement(), parentMenuId);
				parentElement = this.tempParent;
				this.tempParent=null;
			}
			Element item = parentElement.addElement("item")
				.addAttribute("text", (String) row.get("NAMA_MENU"))
				.addAttribute("id", ((Integer) row.get("MENU_ID")).toString());
			
			// logic untuk checkbox menu
			boolean cek = false, publik = false;
			if(row.get("LUS_ID")!=null && Integer.parseInt(row.get("TINGKAT").toString())>1) {
				// bukan public, ada lus_id dan link_menu
				if(!row.get("LUS_ID").toString().trim().equals("") && !row.get("LINK_MENU").toString().trim().equals("")) cek = true;
			}
			if(row.get("FLAG_PUBLIC")!=null) {
				if(row.get("FLAG_PUBLIC").toString().equals("Y")) {
					publik = true;
					if(!row.get("LINK_MENU").toString().trim().equals("")) cek=true;
				}
			}
			
			if(cek) item.addAttribute("checked", "1");
			if(publik) item.addAttribute("aCol", "#0000ff").addAttribute("sCol", "#87ceeb");
		}
		public Document getDomDocument() throws DataAccessException {
			return domDocument;
		}
	}

	static class XmlRowHandler implements RowHandler {
		//khusus untuk pake dengan dhtmlx treeview
		private Document domDocument;
		public XmlRowHandler() throws DataAccessException {
			domDocument = DocumentHelper.createDocument();
			domDocument.setXMLEncoding("iso-8859-1");
			getDomDocument().addElement("tree").addAttribute("id","0");
		}
		public void handleRow(Object object) throws DataAccessException {
			Map row = (HashMap) object;
			Element parentElement= getDomDocument().getRootElement();
			String nama = (String) row.get("LCA_NAMA");
			String id = (String) row.get("LCA_ID");
			
			parentElement.addElement("item")
				.addAttribute("text", "["+id+"] "+nama)
				.addAttribute("id", id);
			
		}
		public Document getDomDocument() throws DataAccessException {
			return domDocument;
		}
	}

	public int selectApakahSudahProsesNilaiTunai(String spaj) throws DataAccessException {
		return (Integer) querySingle("selectApakahSudahProsesNilaiTunai", spaj);
	}
	
	public List<String> selectDaftarSpajYangMauDiProsesNilaiTunai(int mulai, int selesai) throws DataAccessException {
		Map params = new HashMap();
		params.put("mulai", mulai);
		params.put("selesai", selesai);
		return query("selectDaftarSpajYangMauDiProsesNilaiTunai", params);
	}
	
	public List<String> selectDaftarSpajYangMauDiProses() throws DataAccessException {
		return query("selectDaftarSpajYangMauDiProses", null);
	}

	public List<Map> selectClosingDate() throws DataAccessException{
		return query("selectClosingDate", null);
	}
	
	public Map<String, Object> selectUnderTablePeriode() throws DataAccessException{
		return (Map<String, Object>)querySingle("selectUnderTablePeriode", null);
	}
	
	public List<RelasiNasabah> selectMstRelasiNasabah(String mns_kd_nasabah) throws DataAccessException{
		return query("selectMstRelasiNasabah", mns_kd_nasabah);
	}
	
	public List<Children> selectMstChildren(String mns_kd_nasabah) throws DataAccessException{
		return query("selectMstChildren", mns_kd_nasabah);
	}
	
	public List<Matrix> selectMstMatrix(String mns_kd_nasabah) throws DataAccessException{
		return query("selectMstMatrixAdv", mns_kd_nasabah);
	}
	
	public List<Aspirasi> selectMstAspirasi(String mns_kd_nasabah) throws DataAccessException{
		return query("selectMstAspirasi", mns_kd_nasabah);
	}
	
	public List<Rekomendasi> selectMstRekomendasi(String mns_kd_nasabah) throws DataAccessException{
		return query("selectMstRekomendasi", mns_kd_nasabah);
	}
 	
	public int updateEmailAgen(DropDown d) throws DataAccessException{
		return update("updateEmailAgen", d);
	}
	
	public void insertIcdCode(Icd d) throws DataAccessException {
		insert("insertIcdCode", d);
	}
	
	public String selectEncryptDecrypt(String kata, String jenis) throws DataAccessException{
		Map p = new HashMap();
		p.put("kata", kata);
		p.put("jenis", jenis);
		return (String) querySingle("selectEncryptDecrypt", p);
	}
	
	public List<String> selectDaftarBatalGuthrie() throws DataAccessException{
		return query("selectDaftarBatalGuthrie", null);
	}
	
	public List selectAddressRegion() throws DataAccessException{
		return query("selectAddressRegion", null);
	}
	
	public void insertMstHistoryUpload(String upload_id, String code_id, String old_code_id, String filename, 
			String temp_filename, Date revisi_date, String upload_jenis, String status, Integer revisi,
			Integer lus_id, String keterangan, String path, Date upload_date)throws DataAccessException {
		HistoryUpload history = new HistoryUpload();
		history.setUpload_id(upload_id);
		history.setCode_id(code_id);
		history.setOld_code_id(old_code_id);
		history.setFilename(filename);
		history.setTemp_filename(temp_filename);
		history.setRevisi_date(revisi_date);
		history.setUpload_jenis(upload_jenis);
		history.setStatus(status);
		history.setRevisi(revisi);
		history.setLus_id(lus_id);
		history.setKeterangan(keterangan);
		history.setPath(path);
		history.setUpload_date(upload_date);
		insert("insertMstHistoryUpload", history);
		
	}
	
	public void insertLampiranMstHistoryUpload(String upload_id, String code_id, String old_code_id, String filename, 
			String temp_filename, Date revisi_date, String upload_jenis, String status, Integer revisi,
			Integer lus_id, String keterangan, String path,String jabatan, String ketentuan, Date upload_date)throws DataAccessException {
		HistoryUpload history = new HistoryUpload();
		history.setUpload_id(upload_id);
		history.setCode_id(code_id);
		history.setOld_code_id(old_code_id);
		history.setFilename(filename);
		history.setTemp_filename(temp_filename);
		history.setRevisi_date(revisi_date);
		history.setUpload_jenis(upload_jenis);
		history.setStatus(status);
		history.setRevisi(revisi);
		history.setLus_id(lus_id);
		history.setKeterangan(keterangan);
		history.setPath(path);
		history.setJabatan(jabatan);
		history.setKetentuan(ketentuan);
		history.setUpload_date(upload_date);
		insert("insertLampiranMstHistoryUpload", history);
		
	}
	
	public void updateMstHistoryUpload(String temp_filename, String status)throws DataAccessException{
		Map param=new HashMap();
		param.put("temp_filename", temp_filename);
		param.put("status", status);
		update("updateMstHistoryUpload",param);
	}
	
	public void updateLstCabangBii(String kd_cabang, String nm_cabang,Integer jn_bank)throws DataAccessException{
		Map param=new HashMap();
		param.put("kd_cabang", kd_cabang);
		param.put("nm_cabang", nm_cabang);
		param.put("jenis", jn_bank);
		update("updateLstCabangBii", param);
	}
	
	public void updateLstReffBii(String ls_cab, Integer ref, Integer flag, String kd_agent, String nm_agent,Date tglAktif, Date tglNonAktif, String pos_code, Date tglUpdate, Integer jn_bank, String nik, String tnik)throws DataAccessException{
		Map param = new HashMap();
		param.put("ls_cab", ls_cab);
		param.put("ref", ref);
		param.put("flag", flag);
		param.put("kd_agent", kd_agent);
		param.put("nm_agent", nm_agent);
		param.put("jenis", jn_bank);
		param.put("tglAktif", tglAktif);
		param.put("tglUpdate", tglUpdate);
		param.put("tglNonAktif", tglNonAktif);
		param.put("pos_code", pos_code);
		param.put("nik", nik);
		param.put("tnik", tnik);
		update("updateLstReffBii", param);
	}
	
	public List selectMstHistoryUpload() throws DataAccessException{
		return query("selectMstHistoryUpload", null);
	}
	
	public List selectListMstHistoryUpload(String jenis, String level, Date tglawal, Date tglakhir, String upload_jenis) throws DataAccessException{
		Map map = new HashMap();
		map.put("jenis", jenis);
		map.put("level", level);
		map.put("tglawal", tglawal);
		map.put("tglakhir", tglakhir);
		map.put("upload_jenis", upload_jenis);
		return query("selectListMstHistoryUpload", map);
	}
	
	public List selectListMstHistoryUploadByStatus(String jenis, String upload_jenis) throws DataAccessException{
		Map map = new HashMap();
		map.put("jenis", jenis);
		map.put("upload_jenis", upload_jenis);
		return query("selectListMstHistoryUploadByStatus", map);
	}
	
	public List selectListMstHistoryUploadByDate(String jenis, String upload_jenis,String tglawal, String tglakhir) throws DataAccessException{
		Map map = new HashMap();
		map.put("jenis", jenis);
		map.put("upload_jenis", upload_jenis);
		map.put("tglawal", tglawal);
		map.put("tglakhir", tglakhir);
		return query("selectListMstHistoryUploadByDate", map);
	}
	
	public int selectCountMstHistoryUpload(String temp_filename){
		return (Integer) querySingle("selectCountMstHistoryUpload", temp_filename);
	}
	
	public String selectPathMstHistoryUpload(String upload_id)throws DataAccessException {
		return (String) querySingle("selectPathMstHistoryUpload", upload_id);
	}
	
	public String selectFilenameMstHistoryUpload(String upload_id) throws DataAccessException{
		return (String) querySingle("selectFilenameMstHistoryUpload", upload_id);
	}
	
	public String selectPathMstHistoryUpload2(String upload_id, String code_id)throws DataAccessException {
		Map map = new HashMap();
		map.put("upload_id", upload_id);
		map.put("code_id", code_id);
		return (String) querySingle("selectPathMstHistoryUpload2", map);
	}
	
	public String selectFilenameMstHistoryUpload2(String upload_id,String code_id) throws DataAccessException{
		Map map = new HashMap();
		map.put("upload_id", upload_id);
		map.put("code_id", code_id);
		return (String) querySingle("selectFilenameMstHistoryUpload2", map);
	}
	
	
	/**Fungsi:	Untuk menampilkan semua data pada tabel EKA.LST_ADDR_REGION
	 * @param 	String larId (dimana larId ini dapat > 1, dengan menggunakan pembatas ';')
	 * 			Contoh:"123,12,123"
	 * @return	List
	 * @throws 	DataAccessException
	 * @author	Ferry Harlim
	 */
	public List selectAddressRegionLarId(String larId) throws DataAccessException{
		return query("selectAddressRegionLarId", larId);
	}
	public List selectUserAdmin(int lus_id) throws DataAccessException{
		return query("selectUserAdmin", lus_id);
	}
	
	public int updateMstCompanyAddress(AddressNew addressNew) throws DataAccessException{
		return update("updateMstCompanyAddress", addressNew);
	}
	
	public void insertMstCompanyAddress(AddressNew addressNew) throws DataAccessException{
		insert("insertMstCompanyAddress", addressNew);
	}
	
	public List selectContactPerson(String mcl_id) throws DataAccessException {
		return query("selectContactPerson", mcl_id);
	}
	
	public List selectRegions2(String query) throws DataAccessException {
		return query("selectRegions2", query);
	}
	
	public List selectJenisUsaha() throws DataAccessException {
		return query("selectJenisUsaha", null);
	}
	
	public List<Nasabah> selectMstNasabah()throws DataAccessException{
		return query("selectMstNasabahNoParam", null);
	}
	
	public List selectStatusPersonal(String mcl_id) throws DataAccessException{
		return query("selectStatusPersonal", mcl_id);
	}
	
	public Personal selectProfilePerusahaan(String mcl_id) throws DataAccessException{
		return (Personal) querySingle("selectProfilePerusahaan", mcl_id);
	}
	
	public List selectReimenderInvoicePerusahaan() throws DataAccessException{
		return query("selectReimenderInvoicePerusahaan",null);
	}
	
	public List selectProfile(String tipe, String jenis, String kata) throws DataAccessException {
		Map<String, Comparable> params = new HashMap<String, Comparable>();
		params.put("tipe", tipe);
		params.put("jenis", jenis);
		params.put("kata", kata.toUpperCase().trim());
		return query("selectProfile", params);
	}
	
	public Integer validationCurrency(Currency kurs) throws DataAccessException{
		return (Integer) querySingle("validationCurrency", kurs);
	}
	
	public void insertCurrency(Currency kurs) throws DataAccessException{
		insert("insertCurrency", kurs);
	}
	
	public int updateCurrency(Currency kurs) throws DataAccessException{
		return update("updateCurrency", kurs);
	}
	
	@SuppressWarnings("unchecked")
	public List<Currency> selectCurrency(Command cmd) throws DataAccessException{
		List<Currency> query = query("selectCurrency", cmd);
		return query;
	}
	
	public List selectTreeMenu(int jenis, int lus_id) throws DataAccessException{
		Map map = new HashMap();
		map.put("jenis", jenis);
		map.put("lus_id", lus_id);
		return query("selectTreeMenu", map);	
	}
	
	public List selectAllMenu(int jenis) throws DataAccessException {
		return query("selectAllMenu", new Integer(jenis));
	}
	
	public List selectJenisAplikasi() throws DataAccessException {
		return query("selectJenisAplikasi", null);
	}
		
	public List selectUserList(String userName) {
		return query("selectUserList", userName);
	}
	
	public List selectUserList2(String userName) {
		return query("selectUserList2", userName);
	}
	
	public void insertAllAksesCabang(String lus){
		delete("deleteAllCabang", lus);
		insert("insertAllCabang", lus);
	}
	
	public void insertAksesCabang(String lus, String[] nilai) throws DataAccessException {
		delete("deleteAllCabang", lus);
		StringBuffer ucup = new StringBuffer();
		for(int i=0; i<nilai.length; i++) {
			if(i!=0)ucup.append(",");
			ucup.append("'"+nilai[i]+"'");
			//nilai[i]="'"+nilai[i]+"'";
		}
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("lus", lus);
		m.put("nilai", ucup);
		insert("insertSelectedCabang", m);
	}
	
	public void insertMstMatrixAdv(String mns_kd_nasabah, Integer li_bisnis, Integer msma_fund) throws DataAccessException{
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("lsbs_id", li_bisnis);
		map.put("msma_fund", msma_fund);
		insert("insertMstMatrixAdv", map);
	}
	
	public void insertMstAspirasi(String mns_kd_nasabah, Integer msas_no)throws DataAccessException{
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("msas_no", msas_no);
		insert("insertMstAspirasi", map);
	}
	
	public void insertMstRekomendasi(String mns_kd_nasabah, Integer msrek_no)throws DataAccessException{
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("msrek_no", msrek_no);
		insert("insertMstRekomendasi", map);
	}
	
	public void deleteAllAksesCabang(String lus){
		delete("deleteAllCabang", lus);
	}
	
	public void resetIbatisCache() throws DataAccessException {
		query("resetIbatisCache", null);
	}
		
	public void deleteMenuAkses(Map params) throws DataAccessException {
		delete("deleteMenuAkses", params);
	}
	
//	public void updateMenuAkses(String nilai[], String lus) throws DataAccessException {
	public void updateMenuAkses(String nilai[], String lus, String jenisAplikasi ) throws DataAccessException {
		for(int i=0; i<nilai.length; i++) {
			Regex reg = new Regex("\\d");
			if(!reg.search(nilai[i])) nilai[i]="9999";
		}
        Map<String, String> params = new HashMap<String, String>();
        params.put( "lusId", lus );
        params.put( "jenisAplikasi", jenisAplikasi );
        delete("deleteMenuAkses", params );
		Map<String, String> m = new HashMap<String, String>();
		m.put("nilai", StringUtils.arrayToCommaDelimitedString(nilai));
		m.put("lus", lus);
		insert("insertMenuAkses", m);
	}

	public Document selectXmlAllCabangRegistered(Integer lus_id) throws DataAccessException {
		RowHandler rowHandler = new XmlRowHandler();
		queryHandler("selectAllCabangRegistered", lus_id, rowHandler);
		Document doc = ((XmlRowHandler) rowHandler).getDomDocument();
		return doc;
	}

	public Document selectXmlAllCabangNotRegistered(Integer lus_id) throws DataAccessException {
		RowHandler rowHandler = new XmlRowHandler();
		queryHandler("selectAllCabangNotRegistered", lus_id, rowHandler);
		Document doc = ((XmlRowHandler) rowHandler).getDomDocument();
		return doc;
	}
	/*
	private Document queryXml(String queryId, String aplikasi, Object param) throws DataAccessException {
		RowHandler rowHandler = new HierarchicalXmlRowHandler(aplikasi);
		getSqlMapClientTemplate().queryWithRowHandler(this.statementNameSpace+queryId, param, rowHandler);
		Document doc = ((HierarchicalXmlRowHandler) rowHandler).getDomDocument();
		return doc;		
	}*/
	
	private Document queryXml(String queryId, String aplikasi, Integer jenis, Integer lus_id) throws DataAccessException {
		RowHandler rowHandler = new HierarchicalXmlRowHandler(aplikasi);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("jenis", jenis);
		params.put("lus_id", lus_id);
		getSqlMapClientTemplate().queryWithRowHandler(this.statementNameSpace+queryId, params, rowHandler);
		Document doc = ((HierarchicalXmlRowHandler) rowHandler).getDomDocument();
		return doc;
	}

	public Document selectXmlAllUserMenu(Integer lus_id, Integer jenis, String aplikasi) throws DataAccessException {
		return queryXml("selectAllUserMenu", aplikasi, jenis, lus_id);
	}

	public void updateLoginPassword(String id, String pass) throws DataAccessException {
		Map<String, String> m = new HashMap<String, String>();
		m.put("id", id);
		m.put("pass", pass);
		update("updateLoginPassword", m);
	}

	public String selectPasswordAuthentication(String id) throws DataAccessException {
		return (String) querySingle("selectPasswordAuthentication", id);
	}

	public List selectEmployeesInDepartment(String lde) throws DataAccessException {
		return query("selectEmployeesInDepartment", lde); 
	}

	public User selectLoginAuthentication(User user){
		return (User) querySingle("selectLoginAuthentication", user);
	}
	
	public User selectLoginAuthenticationByLusId(User user){
		return(User) querySingle("selectLoginAuthenticationByLusId", user);
	}
	
	public User selectLoginModelAuthentication(User user){
		return (User) querySingle("selectLoginModelAuthentication", user);
	}
	
	public Date selectMaxDatePositionSpaj(String reg_spaj, String msps_desc){
		Map m = new HashMap();
		m.put("reg_spaj", reg_spaj);
		m.put("msps_desc", msps_desc);
		return (Date) querySingle("selectMaxDatePositionSpaj", m);
	}
	
	public Integer selectWorkDays(Date beg_date, Date end_date){
		Map m = new HashMap();
		m.put("beg_date", beg_date);
		m.put("end_date", end_date);
		return (Integer) querySingle("selectWorkDays", m);
	}

	public User selectUser(String lus_id){
		return (User) querySingle("selectUser", lus_id);
	}

	public List selectAllUserMenu(Integer lus_id){
		return query("selectAllUserMenu", lus_id);
	}

	public List selectAllUserMenuWithAccessRights(Integer lus_id){
		Map<String, Comparable> params = new HashMap<String, Comparable>();
		params.put("lus_id", lus_id);
		params.put("aktif", "Y");
		params.put("publik", "N");
		return query("selectAllUserMenuWithAccessRights", params);
	}

	public List selectMenu(String inOrNotIn) throws DataAccessException {
		return query("selectMenu", inOrNotIn);
	}

	public List selectAksesCabang(int lus_id) throws DataAccessException {
		return query("selectAksesCabang", new Integer(lus_id));
	}
	
	public List selectAksesCabang_lar(int lus_id) throws DataAccessException {
		return query("selectAksesCabang_lar", new Integer(lus_id));
	}
	
	public void insertMstCompany(Personal personal){
		Map param=new HashMap();
		insert("insertMstCompany", personal);
	}
	
	public void insertMstCompanyContact(ContactPerson payroll){
		insert("insertMstCompanyContact",payroll);
	}
	
	public int updateMstCompany(Personal personal){
		return update("updateMstCompany",personal);
	}
	
	public int updateMstCompanyContact(ContactPerson contact){
		return update("updateMstCompanyContact", contact);
	}
	
	public void deleteMstCompanyContact(String mcl_id) {
		delete("deleteMstCompanyContact", mcl_id);
	}
	
	public int updateMstClientNew(String mclId,String mclFirst,String mclGelar){
		Map param=new HashMap();
		param.put("mcl_first", mclFirst);
		param.put("mcl_gelar", mclGelar);
		param.put("mcl_id", mclId);
		return update("updateMstClientNew", param);
	}
	
	public List selectAllUsers(String lca_id, String lde_id) throws DataAccessException{
		Map<String, String> params = new HashMap<String, String>();
		params.put("lca_id", lca_id);
		params.put("lde_id", lde_id);
		return query("selectAllUsers", params);
	}
	
	public List selectAllBranchesAndDepartments(String lca_id, String lde_id) throws DataAccessException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("lca_id", lca_id);
		params.put("lde_id", lde_id);
		return query("selectAllBranchesAndDepartments", params);
	}	

	public void insertMstTandatangan(TandaTangan ttd) throws DataAccessException{
		insert("insertMstTandatangan", ttd);
	}
	
	public List selectDaftarMstTandatangan(int lus_id) throws DataAccessException{
		return query("selectDaftarMstTandatangan", lus_id);
	}
	
	public TandaTangan selectMstTandatangan(String mcl_id, String nama) throws DataAccessException{
		Map<String, String> params = new HashMap<String, String>();
		params.put("mcl_id", mcl_id);
		params.put("nama", nama);
		return (TandaTangan) querySingle("selectMstTandatangan", params);
	}
	
	public List  selectMstTandatanganSPAJ(String spaj) throws DataAccessException{
		return  query("selectMstTandatanganSPAJ", spaj);
	}
	
	public TandaTangan selectTandatangan(String mstt_id) throws DataAccessException{
		return (TandaTangan) querySingle("selectTandatangan", mstt_id);
	}
	
	public TandaTangan selectTandatanganMCLID(String mcl_id) throws DataAccessException{
		return (TandaTangan) querySingle("selectTandatanganMCLID", mcl_id);
	}
	
	public int updateMstTandatangan(TandaTangan ttd) throws DataAccessException{
		return update("updateMstTandatangan", ttd);
	}
	public List selectAllMenuMaintenance(Integer jenis){
		return query("selectAllMenuMaintenance", jenis);
	}
	public Menu selectLstMenuNew(Integer menuId){
		return (Menu)querySingle("selectLstMenuNew", menuId);
	}
	public List selectCheckChild(Integer menuId){
		return query("selectCheckChild", menuId);
	}
	public void updateLstMenuNew(Menu menu){
		update("updateLstMenuNew", menu);
	}
	public void deleteLstMenuNew(Integer menuId){
		delete("deleteLstMenuNew", menuId);
	}
	public void insertLstMenuNew(Menu menu){
		insert("insertLstMenuNew", menu);
	}
	public Integer selectMaxMenuId(){
		return(Integer)querySingle("selectMaxMenuId", null);
	}
	public Integer selectTingkatMenu(Integer menuId){
		return(Integer)querySingle("selectTingkatMenu", menuId);
	}
	public Integer selectMaxUrutanMenu(Integer menuId){
		return(Integer)querySingle("selectMaxUrutanMenu", menuId);
	}
	/**@Fungsi:	Untuk Menampilkan data nasabah pada tabel
	 * 			EKA.MST_NASABAH sesuai dengan posisi yang di kirim
	 * @param 	Integer lspdId
	 * @return 	List
	 * @author 	Ferry Harlim
	 */
	public List selectAllMstNasabah(Integer lspdId){
		return query("selectAllMstNasabah",lspdId);
	}
	/**@Fungsi:	Untuk Menampilkan data nasabah pada tabel
	 * 			EKA.MST_NASABAH sesuai dengan kode nasabah
	 * @param 	String kdNasabah
	 * @return 	Nasabah
	 * @author 	Ferry Harlim
	 */
	public Nasabah selectMstNasabah(String kdNasabah){
		return (Nasabah)querySingle("selectMstNasabah", kdNasabah);
	}
	
	public SurplusCalc selectMstSurplusCalc(String mns_kd_nasabah){
		return (SurplusCalc)querySingle("selectMstSurplusCalc", mns_kd_nasabah);
	}
	
	public ProtectCalc selectMstProtectCalc(String mns_kd_nasabah){
		return (ProtectCalc)querySingle("selectMstProtectCalc", mns_kd_nasabah);
	}
	
	public IncomeCalc selectMstIncomeCalc(String mns_kd_nasabah){
		return (IncomeCalc)querySingle("selectMstIncomeCalc", mns_kd_nasabah);
	}
	
	/**@Fungsi:	Untuk Menampilkan data nasabah pada tabel
	 * 			EKA.MST_NASABAH sesuai dengan kode nasabah
	 * @param 	String nomor (KdNasabah atau no referral, Integer tipe (1=kdNasabah, 2=no referral)
	 * @return 	List
	 * @author 	Ferry Harlim
	 */
	public List selectMstNasabahByCode(String nomor,Integer tipe, Integer lspd_id){
		Map param=new HashMap();
		param.put("nomor", nomor);
		param.put("tipe",tipe);
		param.put("lspd_id", lspd_id);
		return query("selectMstNasabahByCode", param);
	}
	
	public List selectMstNasabahByCodeAll(String nomor,Integer tipe){
		Map param=new HashMap();
		param.put("nomor", nomor);
		param.put("tipe",tipe);
		return query("selectMstNasabahByCodeAll", param);
	}
	
	/**@Fungsi:	Untuk Menampilkan data cabang bii pada tabel
	 * 			EKA.LST_CAB_BII sesuai dengan kode region
	 * @param 	String kdRegion
	 * @return 	List
	 * @author 	Ferry Harlim
	 */
	public List selectLstCabBii(String kdRegion){
		return query("selectLstCabBii", kdRegion);
	}
	
	/**@Fungsi:	Untuk Menampilkan data cabang bii pada tabel
	 * 			EKA.LST_CAB_BII di semua region
	 * @param 	
	 * @return 	List
	 * @author 	Deddy
	 */
	public List selectLstCabBiiAll(){
		return query("selectLstCabBiiAll", null);
	}
	
	/**@Fungsi:	Untuk Menampilkan cabang BII dengan  format
	 * 			nama koordinir/nama area/nama cabang	
	 * @param 	String kode
	 * @return 	String
	 * @author 	Ferry Harlim
	 */
	public String selectCabangBii(String kdRegion){
		return (String) querySingle("selectCabangBii", kdRegion);
	}
	/**@Fungsi:	Untuk Menampilkan data pada tabel
	 * 			EKA.MST_BFA
	 * @param	String msagId,String kode
	 * @return 	List
	 * @author 	Ferry Harlim
	 */
	public List selectMstBfa(String msagId,String kode){
		Map param=new HashMap();
		param.put("msag_id",msagId);
		//param.put("kode",kode);
		return query("selectMstBfa", param);
	}
	/**@Fungsi:	Untuk Menampilkan data pada tabel
	 * 			EKA.MST_BFA sesuai dengan level_id
	 * @param	Integer level
	 * @return	List
	 * @author	Ferry Harlim 
	 */
	public List selectMstBfaLevel(Integer level){
		return query("selectMstBfaLevelId", level);
	}
	/**Fungsi:	Untuk menampilkan data pada tabel
	 * 			EKA.LST_JN_LEAD
	 * @param	String filter (level_id)
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectLstJnLead(String levelId){
		Map param=new HashMap();
		param.put("levelId", levelId);
		return query("selectLstJnLead",param);
	}
	
	public List selectLstCabangBii(Integer jenis){
		Map param=new HashMap();
		param.put("jenis", jenis);
		return query("selectLstCabangBii",param);
	}
	/**Fungsi:	Untuk menampilkan data pada tabel
	 * 			EKA.LST_Cab_Bii
	 * @return	List
	 * @author 	Ferry Harlim
	 */
	public List selectAllLstCabBii(){
		return query("selectAllLstCabBii",null);
	}
	/**Fungsi:	Untuk Menampilkan data pada tabel 
	 * 			EKA.MST_BFA sesuai dengan kode.
	 * @param	String kode
	 * @return	com.ekalife.elions.model.Bfa
	 * @author 	Ferry Harlim
	 */
	public List selectMstBfaKode(String kode){
		return query("selectMstBfaKode", kode);
	}
	/**Fungsi:	Untuk Menampilkan data pada tabel
	 * 			EKA.MST.BFA sesuai dengan kondisi level_id,status_aktif
	 * 			kd_region,kd_koord
	 * @param	Bfa bfa
	 * @return 	String
	 * @author 	Ferry Harlim
	 */
	public String selectMstBfaMsagId(Bfa bfa){
		return (String)querySingle("selectMstBfaMsagId", bfa);
	}
	/**Fungsi:	Untuk Menampilkan data pada tabel 
	 * 			EKA.LST_REFERRER_BII dengan flag_aktif=1
	 * @param	Integer levelId
	 * @return	DropDown
	 * @author 	Ferry Harlim
	 */
	public List selectLstReferrerBii(Integer levelId){
		Map param=new HashMap();
		param.put("levelId", levelId);
		return query("selectLstReferrerBii",param);
	}
	/**Fungsi:	Untuk Menampilkan Data Nama BFA beserta cabangnya
	 * @param	String msagId
	 * @return	java.util.HashMap
	 * @author 	Ferry Harlim
	 */
	public Map selectBfaNCabang(String msagId){
		return (HashMap)querySingle("selectBfaNCabang", msagId);
	}
	/**Fungsi:	Untuk Menampilkan Data referrer BII beserta cabangnya
	 * @param	String refBii
	 * @return 	java.util.HashMap
	 * @author 	Ferry Harlim
	 */
	public Map selectLstReffBiiNCabang(String refBii){
		return (HashMap)querySingle("selectLstReffBiiNCabang", refBii);
	}
	
	
	/**Fungsi:	Untuk Menampilkan nilai max MNS_NO_REF pada tabel EKA.MST_NASABAH
	 * 			no referral platinum (kdRegion,kdKoord,kdArea)=(02,07,01) 
	 * @param	String kdRegion
	 * 			String kdKoord
	 * 			String kdArea
	 * 			String jnNasabah
	 * @return 	String
	 * @author	Ferry Harlim
	 */
	public String selectMaxMstNasabahMnsNoReff(String kdRegion,String kdKoord,String kdArea,String jnNasabah){
		Map map=new HashMap();
		map.put("kdRegion", kdRegion);
		map.put("kdKoord", kdKoord);
		map.put("kdArea", kdArea);
		map.put("jnNasabah", jnNasabah);
		return (String)querySingle("selectMaxMstNasabahMnsNoReff", map);
	}
	/**Fungsi: 	Untuk Menampilkan jumlah data pada tabel EKA.MST_AKTIVITAS,
	 * 			sesuai dengan kode nasabah dan jenis aktivitas
	 * @param	String kdNasabah, Integer aktivitas
	 * @author	Ferry Harlim
	 */
	public Integer selectCountMstAktivitas(String kdNasabah, Integer aktivitas){
		Map param=new HashMap();
		param.put("kdNasabah", kdNasabah);
		param.put("aktivitas", aktivitas);
		return (Integer) querySingle("selectCountMstAktivitas", param);
	}
	
	public Integer selectCountMstAspirasi(String mns_kd_nasabah)throws DataAccessException{
		return (Integer) querySingle("selectCountMstAspirasi", mns_kd_nasabah);
	}
	
	public Integer selectCountMstSurplus(String mns_kd_nasabah){
		return (Integer) querySingle("selectCountMstSurplusCalc", mns_kd_nasabah);
	}
	
	public void insertNasabahInMstSurplus(String mns_kd_nasabah){
		insert("insertNasabahInMstSurplus", mns_kd_nasabah);
	}
	
	public Integer selectCountMstProtect(String mns_kd_nasabah){
		return (Integer) querySingle("selectCountMstProtectCalc", mns_kd_nasabah);
	}
	
	public void insertNasabahInMstProtect(String mns_kd_nasabah){
		insert("insertNasabahInMstProtect", mns_kd_nasabah);
	}
	
	public Integer selectCountMstIncome(String mns_kd_nasabah){
		return (Integer) querySingle("selectCountMstIncomeCalc", mns_kd_nasabah);
	}
	
	public void insertNasabahInMstIncome(String mns_kd_nasabah){
		insert("insertNasabahInMstIncome", mns_kd_nasabah);
	}
	
	public List<Map> selectLstJnKebutuhan(){
		return (List<Map>) query("selectLstJnKebutuhan", null);
	}
	
	public List<Map> selectLstPendapatan(){
		return (List<Map>) query("selectLstPendapatan", null);
	}
	
	public Integer selectCountMstKebutuhan(String mns_kd_nasabah){
		return (Integer) querySingle("selectCountMstKebutuhan", mns_kd_nasabah);
	}
	
	public Integer selectCountMstPendapatan(String mns_kd_nasabah){
		return (Integer) querySingle("selectCountMstPendapatan", mns_kd_nasabah);
	}
	
	public void insertNasabahInMstKebutuhan(String mns_kd_nasabah,int ljk_id, int mkb_no){
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("ljk_id", ljk_id);
		map.put("mkb_no", mkb_no);
		insert("insertNasabahInMstKebutuhan", map);
	}
	
	public void insertNasabahInMstPendapatan(String mns_kd_nasabah, int lsp_id, int lsp_in_out){
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("lsp_id", lsp_id);
		map.put("lsp_in_out", lsp_in_out);
		insert("insertNasabahInMstPendapatan", map);
	}
	
	public void updateMstAktivitas(String mns_kd_nasabah, int kd_aktivitas){
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("kd_aktivitas", kd_aktivitas);
		update("updateMstAktivitas", map);
	}
	
	public Integer selectCountMstJiffy(String mns_kd_nasabah){
		return(Integer) querySingle("selectCountMstJiffy", mns_kd_nasabah);
	}
	
	/**Fungsi:	Untuk Menginsert data pada tabel EKA.MST_AKTIVITAS
	 * @param	String kdNasabah, Integer pertKe, Integer kdAktivitas, 
	 * 			String keterangan,Integer status,Integer approval
	 * @author 	Ferry Harlim
	 */
	public void insertMstAktivitas(String kdNasabah,Integer pertKe,Integer kdAktivitas,
									String keterangan, Integer status, Integer approval){
		Map param=new HashMap();
		param.put("kd_nasabah",kdNasabah);
		param.put("pert_ke",pertKe);
		param.put("kd_aktivitas",kdAktivitas);
		param.put("keteragan",keterangan);
		param.put("status",status);
		param.put("approval",approval);
		insert("insertMstAktivitas", param);
	}
	
	public void insertMstJiffyKdNasabah(String mns_kd_nasabah){
		insert("insertMstJiffyKdNasabah", mns_kd_nasabah);
	}
	
	/**Fungsi 	Untuk Menghapus data pada tabel EKA.MST_AGENT_FA
	 * @param	String kdNasabah
	 * @author	Ferry Harlim
	 */
	public void deleteMstAgentFa(String kdNasabah){
		delete("deleteMstAgentFa", kdNasabah);
	}
	
	/**Fungsi:	Untuk Menginsert data pada tabel EKA.MST_AGENT_FA
	 * @param	String kdNasabah, String msagId, Integer levelId,Integer levCom, Integer flagJab
	 * @author	Ferry Harlim
	 */
	public void insertMstAgentFa(String kdNasabah, String msagId, 
								Integer leveld, Integer levCom, Integer flagJab){
		Map param=new HashMap();
		param.put("kdNasabah", kdNasabah);
		param.put("msagId", msagId);
		param.put("levelId", leveld);
		param.put("levCom", levCom);
		param.put("flagJab", flagJab);
		insert("insertMstAgentFa",param);
	}
	/**Fungsi:	Untuk Menginsert data pada tabel EKA.MST_NASABAH
	 * @param	Nasabah
	 * @author	Ferry Harlim
	 */
	public void insertMstNasabah(Nasabah nasabah){
		insert("insertMstNasabah",nasabah);
	}
	
	public void insertMstDetNasabah(Nasabah nasabah){
		insert("insertMstDetNasabah", nasabah);
	}
	
	public void insertLstReffBii(AddReffBii addReffBii){
		insert("insertLstReffBii",addReffBii);
	}
	
	public void insertMstKuesionerBrand(KuesionerBrand kbrand){
		insert("insertMstKuesionerBrand",kbrand);
	}
	
	
	public void insertLstCabangBii(String ls_kd_cabang, String nm_cabang, Integer level_id, Integer jenis, String kd_cabang,Integer jn_bank){
		Map map = new HashMap();
		map.put("ls_kd_cabang", ls_kd_cabang);
		map.put("nm_cabang", nm_cabang);
		map.put("level_id", level_id);
		map.put("jenis", jenis);
		map.put("kd_cabang", kd_cabang);
		map.put("jenis", jenis);
		insert("insertLstCabangBii", map);
	}
	
	public void insertLstReffBiiBSM(Integer lrb_id, String nm_agent, String lcb_no, Integer aktif, String kd_agent, Integer ref, Date tglAktif, Date tglNonAktif, String pos_code, Date tglUpdate, Integer jn_bank, String lus_id, String nik, String tnik ){
		Map map = new HashMap();
		map.put("lrb_id", lrb_id);
		map.put("nm_agent", nm_agent);
		map.put("lcb_no", lcb_no);
		map.put("aktif", aktif);
		map.put("kd_agent", kd_agent);
		map.put("ref", ref);
		map.put("tglAktif", tglAktif);
		map.put("tglNonAktif", tglNonAktif);
		map.put("pos_code", pos_code);
		map.put("tglUpdate", tglUpdate);
		map.put("jn_bank", jn_bank);
		map.put("lus_id", lus_id);
		map.put("nik", nik);
		map.put("tnik", tnik);
		insert("insertLstReffBiiBSM", map);
	}
	
	/**Fungsi:	Untuk Menginsert data pada tabel EKA.MST_NASABAH
	 * @param	Nasabah
	 * @return 	int (Banyaknya baris yang akan di update)
	 * @author	Ferry Harlim
	 */
	public int updateMstnasabah(Nasabah nasabah){
		return update("updateMstNasabah", nasabah);
	}
	
	public int updateMstDetnasabah(Nasabah nasabah){
		return update("updateMstDetNasabah", nasabah);
	}
	
	/**Fungsi:	Untuk Menampilkan nilai max kd_nasabah pada tabel EKA.MST_NASABAH
	 * @param	String kode
	 * @return	String
	 * @author	Ferry Harlim 
	 */
	public String selectMaxMstNasabahMnsKdNasabah(String kode){
		return (String)querySingle("selectMaxMstNasabahMnsKdNasabah", kode);
	}
	/**Fungsi:	Untuk Menampilkan jumlah record pada tabel EKA.MST_NASABAH
	 * 			berdasarkan mns_no_ref
	 * @param	String mnsNoRef
	 * @return	Integer
	 * @author 	Ferry Harlim
	 */
	public Integer selectCountMstNasabahMnsNoRef(String mnsNoRef){
		return(Integer)querySingle("selectCountMstNasabahMnsNoRef", mnsNoRef);
	}
	
	/**Fungsi:	Untuk Menampilkan record terakhir pada tabel EKA.MST_Aktivitas
	 * 			berdasarkan mns_kd_nasabah
	 * @param	String mns_kd_nasabah
	 * @return	Integer
	 * @author 	Deddy
	 */
	public Integer selectCountMstAktivitasPertKe(String mns_kd_nasabah){
		return(Integer)querySingle("selectCountMstAktivitasPertKe", mns_kd_nasabah);
	}
	
	public Integer selectCountMstMatrixAdv(String mns_kd_nasabah){
		return(Integer)querySingle("selectCountMstMatrixAdv", mns_kd_nasabah);
	}
	
	/**Fungsi:	Untuk Menampilkan level id dari tabel EKA.LST_JN_LEAD
	 * 			sesuai dengan ljlJenis
	 * @param	Integer ljlJenis
	 * @return	Integer
	 * @author 	Ferry Harlim
	 */
	public Integer selectLstJnLeadLevelId(Integer ljlJenis){
		return (Integer)querySingle("selectLstJnLeadLevelId", ljlJenis);
	}
	/**Fungsi:	Untuk Menampilkan data Jenis Alokasi Dana pada tabel EKA.MST_DET_ULINK
	 * @param	String spaj
	 * @return	List
	 * @author	Ferry Harlim
	 */
	public List selectDistinctMstDetUlinkLjiId(String spaj){
		return query("selectDistinctMstDetUlinkLjiId", spaj);
	}
	/**Fungsi:	Untuk Menampilkan data Jenis Alokasi Dana pada tabel EKA.MST_TRANS_ULINK
	 * @param	String spaj
	 * @return	List
	 * @author	Ferry Harlim
	 */
	public List selectDistinctMstTransUlinkLjiId(String spaj){
		return query("selectDistinctMstTransUlinkLjiId", spaj);
	}
	/**Fungsi:	Untuk Mengupdate kolom lar_email pada tabel EKA.LST_ADDR_REGION 
	 * @param	String larId,String larEmail;
	 * @author  Ferry Harlim
	 * @return 
	 */
	public void updateLstAddrRegionLarEmail(String larId, String larEmail){
		Map map=new HashMap();
		map.put("LAR_ID", larId);
		map.put("LAR_EMAIL", larEmail);
		update("updateLstAddrRegionLarEmail", map);
	}
	/**Fungsi:	Untuk Mengecek apakah spaj tersebut terdapat dalam tabel EKA.MST_REINS
	 * @param	String spaj
	 * @return 	String
	 * @author 	Ferry Harlim
	 */
	public String selectMstReinsSpaj(String spaj){
		return (String)querySingle("selectMstReinsSpaj", spaj);
	}
	/**Fungsi: 	Untuk menampilkan lsdbs_number dari suatu plan berdasarkan lsbs_id dan spaj
	 * @param	String spaj, Integer lsbsId
	 * @return	Integer
	 * @author	Ferry Harlim
	 */
	public Integer selectMstProductInsuredLsdbsNumber(String spaj,Integer lsbsId){
		Map param=new HashMap();
		param.put("spaj", spaj);
		param.put("lsbs_id",lsbsId);
		return (Integer)querySingle("selectMstProductInsuredLsdbsNumber", param);
	}
	
	/**Fungsi:	Untuk Menampilkan jenis nasabah link
	 * @return	List
	 * @author	Ferry Harlim
	 */
	public List selectLstJnNasabah()throws DataAccessException{
		return query("selectLstJnNasabah",null);
	}
	
	public Integer selectFlagLeadJnNasabah(Integer jn_nasabah){
		return (Integer)querySingle("selectFlagLeadJnNasabah", jn_nasabah);
	}
	
	public Integer selectCountReferralNumber()throws DataAccessException{
		return (Integer)querySingle("selectCountReferralNumber", null);
	}
	
	public Integer selectGenerateLrbId()throws DataAccessException{
		return (Integer)querySingle("selectGenerateLrbId", null);
	}
	
	public Integer selectCountKodeAgent(String kode_agent){
		return(Integer) querySingle("selectCountKodeAgent", kode_agent);
	}
	
	public Integer selectCountLstCabangBiiByKode(String kd_cabang,Integer jn_bank){
		Map m=new HashMap();
		m.put("kd_cabang", kd_cabang);
		m.put("jn_bank", jn_bank);
		return(Integer) querySingle("selectCountLstCabangBiiByKode", m);
	}
	
	public Integer selectCountLstReffBiiByKode(String kd_agent, Integer jn_bank){
		Map m=new HashMap();
		m.put("kd_agent", kd_agent);
		m.put("jn_bank", jn_bank);		
		return(Integer) querySingle("selectCountLstReffBiiByKode", m);
	}
	
	public Integer selectLstReffBiiMaxLrbId(){
		return(Integer) querySingle("selectLstReffBiiMaxLrbId", null);
	}
	
	public String selectLstReferralBiiLcbNo(String kd_cabang,Integer jn_bank){
		Map m=new HashMap();
		m.put("kd_cabang", kd_cabang);
		m.put("jn_bank", jn_bank);
		return(String) querySingle("selectLstReferralBiiLcbNo", m);
	}
	
	public Integer selectCountLstCabangBii(String kd_cabang,Integer jn_bank){
		Map m=new HashMap();
		m.put("kd_cabang", kd_cabang);
		m.put("jn_bank", jn_bank);
		return(Integer) querySingle("selectCountLstCabangBii", m);
	}
	
	public Integer selectCountTotalRider(String reg_spaj){
		return(Integer) querySingle("selectCountTotalRider", reg_spaj);
	}
	
	public Integer selectCountMstSurrender(String reg_spaj){
		return(Integer) querySingle("selectCountMstSurrender", reg_spaj);
	}
	
	public List<Map> selectPremiRiderInMstRiderSave(String reg_spaj){
		return query("selectPremiRiderInMstRiderSave", reg_spaj);
	}
	
	public Integer selectCountNewBusiness(String reg_spaj){
		return(Integer) querySingle("selectCountNewBusiness", reg_spaj);
	}
	
	public Integer selectMstPowerSaveRoCaraBayar(String reg_spaj){
		return(Integer) querySingle("selectMstPowerSaveRoCaraBayar", reg_spaj);
	}
	
	public String selectCountRegSpaj(String mns_kd_nasabah){
		return(String)querySingle("selectCountRegSpaj", mns_kd_nasabah);
	}
	
	/**Fungsi : Untuk Menginsert ke tabel eka.mst_client_history
	 * @author	Ferry Harlim
	 */
	public void insertMstClientHistory(ClientHistory clientHistory)throws Exception{
		insert("insertMstClientHistory", clientHistory);
	}
	
	public Integer selectMaxUrutMstClientHistory(String nopolis){
		return (Integer)querySingle("selectMaxUrutMstClientHistory", nopolis);
	}
	
	public List<Map> selectLstLsbsIDInMatrix(){
		return (List<Map>) query("selectLstLsbsIDInMatrix", null);
	}
	
	public List selectRegSpajFromNoPolis(){
		return query("selectRegSpajFromNoPolis",null);
	}
	public Map selectDataPpAndTt(String nopolis){
		return (HashMap)querySingle("selectDataPpAndTt",nopolis);
	}
	public List selectListMCL_ID(String spaj){
		return query("selectListMCL_ID",spaj);
	}
	
	public void updateMstClientHistory(Map data){
		update("updateMstClientHistory",data);
	}
	
	public List selectDataRiderHCP(String begDateAwal,String begDateAkhir){
		Map param=new HashMap();
		param.put("begDateAwal", begDateAwal);
		param.put("begDateAkhir", begDateAkhir);
		return query("selectDataRiderHCP",param);
	}
	
	public List selectDataRiderLinkNonHCP(String begDateAwal,String begDateAkhir){
		Map param=new HashMap();
		param.put("begDateAwal", begDateAwal);
		param.put("begDateAkhir", begDateAkhir);
		return query("selectDataRiderLinkNonHCP",param);
	}
	
	public void insertMReasTempNew(Map map){
		insert("insertMReasTempNew",map);
	}
	
	public DataRiderLink selectDataReasProdukUtama(String spaj){
		return (DataRiderLink) querySingle("selectDataReasProdukUtama",spaj);
	}
	public DataRiderLink selectDataReasProdukRiderLink(DataRiderLink data){
		return (DataRiderLink) querySingle("selectDataReasProdukRiderLink",data); 
	}
	public List selectRegSpajFromMReasTempNew(){
		return query("selectRegSpajFromMReasTempNew",null);
	}
	
	public String selectBegDatePowerSave(String spaj){
		return (String) querySingle("selectBegDatePowerSave",spaj);
	}
	
	public List selectMstProductionRiderLink(String tglAwal,String tglAkhir){
		Map map=new HashMap();
		map.put("awal", tglAwal);
		map.put("akhir",tglAkhir);
		return query("selectMstProductionRiderLink",map);
	}
	
	public List selectMReasTempNew(String spaj,BigDecimal lsbsId,BigDecimal lsdbsNumber,Integer flag){
		Map param=new HashMap();
		param.put("reg_spaj", spaj);
		param.put("lsbs_id", lsbsId);
		param.put("lsdbs_number", lsdbsNumber);
		param.put("flag", flag);
		return query("selectMReasTempNew",param);
	}	
	
	public void updateMstClientHistoryTgl(ClientHistory clientHistory){
		update("updateMstClientHistoryTgl", clientHistory);
	}
	/**
	 * Fungsi : Untuk Mengupdate Flag ins dari tabel eka.m_reas_temp_new 
	 * 			dmana flag_ins=1 baru di insert tetapi blom di proses
	 * 				  flag_ins=0 sudah di buattin reporting khsuus spaj 
	 * 		          yang production date <15 januari 2008.
	 */
	public void updateMReasTempNewFlagIns(Integer i) {
		update("updateMReasTempNewFlagIns",i);
		
	}
	public void updateMstNasabahLspdId(String mns_kd_nasabah, Integer lspd_id){
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("lspd_id", lspd_id);
		update("updateMstNasabahLspdId", map);
	}
	
	public void updateMstNasabahLsbsId(String mnskd_nasabah, Integer lsbs_id){
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mnskd_nasabah);
		map.put("lsbs_id", lsbs_id);
		update("updateMstNasabahLsbsId", map);
	}
	
	/**@Fungsi:	Untuk Menampilkan data jiffy pada tabel
	 * 			EKA.MST_JIFFY sesuai dengan kode nasabah
	 * @param 	String kdNasabah
	 * @return 	JIFFY
	 * @author 	Ferry Harlim
	 */
	public Jiffy selectMstJiffy(String kdNasabah){
		return (Jiffy)querySingle("selectMstJiffy", kdNasabah);
	}
	
	public List selectListBii()throws DataAccessException{
		return query("selectListBii", null);
	}
	
	public List selectListRekomendasi()throws DataAccessException{
		return query("selectListRekomendasi", null);
	}
	
	public List selectListProdBank()throws DataAccessException{
		return query("selectListProdBank", null);
	}
	
	public List selectListAktivitas()throws DataAccessException{
		return query("selectListAktivitas", null);
	}
	
	public void updateMstjiffy(Jiffy jiffy){
		update("updateMstJiffy", jiffy);
	}
	
	public void updateMstKebutuhan(Kebutuhan kebutuhan){
		update("updateMstKebutuhan", kebutuhan);
	}
	
	public void updateMstPendapatan(Pendapatan pendapatan){
		update("updateMstPendapatan", pendapatan);
	}
	
	public void insertMstRelasiNasabah(RelasiNasabah relasiNasabah) throws DataAccessException{
		insert("insertMstRelasiNasabah", relasiNasabah);
	}
	
	public void insertMstProdBank(ProdBank prodBank) throws DataAccessException{
		insert("insertMstProdBank", prodBank);
	}
	
	public void insertMstAktivitasUsingModel(Aktivitas aktivitas) throws DataAccessException{
		insert("insertMstAktivitasUsingModel", aktivitas);
	}
	
	public void updateRegSpajIntoNasabah(String mns_kd_nasabah, String reg_spaj, Integer jn_lead) throws DataAccessException{
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("reg_spaj", reg_spaj);
		map.put("jn_lead", jn_lead);
		update("updateRegSpajIntoNasabah", map);
		
		
	}
	
	public void updateMstProdBank(ProdBank prodBank) throws DataAccessException{
		update("updateMstProdBank", prodBank);
	}
	
	public void updateMstAktivitasUsingModel(Aktivitas aktivitas) throws DataAccessException{
		update("updateMstAktivitasUsingModel", aktivitas);
	}
	
	public void updateMstRelasiNasabah(RelasiNasabah relasiNasabah)throws DataAccessException{
		update("updateMstRelasiNasabah",relasiNasabah);
	}
	
	public void updateMstMatrix(Matrix matrix)throws DataAccessException{
		update("updateMstMatrixAdv", matrix);
	}
	
	public void updateMstRekomendasi(Rekomendasi rekomendasi) throws DataAccessException{
		update("updateMstRekomendasi", rekomendasi);
	}
	
	public void updateMstAspirasi(Aspirasi aspirasi) throws DataAccessException{
		update("updateMstAspirasi", aspirasi);
	}
	
	public void insertMstChildren(Children children) throws DataAccessException{
		insert("insertMstChildren", children);
	}
	
	public void updateMstChildren(Children children) throws DataAccessException{
		update("updateMstChildren", children);
	}
	
	public void deleteMstRelasiNasabahByNo(String mns_kd_nasabah, int mrn_no_relasi) throws DataAccessException{
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("mrn_no_relasi", mrn_no_relasi);
		delete("deleteMstRelasiNasabahByNo", map);
	}
	
	public void deleteMstProdBankByNo(String mns_kd_nasabah, int mpb_no) throws DataAccessException{
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("mpb_no", mpb_no);
		delete("deleteMstProdBankByNo", map);
	}
	
	public void deleteMstAktivitasByPertKe(String mns_kd_nasabah, int pert_ke) throws DataAccessException{
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("pert_ke", pert_ke);
		delete("deleteMstAktivitasByPertKe", map);
	}
	
	public void updateMstRelasiNasabahRow(String mns_kd_nasabah, int mrn_no_relasiafter, int mrn_no_relasibefore)throws DataAccessException{
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("mrn_no_relasiafter", mrn_no_relasiafter);
		map.put("mrn_no_relasibefore", mrn_no_relasibefore);
		update("updateMstRelasiNasabahRow", map);
	}
	
	public void updateMstProdBankRow(String mns_kd_nasabah, int mpb_noafter, int mpb_nobefore)throws DataAccessException{
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("mpb_noafter", mpb_noafter);
		map.put("mpb_nobefore", mpb_nobefore);
		update("updateMstProdBankRow", map);
	}
	
	public void deleteMstChildrenByNo(String mns_kd_nasabah, int mch_id) throws DataAccessException{
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("mch_id", mch_id);
		delete("deleteMstChildrenByNo", map);
	}
	
	public void updateMstChildrenRow(String mns_kd_nasabah, int mch_idafter, int mch_idbefore)throws DataAccessException{
		Map map = new HashMap();
		map.put("mns_kd_nasabah", mns_kd_nasabah);
		map.put("mch_idafter", mch_idafter);
		map.put("mch_idbefore", mch_idbefore);
		update("updateMstChildrenRow", map);
	}
	
	public Integer selectLsbsIdInMstRekomendasi(String mns_kd_nasabah) throws DataAccessException{
		return (Integer) querySingle("selectLsbsIdInMstRekomendasi", mns_kd_nasabah);
	}
	
	public List<Kebutuhan> selectMstKebutuhanPlusLjkKet(String mns_kd_nasabah) throws DataAccessException{
		return query("selectMstKebutuhanPlusLjkKet", mns_kd_nasabah);
	}
	
	public List<ProdBank> selectMstProdBankPlusLpbKet(String mns_kd_nasabah) throws DataAccessException{
		return query("selectMstProdBankPlusLpbKet", mns_kd_nasabah);
	}
	
	public List<Pendapatan> selectMstPendapatanPlusLspKet(String mns_kd_nasabah)throws DataAccessException{
		return query("selectMstPendapatanPlusLspKet", mns_kd_nasabah);
	}
	
	public List<Aktivitas> selectMstAktivitasNext(String mns_kd_nasabah)throws DataAccessException{
		return query("selectMstAktivitasNext", mns_kd_nasabah);
	}
	
	public List<Aktivitas> selectMstAktivitas(String mns_kd_nasabah)throws DataAccessException{
		return query("selectMstAktivitas", mns_kd_nasabah);
	}
	
	public Integer selectCountMstRekomendasi(String mns_kd_nasabah)throws DataAccessException{
		return(Integer) querySingle("selectCountMstRekomendasi", mns_kd_nasabah);
	}
	
	public void updateMstSurplusCalc(SurplusCalc surplusCalc){
		update("updateMstSurplusCalc", surplusCalc);
	}
	
	public void updateMstProtectCalc(ProtectCalc protectCalc){
		update("updateMstProtectCalc", protectCalc);
	}
	
	public void updateMstKuesionerBrand(KuesionerBrand kbrand){
		update("updateMstKuesionerBrand", kbrand);
	}
	
	public void updateMstIncomeCalc(IncomeCalc incomeCalc){
		update("updateMstIncomeCalc", incomeCalc);
	}
	
	public List<Map> selectLstReferrerLeader(String referrer_id) throws DataAccessException {
		Map param=new HashMap();
		param.put("referrer_id",referrer_id);
		return query("selectLstReferrerLeader", param);
	} 
	
	public List<Map> selectLstReferrerBiiWithId(String referrer_id) throws DataAccessException {
		Map param=new HashMap();
		param.put("referrer_id",referrer_id);
		return query("selectLstReferrerBiiWithId", param);
	} 
	
	public List selectLstRefLeader(String referrer_id){
		Map param=new HashMap();
		param.put("referrer_id", referrer_id);
		return query("selectLstRefLeader",param);
	}
	
	public String selectRefLeader(String referrer_id){
		Map param=new HashMap();
		param.put("referrer_id", referrer_id);
		return (String) querySingle("selectRefLeader",param);
	}
	
	public List selectLstReferrerLeader1(String referrer_id){
		Map param=new HashMap();
		param.put("referrer_id", referrer_id);
		return query("selectLstReferrerLeader1",param);
	}
	
	public List selectLstReferrerLeader2(String referrer_id){
		Map param=new HashMap();
		param.put("referrer_id", referrer_id);
		return query("selectLstReferrerLeader2",param);
	}
	
	public List selectLstReferrerLeader3(String referrer_id){
		Map param=new HashMap();
		param.put("referrer_id", referrer_id);
		return query("selectLstReferrerLeader3",param);
	}
	
	public List<Map> selectCabangBsmByWilayah(String lcb_no) throws DataAccessException{
		return getSqlMapClientTemplate().queryForList("elions.summary_akseptasi.selectCabangBsmByWilayah", lcb_no);
		
	}
	
	public void insertTaxCurrency(Map param) throws DataAccessException{
		insert("insertTaxCurrency", param);
	}	
	
	public List<Surat> selectDaftarSk(String no_surat, String judul, String jenisSurat, String thUpdate, String lstBln, int page, int pageSize, String filter, String filterEx, String nik) throws DataAccessException{
		Map m = new HashMap();
		m.put("no_surat", no_surat);
		if("".equals(no_surat)){
			m.put("judul", judul);
			if(!"".equals(jenisSurat)){
				m.put("jenisSurat", jenisSurat);
			}
			if(!"".equals(thUpdate)){
				if("".equals(lstBln)){
					m.put("thUpdateAll", thUpdate);
				}else{
					m.put("thUpdate", thUpdate);
				}
			}
		}
		if("".equals(lstBln)){
			m.put("lstBln", "12");
		}else{
			m.put("lstBln", lstBln);
		}
		m.put("page", page);
		m.put("pageSize", pageSize);
		m.put("filter", filter);
		m.put("filterEx", filterEx);
		m.put("nik", nik);
		return query("selectDaftarSk", m);
	}
	
	public void insertDaftarSk(String no_surat, String ynCheckRegional, String ynCheckAkm) throws DataAccessException{
		Map m = new HashMap();
		m.put("surat_id", no_surat);
		if(ynCheckRegional == null)ynCheckRegional = "0";
		if(ynCheckAkm == null)ynCheckAkm = "0";
		m.put("regional_flag", ynCheckRegional);
		m.put("akm_flag", ynCheckAkm);
		insert("insertDaftarSk", m);
	}
	
	public int updateDaftarSk(String no_surat, String ynCheckRegional, String ynCheckAkm) throws DataAccessException{
		Map m = new HashMap();
		m.put("surat_id", no_surat);
		if(ynCheckRegional == null)ynCheckRegional = "0";
		if(ynCheckAkm == null)ynCheckAkm = "0";
		m.put("regional_flag", ynCheckRegional);
		m.put("akm_flag", ynCheckAkm);
		return (int) update("updateDaftarSk", m);
	}
	
	public int updateDaftarSkAgency(String no_surat, String ynCheckAgency) throws DataAccessException{
		Map m = new HashMap();
		m.put("surat_id", no_surat);
		if(ynCheckAgency == null)ynCheckAgency = "0";
		m.put("is_agency", ynCheckAgency);
		return (int) update("updateDaftarSkAgency", m);
	}
	
	public void deleteDaftarSk(String no_surat) throws DataAccessException{
		Map m = new HashMap();
		m.put("surat_id", no_surat);
		delete("deleteDaftarSk", m);
	}
	
	public String selectLastPageDaftarSk(String no_surat, String judul, String jenisSurat, String thUpdate, String lstBln, int page, int pageSize, String filter, String nik) throws DataAccessException{
		Map m = new HashMap();
		m.put("no_surat", no_surat);
		m.put("judul", judul);
		if("".equals(no_surat)){
			m.put("judul", judul);
			if(!"".equals(jenisSurat)){
				m.put("jenisSurat", jenisSurat);
			}
			if(!"".equals(thUpdate)){
				if("".equals(lstBln)){
					m.put("thUpdateAll", thUpdate);
				}else{
					m.put("thUpdate", thUpdate);
				}
			}
		}
		if("".equals(lstBln)){
			m.put("lstBln", "12");
		}else{
			m.put("lstBln", lstBln);
		}
		m.put("page", page);
		m.put("pageSize", pageSize);
		m.put("filter", filter);
		m.put("nik", nik);
		return (String) querySingle("selectLastPageDaftarSk", m);
	}
	
	public List<Result> selectLaporanSaveSeries(String beg_date, String end_date)throws DataAccessException{
		Map param = new HashMap();
		param.put("beg_date", beg_date);
		param.put("end_date", end_date);
		return query("selectLaporanSaveSeries", param);
	}
	
	
	public List<Result> selectLaporanBIIBSM(String beg_date, String end_date,String bank)throws DataAccessException{
		Map param = new HashMap();
		param.put("beg_date", beg_date);
		param.put("end_date", end_date);
		param.put("bank", bank);
		return query("selectLaporanBIIBSM", param);
	}
	
	public List<SpajDet> selectMstSpajDetBasedBlanko( String no_blanko )throws DataAccessException{
		 return (List<SpajDet>) query("selectMstSpajDetBasedBlanko", no_blanko);
	}
	
	public Integer agenIsExist(String msag_id, String birth_date) throws DataAccessException {
		Map param = new HashMap();
		param.put("msag_id", msag_id);
		param.put("birth_date", birth_date);
		
		return (Integer) querySingle("agenIsExist", param);
	}
	
	public void insertMstAgentAbsen(String msag_id, String lus_id) throws DataAccessException {
		Map param = new HashMap();
		param.put("msag_id", msag_id);
		param.put("lus_id", lus_id);
		
		insert("insertMstAgentAbsen", param);
	}
	
	public Integer updateMstAgentAbsen(String msag_id, String lus_id) throws DataAccessException {
		Map param = new HashMap();
		param.put("msag_id", msag_id);
		param.put("lus_id", lus_id);
		
		return update("updateMstAgentAbsen", param);
	}	
	
	public Integer selectAbsenAgen(String msag_id) throws DataAccessException {
		
		Map data = (Map) querySingle("selectAbsenAgent", msag_id);
		if(data.get("JAM_KELUAR") == null) return 1;
		return 2;
	}
	
	public List<Absen> selectMstAgentAbsen(Integer tipe, String lus_id) throws DataAccessException {
		Map param = new HashMap();
		param.put("tipe", tipe);
		param.put("lus_id", lus_id);
		
		return query("selectMstAgentAbsen", param);
	}
	
	public List<DropDown> selectLsAgentAbsen(String lus_id) throws DataAccessException {
		return query("selectLsAgentAbsen", lus_id);		
	}
	
	public List<DropDown> selectLsAdminActive() throws DataAccessException {
		return query("selectLsAdminActive", null); 
	}
	
	public KuesionerBrand selectKuesionerBrand(String lus_id) throws DataAccessException {
		return (KuesionerBrand) querySingle("selectKuesionerBrand", lus_id); 
	}
	
	public void insertMstSchedulerHist(Map m) throws DataAccessException {
		insert("insertMstSchedulerHist", m);
	}
	
	public List selectNik(String nik){
		   Map param=new HashMap();
		   param.put("nik", nik);
		   return query("selectNik", param);
	   }
	
	public Integer selectExistingNik(String nik){
		 
		 return (Integer) querySingle("selectExistingNik",nik);
		
	}
	
	public Integer updateNik(String nik,String lus_id) throws DataAccessException{
		Map param = new HashMap();
		param.put("nik",nik);
		param.put("lus_id", lus_id);
		
		
		return update("updateNik", param);
	}
	
	public void insertMstHistDownload(Map map) throws DataAccessException{
		insert("insertMstHistDownload", map);
	}
	
	public String selectLusCoverLetterPositionSpaj(String reg_spaj){
		return (String) querySingle("selectLusCoverLetterPositionSpaj", reg_spaj);
	}
	
	public List<String> selectAllNoVoucherTaxi(User currentUser) throws DataAccessException {
		HashMap<String, Object> params = new HashMap<String, Object>();
		if(!"01".equals(currentUser.getLde_id()))
			params.put("lus_id", currentUser.getLus_id());
		
		return query("selectAllNoVoucherTaxi", params);
	}
	
	public void insertVoucherTaxi(VoucherTaxi voucherTaxi, String lus_id) throws DataAccessException {
		voucherTaxi.setLus_id(lus_id);
		//voucherTaxi.setMsvt_insert_date((Date) querySingle("selectSysdate", null));
		voucherTaxi.setMsvt_insert_date((Date) selectSysdate());
		insert("insertVoucherTaxi", voucherTaxi);
	}
	
	public VoucherTaxi selectVoucherTaxi(String msvt_no) throws DataAccessException {
		VoucherTaxi result = (VoucherTaxi) querySingle("selectVoucherTaxi", msvt_no);
		if(result == null) result = new VoucherTaxi();
		
		return result;
	}
	
	public void updateVoucherTaxi(VoucherTaxi voucherTaxi, String lus_id) throws DataAccessException {
		voucherTaxi.setLus_id_edit(lus_id);
		voucherTaxi.setMsvt_edit_date((Date) selectSysdate());
		update("updateVoucherTaxi", voucherTaxi);
	}
	
	public List selectReportVoucherTaxi(HashMap<String, Object> params) throws DataAccessException {
		return query("selectReportVoucherTaxi", params);
	}
	
	public List selectMarketing(HashMap<String, Object> params) throws DataAccessException {
		return query("selectMarketing", params);
	}
	
	public String selectNoDuplikatUlangan(String spaj, String flag) throws DataAccessException {
		Map params = new HashMap();
		params.put("spaj", spaj);
		params.put("flag", flag);
		return (String) querySingle("selectNoDuplikatUlangan", params);
	}

	public List selectDaftarNamaPegawai(String nama) {		
		
		return query("selectDaftarNamaPegawai",nama);
	}

	public List selectDaftarDetaiNamaPegawai(String nama) {
		return query("selectDaftarDetaiNamaPegawai",nama);
	}
		
	public void insertDataSuratTsr(HashMap map) {
		insert("insertDataSuratTsr", map);	
	}
	
	public HashMap selectMstUrlSecure(String id1, String id2) throws DataAccessException{
		Map params = new HashMap();
		params.put("id1", id1);
		params.put("id2", id2);
		return (HashMap) querySingle("selectMstUrlSecure", params);
	}
	
	public HashMap selectMstUrlSecure2(String no_polis, String link) throws DataAccessException{
		Map params = new HashMap();
		params.put("no_polis", no_polis);
		params.put("link", link);
		return (HashMap) querySingle("selectMstUrlSecure2", params);
	}
	
	public void insertMstUrlSecure(String id1, String id2, String no_polis, String link) {
		Map params = new HashMap();
		params.put("id1", id1);
		params.put("id2", id2);
		params.put("no_polis", no_polis);
		params.put("link", link);
		insert("insertMstUrlSecure", params);
	}
	
	public void deleteMstUrlSecure(String no_polis, String flag) throws DataAccessException {
		Map params = new HashMap();
		params.put("no_polis", no_polis);
		params.put("flag", flag);
		delete("deleteMstUrlSecure", params);
	}	

	public void updateDataPolicy(String nopol, String s_update, String s_jenisEdit) {
		Map param = new HashMap();
		param.put("nopol", nopol);
		param.put("id", s_jenisEdit);
		param.put("s_update", s_update);
		update("updateDataPolicy", param);		
	}
	
	//rds phase3 iga update filling
	public String selectMspoPolicyNo(String nopol){
		return (String) querySingle("selectMspoPolicyNo", nopol);
	}
	
	//rds phase3 iga update filling
	public void updateLspdIdWhereNopol(String polis) {
		Map param = new HashMap();
		param.put("polis", polis);
		update("updateLspdIdWhereNopol", param);		
	}

//	public void updateMstUrlSecure(String no_polis, String flag, String link) throws DataAccessException {
//		//if(flag.equals("5")) link = "flag=6";
//		Map params = new HashMap();
//		params.put("no_polis", no_polis);
//		params.put("flag", flag);
//		params.put("link", link);
//		update("updateMstUrlSecure", params);
//	}
	
	public List selectDynamicReportCs(HashMap<String, Object> param) {
		return query("selectDynamicReportCs", param);
	}

	public void insertDataJne(HashMap map)  throws DataAccessException{
		insert("insertDataJne", map);
	}

	public Integer selectJumlahConnote(String spaj) {
		
		return (Integer) querySingle("selectJumlahConnote", spaj);
	}

	public void updateDataJne(HashMap map) throws DataAccessException {
		update("updateDataJne", map);
		
	}

	public List selectSPAJByConnote(String s_connote )throws DataAccessException {
		return query("selectSPAJByConnote", s_connote);
	}

	public void updateNikWorksite(String reg_spaj, String nik) {
		Map param = new HashMap();
		param.put("spaj", reg_spaj);
		param.put("nik", nik);
		update("updateDataNIK", param);		
		
	}
	
	public Integer selectCountCabangBsmByKota(String kota) throws DataAccessException {
		return (Integer) querySingle("selectCountCabangBsmByKota", kota);
	}
	
	public String selectLspdId(String reg_spaj){
		return (String) querySingle("selectLspdId", reg_spaj);
	}

	public Integer selectCountTmid(String nik, String cc_code) {		
		Map m=new HashMap();
		m.put("nik", nik);
		m.put("cc_code", cc_code);		
		return(Integer) querySingle("selectCountTmid", m);
	}

	public void insertLstSalesTm(String nik, String cc_code, String nm_agent, Integer aktif) {
		Map m=new HashMap();
		m.put("nik", nik);
		m.put("cc_code", cc_code);
		m.put("nm_agent", nm_agent);
		m.put("aktif", aktif);
		insert("insertLstSalesTm", m);
	}

	public void updateLstSalesTm(String nik, String cc_code, String nm_agent, Integer aktif) {
		Map m=new HashMap();
		m.put("nik", nik);
		m.put("cc_code", cc_code);
		m.put("nm_agent", nm_agent);
		m.put("aktif", aktif);	
		update("updateLstSalesTm", m);
	}
}