package com.ekalife.gwt.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DbServiceAsync {

	public void dummy(String dummy, AsyncCallback callback); 
	public void getDaftarPolis(AsyncCallback callback);
	public void updateDaftarPolis(List daftarPolis, AsyncCallback callback);
	
}
