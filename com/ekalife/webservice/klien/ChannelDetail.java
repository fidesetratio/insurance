package com.ekalife.webservice.klien;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ChannelDetail complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ChannelDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="txReff" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="branchCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="company" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="agentId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChannelDetail", propOrder = {"txReff", "branchCode",
		"company", "agentId"})
public class ChannelDetail {

	@XmlElement(required = true)
	protected String txReff;
	@XmlElement(required = true)
	protected String branchCode;
	@XmlElement(required = true)
	protected String company;
	@XmlElement(required = true)
	protected String agentId;

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
	 * Gets the value of the branchCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBranchCode() {
		return branchCode;
	}

	/**
	 * Sets the value of the branchCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBranchCode(String value) {
		this.branchCode = value;
	}

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
	 * Gets the value of the agentId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAgentId() {
		return agentId;
	}

	/**
	 * Sets the value of the agentId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAgentId(String value) {
		this.agentId = value;
	}

}
