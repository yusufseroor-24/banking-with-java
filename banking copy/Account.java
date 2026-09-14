package com.ga.project1.banking;

import java.util.Scanner;
import java.time.LocalDate;
import static com.ga.project1.banking.Bank.scanner;

public class Account {
    private String accountNumber;
    private String accountType;
    private double balance;
    private int customerID;
    private boolean isActive;
    private int overDraftCount;
    private CardType cardType;

    private double withdrawnToday;
    private double transferredToday;
    private double transferredOwnToday;
    private double depositedToday;
    private LocalDate lastResetDate;

    private String status;


    public Account( int customerID, double balance, String accountType, String accountNumber, boolean isActive, int overDraftCount, CardType cardType) {
        this(customerID, balance, accountType, accountNumber, isActive, overDraftCount, cardType,
                0, 0, 0, 0, LocalDate.now(), "PENDING");
    }

    public Account(int customerID, double balance, String accountType, String accountNumber,
                   boolean isActive, int overDraftCount, CardType cardType,
                   double withdrawnToday, double transferredToday, double transferredOwnToday,
                   double depositedToday, LocalDate lastResetDate, String status) {
        this.customerID = customerID;
        this.balance = balance;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.isActive = isActive;
        this.overDraftCount = overDraftCount;
        this.cardType = cardType;
        this.withdrawnToday = withdrawnToday;
        this.transferredToday = transferredToday;
        this.transferredOwnToday = transferredOwnToday;
        this.depositedToday = depositedToday;
        this.lastResetDate = lastResetDate;
        this.status = status;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getOverDraftCount() {
        return overDraftCount;
    }

    public void setOverDraftCount(int overDraftCount) {
        this.overDraftCount = overDraftCount;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public LocalDate getLastResetDate() {
        return lastResetDate;
    }

    public void setLastResetDate(LocalDate lastResetDate) {
        this.lastResetDate = lastResetDate;
    }

    public double getDepositedToday() {
        return depositedToday;
    }

    public void setDepositedToday(double depositedToday) {
        this.depositedToday = depositedToday;
    }

    public double getTransferredOwnToday() {
        return transferredOwnToday;
    }

    public void setTransferredOwnToday(double transferredOwnToday) {
        this.transferredOwnToday = transferredOwnToday;
    }

    public double getTransferredToday() {
        return transferredToday;
    }

    public void setTransferredToday(double transferredToday) {
        this.transferredToday = transferredToday;
    }

    public double getWithdrawnToday() {
        return withdrawnToday;
    }

    public void setWithdrawnToday(double withdrawnToday) {
        this.withdrawnToday = withdrawnToday;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static void upgradeCard(Account account, String upgradeChoice){
            if (upgradeChoice.equalsIgnoreCase("TITANIUM")) {
                account.setCardType(CardType.TITANIUM);
                System.out.println("Your account has been upgraded to " + account.getCardType());
            }
            else if (upgradeChoice.equalsIgnoreCase("PLATINUM")) {
                account.setCardType(CardType.PLATINUM);
                System.out.println("Your account has been upgraded to " + account.getCardType());
            } else {
                System.out.println("Invalid card type.");
            }
    }

    public void resetDailyLimitNewDay(){
        LocalDate today = LocalDate.now();
        if (!today.equals(lastResetDate)) {
            withdrawnToday = 0;
            transferredToday = 0;
            transferredOwnToday = 0;
            depositedToday = 0;
            lastResetDate = today;
        }
    }
}
