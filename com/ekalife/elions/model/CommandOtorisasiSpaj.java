package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;

public class CommandOtorisasiSpaj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8400182070079647220L;
	public List< DaftarSpajOtorisasi> daftarSpaj;
	
	public List<DaftarSpajOtorisasi> getDaftarSpaj() {
		return daftarSpaj;
	}
	public void setDaftarSpaj(List<DaftarSpajOtorisasi> daftarSpaj) {
		this.daftarSpaj = daftarSpaj;
	}                       	
	
}