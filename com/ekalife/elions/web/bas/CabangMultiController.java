package com.ekalife.elions.web.bas;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.FormHist;
import com.ekalife.elions.model.FormSpaj;
import com.ekalife.elions.model.Region;
import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.jasper.JasperUtils;
import com.ekalife.utils.jasper.Report;
import com.ekalife.utils.parent.ParentMultiController;
import com.ibatis.common.resources.Resources;
import com.lowagie.text.pdf.PdfWriter;

public class CabangMultiController extends ParentMultiController {

	protected Region region = new Region();
	
	@SuppressWarnings("rawtypes")
	public ModelAndView updateCabang(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException, Exception {
		Map<String, Object> map=new HashMap<String, Object>();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Report report;
		
		if(request.getParameter("cari")!=null){
			String nm_kota = ServletRequestUtils.getStringParameter(request, "nm_kota");
			region.setNm_kota(nm_kota);
			List a = bacManager.selectLstAddrRegion(nm_kota);
			map.put("daftarCabang", a);
			map.put("tabs", 0);
		}
		
		if(request.getParameter("cariSB")!=null){
			String nm_kota = ServletRequestUtils.getStringParameter(request, "nm_kotaSB");
			region.setNm_kota(nm_kota);
			List a = bacManager.selectLstAddrRegion(nm_kota);
			map.put("daftarCabangSB", a);
			map.put("tabs", 1);
		}
		
		if(request.getParameter("update")!=null){
			String check[] = request.getParameterValues("chbox");
			if(request.getParameterValues("chbox") != null){
				String nm_kota = region.getNm_kota();
				for(int i=0;i<check.length;i++){
					String ch_id = check[i];
					String lar_id =  ServletRequestUtils.getStringParameter(request, "lar_id_"+ch_id,"");
					String nama = ServletRequestUtils.getStringParameter(request, "nama_"+ch_id,null);
					String alamat = ServletRequestUtils.getStringParameter(request, "alamat_"+ch_id,null);
					String admin = ServletRequestUtils.getStringParameter(request, "admin_"+ch_id,null);
					String telp = ServletRequestUtils.getStringParameter(request, "telp_"+ch_id,null);
					String email = ServletRequestUtils.getStringParameter(request, "email_"+ch_id,null);
					String aktif = ServletRequestUtils.getStringParameter(request, "aktif_"+ch_id,"0");
					String lar_speedy = ServletRequestUtils.getStringParameter(request, "lar_speedy_"+ch_id,null);
					
					bacManager.updateLstAddrRegion(lar_id, nama, alamat, admin, telp, email, null, aktif, lar_speedy);
					bacManager.insertLstAddrRegionHist(lar_id, currentUser.getLus_id(), "Update ID pelanggan speedy");
				}
				if(nm_kota != null && nm_kota !=""){
					List a = bacManager.selectLstAddrRegion(nm_kota);
					map.put("daftarCabang", a);
				}
				map.put("tabs", 0);
				map.put("pesan", "update speedy sukses");
			}
		}
		
		if(request.getParameter("updateSB")!=null){
			String check[] = request.getParameterValues("chboxSB");
			if(request.getParameterValues("chboxSB") != null){
				String nm_kota = region.getNm_kota();
				for(int i=0;i<check.length;i++){
					String ch_id = check[i];
					String lar_id =  ServletRequestUtils.getStringParameter(request, "lar_idSB_"+ch_id,"");
					String alamat = ServletRequestUtils.getStringParameter(request, "alamatSB_"+ch_id,null);
					String admin = ServletRequestUtils.getStringParameter(request, "adminSB_"+ch_id,null);
					String telp = ServletRequestUtils.getStringParameter(request, "telpSB_"+ch_id,null);
					String lar_speedy = ServletRequestUtils.getStringParameter(request, "speedySB_"+ch_id,null);
					String tgl = ServletRequestUtils.getStringParameter(request, "tglSB_"+ch_id,null);
					String status = ServletRequestUtils.getStringParameter(request, "statusSB_"+ch_id,"0");
					String ket = ServletRequestUtils.getStringParameter(request, "ketSB_"+ch_id,null);
					
					bacManager.insertLstAutobetSpeedyHist(lar_id, telp, lar_speedy, tgl, status, ket);
				}
				if(nm_kota != null && nm_kota !=""){
					List a = bacManager.selectLstAddrRegion(nm_kota);
					map.put("daftarCabangSB", a);
				}
				map.put("tabs", 1);
				map.put("pesan", "update autodebet sukses");
			}
		}
		
		if(request.getParameter("report")!=null){
			String tglAwal = ServletRequestUtils.getStringParameter(request, "tglAwal");
			String tglAkhir = ServletRequestUtils.getStringParameter(request, "tglAkhir");
			String status = ServletRequestUtils.getStringParameter(request, "statusRS");
			
			List data = bacManager.selectAutodebetSpeedy(tglAwal, tglAkhir, status);
			if(data.size()>0){
				ServletOutputStream sos = response.getOutputStream();
    			File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.autodebet_speedy") + ".jasper");
	    		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
	    		
	    		Map<String, Object> params = new HashMap<String, Object>();
	    		params.put("tglAwal", tglAwal);
	    		params.put("tglAkhir", tglAkhir);
	    		params.put("cetak", currentUser.getName());
	    		params.put("tglcetak", new Date());
	    		params.put("status", status);

	    		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));
	    		JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
	    		sos.close();
	    		return null;
			}else{
				map.put("pesan", "Data tidak ada");
			}
		}
		
		return new ModelAndView("bas/updateCabang", map);
	}
	
	public ModelAndView approveBrosurByEmail(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException, Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		CommandControlSpaj cmd = new CommandControlSpaj();
		
		String msf_id = ServletRequestUtils.getStringParameter(request, "msf_id");
		Integer app = ServletRequestUtils.getIntParameter(request, "app");
		
		cmd.setMsf_id(msf_id);
		
		cmd.setDaftarCabang(elionsManager.selectlstCabang());
		Map all = new HashMap();
		all.put("KEY", "ALL_BRANCH");
		all.put("VALUE", "ALL");
		cmd.getDaftarCabang().add(0,all);
		cmd.setFormHist(new FormHist());
		
		Integer jn = bacManager.selectJenisFormBrosur(cmd.getMsf_id());
		String index = ServletRequestUtils.getStringParameter(request, "index", "");
		cmd.setDaftarFormBrosur(uwManager.selectFormBrosur(cmd.getMsf_id(), currentUser.getLca_id(), currentUser.getLus_id(), jn));
		List<DropDown> x = new ArrayList<DropDown>();
		x.add(new DropDown("5000","10000"));
		x.add(new DropDown("10000","20000"));
		cmd.setTypeTravelIns(x);
//		for(FormSpaj f : cmd.getDaftarFormBrosur()) {
//			f.setWarna((String) daftarWarna.get(f.getStatus_form()));
//		}
		
		Spaj brosur = new Spaj();
		brosur.setMss_jenis(0);
		brosur.setMsab_id(0);
		brosur.setLca_id(cmd.getDaftarFormBrosur().get(0).getLca_id());
		brosur.setLus_id(Integer.valueOf(currentUser.getLus_id()));
		brosur.setJn_brosur(jn);
		cmd.setDaftarStokBrosur(uwManager.selectStokBrosur(brosur));
		
		cmd.setDaftarFormHistory(elionsManager.selectFormHistory(cmd.getMsf_id()));

		FormHist formHist = new FormHist();
		formHist.setMsf_id(cmd.getMsf_id());
		formHist.setMsf_urut(1);
		cmd.setFormHistUser(elionsManager.selectFormHistory(formHist));
		cmd.setIndex(index);
		
		if(app==1){
			cmd.setSubmitMode("approve");
			cmd.getFormHist().setMsfh_desc("Approve via e-mail by "+currentUser.getLus_full_name());
		}else{
			cmd.setSubmitMode("reject");
			cmd.getFormHist().setMsfh_desc("Reject via e-mail by "+currentUser.getLus_full_name());
		}
		
		String sukses = null;
		
		if(cmd.getDaftarFormBrosur().get(0).getPosisi()==0)sukses = uwManager.processFormBrosur(cmd, currentUser);
		else sukses = "Permintaan "+cmd.getMsf_id()+" sudah di approve/reject" ;
		
		Map pesan = new HashMap();
		pesan.put("sukses", sukses);
		pesan.put("direct", 1);
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/cek_brosur.htm")).addAllObjects(pesan);
	}
	
	/**
     * Main Controll STOCK SPAJ untuk UW
     * Ryan - 31 Agustus 2015
     * 
     * dibuat pada Class yang masih Kosong (CabangMultiController)
     */
	public ModelAndView main_stock_spaj(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException, Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
    	Map map = new HashMap();
    	String jenis = ServletRequestUtils.getStringParameter(request, "jenis", "0");
    	String tanggalAwal=ServletRequestUtils.getStringParameter(request, "tanggalAwal");
    	String tanggalAkhir=ServletRequestUtils.getStringParameter(request, "tanggalAkhir");
    	String flag = ServletRequestUtils.getStringParameter(request, "flag", "1");
    	map.put("tab", flag);
    	
    	if(request.getParameter("update") != null) { //Update Data Stock SPAJ
    		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    	Date date = formatter.parse(tanggalAwal);
    		try {
    			String check[] = request.getParameterValues("chbox");
    			if(request.getParameterValues("chbox") != null){
        			for(int i=0;i<check.length;i++){
        				FormSpaj formSpaj = new FormSpaj();
        				
        				String id_stok = check[i];
        				
        				String stok = ServletRequestUtils.getStringParameter(request, "stock_"+id_stok,"");
        				String lsjs_id = ServletRequestUtils.getStringParameter(request, "lsjs_"+id_stok,"");
    					
        				formSpaj.setMsf_amount(Integer.parseInt(stok));
        				formSpaj.setLsjs_id(Integer.parseInt(lsjs_id));
        				formSpaj.setJn_brosur(Integer.parseInt(jenis));
        				formSpaj.setMsfh_dt(date);
        				
    					bacManager.updateStokSpajUw(formSpaj);
        			}
        			map.put("pesan", "Update Stock Spaj Berhasil Diupdate");
        		}
    		}catch (Exception e) {
    			map.put("pesan", "ERROR");
			}
    	}else if(request.getParameter("save") != null) { // Input Data SPAJ keluaran MANUAL UW
    		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    	Date date = formatter.parse(tanggalAwal);
    		try {
    			String check[] = request.getParameterValues("chbox");
    			if(request.getParameterValues("chbox") != null){
        			for(int i=0;i<check.length;i++){
        				FormSpaj formSpaj = new FormSpaj();
        				
        				String id_stok = check[i];
        				
        				String stok = ServletRequestUtils.getStringParameter(request, "stock_"+id_stok,"");
        				String lsjs_id = ServletRequestUtils.getStringParameter(request, "lsjs_"+id_stok,"");
        				String keterangan = ServletRequestUtils.getStringParameter(request, "keterangan_"+id_stok,"");
    					
        				formSpaj.setMsf_amount(Integer.parseInt(stok));
        				formSpaj.setLsjs_id(Integer.parseInt(lsjs_id));
        				formSpaj.setJn_brosur(Integer.parseInt(jenis));
        				formSpaj.setMss_jenis(1);
        				formSpaj.setLca_id("01");
        				formSpaj.setMsfh_dt(date);
        				formSpaj.setMsfh_desc(keterangan);
        				formSpaj.setLus_id(Integer.parseInt(currentUser.getLus_id()));
    					bacManager.insertSpajHist(formSpaj);
        			}
        			map.put("pesan", "Proses Penginputan SPAJ Keluaran( MANUAL UW ) Berhasil");
        		}
    		}catch (Exception e) {
    			map.put("pesan", "ERROR");
			}
    	}else if(request.getParameter("view") != null) { // view history
    		String filter[] = request.getParameterValues("filter");
    		String open = "";
    		if(request.getParameterValues("filter") != null){
    			 open = filter[0];
    		}
    		if(open.equals("")){
    			map.put("brosur2", bacManager.selectStokSPAJSummary(tanggalAwal,tanggalAkhir,1));
    			map.put("filter", "1");
    		}else{
    			map.put("brosur2", bacManager.selectStokSPAJSummary(tanggalAwal,tanggalAkhir,2));	
    			map.put("filter", "2");
    		}
    	}
    	
    	List brosur = bacManager.selectStokSPAJ(jenis,null);
    	
    	map.put("brosur", brosur);
    	map.put("tgl", elionsManager.selectSysdate());
    	map.put("jenis", jenis);
		return new ModelAndView("bas/main_stock", map);
	}
}
