package com.ga.project1.banking;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Transactions {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void withdrawMoney(Account account, double withdrawAmount, AccountData accountData, Customer customer) {
        account.resetDailyLimitNewDay();

        final double overDraftFee = 35;
        double currentBalance = account.getBalance();

        if (!account.isActive()) {
            System.out.println("Account is not active and has been deactivated due to repeated overdraft. Please resolve your balance and fee.");
            return;
        }
        if (withdrawAmount <= 0) {
            System.out.println("Error: withdraw amount has to be above 0.");
            return;
        }
        if(account.getWithdrawnToday() + withdrawAmount > account.getCardType().getWithdrawLimit()){
            System.out.println("Error: this exceeds your " + account.getCardType() + " daily withdrawal limit of $" + account.getCardType().getWithdrawLimit()
            + "(already withdrawn today: $" + account.getWithdrawnToday() + ")");
        }
        if (currentBalance <= 0 && withdrawAmount > 100) {
            System.out.println("Your account is overdrawn. You cannot withdraw more than $100 until your balance is resolved.");
            return;
        }

        double newBalance = currentBalance - withdrawAmount;

        if (newBalance < 0) {
            newBalance -= overDraftFee;
            account.setOverDraftCount(account.getOverDraftCount() + 1);
            System.out.println("Warning: This withdrawal has overdrawn your account. A $35 overdraft fee has been applied.");
            if (account.getOverDraftCount() >= 2) {
                account.setActive(false);
                System.out.println("Your account has been deactivated due to repeated overdrafts.");
            }
        }

        account.setBalance(newBalance);
        account.setWithdrawnToday(account.getWithdrawnToday() + withdrawAmount);
        accountData.updateAccount(account);
        TransactionDataLog(customer, account,"Withdraw", withdrawAmount);
        System.out.println("You have withdrawn " + withdrawAmount + ". Your current balance is: " + account.getBalance());
    }

    public static void depositMoney(Account account, double depositAmount, AccountData accountData, Customer customer, boolean isOwnAccount){
        if (!account.getStatus().equals("APPROVED")) {
            System.out.println("This account is still pending banker approval and cannot receive deposits yet.");
            return;
        }
        account.resetDailyLimitNewDay();

        if (depositAmount <= 0) {
            System.out.println("Error: deposit amount must be above 0.");
            return;
        }

        double limit = isOwnAccount ? account.getCardType().getDepositLimitOwn() : account.getCardType().getDepositLimit();

        if(account.getDepositedToday() + depositAmount > limit){
            System.out.println("Error: this exceeds your " + account.getCardType() + " daily deposit limit of $" + limit + " (already deposited today: $" + account.getDepositedToday() + ")");
            return;
        }
            account.setBalance(account.getBalance() + depositAmount);
            account.setDepositedToday(account.getDepositedToday() + depositAmount);
            accountData.updateAccount(account);
            if (!account.isActive() && account.getBalance() >= 0) {
                account.setActive(true);
                account.setOverDraftCount(0);
                System.out.println("Your balance has been resolved. Your account has been reactivated.");
            }
            TransactionDataLog(customer, account, isOwnAccount ? "Deposit" : "Deposit (to " + account.getAccountNumber() + ")", depositAmount);
            System.out.println(depositAmount + "BD has been deposited. Your current balance is: " + account.getBalance());
    }

    public static void transferMoney(Account fromAccount, String toAccountNumber, double amount, AccountData accountData, Customer customer){
        fromAccount.resetDailyLimitNewDay();
        Account toAccount = accountData.findByAccountNumber(toAccountNumber);
        if (!toAccount.getStatus().equals("APPROVED")) {
            System.out.println("The destination account is still pending approval and cannot receive transfers.");
            return;
        }
        if (toAccount == null) {
            System.out.println("Destination account not found.");
            return;
        }

        if (amount <= 0 || fromAccount.getBalance() < amount) {
            System.out.println("Error: invalid transfer amount or insufficient funds.");
            return;
        }

        boolean isOwnAccount = fromAccount.getCustomerID()== toAccount.getCustomerID();
        double limit = isOwnAccount
                ? fromAccount.getCardType().getTransferLimitOwn()
                : fromAccount.getCardType().getTransferLimit();
        double usedToday = isOwnAccount
                ? fromAccount.getTransferredOwnToday()
                : fromAccount.getTransferredToday();

        if (usedToday + amount > limit) {
            System.out.println("Error: this would exceed your " + fromAccount.getCardType() + " daily transfer limit of $" + limit + " (already transferred today: $" + usedToday + ")");
            return;
        }

        if (isOwnAccount) {
            fromAccount.setTransferredOwnToday(fromAccount.getTransferredOwnToday() + amount);
        } else {
            fromAccount.setTransferredToday(fromAccount.getTransferredToday() + amount);
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountData.updateAccount(fromAccount);
        accountData.updateAccount(toAccount);
        TransactionDataLog(customer, fromAccount, "Transfer Out (to " + toAccountNumber + ")", amount);
        System.out.println("Transferred " + amount + "BD to account " + toAccountNumber);
    }

    public static void TransactionDataLog(Customer customer,Account account, String type, double amount){
        String transactionFileName = "Customer-" + customer.getUsername() + "-" + customer.getCustomerID() + ".txt";
        String date = LocalDateTime.now().format(DATE_FORMAT);

        try (FileWriter writer = new FileWriter(transactionFileName, true)) {
            writer.write(
                    date + "," +
                    account.getAccountNumber()  + "," +
                    type + "," + amount + "," +
                    account.getBalance() + "," + account.getCardType() +
                    System.lineSeparator()
                    );
        } catch (IOException e) {
            System.out.println("Error saving transaction log.");
            e.printStackTrace();
        }
    }

    public static void customerTransactionHistory(Customer customer){
        String transactionFileName = "Customer-" + customer.getUsername() + "-" + customer.getCustomerID() + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(transactionFileName))) {
            System.out.println("\n====== Transaction History ======");
            String line;
            boolean exist = false;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    exist = true;
                    System.out.printf("Date: %s | Account: %s | Type: %-20s | Amount: %s | Balance After: %s%n | Card: %s%n",
                            data[0], data[1], data[2], data[3], data[4], data[5]);
                }
            }
            if (!exist) {
                System.out.println("No transactions recorded yet.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No transaction history found yet.");
        } catch (IOException e) {
            System.out.println("Error reading transaction history.");
        }
    }

    public static List<String> getTransactions(Customer customer) {
        String transactionFileName = "Customer-" + customer.getUsername() + "-" + customer.getCustomerID() + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(transactionFileName))) {
            return reader.lines()
                    .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            System.out.println("No transaction history found yet.");
        } catch (IOException e) {
            System.out.println("Error reading transaction history.");
        }
        return List.of();
    }


    public static List<String> filterTransactions(List<String> transactions, LocalDateTime start, LocalDateTime end) {
        return transactions.stream()
                .filter(line -> {
                    String[] data = line.split(",");
                    if (data.length != 6) {
                        return false;
                    }
                    try {
                        LocalDateTime transactionDate = LocalDateTime.parse(data[0], DATE_FORMAT);
                        return !transactionDate.isBefore(start) && transactionDate.isBefore(end);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public static void displayTransactions(List<String> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this period.");
            return;
        }
        for (String line : transactions) {
            String[] data = line.split(",");
            System.out.printf(
                    "Date: %s | Account: %s | Type: %-20s | Amount: %s | Balance After: %s | Card: %s%n",
                    data[0],
                    data[1],
                    data[2],
                    data[3],
                    data[4],
                    data[5]
            );
        }
    }

    public static void filterTransaction(Account account, Customer customer, int filterUserChoice) {
        LocalDate today = LocalDate.now();
        LocalDateTime start;
        LocalDateTime end;
        String day;

        switch (filterUserChoice) {
            case 1:
                start = today.atStartOfDay();
                end = LocalDateTime.now();
                day = "Today";
                break;
            case 2:
                start = today.minusDays(1).atStartOfDay();
                end = today.atStartOfDay();
                day = "Yesterday";
                break;
            case 3:
                start = LocalDateTime.now().minusDays(7);
                end = LocalDateTime.now();
                day = "Last 7 Days";
                break;
            case 4:
                start = LocalDateTime.now().minusDays(30);
                end = LocalDateTime.now();
                day = "Last 30 Days";
                break;
            case 5:
                start = today
                        .minusWeeks(1)
                        .with(DayOfWeek.SUNDAY)
                        .atStartOfDay();
                end = today
                        .with(DayOfWeek.SUNDAY)
                        .atStartOfDay();
                day = "Last Week";
                break;
            case 6:
                start = today
                        .minusMonths(1)
                        .withDayOfMonth(1)
                        .atStartOfDay();
                end = today
                        .withDayOfMonth(1)
                        .atStartOfDay();
                day = "Last Month";
                break;
            default:
                System.out.println("Invalid filter option.");
                return;
        }
        List<String> transactions = getTransactions(customer);
        List<String> filteredTransactions = filterTransactions(transactions, start, end);
        System.out.println("\n====== " + day + " Transactions ======");
        displayTransactions(filteredTransactions);
    }

}