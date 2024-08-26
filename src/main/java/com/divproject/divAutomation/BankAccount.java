package com.divproject.divAutomation;

public class BankAccount {
	
	private String accountNumber;
	private int accountBalance;
	private String customerName;
	private String email;
	private String phoneNumber;
	
	
	
	public BankAccount(String accountNumber, int accountBalance, String customerName, String email,
			String phoneNumber) {
		super();
		this.accountNumber = accountNumber;
		this.accountBalance = accountBalance;
		this.customerName = customerName;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public int getAccountBalance() {
		return accountBalance;
	}
	public void setAccountBalance(int accountBalance) {
		this.accountBalance = accountBalance;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public void depostfunds() {
		System.out.println("Deposited Successfully");
	}
	public void withdrawfunds() {
		if (accountBalance<0) {
		System.out.println("insufficient funds");
	}else {
		System.out.println("wihtdrawn Successfully");
	}
	}

}
