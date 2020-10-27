package com.ekalife.utils.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.write.WritableWorkbook;

import org.springframework.web.servlet.view.document.AbstractJExcelView;

import com.ekalife.elions.model.CommandUploadBac;

/**
 * Custom View untuk file Excel (XLS)
 * 
 * @author Yusuf Sutarko
 */
public class XLSViewer extends AbstractJExcelView {

	@Override
	protected void buildExcelDocument(Map map, WritableWorkbook workBook, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		
		CommandUploadBac cmd = (CommandUploadBac) map.get("cmd");
		
		response.setHeader("Content-Disposition", "attachment; filename=form.xls");
		// (Yusuf) -> belum bisa, index out of bounds
//		OutputStream out = response.getOutputStream();
//        Workbook template = getTemplateSource("include/form/"+cmd.getForm(), request);
//        workBook = Workbook.createWorkbook(out, template);

	}
	

	/*
	ServletContext servletContext = getServletContext();
	this.elionsManager = (ElionsManager) Common.getBean(servletContext, "elionsManager");
		
	WritableSheet sheet = workBook.createSheet("Master", 2);
	
	Map refData = new HashMap();
	//Hal 0 & 1
	refData.put("select_identitas",DroplistManager.getInstance().get("IDENTITY.xml","ID",request));
	refData.put("select_negara",DroplistManager.getInstance().get("WARGANEGARA.xml","ID",request));
	refData.put("select_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","",request));
	refData.put("select_pekerjaan",DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request));
	refData.put("select_medis",DroplistManager.getInstance().get("medis.xml","ID",request));
	refData.put("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
	refData.put("select_agama",DroplistManager.getInstance().get("AGAMA.xml","ID",request));
	refData.put("select_pendidikan",DroplistManager.getInstance().get("PENDIDIKAN.xml","ID",request));
	refData.put("select_tujuan",DroplistManager.getInstance().get("TUJUAN_ASR.xml","",request));
	refData.put("select_penghasilan",DroplistManager.getInstance().get("PENGHASILAN.xml","",request));
	refData.put("select_dana",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
	refData.put("select_relasi",DroplistManager.getInstance().get("RELATION.xml","",request));
	//Hal 2
	List tmp = this.elionsManager.select_produkutama();
	List tmp2 = this.elionsManager.select_tipeproduk();
	refData.put("listtipeproduk",tmp2);
	refData.put("listprodukutama",tmp);
	refData.put("select_autodebet",DroplistManager.getInstance().get("AUTODEBET.xml","ID",request));
	refData.put("select_insrider",DroplistManager.getInstance().get("INSRIDER.xml","ID",request));
	refData.put("select_rider",DroplistManager.getInstance().get("PLANRIDER.xml","BISNIS_ID",request));
	refData.put("select_persenUp",DroplistManager.getInstance().get("PERSENUP.xml","ID",request));
	refData.put("select_dplk",DroplistManager.getInstance().get("DPLK.xml","ID",request));
	//Hal 3
	refData.put("select_jns_top_up",DroplistManager.getInstance().get("TOPUP.xml","ID",request));	
	refData.put("select_jenis_nasabah",DroplistManager.getInstance().get("jenis_nasabah.xml","ID",request));
	refData.put("select_jenis_tabungan",DroplistManager.getInstance().get("jenis_tabungan.xml","ID",request));
	refData.put("select_jangka_invest",DroplistManager.getInstance().get("JNSTOPUP.xml","ID",request));
	refData.put("select_rollover",DroplistManager.getInstance().get("ROLLOVER.xml","ID",request));
	refData.put("select_hubungan",DroplistManager.getInstance().get("RELATION.xml","",request));
	refData.put("select_jenisbunga",DroplistManager.getInstance().get("jenisbunga.xml","",request));
	refData.put("select_karyawan",DroplistManager.getInstance().get("karyawan.xml","",request));
	refData.put("select_kurs",DroplistManager.getInstance().get("KURS.xml","ID",request));
	//Hal 4
	refData.put("select_regional",DroplistManager.getInstance().get("region.xml","ID",request));
	
	int k=0;
	
	for(Iterator it = refData.keySet().iterator(); it.hasNext();) {
		String key = (String) it.next();
		List dropdown = (List) refData.get(key);
		for(int i=0; i<dropdown.size(); i++) {
			Map record = (Map) dropdown.get(i);
			int j=0;
			if(i==0) {
				for(Iterator ir = record.keySet().iterator(); ir.hasNext();) {
					String ker = (String) ir.next();
					Label label = new Label(0+j++, 0+k, ker); 
					sheet.addCell(label); 
				}
				k++;
			}
			j=0;
			for(Iterator ir = record.keySet().iterator(); ir.hasNext();) {
				String ker = (String) ir.next();
				Label label = new Label(0+j++, 0+k, record.get(ker).toString()); 
				sheet.addCell(label); 
			}
			k++;
		}
		k++;
	}
	*/
}
