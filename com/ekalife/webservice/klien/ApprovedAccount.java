package com.ekalife.webservice.klien;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ApprovedAccount complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ApprovedAccount">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="custCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="custName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="atmCardNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApprovedAccount", propOrder = {"custCode", "custName",
		"atmCardNo", "accountNo"})
public class ApprovedAccount {

	@XmlElement(required = true)
	protected String custCode;
	@XmlElement(required = true)
	protected String custName;
	@XmlElement(required = true)
	protected String atmCardNo;
	@XmlElement(required = true)
	protected String accountNo;

	/**
	 * Gets the value of the custCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCustCode() {
		return custCode;
	}

	/**
	 * Sets the value of the custCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCustCode(String value) {
		this.custCode = value;
	}

	/**
	 * Gets the value of the custName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCustName() {
		return custName;
	}

	/**
	 * Sets the value of the custName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCustName(String value) {
		this.custName = value;
	}

	/**
	 * Gets the value of the atmCardNo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAtmCardNo() {
		return atmCardNo;
	}

	/**
	 * Sets the value of the atmCardNo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAtmCardNo(String value) {
		this.atmCardNo = value;
	}

	/**
	 * Gets the value of the accountNo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAccountNo() {
		return accountNo;
	}

	/**
	 * Sets the value of the accountNo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAccountNo(String value) {
		this.accountNo = value;
	}

}
