package com.ekalife.elions.web.akseptasi_ssh;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.ekalife.elions.model.Product;
import com.ekalife.elions.model.User;
import com.ekalife.elions.service.ElionsManager;

public class TransferSshController implements Controller {
	private ElionsManager elionsManager;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
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

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}

}
