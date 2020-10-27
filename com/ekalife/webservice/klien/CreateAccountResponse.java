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
 *         &lt;element name="responseCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parameter" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"responseCode", "message", "parameter"})
@XmlRootElement(name = "CreateAccountResponse")
public class CreateAccountResponse {

	@XmlElement(required = true)
	protected String responseCode;
	@XmlElement(required = true)
	protected String message;
	@XmlElement(required = true)
	protected String parameter;

	/**
	 * Gets the value of the responseCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * Sets the value of the responseCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setResponseCode(String value) {
		this.responseCode = value;
	}

	/**
	 * Gets the value of the message property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the value of the message property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMessage(String value) {
		this.message = value;
	}

	/**
	 * Gets the value of the parameter property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * Sets the value of the parameter property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setParameter(String value) {
		this.parameter = value;
	}

}
