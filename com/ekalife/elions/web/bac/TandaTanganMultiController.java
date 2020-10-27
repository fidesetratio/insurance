package com.ekalife.elions.web.bac;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.utils.parent.ParentMultiController;

import com.ekalife.elions.model.FilePdf;
import com.ekalife.elions.model.TandaTangan;
import com.ekalife.elions.model.User;

/**
 * Layar Input SPAJ yang baru
 * BELUM KELAR APA2
 * @author Yusuf Sutarko
 * @since Jan 8, 2008 (10:13:16 AM)
 */
public class TandaTanganMultiController extends ParentMultiController {

	/**
	 * Halaman Utama Tanda Tangan (List Tanda Tangan)
	 * @author Yusuf Sutarko
	 * @since Jan 25, 2008 (2:03:32 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map cmd = new HashMap();
		int lus_id= Integer.parseInt(currentUser.getLus_id());
		List daftarTtg=this.elionsManager.selectDaftarMstTandatangan(lus_id);
		List errorMsg= new ArrayList();
		
		
		if(!daftarTtg.isEmpty()){
			String file="";
			String spaj="";
			List daftarF= new ArrayList();
			for(int x=0; x<daftarTtg.size();x++){
				Map ttd= (Map) daftarTtg.get(x);
				FilePdf f=new FilePdf();
				spaj=(String) ttd.get("REG_SPAJ");
				if(spaj!=null){
					file= products.kategoriPrintSpaj(spaj);
					f.setKey(file);
					f=products.kategoriPosisiPrintSpaj(f);	
					f.setSpaj(spaj);
					daftarF.add(f);
				}
				
			}
			cmd.put("daftarf", daftarF);
		}
		
		if(request.getParameter("save")!=null){
			for(int x=0;x<daftarTtg.size();x++){
				Map TTGMap=  (Map) daftarTtg.get(x);
				String mstt_id=((BigDecimal) TTGMap.get("MSTT_ID")).toString();
				if(request.getParameter("hdFlagUpdate"+mstt_id).equals("1")){
					TandaTangan ttd = new TandaTangan();
					String spaj=request.getParameter("txtSPAJ"+mstt_id);
					List cariMCL=this.elionsManager.selectListMCL_ID(spaj);
					String mcl_id="";	
					String nama=(String) TTGMap.get("MSTT_NAMA");
					
					if(!cariMCL.isEmpty()){
						for(int i=0; i<cariMCL.size();i++){
							Map mcl_idMap= (Map) cariMCL.get(i);
							String mcl_idPP= (String) mcl_idMap.get("MSPO_POLICY_HOLDER");
							String mcl_idTT= (String) mcl_idMap.get("MSTE_INSURED");
							String namapp= (String) mcl_idMap.get("MSCH_NAMA_PP");
							String namatt= (String) mcl_idMap.get("MSCH_NAMA_TT");
							if(nama.equals(namapp) && nama.equals(namatt)){
								mcl_id=mcl_idPP;
							}else if(nama.equals(namapp)){
								mcl_id=mcl_idPP;
							}else if(nama.equals(namatt)){
								mcl_id=mcl_idTT;
							}else{
								errorMsg.add("Nama yang pemilik tanda tangan untuk nomor spaj <b>"+spaj+"</b> yang Anda input di ID TTD <b>"+mstt_id+"</b> tidak sesuai dengan data di database");
							}
						}
						if(errorMsg.isEmpty()){
							ttd.setMcl_id(mcl_id);
							ttd.setReg_spaj(spaj);
							ttd.setMstt_id(mstt_id);
							this.elionsManager.updateMstTandatangan(ttd);
						}
						
					}else{
						errorMsg.add("Input SPAJ dengan nomor <b>"+spaj+"</b> pada ID TTD <b>"+mstt_id+"</b> tidak ada di database");
					}
				}
			}			
		}
		
		if(request.getParameter("btnDownloadSpaj")!=null){
			
			String file="";
			String spaj="";
			FilePdf f=new FilePdf();
			spaj=request.getParameter("spaj");
			if(spaj!=null){
				file= products.kategoriPrintSpaj(spaj);
				f.setKey(file);
				f=products.kategoriPosisiPrintSpaj(f);					
			}
				
			cmd.put("file", f.getKey());
			cmd.put("x", f.getX());
			cmd.put("y", f.getY());
			cmd.put("f", f.getFontSize());
			cmd.put("spaj", spaj);
			cmd.put("download1", 1);
		}
		cmd.put("errorMsg", errorMsg);
		cmd.put("daftarTtg", daftarTtg);
		return new ModelAndView("bac/tanda_tangan/main", cmd);
	}

	/**
	 * Halaman Upload Tanda Tangan
	 * @author Yusuf Sutarko
	 * @since Jan 25, 2008 (2:03:37 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView upload(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		return new ModelAndView("bac/tanda_tangan/upload");
	}
	
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		return new ModelAndView("bac/tanda_tangan/list");
	}
	
	/**
	 * Halaman Preview Tanda Tangan
	 * @author Yusuf Sutarko
	 * @since Jan 25, 2008 (2:04:02 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView preview(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, IOException{
		
		String mstt_id=ServletRequestUtils.getStringParameter(request, "mstt_id", "");
		TandaTangan ttd = elionsManager.selectTandatangan(mstt_id);
		if(ttd!=null){
			response.setContentType("image/jpeg");
			ServletOutputStream out = null;
			try {
				out = response.getOutputStream();
				out.write(ttd.getMstt_image());
				
			} catch (IOException e) {
				logger.error("ERROR :", e);
			}finally{
				out.close();
			}
			
		}
		return null;
	}
	
}
	