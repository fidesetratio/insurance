/**
 * @author  : Ryan F
 * @created : May 03, 2011, 8:37:46 AM
 * 
 * Ini merupakan Controller buat Endorse Non Material Bancass II req Novie.
 */
package com.ekalife.elions.web.uw;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Pemegang;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.model.ViewPolis;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.f_hit_umur;
import com.ekalife.utils.parent.ParentMultiController;

public class EndorseNonMaterialMultiController extends ParentMultiController {
	
	// Halaman Depan dari Endorse (Tampilan Awal).
	public ModelAndView endorseNonMaterial (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		ViewPolis viewPolis = new ViewPolis();
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String mspoPolicyHolder = elionsManager.selectpp(spaj).getMspo_policy_holder();
		Map docPos = elionsManager.selectViewerDocPosition(spaj);
		Pemegang pmg=elionsManager.selectpp(spaj);
		String ldsBisnis=uwManager.kodeProductUtama(spaj);
		//Header
		String posisi =elionsManager.selectLstDocumentPosition(spaj);
		String polis =FormatString.nomorPolis(pmg.getMspo_policy_no());
		String nama_pp = pmg.getMcl_first();
		Date sysdate = elionsManager.selectSysdateSimple();
		//Data Untuk Pemegang Polis
		String blanko = elionsManager.selectBlanko(spaj);
		Integer month=sysdate.getMonth()+1;
		if(!currentUser.getLde_id().equals("11")){
		    map.put("main", "true");
	     }
		if(!ldsBisnis.equals("164")&&!ldsBisnis.equals("142")&&!ldsBisnis.equals("143")&&!ldsBisnis.equals("188")&&!ldsBisnis.equals("158")&&!ldsBisnis.equals("175")){
			map.put("product", "true");
		}
		map.put("posisi", posisi);
		map.put("pemegang", pmg);
		map.put("polis", polis);
		//map untuk tertanggung
	   return new ModelAndView("uw/endorsenonmaterial",map);
	}
	
	// Halaman Untuk Proses Blanko
	public ModelAndView editblanko (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		String alasan = ServletRequestUtils.getStringParameter(request, "alasan", "").toUpperCase();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String blanko = ServletRequestUtils.getStringParameter(request, "newBlanko","").toUpperCase();
		Pemegang pemegang=elionsManager.selectpp(spaj);
		List<String> error = new ArrayList <String>();
		if(currentUser.getLde_id().equals("11")){
			map.put("editBlanko", "true");
		}
				if(request.getParameter("save")!= null) 
						if(alasan.equals("") || blanko.equals("")) {
							error.add("Harap Isi Field Yang Kosong Terlebih Dahulu");
						}
						else{
							//String no =uwManager.noEndor(pemegang.getLca_id());
							String no =uwManager.endorseNonMaterial(spaj, Integer.parseInt(currentUser.getLus_id()), alasan,blanko,60,currentUser,null,null,null,null);
							pemegang.setMspo_no_blanko(blanko);
							error.add("Berhasil Disimpan Dengan No : " + no);
						}
				if(!error.equals("")){
					map.put("pesanError", error);
				}
	    map.put("pesanErr", error);
		map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
		map.put("pemegang", pemegang);
		return new ModelAndView("uw/editblanko",map); 
	
}

	//Halaman Untuk Proses Edit Nama Pemegang
	public ModelAndView editnamapemegang (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		Date ldt_now;
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		ldt_now = elionsManager.selectSysdateSimple();
		String alasan = ServletRequestUtils.getStringParameter(request, "alasan","").toUpperCase();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String mcl_id = elionsManager.selectpp(spaj).getMspo_policy_holder();
		String mcl_first= ServletRequestUtils.getStringParameter(request, "mcl_first","").toUpperCase();
		Pemegang pemegang =elionsManager.selectpp(spaj);
		List<String> error = new ArrayList<String>();
	     	if(currentUser.getLde_id().equals("11")){
			    map.put("editPemegang", "true");
		     }
	     	if(request.getParameter("save")!= null) 
				if(alasan.equals("") || mcl_first.equals("")) {
					error.add("Harap Isi Field Yang Kosong Terlebih Dahulu");
				}
				else{
					String no =uwManager.endorseNonMaterial(spaj, Integer.parseInt(currentUser.getLus_id()), alasan, mcl_first,2,currentUser,null,null,null,null);
					pemegang.setMcl_first(mcl_first);
					error.add("Berhasil Disimpan Dengan No : " + no);
				}
		if(!error.equals("")){
			map.put("pesanError", error);
		}
		map.put("err", error);
		map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
		map.put("pemegang", pemegang);
		return new ModelAndView("uw/editnamapemegang",map); 
		
	}
	
	public ModelAndView editnamatertanggung (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		Date ldt_now;
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		ldt_now = elionsManager.selectSysdateSimple();
		String alasan = ServletRequestUtils.getStringParameter(request, "alasan","").toUpperCase();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String mcl_id = elionsManager.selectpp(spaj).getMspo_policy_holder();
		String mcl_first= ServletRequestUtils.getStringParameter(request, "mcl_first","").toUpperCase();
		Pemegang pemegang =elionsManager.selectpp(spaj);
		Tertanggung tertanggung =elionsManager.selectttg(spaj);
		List<String> error = new ArrayList<String>();
		
			if(currentUser.getLde_id().equals("11")){
			    map.put("editTertanggung", "true");
		     }
	     	if (pemegang.getLsre_id().equals(1)){
				map.put("hak", "true");
			}
	     	if(request.getParameter("save")!= null) 
				if(alasan.equals("") || mcl_first.equals("")){
					error.add("Harap Isi Field Yang Kosong Terlebih Dahulu");
				}
				else{
					String no =uwManager.endorseNonMaterial(spaj, Integer.parseInt(currentUser.getLus_id()), alasan, mcl_first,1,currentUser,null,null,null,null);
					tertanggung.setMcl_first(mcl_first);
					error.add("Berhasil Disimpan Dengan No : " + no);
				}
		if(!error.equals("")){
			map.put("pesanError", error);
		}
		
		map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
		map.put("tertanggung", tertanggung);
		return new ModelAndView("uw/editnamatertanggung",map); 
		
	}
	
	//Tgl lahir Pemegang
	public ModelAndView edittglpemegang (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		f_hit_umur umr= new f_hit_umur();
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		String alasan = ServletRequestUtils.getStringParameter(request, "alasan","").toUpperCase();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		String mcl_id = elionsManager.selectpp(spaj).getMspo_policy_holder();
		String tgl= ServletRequestUtils.getStringParameter(request, "tanggalPemegang","");
		String tempat= ServletRequestUtils.getStringParameter(request, "mspe_place_birth","").toUpperCase();
		//Integer umur= ServletRequestUtils.getIntParameter(request, "mspo_age");
		Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
		Pemegang pemegang = elionsManager.selectpp(spaj);
			
			//String aplikasi = tempat+", "+tgl+" ("+li_umur_pp+ " Tahun)";
				List<String> error = new ArrayList<String>();
				if(currentUser.getLde_id().equals("11")){
				    map.put("editTglP", "true");
			     }
		     	if(request.getParameter("save")!= null) 
					if(alasan.equals("") || tgl.equals("")) {
						error.add("Harap Isi Field Yang Kosong Terlebih Dahulu");
					}
					/*else if(umur  69){
						error.add("Umur Diatas 69 tahun atau berumur 69 tahun, Tidak bisa Diubah...!!");*/
					else{
						//uwManager.updateUmurPDanTgl(spaj, umur,mcl_id, tgl,tempat);
						String no =uwManager.endorseNonMaterial(spaj, Integer.parseInt(currentUser.getLus_id()), alasan,null,11,currentUser,tgl,tempat,null,null);
						Pemegang pemegang2 = elionsManager.selectpp(spaj);
						Integer tanggal1=new Integer(0);
						Integer bulan1=new Integer(0);
						Integer tahun1=new Integer(0);
						Integer tanggal3=new Integer(0);
						Integer bulan3=new Integer(0);
						Integer tahun3=new Integer(0);
						Integer li_umur_pp=new Integer(0);
						tanggal1= dataUsulan.getMste_beg_date().getDate();
						bulan1 = dataUsulan.getMste_beg_date().getMonth()+1;
						tahun1 = dataUsulan.getMste_beg_date().getYear()+1900;
						tanggal3=pemegang2.getMspe_date_birth().getDate();
						bulan3=pemegang2.getMspe_date_birth().getMonth()+1;
						tahun3=pemegang2.getMspe_date_birth().getYear()+1900;
						li_umur_pp = umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
						pemegang.setMspo_age(li_umur_pp);
						pemegang.setMspe_place_birth(tempat);
						if(!tgl.equals("")){
							SimpleDateFormat dateFormatDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
							Date dt = dateFormatDDMMYYYY.parse(tgl);
							pemegang.setMspe_date_birth(dt);}
						error.add("Berhasil Disimpan Dengan No : " + no);
					}
		     	if(!error.equals("")){
					map.put("pesanError", error);
				}
			map.put("pemegang", pemegang);
			map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
			return new ModelAndView("uw/edittglpemegang", map);
	}
	
	
	public ModelAndView edittgltanggung (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		f_hit_umur umr= new f_hit_umur();
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		String alasan = ServletRequestUtils.getStringParameter(request, "alasan","").toUpperCase();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		String mcl_id = elionsManager.selectttg(spaj).getMcl_id();
		String tgl= ServletRequestUtils.getStringParameter(request, "tanggalTanggung","");
		//Integer umur= ServletRequestUtils.getIntParameter(request, "mste_age");
		String tempat= ServletRequestUtils.getStringParameter(request, "mspe_place_birth","").toUpperCase();
		//String aplikasi = tempat+", "+tgl+" ("+umur+ " Tahun)";
		Tertanggung tertanggung = elionsManager.selectttg(spaj);
		Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
			List<String> error = new ArrayList<String>();
			if(currentUser.getLde_id().equals("11")){
			    map.put("editTglT", "true");
		     }
			if (tertanggung.getLsre_id().equals(1)){
				map.put("hak", "true");
			}
	     	if(request.getParameter("save")!= null) 
				if(alasan.equals("") || tgl.equals("")) {
					error.add("Harap Isi Field Yang Kosong Terlebih Dahulu");
				}
				else{
					//uwManager.updateUmurTDanTgl(spaj, umur,mcl_id, tgl,tempat);
					String no =uwManager.endorseNonMaterial(spaj, Integer.parseInt(currentUser.getLus_id()), alasan,null,10,currentUser,tgl,tempat,null,null);
					Tertanggung tertanggung2 = elionsManager.selectttg(spaj);
					Integer tanggal1=new Integer(0);
					Integer bulan1=new Integer(0);
					Integer tahun1=new Integer(0);
					Integer tanggal3=new Integer(0);
					Integer bulan3=new Integer(0);
					Integer tahun3=new Integer(0);
					Integer li_umur_tt=new Integer(0);
					tanggal1= dataUsulan.getMste_beg_date().getDate();
					bulan1 = dataUsulan.getMste_beg_date().getMonth()+1;
					tahun1 = dataUsulan.getMste_beg_date().getYear()+1900;
					tanggal3=tertanggung2.getMspe_date_birth().getDate();
					bulan3=tertanggung2.getMspe_date_birth().getMonth()+1;
					tahun3=tertanggung2.getMspe_date_birth().getYear()+1900;
					li_umur_tt = umr.umur(tahun3,bulan3,tanggal3,tahun1,bulan1,tanggal1);
					tertanggung.setMste_age(li_umur_tt);
					tertanggung.setMspe_place_birth(tempat);
						if(!tgl.equals("")){
						SimpleDateFormat dateFormatDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
						Date dt = dateFormatDDMMYYYY.parse(tgl);
						tertanggung.setMspe_date_birth(dt);}
					error.add("berhasil Disimpan Dengan No : " + no);
				}
	     	if(!error.equals("")){
				map.put("pesanError", error);
			}
		map.put("tertanggung", tertanggung);
		map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
		return new ModelAndView("uw/edittgltanggung", map);
	}
	
	public ModelAndView endorse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		List allEndorse = this.uwManager.selectAllEndorse(spaj);
		if(allEndorse.isEmpty()) {
			PrintWriter out = response.getWriter();
			out.println("<script>alert('Tidak Ada Proses Endors !!');window.close();</script>");
			out.close();
			return null;
		}else {
		map.put("endorseList", allEndorse);
		map.put("endorseInfo", this.elionsManager.selectInfoEndorse(
				ServletRequestUtils.getStringParameter(request, "endorseno", (String) ((HashMap) allEndorse.get(0)).get("MSEN_ENDORS_NO"))).get(0));
		return new ModelAndView("uw/viewer/print_endors", "cmd", map);
	}
  }
	
	public ModelAndView editalamattertanggung (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		String alasan = ServletRequestUtils.getStringParameter(request, "alasan","").toUpperCase();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String alamatT= ServletRequestUtils.getStringParameter(request, "alamatT","").toUpperCase();
	    String kotaT= ServletRequestUtils.getStringParameter(request, "kotaT","").toUpperCase();
	    String KdPosT= ServletRequestUtils.getStringParameter(request, "kdPosT","");
		Pemegang pemegang =elionsManager.selectpp(spaj);
		Tertanggung tertanggung =elionsManager.selectttg(spaj);
		String aplikasi = alamatT+" , "+kotaT+" ( Kode Pos :" + KdPosT + " )";
		List<String> error = new ArrayList<String>();
		
			if(currentUser.getLde_id().equals("11")){
			    map.put("editTertanggung", "true");
		     }
	     	if (pemegang.getLsre_id().equals(1)){
				map.put("hak", "true");
			}
	     	if(request.getParameter("save")!= null) 
	     		if(alamatT.equals("")||kotaT.equals("")||KdPosT.equals("")||alasan.equals("")){
					error.add("Harap Isi Field Yang Kosong Terlebih Dahulu");
				}
				else{
					String no =uwManager.endorseNonMaterial(spaj, Integer.parseInt(currentUser.getLus_id()), alasan,aplikasi,24,currentUser,alamatT,kotaT,KdPosT,null);
					tertanggung.setAlamat_rumah(alamatT);
					tertanggung.setKota_rumah(kotaT);
					tertanggung.setKd_pos_rumah(KdPosT);
					error.add("Berhasil Disimpan Dengan No : " + no);
				}
		if(!error.equals("")){
			map.put("pesanError", error);
		}
		
		map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
		map.put("tertanggung", tertanggung);
		return new ModelAndView("uw/addresstertanggung",map); 
	}
	
	public ModelAndView addressbilling (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		String alasan = ServletRequestUtils.getStringParameter(request, "alasan","").toUpperCase();
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
		String alamatB= ServletRequestUtils.getStringParameter(request, "alamatB","").toUpperCase();
	    String kotaB= ServletRequestUtils.getStringParameter(request, "kotaB","").toUpperCase();
	    String KdPosB= ServletRequestUtils.getStringParameter(request, "kdPosB","");
		Pemegang pemegang =elionsManager.selectpp(spaj);
		Tertanggung tertanggung =elionsManager.selectttg(spaj);
		AddressBilling ab 	= elionsManager.selectAddressBilling(spaj);
		String aplikasi = alamatB+" , "+kotaB+" "+ KdPosB;
		List<String> error = new ArrayList<String>();
		
			if(currentUser.getLde_id().equals("11")){
			    map.put("editTertanggung", "true");
		     }
	     	if (pemegang.getLsre_id().equals(1)){
				map.put("hak", "true");
			}
	     	if(request.getParameter("save")!= null) 
	     		if(alamatB.equals("")||kotaB.equals("")||KdPosB.equals("")||alasan.equals("")){
					error.add("Harap Isi Field Yang Kosong Terlebih Dahulu");
				}
				else{
					String no =uwManager.endorseNonMaterial(spaj, Integer.parseInt(currentUser.getLus_id()), alasan,aplikasi,4,currentUser,alamatB,kotaB,KdPosB,null);
					ab.setMsap_address(alamatB);
					ab.setMsap_zip_code(KdPosB);
					ab.setKota(kotaB);
					error.add("Berhasil Disimpan Dengan No : " + no);
				}
		if(!error.equals("")){
			map.put("pesanError", error);
		}
		
		map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
		map.put("ab", ab);
		return new ModelAndView("uw/alamatpenagihan",map); 
	}
	
	public ModelAndView infomedis (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		String medis = ServletRequestUtils.getStringParameter(request, "medis","");
		String alasan = ServletRequestUtils.getStringParameter(request, "alasan","").toUpperCase();
		Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
		Integer reas = uwManager.selectMst_reas(spaj);
		List<String> error = new ArrayList<String>();
     		if (reas != 0){
     			map.put("hak", "true");
			}
     		if(request.getParameter("save")!= null) 
	     		if(alasan.equals("")){
					error.add("Harap Isi Field Yang Kosong Terlebih Dahulu");
				}
				else{
					uwManager.updateflagMedis(spaj, medis, currentUser, alasan);
					dataUsulan.setMste_medical(Integer.parseInt(medis));
					error.add("Berhasil Disimpan ");
				}
			if(!error.equals("")){
				map.put("pesanError", error);
			}
		map.put("dataUsulan", dataUsulan);
		map.put("select_medis",DroplistManager.getInstance().get("medis.xml","ID",request));
		map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
		return new ModelAndView("uw/editinfomedis", map);
	}
	
	public ModelAndView editklarifikasi (HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map map = new HashMap();
		HttpSession session = request.getSession();
		User currentUser = (User)session.getAttribute("currentUser");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		String penghasilan = ServletRequestUtils.getStringParameter(request, "penghasilan","");
		String penghasilanT = ServletRequestUtils.getStringParameter(request, "penghasilanT","");
		String mamah = ServletRequestUtils.getStringParameter(request, "mamah","").toUpperCase();
		String mamahT = ServletRequestUtils.getStringParameter(request, "mamahT","").toUpperCase();
		Datausulan dataUsulan = elionsManager.selectDataUsulanutama(spaj);
		Pemegang pemegang =elionsManager.selectpp(spaj);
		Tertanggung tertanggung =elionsManager.selectttg(spaj);
		Integer reas = uwManager.selectMst_reas(spaj);
		List<String> error = new ArrayList<String>();
     		/*if (reas != 0){
     			map.put("hak", "true");
			}*/
     		if(request.getParameter("save")!= null) {
     				uwManager.updatePenghasilan(mamahT, penghasilan,spaj,currentUser,mamah);
     				pemegang.setMkl_penghasilan(penghasilan);
     				pemegang.setMspe_mother(mamah);
     				tertanggung.setMkl_penghasilan(penghasilanT);
     				tertanggung.setMspe_mother(mamahT);
					error.add("Berhasil Disimpan ");
     		}
				
			if(!error.equals("")){
				map.put("pesanError", error);
			}
		map.put("dataUsulan", dataUsulan);
		map.put("pemegang", pemegang);
		map.put("tertanggung",tertanggung);
		map.put("select_penghasilan",DroplistManager.getInstance().get("PENGHASILAN.xml","",request));
		map.put("select_dana",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
		map.put("select_hasil",DroplistManager.getInstance().get("SUMBER_PENGHASILAN.xml","",request));
		map.put("select_tujuan",DroplistManager.getInstance().get("TUJUAN_ASR.xml","",request));
		map.put("infoDetailUser", elionsManager.selectInfoDetailUser(Integer.valueOf(currentUser.getLus_id())));
		return new ModelAndView("uw/klarifikasi", map);
	}
	
	
	/*public ModelAndView cam (HttpServletRequest request, HttpServletResponse response) throws Exception{
		String type = request.getParameter("type");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj","");
		Map map = new HashMap();
		String cabang = elionsManager.selectCabangFromSpaj(spaj);
		String exportDirectory = props.getProperty("pdf.dir.export")+"\\"+cabang+"\\"+spaj;
		File sourceFile = new File(exportDirectory+"\\"+"softcopy.pdf");
		if (type != null) {
			try{ 
	            // remove data:image/png;base64, and then take rest sting 
	        	String image = request.getParameter("image");
	        	BASE64Decoder decoder = new BASE64Decoder(); 
	        	byte[] decodedBytes = decoder.decodeBuffer(image); 

			    BufferedImage bfi = ImageIO.read(new ByteArrayInputStream(decodedBytes));     
			    File outputfile = new File(exportDirectory+"tes.png"); 
			    ImageIO.write(bfi , "png", outputfile); 
			    bfi.flush(); 
			 }catch(Exception e){   
			      //Implement exception code     
			 } 
		}
		

		//uwManager.schedulerSimpolExpired(null);
		return new ModelAndView("uw/viewer/cam", map);
	}*/
}