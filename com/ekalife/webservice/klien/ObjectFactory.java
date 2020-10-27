package com.ekalife.webservice.klien;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.ekalife.webservice.klien package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.ekalife.webservice.klien
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link ApprovedAccount }
	 * 
	 */
	public ApprovedAccount createApprovedAccount() {
		return new ApprovedAccount();
	}

	/**
	 * Create an instance of {@link CreateAccount }
	 * 
	 */
	public CreateAccount createCreateAccount() {
		return new CreateAccount();
	}

	/**
	 * Create an instance of {@link RejectedAccount }
	 * 
	 */
	public RejectedAccount createRejectedAccount() {
		return new RejectedAccount();
	}

	/**
	 * Create an instance of {@link EnquiryApprovedAccount }
	 * 
	 */
	public EnquiryApprovedAccount createEnquiryApprovedAccount() {
		return new EnquiryApprovedAccount();
	}

	/**
	 * Create an instance of {@link CustomerDetail }
	 * 
	 */
	public CustomerDetail createCustomerDetail() {
		return new CustomerDetail();
	}

	/**
	 * Create an instance of {@link EnquiryRejectedAccount }
	 * 
	 */
	public EnquiryRejectedAccount createEnquiryRejectedAccount() {
		return new EnquiryRejectedAccount();
	}

	/**
	 * Create an instance of {@link CreateAccountResponse }
	 * 
	 */
	public CreateAccountResponse createCreateAccountResponse() {
		return new CreateAccountResponse();
	}

	/**
	 * Create an instance of {@link EnquiryApprovedAccountResponse }
	 * 
	 */
	public EnquiryApprovedAccountResponse createEnquiryApprovedAccountResponse() {
		return new EnquiryApprovedAccountResponse();
	}

	/**
	 * Create an instance of {@link EnquiryAccountbyCustcode }
	 * 
	 */
	public EnquiryAccountbyCustcode createEnquiryAccountbyCustcode() {
		return new EnquiryAccountbyCustcode();
	}

	/**
	 * Create an instance of {@link AccountbyCustcode }
	 * 
	 */
	public AccountbyCustcode createAccountbyCustcode() {
		return new AccountbyCustcode();
	}

	/**
	 * Create an instance of {@link EnquiryAccountbyCustcodeResponse }
	 * 
	 */
	public EnquiryAccountbyCustcodeResponse createEnquiryAccountbyCustcodeResponse() {
		return new EnquiryAccountbyCustcodeResponse();
	}

	/**
	 * Create an instance of {@link ChannelDetail }
	 * 
	 */
	public ChannelDetail createChannelDetail() {
		return new ChannelDetail();
	}

	/**
	 * Create an instance of {@link EnquiryRejectedAccountResponse }
	 * 
	 */
	public EnquiryRejectedAccountResponse createEnquiryRejectedAccountResponse() {
		return new EnquiryRejectedAccountResponse();
	}

	/**
	 * Create an instance of {@link CreateAccountFault }
	 * 
	 */
	public CreateAccountFault createCreateAccountFault() {
		return new CreateAccountFault();
	}

}
