package com.ga.project1.banking;

public class Customer {
    private int customerID;
    private String name;
    private String username;
    private String passwordHash;
    private String salt;
    private String role;

    //new customers
    public Customer(String password, String username,
                    int customerID, String name, String role) {

        this.salt = PasswordUtil.generateSalt();
        this.passwordHash = PasswordUtil.hashPassword(password, this.salt);
        this.username = username;
        this.customerID = customerID;
        this.name = name;
        this.role = role;
    }
//    public Customer( String salt, String password, String username, int customerID, String name, String role) {
//        this.salt = PasswordUtil.generateSalt();
//        this.passwordHash = PasswordUtil.hashPassword(password, this.salt);
//        this.username = username;
//        this.customerID = customerID;
//        this.name = name;
//        this.role = role;
//    }

    //existing customers
    public Customer(String salt, int customerID, String name, String username, String passwordHash, String role) {
        this.salt = salt;
        this.customerID = customerID;
        this.name = name;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
