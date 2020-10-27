package com.ekalife.elions.web.viewer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.CmdCoverLetter;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;
import com.ibatis.common.resources.Resources;

public class CoverLetterFormController extends ParentFormController{
	
	//Fungsi ini untuk mengubah bentuk angka baik type data double, integer, dan date. Misal: hasil keluarnya 12.234AF menjadi 12.234.567
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected HashMap<String, Object> referenceData(HttpServletRequest request, Object cmd, Errors err) throws Exception {
		CmdCoverLetter command = (CmdCoverLetter) cmd;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("listbranch", elionsManager.selectDropDown("eka.lst_addr_region", "lar_id", "(lar_admin || ' - ' || lar_nama)", "", "lar_admin", null));
		map.put("dist", command.getJalur_dist());
		return map;
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		CmdCoverLetter command = new CmdCoverLetter();
		
		command.setLus_id(null);
		if(currentUser.getLde_id().equals("01")){
			command.setJenis_user("it");
		}else if(currentUser.getLde_id().equals("11")){
			command.setJenis_user("uw");
			command.setStatus_polis(0);
		}else if(currentUser.getLde_id().equals("13")){
			command.setJenis_user("ga");
			command.setStatus_polis(1);
		}else if(currentUser.getLde_id().equals("29")){
			command.setJenis_user("adm");
			command.setStatus_polis(3);
			command.setLus_id(currentUser.getLus_id());
		}
		
		if(command.getJenis_report() == null) command.setJenis_report(0);
		if(command.getJenis_report() == 0) command.setBranch(null);
		if(command.getTglcetak_awal() == null) command.setTglcetak_awal(uwManager.selectSysdateTruncated(-7));
		if(command.getTglcetak_akhir() == null) command.setTglcetak_akhir(uwManager.selectSysdateTruncated(0));
		if(command.getTglproses_awal() == null) command.setTglproses_awal(uwManager.selectSysdateTruncated(0));
		if(command.getTglproses_akhir() == null) command.setTglproses_akhir(uwManager.selectSysdateTruncated(0));
		if(command.getJalur_dist() == null) command.setJalur_dist(0);
		
		command.setSpajproses(null);
		command.setPesan(null);
		
		return command;
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		CmdCoverLetter command = (CmdCoverLetter) cmd;
		command.setSpajproses(null);
		command.setPesan(null);
		
		if(command.getJenis_report() == 1 && command.getBranch() == null) err.rejectValue("branch","","--> Silahkan pilih Branch Admin terlebih dahulu!");
		
		//Menampilkan list data yang akan diproses
		if(request.getParameter("btnShow") != null){
			command.setDatapolis(uwManager.selectPolisCoverLetter(defaultDateFormat.format(command.getTglcetak_awal()),
																  defaultDateFormat.format(command.getTglcetak_akhir()),
																  command.getStatus_polis(), command.getBranch(),
																  command.getLus_id(), command.getJalur_dist()));
			
			if(command.getDatapolis().size() == 0) err.rejectValue("pesan","","Data Polis yang dicetak pada tanggal tersebut tidak ada!");
		}
		
		else if(request.getParameter("btnSave") != null){
			if(command.getCek_polis() == null){
				err.rejectValue("pesan","","Pilih Polis yang akan diproses terlebih dahulu!");
			}else if(command.getCek_polis() != null && command.getStatus_polis() == 2 && command.getInput_resi() == null){
				err.rejectValue("pesan","","Silahkan masukkan No PO untuk Polis yang akan diproses!");
			}else{
				command.setDatapolis(uwManager.selectPolisCoverLetter(defaultDateFormat.format(command.getTglcetak_awal()),
						  defaultDateFormat.format(command.getTglcetak_akhir()),
						  command.getStatus_polis(), command.getBranch(),
						  command.getLus_id(), command.getJalur_dist()));
				
				for(int i=0;i<command.getCek_polis().length;i++){
					for(int j=0;j<command.getDatapolis().size();j++){
						if(command.cek_polis[i] == j){
							command.getDatapolis().get(j).setCek(1);
							if(command.getStatus_polis() == 2)
								command.getDatapolis().get(j).setNo_resi(command.input_resi[i]);
						}else{
							if(command.getDatapolis().get(j).getCek() == null)
								command.getDatapolis().get(j).setCek(0);
						}
					}
				}
				
				for(int i=0;i<command.getCek_simascard().length;i++){
					for(int j=0;j<command.getDatapolis().size();j++){
						if(command.cek_simascard[i] == j){
							command.getDatapolis().get(j).setSimascard("ADA");
						}
					}
				}
				
				for(int i=0;i<command.getCek_admedika().length;i++){
					for(int j=0;j<command.getDatapolis().size();j++){
						if(command.cek_admedika[i] == j){
							command.getDatapolis().get(j).setAdmedika("ADA");
						}
					}
				}
				
				String spajtemp = "";
				for(int i=0;i<command.getDatapolis().size();i++){
					if(command.getDatapolis().get(i).getCek() == 1){
						String validasi = uwManager.selectTglCoverLetter(command.getDatapolis().get(i).getNo_polis(), command.getStatus_polis().toString());
						if(!validasi.equals("kosong")) err.rejectValue("pesan","","Polis dengan No. " + command.getDatapolis().get(i).getNo_polis() + " sudah pernah diproses!");
						
						spajtemp = spajtemp + "'" + command.getDatapolis().get(i).getReg_spaj() + "'" + ",";
						
						if(command.getStatus_polis() == 0){
							String simascard = command.getDatapolis().get(i).getSimascard();
							String admedika = command.getDatapolis().get(i).getAdmedika();
							if(simascard.equals("ADA") && admedika.equals("ADA")){ 
								command.getDatapolis().get(i).setKeterangan("POLIS TELAH DIKIRIM KE GA (ADA SIMASCARD DAN ADA KARTU ADMEDIKA)");
							} else if(simascard.equals("ADA") && !admedika.equals("ADA")){
								command.getDatapolis().get(i).setKeterangan("POLIS TELAH DIKIRIM KE GA (ADA SIMASCARD DAN TIDAK ADA KARTU ADMEDIKA)");
							} else if(!simascard.equals("ADA") && admedika.equals("ADA")){
								command.getDatapolis().get(i).setKeterangan("POLIS TELAH DIKIRIM KE GA (TIDAK ADA SIMASCARD DAN ADA KARTU ADMEDIKA)");
							} else if(!simascard.equals("ADA") && !admedika.equals("ADA")){
								command.getDatapolis().get(i).setKeterangan("POLIS TELAH DIKIRIM KE GA (TIDAK ADA SIMASCARD DAN TIDAK ADA KARTU ADMEDIKA)");
							}
							command.getDatapolis().get(i).setTranshistory("tgl_kirim_polis");
							
						}else if(command.getStatus_polis() == 1){
							command.getDatapolis().get(i).setKeterangan("POLIS TELAH DITERIMA OLEH GA");
							command.getDatapolis().get(i).setTranshistory("tgl_terima_ga_from_uw");
							
						}else if(command.getStatus_polis() == 2){
							command.getDatapolis().get(i).setKeterangan("POLIS TELAH DIKIRIM KE ADMIN (NO. PO " + command.getDatapolis().get(i).getNo_resi() + ")");
							command.getDatapolis().get(i).setTranshistory("tgl_kirim_ga_to_admin");
							
						}else if(command.getStatus_polis() == 3){
							command.getDatapolis().get(i).setKeterangan("POLIS TELAH DITERIMA OLEH ADMIN");
							command.getDatapolis().get(i).setTranshistory("tgl_terima_admin_from_ga");
							
						}else if(command.getStatus_polis() == 4){
							command.getDatapolis().get(i).setKeterangan("POLIS TELAH DIKIRIM KE AGENT");
							command.getDatapolis().get(i).setTranshistory("tgl_kirim_admin_to_agent");
							
						}
					}
				}
				command.setSpajproses(spajtemp.substring(0,spajtemp.length()-1));
			}
		}
		
		else if(request.getParameter("btnXls") != null){
			command.setDatapolis(uwManager.selectPolisCoverLetter(defaultDateFormat.format(command.getTglcetak_awal()),
					  defaultDateFormat.format(command.getTglcetak_akhir()),
					  command.getStatus_polis(), command.getBranch(),
					  command.getLus_id(), command.getJalur_dist()));
			
			for(int i=0;i<command.getCek_polis().length;i++){
				for(int j=0;j<command.getDatapolis().size();j++){
					if(command.cek_polis[i] == j){
						command.getDatapolis().get(j).setCek(1);
						if(command.getStatus_polis() == 2)
							command.getDatapolis().get(j).setNo_resi(command.input_resi[i]);
					}else{
						if(command.getDatapolis().get(j).getCek() == null)
							command.getDatapolis().get(j).setCek(0);
					}
				}
			}
			
			String spajtemp = "";
			for(int i=0;i<command.getDatapolis().size();i++){
				if(command.getDatapolis().get(i).getCek() == 1){
					spajtemp = spajtemp + "'" + command.getDatapolis().get(i).getReg_spaj() + "'" + ",";
				}
			}
			command.setSpajproses(spajtemp.substring(0,spajtemp.length()-1));
		}
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		CmdCoverLetter command = (CmdCoverLetter) cmd;

		//Menampilkan Report Cover Letter pada tanggal proses tertentu
		if(request.getParameter("btnPrint") != null){
			ArrayList datareport = uwManager.selectReportCoverLetter(defaultDateFormat.format(command.getTglproses_awal()),
																  defaultDateFormat.format(command.getTglproses_akhir()),
																  command.getBranch(), command.getJalur_dist());
			if(datareport.size() > 0){
				try{
	    			String path = props.getProperty("report.cover_letter_all");
	    			if(command.getBranch() != null)
	    				path = props.getProperty("report.cover_letter");
    	    		File sourceFile = Resources.getResourceAsFile( path + ".jasper");
    	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

    	    		Map<String, Object> params = new HashMap<String, Object>();
    	    		if(command.getBranch() != null)
    	    			params.put("lar_id", command.getBranch());
    	    		params.put("tanggalAwal", command.getTglproses_awal());
    	    		params.put("tanggalAkhir", command.getTglproses_akhir());
    	    		params.put("userlogin", currentUser.getName());
    	    		
    	    		
    	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(datareport));
    		    	JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
    		    	
				} catch (Exception e) {
					logger.error("ERROR :", e);
				}
			}else{
				err.rejectValue("pesan","","Data Report pada tanggal proses tersebut tidak ada!");
    		}
			
		//Melakukan proses transfer Cover Letter
		}else if(request.getParameter("btnSave") != null){
			
			command.setPesan(bacManager.prosesCoverLetter(command, currentUser));
			
			command.setCek_polis(null);
			command.setCek_simascard(null);
			command.setCek_admedika(null);
			command.setInput_resi(null);
			
			command.setDatapolis(uwManager.selectPolisCoverLetter(defaultDateFormat.format(command.getTglcetak_awal()),
					  defaultDateFormat.format(command.getTglcetak_akhir()),
					  command.getStatus_polis(), command.getBranch(),
					  command.getLus_id(), command.getJalur_dist()));

			err.rejectValue("pesan", "", command.getPesan());
		
		
		}else if(request.getParameter("btnXls") != null){
			command.setCek_polis(null);
			command.setPesan("Cetak CoverLetter Dalam Excel!");
			err.rejectValue("pesan", "", "Cetak CoverLetter Dalam Excel!");
		}
		
		return new ModelAndView("uw/viewer/coverletter", err.getModel()).addAllObjects(this.referenceData(request,command,err));
	}
}
