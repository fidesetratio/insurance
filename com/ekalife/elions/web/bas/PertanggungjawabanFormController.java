package com.ekalife.elions.web.bas;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.FormHist;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Controller untuk pertanggungjawaban setiap blanko spaj
 * digunakan di modul sistem kontrol spaj
 * 
 * @author Yusuf Sutarko
 * @since Mar 30, 2007 (9:23:47 AM)
 */
public class PertanggungjawabanFormController extends ParentFormController {

	private Map daftarWarna;
	private Map daftarWarnaAgen;
	private List<Map> daftarJenisSpaj;
	
	public void setDaftarJenisSpaj(List<Map> daftarJenisSpaj) {
		this.daftarJenisSpaj = daftarJenisSpaj;
	}

	public void setDaftarWarnaAgen(Map daftarWarnaAgen) {
		this.daftarWarnaAgen = daftarWarnaAgen;
	}

	public void setDaftarWarna(Map daftarWarna) {
		this.daftarWarna = daftarWarna;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Integer flag=ServletRequestUtils.getIntParameter(request, "flag",0);
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		CommandControlSpaj cmd = new CommandControlSpaj();
		cmd.setFormHist(new FormHist());
		//khusus untuk pertanggungjawaban tidak berdasarkan msf_id
		if(flag==1){
			String msagId=ServletRequestUtils.getStringParameter(request, "msag_id");
			String lcaId=ServletRequestUtils.getStringParameter(request, "lca_id");
			cmd.setMsf_id(elionsManager.selectMsfIdPertanggungjawaban(msagId, lcaId));
		}
		else if(flag == 2) {
			// tarik data semua no blanko dari seorang agent yg status nya 'di agen'
			String msagId = ServletRequestUtils.getStringParameter(request, "msag_id");
			String msabId = ServletRequestUtils.getStringParameter(request, "msab_id");
			
			cmd.setDaftarPertanggungjawaban(elionsManager.selectUpdatePertanggungjawaban(currentUser.getLus_id(),msagId,msabId));
			cmd.setSize(cmd.getDaftarPertanggungjawaban().size());
			cmd.setDaftarJenisPertanggungjawaban(elionsManager.selectJenisPertanggungjawaban());
			return cmd;
		}
		else
			cmd.setMsf_id(ServletRequestUtils.getRequiredStringParameter(request, "msf_id"));
		//cek untuk pertanggungjawaban harus di user asal..
		cmd.setProses(elionsManager.selectCekAgenInUserAdmin(currentUser.getLus_id(), cmd.getMsf_id()));
		
		cmd.setDaftarPertanggungjawaban(elionsManager.selectPertanggungjawaban(cmd.getMsf_id()));
		cmd.setSize(cmd.getDaftarPertanggungjawaban().size());
		//
		cmd.setDaftarJenisPertanggungjawaban(elionsManager.selectJenisPertanggungjawaban());
		cmd.setDaftarFormSpaj(elionsManager.selectFormSpaj(cmd.getMsf_id(), currentUser.getLca_id(), currentUser.getLus_id()));
		Agen agen=new Agen();
		String msabId=request.getParameter("msab_id");
		if(msabId!=null && !(msabId.equals("")))
			agen.setMsab_id(Integer.valueOf(msabId));
		if(ServletRequestUtils.getStringParameter(request, "m","").equals("m")) {
			cmd.setPosisi(6);
		}else {
			cmd.setPosisi(elionsManager.selectCekPosisiFormSpaj(cmd.getMsf_id()));
		}
		return cmd;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		CommandControlSpaj cmd = (CommandControlSpaj) command;
		String sukses = elionsManager.processFormSpaj(
				(CommandControlSpaj) command, 
				(User) request.getSession().getAttribute("currentUser"));
		cmd.setPosisi(elionsManager.selectCekPosisiFormSpaj(cmd.getMsf_id()));
		return new ModelAndView("bas/kontrol_tgjwb", errors.getModel()).addObject("sukses", sukses);
	}
	
}