package com.ekalife.elions.service.remoting;

import java.util.List;

import com.ekalife.elions.service.BacManager;
import com.ekalife.elions.service.ElionsManager;
import com.ekalife.elions.service.UwManager;

public class RemoteServiceImpl implements RemoteService {

	private ElionsManager elionsManager;
	private UwManager uwManager;
	private BacManager bacManager;

	public void setBacManager(BacManager bacManager) {
		this.bacManager = bacManager;
	}

	public void setElionsManager(ElionsManager elionsManager) {
		this.elionsManager = elionsManager;
	}
	
	public void setUwManager(UwManager uwManager) {
		this.uwManager = uwManager;
	}

	/* (non-Javadoc)
	 * @see com.ekalife.elions.service.remoting.RemoteService#selectNilaiTunai(java.lang.String, java.lang.String)
	 */
	public List selectNilaiTunai(String spaj, String lus_id) throws Exception{
		//return Collections.synchronizedList(this.elionsManager.viewNilai(spaj, lus_id));
		return this.elionsManager.viewNilai(spaj, lus_id, 0, 0);
	}
	public List selectViewerKontrolTahapan(List spaj)throws Exception{
		return this.uwManager.selectViewerKontrolTahapan(spaj, null);
	}
}
