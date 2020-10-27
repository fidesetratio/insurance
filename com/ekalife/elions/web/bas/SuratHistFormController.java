package com.ekalife.elions.web.bas;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.Surat_history;
import com.ekalife.elions.model.Upload;
import com.ekalife.elions.model.User;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.parent.ParentFormController;
import com.lowagie.text.pdf.PdfWriter;

import id.co.sinarmaslife.std.model.vo.DropDown;

public class SuratHistFormController extends ParentFormController {

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	
	
	@Override
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		Map<String, List> map = new HashMap<String, List>();
		
		map.put("lsAlasan", uwManager.getLstJnsAlasan());
		
		return map;
	}



	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		CommandControlSpaj ccs = new CommandControlSpaj();
		ccs.setDaftarSuratHist(new ArrayList<Surat_history>());
		ccs.getDaftarSuratHist().add(new Surat_history());
		
		ccs.setDaftarSuratHist2(new ArrayList<Surat_history>());
		ccs.getDaftarSuratHist2().add(new Surat_history());
		
		ccs.getDaftarSuratHist().get(0).setTanggalAwal(ServletRequestUtils.getStringParameter(request, "tglAwal",""));
		ccs.getDaftarSuratHist().get(0).setTanggalAkhir(ServletRequestUtils.getStringParameter(request, "tglAkhir",""));
		ccs.getDaftarSuratHist().get(0).setJenis(ServletRequestUtils.getIntParameter(request, "jenis",0));
		
		ccs.getDaftarSuratHist2().get(0).setTanggalAwal(ServletRequestUtils.getStringParameter(request, "tglAwal",""));
		ccs.getDaftarSuratHist2().get(0).setTanggalAkhir(ServletRequestUtils.getStringParameter(request, "tglAkhir",""));
		ccs.getDaftarSuratHist2().get(0).setJenis(ServletRequestUtils.getIntParameter(request, "jenis",0));
		
		if(!ccs.getDaftarSuratHist().get(0).getTanggalAwal().equals("") && !ccs.getDaftarSuratHist().get(0).getTanggalAkhir().equals("")) {
			Integer allAccess = 0;
			if(currentUser.getLde_id().equals("29") || currentUser.getLde_id().equals("01")) allAccess = 1;
			ccs.setDaftarSuratHist(uwManager.selectSuratHistByDate(ccs.getDaftarSuratHist().get(0).getTanggalAwal(),ccs.getDaftarSuratHist().get(0).getTanggalAkhir(),allAccess,currentUser.getLus_id(), ccs.getDaftarSuratHist().get(0).getJenis(),"",""));
			ccs.setDaftarSuratHist2(uwManager.selectSuratHistByDate(ccs.getDaftarSuratHist2().get(0).getTanggalAwal(),ccs.getDaftarSuratHist2().get(0).getTanggalAkhir(),allAccess,currentUser.getLus_id(), ccs.getDaftarSuratHist2().get(0).getJenis(),"",""));
			ccs.getDaftarSuratHist().get(0).setTanggalAwal(ServletRequestUtils.getStringParameter(request, "tglAwal",""));
			ccs.getDaftarSuratHist().get(0).setTanggalAkhir(ServletRequestUtils.getStringParameter(request, "tglAkhir",""));
			ccs.getDaftarSuratHist().get(0).setSuccessMsg(ServletRequestUtils.getStringParameter(request, "success",""));
		
			//load attachment
			String directory = props.getProperty("pdf.dir.upload.kpl.promo")+"\\KPL\\"+ccs.getDaftarSuratHist().get(0).getReg_spaj()+"\\";
			List<DropDown> dokumen = FileUtils.listFilesInDirectory(directory);
			List<Map> dok = new ArrayList<Map>();
			
			if(!dokumen.isEmpty()){
				for(int i=0;i<dokumen.size();i++){
					DropDown dc = dokumen.get(i);
					Map m = new HashMap();
					m.put("dok", dc.getKey());
					dok.add(m);
				}
			}
			ccs.getDaftarSuratHist().get(0).setLs_attachment(dok);
		}
		ccs.getDaftarSuratHist().get(0).setLus_id(currentUser.getLus_id());
		ccs.getDaftarSuratHist().get(0).setMode(ccs.getDaftarSuratHist().get(0).getJenis());
		
		return ccs;
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if(submitMode.equals("btnCari") || submitMode.equals("btnReport") || submitMode.equals("btnShow") || submitMode.equals("btnShowByPolis")) {
			return true;
		}
		return false;
	}

	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object cmd) throws Exception {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		String polis = ServletRequestUtils.getStringParameter(request, "polis", "");
		String polis2 = ServletRequestUtils.getStringParameter(request, "polis2", "");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		CommandControlSpaj ccs = (CommandControlSpaj) cmd;
		//ccs.getDaftarSuratHist().add(0,new Surat_history());
		
		ccs.setPesan("");
		String begDate = ccs.getDaftarSuratHist().get(0).getTanggalAwal();
		String endDate = ccs.getDaftarSuratHist().get(0).getTanggalAkhir();
		
		Integer mode = ccs.getDaftarSuratHist().get(0).getJenis();
		
		if(!ccs.getDaftarSuratHist().get(0).getTanggalAwal().equals("") && !ccs.getDaftarSuratHist().get(0).getTanggalAkhir().equals("") && ccs.getDaftarSuratHist().get(0).getJenis()!=null) {
		//if(!begDate.equals("") && !endDate.equals("")) {
			Integer allAccess = 0;
			if(currentUser.getLde_id().equals("29") || currentUser.getLde_id().equals("01")) allAccess = 1;
			Surat_history sh = ccs.getDaftarSuratHist().get(0);
			
			if(ccs.getDaftarSuratHist().get(0).getJenis()!=2){
				polis="";
				polis2="";
			}
			
			ccs.setDaftarSuratHist2(uwManager.selectSuratHistByDate(ccs.getDaftarSuratHist().get(0).getTanggalAwal(),ccs.getDaftarSuratHist().get(0).getTanggalAkhir(),allAccess,currentUser.getLus_id(), ccs.getDaftarSuratHist().get(0).getJenis(), "", polis2));
			ccs.setDaftarSuratHist(uwManager.selectSuratHistByDate(ccs.getDaftarSuratHist().get(0).getTanggalAwal(),ccs.getDaftarSuratHist().get(0).getTanggalAkhir(),allAccess,currentUser.getLus_id(), ccs.getDaftarSuratHist().get(0).getJenis(), polis, polis2));
			
			List<Surat_history> surat_h = new ArrayList<Surat_history>();
			surat_h.add(new Surat_history());
			
			if(ccs.getDaftarSuratHist().isEmpty()){
				ccs.setDaftarSuratHist(surat_h);
				ccs.setPesan("Data tidak ada.");
			}
			ccs.getDaftarSuratHist().get(0).setMode(mode);
			if(ccs.getDaftarSuratHist().size() != 0) {
				ccs.getDaftarSuratHist().get(0).setTanggalAwal(begDate);
				ccs.getDaftarSuratHist().get(0).setTanggalAkhir(endDate);
				ccs.getDaftarSuratHist().get(0).setJenis(sh.getJenis());
			}
			else {
				ccs.setDaftarSuratHist(new ArrayList<Surat_history>());
				ccs.getDaftarSuratHist().add(new Surat_history());
				ccs.getDaftarSuratHist().get(0).setTanggalAwal(begDate);
				ccs.getDaftarSuratHist().get(0).setTanggalAkhir(endDate);
			}
			
			//load attachment
			String directory = props.getProperty("pdf.dir.upload.kpl.promo")+"\\KPL\\"+ccs.getDaftarSuratHist().get(0).getReg_spaj()+"\\";
			List<DropDown> dokumen = FileUtils.listFilesInDirectory(directory);
			List<Map> dok = new ArrayList<Map>();
			
			if(!dokumen.isEmpty()){
				for(int i=0;i<dokumen.size();i++){
					DropDown dc = dokumen.get(i);
					Map m = new HashMap();
					m.put("dok", dc.getKey());
					dok.add(m);
				}
			}
			ccs.getDaftarSuratHist().get(0).setLs_attachment(dok);
			
			if(submitMode.equals("btnReport")) {
				CommandControlSpaj ccs2 = (CommandControlSpaj) cmd;
				ccs2.setDaftarSuratHist(uwManager.selectSuratHistByDate(ccs.getDaftarSuratHist().get(0).getTanggalAwal(),ccs.getDaftarSuratHist().get(0).getTanggalAkhir(),allAccess,currentUser.getLus_id(), ccs.getDaftarSuratHist().get(0).getJenis(), "", polis2));
				Map<String, Comparable> params = new HashMap<String, Comparable>();
				String outputDir = props.getProperty("pdf.dir.report") + "\\surat_history\\";
				String outputFilename = "surat_history_"+currentUser.getLus_id()+".pdf";		
				params.put("begDate", begDate);
				params.put("endDate", endDate);
				params.put("cabang", currentUser.getCabang());
				JasperUtils.exportReportToPdf(props.getProperty("report.bas.surat_history") + ".jasper", outputDir, outputFilename, params, ccs2.getDaftarSuratHist(), PdfWriter.AllowPrinting, null, null);
				FileInputStream in = null;
				ServletOutputStream ouputStream = null;
				try {
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "Attachment;filename=surat_history_"+currentUser.getLus_id()+".pdf");
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					
					in = new FileInputStream(outputDir+"surat_history_"+currentUser.getLus_id()+".pdf");
					ouputStream = response.getOutputStream();
					
					IOUtils.copy(in, ouputStream);
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
		}
		else ccs.getDaftarSuratHist().get(0).setMssg("Masukkan tanggal kirim dan Pilih salah satu Jenis terlebih dahulu"); 
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,	HttpServletResponse response, Object cmd, BindException err) throws Exception {
		CommandControlSpaj ccs = (CommandControlSpaj) cmd;
		Map map = new HashMap();
		List<File> attachments = new ArrayList<File>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if(submitMode.equals("btnSimpan")) {
			Integer size = 1;
			Upload upload = new Upload();
			if(ccs.getDaftarSuratHist().get(0).getJenis()==2){
				ServletRequestDataBinder binders = new ServletRequestDataBinder(upload);
				binders.bind(request);
			}else{
				size = ccs.getDaftarSuratHist().size();
			}
			for(int a=0;a<size;a++) {
				ccs.getDaftarSuratHist().get(a).setMsth_attach(upload.getFile1());
				
				if(!ccs.getDaftarSuratHist().get(a).getKet_fu1().equals("")) {
					if(ccs.getDaftarSuratHist().get(a).getUser_fu1()==null)ccs.getDaftarSuratHist().get(a).setUser_fu1(Integer.parseInt(currentUser.getLus_id()));
					if(ccs.getDaftarSuratHist().get(a).getTgl_fu1()==null)ccs.getDaftarSuratHist().get(a).setTgl_fu1(elionsManager.selectSysdate());
				}
				if(!ccs.getDaftarSuratHist().get(a).getKet_fu2().equals("")) {
					if(ccs.getDaftarSuratHist().get(a).getUser_fu2()==null)ccs.getDaftarSuratHist().get(a).setUser_fu2(Integer.parseInt(currentUser.getLus_id()));
					if(ccs.getDaftarSuratHist().get(a).getTgl_fu2()==null)ccs.getDaftarSuratHist().get(a).setTgl_fu2(elionsManager.selectSysdate());
				}
				if(!ccs.getDaftarSuratHist().get(a).getKet_fu3().equals("")) {
					if(ccs.getDaftarSuratHist().get(a).getUser_fu3()==null)ccs.getDaftarSuratHist().get(a).setUser_fu3(Integer.parseInt(currentUser.getLus_id()));
					if(ccs.getDaftarSuratHist().get(a).getTgl_fu3()==null)ccs.getDaftarSuratHist().get(a).setTgl_fu3(elionsManager.selectSysdate());
				}
				
				//upload attachment
				if(ccs.getDaftarSuratHist().get(a).getMsth_attach()!=null){
					if(ccs.getDaftarSuratHist().get(a).getMsth_attach().isEmpty()==false){
						//destiny upload file
						String tDest = props.getProperty("pdf.dir.upload.kpl.promo")+"\\KPL\\"+ccs.getDaftarSuratHist().get(a).getReg_spaj()+"\\";
						File destDir = new File(tDest);
						
						if(!destDir.exists()) destDir.mkdirs();
						
						String filename = ccs.getDaftarSuratHist().get(a).getMsth_attach().getOriginalFilename();
						String dest = tDest +"\\"+filename;
						File outputFile = new File(dest);
						FileCopyUtils.copy(ccs.getDaftarSuratHist().get(a).getMsth_attach().getBytes(), outputFile);
						
						attachments.add(outputFile);
					}
				}
			}
			
			//send mail
			if(ccs.getDaftarSuratHist().get(0).getJenis()==2){
				StringBuffer mssg = new StringBuffer();
				//String[] emailTo = new String[]{"canpri@sinarmasmsiglife.co.id"};//tes
				String[] emailTo = new String[]{"Jajat@sinarmasmsiglife.co.id","angga@sinarmasmsiglife.co.id","masliha@sinarmasmsiglife.co.id","riana@sinarmasmsiglife.co.id","Edmond@sinarmasmsiglife.co.id","Lilyana@sinarmasmsiglife.co.id"};
				mssg.append("No Polis\t : "+ccs.getDaftarSuratHist().get(0).getMspo_policy_no());
				mssg.append("\nNama Pemegang\t : "+ccs.getDaftarSuratHist().get(0).getNm_pp());
				mssg.append("\nCabang\t\t : "+ccs.getDaftarSuratHist().get(0).getLca_nama());
				if(!ccs.getDaftarSuratHist().get(0).getKet_fu3().equals("")){
					mssg.append("\nKeterangan\t : "+ccs.getDaftarSuratHist().get(0).getKet2_fu3());
				}else if(!ccs.getDaftarSuratHist().get(0).getKet_fu2().equals("")){
					mssg.append("\nKeterangan\t : "+ccs.getDaftarSuratHist().get(0).getKet2_fu2());
				}else{
					mssg.append("\nKeterangan\t : "+ccs.getDaftarSuratHist().get(0).getKet2_fu1());
				}
				
//				email.send(true, props.getProperty("admin.ajsjava"), emailTo, null, null, 
//						"[E-Lions] Follow Up Kwitansi Premi Lanjutan", mssg.toString(), attachments);
				
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, null, 0, 0, elionsManager.selectSysdate(), null, true, 
						props.getProperty("admin.ajsjava"), emailTo, null, null, 
						"[E-Lions] Follow Up Kwitansi Premi Lanjutan", mssg.toString(), attachments, null);
			}
			//logger.info("ok");
			map.put("success", uwManager.updateSuratHist(ccs.getDaftarSuratHist()));
			//map.put("success", "Surat History berhasil disimpan");
			map.put("tglAwal", ccs.getDaftarSuratHist().get(0).getTanggalAwal());
			map.put("tglAkhir", ccs.getDaftarSuratHist().get(0).getTanggalAkhir());
			map.put("jenis", ccs.getDaftarSuratHist().get(0).getJenis());
		}
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/surat_history.htm")).addAllObjects(map);
	}

	
}
