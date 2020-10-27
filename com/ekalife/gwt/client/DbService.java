package com.ekalife.gwt.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface DbService extends RemoteService {

	public String dummy(String dummy); 
	/**
	 * @gwt.typeArgs <com.ekalife.gwt.client.model.Polis>
	 */
	public List getDaftarPolis();
	public int updateDaftarPolis(List daftarPolis);
	
}