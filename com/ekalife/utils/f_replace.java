package com.ekalife.utils;

import java.io.Serializable;

/*
 * Created on Aug 12, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author HEMILDA
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class f_replace implements Serializable{

	private static final long serialVersionUID = 1L;

	public String f_replace_karakter(String detil)
	{
		String detil_a;
		String detil_b;
		String detil_c;
		String detil_d;
		int a = 0;
		int b = 0;
		a=detil.indexOf("&");
		if (a>0)
		{
			detil_a=detil.substring(0,a);
			detil_b=detil.substring(a+1,detil.length());
			detil=detil_a.concat(" dan ").concat(detil_b);			 
		}
		b=detil.indexOf("'");
		if (b>0)
		{
			detil_c=detil.substring(0,b);
			detil_d=detil.substring(b+1,detil.length());
			detil=detil_c.concat(" ` ").concat(detil_d);			 
		}
		
		f_return_nilai d = new f_return_nilai();
		detil = d.f_return(detil);
		
		return detil;
	}
	
	public String f_replace_persen(String detil)
	{
		String detil_a;
		String detil_b;
		String detil_c;
		String detil_d;
		String detil_e;
		int a = 0;
		int b = 0;
		int c = 0;
		a=detil.indexOf("%");
		if (a>0)
		{
			detil_a=detil.substring(0,a);
			detil_b=detil.substring(a+1,detil.length());
			detil=detil_a.concat("").concat(detil_b);			 
		}
		b=detil.indexOf(" ");
		if (b>0)
		{
			detil_c=detil.substring(0,b);
			detil_d=detil.substring(b+1,detil.length());
			detil=detil_c.concat("").concat(detil_d);			 
		}
		c=detil.indexOf(",");
		if (c>0)
		{
			detil_e=detil.substring(0,c);
			detil_d=detil.substring(c+1,detil.length());
			detil=detil_e.concat("").concat(".");
			detil=detil.concat("").concat(detil_d);			 
		}
		f_return_nilai d = new f_return_nilai();
		detil = d.f_return(detil);
		
		return detil;
	}
	
	public String f_replace_coma(String detil)
	{
		String detil_a;
		String detil_b;
		String detil_c;
		String detil_d;
		int a = 0;
		int b = 0;
		a=detil.indexOf(",");
		if (a>0)
		{
			detil_a=detil.substring(0,a);
			detil_b=detil.substring(a+1,detil.length());
			detil=detil_a.concat(".").concat(detil_b);			 
		}
		f_return_nilai d = new f_return_nilai();
		detil = d.f_return(detil);
		
		return detil;
	}

}
