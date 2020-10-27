package com.ekalife.elions.web.uw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.jasperreports.JasperReportsUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.Benefeciary;
import com.ekalife.elions.model.Datarider;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilInvestasi;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.MstQuestionAnswer;
import com.ekalife.elions.model.PembayarPremi;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.PesertaPlus;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;
import com.ekalife.elions.web.uw.support.WordingPdfViewer;
import com.ekalife.utils.Common;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.MergePDF;
import com.ekalife.utils.PdfUtils;
import com.ekalife.utils.Print;
import com.ekalife.utils.PrintPolisPerjanjianAgent;
import com.ekalife.utils.Products;
import com.ekalife.utils.QrUtils;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentJasperReportingController;
import com.ekalife.utils.view.PDFViewer;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
/**
 * @author Yusuf
 * Semua report yang di-develop menggunakan jasper reports, diakses disini
 * Untuk mempermudah, fold aja dulu semua code-nya, untuk melihat struktur class ini
 */
@SuppressWarnings("unchecked")
public class PrintPolisPrintingController extends ParentJasperReportingController{
	protected final Log logger = LogFactory.getLog( getClass() );
	Errors errors;
	DateFormat df2 = new SimpleDateFormat("yyyyMMdd");

	/** PRINT POLIS **/
		private Map printPolis(String spaj, HttpServletRequest request, int singleDuplexQuadruplex) {
			Map params = new HashMap();
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			params.put("spaj", spaj);
			params.put("props", props);
			String validasiMeterai = uwManager.validasiBeaMeterai(currentUser.getJn_bank());
			if(validasiMeterai != null){
				params.put("meterai", null);
				params.put("izin", "");
			}else{
				params.put("meterai", "Rp. 6.000,-");
				params.put("izin", elionsManager.selectIzinMeteraiTerakhir());
			}
			
			//jenis print ulang
			int jpu = ServletRequestUtils.getIntParameter(request, "jpu", -1);
			
			if(jpu == 1) { //PRINT ULANG POLIS
				params.put("tipePolis", "O R I G I N A L");
			}else if(jpu == 2) { //PRINT DUPLIKAT POLIS
				String seq = ServletRequestUtils.getStringParameter(request, "seq", "");
				params.put("tipePolis", "D U P L I C A T E " + "("+seq+")");
			}else if(jpu == 3) { //PRINT ULANG POLIS
				params.put("tipePolis", "O R I G I N A L");
			}
			params.put("ingrid", props.get("images.ttd.direksi")); //ttd dr. ingrid (Yusuf - 04/05/2006)
			
			String kategori = products.kategoriPrintPolis(spaj);
			String policyNumber = "09208201800001";
			 	   policyNumber = this.uwManager.getUwDao().selectNoPolisFromSpaj(spaj);
			String logoQr = getQrBasedOnPolicy(this.getServletContext(),props,policyNumber, spaj, bacManager);
			params.put("Print", "cetak");
			if( logoQr.trim().contains("09208201800001") ){
				params.remove("Print");
			}
			params.put("logoQr", logoQr);	
			
			String context = this.getServletContext().getRealPath("/");
			params.put("context", context);
			
			//String kategori = "guthrie";
			logger.info("JENIS POLIS : " + kategori);	
			if(PrintPolisMultiController.POLIS == singleDuplexQuadruplex) { //single
				params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis."+kategori));
			}else if(PrintPolisMultiController.POLIS_DUPLEX == singleDuplexQuadruplex) { //duplex(cetak BOLAK-BALIK)
				params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
				params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.duplex"));
			}else if(PrintPolisMultiController.POLIS_QUADRUPLEX == singleDuplexQuadruplex) { //quadruplex
				params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
				params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex"));
			}else if(PrintPolisMultiController.POLIS_QUADRUPLEX_PLUS_HADIAH == singleDuplexQuadruplex) { //quadruplex
				params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
				params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex_plus_hadiah"));
			}else if(PrintPolisMultiController.POLIS_QUADRUPLEX_MEDICAL_PLUS== singleDuplexQuadruplex) { //quadruplex				
				params.put("pathPolis", props.getProperty("report.polis."+kategori+".medplus")+".jasper");
				params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex_medical_plus"));
			}
			return params;
		}
	
		public static String getQrBasedOnPolicy(ServletContext context,Properties props,String policy, String spaj, BacManager bacManager){
			String def =   "09208201800001.png";
		
			String path = props.get("qr.internal.path").toString();
			Integer mode =  Integer.parseInt(props.get("qr.mode").toString());
			
		
			QrUtils qrUtils = new QrUtils(props, policy, context);
			//(props,policy,context, spaj)
			try{
				def = qrUtils.resolveQr();
			}catch(Exception e){
				path = context.getRealPath("/")+"images";
				System.out.println("File not found and replace with default"+def+"error with e="+e.getMessage());
				def = path+"/"+def;
			}
			System.out.println("File found with :"+def);
			return def;
		}
		
		public ModelAndView polis_duplex(HttpServletRequest request,
				HttpServletResponse response) throws Exception{
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			Map paramsPolis = printPolis(spaj, request, PrintPolisMultiController.POLIS_DUPLEX);
			Map paramsManfaat = elionsManager.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
			String pathManfaat = (String) paramsManfaat.get("reportPath");
			pathManfaat = pathManfaat.substring(17);
			paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
			paramsManfaat.remove("reportPath");
			
			paramsPolis.putAll(paramsManfaat);
			
			//paramsPolis.put("koneksi", getConnection());
			List temp = new ArrayList(); Map map = new HashMap();
			map.put("1", "1"); temp.add(map);
			paramsPolis.put("dsManfaat", JasperReportsUtils.convertReportData(temp));
			return generateReport(temp, request, response, paramsPolis, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty( "pdf.polis_duplex"));
		}
		
		public ModelAndView polis_quadruplex(HttpServletRequest request,
				HttpServletResponse response) throws Exception{
			
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String jpu = ServletRequestUtils.getStringParameter(request, "seq", "");
			
			//1. Polis
			Map paramsPolis = printPolis(spaj, request, PrintPolisMultiController.POLIS_QUADRUPLEX);
			String businessId = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			Integer businessNumber = this.uwManager.selectBusinessNumber(spaj);

			//helpdesk [133346] produk baru 142-13 Smart Investment Protection
			if(businessId.equalsIgnoreCase("142") && businessNumber == 13){
				paramsPolis.remove("tipePolis");
				paramsPolis.put("tipePolis", "");
			}
			//2. Manfaat
			Map paramsManfaat = elionsManager.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
			String pathManfaat = (String) paramsManfaat.get("reportPath");
			pathManfaat = pathManfaat.substring(17);
			paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
			paramsManfaat.remove("reportPath");
			paramsManfaat.put("lsdbs", businessNumber);
			paramsManfaat.put("flag_med_plus",0);
			paramsPolis.putAll(paramsManfaat);
			
			//paramsPolis.put("koneksi", getConnection());
			List temp = new ArrayList(); Map map = new HashMap();
			map.put("halaman", "1"); temp.add(map);//polis
			map.put("halaman", "2"); temp.add(map);//manfaat
			map.put("halaman", "3"); temp.add(map);//surat
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {
				map.put("halaman", "4"); temp.add(map);//kosong
				map.put("halaman", "5"); temp.add(map);//alokasi dana (bisa 1 / 2 halaman)
			}
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {//surat simas card
				map.put("halaman", "7"); temp.add(map);
			}else{
				map.put("halaman", "4"); temp.add(map);
			}
			paramsPolis.put("dsManfaat", JasperReportsUtils.convertReportData(temp));
			
			//businessId = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			
			//3. Surat Polis
//			if("001,045,053,054,130,131,132".indexOf(businessId)==-1) {

				String va = uwManager.selectVirtualAccountSpaj(spaj);
				if(cabang.equals("58")){
					if(elionsManager.selectValidasiTransferPbp(spaj)==0){//ini mste_flag_cc
						if(va==null){
							uwManager.updateVirtualAccountBySpaj(spaj);
						}
					}
				}
				
				Map data 		= uwManager.selectDataVirtualAccount(spaj);
				int lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
				String lku_id 	= (String) data.get("LKU_ID");
				
				Integer flag_cc = elionsManager.select_flag_cc(spaj);
				
//				if(va != null){
				if(lku_id.equals("01") && !products.syariah(businessId,businessNumber.toString()) && lscb_id != 0 && flag_cc ==0 && !businessId.equals("196") && !(businessId.equals("217") && businessNumber ==2)&& !(businessId.equals("190") && "5,6".indexOf(businessNumber.toString())>=0)){
//				if(flag_cc ==0){
					
					List cekSpajPromo = bacManager.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
					
					if(!cekSpajPromo.isEmpty()){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va_promo") + ".jasper");
					}else if(businessId.equals("217")&& businessNumber==2){ //untuk ERbe di set menggunakan surat_polis
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
					}
					
				//	paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
				}else if(products.syariah(businessId, businessNumber.toString())){
					if(flag_cc==0 && !businessId.equals("175")){
						if(businessId.equals("215")&& businessNumber==003){
							paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
						}else{
							paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah_va") + ".jasper");
						}
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
					}
				}else if(cabang.equals("58")){
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.mall") + ".jasper");
				}
				else{
					if(businessId.equals("196")&& businessNumber==002){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.term") + ".jasper");
					}else{
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}
				}
				
				paramsPolis.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid (Yusuf - 04/05/2006)
//			}
			
			//4. Alokasi Dana
			List viewUlink = this.elionsManager.selectViewUlink(spaj);
			if(viewUlink.size()!=0){
				Map paramsAlokasi = this.elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 1, 1,0);
				paramsAlokasi.put("elionsManager", this.elionsManager);
				
				String pathAlokasi = (String) paramsAlokasi.get("reportPath");
				pathAlokasi = pathAlokasi.substring(17);
				paramsAlokasi.put("pathAlokasiDana", pathAlokasi + ".jasper");
				paramsAlokasi.remove("reportPath");
				paramsAlokasi.put("dsAlokasiDana", JasperReportsUtils.convertReportData(viewUlink));
				
				paramsPolis.putAll(paramsAlokasi);
			}
			
//			paramsPolis.put("pathGMIT", props.getProperty("report.endorsment_gmit") + ".jasper");
			
//			5.Surat SimasCard
//			Simcard viewSimcard =this.uwManager.selectSimasCardBySpaj(spaj);
//			if(viewSimcard!=null){
//				Map paramsSuratSimas = this.uwManager.prosesCetakSuratSimasCard(spaj, currentUser.getLus_id(), request);
//				
//				String pathSuratSimas = (String) paramsSuratSimas.get("reportPath"); 
//				pathSuratSimas = pathSuratSimas.substring(17);
//				paramsSuratSimas.put("pathSuratSimas", pathSuratSimas + ".jasper");
//				paramsSuratSimas.remove("reportPath");
//				
//				paramsPolis.putAll(paramsSuratSimas);
//			}
			
			for(Iterator iter = paramsPolis.keySet().iterator(); iter.hasNext();){
				String nama = (String) iter.next();
				logger.info(nama + " = " + paramsPolis.get(nama));
			}
			if (!jpu.isEmpty()){
				return generateReport(temp, request, response, paramsPolis, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.polis_quadruplex_duplicate"));
			}else{
				return generateReport(temp, request, response, paramsPolis, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.polis_quadruplex"));
			}

		}
		
		public ModelAndView polis_quadruplex_plus_hadiah(HttpServletRequest request,
				HttpServletResponse response) throws Exception{
			
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			
			//1. Polis
			Map paramsPolis = printPolis(spaj, request, PrintPolisMultiController.POLIS_QUADRUPLEX_PLUS_HADIAH);
			//2. Manfaat
			Map paramsManfaat = elionsManager.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
			String pathManfaat = (String) paramsManfaat.get("reportPath");
			pathManfaat = pathManfaat.substring(17);
			paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
			paramsManfaat.remove("reportPath");
			paramsManfaat.put("flag_med_plus",0);
			paramsPolis.putAll(paramsManfaat);
			
			//paramsPolis.put("koneksi", getConnection());
			List temp = new ArrayList(); Map map = new HashMap();
			map.put("halaman", "1"); temp.add(map);//polis
			map.put("halaman", "2"); temp.add(map);//manfaat
			map.put("halaman", "4"); temp.add(map);//surat
			map.put("halaman", "3"); temp.add(map);//surat hadiah
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {
				map.put("halaman", "5"); temp.add(map);//kosong
				map.put("halaman", "6"); temp.add(map);//alokasi dana (bisa 1 / 2 halaman)
			}
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {//surat simas card
				map.put("halaman", "8"); temp.add(map);
			}else{
				map.put("halaman", "5"); temp.add(map);
			}
			paramsPolis.put("dsManfaat", JasperReportsUtils.convertReportData(temp));
			
			String businessId = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			Integer businessNumber = this.uwManager.selectBusinessNumber(spaj);
			//3. Surat Polis
//			if("001,045,053,054,130,131,132".indexOf(businessId)==-1) {

				String va = uwManager.selectVirtualAccountSpaj(spaj);
				if(cabang.equals("58")){
					if(elionsManager.selectValidasiTransferPbp(spaj)==0){//ini mste_flag_cc
						if(va==null){
							uwManager.updateVirtualAccountBySpaj(spaj);
						}
					}
				}
				
				Map data 		= uwManager.selectDataVirtualAccount(spaj);
				int lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
				String lku_id 	= (String) data.get("LKU_ID");
				
				Integer flag_cc = elionsManager.select_flag_cc(spaj);
				
//				if(va != null){
				if(lku_id.equals("01") && !products.syariah(businessId,businessNumber.toString()) && lscb_id != 0 && flag_cc ==0 && !businessId.equals("196")  ){
//				if(flag_cc ==0){
					
					List cekSpajPromo = bacManager.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
					
					if(!cekSpajPromo.isEmpty()){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va_promo") + ".jasper");
					}else if(businessId.equals("217")&& businessNumber==2){ //untuk ERbe di set menggunakan surat_polis
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
					}
					
				//	paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
				}else if(products.syariah(businessId, businessNumber.toString())){
					if(flag_cc==0 && !businessId.equals("175")){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah_va") + ".jasper");
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
					}
				}else if(cabang.equals("58")){
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.mall") + ".jasper");
				}
				else{
					if(businessId.equals("196") && businessNumber==02){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.term") + ".jasper");
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}
				}
				
				paramsPolis.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid (Yusuf - 04/05/2006)
//			}
			
			//4. Alokasi Dana
			List viewUlink = this.elionsManager.selectViewUlink(spaj);
			if(viewUlink.size()!=0){
				Map paramsAlokasi = this.elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 1, 1,0);
				paramsAlokasi.put("elionsManager", this.elionsManager);
				
				String pathAlokasi = (String) paramsAlokasi.get("reportPath");
				pathAlokasi = pathAlokasi.substring(17);
				paramsAlokasi.put("pathAlokasiDana", pathAlokasi + ".jasper");
				paramsAlokasi.remove("reportPath");
				paramsAlokasi.put("dsAlokasiDana", JasperReportsUtils.convertReportData(viewUlink));
				
				paramsPolis.putAll(paramsAlokasi);
			}
			
			for(Iterator iter = paramsPolis.keySet().iterator(); iter.hasNext();){
				String nama = (String) iter.next();
				logger.info(nama + " = " + paramsPolis.get(nama));
			}
			
			return generateReport(temp, request, response, paramsPolis, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.polis_quadruplex"));
	
		}
		
		public ModelAndView polis_quadruplex_medical_plus(HttpServletRequest request,
				HttpServletResponse response) throws Exception{
			
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			
			//1. Polis
			Map paramsPolis = printPolis(spaj, request, PrintPolisMultiController.POLIS_QUADRUPLEX_MEDICAL_PLUS);
			//2. Manfaat
			Map paramsManfaat = elionsManager.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
			String pathManfaat = (String) paramsManfaat.get("reportPath");
			pathManfaat = pathManfaat.substring(17);
			paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
			paramsManfaat.remove("reportPath");
			paramsManfaat.put("flag_med_plus",1);
			paramsPolis.putAll(paramsManfaat);
			
			//paramsPolis.put("koneksi", getConnection());
			List temp = new ArrayList(); Map map = new HashMap();
			map.put("halaman", "1"); temp.add(map);//polis
//			map.put("halaman", "3"); temp.add(map);//manfaat
			map.put("halaman", "4"); temp.add(map);//surat
			//map.put("halaman", "3"); temp.add(map);//surat hadiah
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {
				map.put("halaman", "5"); temp.add(map);//kosong
				map.put("halaman", "6"); temp.add(map);//alokasi dana (bisa 1 / 2 halaman)
			}
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {//surat simas card
				map.put("halaman", "8"); temp.add(map);
			}else{
				map.put("halaman", "5"); temp.add(map);
			}
			paramsPolis.put("dsManfaat", JasperReportsUtils.convertReportData(temp));
			
			String businessId = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			Integer businessNumber = this.uwManager.selectBusinessNumber(spaj);
			//3. Surat Polis
//			if("001,045,053,054,130,131,132".indexOf(businessId)==-1) {

				String va = uwManager.selectVirtualAccountSpaj(spaj);
				if(cabang.equals("58")){
					if(elionsManager.selectValidasiTransferPbp(spaj)==0){//ini mste_flag_cc
						if(va==null){
							uwManager.updateVirtualAccountBySpaj(spaj);
						}
					}
				}
				
				Map data 		= uwManager.selectDataVirtualAccount(spaj);
				int lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
				String lku_id 	= (String) data.get("LKU_ID");
				
				Integer flag_cc = elionsManager.select_flag_cc(spaj);
				
//				if(va != null){
				if(lku_id.equals("01") && !products.syariah(businessId,businessNumber.toString()) && lscb_id != 0 && flag_cc ==0 && !businessId.equals("196")  ){
//				if(flag_cc ==0){
					
					List cekSpajPromo = bacManager.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
					
					if(!cekSpajPromo.isEmpty()){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va_promo") + ".jasper");
					}else if(businessId.equals("217")&& businessNumber== 2){ //untuk ERbe di set menggunakan surat_polis
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
					}
					
					//paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
				}else if(products.syariah(businessId, businessNumber.toString())){
					if(flag_cc==0 && !businessId.equals("175")){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah_va") + ".jasper");
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
					}
				}else if(cabang.equals("58")){
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.mall") + ".jasper");
				}
				else{
					if(businessId.equals("196") && businessNumber==02){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.term") + ".jasper");
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}
				}
				
				paramsPolis.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid (Yusuf - 04/05/2006)
//			}
			
			//4. Alokasi Dana
			List viewUlink = this.elionsManager.selectViewUlink(spaj);
			if(viewUlink.size()!=0){
				Map paramsAlokasi = this.elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 1, 1,0);
				paramsAlokasi.put("elionsManager", this.elionsManager);
				
				String pathAlokasi = (String) paramsAlokasi.get("reportPath");
				pathAlokasi = pathAlokasi.substring(17);
				paramsAlokasi.put("pathAlokasiDana", pathAlokasi + ".jasper");
				paramsAlokasi.remove("reportPath");
				paramsAlokasi.put("dsAlokasiDana", JasperReportsUtils.convertReportData(viewUlink));
				
				paramsPolis.putAll(paramsAlokasi);
			}
			
			for(Iterator iter = paramsPolis.keySet().iterator(); iter.hasNext();){
				String nama = (String) iter.next();
				logger.info(nama + " = " + paramsPolis.get(nama));
			}
			
			return generateReport(temp, request, response, paramsPolis, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.polis_quadruplex"));
	
		}
		
		public ModelAndView polis(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException, IOException {
	
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			Map params = printPolis(spaj, request, PrintPolisMultiController.POLIS);
			
			return generateReport(
					this.elionsManager.getUwDao().getDataSource(), 
					request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.polis"));
		}
	
		public ModelAndView guthrie(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException, IOException {
			
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			//Polis
			Map paramsPolis = printPolis(spaj, request, PrintPolisMultiController.POLIS_QUADRUPLEX);
			//Manfaat
			Map paramsManfaat = elionsManager.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
			String pathManfaat = (String) paramsManfaat.get("reportPath");
			pathManfaat = pathManfaat.substring(17);
			paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
			paramsManfaat.remove("reportPath");
			
			paramsPolis.putAll(paramsManfaat);
			
			//paramsPolis.put("koneksi", getConnection());
			List temp = new ArrayList(); Map map = new HashMap();
			map.put("halaman", "1"); temp.add(map);//polis
			map.put("halaman", "2"); temp.add(map);//manfaat
			paramsPolis.put("dsManfaat", JasperReportsUtils.convertReportData(temp));
			
			return generateReport(temp, request, response, paramsPolis, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.polis_quadruplex"));

		}
	
		public ModelAndView manfaat(HttpServletRequest request,
				HttpServletResponse response) throws Exception{
	
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			Map params = this.elionsManager.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
			
			List temp = new ArrayList(); Map map = new HashMap();
			map.put("1", "1"); temp.add(map);
			return generateReport(temp, request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.manfaat"));				
		}
		
		public ModelAndView tandaterimapolis(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException{
			Map params = new HashMap();
			//User currentUser = (User) request.getSession().getAttribute("currentUser");
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			spaj = spaj.trim();
			
			int referal = ServletRequestUtils.getIntParameter(request, "referal", 0);
			
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs_id = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
//			String lsbs_id = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			String reportPath;
			String namaFile = props.getProperty("pdf.tanda_terima_polis");
			int isInputanBank = elionsManager.selectIsInputanBank(spaj); 
			if(((isInputanBank == 2 || isInputanBank == 3) && referal == 0 && !products.productBsmFlowStandardIndividu(Integer.parseInt(lsbs_id), Integer.parseInt(lsdbs))) || (lsbs_id.equals("175")&&lsdbs.equals("002"))) {
				reportPath = props.getProperty("report.tandaterimasertifikat");
				namaFile = props.getProperty("pdf.tanda_terima_sertifikat");
			}else if(products.unitLink(lsbs_id) && !products.stableLink(lsbs_id)) {
				if(products.syariah(lsbs_id, lsdbs)){
					reportPath = props.getProperty("report.tandaterimapolis.syariah");
				}else{
					reportPath = props.getProperty("report.tandaterimapolis.link");
				}
			}else if(lsbs_id.equals("187")){
				reportPath = props.getProperty("report.tandaterimapolis.pas");
			}else {
				reportPath = props.getProperty("report.tandaterimapolis"); //ini udah include biasa + syariah
			}

			//kalo ada file tanda terima, hapus dulu, kalo gak ada ya udah lanjut aja
			try {
				String directory = props.getProperty("pdf.dir.export")+"\\"+elionsManager.selectCabangFromSpaj(spaj)+"\\"+spaj;
				FileUtils.deleteFile(directory, "tanda_terima.pdf");
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
			} catch (IOException e) {
				logger.error("ERROR :", e);
			}
			
			params.put("referal", referal);
			params.put("reportPath", "/WEB-INF/classes/" + reportPath);
			params.put("spaj", spaj);
			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, namaFile);
		}
	
		public ModelAndView tandaterimapolisduplikat(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException{
			Map params = new HashMap();
			//User currentUser = (User) request.getSession().getAttribute("currentUser");
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			spaj = spaj.trim();
			
			int referal = ServletRequestUtils.getIntParameter(request, "referal", 0);
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs_id = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
			String seq = "";
			if((props.getProperty("product.powerSave")).indexOf(lsbs_id)>-1 || (lsbs_id.equals("164") && "001,002,008,012".indexOf(lsdbs)>-1) ||
				(lsbs_id.equals("186") && "001,003".indexOf(lsdbs)>-1)){
				seq = bacManager.selectNoDuplikatUlangan(spaj, "1");
			}else{
				seq = bacManager.selectNoDuplikatUlangan(spaj, "0");
			}
			if(seq == null) seq = "";
			if(seq.equals("")){
				seq = elionsManager.sequenceDuplikatPolis(spaj);
			}
//			String lsbs_id = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			String reportPath;
			String namaFile = props.getProperty("pdf.tanda_terima_polis_duplikat");

			reportPath = props.getProperty("report.tandaterimapolisduplikat");

			//kalo ada file tanda terima, hapus dulu, kalo gak ada ya udah lanjut aja
			try {
				String directory = props.getProperty("pdf.dir.export")+"\\"+elionsManager.selectCabangFromSpaj(spaj)+"\\"+spaj;
				FileUtils.deleteFile(directory, "tanda_terima_duplikat.pdf");
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
			} catch (IOException e) {
				logger.error("ERROR :", e);
			}
			
			params.put("referal", referal);
			params.put("tipePolis", "D U P L I C A T E " + "("+seq+")");
			params.put("reportPath", "/WEB-INF/classes/" + reportPath);
			params.put("spaj", spaj);
			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, namaFile);
		}
		
		public ModelAndView kartupremi(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException{
			Map params = new HashMap();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.kartu_premi").toString());
			params.put("spaj", spaj);
			params.put("props", props);
			
			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.kartu_premi"));
		}	
		
		public ModelAndView kwitansi(HttpServletRequest request, HttpServletResponse response) throws ParseException, MalformedURLException{
			Map params = new HashMap();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String kwitansiBefore = elionsManager.selectMstPositionSpajMspsDesc(spaj, 7);
			String seq = "";
			if(Common.isEmpty(kwitansiBefore)){
				seq = uwManager.sequenceKuitansi();
				Boolean ok = false;
				do{
					try{
						this.elionsManager.insertMstPositionSpaj( currentUser.getLus_id(), "PRINT KWITANSI("+seq+")", spaj, 0);
						ok=true;
					}catch(Exception e){};
				}while (!ok);
				
			}else{
				seq = kwitansiBefore;
			}
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.tandaterimapolismall"));
			params.put("reg_spaj", spaj);
			params.put("seq", seq);
			params.put("props", props);
			
			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.kwitansi"));
		}
	
		public ModelAndView suratpolis(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException{
	
			Map params = new HashMap();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String businessId = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			Integer businessNumber = this.uwManager.selectBusinessNumber(spaj);
//			if("001,045,053,054,130,131,132".indexOf(businessId)>-1) {
//				return null;
//			}else{
				
				List tmp = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				String lsdbs = (String) ((Map) tmp.get(0)).get("DETBISNIS");
				
				String va = uwManager.selectVirtualAccountSpaj(spaj);
				String lcaId = elionsManager.selectCabangFromSpaj(spaj);
				Integer flag_cc = elionsManager.select_flag_cc(spaj);
				
				Map data 		= uwManager.selectDataVirtualAccount(spaj);
				int lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
				String lku_id 	= (String) data.get("LKU_ID");
				
				if(lcaId.equals("58")){
					params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.surat_polis.mall"));
				}else if(products.syariah(businessId, businessNumber.toString()) && !businessId.equals("166")) { //khusus amanah, tetep pake yang link
					if(flag_cc==0 && (!businessId.equals("175"))){
						if (businessId.equals("215") && lsdbs.equals("003") ) {//Special Case Premium Link - Ryan
							params.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid
							params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_polis.syariah").toString());
						}else{
							params.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid
							params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_polis.syariah_va").toString());
						}
					}else{
						params.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid
						params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_polis.syariah").toString());
					}
					
				}else if(lsbs.equals("142") && lsdbs.equals("008")) { //khusus DM/TM, suratnya beda
					params.put("hamid", props.get("images.ttd.gideon")); //ttd pak gideon
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_polis.dmtm").toString());
					
				}else if(lku_id.equals("01") && !products.syariah(businessId,businessNumber.toString()) && lscb_id != 0 && flag_cc ==0 &&!businessId.equals("196") && !(businessId.equals("190") && "5,6".indexOf(businessNumber.toString())>=0)){
				//else if(flag_cc ==0){
					
						params.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid
						
						List cekSpajPromo = bacManager.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
						
						if(!cekSpajPromo.isEmpty()){
							params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_polis.va_promo").toString());
						}else if(lsbs.equals("217") && lsdbs.equals("002")){ //untuk ERbe di set menggunakan surat_polis
							params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_polis").toString());
						}else{
							params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_polis.va").toString());
						}					
						
						//params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_polis.va").toString());
						
				} else {
					params.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid
					if(businessId.equals("196")){
						params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_polis.term").toString());
					}else{
						params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_polis").toString());
					}
				}
//			}
				
			params.put("props", props);
			params.put("spaj", spaj);
	
			boolean isViewer = ServletRequestUtils.getBooleanParameter(request, "isViewer", false);
				
			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.surat_polis"));
			
		}
		
		public ModelAndView endors_ssu(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException{
	
			Map params = new HashMap();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String up = ServletRequestUtils.getStringParameter(request, "up", "");
			String pasal = ServletRequestUtils.getStringParameter(request, "pasal","");
			BigDecimal big=new BigDecimal(up);
			String businessId = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			Integer businessNumber = this.uwManager.selectBusinessNumber(spaj);
				
				List tmp = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				String lsdbs = (String) ((Map) tmp.get(0)).get("DETBISNIS");
				
				String va = uwManager.selectVirtualAccountSpaj(spaj);
				String lcaId = elionsManager.selectCabangFromSpaj(spaj);
				Integer flag_cc = elionsManager.select_flag_cc(spaj);
				
				Map data 		= uwManager.selectDataVirtualAccount(spaj);
				int lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
				String lku_id 	= (String) data.get("LKU_ID");
				
					params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.endorsment_ssu"));
				
			params.put("props", props);
			params.put("spaj", spaj);
			params.put("up", big);
			params.put("pasal", pasal);
	
			boolean isViewer = ServletRequestUtils.getBooleanParameter(request, "isViewer", false);
				
			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.endorsment_ssu"));
		}

		public ModelAndView endors_penyakit(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException{
	
			Map params = new HashMap();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String penyakit = ServletRequestUtils.getStringParameter(request, "penyakit", "");
			String businessId = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			Integer businessNumber = this.uwManager.selectBusinessNumber(spaj);
				
				List tmp = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				String lsdbs = (String) ((Map) tmp.get(0)).get("DETBISNIS");
				
				String va = uwManager.selectVirtualAccountSpaj(spaj);
				String lcaId = elionsManager.selectCabangFromSpaj(spaj);
				Integer flag_cc = elionsManager.select_flag_cc(spaj);
				
				Map data 		= uwManager.selectDataVirtualAccount(spaj);
				int lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
				String lku_id 	= (String) data.get("LKU_ID");
				
					params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.endorsment_penyakit"));
				
			params.put("props", props);
			params.put("spaj", spaj);
			params.put("penyakit", penyakit);
	
			boolean isViewer = ServletRequestUtils.getBooleanParameter(request, "isViewer", false);
				
			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.endorsment_penyakit"));
		}
		
		public ModelAndView endors_gmit(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException{
	
			Map params = new HashMap();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String businessId = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			Integer businessNumber = this.uwManager.selectBusinessNumber(spaj);
				
				List tmp = elionsManager.selectDetailBisnis(spaj);
				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
				String lsdbs = (String) ((Map) tmp.get(0)).get("DETBISNIS");
				
				String va = uwManager.selectVirtualAccountSpaj(spaj);
				String lcaId = elionsManager.selectCabangFromSpaj(spaj);
				Integer flag_cc = elionsManager.select_flag_cc(spaj);
				
				Map data 		= uwManager.selectDataVirtualAccount(spaj);
				int lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
				String lku_id 	= (String) data.get("LKU_ID");
				
					params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.endorsment_gmit"));
				
			params.put("props", props);
			params.put("spaj", spaj);
	
			boolean isViewer = ServletRequestUtils.getBooleanParameter(request, "isViewer", false);
				
			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.endorsment_gmit"));
		}		
		
		public ModelAndView endorsemen(HttpServletRequest request,
			HttpServletResponse response) throws ParseException, MalformedURLException{
			Map params = new HashMap();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			Map map= uwManager.selectProdInsured(spaj);
			BigDecimal lsbs = (BigDecimal) map.get("LSBS_ID");
			BigDecimal lsdbs = (BigDecimal) map.get("LSDBS_NUMBER");
			String lsbs_id = lsbs.toString();
			String lsdbs_number = lsdbs.toString();
			//ttd
			params.put("ingrid", props.get("images.ttd.direksi"));
			Integer note1 = 0;
			Integer note2 = 0;
			List<Map> endors = uwManager.selectRiderSave(spaj);
			if(endors.size()>0){
				for(Map tampung: endors){
					BigDecimal lsbs_id_rider = (BigDecimal) tampung.get("LSBS_ID");
					BigDecimal lscb_id_rider = (BigDecimal) tampung.get("LSCB_ID_RIDER");
					if((lsbs_id_rider.intValue()==813 || lsbs_id_rider.intValue()==818)&& lscb_id_rider.intValue()!=0){
						note1 = +1;
					}
					if(lsbs_id_rider.intValue()==811 || lsbs_id_rider.intValue()==819 ){
						note2 = +1;
					}
				}
			}
			//elionsManager.selectmst_peserta(simas)
			Boolean ttTambahan = false;
			List peserta = elionsManager.select_semua_mst_peserta(spaj);
			if(peserta.size()!=0){
				ttTambahan=true;
			}
			
			//if(products.powerSave(lsbs_id) || products.stableSave(Integer.parseInt(lsbs_id), Integer.parseInt(lsdbs_number))){
				if(products.DanaSejahtera(lsbs_id) || products.SuperSejahtera(lsbs_id) || Integer.parseInt(lsbs_id)==183 || Integer.parseInt(lsbs_id)==189 || Integer.parseInt(lsbs_id)==193){
					//(Deddy)report ini khusus swine flu saja rider endorsemennya 
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.uw.report_endors_new_business").toString());
				}else{
					params.put("reportPath", "/WEB-INF/classes/" + props.get("report.uw.report_endors_new_business_save").toString());
				}
				
			//}else{
			//params.put("reportPath", "/WEB-INF/classes/" + props.get("report.uw.report_endors_new_business").toString());
			//}
			
			params.put("props", props);
			params.put("spaj", spaj);
			params.put("note1", note1);
			params.put("note2", note2);
			params.put("ttTambahan", ttTambahan);
			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.endorsemen"));
		}
	
		public ModelAndView surat_breakable(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException{
	
			Map params = new HashMap();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");

			//ttd
			params.put("hamid", props.get("images.ttd.direksi")); 
			params.put("jaime", props.get("images.ttd.jaime"));
			
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_breakable").toString());
			params.put("props", props);
			params.put("spaj", spaj);

			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.surat_breakable"));
		}
	
		public ModelAndView surat_simcard(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException{
	
			Map params = new HashMap();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
				
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.surat_simcard").toString());
			params.put("props", props);
			params.put("spaj", spaj);

			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.surat_simcard"));
		}
	
		public ModelAndView sertifikat(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException, IOException {
	
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String jpu = ServletRequestUtils.getStringParameter(request, "seq", "");
			
			
			
			String lsbs_id = "";
			String lsdbs_number = "";
			List detail = elionsManager.selectDetailBisnis(spaj);
			Map det = (HashMap) detail.get(0);
			if(!detail.isEmpty()) {
				lsbs_id = (String) det.get("BISNIS");
				lsdbs_number = (String) det.get("DETBISNIS");
			}
			
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			
			int referal = ServletRequestUtils.getIntParameter(request, "referal", 0);
			
			if(products.powerSave(lsbs_id) || products.stableLink(lsbs_id)) {
				Map premiProdukUtama = this.elionsManager.selectPremiProdukUtama(spaj);
				String kurs = (String) premiProdukUtama.get("LKU_ID");
				BigDecimal premi = (BigDecimal) premiProdukUtama.get("MSPR_PREMIUM");
				String polis = uwManager.selectNoPolisFromSpaj(spaj);
				int isInputanBank = elionsManager.selectIsInputanBank(spaj);
				
				if(products.stableLink(lsbs_id)  || isInputanBank == 2 || isInputanBank == 3 || (lsbs_id.equals("175") && lsdbs_number.equals("002")) || ((kurs.equals("01") && premi.doubleValue()>=200000000) || (kurs.equals("02") && premi.doubleValue()>=20000)) ||
						(lsbs_id.equals("158") && (lsdbs_number.equals("013") || lsdbs_number.equals("015") || lsdbs_number.equals("016")))|| 
						(lsbs_id.equals("143") && (lsdbs_number.equals("004") || lsdbs_number.equals("005") || lsdbs_number.equals("006")))|| 
						(lsbs_id.equals("142") && lsdbs_number.equals("002")) || //untuk cetak produk simas prima //Chandra
						(lsbs_id.equals("177") && lsdbs_number.equals("004"))
						
						
						) { 
					Map params = new HashMap();
					params.put("spaj", spaj);
					
					// pastikan menggunakan qr product patar timotius
					if((lsbs_id.equals("142") && lsdbs_number.equals("002")) ||(lsbs_id.equals("142") && lsdbs_number.equals("009")) ||  (lsbs_id.equals("175") && lsdbs_number.equals("002"))){
						String policyNumber = "09208201800001";
						
						String noPolis = this.uwManager.getUwDao().selectNoPolisFromSpaj(spaj);
						System.out.println("No Elions Polis:"+noPolis);
						params.put("logoQr", getQrBasedOnPolicy(this.getServletContext(),props,noPolis, spaj, bacManager));
					//	params.put("logoQr",props.get("images.qr.path"));
					//	params.put("Print", new Boolean(true));	
						params.put("Print", "cetak");
					};
					
					
					//SIMAS STABIL LINK
					if((products.stableLink(lsbs_id))) {//|| (lsbs_id.equals("158") && (lsdbs_number.equals("013") || lsdbs_number.equals("015") || lsdbs_number.equals("016")))|| 
							//(lsbs_id.equals("143") && (lsdbs_number.equals("004") || lsdbs_number.equals("005") || lsdbs_number.equals("006"))))  {
						if(!jpu.equals("")) { //PRINT DUPLIKAT POLIS
							String seq = ServletRequestUtils.getStringParameter(request, "seq", "");
							params.put("tipePolis", "D U P L I C A T E " + "("+seq+")");
							Date tgl_print = uwManager.selectTanggalCetakSertifikatAwal(spaj,"PRINT SERTIFIKAT");
							params.put("info","Polis ini adalah pengganti polis No.<style isBold=\"true\" pdfFontName=\"Tahoma-Bold\" fontSize=\"11\"> " +FormatString.nomorPolis(polis)+ "</style> yang diterbitkan tanggal <style isBold=\"true\" pdfFontName=\"Tahoma-Bold\" fontSize=\"11\">" +FormatDate.toIndonesian(tgl_print)+ "</style> yang hilang. Dengan diterbitkannya polis ini maka polis yang diterbitkan tanggal <style isBold=\"true\" pdfFontName=\"Tahoma-Bold\" fontSize=\"11\">" +FormatDate.toIndonesian(tgl_print)+ "</style> dinyatakan tidak berlaku lagi.");
						}
						String validasiMeterai = uwManager.validasiBeaMeterai(currentUser.getJn_bank());
						if(validasiMeterai != null){
							params.put("meterai", null);
							params.put("izin", "");
						}else{
							params.put("meterai", "Rp. 6.000,-");
							params.put("izin", elionsManager.selectIzinMeteraiTerakhir());
						}
						params.put("seri", ServletRequestUtils.getStringParameter(request, "seri", ""));
						if(Integer.valueOf(lsdbs_number).intValue() == 1 || Integer.valueOf(lsdbs_number).intValue() == 5 ||Integer.valueOf(lsdbs_number).intValue() == 6) { //STABLE LINK
							params.put("flagMeterai", 1); //meterai di kanan atas
						}else if(Integer.valueOf(lsdbs_number).intValue() == 2 || Integer.valueOf(lsdbs_number).intValue() == 8 || Integer.valueOf(lsdbs_number).intValue() == 11)  { //SIMAS STABIL LINK
							params.put("flagMeterai", 0); //meterai di kanan bawah
						}
						Date sep10 = defaultDateFormat.parse("10/09/2009");
						Date tglNow = uwManager.selectBegDateProductInsured(spaj);
						if(products.progressiveLink(lsbs_id)){
							BigDecimal ins_period = (BigDecimal) det.get("INS_PERIOD");
							if(ins_period.toString().equals("99")){
								params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_plink_wholelife"));
							}else{
								params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_plink"));
							}
							
						}else{
							boolean adaRider = uwManager.selectAdaRider(spaj);

							if(tglNow.before(sep10)){
								params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_banksinarmas_stabil_before"));
							}else if(adaRider){ //Yusuf (23/11/2010), bila ada rider, pakai report khusus
								params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_banksinarmas_rider"));
							}else{
								params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_banksinarmas_stabil"));			
							}
						}
						
						
						
						//Yusuf (20 Ags 09) - Bila Simas Stabil Link , langsung copy PDF SSU ke foldernya
						if(lsbs_id.equals("164") && lsdbs_number.equals("002") || lsbs_id.equals("164") && lsdbs_number.equals("008")){
							File dir = new File(
									props.getProperty("pdf.dir.export") + "\\" +
									elionsManager.selectCabangFromSpaj(spaj) + "\\" +
									spaj);
							if(!dir.exists()) dir.mkdirs();
							
							Map data = uwManager.selectDataUsulan(spaj);
							Date begdate = (Date) data.get("MSTE_BEG_DATE");
							//Deddy - batas tgl berlaku diubah dari tgl 1 sept 2009 jadi 10 sept 2009
							Date sep2009 = (new SimpleDateFormat("dd/MM/yyyy")).parse("01/09/2009");
							Date okt2009 = (new SimpleDateFormat("dd/MM/yyyy")).parse("01/10/2009");
							File from = null;
							if(begdate.before(sep2009)){
								from = new File(props.getProperty("pdf.dir.syaratpolis") + "\\164-002-KHUSUS_BSM_BEFORE1SEP.pdf");
							}else if(begdate.before(okt2009)){
								from = new File(props.getProperty("pdf.dir.syaratpolis") + "\\164-002-KHUSUS_BSM_AFTER1SEP.pdf");
							}else{
								from = new File(props.getProperty("pdf.dir.syaratpolis") + "\\164-002-KHUSUS_BSM_AFTER1OCT.pdf");
							}
							if(lsbs_id.equals("164") && lsdbs_number.equals("008")){
								from = new File(props.getProperty("pdf.dir.syaratpolis") + "\\164-008.pdf");
							}
							
							File to = new File(
									props.getProperty("pdf.dir.export") + "\\" +
									elionsManager.selectCabangFromSpaj(spaj) + "\\" +
									spaj + "\\" +
									props.getProperty("pdf.ssu") + ".pdf");
							FileUtils.copy(from, to);
						}

						//Yusuf (12 Oct 09) - Bila Simas Stabil Link dan dapat swine flu, langsung copy PDF SSK ke foldernya
						if(lsbs_id.equals("164") && lsdbs_number.equals("002") && uwManager.selectDapatSwineFlu(spaj)){
							File dir = new File(
									props.getProperty("pdf.dir.export") + "\\" +
									elionsManager.selectCabangFromSpaj(spaj) + "\\" +
									spaj);
							if(!dir.exists()) dir.mkdirs();
							
							File from = new File(props.getProperty("pdf.dir.syaratpolis") + "\\RIDER\\822-001-STABLE.pdf");
							
							File to = new File(
									props.getProperty("pdf.dir.export") + "\\" +
									elionsManager.selectCabangFromSpaj(spaj) + "\\" +
									spaj + "\\" +
									props.getProperty("pdf.ssk") + ".pdf");
							FileUtils.copy(from, to);
						}
						
					//BANK SINARMAS (SIMAS PRIMA DAN SIMAS PRIMA BULANAN)
//					}else if((isInputanBank==2 || isInputanBank==3) && referal == 0) {
					}else if(( (lsbs_id.equals("142") && (lsdbs_number.equals("002") || lsdbs_number.equals("009")|| lsdbs_number.equals("011")) ) || (lsbs_id.equals("175") && (lsdbs_number.equals("002")) ) || ( lsbs_id.equals("158") && (lsdbs_number.equals("006") || lsdbs_number.equals("014")) ) || (lsbs_id.equals("188") && (lsdbs_number.equals("002") || lsdbs_number.equals("008"))) )  && referal == 0) {
						if(!jpu.equals("")) { //PRINT DUPLIKAT POLIS
							String seq = ServletRequestUtils.getStringParameter(request, "seq", "");
							params.put("tipePolis", "D U P L I C A T E " + "("+seq+")");
							Date tgl_print = uwManager.selectTanggalCetakSertifikatAwal(spaj,"PRINT SERTIFIKAT");
							params.put("info","Polis ini adalah pengganti polis No.<style isBold=\"true\" pdfFontName=\"Tahoma-Bold\" fontSize=\"11\"> " +FormatString.nomorPolis(polis)+ "</style> yang diterbitkan tanggal <style isBold=\"true\" pdfFontName=\"Tahoma-Bold\" fontSize=\"11\">" +FormatDate.toIndonesian(tgl_print)+ "</style> yang hilang. Dengan diterbitkannya polis ini maka polis yang diterbitkan tanggal <style isBold=\"true\" pdfFontName=\"Tahoma-Bold\" fontSize=\"11\">" +FormatDate.toIndonesian(tgl_print)+ "</style> dinyatakan tidak berlaku lagi.");
						}
						String validasiMeterai = uwManager.validasiBeaMeterai(currentUser.getJn_bank());
						if(validasiMeterai != null){
							params.put("meterai", null);
							params.put("izin", "");
						}else{
							params.put("meterai", "Rp. 6.000,-");
							params.put("izin", elionsManager.selectIzinMeteraiTerakhir());
						}
						params.put("seri", ServletRequestUtils.getStringParameter(request, "seri", ""));
						if(lsbs_id.equals("142") || lsbs_id.equals("158") || lsbs_id.equals("188") || lsbs_id.equals("175")) { //SIMAS PRIMA
							if(lsbs_id.equals("188")){
								params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_banksinarmas_psave"));
							}else{
								boolean adaRider = uwManager.selectAdaRider(spaj);
								if(adaRider){ //Yusuf (23/11/2010), bila ada rider, pakai report khusus
									params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_banksinarmas_rider")); // *for Rider
								}else{
									params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_banksinarmas"));
								}
								
								if(lsbs_id.equals("175") && lsdbs_number.equals("002")){ //Anta Power Save Syariah BSM
									if(adaRider){ 
										params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_banksinarmas_rider")); // *for Rider
									}else{
										params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_banksinarmas_psave_syariah"));
									}
								}
							}
							
							//Yusuf (20 Ags 09) - Bila Simas Prima (bukan yg referal lho), maka langsung copy PDF SSU ke foldernya
							if(lsbs_id.equals("142") && lsdbs_number.equals("002")){
								File dir = new File(
										props.getProperty("pdf.dir.export") + "\\" +
										elionsManager.selectCabangFromSpaj(spaj) + "\\" +
										spaj);
								if(!dir.exists()) dir.mkdirs();
								
								File from = new File(props.getProperty("pdf.dir.syaratpolis") + "\\142-002-KHUSUS_BSM.pdf");
								File to = new File(
										props.getProperty("pdf.dir.export") + "\\" +
										elionsManager.selectCabangFromSpaj(spaj) + "\\" +
										spaj + "\\" +
										props.getProperty("pdf.ssu") + ".pdf");
								FileUtils.copy(from, to);
							}
							if(lsbs_id.equals("175") && lsdbs_number.equals("002")){
								File dir = new File(
										props.getProperty("pdf.dir.export") + "\\" +
										elionsManager.selectCabangFromSpaj(spaj) + "\\" +
										spaj);
								if(!dir.exists()) dir.mkdirs();
							}
						}
					//POWERSAVE BULANAN
					}else if(lsbs_id.equals("158")) {
						if((lsdbs_number.equals("013") || lsdbs_number.equals("015") || lsdbs_number.equals("016"))){
							params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_stablesave_bulanan"));
						}else{
							params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_powersave_bulanan"));
						}
					//POWERSAVE BULANAN SYARIAH
					}else if(lsbs_id.equals("176")) {
						params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_powersave_bulanan_syariah"));
					//POWERSAVE SYARIAH
					}else if(lsbs_id.equals("175")) {
						params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_powersave_syariah"));
					//POWERSAVE
					}else if(lsbs_id.equals("177")){
						if((lsdbs_number.equals("004"))){
							params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_special_case"));
						}
					}else {
						if((lsbs_id.equals("143") && (lsdbs_number.equals("004") || lsdbs_number.equals("005") || lsdbs_number.equals("006")))){
							params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_stablesave"));
						}else{
							params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.sertifikat_powersave"));
						}
					}
					params.put("props", props);
					params.put("ingrid", props.get("images.ttd.direksi")); //ttd dr. ingrid (Yusuf - 04/05/2006)
					if(!jpu.equals("")) {
					return generateReport(
							this.elionsManager.getUwDao().getDataSource(), 
							request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.sertifikat_powersave_duplicate"));
				}else{
					return generateReport(
							this.elionsManager.getUwDao().getDataSource(), 
							request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.sertifikat_powersave"));
				}
				}}
	
			return noReportData(response, null);
		}
	
		public ModelAndView alokasidana(HttpServletRequest request,
				HttpServletResponse response) throws Exception{
	
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			List viewUlink = this.elionsManager.selectViewUlink(spaj);
			if(viewUlink.size()==0) return noReportData(response, null);
			Map params = this.elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 1, 1,0);
			params.put("elionsManager", this.elionsManager);
			
			return generateReport(viewUlink, request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.alokasi_dana"));
			//return generateReport(this.elionsManager.getUwDao().getDataSource(), request, params, "0", spaj, "polis_surat_ulink");
		} 
		
		public ModelAndView espaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String reg_spaj = request.getParameter("spaj");
			Date sysdate = elionsManager.selectSysdate();
			
			String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
			
			String dir = props.getProperty("pdf.template.espaj");
			
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
	        String espajFile = dir + "\\espaj_"+reg_spaj+".pdf";
	        
	        HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "Admedika");
			moreInfo.put("Subject", "EKA SEHAT Admedika");
			
			PdfContentByte over;
			BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
	        PdfReader reader = new PdfReader(props.getProperty("pdf.template.espaj")+"\\espajnew.pdf");
	        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espaj_"+reg_spaj+".pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
	        
	        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
	        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
	        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
	        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
	        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
	        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
	        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);
	        List detInv = this.uwManager.selectdetilinvestasimallspaj(reg_spaj);
	        List benef = this.elionsManager.select_benef(reg_spaj);
	        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
	        List<MedQuest> mq = uwManager.selectMedQuest(reg_spaj,null);
	        Agen agen =this.elionsManager.select_detilagen(reg_spaj);
	        dataUsulan.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
	        
	        stamp.setMoreInfo(moreInfo);
			
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman,9);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 104, 708, 0);
			//Data PP
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 135, 670, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_mother().toUpperCase(), 135, 648, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLside_name(), 135, 629, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_no_identity(), 270, 629, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataPP.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataPP.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 135, 610, 0);
			if(!Common.isEmpty(dataPP.getNama_si())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getNama_si().toUpperCase(), 285, 597, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_si())?"":FormatDate.toString(dataPP.getTgllhr_si()), 487, 597, 0);
			}
			if(!Common.isEmpty(dataPP.getNama_anak1())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getNama_anak1().toUpperCase(), 285, 581, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak1())?"":FormatDate.toString(dataPP.getTgllhr_anak1()), 487, 581, 0);
			}
			if(!Common.isEmpty(dataPP.getNama_anak2())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getNama_anak2().toUpperCase(), 285, 564, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak2())?"":FormatDate.toString(dataPP.getTgllhr_anak2()), 487, 564, 0);
			}
			if(!Common.isEmpty(dataPP.getNama_anak3())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getNama_anak3().toUpperCase(), 285, 547, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak3())?"":FormatDate.toString(dataPP.getTgllhr_anak3()), 487, 547, 0);
			}
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsne_note().toUpperCase(), 135, 532, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(dataPP.getMspe_date_birth()), 135, 511, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_place_birth().toUpperCase(), 240, 512, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspo_age()+ " TAHUN", 383, 513, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sex2().toUpperCase(), 135, 491, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsag_name().toUpperCase(), 135, 469, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsed_name().toUpperCase(), 135, 449, 0);
			
			int monyong = 0;
        	String[] alamat = StringUtil.pecahParagraf(dataPP.getAlamat_rumah().toUpperCase(), 75);
        	for(int i=0; i<alamat.length; i++) {
        		monyong = 12 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							135, 428-monyong, 0);
        	}
			//over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getAlamat_rumah().toUpperCase(), 135, 426, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"RT/RW: xxxxxxxxxxxxx, Kelurahan xxxxxxxxxxxxx ", 135, 408, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getTelpon_rumah())?"":dataPP.getArea_code_rumah()+"-"+dataPP.getTelpon_rumah()), 135, 385, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getTelpon_rumah2())?"":dataPP.getArea_code_rumah2()+"-"+dataPP.getTelpon_rumah2()), 392, 387, 0);
			
			monyong = 0;
			if(!Common.isEmpty(dataPP.getAlamat_kantor())){
				String[] alamat_kantor =  StringUtil.pecahParagraf(dataPP.getAlamat_kantor().toUpperCase(), 75);
	        	for(int i=0; i<alamat_kantor.length; i++) {
	        		monyong = 12 * i;
	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							135, 363-monyong, 0);
	        	}
			}
			//over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getAlamat_kantor(), 135, 361, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"RT/RW: xxxxxxxxxxxxx, Kelurahan xxxxxxxxxxxxx ", 135, 343, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getTelpon_kantor())?"":dataPP.getArea_code_kantor()+"-"+dataPP.getTelpon_kantor()), 135, 320, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getTelpon_kantor2())?"":dataPP.getArea_code_kantor2()+"-"+dataPP.getTelpon_kantor2()), 392, 321, 0);
			
			monyong = 0;
        	String[] alamat_billing = StringUtil.pecahParagraf(addrBill.getMsap_address(), 75);
        	for(int i=0; i<alamat_billing.length; i++) {
        		monyong = 12 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							135, 297-monyong, 0);
        	}
			//over.showTextAligned(PdfContentByte.ALIGN_LEFT,addrBill.getMsap_address().toUpperCase(), 135, 296, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"RT/RW: xxxxxxxxxxxxx, Kelurahan xxxxxxxxxxxxx", 135, 278, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_phone1())?"":addrBill.getMsap_area_code1()+"-"+ addrBill.getMsap_phone1()) , 135, 255, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_phone2())?"":addrBill.getMsap_area_code2()+"-"+ addrBill.getMsap_phone2()), 392, 256, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,addrBill.getNo_hp(), 135, 230, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,addrBill.getNo_hp2(), 392, 230, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getEmail(), 135, 205, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Alamat Email 2", 392, 207, 0);
			
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_tujuan().toUpperCase(), 135, 181, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_pendanaan().toUpperCase(), 135, 156, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_kerja().toUpperCase(), 135, 131, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getKerjab().toUpperCase(), 392, 132, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getmk" Tahun", 500, 137, 0);
			
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_penghasilan().toUpperCase(), 133, 106, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_smbr_penghasilan(), 133, 79, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getLsre_relation().toUpperCase(), 133, 54, 0);
			over.endText();
			
			over = stamp.getOverContent(2);
			over.beginText();
			over.setFontAndSize(times_new_roman,9);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMcl_first().toUpperCase(), 135, 770, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMspe_mother(), 135, 748, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getLside_name(), 135, 730, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMspe_no_identity(), 269, 731, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataTT.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataTT.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 135, 711, 0);
			if(lsre_id==1){
				if(!Common.isEmpty(dataPP.getNama_si())){
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getNama_si().toUpperCase(), 285, 699, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_si())?"":FormatDate.toString(dataPP.getTgllhr_si()), 487, 699, 0);
				}
				if(!Common.isEmpty(dataPP.getNama_anak1())){
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getNama_anak1().toUpperCase(), 285, 683, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak1())?"":FormatDate.toString(dataPP.getTgllhr_anak1()), 487, 683, 0);
				}
				if(!Common.isEmpty(dataPP.getNama_anak2())){
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getNama_anak2().toUpperCase(), 285, 665, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak2())?"":FormatDate.toString(dataPP.getTgllhr_anak2()), 487, 665, 0);
				}
				if(!Common.isEmpty(dataPP.getNama_anak3())){
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getNama_anak3().toUpperCase(), 285, 648, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak3())?"":FormatDate.toString(dataPP.getTgllhr_anak3()), 487, 648, 0);
				}
			}else{
				if(!Common.isEmpty(dataTT.getNama_si())){
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getNama_si().toUpperCase(), 285, 699, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_si())?"":FormatDate.toString(dataTT.getTgllhr_si()), 458, 699, 0);
				}
				if(!Common.isEmpty(dataTT.getNama_anak1())){
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getNama_anak1().toUpperCase(), 285, 683, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak1())?"":FormatDate.toString(dataTT.getTgllhr_anak1()), 458, 683, 0);
				}
				if(!Common.isEmpty(dataTT.getNama_anak2())){
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getNama_anak2().toUpperCase(), 285, 665, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak2())?"":FormatDate.toString(dataTT.getTgllhr_anak2()), 458, 665, 0);
				}
				if(!Common.isEmpty(dataTT.getNama_anak3())){
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getNama_anak3().toUpperCase(), 285, 648, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak3())?"":FormatDate.toString(dataTT.getTgllhr_anak3()), 458, 648, 0);
				}
			}
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getLsne_note().toUpperCase(), 135, 633, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatDate.toString(dataTT.getMspe_date_birth()), 135, 613, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMspe_place_birth().toUpperCase(), 237, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMste_age()+ " TAHUN", 380, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMspe_sex2().toUpperCase(), 135, 593, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getLsag_name().toUpperCase(), 135, 572, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getLsed_name().toUpperCase(), 135, 550, 0);
			
			monyong = 0;
        	String[] alamat2 = StringUtil.pecahParagraf(dataTT.getAlamat_rumah().toUpperCase(), 75);
        	for(int i=0; i<alamat2.length; i++) {
        		monyong = 12 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat2[i],							135, 530-monyong, 0);
        	}
			//over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getAlamat_rumah().toUpperCase(), 135, 530, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"RT/RW: xxxxxxxxxxxxx, Kelurahan xxxxxxxxxxxxx, xxxxx, Jakarta xxxx", 135, 512, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getTelpon_rumah())?"":dataTT.getArea_code_rumah()+"-"+dataTT.getTelpon_rumah()), 135, 486, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getTelpon_rumah2())?"":dataTT.getArea_code_rumah2()+"-"+dataTT.getTelpon_rumah2()), 405, 488, 0);
			
			monyong = 0;
			if(!Common.isEmpty(dataTT.getAlamat_kantor())){
				String[] alamat_kantor2 = StringUtil.pecahParagraf(dataTT.getAlamat_kantor().toUpperCase(), 75);
	        	if(!Common.isEmpty(alamat_kantor2)){
		        	for(int i=0; i<alamat_kantor2.length; i++) {
		        		monyong = 12 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor2[i],							135, 465-monyong, 0);
		        	}
	        	}
			}
        	
			//over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getAlamat_kantor(), 133, 465, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"RT/RW: xxxxxxxxxxxxx, Kelurahan xxxxxxxxxxxxx, xxxxx, Jakarta xxxx", 135, 446, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getTelpon_kantor())?"":dataTT.getArea_code_kantor()+"-"+dataTT.getTelpon_kantor()), 135, 422, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getTelpon_kantor2())?"":dataTT.getArea_code_kantor2()+"-"+dataTT.getTelpon_kantor2()), 405, 423, 0);
			
			monyong = 0;
        	String[] alamat_billing2 = StringUtil.pecahParagraf(addrBill.getMsap_address().toUpperCase(), 75);
        	for(int i=0; i<alamat_billing2.length; i++) {
        		monyong = 12 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing2[i],							135, 398-monyong, 0);
        	}
			//over.showTextAligned(PdfContentByte.ALIGN_LEFT, addrBill.getMsap_address().toUpperCase(), 135, 398, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"RT/RW: xxxxxxxxxxxxx, Kelurahan xxxxxxxxxxxxx, xxxxx, Jakarta xxxx", 135, 380, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_phone1())?"":addrBill.getMsap_area_code1()+"-"+ addrBill.getMsap_phone1()), 135, 356, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_phone2())?"":addrBill.getMsap_area_code2()+"-"+ addrBill.getMsap_phone2()), 405, 357, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,addrBill.getNo_hp(), 135, 332, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,addrBill.getNo_hp2(), 405, 332, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getEmail(), 135, 307, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Alamat Email 2", 393, 310, 0);
			
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_tujuan().toUpperCase(), 135, 283, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_pendanaan().toUpperCase(),135, 260, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_kerja().toUpperCase(),135, 234, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getKerjab().toUpperCase(),405, 234, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Masa Kerja",502, 240, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_penghasilan().toUpperCase(),135, 209, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_smbr_penghasilan(),135, 182, 0);
			
			//data usulan
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLsdbs_name(),135, 115, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMspo_pay_period().toString(),295, 115, 0);
			//over.setFontAndSize(times_new_roman,9);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),327, 115, 0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())),410, 115, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataUsulan.getMspo_installment())?"":dataUsulan.getMspo_installment().toString()),433, 115, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),467, 115, 0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT, FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())),540, 115, 0);
			//asuransi tambahan
			if(dataUsulan.getDaftaRider().size()>0){
				int vertikal = 113;
				for(int z=0;z<dataUsulan.getDaftaRider().size();z++){
					vertikal = vertikal - 12;
					Datarider rider = (Datarider) dataUsulan.getDaftaRider().get(z);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,rider.getLsdbs_name(),135, vertikal, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,"",295, vertikal, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),327, vertikal, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(rider.getMspr_tsi())),410, vertikal, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,"",433, vertikal, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),467, vertikal, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(rider.getMspr_premium())),540, vertikal, 0);
				}
				
			}
			
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),135, 58, 0);
			
			over.endText();
			
			over = stamp.getOverContent(3);
			over.beginText();
			over.setFontAndSize(times_new_roman,9);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLscb_pay_mode(),135, 780, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),135, 760, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),135, 740, 0);
			
			Double premiTambahan = 0.;
			Double premiExtra = (Common.isEmpty(uwManager.selectSumPremiExtra(reg_spaj))? 0.:uwManager.selectSumPremiExtra(reg_spaj));
			Double premiRider = (Common.isEmpty(uwManager.selectSumPremiRider(reg_spaj))? 0.:uwManager.selectSumPremiRider(reg_spaj));
			premiTambahan = premiTambahan + premiExtra + premiRider;
			
			if(inv!=null){
				DetilTopUp daftarTopup = inv.getDaftartopup();
				Double topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala())?new Double(0):daftarTopup.getPremi_berkala();
				Double topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal())?new Double(0):daftarTopup.getPremi_tunggal();
				Double totalTopup = topUpBerkala + topUpTunggal;
				BigDecimal totalPremiPokok = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup));
				BigDecimal totalPremi = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup) + premiTambahan);
				
				if(inv!=null){
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),297, 721, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(inv.getMu_jlh_premi())),370, 721, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_tunggal())?"":(daftarTopup.getId_tunggal()==2?"X":""),183, 701, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_berkala())?"":(daftarTopup.getId_berkala()==5?"X":""),222, 701, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(totalTopup)?"":dataUsulan.getLku_symbol()),297, 705, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,(Common.isEmpty(totalTopup)?"-": FormatNumber.convertToTwoDigit(new BigDecimal(totalTopup))),370, 705, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),297, 672, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(totalPremi  ) ,370, 672, 0);
				}else{
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),297, 721, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())),370, 721, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),297, 672, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(totalPremi  ) ,370, 672, 0);
				}
				
				Double ratePremiPokok = (inv.getMu_jlh_premi()/ totalPremiPokok.doubleValue()) * 100;
				Double ratePremiTopup = ((Common.isEmpty(inv.getMu_jlh_tu())?0.:inv.getMu_jlh_tu()) / totalPremi.doubleValue()) * 100;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,ratePremiPokok.intValue() + "% : " + ratePremiTopup.intValue() + "%",300, 659, 0);
			}else{
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,("-"),370, 705, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),297, 721, 0);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())),370, 721, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),297, 672, 0);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium()+premiTambahan )  ) ,370, 672, 0);
			}
			
			
			
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(inv.getJml_premium())?dataUsulan.getLku_symbol()+ FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())):dataUsulan.getLku_symbol()+ FormatNumber.convertToTwoDigit(new BigDecimal(inv.getJml_premium()))),285, 721, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(inv.getJml_premium())?"":dataUsulan.getLku_symbol()+ FormatNumber.convertToTwoDigit(new BigDecimal(inv.getMu_jlh_tu()))),285, 705, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(premiTambahan==0?"":dataUsulan.getLku_symbol()),297, 688, 0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,(premiTambahan==0?"-":FormatNumber.convertToTwoDigit(new BigDecimal(premiTambahan))),370, 688, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol() + FormatNumber.convertToTwoDigit(new BigDecimal(inv.getJml_premium() + (Common.isEmpty(inv.getMu_jlh_tu())?0:inv.getMu_jlh_tu()))  ) ,285, 672, 0);
			
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_no_ac(),135, 622, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getLsbp_nama(),382, 622, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_nama(),135, 601, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_cabang(),382, 601, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Jenis Investasi",324, 582, 0);
			DetilInvestasi detinv0 = (DetilInvestasi) detInv.get(0);
			DetilInvestasi detinv1 = (DetilInvestasi) detInv.get(1);
			DetilInvestasi detinv2 = (DetilInvestasi) detInv.get(2);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Fix Income :"+ (detinv0.getMdu_persen1()==null?Integer.toString(0):detinv0.getMdu_persen1()) + " %",50, 562, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Dynamic :"+ (detinv1.getMdu_persen1()==null?Integer.toString(0):detinv1.getMdu_persen1()) + " %",150, 562, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Aggressive :"+ (detinv2.getMdu_persen1()==null?Integer.toString(0):detinv2.getMdu_persen1()) + " %",250, 562, 0);
			int j=0;
			// D . data yang ditunjuk menerima manfaat asuransi
			for(int i=0;i<benef.size();i++){
				Benefeciary benefi = (Benefeciary) benef.get(i);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_first(),44, 487-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(benefi.getMsaw_sex()==1?"LAKI_LAKI":"PEREMPUAN"),282, 487-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getSmsaw_birth(),385, 487-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getLsre_relation(),470, 487-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_persen().toString(),530, 487-j, 0);
				j+=23;
			}
			int k = 0;
			if(lsre_id!=1){
				k=1;
			}
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,mq.get(k).getMsadm_berat().toString() + " Kg",169, 330, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,mq.get(k).getMsadm_tinggi().toString() + " Cm",357, 330, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,mq.get(k).getMsadm_bmi().toString(),370, 323, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(k).getMsadm_sehat()==1?"YA" : "TIDAK"),465, 351, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(k).getMsadm_berat_berubah()==1?"YA" : "TIDAK"),465, 319, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(k).getMsadm_penyakit()==1?"YA" : "TIDAK"),465, 281, 0);	
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(k).getMsadm_medis()==1?"YA" : "TIDAK"),465, 250, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(k).getMsadm_medis_alt()==1?"YA" : "TIDAK"),465, 210, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(k).getMsadm_family_sick()==1?"YA" : "TIDAK"),465, 160, 0);
			if(dataTT.getMspe_sex()==0){
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(k).getMsadm_pregnant()==1?"YA" : "TIDAK"),528, 115, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(k).getMsadm_abortion()==1?"YA" : "TIDAK"),528, 96, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(k).getMsadm_usg()==1?"YA" : "TIDAK"),528, 65, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(k).getMsadm_pregnant_time())?"":mq.get(k).getMsadm_pregnant_time().toString(),330, 112, 0);
			}
			
			// E. DATA PERTANYAAN MENGENAI KESEHATAN
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(0).getMsadm_sehat_desc())?"":mq.get(0).getMsadm_sehat_desc().toUpperCase()),44, 351, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,mq.get(0).getMsadm_berat().toString() + " Kg",169, 318, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,mq.get(0).getMsadm_tinggi().toString() + " Cm",357, 318, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,mq.get(0).getMsadm_bmi().toString(),370, 311, 0);
			
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(0).getMsadm_sehat()==1?"YA" : "TIDAK"),528, 351, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(0).getMsadm_berat_berubah()==1?"YA" : "TIDAK"),528, 319, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(0).getMsadm_penyakit()==1?"YA" : "TIDAK"),528, 281, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(0).getMsadm_medis()==1?"YA" : "TIDAK"),528, 250, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(0).getMsadm_medis_alt()==1?"YA" : "TIDAK"),528, 210, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(0).getMsadm_family_sick()==1?"YA" : "TIDAK"),528, 160, 0);
			if(dataPP.getMspe_sex()==0){
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(0).getMsadm_pregnant()==1?"YA" : "TIDAK"),465, 115, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(0).getMsadm_abortion()==1?"YA" : "TIDAK"),465, 96, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(0).getMsadm_usg()==1?"YA" : "TIDAK"),465, 65, 0);
			}
			
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(k).getMsadm_penyakit_desc())?"":mq.get(k).getMsadm_penyakit_desc().toUpperCase()),44, 270, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(k).getMsadm_medis_desc())?"":mq.get(k).getMsadm_medis_desc().toUpperCase()),54, 222, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(k).getMsadm_medis_alt_desc())?"":mq.get(k).getMsadm_medis_alt_desc().toUpperCase()),54, 184, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(k).getMsadm_family_sick_desc())?"":mq.get(k).getMsadm_family_sick_desc().toUpperCase()),44, 132, 0);
			if(dataTT.getMspe_sex()==0){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(k).getMsadm_pregnant_desc())?"":mq.get(k).getMsadm_pregnant_desc().toUpperCase()),295, 103, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(k).getMsadm_abortion_desc())?"":mq.get(k).getMsadm_abortion_desc().toUpperCase()),190, 75, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(k).getMsadm_usg_desc())?"":mq.get(k).getMsadm_usg_desc().toUpperCase()),54, 48, 0);
			}
			
			over.endText();
			over = stamp.getOverContent(4);
			over.beginText();
			over.setFontAndSize(times_new_roman,9);
			//F. PERTANYAAN UMUM, KEGIATAN DAN KEBIASAAN PRIBADI
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(k).getMsadd_hobby())?"":(Integer.parseInt(mq.get(k).getMsadd_hobby())==1?"YA" : "TIDAK"),465, 740, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(k).getMsadd_flight())?"":(mq.get(k).getMsadd_flight()==1?"YA" : "TIDAK"),465, 699, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(k).getMsadd_smoke())?"":(mq.get(k).getMsadd_smoke()==1?"YA" : "TIDAK"),465, 669, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(k).getMsadd_drink_beer())?"":(mq.get(k).getMsadd_drink_beer()==1?"YA" : "TIDAK"),465, 641, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(k).getMsadd_drugs())?"":(mq.get(k).getMsadd_drugs()==1?"YA" : "TIDAK"),465, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(k).getMsadd_life_ins())?"":(mq.get(k).getMsadd_life_ins()==1?"YA" : "TIDAK"),465, 570, 0);
			
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(0).getMsadd_hobby())?"":(Integer.parseInt(mq.get(0).getMsadd_hobby())==1?"YA" : "TIDAK"),528, 740, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(0).getMsadd_flight())?"":(mq.get(0).getMsadd_flight()==1?"YA" : "TIDAK"),528, 699, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(0).getMsadd_smoke())?"":(mq.get(0).getMsadd_smoke()==1?"YA" : "TIDAK"),528, 669, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(0).getMsadd_drink_beer())?"":(mq.get(0).getMsadd_drink_beer()==1?"YA" : "TIDAK"),528, 641, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(0).getMsadd_drugs())?"":(mq.get(0).getMsadd_drugs()==1?"YA" : "TIDAK"),528, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(0).getMsadd_life_ins())?"":(mq.get(0).getMsadd_life_ins()==1?"YA" : "TIDAK"),528, 570, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Jawab f1",44, 732, 0);
//			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Jawab f2",44, 695, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(k).getNsadd_many_cig())?"":mq.get(k).getNsadd_many_cig().toString()),100, 664, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(k).getMsadd_drink_beer_desc())?"":mq.get(k).getMsadd_drink_beer_desc().toUpperCase()),125, 638, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(k).getMsadd_reason_drugs())?"":mq.get(k).getMsadd_reason_drugs().toUpperCase()),219, 613, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(k).getMsadd_life_ins_desc())?"":mq.get(k).getMsadd_life_ins_desc().toUpperCase()),119, 563, 0);
			//G. FORMULIR KONFIRMASI SURAT PERMINTAAN ASURANSI JIWA ELEKTRONIK
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMcl_first().toUpperCase(),130, 519, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMcl_first().toUpperCase(),130, 501, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatString.nomorSPAJ(reg_spaj),280, 450, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT," Jakarta",115, 190, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatDate.toIndonesian( elionsManager.selectSysdate() ),300, 190, 0);
			
			
			over.setFontAndSize(times_new_roman, 7);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,dataPP.getMcl_first().toUpperCase(),120, 65, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,dataTT.getMcl_first().toUpperCase(),285, 65, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMsag_id(),459, 74, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMcl_first(),459, 62, 0);
			over.endText();
			
			
			stamp.close();	
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
				logger.error("ERROR :", e);
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
	        
			return null;
		}
		
		public ModelAndView espajdmtm(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String reg_spaj = request.getParameter("spaj");
			Date sysdate = elionsManager.selectSysdate();
			
			String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
			Integer data2 = uwManager.selectFlagQuestionare(reg_spaj);
			String dir = props.getProperty("pdf.template.espajdmtm");
			if(data2==0){
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Silakan Mengisi Questionare Terlebih Dahulu');window.close();</script>");
    			sos.close();
			}
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
	        String espajFile = dir + "\\espaj_"+reg_spaj+".pdf";
	        
	        HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "DMTM");
			moreInfo.put("Subject", "E-SPAJ DMTM");
			
			PdfContentByte over;
			BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
	        PdfReader reader = new PdfReader(props.getProperty("pdf.template.espajdmtm")+"\\espajnewTT.pdf");
	        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espajDMTM_"+reg_spaj+".pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
	        
	        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
	        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
	        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
	        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
	        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
	        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
	        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);
	        List detInv = this.uwManager.selectdetilinvestasimallspaj(reg_spaj);
	        List benef = this.elionsManager.select_benef(reg_spaj);
	        List medQuest=this.uwManager.selectquestionareDMTM(reg_spaj);
	        List peserta=this.uwManager.select_all_mst_peserta(reg_spaj);
	        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
	        List<MedQuest> mq = uwManager.selectMedQuest(reg_spaj,null);
	        Agen agen =this.elionsManager.select_detilagen(reg_spaj);
	        dataUsulan.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
	        
	        stamp.setMoreInfo(moreInfo);
			
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 415, 663, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspo_no_blanko().toUpperCase(), 94, 662, 0);
		    
		    over.setFontAndSize(times_new_roman,6);
		  //Data PP
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 127, 621, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_mother().toUpperCase(), 127, 602, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLside_name(), 127, 580, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_no_identity(), 127, 566, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsne_note().toUpperCase(), 127, 550, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_place_birth().toUpperCase(), 127, 532, 0);
		    //over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(dataPP.getMspe_date_birth()), 165, 531, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getDay2(dataPP.getMspe_date_birth()), 235, 533, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getMonth(dataPP.getMspe_date_birth()), 272, 533, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getYearFourDigit(dataPP.getMspe_date_birth()), 308, 533, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sex2().toUpperCase(), 127, 518, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataPP.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataPP.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 127, 504, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsag_name().toUpperCase(), 127, 415, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsed_name().toUpperCase(), 127, 401, 0);
		    
		    //data untuk suami/istri &anak2
		    if(!Common.isEmpty(dataPP.getNama_si())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_si())?"":dataPP.getNama_si().toUpperCase(), 185, 475, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_si())?"":FormatDate.toString(dataPP.getTgllhr_si()), 287, 475, 0);
			}
		    if(!Common.isEmpty(dataPP.getNama_anak1())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_anak1())?"":dataPP.getNama_anak1().toUpperCase(), 185, 460, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak1())?"":FormatDate.toString(dataPP.getTgllhr_anak1()), 287, 460, 0);
			}
			if(!Common.isEmpty(dataPP.getNama_anak2())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_anak2())?"":dataPP.getNama_anak2().toUpperCase(), 185, 445, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak2())?"":FormatDate.toString(dataPP.getTgllhr_anak2()), 287, 445, 0);
			}
			if(!Common.isEmpty(dataPP.getNama_anak3())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_anak3())?"":dataPP.getNama_anak3().toUpperCase(), 185, 430, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak3())?"":FormatDate.toString(dataPP.getTgllhr_anak3()), 287, 430, 0);
			}
		    
		    int monyong = 0;
        	String[] alamat = StringUtil.pecahParagraf(dataPP.getAlamat_rumah().toUpperCase(), 47);
        	for(int i=0; i<alamat.length; i++) {
        		monyong = 12 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							127, 385-monyong, 0);
        	}
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getKd_pos_rumah())?"":dataPP.getKd_pos_rumah()), 161, 360, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getArea_code_rumah())?"":dataPP.getArea_code_rumah()), 245, 359, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getTelpon_rumah())?"":dataPP.getTelpon_rumah()), 272, 359, 0);
        	
        	monyong = 0;
 			if(!Common.isEmpty(dataPP.getAlamat_kantor())){
 				String[] alamat_kantor =  StringUtil.pecahParagraf(dataPP.getAlamat_kantor().toUpperCase(), 47);
 	        	for(int i=0; i<alamat_kantor.length; i++) {
 	        		monyong = 12 * i;
 	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							127, 342-monyong, 0);
 	        	}
 			}
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getKd_pos_kantor())?"":dataPP.getKd_pos_kantor()), 161, 316, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getArea_code_kantor())?"":dataPP.getArea_code_kantor()), 249, 315, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getTelpon_kantor())?"":dataPP.getTelpon_kantor()), 275, 315, 0);
        	
        	monyong = 0;
        	String[] alamat_billing = StringUtil.pecahParagraf(addrBill.getMsap_address(), 47);
        	for(int i=0; i<alamat_billing.length; i++) {
        		monyong = 12 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							127, 298-monyong, 0);
        	}
        	
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_zip_code())?"":addrBill.getMsap_zip_code()) , 161, 271, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_area_code1())?"":addrBill.getMsap_area_code1()) , 247, 271, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_phone1())?"":addrBill.getMsap_phone1()) , 275, 271, 0);
    		
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getNo_hp())?"":dataPP.getNo_hp().substring(0, 4)) , 134, 230, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getNo_hp())?"":dataPP.getNo_hp().substring(4)) , 160, 230, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getEmail())?"":dataPP.getEmail()) , 127, 216, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_penghasilan().toUpperCase(), 127, 204, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_smbr_penghasilan(), 127, 187, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_tujuan().toUpperCase(), 127, 174, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_pendanaan().toUpperCase(), 127, 154, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_kerja().toUpperCase(), 127, 135, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_industri().toUpperCase(), 127, 115, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getLsre_relation().toUpperCase(), 275, 88, 0);
    		
    		if(dataPP.getLsre_id()!=1){
    			over.setFontAndSize(times_new_roman,6);
        		//Data tertanggung
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first().toUpperCase(), 352, 620, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_mother().toUpperCase(), 352, 601, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLside_name(), 352, 579, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_no_identity(), 352, 565, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsne_note().toUpperCase(), 352, 549, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_place_birth().toUpperCase(), 352, 531, 0);
    		    //over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(dataPP.getMspe_date_birth()), 165, 531, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getDay2(dataTT.getMspe_date_birth()), 460, 532, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getMonth(dataTT.getMspe_date_birth()), 497, 532, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getYearFourDigit(dataTT.getMspe_date_birth()), 533, 532, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sex2().toUpperCase(), 352, 517, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataTT.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataTT.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 352, 503, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsag_name().toUpperCase(), 352, 415, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsed_name().toUpperCase(), 352, 401, 0);
    		    //data untuk suami/istri &anak2
    		    if(!Common.isEmpty(dataPP.getNama_si())){
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_si())?"":dataTT.getNama_si().toUpperCase(), 410, 475, 0);
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_si())?"":FormatDate.toString(dataTT.getTgllhr_si()), 513, 475, 0);
    			}
    		    if(!Common.isEmpty(dataPP.getNama_anak1())){
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_anak1())?"":dataTT.getNama_anak1().toUpperCase(), 410, 460, 0);
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak1())?"":FormatDate.toString(dataTT.getTgllhr_anak1()), 513, 460, 0);
    			}
    			if(!Common.isEmpty(dataPP.getNama_anak2())){
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_anak2())?"":dataTT.getNama_anak2().toUpperCase(), 410, 445, 0);
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak2())?"":FormatDate.toString(dataTT.getTgllhr_anak2()), 513, 445, 0);
    			}
    			if(!Common.isEmpty(dataPP.getNama_anak3())){
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_anak3())?"":dataTT.getNama_anak3().toUpperCase(), 410, 430, 0);
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak3())?"":FormatDate.toString(dataTT.getTgllhr_anak3()), 513, 430, 0);
    			}
    			
    			   monyong = 0;
    	        	String[] alamat2 = StringUtil.pecahParagraf(dataTT.getAlamat_rumah().toUpperCase(), 47);
    	        	for(int i=0; i<alamat.length; i++) {
    	        		monyong = 12 * i;
    	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							352, 385-monyong, 0);
    	        	}
    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getKd_pos_rumah())?"":dataTT.getKd_pos_rumah()), 387, 360, 0);
    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getArea_code_rumah())?"":dataTT.getArea_code_rumah()), 471, 359, 0);
    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getTelpon_rumah())?"":dataTT.getTelpon_rumah()), 498, 359, 0);
    	        	
    	        	monyong = 0;
    	 			if(!Common.isEmpty(dataPP.getAlamat_kantor())){
    	 				String[] alamat_kantor =  StringUtil.pecahParagraf(dataPP.getAlamat_kantor().toUpperCase(), 47);
    	 	        	for(int i=0; i<alamat_kantor.length; i++) {
    	 	        		monyong = 12 * i;
    	 	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							352, 342-monyong, 0);
    	 	        	}
    	 			}
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getKd_pos_kantor())?"":dataTT.getKd_pos_kantor()), 388, 315, 0);
    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getArea_code_kantor())?"":dataTT.getArea_code_kantor()), 475, 315, 0);
    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getTelpon_kantor())?"":dataTT.getTelpon_kantor()), 500, 315, 0);
    	        	
    	        	monyong = 0;
    	        	String[] alamat_billing2 = StringUtil.pecahParagraf(addrBill.getMsap_address(), 47);
    	        	for(int i=0; i<alamat_billing.length; i++) {
    	        		monyong = 12 * i;
    	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							352, 298-monyong, 0);
    	        	}
    	        	
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_zip_code())?"":addrBill.getMsap_zip_code()) , 387, 271, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_area_code1())?"":addrBill.getMsap_area_code1()) , 472, 271, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_phone1())?"":addrBill.getMsap_phone1()) , 498, 271, 0);
    	    		
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getNo_hp())?"":dataTT.getNo_hp().substring(0, 4)) , 360, 230, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getNo_hp())?"":dataTT.getNo_hp().substring(4)) , 385, 230, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getEmail())?"":dataTT.getEmail()) , 352, 216, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_penghasilan().toUpperCase(), 352, 204, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_smbr_penghasilan(), 352, 187, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_tujuan().toUpperCase(), 352, 174, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_pendanaan().toUpperCase(), 352, 154, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_kerja().toUpperCase(), 352, 135, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_industri().toUpperCase(), 352, 115, 0);
        	}
    		
    		over.setFontAndSize(times_new_roman,6);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getLsbp_nama().toUpperCase(), 90, 62, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_cabang().toUpperCase(), 90, 49, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_kota().toUpperCase(), 90, 37, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_no_ac().toUpperCase(), 320, 62, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_nama().toUpperCase(), 320, 49, 0);
   
		    over.endText();
		   
		    //Halaman Ke 2
		    over = stamp.getOverContent(2);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspo_no_blanko().toUpperCase(), 509, 817, 0);
			//Data Peserta Tambahan
			over.setFontAndSize(times_new_roman,6);
			if(peserta.size()>0){
			int vertikal=769;
			for(int z=0;z<peserta.size();z++){
				vertikal = vertikal-20;
			PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(z);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,pesertaPlus.getNama(),44, vertikal, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(pesertaPlus.getKelamin()==1?"L":"P"),470, vertikal, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,FormatDate.toString(pesertaPlus.getTgl_lahir()),535, vertikal, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,pesertaPlus.getLsre_relation(),280, vertikal, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"-",385, vertikal, 0);
			}
		}
			
			//product utama
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLsdbs_name(),115, 623, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMspo_pay_period().toString(),270, 623, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLscb_pay_mode(),305, 623, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())),375, 623, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())),455, 623, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"-",530, 623, 0);
			 if(dataUsulan.getLku_id().equals("01")){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "X", 363, 645, 0);}
			else{
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "X", 406, 645, 0);}
		 Double Premirider = 0.;
			if(dataUsulan.getDaftaRider().size()>0){
				int vertikal2 = 623;
				for(int z=0;z<dataUsulan.getDaftaRider().size();z++){
				Datarider rider = (Datarider) dataUsulan.getDaftaRider().get(z);
				vertikal2 = vertikal2-12;
				int shit = 0;
 	        	String[] productrider = StringUtil.pecahParagraf(rider.getLsdbs_name().toUpperCase(), 36);
 	        	for(int i=0; i<productrider.length; i++) {
 	        		shit = 9 * i;
 	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, productrider[i],115, vertikal2-shit, 0);
 	        		//over.showTextAligned(PdfContentByte.ALIGN_LEFT,rider.getLsdbs_name(),115, vertikal2, 0);
 	        	}
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT,rider.getLsdbs_name(),115, vertikal2, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,rider.getMspr_ins_period().toString(),270, vertikal2-shit, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,"",315, vertikal2, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatNumber.convertToTwoDigit(new BigDecimal(rider.getMspr_tsi())),375, vertikal2-shit, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatNumber.convertToTwoDigit(new BigDecimal(rider.getMspr_premium())),455, vertikal2-shit, 0);
				Premirider = rider.getMspr_premium();
				}
				
		}
			BigDecimal totalPremiProduct = new BigDecimal(dataUsulan.getMspr_premium() + Premirider);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatNumber.convertToTwoDigit(totalPremiProduct),455, 542, 0);
			
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLscb_pay_mode(),175, 521, 0);
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),140, 489, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),405, 489, 0);
			
			over.setFontAndSize(times_new_roman,7);
			DetilInvestasi detinv0 = (DetilInvestasi) detInv.get(0);
			DetilInvestasi detinv1 = (DetilInvestasi) detInv.get(1);
			DetilInvestasi detinv2 = (DetilInvestasi) detInv.get(2);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Fix Income :"+ (detinv0.getMdu_persen1()==null?Integer.toString(0):detinv0.getMdu_persen1()) + " %",40, 188, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Dynamic :"+ (detinv1.getMdu_persen1()==null?Integer.toString(0):detinv1.getMdu_persen1()) + " %",100, 188, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Aggressive :"+ (detinv2.getMdu_persen1()==null?Integer.toString(0):detinv2.getMdu_persen1()) + " %",160, 188, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (dataUsulan.getMste_flag_investasi()==1?"Setelah permintaan asuransi disetujui oleh Bagian Underwriting dan Calon Pemegang Polis telah setuju serta membayar ekstra premi (jika ada)." 
			: "Langsung setelah dana diterima di rekening yang ditentukan oleh PT Asuransi Jiwa Sinarmas MSIG Tbk. dan SPAJ telah diterima di bagian Underwriting."), 40, 163, 0);
			//rekening pembayaran polis autodebet tabungan
			if (rekClient!=null){
			over.setFontAndSize(times_new_roman,6);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getLsbp_nama().toUpperCase(), 100, 449, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_cabang().toUpperCase(), 100, 433, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_kota().toUpperCase(), 100, 417, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_no_ac().toUpperCase(), 100, 402, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_nama().toUpperCase(), 100, 386, 0);
			}
    		//rekening pembayaran polis autodebet kartu kredit
    		if (accRecur==null){
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getLsbp_nama().toUpperCase(), 393, 450, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_cabang().toUpperCase(), 393, 434, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_kota().toUpperCase(), 393, 418, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_no_ac().toUpperCase(), 393, 406, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_nama().toUpperCase(), 393, 384, 0);
    		}
			//data manfaat yang ditunjuk
			int j=0;
			for(int i=0;i<benef.size();i++){
			Benefeciary benefi = (Benefeciary) benef.get(i);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_first(),44, 89-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(benefi.getMsaw_sex()==1?"L":"P"),315, 89-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getSmsaw_birth(),385, 89-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getLsre_relation(),470, 89-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_persen().toString(),539, 89-j, 0);
			j+=19;
		}
			if(inv!=null){
				Double premiTambahan = 0.;
				DetilTopUp daftarTopup = inv.getDaftartopup();
				Double topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala())?new Double(0):daftarTopup.getPremi_berkala();
				Double topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal())?new Double(0):daftarTopup.getPremi_tunggal();
				Double totalTopup = topUpBerkala + topUpTunggal;
				BigDecimal totalPremiPokok = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup));
				BigDecimal totalPremi = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup) + premiTambahan);
				Double ratePremiPokok = (inv.getMu_jlh_premi()/ totalPremiPokok.doubleValue()) * 100;
				Double ratePremiTopup = ((Common.isEmpty(inv.getMu_jlh_tu())?0.:inv.getMu_jlh_tu()) / totalPremi.doubleValue()) * 100;
				over.setFontAndSize(times_new_roman,7);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(inv.getMu_jlh_premi())),305, 273, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER,ratePremiPokok.intValue() +"%",439, 273, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,(Common.isEmpty(totalTopup)?"-": FormatNumber.convertToTwoDigit(new BigDecimal(totalTopup))),305, 252, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER,ratePremiTopup.intValue()+"%",439, 252, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(totalPremi  ) ,305, 235, 0);
					/*over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_tunggal())?"":(daftarTopup.getId_tunggal()==2?"X":""),183, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_berkala())?"":(daftarTopup.getId_berkala()==5?"X":""),222, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,(Common.isEmpty(totalTopup)?"-": FormatNumber.convertToTwoDigit(new BigDecimal(totalTopup))),370, 605, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(totalPremi  ) ,370, 672, 0);*/
				
				/*over.showTextAligned(PdfContentByte.ALIGN_LEFT,ratePremiPokok.intValue() + "% : " + ratePremiTopup.intValue() + "%",300, 659, 0);*/
			}
			 over.endText();
			 
		 over = stamp.getOverContent(3);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspo_no_blanko().toUpperCase(), 509, 817, 0);
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "/kg", 85, 769, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "/cm", 86, 748, 0);
			over.setFontAndSize(times_new_roman,6);
			/*
			int k = 0;
			if(lsre_id!=1){
				k=1;
			}*/
			int x=0;
			int y=0;
			for(int i=0;i<mq.size();i++){
		// Questionare DMTM	
			//Berat dan Tinggi, 1a
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_berat().toString(), 380+x, 769, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_tinggi().toString(), 380+x, 748, 0);
			//Pertanyaan 1b
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_berat_berubah()==1?"YA" : "TIDAK"), 380+x, 727, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMsadm_berubah_desc())?"":mq.get(i).getMsadm_berubah_desc()), 43, 717-y, 0);
			//Pertanyaan 2
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(i).getMsadm_sehat()==1?"YA" : "TIDAK"),385+x, 652, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMsadm_sehat_desc())?"":mq.get(i).getMsadm_sehat_desc()), 39, 647-y, 0);
			//Pertanyaan 3
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_penyakit()==1?"YA" : "TIDAK"), 380+x, 588, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMsadm_penyakit_desc())?"":mq.get(i).getMsadm_penyakit_desc()), 35, 567-y, 0);
			//pertanyaan 4a
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_medis()==1?"YA" : "TIDAK"), 380+x, 505, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMsadm_medis_desc())?"":mq.get(i).getMsadm_medis_desc()), 39, 484-y, 0);
			//pertanyaan 4b
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_medis_alt()==1?"YA" : "TIDAK"), 380+x, 418, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMsadm_medis_alt_desc())?"":mq.get(i).getMsadm_medis_alt_desc()), 42, 400-y, 0);
			//Pertanyaan 5
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_family_sick()==1?"YA" : "TIDAK"), 380+x, 337, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMsadm_family_sick_desc())?"":mq.get(i).getMsadm_family_sick_desc()), 38, 305-y, 0);
			//Pertanyaan 6 Khusus Wanita
			if(dataTT.getMspe_sex()==0){
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(i).getMsadm_pregnant()==1?"YA" : "TIDAK"),385+x, 247, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(i).getMsadm_abortion()==1?"YA" : "TIDAK"),385+x, 201, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(i).getMsadm_usg()==1?"YA" : "TIDAK"),385+x, 121, 0);
				//over.showTextAligned(PdfContentByte.ALIGN_CENTER,Common.isEmpty(mq.get(i).getMsadm_pregnant_time())?"":mq.get(i).getMsadm_pregnant_time().toString(),330, 112, 0);
			}
			x+=43;
			y+=10;
			}

		over.endText();
		
		 over = stamp.getOverContent(4);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspo_no_blanko().toUpperCase(), 509, 817, 0);
			over.setFontAndSize(times_new_roman,7);
			int z=0;
			int zt=0;
			for(int i=0;i<mq.size();i++){
		// Questionare DMTM	
			//Pertanyaan 1
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_hobby())?"":(Integer.parseInt(mq.get(i).getMsadd_hobby())==1?"YA" : "TIDAK"), 380+z, 769, 0);
			//Pertanyaan 2
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_flight())?"":(mq.get(i).getMsadd_flight()==1?"YA" : "TIDAK"), 380+z, 691, 0);
			//Pertanyaan 3a
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_smoke())?"":(mq.get(i).getMsadd_smoke()==1?"YA" : "TIDAK"), 380+z, 622, 0);
			//Pertanyaan 3b
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_drugs())?"":(mq.get(i).getMsadd_drugs()==1?"YA" : "TIDAK"), 380+z, 553, 0);
			//Pertanyaan 3c
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_drugs())?"":(mq.get(i).getMsadd_drugs()==1?"YA" : "TIDAK"), 380+z, 488, 0);
			//Pertanyaan 4b
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(mq.get(i).getMsadd_life_ins())?"":(mq.get(i).getMsadd_life_ins()==1?"YA" : "TIDAK"), 375+z, 418, 0);
			z+=43;
			}
		over.endText();
			stamp.close();	
			
			File l_file = new File(outputName);
			
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
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
	        
			return null;
		}
		
		public ModelAndView ekonfirmasi(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String reg_spaj = request.getParameter("spaj");
			Date sysdate = elionsManager.selectSysdate();
			Integer data2 = uwManager.selectCallDate(reg_spaj);
			String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
			
			String dir = props.getProperty("pdf.template.ekonfirmasi");
			/*if(data3==0){
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Tanggal Call Date kosong, Silakan klik tombol 'Call Date'');window.close();</script>");
    			sos.close();
			}*/
			
			if(data2==0){
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Silakan Mengisi Tanggal CALL DATE Terlebih Dahulu');window.close();</script>");
    			sos.close();
			}
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
	        String espajFile = dir + "\\surat_konfirmasiDMTM_"+reg_spaj+".pdf";
	        
	        HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "DMTM");
			moreInfo.put("Subject", "SURAT KONFIRMASI DMTM");
			
			PdfContentByte over;
			BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
	        PdfReader reader = new PdfReader(props.getProperty("pdf.template.ekonfirmasi")+"\\surat_konfirmasiDMTM.pdf");
	        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"surat_konfirmasiDMTM_"+reg_spaj+".pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
	        
	        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
	        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
	        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
	        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
	        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
	        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
	        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);
	        List detInv = this.uwManager.selectdetilinvestasimallspaj(reg_spaj);
	        List benef = this.elionsManager.select_benef(reg_spaj);
	        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
	        List<MedQuest> mq = uwManager.selectMedQuest(reg_spaj,null);
	        Agen agen =this.elionsManager.select_detilagen(reg_spaj);
	        dataUsulan.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
	        
	        over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman,9);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 360, 668, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(sysdate), 85, 668, 0);
		    
		    over.setFontAndSize(times_new_roman,6);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 190, 589, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first().toUpperCase(), 190, 576, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLsdbs_name().toUpperCase(), 190, 564, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 190, 551, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())), 245, 551, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 190, 538, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())), 245, 538, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMcl_first(),190, 490, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMsag_id(),190, 478, 0);
		    
		    Double premiTambahan = 0.;
			Double premiExtra = (Common.isEmpty(uwManager.selectSumPremiExtra(reg_spaj))? 0.:uwManager.selectSumPremiExtra(reg_spaj));
			Double premiRider = (Common.isEmpty(uwManager.selectSumPremiRider(reg_spaj))? 0.:uwManager.selectSumPremiRider(reg_spaj));
			premiTambahan = premiTambahan + premiExtra + premiRider;
		   if(inv!=null){
				DetilTopUp daftarTopup = inv.getDaftartopup();
				Double topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala())?new Double(0):daftarTopup.getPremi_berkala();
				Double topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal())?new Double(0):daftarTopup.getPremi_tunggal();
				Double totalTopup = topUpBerkala + topUpTunggal;
				BigDecimal totalPremiPokok = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup));
				BigDecimal totalPremi = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup) + premiTambahan);
				
					/*over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),263, 499, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(inv.getMu_jlh_premi())),315, 499, 0);
					
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(totalTopup)?"":dataUsulan.getLku_symbol()),263, 491, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(topUpTunggal)),315, 491, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_tunggal())?"T*":(daftarTopup.getId_tunggal()==2?"X":"T*"),317, 491, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,"; ",324, 490, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(totalTopup)?"":dataUsulan.getLku_symbol()),329, 491, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(topUpBerkala)),381, 491, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_berkala())?"B*":(daftarTopup.getId_berkala()==5?"X":"B*"),386, 491, 0);
					
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(totalTopup)?"":dataUsulan.getLku_symbol()),263, 483, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(premiRider)),315, 483, 0);
					
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(totalTopup)?"":dataUsulan.getLku_symbol()),263, 475, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(totalPremi),315, 475, 0);*/
					
					over.setFontAndSize(times_new_roman,7);
					Date date = dataPP.getMspo_call_date();
					String tgl=FormatDate.toStampString(date);					
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, tgl, 108, 159, 0);
					//over.showTextAligned(PdfContentByte.ALIGN_LEFT, "* Ket : T=Tunggal , B=Berkala",54, 120, 0);
				}
		    over.endText();
			stamp.close();	
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
				logger.error("ERROR :", e);
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
	        
			return null;
		}
		
		//Ryan - SPAJ ONLINE buat AGENCY
		public ModelAndView espajonline(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String reg_spaj = request.getParameter("spaj");
			Date sysdate = elionsManager.selectSysdate();
			List<String> pdfs = new ArrayList<String>();
			boolean suksesMerge = false;
			String spaj = "";
			String konfirmasi = "";
			//String noE ="";
			
			String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
			
			String dir = props.getProperty("pdf.template.espajonline");
			
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
	        String espajFile = dir + "\\espaj_"+reg_spaj+".pdf";
	        
	        HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "AGENCY");
			moreInfo.put("Subject", "E-SPAJ ONLINE");
			
			PdfContentByte over;
			BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
	        PdfReader reader = new PdfReader(props.getProperty("pdf.template.espajonline")+"\\spajonline.pdf");
	       /* String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espajONLINE_"+reg_spaj+".pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));*/
	        OutputStream output = new FileOutputStream(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espajONLINE_"+reg_spaj+".pdf");
	       /* PdfReader reader2 = new PdfReader(props.getProperty("pdf.template.ekonfirmasiagen")+"\\konfirmasiagency.pdf");
	        String outputName2 = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"surat_konfirmasiagency_"+reg_spaj+".pdf";
	        PdfStamper stamp2 = new PdfStamper(reader,new FileOutputStream(outputName));*/
	        
	        spaj = dir + "\\spajonline.pdf";
	        konfirmasi=dir +"\\konfirmasiagency.pdf";
	        pdfs.add(spaj);
	        pdfs.add(konfirmasi);
	        suksesMerge = MergePDF.concatPDFs(pdfs, output, false);	
	        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espajONLINE_"+reg_spaj+".pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
	        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
	       // String noE =uwManager.noTest(dataPP.getLca_id());
	        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
	        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
	        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
	        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
	        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
	        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);
	        List detInv = this.uwManager.selectdetilinvestasimallspaj(reg_spaj);
	        List benef = this.elionsManager.select_benef(reg_spaj);
	        List medQuest=this.uwManager.selectquestionareDMTM(reg_spaj);
	        List peserta=this.uwManager.select_all_mst_peserta(reg_spaj);
	        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
	        List<MedQuest> mq = uwManager.selectMedQuest(reg_spaj,null);
	        Agen agen =this.elionsManager.select_detilagen(reg_spaj);
	        List namaBank =uwManager.namaBank(accRecur.getLbn_id());
	        dataUsulan.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
	        Integer data2 = uwManager.selectFlagQuestionare(reg_spaj);
			String noE =uwManager.spajOnline(reg_spaj, dataPP.getLca_id());
	        
			if(data2==0){
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Silakan Mengisi Questionare Terlebih Dahulu');window.close();</script>");
    			sos.close();
			}
	        stamp.setMoreInfo(moreInfo);
			
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 415, 663, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, noE.toUpperCase(), 84, 662, 0);
		    
		    over.setFontAndSize(times_new_roman,6);
		  //Data PP
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 127, 621, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_mother().toUpperCase(), 127, 602, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLside_name(), 127, 580, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_no_identity(), 127, 566, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsne_note().toUpperCase(), 127, 550, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_place_birth().toUpperCase(), 127, 532, 0);
		    //over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(dataPP.getMspe_date_birth()), 165, 531, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getDay2(dataPP.getMspe_date_birth()), 235, 533, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getMonth(dataPP.getMspe_date_birth()), 272, 533, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getYearFourDigit(dataPP.getMspe_date_birth()), 308, 533, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sex2().toUpperCase(), 127, 518, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataPP.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataPP.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 127, 504, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsag_name().toUpperCase(), 127, 415, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsed_name().toUpperCase(), 127, 401, 0);
		    
		    //data untuk suami/istri &anak2
		    if(!Common.isEmpty(dataPP.getNama_si())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_si())?"":dataPP.getNama_si().toUpperCase(), 185, 475, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_si())?"":FormatDate.toString(dataPP.getTgllhr_si()), 287, 475, 0);
			}
		    if(!Common.isEmpty(dataPP.getNama_anak1())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_anak1())?"":dataPP.getNama_anak1().toUpperCase(), 185, 460, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak1())?"":FormatDate.toString(dataPP.getTgllhr_anak1()), 287, 460, 0);
			}
			if(!Common.isEmpty(dataPP.getNama_anak2())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_anak2())?"":dataPP.getNama_anak2().toUpperCase(), 185, 445, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak2())?"":FormatDate.toString(dataPP.getTgllhr_anak2()), 287, 445, 0);
			}
			if(!Common.isEmpty(dataPP.getNama_anak3())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_anak3())?"":dataPP.getNama_anak3().toUpperCase(), 185, 430, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak3())?"":FormatDate.toString(dataPP.getTgllhr_anak3()), 287, 430, 0);
			}
		    
		    int monyong = 0;
        	String[] alamat = StringUtil.pecahParagraf(dataPP.getAlamat_rumah().toUpperCase(), 47);
        	for(int i=0; i<alamat.length; i++) {
        		monyong = 12 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							128, 385-monyong, 0);
        	}
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getKd_pos_rumah())?"":dataPP.getKd_pos_rumah()), 161, 360, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getArea_code_rumah())?"":dataPP.getArea_code_rumah()), 245, 359, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getTelpon_rumah())?"":dataPP.getTelpon_rumah()), 272, 359, 0);
        	
        	monyong = 0;
 			if(!Common.isEmpty(dataPP.getAlamat_kantor())){
 				String[] alamat_kantor =  StringUtil.pecahParagraf(dataPP.getAlamat_kantor().toUpperCase(), 47);
 	        	for(int i=0; i<alamat_kantor.length; i++) {
 	        		monyong = 12 * i;
 	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							127, 342-monyong, 0);
 	        	}
 			}
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getKd_pos_kantor())?"":dataPP.getKd_pos_kantor()), 161, 316, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getArea_code_kantor())?"":dataPP.getArea_code_kantor()), 249, 315, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getTelpon_kantor())?"":dataPP.getTelpon_kantor()), 275, 315, 0);
        	
        	monyong = 0;
        	String[] alamat_billing = StringUtil.pecahParagraf(addrBill.getMsap_address(), 47);
        	for(int i=0; i<alamat_billing.length; i++) {
        		monyong = 12 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							127, 298-monyong, 0);
        	}
        	
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_zip_code())?"":addrBill.getMsap_zip_code()) , 161, 271, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_area_code1())?"":addrBill.getMsap_area_code1()) , 247, 271, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_phone1())?"":addrBill.getMsap_phone1()) , 275, 271, 0);
    		
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getNo_hp())?"":dataPP.getNo_hp().substring(0, 4)) , 134, 230, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getNo_hp())?"":dataPP.getNo_hp().substring(4)) , 160, 230, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getEmail())?"":dataPP.getEmail()) , 127, 216, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_penghasilan().toUpperCase(), 127, 204, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_smbr_penghasilan(), 127, 187, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_tujuan().toUpperCase(), 127, 174, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_pendanaan().toUpperCase(), 127, 154, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_kerja().toUpperCase(), 127, 135, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_industri().toUpperCase(), 127, 115, 0);
    		over.setFontAndSize(times_new_roman,8);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getLsre_relation().toUpperCase(), 282, 88, 0);
    		
    		//if(dataPP.getLsre_id()!=1){
    			over.setFontAndSize(times_new_roman,6);
        		//Data tertanggung
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first().toUpperCase(), 352, 620, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_mother().toUpperCase(), 352, 601, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLside_name(), 352, 579, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_no_identity(), 352, 565, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsne_note().toUpperCase(), 352, 549, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_place_birth().toUpperCase(), 352, 531, 0);
    		    //over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(dataPP.getMspe_date_birth()), 165, 531, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getDay2(dataTT.getMspe_date_birth()), 460, 532, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getMonth(dataTT.getMspe_date_birth()), 497, 532, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getYearFourDigit(dataTT.getMspe_date_birth()), 533, 532, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sex2().toUpperCase(), 352, 517, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataTT.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataTT.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 352, 503, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsag_name().toUpperCase(), 352, 415, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsed_name().toUpperCase(), 352, 401, 0);
    		    //data untuk suami/istri &anak2
    		    if(!Common.isEmpty(dataPP.getNama_si())){
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_si())?"":dataTT.getNama_si().toUpperCase(), 410, 475, 0);
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_si())?"":FormatDate.toString(dataTT.getTgllhr_si()), 513, 475, 0);
    			}
    		    if(!Common.isEmpty(dataPP.getNama_anak1())){
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_anak1())?"":dataTT.getNama_anak1().toUpperCase(), 410, 460, 0);
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak1())?"":FormatDate.toString(dataTT.getTgllhr_anak1()), 513, 460, 0);
    			}
    			if(!Common.isEmpty(dataPP.getNama_anak2())){
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_anak2())?"":dataTT.getNama_anak2().toUpperCase(), 410, 445, 0);
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak2())?"":FormatDate.toString(dataTT.getTgllhr_anak2()), 513, 445, 0);
    			}
    			if(!Common.isEmpty(dataPP.getNama_anak3())){
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_anak3())?"":dataTT.getNama_anak3().toUpperCase(), 410, 430, 0);
    				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak3())?"":FormatDate.toString(dataTT.getTgllhr_anak3()), 513, 430, 0);
    			}
    			
    			   monyong = 0;
    	        	String[] alamat2 = StringUtil.pecahParagraf(dataTT.getAlamat_rumah().toUpperCase(), 47);
    	        	for(int i=0; i<alamat.length; i++) {
    	        		monyong = 12 * i;
    	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							352, 385-monyong, 0);
    	        	}
    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getKd_pos_rumah())?"":dataTT.getKd_pos_rumah()), 387, 360, 0);
    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getArea_code_rumah())?"":dataTT.getArea_code_rumah()), 471, 359, 0);
    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getTelpon_rumah())?"":dataTT.getTelpon_rumah()), 498, 359, 0);
    	        	
    	        	monyong = 0;
    	 			if(!Common.isEmpty(dataPP.getAlamat_kantor())){
    	 				String[] alamat_kantor =  StringUtil.pecahParagraf(dataPP.getAlamat_kantor().toUpperCase(), 47);
    	 	        	for(int i=0; i<alamat_kantor.length; i++) {
    	 	        		monyong = 12 * i;
    	 	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							352, 342-monyong, 0);
    	 	        	}
    	 			}
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getKd_pos_kantor())?"":dataTT.getKd_pos_kantor()), 388, 315, 0);
    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getArea_code_kantor())?"":dataTT.getArea_code_kantor()), 475, 315, 0);
    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getTelpon_kantor())?"":dataTT.getTelpon_kantor()), 500, 315, 0);
    	        	
    	        	monyong = 0;
    	        	String[] alamat_billing2 = StringUtil.pecahParagraf(addrBill.getMsap_address(), 47);
    	        	for(int i=0; i<alamat_billing.length; i++) {
    	        		monyong = 12 * i;
    	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							352, 298-monyong, 0);
    	        	}
    	        	
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_zip_code())?"":addrBill.getMsap_zip_code()) , 387, 271, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_area_code1())?"":addrBill.getMsap_area_code1()) , 472, 271, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_phone1())?"":addrBill.getMsap_phone1()) , 498, 271, 0);
    	    		
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getNo_hp())?"":dataTT.getNo_hp().substring(0, 4)) , 360, 230, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getNo_hp())?"":dataTT.getNo_hp().substring(4)) , 385, 230, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getEmail())?"":dataTT.getEmail()) , 352, 216, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_penghasilan().toUpperCase(), 352, 204, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_smbr_penghasilan(), 352, 187, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_tujuan().toUpperCase(), 352, 174, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_pendanaan().toUpperCase(), 352, 154, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_kerja().toUpperCase(), 352, 135, 0);
    	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_industri().toUpperCase(), 352, 115, 0);
        	
    		
    		over.setFontAndSize(times_new_roman,6);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getLsbp_nama().toUpperCase(), 90, 62, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_cabang().toUpperCase(), 90, 49, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_kota().toUpperCase(), 90, 37, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_no_ac().toUpperCase(), 320, 62, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_nama().toUpperCase(), 320, 49, 0);
   
		    over.endText();
		   
		    //Halaman Ke 2
		    over = stamp.getOverContent(2);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, noE.toUpperCase(), 504, 817, 0);
			//Data Peserta Tambahan
			over.setFontAndSize(times_new_roman,6);
			if(peserta.size()>0){
			int vertikal=769;
			for(int z=0;z<peserta.size();z++){
				vertikal = vertikal-20;
			PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(z);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,pesertaPlus.getNama(),44, vertikal, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(pesertaPlus.getKelamin()==1?"L":"P"),470, vertikal, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,FormatDate.toString(pesertaPlus.getTgl_lahir()),535, vertikal, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,pesertaPlus.getLsre_relation().toUpperCase(),280, vertikal, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"-",385, vertikal, 0);
			}
		}
			
			//product utama
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLsdbs_name(),115, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMspo_pay_period().toString(),270, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLscb_pay_mode(),315, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())),425, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())),505, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"-",530, 587, 0);
			if(inv.getLt_id().equals("01")){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "X", 363, 610, 0);}
			else{
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "X", 406, 610, 0);}
			Double Premirider = 0.;
			if(dataUsulan.getDaftaRider().size()>0){
				int vertikal2 = 588;
				for(int z=0;z<dataUsulan.getDaftaRider().size();z++){
				Datarider rider = (Datarider) dataUsulan.getDaftaRider().get(z);
				vertikal2 = vertikal2-15;
				 monyong = 0;
		        	String[] produkRider = StringUtil.pecahParagraf(rider.getLsdbs_name(), 37);
		        	for(int i=0; i<produkRider.length; i++) {
		        		monyong = 8 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, produkRider[i],							115, vertikal2-monyong, 0);
		        	}
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT,rider.getLsdbs_name(),115, vertikal2, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,rider.getMspr_ins_period().toString(),270, vertikal2, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,"",315, vertikal2, 0);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(rider.getMspr_tsi())),425, vertikal2, 0);
				if(rider.getMspr_premium()==0){
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,"-",505, vertikal2, 0);
				}else{
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(rider.getMspr_premium())),505, vertikal2, 0);
				}
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatNumber.convertToTwoDigit(new BigDecimal(rider.getMspr_premium())),455, vertikal2, 0);
				Premirider = rider.getMspr_premium();
				}
				
		}
			BigDecimal totalPremiProduct = new BigDecimal(dataUsulan.getMspr_premium() + Premirider);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatNumber.convertToTwoDigit(totalPremiProduct),477, 523, 0);
			
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLscb_pay_mode(),175, 500, 0);
			over.setFontAndSize(times_new_roman,8);
			//over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),140, 489, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),405, 465, 0);
			over.setFontAndSize(times_new_roman,7);
			DetilInvestasi detinv0 = (DetilInvestasi) detInv.get(0);
			DetilInvestasi detinv1 = (DetilInvestasi) detInv.get(1);
			DetilInvestasi detinv2 = (DetilInvestasi) detInv.get(2);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Fix Income :"+ (detinv0.getMdu_persen1()==null?Integer.toString(0):detinv0.getMdu_persen1()) + " %",40, 188, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Dynamic :"+ (detinv1.getMdu_persen1()==null?Integer.toString(0):detinv1.getMdu_persen1()) + " %",100, 188, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Aggressive :"+ (detinv2.getMdu_persen1()==null?Integer.toString(0):detinv2.getMdu_persen1()) + " %",160, 188, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (dataUsulan.getMste_flag_investasi()==1?"Setelah permintaan asuransi disetujui oleh Bagian Underwriting dan Calon Pemegang Polis telah setuju serta membayar ekstra premi (jika ada)." 
			: "Langsung setelah dana diterima di rekening yang ditentukan oleh PT Asuransi Jiwa Sinarmas MSIG Tbk. dan SPAJ telah diterima di bagian Underwriting."), 40, 163, 0);
			//rekening pembayaran polis autodebet tabungan
			if (rekClient!=null){
			over.setFontAndSize(times_new_roman,6);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(rekClient.getLsbp_nama())?"":rekClient.getLsbp_nama().toUpperCase(), 100, 427, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(rekClient.getMrc_cabang())?"":rekClient.getMrc_cabang().toUpperCase(), 100, 411, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(rekClient.getMrc_kota())?"":rekClient.getMrc_kota().toUpperCase(), 100, 396, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(rekClient.getMrc_no_ac())?"":rekClient.getMrc_no_ac().toUpperCase(), 100, 380, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(rekClient.getMrc_nama())?"":rekClient.getMrc_nama().toUpperCase(), 100, 365, 0);
			}
		
    		//rekening pembayaran polis autodebet kartu kredit
		if (dataTT.getMste_flag_cc()==1){
    		if (accRecur!=null){
    			String bank_pusat = "";
    			String bank_cabang= "";
    			
    			if(!namaBank.isEmpty()){
        			HashMap m = (HashMap)namaBank.get(0);
        			bank_pusat = (String)m.get("LSBP_NAMA");
        			bank_cabang = (String)m.get("LBN_NAMA");
    			}
    			
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(bank_pusat)?"":bank_pusat.toUpperCase()+" - "+bank_cabang.toUpperCase(), 393, 429, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_acc_no())?"":accRecur.getMar_acc_no().toUpperCase()/*accRecur.getLbn_nama().toUpperCase()*/, 393, 413, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_holder())?"":accRecur.getMar_holder().toUpperCase()/*accRecur.getLbn_nama().toUpperCase()*/, 393, 397, 0);
			//over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getLbn_nama())?"getLbn_nama":accRecur.getLbn_nama().toUpperCase()/*accRecur.getLbn_nama().toUpperCase()*/, 393, 384, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_expired())?"":FormatDate.toIndonesian(accRecur.getMar_expired())/*accRecur.getLbn_nama().toUpperCase()*/, 393, 362, 0);
    		/*over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_acc_no().toUpperCase(), 393, 404, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_holder().toUpperCase(), 393, 388, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_jenis().toString(), 393, 376, 0);*/
    		//over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_expired().parse(s), 393, 354, 0);
    		}
		}
			//data manfaat yang ditunjuk
    		if(benef.size()>0){
			int j=0;
			for(int i=0;i<benef.size();i++){
			Benefeciary benefi = (Benefeciary) benef.get(i);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_first(),44, 89-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(benefi.getMsaw_sex()==1?"L":"P"),315, 89-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getSmsaw_birth(),385, 89-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getLsre_relation().toUpperCase(),470, 89-j, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_persen().toString(),539, 89-j, 0);
			j+=19;}
		}
 			if(inv!=null){
				Double premiTambahan = 0.;
				DetilTopUp daftarTopup = inv.getDaftartopup();
				Double topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala())?new Double(0):daftarTopup.getPremi_berkala();
				Double topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal())?new Double(0):daftarTopup.getPremi_tunggal();
				Double totalTopup = topUpBerkala + topUpTunggal;
				BigDecimal totalPremiPokok = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup));
				BigDecimal totalPremi = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup) + premiTambahan);
				Double ratePremiPokok = (inv.getMu_jlh_premi()/ totalPremiPokok.doubleValue()) * 100;
				Double ratePremiTopup = ((Common.isEmpty(inv.getMu_jlh_tu())?0.:inv.getMu_jlh_tu()) / totalPremi.doubleValue()) * 100;
				over.setFontAndSize(times_new_roman,7);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(inv.getMu_jlh_premi())),305, 273, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,ratePremiPokok.intValue() +"%",450, 273, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,(Common.isEmpty(totalTopup)?"-": FormatNumber.convertToTwoDigit(new BigDecimal(totalTopup))),305, 252, 0);
				/*	over.showTextAligned(PdfContentByte.ALIGN_RIGHT,"",305, 252, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,"",305, 252, 0);*/
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,ratePremiTopup.intValue()+"%",450, 252, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(totalPremi  ) ,305, 235, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_tunggal())?"":(daftarTopup.getId_tunggal()==2?"X":""),128, 255, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_berkala())?"":(daftarTopup.getId_berkala()==5?"X":""),176, 255, 0);
					/*over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_tunggal())?"":(daftarTopup.getId_tunggal()==2?"X":""),183, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_berkala())?"":(daftarTopup.getId_berkala()==5?"X":""),222, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,(Common.isEmpty(totalTopup)?"-": FormatNumber.convertToTwoDigit(new BigDecimal(totalTopup))),370, 605, 0);
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(totalPremi  ) ,370, 672, 0);*/
				
				/*over.showTextAligned(PdfContentByte.ALIGN_LEFT,ratePremiPokok.intValue() + "% : " + ratePremiTopup.intValue() + "%",300, 659, 0);*/
			}
			
			 over.endText();
			 
		 over = stamp.getOverContent(3);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, noE.toUpperCase(), 504, 817, 0);
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "/kg", 85, 769, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "/cm", 86, 748, 0);
			
			int x=0;
			int y=0;
			int y1=0;
		for(int i=0;i<mq.size();i++){
			Integer kelamin =null;
			if(mq.get(i).getMste_insured_no()==0){
				kelamin =dataPP.getMspe_sex();
			}else if (mq.get(i).getMste_insured_no()==1){
				kelamin =dataTT.getMspe_sex();
			}else if (mq.get(i).getMste_insured_no()>1){
				if(peserta !=null){
					PesertaPlus m = (PesertaPlus) peserta.get(0);
					kelamin = m.getKelamin();
				}
			}	
		
		// Questionare SPAJ ONLINE
			//Berat dan Tinggi, 1a
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_berat().toString(), 290+x, 769, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_tinggi().toString(), 290+x, 748, 0);
			//Pertanyaan 1b
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_berat_berubah()==1?"YA" : "TIDAK"), 292+x, 727, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
			(Common.isEmpty(mq.get(i).getMsadm_berubah_desc())?"":mq.get(i).getMsadm_berubah_desc()), 45, 710-y, 0);
			//Pertanyaan 2
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(i).getMsadm_sehat()==1?"YA" : "TIDAK"),300+x, 652, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
			(Common.isEmpty(mq.get(i).getMsadm_sehat_desc())?"":mq.get(i).getMsadm_sehat_desc()), 39, 647-y, 0);
			//Pertanyaan 3
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_penyakit()==1?"YA" : "TIDAK"), 294+x, 588, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
			(Common.isEmpty(mq.get(i).getMsadm_penyakit_desc())?"":mq.get(i).getMsadm_penyakit_desc()), 35, 541-y, 0);
			//pertanyaan 4a
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_medis()==1?"YA" : "TIDAK"), 294+x, 476, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
			(Common.isEmpty(mq.get(i).getMsadm_medis_desc())?"":mq.get(i).getMsadm_medis_desc()), 45, 446-y, 0);
			//pertanyaan 4b
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_medis_alt()==1?"YA" : "TIDAK"), 294+x, 390, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
			(Common.isEmpty(mq.get(i).getMsadm_medis_alt_desc())?"":mq.get(i).getMsadm_medis_alt_desc()), 45, 360-y, 0);
			//Pertanyaan 5
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_family_sick()==1?"YA" : "TIDAK"), 294+x, 308, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
			(Common.isEmpty(mq.get(i).getMsadm_family_sick_desc())?"":mq.get(i).getMsadm_family_sick_desc()), 35, 263-y, 0);
			//Pertanyaan 6 Khusus Wanita
			if(kelamin==0){
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(i).getMsadm_pregnant()==1?"YA" : "TIDAK"),306+x, 218, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadm_pregnant_desc())?"":mq.get(i).getMsadm_pregnant_desc()), 48, 203-y1, 0);
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(i).getMsadm_abortion()==1?"YA" : "TIDAK"),306+x, 174, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadm_pregnant_desc())?"":mq.get(i).getMsadm_abortion_desc())+(Common.isEmpty(mq.get(i).getMsadm_pregnant_time())?"":mq.get(i).getMsadm_pregnant_time().toString()), 48, 152-y1, 0);
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(i).getMsadm_usg()==1?"YA" : "TIDAK"),306+x, 94, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadm_usg_desc())?"":mq.get(i).getMsadm_usg_desc()),55, 78-y1, 0);
				y1+=6;
			}
			x+=43;
			y+=10;
			
		}
		over.endText();
		 over = stamp.getOverContent(4);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, noE.toUpperCase(), 504, 817, 0);
			over.setFontAndSize(times_new_roman,7);
			int z=0;
			for(int i=0;i<mq.size();i++){
		// Questionare SPAJ ONLINE
			//PERTANYAAN PRIBADI 
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_hobby())?"":(Integer.parseInt(mq.get(i).getMsadd_hobby())==1?"YA" : "TIDAK"), 298+z, 769, 0);
			//Pertanyaan 2
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_flight())?"":(mq.get(i).getMsadd_flight()==1?"YA" : "TIDAK"), 298+z, 691, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
			(Common.isEmpty(mq.get(i).getMsadd_desc_flight())?"":mq.get(i).getMsadd_desc_flight()), 33, 693-y, 0);
			//Pertanyaan 3a
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_smoke())?"":(mq.get(i).getMsadd_smoke()==1?"YA" : "TIDAK"), 298+z, 622, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,  (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
			(Common.isEmpty(mq.get(i).getNsadd_many_cig())?"":mq.get(i).getNsadd_many_cig()), 39, 630-y, 0);
			//Pertanyaan 3b
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_drink_beer())?"":(mq.get(i).getMsadd_drink_beer()==1?"YA" : "TIDAK"), 298+z, 553, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,  (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
			(Common.isEmpty(mq.get(i).getMsadd_drink_beer_desc())?"":mq.get(i).getMsadd_drink_beer_desc()), 42, 563-y, 0);
			//Pertanyaan 3c
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_drugs())?"":(mq.get(i).getMsadd_drugs()==1?"YA" : "TIDAK"), 298+z, 488, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,  (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
			(Common.isEmpty(mq.get(i).getMsadd_reason_drugs())?"":mq.get(i).getMsadd_reason_drugs()), 42, 492-y, 0);
			//Pertanyaan 4b
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(mq.get(i).getMsadd_life_ins())?"":(mq.get(i).getMsadd_life_ins()==1?"YA" : "TIDAK"), 290+z, 418, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,  (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TU : ":mq.get(i).getMste_insured_no()==2?"TT- I : ":mq.get(i).getMste_insured_no()==3?"TT- II : ":mq.get(i).getMste_insured_no()==3?"TT- III : ":""))+
			(Common.isEmpty(mq.get(i).getMsadd_life_ins_desc())?"":mq.get(i).getMsadd_life_ins_desc()), 38, 407-y, 0);
			z+=43;
			y+=10;
			}
		over.endText();
		
			stamp.close();	
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
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
	        
			return null;
		}
		
		//MANTA - SPAJ ONLINE UNTUK PRODUK SYARIAH
		public ModelAndView espajonlinesyariah(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String reg_spaj = request.getParameter("spaj");
			Date sysdate = elionsManager.selectSysdate();
			List<String> pdfs = new ArrayList<String>();
			boolean suksesMerge = false;
			String spaj = "";
			String konfirmasi = "";
			//String noE ="";
			
			String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
			
			String dir = props.getProperty("pdf.template.espajonlinesyariah");
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
	        String espajFile = dir + "\\espaj_"+reg_spaj+".pdf";
	        
	        HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk. SYARIAH");
			moreInfo.put("Title", "SYARIAH");
			moreInfo.put("Subject", "E-SPAJ SYARIAH ONLINE");
			
			PdfContentByte over;
			BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
	        PdfReader reader = new PdfReader(props.getProperty("pdf.template.espajonlinesyariah")+"\\spajonlinesyariah.pdf");
	        /* String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espajONLINE_"+reg_spaj+".pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));*/
	        OutputStream output = new FileOutputStream(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espajONLINESYARIAH_"+reg_spaj+".pdf");
	        /* PdfReader reader2 = new PdfReader(props.getProperty("pdf.template.ekonfirmasiagen")+"\\konfirmasiagency.pdf");
	        String outputName2 = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"surat_konfirmasiagency_"+reg_spaj+".pdf";
	        PdfStamper stamp2 = new PdfStamper(reader,new FileOutputStream(outputName));*/
	        
	        spaj = dir + "\\spajonlinesyariah.pdf";
	        konfirmasi=dir +"\\konfirmasisyariah.pdf";
	        pdfs.add(spaj);
	        pdfs.add(konfirmasi);
	        suksesMerge = MergePDF.concatPDFs(pdfs, output, false);	
	        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espajONLINESYARIAH_"+reg_spaj+".pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
	        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
	        // String noE =uwManager.noTest(dataPP.getLca_id());
	        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
	        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
	        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
	        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
	        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
	        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);
	        List detInv = this.uwManager.selectdetilinvestasisyariah(reg_spaj);
	        List benef = this.elionsManager.select_benef(reg_spaj);
	        List medQuest=this.uwManager.selectquestionareDMTM(reg_spaj);
	        List peserta=this.uwManager.select_all_mst_peserta(reg_spaj);
	        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
	        List<MedQuest> mq = uwManager.selectMedQuest(reg_spaj,null);
	        Agen agen =this.elionsManager.select_detilagen(reg_spaj);
	        List namaBank =uwManager.namaBank(accRecur.getLbn_id());
	        dataUsulan.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
	        Integer data2 = uwManager.selectFlagQuestionare(reg_spaj);
			String noE =uwManager.spajOnlineSyariah(reg_spaj, dataPP.getLca_id());
	        
			if(data2==0){
				ServletOutputStream sos = response.getOutputStream();
    			sos.println("<script>alert('Silakan Mengisi Questionare Terlebih Dahulu');window.close();</script>");
    			sos.close();
			}
	        stamp.setMoreInfo(moreInfo);
			
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 415, 663, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, noE.toUpperCase(), 84, 662, 0);
		    
		    over.setFontAndSize(times_new_roman,6);
		    //Data PP
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 127, 621, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_mother().toUpperCase(), 127, 602, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLside_name(), 127, 580, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_no_identity(), 127, 566, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsne_note().toUpperCase(), 127, 550, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_place_birth().toUpperCase(), 127, 532, 0);
		    //over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(dataPP.getMspe_date_birth()), 165, 531, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getDay2(dataPP.getMspe_date_birth()), 238, 533, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getMonth(dataPP.getMspe_date_birth()), 273, 533, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getYearFourDigit(dataPP.getMspe_date_birth()), 308, 533, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sex2().toUpperCase(), 127, 518, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataPP.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataPP.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 127, 504, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsag_name().toUpperCase(), 127, 415, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsed_name().toUpperCase(), 127, 401, 0);
		    
		    //data untuk suami/istri &anak2
		    if(!Common.isEmpty(dataPP.getNama_si())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_si())?"":dataPP.getNama_si().toUpperCase(), 185, 477, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_si())?"":FormatDate.toString(dataPP.getTgllhr_si()), 287, 477, 0);
			}
		    if(!Common.isEmpty(dataPP.getNama_anak1())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_anak1())?"":dataPP.getNama_anak1().toUpperCase(), 185, 462, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak1())?"":FormatDate.toString(dataPP.getTgllhr_anak1()), 287, 462, 0);
			}
			if(!Common.isEmpty(dataPP.getNama_anak2())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_anak2())?"":dataPP.getNama_anak2().toUpperCase(), 185, 447, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak2())?"":FormatDate.toString(dataPP.getTgllhr_anak2()), 287, 447, 0);
			}
			if(!Common.isEmpty(dataPP.getNama_anak3())){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getNama_anak3())?"":dataPP.getNama_anak3().toUpperCase(), 185, 432, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getTgllhr_anak3())?"":FormatDate.toString(dataPP.getTgllhr_anak3()), 287, 432, 0);
			}
		    
		    int monyong = 0;
        	String[] alamat = StringUtil.pecahParagraf(dataPP.getAlamat_rumah().toUpperCase(), 47);
        	for(int i=0; i<alamat.length; i++) {
        		monyong = 12 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							128, 385-monyong, 0);
        	}
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getKd_pos_rumah())?"":dataPP.getKd_pos_rumah()), 163, 360, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getArea_code_rumah())?"":dataPP.getArea_code_rumah()), 247, 359, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getTelpon_rumah())?"":dataPP.getTelpon_rumah()), 275, 359, 0);
        	
        	monyong = 0;
 			if(!Common.isEmpty(dataPP.getAlamat_kantor())){
 				String[] alamat_kantor =  StringUtil.pecahParagraf(dataPP.getAlamat_kantor().toUpperCase(), 47);
 	        	for(int i=0; i<alamat_kantor.length; i++) {
 	        		monyong = 12 * i;
 	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							127, 342-monyong, 0);
 	        	}
 			}
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getKd_pos_kantor())?"":dataPP.getKd_pos_kantor()), 163, 316, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getArea_code_kantor())?"":dataPP.getArea_code_kantor()), 251, 315, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataPP.getTelpon_kantor())?"":dataPP.getTelpon_kantor()), 277, 315, 0);
        	
        	monyong = 0;
        	String[] alamat_billing = StringUtil.pecahParagraf(addrBill.getMsap_address(), 47);
        	for(int i=0; i<alamat_billing.length; i++) {
        		monyong = 12 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							127, 298-monyong, 0);
        	}
        	
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_zip_code())?"":addrBill.getMsap_zip_code()) , 163, 271, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_area_code1())?"":addrBill.getMsap_area_code1()) , 249, 271, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_phone1())?"":addrBill.getMsap_phone1()) , 277, 271, 0);
    		
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getNo_hp())?"":dataPP.getNo_hp().substring(0, 4)) , 136, 230, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getNo_hp())?"":dataPP.getNo_hp().substring(4)) , 162, 230, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getEmail())?"":dataPP.getEmail()) , 127, 216, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_penghasilan().toUpperCase(), 127, 204, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_smbr_penghasilan(), 127, 187, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_tujuan().toUpperCase(), 127, 174, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_pendanaan().toUpperCase(), 127, 154, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_kerja().toUpperCase(), 127, 135, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_industri().toUpperCase(), 127, 115, 0);
    		over.setFontAndSize(times_new_roman,8);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getLsre_relation().toUpperCase(), 282, 88, 0);
    		
    		//if(dataPP.getLsre_id()!=1){
    		over.setFontAndSize(times_new_roman,6);
        	//Data tertanggung
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first().toUpperCase(), 352, 620, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_mother().toUpperCase(), 352, 601, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLside_name(), 352, 579, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_no_identity(), 352, 565, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsne_note().toUpperCase(), 352, 549, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_place_birth().toUpperCase(), 352, 531, 0);
    		//over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(dataPP.getMspe_date_birth()), 165, 531, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getDay2(dataTT.getMspe_date_birth()), 464, 532, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getMonth(dataTT.getMspe_date_birth()), 499, 532, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getYearFourDigit(dataTT.getMspe_date_birth()), 534, 532, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sex2().toUpperCase(), 352, 517, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataTT.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataTT.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 352, 503, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsag_name().toUpperCase(), 352, 415, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsed_name().toUpperCase(), 352, 401, 0);
    		//data untuk suami/istri &anak2
    		if(!Common.isEmpty(dataPP.getNama_si())){
    			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_si())?"":dataTT.getNama_si().toUpperCase(), 410, 477, 0);
    			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_si())?"":FormatDate.toString(dataTT.getTgllhr_si()), 513, 477, 0);
    		}
    		if(!Common.isEmpty(dataPP.getNama_anak1())){
    			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_anak1())?"":dataTT.getNama_anak1().toUpperCase(), 410, 462, 0);
    			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak1())?"":FormatDate.toString(dataTT.getTgllhr_anak1()), 513, 462, 0);
    		}
    		if(!Common.isEmpty(dataPP.getNama_anak2())){
    			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_anak2())?"":dataTT.getNama_anak2().toUpperCase(), 410, 447, 0);
    			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak2())?"":FormatDate.toString(dataTT.getTgllhr_anak2()), 513, 447, 0);
    		}
    		if(!Common.isEmpty(dataPP.getNama_anak3())){
    			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getNama_anak3())?"":dataTT.getNama_anak3().toUpperCase(), 410, 432, 0);
    			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataTT.getTgllhr_anak3())?"":FormatDate.toString(dataTT.getTgllhr_anak3()), 513, 432, 0);
    		}
    			
    		monyong = 0;
    	    String[] alamat2 = StringUtil.pecahParagraf(dataTT.getAlamat_rumah().toUpperCase(), 47);
    	    for(int i=0; i<alamat.length; i++) {
    	    	monyong = 12 * i;
    	    	over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							352, 385-monyong, 0);
    	    }
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getKd_pos_rumah())?"":dataTT.getKd_pos_rumah()), 389, 360, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getArea_code_rumah())?"":dataTT.getArea_code_rumah()), 473, 359, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getTelpon_rumah())?"":dataTT.getTelpon_rumah()), 500, 359, 0);
    	        	
    	    monyong = 0;
    	 	if(!Common.isEmpty(dataPP.getAlamat_kantor())){
    	 		String[] alamat_kantor =  StringUtil.pecahParagraf(dataPP.getAlamat_kantor().toUpperCase(), 47);
    	 	    for(int i=0; i<alamat_kantor.length; i++) {
    	 	        monyong = 12 * i;
    	 	        over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							352, 342-monyong, 0);
    	 	    }
    	 	}
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getKd_pos_kantor())?"":dataTT.getKd_pos_kantor()), 390, 315, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getArea_code_kantor())?"":dataTT.getArea_code_kantor()), 477, 315, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(dataTT.getTelpon_kantor())?"":dataTT.getTelpon_kantor()), 502, 315, 0);
    	        	
    	    monyong = 0;
    	    String[] alamat_billing2 = StringUtil.pecahParagraf(addrBill.getMsap_address(), 47);
    	    for(int i=0; i<alamat_billing.length; i++) {
    	        monyong = 12 * i;
    	        over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							352, 298-monyong, 0);
    	    }
    	        	
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_zip_code())?"":addrBill.getMsap_zip_code()) , 389, 271, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_area_code1())?"":addrBill.getMsap_area_code1()) , 474, 271, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( addrBill.getMsap_phone1())?"":addrBill.getMsap_phone1()) , 500, 271, 0);
    	    	
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getNo_hp())?"":dataTT.getNo_hp().substring(0, 4)) , 362, 230, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getNo_hp())?"":dataTT.getNo_hp().substring(4)) , 387, 230, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getEmail())?"":dataTT.getEmail()) , 352, 216, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_penghasilan().toUpperCase(), 352, 204, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_smbr_penghasilan(), 352, 187, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_tujuan().toUpperCase(), 352, 174, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_pendanaan().toUpperCase(), 352, 154, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_kerja().toUpperCase(), 352, 135, 0);
    	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_industri().toUpperCase(), 352, 115, 0);
        	    		
    		over.setFontAndSize(times_new_roman,6);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getLsbp_nama().toUpperCase(), 90, 62, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_cabang().toUpperCase(), 90, 49, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_kota().toUpperCase(), 90, 37, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_no_ac().toUpperCase(), 320, 62, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_nama().toUpperCase(), 320, 49, 0);
   
		    over.endText();
		   
		    //Halaman Ke 2
		    over = stamp.getOverContent(2);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, noE.toUpperCase(), 504, 817, 0);
			//Data Peserta Tambahan
			over.setFontAndSize(times_new_roman,6);
			if(peserta.size()>0){
				int vertikal=775;
				for(int z=0;z<peserta.size();z++){
					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(z);
					if(!pesertaPlus.getFlag_jenis_peserta().equals(0)){
						vertikal = vertikal-20;
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,pesertaPlus.getNama(),44, vertikal, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,(pesertaPlus.getKelamin()==1?"L":"P"),470, vertikal, 0);
						over.showTextAligned(PdfContentByte.ALIGN_CENTER,FormatDate.toString(pesertaPlus.getTgl_lahir()),535, vertikal, 0);
						over.showTextAligned(PdfContentByte.ALIGN_CENTER,pesertaPlus.getLsre_relation().toUpperCase(),280, vertikal, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,"-",385, vertikal, 0);
					}
				}
			}
			
			//product utama
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLsdbs_name(),100, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMspr_ins_period().toString(),245, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMspo_pay_period().toString(),304, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())),415, 627, 0);
			//over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())),509, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(inv.getTotal_premi_sementara())),509, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"-",538, 627, 0);
			Double Premirider = 0.;
			if(dataUsulan.getDaftaRider().size()>0){
				int vertikal2 = 623;
				for(int z=0;z<dataUsulan.getDaftaRider().size();z++){
				Datarider rider = (Datarider) dataUsulan.getDaftaRider().get(z);
				vertikal2 = vertikal2-12;
				monyong = 0;
		        	String[] produkRider = StringUtil.pecahParagraf(rider.getLsdbs_name(), 37);
		        	for(int i=0; i<produkRider.length; i++) {
		        		over.setFontAndSize(times_new_roman,5);
		        		monyong = 6 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, produkRider[i],							100, vertikal2-monyong, 0);
		        	}
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT,rider.getLsdbs_name(),115, vertikal2, 0);
		        over.setFontAndSize(times_new_roman,6);
		        over.showTextAligned(PdfContentByte.ALIGN_LEFT,rider.getMspr_ins_period().toString(),245, vertikal2, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,"",292, vertikal2, 0);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(rider.getMspr_tsi())),415, vertikal2, 0);
				if(rider.getMspr_premium()==0){
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,"-",510, vertikal2, 0);
				}else{
					over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(rider.getMspr_premium())),509, vertikal2, 0);
				}
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatNumber.convertToTwoDigit(new BigDecimal(rider.getMspr_premium())),455, vertikal2, 0);
				Premirider = rider.getMspr_premium();
				}
			}
			if(dataUsulan.getLku_id().equals("01")){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "*Tunggal", 165, 255, 0);}
			else{
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "*Berkala", 165, 255, 0);}
			//BigDecimal totalPremiProduct = new BigDecimal(dataUsulan.getMspr_premium() + Premirider);
			BigDecimal totalPremiProduct = new BigDecimal(inv.getTotal_premi_sementara() + Premirider);
			over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(totalPremiProduct),509, 543, 0);
			
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLscb_pay_mode(),175, 520, 0);
			over.setFontAndSize(times_new_roman,8);
			//over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),140, 489, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),420, 490, 0);
			over.setFontAndSize(times_new_roman,7);
			DetilInvestasi detinv0 = (DetilInvestasi) detInv.get(0);
			DetilInvestasi detinv1 = (DetilInvestasi) detInv.get(1);
			DetilInvestasi detinv2 = (DetilInvestasi) detInv.get(2);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Fix Income :"+ (detinv0.getMdu_persen1()==null?Integer.toString(0):detinv0.getMdu_persen1()) + " %",40, 188, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Dynamic :"+ (detinv1.getMdu_persen1()==null?Integer.toString(0):detinv1.getMdu_persen1()) + " %",100, 188, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Aggressive :"+ (detinv2.getMdu_persen1()==null?Integer.toString(0):detinv2.getMdu_persen1()) + " %",160, 188, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, (dataUsulan.getMste_flag_investasi()==1?"Setelah permintaan asuransi disetujui oleh Bagian Underwriting dan Calon Pemegang Polis telah setuju serta membayar ekstra premi (jika ada)." 
			: "Langsung setelah dana diterima di rekening yang ditentukan oleh PT Asuransi Jiwa Sinarmas MSIG Tbk. dan SPAJ telah diterima di bagian Underwriting."), 40, 163, 0);
			//rekening pembayaran polis autodebet tabungan
			if (rekClient!=null && dataUsulan.getMste_flag_cc()==2){
				over.setFontAndSize(times_new_roman,6);
	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(rekClient.getLsbp_nama())?"":rekClient.getLsbp_nama().toUpperCase(), 100, 449, 0);
	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(rekClient.getMrc_cabang())?"":rekClient.getMrc_cabang().toUpperCase(), 100, 433, 0);
	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(rekClient.getMrc_kota())?"":rekClient.getMrc_kota().toUpperCase(), 100, 418, 0);
	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(rekClient.getMrc_no_ac())?"":rekClient.getMrc_no_ac().toUpperCase(), 100, 402, 0);
	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(rekClient.getMrc_nama())?"":rekClient.getMrc_nama().toUpperCase(), 100, 387, 0);
			}
    		//rekening pembayaran polis autodebet kartu kredit
			if (dataTT.getMste_flag_cc()==1){
	    		if (accRecur!=null){
	    			String bank_pusat = "";
	    			String bank_cabang= "";
	    			
	    			if(!namaBank.isEmpty()){
	        			HashMap m = (HashMap)namaBank.get(0);
	        			bank_pusat = (String)m.get("LSBP_NAMA");
	        			bank_cabang = (String)m.get("LBN_NAMA");
	    			}
	    			
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(bank_pusat)?"":bank_pusat.toUpperCase()+" - "+bank_cabang.toUpperCase(), 393, 450, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_acc_no())?"":accRecur.getMar_acc_no().toUpperCase()/*accRecur.getLbn_nama().toUpperCase()*/, 393, 435, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_holder())?"":accRecur.getMar_holder().toUpperCase()/*accRecur.getLbn_nama().toUpperCase()*/, 393, 420, 0);
				//over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getLbn_nama())?"getLbn_nama":accRecur.getLbn_nama().toUpperCase()/*accRecur.getLbn_nama().toUpperCase()*/, 393, 384, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_expired())?"":FormatDate.toIndonesian(accRecur.getMar_expired())/*accRecur.getLbn_nama().toUpperCase()*/, 393, 385, 0);
	    		/*over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_acc_no().toUpperCase(), 393, 404, 0);
	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_holder().toUpperCase(), 393, 388, 0);
	    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_jenis().toString(), 393, 376, 0);*/
	    		//over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_expired().parse(s), 393, 354, 0);
	    		}
			}
			
			//data manfaat yang ditunjuk
    		if(benef.size()>0){
				int j=0;
				for(int i=0;i<benef.size();i++){
					Benefeciary benefi = (Benefeciary) benef.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_first(),44, 94-j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(benefi.getMsaw_sex()==1?"L":"P"),315, 94-j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getSmsaw_birth(),385, 94-j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getLsre_relation().toUpperCase(),470, 94-j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_persen().toString(),539, 94-j, 0);
					j+=19;
				}
    		}
 			if(inv!=null){
				Double premiTambahan = 0.;
				DetilTopUp daftarTopup = inv.getDaftartopup();
				Double topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala())?new Double(0):daftarTopup.getPremi_berkala();
				Double topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal())?new Double(0):daftarTopup.getPremi_tunggal();
				Double totalTopup = topUpBerkala + topUpTunggal;
				BigDecimal totalPremiPokok = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup));
				BigDecimal totalPremi = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup) + premiTambahan);
				Double ratePremiPokok = (inv.getMu_jlh_premi()/ totalPremiPokok.doubleValue()) * 100;
				Double ratePremiTopup = ((Common.isEmpty(inv.getMu_jlh_tu())?0.:inv.getMu_jlh_tu()) / totalPremi.doubleValue()) * 100;
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(inv.getMu_jlh_premi())),305, 273, 0);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,ratePremiPokok.intValue() +"%",465, 273, 0);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,(Common.isEmpty(totalTopup)?"-": FormatNumber.convertToTwoDigit(new BigDecimal(totalTopup))),305, 252, 0);
				/* over.showTextAligned(PdfContentByte.ALIGN_RIGHT,"",305, 252, 0);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,"",305, 252, 0);*/
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,ratePremiTopup.intValue()+"%",465, 252, 0);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(totalPremi  ) ,305, 235, 0);
				/* over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_tunggal())?"":(daftarTopup.getId_tunggal()==2?"X":""),128, 255, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_berkala())?"":(daftarTopup.getId_berkala()==5?"X":""),176, 255, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_tunggal())?"":(daftarTopup.getId_tunggal()==2?"X":""),183, 601, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(daftarTopup.getId_berkala())?"":(daftarTopup.getId_berkala()==5?"X":""),222, 601, 0);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,(Common.isEmpty(totalTopup)?"-": FormatNumber.convertToTwoDigit(new BigDecimal(totalTopup))),370, 605, 0);
				over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(totalPremi  ) ,370, 672, 0);*/
				/* over.showTextAligned(PdfContentByte.ALIGN_LEFT,ratePremiPokok.intValue() + "% : " + ratePremiTopup.intValue() + "%",300, 659, 0);*/
			}
			
			over.endText();
			 
			over = stamp.getOverContent(3);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, noE.toUpperCase(), 504, 817, 0);
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "/kg", 85, 769, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "/cm", 86, 748, 0);
			
			int x=0;
			int y=0;
			int y1=0;
			for(int i=0;i<mq.size();i++){
				Integer kelamin = null;
				if(mq.get(i).getMste_insured_no()==0){
					kelamin = dataPP.getMspe_sex();
				}else if (mq.get(i).getMste_insured_no()==1){
					kelamin = dataTT.getMspe_sex();
				}
				
			// Questionare SPAJ ONLINE SYARIAH
				//Berat dan Tinggi, 1a
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_berat().toString(), 370+x, 769, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_tinggi().toString(), 370+x, 748, 0);
				//Pertanyaan 1b
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_berat_berubah()==1?"YA" : "TIDAK"), 370+x, 727, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadm_berubah_desc())?"":mq.get(i).getMsadm_berubah_desc()), 45, 715-y, 0);
				//Pertanyaan 2
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,(mq.get(i).getMsadm_sehat()==1?"YA" : "TIDAK"),380+x, 652, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadm_sehat_desc())?"":mq.get(i).getMsadm_sehat_desc()), 35, 647-y, 0);
				//Pertanyaan 3
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_penyakit()==1?"YA" : "TIDAK"), 370+x, 588, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadm_penyakit_desc())?"":mq.get(i).getMsadm_penyakit_desc()), 35, 550-y, 0);
				//pertanyaan 4a
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_medis()==1?"YA" : "TIDAK"), 370+x, 476, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadm_medis_desc())?"":mq.get(i).getMsadm_medis_desc()), 45, 450-y, 0);
				//pertanyaan 4b
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_medis_alt()==1?"YA" : "TIDAK"), 370+x, 390, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadm_medis_alt_desc())?"":mq.get(i).getMsadm_medis_alt_desc()), 45, 370-y, 0);
				//Pertanyaan 5
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_family_sick()==1?"YA" : "TIDAK"), 370+x, 308, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadm_family_sick_desc())?"":mq.get(i).getMsadm_family_sick_desc()), 35, 270-y, 0);
				//Pertanyaan 6 Khusus Wanita
				if(kelamin==0){
					over.setFontAndSize(times_new_roman,7);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMsadm_pregnant())?"TIDAK":(mq.get(i).getMsadm_pregnant()==1?"YA" : "TIDAK")),370+x, 218, 0);
					over.setFontAndSize(times_new_roman,6);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
					(Common.isEmpty(mq.get(i).getMsadm_pregnant_desc())?"":mq.get(i).getMsadm_pregnant_desc()), 45, 208-y1, 0);
					over.setFontAndSize(times_new_roman,7);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMsadm_abortion())?"TIDAK":(mq.get(i).getMsadm_abortion()==1?"YA" : "TIDAK")),370+x, 174, 0);
					over.setFontAndSize(times_new_roman,6);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
					(Common.isEmpty(mq.get(i).getMsadm_abortion_desc())?"":mq.get(i).getMsadm_abortion_desc())+(Common.isEmpty(mq.get(i).getMsadm_pregnant_time())?"":mq.get(i).getMsadm_pregnant_time().toString()), 45, 152-y1, 0);
					over.setFontAndSize(times_new_roman,7);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMsadm_usg())?"TIDAK":(mq.get(i).getMsadm_usg()==1?"YA" : "TIDAK")),370+x, 94, 0);
					over.setFontAndSize(times_new_roman,6);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
					(Common.isEmpty(mq.get(i).getMsadm_usg_desc())?"":mq.get(i).getMsadm_usg_desc()),45, 78-y1, 0);
					y1+=6;
				}
				x+=43;
				y+=10;
			}
			over.endText();
			over = stamp.getOverContent(4);
			over.beginText();
			over.setFontAndSize(times_new_roman,8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, noE.toUpperCase(), 504, 817, 0);
			over.setFontAndSize(times_new_roman,7);
			int z=0;
			for(int i=0;i<mq.size();i++){
				// Questionare SPAJ ONLINE
				// PERTANYAAN PRIBADI 
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_hobby())?"":(Integer.parseInt(mq.get(i).getMsadd_hobby())==1?"YA" : "TIDAK"), 380+z, 769, 0);
				//Pertanyaan 2
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_flight())?"":(mq.get(i).getMsadd_flight()==1?"YA" : "TIDAK"), 380+z, 691, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadd_desc_flight())?"":mq.get(i).getMsadd_desc_flight()), 35, 700-y, 0);
				//Pertanyaan 3a
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_smoke())?"":(mq.get(i).getMsadd_smoke()==1?"YA" : "TIDAK"), 380+z, 622, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,  (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
				(Common.isEmpty(mq.get(i).getNsadd_many_cig())?"":mq.get(i).getNsadd_many_cig()), 45, 630-y, 0);
				//Pertanyaan 3b
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_drink_beer())?"":(mq.get(i).getMsadd_drink_beer()==1?"YA" : "TIDAK"), 380+z, 553, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,  (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadd_drink_beer_desc())?"":mq.get(i).getMsadd_drink_beer_desc()), 45, 565-y, 0);
				//Pertanyaan 3c
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER, Common.isEmpty(mq.get(i).getMsadd_drugs())?"":(mq.get(i).getMsadd_drugs()==1?"YA" : "TIDAK"), 380+z, 488, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,  (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadd_reason_drugs())?"":mq.get(i).getMsadd_reason_drugs()), 45, 500-y, 0);
				//Pertanyaan 4b
				over.setFontAndSize(times_new_roman,7);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(mq.get(i).getMsadd_life_ins())?"":(mq.get(i).getMsadd_life_ins()==1?"YA" : "TIDAK"), 370+z, 418, 0);
				over.setFontAndSize(times_new_roman,6);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,  (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
				(Common.isEmpty(mq.get(i).getMsadd_life_ins_desc())?"":mq.get(i).getMsadd_life_ins_desc()), 35, 405-y, 0);
				z+=43;
				y+=10;
			}
			over.endText();
		
			stamp.close();	
			
			File l_file = new File(outputName);
			
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
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
	        
			return null;
		}
		
		public ModelAndView eAgency(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String reg_spaj = request.getParameter("spaj");
			Date sysdate = elionsManager.selectSysdate();
			
			String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
			
			String dir = props.getProperty("pdf.template.ekonfirmasiagen");
			
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
	        String espajFile = dir + "\\surat_konfirmasiagency_"+reg_spaj+".pdf";
	        
	        HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "DMTM");
			moreInfo.put("Subject", "SURAT KONFIRMASI E-AGENCY");
			
			PdfContentByte over;
			BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
	        PdfReader reader = new PdfReader(props.getProperty("pdf.template.ekonfirmasiagen")+"\\konfirmasiagency.pdf");
	        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"surat_konfirmasiagency_"+reg_spaj+".pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
	        
	        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
	       // String noEndors =uwManager.noTest(dataPP.getLca_id());
	        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
	        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
	        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
	        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
	        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
	        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);
	        List detInv = this.uwManager.selectdetilinvestasimallspaj(reg_spaj);
	        List benef = this.elionsManager.select_benef(reg_spaj);
	        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
	        List<MedQuest> mq = uwManager.selectMedQuest(reg_spaj,null);
	        Agen agen =this.uwManager.select_detilagenWIthLv(reg_spaj);
	        List peserta=this.uwManager.select_all_mst_peserta(reg_spaj);
	       /* if (agen.getLeader()==null){
	        	agen=this.elionsManager.select_detilagen(reg_spaj);
	        }*/
	        dataUsulan.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
	        
	        over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman,9);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 359, 626, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspo_no_blanko(), 368, 638, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(sysdate), 104, 626, 0);
		    
		    over.setFontAndSize(times_new_roman,6);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 205, 550, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first().toUpperCase(), 205, 539, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLsdbs_name().toUpperCase(), 205, 528, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 205, 515, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())), 260, 515, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 205,504, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())), 260, 504, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMcl_first(),205, 459, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMsag_id(),205, 446, 0);
		    Double premiTambahan = 0.;
			Double premiExtra = (Common.isEmpty(uwManager.selectSumPremiExtra(reg_spaj))? 0.:uwManager.selectSumPremiExtra(reg_spaj));
			Double premiRider = (Common.isEmpty(uwManager.selectSumPremiRider(reg_spaj))? 0.:uwManager.selectSumPremiRider(reg_spaj));
			premiTambahan = premiTambahan + premiExtra + premiRider;
		   if(inv!=null){
				DetilTopUp daftarTopup = inv.getDaftartopup();
				Double topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala())?new Double(0):daftarTopup.getPremi_berkala();
				Double topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal())?new Double(0):daftarTopup.getPremi_tunggal();
				Double totalTopup = topUpBerkala + topUpTunggal;
				BigDecimal totalPremiPokok = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup));
				BigDecimal totalPremi = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup) + premiTambahan);
				
				}
		    over.endText();
		    
		    over = stamp.getOverContent(2);
			over.beginText();
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,Common.isEmpty(dataPP.getMspo_jenis_terbit())?"":(dataPP.getMspo_jenis_terbit()==0?"SOFTCOPY":"HARDCOPY"),242, 690, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(agen.getMcl_first())?"":agen.getMcl_first()),116, 131, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(agen.getLsle_name())?"":agen.getLsle_name().toUpperCase()),116, 124, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(agen.getMsag_id())?"":agen.getMsag_id()),116, 116, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(agen.getMst_leader())?"":agen.getMst_leader()),272, 125, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(agen.getLeader())?"":agen.getLeader()),278, 132, 0);
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 271, 214, 0);
			//over.showTextAligned(PdfContentByte.ALIGN_LEFT, "XxxxXX", 155, 349, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 281, 612, 0);
			if(peserta.size()>0){
				int vertikal=569;
				for(int z=0;z<peserta.size();z++){
				PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(z);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,pesertaPlus.getNama(),281, vertikal, 0);
				vertikal = vertikal-43;
				}
			}
		    over.endText();
			    
			stamp.close();	
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
				logger.error("ERROR :", e);
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
	        
			return null;
		}
		//Ryan - Konfirmasi Syariah
		public ModelAndView konfirmSyrh(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String reg_spaj = request.getParameter("spaj");
			Date sysdate = elionsManager.selectSysdate();
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			
			String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
			Integer lewat =bacManager.selectCekKonfirmasiSyariah(reg_spaj);
			
			String dir = props.getProperty("pdf.template.confirmsyariah");
			
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
	        String espajFile = dir + "\\surat_konfirmasiBSIM_"+reg_spaj+".pdf";
	        
	        HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "BSIM");
			moreInfo.put("Subject", "SURAT KONFIRMASI PIHAK BSIM");
			
			PdfContentByte over;
			BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
	        PdfReader reader = new PdfReader(props.getProperty("pdf.template.confirmsyariah")+"\\konfirmasisyariah.pdf");
	        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"konfirmasisyariah_"+reg_spaj+".pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
	        
	        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
	       // String noEndors =uwManager.noTest(dataPP.getLca_id());
	        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
	        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
	        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
	        Powersave powersave =this.elionsManager.select_powersaver(reg_spaj);
	        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
	        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
	        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);
	        List detInv = this.uwManager.selectdetilinvestasimallspaj(reg_spaj);
	        List benef = this.elionsManager.select_benef(reg_spaj);
	        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
	        
	        over = stamp.getOverContent(1);
			over.beginText();
		 //   over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 359, 626, 0);
		  //  over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(sysdate), 104, 626, 0);
		    
		    over.setFontAndSize(times_new_roman,8);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 195, 515, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorPolis(dataPP.getMspo_policy_no()), 195, 489, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLsdbs_name().toUpperCase(), 195, 462, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())), 195, 438, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, powersave.getMps_jangka_inv(), 195, 413, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(powersave.getMps_deposit_date()), 195, 387, 0);
		 //   over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLsdbs_name().toUpperCase(), 205, 528, 0);
		//    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 205, 515, 0);
		 //   over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())), 260, 515, 0);
		 //   over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 205,504, 0);
		 //   over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())), 260, 504, 0);
		 //   over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMcl_first(),205, 459, 0);
		//	over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMsag_id(),205, 446, 0);
		    over.endText();
			    
			stamp.close();	
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;			
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
				logger.error("ERROR :", e);
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
				
				if (lewat < 1){
					elionsManager.insertMstPositionSpaj(currentUser.getLus_id(),"CETAK KONFIRMASI PIHAK SYARIAH",reg_spaj, 0);
				}
				
			return null;
		}
		
		//MANTA - SURAT KONFIRMASI ONLINE UNTUK PRODUK SYARIAH
		public ModelAndView ekonfirmasisyariah(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String reg_spaj = request.getParameter("spaj");
			Date sysdate = elionsManager.selectSysdate();
			
			String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
			
			String dir = props.getProperty("pdf.template.ekonfirmasisyariah");
			
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
	        String espajFile = dir + "\\surat_konfirmasisyariah_"+reg_spaj+".pdf";
	        
	        HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk. SYARIAH");
			moreInfo.put("Title", "SYARIAH");
			moreInfo.put("Subject", "SURAT KONFIRMASI SYARIAH");
			
			PdfContentByte over;
			BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
	        PdfReader reader = new PdfReader(props.getProperty("pdf.template.ekonfirmasisyariah")+"\\konfirmasisyariah.pdf");
	        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"surat_konfirmasisyariah_"+reg_spaj+".pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
	        
	        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
	       // String noEndors =uwManager.noTest(dataPP.getLca_id());
	        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
	        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
	        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
	        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
	        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
	        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);
	        List detInv = this.uwManager.selectdetilinvestasisyariah(reg_spaj);
	        List benef = this.elionsManager.select_benef(reg_spaj);
	        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
	        List<MedQuest> mq = uwManager.selectMedQuest(reg_spaj,null);
	        Agen agen =this.uwManager.select_detilagenWIthLv(reg_spaj);
	        List peserta=this.uwManager.select_all_mst_peserta(reg_spaj);
	       /* if (agen.getLeader()==null){
	        	agen=this.elionsManager.select_detilagen(reg_spaj);
	        }*/
	        dataUsulan.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
	        
	        over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman,9);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 359, 616, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspo_no_blanko(), 368, 628, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toString(sysdate), 104, 616, 0);
		    
		    over.setFontAndSize(times_new_roman,6);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 205, 518, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first().toUpperCase(), 205, 506, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLsdbs_name().toUpperCase(), 205, 494, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 205, 482, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())), 262, 482, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 205,470, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())), 262, 470, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMcl_first(),205, 424, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMsag_id(),205, 412, 0);
		    Double premiTambahan = 0.;
			Double premiExtra = (Common.isEmpty(uwManager.selectSumPremiExtra(reg_spaj))? 0.:uwManager.selectSumPremiExtra(reg_spaj));
			Double premiRider = (Common.isEmpty(uwManager.selectSumPremiRider(reg_spaj))? 0.:uwManager.selectSumPremiRider(reg_spaj));
			premiTambahan = premiTambahan + premiExtra + premiRider;
			if(inv!=null){
				DetilTopUp daftarTopup = inv.getDaftartopup();
				Double topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala())?new Double(0):daftarTopup.getPremi_berkala();
				Double topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal())?new Double(0):daftarTopup.getPremi_tunggal();
				Double totalTopup = topUpBerkala + topUpTunggal;
				BigDecimal totalPremiPokok = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup));
				BigDecimal totalPremi = new BigDecimal(inv.getMu_jlh_premi() + (totalTopup) + premiTambahan);
				if(topUpBerkala!=0 && topUpTunggal!=0){
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 205,458, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(topUpBerkala)), 262, 458, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, "(Berkala)", 265,458, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 205,470, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(topUpTunggal)), 262, 470, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, "(Tunggal)", 265,470, 0);
				}else if(topUpBerkala!=0){
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 205,458, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(topUpBerkala)), 262, 458, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, "(Berkala)", 265,458, 0);
				}else if(topUpTunggal!=0){
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataUsulan.getLku_symbol().toUpperCase(), 205,458, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(topUpTunggal)), 262, 458, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, "(Tunggal)", 265,458, 0);
				}else{
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, " - ", 205,458, 0);
				}
			}
		    over.endText();
		    
		    over = stamp.getOverContent(2);
			over.beginText();
			over.setFontAndSize(times_new_roman,7);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 271, 505, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 281, 442, 0);
			if(peserta.size()>0){
				int vertikal=399;
				for(int z=0;z<peserta.size();z++){
				PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(z);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,pesertaPlus.getNama(),281, vertikal, 0);
				vertikal = vertikal-43;
				}
			}
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 271, 150, 0);
			over.setFontAndSize(times_new_roman,6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(agen.getMcl_first())?"":agen.getMcl_first()),116, 68, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(agen.getLsle_name())?"":agen.getLsle_name().toUpperCase()),116, 60, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(agen.getMsag_id())?"":agen.getMsag_id()),116, 52, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(agen.getLeader())?"":agen.getLeader()),278, 68, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(agen.getMst_leader())?"":agen.getMst_leader()),278, 60, 0);
			
		    over.endText();
			    
			stamp.close();	
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
				logger.error("ERROR :", e);

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
	        
			return null;
		}
		
		// SPAJ ELEKTRONIK UNTUK DANA SEJAHTERA DMTM - RYan
		public ModelAndView espajdmtm2(HttpServletRequest request, HttpServletResponse response) throws Exception {
					String reg_spaj = request.getParameter("spaj");
					Date sysdate = elionsManager.selectSysdate();
					
					String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
					String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
					Integer data2 = uwManager.selectFlagQuestionare(reg_spaj);
					String dir = props.getProperty("pdf.template.espajdmtm2");
					File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
			        if(!userDir.exists()) {
			            userDir.mkdirs();
			        }
			        String espajFile = dir + "\\espaj_"+reg_spaj+".pdf";
			        
			        HashMap moreInfo = new HashMap();
					moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
					moreInfo.put("Title", "DMTM");
					moreInfo.put("Subject", "E-SPAJ DMTM DANA SEJAHTERA");
					
					PdfContentByte over;
					BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
					
			        PdfReader reader = new PdfReader(props.getProperty("pdf.template.espajdmtm2")+"\\espajdmtm2.pdf");
			        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espajDMTM2_"+reg_spaj+".pdf";
			        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
			        
			        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
			        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
			        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
			        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
			        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
			        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
			        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);//ada isinya
			        List detInv = this.uwManager.selectdetilinvestasimallspaj(reg_spaj);
			        List benef = this.elionsManager.select_benef(reg_spaj);
			        List medQuest=this.uwManager.selectquestionareDMTM(reg_spaj);
			        List peserta=this.uwManager.select_all_mst_peserta(reg_spaj);
			        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
			        List<MedQuest> mq = uwManager.selectMedQuest(reg_spaj,null);
			        Agen agen =this.elionsManager.select_detilagen(reg_spaj);
			        dataUsulan.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
			        List namaBank =uwManager.namaBank(accRecur.getLbn_id());
					Map premiProdukUtama = this.elionsManager.selectPremiProdukUtama(reg_spaj);
					String kurs = (String) premiProdukUtama.get("LKU_ID");
			        
			        if(!dataUsulan.getLca_id().equals("40")){
						ServletOutputStream sos = response.getOutputStream();
		    			sos.println("<script>alert('E-SPAJ DMTM , Hanya Untuk jalur distribusi DMTM');window.close();</script>");
		    			sos.close();
					}
			        
			        stamp.setMoreInfo(moreInfo);
					
					over = stamp.getOverContent(1);
					over.beginText();
					over.setFontAndSize(times_new_roman,7);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 90, 646, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getMspo_no_blanko())?"-":dataPP.getMspo_no_blanko().toUpperCase(), 400, 646, 0);
				    
				    over.setFontAndSize(times_new_roman,5);
				  //Data PP
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 160, 619, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_mother().toUpperCase(), 160, 606, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLside_name(), 160, 592, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_no_identity(), 246, 592, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getLsne_note())?"-":dataPP.getLsne_note().toUpperCase(), 160, 578, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_place_birth().toUpperCase() + ", " + FormatDate.toIndonesian(dataPP.getMspe_date_birth()), 160, 565, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sex2().toUpperCase(), 160, 551, 0);
				    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataPP.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataPP.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 160, 537, 0);
				   
				    int monyong = 0;
		        	String[] alamat = StringUtil.pecahParagraf(dataPP.getAlamat_rumah().toUpperCase(), 55);
		        	for(int i=0; i<alamat.length; i++) {
		        		monyong = 7 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							160, 527-monyong, 0);
		        	}
		        	
		        	monyong = 0;
		 			if(!Common.isEmpty(dataPP.getAlamat_kantor())){
		 				String[] alamat_kantor =  StringUtil.pecahParagraf(dataPP.getAlamat_kantor().toUpperCase(), 55);
		 	        	for(int i=0; i<alamat_kantor.length; i++) {
		 	        		monyong = 7 * i;
		 	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							160, 514-monyong, 0);
		 	        	}
		 			}
		        	
		        	monyong = 0;
		        	String[] alamat_billing = StringUtil.pecahParagraf(addrBill.getMsap_address(), 55);
		        	for(int i=0; i<alamat_billing.length; i++) {
		        		monyong = 7 * i;;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							160, 500-monyong, 0);
		        	}
		        	
		        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getNo_hp())?"":dataPP.getNo_hp()) , 160, 482, 0);
		        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getEmail())?"":dataPP.getEmail()) , 160, 468, 0);
		        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_penghasilan().toUpperCase(), 160, 455, 0);
		        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_pendanaan().toUpperCase(), 160, 441, 0);
		    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_kerja().toUpperCase(), 160, 427, 0);
		    		
		    		over.setFontAndSize(times_new_roman,6);
		    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getLsre_relation().toUpperCase(), 290, 414, 0);
					
		    	if(dataPP.getLsre_id()!=1){
		    			over.setFontAndSize(times_new_roman,5);
		        		//Data tertanggung
		    			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first().toUpperCase(), 350, 619, 0);
		    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_mother().toUpperCase(), 350, 606, 0);
		    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLside_name(), 350, 592, 0);
		    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_no_identity(), 436, 592, 0);
		    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsne_note().toUpperCase(), 350, 578, 0);
		    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_place_birth().toUpperCase() + ", " + FormatDate.toIndonesian(dataTT.getMspe_date_birth()), 350, 565, 0);
		    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sex2().toUpperCase(), 350, 551, 0);
		    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataTT.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataTT.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 350, 537, 0);
		    			 
		    		    monyong = 0;
		    	        	String[] alamat2 = StringUtil.pecahParagraf(dataTT.getAlamat_rumah().toUpperCase(), 55);
		    	        	for(int i=0; i<alamat.length; i++) {
		    	        		monyong = 7 * i;
		    	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							350, 527-monyong, 0);
		    	        	}
		    	        	monyong = 0;
		    	 			if(!Common.isEmpty(dataPP.getAlamat_kantor())){
		    	 				String[] alamat_kantor =  StringUtil.pecahParagraf(dataTT.getAlamat_rumah().toUpperCase()/*getAlamat_kantor().toUpperCase()*/, 55);
		    	 	        	for(int i=0; i<alamat_kantor.length; i++) {
		    	 	        		monyong = 7 * i;
		    	 	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							350, 514-monyong, 0);
		    	 	        	}
		    	 			}
		    	        	
		    	        	monyong = 0;
		    	        	String[] alamat_billing2 = StringUtil.pecahParagraf(addrBill.getMsap_address(), 55);
		    	        	for(int i=0; i<alamat_billing.length; i++) {
		    	        		monyong = 7 * i;
		    	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							350, 500-monyong, 0);
		    	        	}
		    	        	
		    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getNo_hp())?"":dataTT.getNo_hp()) , 350, 482, 0);
				        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getEmail())?"":dataTT.getEmail()) , 350, 468, 0);
				        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_penghasilan().toUpperCase(), 350, 455, 0);
				        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_pendanaan().toUpperCase(), 350, 441, 0);
				    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_kerja().toUpperCase(), 350, 427, 0);
		        	}
				    		
				    		over.setFontAndSize(times_new_roman,6);
				    	//	if (dataTT.getMste_flag_cc()==1){
					    if (accRecur!=null)
					    	{
					    			String bank_pusat = "";
					    			String bank_cabang= "";
					    			
					    			if(!namaBank.isEmpty()){
					        			HashMap m = (HashMap)namaBank.get(0);
					        			bank_pusat = (String)m.get("LSBP_NAMA");
					        			bank_cabang = (String)m.get("LBN_NAMA");
					    			}
					    			
								over.setFontAndSize(times_new_roman,5);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(bank_pusat)?"":bank_pusat.toUpperCase()+" - "+bank_cabang.toUpperCase(), 110, 392, 0);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(bank_cabang)?"":bank_cabang.toUpperCase()/*accRecur.getLbn_nama().toUpperCase()*/, 110, 381, 0);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_acc_no())?"":accRecur.getMar_acc_no().toUpperCase(), 396, 392, 0);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_holder())?"":accRecur.getMar_holder().toUpperCase(), 396, 381, 0);
					    		}
						//	}
					    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLsdbs_name(),155, 338, 0);
					    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())),379, 338, 0);
					    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())),520, 338, 0);//dataUsulan.getMspr_ins_period().toString()
					    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,dataUsulan.getMspr_ins_period().toString()+" Tahun",180, 324, 0);
					    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLscb_pay_mode(),162, 310, 0);
					    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),222, 299, 0);
					    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(dataPP.getMste_tgl_recur()), 162, 288, 0);

						//data manfaat yang ditunjuk
			    		if(benef.size()>0){
						int j=0;
						for(int i=0;i<benef.size();i++){
						Benefeciary benefi = (Benefeciary) benef.get(i);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_first(),86, 236-j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,(benefi.getMsaw_sex()==1?"L":"P"),325, 236-j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getSmsaw_birth(),375, 236-j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getLsre_relation().toUpperCase(),463, 236-j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_persen().toString(),532, 236-j, 0);
						j+=11;}
					}
			    		int x=0;
						int y=0;
						int y1=0;
			    	//Pertanyaan MEDIS 
			    		for(int i=0;i<mq.size();i++){
							Integer kelamin = null;
							if(mq.get(i).getMste_insured_no()==0){
								kelamin = dataPP.getMspe_sex();
							}else if (mq.get(i).getMste_insured_no()==1){
								kelamin = dataTT.getMspe_sex();
							}
							
						// Questionare SPAJ ONLINE SYARIAH
							//Berat dan Tinggi, 1a, 1b
							over.setFontAndSize(times_new_roman,6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_berat().toString() +" Kg", 447+x, 198, 0);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_tinggi().toString()+" Cm",447+x, 189, 0);
							//Pertanyaan 2
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_berat_berubah()==1?"YA" : "TIDAK"), 447+x, 180, 0);
							over.setFontAndSize(times_new_roman,5);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TT : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
							(Common.isEmpty(mq.get(i).getMsadm_berubah_desc())?"":mq.get(i).getMsadm_berubah_desc()), 76, 176-y, 0);
							//Pertanyaan 3
						    over.setFontAndSize(times_new_roman,6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,(mq.get(i).getMsadm_penyakit()==1?"YA" : "TIDAK"),447+x, 154, 0);
							over.setFontAndSize(times_new_roman,5);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
							(Common.isEmpty(mq.get(i).getMsadm_penyakit_desc())?"getMsadm_penyakit_desc":mq.get(i).getMsadm_penyakit_desc()), 76, 143-y, 0);
							//pertanyaan 4
							over.setFontAndSize(times_new_roman,6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_medis()==1?"YA" : "TIDAK"), 447+x, 122, 0);
							over.setFontAndSize(times_new_roman,5);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
							(Common.isEmpty(mq.get(i).getMsadm_medis_desc())?"getMsadm_medis_desc":mq.get(i).getMsadm_medis_desc()), 76, 97-y, 0);
							//Pertanyaan 5 Khusus Wanita
							if(kelamin==0){
								over.setFontAndSize(times_new_roman,6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMsadm_pregnant())?"TIDAK":(mq.get(i).getMsadm_pregnant()==1?"YA" : "TIDAK")),447+x, 76, 0);
								over.setFontAndSize(times_new_roman,5);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
								(Common.isEmpty(mq.get(i).getMsadm_pregnant_desc())?"":mq.get(i).getMsadm_pregnant_desc()), 76, 71-y1, 0);
								y1+=6;
							}
							x+=68;
							y+=9;
						}
			    		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMcl_first(),140, 47, 0);
			    		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMsag_id(),108, 47, 0);
				    over.endText();
				   
					stamp.close();	
					
					File l_file = new File(outputName);
					FileInputStream in = null;
					ServletOutputStream ouputStream = null;
					try{
						
						response.setContentType("application/pdf");
						response.setHeader("Content-Disposition", "Inline");
						response.setHeader("Expires", "0");
						response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
						response.setHeader("Pragma", "public");
						
						in = new FileInputStream(l_file);
					    ouputStream = response.getOutputStream();
					    
					    IOUtils.copy(in, ouputStream);
					}catch (Exception e) {
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
			        
					return null;
		}
		
		//randy - direct print e-spaj dmtm (req. timmy) - 07/09/2016
		public void espajdmtm3(String reg_spaj) throws Exception {
			
			Date sysdate = elionsManager.selectSysdate();

			String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
			Integer data2 = uwManager.selectFlagQuestionare(reg_spaj);
			String dir = props.getProperty("pdf.template.espajdmtm2");
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
	        String espajFile = dir + "\\espaj_"+reg_spaj+".pdf";
	        
	        HashMap moreInfo = new HashMap();
			moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
			moreInfo.put("Title", "DMTM");
			moreInfo.put("Subject", "E-SPAJ DMTM DANA SEJAHTERA");
			
			PdfContentByte over;
			BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			
	        PdfReader reader = new PdfReader(props.getProperty("pdf.template.espajdmtm2")+"\\espajdmtm2.pdf");
	        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espajDMTM2_"+reg_spaj+".pdf";
	        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
	        
	        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
	        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
	        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
	        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
	        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
	        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
	        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);//ada isinya
	        List detInv = this.uwManager.selectdetilinvestasimallspaj(reg_spaj);
	        List benef = this.elionsManager.select_benef(reg_spaj);
	        List medQuest=this.uwManager.selectquestionareDMTM(reg_spaj);
	        List peserta=this.uwManager.select_all_mst_peserta(reg_spaj);
	        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
	        List<MedQuest> mq = uwManager.selectMedQuest(reg_spaj,null);
	        Agen agen =this.elionsManager.select_detilagen(reg_spaj);
	        dataUsulan.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
	        List namaBank =uwManager.namaBank(accRecur.getLbn_id());
			Map premiProdukUtama = this.elionsManager.selectPremiProdukUtama(reg_spaj);
			String kurs = (String) premiProdukUtama.get("LKU_ID");
	        
	        if(!dataUsulan.getLca_id().equals("40")){
//				ServletOutputStream sos = response.getOutputStream();
//    			sos.println("<script>alert('E-SPAJ DMTM , Hanya Untuk jalur distribusi DMTM');window.close();</script>");
//    			sos.close();
			}
	        
	        try {
	        stamp.setMoreInfo(moreInfo);
			
			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman,7);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 90, 646, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getMspo_no_blanko())?"-":dataPP.getMspo_no_blanko().toUpperCase(), 400, 646, 0);
		    
		    over.setFontAndSize(times_new_roman,5);
		  //Data PP
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 160, 619, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_mother().toUpperCase(), 160, 606, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLside_name(), 160, 592, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_no_identity(), 246, 592, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(dataPP.getLsne_note())?"-":dataPP.getLsne_note().toUpperCase(), 160, 578, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_place_birth().toUpperCase() + ", " + FormatDate.toIndonesian(dataPP.getMspe_date_birth()), 160, 565, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sex2().toUpperCase(), 160, 551, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataPP.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataPP.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 160, 537, 0);
		   
		    int monyong = 0;
        	String[] alamat = StringUtil.pecahParagraf(dataPP.getAlamat_rumah().toUpperCase(), 55);
        	for(int i=0; i<alamat.length; i++) {
        		monyong = 7 * i;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							160, 527-monyong, 0);
        	}
        	
        	monyong = 0;
 			if(!Common.isEmpty(dataPP.getAlamat_kantor())){
 				String[] alamat_kantor =  StringUtil.pecahParagraf(dataPP.getAlamat_kantor().toUpperCase(), 55);
 	        	for(int i=0; i<alamat_kantor.length; i++) {
 	        		monyong = 7 * i;
 	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							160, 514-monyong, 0);
 	        	}
 			}
        	
        	monyong = 0;
        	String[] alamat_billing = StringUtil.pecahParagraf(addrBill.getMsap_address(), 55);
        	for(int i=0; i<alamat_billing.length; i++) {
        		monyong = 7 * i;;
        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							160, 500-monyong, 0);
        	}
        	
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getNo_hp())?"":dataPP.getNo_hp()) , 160, 482, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getEmail())?"":dataPP.getEmail()) , 160, 468, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataPP.getMkl_penghasilan())?"":dataPP.getMkl_penghasilan().toUpperCase()), 160, 455, 0);
        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_pendanaan().toUpperCase(), 160, 441, 0);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_kerja().toUpperCase(), 160, 427, 0);
    		
    		over.setFontAndSize(times_new_roman,6);
    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getLsre_relation().toUpperCase(), 290, 414, 0);
			
    	if(dataPP.getLsre_id()!=1){
    			over.setFontAndSize(times_new_roman,5);
        		//Data tertanggung
    			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first().toUpperCase(), 350, 619, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_mother().toUpperCase(), 350, 606, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLside_name(), 350, 592, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_no_identity(), 436, 592, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsne_note().toUpperCase(), 350, 578, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_place_birth().toUpperCase() + ", " + FormatDate.toIndonesian(dataTT.getMspe_date_birth()), 350, 565, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sex2().toUpperCase(), 350, 551, 0);
    		    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataTT.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataTT.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 350, 537, 0);
    			 
    		    monyong = 0;
    	        	String[] alamat2 = StringUtil.pecahParagraf(dataTT.getAlamat_rumah().toUpperCase(), 55);
    	        	for(int i=0; i<alamat.length; i++) {
    	        		monyong = 7 * i;
    	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i],							350, 527-monyong, 0);
    	        	}
    	        	monyong = 0;
    	 			if(!Common.isEmpty(dataPP.getAlamat_kantor())){
    	 				String[] alamat_kantor =  StringUtil.pecahParagraf(dataTT.getAlamat_rumah().toUpperCase()/*getAlamat_kantor().toUpperCase()*/, 55);
    	 	        	for(int i=0; i<alamat_kantor.length; i++) {
    	 	        		monyong = 7 * i;
    	 	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_kantor[i],							350, 514-monyong, 0);
    	 	        	}
    	 			}
    	        	
    	        	monyong = 0;
    	        	String[] alamat_billing2 = StringUtil.pecahParagraf(addrBill.getMsap_address(), 55);
    	        	for(int i=0; i<alamat_billing.length; i++) {
    	        		monyong = 7 * i;
    	        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat_billing[i],							350, 500-monyong, 0);
    	        	}
    	        	
    	        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getNo_hp())?"":dataTT.getNo_hp()) , 350, 482, 0);
		        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getEmail())?"":dataTT.getEmail()) , 350, 468, 0);
		        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty( dataTT.getMkl_penghasilan())?"":dataTT.getMkl_penghasilan().toUpperCase()), 350, 455, 0);
		        	over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_pendanaan().toUpperCase(), 350, 441, 0);
		    		over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_kerja().toUpperCase(), 350, 427, 0);
        	}
		    		
		    		over.setFontAndSize(times_new_roman,6);
		    	//	if (dataTT.getMste_flag_cc()==1){
			    if (accRecur!=null)
			    	{
			    			String bank_pusat = "";
			    			String bank_cabang= "";
			    			
			    			if(!namaBank.isEmpty()){
			        			HashMap m = (HashMap)namaBank.get(0);
			        			bank_pusat = (String)m.get("LSBP_NAMA");
			        			bank_cabang = (String)m.get("LBN_NAMA");
			    			}
			    			
						over.setFontAndSize(times_new_roman,5);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(bank_pusat)?"":bank_pusat.toUpperCase()+" - "+(Common.isEmpty(bank_cabang)?"":bank_cabang.toUpperCase()), 110, 392, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(bank_cabang)?"":bank_cabang.toUpperCase()/*accRecur.getLbn_nama().toUpperCase()*/, 110, 381, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_acc_no())?"":accRecur.getMar_acc_no().toUpperCase(), 396, 392, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common.isEmpty(accRecur.getMar_holder())?"":accRecur.getMar_holder().toUpperCase(), 396, 381, 0);
			    		}
				//	}
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLsdbs_name(),155, 338, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())),379, 338, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())),520, 338, 0);//dataUsulan.getMspr_ins_period().toString()
			    over.showTextAligned(PdfContentByte.ALIGN_RIGHT,dataUsulan.getMspr_ins_period().toString()+" Tahun",180, 324, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLscb_pay_mode(),162, 310, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),222, 299, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(dataPP.getMste_tgl_recur()), 162, 288, 0);

				//data manfaat yang ditunjuk
	    		if(benef.size()>0){
				int j=0;
				for(int i=0;i<benef.size();i++){
				Benefeciary benefi = (Benefeciary) benef.get(i);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_first(),86, 236-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(benefi.getMsaw_sex()==1?"L":"P"),325, 236-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getSmsaw_birth(),375, 236-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getLsre_relation().toUpperCase(),463, 236-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_persen().toString(),532, 236-j, 0);
				j+=11;}
			}
	    		int x=0;
				int y=0;
				int y1=0;
	    	//Pertanyaan MEDIS 
				if (!mq.isEmpty()){
				if(mq.size()>2) {
//					for(int i=0;i<mq.size();i++){
			    		for(int i=0;i<2;i++){
							Integer kelamin = null;
							if(mq.get(i).getMste_insured_no()==0){
								kelamin = dataPP.getMspe_sex();
							}else if (mq.get(i).getMste_insured_no()==1){
								kelamin = dataTT.getMspe_sex();
							}
							
						// Questionare SPAJ ONLINE SYARIAH
							//Berat dan Tinggi, 1a, 1b
							over.setFontAndSize(times_new_roman,6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_berat().toString() +" Kg", 447+x, 198, 0);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_tinggi().toString()+" Cm",447+x, 189, 0);
							//Pertanyaan 2
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_berat_berubah()==1?"YA" : "TIDAK"), 447+x, 180, 0);
							over.setFontAndSize(times_new_roman,5);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TT : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
							(Common.isEmpty(mq.get(i).getMsadm_berubah_desc())?"":mq.get(i).getMsadm_berubah_desc()), 76, 176-y, 0);
							//Pertanyaan 3
						    over.setFontAndSize(times_new_roman,6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,(mq.get(i).getMsadm_penyakit()==1?"YA" : "TIDAK"),447+x, 154, 0);
							over.setFontAndSize(times_new_roman,5);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
							(Common.isEmpty(mq.get(i).getMsadm_penyakit_desc())?"getMsadm_penyakit_desc":mq.get(i).getMsadm_penyakit_desc()), 76, 143-y, 0);
							//pertanyaan 4
							over.setFontAndSize(times_new_roman,6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_medis()==1?"YA" : "TIDAK"), 447+x, 122, 0);
							over.setFontAndSize(times_new_roman,5);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
							(Common.isEmpty(mq.get(i).getMsadm_medis_desc())?"getMsadm_medis_desc":mq.get(i).getMsadm_medis_desc()), 76, 97-y, 0);
							//Pertanyaan 5 Khusus Wanita
							if(kelamin==0){
								over.setFontAndSize(times_new_roman,6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMsadm_pregnant())?"TIDAK":(mq.get(i).getMsadm_pregnant()==1?"YA" : "TIDAK")),447+x, 76, 0);
								over.setFontAndSize(times_new_roman,5);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
								(Common.isEmpty(mq.get(i).getMsadm_pregnant_desc())?"":mq.get(i).getMsadm_pregnant_desc()), 76, 71-y1, 0);
								y1+=6;
							}
							x+=68;
							y+=9;
						}
				}else{
					for(int i=0;i<mq.size();i++){
//			    		for(int i=0;i<2;i++){
							Integer kelamin = null;
							if(mq.get(i).getMste_insured_no()==0){
								kelamin = dataPP.getMspe_sex();
							}else if (mq.get(i).getMste_insured_no()==1){
								kelamin = dataTT.getMspe_sex();
							}
							
						// Questionare SPAJ ONLINE SYARIAH
							//Berat dan Tinggi, 1a, 1b
							over.setFontAndSize(times_new_roman,6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_berat().toString() +" Kg", 447+x, 198, 0);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, mq.get(i).getMsadm_tinggi().toString()+" Cm",447+x, 189, 0);
							//Pertanyaan 2
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_berat_berubah()==1?"YA" : "TIDAK"), 447+x, 180, 0);
							over.setFontAndSize(times_new_roman,5);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"TT : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
							(Common.isEmpty(mq.get(i).getMsadm_berubah_desc())?"":mq.get(i).getMsadm_berubah_desc()), 76, 176-y, 0);
							//Pertanyaan 3
						    over.setFontAndSize(times_new_roman,6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,(mq.get(i).getMsadm_penyakit()==1?"YA" : "TIDAK"),447+x, 154, 0);
							over.setFontAndSize(times_new_roman,5);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
							(Common.isEmpty(mq.get(i).getMsadm_penyakit_desc())?"getMsadm_penyakit_desc":mq.get(i).getMsadm_penyakit_desc()), 76, 143-y, 0);
							//pertanyaan 4
							over.setFontAndSize(times_new_roman,6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (mq.get(i).getMsadm_medis()==1?"YA" : "TIDAK"), 447+x, 122, 0);
							over.setFontAndSize(times_new_roman,5);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
							(Common.isEmpty(mq.get(i).getMsadm_medis_desc())?"getMsadm_medis_desc":mq.get(i).getMsadm_medis_desc()), 76, 97-y, 0);
							//Pertanyaan 5 Khusus Wanita
							if(kelamin==0){
								over.setFontAndSize(times_new_roman,6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMsadm_pregnant())?"TIDAK":(mq.get(i).getMsadm_pregnant()==1?"YA" : "TIDAK")),447+x, 76, 0);
								over.setFontAndSize(times_new_roman,5);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,(Common.isEmpty(mq.get(i).getMste_insured_no())?"":(mq.get(i).getMste_insured_no()==0?"PP : ":mq.get(i).getMste_insured_no()==1?"PU : ":mq.get(i).getMste_insured_no()==2?"PT- I : ":mq.get(i).getMste_insured_no()==3?"PT- II : ":mq.get(i).getMste_insured_no()==3?"PT- III : ":""))+
								(Common.isEmpty(mq.get(i).getMsadm_pregnant_desc())?"":mq.get(i).getMsadm_pregnant_desc()), 76, 71-y1, 0);
								y1+=6;
							}
							x+=68;
							y+=9;
						}
				}
				}
	    		
	    		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMcl_first(),140, 47, 0);
	    		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMsag_id(),108, 47, 0);
		    over.endText();
		   
			stamp.close();	
	        }catch (Exception e) {
				stamp.close();
//				logger.error("ERROR :", e);
				FileUtils.deleteFile(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj, "espajDMTM2_"+reg_spaj+".pdf");
				return;
			}
	        
	        
			File l_file = new File(outputName);
			try{
				FileInputStream in = new FileInputStream(l_file);
			    int length = in.available();
			    byte[] pdfbytes = new byte[length];
			    in.read(pdfbytes);
			    in.close();

			}catch (Exception e) {
				logger.error("ERROR :", e);
			}
	        
		}
		
		/**
		 * Untuk print alokasi dana top up khusus Stabil Link (164~2)
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 * Filename : PrintPolisPrintingController.java
		 * Create By : Bertho Rafitya Iwasurya
		 * Date Created : Aug 11, 2009 8:43:53 AM
		 */
		public ModelAndView alokasidanaSlinkTopup(HttpServletRequest request,
				HttpServletResponse response) throws Exception{
			
			Connection conn = null;
	
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			Integer tu_ke = ServletRequestUtils.getIntParameter(request, "tu_ke", 0);
			String lsbs = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			List viewUlink = null;
			if(lsbs.equals("188")){
				viewUlink = this.uwManager.selectViewPSaveTopUp(spaj, tu_ke);
			}else{
				viewUlink = this.uwManager.selectViewStableLinkTopUp(spaj, tu_ke);
			}
			if(viewUlink.size()==0) return noReportData(response, null);
			
			Map params = new HashMap();
			if(lsbs.equals("188")){
				params = this.elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 3, 11,tu_ke);
			}else{
				params = this.elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 3, 10,tu_ke);
			}
			
			params.put("elionsManager", this.elionsManager);
			Map viewMap=(Map) viewUlink.get(0);
			String no_polis=(String) viewMap.get("MSPO_POLICY_NO");
			String begDate="";
			if(lsbs.equals("188")){
				begDate=new SimpleDateFormat("yyyyMMdd").format((Date) viewMap.get("MPS_BDATE"));
			}else{
				begDate=new SimpleDateFormat("yyyyMMdd").format((Date) viewMap.get("MSL_BDATE"));
			}
			
			HashMap endors=uwManager.selectPrintBabiRider(spaj);
			
			if(endors!=null){
				String no_endors=(String) endors.get("MSEN_ENDORS_NO");
				Integer tu_ke_end=((BigDecimal) endors.get("MSL_TU_KE")).intValue();
				if(tu_ke_end!=0){//klo no endors udah di insert ke mst_slink baru cek per topup
					if(tu_ke==tu_ke_end){
						String cabang=elionsManager.selectCabangFromSpaj(spaj);
						String reportPath=((String) params.get("reportPath")).substring(17);
						
						try {
							//conn = this.getDataSource().getConnection();
							conn = this.getUwManager().getUwDao().getDataSource().getConnection();
							JasperUtils.exportReportToPdf(
									reportPath+".jasper", props.getProperty("pdf.dir.topup.stabil")+"\\"+cabang+"\\"+spaj, "tmp.pdf", 
									params, conn, 
									null, null, null);
							uwManager.updatePrintStableLinkTopup(spaj, new Integer((String) params.get("tu_ke")));
						} catch (Exception e) {
							logger.error("ERROR :", e);
						}finally{
							closeConnection(conn);
						}
						
						params=  new HashMap();
						reportPath=props.get("report.uw.report_endors").toString();
						params.put("props", props);
						params.put("endorsNo", no_endors);
						try {
							//conn = this.getDataSource().getConnection();
							conn = this.getUwManager().getUwDao().getDataSource().getConnection();
							JasperUtils.exportReportToPdf(
									reportPath+".jasper", props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj, "ENDORS_"+no_polis+"_"+no_endors+".pdf", 
									params, conn, 
									null, null, null);
							uwManager.updatePrintEndors(no_endors);
						} catch (Exception e) {
							logger.error("ERROR :", e);
						}finally{
							closeConnection(conn);
						}
						
		
						List<File> concatFile=new ArrayList<File>();
						concatFile.add(new File(props.getProperty("pdf.dir.topup.stabil")+"\\"+cabang+"\\"+spaj+"\\"+"tmp.pdf"));
						concatFile.add(new File(props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj+"\\"+"ENDORS_"+no_polis+"_"+no_endors+".pdf"));
						concatFile.add(new File(props.getProperty("pdf.dir.syaratpolis")+"\\RIDER\\822-001-STABLE.pdf"));
						PdfUtils.combinePdf(concatFile, props.getProperty("pdf.dir.topup.stabil")+"\\"+cabang+"\\"+spaj, "TOP_UP_"+tu_ke+"_"+no_polis+"_"+begDate+".pdf");
						FileUtils.deleteFile(props.getProperty("pdf.dir.topup.stabil")+"\\"+cabang+"\\"+spaj, "tmp.pdf");
						File l_file = new File(props.getProperty("pdf.dir.topup.stabil")+"\\"+cabang+"\\"+spaj, "TOP_UP_"+tu_ke+"_"+no_polis+"_"+begDate+".pdf");
						FileInputStream in = null;
						ServletOutputStream ouputStream = null;
						try{
							
							response.setContentType("application/pdf");
							response.setHeader("Content-Disposition", "Inline");
							response.setHeader("Expires", "0");
							response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
							response.setHeader("Pragma", "public");
							
							in = new FileInputStream(l_file);
						    ouputStream = response.getOutputStream();
						    
						    IOUtils.copy(in, ouputStream);
						}catch (Exception e) {
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
						return null;
					}else{
						return  generateReport(viewUlink, request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, "TOP_UP_"+tu_ke+"_"+no_polis+"_"+begDate);
					}
				}else{
					if(no_endors!=null){
						String cabang=elionsManager.selectCabangFromSpaj(spaj);
						String reportPath=((String) params.get("reportPath")).substring(17);
						try {
							//conn = this.getDataSource().getConnection();
							conn = this.getUwManager().getUwDao().getDataSource().getConnection();
							JasperUtils.exportReportToPdf(
									reportPath+".jasper", props.getProperty("pdf.dir.topup.stabil")+"\\"+cabang+"\\"+spaj, "tmp.pdf", 
									params, conn, 
									null, null, null);
							uwManager.updatePrintStableLinkTopup(spaj, new Integer((String) params.get("tu_ke")));
						} catch (Exception e) {
							logger.error("ERROR :", e);
						}finally{
							closeConnection(conn);
						}
						params=  new HashMap();
						reportPath=props.get("report.uw.report_endors").toString();
						params.put("props", props);
						params.put("endorsNo", no_endors);
						try {
							//conn = this.getDataSource().getConnection();
							conn = this.getUwManager().getUwDao().getDataSource().getConnection();
							JasperUtils.exportReportToPdf(
									reportPath+".jasper", props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj, "ENDORS_"+no_polis+"_"+no_endors+".pdf", 
									params, conn, 
									null, null, null);
							uwManager.updatePrintEndors(no_endors);
						} catch (Exception e) {
							logger.error("ERROR :", e);
						}finally{
							closeConnection(conn);
						}
						
		
						List<File> concatFile=new ArrayList<File>();
						concatFile.add(new File(props.getProperty("pdf.dir.topup.stabil")+"\\"+cabang+"\\"+spaj+"\\"+"tmp.pdf"));
						concatFile.add(new File(props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj+"\\"+"ENDORS_"+no_polis+"_"+no_endors+".pdf"));
						concatFile.add(new File(props.getProperty("pdf.dir.syaratpolis")+"\\RIDER\\822-001-STABLE.pdf"));
						PdfUtils.combinePdf(concatFile, props.getProperty("pdf.dir.topup.stabil")+"\\"+cabang+"\\"+spaj, "TOP_UP_"+tu_ke+"_"+no_polis+"_"+begDate+".pdf");
						FileUtils.deleteFile(props.getProperty("pdf.dir.topup.stabil")+"\\"+cabang+"\\"+spaj, "tmp.pdf");
						File l_file = new File(props.getProperty("pdf.dir.topup.stabil")+"\\"+cabang+"\\"+spaj, "TOP_UP_"+tu_ke+"_"+no_polis+"_"+begDate+".pdf");
						
						FileInputStream in = null;
						ServletOutputStream ouputStream = null;
						try{
							
							response.setContentType("application/pdf");
							response.setHeader("Content-Disposition", "Inline");
							response.setHeader("Expires", "0");
							response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
							response.setHeader("Pragma", "public");
							
							in = new FileInputStream(l_file);
						    ouputStream = response.getOutputStream();
						    
						    IOUtils.copy(in, ouputStream);
						}catch (Exception e) {
							logger.error("ERROR :", e);

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
						return null;
					}else{
						return  generateReport(viewUlink, request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, "TOP_UP_"+tu_ke+"_"+no_polis+"_"+begDate);
					}
				}
			}else{
				String nama_file = "TOP_UP_"+tu_ke+"_"+no_polis+"_"+begDate;
				if(lsbs.equals("188")){
					nama_file = "POWERSAVE_TU_"+tu_ke+"_"+no_polis+"_"+begDate;
				}
				return  generateReport(viewUlink, request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, nama_file);
			}
			
//			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, params, "0", spaj, "polis_surat_ulink");
		} 
		
		
		
		public ModelAndView alokasidanaSlinkTopupEndorsemen(HttpServletRequest request,
				HttpServletResponse response) throws Exception{
	
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			Integer tu_ke = ServletRequestUtils.getIntParameter(request, "tu_ke", 0);
			String lcaId = this.uwManager.selectLcaIdMstPolicyBasedSpaj( spaj );
			
			List temp = new ArrayList();
			if( lcaId != null && "09".equals( lcaId ) )
				{ temp = this.uwManager.selectForPrintEndorsemenLcaId9( spaj, tu_ke ); }
			else
				{ temp = this.uwManager.selectForPrintEndorsemenNotLcaId9( spaj, tu_ke ); }
//			Map params = this.elionsManager.cetakSuratEndorsemen(temp, spaj, true, 3, 1,tu_ke);
//			Map params = new HashMap();
			Map viewMap=(Map) temp.get(0);
			Date mspoDatePrint = (Date) viewMap.get("MSPO_DATE_PRINT");
			Date mslBDate = (Date) viewMap.get("MSL_BDATE");
//			SimpleDateFormat mspoDatePrintString =  
			String no_polis=(String) viewMap.get("MSPO_POLICY_NO");
			String begDate=new SimpleDateFormat("yyyyMMdd").format((Date) viewMap.get("MSL_BDATE"));
			Map params = new HashMap();
			params.put("no_polis", no_polis);
			params.put("spaj", spaj);
			params.put("namaCabang", (String) viewMap.get("NAMA_CABANG"));
			params.put("mspoPolicyNoFormat", (String) viewMap.get("MSPO_POLICY_NO_FORMAT"));
			params.put("namaPp", viewMap.get("NM_PP"));
			params.put("namaTt",  viewMap.get("NM_TT"));
			params.put("namaReff", viewMap.get("NAMA_REFF"));
			params.put("mspoDatePrint", FormatDate.toIndonesian( mspoDatePrint ) );
			params.put("mslBDate", FormatDate.toIndonesian( mslBDate ));
			params.put("alamat", viewMap.get("ALAMAT"));
			params.put("kota", viewMap.get("KOTA"));
			params.put("elionsManager", this.elionsManager);
			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.print_edorsemen").toString());
			params.put("nowDate", FormatDate.toIndonesian( elionsManager.selectSysdate() ) );
			params.put("lcaId", lcaId );
			
			
			return generateReport(temp, request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, "ENDORSEMEN"+tu_ke+"_"+no_polis+"_"+begDate);
		} 
	
		
		public ModelAndView endorseBabiRider(HttpServletRequest request,
				HttpServletResponse response) throws Exception{
	
			Map params = new HashMap();
			String noEndors = ServletRequestUtils.getStringParameter(request, "noEndors", "");				
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String no_polis = ServletRequestUtils.getStringParameter(request, "no_polis", "");
//			params.put("reportPath", "/WEB-INF/classes/" + props.get("report.uw.report_endors").toString());
//			params.put("props", props);
//			params.put("endorsNo", noEndors);
//			
//			uwManager.updatePrintEndors(noEndors);
//			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, "0", spaj, "ENDORS_"+no_polis+"_"+noEndors);
			String cabang=elionsManager.selectCabangFromSpaj(spaj);
			String reportPath=props.get("report.uw.report_endors").toString();
			params.put("props", props);
			params.put("endorsNo", noEndors);
			Connection conn = null;
			try {
				//conn = this.getDataSource().getConnection();
				conn = this.getUwManager().getUwDao().getDataSource().getConnection();
				JasperUtils.exportReportToPdf(
						reportPath+".jasper", props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj, "tmp.pdf", 
						params, conn, 
						null, null, null);
				uwManager.updatePrintEndors(noEndors);
			} catch (Exception e) {
				logger.error("ERROR :", e);				
			}finally{
				closeConnection(conn);
			}
			

			List<File> concatFile=new ArrayList<File>();
			
			concatFile.add(new File(props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj+"\\tmp.pdf"));
			concatFile.add(new File(props.getProperty("pdf.dir.syaratpolis")+"\\RIDER\\822-001-STABLE.pdf"));
			PdfUtils.combinePdf(concatFile, props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj,"ENDORS_"+no_polis+"_"+noEndors+".pdf");
			FileUtils.deleteFile(props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj, "tmp.pdf");
			File l_file = new File(props.getProperty("pdf.dir.endors")+"\\"+cabang+"\\"+spaj+"\\"+"ENDORS_"+no_polis+"_"+noEndors+".pdf");
			
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();
			    
			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
				logger.error("ERROR :", e);
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
			
			return null;
		} 
		
		public ModelAndView generatepdf(HttpServletRequest request,
				HttpServletResponse response) throws Exception{
			
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			//Polis
			Map paramsPolis = printPolis(spaj, request, PrintPolisMultiController.POLIS_QUADRUPLEX);
			//Manfaat
			Map paramsManfaat = elionsManager.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
			String pathManfaat = (String) paramsManfaat.get("reportPath");
			pathManfaat = pathManfaat.substring(17);
			paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
			paramsManfaat.remove("reportPath");
			
			paramsPolis.putAll(paramsManfaat);
			
			//paramsPolis.put("koneksi", getConnection());
			List temp = new ArrayList(); Map map = new HashMap();
			map.put("halaman", "1"); temp.add(map);//polis
			map.put("halaman", "2"); temp.add(map);//manfaat
			map.put("halaman", "3"); temp.add(map);//surat
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {
				map.put("halaman", "4"); temp.add(map);//kosong
				map.put("halaman", "5"); temp.add(map);//alokasi dana (bisa 1 / 2 halaman)
			}
			paramsPolis.put("dsManfaat", JasperReportsUtils.convertReportData(temp));
			
			//Surat Polis
//			if("001,045,053,054,130,131,132".indexOf(FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3))==-1) {
				paramsPolis.put("pathSurat", props.get("report.surat_polis").toString() + ".jasper");
				paramsPolis.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid (Yusuf - 04/05/2006)
//			}		
			
			//Alokasi Dana
			List viewUlink = this.elionsManager.selectViewUlink(spaj);
			if(viewUlink.size()!=0){
				Map paramsAlokasi = this.elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 1, 1,0);
				paramsAlokasi.put("elionsManager", this.elionsManager);
				
				String pathAlokasi = (String) paramsAlokasi.get("reportPath");
				pathAlokasi = pathAlokasi.substring(17);
				paramsAlokasi.put("pathAlokasiDana", pathAlokasi + ".jasper");
				paramsAlokasi.remove("reportPath");
				paramsAlokasi.put("dsAlokasiDana", JasperReportsUtils.convertReportData(viewUlink));
				
				paramsPolis.putAll(paramsAlokasi);
			}
			if (paramsPolis.get("tipePolis").toString().equals("O R I G I N A L")){
				return generateReport(temp, request, response, paramsPolis, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.polis_quadruplex"));
			} else {
				return generateReport(temp, request, response, paramsPolis, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.polis_quadruplex_duplicate"));
			}
		} 
	
	/** VIEWER **/
		public ModelAndView viewendorse(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException{
	
			Map params = new HashMap();
			
			String tipeendorse = ServletRequestUtils.getStringParameter(request, "tipeendorse", "");
			String endorseno = ServletRequestUtils.getStringParameter(request, "endorseno", "");
			
			params = this.elionsManager.prosesCetakEndorsementBaru(endorseno, tipeendorse);
	
			List dummy = new ArrayList();
			dummy.add("1");
			return generateReport(dummy, request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), null, null);
		}

		public ModelAndView viewpayment(HttpServletRequest request,
				HttpServletResponse response) throws ParseException, MalformedURLException{
	
			Map params = new HashMap();
			
			String ket = ServletRequestUtils.getStringParameter(request, "ket", "");
			if(ket.trim().equals("SIMP"))
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.viewpayment3").toString());
			else if(ket.trim().equals("PIJ"))
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.viewpayment2").toString());
			else 
				params.put("reportPath", "/WEB-INF/classes/" + props.get("report.viewpayment1").toString());
	
			params.put("startDate", df2.format(defaultDateFormat.parse(ServletRequestUtils.getStringParameter(request, "startDate", ""))));
			params.put("endDate", df2.format(defaultDateFormat.parse(ServletRequestUtils.getStringParameter(request, "endDate", ""))));
			params.put("ket", ket.trim());
	
			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), null, null);				
		}
		
		//Ryan - Konfirmasi Kepesertaan PSN
	  public ModelAndView konfirmPSN(HttpServletRequest request, HttpServletResponse response) throws Exception {
				String reg_spaj = request.getParameter("spaj");
				reg_spaj =reg_spaj.replace(".", "");
				Date sysdate = elionsManager.selectSysdate();
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				
				String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
				String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
				
				String dir = props.getProperty("pdf.template.confirmsyariah");
				
				File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
		        if(!userDir.exists()) {
		            userDir.mkdirs();
		        }
		        String espajFile = dir + "\\surat_konfirmasiPSN_"+reg_spaj+".pdf";
		        
		        HashMap moreInfo = new HashMap();
				moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
				moreInfo.put("Title", "DMTM");
				moreInfo.put("Subject", "SURAT KONFIRMASI E-AGENCY");
				
				PdfContentByte over;
				BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				
		        PdfReader reader = new PdfReader(props.getProperty("pdf.template.confirmsyariah")+"\\konfirmasiPSN.pdf");
		        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"konfirmasiPSN_"+reg_spaj+".pdf";
		        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
		    	List dPsn=new ArrayList();
		    	dPsn=bacManager.selectDataPSN(reg_spaj);
		        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
		        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
		        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
		        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
		        Powersave powersave =this.elionsManager.select_powersaver(reg_spaj);
		        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
		        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
		        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);
		        List detInv = this.uwManager.selectdetilinvestasimallspaj(reg_spaj);
		        List benef = this.elionsManager.select_benef(reg_spaj);
		        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);
		        List peserta=this.uwManager.select_all_mst_peserta(reg_spaj);
		        
		        over = stamp.getOverContent(1);
				over.beginText();
			    over.setFontAndSize(times_new_roman,10);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatString.nomorSPAJ(reg_spaj), 218, 655, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 218, 603, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first().toUpperCase(), 218, 576, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-"/*FormatString.nomorPolis(dataPP.getMspo_policy_no())*/, 218, 629, 0);
			    over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.toIndonesian(sysdate), 438, 299, 0);
			    
				if(dPsn.size()>0){
					int vertikal=160;
					for(int z=0;z<dPsn.size();z++){
						Map cmd = new HashMap();
						cmd =  (Map) dPsn.get(z);
						String panjangNama=(String) cmd.get("NAMA_PSN");
						String FREQ=(String) cmd.get("FREQ");
						vertikal = vertikal + panjangNama.length()+40;
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,panjangNama+" \\",vertikal, 406, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,FREQ+" \\",vertikal, 386, 0);
					}
				}
			    
			    over.endText();
				String ingrid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
			    Image img = Image.getInstance(ingrid);					
				img.setAbsolutePosition(380, 300);		
				img.scaleAbsolute(120, 34);
				over.addImage(img,img.getScaledWidth(), 0, 0, img.getScaledHeight(), 380, 239);
				over.stroke();
				
				stamp.close();	
				
				File l_file = new File(outputName);
				FileInputStream in = null;
				ServletOutputStream ouputStream = null;
				try{
					
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "Inline");
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					
					in = new FileInputStream(l_file);
				    ouputStream = response.getOutputStream();

				    IOUtils.copy(in, ouputStream);
				}catch (Exception e) {
					logger.error("ERROR :", e);
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
					
				return null;
			}
		
//		public ModelAndView surat_tahapan(HttpServletRequest request,
//				HttpServletResponse response) throws ParseException, MalformedURLException{
//	
//			Map params = new HashMap();
//			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
//			String businessId = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
//			Integer businessNumber = this.uwManager.selectBusinessNumber(spaj);
//				
//				List tmp = elionsManager.selectDetailBisnis(spaj);
//				String lsbs = (String) ((Map) tmp.get(0)).get("BISNIS");
//				String lsdbs = (String) ((Map) tmp.get(0)).get("DETBISNIS");
//				
//				String va = uwManager.selectVirtualAccountSpaj(spaj);
//				String lcaId = elionsManager.selectCabangFromSpaj(spaj);
//				Integer flag_cc = elionsManager.select_flag_cc(spaj);
//				
//				Map data 		= uwManager.selectDataVirtualAccount(spaj);
//				int lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
//				String lku_id 	= (String) data.get("LKU_ID");
//				
//					params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.surat_tahapan"));
//				
//			params.put("props", props);
//			params.put("spaj", spaj);
//	
//			boolean isViewer = ServletRequestUtils.getBooleanParameter(request, "isViewer", false);
//				
//			return generateReport(this.elionsManager.getUwDao().getDataSource(), request, response, params, ServletRequestUtils.getStringParameter(request, "attached", "0"), spaj, props.getProperty("pdf.surat_tahapan"));
//		}		

		
		public ModelAndView surattsr(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String dir =null; //props.getProperty("pdf.surat.tsr_dmtm");
			String nip=ServletRequestUtils.getStringParameter(request, "nip");
			String dept=ServletRequestUtils.getStringParameter(request, "dept");
			if(dept.equalsIgnoreCase("49")){
				dir =props.getProperty("pdf.surat.tsr_dmtm");
			}else if (dept.equalsIgnoreCase("63")){
				dir =props.getProperty("pdf.surat.tsr_mall");
			}else{
				dir =props.getProperty("pdf.surat.tsr_bancass");
			}
			String ouputName=dir+"\\"+nip+".pdf";
			File l_file = new File(ouputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try{
				
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");
				
				in = new FileInputStream(l_file);
			    ouputStream = response.getOutputStream();

			    IOUtils.copy(in, ouputStream);
			}catch (Exception e) {
				logger.error("ERROR :", e);
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
			return null;
		}
		
	/*
	 * Update Lufi(Oktober 2015)
	 * Tambahan satu parameter FlagPrePrinted untuk membedakan directprint dan print polis all  */
	public void generateReport(HttpServletRequest request,Integer mspoProvider, int flagPrePrinted) throws Exception{
			
		Connection conn = null;
		try {
			//conn = this.getDataSource().getConnection();
			conn = this.getUwManager().getUwDao().getDataSource().getConnection();
			String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
			String cabang = elionsManager.selectCabangFromSpaj(spaj);
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			String jpu = ServletRequestUtils.getStringParameter(request, "seq", "");
			Integer punyaSimascard = 0;
			String flagUlink ="0";
			//1. Polis
			Map paramsPolis = printPolis(spaj, request, PrintPolisMultiController.POLIS_QUADRUPLEX);
			
			//2. Manfaat
			Integer businessNumber = uwManager.selectBusinessNumber(spaj);
			Map paramsManfaat = elionsManager.prosesCetakManfaat(spaj, currentUser.getLus_id(), request);
			String pathManfaat = (String) paramsManfaat.get("reportPath");
			pathManfaat = pathManfaat.substring(17);
			paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
			paramsManfaat.remove("reportPath");
			paramsManfaat.put("lsdbs", businessNumber);
			paramsPolis.putAll(paramsManfaat);
			
			paramsPolis.put("koneksi", conn);
			List temp = new ArrayList(); Map map = new HashMap();
			map.put("halaman", "1"); temp.add(map);//polis
			map.put("halaman", "2"); temp.add(map);//manfaat
			map.put("halaman", "3"); temp.add(map);//surat
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {
				map.put("halaman", "4"); temp.add(map);//kosong
				map.put("halaman", "5"); temp.add(map);//alokasi dana (bisa 1 / 2 halaman)
			}
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {//surat simas card
				map.put("halaman", "7"); temp.add(map);
			}else{
				map.put("halaman", "4"); temp.add(map);
			}
			map.put("halaman", "6"); temp.add(map);
			map.put("halaman", "8"); temp.add(map);
			map.put("halaman", "9"); temp.add(map);
			map.put("halaman", "10"); temp.add(map);
			paramsPolis.put("dsManfaat", JasperReportsUtils.convertReportData(temp));
			
			String businessId = FormatString.rpad("0", uwManager.selectBusinessId(spaj), 3);
			
			//3. Surat Polis
				String va = uwManager.selectVirtualAccountSpaj(spaj);
				if(cabang.equals("58")){
					if(elionsManager.selectValidasiTransferPbp(spaj)==0){//ini mste_flag_cc
						if(va==null){
							uwManager.updateVirtualAccountBySpaj(spaj);
						}
					}
				}
				
				Map data 		= uwManager.selectDataVirtualAccount(spaj);
				Integer lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
				String lku_id 	= (String) data.get("LKU_ID");
				
				Integer flag_cc = elionsManager.select_flag_cc(spaj);
				
				if(lku_id.equals("01") && !products.syariah(businessId,businessNumber.toString()) && lscb_id != 0 && flag_cc ==0 && !businessId.equals("196")&& !(businessId.equals("190") && "5,6".indexOf(businessNumber.toString())>=0) ){
					
					List cekSpajPromo = bacManager.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
					
					if(!cekSpajPromo.isEmpty()){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va_promo") + ".jasper");
					}else if(businessId.equals("217")&& businessNumber== 2){ //untuk ERbe di set menggunakan surat_polis
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
					}		
					
					//paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
				}else if(products.syariah(businessId, businessNumber.toString())){
					if(flag_cc==0 && !businessId.equals("175")){
						if (businessId.equals("215") && businessNumber==003 ) {//Special Case Premium Link - Ryan
							paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
						}else{
							paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah_va") + ".jasper");
						}
						
					}else{
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
					}
				}else if(cabang.equals("58")){
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.mall") + ".jasper");
				}
				else{
					if(businessId.equals("196")&& businessNumber==002){
						paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.term") + ".jasper");
					}else{
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
					}
				}
				
				paramsPolis.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid (Yusuf - 04/05/2006)

			
			//4. Alokasi Dana
			List viewUlink = elionsManager.selectViewUlink(spaj);
			if(viewUlink.size()!=0){
				Map paramsAlokasi = elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 1, 1,0);
				paramsAlokasi.put("elionsManager", elionsManager);
				
				String pathAlokasi = (String) paramsAlokasi.get("reportPath");
				pathAlokasi = pathAlokasi.substring(17);
				paramsAlokasi.put("pathAlokasiDana", pathAlokasi + ".jasper");
				paramsAlokasi.remove("reportPath");
				paramsAlokasi.put("dsAlokasiDana", JasperReportsUtils.convertReportData(viewUlink));
				
				paramsPolis.putAll(paramsAlokasi);
			}
			
		
			//5 Tanda Terima Polis
			Integer referal = ServletRequestUtils.getIntParameter(request, "referal", 0);
			
			List detBisnis = elionsManager.selectDetailBisnis(spaj);
			String lsbs_id = (String) ((Map) detBisnis.get(0)).get("BISNIS");
			String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
//			String lsbs_id = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
			String reportPath;
			String namaFile = props.getProperty("pdf.tanda_terima_polis");
			int isInputanBank = elionsManager.selectIsInputanBank(spaj); 
			if(((isInputanBank == 2 || isInputanBank == 3) && referal == 0 && !products.productBsmFlowStandardIndividu(Integer.parseInt(lsbs_id), Integer.parseInt(lsdbs))) || (lsbs_id.equals("175")&&lsdbs.equals("002"))) {
				reportPath = props.getProperty("report.tandaterimasertifikat");
				namaFile = props.getProperty("pdf.tanda_terima_sertifikat");
			}else if(products.unitLink(lsbs_id) && !products.stableLink(lsbs_id)) {
				if(products.syariah(lsbs_id, lsdbs)){
					reportPath = props.getProperty("report.tandaterimapolis.syariah");
				}else{
					reportPath = props.getProperty("report.tandaterimapolis.link");
				}
			}else if(lsbs_id.equals("187")){
				reportPath = props.getProperty("report.tandaterimapolis.pas");
			}else {
				reportPath = props.getProperty("report.tandaterimapolis"); //ini udah include biasa + syariah
			}
			
			paramsPolis.put("referal", referal);
			paramsPolis.put("pathTandaTerimaPolis", reportPath + ".jasper");
			
			 	
			if(flagPrePrinted==2){
				
				List daftarSebelumnya = uwManager.selectSimasCard(spaj);
				List isAgen = uwManager.selectIsSimasCardClientAnAgent(spaj);
				
				if(!daftarSebelumnya.isEmpty() && isAgen.isEmpty()){
					Map SimasCardSebelumnya = (Map) daftarSebelumnya.get(0);
					if(!Common.isEmpty(SimasCardSebelumnya.get("REG_SPAJ"))){
						if(SimasCardSebelumnya.get("REG_SPAJ").equals(spaj)){
							punyaSimascard=1;
						}else{
							elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "SUDAH PERNAH DAPAT SIMAS CARD DENGAN NO SPAJ"+ SimasCardSebelumnya.get("REG_SPAJ"), spaj, 0);
						}
					}
				}	
					
			}
			String reportPathSimasCard = props.get("report.surat_simcard").toString();
			paramsPolis.put("pathSuratSimasCard", reportPathSimasCard + ".jasper");
			
			for(Iterator iter = paramsPolis.keySet().iterator(); iter.hasNext();){
				String nama = (String) iter.next();
				logger.info(nama + " = " + paramsPolis.get(nama));
			}
			
			//SSU	
			//update 19 November 2015 Semua SSU produk utama tidak diikutsertakan(REQ TIMMY-Hanya rider yg diikutsertakan)
			if(	PDFViewer.checkFileProduct(elionsManager, uwManager, props, spaj));{
				List<File> pdfFiles = new ArrayList<File>();
				String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;						
			    String dirSsk = props.getProperty("pdf.dir.syaratpolis");					
			    for(int i=0; i<detBisnis.size(); i++) {
					Map m = (HashMap) detBisnis.get(i);
					File file = PDFViewer.riderFile(elionsManager,uwManager, dirSsk, lsbs_id, lsdbs, m,spaj, props);
					if(file!=null) if(file.exists()) pdfFiles.add(file);
				}
				PdfUtils.combinePdf(pdfFiles, exportDirectory, "pathSS.pdf");
			}
				
			//ENDORS SMILE MEDICAL
			
			if(mspoProvider==2){
				PrintPolisPerjanjianAgent printPolis = new PrintPolisPerjanjianAgent();
				List<String> pdfs = new ArrayList<String>();
				Boolean suksesMerge = false;
				Boolean scFile=false;
				String endorsPolis = "";
				String Kartuadmedika = "";
				String Pesertaadmedika = "";
				String provider = "";
				String filename = "pathAdmedika";
				Date sysdate = elionsManager.selectSysdate();
				Boolean syariah = products.syariah(lsbs_id, lsdbs);
				
				Integer ekaSehatBaru = uwManager.selectCountEkaSehatAdmedikaNew(spaj,0);
				Integer ekaSehatHCP = uwManager.selectCountEkaSehatAdmedikaHCP(spaj);
				Integer ekaSehatPlus = uwManager.selectCountEkaSehatAdmedikaNew(spaj,1);
				Integer s=Integer.parseInt(lsdbs.substring(1));
				Integer punyaEndorsAdmedika = bacManager.selectPunyaEndorsEkaSehatAdmedika(spaj);
				
				if(punyaEndorsAdmedika == 0)bacManager.prosesEndorsKetinggalanNew(spaj, Integer.parseInt(lsbs_id));
				
				if(ekaSehatBaru>=1){
					if(lsbs_id.equals("189")|| products.syariah(lsbs_id, lsdbs)){
						if(lsbs_id.equals("189") && Integer.parseInt(lsdbs.substring(1))>15){
							endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariahSMP.pdf";
						}else{
							endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariah.pdf";
						}
					}else{
						endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedical.pdf"; // EndorsemenPolisBaru
					}
				}else if(ekaSehatPlus>=1){				
					endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedicalPlus.pdf";
				}else{
					endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolis.pdf";
				}
				if(ekaSehatHCP>=1 && ekaSehatBaru<1 && ekaSehatPlus<1){
					Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuHCP.pdf";
					provider = props.getProperty("pdf.template.admedika")+"\\ProviderHCP.pdf";
				}else{
					Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuAdmedika.pdf";
					provider = props.getProperty("pdf.template.admedika")+"\\Provider.pdf";
				}
				Pesertaadmedika = props.getProperty("pdf.template.admedika")+"\\PesertaAdmedika.pdf";
				
				if (ekaSehatBaru>=1 || ekaSehatPlus>=1){
					pdfs.add(endorsPolis);
				}
				if(flagPrePrinted==1){
					//pdfs.add(Kartuadmedika);
					//pdfs.add(provider);
				}
				Integer total_ekasehat = uwManager.getJumlahEkaSehat(spaj);
				List<Map> datapeserta = uwManager.selectDataPeserta(spaj);
				
				
				if(ekaSehatHCP>=1){
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaHCP.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}else if(lsbs_id.equals("189") || products.syariah(lsbs_id, lsdbs)){
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatSyariah.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}else if(ekaSehatPlus>=1){
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaMedicalPlus.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}else{
					// Andhika
					OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatNew.pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}
				
				File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
		        if(!userDir.exists()) {
		            userDir.mkdirs();
		        }
				
				String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+filename+".pdf";
				
				if(!scFile) {
					Map dataAdmedika = uwManager.selectDataAdmedika(spaj);
					String ingrid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
					printPolis.generateEndorseAdmedikaEkaSehat(spaj,total_ekasehat, outputName, dataAdmedika, datapeserta, sysdate, ingrid,lsbs_id,lsdbs,syariah,ekaSehatHCP,ekaSehatBaru,ekaSehatPlus);
				}
			}
			if(products.unitLink(uwManager.selectBusinessId(spaj))) {
				flagUlink="1";
			}
			
			//randy - direct print e-spaj dmtm (req. timmy) - 07/09/2016
			if (cabang.equals("40")){
				this.espajdmtm3(spaj);
			}
			
				String path ="";
				path = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
				String pathTemplate = props.getProperty("pdf.template")+"\\BlankPaper.pdf";
				try {
					JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathSurat").toString(), path , "pathSurat.pdf", paramsPolis, conn);
					JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathManfaat").toString(),path , "pathManfaat.pdf",paramsPolis, conn);
					JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathPolis").toString(),path , "pathPolis.pdf", paramsPolis, conn);
					JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathTandaTerimaPolis").toString(),path, "pathTandaTerimaPolis.pdf", paramsPolis, conn);
					if(viewUlink.size()!=0)JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathAlokasiDana").toString(),path, "pathAlokasiDana.pdf", paramsPolis, conn);
					if(flagPrePrinted==2)JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathSuratSimasCard").toString(),path, "pathSuratSimasCard.pdf", paramsPolis, conn);
					Print.generateReportMergeAndDelete(cabang,path,pathTemplate,lsbs_id,mspoProvider,flagUlink,punyaSimascard,flagPrePrinted);
				}catch(Exception e){
			        throw e;
				}
		}finally{
				this.closeConnection(conn);
		}
	}
	
	public void generateReportScheduler(String spaj,Integer mspoProvider) throws Exception{
		
		Connection conn = null;
		try {
			//conn = this.getDataSource().getConnection();
			conn = this.getUwManager().getUwDao().getDataSource().getConnection();
		
		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		//User currentUser = (User) request.getSession().getAttribute("currentUser");
		String jpu = "";
		//1. Polis
		Map paramsPolis = printPolisScheduler(spaj, PrintPolisMultiController.POLIS_QUADRUPLEX);
		
		//2. Manfaat
		Integer businessNumber = uwManager.selectBusinessNumber(spaj);
		Map paramsManfaat = bacManager.prosesCetakManfaat(spaj, "1");
		String pathManfaat = (String) paramsManfaat.get("reportPath");
		pathManfaat = pathManfaat.substring(17);
		paramsManfaat.put("pathManfaat", pathManfaat + ".jasper");
		paramsManfaat.remove("reportPath");
		paramsManfaat.put("lsdbs", businessNumber);
		paramsPolis.putAll(paramsManfaat);
		
		paramsPolis.put("koneksi", conn);
		List temp = new ArrayList(); Map map = new HashMap();
		map.put("halaman", "1"); temp.add(map);//polis
		map.put("halaman", "2"); temp.add(map);//manfaat
		map.put("halaman", "3"); temp.add(map);//surat
		if(products.unitLink(uwManager.selectBusinessId(spaj))) {
			map.put("halaman", "4"); temp.add(map);//kosong
			map.put("halaman", "5"); temp.add(map);//alokasi dana (bisa 1 / 2 halaman)
		}
		if(products.unitLink(uwManager.selectBusinessId(spaj))) {//surat simas card
			map.put("halaman", "7"); temp.add(map);
		}else{
			map.put("halaman", "4"); temp.add(map);
		}
		map.put("halaman", "6"); temp.add(map);
		map.put("halaman", "8"); temp.add(map);
		map.put("halaman", "9"); temp.add(map);
		map.put("halaman", "10"); temp.add(map);
		paramsPolis.put("dsManfaat", JasperReportsUtils.convertReportData(temp));
		
		String businessId = FormatString.rpad("0", uwManager.selectBusinessId(spaj), 3);
		
		//3. Surat Polis
			String va = uwManager.selectVirtualAccountSpaj(spaj);
			if(cabang.equals("58")){
				if(elionsManager.selectValidasiTransferPbp(spaj)==0){//ini mste_flag_cc
					if(va==null){
						uwManager.updateVirtualAccountBySpaj(spaj);
					}
				}
			}
			
			Map data 		= uwManager.selectDataVirtualAccount(spaj);
			Integer lscb_id 	= ((BigDecimal) data.get("LSCB_ID")).intValue();
			String lku_id 	= (String) data.get("LKU_ID");
			
			Integer flag_cc = elionsManager.select_flag_cc(spaj);
			
			if(lku_id.equals("01") && !products.syariah(businessId,businessNumber.toString()) && lscb_id != 0 && flag_cc ==0 && !businessId.equals("196") && !(businessId.equals("190") && "5,6".indexOf(businessNumber.toString())>=0)){
				
				List cekSpajPromo = bacManager.selectCekSpajPromo(  null , spaj,  "1"); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
				
				if(!cekSpajPromo.isEmpty()){
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va_promo") + ".jasper");
				}else if(businessId.equals("217")&& businessNumber== 2){ //untuk ERbe di set menggunakan surat_polis
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
				}else{
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
				}
				
				//paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.va") + ".jasper");
			}else if(products.syariah(businessId, businessNumber.toString())){
				if(flag_cc==0 && !businessId.equals("175")){
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah_va") + ".jasper");
				}else{
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.syariah") + ".jasper");
				}
			}else if(cabang.equals("58")){
				paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.mall") + ".jasper");
			}
			else{
				if(businessId.equals("196")&& businessNumber==002){
					paramsPolis.put("pathSurat", props.getProperty("report.surat_polis.term") + ".jasper");
				}else{
				paramsPolis.put("pathSurat", props.getProperty("report.surat_polis") + ".jasper");
				}
			}
			
			paramsPolis.put("hamid", props.get("images.ttd.direksi")); //ttd pak hamid (Yusuf - 04/05/2006)

		
		//4. Alokasi Dana
		List viewUlink = elionsManager.selectViewUlink(spaj);
		if(viewUlink.size()!=0){
			Map paramsAlokasi = elionsManager.cetakSuratUnitLink(viewUlink, spaj, true, 1, 1,0);
			paramsAlokasi.put("elionsManager", elionsManager);
			
			String pathAlokasi = (String) paramsAlokasi.get("reportPath");
			pathAlokasi = pathAlokasi.substring(17);
			paramsAlokasi.put("pathAlokasiDana", pathAlokasi + ".jasper");
			paramsAlokasi.remove("reportPath");
			paramsAlokasi.put("dsAlokasiDana", JasperReportsUtils.convertReportData(viewUlink));
			
			paramsPolis.putAll(paramsAlokasi);
		}
		
	
		//5 Tanda Terima Polis
		Integer referal = 0;
		
		List detBisnis = elionsManager.selectDetailBisnis(spaj);
		String lsbs_id = (String) ((Map) detBisnis.get(0)).get("BISNIS");
		String lsdbs = (String) ((Map) detBisnis.get(0)).get("DETBISNIS");
//		String lsbs_id = FormatString.rpad("0", this.uwManager.selectBusinessId(spaj), 3);
		String reportPath;
		String namaFile = props.getProperty("pdf.tanda_terima_polis");
		int isInputanBank = elionsManager.selectIsInputanBank(spaj);
		if(((isInputanBank == 2 || isInputanBank == 3) && referal == 0 && !products.productBsmFlowStandardIndividu(Integer.parseInt(lsbs_id), Integer.parseInt(lsdbs))) || (lsbs_id.equals("175")&&lsdbs.equals("002"))) {
			reportPath = props.getProperty("report.tandaterimasertifikat");
			namaFile = props.getProperty("pdf.tanda_terima_sertifikat");
		}else if(products.unitLink(lsbs_id) && !products.stableLink(lsbs_id)) {
			if(products.syariah(lsbs_id, lsdbs)){
				reportPath = props.getProperty("report.tandaterimapolis.syariah");
			}else{
				reportPath = props.getProperty("report.tandaterimapolis.link");
			}
		}else if(lsbs_id.equals("187")){
			reportPath = props.getProperty("report.tandaterimapolis.pas");
		}else {
			reportPath = props.getProperty("report.tandaterimapolis"); //ini udah include biasa + syariah
		}
		
		paramsPolis.put("referal", referal);
		paramsPolis.put("pathTandaTerimaPolis", reportPath + ".jasper");
		
		for(Iterator iter = paramsPolis.keySet().iterator(); iter.hasNext();){
			String nama = (String) iter.next();
			logger.info(nama + " = " + paramsPolis.get(nama));
		}
		
		//SSU
		if(	PDFViewer.checkFileProduct(elionsManager, uwManager, props, spaj));{
			String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
			List<File> pdfFiles = WordingPdfViewer.listFileProduct(elionsManager, uwManager, props, spaj);
			PdfUtils.combinePdf(pdfFiles, exportDirectory, "pathSS.pdf");
		}
			
		//ENDORS SMILE MEDICAL
		
		if(mspoProvider==2){
			PrintPolisPerjanjianAgent printPolis = new PrintPolisPerjanjianAgent();
			List<String> pdfs = new ArrayList<String>();
			Boolean suksesMerge = false;
			Boolean scFile=false;
			String endorsPolis = "";
			String Kartuadmedika = "";
			String Pesertaadmedika = "";
			String provider = "";
			String filename = "pathAdmedika";
			Date sysdate = elionsManager.selectSysdate();
			Boolean syariah = products.syariah(lsbs_id, lsdbs);
			
			Integer ekaSehatBaru = uwManager.selectCountEkaSehatAdmedikaNew(spaj,0);
			Integer ekaSehatHCP = uwManager.selectCountEkaSehatAdmedikaHCP(spaj);
			Integer ekaSehatPlus = uwManager.selectCountEkaSehatAdmedikaNew(spaj,1);
			Integer s=Integer.parseInt(lsdbs.substring(1));
			Integer punyaEndorsAdmedika = bacManager.selectPunyaEndorsEkaSehatAdmedika(spaj);
			
			if(punyaEndorsAdmedika == 0)bacManager.prosesEndorsKetinggalanNew(spaj, Integer.parseInt(lsbs_id));
			
			if(ekaSehatBaru>=1){
				if(lsbs_id.equals("189")|| products.syariah(lsbs_id, lsdbs)){
					if(lsbs_id.equals("189") && Integer.parseInt(lsdbs.substring(1))>15){
						endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariahSMP.pdf";
					}else{
						endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolisSyariah.pdf";
					}
				}else{
					endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedical.pdf"; // EndorsemenPolisBaru
				}
			}else if(ekaSehatPlus>=1){				
				endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsementSmileMedicalPlus.pdf";
			}else{
				endorsPolis = props.getProperty("pdf.template.admedika")+"\\EndorsemenPolis.pdf";
			}
			if(ekaSehatHCP>=1 && ekaSehatBaru<1 && ekaSehatPlus<1){
				Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuHCP.pdf";
				provider = props.getProperty("pdf.template.admedika")+"\\ProviderHCP.pdf";
			}else{
				Kartuadmedika = props.getProperty("pdf.template.admedika")+"\\KartuAdmedika.pdf";
				provider = props.getProperty("pdf.template.admedika")+"\\Provider.pdf";
			}
			Pesertaadmedika = props.getProperty("pdf.template.admedika")+"\\PesertaAdmedika.pdf";
			
			if (ekaSehatBaru>=1 || ekaSehatPlus>=1){
				pdfs.add(endorsPolis);
			}
			pdfs.add(Kartuadmedika);
			Integer total_ekasehat = uwManager.getJumlahEkaSehat(spaj);
			List<Map> datapeserta = uwManager.selectDataPeserta(spaj);
			pdfs.add(provider);
			
			if(ekaSehatHCP>=1){
				OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaHCP.pdf");
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}else if(lsbs_id.equals("189") || products.syariah(lsbs_id, lsdbs)){
				OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatSyariah.pdf");
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}else if(ekaSehatPlus>=1){
				OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaMedicalPlus.pdf");
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}else{
				// Andhika
				OutputStream output = new FileOutputStream(props.getProperty("pdf.template.admedika")+"\\MergeAdmedikaEkaSehatNew.pdf");
				suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			}
			
			File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj);
	        if(!userDir.exists()) {
	            userDir.mkdirs();
	        }
			
			String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+filename+".pdf";
			
			if(!scFile) {
				Map dataAdmedika = uwManager.selectDataAdmedika(spaj);
				String ingrid = props.getProperty("pdf.template.admedika2")+"\\hamid.bmp";
				printPolis.generateEndorseAdmedikaEkaSehat(spaj,total_ekasehat, outputName, dataAdmedika, datapeserta, sysdate, ingrid,lsbs_id,lsdbs,syariah,ekaSehatHCP,ekaSehatBaru,ekaSehatPlus);
			}
		}
		
			String path ="";
			path = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
			String pathTemplate = props.getProperty("pdf.template")+"\\BlankPaper.pdf";
			try {
				JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathSurat").toString(), path , "pathSurat.pdf", paramsPolis, conn);
				JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathManfaat").toString(),path , "pathManfaat.pdf",paramsPolis, conn);
				JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathPolis").toString(),path , "pathPolis.pdf", paramsPolis, conn);
				JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathAlokasiDana").toString(),path , "pathAlokasiDana.pdf", paramsPolis, conn);
				JasperUtils.exportReportToPdfNoLock(paramsPolis.get("pathTandaTerimaPolis").toString(),path, "pathTandaTerimaPolis.pdf", paramsPolis, conn);
				Print.generateReportMergeAndDelete(cabang,path,pathTemplate,lsbs_id,mspoProvider,"1",0,1);
			}catch(Exception e){
		        throw e;
			}
		}finally{
			this.closeConnection(conn);
		}
		
	}
	
	private Map printPolisScheduler(String spaj, int singleDuplexQuadruplex) {
		Map params = new HashMap();
		
		params.put("spaj", spaj);
		params.put("props", props);
		String validasiMeterai = uwManager.validasiBeaMeterai(0);
		if(validasiMeterai != null){
			params.put("meterai", null);
			params.put("izin", "");
		}else{
			params.put("meterai", "Rp. 6.000,-");
			params.put("izin", elionsManager.selectIzinMeteraiTerakhir());
		}
		
		//jenis print ulang
		int jpu = -1;
		
		if(jpu == 1) { //PRINT ULANG POLIS
			params.put("tipePolis", "O R I G I N A L");
		}else if(jpu == 2) { //PRINT DUPLIKAT POLIS
			String seq = "";
			params.put("tipePolis", "D U P L I C A T E " + "("+seq+")");
			
		}else if(jpu == 3) { //PRINT ULANG POLIS
			params.put("tipePolis", "O R I G I N A L");
		}
		params.put("ingrid", props.get("images.ttd.direksi")); //ttd dr. ingrid (Yusuf - 04/05/2006)
		
		String kategori = products.kategoriPrintPolis(spaj);
		//String kategori = "guthrie";
		logger.info("JENIS POLIS : " + kategori);

		if(PrintPolisMultiController.POLIS == singleDuplexQuadruplex) { //single
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis."+kategori));
		}else if(PrintPolisMultiController.POLIS_DUPLEX == singleDuplexQuadruplex) { //duplex
			params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.duplex"));
		}else if(PrintPolisMultiController.POLIS_QUADRUPLEX == singleDuplexQuadruplex) { //quadruplex
			params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex"));
		}else if(PrintPolisMultiController.POLIS_QUADRUPLEX_PLUS_HADIAH == singleDuplexQuadruplex) { //quadruplex
			params.put("pathPolis", props.getProperty("report.polis."+kategori)+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex_plus_hadiah"));
		}else if(PrintPolisMultiController.POLIS_QUADRUPLEX_MEDICAL_PLUS== singleDuplexQuadruplex) { //quadruplex				
			params.put("pathPolis", props.getProperty("report.polis."+kategori+".medplus")+".jasper");
			params.put("reportPath", "/WEB-INF/classes/" + props.getProperty("report.polis.quadruplex_medical_plus"));
		}
		return params;
	}

	public String pdfFile(String cabang,String spaj){
		String pdfFile = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj+"\\"+"PolisAll.pdf";
		return pdfFile;
	}
	
	public ModelAndView suratKonfirmasiCerdas(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String reg_spaj = request.getParameter("spaj");
		Date sysdate = elionsManager.selectSysdate();
		
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj;
		Integer data2 = uwManager.selectFlagQuestionare(reg_spaj);
		String dir = props.getProperty("pdf.template.skcerdas");
		File userDir = new File(props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj);
        if(!userDir.exists()) {
            userDir.mkdirs();
        }
        String espajFile = dir + "\\suratkonfirmasi_"+reg_spaj+".pdf";
        
        HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "DMTM");
		moreInfo.put("Subject", "SURAT KONFIRMASI CERDAS CARE");
		
		PdfContentByte over;
		BaseFont times_new_roman = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		
        PdfReader reader = new PdfReader(props.getProperty("pdf.template.skcerdas")+"\\skcerdas.pdf");
        String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"skcerdas_"+reg_spaj+".pdf";
        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
        
        Pemegang dataPP = elionsManager.selectpp(reg_spaj);
        Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
        AddressBilling addrBill = this.elionsManager.selectAddressBilling(reg_spaj);
        Datausulan dataUsulan = this.elionsManager.selectDataUsulanutama(reg_spaj);
        InvestasiUtama inv  = this.elionsManager.selectinvestasiutama(reg_spaj);
        Rekening_client rekClient = this.elionsManager.select_rek_client(reg_spaj);
        Account_recur accRecur = this.elionsManager.select_account_recur(reg_spaj);//ada isinya
        List detInv = this.uwManager.selectdetilinvestasimallspaj(reg_spaj);
        List benef = this.elionsManager.select_benef(reg_spaj);      
        List peserta=this.uwManager.select_all_mst_peserta(reg_spaj);
        Integer lsre_id = uwManager.selectPolicyRelation(reg_spaj);      
        Agen agen =this.elionsManager.select_detilagen(reg_spaj);
        dataUsulan.setDaftaRider(elionsManager.selectDataUsulan_rider(reg_spaj));
        List namaBank =uwManager.namaBank(accRecur.getLbn_id());
		Map premiProdukUtama = this.elionsManager.selectPremiProdukUtama(reg_spaj);
		String kurs = (String) premiProdukUtama.get("LKU_ID");
		HashMap x = new HashMap();
		x = Common.serializableMap(bacManager.selectDataKuisionerCerdas(reg_spaj));
		
        if(!dataUsulan.getLca_id().equals("40")){
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('E-SPAJ DMTM , Hanya Untuk jalur distribusi DMTM');window.close();</script>");
			sos.close();
		}
        
        stamp.setMoreInfo(moreInfo);		
		over = stamp.getOverContent(1);
		over.beginText();
		over.setFontAndSize(times_new_roman,9);	  
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, reg_spaj, 180, 682, 0);	
		over.setFontAndSize(times_new_roman,7);	   
	    //Data PP
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMcl_first().toUpperCase(), 297, 567, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_mother().toUpperCase(), 297, 554, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getLsne_note(), 297, 541, 0);	
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_no_identity(), 297, 525, 0);	
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sex2().toUpperCase(), 297, 512, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataPP.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataPP.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 297, 498, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMspe_place_birth().toUpperCase() +""+","+""+FormatDate.getDay2(dataPP.getMspe_date_birth())+"/"+FormatDate.getMonth(dataPP.getMspe_date_birth())+"/"+FormatDate.getYearFourDigit(dataPP.getMspe_date_birth()) , 297, 484, 0);	  
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getMkl_kerja().toUpperCase(), 297, 471, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getAlamat_rumah().toUpperCase()+" "+(Common.isEmpty(dataPP.getKd_pos_rumah())?"":dataPP.getKd_pos_rumah()) , 297, 456, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, addrBill.getMsap_address().toUpperCase()+""+(Common.isEmpty(addrBill.getMsap_zip_code())?"":addrBill.getMsap_zip_code()) , 297, 443, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, (Common.isEmpty(dataPP.getArea_code_rumah())?"":dataPP.getArea_code_rumah())+dataPP.getTelpon_rumah().toUpperCase(), 297, 430, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getArea_code_kantor()+dataPP.getTelpon_kantor().toUpperCase(), 297, 416, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getNo_hp().toUpperCase(), 297, 402, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getLsre_relation().toUpperCase(), 297, 389, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_penghasilan().toUpperCase(),297, 376, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_pendanaan().toUpperCase(), 297, 362, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataPP.getMkl_tujuan().toUpperCase(), 297, 347, 0);
		//end of Data pp
	  //Data tt
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMcl_first().toUpperCase(), 297, 306, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_mother().toUpperCase(), 297, 291, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getLsne_note(), 297, 278, 0);	
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_no_identity(), 297, 264, 0);	
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sex2().toUpperCase(), 297, 251, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_sts_mrt().equals("1")?"BELUM MENIKAH":(dataTT.getMspe_sts_mrt().equals("2")?"MENIKAH":(dataTT.getMspe_sts_mrt().equals("3")?"JANDA":"DUDA") ), 297, 237, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMspe_place_birth().toUpperCase() +""+","+""+FormatDate.getDay2(dataTT.getMspe_date_birth())+"/"+FormatDate.getMonth(dataTT.getMspe_date_birth())+"/"+FormatDate.getYearFourDigit(dataTT.getMspe_date_birth()) , 297, 224, 0);	  
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getMkl_kerja().toUpperCase(), 297, 210, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getAlamat_rumah().toUpperCase()+" "+(Common.isEmpty(dataTT.getKd_pos_rumah())?"":dataTT.getKd_pos_rumah()) , 297, 195, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, addrBill.getMsap_address().toUpperCase()+""+(Common.isEmpty(addrBill.getMsap_zip_code())?"":addrBill.getMsap_zip_code()) , 297, 181, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getArea_code_rumah()+dataPP.getTelpon_rumah().toUpperCase(), 297, 167, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getArea_code_kantor()+dataPP.getTelpon_kantor().toUpperCase(), 297, 154, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getNo_hp().toUpperCase(), 297, 141, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getLsre_relation().toUpperCase(), 297, 127, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_penghasilan().toUpperCase(),297, 111, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_pendanaan().toUpperCase(), 297, 98, 0);
	    over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataTT.getMkl_tujuan().toUpperCase(), 297, 84, 0);
		//end of Data pp		
		 over.endText();
		 over = stamp.getOverContent(2);
		 over.beginText();
		 over.setFontAndSize(times_new_roman,7);	   
		 //Data rekening Penerimaan Manfaat
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getLsbp_nama().toUpperCase(), 222, 699, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_cabang().toUpperCase(), 222, 686, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_no_ac().toUpperCase(), 222, 671, 0);
		    over.showTextAligned(PdfContentByte.ALIGN_LEFT,rekClient.getMrc_nama().toUpperCase(), 222, 658, 0);
		  //end of Data rekening Penerimaan Manfaat		    
		  //Data benef		   
			if(benef.size()>0){
				int j=0;
				for(int i=0;i<benef.size();i++){
				Benefeciary benefi = (Benefeciary) benef.get(i);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_first(),86, 586-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getSmsaw_birth(),250, 586-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,(benefi.getMsaw_sex()==1?"L":"P"),325, 586-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,benefi.getMsaw_persen().toString(),375, 586-j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,benefi.getLsre_relation().toUpperCase(),480, 586-j, 0);				
				j+=15;}
			}
			//end of data benef
			
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("DELAPAN1TT"),315, 485, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("DELAPAN1DESC"),315, 465, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("DELAPAN2TT"),315, 432, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("DELAPAN12ESC"),315, 412, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("DELAPAN4TT"),315, 305, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("DELAPAN5TT"),315, 295, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("DELAPAN6TT"),315, 282, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("DELAPAN7TT"),315, 267, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("DELAPAN8TT"),315, 257, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("DELAPAN9TT"),315, 242, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SEMBILANPULUHTT"),315, 227, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SEMBILAN1TT"),315, 211, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SEMBILAN2TT"),315, 200, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SEMBILAN3TT"),315, 187, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SEMBILAN4TT"),315, 172, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SEMBILAN5TT"),315, 157, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SEMBILAN6TT"),315, 144, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SEMBILAN7TT"),315, 128, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SEMBILAN8TT"),315, 110, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SEMBILAN9TT"),315, 100, 0);
		 over.endText();
		 
		 over = stamp.getOverContent(3);
		 over.beginText();
		 over.setFontAndSize(times_new_roman,7);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SERATUSDESC"),315, 700, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SERATUS1TT"),315, 667, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SERATUS1DESC"),315, 647, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SERATUS2TT"),315, 627, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SERATUS2DESC"),315, 610, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SERATUS3TT"),315, 570, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SERATUS3DESC"),315, 560, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SERATUS4TTTG")+" "+"CM",315, 545, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,(String)x.get("SERATUS4TTBB")+" "+"KG",315, 535, 0);
		 if(dataUsulan.getLku_id().equals("01")){
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,"RUPIAH",260,475, 0);
		 }else{
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,"US DOLLAR",260,475, 0);
		 }
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLsdbs_name().toUpperCase(),260,462, 0);
		 if(dataUsulan.getDaftaRider().size()>0){
				//int vertikal = 442;
				for(int z=0;z<dataUsulan.getDaftaRider().size();z++){
					//vertikal = vertikal - 12;
					Datarider rider = (Datarider) dataUsulan.getDaftaRider().get(z);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,rider.getLsdbs_name(),260, 447, 0);					
				}
				
			}
		 Double topUpBerkala = 0.;
		 Double topUpTunggal = 0.;
		 Double TotalPremi = 0.;
		 if(inv!=null){
				DetilTopUp daftarTopup = inv.getDaftartopup();
				topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala())?new Double(0):daftarTopup.getPremi_berkala();
				topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal())?new Double(0):daftarTopup.getPremi_tunggal();				
				
		 }
		 TotalPremi = topUpBerkala+topUpTunggal+dataUsulan.getMspr_premium();
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),260, 434, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(TotalPremi)),325, 434, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLscb_pay_mode(),260, 419, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,"CALON PEMEGANG POLIS PERORANGAN",260, 407, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getMste_flag_cc()==0?"TUNAI":(dataUsulan.getMste_flag_cc()==2?"TABUNGAN":"KARTU KREDIT"),260, 392, 0);
		 
		 if(dataUsulan.getMste_flag_cc()==1){
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_holder(),260, 377, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_acc_no(),260, 364, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatDate.toString(accRecur.getMar_expired()),260, 351, 0);
		 }else if(dataUsulan.getMste_flag_cc()==2){			 
			 
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_holder(),260, 375, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,accRecur.getMar_acc_no(),260, 362, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,FormatDate.toString(accRecur.getMar_expired()),260, 349, 0);
		 }
		 if(inv!=null){
			 DetilInvestasi detinv0 = (DetilInvestasi) detInv.get(0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,"Fix Income :"+ (detinv0.getMdu_persen1()==null?Integer.toString(0):detinv0.getMdu_persen1()) + " %",260, 267, 0);
		 }
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),260, 255, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_tsi())),325, 255, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),260, 228, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(dataUsulan.getMspr_premium())),325, 228, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),260, 215, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(topUpBerkala)),325, 213, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,dataUsulan.getLku_symbol(),260, 199, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_RIGHT,FormatNumber.convertToTwoDigit(new BigDecimal(topUpTunggal)),325, 199, 0);
		 
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMcl_first(),185, 159, 0);
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT,agen.getMsag_id(),185, 143, 0);
		 over.endText();
		 
		 over = stamp.getOverContent(4);
		 over.beginText();
		 over.setFontAndSize(times_new_roman,7);
		 Date verified =(Date)x.get("VERIFIED");
		 over.showTextAligned(PdfContentByte.ALIGN_LEFT, FormatDate.getDay2(verified)+" "+FormatDate.getMonth(verified)+" "+FormatDate.getYearFourDigit(verified) , 505, 105, 0);	  
		 over.endText();
	     stamp.close();	
	    
		File l_file = new File(outputName);
		FileInputStream in = null;
		ServletOutputStream ouputStream = null;
		try{
			
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "Inline");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			
			in = new FileInputStream(l_file);
		    ouputStream = response.getOutputStream();
		    
		    IOUtils.copy(in, ouputStream);
		}catch (Exception e) {
			logger.error("ERROR :", e);
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
        
		
		return null;
	}
	
	/*
	 * Lufi(OKT 2015)
	 * Method ini berfungsi untuk mencetak polis all(surat polis,polis,manfaat,alokasi dana, ssu/ssk rider, ttp,endorsment,surat simas card)	 
	 */
	
	public ModelAndView printall(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String success  = ""; 
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List errors = new ArrayList();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj");	
		Integer flagFrom = ServletRequestUtils.getIntParameter(request, "flag", 2);//1 print dan kirim email(khusus produk dari click for life) 2: print all tanpa mengirim email
		Integer mspo_provider=uwManager.selectGetMspoProvider(spaj);
		try{
			this.elionsManager.updatePolicyAndInsertPositionSpaj(spaj, "mspo_date_print", currentUser.getLus_id(), 6, 1, "PRINT POLIS ALL(E-LIONS)", true, currentUser);
			generateReport(request, mspo_provider, 2);
		}catch (Exception e) {			
			 errors.add("Terjadi kesalahan dalam generate Polis");
			 logger.error("ERROR", e);
			 this.bacManager.updateMst_policyEmptyPrintDate(spaj);
		}
	    if(errors.isEmpty()){
	    	Map detBisnis = (Map) elionsManager.selectDetailBisnis(spaj).get(0);
			String businessId = (String) detBisnis.get("BISNIS");
			String lsbs_name = (String) detBisnis.get("LSBS_NAME");
			String lsdbs_name = (String) detBisnis.get("LSDBS_NAME");
			String lsdbs = (String) detBisnis.get("DETBISNIS");
	    	if(products.unitLink(businessId) && !products.stableLink(businessId)) {
				this.elionsManager.updateUlink(3, spaj, df2.format(this.elionsManager.selectMuTglTrans(spaj)));
			}
	    	
		    String cabang = elionsManager.selectCabangFromSpaj(spaj);
		    String dir = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
		    String file = "PolisAll.pdf";
		    if(flagFrom==1){
		    	//kirim email dari click for life
		    }else if(flagFrom==2){
			    Document document = new Document();
				try {
					
					//baca source pdf nya
					Boolean itextReaderOld = true;
					PdfReader reader = null;
					com.itextpdf.text.pdf.PdfReader readerNew = null;
					
					try{
						reader = new PdfReader(dir + "\\" + file, "ekalife".getBytes());
					} catch(IOException ioe1){
						try{
							reader = new PdfReader(dir + "\\" + file);
						}catch(IOException ioe2){
							itextReaderOld=false;
							try {
								readerNew = new com.itextpdf.text.pdf.PdfReader(dir + "\\" + file, "ekalife".getBytes());
							} catch (IOException ioe) {
								try {
									readerNew = new com.itextpdf.text.pdf.PdfReader(dir + "\\" + file);
								} catch (IOException e) {		
									response.setContentType("text/html");					
									ServletOutputStream out = response.getOutputStream();
						    		out.println("<script>alert('File tidak ada. Harap cek kembali data Anda masukkan.');</script>");
						    		out.flush();
						    		document.close();
						    		return null;	
								}
							}
						}
					}					
		//			response header
					response.setHeader("Content-Disposition", "inline;filename=file.pdf");
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					response.setContentType("application/pdf");		
					//"stamp" source pdf nya ke output stream tanpa menambah apa2
		            ServletOutputStream sos = response.getOutputStream();
		            if(itextReaderOld){
		            	PdfStamper stamper = new PdfStamper(reader, sos);
		            	//disable printing apabila ada flag
		    			if(ServletRequestUtils.getIntParameter(request, "readonly", 0) == 1){
		    				stamper.setEncryption(false, null, null, PdfWriter.ALLOW_MODIFY_ANNOTATIONS);
		    			}
		    			stamper.close();	
		            }else{
		            	com.itextpdf.text.pdf.PdfStamper stamperNew = new com.itextpdf.text.pdf.PdfStamper(readerNew, sos);
		            	//disable printing apabila ada flag
		    			if(ServletRequestUtils.getIntParameter(request, "readonly", 0) == 1){
		    				stamperNew.setEncryption(false, null, null, PdfWriter.ALLOW_MODIFY_ANNOTATIONS);
		    			}
		    			stamperNew.close();
		            }
					
		            if(sos!=null){
						sos.flush();
						sos.close();
					}
		            if(reader!=null)reader.close();
		            if(readerNew!=null)readerNew.close();			
				}catch(Exception de) {
					logger.error(de);
		    		ServletOutputStream out = response.getOutputStream();
		    		out.println("<script>alert('Halaman tidak ada. Harap cek kembali data yang bersangkutan.');</script>");
		    		out.flush();
				}
				document.close();
		    }
		    
			return null;
	    }else{
	    	return new ModelAndView("uw/printpolis/printpolis_err", "error", errors);
	    }
	}
	
	//Mark Valentino 20180906
	public ModelAndView printall2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintPolisAllPelengkap ppap = new PrintPolisAllPelengkap(this.getServletContext());
		return ppap.generatePolisAllPelengkap(request, response, elionsManager, uwManager, bacManager, props, products);
		//return null;
	}
	
	//Iga Ukiarwan Perubahan format Spaj Gadget full konven, sio konven, full syh, sio syh
	public ModelAndView espajonlinegadget(HttpServletRequest request,
			HttpServletResponse response)  throws Exception{
		String reg_spaj = request.getParameter("spaj");
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String lcaId = elionsManager.selectCabangFromSpaj(reg_spaj);
		if(lcaId.equals("09") || lcaId.equals("37") || lcaId.equals("52")){
			if (mspo_flag_spaj.equals("3")){
				if(syariah == 1){
					espajonlinegadgetfullsyariah(request, response);
				}else{
					espajonlinegadgetfullkonven(request, response);
				}
			}else if (mspo_flag_spaj.equals("4")){
				if(syariah == 1){
					espajonlinegadgetsiosyariah(request, response);
				}else{
					espajonlinegadgetsiokonven(request, response);
				}
			}
		}
		else{
			espajonlinegadgetexisting(request, response);
		}
		return null;
	}
		
	public ModelAndView espajonlinegadgetfullkonven(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String reg_spaj = request.getParameter("spaj");
		Integer type = null;
		Integer question;
		Date sysdate = elionsManager.selectSysdate();
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		List<String> pdfs = new ArrayList<String>();
		Boolean suksesMerge = false;
		HashMap<String, Object> cmd = new HashMap<String, Object>();
		ArrayList data_answer = new ArrayList();
		Integer index = null;
		Integer index2 = null;
		String spaj = "";
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		
		Map map= uwManager.selectProdInsured(reg_spaj);
		BigDecimal lsbs = (BigDecimal) map.get("LSBS_ID");
		BigDecimal lsdbs = (BigDecimal) map.get("LSDBS_NUMBER");
		String lsbs_id = lsbs.toString();
		String lsdbs_number = lsdbs.toString();
		
		String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj;
		System.out.print(mspo_flag_spaj);
		String dir = props.getProperty("pdf.template.espajonlinegadget");
		OutputStream output;
		PdfReader reader;
		File userDir = new File(props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "GADGET");
		moreInfo.put("Subject", "E-SPAJ ONLINE");

		PdfContentByte over;
		PdfContentByte over2;
		BaseFont times_new_roman = BaseFont.createFont(
				"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
		BaseFont italic = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ariali.ttf",
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

		if (lsbs_id.equals("134") && lsdbs_number.equals("13")) {
			reader = new PdfReader(
					props.getProperty("pdf.template.espajonlinegadget")
							+ "\\spajonlinegadgetfullkonvenBTN.pdf");
			output = new FileOutputStream(exportDirectory + "\\"
					+ "espajonlinegadget_" + reg_spaj + ".pdf");

			spaj = dir + "\\spajonlinegadgetfullkonvenBTN.pdf";
		}else if(cabang.equals("37") || cabang.equals("52")){
			reader = new PdfReader(
					props.getProperty("pdf.template.espajonlinegadget")
							+ "\\spajonlinegadgetfullkonvenAgency.pdf");
			output = new FileOutputStream(exportDirectory + "\\"
					+ "espajonlinegadget_" + reg_spaj + ".pdf");

			spaj = dir + "\\spajonlinegadgetfullkonvenAgency.pdf";
		}
		else {
			reader = new PdfReader(
					props.getProperty("pdf.template.espajonlinegadget")
							+ "\\spajonlinegadgetfullkonven.pdf");
			output = new FileOutputStream(exportDirectory + "\\"
					+ "espajonlinegadget_" + reg_spaj + ".pdf");

			spaj = dir + "\\spajonlinegadgetfullkonven.pdf";
		}
			pdfs.add(spaj);
			suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			String outputName = props.getProperty("pdf.dir.export") + "\\"
					+ cabang + "\\" + reg_spaj + "\\" + "espajonlinegadget_"
					+ reg_spaj + ".pdf";
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
					outputName));

			Pemegang dataPP = elionsManager.selectpp(reg_spaj);
			Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
			PembayarPremi pembPremi = bacManager.selectP_premi(reg_spaj);
			if (pembPremi == null)
				pembPremi = new PembayarPremi();
			AddressBilling addrBill = this.elionsManager
					.selectAddressBilling(reg_spaj);
			Datausulan dataUsulan = this.elionsManager
					.selectDataUsulanutama(reg_spaj);
			dataUsulan.setDaftaRider(elionsManager
					.selectDataUsulan_rider(reg_spaj));
			InvestasiUtama inv = this.elionsManager
					.selectinvestasiutama(reg_spaj);
			Rekening_client rekClient = this.elionsManager
					.select_rek_client(reg_spaj);
			Account_recur accRecur = this.elionsManager
					.select_account_recur(reg_spaj);
			List detInv = this.bacManager.selectdetilinvestasi2(reg_spaj);
			List benef = this.elionsManager.select_benef(reg_spaj);
			List peserta = this.uwManager.select_all_mst_peserta(reg_spaj);
			List dist = this.elionsManager.select_tipeproduk();
			List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
			HashMap spajTemp = (HashMap) listSpajTemp.get(0);
			String idgadget = (String) spajTemp.get("NO_TEMP");
			Map agen = this.bacManager.selectAgenESPAJSimasPrima(reg_spaj);
			HashMap agenAgency= Common.serializableMap(uwManager.selectInfoAgen2(reg_spaj));
			List namaBank = uwManager.namaBank(accRecur.getLbn_id());

			// --Question Full Konven/Syariah
			List rslt = bacManager.selectQuestionareGadget(reg_spaj, 2, 1, 15);	
			List rslt2 = bacManager.selectQuestionareGadget(reg_spaj, 1, 16, 18); 
			List rslt3 = bacManager.selectQuestionareGadget(reg_spaj, 3, 106, 136); 
			List rslt4 = bacManager.selectQuestionareGadget(reg_spaj, 3, 137, 145);
			List rslt5 = bacManager.selectQuestionareGadget(reg_spaj, 3, 146, 155);
			
			//Sio
			List rslt6 = bacManager.selectQuestionareGadget(reg_spaj, 12, 81, 104);
			
			String s_channel = "";
			for (int i = 0; i < dist.size(); i++) {
				HashMap dist2 = (HashMap) dist.get(i);
				Integer i_lstp_id = (Integer) dist2.get("lstp_id");
				if (i_lstp_id.intValue() == dataUsulan.getTipeproduk()
						.intValue()) {
					s_channel = (String) dist2.get("lstp_produk");
				}
			}

			Double d_premiRider = 0.;
			if (dataUsulan.getDaftaRider().size() > 0) {
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					d_premiRider = rider.getMspr_premium();
				}
			}
			Double d_topUpBerkala = new Double(0);
			Double d_topUpTunggal = new Double(0);
			Double d_totalTopup = new Double(0);
			if (inv != null) {
				DetilTopUp daftarTopup = inv.getDaftartopup();
				d_topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala()) ? new Double(
						0) : daftarTopup.getPremi_berkala();
				d_topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal()) ? new Double(
						0) : daftarTopup.getPremi_tunggal();
				d_totalTopup = d_topUpBerkala + d_topUpTunggal;
			}
			Double d_premiExtra = (Common.isEmpty(uwManager
					.selectSumPremiExtra(reg_spaj)) ? 0. : uwManager
					.selectSumPremiExtra(reg_spaj));
			Double d_totalPremi = dataUsulan.getMspr_premium() + d_totalTopup
					+ d_premiRider + d_premiExtra;

			stamp.setMoreInfo(moreInfo);

			// ---------- Data Halaman Pertama ----------

			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman, 8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatString.nomorSPAJ(reg_spaj), 380, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(sysdate), 85, 617, 0);

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 160, 516, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 160, 506, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 160, 496, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 160, 486, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 290, 476, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 290, 467, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 290, 457, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiRider.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiRider))), 290, 447, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 290, 437, 0);
			
			if (cabang.equals("37") || cabang.equals("52")){
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						agenAgency.get("MCL_FIRST").toString().toUpperCase(), 160, 409,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						agenAgency.get("MSAG_ID").toString(), 160, 399, 0);
			}else{
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						agen.get("NM_AGEN").toString().toUpperCase(), 160, 409,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						agen.get("KD_AGEN").toString(), 160, 399, 0);
			}
			

			over.endText();

			// ---------- Data Halaman Ketiga ----------
			over = stamp.getOverContent(3);
			over.beginText();
			
			// String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" +
			// (dataPP.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
			String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_"
					+ ".jpg";
			try {
				Image img = Image.getInstance(ttdPp);
				img.scaleAbsolute(30, 30);
				over.addImage(img, img.getScaledWidth(), 0, 0,
						img.getScaledHeight(), 438, 643);
				over.stroke();

				// String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
				// + (dataTT.getMcl_first().toUpperCase()).replace(" ", "") +
				// ".jpg";
				String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
						+ ".jpg";
				if (dataTT.getMste_age() < 17 || dataPP.getLsre_id() == 1)
					ttdTu = ttdPp;
				Image img2 = Image.getInstance(ttdTu);
				img2.scaleAbsolute(30, 30);
				over.addImage(img2, img2.getScaledWidth(), 0, 0,
						img2.getScaledHeight(), 438, 593);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('TTD Pemegang Polis / Tertanggung Utama Tidak Ditemukan');window.close();</script>");
				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(dataPP.getMspo_spaj_date()), 370, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataPP
					.getMcl_first().toUpperCase(), 295, 655, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataTT
					.getMcl_first().toUpperCase(), 295, 605, 0);
			if (peserta.size() > 0) {
				Integer vertikal = 655;
				for (int i = 0; i < peserta.size(); i++) {
					vertikal = vertikal - 50;
					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					if (pesertaPlus.getFlag_jenis_peserta() > 0){
						over.showTextAligned(PdfContentByte.ALIGN_CENTER,
								pesertaPlus.getNama().toUpperCase(), 290, vertikal,
								0);
						vertikal = vertikal + 2;
					}
				}
			}
			if (cabang.equals("37") || cabang.equals("52")){
				try {
					String ttdAgen = exportDirectory + "\\" + idgadget
							+ "_TTD_AGEN_" + ".jpg";
					Image img5 = Image.getInstance(ttdAgen);
					img5.scaleAbsolute(30, 30);
					over.addImage(img5, img5.getScaledWidth(), 0, 0,
							img5.getScaledHeight(), 120, 280);
					over.stroke();
				} catch (FileNotFoundException e) {
					logger.error("ERROR :", e);
//					ServletOutputStream sos = response.getOutputStream();
//					sos.println("<script>alert('TTD Agen Tidak Ditemukan');window.close();</script>");
//					sos.close();
				}
				
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agenAgency.get("MCL_FIRST")) ? "-" : 
							agenAgency.get("MCL_FIRST").toString().toUpperCase(),100, 260,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agenAgency.get("MSAG_ID")) ? "-" : 
							agenAgency.get("MSAG_ID").toString().toUpperCase(),100, 250,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agenAgency.get("TEAM")) ? "-" : 
							agenAgency.get("TEAM").toString().toUpperCase(),100, 240,0);
			}else{
				try {
					String ttdPenutup = exportDirectory + "\\" + idgadget
							+ "_TTD_PENUTUP_" + ".jpg";
					Image img3 = Image.getInstance(ttdPenutup);
					img3.scaleAbsolute(30, 30);
					over.addImage(img3, img3.getScaledWidth(), 0, 0,
							img3.getScaledHeight(), 120, 280);
					over.stroke();
				} catch (FileNotFoundException e) {
					logger.error("ERROR :", e);
//					ServletOutputStream sos = response.getOutputStream();
//					sos.println("<script>alert('TTD Agen Penutup Tidak Ditemukan');window.close();</script>");
//					sos.close();
				}

				try {
					String ttdReff = exportDirectory + "\\" + idgadget
							+ "_TTD_REF_" + ".jpg";
					Image img4 = Image.getInstance(ttdReff);
					img4.scaleAbsolute(30, 30);
					over.addImage(img4, img4.getScaledWidth(), 0, 0,
							img4.getScaledHeight(), 290, 280);
					over.stroke();
				} catch (FileNotFoundException e) {
					logger.error("ERROR :", e);
//					ServletOutputStream sos = response.getOutputStream();
//					sos.println("<script>alert('TTD Agen Refferal Tidak Ditemukan');window.close();</script>");
//					sos.close();
				}
				
				try {
					String ttdAgen = exportDirectory + "\\" + idgadget
							+ "_TTD_AGEN_" + ".jpg";
					Image img5 = Image.getInstance(ttdAgen);
					img5.scaleAbsolute(30, 30);
					over.addImage(img5, img5.getScaledWidth(), 0, 0,
							img5.getScaledHeight(), 440, 280);
					over.stroke();
				} catch (FileNotFoundException e) {
					logger.error("ERROR :", e);
//					ServletOutputStream sos = response.getOutputStream();
//					sos.println("<script>alert('TTD Agen Tidak Ditemukan');window.close();</script>");
//					sos.close();
				}
				over.setFontAndSize(times_new_roman, 6);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agen.get("NM_PENUTUP")) ? "-" : 
							agen.get("NM_PENUTUP").toString().toUpperCase(),100, 260,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agen.get("KD_PENUTUP")) ? "-" : 
							agen.get("KD_PENUTUP").toString().toUpperCase(),100, 250,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agen.get("CB_PENUTUP")) ? "-" : 
							agen.get("CB_PENUTUP").toString().toUpperCase(),100, 240,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agen.get("NM_REFFERAL")) ? "-" : 
							agen.get("NM_REFFERAL").toString().toUpperCase(),270, 260,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agen.get("KD_REFFERAL")) ? "-" : 
							agen.get("KD_REFFERAL").toString().toUpperCase(),270, 250,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agen.get("CB_REFFERAL")) ? "-" : 
							agen.get("CB_REFFERAL").toString().toUpperCase(),270, 240,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agen.get("NM_AGEN")) ? "-" : 
							agen.get("NM_AGEN").toString().toUpperCase(),440, 260,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agen.get("KD_AGEN")) ? "-" : 
							agen.get("KD_AGEN").toString().toUpperCase(),440, 250,0);
				over.showTextAligned(
						PdfContentByte.ALIGN_LEFT,
						Common.isEmpty(agen.get("CB_AGEN")) ? "-" : 
							agen.get("CB_AGEN").toString().toUpperCase(),440, 240,0);
			}
			over.endText();

			// //---------- Data Halaman Keempat ----------
			over = stamp.getOverContent(4);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 250, 725, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_gelar()) ? "-" : dataPP
							.getMcl_gelar(), 250, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_mother(), 250, 705, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsne_note(), 250, 695, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_green_card()) ? "TIDAK"
							: dataPP.getMcl_green_card(), 250, 685, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLside_name(), 250, 675, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_no_identity(), 250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataPP
									.getMspe_no_identity_expired()), 250, 656,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataPP.getMspe_date_birth()),
					250, 646, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspo_age() + " Tahun", 250, 636, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sex2().toUpperCase(), 250, 626, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					617, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsag_name(), 250, 607, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsed_name(), 250, 596, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_company_name()) ? "-" : dataPP
							.getMcl_company_name(), 250, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMkl_kerja(), 250, 568, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKerjab(),
					250, 559, 0);
			int monyong = 0;
			String[] uraian_tugas = StringUtil.pecahParagraf(dataTT
					.getMkl_kerja_ket().toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 549 - monyong, 0);
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getMkl_kerja_ket())?"-":dataPP.getMkl_kerja_ket(),
			// 250, 549, 0);
			monyong = 0;
			if(!Common.isEmpty(dataTT.getAlamat_kantor())){
				String[] alamat = StringUtil.pecahParagraf(dataTT.getAlamat_kantor().toUpperCase(), 75);
	        	if(!Common.isEmpty(alamat)){
		        	for(int i=0; i<alamat.length; i++) {
		        		monyong = 7 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
								529 - monyong, 0);
		        	}
	        	}
			}
//			monyong = 0;
//			String[] alamat = StringUtil.pecahParagraf(dataPP
//					.getAlamat_kantor().toUpperCase(), 70);
//			for (int i = 0; i < alamat.length; i++) {
//				monyong = 7 * i;
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
//						529 - monyong, 0);
//			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getAlamat_kantor())?"-":dataPP.getAlamat_kantor(),
			// 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_kantor()) ? "-" : dataPP
							.getKota_kantor(), 250, 509, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_kantor()) ? "-" : dataPP
							.getKd_pos_kantor(), 250, 500, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_kantor()) ? "-" : dataPP
							.getTelpon_kantor(), 250, 490, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_kantor2())?"-":dataPP.getTelpon_kantor2(),
			// 250, 505, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 480, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_rumah()) ? "-" : dataPP
							.getAlamat_rumah(), 250, 470, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_rumah()) ? "-" : dataPP
							.getKota_rumah(), 250, 460, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_rumah()) ? "-" : dataPP
							.getKd_pos_rumah(), 250, 451, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
							.getTelpon_rumah(), 250, 441, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
			// 250, 445, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 432, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_tpt_tinggal()) ? "-"
							: dataPP.getAlamat_tpt_tinggal(), 250, 422, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_tpt_tinggal()) ? "-" : dataPP
							.getKota_tpt_tinggal(), 250, 412, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_tpt_tinggal()) ? "-"
							: dataPP.getKd_pos_tpt_tinggal(), 250, 402, 0);
//			over.showTextAligned(
//					PdfContentByte.ALIGN_LEFT,
//					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
//							.getTelpon_rumah(), 250, 393, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			 Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
					 250, 393, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 383, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_address(), 250, 373, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getKota(), 250, 353, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_zip_code(), 250, 343, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_phone1(), 250, 334, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_fax1(), 250, 323, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getNo_hp()) ? "-" : dataPP.getNo_hp(),
					250, 313, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getEmail()) ? "-" : dataPP.getEmail(),
					250, 303, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_npwp()) ? "-" : dataPP
							.getMcl_npwp(), 250, 294, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_penghasilan()) ? "-" : dataPP
							.getMkl_penghasilan(), 250, 285, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_pendanaan()) ? "-" : dataPP
							.getMkl_pendanaan(), 250, 273, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_tujuan()) ? "-" : dataPP
							.getMkl_tujuan(), 250, 264, 0);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getLsre_relation().toUpperCase(), 250, 234, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 250, 215, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_gelar()) ? "-" : dataTT
							.getMcl_gelar(), 250, 206, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_mother(), 250, 196, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsne_note(), 250, 186, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_green_card()) ? "TIDAK"
							: dataTT.getMcl_green_card(), 250, 176, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLside_name(), 250, 166, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_no_identity(), 250, 156, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataTT
									.getMspe_no_identity_expired()), 250, 146,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataTT.getMspe_date_birth()),
					250, 137, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMste_age() + " Tahun", 250, 127, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sex2().toUpperCase(), 250, 118, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					108, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsag_name(), 250, 98, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsed_name(), 250, 88, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_company_name()) ? "-" : dataTT
							.getMcl_company_name(), 250, 78, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMkl_kerja(), 250, 58, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getKerjab(),
					250, 49, 0);
			over.endText();

			//
			// //---------- Data Halaman Kelima ----------
			over = stamp.getOverContent(5);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			monyong = 0;
			uraian_tugas = StringUtil.pecahParagraf(dataTT.getMkl_kerja_ket()
					.toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 734 - monyong, 0);
			}

			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getMkl_kerja_ket())?"-":dataTT.getMkl_kerja_ket(),
			// 250, 734, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_kantor()) ? "-" : dataTT
							.getAlamat_kantor(), 250, 714, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_kantor()) ? "-" : dataTT
							.getKota_kantor(), 250, 695, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_kantor()) ? "-" : dataTT
							.getKd_pos_kantor(), 250, 685, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_kantor()) ? "-" : dataTT
							.getTelpon_kantor(), 250, 675, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_kantor2())?"-":dataTT.getTelpon_kantor2(),
			// 153, 105, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_rumah()) ? "-" : dataTT
							.getAlamat_rumah(), 250, 655, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_rumah()) ? "-" : dataTT
							.getKota_rumah(), 250, 645, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_rumah()) ? "-" : dataTT
							.getKd_pos_rumah(), 250, 635, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
							.getTelpon_rumah(), 250, 625, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah2())?"-":dataTT.getTelpon_rumah2(),
			// 153, 46, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_tpt_tinggal()) ? "-"
							: dataTT.getAlamat_tpt_tinggal(), 250, 607, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_tpt_tinggal()) ? "-" : dataTT
							.getKota_tpt_tinggal(), 250, 597, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_tpt_tinggal()) ? "-"
							: dataTT.getKd_pos_tpt_tinggal(), 250, 587, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah())?"-":dataTT.getTelpon_rumah(),
			// 250, 597, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
							.getTelpon_rumah2(), 250, 578, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 567, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(addrBill.getMsap_address())?"-":addrBill.getMsap_address(),
			// 208, 739, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getNo_hp()) ? "-" : dataTT.getNo_hp(),
					250, 557, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getEmail()) ? "-" : dataTT.getEmail(),
					250, 547, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_npwp()) ? "-" : dataTT
							.getMcl_npwp(), 250, 537, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_penghasilan()) ? "-" : dataTT
							.getMkl_penghasilan(), 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_pendanaan()) ? "-" : dataTT
							.getMkl_pendanaan(), 250, 519, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_tujuan()) ? "-" : dataTT
							.getMkl_tujuan(), 250, 509, 0);
			//
			// //Data Pembayar Premi
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getRelation_payor()) ? "-"
							: pembPremi.getRelation_payor(), 250, 478, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(pembPremi.getNama_pihak_ketiga()) ? "-"
					: pembPremi.getNama_pihak_ketiga(), 250, 468, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getKewarganegaraan()) ? "-"
							: pembPremi.getKewarganegaraan(), 250, 458, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getAlamat_lengkap()) ? "-"
							: pembPremi.getAlamat_lengkap(), 250, 450, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_rumah()) ? "-" : pembPremi
							.getTelp_rumah(), 250, 440, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_kantor()) ? "-"
							: pembPremi.getTelp_kantor(), 250, 430, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getEmail()) ? "-" : pembPremi
							.getEmail(), 250, 420, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTempat_lahir()) ? "-"
							: (pembPremi.getTempat_lahir() + ", " + FormatDate.toString(pembPremi
									.getMspe_date_birth_3rd_pendirian())), 250,
					410, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getMkl_kerja()) ? "-" : pembPremi
							.getMkl_kerja(), 250, 401, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 392, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 382, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 372, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getNo_npwp()) ? "-" : pembPremi
							.getNo_npwp(), 250, 362, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getSumber_dana()) ? "-"
							: pembPremi.getSumber_dana(), 250, 352, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTujuan_dana_3rd()) ? "-"
							: pembPremi.getTujuan_dana_3rd(), 250, 342, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 332, 0);
			//
			// //Data Usulan
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					s_channel.toUpperCase(), 250, 291, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 281, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 271, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspr_ins_period() + " Tahun", 250, 261, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspo_pay_period() + " Tahun", 250, 251, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataUsulan.getMspo_installment()) ? "-"
					: dataUsulan.getMspo_installment() + "", 250, 242, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLscb_pay_mode(), 250, 232, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new
			// BigDecimal(dataUsulan.getMspr_premium())), 250, 286, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 250, 222, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
							.getMste_flag_cc() == 2 ? "TABUNGAN"
							: "KARTU KREDIT"), 250, 211, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_beg_date()),
					250, 202, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_end_date()),
					250, 193, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// FormatDate.toIndonesian(dataUsulan.getLsdbs_number()>800?dataUsulan.getLsdbs_name():"-"),
			// 250, 237, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 221,
			// 0);

			if (dataUsulan.getDaftaRider().size() > 0) {
				Integer j = 0;
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							rider.getLsdbs_name(), 270, 163 - j, 0);
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getLku_symbol()
									+ " "
									+ FormatNumber
											.convertToTwoDigit(new BigDecimal(
													rider.getMspr_tsi())), 440,
													163 - j, 0);
					j += 10;
				}
			}

			// //Data Investasi
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 250, 97, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 250, 87, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 250, 77, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiExtra.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiExtra))), 250, 68, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 250, 58, 0);
			Double d_jmlinves = new Double(0);
			String s_jnsinves = "";
			for (int i = 0; i < detInv.size(); i++) {
				DetilInvestasi detInves = (DetilInvestasi) detInv.get(i);
				d_jmlinves = d_jmlinves + detInves.getMdu_jumlah1();
				s_jnsinves = s_jnsinves
						+ detInves.getLji_invest1().toUpperCase() + " "
						+ detInves.getMdu_persen1() + "%";
				if (i != (detInv.size() - 1))
					s_jnsinves = s_jnsinves + ", ";
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new BigDecimal(d_jmlinves)), 208,
			// 183, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, s_jnsinves, 250,
					47, 0);
			over.endText();

			// ---------- Data Halaman Keenam ----------

			over = stamp.getOverContent(6);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			// //Data Rekening
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getLsbp_nama(), 250, 724, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_cabang(), 250, 714, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_no_ac(), 250, 704, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_nama(), 250, 694, 0);

			if (dataTT.getMste_flag_cc() == 1 || dataTT.getMste_flag_cc() == 2) {
				if (accRecur != null) {
					String bank_pusat = "";
					String bank_cabang = "";

					if (!namaBank.isEmpty()) {
						HashMap m = (HashMap) namaBank.get(0);
						bank_pusat = (String) m.get("LSBP_NAMA");
						bank_cabang = (String) m.get("LBN_NAMA");
					}
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getMste_flag_cc() == 0 ? "TUNAI"
									: (dataUsulan.getMste_flag_cc() == 2 ? "TABUNGAN"
											: "KARTU KREDIT"), 250, 631, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							Common.isEmpty(bank_pusat) ? "-"
									: bank_pusat.toUpperCase()/*
															 * accRecur.getLbn_nama
															 * ().toUpperCase()
															 */, 250, 621, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(bank_cabang) ? "-"
							: bank_cabang.toUpperCase()/*
																 * accRecur.
																 * getLbn_nama
																 * ().
																 * toUpperCase()
																 */, 250, 611,
							0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_acc_no()) ? "-" : accRecur
							.getMar_acc_no().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_holder()) ? "-" : accRecur
							.getMar_holder().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 592, 0);
				}
			} else {
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 631,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 621,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 611,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 601,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 592,
						0);
			}

			if (peserta.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < peserta.size(); i++) {

					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					if (pesertaPlus.getFlag_jenis_peserta() > 0){
						int pesertatambahan = 0;
						String[] nama = StringUtil.pecahParagraf(pesertaPlus.getNama().toUpperCase(), 20);
						for (int z = 0; z < nama.length; z++) {
							pesertatambahan = 7 * z;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									nama[z], 76, 517 - pesertatambahan, 0);
						}
//						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
//								pesertaPlus.getNama().toUpperCase(), 76, 517 - j,
//								0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getLsre_relation(), 190, 517 - j, 0);
						over.showTextAligned(
								PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getTinggi() + "/"
										+ pesertaPlus.getBerat(), 260, 517 - j, 0);
						String[] kerjaan = StringUtil.pecahParagraf(pesertaPlus.getKerja().toUpperCase(), 20);
						for (int z = 0; z < kerjaan.length; z++) {
							pesertatambahan = 7 * z;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									kerjaan[z], 290, 517 - pesertatambahan, 0);
						}
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getSex(), 375, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								FormatDate.toString(pesertaPlus.getTgl_lahir()),
								430, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								dataPP.getLsne_note(), 480, 517 - j, 0);
						j += 10;
					}	
				}
			}
			if (benef.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < benef.size(); i++) {
					Benefeciary benefit = (Benefeciary) benef.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_first(), 55, 380 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getSmsaw_birth(), 200, 380 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_sex() == 1 ? "LAKI-LAKI" : "PEREMPUAN",
							270, 380 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_persen() + "%", 331, 380 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, benefit
							.getLsre_relation().toUpperCase(), 375, 380 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							dataPP.getLsne_note(), 475, 380 - j, 0);
					j += 10;
				}
			}
			// -----------data tertanggung-----------
			String jawab = "";
			if (mspo_flag_spaj.equals("3")){
				if (rslt.size() > 0) {
					Integer j = 0;
					for (int i = 0; i < rslt.size(); i++) {
						MstQuestionAnswer ans = (MstQuestionAnswer) rslt.get(i);
						if (ans.getQuestion_id() == 1) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 308, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 308, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 308, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 308, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										290, 0);
							}
						}
						if (ans.getQuestion_id() == 2) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 279, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 279, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 279, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 279, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										270, 0);
							}
						}
						if (ans.getQuestion_id() == 3) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 260, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										230, 0);
							}
						}
						if (ans.getQuestion_id() == 4) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 220, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										190, 0);
							}
						}
						if (ans.getQuestion_id() == 5) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 180, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 180, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 180, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 180, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										163, 0);
							}
						}
						if (ans.getQuestion_id() == 6) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 153, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										143, 0);
							}
						}	
						if (ans.getQuestion_id() == 7) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 113, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										84, 0);
							}
						}
						if (ans.getQuestion_id() == 8) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 73, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 73, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 73, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 73, 0);
							}
						}
						if (ans.getQuestion_id() == 9) {
							if (ans.getOption_type() == 0
									&& ans.getOption_order() == 1) {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 115, 62, 0);
							} else if (ans.getOption_type() == 0
									&& ans.getOption_order() == 2) {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 160, 52, 0);
							}
						}
					}	
			}
			}
			over.endText();

			// ------------Halaman tujuh-----------
			over = stamp.getOverContent(7);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			if (rslt.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt.get(i);
					if (ans.getQuestion_id() == 10) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 735, 0);
						}
					}
					if (ans.getQuestion_id() == 11) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 715, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 715, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 715, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 715, 0);
						}
					}
					if (ans.getQuestion_id() == 12) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 695, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 695, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 695, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 695, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									685, 0);
						}
					}
					if (ans.getQuestion_id() == 13) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 667, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 667, 0);
						}
					}
					if (ans.getQuestion_id() == 15) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 645, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 645, 0);
						}
					}
				}
			}

			// -------------data pemegang polis--------------
			if (rslt2.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt2.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt2.get(i);
					if (ans.getQuestion_id() == 16) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 615, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 615, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 615, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 615, 0);
						} else if (ans.getOption_type() == 0
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									595, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									595, 0);
						}
					}
					if (ans.getQuestion_id() == 17) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 587, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 587, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 587, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 587, 0);
						} else if (ans.getOption_type() == 0
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									577, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									577, 0);
						}
					}
					if (ans.getQuestion_id() == 18) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 558, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 558, 0);
						}
					}
				}
			}
			
			over.endText();
			
			
			// ------------Halaman kedelapan-----------	
			
			over = stamp.getOverContent(8);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			if (rslt3.size() > 0 ) {
				Integer j = 0;
				for (int i = 0; i < rslt3.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt3.get(i);
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 700 - j, 0);
							// --pp
							
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 681 - j, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 681 - j, 0);
						}else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									395, 683 - j, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									395, 683 - j, 0);
						}
						
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);	
								// --tt1/pt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 
										&& ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											425, 685 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											425, 685 - j, 0);
								}
								// --tt2/pt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 
										&& ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											463, 688 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											463, 688 - j, 0);
								}
								// --tt3/pt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 
										&& ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											493, 690 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											493, 690 - j, 0);
								}
								// --tt4/pt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 
										&& ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											540, 692 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											540, 692 - j, 0);
								}
								
							}
						}else if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
										395, 680 - j, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
										395, 680 - j, 0);
							}
						}
					
					j += 1;
				}
			}

			if (rslt4.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt4.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt4.get(i);
					if (ans.getQuestion_id() == 137) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 700, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 356, 0);
							
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 356, 0);
						}
						// --tu/pu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 356, 0);
						}
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 356, 0);
						}
						 
						 else if (ans.getOption_group() == 0) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									320, 0);
						}
						
						
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 356, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 356, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 356, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 356, 0);
								}
								
							}
						}else if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 356, 0);
								
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 356, 0);
							}
						}

					}
							
					if (ans.getQuestion_id() == 139) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 288, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 288, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 288, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 288, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								// --tt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 288, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 288, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 &&ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 288, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 288, 0);
								}
							}
						}else if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 288, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 288, 0);
							}
						}
						
					}
					// j += 3;
					if (ans.getQuestion_id() >= 140
							&& ans.getQuestion_id() <= 143) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 700, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 323 - j, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 323 - j, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 327 - j, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 327 - j, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 331 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 331 - j, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 335 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 335 - j, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 339 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 339 - j, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 343 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 343 - j, 0);
								}
							}
							}else if (dataTT.getLsre_id()==1){
								 if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 395, 324 - j, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 395, 324 - j, 0);
									}
							}
						}
						
					j += 2;
					if (ans.getQuestion_id() == 144) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 188, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 188, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 188, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 188, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 188, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 188, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 188, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 188, 0);
								}
							}
							}else if (dataTT.getLsre_id()==1){
								if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 186, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 186, 0);
								}
							}
						}
						
							
					if (ans.getQuestion_id() == 145) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 178, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 168, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 168, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 168, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 168, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 168, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 168, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 168, 0);
								}
							}
						}else  if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 166, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 166, 0);
							}
						}
					}
				}
			}

			over.endText();

			// --------------Halaman sembilan--------------//
			over = stamp.getOverContent(9);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			if (rslt5.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt5.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt5.get(i);
					if (ans.getQuestion_id() == 146) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 735, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 735, 0);
						}
						// --tu
						else if ( ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 735, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 735, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 735, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 735, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 735, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 735, 0);
								}
							}
						}else  if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 735, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 735, 0);
							}
						}
					}
					// j += 3;
					if (ans.getQuestion_id() == 147) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 697,
								0);
					} else if (ans.getQuestion_id() == 148) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 667,
								0);
					} else if (ans.getQuestion_id() == 149) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 637,
								0);
					} else if (ans.getQuestion_id() == 150) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 607,
								0);
					} else if (ans.getQuestion_id() == 151) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 578,
								0);
					}
					if (ans.getQuestion_id() == 152) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 588, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 588, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 588, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 588, 0);
						}
						
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);						
								// --tt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 588, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 588, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 588, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 588, 0);
								}
							}
						}else if(ans.getAnswer2() != null && dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 588, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 588, 0);
							}
						}
							
					}
					if (ans.getQuestion_id() == 153) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 568, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 568, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 568, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 568, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 568, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 568, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 568, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 568, 0);
								}
							}
						}else if(ans.getAnswer2() != null && dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 568, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 568, 0);
							}
						}
						
					}
					if (ans.getQuestion_id() == 154) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 548, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 548, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 548, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 548, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 548, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 548, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 548, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 548, 0);
								}
							}
						}else if(ans.getAnswer2() != null && dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 548, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 548, 0);
							}
						}
						
					}
					if (ans.getQuestion_id() == 155) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 528, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 528, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 528, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 528, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 528, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 528, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 528, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 528, 0);
								}
							}
						}else if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 528, 0);
							} else if ( ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 528, 0);
							}
						}	
					}
				}
				j += 2;
			}
			over.endText();
			stamp.close();
			
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try {

				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");

				in = new FileInputStream(l_file);
				ouputStream = response.getOutputStream();

				IOUtils.copy(in, ouputStream);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (ouputStream != null) {
						ouputStream.flush();
						ouputStream.close();
					}
				} catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		return null;
	}
	
	public ModelAndView espajonlinegadgetsiokonven(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String reg_spaj = request.getParameter("spaj");
		Integer type = null;
		Integer question;
		Date sysdate = elionsManager.selectSysdate();
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		List<String> pdfs = new ArrayList<String>();
		Boolean suksesMerge = false;
		HashMap<String, Object> cmd = new HashMap<String, Object>();
		ArrayList data_answer = new ArrayList();
		Integer index = null;
		Integer index2 = null;
		String spaj = "";
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj;
		System.out.print(mspo_flag_spaj);
		String dir = props.getProperty("pdf.template.espajonlinegadget");
		OutputStream output;
		PdfReader reader;
		File userDir = new File(props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "GADGET");
		moreInfo.put("Subject", "E-SPAJ ONLINE");

		PdfContentByte over;
		PdfContentByte over2;
		BaseFont times_new_roman = BaseFont.createFont(
				"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
		BaseFont italic = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ariali.ttf",
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

				reader = new PdfReader(
						props.getProperty("pdf.template.espajonlinegadget")
								+ "\\spajonlinegadgetsiokonven.pdf");
				output = new FileOutputStream(exportDirectory + "\\"
						+ "espajonlinegadget_" + reg_spaj + ".pdf");

				spaj = dir + "\\spajonlinegadgetsiokonven.pdf";
			
			pdfs.add(spaj);
			suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			String outputName = props.getProperty("pdf.dir.export") + "\\"
					+ cabang + "\\" + reg_spaj + "\\" + "espajonlinegadget_"
					+ reg_spaj + ".pdf";
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
					outputName));

			Pemegang dataPP = elionsManager.selectpp(reg_spaj);
			Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
			PembayarPremi pembPremi = bacManager.selectP_premi(reg_spaj);
			if (pembPremi == null)
				pembPremi = new PembayarPremi();
			AddressBilling addrBill = this.elionsManager
					.selectAddressBilling(reg_spaj);
			Datausulan dataUsulan = this.elionsManager
					.selectDataUsulanutama(reg_spaj);
			dataUsulan.setDaftaRider(elionsManager
					.selectDataUsulan_rider(reg_spaj));
			InvestasiUtama inv = this.elionsManager
					.selectinvestasiutama(reg_spaj);
			Rekening_client rekClient = this.elionsManager
					.select_rek_client(reg_spaj);
			Account_recur accRecur = this.elionsManager
					.select_account_recur(reg_spaj);
			List detInv = this.bacManager.selectdetilinvestasi2(reg_spaj);
			List benef = this.elionsManager.select_benef(reg_spaj);
			List peserta = this.uwManager.select_all_mst_peserta(reg_spaj);
			List dist = this.elionsManager.select_tipeproduk();
			List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
			HashMap spajTemp = (HashMap) listSpajTemp.get(0);
			String idgadget = (String) spajTemp.get("NO_TEMP");
			Map agen = this.bacManager.selectAgenESPAJSimasPrima(reg_spaj);
			List namaBank = uwManager.namaBank(accRecur.getLbn_id());

			// --Question Full Konven/Syariah
			List rslt = bacManager.selectQuestionareGadget(reg_spaj, 2, 1, 15);	
			List rslt2 = bacManager.selectQuestionareGadget(reg_spaj, 1, 16, 18); 
			List rslt3 = bacManager.selectQuestionareGadget(reg_spaj, 3, 106, 136); 
			List rslt4 = bacManager.selectQuestionareGadget(reg_spaj, 3, 137, 145);
			List rslt5 = bacManager.selectQuestionareGadget(reg_spaj, 3, 146, 155);
			
			//Sio
			List rslt6 = bacManager.selectQuestionareGadget(reg_spaj, 12, 81, 104);
			
			String s_channel = "";
			for (int i = 0; i < dist.size(); i++) {
				HashMap dist2 = (HashMap) dist.get(i);
				Integer i_lstp_id = (Integer) dist2.get("lstp_id");
				if (i_lstp_id.intValue() == dataUsulan.getTipeproduk()
						.intValue()) {
					s_channel = (String) dist2.get("lstp_produk");
				}
			}

			Double d_premiRider = 0.;
			if (dataUsulan.getDaftaRider().size() > 0) {
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					d_premiRider = rider.getMspr_premium();
				}
			}
			Double d_topUpBerkala = new Double(0);
			Double d_topUpTunggal = new Double(0);
			Double d_totalTopup = new Double(0);
			if (inv != null) {
				DetilTopUp daftarTopup = inv.getDaftartopup();
				d_topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala()) ? new Double(
						0) : daftarTopup.getPremi_berkala();
				d_topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal()) ? new Double(
						0) : daftarTopup.getPremi_tunggal();
				d_totalTopup = d_topUpBerkala + d_topUpTunggal;
			}
			Double d_premiExtra = (Common.isEmpty(uwManager
					.selectSumPremiExtra(reg_spaj)) ? 0. : uwManager
					.selectSumPremiExtra(reg_spaj));
			Double d_totalPremi = dataUsulan.getMspr_premium() + d_totalTopup
					+ d_premiRider + d_premiExtra;

			stamp.setMoreInfo(moreInfo);

			// ---------- Data Halaman Pertama ----------

			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman, 8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatString.nomorSPAJ(reg_spaj), 380, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(sysdate), 85, 617, 0);

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 160, 516, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 160, 506, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 160, 496, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 160, 486, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 290, 476, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 290, 467, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 290, 457, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiRider.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiRider))), 290, 447, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 290, 437, 0);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					agen.get("NM_AGEN").toString().toUpperCase(), 160, 409,
					0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					agen.get("KD_AGEN").toString(), 160, 399, 0);

			over.endText();

			// ---------- Data Halaman Ketiga ----------
			over = stamp.getOverContent(3);
			over.beginText();
			
			// String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" +
			// (dataPP.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
			String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_"
					+ ".jpg";
			try {
				Image img = Image.getInstance(ttdPp);
				img.scaleAbsolute(30, 30);
				over.addImage(img, img.getScaledWidth(), 0, 0,
						img.getScaledHeight(), 438, 643);
				over.stroke();

				// String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
				// + (dataTT.getMcl_first().toUpperCase()).replace(" ", "") +
				// ".jpg";
				String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
						+ ".jpg";
				if (dataTT.getMste_age() < 17 || dataPP.getLsre_id() == 1)
					ttdTu = ttdPp;
				Image img2 = Image.getInstance(ttdTu);
				img2.scaleAbsolute(30, 30);
				over.addImage(img2, img2.getScaledWidth(), 0, 0,
						img2.getScaledHeight(), 438, 593);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('TTD Pemegang Polis / Tertanggung Utama Tidak Ditemukan');window.close();</script>");
				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(dataPP.getMspo_spaj_date()), 370, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataPP
					.getMcl_first().toUpperCase(), 295, 655, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataTT
					.getMcl_first().toUpperCase(), 295, 605, 0);
			if (peserta.size() > 0) {
				Integer vertikal = 605;
				for (int i = 0; i < peserta.size(); i++) {
					vertikal = vertikal - 50;
					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					if (pesertaPlus.getFlag_jenis_peserta() > 0){
						over.showTextAligned(PdfContentByte.ALIGN_CENTER,
								pesertaPlus.getNama().toUpperCase(), 290, vertikal,
								0);
						vertikal = vertikal + 2;
					}
					
				}
			}

			try {
				String ttdPenutup = exportDirectory + "\\" + idgadget
						+ "_TTD_PENUTUP_" + ".jpg";
				Image img3 = Image.getInstance(ttdPenutup);
				img3.scaleAbsolute(30, 30);
				over.addImage(img3, img3.getScaledWidth(), 0, 0,
						img3.getScaledHeight(), 120, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Penutup Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			try {
				String ttdReff = exportDirectory + "\\" + idgadget
						+ "_TTD_REF_" + ".jpg";
				Image img4 = Image.getInstance(ttdReff);
				img4.scaleAbsolute(30, 30);
				over.addImage(img4, img4.getScaledWidth(), 0, 0,
						img4.getScaledHeight(), 290, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Refferal Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}
			
			try {
				String ttdAgen = exportDirectory + "\\" + idgadget
						+ "_TTD_AGEN_" + ".jpg";
				Image img5 = Image.getInstance(ttdAgen);
				img5.scaleAbsolute(30, 30);
				over.addImage(img5, img5.getScaledWidth(), 0, 0,
						img5.getScaledHeight(), 440, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_PENUTUP")) ? "-" : 
						agen.get("NM_PENUTUP").toString().toUpperCase(),100, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_PENUTUP")) ? "-" : 
						agen.get("KD_PENUTUP").toString().toUpperCase(),100, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_PENUTUP")) ? "-" : 
						agen.get("CB_PENUTUP").toString().toUpperCase(),100, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_REFFERAL")) ? "-" : 
						agen.get("NM_REFFERAL").toString().toUpperCase(),270, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_REFFERAL")) ? "-" : 
						agen.get("KD_REFFERAL").toString().toUpperCase(),270, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_REFFERAL")) ? "-" : 
						agen.get("CB_REFFERAL").toString().toUpperCase(),270, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_AGEN")) ? "-" : 
						agen.get("NM_AGEN").toString().toUpperCase(),440, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_AGEN")) ? "-" : 
						agen.get("KD_AGEN").toString().toUpperCase(),440, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_AGEN")) ? "-" : 
						agen.get("CB_AGEN").toString().toUpperCase(),440, 240,0);
			over.endText();

			// //---------- Data Halaman Keempat ----------
			over = stamp.getOverContent(4);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 250, 725, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_gelar()) ? "-" : dataPP
							.getMcl_gelar(), 250, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_mother(), 250, 705, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsne_note(), 250, 695, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_green_card()) ? "TIDAK"
							: dataPP.getMcl_green_card(), 250, 685, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLside_name(), 250, 675, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_no_identity(), 250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataPP
									.getMspe_no_identity_expired()), 250, 656,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataPP.getMspe_date_birth()),
					250, 646, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspo_age() + " Tahun", 250, 636, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sex2().toUpperCase(), 250, 626, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					617, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsag_name(), 250, 607, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsed_name(), 250, 596, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_company_name()) ? "-" : dataPP
							.getMcl_company_name(), 250, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMkl_kerja(), 250, 568, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKerjab(),
					250, 559, 0);
			int monyong = 0;
			String[] uraian_tugas = StringUtil.pecahParagraf(dataTT
					.getMkl_kerja_ket().toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 549 - monyong, 0);
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getMkl_kerja_ket())?"-":dataPP.getMkl_kerja_ket(),
			// 250, 549, 0);
			monyong = 0;
			if(!Common.isEmpty(dataTT.getAlamat_kantor())){
				String[] alamat = StringUtil.pecahParagraf(dataTT.getAlamat_kantor().toUpperCase(), 75);
	        	if(!Common.isEmpty(alamat)){
		        	for(int i=0; i<alamat.length; i++) {
		        		monyong = 7 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
								529 - monyong, 0);
		        	}
	        	}
			}
			
//			monyong = 0;
//			String[] alamat = StringUtil.pecahParagraf(dataPP
//					.getAlamat_kantor().toUpperCase(), 70);
//			for (int i = 0; i < alamat.length; i++) {
//				monyong = 7 * i;
//				over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
//						529 - monyong, 0);
//			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getAlamat_kantor())?"-":dataPP.getAlamat_kantor(),
			// 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_kantor()) ? "-" : dataPP
							.getKota_kantor(), 250, 509, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_kantor()) ? "-" : dataPP
							.getKd_pos_kantor(), 250, 500, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_kantor()) ? "-" : dataPP
							.getTelpon_kantor(), 250, 490, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_kantor2())?"-":dataPP.getTelpon_kantor2(),
			// 250, 505, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 480, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_rumah()) ? "-" : dataPP
							.getAlamat_rumah(), 250, 470, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_rumah()) ? "-" : dataPP
							.getKota_rumah(), 250, 460, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_rumah()) ? "-" : dataPP
							.getKd_pos_rumah(), 250, 451, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
							.getTelpon_rumah(), 250, 441, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
			// 250, 445, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 432, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_tpt_tinggal()) ? "-"
							: dataPP.getAlamat_tpt_tinggal(), 250, 422, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_tpt_tinggal()) ? "-" : dataPP
							.getKota_tpt_tinggal(), 250, 412, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_tpt_tinggal()) ? "-"
							: dataPP.getKd_pos_tpt_tinggal(), 250, 402, 0);
//			over.showTextAligned(
//					PdfContentByte.ALIGN_LEFT,
//					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
//							.getTelpon_rumah(), 250, 393, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			 Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
					 250, 393, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 383, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_address(), 250, 373, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getKota(), 250, 353, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_zip_code(), 250, 343, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_phone1(), 250, 334, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_fax1(), 250, 323, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getNo_hp()) ? "-" : dataPP.getNo_hp(),
					250, 313, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getEmail()) ? "-" : dataPP.getEmail(),
					250, 303, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_npwp()) ? "-" : dataPP
							.getMcl_npwp(), 250, 294, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_penghasilan()) ? "-" : dataPP
							.getMkl_penghasilan(), 250, 285, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_pendanaan()) ? "-" : dataPP
							.getMkl_pendanaan(), 250, 273, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_tujuan()) ? "-" : dataPP
							.getMkl_tujuan(), 250, 264, 0);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getLsre_relation().toUpperCase(), 250, 234, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 250, 215, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_gelar()) ? "-" : dataTT
							.getMcl_gelar(), 250, 206, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_mother(), 250, 196, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsne_note(), 250, 186, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_green_card()) ? "TIDAK"
							: dataTT.getMcl_green_card(), 250, 176, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLside_name(), 250, 166, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_no_identity(), 250, 156, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataTT
									.getMspe_no_identity_expired()), 250, 146,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataTT.getMspe_date_birth()),
					250, 137, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMste_age() + " Tahun", 250, 127, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sex2().toUpperCase(), 250, 118, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					108, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsag_name(), 250, 98, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsed_name(), 250, 88, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_company_name()) ? "-" : dataTT
							.getMcl_company_name(), 250, 78, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMkl_kerja(), 250, 58, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getKerjab(),
					250, 49, 0);
			over.endText();

			//
			// //---------- Data Halaman Kelima ----------
			over = stamp.getOverContent(5);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			monyong = 0;
			uraian_tugas = StringUtil.pecahParagraf(dataTT.getMkl_kerja_ket()
					.toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 734 - monyong, 0);
			}

			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getMkl_kerja_ket())?"-":dataTT.getMkl_kerja_ket(),
			// 250, 734, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_kantor()) ? "-" : dataTT
							.getAlamat_kantor(), 250, 714, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_kantor()) ? "-" : dataTT
							.getKota_kantor(), 250, 695, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_kantor()) ? "-" : dataTT
							.getKd_pos_kantor(), 250, 685, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_kantor()) ? "-" : dataTT
							.getTelpon_kantor(), 250, 675, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_kantor2())?"-":dataTT.getTelpon_kantor2(),
			// 153, 105, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_rumah()) ? "-" : dataTT
							.getAlamat_rumah(), 250, 655, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_rumah()) ? "-" : dataTT
							.getKota_rumah(), 250, 645, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_rumah()) ? "-" : dataTT
							.getKd_pos_rumah(), 250, 635, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
							.getTelpon_rumah(), 250, 625, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah2())?"-":dataTT.getTelpon_rumah2(),
			// 153, 46, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_tpt_tinggal()) ? "-"
							: dataTT.getAlamat_tpt_tinggal(), 250, 607, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_tpt_tinggal()) ? "-" : dataTT
							.getKota_tpt_tinggal(), 250, 597, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_tpt_tinggal()) ? "-"
							: dataTT.getKd_pos_tpt_tinggal(), 250, 587, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah())?"-":dataTT.getTelpon_rumah(),
			// 250, 597, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
							.getTelpon_rumah2(), 250, 578, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 567, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(addrBill.getMsap_address())?"-":addrBill.getMsap_address(),
			// 208, 739, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getNo_hp()) ? "-" : dataTT.getNo_hp(),
					250, 557, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getEmail()) ? "-" : dataTT.getEmail(),
					250, 547, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_npwp()) ? "-" : dataTT
							.getMcl_npwp(), 250, 537, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_penghasilan()) ? "-" : dataTT
							.getMkl_penghasilan(), 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_pendanaan()) ? "-" : dataTT
							.getMkl_pendanaan(), 250, 519, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_tujuan()) ? "-" : dataTT
							.getMkl_tujuan(), 250, 509, 0);
			//
			// //Data Pembayar Premi
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getRelation_payor()) ? "-"
							: pembPremi.getRelation_payor(), 250, 478, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(pembPremi.getNama_pihak_ketiga()) ? "-"
					: pembPremi.getNama_pihak_ketiga(), 250, 468, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getKewarganegaraan()) ? "-"
							: pembPremi.getKewarganegaraan(), 250, 458, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getAlamat_lengkap()) ? "-"
							: pembPremi.getAlamat_lengkap(), 250, 450, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_rumah()) ? "-" : pembPremi
							.getTelp_rumah(), 250, 440, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_kantor()) ? "-"
							: pembPremi.getTelp_kantor(), 250, 430, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getEmail()) ? "-" : pembPremi
							.getEmail(), 250, 420, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTempat_lahir()) ? "-"
							: (pembPremi.getTempat_lahir() + ", " + FormatDate.toString(pembPremi
									.getMspe_date_birth_3rd_pendirian())), 250,
					410, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getMkl_kerja()) ? "-" : pembPremi
							.getMkl_kerja(), 250, 401, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 392, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 382, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 372, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getNo_npwp()) ? "-" : pembPremi
							.getNo_npwp(), 250, 362, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getSumber_dana()) ? "-"
							: pembPremi.getSumber_dana(), 250, 352, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTujuan_dana_3rd()) ? "-"
							: pembPremi.getTujuan_dana_3rd(), 250, 342, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 332, 0);
			//
			// //Data Usulan
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					s_channel.toUpperCase(), 250, 291, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 281, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 271, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspr_ins_period() + " Tahun", 250, 261, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspo_pay_period() + " Tahun", 250, 251, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataUsulan.getMspo_installment()) ? "-"
					: dataUsulan.getMspo_installment() + "", 250, 242, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLscb_pay_mode(), 250, 232, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new
			// BigDecimal(dataUsulan.getMspr_premium())), 250, 286, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 250, 222, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
							.getMste_flag_cc() == 2 ? "TABUNGAN"
							: "KARTU KREDIT"), 250, 211, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_beg_date()),
					250, 202, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_end_date()),
					250, 193, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// FormatDate.toIndonesian(dataUsulan.getLsdbs_number()>800?dataUsulan.getLsdbs_name():"-"),
			// 250, 237, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 221,
			// 0);

			if (dataUsulan.getDaftaRider().size() > 0) {
				Integer j = 0;
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							rider.getLsdbs_name(), 250, 185, 0);
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getLku_symbol()
									+ " "
									+ FormatNumber
											.convertToTwoDigit(new BigDecimal(
													rider.getMspr_tsi())), 250,
													175, 0);
					j += 10;
				}
			}

			// //Data Investasi
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 250, 95, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 250, 85, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 250, 75, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiExtra.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiExtra))), 250, 65, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 250, 55, 0);
			Double d_jmlinves = new Double(0);
			String s_jnsinves = "";
			for (int i = 0; i < detInv.size(); i++) {
				DetilInvestasi detInves = (DetilInvestasi) detInv.get(i);
				d_jmlinves = d_jmlinves + detInves.getMdu_jumlah1();
				s_jnsinves = s_jnsinves
						+ detInves.getLji_invest1().toUpperCase() + " "
						+ detInves.getMdu_persen1() + "%";
				if (i != (detInv.size() - 1))
					s_jnsinves = s_jnsinves + ", ";
			}
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, s_jnsinves, 250,
					45, 0);
			over.endText();

			// ---------- Data Halaman Keenam ----------

			over = stamp.getOverContent(6);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			// //Data Rekening
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getLsbp_nama(), 250, 724, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_cabang(), 250, 714, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_no_ac(), 250, 704, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_nama(), 250, 694, 0);

			if (dataTT.getMste_flag_cc() == 1 || dataTT.getMste_flag_cc() == 2) {
				if (accRecur != null) {
					String bank_pusat = "";
					String bank_cabang = "";

					if (!namaBank.isEmpty()) {
						HashMap m = (HashMap) namaBank.get(0);
						bank_pusat = (String) m.get("LSBP_NAMA");
						bank_cabang = (String) m.get("LBN_NAMA");
					}
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getMste_flag_cc() == 0 ? "TUNAI"
									: (dataUsulan.getMste_flag_cc() == 2 ? "TABUNGAN"
											: "KARTU KREDIT"), 250, 631, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							Common.isEmpty(bank_pusat) ? "-"
									: bank_pusat.toUpperCase()/*
															 * accRecur.getLbn_nama
															 * ().toUpperCase()
															 */, 250, 621, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(bank_cabang) ? "-"
							: bank_cabang.toUpperCase()/*
																 * accRecur.
																 * getLbn_nama
																 * ().
																 * toUpperCase()
																 */, 250, 611,
							0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_acc_no()) ? "-" : accRecur
							.getMar_acc_no().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_holder()) ? "-" : accRecur
							.getMar_holder().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 592, 0);
				}
			} else {
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 631,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 621,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 611,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 601,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 592,
						0);
			}

			if (peserta.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < peserta.size(); i++) {

					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					if (pesertaPlus.getFlag_jenis_peserta() > 0){
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getNama().toUpperCase(), 80, 517 - j,
								0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getLsre_relation(), 190, 517 - j, 0);
						over.showTextAligned(
								PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getTinggi() + "/"
										+ pesertaPlus.getBerat(), 260, 517 - j, 0);
//						String[] pekerjaan = StringUtil.pecahParagraf(pesertaPlus.
//								getKerja().toUpperCase(), 15);
//							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
//									pekerjaan[i], 290, 519 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getKerja(), 290, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getSex(), 375, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								FormatDate.toString(pesertaPlus.getTgl_lahir()),
								430, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								dataPP.getLsne_note(), 480, 517 - j, 0);
						j += 10;
					}	
				}
			}
			if (benef.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < benef.size(); i++) {
					Benefeciary benefit = (Benefeciary) benef.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_first(), 60, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getSmsaw_birth(), 200, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_sex() == 1 ? "LAKI-LAKI" : "PEREMPUAN",
							275, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_persen() + "%", 333, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, benefit
							.getLsre_relation().toUpperCase(), 380, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							dataPP.getLsne_note(), 480, 420 - j, 0);
					j += 10;
				}
			}
			over.endText();
			
			// ------------Halaman tujuh-----------
			over = stamp.getOverContent(7);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);
			String jawab;
				 if (rslt6.size() > 0) {
						// /81, 104
						Integer j = 0;
						Integer k = 0;
						Integer l = 0;
						for (int i = 0; i < rslt6.size(); i++) {
							MstQuestionAnswer ans = (MstQuestionAnswer) rslt6.get(i);
							// j += 3;
							if (ans.getQuestion_id() > 80 && ans.getQuestion_id() < 83) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 745 - j, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 745 - j, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 765 - j, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 765 - j, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 765 - j, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 765 - j, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 765 - j, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 765 - j, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 745 - j, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 745 - j, 0);
									}
								}

							}
							j += 10;
							if (ans.getQuestion_id() == 84) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 595, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 595, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 595, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 595, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 595, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 595, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 595, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 595, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 595, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 595, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 85) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 585, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 585, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 585, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 585, 0);
								}

								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 585, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 585, 0);
										}
										//--tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 585, 0);
										}
										//--tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 585, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 585, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 585, 0);
									}
								}

							}
							if (ans.getQuestion_id() == 86) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 575, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 575, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 575, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 575, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 575, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 575, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 575, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 575, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if ( ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 575, 0);
									} else if ( ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 575, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 87) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 565, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 565, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 565, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 565, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 565, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 565, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 565, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 565, 0);
										}
										
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 565, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 565, 0);
									}
								}

							}
							if (ans.getQuestion_id() == 88) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 555, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 555, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 555, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 555, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 555, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 555, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 555, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 555, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 555, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 555, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 89) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 545, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 545, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 545, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 545, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 545, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 545, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 545, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 545, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										}
								}
							
							}
							if (ans.getQuestion_id() == 90) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 535, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 535, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 535, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 535, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 535, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 535, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 535, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 535, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 535, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 535, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 91) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 525, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 525, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 525, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 525, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
									
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 525, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 525, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 525, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 525, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 525, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 525, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 92) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 515, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 515, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 515, 0);
									} else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 515, 0);
									}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 515, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 515, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 515, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 515, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										}
								}
								

							}
							if (ans.getQuestion_id() == 93) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 425, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 505, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 505, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 505, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 505, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 505, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 505, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 505, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 505, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 505, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 94) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 495, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 495, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 495, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 495, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 495, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 495, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 495, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 495, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 495, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 495, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 95) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 485, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 485, 0);
								}// --tu
								else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 485, 0);
									} else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 485, 0);
									}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 485, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 485, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 485, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 485, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										}
								}
							}
							
							if (ans.getQuestion_id() == 96) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 475, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 475, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 475, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 475, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 475, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 475, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 475, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 475, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 475, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 475, 0);
									}
								}
							}
									
							if (ans.getQuestion_id() == 97) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 465, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 465, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 465, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 465, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 465, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 465, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 465, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 465, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if ( ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 465, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 465, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 98) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 455, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 455, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 455, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 455, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 455, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 455, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 455, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 455, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 455, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 455, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 99) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 445, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 445, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 445, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 445, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 445, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 445, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 445, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 445, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 445, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 445, 0);
									}
								}

							}
							if (ans.getQuestion_id() == 100) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 435, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 435, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 435, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 435, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 435, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 435, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 435, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 435, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 435, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 435, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 101) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 430, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 430, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 430, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 430, 0);
								}
								else if (ans.getOption_group() == 0
										&& ans.getOption_order() == 1) {
									jawab = ans.getAnswer2();
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 110,
											400, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 430, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 430, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 430, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 430, 0);
										}
									}
								}
								 else if (dataTT.getLsre_id()==1){
									 if ( ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										} else if ( ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										}
								}
							}
							
							if (ans.getQuestion_id() == 102) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 390, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 390, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 390, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 390, 0);
								}
								else if (ans.getOption_group() == 0
										&& ans.getOption_order() == 1) {
									jawab = ans.getAnswer2();
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 110,
											352, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 390, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 390, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 390, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 390, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										}
								}
								 
							}
							if (ans.getQuestion_id() == 103) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 342, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 342, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 342, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 342, 0);
								}
								 else if (ans.getOption_group() == 0
											&& ans.getOption_order() == 1) {
										jawab = ans.getAnswer2();
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 110,
												282, 0);
									}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 342, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 342, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 342, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 342, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 342, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 342, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 104) {
								if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1) {
									jawab = ans.getAnswer2();
									over.setFontAndSize(italic, 6);
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 370,
											272, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2) {
									jawab = ans.getAnswer2();
									over.setFontAndSize(italic, 6);
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 370,
											262, 0);
								}
								else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 1) {
										jawab = ans.getAnswer2();
										over.setFontAndSize(italic, 6);
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 400,
												272, 0);
								} else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 2) {
										jawab = ans.getAnswer2();
										over.setFontAndSize(italic, 6);
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 400,
												262, 0);
								}

								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 425,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 425,
													262, 0);
										}

										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 463,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 463,
													262, 0);
										}

										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 493,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 493,
													262, 0);
										}

										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 540,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 540,
													262, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1) {
										jawab = ans.getAnswer2();
										over.setFontAndSize(italic, 6);
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 400,
												272, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2) {
										jawab = ans.getAnswer2();
										over.setFontAndSize(italic, 6);
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 400,
												262, 0);
									}
								}
							}
						}
					}	
			
			over.endText();
			stamp.close();
			
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try {

				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");

				in = new FileInputStream(l_file);
				ouputStream = response.getOutputStream();

				IOUtils.copy(in, ouputStream);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (ouputStream != null) {
						ouputStream.flush();
						ouputStream.close();
					}
				} catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		return null;
	}
	
	public ModelAndView espajonlinegadgetfullsyariah(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String reg_spaj = request.getParameter("spaj");
		Integer type = null;
		Integer question;
		Date sysdate = elionsManager.selectSysdate();
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		List<String> pdfs = new ArrayList<String>();
		Boolean suksesMerge = false;
		HashMap<String, Object> cmd = new HashMap<String, Object>();
		ArrayList data_answer = new ArrayList();
		Integer index = null;
		Integer index2 = null;
		String spaj = "";
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj;
		System.out.print(mspo_flag_spaj);
		String dir = props.getProperty("pdf.template.espajonlinegadget");
		OutputStream output;
		PdfReader reader;
		File userDir = new File(props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "GADGET");
		moreInfo.put("Subject", "E-SPAJ ONLINE");

		PdfContentByte over;
		PdfContentByte over2;
		BaseFont times_new_roman = BaseFont.createFont(
				"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
		BaseFont italic = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ariali.ttf",
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

				reader = new PdfReader(
						props.getProperty("pdf.template.espajonlinegadget")
								+ "\\spajonlinegadgetfullsyariah.pdf");
				output = new FileOutputStream(exportDirectory + "\\"
						+ "espajonlinegadget_" + reg_spaj + ".pdf");

				spaj = dir + "\\spajonlinegadgetfullsyariah.pdf";
			
			pdfs.add(spaj);
			suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			String outputName = props.getProperty("pdf.dir.export") + "\\"
					+ cabang + "\\" + reg_spaj + "\\" + "espajonlinegadget_"
					+ reg_spaj + ".pdf";
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
					outputName));

			Pemegang dataPP = elionsManager.selectpp(reg_spaj);
			Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
			PembayarPremi pembPremi = bacManager.selectP_premi(reg_spaj);
			if (pembPremi == null)
				pembPremi = new PembayarPremi();
			AddressBilling addrBill = this.elionsManager
					.selectAddressBilling(reg_spaj);
			Datausulan dataUsulan = this.elionsManager
					.selectDataUsulanutama(reg_spaj);
			dataUsulan.setDaftaRider(elionsManager
					.selectDataUsulan_rider(reg_spaj));
			InvestasiUtama inv = this.elionsManager
					.selectinvestasiutama(reg_spaj);
			Rekening_client rekClient = this.elionsManager
					.select_rek_client(reg_spaj);
			Account_recur accRecur = this.elionsManager
					.select_account_recur(reg_spaj);
			List detInv = this.bacManager.selectdetilinvestasi2(reg_spaj);
			List benef = this.elionsManager.select_benef(reg_spaj);
			List peserta = this.uwManager.select_all_mst_peserta(reg_spaj);
			List dist = this.elionsManager.select_tipeproduk();
			List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
			HashMap spajTemp = (HashMap) listSpajTemp.get(0);
			String idgadget = (String) spajTemp.get("NO_TEMP");
			Map agen = this.bacManager.selectAgenESPAJSimasPrima(reg_spaj);
			List namaBank = uwManager.namaBank(accRecur.getLbn_id());

			// --Question Full Konven/Syariah
			List rslt = bacManager.selectQuestionareGadget(reg_spaj, 2, 1, 15);	
			List rslt2 = bacManager.selectQuestionareGadget(reg_spaj, 1, 16, 18); 
			List rslt3 = bacManager.selectQuestionareGadget(reg_spaj, 3, 106, 136); 
			List rslt4 = bacManager.selectQuestionareGadget(reg_spaj, 3, 137, 145);
			List rslt5 = bacManager.selectQuestionareGadget(reg_spaj, 3, 146, 155);
			
			//Sio
			List rslt6 = bacManager.selectQuestionareGadget(reg_spaj, 12, 81, 104);
			
			String s_channel = "";
			for (int i = 0; i < dist.size(); i++) {
				HashMap dist2 = (HashMap) dist.get(i);
				Integer i_lstp_id = (Integer) dist2.get("lstp_id");
				if (i_lstp_id.intValue() == dataUsulan.getTipeproduk()
						.intValue()) {
					s_channel = (String) dist2.get("lstp_produk");
				}
			}

			Double d_premiRider = 0.;
			if (dataUsulan.getDaftaRider().size() > 0) {
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					d_premiRider = rider.getMspr_premium();
				}
			}
			Double d_topUpBerkala = new Double(0);
			Double d_topUpTunggal = new Double(0);
			Double d_totalTopup = new Double(0);
			if (inv != null) {
				DetilTopUp daftarTopup = inv.getDaftartopup();
				d_topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala()) ? new Double(
						0) : daftarTopup.getPremi_berkala();
				d_topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal()) ? new Double(
						0) : daftarTopup.getPremi_tunggal();
				d_totalTopup = d_topUpBerkala + d_topUpTunggal;
			}
			Double d_premiExtra = (Common.isEmpty(uwManager
					.selectSumPremiExtra(reg_spaj)) ? 0. : uwManager
					.selectSumPremiExtra(reg_spaj));
			Double d_totalPremi = dataUsulan.getMspr_premium() + d_totalTopup
					+ d_premiRider + d_premiExtra;

			stamp.setMoreInfo(moreInfo);

			// ---------- Data Halaman Pertama ----------

			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman, 8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatString.nomorSPAJ(reg_spaj), 380, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(sysdate), 85, 617, 0);

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 160, 516, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 160, 506, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 160, 496, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 160, 486, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 290, 476, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 290, 467, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 290, 457, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiRider.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiRider))), 290, 447, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 290, 437, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_AGEN").toString().toUpperCase()) ? "-" : 
						agen.get("NM_AGEN").toString().toUpperCase(),160, 409,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_AGEN").toString().toUpperCase()) ? "-" : 
						agen.get("KD_AGEN").toString().toUpperCase(),160, 399,0);
			over.endText();

			// ---------- Data Halaman keempat ----------
			over = stamp.getOverContent(4);
			over.beginText();
			
			// String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" +
			// (dataPP.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
			String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_"
					+ ".jpg";
			try {
				Image img = Image.getInstance(ttdPp);
				img.scaleAbsolute(30, 30);
				over.addImage(img, img.getScaledWidth(), 0, 0,
						img.getScaledHeight(), 438, 643);
				over.stroke();

				// String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
				// + (dataTT.getMcl_first().toUpperCase()).replace(" ", "") +
				// ".jpg";
				String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
						+ ".jpg";
				if (dataTT.getMste_age() < 17 || dataPP.getLsre_id() == 1)
					ttdTu = ttdPp;
				Image img2 = Image.getInstance(ttdTu);
				img2.scaleAbsolute(30, 30);
				over.addImage(img2, img2.getScaledWidth(), 0, 0,
						img2.getScaledHeight(), 438, 593);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('TTD Pemegang Polis / Tertanggung Utama Tidak Ditemukan');window.close();</script>");
				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(dataPP.getMspo_spaj_date()), 370, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataPP
					.getMcl_first().toUpperCase(), 295, 655, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataTT
					.getMcl_first().toUpperCase(), 295, 605, 0);
			if (peserta.size() > 0 ) {
				Integer vertikal = 605;
				for (int i = 0; i < peserta.size(); i++) {
					vertikal = vertikal - 50;
					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					if (pesertaPlus.getFlag_jenis_peserta() > 0){
						over.showTextAligned(PdfContentByte.ALIGN_CENTER,
								pesertaPlus.getNama().toUpperCase(), 290, vertikal,
								0);
						vertikal = vertikal + 2;
					}
					
				}
			}

			try {
				String ttdPenutup = exportDirectory + "\\" + idgadget
						+ "_TTD_PENUTUP_" + ".jpg";
				Image img3 = Image.getInstance(ttdPenutup);
				img3.scaleAbsolute(30, 30);
				over.addImage(img3, img3.getScaledWidth(), 0, 0,
						img3.getScaledHeight(), 120, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Penutup Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			try {
				String ttdReff = exportDirectory + "\\" + idgadget
						+ "_TTD_REF_" + ".jpg";
				Image img4 = Image.getInstance(ttdReff);
				img4.scaleAbsolute(30, 30);
				over.addImage(img4, img4.getScaledWidth(), 0, 0,
						img4.getScaledHeight(), 290, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Refferal Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}
			
			try {
				String ttdAgen = exportDirectory + "\\" + idgadget
						+ "_TTD_AGEN_" + ".jpg";
				Image img5 = Image.getInstance(ttdAgen);
				img5.scaleAbsolute(30, 30);
				over.addImage(img5, img5.getScaledWidth(), 0, 0,
						img5.getScaledHeight(), 440, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_PENUTUP")) ? "-" : 
						agen.get("NM_PENUTUP").toString().toUpperCase(),100, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_PENUTUP")) ? "-" : 
						agen.get("KD_PENUTUP").toString().toUpperCase(),100, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_PENUTUP")) ? "-" : 
						agen.get("CB_PENUTUP").toString().toUpperCase(),100, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_REFFERAL")) ? "-" : 
						agen.get("NM_REFFERAL").toString().toUpperCase(),270, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_REFFERAL")) ? "-" : 
						agen.get("KD_REFFERAL").toString().toUpperCase(),270, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_REFFERAL")) ? "-" : 
						agen.get("CB_REFFERAL").toString().toUpperCase(),270, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_AGEN")) ? "-" : 
						agen.get("NM_AGEN").toString().toUpperCase(),440, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_AGEN")) ? "-" : 
						agen.get("KD_AGEN").toString().toUpperCase(),440, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_AGEN")) ? "-" : 
						agen.get("CB_AGEN").toString().toUpperCase(),440, 240,0);

			over.endText();

			// //---------- Data Halaman kelima ----------
			over = stamp.getOverContent(5);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 250, 725, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_gelar()) ? "-" : dataPP
							.getMcl_gelar(), 250, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_mother(), 250, 705, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsne_note(), 250, 695, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_green_card()) ? "TIDAK"
							: dataPP.getMcl_green_card(), 250, 685, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLside_name(), 250, 675, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_no_identity(), 250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataPP
									.getMspe_no_identity_expired()), 250, 656,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataPP.getMspe_date_birth()),
					250, 646, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspo_age() + " Tahun", 250, 636, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sex2().toUpperCase(), 250, 626, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					617, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsag_name(), 250, 607, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsed_name(), 250, 596, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_company_name()) ? "-" : dataPP
							.getMcl_company_name(), 250, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMkl_kerja(), 250, 568, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKerjab(),
					250, 559, 0);
			int monyong = 0;
			String[] uraian_tugas = StringUtil.pecahParagraf(dataTT
					.getMkl_kerja_ket().toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 549 - monyong, 0);
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getMkl_kerja_ket())?"-":dataPP.getMkl_kerja_ket(),
			// 250, 549, 0);
			monyong = 0;
			if(!Common.isEmpty(dataTT.getAlamat_kantor())){
				String[] alamat = StringUtil.pecahParagraf(dataTT.getAlamat_kantor().toUpperCase(), 75);
	        	if(!Common.isEmpty(alamat)){
		        	for(int i=0; i<alamat.length; i++) {
		        		monyong = 7 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
								529 - monyong, 0);
		        	}
	        	}
			}

			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getAlamat_kantor())?"-":dataPP.getAlamat_kantor(),
			// 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_kantor()) ? "-" : dataPP
							.getKota_kantor(), 250, 509, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_kantor()) ? "-" : dataPP
							.getKd_pos_kantor(), 250, 500, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_kantor()) ? "-" : dataPP
							.getTelpon_kantor(), 250, 490, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_kantor2())?"-":dataPP.getTelpon_kantor2(),
			// 250, 505, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 480, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_rumah()) ? "-" : dataPP
							.getAlamat_rumah(), 250, 470, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_rumah()) ? "-" : dataPP
							.getKota_rumah(), 250, 460, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_rumah()) ? "-" : dataPP
							.getKd_pos_rumah(), 250, 451, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
							.getTelpon_rumah(), 250, 441, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
			// 250, 445, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 432, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_tpt_tinggal()) ? "-"
							: dataPP.getAlamat_tpt_tinggal(), 250, 422, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_tpt_tinggal()) ? "-" : dataPP
							.getKota_tpt_tinggal(), 250, 412, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_tpt_tinggal()) ? "-"
							: dataPP.getKd_pos_tpt_tinggal(), 250, 402, 0);
//			over.showTextAligned(
//					PdfContentByte.ALIGN_LEFT,
//					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
//							.getTelpon_rumah(), 250, 393, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			 Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
					 250, 393, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 383, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_address(), 250, 373, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getKota(), 250, 353, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_zip_code(), 250, 343, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_phone1(), 250, 334, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_fax1(), 250, 323, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getNo_hp()) ? "-" : dataPP.getNo_hp(),
					250, 313, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getEmail()) ? "-" : dataPP.getEmail(),
					250, 303, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_npwp()) ? "-" : dataPP
							.getMcl_npwp(), 250, 294, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_penghasilan()) ? "-" : dataPP
							.getMkl_penghasilan(), 250, 285, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_pendanaan()) ? "-" : dataPP
							.getMkl_pendanaan(), 250, 273, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_tujuan()) ? "-" : dataPP
							.getMkl_tujuan(), 250, 264, 0);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getLsre_relation().toUpperCase(), 250, 234, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 250, 215, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_gelar()) ? "-" : dataTT
							.getMcl_gelar(), 250, 206, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_mother(), 250, 196, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsne_note(), 250, 186, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_green_card()) ? "TIDAK"
							: dataTT.getMcl_green_card(), 250, 176, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLside_name(), 250, 166, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_no_identity(), 250, 156, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataTT
									.getMspe_no_identity_expired()), 250, 146,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataTT.getMspe_date_birth()),
					250, 137, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMste_age() + " Tahun", 250, 127, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sex2().toUpperCase(), 250, 118, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					108, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsag_name(), 250, 98, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsed_name(), 250, 88, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_company_name()) ? "-" : dataTT
							.getMcl_company_name(), 250, 78, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMkl_kerja(), 250, 58, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getKerjab(),
					250, 49, 0);
			over.endText();

			//
			// //---------- Data Halaman keenam ----------
			over = stamp.getOverContent(6);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			monyong = 0;
			uraian_tugas = StringUtil.pecahParagraf(dataTT.getMkl_kerja_ket()
					.toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 734 - monyong, 0);
			}

			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getMkl_kerja_ket())?"-":dataTT.getMkl_kerja_ket(),
			// 250, 734, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_kantor()) ? "-" : dataTT
							.getAlamat_kantor(), 250, 714, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_kantor()) ? "-" : dataTT
							.getKota_kantor(), 250, 695, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_kantor()) ? "-" : dataTT
							.getKd_pos_kantor(), 250, 685, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_kantor()) ? "-" : dataTT
							.getTelpon_kantor(), 250, 675, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_kantor2())?"-":dataTT.getTelpon_kantor2(),
			// 153, 105, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_rumah()) ? "-" : dataTT
							.getAlamat_rumah(), 250, 655, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_rumah()) ? "-" : dataTT
							.getKota_rumah(), 250, 645, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_rumah()) ? "-" : dataTT
							.getKd_pos_rumah(), 250, 635, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
							.getTelpon_rumah(), 250, 625, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah2())?"-":dataTT.getTelpon_rumah2(),
			// 153, 46, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_tpt_tinggal()) ? "-"
							: dataTT.getAlamat_tpt_tinggal(), 250, 607, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_tpt_tinggal()) ? "-" : dataTT
							.getKota_tpt_tinggal(), 250, 597, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_tpt_tinggal()) ? "-"
							: dataTT.getKd_pos_tpt_tinggal(), 250, 587, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah())?"-":dataTT.getTelpon_rumah(),
			// 250, 597, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
							.getTelpon_rumah2(), 250, 578, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 567, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(addrBill.getMsap_address())?"-":addrBill.getMsap_address(),
			// 208, 739, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getNo_hp()) ? "-" : dataTT.getNo_hp(),
					250, 557, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getEmail()) ? "-" : dataTT.getEmail(),
					250, 547, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_npwp()) ? "-" : dataTT
							.getMcl_npwp(), 250, 537, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_penghasilan()) ? "-" : dataTT
							.getMkl_penghasilan(), 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_pendanaan()) ? "-" : dataTT
							.getMkl_pendanaan(), 250, 519, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_tujuan()) ? "-" : dataTT
							.getMkl_tujuan(), 250, 509, 0);
			//
			// //Data Pembayar Premi
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getRelation_payor()) ? "-"
							: pembPremi.getRelation_payor(), 250, 478, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(pembPremi.getNama_pihak_ketiga()) ? "-"
					: pembPremi.getNama_pihak_ketiga(), 250, 468, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getKewarganegaraan()) ? "-"
							: pembPremi.getKewarganegaraan(), 250, 458, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getAlamat_lengkap()) ? "-"
							: pembPremi.getAlamat_lengkap(), 250, 450, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_rumah()) ? "-" : pembPremi
							.getTelp_rumah(), 250, 440, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_kantor()) ? "-"
							: pembPremi.getTelp_kantor(), 250, 430, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getEmail()) ? "-" : pembPremi
							.getEmail(), 250, 420, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTempat_lahir()) ? "-"
							: (pembPremi.getTempat_lahir() + ", " + FormatDate.toString(pembPremi
									.getMspe_date_birth_3rd_pendirian())), 250,
					410, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getMkl_kerja()) ? "-" : pembPremi
							.getMkl_kerja(), 250, 401, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 392, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 382, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 372, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getNo_npwp()) ? "-" : pembPremi
							.getNo_npwp(), 250, 362, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getSumber_dana()) ? "-"
							: pembPremi.getSumber_dana(), 250, 352, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTujuan_dana_3rd()) ? "-"
							: pembPremi.getTujuan_dana_3rd(), 250, 342, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 332, 0);
			//
			// //Data Usulan
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					s_channel.toUpperCase(), 250, 291, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 281, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 271, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspr_ins_period() + " Tahun", 250, 261, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspo_pay_period() + " Tahun", 250, 251, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataUsulan.getMspo_installment()) ? "-"
					: dataUsulan.getMspo_installment() + "", 250, 242, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLscb_pay_mode(), 250, 232, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new
			// BigDecimal(dataUsulan.getMspr_premium())), 250, 286, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 250, 222, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
							.getMste_flag_cc() == 2 ? "TABUNGAN"
							: "KARTU KREDIT"), 250, 211, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_beg_date()),
					250, 202, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_end_date()),
					250, 193, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// FormatDate.toIndonesian(dataUsulan.getLsdbs_number()>800?dataUsulan.getLsdbs_name():"-"),
			// 250, 237, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 221,
			// 0);

			if (dataUsulan.getDaftaRider().size() > 0) {
				Integer j = 0;
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							rider.getLsdbs_name(), 270, 163 - j, 0);
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getLku_symbol()
									+ " "
									+ FormatNumber
											.convertToTwoDigit(new BigDecimal(
													rider.getMspr_tsi())), 440,
													163 - j, 0);
					j += 10;
				}
			}

			// //Data Investasi
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 250, 97, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 250, 87, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 250, 77, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiExtra.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiExtra))), 250, 68, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 250, 58, 0);
			Double d_jmlinves = new Double(0);
			String s_jnsinves = "";
			for (int i = 0; i < detInv.size(); i++) {
				DetilInvestasi detInves = (DetilInvestasi) detInv.get(i);
				d_jmlinves = d_jmlinves + detInves.getMdu_jumlah1();
				s_jnsinves = s_jnsinves
						+ detInves.getLji_invest1().toUpperCase() + " "
						+ detInves.getMdu_persen1() + "%";
				if (i != (detInv.size() - 1))
					s_jnsinves = s_jnsinves + ", ";
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new BigDecimal(d_jmlinves)), 208,
			// 183, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, s_jnsinves, 250,
					47, 0);
			over.endText();

			// ---------- Data Halaman ketujuh----------

			over = stamp.getOverContent(7);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			// //Data Rekening
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getLsbp_nama(), 250, 724, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_cabang(), 250, 714, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_no_ac(), 250, 704, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_nama(), 250, 694, 0);

			if (dataTT.getMste_flag_cc() == 1 || dataTT.getMste_flag_cc() == 2) {
				if (accRecur != null) {
					String bank_pusat = "";
					String bank_cabang = "";

					if (!namaBank.isEmpty()) {
						HashMap m = (HashMap) namaBank.get(0);
						bank_pusat = (String) m.get("LSBP_NAMA");
						bank_cabang = (String) m.get("LBN_NAMA");
					}
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getMste_flag_cc() == 0 ? "TUNAI"
									: (dataUsulan.getMste_flag_cc() == 2 ? "TABUNGAN"
											: "KARTU KREDIT"), 250, 631, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							Common.isEmpty(bank_pusat) ? "-"
									: bank_pusat.toUpperCase()/*
															 * accRecur.getLbn_nama
															 * ().toUpperCase()
															 */, 250, 621, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(bank_cabang) ? "-"
							: bank_cabang.toUpperCase()/*
																 * accRecur.
																 * getLbn_nama
																 * ().
																 * toUpperCase()
																 */, 250, 611,
							0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_acc_no()) ? "-" : accRecur
							.getMar_acc_no().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_holder()) ? "-" : accRecur
							.getMar_holder().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 592, 0);
				}
			} else {
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 631,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 621,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 611,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 601,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 592,
						0);
			}

			if (peserta.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < peserta.size(); i++) {

					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					if (pesertaPlus.getFlag_jenis_peserta() > 0){
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getNama().toUpperCase(), 80, 517 - j,
								0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getLsre_relation(), 190, 517 - j, 0);
						over.showTextAligned(
								PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getTinggi() + "/"
										+ pesertaPlus.getBerat(), 260, 517 - j, 0);
//						String[] pekerjaan = StringUtil.pecahParagraf(pesertaPlus.
//								getKerja().toUpperCase(), 15);
//							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
//									pekerjaan[i], 290, 519 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getKerja(), 290, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getSex(), 375, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								FormatDate.toString(pesertaPlus.getTgl_lahir()),
								430, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								dataPP.getLsne_note(), 480, 517 - j, 0);
						j += 10;
					}
					
				}
			}
			if (benef.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < benef.size(); i++) {
					Benefeciary benefit = (Benefeciary) benef.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_first(), 60, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getSmsaw_birth(), 200, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_sex() == 1 ? "LAKI-LAKI" : "PEREMPUAN",
							275, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_persen() + "%", 333, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, benefit
							.getLsre_relation().toUpperCase(), 380, 420 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							dataPP.getLsne_note(), 480, 420 - j, 0);
					j += 10;
				}
			}
			// -----------data tertanggung-----------
			String jawab = "";
			if (mspo_flag_spaj.equals("3")){
				if (rslt.size() > 0) {
					Integer j = 0;
					for (int i = 0; i < rslt.size(); i++) {
						MstQuestionAnswer ans = (MstQuestionAnswer) rslt.get(i);
						if (ans.getQuestion_id() == 1) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 348, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 348, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 348, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 348, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										330, 0);
							}
						}
						if (ans.getQuestion_id() == 2) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 319, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 319, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 319, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 319, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										310, 0);
							}
						}
						if (ans.getQuestion_id() == 3) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 300, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 300, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 300, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 300, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										270, 0);
							}
						}
						if (ans.getQuestion_id() == 4) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 260, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 260, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										230, 0);
							}
						}
						if (ans.getQuestion_id() == 5) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 220, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 220, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										203, 0);
							}
						}
						if (ans.getQuestion_id() == 6) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 193, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 193, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 193, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 193, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										183, 0);
							}
						}	
						if (ans.getQuestion_id() == 7) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 153, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 153, 0);
							} else {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										Common.isEmpty(jawab) ? "-" : jawab, 100,
										124, 0);
							}
						}
						if (ans.getQuestion_id() == 8) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 113, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 113, 0);
							}
						}
						if (ans.getQuestion_id() == 9) {
							if (ans.getOption_type() == 0
									&& ans.getOption_order() == 1) {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 115, 102, 0);
							} else if (ans.getOption_type() == 0
									&& ans.getOption_order() == 2) {
								jawab = ans.getAnswer2();
								over.setFontAndSize(italic, 6);
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 160, 92, 0);
							}
						}
						if (ans.getQuestion_id() == 10) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 86, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 86, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 86, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 86, 0);
							}
						}
						if (ans.getQuestion_id() == 11) {
							if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 63, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 510, 63, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "v";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 63, 0);
							} else if (ans.getOption_type() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("0")) {
								jawab = "-";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 530, 63, 0);
							}
						}

					}	
			}
			}
			over.endText();

			// ------------Halaman delapan-----------
			over = stamp.getOverContent(8);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);
			if (mspo_flag_spaj.equals("3")){
			if (rslt.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt.get(i);
					if (ans.getQuestion_id() == 12) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 735, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 735, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									725, 0);
						}
					}
					if (ans.getQuestion_id() == 13) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 705, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 705, 0);
						}
					}
					if (ans.getQuestion_id() == 15) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 685, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 685, 0);
						}
					}
				}
			}

			// -------------data pemegang polis--------------
			if (rslt2.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt2.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt2.get(i);
					if (ans.getQuestion_id() == 16) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 655, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 655, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 655, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 655, 0);
						} else if (ans.getOption_type() == 0
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									635, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									635, 0);
						}
					}
					if (ans.getQuestion_id() == 17) {
						if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 625, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 510, 625, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "v";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 625, 0);
						} else if (ans.getOption_type() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("0")) {
							jawab = "-";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 530, 625, 0);
						} else if (ans.getOption_type() == 0
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									615, 0);
						} else {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									615, 0);
						}
					}
					if (ans.getQuestion_id() == 18) {
						if (ans.getOption_type() == 4
								&& ans.getOption_order() == 1) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 150, 595, 0);
						} else if (ans.getOption_type() == 4
								&& ans.getOption_order() == 2) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 250, 595, 0);
						}
					}
				}
			}
			}
			else{
				 if (rslt6.size() > 0) {
					// /81, 104
								Integer j = 0;
								Integer k = 0;
								Integer l = 0;
								for (int i = 0; i < rslt6.size(); i++) {
									MstQuestionAnswer ans = (MstQuestionAnswer) rslt6.get(i);
									// j += 3;
									if (ans.getQuestion_id() > 80 && ans.getQuestion_id() < 83) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 745 - j, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 745 - j, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 765 - j, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 765 - j, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 765 - j, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 765 - j, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 765 - j, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 765 - j, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 765 - j, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 765 - j, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 765 - j, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 765 - j, 0);
										}
									}
									j += 10;
									if (ans.getQuestion_id() == 84) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 595, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 595, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 595, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 595, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 595, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 595, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 595, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 595, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 595, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 595, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 595, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 595, 0);
										}
									}
									if (ans.getQuestion_id() == 85) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 585, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 585, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 585, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 585, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 585, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 585, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 585, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 585, 0);
										}

									}
									if (ans.getQuestion_id() == 86) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 575, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 575, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 575, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 575, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 575, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 575, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 575, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 575, 0);
										}

									}
									if (ans.getQuestion_id() == 87) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 565, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 565, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 565, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 565, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 565, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 565, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 565, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 565, 0);
										}

									}
									if (ans.getQuestion_id() == 88) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 555, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 555, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 555, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 555, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 555, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 555, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 555, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 555, 0);
										}

									}
									if (ans.getQuestion_id() == 89) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 545, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 545, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 545, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 545, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 545, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 545, 0);
										}

									}
									if (ans.getQuestion_id() == 90) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 535, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 535, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 535, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 535, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 535, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 535, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 535, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 535, 0);
										}

									}
									if (ans.getQuestion_id() == 91) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 525, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 525, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 525, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 525, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 525, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 525, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 525, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 525, 0);
										}

									}
									if (ans.getQuestion_id() == 92) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 515, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 515, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 515, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 515, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 515, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 515, 0);
										}

									}
									if (ans.getQuestion_id() == 93) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 425, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 505, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 505, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 505, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 505, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 505, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 505, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 505, 0);
										}

									}
									if (ans.getQuestion_id() == 94) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 495, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 495, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 495, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 495, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 495, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 495, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 495, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 495, 0);
										}

									}
									if (ans.getQuestion_id() == 95) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 485, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 485, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 485, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 485, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 485, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 485, 0);
										}

									}
									if (ans.getQuestion_id() == 96) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 475, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 475, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 475, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 475, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 475, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 475, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 475, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 475, 0);
										}

									}
									if (ans.getQuestion_id() == 97) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 465, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 465, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 465, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 465, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 465, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 465, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 465, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 465, 0);
										}
									}
									if (ans.getQuestion_id() == 98) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 455, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 455, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 455, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 455, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 455, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 455, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 455, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 455, 0);
										}
									}
									if (ans.getQuestion_id() == 99) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 445, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 445, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 445, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 445, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 445, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 445, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 445, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 445, 0);
										}

									}
									if (ans.getQuestion_id() == 100) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 435, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 435, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 435, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 435, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 435, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 435, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 435, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 435, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 435, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 435, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 435, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 435, 0);
										}
									}
									if (ans.getQuestion_id() == 101) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 430, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 430, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 430, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 430, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 430, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 430, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 430, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 430, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 430, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 430, 0);
										} else if (ans.getOption_group() == 0
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 110,
													400, 0);
										}
									}
									if (ans.getQuestion_id() == 102) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 390, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 390, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 390, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 390, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 390, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 390, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 390, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 390, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 390, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 390, 0);
										} else if (ans.getOption_group() == 0
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 110,
													352, 0);
										}
									}
									if (ans.getQuestion_id() == 103) {
										if ((ans.getOption_group() == 1
												|| ans.getOption_group() == 2
												|| ans.getOption_group() == 3
												|| ans.getOption_group() == 4 || ans
												.getOption_group() == 5)
												&& (ans.getOption_order() == 1 || ans
														.getOption_order() == 2)
												&& ans.getAnswer2() == null) {
											jawab = null;
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 100, 700, 0);
											// --pp
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 342, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 370, 342, 0);
										}
										// --tu
										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 342, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 342, 0);
										}
										// --tt1
										else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 342, 0);
										} else if (ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 342, 0);
										}
										// --tt2
										else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 342, 0);
										} else if (ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 342, 0);
										}
										// --tt3
										else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 342, 0);
										} else if (ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 342, 0);
										}
										// --tt4
										else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 342, 0);
										} else if (ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 342, 0);
										} else if (ans.getOption_group() == 0
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 110,
													282, 0);
										}
									}
									if (ans.getQuestion_id() == 104) {
										if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 370,
													272, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 370,
													262, 0);
										}

										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 400,
													272, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 400,
													262, 0);
										}

										else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 425,
													272, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 425,
													262, 0);
										}

										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 463,
													272, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 463,
													262, 0);
										}

										else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 493,
													272, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 493,
													262, 0);
										}

										else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 540,
													272, 0);
										} else if (ans.getOption_group() == 2
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 540,
													262, 0);
										}
									}
								}
							}	
			}
			over.endText();
			
			
			// ------------Halaman sembilan-----------
			if (mspo_flag_spaj.equals("3")){
				
			
			over = stamp.getOverContent(9);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			if (rslt3.size() > 0 ) {
				Integer j = 0;
				for (int i = 0; i < rslt3.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt3.get(i);
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 700 - j, 0);
							// --pp
							
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 681 - j, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									360, 681 - j, 0);
						}
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									395, 683 - j, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
									395, 683 - j, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1/pt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 
										&& ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											425, 685 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											425, 685 - j, 0);
								}
								// --tt2/pt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 
										&& ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											463, 688 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											463, 688 - j, 0);
								}
								// --tt3/pt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 
										&& ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											493, 690 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											493, 690 - j, 0);
								}
								// --tt4/pt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 
										&& ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											540, 692 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
											540, 692 - j, 0);
								}
								
							}
						}else if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
										395, 683 - j, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT, jawab,
										395, 683 - j, 0);
							}
						}
					
					j += 1;
				}
			}

			if (rslt4.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt4.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt4.get(i);
					if (ans.getQuestion_id() == 137) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 700, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 356, 0);
							
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 356, 0);
						}
						 
						 else if (ans.getOption_group() == 0) {
							jawab = ans.getAnswer2();
							over.setFontAndSize(italic, 6);
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									Common.isEmpty(jawab) ? "-" : jawab, 100,
									320, 0);
						}
						
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								// --tu/pu
								if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 356, 0);
								}
								else if (pesertaPlus.getFlag_jenis_peserta()==0 && ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 356, 0);
								}
								// --tt1
								else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 356, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 356, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 356, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 356, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 356, 0);
								}
								
							}
						}else if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 356, 0);
								
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 356, 0);
							}
						}

					}
							
					if (ans.getQuestion_id() == 139) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 288, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 288, 0);
						}

						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 288, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 288, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 288, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 288, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 &&ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 288, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 288, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 288, 0);
								}
							}
						}else if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 288, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 288, 0);
							}
						}
						
					}
					// j += 3;
					if (ans.getQuestion_id() >= 140
							&& ans.getQuestion_id() <= 143) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 700, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 323 - j, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 323 - j, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 327 - j, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 327 - j, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 331 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 331 - j, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 335 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 335 - j, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 339 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 339 - j, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 343 - j, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 343 - j, 0);
								}
							}
							}else if (dataTT.getLsre_id()==1){
								 if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 395, 324 - j, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 395, 324 - j, 0);
									}
							}
						}
						
					j += 2;
					if (ans.getQuestion_id() == 144) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 188, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 188, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 188, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 188, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 188, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 188, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 188, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 188, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 188, 0);
								}
							}
							}else if (dataTT.getLsre_id()==1){
								if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 186, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 395, 186, 0);
								}
							}
						}
						
							
					if (ans.getQuestion_id() == 145) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 178, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 168, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 168, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 168, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 168, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 168, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 168, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 168, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 168, 0);
								}
							}
						}else if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 166, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 166, 0);
							}
						}
					}
				}
			}

			over.endText();

			// --------------Halaman sepuluh--------------//
			over = stamp.getOverContent(10);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			if (rslt5.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < rslt5.size(); i++) {
					MstQuestionAnswer ans = (MstQuestionAnswer) rslt5.get(i);
					if (ans.getQuestion_id() == 146) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 735, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 735, 0);
						}
						// --tu
						else if ( ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 735, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 735, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 735, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 735, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 735, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 735, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 735, 0);
								}
							}
						}else  if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 735, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 735, 0);
							}
						}
					}
					// j += 3;
					if (ans.getQuestion_id() == 147) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 697,
								0);
					} else if (ans.getQuestion_id() == 148) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 667,
								0);
					} else if (ans.getQuestion_id() == 149) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 637,
								0);
					} else if (ans.getQuestion_id() == 150) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 607,
								0);
					} else if (ans.getQuestion_id() == 151) {
						jawab = ans.getAnswer2();
						over.setFontAndSize(italic, 6);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								Common.isEmpty(jawab) ? "-" : jawab, 120, 578,
								0);
					}
					if (ans.getQuestion_id() == 152) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 588, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 588, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 588, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 588, 0);
						}
						
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);						
								// --tt1
								if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 588, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 588, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 588, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 588, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 588, 0);
								}
							}
						}else if(ans.getAnswer2() != null && dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 588, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 588, 0);
							}
						}
							
					}
					if (ans.getQuestion_id() == 153) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 568, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 568, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 568, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 568, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 568, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 568, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 568, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 568, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 568, 0);
								}
							}
						}else if(ans.getAnswer2() != null && dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 568, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 568, 0);
							}
						}
						
					}
					if (ans.getQuestion_id() == 154) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 548, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 548, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 548, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 548, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 548, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 548, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 548, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 548, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 548, 0);
								}
							}
						}else if(ans.getAnswer2() != null && dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 548, 0);
							} else if (ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 548, 0);
							}
						}
						
					}
					if (ans.getQuestion_id() == 155) {
						if ((ans.getOption_group() == 1
								|| ans.getOption_group() == 2
								|| ans.getOption_group() == 3
								|| ans.getOption_group() == 4 || ans
								.getOption_group() == 5)
								&& (ans.getOption_order() == 1 || ans
										.getOption_order() == 2)
								&& ans.getAnswer2() == null) {
							jawab = null;
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 100, 676, 0);
							// --pp
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 528, 0);
						} else if (ans.getOption_group() == 1
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 360, 528, 0);
						}
						// --tu
						else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 1
								&& ans.getAnswer2().equals("1")) {
							jawab = "Ya";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 528, 0);
						} else if (ans.getOption_group() == 2
								&& ans.getOption_order() == 2
								&& ans.getAnswer2().equals("1")) {
							jawab = "Tidak";
							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
									jawab, 395, 528, 0);
						}
						if (peserta.size() > 0 && ans.getAnswer2() != null) {
							for (int x = 0; x < peserta.size(); x++) {
								PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
								
								// --tt1
								 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 425, 528, 0);
								}
								// --tt2
								else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 463, 528, 0);
								}
								// --tt3
								else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 493, 528, 0);
								}
								// --tt4
								else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 528, 0);
								} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 540, 528, 0);
								}
							}
						}else if (dataTT.getLsre_id()==1){
							if (ans.getOption_group() == 1
									&& ans.getOption_order() == 1
									&& ans.getAnswer2().equals("1")) {
								jawab = "Ya";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 528, 0);
							} else if ( ans.getOption_group() == 1
									&& ans.getOption_order() == 2
									&& ans.getAnswer2().equals("1")) {
								jawab = "Tidak";
								over.showTextAligned(PdfContentByte.ALIGN_LEFT,
										jawab, 395, 528, 0);
							}
						}	
					}
				}
				j += 2;
			}
			over.endText();
			stamp.close();
			}
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try {

				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");

				in = new FileInputStream(l_file);
				ouputStream = response.getOutputStream();

				IOUtils.copy(in, ouputStream);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (ouputStream != null) {
						ouputStream.flush();
						ouputStream.close();
					}
				} catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		return null;
	
}
	
	public ModelAndView espajonlinegadgetsiosyariah(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String reg_spaj = request.getParameter("spaj");
		Integer type = null;
		Date sysdate = elionsManager.selectSysdate();
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		List<String> pdfs = new ArrayList<String>();
		Boolean suksesMerge = false;
		HashMap<String, Object> cmd = new HashMap<String, Object>();
		ArrayList data_answer = new ArrayList();
		Integer index = null;
		Integer index2 = null;
		String spaj = "";
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj;
		System.out.print(mspo_flag_spaj);
		String dir = props.getProperty("pdf.template.espajonlinegadget");
		OutputStream output;
		PdfReader reader;
		File userDir = new File(props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "GADGET");
		moreInfo.put("Subject", "E-SPAJ ONLINE");

		PdfContentByte over;
		PdfContentByte over2;
		BaseFont times_new_roman = BaseFont.createFont(
				"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
		BaseFont italic = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ariali.ttf",
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

				reader = new PdfReader(
						props.getProperty("pdf.template.espajonlinegadget")
								+ "\\spajonlinegadgetsiosyariah.pdf");
				output = new FileOutputStream(exportDirectory + "\\"
						+ "espajonlinegadget_" + reg_spaj + ".pdf");

				spaj = dir + "\\spajonlinegadgetsiosyariah.pdf";
			
			pdfs.add(spaj);
			suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
			String outputName = props.getProperty("pdf.dir.export") + "\\"
					+ cabang + "\\" + reg_spaj + "\\" + "espajonlinegadget_"
					+ reg_spaj + ".pdf";
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
					outputName));

			Pemegang dataPP = elionsManager.selectpp(reg_spaj);
			Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
			PembayarPremi pembPremi = bacManager.selectP_premi(reg_spaj);
			if (pembPremi == null)
				pembPremi = new PembayarPremi();
			AddressBilling addrBill = this.elionsManager
					.selectAddressBilling(reg_spaj);
			Datausulan dataUsulan = this.elionsManager
					.selectDataUsulanutama(reg_spaj);
			dataUsulan.setDaftaRider(elionsManager
					.selectDataUsulan_rider(reg_spaj));
			InvestasiUtama inv = this.elionsManager
					.selectinvestasiutama(reg_spaj);
			Rekening_client rekClient = this.elionsManager
					.select_rek_client(reg_spaj);
			Account_recur accRecur = this.elionsManager
					.select_account_recur(reg_spaj);
			List detInv = this.bacManager.selectdetilinvestasi2(reg_spaj);
			List benef = this.elionsManager.select_benef(reg_spaj);
			List peserta = this.uwManager.select_all_mst_peserta(reg_spaj);
			List dist = this.elionsManager.select_tipeproduk();
			List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
			HashMap spajTemp = (HashMap) listSpajTemp.get(0);
			String idgadget = (String) spajTemp.get("NO_TEMP");
			Map agen = this.bacManager.selectAgenESPAJSimasPrima(reg_spaj);
			List namaBank = uwManager.namaBank(accRecur.getLbn_id());

			// --Question Full Konven/Syariah
			List rslt = bacManager.selectQuestionareGadget(reg_spaj, 2, 1, 15);	
			List rslt2 = bacManager.selectQuestionareGadget(reg_spaj, 1, 16, 18); 
			List rslt3 = bacManager.selectQuestionareGadget(reg_spaj, 3, 106, 136); 
			List rslt4 = bacManager.selectQuestionareGadget(reg_spaj, 3, 137, 145);
			List rslt5 = bacManager.selectQuestionareGadget(reg_spaj, 3, 146, 155);
			
			//Sio
			List rslt6 = bacManager.selectQuestionareGadget(reg_spaj, 12, 81, 104);
			
			String s_channel = "";
			for (int i = 0; i < dist.size(); i++) {
				HashMap dist2 = (HashMap) dist.get(i);
				Integer i_lstp_id = (Integer) dist2.get("lstp_id");
				if (i_lstp_id.intValue() == dataUsulan.getTipeproduk()
						.intValue()) {
					s_channel = (String) dist2.get("lstp_produk");
				}
			}

			Double d_premiRider = 0.;
			if (dataUsulan.getDaftaRider().size() > 0) {
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					d_premiRider = rider.getMspr_premium();
				}
			}
			Double d_topUpBerkala = new Double(0);
			Double d_topUpTunggal = new Double(0);
			Double d_totalTopup = new Double(0);
			if (inv != null) {
				DetilTopUp daftarTopup = inv.getDaftartopup();
				d_topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala()) ? new Double(
						0) : daftarTopup.getPremi_berkala();
				d_topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal()) ? new Double(
						0) : daftarTopup.getPremi_tunggal();
				d_totalTopup = d_topUpBerkala + d_topUpTunggal;
			}
			Double d_premiExtra = (Common.isEmpty(uwManager
					.selectSumPremiExtra(reg_spaj)) ? 0. : uwManager
					.selectSumPremiExtra(reg_spaj));
			Double d_totalPremi = dataUsulan.getMspr_premium() + d_totalTopup
					+ d_premiRider + d_premiExtra;

			stamp.setMoreInfo(moreInfo);

			// ---------- Data Halaman Pertama ----------

			over = stamp.getOverContent(1);
			over.beginText();
			over.setFontAndSize(times_new_roman, 8);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatString.nomorSPAJ(reg_spaj), 380, 627, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(sysdate), 85, 617, 0);

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 160, 516, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 160, 506, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 160, 496, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 160, 486, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 290, 476, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 290, 467, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 290, 457, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiRider.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiRider))), 290, 447, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 290, 437, 0);

			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_AGEN").toString().toUpperCase()) ? "-" : 
						agen.get("NM_AGEN").toString().toUpperCase(),160, 409,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_AGEN").toString().toUpperCase()) ? "-" : 
						agen.get("KD_AGEN").toString().toUpperCase(),160, 399,0);
			over.endText();

			// ---------- Data Halaman keempat ----------
			over = stamp.getOverContent(4);
			over.beginText();
			
			// String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" +
			// (dataPP.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
			String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_"
					+ ".jpg";
			try {
				Image img = Image.getInstance(ttdPp);
				img.scaleAbsolute(30, 30);
				over.addImage(img, img.getScaledWidth(), 0, 0,
						img.getScaledHeight(), 438, 643);
				over.stroke();

				// String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
				// + (dataTT.getMcl_first().toUpperCase()).replace(" ", "") +
				// ".jpg";
				String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
						+ ".jpg";
				if (dataTT.getMste_age() < 17 || dataPP.getLsre_id() == 1)
					ttdTu = ttdPp;
				Image img2 = Image.getInstance(ttdTu);
				img2.scaleAbsolute(30, 30);
				over.addImage(img2, img2.getScaledWidth(), 0, 0,
						img2.getScaledHeight(), 438, 593);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('TTD Pemegang Polis / Tertanggung Utama Tidak Ditemukan');window.close();</script>");
				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toString(dataPP.getMspo_spaj_date()), 370, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataPP
					.getMcl_first().toUpperCase(), 295, 655, 0);
			over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataTT
					.getMcl_first().toUpperCase(), 295, 605, 0);
			if (peserta.size() > 0) {
				Integer vertikal = 605;
				for (int i = 0; i < peserta.size(); i++) {
					vertikal = vertikal - 50;
					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					if (pesertaPlus.getFlag_jenis_peserta() > 0){
						over.showTextAligned(PdfContentByte.ALIGN_CENTER,
								pesertaPlus.getNama().toUpperCase(), 290, vertikal,
								0);
						vertikal = vertikal + 2;
					}
					
				}
			}

			try {
				String ttdPenutup = exportDirectory + "\\" + idgadget
						+ "_TTD_PENUTUP_" + ".jpg";
				Image img3 = Image.getInstance(ttdPenutup);
				img3.scaleAbsolute(30, 30);
				over.addImage(img3, img3.getScaledWidth(), 0, 0,
						img3.getScaledHeight(), 120, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Penutup Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			try {
				String ttdReff = exportDirectory + "\\" + idgadget
						+ "_TTD_REF_" + ".jpg";
				Image img4 = Image.getInstance(ttdReff);
				img4.scaleAbsolute(30, 30);
				over.addImage(img4, img4.getScaledWidth(), 0, 0,
						img4.getScaledHeight(), 290, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Refferal Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}
			
			try {
				String ttdAgen = exportDirectory + "\\" + idgadget
						+ "_TTD_AGEN_" + ".jpg";
				Image img5 = Image.getInstance(ttdAgen);
				img5.scaleAbsolute(30, 30);
				over.addImage(img5, img5.getScaledWidth(), 0, 0,
						img5.getScaledHeight(), 440, 280);
				over.stroke();
			} catch (FileNotFoundException e) {
				logger.error("ERROR :", e);
//				ServletOutputStream sos = response.getOutputStream();
//				sos.println("<script>alert('TTD Agen Tidak Ditemukan');window.close();</script>");
//				sos.close();
			}

			over.setFontAndSize(times_new_roman, 6);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_PENUTUP")) ? "-" : 
						agen.get("NM_PENUTUP").toString().toUpperCase(),100, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_PENUTUP")) ? "-" : 
						agen.get("KD_PENUTUP").toString().toUpperCase(),100, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_PENUTUP")) ? "-" : 
						agen.get("CB_PENUTUP").toString().toUpperCase(),100, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_REFFERAL")) ? "-" : 
						agen.get("NM_REFFERAL").toString().toUpperCase(),270, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_REFFERAL")) ? "-" : 
						agen.get("KD_REFFERAL").toString().toUpperCase(),270, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_REFFERAL")) ? "-" : 
						agen.get("CB_REFFERAL").toString().toUpperCase(),270, 240,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("NM_AGEN")) ? "-" : 
						agen.get("NM_AGEN").toString().toUpperCase(),440, 260,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("KD_AGEN")) ? "-" : 
						agen.get("KD_AGEN").toString().toUpperCase(),440, 250,0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(agen.get("CB_AGEN")) ? "-" : 
						agen.get("CB_AGEN").toString().toUpperCase(),440, 240,0);

			over.endText();

			// //---------- Data Halaman kelima ----------
			over = stamp.getOverContent(5);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMcl_first().toUpperCase(), 250, 725, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_gelar()) ? "-" : dataPP
							.getMcl_gelar(), 250, 715, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_mother(), 250, 705, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsne_note(), 250, 695, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_green_card()) ? "TIDAK"
							: dataPP.getMcl_green_card(), 250, 685, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLside_name(), 250, 675, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_no_identity(), 250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataPP
									.getMspe_no_identity_expired()), 250, 656,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataPP.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataPP.getMspe_date_birth()),
					250, 646, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMspo_age() + " Tahun", 250, 636, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sex2().toUpperCase(), 250, 626, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataPP
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					617, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsag_name(), 250, 607, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getLsed_name(), 250, 596, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_company_name()) ? "-" : dataPP
							.getMcl_company_name(), 250, 587, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataPP.getMkl_kerja(), 250, 568, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKerjab(),
					250, 559, 0);
			int monyong = 0;
			String[] uraian_tugas = StringUtil.pecahParagraf(dataTT
					.getMkl_kerja_ket().toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 549 - monyong, 0);
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getMkl_kerja_ket())?"-":dataPP.getMkl_kerja_ket(),
			// 250, 549, 0);
			monyong = 0;
			if(!Common.isEmpty(dataTT.getAlamat_kantor())){
				String[] alamat = StringUtil.pecahParagraf(dataTT.getAlamat_kantor().toUpperCase(), 75);
	        	if(!Common.isEmpty(alamat)){
		        	for(int i=0; i<alamat.length; i++) {
		        		monyong = 7 * i;
		        		over.showTextAligned(PdfContentByte.ALIGN_LEFT, alamat[i], 250,
								529 - monyong, 0);
		        	}
	        	}
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getAlamat_kantor())?"-":dataPP.getAlamat_kantor(),
			// 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_kantor()) ? "-" : dataPP
							.getKota_kantor(), 250, 509, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_kantor()) ? "-" : dataPP
							.getKd_pos_kantor(), 250, 500, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_kantor()) ? "-" : dataPP
							.getTelpon_kantor(), 250, 490, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_kantor2())?"-":dataPP.getTelpon_kantor2(),
			// 250, 505, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 480, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_rumah()) ? "-" : dataPP
							.getAlamat_rumah(), 250, 470, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_rumah()) ? "-" : dataPP
							.getKota_rumah(), 250, 460, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_rumah()) ? "-" : dataPP
							.getKd_pos_rumah(), 250, 451, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
							.getTelpon_rumah(), 250, 441, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
			// 250, 445, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 432, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getAlamat_tpt_tinggal()) ? "-"
							: dataPP.getAlamat_tpt_tinggal(), 250, 422, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKota_tpt_tinggal()) ? "-" : dataPP
							.getKota_tpt_tinggal(), 250, 412, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getKd_pos_tpt_tinggal()) ? "-"
							: dataPP.getKd_pos_tpt_tinggal(), 250, 402, 0);
//			over.showTextAligned(
//					PdfContentByte.ALIGN_LEFT,
//					Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
//							.getTelpon_rumah(), 250, 393, 0);
			 over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			 Common.isEmpty(dataPP.getTelpon_rumah2())?"-":dataPP.getTelpon_rumah2(),
					 250, 393, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
					250, 383, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_address(), 250, 373, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getKota(), 250, 353, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_zip_code(), 250, 343, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_phone1(), 250, 334, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
							.getMsap_fax1(), 250, 323, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getNo_hp()) ? "-" : dataPP.getNo_hp(),
					250, 313, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getEmail()) ? "-" : dataPP.getEmail(),
					250, 303, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMcl_npwp()) ? "-" : dataPP
							.getMcl_npwp(), 250, 294, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_penghasilan()) ? "-" : dataPP
							.getMkl_penghasilan(), 250, 285, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_pendanaan()) ? "-" : dataPP
							.getMkl_pendanaan(), 250, 273, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataPP.getMkl_tujuan()) ? "-" : dataPP
							.getMkl_tujuan(), 250, 264, 0);

			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getLsre_relation().toUpperCase(), 250, 234, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMcl_first().toUpperCase(), 250, 215, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_gelar()) ? "-" : dataTT
							.getMcl_gelar(), 250, 206, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_mother(), 250, 196, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsne_note(), 250, 186, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_green_card()) ? "TIDAK"
							: dataTT.getMcl_green_card(), 250, 176, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLside_name(), 250, 166, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_no_identity(), 250, 156, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMspe_no_identity_expired()) ? "-"
							: FormatDate.toString(dataTT
									.getMspe_no_identity_expired()), 250, 146,
					0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataTT.getMspe_place_birth() + ", "
							+ FormatDate.toString(dataTT.getMspe_date_birth()),
					250, 137, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMste_age() + " Tahun", 250, 127, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sex2().toUpperCase(), 250, 118, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
					.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataTT
					.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 250,
					108, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsag_name(), 250, 98, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getLsed_name(), 250, 88, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_company_name()) ? "-" : dataTT
							.getMcl_company_name(), 250, 78, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataTT.getMkl_kerja(), 250, 58, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getKerjab(),
					250, 49, 0);
			over.endText();

			//
			// //---------- Data Halaman keenam ----------
			over = stamp.getOverContent(6);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			monyong = 0;
			uraian_tugas = StringUtil.pecahParagraf(dataTT.getMkl_kerja_ket()
					.toUpperCase(), 70);
			for (int i = 0; i < uraian_tugas.length; i++) {
				monyong = 7 * i;
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						uraian_tugas[i], 250, 734 - monyong, 0);
			}

			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getMkl_kerja_ket())?"-":dataTT.getMkl_kerja_ket(),
			// 250, 734, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_kantor()) ? "-" : dataTT
							.getAlamat_kantor(), 250, 714, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_kantor()) ? "-" : dataTT
							.getKota_kantor(), 250, 695, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_kantor()) ? "-" : dataTT
							.getKd_pos_kantor(), 250, 685, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_kantor()) ? "-" : dataTT
							.getTelpon_kantor(), 250, 675, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_kantor2())?"-":dataTT.getTelpon_kantor2(),
			// 153, 105, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 665, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_rumah()) ? "-" : dataTT
							.getAlamat_rumah(), 250, 655, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_rumah()) ? "-" : dataTT
							.getKota_rumah(), 250, 645, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_rumah()) ? "-" : dataTT
							.getKd_pos_rumah(), 250, 635, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
							.getTelpon_rumah(), 250, 625, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah2())?"-":dataTT.getTelpon_rumah2(),
			// 153, 46, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 615, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getAlamat_tpt_tinggal()) ? "-"
							: dataTT.getAlamat_tpt_tinggal(), 250, 607, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKota_tpt_tinggal()) ? "-" : dataTT
							.getKota_tpt_tinggal(), 250, 597, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getKd_pos_tpt_tinggal()) ? "-"
							: dataTT.getKd_pos_tpt_tinggal(), 250, 587, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(dataTT.getTelpon_rumah())?"-":dataTT.getTelpon_rumah(),
			// 250, 597, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
							.getTelpon_rumah2(), 250, 578, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
					250, 567, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// Common.isEmpty(addrBill.getMsap_address())?"-":addrBill.getMsap_address(),
			// 208, 739, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getNo_hp()) ? "-" : dataTT.getNo_hp(),
					250, 557, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getEmail()) ? "-" : dataTT.getEmail(),
					250, 547, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMcl_npwp()) ? "-" : dataTT
							.getMcl_npwp(), 250, 537, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_penghasilan()) ? "-" : dataTT
							.getMkl_penghasilan(), 250, 529, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_pendanaan()) ? "-" : dataTT
							.getMkl_pendanaan(), 250, 519, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(dataTT.getMkl_tujuan()) ? "-" : dataTT
							.getMkl_tujuan(), 250, 509, 0);
			//
			// //Data Pembayar Premi
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getRelation_payor()) ? "-"
							: pembPremi.getRelation_payor(), 250, 478, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(pembPremi.getNama_pihak_ketiga()) ? "-"
					: pembPremi.getNama_pihak_ketiga(), 250, 468, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getKewarganegaraan()) ? "-"
							: pembPremi.getKewarganegaraan(), 250, 458, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getAlamat_lengkap()) ? "-"
							: pembPremi.getAlamat_lengkap(), 250, 450, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_rumah()) ? "-" : pembPremi
							.getTelp_rumah(), 250, 440, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTelp_kantor()) ? "-"
							: pembPremi.getTelp_kantor(), 250, 430, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getEmail()) ? "-" : pembPremi
							.getEmail(), 250, 420, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTempat_lahir()) ? "-"
							: (pembPremi.getTempat_lahir() + ", " + FormatDate.toString(pembPremi
									.getMspe_date_birth_3rd_pendirian())), 250,
					410, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getMkl_kerja()) ? "-" : pembPremi
							.getMkl_kerja(), 250, 401, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 392, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 382, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 372, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getNo_npwp()) ? "-" : pembPremi
							.getNo_npwp(), 250, 362, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getSumber_dana()) ? "-"
							: pembPremi.getSumber_dana(), 250, 352, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					Common.isEmpty(pembPremi.getTujuan_dana_3rd()) ? "-"
							: pembPremi.getTujuan_dana_3rd(), 250, 342, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 332, 0);
			//
			// //Data Usulan
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					s_channel.toUpperCase(), 250, 291, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 281, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLsdbs_name(), 250, 271, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspr_ins_period() + " Tahun", 250, 261, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMspo_pay_period() + " Tahun", 250, 251, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
					.isEmpty(dataUsulan.getMspo_installment()) ? "-"
					: dataUsulan.getMspo_installment() + "", 250, 242, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLscb_pay_mode(), 250, 232, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new
			// BigDecimal(dataUsulan.getMspr_premium())), 250, 286, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_tsi())), 250, 222, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
							.getMste_flag_cc() == 2 ? "TABUNGAN"
							: "KARTU KREDIT"), 250, 211, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_beg_date()),
					250, 202, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					FormatDate.toIndonesian(dataUsulan.getMste_end_date()),
					250, 193, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// FormatDate.toIndonesian(dataUsulan.getLsdbs_number()>800?dataUsulan.getLsdbs_name():"-"),
			// 250, 237, 0);
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 221,
			// 0);

			if (dataUsulan.getDaftaRider().size() > 0) {
				Integer j = 0;
				for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
					Datarider rider = (Datarider) dataUsulan.getDaftaRider()
							.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							rider.getLsdbs_name(), 250, 183, 0);
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getLku_symbol()
									+ " "
									+ FormatNumber
											.convertToTwoDigit(new BigDecimal(
													rider.getMspr_tsi())), 250,
													173, 0);
					j += 10;
				}
			}

			// //Data Investasi
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									dataUsulan.getMspr_premium())), 250, 105, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpBerkala.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpBerkala))), 250, 95, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_topUpTunggal.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_topUpTunggal))), 250, 85, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					d_premiExtra.doubleValue() == new Double(0) ? "-"
							: (dataUsulan.getLku_symbol() + " " + FormatNumber
									.convertToTwoDigit(new BigDecimal(
											d_premiExtra))), 250, 75, 0);
			over.showTextAligned(
					PdfContentByte.ALIGN_LEFT,
					dataUsulan.getLku_symbol()
							+ " "
							+ FormatNumber.convertToTwoDigit(new BigDecimal(
									d_totalPremi)), 250, 65, 0);
			Double d_jmlinves = new Double(0);
			String s_jnsinves = "";
			for (int i = 0; i < detInv.size(); i++) {
				DetilInvestasi detInves = (DetilInvestasi) detInv.get(i);
				d_jmlinves = d_jmlinves + detInves.getMdu_jumlah1();
				s_jnsinves = s_jnsinves
						+ detInves.getLji_invest1().toUpperCase() + " "
						+ detInves.getMdu_persen1() + "%";
				if (i != (detInv.size() - 1))
					s_jnsinves = s_jnsinves + ", ";
			}
			// over.showTextAligned(PdfContentByte.ALIGN_LEFT,
			// dataUsulan.getLku_symbol() + " " +
			// FormatNumber.convertToTwoDigit(new BigDecimal(d_jmlinves)), 208,
			// 183, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT, s_jnsinves, 250,
					55, 0);
			over.endText();

			// ---------- Data Halaman ketujuh----------

			over = stamp.getOverContent(7);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);

			// //Data Rekening
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getLsbp_nama(), 250, 724, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_cabang(), 250, 714, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_no_ac(), 250, 704, 0);
			over.showTextAligned(PdfContentByte.ALIGN_LEFT,
					rekClient.getMrc_nama(), 250, 694, 0);

			if (dataTT.getMste_flag_cc() == 1 || dataTT.getMste_flag_cc() == 2) {
				if (accRecur != null) {
					String bank_pusat = "";
					String bank_cabang = "";

					if (!namaBank.isEmpty()) {
						HashMap m = (HashMap) namaBank.get(0);
						bank_pusat = (String) m.get("LSBP_NAMA");
						bank_cabang = (String) m.get("LBN_NAMA");
					}
					over.showTextAligned(
							PdfContentByte.ALIGN_LEFT,
							dataUsulan.getMste_flag_cc() == 0 ? "TUNAI"
									: (dataUsulan.getMste_flag_cc() == 2 ? "TABUNGAN"
											: "KARTU KREDIT"), 250, 631, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							Common.isEmpty(bank_pusat) ? "-"
									: bank_pusat.toUpperCase()/*
															 * accRecur.getLbn_nama
															 * ().toUpperCase()
															 */, 250, 621, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(bank_cabang) ? "-"
							: bank_cabang.toUpperCase()/*
																 * accRecur.
																 * getLbn_nama
																 * ().
																 * toUpperCase()
																 */, 250, 611,
							0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_acc_no()) ? "-" : accRecur
							.getMar_acc_no().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 601, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
							.isEmpty(accRecur.getMar_holder()) ? "-" : accRecur
							.getMar_holder().toUpperCase()/*
														 * accRecur.getLbn_nama()
														 * .toUpperCase()
														 */, 250, 592, 0);
				}
			} else {
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 631,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 621,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 611,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 601,
						0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 250, 592,
						0);
			}

			if (peserta.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < peserta.size(); i++) {

					PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
					if (pesertaPlus.getFlag_jenis_peserta() > 0){
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getNama().toUpperCase(), 80, 517 - j,
								0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getLsre_relation(), 190, 517 - j, 0);
						over.showTextAligned(
								PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getTinggi() + "/"
										+ pesertaPlus.getBerat(), 260, 517 - j, 0);
//						String[] pekerjaan = StringUtil.pecahParagraf(pesertaPlus.
//								getKerja().toUpperCase(), 15);
//							over.showTextAligned(PdfContentByte.ALIGN_LEFT,
//									pekerjaan[i], 290, 519 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getKerja(), 290, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								pesertaPlus.getSex(), 375, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								FormatDate.toString(pesertaPlus.getTgl_lahir()),
								430, 517 - j, 0);
						over.showTextAligned(PdfContentByte.ALIGN_LEFT,
								dataPP.getLsne_note(), 480, 517 - j, 0);
						j += 10;
					}
					
				}
			}
			if (benef.size() > 0) {
				Integer j = 0;
				for (int i = 0; i < benef.size(); i++) {
					Benefeciary benefit = (Benefeciary) benef.get(i);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_first(), 60, 410 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getSmsaw_birth(), 200, 410 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_sex() == 1 ? "LAKI-LAKI" : "PEREMPUAN",
							270, 410 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							benefit.getMsaw_persen() + "%", 330, 410 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT, benefit
							.getLsre_relation().toUpperCase(), 370, 410 - j, 0);
					over.showTextAligned(PdfContentByte.ALIGN_LEFT,
							dataPP.getLsne_note(), 480, 410 - j, 0);
					j += 10;
				}
			}
			over.endText();
			
			// ------------Halaman delapan-----------
			over = stamp.getOverContent(8);
			over.beginText();
			over.setFontAndSize(times_new_roman, 6);
			String jawab;

				 if (rslt6.size() > 0) {
						// /81, 104
						Integer j = 0;
						Integer k = 0;
						Integer l = 0;
						for (int i = 0; i < rslt6.size(); i++) {
							MstQuestionAnswer ans = (MstQuestionAnswer) rslt6.get(i);
							// j += 3;
							if (ans.getQuestion_id() > 80 && ans.getQuestion_id() < 83) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 745 - j, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 745 - j, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 765 - j, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 765 - j, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 765 - j, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 765 - j, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 765 - j, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 765 - j, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 765 - j, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 745 - j, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 745 - j, 0);
									}
								}

							}
							j += 10;
							if (ans.getQuestion_id() == 84) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 595, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 595, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 595, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 595, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 595, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 595, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 595, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 595, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 595, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 595, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 595, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 85) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 585, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 585, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 585, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 585, 0);
								}

								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 585, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 585, 0);
										}
										//--tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 585, 0);
										}
										//--tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 585, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 585, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 585, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 585, 0);
									}
								}

							}
							if (ans.getQuestion_id() == 86) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 575, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 575, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 575, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 575, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 575, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 575, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 575, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 575, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 575, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if ( ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 575, 0);
									} else if ( ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 575, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 87) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 565, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 565, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 565, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 565, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 565, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 565, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 565, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 565, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 565, 0);
										}
										
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 565, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 565, 0);
									}
								}

							}
							if (ans.getQuestion_id() == 88) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 555, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 555, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 555, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 555, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 555, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 555, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 555, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 555, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 555, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 555, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 555, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 89) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 545, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 545, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 545, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 545, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 545, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 545, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 545, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 545, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 545, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 545, 0);
										}
								}
							
							}
							if (ans.getQuestion_id() == 90) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 535, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 535, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 535, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 535, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 535, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 535, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 535, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 535, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 535, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 535, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 535, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 91) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 525, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 525, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 525, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 525, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
									
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 525, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 525, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 525, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 525, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 525, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 525, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 525, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 92) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 515, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 515, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 515, 0);
									} else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 515, 0);
									}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 515, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 515, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 515, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 515, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 515, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 515, 0);
										}
								}
								

							}
							if (ans.getQuestion_id() == 93) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 425, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 505, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 505, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 505, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 505, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 505, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 505, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 505, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 505, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 505, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 505, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 94) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 495, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 495, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 495, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 495, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 495, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 495, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 495, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 495, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 495, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 495, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 495, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 95) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 485, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 485, 0);
								}// --tu
								else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 485, 0);
									} else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 485, 0);
									}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 485, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 485, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 485, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 485, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 485, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 485, 0);
										}
								}
							}
							
							if (ans.getQuestion_id() == 96) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 475, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 475, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 475, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 475, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 475, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 475, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 475, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 475, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 475, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 475, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 475, 0);
									}
								}
							}
									
							if (ans.getQuestion_id() == 97) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 465, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 465, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 465, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 465, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 465, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 465, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 465, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 465, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 465, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if ( ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 465, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 465, 0);
									}
								}
							}
							if (ans.getQuestion_id() == 98) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 455, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 455, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 455, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 455, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 455, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 455, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 455, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 455, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 455, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 455, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 455, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 99) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 445, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 445, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 445, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 445, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 445, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 445, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 445, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 445, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 445, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 445, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 445, 0);
									}
								}

							}
							if (ans.getQuestion_id() == 100) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 435, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 435, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 435, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 435, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 435, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 435, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 435, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 435, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 435, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 435, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 435, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 101) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 430, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 430, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 430, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 430, 0);
								}
								else if (ans.getOption_group() == 0
										&& ans.getOption_order() == 1) {
									jawab = ans.getAnswer2();
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 110,
											400, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 430, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 430, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 430, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 430, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 430, 0);
										}
									}
								}
								 else if (dataTT.getLsre_id()==1){
									 if ( ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										} else if ( ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 430, 0);
										}
								}
							}
							
							if (ans.getQuestion_id() == 102) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 390, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 390, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 390, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 390, 0);
								}
								else if (ans.getOption_group() == 0
										&& ans.getOption_order() == 1) {
									jawab = ans.getAnswer2();
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 110,
											352, 0);
								}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 390, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 390, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 390, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 390, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 390, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									 if (ans.getOption_group() == 1
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										} else if (ans.getOption_group() == 1
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 400, 390, 0);
										}
								}
								 
							}
							if (ans.getQuestion_id() == 103) {
								if ((ans.getOption_group() == 1
										|| ans.getOption_group() == 2
										|| ans.getOption_group() == 3
										|| ans.getOption_group() == 4 || ans
										.getOption_group() == 5)
										&& (ans.getOption_order() == 1 || ans
												.getOption_order() == 2)
										&& ans.getAnswer2() == null) {
									jawab = null;
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 100, 700, 0);
									// --pp
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 342, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 370, 342, 0);
								}
								// --tu
								else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 1
										&& ans.getAnswer2().equals("1")) {
									jawab = "Ya";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 342, 0);
								} else if (ans.getOption_group() == 2
										&& ans.getOption_order() == 2
										&& ans.getAnswer2().equals("1")) {
									jawab = "Tidak";
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											jawab, 400, 342, 0);
								}
								 else if (ans.getOption_group() == 0
											&& ans.getOption_order() == 1) {
										jawab = ans.getAnswer2();
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 110,
												282, 0);
									}
								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										
										// --tt1
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 425, 342, 0);
										}
										// --tt2
										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 463, 342, 0);
										}
										// --tt3
										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 493, 342, 0);
										}
										// --tt4
										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1
												&& ans.getAnswer2().equals("1")) {
											jawab = "Ya";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 342, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2
												&& ans.getAnswer2().equals("1")) {
											jawab = "Tidak";
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													jawab, 540, 342, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1
											&& ans.getAnswer2().equals("1")) {
										jawab = "Ya";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 342, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2
											&& ans.getAnswer2().equals("1")) {
										jawab = "Tidak";
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												jawab, 400, 342, 0);
									}
								}
								
							}
							if (ans.getQuestion_id() == 104) {
								if (ans.getOption_group() == 1
										&& ans.getOption_order() == 1) {
									jawab = ans.getAnswer2();
									over.setFontAndSize(italic, 6);
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 370,
											272, 0);
								} else if (ans.getOption_group() == 1
										&& ans.getOption_order() == 2) {
									jawab = ans.getAnswer2();
									over.setFontAndSize(italic, 6);
									over.showTextAligned(PdfContentByte.ALIGN_LEFT,
											Common.isEmpty(jawab) ? "-" : jawab, 370,
											262, 0);
								}
								else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 1) {
										jawab = ans.getAnswer2();
										over.setFontAndSize(italic, 6);
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 400,
												272, 0);
								} else if (ans.getOption_group() == 2
											&& ans.getOption_order() == 2) {
										jawab = ans.getAnswer2();
										over.setFontAndSize(italic, 6);
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 400,
												262, 0);
								}

								if (peserta.size() > 0 && ans.getAnswer2() != null) {
									for (int x = 0; x < peserta.size(); x++) {
										PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(x);
										 if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 425,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==1 && ans.getOption_group() == 3
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 425,
													262, 0);
										}

										else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 463,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==2 && ans.getOption_group() == 4
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 463,
													262, 0);
										}

										else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 493,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==3 && ans.getOption_group() == 5
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 493,
													262, 0);
										}

										else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 1) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 540,
													272, 0);
										} else if (pesertaPlus.getFlag_jenis_peserta()==4 && ans.getOption_group() == 6
												&& ans.getOption_order() == 2) {
											jawab = ans.getAnswer2();
											over.setFontAndSize(italic, 6);
											over.showTextAligned(PdfContentByte.ALIGN_LEFT,
													Common.isEmpty(jawab) ? "-" : jawab, 540,
													262, 0);
										}
									}
								}else if (dataTT.getLsre_id()==1){
									if (ans.getOption_group() == 1
											&& ans.getOption_order() == 1) {
										jawab = ans.getAnswer2();
										over.setFontAndSize(italic, 6);
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 400,
												272, 0);
									} else if (ans.getOption_group() == 1
											&& ans.getOption_order() == 2) {
										jawab = ans.getAnswer2();
										over.setFontAndSize(italic, 6);
										over.showTextAligned(PdfContentByte.ALIGN_LEFT,
												Common.isEmpty(jawab) ? "-" : jawab, 400,
												262, 0);
									}
								}
							}
						}
					}	
			
			over.endText();
			stamp.close();
			
			File l_file = new File(outputName);
			FileInputStream in = null;
			ServletOutputStream ouputStream = null;
			try {

				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "Inline");
				response.setHeader("Expires", "0");
				response.setHeader("Cache-Control",
						"must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");

				in = new FileInputStream(l_file);
				ouputStream = response.getOutputStream();

				IOUtils.copy(in, ouputStream);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (ouputStream != null) {
						ouputStream.flush();
						ouputStream.close();
					}
				} catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}
		return null;	
}
	
	public ModelAndView espajonlinegadgetexisting(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String reg_spaj = request.getParameter("spaj");
		Integer type = null;
		Integer question;
		Date sysdate = elionsManager.selectSysdate();
		Integer syariah = this.elionsManager.getUwDao().selectSyariah(reg_spaj);
		List<String> pdfs = new ArrayList<String>();
		Boolean suksesMerge = false;
		HashMap<String, Object> cmd = new HashMap<String, Object>();
		ArrayList data_answer = new ArrayList();
		Integer index = null;
		Integer index2 = null;
		String spaj = "";
		String mspo_flag_spaj = bacManager.selectMspoFLagSpaj(reg_spaj);
		String cabang = elionsManager.selectCabangFromSpaj(reg_spaj);
		String exportDirectory = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj;
		System.out.print(mspo_flag_spaj);
		String dir = props.getProperty("pdf.template.espajonlinegadget");
		OutputStream output;
		PdfReader reader;
		File userDir = new File(props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj);
		if (!userDir.exists()) {
			userDir.mkdirs();
		}

		HashMap moreInfo = new HashMap();
		moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
		moreInfo.put("Title", "GADGET");
		moreInfo.put("Subject", "E-SPAJ ONLINE");

		PdfContentByte over;
		PdfContentByte over2;
		BaseFont times_new_roman = BaseFont.createFont(
				"C:\\WINDOWS\\FONTS\\ARIAL.TTF", BaseFont.CP1252,
				BaseFont.NOT_EMBEDDED);
		BaseFont italic = BaseFont.createFont("C:\\WINDOWS\\FONTS\\ariali.ttf",
				BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		
		 // --------------spaj template existing
		reader = new PdfReader(
				props.getProperty("pdf.template.espajonlinegadget")
						+ "\\spajonlinegadget.pdf");
		output = new FileOutputStream(exportDirectory + "\\"
				+ "espajonlinegadget_" + reg_spaj + ".pdf");

		spaj = dir + "\\spajonlinegadget.pdf";
		pdfs.add(spaj);
		suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
		String outputName = props.getProperty("pdf.dir.export") + "\\"
				+ cabang + "\\" + reg_spaj + "\\" + "espajonlinegadget_"
				+ reg_spaj + ".pdf";
		PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
				outputName));

		Pemegang dataPP = elionsManager.selectpp(reg_spaj);
		Tertanggung dataTT = elionsManager.selectttg(reg_spaj);
		PembayarPremi pembPremi = bacManager.selectP_premi(reg_spaj);
		if (pembPremi == null)
			pembPremi = new PembayarPremi();
		AddressBilling addrBill = this.elionsManager
				.selectAddressBilling(reg_spaj);
		Datausulan dataUsulan = this.elionsManager
				.selectDataUsulanutama(reg_spaj);
		dataUsulan.setDaftaRider(elionsManager
				.selectDataUsulan_rider(reg_spaj));
		InvestasiUtama inv = this.elionsManager
				.selectinvestasiutama(reg_spaj);
		Rekening_client rekClient = this.elionsManager
				.select_rek_client(reg_spaj);
		Account_recur accRecur = this.elionsManager
				.select_account_recur(reg_spaj);
		List detInv = this.bacManager.selectdetilinvestasi2(reg_spaj);
		List benef = this.elionsManager.select_benef(reg_spaj);
		List peserta = this.uwManager.select_all_mst_peserta(reg_spaj);
		List dist = this.elionsManager.select_tipeproduk();
		List listSpajTemp = bacManager.selectReferensiTempSpaj(reg_spaj);
		HashMap spajTemp = (HashMap) listSpajTemp.get(0);
		String idgadget = (String) spajTemp.get("NO_TEMP");
		String submitGadget = (String) spajTemp.get("TGL_UPLOAD");
		Map agen = this.bacManager.selectAgenESPAJSimasPrima(reg_spaj);

		String s_channel = "";
		for (int i = 0; i < dist.size(); i++) {
			HashMap dist2 = (HashMap) dist.get(i);
			Integer i_lstp_id = (Integer) dist2.get("lstp_id");
			if (i_lstp_id.intValue() == dataUsulan.getTipeproduk()
					.intValue()) {
				s_channel = (String) dist2.get("lstp_produk");
			}
		}

		Double d_premiRider = 0.;
		if (dataUsulan.getDaftaRider().size() > 0) {
			for (int i = 0; i < dataUsulan.getDaftaRider().size(); i++) {
				Datarider rider = (Datarider) dataUsulan.getDaftaRider()
						.get(i);
				d_premiRider = rider.getMspr_premium();
			}
		}
		Double d_topUpBerkala = new Double(0);
		Double d_topUpTunggal = new Double(0);
		Double d_totalTopup = new Double(0);
		if (inv != null) {
			DetilTopUp daftarTopup = inv.getDaftartopup();
			d_topUpBerkala = Common.isEmpty(daftarTopup.getPremi_berkala()) ? new Double(
					0) : daftarTopup.getPremi_berkala();
			d_topUpTunggal = Common.isEmpty(daftarTopup.getPremi_tunggal()) ? new Double(
					0) : daftarTopup.getPremi_tunggal();
			d_totalTopup = d_topUpBerkala + d_topUpTunggal;
		}
		Double d_premiExtra = (Common.isEmpty(uwManager
				.selectSumPremiExtra(reg_spaj)) ? 0. : uwManager
				.selectSumPremiExtra(reg_spaj));
		Double d_totalPremi = dataUsulan.getMspr_premium() + d_totalTopup
				+ d_premiRider + d_premiExtra;

		stamp.setMoreInfo(moreInfo);

		// ---------- Data Halaman Pertama ----------
		over = stamp.getOverContent(1);
		over.beginText();
		over.setFontAndSize(times_new_roman, 8);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatString.nomorSPAJ(reg_spaj), 370, 706, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toString(sysdate), 80, 693, 0);

		over.setFontAndSize(times_new_roman, 6);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
				.getMcl_first().toUpperCase(), 200, 601, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
				.getMcl_first().toUpperCase(), 200, 588, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_depkeu(), 200, 575, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_tsi())), 200, 562, 0);

		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_premium())), 280, 551, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_totalTopup.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_totalTopup))), 280, 541, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_premiRider.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_premiRider))), 280, 531, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								d_totalPremi)), 280, 521, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("NM_PENUTUP").toString().toUpperCase(), 200, 492,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("KD_PENUTUP").toString(), 200, 479, 0);

		over.endText();

		// ---------- Data Halaman Kedua ----------
		over = stamp.getOverContent(2);
		over.beginText();

		// String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_" +
		// (dataPP.getMcl_first().toUpperCase()).replace(" ", "") + ".jpg";
		String ttdPp = exportDirectory + "\\" + idgadget + "_TTD_PP_"
				+ ".jpg";
		try {
			Image img = Image.getInstance(ttdPp);
			img.scaleAbsolute(40, 40);
			over.addImage(img, img.getScaledWidth(), 0, 0,
					img.getScaledHeight(), 458, 705);
			over.stroke();

			// String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
			// + (dataTT.getMcl_first().toUpperCase()).replace(" ", "") +
			// ".jpg";
			String ttdTu = exportDirectory + "\\" + idgadget + "_TTD_TU_"
					+ ".jpg";
			if (dataTT.getMste_age() < 17 || dataPP.getLsre_id() == 1)
				ttdTu = ttdPp;
			Image img2 = Image.getInstance(ttdTu);
			img2.scaleAbsolute(40, 40);
			over.addImage(img2, img2.getScaledWidth(), 0, 0,
					img2.getScaledHeight(), 458, 658);
			over.stroke();
		} catch (FileNotFoundException e) {
			logger.error("ERROR :", e);
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('TTD Pemegang Polis / Tertanggung Utama Tidak Ditemukan');window.close();</script>");
			sos.close();
		}

		over.setFontAndSize(times_new_roman, 8);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toString(sysdate), 280, 790, 0);
		over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataPP
				.getMcl_first().toUpperCase(), 300, 723, 0);
		over.showTextAligned(PdfContentByte.ALIGN_CENTER, dataTT
				.getMcl_first().toUpperCase(), 300, 676, 0);
		if (peserta.size() > 0) {
			Integer vertikal = 676;
			for (int i = 0; i < peserta.size(); i++) {
				vertikal = vertikal - 47;
				PesertaPlus pesertaPlus = (PesertaPlus) peserta.get(i);
				over.showTextAligned(PdfContentByte.ALIGN_CENTER,
						pesertaPlus.getNama().toUpperCase(), 300, vertikal,
						0);
				vertikal = vertikal + 2;
			}
		}

		try {
			// String ttdAgen = exportDirectory + "\\" + idgadget +
			// "_TTD_AGEN_" +
			// (agen.get("NM_PENUTUP").toString().toUpperCase()).replace(" ",
			// "") + ".jpg";
			String ttdAgen = exportDirectory + "\\" + idgadget
					+ "_TTD_AGEN_" + ".jpg";
			Image img3 = Image.getInstance(ttdAgen);
			img3.scaleAbsolute(40, 40);
			over.addImage(img3, img3.getScaledWidth(), 0, 0,
					img3.getScaledHeight(), 100, 420);
			over.stroke();
		} catch (FileNotFoundException e) {
			logger.error("ERROR :", e);
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('TTD Agen Penutup Tidak Ditemukan');window.close();</script>");
			sos.close();
		}

		try {
			// String ttdReff = exportDirectory + "\\" + idgadget +
			// "_TTD_REF_" +
			// (agen.get("NM_REFFERAL").toString().toUpperCase()).replace(" ",
			// "") + ".jpg";
			String ttdReff = exportDirectory + "\\" + idgadget
					+ "_TTD_REF_" + ".jpg";
			Image img4 = Image.getInstance(ttdReff);
			img4.scaleAbsolute(40, 40);
			over.addImage(img4, img4.getScaledWidth(), 0, 0,
					img4.getScaledHeight(), 280, 420);
			over.stroke();
		} catch (FileNotFoundException e) {
			logger.error("ERROR :", e);
			ServletOutputStream sos = response.getOutputStream();
			sos.println("<script>alert('TTD Agen Refferal Tidak Ditemukan');window.close();</script>");
			sos.close();
		}

		over.setFontAndSize(times_new_roman, 6);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("NM_PENUTUP").toString().toUpperCase(), 110, 415,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 61, 405, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("KD_PENUTUP").toString(), 81, 395, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("NM_REFFERAL").toString().toUpperCase(), 290, 415,
				0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				agen.get("KD_REFFERAL").toString(), 261, 405, 0);
		over.endText();

		// ---------- Data Halaman Ketiga ----------
		over = stamp.getOverContent(3);
		over.beginText();
		over.setFontAndSize(times_new_roman, 6);
		String flag_spaj = "";
		if (dataPP.getMspo_flag_spaj() == 0) {
			flag_spaj = "SPAJ OLD";
		} else if (dataPP.getMspo_flag_spaj() == 1) {
			flag_spaj = "SPAJ NEW VA";
		} else if (dataPP.getMspo_flag_spaj() == 2) {
			flag_spaj = "SPAJ NEW VA & PEMBAYAR PREMI";
		} else if (dataPP.getMspo_flag_spaj() == 3) {
			flag_spaj = "SPAJ FULL (MARET 2015)";
		} else if (dataPP.getMspo_flag_spaj() == 4) {
			flag_spaj = "SPAJ SIO (MARET 2015)";
		} else {
			flag_spaj = "SPAJ GIO (MARET 2015)";
		}
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, flag_spaj, 190,
				788, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				s_channel.toUpperCase(), 190, 778, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_depkeu(), 190, 768, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_name(), 190, 758, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
				.getMcl_first().toUpperCase(), 190, 748, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMcl_gelar()) ? "-" : dataPP
						.getMcl_gelar(), 190, 739, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getMspe_mother(), 190, 730, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getLsne_note(), 190, 720, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMcl_green_card()) ? "TIDAK"
						: dataPP.getMcl_green_card(), 190, 710, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getLside_name(), 190, 700, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getMspe_no_identity(), 190, 690, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMspe_no_identity_expired()) ? "-"
						: FormatDate.toString(dataPP
								.getMspe_no_identity_expired()), 190, 680,
				0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataPP.getMspe_place_birth() + ", "
						+ FormatDate.toString(dataPP.getMspe_date_birth()),
				190, 671, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getMspo_age() + " Tahun", 190, 661, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
				.getMspe_sex2().toUpperCase(), 190, 651, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP
				.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataPP
				.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataPP
				.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 190,
				642, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getLsag_name(), 190, 632, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getLsed_name(), 190, 622, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMcl_company_name()) ? "-" : dataPP
						.getMcl_company_name(), 246, 613, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataPP.getMkl_kerja(), 190, 603, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataPP.getKerjab(),
				198, 593, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMkl_kerja_ket()) ? "-" : dataPP
						.getMkl_kerja_ket(), 190, 583, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getAlamat_kantor()) ? "-" : dataPP
						.getAlamat_kantor(), 250, 573, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKota_kantor()) ? "-" : dataPP
						.getKota_kantor(), 153, 563, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKd_pos_kantor()) ? "-" : dataPP
						.getKd_pos_kantor(), 153, 553, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_kantor()) ? "-" : dataPP
						.getTelpon_kantor(), 153, 544, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_kantor2()) ? "-" : dataPP
						.getTelpon_kantor2(), 153, 535, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
				.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
				153, 525, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getAlamat_rumah()) ? "-" : dataPP
						.getAlamat_rumah(), 153, 515, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKota_rumah()) ? "-" : dataPP
						.getKota_rumah(), 153, 505, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKd_pos_rumah()) ? "-" : dataPP
						.getKd_pos_rumah(), 153, 495, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
						.getTelpon_rumah(), 153, 486, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_rumah2()) ? "-" : dataPP
						.getTelpon_rumah2(), 153, 476, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getAlamat_tpt_tinggal()) ? "-"
						: dataPP.getAlamat_tpt_tinggal(), 177, 466, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKota_tpt_tinggal()) ? "-" : dataPP
						.getKota_tpt_tinggal(), 153, 456, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getKd_pos_tpt_tinggal()) ? "-"
						: dataPP.getKd_pos_tpt_tinggal(), 153, 446, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_rumah()) ? "-" : dataPP
						.getTelpon_rumah(), 153, 437, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getTelpon_rumah2()) ? "-" : dataPP
						.getTelpon_rumah2(), 153, 427, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
				.isEmpty(dataPP.getNo_fax()) ? "-" : dataPP.getNo_fax(),
				153, 417, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
						.getMsap_address(), 208, 407, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getNo_hp()) ? "-" : dataPP.getNo_hp(),
				153, 397, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getEmail()) ? "-" : dataPP.getEmail(),
				153, 387, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMcl_npwp()) ? "-" : dataPP
						.getMcl_npwp(), 153, 378, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMkl_penghasilan()) ? "-" : dataPP
						.getMkl_penghasilan(), 226, 368, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMkl_pendanaan()) ? "-" : dataPP
						.getMkl_pendanaan(), 153, 359, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataPP.getMkl_tujuan()) ? "-" : dataPP
						.getMkl_tujuan(), 190, 349, 0);

		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
				.getMcl_first().toUpperCase(), 190, 319, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMcl_gelar()) ? "-" : dataTT
						.getMcl_gelar(), 190, 309, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getMspe_mother(), 190, 300, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getLsne_note(), 190, 290, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMcl_green_card()) ? "TIDAK"
						: dataTT.getMcl_green_card(), 190, 281, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getLside_name(), 190, 271, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getMspe_no_identity(), 190, 261, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMspe_no_identity_expired()) ? "-"
						: FormatDate.toString(dataTT
								.getMspe_no_identity_expired()), 190, 251,
				0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataTT.getMspe_place_birth() + ", "
						+ FormatDate.toString(dataTT.getMspe_date_birth()),
				190, 241, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getMste_age() + " Tahun", 190, 231, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
				.getMspe_sex2().toUpperCase(), 190, 221, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT
				.getMspe_sts_mrt().equals("1") ? "BELUM MENIKAH" : (dataTT
				.getMspe_sts_mrt().equals("2") ? "MENIKAH" : (dataTT
				.getMspe_sts_mrt().equals("3") ? "JANDA" : "DUDA")), 190,
				212, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getLsag_name(), 190, 202, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getLsed_name(), 190, 192, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMcl_company_name()) ? "-" : dataTT
						.getMcl_company_name(), 246, 183, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataTT.getMkl_kerja(), 190, 173, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, dataTT.getKerjab(),
				198, 163, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMkl_kerja_ket()) ? "-" : dataTT
						.getMkl_kerja_ket(), 190, 154, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getAlamat_kantor()) ? "-" : dataTT
						.getAlamat_kantor(), 250, 144, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKota_kantor()) ? "-" : dataTT
						.getKota_kantor(), 153, 134, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKd_pos_kantor()) ? "-" : dataTT
						.getKd_pos_kantor(), 153, 124, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_kantor()) ? "-" : dataTT
						.getTelpon_kantor(), 153, 115, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_kantor2()) ? "-" : dataTT
						.getTelpon_kantor2(), 153, 105, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
				.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
				153, 95, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getAlamat_rumah()) ? "-" : dataTT
						.getAlamat_rumah(), 153, 85, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKota_rumah()) ? "-" : dataTT
						.getKota_rumah(), 153, 75, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKd_pos_rumah()) ? "-" : dataTT
						.getKd_pos_rumah(), 153, 65, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
						.getTelpon_rumah(), 153, 56, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
						.getTelpon_rumah2(), 153, 46, 0);
		over.endText();

		// ---------- Data Halaman Keempat ----------
		over = stamp.getOverContent(4);
		over.beginText();
		over.setFontAndSize(times_new_roman, 6);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getAlamat_tpt_tinggal()) ? "-"
						: dataTT.getAlamat_tpt_tinggal(), 177, 798, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKota_tpt_tinggal()) ? "-" : dataTT
						.getKota_tpt_tinggal(), 153, 788, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getKd_pos_tpt_tinggal()) ? "-"
						: dataTT.getKd_pos_tpt_tinggal(), 153, 779, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_rumah()) ? "-" : dataTT
						.getTelpon_rumah(), 153, 769, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getTelpon_rumah2()) ? "-" : dataTT
						.getTelpon_rumah2(), 153, 759, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
				.isEmpty(dataTT.getNo_fax()) ? "-" : dataTT.getNo_fax(),
				153, 749, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(addrBill.getMsap_address()) ? "-" : addrBill
						.getMsap_address(), 208, 739, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getNo_hp()) ? "-" : dataTT.getNo_hp(),
				153, 729, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getEmail()) ? "-" : dataTT.getEmail(),
				153, 719, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMcl_npwp()) ? "-" : dataTT
						.getMcl_npwp(), 153, 710, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMkl_penghasilan()) ? "-" : dataTT
						.getMkl_penghasilan(), 226, 700, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMkl_pendanaan()) ? "-" : dataTT
						.getMkl_pendanaan(), 153, 690, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(dataTT.getMkl_tujuan()) ? "-" : dataTT
						.getMkl_tujuan(), 190, 681, 0);

		// Data Pembayar Premi
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, Common
				.isEmpty(pembPremi.getNama_pihak_ketiga()) ? "-"
				: pembPremi.getNama_pihak_ketiga(), 190, 652, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getKewarganegaraan()) ? "-"
						: pembPremi.getKewarganegaraan(), 190, 642, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getAlamat_lengkap()) ? "-"
						: pembPremi.getAlamat_lengkap(), 190, 632, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTelp_rumah()) ? "-" : pembPremi
						.getTelp_rumah(), 190, 622, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTelp_kantor()) ? "-"
						: pembPremi.getTelp_kantor(), 190, 613, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getEmail()) ? "-" : pembPremi
						.getEmail(), 190, 603, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTempat_lahir()) ? "-"
						: (pembPremi.getTempat_lahir() + ", " + FormatDate.toString(pembPremi
								.getMspe_date_birth_3rd_pendirian())), 190,
				593, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getMkl_kerja()) ? "-" : pembPremi
						.getMkl_kerja(), 190, 584, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 574, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 564, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 554, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getNo_npwp()) ? "-" : pembPremi
						.getNo_npwp(), 190, 544, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getLsre_relation()) ? "-"
						: pembPremi.getLsre_relation(), 215, 535, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getSumber_dana()) ? "-"
						: pembPremi.getSumber_dana(), 190, 525, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				Common.isEmpty(pembPremi.getTujuan_dana_3rd()) ? "-"
						: pembPremi.getTujuan_dana_3rd(), 190, 515, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 505, 0);

		// Data Usulan
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				s_channel.toUpperCase(), 190, 476, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_name(), 190, 466, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLsdbs_name(), 190, 456, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getMspr_ins_period() + " Tahun", 190, 446, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLscb_pay_mode(), 190, 437, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_premium())), 190, 427, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_tsi())), 190, 417, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
						.getMste_flag_cc() == 2 ? "TABUNGAN"
						: "KARTU KREDIT"), 190, 407, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toIndonesian(dataUsulan.getMste_beg_date()),
				190, 397, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toIndonesian(dataUsulan.getMste_end_date()),
				190, 387, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 378, 0);

		// Data Investasi
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								dataUsulan.getMspr_premium())), 190, 349, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_topUpBerkala.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_topUpBerkala))), 190, 339, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_topUpTunggal.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_topUpTunggal))), 190, 329, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				d_premiExtra.doubleValue() == new Double(0) ? "-"
						: (dataUsulan.getLku_symbol() + " " + FormatNumber
								.convertToTwoDigit(new BigDecimal(
										d_premiExtra))), 190, 319, 0);
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								d_totalPremi)), 190, 309, 0);

		// Data Rekening
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getMste_flag_cc() == 0 ? "TUNAI" : (dataUsulan
						.getMste_flag_cc() == 2 ? "TABUNGAN"
						: "KARTU KREDIT"), 190, 280, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getLsbp_nama(), 190, 270, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getMrc_cabang(), 190, 260, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 252, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 242, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getMrc_no_ac(), 190, 232, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				rekClient.getMrc_nama(), 190, 222, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, rekClient
				.getKuasa().toUpperCase(), 190, 212, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT,
				FormatDate.toIndonesian(rekClient.getTgl_surat()), 190,
				202, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, "-", 190, 193, 0);

		Double d_jmlinves = new Double(0);
		String s_jnsinves = "";
		for (int i = 0; i < detInv.size(); i++) {
			DetilInvestasi detInves = (DetilInvestasi) detInv.get(i);
			d_jmlinves = d_jmlinves + detInves.getMdu_jumlah1();
			s_jnsinves = s_jnsinves
					+ detInves.getLji_invest1().toUpperCase() + " "
					+ detInves.getMdu_persen1() + "%";
			if (i != (detInv.size() - 1))
				s_jnsinves = s_jnsinves + ", ";
		}
		over.showTextAligned(
				PdfContentByte.ALIGN_LEFT,
				dataUsulan.getLku_symbol()
						+ " "
						+ FormatNumber.convertToTwoDigit(new BigDecimal(
								d_jmlinves)), 208, 183, 0);
		over.showTextAligned(PdfContentByte.ALIGN_LEFT, s_jnsinves, 190,
				173, 0);

		if (benef.size() > 0) {
			Integer j = 0;
			for (int i = 0; i < benef.size(); i++) {
				Benefeciary benefit = (Benefeciary) benef.get(i);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						benefit.getMsaw_first(), 60, 123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						benefit.getSmsaw_birth(), 208, 123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						benefit.getMsaw_sex() == 1 ? "PRIA" : "WANITA",
						255, 123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT,
						dataPP.getLsne_note(), 310, 123 - j, 0);
				over.showTextAligned(PdfContentByte.ALIGN_LEFT, benefit
						.getLsre_relation().toUpperCase(), 400, 123 - j, 0);
				j += 10;
			}
		}
		over.endText();

		stamp.close();

		File l_file = new File(outputName);
		FileInputStream in = null;
		ServletOutputStream ouputStream = null;
		try {

			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "Inline");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");

			in = new FileInputStream(l_file);
			ouputStream = response.getOutputStream();

			IOUtils.copy(in, ouputStream);
		} catch (Exception e) {
			logger.error("ERROR :", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (ouputStream != null) {
					ouputStream.flush();
					ouputStream.close();
				}
			} catch (Exception e) {
				logger.error("ERROR :", e);
			}
		}
		return null;
	}
	
}
