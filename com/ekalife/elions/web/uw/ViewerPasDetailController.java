package com.ekalife.elions.web.uw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Pas;
import com.ekalife.elions.model.User;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.parent.ParentController;

public class ViewerPasDetailController extends ParentController{
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//Pas detiledit = (Pas) cmd;
		
		String msp_id=ServletRequestUtils.getStringParameter(request, "msp_id",null);
		String action = request.getParameter("action");
		String no_kartu_new = request.getParameter("no_kartu_new");
		int err = 0;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lus_id   = currentUser.getLus_id();
		if("edit".equals(action)){
			err = uwManager.updateNoKartuPas(msp_id, no_kartu_new, lus_id);
			if(err == 1){
				request.setAttribute("mssg","GAGAL! update no kartu");
			}else if(err == 2){
				request.setAttribute("mssg","GAGAL! paket no kartu tidak sesuai");
			}else{
				request.setAttribute("mssg","SUKSES! update no kartu");
			}
		}
		
		Pas pas=new Pas();
		
		Map<String, Object> refData = new HashMap<String, Object>();
		
		request.setAttribute("kode_nama_list",DroplistManager.getInstance().get("KODE_NAMA.xml","ID",request));
		request.setAttribute("kode_alamat_list",DroplistManager.getInstance().get("KODE_ALAMAT.xml","ID",request));
		request.setAttribute("kode_okupasi_list",DroplistManager.getInstance().get("KODE_OKUPASI.xml","ID",request));
		request.setAttribute("kode_obyek_sekitar_list",DroplistManager.getInstance().get("KODE_OBYEK_SEKITAR.xml","ID",request));
		request.setAttribute("carabayar_pas",DroplistManager.getInstance().get("CARABAYAR_PAS.xml","ID",request));
		request.setAttribute("autodebet_pas",DroplistManager.getInstance().get("AUTODEBET_PAS.xml","ID",request));
		request.setAttribute("relasi_pas",DroplistManager.getInstance().get("RELATION_PAS.xml","ID",request));
		request.setAttribute("produk_pas",DroplistManager.getInstance().get("PRODUK_PAS.xml","ID",request));
		request.setAttribute("sumber_pendanaan",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request));
		request.setAttribute("bidang_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request));
		request.setAttribute("pekerjaan",DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request));
		
		List<Pas> pasList = uwManager.selectAllPasList(msp_id, null, null, null, null, null, null, null,null,null,null);
		pas = pasList.get(0);
		
		String msag_id_pp = pas.getMsag_id_pp();
		
		List sumberPendanaanList = DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request);
		List bidangIndustriList = DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request);
		List pekerjaanList = DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request);
		
		if(msag_id_pp == null){
			for(int i = 0 ; i < sumberPendanaanList.size() ; i++){
				Map map = (Map) sumberPendanaanList.get(i);
				String id = (String) map.get("ID");
				if(id.equals(pas.getMsp_fire_source_fund())){
					pas.setMsp_fire_source_fund2(id);
					break;
				}else{
					pas.setMsp_fire_source_fund2("LAINNYA");
				}
			}
			
			for(int i = 0 ; i < bidangIndustriList.size() ; i++){
				Map map = (Map) bidangIndustriList.get(i);
				String id = (String) map.get("ID");
				if(id.equals(pas.getMsp_fire_type_business())){
					pas.setMsp_fire_type_business2(id);
					break;
				}else{
					pas.setMsp_fire_type_business2("LAINNYA");
				}
			}
			
			for(int i = 0 ; i < pekerjaanList.size() ; i++){
				Map map = (Map) pekerjaanList.get(i);
				String id = (String) map.get("ID");
				if(id.equals(pas.getMsp_fire_occupation())){
					pas.setMsp_fire_occupation2(id);
					break;
				}else{
					pas.setMsp_fire_occupation2("LAIN-LAIN");
				}
			}
			
			CheckSum checkSum = new CheckSum();
			
			if(pas.getPin() != null){
				String lsdbs_number = uwManager.selectCekPin(pas.getPin(), 1);
				if(lsdbs_number == null){
					pas.setProduk(null);
				}else{
					pas.setProduk(Integer.parseInt(lsdbs_number));
				}
			}
		}else{
			// business partner product
			pas.setProduk(5);
		}
		
		String bank_pp1=pas.getLsbp_id();
		String nama_bank_rekclient="";
		if(bank_pp1!=null){
			Map data1= (HashMap) this.elionsManager.select_bank1(bank_pp1);
			if (data1!=null){		
				nama_bank_rekclient = (String)data1.get("BANK_NAMA");
				pas.setLsbp_nama(nama_bank_rekclient);
			}
		}
		
		String bank_pp2=pas.getLsbp_id_autodebet();
		String nama_bank_rekclient_autodebet="";
		if(bank_pp2!=null){
			Map data1= (HashMap) this.elionsManager.select_bank2(bank_pp2);
			if (data1!=null){		
				nama_bank_rekclient_autodebet = (String)data1.get("BANK_NAMA");
				pas.setLsbp_nama_autodebet(nama_bank_rekclient_autodebet);
			}
		}
		
		if(pas.getMsp_pas_beg_date() == null){
			pas.setMsp_pas_beg_date(elionsManager.selectSysdate());
		}
		
//		if(msag_id_pp == null){
//			return new ModelAndView(
//	        "uw/viewer/viewer_pas_detail", "cmd", pas);
//		}else{
//			return new ModelAndView(
//			        "uw/viewer/viewer_pas_detail_partner", "cmd", pas);
//		}
		
		if("AP/BP, AP/BP ONLINE, BP SMS".contains(pas.getJenis_pas())){
			return new ModelAndView(
			        "uw/viewer/viewer_pas_detail_partner", "cmd", pas);
		}else{
			return new ModelAndView(
			        "uw/viewer/viewer_pas_detail", "cmd", pas);
		}

    }
	
}
