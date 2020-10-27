package com.ekalife.elions.web.cross_selling;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.elions.model.cross_selling.CrossSelling;
import com.ekalife.elions.model.cross_selling.PolicyCs;
import com.ekalife.utils.parent.ParentMultiController;

/**
 * Controller untuk semua menu yang berhubungan cross selling antara AJS dan ASM
 * 
 * @author Yusuf
 * @since Jul 18, 2008 (3:38:28 PM)
 */
public class CrossSellingMultiController extends ParentMultiController {

	/**
	 * Menu untuk konfirmasi pembayaran oleh BAS, diproses ini juga selain mengupdate tanggal bayar, dihitung juga komisi, produksi, upp, etc.
	 * 
	 * @author Yusuf
	 * @since Aug 4, 2008 (4:43:00 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView konfirmasi(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		HttpSession session = request.getSession();
		CrossSelling cmd = new CrossSelling();
		cmd.setDaftarSpaj(new ArrayList<PolicyCs>());
		for(int i=0; i<ServletRequestUtils.getIntParameter(request, "ukuran", 0); i++) {
			cmd.daftarSpaj.add(new PolicyCs());
		}

		if(request.getParameter("search") != null) {
			cmd.setDaftarSpaj(elionsManager.selectDaftarCrossSelling(82, cmd.reg_spaj, cmd.mscs_holder, cmd.startDate, cmd.endDate));
			request.setAttribute("ukuran", cmd.daftarSpaj.size());
		}else if(request.getParameter("save") != null) {
			ServletRequestDataBinder binder = new ServletRequestDataBinder(cmd, "cmd");
			binder.registerCustomEditor(Date.class, null, dateEditor);
			binder.bind(request);
			
			//validasi
			String error = "";
			for(PolicyCs pc : cmd.daftarSpaj) {
				if(pc.selected!=null) {
					if(pc.selected.intValue() == 1) {
						if(pc.mscs_tgl_terima_asm == null) {
							error = "Untuk setiap SPAJ yang dipilih, harap masukkan TANGGAL TERIMA ASM";
							break;
						}
					}
				}
			}
			
			if(!error.equals("")) {
				request.setAttribute("pesan", error);
			}else {
				User currentUser = (User) request.getSession().getAttribute("currentUser");
				String pesan = elionsManager.saveKonfirmasiCrossSelling(cmd.daftarSpaj, Integer.valueOf(currentUser.getLus_id()));
				cmd.setDaftarSpaj(elionsManager.selectDaftarCrossSelling(82, cmd.reg_spaj, cmd.mscs_holder, cmd.startDate, cmd.endDate));
				request.setAttribute("ukuran", cmd.daftarSpaj.size());
				request.setAttribute("pesan", pesan);
			}
		}
		return new ModelAndView("cross_selling/konfirmasi", "cmd", cmd);
	}
	
	/**
	 * Menu untuk searching nomor cross selling
	 * 
	 * @author Yusuf
	 * @since Jul 21, 2008 (8:25:14 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView cari(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		String tipe = ServletRequestUtils.getStringParameter(request, "tipe", "");
		String kata = ServletRequestUtils.getStringParameter(request, "kata", "");
		
		Map cmd = new HashMap();
		cmd.put("tipe", tipe);
		cmd.put("kata", kata);
		
		if(!tipe.equals("") && !kata.equals("")) cmd.put("listSpaj", elionsManager.selectCariCrossSelling(tipe, kata));
		
		return new ModelAndView("cross_selling/cari", cmd);
	}
	
	/**
	 * cari komisi cross selling
	 * 
	 * @author Canpri
	 * @since Feb 19, 2014 (8:25:14 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletRequestBindingException
	 */
	public ModelAndView cari_komisi(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException{
		Map cmd = new HashMap();
		
		if(request.getParameter("btnCari") != null) {
			String cari = ServletRequestUtils.getStringParameter(request, "cari_komisi", "");
			
			if(!cari.equals("")){
				List<Map> komisi = bacManager.selectKomisiCrossSelling(cari);
			
				cmd.put("komisi", komisi);
			}else{
				cmd.put("pesan", "Harap masukkan nama pemegang/no polis!");
			}
		}
			
		return new ModelAndView("cross_selling/cari_komisi", cmd);
	}
}