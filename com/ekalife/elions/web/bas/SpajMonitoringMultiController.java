package com.ekalife.elions.web.bas;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Agen;
import com.ekalife.elions.model.CommandControlSpaj;
import com.ekalife.elions.model.FormHist;
import com.ekalife.elions.model.FormSpaj;
import com.ekalife.elions.model.Spaj;
import com.ekalife.elions.model.SpajDet;
import com.ekalife.elions.model.User;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentMultiController;
import com.ibatis.common.resources.Resources;

import id.co.sinarmaslife.std.model.vo.DropDown;
import id.co.sinarmaslife.std.util.FileUtil;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
/**
 * Controller untuk sistem kontrol SPAJ (pemakaian SPAJ)
 * User berkaitan adalah GA sebagai penyedia barang, BAS sebagai perantara cabang,
 * dan branch admin untuk menghandle permintaan SPAJ dari agen
 * 
 * @author Yusuf Sutarko
 * @since Feb 19, 2007 (2:13:43 PM)
 */
public class SpajMonitoringMultiController extends ParentMultiController{
	protected final Log logger = LogFactory.getLog( getClass() );
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

	/**
	 * Halaman innerFrame untuk approval SPAJ, isinya daftar form yang direquest oleh cabang tertentu 
	 * 
	 * @author Yusuf Sutarko
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
		String pos=ServletRequestUtils.getStringParameter(request, "pos","");
		int all = ServletRequestUtils.getIntParameter(request, "all", 0);
		//Integer flag=ServletRequestUtils.getIntParameter(request, "flag");//1=cabang 2=agen
		String msf_id = ServletRequestUtils.getStringParameter(request, "msf_id", "");
		String index = ServletRequestUtils.getStringParameter(request, "index", "");
		
		cmd.put("posisi",posisi);
		if(!"".equals(lca_id)) {
			FormSpaj formSpaj = new FormSpaj();
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			formSpaj.setMss_jenis(0);
			if(!lca_id.equals("ALL_BRANCH")) formSpaj.setLca_id(lca_id);
			formSpaj.setMsab_id(0);		
			if(all==0){
				formSpaj.setPosisi(posisi);
				formSpaj.setPos(pos);
			}	
			if(currentUser.getName().equals("MARIA DF") || currentUser.getName().equals("NOVI_AGUSTINA1")) {
				formSpaj.setMcl_first("special");
			}
			List<FormSpaj> daftar = elionsManager.selectDaftarFormSpaj(formSpaj);
			
			if(daftar.isEmpty()) {
				cmd.put("pesan", "Tidak ada permintaan untuk cabang ini");
			}else {
				for(FormSpaj f : daftar) {
					f.setWarna((String) daftarWarna.get(f.getStatus_form()));
                    String tanggal=FormatDate.toString(f.getMsfh_dt());                    
                    f.setMsfh_dt(defaultDateFormat.parse(tanggal));
                    
				}
			}
			cmd.put("lca_id", lca_id);
			cmd.put("daftarForm", daftar);
			cmd.put("msf_id", msf_id);
			cmd.put("index", index);
		}else {
			cmd.put("pesan", "Silahkan pilih salah satu cabang terlebih dahulu");
		}

		return new ModelAndView("bas/daftar_form", "cmd", cmd);
	}
	
	/**
	 * Halaman innerFrame untuk permintaan SPAJ agen ke admin, isinya daftar agen dalam suatu region 
	 * 
	 * @author Yusuf Sutarko
	 * @since Feb 27, 2007 (10:28:00 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView daftarAgen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		String lca_lwk_lsrg = ServletRequestUtils.getStringParameter(request, "lca_lwk_lsrg", "");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		
		if("ALL_AGEN".equals(lca_lwk_lsrg)) {
			cmd.put("daftarAgen", elionsManager.selectAllAgentFromRegion(Integer.valueOf(currentUser.getLus_id())));
			//cmd.put("daftarAgenBaru", elionsManager.selectAllAgentBranchFromRegion(Integer.valueOf(currentUser.getLus_id())));
		}else if(!"".equals(lca_lwk_lsrg)) {
			cmd.put("daftarAgen", elionsManager.selectAgentFromRegion(lca_lwk_lsrg));
			//cmd.put("daftarAgenBaru", elionsManager.selectAgentBranchFromRegion(lca_lwk_lsrg));
		}else {
			//cmd.put("pesan", "Silahkan pilih salah satu region atau pilih AGEN BARU");
			cmd.put("pesan", "Silahkan pilih salah satu region");
		}
		
		cmd.put("daftarAgenBaru", elionsManager.selectAllAgentBranchFromRegion(Integer.valueOf(currentUser.getLus_id())));
		
		return new ModelAndView("bas/daftar_agen", "cmd", cmd);
	}
	
	/**
	 * Halaman innerFrame untuk pengontrolan SPAJ agen oleh branch admin, isinya daftar agen yang sudah
	 * mempunyai sejarah permintaan 
	 * 
	 * @author Yusuf Sutarko
	 * @since March 7, 2007 (08:05:00 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView daftarAgenSpaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		String lca_lwk_lsrg = ServletRequestUtils.getStringParameter(request, "lca_lwk_lsrg", "");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String msf_id = ServletRequestUtils.getStringParameter(request, "msf_id", "");
		String index = ServletRequestUtils.getStringParameter(request, "index", "");
		
		if("ALL_AGEN".equals(lca_lwk_lsrg)) {
			cmd.put("daftarAgen", elionsManager.selectHistoryAgentAllRegion(Integer.valueOf(currentUser.getLus_id())));
		}else if(!"".equals(lca_lwk_lsrg)) {
			cmd.put("daftarAgen", elionsManager.selectHistoryAgent(Integer.valueOf(currentUser.getLus_id()), lca_lwk_lsrg));
		}else {
			//cmd.put("pesan", "Silahkan pilih salah satu region atau pilih AGEN BARU");
			cmd.put("pesan", "Silahkan pilih salah satu region");
		}
		cmd.put("msf_id", msf_id);
		cmd.put("index", index);
		
		return new ModelAndView("bas/daftar_agen_spaj", "cmd", cmd);
	}
	
	/**
	 * Halaman untuk download form2 yang bisa digunakan di cabang untuk pengontrolan SPAJ
	 * 
	 * @author Yusuf Sutarko
	 * @since March 9, 2007 (08:00:00 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		String path = request.getContextPath();
		
		List<DropDown> daftarForm = new ArrayList<DropDown>();
		daftarForm.add(new DropDown(path+"/include/form/form_monitoring.xls", "Form Monitoring SPAJ"));
		daftarForm.add(new DropDown(path+"/include/form/form_pertanggungan.xls", "Form Permintaan SPAJ dan Pertanggungjawaban"));
		
		cmd.put("daftarForm", daftarForm);
		
		return new ModelAndView("bas/download_form", "cmd", cmd);
	}
	
	/**
	 * Halaman untuk view detail stok blanko yang ada di cabang
	 * 
	 * @author Yusuf Sutarko
	 * @since Apr 2, 2007
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView daftarBlanko(HttpServletRequest request, HttpServletResponse response) throws Exception{
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Map cmd = new HashMap();
		cmd.put("daftarBlanko", elionsManager.selectDetailBlankoCabang(currentUser.getLca_id()));
		return new ModelAndView("bas/daftar_blanko", "cmd", cmd);
	}
	
	/**
	 * Halaman innerFrame untuk daftar form yang dicari dari viewer 
	 * 
	 * @author Yusuf Sutarko
	 * @since March 31st, 2007 (2:45:13 PM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView daftarFormCari(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> cmd = new HashMap();
		String cari = ServletRequestUtils.getStringParameter(request, "msf_id", "");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id", "");
		List<FormSpaj> daftar=new ArrayList();
		cmd.put("lca_id",lca_id);

		if(lca_id!= ""){
			FormSpaj formSpaj=new FormSpaj();
			if(!lca_id.equals("ALL_BRANCH")) 
				formSpaj.setLca_id(lca_id);
			formSpaj.setMsab_id(0);		
			formSpaj.setMss_jenis(0);		
			daftar = elionsManager.selectDaftarFormSpaj(formSpaj);
		}else if(!"".equals(cari)) {
			daftar = elionsManager.selectDaftarFormCari(cari);
			
		}else {
			cmd.put("pesan", "Silahkan masukkan dahulu nomor form yang dicari");
		}
		if(daftar.isEmpty()) {
			cmd.put("pesan", "Tidak ada form dengan nomor tersebut");
		}else {
			for(FormSpaj f : daftar) {
				f.setWarna((String) daftarWarna.get(f.getStatus_form()));
			}
		}
		cmd.put("daftarForm", daftar);
		return new ModelAndView("bas/daftar_form", "cmd", cmd);
	}	
	/**
	 * Halaman untuk viewer form spaj, viewer ini dapat diakses dari semua orang, tapi jenis view nya berbeda
	 * 
	 * @author Yusuf Sutarko
	 * @since March 31, 2007 (08:00:00 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommandControlSpaj cmd = new CommandControlSpaj();
		daftarJenisSpaj = elionsManager.selectJenisSpaj();;
		cmd.setLegend(daftarWarna, daftarWarnaAgen, daftarJenisSpaj);
		cmd.setMsf_id(ServletRequestUtils.getStringParameter(request, "msf_id", ""));
		//cmd.setDaftarCabang(this.elionsManager.selectlstCabang());
		cmd.setLca_id(ServletRequestUtils.getStringParameter(request, "lca_id", ""));
		if(!"".equals(cmd.getMsf_id())) {
			User currentUser = (User) request.getSession().getAttribute("currentUser");
	
			cmd.setDaftarFormSpaj(this.elionsManager.selectFormSpaj(cmd.getMsf_id(), null, currentUser.getLus_id()));
			cmd.setDaftarFormHistory(elionsManager.selectFormHistory(cmd.getMsf_id()));
			for(FormSpaj f : cmd.getDaftarFormSpaj()) {
				f.setWarna((String) daftarWarna.get(f.getStatus_form()));
			}
			FormHist formHist = new FormHist();
			formHist.setMsf_id(cmd.getMsf_id());
			formHist.setMsf_urut(1);
			cmd.setFormHistUser(elionsManager.selectFormHistory(formHist));
		}
		
		return new ModelAndView("bas/viewer", "cmd", cmd);
	}
	
	/**Fungsi:	Untuk Menampilkan detail dari no blanko yang ada di cabang
	 * @param request
	 * @throws Exception
	 * @author Ferry Harlim
	 */
	public ModelAndView detailBlankoCabang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommandControlSpaj cmd=new CommandControlSpaj();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Integer lsjsId=ServletRequestUtils.getIntParameter(request, "lsjsId");
		Integer ke=ServletRequestUtils.getIntParameter(request, "ke");
		// mssjenis=0 stock yg ada di cabang
		List daftarBlanko=elionsManager.selectDetailStokCabang(currentUser.getLca_id(), 0, lsjsId,1,Integer.valueOf(currentUser.getLus_id()),null,null);
		Map map=elionsManager.selectLstJenisSpaj(lsjsId);
		cmd.setJnsSpaj((String)map.get("LSJS_DESC"));
		cmd.setDaftarBlanko(daftarBlanko);
		cmd.setKe(ke);
		if(daftarBlanko!=null)
			cmd.setSize(daftarBlanko.size());
		
		return new ModelAndView("bas/detailBlankoCabang", "cmd", cmd);
	}
	
	public ModelAndView detailBlankoSimasCard(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommandControlSpaj cmd =new CommandControlSpaj();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Integer lsjsId = ServletRequestUtils.getIntParameter(request, "lsjsId");
		Integer ke = ServletRequestUtils.getIntParameter(request, "ke");		
		
		List daftarBlanko= uwManager.selectDetailStockSimasCard();
		Map map=elionsManager.selectLstJenisSpaj(lsjsId);
		cmd.setJnsSpaj((String)map.get("LSJS_DESC"));
		cmd.setDaftarBlanko(daftarBlanko);
		cmd.setKe(ke);
		if(daftarBlanko!=null)
			cmd.setSize(daftarBlanko.size());
		
		return new ModelAndView("bas/detailBlankoCabang", "cmd", cmd);
	}
	
	/**Fungsi:	Untuk Mencari nomor form dari cabang maupun agen
	 * @param request
	 * @throws Exception
	 * @author Ferry Harlim
	 */
	public ModelAndView cariBlanko(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommandControlSpaj cmd=new CommandControlSpaj();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Integer flag=ServletRequestUtils.getIntParameter(request, "flag",0);
		String msfId=ServletRequestUtils.getStringParameter(request, "msf_id","");
		String lcaId=ServletRequestUtils.getStringParameter(request, "lca_id","All");
		String msagId=ServletRequestUtils.getStringParameter(request, "msagId",null);
		Integer cabang=ServletRequestUtils.getIntParameter(request, "cabang",0);
		if(flag==1){//1=express search 
			cmd.setDaftarFormSpaj(elionsManager.selectSearchFormSpajExpress(msfId));
		}else if(flag==2){//2=detail search
			FormSpaj formSpaj=new FormSpaj();
			formSpaj.setLca_id(lcaId);
			formSpaj.setMsag_id(msagId);
			formSpaj.setMss_jenis(cabang);
			cmd.setDaftarFormSpaj(elionsManager.selectSearchFormSpajDetail(formSpaj));
		}
		cmd.setMsf_id(msfId);
		cmd.setDaftarCabang(this.elionsManager.selectlstCabang());
		cmd.setLca_id(lcaId);
		
		return new ModelAndView("bas/cariBlanko", "cmd", cmd);
	}

	/**
	 * Halaman untuk memberikan status agent (SP=1,2,3)
	 * jikalau agent mendapatkan SP=3 maka tidak bisa minta kertas polis
	 * 
	 * @author Ferry Harlim
	 * @since May 23, 2007 (08:00:00 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView agent_status (HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommandControlSpaj cmd = new CommandControlSpaj();
		cmd.setDaftarCabang(this.elionsManager.selectlstCabang());
		cmd.setLca_id(ServletRequestUtils.getStringParameter(request, "lca_id",""));
		String msag_id=ServletRequestUtils.getStringParameter(request, "msag_id");
		String up=ServletRequestUtils.getStringParameter(request, "up","0");
		String status=ServletRequestUtils.getStringParameter(request, "status",null);
		
		if(cmd.getLca_id().equals(""))
			cmd.setNama_cabang(cmd.getLca_id());
		else
			cmd.setNama_cabang(elionsManager.selectLstCabangNamaCabang(cmd.getLca_id()));
		
		if(up.equals("1")){
			Agen agen=new Agen();
			agen.setMsag_id(msag_id);
			agen.setMsag_sp(Integer.valueOf(status));
			elionsManager.updateMstAgentStatus(agen);
			cmd.setSuc(1);
			cmd.setAgen(agen);
		}
		Agen agen=new Agen();
		agen.setLca_id(cmd.getLca_id());
		agen.setLstb_id(1);
		agen.setMsag_active(1);
		cmd.setDaftarAgen(elionsManager.selectAllAgentFromBranch(agen));
		cmd.setDaftarStatus(agen.setStatus());
		return new ModelAndView("bas/agent_status", "cmd", cmd);
	}
	
	public ModelAndView outstanding_cabang(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		List lsCabang=new ArrayList();
		List lsAdmin=new ArrayList();
		Integer lock=0;
		Integer idx=props.getProperty("access.bas.report.kontrol_spaj").indexOf(currentUser.getLde_id());
		if(idx>=0){
			lsCabang=elionsManager.selectlstCabang();
			Map cabang=new HashMap();
			cabang.put("KEY","0");
			cabang.put("VALUE","All");
			lsCabang.add(0, cabang);
			lsAdmin=elionsManager.selectAllUserInCabang(lca_id);
		}else{
			Map cabang=new HashMap();
			cabang.put("KEY",currentUser.getLca_id());
			cabang.put("VALUE",currentUser.getCabang());
			lsCabang.add(cabang);
			Map admin=new HashMap();
			admin.put("KEY", currentUser.getLus_id());
			admin.put("VALUE", currentUser.getName());
			lsAdmin.add(admin);
			lock=1;
		}
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());
		Integer flag=ServletRequestUtils.getIntParameter(request, "l");
		if(flag==null)
			lus_id=currentUser.getLus_id();
		param.put("lock",lock);
		param.put("lsCabang", lsCabang);
		param.put("lsAdmin", lsAdmin);
		param.put("lca_id",lca_id);
		param.put("lus_id",lus_id);
		param.put("tgl", elionsManager.selectSysdate());
		
		return new ModelAndView("bas/outstanding_cabang",param);
	}

	
	public ModelAndView outstanding_agen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<Map<String, String>> listAgenNew=new ArrayList();
		List listCabangNew=new ArrayList();
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		param.put("lca_id",lca_id);
		param.put("tgl", elionsManager.selectSysdate());
		Agen paramAgen=new Agen();
		paramAgen.setMsag_active(1);
		paramAgen.setLstb_id(1);
		List listAgen=new ArrayList();
		List listCabang=new ArrayList();
		Integer idx=props.getProperty("access.bas.report.kontrol_spaj").indexOf(currentUser.getLde_id());
		paramAgen.setLca_id(lca_id);
		listAgen=elionsManager.selectAllAgentFromBranch(paramAgen);
		if(idx>=0){
			Map mapDefault=new HashMap();
			mapDefault.put("KEY", "0");
			mapDefault.put("VALUE", "---Pilih---");
			listCabangNew.add(mapDefault);
			listAgenNew.add(mapDefault);
			listCabang=elionsManager.selectAllBranchesAndDepartments(null, "19,01");
		}else{
			User user=new User();
			user.setLca_id(currentUser.getLca_id());
			user.setCabang(currentUser.getCabang());
			listCabang.add(user);
		}
		
		for(int j=0;j<listAgen.size();j++){
			Map map=new HashMap();
			Agen agen=(Agen)listAgen.get(j);
			map.put("KEY", agen.getMsag_id());
			map.put("VALUE", agen.getMcl_first());
			listAgenNew.add(map);
		}
		for(int j=0;j<listCabang.size();j++){
			Map map=new HashMap();
			User user=(User)listCabang.get(j);
			map.put("KEY", user.getLca_id());
			map.put("VALUE", user.getCabang());
			listCabangNew.add(map);
		}
		
		param.put("listAgen", listAgenNew);
		param.put("listCabang", listCabangNew);
		
		return new ModelAndView("bas/outstanding_agen",param);
	}
		
	/**
	 * Report permintaan spaj percabang
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * @author Yusup_A
	 * @since Jan 14, 2009 (2:59:52 PM)
	 */
	public ModelAndView permintaan_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=currentUser.getLca_id();
		List lsCabang=new ArrayList();
		List lsAdmin=new ArrayList();
		Integer lock=0;
		Integer idx=props.getProperty("access.bas.report.kontrol_spaj").indexOf(currentUser.getLde_id());
		// khusus bas & Maria DF
		if(idx>=0 || currentUser.getName().equals("MARIA DF") || currentUser.getName().equals("NOVI_AGUSTINA1")){
			lsCabang=elionsManager.selectlstCabang();
			Map cabang=new HashMap();
			cabang.put("KEY","0");
			cabang.put("VALUE","All");
			lsCabang.add(0, cabang);
			lsAdmin=elionsManager.selectAllUserInCabang(lca_id);
			List<Map> lsJenis = elionsManager.selectJenisSpaj();
			param.put("lsJenis", lsJenis);
			
		}else{
			Map cabang=new HashMap();
			cabang.put("KEY",currentUser.getLca_id());
			cabang.put("VALUE",currentUser.getCabang());
			lsCabang.add(cabang);
			Map admin=new HashMap();
			admin.put("KEY", currentUser.getLus_id());
			admin.put("VALUE", currentUser.getName());
			lsAdmin.add(admin);
			lock=1;
		}	

		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());

		param.put("lock",lock);
		param.put("lsCabang", lsCabang);
		param.put("lsAdmin", lsAdmin);
		//param.put("lca_id",lca_id);
		param.put("lus_id",lus_id);
		param.put("tgl", elionsManager.selectSysdate());		
		
		return new ModelAndView("bas/reportPermintaanSpaj",param); 
	}
	
	public ModelAndView pembatalan_cabang_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=currentUser.getLca_id();
		List lsCabang=new ArrayList();
		List lsAdmin=new ArrayList();
		Integer lock=0;
		Integer idx=props.getProperty("access.bas.report.kontrol_spaj").indexOf(currentUser.getLde_id());
		// khusus bas & Maria DF
		if(idx>=0 || currentUser.getLus_id().equals("288")){
			lsCabang=elionsManager.selectlstCabang();
			Map cabang=new HashMap();
			cabang.put("KEY","0");
			cabang.put("VALUE","All");
			lsCabang.add(0, cabang);
			lsAdmin=elionsManager.selectAllUserInCabang(lca_id);
			List<Map> lsJenis = elionsManager.selectJenisSpaj();
			param.put("lsJenis", lsJenis);
			
		}else{
			Map cabang=new HashMap();
			cabang.put("KEY",currentUser.getLca_id());
			cabang.put("VALUE",currentUser.getCabang());
			lsCabang.add(cabang);
			Map admin=new HashMap();
			admin.put("KEY", currentUser.getLus_id());
			admin.put("VALUE", currentUser.getName());
			lsAdmin.add(admin);
			lock=1;
		}	

		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());

		param.put("lock",lock);
		param.put("lsCabang", lsCabang);
		param.put("lsAdmin", lsAdmin);
		//param.put("lca_id",lca_id);
		param.put("lus_id",lus_id);
		param.put("tgl", elionsManager.selectSysdate());		
		
		return new ModelAndView("bas/reportPembatalanCabangSpaj",param); 
	}
	
	/**
	 * Report pertanggung jawaban dilihat dari no permintaan dari cabang ke pusat
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *
	 * @author Yusup_A
	 * @since Jan 14, 2009 (3:30:11 PM)
	 */
	public ModelAndView pertanggungjawaban_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lcaIdAdmin =ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		String lca_id = ServletRequestUtils.getStringParameter(request, "lca_id","");
		List lsCabang = new ArrayList();
		List lsAdmin = new ArrayList();
		Integer idx = props.getProperty("access.bas.report.kontrol_spaj").indexOf(currentUser.getLde_id());
		
		List<Map> lsJenis = elionsManager.selectJenisSpaj();
		
		if(idx>=0 || currentUser.getName().equals("MARIA DF")){		
			lsCabang=elionsManager.selectlstCabang();
			Map cabang=new HashMap();
			lsAdmin=elionsManager.selectAllUserInCabang(lcaIdAdmin);
			
			// tarik no permintaan yg sdh dibuat
			if(!lca_id.equals("")) {
				List permintaan = new ArrayList();
				FormSpaj formSpaj = new FormSpaj();
				formSpaj.setMss_jenis(0);
				formSpaj.setLca_id(lca_id);
				formSpaj.setMsab_id(0);
				//formSpaj.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				permintaan = elionsManager.selectDaftarFormSpaj(formSpaj);
				param.put("daftarNomorFormSpaj",permintaan);
			}

		}else {
			Map cabang=new HashMap();
			cabang.put("KEY",currentUser.getLca_id());
			cabang.put("VALUE",currentUser.getCabang());
			lsCabang.add(cabang);
			Map admin=new HashMap();
			admin.put("KEY", currentUser.getLus_id());
			admin.put("VALUE", currentUser.getName());
			lsAdmin.add(admin);		
			
			// tarik no permintaan yg sdh dibuat
			if(!lca_id.equals("")) {
				List permintaan = new ArrayList();
				FormSpaj formSpaj = new FormSpaj();
				formSpaj.setMss_jenis(0);
				formSpaj.setLca_id(currentUser.getLca_id());
				formSpaj.setMsab_id(0);
				formSpaj.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				permintaan = elionsManager.selectDaftarFormSpaj(formSpaj);
				param.put("daftarNomorFormSpaj",permintaan);
			}			
		}
		
		String lus_id=ServletRequestUtils.getStringParameter(request,"lus_id",currentUser.getLus_id());
		
		param.put("lsJenis", lsJenis);
		param.put("lsCabang", lsCabang);
		param.put("lsAdmin", lsAdmin);
		param.put("lca_id",lca_id);
		param.put("lus_id",lus_id);
		
		return new ModelAndView("bas/reportPertanggungjawabanSpaj",param); 
	}
	
	public ModelAndView laporanBasCekDataInputData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		List lsCabang=new ArrayList();
		List lsAdmin=new ArrayList();
		Integer lock=0;
		Integer idx=props.getProperty("access.bas.report.update_nasabah").indexOf(currentUser.getLus_id());
		if(idx>=0){
			lsCabang=elionsManager.selectlstCabang();
			Map cabang=new HashMap();
			cabang.put("KEY","0");
			cabang.put("VALUE","All");
			lsCabang.add(0, cabang);
			lsAdmin=elionsManager.selectAllUserInCabang(lca_id);
		}else{
			Map cabang=new HashMap();
			cabang.put("KEY",currentUser.getLca_id());
			cabang.put("VALUE",currentUser.getCabang());
			lsCabang.add(cabang);
			Map admin=new HashMap();
			admin.put("KEY", currentUser.getLus_id());
			admin.put("VALUE", currentUser.getName());
			lsAdmin.add(admin);
			lock=1;
		}
		param.put("lsCabang", lsCabang);
		param.put("tgl", elionsManager.selectSysdate());
		
		return new ModelAndView("bas/laporanBasCekDataInputData",param);
	}
	
	public ModelAndView laporanBasCekDataBalik(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param=new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lca_id=ServletRequestUtils.getStringParameter(request, "lca_id",currentUser.getLca_id());
		List lsCabang=new ArrayList();
		List lsAdmin=new ArrayList();
		Integer lock=0;
		Integer idx=props.getProperty("access.bas.report.update_nasabah").indexOf(currentUser.getLus_id());
		if(idx>=0){
			lsCabang=elionsManager.selectlstCabang();
			Map cabang=new HashMap();
			cabang.put("KEY","0");
			cabang.put("VALUE","All");
			lsCabang.add(0, cabang);
			lsAdmin=elionsManager.selectAllUserInCabang(lca_id);
		}else{
			Map cabang=new HashMap();
			cabang.put("KEY",currentUser.getLca_id());
			cabang.put("VALUE",currentUser.getCabang());
			lsCabang.add(cabang);
			Map admin=new HashMap();
			admin.put("KEY", currentUser.getLus_id());
			admin.put("VALUE", currentUser.getName());
			lsAdmin.add(admin);
			lock=1;
		}
		param.put("lsCabang", lsCabang);
		param.put("tgl", elionsManager.selectSysdate());
		
		return new ModelAndView("bas/laporanBasCekDataBalik",param);
	}

	/**
	 * Halaman innerFrame untuk permintaan SPAJ agen ke admin, isinya daftar agen dalam semua region 
	 * 
	 * @author Ferry Harlim
	 * @since Okt 22, 2007 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewerAgen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		String mode = ServletRequestUtils.getStringParameter(request, "mode", "");
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lcaId=ServletRequestUtils.getStringParameter(request, "lcaId","00");
		Integer flag=ServletRequestUtils.getIntParameter(request, "flag",0);
		String query=ServletRequestUtils.getStringParameter(request, "query","");
		List lsAgen=new ArrayList();
		if(flag!=0 && (!query.equals("")))
			lsAgen=elionsManager.selectAllAgent(flag, lcaId, query);
		List lsCabang=elionsManager.selectlstCabang2();
		List lsCari=new ArrayList();
		Map mCari=new HashMap();
		mCari.put("KEY",1);
		mCari.put("VALUE","Kode Agen/Kode TM");
		lsCari.add(mCari);
		mCari=new HashMap();
		mCari.put("KEY",2);
		mCari.put("VALUE","Nama Agen");
		lsCari.add(mCari);
		mCari=new HashMap();
		mCari.put("KEY",3);
		mCari.put("VALUE","No. Identitas");
		lsCari.add(mCari);
		
		if(lsAgen.isEmpty())
			cmd.put("pesan", "Silahkan Cari Agen dengan Menggunakan Tools Di atas.");
		cmd.put("lsCabang", lsCabang);
		cmd.put("lsAgen", lsAgen);
		cmd.put("lsCari", lsCari);
		cmd.put("query",query);
		cmd.put("flag",flag);
		cmd.put("lca_id",lcaId);
		cmd.put("mode",mode); 
		
		return new ModelAndView("bas/viewer/viewer_agen", "cmd", cmd);
	}
	
	public ModelAndView viewerAgenNew(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String lcaId=ServletRequestUtils.getStringParameter(request, "lcaId","00");
		Integer flag=ServletRequestUtils.getIntParameter(request, "flag",0);
		String query=ServletRequestUtils.getStringParameter(request, "query","");
		List lsAgen=new ArrayList();
		if((flag!=0 && (!query.equals(""))) || flag == 4)
			lsAgen=elionsManager.selectAllAgent(flag, lcaId, query);
		List lsCabang=elionsManager.selectlstCabang2();
		List lsCari=new ArrayList();
		Map mCari=new HashMap();
		mCari.put("KEY", 4);
		mCari.put("VALUE","All");
		lsCari.add(mCari);
		mCari=new HashMap();
		mCari.put("KEY",1);
		mCari.put("VALUE","Kode Agen");
		lsCari.add(mCari);
		mCari=new HashMap();
		mCari.put("KEY",2);
		mCari.put("VALUE","Nama Agen");
		lsCari.add(mCari);
		mCari=new HashMap();
		mCari.put("KEY",3);
		mCari.put("VALUE","No. Identitas");
		lsCari.add(mCari);
		
		if(lsAgen.isEmpty())
			cmd.put("pesan", "Silahkan Cari Agen dengan Menggunakan Tools Di atas.");
		cmd.put("lsCabang", lsCabang);
		cmd.put("lsAgen", lsAgen);
		cmd.put("lsCari", lsCari);
		cmd.put("query",query);
		cmd.put("flag",flag);
		cmd.put("lca_id",lcaId);
		
		return new ModelAndView("bas/viewer/viewer_agen", "cmd", cmd);
	}	
	
	public ModelAndView endorse_form_agen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String msfId=ServletRequestUtils.getStringParameter(request, "msfId");
		Integer flagEndorse=ServletRequestUtils.getIntParameter(request, "flagEndorse",0);
		Map cmd=new HashMap();
		//cek dulu apakah admin tersebut termasuk dalam
		Integer cek=elionsManager.selectCekAgenInUserAdmin(currentUser.getLus_id(),msfId);
		if(cek>0){
			List lsDaftar=elionsManager.selectFormSpaj(msfId, msfId.substring(0, 2), currentUser.getLus_id());
			cmd.put("lsDaftar",lsDaftar);
			if(lsDaftar.isEmpty()==false){
				FormSpaj form=(FormSpaj)lsDaftar.get(0);
				User user=elionsManager.selectUser(""+form.getLus_id());
				cmd.put("nama", user.getLus_full_name());
				cmd.put("cabang", user.getCabang());
				cmd.put("msag_id", form.getMsag_id());
				cmd.put("nama_agen", form.getNama_agen());
				if(flagEndorse==1){
					//lakukan proses endrose user_id
					elionsManager.prosesEndorseFormAgen(msfId,currentUser); 
				}
			}
		}	
		cmd.put("msfId", msfId);
		return new ModelAndView("bas/viewer/endorse_form_agen", "cmd", cmd);
	}

	public ModelAndView update_tanggal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		
		if(request.getParameter("update") != null) {
			String kodeAgent = ServletRequestUtils.getStringParameter(request, "agent","");
			String noBlanko = ServletRequestUtils.getStringParameter(request, "blanko","");
			String tanggal = ServletRequestUtils.getStringParameter(request, "tanggal", "");
			
			String status = bacManager.updateTanggalPertanggungjawaban(kodeAgent,noBlanko,tanggal);
			cmd.put("pesan",status);
			//logger.info("ok");
		}
		return new ModelAndView("bas/update_tanggal","cmd",cmd);
	}
	
	public ModelAndView history_fitrah(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param = new HashMap();
		//List<Map> lsJenis = elionsManager.selectJenisSpaj();
		param.put("lsJenis", elionsManager.selectJenisSpaj());
		param.put("lsCabang", elionsManager.selectAllLstCab());
		
		return new ModelAndView("bas/reportHistoryFitrah",param);
	}
	
	public ModelAndView fitrah_bmi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param = new HashMap();
		param.put("lsCabBmi", uwManager.selectCabBmiForReport());
		return new ModelAndView("bas/fitrah_bmi",param);
	}
	
/*	public ModelAndView pending_rk(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param = new HashMap();
		param.put("lsCabBmi", uwManager.selectCabBmiForReport());
		return new ModelAndView("bas/pending_rk",param);
	}*/
	
	public ModelAndView permintaan_spaj_bandara(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param = new HashMap();
		List<String> err = new ArrayList<String>();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		List<String> noBlanko2 = new ArrayList<String>();
		List<String> noBlankoSlh = new ArrayList<String>();
		
		String no_permintaan = ServletRequestUtils.getStringParameter(request, "no_permintaan","");
		String tglMinta = ServletRequestUtils.getStringParameter(request, "tglMinta","");
		String nama = ServletRequestUtils.getStringParameter(request, "nama","");
		String jumlh = ServletRequestUtils.getStringParameter(request, "jumlBlanko","");
		String noBlanko = ServletRequestUtils.getStringParameter(request, "noBlanko","");
		Integer jenis = ServletRequestUtils.getIntParameter(request, "jnsTravIns",0);
		Boolean success = true;
		
		
		List<DropDown> jenisTravelIns = new ArrayList<DropDown>();
		jenisTravelIns.add(new DropDown("5000","10000"));
		jenisTravelIns.add(new DropDown("10000","20000"));
		
		if(request.getParameter("new") != null) {
			param.put("show", "show");
			tglMinta = nama = jumlh = noBlanko = "";
		}
		else if(request.getParameter("save") != null) {
			if(tglMinta.equals("") || nama.equals("") || jumlh.equals("") || noBlanko.equals(""))  {
				err.add("Tidak boleh ada kolom yang kosong");
				success = false;
			}
			else {
				String[] noBlanko1 = noBlanko.split(";");
				int x = 0;
				for(int a=0; a<noBlanko1.length;a++) {
					if(noBlanko1[a].contains("-")) {
						String[] temp = noBlanko1[a].split("-");
						for(int b=Integer.parseInt(temp[0]); b<= Integer.parseInt(temp[1]);b++) {
							noBlanko2.add(FormatString.rpad("0", String.valueOf(b), 6));
						}
					}
					else noBlanko2.add(noBlanko1[a]);
				}
				if(noBlanko2.size() != Integer.parseInt(jumlh)) {
					err.add("Jumlah dengan no blanko yang diberi tidak sesuai");
					success = false;
				}
				else if(success) {
					if(no_permintaan.equals("")) {
						String sequence = uwManager.getMaxPermintaanId(currentUser.getLus_id());
						String kodeBandara = uwManager.getKodeBandara(currentUser.getLus_id());
						String id;
						if(sequence == null) {
							id = kodeBandara+FormatString.rpad("0", "1", 6);
						}
						else {
							Integer next = Integer.parseInt(sequence.substring(3));
							id = kodeBandara+FormatString.rpad("0", String.valueOf((Integer.parseInt(sequence.substring(3))+1)), 6);
						}
						
						// cek no blanko yg di minta dengan stok yg ada di admin
						for(int a=0; a<noBlanko2.size();a++) {
							Boolean cekNo = uwManager.cekNoBlankoIsExist(currentUser.getLus_id(),currentUser.getLca_id(),15,noBlanko2.get(a),0,0,jenis);
							if(!cekNo) { noBlankoSlh.add(noBlanko2.get(a)); }
						}
						
						if(noBlankoSlh.size() != 0) {
							String blanko = "";
							for(int a=0;a<noBlankoSlh.size();a++) {
								if(a != 0) blanko += ", "+noBlankoSlh.get(a);
								else blanko += noBlankoSlh.get(a);
							}
							err.add("no blanko "+blanko+" tidak terdapat di stock admin");
						}
						else {
							// buat form permintaan bandara
							uwManager.insertFormBandara(id,tglMinta,nama,jumlh,noBlanko,currentUser.getLus_id(),jenis);
							
							// kurangi stok di admin
							Spaj spajCabang = new Spaj();
							spajCabang.newSpaj(0, currentUser.getLca_id(), 15, 0, 0-noBlanko2.size(), Integer.valueOf(currentUser.getLus_id()),Integer.valueOf(currentUser.getLus_id()));
							uwManager.updateStockCabang(spajCabang);
							
							//ubah status di spaj det
							SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
							logger.info("Size : " + noBlanko2.size());
							for(int a=0; a<noBlanko2.size();a++) {
								List<SpajDet> spajDet = new ArrayList<SpajDet>();
								spajDet = elionsManager.selectDetailStokCabang(currentUser.getLca_id(), 0, 15,1,Integer.valueOf(currentUser.getLus_id()),noBlanko2.get(a),jenis);
								spajDet.get(0).setLsp_id(2);
								spajDet.get(0).setMsf_id(id);
								spajDet.get(0).setMss_jenis(1);
								spajDet.get(0).setMssd_dt(df.parse(tglMinta));
								spajDet.get(0).setMsf_id_bef(id);
								spajDet.get(0).setFlag_bef(1);
								
								uwManager.updateSpajDet(spajDet.get(0));
							}
							
							param.put("ID", id);
							param.put("mssg", "Permintaan baru dengan no "+id+" telah berhasil disimpan");							
						}
					}
					else {
						uwManager.updateFormBandara(no_permintaan,tglMinta,nama,jumlh,noBlanko,currentUser.getLus_id());
						param.put("ID", no_permintaan);
						param.put("mssg", "Permintaan no "+no_permintaan+" telah diubah");
					}
					
				}
			}
			param.put("show", "show");
			param.put("TGLMINTA", tglMinta);
			param.put("NAMA", nama);
			param.put("JUMLH", jumlh);
			param.put("NOBLANKO", noBlanko);
			param.put("JENIS", jenis);
		}
		else if(request.getParameter("view") != null) {
			param = uwManager.selectFormPermintaanBandara(no_permintaan);
			param.put("show", "show");
			param.put("view", 1);
		}
		else if(request.getParameter("save2") != null) {
			String no_blanko = ServletRequestUtils.getStringParameter(request, "no","");
			String status = ServletRequestUtils.getStringParameter(request, "stat","");
			
			if(!no_blanko.equals("") && !status.equals("")) {
				List<SpajDet> spajDet = new ArrayList<SpajDet>();
				spajDet = elionsManager.selectDetailStokCabang(currentUser.getLca_id(), 1, 15,2,Integer.valueOf(currentUser.getLus_id()),no_blanko,jenis);
				spajDet.get(0).setLsp_id(Integer.parseInt(status));
				spajDet.get(0).setMssd_dt(elionsManager.selectSysdate());
				
				uwManager.updateSpajDet(spajDet.get(0));
				param.put("mssg", "Perubahan status blanko telah di input");
			}
			else {
				param.put("mssg", "Silahkan pililh no blanko & status terlebih dahulu");
			}
		}
		
		param.put("errors", err);
		param.put("typeTravelIns", jenisTravelIns);
		param.put("lsPermintaan", uwManager.selectPermintaan(currentUser.getLus_id()));
		param.put("lsBlanko", uwManager.selectNoBlankoBandara(currentUser.getLus_id(),currentUser.getLca_id()));
		
		return new ModelAndView("bas/permintaan_spaj_bandara",param);
	}
	
	public ModelAndView spaj_bandara(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Integer idx=props.getProperty("access.bas.report.kontrol_spaj").indexOf(currentUser.getLde_id());
		if(idx < 0) param.put("lock", 1);
		
		param.put("lsAdminBandara", uwManager.selectAdminBandara());
		return new ModelAndView("bas/report_spaj_bandara",param);
	}
	
	public ModelAndView pertanggungjawaban_spaj_bandara(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param = new HashMap();
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		Integer idx=props.getProperty("access.bas.report.kontrol_spaj").indexOf(currentUser.getLde_id());
		if(idx < 0) param.put("lock", 1);
		
		param.put("lsAdminBandara", uwManager.selectAdminBandara());
		return new ModelAndView("bas/pertanggungjawaban_spaj_bandara",param);
	}	
	
	public ModelAndView cari_mia(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param = new HashMap();
		List<DropDown> lsSearch = new ArrayList<DropDown>();
		lsSearch.add(new DropDown("Nama","0"));
		lsSearch.add(new DropDown("No Register","1"));		
		
		Integer type = ServletRequestUtils.getIntParameter(request, "search_data",-1);
		String input = ServletRequestUtils.getStringParameter(request, "searchInput","");
		
		if(type == 0) {
			param.put("lsMis", uwManager.getMisFromName(input));
		}
		else if(type == 1) {
			param.put("lsMis", uwManager.getMisFromSpaj(input));
		}

		
		param.put("lsSearch", lsSearch);
		
		return new ModelAndView("bas/cari_mia",param);
	}
	
	public ModelAndView cari_bridge(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map param = new HashMap();
		List<DropDown> lsSearchBridge = new ArrayList<DropDown>();
		lsSearchBridge.add(new DropDown("Nama Agent","0"));
		lsSearchBridge.add(new DropDown("Kode Agent","1"));
		
		Integer type = ServletRequestUtils.getIntParameter(request, "search_data",-1);
		String input = ServletRequestUtils.getStringParameter(request, "searchInput","");
		List namaBridge = uwManager.getNamaBridge(input);
		List kodeBridge = uwManager.getKodeBridge(input);
		
		if(type == 0) {
			param.put("lsBridge", namaBridge);
		}
		else if(type == 1) {
			param.put("lsBridge", kodeBridge);
		}
	
		param.put("lsSearch", lsSearchBridge);
		
		return new ModelAndView("bas/cari_bridge",param);
	}
	
	public ModelAndView outstanding_all(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("bas/outstanding_all",null);
	}
	
	/**
	 * Halaman innerFrame untuk approval Brosur, isinya daftar form yang direquest oleh cabang tertentu 
	 * 
	 * @author Canpri
	 * @since Feb 06, 2013 (8:45:13 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView daftarFormBrosur(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		String lca_id = ServletRequestUtils.getStringParameter(request, "lca_id", "");
		int posisi = ServletRequestUtils.getIntParameter(request, "posisi", 99);
		String pos=ServletRequestUtils.getStringParameter(request, "pos","");
		int all = ServletRequestUtils.getIntParameter(request, "all", 0);
		//Integer flag=ServletRequestUtils.getIntParameter(request, "flag");//1=cabang 2=agen
		String msf_id = ServletRequestUtils.getStringParameter(request, "msf_id", "");
		String index = ServletRequestUtils.getStringParameter(request, "index", "");
		
		cmd.put("posisi",posisi);
		if(!"".equals(lca_id)) {
			FormSpaj formBrosur = new FormSpaj();
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			formBrosur.setMss_jenis(2);
			if(!lca_id.equals("ALL_BRANCH")) formBrosur.setLca_id(lca_id);
			formBrosur.setMsab_id(0);		
			if(all==0){
				formBrosur.setPosisi(posisi);
				formBrosur.setPos(pos);
			}	
			if(currentUser.getName().equals("MARIA DF") || currentUser.getName().equals("NOVI_AGUSTINA1")) {
				formBrosur.setMcl_first("special");
			}
			List<FormSpaj> daftar = uwManager.selectDaftarFormBrosur(formBrosur);
			
			if(daftar.isEmpty()) {
				cmd.put("pesan", "Tidak ada permintaan untuk cabang ini");
			}else {
				for(FormSpaj f : daftar) {
					f.setWarna((String) daftarWarna.get(f.getStatus_form()));
                    String tanggal=FormatDate.toString(f.getMsfh_dt());                    
                    f.setMsfh_dt(defaultDateFormat.parse(tanggal));
                    
				}
			}
			cmd.put("lca_id", lca_id);
			cmd.put("daftarForm", daftar);
			cmd.put("msf_id", msf_id);
			cmd.put("index", index);
		}else {
			cmd.put("pesan", "Silahkan pilih salah satu cabang terlebih dahulu");
		}

		return new ModelAndView("bas/daftar_form_brosur", "cmd", cmd);
	}
	
	/**
	 * Halaman untuk viewer form brosur, viewer ini dapat diakses dari semua orang, tapi jenis view nya berbeda
	 * 
	 * @author Canpri
	 * @since Feb 07, 2013 (08:51:00 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView viewerBrosur(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommandControlSpaj cmd = new CommandControlSpaj();
		
		daftarJenisBrosur = uwManager.selectJenisBrosur();;
		
		cmd.setLegendBrosur(daftarWarna, daftarWarnaAgen, daftarJenisBrosur);
		cmd.setMsf_id(ServletRequestUtils.getStringParameter(request, "msf_id", ""));
		cmd.setLca_id(ServletRequestUtils.getStringParameter(request, "lca_id", ""));
		
		if(!"".equals(cmd.getMsf_id())) {
			Integer jn = bacManager.selectJenisFormBrosur(cmd.getMsf_id());
			User currentUser = (User) request.getSession().getAttribute("currentUser");
	
			cmd.setDaftarFormBrosur(this.uwManager.selectFormBrosur(cmd.getMsf_id(), null, currentUser.getLus_id(), jn));
			cmd.setDaftarFormHistory(elionsManager.selectFormHistory(cmd.getMsf_id()));
			for(FormSpaj f : cmd.getDaftarFormBrosur()) {
				f.setWarna((String) daftarWarna.get(f.getStatus_form()));
			}
			FormHist formHist = new FormHist();
			formHist.setMsf_id(cmd.getMsf_id());
			formHist.setMsf_urut(1);
			cmd.setFormHistUser(elionsManager.selectFormHistory(formHist));
		}
		
		return new ModelAndView("bas/viewer_brosur", "cmd", cmd);
	}
	
	/**Fungsi:	Untuk Mencari nomor form brosur dari cabang maupun agen
	 * @author Canpri
	 * @since Feb 07, 2013 (08:51:00 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView cariBrosur(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CommandControlSpaj cmd=new CommandControlSpaj();
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Integer flag=ServletRequestUtils.getIntParameter(request, "flag",0);
		String msfId=ServletRequestUtils.getStringParameter(request, "msf_id","");
		String lcaId=ServletRequestUtils.getStringParameter(request, "lca_id","All");
		String msagId=ServletRequestUtils.getStringParameter(request, "msagId",null);
		Integer cabang=ServletRequestUtils.getIntParameter(request, "cabang",0);
		if(flag==1){//1=express search 
			cmd.setDaftarFormBrosur(uwManager.selectSearchFormBrosurExpress(msfId));
		}else if(flag==2){//2=detail search
			FormSpaj brosur = new FormSpaj();
			brosur.setLca_id(lcaId);
			brosur.setMsag_id(msagId);
			brosur.setMss_jenis(cabang);
			cmd.setDaftarFormBrosur(uwManager.selectSearchFormBrosurDetail(brosur));
		}
		cmd.setMsf_id(msfId);
		cmd.setDaftarCabang(this.elionsManager.selectlstCabang());
		cmd.setLca_id(lcaId);
		
		return new ModelAndView("bas/cariBrosur", "cmd", cmd);
	}
	
	/**
     * update stok brosur
     * @author Canpri
     * @since 7 Oct 2013
     * @param 
     * http://localhost/E-Lions/bas/spaj.htm?window=update_stok_brosur
     */
    public ModelAndView update_stok_brosur(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser=(User)request.getSession().getAttribute("currentUser");
    	Map map = new HashMap();
    	
    	String jenis = ServletRequestUtils.getStringParameter(request, "jenis", "1");
    	String busdev = ServletRequestUtils.getStringParameter(request, "busdev", "1");
    	
    	/*List<DropDown> lsBusdev = new ArrayList<DropDown>();
    	lsBusdev.add(new DropDown("1","Agency"));
    	lsBusdev.add(new DropDown("2","Corporate"));
    	lsBusdev.add(new DropDown("3","Bancass 1 (Team Ibu Yanti S) Bancass Support"));
    	lsBusdev.add(new DropDown("4","Bancass 2 & Bancass 1 BusDev"));*/
    	
    	/*List<DropDown> lsJenis = new ArrayList<DropDown>();
    	lsJenis.add(new DropDown("1","Brosur"));
    	lsJenis.add(new DropDown("2","Poster"));
    	lsJenis.add(new DropDown("3","Banner"));
    	lsJenis.add(new DropDown("4","Flayer"));*/
    	
    	/*String busdev = "0";
    	String nm_busdev = "";
    	
    	//validate per PIC
    	if("2664, 2832".indexOf(currentUser.getLus_id()) >= 0 ){
    		busdev = "1";//Ani chryswantini (Agency)
    		nm_busdev = "Agency";
    	}
    	if("3530".indexOf(currentUser.getLus_id()) >= 0 ){
    		busdev = "2";//Nixon Sompie (Corporate)
    		nm_busdev = "Corporate";
    	}
    	if("672".indexOf(currentUser.getLus_id()) >= 0 ){
    		busdev = "3";//Grisye S (Bancass 1 (Team Ibu Yanti S) Bancass Support)
    		nm_busdev = "Bancass 1 (Team Ibu Yanti S) Bancass Support";
    	}
    	if("39".indexOf(currentUser.getLus_id()) >= 0 ){
    		busdev = "4";//Jelita S (Bancass 2 & Bancass 1 BusDev)
    		nm_busdev = "Bancass 2 & Bancass 1 BusDev";
    	}*/
    	
    	map.put("tab", "1");
    	
    	if(request.getParameter("update") != null) {
    		String check[] = request.getParameterValues("chbox");
    		if(request.getParameterValues("chbox") != null){
    			for(int i=0;i<check.length;i++){
    				FormSpaj formbrosur = new FormSpaj();
    				
    				/*String id_stok = check[i].substring(0,check[i].indexOf("_"));
    				String kode = check[i].substring(check[i].indexOf("_")+1,check[i].lastIndexOf("_"));
    				String bdev = check[i].substring(check[i].lastIndexOf("_")+1,check[i].length());*/
    				
    				String id_stok = check[i];
    				
    				String stok = ServletRequestUtils.getStringParameter(request, "stok_"+id_stok,"");
    				String prefix = ServletRequestUtils.getStringParameter(request, "prefix_"+id_stok,"");
					
    				formbrosur.setMsf_amount(Integer.parseInt(stok));
    				formbrosur.setLsjs_prefix(prefix);
    				formbrosur.setBusdev(busdev);
    				formbrosur.setJn_brosur(Integer.parseInt(jenis));
    				
					bacManager.updateStokBrosur(formbrosur);
    			}
    			map.put("pesan", "update sukses");
    		}
    	}
    	
    	if(request.getParameter("simpan") != null) {
    		String prefix = ServletRequestUtils.getStringParameter(request, "prefix","");
    		String nm_brosur = ServletRequestUtils.getStringParameter(request, "nm_brosur","");
    		String jenis2 = ServletRequestUtils.getStringParameter(request, "jenis2", "1");
        	String busdev2 = ServletRequestUtils.getStringParameter(request, "busdev2", "1");
    		
    		String sukses = bacManager.TambahBrosur(prefix,nm_brosur,busdev2,currentUser, jenis2);
    		
    		map.put("pesan", sukses);
    		map.put("tab", "2");
    	}
    	
    	List brosur = bacManager.selectStokBrosurBusDev(busdev, jenis);
    	
    	map.put("brosur", brosur);
    	//map.put("lsBusdev", lsBusdev);
    	/*map.put("lsJenis", lsJenis);*/
    	map.put("busdev", busdev);
    	map.put("jenis", jenis);
    	//map.put("nm_busdev", nm_busdev);
    	return new ModelAndView("bas/updateBrosur", map);
    }
    
    /**
     * Insert keterangan spaj belum upload scan dan transfer ke uw
     * @author Canpri
     * @since 7 Oct 2013
     * @param 
     * http://localhost/E-Lions/bas/spaj.htm?window=ket_upload_transfer
     */
    public ModelAndView ket_upload_transfer(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User currentUser=(User)request.getSession().getAttribute("currentUser");
    	Map map = new HashMap();
    	
    	String spaj = ServletRequestUtils.getStringParameter(request, "spaj", "");
    	String lus_id = ServletRequestUtils.getStringParameter(request, "id", "");
    	
    	//User user = elionsManager.selectUser(lus_id);
    	
    	if(request.getParameter("simpan") != null) {
    		String keterangan = ServletRequestUtils.getStringParameter(request, "keterangan", "");
    		
    		elionsManager.insertMstPositionSpaj(lus_id, "[BAS] "+keterangan.toUpperCase(), spaj, 0);
    		map.put("pesan", "Keterangan sudah diinput.");
    	}
    	
    	map.put("spaj", spaj);
    	map.put("lus_id", lus_id);
    	return new ModelAndView("bas/ket_upload_transfer_spaj", map);
    }
    
    /**
	 * Halaman innerFrame untuk approval Promo Item, isinya daftar form yang direquest oleh cabang tertentu 
	 * 
	 * @author Canpri
	 * @since Apr 11, 20134 (8:45:13 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView daftarFormPromo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		String lca_id = ServletRequestUtils.getStringParameter(request, "lca_id", "");
		int posisi = ServletRequestUtils.getIntParameter(request, "posisi", 99);
		String pos=ServletRequestUtils.getStringParameter(request, "pos","");
		int all = ServletRequestUtils.getIntParameter(request, "all", 0);
		//Integer flag=ServletRequestUtils.getIntParameter(request, "flag");//1=cabang 2=agen
		String msf_id = ServletRequestUtils.getStringParameter(request, "msf_id", "");
		String index = ServletRequestUtils.getStringParameter(request, "index", "");
		
		cmd.put("posisi",posisi);
		if(!"".equals(lca_id)) {
			FormSpaj formBrosur = new FormSpaj();
			User currentUser = (User) request.getSession().getAttribute("currentUser");
			formBrosur.setMss_jenis(3);
			if(!lca_id.equals("ALL_BRANCH")) formBrosur.setLca_id(lca_id);
			formBrosur.setMsab_id(0);		
			if(all==0){
				formBrosur.setPosisi(posisi);
				formBrosur.setPos(pos);
			}	
			if(currentUser.getName().equals("MARIA DF") || currentUser.getName().equals("NOVI_AGUSTINA1")) {
				formBrosur.setMcl_first("special");
			}
			List<FormSpaj> daftar = uwManager.selectDaftarFormBrosur(formBrosur);
			
			if(daftar.isEmpty()) {
				cmd.put("pesan", "Tidak ada permintaan untuk cabang ini");
			}else {
				for(FormSpaj f : daftar) {
					f.setWarna((String) daftarWarna.get(f.getStatus_form()));
                    String tanggal=FormatDate.toString(f.getMsfh_dt());                    
                    f.setMsfh_dt(defaultDateFormat.parse(tanggal));
                    
				}
			}
			cmd.put("lca_id", lca_id);
			cmd.put("daftarForm", daftar);
			cmd.put("msf_id", msf_id);
			cmd.put("index", index);
		}else {
			cmd.put("pesan", "Silahkan pilih salah satu cabang terlebih dahulu");
		}

		return new ModelAndView("bas/daftar_form_promo", "cmd", cmd);
	}
	
	/**
	 * Halaman innerFrame untuk kantor pemasaran 
	 * 
	 * @author Canpri
	 * @since Apr 14, 20134 (8:45:13 AM)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView daftarKantor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map cmd = new HashMap();
		String kota = ServletRequestUtils.getStringParameter(request, "kota", "");
		String lar_id = ServletRequestUtils.getStringParameter(request, "lar_id", "");
		String index = ServletRequestUtils.getStringParameter(request, "index", "");
		List daftar = new ArrayList();
		
		String file =  ServletRequestUtils.getStringParameter(request, "file",null);
		
		if(file==null){
			if(!kota.equals("")){
				daftar = bacManager.selectLstAddrRegion(kota);
				
				cmd.put("lar_id", lar_id);
				cmd.put("index", index);
				cmd.put("kota", kota);
				cmd.put("daftarKantor", daftar);
			}else{
				cmd.put("pesan", "Silahkan pilih kota terlebih dahulu");
			}

			return new ModelAndView("bas/daftar_kantor", "cmd", cmd);
		}else{
			String directory = props.getProperty("pdf.dir.surat_domisili")+"\\"+lar_id+"\\";
			FileUtil.downloadFile(directory, file, response, "inline");
			return null;
		}
	}
	
	/**
	 * Halaman untuk monitoring penerimaan spaj
	 * 
	 * @author Canpri, Rahmayanti, Ridhaal
	 * @since Apr 21, 2013 (8:45:13 AM), Mar 05, 2015, Mar 4, 2016
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * 
	 * http://localhost/E-Lions/bas/spaj.htm?window=monitoring_penerimaan_spaj
	 */
	public ModelAndView monitoring_penerimaan_spaj(HttpServletRequest request, HttpServletResponse response) throws Exception {
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Map map = new HashMap();
		String pesan = "";
		Integer admin = 0;
		Integer cek = 0;
		String statEditToFurt = ServletRequestUtils.getStringParameter(request, "statEditToFurt", "");

//		if(currentUser.getLus_id().equals("133")|| currentUser.getLus_id().equals("5"))
//			admin = 1;
		if( currentUser.getLde_id().equals("29") )
			admin = 2;

		Integer pilihan = 2; //untuk menandakan flag further(1) dan non further (2)
		Integer flag_form = 2;

		ArrayList daftar_spaj_agency_further = new ArrayList();
		ArrayList daftar_spaj_agency_nonfurther = new ArrayList();

		String bdate = ServletRequestUtils.getStringParameter(request, "bdate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
		String edate = ServletRequestUtils.getStringParameter(request, "edate", defaultDateFormat.format(elionsManager.selectSysdate(0))); 

		if(request.getParameter("btnSave1") !=null){

			Date dateNow = elionsManager.selectSysdateSimple();
			String status = ServletRequestUtils.getStringParameter(request, "status", "");  
			String searchList = ServletRequestUtils.getStringParameter(request, "searchList","");
			String searchText = ServletRequestUtils.getStringParameter(request, "searchText","");
			cek = ServletRequestUtils.getIntParameter(request, "cek");


			Integer jenis = 1; 
			String no_blanko = ServletRequestUtils.getStringParameter(request, "no_blanko1", "");
			String msag_id = ServletRequestUtils.getStringParameter(request, "kd_agen1", "");
			String nama_pemegang = ServletRequestUtils.getStringParameter(request, "nama_pemegang","");
			String informasi = ServletRequestUtils.getStringParameter(request, "informasi","");
			String emailcc = ServletRequestUtils.getStringParameter(request, "emailcc","");
			//    		monitoring spaj further
			if(status.equals("f")){
				pilihan = 1;
				HashMap blanko = (HashMap) bacManager.selectBlankoMonitoringSpaj(no_blanko, "update_further"); 
				HashMap blankoNFtoF = (HashMap) bacManager.selectBlankoMonitoringSpaj(no_blanko, "update_NF_to_further"); 

				DateFormat times = new SimpleDateFormat("HH:mm:ss");
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Calendar calobj = Calendar.getInstance();
				String time = times.format(calobj.getTime());

				Date tgl_kembali_ke_agen = null;
				Date tgl_terima_agen = null;

				String kembali_ke_agen = ServletRequestUtils.getStringParameter(request, "tgl_kembali_agen", "");    			
				if(kembali_ke_agen!=null){
					String temp_kembali_ke_agen = kembali_ke_agen+" "+time;
					tgl_kembali_ke_agen = df.parse(temp_kembali_ke_agen);	
				}    			
				String terima_agen = ServletRequestUtils.getStringParameter(request, "tgl_terima_agen", null);
				if(terima_agen!=null){
					String temp_terima_agen = terima_agen+" "+time;
					tgl_terima_agen = df.parse(temp_terima_agen);
				}

				String kategori_further = ServletRequestUtils.getStringParameter(request, "kategori_further","");
				if(kategori_further.equals("LAIN-LAIN")){
					String other = ServletRequestUtils.getStringParameter(request, "other", "");
					kategori_further = other;
				}
				String keterangan_further = ServletRequestUtils.getStringParameter(request, "keterangan_further","");        	

				if(blanko!=null && cek == 1){

					bacManager.updateMonitoringSpaj(no_blanko, jenis, "edit_data_further", tgl_kembali_ke_agen, tgl_terima_agen, kategori_further, keterangan_further,msag_id,nama_pemegang,emailcc);
					pesan = "Data Berhasil di Edit";
					status = "f";
				}else 
					if(blankoNFtoF!=null && statEditToFurt.equals("1")){

						
						statEditToFurt = "";
						status = "f";
						tgl_kembali_ke_agen = elionsManager.selectSysdate();

						bacManager.updateMonitoringSpaj(no_blanko, jenis, "update_NF_to_further", tgl_kembali_ke_agen, tgl_terima_agen, kategori_further, keterangan_further,msag_id,nama_pemegang,emailcc);
						pesan = "Data Berhasil diubah dan di jadikan ke Further";


					}else {
						
						pesan = bacManager.insertMonitoringSpaj(no_blanko, msag_id, nama_pemegang,informasi, null, currentUser, jenis, "Monitoring penerimaan SPAJ", kategori_further, elionsManager.selectSysdate(), tgl_terima_agen, keterangan_further, dateNow, 1, emailcc);
						status = "f";
					}    		

				
			}
			//    		monitoring spaj non further
			else if(status.equals("nf") && cek == 1){
				status = "f";
				Map mapCekBlanko = bacManager.selectMstSpajAoFurtherOrNonFurther(no_blanko,status);

				DateFormat times = new SimpleDateFormat("HH:mm:ss");
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Calendar calobj = Calendar.getInstance();
				String time = times.format(calobj.getTime());

				Date tgl_kembali_ke_agen = null;
				Date tgl_terima_agen = null;

				String kembali_ke_agen = ServletRequestUtils.getStringParameter(request, "tgl_kembali_agen", null);    			
				if(kembali_ke_agen!=null){
					String temp_kembali_ke_agen = kembali_ke_agen+" "+time;
					tgl_kembali_ke_agen = df.parse(temp_kembali_ke_agen);	
				}    			
				String terima_agen = ServletRequestUtils.getStringParameter(request, "tgl_terima_agen", null);
				if(terima_agen!=null){
					String temp_terima_agen = terima_agen+" "+time;
					tgl_terima_agen = df.parse(temp_terima_agen);
				}

				String kategori_further = ServletRequestUtils.getStringParameter(request, "kategori_further","");
				if(kategori_further.equals("LAIN-LAIN")){
					String other = ServletRequestUtils.getStringParameter(request, "other", "");
					kategori_further = other;
				}
				String keterangan_further = ServletRequestUtils.getStringParameter(request, "keterangan_further","");        	


				String jenis_further = (String) mapCekBlanko.get("JENIS_FURTHER");
				String keterangan = (String) mapCekBlanko.get("KETERANGAN_FURTHER");

				if(jenis_further==null||keterangan==null)	bacManager.updateMonitoringSpaj(no_blanko, jenis, "update_further", tgl_kembali_ke_agen, tgl_terima_agen, jenis_further, keterangan,msag_id,nama_pemegang,emailcc);         				
				else bacManager.updateMonitoringSpaj(no_blanko, jenis, "update_further", tgl_kembali_ke_agen, tgl_terima_agen, kategori_further, keterangan_further,msag_id,nama_pemegang,emailcc);
				pesan = "Data Berhasil diubah menjadi Non Further";

				pilihan = 2;
				HashMap blanko = (HashMap) bacManager.selectBlankoMonitoringSpaj(no_blanko, "update_further");
				status = "nf";

			}
			else {

				pilihan = 2;
				pesan = bacManager.insertMonitoringSpaj(no_blanko, null, nama_pemegang,informasi, null, currentUser, jenis, "Monitoring penerimaan SPAJ", null, null, null, null, null, null, null);
				status = "nf";
			}
			//    		map.put("status", status);
			//    		map.put("statEditToFurt", null);


			map.put("no_blanko1", null);
			map.put("kd_agen1", null);
			map.put("nama_pemegang", null);
			map.put("informasi", null);
			map.put("tgl_kembali_agen", null);
			map.put("tgl_terima_dari_agen", null);
			map.put("keterangan_further", null);   
			map.put("email_cc", null);
			map.put("statEditToFurt", null);

			map.put("statEditToFurt", statEditToFurt);
			map.put("status", status);
			map.put("flag_form", flag_form);
			map.put("searchList", searchList);
			map.put("searchText", searchText);

			cek = 0;
			statEditToFurt = "";
		}

		//    	delete data monitoring spaj further
		if(request.getParameter("btnDel1") != null) {
			String status = "f";  


			String searchList = ServletRequestUtils.getStringParameter(request, "searchList","");
			String searchText = ServletRequestUtils.getStringParameter(request, "searchText","");
			pilihan = 1;
			flag_form = 2;
			Integer jenis = 1; 
			String check[] = request.getParameterValues("chbox");
			if(request.getParameterValues("chbox") != null){
				for(int i=0;i<check.length;i++){
					String no_blanko = check[i];    				
					bacManager.updateMonitoringSpaj(no_blanko, jenis, "old", null, null, null, null,null,null,null);
				}
			}
			cek = 0;
			pesan = "Hapus data berhasil";
			map.put("status", status);
			map.put("flag_form", flag_form);
			map.put("searchList", searchList);
			map.put("searchText", searchText);

		}

		//    	delete data monitoring spaj non further
		if(request.getParameter("btnDel2") != null) {
			String status = "nf"; 

			String searchList = ServletRequestUtils.getStringParameter(request, "searchList","");
			String searchText = ServletRequestUtils.getStringParameter(request, "searchText","");
			pilihan = 2;
			flag_form = 2;
			Integer jenis = 1;
			String check[] = request.getParameterValues("chbox");
			if(request.getParameterValues("chbox") != null){
				for(int i=0;i<check.length;i++){
					String no_blanko = check[i];    				
					bacManager.updateMonitoringSpaj(no_blanko, jenis, "old", null, null, null, null,null,null,null);
				}
			}
			cek = 0;
			pesan = "Hapus data berhasil";
			map.put("status", status);
			map.put("flag_form", flag_form);
			map.put("searchList", searchList);
			map.put("searchText", searchText);
			map.put("btnEditToFurt", null);

		}

		//    	edit data monitoring spaj non further
		if(request.getParameter("btnEdit") != null) {
			String status = "nf";    
			String searchList = ServletRequestUtils.getStringParameter(request, "searchList","");
			String searchText = ServletRequestUtils.getStringParameter(request, "searchText","");
			pilihan = 2;
			flag_form = 2;
			Integer jenis = 1;
			String check[] = request.getParameterValues("chbox");
			String a = "test";
			if(request.getParameterValues("chbox") != null){
				for(int i=0;i<check.length;i++){
					String no_blanko = check[i];    				
					bacManager.updateMonitoringSpaj(no_blanko, jenis, "update_to_further", null, null, null, null,null,null,null);
				}
			}

			cek = 0;
			pesan = "Hapus data berhasil";
			map.put("status", status);
			map.put("flag_form", flag_form);
			map.put("searchList", searchList);
			map.put("searchText", searchText);
			map.put("btnEditToFurt", null);

		}

		// report monitoring spaj lengkap dan pending   	
		if(request.getParameter("btnReport") != null) {
			String jn_report = ServletRequestUtils.getStringParameter(request, "jn_report", "pdf");
			bdate = ServletRequestUtils.getStringParameter(request, "bdate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
			edate = ServletRequestUtils.getStringParameter(request, "edate", defaultDateFormat.format(elionsManager.selectSysdate(0)));

			ArrayList data = Common.serializableList(bacManager.selectReportMonitoringSPAJ(bdate, edate, currentUser.getLus_id(), 1, admin,null,null));

			if(data.size() > 0){ //bila ada data
				ServletOutputStream sos = response.getOutputStream();
				File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.report_monitoring_spaj") + ".jasper");
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

				Map<String, Object> params = new HashMap<String, Object>();
				String user_input = currentUser.getLus_full_name();

				params.put("bdate", bdate);
				params.put("edate", edate);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

				if(jn_report.equals("xls")){
					response.setContentType("application/vnd.ms-excel");
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
					exporter.exportReport();
				}else if(jn_report.equals("pdf")){
					JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
				}
				sos.flush();
				sos.close();
			}else{ //bila tidak ada data
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('Tidak ada data');window.close();</script>");
				sos.flush();
				sos.close();
			}
			return null;
		}

		//    	view data di monitoring spaj non further
		else if(request.getParameter("btnTampil") != null){

			bdate = ServletRequestUtils.getStringParameter(request, "bdate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
			edate = ServletRequestUtils.getStringParameter(request, "edate", defaultDateFormat.format(elionsManager.selectSysdate(0)));
			daftar_spaj_agency_nonfurther = Common.serializableList(bacManager.selectReportMonitoringSPAJ(bdate, edate, currentUser.getLus_id(), 0, admin,null,null));  
			cek = 0;
		}

		//		Search data di monitoring spaj non further (*Ridhaal)    	
		else if(request.getParameter("btnSearch") != null){


			String searchList = ServletRequestUtils.getStringParameter(request, "searchList","");
			String searchText = ServletRequestUtils.getStringParameter(request, "searchText","");


			daftar_spaj_agency_nonfurther = Common.serializableList(bacManager.selectReportMonitoringSPAJ(bdate, edate, currentUser.getLus_id(), 3, admin,searchList,searchText));  
			map.put("searchList", searchList);
			map.put("searchText", searchText);
			cek = 0;
		}

		else{
			daftar_spaj_agency_further = Common.serializableList(bacManager.selectReportMonitoringSPAJFurther(defaultDateFormat.format(elionsManager.selectSysdate(-1)), defaultDateFormat.format(elionsManager.selectSysdate(0)), currentUser.getLus_id(), admin, 0));   
			daftar_spaj_agency_nonfurther = Common.serializableList(bacManager.selectReportMonitoringSPAJ(defaultDateFormat.format(elionsManager.selectSysdate(-1)), defaultDateFormat.format(elionsManager.selectSysdate(0)), currentUser.getLus_id(), 0, admin,null, null));  		
		}




		//    	report monitoring spaj further
		if(request.getParameter("btnReport1") != null) {
			String jn_report = ServletRequestUtils.getStringParameter(request, "jn_report1", "pdf");
			bdate = ServletRequestUtils.getStringParameter(request, "bdate1", defaultDateFormat.format(elionsManager.selectSysdate(0)));
			edate = ServletRequestUtils.getStringParameter(request, "edate1", defaultDateFormat.format(elionsManager.selectSysdate(0)));

			ArrayList data = Common.serializableList(bacManager.selectReportMonitoringSPAJFurther(bdate, edate, currentUser.getLus_id(), admin, 0));

			if(data.size() > 0){ //bila ada data
				ServletOutputStream sos = response.getOutputStream();
				File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.report_penerimaan_berkas") + ".jasper");
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

				Map<String, Object> params = new HashMap<String, Object>();
				String user_input = currentUser.getLus_full_name();

				params.put("bdate", bdate);
				params.put("edate", edate);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

				if(jn_report.equals("xls")){
					response.setContentType("application/vnd.ms-excel");
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
					exporter.exportReport();
				}else if(jn_report.equals("pdf")){
					JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
				}
				sos.flush();
				sos.close();
			}else{ //bila tidak ada data
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('Tidak ada data');window.close();</script>");
				sos.flush();
				sos.close();
			}
			return null;
		}

		//    	report monitoring spaj non further
		if(request.getParameter("btnReport2") != null) {
			String jn_report = ServletRequestUtils.getStringParameter(request, "jn_report3", "pdf");
			bdate = ServletRequestUtils.getStringParameter(request, "bdate2", defaultDateFormat.format(elionsManager.selectSysdate(0)));
			edate = ServletRequestUtils.getStringParameter(request, "edate2", defaultDateFormat.format(elionsManager.selectSysdate(0)));

			ArrayList data = Common.serializableList(bacManager.selectReportMonitoringSPAJ(bdate, edate, currentUser.getLus_id(), 0, admin,null,null));

			if(data.size() > 0){ //bila ada data
				ServletOutputStream sos = response.getOutputStream();
				File sourceFile = Resources.getResourceAsFile(props.getProperty("report.bas.report_penerimaan_spaj") + ".jasper");
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);

				Map<String, Object> params = new HashMap<String, Object>();
				String user_input = currentUser.getLus_full_name();

				params.put("bdate", bdate);
				params.put("edate", edate);

				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(data));

				if(jn_report.equals("xls")){
					response.setContentType("application/vnd.ms-excel");
					JRXlsExporter exporter = new JRXlsExporter();
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, sos);
					exporter.exportReport();
				}else if(jn_report.equals("pdf")){
					JasperExportManager.exportReportToPdfStream(jasperPrint, sos);
				}
				sos.flush();
				sos.close();
			}else{ //bila tidak ada data
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('Tidak ada data');window.close();</script>");
				sos.flush();
				sos.close();
			}
			return null;
		}


		if( (statEditToFurt.equals("1"))){

			String no_blankos = ServletRequestUtils.getStringParameter(request, "btnEditToFurtA", "");
			String status = ServletRequestUtils.getStringParameter(request, "status", ""); 
			String searchList = ServletRequestUtils.getStringParameter(request, "searchList", "");
			String searchText = ServletRequestUtils.getStringParameter(request, "searchText", "");

			cek = 0;
			pilihan = 1;
			flag_form = 2;
			Integer jenis = 3;
			Map mapCekBlanko = bacManager.selectMstSpajAoFurtherOrNonFurther(no_blankos,status);
			if(mapCekBlanko!=null){
				String jenis_further = (String) mapCekBlanko.get("JENIS_FURTHER");

				map.put("no_blanko1", no_blankos);
				map.put("kd_agen1", mapCekBlanko.get("MSAG_ID"));
				map.put("nama_pemegang", mapCekBlanko.get("NAME_CUSTOMER"));
				map.put("informasi", mapCekBlanko.get("INFORMASI"));
				map.put("tgl_kembali_agen", mapCekBlanko.get("TGL_KEMBALI_KE_AGEN"));
				map.put("tgl_terima_agen", mapCekBlanko.get("TGL_TERIMA_DARI_AGEN"));
				if(jenis_further!=null){
					if(!jenis_further.equals("PENGISIAN SPAJ KURANG LENGKAP")&&!jenis_further.equals("KEKURANGAN DOKUMEN (BSB DAN KELENGKAPAN LAINNYA)")&&!jenis_further.equals("DOKUMEN TIDAK JELAS")
							&& !jenis_further.equals("PROPOSAL TIDAK DITANDATANGANI / TIDAK ADA)")&&!jenis_further.equals("MENUNGGU KONFIRMASI AKTUARIA UNTUK SPECIAL CASE")&&!jenis_further.equals("FORM YANG DIGUNAKAN TIDAK SESUAI"))
						jenis_further = "LAIN-LAIN";

				}

				map.put("select_kategori_further", jenis_further);
				map.put("keterangan_further", mapCekBlanko.get("KETERANGAN_FURTHER"));
				map.put("email_cc", mapCekBlanko.get("EMAIL_CC"));


			}

			map.put("status", "f");
			map.put("flag_form", flag_form);

			map.put("searchList", searchList);
			map.put("searchText", searchText);
			map.put("statEditToFurt", statEditToFurt);

		}

		//    	tombol cek untuk monitoring spaj further
		if(request.getParameter("btnCek") != null){
			String no_blanko = ServletRequestUtils.getStringParameter(request, "no_blanko1", "");
			String status = ServletRequestUtils.getStringParameter(request, "status", ""); 
			String searchList = ServletRequestUtils.getStringParameter(request, "searchList", "");
			String searchText = ServletRequestUtils.getStringParameter(request, "searchText", "");

			statEditToFurt = "";
			pilihan = 1;
			flag_form = 2;
			Integer jenis = 3;
			Map mapCekBlanko = bacManager.selectMstSpajAoFurtherOrNonFurther(no_blanko,status);
			if(mapCekBlanko!=null){
				String jenis_further = (String) mapCekBlanko.get("JENIS_FURTHER");
				//    			Date tgl_terima_agen = (Date) mapCekBlanko.get("TGL_TERIMA_DARI_AGEN");
				map.put("no_blanko1", no_blanko);
				map.put("kd_agen1", mapCekBlanko.get("MSAG_ID"));
				map.put("nama_pemegang", mapCekBlanko.get("NAME_CUSTOMER"));
				map.put("informasi", mapCekBlanko.get("INFORMASI"));
				map.put("tgl_kembali_agen", mapCekBlanko.get("TGL_KEMBALI_KE_AGEN"));
				map.put("tgl_terima_agen", mapCekBlanko.get("TGL_TERIMA_DARI_AGEN"));
				if(jenis_further!=null){
					if(!jenis_further.equals("PENGISIAN SPAJ KURANG LENGKAP")&&!jenis_further.equals("KEKURANGAN DOKUMEN (BSB DAN KELENGKAPAN LAINNYA)")&&!jenis_further.equals("DOKUMEN TIDAK JELAS")
							&& !jenis_further.equals("PROPOSAL TIDAK DITANDATANGANI / TIDAK ADA)")&&!jenis_further.equals("MENUNGGU KONFIRMASI AKTUARIA UNTUK SPECIAL CASE")&&!jenis_further.equals("FORM YANG DIGUNAKAN TIDAK SESUAI"))
						jenis_further = "LAIN-LAIN";

				}
				map.put("select_kategori_further", jenis_further);
				map.put("keterangan_further", mapCekBlanko.get("KETERANGAN_FURTHER"));
				map.put("email_cc", mapCekBlanko.get("EMAIL_CC"));

				cek = 1;
			}
			else{
				ServletOutputStream sos = response.getOutputStream();
				sos.println("<script>alert('Tidak ada data');window.close();</script>");
				sos.flush();
				sos.close();
				return null;
			}
			map.put("status", status);
			map.put("flag_form", flag_form);
			map.put("searchList", searchList);
			map.put("searchText", searchText);
			map.put("searchText", statEditToFurt);


		}

		//    	button cancel
		if(request.getParameter("btnCancel1") != null){
			statEditToFurt = "";
			String status = ServletRequestUtils.getStringParameter(request, "status", "");   
			String searchList = ServletRequestUtils.getStringParameter(request, "searchList", "");
			String searchText = ServletRequestUtils.getStringParameter(request, "searchText", "");
			flag_form = 2;
			if(status.equals("f")){
				pilihan = 1;
			}
			else{
				pilihan = 2;
			}

			cek = 0;
			
			map.put("no_blanko1", null);
			map.put("kd_agen1", null);
			map.put("nama_pemegang", null);
			map.put("informasi", null);
			map.put("tgl_kembali_agen", null);
			map.put("tgl_terima_dari_agen", null);
			map.put("keterangan_further", null);   
			map.put("email_cc", null);
			map.put("statEditToFurt", null);

			map.put("status", status);
			map.put("flag_form", flag_form);
			map.put("searchList", searchList);
			map.put("searchText", searchText);

		}
//		ArrayList daftar_spaj_blm_input = Common.serializableList(bacManager.selectReportMonitoringSPAJ(defaultDateFormat.format(elionsManager.selectSysdate(0)), defaultDateFormat.format(elionsManager.selectSysdate(0)), currentUser.getLus_id(), 2, admin,null,null));
//		ArrayList daftar_spaj_agency_further2 = Common.serializableList(bacManager.selectReportMonitoringSPAJFurther(null, null, currentUser.getLus_id(), admin, 1));
		daftar_spaj_agency_further = Common.serializableList(bacManager.selectReportMonitoringSPAJFurther(defaultDateFormat.format(elionsManager.selectSysdate(-1)), defaultDateFormat.format(elionsManager.selectSysdate(0)), currentUser.getLus_id(), admin, 0));

		map.put("bdate", bdate);
		map.put("edate", edate);
		map.put("bdate1", bdate);
		map.put("edate1", edate);
		map.put("pesan", pesan);
//		map.put("daftar_spaj_blm_input", daftar_spaj_blm_input);
		map.put("daftar_spaj_agency_further", daftar_spaj_agency_further);
//		map.put("daftar_spaj_agency_further2", daftar_spaj_agency_further2);
		map.put("daftar_spaj_agency_nonfurther", daftar_spaj_agency_nonfurther);
		map.put("pilihan", pilihan);
		map.put("flag_form", flag_form);
		map.put("kategori_further", DroplistManager.getInstance().get("FURTHER.xml", "", request));

		map.put("cek", cek);
		map.put("statEditToFurt", statEditToFurt);


		return new ModelAndView("bas/monitoring_penerimaan_spaj", map);
	}

}