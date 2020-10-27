package com.ekalife.elions.web.uw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Pas;
import com.ekalife.utils.CheckSum;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.parent.ParentController;

public class ViewerBpOnlineDetailController extends ParentController{
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Pas pas=new Pas();
        String msp_id=ServletRequestUtils.getStringParameter(request, "msp_id",null);
        
        // menentukan pas save langsung muncul konfirmasi transfer ke uw pada pas_detail
        String flag_posisi=ServletRequestUtils.getStringParameter(request, "flag_posisi",null);
        request.setAttribute("flag_posisi", flag_posisi);
        
        
        Map<String, Object> refData = new HashMap<String, Object>();
        //schedulerPas();
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
        request.setAttribute("select_agama", elionsManager.selectDropDown("eka.lst_agama", "lsag_id", "lsag_name", "", "lsag_name", null));
        request.setAttribute("gelar_company", elionsManager.selectGelar(1));
        request.setAttribute("bidang_usaha", elionsManager.selectBidangUsaha(0));
        request.setAttribute("select_negara", elionsManager.selectDropDown("eka.lst_negara", "lsne_id", "lsne_note", "", "lsne_urut", null));
        request.setAttribute("select_pendidikan", elionsManager.selectDropDown("eka.lst_education", "lsed_id", "lsed_name", "", "lsed_name", null));
        request.setAttribute("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
        request.setAttribute("select_pekerjaan",DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request));
        request.setAttribute("select_identitas", elionsManager.selectDropDown("eka.lst_identity", "lside_id", "lside_name", "", "lside_id", null));
//        String jenisQuery;
//        if(currentUser.getLde_id().equals("29") && currentUser.getLca_id().equals("01")){
//            jenisQuery = "bpgetbp";
//        }else{
//            jenisQuery = "partner";    
//        }
        List<Pas> pasList = uwManager.selectAllPasList(msp_id, null, null, null, null, null, null, null, null,null,null);
        pas = pasList.get(0);
        if("BPGETBP".equals(pas.getJenis_pas())){
            String msag_id = uwManager.selectKodeAgentFromMstKusioner(pas.getMsp_fire_id());
            pas.setMsag_id_pp(msag_id);
            pas.setMsag_id(msag_id);
        }
        pas.setKode_ao(pas.getMsag_id());
        
        pas.setLscb_id(3);
        pas.setLsre_id(1);
    //    pas.setMsp_flag_cc(2);
        //pas.setPribadi(1);
        // pas.setProduk(5);
            pas.setJenis_bp(0);            
        
        List sumberPendanaanList = DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","ID",request);
        List bidangIndustriList = DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","ID",request);
        List pekerjaanList = DroplistManager.getInstance().get("PEKERJAAN_PAS.xml","ID",request);
        
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
            //if(lsdbs_number == null)lsdbs_number = "x";
            pas.setProduk(Integer.parseInt(lsdbs_number));
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
        
        return new ModelAndView(
                "uw/viewer/viewer_bp_online_detail", "cmd", pas);
        
    }
	
}
