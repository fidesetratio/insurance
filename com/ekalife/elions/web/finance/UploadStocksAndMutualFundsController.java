/**
 * 
 */
package com.ekalife.elions.web.finance;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Reksadana;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatNumber;
import com.ekalife.utils.parent.ParentController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Upload Data Saham, Pooled Funds, dan menghitung Yield Curve nya
 * - bisa Upload Bonds (Jan 8, 2009)
 * - bisa upload trans reksadana (feb 12, 2009)
 * 
 * @author Yusuf
 * @since Sep 9, 2008 (10:25:31 AM)
 */
public class UploadStocksAndMutualFundsController extends ParentController{
	protected final Log logger = LogFactory.getLog( getClass() );
	
	private Properties listPershReksadana;
	public Properties getListPershReksadana() {return listPershReksadana;}
	public void setListPershReksadana(Properties listPershReksadana) {this.listPershReksadana = listPershReksadana;}


	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap<String, Object>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		Upload upload = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);		
		HttpSession session = request.getSession();
		
		cmd.put("tanggal", ServletRequestUtils.getStringParameter(request, "tanggal", defaultDateFormat.format(elionsManager.selectSysdate())));
		cmd.put("tanggalAkhir", ServletRequestUtils.getStringParameter(request, "tanggalAkhir", defaultDateFormat.format(elionsManager.selectSysdate())));
		
		//Upload lainnya
		/*cmd.put("tanggal2", ServletRequestUtils.getStringParameter(request, "tanggal2", defaultDateFormat.format(elionsManager.selectSysdate())));
		cmd.put("tanggalAkhir2", ServletRequestUtils.getStringParameter(request, "tanggalAkhir2", defaultDateFormat.format(elionsManager.selectSysdate())));
		
		cmd.put("tanggal3", ServletRequestUtils.getStringParameter(request, "tanggal3", defaultDateFormat.format(elionsManager.selectSysdate())));
		cmd.put("tanggalAkhir3", ServletRequestUtils.getStringParameter(request, "tanggalAkhir3", defaultDateFormat.format(elionsManager.selectSysdate())));
		
		cmd.put("tanggal4", ServletRequestUtils.getStringParameter(request, "tanggal4", defaultDateFormat.format(elionsManager.selectSysdate())));
		cmd.put("tanggalAkhir4", ServletRequestUtils.getStringParameter(request, "tanggalAkhir4", defaultDateFormat.format(elionsManager.selectSysdate())));
		
		cmd.put("tanggal5", ServletRequestUtils.getStringParameter(request, "tanggal5", defaultDateFormat.format(elionsManager.selectSysdate())));
		cmd.put("tanggalAkhir5", ServletRequestUtils.getStringParameter(request, "tanggalAkhir5", defaultDateFormat.format(elionsManager.selectSysdate())));*/
		
		/* Upload SAHAM */
		if(request.getParameter("preview_stocks") != null) {
			HSSFWorkbook wb = new HSSFWorkbook(upload.getFile1().getInputStream());
			HSSFSheet sheet = wb.getSheetAt(0);
			
			boolean stop = false;
			int baris = 1; //dimulai dari 1 bukan dari 0, karena di baris 0 ada headernya
			List<String> errorMessages = new ArrayList<String>();
			List<Map> result = new ArrayList<Map>();
			
			//LOOPING ROW
			do {
				HSSFRow row = sheet.getRow(baris);
				baris++;
				if(row != null) {
					HSSFCell symbol = row.getCell((short) 0); //kolom A
					HSSFCell value = row.getCell((short) 2); //kolom C
					logger.info(symbol.getCellType());
					if(symbol.getCellType() != HSSFCell.CELL_TYPE_STRING || value.getCellType() != HSSFCell.CELL_TYPE_NUMERIC) {
						errorMessages.add("Harap Cek Baris ke " + (baris-1));
					}else {
						Map saham = new HashMap();
						saham.put("SYMBOL", symbol.getStringCellValue().substring(0,4));
						saham.put("VALUE", value.getNumericCellValue());
						result.add(saham);
					}
				}else {
					stop = true;
				}
			}while(!stop);
			
			if(!errorMessages.isEmpty()) { 
				cmd.put("errorMessages", errorMessages);
			}else { 
				session.setAttribute("resultStocks", result);
				cmd.put("resultStocks", result);
			}
			
		/* Save SAHAM */
		}else if(request.getParameter("save_stocks") != null) {
			List<Map> result = (List<Map>) session.getAttribute("resultStocks");
			cmd.put("hasilProses", elionsManager.saveUploadSaham(
					ServletRequestUtils.getRequiredStringParameter(request, "tanggal"), 
					ServletRequestUtils.getRequiredStringParameter(request, "tanggalAkhir"), 
					result, currentUser));
			
		/* Delete SAHAM */
		}else if(request.getParameter("delete_stocks") != null) {
			cmd.put("hasilProses", elionsManager.hapusSaham(
					ServletRequestUtils.getRequiredStringParameter(request, "tanggal"), 
					ServletRequestUtils.getRequiredStringParameter(request, "tanggalAkhir"), currentUser));

		/* Upload POOLED FUNDS */
		}else if(request.getParameter("preview_pooled_funds") != null) {
			HSSFWorkbook wb = new HSSFWorkbook(upload.getFile1().getInputStream());
			HSSFSheet sheet = wb.getSheetAt(0);
			
			boolean stop = false;
			int baris = 7; //dimulai dari baris 8
			List<String> errorMessages = new ArrayList<String>();
			List<Map> result = new ArrayList<Map>();
			
			//LOOPING ROW
			do {
				HSSFRow row = sheet.getRow(baris);
				baris++;
				logger.info(baris);
				if(row != null && !row.getCell((short) 1).getStringCellValue().equals("")) {
					HSSFCell fund = row.getCell((short) 1); //kolom B
					HSSFCell nab = row.getCell((short) 3); //kolom D
					
					//CELL_TYPE_NUMERIC = 0;
					//CELL_TYPE_STRING = 1;
					//CELL_TYPE_FORMULA = 2;
					//CELL_TYPE_BLANK = 3;
					//CELL_TYPE_BOOLEAN = 4;
					//CELL_TYPE_ERROR = 5;
					
					//logger.info("Baris " + (baris-1) + ": ");
					//logger.info(fund.getCellType() + ", " + nab.getCellType());
					
					if((fund.getCellType() != HSSFCell.CELL_TYPE_STRING && fund.getCellType() != HSSFCell.CELL_TYPE_BLANK) || 
							(nab.getCellType() != HSSFCell.CELL_TYPE_NUMERIC && fund.getCellType() != HSSFCell.CELL_TYPE_BLANK)) {
						//errorMessages.add("Harap Cek Baris ke " + (baris) + " (" + fund.getStringCellValue() + ")");
						continue;
					}else {
						if(!fund.getStringCellValue().trim().equals("")) {
							Map reksadana = new HashMap();
							reksadana.put("FUND", fund.getStringCellValue());
							reksadana.put("NAB", nab.getNumericCellValue());
							reksadana.put("TYPE", uwManager.selectReksadanaByName(reksadana.get("FUND").toString()));
							result.add(reksadana);
						}
					}
				}else {
					stop = true;
				}
			}while(!stop);
			
			if(!errorMessages.isEmpty()) { 
				cmd.put("errorMessages", errorMessages);
			}else {
				session.setAttribute("resultPooledFunds", result);
				cmd.put("resultPooledFunds", result);
			}

		/* Save POOLED FUNDS */
		}else if(request.getParameter("save_pooled_funds") != null) {
			List<Map> result = (List<Map>) session.getAttribute("resultPooledFunds");
			cmd.put("hasilProses", elionsManager.saveUploadPooledFunds(
					ServletRequestUtils.getRequiredStringParameter(request, "tanggal"), 
					ServletRequestUtils.getRequiredStringParameter(request, "tanggalAkhir"), 
					result, currentUser,request));
			
		/* Delete POOLED FUNDS */
		}else if(request.getParameter("delete_pooled_funds") != null) {
			cmd.put("hasilProses", elionsManager.hapusPooledFunds(
					ServletRequestUtils.getRequiredStringParameter(request, "tanggal"), 
					ServletRequestUtils.getRequiredStringParameter(request, "tanggalAkhir"), currentUser));
			
		/* Upload BONDS */
		}else if(request.getParameter("preview_bonds") != null) {
			int jml_upload = 0;
			for(int i=1;i<=5;i++){
				if(i==1){
					if(!upload.getFile1().getOriginalFilename().equals("")){
						jml_upload += 1;
					}
				}else if(i==2){
					if(!upload.getFile2().getOriginalFilename().equals("")){
						jml_upload += 1;
					}
				}else if(i==3){
					if(!upload.getFile3().getOriginalFilename().equals("")){
						jml_upload += 1;
					}
				}else if(i==4){
					if(!upload.getFile4().getOriginalFilename().equals("")){
						jml_upload += 1;
					}
				}else{
					if(!upload.getFile5().getOriginalFilename().equals("")){
						jml_upload += 1;
					}
				}
			}
			
			session.setAttribute("jml_upload", jml_upload);
			
			for(int i=1;i<=jml_upload;i++){
				//BEGIN
				HSSFWorkbook wb = null;
				HSSFSheet sheet = null;
				if(i==1){
					wb = new HSSFWorkbook(upload.getFile1().getInputStream());
					sheet = wb.getSheetAt(0);
				}else if(i==2){
					wb = new HSSFWorkbook(upload.getFile2().getInputStream());
					sheet = wb.getSheetAt(0);
				}else if(i==3){
					wb = new HSSFWorkbook(upload.getFile3().getInputStream());
					sheet = wb.getSheetAt(0);
				}else if(i==4){
					wb = new HSSFWorkbook(upload.getFile4().getInputStream());
					sheet = wb.getSheetAt(0);
				}else{
					wb = new HSSFWorkbook(upload.getFile5().getInputStream());
					sheet = wb.getSheetAt(0);
				}
				//HSSFWorkbook wb = new HSSFWorkbook(upload.getFile1().getInputStream());
				//HSSFSheet sheet = wb.getSheetAt(0);
				
				boolean stop = false;
				int baris = 1; //dimulai dari baris 2
				List<String> errorMessages = new ArrayList<String>();
				List<Map> result = new ArrayList<Map>();
				
				//LOOPING ROW
				do {
					HSSFRow row = sheet.getRow(baris);
					baris++;
					if(row != null) {
						HSSFCell kode 	= row.getCell((short) 0); //kolom C
						HSSFCell seri 	= row.getCell((short) 1); //kolom D
						HSSFCell price	= row.getCell((short) 5); //kolom D
						
						if(kode != null && price != null) {
							
							if((kode.getCellType() != HSSFCell.CELL_TYPE_STRING 		&& kode.getCellType() != HSSFCell.CELL_TYPE_BLANK) || 
									(seri.getCellType() != HSSFCell.CELL_TYPE_STRING 	&& seri.getCellType() != HSSFCell.CELL_TYPE_BLANK) ||
									(price.getCellType() != HSSFCell.CELL_TYPE_NUMERIC 	&& price.getCellType() != HSSFCell.CELL_TYPE_BLANK && price.getCellType() != HSSFCell.CELL_TYPE_FORMULA)) {
								errorMessages.add("Harap Cek Baris ke " + (baris) + " (" + kode.getStringCellValue() + ")");
							}else {
								if(!kode.getStringCellValue().trim().equals("")) {
									Map bonds = new HashMap();
									bonds.put("KODE", kode.getStringCellValue());
									bonds.put("SERI", seri.getStringCellValue());
									bonds.put("PRICE", price.getNumericCellValue());
									result.add(bonds);
								}
							}
						}
						
					}else {
						stop = true;
					}
				}while(!stop);
				
				String resultBonds = "resultBonds"+i;
				logger.info(resultBonds);
				
				if(!errorMessages.isEmpty()) { 
					cmd.put("errorMessages", errorMessages);
				}else {
					session.setAttribute(resultBonds, result);
					cmd.put(resultBonds, result);
				}
				//END
			}
		/* Save BONDS */
		}else if(request.getParameter("save_bonds") != null) {
			DateFormat df= new SimpleDateFormat("dd/MM/yyyy");
			int jml = (Integer) session.getAttribute("jml_upload");
			
			//JIKA TANGGAL BERURUT
			int jml2 = jml-1;
			Date tglAwal = df.parse(ServletRequestUtils.getRequiredStringParameter(request, "tanggal"));
			Date tglAkhir = df.parse(ServletRequestUtils.getRequiredStringParameter(request, "tanggalAkhir"));
			
			long tgl1 = tglAwal.getTime();
			long tgl2 = tglAkhir.getTime();			
			long diff = tgl2-tgl1;
			long diffday = diff/(24 * 60 * 60 * 1000);
			
			if(jml2==diffday){
				int i = 1;
				do{
					//PROSES
					String hasilProses = null;
					String resultBonds = "resultBonds"+i;
					String tanggal = df.format(tglAwal);
					
					if(i==1){
						hasilProses = "hasilProses";
					}else{
						hasilProses = "hasilProses"+i;
					}
					List<Map> result = (List<Map>) session.getAttribute(resultBonds);
					cmd.put(hasilProses, elionsManager.saveUploadBonds(
							tanggal, result, currentUser));
					
					tglAwal = FormatDate.add(tglAwal, Calendar.DATE, 1);
					i++;
				}while(tglAwal.compareTo(tglAkhir) <= 0);
			}else{
				cmd.put("hasilProses", "Save Gagal - Jumlah data upload dan range tanggal tidak sama. Ulangi lagi.");
			}
			
			
			//JIKA TANGGAL TIDAK BERURUT
			/*for(int i=1;i<=jml;i++){
				String hasilProses = null;
				String resultBonds = "resultBonds"+i;
				String tanggal = null;
				
				if(i==1){
					tanggal = "tanggal";
					hasilProses = "hasilProses";
				}else{
					tanggal = "tanggal"+i;
					hasilProses = "hasilProses"+i;
				}
				List<Map> result = (List<Map>) session.getAttribute(resultBonds);
				cmd.put(hasilProses, elionsManager.saveUploadBonds(
						ServletRequestUtils.getRequiredStringParameter(request, tanggal), result, currentUser));
			}*/
			
		/* Delete BONDS */
		}else if(request.getParameter("delete_bonds") != null) {
			cmd.put("hasilProses", elionsManager.hapusBonds(
					ServletRequestUtils.getRequiredStringParameter(request, "tanggal"), currentUser));
			
		/* Upload TRANSAKSI REKSADANA */
		}else if(request.getParameter("preview_trans_reksadana") != null) {
			List daftar = new ArrayList();
			Map data = null;
			StringTokenizer daftarNama = new StringTokenizer(listPershReksadana.getProperty("sheetReksadana"),"|");
			while(daftarNama.hasMoreTokens()) {
				String content = daftarNama.nextToken();
				data = new HashMap<String,String>();
				data.put("kode", content.substring(0,content.indexOf(",")));
				data.put("nama", content.substring(content.indexOf(",")+1));
				daftar.add(data); 
			}

			HSSFWorkbook wb = new HSSFWorkbook(upload.getFile1().getInputStream());
			Workbook workbook = Workbook.getWorkbook(upload.getFile1().getInputStream());
			List<Reksadana> result = new ArrayList<Reksadana>();
			List<String> errorMessages = new ArrayList<String>();
			SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
			
			//logger.info("Start : " + new Date());
			int count = 0;
			for(int a=0;a<wb.getNumberOfSheets();a++) {
				//logger.info(wb.getSheetName(a));
				for(int b=0;b<daftar.size();b++) {
					data = (HashMap) daftar.get(b); 
					if(data.get("nama").toString().trim().equalsIgnoreCase(wb.getSheetName(a).toString().trim())) {
						//logger.info(data.get("nama") + " pada sheet no " + (a+1) + " diurutan list ke " + (b+1));
						
						Sheet sheet = workbook.getSheet(a);
						Cell cell = null;
						int baris = 6; //dimulai dari baris 7
						
						do {
							try {
								cell  = sheet.getCell(0, baris);
							}catch(ArrayIndexOutOfBoundsException e) {
								break;
							}
							if(cell.getType() == CellType.EMPTY) {
								break;
							} else {
								
								HSSFSheet sheet2 = wb.getSheetAt(a);
								HSSFRow row = sheet2.getRow(baris);
								//HSSFCell kode 		= row.getCell((short) 0); //kolom A
								//HSSFCell nama		= row.getCell((short) 1); //kolom B
								//HSSFCell tanggal 	= row.getCell((short) 2); //kolom C
								//HSSFCell transaksi	= row.getCell((short) 2); //kolom D
								HSSFCell total_cost	= row.getCell((short) 2); //kolom E
								HSSFCell nab		= row.getCell((short) 3); //kolom F
								HSSFCell total_unit	= row.getCell((short) 4); //kolom G
								HSSFCell redemp		= row.getCell((short) 5); //kolom H
								HSSFCell gain_lost = row.getCell((short) 6);
								HSSFCell avg_cost = row.getCell((short) 7);
								
								String row0 = sheet.getCell(0, baris).getContents();
								String part1 = sheet.getCell(8, baris).getContents();
								String part2 = sheet.getCell(9, baris).getContents();
								String amount = sheet.getCell(5, baris).getContents().equals("") ? "0" : sheet.getCell(5, baris).getContents().replace(",", "");
								String nav = sheet.getCell(3, baris).getContents().equals("") ? "0" : sheet.getCell(3, baris).getContents().replace(",", "");
								String unit = "";
								if(sheet.getCell(4, baris).getContents().equals("")) unit = "0";
								else {
									 if(sheet.getCell(4, baris).getContents().contains("(")) {
										 unit = sheet.getCell(4, baris).getContents().replace("(", "");
										 unit = unit.replace(")", ""); 
										 unit = "-"+unit.replace(",", "");
									 }
									 else unit = sheet.getCell(4, baris).getContents().replace(",", "");
								}
								String avgCost = sheet.getCell(7, baris).getContents().equals("") ? "0" : sheet.getCell(7, baris).getContents().replace(",", "");
								String ttlCost = "";
								if(sheet.getCell(2, baris).getContents().equals("")) ttlCost = "0";
								else {
									 if(sheet.getCell(2, baris).getContents().contains("(")) {
										 ttlCost = sheet.getCell(2, baris).getContents().replace("(", "");
										 ttlCost = ttlCost.replace(")", ""); 
										 ttlCost = "-"+ttlCost.replace(",", "");
									 }
									 else ttlCost = sheet.getCell(2, baris).getContents().replace(",", "");
								}								
								String gainLoss = "";
								if(sheet.getCell(6, baris).getContents().equals("")) gainLoss = "0";
								else {
									 if(sheet.getCell(6, baris).getContents().contains("(")) {
										 gainLoss = sheet.getCell(6, baris).getContents().replace("(", "");
										 gainLoss = gainLoss.replace(")", ""); 
										 gainLoss = "-"+gainLoss.replace(",", "");
									 }
									 else gainLoss = sheet.getCell(6, baris).getContents().replace(",", "");
								}
								
								if(row0.equals(cmd.get("tanggal").toString())) {
									Reksadana r = new Reksadana();
									r.setIre_reksa_no(data.get("kode").toString());
									r.setIre_reksa_name(data.get("nama").toString());
									r.setLku_id(elionsManager.selectKursReksadana(r.getIre_reksa_no()));
									
									if(r.getLku_id() == null){ //bila kurs null, berarti no reksa tsb gak ada
										errorMessages.add("Kode Reksadana untuk baris ke " + (baris) + " (" + r.getIre_reksa_no() + ") tidak ditemukan di sistem!");
									}else{
										r.setLus_id(Integer.valueOf(currentUser.getLus_id()));
										
										if(sheet.getCell(1, baris).getContents().trim().toUpperCase().equals("BUYING")) {
											r.setIrt_rtrans_jn(1); //buying
											//r.setIrt_total_cost(Double.parseDouble(ttlCost));
										}	
										else if(sheet.getCell(1, baris).getContents().trim().toUpperCase().equals("SELLING")) {
											r.setIrt_rtrans_jn(0); //selling
											//r.setIrt_total_cost(Double.parseDouble(amount));
										}
											
										r.setIrt_trans_date(formatDate.parse(sheet.getCell(0, baris).getContents()));
										r.setIrt_effective_date(formatDate.parse(sheet.getCell(0, baris).getContents()));
										r.setIrt_cost((double) 0); //other cost
										r.setIrt_amount(redemp.getNumericCellValue());
										r.setIrt_nav(nab.getNumericCellValue());
										r.setIrt_subscribe_unit(total_unit.getNumericCellValue()<1?total_unit.getNumericCellValue()*-1:total_unit.getNumericCellValue());
										r.setIrt_subs_redem_fee((double) 0);
										r.setIrt_note(part1 + " " + part2);
										
										//perhitungan average cost
										Map getData = elionsManager.selectDataAverageCostReksadana(r.getIre_reksa_no(), r.getIrt_trans_date());
										double ldec_acost;
										//sell
										double ldec_subs = ((BigDecimal) getData.get("SUBS")).doubleValue(); 
										double ldec_cost = ((BigDecimal) getData.get("COST")).doubleValue();
										//buy
										double ldec_tsubs = ((BigDecimal) getData.get("TSUBS")).doubleValue(); 
										double ldec_tcost = ((BigDecimal) getData.get("TCOST")).doubleValue(); 
										
										ldec_tsubs -= ldec_subs;
										ldec_tcost -= ldec_cost;

										if(ldec_tsubs == 0){
											ldec_acost = 0;
										}else{
											ldec_acost = ldec_tcost / ldec_tsubs;
										}

										if(r.getLku_id().equals("01")){
											ldec_acost = FormatNumber.round(ldec_acost, 4);
										}else{
											ldec_acost = FormatNumber.round(ldec_acost, 10);
										}

										if(r.getIrt_rtrans_jn().intValue() == 1){
											Map dataBuying = elionsManager.selectDataAverageCostBuyingReksadana(r.getIre_reksa_no(), r.getIrt_trans_date());
											int jn = ((BigDecimal) dataBuying.get("IRT_AVERAGE_COST")).intValue();
											ldec_acost = ((BigDecimal) dataBuying.get("IRT_AVERAGE_COST")).doubleValue();
											if(jn == 1) r.setIrt_average_cost(ldec_acost);
										}
										
										r.setIrt_average_cost(ldec_acost);

										//perhitungan total cost
										if(r.getIrt_rtrans_jn().intValue() == 1){
											//r.setIrt_total_cost(r.getIrt_amount());
											r.setIrt_total_cost(total_cost.getNumericCellValue());
											r.setIrt_amount(r.getIrt_total_cost());
											//r.setIrt_total_cost(total_cost.getNumericCellValue());
										}else if(r.getIrt_rtrans_jn().intValue() == 0){
											r.setIrt_total_cost(r.getIrt_average_cost() * r.getIrt_subscribe_unit());
										}
										
										// perhitungan gain/lost
										if(r.getIrt_rtrans_jn().intValue() == 1){
											r.setIrt_gain_loss(new Double(0));
										}else if(r.getIrt_rtrans_jn().intValue() == 0){
											r.setIrt_gain_loss(r.getIrt_total_cost() + r.getIrt_amount());
										}										
										result.add(r);
									}
									
									/*logger.info("kode : " + data.get("kode"));
									logger.info("nama : " + data.get("nama"));
									logger.info("tgl : " + sheet.getCell(0, baris).getContents());
									logger.info("transaksi : " + sheet.getCell(1, baris).getContents());
									logger.info("total cost : " + sheet.getCell(2, baris).getContents());
									logger.info("total cost v2 : " + ttlCost);
									logger.info("total cost-2 : " + total_cost.getNumericCellValue());
									logger.info("nab : " + sheet.getCell(3, baris).getContents());
									logger.info("nab v2 : " + nav);
									logger.info("nab-2 : " + nab.getNumericCellValue());
									logger.info("total unit : " + sheet.getCell(4, baris).getContents());
									logger.info("total unit v2 : " + unit);
									logger.info("total unit-2 : " + total_unit.getNumericCellValue());
									logger.info("redemp : " + sheet.getCell(5, baris).getContents());
									logger.info("redemp v2 : " + amount);
									logger.info("redemp-2 : " + redemp.getNumericCellValue());
									logger.info("gain / lost : " + sheet.getCell(6, baris).getContents());
									logger.info("gain / lost v2 : " + gainLoss);
									logger.info("gain / lost-2 : " + gain_lost.getNumericCellValue());
									logger.info("avg cost : " + sheet.getCell(7, baris).getContents());
									logger.info("avg cost v2 : " + avgCost);
									logger.info("avg cost-2 : " + avg_cost.getNumericCellValue());
									logger.info("ket : " + part1 + " " + part2);*/
								}
							}
							baris++;
						}while(true);
						count++;
						break;
					}	
				}
				if(count == daftar.size()) break;
			}
			workbook.close();
			//logger.info("End : " + new Date());
			
			//Integer sheet = wb.getNumberOfSheets();
			//logger.info("sheet total : " + sheet);
			/*HSSFSheet sheet = wb.getSheetAt(0);
			boolean stop = false;
			int baris = 3; //dimulai dari baris 4
			List<String> errorMessages = new ArrayList<String>();
			List<Reksadana> result = new ArrayList<Reksadana>();
			
			//LOOPING ROW
			do {
				HSSFRow row = sheet.getRow(baris);
				baris++;
				if(row != null) {
					HSSFCell kode 		= row.getCell((short) 0); //kolom A
					HSSFCell nama		= row.getCell((short) 1); //kolom B
					HSSFCell tanggal 	= row.getCell((short) 2); //kolom C
					HSSFCell transaksi	= row.getCell((short) 3); //kolom D
					HSSFCell total_cost	= row.getCell((short) 4); //kolom E
					HSSFCell nab		= row.getCell((short) 5); //kolom F
					HSSFCell total_unit	= row.getCell((short) 6); //kolom G
					HSSFCell redemp		= row.getCell((short) 7); //kolom H
					//HSSFCell gain_loss	= row.getCell((short) 8); //kolom I
					//HSSFCell avg_cost	= row.getCell((short) 9); //kolom J
					HSSFCell keterangan	= row.getCell((short) 10); //kolom K
					
					//CELL_TYPE_NUMERIC = 0;
					//CELL_TYPE_STRING = 1;
					//CELL_TYPE_FORMULA = 2;
					//CELL_TYPE_BLANK = 3;
					//CELL_TYPE_BOOLEAN = 4;
					//CELL_TYPE_ERROR = 5;
					
					if(kode != null && tanggal != null) {
						
						if(
							(kode.getCellType() != HSSFCell.CELL_TYPE_STRING && kode.getCellType() != HSSFCell.CELL_TYPE_BLANK) || 
							(nama.getCellType() != HSSFCell.CELL_TYPE_STRING && nama.getCellType() != HSSFCell.CELL_TYPE_BLANK) ||
							(tanggal.getCellType() != HSSFCell.CELL_TYPE_NUMERIC && tanggal.getCellType() != HSSFCell.CELL_TYPE_BLANK && tanggal.getCellType() != HSSFCell.CELL_TYPE_FORMULA) ||
							(transaksi.getCellType() != HSSFCell.CELL_TYPE_STRING && transaksi.getCellType() != HSSFCell.CELL_TYPE_BLANK) || 
							(nab.getCellType() != HSSFCell.CELL_TYPE_NUMERIC && nab.getCellType() != HSSFCell.CELL_TYPE_BLANK && nab.getCellType() != HSSFCell.CELL_TYPE_FORMULA) ||
							(total_unit.getCellType() != HSSFCell.CELL_TYPE_NUMERIC && total_unit.getCellType() != HSSFCell.CELL_TYPE_BLANK && total_unit.getCellType() != HSSFCell.CELL_TYPE_FORMULA) ||
							(redemp.getCellType() != HSSFCell.CELL_TYPE_NUMERIC && redemp.getCellType() != HSSFCell.CELL_TYPE_BLANK && redemp.getCellType() != HSSFCell.CELL_TYPE_FORMULA) ||
							(keterangan.getCellType() != HSSFCell.CELL_TYPE_STRING && keterangan.getCellType() != HSSFCell.CELL_TYPE_BLANK)
						){
							errorMessages.add("Harap Cek Baris ke " + (baris) + " (" + kode.getStringCellValue() + ")");
						}else {
							if(!kode.getStringCellValue().trim().equals("")) {
								Reksadana r = new Reksadana();
								
								r.setIre_reksa_no(kode.getStringCellValue());
								r.setIre_reksa_name(nama.getStringCellValue());
								r.setLku_id(elionsManager.selectKursReksadana(r.getIre_reksa_no()));
								
								if(r.getLku_id() == null){ //bila kurs null, berarti no reksa tsb gak ada
									errorMessages.add("Kode Reksadana untuk baris ke " + (baris) + " (" + kode.getStringCellValue() + ") tidak ditemukan di sistem!");
								}else{
									r.setLus_id(Integer.valueOf(currentUser.getLus_id()));
									
									if(transaksi.getStringCellValue().trim().toUpperCase().equals("BUYING"))
										r.setIrt_rtrans_jn(1); //buying
									else if(transaksi.getStringCellValue().trim().toUpperCase().equals("SELLING"))
										r.setIrt_rtrans_jn(0); //selling
										
									r.setIrt_trans_date(tanggal.getDateCellValue());
									r.setIrt_effective_date(tanggal.getDateCellValue());
									r.setIrt_cost((double) 0); //other cost
									r.setIrt_amount(redemp.getNumericCellValue());
									r.setIrt_nav(nab.getNumericCellValue());
									r.setIrt_subscribe_unit(total_unit.getNumericCellValue());
									r.setIrt_subs_redem_fee((double) 0);
									r.setIrt_note(keterangan.getStringCellValue());
																	
									//perhitungan average cost
									Map data = elionsManager.selectDataAverageCostReksadana(r.getIre_reksa_no(), r.getIrt_trans_date());
									double ldec_acost;
									//sell
									double ldec_subs = ((BigDecimal) data.get("SUBS")).doubleValue(); 
									double ldec_cost = ((BigDecimal) data.get("COST")).doubleValue();
									//buy
									double ldec_tsubs = ((BigDecimal) data.get("TSUBS")).doubleValue(); 
									double ldec_tcost = ((BigDecimal) data.get("TCOST")).doubleValue(); 
									
									ldec_tsubs -= ldec_subs;
									ldec_tcost -= ldec_cost;

									if(ldec_tsubs == 0){
										ldec_acost = 0;
									}else{
										ldec_acost = ldec_tcost / ldec_tsubs;
									}

									if(r.getLku_id().equals("01")){
										ldec_acost = FormatNumber.round(ldec_acost, 4);
									}else{
										ldec_acost = FormatNumber.round(ldec_acost, 10);
									}

									if(r.getIrt_rtrans_jn().intValue() == 1){
										Map dataBuying = elionsManager.selectDataAverageCostBuyingReksadana(r.getIre_reksa_no(), r.getIrt_trans_date());
										int jn = ((BigDecimal) dataBuying.get("IRT_AVERAGE_COST")).intValue();
										ldec_acost = ((BigDecimal) dataBuying.get("IRT_AVERAGE_COST")).doubleValue();
										if(jn == 1) r.setIrt_average_cost(ldec_acost);
									}
									
									r.setIrt_average_cost(ldec_acost);

									//perhitungan total cost
									if(r.getIrt_rtrans_jn().intValue() == 1){
										//r.setIrt_total_cost(r.getIrt_amount());
										r.setIrt_total_cost(total_cost.getNumericCellValue());
									}else if(r.getIrt_rtrans_jn().intValue() == 0){
										r.setIrt_total_cost(r.getIrt_average_cost() * r.getIrt_subscribe_unit());
									}
									
									result.add(r);
								}
							}
						}
					}
					
				}else {
					stop = true;
				}
			}while(!stop);*/
			
			if(!errorMessages.isEmpty()) { 
				cmd.put("errorMessages", errorMessages);
			}else {
				session.setAttribute("resultTransaksiReksadana", result);
				cmd.put("resultTransaksiReksadana", result);
			}

		/* Save TRANSAKSI REKSADANA */
		}else if(request.getParameter("save_trans_reksadana") != null) {
			List<Reksadana> result = (List<Reksadana>) session.getAttribute("resultTransaksiReksadana");
			cmd.put("hasilProses", elionsManager.saveUploadTransaksiReksadana(result, currentUser));
		}
		
		return new ModelAndView("finance/upload", cmd);
	}

}