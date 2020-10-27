package com.ekalife.elions.model;

import java.io.Serializable;
import java.util.List;


public class CmdInputBlacklist implements Serializable {

	private static final long serialVersionUID = 1L;
	

private Pemegang pemegang;
private Client client;
private BlackList blacklist;
private List<DetBlackList> detBlackListAll;
private BlackListFamily blacklistfamily;
private AddressNew addressNew;
private User currentUser;
private List<String> errorMessages;

public AddressNew getAddressNew() {
	return addressNew;
}
public void setAddressNew(AddressNew addressNew) {
	this.addressNew = addressNew;
}
public BlackList getBlacklist() {
	return blacklist;
}
public void setBlacklist(BlackList blacklist) {
	this.blacklist = blacklist;
}
public Client getClient() {
	return client;
}
public void setClient(Client client) {
	this.client = client;
}
public User getCurrentUser() {
	return currentUser;
}
public void setCurrentUser(User currentUser) {
	this.currentUser = currentUser;
}
public Pemegang getPemegang() {
	return pemegang;
}
public void setPemegang(Pemegang pemegang) {
	this.pemegang = pemegang;
}
public BlackListFamily getBlacklistfamily() {
	return blacklistfamily;
}
public void setBlacklistfamily(BlackListFamily blacklistfamily) {
	this.blacklistfamily = blacklistfamily;
}
public List<String> getErrorMessages() {
	return errorMessages;
}
public void setErrorMessages(List<String> errorMessages) {
	this.errorMessages = errorMessages;
}
public List<DetBlackList> getDetBlackListAll() {
	return detBlackListAll;
}
public void setDetBlackListAll(List<DetBlackList> detBlackListAll) {
	this.detBlackListAll = detBlackListAll;
}


}
