package com.ekalife.elions.web.bas;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.KartuNama;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * Controller untuk form permintaan kartu nama
 * http://localhost/E-Lions/bas/kartu_nama.htm
 * 
 * @author Canpri
 * @since Des 4, 2013 (9:23:47 AM)
 */
public class PermintaanKartuNamaFormController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );
	private Map daftarWarna;
	private Map daftarWarnaAgen;
	private List<Map> daftarJenisBrosur;
	private List<Map> daftarJenisSpaj;
	
	public void setDaftarJenisSpaj(List<Map> daftarJenisSpaj) {
		this.daftarJenisSpaj = daftarJenisSpaj;
	}

	public void setDaftarJenisBrosur(List<Map> daftarJenisBrosur) {
		this.daftarJenisBrosur = daftarJenisBrosur;
	}

	public void setDaftarWarnaAgen(Map daftarWarnaAgen) {
		this.daftarWarnaAgen = daftarWarnaAgen;
	}

	public void setDaftarWarna(Map daftarWarna) {
		this.daftarWarna = daftarWarna;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		KartuNama cmd = new KartuNama();
		
		Upload upload = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		
		Integer posisi = ServletRequestUtils.getIntParameter(request, "posisi", 0);
		
		cmd.setMkn_position(posisi);
		cmd.setMkn_lus_id(currentUser.getLus_id());
		if(cmd.getMode()==null)cmd.setMode(posisi);
		
		cmd.setDaftarNoKartuNama(bacManager.selectDaftarNoKartuNama(cmd));
		
		return cmd;
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if("show".equals(submitMode) || "new".equals(submitMode)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command, BindException err) throws Exception {
		KartuNama cmd = (KartuNama) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		HttpSession session = request.getSession();
		
		if("show".equals(cmd.getSubmitMode())) {
			cmd.setDaftarKartuNama(bacManager.selectDaftarKartuNama(cmd));
			cmd.setHistoryKartuNama(bacManager.selectHistoryKartuNama(cmd));
		}else if("new".equals(cmd.getSubmitMode())) {
			cmd.setMkn_position(0);
			session.removeAttribute("resultDaftarAgent");
		}
		
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	// Ryan - Pembatasan Permintaan Brosur , Max 50 per Jenisnya -> req Martino (BAS)
	// tambahan req desy(BAS)-> validasi untuk EV dan MV, tidak boleh >100
	protected void onBind(HttpServletRequest request, Object command, BindException err) throws Exception {
		KartuNama cmd = (KartuNama) command;
		HttpSession session = request.getSession();
		
		Upload upload = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		
		if(cmd.getSubmitMode().equals("upload_agent")){
			if(!upload.getFile1().isEmpty()){
				logger.info(upload.getFile1().getContentType());
				if(!upload.getFile1().getContentType().contains("excel") && !upload.getFile1().getContentType().contains("octet")){
					if(session.getAttribute("resultDaftarAgent")==null)err.reject("","Harap upload file xls untuk daftar agent");
				}
				else{
					HSSFWorkbook wb = new HSSFWorkbook(upload.getFile1().getInputStream());
					HSSFSheet sheet = wb.getSheetAt(0);
					
					boolean stop = false;
					int baris = 6; //dimulai dari baris 1
					List<Map> result = new ArrayList<Map>();
					
					//LOOPING ROW
					do {
						HSSFRow row = sheet.getRow(baris);
						baris++;
						logger.info(baris);
						if(baris <= sheet.getLastRowNum()){
							if(row != null && !row.getCell((short) 1).getStringCellValue().equals("")) {
								HSSFCell msag_id = row.getCell((short) 1); //kolom B
								HSSFCell nama = row.getCell((short) 2); //kolom C
								HSSFCell telp = row.getCell((short) 4); //kolom E
								
								if((msag_id.getCellType() != HSSFCell.CELL_TYPE_STRING && msag_id.getCellType() != HSSFCell.CELL_TYPE_BLANK) || 
										(nama.getCellType() != HSSFCell.CELL_TYPE_STRING && nama.getCellType() != HSSFCell.CELL_TYPE_BLANK)) {
									//errorMessages.add("Harap Cek Baris ke " + (baris) + " (" + fund.getStringCellValue() + ")");
									continue;
								}else {
									if(!msag_id.getStringCellValue().trim().equals("")) {
										Map agent = new HashMap();
										agent.put("MSAG_ID", msag_id.getStringCellValue());
										agent.put("NAMA", nama.getStringCellValue());
										agent.put("TELP", telp.getStringCellValue());
										result.add(agent);
									}
								}
							}else {
								stop = true;
							}
						}else{
							stop = true;
						}
						
					}while(!stop);
					
					if(!err.hasErrors()) { 
						session.setAttribute("resultDaftarAgent", result);
					}else{
						err.reject("","Error!");
					}
				}
			}
		}
		
		if(cmd.getSubmitMode().equals("save")){
			if(upload.getFile2().isEmpty())err.reject("","Harap upload bukti approval atasan!");
			else if(session.getAttribute("resultDaftarAgent")==null)err.reject("","Harap upload data Agent!");
			else if(!upload.getFile2().isEmpty()){
				if(!upload.getFile2().getContentType().contains("pdf"))err.reject("","Harap upload bukti approval atasan dalam bentuk pdf!");
			}
		}
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		KartuNama cmd = (KartuNama) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		HttpSession session = request.getSession();
		String sukses = "";
		
		Upload upload = new Upload();
		ServletRequestDataBinder binder = new ServletRequestDataBinder(upload);
		binder.bind(request);
		
		/*sukses = uwManager.processFormBrosur(cmd, currentUser);
		cmd.setSubmitMode("show");*/
		
		if(cmd.getSubmitMode().equals("save")){
			
			sukses = bacManager.processKartuNama(cmd, currentUser, session);
			
			String dest = props.getProperty("pdf.dir.formkartunama") +"\\"+cmd.getMkn_document();
			File outputFile = new File(dest);
			FileCopyUtils.copy(upload.getFile2().getBytes(), outputFile);
		}
		
		if(cmd.getSubmitMode().equals("approve_ds") || cmd.getSubmitMode().equals("reject") || cmd.getSubmitMode().equals("approve_bd")
				|| cmd.getSubmitMode().equals("send_purchasing") || cmd.getSubmitMode().equals("send_ga")){
			sukses = bacManager.processKartuNama(cmd, currentUser, session);
		}
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/kartu_nama.htm?posisi="+cmd.getMode())).addObject("sukses", sukses);
	}
	
}