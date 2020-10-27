package com.ekalife.utils.scheduler.unused;

import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class ini digunakan dengan cara mengaktifkan sebagai spring bean, 
 * 1. Melakukan query data ke database Jatis (Prata)
 * 2. Membuat hasil query ke dalam comma delimited text file
 * 3. men-zip semua file menjadi satu, kemudian di password dgn 256b encryption
 * 4. email
 * 
 * @author yusuf
 * @since Sep 28, 2009 (9:03:20 AM)
 */
public class PrataScheduler extends ParentScheduler{

	//main method
	public void main() throws Exception{
		if(jdbcName.equals("eka8i")) {
			this.uwManager.schedulerPrata();
		}
	}

}