/**
 * @author  : Ferry Harlim
 * @created : Mar 14, 2007 
 */
package com.ekalife.elions.web.uw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Commission;
import com.ekalife.elions.model.Deduct;
import com.ekalife.elions.model.User;
import com.ekalife.utils.FormatString;
import com.ekalife.utils.parent.ParentFormController;

public class SlipPotonganKomisiFormController extends ParentFormController {
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		String spaj=ServletRequestUtils.getStringParameter(request, "spaj");
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Double total2= ServletRequestUtils.getDoubleParameter(request, "total2");
		String jns_lisence=ServletRequestUtils.getStringParameter(request, "jns_lisence");
		Integer  jns = ServletRequestUtils.getIntParameter(request, "lsjd_id");
		Map infoAgen=uwManager.selectInfoAgen2(spaj);
		String agen=(String)infoAgen.get("MCL_FIRST");
		String kode=(String)infoAgen.get("MSAG_ID");
		String cabang=(String)infoAgen.get("REGION_NAME");
		
		//
		List listKomisi;
		Commission komisi=null;;
		Deduct deduct=null;
		//
		listKomisi=elionsManager.selectKomisiAgen(spaj, 1, 1);
		if(listKomisi.isEmpty()==false){
			komisi=(Commission) listKomisi.get(0);
			deduct=elionsManager.selectMstDeduct(komisi.getCo_id(), 1);//1=karena awal di mst deduct
		}	
		//cheking posisi polis
		Map mPosisi=elionsManager.selectF_check_posisi(spaj);
		Integer lspdId=(Integer)mPosisi.get("LSPD_ID");
		String lspdPos=(String)mPosisi.get("LSPD_POSITION");
//		deduct.setTotal2(new Double(0));
		if(lspdId==9){
			if(komisi==null){
				deduct= new Deduct();
				deduct.setTotal2(new Double(0));
				deduct.setSpaj(spaj);
				deduct.setPosisi("Komisi Agen Tidak ada!");
				return deduct;
			}	
			
			if(deduct==null){
				deduct=new Deduct();
				deduct.setSpaj(spaj);
				deduct.setMsco_id(komisi.getCo_id());
				deduct.setNama_agen(agen);
				deduct.setKode_agen(kode);
				deduct.setCabang(cabang);
				deduct.setMsdd_date(elionsManager.selectSysdate());
				deduct.setTotal(new Double(0));
				deduct.setTotal2(new Double(0));
				deduct.setPot_biaya(new Double(0));
				deduct.setPot_extra(new Double(0));
				deduct.setPot_upp(new Double(0));
				deduct.setMsdd_number(1);
				deduct.setNopolis(FormatString.nomorPolis(this.elionsManager.selectPolicyNumberFromSpaj(spaj)));
				deduct.setLus_id(Integer.valueOf(currentUser.getLus_id()));
				deduct.setMsdd_deduct((deduct.getPot_upp()+deduct.getPot_biaya()+deduct.getPot_extra()));
			}else{
				deduct.setSpaj(spaj);
				deduct.setFlag(5);
				deduct.setNama_agen(agen);
				deduct.setKode_agen(kode);
				deduct.setCabang(cabang);
				deduct.setJns_lisensi(deduct.getMsdd_desc());
				if (deduct.getLsjd_id().intValue() == 9){
					deduct.setTotal2(deduct.getMsdd_deduct());
					deduct.setMsco_id(komisi.getCo_id());
					deduct.setTotal(deduct.getTotal2());
					deduct.setPot_upp(deduct.getTotal2());
				}else{
					deduct.setTotal2(0.0);
					deduct.setTotal(deduct.getPot_upp()+deduct.getPot_biaya()+deduct.getPot_extra());
					deduct.setMsco_id(komisi.getCo_id());
					deduct.setMsdd_deduct(deduct.getTotal());
				}
			}
			deduct.setLsDetProduk(this.elionsManager.selectLstDetBisnis(deduct.getLsbs_id()));
		}else{
			deduct.setSpaj(spaj);
			deduct.setPosisi("Posisi Polis ini ada di "+lspdPos);
		}
		
		return deduct;
	}
	@Override
	protected Map referenceData(HttpServletRequest arg0) throws Exception {
		Map map=new HashMap();
		map.put("lsDeduct", this.elionsManager.selectLstJnDeduct());
		map.put("selectLstBisnis",this.elionsManager.selectLstBisnis());

		return map;
	}
	
	@Override
	protected void onBindAndValidate(HttpServletRequest request, Object cmd, BindException err) throws Exception {
		Deduct deduct=(Deduct)cmd;
		
		Double totTemp=0.0;
		Integer  jns = ServletRequestUtils.getIntParameter(request, "lsjd_id");
		
		String ubah=ServletRequestUtils.getStringParameter(request, "ubah","0");
		if(ubah.equals("0")){
			if (jns== 9||jns.equals("9")){
				totTemp= deduct.getTotal2();//RequestUtils.getDoubleParameter(request, "total");
				String jns_lisence=ServletRequestUtils.getStringParameter(request, "jns_lisence");
				deduct.setMsdd_deduct(totTemp);
				deduct.setMsdd_desc(jns_lisence);
				deduct.setTotal(totTemp);
			}else{
				deduct.setTotal2(new Double(0));
				deduct.setTotal(new Double(deduct.getPot_upp()+deduct.getPot_extra()+deduct.getPot_biaya()));
				deduct.setMsdd_deduct(deduct.getTotal());
			}}
		deduct.setTotal(new Double(deduct.getPot_upp()+deduct.getPot_extra()+deduct.getPot_biaya()));
			if (deduct.getMsdd_deduct()<=0&&jns== 9||jns.equals("9") ){
					err.reject("","Harap Isi potongan UPP");
			}
			if(deduct.getMsdd_desc()==null&&deduct.getLsjd_id().intValue() == 9){
				err.reject("","Harap Isi jenis licensi");}
			if(deduct.getLsjd_id()==0){
				err.reject("","Silahkan pilih jenis yang akan di potong komisi!.");}
			if(deduct.getLsbs_id()==0 && deduct.getLsjd_id().intValue() != 9){
				err.reject("","Silahkan Pilih Plan yang akan di ambil");
	}else if(ubah.equals("1")){
			deduct.setLsDetProduk(this.elionsManager.selectLstDetBisnis(deduct.getLsbs_id()));
			err.reject("","Telah Dilakukan Perubahan");
		}else if(ubah.equals("2")){//transfer ke TTP
			if(elionsManager.selectMstDeduct(deduct.getMsco_id(), 1)==null)
				err.reject("","Tidak Bisa Transfer, Slip Pemotongan Komisi Blom diisi");
			else
				deduct.setFlag(2);
		}
		
		//ceking nilai deduct tidak boleh lebih besar dari komisi agen (komisi-tax) 
		Double jumKomisi=elionsManager.selectMstCommissionKomisiAgen(deduct.getSpaj(), 4, 1, 1);
		
		if(deduct.getTotal()>jumKomisi)
			err.reject("","Komisi Agen tidak Mencukupi untuk Dilakukan Deduct");
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object cmd, BindException err) throws Exception {
		Deduct deduct=(Deduct)cmd;
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		String ubah=ServletRequestUtils.getStringParameter(request, "ubah","0");
		deduct.setFlag(elionsManager.prosesSimpanSlipPemotonganPolis(deduct,ubah,currentUser));
		
		return new ModelAndView("uw/slip_potongan_komisi", err.getModel()).addAllObjects(this.referenceData(request,cmd,err));
	}
}
