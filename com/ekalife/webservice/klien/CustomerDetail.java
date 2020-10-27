package com.ekalife.webservice.klien;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for CustomerDetail complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerDetail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="custcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="atmCardNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="birthPlace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mothersMaiden" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idExpiryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="homePhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="workPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mobilePhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerDetail", propOrder = {"custcode", "atmCardNo", "name",
		"birthPlace", "birthDate", "gender", "mothersMaiden", "address",
		"city", "postalCode", "idNumber", "idExpiryDate", "homePhone",
		"workPhone", "mobilePhone"})
public class CustomerDetail {

	@XmlElement(required = true)
	protected String custcode;
	@XmlElement(required = true)
	protected String atmCardNo;
	@XmlElement(required = true)
	protected String name;
	protected String birthPlace;
	protected String birthDate;
	protected String gender;
	protected String mothersMaiden;
	protected String address;
	protected String city;
	protected String postalCode;
	protected String idNumber;
	protected String idExpiryDate;
	protected String homePhone;
	protected String workPhone;
	protected String mobilePhone;

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
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the birthPlace property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBirthPlace() {
		return birthPlace;
	}

	/**
	 * Sets the value of the birthPlace property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBirthPlace(String value) {
		this.birthPlace = value;
	}

	/**
	 * Gets the value of the birthDate property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBirthDate() {
		return birthDate;
	}

	/**
	 * Sets the value of the birthDate property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBirthDate(String value) {
		this.birthDate = value;
	}

	/**
	 * Gets the value of the gender property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets the value of the gender property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGender(String value) {
		this.gender = value;
	}

	/**
	 * Gets the value of the mothersMaiden property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMothersMaiden() {
		return mothersMaiden;
	}

	/**
	 * Sets the value of the mothersMaiden property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMothersMaiden(String value) {
		this.mothersMaiden = value;
	}

	/**
	 * Gets the value of the address property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the value of the address property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAddress(String value) {
		this.address = value;
	}

	/**
	 * Gets the value of the city property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the value of the city property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCity(String value) {
		this.city = value;
	}

	/**
	 * Gets the value of the postalCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * Sets the value of the postalCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPostalCode(String value) {
		this.postalCode = value;
	}

	/**
	 * Gets the value of the idNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdNumber() {
		return idNumber;
	}

	/**
	 * Sets the value of the idNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIdNumber(String value) {
		this.idNumber = value;
	}

	/**
	 * Gets the value of the idExpiryDate property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdExpiryDate() {
		return idExpiryDate;
	}

	/**
	 * Sets the value of the idExpiryDate property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIdExpiryDate(String value) {
		this.idExpiryDate = value;
	}

	/**
	 * Gets the value of the homePhone property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getHomePhone() {
		return homePhone;
	}

	/**
	 * Sets the value of the homePhone property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setHomePhone(String value) {
		this.homePhone = value;
	}

	/**
	 * Gets the value of the workPhone property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getWorkPhone() {
		return workPhone;
	}

	/**
	 * Sets the value of the workPhone property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setWorkPhone(String value) {
		this.workPhone = value;
	}

	/**
	 * Gets the value of the mobilePhone property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * Sets the value of the mobilePhone property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMobilePhone(String value) {
		this.mobilePhone = value;
	}

}
