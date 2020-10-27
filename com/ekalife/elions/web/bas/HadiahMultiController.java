package com.ekalife.elions.web.bas;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.App;
import com.ekalife.elions.model.Hadiah;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.elions.process.Sequence;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentMultiController;
import com.google.gson.Gson;

import id.co.sinarmaslife.std.model.vo.DropDown;
/**
 * Form Inputan Hadiah pembelian polis (dipakai oleh admin/bancass, purchasing, dan finance). Detail flow ada dibawah
 * 
Posisi	Desc						PIC				Detail			
84		INPUT IDENTIFIKASI BARANG	BANCASS SUPPORT	admin input, transfer ke purchasing + memo 1			
85		PEMBELIAN BARANG			PURCHASING		purchasing aksep, buat memo 2 utk head purchasing, lalu transfer ke finance + memo 3			
86		PEMBAYARAN BARANG			FINANCE			finance lakukan pembayaran, lalu transfer balik ke purchasing + berikan BSB (via email only)			
87		KONFIRMASI PEMBAYARAN		PURCHASING		purchasing melakukan konfirmasi pembayaran + minta barang dikirim (bisa direct ke nasabah, atau ke purchasing dulu)			
88		TERIMA BARANG DARI VENDOR	PURCHASING		bila barang dikirim ke AJS dulu, maka purchasing konfirmasi terima barang, lalu masuk ke step berikutnya untuk kirim ke nasabah			
89		PENGIRIMAN BARANG KE NSBH	PURCHASING		barang sudah dikirim OTW ke nasabah (baik dari AJS atau lgsg dari vendor), lalu transfer ke penerimaan barang			
90		PENERIMAAN BARANG OLEH NSBH	PURCHASING		tanda terima barang oleh nasabah sudah diterima purchasing, transfer ke filling			
99		FILLING						PURCHASING		selesai			
95		CANCELLED					PURCHASING		pembatalan (bisa dibatalkan bila belum paid)			
 * 
 * @author Yusuf
 * @since 29 Nov 2011
 *
 */
public class HadiahMultiController extends ParentMultiController{
	
	protected final Log logger = LogFactory.getLog( getClass() );
	protected Sequence sequence;
	/**
	 * Method khusus untuk return data dalam bentuk JSON (untuk ajax)
	 * http://yusufxp/E-Lions/bas/hadiah.htm?window=json
	 * 
	 * @author Yusuf
	 * @throws ParseException 
	 * @since 29 Nov 2011
	 */
	public ModelAndView json(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException, ParseException {
		String tipe = ServletRequestUtils.getStringParameter(request, "t", "");
		Hadiah hadiah = new Hadiah();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Object result = null;
		
		// user memilih kategori hadiah, tampilkan pilihan hadiah nya
		if(tipe.equals("lhc")){
			int lhc_id = ServletRequestUtils.getRequiredIntParameter(request, "lhc_id");
			result = elionsManager.selectDropDown("EKA.LST_HADIAH", "LH_ID", "LH_NAMA", "", "LH_NAMA", "lhc_id = " + lhc_id);

		// user memilih hadiah, tarik beberapa nilai default (seperti harga)
		}else if(tipe.equals("lh")){
			int lhc_id = ServletRequestUtils.getRequiredIntParameter(request, "lhc_id");
			int lh_id = ServletRequestUtils.getRequiredIntParameter(request, "lh_id");
			result = uwManager.selectLstHadiah(lhc_id, lh_id);

		// user menekan view history
		}else if(tipe.equals("hist")){
			String reg_spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
			Integer mh_no = ServletRequestUtils.getRequiredIntParameter(request, "mh_no");
			result = uwManager.selectHadiahHist(reg_spaj, mh_no);
			
		//show tanggal history
		}else if(tipe.equals("tglHistory")){
			String reg_spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
			result = uwManager.selecttglHistory(reg_spaj);	

		// user menginput spaj/polis, tampilkan datanya
		}else if(tipe.equals("polis")){
			String spajPolis = ServletRequestUtils.getStringParameter(request, "spajPolis", "");
			if(spajPolis.equals("")) result = null;
			result = ajaxManager.selectDataPolisUntukInputHadiah(spajPolis);
			
		}else if(tipe.equals("polis2")){
			String spajPolis = ServletRequestUtils.getStringParameter(request, "spajPolis", "");
			if(spajPolis.equals("")) result = null;
			result = ajaxManager.selectDataPolisUntukInputHadiah2(spajPolis);
			
		}else if(tipe.equals("polis3")){
			String spajPolis = ServletRequestUtils.getStringParameter(request, "spajPolis", "");
			if(spajPolis.equals("")) result = null;
			result = ajaxManager.selectDataPolisUntukInputHadiah3(spajPolis);
	
		}else if(tipe.equals("cek")){
			String spajPolis = ServletRequestUtils.getStringParameter(request, "spajPolis", "");
			if(spajPolis.equals("")) result = null;
			result = ajaxManager.selectHadiahCek(spajPolis);

		// autocomplete untuk inputan kota (lst_kabupaten)
		}else if(tipe.equals("kota")){
			String term = ServletRequestUtils.getRequiredStringParameter(request, "term");
			result = elionsManager.selectDropDown("EKA.LST_KABUPATEN", "LSKA_NOTE", "LSKA_NOTE", "", "LSKA_NOTE", "LSKA_NOTE like '%" + term.toUpperCase().trim() + "%'");

		// autocomplete untuk inputan vendor (master_supplier)
		}else if(tipe.equals("supplier")){
			String term = ServletRequestUtils.getRequiredStringParameter(request, "term");
			result = ajaxManager.selectMasterSupplier(term.toUpperCase().trim());

		// autocomplete untuk inputan bank (lst_bank)
		}else if(tipe.equals("bank")){
			String term = ServletRequestUtils.getRequiredStringParameter(request, "term");
			result = ajaxManager.select_bank2(term.toUpperCase().trim());
		}
		
		//validation vendor
		else if(tipe.equals("vendor")){
			String vendorname = ServletRequestUtils.getStringParameter(request, "vendorname", "");
			if(vendorname.equals("")) result = null;
			result = ajaxManager.selectMasterSupplier(vendorname.toUpperCase().trim());
		}
		
		//validation bank
		else if(tipe.equals("val_bank")){
			String bankname = ServletRequestUtils.getStringParameter(request, "bankname", "");
			if(bankname.equals("")) result = null;
			result = ajaxManager.select_bank2(bankname.toUpperCase().trim());
		}
		
		//untuk press proses hadiah
		else if(tipe.equals("followup")){
			String begdate = ServletRequestUtils.getRequiredStringParameter(request, "begdate");
		    String enddate = ServletRequestUtils.getRequiredStringParameter(request, "enddate");
		    int selectPosisi = ServletRequestUtils.getRequiredIntParameter(request, "lspd_id");
		    String jenis = ServletRequestUtils.getRequiredStringParameter(request, "jenis");
		    String program_hadiah = ServletRequestUtils.getStringParameter(request, "program_hadiah","0");
		    logger.info("LSPD_ID = "+selectPosisi);
		    result = ajaxManager.selectPosisiProsesHadiah(begdate, enddate, selectPosisi, jenis, program_hadiah);
	    }
		
		else if(tipe.equals("rFrame")){
			String spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
			result = ajaxManager.selectInfoMstHadiah(spaj);
		}
		
		else if(tipe.equals("sel_hadiah")){
			String spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
			result = uwManager.selectHadiahStableSave(spaj);
		}
		
		else if(tipe.equals("transf")){
			String spaj = ServletRequestUtils.getRequiredStringParameter(request, "_spaj");
			int pos = ServletRequestUtils.getRequiredIntParameter(request, "lspdID");
			int mh = ServletRequestUtils.getRequiredIntParameter(request, "mh_no");
			hadiah.reg_spaj = spaj;
			hadiah.lspd_id = pos + 1;
			hadiah.mh_no = mh;
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String d = dateFormat.format(cal.getTime());
			Date sysdate = dateFormat.parse(d);				
			hadiah.mh_tgl_paid = sysdate;
			hadiah.keterangan = "Transfer Data Hadiah ke-" + hadiah.lspd_id;
			result = ajaxManager.updateProses(hadiah, currentUser);
		}
		// view detail
		else if(tipe.equals("fDetail")){
			String spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
			Integer mh = ServletRequestUtils.getRequiredIntParameter(request, "mh_no");
			String program_hadiah = ServletRequestUtils.getStringParameter(request, "program_hadiah","0");
			result = ajaxManager.viewDetailHadiah(spaj, mh, program_hadiah);
		}
		
		//user memilih kota
		else if(tipe.equals("wKota")){
			String kota = ServletRequestUtils.getStringParameter(request, "kota", "");
			
			List jam = new ArrayList();
			
			if(kota.toUpperCase().contains("JAKARTA")){
				for(int i=11;i<=17;i++){
					HashMap jm = new HashMap();
					jm.put("value",i);
					jm.put("label",i);
					
					jam.add(jm);
				}					
			}else if((kota.toUpperCase().contains("BEKASI") || kota.toUpperCase().contains("TANGERANG") || kota.toUpperCase().contains("DEPOK") || kota.toUpperCase().contains("BOGOR"))){
				for(int i=15;i<=17;i++){
					HashMap jm = new HashMap();
					jm.put("value",i);
					jm.put("label",i);
					
					jam.add(jm);
				}
			}else{
				for(int i=8;i<=17;i++){
					HashMap jm = new HashMap();
					jm.put("value",i);
					jm.put("label",i);
					
					jam.add(jm);
				}
			}
			
			result = jam;
		}
		
		if(result != null){
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(result));
			out.close();
		}
		
		return null;
	}
	
	/**
	 * Menu proses hadiah, untuk digunakan oleh user purchasing
	 * 
	 * http://yusufxp/E-Lions/bas/hadiah.htm?window=proses
	 * 
	 * @author Yusuf
	 * @since 29 Nov 2011
	 */	
	public ModelAndView proses(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Hadiah hadiah = new Hadiah();		

		//bind data
		ServletRequestDataBinder binder = createBinder(request, hadiah);

		//setup binding
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, completeDateEditor); //untuk date secara umum
		binder.registerCustomEditor(Date.class, "beg_date", completeDateEditor);
		binder.registerCustomEditor(Date.class, "end_date", completeDateEditor);
		binder.registerCustomEditor(Date.class, "tgl_aksep", completeDateEditor);		
		
		binder.bind(request);
		BindException err = new BindException(binder.getBindingResult());
		//
		String begdate = ServletRequestUtils.getStringParameter(request, "begdate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
		String enddate = ServletRequestUtils.getStringParameter(request, "enddate", defaultDateFormat.format(elionsManager.selectSysdate(30)));
		String tgl_proses = ServletRequestUtils.getStringParameter(request, "tgl_proses",defaultDateFormat.format(elionsManager.selectSysdate(0)));
	    
		//Reference Data
		List<DropDown> listPosisi = elionsManager.selectDropDown("EKA.LST_DOCUMENT_POSITION", "LSPD_ID", "LSPD_POSITION", "", "LSPD_ID", "LSPD_ID IN (84, 85, 86, 87, 88, 89, 90, 99, 95)");
		
		List<DropDown> listTanggal = new ArrayList<DropDown>();
		listTanggal.add(new DropDown("1", "Tanggal Input"));
		listTanggal.add(new DropDown("2", "Tanggal Akseptasi UW"));
		listTanggal.add(new DropDown("3", "Tanggal Mulai Berlaku Polis"));
		
		//Model Object
		Map<String, Object> cmd = new HashMap<String, Object>();
		cmd.put("begdate", begdate);
		cmd.put("enddate", enddate);
	    cmd.put("tgl_proses", tgl_proses);
		cmd.put("listPosisi", listPosisi);
		cmd.put("listTanggal", listTanggal);
		
		//proses transfer
		if(request.getParameter("btnTransfer") != null) {
			//bila tidak ada error, update data
			if(!err.hasErrors()) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				
				Calendar cal = Calendar.getInstance();
				
				String d = dateFormat.format(cal.getTime());
				String t = timeFormat.format(new Date());
				String tggl = ServletRequestUtils.getStringParameter(request, "tgl_proses");
				String tggl2 = ServletRequestUtils.getStringParameter(request, "tgl_pembayaran");
				
				
				Date sysdate = dateFormat.parse(d); // Posisi 85 dan 86
				String dt1 = tggl + " " + t;
				String dt2 = tggl2 + " " + t;
				Date tgl = dateFormat.parse(dt1); // posisi 87, 88, 89, dan 90 
				Date tgl_deadline = dateFormat.parse(dt1); //posisi 85->86
				//logger.info(tggl+" <-> "+dt1+" <-> "+cal+" <-> "+tgl);
				
				String selectPosisi = ServletRequestUtils.getStringParameter(request, "selectPosisi");
				
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				int lspdID = Integer.parseInt(request.getParameter("lspdID"));
				hadiah.lspd_id = lspdID;
				logger.info(lspdID);
				hadiah.reg_spaj = request.getParameter("spn");
				int mhno = Integer.parseInt(request.getParameter("mh_no"));
				hadiah.mh_no = mhno;
				hadiah.keterangan = "Transfer Data Hadiah ke-" + hadiah.lspd_id;				
				
				//kirim pemberitahuan ke BAS
				String to = to = props.getProperty("hadiah.email.bas");
				String[] emailTo = to.split(";");
				
				for(int y=0; y<emailTo.length; y++){
					emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
				}
				
				if("85".equals(selectPosisi)){
					hadiah.mh_tgl_aksep = sysdate;
					hadiah.mh_tgl_deadline_bayar = tgl_deadline;
					hadiah.pesan = ajaxManager.prosesHadiahMemo3(hadiah, currentUser);
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
				}else if("86".equals(selectPosisi)){
					hadiah.mh_tgl_paid = sysdate;
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
				}else if("87".equals(selectPosisi)){
					hadiah.mh_tgl_kirim_vendor = tgl;
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
				}else if("88".equals(selectPosisi)){
					hadiah.mh_tgl_terima_ajs = tgl;
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
				}else if("89".equals(selectPosisi)){
					hadiah.mh_tgl_kirim_ajs = tgl;
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
				}else if("90".equals(selectPosisi)){
					hadiah.mh_tgl_terima_nsbh = tgl;
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
				}
				
				email.send(true, props.getProperty("admin.ajsjava"), emailTo, null, null, "System Tracking", "Spaj dengan no "+hadiah.reg_spaj+" telah di transfer ke posisi "+hadiah.lspd_id, null);
			}
		}
		
		//proses cancel
		if(request.getParameter("btnCancel") != null) {
			//bila tidak ada error, update data
			if(!err.hasErrors()) {
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				
				hadiah.lspd_id = 95;
				hadiah.reg_spaj = request.getParameter("spn");
				int mhno = Integer.parseInt(request.getParameter("mh_no"));
				hadiah.mh_no = mhno;
				hadiah.pesan = ajaxManager.cancelProses(hadiah, currentUser);
				
				//kirim pemberitahuan ke BAS
				String to = to = props.getProperty("hadiah.email.bas");
				String[] emailTo = to.split(";");
				
				for(int y=0; y<emailTo.length; y++){
					emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
				}
				email.send(true, props.getProperty("admin.ajsjava"), emailTo, null, null, "System Tracking", "Spaj dengan no "+hadiah.reg_spaj+" telah di cancel (posisi 95)", null);
			}
		}
		
		if(request.getParameter("btnPrint") != null){
			if(!err.hasErrors()){
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				
				Calendar cal = Calendar.getInstance();
				
				String d = dateFormat.format(cal.getTime());
				String t = timeFormat.format(new Date());
				String tggl = ServletRequestUtils.getStringParameter(request, "tgl_pembayaran");
				
				Date sysdate = dateFormat.parse(d); // Posisi 85 dan 86
				String dt1 = tggl + " " + t;				
				
				Date tgl_pembayaran = dateFormat.parse(dt1); // posisi 85 -> 86
				
				hadiah.reg_spaj = request.getParameter("spn");
				hadiah.lhc_nama = request.getParameter("lhc");
				hadiah.lh_nama = request.getParameter("lh");
				hadiah.mh_tgl_deadline_bayar = tgl_pembayaran;
				hadiah.mh_quantity = Integer.valueOf(request.getParameter("qty"));
				hadiah.mh_harga = Double.valueOf(request.getParameter("hrg"));
				
				ajaxManager.prosesHadiahMemo2(hadiah, currentUser);
				
				String filename="Memo2_" + hadiah.reg_spaj +".pdf";
				String file = props.getProperty("pdf.dir.hadiah.memo")+""+hadiah.reg_spaj+"\\"+filename;
				File sourceFile = new File(file);
				FileInputStream in = null;
				ServletOutputStream ouputStream = null;
				try{
					
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "Attachment;filename="+filename);
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					
					in = new FileInputStream(sourceFile);
				    ouputStream = response.getOutputStream();
				    
				    IOUtils.copy(in, ouputStream);
				}catch(Exception e){
					logger.error("ERROR :", e);
				}finally{
					try {
		            	if(in != null) {
		            		in.close();
		            	}
		            	if(ouputStream != null) {
		            		ouputStream.flush();
		            		ouputStream.close();
		            	}  
		            } catch (Exception e) {
		                   // TODO Auto-generated catch block
		                   logger.error("ERROR", e);
		            }
				}
			}
			return null;
		}
		
		return new ModelAndView("bas/hadiah_proses", cmd);
	}
	
	/**
	 * Menu input hadiah, untuk digunakan untuk input awal (oleh admin/bancass)
	 * 
	 * http://yusufxp/E-Lions/bas/hadiah.htm?window=input
	 * 
	 * @author Yusuf
	 * @since 29 Nov 2011
	 */	
	public ModelAndView input(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String rs = ServletRequestUtils.getStringParameter(request, "rs", ""); //reg_spaj
		String tipe = ServletRequestUtils.getStringParameter(request, "t", "");
		
		//form backing object
		Hadiah hadiah;
		if(rs.equals("")){
			hadiah = new Hadiah();
			hadiah.mh_quantity = 1;
			hadiah.mh_flag_kirim = 0;
			hadiah.mh_no = 1;
		}else{
			hadiah = uwManager.selectMstHadiah(rs, 1); //saat ini baru bisa support 1 input saja
		}
		
		//bind data
		ServletRequestDataBinder binder = createBinder(request, hadiah);

		//setup binding
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, completeDateEditor); //untuk date secara umum
		binder.registerCustomEditor(Date.class, "beg_date", completeDateEditor);
		binder.registerCustomEditor(Date.class, "end_date", completeDateEditor);
		binder.registerCustomEditor(Date.class, "tgl_aksep", completeDateEditor);		
		
		binder.bind(request);
		BindException err = new BindException(binder.getBindingResult());

		//Reference Data
		List<DropDown> listHC = elionsManager.selectDropDown("EKA.LST_HADIAH_CAT", "LHC_ID", "LHC_NAMA", "", "LHC_NAMA", "");
		request.setAttribute("listHC", listHC);

		List<DropDown> listPosisi = elionsManager.selectDropDown("EKA.LST_DOCUMENT_POSITION", "LSPD_ID", "LSPD_POSITION", "", "LSPD_ID", "LSPD_ID IN (84, 85, 86, 87, 88, 89, 90, 99, 95)");
		request.setAttribute("listPosisi", listPosisi);
		
		List<DropDown> listH = new ArrayList<DropDown>();
		if(hadiah.lhc_id != null){
			listH = elionsManager.selectDropDown("EKA.LST_HADIAH", "LH_ID", "LH_NAMA", "", "LH_NAMA", "lhc_id = " + hadiah.lhc_id);
		}
		request.setAttribute("listH", listH);

		//user menekan tombol simpan
		if(request.getParameter("btnSave") != null) {
			//validasi
			Integer lspd = null;
			if(hadiah.lspd_id==null){
				lspd = 84;
			}else{
				lspd = hadiah.lspd_id;
			}
			logger.info("lspd_id "+lspd);
			if(lspd>=85){
				ValidationUtils.rejectIfEmpty(err, "reg_spaj", "", "No SPAJ harus diisi");
				ValidationUtils.rejectIfEmpty(err, "policy_no", "", "Polis harus diisi");
				ValidationUtils.rejectIfEmpty(err, "lhc_id", "", "Kategori Unit Hadiah harus diisi");
				ValidationUtils.rejectIfEmpty(err, "lh_id", "", "Nama Unit Hadiah harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_flag_kirim", "", "Jenis Pengiriman harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_quantity", "", "Jumlah Pembelian Unit harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_budget", "", "Budget Pembelian Unit harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_alamat", "", "Alamat harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_kodepos", "", "Kode Pos harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_kota", "", "Kota harus diisi");
				ValidationUtils.rejectIfEmpty(err, "supplier_name", "", "Nama Vendor harus diisi");
				ValidationUtils.rejectIfEmpty(err, "bank_name", "", "Nama Bank harus diisi");
			}else{
				ValidationUtils.rejectIfEmpty(err, "reg_spaj", "", "No SPAJ harus diisi");
				ValidationUtils.rejectIfEmpty(err, "policy_no", "", "Polis harus diisi");
				ValidationUtils.rejectIfEmpty(err, "lhc_id", "", "Kategori Unit Hadiah harus diisi");
				ValidationUtils.rejectIfEmpty(err, "lh_id", "", "Nama Unit Hadiah harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_flag_kirim", "", "Jenis Pengiriman harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_quantity", "", "Jumlah Pembelian Unit harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_budget", "", "Budget Pembelian Unit harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_alamat", "", "Alamat harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_kodepos", "", "Kode Pos harus diisi");
				ValidationUtils.rejectIfEmpty(err, "mh_kota", "", "Kota harus diisi");
			}
			
			//bila tidak ada error, simpan data
			if(!err.hasErrors()) {
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				hadiah.pesan = uwManager.saveHadiah(hadiah, currentUser);
			}
		}
		
		//user menekan tombol transfer
		if(request.getParameter("btnTransfer") != null) {
			//validasi
			ValidationUtils.rejectIfEmpty(err, "reg_spaj", "", "No SPAJ harus diisi");
			ValidationUtils.rejectIfEmpty(err, "policy_no", "", "Polis harus diisi");
			
			//bila tidak ada error, simpan data
			if(!err.hasErrors()) {
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				hadiah.pesan = uwManager.transferDocInputHadiah(hadiah, currentUser);
				
				//kirim pemberitahuan ke BAS
				String to = to = props.getProperty("hadiah.email.bas");
				String[] emailTo = to.split(";");
				
				for(int y=0; y<emailTo.length; y++){
					emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
				}
				email.send(true, props.getProperty("admin.ajsjava"), emailTo, null, null, "System Tracking", "Spaj dengan no "+hadiah.reg_spaj+" telah di transfer ke posisi "+hadiah.lspd_id, null);
			}
		}
		
		//user menekan tombol cancel
		if(request.getParameter("btnCancel") != null) {
			//validasi
			ValidationUtils.rejectIfEmpty(err, "reg_spaj", "", "No SPAJ harus diisi");
			ValidationUtils.rejectIfEmpty(err, "policy_no", "", "Polis harus diisi");
			
			//bila tidak ada error, simpan data
			if(!err.hasErrors()) {
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				hadiah.pesan = uwManager.cancelHadiah(hadiah, currentUser);
				
				//kirim pemberitahuan ke BAS
				kirimEmail(hadiah);
				/*String to = to = props.getProperty("hadiah.email.bas");
				String[] emailTo = to.split(";");
				
				for(int y=0; y<emailTo.length; y++){
					emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
				}
				email.send(true, props.getProperty("admin.ajsjava"), emailTo, null, null, "System Tracking", "Spaj dengan no "+hadiah.reg_spaj+" telah di cancel (posisi 95)", null);*/
			}
		}
		
		//user menekan tombol print
		if(request.getParameter("btnPrintMemo") != null) {
			//validasi
			ValidationUtils.rejectIfEmpty(err, "reg_spaj", "", "No SPAJ harus diisi");
			ValidationUtils.rejectIfEmpty(err, "policy_no", "", "Polis harus diisi");
			
			//bila tidak ada error, simpan data
			if(!err.hasErrors()) {
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				hadiah.pesan = uwManager.printMemoHadiah(hadiah, currentUser);
				
				//Download File
				String filename="Memo1_" + hadiah.reg_spaj +".pdf";
				String file = props.getProperty("pdf.dir.hadiah.memo")+""+hadiah.reg_spaj+"\\"+filename;
				File sourceFile = new File(file);
				FileInputStream in = null;
				ServletOutputStream ouputStream = null;
				try{
					
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "Attachment;filename="+filename);
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					
					in = new FileInputStream(sourceFile);
				    ouputStream = response.getOutputStream();
				    
				    IOUtils.copy(in, ouputStream);
				}catch(Exception e){
					logger.error("ERROR :", e);
				}finally{
					try {
		            	if(in != null) {
		            		in.close();
		            	}
		            	if(ouputStream != null) {
		            		ouputStream.flush();
		            		ouputStream.close();
		            	}  
		            } catch (Exception e) {
		                   // TODO Auto-generated catch block
		                   logger.error("ERROR", e);
		            }
				}
			}
			return null;
		}
		
		if(tipe.equals("sendInput")){
			String spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
			Map<String, Object> cmd = new HashMap<String, Object>();
			cmd.put("reg_spaj", spaj);
		}
		
		return new ModelAndView("bas/hadiah_input", err.getModel());
	}
	
	public ModelAndView upload(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String tipe = ServletRequestUtils.getStringParameter(request, "t", "");
		Hadiah hadiah = new Hadiah();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String jam = timeFormat.format(cal.getTime());
//		String d = dateFormat.format(cal.getTime());
//		Date sysdate = dateFormat.parse(d);				
//		hadiah.mh_tgl_paid = sysdate;
		
		String spaj;
		int pos, mh;
		
		//bind data
		ServletRequestDataBinder binder = createBinder(request, hadiah);

		//setup binding
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, completeDateEditor); //untuk date secara umum
		binder.registerCustomEditor(Date.class, "beg_date", completeDateEditor);
		binder.registerCustomEditor(Date.class, "end_date", completeDateEditor);
		binder.registerCustomEditor(Date.class, "tgl_aksep", completeDateEditor);
		binder.bind(request);
		BindException err = new BindException(binder.getBindingResult());
		
		String tgl_paid = ServletRequestUtils.getStringParameter(request, "tgl_proses",defaultDateFormat.format(elionsManager.selectSysdate(0)));
		
		Map<String, Object> cmd = new HashMap<String, Object>();
		cmd.put("tgl_paid", tgl_paid);
		
		String _spaj = ServletRequestUtils.getStringParameter(request, "_spaj");
		Integer post = ajaxManager.selectPositionHadiah(_spaj);
		request.setAttribute("lspd_pos", post);
		request.setAttribute("spaj", _spaj);
		
		if(request.getParameter("btnSave") != null){
			String sspaj = ServletRequestUtils.getRequiredStringParameter(request, "_spaj");
//			pos = ServletRequestUtils.getRequiredIntParameter(request, "lspd_id");
//			mh = ServletRequestUtils.getRequiredIntParameter(request, "mh_no");
			
			post = ajaxManager.selectPositionHadiah(sspaj);
			if(post==86){
				hadiah.reg_spaj = sspaj;
				hadiah.lspd_id = ServletRequestUtils.getRequiredIntParameter(request, "lspd_id");
				hadiah.mh_no = ServletRequestUtils.getRequiredIntParameter(request, "mh_no");
				String judul=ServletRequestUtils.getStringParameter(request, "up_file", "BUKTI BAYAR HADIAH");
				
				String tgl = ServletRequestUtils.getStringParameter(request, "tgl_paid");
				String tg = tgl +" "+ jam;
				Date sysdate1 = dateFormat.parse(tg);				
				hadiah.mh_tgl_paid = sysdate1;
				
				Upload upload = new Upload();
				ServletRequestDataBinder binders = new ServletRequestDataBinder(upload);
				binders.bind(request);
				
				String path = (String) request.getSession().getAttribute("pathHadiah");
				String tDest=props.getProperty("upload.hadiah")+"\\"+hadiah.reg_spaj+"\\";
				File destDir = new File(tDest);
				
				if(!destDir.exists()) destDir.mkdirs();
				
				if(!judul.equals("") && !"".equals(sspaj)){
					if(upload.getFile1() != null)
					if(upload.getFile1().isEmpty()==false){
						String filename = "BUKTI BAYAR HADIAH";
						String name = filename.toUpperCase();
						//String pdf = name+"("+hadiah.reg_spaj+")";
						String dest = tDest +"\\"+name+"_"+hadiah.reg_spaj+".pdf";
						File outputFile = new File(dest);
						FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
						
						// Insert History Upload dan History Hadiah
						cmd.put("pesan", "Data Berhasil diupload");
					}else{
						logger.info("upload file tidak ada");
					}
				}
				hadiah.keterangan = "Upload Bukti Bayar " + hadiah.reg_spaj;
				ajaxManager.updateProses(hadiah, currentUser);
				
				//kirim pemberitahuan ke BAS
				String to = to = props.getProperty("hadiah.email.bas");
				String[] emailTo = to.split(";");
				
				for(int y=0; y<emailTo.length; y++){
					emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
				}
				email.send(true, props.getProperty("admin.ajsjava"), emailTo, null, null, "System Tracking", "Spaj dengan no "+hadiah.reg_spaj+" telah di komfirmasi pembayarannya (transfer ke posisi 87)", null);
				
			}else{
				hadiah.setPesan("SPAJ ini sudah di konfirmasi pembayarannya");
			}

			request.getSession().removeAttribute("pathHadiah");
			
			//return new ModelAndView(new RedirectView(path.replace(null, "")+"hadiah.htm?window=upload&?_spaj="+hadiah.reg_spaj));
		}
		
		if(request.getParameter("btnTransfer") != null){
//			spaj = ServletRequestUtils.getRequiredStringParameter(request, "_spaj");
//			pos = ServletRequestUtils.getRequiredIntParameter(request, "lspdID");
//			mh = ServletRequestUtils.getRequiredIntParameter(request, "mh_no");
						
//			hadiah.reg_spaj = spaj;
//			hadiah.lspd_id = pos + 1;
//			hadiah.mh_no = mh;		
			//hadiah.mh_tgl_paid = sysdate;
			hadiah.keterangan = "Transfer Data Hadiah ke-" + hadiah.lspd_id;
			ajaxManager.updateProses(hadiah, currentUser);
		}
		
		if(request.getParameter("btnLogOff") != null){
			User user = (User) request.getSession().getAttribute("currentUser");
			
			if(user!=null) {
				String userName = user.getName();
				@SuppressWarnings("rawtypes")
				Map users = (HashMap) request.getSession().getServletContext().getAttribute("users");
				if(users!=null)
					users.remove(userName);
				request.getSession().getServletContext().setAttribute("users", users);
				request.getSession().removeAttribute("currentUser");
				request.getSession().removeAttribute("pathHadiah");
			}			
			request.getSession().invalidate();
			return new ModelAndView(new RedirectView(request.getContextPath()));
		}
		
		if(tipe.equals("upPath")){
			spaj = ServletRequestUtils.getRequiredStringParameter(request, "_spaj");
			pos = ServletRequestUtils.getRequiredIntParameter(request, "lspdID");
			mh = ServletRequestUtils.getRequiredIntParameter(request, "mh_no");
			
			hadiah.reg_spaj = spaj;
			hadiah.lspd_id = pos + 1;
			hadiah.mh_no = mh;
			
			cmd.put("reg_spaj", spaj);
			
			request.getSession().removeAttribute("pathHadiah");
			
			return new ModelAndView("bas/hadiah_upload",err.getModel());
		}
		return new ModelAndView("bas/hadiah_upload",err.getModel());
	}
	
	public void kirimEmail(Hadiah hadiah) throws MailException, MessagingException{
		String to = to = props.getProperty("hadiah.email.bas");
		String[] emailTo = to.split(";");
		
		for(int y=0; y<emailTo.length; y++){
			emailTo[y] = emailTo[y].concat("@sinarmasmsiglife.co.id");
		}
		email.send(true, props.getProperty("admin.ajsjava"), emailTo, null, null, "System Tracking",
				"<p>System Tracking"+
				"</p>"+
				"<p>Posisi dokumen dengan SPAJ "+hadiah.reg_spaj+" sekarang di posisi "+hadiah.lspd_id+" </p>"+
				"<table width='100%' border='0'>"+
				"<tr>"+
			    "<td width='236'>No SPAJ</td>"+
			    "<td width='20'>:</td>"+
			    "<td width='892'>"+hadiah.reg_spaj+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>No Polis</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.policy_no+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Pemegang Polis</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.pemegang+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Periode Polis</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.beg_date+" s/d "+hadiah.end_date+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Tgl Aksel UW</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.tgl_aksep+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Kategori Unit Hadiah</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.lhc_nama+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Nama Unit Hadiah</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.lh_nama+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Jumlah Pembelian Unit</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.mh_quantity+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Budget Pembelian Unit</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.mh_budget.toString()+"</td>"+
				"</tr>"+
				"<tr>"+
				"<td width='236'>Harga Unit</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.mh_harga.toString()+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Jenis Pengiriman</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.mh_flag_kirim+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Alamat Nasabah</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.mh_alamat+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Kota</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.mh_kota+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Kode Pos</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.mh_kodepos+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Telepon</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.mh_telepon+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Vendor</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.supplier_name+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Bank Rekening Vendor</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.bank_name+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Nomor Rekening Vendor</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.mh_rek_no+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Atas Nama Rekening Vendor</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.mh_rek_nama+"</td>"+
				"</tr>"+
				"<tr>"+
			    "<td width='236'>Keterangan</td>"+
			    "<td>:</td>"+
			    "<td>"+hadiah.keterangan+"</td>"+
				"</tr>"+
				"</table>"
				,null);
	}
	
	/**
	 * Menu proses hadiah, untuk digunakan oleh user purchasing
	 * 
	 * @author Canpri
	 * @since 08 Oct 2012
	 */	
	public ModelAndView program_hadiah(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Hadiah hadiah = new Hadiah();		
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		//bind data
		ServletRequestDataBinder binder = createBinder(request, hadiah);

		//setup binding
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, completeDateEditor); //untuk date secara umum
		binder.registerCustomEditor(Date.class, "beg_date", completeDateEditor);
		binder.registerCustomEditor(Date.class, "end_date", completeDateEditor);
		binder.registerCustomEditor(Date.class, "tgl_aksep", completeDateEditor);		
		
		binder.bind(request);
		BindException err = new BindException(binder.getBindingResult());
		//
		String begdate = ServletRequestUtils.getStringParameter(request, "begdate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
		String enddate = ServletRequestUtils.getStringParameter(request, "enddate", defaultDateFormat.format(elionsManager.selectSysdate(30)));
		String tgl_proses = ServletRequestUtils.getStringParameter(request, "tgl_proses",defaultDateFormat.format(elionsManager.selectSysdate(0)));
	    
		//Reference Data
		List<DropDown> listPosisi = elionsManager.selectDropDown("EKA.LST_DOCUMENT_POSITION", "LSPD_ID", "LSPD_POSITION", "", "LSPD_ID", "LSPD_ID IN (84, 91, 92, 93, 95, 99)");
		
		List<DropDown> listTanggal = new ArrayList<DropDown>();
		listTanggal.add(new DropDown("1", "Tanggal Input"));
		listTanggal.add(new DropDown("2", "Tanggal Akseptasi UW"));
		listTanggal.add(new DropDown("3", "Tanggal Mulai Berlaku Polis"));
		
		//Model Object
		Map<String, Object> cmd = new HashMap<String, Object>();
		cmd.put("begdate", begdate);
		cmd.put("enddate", enddate);
		cmd.put("prosdate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
	    cmd.put("tgl_proses", tgl_proses);
		cmd.put("listPosisi", listPosisi);
		cmd.put("listTanggal", listTanggal);
		cmd.put("listHadiah", uwManager.selectHadiahStableSave(null));
		
		//proses transfer
		if(request.getParameter("btnTransfer") != null) {
			//bila tidak ada error, update data
			if(!err.hasErrors()) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
				
				Calendar cal = Calendar.getInstance();
				
				String d = dateFormat.format(cal.getTime());
				String t = timeFormat.format(new Date());
				String tggl = ServletRequestUtils.getStringParameter(request, "tgl_proses");
				String tggl2 = ServletRequestUtils.getStringParameter(request, "tgl_pembayaran");
				hadiah.mh_sn = ServletRequestUtils.getStringParameter(request, "sn", "");
				String alamat_kirim = ServletRequestUtils.getStringParameter(request, "alamat", "");
				
				
				Date sysdate = dateFormat.parse(d); // Posisi 85 dan 86
				String dt1 = tggl + " " + t;
				String dt2 = tggl2 + " " + t;
				Date tgl = dateFormat.parse(dt1); // posisi 87, 88, 89, dan 90 
				Date tgl_deadline = dateFormat.parse(dt1); //posisi 85->86
				//logger.info(tggl+" <-> "+dt1+" <-> "+cal+" <-> "+tgl);
				
				String selectPosisi = ServletRequestUtils.getStringParameter(request, "selectPosisi");
				
				int lspdID = Integer.parseInt(request.getParameter("lspdID"));
				hadiah.lspd_id = lspdID;
				hadiah.reg_spaj = request.getParameter("spn");
				int mhno = Integer.parseInt(request.getParameter("mh_no"));
				hadiah.mh_no = mhno;
				//hadiah.keterangan = "Transfer Data Hadiah ke-" + hadiah.lspd_id;				
				
				String from = null;
				String[] to = null;
				String[] cc = null;
				String[] bcc = null;
				String subject= null;
				Boolean kirim = false;
				
				String message = null;
				List<File> attachments = new ArrayList<File>();
				
				if("84".equals(selectPosisi)){
					//hadiah.mh_tgl_aksep = sysdate;
					hadiah.mh_tgl_kirim_uw = tgl;
					hadiah.mh_tgl_aksep = tgl;
					//hadiah.pesan = ajaxManager.prosesHadiahMemo3(hadiah, currentUser);
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
					
					//kirim email
					from = null;
					to = new String[]{""};
					cc = new String[]{""};
					bcc = new String[]{""};
					message = "";
					attachments = null;
					
				}else if("91".equals(selectPosisi)){
					hadiah.mh_tgl_order_purchasing = tgl;
					hadiah.keterangan = "Order hadiah oleh purchasing (92)";
					//hadiah.mh_alamat_kirim = alamat_kirim;
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
					
					//kirim email
					from = null;
					to = new String[]{""};
					cc = new String[]{""};
					bcc = new String[]{""};
					message = "";
					attachments = null;
					
				}else if("92".equals(selectPosisi)){
					hadiah.mh_tgl_barang_ready = tgl;
					hadiah.keterangan = "Hadiah ready dan confirm ke marketing (93)";
					hadiah.mh_tgl_kirim_hadiah = tanggal(tgl, 3);
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
					
					Pemegang pemegang = elionsManager.selectpp(hadiah.reg_spaj);
					Agen agen = elionsManager.select_detilagen(hadiah.reg_spaj);
					Hadiah data = uwManager.selectMstHadiah(hadiah.reg_spaj, 1);
					Hadiah jn_hadiah= uwManager.selectLstHadiah(data.getLhc_id(),data.getLh_id());
					String lca_id = uwManager.selectCabangFromSpaj(hadiah.reg_spaj);
					Product produk = uwManager.selectMstProductInsuredUtamaFromSpaj(hadiah.reg_spaj);
					
					double pr = produk.getMspr_premium();
					
					String team_name = uwManager.selectBancassTeam(hadiah.reg_spaj);
					if(team_name==null){
						team_name= "";
					}
					
					String cb = "";
					if("37,52".indexOf(lca_id)>-1) { //Agency
					       cb = "Agency";
					}else if(lca_id.equals("46")) { //Hybrid
					       cb = "Hybrid";
					}else if(lca_id.equals("09")) { //Bancassurance
					       if(team_name.toUpperCase().equals("TEAM JAN ROSADI")) { //Bancassurance2
					    	   cb = "Bancassurance 2";
					       }else if(team_name.toUpperCase().equals("TEAM DEWI")) { //Bancassurance3
					    	   cb = "Bancassurance 3";
					       }else if(team_name.toUpperCase().equals("TEAM YANTI SUMIRKAN")) { //Bancassurance1
					    	   cb = "Bancassurance 1";
					       }else{
					    	   cb = "Bancassurance";
					       }
					}else if(lca_id.equals("08") || lca_id.equals("42")) { //Worksite
					       cb = "Worksite";
					}else if(lca_id.equals("55")) { //DM/TM
					       cb = "DMTM";
					}else if(lca_id.equals("58")) { //MallAssurance
					       cb = "MallAssurance";
					}else { //Regional
					       cb = "Regional";
					}
					
					StringBuffer pesan = new StringBuffer();
					//Format isi email
					
					String link = "https://www.sinarmasmsiglife.co.id/E-Lions/bas/hadiah.htm?window=reschedule&hadiah_spaj="+hadiah.reg_spaj;
					String tkey = "";
			   		try {
			   			tkey = uwManager.encryptUrlKey("hadiah_reschedule", hadiah.reg_spaj, App.ID, link.replaceAll("&", "~"));
			   		}catch (Exception e) {
						logger.error("ERROR", e);
					}
			   		link = link +"&tkey="+tkey;
					
					pesan.append("<p><strong>BARANG TERSEDIA</strong> <br><br>");
					pesan.append("Konfirmasi  dengan supplier, barang untuk hadiah nasabah Stable Save program hadiah  telah tersedia ("+jn_hadiah.getLh_nama()+")<br>");
					pesan.append("- Nama\t\t : "+pemegang.getMcl_first()+"</strong><br>");
					pesan.append("- No polis\t : "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+"<br>");
					pesan.append("- Premi\t\t : Rp. "+FormatString.formatCurrency("", new BigDecimal(pr))+"<br>");
					pesan.append("- Penutup\t : "+agen.getMcl_first()+" ("+cb+")<br>");
					pesan.append("Mohon  informasi schedule pengiriman (paling cepat 3 hari kerja dari email ini  diterima)</p><br><br><br>");
					//pesan.append("<h2><a href='http://localhost/E-Lions/bas/hadiah.htm?window=reschedule&hadiah_spaj="+hadiah.reg_spaj+"'>Confirm</a></h2><br><br><br>");
					pesan.append("<h2><a href='"+link+"'>Confirm</a></h2><br><br><br>");
					//pesan.append("<h2><a href='http://ekasms/E-LionsTE/bas/hadiah.htm?window=reschedule&hadiah_spaj="+hadiah.reg_spaj+"'>Reschedule</a></h2><br><br><br>");
					
					//kirim email
					
					/*Email ke : 	- Grisye, Erwin, Yunita, Yanty (Bancassurance)
					- Mega, Lily Jo, Gideon (Mallassurance dan DMTM)
					- Nixon, Wekky, Molly Westni (Worksite)
					Semua cc Jelita dan Hisar dulu ya..*/
					
					from = props.getProperty("admin.ajsjava");
					//to = new String[]{"canpri@sinarmasmsiglife.co.id","Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id"};
					to = new String[]{"Grisye@sinarmasmsiglife.co.id","erwin_k@sinarmasmsiglife.co.id", "yunita@sinarmasmsiglife.co.id","yantisumirkan@sinarmasmsiglife.co.id",
										"Mega@sinarmasmsiglife.co.id", "Lilyanti@sinarmasmsiglife.co.id", "Gideon@sinarmasmsiglife.co.id",
										"nixon@sinarmasmsiglife.co.id", "weky.corporate@sinarmasmsiglife.co.id","wesni@sinarmasmsiglife.co.id"};
					cc = new String[]{"Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id","shima@sinarmasmsiglife.co.id"};
					bcc = new String[]{"canpri@sinarmasmsiglife.co.id", "antasari@sinarmasmsiglife.co.id", "deddy@sinarmasmsiglife.co.id"};
					message = pesan.toString();
					subject = "Program Hadiah Stable Save (Hadiah Tersedia untuk Polis "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+")";
					
					attachments = null;
					
					kirim = true;
					//email.send(true, props.getProperty("admin.ajsjava"), to, null, null, "Program Hadiah Stable Save", message, null);
					
					//send email ke atasan lvl 1 dan 2 lca id = 42
					String lca = uwManager.selectLcaIdMstPolicyBasedSpaj(hadiah.reg_spaj);
					if(lca.equals("42")){
						
						List email_leader = uwManager.selectEmailLeader(hadiah.reg_spaj);
						
						Map e = (HashMap) email_leader.get(0);
						
						String lvl_1 = (String)e.get("LVL_1");
						String lvl_2 = (String)e.get("LVL_2");
						
						String from2 = props.getProperty("admin.ajsjava");
						String[] to2 = new String[]{"lucianna@sinarmasmsiglife.co.id", lvl_1, lvl_2};
						String[] cc2 = new String[]{"nixon@sinarmasmsiglife.co.id", "Juan@sinarmasmsiglife.co.id", "Edhy@sinarmasmsiglife.co.id", "wesni@sinarmasmsiglife.co.id","shima@sinarmasmsiglife.co.id"};
						String[] bcc2 = new String[]{"canpri@sinarmasmsiglife.co.id", "antasari@sinarmasmsiglife.co.id", "deddy@sinarmasmsiglife.co.id"};
						String subject2 = "Stable Save Berhadiah";
						
						StringBuffer pesan2 = new StringBuffer();
						
						pesan2.append("\"INFORMASI HADIAH SUDAH TERSEDIA\"<br><br>");
						pesan2.append("Polis\t\t\t : "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+"<br>");
						pesan2.append("Nama Pemegang Polis\t : "+pemegang.getMcl_first()+"<br>");
						pesan2.append("Alamat\t\t\t : "+data.mh_alamat+" "+data.mh_kota+" "+data.mh_kodepos+"<br><br>");
						pesan2.append("Mohon konfirmasi alamat pengiriman ( Hari/Tgl/Jam/Alamat )Apakah sesuai dengan yang dipolis, informasi secepatnya kami terima 3 hari kerja dari email ini terkirim dialamatkan ke email : Lucianna Natalia Tampubolon dan CC Nixon, Juan Tuvano, Edhy Susantyo, Molly Wesni <br><br>");
						pesan2.append("Terima Kasih");
						
						String message2 = pesan2.toString();
						
						EmailPool.send("Program Hadiah E-Lions", 1, 1, 0, 0, null, 0, Integer.parseInt(currentUser.getLus_id()), elionsManager.selectSysdate(), null, true, from2, to2, cc2, bcc2, subject2, message2, null, hadiah.reg_spaj);
					}
					
				}else if("94".equals(selectPosisi)){
					hadiah.mh_tgl_kirim_package = tgl;
					hadiah.keterangan = "Purchasing kirim hadiah ke GA (96)";
					//hadiah.mh_alamat_kirim = alamat_kirim;
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
					
					//kirim email
					from = null;
					to = new String[]{""};
					cc = new String[]{""};
					bcc = new String[]{""};
					message = "";
					attachments = null;
					
				}else if("96".equals(selectPosisi)){
					hadiah.mh_tgl_kirim_ga = tgl;
					hadiah.keterangan = "Pengiriman hadiah ke nasabah (90)";
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
					
					Pemegang pemegang = elionsManager.selectpp(hadiah.reg_spaj);
					Agen agen = elionsManager.select_detilagen(hadiah.reg_spaj);
					Hadiah data = uwManager.selectMstHadiah(hadiah.reg_spaj, 1);
					Hadiah jn_hadiah= uwManager.selectLstHadiah(data.getLhc_id(),data.getLh_id());
					String lca_id = uwManager.selectCabangFromSpaj(hadiah.reg_spaj);
					Product produk = uwManager.selectMstProductInsuredUtamaFromSpaj(hadiah.reg_spaj);
					
					double pr = produk.getMspr_premium();
					
					String team_name = uwManager.selectBancassTeam(hadiah.reg_spaj);
					if(team_name==null){
						team_name= "";
					}
					
					String cb = "";
					if("37,52".indexOf(lca_id)>-1) { //Agency
					       cb = "Agency";
					}else if(lca_id.equals("46")) { //Hybrid
					       cb = "Hybrid";
					}else if(lca_id.equals("09")) { //Bancassurance
					       if(team_name.toUpperCase().equals("TEAM JAN ROSADI")) { //Bancassurance2
					    	   cb = "Bancassurance 2";
					       }else if(team_name.toUpperCase().equals("TEAM DEWI")) { //Bancassurance3
					    	   cb = "Bancassurance 3";
					       }else if(team_name.toUpperCase().equals("TEAM YANTI SUMIRKAN")) { //Bancassurance1
					    	   cb = "Bancassurance 1";
					       }else{
					    	   cb = "Bancassurance";
					       }
					}else if(lca_id.equals("08") || lca_id.equals("42")) { //Worksite
					       cb = "Worksite";
					}else if(lca_id.equals("55")) { //DM/TM
					       cb = "DMTM";
					}else if(lca_id.equals("58")) { //MallAssurance
					       cb = "MallAssurance";
					}else { //Regional
					       cb = "Regional";
					}
					
					SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
					
					StringBuffer pesan = new StringBuffer();
					//Format isi email
					
					pesan.append("<p><strong>Informasi pengiriman hadiah hari "+FormatDate.getDayInWeekIndonesia(data.mh_tgl_kirim_hadiah)+", "+FormatDate.toIndonesian(data.mh_tgl_kirim_hadiah)+"</strong> <br><br>");
					pesan.append("- Nama\t\t\t : "+pemegang.getMcl_first()+" ("+FormatString.nomorSPAJ(hadiah.reg_spaj)+")<br>");
					pesan.append("- Premi\t\t\t : Rp. "+FormatString.formatCurrency("", new BigDecimal(pr))+"<br>");
					pesan.append("- Penutup\t\t : "+agen.getMcl_first()+" ("+cb+")<br>");
					pesan.append("- Jenis Hadiah\t\t : "+jn_hadiah.getLh_nama()+"<br>");
					pesan.append("- Alamat Pengiriman\t : "+data.mh_alamat+" "+data.mh_kota+" "+data.mh_kodepos+"<br>");
					pesan.append("- Appointment\t\t : "+FormatDate.getDayInWeekIndonesia(data.mh_tgl_kirim_hadiah)+", "+FormatDate.toIndonesian(data.mh_tgl_kirim_hadiah)+" (jam "+dt.format(data.mh_tgl_kirim_hadiah)+")<br>");
					
					//kirim email
					
				    /*Email ke : 	- Grisye, Erwin, Yunita, Yanty (Bancassurance)
					- Mega, Lily Jo, Gideon (Mallassurance dan DMTM)
					- Nixon, Wekky, Molly Westni (Worksite)
					cc : Jelita, Hisar, Meidy*/
					
					from = props.getProperty("admin.ajsjava");
					//to = new String[]{"canpri@sinarmasmsiglife.co.id","Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id"};
					to = new String[]{"Grisye@sinarmasmsiglife.co.id","erwin_k@sinarmasmsiglife.co.id", "yunita@sinarmasmsiglife.co.id","yantisumirkan@sinarmasmsiglife.co.id",
										"Mega@sinarmasmsiglife.co.id", "Lilyanti@sinarmasmsiglife.co.id", "Gideon@sinarmasmsiglife.co.id",
										"nixon@sinarmasmsiglife.co.id", "weky.corporate@sinarmasmsiglife.co.id","wesni@sinarmasmsiglife.co.id"};
					cc = new String[]{"Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id", "meidytumewu@sinarmasmsiglife.co.id","shima@sinarmasmsiglife.co.id"};
					bcc = new String[]{"canpri@sinarmasmsiglife.co.id", "antasari@sinarmasmsiglife.co.id", "deddy@sinarmasmsiglife.co.id"};
					message = pesan.toString();
					subject = "Program Hadiah Stable Save (Pengiriman Hadiah untuk Polis "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+")";
					attachments = null;
					
					kirim = true;
					//email.send(true, props.getProperty("admin.ajsjava"), to, null, null, "Program Hadiah Stable Save", message, null);
					
				}else if("90".equals(selectPosisi)){
					hadiah.mh_tgl_terima_nsbh = tgl;
					hadiah.keterangan = "Hadiah telah diterima nasabah (99)";
					hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
					
					//upload
					Upload upload = new Upload();
					ServletRequestDataBinder binders = new ServletRequestDataBinder(upload);
					binders.bind(request);
					
					String path = (String) request.getSession().getAttribute("pathHadiah");
					//String tDest=props.getProperty("upload.hadiah")+"\\"+hadiah.reg_spaj+"\\"+hadiah.mh_no+"\\";
					//String tDest="\\\\ekasms\\Setup\\Tambang Emas\\Hadiah\\"+hadiah.reg_spaj+"\\"+hadiah.mh_no+"\\";
					//String tDest= "D:\\test\\"+hadiah.reg_spaj+"\\";
					String tDest= props.getProperty("pdf.dir.export")+"\\"+elionsManager.selectCabangFromSpaj(hadiah.reg_spaj)+"\\"+hadiah.reg_spaj;
					File destDir = new File(tDest);
					
					String a = upload.getFile1().getOriginalFilename();
					
					if(!destDir.exists()) destDir.mkdirs();
					
					if(upload.getFile1() != null)
					if(upload.getFile1().isEmpty()==false){
						String filename = "BUKTI_TERIMA_NASABAH_"+hadiah.reg_spaj;
						String name = filename.toUpperCase();
						String dest = tDest +"\\"+name+".pdf";
						File outputFile = new File(dest);
						FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
						
						attachments.add(outputFile);
					}else{
						logger.info("upload file tidak ada");
						attachments = null;
					}
					//end upload
					
					Pemegang pemegang = elionsManager.selectpp(hadiah.reg_spaj);
					Agen agen = elionsManager.select_detilagen(hadiah.reg_spaj);
					Hadiah data = uwManager.selectMstHadiah(hadiah.reg_spaj, 1);
					Hadiah jn_hadiah= uwManager.selectLstHadiah(data.getLhc_id(),data.getLh_id());
					String lca_id = uwManager.selectCabangFromSpaj(hadiah.reg_spaj);
					Product produk = uwManager.selectMstProductInsuredUtamaFromSpaj(hadiah.reg_spaj);
					
					double pr = produk.getMspr_premium();
					
					String team_name = uwManager.selectBancassTeam(hadiah.reg_spaj);
					if(team_name==null){
						team_name= "";
					}
					
					String cb = "";
					if("37,52".indexOf(lca_id)>-1) { //Agency
					       cb = "Agency";
					}else if(lca_id.equals("46")) { //Hybrid
					       cb = "Hybrid";
					}else if(lca_id.equals("09")) { //Bancassurance
					       if(team_name.toUpperCase().equals("TEAM JAN ROSADI")) { //Bancassurance2
					    	   cb = "Bancassurance 2";
					       }else if(team_name.toUpperCase().equals("TEAM DEWI")) { //Bancassurance3
					    	   cb = "Bancassurance 3";
					       }else if(team_name.toUpperCase().equals("TEAM YANTI SUMIRKAN")) { //Bancassurance1
					    	   cb = "Bancassurance 1";
					       }else{
					    	   cb = "Bancassurance";
					       }
					}else if(lca_id.equals("08") || lca_id.equals("42")) { //Worksite
					       cb = "Worksite";
					}else if(lca_id.equals("55")) { //DM/TM
					       cb = "DMTM";
					}else if(lca_id.equals("58")) { //MallAssurance
					       cb = "MallAssurance";
					}else { //Regional
					       cb = "Regional";
					}
					
					SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
					
					StringBuffer pesan = new StringBuffer();
					//Format isi email
					
					pesan.append("<p><strong>Berikut disampaikan daftar hadiah yang telah terkirim </strong> <br><br>");
					pesan.append("- Nama\t\t\t : "+pemegang.getMcl_first()+" ("+FormatString.nomorSPAJ(hadiah.reg_spaj)+")<br>");
					pesan.append("- Premi\t\t\t : Rp. "+FormatString.formatCurrency("", new BigDecimal(pr))+"<br>");
					pesan.append("- Penutup\t\t : "+agen.getMcl_first()+" ("+cb+")<br>");
					pesan.append("- Jenis Hadiah\t\t : "+jn_hadiah.getLh_nama()+"<br>");
					pesan.append("- Alamat Pengiriman\t : "+data.mh_alamat+" "+data.mh_kota+" "+data.mh_kodepos+"<br>");
					pesan.append("- Terkirim\t\t : "+FormatDate.getDayInWeekIndonesia(hadiah.mh_tgl_terima_nsbh)+", "+FormatDate.toIndonesian(hadiah.mh_tgl_terima_nsbh)+" (jam "+dt.format(hadiah.mh_tgl_terima_nsbh)+")<br>");
					
					//kirim email
					/*Email ke : 	- Grisye, Erwin, Yunita, Yanty (Bancassurance)
					- Mega, Lily Jo, Gideon (Mallassurance dan DMTM)
					- Nixon, Wekky, Molly Westni (Worksite)
					cc : Jelita, Hisar, Meidy*/

					from = props.getProperty("admin.ajsjava");
					//to = new String[]{"canpri@sinarmasmsiglife.co.id","Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id"};
					to = new String[]{"Grisye@sinarmasmsiglife.co.id","erwin_k@sinarmasmsiglife.co.id", "yunita@sinarmasmsiglife.co.id","yantisumirkan@sinarmasmsiglife.co.id",
										"Mega@sinarmasmsiglife.co.id", "Lilyanti@sinarmasmsiglife.co.id", "Gideon@sinarmasmsiglife.co.id",
										"nixon@sinarmasmsiglife.co.id", "weky.corporate@sinarmasmsiglife.co.id","wesni@sinarmasmsiglife.co.id"};
					cc = new String[]{"Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id", "meidytumewu@sinarmasmsiglife.co.id","shima@sinarmasmsiglife.co.id"};
					bcc = new String[]{"canpri@sinarmasmsiglife.co.id", "antasari@sinarmasmsiglife.co.id", "deddy@sinarmasmsiglife.co.id"};
					message = pesan.toString();
					subject = "Program Hadiah Stable Save (Hadiah Sudah di terima nasabah untuk Polis "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+")";
					
					kirim = true;
					//email.send(true, props.getProperty("admin.ajsjava"), to, null, null, "Program Hadiah Stable Save", message, attachments);
				}
				
				//String me_id = sequence.sequenceMeIdEmail();
				//EmailPool.send(me_id, "Program Hadiah E-Lions", 1, 1, 0, 0, null, 0, Integer.parseInt(currentUser.getLus_id()), elionsManager.selectSysdate(), null, true, from, to, cc, bcc, "Program Hadiah Power Save Mgi 36 Bulan ", message, attachments);
//				if(kirim == true)email.send(true, from, to, cc, bcc, subject, message, attachments);
				if(kirim == true){
					EmailPool.send( "Program Hadiah E-Lions", 1, 1, 0, 0, null, 0, 0, elionsManager.selectSysdate(), null, true, from, to, cc, bcc, subject, message, attachments, hadiah.reg_spaj);
				}
			}
		}
		
		//user menekan tombol print
		if(request.getParameter("btnPrint") != null) {
			hadiah.reg_spaj = request.getParameter("spn");
			int mhno = Integer.parseInt(request.getParameter("mh_no"));
			
			hadiah = uwManager.selectMstHadiah(hadiah.reg_spaj, mhno);
			//bila tidak ada error, simpan data
			if(!err.hasErrors()) {
				hadiah.pesan = uwManager.printHadiahStableSave(hadiah, currentUser);
				
				//Download File
				String filename="TANDA_TERIMA_HADIAH.pdf";
				//String file = props.getProperty("pdf.dir.tt.hadiah")+""+hadiah.reg_spaj+"\\"+filename;
				String file = props.getProperty("pdf.dir.export")+"\\"+elionsManager.selectCabangFromSpaj(hadiah.reg_spaj)+"\\"+hadiah.reg_spaj+"\\"+filename;
				File sourceFile = new File(file);
				FileInputStream in = null;
				ServletOutputStream ouputStream = null;
				try{
					
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "Attachment;filename="+filename);
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					
					in = new FileInputStream(sourceFile);
				    ouputStream = response.getOutputStream();
				    
				    IOUtils.copy(in, ouputStream);
				}catch(Exception e){
					logger.error("ERROR :", e);
				}finally{
					try {
		            	if(in != null) {
		            		in.close();
		            	}
		            	if(ouputStream != null) {
		            		ouputStream.flush();
		            		ouputStream.close();
		            	}  
		            } catch (Exception e) {
		                   // TODO Auto-generated catch block
		                   logger.error("ERROR", e);
		            }
				}
			}
			return null;
		}
				
		cmd.put("lde_id", currentUser.getLde_id());
		cmd.put("pesan", hadiah.pesan);
		
		return new ModelAndView("bas/program_hadiah", cmd);
	}
	
	/**
	 * Fungsi untuk mendapatkan tanggal + (bilangan yang di tentukan)
	 * 
	 * @author Canpri Setiawan
	 * @since 17 Oct 2012
	 * @param tanggal, hari+
	 * @return tanggal
	 */
	public Date tanggal(Date date, Integer hari){
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date);
		cal.add(Calendar.DATE, hari); 
		
		Boolean hasil = false;
		Boolean hasil2 = false;
		
		Integer i = 0;
		while(i==0){
			//cek apakah hari senin/minggu
			while(hasil == false) {
				logger.info(cal.getTime());
				
				logger.info("week : "+cal.get(Calendar.DAY_OF_WEEK)+" | hari : "+cal.get(Calendar.SATURDAY));
				if((cal.get(Calendar.DAY_OF_WEEK)==7) || (cal.get(Calendar.DAY_OF_WEEK)==1)){
					cal.add(Calendar.DATE, 1); 
					hasil = false;
				}else{
					hasil = true;
				}
			}
			
			//cek apakah hari libur di ajs
			while(hasil2 == false) {
				String tgl = df.format(cal.getTime());
				Integer libur = uwManager.selectLibur(tgl);
				if(libur>0){
					cal.add(Calendar.DATE, 1);
					hasil2 = false;
				}else{
					hasil2 = true;
				}
			}
			
			if((cal.get(Calendar.DAY_OF_WEEK)==7) || (cal.get(Calendar.DAY_OF_WEEK)==1)){
				hasil = false;
				hasil2 = false;
			}else{
				i = 1;
			}
			
			//cek lagi apakah hari senin/minggu
			/*while(hasil == false) {
				logger.info(cal.getTime());
				
				if((cal.get(Calendar.DAY_OF_WEEK)==cal.get(Calendar.SATURDAY)) || (cal.get(Calendar.DAY_OF_WEEK)==cal.get(Calendar.SUNDAY))){
					cal.add(Calendar.DATE, 1); 
					hasil = false;
				}else{
					hasil = true;
				}
			}*/
		}
		
		//String tglplus = df.format(cal.getTime());
		return cal.getTime();
	}
	
	/**
	 * Menu reschedule pengiriman hadiah, by pass e-lions
	 * 
	 * @author Canpri
	 * @since 08 Oct 2012
	 */		
	public ModelAndView reschedule(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Hadiah hadiah = new Hadiah();		
		Map<String, Object> cmd = new HashMap<String, Object>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Sequence sequence = new Sequence();
		
		//bind data
		ServletRequestDataBinder binder = createBinder(request, hadiah);
		
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		SimpleDateFormat dw = new SimpleDateFormat("HH:mm");
		SimpleDateFormat dd = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dj = new SimpleDateFormat("HH");
		SimpleDateFormat dm = new SimpleDateFormat("mm");
		
		String spaj = ServletRequestUtils.getStringParameter(request, "hadiah_spaj","");
		
		Date tg = tanggal(elionsManager.selectSysdate(), 3);
		
		hadiah = uwManager.selectMstHadiahRE(spaj, 1);
		
		if(request.getParameter("btnSimpan") != null) {
			
			String tgl = ServletRequestUtils.getStringParameter(request, "re_tgl_kirim_hadiah");
			String jam = ServletRequestUtils.getStringParameter(request, "re_tgl_kirim_hadiah_jam", "00");
			String mnt = ServletRequestUtils.getStringParameter(request, "re_tgl_kirim_hadiah_mnt", "00");
			String tgl_sebelum = ServletRequestUtils.getStringParameter(request, "tgl_sebelum","");
			String alamat = ServletRequestUtils.getStringParameter(request, "alamat", "");
			String kota = ServletRequestUtils.getStringParameter(request, "kota", "");
			String kodepos = ServletRequestUtils.getStringParameter(request, "kodepos", "");
			String keterangan = ServletRequestUtils.getStringParameter(request, "keterangan", "");
			
			String tgl2 = tgl+" "+jam+":"+mnt+":00";
			
			Date tgl_hadiah = dt.parse(tgl2);
			//Date tgl_before = dt.parse(tgl_sebelum);
			
			//jika tidak lewat dari 3 hari
			if(tgl_hadiah.before(tg)){
				//hadiah.pesan = "Hari pengiriman harus +3 hari kerja!";
				cmd.put("spaj", spaj);
				cmd.put("pesan", "Hari pengiriman harus +3 hari kerja!");
			}else{
				hadiah.mh_tgl_kirim_hadiah = tgl_hadiah;
				hadiah.mh_keterangan = keterangan;
				hadiah.lspd_id = 94;
				hadiah.mh_alamat_kirim = alamat;
				hadiah.mh_kota_kirim = kota;
				hadiah.mh_kodepos_kirim = kodepos;
				
				hadiah.pesan = ajaxManager.updateProses(hadiah, currentUser);
				
				Pemegang pemegang = elionsManager.selectpp(hadiah.reg_spaj);
				Agen agen = elionsManager.select_detilagen(hadiah.reg_spaj);
				Hadiah jn_hadiah= uwManager.selectLstHadiah(hadiah.getLhc_id(),hadiah.getLh_id());
				String lca_id = uwManager.selectCabangFromSpaj(hadiah.reg_spaj);
				Product produk = uwManager.selectMstProductInsuredUtamaFromSpaj(hadiah.reg_spaj);
				
				double pr = produk.getMspr_premium();
				
				String team_name = uwManager.selectBancassTeam(hadiah.reg_spaj);
				if(team_name==null){
					team_name= "";
				}
				
				String cb = "";
				String email_cc = "";
				if("37,52".indexOf(lca_id)>-1) { //Agency
				       cb = "Agency";
				}else if(lca_id.equals("46")) { //Hybrid
				       cb = "Hybrid";
				}else if(lca_id.equals("09")) { //Bancassurance
				       if(team_name.toUpperCase().equals("TEAM JAN ROSADI")) { //Bancassurance2
				    	   cb = "Bancassurance 2";
				       }else if(team_name.toUpperCase().equals("TEAM DEWI")) { //Bancassurance3
				    	   cb = "Bancassurance 3";
				       }else if(team_name.toUpperCase().equals("TEAM YANTI SUMIRKAN")) { //Bancassurance1
				    	   cb = "Bancassurance 1";
				    	   email_cc = "yantisumirkan@sinarmasmsiglife.co.id";
				       }else{
				    	   cb = "Bancassurance";
				       }
				}else if(lca_id.equals("08") || lca_id.equals("42")) { //Worksite
				       cb = "Worksite";
				}else if(lca_id.equals("55")) { //DM/TM
				       cb = "DMTM";
				       email_cc= "Gideon@sinarmasmsiglife.co.id";
				}else if(lca_id.equals("58")) { //MallAssurance
				       cb = "MallAssurance";
				       email_cc= "Gideon@sinarmasmsiglife.co.id";
				}else { //Regional
				       cb = "Regional";
				}
				
				StringBuffer pesan = new StringBuffer();
				//Format isi email
				
				pesan.append("<p><strong>RESCHEDULE</strong> <br>");
				pesan.append("<p><strong>Permintaan untuk reschedule pengiriman hadiah nasabah</strong> <br>");
				pesan.append("- Nama\t\t\t : "+pemegang.getMcl_first()+" ("+FormatString.nomorSPAJ(hadiah.reg_spaj)+")<br>");
				pesan.append("- No Polis\t\t : "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+"<br>");
				pesan.append("- Premi\t\t\t : Rp. "+FormatString.formatCurrency("", new BigDecimal(pr))+"<br>");
				pesan.append("- Penutup\t\t : "+agen.getMcl_first()+" ("+cb+")<br>");
				//pesan.append("- Appointment sblmnya\t : "+FormatDate.getDayInWeekIndonesia(tgl_before)+", "+FormatDate.toIndonesian(tgl_before)+" (jam "+dw.format(tgl_before)+")<br>");
				pesan.append("- Alasan reschedule\t : "+keterangan+"<br>");
				pesan.append("- Reschedule\t\t : "+FormatDate.getDayInWeekIndonesia(hadiah.mh_tgl_kirim_hadiah)+", "+FormatDate.toIndonesian(hadiah.mh_tgl_kirim_hadiah)+" (jam "+dw.format(hadiah.mh_tgl_kirim_hadiah)+")<br>");
				
				//kirim email
				/*Email ke : 	Ferry (GA), 
								Sanga (GA), 
								Saphry (Purchasing), 
								Lucianna (Purchasing), 
								Meidy (Purchasing)
				cc : User input reschedule, 
						GM user input reschedule
						Jelita  dan Hisar*/

				String from = props.getProperty("admin.ajsjava");
				//String[] to = new String[]{"canpri@sinarmasmsiglife.co.id","Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id"};
				//String[] cc = null;
				//String[] bcc = null;
				String[] to = new String[]{"Ferryanto@sinarmasmsiglife.co.id","Sanga@sinarmasmsiglife.co.id", "Saphry@sinarmasmsiglife.co.id","lucianna@sinarmasmsiglife.co.id",
									"meidytumewu@sinarmasmsiglife.co.id"};
				String[] cc = new String[]{"Jelita@sinarmasmsiglife.co.id", "Hisar@sinarmasmsiglife.co.id","shima@sinarmasmsiglife.co.id", email_cc};
				String[] bcc = new String[]{"canpri@sinarmasmsiglife.co.id", "antasari@sinarmasmsiglife.co.id", "deddy@sinarmasmsiglife.co.id"};
				String message = pesan.toString();
				String subject = "Program Hadiah Stable Save (Reschedule pengiriman hadiah untuk Polis "+FormatString.nomorPolis(pemegang.getMspo_policy_no())+")";
				List<File> attachments = new ArrayList<File>();
				attachments = null;
				
				EmailPool.send("Program Hadiah E-Lions", 1, 1, 0, 0, null, 0, 0, elionsManager.selectSysdate(), null, true, from, to, cc, bcc, subject, message, attachments, hadiah.reg_spaj);
				
				//hadiah.pesan = "Sukses";
				cmd.put("pesan", "Sukses");
			}
			
		}
		
		List jam = new ArrayList();
		List menit = new ArrayList();
		
		if(hadiah!=null){
			cmd.put("tgl", dd.format(tg));
			cmd.put("jams", Integer.parseInt(dj.format(hadiah.mh_tgl_kirim_hadiah)));
			cmd.put("mnts", Integer.parseInt(dm.format(hadiah.mh_tgl_kirim_hadiah)));
			if(spaj!=null && !spaj.equals("")){
				cmd.put("spaj", spaj);
				cmd.put("almt_kirim", hadiah.mh_alamat_kirim);
				cmd.put("kota_kirim", hadiah.mh_kota_kirim);
				cmd.put("kdpos_kirim", hadiah.mh_kodepos_kirim);
				cmd.put("lspd_id", hadiah.lspd_id);
				cmd.put("ket", hadiah.mh_keterangan);
				
				//set minimum jam berdasarkan Kota
				
				if(hadiah.mh_kota_kirim.toUpperCase().contains("JAKARTA")){
					for(int i=11;i<=17;i++){
						HashMap jm = new HashMap();
						jm.put("value",i);
						jm.put("label",i);
						
						jam.add(jm);
					}					
				}else if((hadiah.mh_kota_kirim.toUpperCase().contains("BEKASI") || hadiah.mh_kota_kirim.toUpperCase().contains("TANGERANG") || hadiah.mh_kota_kirim.toUpperCase().contains("DEPOK") || hadiah.mh_kota_kirim.toUpperCase().contains("BOGOR"))){
					for(int i=15;i<=17;i++){
						HashMap jm = new HashMap();
						jm.put("value",i);
						jm.put("label",i);
						
						jam.add(jm);
					}
				}else{
					for(int i=8;i<=17;i++){
						HashMap jm = new HashMap();
						jm.put("value",i);
						jm.put("label",i);
						
						jam.add(jm);
					}
				}
				
				for(int i=0;i<60;i++){
					HashMap mnt = new HashMap();
					mnt.put("value",i);
					mnt.put("label",i);
					
					menit.add(mnt);
				}
				
				cmd.put("jam", jam);
				cmd.put("menit", menit);
			}
		}else{
			for(int i=8;i<=17;i++){
				HashMap jm = new HashMap();
				jm.put("value",i);
				jm.put("label",i);
				
				jam.add(jm);
			}
			
			for(int i=0;i<60;i++){
				HashMap mnt = new HashMap();
				mnt.put("value",i);
				mnt.put("label",i);
				
				menit.add(mnt);
			}
			
			cmd.put("jam", jam);
			cmd.put("menit", menit);
		}
		
		return new ModelAndView("bas/reschedule_hadiah", cmd);
	}
	
	/**
	 * Menu add hadiah untuk program hadiah power save
	 * 
	 * @author Canpri
	 * @since 01 Apr 2012
	 */		
	public ModelAndView addHadiah(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map<String, Object> cmd = new HashMap<String, Object>();
		
		Hadiah hadiah = new Hadiah();		
		String pesan = "";
		
		//bind data
		ServletRequestDataBinder binder = createBinder(request, hadiah);
		
		if(request.getParameter("btnSimpan") != null) {
			String nama_hadiah = ServletRequestUtils.getStringParameter(request, "lh_nama");
			String premi = ServletRequestUtils.getStringParameter(request, "lh_harga", "0");
			String standard = ServletRequestUtils.getStringParameter(request, "flag_standard", "0");
			String aktif = ServletRequestUtils.getStringParameter(request, "flag_aktif","0");
			
			pesan = uwManager.saveJenisHadiahPS(nama_hadiah, premi, standard, aktif, currentUser.getLus_id());
		}
		
		if(request.getParameter("btnAktif") != null) {
			String check[] = request.getParameterValues("chbox");
			String lhc_id = "8";
			
			if(request.getParameterValues("chbox") != null){
				for(int i=0;i<check.length;i++){
					//String lh_id = ServletRequestUtils.getStringParameter(request, "lh_id");
					String lh_id = check[i];
					
					pesan = uwManager.updateJenisHadiah(lh_id, lhc_id, "aktif");
				}
			}
		}
		
		if(request.getParameter("btnNonAktif") != null) {
			String check[] = request.getParameterValues("chbox");
			String lhc_id = "8";
			
			if(request.getParameterValues("chbox") != null){
				for(int i=0;i<check.length;i++){
					//String lh_id = ServletRequestUtils.getStringParameter(request, "lh_id");
					String lh_id = check[i];
					
					pesan = uwManager.updateJenisHadiah(lh_id, lhc_id, "non_aktif");
				}
			}
		}
		
		cmd.put("pesan", pesan);
		cmd.put("lst_hadiah", uwManager.selectHadiahPS(null));
		return new ModelAndView("bas/add_hadiah", cmd);
	}
}