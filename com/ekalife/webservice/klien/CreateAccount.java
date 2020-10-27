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
 *         &lt;element name="channel" type="{http://agency.banksinarmas.com/customer}ChannelDetail"/>
 *         &lt;element name="customer" type="{http://agency.banksinarmas.com/customer}CustomerDetail"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"channel", "customer"})
@XmlRootElement(name = "CreateAccount")
public class CreateAccount {

	@XmlElement(required = true)
	protected ChannelDetail channel;
	@XmlElement(required = true)
	protected CustomerDetail customer;

	/**
	 * Gets the value of the channel property.
	 * 
	 * @return possible object is {@link ChannelDetail }
	 * 
	 */
	public ChannelDetail getChannel() {
		return channel;
	}

	/**
	 * Sets the value of the channel property.
	 * 
	 * @param value
	 *            allowed object is {@link ChannelDetail }
	 * 
	 */
	public void setChannel(ChannelDetail value) {
		this.channel = value;
	}

	/**
	 * Gets the value of the customer property.
	 * 
	 * @return possible object is {@link CustomerDetail }
	 * 
	 */
	public CustomerDetail getCustomer() {
		return customer;
	}

	/**
	 * Sets the value of the customer property.
	 * 
	 * @param value
	 *            allowed object is {@link CustomerDetail }
	 * 
	 */
	public void setCustomer(CustomerDetail value) {
		this.customer = value;
	}

}
