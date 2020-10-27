package com.ekalife.elions.web.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.MergePDF;
import com.ekalife.utils.PrintPolisPerjanjianAgent;
import com.ekalife.utils.StringUtil;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentJasperReportingController;
import com.ibatis.common.resources.Resources;

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
 * Controller untuk pencetakan kontrak marketing untuk agen
 * 
 * @author Yusuf
 * @since Apr 15, 2008 (8:12:26 AM)
 */
public class AgencyController extends ParentJasperReportingController{
	
	private Map initDistribusi(String distribusi){
		Map map = new HashMap();
		if(distribusi.equals("AGENCY") || distribusi.equals("NEW AGENCY") ){
			List<DropDown> lstLevel = elionsManager.selectLstLevelAgency();
			
			
			/*List<DropDown> lstLevely = new ArrayList();
			lstLevelx.add(new DropDown("0", ""));
			lstLevelx.add(new DropDown("1", "AGENCY DIRECTOR (Inkubator)"));
			lstLevelx.add(new DropDown("2", "AGENCY DIRECTOR (Kantor Mandiri)"));
			lstLevelx.add(new DropDown("3", "AGENCY MANAGER"));
			lstLevelx.add(new DropDown("4", "SALES MANAGER"));
			lstLevelx.add(new DropDown("5", "SALES EXECUTIVE"));
			lstLevely.add(new DropDown("0", ""));
			lstLevely.add(new DropDown("1", "BUSINESS PARTNER *** (INKUBATOR)"));
			lstLevely.add(new DropDown("2", "BUSINESS PARTNER *** (KANTOR MANDIRI)"));
			lstLevely.add(new DropDown("3", "BUSINESS PARTNER **"));
			lstLevely.add(new DropDown("4", "BUSINESS PARTNER *"));
			lstLevely.add(new DropDown("5", "FINANCIAL CONSULTANT"));*/
			
			String indexCabangAgency = "37";
			String[] a = indexCabangAgency.split(",");
			
			a = indexCabangAgency.split(",");
			
			List lstAgen = elionsManager.selectLstAgenAgency(indexCabangAgency);
			map.put("distribusi", distribusi);
			map.put("reg","Agency");
			map.put("lstLevel", lstLevel);
			map.put("jenisLeader", "Atasan Pusat");
			map.put("atasanCab", "Atasan Cabang");
			map.put("lstAgen", lstAgen);
		}else if(distribusi.equals("HYBRID(ARTHAMAS)")  ){
			List<DropDown> lstLevel = elionsManager.selectLstLevelHybrid();
			List lstAgen = elionsManager.selectLstHybridArthaMas("00");
			map.put("distribusi", distribusi);
			map.put("reg","Hybrid");
			map.put("lstLevel", lstLevel);
			map.put("jenisLeader", "Perwakilan Perusahaan");
			map.put("atasanCab", "Atasan Cabang");
			map.put("lstAgen", lstAgen);
		//code lca_id untuk hybridAJS dicari tau lagi berapa
		}else if(distribusi.equals("HYBRID(AJS)")){//Req Tri Handini(Info ke IT tgl 4 Januari 2010) : hybrid arthamas digabung dengan hybrid AJS. diubah di selectnya dengan join dua select 
			List<DropDown> lstLevel = elionsManager.selectLstLevelHybrid();
			List lstAgen = elionsManager.selectLstHybridAJS("01");
			map.put("distribusi", distribusi);
			map.put("reg","Hybrid");
			map.put("lstLevel", lstLevel);
			map.put("jenisLeader", "Perwakilan Perusahaan");
			map.put("atasanCab", "Atasan Cabang");
			map.put("lstAgen", lstAgen);
		}else if(distribusi.equals("REGIONAL")){
			List<DropDown> lstLevel = elionsManager.selectLstLevelRegional();
			String indexCabang = "37,46,40,52,55,58,60";
			List lstAgen = elionsManager.selectLstAgenRegional(indexCabang);
			map.put("distribusi", distribusi);
			map.put("reg","Regional");
			map.put("lstLevel", lstLevel);
			map.put("jenisLeader", "Atasan Pusat");
			map.put("atasanCab", "Atasan Cabang");
			map.put("lstAgen", lstAgen);
//		}else if(distribusi.equals("AGENCY ARTHAMAS")){//agency arthamas dihilangkan atas permintaan wasisti(75656)
//			List<DropDown> lstLevel = elionsManager.selectLstLevelAgency();
//			List lstAgen = elionsManager.selectLstAgencyArthamas("2");
//			map.put("distribusi", distribusi);
//			map.put("reg","Agency");
//			map.put("lstLevel", lstLevel);
//			map.put("jenisLeader", "Atasan Pusat");
//			map.put("atasanCab", "Atasan Cabang");
//			map.put("lstAgen", lstAgen);
		}else if(distribusi.equals("BRIDGE AGENCY")){
			List<DropDown> lstLevel = elionsManager.selectLstLevelAgency();
			List lstAgen = elionsManager.selectLstAgen("60");
			map.put("distribusi", distribusi);
			map.put("reg","Agency");
			map.put("lstLevel", lstLevel);
			map.put("jenisLeader", "Atasan Pusat");
			map.put("atasanCab", "Atasan Cabang");
			map.put("lstAgen", lstAgen);
		}else if(distribusi.equals("BUSINESS PARTNER")){
			List<DropDown> lstLevel = elionsManager.selectLstLevelBP();
			List lstAgen = elionsManager.selectLstAgenBP("61");
			map.put("distribusi", distribusi);
			map.put("reg","Cabang RO/CRO");
			map.put("lstLevel", lstLevel);
			map.put("jenisLeader", "Atasan Pusat");
			map.put("atasanCab", "Atasan Cabang");
			map.put("lstAgen", lstAgen);
		}else if(distribusi.equals("REGIONAL")){
			List<DropDown> lstLevel = elionsManager.selectLstLevelRegional();
			String indexCabang = "37,46,40,52,55,58,60";
			List lstAgen = elionsManager.selectLstAgenRegional(indexCabang);
			map.put("distribusi", distribusi);
			map.put("reg","Regional");
			map.put("lstLevel", lstLevel);
			map.put("jenisLeader", "Atasan Pusat");
			map.put("atasanCab", "Atasan Cabang");
			map.put("lstAgen", lstAgen);
		} else if(distribusi.equals("AKM WORKSITE")){

				List<DropDown> lstLevel = new ArrayList();
				lstLevel.add(new DropDown("0", ""));
				lstLevel.add(new DropDown("1", "AD"));
				lstLevel.add(new DropDown("2", "AM-BM"));
				lstLevel.add(new DropDown("3", "SM"));
				lstLevel.add(new DropDown("4", "SE"));
				
			String indexCabangAgency = "67";
			String[] a = indexCabangAgency.split(",");
			
			a = indexCabangAgency.split(",");
			
			List lstAgen = bacManager.selectLstAgenAKM(indexCabangAgency);
			map.put("distribusi", distribusi);
			map.put("reg","Agency");
			map.put("lstLevel", lstLevel);
			map.put("jenisLeader", "Atasan Pusat");
			map.put("atasanCab", "Atasan Cabang");
			map.put("lstAgen", lstAgen);
		}
		return map;
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String filename = "";
		String fileString = "";
		
		Boolean scFile=false;
		PrintPolisPerjanjianAgent printPolis = new PrintPolisPerjanjianAgent();
		String cekagen = ServletRequestUtils.getStringParameter(request, "cekagen", "");
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> err = new ArrayList<String>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		String cari = ServletRequestUtils.getStringParameter(request, "cari", "");
		request.setAttribute("cari", cari);
		String tampil = ServletRequestUtils.getStringParameter(request, "tampil", "");
		String tanggallahir = request.getParameter("birthdate");
		
		
		//untuk menentukan nama agency/regional/nama perusahaan
		if(cekagen.equals("1")){
			map.put("redirect","tampilkan form");
			map.put("agency", "");
			String distribusi = ServletRequestUtils.getStringParameter(request, "distribusi", "");
			
			map.putAll(initDistribusi(distribusi));
			//content.put("MCL_FIRST", request.getParameter("namaAgent").toUpperCase());
			
			//auto fill data berdasarkan no reg agen
			String no_reg = ServletRequestUtils.getStringParameter(request, "no_reg", null);
			if(no_reg!=null){
				List data_no_reg = uwManager.selectKuesionerByNoReg(no_reg);
				if(!data_no_reg.isEmpty()){
					Map m = (HashMap) data_no_reg.get(0);
					String tgl_kontrak = "";
					
					String tgl_lahir = defaultDateFormat.format((Date)m.get("TGL_LAHIR"));
					if(m.get("TGL_KONTRAK")!=null)tgl_kontrak = defaultDateFormat.format((Date)m.get("TGL_KONTRAK"));
					
					map.put("no_reg", no_reg);
					map.put("reg_alamat", (String)m.get("ALAMAT"));
					map.put("reg_tgl_lahir", tgl_lahir);
					map.put("reg_kota", (String)m.get("KOTA"));
					map.put("reg_tgl_kontrak", tgl_kontrak);
					map.put("reg_ktp", (String)m.get("KTP"));
					map.put("reg_regional", (String)m.get("REGIONAL"));
					map.put("reg_nama_atasan", (String)m.get("MCL_FIRST"));
					map.put("reg_tempat_lahir", (String)m.get("TEMPAT_LAHIR"));
					//map.put("reg_distribusi", (String)m.get("DISTRIBUSI"));
					map.put("reg_nama", (String)m.get("NAMA"));
					map.put("reg_kd_atasan", (String)m.get("KD_ATASAN"));
					
					//map.put("redirect","tampilkan form");
					map.put("data", data_no_reg);
				}
			}
			
		}
		
		if(cekagen.equals("2")){
			map.put("redirect","tampilkan form");
			String distribusi = ServletRequestUtils.getStringParameter(request, "distribusi", "");
			String nama = ServletRequestUtils.getStringParameter(request, "nama", "");
			String msag_id=null, agency = null;
			
			if(! nama.equals("")){
				int pos=nama.indexOf('~');
				msag_id=nama.substring(0,pos);
				agency=nama.substring(pos+1,nama.length());
				//String nama_leader = elionsManager.selectNamaLeader(msag_id);
				//map.put("vp", nama_leader);
				map.put("vp","");
				map.put("distribusi", distribusi);
				map.put("agency", nama);
			}
			
			map.putAll(initDistribusi(distribusi));
			
			//auto fill data berdasarkan no reg agen
			String no_reg = ServletRequestUtils.getStringParameter(request, "no_reg", null);
			if(no_reg!=null){
				List data_no_reg = uwManager.selectKuesionerByNoReg(no_reg);
				if(!data_no_reg.isEmpty()){
					Map m = (HashMap) data_no_reg.get(0);
					
					String tgl_lahir = defaultDateFormat.format((Date)m.get("TGL_LAHIR"));
					String tgl_kontrak = defaultDateFormat.format((Date)m.get("TGL_KONTRAK"));
					
					map.put("no_reg", no_reg);
					map.put("reg_alamat", (String)m.get("ALAMAT"));
					map.put("reg_tgl_lahir", tgl_lahir);
					map.put("reg_kota", (String)m.get("KOTA"));
					map.put("reg_tgl_kontrak", tgl_kontrak);
					map.put("reg_ktp", (String)m.get("KTP"));
					//map.put("reg_regional", (String)m.get("REGIONAL"));
					map.put("reg_nama_atasan", (String)m.get("MCL_FIRST"));
					map.put("reg_tempat_lahir", (String)m.get("TEMPAT_LAHIR"));
					//map.put("reg_distribusi", (String)m.get("DISTRIBUSI"));
					map.put("reg_nama", (String)m.get("NAMA"));
					map.put("reg_kd_atasan", (String)m.get("KD_ATASAN"));
					
					//map.put("redirect","tampilkan form");
					map.put("data", data_no_reg);
				}
			}
			
		}
		
		//TEKAN TOMBOL CARI
		if(request.getParameter("btnCari") != null) {
			if(request.getParameter("birthdate").equals("") || cari.length()<3) {
				err.add("Harap masukkan tanggal lahir.");
				err.add("Harap masukkan minimal 3 angka/huruf untuk melakukan pencarian. Terima kasih");
			}else {
				SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
				Date birthdate = formatDate.parse(request.getParameter("birthdate"));
				String date = formatDate.format(birthdate);
				// tampilkan data agent berdasarkan hak akses tiap2 admin
				map.put("daftarAgen", elionsManager.selectCariAgen(cari, date ,currentUser.getLus_id()));
				map.put("birthdate", request.getParameter("birthdate"));
				
				//auto fill data berdasarkan no reg agen
				List data_no_reg = uwManager.selectKuesionerByNoReg(cari);
				if(!data_no_reg.isEmpty()){
					Map m = (HashMap) data_no_reg.get(0);
					String tgl_kontrak = "";
					
					String tgl_lahir = defaultDateFormat.format((Date)m.get("TGL_LAHIR"));
					if(m.get("TGL_KONTRAK")!=null)tgl_kontrak = defaultDateFormat.format((Date)m.get("TGL_KONTRAK"));
					
					map.put("no_reg", cari);
					map.put("reg_distribusi", (String)m.get("DISTRIBUSI"));
					
					map.put("redirect","tampilkan form");
					map.put("data", data_no_reg);
				}
			}
		//TAMPILKAN DETAIL AGEN
		}else if(!tampil.equals("")) {
			if(tampil.trim().length() <= 6){ //tampilkan data agen lama
				map.put("sysDate", elionsManager.selectSysdate());
				List<HashMap> dataAgenList = elionsManager.selectAgentHist(tampil);
				for(int a=0;a<dataAgenList.size();a++) {
					if(dataAgenList.get(a).get("MSAG_ID").equals(tampil)) {
						dataAgenList.set(0, dataAgenList.get(a));
						break;
					}	
				}
				map.put("dataAgen", dataAgenList);
			}else{ //tampilkan data agen baru
				map.put("redirect","tampilkan form");
				Map agentContract = uwManager.selectAgentContract(tampil);

				map.put("agency", agentContract.get("REGION"));
				map.put("vp", agentContract.get("MAC_ATASAN_CAB"));
				map.put("namaAgent", agentContract.get("MAC_NAMA_AGEN"));
				map.put("maclevel", agentContract.get("MAC_LEVEL"));
				map.put("ktp", agentContract.get("MAC_IDENTITAS"));
				map.put("tlh", agentContract.get("MAC_TMPT_LAHIR"));
				map.put("tglLahir", defaultDateFormat.format((Date) agentContract.get("MAC_TGL_LAHIR")));
				map.put("alamat", agentContract.get("MAC_ALAMAT"));
				map.put("tglAwal", defaultDateFormat.format((Date) agentContract.get("MAC_TGL_KONTRAK")));
				map.put("kota_cab", agentContract.get("MAC_KOTA_CAB"));
				map.put("leader2", agentContract.get("MAC_ATASAN_PUSAT"));
				map.put("jabatanpusat", agentContract.get("MAC_JBTN_ATASAN_PUSAT"));

				String distribusi = (String) agentContract.get("MAC_DISTRIBUSI");
				if(distribusi != null) map.putAll(initDistribusi(distribusi));
			}
		//TEKAN TOMBOL SIMPAN
		}else if(request.getParameter("btnSave") != null) {
			// simpan semua data di map
			request.setAttribute("birthdate", request.getParameter("birthdate"));
			String catatan = request.getParameter("catatan");
			String msag_id = request.getParameter("msag_id");
			String jabatanpusat = request.getParameter("jabatanpusat");
			String atasanCabang = ServletRequestUtils.getStringParameter(request, "vp", "").toUpperCase();
			String atasanUtama = ServletRequestUtils.getStringParameter(request, "leader2", "").toUpperCase();
			
			Map<String, Object> content = new HashMap<String, Object>();
			
			if(!catatan.equals("")) {
				//map.put("dataAgen", elionsManager.selectAgentHist(msag_id));
				List<HashMap> dataAgenList = elionsManager.selectAgentHist(msag_id);
				for(int a=0;a<dataAgenList.size();a++) {
					if(dataAgenList.get(a).get("MSAG_ID").equals(msag_id)) {
						content = dataAgenList.get(a);
						dataAgenList.set(0, dataAgenList.get(a));
						break;
					}	
				}
				map.put("dataAgen", dataAgenList);
				
				content.put("MAGH_PROCESS_DATE", elionsManager.selectDate());
				for(int a=1;a<=4;a++) {
					content.put("K"+a, "");
				}
				// set susunan agen
				for(int a=1;a<=4;a++) {
					if(dataAgenList.size() - a >= 0) {
						Map temp = dataAgenList.get(dataAgenList.size() - a);
						BigDecimal lsle = (BigDecimal) temp.get("LSLE_ID");
						content.put("K"+lsle.intValue(), temp.get("MSAG_ID"));
						if(lsle.intValue() == 1) {
							content.put("LEADER", temp.get("MCL_FIRST"));
							content.put("vp_sales", elionsManager.selectVP(temp.get("MSAG_ID").toString()));
						}
					}
				}
				
				User user = (User) request.getSession().getAttribute("currentUser");
				content.put("lus_id".toUpperCase(), user.getLus_id());
				content.put( "magh_desc".toUpperCase(), catatan );

				map.put("cekFlag", elionsManager.selectFlagBmSbm(msag_id));
				List<HashMap> dataCekFlag = ( List<HashMap> ) map.get("cekFlag"); 
				Map flag = dataCekFlag.get(0);
				if(flag.get("MSAG_FLAG_BM") == null) {
					content.put("MSAG_FLAG_BM", 0);
					if(flag.get("MSAG_SBM") == null) { content.put("MSAG_SBM", 0); }
				}
				else {
					content.put("MSAG_SBM", 0);
					content.put("MSAG_FLAG_BM", 0);
				}
				content.put("LSAS_ID", 14);
				content.put("MAGH_INPUT_DATE", elionsManager.selectDate());
				
				if(elionsManager.saveHistory(msag_id) == null) {
					elionsManager.insertHistory(content);	
				}
				else {
					// ga perlu update, slalu insert
					//elionsManager.updateHistory(content);
				}
				
				map.put("KetTambahan", elionsManager.selectKetAgent(content.get("MSAG_ID").toString()));
				List<HashMap> dataKetTambahan = ( List<HashMap> ) map.get("KetTambahan"); 
				Map KetTambahan = dataKetTambahan.get(0);
				
				if(content.get("vp_sales") == null) { content.put("vp_sales", ""); }
				if(KetTambahan.get("MSPE_NO_IDENTITY") == null) { content.put("ktp", ""); }
				else { content.put("ktp", KetTambahan.get("MSPE_NO_IDENTITY")); }
				if(KetTambahan.get("ALAMAT_RUMAH") == null) { content.put("alamat", ""); }
				else { content.put("alamat", KetTambahan.get("ALAMAT_RUMAH")); }
				if(KetTambahan.get("MSPE_DATE_BIRTH") == null) { content.put("birth", ""); }
				else { content.put("birth", KetTambahan.get("MSPE_DATE_BIRTH")); }
				if(KetTambahan.get("MSPE_PLACE_BIRTH") == null) { content.put("placeBirth", ""); }
				else { content.put("placeBirth", KetTambahan.get("MSPE_PLACE_BIRTH")); }
				if(atasanCabang.equals("-")){ atasanCabang=""; }
				if(atasanUtama.equals("-")){
					atasanUtama="";
					jabatanpusat="";
				}
				String nama = request.getParameter("jnsagen");
				String agency = "";
				if(!StringUtil.isEmpty(nama)){
					int pos = nama.indexOf('~');
					msag_id = nama.substring(0,pos);
					agency = nama.substring(pos+1,nama.length());
				}
				
				//content.put("LEADER", ServletRequestUtils.getStringParameter(request, "leader", "IJ.SOEGENG WIBOWO").toUpperCase());
//				content.put("LEADER", ServletRequestUtils.getStringParameter(request, "leader", "PARLIN SIAHAAN").toUpperCase());
				if ("AGENCY DIVISION".toUpperCase().equals(agency)){
					content.put("LEADER", ServletRequestUtils.getStringParameter(request, "leader2","").toUpperCase());
					content.put("LEADER2", "");
				}else{
					content.put("LEADER", ServletRequestUtils.getStringParameter(request, "leader", "").toUpperCase());
					content.put("LEADER2", atasanUtama);
				}
				content.put("jabatanpusat", jabatanpusat);
				
				content.put("cab_bank", currentUser.getCab_bank());
				content.put("dateNow", elionsManager.selectSysdate());
				//tambahan untuk kirim nilai properties ke PrintPolis
				content.put("save", props.getProperty("pdf.save.perjanjianKeagenan"));
				content.put("template", props.getProperty("pdf.merge.perjanjianKeagenan"));				
				
				//menampilkan window save pdf
				/*if(content.get("DISTRIBUSI").toString().equals("AGENCY")) {
					filename = "Kontrak Agency System - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
				}
				else if(content.get("DISTRIBUSI").toString().equals("HYBRID(ARTHAMAS)") || content.get("DISTRIBUSI").toString().equals("HYBRID(AJS)")) {
					filename = "Kontrak Hybrid System - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
				}
				else if(content.get("DISTRIBUSI").toString().equals("REGIONAL")) {
					filename = "Kontrak Regional System - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
				}*/

				//buat nama untuk pdf & create template berdasarkan distribusi & level
				
				//gabungin PDF
				List<String> pdfs = new ArrayList<String>();
				boolean suksesMerge = false;
				String fileKontrak = "";
				String fileLampiran = "";
				String tempName = "";				
				
				if(content.get("DISTRIBUSI").toString().equals("AGENCY") || content.get("DISTRIBUSI").toString().equals("BRIDGE AGENCY") || 
				   content.get("DISTRIBUSI").toString().equals("NEW AGENCY")) {
					filename = "Kontrak Agency System - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
					
					fileKontrak = elionsManager.selectGetPDF("AKTIF","SINARMAS"); 
					//fileKontrak= "\\\\EBSERVER.sinarmasmsiglife.co.id\\PDFIND\PERJANJIAN_KEAGENAN\\TEMPLATE\LEGAL\\PERJANJIAN KEAGENAN PT AJ SINARMAS(7).PDF";
					
					
					if(content.get("DISTRIBUSI").toString().equals("NEW AGENCY")){
						if(content.get("JABATAN").equals("AGENCY DIRECTOR (INKUBATOR)")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","INKUBATOR(NEW AGENCY");
							tempName = "INKUBATOR(NEW AGENCY)";
						}
						else if(content.get("JABATAN").equals("AGENCY DIRECTOR (KANTOR MANDIRI)")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","MANDIRI(NEW AGENCY)");
							tempName = "MANDIRI(NEW AGENCY)";
						}
						else if(content.get("JABATAN").equals("AGENCY MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN AM(NEW AGENCY)");
							tempName = "AM(NEW AGENCY)";
						}
						else if(content.get("JABATAN").equals("SALES MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SM(NEW AGENCY)");
							tempName = "SM(NEW AGENCY)";
						}
						else if(content.get("JABATAN").equals("SALES EXECUTIVE")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SE(NEW AGENCY)");
							tempName = "SE(NEW AGENCY)";
						}
					}else{
						if(content.get("JABATAN").equals("AGENCY DIRECTOR (INKUBATOR)")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","INKUBATOR(AGENCY)");
							tempName = "INKUBATOR";
						}
						else if(content.get("JABATAN").equals("AGENCY DIRECTOR (KANTOR MANDIRI)")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","MANDIRI(AGENCY)");
							tempName = "MANDIRI";
						}
						else if(content.get("JABATAN").equals("AGENCY MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN AM(AGENCY)");
							tempName = "AM";
						}
						else if(content.get("JABATAN").equals("SALES MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SM(AGENCY)");
							tempName = "SM";
						}
						else if(content.get("JABATAN").equals("SALES EXECUTIVE")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SE(AGENCY)");
							tempName = "SE";
						}
						
						if(content.get("DISTRIBUSI").toString().equals("BRIDGE AGENCY")){
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN BRIDGE");
						}
					}
					// merge PDF
					/*pdfs.add(fileKontrak);
					pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Agency System-"+tempName+".pdf");
					if(content.get("DISTRIBUSI").toString().equals("BRIDGE AGENCY")){
						output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Bridge System-"+tempName+".pdf");
					}
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);*/
					pdfs.add(fileKontrak);
					pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Agency System-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}
				else if(content.get("DISTRIBUSI").toString().equals("HYBRID(ARTHAMAS)") || content.get("DISTRIBUSI").toString().equals("HYBRID(AJS)") ) {
					if(content.get("DISTRIBUSI").toString().equals("HYBRID(ARTHAMAS)")) {
						filename = "Kontrak Hybrid System(Arthamas) - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
						filename = filename.replace(" ", "_");
						
						fileKontrak = elionsManager.selectGetPDF("AKTIF","ARTHAMAS");  
						if(content.get("JABATAN").equals("REGIONAL DIRECTOR")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN RD(HYBRID)");
							tempName = "RD";
						}
						else if(content.get("JABATAN").equals("REGIONAL MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN RM(HYBRID)");
							tempName = "RM";
						}
						else if(content.get("JABATAN").equals("DISTRICT MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","DM(HYBRID)");
							tempName = "DM";
						}
						else if(content.get("JABATAN").equals("BRANCH MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN BM(HYBRID)");
							tempName = "BM";
						}
						else if(content.get("JABATAN").equals("SALES MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","SM(HYBRID)");
							tempName = "SM";
						}
						else if(content.get("JABATAN").equals("FINANCIAL CONSULTANT")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","FC(HYBRID)");
							tempName = "FC";
						}						
					}
					else {
						filename = "Kontrak Hybrid System(AJS) - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
						filename = filename.replace(" ", "_");
						
						fileKontrak = elionsManager.selectGetPDF("AKTIF","SINARMAS");  
						if(content.get("JABATAN").equals("REGIONAL DIRECTOR")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","RD(HYBRID)");
							tempName = "RD(AJS)";
						}
						else if(content.get("JABATAN").equals("REGIONAL MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN RM(HYBRID)");
							tempName = "RM(AJS)";
						}
						else if(content.get("JABATAN").equals("DISTRICT MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","DM(HYBRID)");
							tempName = "DM(AJS)";
						}
						else if(content.get("JABATAN").equals("BRANCH MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN BM(HYBRID)");
							tempName = "BM(AJS)";
						}
						else if(content.get("JABATAN").equals("SALES MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","SM(HYBRID)");
							tempName = "SM(AJS)";
						}
						else if(content.get("JABATAN").equals("FINANCIAL CONSULTANT")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","FC(HYBRID)");
							tempName = "FC(AJS)";
						}
					}
					
					pdfs.add(fileKontrak);
					pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Hybrid System-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);					
					
				}
				else if(content.get("DISTRIBUSI").toString().equals("REGIONAL")) {
					filename = "Kontrak Regional System - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
					
					fileKontrak = elionsManager.selectGetPDF("AKTIF","SINARMAS"); 
					
					if(content.get("JABATAN").equals("REGIONAL MANAGER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","RM(REGIONAL)");
						tempName = "RM";
					}
					else if(content.get("JABATAN").equals("SENIOR BRANCH MANAGER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","SBM(REGIONAL)");
						tempName = "SBM";
					}
					else if (content.get("JABATAN").equals("BRANCH MANAGER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","BM(REGIONAL)");
						tempName = "BM";
					}
					else if(content.get("JABATAN").equals("UNIT MANAGER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","UM(REGIONAL)");
						tempName = "UM";
					}
					else if(content.get("JABATAN").equals("MARKETING EXECUTIVE")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","ME(REGIONAL)");
						tempName = "ME";
					}
					
					pdfs.add(fileKontrak);
					if(!fileLampiran.equals("")) pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Regional System-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);					

				}
				else if(content.get("DISTRIBUSI").toString().equals("CORPORATE")) {
					filename = "Kontrak Corporate System - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
					
					fileKontrak = elionsManager.selectGetPDF("AKTIF","SINARMAS"); 
					
					if(content.get("JABATAN").equals("EMPLOYEE BENEFIT GENERAL MANAGER")) {	
						fileLampiran = elionsManager.selectGetPDF("AKTIF","");
					}
					else if(content.get("JABATAN").equals("TEAM LEADER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","");
					}
					else if(content.get("JABATAN").equals("CORPORATE ACCOUNT MANAGER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","");
					}
					else if(content.get("JABATAN").equals("EMPLOYEE BENEFIT FINANCIAL CONSULTANT")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","");
					}
					
					pdfs.add(fileKontrak);
					pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\none-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);					

				}if(content.get("DISTRIBUSI").toString().equals("BUSINESS PARTNER")){
					filename = "Kontrak Business Partner - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
					
					fileKontrak = elionsManager.selectGetPDF("AKTIF","SINARMAS"); 
					
					if(content.get("JABATAN").equals("ADM")) {	
						fileLampiran = elionsManager.selectGetPDF("AKTIF","");
						tempName = "ADM";
					}
					else if(content.get("JABATAN").equals("CRO")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","");
						tempName = "CRO";
					}
					else if(content.get("JABATAN").equals("RO")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","");
						tempName = "RO";
					}
					
					pdfs.add(fileKontrak);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak BP-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);		
//					String outputName = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+reg_spaj+"\\"+"espaj_"+reg_spaj+".pdf";
//			        PdfStamper stamp = new PdfStamper(reader,new FileOutputStream(outputName));
				}
				
				if(content.get("DISTRIBUSI").toString().equals("AKM WORKSITE")) {
					filename = "Kontrak AKM Worksite - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
					
					fileKontrak = elionsManager.selectGetPDF("AKTIF","SINARMAS"); 
					
					
					if(content.get("JABATAN").equals("AD")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","AD");
						tempName = "AD";
					}
					else if(content.get("JABATAN").equals("AM-BM")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","MANDIRI(AGENCY)");
						tempName = "MANDIRI";
					}
					else if(content.get("JABATAN").equals("SM")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SM(AGENCY)");
						tempName = "SM";
					}
					else if(content.get("JABATAN").equals("SE")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SE(AGENCY)");
						tempName = "SE";
					}
					
					if(content.get("DISTRIBUSI").toString().equals("BRIDGE AGENCY")){
						fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN BRIDGE");
					}
					
					// merge PDF
					/*pdfs.add(fileKontrak);
					pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Agency System-"+tempName+".pdf");
					if(content.get("DISTRIBUSI").toString().equals("BRIDGE AGENCY")){
						output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Bridge System-"+tempName+".pdf");
					}
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);*/
					pdfs.add(fileKontrak);
					pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Agency System-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}
				
				if(!suksesMerge) {
					err.add("Tidak berhasil menggabungkan PDF perjanjian keagenan dengan lampiran");
					map.put("errors", err);
					return new ModelAndView("bac/kontrak_marketing", map);					
				}				
				
				try{
					  File l_file = new File(filename);
				      FileInputStream in = new FileInputStream(l_file);				      
				      in.close();
				   
				      scFile=true;
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}
				
				if(!scFile) {
					printPolis.generatePolis(content);
				}
				
				fileString = props.getProperty("pdf.save.perjanjianKeagenan")+"\\"+filename+".pdf";
				File l_file = new File(fileString);
				content.put("fileName", l_file.getAbsolutePath());
				FileInputStream in = null;
				ServletOutputStream ouputStream = null;
				try{
					
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "Attachment;filename="+filename+".pdf");
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					
					in = new FileInputStream(l_file);
				    ouputStream = response.getOutputStream();
				    
				    IOUtils.copy(in, ouputStream);
				    
				    uwManager.saveAgentContractHist(currentUser, content);
				    return null;
				    
				}catch (Exception e) {
					logger.error("ERROR :", e);
				}finally {
					try {
						if(in != null) {
							in.close();
						}
						if(ouputStream != null) {
							ouputStream.close();
						}
					}catch (Exception e) {
						logger.error("ERROR :", e);
					}
				}
			}
			else {
				err.add("Harap masukkan catatan sebelum melakukan pencetakan kontrak");
			}
			
		}else if(request.getParameter("btnInput") != null) {
			map.put("redirect","tampilkan form");

		}else if(request.getParameter("btnSaveBaru") != null) {
			// validasi form
			if( !request.getParameter("distribusi").equals("") && !request.getParameter("alamat").equals("") &&
				!request.getParameter("tglLahir").equals("__/__/____") && !request.getParameter("tlh").equals("") && !request.getParameter("ktp").equals("") && 
				!request.getParameter("tglAwal").equals("__/__/____") && !request.getParameter("level").equals("") && !request.getParameter("cabang").equals("")/* &&
				!request.getParameter("jabatanpusat").equals("")*/) {
				// !request.getParameter("namaAgent").equals("") &&
				// simpan data 
				Map<String, Object> content = new HashMap<String, Object>();

				SimpleDateFormat formatDate =new SimpleDateFormat("dd/MM/yyyy");
				//Date awalKontrak = formatDate.parse(request.getParameter("awalKontrak"));
				Date MSAG_BEG_DATE = formatDate.parse(request.getParameter("tglAwal"));
				//Date MSAG_END_DATE = formatDate.parse(request.getParameter("tglAkhir"));
				Date tglLahir = formatDate.parse(request.getParameter("tglLahir"));
				String jabatanpusat = request.getParameter("jabatanpusat");
				
				String nama = request.getParameter("jnsagen");
				String msag_id=null, agency=null;
				if(! nama.equals("")){
					int pos=nama.indexOf('~');
					msag_id=nama.substring(0,pos);
					agency=nama.substring(pos+1,nama.length());
				}
				
				//gabungin PDF
				List<String> pdfs = new ArrayList<String>();
				boolean suksesMerge = false;
				String fileKontrak = "";
				String fileLampiran = "";
				String tempName = "";
				
				String atasanCabang = ServletRequestUtils.getStringParameter(request, "vp", "").toUpperCase();
				if(atasanCabang.equals("-")){
					atasanCabang="";
				}
				
				String atasanUtama = ServletRequestUtils.getStringParameter(request, "leader2", "").toUpperCase();
				if(atasanUtama.equals("-")){
					atasanUtama="";
					jabatanpusat="";
				}
				
				//content.put("awalKontrak", awalKontrak);
				content.put("MSAG_BEG_DATE", MSAG_BEG_DATE);
				//content.put("MSAG_END_DATE", MSAG_END_DATE);
				content.put("dateNow",elionsManager.selectSysdate());
				content.put("DISTRIBUSI", request.getParameter("distribusi"));
				content.put("JABATAN",request.getParameter("level").toUpperCase());
				content.put("MSAG_ID", "");
				content.put("MCL_FIRST", request.getParameter("namaAgent").toUpperCase());
				content.put("ktp", request.getParameter("ktp"));
				content.put("alamat", request.getParameter("alamat").toUpperCase());
				content.put("vp_sales", atasanCabang);
				content.put("jabatanpusat", jabatanpusat);
//				if(!request.getParameter("distribusi").contains("HYBRID")){ 
					content.put("LSRG_NAMA", agency.toUpperCase()); 
//					}
				//content.put("LEADER", ServletRequestUtils.getStringParameter(request, "leader", "IJ.SOEGENG WIBOWO").toUpperCase());	
				if (request.getParameter("distribusi").toUpperCase().equals("AGENCY")){
					if(content.get("JABATAN").toString().contains("AGENCY DIRECTOR")){
						content.put("LEADER", ServletRequestUtils.getStringParameter(request, "leader2","").toUpperCase());
						content.put("LEADER2", "");
					}else{
						content.put("LEADER", ServletRequestUtils.getStringParameter(request, "leader", "PARLIN SIAHAAN").toUpperCase());
						content.put("LEADER2", atasanUtama);
					}
				}
				content.put("placeBirth", request.getParameter("tlh").toUpperCase());
				content.put("birth", tglLahir);
				content.put("cab_bank", request.getParameter("cabang").toUpperCase());
				content.put("status", "printBaru");
				//tambahan untuk kirim nilai properties ke PrintPolis
				content.put("save", props.getProperty("pdf.save.perjanjianKeagenan"));
				content.put("template", props.getProperty("pdf.merge.perjanjianKeagenan"));
				
								
				
				//buat nama untuk pdf & create template berdasarkan distribusi & level
				if(content.get("DISTRIBUSI").toString().equals("AGENCY") || content.get("DISTRIBUSI").toString().equals("BRIDGE AGENCY") || 
					content.get("DISTRIBUSI").toString().equals("NEW AGENCY")) {
					
					//yayan
					String filenameJabatanBaru = (String) content.get("JABATAN");					
					if(filenameJabatanBaru.equalsIgnoreCase("BUSINESS PARTNER *** (INKUBATOR)")){
						filenameJabatanBaru = "BUSINESS PARTNER 3 (INKUBATOR)";
					}
					else if(filenameJabatanBaru.equalsIgnoreCase("BUSINESS PARTNER *** (KANTOR MANDIRI)")){
						filenameJabatanBaru = "BUSINESS PARTNER 3 (KANTOR MANDIRI)";
					}
					else if(filenameJabatanBaru.equalsIgnoreCase("BUSINESS PARTNER **")){
						filenameJabatanBaru = "BUSINESS PARTNER 2";
					}	
					else if(filenameJabatanBaru.equalsIgnoreCase("BUSINESS PARTNER *")){
						filenameJabatanBaru = "BUSINESS PARTNER";
					}
					else if(filenameJabatanBaru.equalsIgnoreCase("FINANCIAL CONSULTANT")){
						filenameJabatanBaru = "FINANCIAL CONSULTANT";
					}
					else{
						filenameJabatanBaru = (String) content.get("JABATAN");
					}
					
					filename = "Kontrak Agency System - " + filenameJabatanBaru + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
					fileKontrak = elionsManager.selectGetPDF("AKTIF","SINARMAS"); 
					if(content.get("DISTRIBUSI").toString().equals("NEW AGENCY")){
						if(content.get("JABATAN").equals("AGENCY DIRECTOR (INKUBATOR)") || content.get("JABATAN").equals("BUSINESS PARTNER *** (INKUBATOR)") ) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","INKUBATOR(NEW AGENCY");
							tempName = "INKUBATOR(NEW AGENCY)";
						}
						else if(content.get("JABATAN").equals("AGENCY DIRECTOR (KANTOR MANDIRI)") || content.get("JABATAN").equals("BUSINESS PARTNER *** (KANTOR MANDIRI)") ) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","MANDIRI(NEW AGENCY)");
							tempName = "MANDIRI(NEW AGENCY)";
						}
						else if(content.get("JABATAN").equals("AGENCY MANAGER") || content.get("JABATAN").equals("BUSINESS PARTNER **")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN AM(NEW AGENCY)");
							tempName = "AM(NEW AGENCY)";
						}
						else if(content.get("JABATAN").equals("SALES MANAGER") || content.get("JABATAN").equals("BUSINESS PARTNER *")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SM(NEW AGENCY)");
							tempName = "SM(NEW AGENCY)";
						}
						else if(content.get("JABATAN").equals("SALES EXECUTIVE") || content.get("JABATAN").equals("FINANCIAL CONSULTANT")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SE(NEW AGENCY)");
							tempName = "SE(NEW AGENCY)";
						}
					}else{
						if(content.get("JABATAN").equals("AGENCY DIRECTOR (INKUBATOR)") || content.get("JABATAN").equals("BUSINESS PARTNER *** (INKUBATOR)") ) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","INKUBATOR(AGENCY)");
							tempName = "INKUBATOR";
						}
						else if(content.get("JABATAN").equals("AGENCY DIRECTOR (KANTOR MANDIRI)") || content.get("JABATAN").equals("BUSINESS PARTNER *** (KANTOR MANDIRI)")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","MANDIRI(AGENCY)");
							tempName = "MANDIRI";
						}
						else if(content.get("JABATAN").equals("AGENCY MANAGER") || content.get("JABATAN").equals("BUSINESS PARTNER **") ) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN AM(AGENCY)");
							tempName = "AM";
						}
						else if(content.get("JABATAN").equals("SALES MANAGER") || content.get("JABATAN").equals("BUSINESS PARTNER *") ) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SM(AGENCY)");
							tempName = "SM";
						}
						else if(content.get("JABATAN").equals("SALES EXECUTIVE") || content.get("JABATAN").equals("FINANCIAL CONSULTANT")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SE(AGENCY)");
							tempName = "SE";
						}
						
						if(content.get("DISTRIBUSI").toString().equals("BRIDGE AGENCY")){
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN BRIDGE");
						}
					}				
					
					// merge PDF
					pdfs.add(fileKontrak);
					pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Agency System-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}
				else if(content.get("DISTRIBUSI").toString().equals("HYBRID(ARTHAMAS)") || content.get("DISTRIBUSI").toString().equals("HYBRID(AJS)") ) {
					if(content.get("DISTRIBUSI").toString().equals("HYBRID(ARTHAMAS)")) {	
						filename = "Kontrak Hybrid System(Arthamas) - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
						filename = filename.replace(" ", "_");
						
						fileKontrak = elionsManager.selectGetPDF("AKTIF","ARTHAMAS");  
						if(content.get("JABATAN").equals("REGIONAL DIRECTOR")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN RD(HYBRID)");
							tempName = "RD";
						}
						else if(content.get("JABATAN").equals("REGIONAL MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN RM(HYBRID)");
							tempName = "RM";
						}
						else if(content.get("JABATAN").equals("DISTRICT MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","DM(HYBRID)");
							tempName = "DM";
						}
						else if(content.get("JABATAN").equals("BRANCH MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN BM(HYBRID)");
							tempName = "BM";
						}
						else if(content.get("JABATAN").equals("SALES MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","SM(HYBRID)");
							tempName = "SM";
						}
						else if(content.get("JABATAN").equals("FINANCIAL CONSULTANT")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","FC(HYBRID)");
							tempName = "FC";
						}						
					}
					else {
						filename = "Kontrak Hybrid System(AJS) - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
						filename = filename.replace(" ", "_");
						
						fileKontrak = elionsManager.selectGetPDF("AKTIF","SINARMAS");  
						if(content.get("JABATAN").equals("REGIONAL DIRECTOR")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","RD(HYBRID)");
							tempName = "RD(AJS)";
						}
						else if(content.get("JABATAN").equals("REGIONAL MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN RM(HYBRID)");
							tempName = "RM(AJS)";
						}
						else if(content.get("JABATAN").equals("DISTRICT MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","DM(HYBRID)");
							tempName = "DM(AJS)";
						}
						else if(content.get("JABATAN").equals("BRANCH MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN BM(HYBRID)");
							tempName = "BM(AJS)";
						}
						else if(content.get("JABATAN").equals("SALES MANAGER")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","SM(HYBRID)");
							tempName = "SM(AJS)";
						}
						else if(content.get("JABATAN").equals("FINANCIAL CONSULTANT")) {
							fileLampiran = elionsManager.selectGetPDF("AKTIF","FC(HYBRID)");
							tempName = "FC(AJS)";
						}
					}
					
					pdfs.add(fileKontrak);
					pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Hybrid System-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);					
					
				}
				else if(content.get("DISTRIBUSI").toString().equals("REGIONAL")) {
					filename = "Kontrak Regional System - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
					
					fileKontrak = elionsManager.selectGetPDF("AKTIF","SINARMAS"); 
					
					if(content.get("JABATAN").equals("REGIONAL MANAGER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","RM(REGIONAL)");
						tempName = "RM";
					}
					else if(content.get("JABATAN").equals("SENIOR BRANCH MANAGER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","SBM(REGIONAL)");
						tempName = "SBM";
					}
					else if (content.get("JABATAN").equals("BRANCH MANAGER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","BM(REGIONAL)");
						tempName = "BM";
					}
					else if(content.get("JABATAN").equals("UNIT MANAGER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","UM(REGIONAL)");
						tempName = "UM";
					}
					else if(content.get("JABATAN").equals("MARKETING EXECUTIVE")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","ME(REGIONAL)");
						tempName = "ME";
					}
					
					pdfs.add(fileKontrak);
					pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Regional System-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);					

				}else if(content.get("DISTRIBUSI").toString().equals("CORPORATE")) {
					filename = "Kontrak Corporate System - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
					
					fileKontrak = elionsManager.selectGetPDF("AKTIF","SINARMAS"); 
					
					if(content.get("JABATAN").equals("EMPLOYEE BENEFIT GENERAL MANAGER")) {	
						fileLampiran = elionsManager.selectGetPDF("AKTIF","");
					}
					else if(content.get("JABATAN").equals("TEAM LEADER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","");
					}
					else if(content.get("JABATAN").equals("CORPORATE ACCOUNT MANAGER")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","");
					}
					else if(content.get("JABATAN").equals("EMPLOYEE BENEFIT FINANCIAL CONSULTANT")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","");
					}
					
					pdfs.add(fileKontrak);
					pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\none-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);					

//				}if(content.get("DISTRIBUSI").toString().equals("AGENCY ARTHAMAS")) {//agency arthamas dihilangkan atas permintaan wasisti(75656)
//					filename = "Kontrak Agency Arthamas System - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
//					filename = filename.replace(" ", "_");
//					
//					fileKontrak = elionsManager.selectGetPDF("AKTIF","ARTHAMAS"); 
//					
//					
//					if(content.get("JABATAN").equals("AGENCY DIRECTOR (INKUBATOR)")) {
//						fileLampiran = elionsManager.selectGetPDF("AKTIF","INKUBATOR");
//						tempName = "INKUBATOR";
//					}
//					else if(content.get("JABATAN").equals("AGENCY DIRECTOR (KANTOR MANDIRI)")) {
//						fileLampiran = elionsManager.selectGetPDF("AKTIF","MANDIRI");
//						tempName = "MANDIRI";
//					}
//					else if(content.get("JABATAN").equals("AGENCY MANAGER")) {
//						fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN AM(AGENCY)");
//						tempName = "AM";
//					}
//					else if(content.get("JABATAN").equals("SALES MANAGER")) {
//						fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SM(AGENCY)");
//						tempName = "SM";
//					}
//					else if(content.get("JABATAN").equals("SALES EXECUTIVE")) {
//						fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SE(AGENCY)");
//						tempName = "SE";
//					}
//					
//					// merge PDF
//					pdfs.add(fileKontrak);
//					pdfs.add(fileLampiran);
//					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Agency Arthamas System-"+tempName+".pdf");
//					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}else if(content.get("DISTRIBUSI").toString().equals("BUSINESS PARTNER")) {
					filename = "Kontrak Business Partner - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
					
					if(content.get("JABATAN").equals("ADM")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","ADM");
						tempName = "ADM";
					}
					else if(content.get("JABATAN").equals("CRO")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","(CRO)");
						tempName = "CRO";
					}
					else if (content.get("JABATAN").equals("RO")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","(RO)");
						tempName = "RO";
					}
					
					pdfs.add(fileKontrak);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Business Partner-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);					

				}
				
				if(content.get("DISTRIBUSI").toString().equals("AKM WORKSITE")) {
					filename = "Kontrak AKM Worksite - " + content.get("JABATAN") + " (" + content.get("MCL_FIRST") + ")";
					filename = filename.replace(" ", "_");
					
					fileKontrak = elionsManager.selectGetPDF("AKTIF","SINARMAS"); 
					if(content.get("JABATAN").equals("AD")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN AD KANTOR MANDIRI");
						tempName = "LAMPIRAN AD KANTOR MANDIRI";
					}else if(content.get("JABATAN").equals("AM-BM")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN AM(AGENCY)");
						tempName = "AM";
					}
					else if(content.get("JABATAN").equals("SM")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SM(AGENCY)");
						tempName = "SM";
					}
					else if(content.get("JABATAN").equals("SE")) {
						fileLampiran = elionsManager.selectGetPDF("AKTIF","LAMPIRAN SE(AGENCY)");
						tempName = "SE";
					}
					
					// merge PDF
					pdfs.add(fileKontrak);
					pdfs.add(fileLampiran);
					OutputStream output = new FileOutputStream(props.getProperty("pdf.merge.perjanjianKeagenan")+"\\Kontrak Agency System-"+tempName+".pdf");
					suksesMerge = MergePDF.concatPDFs(pdfs, output, false);
				}
				
				if(!suksesMerge) {
					err.add("Tidak berhasil menggabungkan PDF perjanjian keagenan dengan lampiran");
					map.put("errors", err);
					return new ModelAndView("bac/kontrak_marketing", map);					
				}
				
				File l_file = uwManager.prosesPerjanjianKeagenan(fileString, scFile, filename, content);
				content.put("fileName", l_file.getAbsolutePath());
				
				FileInputStream in = null;
				ServletOutputStream ouputStream = null;
				try{
					
					response.setContentType("application/pdf");
					response.setHeader("Content-Disposition", "Attachment;filename="+filename+".pdf");
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					
					in = new FileInputStream(l_file);
				    ouputStream = response.getOutputStream();
				    
				    IOUtils.copy(in, ouputStream);

				    uwManager.saveAgentContractHist(currentUser, content);
				    return null;
				    
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
			}
			else {
				err.add("Semua keterangan harus diisi. Kolom PIMPINAN boleh dikosongkan jika memang tidak ada");
			}
		}
		
		map.put("errors", err);
		
		//untuk testing only
		//map.put("cari", "NAMA AGEN");
		//map.put("birthdate", "09/11/1981");
		
		return new ModelAndView("bac/kontrak_marketing", map);
	}
	
    /**
     * Report Summary Kontrak Keagenan untuk orang BAS, req Yusy (Helpdesk #21258)
     * http://yusufxp/E-Lions/report/agency.htm?window=summary_agent_contract
     * Yusuf - 12 Sep 2011
     * 
     * Canpri - 18 Mar 2013
     * tambahan view report digroup peruser
     */
    public ModelAndView summary_agent_contract(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser = (User)request.getSession().getAttribute("currentUser");
    	int json = ServletRequestUtils.getIntParameter(request, "json", 0);

    	//bila tampilkan report
    	if(request.getParameter("showReport") != null){
    		
    		String bdate = ServletRequestUtils.getStringParameter(request, "bdate");
    		String edate = ServletRequestUtils.getStringParameter(request, "edate");
    		String jn_report = ServletRequestUtils.getStringParameter(request, "jn_report", "0");
    		String userBas = ServletRequestUtils.getStringParameter(request, "seluserBas", "ALL");
    		List data = uwManager.selectReportSummaryAgentContract(bdate, edate, userBas, jn_report);
    		
    		if(data.size() > 0){ //bila ada data
	    		ServletOutputStream sos = response.getOutputStream();
	    		File sourceFile = null;
	    		
	    		if(jn_report.equals("0")){
	    			sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.summary_agent_contract") + ".jasper");
	    		}else{
	    			sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.summary_agent_contract_peruser") + ".jasper");
	    		}
	    		
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("bdate", bdate);
	    		params.put("edate", edate);

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
    	
    	//halaman depan
    	}else if(json == 0){
        	Map<String, Object> m = new HashMap<String, Object>();

        	m.put("bdate", defaultDateFormat.format(uwManager.selectSysdateTruncated(0)));
	    	m.put("edate", defaultDateFormat.format(uwManager.selectSysdateTruncated(1)));
	    	
	    	List<DropDown> userBas = uwManager.selectUserBasSummaryInput(props.getProperty("user.bas.summaryinput"));
	    	m.put("userBas", userBas);
	    	
	    	Integer bas = 0;
	    	if("690, 3041, 3179, 113, 5, 55".indexOf(currentUser.getLus_id()) != -1){
				bas = 1;
			}
	    	m.put("bas", bas);
	    	
	    	return new ModelAndView("report/summary_agent_contract", m);
    	}
    	
		return null;
    }
	
    public ModelAndView premium_received (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report(" Report Premium Received ", props.getProperty("report.bridge.premium_received"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		return prepareReport(request, response, report);
	}
    
    public ModelAndView polis_generation (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report(" Report Policy Generation ", props.getProperty("report.bridge.polis_generation"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		return prepareReport(request, response, report);
	}
    
    public ModelAndView report_deduct_komisi (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Report report;
		report = new Report(" Report Deduct Komisi ", props.getProperty("report.deduct_komisi"), Report.PDF, null);
		report.addParamDate("Tanggal", "tanggal", true, new Date[] {new Date(), new Date()}, true);
		return prepareReport(request, response, report);
	}
    
}