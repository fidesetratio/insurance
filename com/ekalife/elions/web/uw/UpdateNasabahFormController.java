/**
 * @author  : Ferry Harlim
 * @created : Sep 10, 2007 2:32:56 PM
 */
package com.ekalife.elions.web.uw;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ekalife.elions.model.AddressBilling;
import com.ekalife.elions.model.DataNasabah;
import com.ekalife.elions.model.Datausulan;
import com.ekalife.elions.model.Keluarga;
import com.ekalife.elions.model.Pemegang2;
import com.ekalife.elions.model.Policy;
import com.ekalife.elions.model.Tertanggung;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.utils.Common;
import com.ekalife.utils.DroplistManager;
import com.ekalife.utils.FormatDate;
import com.ekalife.utils.parent.ParentFormController;

public class UpdateNasabahFormController extends ParentFormController {
	protected final Log logger = LogFactory.getLog( getClass() );
	private ElionsManager elionsManager;
	private Properties props;
	
	public void setProps(Properties props) {this.props = props;}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Double.class, null, doubleEditor); 
		binder.registerCustomEditor(Integer.class, null, integerEditor); 
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(df,true));
//		binder.registerCustomEditor(Date.class, "pemegang.listKeluarga[0].tanggal_lahir", new CustomDateEditor(df,true));
//		binder.registerCustomEditor(Date.class, "pemegang.listKeluarga[1].tanggal_lahir", new CustomDateEditor(df,true));
//		binder.registerCustomEditor(Date.class, "pemegang.listKeluarga[2].tanggal_lahir", new CustomDateEditor(df,true));
//		binder.registerCustomEditor(Date.class, "pemegang.listKeluarga[3].tanggal_lahir", new CustomDateEditor(df,true));
//		binder.registerCustomEditor(Date.class, "pemegang.mspe_date_birth", new CustomDateEditor(df,true));
	}	

	@Override
	protected Map referenceData(HttpServletRequest request, Object cmd, Errors errors) throws Exception {
		Map refData = new HashMap();		
		//DataNasabah nasabah = (DataNasabah) cmd;
		refData.put("select_industri",DroplistManager.getInstance().get("BIDANG_INDUSTRI.xml","",request));
		refData.put("select_pekerjaan",DroplistManager.getInstance().get("KLASIFIKASI_PEKERJAAN.xml","",request));
		refData.put("select_marital",DroplistManager.getInstance().get("MARITAL.xml","ID",request));
		refData.put("select_penghasilan",DroplistManager.getInstance().get("PENGHASILAN.xml","",request));
		refData.put("select_dana",DroplistManager.getInstance().get("SUMBER_PENDANAAN.xml","",request));
		refData.put("select_hasil",DroplistManager.getInstance().get("SUMBER_PENGHASILAN.xml","",request));
		return refData;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		
		DataNasabah nasabah = new DataNasabah();
		//default status aksep diterima
		nasabah.setLssh_id(1);
		
		if(request.getParameter("msch_bas_tgl_terima")==null)
			nasabah.setMsch_bas_tgl_terima(FormatDate.toString(elionsManager.selectSysdate()));
		else
			nasabah.setMsch_bas_tgl_terima(request.getParameter("msch_bas_tgl_terima"));
		nasabah.setCekUpdate(0);
		nasabah.setAlasan("");
		String status_submit = ServletRequestUtils.getStringParameter(request, "status_submit", "");
		String spaj = ServletRequestUtils.getStringParameter(request, "spaj");
		nasabah.setSpajAwal(spaj);
		Map mapPolis = (HashMap) elionsManager.selectPemegang(spaj);
		String nopolis = (String) mapPolis.get("MSPO_POLICY_NO");
		nasabah.setNo_polis(nopolis);
		if (request.getSession().getAttribute("jumlah_sisa_polis") !=  null)
		{
			nasabah.setJumlah_sisa_polis(Integer.parseInt(request.getSession().getAttribute("jumlah_sisa_polis").toString()));
			nasabah.setListPolis2((List)request.getSession().getAttribute("polis2"));
		}
		User user = (User) request.getSession().getAttribute("currentUser");
		nasabah.setPemegang((Pemegang2)this.elionsManager.selectPemegangPolisUpdateNasabah(spaj));
		String mclId=nasabah.getPemegang().getMcl_id();
		nasabah.setAddressBilling((AddressBilling)this.elionsManager.selectAddressBilling(spaj));
		nasabah.setTertanggung((Tertanggung)this.elionsManager.selectTertanggungUpdateNasabah(spaj));
		nasabah.setDatausulan((Datausulan) this.elionsManager.selectDataUsulanutama(spaj));
		nasabah.getDatausulan().setKeterangan(request.getParameter("ket"));

//
		nasabah.setAgen(this.elionsManager.select_detilagen(spaj));
		nasabah.getDatausulan().setStatus_submit(status_submit);
//			
		List keluarga_pp = this.elionsManager.selectMstKeluarga(spaj, new Integer(1));
		if (keluarga_pp.size() == 0)
		{
			keluarga_pp= new ArrayList();
			Keluarga data = new Keluarga();
			data.setNo(new Integer(1));
			data.setLsre_id(new Integer(5));
			data.setLsre_relation("SUAMI/ISTRI");
			data.setInsured(new Integer(1));
			data.setReg_spaj(spaj);
			keluarga_pp.add(data);
		}
		int totPp=6-keluarga_pp.size();
		for (int i = 0 ; i <totPp ; i++)
		{
			Keluarga data1 = new Keluarga();
			data1.setNo(i+2);
			data1.setLsre_id(new Integer(4));
			data1.setLsre_relation("Anak" + (i+1));
			data1.setInsured(new Integer(1));
			data1.setReg_spaj(spaj);
			keluarga_pp.add(data1);
		}
		
		List keluarga_ttg = this.elionsManager.selectMstKeluarga(spaj, new Integer(0));
		if (keluarga_ttg.size() == 0)
		{
			keluarga_ttg=  new ArrayList();
			Keluarga data = new Keluarga();
			data.setLsre_id(new Integer(5));
			data.setLsre_relation("SUAMI/ISTRI");
			data.setInsured(new Integer(0));
			data.setReg_spaj(spaj);
			keluarga_ttg.add(data);
		}
		int totTtg=6-keluarga_ttg.size();
		for (int i = 0 ; i <totTtg ; i++)
		{
			Keluarga data1 = new Keluarga();
			data1.setNo(i+2);
			data1.setLsre_id(new Integer(4));
			data1.setLsre_relation("Anak" + (i+1));
			data1.setInsured(new Integer(0));
			data1.setReg_spaj(spaj);
			keluarga_ttg.add(data1);
		}
		
		for(int i=0;i<keluarga_pp.size();i++){
			Keluarga data1 = (Keluarga)keluarga_pp.get(i);
			data1.setNo(i+1);
			keluarga_pp.set(i, data1);
		}
		for(int i=0;i<keluarga_ttg.size();i++){
			Keluarga data1 = (Keluarga)keluarga_ttg.get(i);
			data1.setNo(i+1);
			keluarga_ttg.set(i, data1);
		}
			nasabah.getPemegang().setListKeluarga(keluarga_pp);
			nasabah.getTertanggung().setListKeluarga(keluarga_ttg);
		 
		nasabah.getDatausulan().setMspo_policy_no(nopolis);
		
		ArrayList lsPolicy = new ArrayList();
		ArrayList listpolis = new ArrayList();
		HashMap policy = new HashMap();
//		String noId=request.getParameter("noId");
//		noId = noId.replace(".", "");
		lsPolicy = Common.serializableList(elionsManager.selectAllPolicy(mclId, null));
		nasabah.setLsPolicy(lsPolicy);
		Integer count = new Integer(0);
		if (status_submit.equalsIgnoreCase(""))
		{
			if(lsPolicy.isEmpty()==false){
				for (int i = 0 ; i < lsPolicy.size() ; i++)
				{
					policy = (HashMap) lsPolicy.get(i);
					nopolis=(String)policy.get("MSPO_POLICY_NO");
					//logger.info(nopolis+" = "+nasabah.getDatausulan().getMspo_policy_no());
					if (!nopolis.equalsIgnoreCase(nasabah.getDatausulan().getMspo_policy_no()))
					{
						Policy polis = new Policy();
						polis.setMspo_policy_no(nopolis);
						polis.setCek(new Integer(1));
						listpolis.add(polis);
						count = new Integer(count.intValue() + 1);
					}
				}
			}
			if (lsPolicy.size() < 14)
			{
				for (int i = count.intValue() - 1 ; i <14 ; i++)
				{
					Policy polis = new Policy();
					polis.setMspo_policy_no(null);
					polis.setCek(new Integer(0));
					listpolis.add(polis);
					count = new Integer(count.intValue() + 1);
				}
			}
		}else{
			listpolis = (ArrayList) request.getSession().getAttribute("polis");
		}
		nasabah.setListPolisLain1(listpolis);
		return nasabah;
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object cmd, BindException err)
			throws Exception {
		return new ModelAndView("view/update_nasabah");

	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		DataNasabah nasabah = (DataNasabah) cmd;
		if(request.getParameter("msch_bas_tgl_terima")==null)
			nasabah.setMsch_bas_tgl_terima(FormatDate.toString(elionsManager.selectSysdate()));
		else
			nasabah.setMsch_bas_tgl_terima(request.getParameter("msch_bas_tgl_terima"));
		if(nasabah.getCekUpdate()==null)
			nasabah.setCekUpdate(0);
		if(nasabah.getAlasan()==null)
			nasabah.setAlasan("");
		
	}

	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		DataNasabah nasabah = (DataNasabah) cmd;
		nasabah=this.elionsManager.prosesUpdateNasabah(cmd, currentUser,err);
		nasabah.getDatausulan().setStatus_submit("");
		
		HttpSession session = request.getSession();
		session.setAttribute("jumlah_sisa_polis", nasabah.getJumlah_sisa_polis());
		List a = nasabah.getListPolis2();
		session.setAttribute("polis2", a);
		List b= nasabah.getListPolisLain1();
		session.setAttribute("polis",b);
		if(nasabah.getProses()!=null)	
			return new ModelAndView(new RedirectView("update_nasabah.htm?status_submit=berhasil&spaj="+nasabah.getDatausulan().getReg_spaj()+
					"&msch_bas_tgl_terima="+nasabah.getMsch_bas_tgl_terima()));
		else
			return new ModelAndView(new RedirectView("update_nasabah.htm?status_submit=gagal&spaj="+nasabah.getDatausulan().getReg_spaj()+"&ket="+
					nasabah.getDatausulan().getKeterangan()+"&msch_bas_tgl_terima="+nasabah.getMsch_bas_tgl_terima()));

	}
}