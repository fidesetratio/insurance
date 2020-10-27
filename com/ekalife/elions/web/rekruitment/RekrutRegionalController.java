package com.ekalife.elions.web.rekruitment;


import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Kuesioner;
import com.ekalife.elions.model.User;
import com.ekalife.elions.web.rekruitment.support.RekrutRegionalvalidator;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.EmailPool;
import com.ekalife.utils.parent.ParentFormController;

import id.co.sinarmaslife.std.model.vo.DropDown;

/**
 * @author HEMILDA
 * Controller untuk rekruitment regional
 * 
 * @author Canpri 
 * @since Feb 25, 2013
 * Penambahan untuk upload Dokumen
 */
public class RekrutRegionalController extends ParentFormController{
	
//	private Map initDistribusi(String mku_jenis_cabang){
//		Map map = new HashMap();
//		if(mku_jenis_cabang.equals("1")){
//			List<DropDown> lstLevel = elionsManager.selectLstLevelAgency();
//			String indexCabangAgency = "37";
//			String[] a = indexCabangAgency.split(",");
//			a = indexCabangAgency.split(",");
//			List lstAgen = elionsManager.selectLstAgenAgency(indexCabangAgency);
//			map.put("mku_jenis_cabang", mku_jenis_cabang);
//			map.put("lstAgen", lstAgen);
//		}else if(mku_jenis_cabang.equals("HYBRID(ARTHAMAS)")  ){
//			List<DropDown> lstLevel = elionsManager.selectLstLevelHybrid();
//			List lstAgen = elionsManager.selectLstHybridArthaMas("00");
//			map.put("mku_jenis_cabang", mku_jenis_cabang);
//			map.put("lstAgen", lstAgen);
//		//code lca_id untuk hybridAJS dicari tau lagi berapa
//		}else if(mku_jenis_cabang.equals("HYBRID(AJS)")){//Req Tri Handini(Info ke IT tgl 4 Januari 2010) : hybrid arthamas digabung dengan hybrid AJS. diubah di selectnya dengan join dua select 
//			List<DropDown> lstLevel = elionsManager.selectLstLevelHybrid();
//			List lstAgen = elionsManager.selectLstHybridAJS("01");
//			map.put("mku_jenis_cabang", mku_jenis_cabang);
//			map.put("lstAgen", lstAgen);
//		}else if(mku_jenis_cabang.equals("REGIONAL")){
//			List<DropDown> lstLevel = elionsManager.selectLstLevelRegional();
//			String indexCabang = "37,46,40,52,55,58,60";
//			List lstAgen = elionsManager.selectLstAgenRegional(indexCabang);
//			map.put("mku_jenis_cabang", mku_jenis_cabang);
//			map.put("lstAgen", lstAgen);
//		}else if(mku_jenis_cabang.equals("AGENCY ARTHAMAS")){
//			List<DropDown> lstLevel = elionsManager.selectLstLevelAgency();
//			List lstAgen = elionsManager.selectLstAgencyArthamas("2");
//			map.put("mku_jenis_cabang", mku_jenis_cabang);
//			map.put("lstAgen", lstAgen);
//		}else if(mku_jenis_cabang.equals("BRIDGE AGENCY")){
//			List<DropDown> lstLevel = elionsManager.selectLstLevelAgency();
//			List lstAgen = elionsManager.selectLstAgen("60");
//			map.put("mku_jenis_cabang", mku_jenis_cabang);
//			map.put("lstAgen", lstAgen);
//		}else if(mku_jenis_cabang.equals("BUSINESS PARTNER")){
//			List<DropDown> lstLevel = elionsManager.selectLstLevelBP();
//			List lstAgen = elionsManager.selectLstAgenBP("61");
//			map.put("mku_jenis_cabang", mku_jenis_cabang);
//			map.put("lstAgen", lstAgen);
//		}
//		return map;
//	}
	
	
	protected void validatePage(Object cmd, Errors err, int page) {
		RekrutRegionalvalidator validator = (RekrutRegionalvalidator) this.getValidator();
	}
	
	protected Map referenceData(HttpServletRequest request,Object cmd, Errors errors) throws Exception {
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", ServletRequestUtils.getStringParameter(request, "editSPAJ", ""));
		String sysdate = defaultDateFormat.format(new Date());
		Map refData = null;
		refData = new HashMap();
		Kuesioner data = (Kuesioner) cmd;
		HttpSession session = request.getSession();
		String res = session.getServletContext().getResource("/xml/").toString();
		
		
//		refData.put("jenis_cabang",DroplistManager.getInstance().get("JENIS_CABANG.xml","ID",request));
		refData.put("bank",DroplistManager.getInstance().get("bank.xml","ID",request));
		refData.put("select_regional",DroplistManager.getInstance().get("region.xml","REGION",request));
		refData.put("select_relasi",DroplistManager.getInstance().get("RELATION_KUESIONER.xml","ID",request));
		refData.put("select_pendidikan",DroplistManager.getInstance().get("PENDIDIKAN_KUESIONER.xml","ID",request));
		refData.put("select_jalurdist", elionsManager.selectDropDown("eka.lst_jalur_dist", "id_dist", "nama_dist", "", "id_dist", "id_dist in (1,2,7,8,9,11)"));
		refData.put("select_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "", "lsag_id", null));
		refData.put("select_propinsi", elionsManager.selectDropDown("eka.lst_propinsi", "lspr_id", "lspr_note", "", "lspr_id", null));
//		refData.put("select_bank", elionsManager.selectDropDown("eka.lst_bank_pusat", "lsbp_id", "lsbp_nama", "", "lsbp_nama", null));
		refData.put("select_bank", uwManager.selectlsBank());
//		refData.put("select_pendidikan", elionsManager.selectDropDown("eka.lst_education", "lsed_id", "lsed_name", "", "lsed_id", "lsed_id in (1,4,5,6,12,13,14,15)"));
		
		// Pilihan "NON" pada list bank diganti jadi blank
		List<DropDown> list_select_bank = (List<DropDown>) refData.get("select_bank");
		for(DropDown bank : list_select_bank) {
			if("0".equals(bank.getKey())) {
				bank.setKey("");
				bank.setValue("");
				bank.setDesc("");
				break;
			}
		}
		
		if(data.getCurrentUser().getMsag_id_ao()!=null){
			refData.put("jenis_rekrut",DroplistManager.getInstance().get("JENIS_REKRUT_AGEN.xml","ID",request));
		}else{
			refData.put("jenis_rekrut",DroplistManager.getInstance().get("JENIS_REKRUT.xml","ID",request));
		}
		
		//klo dari e-agency cek hanya boleh yang punya dia aja di akses
		if(data.getStatus().equals("edit")){
			if(data.getCurrentUser().getMsag_id_ao()!=null&data.getCurrentUser().getLus_id().equals("2661")){
				//klo sudah ditransfer hanya bisa view aja
				if(data.getMku_tgl_transfer_admin()!=null){
					refData.put("editAuthorize", 1);
				}
				
				//klo bukan inputan agen di pentalin			
				if(!data.getMsag_id().equals(data.getCurrentUser().getMsag_id_ao())){
					refData.put("notePermited", 1);
				}
			}
			refData.put("EDITNOTIMPLEMENT", 1);
		}
		
		return refData;
	}
	
	
	protected void initBinder(HttpServletRequest arg0, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Kuesioner data = new Kuesioner();
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		
		if (data.getJuml_daftarTanggungan()==null){
			data.setJuml_daftarTanggungan(0);
		}
		
		String kode_rekrut = request.getParameter("kode_rekrut");
		if (kode_rekrut == null)
		{
			kode_rekrut ="";
		}
		
		if (kode_rekrut.equalsIgnoreCase(""))
		{
			data = new Kuesioner();
			data.setStatus("input");
			
		}else{
			data = this.elionsManager.selectkuesioner(kode_rekrut);
			data.setDaftarTanggungan(this.uwManager.select_tertanggung_rekrut(kode_rekrut));
			data.setStatus("edit");
		}
		
		data.setMku_tglkues(new Date());
		data.setCurrentUser(currentUser);
		//default data
		if(currentUser.getMsag_id_ao()!=null){
			
			data.setMsrk_id(currentUser.getMsag_id_ao());
			data.setMku_jenis_rekrut("3");
			
			Map agenRekrut =(HashMap) this.uwManager.selectagenrekrut(data.getMku_jenis_rekrut(), data.getMsrk_id());
			if (agenRekrut != null)
			{
				data.setMku_rekruiter(agenRekrut.get("NAMA")==null?null:(String)agenRekrut.get("NAMA"));
				data.setMku_acc_rekruiter(agenRekrut.get("ACC")==null?null:(String) agenRekrut.get("ACC"));
				data.setMku_bank_id(agenRekrut.get("NAMABANK")==null?null:((BigDecimal) agenRekrut.get("NAMABANK")).intValue());
				
			}
		}
		return data;
	}
	
	protected void onBind(HttpServletRequest request, Object cmd, BindException errors) throws Exception {
		Kuesioner data = (Kuesioner) cmd;
		String spaj = request.getParameter("spaj");
		NumberFormat nf = NumberFormat.getNumberInstance();
		
		try{
			if(data.getMku_posisi_agen() != null){
				String posisiagen = data.getMku_posisi_agen().toString();
				
				HashMap leveldist = bacManager.selectLstLevelDistRek(
										posisiagen.substring(0, posisiagen.indexOf("~")).toString(),
										posisiagen.substring(posisiagen.indexOf("~")+1, posisiagen.length()).toString()
									);
				
				data.setId_dist( ((BigDecimal) leveldist.get("ID_DIST")).intValue() );
				data.setLsle_id( ((BigDecimal) leveldist.get("LSLE_ID")).intValue() );
				data.setMsag_flag_bm( ((BigDecimal) leveldist.get("MSAG_FLAG_BM")).intValue() );
				data.setMsag_sbm( ((BigDecimal) leveldist.get("MSAG_SBM")).intValue() );
				data.setMsag_gws( ((BigDecimal) leveldist.get("MSAG_GWS")).intValue() );
				data.setMsag_mws( ((BigDecimal) leveldist.get("MSAG_MWS")).intValue() );
//				data.setMsag_tsr( ((BigDecimal) leveldist.get("MSAG_TSR")).intValue() );
//				data.setLvl_fcd( ((BigDecimal) leveldist.get("LVL_FCD")).intValue() );
//				data.setMsag_flag_hp( ((BigDecimal) leveldist.get("MSAG_FLAG_HP")).intValue() );
			}
		}
		catch (Exception e) {
			//err.rejectValue("pesan","","Terjadi kesalahan pada proses perubahan BegDate!");
			logger.error("ERROR :", e);
		}
/*		
		List listTanggungan = data.getDaftarTanggungan();
		//int jumlTang = Integer.parseInt(request.getParameter("juml_x"));
		
		 String mku_no_reg;
		 String mkt_nama;
		 Date mkt_tgl_lahir2;
		 String mkt_tgl_lahir;
		 String tgllhr = null;
		 String blnhr = null;
		 String thnhr = null;
		 String tanggal_lahir = null;
		 String mkt_tempat_lahir;
		 String mkt_pendidikan;
		 Integer mkt_umur2 = 0;
		 Integer umur = null;
		 String mkt_hubungan;
		
		List<KuesionerTanggungan> kuetang = new ArrayList<KuesionerTanggungan>();

		data.setJuml_daftarTanggungan(jumlTang);
		logger.info(data.getJuml_daftarTanggungan());
		
		if(jumlTang > 0){
			for (int k = 1; k <= jumlTang; k++){
				mku_no_reg = request.getParameter("listTanggungan.mku_no_reg"+k);
				mkt_nama = request.getParameter("listTanggungan.mkt_nama"+k);
//				mkt_jenis = request.getParameter("listTanggungan.mkt_jenis"+k);
				tanggal_lahir = FormatString.rpad("0",tgllhr,2)+"/"+FormatString.rpad("0",blnhr,2)+"/"+thnhr;
				mkt_tempat_lahir = request.getParameter("listTanggungan.mkt_tempat_lahir"+k);
				mkt_pendidikan = request.getParameter("listTanggungan.mkt_pendidikan"+k);
				mkt_hubungan = request.getParameter("listTanggungan.mkt_hubungan"+k);
				
				if(mku_no_reg==null){
					mku_no_reg="";
				}
				if(mkt_nama==null){
					mkt_nama="";
				}
//				if(mkt_jenis==null){
//					mkt_jenis="";
//				}
				if(mkt_tempat_lahir==null){
					mkt_tempat_lahir="";
				}
				if(mkt_pendidikan==null){
					mkt_pendidikan="";
				}
				if(mkt_hubungan==null){
					mkt_hubungan="";
				}
				
					tgllhr = request.getParameter("tgllhr"+k);
					blnhr = request.getParameter("blnhr"+k);
					thnhr = request.getParameter("thnhr"+k);
					tanggal_lahir = FormatString.rpad("0",tgllhr,2)+"/"+FormatString.rpad("0",blnhr,2)+"/"+thnhr;
				
					tgllhr = request.getParameter("tgllhr"+k);
					if (tgllhr == null){
						tgllhr ="";
					}
					blnhr = request.getParameter("blnhr"+k);
					if (blnhr == null){
						blnhr = "";
					}
					thnhr = request.getParameter("thnhr"+k);
					if (thnhr == null){
						thnhr = "";
					}
					if ((tgllhr.trim().length()==0)||(blnhr.trim().length()==0)||(thnhr.trim().length()==0)){
						tanggal_lahir=null;
					}else{
						boolean cekk1= f_validasi.f_validasi_numerik(tgllhr);	
						boolean cekk2= f_validasi.f_validasi_numerik(blnhr);
						boolean cekk3= f_validasi.f_validasi_numerik(thnhr);		
						if ((cekk1==false) ||(cekk2==false) || (cekk3==false)){
							tanggal_lahir=null;
						}
					}
				Date tanggallahir = null;
				
					if (tanggal_lahir != null){
						tanggallahir = defaultDateFormat.parse(tanggal_lahir);
					}
					if(tanggallahir==null){
						errors.rejectValue("mku_rekruiter", "", "Tanggal Lahir Tanggungan masih kosong, Silahkan isi terlebih dahulu.");
						String sysdate = defaultDateFormat.format(new Date());
						tanggallahir= new Date();
					}
				
					if (umur==null){
						umur = new Integer(0);
					}
					
						f_hit_umur umr= new f_hit_umur();
						Integer tahun1,tahun2, bulan1, bulan2, tanggal1, tanggal2;
						tanggal1= new Integer(0);
						bulan1 = new Integer(0);
						tahun1 = new Integer(0);
						tanggal2=tanggallahir.getDate();
						bulan2=(tanggallahir.getMonth())+1;
						tahun2=(tanggallahir.getYear())+1900;
						umur = umr.umur(tahun2,bulan2,tanggal2,tahun1,bulan1,tanggal1); //umurx
						if(umur == 0){umur = 1;}
						if(tanggal2==null){tanggal2=0;}
						if(bulan2==null){bulan2=0;}
						if(tahun2==null){tahun2=0;}
					
				
				KuesionerTanggungan ktt = new KuesionerTanggungan();
				ktt.setMku_no_reg(mku_no_reg);
				ktt.setMkt_nama(mkt_nama);
				ktt.setMkt_tgl_lahir(tanggallahir);
				ktt.setMkt_tempat_lahir(mkt_tempat_lahir);
				ktt.setMkt_pendidikan(mkt_pendidikan);
				ktt.setMkt_umur(umur);
				ktt.setMkt_hubungan(mkt_hubungan);
					
				kuetang.add(ktt);
				
			}
		}
		data.setDaftarTanggungan(kuetang);
*/
	}
		
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Kuesioner data = (Kuesioner) cmd;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String sts = data.getStatus();
		Date nowDate = elionsManager.selectSysdate();
		
		String status = "";
		String mku_no_reg = "";
		String keterangan  = "";
		Map m = new HashMap();
		
		if(data.getSubmit1()!=null){ // save data
			data.setPosisi("1");
			try{
				data = this.elionsManager.savingrekruitment(data, err, sts, currentUser);
			}catch(Exception e){
				String desc = "ERROR";
				logger.error("ERROR :", e);
				String error=e.getLocalizedMessage();
				EmailPool.send("SMiLe E-Lions", 1, 1, 0, 0, 
						null, 0, 0, nowDate, null, 
						true, "ajsjava@sinarmasmsiglife.co.id", 
						new String[]{"ryan@sinarmasmsiglife.co.id","alfian_h@sinarmasmsiglife.co.id"},
						null,
						null, 
						"[E-Lions] Error Saving Rekruitment", 
						"Ada Error saat menjalankan simpan data rekruitment, Terlampir Pesannya : <br><br>"+Common.getRootCause(e).getMessage(),
						null, null);
			}
			status = data.getStatus();
			mku_no_reg = data.getMku_no_reg();
			keterangan  = data.getKeterangan();
			m.put("status", status);
			m.put("mku_no_reg", mku_no_reg);
			m.put("keterangan", keterangan);
			this.bacManager.insert_mstkuesioner_hist(mku_no_reg, status +" - "+keterangan, "1", currentUser.getLus_id());
		}else if(data.getSubmit2()!=null){ // search data
			data.setMku_no_reg(data.getMku_noreg().replace("-", ""));
			data = elionsManager.selectkuesioner(data.getMku_no_reg());
			m.put("cmd", data);
			return new ModelAndView("rekruitment/rekrut_keagenan",m);
		}
		/*else if(data.getTools()!=null){
			m.put("currentUser", currentUser);
			m.put("data", this.bacManager.selectkuesionerBy(currentUser.getLus_id(),"1",null));
			return new ModelAndView("rekruitment/rekrut_tools",m);
		}*/
		
		/*if(data.isChbox()==true){//upload dokumen
			String filename = null;
			Integer error = 0;
			StringBuffer err_msg = new StringBuffer();
			ServletRequestDataBinder binders = new ServletRequestDataBinder(data);
			binders.bind(request);
			
			//destiny upload file
			String tDest = props.getProperty("pdf.dir.arthamas.dokumenRegisterAgen");
			File destDir = new File(tDest);
			
			if(!destDir.exists()) destDir.mkdirs();
			
			if(data.getFile1().isEmpty()==false){
				filename = data.getFile1().getOriginalFilename().substring(0,data.getFile1().getOriginalFilename().indexOf("."));
				if(!data.getFile1().getContentType().contains("pdf")){
					error = 1;
					m.put("keterangan", "File Upload PDF bukan format pdf.");
				}
			}
			
			if(error==0){
				//Upload pdf
				if(data.getFile1() != null)
				if(data.getFile1().isEmpty()==false){
					String dest = tDest +"\\"+data.getMku_no_reg_upload()+".pdf";
					File outputFile = new File(dest);
					//Hapus filenya terlebih dahulu jika exist
					try {
						FileUtils.deleteFile(tDest, data.getMku_no_reg_upload()+".pdf");
					} catch (FileNotFoundException e) {
						logger.error("ERROR :", e);
					} catch (IOException e) {
						logger.error("ERROR :", e);
					}
					FileCopyUtils.copy(data.getFile1().getBytes(), outputFile);
					m.put("keterangan", "Upload dokument untuk no rekrut "+data.getMku_no_reg_upload()+" berhasil");

				}
			}else{
				m.put("keterangan", err_msg.toString());
			}
			m.put("mku_no_reg", data.getMku_no_reg_upload());
			m.put("status", "upload");
		}*/
		return new ModelAndView("rekruitment/rekruitmentsubmit",m);
	}

	protected ModelAndView handleInvalidSubmit(HttpServletRequest request,
            HttpServletResponse response)
			throws Exception{
		return new ModelAndView("common/duplicate");
	}
	
}
