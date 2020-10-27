package com.ekalife.elions.web.bac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekalife.elions.model.MedQuest;
import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author HEMILDA
 * Controller untuk frame penginputan bac
 */
public class BacControllerMall extends ParentController {

	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map map = new HashMap();
//		String spaj=request.getParameter("spaj");
//		String spaj;
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		map.put("user_id", lus_id);
		
		String cab_bank="";
		Integer jn_bank = -1;
		Integer flag_approve = -1;
		Map data_valid = (HashMap)this.elionsManager.select_validbank(lus_id);
		if (data_valid != null)
		{
			cab_bank = (String)data_valid.get("CAB_BANK");
			jn_bank = (Integer) data_valid.get("JN_BANK");
			flag_approve = (Integer) data_valid.get("FLAG_APPROVE");
		}
		
		if (cab_bank == null)
		{
			cab_bank = "";
		}
		map.put("cabang_bank", cab_bank);
		map.put("jn_bank", jn_bank);
		List lsDaftarSpaj = null;
		//apabila inputan bank, daftar spaj yang visible hanya yang inputan dia saja
		lsDaftarSpaj=this.uwManager.selectDaftarSPAJMall("1",1,null,null);
		
//		List transferDisabledOrNot = null;
		String spajList = null;
		String spajList4EditButton = null;
		if( jn_bank == 2 )
		{
			if( !"11".equals( user.getLde_id() ) )
			{
				for( int i = 0 ; i < lsDaftarSpaj.size() ; i ++ )
				{
					HashMap temp = (HashMap)lsDaftarSpaj.get(i);
					String spaj = temp.get("REG_SPAJ").toString();	
					String warnaBgSpaj = temp.get("OTORISASI_BG").toString();
					List transferDisabledOrNot = this.uwManager.selectSpajOtorisasiDisabled(spaj);
					if( transferDisabledOrNot != null && transferDisabledOrNot.size() > 0 )
					{
						if( spajList == null )
						{
							spajList = spaj;
						}
						else
						{
							spajList = spajList + "," + spaj;
						}
					}
					if( warnaBgSpaj != null && "rgb(255,255,128)".equals(warnaBgSpaj))
					{
						if( spajList4EditButton == null )
						{
							spajList4EditButton = spaj;
						}
						else
						{
							spajList4EditButton = spajList4EditButton + "," + spaj;
						}
					}
				}									
			}
		}
//			if( transferDisabledOrNot == null )
//			{
//				transferDisabledOrNot = this.uwManager.selectSpajOtorisasiDisabled(spaj);
//			}
//			else 
//			{
//				if( this.uwManager.selectSpajOtorisasiDisabled(spaj) != null && this.uwManager.selectSpajOtorisasiDisabled(spaj).size() > 0 )
//				{
//					transferDisabledOrNot.add( this.uwManager.selectSpajOtorisasiDisabled(spaj) );					
//				}
//			}

//		Map a=(HashMap)lsDaftarSpaj.get(0);
//			spaj=(String)a.get("REG_SPAJ");
//		map.put("transferDisabledOrNot", transferDisabledOrNot);
		map.put("daftarSPAJ", lsDaftarSpaj);
		map.put("spajList4EditButton", spajList4EditButton);
		map.put("spajList", spajList);
		map.put("ldeId", user.getLde_id());
		map.put("jnBank", jn_bank);
		
//		map.put("flagCc",this.elionsManager.selectDataUsulan(spaj));
//		if(spaj!=null){
//			map.put("rekClientCount", this.elionsManager.selectRekClientCount(spaj));
//			map.put("bisnisId",this.elionsManager.selectBisnisId(spaj));
//			logger.info(map);
//			JOptionPane.showMessageDialog(null,"SPAJ TIDAK NULL");
//			
//		}

		map.put("lebar", user.getScreenWidth()*3/4);
		map.put("panjang", user.getScreenHeight()*3/4);
		
		return new ModelAndView("bac/bac_mall", map);
	}
	
}