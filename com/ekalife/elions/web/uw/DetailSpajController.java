package com.ekalife.elions.web.uw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.AddressNew;
import com.ekalife.elions.model.Client;
import com.ekalife.utils.parent.ParentController;

public class DetailSpajController extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String spaj=request.getParameter("spaj");
		String flag=request.getParameter("flag");//1=tertanggung 2=pemegang 3=data usulan
		String data = null;
		Client client=new Client();
		AddressNew addressNew=new AddressNew();
		//
		String relasi=null;
		Integer umurBeasiswa=null;
		
		if(flag.equals("1")){//tertanggung
			data="Tertanggung";
			client=elionsManager.selectAllClientNew(spaj,1);
			addressNew=elionsManager.selectAllAddressNew(spaj,1);
		}else if(flag.equals("2")){//pemegang
			data="Pemegang";
			Map mPemegang=elionsManager.selectPemegang(spaj);
			relasi=(String)mPemegang.get("LSRE_RELATION");
			umurBeasiswa=(Integer)mPemegang.get("MSPO_UMUR_BEASISWA");
			client=elionsManager.selectAllClientNew(spaj,2);
			addressNew=elionsManager.selectAllAddressNew(spaj,2);
		}
		//reference
		List lsWargaNegara,lsJnsId,lsSex,lsStsMrt,lsAgama,lsPendidikan;
		lsWargaNegara=elionsManager.selectAllLstNegara();
		lsJnsId=elionsManager.selectAllLstIdentity();
		lsAgama=elionsManager.selectAllLstAgama();
		lsPendidikan=elionsManager.selectAllLstEducation();
		Map map=new HashMap();
		map.put("data",data);
		map.put("clientNew",client);
		map.put("addressNew",addressNew);
		map.put("lsWargaNegara",lsWargaNegara);
		map.put("lsJnsId",lsJnsId);
		map.put("lsSex",sex());
		map.put("lsStsMrt",statusMrt());
		map.put("lsAgama",lsAgama);
		map.put("lsPendidikan",lsPendidikan);
		map.put("relasi",relasi);
		map.put("umurBeasiswa",umurBeasiswa);
		return new ModelAndView("uw/detailspaj",map);
	}
	
	private List sex(){
		List list=new ArrayList();
		Map a=new HashMap();
		a.put("ID","0");
		a.put("VALUE","Wanita");
		list.add(a);
		a=new HashMap();
		a.put("ID","1");
		a.put("VALUE","Pria");
		list.add(a);
		return list;
	}
	
	private List statusMrt(){
		List list=new ArrayList();
		Map a=new HashMap();
		a.put("ID","1");
		a.put("VALUE","Belum Menikah");
		list.add(a);
		a=new HashMap();
		a.put("ID","2");
		a.put("VALUE","Menikah");
		list.add(a);
		a=new HashMap();
		a.put("ID","3");
		a.put("VALUE","Janda");
		list.add(a);
		a=new HashMap();
		a.put("ID","4");
		a.put("VALUE","Duda");
		list.add(a);
		
		return list;
	}
	
}