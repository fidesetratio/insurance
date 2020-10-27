package com.ekalife.elions.web.bac;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.business.bac.AmanahLinkSyariah;
import com.ekalife.elions.business.bac.BankMayapada;
import com.ekalife.elions.business.bac.BankSinarmas;
import com.ekalife.elions.business.bac.Buana;
import com.ekalife.elions.business.bac.Cerdas;
import com.ekalife.elions.business.bac.Eduvest;
import com.ekalife.elions.business.bac.Hcp;
import com.ekalife.elions.business.bac.Horison;
import com.ekalife.elions.business.bac.Investimax;
import com.ekalife.elions.business.bac.Link;
import com.ekalife.elions.business.bac.Maxi;
import com.ekalife.elions.business.bac.PlatinumSave;
import com.ekalife.elions.business.bac.PowerSave;
import com.ekalife.elions.business.bac.SimasPrima;
import com.ekalife.elions.business.bac.SmartInvest;
import com.ekalife.elions.business.bac.SpectaSave;
import com.ekalife.elions.business.bac.Ssp;
import com.ekalife.elions.business.bac.SuperProtection;
import com.ekalife.elions.business.bac.Syariah;
import com.ekalife.elions.business.bac.mediplan;
import com.ekalife.elions.business.bac.medivest;
import com.ekalife.elions.model.Absen;
import com.ekalife.elions.model.Account_recur;
import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.CommandUploadBac;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.DetilTopUp;
import com.ekalife.elions.model.Employee;
import com.ekalife.elions.model.FilePdf;
import com.ekalife.elions.model.InvestasiUtama;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Powersave;
import com.ekalife.elions.model.PrwSeller;
import com.ekalife.elions.model.Rekening_client;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.vo.BacSpajParamVO;
import com.ekalife.utils.Common;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentMultiController;
import com.google.gson.Gson;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.FileUtil;

/**
 *
 */
public class BacMultiController extends ParentMultiController {
	protected final Log logger = LogFactory.getLog( getClass() );

	private String statementNameSpace;

	/**
	 * Untuk cek apakah tertanggung sudah punya polis sblmnya, dicek dari nama dan tanggal lahir, misalnya untuk produk stable link
	 * 
	 * @author Yusuf
	 * @since Jul 15, 2008 (7:40:42 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView cek_sama(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		Map m = new HashMap();
		try {
			String lku_id 	= ServletRequestUtils.getRequiredStringParameter(request, "lku_id");
			Date pp_dob 	= defaultDateFormatReversed.parse(ServletRequestUtils.getRequiredStringParameter(request, "pp_dob"));
			String pp_name 	= ServletRequestUtils.getRequiredStringParameter(request, "pp_name");
			Date tt_dob 	= defaultDateFormatReversed.parse(ServletRequestUtils.getRequiredStringParameter(request, "tt_dob"));
			String tt_name 	= ServletRequestUtils.getRequiredStringParameter(request, "tt_name");
			List daftar = elionsManager.selectPunyaStableLink(lku_id, pp_dob, pp_name, tt_dob, tt_name);
			m.put("daftar", daftar);
		} catch (ParseException e) {
			logger.error("ERROR :", e);
		}
		return new ModelAndView("bac/cek_sama", m);
	}
	
	/**
	 * Controller untuk download form SPAJ
	 * TODO: SEBELUM ENABLE, HARUS DITAMBAHKAN VALIDASI AKSES DAN COUNTER UNTUK NO BLANKO SPAJ
	 * 
	 * @since Aug 14, 2007 (7:06:43 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView download_spaj(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		String dir = props.getProperty("pdf.dir.formSPAJ");
		String dirTemp=props.getProperty("pdf.dir.export");
//		String dir = props.getProperty("//pdf.dir.formSPAJ");
		String file = ServletRequestUtils.getStringParameter(request, "file", "");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lde_id = currentUser.getLde_id();
		
		if (spaj == null){
			spaj ="";
			
			
		}else{
			
		}		
		

		
		Pemegang pemegang= new Pemegang();
		Tertanggung tertanggung = new Tertanggung();
		AddressBilling addrbill = new AddressBilling();
		Datausulan datausulan =new Datausulan();
		InvestasiUtama inv = new InvestasiUtama();
		inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
		inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
		inv.setDaftartopup(new DetilTopUp());
		Rekening_client data_rek=new Rekening_client();
		Account_recur data_account=new Account_recur();
		Powersave data_pwr=new Powersave();
		Agen dataagen=new Agen();
		Employee emp = new Employee();
		
		if (!spaj.equalsIgnoreCase(""))
		{
			pemegang = (Pemegang)this.elionsManager.selectpp(spaj);
			tertanggung = (Tertanggung)this.elionsManager.selectttg(spaj);
			datausulan = (Datausulan)this.elionsManager.selectDataUsulanutama(spaj);
			addrbill = (AddressBilling)this.elionsManager.selectAddressBilling(spaj);
			
			inv  = (InvestasiUtama)this.elionsManager.selectinvestasiutama(spaj);
			if (inv == null)
			{
				inv = new InvestasiUtama();
				inv.setDaftarinvestasi(this.elionsManager.selectDetailInvestasi(null));
				inv.setJmlh_invest(this.elionsManager.selectinvestasiutamakosong(null));
				DetilTopUp  detiltopup = new DetilTopUp();
				inv.setDaftartopup(detiltopup);
				//investasi = inv;
			}else{
				DetilTopUp detiltopup = (DetilTopUp)this.elionsManager.select_detil_topup(spaj);
				if (detiltopup == null)
				{
					detiltopup = new DetilTopUp();
				}
				inv.setDaftartopup(detiltopup);
			}
				
			data_rek = (Rekening_client)this.elionsManager.select_rek_client(spaj);
			if (data_rek==null)
			{
				data_rek = new Rekening_client() ;
			}
			
			data_account =  (Account_recur)this.elionsManager.select_account_recur(spaj);
			if (data_account==null)
			{
				data_account = new Account_recur();
			}
			
			data_pwr = (Powersave)this.elionsManager.select_powersaver(spaj);
			if (data_pwr==null)
			{
				data_pwr = new Powersave();
			}

			dataagen = (Agen)this.elionsManager.select_detilagen(spaj);
			if (dataagen==null)
			{
				dataagen = new Agen();
			}
			
			String kode_agen = dataagen.getMsag_id();
			String nama_agent="";
			if (kode_agen.equalsIgnoreCase("000000"))
			{
				nama_agent = (String)this.elionsManager.select_agent_temp(spaj);
				dataagen.setMcl_first(nama_agent);
			}
			
			
			emp = (Employee)this.elionsManager.select_detilemployee(spaj);
			if (emp ==null)
			{
				emp = new Employee();
			}
			
			datausulan.setDaftahcp(this.elionsManager.select_hcp(spaj));
			datausulan.setDaftapeserta(this.elionsManager.select_semua_mst_peserta(spaj));
		}
		
		
		if(!file.equals("")) {
			if((spaj==null||spaj.equalsIgnoreCase(""))){
							
				/** Contoh 1 - pake pdf stamper, tapi cuman bisa untuk file PDF */ 
	        	if(file.toLowerCase().indexOf(".pdf") > -1) {
//			        try {
//						PdfReader reader = new PdfReader(dir +"\\"+ file);
//						int n = reader.getNumberOfPages();
//						
//						PdfStamper stamp = new PdfStamper(reader,new FileOutputStream("C:\\EkaWeb\\temp.pdf"));
//				
//						//metadata aja
//						HashMap moreInfo = new HashMap();
//						moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
//				        moreInfo.put("Title", file);
//				        moreInfo.put("Subject", "SPAJ Kosong. Didownload oleh " + currentUser.getLus_full_name());
////				        moreInfo.put("Created", new Date());
//				        stamp.setMoreInfo(moreInfo);
//				       
//						stamp.close();
//						
//					} catch (IllegalStateException e) {
//						logger.error("ERROR :", e);
//					} catch (IOException e) {
//						logger.error("ERROR :", e);
//					} catch (DocumentException e) {
//						logger.error("ERROR :", e);
//					}
	        		File l_file = new File(dir +"\\"+ file);
		        	FileInputStream in=null;
		        	ServletOutputStream ouputStream = null;
					try{
						
						response.setContentType("application/pdf");
						response.setHeader("Content-Disposition", "Attachment;filename="+file);
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
	        	}else {
	        		try {
						ServletOutputStream os = response.getOutputStream();
						os.print("<script>alert('Maaf, tetapi anda tidak bisa mengakses file ini.');</script>");
						os.close();
					} catch (IOException e) {
						logger.error("ERROR :", e);
					}
	        	}
					
				/** Contoh 2 - pake class fileutils */
//				try {
//					FileUtils.downloadFile(dir, file, response);
//				} catch (FileNotFoundException e) {
//					logger.error("ERROR :", e);
//				} catch (IOException e) {
//					logger.error("ERROR :", e);
//				}
	        	
				 return null;
			}else{
				Integer x = ServletRequestUtils.getIntParameter(request, "x", 0);
				Integer y = ServletRequestUtils.getIntParameter(request, "y", 0);
				Integer f = ServletRequestUtils.getIntParameter(request, "f", 0);
				
				/** Contoh 1 - pake pdf stamper, tapi cuman bisa untuk file PDF */ 
	        	if(file.toLowerCase().indexOf(".pdf") > -1) {
			        try {
			        	
			        	dirTemp+="\\"+elionsManager.selectCabangFromSpaj(spaj)+"\\"+spaj;
			        	File userDir= new File(dirTemp);
			        	 if(!userDir.exists()) {
					            userDir.mkdirs();
					        }
						PdfReader reader = new PdfReader(dir +"\\"+ file);
						int n = reader.getNumberOfPages();
						
						PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(dirTemp+"\\"+file));
				
						//metadata aja
						HashMap moreInfo = new HashMap();
						moreInfo.put("Author", "PT ASURANSI JIWA SINARMAS MSIG Tbk.");
				        moreInfo.put("Title", file);
				        moreInfo.put("Subject", "SPAJ Kosong. Didownload oleh " + currentUser.getLus_full_name());
//				        moreInfo.put("Created", new Date());
				        stamp.setMoreInfo(moreInfo);

				        if(x.intValue() > 0 && y.intValue() > 0) {
				        	String sequence = elionsManager.sequenceSpajElektronik(currentUser, file);
				        	
					        PdfContentByte over;
					        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
					        over = stamp.getOverContent(1);
					        
					    	//
					    	over.beginText();
					    	over.setFontAndSize(bf, f); //set ukuran font
//					    	over.showTextAligned(Element.ALIGN_LEFT, sequence, x, y, 0);
					    	
					    	// Start 
					    	BacSpajParamVO paramVO = new BacSpajParamVO();
					    	paramVO.setBf(bf);
					    	paramVO.setData_pwr(data_pwr);
					    	paramVO.setData_rek(data_rek);
					    	paramVO.setDataagen(dataagen);
					    	paramVO.setDatausulan(datausulan);
					    	paramVO.setDir(dir);
					    	paramVO.setF(f);
					    	paramVO.setFile(file);
					    	paramVO.setInv(inv);
					    	paramVO.setOver(over);
					    	paramVO.setPemegang(pemegang);
					    	paramVO.setReader(reader);
					    	paramVO.setStamp(stamp);
					    	paramVO.setTertanggung(tertanggung);
					    	paramVO.setX(x);
					    	paramVO.setY(y);
					    	paramVO.setNoBlanko(sequence);
					    	/**
					    	 * dian natalia
					    	 * breakdown ke class sesaui nama file
					    	 */
					    	if(file != null) {
					    		// TODO: break down 
					    		if(file.equals("SPAJ Bancass - Platinum Save.pdf")) //platinum save
					    		{
					    			PlatinumSave business = PlatinumSave.getInstance();
					    			over.showTextAligned(Element.ALIGN_LEFT, sequence, x, y, 0); //cetak string sequence pada posisi x dan y
					    			over = business.createPdf( paramVO );
					    			products.kategoriPosisiTandaTanganSpaj(file, spaj, stamp);
					    		}else if(file.equals("SPAJ Bancass - Platinum Save - Smart Invest.pdf")) {	//Smart invest
					    			SmartInvest business = SmartInvest.getInstance();
					    			over = business.createPdf( paramVO );
					    		}else if(file.equals("SPAJ Bancass - Maxi.pdf")) { // maxi
					    			Maxi business = Maxi.getInstance();
					    			over = business.createPdf( paramVO );	
					    		} else if(file.equals("SPAJ Bancass - Link.pdf")) { //SPAJ Bancass - Link.pdf
					    			Link business = Link.getInstance();
					    			over = business.createPdf( paramVO );			    				
					    		} else if(file.equals("SPAJ Bancass - Cerdas.pdf")) { //cerdas
					    			Cerdas business = Cerdas.getInstance();
					    			over = business.createPdf( paramVO );	
					    		}else if(file.equals("SPAJ Bancass - Specta Save.pdf")) { //specta save
					    			SpectaSave business = SpectaSave.getInstance();
					    			over = business.createPdf( paramVO );
					    		}else if(file.equals("SPAJ Bancass -  Simas Prima.pdf")) { //Simas Prima.pdf
					    			SimasPrima business = SimasPrima.getInstance();
					    			over = business.createPdf( paramVO );
					    		}else if(file.equals("SPAJ Bancass - Bank Sinarmas.pdf")) { //bank sinarmas
					    			BankSinarmas business = BankSinarmas.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ POWER SAVE.pdf")) { //power save
					    			PowerSave business = PowerSave.getInstance();
					    			over = business.createPdf( paramVO );			    			
					    		}else if(file.equals("SPAJ EDUVEST.pdf")) { //edusa
					    			Eduvest business = Eduvest.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ HORISON.pdf")) { //horison
					    			Horison business = Horison.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ INVESTIMAX.pdf")) { //investimax
					    			Investimax business = Investimax.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ MEDIPLAN.pdf")) { //mediplan
					    			mediplan business = mediplan.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ MEDIVEST.pdf")) { //medivest
					    			medivest business = medivest.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ SS - SSP.pdf")) { //medivest
					    			Ssp business = Ssp.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ SUPER PROTECTION.pdf")) { //medivest
					    			SuperProtection business = SuperProtection.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ SYARIAH.pdf")) { //medivest
					    			Syariah business = Syariah.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ Bank Mayapada.pdf")) { //medivest
					    			BankMayapada business = BankMayapada.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ Amanah Link Syariah.pdf")) { //medivest
					    			AmanahLinkSyariah business = AmanahLinkSyariah.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ Power Save UOB Buana.pdf")) { //medivest
					    			Buana business = Buana.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}else if(file.equals("SPAJ HCP Family.pdf")) { //medivest
					    			Hcp business = Hcp.getInstance();
					    			over = business.createPdf( paramVO );		
					    		}
					    	} 	
					    	over.endText();
					    	
				        }
				        
				    	//Yusuf (18/01/08) - Tanda Tangan Pemegang Polis dan Tertanggung
				        products.kategoriPosisiTandaTanganSpaj(file, spaj, stamp);
				        
						stamp.close();
					} catch (IllegalStateException e) {
						logger.error("ERROR :", e);
					} catch (IOException e) {
						logger.error("ERROR :", e);
					} catch (DocumentException e) {
						logger.error("ERROR :", e);
					}
					File l_file = new File(dirTemp+"\\"+file);
		        	FileInputStream in=null;
		        	ServletOutputStream ouputStream = null;
					try{
						
						response.setContentType("application/pdf");
						response.setHeader("Content-Disposition", "Attachment;filename="+file);
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
					try {
						FileUtils.deleteFile(dirTemp, file);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.error("ERROR :", e);
					}
	        	}else {
	        		try {
						ServletOutputStream os = response.getOutputStream();
						os.print("<script>alert('Maaf, tetapi anda tidak bisa mengakses file ini.');</script>");
						os.close();
					} catch (IOException e) {
						logger.error("ERROR :", e);
					}
	        	}
					
				/** Contoh 2 - pake class fileutils */
//				try {
//					FileUtils.downloadFile(dir, file, response);
//				} catch (FileNotFoundException e) {
//					logger.error("ERROR :", e);
//				} catch (IOException e) {
//					logger.error("ERROR :", e);
//				}
	        	
			}
			return null;
			
		}else {
			Map cmd = new HashMap();
			List<FilePdf> daftarFilePdf = new ArrayList<FilePdf>();
			List<DropDown> daftarFile = FileUtils.listFilesInDirectory(dir);
			for(DropDown d : daftarFile) {
				FilePdf f = new FilePdf();
				try {
					PropertyUtils.copyProperties(f, d);
				} catch (IllegalAccessException e) {
					logger.error("ERROR :", e);
				} catch (InvocationTargetException e) {
					logger.error("ERROR :", e);
				} catch (NoSuchMethodException e) {
					logger.error("ERROR :", e);
				}
				if(f.getKey() != null) {
					//Yusuf : Dian, untuk penambahan posisi ini dipindah ke class "products" (17/01/08)
					f = products.kategoriPosisiPrintSpaj(f);
					f.setSpaj(spaj);
					daftarFilePdf.add(f);
				}
			}
			cmd.put("daftar", daftarFilePdf);
			cmd.put("spaj", spaj);
			cmd.put("hist", elionsManager.selectHistoryBlanko(Integer.valueOf(currentUser.getLus_id())));
			return new ModelAndView("bac/download_spaj", cmd);
		}
	}
	
	protected Object insert(String queryName, Object param) throws DataAccessException {
		if (logger.isDebugEnabled()) logger.debug("INSERT: " + queryName);
		return getSqlMapClientTemplate().insert(this.statementNameSpace + queryName, param);
	}

	private BacMultiController getSqlMapClientTemplate() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Controller untuk membantu agency system upload email agen 
	 * @author Yusuf Sutarko
	 * @since Aug 14, 2007 (7:07:19 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView upload_email_agen(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		CommandUploadBac cmd = new CommandUploadBac();
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
					cmd.setDaftarStatus(elionsManager.uploadEmailAgen(cmd, request));
				}
				cmd.setErrorMessages(error);
			} catch (Exception e) {
				logger.error("ERROR :", e);
			}
		}
		return new ModelAndView("common/upload_email_agen", "cmd", cmd);
	}
	
	public ModelAndView absensi_agen(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		User currenUser = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		//SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
		String msag_id = ServletRequestUtils.getStringParameter(request, "msag_id","");
		String birth_date = ServletRequestUtils.getStringParameter(request, "birth_date","");
		
		if(request.getParameter("absen") != null) {
			try {
				//df.parse(birth_date.trim());
				Boolean agenIsExist = uwManager.agenIsExist(msag_id,birth_date);
				if(agenIsExist) {
					String pesan = uwManager.saveDataAbsenAgent(msag_id,birth_date,currenUser.getLus_id());
					Integer tipe = uwManager.selectAbsenAgen(msag_id);
					List<Absen> absen = uwManager.selectMstAgentAbsen(tipe,currenUser.getLus_id());
					
					map.put("pesan", pesan);
					map.put("absen", absen);						
				}
				else map.put("error", "Absen Gagal. Data Agen tidak ada !");

			} catch (Exception e) {
				map.put("error", "Format tanggal salah !");
			}
		}
		return new ModelAndView("common/absensi_agen",map);
	}
	
	public ModelAndView report_absensi(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		if(currentUser.getLde_id().equals("01") || currentUser.getName().equals("ARISEKO") || currentUser.getName().equals("SITI_MAULANI") || currentUser.getName().equals("WIDIA_ASTI")) map.put("lsAdminActive", uwManager.selectLsAdminActive());
		else if(currentUser.getName().equals("TRI")) {
			List<DropDown> admin = uwManager.selectLsAdminActive();
			admin.add(new DropDown("SITI_MAULANI", "1460"));
			map.put("lsAdminActive", admin);
		}
		map.put("lsAgent", uwManager.selectLsAgentAbsen(ServletRequestUtils.getStringParameter(request, "admin",currentUser.getLus_id())));
		map.put("admin", ServletRequestUtils.getStringParameter(request, "admin",""));
		
		
		
		return new ModelAndView("common/report_absensi",map);
	}

    public ModelAndView cek_struktur(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
        logger.info( "BacMultiController.cek_struktur" );

        Map<String, Object> map = new HashMap<String, Object>();
        String msag_id = ServletRequestUtils.getStringParameter(request, "msag_id", "");
        logger.info( "msag_id = " + msag_id );

        List<Map<String, Object>> strukturList = uwManager.selectStrukturAgen( msag_id );
        map.put( "strukturList", strukturList );

        return new ModelAndView( "bac/cek_struktur", map );
	}
    

    
    /**
     * @description Controller untuk Auto Accept
     * @author Deddy / RYan / Canpri
     * @since 15 Aug 2013
     * @param request, response
     * @throws Exception 
     */
    public ModelAndView autoAccept(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	Map pesan = new HashMap();
    	Map pesan2 = new HashMap();
    	List err = new ArrayList();
    	User currentUser = (User) request.getSession().getAttribute("currentUser");
    	String no_reg = ServletRequestUtils.getStringParameter(request, "no_reg", "");
    	Integer flag_reg = ServletRequestUtils.getIntParameter(request, "flag_reg", 0);//0 untuk DBD,PAS, PA Stand Alone, 1 untuk super Protection
    	Pemegang pmg=elionsManager.selectpp(no_reg);
    	Tertanggung tertanggung =elionsManager.selectttg(no_reg);
    	Datausulan dataUsulan = elionsManager.selectDataUsulanutama(no_reg);
    	Map mTertanggung =elionsManager.selectTertanggung(no_reg);
    	Integer isInputanBank = -1;
		isInputanBank = elionsManager.selectIsInputanBank(no_reg);
		
    	//cek no registrasi
    	if(no_reg.equals("")){
    		if(flag_reg==0){
    			pesan.put("lsError", "Fire ID tidak ada, silakan dicek kembali");
    		}else if(flag_reg==1){
    			pesan.put("lsError", "no SPAJ tidak ada, silakan dicek kembali");
    		}
    	}
    	
    	// cek produk, hanya kode2 tertentu yang boleh proses ini
    	if("45,73,130,187".indexOf(dataUsulan.getLsbs_id().toString())<0){
    		pesan.put("lsError", "Hanya Produk-Produk : <br> 1. DB ( Demam Berdarah ) <br> 2. PAS ( Perdana / Single / Ceria / Ideal ) <br> 3. Super Protection <br> 4. PA Stand Alone");
    	}
	
    	//bagian validasi
    	if("45,73,130".indexOf(dataUsulan.getLsbs_id().toString())>-1){
	    	if (dataUsulan.getLku_id().equals("01")){
			   	if (dataUsulan.getMspr_tsi() >500000000){
			   		pesan.put("lsError", "Up Diatas Ketentuan > Rp. 500.000.000 , Tidak Bisa Menggunakan Menu ini");
			   	}
	    	}else if (dataUsulan.getLku_id().equals("02")){
	    		if (dataUsulan.getMspr_tsi() >50000){
			   		pesan.put("lsError", "Up Diatas Ketentuan > US$ 50000 , Tidak Bisa Menggunakan Menu ini");
			   	}
	    	}
    	}
    	
     	if (pmg.getMspo_age() < 17){
    		pesan.put("lsError", "Usia Pemegang POLIS harus > 17 Tahun");
    	}
     	
    	if ("2,4,5,7".indexOf(pmg.getLsre_id().toString())>-1){
    		pesan.put("lsError", "Hubungan PP dengan TTG  PP  harus suami / istri / orang tua / anak/adik kandung / kakak kandung dari TTG");
    	}
    	
    	if(!elionsManager.selectValidasiCheckListBySpaj(no_reg)){
    		pesan.put("lsError", "Harap Input CHECKLIST DOKUMEN POLIS Terlebih Dahulu!");
    		err.add("Harap Input CHECKLIST DOKUMEN POLIS Terlebih Dahulu!");
		}
    	//List<Checklist> listChecklist
    	Date tglTerimaSpaj=(Date)mTertanggung.get("MSTE_TGL_TERIMA_SPAJ");
		Date tglTerimaadmin=(Date)mTertanggung.get("MSTE_TGL_TERIMA_ADMIN");
		Integer lssaId=(Integer)mTertanggung.get("LSSA_ID");
			
		if("2,3,16".indexOf(isInputanBank.toString())<=-1){
			if ("01,58".indexOf(pmg.getLca_id())<=-1 || isInputanBank==4){
				if(tglTerimaadmin==null) {
					pesan.put("lsError","Tanggal Terima Admin masih Kosong. Silakan mengisi tanggal terima Admin terlebih dahulu");
					err.add("Tanggal Terima Admin masih Kosong. Silakan mengisi tanggal terima Admin terlebih dahulu");}
				}
		}
		
		if(lssaId.intValue() == 2) {
			pesan.put("lsError", "SPAJ ini Status Aksepnya : DECLINE. Tidak bisa memproses SPAJ ini lebih lanjut sebelum merubah statusnya.");
		}
		
		if(lssaId.intValue() == 9) {
			pesan.put("lsError", "SPAJ ini Status Aksepnya : POSTPONED. Tidak bisa memproses SPAJ ini lebih lanjut sebelum merubah statusnya.");
		}
    	
		//selain tutupan bancass dan mall, ditambahkan validasi bahwa admin harus melakukan upload file sebelum transfer.
		Date jul182012		= defaultDateFormat.parse("18/7/2012");
		if(uwManager.selectMstInbox(pmg.getLca_id(), null)==null && pmg.getMspo_input_date().after(jul182012)){
				if(isInputanBank < 0 || isInputanBank == 2) {
					String cabang = elionsManager.selectCabangFromSpaj(no_reg);
					String path = props.getProperty("pdf.dir.export").trim() + "\\" + 
									cabang.trim() + "\\" + 
									no_reg.trim() + "\\" + 
									no_reg.trim() + "SPAJ 001.pdf";
					File file = new File(path);
					if(!file.exists()){
						pesan.put("lsError", "Proses Transfer tidak dapat dilakukan sebelum melakukan upload scan(Terutama Upload Scan SPAJ).Silakan upload scan terlebih dahulu.");
					}
					file = null;
				}
		}
			
			
    	//bagian submit
    	if(pesan.isEmpty()){
    		Map mapAutoAccept = bacManager.ProsesAutoAccept(no_reg, flag_reg,pmg,tertanggung,currentUser,request,elionsManager);
    		if(!mapAutoAccept.get("success").toString().isEmpty()){
    			pesan.put("successMessage", mapAutoAccept.get("success").toString());
    		}else if(!mapAutoAccept.get("error").toString().isEmpty()){
    			pesan.put("lsError", mapAutoAccept.get("error").toString());
    		}
    	}
    	
    	pesan.put("err", err);
    	
    	return new ModelAndView("common/info_proses", pesan);
    }
    
    
    
    /**
	 * Controller untuk input Referral Polis di BAC (Menu Tambang Emas)
	 * @author Canpri Setiawan
	 * @since 10 Jun 2012
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException, MailException, MessagingException
     * @throws ParseException 
	 */
    public ModelAndView referensi(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, MailException, MessagingException, ParseException{
    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	ServletContext context = request.getSession().getServletContext();
		HttpSession session=request.getSession();
		User currentUser=(User) session.getAttribute("currentUser");
		
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		String polis = ServletRequestUtils.getStringParameter(request, "polis","");
		/*String cek = ServletRequestUtils.getStringParameter(request, "check","");*/
		Boolean cek = ServletRequestUtils.getBooleanParameter(request, "check", false);
		
		List lsAgent = new ArrayList();
		String id = null;
		String mcl_first = null;
		Date mspe_date_birth = null;
		String policy_no = null;
		String ref = null;
		String email_ref = null;
		
		List dt_polis = uwManager.selectDataPolisRef(spaj,polis);
		
		if(!dt_polis.isEmpty()){
			Map m = (HashMap) dt_polis.get(0);
			lsAgent = uwManager.selectAgentByPolis((String)m.get("MSPO_POLICY_HOLDER"));
			
			Map ag = (HashMap)lsAgent.get(0);
			String email_agen = (String)ag.get("EMAIL");
			String lsrg = (String)ag.get("LSRG_NAMA");
			
			id = (String) m.get("ID");
			mcl_first = (String)m.get("NAMA");
			mspe_date_birth = (Date)m.get("TGL_LAHIR");
			policy_no = (String) m.get("MSPO_POLICY_NO");
			email_ref = (String) m.get("EMAIL");
			
			map.put("nama_ref", (String)m.get("NAMA"));
			map.put("tgl_lahir_ref", (Date)m.get("TGL_LAHIR"));
			map.put("email_ref", email_ref.trim());
			map.put("agent", lsAgent);
			map.put("email_agen", email_agen);
			map.put("lsrg", lsrg);
			map.put("daftar", uwManager.selectReference(id));
			map.put("polis", spaj);
			map.put("id_seller", uwManager.selectIdSeller(mcl_first, mspe_date_birth, id));
		}
		
		if(request.getParameter("btnSimpan")!=null){
			String nama_ref = ServletRequestUtils.getStringParameter(request, "nama_ref","");
			String tgl_lahir_ref = ServletRequestUtils.getStringParameter(request, "tgl_lahir_ref","");
			//String no_telp_ref = ServletRequestUtils.getStringParameter(request, "tgl_lahir_ref","");
			
			String nama = ServletRequestUtils.getStringParameter(request, "nama","");
			String tgl_lahir = ServletRequestUtils.getStringParameter(request, "tgl_lahir","");
			String no_telp = ServletRequestUtils.getStringParameter(request, "no_telp","");
			String jenis = ServletRequestUtils.getStringParameter(request, "jenis","");
			String agen = ServletRequestUtils.getStringParameter(request, "agen","");
			String kd_agen = ServletRequestUtils.getStringParameter(request, "kd_agen","");
			String agen1 = ServletRequestUtils.getStringParameter(request, "agen1","");
			String kd_agen1 = ServletRequestUtils.getStringParameter(request, "kd_agen1","");
			String cmb_agen = ServletRequestUtils.getStringParameter(request, "cmb_agen","");
			String email_agen = ServletRequestUtils.getStringParameter(request, "email_agen","");
			String email_agen1 = ServletRequestUtils.getStringParameter(request, "email_agen1","");
			
			//Untuk isi manual Referral
			String nama_ref2 = ServletRequestUtils.getStringParameter(request, "nama_ref2","");
			String tgl_lahir_ref2 = ServletRequestUtils.getStringParameter(request, "tgl_lahir_ref2","");
			String tp_lahir_ref = ServletRequestUtils.getStringParameter(request, "tp_lahir_ref","");
			String alamat_ref = ServletRequestUtils.getStringParameter(request, "alamat_ref","");
			String kota_ref = ServletRequestUtils.getStringParameter(request, "kota_ref","");
			String kode_pos_ref = ServletRequestUtils.getStringParameter(request, "kode_pos_ref","");
			String area_telp_ref = ServletRequestUtils.getStringParameter(request, "area_telp_ref","");
			String telp_ref = ServletRequestUtils.getStringParameter(request, "telp_ref","");
			String area_telp_kantor_ref = ServletRequestUtils.getStringParameter(request, "area_telp_kantor_ref","");
			String telp_kantor_ref = ServletRequestUtils.getStringParameter(request, "telp_kantor_ref","");
			String hp_ref = ServletRequestUtils.getStringParameter(request, "hp_ref","");
					
			map.put("spaj2", spaj);
			map.put("polis", polis);
			
			Integer cek_agen = uwManager.cekRef(nama,tgl_lahir);
			if(cek_agen>0){
				map.put("pesan", "Nama ini terdaftar sebagai Agen dan tidak bisa direferensikan");
				map.put("daftar", uwManager.selectReference(id));
			}else{
				//START
				
				//IF NULL			
				if(mcl_first==null){
					String spaj2 = ServletRequestUtils.getStringParameter(request, "spaj2","");
					String polis2 = ServletRequestUtils.getStringParameter(request, "polis2","");
					dt_polis = uwManager.selectDataPolisRef(spaj2,polis2);
					if(!dt_polis.isEmpty()){
						Map m = (HashMap) dt_polis.get(0);
						lsAgent = uwManager.selectAgentByPolis((String)m.get("MSPO_POLICY_HOLDER"));
						
						Map ag = (HashMap)lsAgent.get(0);
						email_agen = (String)ag.get("EMAIL");
						String lsrg = (String)ag.get("LSRG_NAMA");
						
						id = (String) m.get("ID");
						mcl_first = (String)m.get("NAMA");
						mspe_date_birth = (Date)m.get("TGL_LAHIR");
						policy_no = (String) m.get("MSPO_POLICY_NO");
						email_ref = (String) m.get("EMAIL");
						
						map.put("nama_ref", (String)m.get("NAMA"));
						map.put("tgl_lahir_ref", (Date)m.get("TGL_LAHIR"));
						map.put("email_ref", email_ref);
						map.put("agent", lsAgent);
						map.put("email_agen", email_agen);
						map.put("lsrg", lsrg);
						map.put("daftar", uwManager.selectReference(id));
						map.put("polis", spaj);
						map.put("id_seller", uwManager.selectIdSeller(mcl_first, mspe_date_birth, id));
					}
				}
				//END
				
				String ids = null;
				String pesan = null;
				String kode_agent = null;
				String nama_agent = null;
				String email_ag = null;
				Integer err = 0;
				
				if(cmb_agen.equals("0")){
					kode_agent = kd_agen;
					nama_agent = agen;
					email_ag = email_agen;
					//ref = "Nasabah";
				}else{
					kode_agent = kd_agen1;
					nama_agent = agen1;
					email_ag = email_agen1;
					//ref = "Agent";
				}
				
				if(jenis.equals("0")){
					ref = "Nasabah";
				}else{
					ref = "Agent";
				}
				
				String msag_id = uwManager.selectMsag_id(id);
				
				if(msag_id==null){//jika msag_id tidak ada
					String id_seller = "";
					if(cek==true){
						id_seller = uwManager.selectIdSeller(nama_ref2, df.parse(tgl_lahir_ref2), null);
					}else{
						id_seller = uwManager.selectIdSeller(mcl_first, mspe_date_birth, id);
					}
					
					if(id_seller==null){//jika id seller tidak ada
						ids = uwManager.getIdSeller();
						PrwSeller ps = new PrwSeller();
						
						if(cek==true){
							ps.setId_seller(ids);
							ps.setId(1);
							ps.setKode_program(2);
							ps.setNo_polis(policy_no);
							ps.setNama(nama_ref2.toUpperCase());
							ps.setTgl_lahir(df.parse(tgl_lahir_ref2));
							ps.setTp_lahir(tp_lahir_ref.toUpperCase());
							ps.setAlamat(alamat_ref.toUpperCase());
							ps.setKota(kota_ref.toUpperCase());
							ps.setKd_pos(kode_pos_ref);
							ps.setArea_tlp(area_telp_ref);
							ps.setNo_telp(telp_ref);
							ps.setArea_tlp_kantor(area_telp_kantor_ref);
							ps.setTlp_kantor(telp_kantor_ref);
							ps.setNo_hp(hp_ref);
						}else{
							ps = uwManager.selectDataPs(id);	
							ps.setId_seller(ids);
							ps.setId(1);
							ps.setKode_program(2);
							ps.setNo_polis(policy_no);
						}
						
						//insert ke table pwr_seller
						uwManager.insertDataPWRSeller(ps);
						//update msag_id di lst_user_external
						uwManager.updateMsag_idUserExternal(ids, id);
						
						//send email pertama kali generate id seller
						String mail = "";
						if(ps.getEmail()==null){
							mail = "dan Email tidak bisa dikirim karena kosong";
						}else{
//							email.send(true, "cs@sinarmasmsiglife.co.id", new String[]{ps.getEmail()}, null, new String[]{"canpri@sinarmasmsiglife.co.id"}, "Id Referral Tambang Emas",
//									"ID Refferal berhasil di buat<br><br>"+
//									"ID Refferal : "+ps.getId_seller()+"<br>"+
//									"Nama : "+ps.getNama()+"<br>"+
//									"Tgl. Lahir : "+defaultDateFormat.format(ps.getTgl_lahir())+"<br>"+
//									"No. Telp : "+ps.getNo_telp()+"<br>", 
//									null);
							EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, elionsManager.selectSysdate(), null, true, 
									"cs@sinarmasmsiglife.co.id", new String[]{ps.getEmail()}, null, new String[]{"canpri@sinarmasmsiglife.co.id"}, 
									"Id Referral Tambang Emas", "ID Refferal berhasil di buat<br><br>"+
											"ID Refferal : "+ps.getId_seller()+"<br>"+
											"Nama : "+ps.getNama()+"<br>"+
											"Tgl. Lahir : "+defaultDateFormat.format(ps.getTgl_lahir())+"<br>"+
											"No. Telp : "+ps.getNo_telp()+"<br>", null, null);
							
							mail = "dan Email berhasil dikirim";
						}
						
						if(jenis.equals("0")){//Nasabah
							try{
								/*String cekref = uwManager.selectCekRef(nama,tgl_lahir,jenis);
								if(cekref.equals("") || cekref==null){*/
									uwManager.insertReferensi(ids, nama, tgl_lahir, no_telp, kode_agent, nama_agent, jenis, currentUser.getLus_id());
									uwManager.updateIdRef(spaj,ids);
									pesan = "Data Nasabah Berhasil di Input ."+mail+". Id Referral : "+ps.getId_seller();
								/*}else{
									pesan = "Nama ini telah direferensikan";
								}*/
							}catch(Exception e){
								pesan = "Data Nasabah Gagal di Input";
								err += 1;
							}
						}
						
						if(jenis.equals("1")){//Agen
							try{
								/*String cekref = uwManager.selectCekRef(nama,tgl_lahir,jenis);
								if(cekref.equals("") || cekref==null){*/
									uwManager.insertReferensi(ids, nama, tgl_lahir, no_telp, null, null, jenis, currentUser.getLus_id());
									uwManager.updateIdRef(spaj,ids);
									pesan = "Data Agen Berhasil di Input. "+mail+". Id Referral : "+ps.getId_seller();
								/*}else{
									pesan = "Nama ini telah direferensikan";
								}*/
								
							}catch(Exception e){
								pesan = "Data Agen Gagal di Input";
								err += 1;
							}
						}
						
					}else{//jika id seller ada
						
						ids = id_seller;
						//update msag_id di lst_user_external
						uwManager.updateMsag_idUserExternal(ids, id);
						
						if(jenis.equals("0")){//Nasabah
							try{
								/*String cekref = uwManager.selectCekRef(nama,tgl_lahir,jenis);
								if(cekref.equals("") || cekref==null){*/
									uwManager.insertReferensi(ids, nama, tgl_lahir, no_telp, kode_agent, nama_agent, jenis, currentUser.getLus_id());
									uwManager.updateIdRef(spaj,ids);
									pesan = "Data Nasabah Berhasil di Input";
								/*}else{
									pesan = "Nama ini telah direferensikan";
								}*/
							}catch(Exception e){
								pesan = "Data Nasabah Gagal di Input";
								err += 1;
							}
						}
						
						if(jenis.equals("1")){//Agen
							try{
								/*String cekref = uwManager.selectCekRef(nama,tgl_lahir,jenis);
								if(cekref.equals("") || cekref==null){*/
									uwManager.insertReferensi(ids, nama, tgl_lahir, no_telp, null, null, jenis, currentUser.getLus_id());
									uwManager.updateIdRef(spaj,ids);
									pesan = "Data Agen Berhasil di Input";
								/*}else{
									pesan = "Nama ini telah direferensikan";
								}*/
							}catch(Exception e){
								pesan = "Data Agen Gagal di Input";
								err += 1;
							}
						}
					}
				}else{//jika msag_id ada
					ids = msag_id;
					if(jenis.equals("0")){//Nasabah
						try{
							/*String cekref = uwManager.selectCekRef(nama,tgl_lahir,jenis);
							if(cekref.equals("") || cekref==null){*/
								uwManager.insertReferensi(ids, nama, tgl_lahir, no_telp, kode_agent, nama_agent, jenis, currentUser.getLus_id());
								uwManager.updateIdRef(spaj,ids);
								pesan = "Data Nasabah Berhasil di Input";
							/*}else{
								pesan = "Nama ini telah direferensikan";
							}*/
						}catch(Exception e){
							pesan = "Data Nasabah Gagal di Input";
							err += 1;
						}
					}
					
					if(jenis.equals("1")){//Agen
						try{
							/*String cekref = uwManager.selectCekRef(nama,tgl_lahir,jenis);
							if(cekref.equals("") || cekref==null){*/
								uwManager.insertReferensi(ids, nama, tgl_lahir, no_telp, null, null, jenis, currentUser.getLus_id());
								uwManager.updateIdRef(spaj,ids);
								pesan = "Data Agen Berhasil di Input";
							/*}else{
								pesan = "Nama ini telah direferensikan";
							}*/
						}catch(Exception e){
							pesan = "Data Agen Gagal di Input";
							err += 1;
						}
					}
				}
				if(cek==true)mcl_first = nama_ref2;
				
				if(err==0){
//					email.send(true, props.getProperty("admin.java"), new String[]{"wesni@sinarmasmsiglife.co.id","tri.handini@sinarmasmsiglife.co.id"}, null, new String[]{"canpri@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id"}, "Tambang Emas",
//							"Berhasil input dengan id "+ids+"<br>"+
//							"Nama Referal : "+mcl_first.toUpperCase()+"<br><br>"+
//							"REFERENSI"+"<br>"+
//							"Nama : "+nama.toUpperCase()+"<br>"+
//							"Tgl. Lahir : "+tgl_lahir+"<br>"+
//							"No. Telp : "+no_telp+"<br>"+
//							"Jenis Referensi : "+ref, null);
					EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, elionsManager.selectSysdate(), null, true, 
							props.getProperty("admin.ajsjava"), new String[]{"tri.handini@sinarmasmsiglife.co.id"}, null, new String[]{"canpri@sinarmasmsiglife.co.id","deddy@sinarmasmsiglife.co.id"}, 
							"Tambang Emas", "Berhasil input dengan id "+ids+"<br>"+
									"Nama Referal : "+mcl_first.toUpperCase()+"<br><br>"+
									"REFERENSI"+"<br>"+
									"Nama : "+nama.toUpperCase()+"<br>"+
									"Tgl. Lahir : "+tgl_lahir+"<br>"+
									"No. Telp : "+no_telp+"<br>"+
									"Jenis Referensi : "+ref, null, spaj);
				
					//kirim email ke agen
					if(jenis.equals("0")){
						if(email_ag!=null || email_ag.equals("")){
//							email.send(true, "cs@sinarmasmsiglife.co.id", new String[]{email_ag}, null, new String[]{"canpri@sinarmasmsiglife.co.id"}, "Tambang Emas",
//								"<p><strong>Tambang Emas</strong></p>"+
//								"<p>Telah diinput atas kode agent Anda tanggal <strong>"+df.format(elionsManager.selectSysdate())+"</strong> dengan rincian :</p>"+
//								"<p><strong>Agen </strong><br />"+
//								"Kode agen : "+kode_agent+"<br />"+
//								"Nama : "+nama_agent+"</p>"+
//								"<p><strong>Referral </strong><br />"+
//								"Kode referral : "+ids+"<br />"+
//								"Nama : "+mcl_first.toUpperCase()+"<br />"+
//								"Tgl lahir : "+mspe_date_birth+"</p>"+
//								"<p><strong>Referensi </strong><br />"+
//								"Nama : "+nama.toUpperCase()+"<br />"+
//								"Tgl lahir : "+tgl_lahir+"<br />"+
//								"Telpon : "+no_telp+"<br />"+
//								"Jenis referensi :"+ref+"</p>",
//								null);
							EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, elionsManager.selectSysdate(), null, true, 
									"cs@sinarmasmsiglife.co.id", new String[]{email_ag}, null, new String[]{"canpri@sinarmasmsiglife.co.id"}, 
									"Tambang Emas", "<p><strong>Tambang Emas</strong></p>"+
											"<p>Telah diinput atas kode agent Anda tanggal <strong>"+df.format(elionsManager.selectSysdate())+"</strong> dengan rincian :</p>"+
											"<p><strong>Agen </strong><br />"+
											"Kode agen : "+kode_agent+"<br />"+
											"Nama : "+nama_agent+"</p>"+
											"<p><strong>Referral </strong><br />"+
											"Kode referral : "+ids+"<br />"+
											"Nama : "+mcl_first.toUpperCase()+"<br />"+
											"Tgl lahir : "+mspe_date_birth+"</p>"+
											"<p><strong>Referensi </strong><br />"+
											"Nama : "+nama.toUpperCase()+"<br />"+
											"Tgl lahir : "+tgl_lahir+"<br />"+
											"Telpon : "+no_telp+"<br />"+
											"Jenis referensi :"+ref+"</p>", null, spaj);
						}
					}
				}
				map.put("pesan", pesan);
				map.put("daftar", uwManager.selectReference(id));
				map.put("id_seller", uwManager.selectIdSeller(mcl_first, mspe_date_birth, id));
				//END
			}
			
		}
		
		map.put("produk", uwManager.selectProdukTE());

        return new ModelAndView( "bac/referensi", map );
	}
    
    /**
	 * Controller untuk Men-generate kode untuk melewati input id referral di Input BAC (Menu Tambang Emas)
	 * @author Canpri Setiawan
	 * @since 12 Jul 2012
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException, MailException, MessagingException
     * @throws ParseException 
	 */
    /*public ModelAndView generate(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, MailException, MessagingException, ParseException{
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	ServletContext context = request.getSession().getServletContext();
		HttpSession session=request.getSession();
		User currentUser=(User) session.getAttribute("currentUser");
		
		if(request.getParameter("btnGenerate")!=null){
			String kode = CheckSum.generateKode10Digit(1);
			map.put("kode", kode);
		}
		
		if(request.getParameter("btnSimpan")!=null){
			String kode = ServletRequestUtils.getStringParameter(request, "kode","");
			
			String cek = uwManager.selectKodeTambangEmas(kode);
			if(cek==null){//insert
				uwManager.insertKodeTE(kode,currentUser.getLus_id());
				map.put("pesan", "Generate Kode Berhasil - "+kode);
			}else{
				map.put("pesan", "Kode Sudah Ada Harap Generate Ulang");
			}
		}
    	
    	return new ModelAndView( "bac/generateKode", map );
    }*/
    
    /**
	 * Controller untuk meredeem/menarik point Tambang Emas
	 * @author Canpri Setiawan
	 * @since 13 Jul 2012
	 * @param request
	 * @param response
	 * @return
     * @throws Exception 
	 */
    /*public ModelAndView redeem_point(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	
		HttpSession session=request.getSession();
		User currentUser=(User) session.getAttribute("currentUser");
		
		//form backing object
		Redeem redeem = new Redeem();
		
		//bind data
		ServletRequestDataBinder binder = createBinder(request, redeem);		
		
		binder.bind(request);
		BindException err = new BindException(binder.getBindingResult());
		
		List product = uwManager.selectProdukTE();
		request.setAttribute("produk", product);
		
		if(request.getParameter("btnSimpan")!=null){
			
			ValidationUtils.rejectIfEmpty(err, "kd_ref", "", "Kode Referral harus diisi");
			ValidationUtils.rejectIfEmpty(err, "nama_ref", "", "Nama Referral harus diisi");
			ValidationUtils.rejectIfEmpty(err, "poin_ref", "", "Poin harus diisi");
			ValidationUtils.rejectIfEmpty(err, "reg_hadiah", "", "registrasi hadiah harus pilih");
			ValidationUtils.rejectIfEmpty(err, "alamat", "", "Alamat harus diisi");
			ValidationUtils.rejectIfEmpty(err, "kd_pos", "", "Kode pos harus diisi");
			
			//bila tidak ada error
			if(!err.hasErrors()) {
				
				String id_item = redeem.getHadiah().substring(0, redeem.getHadiah().indexOf("~"));
				Long poin = Long.parseLong(redeem.getHadiah().substring(redeem.getHadiah().indexOf("~")+1));
				
				Long poin_akum = uwManager.selectPoinRedeem(redeem.getKd_ref());
				
				Long poin2 = poin + poin_akum;
				
				//redeem.setPoin_ref((long) 310000);
				Long sisa_poin = redeem.getPoin_ref() - poin2;
				if(sisa_poin<0){
					request.setAttribute("pesan", "Maaf poin Anda tidak mencukupi untuk mengambil hadiah ini!");
				}else{
					redeem.setId_item(id_item);
					redeem.setPoin_hadiah(poin);
					uwManager.insertRedeemTE(redeem);
					request.setAttribute("pesan", "Redeem poin Anda  berhasil");
				}
				
			}
		}
		if(redeem.getKd_ref()!=null){
			List red = uwManager.selectRedeemTE(redeem.getKd_ref(), "ALL", null);	
			request.setAttribute("list", red);
		}
		
		return new ModelAndView("bac/redeem_poin", err.getModel());
    }*/
    
    /**
	 * Controller untuk proses approve redeem point Tambang Emas
	 * @author Canpri Setiawan
	 * @since 28 Sep 2012
	 * @param request
	 * @param response
	 * @return
     * @throws Exception 
	 */
    /*public ModelAndView redeem_proses(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	
		HttpSession session=request.getSession();
		User currentUser=(User) session.getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		String warn = "";
		
		if(request.getParameter("btnApprove") != null) {
			String check[] = request.getParameterValues("chbox");
			
			if(request.getParameterValues("chbox") != null){
				for(int i=0;i<check.length;i++){
					List info = uwManager.selectRedeemTE(null, "0", check[i]);
					
					Map m = (HashMap)info.get(0);
					String id_ref = (String)m.get("ID_SELLER");
					Integer id_red = ((BigDecimal)m.get("ID_REDEEM")).intValue();
					String id_redeem = id_red.toString();
					String item = (String)m.get("ID_ITEM");
					Integer poin = ((BigDecimal)m.get("POIN")).intValue();
					String nama = (String)m.get("NAMA");
					String nama_item = (String)m.get("NAMA_ITEM");
					
					String id_trx = StringUtils.leftPad(uwManager.selectIdTrx(null,null,"3"), 10, '0');
					uwManager.insertPwrTrx(id_trx, id_ref, id_redeem, "1");
					uwManager.insertPwrDTrx(id_trx, item, id_redeem, (double) 0, poin, currentUser.getLus_id());
					uwManager.updateRedeem(check[i],"1", currentUser.getLus_id());//1 di approve, 2 di tolak
					
					warn += "Redeem dengan Id Referral "+id_ref+" atas nama "+nama+" dengan pilihan item -"+nama_item+"- berhasil di approve.";
				}
			}
		}
		
		if(request.getParameter("btnTolak") != null) {
			String check[] = request.getParameterValues("chbox");
			
			if(request.getParameterValues("chbox") != null){
				for(int i=0;i<check.length;i++){
					List info = uwManager.selectRedeemTE(null, "0", check[i]);
					
					Map m = (HashMap)info.get(0);
					String id_ref = (String)m.get("ID_SELLER");
					Integer id_red = ((BigDecimal)m.get("ID_REDEEM")).intValue();
					String id_redeem = id_red.toString();
					String item = (String)m.get("ID_ITEM");
					Integer poin = ((BigDecimal)m.get("POIN")).intValue();
					String nama = (String)m.get("NAMA");
					String nama_item = (String)m.get("NAMA_ITEM");
					
					uwManager.updateRedeem(check[i],"2", currentUser.getLus_id());//1 di approve, 2 di tolak
					
					warn += "Redeem dengan Id Referral "+id_ref+" atas nama "+nama+" dengan pilihan item -"+nama_item+"- telah ditolak.";
					
				}
			}
		}
		
		List red = uwManager.selectRedeemTE(null, "0", null);	
		map.put("list", red);
		map.put("warn", warn);
		
		return new ModelAndView("bac/redeem_proses", map);        
    }*/
    
    /**
	 * Controller Admin melihat data kontes proklamasi (kerjasama dengan jobstreet)
	 * @author Canpri Setiawan
	 * @since 19 Agustus 2013
	 * @param request
	 * @param response
	 * @return
     * @throws Exception 
     * 
     * http://localhost/E-Lions/bac/multi.htm?window=admin_kontes_proklamasi
	 */
    public ModelAndView admin_kontes_proklamasi(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session=request.getSession();
		User currentUser=(User) session.getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		List data = bacManager.selectDataKontesProklamasi();
    	
		map.put("data", data);
		return new ModelAndView("bac/admin_kontes_proklamasi", map);     
    }
    
    /**
	 * Controller Admin melihat data kontes Photo Family (kerjasama dengan jobstreet)
	 * @author Canpri Setiawan
	 * @since 05 September 2013
	 * @param request
	 * @param response
	 * @return
     * @throws Exception 
     * 
     * http://localhost/E-Lions/bac/multi.htm?window=admin_kontes_photo
	 */
    public ModelAndView admin_kontes_photo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session=request.getSession();
		User currentUser=(User) session.getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		String jenis = ServletRequestUtils.getStringParameter(request, "jenis","0");
		
		if(jenis.equals("0")){
			List data = bacManager.selectDataKontesPhoto();
	    	
			map.put("data", data);
			return new ModelAndView("bac/admin_kontes_photo", map);     
		}else{
			String image = ServletRequestUtils.getStringParameter(request, "image",null);
			String attach=ServletRequestUtils.getStringParameter(request, "attach", "inline");
			
			String fileName="noimage.png";
			String destDir = props.getProperty("pdf.dir.photo_family");
			
			if(image!=null){
				fileName=image;
				File file=new File(destDir+"\\"+fileName);
				if(file.exists()){
					try{
						FileUtil.downloadFile(destDir, fileName, response, attach);
					}catch (Exception e) {
						// TODO: handle exception
					}
				}else{
					fileName="noimage.png";
					try{
						FileUtil.downloadFile(destDir, fileName, response, attach);
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
			}else{
				try{
					FileUtil.downloadFile(destDir, fileName, response, attach);
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			// 
			return null;
		}
    }
    
    /**
	 * Controller untuk merequest print surat oleh cabang
	 * 
	 * @since Feb 10, 2014 (16:06:43 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
     * @throws IOException 
     * 
     * http://localhost/E-Lions/bac/multi.htm?window=request_surat
     * @throws MessagingException 
     * @throws MailException 
     * @throws DataAccessException 
	 */
	public ModelAndView request_surat(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, IOException, DataAccessException, MailException, MessagingException{
		HttpSession session=request.getSession();
		User currentUser=(User) session.getAttribute("currentUser");
		Map<String, Object> map = new HashMap<String, Object>();
		
		int json = ServletRequestUtils.getIntParameter(request, "json", 0);
		
		if(json==1){
			String polis = ServletRequestUtils.getStringParameter(request, "polis","");
			polis = polis.replace(".", "");
			
			String spaj = elionsManager.selectSpajFromPolis(polis);
    		String result = "Nomor polis tidak ada.";
    		if(spaj!=null){
    			Map select_p = elionsManager.selectPemegang(spaj);
    			String pemegang = (String)select_p.get("MCL_FIRST");
    			result = "Pemegang polis : "+pemegang;
    		}
    		
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
		}else{
			List jn_surat = bacManager.selectJenisSurat("0,1");
			
			if(request.getParameter("btnSave") != null) {
				String polis = ServletRequestUtils.getStringParameter(request, "polis","");
				String jn_srt = ServletRequestUtils.getStringParameter(request, "jn_surat","");
				String surat = ServletRequestUtils.getStringParameter(request, "srt_text","");
				
				polis = polis.replace(".", "");
				
				String spaj = elionsManager.selectSpajFromPolis(polis);
				String pesan = "";
				
				if(spaj==null){
					pesan = "No polis tidak ada.";
				}else{
					pesan = bacManager.insertRequestPrintSurat(currentUser, spaj, polis, jn_srt, surat);
				}
				
				//map.put("surat_text", surat);
				map.put("pesan", pesan);
				//map.put("polis", polis);
				//map.put("sel_surat", jn_srt);
			}
			
			if(request.getParameter("btnCari") != null) {
				String polis = ServletRequestUtils.getStringParameter(request, "polis","");
				//polis = polis.replace(".", "");
				
				String spaj = elionsManager.selectSpajFromPolis(polis);
				List listPolis = new ArrayList();
				if(!polis.equals(""))listPolis = bacManager.selectFindPolis(polis);
				
				
				map.put("listPolis", listPolis);
			}
			
			List req_surat = bacManager.selectReqPrintSurat(currentUser.getLus_id());
			
			map.put("req_surat", req_surat);
			map.put("surat", jn_surat);
			map.put("user", currentUser.getLus_full_name());
			return new ModelAndView("bac/request_surat", map); 
		}
		
		return null;
	}
    
	/**
	* Program Sosial Nasabah (Menu PSN)
	* @author Ryan
	* @since 24/03/2014
	* @param request
	* @param response
	* @return
	*/
    public ModelAndView psn(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, MailException, MessagingException, ParseException{
    	Map cmd = new HashMap();
    	List listLembaga=null;
    	List listItem=null;
    	String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
    	String stpolis = ServletRequestUtils.getStringParameter(request, "select_data","");
    	String namapsn = ServletRequestUtils.getStringParameter(request, "namapsn","");
    	String qty= ServletRequestUtils.getStringParameter(request, "qty", "0");
    	User currentUser = (User) request.getSession().getAttribute("currentUser");
    	List<Map> pesanList = new ArrayList<Map>();	
    	Date sysdate = uwManager.selectSysdateTruncated(0);
    	List select_data = bacManager.selectJenisPSN();
    	Pemegang pmg=elionsManager.selectpp(spaj);
    	Tertanggung tertanggung =elionsManager.selectttg(spaj);
    	String polis =FormatString.nomorPolis(pmg.getMspo_policy_no());
    	String formatspaj = FormatString.nomorPolis(pmg.getReg_spaj());
    	Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
    	Integer id_item=null;
    	String buka=null;
    	String check=null;
    	if (stpolis.equals("ALL")){
    		Map<String,String> map2= new HashMap<String, String>();
    		map2.put("sts", "ERROR ");
    		map2.put("msg", "Harap Memilih Jenis Form Terlebih Dahulu");
    		pesanList.add(map2);
    	}

    	if(stpolis.equals("1")){
    		buka="1";
    		listLembaga = bacManager.selectDataLembagaPsn(stpolis);
    	}
    	if(stpolis.equals("2")){
    		buka="2";
    		listLembaga = bacManager.selectDataLembagaPsn(stpolis);
    		id_item=1;
    		sysdate =null;
    	}
    	if(stpolis.equals("3")){
    		buka="3";
    		listLembaga = bacManager.selectDataLembagaPsn(stpolis);
    	}
    	if(stpolis.equals("4")){
    		buka="4";
    		listLembaga = bacManager.selectDataLembagaPsn(stpolis);
    	}
    	if(stpolis.equals("5")){
    		buka="5";
    		listLembaga = bacManager.selectDataLembagaPsn(stpolis);
    		listItem=bacManager.selectDataHargaItem(stpolis);
    		String check4 = request.getParameter("chbox4");
    		if (check4!=null){
    			id_item=Integer.parseInt(check4);
    		}

    	}
    	if(stpolis.equals("6")){
    		buka="6";
    		listLembaga = bacManager.selectDataLembagaPsn(stpolis);
    		listItem=bacManager.selectDataReligi(stpolis);
    		String id_item2 = ServletRequestUtils.getStringParameter(request, "select_id_item","");
    		if (!id_item2.equals("")){
    			id_item=Integer.parseInt(id_item2);
    		}
    	}
    	//Save Point 
    	if(request.getParameter("save") != null){
    		if(stpolis.equals("1") || stpolis.equals("2")){
    			check = request.getParameter("chbox");}
    		else{
    			check="0";
    		}
    		String check2 = request.getParameter("chbox2");
    		String check3 = request.getParameter("chbox3");
    		//untuk religi, component parameternya di set 5 untk check3
    		if(stpolis.equals("6")){
    			check3 = "5";
    			sysdate=elionsManager.selectFAddMonths(defaultDateFormatStripes.format(sysdate),new Integer(12));}
    		String code_id = ServletRequestUtils.getStringParameter(request, "nominal", "");

    		bacManager.insertPSN(spaj, id_item, Integer.parseInt(stpolis), Integer.parseInt(check3), sysdate, Integer.parseInt(check2), Integer.parseInt(check2), Integer.parseInt(code_id), Integer.parseInt(qty), Integer.parseInt(check), Integer.parseInt(currentUser.getLus_id()), null, 1);
    		Map<String,String> map2= new HashMap<String, String>();
    		map2.put("sts", "SUCCESS ");
    		map2.put("msg", "Data Berhasil Disimpan ");
    		pesanList.add(map2);
    		buka=null;
    	}
    	cmd.put("select_data", select_data);
    	cmd.put("pemegang", pmg);
    	cmd.put("tertanggung", tertanggung);
    	cmd.put("polis", polis);
    	cmd.put("formatspaj", formatspaj);
    	cmd.put("dataUsulan", dataUsulan);
    	cmd.put("pesanList", pesanList);
    	cmd.put("listItem", listItem);
    	cmd.put("listLembaga", listLembaga);
    	cmd.put("buka", buka);
    	cmd.put("namapsn", namapsn);
    	cmd.put("bang", this.uwManager.selectBankPusat());
    	return new ModelAndView( "bac/psn", cmd );
	}
    
    //request ArisEko - View Agen di Bac Sebelum Input
    
    public ModelAndView cek_struktur_view(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{

        Map<String, Object> map = new HashMap<String, Object>();
        String msag_id = ServletRequestUtils.getStringParameter(request, "msag_id", "");

        List<Map<String, Object>> strukturList = uwManager.selectStrukturAgen( msag_id );
        map.put( "strukturList", strukturList );

        return new ModelAndView( "bac/cek_struktur_view", map );
	}
    
	//View Data Dari Android or Gadget
    public ModelAndView viewDataFromGadget(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
    	Map cmd = new HashMap();
    	List dbpolis = new ArrayList();
    	dbpolis = bacManager.selectGadgetisHere();
    	cmd.put("dbpolis", dbpolis);cmd.put("open",1);

        return new ModelAndView( "bac/view_data_gadget", cmd );
	}
    
	public ModelAndView testRedirect(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", ServletRequestUtils.getStringParameter(request, "reg_spaj"));
		String mspoFlag = bacManager.selectMspoFLagSpaj(spaj);
		
		if ("2,3,4,5,6".indexOf(mspoFlag)>-1){
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/editspajnew.htm?data_baru=true&showSPAJ="+spaj));
		}else{
			return new ModelAndView(new RedirectView(request.getContextPath()+"/bac/edit.htm?data_baru=true&showSPAJ="+spaj));
		}
	}
	
	public ModelAndView view_attentionlist(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, ParseException{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Map cmd = new HashMap(); 
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String blacklist = ServletRequestUtils.getStringParameter(request, "blacklist");
		String tgl_lahir = ServletRequestUtils.getStringParameter(request, "tgl_lahir");
		String dateStr = df.format(new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", java.util.Locale.ENGLISH).parse(tgl_lahir));
		ArrayList blacklist2 = Common.serializableList(this.uwManager.selectBlacklist(blacklist, "mcl_first", df.parse(dateStr)));

		cmd.put("blacklist", blacklist2);
		cmd.put("open", 0);
        return new ModelAndView( "bac/view_data_gadget", cmd );
	}
	
	public ModelAndView promo_aktiv(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap params = new HashMap();	
		String result = null;		
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		
	    //untuk halaman report, Kriteria Report
        List<DropDown> jn_promo = new ArrayList<DropDown>(); 
        jn_promo.add(new DropDown("0", "Silahkan Pilih Promo"));
        jn_promo.add(new DropDown("1", "Buy One Get One Free"));
		
                
        String type = ServletRequestUtils.getStringParameter(request, "type", "");
		
		if(type.equals("promovalid")){
			int jen_promo = ServletRequestUtils.getIntParameter(request, "jen_promo",0);
			
			
			if(jen_promo == 1){
				result = " Promo Buy SIMAS PRIME LINK / SIMAS MAGNA LINK Get Free SMiLe MEDICAL AC PLAN E s/d PLAN O (BANCASS/TM). Valid from 01/03/2017 s/d 31/05/2017";
			}else{
				result = null;
			}
    		    		
    		response.setContentType("application/json");
    		PrintWriter out = response.getWriter();
    		Gson gson = new Gson();
    		out.print(gson.toJson(result));
    		out.close();
		}else if(type.equals("btnAktiv") || type.equals("btnCek") ){
			
				//String pesan ="";
					
					String jen_promo = ServletRequestUtils.getStringParameter(request, "jen_promo", "");
					String spajreff = ServletRequestUtils.getStringParameter(request, "spajreff", "");
					String sysdate = defaultDateFormat.format(new Date());
					
				if(jen_promo.equals("1")){						
					
						String beg_date_promo = "14/09/2016";
						String end_date_promo = "31/05/2017";	
						
						List CountandvalidSpajPromoKW7 = bacManager.selectCountandvalidSpajPromo(beg_date_promo,end_date_promo,"S161", jen_promo,3);
						List CountandvalidSpajPromoKW11 = bacManager.selectCountandvalidSpajPromo(beg_date_promo,end_date_promo,"S279", jen_promo,3);
						Integer total_freespaj7  = 0;
						String promo_valid7 = "";
						Integer FreeSpaj7 = 50; // maximum Free spaj
						Integer total_freespaj11  = 0;
						String promo_valid11 = "";
						Integer FreeSpaj11 = 50; // maximum Free spaj
						Integer APE = bacManager.selectHitungApe(spajreff); // cek nilai APE manual
						
						if(APE == null){
							APE = 0;
						}
						
							if(!CountandvalidSpajPromoKW7.isEmpty() && !CountandvalidSpajPromoKW11.isEmpty() ){
									Map kw7 = (HashMap) CountandvalidSpajPromoKW7.get(0);
									total_freespaj7 = ((BigDecimal)kw7.get("TOTAL_FREESPAJ")).intValue();
									promo_valid7 = (String)kw7.get("PROMO_VALID");
									
									Map kw11 = (HashMap) CountandvalidSpajPromoKW11.get(0);
									total_freespaj11 = ((BigDecimal)kw11.get("TOTAL_FREESPAJ")).intValue();
									promo_valid11 = (String)kw11.get("PROMO_VALID");
									
								if(promo_valid7.equalsIgnoreCase("Expired") || promo_valid11.equalsIgnoreCase("Expired") ){
										result = " Promo ini sudah Expired dengan batas waktu Promo 3 bulan terhitung dari tanggal " + beg_date_promo + " s/d " + end_date_promo;
								}else {
																		
									if(total_freespaj7 >= FreeSpaj7  ){
										result = " Batas Maximal untuk promo ini sudah melewati jumlah maximum (50 SPAJ per Kanwil 7). \n Kantor Wilayah 7 : " + (FreeSpaj7 - total_freespaj7) + " Free SPAJ \n Kantor Wilayah 11 :" + (FreeSpaj11 - total_freespaj11) + " Free SPAJ";
									}else if(total_freespaj11 >= FreeSpaj11  ){
										result = " Batas Maximal untuk promo ini sudah melewati jumlah maximum (50 SPAJ per Kanwil 11). \n Kantor Wilayah 7 : " + total_freespaj7 + " Free SPAJ \n Kantor Wilayah 11 : " + total_freespaj11 + " Free SPAJ";
									}else {
												Integer jn_spaj = 0; //0 SPAJ Reff / Utama ; 1 SPAJ Free
												List cekSpajUtamaPromo = bacManager.selectCekSpajPromoUF(beg_date_promo,end_date_promo,spajreff, jen_promo,jn_spaj);
												List cekSpajUtamaFree = bacManager.selectCekSpajPromoUF(beg_date_promo,end_date_promo,spaj, jen_promo,1);
												
												if(cekSpajUtamaFree.isEmpty()){
													result = "Promo tidak bisa di Aktifkan karena produk SPAJ yang di gratiskan bukan Produk SMiLe MEDICAL AC PLAN E s/d PLAN O (BANCASS/TM) R-300 / SPAJ bukan berasal dari nasabah KANWIL 7 SEMARANG dan KANWIL 11 PONTIANAK";
												}else if (APE < 15000000){
													result = " Nilai APE Untuk Produk SPAJ Referal tidak mencukupi untuk Promo ini.\n Minimal APE adalah Rp. 15.000.000,-. \n sedangkan nilai APE untuk\n produk Reff SPAJ : " + spajreff + " adalah : Rp. " + APE;
												}else if(!cekSpajUtamaPromo.isEmpty()){
													Map m = (HashMap) cekSpajUtamaPromo.get(0);
																										
													String wn = (String)m.get("WIL_NO");
													String produk = (String)m.get("LSDBS_NAME");
													String pp = (String)m.get("MCL_FIRST");
													String pptgl = m.get("MSPE_DATE_BIRTH").toString();
													String wilayah = (String)m.get("NAMA_KANWIL");
													String cabang = (String)m.get("NAMA_CABANG");
													String sts_promo = "Menunggu Aktivasi";
													//sts_promo : Menunggu Aktivasi --> Menungggu Approval --> Diterima / Ditolak
																				
													if(wn == null){
														wn = "";
													}
													if(produk == null){
														produk = "";
													}
													if(pp == null){
														pp = "";
													}
													if(pptgl == null){
														pptgl = "";
													}
													if(wilayah == null){
														wilayah = "";
													}
													if(cabang == null){
														cabang = "";
													}
													
													if(wn.equalsIgnoreCase("S161") || wn.equalsIgnoreCase("S279")){
														
														if (type.equals("btnAktiv") ){
															
																Integer row_num = 1;
																
																List cekSpajPromoV1 = bacManager.selectCekSpajPromo(  spajreff , spaj,  jen_promo); // cek spaj Utama dan Free sudah terdaftar atau belum di MST_FREE_SPAJ
																List cekSpajPromoV2 = bacManager.selectCekSpajPromo(  spajreff , null,  jen_promo); //cek spaj Utama sudah terdaftar atau belum di MST_FREE_SPAJ
																List cekSpajPromoV3 = bacManager.selectCekSpajPromo(  null , spaj,  jen_promo); // cek spaj free sudah terdaftar atau belum MST_FREE_SPAJ
																
																Map cekfree = (HashMap) cekSpajUtamaFree.get(0);
																Integer up = ((BigDecimal)cekfree.get("UP")).intValue();
																String tt = "Tidak Ada";
																List select_peserta =  bacManager.select_semua_mst_peserta2(spaj);
																
																if(!select_peserta.isEmpty()){
																	for(int j=0; j<select_peserta.size();j++){
																		Map y = (HashMap) select_peserta.get(j);
																		Integer lsbs_id = ((BigDecimal)y.get("LSBS_ID")).intValue();
																	
																		if(lsbs_id >=800){
																			tt = "Ada";
																		}
																	}
																}
																
																if(!cekSpajPromoV1.isEmpty()){//cek jika spaj Utama sudah terdaftar dengan spaj Free di MST_FREE_SPAJ																	
																	result = "Free SPAJ hanya bisa di daftarkan satu kali dan SPAJ "+ spaj+" telah terdaftar dengan no Reff SPAJ " + spajreff ;
																}else if(!cekSpajPromoV3.isEmpty()){//cek jika spaj Utama sudah terdaftar dengan spaj Free di MST_FREE_SPAJ
																	Map csp3 = (HashMap) cekSpajPromoV3.get(0);																	
																	spajreff = (String)csp3.get("REG_SPAJ_PRIMARY");
																	result = "Free SPAJ hanya bisa di daftarkan satu kali dan SPAJ "+ spaj+" telah terdaftar dengan no Reff SPAJ " + spajreff ;
																}else if(!cekSpajPromoV2.isEmpty()){//cek jika spaj Utama sudah terdaftar, tapi spaj Free masih kosong di MST_FREE_SPAJ --> update Spaj Free dan set process_type menjadi 0 (waiting acceptation)
																	try{
																		bacManager.updatePromoFreeSpaj(spajreff, spaj, 0);
																		elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), "Perubahan Referal SPAJ utama menjadi : "+ spajreff +"\n Program Free Smile Medical UP : "+ up + "\n Tertanggung Tambahan : "+tt, spaj, 0);
																		result = "Aktivasi untuk update Free SPAJ "+ spaj+" telah berhasil di proses";
																	} catch (Exception e) {
																		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
																		logger.error("ERROR :", e);
																		result = "Terjadi kesalahan dalam proses Aktivasi untuk memperbarui Free SPAJ "+ spaj+" ini, silahkan menghubungi IT";
																	}										
																}else{											
																	try{
																		
																		bacManager.insertPromoFreeSpaj(spajreff, row_num, spaj, 0, 1);
																		bacManager.updateflagSpecialOffer( spaj, 3);
																		elionsManager.insertMstPositionSpaj(currentUser.getLus_id(), " Program Free Smile Medical UP : "+ up + "\n Tertanggung Tambahan : "+tt + "\n Referal Spaj Utama : " + spajreff, spaj, 0);
																		
																		result = "Aktivasi untuk Free SPAJ "+ spaj+" telah berhasil di proses";
																	} catch (Exception e) {
																		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
																		logger.error("ERROR :", e);
																		result = "Terjadi kesalahan dalam proses Aktivasi untuk Free SPAJ "+ spaj+" ini, silahkan menghubungi IT";
																	}
																}															
															
															
														}else if(type.equals("btnCek") ){
															List cekSpajPromoV1 = bacManager.selectCekSpajPromo(  spajreff , spaj,  jen_promo);
															List cekSpajPromoV2 = bacManager.selectCekSpajPromo(  spajreff , null,  jen_promo);
															
															if(!cekSpajPromoV1.isEmpty()){	
																Map c1 = (HashMap) cekSpajPromoV1.get(0);										
																Integer pt = ((BigDecimal)c1.get("PROCESS_TYPE")).intValue();
																if (pt == 0){
																	sts_promo = "Menungggu Approval";
																}else if (pt == 1){
																	sts_promo = "Accepted (Get Free)";
																}else if (pt == 2){
																	sts_promo = "Rejected";
																}
															}
															
															Integer jumlahfree = 0;
//															if(total_freespaj7){
//																jumlahfree =  
//															}
															
															if(wn.equalsIgnoreCase("S161")){
																jumlahfree = (FreeSpaj7 - total_freespaj7);
															}else if ( wn.equalsIgnoreCase("S279")){
																jumlahfree = (FreeSpaj11 - total_freespaj11);
															}
															
															result = " Informasi No. SPAJ Utama "+ spajreff + "\n Produk : "+ produk + "\n APE : "+ APE + "\n Pemegang Polis : " + pp + "\n Tanggal Lahir Pemegang Polis : " + pptgl + "\n Kantor Wilayah : " + wilayah + " (Tersisa Free "+ jumlahfree + ")\n Kantor Cabang : " + cabang+ "\n Status Promo : " + sts_promo;
															
														}
														
													}else{
														result = "Promo tidak bisa di Aktifkan karena Reff SPAJ Utama bukan berasal dari nasabah KANWIL 7 SEMARANG dan KANWIL 11 PONTIANAK";
													}
													
												}else{
													result = " Referensi SPAJ Utama tidak terdaftar atau tidak bisa digunakan.\n Silahkan periksa kembali no Refferal SPAJ nya !";
												}
									}
								}
							}
					}else{
						result = "Tidak Ada Promo untuk Periode ini";
						
					}					
					
				
				
				response.setContentType("application/json");
	    		PrintWriter out = response.getWriter();
	    		Gson gson = new Gson();
	    		out.print(gson.toJson(result));
	    		out.close();
				
			}else{
				params.put("jn_promo", jn_promo);
				params.put("spaj",spaj);
				
				return new ModelAndView("bac/promo_aktivasi", params);
			}
		return null;
	}
	
	public ModelAndView profile_risk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap params = new HashMap();	
		Object result = null;
		String pesan = null;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String viewonly = ServletRequestUtils.getStringParameter(request, "viewonly", "0");
		String type = ServletRequestUtils.getStringParameter(request, "type", "");
		String listanswer = ServletRequestUtils.getStringParameter(request, "listanswer", "");
		String listanswer1[]= listanswer.split(";");
		int totalQuestionRP = 10; // untuk set jumlah pertanyaan pada questionare ini ada berapa
		int score = 0 ; 
		String tinkrisk ="", riskproduk="",profilrisk = "";
		
		 //cek apakah produk unitlink
        Integer lsgb_id = uwManager.selectIsPolisUnitlink(spaj);
        
        if(lsgb_id != 17){
        	params.put("pesanerror","Menu Profil Resiko Nasabah hanya bisa digunakan untuk Produk Unit Link");
        }
		
		//proses pengecekan data questionare sudah ada atau belum
		List dataQuestionareRP = bacManager.selectMst_Crp_Result(spaj);
		List dataQuestionareRPESPAJ = bacManager.selectMst_Crp_Result_ESPAJ(spaj);
		
		//jika Questionare sudah di input pada proses ESPAJ
		if(!dataQuestionareRPESPAJ.isEmpty()){
			if(dataQuestionareRP.isEmpty()){
				String proses = bacManager.transferDataMstCrpResultFromMstCrpTemp(spaj);
				if (proses.equals("ERROR"))
				params.put("pesanerror","Terjadi kesalahan dalam proses perpindahan data quisionare Risk Profile dari ESPAJ ke SPAJ "+ spaj+" ini, silahkan menghubungi IT");
    		
			}
		}
		
		dataQuestionareRP = bacManager.selectMst_Crp_Result(spaj);
		
		if(!dataQuestionareRP.isEmpty()){
			for(int j=0; j<dataQuestionareRP.size();j++){
				
				Map y = (HashMap) dataQuestionareRP.get(j);
				int quest = ((BigDecimal)y.get("QUESTION_ID")).intValue();
				
				String answerid = FormatString.rpad("0",y.get("ANSWER_ID").toString(), 3);
				Integer valueanswer = bacManager.retrieveAnswerIdtoValue(answerid);
				score = score + valueanswer ;
				params.put("ans"+quest , y.get("ANSWER_ID").toString());
				params.put("tans"+quest , valueanswer);			
			}
		}
		
		if(type.equals("save")){
				try{
					
					if(dataQuestionareRP.isEmpty()){// data quisioner masih kosong
						for(int n=0;n<listanswer1.length;n++){
							Integer idanswer = Integer.parseInt(listanswer1[n]);
							Integer idq = 0;
							
							if(n == 0){
								idq = 1;
							}else if (n == 1){
								idq = 7;
							}else if (n == 2){
								idq = 12;
							}else if (n == 3){
								idq = 18;
							}else if (n == 4){
								idq = 23;
							}else if (n == 5){
								idq = 28;
							}else if (n == 6){
								idq = 34;
							}else if (n == 7){
								idq = 40;
							}else if (n == 8){
								idq = 45;
							}else if (n == 9){
								idq = 51;
							}
														
							pesan = bacManager.insertMst_Crp_Result(spaj, idq  , idanswer);
								
						}
						
					}else{ // data quisioner diupdate
						for(int u=0;u<listanswer1.length;u++){
							Integer idanswer = Integer.parseInt(listanswer1[u]);
							Integer idq = 0;
							
							if(u == 0){
								idq = 1;
							}else if (u == 1){
								idq = 7;
							}else if (u == 2){
								idq = 12;
							}else if (u == 3){
								idq = 18;
							}else if (u == 4){
								idq = 23;
							}else if (u == 5){
								idq = 28;
							}else if (u == 6){
								idq = 34;
							}else if (u == 7){
								idq = 40;
							}else if (u == 8){
								idq = 45;
							}else if (u == 9){
								idq = 51;
							}
							pesan = bacManager.updateMst_Crp_Result(spaj, idq  , idanswer);							
							
						}				
					}
					
					List dataQuestionareRPInsertupdate = bacManager.selectMst_Crp_Result(spaj);
					score = 0;			
					
					if(!dataQuestionareRPInsertupdate.isEmpty()){
						for(int j=0; j<dataQuestionareRPInsertupdate.size();j++){
							
							Map y = (HashMap) dataQuestionareRPInsertupdate.get(j);
							int quest = ((BigDecimal)y.get("QUESTION_ID")).intValue();
							
							String answerid = FormatString.rpad("0",y.get("ANSWER_ID").toString(), 3);
							Integer valueanswer = bacManager.retrieveAnswerIdtoValue(answerid);
							score = score + valueanswer ;
							params.put("ans"+quest , y.get("ANSWER_ID").toString());
							params.put("tans"+quest , valueanswer);
						
						}
					}
					
					//Score detail Information setelah ada pembaharuan/ penambahan score
					Map info = bacManager.cekInfoScoreRiskProfile(score);
					params.putAll(info);
					
					//update data MSPO_CRP_SCORE dan MSPO_CRP_RESULT di mst_policy
					String infoDesc = (String) info.get("tinkrisk")+"-"+(String) info.get("riskproduk")+"-"+(String) info.get("profilrisk");
					bacManager.updateMst_policy_CRPResult(spaj,score,infoDesc);
					
					
				} catch (Exception e) {
					TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
					logger.error("ERROR :", e);
					pesan = "Terjadi kesalahan dalam proses penyimpanan data quisionare Risk Profile SPAJ "+ spaj+" ini, silahkan menghubungi IT";
				}
				
				params.put("pesan", pesan);		
				result = params;
	
				response.setContentType("application/json");
	    		PrintWriter out = response.getWriter();
	    		Gson gson = new Gson();
	    		out.print(gson.toJson(result));
	    		out.close();
		}	
		
		
		//Score detail Information berdasarkan data yang sudah tersimpan
		params.putAll(bacManager.cekInfoScoreRiskProfile(score));
		
	    params.put("spaj",spaj);
        params.put("viewonly",viewonly);
      
		return new ModelAndView("bac/questionareRisk_Profile", params);
	}
    
}
	