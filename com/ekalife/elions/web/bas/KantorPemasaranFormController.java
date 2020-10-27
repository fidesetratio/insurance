package com.ekalife.elions.web.bas;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.AddressRegion;
import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FileUtils;
import com.ekalife.utils.parent.ParentFormController;

/**
 * Modul controller untuk Branch Support dan CS Branch dapat mengetahui dan memfollow up 
 * informasi mengenai kantor pemasaran
 * 
 * digunakan oleh bas
 * http://localhost/E-Lions/bas/kantor_pemasaran.htm
 * 
 * (package com.ekalife.elions.web.bas)
 * @author Canpri
 * @since Apr 14, 2014 (10:21:23 AM)
 */
public class KantorPemasaranFormController extends ParentFormController {

	private Map daftarWarna;
	private Map daftarWarnaAgen;
	private List<Map> daftarJenisSpaj;
	private List<Map> daftarJenisBrosur;
	
	public void setDaftarJenisBrosur(List<Map> daftarJenisBrosur) {
		this.daftarJenisBrosur = daftarJenisBrosur;
	}
	
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
		AddressRegion cmd = new AddressRegion();
		
		//bacManager.schedulerSuratDomisili();
		
		return cmd;
	}

	@Override
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		String submitMode = ServletRequestUtils.getStringParameter(request, "submitMode", "");
		if("show".equals(submitMode) || "history".equals(submitMode)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		AddressRegion cmd = (AddressRegion) command;
		if("show".equals(cmd.getSubmitMode()) || "history".equals(cmd.getSubmitMode())) {
			//request.setAttribute("infoMessage", "Silahkan masukkan keterangan pengiriman diatas.");				
			cmd.setDaftarKantor(elionsManager.selectAddressRegionLarId(cmd.getLar_id().toString()));
			
			//cmd.setCabang(uwManager.selectNamaCabang((String)cmd.getDaftarKantor().get(0).get("LAR_LCA_ID")));
			cmd.setLar_lca_id((String)cmd.getDaftarKantor().get(0).get("LAR_LCA_ID"));
			cmd.setCabang(elionsManager.selectLstCabangNamaCabang((String)cmd.getDaftarKantor().get(0).get("LAR_LCA_ID")));
			
			if("history".equals(cmd.getSubmitMode()))cmd.setHistory(bacManager.selectHistoryUpdateRegion(cmd.getLar_id().toString(), "asc"));
			else cmd.setHistory(bacManager.selectHistoryUpdateRegion(cmd.getLar_id().toString(), "desc"));
			
			//load attachment
			String directory = props.getProperty("pdf.dir.surat_domisili")+"\\"+cmd.getLar_id()+"\\";
			List<DropDown> dokumen = FileUtils.listFilesInDirectory(directory); //FileUtils.listFilesInDirectory(directory);
			List<Map> dok = new ArrayList<Map>();
			
			if(!dokumen.isEmpty()){
				for(int i=0;i<dokumen.size();i++){
					DropDown dc = dokumen.get(i);
					Map m = new HashMap();
					m.put("dok", dc.getKey());
					dok.add(m);
				}
			}
			cmd.setLs_attachment(dok);
			
			cmd.setLar_admin((String)cmd.getDaftarKantor().get(0).get("LAR_ADMIN"));
			cmd.setLar_nama((String)cmd.getDaftarKantor().get(0).get("LAR_NAMA"));
		}
		
		//cek akses user untuk cabang yang dipilih
		Integer cek_akses = bacManager.selectValidAdminKantorPemasaran(currentUser.getLus_id(), cmd.getLar_id().toString());
		if(cek_akses>0) cmd.setAkses(1);
		else cmd.setAkses(0); 
		
	}
	
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, dateEditor);
	}
	
	@Override
	protected void onBind(HttpServletRequest httpservletrequest, Object obj) throws Exception {
		super.onBind(httpservletrequest, obj);
	}
	
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException err) throws Exception {
		AddressRegion cmd = (AddressRegion) command;
		HttpSession session = request.getSession();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		AddressRegion cmd = (AddressRegion) command;
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		String sukses = "";
		
		cmd.setLar_update_date(elionsManager.selectSysdate());
		sukses = bacManager.updateKantorPemasaran(cmd, currentUser);
		
		cmd.setSubmitMode("new");
		
		map.put("sukses", sukses);
		map.put("kota", cmd.getKota());
		
		return new ModelAndView(new RedirectView(request.getContextPath()+"/bas/kantor_pemasaran.htm")).addAllObjects(map);
	}
	
}