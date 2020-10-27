package com.ekalife.elions.web.bac.support;
/*
 * Created on Oct 18, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Hemilda
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class wf_set_biaya_ulink {

	int li_bisnis = 7;
	int li_insper = 18;
	double ldec_pct = 1;
	
	public int set_pmode(int pmode)
	{
		if (pmode == 1 || pmode == 2 || pmode == 6)
		{
			pmode = 3;
		}
		return pmode;
	}
	
	
}
