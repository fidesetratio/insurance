/**
 * @author  : Ferry Harlim
 * @created : Aug 20, 2007 11:34:46 AM
 */
package com.ekalife.elions.web.uw;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.dao.BasDao;
import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.BlackList;
import com.ekalife.elions.model.BlackListFamily;
import com.ekalife.elions.model.Client;
import com.ekalife.elions.model.CmdInputBlacklist;
import com.ekalife.elions.model.CommandUploadUw;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetBlackList;
import com.ekalife.elions.model.Drek;
import com.ekalife.elions.model.Hadiah;
import com.ekalife.elions.model.Insured;
import com.ekalife.elions.model.KuesionerPelayananAll;
import com.ekalife.elions.model.KuesionerPelayananByGroup;
import com.ekalife.elions.model.KuesionerPelayananByQuestion;
import com.ekalife.elions.model.Payment;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.ProSaveBayar;
import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.Scan;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.TopUp;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.Upp;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.VirtualAccount;
import com.ekalife.elions.model.kyc.NewBusinessCase;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.Print;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.jasper.JasperScriptlet;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentMultiController;
import com.google.gson.Gson;
import com.ibatis.common.resources.Resources;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.FileUtil;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.DateFormats;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

public class UwMultiController extends ParentMultiController {
	protected final Log logger = LogFactory.getLog( getClass() );
	/**
	 * Yusuf (req Edy Kohar, 20 jun 2011) top seller by referral di layar utama BSM 
	 */
	public ModelAndView top_seller(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		
		//Yusuf - querynya lama, bisa 10 detik, ditaro di session aja
		List listIDR = (List) session.getAttribute("listTopSellerIDR");
		List listUSD = (List) session.getAttribute("listTopSellerUSD");
		
		if(listIDR == null || listUSD == null){
			List<Map> listTopSeller = uwManager.selectTopSellerBSM(currentUser.getCab_bank());
			
			listIDR = new ArrayList();
			listUSD = new ArrayList();
			
			for(Map m : listTopSeller){
				if(m.get("KURS").equals("Rp")){
					listIDR.add(m);
				}else if(m.get("KURS").equals("USD")){
					listUSD.add(m);
				}
			}
			
			session.setAttribute("listTopSellerIDR", listIDR);
			session.setAttribute("listTopSellerUSD", listUSD);
		}
		
		return new ModelAndView("cross_selling/top_seller");
	}
	
	//Anta
	public ModelAndView spaj_mall_untransf(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		
		List listSpaj = (List) session.getAttribute("listSpajMall");
		
		if(listSpaj == null){
			List<Map> listSpajMall = uwManager.selectSpajMallBelumTrans();
			listSpaj = new ArrayList();
			
			for(Map m : listSpajMall){
				listSpaj.add(m);
			}
			session.setAttribute("listSpajMall", listSpaj);
		}
		return new ModelAndView("uw/spaj_mall_untransf");
	}
	
	
	public ModelAndView pembayaran_bunga(HttpServletRequest request, HttpServletResponse response) throws Exception{

//		Date now = this.elionsManager.selectSysdate();
		Date now = new Date();
		
		int mpb_flag_bs = ServletRequestUtils.getIntParameter(request, "mpb_flag_bs", -1);
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(now));
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(now));
		String lcb_no = ServletRequestUtils.getStringParameter(request, "lcb_no", "");
		
		Map map = new HashMap();
		map.put("mpb_flag_bs", mpb_flag_bs);
		map.put("startDate", startDate);//
		map.put("endDate", endDate);//
		map.put("lcb_no", lcb_no);//
		
		//map.put("piyu", request.getParameter("piyu"));
		
		List<DropDown> daftarRekAjs = new ArrayList<DropDown>();
		daftarRekAjs.add(new DropDown("-1", "ALL"));
		daftarRekAjs.add(new DropDown("1", "BELUM BAYAR"));
		daftarRekAjs.add(new DropDown("2", "SUDAH BAYAR"));

		List<DropDown> daftarCab = new ArrayList<DropDown>();
		daftarCab.add(0, new DropDown("", "ALL"));
		daftarCab.addAll(this.elionsManager.selectDropDown("EKA.LST_CABANG_BII", "LCB_NO", "NAMA_CABANG", "", "NAMA_CABANG", "JENIS=3"));
		
		List<ProSaveBayar> daftar = new ArrayList<ProSaveBayar>();
		daftar = elionsManager.selectMstProSave(mpb_flag_bs, lcb_no, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate));
		
		
		if(request.getParameter("recheck") != null) {
			String[] daftar_bayar_id = ServletRequestUtils.getStringParameters(request, "bayarid");
			int[] daftar_jenis = ServletRequestUtils.getIntParameters(request, "jenis");
			
			int counter = 0;
			String tampungbayar;
			int tampungjenis;
			for(int i=0; i<daftar_bayar_id.length; i++) {
				int pilih = ServletRequestUtils.getIntParameter(request, "ucup" + i, 0);
				counter += pilih;
				if(pilih == 1) {
					tampungbayar = daftar_bayar_id[i];
					tampungjenis = daftar_jenis[i];
					elionsManager.updateMstProSaveBayar(tampungbayar, tampungjenis);
					daftar.get(i).setFlag_check(pilih);
					daftar.get(i).setStatus("SUDAH BAYAR");
				}
				map.put("pilih"+i, pilih);
				map.put("check", pilih);
			}
			
			String flag = "1";
			map.put("flag", flag);
			//elionsManager.updateMstProSaveBayar(daftarUpdate, daftarUpdateJenis);
		}
		
		if(request.getParameter("cari")!=null){
//			String a = "tes";
//			logger.info(a);
			String flag = ServletRequestUtils.getStringParameter(request, "flag", "1");
			if(daftar.size()==0){
				flag = "0";
			}else flag ="1";
			map.put("flag", flag);
		}
		
		map.put("StatusBayar", daftarRekAjs);
		map.put("daftarCab", daftarCab);
		//map.put("daftarDrek", this.elionsManager.selectMstDrek(lsrek_id, lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal)));
		map.put("daftarDrek", daftar);
		map.put("ukuran", daftar.size());
		
		return new ModelAndView("uw/pembayaran_bunga", map);
	}
	
	public ModelAndView drek(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Date now = this.elionsManager.selectSysdate();
		Date now = new Date();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		int json = ServletRequestUtils.getIntParameter(request, "json", 0);
		
		//halaman depan
		if(json==0){
			int lsrek_id = ServletRequestUtils.getIntParameter(request, "lsrek_id", -1);
			String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(now));
			String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(now));
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String no_spaj = ServletRequestUtils.getStringParameter(request, "no_spaj", "");
			String no_trx = ServletRequestUtils.getStringParameter(request, "no_trx", "");
			String noSpaj = ServletRequestUtils.getStringParameter(request, "noSpaj", "");
			int lsbp_id = ServletRequestUtils.getIntParameter(request, "lsbp_id", 156);
			String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
			String startNominal = ServletRequestUtils.getStringParameter(request, "startNominal", "0");
			String endNominal = ServletRequestUtils.getStringParameter(request, "endNominal", "99999999999");
			String flagHalaman = ServletRequestUtils.getStringParameter(request, "flagHalaman", "");
			int flag =0;
			
			if(request.getParameter("recheck") != null && request.getParameter("edit") == null) {
				String[] daftar_trx 	= ServletRequestUtils.getStringParameters(request, "recheck");
				String[] daftar_edit 	= ServletRequestUtils.getStringParameters(request, "rubah");
				elionsManager.updateMstDrekRecheck(daftar_trx);
			}
		
			Map map = new HashMap();
			map.put("lsrek_id", lsrek_id);
			map.put("startDate", startDate);//
			map.put("endDate", endDate);//
			map.put("lsbp_id", lsbp_id);//
			map.put("kode", kode);
			map.put("startNominal", startNominal);
			map.put("endNominal", endNominal);
			
			if( noSpaj != null && !"".equals(noSpaj))
			{
				String regSpaj  = noSpaj;
				map.put("regSpaj", regSpaj);
				
				//MANTA
				Product prod = new Product();
				prod = uwManager.selectMstProductInsuredUtamaFromSpaj(noSpaj);
				Integer bisnisId = prod.getLsbs_id();
				Integer bisnisNo = prod.getLsdbs_number();
				if((lsrek_id == -1) && ((bisnisId == 142 && bisnisNo == 2) || (bisnisId == 158 && bisnisNo == 6) || (bisnisId == 188 && bisnisNo == 2))) {
					if(prod.getLku_id().equals("01")){
						lsrek_id = 161;
					}else if(prod.getLku_id().equals("02")){
						lsrek_id = 160;
					}
				} 
			}
			
			map.put("piyu", request.getParameter("piyu"));
			map.put("cupcup", request.getParameter("cupcup"));
			//map.put("flagValue1", request.getParameter("flagValue1"));
			
			List<DropDown> daftarRekAjs = new ArrayList<DropDown>();
			daftarRekAjs.add(new DropDown("-1", 	"ALL"));
		
			//Yusuf (5/4/2012) Start now, tampilkan dari query saja, lebih paten, biar gak ada yg ketinggalan2 karena harus hardcode
			//daftarRekAjs.addAll(uwManager.selectRekeningAjs(156));
			daftarRekAjs.addAll(uwManager.selectRekeningAjs(null,null));
	
			/*
			//Bank Sinarmas - Simas Prima
			daftarRekAjs.add(new DropDown("97", 	"[0000029025] BSM Mangga Dua RUPIAH", 		"[BSM] Simas Prima"));
			daftarRekAjs.add(new DropDown("98", 	"[0000029041] BSM Mangga Dua DOLLAR", 		"[BSM] Simas Prima"));
			daftarRekAjs.add(new DropDown("161", 	"[0000791814] BSM Wisma Eka Jiwa RUPIAH", 	"[BSM] Simas Prima"));
			daftarRekAjs.add(new DropDown("160", 	"[0000791822] BSM Wisma Eka Jiwa DOLLAR", 	"[BSM] Simas Prima"));
	
			//Bank Sinarmas - Simas Stabil Link
			daftarRekAjs.add(new DropDown("261", 	"[0001238299] BSM Wisma Eka Jiwa RUPIAH", 	"[BSM] Simas Stabil Link")); //DULU 169, PER APR 2010, RUBAH
			daftarRekAjs.add(new DropDown("266", 	"[0001238288] BSM Wisma Eka Jiwa DOLLAR", 	"[BSM] Simas Stabil Link")); //DULU 170, PER APR 2010, RUBAH
	
			//Sinarmas Sekuritas - Danamas Prima
			daftarRekAjs.add(new DropDown("214", 	"[0002595777] BSM Thamrin RUPIAH", 			"[SMS] Danamas Prima"));
			daftarRekAjs.add(new DropDown("216", 	"[0002596555] BSM Thamrin DOLLAR", 			"[SMS] Danamas Prima"));
	
			//Sinarmas Sekuritas - Stable Link SMS
			daftarRekAjs.add(new DropDown("223", 	"[0002184435] BSM Thamrin RUPIAH", 			"[SMS] Stable Link SMS"));
			daftarRekAjs.add(new DropDown("278", 	"[0005170443] BSM Thamrin DOLLAR", 			"[SMS] Stable Link SMS"));
	
			//Non-Link
			daftarRekAjs.add(new DropDown("152",	"[0000383414] Non-Link RUPIAH",				"Non-Link"));
			daftarRekAjs.add(new DropDown("98",		"[0000029041] Non-Link DOLLAR",				"Non-Link"));
			
			//Link
			daftarRekAjs.add(new DropDown("212",	"[0000383414] Link RUPIAH",					"Link"));
			daftarRekAjs.add(new DropDown("213",	"[0002118998] Link DOLLAR",					"Link"));
	
			//Stable Link Agency & Worksite
			daftarRekAjs.add(new DropDown("171",	"[0000383414] Stable Link RUPIAH",			"Stable Link (Agen, WS)"));
			daftarRekAjs.add(new DropDown("245",	"[0002118998] Stable Link DOLLAR",			"Stable Link (Agen, WS)"));
			*/
			
			List<DropDown> daftarCab = new ArrayList<DropDown>();
			daftarCab.add(0, new DropDown("", "ALL"));
			daftarCab.addAll(this.elionsManager.selectDropDown("EKA.CAB_BSM", "KODE", "NAMA_CAB  || ' (' || KODE || ')'", "", "NAMA_CAB", null));
			List<Drek> daftarDrek= this.elionsManager.selectMstDrek(lsrek_id, lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal));
			if(request.getParameter("edit") != null) {
				flag=1;
				for (int i=0; i<daftarDrek.size(); i++){
					String daftar_edit 	= ServletRequestUtils.getStringParameter(request, "rubah"+i);					
					if(daftar_edit!=null){
						String dftr_spaj = ServletRequestUtils.getStringParameter(request, "reg_spaj"+i);
						elionsManager.updateMstDrekEdit(daftar_edit, currentUser.getLus_id(), dftr_spaj);
					}
				}
			}
			if(request.getParameter("buang") != null) {
				for (int i=0; i<daftarDrek.size(); i++){
					String daftar_buang 	= ServletRequestUtils.getStringParameter(request, "rubah"+i);
					if(daftar_buang!=null){
						String dftr_spaj = ServletRequestUtils.getStringParameter(request, "reg_spaj"+i);
						if (dftr_spaj!= null)
							dftr_spaj=null;
						elionsManager.updateDrekKosongkanSpaj(daftar_buang, currentUser.getLus_id(), dftr_spaj);;
					}
				}
			}
			
			map.put("daftarRekAjs", daftarRekAjs);
			map.put("daftarBank", this.elionsManager.selectDropDown("EKA.LST_BANK_PUSAT", "LSBP_ID", "LSBP_NAMA", "", "LSBP_NAMA", null));
			map.put("daftarCab", daftarCab);
			map.put("daftarDrek", this.elionsManager.selectMstDrek(lsrek_id, lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal)));
			if(flagHalaman.equalsIgnoreCase("")){
				map.put("flagHalaman", request.getParameter("flagValue"));
			}else{
				map.put("flagHalaman", flagHalaman);
			}
			
			return new ModelAndView("uw/drek", map);
		}else if(json==1){
			String no_rek = ServletRequestUtils.getStringParameter(request, "no_rek", null);
    		List<DropDown> result = uwManager.selectRekeningAjs(null,no_rek);
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
		}
		return null;
	}

	public ModelAndView drek2(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Date now = this.elionsManager.selectSysdate();
		Date now = new Date();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		int lsrek_id = ServletRequestUtils.getIntParameter(request, "lsrek_id", -1);
		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(now));
		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(now));
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String no_spaj = ServletRequestUtils.getStringParameter(request, "no_spaj", "");
		String no_trx = ServletRequestUtils.getStringParameter(request, "no_trx", "");
		int lsbp_id = ServletRequestUtils.getIntParameter(request, "lsbp_id", 156);
		String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
		String startNominal = ServletRequestUtils.getStringParameter(request, "startNominal", "0");
		String endNominal = ServletRequestUtils.getStringParameter(request, "endNominal", "99999999999");
		int flag =0;

		Map map = new HashMap();
		map.put("lsrek_id", lsrek_id);
		map.put("startDate", startDate);//
		map.put("endDate", endDate);//
		map.put("lsbp_id", lsbp_id);//
		map.put("kode", kode);
		map.put("startNominal", startNominal);
		map.put("endNominal", endNominal);
		
		map.put("piyu", request.getParameter("piyu"));
		map.put("cupcup", request.getParameter("cupcup"));
		
		List<DropDown> daftarRekAjs = new ArrayList<DropDown>();
		daftarRekAjs.add(new DropDown("-1", 	"ALL"));
		
		//Yusuf (5/4/2012) Start now, tampilkan dari query saja, lebih paten, biar gak ada yg ketinggalan2 karena harus hardcode
		daftarRekAjs.addAll(uwManager.selectRekeningAjs(156,null));

		/*
		//Bank Sinarmas - Simas Prima
		daftarRekAjs.add(new DropDown("97", 	"[0000029025] BSM Mangga Dua RUPIAH", 		"[BSM] Simas Prima"));
		daftarRekAjs.add(new DropDown("98", 	"[0000029041] BSM Mangga Dua DOLLAR", 		"[BSM] Simas Prima"));
		daftarRekAjs.add(new DropDown("161", 	"[0000791814] BSM Wisma Eka Jiwa RUPIAH", 	"[BSM] Simas Prima"));
		daftarRekAjs.add(new DropDown("160", 	"[0000791822] BSM Wisma Eka Jiwa DOLLAR", 	"[BSM] Simas Prima"));

		//Bank Sinarmas - Simas Stabil Link
		daftarRekAjs.add(new DropDown("261", 	"[0001238299] BSM Wisma Eka Jiwa RUPIAH", 	"[BSM] Simas Stabil Link")); //DULU 169, PER APR 2010, RUBAH
		daftarRekAjs.add(new DropDown("266", 	"[0001238288] BSM Wisma Eka Jiwa DOLLAR", 	"[BSM] Simas Stabil Link")); //DULU 170, PER APR 2010, RUBAH
		
		//Sinarmas Sekuritas - Danamas Prima
		daftarRekAjs.add(new DropDown("214", 	"[0002595777] BSM Thamrin RUPIAH", 			"[SMS] Danamas Prima"));
		daftarRekAjs.add(new DropDown("216", 	"[0002596555] BSM Thamrin DOLLAR", 			"[SMS] Danamas Prima"));

		//Sinarmas Sekuritas - Stable Link SMS
		daftarRekAjs.add(new DropDown("223", 	"[0002184435] BSM Thamrin RUPIAH", 			"[SMS] Stable Link SMS"));
		daftarRekAjs.add(new DropDown("278", 	"[0005170443] BSM Thamrin DOLLAR", 			"[SMS] Stable Link SMS"));

		//Non-Link
		daftarRekAjs.add(new DropDown("152",	"[0000383414] Non-Link RUPIAH",				"Non-Link"));
		daftarRekAjs.add(new DropDown("98",		"[0000029041] Non-Link DOLLAR",				"Non-Link"));
		
		//Link
		daftarRekAjs.add(new DropDown("212",	"[0000383414] Link RUPIAH",					"Link"));
		daftarRekAjs.add(new DropDown("213",	"[0002118998] Link DOLLAR",					"Link"));

		//Stable Link Agency & Worksite
		daftarRekAjs.add(new DropDown("171",	"[0000383414] Stable Link RUPIAH",			"Stable Link (Agen, WS)"));
		daftarRekAjs.add(new DropDown("245",	"[0002118998] Stable Link DOLLAR",			"Stable Link (Agen, WS)"));
		*/
		
		List<DropDown> daftarCab = new ArrayList<DropDown>();
		daftarCab.add(0, new DropDown("", "ALL"));
		daftarCab.addAll(this.elionsManager.selectDropDown("EKA.CAB_BSM", "KODE", "NAMA_CAB  || ' (' || KODE || ')'", "", "NAMA_CAB", null));
		List<Drek> daftarDrek= this.elionsManager.selectMstDrek(lsrek_id, lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal));
		
		map.put("daftarRekAjs", daftarRekAjs);
		map.put("daftarBank", this.elionsManager.selectDropDown("EKA.LST_BANK_PUSAT", "LSBP_ID", "LSBP_NAMA", "", "LSBP_NAMA", null));
		map.put("daftarCab", daftarCab);
		map.put("daftarDrek", daftarDrek);
		
		
		return new ModelAndView("uw/drek2", map);
	}

	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String snow_spaj = ServletRequestUtils.getStringParameter(request, "snow_spaj", "");
		Integer i_posisi = ServletRequestUtils.getIntParameter(request, "posisi_uw", 0);
		map.put("snow_spaj", snow_spaj);
		
		if(i_posisi==27)map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("27", 1,null,null));
		else if(i_posisi==209)map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("209", 1,null,null));
		else if(currentUser.getLde_id().equals("07")) map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("2", 2,null,null));
		else if(currentUser.getLde_id().equals("01") || currentUser.getName().equals("INGRID") || currentUser.getName().equals("RACHEL") || currentUser.getName().equals("ARIANI")) map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("2", 3,null,null));
		else map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("2", 1,null,null));
		
		map.put("posisi_uw", i_posisi);
		
		return new ModelAndView("uw/uw", map);
	}
	
	public ModelAndView collection(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		List spajspeedy= this.uwManager.selectDaftarSPAJ("27", 1,null,null);
		List spajUw=this.uwManager.selectDaftarSPAJ("2", 1,null,null);
		spajspeedy.addAll(spajUw);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String snow_spaj = ServletRequestUtils.getStringParameter(request, "snow_spaj", "");
		Integer i_speedy = ServletRequestUtils.getIntParameter(request, "speedy", 0);
		map.put("snow_spaj", snow_spaj);
		
		if(i_speedy==1)map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("27", 1,null,null));
		else if(currentUser.getLde_id().equals("07")) map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("2", 2,null,null));
		else map.put("daftarSPAJ", spajspeedy);
		map.put("speedy", i_speedy);
		
		return new ModelAndView("uw/collection", map);
	}
	
	public ModelAndView viewsimultan(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("2", 1,null,null));
		return new ModelAndView("uw/viewsimultan", map);
	}
	
	public ModelAndView payment(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String jumlah = request.getParameter("jumlah");
		String simbol = request.getParameter("simbol");
		String no_trx = request.getParameter("no_trx");
		String noSpaj = request.getParameter("noSpaj");
		String norek_ajs = request.getParameter("norek_ajs");
		String premiTerpakai = request.getParameter("premiTerpakai");
		String piyu = request.getParameter("piyu");
		Map map = new HashMap();
		map.put("jumlah", jumlah);
		map.put("simbol", simbol);
		map.put("no_trx", no_trx);
		map.put("noSpaj", noSpaj);
		map.put("norek_ajs", norek_ajs);
		map.put("premiTerpakai", premiTerpakai);
		map.put("piyu", piyu);
		return new ModelAndView("uw/payment_frame", map);
	}
	
	public ModelAndView list_komisi(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		String dist = ServletRequestUtils.getStringParameter(request, "dist", "");
		String prod = ServletRequestUtils.getStringParameter(request, "prod", "");
		map.put("dist", dist);
		map.put("prod", prod);
		
		List<DropDown> daftarDist = new ArrayList<DropDown>();
		daftarDist.add(new DropDown("", "JALUR DISTRIBUSI >>"));
		daftarDist.add(new DropDown("1", "Regional"));
		daftarDist.add(new DropDown("3", "Agency"));
		daftarDist.add(new DropDown("4", "Hybrid"));
		map.put("daftarDist", daftarDist);
		
		List<DropDown> daftarProduk = uwManager.selectDaftarProdukKomisi(dist);
		daftarProduk.add(0, new DropDown("", "NAMA PRODUK >>"));
		map.put("daftarProduk", daftarProduk);
		
		if(request.getParameter("show") != null) {
			
			List<Map> daftarKomisi = uwManager.selectDaftarKomisi(dist, prod);
			for(Map m : daftarKomisi) {
				Upp upp = new Upp();
				
				if(dist.equals("1")) 		upp.lca_id = "01";
				else if(dist.equals("3")) 	upp.lca_id = "37";
				if(dist.equals("4")) 		upp.lca_id = "46";
				
				upp.mspro_jn_prod 	= 1;
				upp.lsgb_id 		= ((BigDecimal) m.get("LSGB_ID")).intValue();
				upp.lsbs_id 		= ((BigDecimal) m.get("LSBS_ID")).intValue();
				upp.lsdbs_number 	= ((BigDecimal) m.get("LSDBS_NUMBER")).intValue();
				upp.lscb_id 		= ((BigDecimal) m.get("LSCB_ID")).intValue();
				upp.msbi_tahun_ke 	= ((BigDecimal) m.get("LSCO_YEAR")).intValue();
				upp.lsbs_jenis 		= ((BigDecimal) m.get("LSBS_JENIS")).intValue();
//				upp.mspro_prod_date = elionsManager.selectSysdate();
				upp.mspro_prod_date = new Date();
				upp.premi_pokok 	= (double) 1;
				upp.mspro_nilai_kurs = (double) 1;
				upp.premi_rider 	= (double) 0;
				if(upp.msbi_tahun_ke.intValue() == 0) {
					upp.msbi_premi_ke 	= 2;
					upp.flag_topup 		= 1;
				}else {
					upp.msbi_premi_ke 	= 1;
					upp.flag_topup 		= 0;
				}
				
				if(upp.lsbs_jenis.intValue() == 1) {
					map.put("IS_POWERSAVE", 1);
					upp.mgi = 1;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_1", 		upp.upp_eva * 100);					
					m.put("UPP_KONTES_1", 	upp.upp_kontes * 100);
					upp.mgi = 3;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_3", 		upp.upp_eva * 100);					
					m.put("UPP_KONTES_3", 	upp.upp_kontes * 100);
					upp.mgi = 6;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_6", 		upp.upp_eva * 100);					
					m.put("UPP_KONTES_6", 	upp.upp_kontes * 100);
					upp.mgi = 9;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_9", 		upp.upp_eva * 100);					
					m.put("UPP_KONTES_9", 	upp.upp_kontes * 100);
					upp.mgi = 12;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_12", 	upp.upp_eva * 100);					
					m.put("UPP_KONTES_12", 	upp.upp_kontes * 100);
					upp.mgi = 24;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_24", 	upp.upp_eva * 100);					
					m.put("UPP_KONTES_24", 	upp.upp_kontes * 100);
					upp.mgi = 36;
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA_36", 	upp.upp_eva * 100);					
					m.put("UPP_KONTES_36", 	upp.upp_kontes * 100);

				}else {
					map.put("IS_POWERSAVE", 0);
					elionsManager.prosesPerhitunganUpp(upp);
					m.put("UPP_EVA", 		upp.upp_eva * 100);					
					m.put("UPP_KONTES", 	upp.upp_kontes * 100);
				}
				
			}
			map.put("daftarKomisi", daftarKomisi);
			map.put("ukuran", daftarKomisi.size());
		}
		
		return new ModelAndView("uw/list_komisi", map);
	}

	// ini cara lama penarikan UPP eva dan kontes, yaitu dari hardcoding, cara baru pake tabel baru
//	public ModelAndView list_komisi(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Map map = new HashMap();
//		String dist = ServletRequestUtils.getStringParameter(request, "dist", "");
//		String prod = ServletRequestUtils.getStringParameter(request, "prod", "");
//		map.put("dist", dist);
//		map.put("prod", prod);
//		
//		List<DropDown> daftarDist = new ArrayList<DropDown>();
//		daftarDist.add(new DropDown("", "JALUR DISTRIBUSI >>"));
//		daftarDist.add(new DropDown("1", "Regional"));
//		daftarDist.add(new DropDown("3", "Agency"));
//		daftarDist.add(new DropDown("4", "Hybrid"));
//		map.put("daftarDist", daftarDist);
//		
//		List<DropDown> daftarProduk = elionsManager.selectDaftarProdukKomisi(dist);
//		daftarProduk.add(0, new DropDown("", "NAMA PRODUK >>"));
//		map.put("daftarProduk", daftarProduk);
//		
//		if(request.getParameter("show") != null) {
//			List<Map> daftarKomisi = elionsManager.selectDaftarKomisi(dist, prod);
//			JasperFunctionReport jf = new JasperFunctionReport();
//			for(Map m : daftarKomisi) {
//				if(dist.equals("1")) jf.cabang = "01";
//				else if(dist.equals("3")) jf.cabang = "37";
//				if(dist.equals("4")) jf.cabang = "46";
//				
//				int lsgb_id = ((BigDecimal) m.get("LSGB_ID")).intValue();
//				int lsbs_id = ((BigDecimal) m.get("LSBS_ID")).intValue();
//				int lsdbs_number = ((BigDecimal) m.get("LSDBS_NUMBER")).intValue();
//				int lscb_id = ((BigDecimal) m.get("LSCB_ID")).intValue();
//				int lsco_year = ((BigDecimal) m.get("LSCO_YEAR")).intValue();
//				int lsbs_jenis = ((BigDecimal) m.get("LSBS_JENIS")).intValue();
//				
//				if(products.powerSave(String.valueOf(lsbs_id))) {
//					map.put("IS_POWERSAVE", 1);
//					jf.li_jamin = 1;
//					double upp_eva_1 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 3;
//					double upp_eva_3 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 6;
//					double upp_eva_6 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 9;
//					double upp_eva_9 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 12;
//					double upp_eva_12 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 24;
//					double upp_eva_24 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					jf.li_jamin = 36;
//					double upp_eva_36 = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					double koef_upp_kontes = jf.f_koef_upp_kontes(lsgb_id, lsbs_id, lsdbs_number, lscb_id, jf.cabang, lsbs_jenis, (lsco_year==0 ? 1 : 0));
//					m.put("UPP_EVA_1", upp_eva_1);					
//					m.put("UPP_EVA_3", upp_eva_3);					
//					m.put("UPP_EVA_6", upp_eva_6);					
//					m.put("UPP_EVA_9", upp_eva_9);					
//					m.put("UPP_EVA_12", upp_eva_12);					
//					m.put("UPP_EVA_24", upp_eva_24);					
//					m.put("UPP_EVA_36", upp_eva_36);					
//					m.put("UPP_KONTES_1", koef_upp_kontes * upp_eva_1);
//					m.put("UPP_KONTES_3", koef_upp_kontes * upp_eva_3);
//					m.put("UPP_KONTES_6", koef_upp_kontes * upp_eva_6);
//					m.put("UPP_KONTES_9", koef_upp_kontes * upp_eva_9);
//					m.put("UPP_KONTES_12", koef_upp_kontes * upp_eva_12);
//					m.put("UPP_KONTES_24", koef_upp_kontes * upp_eva_24);
//					m.put("UPP_KONTES_36", koef_upp_kontes * upp_eva_36);
//				}else {
//					map.put("IS_POWERSAVE", 0);
//					double upp_eva = jf.f_koef_upp_evaluasi(lsgb_id, lsbs_id, lsdbs_number, lscb_id, 1, 1, lsco_year, null, 1, elionsManager) * 100;
//					double koef_upp_kontes = jf.f_koef_upp_kontes(lsgb_id, lsbs_id, lsdbs_number, lscb_id, jf.cabang, lsbs_jenis, (lsco_year==0 ? 1 : 0));
//					m.put("UPP_EVA", upp_eva);					
//					m.put("UPP_KONTES", koef_upp_kontes * upp_eva);
//				}
//				
//			}
//			map.put("daftarKomisi", daftarKomisi);
//			map.put("ukuran", daftarKomisi.size());
//		}
//		
//		return new ModelAndView("uw/list_komisi", map);
//	}
	
	public ModelAndView kyc_proses(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();	
		NewBusinessCase newBus=new NewBusinessCase();
		Integer info;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj");
		String proses=ServletRequestUtils.getStringParameter(request, "proses","");
		String proses2=request.getParameter("proses2");
		info=0;
		Map data= elionsManager.selectKYC(spaj);
		Integer flag=0;
		flag=(Integer)data.get("LSSP_ID");
//		Integer insured= (Integer)data.get("mspo_policy_no_format");
		String policy=(String)data.get("mspo_policy_holder");
		String lusId= currentUser.getLus_id();
		Date mste_kyc_date;
		
		if(proses2==null){//tidak
			if ( (policy.substring(0,2).equalsIgnoreCase("XX")) || (policy.substring(0,2).equalsIgnoreCase("WW")) ){
				info=2;
				proses="1";
				mste_kyc_date=null;
			}else{
//				mste_kyc_date= elionsManager.selectSysdate();
				mste_kyc_date= new Date();
				elionsManager.updateProsesKyc2(spaj, 1, lusId, Integer.parseInt(proses),mste_kyc_date);
			}
		}else{//ya plus keterangan
			if ( (policy.substring(0,2).equalsIgnoreCase("XX")) || (policy.substring(0,2).equalsIgnoreCase("WW")) ){
				info=2;
				proses="1";
				mste_kyc_date=null;
			}else{
				proses="3";
//				mste_kyc_date= elionsManager.selectSysdate();
				mste_kyc_date= new Date();
				elionsManager.updateProsesKyc2(spaj, 1, lusId, 1,mste_kyc_date);
				String desc=ServletRequestUtils.getStringParameter(request, "keterangan");
				elionsManager.updateProsesKycResultKyc(spaj, 1, lusId, desc,mste_kyc_date);
			}
			
		}
		
		map.put("info", info);
		map.put("spaj", spaj);
		map.put("proses", proses);
		return new ModelAndView("uw/kyc_proses", map);
		
		
//		if (request.getParameter("tdk")!=null){
//			elionsManager.updateProsesKyc2(spaj, 1, lusId, 0,mste_kyc_date);
//		}
//		if (request.getParameter("ya")!=null){		
//			elionsManager.updateProsesKyc2(spaj, 1, lusId, 1,mste_kyc_date);	
//		}
//			
//		if (request.getParameter("save")!=null){
//			String desc=ServletRequestUtils.getStringParameter(request, "keterangan");
//			elionsManager.updateProsesKycResultKyc(spaj, 1, lusId, desc,mste_kyc_date);
//		}
	}
	
	public ModelAndView akseptasi_khusus(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		
        User currentUser = (User) request.getSession().getAttribute("currentUser");

        //hanya IT dan UW
        if(currentUser.getLde_id().equals("01") || currentUser.getLde_id().equals("11")){
            double angka = (double) currentUser.getScreenWidth() / (double) currentUser.getScreenHeight();
            if(angka > 1.4) {
            	map.put("wideScreen", true);
            }
        }
//        Date now = elionsManager.selectSysdate();
        Date now = new Date();
        map.put("startDate", defaultDateFormat.format(now));
		map.put("endDate", defaultDateFormat.format(now));
		map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("1,2,6,7,8,99", 1,10,null));
		return new ModelAndView("uw/akseptasi_khusus", map);
	}
	
	public ModelAndView update_akseptasi(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj");
		String desc=ServletRequestUtils.getStringParameter(request, "desc");
		Integer lsspId,lspdId;
		boolean ceklis= elionsManager.selectValidasiCheckListBySpaj(spaj);
		String lca_id = elionsManager.selectLcaIdBySpaj(spaj);
		List<String> error = new ArrayList<String>();
		Map mapTt=elionsManager.selectTertanggung(spaj);
		lsspId=(Integer)mapTt.get("LSSP_ID");
		lspdId=(Integer)mapTt.get("LSPD_ID");
		if(ceklis==false&&lca_id.equals("09")){
			error.add("Harap Input CHECKLIST DOKUMEN POLIS Terlebih Dahulu!");
			map.put("error","Harap Input CHECKLIST DOKUMEN POLIS Terlebih Dahulu!");
		}else{
			elionsManager.prosesUpdateAkseptasiKhusus(spaj, lspdId, 5, 1,currentUser.getLus_id(),lsspId,desc);
			map.put("msg","Berhasil Update SPAJ ("+spaj+").Status Menjadi ACCEPTED");
			map.put("lspd_id", lspdId);
			map.put("spaj", spaj);
		
		}
		// Rahmayanti, Ridhaal
		// Send Email to Rizky Amaliah (sebelumnya Riyadi -resign) untuk pemberitahuan update status 
		if(lca_id.equals("37")||lca_id.equals("39")||lca_id.equals("49")||lca_id.equals("52")||lca_id.equals("60")){
			try{
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, "ajsjava@sinarmasmsiglife.co.id", 
					new String[]{"rizky_a@sinarmasmsiglife.co.id"}, 
//					new String[]{"rahmayanti@sinarmasmsiglife.co.id"}, 
					new String[]{"timmy@sinarmasmsiglife.co.id","fajar@sinarmasmsiglife.co.id"},
					null, 
					"ADA UPDATE STATUS "+spaj+" Jalur Distribusi : Agency",
					"Telah dilakukan Update Status untuk SPAJ No."+spaj+", Silakan dilakukan proses transfer ke tanda terima / filling", 
					null, spaj);
			}catch (Exception e) {
				logger.error("ERROR :", e);
				throw e;
			}
		}
		return new ModelAndView("uw/update_akseptasi", map);
	}
	
	public ModelAndView report_akseptasi_khusus(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		String to=ServletRequestUtils.getStringParameter(request, "to");
		String cc=ServletRequestUtils.getStringParameter(request, "cc");
		
		List lsCabang=new ArrayList();
		List lsAdmin=new ArrayList();
			lsCabang=elionsManager.selectlstCabangForAkseptasiKhusus();
			DropDown cabang=new DropDown();
			cabang.setKey("0");
			cabang.setValue("ALL");
			lsCabang.add(0, cabang);
		List lsCabangToday=elionsManager.selectlstCabangForAkseptasiKhususToday();	
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());
		Integer flag=ServletRequestUtils.getIntParameter(request, "l");
		
		if(flag==null)
			lus_id=currentUser.getLus_id();
		param.put("lsCabang", lsCabang);
		if(lsCabangToday.isEmpty())
			param.put("flagCabang",1);
		else
			param.put("flagCabang",0);
		param.put("lsCabangToday",lsCabangToday);
		param.put("lca_id",lca_id);
		param.put("to",to);
		param.put("cc",cc);
		param.put("lus_id",lus_id);
//		param.put("tgl", elionsManager.selectSysdate());
		param.put("tgl", new Date());
//		request.getSession().setAttribute("report", report);
//		return prepareReport(request, response, report);
		return new ModelAndView("uw/report_akseptasi_khusus", param);
	}
	
	public ModelAndView report_polis_expired(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		String to=ServletRequestUtils.getStringParameter(request, "to");
		String cc=ServletRequestUtils.getStringParameter(request, "cc");
		
		List lsCabang=new ArrayList();
		List lsAdmin=new ArrayList();
			lsCabang=elionsManager.selectlstCabangForAkseptasiKhusus();
			DropDown cabang=new DropDown();
			cabang.setKey("0");
			cabang.setValue("ALL");
			lsCabang.add(0, cabang);
		List lsCabangToday=elionsManager.selectlstCabangForAkseptasiKhususToday();	
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());
		Integer flag=ServletRequestUtils.getIntParameter(request, "l");
		
		if(flag==null)
			lus_id=currentUser.getLus_id();
		param.put("lsCabang", lsCabang);
		if(lsCabangToday.isEmpty())
			param.put("flagCabang",1);
		else
			param.put("flagCabang",0);
		param.put("lsCabangToday",lsCabangToday);
		param.put("lca_id",lca_id);
		param.put("to",to);
		param.put("cc",cc);
		param.put("lus_id",lus_id);
//		param.put("tgl", elionsManager.selectSysdate());
		param.put("tgl", new Date());
//		request.getSession().setAttribute("report", report);
//		return prepareReport(request, response, report);
		return new ModelAndView("uw/report_polis_expired", param);
	}
	
//	public ModelAndView reportFurtherRequirement(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Map param=new HashMap();
//		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
//		List lsCabang=new ArrayList();
//		List lsAdmin=new ArrayList();
//			lsCabang=elionsManager.selectlstCabangForAkseptasiKhusus();
//			DropDown cabang=new DropDown();
//			cabang.setKey("0");
//			cabang.setValue("ALL");
//			lsCabang.add(0, cabang);
//		List lsCabangToday=elionsManager.selectlstCabangForAkseptasiKhususToday();	
//		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());
//		Integer flag=ServletRequestUtils.getIntParameter(request, "l");
//		if(flag==null)
//			lus_id=currentUser.getLus_id();
//		param.put("lsCabang", lsCabang);
//		if(lsCabangToday.isEmpty())
//			param.put("flagCabang",1);
//		else
//			param.put("flagCabang",0);
//		param.put("lsCabangToday",lsCabangToday);
//		param.put("lca_id",lca_id);
//		param.put("lus_id",lus_id);
//		param.put("tgl", elionsManager.selectSysdate());
//		return new ModelAndView("report/reportFurtherRequirement", param);
//	}
	public ModelAndView main_update_nasabah(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ(null, 1,null,null));
		return new ModelAndView("uw/main_update_nasabah", map);
	}
	
	public ModelAndView MainCetakPolisCab(HttpServletRequest request, HttpServletResponse response) throws Exception{
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Map map = new HashMap();
		String lap=ServletRequestUtils.getStringParameter(request, "All");
		return new ModelAndView("uw/MainCetakPolisCab", map);
	}
	
	public ModelAndView cetakPolisCabang(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());	

				lsRegion=elionsManager.selectAllLstCab();
				Map cabang=new HashMap();
				lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
				lsRegion.add(0, cabang);
		
				if (All !=null){
					if(All.equals("1")){ // all user 
						cabang=new HashMap();
						cabang.put("KEY",currentUser.getLca_id());
						cabang.put("VALUE",currentUser.getCabang());
						lsRegion.add(cabang);
						lsAdmin=elionsManager.selectAllUserInCabang(lca_id); //all
					}else if (All.equals("2")){ //Regional User
						lsAdmin=elionsManager.selectAllUserRegional(lca_id); //reginonal
					}else if (All.equals("3")){ //agency
						lsAdmin=elionsManager.selectAllUserAgency(); //agency
					}else if (All.equals("4")){ //worksite
						lsAdmin=elionsManager.selectAllUserWorksite(lca_id); //worksite
					}
				}else{
					cabang=new HashMap();
					cabang.put("KEY",currentUser.getLca_id());
					cabang.put("VALUE",currentUser.getCabang());
					lsRegion.add(cabang);

				}
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		map.put("lock",lock);
		map.put("lsRegion", lsRegion);
		map.put("lca_id",lca_id);
		map.put("lus_id",lus_id);
		map.put("lsAgency", lsAgency);
		map.put("All", All);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/cetakPolisCabang", map);
	}

	public ModelAndView extraPremi(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());	

		lsRegion=elionsManager.selectAllLstCab();
		Map cabang=new HashMap();
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		lsRegion.add(0, cabang);
		
		if (All !=null){
			if(All.equals("1")){ // all user 
				cabang=new HashMap();
				cabang.put("KEY",currentUser.getLca_id());
				cabang.put("VALUE",currentUser.getCabang());
				lsRegion.add(cabang);
				lsAdmin=elionsManager.selectAllUserInCabang(lca_id); //all
			}else{
				cabang=new HashMap();
				cabang.put("KEY",currentUser.getLca_id());
				cabang.put("VALUE",currentUser.getCabang());
				lsRegion.add(cabang);

			}
		}
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		map.put("lock",lock);
		map.put("lsRegion", lsRegion);
		map.put("lca_id",lca_id);
		map.put("lus_id",lus_id);
		map.put("lsAgency", lsAgency);
		map.put("All", All);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/extraPremi", map);
	}
	
	public ModelAndView cetakPolisCabangBas(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());	

				lsRegion=elionsManager.selectAllLstCab();
				Map cabang=new HashMap();
				lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
				lsRegion.add(0, cabang);
		
				Map admin=new HashMap();
	    		admin.put("KEY", currentUser.getLus_id());
	    		admin.put("VALUE", currentUser.getName());
	    		lsAdmin.add(admin);
				
				if (All !=null){
					if(All.equals("1")){ // all user 
						cabang=new HashMap();
						cabang.put("KEY",currentUser.getLca_id());
						cabang.put("VALUE",currentUser.getCabang());
						lsRegion.add(cabang);
						lsAdmin=elionsManager.selectAllUserInCabang(lca_id); //all
					}else if (All.equals("2")){ //Regional User
						lsAdmin=elionsManager.selectAllUserRegional(lca_id); //reginonal
					}else if (All.equals("3")){ //agency
						lsAdmin=elionsManager.selectAllUserAgency(); //agency
					}else if (All.equals("4")){ //worksite
						lsAdmin=elionsManager.selectAllUserWorksite(lca_id); //worksite
					}
				}else{
					cabang=new HashMap();
					cabang.put("KEY",currentUser.getLca_id());
					cabang.put("VALUE",currentUser.getCabang());
					lsRegion.add(cabang);

				}
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		map.put("lock",lock);
		map.put("lsRegion", lsRegion);
		map.put("lca_id",lca_id);
		map.put("lus_id",lus_id);
		map.put("lsAgency", lsAgency);
		map.put("All", All);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		map.put("lsAdmin", lsAdmin);
		return new ModelAndView("uw/cetakPolisCabangBas", map);
	}

	public ModelAndView reportPremiNonCash(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		

		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/reportPremiNonCash", map);
	}
	
	public ModelAndView reportBlackList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		

		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/reportBlackList", map);
	}
	
	public ModelAndView report_pas(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String liniBisnis=ServletRequestUtils.getStringParameter(request, "liniBisnis");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		

		map.put("liniBisnis",liniBisnis);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/report_pas", map);
	}
	
	public ModelAndView report_pas_bp(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		

		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/report_pas_bp", map);
	}
	
	public ModelAndView report_dbd_bp(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		

		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/report_dbd_bp", map);
	}
	
	public ModelAndView report_dbd_agency(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		

		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/report_dbd_agency", map);
	}
	
	public ModelAndView reportRekonsiliasi(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		

		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/reportRekonsiliasi", map);
	}
	public ModelAndView reportTtpBlmDikembalikan(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		

		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/reportTtpBlmDikembalikan", map);
	}
	
	public ModelAndView ttp_blm_dikembalikan_cab(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
		
//		Date sysdate=elionsManager.selectSysdate();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
//		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());	

				lsRegion=elionsManager.selectAllLstCab();
				Map cabang=new HashMap();
				lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
				lsRegion.add(0, cabang);
		
				if (All !=null){
					if(All.equals("1")){ // all user 
						cabang=new HashMap();
						cabang.put("KEY",currentUser.getLca_id());
						cabang.put("VALUE",currentUser.getCabang());
						lsRegion.add(cabang);
						lsAdmin=elionsManager.selectAllUserInCabang(lca_id); //all
					}else if (All.equals("2")){ //Regional User
						lsAdmin=elionsManager.selectAllUserRegional(lca_id); //reginonal
					}else if (All.equals("3")){ //agency
						lsAdmin=elionsManager.selectAllUserAgency(); //agency
					}else if (All.equals("4")){ //worksite
						lsAdmin=elionsManager.selectAllUserWorksite(lca_id); //worksite
					}
				}else{
					cabang=new HashMap();
					cabang.put("KEY",currentUser.getLca_id());
					cabang.put("VALUE",currentUser.getCabang());
					lsRegion.add(cabang);

				}
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		map.put("lock",lock);
		map.put("lsRegion", lsRegion);
		map.put("lca_id",lca_id);
		map.put("lus_id",lus_id);
		map.put("lsAgency", lsAgency);
		map.put("All", All);
//		map.put("tglAwal",tglAwal);
//		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/ttp_blm_dikembalikan_cab", map);
	}
	public ModelAndView reportDataNasabah(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String jenisReport=ServletRequestUtils.getStringParameter(request, "jenisReport","tanggal_input");
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		map.put("jenisReport", jenisReport);
		return new ModelAndView("uw/reportDataNasabah", map);
	}
	
	public ModelAndView percentageQuestionnaire(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String jenisReport=ServletRequestUtils.getStringParameter(request, "jenisReport","tanggal_input");
		Integer countMstKuesionerPelayanan = uwManager.selectCountMstKuesionerPelayanan();
//		String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(elionsManager.selectSysdateSimple());
		String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		map.put("jmlInput",countMstKuesionerPelayanan);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		map.put("jenisReport", jenisReport);
		map.put("startDate", nowDate );
		map.put("endDate", nowDate );
		
		return new ModelAndView("uw/percentageQuestionnaire", map);
	}
	
	public ModelAndView percentageQuestionnaireMultiBarChart(HttpServletRequest request, HttpServletResponse response) throws Exception{
		byte[] captchaChallengeAsJpeg = null;
		Map map = new HashMap();
		int nilai=10;
		
		String jenisReport = request.getParameter("jenisReport");
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Integer width = 0;
		Integer height = 0;
		if( "1".equals( jenisReport ) ){
			KuesionerPelayananByQuestion percentageQuestionnaireByQuestion = uwManager.selectPercentageQuestionnaireByQuestion();
			
			dataset.setValue(percentageQuestionnaireByQuestion.getSikap1(), "Sangat Baik", "Sikap/Keramahan");
			dataset.setValue(percentageQuestionnaireByQuestion.getSikap2(), "Baik", "Sikap/Keramahan");
			dataset.setValue(percentageQuestionnaireByQuestion.getSikap3(), "Kurang Baik", "Sikap/Keramahan");
			dataset.setValue(percentageQuestionnaireByQuestion.getSikap4(), "Buruk", "Sikap/Keramahan");
			dataset.setValue(percentageQuestionnaireByQuestion.getSikap5(), "Buruk Sekali", "Sikap/Keramahan");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getInisiatif1(), "Sangat Baik", "Inisiatif/kesigapan membantu");
			dataset.setValue(percentageQuestionnaireByQuestion.getInisiatif2(), "Baik", "Inisiatif/kesigapan membantu");
			dataset.setValue(percentageQuestionnaireByQuestion.getInisiatif3(), "Kurang Baik", "Inisiatif/kesigapan membantu");
			dataset.setValue(percentageQuestionnaireByQuestion.getInisiatif4(), "Buruk", "Inisiatif/kesigapan membantu");
			dataset.setValue(percentageQuestionnaireByQuestion.getInisiatif5(), "Buruk Sekali", "Inisiatif/kesigapan membantu");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getKejelasan_dlm_info1(), "Sangat Baik", "Kejelasan dalam memberikan informasi");
			dataset.setValue(percentageQuestionnaireByQuestion.getKejelasan_dlm_info2(), "Baik", "Kejelasan dalam memberikan informasi");
			dataset.setValue(percentageQuestionnaireByQuestion.getKejelasan_dlm_info3(), "Kurang Baik", "Kejelasan dalam memberikan informasi");
			dataset.setValue(percentageQuestionnaireByQuestion.getKejelasan_dlm_info4(), "Buruk", "Kejelasan dalam memberikan informasi");
			dataset.setValue(percentageQuestionnaireByQuestion.getKejelasan_dlm_info5(), "Buruk Sekali", "Kejelasan dalam memberikan informasi");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getSolusi1(), "Sangat Baik", "Solusi atas masalah yang dikemukan");
			dataset.setValue(percentageQuestionnaireByQuestion.getSolusi2(), "Baik", "Solusi atas masalah yang dikemukan");
			dataset.setValue(percentageQuestionnaireByQuestion.getSolusi3(), "Kurang Baik", "Solusi atas masalah yang dikemukan");
			dataset.setValue(percentageQuestionnaireByQuestion.getSolusi4(), "Buruk", "Solusi atas masalah yang dikemukan");
			dataset.setValue(percentageQuestionnaireByQuestion.getSolusi5(), "Buruk Sekali", "Solusi atas masalah yang dikemukan");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getPenguasaan_produk1(), "Sangat Baik", "Penguasaan produk yang ditanyakan");
			dataset.setValue(percentageQuestionnaireByQuestion.getPenguasaan_produk2(), "Baik", "Penguasaan produk yang ditanyakan");
			dataset.setValue(percentageQuestionnaireByQuestion.getPenguasaan_produk3(), "Kurang Baik", "Penguasaan produk yang ditanyakan");
			dataset.setValue(percentageQuestionnaireByQuestion.getPenguasaan_produk4(), "Buruk", "Penguasaan produk yang ditanyakan");
			dataset.setValue(percentageQuestionnaireByQuestion.getPenguasaan_produk5(), "Buruk Sekali", "Penguasaan produk yang ditanyakan");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getKetepatan_waktu1(), "Sangat Baik", "Ketepatan waktu/cepat dalam pelayanan");
			dataset.setValue(percentageQuestionnaireByQuestion.getKetepatan_waktu2(), "Baik", "Ketepatan waktu/cepat dalam pelayanan");
			dataset.setValue(percentageQuestionnaireByQuestion.getKetepatan_waktu3(), "Kurang Baik", "Ketepatan waktu/cepat dalam pelayanan");
			dataset.setValue(percentageQuestionnaireByQuestion.getKetepatan_waktu4(), "Buruk", "Ketepatan waktu/cepat dalam pelayanan");
			dataset.setValue(percentageQuestionnaireByQuestion.getKetepatan_waktu5(), "Buruk Sekali", "Ketepatan waktu/cepat dalam pelayanan");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getManfaat_produk1(), "Sangat Baik", "Manfaat Produk");
			dataset.setValue(percentageQuestionnaireByQuestion.getManfaat_produk2(), "Baik", "Manfaat Produk");
			dataset.setValue(percentageQuestionnaireByQuestion.getManfaat_produk3(), "Kurang Baik", "Manfaat Produk");
			dataset.setValue(percentageQuestionnaireByQuestion.getManfaat_produk4(), "Buruk", "Manfaat Produk");
			dataset.setValue(percentageQuestionnaireByQuestion.getManfaat_produk5(), "Buruk Sekali", "Manfaat Produk");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getPemahaman_resiko1(), "Sangat Baik", "Pemahaman atas resiko & spesifikasi produk");
			dataset.setValue(percentageQuestionnaireByQuestion.getPemahaman_resiko2(), "Baik", "Pemahaman atas resiko & spesifikasi produk");
			dataset.setValue(percentageQuestionnaireByQuestion.getPemahaman_resiko3(), "Kurang Baik", "Pemahaman atas resiko & spesifikasi produk");
			dataset.setValue(percentageQuestionnaireByQuestion.getPemahaman_resiko4(), "Buruk", "Pemahaman atas resiko & spesifikasi produk");
			dataset.setValue(percentageQuestionnaireByQuestion.getPemahaman_resiko5(), "Buruk Sekali", "Pemahaman atas resiko & spesifikasi produk");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getPerlindungan_asuransi1(), "Sangat Baik", "Perlindungan Asuransi yang diperoleh");
			dataset.setValue(percentageQuestionnaireByQuestion.getPerlindungan_asuransi2(), "Baik", "Perlindungan Asuransi yang diperoleh");
			dataset.setValue(percentageQuestionnaireByQuestion.getPerlindungan_asuransi3(), "Kurang Baik", "Perlindungan Asuransi yang diperoleh");
			dataset.setValue(percentageQuestionnaireByQuestion.getPerlindungan_asuransi4(), "Buruk", "Perlindungan Asuransi yang diperoleh");
			dataset.setValue(percentageQuestionnaireByQuestion.getPerlindungan_asuransi5(), "Buruk Sekali", "Perlindungan Asuransi yang diperoleh");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getTarif_premi1(), "Sangat Baik", "Tarif Premi yang kompetitif");
			dataset.setValue(percentageQuestionnaireByQuestion.getTarif_premi2(), "Baik", "Tarif Premi yang kompetitif");
			dataset.setValue(percentageQuestionnaireByQuestion.getTarif_premi3(), "Kurang Baik", "Tarif Premi yang kompetitif");
			dataset.setValue(percentageQuestionnaireByQuestion.getTarif_premi4(), "Buruk", "Tarif Premi yang kompetitif");
			dataset.setValue(percentageQuestionnaireByQuestion.getTarif_premi5(), "Buruk Sekali", "Tarif Premi yang kompetitif");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getKelayakan_beban_biaya1(), "Sangat Baik", "Kelayakan pembebanan biaya produk asuransi");
			dataset.setValue(percentageQuestionnaireByQuestion.getKelayakan_beban_biaya2(), "Baik", "Kelayakan pembebanan biaya produk asuransi");
			dataset.setValue(percentageQuestionnaireByQuestion.getKelayakan_beban_biaya3(), "Kurang Baik", "Kelayakan pembebanan biaya produk asuransi");
			dataset.setValue(percentageQuestionnaireByQuestion.getKelayakan_beban_biaya4(), "Buruk", "Kelayakan pembebanan biaya produk asuransi");
			dataset.setValue(percentageQuestionnaireByQuestion.getKelayakan_beban_biaya5(), "Buruk Sekali", "Kelayakan pembebanan biaya produk asuransi");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getPenggunaan_policy1(), "Sangat Baik", "Penggunaan E-Policy");
			dataset.setValue(percentageQuestionnaireByQuestion.getPenggunaan_policy2(), "Baik", "Penggunaan E-Policy");
			dataset.setValue(percentageQuestionnaireByQuestion.getPenggunaan_policy3(), "Kurang Baik", "Penggunaan E-Policy");
			dataset.setValue(percentageQuestionnaireByQuestion.getPenggunaan_policy4(), "Buruk", "Penggunaan E-Policy");
			dataset.setValue(percentageQuestionnaireByQuestion.getPenggunaan_policy5(), "Buruk Sekali", "Penggunaan E-Policy");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getKetepatan_waktu_mail_sms1(), "Sangat Baik", "Ketepatan waktu dalam memberikan informasi melalui E-mail atau SMS");
			dataset.setValue(percentageQuestionnaireByQuestion.getKetepatan_waktu_mail_sms2(), "Baik", "Ketepatan waktu dalam memberikan informasi melalui E-mail atau SMS");
			dataset.setValue(percentageQuestionnaireByQuestion.getKetepatan_waktu_mail_sms3(), "Kurang Baik", "Ketepatan waktu dalam memberikan informasi melalui E-mail atau SMS");
			dataset.setValue(percentageQuestionnaireByQuestion.getKetepatan_waktu_mail_sms4(), "Buruk", "Ketepatan waktu dalam memberikan informasi melalui E-mail atau SMS");
			dataset.setValue(percentageQuestionnaireByQuestion.getKetepatan_waktu_mail_sms5(), "Buruk Sekali", "Ketepatan waktu dalam memberikan informasi melalui E-mail atau SMS");
			
			dataset.setValue(percentageQuestionnaireByQuestion.getKemudahan_call_center1(), "Sangat Baik", "Kemudahan menghubungi Call Center");
			dataset.setValue(percentageQuestionnaireByQuestion.getKemudahan_call_center2(), "Baik", "Kemudahan menghubungi Call Center");
			dataset.setValue(percentageQuestionnaireByQuestion.getKemudahan_call_center3(), "Kurang Baik", "Kemudahan menghubungi Call Center");
			dataset.setValue(percentageQuestionnaireByQuestion.getKemudahan_call_center4(), "Buruk", "Kemudahan menghubungi Call Center");
			dataset.setValue(percentageQuestionnaireByQuestion.getKemudahan_call_center5(), "Buruk Sekali", "Kemudahan menghubungi Call Center");
			
			width = 2000;
			height = 600;
		}else if( "2".equals( jenisReport ) ){
			KuesionerPelayananByGroup percentageQuestionnaireByGroup = uwManager.selectPercentageQuestionnaireByGroup();
			
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_a1(), "Sangat Baik", "Pelayanan");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_a2(), "Baik", "Pelayanan");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_a3(), "Kurang Baik", "Pelayanan");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_a4(), "Buruk", "Pelayanan");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_a5(), "Buruk Sekali", "Pelayanan");
			
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_b1(), "Sangat Baik", "Produk");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_b2(), "Baik", "Produk");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_b3(), "Kurang Baik", "Produk");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_b4(), "Buruk", "Produk");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_b5(), "Buruk Sekali", "Produk");
			
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_c1(), "Sangat Baik", "Teknologi");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_c2(), "Baik", "Teknologi");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_c3(), "Kurang Baik", "Teknologi");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_c4(), "Buruk", "Teknologi");
			dataset.setValue(percentageQuestionnaireByGroup.getGroup_c5(), "Buruk Sekali", "Teknologi");
			
			width = 800;
			height = 400;
		}else if( "3".equals( jenisReport ) ){
			KuesionerPelayananAll percentageQuestionnaireAll = uwManager.selectPercentageQuestionnaireAll();
			
			dataset.setValue(percentageQuestionnaireAll.getSangat_baik(), "Semua", "Sangat Baik");
			dataset.setValue(percentageQuestionnaireAll.getBaik(), "Semua", "Baik");
			dataset.setValue(percentageQuestionnaireAll.getKurang_baik(), "Semua", "Kurang Baik");
			dataset.setValue(percentageQuestionnaireAll.getBuruk(), "Semua", "Buruk");
			dataset.setValue(percentageQuestionnaireAll.getBuruk_sekali(), "Semua", "Buruk Sekali");
			
			width = 800;
			height = 400;
		}

		JFreeChart chart = ChartFactory.createBarChart3D( "Persentase Hasil Customer Service Survey",
				"Tingkatan", "Persentase(%)", dataset, PlotOrientation.VERTICAL, true, true, false );	
		  
		BufferedImage image = chart.createBufferedImage(width,height);
//		JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
//		jpegEncoder.encode(image);
		 ImageIO.write(image, "jpeg", jpegOutputStream);
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		// flush it in the response
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
		responseOutputStream.flush();
		responseOutputStream.close();
		jpegOutputStream.close();
		
		return null;
	}
	
	public ModelAndView percentageQuestionnaireMultiPieChart(HttpServletRequest request, HttpServletResponse response) throws Exception{
		byte[] captchaChallengeAsJpeg = null;
		Map map = new HashMap();
		int nilai=10;
		
		String jenisReport = request.getParameter("jenisReport");
		String pieKe = request.getParameter("pieKe");
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		DefaultPieDataset dataset = new DefaultPieDataset();
		Integer width = 0;
		Integer height = 0;
		String judul = "";
		if( "1".equals( jenisReport ) ){
			KuesionerPelayananByQuestion percentageQuestionnaireByQuestion = uwManager.selectPercentageQuestionnaireByQuestion();
			
			if( "1".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getSikap1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getSikap2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getSikap3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getSikap4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getSikap5());
				judul = "1. Sikap/Keramahan";
			}else if( "2".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getInisiatif1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getInisiatif2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getInisiatif3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getInisiatif4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getInisiatif5());
				judul = "2. Inisiatif/kesigapan membantu";
			}else if( "3".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getKejelasan_dlm_info1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getKejelasan_dlm_info2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getKejelasan_dlm_info3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getKejelasan_dlm_info4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getKejelasan_dlm_info5());
				judul = "3. Kejelasan dalam memberikan informasi";
			}else if( "4".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getSolusi1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getSolusi2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getSolusi3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getSolusi4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getSolusi5());
				judul = "4. Solusi atas masalah yang dikemukan";
			}else if( "5".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getPenguasaan_produk1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getPenguasaan_produk2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getPenguasaan_produk3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getPenguasaan_produk4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getPenguasaan_produk5());
				judul = "5. Penguasaan produk yang ditanyakan";
			}else if( "6".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getKetepatan_waktu1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getKetepatan_waktu2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getKetepatan_waktu3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getKetepatan_waktu4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getKetepatan_waktu5());
				judul = "6. Ketepatan waktu/cepat dalam pelayanan";
			}else if( "7".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getManfaat_produk1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getManfaat_produk2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getManfaat_produk3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getManfaat_produk4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getManfaat_produk5());
				judul = "7. Manfaat Produk";
			}else if( "8".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getPemahaman_resiko1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getPemahaman_resiko2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getPemahaman_resiko3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getPemahaman_resiko4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getPemahaman_resiko5());
				judul = "8. Pemahaman atas resiko & spesifikasi produk";
			}else if( "9".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getPerlindungan_asuransi1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getPerlindungan_asuransi2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getPerlindungan_asuransi3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getPerlindungan_asuransi4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getPerlindungan_asuransi5());
				judul = "9. Perlindungan Asuransi yang diperoleh";
			}else if( "10".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getTarif_premi1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getTarif_premi2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getTarif_premi3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getTarif_premi4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getTarif_premi5());
				judul = "10. Tarif Premi yang kompetitif";
			}else if( "11".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getKelayakan_beban_biaya1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getKelayakan_beban_biaya2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getKelayakan_beban_biaya3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getKelayakan_beban_biaya4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getKelayakan_beban_biaya5());
				judul = "11. Kelayakan pembebanan biaya produk asuransi";
			}else if( "12".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getPenggunaan_policy1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getPenggunaan_policy2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getPenggunaan_policy3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getPenggunaan_policy4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getPenggunaan_policy5());
				judul = "12. Penggunaan E-Policy";
			}else if( "13".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getKetepatan_waktu_mail_sms1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getKetepatan_waktu_mail_sms2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getKetepatan_waktu_mail_sms3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getKetepatan_waktu_mail_sms4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getKetepatan_waktu_mail_sms5());
				judul = "13. Ketepatan waktu dalam memberikan informasi melalui E-mail atau SMS";
			}else if( "14".equals( pieKe ) ){
				dataset.setValue("Sangat Baik", percentageQuestionnaireByQuestion.getKemudahan_call_center1());
				dataset.setValue("Baik", percentageQuestionnaireByQuestion.getKemudahan_call_center2());
				dataset.setValue("Kurang Baik", percentageQuestionnaireByQuestion.getKemudahan_call_center3());
				dataset.setValue("Buruk", percentageQuestionnaireByQuestion.getKemudahan_call_center4());
				dataset.setValue("Buruk Sekali", percentageQuestionnaireByQuestion.getKemudahan_call_center5());
				judul = "14. Kemudahan menghubungi Call Center";
			}
			
			
			width = 400;
			height = 300;
		}else if( "2".equals( jenisReport ) ){
			KuesionerPelayananByGroup percentageQuestionnaireByGroup = uwManager.selectPercentageQuestionnaireByGroup();
			
			if( "1".equals( pieKe ) ){
				dataset.setValue( "Sangat Baik", percentageQuestionnaireByGroup.getGroup_a1() );
				dataset.setValue( "Baik", percentageQuestionnaireByGroup.getGroup_a2() );
				dataset.setValue( "Kurang Baik", percentageQuestionnaireByGroup.getGroup_a3() );
				dataset.setValue( "Buruk", percentageQuestionnaireByGroup.getGroup_a4() );
				dataset.setValue( "Buruk Sekali", percentageQuestionnaireByGroup.getGroup_a5() );
				judul = "A. Pelayanan";
			}else if( "2".equals( pieKe ) ){
				dataset.setValue( "Sangat Baik", percentageQuestionnaireByGroup.getGroup_b1() );
				dataset.setValue( "Baik", percentageQuestionnaireByGroup.getGroup_b2() );
				dataset.setValue( "Kurang Baik", percentageQuestionnaireByGroup.getGroup_b3() );
				dataset.setValue( "Buruk", percentageQuestionnaireByGroup.getGroup_b4() );
				dataset.setValue( "Buruk Sekali", percentageQuestionnaireByGroup.getGroup_b5() );
				judul = "B. Produk";
			}else if( "3".equals( pieKe ) ){
				dataset.setValue( "Sangat Baik", percentageQuestionnaireByGroup.getGroup_c1() );
				dataset.setValue( "Baik", percentageQuestionnaireByGroup.getGroup_c2() );
				dataset.setValue( "Kurang Baik", percentageQuestionnaireByGroup.getGroup_c3() );
				dataset.setValue( "Buruk", percentageQuestionnaireByGroup.getGroup_c4() );
				dataset.setValue( "Buruk Sekali", percentageQuestionnaireByGroup.getGroup_c5() );
				judul = "C. Teknologi";
			}

			width = 400;
			height = 300;
		}else if( "3".equals( jenisReport ) ){
			KuesionerPelayananAll percentageQuestionnaireAll = uwManager.selectPercentageQuestionnaireAll();
			if( "1".equals( pieKe ) ){
				dataset.setValue( "Sangat Baik", percentageQuestionnaireAll.getSangat_baik());
				dataset.setValue( "Baik", percentageQuestionnaireAll.getBaik() );
				dataset.setValue( "Kurang Baik", percentageQuestionnaireAll.getKurang_baik() );
				dataset.setValue( "Buruk", percentageQuestionnaireAll.getBuruk() );
				dataset.setValue( "Buruk Sekali", percentageQuestionnaireAll.getBuruk_sekali() );
			}
			width = 600;
			height = 400;
		}

        JFreeChart chart = ChartFactory.createPieChart(
        		judul,  // chart title
                dataset,             // data
                false,               // include legend
                true,
                false
            );
		  
		BufferedImage image = chart.createBufferedImage(width,height);
//		JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
//		jpegEncoder.encode(image);
		ImageIO.write(image, "jpeg", jpegOutputStream);
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		// flush it in the response
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
		responseOutputStream.flush();
		responseOutputStream.close();
		jpegOutputStream.close();
		return null;
	}
	
	public ModelAndView listoutstandingkrg90hr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());	

				lsRegion=elionsManager.selectAllLstCab();
				Map cabang=new HashMap();
				lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
				lsRegion.add(0, cabang);
		
				if (All !=null){
					if(All.equals("1")){ // all user 
						cabang=new HashMap();
						cabang.put("KEY",currentUser.getLca_id());
						cabang.put("VALUE",currentUser.getCabang());
						lsRegion.add(cabang);
						lsAdmin=elionsManager.selectAllUserInCabang(lca_id); //all
					}else if (All.equals("2")){ //Regional User
						lsAdmin=elionsManager.selectAllUserRegional(lca_id); //reginonal
					}else if (All.equals("3")){ //agency
						lsAdmin=elionsManager.selectAllUserAgency(); //agency
					}else if (All.equals("4")){ //worksite
						lsAdmin=elionsManager.selectAllUserWorksite(lca_id); //worksite
					}
				}else{
					cabang=new HashMap();
					cabang.put("KEY",currentUser.getLca_id());
					cabang.put("VALUE",currentUser.getCabang());
					lsRegion.add(cabang);

				}
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		map.put("lock",lock);
		map.put("lsRegion", lsRegion);
		map.put("lca_id",lca_id);
		map.put("lus_id",lus_id);
		map.put("lsAgency", lsAgency);
		map.put("All", All);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("report/listoutstandingkrg90hr",map);
	}
	
	public ModelAndView listoutstandingkrg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		List lsRegion=new ArrayList();
		List lsAdmin=new ArrayList();
		List lsAgency = new ArrayList();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());	

				lsRegion=elionsManager.selectAllLstCab();
				Map cabang=new HashMap();
				lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
				lsRegion.add(0, cabang);
		
				if (All !=null){
					if(All.equals("1")){ // all user 
						cabang=new HashMap();
						cabang.put("KEY",currentUser.getLca_id());
						cabang.put("VALUE",currentUser.getCabang());
						lsRegion.add(cabang);
						lsAdmin=elionsManager.selectAllUserInCabang(lca_id); //all
					}else if (All.equals("2")){ //Regional User
						lsAdmin=elionsManager.selectAllUserRegional(lca_id); //reginonal
					}else if (All.equals("3")){ //agency
						lsAdmin=elionsManager.selectAllUserAgency(); //agency
					}else if (All.equals("4")){ //worksite
						lsAdmin=elionsManager.selectAllUserWorksite(lca_id); //worksite
					}
				}else{
					cabang=new HashMap();
					cabang.put("KEY",currentUser.getLca_id());
					cabang.put("VALUE",currentUser.getCabang());
					lsRegion.add(cabang);

				}
		lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		map.put("lock",lock);
		map.put("lsRegion", lsRegion);
		map.put("lca_id",lca_id);
		map.put("lus_id",lus_id);
		map.put("lsAgency", lsAgency);
		map.put("All", All);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("report/listoutstandingkrg",map);
	}
	
	public ModelAndView listuserprintuw(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");

		List<DropDown> daftarUser = new ArrayList<DropDown>();
//		daftarUser.add(0, new DropDown("0", "ALL"));
		daftarUser.addAll(this.elionsManager.selectDropDownUserUw("EKA.LST_USER", "LUS_ID", "LUS_LOGIN_NAME", "LUS_LOGIN_NAME", "LDE_ID = 11"));
		
		map.put("lock",lock);
		map.put("daftarUser", daftarUser);
		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("report/listuserprintuw",map);
	}
	
	public ModelAndView listuserprintuw_all(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
		boolean cari=false;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		
		String All=ServletRequestUtils.getStringParameter(request, "All");

		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("report/listuserprintuw_all",map);
	}
	
//	public ModelAndView report_TrkDtByTglRK(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Report report;
//		Map map = new HashMap();
//		Date now = this.elionsManager.selectSysdate();
//		Integer lock=0;
//		boolean show=false;
//		String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(now));
//		String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(now));
//		int lsbp_id = ServletRequestUtils.getIntParameter(request, "lsbp_id", 156);
//		String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
//		String startNominal = ServletRequestUtils.getStringParameter(request, "startNominal", "0");
//		String endNominal = ServletRequestUtils.getStringParameter(request, "endNominal", "99999999999");
//		
//		map.put("startDate", startDate);
//		map.put("endDate", endDate);
//		map.put("lsbp_id", lsbp_id);
//		map.put("kode", kode);
//		map.put("startNominal", startNominal);
//		map.put("endNominal", endNominal);
//		
//		map.put("daftarBank", this.elionsManager.selectDropDown("EKA.LST_BANK_PUSAT", "LSBP_ID", "LSBP_NAMA", "LSBP_NAMA", null));
//		
//		List<DropDown> daftarCab = new ArrayList<DropDown>();
//		daftarCab.add(0, new DropDown("", "ALL"));
//		daftarCab.addAll(this.elionsManager.selectDropDown("EKA.CAB_BSM", "KODE", "NAMA_CAB  || ' (' || KODE || ')'", "NAMA_CAB", null));
//		
//		map.put("daftarCab", daftarCab);
//		map.put("daftarDrek", this.elionsManager.selectMstDrek(lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal)));
//		
//		return new ModelAndView("uw/report_TrkDtByTglRK", map);
//	}

	public ModelAndView upload_icd_code(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommandUploadUw cmd = new CommandUploadUw();
		if(request.getParameter("upload") != null) {
			try {
				ServletRequestDataBinder binder;
				binder = createBinder(request, cmd);
				binder.bind(request);
				List<String> error = new ArrayList<String>();
				if(cmd.getFile1() == null) {
					error.add("Silahkan masukan file dengan tipe EXCEL (.xls)");
				}else if(cmd.getFile1().isEmpty() || cmd.getFile1().getOriginalFilename().toLowerCase().indexOf(".xls")==-1) {
					error.add("Silahkan masukan file dengan tipe EXCEL (.xls)");
				}else {
					cmd.setDaftarStatus(uwManager.uploadIcdCode(cmd,request));
				}
				cmd.setErrorMessages(error);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			}			
		}
		return new ModelAndView("common/upload_icd_code", "cmd", cmd);
	}
	
	public ModelAndView report_filing(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
		String tahun = ServletRequestUtils.getStringParameter(request, "tahun", "");
		
		map.put("daftarTahun", uwManager.selectTahunFiling());
		
		if(!tahun.equals("")) {
			map.put("getTahun", tahun);
			map.put("daftarBulan", uwManager.selectBulanFiling(tahun));
			map.put("tampil", "ok");
		}
		
		return new ModelAndView("report/report_filing",map);
	}
	
	public ModelAndView healthClaim(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
		String spaj=ServletRequestUtils.getStringParameter(request,"spaj","");
	
		
		if(!spaj.equals("")){
			Tertanggung ttg=elionsManager.selectttg(spaj);
			if(ttg!=null){
				
				Integer spasi=ttg.getMcl_first().indexOf(' ');
				Integer titik=ttg.getMcl_first().indexOf('.');
				Integer koma=ttg.getMcl_first().indexOf(',');
				Integer pjg=ttg.getMcl_first().length();
				String nama="";
				nama=ttg.getMcl_first();
				if(spasi>0)
					nama=ttg.getMcl_first().substring(0,spasi);
				else if(titik>0)
					nama=ttg.getMcl_first().substring(0,titik);
				else if(koma>0)
					nama=ttg.getMcl_first().substring(0,koma);
				map.put("healthproduct", uwManager.selectCekHealthProduct(nama, ttg.getMspe_date_birth()));
				map.put("spaj", spaj);
//				map.put("blacklist", uwManager.selectCekBlacklist(new SimpleDateFormat("yyyy/MM/dd").format(ttg.getMspe_date_birth()), nama));
	//			map.put("claim", uwManager.selectHealthClaim(nama, ttg.getMspe_date_birth()));
			}
		}else if(request.getParameter("cari")!=null){
			
			String nama=ServletRequestUtils.getStringParameter(request, "nama","");
			String tglhr=ServletRequestUtils.getStringParameter(request, "tgllhr","");
			String err="";
			if(nama.equals("")){
				err="Silahkan masukkan nama tertanggung";
			}else if(tglhr.equals("")){
				err="Silahkan masukkan tanggal lahir tertanggung";
			}else{
				
				Date tgllhr=null;
				
				try{
					tgllhr=new SimpleDateFormat("dd/MM/yyyy").parse(tglhr);
				}catch (Exception e) {
					// TODO: handle exception
					err="Tanggal lahir tertanggung tidak valid";
				}
				
				if(err.equals("")){
					map.put("healthproduct", uwManager.selectCekHealthProduct(nama, tgllhr));
//					map.put("blacklist", uwManager.selectCekBlacklist(new SimpleDateFormat("yyyy/MM/dd").format(tgllhr), nama));
				}
			}
			
			if(!err.equals("")){
				map.put("err", err);
			}
			
			map.put("nama", nama);
			map.put("tgllhr", tglhr);
			
		}
		
		
		return new ModelAndView("uw/healthClaim",map);
	}
	
	public ModelAndView healthClaimDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
		String multiparam=ServletRequestUtils.getStringParameter(request, "multiparam","");
		//
		Integer key = 0;
		String [] multiparamSplit=multiparam.split("#");
		List<Map> listnya=new ArrayList<Map>();
		for(String param: multiparamSplit){
			Map map1=new HashMap<String, Object>();
			String [] paramSplit=param.split(">");
			String spaj=paramSplit[0];
			String insured_no=paramSplit[2];
			Integer kode=Integer.parseInt(paramSplit[1]);
			String polis=paramSplit[3];
			
			map1.put("polis", polis);
			map1.put("kode", kode);
			map1.put("spaj", spaj);
			map1.put("insured_no", insured_no);
			map1.put("key", key);
			
			if(kode==1){//claim individu
				map1.put("claim", uwManager.selectHealthClaimSum(spaj));
			}else if(kode==2){//claim tm
				map1.put("claim", uwManager.selectHealthClaimTMSum(spaj));			
			}else if(kode==3){//claim eb
				map1.put("claimAccept", uwManager.selectHealthClaimEBAcceptSum(spaj, insured_no));			
				map1.put("claimTolak", uwManager.selectHealthClaimEBTolakSum(spaj, insured_no));
				map1.put("claimPreAccept", uwManager.selectHealthClaimEBPREAcceptSum(spaj, insured_no));
				map1.put("claimPU", uwManager.selectHealthClaimEBPUSum(spaj, insured_no));
			}
			key += 1;
			listnya.add(map1);
		}
		
		map.put("isinya", listnya);

//		String spaj=ServletRequestUtils.getStringParameter(request,"spaj","");
//		String insured_no=ServletRequestUtils.getStringParameter(request,"insured_no","");
//		Integer kode=ServletRequestUtils.getIntParameter(request,"kode",0);
//		
//		if(kode==1){//claim individu
//			map.put("claim", uwManager.selectHealthClaim(spaj));
//		}else if(kode==2){//claim tm
//			map.put("claim", uwManager.selectHealthClaimTM(spaj));			
//		}else if(kode==3){//claim eb
//			map.put("claimAccept", uwManager.selectHealthClaimEBAccept(spaj, insured_no));			
//			map.put("claimTolak", uwManager.selectHealthClaimEBTolak(spaj, insured_no));
//			map.put("claimPreAccept", uwManager.selectHealthClaimEBPREAccept(spaj, insured_no));
//		}
//		map.put("kode", kode);
		
		return new ModelAndView("uw/healthClaimDetail",map);
	}
	
	public ModelAndView healthClaimMoreDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String c=ServletRequestUtils.getStringParameter(request, "c","");
		String d=ServletRequestUtils.getStringParameter(request, "d","");
		String e=ServletRequestUtils.getStringParameter(request, "e","");
		
		Map map1=new HashMap<String, Object>();
		
		map1.put("no_klaim", c);
		map1.put("kode", d);
		
		
			if(d.equals("1")){
				map1.put("claimPreAccept", uwManager.selectHealthClaimEBPREAccept(c,e));
			}else if(d.equals("2")){
				map1.put("claimPU", uwManager.selectHealthClaimEBPU(c,e));
			}else if(d.equals("3")){
				map1.put("claimAccept", uwManager.selectHealthClaimEBAccept(c,e));			
			}else if(d.equals("4")){
				map1.put("claimTolak", uwManager.selectHealthClaimEBTolak(c,e));
			}else if(d.equals("5")){//individu
				map1.put("claimIndividu", uwManager.selectHealthClaim(c,e));
			}else if(d.equals("6")){//dmtm
				map1.put("claimDMTM", uwManager.selectHealthClaimTM(c));
			}
				
			
			return new ModelAndView("uw/healthClaimMoreDetail",map1);
	}
	
	public ModelAndView healthClaimTracking(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
		String regclaim=ServletRequestUtils.getStringParameter(request, "regclaim","");
		
		
		map.put("claimTracking", uwManager.selectTrackingClaimHealth(regclaim));
		return new ModelAndView("uw/healthClaimTracking",map);
	}
	
	public ModelAndView windowWorksheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Map> daftar = new  ArrayList<Map>();
		String mode = ServletRequestUtils.getStringParameter(request,"mode","");
		String sub = ServletRequestUtils.getStringParameter(request, "sub");
		String indx = ServletRequestUtils.getStringParameter(request, "indx","");
		Integer no = ServletRequestUtils.getIntParameter(request, "no",-1);
		
		Map params = new HashMap();
		
		if(mode.equals("cari_icd")) {
			String id = ServletRequestUtils.getStringParameter(request,"icdId","");
			String desc = ServletRequestUtils.getStringParameter(request,"icdDesc","");
			String classy = ServletRequestUtils.getStringParameter(request,"klasifikasi","");
			Integer type = ServletRequestUtils.getIntParameter(request, "icdType",10);
			
			List<String> srgMuncul = new ArrayList<String>();
			srgMuncul.add("A16");
			srgMuncul.add("B15");
			srgMuncul.add("B16");
			srgMuncul.add("B17.1");
			srgMuncul.add("C54.2");
			srgMuncul.add("C95.9");
			srgMuncul.add("D64.9");
			srgMuncul.add("D72.8");
			srgMuncul.add("E05.9");
			srgMuncul.add("E10");
			srgMuncul.add("E11");
			srgMuncul.add("E14");
			srgMuncul.add("E16.0");
			srgMuncul.add("E16.2");
			srgMuncul.add("E66.9");
			srgMuncul.add("E78.5");
			srgMuncul.add("E79.0");
			srgMuncul.add("E80.7");
			srgMuncul.add("F40.9");
			srgMuncul.add("G61.0");
			srgMuncul.add("G70.0");
			srgMuncul.add("I10");
			srgMuncul.add("I20.9");
			srgMuncul.add("I24.9");
			srgMuncul.add("I25.2");
			srgMuncul.add("I25.9");
			srgMuncul.add("I42.9");
			srgMuncul.add("I49.3");
			srgMuncul.add("I49.9");
			srgMuncul.add("I49.10");
			srgMuncul.add("I64");
			srgMuncul.add("J18.0");
			srgMuncul.add("J20.9");
			srgMuncul.add("J30.4");
			srgMuncul.add("J40");
			srgMuncul.add("J42");
			srgMuncul.add("J45");
			srgMuncul.add("J47");
			srgMuncul.add("K29.5");
			srgMuncul.add("K29.7");
			srgMuncul.add("K76.0");
			srgMuncul.add("K80");
			srgMuncul.add("M06.9");
			srgMuncul.add("M81.9");
			srgMuncul.add("N13.3");
			srgMuncul.add("N19");
			srgMuncul.add("N20.0");
			srgMuncul.add("N20.1");
			srgMuncul.add("N21.0");
			srgMuncul.add("N30.9");
			srgMuncul.add("N39.0");
			srgMuncul.add("N39.1");
			srgMuncul.add("N40");
			srgMuncul.add("N41.0");
			srgMuncul.add("N41.1");
			srgMuncul.add("N42.0");
			srgMuncul.add("N60.0");
			srgMuncul.add("Q62.0");
			srgMuncul.add("R00.0");
			srgMuncul.add("R07.4");
			srgMuncul.add("R31");
			srgMuncul.add("R63.4");
			srgMuncul.add("R70.0");
			srgMuncul.add("R73.0");
			srgMuncul.add("R73.9");
			srgMuncul.add("R80");
			srgMuncul.add("R81");
			srgMuncul.add("R82.2");
			srgMuncul.add("R82.4");
			srgMuncul.add("R82.9");
			srgMuncul.add("R91");
			srgMuncul.add("R93.1");
			srgMuncul.add("R93.5");
			srgMuncul.add("R94.3");
			srgMuncul.add("R94.5");
			srgMuncul.add("R94.6");
			srgMuncul.add("Y90");
			srgMuncul.add("Z22.5");
			srgMuncul.add("Z82.3");
			srgMuncul.add("Z83.3");
			
			if(request.getParameter("btnCari") != null) {
				daftar = uwManager.selectDaftarIcd(id,desc,type);
				for(int a=0;a<srgMuncul.size();a++) {
					for(int b=0;b<daftar.size();b++) {
						Map x = daftar.get(b);
						if(x.get("LIC_ID").toString().equals(srgMuncul.get(a))) {
							x.put("color", "color: red;");
							daftar.set(b, x);
							break;
						}
					}
				}
			}
			else if(!classy.equals("")) {
				String[] dataSplit = classy.split("-");
				daftar = uwManager.selectIcdByClassy(dataSplit[0],dataSplit[1]);
				for(int a=0;a<srgMuncul.size();a++) {
					for(int b=0;b<daftar.size();b++) {
						Map x = daftar.get(b);
						if(x.get("LIC_ID").toString().equals(srgMuncul.get(a))) {
							x.put("color", "color: red;");
							daftar.set(b, x);
							break;
						}
					}
				}
			}
			params.put("no", no);
			params.put("srgMuncul", srgMuncul);
		}
		else if(mode.equals("cari_provider")) {
			String name = ServletRequestUtils.getStringParameter(request, "name","");
			String addr = ServletRequestUtils.getStringParameter(request, "addr","");
			if(request.getParameter("btnCari") != null) {
				daftar = uwManager.selectDaftarProvider(name,addr);
			}
		}
		params.put("daftar", daftar);
		params.put("indx", indx);
		params.put("sub", sub);
		
		return new ModelAndView("uw/"+mode,params);
	}
	
	public ModelAndView kwitansi_mall(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		if(request.getParameter("tampil") != null) {
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
			if(!spaj.equals("")) map.put("src",request.getContextPath()+"/reports/kwitansi.pdf?spaj="+spaj);
		}
		
		return new ModelAndView("uw/kwitansi_mall",map);
	}
	//Method untuk aksep spt by direksi	
		public ModelAndView aksepsp(HttpServletRequest request, HttpServletResponse response) throws Exception {
			HttpSession session = request.getSession();
			User currentUser = (User) session.getAttribute("currentUser");
			String reg_spaj=ServletRequestUtils.getRequiredStringParameter(request,"spaj");
			String pp=ServletRequestUtils.getRequiredStringParameter(request,"pp");
			String status=ServletRequestUtils.getRequiredStringParameter(request,"status");
			String email=ServletRequestUtils.getRequiredStringParameter(request,"email");
			String flag=ServletRequestUtils.getRequiredStringParameter(request,"flag");	
			
//			
			Map test=new HashMap();
			List<String> error = new ArrayList<String>();			
			HashMap hasil = uwManager.aksepSPT(reg_spaj.trim(), currentUser, pp, status, email,flag) ;//true berarti berhasil, false gagal.
			String statusAksep = (String)hasil.get("status");
			String namaUserAksep = (String)hasil.get("nama");
			if(statusAksep.equals("1")){
				error.add("Selamat Anda Telah Berhasil Melakukan Akseptasi");
			}else if (statusAksep.equals("2")){
				error.add("SPT Telah  Anda Tolak");
			}else if(statusAksep.equals("3")){
				error.add("SPT Telah Di Aksep/Di Tolak Oleh " + "" + namaUserAksep);
			}
			if(!error.equals("")){
				test.put("pesanError", error);
			}	
			test.put("err", error);	
			return new ModelAndView("uw/aksepsp",test);
		}
	
	public ModelAndView reportKartuEkaSehat(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Report report;
		Map map = new HashMap();
		Integer lock=0;
		boolean cari=false;
		String lca_id;
//		Date sysdate=elionsManager.selectSysdate();
		Date sysdate=new Date();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String tglAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal",FormatDate.toString(sysdate));
		String tglAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir",FormatDate.toString(sysdate));		

		map.put("tglAwal",tglAwal);
		map.put("tglAkhir", tglAkhir);
		return new ModelAndView("uw/reportKartuEkaSehat", map);
	}
	
	public ModelAndView rekklaim(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");		
		String reg_spaj=ServletRequestUtils.getRequiredStringParameter(request,"spaj");
		String inbox=ServletRequestUtils.getRequiredStringParameter(request,"inbox");
		Integer flag_aksep= ServletRequestUtils.getRequiredIntParameter(request,"flag_aksep");
		String lca_id=elionsManager.selectCabangFromSpaj(reg_spaj);
		String dir = props.getProperty("pdf.dir.export") + "//" + lca_id;
		Integer hasil=uwManager.rekklaim(currentUser, reg_spaj.trim(), dir, flag_aksep, inbox);
		Map test=new HashMap();	
		List<String> error = new ArrayList<String>();			
		if (hasil == 0)	{
				error.add("Rekomendasi Klaim Berhasil terkirim.");
		}else if(hasil == 1) {				
					error.add(" Rekomendasi Klaim Telah disetujui.");
		}else if(hasil == 2){
			error.add(" Rekomendasi Klaim Tidak disetujui.");
		}
		if(!error.equals("")){
			test.put("pesanError", error);
		}	
	    
		test.put("err", error);	
		return new ModelAndView("uw/aksepsp",test);
	}
	
	public ModelAndView snowsdireksi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");	
		String lus_id=ServletRequestUtils.getRequiredStringParameter(request,"id");
		String login_name=ServletRequestUtils.getRequiredStringParameter(request,"name");
		String reg_spaj=ServletRequestUtils.getRequiredStringParameter(request,"spaj");
		String inbox=ServletRequestUtils.getRequiredStringParameter(request,"inbox");
		String jns=ServletRequestUtils.getRequiredStringParameter(request,"jns");
		Integer flag_aksep= null;//ServletRequestUtils.getRequiredIntParameter(request,"flag_aksep");
		String idx_aksep=ServletRequestUtils.getStringParameter(request,"idx");
		String lca_id=elionsManager.selectCabangFromSpaj(reg_spaj);
		String dir = props.getProperty("pdf.dir.export") + "//" + lca_id;
		currentUser.setLus_id(lus_id);
		currentUser.setName(login_name);
		Integer hasil=bacManager.snowsdireksi(currentUser, reg_spaj.trim(), dir, flag_aksep, inbox, jns, idx_aksep);
		Map test=new HashMap();	
		List<String> error = new ArrayList<String>();			
//		if (hasil == 0)	{
//				error.add("Rekomendasi Klaim Berhasil terkirim.");
//		}else if(hasil == 1) {				
//					error.add(" Rekomendasi Klaim Telah disetujui.");
//		}else if(hasil == 2){
//			error.add(" Rekomendasi Klaim Tidak disetujui.");
//		}
		if(!error.equals("")){
			test.put("pesanError", error);
		}	
	    
		test.put("err", error);	
		return new ModelAndView("uw/aksepsp",test);
	}
	
	public ModelAndView snowsdireksiaksep(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");		
		String lus_id=ServletRequestUtils.getRequiredStringParameter(request,"id");
		String login_name=ServletRequestUtils.getRequiredStringParameter(request,"name");
		String reg_spaj=ServletRequestUtils.getRequiredStringParameter(request,"spaj");
		String inbox=ServletRequestUtils.getRequiredStringParameter(request,"inbox");
		Integer status= ServletRequestUtils.getRequiredIntParameter(request,"status");
		String jns=ServletRequestUtils.getRequiredStringParameter(request,"jns");
		String idx_aksep=ServletRequestUtils.getStringParameter(request,"idx");
		String lca_id=elionsManager.selectCabangFromSpaj(reg_spaj);
		String dir = "";//props.getProperty("pdf.dir.export") + "//" + lca_id;
		
		currentUser.setLus_id(lus_id);
		currentUser.setName(login_name);
		Integer hasil=bacManager.snowsdireksiaksep(currentUser, reg_spaj.trim(), dir, status, inbox, jns, idx_aksep);
		Map test=new HashMap();	
		List<String> error = new ArrayList<String>();			
		if (hasil == 0)	{
				error.add("");
		}else if(hasil == 1) {				
					error.add(" Policy has been approved.");
		}else if(hasil == 2){
			error.add(" Policy has been rejected.");
		}
		if(!error.equals("")){
			test.put("pesanError", error);
		}	
	    
		test.put("err", error);	
		return new ModelAndView("uw/aksepsp",test);
	}
	
	//Akseptasi SPT LB
	public ModelAndView aksepsplb(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");		
		String reg_spaj=ServletRequestUtils.getRequiredStringParameter(request,"spaj");			
		String lca_id=elionsManager.selectCabangFromSpaj(reg_spaj);
		String dir = props.getProperty("pdf.dir.export") + "//" + lca_id;	
		Integer hasil=uwManager.aksepsptlb(currentUser,reg_spaj.trim(),dir);
		Map test=new HashMap();		
		return new ModelAndView("uw/kirimspt",test);
	}
	
	/**
     * @author Canpri
     * @since 25 Apr 2013
     * @category Spesial hadiah stable save
     * @param reg_spaj
     * http://localhost/E-Lions/uw/uw.htm?window=spesial_hadiah
     */
	public ModelAndView spesial_hadiah(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		
		Map map = new HashMap();
		String reg_spaj=ServletRequestUtils.getRequiredStringParameter(request,"reg_spaj");
		Integer jml = 0;
		Integer mode = 0;
		
		Map data = uwManager.selectDataUsulan(reg_spaj);
		Integer lsbs_id = (Integer) data.get("LSBS_ID");
		Integer lsdbs_number = (Integer) data.get("LSDBS_NUMBER");
		Double premi = (Double) data.get("MSPR_PREMIUM");
		
		Integer status = elionsManager.selectStsAksepBySpaj(reg_spaj);
		
		if(status==5 || status==10){
			map.put("pesan", "Status polis sudah akseptasi");
		}else if(lsbs_id==184 && lsdbs_number==6){
			mode = 1;
			if(request.getParameter("btnSimpan")!=null){
				Integer jml_hadiah = ServletRequestUtils.getIntParameter(request, "jmlhadiah", 0);
				List<Hadiah> sel_hadiah = uwManager.selectHadiah(reg_spaj);
				
				Hadiah info_hadiah = sel_hadiah.get(0);
				
				if(jml_hadiah>0){
					uwManager.deleteHadiahBAC(info_hadiah);
					for(int i=1;i<=jml_hadiah;i++){
						String value = request.getParameter("hadiah.mh_no"+i);
						String qty = request.getParameter("hadiah.mh_quantity"+i);
						
						String lh_id = value.substring(0,value.indexOf("~"));
						String lh_harga = value.substring(value.indexOf("~")+1, value.length());
						
						Hadiah hadiah = new Hadiah();
						hadiah.reg_spaj = info_hadiah.reg_spaj;
						hadiah.mh_no = i;
						hadiah.lhc_id = 8;
						hadiah.lh_id = Integer.parseInt(lh_id);
						hadiah.lspd_id = 84;
						hadiah.mh_alamat = info_hadiah.mh_alamat;
						hadiah.mh_kota = info_hadiah.mh_kota;
						hadiah.mh_kodepos = info_hadiah.mh_kodepos;
						hadiah.mh_telepon = info_hadiah.mh_telepon;
						hadiah.mh_quantity = Integer.parseInt(qty);
						hadiah.create_id = Integer.parseInt(currentUser.getLus_id());
						hadiah.program_hadiah = 1;
						hadiah.mh_alamat_kirim = info_hadiah.mh_alamat_kirim;
						hadiah.mh_kota_kirim = info_hadiah.mh_kota_kirim;
						hadiah.mh_kodepos_kirim = info_hadiah.mh_kodepos_kirim;
						hadiah.flag_standard = 2;
						hadiah.lh_harga = Double.parseDouble(lh_harga);
							
						uwManager.saveHadiahBAC(hadiah);
					}
				}
			}
			
			List<Hadiah> hd = uwManager.selectHadiah(reg_spaj);
			
			jml = hd.size();		
			map.put("hadiah", hd);
			map.put("select_hadiah", ajaxManager.select_hadiah_ps_spesial(premi));
			map.put("premi", premi);
		}else{
			map.put("pesan", "Polis ini bukan termasuk program hadiah");
		}
		
		map.put("jml", jml);
		map.put("mode", mode);
		return new ModelAndView("uw/spesial_hadiah",map);
	}
	
	/**
	 * @author Chandra A
	 * @date 20180413
	 * @return siap2u data
	 * url uw/uw.htm?window=monitor_ws
	 */
	public ModelAndView monitor_ws(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		HttpSession session = request.getSession();
		
		try{			
			if(request.getParameter("btnFixJob") != null){
				String reg_spaj = ServletRequestUtils.getRequiredStringParameter(request, "reg_spaj");
				bacManager.ultimateJobFixing(reg_spaj);
				map.put("reg_spaj", reg_spaj);
			}
			
			if(request.getParameter("retryProd") != null){
				String noid = request.getParameter("noid");
				bacManager.retryProduction(noid);
				response.sendRedirect("/E-Lions/uw/uw.htm?window=monitor_ws");
			}
			
			if(request.getParameter("retryPayment") != null){
				String noid = request.getParameter("noid");
				bacManager.retryPayment(noid);
				response.sendRedirect("/E-Lions/uw/uw.htm?window=monitor_ws");
			}
			
			if(request.getParameter("retryDeposit") != null){
				String reg_spaj = request.getParameter("spaj");
				/**
				 * Belum di implementasikan
				 */
				response.sendRedirect("/E-Lions/uw/uw.htm?window=monitor_ws");
			}
			
			List daftar_contract = bacManager.selectJumlahContract(); 
			List data_belum_prod = bacManager.selectDataBelumProd();
			List data_json_temp = bacManager.selectDataJsonTemp();
			List fail_prod = bacManager.selectFailSiap2UProd();		
			List fail_payment = bacManager.selectFailPayment();		
			List fail_to_deposit  = bacManager.selectDepositPremiumFailed();		
			
			map.put("daftar", daftar_contract);
			map.put("belum_prod", data_belum_prod);
			map.put("json_temp", data_json_temp);
			map.put("fail_prod", fail_prod);
			map.put("fail_payment", fail_payment);
			map.put("fail_to_deposit", fail_to_deposit);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ModelAndView("uw/monitor_ws", map);
	}
	
	public ModelAndView sphlions(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map test=new HashMap();
		List<String> error = new ArrayList<String>();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		String reg_spaj=ServletRequestUtils.getRequiredStringParameter(request,"reg_spaj");
		String path_file=ServletRequestUtils.getRequiredStringParameter(request,"path_file");
		String nama_file=ServletRequestUtils.getRequiredStringParameter(request,"nama_file");
		String ttd=ServletRequestUtils.getRequiredStringParameter(request,"ttd");//0 = mie yoen 1 = ety
//		Integer type=ServletRequestUtils.getIntParameter(request, "type");
		Integer type = 2;//default type 2 untuk yang full(perlu dirotate). type 1 untuk yang half.
		if(nama_file == null){
			nama_file = "";
		}
		
		if(nama_file.contains("SPH2")){
			type = 1;
		}
		String lca_id=elionsManager.selectCabangFromSpaj(reg_spaj);
		String userDir = props.getProperty("pdf.dir.export")+"\\"+lca_id+"\\"+reg_spaj;
		String outputFile = userDir+"\\"+path_file+"\\"+"SPH_TTD.pdf";
//		String outputFile = userDir+"\\"+"SPH_TTD.pdf";
//		String miyoen = props.getProperty("images.ttd.miyoen");
//		String miyoen = props.getProperty("pdf.dir.syaratpolis")+"//images//miyoen.bmp";
		//Deddy (2 sept 2014) : REQ ERI,ganti ttd dari Bu Miyoen jd Dr. Indra
		//Adrian(28 Agt 2015) : Helpdesk 76376, ganti jd S.R. Annalysa
		String miyoen = props.getProperty("images.ttd.annalisa");
		if(ttd.equals("1")){
			miyoen = props.getProperty("pdf.dir.syaratpolis")+"//images//ety.bmp";
		}
		//contoh spaj : 09201021787;
		File fileDir = new File(userDir);
//      1. cek apakah directory sudah ada atau belum, kalau belum dicreate otomatis
		if(!fileDir.exists()) {
        	fileDir.mkdirs();
        }
        
//      2. cek apakah file sudah ada atau belum
		File file = new File(outputFile);
		if (file.exists()){
//			Kalau sudah ada, infoin bahwa file sudah pernah dicreate.Bisa langsung ambil file yang ada.
			error.add("Sudah pernah di Generate untuk SPH yang sudah ditandatangani.");
		}else{
//			Kalau belum ada, ambil file sph_blank dahulu(dilihat dari nama_file yang pak him kirimkan), di itext, kemudian dioutput jadi sph_ttd
//			File from = new File(userDir+"\\"+nama_file);
//			File to = new File(userDir+"\\"+"sph_ttd.pdf");
			PdfReader reader = new PdfReader(userDir+"\\"+path_file+"\\"+nama_file);
//			PdfReader reader = new PdfReader(userDir+"\\"+nama_file);
			if(type==2){
				for (int p = 1; p <= reader.getNumberOfPages(); ++p) {
					reader.getPageN(p).put(PdfName.ROTATE, new PdfNumber(90));
		        }
			}
			
			PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputFile));
			Map m = uwManager.selectMstPinjaman(reg_spaj);
			BigDecimal nilai_pinjaman = (BigDecimal)m.get("MSPIN_PINJAMAN");
			BigDecimal bunga = (BigDecimal)m.get("MSPIN_BUNGA");
			BigDecimal nilai_tunai = (BigDecimal)m.get("MSPIN_JLH_NT");
			Date end_date_pinjaman = (Date)m.get("MSPIN_END_DATE");
			String kurs = (String) m.get("LKU_ID"); 
			PdfContentByte cb = stamp.getOverContent(1);
			cb = stamp.getOverContent(1);
        	cb.beginText();
        	BaseFont bf = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIALNB.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        	cb.setFontAndSize(bf, 9);
        	JasperScriptlet jasper = new JasperScriptlet();
        	String[] terbilang = StringUtil.pecahParagrafLineBreaksInclude(jasper.formatTerbilang(nilai_pinjaman, kurs), 64);
        	Integer addspace=0;
        	if(type==1){//1 untuk non logo AJSMSIG
        		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, jasper.format2Digit(nilai_pinjaman), 220, 932, 0);
        		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, bunga.toString(), 140, 920, 0);
        		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, df.format(end_date_pinjaman), 140, 908, 0);
        		for(int i=0; i<terbilang.length; i++) {
            		addspace = 12 * i;
//            		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, terbilang[i] , 			370, 915-addspace, 0);
            	}
        		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.format2Digit(new BigDecimal(nilai_tunai.doubleValue() * new Double(0.8)) ) , 			370, 915, 0);
        		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.format2Digit(new BigDecimal(nilai_tunai.doubleValue()) ) , 			410, 679, 0);
        		cb.endText();
            	Image img = Image.getInstance(miyoen);
    			
    			img.setAbsolutePosition(85, 85);		
    			img.scaleAbsolute(90, 25);
    			cb.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 50, 640);
    			cb.stroke();
        	}else if(type==2){//2 untuk yg logo AJSMSIG
        		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, jasper.format2Digit(nilai_pinjaman), 400, 410, 0);
        		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, bunga.toString(), 340, 397, 0);
        		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, df.format(end_date_pinjaman), 360, 384, 0);
        		for(int i=0; i<terbilang.length; i++) {
            		addspace = 12 * i;
//            		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, terbilang[i] , 			700, 397-addspace, 0);
            	}
        		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.format2Digit(new BigDecimal(nilai_tunai.doubleValue() * new Double(0.8)) ) , 			660, 392, 0);
        		cb.showTextAligned(PdfContentByte.ALIGN_LEFT, jasper.format2Digit(new BigDecimal(nilai_tunai.doubleValue()) ) , 			700, 156, 0);
        		cb.endText();
            	Image img = Image.getInstance(miyoen);
    			
    			img.setAbsolutePosition(85, 85);		
    			img.scaleAbsolute(90, 25);
    			cb.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 280, 75);
    			cb.stroke();
        	}
        	
			stamp.close();
			reader.close();
		}
		
		if(!error.equals("")){
			test.put("pesanError", error);
		}	
	    
		test.put("err", error);	
		return new ModelAndView("uw/aksepsp",test);
	}
	
	/**
	 * @author  : Andhika
	 * @created : Mar 29, 2013 11:34:46 AM
	 */
	public ModelAndView akseptasi_endorsment(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		
        User currentUser = (User) request.getSession().getAttribute("currentUser");
        String lus_id = currentUser.getLus_id();

        // Cuman IT ama UW
        if(currentUser.getLde_id().equals("01") || currentUser.getLde_id().equals("11")){
            double angka = (double) currentUser.getScreenWidth() / (double) currentUser.getScreenHeight();
            if(angka > 1.4) {
            	map.put("wideScreen", true);
            }
        }
//        Date now = elionsManager.selectSysdate();
        Date now = new Date();
        map.put("startDate", defaultDateFormat.format(now));
		map.put("endDate", defaultDateFormat.format(now));
		map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ_aksepEndors("1,2,6,7,8,99", 1, 10, null));
		map.put("flag_back_to_lb", 1);
		return new ModelAndView("uw/akseptasi_endorsment", map);
	}	
	
	/**
	 * @author  : Ryan
	 * @created : 20 Desember 2013
	 * 
	 * Fungsi Auto Proses H+1
	 */
	
	public ModelAndView autoprosesUwSimas(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map m = new HashMap();
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		m.put("user_id", lus_id);
		
		String cab_bank="";
		Integer jn_bank = -1;
		Map data_valid = (HashMap)this.elionsManager.select_validbank(lus_id);
		if (data_valid != null)
		{
			cab_bank = (String)data_valid.get("CAB_BANK");
			jn_bank = (Integer) data_valid.get("JN_BANK");
		}
		
		if (cab_bank == null)
		{
			cab_bank = "";
		}
		m.put("cabang_bank", cab_bank);
		m.put("jn_bank", jn_bank);
		
		int dari = ServletRequestUtils.getIntParameter(request, "dari", 1);
		int sampai = ServletRequestUtils.getIntParameter(request, "sampai", 30);
		String kata = ServletRequestUtils.getStringParameter(request, "kata", "");
		String tipe = ServletRequestUtils.getStringParameter(request, "tipe", "");
		String pilter = ServletRequestUtils.getStringParameter(request, "pilter", "");
		int tambah = 0; 
	
		if("prev".equals(ServletRequestUtils.getStringParameter(request, "action", ""))) {
			if(dari > 30) tambah = -30;
		}else if("next".equals(ServletRequestUtils.getStringParameter(request, "action", ""))) {
			tambah = 30;
		}
		dari += tambah;
		sampai += tambah;
		
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<Policy> daftarPolis = 
			("SSS".equals(currentUser.getCab_bank().trim()) || currentUser.getLde_id().equals("11")) ? 
					bacManager.selectDaftarPolisOtorisasiUwSimasPrimaUW(dari, sampai) : 
						elionsManager.selectDaftarPolisOtorisasiBankSinarmas(Integer.valueOf(currentUser.getLus_id()), dari, sampai);
		m.put("daftarPolis", daftarPolis);
		Integer row= daftarPolis.size();
		int myself=1;
//		row=80;
		myself += tambah;
		Integer page = sampai/30;
		
		m.put("myself", myself);
		m.put("page", page);
		
		if(request.getParameter("search")!=null){
			if("SSS".equals(user.getCab_bank().trim()) || user.getJn_bank().intValue() == 3) {
				if (kata==null){
					daftarPolis = 
						("SSS".equals(currentUser.getCab_bank().trim()) || currentUser.getLde_id().equals("11")) ? 
								bacManager.selectDaftarPolisOtorisasiUwSimasPrimaUW(dari, sampai) : 
									elionsManager.selectDaftarPolisOtorisasiBankSinarmas(Integer.valueOf(currentUser.getLus_id()), dari, sampai);
					m.put("daftarPolis", daftarPolis);

				}else
				m.put("daftarPolis", 
						elionsManager.selectFilterOtorisasi(tipe, kata, pilter,jn_bank));
			}else{
				if (kata==null){
					daftarPolis = 
						("SSS".equals(currentUser.getCab_bank().trim()) || currentUser.getLde_id().equals("11")) ? 
								bacManager.selectDaftarPolisOtorisasiUwSimasPrimaUW(dari, sampai) : 
									elionsManager.selectDaftarPolisOtorisasiBankSinarmas(Integer.valueOf(currentUser.getLus_id()), dari, sampai);
					m.put("daftarPolis", daftarPolis);
				}else
					m.put("daftarPolis", 
							bacManager.selectFilterOtorisasiHPlusSatu(tipe, kata, pilter,jn_bank));
			}
		}
		
		if(request.getParameter("proses") != null) {
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			Pemegang pmg=elionsManager.selectpp(spaj);
	    	Tertanggung tertanggung =elionsManager.selectttg(spaj);
			if( spaj.equals("")) {
				m.put("pesanError", "Harap Pilih Nomor Register.");
			}else {
				Map proses = bacManager.ProsesAutoAcceptHPlusSatu(spaj, 0,pmg,tertanggung,currentUser,request);
				m.put("pesanError", proses);
			}
		}
		
		m.put("dari", dari);
		m.put("sampai", sampai);
		
		return new ModelAndView("uw/autoproses", m);
	}
	
	
	/**
	  Lufi
	 
	 * 
	 * View Kesehatan DMTM
	 */
	public ModelAndView view_kesehatanDMTM(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();		
		User currentUser = (User) request.getSession().getAttribute("currentUser");			
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj");
		int json = ServletRequestUtils.getIntParameter(request, "json", 0);
		int flag_jenis_peserta = ServletRequestUtils.getIntParameter(request, "fjp",0);
		int lsbs = ServletRequestUtils.getIntParameter(request, "lsbs",0);
		
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(spaj);
		
		if(mspo_flag_spaj==null){
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('Data tidak dapat ditampilkan');window.close();</script>");
			sos.close();
			return null;
		}
		
		List<DropDown> daftarPeserta = new ArrayList<DropDown>();	
		List listjawaban=new ArrayList();
		if(json==0){
			if(lsbs==163 || lsbs==169 || lsbs == 173 || lsbs==197 || lsbs==212 || lsbs==73 || lsbs==203 || lsbs == 208){
				daftarPeserta.add(0, new DropDown("0", "TERTANGGUNG UTAMA"));
				daftarPeserta.add(0, new DropDown("-1", "PEMEGANG POLIS")); // pemegang polis untuk dmtm patar timotius
			}else if(lsbs==225){
				daftarPeserta=bacManager.selectDropDownDaftarPesertaSMP(spaj)	;
			}else{
				daftarPeserta=bacManager.selectDropDownDaftarPeserta(spaj)	;
			}
			daftarPeserta.add(0, new DropDown("10", "-----------PILIH PESERTA----------"));//10 buat key aja 		
		}
		if(json==1){
			if(mspo_flag_spaj.equals("4")){
				if(lsbs==225){
					listjawaban=bacManager.selectJawabanMedicalsSIOtambahan(spaj,flag_jenis_peserta);
				}else{
					listjawaban=bacManager.selectJawabanMedicalsSIOtambahan(spaj,flag_jenis_peserta+2);
				}
			}else{
				listjawaban=bacManager.selectJawabanMedical(spaj,flag_jenis_peserta);
			}
			response.setContentType("application/json");
	    	PrintWriter out = response.getWriter();
	    	Gson gson = new Gson();
	    	out.print(gson.toJson(listjawaban));
	    	out.close();
	    	return null;
		}
		map.put("daftarPeserta", daftarPeserta);
		map.put("spaj", spaj);
		map.put("lsbs", lsbs);
		
		if(mspo_flag_spaj.equals("4")){
			return new ModelAndView("uw/view_kesehatanDMTM_SIO",map);
		}else{
			if(lsbs!=163){
				return new ModelAndView("uw/view_kesehatanDMTM",map);
			}else{
				return new ModelAndView("uw/view_kesehatanDS",map);
			}
		}
	}	

	/**
	 * @author  : Canpri
	 * @created : 3 Mar 2014
	 * 
	 * @beans Permintaan Virtual Account
	 * @see param format konvensional = 8006XZYYMMCCCCCC, syariah = 8076XZYYMMCCCCCC
	 * @see param X -> 0 = link, 1 = Nonlink 
	 * @see param  Z -> 0 = kertas, 1 = online
	 * @see param  YY -> tahun
	 * @see param  MM -> Jenis Spaj
	 * @see param  CCCCCC -> 6 digit terakhir nomor urut virtual account
	 * 
	 * @link http://localhost/E-Lions/uw/uw.htm?window=permintaan_va
	 */
	
	public ModelAndView permintaan_va(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		VirtualAccount va = new VirtualAccount();
		
		HashMap map = new HashMap();
		String pesan = "";
		
		HashMap paper_ul = (HashMap) elionsManager.selectMstCounter(171, "01");
		HashMap paper_nul = (HashMap) elionsManager.selectMstCounter(177, "01");
		HashMap paper_simpol = (HashMap) elionsManager.selectMstCounter(179, "01");
		HashMap paper_sprima = (HashMap) elionsManager.selectMstCounter(181, "01");
		HashMap paper_psave = (HashMap) elionsManager.selectMstCounter(183, "01");
		HashMap paper_slinksatu = (HashMap) elionsManager.selectMstCounter(185, "01");
		HashMap paper_kcl = (HashMap) elionsManager.selectMstCounter(187, "01");
		HashMap paper_familyplan = (HashMap) elionsManager.selectMstCounter(189, "01");
		HashMap paper_harda = (HashMap) elionsManager.selectMstCounter(213, "01");
		HashMap paper_smultimate = (HashMap) elionsManager.selectMstCounter(224, "01");
		HashMap paper_btn_syariah_life = (HashMap) elionsManager.selectMstCounter(230, "01");
		HashMap paper_btn_syariah_link = (HashMap) elionsManager.selectMstCounter(242, "01"); //change from 231 to 242

		//tambahan
		HashMap paper_ul_syariah = (HashMap) elionsManager.selectMstCounter(198, "01");
		HashMap paper_nul_syariah = (HashMap) elionsManager.selectMstCounter(240, "01"); //change from 200 to 240
		HashMap paper_simpol_syariah = (HashMap) elionsManager.selectMstCounter(202, "01");
		HashMap paper_powersave_syariah = (HashMap) elionsManager.selectMstCounter(241, "01"); //change from 204 to 241
		HashMap paper_psave_syariah = (HashMap) elionsManager.selectMstCounter(206, "01");
		HashMap paper_kcl_syariah = (HashMap) elionsManager.selectMstCounter(208, "01");
		HashMap paper_danamas_prima = (HashMap) elionsManager.selectMstCounter(210, "01");
		HashMap paper_primemagna = (HashMap) elionsManager.selectMstCounter(238, "01"); //change from 215 to 238
		
		HashMap gadget_ul = (HashMap) elionsManager.selectMstCounter(172, "01");
		HashMap gadget_nul = (HashMap) elionsManager.selectMstCounter(178, "01");
		HashMap gadget_simpol = (HashMap) elionsManager.selectMstCounter(180, "01");
		HashMap gadget_sprima = (HashMap) elionsManager.selectMstCounter(182, "01");
		HashMap gadget_psave = (HashMap) elionsManager.selectMstCounter(184, "01");
		HashMap gadget_slinksatu = (HashMap) elionsManager.selectMstCounter(186, "01");
		HashMap gadget_kcl = (HashMap) elionsManager.selectMstCounter(188, "01");
		HashMap gadget_familyplan = (HashMap) elionsManager.selectMstCounter(190, "01");
		HashMap gadget_smultimate = (HashMap) elionsManager.selectMstCounter(223, "01");
		
		//tambahan
		HashMap gadget_ul_syariah = (HashMap) elionsManager.selectMstCounter(239, "01"); //change from 199 to 239
		HashMap gadget_nul_syariah = (HashMap) elionsManager.selectMstCounter(201, "01");
		HashMap gadget_simpol_syariah = (HashMap) elionsManager.selectMstCounter(203, "01");
		HashMap gadget_powersave_syariah = (HashMap) elionsManager.selectMstCounter(205, "01");
		HashMap gadget_psave_syariah = (HashMap) elionsManager.selectMstCounter(207, "01");
		HashMap gadget_kcl_syariah = (HashMap) elionsManager.selectMstCounter(209, "01");
		HashMap gadget_danamas_prima = (HashMap) elionsManager.selectMstCounter(211, "01");
		
		Integer paper_ul_val = ((Double)paper_ul.get("MSCO_VALUE")).intValue();
		Integer paper_nul_val = ((Double)paper_nul.get("MSCO_VALUE")).intValue();
		Integer paper_simpol_val = ((Double)paper_simpol.get("MSCO_VALUE")).intValue();
		Integer paper_sprima_val = ((Double)paper_sprima.get("MSCO_VALUE")).intValue();
		Integer paper_psave_val = ((Double)paper_psave.get("MSCO_VALUE")).intValue();
		Integer paper_slinksatu_val = ((Double)paper_slinksatu.get("MSCO_VALUE")).intValue();
		Integer paper_kcl_val = ((Double)paper_kcl.get("MSCO_VALUE")).intValue();
		Integer paper_familyplan_val = ((Double)paper_familyplan.get("MSCO_VALUE")).intValue();
		Integer paper_harda_val = ((Double)paper_harda.get("MSCO_VALUE")).intValue();
		Integer paper_smultimate_val = ((Double)paper_smultimate.get("MSCO_VALUE")).intValue();
		Integer paper_btn_syariah_life_val = ((Double)paper_btn_syariah_life.get("MSCO_VALUE")).intValue();
		Integer paper_btn_syariah_link_val = ((Double)paper_btn_syariah_link.get("MSCO_VALUE")).intValue();

		//tambahan
		Integer paper_ul_syariah_val = ((Double)paper_ul_syariah.get("MSCO_VALUE")).intValue();
		Integer paper_nul_syariah_val = ((Double)paper_nul_syariah.get("MSCO_VALUE")).intValue();
		Integer paper_simpol_syariah_val = ((Double)paper_simpol_syariah.get("MSCO_VALUE")).intValue();
		Integer paper_powersave_syariah_val = ((Double)paper_powersave_syariah.get("MSCO_VALUE")).intValue();
		Integer paper_psave_syariah_val = ((Double)paper_psave_syariah.get("MSCO_VALUE")).intValue();
		Integer paper_kcl_syariah_val = ((Double)paper_kcl_syariah.get("MSCO_VALUE")).intValue();
		Integer paper_danamas_prima_val = ((Double)paper_danamas_prima.get("MSCO_VALUE")).intValue();
		Integer paper_primemagna_val = ((Double)paper_primemagna.get("MSCO_VALUE")).intValue();
		
		Integer gadget_ul_val = ((Double)gadget_ul.get("MSCO_VALUE")).intValue();
		Integer gadget_nul_val = ((Double)gadget_nul.get("MSCO_VALUE")).intValue();
		Integer gadget_simpol_val = ((Double)gadget_simpol.get("MSCO_VALUE")).intValue();
		Integer gadget_sprima_val = ((Double)gadget_sprima.get("MSCO_VALUE")).intValue();
		Integer gadget_psave_val = ((Double)gadget_psave.get("MSCO_VALUE")).intValue();
		Integer gadget_slinksatu_val = ((Double)gadget_slinksatu.get("MSCO_VALUE")).intValue();
		Integer gadget_kcl_val = ((Double)gadget_kcl.get("MSCO_VALUE")).intValue();
		Integer gadget_familyplan_val = ((Double)gadget_familyplan.get("MSCO_VALUE")).intValue();
		Integer gadget_smultimate_val = ((Double)gadget_smultimate.get("MSCO_VALUE")).intValue();
		
		//tambahan
		Integer gadget_ul_syariah_val = ((Double)gadget_ul_syariah.get("MSCO_VALUE")).intValue();
		Integer gadget_nul_syariah_val = ((Double)gadget_nul_syariah.get("MSCO_VALUE")).intValue();
		Integer gadget_simpol_syariah_val = ((Double)gadget_simpol_syariah.get("MSCO_VALUE")).intValue();
		Integer gadget_powersave_syariah_val = ((Double)gadget_powersave_syariah.get("MSCO_VALUE")).intValue();
		Integer gadget_psave_syariah_val = ((Double)gadget_psave_syariah.get("MSCO_VALUE")).intValue();
		Integer gadget_kcl_syariah_val = ((Double)gadget_kcl_syariah.get("MSCO_VALUE")).intValue();
		Integer gadget_danamas_prima_val = ((Double)gadget_danamas_prima.get("MSCO_VALUE")).intValue();
		
		//penambahan
		HashMap l_konven = (HashMap) elionsManager.selectMstCounter(192, "01");
		HashMap lainnya = (HashMap) elionsManager.selectMstCounter(193, "01");
		
		Integer l_konven_val = ((Double)l_konven.get("MSCO_VALUE")).intValue();
		Integer lainnya_val = ((Double)lainnya.get("MSCO_VALUE")).intValue();
		//end
		
		if(request.getParameter("btnSimpan") != null) {
//			String tgl = ServletRequestUtils.getStringParameter(request, "tgl", defaultDateFormat.format(elionsManager.selectSysdate()));
			String tgl = ServletRequestUtils.getStringParameter(request, "tgl", defaultDateFormat.format(new Date()));
			Date tgl_req = defaultDateFormat.parse(tgl);
			
			va.setJenis_va(ServletRequestUtils.getIntParameter(request, "jn_va"));//0=kertas, 1=online
			va.setJenis_syariah(ServletRequestUtils.getIntParameter(request, "jn_produk"));//0=konvensional, 1=syariah
			va.setJenis_link(ServletRequestUtils.getIntParameter(request, "jn_link"));//0=link, 1=non link
			va.setJenis_spaj(ServletRequestUtils.getStringParameter(request, "jn_spaj"));
			va.setTgl(tgl_req);
			va.setMsv_amount_req(ServletRequestUtils.getStringParameter(request, "amount_req", "0"));
			va.setStart_no_va_req(ServletRequestUtils.getStringParameter(request, "start_no_va_req",""));
			va.setEnd_no_va_req(ServletRequestUtils.getStringParameter(request, "end_no_va_req",""));
			va.setStart_no_va_cetak(ServletRequestUtils.getStringParameter(request, "start_no_va_cetak",""));
			va.setEnd_no_va_cetak(ServletRequestUtils.getStringParameter(request, "end_no_va_cetak",""));
			va.setJ_bank(ServletRequestUtils.getStringParameter(request,"jbank",""));

			if(va.getJenis_va().equals(1) && va.getJ_bank().equals("161")){
				va.setFlag_active(1);
			}
			
			pesan = bacManager.prosesPermintaanVA(va, currentUser);
			
			paper_ul = (HashMap) elionsManager.selectMstCounter(171, "01");
			paper_nul = (HashMap) elionsManager.selectMstCounter(177, "01");
			paper_simpol = (HashMap) elionsManager.selectMstCounter(179, "01");
			paper_sprima = (HashMap) elionsManager.selectMstCounter(181, "01");
			paper_psave = (HashMap) elionsManager.selectMstCounter(183, "01");
			paper_slinksatu = (HashMap) elionsManager.selectMstCounter(185, "01");
			paper_kcl = (HashMap) elionsManager.selectMstCounter(187, "01");
			paper_familyplan = (HashMap) elionsManager.selectMstCounter(189, "01");
		    paper_harda = (HashMap) elionsManager.selectMstCounter(213, "01");
		    paper_smultimate = (HashMap) elionsManager.selectMstCounter(224, "01");
		    paper_btn_syariah_life = (HashMap) elionsManager.selectMstCounter(230, "01");
		    paper_btn_syariah_link = (HashMap) elionsManager.selectMstCounter(242, "01"); //change from 231 to 242
		    
			//tambahan
			paper_ul_syariah = (HashMap) elionsManager.selectMstCounter(198, "01");
			paper_nul_syariah = (HashMap) elionsManager.selectMstCounter(240, "01"); //change from 200 to 240
			paper_simpol_syariah = (HashMap) elionsManager.selectMstCounter(202, "01");
			paper_powersave_syariah = (HashMap) elionsManager.selectMstCounter(241, "01"); //change from 204 to 241
			paper_psave_syariah = (HashMap) elionsManager.selectMstCounter(206, "01");
			paper_kcl_syariah = (HashMap) elionsManager.selectMstCounter(208, "01");
			paper_danamas_prima = (HashMap) elionsManager.selectMstCounter(210, "01");
			paper_primemagna = (HashMap) elionsManager.selectMstCounter(238, "01"); //change from 215 to 238
			
			gadget_ul = (HashMap) elionsManager.selectMstCounter(172, "01");
			gadget_nul = (HashMap) elionsManager.selectMstCounter(178, "01");
			gadget_simpol = (HashMap) elionsManager.selectMstCounter(180, "01");
			gadget_sprima = (HashMap) elionsManager.selectMstCounter(182, "01");
			gadget_psave = (HashMap) elionsManager.selectMstCounter(184, "01");
			gadget_slinksatu = (HashMap) elionsManager.selectMstCounter(186, "01");
			gadget_kcl = (HashMap) elionsManager.selectMstCounter(188, "01");
			gadget_familyplan = (HashMap) elionsManager.selectMstCounter(190, "01");
			gadget_smultimate = (HashMap) elionsManager.selectMstCounter(223, "01");
			//tambahan
			gadget_ul_syariah = (HashMap) elionsManager.selectMstCounter(239, "01"); //change from 199 to 239
			gadget_nul_syariah = (HashMap) elionsManager.selectMstCounter(201, "01");
			gadget_simpol_syariah = (HashMap) elionsManager.selectMstCounter(203, "01");
			gadget_powersave_syariah = (HashMap) elionsManager.selectMstCounter(205, "01");
			gadget_psave_syariah = (HashMap) elionsManager.selectMstCounter(207, "01");
			gadget_kcl_syariah = (HashMap) elionsManager.selectMstCounter(209, "01");
			gadget_danamas_prima = (HashMap) elionsManager.selectMstCounter(211, "01");
			
			paper_ul_val = ((Double)paper_ul.get("MSCO_VALUE")).intValue();
			paper_nul_val = ((Double)paper_nul.get("MSCO_VALUE")).intValue();
			paper_simpol_val = ((Double)paper_simpol.get("MSCO_VALUE")).intValue();
			paper_sprima_val = ((Double)paper_sprima.get("MSCO_VALUE")).intValue();
			paper_psave_val = ((Double)paper_psave.get("MSCO_VALUE")).intValue();
			paper_slinksatu_val = ((Double)paper_slinksatu.get("MSCO_VALUE")).intValue();
			paper_kcl_val = ((Double)paper_kcl.get("MSCO_VALUE")).intValue();
			paper_familyplan_val = ((Double)paper_familyplan.get("MSCO_VALUE")).intValue();
			paper_harda_val = ((Double)paper_harda.get("MSCO_VALUE")).intValue();
			paper_smultimate_val = ((Double)paper_smultimate.get("MSCO_VALUE")).intValue();
			paper_btn_syariah_life_val = ((Double)paper_btn_syariah_life.get("MSCO_VALUE")).intValue();
			paper_btn_syariah_link_val = ((Double)paper_btn_syariah_link.get("MSCO_VALUE")).intValue();
			//tambahan
			paper_ul_syariah_val = ((Double)paper_ul_syariah.get("MSCO_VALUE")).intValue();
			paper_nul_syariah_val = ((Double)paper_nul_syariah.get("MSCO_VALUE")).intValue();
			paper_simpol_syariah_val = ((Double)paper_simpol_syariah.get("MSCO_VALUE")).intValue();
			paper_powersave_syariah_val = ((Double)paper_powersave_syariah.get("MSCO_VALUE")).intValue();
			paper_psave_syariah_val = ((Double)paper_psave_syariah.get("MSCO_VALUE")).intValue();
			paper_kcl_syariah_val = ((Double)paper_kcl_syariah.get("MSCO_VALUE")).intValue();
			paper_danamas_prima_val = ((Double)paper_danamas_prima.get("MSCO_VALUE")).intValue();
			paper_primemagna_val = ((Double)paper_primemagna.get("MSCO_VALUE")).intValue();
			
			gadget_ul_val = ((Double)gadget_ul.get("MSCO_VALUE")).intValue();
			gadget_nul_val = ((Double)gadget_nul.get("MSCO_VALUE")).intValue();
			gadget_simpol_val = ((Double)gadget_simpol.get("MSCO_VALUE")).intValue();
			gadget_sprima_val = ((Double)gadget_sprima.get("MSCO_VALUE")).intValue();
			gadget_psave_val = ((Double)gadget_psave.get("MSCO_VALUE")).intValue();
			gadget_slinksatu_val = ((Double)gadget_slinksatu.get("MSCO_VALUE")).intValue();
			gadget_kcl_val = ((Double)gadget_kcl.get("MSCO_VALUE")).intValue();
			gadget_familyplan_val = ((Double)gadget_familyplan.get("MSCO_VALUE")).intValue();
			gadget_smultimate_val = ((Double)gadget_smultimate.get("MSCO_VALUE")).intValue();
			
			//tambahan
			gadget_ul_syariah_val = ((Double)gadget_ul_syariah.get("MSCO_VALUE")).intValue();
			gadget_nul_syariah_val = ((Double)gadget_nul_syariah.get("MSCO_VALUE")).intValue();
			gadget_simpol_syariah_val = ((Double)gadget_simpol_syariah.get("MSCO_VALUE")).intValue();
			gadget_powersave_syariah_val = ((Double)gadget_powersave_syariah.get("MSCO_VALUE")).intValue();
			gadget_psave_syariah_val = ((Double)gadget_psave_syariah.get("MSCO_VALUE")).intValue();
			gadget_kcl_syariah_val = ((Double)gadget_kcl_syariah.get("MSCO_VALUE")).intValue();
			gadget_danamas_prima_val = ((Double)gadget_danamas_prima.get("MSCO_VALUE")).intValue();
			
			
			//penambahan
			l_konven = (HashMap) elionsManager.selectMstCounter(192, "01");
			lainnya = (HashMap) elionsManager.selectMstCounter(193, "01");
			
			l_konven_val = ((Double)l_konven.get("MSCO_VALUE")).intValue();
			lainnya_val = ((Double)lainnya.get("MSCO_VALUE")).intValue();
			//end
		}
		
		if(request.getParameter("btnShow") != null) {
			String msv_id = ServletRequestUtils.getStringParameter(request, "msv_id","");
			ArrayList l_msv = Common.serializableList(bacManager.selectPermintaanVA(msv_id));
			
			map.put("l_msv", l_msv);
			map.put("msv_id", msv_id);
			
			if(request.getParameter("btnExcel") != null) {
				if(l_msv.size() > 0){ //bila ada data
	    			ServletOutputStream sos = response.getOutputStream();
	    			File sourceFile = Resources.getResourceAsFile(props.getProperty("report.uw.permintaan.va") + ".jasper");
		    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

		    		Map<String, Object> params = new HashMap<String, Object>();
		    		params.put("msv_id", msv_id);
		    		params.put("user", currentUser.getLus_full_name());

		    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(l_msv));

		    		response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
		            exporter.exportReport();
		            
		    		sos.close();
	    		}else{ //bila tidak ada data
	    			ServletOutputStream sos = response.getOutputStream();
	    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
	    			sos.close();
	    		}
				return null;
			}
		}
		
		List<DropDown> jb = bacManager.selectJenisBank(va.getJ_bank());
		map.put("jbank", jb);
				
		List<DropDown> id = elionsManager.selectDropDown("eka.mst_va", "msv_id", "msv_id", "", "msv_id desc", null);
		
 		map.put("pesan", pesan);
//		map.put("tgl", defaultDateFormat.format(elionsManager.selectSysdate()));
		map.put("tgl", defaultDateFormat.format(new Date()));
		map.put("id", id);
		
		map.put("paper_ul", paper_ul_val);
		map.put("paper_nul", paper_nul_val);
		map.put("paper_simpol", paper_simpol_val);
		map.put("paper_sprima", paper_sprima_val);
		map.put("paper_psave", paper_psave_val);
		map.put("paper_slinksatu", paper_slinksatu_val);
		map.put("paper_kcl", paper_kcl_val);
		map.put("paper_harda", paper_harda_val);
		map.put("paper_familyplan", paper_familyplan_val);
		map.put("paper_primemagna", paper_primemagna_val);
		map.put("paper_smultimate", paper_smultimate_val);
		map.put("paper_btn_syariah_life", paper_btn_syariah_life_val);
		map.put("paper_btn_syariah_link", paper_btn_syariah_link_val);
		//tambahan
		map.put("paper_ul_syariah", paper_ul_syariah_val);
		map.put("paper_nul_syariah", paper_nul_syariah_val);
		map.put("paper_simpol_syariah", paper_simpol_syariah_val);
		map.put("paper_powersave_syariah", paper_powersave_syariah_val);
		map.put("paper_psave_syariah", paper_psave_syariah_val);
		map.put("paper_kcl_syariah", paper_kcl_syariah_val);
		map.put("paper_danamas_prima", paper_danamas_prima_val);
		
		map.put("gadget_ul", gadget_ul_val);
		map.put("gadget_nul", gadget_nul_val);
		map.put("gadget_simpol", gadget_simpol_val);
		map.put("gadget_sprima", gadget_sprima_val);
		map.put("gadget_psave", gadget_psave_val);
		map.put("gadget_slinksatu", gadget_slinksatu_val);
		map.put("gadget_kcl", gadget_kcl_val);
		map.put("gadget_familyplan", gadget_familyplan_val);
		map.put("gadget_smultimate", gadget_smultimate_val);
		//tambahan
		map.put("gadget_ul_syariah", gadget_ul_syariah_val);
		map.put("gadget_nul_syariah", gadget_nul_syariah_val);
		map.put("gadget_simpol_syariah", gadget_simpol_syariah_val);
		map.put("gadget_powersave_syariah", gadget_powersave_syariah_val);
		map.put("gadget_psave_syariah", gadget_psave_syariah_val);
		map.put("gadget_kcl_syariah", gadget_kcl_syariah_val);
		map.put("gadget_danamas_prima", gadget_danamas_prima_val);
		
		//penambahan
		map.put("l_konven", l_konven_val);
		map.put("lainnya", lainnya_val);
		//end
		
		/**
		 * Tab Detail VA
		 * req Dewi Andriyati (#84598)
		 * @author Daru
		 * @since Mar 29, 2016
		 */
//		String sysdate = defaultDateFormat.format(elionsManager.selectSysdate());
		String sysdate = defaultDateFormat.format(new Date());
		String detva_tgl1 = ServletRequestUtils.getStringParameter(request, "detva_tgl1", sysdate);
		String detva_tgl2 = ServletRequestUtils.getStringParameter(request, "detva_tgl2", sysdate);
		String detva_no = ServletRequestUtils.getStringParameter(request, "detva_no", null);
		
		map.put("detva_tgl1", detva_tgl1);
		map.put("detva_tgl2", detva_tgl2);
		map.put("detva_no", detva_no);
		
		if(request.getParameter("detva_search") != null) {
			map.put("listPermintaanVa", bacManager.selectDetailPermintaanVa(detva_no, detva_tgl1, detva_tgl2));
			map.put("showTab", "tab-3");
		}
		
		return new ModelAndView("uw/permintaan_va",map);
	}
	
	//Method untuk aksep Email Blast Dari Avnel - Ryan	
	public ModelAndView aksepEmailBlast(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String polis = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String noE = ServletRequestUtils.getStringParameter(request, "noE", "");
		String flag = ServletRequestUtils.getStringParameter(request,"flag","");
		String sts = "0";
		String spaj = elionsManager.selectSpajFromPolis(polis);
		
		if (flag.equals("3")){
			sts=ServletRequestUtils.getRequiredStringParameter(request,"sts");
		}
		
		if(flag.equals("5")){
//			Date dt_now = this.elionsManager.selectSysdate();
			Date dt_now = new Date();
			Map m_pmg = new HashMap();
			Pemegang cls_pmg=elionsManager.selectpp(spaj);
			m_pmg.put("pemegang", cls_pmg);
			m_pmg.put("dt_now", dt_now);
			return new ModelAndView("uw/ubahalamat",m_pmg);
		}
		
		if(flag.equals("6")){
			spaj=ServletRequestUtils.getStringParameter(request, "no_spaj", "");
			String newTelp=ServletRequestUtils.getStringParameter(request, "newTelp", "");
			String newTelpWork=ServletRequestUtils.getStringParameter(request, "newTelp", "");
			String newAddress=ServletRequestUtils.getStringParameter(request, "address", "");
			String hpNew=ServletRequestUtils.getStringParameter(request, "hpNew", "");
			String email=ServletRequestUtils.getStringParameter(request, "email", "");
			//bacManager.prosesEditAlamatNasabah(spaj, currentUser, newAddress,newTelp,newTelpWork , hpNew, email);
		}
		
	//	bacManager.emailboosters(spaj, Integer.parseInt(flag), noE, Integer.parseInt(sts), "");
		ServletOutputStream sos = response.getOutputStream();
    	sos.println("<script>alert('Email Berhasil Dikirim');window.close();</script>");
    	sos.close();
    	request.getSession().removeAttribute("currentUser");
		return null;
	}
	
	//Method untuk aksep Email Blast (New) Dari Avnel - MANTA	
	public ModelAndView aksepEmailBlastNew(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		String s_id1 = ServletRequestUtils.getStringParameter(request, "id1", "");
		String s_id2 = ServletRequestUtils.getStringParameter(request, "id2", "");
		String[] sa_param;
		String s_spaj = "";
		String s_flag = "0";
		String s_noEndors = "";
		String s_status = "0";
		String s_option = "";
		String s_pesan = "";
		
		try{
			HashMap hm_data = new HashMap();
			hm_data = bacManager.selectMstUrlSecure(s_id1, s_id2);
			
			if(hm_data.size() > 0){
				s_spaj = elionsManager.selectSpajFromPolis(hm_data.get("KEY_ID").toString());
				
				if(!s_spaj.equals("")){
					sa_param = hm_data.get("LINK").toString().split("~");
					s_flag = sa_param[0].replace("flag=", "");
					
					if(s_flag.equals("1")){
						s_option = sa_param[1].replace("ans=", "");
					}
					
					if(s_flag.equals("3")){
						
					}
					
					if(s_flag.equals("5")){
						HashMap hm_prm = new HashMap();
//						Date dt_now = this.elionsManager.selectSysdate();
						Date dt_now = new Date();
						Pemegang cls_pmg = elionsManager.selectpp(s_spaj);
						hm_prm.put("pemegang", cls_pmg);
						hm_prm.put("dt_now", dt_now);
						String s_urlid = bacManager.selectSeqUrlSecureId();
						bacManager.insertMstUrlSecure(s_urlid, "aksepEmailBlast", hm_data.get("KEY_ID").toString(), "flag=6");
						HashMap hm_url = new HashMap();
						hm_url = bacManager.selectMstUrlSecure2(hm_data.get("KEY_ID").toString(), "flag=6");
						hm_prm.put("id1", hm_url.get("ID_ENCRYPT_1").toString());
						hm_prm.put("id2", hm_url.get("ID_ENCRYPT_2").toString());
						//bacManager.updateMstUrlSecure(hm_data.get("KEY_ID").toString(), s_flag, hm_data.get("LINK").toString());
						return new ModelAndView("uw/ubahalamat", hm_prm);
					}
					
					if(s_flag.equals("6")){
						String newAddress = ServletRequestUtils.getStringParameter(request, "newAddress", "");
						String newKdPos = ServletRequestUtils.getStringParameter(request, "newKdPos", "");
						String newTelp = ServletRequestUtils.getStringParameter(request, "newTelp", "");
						String newWorkTelp = ServletRequestUtils.getStringParameter(request, "newWorkTelp", "");
						String hpNew1 = ServletRequestUtils.getStringParameter(request, "hpNew", "");
						String hpNew2 = ServletRequestUtils.getStringParameter(request, "hpNew2", "");
						if(!newAddress.equals("")){
							bacManager.prosesEditAlamatNasabah(s_spaj, currentUser, newAddress, newKdPos, newTelp, newWorkTelp, hpNew1, hpNew2);
						}else{
							s_pesan = "<script>alert('Proses Konfirmasi Gagal Dilakukan');window.close();</script>";
						}
					}
					
					bacManager.emailboosters(s_spaj, Integer.parseInt(s_flag), s_noEndors, Integer.parseInt(s_status), s_option);
					bacManager.deleteMstUrlSecure(hm_data.get("KEY_ID").toString(), s_flag);
			    	if(s_pesan==""){
						s_pesan = "<script>alert('Proses Konfirmasi Berhasil Dilakukan');window.close();</script>";	
			    	}
				}else{
					s_pesan = "<script>alert('Proses Konfirmasi Gagal, Polis Tidak Terdaftar');window.close();</script>";
				}
			}else{
				s_pesan = "<script>alert('Proses Konfirmasi Gagal, LINK Tersebut Sudah Tidak Valid');window.close();</script>";
			}
		}catch (Exception e) {
			logger.error("ERROR :", e);
			s_pesan = "<script>alert('Proses Konfirmasi Gagal');window.close();</script>";
		}
		ServletOutputStream sos = response.getOutputStream();
		sos.println(s_pesan);
    	sos.close();
    	request.getSession().removeAttribute("currentUser");
		return null;
	}
	
	public void emailAuto(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();		
		User currentUser = (User) request.getSession().getAttribute("currentUser");			
		String spaj=ServletRequestUtils.getStringParameter(request, "l");
		int json = ServletRequestUtils.getIntParameter(request, "json", 0);
		int jsonStatus = ServletRequestUtils.getIntParameter(request, "jsonStatus", 0);
		List<DropDown> listEmail = new ArrayList<DropDown>();
		List<DropDown> listsubstatus = new ArrayList<DropDown>();
		if(json==1){
				 String[] to= spaj.split(";");
				 String to2=null;
				 for(int i=0;i<to.length;i++){
					logger.info(to[i]);	
					if(i+1==to.length)to2=to[i];
				 }
				 listEmail=bacManager.selectEmailAutoComplete(to2);
				 response.setContentType("application/json");
		    	 PrintWriter out = response.getWriter();
		    	 Gson gson = new Gson();
		    	 out.print(gson.toJson(listEmail));
		    	 out.close();
			
	    } 
		if(jsonStatus==1){
			 listsubstatus=ajaxManager.selectLstStatusAcceptSub(spaj);
			 response.setContentType("application/json");
	    	 PrintWriter out = response.getWriter();
	    	 Gson gson = new Gson();
	    	 out.print(gson.toJson(listsubstatus));
	    	 out.close();
		
		}
		
	}
	/**
	  Lufi
	 
	 * 
	 *Auto Payment DMTM 
	 */
	public ModelAndView autopayment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		HttpSession session=request.getSession();
		Map map3 = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");			
		ArrayList dataSpaj=new ArrayList();
		ArrayList dataSpajAutoPayment=new ArrayList();
	
//		Date sysDate = elionsManager.selectSysdate();
		dataSpaj=bacManager.selectDataSpajDmtmdiPayment();
		String status="";
		
		if(request.getParameter("proses") != null){
			  String check[] = request.getParameterValues("chbox");
			  if(check != null){ 
				  for(int i=0;i<check.length;i++){
					  ArrayList billInfo=new ArrayList();
					  TopUp topup = new TopUp();
					  String data = check[i];
					  String spaj = data;
					  billInfo = Common.serializableList(this.elionsManager.selectBillingInformation(spaj, 1, 1));//premiKe sama tahunKe diset 1
					  Map tmp = (HashMap) billInfo.get(0);
					  ArrayList<Payment> paymentInfo = Common.serializableList(this.elionsManager.selectPaymentCount(spaj, 1, 1));
					  topup.setReg_spaj(spaj);
					  topup.setActionTypeForDrekDet("insert");
					  Payment p = new Payment();
					  p.setKe(paymentInfo.size());
					  p.setTipe("Penambahan");
					  paymentInfo.add(p);
					  topup.setMspa_pay_date((Date) tmp.get("MSBI_BEG_DATE"));
					  topup.setLku_id((String) tmp.get("LKU_ID"));
				      topup.setBill_lku_id((String) tmp.get("LKU_ID"));
					  topup.setMspa_payment(new Double(((BigDecimal) tmp.get("SISA")).doubleValue()));						
					  String lca_id = (String) tmp.get("LCA_ID");
					  topup.setLca_polis(lca_id);
					  String lsbs = uwManager.selectBusinessId(topup.getReg_spaj());
					  Account_recur account_recur = elionsManager.select_account_recur(topup.getReg_spaj());
					  Integer i_flagCC=elionsManager.select_flag_cc(topup.getReg_spaj());
				      if(i_flagCC==1 || i_flagCC==2){					    
							if(account_recur!=null)topup.setMspa_no_rek(account_recur.getMar_acc_no());
				      }				     
					  HashMap drekCC=Common.serializableMap(bacManager.selectDataFromDrekCC(topup.getReg_spaj()));					  
					  if(drekCC!=null){	
						  String no_trx = (String)drekCC.get("NO_TRX");
						  Double net_bayar = ((BigDecimal)drekCC.get("NET_BAYAR")).doubleValue();					
						  Double sisa_saldo = uwManager.selectCheckTotalUsedMstDrek(no_trx, null, null, null);
						  if(net_bayar<=sisa_saldo){
							  topup.setLsjb_id(3);
							  topup.setLsrek_id(((BigDecimal)drekCC.get("LSREK_ID")).toString());
							  topup.setMspa_active(1);
							  topup.setMspa_nilai_kurs(((BigDecimal)drekCC.get("NILAI_KURS")).doubleValue());
							  topup.setMspa_desc("CEK I-BANK(No Transaksi "+no_trx+")");
							  topup.setNo_trx((String)drekCC.get("NO_TRX"));
							  topup.setMspa_payment(net_bayar);
							  topup.setMspa_no_rek((String)drekCC.get("NO_KARTU_KREDIT"));
							  topup.setMspa_date_book((Date) drekCC.get("TGL_TRX"));
							  topup.setPay_date((Date) tmp.get("MSBI_BEG_DATE"));
							  topup.setLus_id(currentUser.getLus_id());						
							  topup.setLca_polis((String)tmp.get("LCA_ID"));						  
							  topup.setPremi_ke(1);
							  topup.setTahun_ke(1);
							  topup.setTipe("Gabungan");
							  status = bacManager.prosesAutopayment(topup, 9, currentUser,elionsManager,uwManager,i_flagCC,billInfo);
						  }else{
							  status = "Saldo i-BANK TIDAK CUKUP";
						  }
					  }else{
					  
						  status="DATA i-BANK TIDAK DITEMUKAN/BELUM DIUPLOAD";
					  }					 
					  Pemegang pmg=elionsManager.selectpp(spaj);
					  Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
					  double ldec_sisa=bacManager.selectBillingRemain(spaj);
					  String region=uwManager.select_region(pmg.getLca_id(), pmg.getLwk_id(), pmg.getLsrg_id());
					  Map map2 = new HashMap();
					  map2.put("SPAJ", topup.getReg_spaj());
					  map2.put("NO_KARTU_KREDIT", account_recur.getMar_acc_no());
					  map2.put("NAMA_PLAN", dataUsulan.getLsdbs_name());
					  map2.put("PEMEGANG", pmg.getMcl_first());
					  map2.put("POLIS", pmg.getMspo_policy_no());
					  map2.put("STATUS", status);
					  map2.put("REMAIN", ldec_sisa);
					  map2.put("REGION", region);
					  dataSpajAutoPayment.add(map2);
				  }
			  }
			  map3.put("dataSpajAutoPayment", dataSpajAutoPayment);
			  session.setAttribute("dataSpajAutoPayment", dataSpajAutoPayment);
			  bacManager.prosesEmailAutoPayment(dataSpajAutoPayment,0);
			  return new ModelAndView("uw/hasilautopayment",map3);
		} 
		
		if(request.getParameter("proses2") != null){			
		
			ArrayList data=Common.serializableList((List) session.getAttribute("dataSpajAutoPayment"));
			if(data.size() > 0){ //bila ada data
//	    		Date sysdate=elionsManager.selectSysdate();
	    		Date sysdate=new Date();
	    		ServletOutputStream sos2 = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.uw.autopayment") + ".jasper");
	    		
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		
	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("date", sysdate);
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));
		    	
		    	response.setContentType("application/vnd.ms-excel");
		        JRXlsExporter exporter = new JRXlsExporter();
		        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		        exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos2);
		        exporter.exportReport();		    	
	    		sos2.close();
		 }else{ //bila tidak ada data
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('Tidak ada data');window.close();</script>");
			sos.close();
			
		}
			
//			session.removeAttribute("dataSpajAutoPayment");
			return null;
//			return new ModelAndView("uw/hasilautopayment",map3);
		}
		map.put("dataSpaj", dataSpaj);
		session.removeAttribute("dataSpajAutoPayment");
		return new ModelAndView("uw/autopayment",map);
	}	
	
	/**
	  Ryan
	 
	 * 
	 * View Kesehatan ALL Untuk Sementara Existing
	 */
	public ModelAndView view_kesehatan(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();		
		User currentUser = (User) request.getSession().getAttribute("currentUser");			
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj");
		int json = ServletRequestUtils.getIntParameter(request, "json", 0);
		int flag_jenis_peserta = ServletRequestUtils.getIntParameter(request, "fjp",0);
		int lsbs = ServletRequestUtils.getIntParameter(request, "lsbs",0);
		List<DropDown> daftarPeserta = new ArrayList<DropDown>();	
		List listjawaban=new ArrayList();
		if(json==0){
			if(lsbs==163 || lsbs==169|| lsbs==120){
				daftarPeserta.add(0, new DropDown("0", "TERTANGGUNG UTAMA"));
			}else{
				
				daftarPeserta=bacManager.selectDropDownDaftarPeserta(spaj)	;
			}
			daftarPeserta.add(0, new DropDown("10", "-----------PILIH PESERTA----------"));//10 buat key aja 		
		}
		 if(json==1){			
				 listjawaban=bacManager.selectJawabanMedicalALL(spaj,flag_jenis_peserta);
				 response.setContentType("application/json");
		    	 PrintWriter out = response.getWriter();
		    	 Gson gson = new Gson();
		    	 out.print(gson.toJson(listjawaban));
		    	 out.close();
			 
		 return null;
	   }
		map.put("daftarPeserta", daftarPeserta);
		map.put("spaj", spaj);
			return new ModelAndView("uw/view_kesehatanALL",map);
	}	
	
	/**
	 * Controller untuk proses Speedy , Paralel (untuk > Januari 2016)
	 * @author Canpri Setiawan, Ryan F
	 * @since 4 Aug 2014
	 * @param request
	 * @param response
	 * @return
	 * @link http://localhost/E-Lions/uw/uw.htm?window=prosesSpeedy
	 */
	public ModelAndView prosesSpeedy(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap hm_map = new HashMap();
		HashMap validasi = new HashMap();
		BindException err = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Double ldecPremi,ldecBill = null;
		List bill;
		String s_spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String lca_id = elionsManager.selectCabangFromSpaj(s_spaj);
		Integer flag = ServletRequestUtils.getIntParameter(request, "flag",1);
		Map mapAutoAccept=null;
		Integer i_posisi = elionsManager.selectPosisiDokumenBySpaj(s_spaj);
		Insured ins = bacManager.selectMstInsuredAll(s_spaj);
		Pemegang pmg = elionsManager.selectpp(s_spaj);
		Tertanggung tertanggung = elionsManager.selectttg(s_spaj);
		Datausulan dataUsulan = elionsManager.selectDataUsulanutama(s_spaj);
		HashMap mTertanggung = Common.serializableMap(elionsManager.selectTertanggung(s_spaj));
		Date jan2016 = defaultDateFormat.parse(props.getProperty("jan.2016"));
		Integer status_polis = this.elionsManager.selectPositionSpaj(s_spaj);

		if(flag==1){//NB
			//validasi2 in dulu nie sebelum proses
			/**cek nominal billing , jika collection udh approve dan UW/NB belum, maka data nya dicocokan.
			 * takut ga sama, krn dibatalkan kalau tidak sama
			 */
			validasi=(HashMap) bacManager.validasiNewBusiness(s_spaj, currentUser,flag);
			//validasi.clear();
			if(!validasi.isEmpty()){
				hm_map.put("lsError",validasi.toString());
			}

			if(hm_map.isEmpty()){
				//ins.setFlag_speedy(null);
				if(ins.getFlag_speedy()==null){
					if(i_posisi==27 || i_posisi==2){
						//Map mapAutoAccept = bacManager.ProsesSpeedy(s_spaj, i_owner, i_parent, i_agent, i_reviewed, 1,pmg,tertanggung, dataUsulan, currentUser,request, elionsManager);
						if(pmg.getMspo_input_date().before(jan2016)){
						    mapAutoAccept =bacManager.ProsesSpeedy(s_spaj, 1,pmg,tertanggung, dataUsulan, currentUser,request, elionsManager);
						}else{
							mapAutoAccept =bacManager.ProsesParalel(flag, s_spaj, 1,pmg,tertanggung, dataUsulan, currentUser,request,err,elionsManager,uwManager);
						}
						//if(!mapAutoAccept.get("success").toString().isEmpty()){
						hm_map.put("successMessage", mapAutoAccept.get("success").toString());
						//}else if(!mapAutoAccept.get("error").toString().isEmpty()){
						hm_map.put("lsError", mapAutoAccept.get("error").toString());
						//}
						if(mapAutoAccept.get("error").toString().isEmpty()){
							hm_map.put("flag_sukses", 1);
						}else{
							elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "[INFO PROSES] "+mapAutoAccept.get("error").toString().replaceAll("<br>-", " "), s_spaj, 0);
						}
					}else{
						hm_map.put("successMessage", "Posisi SPAJ ini sudah bukan di SPEEDY.");
					}
				}else{
					hm_map.put("successMessage", "SPAJ ini sudah melakukan proses SPEEDY. Silahkan lengkapi e-mail further dan transfer ke UW Helpdesk");
				}
			}
		}else if(flag==2){//Collection
			//note : untuk proses collection, proses dari sini sampai akhir (termasuk proses direct print) juga sama dengan proses prosesAutoPaymentVA (bacManager.prosesAutoPaymentVA) sehingga jika ada perubahan di sini, lakukan juga perubahan di  (bacManager.prosesAutoPaymentVA) - Ridhaal
			validasi=(HashMap) bacManager.validasiNewBusiness(s_spaj, currentUser,flag);
			if(!validasi.isEmpty()){
				hm_map.put("lsError",validasi.toString());
			}

			if(hm_map.isEmpty()){
				mapAutoAccept =bacManager.ProsesParalel(flag, s_spaj, 1,pmg,tertanggung, dataUsulan, currentUser,request,err,elionsManager,uwManager);
				hm_map.put("successMessage", mapAutoAccept.get("success").toString());
				hm_map.put("lsError", mapAutoAccept.get("error").toString());
				if(mapAutoAccept.get("error").toString().isEmpty())
				{
					hm_map.put("flag_sukses", 5);
					bacManager.prosesSnows(s_spaj, currentUser.getLus_id(), null, 201);
				}else{
					elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "[GAGAL PROSES ] "+mapAutoAccept.get("error").toString().replaceAll("<br>-", " "), s_spaj, 0);
				}
			}
		}
		PrintPolisPrintingController ppc = new PrintPolisPrintingController();
		//Proses Direct Print 
		/// jika ada perubahan disini , lakukan juga perubahan di proses prosesAutoPaymentVA (bacManager.prosesAutoPaymentVA) - Ridhaal
		if(mapAutoAccept!=null){
			//20190130 Mark Valentino 183,189 Tidak Direct Print
			if((dataUsulan.getLsbs_id() == 183) && (dataUsulan.getLsdbs_number() >= 46 && dataUsulan.getLsdbs_number() <= 60)){
				return new ModelAndView("common/info_proses", hm_map);				
			}
			if(mapAutoAccept.get("flag_print").toString().equals("1")){
				int isInputanBank = elionsManager.selectIsInputanBank(s_spaj);
				Integer count = elionsManager.selectCountProductSimasPrimaAkseptasiKhusus(s_spaj, 1,10, isInputanBank);			
				String flag2 = "0";
				String businessId = uwManager.selectBusinessId(s_spaj);
				String businessNo = uwManager.selectLsdbsNumber(s_spaj);
				String pesanKemenangan="";
				Map tambahan = new HashMap();
				// PROJECT: POWERSAVE & STABLE LINK 			
				if(isInputanBank==2 || isInputanBank==3 || (businessId.equals("175") && businessNo.equals("2")) || (businessId.equals("73") && businessNo.equals("14"))) {
					//if(products.powerSave(businessId) || products.stableLink(businessId)){
					if(businessId.equals("73") && businessNo.equals("14")){//MANTA (13/01/2014) - Request Andy
						pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Filling.";
					}else if(isInputanBank==2){
						pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis.";
					}else if(products.productBsmFlowStandardIndividu(Integer.parseInt(businessId), Integer.parseInt(businessNo))){
						pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis.";
					}else{
						if(count>0){ //AKSEPTASI KHUSUS
							pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis. Status masih Akseptasi Khusus.";
						}else { //AKSEPTASI BIASA
							pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Input Tanda Terima.";
						}
						tambahan.put("transferTTP", true);
						tambahan.put("spaj", s_spaj);
						flag2 ="1";
					}
				}else if(businessId.equals("157") ) {
					pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Input Tanda Terima.";
				}else if("187,203,209".indexOf(businessId) > -1) {
					Map m = uwManager.selectInformasiEmailSoftcopy(s_spaj);
					String errorRekening = elionsManager.cekRekAgen2(s_spaj);
					String email = (String) m.get("MSPE_EMAIL");
					String keterangan = null;
					if(email != null) {
						if((businessId.equals("187") && "5,6".indexOf(businessNo)>-1)){
							keterangan = "Filling.Softcopy Telah dikirimkan ke : " + email;
						}else{
							if(!errorRekening.equals("")) {
								keterangan = "PROSES CHECKING TTP (REKENING AGEN MASIH KOSONG).Softcopy Telah dikirimkan.";
							}else{
								keterangan = "Komisi (Finance).Softcopy Telah dikirimkan.";
							}
						}
						pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke " + keterangan;
					}else{
						if((businessId.equals("187") && "5,6".indexOf(businessNo)>-1)){
							keterangan = "Filling.";
						}else{
							if(!errorRekening.equals("")) {
								keterangan = "PROSES CHECKING TTP (REKENING AGEN MASIH KOSONG).Softcopy Telah dikirimkan.";
							}else{
								keterangan = "Komisi (Finance).";
							}	
						}
						pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke " + keterangan;
					}
				}else {
					pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis.";
				}

				Integer mspo_provider= uwManager.selectGetMspoProvider(s_spaj);
				String LusId = currentUser.getLus_id();
				String lde_id = currentUser.getLde_id();
				
				//20190408 Mark Valentino DISABLE DIRECT PRINT
//				try{
//					String pesanDirectPrint = "";
//					if( businessId.equals("163") || ( businessId.equals("143") && ("1,2,3,7".indexOf(businessNo)>-1) && lca_id.equals("01") ) || (businessId.equals("144") && businessNo.equals("1") && lca_id.equals("01") ) ||
//						businessId.equals("183") || (businessId.equals("142") && lca_id.equals("01")) || (businessId.equals("177") && businessNo.equals("4")))
//					{
//						if("11,39".indexOf(lde_id)>-1){
//							String email = currentUser.getEmail();
//							Integer flagprint = 1;
//							if(businessId.equals("177") && businessNo.equals("4")) flagprint = 4; //Khusus PT INTI
//
//							elionsManager.updatePolicyAndInsertPositionSpaj(s_spaj, "mspo_date_print", LusId, 6, 1, "PRINT POLIS (E-LIONS) DIRECT PRINT", true, currentUser);
//
//							bacManager.generateReport(request, mspo_provider, flagprint, elionsManager, uwManager, 0, null);
//
//							HashMap<String, Object> printer = (HashMap<String, Object>) this.bacManager.selectPropertiesPrinter();
//							String ipAddress = (String) printer.get("IP_ADDRESS");
//							String printerName = (String) printer.get("PRINTER_NAME");
//
//							String cabang = elionsManager.selectCabangFromSpaj(s_spaj);
//							String allowPrint = this.bacManager.getAllowPrint(printerName);
//							String pdfFile = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+s_spaj+"\\"+"PolisAll.pdf";
//
//							try{
//								ThreadPrint T1 = new ThreadPrint(printerName,"directPrint",pdfFile,allowPrint,s_spaj,currentUser.getLus_id(),Print.getCountPrint(pdfFile));
//								T1.start();
//							}catch(Exception e){
//								EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
//										null, 0, 0, new Date(), null, false,
//										props.getProperty("admin.ajsjava"),
//										new String[]{"ryan@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id;antasari@sinarmasmsiglife.co.id;randy@sinarmasmsiglife.co.id;ridhaal@sinarmasmsiglife.co.id;trifena_y@sinarmasmsiglife.co.id"},
//										null,
//										null,  
//										"Error Thread print", 
//										e+"", null, s_spaj);
//							}
//
//							pesanDirectPrint = bacManager.prosesPrint(s_spaj,cabang,ipAddress,printerName);
//							hm_map.put("pesanDirectPrint", pesanDirectPrint);
//						}
//					}
//				}catch(Exception e){
//					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
//							null, 0, 0, new Date(), null, false,
//							props.getProperty("admin.ajsjava"),
//							new String[]{"ryan@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id;antasari@sinarmasmsiglife.co.id;randy@sinarmasmsiglife.co.id;ridhaal@sinarmasmsiglife.co.id;trifena_y@sinarmasmsiglife.co.id"},
//							null,
//							null,  
//							"Error Direct Print Transfer "+s_spaj, 
//							e+"", null, s_spaj);
//					logger.error("ERROR :", e);
//				}
				hm_map.put("pesanKemenangan", pesanKemenangan);
			}
		}
		//return new ModelAndView("uw/prosesSpeedy", hm_map);
		return new ModelAndView("common/info_proses", hm_map);
		
		/*HashMap hm_map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		String s_spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		
		Integer i_posisi = elionsManager.selectPosisiDokumenBySpaj(s_spaj);
		Insured ins = bacManager.selectMstInsuredAll(s_spaj);
		if(ins.getFlag_speedy()==null){
			if (ins.getLssa_id()==3){
				hm_map.put("lsError","Polis Ini Berstatus FURTHER , Tidak Bisa Melakukan Proses Ini");
			}else if(i_posisi==27){
				//baca session dari file
				//** 1. Read File txt*//*
				Boolean file=false;
				DateFormat df = new SimpleDateFormat("ddMMyyyy HH:mm:ss");
				Date date = new Date();
				String reportDate = df.format(date);
				//if(file){
					Pemegang pmg = elionsManager.selectpp(s_spaj);
					Tertanggung tertanggung = elionsManager.selectttg(s_spaj);
					Datausulan dataUsulan = elionsManager.selectDataUsulanutama(s_spaj);
					HashMap mTertanggung = Common.serializableMap(elionsManager.selectTertanggung(s_spaj));

					//Map mapAutoAccept = bacManager.ProsesSpeedy(s_spaj, i_owner, i_parent, i_agent, i_reviewed, 1,pmg,tertanggung, dataUsulan, currentUser,request,elionsManager);
					Map mapAutoAccept =bacManager.ProsesSpeedy(s_spaj, 1,pmg,tertanggung, dataUsulan, currentUser,request, elionsManager);
					//if(!mapAutoAccept.get("success").toString().isEmpty()){
					hm_map.put("successMessage", mapAutoAccept.get("success").toString());
					//}else if(!mapAutoAccept.get("error").toString().isEmpty()){
					hm_map.put("lsError", mapAutoAccept.get("error").toString());
					//}
					if(mapAutoAccept.get("error").toString().isEmpty())hm_map.put("flag_sukses", 1);
					{
				}
			}else{
				hm_map.put("successMessage", "Posisi SPAJ ini sudah bukan di SPEEDY.");
			}
		}else{
			hm_map.put("successMessage", "SPAJ ini sudah melakukan proses SPEEDY. Silahkan lengkapi e-mail further dan transfer ke UW Helpdesk");
		}
		
		//return new ModelAndView("uw/prosesSpeedy", hm_map);
		return new ModelAndView("common/info_proses", hm_map);*/

	}
	
	/**
	 * Controller untuk kirim email further
	 * @author Canpri Setiawan, Rahmayanti
	 * @since 4 Aug 2014, 05 Mar 2015
	 * @param request
	 * @param response
	 * @return
	 * @link http://localhost/E-Lions/uw/uw.htm?window=email_further
	 */
	public ModelAndView email_further(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap hm_map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		String s_spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		
		Integer create_id = Integer.parseInt(currentUser.getLus_id());
		
//		Date nowDate = bacManager.selectSysdate(); 
		
		if(request.getParameter("btnKirim") != null){
			SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String get_to = ServletRequestUtils.getStringParameter(request, "to", "");
			String get_cc = ServletRequestUtils.getStringParameter(request, "cc", "");
			String isi = ServletRequestUtils.getStringParameter(request, "isi", "");
			String subject = ServletRequestUtils.getStringParameter(request, "subject", "");
			
			String fileName;String directory ;
			Upload upload = new Upload();
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			directory = props.getProperty("pdf.dir.export");//("temp.dir.fileupload");
			MultipartFile mf = upload.getFile1();
		    fileName = mf.getOriginalFilename();
			ArrayList attachment=new ArrayList();	
			if(!StringUtil.isEmpty(fileName)){				
				String dest=directory + "/" + fileName ;
				File outputFile = new File(dest);
				FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
				attachment.add(outputFile);
			}
					
			
			String[] to = get_to.split(";");
			String[] cc = get_cc.split(";");
			
			bacManager.transferPosisi(currentUser, s_spaj, 209, isi, "tgl_transfer_uw_hd",null);
			
			//kirim email
			String css = props.getProperty("email.uw.css.satu")
					+ props.getProperty("email.uw.css.dua");
			String footer = props.getProperty("email.uw.footer");
			
			List lsProduk = elionsManager.selectMstProductInsured(s_spaj);
			Product produk=(Product)lsProduk.get(0);
			
			if(elionsManager.selectIsInputanBank(s_spaj)==16 || (produk.getLsbs_id()==182 && "19,20,21".indexOf(produk.getLsdbs_number())>-1)) footer = props.getProperty("email.uw.syariah.footer");//Anta - Khusus Power Save Syariah BSM dan Multi Invest Syariah BSM
			DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
			
			HashMap info = Common.serializableMap(bacManager.selectInformasiCabangFromSpaj(s_spaj));
			String cabang = (String) info.get("NAMA_CABANG");
			String kci =  (info.get("KCI")==null?null:(String)info.get("KCI"));
			String nama_ao = (info.get("NAMA_AO")==null?null:(String)info.get("NAMA_AO"));
			
			HashMap gen = Common.serializableMap(elionsManager.selectEmailAgen(s_spaj));
			String namaAgen = (String) gen.get("MCL_FIRST");
			
			Pemegang pp = elionsManager.selectpp(s_spaj);
			Tertanggung ttg = elionsManager.selectttg(s_spaj);
			Datausulan du = elionsManager.selectDataUsulanutama(s_spaj);
			
			String from = "ajsjava@sinarmasmsiglife.co.id";
			
			String pesan = "";
			
			pesan = css + 
					"<table width=100% class=satu>"
						+ (cabang!=null	? 	"<tr><td>Cabang   	</td><td>:</td><td>" + cabang + "</td></tr>" : "")
						+ (kci!=null 	?	"<tr><td>KCI   	</td><td>:</td><td>" + kci + "</td></tr>" : "")
						+ "<tr><td>Agen   		</td><td>:</td><td>" + namaAgen + "</td></tr>"
						+ "<tr><td>Status   	</td><td>:</td><td>FURTHER REQUIREMENT</td><td> Tanggal:" + defaultDateFormat.format(new Date()) + "</td></tr>"
						+ "<tr><td>Akseptor 	</td><td>:</td><td>" + currentUser.getName() + " [" + currentUser.getDept() + " ]<td></tr>" 
						+ "<tr><td>No. Spaj 	</td><td>:</td><td>" + FormatString.nomorSPAJ(s_spaj) + "<td></tr>"
						+ "<tr><td>Produk		</td><td>:</td><td>"+produk.getLsdbs_name()+"("+produk.getLsbs_id()+")"+"<td colspan=2></tr>" 
						+ "<tr><td>A/N			</td><td>:</td><td colspan=2>" + pp.getMcl_first() + " (Pemegang) -- " + ttg.getMcl_first() + " (Tertanggung) " + "</td></tr>" 
						+ "<tr><td>Keterangan	</td><td>:</td><td>" + isi + "<td></tr>" 
						+ "<tr><td>UP</td><td>:</td><td>"+produk.getLku_symbol()+" "+ df.format(du.getMspr_tsi()) +"</td></tr>"
						+ "<tr><td>Premi</td><td>:</td><td>"+produk.getLku_symbol()+" "+ df.format(du.getMspr_premium())+"</td></tr>"
						+ "<tr><td>Cara Bayar</td><td>:</td><td>"+ du.getLscb_pay_mode() +"</td></tr>"
						+ (nama_ao!=null	?	"<tr><td>Nama AO   	</td><td>:</td><td>" + nama_ao + "</td></tr>" : "")
					+"</table>" + footer;
			
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, from, 
					to, 
					cc,  
					null, 
					subject, 
					pesan, 
					attachment, s_spaj);
			
			//email.send(true, from, new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null,subject, pesan, null);
			if(!StringUtil.isEmpty(fileName))FileUtil.deleteFile(directory, fileName, response);
			
			hm_map.put("speedy", 0);
			hm_map.put("sukses", 1);
			hm_map.put("successMessage", "SPAJ "+s_spaj+" berhasil ditansfer ke UW Helpdesk");
		}else{
			Insured ins = bacManager.selectMstInsuredAll(s_spaj);
			
			if(ins.getFlag_speedy()==null){
				hm_map.put("successMessage", "SPAJ ini belum proses speedy");
				hm_map.put("speedy", 0);
			}else{
				String to = "";
				
				Product prd = uwManager.selectMstProductInsuredUtamaFromSpaj(s_spaj);
				
				//untuk produk simas prima, power save syariah dan danamas prima permintaan Feri UW
				if(prd.getLsbs_id()==142 && prd.getLsdbs_number()==2){
					HashMap em = bacManager.selectMstConfig(6, "email_further", "SIMAS_PRIMA");
					to = (String)em.get("NAME"); 
				}else if(prd.getLsbs_id()==175 && prd.getLsdbs_number()==2){
					HashMap em = bacManager.selectMstConfig(6, "email_further", "POWER_SAVE_SYARIAH");
					to = (String)em.get("NAME"); 
				}else if(prd.getLsbs_id()==142 && prd.getLsdbs_number()==9){
					HashMap em = bacManager.selectMstConfig(6, "email_further", "DANAMAS_PRIMA");
					to = (String)em.get("NAME"); 
				}else{
					to = uwManager.selectEmailCabangFromSpaj(s_spaj)+";";
				}
				
				String email_cab = bacManager.selectEmailAdminInputter(s_spaj);
				String cc = "uwhelpdeskAGENCY@sinarmasmsiglife.co.id;bas@sinarmasmsiglife.co.id;"+uwManager.selectEmailUser(currentUser.getLus_id());
				if(email_cab!=null)cc = email_cab+";"+cc;
				
				StringBuffer message = new StringBuffer();
				ArrayList l_histSpeedy = Common.serializableList(bacManager.selectHistorySpeedy(s_spaj));
				
				String medis = bacManager.selectMedisDescNew(s_spaj);
				Pemegang pp = elionsManager.selectpp(s_spaj);
				Tertanggung ttg = elionsManager.selectttg(s_spaj);
				
				for(int i=0; i<l_histSpeedy.size();i++){
					HashMap m = (HashMap) l_histSpeedy.get(i);
					message.append("\n"+(String)m.get("DESCRIPTION"));
					
				}
				
				hm_map.put("to", to);
				hm_map.put("cc", cc);
				hm_map.put("message", message.toString()+"\n\nMedis : "+medis);
				//hm_map.put("subject", "Further Requirement SPAJ "+s_spaj);
				hm_map.put("subject", "FURTHER REQUIREMENT SPAJ No."+FormatString.nomorSPAJ(s_spaj)+" a/n "+pp.getMcl_first()+"/"+ttg.getMcl_first());
				hm_map.put("speedy", 1);
				hm_map.put("sukses", 0);
			}
		}
		return new ModelAndView("uw/email_further", hm_map);
	}
	
	/**
	 * Controller untuk transfer to UW dari UW Helpdesk
	 * @author Canpri Setiawan, Rahmayanti
	 * @since 4 Aug 2014, 05 Mar 2015
	 * @param request
	 * @param response
	 * @return
	 * @link http://localhost/E-Lions/uw/uw.htm?window=transferToUw
	 */
	public ModelAndView transferToUw(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap hm_map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		String s_spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		
		bacManager.transferPosisi(currentUser, s_spaj, 2, "TRANSFER KE U/W PROSES", "tgl_transfer_uw",null);
		
		Integer create_id = Integer.parseInt(currentUser.getLus_id());
		
//		Date nowDate = bacManager.selectSysdate();
		
		hm_map.put("successMessage", "SPAJ ini berhasil ditansfer ke UW Proses");
		hm_map.put("flag_sukses", 2);//2 untuk sukses transfer dari uw helpdesk ke uw
		return new ModelAndView("common/info_proses", hm_map);
	}
	
	/**
	 * Controller untuk Back transfer to UW Helpdesk dari UW Proses
	 * @author Canpri Setiawan
	 * @since 4 Aug 2014
	 * @param request
	 * @param response
	 * @return
	 * @link http://localhost/E-Lions/uw/uw.htm?window=transferToUwHelpdesk
	 */
	public ModelAndView transferToUwHelpdesk(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap hm_map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
//		String s_spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
//		
//		bacManager.transferPosisi(currentUser, s_spaj, 209, "TRANSFER KE U/W HELPDESK DARI UW PROSES", "tgl_transfer_uw",
//				209, 210, "TRANSFER KE UW HELPDESK DARI UW PROSES");
//		
//		hm_map.put("successMessage", "SPAJ ini berhasil ditansfer ke UW Helpdesk");
//		hm_map.put("flag_sukses", 2);//2 untuk sukses transfer dari uw proses ke uw helpdesk
//		return new ModelAndView("common/info_proses", hm_map);
		
		String s_spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		
		if(request.getParameter("btnKirim") != null){
			SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String get_to = ServletRequestUtils.getStringParameter(request, "to", "");
			String get_cc = ServletRequestUtils.getStringParameter(request, "cc", "");
			String isi = ServletRequestUtils.getStringParameter(request, "isi", "");
			String subject = ServletRequestUtils.getStringParameter(request, "subject", "");
			
			String fileName;String directory ;
			Upload upload = new Upload();
			upload.setDaftarFile(new ArrayList<MultipartFile>(10));
			ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
			binder.bind(request);
			directory = props.getProperty("pdf.dir.export");//("temp.dir.fileupload");
			MultipartFile mf = upload.getFile1();
		    fileName = mf.getOriginalFilename();
			ArrayList attachment=new ArrayList();	
			if(!StringUtil.isEmpty(fileName)){				
				String dest=directory + "/" + fileName ;
				File outputFile = new File(dest);
				FileCopyUtils.copy(upload.getFile1().getBytes(), outputFile);
				attachment.add(outputFile);
			}
			
			String[] to = get_to.split(";");
			String[] cc = get_cc.split(";");
			
			bacManager.transferPosisi(currentUser, s_spaj, 209, isi, "tgl_transfer_uw_hd",null);
			
			//kirim email
			String css = props.getProperty("email.uw.css.satu")
					+ props.getProperty("email.uw.css.dua");
			String footer = props.getProperty("email.uw.footer");
			
			List lsProduk = elionsManager.selectMstProductInsured(s_spaj);
			Product produk=(Product)lsProduk.get(0);
			
			if(elionsManager.selectIsInputanBank(s_spaj)==16 || (produk.getLsbs_id()==182 && "19,20,21".indexOf(produk.getLsdbs_number())>-1)) footer = props.getProperty("email.uw.syariah.footer");//Anta - Khusus Power Save Syariah BSM dan Multi Invest Syariah BSM
			DecimalFormat df = new DecimalFormat("#,##0.00;(#,##0.00)");
			
			HashMap info = Common.serializableMap(bacManager.selectInformasiCabangFromSpaj(s_spaj));
			String cabang = (String) info.get("NAMA_CABANG");
			String kci =  (info.get("KCI")==null?null:(String)info.get("KCI"));
			String nama_ao = (info.get("NAMA_AO")==null?null:(String)info.get("NAMA_AO"));
			
			HashMap gen = Common.serializableMap(elionsManager.selectEmailAgen(s_spaj));
			String namaAgen = (String) gen.get("MCL_FIRST");
			
			Pemegang pp = elionsManager.selectpp(s_spaj);
			Tertanggung ttg = elionsManager.selectttg(s_spaj);
			Datausulan du = elionsManager.selectDataUsulanutama(s_spaj);
			
			String from = "ajsjava@sinarmasmsiglife.co.id";
			
			String pesan = "";
			
			pesan = css + 
					"<table width=100% class=satu>"
						+ (cabang!=null	? 	"<tr><td>Cabang   	</td><td>:</td><td>" + cabang + "</td></tr>" : "")
						+ (kci!=null 	?	"<tr><td>KCI   	</td><td>:</td><td>" + kci + "</td></tr>" : "")
						+ "<tr><td>Agen   		</td><td>:</td><td>" + namaAgen + "</td></tr>"
						+ "<tr><td>Status   	</td><td>:</td><td>FURTHER REQUIREMENT</td><td> Tanggal:" + defaultDateFormat.format(new Date()) + "</td></tr>"
						+ "<tr><td>Akseptor 	</td><td>:</td><td>" + currentUser.getName() + " [" + currentUser.getDept() + " ]<td></tr>" 
						+ "<tr><td>No. Spaj 	</td><td>:</td><td>" + FormatString.nomorSPAJ(s_spaj) + "<td></tr>"
						+ "<tr><td>Produk		</td><td>:</td><td>"+produk.getLsdbs_name()+"("+produk.getLsbs_id()+")"+"<td colspan=2></tr>" 
						+ "<tr><td>A/N			</td><td>:</td><td colspan=2>" + pp.getMcl_first() + " (Pemegang) -- " + ttg.getMcl_first() + " (Tertanggung) " + "</td></tr>" 
						+ "<tr><td>Keterangan	</td><td>:</td><td>" + isi + "<td></tr>" 
						+ "<tr><td>UP</td><td>:</td><td>"+produk.getLku_symbol()+" "+ df.format(du.getMspr_tsi()) +"</td></tr>"
						+ "<tr><td>Premi</td><td>:</td><td>"+produk.getLku_symbol()+" "+ df.format(du.getMspr_premium())+"</td></tr>"
						+ "<tr><td>Cara Bayar</td><td>:</td><td>"+ du.getLscb_pay_mode() +"</td></tr>"
						+ (nama_ao!=null	?	"<tr><td>Nama AO   	</td><td>:</td><td>" + nama_ao + "</td></tr>" : "")
					+"</table>" + footer;
			
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, from, 
					to, 
					cc,  
					null, 
					subject, 
					pesan, 
					attachment, s_spaj);
			
			//email.send(true, from, new String[]{"canpri@sinarmasmsiglife.co.id"}, null, null,subject, pesan, null);
			if(!StringUtil.isEmpty(fileName))FileUtil.deleteFile(directory, fileName, response);
			
			hm_map.put("speedy", 0);
			hm_map.put("sukses", 2);
			hm_map.put("successMessage", "SPAJ "+s_spaj+" berhasil ditansfer ke UW Helpdesk");
		}else{
			String to = "uwhelpdeskAGENCY@sinarmasmsiglife.co.id;"+uwManager.selectEmailCabangFromSpaj(s_spaj)+";";
			String cc = uwManager.selectEmailUser(currentUser.getLus_id())+";";
			
			hm_map.put("to", to);
			hm_map.put("cc", cc);
			hm_map.put("subject", "Further Requirement SPAJ "+s_spaj);
			hm_map.put("speedy", 1);
			hm_map.put("sukses", 0);
		}
		return new ModelAndView("uw/email_further", hm_map);
	}
	
	/**
	 * Controller untuk transfer ke BAS dari UW speedy
	 * @author Canpri Setiawan
	 * @since 5 Jan 2015
	 * @param request
	 * @param response
	 * @return
	 * @link http://localhost/E-Lions/uw/uw.htm?window=backtobas
	 */
	public ModelAndView backtobas(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap hm_map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Integer create_id = Integer.parseInt(currentUser.getLus_id());
		
		String s_spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");		
		
		String spajr = uwManager.selectSpajRecur(s_spaj);
		if (spajr != null){		
			hm_map.put("spajrecur", 1);
			hm_map.put("block_r", "SPAJ RECURRING TIDAK DAPAT DIPROSES BACK TO BAS");
		}
		
//		Date nowDate = bacManager.selectSysdate(); 
		
		if(request.getParameter("btnKirim") != null){
			String get_to = ServletRequestUtils.getStringParameter(request, "to", "");
			String get_cc = ServletRequestUtils.getStringParameter(request, "cc", "");
			String isi = ServletRequestUtils.getStringParameter(request, "isi", "");
			String subject = ServletRequestUtils.getStringParameter(request, "subject", "");
			
			String[] to = get_to.split(";");
			String[] cc = get_cc.split(";");
			
			bacManager.transferPosisi(currentUser, s_spaj, 1, "BACK TRANSFER KE BAS: "+isi.toUpperCase(), "tgl_back_to_bas",null);
			
			//kirim email
			
			EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
					null, 0, 0, new Date(), null, 
					true, "ajsjava@sinarmasmsiglife.co.id", 
					to, 
					cc,  
					null, 
					subject, 
					isi, 
					null, s_spaj);
			
//			email.send(true, "ajsjava@sinarmasmsiglife.co.id", new String[]{"randy@sinarmasmsiglife.co.id"}, null, null,subject, isi, null);
							
			hm_map.put("speedy", 0);
			hm_map.put("sukses", 1);
			hm_map.put("successMessage", "SPAJ "+s_spaj+" berhasil ditansfer ke BAS");
		}else{
			Insured ins = bacManager.selectMstInsuredAll(s_spaj);
			
			String lca_id = uwManager.selectLcaIdMstPolicyBasedSpaj(s_spaj);
			String lsbs_id = uwManager.selectLsbsId(s_spaj);
			String lsdbs_number = uwManager.selectLsdbsNumber(s_spaj);
			
			if(ins.getFlag_speedy()!=null){
				hm_map.put("successMessage", "SPAJ ini sudah proses Speedy");
				hm_map.put("speedy", 1);
			}else if(lca_id.equals("09")){
				//simpol, smile link satu, simpol syariah, vip family plan bisa back to bas
				if( lsbs_id.equals("120") || lsbs_id.equals("190") || lsbs_id.equals("202") || lsbs_id.equals("208") ||
					lsbs_id.equals("212") || lsbs_id.equals("213") || lsbs_id.equals("220") ||
					(lsbs_id.equals("134") && (lsdbs_number.equals("5") || lsdbs_number.equals("10") || lsdbs_number.equals("12"))) ||
					(lsbs_id.equals("134") && lsdbs_number.equals("9")) ||
					(lsbs_id.equals("215") && lsdbs_number.equals("1")) ||
					(lsbs_id.equals("216") && lsdbs_number.equals("1")) ){
						//randy - tambahan produk yg bs BTB Magna Link (konven-syariah) dan Prime Link (konven-syariah) & JEMPOL LINK  & Smile Link Plus
					String emailProses = uwManager.selectEmailUser(currentUser.getLus_id());
					
					String to = bacManager.selectEmailAdminInputter(s_spaj)+";";
					String cc = uwManager.selectEmailCabangFromSpaj(s_spaj)+";"+"bas@sinarmasmsiglife.co.id;";
					if(emailProses!=null)cc = cc+emailProses+";";
					String emailCabangBank = uwManager.selectEmailCabangBankSinarmas(s_spaj);
			    	String emailAoBank = uwManager.selectEmailAoBankSinarmas(s_spaj);
			    	if(emailCabangBank != null) to = to.concat(emailCabangBank+";");
			    	if(emailAoBank != null) to = to.concat( emailAoBank +";" );
					
					hm_map.put("to", to);
					hm_map.put("cc", cc);
					hm_map.put("subject", "Transfer to BAS SPAJ "+s_spaj);
					hm_map.put("speedy", 0);
					hm_map.put("sukses", 0);
				}else{
					hm_map.put("successMessage", "SPAJ Bancassurance tidak bisa di transfer ke BAS");
					hm_map.put("speedy", 1);
				}
			}else{
				String emailProses = uwManager.selectEmailUser(currentUser.getLus_id());
				
				String to = bacManager.selectEmailAdminInputter(s_spaj)+";";
				String cc = uwManager.selectEmailCabangFromSpaj(s_spaj)+";"+"bas@sinarmasmsiglife.co.id;";
				if(emailProses!=null)cc = cc+emailProses+";";
				
				hm_map.put("to", to);
				hm_map.put("cc", cc);
				hm_map.put("subject", "Transfer to BAS SPAJ "+s_spaj);
				hm_map.put("speedy", 0);
				hm_map.put("sukses", 0);
			}
		}
		return new ModelAndView("uw/backtobas", hm_map);
	}
	
	public ModelAndView showValidasiTtd(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap map=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String s_spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		map.put("spaj",s_spaj);
		return new ModelAndView("common/validasi_ttd", map);
		
	}
	
	public ModelAndView reportCoverLetterJne(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String spajproses = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String flag = ServletRequestUtils.getStringParameter(request, "flag", "cl");
		String dist = ServletRequestUtils.getStringParameter(request, "dist", "");
		String path = null;
		
		try{
			ArrayList data = bacManager.selectReportCoverLetterJne(spajproses, dist);
			
			if(data.size() > 0){
				//Rahmayanti - Flag antara jne bsm dengan jne agency 
				ServletOutputStream sos = response.getOutputStream();
				if(flag.equals("clBsm")){
					path = props.getProperty("report.coverletter_jnebsm");
				}
				else{
					path = props.getProperty("report.coverletter_jne");
				}
				File sourceFile = Resources.getResourceAsFile( path + ".jasper");
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("spaj", spajproses);
				
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));
	    		response.setContentType("application/vnd.ms-excel");
	            JRXlsExporter exporter = new JRXlsExporter();
	            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
	            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
	            exporter.exportReport();
	            
		    	if(sos!=null){
		    		sos.flush();
    		    	sos.close();
		    	}
			}
			
		} catch (Exception e) {
			logger.error("ERROR :", e);
		}
		
		return null;
	}
	
	public ModelAndView reportFollowUpCsfCall(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		Map map = new HashMap();
		
		String tanggalAwal = ServletRequestUtils.getStringParameter(request, "tanggalAwal", FormatDate.toString(sysdate));
		String tanggalAkhir = ServletRequestUtils.getStringParameter(request, "tanggalAkhir", FormatDate.toString(sysdate));		

		if(request.getParameter("btnShow") != null) {
			try{
				ArrayList data = bacManager.selectReportFollowUpCsfCall(tanggalAwal, tanggalAkhir);
				
				ServletOutputStream sos = response.getOutputStream();
				String path = props.getProperty("report.uw.followup_csfcall");
				File sourceFile = Resources.getResourceAsFile( path + ".jasper");
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("tanggalAwal", tanggalAwal);
				params.put("tanggalAkhir", tanggalAkhir);
				
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));
	    		response.setContentType("application/vnd.ms-excel");
	            JRXlsExporter exporter = new JRXlsExporter();
	            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
	            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
	            exporter.exportReport();
	            
		    	if(sos!=null){
		    		sos.flush();
			    	sos.close();
		    	}
			} catch (Exception e) {
				logger.error("ERROR :", e);
			}
			return null;
		}
		map.put("tanggalAwal", tanggalAwal);
		map.put("tanggalAkhir", tanggalAkhir);
		return new ModelAndView("uw/reportFollowUpCsfCall", map);
	}
	
	/**
	 * @author alfian_h
	 * @since 18/03/2015
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @link http://localhost:8080/E-Lions/uw/uw.htm?window=generateNoKartuPAS
	 */
	public ModelAndView generateNoKartuPAS(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap map = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
		BasDao basDao = null;
		String lus_id = currentUser.getLus_id();
		
		if(request.getParameter("submit")!=null){
			String product_code = null;// = ServletRequestUtils.getStringParameter(request, "product_code", "");
			String sub_code = ServletRequestUtils.getStringParameter(request, "sub_code", "");
			String pc[] = sub_code.split("-");
			product_code = pc[0].toString();
			sub_code = pc[1].toString();
			String totalGenerate = ServletRequestUtils.getStringParameter(request, "totalGenerate", "");
			String premi = ServletRequestUtils.getStringParameter(request, "premi", "");
			String up = ServletRequestUtils.getStringParameter(request, "up", "");
			String generateIdNumber = ServletRequestUtils.getStringParameter(request, "generateIdNumber", "0544");
			
			map.put("product_code", product_code);
			map.put("sub_code", sub_code);
			map.put("totalGenerate", totalGenerate);
			map.put("premi", premi);
			map.put("up", up);
			map.put("generateIdNumber", generateIdNumber);
			map.put("lus_id", lus_id);
			
			bacManager.generateNoKartuPAS(map);
			map.put("pesan", "Generate selesai");
		}
		
		if(request.getParameter("btnShow") != null) {
			String tgl_input1 = ServletRequestUtils.getStringParameter(request, "tgl_input","");
			String tgl_input = tgl_input1.substring(0, 10);
			ArrayList kartu = Common.serializableList(bacManager.selectKartuPasbyTglInput(tgl_input));
			map.put("kartu", kartu);
			map.put("tgl_input", tgl_input1);
			
			if(request.getParameter("btnExcel") != null) {
				if(kartu.size() > 0){ //bila ada data
					// get path my document
					String myDocuments = null;
		    		try {
		    		    Process p =  Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
		    		    p.waitFor();

		    		    InputStream in = p.getInputStream();
		    		    byte[] b = new byte[in.available()];
		    		    in.read(b);
		    		    in.close();

		    		    myDocuments = new String(b);
		    		    myDocuments = myDocuments.split("\\s\\s+")[4];

		    		} catch(Throwable t) {
		    		    logger.error(t);
		    		}

		    		// Format Header
		    		WritableFont headerFont = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
		    		WritableCellFormat cellHeader = new WritableCellFormat(headerFont);
		    		// Format HeaderIsi
		    		WritableFont normalFont = new WritableFont(WritableFont.ARIAL,10);
		    		WritableCellFormat isi = new WritableCellFormat(normalFont);
		    		isi.setBorder(Border.ALL, BorderLineStyle.THIN);
		    		NumberFormat numFormat = new NumberFormat("###,###,###,###,##0.00");
		    		WritableCellFormat numberFormat = new WritableCellFormat(numFormat);
		    		numberFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		    		// Format DateTime
		    		WritableCellFormat cf1 = new WritableCellFormat(DateFormats.FORMAT9);

		    		WritableWorkbook workbook;
		    		workbook = Workbook.createWorkbook(new File(myDocuments+"\\kartu.xls"));
		    		WritableSheet sheet = workbook.createSheet("Kartu", 0);
		    		Label header = new Label(0, 0, "PERMINTAAN NO KARTU PAS", cellHeader);
		    		sheet.addCell(header);
		    		Label tglGen = new Label(0, 1, "Tanggal Generate : ");
		    		sheet.addCell(tglGen);
		    		DateTime dt = new DateTime(1,1,new Date(),cf1);
		    		sheet.addCell(dt);
		    		
		    		Label no_kartu = new Label(0,2,"No Kartu",isi);
		    		Label flag = new Label(1,2,"Flag",isi);
		    		Label permi = new Label(2,2,"Premi",isi);
		    		Label up = new Label(3,2,"UP",isi);
		    		Label produk = new Label(4,2,"Produk",isi);
		    		Label vc = new Label(5,2,"Virtual Account",isi);
		    		Label user_input = new Label(6,2,"User Input",isi);
		    		
		    		sheet.addCell(no_kartu);
		    		sheet.addCell(flag);
		    		sheet.addCell(permi);
		    		sheet.addCell(up);
		    		sheet.addCell(produk);
		    		sheet.addCell(vc);
		    		sheet.addCell(user_input);
		    		
		    		for(int i=0; i<kartu.size();i++){
		    			HashMap m = (HashMap) kartu.get(i);
		    			int row = 3+i;
		    			
		    			String ikartu = (String) m.get("NO_KARTU");
		    			String iflag = (String) m.get("FLAG");
		    			BigDecimal ipermi = (BigDecimal) m.get("PREMI");
		    			BigDecimal iup = (BigDecimal) m.get("UP");
		    			String iproduk = (String) m.get("PRODUCT");
		    			String ivc = (String) m.get("NO_VA");
		    			String iuser_input = (String) m.get("NAMA");
		    			
		    			Label Lkartu = new Label(0,row,ikartu,isi);
			    		Label Lflag = new Label(1,row,iflag,isi);
			    		jxl.write.Number Lpermi = new Number(2,row,Double.valueOf(String.valueOf(ipermi)),numberFormat);
			    		jxl.write.Number Lup = new Number(3,row,Double.valueOf(String.valueOf(iup)),numberFormat);
			    		Label Lproduk = new Label(4,row,iproduk,isi);
			    		Label Lvc = new Label(5,row,ivc,isi);
			    		Label Luser_input = new Label(6,row,iuser_input,isi);
			    		
			    		sheet.addCell(Lkartu);
			    		sheet.addCell(Lflag);
			    		sheet.addCell(Lpermi);
			    		sheet.addCell(Lup);
			    		sheet.addCell(Lproduk);
			    		sheet.addCell(Lvc);
			    		sheet.addCell(Luser_input);
		    		}
		    		
		    		workbook.write();
		    		workbook.close();
		    		
		    		map.put("pesan", "File tersimpan di MyDocuments kartu.xls");
	    		}else{ //bila tidak ada data
	    			ServletOutputStream sos = response.getOutputStream();
	    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
	    			sos.close();
	    			return null;
	    		}
			}
		}
		
		List<DropDown> id = elionsManager.selectDropDown("eka.mst_kartu_pas", "distinct(trunc(tgl_input))", "trunc(tgl_input)", "", "trunc(tgl_input) desc", "product_code in ('73','187')");
		map.put("id", id);
		map.put("prod", bacManager.selectLstDetBisnisPAS());
		
		return new ModelAndView("uw/generateNoKartuPAS", map);
	}
	
	public ModelAndView drekNonSpaj(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Date now = this.elionsManager.selectSysdate();
		Date now = new Date();
		Integer json = ServletRequestUtils.getIntParameter(request, "json", 0);
		
		//Halaman depan
		if(json == 0){
			String startDate = ServletRequestUtils.getStringParameter(request, "startDate", defaultDateFormat.format(now));
			String endDate = ServletRequestUtils.getStringParameter(request, "endDate", defaultDateFormat.format(now));
			String startNominal = ServletRequestUtils.getStringParameter(request, "startNominal", "0");
			String endNominal = ServletRequestUtils.getStringParameter(request, "endNominal", "99999999999");
			String kode = ServletRequestUtils.getStringParameter(request, "kode", "");
			Integer lsrek_id = ServletRequestUtils.getIntParameter(request, "lsrek_id", -1);
			Integer lsbp_id = ServletRequestUtils.getIntParameter(request, "lsbp_id", 156);
			
			HashMap map = new HashMap();
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("startNominal", startNominal);
			map.put("endNominal", endNominal);
			map.put("kode", kode);
			map.put("lsrek_id", lsrek_id);
			map.put("lsbp_id", lsbp_id);
			
			List<DropDown> daftarRekAjs = new ArrayList<DropDown>();
			daftarRekAjs.add(new DropDown("-1", "ALL"));
			daftarRekAjs.addAll(uwManager.selectRekeningAjs(null, null));
			
			List<DropDown> daftarCab = new ArrayList<DropDown>();
			daftarCab.add(0, new DropDown("", "ALL"));
			daftarCab.addAll(this.elionsManager.selectDropDown("EKA.CAB_BSM", "KODE", "NAMA_CAB  || ' (' || KODE || ')'", "", "NAMA_CAB", null));
			
			map.put("daftarRekAjs", daftarRekAjs);
			map.put("daftarBank", this.elionsManager.selectDropDown("EKA.LST_BANK_PUSAT", "LSBP_ID", "LSBP_NAMA", "", "LSBP_NAMA", null));
			map.put("daftarCab", daftarCab);
			map.put("daftarDrek", this.elionsManager.selectMstDrek(lsrek_id, lsbp_id, kode, defaultDateFormat.parse(startDate), defaultDateFormat.parse(endDate), Double.valueOf(startNominal), Double.valueOf(endNominal)));
			
			return new ModelAndView("uw/drekNonSpaj", map);
			
		}else if(json == 1){
			String no_rek = ServletRequestUtils.getStringParameter(request, "no_rek", null);
    		List<DropDown> result = uwManager.selectRekeningAjs(null, no_rek);
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
		}
		return null;
	}
	
	public ModelAndView paymentNonSpaj(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String jumlah = request.getParameter("jumlah");
		String simbol = request.getParameter("simbol");
		String no_trx = request.getParameter("no_trx");
		String noSpaj = request.getParameter("noSpaj");
		String norek_ajs = request.getParameter("norek_ajs");
		String premiTerpakai = request.getParameter("premiTerpakai");
		Map map = new HashMap();
		map.put("jumlah", jumlah);
		map.put("simbol", simbol);
		map.put("no_trx", no_trx);
		map.put("noSpaj", noSpaj);
		map.put("norek_ajs", norek_ajs);
		map.put("premiTerpakai", premiTerpakai);
		return new ModelAndView("uw/payment_frame_nonspaj", map);
	}
	
	/**
	 * MANTA
	 * autopayment khusus produk PA
	 */
	public ModelAndView autopayment_pa(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		HttpSession session = request.getSession();
		Map map = new HashMap();
		Map map3 = new HashMap();		
		ArrayList<HashMap> dataSpaj = new ArrayList<HashMap>();
		ArrayList dataSpajAutoPayment = new ArrayList();
	
//		Date sysdate = elionsManager.selectSysdate();
		Date sysdate = new Date();
		dataSpaj = bacManager.selectSpajPAPosisiPayment();
		String status = "";
		
		if(request.getParameter("proses") != null){
			String check[] = request.getParameterValues("chbox");
			if(check != null){
				for(int i=0; i<check.length; i++){
					ArrayList billInfo = new ArrayList();
					TopUp topup = new TopUp();
					String data = check[i];
					String spaj = data;
					billInfo = Common.serializableList(this.elionsManager.selectBillingInformation(spaj, 1, 1));//premiKe sama tahunKe diset 1
					Map tmp = (HashMap) billInfo.get(0);
					ArrayList<Payment> paymentInfo = Common.serializableList(this.elionsManager.selectPaymentCount(spaj, 1, 1));
					topup.setReg_spaj(spaj);
					topup.setActionTypeForDrekDet("insert");
					Payment p = new Payment();
					p.setKe(paymentInfo.size());
					p.setTipe("Penambahan");
					paymentInfo.add(p);
					topup.setMspa_pay_date((Date) tmp.get("MSBI_BEG_DATE"));
					topup.setLku_id((String) tmp.get("LKU_ID"));
				    topup.setBill_lku_id((String) tmp.get("LKU_ID"));
					topup.setMspa_payment(new Double(((BigDecimal) tmp.get("SISA")).doubleValue()));					
					String lca_id = (String) tmp.get("LCA_ID");
					topup.setLca_polis(lca_id);
					String lsbs = uwManager.selectBusinessId(topup.getReg_spaj());
					Integer i_flagCC=elionsManager.select_flag_cc(topup.getReg_spaj());
					Account_recur account_recur = elionsManager.select_account_recur(topup.getReg_spaj());
				    if(i_flagCC==1 || i_flagCC==2){					   
					    if(account_recur!=null)topup.setMspa_no_rek(account_recur.getMar_acc_no());
				    }				    
				    HashMap dataVA = Common.serializableMap(bacManager.selectDataRKVA(topup.getReg_spaj()));
					
//					//Mark Valentino 20190315 - Aktifkan utk Test
//					request.setAttribute("produk", "PA Konven");
//					request.setAttribute("flagAutoPayment", "1");
//					request.setAttribute("spaj", spaj);
//					PrintPolisAllPelengkap ppap = new PrintPolisAllPelengkap();
//					String resultEmail = ppap.emailPaKonven(request, response, elionsManager, uwManager, props);
//					if (resultEmail.equals("Proses Pengiriman Email Gagal.")){
//						Map mapError = new HashMap();
//						mapError.put("ERROR", "Proses Autopayment PA Berhasil, namun terjadi error pada saat email Sertifikat.");
//						return new ModelAndView("uw/hasilautopayment_pa",mapError);
//					}
					
					//Mark Valentino 20190315 - Aktifkan utk Production
					if(dataVA!=null){
						String no_trx = (String) dataVA.get("NO_TRX");
						Double net_bayar = ((BigDecimal) dataVA.get("MSPR_PREMIUM")).doubleValue();					
						Double sisa_saldo = uwManager.selectCheckTotalUsedMstDrek(no_trx, null, null, null);
						
						//Mark 20190612 - Nonaktifkan untuk production (bypass proses inforce)
						//net_bayar=Double.valueOf(0);
						
						if(net_bayar <= sisa_saldo){
							topup.setLsjb_id(1);
							topup.setLsrek_id(((BigDecimal) dataVA.get("LSREK_ID")).toString());
							topup.setMspa_active(1);
							topup.setMspa_nilai_kurs(((BigDecimal) dataVA.get("NILAI_KURS")).doubleValue());
							topup.setMspa_desc("CEK I-BANK(No Transaksi "+no_trx+")");
							topup.setNo_trx(no_trx);
							topup.setMspa_payment(net_bayar);
							topup.setMspa_no_rek(null);
							topup.setMspa_date_book((Date) dataVA.get("TGL_TRX"));
							topup.setPay_date((Date) tmp.get("MSBI_BEG_DATE"));
							topup.setLus_id(currentUser.getLus_id());						
							topup.setLca_polis((String)tmp.get("LCA_ID"));						  
							topup.setPremi_ke(1);
							topup.setTahun_ke(1);
							topup.setTipe("Gabungan");
							status = bacManager.prosesAutopayment(topup, 9, currentUser, elionsManager, uwManager,i_flagCC,billInfo);
							//Mark Valentino 20190315 - Revisi PA Konven (Jika Inforce -> Email Customer)
							//status = "BERHASIL";
							if (status.equals("BERHASIL")){
									request.setAttribute("produk", "PA Konven");
									PrintPolisAllPelengkap ppap = new PrintPolisAllPelengkap();
									String resultEmail = ppap.emailPaKonven(request, response, elionsManager, uwManager, props, spaj);
									if (resultEmail.equals("Proses Pengiriman Email Gagal.")){
										Map mapError = new HashMap();
										mapError.put("ERROR", "Proses Autopayment PA Berhasil, namun terjadi error pada saat email Sertifikat.");
									return new ModelAndView("uw/hasilautopayment_pa",mapError);
									}									
							}
						}else{
							status = "Saldo i-BANK TIDAK CUKUP";
						}
					}else{
						status = "DATA i-BANK TIDAK DITEMUKAN/BELUM DIUPLOAD";
					}					
					Pemegang pmg = elionsManager.selectpp(spaj);
					Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
					Double ldec_sisa = bacManager.selectBillingRemain(spaj);
					String region = uwManager.select_region(pmg.getLca_id(), pmg.getLwk_id(), pmg.getLsrg_id());
					Map map2 = new HashMap();
					map2.put("SPAJ", topup.getReg_spaj());
					map2.put("NAMA_PLAN", dataUsulan.getLsdbs_name());
					map2.put("PEMEGANG", pmg.getMcl_first());
					map2.put("POLIS", pmg.getMspo_policy_no());
					map2.put("STATUS", status);
					map2.put("REMAIN", ldec_sisa);
					map2.put("REGION", region);
					dataSpajAutoPayment.add(map2);
				}
			}
			map3.put("dataSpajAutoPayment", dataSpajAutoPayment);
			session.setAttribute("dataSpajAutoPayment", dataSpajAutoPayment);
			bacManager.prosesEmailAutoPayment(dataSpajAutoPayment,0);
			return new ModelAndView("uw/hasilautopayment_pa",map3);
		} 
		
		if(request.getParameter("proses2") != null){			
		
			ArrayList data = Common.serializableList((List) session.getAttribute("dataSpajAutoPayment"));
			if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = Resources.getResourceAsFile(props.getProperty("report.uw.autopayment_pa") + ".jasper");
	    		
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		
	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("date", sysdate);
	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));
		    	
		    	response.setContentType("application/vnd.ms-excel");
		        JRXlsExporter exporter = new JRXlsExporter();
		        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		        exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
		        exporter.exportReport();		    	
	    		sos.close();
	    		
	  		}else{ //bila tidak ada data
	  			ServletOutputStream sos = response.getOutputStream();
	  			sos.println("<script>alert('Tidak ada data');window.close();</script>");
	  			sos.close();
	  		}
//			session.removeAttribute("dataSpajAutoPayment");
			return null;
//			return new ModelAndView("uw/hasilautopayment_pa",map3);
		}
		
		map.put("dataSpaj", dataSpaj);
		session.removeAttribute("dataSpajAutoPayment");
		return new ModelAndView("uw/autopayment_pa", map);
	}
	
	public ModelAndView npw_aktif(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap params = new HashMap();
		String passDefault="npwnpw";
		String success = "BERHASIL";
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String pass = ServletRequestUtils.getStringParameter(request, "pass", "");
		String keterangan = ServletRequestUtils.getStringParameter(request, "keterangan", "");
		String lspdIdBefore = ServletRequestUtils.getStringParameter(request, "lspd_id", "");
		String status = ServletRequestUtils.getStringParameter(request, "status", "");
		String position = ServletRequestUtils.getStringParameter(request, "position", "");
		String ppttg = ServletRequestUtils.getStringParameter(request, "ppttg", "");
		String type = ServletRequestUtils.getStringParameter(request, "type", "1");
//		Date now = this.elionsManager.selectSysdate();
		Date now = new Date();
		int json = ServletRequestUtils.getIntParameter(request, "json", 0);
		spaj= spaj.replace(".", "").trim();
		//json
		if(json==1 && !spaj.equals("")){
			 ArrayList<DropDown> daftarNama = new ArrayList<DropDown>();
			 daftarNama = Common.serializableList(ajaxManager.selectDataPolisNpw(spaj));
			 response.setContentType("application/json");
	    	 PrintWriter out = response.getWriter();
	    	 Gson gson = new Gson();
	    	 out.print(gson.toJson(daftarNama));
	    	 out.close();
	    	 return null;
		}
		
		if(request.getParameter("aktif") != null){
			if (pass.toUpperCase().equals(passDefault.toUpperCase())){
				success=bacManager.prosesNpwAktif(currentUser, spaj.replace(".", ""), keterangan, lspdIdBefore,Integer.parseInt(type));
			}else{
				success = "Password yg diinput salah!";
				params.put("spaj", spaj);
				params.put("keterangan", keterangan);
				params.put("lspd_id", lspdIdBefore);
				params.put("position", position);
				params.put("ppttg", ppttg);
				params.put("status", status);
			}
			
			params.put("success", success);
		}
		
		params.put("date", now);
		params.put("spaj", spaj);
		params.put("namaUSer",currentUser.getName());
		
		return new ModelAndView("uw/npw_aktif", params);
	}
	
	/**
	 * @author  : Ryan
	 * @created : 27 Sept 2015
	 * 
	 * Fungsi Proses Untuk Validasi Terhadap No polis yang sudah pernah cetak polis untuk penggantian biaya materai
	 */
	
	public ModelAndView validasimaterai(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map m = new HashMap();
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		m.put("user_id", lus_id);

		String cab_bank="";String pic="" ;String norek="";
		Integer jn_bank = -1;
		Integer error=0;
		Map data_valid = (HashMap)this.elionsManager.select_validbank(lus_id);
		if (data_valid != null)
		{
			cab_bank = (String)data_valid.get("CAB_BANK");
			jn_bank = (Integer) data_valid.get("JN_BANK");
		}

		if (cab_bank == null)
		{
			cab_bank = "";
		}
		m.put("cabang_bank", cab_bank);
		m.put("jn_bank", jn_bank);

		int dari = ServletRequestUtils.getIntParameter(request, "dari", 1);
		int sampai = ServletRequestUtils.getIntParameter(request, "sampai", 60);
		String kata = ServletRequestUtils.getStringParameter(request, "kata", "");
		String tipe = ServletRequestUtils.getStringParameter(request, "tipe", "");
		String pilter = ServletRequestUtils.getStringParameter(request, "pilter", "");
		int tambah = 0; 

		if("prev".equals(ServletRequestUtils.getStringParameter(request, "action", ""))) {
			if(dari > 60) tambah = -60;
		}else if("next".equals(ServletRequestUtils.getStringParameter(request, "action", ""))) {
			tambah = 60;
		}
		dari += tambah;
		sampai += tambah;

		List<Policy> daftarPolis = bacManager.selectDaftarPolisValidasiMaterai(user.getCab_bank(), dari, sampai);
		Integer row= daftarPolis.size();
		int myself=1;
		myself += tambah;
		Integer page = sampai/60;

		m.put("myself", myself);
		m.put("page", page);
		
		List result = bacManager.selectCabangSinarmasAll(user.getJn_bank().toString(),user.getCab_bank().trim());

		if (!result.isEmpty()){
			Map getData = (HashMap) result.get(0);
			pic=(String) getData.get("NAMA_REK_PIC");
			norek=(String) getData.get("NO_REK_PIC");
		}
		
		if (pic==null && norek==null){
			error=1;
			m.put("pesanError", "No Rek & PIC masih kosong , harap Hubungi Dewi_Andriyati@sinarmasmsiglife.co.id ");
		}
		
		if(request.getParameter("proses") != null && error==0) {
			String sukses="";
			try {
				String check[] = request.getParameterValues("chbox");
				if(request.getParameterValues("chbox") != null){
					sukses = bacManager.processValidasiDataBMaterai(check, user,request);
					daftarPolis = bacManager.selectDaftarPolisValidasiMaterai(user.getCab_bank(), dari, sampai);
					m.put("pesanError", sukses);
				}
			}catch (Exception e) {
				m.put("pesanError", "ERROR");
			}
		}
		m.put("result", result);
		m.put("dari", dari);
		m.put("sampai", sampai);
		m.put("daftarPolis", daftarPolis);

		return new ModelAndView("uw/validasimaterai", m);
	}
	
	/**
	 * @author  : Ryan
	 * @created : 27 Sept 2015
	 * 
	 * Fungsi Controlling No rekening PIC penggantian biaya materai
	 */
	public ModelAndView picrekeningbsm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map m = new HashMap();
		User user = (User) request.getSession().getAttribute("currentUser");
		String json = ServletRequestUtils.getStringParameter(request, "json","0");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		String jenis = ServletRequestUtils.getStringParameter(request, "jenis", null);
		String lcb_no = ServletRequestUtils.getStringParameter(request, "lcb_no", null);
		String e_pic = ServletRequestUtils.getStringParameter(request, "e_pic", "");
		String nama_cabang = ServletRequestUtils.getStringParameter(request, "cabang", "");
		String rekening = ServletRequestUtils.getStringParameter(request, "rekening", "");
		String flag = ServletRequestUtils.getStringParameter(request, "flag", "");
		String periode = ServletRequestUtils.getStringParameter(request, "bdate", "");
		String periode2 = ServletRequestUtils.getStringParameter(request, "edate", "");
		StringBuffer err = new StringBuffer();
		String pic = ServletRequestUtils.getStringParameter(request, "pic", "");
		String pesan ="";
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat df = new SimpleDateFormat("yyyyMM");
		if(!periode.equals("")){
			Date date = formatter.parse(periode);
			periode=df.format(date);
		}
		if(!periode2.equals("")){
			Date date = formatter.parse(periode2);
			periode2=df.format(date);
		}
		m.put("lus_id", lus_id);
		m.put("flag", flag);
		if(json.equals("1")){
			List result = bacManager.selectCabangSinarmasAll(jenis,null);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(result));
			out.close();
		}
		if(json.equals("2")){
			List result = bacManager.selectCabangSinarmasAll(jenis,lcb_no);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(result));
			out.close();
		}

		if(request.getParameter("update") != null){
			if (lcb_no.equals("")){
				err.append("- Cabang Masih Kosong ");}else
					if (e_pic.equals("")){
						err.append("- Email PIC Masih Kosong ");}else
							if (rekening.equals("")){
								err.append("- No Rekening PIC Masih Kosong ");}else
									if (rekening.length() > 10 || rekening.length() < 10){
										err.append("- No Rekening Harus 10 Digit");}else
											if (pic.equals("")){
												err.append("- PIC no rekening masih kosong ");
											}else{
												bacManager.updateDataPicRekening(lcb_no, rekening, pic , e_pic);
												err.append("- Data Berhasil Diupdate");
											}
			m.put("pesan", err);
		}else if(request.getParameter("pdf") != null){
			ArrayList data=new ArrayList();
			data=Common.serializableList(bacManager.selectDataBmaterai(periode,periode2,null,jenis, lcb_no,null));
			if(data.size()>0)
			{
				ServletOutputStream out=response.getOutputStream();
				File sourceFile = Resources.getResourceAsFile(props.getProperty("report.uw.biayaMaterai")+".jasper");
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
				Map map = new HashMap();
				map.put("bdate", periode);
				map.put("edate", periode2);
				map.put("cabang", nama_cabang);
				/*if(jenis.equals("2") || jenis.equals("3")){
					map.put("jenis","BANK SINARMAS ");
				}else if(jenis.equals("16")){
					map.put("jenis","BANK SINARMAS ");
				}else{

				}*/
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(data));
				JasperExportManager.exportReportToPdfStream(jasperPrint, out);
				if(out!=null)out.close();out.flush();
			}
			else
			{
				ServletOutputStream out=response.getOutputStream();
				out.println("<script>alert('Tidak ada data');window.close();</script>");
				if(out!=null)
					out.close();
			}
			return null;
		}else if(request.getParameter("excel") != null){
			ArrayList data=new ArrayList();
			data=Common.serializableList(bacManager.selectDataBmaterai(periode,periode2,null,jenis,lcb_no,null));
			if(data.size()>0)
			{
				ServletOutputStream out=response.getOutputStream();
				File sourceFile = Resources.getResourceAsFile(props.getProperty("report.uw.biayaMaterai")+".jasper");
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
				Map map = new HashMap();
				map.put("bdate", periode);
				map.put("edate", periode2);
				map.put("cabang", nama_cabang);

				ServletOutputStream sos = response.getOutputStream();
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanCollectionDataSource(data));
				response.setContentType("application/vnd.ms-excel");
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
				exporter.exportReport();
				if(sos!=null){
					sos.flush();
					sos.close();
				}
			}
			else
			{
				ServletOutputStream out=response.getOutputStream();
				out.println("<script>alert('Tidak ada data');window.close();</script>");
				if(out!=null)
					out.close();
			}
			return null;
		}

		return new ModelAndView("uw/picrekening", m);
	}
	
	/**
	 * @author  : Ryan
	 * @created : 01 Okt 2015
	 * 
	 * Fungsi Proses Pembayaran/Pengiriman Tagihan ke Finace
	 */
	public ModelAndView prosesbiayamaterai(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map m = new HashMap();
		List result=new ArrayList();
		DateFormat df = new SimpleDateFormat("yyyyMM");
		String sukses="";
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		User user = (User) request.getSession().getAttribute("currentUser");
		String periode = ServletRequestUtils.getStringParameter(request, "bdate", "");
		String periode2 = ServletRequestUtils.getStringParameter(request, "edate", null);
		String status = ServletRequestUtils.getStringParameter(request, "jenis_report", null);
		String filter = ServletRequestUtils.getStringParameter(request, "filter", null);
		m.put("bdate", periode);
		m.put("edate", periode2);
		if(!periode.equals("")){
			Date date = formatter.parse(periode);
			periode=df.format(date);
		}
		if(periode2!=null){
			if(!periode2.equals("")){
				Date date = formatter.parse(periode2);
				periode2=df.format(date);
			}
		}
		if(request.getParameter("btnShow") != null) {
			result = bacManager.selectDataBmaterai(periode,periode2,status,null,null,filter);
		}else if(request.getParameter("kirim") != null){
			String check[] = request.getParameterValues("chbox");
			if(request.getParameterValues("chbox") != null){
				sukses=bacManager.processBayarDataBMaterai(check, user, request, 1,periode,periode2);
			}else{
				sukses="Harap Checklist Data Yang Dipilih Terlebih Dahulu";
			}
		}else if(request.getParameter("kirim2") != null){
			String check[] = request.getParameterValues("chbox2");
			if(request.getParameterValues("chbox2") != null){
				sukses=bacManager.processBayarDataBMaterai(check, user, request, 3,periode,periode2);
			}else{
				sukses="Harap Checklist Data Susulan Yang Dipilih Terlebih Dahulu";
			}
		}else if(request.getParameter("update") != null){
			String check3[] = request.getParameterValues("chbox3");
			if(request.getParameterValues("chbox3") != null){
				for(int i=0;i<check3.length;i++){
					String posisi = ServletRequestUtils.getStringParameter(request, "posisi_"+check3[i],"");
					if(posisi.equals("2") || posisi.equals("3") ){
						sukses="Cabang";
					}

				}
				sukses=bacManager.processBayarDataBMaterai(check3, user, request, 2,periode,periode2);
			}else{
				sukses="Harap Checklist Data Yang Diupdate Terlebih Dahulu";
			}
		}

		m.put("status", status);
		m.put("result", result);
		m.put("pesan", sukses);
		return new ModelAndView("uw/viewer/biayamaterai", m);
	}
	
	public ModelAndView viewDetailbiayamaterai(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map m = new HashMap();
		User user = (User) request.getSession().getAttribute("currentUser");
		String no = ServletRequestUtils.getStringParameter(request, "no", "");
		List result = bacManager.selectDataBmateraiDet(no,user.getCab_bank().trim());
		m.put("result", result);
		return new ModelAndView("uw/viewer/detailbiayamaterai", m);
	}
	
	public ModelAndView ceknohp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap params = new HashMap();	
				
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String hp = ServletRequestUtils.getStringParameter(request, "hp", "");	
		String no_seri = ServletRequestUtils.getStringParameter(request, "seri", "");
		String bdate = ServletRequestUtils.getStringParameter(request, "bdate", "");
		String nama_pp = ServletRequestUtils.getStringParameter(request, "namapp", "");
		
		String flag="";
		String pesan ="";
		if(request.getParameter("btnSearch") != null){
			HashMap paramsPesan =  bacManager.CekNoHpNasabah(hp, bdate, nama_pp,no_seri ,currentUser);	
			pesan = (String)paramsPesan.get("PESAN");
			flag =	(String)paramsPesan.get("FLAG");
		}		
		params.put("namapp", nama_pp);
		params.put("flag", flag);
		params.put("seri", no_seri);
//		if(bdate.equals("") )params.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
		if(bdate.equals("") )params.put("bdate", defaultDateFormat.format(new Date()));
		else params.put("bdate", bdate);
		params.put("hp", hp);	
		params.put("pesan", pesan);	
		params.put("namaUSer",currentUser.getName());
		
		return new ModelAndView("uw/cekphone", params);
	}
	
	public ModelAndView back_to_lb(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap map = new HashMap();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");	
		String keterangan = ServletRequestUtils.getStringParameter(request, "ket_aksep_uw", null);
		Integer flag = ServletRequestUtils.getIntParameter(request, "flag");
		List<String> pesan = new ArrayList<String>();
		if(request.getParameter("save")!= null) {
			if(keterangan==null){
				pesan.add("Keterangan harus diisi");
			}else{
				Integer flag_pesan = bacManager.prosesBackToLb(spaj, currentUser, keterangan, flag);
				if(flag_pesan==1){
					pesan.add("Back to life benefit tidak dapat dilakukan karena posisi snows ada di life benefit");
				}else{
					pesan.add("Back to life benefit berhasil dilakukan");
				}
			}
		}
		map.put("flag", flag);
		map.put("pesan", pesan);
		map.put("infoEndorsNew", uwManager.selectMsen_aksep_uw_new(spaj));
		map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
		return new ModelAndView("uw/back_to_lb", map);
	}
	
	public ModelAndView inputattlist(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap map=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String s_spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		ArrayList error =new ArrayList();
		if(request.getParameter("input")!=null){
			s_spaj = request.getParameter("spaj");
			String alasan = request.getParameter("alasan");			
			Map mapTt=elionsManager.selectTertanggung(s_spaj);
			Integer lssaId=(Integer)mapTt.get("LSSA_ID");
			Tertanggung ttg=elionsManager.selectttg(s_spaj);
			map.put("alasan", alasan);
			if(alasan==null || alasan.equals("")){
				error.add("Alasan harus Diisi");
			}
			if(lssaId!=2){
				error.add("Status SPAJ ini Belum Decline");
			}
			Integer blacklistCount = uwManager.selectExistBlacklist(defaultDateFormat.format(ttg.getMspe_date_birth()), ttg.getMcl_first(), ttg.getMspe_no_identity());
			if(blacklistCount>0){
				error.add("Data ini sudah pernah diinput ke daftar attention list");
			}
			if(error.isEmpty()){
				try{
					CmdInputBlacklist cmd =new CmdInputBlacklist();
					cmd.setBlacklist(new BlackList());
					cmd.setClient(new Client());
					cmd.setAddressNew(new AddressNew());
					cmd.setBlacklistfamily(new BlackListFamily());
					cmd.setCurrentUser(currentUser);
					ArrayList detBlackList=new ArrayList<DetBlackList>();
					cmd.setDetBlackListAll(detBlackList);
					cmd.getBlacklist().setLbl_sumber_info("1");
					cmd.getBlacklist().setLbl_sumber_info2("1");
					cmd.getBlacklist().setLbl_sumber_informasi("SPAJ DECLINE");
					cmd.getBlacklist().setLbl_nb_process("0");
					cmd.getBlacklist().setLbl_sts_cust("2");
					cmd.getBlacklist().setNo_policy(ttg.getMspo_policy_no());
					cmd.getBlacklist().setReg_spaj(s_spaj);
					cmd.getBlacklist().setLbl_nama_alias_1(ttg.getMcl_first_alias());
					cmd.getBlacklist().setLbl_flag_alasan("7");
					cmd.getBlacklist().setLbl_alasan(alasan.toString());
					
					cmd.getClient().setMcl_first(ttg.getMcl_first());
					cmd.getClient().setMspe_date_birth(ttg.getMspe_date_birth());
					cmd.getClient().setMspe_place_birth(ttg.getMspe_place_birth());
					cmd.getClient().setMspe_sex(ttg.getMspe_sex());
					cmd.getClient().setMspe_sts_mrt(ttg.getMspe_sts_mrt());
					cmd.getClient().setLside_id(ttg.getLside_id());
					cmd.getBlacklist().setLbl_no_identity("");
					cmd.getBlacklist().setLbl_no_identity_sim("");
					cmd.getBlacklist().setLbl_no_identity_paspor("");
					cmd.getBlacklist().setLbl_no_identity_kk("");
					cmd.getBlacklist().setLbl_no_identity_akte_lahir("");
					cmd.getBlacklist().setLbl_no_identity_kims_kitas("");
					if(cmd.getClient().getLside_id() == 1){
						cmd.getBlacklist().setLbl_no_identity(ttg.getMspe_no_identity());
					}else if(cmd.getClient().getLside_id() == 2){
						cmd.getBlacklist().setLbl_no_identity_sim(ttg.getMspe_no_identity());
					}else if(cmd.getClient().getLside_id() == 3){
						cmd.getBlacklist().setLbl_no_identity_paspor(ttg.getMspe_no_identity());
					}else if(cmd.getClient().getLside_id() == 4){
						cmd.getBlacklist().setLbl_no_identity_kk(ttg.getMspe_no_identity());
					}else if(cmd.getClient().getLside_id() == 5){
						cmd.getBlacklist().setLbl_no_identity_akte_lahir(ttg.getMspe_no_identity());
					}else if(cmd.getClient().getLside_id() == 7){
						cmd.getBlacklist().setLbl_no_identity_kims_kitas(ttg.getMspe_no_identity());
					}
					cmd.getClient().setMkl_kerja(ttg.getMkl_kerja());
					
					//address new
					cmd.getAddressNew().setAlamat_kantor(ttg.getAlamat_kantor());
					cmd.getAddressNew().setAlamat_rumah(ttg.getAlamat_rumah());
					cmd.getAddressNew().setArea_code_rumah(ttg.getArea_code_rumah());
					cmd.getAddressNew().setArea_code_kantor(ttg.getArea_code_kantor());
					cmd.getAddressNew().setNo_hp(ttg.getNo_hp());
					cmd.getAddressNew().setTelpon_kantor(ttg.getTelpon_kantor());
					cmd.getAddressNew().setTelpon_rumah(ttg.getTelpon_rumah());
					cmd.getAddressNew().setEmail(ttg.getEmail());
					
					cmd.getBlacklistfamily().setLblf_nama_si(ttg.getNama_si());
					cmd.getBlacklistfamily().setLblf_tgllhr_si(ttg.getTgllhr_si());
					cmd.getBlacklistfamily().setLblf_nama_anak1(ttg.getNama_anak1());
					cmd.getBlacklistfamily().setLblf_tgllhr_anak1(ttg.getTgllhr_anak1());
					cmd.getBlacklistfamily().setLblf_nama_anak2(ttg.getNama_anak2());
					cmd.getBlacklistfamily().setLblf_tgllhr_anak2(ttg.getTgllhr_anak2());
					cmd.getBlacklistfamily().setLblf_nama_anak3(ttg.getNama_anak3());
					cmd.getBlacklistfamily().setLblf_tgllhr_anak3(ttg.getTgllhr_anak3());
				
					String clientId = uwManager.prosesInputBlacklist(cmd, currentUser);
					HashMap mapData = (HashMap) uwManager.selectBlacklist(clientId, "mcl_id", null).get(0);
					BigDecimal lbl_id = (BigDecimal)mapData.get("LBL_ID");
					if(lbl_id!=null){
						elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "INPUT ATTENTION LIST ID : "+lbl_id, s_spaj, 0);
					}
					error.add("Data Berhasil Di Simpan");
				}catch (Exception e) {
					//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					error.add("Gagal Save");
				}
				
			}
		}
		
		map.put("spaj",s_spaj);
		map.put("err", error);
		return new ModelAndView("uw/input_att_list", map);
		
	}
	
	class ThreadPrint extends Thread implements Serializable{
		private static final long serialVersionUID = -2208935165403596233L;
		private Thread t;
		   private String printerNameThread;
		   private String pdfFileThread;
		   private String allowPrintThread;
		   private String ThreadPrint;
		   private String spajPrint;
		   private Integer CountPrint;
		   private String lusIdPrint;
		   
		   ThreadPrint(String printerName,String name,String pdfFile,String allowPrint,String spaj,String lusId,Integer Count){
			   ThreadPrint = name;
			   printerNameThread = printerName;
		       pdfFileThread =pdfFile;
			   allowPrintThread = allowPrint;
			   spajPrint = spaj;
			   CountPrint = Count;
			   lusIdPrint = lusId;
		   }
		   
		   public void run() {
		      if(Print.directPrint(pdfFileThread, printerNameThread, allowPrintThread)){
		    	  bacManager.insertPrintHistory(spajPrint, "POLIS ALL", CountPrint, lusIdPrint);
			  }

		   }
		   
		   public void start ()
		   {
		      if (t == null)
		      {
		         t = new Thread (this, ThreadPrint);
		         t.start ();
		      }
		   }
		}
	
	public ModelAndView view_attentionlist(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();		
		User currentUser = (User) request.getSession().getAttribute("currentUser");			
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj");
		Pemegang cls_pmg = elionsManager.selectpp(spaj);
		ArrayList blacklist = Common.serializableList(this.uwManager.selectBlacklist(cls_pmg.getMcl_first(), "mcl_first", cls_pmg.getMspe_date_birth()));

		map.put("blacklist", blacklist);
		map.put("spaj", spaj);
		return new ModelAndView("uw/view_kesehatanALL",map);
	}	
	
	/**
	 * Controller untuk proses Speedy Akan Tetapi Unclean Untuk Workflow Yang Baru
	 * @author Ryan F
	 * @since 4 Aug 2014
	 * @param request
	 * @param response
	 * @return
	 * @link http://localhost/E-Lions/uw/uw.htm?window=prosesSpeedyUnclean
	 */
	public ModelAndView prosesSpeedyUnclean(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap hm_map = new HashMap();
		HashMap validasi = new HashMap();
		BindException err = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Double ldecPremi,ldecBill = null;
		List bill;
		String s_spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String lca_id = elionsManager.selectCabangFromSpaj(s_spaj);
		Integer flag = ServletRequestUtils.getIntParameter(request, "flag",1);
		Map mapAutoAccept=null;
		Integer i_posisi = elionsManager.selectPosisiDokumenBySpaj(s_spaj);
		Insured ins = bacManager.selectMstInsuredAll(s_spaj);
		Pemegang pmg = elionsManager.selectpp(s_spaj);
		Tertanggung tertanggung = elionsManager.selectttg(s_spaj);
		Datausulan dataUsulan = elionsManager.selectDataUsulanutama(s_spaj);
		HashMap mTertanggung = Common.serializableMap(elionsManager.selectTertanggung(s_spaj));
		Date jan2016 = defaultDateFormat.parse(props.getProperty("jan.2016"));
		Integer status_polis = this.elionsManager.selectPositionSpaj(s_spaj);

		if(flag==1){//NB
			//validasi2 in dulu nie sebelum proses
			/**cek nominal billing , jika collection udh approve dan UW/NB belum, maka data nya dicocokan.
			 * takut ga sama, krn dibatalkan kalau tidak sama
			 */
			validasi=(HashMap) bacManager.validasiNewBusiness(s_spaj, currentUser,flag);
			if(!validasi.isEmpty()){
				hm_map.put("lsError",validasi.toString());
			}
			// harus > dari tanggal cut off
			if(pmg.getMspo_input_date().before(jan2016)){
				hm_map.put("lsError","- Proses Ini Hanya Berlaku Untuk Polis > " + jan2016);
			}
			//harus accept dulu *krn dari UW PROSES
			if(ins.getLssa_id()!=5){
				hm_map.put("lsError","- Polis Ini Belum Accept, Mohoh Di Accept Terlebih Dahulu ");
			}
			
			if(hm_map.isEmpty()){
				mapAutoAccept =bacManager.ProsesParalelUnclean(flag, s_spaj, 1,pmg,tertanggung, dataUsulan, currentUser,request,err,elionsManager,uwManager);
				hm_map.put("successMessage", mapAutoAccept.get("success").toString());
				hm_map.put("lsError", mapAutoAccept.get("error").toString());
				if(mapAutoAccept.get("error").toString().isEmpty()){
					hm_map.put("flag_sukses", 6);
				}				
			}
		}
		PrintPolisPrintingController ppc = new PrintPolisPrintingController();
		//Proses Direct Print
		if(mapAutoAccept!=null){
			//20190130 Mark Valentino 183,189 Tidak Direct Print
			if((dataUsulan.getLsbs_id() == 183) && (dataUsulan.getLsdbs_number() >= 46 && dataUsulan.getLsdbs_number() <= 60)){
				return new ModelAndView("common/info_proses", hm_map);				
			}			
			if(mapAutoAccept.get("flag_print").toString().equals("1")){
				int isInputanBank = elionsManager.selectIsInputanBank(s_spaj);
				Integer count = elionsManager.selectCountProductSimasPrimaAkseptasiKhusus(s_spaj, 1,10, isInputanBank);			
				String flag2 = "0";
				String businessId = uwManager.selectBusinessId(s_spaj);
				String businessNo = uwManager.selectLsdbsNumber(s_spaj);
				String pesanKemenangan="";
				Map tambahan = new HashMap();
				// PROJECT: POWERSAVE & STABLE LINK 			
				if(isInputanBank==2 || isInputanBank==3 || (businessId.equals("175") && businessNo.equals("2")) || (businessId.equals("73") && businessNo.equals("14"))) {
					//if(products.powerSave(businessId) || products.stableLink(businessId)){
					if(businessId.equals("73") && businessNo.equals("14")){//MANTA (13/01/2014) - Request Andy
						pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Filling.";
					}else if(isInputanBank==2){
						pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis.";
					}else if(products.productBsmFlowStandardIndividu(Integer.parseInt(businessId), Integer.parseInt(businessNo))){
						pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis.";
					}else{
						if(count>0){ //AKSEPTASI KHUSUS
							pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis. Status masih Akseptasi Khusus.";
						}else { //AKSEPTASI BIASA
							pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Input Tanda Terima.";
						}
						tambahan.put("transferTTP", true);
						tambahan.put("spaj", s_spaj);
						flag2 ="1";
					}
				}else if(businessId.equals("157") ) {
					pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Input Tanda Terima.";
				}else if("187,203,209".indexOf(businessId) > -1) {
					Map m = uwManager.selectInformasiEmailSoftcopy(s_spaj);
					String errorRekening = elionsManager.cekRekAgen2(s_spaj);
					String email = (String) m.get("MSPE_EMAIL");
					String keterangan = null;
					if(email != null) {
						if((businessId.equals("187") && "5,6".indexOf(businessNo)>-1)){
							keterangan = "Filling.Softcopy Telah dikirimkan ke : " + email;
						}else{
							if(!errorRekening.equals("")) {
								keterangan = "PROSES CHECKING TTP (REKENING AGEN MASIH KOSONG).Softcopy Telah dikirimkan.";
							}else{
								keterangan = "Komisi (Finance).Softcopy Telah dikirimkan.";
							}
						}
						pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke " + keterangan;
					}else{
						if((businessId.equals("187") && "5,6".indexOf(businessNo)>-1)){
							keterangan = "Filling.";
						}else{
							if(!errorRekening.equals("")) {
								keterangan = "PROSES CHECKING TTP (REKENING AGEN MASIH KOSONG).Softcopy Telah dikirimkan.";
							}else{
								keterangan = "Komisi (Finance).";
							}	
						}
						pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke " + keterangan;
					}
				}else {
					pesanKemenangan = "SPAJ nomor " + s_spaj + " berhasil ditransfer ke Print Polis.";
				}

				Integer mspo_provider= uwManager.selectGetMspoProvider(s_spaj);
				String LusId = currentUser.getLus_id();
				String lde_id = currentUser.getLde_id();
				
				//20190408 Mark Valentino DISABLE DIRECT PRINT
//				try{
//					String pesanDirectPrint = "";
//					if( businessId.equals("163") || ( businessId.equals("143") && ("1,2,3,7".indexOf(businessNo)>-1) && lca_id.equals("01") ) || (businessId.equals("144") && businessNo.equals("1") && lca_id.equals("01") ) ||
//						businessId.equals("183") || (businessId.equals("142") && lca_id.equals("01")) || (businessId.equals("177") && businessNo.equals("4")))
//					{
//						if("11,39".indexOf(lde_id)>-1){
//							String email = currentUser.getEmail();
//							Integer flagprint = 1;
//							if(businessId.equals("177") && businessNo.equals("4")) flagprint = 4; //Khusus PT INTI
//
//							elionsManager.updatePolicyAndInsertPositionSpaj(s_spaj, "mspo_date_print", LusId, 6, 1, "PRINT POLIS (E-LIONS) DIRECT PRINT", true, currentUser);
//
//							bacManager.generateReport(request, mspo_provider, flagprint, elionsManager, uwManager, 0, null);
//
//							HashMap<String, Object> printer = (HashMap<String, Object>) this.bacManager.selectPropertiesPrinter();
//							String ipAddress = (String) printer.get("IP_ADDRESS");
//							String printerName = (String) printer.get("PRINTER_NAME");
//
//							String cabang = elionsManager.selectCabangFromSpaj(s_spaj);
//							String allowPrint = this.bacManager.getAllowPrint(printerName);
//							String pdfFile = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+s_spaj+"\\"+"PolisAll.pdf";
//
//							try{
//								ThreadPrint T1 = new ThreadPrint(printerName,"directPrint",pdfFile,allowPrint,s_spaj,currentUser.getLus_id(),Print.getCountPrint(pdfFile));
//								T1.start();
//							}catch(Exception e){
//								EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
//										null, 0, 0, new Date(), null, false,
//										props.getProperty("admin.ajsjava"),
//										new String[]{"ryan@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id;antasari@sinarmasmsiglife.co.id;randy@sinarmasmsiglife.co.id;ridhaal@sinarmasmsiglife.co.id;trifena_y@sinarmasmsiglife.co.id"},
//										null,
//										null,  
//										"Error Thread print", 
//										e+"", null, s_spaj);
//							}
//
//							pesanDirectPrint = bacManager.prosesPrint(s_spaj,cabang,ipAddress,printerName);
//							hm_map.put("pesanDirectPrint", pesanDirectPrint);
//						}
//					}
//				}catch(Exception e){
//					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
//							null, 0, 0, new Date(), null, false,
//							props.getProperty("admin.ajsjava"),
//							new String[]{"ryan@sinarmasmsiglife.co.id;deddy@sinarmasmsiglife.co.id;antasari@sinarmasmsiglife.co.id;randy@sinarmasmsiglife.co.id;ridhaal@sinarmasmsiglife.co.id;trifena_y@sinarmasmsiglife.co.id"},
//							null,
//							null,  
//							"Error Direct Print Transfer "+s_spaj, 
//							e+"", null, s_spaj);
//					logger.error("ERROR :", e);
//				}
				hm_map.put("pesanKemenangan", pesanKemenangan);
			}
		}
		//return new ModelAndView("uw/prosesSpeedy", hm_map);
		return new ModelAndView("common/info_proses", hm_map);

	}
	
	
	/**
	 * Controller untuk rider SMile Baby
	 * @author Randy
	 * @since 8 Aug 2016
	 */
	public ModelAndView prosesSmileBaby(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HashMap param = new HashMap();
		BindException err = null;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lus_id = (currentUser.getLus_id());
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		Pemegang pp = elionsManager.selectpp(spaj);
		Tertanggung tt = elionsManager.selectttg(spaj);
		HashMap mTertanggung = Common.serializableMap(elionsManager.selectTertanggung(spaj));
		Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
		
		try{
			List<Datarider> xx = dataUsulan.getDaftaRider();
			Integer sb = 0 ;
			param.put("namapp",pp.getMcl_first());
			param.put("namatt",tt.getMcl_first());
			param.put("spaj",spaj);
			HashMap tgl_kr_lhr = bacManager.selectMstTransHistoryNewBussines(spaj);
			
			if (tgl_kr_lhr.get("TGL_PERKIRAAN_LAHIR") == null) {
				if (dataUsulan.getDaftaRider().size() != 0) {
					for (Datarider datarider : xx) {
						if (datarider.getLsbs_id() == 836) {
							Integer lsbs = datarider.getLsbs_id();
							param.put("lsbs", lsbs);
							sb = sb + 1;
						} else {
							sb = sb + 0;
						}
					}
					if (sb == 0) param.put("err", "Tidak dapat diisi untuk polis ini");
				} else {
					param.put("err", "Tidak dapat diisi untuk polis ini");
				}
			}else{
				param.put("err", "Sudah diinput tanggal perkiraan lahir.");
			}
			
			if(request.getParameter("save")!=null){
				String hpl = ServletRequestUtils.getStringParameter(request, "bdate");
				String mggu = ServletRequestUtils.getStringParameter(request, "mggu");
				String msps_desc = "INPUT TANGGAL PERKIRAAN LAHIR : "+hpl+", UMUR KEHAMILAN : "+mggu+" MINGGU";
				
				param.put("perkiraan", hpl);
				param.put("mggu", mggu);
				
				Integer testmggu = Integer.parseInt(mggu);
				if(testmggu>=20 && testmggu <= 32){
					String tggl_hpl = bacManager.selectTanggalHpl(spaj);
					if (tggl_hpl == null){
						bacManager.updateTglSmileBaby(spaj,hpl);
						uwManager.insertMstPositionSpajRenewal(lus_id,spaj,msps_desc);
					}
				return new ModelAndView(new RedirectView(request.getContextPath()
						+ "/uw/view.htm?p=v&showSPAJ="+spaj));
				}else{
//					param.put("err", "Kanduangan tidak sesuai dengan Kententuan Produk");
				}
			}
		}catch(Exception e){
			logger.error("ERROR :", e);
		}


		return new ModelAndView("uw/smile_baby",param);
	}
	
	/**
     * Upload SSU / SSK
     * @author Randy
     * http://localhost/E-Lions/uw/uw.htm?window=upload_ssu
     * November 2016
     */
    public ModelAndView upload_ssu(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HashMap param = new HashMap();
		String err = "";
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List cari_lsbs=new ArrayList();
		List cari_lsdbs=new ArrayList();
		List scan=new ArrayList();
		List<Scan> daftarScan =null;
		Upload upload = new Upload(); 
		String fileName,prod, sub, pdfname, putable;
		Integer fl;
//		String folder =  "C:\\EkaWeb\\ss_smile_test";
		String folder =  "\\\\Ebserver\\pdfind\\ss_smile";
		param.put("jumlah",0);
		
		try{
			if(request.getParameter("save")!=null){
				fl = ServletRequestUtils.getIntParameter(request, "fl");//jumlah file
	            for(int i=1; i<=fl; i++) {
		            	
//					prod = ServletRequestUtils.getStringParameter(request, "nameprod"+i,"");
//					sub = ServletRequestUtils.getStringParameter(request, "namesub"+i,"");
					pdfname = ServletRequestUtils.getStringParameter(request, "pdfname"+i,"");
					putable = ServletRequestUtils.getStringParameter(request, "putable"+i,"");
					if(i==1){
						String spcase = ServletRequestUtils.getStringParameter(request, "sprod"+i,"");
						if (!spcase.equals(".pdf") && !spcase.equals("")) pdfname = spcase;
					}
					
//					if (!prod.equals("") && !sub.equals("")){
					if (!pdfname.equals("")){
						File destDir = new File(folder);
//						prod = pdfname.substring(0,3);
//						sub = pdfname.substring(4,7);
						fileName = pdfname;
						String str = pdfname.substring(0, pdfname.length()-4);
						
						upload.setDaftarFile(new ArrayList<MultipartFile>(fl));
						ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
						binder.bind(request);
		            	
						MultipartFile file = (MultipartFile) upload.getFile1();
						if(i==2){ file = (MultipartFile) upload.getFile2(); }
						else if(i==3){ file = (MultipartFile) upload.getFile3(); }
						else if(i==4){ file = (MultipartFile) upload.getFile4(); }
						else if(i==5){ file = (MultipartFile) upload.getFile5(); }
						else if(i==6){ file = (MultipartFile) upload.getFile6(); }
						else if(i==7){ file = (MultipartFile) upload.getFile7(); }
						else if(i==8){ file = (MultipartFile) upload.getFile8(); }
						else if(i==9){ file = (MultipartFile) upload.getFile9(); }
						else if(i==10){ file = (MultipartFile) upload.getFile10(); }
						
			            if(!file.isEmpty()){
			            	
							File file_lama = new File(destDir+"\\"+fileName);
							if (file_lama.exists()){
								SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
								String upload_id = bacManager.getCounterSsu(6,"01",0);
								String file_to_history = str+"-"+df.format(new Date())+"-"+upload_id.substring(upload_id.length() - 3);
								File history = new File(folder+"\\HISTORY");
								String dest_hist = history.getPath() +"\\"+ file_to_history+".pdf"; 
								File outputFile_hist = new File(dest_hist);
								FileUtils.copy(file_lama, outputFile_hist);
							}
			            	
							String extention = ".pdf";				
							String namaFileUpload = file.getOriginalFilename();				
							if(namaFileUpload.length()!=0){	
								String fileExtention = namaFileUpload.substring(namaFileUpload.length() - extention.length(), namaFileUpload.length());
								if(!fileExtention.toLowerCase().equals(extention)){
									param.put("err", " File SSU/SSK harus dalam bentuk PDF." );							
									return new ModelAndView("uw/upload_ssu", param);
								}
								String dest = destDir.getPath() +"\\"+ fileName; 
								File outputFile = new File(dest);
							    FileCopyUtils.copy(file.getBytes(), outputFile);
							}
			            }else{
			            	err = "File belum dipilih.";
			            }
						if(err.equals("")){
							param.put("scc"+i,fileName+" telah berhasil diupload.");
							SimpleDateFormat dfh = new SimpleDateFormat("dd/MM/yyyy");
							Date tgl_upload = dfh.parse(dfh.format(new Date()));
							String folder_hist = folder+"\\"+fileName;
							
							String prodtable = putable.substring(0,3);
							String subtable = putable.substring(4,7);
							String namaprodtable = ajaxManager.selectNamaProduk(prodtable, subtable);
							
							//counter untuk upload id
							String upload_id = bacManager.getCounterSsu(6,"01",1);
						    uwManager.insertLampiranMstHistoryUpload( upload_id,upload_id,upload_id,fileName,putable,null,"SSU","AKTIF",null,
						    			Integer.parseInt(currentUser.getLus_id()), "SSU/SSK", folder_hist, null, namaprodtable, tgl_upload);
						    
						    param.put("page", 1);
						    param.put("jumlah",fl-1);
						}else{
//							param.put("prod",prod);
//							param.put("sub",sub);
							param.put("error"+i, err);
							param.put("page", 1);
							param.put("jumlah",fl-1);
						}
					}else{
						err = "Kode produk / sub produk belum dipilih.";
		            }
//					Integer hasil = uwManager.selectProsesUploadScan (reg_spaj, upload, destDir, daftarScan,filenames, currentUser);//true berarti berhasil, false gagal.
				}//end of for
			    
			}else if(request.getParameter("history")!=null){
			//HISTORY UPLOAD	
				List hist_upload=new ArrayList();
				hist_upload=bacManager.selectHistoryUploadSsu();
		    	param.put("hist_upload", hist_upload);
		    	param.put("page", 3);
			}else if(request.getParameter("view")!=null){
			//VIEW DOCUMENT
				List<DropDown> daftarFileSSU = FileUtils.listFilesInDirectorySSU(folder);
				param.put("daftarFileSSU", daftarFileSSU);
				String folder_ssk =folder+"\\RIDER";
				List<DropDown> daftarFileSSK = FileUtils.listFilesInDirectorySSU(folder_ssk);
				param.put("daftarFileSSK", daftarFileSSK);
				param.put("page", 2);
				
				cari_lsbs = ajaxManager.selectLsbsSrc();
				param.put("cari_lsbs", cari_lsbs);
//				cari_lsdbs =
//				File file = PDFViewer.productFile();
//				File file = PDFViewer.productFile(elionsManager,uwManager, dirSsk, lsbs_id, lsdbs, m,spaj, props);
				
			}else{
//				lsbs=bacManager.selectLsd
				param.put("page", 1);
			}
		
		}catch(Exception e){
			logger.error("ERROR :", e);
		}
		
		
		return new ModelAndView("uw/upload_ssu",param);
    }
	
	/**
     * Report Daily Aksep
     * @author Randy
     * @since 26 Sept 2016
     * @param 
     * http://localhost/E-Lions/uw/uw.htm?window=report_daily_aksep
     */
    public ModelAndView report_daily_aksep(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	Map<String, Object> m = new HashMap<String, Object>();
    	SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	    	
//    	String bdate = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
//		String edate = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	String bdate = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(new Date()));
		String edate = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(new Date()));
		String lus_id = ServletRequestUtils.getStringParameter(request, "users","");
		
		List datauser=new ArrayList();
		datauser=bacManager.selectReportUserDailyAksep();

		if(request.getParameter("showReport") != null){
    		ArrayList data = new ArrayList();
    			data = Common.serializableList(bacManager.selectReportDailyAksep(bdate, edate, lus_id));
    			String report = props.getProperty("report.uw.report_daily_aksep") + ".jasper";
    		
    		
    		if(data.size() > 0){ //bila ada data
    			ServletOutputStream sos = response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(report);
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		
	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);
	    		params.put("pdate", dt.format(new Date()));

	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

		    	if(request.getParameter("showXLS") != null){
		    		response.setContentType("application/vnd.ms-excel");
		            JRXlsExporter exporter = new JRXlsExporter();
		            exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		            exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
		            exporter.exportReport();
		    	}else if(request.getParameter("showPDF") != null){
		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
		    	}
	    		sos.close();
    		}else{ //bila tidak ada data
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
    		}
    		return null;
		}
    	
    	m.put("bdate", bdate);
    	m.put("edate", edate);
    	m.put("datauser", datauser);
    	return new ModelAndView("report/reportDailyAksep",m);
    }
    
    /*
	protected Connection connection = null;
	protected Connection getConnection() {
		if(this.connection==null) {
			try {
				this.connection = this.elionsManager.getUwDao().getDataSource().getConnection();
			} catch (SQLException e) {
				logger.error("ERROR :", e);
			}
		}
		return this.connection;
	}
	*/
    
	/**
     * Report Followup Billing
     * @author Randy
     * @since 1 November 2016
     * @param 
     * http://localhost/E-Lions/uw/uw.htm?window=report_followup_billing
     */
    public ModelAndView report_followup_billing(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	String lusID = currentUser.getLus_id();
    	Map<String, Object> m = new HashMap<String, Object>();
    	SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	    	
//    	String tgl1 = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
//		String tgl2 = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
    	String tgl1 = ServletRequestUtils.getStringParameter(request, "bdate",defaultDateFormat.format(new Date()));
		String tgl2 = ServletRequestUtils.getStringParameter(request, "edate",defaultDateFormat.format(new Date()));
		String prod = ServletRequestUtils.getStringParameter(request, "prod",""); //1:prioritas 2:non-prioritas 3:All
		String tipe = ServletRequestUtils.getStringParameter(request, "tipe",""); //1:followup  2:outstanding 3:hasil follow up
		String status = ServletRequestUtils.getStringParameter(request, "status",""); //1:all   2:inforce     3:lapse
		
		String prod_p = "", tipe_p = "", status_p = "";;
		if(prod.equals("1")){prod_p = "SMiLe PRIORITAS";}
		else if(prod.equals("2")){prod_p = "SMiLe NON-PRIORITAS";}
		else if(prod.equals("3")){prod_p = "ALL PRODUCTS";}
		
		if(tipe.equals("1")){tipe_p = "FOLLOW UP";}
		else if(tipe.equals("2")){tipe_p = "OUTSTANDING";}
		else{tipe_p = "HASIL FOLLOW UP BAS";}
		
		if(status.equals("1")){status_p = "ALL";}
		else if(status.equals("2")){status_p = "INFORCE";}
		else{status_p = "LAPSE";}
		
		if(request.getParameter("showReport") != null){
			String report; 
    		ArrayList data = new ArrayList();
    		if(tipe.equals("3")){
    			data = Common.serializableList(bacManager.selectReportHasilFollowUpBilling(tgl1, tgl2, prod, status)); 
    			report = props.getProperty("report.uw.report_followup_billing_hasil") + ".jasper";
    		}else{
        		data = Common.serializableList(bacManager.selectReportFollowUpBillingCol(tgl1, tgl2, prod, tipe, status));
        		report = props.getProperty("report.uw.report_followup_billing") + ".jasper";
    		}
    		
    		if(data.size() > 0){ //bila ada data
    			if(request.getParameter("kirim") != null){
    				int kirimemail = bacManager.report_fu_bas(tgl1,tgl2,prod,tipe,status);
    				if(kirimemail==0){
//    					m.put("err", "Email gagal dikirim. Mohon coba kembali");
    	    			ServletOutputStream sos = response.getOutputStream();
    	    			sos.println("<script>alert('Email gagal dikirim. Mohon coba kembali');window.close();</script>");
    	    			sos.close();
    				}else if(kirimemail==1){
//    					m.put("err", "Email telah berhasil dikirim");
    	    			ServletOutputStream sos = response.getOutputStream();
    	    			sos.println("<script>alert('Email telah berhasil dikirim');window.close();</script>");
    	    			sos.close();
    				}
    			}else if(request.getParameter("summary") != null){
    				String report_sum = props.getProperty("report.uw.report_followup_billing_sum") + ".jasper";
    				ArrayList data_sum = new ArrayList();
    				data_sum = Common.serializableList(bacManager.selectReportFollowUpBillingSum(tgl1, tgl2, prod, tipe, status));
    				ServletOutputStream sos = response.getOutputStream();
        			File sourceFile = Resources.getResourceAsFile(report_sum);
    	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
    	    		
    	    		Map<String, Object> params = new HashMap<String, Object>();
    	    		params.put("bdate", tgl1);
    	    		params.put("edate", tgl2);
    	    		params.put("prod_p", prod_p);
    	    		params.put("tipe_p", tipe_p);
    	    		params.put("status_p", status_p);
    	    		params.put("pdate", dt.format(new Date()));
    	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data_sum));
    	    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
    	    		sos.close();

    			}else{
    				
        			File sourceFile = Resources.getResourceAsFile(report);
    	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
    	    		
    	    		Map<String, Object> params = new HashMap<String, Object>();
    	    		params.put("bdate", tgl1);
    	    		params.put("edate", tgl2);
    	    		params.put("prod_p", prod_p);
    	    		params.put("tipe_p", tipe_p);
    	    		params.put("status_p", status_p);
    	    		params.put("pdate", dt.format(new Date()));


    		    	if(request.getParameter("showXLS") != null){
		    			Report reports;
		    			reports = new Report("Report Follow Up Billing",props.getProperty("report.uw.report_followup_billing"),Report.XLS, null);
		    			
		    			String status_q = "1=1";
		    			if(status.equals("2")){status_q = "( EKA.MST_POLICY.LSSP_ID = 1 )";}
		    			else if(status.equals("3")){status_q = "( EKA.MST_POLICY.LSSP_ID = 14 )";}
		    			
		    			String prod_q = "1=1";
		    			if(prod.equals("1")){
		    				prod_q = "(( EKA.MST_POLICY.LCA_ID IN (37) AND EKA.MST_POLICY.LWK_ID IN('M1') AND EKA.MST_POLICY.LSRG_ID IN('00','01','02','03','04')) "+
		    	                    	"or ( EKA.MST_POLICY.LCA_ID IN (66) AND EKA.MST_POLICY.LWK_ID IN('00') AND EKA.MST_POLICY.LSRG_ID IN('B2')))";
		    				/*Region:
			    				BP/AP SMILE PRIORITAS AGENCY
			    				SMILE PRIORITAS AGENCY
			    				SMILE PRIORITAS AGENCY 1
			    				SMILE PRIORITAS AGENCY 2
			    				SMILE PRIORITAS AGENCY 3
			    				SMILE PRIORITAS AGENCY 4*/
		    			}else if(prod.equals("2")){
		    				prod_q = "NOT (( EKA.MST_POLICY.LCA_ID IN (37) AND EKA.MST_POLICY.LWK_ID IN('M1') AND EKA.MST_POLICY.LSRG_ID IN('00','01','02','03','04')) "+
		    	                    	"or ( EKA.MST_POLICY.LCA_ID IN (66) AND EKA.MST_POLICY.LWK_ID IN('00') AND EKA.MST_POLICY.LSRG_ID IN('B2')))";
		    			}
		    			
		    			String tipe_q = "1=1";
		    			String q1 = "1=1";String q2 = "1=1";String q3 = "1=1";String q4 = "1=1";
		    			if(tipe.equals("1")){
		    				tipe_q = "( (EKA.MST_BILLING.MSBI_BEG_DATE BETWEEN TO_DATE('"+tgl1+"','DD/MM/YYYY') AND TO_DATE('"+tgl2+"','DD/MM/YYYY'))";
		    				q1 = "between add_months(to_date('"+tgl1+"','dd/MM/yyyy'),-6) and add_months(to_date('"+tgl2+"','dd/MM/yyyy'),-6)";
		    				q2 = "between add_months(to_date('"+tgl1+"','dd/MM/yyyy'),-12) and add_months(to_date('"+tgl2+"','dd/MM/yyyy'),-12)";
		    				q3 = "between add_months(to_date('"+tgl1+"','dd/MM/yyyy'),-3) and add_months(to_date('"+tgl2+"','dd/MM/yyyy'),-3)";
		    				q4 = "between add_months(to_date('"+tgl1+"','dd/MM/yyyy'),-1) and add_months(to_date('"+tgl2+"','dd/MM/yyyy'),-1)";
		    			}else if(tipe.equals("2")){
		    				tipe_q = "( (EKA.MST_BILLING.MSBI_BEG_DATE between add_months(to_date('"+tgl1+"','dd/MM/yyyy'),-1) and add_months(to_date('"+tgl2+"','dd/MM/yyyy'),-1))";
		    				q1 = "between add_months(to_date('"+tgl1+"','dd/MM/yyyy'),-7) and add_months(to_date('"+tgl2+"','dd/MM/yyyy'),-7)";
		    				q2 = "between add_months(to_date('"+tgl1+"','dd/MM/yyyy'),-13) and add_months(to_date('"+tgl2+"','dd/MM/yyyy'),-13)";
		    				q3 = "between add_months(to_date('"+tgl1+"','dd/MM/yyyy'),-4) and add_months(to_date('"+tgl2+"','dd/MM/yyyy'),-4)";
		    				q4 = "between add_months(to_date('"+tgl1+"','dd/MM/yyyy'),-2) and add_months(to_date('"+tgl2+"','dd/MM/yyyy'),-2)";
		    			}else if(tipe.equals("3")){
		    				reports = new Report("Report Follow Up Billing",props.getProperty("report.uw.report_followup_billing_hasil"),Report.XLS, null);
		    				tipe_q = "(b.MSBI_BEG_DATE BETWEEN TO_DATE('"+tgl1+"','DD/MM/YYYY') AND TO_DATE('"+tgl2+"','DD/MM/YYYY'))";
		    				
		    				if(status.equals("1")){status_q = "1=1";}
		    				else if(status.equals("2")){status_q = "C.LSSP_ID=1";}
		    				else if(status.equals("3")){status_q = "C.LSSP_ID=14";}
		    				
		    				if(prod.equals("1")){
		    					prod_q = "((c.LCA_ID IN (37) AND c.LWK_ID IN('M1') AND c.LSRG_ID IN('00','01','02','03','04')) "+
		    		                    	"or (c.LCA_ID IN (66) AND c.LWK_ID IN('00') AND c.LSRG_ID IN('B2')))";
		    				}else if(prod.equals("2")){
		    					prod_q = "not ((c.LCA_ID IN (37) AND c.LWK_ID IN('M1') AND c.LSRG_ID IN('00','01','02','03','04')) "+
		    		                    	"or (c.LCA_ID IN (66) AND c.LWK_ID IN('00') AND c.LSRG_ID IN('B2')))";
		    				}else{
		    					prod_q = "1=1";
		    				}
		    			}
		    			
		    			params.put("prod_q", prod_q);
		    			params.put("tipe_q", tipe_q);
		    			params.put("status_q", status_q);
		    			params.put("q1", q1);
		    			params.put("q2", q2);
		    			params.put("q3", q3);
		    			params.put("q4", q4);
		            	
//		    			String dest =  "C:\\EkaWeb\\";
		    			String dest =  "\\\\ebserver\\pdfind\\Report\\temp\\";
		    			
		    			String nama_file = "Report_FU_Billing_"+lusID+".xls";
		    			
		    			Connection conn = null;
		    			
		    			try{
		    				conn = this.elionsManager.getUwDao().getDataSource().getConnection();
		    				JasperUtils.exportReportToXls(reports.getReportPath()+".jasper", dest, nama_file, params, conn);
		    			}finally {
							this.elionsManager.getUwDao().closeConn(conn);
						}
		    				
		    			File file = new File(dest+"\\"+nama_file);
		    			
		    			if (file.exists()){
		    				
//		    				com.ekalife.utils.FileUtils.downloadFile("inline", dest, nama_file, response);
		    				
		    				FileInputStream in = null;
		    				ServletOutputStream ouputStream = null;
		    			    try{
		    			    	
		    			    	response.setContentType("application/vnd.ms-excel");
		    			    	response.setHeader("Content-Disposition","inline; filename=" + nama_file);
			    				response.setHeader("Expires", "0");
			    				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			    				response.setHeader("Pragma", "public");
		    			    	
		    			    	in = new FileInputStream(file);
			    			    ouputStream = response.getOutputStream();
		    					
			    			    IOUtils.copy(in, ouputStream);
		    				}finally {
		    		            try {
		    		                if(in != null) {
		    		                     in.close();
		    		                }
		    		                if(ouputStream != null) {
		    		                       ouputStream.flush();
		    		                       ouputStream.close();
		    		                }
		    		              }catch (Exception e) {
		    		                    logger.error("ERROR :", e);
		    		              }
		    				}
		    			    
		    			    org.apache.commons.io.FileUtils.forceDelete(file);
		    			}
		    		
		    		
		    	}else if(request.getParameter("showPDF") != null){
		    			ServletOutputStream sos = response.getOutputStream();
		    			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));
    		    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
    		    		sos.close();
    		    	}
    			}
    		}else{ //bila tidak ada data
    			ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tidak ada data');window.close();</script>");
    			sos.close();
    		}
    		return null;
		}
		
        HashMap alamat = bacManager.selectMstConfig(6, "FU Billing", "fu_billing");
        String from = props.getProperty("admin.ajsjava");
        String to = alamat.get("NAME")!=null?alamat.get("NAME").toString():null;
        String cc = alamat.get("NAME2")!=null?alamat.get("NAME2").toString():null;   
        
        m.put("tujuan", to);
        m.put("tujuan_cc", cc);
    	m.put("bdate", tgl1);
    	m.put("edate", tgl2);
    	return new ModelAndView("report/reportFollowUpBilling",m);
    }
	
    /**
     * helpdesk [133348] email validasi transaksi Direksi/ Dept Head
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView paymentintegration(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");	
		String lus_id = ServletRequestUtils.getRequiredStringParameter(request, "id");
		String login_name = ServletRequestUtils.getRequiredStringParameter(request, "name");
		String kode_batch = ServletRequestUtils.getRequiredStringParameter(request, "spaj");
		String ls_jenis = ServletRequestUtils.getRequiredStringParameter(request, "jns");
		String email = ServletRequestUtils.getStringParameter(request, "email");
//		String tkey = ServletRequestUtils.getStringParameter(request, "tkey");
		
		currentUser.setLus_id(lus_id);
		currentUser.setName(login_name);
		currentUser.setEmail(email);
		
		Integer hasil = bacManager.paymentintegration(currentUser, kode_batch.trim(), ls_jenis);
		
		Map test = new HashMap();	
		List<String> error = new ArrayList<String>();	
		
		if (hasil == 0)	{
			error.add("Cannot proceed your action! This data already on progress.");
		}else if(hasil == 1) {				
			error.add("Email has sent successfully!");
		}else if(hasil == 2) {				
			error.add("Something wrong, please check the URL/batch no!");
		}
		
		if(!error.equals("")){
			test.put("pesanError", error);
		}	
	    
		session.removeAttribute("currentUser");
		test.put("err", error);	
		return new ModelAndView("uw/aksepsp", test);
	}
    
    /**
     * helpdesk [133348] email validasi transaksi Direksi/ Dept Head
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView paymentintegrationaksep(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");		
		String lus_id = ServletRequestUtils.getRequiredStringParameter(request, "id");
		String kode_batch = ServletRequestUtils.getRequiredStringParameter(request, "batch_no");
		String email = ServletRequestUtils.getRequiredStringParameter(request, "email");
//		String lde = ServletRequestUtils.getRequiredStringParameter(request, "lde");
		String login_name = ServletRequestUtils.getRequiredStringParameter(request, "name");
		Integer status = ServletRequestUtils.getRequiredIntParameter(request, "status");
		String ls_jenis = ServletRequestUtils.getRequiredStringParameter(request, "ls_jenis");
		String idx_aksep = ServletRequestUtils.getStringParameter(request, "idx");
//		String tkey = ServletRequestUtils.getStringParameter(request, "tkey");
		
		currentUser.setLus_id(lus_id);
		currentUser.setName(login_name);
		currentUser.setEmail(email);
		
		Integer hasil = bacManager.paymentintegrationaksep(currentUser, kode_batch.trim(), status, ls_jenis, idx_aksep);
		
		Map test = new HashMap();	
		List<String> error = new ArrayList<String>();
		
		if (hasil == 0)	{
			error.add("Cannot proceed your action! Required action for this email are already executed. [code:0]");
		}else if(hasil == 1) {				
			error.add(" Batch " + kode_batch.trim() + " has been approved. [code:1]");
		}else if(hasil == 2){
			error.add(" Batch " + kode_batch.trim() + " has been rejected. [code:2]");
		}else if(hasil == 4){
			error.add(" Error when moving file! Please check if file exists! [code:4]");
		}
		//helpdesk []
		else if(hasil == 5){
			error.add(" Get approver data error! [code:5]");
		}else if(hasil == 6){
			error.add(" update eka.mst_pembayaran_api on 2 approval approved failed! [code:6]");
		}else if(hasil == 7){
			error.add(" update eka.mst_pembayaran_api on 1 approval decline failed! [code:7]");
		}else if(hasil == 16){
			error.add(" update eka.mst_pembayaran_api on 2 approval approved is already updated! [code:16]");
		}else if(hasil == 17){
			error.add(" update eka.mst_pembayaran_api on 1 approval decline is already updated! [code:17]");
		}
		
		if(!error.equals("")){
			test.put("pesanError", error);
		}	

		session.removeAttribute("currentUser");
		test.put("err", error);	
		return new ModelAndView("uw/aksepsp", test);
	}
    
    /**
     * helpdesk [133348] email validasi transaksi Direksi/ Dept Head
     * untuk pindahin file manual
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView movefilepaymentintegrationmanual(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession();
    	
		String file_name = ServletRequestUtils.getRequiredStringParameter(request, "filename");
		
		Boolean result = bacManager.movefilepaymentintegration(file_name.trim());
		
		Map test = new HashMap();	
		List<String> error = new ArrayList<String>();
		
		if(result)
			error.add(" File successfully moved!");
		else 
			error.add(" Error when moving file! Please check if file exists!");
		
		if(!error.equals("")){
			test.put("pesanError", error);
		}	

		session.removeAttribute("currentUser");
		test.put("err", error);			
    	return new ModelAndView("uw/aksepsp", test);
    }
}
