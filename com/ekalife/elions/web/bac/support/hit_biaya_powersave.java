package com.ekalife.elions.web.bac.support;

/*
 * Created on Sep 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author hemilda
 * 
 * konstanta untuk produk powersave sesuai dengan jangka waktu mgi yang dipilih
 */
public class hit_biaya_powersave {

	public int hit_jangka_invest(String pil_jangka) {
		double ldo_koef = 1000 ^ 3;
		int b = 0;

		int pil_koplo = Integer.parseInt(pil_jangka);
		
		if(pil_koplo == 1) 			b = 60;
		else if (pil_koplo == 3) 	b = 10;
		else if (pil_koplo == 6) 	b = 20;
		else if (pil_koplo == 12) 	b = 30;
		else if (pil_koplo == 24) 	b = 40;
		else if (pil_koplo == 36) 	b = 50;

		return b;
	}

}
