package com.ekalife.elions.web.refund;

/**********************************************************************
 * Program History
 *
 * Project Name      	: E-Lions
 * Function Id         	: 
 * Program Name   		: RefundSpajController
 * Description         	:
 * Environment      	: Java  1.5.0_06
 * Author               : samuel
 * Version              : 1.0
 * Creation Date    	: Dec 12, 2008 2:21:53 PM
 *
 * Version      Re-fix date                 Person in charge    Description
 *
 *
 * Copyright(C) 2007-Asuransi Jiwa Sinarmas. All Rights Reserved.
 ***********************************************************************/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.ekalife.elions.model.User;
import com.ekalife.utils.parent.ParentController;

/**
 * @author Yusuf
 *
 */
public class RefundSpajController extends ParentController
{

	public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        if(logger.isDebugEnabled())logger.debug( "*-*-*-* RefundSpajController.handleRequest" );

        Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) request.getSession().getAttribute("currentUser");
		Integer lus_id = Integer.parseInt(user.getLus_id());
		map.put("user_id", lus_id);

		String cab_bank="";
		Integer jn_bank = -1;
		Map data_valid = this.elionsManager.select_validbank(lus_id);
		if (data_valid != null)
		{
			cab_bank = (String)data_valid.get("CAB_BANK");
			jn_bank = (Integer) data_valid.get("JN_BANK");
		}

		if (cab_bank == null)
		{
			cab_bank = "";
		}
		map.put("cabang_bank", cab_bank);
		map.put("jn_bank", jn_bank);
		String spaj = ServletRequestUtils.getStringParameter(request,"spaj", "");
		
		if( request.getSession().getAttribute("spaj") != null && !"".equals( request.getSession().getAttribute("spaj") ) )
		{
			if( spaj.equals(request.getSession().getAttribute("spaj") ) )
			{
				request.getSession().setAttribute("spaj", request.getSession().getAttribute("spaj").toString());	
			}
			else
			{
				request.getSession().setAttribute("spaj", spaj);
			}
		}
		else
		{
			request.getSession().setAttribute("spaj", spaj);
		}
		
		if(request.getParameter("search")!=null){

			String tgl_lahir = "";
			int centang = ServletRequestUtils.getIntParameter(request, "centang", 0);
			if(centang == 1) {
                tgl_lahir = request.getParameter("tgl_lahir");
            }
			
			List selectFilterRefundSpaj = elionsManager.selectFilterRefundSpaj(
                    null,
                    request.getParameter("tipe"),
                    request.getParameter("kata"),
                    ServletRequestUtils.getStringParameter(request, "pilter", "="),null,null, tgl_lahir, request.getParameter("isNewSpaj"));

            map.put("listSpaj",
                    elionsManager.selectFilterRefundSpaj(
                            null,
                            request.getParameter("tipe"),
                            request.getParameter("kata"),
                            ServletRequestUtils.getStringParameter(request, "pilter", "="),null,null, tgl_lahir, request.getParameter("isNewSpaj")));
        }
		map.put("flag", ServletRequestUtils.getStringParameter(request, "flag", "0"));

		return new ModelAndView("refund/refund_spaj", "cmd", map); 
	}

}
