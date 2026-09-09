package com.ga.project1.banking;

public class Account {
    private String accountNumber;
    private String accountType;
    private double balance;
    private int customerID;
    public Account( int customerID, double balance, String accountType, String accountNumber) {
        this.customerID = customerID;
        this.balance = balance;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
}
