 package com.ekalife.elions.web.uw;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.dao.UwDao;
import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.BillingWS;
import com.ekalife.elions.model.CmdSpajExpired;
import com.ekalife.elions.model.Cnote;
import com.ekalife.elions.model.DataUsulan2;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Detail;
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Reas;
import com.ekalife.elions.model.Rekening_auto_debet;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Simcard;
import com.ekalife.elions.model.TrackingHistory;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.sms.Smsserver_in;
import com.ekalife.elions.model.sms.Smsserver_out;
import com.ekalife.elions.model.sms.Smsserver_out_hist;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.HitungBungaPremi;
import com.ekalife.utils.LazyConverter;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentMultiController;
import com.ibatis.common.resources.Resources;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import id.co.sinarmaslife.std.model.vo.DropDown;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * @author Yusuf
 * @since Jan 11, 2006
 */
public class ViewerMultiController extends ParentMultiController{
	protected final Log logger = LogFactory.getLog( getClass() );
	private static final HitungBungaPremi hitungBungaPremi = null;

	public ModelAndView rekening(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String spaj = request.getParameter("spaj"); 
		Map map = new HashMap();
		Rekening_client rekening = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String dept = currentUser.getLde_id();
		
		if("01, 11".indexOf(dept.trim())==-1) {
			map.put("notUnderWriting", "true");
		}
		
		if(request.getParameter("save") != null) {
			rekening = new Rekening_client();
			ServletRequestDataBinder binder = new ServletRequestDataBinder(rekening);
			binder.registerCustomEditor(Date.class, "tgl_surat", dateEditor);
			binder.bind(request);
			BindingResult err = binder.getBindingResult();

			if("01, 11".indexOf(dept.trim())==-1) {
				err.reject("lus_id", "Maaf, tetapi anda tidak bisa melakukan perubahan nomor rekening.");
			}else {
//				gabungkan no rekening
				String mrc_no="";
				for (int i = 0; i < rekening.getMrc_no_ac_split().length; i++) {
					if(rekening.getMrc_no_ac_split()[i]!=null){
						mrc_no=mrc_no+rekening.getMrc_no_ac_split()[i];
					}
				}
				rekening.setMrc_no_ac(mrc_no);
				logger.info("MRC NO = "+rekening.getMrc_no_ac());
				if(rekening.getMrc_kuasa()==1 && rekening.getTgl_surat()==null) err.reject("tgl_surat", "Silahkan Masukkan tanggal Surat Kuasa");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "lsbp_id", "", "Harap Pilih Bank");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "mrc_no_ac", "", "Harap Masukkan Nomor Rekening");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "mrc_nama", "", "Harap Masukkan Atas Nama Rekening");
				ValidationUtils.rejectIfEmptyOrWhitespace(err, "keterangan", "", "Harap Masukkan Keterangan Perubahan Nomor Rekening");
			}
			if(err.getErrorCount()==0) {
				rekening.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				rekening.setReg_spaj(spaj);
				rekening.setTypeRek(8);
				elionsManager.updateRekeningNasabah(rekening);
			}else {
				map.put("err", err.getAllErrors());
			}
			
		}else {
			rekening = this.elionsManager.selectRekeningNasabah(spaj);
			if(rekening!=null){
				rekening.setMrc_no_ac_split(FormatString.splitWordToCharacter(rekening.getMrc_no_ac(),21));
			}
		}
		
		map.put("rekening", rekening);
		map.put("bang", this.uwManager.selectBankPusat());
		map.put("select_kurs",DroplistManager.getInstance().get("KURS.xml","ID",request));
		map.put("select_jenis_tabungan",DroplistManager.getInstance().get("jenis_tabungan.xml","ID",request));
		map.put("select_jenis_nasabah",DroplistManager.getInstance().get("jenis_nasabah.xml","ID",request));
		map.put("sysDate", elionsManager.selectSysdateSimple());
		map.put("historyRekening", elionsManager.selectHistoryRekeningNasabah(spaj,8));
		
		Map pribadi = elionsManager.selectInfoPribadi(spaj);
		String ls_region = (String) pribadi.get("REGION");
		if(ls_region.startsWith("0914")) map.put("enableJenisTabungan", "true");
		
		map.put("virtual_account", uwManager.selectVirtualAccountSpaj(spaj));
		
		return new ModelAndView("uw/viewer/viewrekening", "cmd", map);
	}
	public ModelAndView reportAutoDebet(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
		Date sysdate=elionsManager.selectSysdate();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
//		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		

		map.put("spaj",spaj);
//		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/viewer/reportAutoDebet", map);
	}
	
	public ModelAndView autodebet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
				String spaj = request.getParameter("spaj");
				String flag = request.getParameter("flag_jn_tabungan");
			    String flag2 = request.getParameter("lsbp_id");
				Map map = new HashMap();
				Rekening_auto_debet rekening = null;
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				String dept = currentUser.getLde_id();
				rekening =elionsManager.selectRekeningAutoDebet(spaj);
				if("01, 11".indexOf(dept.trim())==-1) {
					map.put("notUnderWriting", "true");
				}		
				
				if(request.getParameter("save") != null) {
					rekening = new Rekening_auto_debet();
					ServletRequestDataBinder binder = new ServletRequestDataBinder(rekening);
					binder.registerCustomEditor(Date.class, "tgl_surat", dateEditor);
					binder.bind(request);
					BindingResult err = binder.getBindingResult();		
		
					if("01, 11".indexOf(dept.trim())==-1) {
						err.reject("lus_id", "Maaf, tetapi anda tidak bisa melakukan perubahan nomor rekening.");
					}else if (flag.equals("1"))
					{	if(flag2.equals("28") || flag2.equals("132")) {
							err.reject("lus_id", "Maaf, Jenis TABUNGANKU untuk Bank BCA dan MANDIRI tidak bisa digunakan untuk Autodebet");}
						
					}else {
		//				gabungkan no rekening
						String mrc_no="";
						for (int i = 0; i < rekening.getMrc_no_ac_split().length; i++) {
							if(rekening.getMrc_no_ac_split()[i]!=null){
								mrc_no=mrc_no+rekening.getMrc_no_ac_split()[i];
							}
						}
						rekening.setMrc_no_ac(mrc_no);
						//if(rekening.getLbn_id().equals("")) rekening.setLbn_id(rekening.getLsbp_id().substring(0,rekening.getLsbp_id().indexOf('-')));
						//logger.info("MRC NO = "+rekening.getMrc_no_ac());
						//if(rekening.getMrc_kuasa()==1 && rekening.getTgl_surat()==null) err.reject("tgl_surat", "Silahkan Masukkan tanggal Surat Kuasa");
						ValidationUtils.rejectIfEmptyOrWhitespace(err, "lsbp_id", "", "Harap Pilih Bank");
						ValidationUtils.rejectIfEmptyOrWhitespace(err, "mrc_no_ac", "", "Harap Masukkan Nomor Rekening");
						ValidationUtils.rejectIfEmptyOrWhitespace(err, "mar_holder", "", "Harap Masukkan Atas Nama Rekening");
						ValidationUtils.rejectIfEmptyOrWhitespace(err, "keterangan", "", "Harap Masukkan Keterangan Perubahan Nomor Rekening");
	 			}
					if(err.getErrorCount()==0) {
						if(rekening != null){
							rekening.setLus_id(Integer.valueOf(currentUser.getLus_id()));
							rekening.setReg_spaj(spaj);
							//if(rekening.getLbn_id().equals("")) rekening.setLbn_id(rekening.getLsbp_id().substring(0,rekening.getLsbp_id().indexOf('-')));
							rekening.setTypeRek(9);
							uwManager.updateAutodebetNasabah(rekening);
							//MANTA err.reject("lus_id", "Berhasil");
							map.put("scc", "true");
						}
					}else {
						map.put("err", err.getAllErrors());
					}			
				}else {
					rekening =elionsManager.selectRekeningAutoDebet(spaj);
					if (rekening !=null){
						String nm_bank=rekening.getBank();
						String mrc_no=rekening.getMar_acc_no();
						rekening.setMrc_no_ac_split(FormatString.splitWordToCharacter(rekening.getMar_acc_no(),21));
						
						String mrc_nama= rekening.getMar_holder();
						String mar_acc_no1=rekening.getMar_acc_no();
						String mata_uang= rekening.getLku_id();
						if (mata_uang.equals("01")){
							rekening.setMrc_kurs("RP");
						}else
							rekening.setMrc_kurs("$");
					}			
				}
					
				List<DropDown> jenisTab = new ArrayList<DropDown>();
				jenisTab.add(new DropDown("1","Credit Card"));
				jenisTab.add(new DropDown("2","Tabungan"));
				if(rekening != null) {
					rekening.setLsbp_id(uwManager.getBankPusat(rekening.getLbn_id()));
					if(rekening.getMrc_no_ac_lama() == null) rekening.setMrc_no_ac_lama(rekening.getMar_acc_no());
				}
				map.put("rekening", rekening);
				map.put("bang", uwManager.selectBank());
				map.put("select_kurs",DroplistManager.getInstance().get("KURS.xml","ID",request));
				map.put("select_jenis_tabungan",jenisTab);
				map.put("select_jenis_nasabah",DroplistManager.getInstance().get("jenis_nasabah.xml","ID",request));
				//map.put("sysDate", elionsManager.selectSysdateSimple());
				map.put("historyRekening", elionsManager.selectHistoryRekeningNasabah(spaj,9));
				
				return new ModelAndView("uw/viewer/viewrekeningautodebet", "cmd", map);
			}

	
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        map.put("currentUser", currentUser);
        
         //hanya IT dan UW
        if(currentUser.getLde_id().equals("01") || currentUser.getLde_id().equals("11")){
            double angka = (double) currentUser.getScreenWidth() / (double) currentUser.getScreenHeight();
            if(angka > 1.4) {
            	map.put("wideScreen", true);
            }
        }
        
        //dept yg boleh buka payment?
		if(this.props.getProperty("access.viewer.payment").indexOf(currentUser.getLde_id())==-1) {
			map.put("paymentDisabled", "disabled");
		}
		
		//user yg boleh buka tombol CS Call dan CS Summary?
		//if(this.props.getProperty("access.cs.summary").indexOf(currentUser.getLus_id())>-1) {
		//	map.put("summaryEnabled", "enabled");
		//}
		//user yg boleh buka tombol CS Summary saja?
		//if(this.props.getProperty("access.cs.call").indexOf(currentUser.getLus_id())>-1) {
		if( "01,11,12,21,27,04,10,19,39".indexOf(currentUser.getLde_id())==-1) {
			map.put("viewDokumenPolis", "disabled");
		}
		
		//Ridhaal - Tombol Status di Viewer di tutup untuk Collection
		if( "01,11,12,21,27,04,10,19".indexOf(currentUser.getLde_id())==-1) {
			map.put("viewDokumenPolisUW", "disabled");
		}
		
		//Anta - Request by RezaKurniawan untuk membuka akses Button Dokumen
		if(!currentUser.getLus_id().equals("2658") && "01,11,12,21,27,04,10,19,29".indexOf(currentUser.getLde_id())==-1) {
			map.put("viewDokumenPolisBanc", "disabled");
		}
		if(currentUser.getLus_id().equals("148") || currentUser.getLus_id().equals("660")) {
			map.put("viewDokumenPolis", "");
			map.put("viewDokumenPolisBanc", "");
		}
		if("01,11,12,27,04,07".indexOf(currentUser.getLde_id())==-1) {
			map.put("viewDokumenPolisNew", "disabled");
		}		
		if("12".indexOf(currentUser.getLde_id())>-1) {
			map.put("callEnabled", "enabled");
			map.put("summaryEnabled", "enabled");
			int jumlahCustomerYangHarusDiFollowUp = this.uwManager.selectCsfCallReminder();
			if(jumlahCustomerYangHarusDiFollowUp > 0) map.put("showCsfReminder", "true");
		}
		// Andhika (06/11/2013) - Helpdesk 43734
		if( (!currentUser.getLus_id().equals("1791")) && (!currentUser.getLus_id().equals("2029"))&& (!currentUser.getLus_id().equals("2210")) ) {
			map.put("dsDisabled", "disabled");
		}			
		
		// Andhika (05/06/2013) - helpdesk ACHMAD RIYAN
		if(currentUser.getLus_id().equals("3951") || ((currentUser.getJn_bank().intValue() == 2 || currentUser.getJn_bank().intValue() == 3) && !currentUser.getLus_id().equals("3504"))){ // 2678
			return new ModelAndView("uw/viewer/viewer_sekuritas", "cmd", map);
		}else if(currentUser.getLde_id().equals("19") || currentUser.getLde_id().equals("20")) {
			if(currentUser.getLca_id().equals("37")){
				// Andhika (29/08/2013) Req : YUDI HERNAWAN Permohonan #39590
				return new ModelAndView("uw/viewer/viewer_cabang_agency", "cmd", map);
			}else{
				return new ModelAndView("uw/viewer/viewer_cabang", "cmd", map);
			}
		}else {
			return new ModelAndView("uw/viewer/viewer", "cmd", map);
		}
	}

	public ModelAndView csfreminder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		map.put("daftar", this.uwManager.selectCsfCallReminderList());
		return new ModelAndView("uw/viewer/viewcsfreminder", "cmd", map);
	}
	
	public ModelAndView cssms_in(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String[] panel=ServletRequestUtils.getStringParameters(request, "pane");
		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
		String edate = ServletRequestUtils.getStringParameter(request, "edate");
		String jn_report = ServletRequestUtils.getStringParameter(request, "jn_report");
		String user = ServletRequestUtils.getStringParameter(request, "seluserCs");
		String report = ServletRequestUtils.getStringParameter(request, "showReport");
		
		String pane="1";
		if(panel.length!=0){
			pane=panel[panel.length-1];
		}		
		//Special-Case @ANDY-04/08/2015: SMS Viewer Dibukakan buat Bu MIE YOEN (Dept:LB)
		if(!currentUser.getLus_id().trim().equals("91")){
			String dept = currentUser.getLde_id();
			if("01, 12".indexOf(dept.trim())==-1) {
				map.put("notCS", "true");
			}
		}
		map.put("pane",pane);
		
		// HANDLE PANE-1 
		String noPage = "";
        String noRow = "20";
        String cPage = request.getParameter("cPage");
        if(request.getParameter("cPage") == null || "".equals(request.getParameter("cPage")) ){
        	noPage = "1";
        }else{
        	noPage = request.getParameter("cPage");
        	noPage = ServletRequestUtils.getStringParameter(request, "cPage");
        }
        Map param = new HashMap();      
        param.put("noRow", noRow);
        String totalPage = bacManager.selectTotalSmsserver_in(null, null, null, 0,null,null,null,param);
        
        // HANDLE PAGE LESS OR PAGE OVER
        if(Integer.parseInt(noPage) < 1){
        	noPage = "1";
        }
        
        if(Integer.parseInt(noPage) > Integer.parseInt(totalPage)){
        	noPage = totalPage;
        }
        
        List<Smsserver_in> list = new ArrayList<Smsserver_in>();
        Map prm = new HashMap();	
		prm.put("noRow", noRow);
		prm.put("noPage", noPage);
		//map.put("daftar", this.uwManager.selectSmsserver_in(null, null, null, 0,null,null,null, prm));		
		list = this.uwManager.selectSmsserver_in(null, null, null, 0,null,null,null, prm);		
		request.setAttribute("daftar", list);
			
		int currPage = Integer.parseInt(noPage); 
		request.setAttribute("currPage", noPage);
    	request.setAttribute("firstPage", 1 + "");
    	request.setAttribute("lastPage", totalPage);        	
    	request.setAttribute("nextPage", (currPage + 1) + "");
    	request.setAttribute("previousPage", (currPage - 1) + "");  
		    	
    	 // HANDLE PANE-2 
    	String noPage2 = "";       
        String cPage2 = request.getParameter("cPage2");
        if(request.getParameter("cPage2") == null || "".equals(request.getParameter("cPage2")) ){
        	noPage2 = "1";
        }else{
        	noPage2 = request.getParameter("cPage2");
        	noPage2 = ServletRequestUtils.getStringParameter(request, "cPage2");
        }
        param = new HashMap();      
        param.put("noRow", noRow);
        totalPage = bacManager.selectTotalSmsserver_in(null, null, null, null,null,Integer.parseInt(currentUser.getLus_id()),1, param);
        // HANDLE PAGE LESS OR PAGE OVER
        if(Integer.parseInt(noPage2) < 1){
        	noPage2 = "1";
        }
        
        if(Integer.parseInt(noPage2) > Integer.parseInt(totalPage)){
        	noPage2 = totalPage;
        }
        List<Smsserver_in> list2 = new ArrayList<Smsserver_in>();
        prm = new HashMap();	
		prm.put("noRow", noRow);
		prm.put("noPage", noPage2);		
		//map.put("daftarFollowup", this.uwManager.selectSmsserver_in(null, null, null, null,null,Integer.parseInt(currentUser.getLus_id()),1, null));
		list2 = this.uwManager.selectSmsserver_in(null, null, null, null,null,Integer.parseInt(currentUser.getLus_id()),1, prm);
		request.setAttribute("daftarFollowup", list2);
		
		int currPage2 = Integer.parseInt(noPage2); 
		request.setAttribute("currPage2", noPage2);
    	request.setAttribute("firstPage2", 1 + "");
    	request.setAttribute("lastPage2", totalPage);        	
    	request.setAttribute("nextPage2", (currPage2 + 1) + "");
    	request.setAttribute("previousPage2", (currPage2 - 1) + "");  
		
    	// HANDLE PANE-3 
		List<Smsserver_in> daftarFollowupSPV = new ArrayList<Smsserver_in>();
		if("558".equals(currentUser.getLus_id()) || "1436".equals(currentUser.getLus_id()) || "1651".equals(currentUser.getLus_id()) ||
				"1653".equals(currentUser.getLus_id()) || "1493".equals(currentUser.getLus_id()) || "91".equals(currentUser.getLus_id())){
			
			String noPage3 = "";       
	        String cPage3 = request.getParameter("cPage3");
	        if(request.getParameter("cPage3") == null || "".equals(request.getParameter("cPage3")) ){
	        	noPage3 = "1";
	        }else{
	        	noPage3 = request.getParameter("cPage3");
	        	noPage3 = ServletRequestUtils.getStringParameter(request, "cPage3");
	        }
	        param = new HashMap();      
	        param.put("noRow", noRow);
	        totalPage = bacManager.selectTotalSmsserver_in(null, null, null, null,null,null,1, param);
	        // HANDLE PAGE LESS OR PAGE OVER
	        if(Integer.parseInt(noPage3) < 1){
	        	noPage3 = "1";
	        }
	        
	        if(Integer.parseInt(noPage3) > Integer.parseInt(totalPage)){
	        	noPage3 = totalPage;
	        }
			
	        List<Smsserver_in> list3 = new ArrayList<Smsserver_in>();
	        prm = new HashMap();	
			prm.put("noRow", noRow);
			prm.put("noPage", noPage3);				
			list3 = this.uwManager.selectSmsserver_in(null, null, null, null,null,null,1, prm);
			request.setAttribute("daftarFollowupSPV", list3);
			//daftarFollowupSPV = this.uwManager.selectSmsserver_in(null, null, null, null,null,null,1, null);
			//map.put("daftarFollowupSPV", daftarFollowupSPV);
			
			int currPage3 = Integer.parseInt(noPage3); 
			request.setAttribute("currPage3", noPage3);
	    	request.setAttribute("firstPage3", 1 + "");
	    	request.setAttribute("lastPage3", totalPage);        	
	    	request.setAttribute("nextPage3", (currPage3 + 1) + "");
	    	request.setAttribute("previousPage3", (currPage3 - 1) + "");  
		}
		
		// HANDLE PANE-5
		String noPage5 = "";
		String cPage5 = request.getParameter("cPage5");
		if(request.getParameter("cPage5") == null || "".equals(request.getParameter("cPage5"))) {
			noPage5 = "1";
		} else {
			noPage5 = request.getParameter("cPage5");
		}
		
		Integer search5 = ServletRequestUtils.getIntParameter(request, "search5", 1);
		String tgl_kirim1 = request.getParameter("tgl_kirim1");
		String tgl_kirim2 = request.getParameter("tgl_kirim2");
		String tgl_create1 = request.getParameter("tgl_create1");
		String tgl_create2 = request.getParameter("tgl_create2");
		String tgl_status1 = request.getParameter("tgl_status1");
		String tgl_status2 = request.getParameter("tgl_status2");
		String nowDate = defaultDateFormat.format(bacManager.selectSysdate());
		
		String reg_spaj5 = request.getParameter("reg_spaj5");
		String no_polis5 = request.getParameter("no_polis5");
		String status5 = request.getParameter("status5");
		String no_hp5 = request.getParameter("no_hp5");
		String format_hp5 = null;
		if(no_hp5 != null) {
			if("0".equals(no_hp5.substring(0, 1)))
				format_hp5 = "+62" + no_hp5.substring(1);
			else if("62".equals(no_hp5.substring(0, 3)))
				format_hp5 = "+" + no_hp5;
			else
				format_hp5 = no_hp5;
		}
		
		param = new HashMap();
		param.put("noRow", noRow);
		if(search5 == 1 && tgl_kirim1 != null && tgl_kirim2 != null) {
			param.put("beg_date", tgl_kirim1);
			param.put("end_date", tgl_kirim2);
		} else if(search5 == 2 && reg_spaj5 != null) {
			param.put("reg_spaj", reg_spaj5);
		} else if(search5 == 3 && no_polis5 != null) {
			param.put("no_polis", no_polis5);
		} else if(search5 == 4 && format_hp5 != null) {
			param.put("no_hp", format_hp5);
		} else if(search5 == 5 && status5 != null) {
			param.put("status", status5);
			param.put("create_date1", tgl_status1);
			param.put("create_date2", tgl_status2);
		} else if(search5 == 6 && tgl_create1 != null && tgl_create2 != null) {
			param.put("create_date1", tgl_create1);
			param.put("create_date2", tgl_create2);
		}
		totalPage = bacManager.selectTotalSmsserver_out(null, null, null, null, null, null, param);
		String totalResult = bacManager.selectTotalResultSmsserver_out(null, null, null, null, null, null, param);
        // HANDLE PAGE LESS OR PAGE OVER
        if(Integer.parseInt(noPage5) < 1){
        	noPage5 = "1";
        }
        
        if(Integer.parseInt(noPage5) > Integer.parseInt(totalPage)){
        	noPage5 = totalPage;
        }
        
        ArrayList<Smsserver_out> list5 = new ArrayList<Smsserver_out>();
        prm = new HashMap();
        prm.put("noRow", noRow);
        prm.put("noPage", noPage5);
		if(search5 == 1 && tgl_kirim1 != null && tgl_kirim1 != nowDate && tgl_kirim2 != null) {
			prm.put("beg_date", tgl_kirim1);
			prm.put("end_date", tgl_kirim2);
		} else if(search5 == 2 && reg_spaj5 != null) {
			prm.put("reg_spaj", reg_spaj5);
		} else if(search5 == 3 && no_polis5 != null) {
			prm.put("no_polis", no_polis5);
		} else if(search5 == 4 && format_hp5 != null) {
			prm.put("no_hp", format_hp5);
		} else if(search5 == 5 && status5 != null) {
			prm.put("status", status5);
			prm.put("create_date1", tgl_status1);
			prm.put("create_date2", tgl_status2);
		} else if(search5 == 6 && tgl_create1 != null && tgl_create2 != null) {
			prm.put("create_date1", tgl_create1);
			prm.put("create_date2", tgl_create2);
		}
        list5 = Common.serializableList(bacManager.selectSmsserver_out(null, null, null, null, null, null, prm));
        request.setAttribute("daftarSmsOut", list5);
		
		int currPage5 = Integer.parseInt(noPage5); 
		request.setAttribute("currPage5", noPage5);
    	request.setAttribute("firstPage5", 1 + "");
    	request.setAttribute("lastPage5", totalPage);        	
    	request.setAttribute("nextPage5", (currPage5 + 1) + "");
    	request.setAttribute("previousPage5", (currPage5 - 1) + "");  
    	request.setAttribute("totalResult5", totalResult);
    	
    	request.setAttribute("search5", search5);
    	request.setAttribute("tgl_kirim1", (tgl_kirim1 == null) ? nowDate : tgl_kirim1);
    	request.setAttribute("tgl_kirim2", (tgl_kirim2 == null) ? nowDate : tgl_kirim2);
    	request.setAttribute("reg_spaj5", reg_spaj5);
    	request.setAttribute("no_polis5", no_polis5);
    	request.setAttribute("no_hp5", no_hp5);
    	request.setAttribute("status5", status5);
    	request.setAttribute("tgl_create1", (tgl_create1 == null) ? nowDate : tgl_create1);
    	request.setAttribute("tgl_create2", (tgl_create2 == null) ? nowDate : tgl_create2);
    	request.setAttribute("tgl_status1", (tgl_status1 == null) ? nowDate : tgl_status1);
    	request.setAttribute("tgl_status2", (tgl_status2 == null) ? nowDate : tgl_status2);
    	
    	if((search5 == 1 && tgl_kirim1 != null) || search5 > 1)
    		map.put("pane", 5);
    	
    	// HANDLE PANE-6
    	Date today = uwManager.selectSysdateTruncated(0);
    	String month6 = ServletRequestUtils.getStringParameter(request, "month6", new SimpleDateFormat("MM").format(today));
    	String year6 = ServletRequestUtils.getStringParameter(request, "year6", new SimpleDateFormat("yyyy").format(today));
    	
    	HashMap<String, String> monthMap = new HashMap<String, String>();
    	ArrayList<HashMap<String, String>> monthList = new ArrayList<HashMap<String,String>>();
    	for(int i = 1; i <= 12; i++) {
    		monthMap = new HashMap<String, String>();
    		String month = "";
    		if(i < 10) month = "0" + i;
    		else month = String.valueOf(i);
    		
    		monthMap.put("value", month);
    		monthMap.put("label", new SimpleDateFormat("MMMM").format(new SimpleDateFormat("MM").parse(month)));
    		monthList.add(monthMap);
    	}
    	
    	ArrayList<String> yearList = new ArrayList<String>();
    	int thisYear = Integer.valueOf(new SimpleDateFormat("yyyy").format(today));
    	for(int i = 2014; i <= thisYear; i++) {
    		yearList.add(String.valueOf(i));
    	}
    	
    	map.put("monthList", monthList);
    	map.put("yearList", yearList);
    	map.put("month6", month6);
    	map.put("year6", year6);
		
		map.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	map.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	
		List userCS = uwManager.selectUserCSsms(props.getProperty("user.cs.sms"));
		map.put("userCS",userCS);
		List data=new ArrayList();
		File sourceFile;
		if(bdate!=null && edate!=null){			
			if(jn_report.equals("0")){
				data.clear();
				data=uwManager.selectReportSMSHarian(user, bdate, edate);
				sourceFile = Resources.getResourceAsFile(props.getProperty("report_sms_cs_user_harian") + ".jasper");
			}else{
				data.clear();
				data=uwManager.selectReportSMSBulanan(user, bdate, edate);
				 sourceFile = Resources.getResourceAsFile(props.getProperty("report_sms_cs_user_bulanan") + ".jasper");
			}
			if(data.size() > 0){ //bila ada data
	    		
	    		ServletOutputStream sos2 = response.getOutputStream();	    		
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("tanggal1", bdate);
	    		params.put("tanggal2", edate);
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

		    	if(request.getParameter("showXLS") != null){
		    		response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos2);
		            exporter.exportReport();
		    	}else if(request.getParameter("showPDF") != null){
		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos2);
		    	}
	    		sos2.close();
    		 }else{ //bila tidak ada data
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
//    			return new ModelAndView("uw/viewer/cssms_in", "cmd", map);
    		}
			
			return null;
	    }
		
		
		return new ModelAndView("uw/viewer/cssms_in", "cmd", map);
	}
	
	public ModelAndView cssms_view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		Integer id=ServletRequestUtils.getIntParameter(request, "id",-1);
		List<String> errorMessage =new ArrayList<String>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String[] panel=ServletRequestUtils.getStringParameters(request, "pane");
		String pane="1";
		if(panel.length!=0){
			pane=panel[panel.length-1];
		}
		String dept = currentUser.getLde_id();
		if("01, 12".indexOf(dept.trim())==-1) {
			map.put("notCS", "true");
		}
		map.put("pane",pane);
		
		if(id!=-1){
			List<Smsserver_in> ls_sms=this.uwManager.selectSmsserver_in(null, null, null, null,id,null,null, null);
			if(!ls_sms.isEmpty()){
				Smsserver_in sms_in=ls_sms.get(0);
				Integer process=sms_in.getProcess();
				String proses_ket=sms_in.getProses_ket();
				String msg_reply=sms_in.getMsg_reply();
				String email_to = ServletRequestUtils.getStringParameter(request, "email_to", "");
				String email_cc = ServletRequestUtils.getStringParameter(request, "email_cc", "");
				String email_bcc = ServletRequestUtils.getStringParameter(request, "email_bcc", "");
				String email_subject = ServletRequestUtils.getStringParameter(request, "email_subject", "");
				String email_msg = ServletRequestUtils.getStringParameter(request, "email_msg", "");
				
				if(request.getParameter("save")!=null) {
					 process=ServletRequestUtils.getIntParameter(request, "process",0);
					 proses_ket=ServletRequestUtils.getStringParameter(request, "keterangan","");
					 msg_reply=ServletRequestUtils.getStringParameter(request, "msg_reply","");
					
					if(process==0){
						errorMessage.add("Pilih Status Follow Up");
					}
					
					if(process==2){
						if (msg_reply.equals("")) {
							errorMessage.add("Masukkan Isi sms balasan terlebih dahulu");
						}
					}
					
					// Follow up by email req Niko CS (#73610) - Daru 29 Juni 2015
					if(process == 5) {
						
						if("".equals(email_to)) {
							errorMessage.add("Masukkan tujuan email terlebih dahulu");
						}
						
						if("".equals(email_subject)) {
							errorMessage.add("Masukkan judul email terlebih dahulu");
						}
						
						if("".equals(email_msg)) {
							errorMessage.add("Masukkan isi email terlebih dahulu");
						}
					}
					
					if(proses_ket.equals("")){
						errorMessage.add("Masukkan keterangan terlebih dahulu");
					}
					
					if(sms_in.getProcess()!=0&sms_in.getProcess()!=4){
						User user=elionsManager.selectUser(sms_in.getLus_id().toString());
						errorMessage.add("SMS ini sudah di follow up oleh "+user.getLus_full_name());
					}
					
					if(errorMessage.isEmpty()){
						Smsserver_in sms_masuk=new Smsserver_in();
						sms_masuk.setId(sms_in.getId());
						sms_masuk.setLus_id(Integer.parseInt(currentUser.getLus_id()));
						sms_masuk.setProcess(process);
						sms_masuk.setProcess_date(elionsManager.selectSysdate1("dd", false, 0));
						sms_masuk.setProses_ket(proses_ket);
						if(process==2){
							Smsserver_out sms_out=new Smsserver_out();
							sms_out.setText(msg_reply);
							sms_out.setJenis(19);
							sms_out.setLjs_id(19);
							sms_out.setRecipient(sms_in.getOriginator());
//							sms_out.setRecipient("08159125426");
							sms_out.setId_refrence(id);
							sms_out.setLus_id(Integer.parseInt(currentUser.getLus_id()));
							Boolean result=uwManager.smsProsesUpdate(sms_masuk,sms_out);
							if(result){
								map.put("result", "Update Follow Up SMS Berhasil");
								sms_in=this.uwManager.selectSmsserver_in(null, null, null, null,id,null,null,null).get(0);
							}else{
								errorMessage.add("Update Follow Up SMS Gagal");
							}
						} else if(process == 5) {// Follow up by email
							Boolean result = EmailPool.send("SMiLe E-Lions", 0, 0, 0, 0, null, 0, 
												Integer.valueOf(currentUser.getLus_id()), elionsManager.selectSysdate(), null, true, 
												props.getProperty("admin.ajsjava"), 
												email_to.split(";"), 
												email_cc.split(";"), 
												email_bcc.split(";"), 
												"[Follow Up SMS By Email oleh " + currentUser.getName() + 
												" (" + currentUser.getDept() + ")] " +
												email_subject, 
												email_msg, 
												null, 
												null);
							if(result) {
								Boolean update = uwManager.smsProsesUpdate(sms_masuk, null);
								if(update) {
									map.put("result", "Update Follow Up SMS Berhasil");
									sms_in = this.uwManager.selectSmsserver_in(null, null, null, null, id, null, null, null).get(0);
									email_to = "";
									email_cc = "";
									email_bcc = "";
									email_subject = "";
									email_msg = "";
								} else {
									errorMessage.add("Update Follow Up SMS Gagal");
								}
							} else {
								errorMessage.add("Update Follow Up SMS Gagal");
							}
						}else{
							Boolean result=uwManager.smsProsesUpdate(sms_masuk,null);
							if(result){
								map.put("result", "Update Follow Up SMS Berhasil");
								sms_in=this.uwManager.selectSmsserver_in(null, null, null, null,id,null,null,null).get(0);
							}else{
								errorMessage.add("Update Follow Up SMS Gagal");
							}
						}
					}
					
				}
				map.put("sms_in", sms_in);
				map.put("process", process);
				map.put("keterangan", proses_ket);
				map.put("msg_reply", msg_reply);
				map.put("email_to", email_to);
				map.put("email_cc", email_cc);
				map.put("email_bcc", email_bcc);
				map.put("email_subject", email_subject);
				map.put("email_msg", email_msg);
				//sms in history
				String originator=sms_in.getOriginator();
				
				if(originator.contains("+62")){
					originator=originator.replace("+62", "");
				}else if(originator.substring(0, 2).equals("62")){
					originator=originator.substring(2, originator.length());
				}
				
				map.put("sms_in_hist", uwManager.selectSmsserver_in_by_originator(originator));
				map.put("sms_out_hist", uwManager.selectSmsserver_out_by_recipient(originator));
			}else{
				errorMessage.add("SMS TIDAK DITEMUKAN");
			}
		}else{
			errorMessage.add("SMS TIDAK DITEMUKAN");
		}
		map.put("errorMessage", errorMessage);
		map.put("id", id);
		
		return new ModelAndView("uw/viewer/cssms_view", "cmd", map);
	}
	
	public ModelAndView csfsummary(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		map.put("daftar", this.uwManager.selectCsfCallSummary(request.getParameter("spaj")));
		return new ModelAndView("uw/viewer/viewcsfsummary", "cmd", map);
	}
	
	public ModelAndView csfcall(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map map = new HashMap();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String inout = ServletRequestUtils.getStringParameter(request, "inout", "I");
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		if(request.getParameter("save")!=null) {
			List<String> errorMessage = this.elionsManager.saveCsfCall(spaj, inout, currentUser.getLus_id(), 
					request.getParameterValues("s_dial"),
					request.getParameterValues("s_jenis"),
					request.getParameterValues("s_ket"),
					request.getParameterValues("s_start"),
					request.getParameterValues("s_end"),
					request.getParameterValues("s_callback")
					);
			map.put("errorMessage", errorMessage);
		}		
		List callList = this.uwManager.selectCsfCall(spaj, inout, currentUser.getLus_id());
		
		map.put("callList", callList);
		map.put("sysDate", this.elionsManager.selectSysdateSimple());
		map.put("spaj", spaj);
		map.put("inout", inout);
		map.put("totalTambah", this.elionsManager.selectTotalCsfCall(spaj, currentUser.getLus_id()));
		return new ModelAndView("uw/viewer/viewcsfcall", "cmd", map);
	}
	
	public ModelAndView cssms_out_hist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
		ArrayList<String> errors = new ArrayList<String>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String[] panel=ServletRequestUtils.getStringParameters(request, "pane");
		String pane="1";
		if(panel.length!=0){
			pane=panel[panel.length-1];
		}
		map.put("pane",pane);
		
		//Special-Case @ANDY-04/08/2015: SMS Viewer Dibukakan buat Bu MIE YOEN (Dept:LB)
		if(!currentUser.getLus_id().trim().equals("91")){
			String dept = currentUser.getLde_id();
			if("01, 12".indexOf(dept.trim())==-1) {
				map.put("notCS", "true");
			}
		}
		
		if(id > -1) {
			ArrayList<Smsserver_out_hist> smsList = bacManager.selectSmsserver_out_hist(id);
			if(!smsList.isEmpty()) {
				map.put("smsList", smsList);
			} else {
				errors.add("SMS TIDAK DITEMUKAN");
			}
		} else {
			errors.add("SMS TIDAK DITEMUKAN");
		}
		
		if(errors.size() > 0) {
			map.put("errors", errors);
		}
		
		return new ModelAndView("uw/viewer/cssms_out_hist", map);
	}

	public ModelAndView nilaitunai(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
//		logger.info(request.getRequestURI());
//		logger.info(request.getRequestURL());
//		logger.info(request.getServletPath());
//		
//		/E-Lions/uw/viewer.htm
//		http://yusufxp/E-Lions/uw/viewer.htm
//		/uw/viewer.htm
		
		Map m = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String spaj = request.getParameter("spaj");
		if(currentUser.getLca_id().equals("01")) {
			List daftar = this.elionsManager.viewNilai(spaj, currentUser.getLus_id(), 0, 0);
			if(daftar.isEmpty()) {
				m.put("pesanError", "SPAJ dengan nomor ["+spaj+"] tidak mempunyai Nilai Tunai.");
			}
			
			m.put("daftar", daftar);
		}else {
			m.put("pesanError", "Anda tidak mempunyai akses untuk melihat Nilai Tunai.");
		}
		m.put("spaj", spaj);
		return new ModelAndView("uw/viewer/viewnilaitunai", m);
	}
	
	/**
	 * View Ilustrasi Diskon Multi Invest, REPORT > VIEWER > VIEW > BILLING > ILUSTRASI DISC (dari EAS)
	 * contoh: http://yusufxp:8081/E-Lions/viewer.htm?window=ilustrasi_disc&spaj=37200900836
	 * 
	 * @author Yusuf
	 * @since Apr 1, 2011 (10:50:10 AM)
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView ilustrasi_disc(HttpServletRequest request, HttpServletResponse response) throws Exception {

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		String today = defaultDateFormat.format(elionsManager.selectSysdateSimple());
		
		Map m = new HashMap();
		
		Map data = elionsManager.selectPremiProdukUtama(spaj);
		
		if(data != null) {
			//variabel2 dari data spaj (query)
			String kurs		= (String) data.get("LKU_ID");
			double premi	= ((BigDecimal) data.get("MSPR_PREMIUM")).doubleValue();
			m.put("kurs", kurs);
			m.put("premi", premi);

			//variabel2 yg diisi oleh user
			Date tgl_trans 	= defaultDateFormat.parse(ServletRequestUtils.getStringParameter(request, "tgl_trans", today));
			Date jt_tempo 	= defaultDateFormat.parse(ServletRequestUtils.getStringParameter(request, "jt_tempo", today));
			int kali 		= ServletRequestUtils.getIntParameter(request, "kali", 1);
			m.put("tgl_trans", tgl_trans);
			m.put("jt_tempo", jt_tempo);
			m.put("kali", kali);
			
			if(request.getParameter("hitung") != null){
				//hitung!
				m.putAll(uwManager.prosesIlustrasiDiscountMi(kurs, premi, tgl_trans, jt_tempo, kali));
			}			
			
		}else {
			m.put("pesanError", "ERROR");
		}
		
		return new ModelAndView("uw/viewer/view_ilustrasi_disc", m);
	}
	
	public ModelAndView viewerkontrol(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		//request Shima tambah akses untuk user 2210-helpdesk 57048
		if( currentUser.getLca_id().equals("01") || currentUser.getLus_id().equals("2210")) {							//PUSAT
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String tipeView = ServletRequestUtils.getStringParameter(request, "tipeView", "-1");
			map.put("spaj", spaj);
			map.put("tipeView", tipeView);
			map.put("info", this.elionsManager.selectPolisSpajPemegang(spaj));
			List otherSpaj = this.uwManager.selectAllSpaj(spaj);
			map.put("otherSpaj", otherSpaj);
			List<DropDown> tipeViewList = new ArrayList<DropDown>();	
				//Deddy (25-Sep-2012) - REQ Dr.Ingrid, untuk listing typeView nya diorder by abjad .
				tipeViewList.add(new DropDown("7","Agent")); //7
				tipeViewList.add(new DropDown("9","Akumulasi Polis")); //9
				tipeViewList.add(new DropDown("2","Billing")); //2
				tipeViewList.add(new DropDown("17","Bonus")); //17
				tipeViewList.add(new DropDown("4","Claim Nilai Tunai")); //4
				tipeViewList.add(new DropDown("6","Claim non Nilai Tunai")); //6 TODO (Yusuf) -> belum dikerjakan
				tipeViewList.add(new DropDown("13","Commission")); //13
				tipeViewList.add(new DropDown("19","Deduct")); //19
				tipeViewList.add(new DropDown("10","Maturity")); //10
				tipeViewList.add(new DropDown("16","Medis")); //16
				tipeViewList.add(new DropDown("15","NAV")); //15
				tipeViewList.add(new DropDown("0","Pinjaman")); //0
				tipeViewList.add(new DropDown("5","Powersave")); //5
				tipeViewList.add(new DropDown("11","Privasi")); //11
				tipeViewList.add(new DropDown("8","Reas")); //8
				tipeViewList.add(new DropDown("20","Referrer")); //20
				tipeViewList.add(new DropDown("12","Reinstate")); //12
				tipeViewList.add(new DropDown("14","Rewards")); //14
				tipeViewList.add(new DropDown("1","Simpanan")); //1
				tipeViewList.add(new DropDown("18","Stable Link")); //18
				tipeViewList.add(new DropDown("3","Tahapan")); //3
				map.put("tipeViewList", tipeViewList);
		}
		else if( currentUser.getLca_id().equals("09") ) // BANCASSURANCE
		{
			map.put("pesanError", "Anda tidak mempunyai akses terhadap menu ini");
		}
		else{													//CABANG
//			map.put("pesanError", "Anda tidak mempunyai akses terhadap menu ini");
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String tipeView = ServletRequestUtils.getStringParameter(request, "tipeView", "-1");
			map.put("spaj", spaj);
			map.put("tipeView", tipeView);
			map.put("info", this.elionsManager.selectPolisSpajPemegang(spaj));
			List otherSpaj = this.uwManager.selectAllSpaj(spaj);
			map.put("otherSpaj", otherSpaj);
			List<DropDown> tipeViewList = new ArrayList<DropDown>();			
        	tipeViewList.add(new DropDown("0","Pinjaman")); //0
			tipeViewList.add(new DropDown("1","Simpanan")); //1
			tipeViewList.add(new DropDown("3","Tahapan")); //3
			tipeViewList.add(new DropDown("4","Claim Nilai Tunai")); //4
			tipeViewList.add(new DropDown("5","Powersave")); //5
			tipeViewList.add(new DropDown("10","Maturity")); //10
			tipeViewList.add(new DropDown("18","Stable Link")); //18
			map.put("tipeViewList", tipeViewList);
		}
		
		return new ModelAndView("uw/viewer/viewkontrolspaj", map);
	}
	
	public ModelAndView akum(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();

		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if(currentUser.getLca_id().equals("01")) {
		}else {
			map.put("pesanError", "Anda tidak mempunyai akses terhadap menu ini");
		}
		return new ModelAndView("uw/viewer/viewkontrolspaj_akum", map);
	}
	
	public ModelAndView akum_new(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();

		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		if(currentUser.getLca_id().equals("01")) {
//		}else {
//			map.put("pesanError", "Anda tidak mempunyai akses terhadap menu ini");
//		}
		
		String pusatOrCabang = "";
		if( currentUser.getLca_id().equals("01") || currentUser.getLca_id().equals("09") ) {	// PUSAT / BANCASSURANCE			
			pusatOrCabang = "pusat";
		}else{// CABANG
			pusatOrCabang = "cabang";
		}
		
		map.put("pusatOrCabang", pusatOrCabang);
		
		return new ModelAndView("uw/viewer/viewkontrolspaj_akum_new", map);
	}
	
	public ModelAndView worksheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String copy_reg_spaj = ServletRequestUtils.getStringParameter(request, "copy_reg_spaj","");
		map.put("spaj", spaj);
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		if(currentUser.getLca_id().equals("01") || currentUser.getLca_id().equals("07") || currentUser.getLca_id().equals("11")) {
		}else {
			map.put("pesanError", "Anda tidak mempunyai akses terhadap menu ini");
		}
		return new ModelAndView(new RedirectView("worksheet.htm?spaj="+spaj+"&view=1&copy_reg_spaj="+copy_reg_spaj));
	}
	
	//TODO
	
	public ModelAndView displaytag(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lusId = "";
		String pusatOrCabang = "";
		Integer bulanKe = ServletRequestUtils.getIntParameter(request,"bulanKe",0);
		String tglSar = ServletRequestUtils.getStringParameter(request, "tglSar","");
		if( currentUser.getLca_id().equals("01") || currentUser.getLca_id().equals("09") ) {	// PUSAT / BANCASSURANCE			
			lusId = null;
			pusatOrCabang = "pusat";
		}else 										// CABANG
		{
			lusId = currentUser.getLus_id();
			pusatOrCabang = "cabang";
		}
		
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String tipeView = ServletRequestUtils.getStringParameter(request, "tipeView", "");
		Integer flagTglLahir=ServletRequestUtils.getIntParameter(request, "flagTglLahir",0);
		String tglLahir=ServletRequestUtils.getStringParameter(request, "tglLahir","");
			if(tipeView.equals("0")) {
				map.put("daftar", this.uwManager.selectViewerKontrolPinjaman(spaj, lusId));
			}else if(tipeView.equals("1")) {
				List otherSpaj = this.uwManager.selectAllSpaj(spaj);
				map.put("daftar", this.uwManager.selectViewerKontrolSimpanan(otherSpaj, lusId));
			}else if(tipeView.equals("2")) {
				List otherSpaj = this.uwManager.selectAllSpaj(spaj);
				map.put("daftar", this.uwManager.selectViewerKontrolBilling(otherSpaj, lusId));
			}else if(tipeView.equals("3")) {
				List otherSpaj = this.uwManager.selectAllSpaj(spaj);
				map.put("daftar", this.uwManager.selectViewerKontrolTahapan(otherSpaj, null));
			}
			else if(tipeView.equals("4")) map.put("daftar", this.uwManager.selectViewerKontrolClaimNilaiTunai(spaj, lusId));
			else if(tipeView.equals("5")) map.put("daftar", this.uwManager.selectViewerKontrolPowersave(spaj, lusId));
			else if(tipeView.equals("7")) map.put("daftar", this.uwManager.selectViewerKontrolAgent(spaj, lusId));
			else if(tipeView.equals("8")) map.put("daftar", this.uwManager.selectViewerKontrolReas(spaj, 1, lusId));
			else if(tipeView.equals("9")) {
				if(flagTglLahir==0){
					tglLahir=null;
				}else{
					//format tgl lahir dd/mm/yyyy => yyyymmdd
					tglLahir=tglLahir.replace("/", "");
					if(tglLahir.length()==8){
						String dd=tglLahir.substring(0,2);
						String mm=tglLahir.substring(2,4);
						String yy=tglLahir.substring(4,8);
						tglLahir=yy+mm+dd;
					} 
				}
				
				List filters = this.elionsManager.selectFilterSpaj(
							request.getParameter("pptt").toString(), request.getParameter("pilter2"), tglLahir, ServletRequestUtils.getStringParameter(request, "pilter", "="));
				map.put("daftar", this.uwManager.selectAkumulasiPolisBySpajList(filters));
			}		
			else if(tipeView.equals("10")) map.put("daftar", this.uwManager.selectViewerKontrolMaturity(spaj, lusId));
			else if(tipeView.equals("11")) map.put("daftar", this.uwManager.selectViewerKontrolPrivasi(spaj, lusId));
			else if(tipeView.equals("12")) map.put("daftar", this.uwManager.selectViewerKontrolReinstate(spaj, lusId));
			else if(tipeView.equals("13")) map.put("daftar", this.uwManager.selectViewerKontrolKomisi(spaj, lusId));
			else if(tipeView.equals("14")) map.put("daftar", this.uwManager.selectViewerKontrolRewards(spaj, lusId));
			else if(tipeView.equals("15")) 
				{
					List tempViewerKontrol = this.uwManager.selectGetYearViewerKontrolNAV(spaj, lusId);
					Map tempInfo = this.uwManager.selectGetInfoForRate(spaj);
					if(tempViewerKontrol.size() > 0)
					{
						String liUmur = tempInfo.get("LI_UMUR").toString();
						String liBisnis = tempInfo.get("LI_BISNIS").toString();
						String ldecUp = tempInfo.get("LDEC_UP").toString();
						String tempLdtbDate = tempInfo.get("LDT_BDATE").toString();	
						double multiply = 0.0;
						double multiplyNon = 0.0;
						int varAdd = 0;
						int varAddNon = 0;
						List viewerKontrolNAV = new ArrayList();
						List allValueViewerKontrolNAV = new ArrayList();
						String countIndexViewerKontrolNAV = this.uwManager.selectCountRowViewerKontrolNAV(spaj, lusId);
						if(LazyConverter.toInt(liUmur) < 17)
						{
							multiply = 2.0;
							varAdd = 60000;
							multiplyNon = 1.0;
							varAddNon = 30000;
						}
						else if(LazyConverter.toInt(liUmur) <= 50)
						{
							multiply = 2.0;
							varAdd = 150000;
							multiplyNon = 1.0;
							varAddNon = 75000;
						}
						else
						{
							multiply = 0.4;
							varAdd = 60000;
							multiplyNon = 0.2;
							varAddNon = 30000;
						}
						for(int i = 0 ; i < LazyConverter.toInt(countIndexViewerKontrolNAV) ; i++)
						{
							Map mapTempViewerKontrol = (HashMap)tempViewerKontrol.get(i);
							String thKe = mapTempViewerKontrol.get("TH_KE").toString();
							String ldecBonus = mapTempViewerKontrol.get("MNS_SALDO").toString();
							
							if(this.uwManager.selectGetLdecRate(liBisnis, thKe, liUmur)==null)
							{
								if( LazyConverter.toInt(thKe) >= 4 )
								{
									if(this.uwManager.selectGetLdecRateIfNoRow(liBisnis, thKe, liUmur)==null)
									{
										
									}
									else if(this.uwManager.selectGetLdecRateIfNoRow(liBisnis, thKe, liUmur)!=null)
									{
										thKe = "4";
									}
								}
							}
							else if(this.uwManager.selectGetLdecRate(liBisnis, thKe, liUmur)!=null)
							{
								
							}
							viewerKontrolNAV = this.uwManager.selectViewerKontrolNAV(spaj,liBisnis, thKe, liUmur, i + 1, ldecUp, multiply, varAdd, ldecBonus, multiplyNon, varAddNon, lusId);
							allValueViewerKontrolNAV.add(viewerKontrolNAV.get(0));
							}
							map.put("daftar",allValueViewerKontrolNAV);
						}
					else {
						map.put("daftar",null);
					}
				}
			else if(tipeView.equals("16")) {
				//map.put("daftar", this.uwManager.selectViewerKontrolMedis(spaj, 1, lusId));
				map.put("spaj", spaj);
			}
			else if(tipeView.equals("17")) map.put("daftar", this.uwManager.selectViewerKontrolBonus(spaj, lusId));
			else if(tipeView.equals("18")) { 
				map.put("daftar", this.uwManager.selectViewerKontrolStableLink(spaj, lusId));
			}
			else if(tipeView.equals("19")) {
				Boolean flagLifeOnly = ServletRequestUtils.getBooleanParameter(request, "flagLifeOnly",false);
				if(flagTglLahir==0){
					tglLahir=null;
				}else{
					//format tgl lahir dd/mm/yyyy => yyyymmdd
					tglLahir = tglLahir.substring(6,10)+tglLahir.substring(3,5)+tglLahir.substring(0,2);
				}

				List<Map> filters = this.uwManager.selectFilterSpajNew(request.getParameter("pptt").toString(), request.getParameter("pilter2"), tglLahir, ServletRequestUtils.getStringParameter(request, "pilter", "="), flagLifeOnly);
				map.put("daftar", this.uwManager.selectAkumulasiPolisBySpajListNew(filters,bulanKe,flagLifeOnly,tglSar,(tglLahir==null?null:tglLahir.substring(6)+"/"+tglLahir.substring(4,6)+"/"+tglLahir.substring(0,4)) ) );
				
				if(pusatOrCabang.equals("cabang")){
					//list Polis Kesehatan
					List polis_klamKesehatan = this.uwManager.selectPolisKlaimKesehatan(request.getParameter("pptt").toString(),ServletRequestUtils.getStringParameter(request, "pilter", "="),request.getParameter("pilter2"), tglLahir);
					map.put("klaim", polis_klamKesehatan);
					
					//Attention List
					List attention = this.uwManager.selectAttentionList(request.getParameter("pptt").toString(),ServletRequestUtils.getStringParameter(request, "pilter", "="),request.getParameter("pilter2"), tglLahir);
					map.put("attention", attention);
				}
			}
			else if(tipeView.equals("20")){ map.put("daftar", this.uwManager.selectViewerKontrolReferrer(spaj));}
			
			map.put("pusatOrCabang", pusatOrCabang);
		return new ModelAndView("uw/viewer/viewkontrolspaj_display", map);		
	}
	
	public ModelAndView viewendorse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		String no = ServletRequestUtils.getStringParameter(request, "noendors", "");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		List viewer = this.uwManager.selectViewerEndors(no);
		List allEndorse = this.uwManager.selectAllEndorsements(spaj);
		
		map.put("endorseInfo", this.elionsManager.selectInfoEndorse(
				ServletRequestUtils.getStringParameter(request, "noendors", (String) ((HashMap) allEndorse.get(0)).get("MSEN_ENDORS_NO"))).get(0));
		map.put("listendors", viewer);
		return new ModelAndView ("uw/viewer/viewerendors","cmd",map);
	}
	public ModelAndView endorse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		List allEndorse = this.uwManager.selectAllEndorsements(spaj);
		
		if(allEndorse.isEmpty()) {
			PrintWriter out = response.getWriter();
			out.println("<script>alert('Tidak ada History Endorsement');window.close();</script>");
			out.close();
			return null;
			
		}else {
			map.put("endorseList", allEndorse);
			map.put("endorseInfo", this.elionsManager.selectInfoEndorse(
					ServletRequestUtils.getStringParameter(request, "endorseno", (String) ((HashMap) allEndorse.get(0)).get("MSEN_ENDORS_NO"))).get(0));
			map.put("tipeendorse", ServletRequestUtils.getStringParameter(request, "tipeendorse", (String) ((HashMap) allEndorse.get(0)).get("TIPE")));
			map.put("endorseno", ServletRequestUtils.getStringParameter(request, "endorseno", (String) ((HashMap) allEndorse.get(0)).get("MSEN_ENDORS_NO")));
			return new ModelAndView("uw/viewer/viewendorse", "cmd", map);
		}
	}

	public ModelAndView payment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		//String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
        List k = new ArrayList();
        k.add("THP - Tahapan");
        k.add("NTS - Nilai Tunai");
        k.add("EBK - Bayar Klaim / Paid Claim");
        k.add("PIJ - Pinjaman NTS");
        k.add("AKH - Maturity");
        k.add("ASK - Askol");
        k.add("SRS - Pembayaran Selisih");
        k.add("SIMP - Simpanan");
        map.put("ktg", k);
        map.put("sysdate", this.elionsManager.selectSysdateSimple());
		return new ModelAndView("uw/viewer/viewpayment", "cmd", map);
	}

	public ModelAndView addressbilling(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		AddressBilling address = null;
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String dept = currentUser.getLde_id();
		
		//if("002, 004, 005, 006, 024, 025, 026, 027, 028, 051, 044, 534,542, 558, 574".indexOf(FormatString.rpad("0", currentUser.getLus_id(), 3))>-1) {
		if(currentUser.getLde_id().equals("11")){
			map.put("editAlamat", "true");
		}
		
		if(request.getParameter("save")!=null) {
			address = new AddressBilling();
			ServletRequestDataBinder binder = new ServletRequestDataBinder(address);
			binder.bind(request);
			BindingResult err = binder.getBindingResult();
			
			if(err.hasErrors()) {
				map.put("err", err.getAllErrors());
			}else {
				elionsManager.updateMst_address_billing(address, currentUser);
			}

		}else {
			address = this.elionsManager.selectAddressBilling(spaj);
		}

		//reference data
			//map.put("wilayah", this.elionsManager.selectWilayah());
			//map.put("region", this.elionsManager.selectRegions());
			map.put("gelar", this.elionsManager.selectGelar(0));

		//command object
			map.put("address", address);
		
		return new ModelAndView("uw/viewer/viewaddressbilling", "cmd", map);
	}

	public ModelAndView investasi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		
		if(currentUser.getLca_id().equals("01") ||currentUser.getLus_id().equals("1791") || currentUser.getLus_id().equals("2210")) {
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			map.put("rincian", this.elionsManager.selectRincianInvestasi(spaj));
			map.put("rincian2", this.elionsManager.selectRincianInvestasiNilaiPolis(spaj, 1));
			map.put("rincian3", this.elionsManager.selectRincianInvestasiRegister(spaj));
			map.put("jenisTrans", this.elionsManager.selectJenisTransaksi());
			
			DatasetProducer navRupiah = new DatasetProducer() {
			    public Object produceDataset(Map params) throws DatasetProduceException {
			    	List daftar = uwManager.selectChartNav("rupiah");
			    	DefaultCategoryDataset dataset = new DefaultCategoryDataset() ;
			    	for(int i=0; i<daftar.size(); i++) {
			    		Map m = (HashMap) daftar.get(i);
			    		double d = ((Double) m.get("LNU_NILAI")).doubleValue();
			    		dataset.addValue(d, (String) m.get("LJI_INVEST"), (String) m.get("LNU_TGL"));
			    	}
			    	return dataset;
		        }
				public String getProducerId() {
					return "navRupiah";
				}
				public boolean hasExpired(Map params, Date since) {
					return false;
				}
			};
	
			DatasetProducer navDollar= new DatasetProducer() {
			    public Object produceDataset(Map params) throws DatasetProduceException {
			    	List daftar = uwManager.selectChartNav("dollar");
			    	DefaultCategoryDataset dataset = new DefaultCategoryDataset() ;
			    	for(int i=0; i<daftar.size(); i++) {
			    		Map m = (HashMap) daftar.get(i);
			    		double d = ((Double) m.get("LNU_NILAI")).doubleValue();
			    		dataset.addValue(d, (String) m.get("LJI_INVEST"), (String) m.get("LNU_TGL"));
			    	}
			    	return dataset;
		        }
				public String getProducerId() {
					return "navDollar";
				}
				public boolean hasExpired(Map params, Date since) {
					return false;
				}
			};
	    	map.put("navRupiah", navRupiah);
			map.put("navDollar", navDollar);
			map.put("daftar", this.uwManager.selectChartNav(null));
			map.put("sysDate", this.elionsManager.selectSysdateSimple());
		}else {
			map.put("pesanError", "Anda tidak mempunyai akses untuk melihat data INVESTASI");
		}
		
		return new ModelAndView("uw/viewer/viewinvestasi", map);
		
	}

	public ModelAndView slinkbayar(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map params = new HashMap();

		String reg_spaj = ServletRequestUtils.getStringParameter(request, "spaj");
		int msl_no = ServletRequestUtils.getIntParameter(request, "msl_no");
		
		params.put("slinkBayar", uwManager.selectInfoSlinkBayar(reg_spaj, msl_no));
		
		return new ModelAndView("uw/viewer/slinkbayar", params);

	}

	
	public ModelAndView uwinfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map params = new HashMap();

		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		params.put("spaj", spaj);
		params.put("polis", elionsManager.selectPolicyNumberFromSpaj(spaj));
		
		params.put("daftar0", elionsManager.selectUwInfo("0", spaj));
		params.put("daftar1", elionsManager.selectUwInfo("1", spaj));
		params.put("daftar2", elionsManager.selectUwInfo("2", spaj));
		params.put("daftar3", elionsManager.selectUwInfo("3", spaj));
		params.put("daftar4", elionsManager.selectUwInfo("4", spaj));
		params.put("daftar5", elionsManager.selectUwInfo("5", spaj));
		params.put("daftar6", elionsManager.selectUwInfo("6", spaj));
		params.put("daftar7", elionsManager.selectUwInfo("7", spaj));
		params.put("daftar9", elionsManager.selectUwInfo("9", spaj));
		
		List info = uwManager.selectInfoPosisiTerakhir(spaj.trim());
				
		if(!info.isEmpty()) {
			params.put("info", info.get(0));
		}
		

		String result = null;
		Integer statusCode = null;
		Map responseResult = new HashMap();
		
		Cnote cnote = new Cnote();
		List<Detail> detailList = new ArrayList<Detail>();
		List<Cnote> cnoteList = new ArrayList<Cnote>();
		List<TrackingHistory> historyList = new ArrayList<TrackingHistory>();
		
		ViewerMultiController vmc = new ViewerMultiController();
		
		String polis = elionsManager.selectPolicyNumberFromSpaj(spaj);
		String awb = elionsManager.getUwDao().selectNoSenderSpaj(spaj);
		String url = props.getProperty("jne.path.url");
		
		if (url != null) {
			
			if (awb != null && awb.length() == 16){
				responseResult = vmc.getResponseHttpRequest2(url, awb); 
				statusCode = (Integer) responseResult.get("statusCode");
				
				if (statusCode != null && statusCode == 200){
					result = (String) responseResult.get("result");
					JSONObject json_pars = new JSONObject(result);
					
					if (json_pars.has("cnote") && json_pars.has("detail") && json_pars.has("history")) {
						
						JSONObject cnote_obj = json_pars.getJSONObject("cnote");
						JSONArray detail_arr = json_pars.getJSONArray("detail");
						JSONArray history_arr = json_pars.getJSONArray("history");
							
						cnote = vmc.cnoteConverter(cnote_obj);
						historyList = vmc.historyConverter(history_arr);
						detailList = vmc.detailConverter(detail_arr);
							
						cnoteList.add(cnote);
							
						params.put("cnoteList", cnoteList);
						params.put("historyList", historyList);
						params.put("detailList", detailList);
						
					} else {
						params.put("flag", "flag");
						params.put("statusServer", "Data Tidak ditemukan");
					}
					
				} else {
					params.put("flag", "flag");
					params.put("statusServer", "Data Tidak ditemukan, server status " + statusCode);
				}
				
			 } else if (polis != null) {
				 responseResult = vmc.getResponseHttpRequest2(url, polis); 
				 
				 statusCode = (Integer) responseResult.get("statusCode");
				 
				 if (statusCode != null && statusCode == 200){
					 	result = (String) responseResult.get("result");
						JSONObject json_pars = new JSONObject(result);
						
						if (json_pars.has("cnote") && json_pars.has("detail") && json_pars.has("history")) {
							
							JSONObject cnote_obj = json_pars.getJSONObject("cnote");
							JSONArray detail_arr = json_pars.getJSONArray("detail");
							JSONArray history_arr = json_pars.getJSONArray("history");
								
							cnote = vmc.cnoteConverter(cnote_obj);
							historyList = vmc.historyConverter(history_arr);
							detailList = vmc.detailConverter(detail_arr);
								
							cnoteList.add(cnote);
								
							params.put("cnoteList", cnoteList);
							params.put("historyList", historyList);
							params.put("detailList", detailList);
							
						} else {
							
							params.put("flag", "flag");
							params.put("statusServer", "Data Tidak ditemukan");
						}
						
					} else {
						params.put("flag", "flag");
						params.put("statusServer", "Data Tidak ditemukan, server status " + statusCode);
					} 
			 } else {
					params.put("flag", "flag");
					params.put("statusServer", "Data Tidak ditemukan");
			}
			
		} else {
			
			params.put("flag", "flag");
			params.put("statusServer", "Url salah atau tidak ditemukan ");
		}
		
		try {
			String statusCodeStr = null;
			String id = elionsManager.getUwDao().selectWsId();
			String endpoint = (String) responseResult.get("endpoint");
			
			if (statusCode != null){
				statusCodeStr = Integer.toString(statusCode);
			}
			
			elionsManager.getUwDao().insertLstHistActvWsOut(id, 4, 84, endpoint, endpoint, statusCodeStr, result, 4);
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		/* End Nana */
		
		
		return new ModelAndView("uw/viewer/uwinfo", params);

	}
	
	public Map getResponseHttpRequest2(String url, String polis) throws IOException {
//		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpClient httpclient = new DefaultHttpClient();


		String endpoint = url + polis;
		
		String result = null;
		Map map = new HashMap();

		try {
			HttpGet request = new HttpGet(endpoint);
//			CloseableHttpResponse response = httpClient.execute(request);
			HttpResponse response = httpclient.execute(request);
			
			// Get HttpResponse Status
			System.out.println(response.getProtocolVersion()); // HTTP/1.1
			System.out.println(response.getStatusLine().getStatusCode()); // 200
			System.out.println(response.getStatusLine().toString()); // HTTP/1.1 200 OK
			
			HttpEntity entity = response.getEntity();
				
			if (entity != null) {
				result = EntityUtils.toString(entity);
				System.out.println(result);
			}
			
			map.put("result", result);
			map.put("statusCode", response.getStatusLine().getStatusCode());
			map.put("endpoint", endpoint);
			
			
		} catch (Exception e){
			System.out.println(e.getMessage());
		} 
		
		return map;
		
	}
	
	public Cnote cnoteConverter(JSONObject cnote_obj) throws JSONException {
		Cnote cnote = new Cnote();
		cnote.setCnote_destination(cnote_obj.isNull("cnote_destination") ? null : cnote_obj.get("cnote_destination").toString());
		cnote.setCnote_receiver_name(cnote_obj.isNull("cnote_receiver_name") ? null : cnote_obj.get("cnote_receiver_name").toString());
		cnote.setPod_code(cnote_obj.isNull("pod_code") ? null : cnote_obj.get("pod_code").toString());
		cnote.setKeterangan(cnote_obj.isNull("keterangan") ? null : cnote_obj.get("keterangan").toString() );
		cnote.setSignature(cnote_obj.isNull("signature") ? null : cnote_obj.get("signature").toString());
		cnote.setCnote_weight(cnote_obj.isNull("cnote_weight") ? null : cnote_obj.get("cnote_weight").toString());
		cnote.setLongitude(cnote_obj.isNull("long") ? null : cnote_obj.get("long").toString());
		cnote.setCnote_amount(cnote_obj.isNull("cnote_amount") ? null : cnote_obj.get("cnote_amount").toString());
		cnote.setCity_name(cnote_obj.isNull("city_name") ? null : cnote_obj.get("city_name").toString()); 
		cnote.setCnote_no(cnote_obj.isNull("cnote_no") ?  null : cnote_obj.get("cnote_no").toString());
		cnote.setCust_type(cnote_obj.isNull("cust_type") ? null : cnote_obj.get("cust_type").toString());
		cnote.setCnote_date(cnote_obj.isNull("cnote_date") ? null : cnote_obj.get("cnote_date").toString());
		cnote.setLatitude(cnote_obj.isNull("lat") ? null : cnote_obj.get("lat").toString());
		cnote.setPriceperkg(cnote_obj.isNull("priceperkg") ? null : cnote_obj.get("priceperkg").toString());
		cnote.setCnote_pod_receiver(cnote_obj.isNull("cnote_pod_receiver") ? null : cnote_obj.get("cnote_pod_receiver").toString());
		cnote.setPhoto(cnote_obj.isNull("photo") ? null : cnote_obj.get("photo").toString());
		cnote.setCnote_origin(cnote_obj.isNull("cnote_origin") ? null : cnote_obj.get("cnote_origin").toString());
		cnote.setPod_status(cnote_obj.isNull("pod_status") ? null : cnote_obj.get("pod_status").toString());
		cnote.setReference_number(cnote_obj.isNull("reference_number") ? null : cnote_obj.get("reference_number").toString());
		cnote.setCnote_pod_date(cnote_obj.isNull("cnote_pod_date") ? null : cnote_obj.get("cnote_pod_date").toString());
		cnote.setServicetype(cnote_obj.isNull("servicetype") ? null : cnote_obj.get("servicetype").toString());
		cnote.setCnote_services_code(cnote_obj.isNull("cust_type") ? null :  cnote_obj.get("cust_type").toString());
		cnote.setFreight_charge(cnote_obj.isNull("freight_charge") ? null : cnote_obj.get("freight_charge").toString());
		cnote.setCnote_cust_no(cnote_obj.isNull("cnote_cust_no")  ? null : cnote_obj.get("cnote_cust_no").toString());
		cnote.setCnote_goods_descr(cnote_obj.isNull("cnote_goods_descr")  ? null : cnote_obj.get("cnote_goods_descr").toString());
		cnote.setShippingcost(cnote_obj.isNull("shippingcost") ? null : cnote_obj.get("shippingcost").toString());
		cnote.setInsuranceamount(cnote_obj.isNull("insuranceamount") ? null : cnote_obj.get("insuranceamount").toString());
		return cnote;
	}
	
	public List <TrackingHistory> historyConverter(JSONArray history_arr) throws JSONException {
		List <TrackingHistory> historyList = new ArrayList<TrackingHistory>();
		for (int i = 0; i < history_arr.length(); i++) {
		    JSONObject history_obj = history_arr.getJSONObject(i);
		    TrackingHistory history = new TrackingHistory();
		    history.setDate(history_obj.isNull("date") ? null : history_obj.getString("date"));
		    history.setDesc(history_obj.isNull("desc") ? null : history_obj.getString("desc"));
		    historyList.add(history);
		}
		return historyList;
	}
	
	public List <Detail> detailConverter(JSONArray detail_arr) throws JSONException {
		List <Detail> detailList = new ArrayList<Detail>();
		for (int i = 0; i < detail_arr.length(); i++) {
		    JSONObject detail_obj = detail_arr.getJSONObject(i);
		    Detail detail = new Detail();
		    detail.setCnote_no(detail_obj.isNull("cnote_no") ? null : detail_obj.getString("cnote_no"));
		    detail.setCnote_date(detail_obj.isNull("cnote_date") ? null : detail_obj.getString("cnote_date"));
		    detail.setCnote_weight(detail_obj.isNull("cnote_weight") ? null : detail_obj.getString("cnote_weight"));
		    detail.setCnote_origin(detail_obj.isNull("cnote_origin") ? null : detail_obj.getString("cnote_origin"));
		    detail.setCnote_receiver_name(detail_obj.isNull("cnote_receiver_name") ? null : detail_obj.getString("cnote_receiver_name"));
		    detail.setCnote_receiver_city(detail_obj.isNull("cnote_receiver_city")  ? null : detail_obj.getString("cnote_receiver_city"));
		    detail.setCnote_shipper_addr3(detail_obj.isNull("cnote_shipper_addr3") ?  null :  detail_obj.getString("cnote_shipper_addr3"));
		    detail.setCnote_shipper_addr2(detail_obj.isNull("cnote_shipper_addr2") ? null : detail_obj.getString("cnote_shipper_addr2"));
		    detail.setCnote_shipper_addr1(detail_obj.isNull("cnote_shipper_addr1") ? null : detail_obj.getString("cnote_shipper_addr1"));
		    detail.setCnote_receiver_addr3(detail_obj.isNull("cnote_receiver_addr3") ? null : detail_obj.getString("cnote_receiver_addr3"));
		    detail.setCnote_receiver_addr2(detail_obj.isNull("cnote_receiver_addr2") ? null : detail_obj.getString("cnote_receiver_addr2"));
		    detail.setCnote_receiver_addr1(detail_obj.isNull("cnote_receiver_addr1") ? null : detail_obj.getString("cnote_receiver_addr1"));
		    detail.setCnote_shipper_name(detail_obj.isNull("cnote_shipper_name") ? null : detail_obj.getString("cnote_shipper_name"));
		    detail.setCnote_shipper_city(detail_obj.isNull("cnote_shipper_city") ? null : detail_obj.getString("cnote_shipper_city"));
		    detailList.add(detail);
		}
		return detailList;
	}
	
	public ModelAndView uwinfopas(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Map params = new HashMap();

		String msp_id = ServletRequestUtils.getStringParameter(request, "msp_id", "");
		params.put("msp_id", msp_id);
		params.put("polis", "00000000000000");
		params.put("spaj", "00000000000");
		params.put("nokartu", uwManager.selectNoKartuFromId(msp_id));
		
		params.put("daftar0", uwManager.selectUwPasInfo("0", msp_id));
		params.put("daftar1", uwManager.selectUwPasInfo("1", msp_id));
		//params.put("daftar1", null);
		
		List info = uwManager.selectInfoPosisiPasTerakhir(msp_id.trim());
				
		if(!info.isEmpty()) {
			params.put("info", info.get(0));
		}
		
		return new ModelAndView("uw/viewer/uwinfopas", params);

	}
	
	/**Fungsi : Untuk Mengedit Tanggal Terima Spaj, dimana Akses nya hanya di berikan
	 * 			pada saat posisi polis masih 2(Underwriting) dan polis tidak dapat di transfer
	 * 			ke Payment atau Print Polis jika tangal terima spaj masih kosong 
	 * 			Untuk Mengedit Tanggal Kirim SPAJ 
	 * @param :	HttpServletRequest request, HttpServletResponse response
	 * @return:	ModelAndView
	 * */
	public ModelAndView editTglTrmKrmSpaj(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Date tanggal = elionsManager.selectSysdateSimple();
		
		String sysdate = defaultDateFormat.format(new Date());
		
		DateFormat dfh = new SimpleDateFormat("HH");
		DateFormat dfm = new SimpleDateFormat("mm");
		
		String hh = ServletRequestUtils.getStringParameter(request, "hh", dfh.format(tanggal));
		String mm = ServletRequestUtils.getStringParameter(request, "mm", dfm.format(tanggal));
		ArrayList<DropDown> listproses = new ArrayList<DropDown>();
		if(hh.length()>1){
			if(hh.substring(0,1).equals("0")){
				hh = hh.substring(1, 2);
			}
		}
		
		
		if(mm.length()>1){
			if(mm.substring(0,1).equals("0")){
				mm = mm.substring(1, 2);
			}
		}
		
		HashMap params = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		Integer show= ServletRequestUtils.getIntParameter(request, "show",0);
		String flag= ServletRequestUtils.getStringParameter(request, "flag","");
		HashMap mPosisi=Common.serializableMap(elionsManager.selectF_check_posisi(spaj));
		Integer lspdId=(Integer)mPosisi.get("LSPD_ID");
		String lspdPosition=(String)mPosisi.get("LSPD_POSITION");
		String keterangan;
		String s_noresi=new String();
		String s_proses;
		//
		Date tglTerimaSpaj;
		Date tglTerimaAdmin;
		Date tglKirimPolis;
		Date tglPrintPolis;
		Date tglSpajDoc;
		Date tglTerimaAdmedika;
		ArrayList lsError=new ArrayList();
		int jn_bank = elionsManager.selectIsInputanBank(spaj);
		//
		if(show==0){//tanggal terima spaj 
			if(lspdId!=2 && !currentUser.getLde_id().equals("11")){
				lsError.add("Posisi Polis ada di "+lspdPosition+" Tidak Bisa Edit Tanggal Terima SPAJ");
			}
		}else if(show==2 && lspdId!=2){//tanggal spaj doc
			lsError.add("Posisi Polis ada di "+lspdPosition+" Tidak Bisa Edit Tanggal SPAJ Doc");
		
		}else if(show==1){//tanggal kirim
			//cek posisi hanya 6 dan 9
			if(! (lspdId==6 || lspdId==9 ))
				lsError.add("Posisi Polis ada di "+lspdPosition+" Tidak Bisa Edit Tanggal Kirim SPAJ");
		}else if(show==3 && uwManager.selectGetMspoProvider(spaj)!=2){ //tanggal terima admedika
			lsError.add("Edit tanggal terima Admedika hanya bisa untuk case Provider");

		}else if(show==4 && lspdId != 1){//tanggal terima admin
			lsError.add("Posisi Polis ada di "+lspdPosition+" Tidak Bisa Edit Tanggal Terima oleh Admin");
		}
		
		HashMap mTertanggung=Common.serializableMap(elionsManager.selectTertanggung(spaj));
		tglTerimaSpaj=(Date)mTertanggung.get("MSTE_TGL_TERIMA_SPAJ");
		tglTerimaAdmin=(Date)mTertanggung.get("MSTE_TGL_TERIMA_ADMIN");
		tglPrintPolis=(Date)mTertanggung.get("MSPO_DATE_PRINT");		
		tglKirimPolis=(Date)mTertanggung.get("MSTE_TGL_KIRIM_POLIS");
		Date tglInput = (Date) mTertanggung.get("MSPO_INPUT_DATE");
		tglSpajDoc=(Date)mTertanggung.get("MSTE_TGL_SPAJ_DOC");
		tglTerimaAdmedika=(Date)mTertanggung.get("MSTE_TGL_TERIMA_ADMEDIKA");
		Integer lssaId=(Integer)mTertanggung.get("LSSA_ID");
		String lca_id = (String) mTertanggung.get("LCA_ID");
		String lkw_id = (String) mTertanggung.get("LWK_ID");
		String lsrg_id = (String) mTertanggung.get("LSRG_ID");
		//
		HashMap mPemegang=Common.serializableMap(elionsManager.selectPemegang(spaj));
		Integer lsspId=(Integer)mPemegang.get("LSSP_ID");
		
		if(flag.equals("")){//not save
			
			if(show==0){//edit tanggal terima spaj
				if(tglTerimaSpaj==null)
					params.put("tanggalTerima", sysdate);
				else
					params.put("tanggalTerima", FormatDate.toString(tglTerimaSpaj));
				
			}else if(show==1){//edit tanggal kirim spaj				
				listproses.add(new DropDown("00","------PILIH KURIR--------"));				
				listproses.add(new DropDown("01","JNE"));
				listproses.add(new DropDown("02","KURIR EKSTERNAL"));
				listproses.add(new DropDown("03","KURIR INTERNAL"));
				params.put("listproses", listproses);
				if(tglKirimPolis==null)
					params.put("tanggalKirim", sysdate);
				else
					params.put("tanggalKirim", FormatDate.toString(tglKirimPolis));
				
			}else if(show==2){//edit tanggal spaj doc
				if(tglSpajDoc==null)
					params.put("tglSpajDoc", sysdate);
				else
					params.put("tglSpajDoc", FormatDate.toString(tglSpajDoc));
				
			}else if(show==3){ //edit tgl terima admedika
				if(tglTerimaAdmedika==null)
					params.put("tglTerimaAdmedika", sysdate);
				else
					params.put("tglTerimaAdmedika", FormatDate.toString(tglTerimaAdmedika));
				
			}else if(show==4){//edit tanggal terima oleh admin
				if(tglTerimaAdmin==null)
					params.put("tanggalTerimaAdmin", sysdate);
				else
					params.put("tanggalTerimaAdmin", FormatDate.toString(tglTerimaAdmin));
			}
			keterangan=elionsManager.selectMstPositionSpajMspsDesc(spaj, show);
			
		}else{//save
			String sTglTerimaSpaj=ServletRequestUtils.getStringParameter(request, "tanggalTerima","00/00/0000");
			String sTglTerimaAdmin=ServletRequestUtils.getStringParameter(request, "tanggalTerimaAdmin","00/00/0000");
			String sTglKirimPolis=ServletRequestUtils.getStringParameter(request, "tanggalKirim","00/00/0000");
			String sTglSpajDoc=ServletRequestUtils.getStringParameter(request, "tglSpajDoc","00/00/0000");
			String stglTerimaAdmedika=ServletRequestUtils.getStringParameter(request, "tglTerimaAdmedika","00/00/0000");
			keterangan=ServletRequestUtils.getStringParameter(request, "keterangan",null);
			
			if(show==0){//edit tanggal terima spaj
				if(sTglTerimaSpaj.equals("00/00/0000")){
					lsError.add("Silahkan Masukkan tanggal Terima Spaj dengan Benar");
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(sTglTerimaSpaj + " " + hh + ":" + mm);
						
						if(tglInput!=null){
							if(tmp.before(tglInput)){
								lsError.add("Tanggal Terima SPAJ harus lebih besar dari tanggal input spaj!");
							}else{
								elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,s_noresi);
								params.put("success", "Berhasil Update Tanggal Terima Spaj");
							}
						}else{
							elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,s_noresi);
							params.put("success", "Berhasil Update Tanggal Terima Spaj");
						}
					}	
				}
				params.put("tanggalTerima", sTglTerimaSpaj);
				
			}else if (show==1){//edit tanggal kirim spaj
				listproses.add(new DropDown("00","------PILIH KURIR--------"));				
				listproses.add(new DropDown("01","JNE"));
				listproses.add(new DropDown("02","KURIR EKSTERNAL"));
				listproses.add(new DropDown("03","KURIR INTERNAL"));
				params.put("listproses", listproses);
				if(sTglKirimPolis.equals("00/00/0000")){
					lsError.add("Silahkan Masukan Tanggal Kirim Spaj dengan Benar");
				}else{
					if(lsError.isEmpty()){	
						Date tmp = completeDateFormat.parse(sTglKirimPolis+ " " + hh + ":" + mm);
						//Tambahan MAnta
						//String dist = ServletRequestUtils.getStringParameter(request,"dist",null);
						String adm = ServletRequestUtils.getStringParameter(request,"adm",null);
						String sms = ServletRequestUtils.getStringParameter(request,"sms",null);
						//String s_pas = ServletRequestUtils.getStringParameter(request,"pas",null);
						s_noresi = ServletRequestUtils.getStringParameter(request,"resi",null);
						s_proses= ServletRequestUtils.getStringParameter(request,"proses",null);

						if( adm == null || sms == null ) {
							lsError.add("Perantara, Kartu Admedika,SimasCard dan KartuPas harus dipilih!");
						}else if(s_noresi==null && !s_proses.equals("01")){
							lsError.add("No.Resi Pengiriman Harus Diisi!");
						}else if(s_proses.equals("00")){
							lsError.add("Pilih Tipe Kurir!");
						}else{
							String plus = "";
							if(sms.equals("0") && adm.equals("0")){ 
								plus = " , ADA SIMASCARD DAN ADA KARTU ADMEDIKA";
							}else if(sms.equals("0") && adm.equals("1")){
								plus = " , ADA SIMASCARD DAN TIDAK ADA KARTU ADMEDIKA";							
							}else if(sms.equals("1") && adm.equals("0")){
								plus = " , TIDAK ADA SIMASCARD DAN ADA KARTU ADMEDIKA";
							} else if(sms.equals("1") && adm.equals("1")){
								plus = " , TIDAK ADA SIMASCARD DAN TIDAK ADA KARTU ADMEDIKA";
							}else if(sms.equals("1") && adm.equals("0")){
								plus = " , TIDAK ADA SIMASCARD DAN ADA KARTU ADMEDIKA";
							}else if(sms.equals("1") && adm.equals("1")){
								plus = " , TIDAK ADA SIMASCARD DAN TIDAK ADA KARTU ADMEDIKA";
							}else if(sms.equals("0") && adm.equals("0")){
								plus = " , ADA SIMASCARD DAN ADA KARTU ADMEDIKA";
							}
							if(!s_noresi.equals("") || s_noresi!=null){
								plus= plus + " , NO :"+" "+ s_noresi;
							}
//							if(dist == "0"){
//								keterangan = "GA";
//							}else{
							if(s_proses.equalsIgnoreCase("01")){
								keterangan = "JNE";
							}else if(s_proses.equalsIgnoreCase("02")){
								keterangan = "KURIR EKSTERNAL";
							}else{
								keterangan = "KURIR INTERNAL";
							}
//							}
							keterangan ="POLIS TELAH DIBERIKAN KEPADA " + keterangan + plus;
							
							if(tglPrintPolis!=null){
								if(tmp.before(tglPrintPolis)){
									lsError.add("Tanggal Kirim Polis harus lebih besar dari tanggal print polis!");
								} else{
									elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,s_noresi);
									HashMap policy =Common.serializableMap((Map)elionsManager.selectPemegang(spaj));
									String noPolis=(String)policy.get("MSPO_POLICY_NO");
									String nama=(String)policy.get("MCL_FIRST");
									String subject="Print Polis";
									String emailCabang=uwManager.selectEmailCabangFromSpaj(spaj);
									if(emailCabang==null)
										emailCabang=request.getParameter("email");
									
									if(emailCabang!=null){
										params.put("email",1);
										String to[]={emailCabang};
										String cc[]={currentUser.getEmail()};
//										String to[]={"antasari@sinarmasmsiglife.co.id"};
//										String cc[]={"antasari@sinarmasmsiglife.co.id"};
										String message="Polis No. "+FormatString.nomorPolis(noPolis)+"\nAtas Nama "+nama+" telah selesai dicetak oleh Underwriting dan dalam proses pengiriman.";
										if(!lca_id.equals("09")&& !lca_id.equals("40")){ 
											EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
													null, 0, 0, tanggal, null, 
													false, props.getProperty("admin.ajsjava"), to, cc, null, subject,message, null, spaj);
										}else if(lca_id.equals("40")){
											
											//Pembagian pengiriman email berdasarkan site DMTM (Req by Rumanthi) *updated by Ridhaal.
											
											HashMap mapEmail = new HashMap();
											
											String[] toDMTM = null, ccDMTM = null, bccDMTM = null  ;
											
											if((lkw_id.equals("01") && lsrg_id.equals("00")) || (lkw_id.equals("01") && lsrg_id.equals("02")) || (lkw_id.equals("02") && lsrg_id.equals("02")) || (lkw_id.equals("02") && lsrg_id.equals("04")) || (lkw_id.equals("02") && lsrg_id.equals("07"))  ){
											
												if(lkw_id.equals("01") && lsrg_id.equals("00")  ){
													mapEmail = bacManager.selectMstConfig(6, "printPolisDMTM", "DMTM_DMTM");
													
												}else if(lkw_id.equals("01") && lsrg_id.equals("02")){
													mapEmail = bacManager.selectMstConfig(6, "printPolisDMTM", "DMTM_BSIM");
													
												}else if(lkw_id.equals("02") && lsrg_id.equals("02")){
													mapEmail = bacManager.selectMstConfig(6, "printPolisDMTM", "DMTM_SMP");
													
												}else if(lkw_id.equals("02") && lsrg_id.equals("04")){
													mapEmail = bacManager.selectMstConfig(6, "printPolisDMTM", "DMTM_RedBerry");
													
												}else if(lkw_id.equals("02") && lsrg_id.equals("07")){
													mapEmail = bacManager.selectMstConfig(6, "printPolisDMTM", "DMTM_DENA");
													
												}
												
												toDMTM = mapEmail.get("NAME")!=null?mapEmail.get("NAME").toString().split(";"):null;
												ccDMTM = mapEmail.get("NAME2")!=null?mapEmail.get("NAME2").toString().split(";"):null;
												bccDMTM = mapEmail.get("NAME3")!=null?mapEmail.get("NAME3").toString().split(";"):null;
												
											}else{
												toDMTM = new String[] {"rumanthi@sinarmasmsiglife.co.id;astri@sinarmasmsiglife.co.id"};
												ccDMTM = new String[] {"qadmtm@sinarmasmsiglife.co.id"};
											}
											
										//	String to1[]=new String[]{"rumanthi@sinarmasmsiglife.co.id;astri@sinarmasmsiglife.co.id"};
//											String to1[]={"antasari@sinarmasmsiglife.co.id"};
											EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
													null, 0, 0, tanggal, null, 
													false, props.getProperty("admin.ajsjava"), toDMTM, ccDMTM, bccDMTM, subject,message, null, spaj);
										}
									}else
										params.put("email",0);
		
									params.put("success", "Berhasil Update Tanggal Kirim Spaj");
								}
							}else{

								elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,s_noresi);
								HashMap policy =Common.serializableMap((Map)elionsManager.selectPemegang(spaj));
								String noPolis=(String)policy.get("MSPO_POLICY_NO");
								String nama=(String)policy.get("MCL_FIRST");
								String subject="Print Polis";
								String emailCabang=uwManager.selectEmailCabangFromSpaj(spaj);
								if(emailCabang==null)
									emailCabang=request.getParameter("email");
								
								if(emailCabang!=null){
									params.put("email",1);
									String to[]={emailCabang};
									String cc[]={currentUser.getEmail()};
//									String to[]={"antasari@sinarmasmsiglife.co.id"};
//									String cc[]={"antasari@sinarmasmsiglife.co.id"};
									String message="Polis No. "+FormatString.nomorPolis(noPolis)+"\nAtas Nama "+nama+" telah selesai dicetak oleh Underwriting dan dalam proses pengiriman.";
									if(!lca_id.equals("09"))EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
											null, 0, 0, tanggal, null, 
											false, props.getProperty("admin.ajsjava"), to, cc, null, subject,message, null, spaj);
								}else
								params.put("email",0);
								params.put("listproses", listproses);
								params.put("success", "Berhasil Update Tanggal Kirim Spaj");
							}
							
						}
					}
				}
				params.put("tanggalKirim", sTglKirimPolis);
				
			}else if(show==2){//edit tanggal spaj doc
				if(keterangan==null)
					lsError.add("Silahkan Isi Kolom Keterangan yang telah disediakan");
				if(sTglSpajDoc.equals("00/00/0000")){
					lsError.add("Silahkan Masukan Tanggal Spaj dengan Benar");
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(sTglSpajDoc+ " " + hh + ":" + mm);
						if(tglTerimaSpaj!=null){
							if(tmp.before(tglTerimaSpaj)){
								lsError.add("Tanggal terima Spaj Doc harus lebih besar dari tanggal Terima Spaj!");
							}else{
								elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,s_noresi);
								params.put("success", "Berhasil Update Tanggal Spaj Doc");
							}
						}else{
							elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,s_noresi);
							params.put("success", "Berhasil Update Tanggal Spaj Doc");
						}
					}
				}
				params.put("tglSpajDoc", sTglSpajDoc);
				
			}else if(show==3){
				if(keterangan==null){
					lsError.add("Silahkan Isi Kolom Keterangan yang telah disediakan");
				}
				if(stglTerimaAdmedika.equals("00/00/0000")){
					lsError.add("Silahkan Masukan Tanggal Spaj dengan Benar");
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(stglTerimaAdmedika+ " " + hh + ":" + mm);
						if(tglTerimaAdmedika!=null){
							elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,s_noresi);
							params.put("success", "Berhasil Update Tanggal Terima Admedika");
						}else{
							elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,s_noresi);
							params.put("success", "Berhasil Update Tanggal Terima Admedika");
						}
					}
				}
				
			}else if(show==4){
				if(keterangan==null){
					lsError.add("Silahkan Isi Kolom Keterangan yang telah disediakan");
				}
				if(sTglTerimaAdmin.equals("00/00/0000")){
					lsError.add("Silahkan Masukan Tanggal Terima Admin dengan Benar");
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(sTglTerimaAdmin+ " " + hh + ":" + mm);
						if(tglTerimaAdmin!=null){
							elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,s_noresi);
							params.put("success", "Berhasil Update Tanggal Terima Admin");
						}else{
							elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,s_noresi);
							params.put("success", "Berhasil Update Tanggal Terima Admin");
						}
					}
				}
				
			}else if(show==8){/*
				if(keterangan==null){
					lsError.add("Silahkan Isi Kolom Keterangan yang telah disediakan");
				}
				if(stglKirimSkDebet.equals("00/00/0000")){
					lsError.add("Silahkan Masukan Tanggal Terima Admin dengan Benar");
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(stglKirimSkDebet+ " " + hh + ":" + mm);
						if(stglKirimSkDebet!=null){
							elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
							params.put("success", "Berhasil Update Tanggal Kirim SK Debet");
						}else{
							elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
							params.put("success", "Berhasil Update Tanggal Kirim SK Debet");
						}
					}
				}
				
			*/}
		}
		params.put("keterangan", keterangan);
		params.put("show", show);
		params.put("lsError", lsError);
		params.put("listHH", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23});
		params.put("listMM", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59});
		params.put("hh", hh);
		params.put("mm", mm);
		return new ModelAndView("uw/viewer/editTglTrmKrmSpaj", params);
	}
	
	public ModelAndView simasCard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String dept = currentUser.getLde_id();
		String spaj = request.getParameter("spaj");
		Map params = new HashMap();
		Simcard simascard = new Simcard();
		List lsError = new ArrayList();
		List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj);
		List detBisnis = elionsManager.selectDetailBisnis(spaj);
		String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
		
		if(lsbs.equals("187") || products.bethany(Integer.parseInt(lsbs), Integer.parseInt(lsdbs)) || products.muamalat(spaj)){
			lsError.add("Khusus Produk PAS, Bethany, dan Muamalat tidak perlu menginput Simas Card");
		}else if(!isAgen.isEmpty() && !FormatString.rpad("0",(spaj.substring(0,2)),2).equalsIgnoreCase("09")){
			lsError.add("Pemegang Polis adalah agent, tidak perlu Proses Input Simas Card");
		}
		
		if(request.getParameter("save") != null) {
			List daftarSebelumnya = uwManager.selectSimasCard(spaj); //daftar simas card sebelumnya yang masih aktif
			//daftarSebelumnya.removeAll(daftarSebelumnya);
			if(daftarSebelumnya.isEmpty()) {
				String mrc_no_kartu = ServletRequestUtils.getStringParameter(request, "mrc_no_kartu", "");
				if(!StringUtil.isEmpty(mrc_no_kartu)){
					simascard = this.uwManager.selectSimasCardByNoKartu(mrc_no_kartu);
					if(simascard==null){
						simascard = this.uwManager.selectSimasCardBySpaj(spaj);
					}
					if(simascard!=null){
						lsError.add("No Kartu sudah pernah diinput pada no SPAJ : "+ simascard.getReg_spaj());
					}
					if(lsError.isEmpty()){
						Boolean prosesSimasCard = uwManager.prosesInsertSimasCardNew(spaj, mrc_no_kartu, currentUser,0);
						if(prosesSimasCard==true){
							params.put("success", "Simas Card berhasil Diinput");
							params.put("mrc_no_kartu", mrc_no_kartu);
							params.put("buttonDisabled", true);
						}else{
							lsError.add("Proses Penginputan tidak berhasil, silakan hubungi EDP");
						}
					}
				}else{
					lsError.add("Harap masukkan No Kartu terlebih dahulu");
				}
			}else{
				Map SimasCardSebelumnya = (Map) daftarSebelumnya.get(0);
				String reg_spaj_lama = (String) SimasCardSebelumnya.get("REG_SPAJ");
				String mspo_policy_no_lama = elionsManager.selectPolicyNumberFromSpaj(reg_spaj_lama);
				String msps_desc = elionsManager.selectMstPositionSpajMspsDesc(spaj, 5);
				if(Common.isEmpty(msps_desc) && lsError.isEmpty()){
					elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO POLIS "+ FormatString.nomorPolis(mspo_policy_no_lama), spaj, 0);
				}
				lsError.add("Polis ini sudah pernah mendapatkan Simas Card pada no polis "+ FormatString.nomorPolis(mspo_policy_no_lama)+". Silakan langsung proses Print Polis.");
			}
			params.put("lsError", lsError);
		}else{
			simascard = this.uwManager.selectSimasCardBySpaj(spaj);
			if(simascard!=null){
				//simascard.setNo_kartu_split(FormatString.splitWordToCharacter(simascard.getNo_kartu(),16));
				simascard.setMrc_no_kartu(simascard.getNo_kartu());
				params.put("mrc_no_kartu", simascard.getNo_kartu());
				params.put("no_kartu", simascard.getNo_kartu());
				params.put("buttonDisabled", true);
			}
		}
		return new ModelAndView("uw/simas_card/simascard", params);
	}
	
	public ModelAndView simasCardUlang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		Date now = this.elionsManager.selectSysdateSimple();
		
		//user menekan tombol print ulang
		if(request.getParameter("save")!=null) {
			String ket = ServletRequestUtils.getStringParameter(request, "ket", "");
			String spaj = ServletRequestUtils.getStringParameter(request, "reg_spaj","");
			String mrc_no_kartu = ServletRequestUtils.getStringParameter(request, "mrc_no_kartu", "");
			
			//validasi2
			if(ket.trim().equals("")) {
				map.put("err", "Harap isi keterangan");
			}else if(Common.isEmpty(spaj)){
				map.put("err", "Harap isi No Polis/ SPAJ");
			}else if(StringUtil.isEmpty(mrc_no_kartu)){
				map.put("err", "Harap memilih No kartu yang akan diproses");
			}
			String mspo_policy_no = uwManager.selectGetMspoPolicyNo(spaj);
			spaj = elionsManager.selectGetSpaj(spaj);
			if(Common.isEmpty(spaj)){
				map.put("err", "No Polis/SPAJ yang dimasukkan tidak ada");
			}
			Map simas_card_old = uwManager.selectSimasCardExist(spaj, "05");
			//cara pengecekan apakah kartu baru ato kartu lama(format no polis) dicompare antara mspo_policy_no_format dgn no_kartu lama.
			Simcard simcard = uwManager.selectSimasCardBySpaj(spaj);
			if(simcard!=null){
				String no_kartu_lama = simcard.getNo_kartu();
				Date tgl_expired_lama = simcard.getTgl_akhir();
				if(FormatString.nomorPolis(mspo_policy_no.trim()).equals(no_kartu_lama.trim())){//perubahan kartu lama jadi kartu baru
//					if(now.before(tgl_expired_lama)){
//						map.put("err", "Kartu Lama masih berlaku hingga periode "+ FormatDate.toIndonesian(tgl_expired_lama));
//					}
				}else{//perubahan atau penggantian kartu jenis baru menjadi no lain
					if(Common.isEmpty(simas_card_old)){
						map.put("err", "Polis ini tidak dapat melakukan penggantian simas card karena masih menggunakan Simas Card Lama");
					}
				}
			}
			
			int flag_print = elionsManager.selectPrintCabangAtauPusat(spaj);
			if(flag_print==1){
				if(!user.getLde_id().equals("11")){
					int count= elionsManager.selectCekPrintUlang(spaj, "VALID PENGINPUTAN ULANG SIMAS CARD");
					if(count<1){
						map.put("err", "Proses Penginputan ulang Pada SPAJ "+spaj+" tidak dapat dilakukan karena belum divalid oleh pihak U/W. Silakan menghubungi pihak U/W untuk info lebih lanjut ");
					}
					if(count>=1){
						Date tgl_valid_terakhir = uwManager.selectMaxDatePositionSpaj(spaj, "VALID PENGINPUTAN ULANG SIMAS CARD");
						if(uwManager.selectWorkDays(tgl_valid_terakhir, now)>3){
							map.put("err", "Valid Sebelumnya sudah berakhir(> 3 hari).Silakan Request Valid kembali dari pihak U/W");
						}
					}
				}
			}
			
			if(map.isEmpty()){
				String mrc_no_kartu_lama = simcard.getNo_kartu();
				this.uwManager.prosesSavePenggantianSimasCard(spaj, mrc_no_kartu, ket.toUpperCase(), mrc_no_kartu_lama,user);
				map.put("success", "Simas Card berhasil Diinput");
			}
//			else {
//				//proses print ulang
//				List detBisnis = elionsManager.selectDetailBisnis(spaj);
//				String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
//				String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
//				this.uwManager.insertLst_ulangan(
//						spaj, now, jenis_ulangan, ServletRequestUtils.getIntParameter(request, "_status"), user.getLus_id(), ket + (!seq.equals("")?(" (" + seq + ")"):""));
				
				String ketek = "";
				ketek = " (" + ketek + ": " + ket + ")";
				
//				this.elionsManager.insertMstPositionSpaj(spaj, ketek.toUpperCase(), user.getLus_id());
//			}
			
			map.put("ket", ket);
			map.put("spaj", spaj);
		}
		map.put("tgl", now);
		return new ModelAndView("uw/simas_card/simascard_ulang", map);
	}
	
	public ModelAndView TglTerimaAdmedika(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Date tanggal = elionsManager.selectSysdateSimple();
		
		String sysdate = defaultDateFormat.format(new Date());
		
		DateFormat dfh = new SimpleDateFormat("HH");
		DateFormat dfm = new SimpleDateFormat("mm");
		
		String hh = ServletRequestUtils.getStringParameter(request, "hh", dfh.format(tanggal));
		String mm = ServletRequestUtils.getStringParameter(request, "mm", dfm.format(tanggal));
		
		Map params = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		
		
		return new ModelAndView("uw/viewer/tglTerimaAdmedika", params);
	}
	public ModelAndView editTglTrmTtp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Date tanggal = elionsManager.selectSysdateSimple();
		
		String sysdate = defaultDateFormat.format(new Date());
		
		DateFormat dfh = new SimpleDateFormat("HH");
		DateFormat dfm = new SimpleDateFormat("mm");
		
		String hh = ServletRequestUtils.getStringParameter(request, "hh", dfh.format(tanggal));
		String mm = ServletRequestUtils.getStringParameter(request, "mm", dfm.format(tanggal));
		
		Map params = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		Integer show= ServletRequestUtils.getIntParameter(request, "show",0);
		String flag= ServletRequestUtils.getStringParameter(request, "flag","");
		Map mPosisi=elionsManager.selectF_check_posisi(spaj);
		Integer lspdId=(Integer)mPosisi.get("LSPD_ID");
		String lspdPosition=(String)mPosisi.get("LSPD_POSITION");
		String keterangan;
		//
		Date tglTerimaAdmin;
		Date tglTtp;
		Date tglKirimPolis;
		Date tglPrintPolis;
		Date tglSpajDoc;
		List lsError=new ArrayList();
		//

		if(! (lspdId==7))
			lsError.add("Posisi Polis ada di "+lspdPosition+" Tidak Bisa Edit Tanggal Terima SPAJ");
		
		Map mTertanggung=elionsManager.selectTertanggung(spaj);
		tglTerimaAdmin=(Date)mTertanggung.get("MSTE_TGL_TERIMA_ADMIN");
		tglPrintPolis=(Date)mTertanggung.get("MSPO_DATE_PRINT");		
		tglKirimPolis=(Date)mTertanggung.get("MSTE_TGL_KIRIM_POLIS");
		Date tglInput = (Date) mTertanggung.get("MSPO_INPUT_DATE");
		tglSpajDoc=(Date)mTertanggung.get("MSTE_TGL_SPAJ_DOC");
		tglTtp=(Date)mTertanggung.get("MSPO_TERIMA_TTP");
		Integer lssaId=(Integer)mTertanggung.get("LSSA_ID");
		//
		Map mPemegang=elionsManager.selectPemegang(spaj);
		Integer lsspId=(Integer)mPemegang.get("LSSP_ID");
		
		if(flag.equals("")){//not save
			if(show==0){//edit tanggal terima spaj
				if(tglTerimaAdmin==null)
					params.put("tanggalTerima", sysdate);
				else
					params.put("tanggalTerima", FormatDate.toString(tglTerimaAdmin));
			}else if(show==1){//edit tanggal kirim spaj
				if(tglKirimPolis==null)
					params.put("tanggalKirim", sysdate);
				else
					params.put("tanggalKirim", FormatDate.toString(tglKirimPolis));
			}else if(show==2){//edit tanggal spaj doc
				if(tglSpajDoc==null)
					params.put("tglSpajDoc", sysdate);
				else
					params.put("tglSpajDoc", FormatDate.toString(tglSpajDoc));
			}else if(show==4){//edit tanggal spaj doc
				if(tglSpajDoc==null)
					params.put("tglTtp", sysdate);
				else
					params.put("tglTtp", sysdate); // No HD #40575 req MUHAMAD RIYADI
//					params.put("tglTtp", FormatDate.toString(tglSpajDoc));
			}
			keterangan=elionsManager.selectMstPositionSpajMspsDesc(spaj, show);
			
		}else{//save
			String sTglTerimaAdmin=ServletRequestUtils.getStringParameter(request, "tanggalTerimaAdmin","00/00/0000");
			String sTglTtp=ServletRequestUtils.getStringParameter(request, "tglTtp","00/00/0000");
			String sTglKirimPolis=ServletRequestUtils.getStringParameter(request, "tanggalKirim","00/00/0000");
			String sTglSpajDoc=ServletRequestUtils.getStringParameter(request, "tglSpajDoc","00/00/0000");
			keterangan=ServletRequestUtils.getStringParameter(request, "keterangan",null);
			if(show==4){//edit tanggal terima spaj
				if(sTglTtp.equals("00/00/0000")){
					lsError.add("Silahkan Masukan tanggal Input TTP dengan Benar");
					if(tglTtp==null)
						params.put("tglTtp", sysdate);
					else
						params.put("tglTtp", FormatDate.toString(tglTtp));
				
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(sTglTtp + " " + hh + ":" + mm);
						
						elionsManager.prosesEditTanggalTTP(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
						
						//referensi(tambang emas) -> update flag = 1
						uwManager.updateFlagDTRX(spaj);
						//end
						
						params.put("success", "Berhasil Input Tanggal Terima TTP");
						
					}	
				}
					params.put("tanggalTerima", sTglTtp);
			}
		}
		params.put("keterangan", keterangan);
		params.put("show", show);
		params.put("lsError", lsError);
		params.put("listHH", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23});
		params.put("listMM", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59});
		params.put("hh", hh);
		params.put("mm", mm);
		
		return new ModelAndView("uw/viewer/editTglTrmTtp", params);
	}
	public ModelAndView cek_agen(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map params = new HashMap();
		String msag_id=ServletRequestUtils.getStringParameter(request, "msag_id","");
		Integer flag=0;
		
		params.put("msag_id",msag_id);
		if ((msag_id!=null)&&(msag_id !="")){
			Map agen =(Map)elionsManager.selectbacCekAgen(msag_id);
			String kode_agen=(String)agen.get("KODE_AGEN");
			String nama_agen=(String)agen.get("NAMA_AGEN");
			String email_agen=(String)agen.get("EMAIL_AGEN");
			String region=(String)agen.get("REGION");
			String bank=(String)agen.get("BANK");
			String bank_cabang=(String)agen.get("BANK_CABANG");
			String bank_rekening=(String)agen.get("BANK_REKENING");
			String sertifikat=(String)agen.get("SERTIFIKAT");
			String sertifikat_no=(String)agen.get("SERTIFIKAT_NO");
			String npwp_agen=(String)agen.get("NPWP_AGEN");
			
			Date beg_date_kontrak=(Date)agen.get("BEG_DATE_KONTRAK");
			Date end_date_kontrak=(Date)agen.get("END_DATE_KONTRAK");
			Date tgl_aktif=(Date)agen.get("TGL_AKTIF");
			Date sertifikat_aktif=(Date)agen.get("SERTIFIKAT_AKTIF");
			
			flag=1;
			params.put("flag",flag);
			params.put("kode_agen",kode_agen);
			params.put("nama_agen",nama_agen);
			params.put("email_agen",email_agen);
			params.put("region",region);
			params.put("bank",bank);
			params.put("bank_cabang",bank_cabang);
			params.put("bank_rekening",bank_rekening);
			params.put("sertifikat",sertifikat);
			params.put("sertifikat_no",sertifikat_no);
			params.put("npwp_agen",npwp_agen);
			params.put("tgl_aktif",tgl_aktif);
			params.put("sertifikat_aktif",sertifikat_aktif);
			params.put("end_date_kontrak",end_date_kontrak);
			params.put("beg_date_kontrak",beg_date_kontrak);
		}
		
		return new ModelAndView("uw/viewer/cek_agen", params);
	}
	
	public ModelAndView editKetTTp(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Date tanggal = elionsManager.selectSysdateSimple();
		
		String sysdate = defaultDateFormat.format(new Date());
		
		DateFormat dfh = new SimpleDateFormat("HH");
		DateFormat dfm = new SimpleDateFormat("mm");
		boolean emailCabangValid = false;
		boolean emailAgenValid = false;
		
		String hh = ServletRequestUtils.getStringParameter(request, "hh", dfh.format(tanggal));
		String mm = ServletRequestUtils.getStringParameter(request, "mm", dfm.format(tanggal));
		String keterangan=ServletRequestUtils.getStringParameter(request, "keterangan","");
		
		Map params = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		Integer show= ServletRequestUtils.getIntParameter(request, "show",0);
		String flag= ServletRequestUtils.getStringParameter(request, "flag","");
		String emailCabang = uwManager.selectEmailCabangFromSpaj(spaj);
		
		Map infoAgen = elionsManager.selectEmailAgen(spaj);
		String emailAgen = (String) infoAgen.get("MSPE_EMAIL");
		String alamat = "";
		String cc = "";
		String bcc="";
		//validasi email agen
		if(emailAgen!=null) {
			if(!emailAgen.trim().equals("")) {
				if(emailAgen.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
					emailAgenValid = true;
					cc =emailAgen;
					
				}
			}
		}
		//validasi email cabang
		if(emailCabang!=null) {
			if(!emailCabang.trim().equals("")) {
				if(emailCabang.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
					emailCabangValid = true;
					alamat= emailCabang;
					
				}
			}
		}
		Map infopemegang =elionsManager.selectInfoPemegang(spaj);
		String pemegang=(String)infopemegang.get("MCL_FIRST");
		String product= (String)infopemegang.get("LSBS_NAME");
		String no_polis= (String)infopemegang.get("MSPO_POLICY_NO");
		String tgl_kirim= (String)infopemegang.get("MSTE_TGL_KIRIM_POLIS");
		String lca_id= (String)infopemegang.get("LCA_ID");
		
		bcc=currentUser.getEmail();
		if (flag.equals("1")){
			elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "TTP : " + keterangan, spaj, 0);
			email.send(true, 
					props.getProperty("admin.ajsjava"), 
					alamat.split(";"), 
					new String[]{cc,bcc},
					bcc.split(";"), 
					"Pemberitahuan bahwa TTP dipending oleh Bagian Underwriting",
					"<table width=100% class=satu>"
					+"<tr><td colspan='3'>TTP untuk nasabah dibawah ini : </td></tr>"
					+"<tr><td colspan='3'>&nbsp;</td></tr>"
					+ "<tr><td width='236'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;No. Polis 	  </td><td width='5'>:</td><td width='767'>" + FormatString.nomorPolis(no_polis) + "</td></tr>"
					+ "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Nama Pemegang Polis </td><td>:</td><td>" + pemegang + ""+ "</td></tr>" 
					+ "<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Alasan Dipending   	 </td><td>:</td><td>" + keterangan + "</td></tr>"
//					+"<tr><td colspan='3'>&nbsp;</td></tr>"
//					+"<tr><td colspan='3'>Dimohon agar  TTP  ditandatangani  ulang oleh Pemegang Polis .</td></tr>"
					+"<tr><td colspan='3'>&nbsp;</td></tr>"
					+"<tr><td colspan='3'>Terima kasih .</td></tr>"
					+"<tr><td colspan='3'>&nbsp;</td></tr>"
					+"<tr><td colspan='3'>&nbsp;</td></tr>"
					+"<tr><td><img src='cid:myLogo'></td><td colspan='2'>&nbsp;</td></tr>"
					+"<tr><td><font size='2'>"+currentUser.getLus_full_name()+"</font></td><td colspan='2'>&nbsp;</td></tr>"
					+"<tr><td><font size='2'>"+currentUser.getDept()+"</font> </td><td colspan='2'>&nbsp;</td></tr>"
					+"<tr><td><font size='2'>PT Asuransi Jiwa Sinarmas MSIG Tbk. </font></td><td colspan='2'>&nbsp;</td></tr>"
					+"<tr><td><font size='2'>Wisma Eka Jiwa Lt.8 </font></td><td colspan='2'>&nbsp;</td></tr>"
					+"<tr><td><font size='2'>JL. Mangga Dua Raya, Jkt 10730</font> </td><td colspan='2'>&nbsp;</td></tr>"
					+"<tr><td><font size='2'>Telp.+62(021)6257808 </font></td><td colspan='2'>&nbsp;</td></tr>"
					+"<tr><td><font size='2'>Fax. +62(021)6257779</font> </td><td colspan='2'>&nbsp;</td></tr>"
					+"<tr><td colspan='3'><font size='1'>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.</font></td></tr>"
					+"</table>", 
					null);
		}
		
		params.put("keterangan", keterangan);
		
		return new ModelAndView("uw/editKetTTp", params);
	}
	
	public ModelAndView editTglTrmKrmAdmin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Date tanggal = elionsManager.selectSysdateSimple();
		
		String sysdate = defaultDateFormat.format(new Date());
		
		DateFormat dfh = new SimpleDateFormat("HH");
		DateFormat dfm = new SimpleDateFormat("mm");
		
		String hh = ServletRequestUtils.getStringParameter(request, "hh", dfh.format(tanggal));
		String mm = ServletRequestUtils.getStringParameter(request, "mm", dfm.format(tanggal));
		
		Map params = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		Integer show= ServletRequestUtils.getIntParameter(request, "show",0);
		String flag= ServletRequestUtils.getStringParameter(request, "flag","");
		Map mPosisi=elionsManager.selectF_check_posisi(spaj);
		Integer lspdId=(Integer)mPosisi.get("LSPD_ID");
		String lspdPosition=(String)mPosisi.get("LSPD_POSITION");
		String keterangan;
		//
		Date tglTerimaAdmin;
		Date tglKirimPolis;
		Date tglPrintPolis;
		Date tglSpajDoc;
		List lsError=new ArrayList();
		//
		if(show==0 && lspdId!=1){//tanggal terima spaj 
			lsError.add("Posisi Polis ada di "+lspdPosition+" Tidak Bisa Edit Tanggal Terima SPAJ");
			
		}else if(show==2 && lspdId!=2){//tanggal spaj doc
			lsError.add("Posisi Polis ada di "+lspdPosition+" Tidak Bisa Edit Tanggal Terima SPAJ");
		}else if(show==1){//tanggal kirim
			//cek posisi hanya 6 dan 9
			if(! (lspdId==6 || lspdId==9 ))
				lsError.add("Posisi Polis ada di "+lspdPosition+" Tidak Bisa Edit Tanggal Terima SPAJ");
		}
		
		Map mTertanggung=elionsManager.selectTertanggung(spaj);
		tglTerimaAdmin=(Date)mTertanggung.get("MSTE_TGL_TERIMA_ADMIN");
		tglPrintPolis=(Date)mTertanggung.get("MSPO_DATE_PRINT");		
		tglKirimPolis=(Date)mTertanggung.get("MSTE_TGL_KIRIM_POLIS");
		Date tglInput = (Date) mTertanggung.get("MSPO_INPUT_DATE");
		tglSpajDoc=(Date)mTertanggung.get("MSTE_TGL_SPAJ_DOC");
		Integer lssaId=(Integer)mTertanggung.get("LSSA_ID");
		//
		Map mPemegang=elionsManager.selectPemegang(spaj);
		Integer lsspId=(Integer)mPemegang.get("LSSP_ID");
		
		if(flag.equals("")){//not save
			if(show==0){//edit tanggal terima spaj
				if(tglTerimaAdmin==null)
					params.put("tanggalTerima", sysdate);
				else
					params.put("tanggalTerima", FormatDate.toString(tglTerimaAdmin));
			}else if(show==1){//edit tanggal kirim spaj
				if(tglKirimPolis==null)
					params.put("tanggalKirim", sysdate);
				else
					params.put("tanggalKirim", FormatDate.toString(tglKirimPolis));
			}else if(show==2){//edit tanggal spaj doc
				if(tglSpajDoc==null)
					params.put("tglSpajDoc", sysdate);
				else
					params.put("tglSpajDoc", FormatDate.toString(tglSpajDoc));
			}
			keterangan=elionsManager.selectMstPositionSpajMspsDesc(spaj, show);
			
		}else{//save
			String sTglTerimaAdmin=ServletRequestUtils.getStringParameter(request, "tanggalTerimaAdmin","00/00/0000");
			String sTglKirimPolis=ServletRequestUtils.getStringParameter(request, "tanggalKirim","00/00/0000");
			String sTglSpajDoc=ServletRequestUtils.getStringParameter(request, "tglSpajDoc","00/00/0000");
			keterangan=ServletRequestUtils.getStringParameter(request, "keterangan",null);
			if(show==0){//edit tanggal terima spaj
				if(sTglTerimaAdmin.equals("00/00/0000")){
					lsError.add("Silahkan Masukan tanggal Terima Admin dengan Benar");
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(sTglTerimaAdmin + " " + hh + ":" + mm);
						
						if(tglInput!=null){
							if(tmp.before(tglInput)){
								lsError.add("Tanggal Terima Admin harus lebih besar dari tanggal input spaj!");
							}else{
								elionsManager.prosesEditTanggalSpajAdmin(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
								params.put("success", "Berhasil Update Tanggal Terima Admin");
							}
						}else{
							elionsManager.prosesEditTanggalSpajAdmin(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
							params.put("success", "Berhasil Update Tanggal Terima Spaj");
						}
					}	
				}
					params.put("tanggalTerima", sTglTerimaAdmin);
			}else if (show==1){//edit tanggal kirim spaj
				if(sTglKirimPolis.equals("00/00/0000")){
					lsError.add("Silahkan Masukan Tanggal Kirim Spaj dengan Benar");
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(sTglKirimPolis+ " " + hh + ":" + mm);
						if(tglPrintPolis!=null){
							if(tmp.before(tglPrintPolis)){
								lsError.add("Tanggal Kirim Polis harus lebih besar dari tanggal print polis!");
							}else{
								elionsManager.prosesEditTanggalSpajAdmin(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
								Map policy =(Map)elionsManager.selectPemegang(spaj);
								String noPolis=(String)policy.get("MSPO_POLICY_NO");
								String nama=(String)policy.get("MCL_FIRST");
								String subject="Print Polis";
								String emailCabang=uwManager.selectEmailCabangFromSpaj(spaj);
								if(emailCabang==null)
									emailCabang=request.getParameter("email");
								
								String lca_id = (String) mTertanggung.get("LCA_ID");
								
								if(emailCabang!=null){
									params.put("email",1);
									String to[]={emailCabang};
									String cc[]={currentUser.getEmail()};
									String message="Polis No. "+FormatString.nomorPolis(noPolis)+"\nAtas Nama "+nama+" telah selesai dicetak oleh Underwriting";
									if(!lca_id.equals("09")) email.send(false, props.getProperty("admin.ajsjava"), to, cc, null, subject,message, null);
								}else
									params.put("email",0);
	
								params.put("success", "Berhasil Update Tanggal Kirim Spaj");
							}
						}else{

							elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,null);
							Map policy =(Map)elionsManager.selectPemegang(spaj);
							String noPolis=(String)policy.get("MSPO_POLICY_NO");
							String nama=(String)policy.get("MCL_FIRST");
							String subject="Print Polis";
							String emailCabang=uwManager.selectEmailCabangFromSpaj(spaj);
							if(emailCabang==null)
								emailCabang=request.getParameter("email");
							
							String lca_id = (String) mTertanggung.get("LCA_ID");
							
							if(emailCabang!=null){
								params.put("email",1);
								String to[]={emailCabang};
								String cc[]={currentUser.getEmail()};
								String message="Polis No. "+FormatString.nomorPolis(noPolis)+"\nAtas Nama "+nama+" telah selesai dicetak oleh Underwriting";
								if(!lca_id.equals("09")) email.send(false, props.getProperty("admin.ajsjava"), to, cc, null, subject,message, null);
							}else
								params.put("email",0);

							params.put("success", "Berhasil Update Tanggal Kirim Spaj");
						
						}
						
					}
				}
					params.put("tanggalKirim", sTglKirimPolis);
			}else if(show==2){//edit tanggal spaj doc
				if(keterangan==null)
					lsError.add("Silahkan Isi Kolom Keterangan yang telah disediakan");
				if(sTglSpajDoc.equals("00/00/0000")){
					lsError.add("Silahkan Masukan Tanggal Spaj dengan Benar");
				}else{
					if(lsError.isEmpty()){
						Date tmp = completeDateFormat.parse(sTglSpajDoc+ " " + hh + ":" + mm);
						if(tglTerimaAdmin!=null){
							if(tmp.before(tglTerimaAdmin)){
								lsError.add("Tanggal terima Admin Doc harus lebih besar dari tanggal Terima Spaj!");
							}else{
								elionsManager.prosesEditTanggalSpaj(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan,null);
								params.put("success", "Berhasil Update Tanggal Spaj Doc");
							}
						}else{
							elionsManager.prosesEditTanggalSpajAdmin(spaj,currentUser,1,lspdId,lsspId,lssaId,tmp,show,keterangan);
							params.put("success", "Berhasil Update Tanggal Spaj Doc");
						}
					
					}
				}
					params.put("tglSpajDoc", sTglSpajDoc);
			}
		}
		params.put("keterangan", keterangan);
		params.put("show", show);
		params.put("lsError", lsError);
		params.put("listHH", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23});
		params.put("listMM", new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59});
		params.put("hh", hh);
		params.put("mm", mm);
		return new ModelAndView("uw/editTglTrmKrmAdmin", params);
	}

	/**Fungsi : Untuk Melakukan pencarian data simultan polis 
	 * @param :	HttpServletRequest request, HttpServletResponse response
	 * @return:	ModelAndView
	 * @author 	Ferry Harlim
	 * */
	public ModelAndView view_simultan(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		List lsError=new ArrayList();
		String nama=ServletRequestUtils.getStringParameter(request, "nama","");
		String tglLahir=ServletRequestUtils.getStringParameter(request, "tglLahir","00/00/0000");
		Integer p=ServletRequestUtils.getIntParameter(request, "p",0);
		map.put("nama", nama);
		map.put("tglLahir", tglLahir);
		List lsSimultan=new ArrayList();
		if(tglLahir.equals("00/00/0000"))
			lsError.add("Silahkan masukan Tanggal Dengan Benar");
		if(nama.equals(""))
			lsError.add("Nama Tidak Boleh Kosong");
		if(nama.length()<3)
			lsError.add("Panjang Nama minimal 3 karakter");
		
		if(p==1 && lsError.size()==0){
			lsSimultan=elionsManager.selectViewSimultan(nama,tglLahir);
		}
		map.put("lsSimultan", lsSimultan);
		map.put("lsError",lsError);
		return new ModelAndView("uw/viewer/view_simultan",map);
	}

	/**Fungsi : Untuk Melakukan akseptasi polis asm 
	 * @param :	HttpServletRequest request, HttpServletResponse response
	 * @return:	ModelAndView
	 * @author 	Ferry Harlim
	 * */
	public ModelAndView akseptasi_asm(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj=request.getParameter("spaj");
		String flag=request.getParameter("flag");
		String nopolis_asm=ServletRequestUtils.getStringParameter(request, "nopolis_asm",null);
		Map mapTt=elionsManager.selectTertanggung(spaj);
		Integer lssaId=(Integer)mapTt.get("LSSA_ID");
		if(lssaId.intValue()!=5){
			String sBegDate="00/00/0000",sEndDate="00/00/0000";
			if(flag==null){
				Map ds=this.uwManager.selectDataUsulan(spaj);
				Date begDate=(Date)ds.get("MSTE_BEG_DATE");
				Date endDate=(Date)ds.get("MSTE_END_DATE");
				sBegDate=FormatDate.toString(begDate);
				sEndDate=FormatDate.toString(endDate);
				
			}else if(flag.equals("1")){//hitung ulang
				sBegDate=request.getParameter("begDate");
				int dd,mm,yy;
				dd=Integer.parseInt(sBegDate.substring(0,2));
				mm=Integer.parseInt(sBegDate.substring(3,5));
				yy=Integer.parseInt(sBegDate.substring(6,10));
				Calendar calAwal=new GregorianCalendar(yy,mm-1,dd);
				Date begDate=calAwal.getTime();
				Date endDate=FormatDate.add(begDate, Calendar.YEAR, 1);
				sEndDate=FormatDate.toString(endDate);
			}else if(flag.equals("2")){//proses akseptasi asm
				sBegDate=request.getParameter("begDate");
				sEndDate=request.getParameter("endDate");
				int proses=elionsManager.prosesAkseptasiAsm(spaj, sBegDate, sEndDate,currentUser,nopolis_asm);
				if(proses==0){
					map.put("info", 1);
				}else if(proses==51){
					map.put("info", 2);
				}else if(proses==52){
					map.put("info", 3);
				}
			}
			map.put("begDate", sBegDate);
			map.put("endDate", sEndDate);
		}else{
			map.put("spaj", spaj);
			map.put("err", 1);
		}	
		
		return new ModelAndView("uw/akseptasi_asm",map);
	}
	
	/**Fungsi : Untuk Melakukan proses reas khusus PT. GUTHRIE
	 * @param :	HttpServletRequest request, HttpServletResponse response
	 * @return:	ModelAndView
	 * @author 	Ferry Harlim
	 * */
	public ModelAndView reas_guthrie(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Reas reas=new Reas();
		reas.setSpaj(request.getParameter("spaj"));
		Map mTertanggung=elionsManager.selectTertanggung(reas.getSpaj());
		DataUsulan2 dataUsulan=elionsManager.selectDataUsulan(reas.getSpaj(), 1);
		Date begDate=dataUsulan.getMspr_beg_date();
		reas.setInsuredNo((Integer)mTertanggung.get("MSTE_INSURED_NO"));
		reas.setMsteInsured((String)mTertanggung.get("MCL_ID"));
		reas.setUmurTt((Integer)mTertanggung.get("MSTE_AGE"));
		Map mStatus=elionsManager.selectWfGetStatus(reas.getSpaj(),reas.getInsuredNo());
		reas.setLiAksep((Integer)mStatus.get("LSSA_ID"));
		reas.setLiReas((Integer)mStatus.get("MSTE_REAS"));
		if (reas.getLiAksep()==null) 
			reas.setLiAksep( 1);
		Map mapBisnis=uwManager.selectDataUsulan(reas.getSpaj());
		Integer lsbsId=(Integer)mapBisnis.get("LSBS_ID");
		Integer lsdbsNumber=(Integer)mapBisnis.get("LSDBS_NUMBER");
		Double ldecTsi=(Double)mapBisnis.get("MSPR_TSI");
//		Double retensi=new Double(500000000);
		Double retensi=new Double(750000000);//per 1 april2008
		Integer info=0;
		//Ultra sejahtera PLB
		if(lsbsId.intValue()==89){
			if(ldecTsi>retensi || ldecTsi==0){
				info=1;//jalankan proses reas yang biasa
			}else{
				info=elionsManager.prosesReasGuthrie(reas.getSpaj(), ldecTsi, retensi, lsbsId, lsdbsNumber,begDate);
			}
		}else 
			info=2;
		map.put("info", info);
		map.put("spaj",reas.getSpaj());
		return new ModelAndView("uw/reas_guthrie",map);
	}
	
//	/**Fungsi : Untuk mengemail hasil medis ke cabang berdasarkan TABEL MEDIS UW 
//	 * @param :	HttpServletRequest request, HttpServletResponse response
//	 * @return:	ModelAndView
//	 * @author 	Ferry Harlim
//	 * */
//	public ModelAndView reas_medis(HttpServletRequest request,
//			HttpServletResponse response) throws Exception {
//		Map map=new HashMap();
//		User currentUser=(User)request.getSession().getAttribute("currentUser");
//		String spaj=ServletRequestUtils.getStringParameter(request, "spaj");
//		List lsMedis=elionsManager.selectMedical(spaj, 1);
//		String message="";
//		for(int i=0;i<lsMedis.size();i++){
//			Medical medis=(Medical)lsMedis.get(i);
//			if(i==0 && lsMedis.size()==1)
//				message+=medis.getLsmc_name();
//			else if(i==lsMedis.size()-1)
//				message+=medis.getLsmc_name();
//			else
//			message+=medis.getLsmc_name()+"+";
//		}
//		String ket=ServletRequestUtils.getStringParameter(request, "ket");
//		String to[];
//		String cc[]={currentUser.getEmail()};
//		String subject="Medis untuk SPAJ No.="+FormatString.nomorSPAJ(spaj);
//		if(ket != null)
//			email.send(to, cc, subject, message, currentUser);
//		
//		map.put("spaj", spaj);
//		
//		return new ModelAndView("uw/viewer/reas_medis",map);
//	}

	public ModelAndView input_highRiskCostm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		
		if(request.getParameter("simpan")!=null){
			elionsManager.inserthighrisk_cust(request.getParameter("Desc"));			
			map.put("simpan", null);
			map.put("Desk", null);
		}
		map.put("HighRiskCostm", elionsManager.selectHighRiskCustm());
		return new ModelAndView("uw/kyc/input_highRiskCostm",map);
	}

	
	public ModelAndView putus_kontrak(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
		SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
		String dariTanggal=request.getParameter("dariTanggal");
		String sampaiTanggal=request.getParameter("sampaiTanggal");
		Date sysdate=elionsManager.selectSysdate();
		if(dariTanggal==null)
			dariTanggal=sdf.format(sysdate);
		if(dariTanggal==null)
			dariTanggal=sdf.format(sysdate);
		
		if(request.getParameter("simpan")!=null){
			elionsManager.inserthighrisk_cust(request.getParameter("Desc"));			
			map.put("simpan", null);
			map.put("Desk", null);
		}
		map.put("HighRiskCostm", elionsManager.selectHighRiskCustm());
		return new ModelAndView("uw/kyc/putus_kontrak",map);
	}
	
	/**
	 * Dian natalia
	 * Digunakan untuk melihat data details bunga tunggakan si nasabah
	 * @param spaj
	 * @param premi ke..
	 * @param tahun ke..
	 * @return
	 * @throws Exception 
	 * 
	 */
	
	public ModelAndView hit_bunga_tunggakan(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> map=new HashMap<String, Object>(); 
		List error = new ArrayList();
		/**Ngambil parameter yang di lempar dari halaman info tahapan*/
		String reg_spaj= request.getParameter("spaj");
		
//		String premike= request.getParameter("premike");
//		String tahunke= request.getParameter("tahunke");
		String tgl_bayar=request.getParameter("tgl_bayar");
		String polis= request.getParameter("polis");
		Date dateBesok=elionsManager.selectSysdate1("dd", false, 0);
		
		/** ngambil parameter tanggal yang baru diinput di halaman bit_bunga_tunggakan*/
		BigDecimal kursToday = new BigDecimal(1);
		BillingWS bill = new BillingWS();
		HashMap mapTot = new HashMap();
		
		Double bungaPremi=new Double(0.0);
		Double tahapan=new Double(0.0);
		String lsbs_id="";
		Double ldec_premi=new Double(0.0);
		
		Double ldec_persen=new Double(0.0);
		Double ldec_premi_min=new Double(0.0);
		Double li_hari=new Double(0.0);
		Double ldec_bunga=new Double(0.0);
		Integer msbi_tahun_ke=0;
		Double msbi_stamp=new Double(0.0);
		Double ldec_tahap=new Double(0.0);
//		if (tahunke==null){
//			tahunke="1";
//		}
//		if (premike==null){
//			premike="1";
//		}
//		Integer tahun_ke= Integer.parseInt(tahunke);
//		Integer premi_ke= Integer.parseInt(premike.trim());
		
		if (request.getParameter("btnShow")!=null){
			dateBesok=new SimpleDateFormat("dd/MM/yyyy").parse(tgl_bayar);
			
			Date tglBayar = dateBesok;
			
			List total_tagih = elionsManager.selectTotalTagih(reg_spaj);
			if(total_tagih.size()==0){
				error.add("Tidak Ada Data");
				map.put("error", error);
			}else{
			mapTot = (HashMap) total_tagih.get(0);
			
//			tahapan=policyManager.selectCekTahapan(reg_spaj);
//			tahapan=tahapan==null?new Double(0.0): tahapan;
			
			map.put("reg_spaj",reg_spaj);
//			map.put("tahun_ke",tahun_ke);
//			map.put("premi_ke",premi_ke);
			map.put("tglBayar",tglBayar);
		   
			ldec_premi=(((BigDecimal) mapTot.get("PREMI_STD")).doubleValue());
		    
			bill.setTotal_premi(((BigDecimal) mapTot.get("TOTAL_PREMI")).doubleValue());//total premi
			bill.setLku_id((String) mapTot.get("LKU_ID"));
			bill.setMsbi_premi_ke( ((BigDecimal) mapTot.get("MSBI_PREMI_KE")).intValue() );
			bill.setMsbi_tahun_ke( ((BigDecimal) mapTot.get("MSBI_TAHUN_KE")).intValue() );
			bill.setReg_spaj(reg_spaj);
			bill.setLsbs_id(((BigDecimal) mapTot.get("LSBS_ID")).intValue());
			bill.setMsbi_beg_date((Date) mapTot.get("MSBI_BEG_DATE"));
			bill.setMsbi_end_date((Date) mapTot.get("MSBI_END_DATE"));
			bill.setMsbi_due_date((Date) mapTot.get("MSBI_DUE_DATE"));
			bill.setDisc(((BigDecimal) mapTot.get("DISCOUNT")).doubleValue());//diskon premi biasa
			bill.setMsbi_stamp(((BigDecimal) mapTot.get("MSBI_STAMP")).doubleValue());
			
//			hitungBungaPremi.hitungBungaPremi(bill,tglBayar);
//			tahapan=elionsManager.selectCekTahapan(bill.getReg_spaj());
//			tahapan=tahapan==null?new Double(0.0): tahapan;
//			if (tahapan==null) tahapan=0.0;
			
//			Map lds_1=(Map) policyManager.selectBillOSBunga(bill.getReg_spaj(),bill.getMsbi_tahun_ke(),bill.getMsbi_premi_ke()).get(0);
//			if(bill.getLku_id().equals("01")){
//				bungaPremi=new Double(props.getProperty("nilai.bungapremirp"));
//			}else{
//				bungaPremi=new Double(props.getProperty("nilai.bungapremielse"));
//			}
			
			
			Integer li_bisnis=bill.getLsbs_id();
			lsbs_id=FormatString.rpad("0", li_bisnis.toString() , 3);
//			Integer msbi_tahun_ke=0;//((BigDecimal) lds_1.get("MSBI_TAHUN_KE")).intValue(); 
//			Integer msbi_premi_ke=((BigDecimal) lds_1.get("MSBI_PREMI_KE")).intValue();
//			Double msdb_premium=((BigDecimal) lds_1.get("MSDB_PREMIUM")).doubleValue();
//			Double msdb_discount=((BigDecimal) lds_1.get("MSDB_DISCOUNT")).doubleValue();				
//			Date msbi_beg_date=(Date) lds_1.get("MSBI_BEG_DATE");
//			Date msbi_end_date=(Date) lds_1.get("MSBI_END_DATE");
//			Double msbi_stamp=((BigDecimal) lds_1.get("MSBI_STAMP")).doubleValue();
			
//			ldec_premi=bill.getTotal_premi()- bill.getDisc()-tahapan;
//			
			bungaPremi=cariSukuBunga(tglBayar, lsbs_id, bill.getLku_id());
			
			if("074, 076, 096, 099, 135, 136".contains(lsbs_id)){
				msbi_tahun_ke=bill.getMsbi_tahun_ke();
				if(msbi_tahun_ke==1){
					ldec_persen=new Double(1.0);
				}else if(msbi_tahun_ke==2){
					ldec_persen=new Double(0.2);
				}else if(msbi_tahun_ke==3){
					ldec_persen=new Double(0.1);
				}else if(msbi_tahun_ke==4){
					ldec_persen=new Double(0.05);
				}else {
					ldec_persen=new Double(0.0);
				}
				ldec_premi_min=ldec_premi*ldec_persen;
				ldec_premi=ldec_premi_min;
			}
			
			li_hari= new Double(FormatDate.dateDifference(bill.getMsbi_beg_date(), tglBayar, false)+1);
			
			if(li_hari<0)li_hari=new Double(0.0);
			
			if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("13082003"))<=0){
				ldec_bunga=(li_hari/365)*ldec_premi*bungaPremi/100;					
			}else if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("13082003"))>0 && tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31102003"))<=0){
				ldec_bunga=li_hari*bungaPremi/100*ldec_premi;					
			}else if(tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31102003"))>0 && tglBayar.compareTo(new SimpleDateFormat("ddMMyyyy").parse("31082005"))<=0){
				if(bill.getLku_id().equals("02")){
					bungaPremi=new Double(0.085);
					if(li_hari<=60){
						ldec_bunga = li_hari * bungaPremi / 100 * ldec_premi;
					}else{
						ldec_bunga = ( 60 * bungaPremi / 100 * ldec_premi ) + ((li_hari - 60) * 0.05 / 100 * ldec_premi );//IM No. 083/IM-DIR/X/03						
					}
				}else{
					bungaPremi=new Double(0.1);
					ldec_bunga = li_hari * bungaPremi / 100 * ldec_premi ;
				}
									
			}else{
				ldec_bunga = (li_hari/360) * (bungaPremi/100) * ldec_premi;
			}
			
			if(tglBayar.compareTo(bill.getMsbi_due_date())>0){
				Integer flag_ekalink=elionsManager.selectIsEkaLink(bill.getReg_spaj());
				Integer flag_unitLink=elionsManager.selectIsUlink(bill.getReg_spaj());
				
				if(flag_ekalink==0 || flag_unitLink==0){
					bill.setHari_denda(li_hari.intValue());
					bill.setBesar_denda(ldec_bunga);
				}else{
					bill.setHari_denda(0);
					bill.setBesar_denda(0.0);
				}
			}else{
				bill.setHari_denda(0);
				bill.setBesar_denda(0.0);
			}
			
			bill.setTahapan(tahapan);
			

			map.put("totPremi", bill.getTotal_premi());
			map.put("MSBI_BEG_DATE",bill.getMsbi_beg_date());
			map.put("MSBI_END_DATE",bill.getMsbi_end_date());
			map.put("DISCOUNT",bill.getDisc());
			map.put("SELISIH",bill.getHari_denda());
			map.put("BUNGA",bill.getBesar_denda());
			
			bill.setJumlah_bayar( bill.getTotal_premi());
			
			/**
			 * hitung total premi - diskon klo ada diskon premi
			 * TODO: klo ada diskon khusus jangan disini kali ya??
			 */
			map.put("jumDisc", bill.getDisc());
			bill.setJumlah_bayar(bill.getJumlah_bayar()-bill.getDisc());
			
			
			/**
			 * Hitung premi yang sudah didiskon di tambah tahapan klo ada
			 */
			//bill.setDisc(bill.getTotal_premi()*disc.doubleValue());//untuk diskon khusus
			if(bill.getTahapan()==null)
				bill.setTahapan(0.0);
			map.put("tahapan", bill.getTahapan());
			Double jmByr= (new Double( Math.max( 0,bill.getJumlah_bayar()+bill.getTahapan())));
			bill.setJumlah_bayar(jmByr);
			map.put("subTotal", bill.getJumlah_bayar());
			if(bill.getBesar_denda()==null)
				bill.setBesar_denda(0.0);
			Double TagihanPremi=bill.getTotal_premi()+ bill.getBesar_denda()+bill.getTahapan()- bill.getDisc();
			map.put("TagihanPremi", TagihanPremi);
			map.put("MATERAI", bill.getMsbi_stamp());
			Double TotTagih= TagihanPremi+bill.getMsbi_stamp();
			map.put("TotTagih", TotTagih);
			
			//error tgl bayar tidak boleh dibawah tgl beg date
			if(dateBesok.compareTo(bill.getMsbi_beg_date())<=0){
				error.add("Tanggal bayar tidak boleh di bawah beg_date");
			}
			
			map.put("error", error);
			
		}
		}
//		map.put("premike", premike);
//		map.put("tahunke", tahunke);
		map.put("dateBesok", dateBesok);
		return new ModelAndView("uw/viewer/hit_bunga_tunggakan",map);
	}
	
	public  Double cariSukuBunga(Date tglBayar,String lsbs_id, String lku_id){
		Double bunga=new Double(0.0);
		
		if("079, 080, 091, 092".contains(lsbs_id)){
			bunga=new Double(18.0);
		}else{
			bunga=elionsManager.selectCariSukuBunga(lku_id, tglBayar);
		}
		
		
		return bunga;
	}
	
	/**
	 * Window Hitung Bunga Pinjaman (VIEWER > VIEW > PINJAMAN > BUNGA PINJAMAN)
	 * http://yusufxp/E-Lions/uw/viewer.htm?window=bunga_pinjaman&spaj=09200100025
	 * Yusuf (13 Sep 2011) - Req Yune (Helpdesk #21107)
	 */
	public ModelAndView bunga_pinjaman(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String spaj = ServletRequestUtils.getRequiredStringParameter(request, "spaj"); 
		Map m = uwManager.selectHitungBungaPinjaman(spaj);
		if(m != null) {
			m.put("today", uwManager.selectSysdateTruncated(0));
			return new ModelAndView("uw/viewer/bunga_pinjaman", m);
		}else{
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('Tidak ada data');window.close();</script>");
			sos.close();
			return null;
		}
	}
	
	public ModelAndView permintaanBSB(HttpServletRequest request, HttpServletResponse response)throws Exception {
		Map pesan = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		Datausulan datausulan = elionsManager.selectDataUsulanutama(spaj);
		// Menu hanya untuk :
		// 1. Danamas Prima ( Sekuritas Sinarmas) ' konvensional    142-9   158-14
		// 2. Simas Prima (Bank Sinarmas ) ' konvensional   142-2   158-6
		// 3. Power Save Syariah BSM (Bank Sinarmas Syariah) ' syariah  175-2
		// 4. Personal Accident ABD     73 - 4,7,11,14     MANTA (12/05/2015)
		Boolean kategoriProdukBSB = false;
		if( (datausulan.getLsbs_id()==142 && datausulan.getLsdbs_number()==2) || (datausulan.getLsbs_id()==142 && datausulan.getLsdbs_number()==9)
			|| (datausulan.getLsbs_id()==158 && datausulan.getLsdbs_number()==6) || (datausulan.getLsbs_id()==158 && datausulan.getLsdbs_number()==14)
			|| (datausulan.getLsbs_id()==175 && datausulan.getLsdbs_number()==2)
			|| (datausulan.getLsbs_id()==73 && ("4,7,11,14".indexOf(datausulan.getLsdbs_number().toString())>-1)) ){
			kategoriProdukBSB = true;
		}else{
			pesan.put("lsError", "Proses Permintaan BSB hanya bisa untuk Produk Danamas Prima/Simas Prima/ Power Save Syariah BSM");
		}
		
		//jika validasi sudah valid semua, masuk proses kirim email
		if(pesan.isEmpty()){
			Boolean proses = uwManager.prosesPermintaanBSB(spaj, datausulan.getLsbs_id(), datausulan.getLsdbs_number(), currentUser, datausulan);
			if(proses==true){
				pesan.put("successMessage", "Email sudah berhasil dikirim");
			}else{
				pesan.put("lsError", "Email tidak berhasil dikirim. Silakan hubungi IT mengenai perihal gagal Email ini.");
			}
		}
		
		return new ModelAndView("common/info_proses", pesan);
	}
	
	public ModelAndView autoDebetPPertama(HttpServletRequest request, HttpServletResponse response)throws Exception {
		Map pesan = new HashMap();
		String spaj = (String) ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Account_recur account_recur = elionsManager.select_account_recur(spaj);
		boolean refferal = true;
		HashMap mapregion = Common.serializableMap(bacManager.selectRegion(spaj));
		//Ryan - Untuk SITE DMTM - BSIM Now AUTODEBET dari BSIM Langsung
		if(((String)mapregion.get("LCA_ID").toString()).equals("40") && ((String)mapregion.get("LWK_ID").toString()).equals("01") && ((String)mapregion.get("LSRG_ID").toString()).equals("02")){
			/*Map mReffBii=elionsManager.selectReferrerBii(spaj);
			if(mReffBii==null){
				refferal=false;
				pesan.put("lsError", "Untuk produk ini harap input refferal terlebih dahulu sebelum di proses autodebet.");
			}*/
			refferal=false;
			pesan.put("lsError", "Untuk SITE DMTM - BSIM Tidak Bisa Menggunakan Proses AUTODEBET Premi Pertama.");
		}
		
		//CREATE BY RYAN
		if(((String)mapregion.get("LCA_ID").toString()).equals("40")) {// TERM INSURANCE (SMS) & DANA SEJAHTERA
			List<MedQuest> questionare = Common.serializableList(uwManager.selectMedQuest(spaj, 1));//insured = 1
			if(questionare == null || questionare.size() == 0){
				pesan.put("lsError", "Questionare masih Kosong. Silakan mengisi questionare terlebih dahulu (Menu Pengisian Kuesionare )");
				refferal=false;
			}
		}
		
		if(account_recur.getFlag_autodebet_nb()!=null){
			if(account_recur.getFlag_autodebet_nb()==1){
				if(uwManager.selectCountMstBillingNB(spaj)>0){//cek di billing untuk plan new business apakah ada. Apabila sudah ada, maka sudah pernah dilakukan proses Autodebet(lspd_id billing=4)/sedang diproses Autodebet(lspd_id billing!=4)
					if(elionsManager.selectstsgutri(spaj)==4){//posisi lspd_id di billing 4 (kalau sudah selesai di autodebet)
						pesan.put("lsError", "SPAJ ini sudah pernah dilakukan proses Autodebet Premi Pertama di Finance.");
					}else{
						pesan.put("lsError", "SPAJ ini sedang dalam tahap proses Autodebet di Finance.");
					}
					Map mPemegang =elionsManager.selectPemegang(spaj);
					String lcaId=(String)mPemegang.get("LCA_ID");
					String lwkId=(String)mPemegang.get("LWK_ID");
					String lsrg_id=(String)mPemegang.get("LSRG_ID");
					if(lcaId.equals("40") && lwkId.equals("01") && lsrg_id.equals("02")){
						Map mReffBii=elionsManager.selectReferrerBii(spaj);
						if( mReffBii==null) pesan.put("lsError", "Harap Input Referral Bank Terlebih Dahulu.");
					}
				}else if(refferal){
					Integer proses =uwManager.prosesAutoDebetNB(spaj,currentUser);
					if(proses>0){
						if(proses==51){
							pesan.put("lsError","No Polis Kosong , Silahkan Hubungi EDP");
						}else if(proses==52){
							pesan.put("lsError","Nomor Polis Kembar, Coba tranfer ulang...");
						}else if(proses==81){
							pesan.put("lsError","Produk Unit-Linked !!!~nAlokasi Investasi Belum Lengkap ");
						}else if(proses==82){
							pesan.put("lsError","Produk Unit-Linked !!!~nBiaya Alokasi Investasi Belum Lengkap !!!");
						}/*else if(hasil==83){
							err.reject("","Power SaveBelum Lengkap !!!Silahkan Tekan Tombol Investasi");
							proSubmit=false;
						}*/
					}else{
						Policy policy=elionsManager.selectDw1Underwriting(spaj,118,1);
						pesan.put("lsError", "Proses AutoDebet Berhasil ditransfer ke Finance, Silakan Menunggu Konfirmasi Via Email dari Finance Untuk Proses AutoDebet. No Polis :"+policy.getMspo_policy_no());
					}
				}
				
				
			}else{
				pesan.put("lsError", "SPAJ ini tidak melakukan proses Autodebet Premi Pertama. Silakan dipastikan kembali");
			}
		}else{
			pesan.put("lsError", "SPAJ ini tidak melakukan proses Autodebet Premi Pertama. Silakan dipastikan kembali");
		}
		return new ModelAndView("uw/autodebet", pesan);
	}

	public ModelAndView kirimUlangSoftcopy(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Date tanggal = elionsManager.selectSysdateSimple();
		String sysdate = defaultDateFormat.format(new Date());
		Map params = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String emailnasabah = ServletRequestUtils.getStringParameter(request, "email", "");
		Integer show= ServletRequestUtils.getIntParameter(request, "show",0);
		String flag= ServletRequestUtils.getStringParameter(request, "flag","");
		Pemegang pmg = elionsManager.selectpp(spaj);
		List<File> attachments = new ArrayList<File>();
		List<String> error = new ArrayList<String>();
		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
		File sourceFile = new File(exportDirectory+"\\"+"softcopy.pdf");
		attachments.add(sourceFile);
		
		if(!currentUser.getLde_id().equals("11")){
		    params.put("main", "true");
	     }
		
		if(flag.equals("")){//not save
			
		}else{//save
			//if(sourceFile.equals("")
			if(!sourceFile.exists()){
				error.add("Data SOFTCOPY Kosong, Pengiriman GAGAL");
			}else{
			email.send(true, 
					props.getProperty("admin.ajsjava"), 
					new String[]{pmg.getMspe_email(),emailnasabah}, 
					new String[]{"ryan@sinarmasmsiglife.co.id", currentUser.getEmail()},
					null, 
					"PT . ASURANSI JIWA SINARMAS MSIG",
					"Telah dilakukan proses pengiriman ulang softcopy oleh PT ASURANSI JIWA SINARMAS MSIG Tbk. a/n "+pmg.getMcl_first()
					+ "<br><br>nb: E-mail ini dikirim secara otomatis melalui sistem E-Lions.", 
					attachments);
			error.add("Softcopy Berhasil Terkirim .");
			}
		}
			
		if(!error.equals("")){
			params.put("pesanError", error);
		}
		params.put("pmg", pmg);
		return new ModelAndView("uw/viewer/kirimlagi", params);
	}
	
	/**
	 * Andhika (30 Okt 2013) - Req Hayatin (Helpdesk #42077)
	 */
	public ModelAndView emailexpired(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");

		CmdSpajExpired uploader = new CmdSpajExpired();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(uploader);
		binder.bind(request);
		uploader.setErrorMessages(new ArrayList<String>());
		DateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
		
			Map info = bacManager.selectInformasiSpajExpired(uploader.getSpaj());
			if(info==null){
				uploader.getErrorMessages().add("SPAJ ini belum expired");
			}else{	
			uploader.setEmailto((String) info.get("MSPE_EMAIL"));
			uploader.setEmailsubject("Pengembalian Premi SPAJ " + info.get("REG_SPAJ") + " a.n. " + info.get("MCL_FIRST"));
			}
			StringBuffer pesan = new StringBuffer();
			String kalimatPertama = "";
			
			List detBisnis = elionsManager.selectDetailBisnis(uploader.getSpaj());
			String lsbs = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			Date tgl = (Date) info.get("PROSES_TERAKHIR");
			
				pesan.append("Kepada Yth.\n");
				pesan.append(info.get("GELAR") + " " + info.get("MCL_FIRST")+"\n");
				pesan.append("di tempat\n\n");
				pesan.append("Selamat Pagi,\n\n");
				pesan.append("Sehubungan dengan persyaratan kelengkapan data yang kami minta sejak <strong>" + FormatDate.toIndonesian(tgl) + "</strong> tidak dipenuhi hingga saat ini, maka dengan ini kami sampaikan bahwa permohonan asuransi tersebut telah kadaluarsa.\n");
				pesan.append("Selanjutnya untuk pengembalian premi pertama yang telah dibayar, akan kami kembalikan ke rekening nasabah sesuai dengan yang tercantum di SPAJ.\n");
				pesan.append("\n");
				pesan.append("No. Rekening	:<strong>" + info.get("NO_REK") + "</strong> \n");
				pesan.append("Atas Nama 	:<strong>" + info.get("ATAS_NAMA") + "</strong> \n");
				pesan.append("Nama bank 	:<strong>" + info.get("NAMA_BANK") + "</strong> \n");
				pesan.append("Cabang 		:<strong>" + info.get("CABANG") + "</strong> \n");
				pesan.append("Kota		:<strong>" + info.get("KOTA") + "</strong> \n");
				pesan.append("\n");
				pesan.append("Mohon hal ini disampaikan ke nasabah ybs, bila tidak ada jawaban dalam 1 hari setelah email kami kirimkan maka kami akan lakukan proses batal.\n");
				pesan.append("Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n");
				pesan.append("\n");
				pesan.append("Hormat kami, \n");
				pesan.append("PT Asuransi Jiwa Sinarmas MSIG Tbk.");
			
			uploader.setEmailmessage(pesan.toString().replaceAll("\n", "</br>"));

				String emailCabang = uwManager.selectEmailCabangFromSpaj(uploader.getSpaj());
				if(emailCabang!=null) {
					if(!emailCabang.trim().equals("")) {
						if(emailCabang.toLowerCase().matches("^.+@[^\\.].*\\.[a-z]{2,}$")) {
							uploader.setEmailto(emailCabang);
//							uploader.setEmailto("andhika@sinarmasmsiglife.co.id");
						}
					}
				}
				
			uploader.setEmailcc("hayatin@sinarmasmsiglife.co.id");
//			uploader.setEmailcc("parniatun@sinarmasmsiglife.co.id;chriswantini@sinarmasmsiglife.co.id;rachel@sinarmasmsiglife.co.id;hayatin@sinarmasmsiglife.co.id;ingrid@sinarmasmsiglife.co.id");
				
				// kirim
				try {
					email.send(true,
							currentUser.getEmail(), 
							uploader.getEmailto().replaceAll(" ", "").split(";"), 
							uploader.getEmailcc().replaceAll(" ", "").split(";"), 
							new String[] {currentUser.getEmail(), "andhika@sinarmasmsiglife.co.id"},
							uploader.getEmailsubject(), uploader.getEmailmessage(), null);
				}catch(Exception e) {
					logger.error("ERROR :", e);
					throw e;
				}
				
			request.setAttribute("success", "SPAJ dengan nomor "+uploader.getSpaj()+" berhasil di e-mail ke " + uploader.getEmailto());
		
		return new ModelAndView("uw/email_expired", "cmd", uploader);
	}	
	
	/**
	 * Posisi Berkas (SK Debet / Kredit)
	 * Req : Maretno Sibarani (Helpdesk #44865)
	 * @author Daru
	 * @since 03 Dec 2013
	 */
	public ModelAndView posisi_berkas(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		
		String reg_spaj = request.getParameter("spaj");
		map.put("spaj", reg_spaj);
		
		String save = request.getParameter("save");
		if(save != null) {
			String lus_id = currentUser.getLus_id();
			try {
				Integer posisi = Integer.valueOf(request.getParameter("posisi"));
				String keterangan = request.getParameter("keterangan");
				bacManager.updatePosisiSkDebetKredit(reg_spaj, posisi, lus_id, keterangan);
				map.put("msg", "Posisi berkas berhasil disimpan.");
			} catch(NumberFormatException e) {
				map.put("errMsg", "Posisi belum dipilih!");
			}
		}
		
		return new ModelAndView("uw/viewer/posisi_berkas",map);
	}
	
	public ModelAndView no_kartu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		int err = 0;
		
		String reg_spaj = request.getParameter("spaj");
		String jenis = request.getParameter("jenis");
		String no_kartu = request.getParameter("no_kartu");
		String no_kartu_lama = "";
		String pas_id = "";
		String no_blanko = "";
		String reg_id_pas = "";
		String lsbs_id = "";
		String lsdbs_number = "";
		
		if("0".equals(jenis)){
			err++;
			map.put("errMsg", "Jenis belum dipilih!");
		}else if("1".equals(jenis)){//PAS SYARIAH
			Map data_pas = bacManager.selectNoKartuPas(reg_spaj);
			if(data_pas != null){
				lsbs_id = new BigDecimal(data_pas.get("PRODUCT_CODE").toString()).toString();
				lsdbs_number = new BigDecimal(data_pas.get("PRODUK").toString()).toString();
				Map kartu_pas = uwManager.selectKartuPas(no_kartu);
				if(kartu_pas == null){
					err++;
					map.put("errMsg", "No Kartu Tidak Ditemukan!");
				}else{
					String produk = new BigDecimal(kartu_pas.get("PRODUK").toString()).toString();
					if("205".equals(lsbs_id)){
						if("11".equals(produk))produk = "1,5"; //PERDANA SYARIAH
						else if("12".equals(produk))produk = "2,6"; //SINGLE SYARIAH
						else if("13".equals(produk))produk = "3,7"; //CERIA SYARIAH
						else if("14".equals(produk))produk = "4,8"; //IDEAL SYARIAH
						if(produk.contains(lsdbs_number)){
							if(data_pas.get("MSPO_NASABAH_ACC") != null){
								//no_kartu = (String) data_pas.get("MSPO_NASABAH_ACC");
								//no_kartu_lama = (String) data_pas.get("MSPO_NASABAH_ACC");
							}
							pas_id = new BigDecimal(data_pas.get("MSP_ID").toString()).toString();
							reg_id_pas = (String) data_pas.get("MSP_FIRE_ID");
						}else{
							err++;
							map.put("errMsg", "No kartu tidak sesuai untuk produk PAS SYARIAH!");
						}
					}else{
						err++;
						map.put("errMsg", "No Spaj bukan produk PAS SYARIAH!");
					}
				}
				if(data_pas.get("MSPO_NASABAH_ACC") != null){
					no_kartu_lama = (String) data_pas.get("MSPO_NASABAH_ACC");
				}
			}else{
				err++;
				map.put("errMsg", "No Spaj bukan produk PAS!");
			}
		}else if("2".equals(jenis)){//SMART ACCIDENT CARE
			Map data_pas = bacManager.selectNoKartuPas(reg_spaj);
			if(data_pas != null){
				lsbs_id = new BigDecimal(data_pas.get("PRODUCT_CODE").toString()).toString();
				lsdbs_number = new BigDecimal(data_pas.get("PRODUK").toString()).toString();
				Map kartu_pas = uwManager.selectKartuPas(no_kartu);
				if(kartu_pas == null){
					err++;
					map.put("errMsg", "No Kartu Tidak Ditemukan!");
				}else{
					String produk = new BigDecimal(kartu_pas.get("PRODUK").toString()).toString();
					if("187".equals(lsbs_id)){
						if("1".equals(produk))produk = "11"; //PERDANA 
						else if("2".equals(produk))produk = "12"; //SINGLE 
						else if("3".equals(produk))produk = "13"; //CERIA 
						else if("4".equals(produk))produk = "14"; //IDEAL 
						if(produk.contains(lsdbs_number)){
							if(data_pas.get("MSPO_NASABAH_ACC") != null){
								//no_kartu = (String) data_pas.get("MSPO_NASABAH_ACC");
								//no_kartu_lama = (String) data_pas.get("MSPO_NASABAH_ACC");
							}
							pas_id = new BigDecimal(data_pas.get("MSP_ID").toString()).toString();
							reg_id_pas = (String) data_pas.get("MSP_FIRE_ID");
						}else{
							err++;
							map.put("errMsg", "No kartu tidak sesuai untuk produk SMART ACCIDENT CARE");
						}
					}else{
						err++;
						map.put("errMsg", "No Spaj bukan produk SMART ACCIDENT CARE");
					}
				}
				if(data_pas.get("MSPO_NASABAH_ACC") != null){
					no_kartu_lama = (String) data_pas.get("MSPO_NASABAH_ACC");
				}
			}else{
				err++;
				map.put("errMsg", "No Spaj bukan produk PAS!");
			}
		}
			
		String save = request.getParameter("save");
		if(save != null && err == 0) {
			String lus_id = currentUser.getLus_id();
			if(StringUtil.isEmpty(jenis)){
				map.put("errMsg", "Jenis belum dipilih!");
			}else if(StringUtil.isEmpty(no_kartu)){
				map.put("errMsg", "No Kartu harus diisi!");
			}else{
				try {
					String jenisProd = "PAS_SYARIAH";
					if("2".equals(jenis)){
						jenisProd = "SAC";
					}
					String cek_produk = uwManager.selectCekNoKartu(no_kartu, jenisProd, 0);
					
					if(cek_produk == null){
						map.put("msg", "No Kartu sudah pernah dipakai!");
					}else{
						Map paramBlanko = new HashMap();
						paramBlanko.put("no_kartu", no_kartu);
						int err_p = uwManager.updateNoKartu(reg_spaj, pas_id, no_kartu_lama, no_kartu, lus_id, jenis);
						if(err_p == 1){
							map.put("msg", "No Kartu gagal disimpan!");
						}else{
							map.put("msg", "No Kartu berhasil disimpan.");
						}
					}
				} catch(NumberFormatException e) {
					map.put("msg", "No Kartu gagal disimpan!");
				}
			}
		}
		
		map.put("spaj", reg_spaj);
		map.put("jenis", jenis);
		map.put("no_kartu", no_kartu);
		map.put("no_kartu_lama", no_kartu_lama);
		
		return new ModelAndView("uw/viewer/no_kartu",map);
	}
	
	/**
	 * Catatan Polis
	 * http://localhost/E-Lions/uw/viewer.htm?window=catatan_polis
	 * Canpri (28 Nov 2013) - Req Erty - Admin Cabang (Helpdesk #45181)
	 */
	public ModelAndView catatan_polis(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map params = new HashMap();
		
		String pemegang = ServletRequestUtils.getStringParameter(request, "pemegang", "");
		String bdate = ServletRequestUtils.getStringParameter(request, "bdate", "");
		String err = "";
		
		if (request.getParameter("btnView")!=null){
			List data = bacManager.selectCatatanPolis(pemegang,bdate);
			
			if(data.isEmpty())err = "Data tidak ada.";
			params.put("data", data);
		}
		
		params.put("err", err);
		params.put("bdate", bdate);
		params.put("pemegang", pemegang.toUpperCase());
		return new ModelAndView("uw/viewer/catatan_polis", params);
	}
	
	/**
	 * Video
	 * http://localhost/E-Lions/uw/viewer.htm?window=video
	 * Canpri (12 Feb 2014)
	 */
	public ModelAndView video(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map params = new HashMap();
		
		String video = ServletRequestUtils.getStringParameter(request, "video", "0");
		
		if(video.equals("1")){
			String dir = ServletRequestUtils.getStringParameter(request, "dir", "deeper");
			String fileName = ServletRequestUtils.getStringParameter(request, "file", "Fund_Fact_Sheet.flv");
			
			params.put("dir", dir);
			params.put("vid", fileName);
			return new ModelAndView("uw/viewer/video", params);
		}
		
		return new ModelAndView("uw/viewer/video", params);
	}
	
	/**
	 * Polis Retur
	 * http://localhost/E-Lions/uw/viewer.htm?window=polis_retur
	 * lufi (17 Oktober 2014)
	 */
	public ModelAndView polis_retur(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map params = new HashMap();
		String spaj=request.getParameter("spaj");
		params.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		params.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		String tabs="1";	
		ArrayList<DropDown> listproses = new ArrayList<DropDown>();
		listproses.add(new DropDown("00","------PILIH PROSES--------"));				
		listproses.add(new DropDown("01","POLIS RETUR"));
		listproses.add(new DropDown("02","KIRIM ULANG POLIS RETUR"));
		String pesan="";
		if(request.getParameter("sending")!=null){
		   tabs="1";
		   String bdate = ServletRequestUtils.getStringParameter(request, "bdate");	   
		   String proses=ServletRequestUtils.getStringParameter(request, "proses",null);
		   String alasan=ServletRequestUtils.getStringParameter(request, "alasan",null);
		   String s_noresi=ServletRequestUtils.getStringParameter(request, "resi",null);
		   boolean hasil=bacManager.prosesPolisRetur(spaj,bdate,proses,alasan,s_noresi,currentUser);
		   if(hasil){
			   pesan="Berhasil Proses. Cek UW Info";
		   }else{
			   pesan="Proses Gagal, harap hubungi IT";
		   }
			   
		   
		}else if(request.getParameter("showReport")!=null){
		   tabs="2";
		   String bdate = ServletRequestUtils.getStringParameter(request, "bdate1");
		   String edate = ServletRequestUtils.getStringParameter(request, "edate1");
		   String proses=ServletRequestUtils.getStringParameter(request, "proses1",null);
		   ArrayList data=new ArrayList();
		   data=bacManager.selectReportPolisDataRetur(bdate,edate,proses);
		   if(data.size() > 0){ //bila ada data
	    		
	    		ServletOutputStream sos2 = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.uw.polis_retur") + ".jasper");
	    		
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> param = new HashMap<String, Object>();
	    		param.put("tanggal1", bdate);
	    		param.put("tanggal2", edate);
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, new JRBeanCollectionDataSource(data));

		    	if(request.getParameter("showXLS") != null){
		    		response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos2);
		            exporter.exportReport();
		    	}else if(request.getParameter("showPDF") != null){
		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos2);
		    	}
		    	sos2.flush();
	    		sos2.close();
	   		 }else{ //bila tidak ada data
	   			ServletOutputStream sos = response.getOutputStream();
	   			sos.println("<script>alert('Tidak ada data');window.close();</script>");
	   			sos.flush();
	   			sos.close();
	   		}
		   return null;
		}
		params.put("spaj", spaj);
		params.put("listproses", listproses);
		params.put("showTab", tabs);
		params.put("pesan", pesan);
		return new ModelAndView("uw/viewer/polis_retur", params);
	}
	
	public ModelAndView followup_csfcall(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String flag = ServletRequestUtils.getStringParameter(request, "flag", "0");
		String chbox1 = ServletRequestUtils.getStringParameter(request, "chbox1", "");
		String chbox2 = ServletRequestUtils.getStringParameter(request, "chbox2", "");
		String chbox3 = ServletRequestUtils.getStringParameter(request, "chbox3", "");
		String chboxAdmin = ServletRequestUtils.getStringParameter(request, "chboxAdmin", "");
		String keterangan = ServletRequestUtils.getStringParameter(request, "keterangan", "");
		HashMap map = new HashMap();
		ArrayList<String> errorMessage = new ArrayList<String>();
		
		if(request.getParameter("save")!=null) {
			if(!keterangan.equals("")){
				if(!chbox1.equals("") || !chbox2.equals("") || !chbox3.equals("")){
					String desc = bacManager.prosesFollowUpCsfCall(currentUser, spaj, chbox1, chbox2, chbox3, chboxAdmin, keterangan,flag);
					errorMessage.add(desc);
				}else{
					errorMessage.add("Harap pilih salah satu kategori!");
				}
			}else{
				errorMessage.add("Kolom keterangan harus diisi!");
			}
		}
		
		map.put("errorMessage", errorMessage);
		map.put("spaj", spaj);
		map.put("flag", flag);
		map.put("chbox1", chbox1);
		map.put("chbox2", chbox2);
		map.put("chbox3", chbox3);
		map.put("chboxAdmin", chboxAdmin);
		map.put("keterangan", keterangan);
		return new ModelAndView("uw/viewer/followup_csfcall", map);
	}
	
	public ModelAndView view_report_cs(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		if("01,12,32".indexOf(currentUser.getLde_id()) > -1) {
			map.put("access", true);
		}
		
		ArrayList<DropDown> listReport = new ArrayList<DropDown>();
		listReport.add(new DropDown("1", "Report Complaint Register E-MNC"));
		listReport.add(new DropDown("2", "Report Complaint Register E-Corporate"));
		map.put("listReport", listReport);
		
		String today = defaultDateFormat.format(uwManager.selectSysdateTruncated(0));
    	String begDate = ServletRequestUtils.getStringParameter(request, "begDate", null);
    	String endDate = ServletRequestUtils.getStringParameter(request, "endDate", today);
    	map.put("begDate", begDate);
    	map.put("endDate", endDate);
		
		if(request.getParameter("showReport") != null) {
			Integer jenisReport = ServletRequestUtils.getIntParameter(request, "jenisReport", 0);
			String tipe = ServletRequestUtils.getStringParameter(request, "tipe", "pdf");
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("jenisReport", jenisReport);
			params.put("begDate", begDate);
			params.put("endDate", endDate);
			
			ArrayList dataSource = Common.serializableList(bacManager.selectDynamicReportCs(params));
			if(dataSource.size() > 0) {
//				HashMap<String, Object> params = new HashMap<String, Object>();
//				params.put("begDate", begDate);
//				params.put("endDate", endDate);
				
				ServletOutputStream os = response.getOutputStream();
				String reportFile = "";
				if(new Integer(1).equals(jenisReport) || new Integer(2).equals(jenisReport)) {
					reportFile = props.getProperty("report.cs.report_complaint_register") + ".jasper";
				}
				
        		File sourceFile = Resources.getResourceAsFile(reportFile);
        		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
        		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(dataSource));
        		
        		if("pdf".equals(tipe))
        			JasperExportManager.exportReportToPdfStream(jasperPrint, os);
        		else if("xls".equals(tipe)) {
        			response.setContentType("application/vnd.ms-excel");
        			JRXlsExporter exporter = new JRXlsExporter();
        			exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
        			exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, os);
        			exporter.exportReport();
        		}
        		
        		if(os != null)
        			os.close();
			} else {
				ServletOutputStream out=response.getOutputStream();
        		out.println("<script>alert('Tidak ada data');</script>");
        		if(out!=null)
        			out.close();
			}
			
			return null;
		}
		
		return new ModelAndView("uw/viewer/view_report_cs", map);
	}
	
	public ModelAndView mainmaterai(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		return new ModelAndView("uw/viewer/mainmaterai", "cmd", map);
	}
	
	public ModelAndView mainQuestionare(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		Map mapBisnis=uwManager.selectDataUsulan(spaj);
		Integer lsbsId=(Integer)mapBisnis.get("LSBS_ID");
		map.put("spaj", spaj);
		map.put("lsbsId", lsbsId);
		return new ModelAndView("uw/viewer/mainquestionare", map);
	}
	
	/**
	 * Controller untuk Further Collection di menu Inputan Premi Collection
	 * @author Ridhaal
	 * @since 02/05/2016
	 * @param request
	 * @param response
	 * @return
	 * @link http://localhost/E-Lions/uw/uw.htm?window=furtherCollection
	 */
	public ModelAndView furtherCollection(HttpServletRequest request, HttpServletResponse response)throws Exception {
		Map hm_map  = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		Datausulan datausulan = elionsManager.selectDataUsulanutama(spaj);
		 
		String flagStatus = ServletRequestUtils.getStringParameter(request, "flagStatus", "");
		String flagStatusFR = ServletRequestUtils.getStringParameter(request, "flagStatusFR", "");
		Integer lsbs_id = datausulan.getLsbs_id();
		Integer lsdbs_number = datausulan.getLsdbs_number();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date nowDate = bacManager.selectSysdate();
		Integer speedy = 0;
		String sukses = "";
		
		// - Produk Danamas Prima : yancindy.malino@sinarmas-am.co.id, yancindy.malino@sinarmassekuritas.co.id, cindy@sinarmassekuritas.co.id, dan natalia listiawati ; CC : user uw
		// - Produk Simas Prima Bank Sinarmas : email cs sesuai cabang BSM (terlampir) dan AO sesuai cabang BSM ; CC : user UW
		// - Produk Power Save Syariah BSM : cs0910@banksinarmas.com, taufik.rudiyanto@banksinarmas.com, sadewo@sinarmasmsiglife.co.id, dan siti nihayatun ; CC : user UW
		// - Produk Personal Accident ABD
		// - Produk Agency : email admin atau email agen 
		String emailto = currentUser.getEmail();
		String emailcc = currentUser.getEmail()+";"+"newbusiness@sinarmasmsiglife.co.id;collectionindividu@sinarmasmsiglife.co.id;";
		
		if((lsbs_id==142 && lsdbs_number==2) || (lsbs_id==158 && lsdbs_number==6)){
			String email_cab = uwManager.selectEmailCabangBankSinarmas(spaj);
			String email_ao = uwManager.selectEmailAoBankSinarmas(spaj);
			if(email_cab == null) email_cab = "";
			if(email_ao == null) email_ao = "";
			emailto = email_cab+";"+email_ao;
			
		}else if((lsbs_id==142 && lsdbs_number==9) || (lsbs_id==158 && lsdbs_number==14)){
			emailto = "yancindy.malino@sinarmas-am.co.id;yancindy.malino@sinarmassekuritas.co.id;cindy@sinarmassekuritas.co.id;natalia@sinarmasmsiglife.co.id;";
			
		}else if((lsbs_id==175 && lsdbs_number==2) ){
			emailto = "cs0910@banksinarmas.com;taufik.rudiyanto@banksinarmas.com;sadewo@sinarmasmsiglife.co.id;bancass_bsk01@sinarmasmsiglife.co.id;";
		
		}else if(lsbs_id==73 && ("4,7,11,14".indexOf(lsdbs_number.toString())>-1)){			
			String email_cab = uwManager.selectEmailCabangBankSinarmas(spaj);
			String email_ao = uwManager.selectEmailAoBankSinarmas(spaj);
			if(email_cab == null) email_cab = "";
			if(email_ao == null) email_ao = "";
			emailto = email_cab+";"+email_ao;
			emailcc = "alif_bam@sinarmasmsiglife.co.id;rizkiyano@sinarmasmsiglife.co.id;Sylvia@sinarmasmsiglife.co.id;Retno@sinarmasmsiglife.co.id;" +
								   "Liana@sinarmasmsiglife.co.id;ningrum@sinarmasmsiglife.co.id;Iriana@sinarmasmsiglife.co.id";
		}else{			
			String emailAdmin = uwManager.selectEmailAdmin(spaj);
	    	Map agen = bacManager.selectEmailAgen(spaj);
			String emailAgen = (String) agen.get("MSPE_EMAIL");
	    		    	
	    	if(emailAdmin == null) emailAdmin = "";
			if(emailAgen == null) emailAgen = "";
			emailto = emailAdmin+";"+emailAgen;	    	
	    	//if(!currentUser.getEmail().equals(""))emailcc = currentUser.getEmail();
	    		    	
		}
		

//		
//		if(pesan.isEmpty()){
//			Boolean proses = uwManager.prosesPermintaanBSB(spaj, datausulan.getLsbs_id(), datausulan.getLsdbs_number(), currentUser, datausulan);
//			if(proses==true){
//				pesan.put("successMessage", "Email sudah berhasil dikirim");
//			}else{
//				pesan.put("lsError", "Email tidak berhasil dikirim. Silakan hubungi IT mengenai perihal gagal Email ini.");
//			}
//		}
		
		if(request.getParameter("btnKirimAndSave") != null){
			Boolean proses = false;
			emailto = ServletRequestUtils.getStringParameter(request, "to", "");
			emailcc = ServletRequestUtils.getStringParameter(request, "cc", "");
			String detailFR = ServletRequestUtils.getStringParameter(request, "detail", "");
				proses = bacManager.prosesFurtherCollection(spaj, datausulan.getLsbs_id(), datausulan.getLsdbs_number(), currentUser, datausulan, flagStatusFR, emailto,emailcc, detailFR );
				
				if(proses==true){
					sukses = "Status SPAJ berhasil di perbarui menjadi Further Collection dan email berhasil di kirimkan";
					
				}else{
					sukses = "Status belum terupdate dan Email tidak berhasil dikirim. Silakan hubungi IT mengenai perihal gagal Email ini.";
				}
				
				speedy = 1;
			
			
		}
		
		if(request.getParameter("btnSave") != null){
			bacManager.insertMstPositionSpajWithSubIdBas(currentUser.getLus_id(), "FC: Complete and No Further Collection", spaj, 1 ,19);
			speedy = 1;
			sukses = "Status SPAJ berhasil di akseptasi menjadi Complete.";
		}
		
		
		hm_map.put("to", emailto+=";");
		hm_map.put("cc", emailcc+=";");
		hm_map.put("speedy", speedy);
		hm_map.put("successMessage", sukses);
		
		return new ModelAndView("uw/further_colection", hm_map );
	}
}