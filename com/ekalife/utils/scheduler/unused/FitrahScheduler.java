package com.ekalife.utils.scheduler.unused;

import com.ekalife.utils.parent.ParentScheduler;

/**
 * @spring.bean Class yg digunakan untuk memberitahu admin cabang & bas, fitrah 
 * card yg dipeganag oleh agent harus dikembalikan ke admin jika setelah 21 hari 
 * kerja belum terjual
 * 
 * @author Yusup_A
 * @since Apr 1, 2009 (9:04:41 AM)
 */
public class FitrahScheduler extends ParentScheduler {

	public void main() {
		if(jdbcName.equals("eka8i")) {
			this.uwManager.prosesGenerate21HariFitrah();
		}
	}
}
