package com.ekalife.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Class yang digunakan untuk meng-compare elemen map dalam suatu list, untuk
 * tujuan sorting
 * 
 * @author Yusuf
 * @since 01/11/2005
 */
public class FieldComparator implements Comparator {

	private String field;

	public void setField(String field) {
		this.field = field;
	}

	public FieldComparator(String field) {
		this.field = field;
	}

	/**
	 * Fungsi untuk membandingkan elemen map
	 * 
	 * @param o1
	 *            object yang ingin dibandingkan
	 * @param o2
	 *            object pembandingnya
	 * @return Angka negatif apabila o1 lebih kecil dari o2, 0 apabila o1 sama
	 *         dengan o2, dan positif apabila o1 lebih besar dari o2
	 * @see java.util.String
	 */
	public int compare(Object o1, Object o2) {
		Map m1 = (HashMap) o1;
		Map m2 = (HashMap) o2;
		Object one = m1.get(field);
		Object two = m2.get(field);
		if(one!=null && two!=null) return one.toString().compareTo(two.toString()); 
		else return 0;
	}

}
