package com.ekalife.elions.web.blanko;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.FormSpaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;

/**
 * Controller untuk sistem kontrol KERTAS POLIS (pemakaian KERTAS POLIS)
 * User berkaitan adalah GA sebagai penyedia barang, BAS sebagai perantara cabang,
 * dan branch admin untuk menghandle permintaan KERTAS POLIS dari agen
 * 
 * @author Hemilda Sari Dewi
 * @since Feb 19, 2007 (2:13:43 PM)
 */
public class BlankoMonitoringMultiController extends ParentMultiController{
	
	private Map daftarWarna;

	public void setDaftarWarna(Map daftarWarna) {
		this.daftarWarna = daftarWarna;
	}
	
	/**
	 * Halaman innerFrame untuk approval KERTAS POLIS, isinya daftar form yang direquest oleh cabang tertentu 
	 * 
	 * @author Hemilda Sari Dewi
	 * @since Feb 19, 2007 (2:45:13 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView daftarForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		String lca_id = ServletRequestUtils.getStringParameter(request, "lca_id", "");
		int posisi = ServletRequestUtils.getIntParameter(request, "posisi", 99);
		
		if(!"".equals(lca_id)) {
			FormSpaj formSpaj = new FormSpaj();
			//User currentUser = (User) request.getSession().getAttribute("currentUser");
			formSpaj.setMss_jenis(2);
			if(!lca_id.equals("ALL_BRANCH")) formSpaj.setLca_id(lca_id);
			formSpaj.setMsab_id(0);		
			formSpaj.setPosisi(posisi);
			List<FormSpaj> daftar = elionsManager.selectDaftarFormSpaj(formSpaj);
			if(daftar.isEmpty()) {
				cmd.put("pesan", "Tidak ada permintaan untuk cabang ini");
			}else {
				for(FormSpaj f : daftar) {
					f.setWarna((String) daftarWarna.get(f.getStatus_form()));
				}
			}
			cmd.put("daftarForm", daftar);
		}else {
			cmd.put("pesan", "Silahkan pilih salah satu cabang terlebih dahulu");
		}

		return new ModelAndView("blanko/daftar_form", "cmd", cmd);
	}
	
	/**
	 * @author Hemilda Sari Dewi
	 * @since Feb 19, 2007 (2:45:13 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView lappemakaian(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String user_name = currentUser.getName();
		String lca_id = currentUser.getLca_id();
		String user_id = currentUser.getLus_id();
		//response.sendRedirect("../report/uw.pdf?window=lap_pemakaian&show=pdf&user="+user_name+"&lca_id="+lca_id);
		return new ModelAndView(new RedirectView("../report/uw.pdf?window=lap_pemakaian&show=pdf?window=lap_pemakaian&show=pdf&user="+user_name+"&user_id="+user_id));
	}
	
}