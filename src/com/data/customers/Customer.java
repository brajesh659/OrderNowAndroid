package com.data.customers;


public class Customer {

	private String customerId;
	private String phoneNumber;
	private String parseId;
	private String facebookId;
	private String firstName;
	private String lastName;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getNumber() {
		return phoneNumber;
	}

	public void setNumber(String number) {
		this.phoneNumber = number;
	}

	public String getParseId() {
		return parseId;
	}

	public void setParseId(String parseId) {
		this.parseId = parseId;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public Customer(Customer obj1) {
		this(obj1.customerId, obj1.firstName, obj1.lastName, obj1.phoneNumber,
				obj1.parseId, obj1.facebookId);
	}

	public Customer(String customerId, String firstName, String lastName, String phoneNumber,
			String parseId, String facebookId) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.parseId = parseId;
		this.facebookId = facebookId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", firstName="
				+ firstName + ", lastName=" + lastName +", phoneNumber=" + phoneNumber + ", parseId="
				+ parseId + ", facebookId=" + facebookId + "]";
	}
}
