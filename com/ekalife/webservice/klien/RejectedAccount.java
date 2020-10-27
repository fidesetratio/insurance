package com.ekalife.webservice.klien;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for RejectedAccount complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="RejectedAccount">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="custCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="custName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="txReff" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="errorMessage" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RejectedAccount", propOrder = {"custCode", "custName",
		"txReff", "errorMessage"})
public class RejectedAccount {

	@XmlElement(required = true)
	protected String custCode;
	@XmlElement(required = true)
	protected String custName;
	@XmlElement(required = true)
	protected String txReff;
	@XmlElement(required = true)
	protected List<String> errorMessage;

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
	 * Gets the value of the txReff property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTxReff() {
		return txReff;
	}

	/**
	 * Sets the value of the txReff property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTxReff(String value) {
		this.txReff = value;
	}

	/**
	 * Gets the value of the errorMessage property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the errorMessage property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getErrorMessage().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getErrorMessage() {
		if (errorMessage == null) {
			errorMessage = new ArrayList<String>();
		}
		return this.errorMessage;
	}

}
