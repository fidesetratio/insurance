/**
 * 
 */
package com.ekalife.elions.web.uw;

import id.co.sinarmaslife.std.model.vo.DropDown;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.DrekDet;
import com.ekalife.elions.model.Linkdetail;
import com.ekalife.utils.MappingUtil;
import com.ekalife.utils.parent.ParentFormController;

/**
 * @author Yusuf
 * 
 */
public class PaymentPremiController extends ParentFormController {

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		return new HashMap();
	}

	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map map = new HashMap();
		BigDecimal jumlah;
		String premiTerpakai = request.getParameter("premiTerpakai");
		String no_trx = request.getParameter("no_trx");
		String noSpaj = request.getParameter("noSpaj");
		String norek_ajs = request.getParameter("norek_ajs");
		String piyu = request.getParameter("piyu");
//		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		
		List<Linkdetail> jenisTransaksiList = new ArrayList<Linkdetail>(); 
		Integer isUlink = this.uwManager.isUlinkBasedSpajNo( noSpaj );
		
		if( isUlink != null && isUlink > 0 ){
			jenisTransaksiList = this.uwManager.uLinkDescrAndDetail( noSpaj );
		}
		else {
			Integer isSlink = this.uwManager.isSlinkBasedSpajNo( noSpaj );
			if( isSlink != null && isSlink > 0 ){
				jenisTransaksiList = this.uwManager.sLinkDescrAndDetail( noSpaj );
			}
			else{
				jenisTransaksiList = null;
			}
		}
		List<DropDown> temp = new ArrayList<DropDown>(); 
		if( jenisTransaksiList != null && jenisTransaksiList.size() > 0 ) // jika spaj adalah slink/ulink
		{
			for( Linkdetail descrValueLink : jenisTransaksiList ){
				temp.add(new DropDown(descrValueLink.getTahunKe() + "@"+descrValueLink.getPremiKe(),descrValueLink.getDescr()));
			}
		}
		else // jika spaj bukan ulink maupun slink
		{
			temp.add(new DropDown("1@1","Premi"));
		}
		
		List<DrekDet> mstDrekDetBasedNoTrx = uwManager.selectMstDrekDet( no_trx, null, null, norek_ajs ); 
		String detailDisplay;
		if( mstDrekDetBasedNoTrx != null && mstDrekDetBasedNoTrx.size() > 0 )
		{
			detailDisplay = "true";
			Double total = 0.0;
			for( int i = 0 ; i < mstDrekDetBasedNoTrx.size() ; i ++ )
			{
				String tahunKeAndPremiKe = mstDrekDetBasedNoTrx.get(i).getTahun_ke() + "@" + mstDrekDetBasedNoTrx.get(i).getPremi_ke();
				String descr = MappingUtil.getLabel(temp, tahunKeAndPremiKe);
//				if("Alokasi Investasi".equals(descr))
//					{ mstDrekDetBasedNoTrx.get(i).setDescr("Premi Pokok"); }
//				else
//					{ mstDrekDetBasedNoTrx.get(i).setDescr(descr); }
				
				mstDrekDetBasedNoTrx.get(i).setDescr("Premi utk SPAJ "+ mstDrekDetBasedNoTrx.get(i).getNo_spaj());
				if( mstDrekDetBasedNoTrx.get(i).getJumlah() == null)
					{ mstDrekDetBasedNoTrx.get(i).setJumlahForDisplay("0"); }
				else
					{ mstDrekDetBasedNoTrx.get(i).setJumlahForDisplay(mstDrekDetBasedNoTrx.get(i).getJumlah().toString()); }
				
				total = total + mstDrekDetBasedNoTrx.get(i).getJumlah();
			}
			String totalDisplay;
			if( total == null )
			{
				totalDisplay = "0";
			}
			else
			{
				totalDisplay = total.toString();
			}
			
			map.put("total", totalDisplay);
		}
		else
		{
			detailDisplay = "none";
		}
		
		jumlah = new BigDecimal( uwManager.selectSumPremiMstDrekAndDet(no_trx, noSpaj) );
//		if( premiTerpakai != null && !"".equals( premiTerpakai ) )
//		{
//			jumlah =  new BigDecimal( request.getParameter("premiTerpakai") );
//		}
//		else
//		{
//			jumlah = new BigDecimal( request.getParameter("jumlah") );
//		}
		String result = jumlah.toString();
		String simbol = request.getParameter("simbol");
		map.put("mstDrekDetBasedNoTrx", mstDrekDetBasedNoTrx);
		map.put("jumlah", result);
		map.put("simbol", simbol);
		map.put("piyu", piyu);
		map.put("detailDisplay", detailDisplay);
		map.put("jumlahReal", new BigDecimal( request.getParameter("jumlah") ) );
		map.put("no_trx", no_trx.replace("\\","\\\\"));
		map.put("norek_ajs", norek_ajs);
		
		return map;
	}
	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		return new ModelAndView("uw/drek", errors.getModel()).addObject("submitSuccess", "true").addAllObjects(this.referenceData(request));

	}

}