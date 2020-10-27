package com.ekalife.utils.scheduler.unused;

import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini digunakan dengan cara mengaktifkan sebagai spring bean, 
 * memproses file text-delimited untuk di-upload ke sistem, kemudian nantinya diemailkan ke bank muamalat
 * untuk report produk2 new business bank muamalat, seperti mabrur, ikhlas, saqinah 
 * 
 * @author Yusuf
 * @since Nov 21, 2008 (1:24:15 PM)
 */
public class MuamalatScheduler extends ParentScheduler{
	
	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i")) {
			this.uwManager.schedulerBmi();
		}
	}

}