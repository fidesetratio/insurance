package com.ekalife.utils;

/**
 * Cette classe représente un nombre romain. Elle inclu un <a
 * href="http://www.moxlotus.alternatifs.eu/programmation/index.html">algorithme
 * de conversion de nombre arabe-romain</a>.
 * 
 * @author MoxLotus
 * 
 */
public class RomanNumber {

	/**
	 * Ensemble des <i>nombres romains élémentaires</i>.
	 */
	private final static String[] BASIC_ROMAN_NUMBERS = { "M", "CM", "D", "CD",
			"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };

	/**
	 * Ensemble des correspondants arabes des <i>nombres romains élémentaires</i>.
	 */
	private final static int[] BASIC_VALUES = { 1000, 900, 500, 400, 100, 90,
			50, 40, 10, 9, 5, 4, 1 };

	/**
	 * Valeur arabe du nombre romain.
	 */
	private int value;

	/**
	 * Valeur romaine du nombre romain.
	 */
	private String romanString;

	/**
	 * Construit un nombre romain à partir d'un nombre arabe.
	 * 
	 * @param value
	 *           Valeur arabe du nombre romain à construire.
	 * @throws IllegalArgumentException
	 *            La valeur doit être entre 1 et 3999.
	 */
	public RomanNumber(int value) throws IllegalArgumentException {
		if (1 <= value && value <= 3999) {
			this.value = value;
		} else {
			throw new IllegalArgumentException("" + value);
		}
	}

	/**
	 * Construire un nombre romain selon un nombre romain.
	 * 
	 * @param s
	 *           Valeur romaine du nombre romain à construire.
	 * @throws IllegalArgumentException
	 *            Le valeur doit un nombre romain valide.
	 */
	public RomanNumber(String s) throws IllegalArgumentException {
		String r = s.toUpperCase();
		int index = 0;
		for (int i = 0; i < BASIC_ROMAN_NUMBERS.length; i++) {
			while (r.startsWith(BASIC_ROMAN_NUMBERS[i], index)) {
				this.value += BASIC_VALUES[i];
				index += BASIC_ROMAN_NUMBERS[i].length();
			}
		}
		// Verify the input string is a valid roman number.
		RomanNumber tempVerify;
		String verifyString;
		try {
			tempVerify = new RomanNumber(this.value);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(s);
		}
		if ((verifyString = tempVerify.toRomanValue()).equals(r)) {
			this.romanString = r;
		} else {
			throw new RuntimeException(s);
		}

	}

	/**
	 * Retourne la valeur romaine du nombre romain.
	 * 
	 * @return La valeur romaine du nombre romain.
	 */
	public String toRomanValue() {
		if (this.romanString == null) {
			this.romanString = "";
			int remainder = this.value;
			for (int i = 0; i < BASIC_VALUES.length; i++) {
				while (remainder >= BASIC_VALUES[i]) {
					this.romanString += BASIC_ROMAN_NUMBERS[i];
					remainder -= BASIC_VALUES[i];
				}
			}
		}
		return this.romanString;
	}

	/**
	 * Retourne la valeur arabe du nombre romain.
	 * 
	 * @return La valeur arabe du nombre romain.
	 */
	public int getValue() {
		return this.value;
	}
}