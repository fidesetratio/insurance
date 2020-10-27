package com.ekalife.webservice.klien;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="company" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="custcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"company", "custcode"})
@XmlRootElement(name = "EnquiryAccountbyCustcode")
public class EnquiryAccountbyCustcode {

	@XmlElement(required = true)
	protected String company;
	@XmlElement(required = true)
	protected String custcode;

	/**
	 * Gets the value of the company property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * Sets the value of the company property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCompany(String value) {
		this.company = value;
	}

	/**
	 * Gets the value of the custcode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCustcode() {
		return custcode;
	}

	/**
	 * Sets the value of the custcode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCustcode(String value) {
		this.custcode = value;
	}

}
