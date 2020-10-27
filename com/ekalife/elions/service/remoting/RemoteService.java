package com.ekalife.elions.service.remoting;

import java.util.List;

public interface RemoteService {

	public abstract List selectNilaiTunai(String spaj, String lus_id) throws Exception;
	public abstract List selectViewerKontrolTahapan(List spaj)throws Exception;
}