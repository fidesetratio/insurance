package com.ekalife.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.ServletRequestUtils;

import com.ekalife.elions.model.User;
import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;

public class Otorisasi {
	
	private ElionsManager elionsManager;
	private String jdbcName;
	private Products products;
	private Properties props;
	private UwManager uwManager;
	private BacManager bacManager;

	//Getter Setter
	public void setUwManager(UwManager uwManager) {this.uwManager = uwManager;}
	public void setBacManager(BacManager bacManager) {this.bacManager = bacManager;}
	public void setElionsManager(ElionsManager elionsManager) {this.elionsManager = elionsManager;}
	public void setJdbcName(String jdbcName) {this.jdbcName = jdbcName;}
	public void setProducts(Products products) {this.products = products;}
	public void setProps(Properties props) {this.props = props;}

	/** FITUR BLOCK ALL ACCESS TO APPLICATION **/
	public boolean validasiApplicationBlocked() {
		return props.getProperty("app.status").equals("1");
	}

	/** VALIDASI SESSION USER **/
	public boolean validasiSession(HttpSession session, User currentUser) {
		return !(session.isNew() || currentUser == null);
	}
/*hanya untuk percobaan aja...
	dian*/
	/** AKSES YANG HANYA GUA DAN UNDERWRITING YANG BOLEH **/
	
public boolean validasiUnderwriting(HttpServletRequest request, User currentUser) {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		boolean retVal = true;
		
		Map<String, String[]> validasiUw = new HashMap<String, String[]>();
		//validasiUw.put(contextPath + "/uw/viewer.htm", new String[] {"editTglTrmKrmSpaj"});
		validasiUw.put(contextPath + "/report/uw.htm", new String[] {"cover_letter"});
		validasiUw.put(contextPath + "/uw/printpolis.htm", new String[] {"validforprint", "wording", "spaj"});
		
		if(validasiUw.containsKey(requestUri)) {
			for(String tes : validasiUw.get(requestUri)) {
				if(tes.equals(request.getParameter("window"))) {
					//if("11".indexOf(currentUser.getLde_id())<0 || currentUser.getLus_id().equals("574")) {) {
					if("11".indexOf(currentUser.getLde_id())<0 && !currentUser.getLus_id().equals("74") && !currentUser.getLus_id().equals("574") && !currentUser.getLus_id().equals("534") && !currentUser.getLus_id().equals("113")) {
						retVal = false;
					}
					
					//ACCESS DISETUJUI OLEH DR.INGRID, UTK MALL BOLEH PRINT ULANG LAGI.NAMUN HARUS DI HARDCODE KIRIM EMAIL CC KE UnderwritingDMTM@sinarmasmsiglife.co.id & TO Lylianty@sinarmasmsiglife.co.id
					if(currentUser.getLca_id().equals("58")){
						retVal = true;
					}
				}
			}
		}
		
		return retVal;
	}

public boolean validasiOtorisasiInputSpaj(HttpServletRequest request, User currentUser) {
	String requestUri = request.getRequestURI();
	String contextPath = request.getContextPath();
	boolean retVal = true;
	
	
	Map<String, String> validasiOtorisasiInputSpaj = new HashMap<String, String>();
	//validasiUw.put(contextPath + "/uw/viewer.htm", new String[] {"editTglTrmKrmSpaj"});
	validasiOtorisasiInputSpaj.put(contextPath + "/bac/otorisasiInputSpaj.htm","");
	
	if(validasiOtorisasiInputSpaj.containsKey(requestUri)) {
		String cabBank = currentUser.getCab_bank();
		String jn_bank = currentUser.getJn_bank().toString();
	
		String validBank1All = null;
		String validBank2All = null;
		int flag = 0;
		if( cabBank != null && !"".equals( cabBank ) )
		{
			List accessOtorisasiInputSpaj = uwManager.selectAccessMenuOtorisasiSpaj( cabBank,jn_bank );
			for( int i = 0 ; i < accessOtorisasiInputSpaj.size() ; i ++ )
			{
				Map temp = (Map) accessOtorisasiInputSpaj.get(i);
				Object validBank1 = temp.get("VALID_BANK_1");
				Object validBank2 = temp.get("VALID_BANK_2");
				if( validBank1 != null && !"".equals( validBank1 ) )
				{
					if( validBank1All == null )
					{
						validBank1All = validBank1.toString();
					}
					else
					{
						validBank1All = validBank1All + "," + validBank1.toString();
					}
				}
				if( validBank2 != null && !"".equals( validBank2 ) )
				{
					if( validBank2All == null )
					{
						validBank2All = validBank2.toString();
					}
					else
					{
						validBank2All = validBank2All + "," + validBank2.toString();
					}
				}
			}
			
			if( flag == 0 && validBank1All != null )
			{
				if( validBank1All.contains( currentUser.getLus_id()))
				{
					retVal = true;
					flag = 1;
				}
				else
				{
					retVal = false;
				}
			}
			
			if( flag == 0 && validBank2All != null )
			{
				if( validBank2All.contains( currentUser.getLus_id()))
				{
					retVal = true;
					flag = 1;
				}
				else
				{
					retVal = false;
				}
			}
		}
		else
		{
			retVal = false;
		}
	}
	
	return retVal;
}

public boolean validasiOtorisasiInputTopUp(HttpServletRequest request, User currentUser) {
	String requestUri = request.getRequestURI();
	String contextPath = request.getContextPath();
	boolean retVal = true;
	
	
	Map<String, String> validasiOtorisasiInputSpaj = new HashMap<String, String>();
	//validasiUw.put(contextPath + "/uw/viewer.htm", new String[] {"editTglTrmKrmSpaj"});
	validasiOtorisasiInputSpaj.put(contextPath + "/bac/otorisasi_topup.htm","");
	
	if(validasiOtorisasiInputSpaj.containsKey(requestUri)) {
		String cabBank = currentUser.getCab_bank();
		String jn_bank =Integer.toString(currentUser.getJn_bank());
		String validBank1All = null;
		String validBank2All = null;
		int flag = 0;
		if( cabBank != null && !"".equals( cabBank ) )
		{
			List accessOtorisasiInputSpaj = uwManager.selectAccessMenuOtorisasiSpaj( cabBank,jn_bank );
			for( int i = 0 ; i < accessOtorisasiInputSpaj.size() ; i ++ )
			{
				Map temp = (Map) accessOtorisasiInputSpaj.get(i);
				Object validBank1 = temp.get("VALID_BANK_1");
				Object validBank2 = temp.get("VALID_BANK_2");
				if( validBank1 != null && !"".equals( validBank1 ) )
				{
					if( validBank1All == null )
					{
						validBank1All = validBank1.toString();
					}
					else
					{
						validBank1All = validBank1All + "," + validBank1.toString();
					}
				}
				if( validBank2 != null && !"".equals( validBank2 ) )
				{
					if( validBank2All == null )
					{
						validBank2All = validBank2.toString();
					}
					else
					{
						validBank2All = validBank2All + "," + validBank2.toString();
					}
				}
			}
			
			if( flag == 0 && validBank1All != null )
			{
				if( validBank1All.contains( currentUser.getLus_id()))
				{
					retVal = true;
					flag = 1;
				}
				else
				{
					retVal = false;
				}
			}
			
			if( flag == 0 && validBank2All != null )
			{
				if( validBank2All.contains( currentUser.getLus_id()))
				{
					retVal = true;
					flag = 1;
				}
				else
				{
					retVal = false;
				}
			}
		}
		else
		{
			retVal = false;
		}
	}
	
	return retVal;
}

	/** AKSES TERHADAP SPAJ **/
	public boolean validasiAkses(HttpServletRequest request, User currentUser) {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();

		//Daftar Menu yang dibatesin, plus nama parameter yang berisi nilai SPAJ nya
		Map<String, String> validasiCabang = new HashMap<String, String>();

		validasiCabang.put(contextPath + "/bac/edit.htm", "showSPAJ"); //edit BAC
		validasiCabang.put(contextPath + "/bac/cicreff_bii.htm", "editSPAJ");
		validasiCabang.put(contextPath + "/bac/shintareff_bii.htm", "editSPAJ");
		validasiCabang.put(contextPath + "/bac/reff_bii_new.htm", "editSPAJ");
		validasiCabang.put(contextPath + "/bac/pending.htm", "spaj");//aksep manual spaj
		validasiCabang.put(contextPath + "/bac/transferbactouw.htm", "spaj");
		validasiCabang.put(contextPath + "/ttppremi/titipan_premi.htm", "editSPAJ"); // titipan premi
		validasiCabang.put(contextPath + "/uw/viewer.htm", "spaj"); // uwinfo
		validasiCabang.put(contextPath + "/uw/view.htm", "showSPAJ"); //info nya spaj
		validasiCabang.put(contextPath + "/uw/printpolis.htm", "spaj"); //print polis
		validasiCabang.put(contextPath + "/bac/summary.htm", "spaj"); //summary spaj
		validasiCabang.put(contextPath + "/bac/cancelBac.htm", "spaj"); //batalin spaj
		validasiCabang.put(contextPath + "/bac/keterangan_kesehatan", "spaj"); //kuesioner kesehatan platinum save
		validasiCabang.put(contextPath + "/bac/inputpaymentsucc", "reg_spaj"); //input payment successive
		validasiCabang.put(contextPath + "/uw/uw.htm", "regspajClaimDetail"); 
		
		Map<String, String> validasimsupport = new HashMap<String, String>();
		validasimsupport.put(contextPath + "/bac/editagenpenutup.htm", "spaj"); //edit kode agen
		
		boolean retVal = true;

		String spaj = null, cabang = null;
		
		if(validasiCabang.containsKey(requestUri)){
			spaj = ServletRequestUtils.getStringParameter(request, (String) validasiCabang.get(requestUri), "");
			cabang = elionsManager.selectCabangFromSpaj(spaj);
		}
		
		if(currentUser != null) {
			if(Integer.valueOf(currentUser.getLus_id()).intValue() == 39  || Integer.valueOf(currentUser.getLus_id()).intValue() == 672) {
				//kalau jelita dan grisye bancass, boleh liat viewer, khusus bancass
				if(cabang != null) {
					if(cabang.equals("09")) {
						return true;
					}
				}
			}
		}
		
		if(currentUser != null) {
			if ((!currentUser.getLde_id().equalsIgnoreCase("01")) 
					&& (!currentUser.getLde_id().equalsIgnoreCase("11")) && (!currentUser.getLde_id().equalsIgnoreCase("05"))
					&& (!currentUser.getLde_id().equalsIgnoreCase("29"))
					&& (!currentUser.getLus_id().equalsIgnoreCase("144")) && (!currentUser.getLus_id().equalsIgnoreCase("449"))// case untuk 2 user corporate : Ade W, Layla (sementara)
					&& (!currentUser.getLde_id().equalsIgnoreCase("12")) && (!currentUser.getLde_id().equalsIgnoreCase("21"))
					&& (!currentUser.getLus_id().equalsIgnoreCase("5757")) && (!currentUser.getLus_id().equalsIgnoreCase("5760"))
					&& (!currentUser.getLus_id().equalsIgnoreCase("5759")) && (!currentUser.getLus_id().equalsIgnoreCase("1925")) // 4 orang provider (97268)
					&& (!currentUser.getLde_id().equalsIgnoreCase("39")) && (!currentUser.getLus_id().equalsIgnoreCase("430")))
			{
				if  (currentUser.getLus_admin().intValue() !=1)
				{
					if(validasiCabang.containsKey(requestUri)){
						// validasi departemen dulu
						if (props.getProperty("access.viewer.region").indexOf(currentUser.getLde_id()) > -1) {
							List akses = currentUser.getAksesCabang();
							if(!spaj.equals("")) {
								String region = elionsManager.selectCabangFromSpaj_lar(spaj);
								if (!akses.contains(region)) {
									retVal = false;
								}
							}
						}else{
							List akses = currentUser.getAksesCabang();
							if(!spaj.equals("")) {
								if (!akses.contains(cabang)) {
									retVal = false;
								}
							}				
						}
					}
					if(validasimsupport.containsKey(requestUri)){
						retVal = false;
					}
				}else{
					if(validasimsupport.containsKey(requestUri)){
						// validasi departemen dulu
						if (props.getProperty("access.viewer.msupport").indexOf(currentUser.getLde_id()) <= -1) {
							retVal = false;
						}	
					}
				}
			}
		}	
		return retVal;
	}
	
	/** VALIDASI PRODUK YANG BELOM MELEWATI UAT **/
	public boolean validasiUAT(HttpServletRequest request, User currentUser) {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();

		String window = request.getParameter("window");
		
		//Daftar Menu yang dibatesin, plus nama parameter yang berisi nilai SPAJ nya
		Map<String, String> validasi = new HashMap<String, String>();
		validasi.put(contextPath + "/uw/transfer_to_print.htm", "spaj"); //transfer ke print polis (ada komisi/jurnal/produksi/nilaitunai/jurnalmemorial/simcard)
		validasi.put(contextPath + "/uw/printpolis.htm", "spaj"); //print polis
		if(!("worksheet".equals(window) && "11".equals(currentUser.getLde_id()))){ // pengecualian di worksheet & untuk uw (asriwulan,andy)
			validasi.put(contextPath + "/uw/viewer.htm", "spaj"); //menu2 di viewer
		}
		
		if(jdbcName.equals("eka8i") && validasi.containsKey(requestUri)) {
			String spaj = ServletRequestUtils.getStringParameter(request, (String) validasi.get(requestUri), "");
			if(!spaj.equals("")) {
				Map map = (Map) elionsManager.selectDetailBisnis(spaj).get(0);
				if(!products.isUatPassed((String) map.get("BISNIS"))) {
					return false;
				}
			}
		}
		
		return true;
	}	

}