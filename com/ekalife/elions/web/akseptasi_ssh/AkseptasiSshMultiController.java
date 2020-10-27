package com.ekalife.elions.web.akseptasi_ssh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentMultiController;

public class AkseptasiSshMultiController extends ParentMultiController {
	
	public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map=new HashMap();
//		List list=elionsManager.selectMastPolicy(new Integer(31),new Integer(1),new Integer(10),new Integer(1),null,null);
//		map.put("lsDaftarSpaj",list);
		
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		String snow_spaj = ServletRequestUtils.getStringParameter(request, "snow_spaj", "");
//		Integer i_posisi = ServletRequestUtils.getIntParameter(request, "posisi_uw", 0);
		map.put("snow_spaj", snow_spaj);
		
		map.put("daftarSPAJ", this.uwManager.selectDaftarSPAJ("30", 1,null,null));
		
//		map.put("posisi_uw", i_posisi);
//		return new ModelAndView("akseptasi_ssh/akseptasi", "cmd", map);
		return new ModelAndView("akseptasi_ssh/akseptasi", map);
	}	
	
	public ModelAndView cari(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
		
		//	map.put("listPosition", this.reinstateManager.selectDaftarSPAJ(18,106,null,""));
			
			if(request.getParameter("search")!=null){
				List list = this.elionsManager.selectMstPolicy(new Integer(31),new Integer(1),new Integer(1),new Integer(10),
						ServletRequestUtils.getStringParameter(request, "kata", ""), 
						ServletRequestUtils.getStringParameter(request, "kategori", ""));
				map.put("listSpaj", list);
			}
		return new ModelAndView("akseptasi_ssh/cari_akseptasi", "cmd", map);
		
	}	
	
	public ModelAndView transfer(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		Integer lspdId=new Integer(31),lstbId=new Integer(1);
		List list;
		
		Integer premiKe,tahunKe;
		Integer lsspId;
		String spaj;
		String lwkId,lsrgId,lcaId,lkuId,mspoPolicyNo,noBill,spajLama;

		spaj=request.getParameter("spaj");
		spajLama = null;
		noBill=null;
		Map map=new HashMap();
		list=elionsManager.selectMstPolicy(lspdId,lstbId,new Integer(10),new Integer(1),spaj,"4");
		Map a=(HashMap)list.get(0);
		lsspId=(Integer)a.get("LSSP_ID");
		lwkId=(String)a.get("LWK_ID");
		lsrgId=(String)a.get("LSRG_ID");
		lcaId=(String)a.get("LCA_ID");
		lkuId=(String)a.get("LKU_ID");
		mspoPolicyNo=(String)a.get("MSPO_POLICY_NO");
		
		
		if(lsspId.intValue()==10){
			spajLama=(String)elionsManager.selectMstCancelRegSpaj(spaj);
			if(spajLama==null)
				spajLama=spaj;
			//
			Map b=elionsManager.selectMstBillingMax(spajLama);
			tahunKe=(Integer)b.get("TAHUN_KE");
			premiKe=(Integer)b.get("PREMI_KE");
		}else{
			Map b=elionsManager.selectMstBillingMax(spaj);
			tahunKe=(Integer)b.get("TAHUN_KE");
			premiKe=(Integer)b.get("PREMI_KE");

		}
		//
		/*
		 * lds=billing
		 * lds2=detBilling
		 * lds3=produkInsured
		 * */
		User currentUser=(User)request.getSession().getAttribute("currentUser");
		Integer lusId=Integer.valueOf(currentUser.getLus_id());
		
		List lsProdukInsured=elionsManager.selectMstProductInsured(spaj);
		Product produkInsured=(Product)lsProdukInsured.get(0);
		
		//  lds3.SetItem(1,"mspr_premium",tab_1.tabpage_3.dw_3.GetItemDecimal(1,"premi"))
		//	0=ok 1=no polis error   2=no endor error
		int proses=elionsManager.prosesTransfer(spaj,lsProdukInsured,produkInsured,tahunKe,premiKe,lcaId,lsspId,
											 lwkId,lsrgId,lkuId,lusId,mspoPolicyNo,lspdId,lstbId);
		
		map.put("spaj",spaj);
		map.put("info",new Integer(proses));
		return new ModelAndView("akseptasi_ssh/transferssh", "cmd", map);		
	}	
	
}