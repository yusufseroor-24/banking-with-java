package com.ga.project1.banking;

public class Transactions {

    public static void withdrawMoney(Account account, double withdrawAmount, AccountData accountData) {
        double currentBalance = account.getBalance();
        if (withdrawAmount > 0 && currentBalance >= withdrawAmount) {
            account.setBalance(currentBalance - withdrawAmount);
            accountData.updateAccount(account);
            System.out.println("You have withdrawn " + withdrawAmount + ". Your current balance is: " + account.getBalance());
        } else {
            System.out.println("Error: withdraw amount has to be above 0, or your account doesn't have sufficient balance.");
        }
    }

    public static void depositMoney(Account account, double depositAmount, AccountData accountData) {
        if (depositAmount > 0) {
            account.setBalance(account.getBalance() + depositAmount);
            accountData.updateAccount(account);
            System.out.println(depositAmount + "BD has been deposited. Your current balance is: " + account.getBalance());
        } else {
            System.out.println("Error: deposit amount must be above 0.");
        }
    }

    public static void transferMoney(Account fromAccount, String toAccountNumber, double amount, AccountData accountData) {
        Account toAccount = accountData.findByAccountNumber(toAccountNumber);

        if (toAccount == null) {
            System.out.println("Destination account not found.");
            return;
        }
        if (amount <= 0 || fromAccount.getBalance() < amount) {
            System.out.println("Error: invalid transfer amount or insufficient funds.");
            return;
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountData.updateAccount(fromAccount);
        accountData.updateAccount(toAccount);

        System.out.println("Transferred " + amount + "BD to account " + toAccountNumber);
    }
}